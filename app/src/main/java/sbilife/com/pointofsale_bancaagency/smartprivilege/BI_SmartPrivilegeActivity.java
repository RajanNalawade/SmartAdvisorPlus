package sbilife.com.pointofsale_bancaagency.smartprivilege;

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
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TableLayout;
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
import sbilife.com.pointofsale_bancaagency.common.DesimalDigitsInputFilter;
import sbilife.com.pointofsale_bancaagency.common.ProductInfo;
import sbilife.com.pointofsale_bancaagency.common.SimpleCrypto;
import sbilife.com.pointofsale_bancaagency.common.StorageUtils;
import sbilife.com.pointofsale_bancaagency.common.Success;
import sbilife.com.pointofsale_bancaagency.needanalysis.NeedAnalysisActivity;
import sbilife.com.pointofsale_bancaagency.new_bussiness.ProductBIBean;
import sbilife.com.pointofsale_bancaagency.utility.GridHeight;

@SuppressWarnings("deprecation")
public class BI_SmartPrivilegeActivity extends AppCompatActivity implements
        OnEditorActionListener {

    private NeedAnalysisBIService NABIObj;
    private NA_CBI_bean na_cbi_bean;
    private File needAnalysispath, newFile;
    private String agentcode, agentMobile, agentEmail, userType;
    private int needAnalysis_flag = 0;
    private String na_input = null;
    private String na_output = null;
    private String na_dob = "";
    private int flag = 0;

    // UI Elements
    private CheckBox isStaffDisc;
    private CheckBox selViewSFIN;
    private Spinner ageInYears;
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
    private EditText percent_Top300Fund;
    private EditText percent_PureFund;
    private EditText percent_MidcapFund;
    private EditText percent_CorporateBondFund;
    private Button btnSubmit;
    private Button back;
    private TableRow tbSFINEquityFund;
    private TableRow tbSFINBondFund;
    private TableRow tbSFINTop300Fund;
    private TableRow tbSFINEquityOptFund;
    private TableRow tbSFINGrowthFund;
    private TableRow tbSFINBalancedFund;
    private TableRow tbSFINMoneyMktFund;
    private TableRow tbSFINPureFund, tbSFINMoneyMarketFund2;
    private TableRow tbSFINMidcapFund, tbSFINCorporateBondFund, tbSFINBondOptimiserFund2;

    private String output = "", input = "", staffdiscount = "";

    private TableLayout tablelayoutSmartPrivilegeProposerTitle;
    // Context con;
    private double annualPremium;
    // For BI

    private StringBuilder retVal;
    private StringBuilder bussIll = null;


    private String QuatationNumber;
    private String planName = "";

    // class declaration
    private CommonForAllProd commonForAllProd;
    private SmartPrivilegeBean smartPrivilegeBean;
    SmartPrivilegeProperties prop;

    // variable declaration
    private DecimalFormat currencyFormat;
    private AlertDialog.Builder showAlert;
    private String effectivePremium;

    private Spinner spnr_bi_smart_privilege_proposer_title;
    private Spinner selGender;
    private Spinner spinnerProposerGender;
    private EditText edt_bi_smart_privilege_proposer_first_name;
    private EditText edt_bi_smart_privilege_proposer_middle_name;
    private EditText edt_bi_smart_privilege_proposer_last_name;
    private Button btn_bi_smart_privilege_proposer_date;
    private EditText edt_bi_smart_privilege_proposer_age;

    private EditText edt_bi_smart_privilege_life_assured_first_name;
    private EditText edt_bi_smart_privilege_life_assured_middle_name;
    private EditText edt_bi_smart_privilege_life_assured_last_name;
    private EditText edt_bi_smart_privilege_life_assured_age;

    private Spinner spnr_bi_smart_privilege_life_assured_title;

    private Button btn_bi_smart_privilege_life_assured_date;

    private String lifeAssured_Title = "";
    private String lifeAssured_First_Name = "";
    private String lifeAssured_Middle_Name = "";
    private String lifeAssured_Last_Name = "";
    private String name_of_life_assured = "";
    private String lifeAssured_date_of_birth = "";


    private String age_entry = "";

    private String maturityAge = "";

    private String gender = "";
    private String gender_proposer = "";

    private String planType = "";

    private String annPrem = "";
    private String sumAssured = "";

    private String mode_of_policy = "";
    private String plan = "";
    private String premPayTerm = "";

    private String perInvEquityFund = "";
    private String perInvEquityOptimiserFund = "";
    private String perInvgrowthFund = "";
    private String perInvBalancedFund = "";
    private String perInvBondFund = "";
    private String perInvMoneyMarketFund = "";
    private String perInvTop300Fund = "";
    private String perInvPureFund = "";
    private String perInvMidcapFund = "";
    private String policyTerm = "";
    private String perInvbondOptimiserFund2 = "", perInvmoneyMarketFund2 = "";

    private String noOfYrElapsed = "";
    private String redInYieldNoYr = "";

    private String perInvCorporateBondFund = "";

    private List<M_BI_SmartPrivilegeAdapterCommon> list_data;
    private List<M_BI_SmartPrivilegeAdapterCommon2> list_data1;
    private List<M_BI_SmartPrivilegeAdapterCommon2> list_data2;

    // For Bi Dialog
    private ParseXML prsObj;
    private String name_of_proposer = "";
    private String name_of_person = "";
    private String place2 = "";
    private String date1 = "";
    private String date2 = "";
    private String agent_sign = "";
    private String proposer_sign = "";
    String strProposerAge = "";

    private File mypath;

    private String proposer_Title = "";
    private String proposer_First_Name = "";
    private String proposer_Middle_Name = "";
    private String proposer_Last_Name = "";
    private String proposer_Is_Same_As_Life_Assured = "y";

    private Button btn_MarketingOfficalDate;
    private Button btn_PolicyholderDate;

    private ImageButton Ibtn_signatureofMarketing;
    private ImageButton Ibtn_signatureofPolicyHolders;

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

    // Database Related Variable
    private DatabaseHelper db;

    // To Store User Info and sync info

    private String proposer_date_of_birth = "";


    private CommonForAllProd obj;
    private int b;

    private ScrollView sv_smart_privilege_main;

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

    private double[] arrGuaranteedAddition_AC;
    private double[] arrGuaranteedAddition_AU;

    private int minPolicyTerm = 5, maxPolicyTerm = 30;
    private final int minPremiumPayingTerm = 5;
    private int maxPremiumPayingTerm = minPolicyTerm;
    private String netYield = "";
    private String reductionInYield = "";
    private String MinesOccuInterest = "";
    private String premfreqMode = "";
    private String samf = "";
    private String premiumAmount = "";

    private String product_Code, product_UIN, product_cateogory, product_type;
    private String bankUserType = "", mode = "";

    String str_kerla_discount = "No";

    private Context context;

    private String Check = "";
    private CommonMethods commonMethods;
    private StorageUtils mStorageUtils;
    private ImageButton imageButtonSmartPrivilegeProposerPhotograph;

    String STR_PLAN_AB = "B";
    TextView tv_bond_optimiser_fund2, tv_bond_optimiser_fund2_sfin, tv_bond_optimiser_fund2_sfin_uin,
            tv_money_market_fund2, tv_money_market_fund2_sfin, tv_money_market_fund2_sfin_uin;


    EditText percent_bondOptimiserFund2, percent_moneyMarketFund2;

    private LinearLayout linearlayoutThirdpartySignature;
    private LinearLayout linearlayoutAppointeeSignature;
    private ImageButton Ibtn_signatureofThirdParty, Ibtn_signatureofAppointee,
            Ibtn_signatureofLifeAssured;
    private String thirdPartySign = "", appointeeSign = "",
            proposerAsLifeAssuredSign = "", Company_policy_surrender_dec = "";

    private CheckBox cb_kerladisc;
    private String agentdeclaration;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.bi_smartprivilegemain);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);

        db = new DatabaseHelper(this);

        context = this;
        commonMethods = new CommonMethods();
        mStorageUtils = new StorageUtils();
        commonMethods.setActionbarLayout(this);

        initialiseDate();
        Date();
        UI_Declaration();
        // Intent intent = getIntent();
        // frmProductHome = intent.getStringExtra("DashBoard");


        NABIObj = new NeedAnalysisBIService(this);
        prsObj = new ParseXML();
        Intent intent = getIntent();

        //Added Tushar Kadam Kerla Applicable Tax 7-Jun-2019
	/*	TableRow tablerowKerlaDiscount = findViewById(R.id.tablerowKerlaDiscount);
		cb_kerladisc =  (CheckBox) findViewById(R.id.cb_kerladisc);
		commonMethods.setKerlaDiscount(context, tablerowKerlaDiscount, cb_kerladisc);*/
        //End Added Tushar Kadam Kerla Applicable Tax 7-Jun-2019
        String na_flag = intent.getStringExtra("NAFlag");

        if (na_flag != null) {
            if (na_flag.equalsIgnoreCase("1")) {
                needAnalysis_flag = 1;
                na_input = intent.getStringExtra("NaInput");
                na_output = intent.getStringExtra("NaOutput");
                na_dob = intent.getStringExtra("custDOB");
                gender = intent.getStringExtra("custGender");

                bankUserType = intent.getStringExtra("Other");

                if (bankUserType != null) {

                } else {
                    bankUserType = "";
                }

                tv_bond_optimiser_fund2 = (TextView) findViewById(R.id.tv_bond_optimiser_fund2);
                tv_bond_optimiser_fund2_sfin = (TextView) findViewById(R.id.tv_bond_optimiser_fund2_sfin);
                tv_bond_optimiser_fund2_sfin_uin = (TextView) findViewById(R.id.tv_bond_optimiser_fund2_sfin_uin);


                tv_money_market_fund2 = (TextView) findViewById(R.id.tv_money_market_fund2);
                tv_money_market_fund2_sfin = (TextView) findViewById(R.id.tv_money_market_fund2_sfin);
                tv_money_market_fund2_sfin_uin = (TextView) findViewById(R.id.tv_money_market_fund2_sfin_uin);

                if (STR_PLAN_AB.equals("A")) {
                    tv_bond_optimiser_fund2.setText("% to be invested for Bond Optimiser Fund II");
                    tv_bond_optimiser_fund2_sfin.setText("Bond Optimiser Fund II");
                    tv_bond_optimiser_fund2_sfin_uin.setText("ULIF037160919BONDOPFND2111");

                    tv_money_market_fund2.setText("% to be invested for Money Market Fund II");
                    tv_money_market_fund2_sfin.setText("Money Market Fund II");
                    tv_money_market_fund2_sfin_uin.setText("ULIF036160919MONMKTFND2111");
                } else if (STR_PLAN_AB.equals("B")) {
                    tv_bond_optimiser_fund2.setText("% to be invested for Bond Optimiser Fund");
                    tv_bond_optimiser_fund2_sfin.setText("Bond Optimiser Fund");
                    tv_bond_optimiser_fund2_sfin_uin.setText("ULIF032290618BONDOPTFND111");

                    tv_money_market_fund2.setText("% to be invested for Money Market Fund");
                    tv_money_market_fund2_sfin.setText("Money Market Fund");
                    tv_money_market_fund2_sfin_uin.setText("ULIF005010206MONYMKTFND111");
                }

                try {
                    agentcode = SimpleCrypto.decrypt("SBIL", db.GetUserCode());

                    agentMobile = SimpleCrypto
                            .decrypt("SBIL", db.GetMobileNo());
                    agentEmail = SimpleCrypto.decrypt("SBIL", db.GetEmailId());
                    userType = SimpleCrypto.decrypt("SBIL", db.GetUserType());

                    ProductInfo prodInfoObj = new ProductInfo();
                    planName = "Smart Privilege";
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

        obj = new CommonForAllProd();
        prsObj = new ParseXML();

        String str_usertype = "";
        try {
            str_usertype = SimpleCrypto.decrypt("SBIL",
                    db.GetUserType());

        } catch (Exception e) {
            e.printStackTrace();
        }
        TableRow tr_staff_disc = findViewById(R.id.tr_smart_privilege_staff_disc);

		/*if (str_usertype.equalsIgnoreCase("BAP")
				|| str_usertype.equalsIgnoreCase("CAG")) {
			tr_staff_disc.setVisibility(View.GONE);
		}*/

        list_data = new ArrayList<M_BI_SmartPrivilegeAdapterCommon>();
        list_data1 = new ArrayList<M_BI_SmartPrivilegeAdapterCommon2>();
        list_data2 = new ArrayList<M_BI_SmartPrivilegeAdapterCommon2>();

        Date();
        // setBIInputGui();
        // getBasicDetail();
        prop = new SmartPrivilegeProperties();
    }

    private void UI_Declaration() {

        spnr_bi_smart_privilege_life_assured_title = findViewById(R.id.spnr_bi_smart_privilege_life_assured_title);

        edt_bi_smart_privilege_life_assured_first_name = findViewById(R.id.edt_bi_smart_privilege_life_assured_first_name);
        edt_bi_smart_privilege_life_assured_middle_name = findViewById(R.id.edt_bi_smart_privilege_life_assured_middle_name);
        edt_bi_smart_privilege_life_assured_last_name = findViewById(R.id.edt_bi_smart_privilege_life_assured_last_name);
        selGender = findViewById(R.id.selGender);
        spinnerProposerGender = findViewById(R.id.spinnerProposerGender);
//		selGender.setClickable(false);
//		selGender.setEnabled(false);

        btn_bi_smart_privilege_life_assured_date = findViewById(R.id.btn_bi_smart_privilege_life_assured_date);
        edt_bi_smart_privilege_life_assured_age = findViewById(R.id.edt_bi_smart_privilege_life_assured_age);

        tablelayoutSmartPrivilegeProposerTitle = findViewById(R.id.tablelayoutSmartPrivilegeProposerTitle);
        spnr_bi_smart_privilege_proposer_title = findViewById(R.id.spnr_bi_smart_privilege_proposer_title);
        edt_bi_smart_privilege_proposer_first_name = findViewById(R.id.edt_bi_smart_privilege_proposer_first_name);
        edt_bi_smart_privilege_proposer_middle_name = findViewById(R.id.edt_bi_smart_privilege_proposer_middle_name);
        edt_bi_smart_privilege_proposer_last_name = findViewById(R.id.edt_bi_smart_privilege_proposer_last_name);
        btn_bi_smart_privilege_proposer_date = findViewById(R.id.btn_bi_smart_privilege_proposer_date);

        edt_bi_smart_privilege_proposer_age = findViewById(R.id.edt_bi_smart_privilege_proposer_age);

        sv_smart_privilege_main = findViewById(R.id.sv_smart_privilege_main);

        btnSubmit = findViewById(R.id.btnSubmit);
        back = findViewById(R.id.back);

        edt_proposerdetail_basicdetail_contact_no = findViewById(R.id.edt_smart_builider_contact_no);
        edt_proposerdetail_basicdetail_Email_id = findViewById(R.id.edt_smart_builider_Email_id);
        edt_proposerdetail_basicdetail_ConfirmEmail_id = findViewById(R.id.edt_smart_builider_ConfirmEmail_id);

        premiumAmt = findViewById(R.id.premium_Amt);

        TableRow tablerowKerlaDiscount = findViewById(R.id.tablerowKerlaDiscount);
        cb_kerladisc = (CheckBox) findViewById(R.id.cb_kerladisc);
        commonMethods.setKerlaDiscount(context, tablerowKerlaDiscount, cb_kerladisc);

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
        percent_Top300Fund = findViewById(R.id.top300Fund);
        percent_PureFund = findViewById(R.id.pureFund);
        percent_MidcapFund = findViewById(R.id.midcapFund);
        percent_bondOptimiserFund2 = (EditText) findViewById(R.id.bondOptimiserFund2);
        percent_moneyMarketFund2 = (EditText) findViewById(R.id.moneyMarketFund2);
        percent_CorporateBondFund = (EditText) findViewById(R.id.corporateBondFund);

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

        tbSFINPureFund = (TableRow) findViewById(R.id.tbSFINPureFund);
        tbSFINMidcapFund = (TableRow) findViewById(R.id.tbSFINMidcapFund);
        tbSFINBondOptimiserFund2 = (TableRow) findViewById(R.id.tbSFINBondOptimiserFund2);
        tbSFINCorporateBondFund = (TableRow) findViewById(R.id.tbSFINCorporateBondFund);
        tbSFINMoneyMarketFund2 = (TableRow) findViewById(R.id.tbSFINMoneyMarketFund2);


        TextView inputActivityHeader = findViewById(R.id.txt_input_activityheader);
        isStaffDisc = findViewById(R.id.cb_staffdisc);
        // Age
        ageInYears = findViewById(R.id.age);

        selPlan = findViewById(R.id.planType);
        txtPremiumFreqMode = findViewById(R.id.txt_premium_frequency_mode);
        selPremiumFrequencyMode = findViewById(R.id.premium_frequency_mode);
        txtPremiumPayingTerm = findViewById(R.id.txt_premiumPayingTerm);
        premPayingTerm = findViewById(R.id.premiumPayingTerm);
        selPolicyTerm = findViewById(R.id.policyTerm);

        edt_bi_smart_privilege_life_assured_first_name
                .setOnEditorActionListener(this);
        edt_bi_smart_privilege_life_assured_middle_name
                .setOnEditorActionListener(this);
        edt_bi_smart_privilege_life_assured_last_name
                .setOnEditorActionListener(this);
        edt_bi_smart_privilege_proposer_first_name
                .setOnEditorActionListener(this);
        edt_bi_smart_privilege_proposer_middle_name
                .setOnEditorActionListener(this);
        edt_bi_smart_privilege_proposer_last_name
                .setOnEditorActionListener(this);

        premiumAmt.setOnEditorActionListener(this);
        SAMF.setOnEditorActionListener(this);
        noOfYearsElapsedSinceInception.setOnEditorActionListener(this);
        percent_EquityFund.setOnEditorActionListener(this);
        percent_EquityOptFund.setOnEditorActionListener(this);
        percent_GrowthFund.setOnEditorActionListener(this);
        percent_BalancedFund.setOnEditorActionListener(this);
        percent_BondFund.setOnEditorActionListener(this);
        percent_Top300Fund.setOnEditorActionListener(this);
        percent_PureFund.setOnEditorActionListener(this);
        percent_MidcapFund.setOnEditorActionListener(this);
        percent_bondOptimiserFund2.setOnEditorActionListener(this);
        percent_moneyMarketFund2.setOnEditorActionListener(this);
        percent_CorporateBondFund.setOnEditorActionListener(this);
        setFocusable(spnr_bi_smart_privilege_life_assured_title);

        spnr_bi_smart_privilege_life_assured_title.requestFocus();

        setSpinnerAndOtherListner();
        setSpinner_Value();

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
                        // TODO Auto-generated method stub
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

        edt_proposerdetail_basicdetail_contact_no
                .setOnEditorActionListener(this);
        edt_proposerdetail_basicdetail_Email_id.setOnEditorActionListener(this);
        edt_proposerdetail_basicdetail_ConfirmEmail_id
                .setOnEditorActionListener(this);
    }

    private void setSpinner_Value() {
        list_data = new ArrayList<>();

        commonMethods.fillSpinnerValue(context, spnr_bi_smart_privilege_proposer_title,
                Arrays.asList(getResources().getStringArray(R.array.arr_select_title)));

        commonMethods.fillSpinnerValue(context, spnr_bi_smart_privilege_life_assured_title,
                Arrays.asList(getResources().getStringArray(R.array.arr_select_title)));

        // class declaration
        commonForAllProd = new CommonForAllProd();
        SmartPrivilegeProperties prop = new SmartPrivilegeProperties();

        // variable declaration
        currencyFormat = new DecimalFormat("##,##,##,###");
        showAlert = new AlertDialog.Builder(this);

        // UI elements initialization

        ageInYears.setClickable(false);
        ageInYears.setEnabled(false);

        String[] ageList = new String[59];
        for (int i = 7; i <= 65; i++) {
            ageList[i - 7] = i + "";
        }
        ArrayAdapter<String> ageAdapter = new ArrayAdapter<>(
                getApplicationContext(), R.layout.spinner_item, ageList);
        ageAdapter.setDropDownViewResource(R.layout.spinner_item1);
        ageInYears.setAdapter(ageAdapter);
        ageAdapter.notifyDataSetChanged();

        // gender

        // selGender.setClickable(false);
        // selGender.setEnabled(false);
        String[] genderArr = new String[]{"Male", "Female", "Third Gender"};
        ArrayAdapter<String> genderAdapter = new ArrayAdapter<>(
                getApplicationContext(), R.layout.spinner_item, genderArr);
        genderAdapter.setDropDownViewResource(R.layout.spinner_item1);
        selGender.setAdapter(genderAdapter);
        spinnerProposerGender.setAdapter(genderAdapter);
        genderAdapter.notifyDataSetChanged();

        // plan type

        String[] planList = new String[]{"Single", "Regular", "Limited"};
        ArrayAdapter<String> planAdapter = new ArrayAdapter<>(
                getApplicationContext(), R.layout.spinner_item, planList);
        planAdapter.setDropDownViewResource(R.layout.spinner_item1);
        selPlan.setAdapter(planAdapter);
        /* Changes done by vrushali on 9/4/2015 */
        // selPlan.setSelection(planAdapter.getPosition("Limited"));
        planAdapter.notifyDataSetChanged();

        // premium frequency mode

        String[] premFreq = new String[]{"Yearly", "Half Yearly",
                "Quarterly", "Monthly"};
        ArrayAdapter<String> premFreqAdapter = new ArrayAdapter<>(
                getApplicationContext(), R.layout.spinner_item, premFreq);
        premFreqAdapter.setDropDownViewResource(R.layout.spinner_item1);
        selPremiumFrequencyMode.setAdapter(premFreqAdapter);
        premFreqAdapter.notifyDataSetChanged();

        // Premium Paying term

        String[] premPayingTermList = new String[maxPremiumPayingTerm
                - minPremiumPayingTerm + 1];
        for (int i = minPremiumPayingTerm; i < maxPremiumPayingTerm; i++) {
            premPayingTermList[i - minPremiumPayingTerm] = i + "";
        }

        ArrayAdapter<String> premPayingTermAdapter = new ArrayAdapter<>(
                getApplicationContext(), R.layout.spinner_item,
                premPayingTermList);
        premPayingTermAdapter.setDropDownViewResource(R.layout.spinner_item1);
        premPayingTerm.setAdapter(premPayingTermAdapter);
        premPayingTermAdapter.notifyDataSetChanged();

        // policy Term

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

        selPolicyTerm.setSelection(5);
    }

    private void setSpinnerAndOtherListner() {

        cb_kerladisc.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cb_kerladisc.isChecked()) {
                    str_kerla_discount = "Yes";
                    clearFocusable(cb_kerladisc);
                    setFocusable(spnr_bi_smart_privilege_life_assured_title);
                    spnr_bi_smart_privilege_life_assured_title.requestFocus();
                } else {
                    str_kerla_discount = "No";
                    clearFocusable(cb_kerladisc);
                    setFocusable(spnr_bi_smart_privilege_life_assured_title);
                    spnr_bi_smart_privilege_life_assured_title.requestFocus();
                }
            }
        });

        selPremiumFrequencyMode
                .setOnItemSelectedListener(new OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int arg2, long arg3) {
                        // TODO Auto-generated method stub
                        if (selPlan.getSelectedItem().toString()
                                .equalsIgnoreCase("Limited")) {

                            setFocusable(premPayingTerm);
                            premPayingTerm.requestFocus();
                        } else {
                            setFocusable(selPolicyTerm);
                            selPolicyTerm.requestFocus();
                        }
                        updatePremiumAmtLabel();

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                        // TODO Auto-generated method stub

                    }
                });

        // plan type
        selPlan.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int pos,
                                       long id) {
                // TODO Auto-generated method stub

                if (selPlan.getSelectedItem().toString().equals("Regular")
                        || selPlan.getSelectedItem().toString()
                        .equals("Limited")) {
                    if (proposer_Is_Same_As_Life_Assured.equals("")) {
                        showAlert
                                .setMessage("Please Select Whether Proposer Is Same As Life Assured");
                        showAlert.setNeutralButton("OK",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                    }
                                });
                        showAlert.show();
                    } else if (Integer.parseInt(ageInYears.getSelectedItem()
                            .toString()) > 60) {

                        if (Integer
                                .parseInt(!edt_bi_smart_privilege_proposer_age
                                        .getText().toString().equals("") ? edt_bi_smart_privilege_proposer_age
                                        .getText().toString() : "0") < 18) {
                            showAlert
                                    .setMessage("Life Assured Date of Birth Must Be Less Than 60");

                            showAlert.setNeutralButton("OK",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(
                                                DialogInterface dialog,
                                                int which) {
                                        }
                                    });
                            showAlert.show();
                            btn_bi_smart_privilege_proposer_date
                                    .setText("Select Date");
                        } else {
                            showAlert
                                    .setMessage("Proposer Date of Birth Must Be Less Than 60");

                            showAlert.setNeutralButton("OK",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(
                                                DialogInterface dialog,
                                                int which) {
                                        }
                                    });
                            showAlert.show();
                            btn_bi_smart_privilege_life_assured_date
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
                updatePremiumPayingTerm(Integer.parseInt(selPolicyTerm
                        .getSelectedItem().toString()));
                //effectivePremium = getEffectivePremium();

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

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }

        });

        // premium paying term
        premPayingTerm.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int pos,
                                       long id) {
                // TODO Auto-generated method stub
                updatePolicyTermLabel();
                clearFocusable(premPayingTerm);
                setFocusable(selPolicyTerm);
                selPolicyTerm.requestFocus();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });

        // age in years
        ageInYears.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int pos,
                                       long id) {
                // TODO Auto-generated method stub
                updatePolicyTermLabel();
                updateSAMFlabel();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }

        });

        // policy term
        selPolicyTerm.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int pos,
                                       long id) {
                // TODO Auto-generated method stub
                valMaturityAge();
                updateSAMFlabel();
                updatenoOfYearsElapsedSinceInception();
                updatePremiumPayingTerm(Integer.parseInt(selPolicyTerm
                        .getSelectedItem().toString()));
                // noOfYearsElapsedSinceInception.setText(selPolicyTerm
                // .getSelectedItem().toString().trim());

                if (b == 1) {
                    edt_bi_smart_privilege_life_assured_first_name
                            .requestFocus();
                    b = 0;
                } else {

                    clearFocusable(selPolicyTerm);
                    if (edt_bi_smart_privilege_life_assured_first_name
                            .getText().toString().equals("")) {

                        setFocusable(edt_bi_smart_privilege_life_assured_first_name);
                        edt_bi_smart_privilege_life_assured_first_name
                                .requestFocus();

                    } else {
                        setFocusable(premiumAmt);
                        premiumAmt.requestFocus();
                    }

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }

        });

        // premium amount
        premiumAmt.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View arg0, boolean arg1) {
                // TODO Auto-generated method stub
                updatePolicyTermLabel();
                if (!premiumAmt.getText().equals("")) {
                    //effectivePremium = getEffectivePremium();
                }
            }
        });

        selViewSFIN
                .setOnCheckedChangeListener(new OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton buttonView,
                                                 boolean isChecked) {
                        // TODO Auto-generated method stub

                        if (isChecked) {
                            tbSFINEquityFund.setVisibility(View.VISIBLE);
                            tbSFINBondFund.setVisibility(View.VISIBLE);
                            tbSFINTop300Fund.setVisibility(View.VISIBLE);
                            tbSFINEquityOptFund.setVisibility(View.VISIBLE);
                            tbSFINGrowthFund.setVisibility(View.VISIBLE);
                            tbSFINBalancedFund.setVisibility(View.VISIBLE);
                            tbSFINMoneyMktFund.setVisibility(View.GONE);
                            tbSFINPureFund.setVisibility(View.VISIBLE);
                            tbSFINMidcapFund.setVisibility(View.VISIBLE);
                            tbSFINBondOptimiserFund2.setVisibility(View.VISIBLE);
                            tbSFINMoneyMarketFund2.setVisibility(View.VISIBLE);
                            tbSFINCorporateBondFund.setVisibility(View.VISIBLE);
                        } else {
                            tbSFINEquityFund.setVisibility(View.GONE);
                            tbSFINBondFund.setVisibility(View.GONE);
                            tbSFINTop300Fund.setVisibility(View.GONE);
                            tbSFINEquityOptFund.setVisibility(View.GONE);
                            tbSFINGrowthFund.setVisibility(View.GONE);
                            tbSFINBalancedFund.setVisibility(View.GONE);
                            tbSFINMoneyMktFund.setVisibility(View.GONE);

                            tbSFINPureFund.setVisibility(View.GONE);
                            tbSFINMidcapFund.setVisibility(View.GONE);
                            tbSFINCorporateBondFund.setVisibility(View.GONE);
                            tbSFINBondOptimiserFund2.setVisibility(View.GONE);
                            tbSFINMoneyMarketFund2.setVisibility(View.GONE);

                        }

                    }
                });

        spnr_bi_smart_privilege_proposer_title
                .setOnItemSelectedListener(new OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int position, long id) {
                        // TODO Auto-generated method stub
                        if (position == 0) {
                            proposer_Title = "";
                        } else {
                            proposer_Title = spnr_bi_smart_privilege_proposer_title
                                    .getSelectedItem().toString();

                            if (proposer_Title.equalsIgnoreCase("Mr.")) {
                                // selGender.setSelection(getIndex(selGender,
                                // "Male"));
                            } else if (proposer_Title.equalsIgnoreCase("Ms.")
                                    || proposer_Title.equalsIgnoreCase("Mrs.")) {
                                // selGender.setSelection(getIndex(selGender,
                                // "Female"));
                            }
                            setFocusable(edt_bi_smart_privilege_proposer_first_name);

                            edt_bi_smart_privilege_proposer_first_name
                                    .requestFocus();

                        }

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                        // TODO Auto-generated method stub

                    }
                });

        spnr_bi_smart_privilege_life_assured_title
                .setOnItemSelectedListener(new OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int position, long id) {
                        // TODO Auto-generated method stub
                        if (position == 0) {
                            lifeAssured_Title = "";
                        } else {
                            lifeAssured_Title = spnr_bi_smart_privilege_life_assured_title
                                    .getSelectedItem().toString();
						/*	if (lifeAssured_Title.equalsIgnoreCase("Mr.")) {
								selGender.setSelection(
										getIndex(selGender, "Male"), false);
							} else if (lifeAssured_Title
									.equalsIgnoreCase("Ms.")) {
								selGender.setSelection(
										getIndex(selGender, "Female"), false);
							} else if (lifeAssured_Title
									.equalsIgnoreCase("Mrs.")) {
								selGender.setSelection(
										getIndex(selGender, "Female"), false);
								iv_women.setImageDrawable(getResources().getDrawable(
										R.drawable.female_selected));
								iv_men.setImageDrawable(getResources().getDrawable(
										R.drawable.male_nonselected));
								gender="Female";
							}*/
                            clearFocusable(spnr_bi_smart_privilege_life_assured_title);

                            setFocusable(edt_bi_smart_privilege_life_assured_first_name);

                            edt_bi_smart_privilege_life_assured_first_name
                                    .requestFocus();

                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                        // TODO Auto-generated method stub

                    }
                });

        // staff disc
        isStaffDisc.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                if (isStaffDisc.isChecked()) {
                    isStaffDisc.setChecked(true);
                    clearFocusable(spnr_bi_smart_privilege_life_assured_title);
                    setFocusable(spnr_bi_smart_privilege_life_assured_title);
                    spnr_bi_smart_privilege_life_assured_title.requestFocus();
                }

            }
        });

        // go button

        back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
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

                gender_proposer = spinnerProposerGender.getSelectedItem().toString();
                gender = selGender.getSelectedItem().toString();

                proposer_First_Name = edt_bi_smart_privilege_proposer_first_name
                        .getText().toString().trim();
                proposer_Middle_Name = edt_bi_smart_privilege_proposer_middle_name
                        .getText().toString().trim();
                proposer_Last_Name = edt_bi_smart_privilege_proposer_last_name
                        .getText().toString().trim();

                name_of_proposer = proposer_Title + " " + proposer_First_Name
                        + " " + proposer_Middle_Name + " " + proposer_Last_Name;

                lifeAssured_First_Name = edt_bi_smart_privilege_life_assured_first_name
                        .getText().toString().trim();
                lifeAssured_Middle_Name = edt_bi_smart_privilege_life_assured_middle_name
                        .getText().toString().trim();
                lifeAssured_Last_Name = edt_bi_smart_privilege_life_assured_last_name
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

                // TODO Auto-generated method stub
                if (valProposerSameAsLifeAssured()
                        && valLifeAssuredProposerDetail() && valDob()
                        && valLifeAssuredMinorDetail() && valBasicDetail()
                        && valPolicyTerm() && valPremiumAmt() && valSAMF()
                        && valTotalAllocation() && valPlan()) {

                    if (proposer_Is_Same_As_Life_Assured.equalsIgnoreCase("y")) {
                        proposer_Title = "";
                        proposer_First_Name = "";
                        proposer_Middle_Name = "";
                        proposer_Last_Name = "";
                        name_of_proposer = "";
                        proposer_date_of_birth = "";
                    }


                    addListenerOnSubmit();
                    getInput(smartPrivilegeBean);
                    // insertDataIntoDatabase();

                    // double sumAssured1 = Double.parseDouble(outputArr[0]);
                    if (needAnalysis_flag == 0) {
                        Intent i = new Intent(getApplicationContext(),
                                Success.class);

                        i.putExtra("ProductName",
                                "Product : SBI Life - Smart Privilege (UIN:111L107V03)");

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
                        i.putExtra("header", "SBI Life - Smart Privilege");
                        i.putExtra("header1", "(UIN:111L107V03)");
                        startActivity(i);
                    } else
                        Dialog();
                }

            }
        });

    }

    /*
     * public void setBIInputGui() { if (getValueFromDatabase()) { flagFocus =
     * false; int i = 0; List<M_Benefit_Illustration_Detail> data = db
     * .getBIDetail(QuatationNumber);
     *
     * name_of_life_assured = data.get(i).getName_of_lifeAssured(); String[]
     * lifeAssuredname = name_of_life_assured.split(" "); lifeAssured_Title =
     * lifeAssuredname[0];
     * spnr_bi_smart_privilege_life_assured_title.setSelection(
     * getIndex(spnr_bi_smart_privilege_life_assured_title, lifeAssured_Title),
     * false); edt_bi_smart_privilege_life_assured_first_name
     * .setText(lifeAssuredname[1]);
     * edt_bi_smart_privilege_life_assured_middle_name
     * .setText(lifeAssuredname[2]);
     * edt_bi_smart_privilege_life_assured_last_name
     * .setText(lifeAssuredname[3]);
     *
     * proposer_Is_Same_As_Life_Assured = data.get(i)
     * .getProposer_Same_As_Life_Assured(); if
     * (proposer_Is_Same_As_Life_Assured.equalsIgnoreCase("y")) {
     *
     * tr_smart_privilege_proposer_detail1.setVisibility(View.GONE);
     * tr_smart_privilege_proposer_detail2.setVisibility(View.GONE); } else {
     * tr_smart_privilege_proposer_detail1.setVisibility(View.VISIBLE);
     * tr_smart_privilege_proposer_detail2.setVisibility(View.VISIBLE);
     *
     * name_of_proposer = data.get(i).getName_of_proposer(); String[]
     * proposername = name_of_proposer.split(" "); proposer_Title =
     * proposername[0];
     *
     * spnr_bi_smart_privilege_proposer_title.setSelection(
     * getIndex(spnr_bi_smart_privilege_proposer_title, proposer_Title), false);
     * edt_bi_smart_privilege_proposer_first_name .setText(proposername[1]);
     * edt_bi_smart_privilege_proposer_middle_name .setText(proposername[2]);
     * edt_bi_smart_privilege_proposer_last_name .setText(proposername[3]);
     *
     * proposer_date_of_birth = data.get(i).getProposer_dob();
     * btn_bi_smart_privilege_proposer_date
     * .setText(getDate(proposer_date_of_birth));
     *
     * }
     *
     * lifeAssured_date_of_birth = data.get(i) .getLife_assured_date_of_birth();
     * btn_bi_smart_privilege_life_assured_date
     * .setText(getDate(lifeAssured_date_of_birth));
     *
     * String input = data.get(i).getInput();
     *
     * age_entry = prsObj.parseXmlTag(input, "age");
     * ageInYears.setSelection(getIndex(ageInYears, age_entry), false);
     *
     * gender = prsObj.parseXmlTag(input, "gender");
     * selGender.setSelection(getIndex(selGender, gender), false);
     *
     * staffdiscount = prsObj.parseXmlTag(input, "isStaff"); if
     * (staffdiscount.equalsIgnoreCase("true")) { isStaffDisc.setChecked(true);
     * } policyTerm = prsObj.parseXmlTag(input, "policyTerm");
     * selPolicyTerm.setSelection(getIndex(selPolicyTerm, policyTerm), false);
     *
     * updatePremiumPayingTerm(Integer.parseInt(policyTerm));
     *
     * String premFreq = prsObj.parseXmlTag(input, "plan");
     * selPlan.setSelection(getIndex(selPlan, premFreq), false); premfreqMode =
     * prsObj.parseXmlTag(input, "premFreqMode");
     *
     * if (premFreq.equalsIgnoreCase("single")) {
     * txtPremiumFreqMode.setVisibility(View.GONE);
     * selPremiumFrequencyMode.setVisibility(View.GONE);
     * txtPremiumPayingTerm.setVisibility(View.GONE);
     * premPayingTerm.setVisibility(View.GONE); } else if
     * (premFreq.equalsIgnoreCase("Regular")) {
     * txtPremiumFreqMode.setVisibility(View.VISIBLE);
     * selPremiumFrequencyMode.setVisibility(View.VISIBLE);
     * txtPremiumPayingTerm.setVisibility(View.GONE);
     * premPayingTerm.setVisibility(View.GONE); } else if
     * (premFreq.equalsIgnoreCase("Limited")) {
     * txtPremiumFreqMode.setVisibility(View.VISIBLE);
     * selPremiumFrequencyMode.setVisibility(View.VISIBLE);
     * txtPremiumPayingTerm.setVisibility(View.VISIBLE);
     * premPayingTerm.setVisibility(View.VISIBLE); }
     *
     * if (premFreq.equalsIgnoreCase("Limited") ||
     * premFreq.equalsIgnoreCase("Regular")) {
     * tableRow6.setVisibility(View.VISIBLE); // String premFreqMod =
     * prsObj.parseXmlTag(input, // "premFreqMode"); //
     * selPremiumFrequencyMode.setSelection( //
     * getIndex(selPremiumFrequencyMode, premFreqMod), false);
     *
     * selPremiumFrequencyMode.setSelection( getIndex(selPremiumFrequencyMode,
     * premfreqMode), false);
     *
     * if (premFreq.equalsIgnoreCase("Limited")) {
     *
     * tableRow7.setVisibility(View.VISIBLE); String premPaying_Term =
     * prsObj.parseXmlTag(input, "premPayingTerm"); premPayingTerm.setSelection(
     * getIndex(premPayingTerm, premPaying_Term), false);
     *
     * }
     *
     * }
     *
     * premiumAmount = ((int) Double.parseDouble(prsObj.parseXmlTag(input,
     * "premiumAmount"))) + "";
     *
     * // premfreqMode = prsObj.parseXmlTag(input, "premFreqMode");
     *
     * premiumAmt.setText(premiumAmount);
     *
     * samf = prsObj.parseXmlTag(input, "SAMF"); SAMF.setText(samf);
     *
     * noOfYrElapsed = prsObj.parseXmlTag(input, "noOfYrElapsed");
     * noOfYearsElapsedSinceInception.setText(noOfYrElapsed);
     *
     * perInvEquityFund = (prsObj.parseXmlTag(input, "perInvEquityFund"));
     * percent_EquityFund.setText((perInvEquityFund.equals("") ? "0" :
     * perInvEquityFund));
     *
     * perInvEquityOptimiserFund = (prsObj.parseXmlTag(input,
     * "perInvEquityOptimiserFund"));
     *
     * percent_EquityOptFund .setText((perInvEquityOptimiserFund.equals("") ?
     * "0" : perInvEquityOptimiserFund));
     *
     * perInvgrowthFund = (prsObj.parseXmlTag(input, "perInvgrowthFund"));
     *
     * percent_GrowthFund.setText((perInvgrowthFund.equals("") ? "0" :
     * perInvgrowthFund));
     *
     * perInvBalancedFund = (prsObj.parseXmlTag(input, "perInvBalancedFund"));
     *
     * percent_BalancedFund.setText((perInvBalancedFund.equals("") ? "0" :
     * perInvBalancedFund));
     *
     * perInvBondFund = (prsObj.parseXmlTag(input, "perInvBondFund"));
     *
     * percent_BondFund.setText((perInvBondFund.equals("") ? "0" :
     * perInvBondFund));
     *
     * perInvTop300Fund = (prsObj.parseXmlTag(input, "perInvTop300Fund"));
     * percent_Top300Fund.setText((perInvTop300Fund.equals("") ? "0" :
     * perInvTop300Fund));
     *
     * perInvPureFund = (prsObj.parseXmlTag(input, "perInvPureFund"));
     * percent_PureFund.setText((perInvPureFund.equals("") ? "0" :
     * perInvPureFund));
     *
     * perInvMidcapFund = (prsObj.parseXmlTag(input, "perInvMidcapFund"));
     * percent_MidcapFund.setText((perInvMidcapFund.equals("") ? "0" :
     * perInvMidcapFund));
     *
     * } }
     */

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

                    } else if (latestImage
                            .equalsIgnoreCase("proposerSignOnLifeAssured")) {

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
                        imageButtonSmartPrivilegeProposerPhotograph
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

        d.setContentView(R.layout.layout_smart_privilege_bi_grid);

        input = inputVal.toString();
        output = retVal.toString();
        staffdiscount = prsObj.parseXmlTag(input, "isStaff");

        age_entry = prsObj.parseXmlTag(input, "age");
        strProposerAge = prsObj.parseXmlTag(input, "proposer_age");

        TextView tv_proposername = d
                .findViewById(R.id.tv_proposername);
        TextView tv_proposal_number = d
                .findViewById(R.id.tv_proposal_number);
        TextView tv_smart_privilege_distribution_channel = (TextView) d
                .findViewById(R.id.tv_smart_privilege_distribution_channel);

        tv_smart_privilege_distribution_channel.setText(userType);
        TextView tv_bi_smart_privilege_age_entry = d
                .findViewById(R.id.tv_bi_smart_privilege_age_entry);
        TextView tv_bi_smart_privilege_maturity_age = d
                .findViewById(R.id.tv_bi_smart_privilege_maturity_age);
        TextView tv_bi_smart_privilege_life_assured_gender = d
                .findViewById(R.id.tv_bi_smart_privilege_life_assured_gender);
        TextView tv_bi_smart_privilege_policy_term = d
                .findViewById(R.id.tv_bi_smart_privilege_policy_term);
        TextView tv_bi_smart_privilege_annualised_premium = d
                .findViewById(R.id.tv_bi_smart_privilege_annualised_premium);
        TextView tv_bi_smart_privilege_sum_assured_main = d
                .findViewById(R.id.tv_bi_smart_privilege_sum_assured_main);
        TextView tv_bi_smart_privilege_mode = d
                .findViewById(R.id.tv_bi_smart_privilege_mode);
        TextView tv_bi_smart_privilege_premium_paying_term = d
                .findViewById(R.id.tv_bi_smart_privilege_premium_paying_term);

		/*TextView tv_bi_smart_privilege_SAMF =d
				.findViewById(R.id.tv_bi_smart_privilege_SAMF);
		TextView tv_bi_smart_privilege_freq_premium_title = d
				.findViewById(R.id.tv_bi_smart_privilege_freq_premium_title);
		TextView tv_bi_smart_privilege_freq_premium = d
				.findViewById(R.id.tv_bi_smart_privilege_freq_premium);*/

        TextView tv_smart_privilege_equity_fund_allocation = d
                .findViewById(R.id.tv_smart_privilege_equity_fund_allocation);
        TextView tv_smart_privilege_equity_optimiser_fund_allocation = d
                .findViewById(R.id.tv_smart_privilege_equity_optimiser_fund_allocation);
        TextView tv_smart_privilege_growth_fund_allocation = d
                .findViewById(R.id.tv_smart_privilege_growth_fund_allocation);
        TextView tv_smart_privilege_balanced_fund_allocation = d
                .findViewById(R.id.tv_smart_privilege_balanced_fund_allocation);
        TextView tv_smart_privilege_bond_fund_allocation = d
                .findViewById(R.id.tv_smart_privilege_bond_fund_allocation);
        TextView tv_smart_privilege_top_300_fund_allocation = d
                .findViewById(R.id.tv_smart_privilege_top_300_fund_allocation);
        TextView tv_smart_privilege_pure_fund_allocation = d
                .findViewById(R.id.tv_smart_privilege_pure_fund_allocation);
        TextView tv_smart_privilege_midcap_fund_allocation = d
                .findViewById(R.id.tv_smart_privilege_midcap_fund_allocation);

        TextView tv_smart_privilege_bond_optimiser_fund2_allocation = (TextView) d
                .findViewById(R.id.tv_smart_privilege_bond_optimiser_fund2_allocation);
        TextView tv_smart_privilege_money_market_fund2_allocation = (TextView) d
                .findViewById(R.id.tv_smart_privilege_money_market_fund2_allocation);
        TextView tv_smart_wealth_builder_corporate_bond_fund_allocation = (TextView) d
                .findViewById(R.id.tv_smart_wealth_builder_corporate_bond_fund_allocation);


        TextView tv_smart_privilege_equity_fund_fmc = d
                .findViewById(R.id.tv_smart_privilege_equity_fund_fmc);
        TextView tv_smart_privilege_equity_optimiser_fund_fmc = d
                .findViewById(R.id.tv_smart_privilege_equity_optimiser_fund_fmc);
        TextView tv_smart_privilege_growth_fund_fmc = d
                .findViewById(R.id.tv_smart_privilege_growth_fund_fmc);
        TextView tv_smart_privilege_balanced_fund_fmc = d
                .findViewById(R.id.tv_smart_privilege_balanced_fund_fmc);
        TextView tv_smart_privilege_bond_fund_fmc = d
                .findViewById(R.id.tv_smart_privilege_bond_fund_fmc);
        TextView tv_smart_privilege_top_300_fund_fmc = d
                .findViewById(R.id.tv_smart_privilege_top_300_fund_fmc);
        TextView tv_smart_privilege_pure_fund_fmc = d
                .findViewById(R.id.tv_smart_privilege_pure_fund_fmc);
        TextView tv_smart_privilege_midcap_fund_fmc = d
                .findViewById(R.id.tv_smart_privilege_midcap_fund_fmc);

        TextView tv_smart_wealth_builder_corporate_bond_fund_fmc = (TextView) d
                .findViewById(R.id.tv_smart_wealth_builder_corporate_bond_fund_fmc);

        TextView tv_smart_privilege_bond_optimiser_fund2_fmc = (TextView) d
                .findViewById(R.id.tv_smart_privilege_bond_optimiser_fund2_fmc);

        TextView tv_smart_privilege_money_market_fund2_fmc = (TextView) d
                .findViewById(R.id.tv_smart_privilege_money_market_fund2_fmc);

        TextView tv_smart_privilege_no_of_years_elapsed = d
                .findViewById(R.id.tv_smart_privilege_no_of_years_elapsed);
        TextView tv_smart_privilege_reduction_yield = d
                .findViewById(R.id.tv_smart_privilege_reduction_yield);
        TextView tv_smart_privilege_maturity_at = d
                .findViewById(R.id.tv_smart_privilege_maturity_at);
        TextView tv_smart_privilege_reduction_yeild2 = d
                .findViewById(R.id.tv_smart_privilege_reduction_yeild2);
        final TextView edt_proposer_name = d
                .findViewById(R.id.edt_ProposalName);
        final EditText edt_Policyholderplace = d
                .findViewById(R.id.edt_Policyholderplace);

        TextView tv_smart_privilege_death_benefit = d
                .findViewById(R.id.tv_smart_privilege_death_benefit);

        TextView tv_smart_privilege_net_yield = d
                .findViewById(R.id.tv_smart_privilege_net_yield);

        TextView tv_bi_is_Staff = d
                .findViewById(R.id.tv_bi_is_Staff);

        TableRow txt_submit_proof = (TableRow) d
                .findViewById(R.id.txt_submit_proof);

        final EditText edt_MarketingOfficalPlace = d
                .findViewById(R.id.edt_MarketingOfficalPlace);

        GridView gv_userinfo = d.findViewById(R.id.gv_userinfo);
        GridView gv_userinfo2 = (GridView) d.findViewById(R.id.gv_userinfo2);
        GridView gv_userinfo3 = (GridView) d.findViewById(R.id.gv_userinfo3);

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

        TextView tv_bi_smart_privilege_proposer_name = (TextView) d
                .findViewById(R.id.tv_bi_smart_privilege_proposer_name);

        TextView tv_bi_smart_privilege_proposer_age = (TextView) d
                .findViewById(R.id.tv_bi_smart_privilege_proposer_age);

        TextView tv_bi_smart_privilege_lifeassured_name = (TextView) d
                .findViewById(R.id.tv_bi_smart_privilege_lifeassured_name);

        TextView tv_bi_smart_privilege_frequency_mode = (TextView) d
                .findViewById(R.id.tv_bi_smart_privilege_frequency_mode);

        TextView tv_bond_optimiser_fund2_dialog = (TextView) d.findViewById(R.id.tv_bond_optimiser_fund2_dialog);
        TextView tv_money_market_fund2_dialog = (TextView) d.findViewById(R.id.tv_money_market_fund2_dialog);

        if (STR_PLAN_AB.equals("A")) {
            tv_bond_optimiser_fund2_dialog.setText("Bond Optimiser Fund II (SFIN : ULIF037160919BONDOPFND2111)");
            tv_money_market_fund2_dialog.setText("Money Market Fund II (SFIN : ULIF036160919MONMKTFND2111)");
        } else if (STR_PLAN_AB.equals("B")) {
            tv_bond_optimiser_fund2_dialog.setText("Bond Optimiser Fund (SFIN : ULIF032290618BONDOPTFND111)");
            tv_money_market_fund2_dialog.setText("Money Market Fund (SFIN : ULIF005010206MONYMKTFND111)");
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
        list_data.clear();
        list_data1.clear();
        list_data2.clear();

        // getValueFromDatabase();

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
                            + ", having received the information with respect to the above, have understood the above statement before entering into the contract.");
            edt_proposer_name_need_analysis
                    .setText("I, "
                            + name_of_life_assured
                            + ", have undergone the Need Analysis & after having reviewed the SBI Life Product options, I have opted for SBI LIFE - Smart Privilege.");

            // tv_life_assured_name.setText(name_of_life_assured);
            tv_bi_smart_privilege_proposer_name.setText(name_of_life_assured);
            tv_bi_smart_privilege_lifeassured_name.setText(name_of_life_assured);
            tv_bi_smart_privilege_proposer_age.setText(age_entry);
            tv_proposername.setText(name_of_life_assured);
        } else if (proposer_Is_Same_As_Life_Assured.equalsIgnoreCase("n")) {
            edt_proposer_name
                    .setText("I, "
                            + name_of_proposer
                            + ", having received the information with respect to the above, have understood the above statement before entering into the contract.");
            edt_proposer_name_need_analysis
                    .setText("I, "
                            + name_of_proposer
                            + ", have undergone the Need Analysis & after having reviewed the SBI Life Product options, I have opted for SBI LIFE - Smart Privilege.");

            tv_proposername.setText(name_of_proposer);
            tv_bi_smart_privilege_proposer_name.setText(name_of_proposer);
            tv_bi_smart_privilege_lifeassured_name.setText(name_of_life_assured);
            tv_bi_smart_privilege_proposer_age.setText(strProposerAge);

            // #######
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

        agentdeclaration = "I, " + commonMethods.getUserName(context)
                + " have explained the premiums, charges and benefits under the policy fully to the prospect/policyholder.";
        TextView textviewAGentStatement = d.findViewById(R.id.textviewAGentStatement);
        textviewAGentStatement.setText(agentdeclaration);

        tv_proposal_number.setText(QuatationNumber);

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

        imageButtonSmartPrivilegeProposerPhotograph = d
                .findViewById(R.id.imageButtonSmartPrivilegeProposerPhotograph);
        imageButtonSmartPrivilegeProposerPhotograph
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
            imageButtonSmartPrivilegeProposerPhotograph
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
                            imageButtonSmartPrivilegeProposerPhotograph
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
                // if (frmProductHome.equals("FALSE")) {
                name_of_person = edt_proposer_name.getText().toString();
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
                        && (((photoBitmap != null)
                        && radioButtonTrasactionModeParivartan.isChecked())
                        || radioButtonTrasactionModeManual.isChecked())) {
                    NeedAnalysisActivity.str_need_analysis = "";
                    String productCode = "2B";

                    na_cbi_bean = new NA_CBI_bean(QuatationNumber, agentcode,
                            "", userType, "", lifeAssured_Title,
                            lifeAssured_First_Name, lifeAssured_Middle_Name,
                            lifeAssured_Last_Name, planName,
                            obj.getRound(obj.getStringWithout_E(Double
                                    .valueOf(sumAssured.equals("") ? "0"
                                            : sumAssured))), obj
                            .getRound(premiumAmount), emailId,
                            mobileNo, agentEmail, agentMobile, na_input,
                            na_output, smartPrivilegeBean.getPremFreq(),
                            Integer.parseInt(policyTerm), 0, productCode,
                            getDate(lifeAssured_date_of_birth),
                            getDate(proposer_date_of_birth), inputVal
                            .toString(), retVal.toString().replace(
                            bussIll.toString(), ""));

                    name_of_person = name_of_life_assured;

                    if (radioButtonTrasactionModeParivartan.isChecked()) {
                        mode = "Parivartan";
                    } else if (radioButtonTrasactionModeManual.isChecked()) {
                        mode = "Manual";
                    }

                    db.AddNeedAnalysisDashboardDetails(new ProductBIBean("",
                            QuatationNumber, planName, getCurrentDate(),
                            mobileNo, getCurrentDate(), db.GetUserCode(),
                            emailId, "", "", agentcode, "", userType, "",
                            lifeAssured_Title, lifeAssured_First_Name,
                            lifeAssured_Middle_Name, lifeAssured_Last_Name,
                            obj.getRound(obj.getStringWithout_E(Double
                                    .valueOf(sumAssured.equals("") ? "0"
                                            : sumAssured))), obj
                            .getRound(premiumAmount), agentEmail,
                            agentMobile, na_input, na_output,
                            smartPrivilegeBean.getPremFreq(), Integer
                            .parseInt(policyTerm), 0, productCode,
                            getDate(lifeAssured_date_of_birth),
                            getDate(proposer_date_of_birth), "", mode, inputVal
                            .toString(), retVal.toString().replace(
                            bussIll.toString(), "")));

                    CreateSmartPrivilegeBIPdf();

                    NABIObj.serviceHit(BI_SmartPrivilegeActivity.this,
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
                        setFocusable(checkboxAgentStatement);
                        checkboxAgentStatement.requestFocus();
                    } else if (!radioButtonTrasactionModeParivartan.isChecked()
                            && !radioButtonTrasactionModeManual.isChecked()) {
                        commonMethods.dialogWarning(context, "Please Select Transaction Mode", true);
                        setFocusable(linearlayoutTrasactionModeParivartan);
                        linearlayoutTrasactionModeParivartan.requestFocus();
                    } else if (photoBitmap == null) {
                        commonMethods.dialogWarning(context, "Please Capture the Photo", true);
                        setFocusable(imageButtonSmartPrivilegeProposerPhotograph);
                        imageButtonSmartPrivilegeProposerPhotograph
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
                    } else if (proposer_Is_Same_As_Life_Assured
                            .equalsIgnoreCase("n")
                            && proposerAsLifeAssuredSign.equals("")) {
                        commonMethods.dialogWarning(context, "Please Make Signature for Life Assured ", true);
                        setFocusable(Ibtn_signatureofAppointee);
                        Ibtn_signatureofAppointee.requestFocus();
                    }
                }

            }
        });

        btn_PolicyholderDate.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                DIALOG_ID = 2;
                showDialog(DATE_DIALOG_ID);
            }
        });

        btn_MarketingOfficalDate.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                DIALOG_ID = 3;
                showDialog(DATE_DIALOG_ID);

            }
        });


        if (staffdiscount.equalsIgnoreCase("true")) {
            tv_bi_is_Staff.setText("Yes");
        } else {

            tv_bi_is_Staff.setText("No");

        }

        premiumAmount = ((int) Double.parseDouble(prsObj.parseXmlTag(input,
                "premiumAmount"))) + "";

        if (Double.parseDouble(premiumAmount) > 100000) {
            txt_submit_proof.setVisibility(View.VISIBLE);
        } else {
            txt_submit_proof.setVisibility(View.GONE);
        }
        samf = prsObj.parseXmlTag(input, "SAMF");

        age_entry = prsObj.parseXmlTag(input, "age");
        tv_bi_smart_privilege_age_entry.setText(age_entry + " Years");

        maturityAge = prsObj.parseXmlTag(output, "maturityAge");
        tv_bi_smart_privilege_maturity_age.setText(maturityAge + " Years");
        String netYield8pa = prsObj.parseXmlTag(output, "netYield8pa");
        gender = prsObj.parseXmlTag(input, "gender");
        tv_bi_smart_privilege_life_assured_gender.setText(gender);
        TextView tv_bi_smart_privilege_premium_type = d
                .findViewById(R.id.tv_bi_smart_privilege_premium_type);

        premPayTerm = prsObj.parseXmlTag(input, "premPayingTerm");

        netYield = prsObj.parseXmlTag(output, "netYield");
        tv_smart_privilege_net_yield.setText(netYield + " %");

        planType = prsObj.parseXmlTag(input, "plan");

        annPrem = prsObj.parseXmlTag(output, "annPrem");

        tv_bi_smart_privilege_annualised_premium.setText("Rs. "
                + obj.getRound(obj.getStringWithout_E(Double.valueOf((premiumAmount
                .equals("") || premiumAmount == null) ? "0" : premiumAmount))));

        sumAssured = prsObj.parseXmlTag(output, "sumAssured");
        tv_smart_privilege_death_benefit
                .setText("10) This policy provides guaranteed death benefit of Rs."
                        + obj.getRound(obj.getStringWithout_E(Double
                        .valueOf(sumAssured.equals("") ? "0"
                                : sumAssured))));

        tv_bi_smart_privilege_sum_assured_main.setText("Rs. "
                + getformatedThousandString(Double.parseDouble(obj.getRound(obj
                .getStringWithout_E(Double.valueOf(sumAssured
                        .equals("") ? "0" : sumAssured))))));

        mode_of_policy = prsObj.parseXmlTag(input, "plan");
        tv_bi_smart_privilege_mode.setText(mode_of_policy);

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
        perInvPureFund = (prsObj.parseXmlTag(input, "perInvPureFund"));
        perInvMidcapFund = (prsObj.parseXmlTag(input, "perInvMidcapFund"));
        perInvmoneyMarketFund2 = (prsObj.parseXmlTag(input, "perInvmoneyMarketFund2"));
        perInvbondOptimiserFund2 = (prsObj.parseXmlTag(input, "perInvbondOptimiserFund2"));

        perInvCorporateBondFund = (prsObj.parseXmlTag(input, "perInvCorporateBondFund"));


        tv_smart_privilege_equity_fund_allocation.setText((perInvEquityFund
                .equals("") ? "0" : perInvEquityFund) + " %");

        tv_smart_privilege_equity_optimiser_fund_allocation
                .setText((perInvEquityOptimiserFund.equals("") ? "0"
                        : perInvEquityOptimiserFund) + " %");
        tv_smart_privilege_growth_fund_allocation.setText((perInvgrowthFund
                .equals("") ? "0" : perInvgrowthFund) + " %");
        tv_smart_privilege_balanced_fund_allocation.setText((perInvBalancedFund
                .equals("") ? "0" : perInvBalancedFund) + " %");
        tv_smart_privilege_bond_fund_allocation.setText((perInvBondFund
                .equals("") ? "0" : perInvBondFund) + " %");
        tv_smart_privilege_top_300_fund_allocation.setText((perInvTop300Fund
                .equals("") ? "0" : perInvTop300Fund) + " %");
        tv_smart_privilege_pure_fund_allocation.setText((perInvPureFund
                .equals("") ? "0" : perInvPureFund) + " %");
        tv_smart_privilege_midcap_fund_allocation.setText((perInvMidcapFund
                .equals("") ? "0" : perInvMidcapFund) + " %");

        tv_smart_privilege_bond_optimiser_fund2_allocation.setText((perInvbondOptimiserFund2
                .equals("") ? "0" : perInvbondOptimiserFund2) + " %");

        tv_smart_privilege_money_market_fund2_allocation.setText((perInvmoneyMarketFund2
                .equals("") ? "0" : perInvmoneyMarketFund2) + " %");

        tv_smart_wealth_builder_corporate_bond_fund_allocation
                .setText((perInvCorporateBondFund.equals("") ? "0" : perInvCorporateBondFund)
                        + " %");


        tv_smart_privilege_equity_fund_fmc.setText("1.35 %");
        tv_smart_privilege_equity_optimiser_fund_fmc.setText("1.35 %");
        tv_smart_privilege_growth_fund_fmc.setText("1.35 %");
        tv_smart_privilege_balanced_fund_fmc.setText("1.25 %");
        tv_smart_privilege_bond_fund_fmc.setText("1.00 %");
        tv_smart_privilege_top_300_fund_fmc.setText("1.35 %");
        tv_smart_privilege_pure_fund_fmc.setText("1.35 %");
        tv_smart_privilege_midcap_fund_fmc.setText("1.35 %");
        tv_smart_wealth_builder_corporate_bond_fund_fmc.setText("1.15 %");

        tv_smart_privilege_bond_optimiser_fund2_fmc.setText("1.15 %");
        tv_smart_privilege_money_market_fund2_fmc.setText("0.25 %");

        noOfYrElapsed = prsObj.parseXmlTag(input, "noOfYrElapsed");
        tv_smart_privilege_no_of_years_elapsed
                .setText(noOfYrElapsed + " Years");

        redInYieldNoYr = prsObj.parseXmlTag(output, "redInYieldNoYr");
        tv_smart_privilege_reduction_yield.setText(redInYieldNoYr + " %");

        policyTerm = prsObj.parseXmlTag(input, "policyTerm");
        premfreqMode = prsObj.parseXmlTag(input, "premFreqMode");
        //tv_bi_smart_privilege_frequency_mode.setText(premfreqMode);

        netYield = prsObj.parseXmlTag(output, "netYield");

        tv_smart_privilege_maturity_at.setText(netYield + " %");

        tv_bi_smart_privilege_policy_term.setText(policyTerm + " Years");

		/*tv_bi_smart_privilege_SAMF.setText(samf);
		tv_bi_smart_privilege_freq_premium_title.setText(premfreqMode + " Premium");
		tv_bi_smart_privilege_freq_premium.setText("Rs. " + premiumAmount);
		tv_bi_smart_privilege_freq_premium_title.setVisibility(View.VISIBLE);
		tv_bi_smart_privilege_freq_premium.setVisibility(View.VISIBLE);*/

        String premPayTermString = "";
        switch (planType) {
            case "Single":
                tv_bi_smart_privilege_premium_type.setText("Amount of Installment Premium ");
                tv_bi_smart_privilege_premium_paying_term.setText("One time at the inception of the policy");
                tv_bi_smart_privilege_frequency_mode.setText("One time at the inception of the policy");

                break;
            case "Regular":
                tv_bi_smart_privilege_premium_paying_term.setText("Same as Policy Term");
                tv_bi_smart_privilege_frequency_mode.setText(premfreqMode);
                premPayTermString = policyTerm;
                break;
            case "Limited":
                tv_bi_smart_privilege_premium_paying_term.setText(premPayTerm
                        + "Years");
                tv_bi_smart_privilege_frequency_mode.setText(premfreqMode);
                premPayTermString = premPayTerm;

                break;
        }

        String redInYieldMat = prsObj.parseXmlTag(output, "redInYieldMat");

        reductionInYield = prsObj.parseXmlTag(output, "reductionInYield");
        tv_smart_privilege_reduction_yeild2.setText(reductionInYield + " %");

        TextView tv_Company_policy_surrender_dec = d
                .findViewById(R.id.tv_Company_policy_surrender_dec);

        String str_prem_pay = "";

        if (planType.equalsIgnoreCase("Single")) {
            str_prem_pay = "Single";
        } else {
            str_prem_pay = "Regular/Limited";
        }

		/*Company_policy_surrender_dec = "Your SBI Life - Smart Privilege (UIN : 111L107V03) is a "
					+ str_prem_pay
					+ " premium policy and you are required to pay One Time Premium of Rs. "
					+ obj.getRound(obj.getStringWithout_E(Double
					.valueOf(premiumAmount.equals("") ? "0" : premiumAmount)))
					+ " .Your Policy Term is "
					+ policyTerm
					+ " years and Basic Sum Assured is Rs. "
					+ getformatedThousandString(Integer.parseInt(obj.getRound(obj
					.getStringWithout_E(Double.valueOf((sumAssured
							.equals("") || sumAssured == null) ? "0"
								: sumAssured)))));*/

        if (planType.equalsIgnoreCase("Single")) {
            str_prem_pay = "Single";
            Company_policy_surrender_dec = "Your SBI Life - Smart Privilege (111L107V03) is a "
                    + str_prem_pay
                    + " Premium Policy and you are required to pay premium once at the inception of the policy of Rs. "
                    + obj.getRound(obj.getStringWithout_E(Double
                    .valueOf(premiumAmount.equals("") ? "0" : premiumAmount)))
                    + " .Your Policy Term is "
                    + policyTerm
                    + " years,Premium Payment Term is Not Applicable and Sum Assured is Rs. "
                    + getformatedThousandString(Integer.parseInt(obj.getRound(obj
                    .getStringWithout_E(Double.valueOf((sumAssured
                            .equals("") || sumAssured == null) ? "0"
                            : sumAssured)))));
        } else if (planType.equalsIgnoreCase("Limited")) {

            str_prem_pay = "Limited";

            Company_policy_surrender_dec = "Your SBI Life - Smart Privilege (111L107V03) is a "
                    + str_prem_pay
                    + " Premium Policy, and you are required to pay  "
                    + premfreqMode
                    + " premium of Rs. "
                    + getformatedThousandString(Integer.parseInt(obj.getRound(obj.getStringWithout_E(Double.valueOf((premiumAmount
                    .equals("") || premiumAmount == null) ? "0" : premiumAmount)))))
                    + " .Your Policy Term is "
                    + policyTerm
                    + " years"
                    + " , Premium Payment Term is "
                    + premPayTerm
                    + " years and Sum Assured is Rs. "
                    + getformatedThousandString(Integer.parseInt(obj.getRound(obj
                    .getStringWithout_E(Double.valueOf((sumAssured
                            .equals("") || sumAssured == null) ? "0"
                            : sumAssured)))));
        } else {
            str_prem_pay = "Regular";
            Company_policy_surrender_dec = "Your SBI Life - Smart Privilege (111L107V03) is a "
                    + str_prem_pay
                    + " Premium Policy, and you are required to pay  "
                    + premfreqMode
                    + " yearly premium of Rs. "
                    + getformatedThousandString(Integer.parseInt(obj.getRound(obj.getStringWithout_E(Double.valueOf((premiumAmount
                    .equals("") || premiumAmount == null) ? "0" : premiumAmount)))))
                    + " .Your Policy Term is "
                    + policyTerm
                    + " years"
                    + " , Premium Payment Term is same as policy term "
                    + " and Sum Assured is Rs. "
                    + getformatedThousandString(Integer.parseInt(obj.getRound(obj
                    .getStringWithout_E(Double.valueOf((sumAssured
                            .equals("") || sumAssured == null) ? "0"
                            : sumAssured)))));
        }


        tv_Company_policy_surrender_dec.setText(Company_policy_surrender_dec);

        for (int i = 1; i <= Integer.parseInt(policyTerm); i++) {
            String policy_year = prsObj
                    .parseXmlTag(output, "policyYr" + i + "");
            String premium = prsObj.parseXmlTag(output, "AnnPrem" + i + "");
            String premium_allocation_charge = prsObj.parseXmlTag(output,
                    "PremAllCharge" + i + "");
            String annulize_premium_allocation_charge = prsObj.parseXmlTag(output,
                    "AmtAviFrInv" + i + "");

            String AdditionsToTheFund4Pr = prsObj.parseXmlTag(output,
                    "AdditionsToTheFund4Pr" + i + "");

            String AdditionsToTheFund8Pr = prsObj.parseXmlTag(output,
                    "AdditionsToTheFund8Pr" + i + "");
            String policy_administration_charge = prsObj.parseXmlTag(output,
                    "PolAdminChrg" + i + "");
            String mortality_charge1 = prsObj.parseXmlTag(output, "MortChrg4Pr"
                    + i + "");
            String total_charge1A = prsObj.parseXmlTag(output, "OtherCharges4Pr_PartA" + i
                    + "");
            String total_charge2A = prsObj.parseXmlTag(output, "OtherCharges8Pr_PartA" + i
                    + "");
            String service_tax_on_mortality_charge1 = prsObj.parseXmlTag(
                    output, "TotServTax4Pr" + i + "");
            String fund_before_fmc1 = prsObj.parseXmlTag(output,
                    "FundBefFMC4Pr" + i + "");
            String fund_management_charge1 = prsObj.parseXmlTag(output,
                    "FundMgmtCharg4Pr" + i + "");
            String guranteed_addition1 = prsObj.parseXmlTag(output,
                    "LoyaltyAdd4Pr" + i + "");
            String fund_value_at_end1 = prsObj.parseXmlTag(output,
                    "FundValAtEnd4Pr" + i + "");
            String surrender_value1 = prsObj.parseXmlTag(output, "SurrVal4Pr"
                    + i + "");
            String death_benefit1 = prsObj.parseXmlTag(output, "DeathBen4Pr"
                    + i + "");
            String mortality_charge2 = prsObj.parseXmlTag(output, "MortChrg8Pr"
                    + i + "");
            String total_charge1B = prsObj.parseXmlTag(output, "OtherCharges4Pr_PartB" + i
                    + "");
            String total_charge2B = prsObj.parseXmlTag(output, "OtherCharges8Pr_PartB" + i
                    + "");
            String service_tax_on_mortality_charge2 = prsObj.parseXmlTag(
                    output, "TotServTxOnCharg8Pr" + i + "");
            String fund_before_fmc2 = prsObj.parseXmlTag(output,
                    "FundBefFMC8Pr" + i + "");
            String fund_management_charge2 = prsObj.parseXmlTag(output,
                    "FundMgmtCharg8Pr" + i + "");
            String guranteed_addition2 = prsObj.parseXmlTag(output,
                    "LoyaltyAdd8Pr" + i + "");
            String fund_value_at_end2 = prsObj.parseXmlTag(output,
                    "FundValAtEnd8Pr" + i + "");
            String surrender_value2 = prsObj.parseXmlTag(output, "SurrVal8Pr"
                    + i + "");
            String death_benefit2 = prsObj.parseXmlTag(output, "DeathBen8Pr"
                    + i + "");
            String commission = prsObj.parseXmlTag(output, "CommIfPay8Pr" + i
                    + "");


            list_data.add(new M_BI_SmartPrivilegeAdapterCommon(
                    policy_year,
                    obj.getRound(obj.getStringWithout_E(Double
                            .valueOf((premium.equals("") || premium == null) ? "0"
                                    : premium)))
                            + "",
                    obj.getRound(obj.getStringWithout_E(Double.valueOf((mortality_charge1.equals("") || mortality_charge1 == null) ? "0" : mortality_charge1))) + "",
                    obj.getRound(obj.getStringWithout_E(Double.valueOf((total_charge1A.equals("") || total_charge1A == null) ? "0" : total_charge1A))) + "",
                    obj.getRound(obj.getStringWithout_E(Double.valueOf((service_tax_on_mortality_charge1.equals("") || service_tax_on_mortality_charge1 == null) ? "0" : service_tax_on_mortality_charge1))) + "",
                    obj.getRound(obj.getStringWithout_E(Double.valueOf((fund_value_at_end1.equals("") || fund_value_at_end1 == null) ? "0" : fund_value_at_end1))) + "",
                    obj.getRound(obj.getStringWithout_E(Double.valueOf((surrender_value1.equals("") || surrender_value1 == null) ? "0" : surrender_value1))) + "",
                    obj.getRound(obj.getStringWithout_E(Double.valueOf((death_benefit1.equals("") || death_benefit1 == null) ? "0" : death_benefit1))) + "",
                    obj.getRound(obj.getStringWithout_E(Double.valueOf((mortality_charge2.equals("") || mortality_charge2 == null) ? "0" : mortality_charge2))) + "",
                    obj.getRound(obj.getStringWithout_E(Double.valueOf((total_charge2A.equals("") || total_charge2A == null) ? "0" : total_charge2A))) + "",
                    obj.getRound(obj.getStringWithout_E(Double.valueOf((service_tax_on_mortality_charge2.equals("") || service_tax_on_mortality_charge2 == null) ? "0" : service_tax_on_mortality_charge2))) + "",
                    obj.getRound(obj.getStringWithout_E(Double.valueOf((fund_value_at_end2.equals("") || fund_value_at_end2 == null) ? "0" : fund_value_at_end2))) + "",
                    obj.getRound(obj.getStringWithout_E(Double.valueOf((surrender_value2.equals("") || surrender_value2 == null) ? "0" : surrender_value2))) + "",
                    obj.getRound(obj.getStringWithout_E(Double.valueOf((death_benefit2.equals("") || death_benefit2 == null) ? "0" : death_benefit2))) + "",
                    obj.getRound(obj.getStringWithout_E(Double.valueOf((commission.equals("") || commission == null) ? "0" : commission))) + ""));

            list_data1.add(new M_BI_SmartPrivilegeAdapterCommon2(
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
                    obj.getRound(obj.getStringWithout_E(Double.valueOf((surrender_value2.equals("") || surrender_value2 == null) ? "0" : surrender_value2))) + "",
                    obj.getRound(obj.getStringWithout_E(Double.valueOf((death_benefit2.equals("") || death_benefit2 == null) ? "0" : death_benefit2))) + ""));

            list_data2.add(new M_BI_SmartPrivilegeAdapterCommon2(
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
                    obj.getRound(obj.getStringWithout_E(Double.valueOf((surrender_value1.equals("") || surrender_value1 == null) ? "0" : surrender_value1))) + "",
                    obj.getRound(obj.getStringWithout_E(Double.valueOf((death_benefit1.equals("") || death_benefit1 == null) ? "0" : death_benefit1))) + ""));
        }


        Adapter_BI_SmartPrivilegeGridCommon adapter = new Adapter_BI_SmartPrivilegeGridCommon(
                BI_SmartPrivilegeActivity.this, list_data);
        gv_userinfo.setAdapter(adapter);

        Adapter_BI_SmartPrivilegeGridCommon2 adapter1 = new Adapter_BI_SmartPrivilegeGridCommon2(
                BI_SmartPrivilegeActivity.this, list_data1);
        gv_userinfo2.setAdapter(adapter1);

        Adapter_BI_SmartPrivilegeGridCommon2 adapter2 = new Adapter_BI_SmartPrivilegeGridCommon2(
                BI_SmartPrivilegeActivity.this, list_data2);
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
        showDialog(DATE_DIALOG_ID);
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

                            btn_bi_smart_privilege_proposer_date.setText(date);
                            edt_bi_smart_privilege_proposer_age.setText(final_age);
                            proposer_date_of_birth = getDate1(date + "");

                            clearFocusable(btn_bi_smart_privilege_proposer_date);

                            setFocusable(edt_proposerdetail_basicdetail_contact_no);
                            edt_proposerdetail_basicdetail_contact_no
                                    .requestFocus();

                        } else {
                            commonMethods.BICommonDialog(context, "Minimum Age should be 18yrs For Proposer");
                            btn_bi_smart_privilege_proposer_date
                                    .setText("Select Date");
                            edt_bi_smart_privilege_proposer_age.setText("");
                            proposer_date_of_birth = "";
                            clearFocusable(btn_bi_smart_privilege_proposer_date);
                            setFocusable(btn_bi_smart_privilege_proposer_date);
                            btn_bi_smart_privilege_proposer_date.requestFocus();

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
                            if (8 <= age && age <= 55) {

                                btn_bi_smart_privilege_life_assured_date
                                        .setText(date);
                                edt_bi_smart_privilege_life_assured_age
                                        .setText(final_age);

                                if (Integer.parseInt(final_age) < 18) {
                                    /*
                                     * tr_smart_privilege_proposer_detail1
                                     * .setVisibility(View.VISIBLE);
                                     * tr_smart_privilege_proposer_detail2
                                     * .setVisibility(View.VISIBLE);
                                     */
                                    proposer_Is_Same_As_Life_Assured = "n";

                                    clearFocusable(btn_bi_smart_privilege_life_assured_date);

                                    /*
                                     * setFocusable(selPlan);
                                     * selPlan.requestFocus();
                                     */
                                    tablelayoutSmartPrivilegeProposerTitle
                                            .setVisibility(View.VISIBLE);
                                    setFocusable(edt_proposerdetail_basicdetail_contact_no);
                                    edt_proposerdetail_basicdetail_contact_no
                                            .requestFocus();
                                } else {

                                    proposer_Title = "";
                                    proposer_First_Name = "";
                                    proposer_Middle_Name = "";
                                    proposer_Last_Name = "";
                                    proposer_date_of_birth = "";


                                    commonMethods.fillSpinnerValue(context, spnr_bi_smart_privilege_proposer_title,
                                            Arrays.asList(getResources().getStringArray(R.array.arr_select_title)));
                                    btn_bi_smart_privilege_proposer_date
                                            .setText("Select Date");

                                    edt_bi_smart_privilege_proposer_first_name
                                            .setText("");
                                    edt_bi_smart_privilege_proposer_middle_name
                                            .setText("");
                                    edt_bi_smart_privilege_proposer_last_name
                                            .setText("");

                                    /*
                                     * tr_smart_privilege_proposer_detail1
                                     * .setVisibility(View.GONE);
                                     * tr_smart_privilege_proposer_detail2
                                     * .setVisibility(View.GONE);
                                     */
                                    proposer_Is_Same_As_Life_Assured = "y";
                                    tablelayoutSmartPrivilegeProposerTitle
                                            .setVisibility(View.GONE);
                                    clearFocusable(btn_bi_smart_privilege_life_assured_date);
                                }

                                ageInYears.setSelection(
                                        getIndex(ageInYears, final_age), false);
                                valMaturityAge();
                                updatePolicyTermLabel();
                                updateSAMFlabel();
                                lifeAssured_date_of_birth = getDate1(date + "");

                            } else {
                                commonMethods.BICommonDialog(context,
                                        "Minimum Age should be 8 yrs and Maximum Age should be 55 yrs For LifeAssured Plan Type Regular/Limited");
                                btn_bi_smart_privilege_life_assured_date
                                        .setText("Select Date");
                                edt_bi_smart_privilege_life_assured_age.setText("");
                                lifeAssured_date_of_birth = "";
                                clearFocusable(btn_bi_smart_privilege_life_assured_date);
                                setFocusable(btn_bi_smart_privilege_life_assured_date);
                                btn_bi_smart_privilege_life_assured_date
                                        .requestFocus();
                            }
                        } else if (selPlan.getSelectedItem().toString()
                                .equals("Single")) {
                            if (13 <= age && age <= 55) {

                                btn_bi_smart_privilege_life_assured_date
                                        .setText(date);
                                edt_bi_smart_privilege_life_assured_age
                                        .setText(final_age);

                                if (Integer.parseInt(final_age) < 18) {
                                    /*
                                     * tr_smart_privilege_proposer_detail1
                                     * .setVisibility(View.VISIBLE);
                                     * tr_smart_privilege_proposer_detail1
                                     * .setVisibility(View.VISIBLE);
                                     */
                                    proposer_Is_Same_As_Life_Assured = "n";
                                    clearFocusable(btn_bi_smart_privilege_life_assured_date);
                                    setFocusable(spnr_bi_smart_privilege_proposer_title);
                                    spnr_bi_smart_privilege_proposer_title
                                            .requestFocus();
                                    tablelayoutSmartPrivilegeProposerTitle
                                            .setVisibility(View.VISIBLE);
                                } else {

                                    proposer_Title = "";
                                    proposer_First_Name = "";
                                    proposer_Middle_Name = "";
                                    proposer_Last_Name = "";
                                    proposer_date_of_birth = "";

                                    commonMethods.fillSpinnerValue(context, spnr_bi_smart_privilege_life_assured_title,
                                            Arrays.asList(getResources().getStringArray(R.array.arr_select_title)));

                                    btn_bi_smart_privilege_proposer_date
                                            .setText("Select Date");

                                    edt_bi_smart_privilege_proposer_first_name
                                            .setText("");
                                    edt_bi_smart_privilege_proposer_middle_name
                                            .setText("");
                                    edt_bi_smart_privilege_proposer_last_name
                                            .setText("");

                                    /*
                                     * tr_smart_privilege_proposer_detail1
                                     * .setVisibility(View.GONE);
                                     * tr_smart_privilege_proposer_detail1
                                     * .setVisibility(View.GONE);
                                     */
                                    proposer_Is_Same_As_Life_Assured = "y";
                                    clearFocusable(btn_bi_smart_privilege_life_assured_date);

                                    /*
                                     * setFocusable(selPlan);
                                     * selPlan.requestFocus();
                                     */

                                    setFocusable(edt_proposerdetail_basicdetail_contact_no);
                                    edt_proposerdetail_basicdetail_contact_no
                                            .requestFocus();
                                    tablelayoutSmartPrivilegeProposerTitle
                                            .setVisibility(View.GONE);

                                }

                                ageInYears.setSelection(
                                        getIndex(ageInYears, final_age), false);
                                valMaturityAge();
                                updatePolicyTermLabel();
                                updateSAMFlabel();
                                lifeAssured_date_of_birth = getDate1(date + "");

                            } else {
                                commonMethods.BICommonDialog(context, "Minimum Age should be 13 yrs and Maximum Age should be 55 yrs For LifeAssured for Plan Type Single");
                                btn_bi_smart_privilege_life_assured_date
                                        .setText("Select Date");
                                edt_bi_smart_privilege_life_assured_age.setText("");
                                lifeAssured_date_of_birth = "";
                                clearFocusable(btn_bi_smart_privilege_life_assured_date);
                                setFocusable(btn_bi_smart_privilege_life_assured_date);
                                btn_bi_smart_privilege_life_assured_date
                                        .requestFocus();
                                tablelayoutSmartPrivilegeProposerTitle
                                        .setVisibility(View.GONE);
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
        /*
         * initialiseDateParameter(lifeAssured_date_of_birth, 35); DIALOG_ID =
         * 5; showDialog(DATE_DIALOG_ID);
         */

        if (!na_dob.equals("") && flag == 0) {
            System.out.println(" na_dob : " + na_dob);
            initialiseDateParameter(na_dob, 35);
            DIALOG_ID = 5;
            updateDisplay(DIALOG_ID);
            flag = 1;
        } else {
            initialiseDateParameter(lifeAssured_date_of_birth, 35);
            DIALOG_ID = 5;
            showDialog(DATE_DIALOG_ID);
        }
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

    private void CreateSmartPrivilegeBIPdf() {
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

            mypath = mStorageUtils.createFileToAppSpecificDir(context, QuatationNumber + "BI.pdf");
            needAnalysispath = mStorageUtils.createFileToAppSpecificDir(context, "NA.pdf");
            // needAnalysis_Tabular_path = new File(folder, "NA_Tabular.pdf");
            newFile = mStorageUtils.createFileToAppSpecificDir(context, QuatationNumber + "P01.pdf");
            /*
             * File mypath; mypath = new File(folder, QuatationNumber +
             * "P01.pdf");
             */

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
                            "ULIP Benefit Illustration for SBI LIFE - Smart Privilege (UIN No - 111L107V03)",
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
                    "Customised Benefit Illustration",
                    small_bold);
            para_address5.setAlignment(Element.ALIGN_CENTER);
            Paragraph para_address6 = new Paragraph(
                    "SBI Life - Smart Privilege (111L107V03)",
                    small_bold);
            para_address6.setAlignment(Element.ALIGN_CENTER);
            Paragraph para_address7 = new Paragraph(
                    "An Individual, Unit-linked, Non-Participating, Life Insurance Product",
                    small_bold);
            para_address7.setAlignment(Element.ALIGN_CENTER);
            Paragraph para_address8 = new Paragraph(
                    "IN THIS POLICY, THE INVESTMENT RISK IN INVESTMENT PORTFOLIO IS BORNE BY THE POLICYHOLDER.",
                    small_bold);
            para_address7.setAlignment(Element.ALIGN_CENTER);

            document.add(para_address);
            document.add(para_address2);
            document.add(para_address3);
            document.add(para_address4);
            document.add(para_address5);
            document.add(para_address6);
            document.add(para_address7);
            document.add(para_img_logo_after_space_1);
            //document.add(headertable);
            document.add(para_address8);
            document.add(para_img_logo_after_space_1);

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
                    "Channel / Intermediary : ", small_normal));
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

            PdfPTable BI_Pdftable2 = new PdfPTable(1);
            BI_Pdftable2.setWidthPercentage(100);
            // CR_table6.setWidths(columnWidths_2);
            PdfPCell BI_Pdftable2_cell1 = new PdfPCell(
                    new Paragraph(
                            "Insurance Regulatory and Development authority of India (IRDAI) requires all life insurance companies operating in India to provide official illustrations to their customers. The illustrations are based on the investment rates of return set by the Insurance Regulatory and Development Authority of India (Unit Linked Insurance Products) Regulations, 2019 and is not intended to reflect the actual investment returns achieved or which may be achieved in future by SBI Life Insurance Company Limited.",
                            small_normal));

            BI_Pdftable2_cell1.setPadding(5);

            BI_Pdftable2.addCell(BI_Pdftable2_cell1);
            document.add(BI_Pdftable2);

            PdfPTable BI_Pdftable3 = new PdfPTable(1);
            BI_Pdftable3.setWidthPercentage(100);
            PdfPCell BI_Pdftable3_cell1 = new PdfPCell(
                    new Paragraph(
                            "The main objective of the illustration is that the client is able to appreciate the features of the product and the flow of benefits in different circumstances with some level of quantification. For further information on the product, its benefits and applicable charges please refer to the sales brochure and/or policy document. Further information will also be available on request.",
                            small_normal));

            BI_Pdftable3_cell1.setPadding(5);

            BI_Pdftable3.addCell(BI_Pdftable3_cell1);
            document.add(BI_Pdftable3);

            PdfPTable BI_Pdftable4 = new PdfPTable(1);
            BI_Pdftable4.setWidthPercentage(100);
            PdfPCell BI_Pdftable4_cell1 = new PdfPCell(
                    new Paragraph(
                            "Some benefits are guaranteed and some benefits are variable with returns based on the future fund performance of SBI Life Insurance Company Limited. If your policy offers guaranteed returns then the same will be clearly marked as “guaranteed” in the illustration table. If your policy offers variable returns then the illustration will show two different rates of assumed future investment returns. These assumed rates of return are not guaranteed and they are not the upper or lower limits of what you might get back, as the value of your policy is dependent on a number of factors including future fund investment performance.",
                            small_normal));

            BI_Pdftable4_cell1.setPadding(5);

            BI_Pdftable4.addCell(BI_Pdftable4_cell1);
            document.add(BI_Pdftable4);

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

            //document.add(table_staff_disccount);

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
                    new Paragraph("Age of Proposer", small_normal));
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
                    "Name of Life Assured", small_normal));
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

            String payingTerm = "";
            String str_freq_mode = "";
            if (planType.equalsIgnoreCase("single")) {
                payingTerm = "One time at the inception of the policy";
                str_freq_mode = "One time at the inception of the policy";
            } else if (planType.equalsIgnoreCase("Regular")) {
                payingTerm = "Same as Policy Term";
                str_freq_mode = premfreqMode;
            } else if (planType.equalsIgnoreCase("Limited")) {
                payingTerm = premPayTerm + " Years";
                str_freq_mode = premfreqMode;
            }

            PdfPTable table_lifeAssuredDetails_gender_policy_term = new PdfPTable(
                    4);
            table_lifeAssuredDetails_gender_policy_term.setWidthPercentage(100);

            PdfPCell cell_lifeAssured_gender1 = new PdfPCell(new Paragraph(
                    "Premium Payment Term", small_normal));
            cell_lifeAssured_gender1.setPadding(5);
            PdfPCell cell_lifeAssured_gender2 = new PdfPCell(new Paragraph(payingTerm, small_bold));
            cell_lifeAssured_gender2.setPadding(5);
            cell_lifeAssured_gender2
                    .setHorizontalAlignment(Element.ALIGN_CENTER);

            PdfPCell cell_termsofPolicy1 = new PdfPCell(new Paragraph(
                    "Policy Term  " + "  ", small_normal));
            cell_lifeAssuredAge1.setPadding(5);
            PdfPCell cell_termsofPolicy2 = new PdfPCell(new Paragraph("  "
                    + policyTerm + " Years", small_bold));
            cell_termsofPolicy2.setPadding(5);
            cell_termsofPolicy2.setHorizontalAlignment(Element.ALIGN_CENTER);

            table_lifeAssuredDetails_gender_policy_term
                    .addCell(cell_termsofPolicy1);
            table_lifeAssuredDetails_gender_policy_term
                    .addCell(cell_termsofPolicy2);

            table_lifeAssuredDetails_gender_policy_term
                    .addCell(cell_lifeAssured_gender1);
            table_lifeAssuredDetails_gender_policy_term
                    .addCell(cell_lifeAssured_gender2);


            document.add(table_lifeAssuredDetails_gender_policy_term);

			/*String PlanType1 = "";
			if (plan.equalsIgnoreCase("Regular")
					|| plan.equalsIgnoreCase("Limited")) {

				PlanType1 = "Annualised Premium";

			} else if (plan.equalsIgnoreCase("Single")) {
				PlanType1 = "Single Premium";
			}*/

            PdfPTable table_lifeAssuredDetails_premium_sumAssured = new PdfPTable(
                    4);
            table_lifeAssuredDetails_premium_sumAssured.setWidthPercentage(100);

            PdfPCell cell_regularPremium1 = new PdfPCell(new Paragraph(
                    "Amount of Installment Premium ", small_normal));
            cell_lifeAssuredAmaturityAge1.setPadding(5);
            PdfPCell cell_regularPremium2 = new PdfPCell(
                    new Paragraph(
                            "Rs. "

                                    + obj.getRound(obj.getStringWithout_E(Double
                                    .valueOf((premiumAmount.equals("") || premiumAmount == null) ? "0"
                                            : premiumAmount))), small_bold));
            cell_regularPremium2.setPadding(5);
            cell_regularPremium2.setHorizontalAlignment(Element.ALIGN_CENTER);

            PdfPCell cell_sumAssured1 = new PdfPCell(new Paragraph(
                    "Sum assured", small_normal));
            cell_lifeAssured_gender1.setPadding(5);
            PdfPCell cell_sumAssured2 = new PdfPCell(new Paragraph(
                    "Rs. "
                            + obj.getRound(obj.getStringWithout_E(Double
                            .valueOf(sumAssured.equals("") ? "0"
                                    : sumAssured))), small_bold));
            cell_sumAssured2.setPadding(5);
            cell_sumAssured2.setHorizontalAlignment(Element.ALIGN_CENTER);

            table_lifeAssuredDetails_premium_sumAssured
                    .addCell(cell_regularPremium1);
            table_lifeAssuredDetails_premium_sumAssured
                    .addCell(cell_regularPremium2);

            table_lifeAssuredDetails_premium_sumAssured
                    .addCell(cell_sumAssured1);
            table_lifeAssuredDetails_premium_sumAssured
                    .addCell(cell_sumAssured2);

            document.add(table_lifeAssuredDetails_premium_sumAssured);

            PdfPTable tablePolicyDetails2 = new PdfPTable(4);
            tablePolicyDetails2.setWidthPercentage(100);

            PdfPCell cell_mode1 = new PdfPCell(new Paragraph("Mode / Frequency of Premium Payment ",
                    small_normal));
            cell_mode1.setPadding(5);
            PdfPCell cell_mode2 = new PdfPCell(new Paragraph(str_freq_mode, small_bold));
            cell_mode2.setPadding(5);
            cell_mode2.setHorizontalAlignment(Element.ALIGN_CENTER);


            PdfPCell cell_firstYearPremium1 = new PdfPCell(new Paragraph(
                    "Rate of Applicable Taxes", small_normal));
            cell_firstYearPremium1.setPadding(5);
            PdfPCell cell_firstYearPremium2 = new PdfPCell(new Paragraph(
                    " 18%", small_bold));
            cell_firstYearPremium2.setPadding(5);
            cell_firstYearPremium2.setHorizontalAlignment(Element.ALIGN_CENTER);

            tablePolicyDetails2.addCell(cell_mode1);
            tablePolicyDetails2.addCell(cell_mode2);

            tablePolicyDetails2.addCell(cell_firstYearPremium1);
            tablePolicyDetails2.addCell(cell_firstYearPremium2);

            document.add(tablePolicyDetails2);

            PdfPTable table_samf = new PdfPTable(2);
            table_samf.setWidthPercentage(100);

            PdfPCell cell_samf1 = new PdfPCell(new Paragraph(
                    "Investment Strategy Opted For", small_normal));
            cell_samf1.setPadding(5);
            PdfPCell cell_samf2 = new PdfPCell(new Paragraph("NA", small_bold));
            cell_samf2.setPadding(5);
            cell_samf2.setHorizontalAlignment(Element.ALIGN_CENTER);

            table_samf.addCell(cell_samf1);
            table_samf.addCell(cell_samf2);

            document.add(table_samf);

            if (plan.equalsIgnoreCase("Regular")
                    || plan.equalsIgnoreCase("Limited")) {

                PdfPTable table_mode_of_premium = new PdfPTable(2);
                table_mode_of_premium.setWidthPercentage(100);

                PdfPCell cell_table_mode_of_premium1 = new PdfPCell(
                        new Paragraph(premfreqMode + " Premium", small_normal));
                cell_table_mode_of_premium1.setPadding(5);
                PdfPCell cell_table_mode_of_premium2 = new PdfPCell(
                        new Paragraph("Rs. " + premiumAmount, small_bold));
                cell_table_mode_of_premium2.setPadding(5);
                cell_table_mode_of_premium2
                        .setHorizontalAlignment(Element.ALIGN_CENTER);

                table_mode_of_premium.addCell(cell_table_mode_of_premium1);
                table_mode_of_premium.addCell(cell_table_mode_of_premium2);

                //document.add(table_mode_of_premium);

            }

            document.add(para_img_logo_after_space_1);
            PdfPTable BI_PdftableFundDetails = new PdfPTable(1);
            BI_PdftableFundDetails.setWidthPercentage(100);

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
            BI_PdftableFundTypes_cell4
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableFundTypes_cell4.setPadding(5);

            BI_PdftableFundTypes.addCell(BI_PdftableFundTypes_cell1);
            BI_PdftableFundTypes.addCell(BI_PdftableFundTypes_cell2);
            BI_PdftableFundTypes.addCell(BI_PdftableFundTypes_cell3);
            BI_PdftableFundTypes.addCell(BI_PdftableFundTypes_cell4);
            document.add(BI_PdftableFundTypes);

            String EquityFundFMC = "1.35%";
            if (perInvEquityFund.equalsIgnoreCase("")) {

                perInvEquityFund = "0";
            }

            PdfPTable BI_PdftableEquityFund = new PdfPTable(4);
            BI_PdftableEquityFund.setWidthPercentage(100);

            PdfPCell BI_PdftableEquityFund_cell1 = new PdfPCell(new Paragraph(
                    "Equity Fund (SFIN : ULIF001100105EQUITY-FND111)", small_normal));
            PdfPCell BI_PdftableEquityFund_cell2 = new PdfPCell(new Paragraph(
                    perInvEquityFund + " %", small_normal));

            PdfPCell BI_PdftableEquityFund_cell3 = new PdfPCell(new Paragraph(
                    EquityFundFMC, small_normal));
            PdfPCell BI_PdftableEquityFund_cell4 = new PdfPCell(new Paragraph(
                    "High", small_normal));

            BI_PdftableEquityFund_cell1.setPadding(5);

            BI_PdftableEquityFund_cell2
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableEquityFund_cell2.setPadding(5);

            BI_PdftableEquityFund_cell3
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableEquityFund_cell3.setPadding(5);
            BI_PdftableEquityFund_cell4
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableEquityFund_cell4.setPadding(5);

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
                    new Paragraph(
                            "Equity Optimiser Fund (SFIN : ULIF010210108EQTYOPTFND111)",
                            small_normal));
            PdfPCell BI_PdftableEquityOptimiserFund_cell2 = new PdfPCell(
                    new Paragraph(perInvEquityOptimiserFund + " %",
                            small_normal));

            PdfPCell BI_PdftableTopEquityOptimiserFund_cell3 = new PdfPCell(
                    new Paragraph(EquityOptimiserFundFMC, small_normal));
            PdfPCell BI_PdftableTopEquityOptimiserFund_cell4 = new PdfPCell(
                    new Paragraph("High", small_normal));

            BI_PdftableTopEquityOptimiserFund_cell1.setPadding(5);

            BI_PdftableEquityOptimiserFund_cell2
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableEquityOptimiserFund_cell2.setPadding(5);

            BI_PdftableTopEquityOptimiserFund_cell3
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableTopEquityOptimiserFund_cell3.setPadding(5);
            BI_PdftableTopEquityOptimiserFund_cell4
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableTopEquityOptimiserFund_cell4.setPadding(5);

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
                    new Paragraph("Growth Fund (SFIN : ULIF003241105GROWTH-FND111)",
                            small_normal));
            PdfPCell BI_PdftableGrowthFund_cell2 = new PdfPCell(new Paragraph(
                    perInvgrowthFund + " %", small_normal));

            PdfPCell BI_PdftableTopGrowthFund_cell3 = new PdfPCell(
                    new Paragraph(GrowthFundFMC, small_normal));
            PdfPCell BI_PdftableTopGrowthFund_cell4 = new PdfPCell(
                    new Paragraph("Medium to High", small_normal));

            BI_PdftableTopGrowthFund_cell1.setPadding(5);

            BI_PdftableGrowthFund_cell2
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableGrowthFund_cell2.setPadding(5);

            BI_PdftableTopGrowthFund_cell3
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableTopGrowthFund_cell3.setPadding(5);
            BI_PdftableTopGrowthFund_cell4
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableTopGrowthFund_cell4.setPadding(5);

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
                    new Paragraph("Balanced Fund (SFIN : ULIF004051205BALANCDFND111)",
                            small_normal));
            PdfPCell BI_PdftableBalancedFund_cell2 = new PdfPCell(
                    new Paragraph(perInvBalancedFund + " %", small_normal));

            PdfPCell BI_PdftableBalancedFund_cell3 = new PdfPCell(
                    new Paragraph(BalancedFundFMC, small_normal));
            PdfPCell BI_PdftableBalancedFund_cell4 = new PdfPCell(
                    new Paragraph("Medium", small_normal));

            BI_PdftableBalancedFund_cell1.setPadding(5);

            BI_PdftableBalancedFund_cell2
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableBalancedFund_cell2.setPadding(5);

            BI_PdftableBalancedFund_cell3
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableBalancedFund_cell3.setPadding(5);
            BI_PdftableBalancedFund_cell4
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableBalancedFund_cell4.setPadding(5);

            BI_PdftableBalancedFund.addCell(BI_PdftableBalancedFund_cell1);
            BI_PdftableBalancedFund.addCell(BI_PdftableBalancedFund_cell2);
            BI_PdftableBalancedFund.addCell(BI_PdftableBalancedFund_cell3);
            BI_PdftableBalancedFund.addCell(BI_PdftableBalancedFund_cell4);
            document.add(BI_PdftableBalancedFund);
            //document.newPage();

            if (perInvBondFund.equalsIgnoreCase("")) {

                perInvBondFund = "0";
            }

            String BondFund = "0.2";
            String BondFundFMC = "1.00%";

            PdfPTable BI_PdftableBondFund = new PdfPTable(4);
            BI_PdftableBondFund.setWidthPercentage(100);

            PdfPCell BI_PdftableBondFund_cell1 = new PdfPCell(new Paragraph(
                    "Bond Fund (SFIN : ULIF002100105BONDULPFND111)", small_normal));
            PdfPCell BI_PdftableBondFund_cell2 = new PdfPCell(new Paragraph(
                    perInvBondFund + " %", small_normal));

            PdfPCell BI_PdftableBondFund_cell3 = new PdfPCell(new Paragraph(
                    BondFundFMC, small_normal));
            PdfPCell BI_PdftableBondFund_cell4 = new PdfPCell(new Paragraph(
                    "Low to Medium", small_normal));

            BI_PdftableBondFund_cell1.setPadding(5);

            BI_PdftableBondFund_cell2
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableBondFund_cell2.setPadding(5);

            BI_PdftableBondFund_cell3
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableBondFund_cell3.setPadding(5);
            BI_PdftableBondFund_cell4
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableBondFund_cell4.setPadding(5);

            BI_PdftableBondFund.addCell(BI_PdftableBondFund_cell1);
            BI_PdftableBondFund.addCell(BI_PdftableBondFund_cell2);
            BI_PdftableBondFund.addCell(BI_PdftableBondFund_cell3);
            BI_PdftableBondFund.addCell(BI_PdftableBondFund_cell4);
            document.add(BI_PdftableBondFund);

            if (perInvTop300Fund.equalsIgnoreCase("")) {

                perInvTop300Fund = "0";
            }

            String Top300Fund = "0.0";
            String Top300FundFMC = "1.35%";

            PdfPTable BI_PdftableTop300Fund = new PdfPTable(4);
            BI_PdftableTop300Fund.setWidthPercentage(100);

            PdfPCell BI_PdftableTop300Fund_cell1 = new PdfPCell(new Paragraph(
                    "Top 300 Fund (SFIN : ULIF016070110TOP300-FND111)", small_normal));
            PdfPCell BI_PdftableTop300Fund_cell2 = new PdfPCell(new Paragraph(
                    perInvTop300Fund + " %", small_normal));

            PdfPCell BI_PdftableTop300Fund_cell3 = new PdfPCell(new Paragraph(
                    Top300FundFMC, small_normal));
            PdfPCell BI_PdftableTop300Fund_cell4 = new PdfPCell(new Paragraph(
                    "High", small_normal));

            BI_PdftableTop300Fund_cell1.setPadding(5);

            BI_PdftableTop300Fund_cell2
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableTop300Fund_cell2.setPadding(5);

            BI_PdftableTop300Fund_cell3
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableTop300Fund_cell3.setPadding(5);
            BI_PdftableTop300Fund_cell4
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableTop300Fund_cell4.setPadding(5);

            BI_PdftableTop300Fund.addCell(BI_PdftableTop300Fund_cell1);
            BI_PdftableTop300Fund.addCell(BI_PdftableTop300Fund_cell2);
            BI_PdftableTop300Fund.addCell(BI_PdftableTop300Fund_cell3);
            BI_PdftableTop300Fund.addCell(BI_PdftableTop300Fund_cell4);
            document.add(BI_PdftableTop300Fund);

            if (perInvPureFund.equalsIgnoreCase("")) {

                perInvPureFund = "0";
            }
            String PureFundFMC = "1.35%";

            PdfPTable BI_PdftablePureFund = new PdfPTable(4);
            BI_PdftablePureFund.setWidthPercentage(100);

            PdfPCell BI_PdftablePureFund_cell1 = new PdfPCell(new Paragraph(
                    "Pure Fund (SFIN : ULIF030290915PUREULPFND111)", small_normal));
            PdfPCell BI_PdftablePureFund_cell2 = new PdfPCell(new Paragraph(
                    perInvPureFund + " %", small_normal));

            PdfPCell BI_PdftablePureFund_cell3 = new PdfPCell(new Paragraph(
                    PureFundFMC, small_normal));
            PdfPCell BI_PdftablePureFund_cell4 = new PdfPCell(new Paragraph(
                    "High", small_normal));

            BI_PdftablePureFund_cell1.setPadding(5);

            BI_PdftablePureFund_cell2
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftablePureFund_cell2.setPadding(5);

            BI_PdftablePureFund_cell3
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftablePureFund_cell3.setPadding(5);
            BI_PdftablePureFund_cell4
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftablePureFund_cell4.setPadding(5);

            BI_PdftablePureFund.addCell(BI_PdftablePureFund_cell1);
            BI_PdftablePureFund.addCell(BI_PdftablePureFund_cell2);
            BI_PdftablePureFund.addCell(BI_PdftablePureFund_cell3);
            BI_PdftablePureFund.addCell(BI_PdftablePureFund_cell4);
            document.add(BI_PdftablePureFund);


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

            if (perInvMidcapFund.equalsIgnoreCase("")) {

                perInvMidcapFund = "0";
            }

            String MidcapFundFMC = "1.35%";

            PdfPTable BI_PdftableMidcapFund = new PdfPTable(4);
            BI_PdftableMidcapFund.setWidthPercentage(100);

            PdfPCell BI_PdftableMidcapFund_cell1 = new PdfPCell(new Paragraph(
                    "Midcap Fund (SFIN : ULIF031290915MIDCAPFUND111)", small_normal));
            PdfPCell BI_PdftableMidcapFund_cell2 = new PdfPCell(new Paragraph(
                    perInvMidcapFund + " %", small_normal));

            PdfPCell BI_PdftableMidcapFund_cell3 = new PdfPCell(new Paragraph(
                    MidcapFundFMC, small_normal));
            PdfPCell BI_PdftableMidcapFund_cell4 = new PdfPCell(new Paragraph(
                    "High", small_normal));

            BI_PdftableMidcapFund_cell1.setPadding(5);

            BI_PdftableMidcapFund_cell2
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableMidcapFund_cell2.setPadding(5);

            BI_PdftableMidcapFund_cell3
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableMidcapFund_cell3.setPadding(5);
            BI_PdftableMidcapFund_cell4
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableMidcapFund_cell4.setPadding(5);

            BI_PdftableMidcapFund.addCell(BI_PdftableMidcapFund_cell1);
            BI_PdftableMidcapFund.addCell(BI_PdftableMidcapFund_cell2);
            BI_PdftableMidcapFund.addCell(BI_PdftableMidcapFund_cell3);
            BI_PdftableMidcapFund.addCell(BI_PdftableMidcapFund_cell4);
            document.add(BI_PdftableMidcapFund);


            if (perInvbondOptimiserFund2.equalsIgnoreCase("")) {

                perInvbondOptimiserFund2 = "0";
            }

            String BondOptimiserFund2FMC = "1.15%";

            PdfPTable BI_PdftableMidcapFund1 = new PdfPTable(4);
            BI_PdftableMidcapFund1.setWidthPercentage(100);

            String str_bond_optimiser_fund2 = "";
            String str_money_market_fund2 = "";
            if (STR_PLAN_AB.equals("A")) {
                str_bond_optimiser_fund2 = "Bond Optimiser Fund II (SFIN : ULIF037160919BONDOPFND2111)";
                str_money_market_fund2 = "Money Market Fund II (SFIN : ULIF036160919MONMKTFND2111)";

            } else if (STR_PLAN_AB.equals("B")) {
                str_bond_optimiser_fund2 = "Bond Optimiser Fund (SFIN : ULIF032290618BONDOPTFND111)";
                str_money_market_fund2 = "Money Market Fund (SFIN : ULIF005010206MONYMKTFND111)";
            }
            PdfPCell BI_PdftableMidcapFund_cell11 = new PdfPCell(new Paragraph(
                    str_bond_optimiser_fund2, small_normal));


            PdfPCell BI_PdftableMidcapFund_cell21 = new PdfPCell(new Paragraph(
                    perInvbondOptimiserFund2 + " %", small_normal));

            PdfPCell BI_PdftableMidcapFund_cell31 = new PdfPCell(new Paragraph(
                    BondOptimiserFund2FMC, small_normal));
            PdfPCell BI_PdftableMidcapFund_cell34 = new PdfPCell(new Paragraph(
                    "Low to Medium", small_normal));

            BI_PdftableMidcapFund_cell11.setPadding(5);

            BI_PdftableMidcapFund_cell21
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableMidcapFund_cell21.setPadding(5);

            BI_PdftableMidcapFund_cell31
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableMidcapFund_cell31.setPadding(5);
            BI_PdftableMidcapFund_cell34
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableMidcapFund_cell34.setPadding(5);

            BI_PdftableMidcapFund1.addCell(BI_PdftableMidcapFund_cell11);
            BI_PdftableMidcapFund1.addCell(BI_PdftableMidcapFund_cell21);
            BI_PdftableMidcapFund1.addCell(BI_PdftableMidcapFund_cell31);
            BI_PdftableMidcapFund1.addCell(BI_PdftableMidcapFund_cell34);
            document.add(BI_PdftableMidcapFund1);

            if (perInvmoneyMarketFund2.equalsIgnoreCase("")) {

                perInvmoneyMarketFund2 = "0";
            }

            String MoneyMarketFund2FMC = "0.25%";

            PdfPTable BI_PdftableMidcapFund2 = new PdfPTable(4);
            BI_PdftableMidcapFund2.setWidthPercentage(100);

            PdfPCell BI_PdftableMidcapFund_cell12 = new PdfPCell(new Paragraph(
                    str_money_market_fund2, small_normal));
            PdfPCell BI_PdftableMidcapFund_cell22 = new PdfPCell(new Paragraph(
                    perInvmoneyMarketFund2 + " %", small_normal));

            PdfPCell BI_PdftableMidcapFund_cell32 = new PdfPCell(new Paragraph(
                    MoneyMarketFund2FMC, small_normal));
            PdfPCell BI_PdftableMidcapFund_cell345 = new PdfPCell(new Paragraph(
                    "Low", small_normal));

            BI_PdftableMidcapFund_cell12.setPadding(5);

            BI_PdftableMidcapFund_cell22
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableMidcapFund_cell22.setPadding(5);

            BI_PdftableMidcapFund_cell32
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableMidcapFund_cell32.setPadding(5);
            BI_PdftableMidcapFund_cell345
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableMidcapFund_cell345.setPadding(5);

            BI_PdftableMidcapFund2.addCell(BI_PdftableMidcapFund_cell12);
            BI_PdftableMidcapFund2.addCell(BI_PdftableMidcapFund_cell22);
            BI_PdftableMidcapFund2.addCell(BI_PdftableMidcapFund_cell32);
            BI_PdftableMidcapFund2.addCell(BI_PdftableMidcapFund_cell345);
            document.add(BI_PdftableMidcapFund2);


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
            document.add(para_img_logo_after_space_1);

            // PdfPTable BI_PdftableNoofyearselapsedsinceinception = new
            // PdfPTable(
            // 4);
            // BI_PdftableNoofyearselapsedsinceinception.setWidthPercentage(100);
            //
            // PdfPCell
            // BI_PdftableBI_PdftableNoofyearselapsedsinceinception_cell1 = new
            // PdfPCell(
            // new Paragraph("No. of years elapsed since inception",
            // small_normal));
            // PdfPCell
            // BI_PdftableBI_PdftableNoofyearselapsedsinceinception_cell2 = new
            // PdfPCell(
            // new Paragraph(noOfYrElapsed + " Years", small_bold));
            //
            // PdfPCell
            // BI_PdftableBI_PdftableNoofyearselapsedsinceinception_cell3 = new
            // PdfPCell(
            // new Paragraph("Reduction in Yield @ 8%", small_normal));
            //
            // PdfPCell
            // BI_PdftableBI_PdftableNoofyearselapsedsinceinception_cell4 = new
            // PdfPCell(
            // new Paragraph(redInYieldNoYr + " %", small_bold));
            //
            // BI_PdftableBI_PdftableNoofyearselapsedsinceinception_cell1
            // .setHorizontalAlignment(Element.ALIGN_CENTER);
            // BI_PdftableBI_PdftableNoofyearselapsedsinceinception_cell1
            // .setPadding(5);
            //
            // BI_PdftableBI_PdftableNoofyearselapsedsinceinception_cell2
            // .setHorizontalAlignment(Element.ALIGN_CENTER);
            // BI_PdftableBI_PdftableNoofyearselapsedsinceinception_cell2
            // .setPadding(5);
            //
            // BI_PdftableBI_PdftableNoofyearselapsedsinceinception_cell3
            // .setHorizontalAlignment(Element.ALIGN_CENTER);
            // BI_PdftableBI_PdftableNoofyearselapsedsinceinception_cell3
            // .setPadding(5);
            //
            // BI_PdftableBI_PdftableNoofyearselapsedsinceinception_cell4
            // .setHorizontalAlignment(Element.ALIGN_CENTER);
            // BI_PdftableBI_PdftableNoofyearselapsedsinceinception_cell4
            // .setPadding(5);
            //
            // BI_PdftableNoofyearselapsedsinceinception
            // .addCell(BI_PdftableBI_PdftableNoofyearselapsedsinceinception_cell1);
            // BI_PdftableNoofyearselapsedsinceinception
            // .addCell(BI_PdftableBI_PdftableNoofyearselapsedsinceinception_cell2);
            // BI_PdftableNoofyearselapsedsinceinception
            // .addCell(BI_PdftableBI_PdftableNoofyearselapsedsinceinception_cell3);
            // BI_PdftableNoofyearselapsedsinceinception
            // .addCell(BI_PdftableBI_PdftableNoofyearselapsedsinceinception_cell4);
            // document.add(BI_PdftableNoofyearselapsedsinceinception);

            document.add(para_img_logo_after_space_1);

			/*PdfPTable BI_PdftableOutputHeader = new PdfPTable(4);

			BI_PdftableOutputHeader.setWidthPercentage(100);
			float[] columnWidths = { 5f, 9f, 9f, 1f };
			BI_PdftableOutputHeader.setWidths(columnWidths);
			PdfPCell BI_PdftableBI_PdftableOutputHeader_cell1 = new PdfPCell(
					new Paragraph("", small_normal));
			PdfPCell BI_PdftableBI_PdftableOutputHeader_cell2 = new PdfPCell(
					new Paragraph("Assuming gross interest rate of 4% pa",
							small_bold2));

			PdfPCell BI_PdftableBI_PdftableOutputHeader_cell3 = new PdfPCell(
					new Paragraph("Assuming gross interest rate of 8% pa",
							small_bold2));

			PdfPCell BI_PdftableBI_PdftableOutputHeader_cell4 = new PdfPCell(
					new Paragraph("", small_bold));

			BI_PdftableBI_PdftableOutputHeader_cell1
					.setHorizontalAlignment(Element.ALIGN_CENTER);
			BI_PdftableBI_PdftableOutputHeader_cell1.setPadding(5);

			BI_PdftableBI_PdftableOutputHeader_cell2
					.setHorizontalAlignment(Element.ALIGN_CENTER);
			BI_PdftableBI_PdftableOutputHeader_cell2.setPadding(5);

			BI_PdftableBI_PdftableOutputHeader_cell3
					.setHorizontalAlignment(Element.ALIGN_CENTER);
			BI_PdftableBI_PdftableOutputHeader_cell3.setPadding(5);

			BI_PdftableBI_PdftableOutputHeader_cell4
					.setHorizontalAlignment(Element.ALIGN_CENTER);
			BI_PdftableBI_PdftableOutputHeader_cell4.setPadding(5);

			BI_PdftableOutputHeader
					.addCell(BI_PdftableBI_PdftableOutputHeader_cell1);
			BI_PdftableOutputHeader
					.addCell(BI_PdftableBI_PdftableOutputHeader_cell2);
			BI_PdftableOutputHeader
					.addCell(BI_PdftableBI_PdftableOutputHeader_cell3);
			BI_PdftableOutputHeader
					.addCell(BI_PdftableBI_PdftableOutputHeader_cell4);

			document.add(BI_PdftableOutputHeader);

			PdfPTable BI_PdftablecolumnsNo = new PdfPTable(24);
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
			PdfPCell BI_Pdftable_columnsNo_cell17 = new PdfPCell(new Paragraph(
					"17", small_bold2));

			PdfPCell BI_Pdftable_columnsNo_cell18 = new PdfPCell(new Paragraph(
					"18", small_bold2));

			PdfPCell BI_Pdftable_columnsNo_cell19 = new PdfPCell(new Paragraph(
					"19", small_bold2));

			PdfPCell BI_Pdftable_columnsNo_cell20 = new PdfPCell(new Paragraph(
					"20", small_bold2));

			PdfPCell BI_Pdftable_columnsNo_cell21 = new PdfPCell(new Paragraph(
					"21", small_bold2));
			PdfPCell BI_Pdftable_columnsNo_cell22 = new PdfPCell(new Paragraph(
					"22", small_bold2));

			PdfPCell BI_Pdftable_columnsNo_cell23 = new PdfPCell(new Paragraph(
					"23", small_bold2));
			PdfPCell BI_Pdftable_columnsNo_cell24 = new PdfPCell(new Paragraph(
					"24", small_bold2));

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
			BI_PdftablecolumnsNo.addCell(BI_Pdftable_columnsNo_cell17);
			BI_PdftablecolumnsNo.addCell(BI_Pdftable_columnsNo_cell18);

			BI_PdftablecolumnsNo.addCell(BI_Pdftable_columnsNo_cell19);
			BI_PdftablecolumnsNo.addCell(BI_Pdftable_columnsNo_cell20);
			BI_PdftablecolumnsNo.addCell(BI_Pdftable_columnsNo_cell21);
			BI_PdftablecolumnsNo.addCell(BI_Pdftable_columnsNo_cell22);
			BI_PdftablecolumnsNo.addCell(BI_Pdftable_columnsNo_cell23);
			BI_PdftablecolumnsNo.addCell(BI_Pdftable_columnsNo_cell24);
			document.add(BI_PdftablecolumnsNo);

			PdfPTable BI_Pdftableoutput = new PdfPTable(24);
			BI_Pdftableoutput.setWidthPercentage(100);

			PdfPCell BI_Pdftable_output_cell1 = new PdfPCell(new Paragraph(
					"Policy Year", small_bold2));
			PdfPCell BI_Pdftable_output_cell2 = new PdfPCell(new Paragraph(
					PlanType1, small_bold2));

			PdfPCell BI_Pdftable_output_cell3 = new PdfPCell(new Paragraph(
					"Premium Allocation Charge", small_bold2));

			PdfPCell BI_Pdftable_output_cell4 = new PdfPCell(new Paragraph(
					"Amount available for investment(out of premium)",
					small_bold2));
			PdfPCell BI_Pdftable_output_cell5 = new PdfPCell(new Paragraph(
					"Policy Administration Charge", small_bold2));

			PdfPCell BI_Pdftable_output_cell6 = new PdfPCell(new Paragraph(
					"Mortality Charges", small_bold2));

			PdfPCell BI_Pdftable_output_cell7 = new PdfPCell(new Paragraph(
					"Total Charges", small_bold2));
			PdfPCell BI_Pdftable_output_cell8 = new PdfPCell(new Paragraph(
					"Total Applicable Taxes on Charges", small_bold2));

			PdfPCell BI_Pdftable_output_cell9 = new PdfPCell(new Paragraph(
					"Fund before FMC", small_bold2));

			PdfPCell BI_Pdftable_output_cell10 = new PdfPCell(new Paragraph(
					"Fund Management Charge", small_bold2));
			PdfPCell BI_Pdftable_output_cell11 = new PdfPCell(new Paragraph(
					"Loyalty Addition", small_bold2));
			PdfPCell BI_Pdftable_output_cell12 = new PdfPCell(new Paragraph(
					"Fund value at end", small_bold2));

			PdfPCell BI_Pdftable_output_cell13 = new PdfPCell(new Paragraph(
					"Surrender Value", small_bold2));
			PdfPCell BI_Pdftable_output_cell14 = new PdfPCell(new Paragraph(
					"Death Benefit", small_bold2));

			PdfPCell BI_Pdftable_output_cell15 = new PdfPCell(new Paragraph(
					"Mortality Charges", small_bold2));

			PdfPCell BI_Pdftable_output_cell16 = new PdfPCell(new Paragraph(
					"Total Charges", small_bold2));
			PdfPCell BI_Pdftable_output_cell17 = new PdfPCell(new Paragraph(
					"Total Applicable Taxes on Charges", small_bold2));

			PdfPCell BI_Pdftable_output_cell18 = new PdfPCell(new Paragraph(
					"Fund before FMC", small_bold2));

			PdfPCell BI_Pdftable_output_cell19 = new PdfPCell(new Paragraph(
					"Fund Management Charge", small_bold2));

			PdfPCell BI_Pdftable_output_cell20 = new PdfPCell(new Paragraph(
					"Loyalty Addition", small_bold2));

			PdfPCell BI_Pdftable_output_cell21 = new PdfPCell(new Paragraph(
					"Fund value at end", small_bold2));
			PdfPCell BI_Pdftable_output_cell22 = new PdfPCell(new Paragraph(
					"Surrender Value", small_bold2));

			PdfPCell BI_Pdftable_output_cell23 = new PdfPCell(new Paragraph(
					"Death Benefit", small_bold2));
			PdfPCell BI_Pdftable_output_cell24 = new PdfPCell(new Paragraph(
					"Commission/ Brokerage if payable", small_bold2));

			BI_Pdftableoutput.addCell(BI_Pdftable_output_cell1);
			BI_Pdftableoutput.addCell(BI_Pdftable_output_cell2);
			BI_Pdftableoutput.addCell(BI_Pdftable_output_cell3);

			BI_Pdftableoutput.addCell(BI_Pdftable_output_cell4);
			BI_Pdftableoutput.addCell(BI_Pdftable_output_cell5);
			BI_Pdftableoutput.addCell(BI_Pdftable_output_cell6);

			BI_Pdftableoutput.addCell(BI_Pdftable_output_cell7);
			BI_Pdftableoutput.addCell(BI_Pdftable_output_cell8);
			BI_Pdftableoutput.addCell(BI_Pdftable_output_cell9);

			BI_Pdftableoutput.addCell(BI_Pdftable_output_cell10);
			BI_Pdftableoutput.addCell(BI_Pdftable_output_cell11);
			BI_Pdftableoutput.addCell(BI_Pdftable_output_cell12);

			BI_Pdftableoutput.addCell(BI_Pdftable_output_cell13);
			BI_Pdftableoutput.addCell(BI_Pdftable_output_cell14);
			BI_Pdftableoutput.addCell(BI_Pdftable_output_cell15);

			BI_Pdftableoutput.addCell(BI_Pdftable_output_cell16);
			BI_Pdftableoutput.addCell(BI_Pdftable_output_cell17);
			BI_Pdftableoutput.addCell(BI_Pdftable_output_cell18);

			BI_Pdftableoutput.addCell(BI_Pdftable_output_cell19);
			BI_Pdftableoutput.addCell(BI_Pdftable_output_cell20);
			BI_Pdftableoutput.addCell(BI_Pdftable_output_cell21);
			BI_Pdftableoutput.addCell(BI_Pdftable_output_cell22);
			BI_Pdftableoutput.addCell(BI_Pdftable_output_cell23);
			BI_Pdftableoutput.addCell(BI_Pdftable_output_cell24);
			document.add(BI_Pdftableoutput);

			PdfPTable BI_Pdftableoutput_no = new PdfPTable(24);
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

			PdfPCell BI_Pdftable_output_no_cell15 = new PdfPCell(new Paragraph(
					"15", small_bold2));

			PdfPCell BI_Pdftable_output_no_cell16 = new PdfPCell(new Paragraph(
					"16", small_bold2));
			PdfPCell BI_Pdftable_output_no_cell17 = new PdfPCell(new Paragraph(
					"17", small_bold2));

			PdfPCell BI_Pdftable_output_no_cell18 = new PdfPCell(new Paragraph(
					"18", small_bold2));

			PdfPCell BI_Pdftable_output_no_cell19 = new PdfPCell(new Paragraph(
					"19", small_bold2));

			PdfPCell BI_Pdftable_output_no_cell20 = new PdfPCell(new Paragraph(
					"20", small_bold2));

			PdfPCell BI_Pdftable_output_no_cell21 = new PdfPCell(new Paragraph(
					"21", small_bold2));
			PdfPCell BI_Pdftable_output_no_cell22 = new PdfPCell(new Paragraph(
					"22", small_bold2));

			PdfPCell BI_Pdftable_output_no_cell23 = new PdfPCell(new Paragraph(
					"23", small_bold2));
			PdfPCell BI_Pdftable_output_no_cell24 = new PdfPCell(new Paragraph(
					"24", small_bold2));

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
			BI_Pdftableoutput_no.addCell(BI_Pdftable_output_no_cell15);

			BI_Pdftableoutput_no.addCell(BI_Pdftable_output_no_cell16);
			BI_Pdftableoutput_no.addCell(BI_Pdftable_output_no_cell17);
			BI_Pdftableoutput_no.addCell(BI_Pdftable_output_no_cell18);

			BI_Pdftableoutput_no.addCell(BI_Pdftable_output_no_cell19);
			BI_Pdftableoutput_no.addCell(BI_Pdftable_output_no_cell20);
			BI_Pdftableoutput_no.addCell(BI_Pdftable_output_no_cell21);
			BI_Pdftableoutput_no.addCell(BI_Pdftable_output_no_cell22);
			BI_Pdftableoutput_no.addCell(BI_Pdftable_output_no_cell23);
			BI_Pdftableoutput_no.addCell(BI_Pdftable_output_no_cell24);
			document.add(BI_Pdftableoutput_no);

			for (int i = 0; i < Integer.parseInt(policyTerm); i++) {

				PdfPTable BI_Pdftableoutput_row1 = new PdfPTable(24);
				BI_Pdftableoutput_row1.setWidthPercentage(100);

				PdfPCell BI_Pdftable_output_row1_cell1 = new PdfPCell(
						new Paragraph(list_data.get(i).getPolicy_year(),
								small_bold2));
				PdfPCell BI_Pdftable_output_row1_cell2 = new PdfPCell(
						new Paragraph(list_data.get(i).getPremium(),
								small_bold2));

				PdfPCell BI_Pdftable_output_row1_cell3 = new PdfPCell(
						new Paragraph(list_data.get(i)
								.getPremium_allocation_charge(), small_bold2));

				PdfPCell BI_Pdftable_output_row1_cell4 = new PdfPCell(
						new Paragraph(list_data.get(i)
								.getAmount_for_investment(), small_bold2));
				PdfPCell BI_Pdftable_output_row1_cell5 = new PdfPCell(
						new Paragraph(list_data.get(i)
								.getPolicy_administration_charge(), small_bold2));

				PdfPCell BI_Pdftable_output_row1_cell6 = new PdfPCell(
						new Paragraph(list_data.get(i).getMortality_charge1(),
								small_bold2));

				PdfPCell BI_Pdftable_output_row1_cell7 = new PdfPCell(
						new Paragraph(list_data.get(i).getTotal_charge1(),
								small_bold2));
				PdfPCell BI_Pdftable_output_row1_cell8 = new PdfPCell(
						new Paragraph(list_data.get(i)
								.getService_tax_on_mortality_charge1(),
								small_bold2));

				PdfPCell BI_Pdftable_output_row1_cell9 = new PdfPCell(
						new Paragraph(list_data.get(i).getFund_before_fmc1(),
								small_bold2));

				PdfPCell BI_Pdftable_output_row1_cell10 = new PdfPCell(
						new Paragraph(list_data.get(i)
								.getFund_management_charge1(), small_bold2));
				PdfPCell BI_Pdftable_output_row1_cell11 = new PdfPCell(
						new Paragraph(
								list_data.get(i).getGuranteed_addition1(),
								small_bold2));

				PdfPCell BI_Pdftable_output_row1_cell12 = new PdfPCell(
						new Paragraph(list_data.get(i).getFund_value_at_end1(),
								small_bold2));

				PdfPCell BI_Pdftable_output_row1_cell13 = new PdfPCell(
						new Paragraph(list_data.get(i).getSurrender_value1(),
								small_bold2));

				PdfPCell BI_Pdftable_output_row1_cell14 = new PdfPCell(
						new Paragraph(list_data.get(i).getDeath_benefit1(),
								small_bold2));

				PdfPCell BI_Pdftable_output_row1_cell15 = new PdfPCell(
						new Paragraph(list_data.get(i).getMortality_charge2(),
								small_bold2));

				PdfPCell BI_Pdftable_output_row1_cell16 = new PdfPCell(
						new Paragraph(list_data.get(i).getTotal_charge2(),
								small_bold2));

				PdfPCell BI_Pdftable_output_row1_cell17 = new PdfPCell(
						new Paragraph(list_data.get(i)
								.getService_tax_on_mortality_charge2(),
								small_bold2));

				PdfPCell BI_Pdftable_output_row1_cell18 = new PdfPCell(
						new Paragraph(list_data.get(i).getFund_before_fmc2(),
								small_bold2));

				PdfPCell BI_Pdftable_output_row1_cell19 = new PdfPCell(
						new Paragraph(list_data.get(i)
								.getFund_management_charge2(), small_bold2));
				PdfPCell BI_Pdftable_output_row1_cell20 = new PdfPCell(
						new Paragraph(
								list_data.get(i).getGuranteed_addition2(),
								small_bold2));

				PdfPCell BI_Pdftable_output_row1_cell21 = new PdfPCell(
						new Paragraph(list_data.get(i).getFund_value_at_end2(),
								small_bold2));
				PdfPCell BI_Pdftable_output_row1_cell22 = new PdfPCell(
						new Paragraph(list_data.get(i).getSurrender_value2(),
								small_bold2));

				PdfPCell BI_Pdftable_output_row1_cell23 = new PdfPCell(
						new Paragraph(list_data.get(i).getDeath_benefit2(),
								small_bold2));

				PdfPCell BI_Pdftable_output_row1_cell24 = new PdfPCell(
						new Paragraph(list_data.get(i).getCommission(),
								small_bold2));

				BI_Pdftableoutput_row1.addCell(BI_Pdftable_output_row1_cell1);
				BI_Pdftableoutput_row1.addCell(BI_Pdftable_output_row1_cell2);
				BI_Pdftableoutput_row1.addCell(BI_Pdftable_output_row1_cell3);

				BI_Pdftableoutput_row1.addCell(BI_Pdftable_output_row1_cell4);
				BI_Pdftableoutput_row1.addCell(BI_Pdftable_output_row1_cell5);
				BI_Pdftableoutput_row1.addCell(BI_Pdftable_output_row1_cell6);

				BI_Pdftableoutput_row1.addCell(BI_Pdftable_output_row1_cell7);
				BI_Pdftableoutput_row1.addCell(BI_Pdftable_output_row1_cell8);
				BI_Pdftableoutput_row1.addCell(BI_Pdftable_output_row1_cell9);

				BI_Pdftableoutput_row1.addCell(BI_Pdftable_output_row1_cell10);
				BI_Pdftableoutput_row1.addCell(BI_Pdftable_output_row1_cell11);
				BI_Pdftableoutput_row1.addCell(BI_Pdftable_output_row1_cell12);

				BI_Pdftableoutput_row1.addCell(BI_Pdftable_output_row1_cell13);
				BI_Pdftableoutput_row1.addCell(BI_Pdftable_output_row1_cell14);
				BI_Pdftableoutput_row1.addCell(BI_Pdftable_output_row1_cell15);

				BI_Pdftableoutput_row1.addCell(BI_Pdftable_output_row1_cell16);
				BI_Pdftableoutput_row1.addCell(BI_Pdftable_output_row1_cell17);
				BI_Pdftableoutput_row1.addCell(BI_Pdftable_output_row1_cell18);

				BI_Pdftableoutput_row1.addCell(BI_Pdftable_output_row1_cell19);
				BI_Pdftableoutput_row1.addCell(BI_Pdftable_output_row1_cell20);
				BI_Pdftableoutput_row1.addCell(BI_Pdftable_output_row1_cell21);
				BI_Pdftableoutput_row1.addCell(BI_Pdftable_output_row1_cell22);
				BI_Pdftableoutput_row1.addCell(BI_Pdftable_output_row1_cell23);
				BI_Pdftableoutput_row1.addCell(BI_Pdftable_output_row1_cell24);
				document.add(BI_Pdftableoutput_row1);

			}*/

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
                    "Commission payable to intermediary(Rs.)", small_bold2));

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
            PdfPCell cell221 = new PdfPCell(new Paragraph("IN THIS POLICY, THE INVESTMENT RISK IS BORNE BY THE POLICYHOLDER AND THE ABOVE INTEREST RATES ARE ONLY FOR ILLUSTRATIVE PURPOSE", small_bold2));
            cell221.setHorizontalAlignment(Element.ALIGN_CENTER);
            t22.addCell(cell22);
            t22.addCell(cell221);
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
            document.add(BI_Pdftable26_cell1);
//			document.add(new_line);

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
                    new Paragraph(agentdeclaration));

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
                    new Paragraph("Net Yield " + netYield + "%", small_bold2));

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
            BI_Pdftableoutput8.addCell(BI_Pdftable_output_cell781);
            BI_Pdftableoutput8.addCell(BI_Pdftable_output_cell88);
            BI_Pdftableoutput8.addCell(BI_Pdftable_output_cell881);
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
            //document.add(BI_Pdftableoutput_no8);

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
                    new Paragraph("Net Yield -  " + netYield + " %", small_bold2));

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
            BI_Pdftableoutput4.addCell(BI_Pdftable_output_cell7811);
            BI_Pdftableoutput4.addCell(BI_Pdftable_output_cell8814);
            BI_Pdftableoutput4.addCell(BI_Pdftable_output_cell8811);
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
            //document.add(BI_Pdftableoutput_no4);

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

            PdfPTable BI_PdftableMaturityAt = new PdfPTable(4);
            BI_PdftableMaturityAt.setWidthPercentage(100);

            PdfPCell BI_PdftableBI_PdftableMaturityAt_cell1 = new PdfPCell(
                    new Paragraph("Net Yield at 8% pa", small_normal));
            PdfPCell BI_PdftableBI_PdftableMaturityAt_cell2 = new PdfPCell(
                    new Paragraph(netYield + " %", small_bold));

            PdfPCell BI_PdftableBI_PdftableMaturityAt_cell3 = new PdfPCell(
                    new Paragraph("Reduction in Yield at 8% pa", small_normal));

            PdfPCell BI_PdftableBI_PdftableMaturityAt_cell4 = new PdfPCell(
                    new Paragraph(reductionInYield + " %", small_bold));

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

            PdfPTable BI_Pdftable_note = new PdfPTable(1);
            BI_Pdftable_note.setWidthPercentage(100);
            PdfPCell BI_Pdftable_note_cell1 = new PdfPCell(new Paragraph(
                    "Notes", small_bold));
            BI_Pdftable_note_cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_Pdftable_note_cell1.setPadding(5);

            BI_Pdftable_note.addCell(BI_Pdftable_note_cell1);
            document.add(BI_Pdftable_note);

            PdfPTable BI_Pdftable6 = new PdfPTable(1);
            BI_Pdftable6.setWidthPercentage(100);
            PdfPCell BI_Pdftable6_cell6 = new PdfPCell(
                    new Paragraph(
                            "1) Refer the sales literature for explanation of terms used in this illustration",

                            small_normal));

            BI_Pdftable6_cell6.setPadding(5);

            BI_Pdftable6.addCell(BI_Pdftable6_cell6);
            document.add(BI_Pdftable6);

            PdfPTable BI_Pdftable7 = new PdfPTable(1);
            BI_Pdftable7.setWidthPercentage(100);
            PdfPCell BI_Pdftable7_cell1 = new PdfPCell(
                    new Paragraph(
                            "2) Please read this benefit illustration in conjunction with Sales Brochure and the Policy Document to understand all Terms, Conditions & Exclusions carefully.",

                            small_normal));

            BI_Pdftable7_cell1.setPadding(5);

            BI_Pdftable7.addCell(BI_Pdftable7_cell1);
            document.add(BI_Pdftable7);

            PdfPTable BI_Pdftable8 = new PdfPTable(1);
            BI_Pdftable8.setWidthPercentage(100);
            PdfPCell BI_Pdftable8_cell1 = new PdfPCell(
                    new Paragraph(
                            "3) Kindly note that above is only an illustration and does not in any way create any rights and/or obligations. The actual experience on the contract may be different from what is illustrated. The non-guaranteed low and high rate mentioned above relate to assumed investment returns at different rates and may vary depending upon market conditions. For more details on risk factors, terms and conditions please read sales brochure carefully.",

                            small_normal));

            BI_Pdftable8_cell1.setPadding(5);

            BI_Pdftable8.addCell(BI_Pdftable8_cell1);
            document.add(BI_Pdftable8);

            PdfPTable BI_Pdftable9 = new PdfPTable(1);
            BI_Pdftable9.setWidthPercentage(100);
            PdfPCell BI_Pdftable9_cell1 = new PdfPCell(
                    new Paragraph(
                            "4) The unit values may go up as well as down and past performance is no indication of future performance on the part of SBI Life Insurance Co. Ltd. We would request you to appreciate the associated risk under this plan vis-à-vis the likely future returns before taking your investment decision.",

                            small_normal));

            BI_Pdftable9_cell1.setPadding(5);

            BI_Pdftable9.addCell(BI_Pdftable9_cell1);
            document.add(BI_Pdftable9);

            PdfPTable BI_Pdftable10 = new PdfPTable(1);
            BI_Pdftable10.setWidthPercentage(100);
            PdfPCell BI_Pdftable10_cell1 = new PdfPCell(
                    new Paragraph(
                            "5) It is assumed that the policy is in force throughout the term.",
                            small_normal));

            BI_Pdftable10_cell1.setPadding(5);

            BI_Pdftable10.addCell(BI_Pdftable10_cell1);
            document.add(BI_Pdftable10);

            PdfPTable BI_Pdftable11 = new PdfPTable(1);
            BI_Pdftable11.setWidthPercentage(100);
            PdfPCell BI_Pdftable11_cell1 = new PdfPCell(
                    new Paragraph(
                            "6) Fund management charge is based on the specific  investment strategy / fund option(s) chosen",
                            small_normal));

            BI_Pdftable11_cell1.setPadding(5);

            BI_Pdftable11.addCell(BI_Pdftable11_cell1);
            document.add(BI_Pdftable11);

            PdfPTable BI_Pdftable12 = new PdfPTable(1);
            BI_Pdftable12.setWidthPercentage(100);
            PdfPCell BI_Pdftable12_cell1 = new PdfPCell(
                    new Paragraph(
                            "7) Surrender Value equals the Fund Value at the end of the year minus Discontinuance Charges. Surrender value is available on or after 5th policy anniversary.",
                            small_normal));

            BI_Pdftable12_cell1.setPadding(5);

            BI_Pdftable12.addCell(BI_Pdftable12_cell1);
            document.add(BI_Pdftable12);

            PdfPTable BI_Pdftable13 = new PdfPTable(1);
            BI_Pdftable13.setWidthPercentage(100);
            PdfPCell BI_Pdftable13_cell1 = new PdfPCell(
                    new Paragraph(
                            "8) Acceptance of proposal is subject to Underwriting decision. Mortality charges are for a healthy person.",
                            small_normal));

            BI_Pdftable13_cell1.setPadding(5);

            BI_Pdftable13.addCell(BI_Pdftable13_cell1);
            document.add(BI_Pdftable13);

            PdfPTable BI_Pdftable131 = new PdfPTable(1);
            BI_Pdftable131.setWidthPercentage(100);
            PdfPCell BI_Pdftable131_cell1 = new PdfPCell(
                    new Paragraph(
                            "9) Applicable Taxes (including surcharge/cess etc), at the rate notified by the Central Government/ State Government / Union Territories of India from time to time and as per the provisions of the prevalent tax laws will be payable on premium/ or any other charges as per the product features.",
                            small_normal));

            BI_Pdftable131_cell1.setPadding(5);

            BI_Pdftable131.addCell(BI_Pdftable131_cell1);
            document.add(BI_Pdftable131);

            PdfPTable BI_Pdftable_TAX_FMC = new PdfPTable(1);
            BI_Pdftable_TAX_FMC.setWidthPercentage(100);
            PdfPCell BI_Pdftable_TAX_FMC_cell1 = new PdfPCell(new Paragraph(
                    "10)	This policy provides guaranteed death benefit of Rs. "
                            + obj.getRound(obj.getStringWithout_E(Double
                            .valueOf(sumAssured.equals("") ? "0"
                                    : sumAssured))),

                    small_normal));

            BI_Pdftable_TAX_FMC_cell1.setPadding(5);

            BI_Pdftable_TAX_FMC.addCell(BI_Pdftable_TAX_FMC_cell1);
            document.add(BI_Pdftable_TAX_FMC);

            PdfPTable BI_Pdftable14 = new PdfPTable(1);
            BI_Pdftable14.setWidthPercentage(100);
            PdfPCell BI_Pdftable14_cell1 = new PdfPCell(
                    new Paragraph(
                            "11) Net Yield have been calculated after applying all the charges (except GST, mortality charges).",

                            small_normal));

            BI_Pdftable14_cell1.setPadding(5);

            BI_Pdftable14.addCell(BI_Pdftable14_cell1);
            document.add(BI_Pdftable14);

            document.add(para_img_logo_after_space_1);

            //document.newPage();
            PdfPTable BI_Pdftable19 = new PdfPTable(1);
            BI_Pdftable19.setWidthPercentage(100);
            PdfPCell BI_Pdftable19_cell1 = new PdfPCell(new Paragraph(
                    "Definition of Various Charges", small_bold));
            BI_Pdftable19_cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_Pdftable19_cell1.setPadding(5);

            BI_Pdftable19.addCell(BI_Pdftable19_cell1);
            document.add(BI_Pdftable19);

            PdfPTable BI_Pdftable20 = new PdfPTable(1);
            BI_Pdftable20.setWidthPercentage(100);
            PdfPCell BI_Pdftable20_cell1 = new PdfPCell(
                    new Paragraph(
                            "1) Policy Administration Charges : a charge of a fixed sum which is applied at the beginning of each policy month by cancelling units for equivalent amount, deducted for maintaining the policy.",
                            small_normal));

            BI_Pdftable20_cell1.setPadding(5);

            BI_Pdftable20.addCell(BI_Pdftable20_cell1);
            document.add(BI_Pdftable20);

            PdfPTable BI_Pdftable21 = new PdfPTable(1);
            BI_Pdftable21.setWidthPercentage(100);
            PdfPCell BI_Pdftable21_cell1 = new PdfPCell(
                    new Paragraph(
                            "2) Premium Allocation Charge : is the percentage of premium that would not be utilised to purchase units.",
                            small_normal));

            BI_Pdftable21_cell1.setPadding(5);

            BI_Pdftable21.addCell(BI_Pdftable21_cell1);
            document.add(BI_Pdftable21);

            PdfPTable BI_Pdftable22 = new PdfPTable(1);
            BI_Pdftable22.setWidthPercentage(100);
            PdfPCell BI_Pdftable22_cell1 = new PdfPCell(
                    new Paragraph(
                            "3) Mortality Charges : are the charges recovered for providing life insurance cover, deducted applied at the beginning of each policy month by cancelling units for equivalent amount.",
                            small_normal));

            BI_Pdftable22_cell1.setPadding(5);

            BI_Pdftable22.addCell(BI_Pdftable22_cell1);
            document.add(BI_Pdftable22);

            PdfPTable BI_Pdftable23 = new PdfPTable(1);
            BI_Pdftable23.setWidthPercentage(100);
            PdfPCell BI_Pdftable23_cell1 = new PdfPCell(
                    new Paragraph(
                            "4) Fund Management Charge (FMC) : is the deduction made from the fund at a stated percentage before the computation of the NAV of the fund.",
                            small_normal));

            BI_Pdftable23_cell1.setPadding(5);

            BI_Pdftable23.addCell(BI_Pdftable23_cell1);
            document.add(BI_Pdftable23);
            document.add(para_img_logo_after_space_1);


            PdfPTable BI_Pdftable191 = new PdfPTable(1);
            BI_Pdftable191.setWidthPercentage(100);
            PdfPCell BI_Pdftable191_cell1 = new PdfPCell(new Paragraph(
                    "Important:", small_bold));
            BI_Pdftable191_cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_Pdftable191_cell1.setPadding(5);

            BI_Pdftable191.addCell(BI_Pdftable191_cell1);

            PdfPTable BI_Pdftable_CompanysPolicySurrender2 = new PdfPTable(1);
            BI_Pdftable_CompanysPolicySurrender2.setWidthPercentage(100);
            PdfPCell BI_Pdftable_CompanysPolicySurrender2_cell = new PdfPCell(
                    new Paragraph(
                            "You may receive a Welcome Call from our representative to confirm your proposal details like Date of Birth, Nominee Name, Address, Email ID, Sum Assured, Premium amount, Premium Payment Term etc.",
                            small_normal));

            BI_Pdftable_CompanysPolicySurrender2_cell.setPadding(5);

            BI_Pdftable_CompanysPolicySurrender2
                    .addCell(BI_Pdftable_CompanysPolicySurrender2_cell);
            document.add(BI_Pdftable191);
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

            if (Double.parseDouble(premiumAmount) > 100000) {
                document.add(BI_Pdftable_CompanysPolicySurrender4);
            }
            //document.add(BI_Pdftable_CompanysPolicySurrender4);

            PdfPTable BI_Pdftable_CompanysPolicySurrender5 = new PdfPTable(1);
            BI_Pdftable_CompanysPolicySurrender5.setWidthPercentage(100);
            PdfPCell BI_Pdftable_CompanysPolicySurrender5_cell = new PdfPCell(
                    new Paragraph(Company_policy_surrender_dec, small_normal));

            BI_Pdftable_CompanysPolicySurrender5_cell.setPadding(5);

            BI_Pdftable_CompanysPolicySurrender5
                    .addCell(BI_Pdftable_CompanysPolicySurrender5_cell);
            document.add(BI_Pdftable_CompanysPolicySurrender5);

            document.add(para_img_logo_after_space_1);
            document.add(para_img_logo_after_space_1);

            //Added Disclaimer Second Time
            document.add(BI_Pdftable26);
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
                if (BI_PdftableMarketing != null) {
                    document.add(BI_PdftableMarketing);
                }
                if (BI_PdftableMarketing_signature != null) {
                    document.add(BI_PdftableMarketing_signature);
                }
            }

            document.close();

        } catch (Exception e) {
            Log.i(getLocalClassName(), e.toString() + "Error in creating pdf");
        }
    }

    private void windowmessagesgin() {

        d = new Dialog(BI_SmartPrivilegeActivity.this);
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
                Intent intent = new Intent(BI_SmartPrivilegeActivity.this,
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

    // Store user input in Bean object
    private void addListenerOnSubmit() {
        smartPrivilegeBean = new SmartPrivilegeBean();

        // System.out.println("********* 1 ***********");
        if (isStaffDisc.isChecked()) {
            smartPrivilegeBean.setIsForStaffOrNot(true);
        } else {
            smartPrivilegeBean.setIsForStaffOrNot(false);
        }

        if (cb_kerladisc.isChecked()) {
            smartPrivilegeBean.setKerlaDisc(true);
            //smartPrivilegeBean.setServiceTax(true);
        } else {
            //smartPrivilegeBean.setServiceTax(false);
            smartPrivilegeBean.setKerlaDisc(false);
        }

        // System.out.println("********* 2 ***********");
        smartPrivilegeBean.setAgeAtEntry(Integer.parseInt(ageInYears
                .getSelectedItem().toString()));
        // System.out.println("********* 3 ***********");
        smartPrivilegeBean.setGender(selGender.getSelectedItem().toString());
        // System.out.println("********* 4 ***********");
        smartPrivilegeBean.setPlanType(selPlan.getSelectedItem().toString());

        if (selPlan.getSelectedItem().toString().equals("Single")) {
            // System.out.println("********* 4.1 ***********");
            smartPrivilegeBean.setPremFreq("Single");
            // System.out.println("********* 4.2 ***********");
            smartPrivilegeBean.setPremiumPayingTerm(1);
        } else if (selPlan.getSelectedItem().toString().equals("Regular")) {
            // System.out.println("********* 4.3 ***********");
            smartPrivilegeBean.setPremFreq(selPremiumFrequencyMode
                    .getSelectedItem().toString());
            // System.out.println("********* 4.4 ***********");
            smartPrivilegeBean.setPremiumPayingTerm(1);
        } else if (selPlan.getSelectedItem().toString().equals("Limited")) {
            // System.out.println("********* 4.5 ***********");
            smartPrivilegeBean.setPremFreq(selPremiumFrequencyMode
                    .getSelectedItem().toString());
            smartPrivilegeBean.setPremiumPayingTerm(Integer
                    .parseInt(premPayingTerm.getSelectedItem().toString()));
        }

        // }

        // System.out.println("********* 6 ***********");
        smartPrivilegeBean.setPolicyTerm_Basic(Integer.parseInt(selPolicyTerm
                .getSelectedItem().toString()));

        // System.out.println("********* 7 ***********");
        smartPrivilegeBean.setPremiumAmount(Double.parseDouble(premiumAmt
                .getText().toString()));

        // System.out.println("********* 8 ***********");
        smartPrivilegeBean.setSAMF(Double
                .parseDouble(SAMF.getText().toString()));

        // System.out.println("********* 9 ***********");
        // smartPrivilegeBean.setNoOfYearsElapsedSinceInception(Integer
        // .parseInt(noOfYearsElapsedSinceInception.getText().toString()));

        // System.out.println("********* 10 ***********");
        // smartPrivilegeBean.setEffectivePremium(Double
        // .parseDouble(getEffectivePremium()));

        // System.out.println("********* 11 ***********");
        smartPrivilegeBean.setPF(1);

        if (!percent_EquityFund.getText().toString().equals(""))
            smartPrivilegeBean.setPercentToBeInvested_EquityFund(Double
                    .parseDouble(percent_EquityFund.getText().toString()));
        else
            smartPrivilegeBean.setPercentToBeInvested_EquityFund(0);

        if (!percent_EquityOptFund.getText().toString().equals(""))
            smartPrivilegeBean.setPercentToBeInvested_EquityOptFund(Double
                    .parseDouble(percent_EquityOptFund.getText().toString()));
        else
            smartPrivilegeBean.setPercentToBeInvested_EquityOptFund(0);

        if (!percent_GrowthFund.getText().toString().equals(""))
            smartPrivilegeBean.setPercentToBeInvested_GrowthFund(Double
                    .parseDouble(percent_GrowthFund.getText().toString()));
        else
            smartPrivilegeBean.setPercentToBeInvested_GrowthFund(0);

        if (!percent_BalancedFund.getText().toString().equals(""))
            smartPrivilegeBean.setPercentToBeInvested_BalancedFund(Double
                    .parseDouble(percent_BalancedFund.getText().toString()));
        else
            smartPrivilegeBean.setPercentToBeInvested_BalancedFund(0);

        if (!percent_BondFund.getText().toString().equals(""))
            smartPrivilegeBean.setPercentToBeInvested_BondFund(Double
                    .parseDouble(percent_BondFund.getText().toString()));
        else
            smartPrivilegeBean.setPercentToBeInvested_BondFund(0);

        if (!percent_Top300Fund.getText().toString().equals(""))
            smartPrivilegeBean.setPercentToBeInvested_Top300Fund(Double
                    .parseDouble(percent_Top300Fund.getText().toString()));
        else
            smartPrivilegeBean.setPercentToBeInvested_Top300Fund(0);

        if (!percent_PureFund.getText().toString().equals(""))
            smartPrivilegeBean.setPercentToBeInvested_PureFund(Double
                    .parseDouble(percent_PureFund.getText().toString()));
        else
            smartPrivilegeBean.setPercentToBeInvested_PureFund(0);

        if (!percent_MidcapFund.getText().toString().equals(""))
            smartPrivilegeBean.setPercentToBeInvested_MidcapFund(Double
                    .parseDouble(percent_MidcapFund.getText().toString()));
        else
            smartPrivilegeBean.setPercentToBeInvested_MidcapFund(0);

        if (!percent_CorporateBondFund.getText().toString().equals(""))
            smartPrivilegeBean.setPercentToBeInvested_CorpBondFund(Double
                    .parseDouble(percent_CorporateBondFund.getText().toString()));
        else
            smartPrivilegeBean.setPercentToBeInvested_CorpBondFund(0);


        if (!percent_bondOptimiserFund2.getText().toString().equals(""))
            smartPrivilegeBean.setPercentToBeInvested_bondOptimiserFund2(Double
                    .parseDouble(percent_bondOptimiserFund2.getText().toString()));
        else
            smartPrivilegeBean.setPercentToBeInvested_bondOptimiserFund2(0);

        if (!percent_moneyMarketFund2.getText().toString().equals(""))
            smartPrivilegeBean.setPercentToBeInvested_moneyMarketFund2(Double
                    .parseDouble(percent_moneyMarketFund2.getText().toString()));
        else
            smartPrivilegeBean.setPercentToBeInvested_moneyMarketFund2(0);


        // Show smart privilege Output Screen
        showSmartPrivilegeOutputPg(smartPrivilegeBean);
    }

    /******************************************* calculation part starts here ******************************************/
    private void showSmartPrivilegeOutputPg(SmartPrivilegeBean smartPrivilegeBean) {

        arrGuaranteedAddition_AC = new double[400];
        arrGuaranteedAddition_AU = new double[400];

        String[] outputArr = getOutput("BI_Incl_Mort & Ser Tax",
                smartPrivilegeBean);
        String[] outputArrReductionYield = getOutputReductionInYield(
                "Benefit Illustrator_CAP", smartPrivilegeBean);
        String mode = "";
        if (smartPrivilegeBean.getPlanType().equals("Single"))
            mode = "Single";
        else
            mode = "Yearly";
        try {
            retVal = new StringBuilder();
            retVal.append("<?xml version='1.0' encoding='utf-8' ?><smartPrivilege>");

            if (isStaffDisc.isChecked()) {

                retVal.append("<staffStatus>sbi</staffStatus>"
                        + "<staffRebate>" + "0" + "</staffRebate>");

            } else {
                retVal.append("<staffStatus>none</staffStatus>"
                        + "<staffRebate>" + "0" + "</staffRebate>");
            }

            retVal.append("<OccuInt>").append(MinesOccuInterest).append("</OccuInt>");
            /* <netYield8pa> added by Akshaya on 18-Feb-2014 **/
            retVal.append("<errCode>0</errCode>" + "<maturityAge>").append(smartPrivilegeBean.getAgeAtEntry() + smartPrivilegeBean
                    .getPolicyTerm_Basic()).append("</maturityAge>").append("<annPrem>").append(obj.getStringWithout_E(annualPremium)).append("</annPrem>").append("<premPayTerm>").append(smartPrivilegeBean.getPremiumPayingTerm()).append("</premPayTerm>").append("<mode>").append(mode).append("</mode>").append("<netYield>").append(outputArrReductionYield[0]).append("</netYield>").append("<reductionInYield>").append(outputArrReductionYield[1]).append("</reductionInYield>").append("<sumAssured>").append(Double.parseDouble(outputArr[0])).append("</sumAssured>").append("<fundVal4>").append(outputArr[1]).append("</fundVal4>").append("<fundVal8>").append(outputArr[2]).append("</fundVal8>").append(outputArr[3]);

            int index = smartPrivilegeBean.getPolicyTerm_Basic();
            String FundValAtEnd4Pr = prsObj.parseXmlTag(bussIll.toString(), "FundValAtEnd4Pr" + index + "");
            String FundValAtEnd8Pr = prsObj.parseXmlTag(bussIll.toString(), "FundValAtEnd8Pr" + index + "");

            retVal.append("<FundValAtEnd4Pr" + index + ">" + FundValAtEnd4Pr + "</FundValAtEnd4Pr" + index + ">");
            retVal.append("<FundValAtEnd8Pr" + index + ">" + FundValAtEnd8Pr + "</FundValAtEnd8Pr" + index + ">");

            retVal.append(bussIll.toString());

            retVal.append("</smartPrivilege>");

        } catch (Exception e) {
            retVal.append("<?xml version='1.0' encoding='utf-8' ?><smartPrivilege>" + "<errCode>1</errCode>" + "<errorMessage>").append(e.getMessage()).append("</errorMessage></smartPrivilege>");
        }

        /********************************************* xml Output *************************************/

    }

    private String[] getOutput(String sheetName,
                               SmartPrivilegeBean smartprivilegeBean) {

        // commonForAllProd=new CommonForAllProd();
        SmartPrivilegeProperties prop = new SmartPrivilegeProperties();

        // ouput variable declaration
        int _month_E = 0, _year_F = 0, _age_H = 0;

        String _policyInforce_G = "Y";

        // temp variable declaration.
        int month_E = 0, year_F = 0, age_H = 0;

        String policyInforce_G = "Y";

        bussIll = new StringBuilder();

        // For BI
        double sum_I = 0, sum_K = 0, sum_01 = 0, sum_N = 0, sum_N1 = 0, sum_Q = 0, sum_02 = 0, sum_R = 0, sum_S = 0, sum_U = 0, sum_X = 0, sum_03 = 0, sum_AA = 0, sum_AG = 0, sum_AH = 0, sum_AJ = 0, sum_AM = 0, sum1_03 = 0, sum_AZ = 0, sum_AV = 0, otherCharges_PartB = 0, otherCharges1_PartB = 0, otherCharges_PartA = 0, otherCharges1_PartA = 0, sum_AP = 0, Commission_BL = 0, sum_J = 0;

        double _sum_X = 0, _sum_AM = 0, _sum_J = 0, _sum_I = 0;

        double premium_I = 0, last2YearAvgFundValue_AR = 0, last2YearAvgFundValue_AB = 0, topUpPremium_J = 0, premiumAllocationCharge_K = 0, topUpCharges_L = 0, ServiceTaxOnAllocation_M = 0, amountAvailableForInvestment_N = 0, amountAvailableForInvestment_N1 = 0, sumAssuredRelatedCharges_O = 0, riderCharges_P = 0, policyAdministrationCharges_Q = 0, mortalityCharges_R = 0, totalCharges_S = 0, serviceTaxExclOfSTOnAllocAndSurr_T = 0, totalServiceTax_U = 0, AccTPDCharges_S = 0, ppwbCharges_O = 0, riderBenefitCharges_P = 0, AdbNAtpdCharges_Q = 0, additionToFundIfAny_V = 0, fundBeforeFMC_W = 0, terminalAddition_AA = 0, guaranteeCharge_Z = 0, fundManagementCharge_X = 0, serviceTaxOnFMC_Y = 0, fundValueAfterFMCBerforeGA_Z = 0, guaranteedAddition_AA = 0, fundValueAtEnd_AB = 0, surrenderCharges_AC = 0, serviceTaxOnSurrenderCharges_AD = 0, surrenderValue_AE = 0, deathBenefit_AF = 0, mortalityCharges_AG = 0, totalCharges_AH = 0, serviceTaxExclOfSTOnAllocAndSurr_AI = 0, totalServiceTax_AJ = 0, additionToFundIfAny_AK = 0, fundBeforeFMC_AL = 0, fundManagementCharge_AM = 0, serviceTaxOnFMC_AN = 0, fundValueAfterFMCBerforeGA_AO = 0, guaranteedAddition_AP = 0, fundValueAtEnd_AQ = 0, surrenderCharges_AR = 0, serviceTaxOnSurrenderCharges_AS = 0, surrenderValue_AT = 0, deathBenefit_AU = 0, surrenderCap_AV = 0, oneHundredPercentOfCummulativePremium_AW = 0, terminalAddition_AV = 0;

        // from GUI inputs
        boolean StaffDisc = smartprivilegeBean.getIsForStaffOrNot();
        boolean bancAssuranceDisc = false;
        int ageAtEntry = smartprivilegeBean.getAgeAtEntry();

//  Added By Saurabh Jain on 10/06/2019 Start

        double serviceTax = 0;
        boolean isKerlaDisc = smartprivilegeBean.isKerlaDisc();

//Added By Saurabh Jain on 10/06/2019 End

        int policyTerm = smartprivilegeBean.getPolicyTerm_Basic();
        String planType = smartprivilegeBean.getPlanType();
        String premFreq = smartprivilegeBean.getPremFreq();
        int premiumPayingTerm = smartprivilegeBean.getPremiumPayingTerm();
        double premiumAmt = smartprivilegeBean.getPremiumAmount();
        double SAMF = smartprivilegeBean.getSAMF();
        double percentToBeInvested_EquityFund = smartprivilegeBean
                .getPercentToBeInvested_EquityFund();
        double percentToBeInvested_EquityOptFund = smartprivilegeBean
                .getPercentToBeInvested_EquityOptFund();
        double percentToBeInvested_GrowthFund = smartprivilegeBean
                .getPercentToBeInvested_GrowthFund();
        double percentToBeInvested_BalancedFund = smartprivilegeBean
                .getPercentToBeInvested_BalancedFund();
        double percentToBeInvested_BondFund = smartprivilegeBean
                .getPercentToBeInvested_BondFund();
        double percentToBeInvested_PureFund = smartprivilegeBean
                .getPercentToBeInvested_PureFund();
        double percentToBeInvested_MidCapFund = smartprivilegeBean
                .getPercentToBeInvested_MidcapFund();
        double percentToBeInvested_Top300Fund = smartprivilegeBean
                .getPercentToBeInvested_Top300Fund();
        double percentToBeInvested_BondOptimiserFundII = smartprivilegeBean
                .getPercentToBeInvested_bondOptimiserFund2();
        double percentToBeInvested_MoneyMarketFundII = smartprivilegeBean
                .getPercentToBeInvested_moneyMarketFund2();
        double percentToBeInvested_CorpBondFund = smartprivilegeBean
                .getPercentToBeInvested_CorpBondFund();
        String transferFundStatus = "No Transfer";
        String addTopUp = "No";

        annualPremium = getAnnualPremium(premiumAmt, premFreq);
        //int PF = getPremFreqMode();
        int PF = getPremFreqMode(smartprivilegeBean.getPremFreq());
        //int PPT = calPPT(premiumPayingTerm, policyTerm);
        int PPT = calPPT(premiumPayingTerm, policyTerm, smartprivilegeBean.getPlanType());
        // double effectiveTopUpPrem=smartprivilegeBean.getEffectiveTopUpPrem();
        // //*Sheet Name -> Input,*Cell -> Y70
        double sumAssured = (annualPremium * SAMF);
        SmartPrivilegeBusinessLogic BIMAST = new SmartPrivilegeBusinessLogic();
        /* Added by Akshaya on 14-Jun-16 start * */
        MinesOccuInterest = commonForAllProd.getStringWithout_E(Math
                .round(BIMAST.getMinesOccuInterest(sumAssured,
                        smartprivilegeBean.getPremiumAmount())));
        /* Added by Akshaya on 14-Jun-16 end * */

        // String[]
        // forBIArray=BIMAST.getForBIArr(prop.mortalityCharges_AsPercentOfofLICtable);
        int rowNumber = 0;
        String[] forBIArray = BIMAST.getForBIArr(prop.mortalityCharges_AsPercentOfofLICtable);
        for (int i = 0; i <= 11; i++) {

            {
                BIMAST.arrFundValAfterFMCBeforeGA[i] = 0;
            }
        }
        int currentElement = 11;
        for (int i = 0; i <= 11; i++) {

            {
                BIMAST.arrFundValAfterFMCBeforeGA_AR[i] = 0;
            }
        }
        int currentElement_AR = 11;
        try {
            for (int i = 0; i < (policyTerm * 12); i++)
            // for(int i=0;i<1;i++)
            {
                rowNumber++;

                BIMAST.setMonth_E(rowNumber);
                month_E = Integer.parseInt(BIMAST.getMonth_E());
                _month_E = month_E;
                // System.out.println("1. month_E "+BIMAST.getMonth_E());
//				System.out.println("1. month_E " + month_E);

                BIMAST.setYear_F();
                year_F = Integer.parseInt(BIMAST.getYear_F());
                _year_F = year_F;
                // System.out.println("2. year_F "+BIMAST.getYear_F());
//				System.out.println("2. year_F " + year_F);

                if ((_month_E % 12) == 0) {
                    bussIll.append("<policyYr").append(_year_F).append(">").append(_year_F).append("</policyYr").append(_year_F).append(">");
                }

				/*if(isKerlaDisc == true && _year_F <=2){
					serviceTax = 0.19;
				} else {
					serviceTax = 0.18;
				}*/
                String s3 = "30/6/2021";
                Date da1 = new SimpleDateFormat("dd/MM/yyyy").parse(s3);
                Date da = new SimpleDateFormat("dd/MM/yyyy").parse(commonMethods.getCurrentDateSlashFormat());
                int mo1 = obj.getMonthDiff(da1, da);
                if (isKerlaDisc == true && month_E <= (mo1 + 1)) {
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
                // _age_H=age_H;
                // System.out.println("4. age_H "+BIMAST.getAge_H());
//				System.out.println("4. age_H " + age_H);

                BIMAST.setPremium_I(PPT, PF, premiumAmt);
                premium_I = Double.parseDouble(BIMAST.getPremium_I());
                // _premium_I=premium_I;
                // System.out.println("5. premium_I "+BIMAST.getPremium_I());
//				System.out.println("5. premium_I " + premium_I);

                sum_I += premium_I;
                if ((_month_E % 12) == 0) {

                    bussIll.append("<AnnPrem" + _year_F + ">" + commonForAllProd.getRound(String
                            .valueOf(sum_I))
                            + "</AnnPrem" + _year_F + ">");
                    _sum_I = sum_I;
                    sum_I = 0;
                }

                // BIMAST.setTopUpPremium_J(prop.topUpStatus,effectiveTopUpPrem);
                // topUpPremium_J=Double.parseDouble(BIMAST.getTopUpPremium_J());
                // // _topUpPremium_J=topUpPremium_J;
                // //System.out.println("6. topUpPremium_J "+BIMAST.getTopUpPremium_J());
                // System.out.println("6. topUpPremium_J "+topUpPremium_J);

                BIMAST.setPremiumAllocationCharge_J(StaffDisc,
                        bancAssuranceDisc, planType, PPT, premiumAmt);
                premiumAllocationCharge_K = Double.parseDouble(BIMAST
                        .getPremiumAllocationCharge_J());
                // _premiumAllocationCharge_K=premiumAllocationCharge_K;
                // System.out.println("7. premiumAllocationCharge_K "+BIMAST.getPremiumAllocationCharge_K());
//				System.out.println("7. premiumAllocationCharge_K "
//						+ premiumAllocationCharge_K);

                sum_K += premiumAllocationCharge_K;
                sum_01 = sum_K;
                if ((_month_E % 12) == 0) {

                    bussIll.append("<PremAllCharge"
                            + _year_F
                            + ">"
                            + commonForAllProd.getRound(String
                            .valueOf(sum_K)) + "</PremAllCharge" + _year_F
                            + ">");
                    sum_K = 0;
                }

                // BIMAST.setTopUpCharges_L(prop.topUp);
                // topUpCharges_L=Double.parseDouble(BIMAST.getTopUpCharges_L());
                // // _topUpCharges_L=topUpCharges_L;
                // //System.out.println("8. topUpCharges_L "+BIMAST.getTopUpCharges_L());
                // System.out.println("8. topUpCharges_L "+topUpCharges_L);

                BIMAST.setServiceTaxOnAllocation_K(prop.allocationCharges,
                        serviceTax);
                ServiceTaxOnAllocation_M = Double.parseDouble(BIMAST
                        .getServiceTaxOnAllocation_K());
                // _ServiceTaxOnAllocation_M=ServiceTaxOnAllocation_M;
                // System.out.println("9. ServiceTaxOnAllocation_M "+BIMAST.getServiceTaxOnAllocation_M());
//				System.out.println("9. ServiceTaxOnAllocation_M "
//						+ ServiceTaxOnAllocation_M);

                BIMAST.setAmountAvailableForInvestment_L();
                amountAvailableForInvestment_N = Double.parseDouble(BIMAST
                        .getAmountAvailableForInvestment_L());
                // _amountAvailableForInvestment_N=amountAvailableForInvestment_N;
                // System.out.println("10. amountAvailableForInvestment_N "+BIMAST.getAmountAvailableForInvestment_N());
//				System.out.println("10. amountAvailableForInvestment_N "
//						+ amountAvailableForInvestment_N);

                sum_N += amountAvailableForInvestment_N;
				/*if ((_month_E % 12) == 0) {

					bussIll.append("<AmtAviFrInv"
							+ _year_F
							+ ">"
							+commonForAllProd.getRound(String
									.valueOf(sum_N)) + "</AmtAviFrInv" + _year_F
							+ ">");
					sum_N = 0;
				}*/


                BIMAST.setAmountAvailableForInvestment_L1();
                amountAvailableForInvestment_N1 = Double.parseDouble(BIMAST
                        .getAmountAvailableForInvestment_L1());
                // _amountAvailableForInvestment_N=amountAvailableForInvestment_N;
                // System.out.println("10. amountAvailableForInvestment_N "+BIMAST.getAmountAvailableForInvestment_N());
//				System.out.println("10. amountAvailableForInvestment_N "
//						+ amountAvailableForInvestment_N);

                sum_N1 += amountAvailableForInvestment_N1;
                if ((_month_E % 12) == 0) {

                    bussIll.append("<AmtAviFrInv" + _year_F + ">"
                            + commonForAllProd.getRound(String
                            .valueOf(sum_N1)) + "</AmtAviFrInv" + _year_F
                            + ">");
                    sum_N1 = 0;
                }

                // BIMAST.setSumAssuredRelatedCharges_O(policyTerm,sumAssured,prop.charge_SumAssuredBase);
                // sumAssuredRelatedCharges_O=Double.parseDouble(BIMAST.getSumAssuredRelatedCharges_O());
                // _sumAssuredRelatedCharges_O=sumAssuredRelatedCharges_O;
                // //System.out.println("11. sumAssuredRelatedCharges_O "+BIMAST.getSumAssuredRelatedCharges_O());
                // //System.out.println("11. sumAssuredRelatedCharges_O "+sumAssuredRelatedCharges_O);
                //
                // //System.out.println("12. RiderCharges_P "+riderCharges_P);
                //
                BIMAST.setPolicyAdministrationCharge_N();
                policyAdministrationCharges_Q = Double.parseDouble(BIMAST
                        .getPolicyAdministrationCharge_N());
                // _policyAdministrationCharges_Q=policyAdministrationCharges_Q;
                // //System.out.println("13. policyAdministrationCharges_Q "+BIMAST.getPolicyAdministrationCharge_Q());
                // //System.out.println("13. policyAdministrationCharges_Q "+policyAdministrationCharges_Q);
                //

                sum_Q += policyAdministrationCharges_Q;
                sum_02 = sum_Q;
                if ((_month_E % 12) == 0) {

                    bussIll.append("<PolAdminChrg"
                            + _year_F
                            + ">"
                            + commonForAllProd.getRound(String
                            .valueOf(sum_Q)) + "</PolAdminChrg" + _year_F
                            + ">");
                    sum_Q = 0;
                }

                BIMAST.setPPWBCharge_O(prop.mortalityAndRiderCharges, PPT, PF,
                        annualPremium);
                ppwbCharges_O = Double.parseDouble(BIMAST.getPPWBCharge_O());
                // _policyAdministrationCharges_Q=policyAdministrationCharges_Q;
                // //System.out.println("13. policyAdministrationCharges_Q "+BIMAST.getPolicyAdministrationCharge_Q());
//				System.out.println("13. ppwbCharges_O " + ppwbCharges_O);
                //

                BIMAST.setRiderBenefitCharges_P();
                riderBenefitCharges_P = Double.parseDouble(BIMAST
                        .getRiderBenefitCharges_P());

                BIMAST.setAdd_AdbNAtpdCharges_Q(prop.mortalityCharges,
                        sumAssured);
                AdbNAtpdCharges_Q = Double.parseDouble(BIMAST
                        .getAdd_AdbNAtpdCharges_Q());

                BIMAST.setOneHundredPercentOfCummulativePremium_AW(oneHundredPercentOfCummulativePremium_AW);
                oneHundredPercentOfCummulativePremium_AW = Double
                        .parseDouble(BIMAST
                                .getOneHundredPercentOfCummulativePremium_AW());
                // _oneHundredPercentOfCummulativePremium_AW=oneHundredPercentOfCummulativePremium_AW;
                // //System.out.println("45. oneHundredPercentOfCummulativePremium_AW "+BIMAST.getOneHundredPercentOfCummulativePremium_AW());
//				System.out
//						.println("45. oneHundredPercentOfCummulativePremium_AW "
//								+ oneHundredPercentOfCummulativePremium_AW);
                //
                BIMAST.setMortalityCharges_R(fundValueAtEnd_AB, policyTerm, forBIArray,
                        sumAssured, prop.mortalityCharges);
                mortalityCharges_R = Double.parseDouble(BIMAST
                        .getMortalityCharges_R());
                // _mortalityCharges_R=mortalityCharges_R;
                // System.out.println("14. mortalityCharges_R "+BIMAST.getMortalityCharges_R());
//				System.out.println("14. mortalityCharges_R "
//						+ mortalityCharges_R);

                sum_R += mortalityCharges_R;
                if ((_month_E % 12) == 0) {

                    bussIll.append("<MortChrg4Pr"
                            + _year_F
                            + ">"
                            + commonForAllProd.getRound(String
                            .valueOf(sum_R)) + "</MortChrg4Pr" + _year_F
                            + ">");
                    sum_R = 0;
                }

                BIMAST.setAccTPDCharges_S(fundValueAtEnd_AB, policyTerm,
                        sumAssured, prop.mortalityCharges);
                AccTPDCharges_S = Double.parseDouble(BIMAST
                        .getAccTPDCharges_S());
                // _totalCharges_S=totalCharges_S;
                // System.out.println("15. totalCharges_S "+BIMAST.getTotalCharges_S());
//				System.out.println("15. AccTPDCharges_S " + AccTPDCharges_S);

                BIMAST.setTotalCharges_T(policyTerm, riderCharges_P);
                totalCharges_S = Double.parseDouble(BIMAST.getTotalCharges_T());
                // _totalCharges_S=totalCharges_S;
                // System.out.println("15. totalCharges_S "+BIMAST.getTotalCharges_S());
//				System.out.println("15. totalCharges_S " + totalCharges_S);

                sum_S += totalCharges_S;
                if ((_month_E % 12) == 0) {

                    bussIll.append("<TotCharg4Pr"
                            + _year_F
                            + ">"
                            + commonForAllProd.getRound(String
                            .valueOf(sum_S)) + "</TotCharg4Pr" + _year_F
                            + ">");
                    sum_S = 0;
                }

                BIMAST.setServiceTax_exclOfSTonAllocAndSurr_U(serviceTax,
                        prop.mortalityAndRiderCharges,
                        prop.administrationCharges);
                serviceTaxExclOfSTOnAllocAndSurr_T = Double.parseDouble(BIMAST
                        .getServiceTax_exclOfSTonAllocAndSurr_U());
                // _serviceTaxExclOfSTOnAllocAndSurr_T=serviceTaxExclOfSTOnAllocAndSurr_T;
                // System.out.println("16. serviceTaxExclOfSTOnAllocAndSurr_T "+BIMAST.getServiceTax_exclOfSTonAllocAndSurr_T());
//				System.out.println("16. serviceTaxExclOfSTOnAllocAndSurr_T "
//						+ serviceTaxExclOfSTOnAllocAndSurr_T);
                //
                BIMAST.setAdditionToFundIfAny_W(fundValueAtEnd_AB, policyTerm,
                        riderCharges_P);
                additionToFundIfAny_V = Double.parseDouble(BIMAST
                        .getAdditionToFundIfAny_W());
                // _additionToFundIfAny_V=additionToFundIfAny_V;
                // System.out.println("18. additionToFundIfAny_V "+BIMAST.getAdditionToFundIfAny_V());
//				System.out.println("18. additionToFundIfAny_V "
//						+ additionToFundIfAny_V);

                sum_AV += additionToFundIfAny_V;
                if ((_month_E % 12) == 0) {

                    bussIll.append("<AdditionsToTheFund4Pr" + _year_F + ">"
                            + commonForAllProd.getRound(String
                            .valueOf(sum_AV)) + "</AdditionsToTheFund4Pr" + _year_F
                            + ">");
                    sum_AV = 0;
                }

                BIMAST.setFundBeforeFMC_X(fundValueAtEnd_AB, policyTerm,
                        riderCharges_P);
                fundBeforeFMC_W = Double.parseDouble(BIMAST
                        .getFundBeforeFMC_X());
                // _fundBeforeFMC_W=fundBeforeFMC_W;
                // System.out.println("19. fundBeforeFMC_W "+BIMAST.getFundBeforeFMC_W());
//				System.out.println("19. fundBeforeFMC_W " + fundBeforeFMC_W);

                BIMAST.setFundManagementCharge_Y(policyTerm,
                        percentToBeInvested_EquityFund,
                        percentToBeInvested_EquityOptFund,
                        percentToBeInvested_GrowthFund,
                        percentToBeInvested_BalancedFund,
                        percentToBeInvested_BondFund,
                        percentToBeInvested_PureFund,
                        percentToBeInvested_Top300Fund,
                        percentToBeInvested_MidCapFund,
                        percentToBeInvested_BondOptimiserFundII,
                        percentToBeInvested_MoneyMarketFundII,
                        percentToBeInvested_CorpBondFund);
                fundManagementCharge_X = Double.parseDouble(BIMAST
                        .getFundManagementCharge_Y());
                // _fundManagementCharge_X=fundManagementCharge_X;
                // System.out.println("20. fundManagementCharge_X "+BIMAST.getFundManagementCharge_X());
//				System.out.println("20. fundManagementCharge_X "
//						+ fundManagementCharge_X);

                sum_X += fundManagementCharge_X;
                sum_03 = sum_X;
                if ((_month_E % 12) == 0) {
                    _sum_X = Math.round(sum_X);
                    bussIll.append("<FundMgmtCharg4Pr" + _year_F + ">" + commonForAllProd.getRound(String
                            .valueOf(_sum_X))
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

                BIMAST.setGuaranteeCharge_Z(policyTerm);
                guaranteeCharge_Z = Double.parseDouble(BIMAST
                        .getGuaranteeCharge_Z());

                BIMAST.setServiceTaxOnFMC_AA(prop.fundManagementCharges,
                        percentToBeInvested_EquityFund,
                        percentToBeInvested_EquityOptFund,
                        percentToBeInvested_GrowthFund,
                        percentToBeInvested_BalancedFund,
                        percentToBeInvested_BondFund,
                        percentToBeInvested_PureFund,
                        percentToBeInvested_Top300Fund,
                        percentToBeInvested_MidCapFund, serviceTax, percentToBeInvested_BondOptimiserFundII,
                        percentToBeInvested_MoneyMarketFundII,
                        percentToBeInvested_CorpBondFund);
                serviceTaxOnFMC_Y = Double.parseDouble(BIMAST
                        .getServiceTaxOnFMC_AA());
                // _serviceTaxOnFMC_Y=serviceTaxOnFMC_Y;
                // System.out.println("21. serviceTaxOnFMC_Y "+BIMAST.getServiceTaxOnFMC_Y());
//				System.out
//						.println("21. serviceTaxOnFMC_Y " + serviceTaxOnFMC_Y);

                BIMAST.setFundValueAfterFMCAndBeforeGA_AB(policyTerm);
                fundValueAfterFMCBerforeGA_Z = Double.parseDouble(BIMAST
                        .getFundValueAfterFMCAndBeforeGA_AB());
                // _fundValueAfterFMCBerforeGA_Z=fundValueAfterFMCBerforeGA_Z;
                // System.out.println("22. fundValueAfterFMCBerforeGA_Z "+BIMAST.getFundValueAfterFMCAndBeforeGA_Z());
//				System.out.println("22. fundValueAfterFMCBerforeGA_Z "
//						+ fundValueAfterFMCBerforeGA_Z);

                BIMAST.arrFundValAfterFMCBeforeGA[currentElement] = fundValueAfterFMCBerforeGA_Z;
                currentElement++;

                BIMAST.setLast2YearAvgFundValue_AB(
                        BIMAST.arrFundValAfterFMCBeforeGA, i);
                last2YearAvgFundValue_AB = Double.parseDouble(BIMAST.getLast2YearAvgFundValue_AB());
//				System.out.println("40.   last2YearAvgFundValue_AB: "+last2YearAvgFundValue_AB);


                BIMAST.setTotalServiceTax_T(riderCharges_P, serviceTax);
                totalServiceTax_U = Double.parseDouble(BIMAST
                        .getTotalServiceTax_T());
                // _totalServiceTax_U=totalServiceTax_U;
                // System.out.println("17. totalServiceTax_U "+BIMAST.getTotalServiceTax_U());
//				System.out
//						.println("17. totalServiceTax_U " + totalServiceTax_U);

                sum_U += totalServiceTax_U;
                if ((_month_E % 12) == 0) {
                    bussIll.append("<TotServTax4Pr"
                            + _year_F
                            + ">"
                            + commonForAllProd.getRound(String
                            .valueOf(sum_U)) + "</TotServTax4Pr" + _year_F
                            + ">");
                    sum_U = 0;
                }

                BIMAST.setGuaranteedAddition_AC(planType, annualPremium, PPT);
                guaranteedAddition_AA = Double.parseDouble(BIMAST
                        .getGuaranteedAddition_AC());
                // _guaranteedAddition_AA=guaranteedAddition_AA;
                // System.out.println("23. guaranteedAddition_AA "+BIMAST.getGuaranteedAddition_AA());
//				System.out.println("23. guaranteedAddition_AA "
//						+ guaranteedAddition_AA);
                arrGuaranteedAddition_AC[i] = guaranteedAddition_AA;

                sum_AA += guaranteedAddition_AA;
                if ((_month_E % 12) == 0) {
                    bussIll.append("<LoyaltyAdd4Pr"
                            + _year_F
                            + ">"
                            + commonForAllProd.getRound(String
                            .valueOf(sum_AA)) + "</LoyaltyAdd4Pr" + _year_F
                            + ">");
                    sum_AA = 0;
                }

                BIMAST.setTerminalAddition_AD(planType, PPT, policyTerm);
                terminalAddition_AA = Double.parseDouble(BIMAST
                        .getTerminalAddition_AD());
//				System.out.println("23. terminalAddition_AA "
//						+ terminalAddition_AA);

                BIMAST.setFundValueAtEnd_AE();
                fundValueAtEnd_AB = Double.parseDouble(BIMAST
                        .getFundValueAtEnd_AE());
                // _fundValueAtEnd_AB=fundValueAtEnd_AB;
                // System.out.println("24. fundValueAtEnd_AB "+BIMAST.getFundValueAtEnd_AB());
//				System.out
//						.println("24. fundValueAtEnd_AB " + fundValueAtEnd_AB);

                if ((_month_E % 12) == 0) {
                    bussIll.append("<FundValAtEnd4Pr"
                            + _year_F
                            + ">"
                            + commonForAllProd.getRoundUp(commonForAllProd
                            .getStringWithout_E(fundValueAtEnd_AB))
                            + "</FundValAtEnd4Pr" + _year_F + ">");

                }

                if ((_month_E % 12) == 0) {
                    // double temp =
                    // Math.round(Double.parseDouble(commonForAllProd.getRoundUp(String.valueOf(_fundValueAtEnd_AC)))
                    // + _sum_Y);
                    // System.out.println(commonForAllProd.getRoundUp(String.valueOf(_fundValueAtEnd_AC))
                    // + " + " + _sum_Y + " = " + temp);
                    bussIll.append("<FundBefFMC4Pr"
                            + _year_F
                            + ">"
                            + Math.round(Double.parseDouble(commonForAllProd
                            .getRound(commonForAllProd.getStringWithout_E(fundValueAtEnd_AB)))
                            + _sum_X) + "</FundBefFMC4Pr" + _year_F
                            + ">");
                }

                BIMAST.setSurrenderCap_AV(annualPremium, planType);
                surrenderCap_AV = Double.parseDouble(BIMAST
                        .getSurrenderCap_AV());
                // // _surrenderCap_AV=surrenderCap_AV;
                // //System.out.println("44. surrenderCap_AV "+BIMAST.getSurrenderCap_AV());
//				System.out.println("44. surrenderCap_AV " + surrenderCap_AV);

                BIMAST.setSurrenderCharges_AF(annualPremium, PPT, planType);
                surrenderCharges_AC = Double.parseDouble(BIMAST
                        .getSurrenderCharges_AF());
                // _surrenderCharges_AC=surrenderCharges_AC;
                // System.out.println("25. surrenderCharges_AC "+BIMAST.getSurrenderCharges_AC());
//				System.out.println("25. surrenderCharges_AC "
//						+ surrenderCharges_AC);

                BIMAST.setServiceTaxOnSurrenderCharges_AG(prop.surrenderCharges, serviceTax);
                serviceTaxOnSurrenderCharges_AD = Double.parseDouble(BIMAST
                        .getServiceTaxOnSurrenderCharges_AG());
                // _serviceTaxOnSurrenderCharges_AD=serviceTaxOnSurrenderCharges_AD;
                // System.out.println("26. serviceTaxOnSurrenderCharges_AD "+BIMAST.getServiceTaxOnSurrenderCharges_AD());
//				System.out.println("26. serviceTaxOnSurrenderCharges_AD "
//						+ serviceTaxOnSurrenderCharges_AD);

                BIMAST.setSurrenderValue_AH();
                surrenderValue_AE = Double.parseDouble(BIMAST
                        .getSurrenderValue_AH());
                // _surrenderValue_AE=surrenderValue_AE;
                // System.out.println("27. surrenderValue_AE "+BIMAST.getSurrenderValue_AE());
//				System.out
//						.println("27. surrenderValue_AE " + surrenderValue_AE);

                if ((_month_E % 12) == 0) {
                    bussIll.append("<SurrVal4Pr"
                            + _year_F
                            + ">"
							/*+ commonForAllProd.getRound(String
									.valueOf(surrenderValue_AE))*/
                            + commonForAllProd.getRound(commonForAllProd.getStringWithout_E(surrenderValue_AE))
                            + "</SurrVal4Pr" + _year_F + ">");

                }

                BIMAST.setDeathBenefit_AI(policyTerm, sumAssured);
                deathBenefit_AF = Double.parseDouble(BIMAST
                        .getDeathBenefit_AI());
                // _deathBenefit_AF=deathBenefit_AF;
                // System.out.println("28. deathBenefit_AF "+BIMAST.getDeathBenefit_AF());
//				System.out.println("28. deathBenefit_AF " + deathBenefit_AF);
                //

                if ((_month_E % 12) == 0) {

                    bussIll.append("<DeathBen4Pr"
                            + _year_F
                            + ">"
							/*+ commonForAllProd.getRound(String
									.valueOf(deathBenefit_AF))*/
                            + commonForAllProd.getRound(commonForAllProd.getStringWithout_E(deathBenefit_AF))
                            + "</DeathBen4Pr" + _year_F + ">");

                }

                BIMAST.setMortalityCharges_AJ(fundValueAtEnd_AQ, policyTerm, forBIArray,
                        sumAssured, prop.mortalityCharges);
                mortalityCharges_AG = Double.parseDouble(BIMAST
                        .getMortalityCharges_AJ());
                // _mortalityCharges_AG=mortalityCharges_AG;
                // System.out.println("29. mortalityCharges_AG "+BIMAST.getMortalityCharges_AG());
//				System.out.println("29. mortalityCharges_AG "
//						+ mortalityCharges_AG);

                sum_AG += mortalityCharges_AG;
                if ((_month_E % 12) == 0) {

                    bussIll.append("<MortChrg8Pr" + _year_F + ">"
                            + Math.round(sum_AG) + "</MortChrg8Pr" + _year_F
                            + ">");
                    sum_AG = 0;
                }

                BIMAST.setAccTPDCharges_AK(fundValueAtEnd_AQ, policyTerm,
                        sumAssured, prop.mortalityCharges);
                AccTPDCharges_S = Double.parseDouble(BIMAST
                        .getAccTPDCharges_AK());
                // _totalCharges_S=totalCharges_S;
                // System.out.println("15. totalCharges_S "+BIMAST.getTotalCharges_S());
//				System.out.println("15. AccTPDCharges_S " + AccTPDCharges_S);

                BIMAST.setTotalCharges_AL(policyTerm, riderCharges_P);
                totalCharges_AH = Double.parseDouble(BIMAST
                        .getTotalCharges_AL());
                // _totalCharges_AH=totalCharges_AH;
                // System.out.println("30. totalCharges_AH "+BIMAST.getTotalCharges_AH());
//				System.out.println("30. totalCharges_AH " + totalCharges_AH);

                sum_AH += totalCharges_AH;
                if ((_month_E % 12) == 0) {
                    bussIll.append("<TotCharg8Pr" + _year_F + ">"
                            + Math.round(sum_AH) + "</TotCharg8Pr" + _year_F
                            + ">");
                    sum_AH = 0;
                }

                BIMAST.setServiceTax_exclOfSTonAllocAndSurr_AM(serviceTax,
                        prop.mortalityAndRiderCharges,
                        prop.administrationCharges);
                serviceTaxExclOfSTOnAllocAndSurr_AI = Double.parseDouble(BIMAST
                        .getServiceTax_exclOfSTonAllocAndSurr_AM());
                // _serviceTaxExclOfSTOnAllocAndSurr_AI=serviceTaxExclOfSTOnAllocAndSurr_AI;
                // System.out.println("31. serviceTaxExclOfSTOnAllocAndSurr_AI "+BIMAST.getServiceTax_exclOfSTonAllocAndSurr_AI());
//				System.out.println("31. serviceTaxExclOfSTOnAllocAndSurr_AI "
//						+ serviceTaxExclOfSTOnAllocAndSurr_AI);

                BIMAST.setAdditionToFundIfAny_AO(fundValueAtEnd_AQ, policyTerm,
                        riderCharges_P);
                additionToFundIfAny_AK = Double.parseDouble(BIMAST
                        .getAdditionToFundIfAny_AO());
                // _additionToFundIfAny_AK=additionToFundIfAny_AK;
                // System.out.println("33. additionToFundIfAny_AK "+BIMAST.getAdditionToFundIfAny_AK());
//				System.out.println("33. additionToFundIfAny_AK "
//						+ additionToFundIfAny_AK);

                sum_AZ += additionToFundIfAny_AK;
                if ((_month_E % 12) == 0) {

                    bussIll.append("<AdditionsToTheFund8Pr" + _year_F + ">"
                            + commonForAllProd.getRound(String
                            .valueOf(sum_AZ)) + "</AdditionsToTheFund8Pr" + _year_F
                            + ">");
                    sum_AZ = 0;
                }

                BIMAST.setFundBeforeFMC_AP(fundValueAtEnd_AQ, policyTerm,
                        riderCharges_P);
                fundBeforeFMC_AL = Double.parseDouble(BIMAST
                        .getFundBeforeFMC_AP());
                // _fundBeforeFMC_AL=fundBeforeFMC_AL;
                // System.out.println("34. fundBeforeFMC_AL "+BIMAST.getFundBeforeFMC_AL());
//				System.out.println("34. fundBeforeFMC_AL " + fundBeforeFMC_AL);

                BIMAST.setFundManagementCharge_AQ(policyTerm,
                        percentToBeInvested_EquityFund,
                        percentToBeInvested_EquityOptFund,
                        percentToBeInvested_GrowthFund,
                        percentToBeInvested_BalancedFund,
                        percentToBeInvested_BondFund,
                        percentToBeInvested_PureFund,
                        percentToBeInvested_Top300Fund,
                        percentToBeInvested_MidCapFund, percentToBeInvested_BondOptimiserFundII,
                        percentToBeInvested_MoneyMarketFundII,
                        percentToBeInvested_CorpBondFund);
                fundManagementCharge_AM = Double.parseDouble(BIMAST
                        .getFundManagementCharge_AQ());
                // _fundManagementCharge_AM=fundManagementCharge_AM;
                // System.out.println("35. fundManagementCharge_AM "+BIMAST.getFundManagementCharge_AM());
//				System.out.println("35. fundManagementCharge_AM "
//						+ fundManagementCharge_AM);

                sum_AM += fundManagementCharge_AM;
                sum1_03 = sum_AM;
                if ((_month_E % 12) == 0) {
                    _sum_AM = Math.round(sum_AM);
                    bussIll.append("<FundMgmtCharg8Pr" + _year_F + ">"
                            + _sum_AM + "</FundMgmtCharg8Pr" + _year_F + ">");
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

                BIMAST.setGuaranteeCharge_AR(policyTerm);
                guaranteeCharge_Z = Double.parseDouble(BIMAST
                        .getGuaranteeCharge_AR());

                BIMAST.setServiceTaxOnFMC_AS(prop.fundManagementCharges,
                        percentToBeInvested_EquityFund,
                        percentToBeInvested_EquityOptFund,
                        percentToBeInvested_GrowthFund,
                        percentToBeInvested_BalancedFund,
                        percentToBeInvested_BondFund,
                        percentToBeInvested_PureFund,
                        percentToBeInvested_Top300Fund,
                        percentToBeInvested_MidCapFund, serviceTax, percentToBeInvested_BondOptimiserFundII,
                        percentToBeInvested_MoneyMarketFundII,
                        percentToBeInvested_CorpBondFund);
                serviceTaxOnFMC_AN = Double.parseDouble(BIMAST
                        .getServiceTaxOnFMC_AS());
                // _serviceTaxOnFMC_AN=serviceTaxOnFMC_AN;
                // System.out.println("36. serviceTaxOnFMC_AN "+BIMAST.getServiceTaxOnFMC_AN());
//				System.out.println("36. serviceTaxOnFMC_AN "
//						+ serviceTaxOnFMC_AN);

                BIMAST.setFundValueAfterFMCAndBeforeGA_AT(policyTerm);
                fundValueAfterFMCBerforeGA_AO = Double.parseDouble(BIMAST
                        .getFundValueAfterFMCAndBeforeGA_AT());
                // _fundValueAfterFMCBerforeGA_AO=fundValueAfterFMCBerforeGA_AO;
                // System.out.println("37. fundValueAfterFMCBerforeGA_AO "+BIMAST.getFundValueAfterFMCAndBeforeGA_AO());
//				System.out.println("37. fundValueAfterFMCBerforeGA_AO "
//						+ fundValueAfterFMCBerforeGA_AO);

                BIMAST.arrFundValAfterFMCBeforeGA_AR[currentElement_AR] = fundValueAfterFMCBerforeGA_AO;
                currentElement_AR++;

                BIMAST.setLast2YearAvgFundValue_AR(
                        BIMAST.arrFundValAfterFMCBeforeGA_AR, i);
                last2YearAvgFundValue_AR = Double.parseDouble(BIMAST.getLast2YearAvgFundValue_AR());
                // _last2YearAvgFundValue_AR=last2YearAvgFundValue_AR;
//				System.out.println("40.   Last2YearAvgFundValue_AR: "+last2YearAvgFundValue_AR);


                BIMAST.setTotalServiceTax_AN(riderCharges_P, serviceTax);
                totalServiceTax_AJ = Double.parseDouble(BIMAST
                        .getTotalServiceTax_AN());
                // _totalServiceTax_AJ=totalServiceTax_AJ;
                // System.out.println("32. totalServiceTax_AJ "+BIMAST.getTotalServiceTax_AJ());
//				System.out.println("32. totalServiceTax_AJ "
//						+ totalServiceTax_AJ);

                sum_AJ += totalServiceTax_AJ;
                if ((_month_E % 12) == 0) {

                    bussIll.append("<TotServTxOnCharg8Pr" + _year_F + ">"
                            + Math.round(sum_AJ) + "</TotServTxOnCharg8Pr"
                            + _year_F + ">");
                    sum_AJ = 0;
                }

                BIMAST.setGuaranteedAddition_AU(planType, annualPremium, PPT);
                guaranteedAddition_AP = Double.parseDouble(BIMAST
                        .getGuaranteedAddition_AU());
                // _guaranteedAddition_AP=guaranteedAddition_AP;
                // System.out.println("38. guaranteedAddition_AP "+BIMAST.getGuaranteedAddition_AP());
//				System.out.println("38. guaranteedAddition_AP "
//						+ guaranteedAddition_AP);
                arrGuaranteedAddition_AU[i] = guaranteedAddition_AP;

                sum_AP += guaranteedAddition_AP;
                if ((_month_E % 12) == 0) {
                    bussIll.append("<LoyaltyAdd8Pr" + _year_F + ">"
                            + Math.round(sum_AP) + "</LoyaltyAdd8Pr" + _year_F
                            + ">");
                    sum_AP = 0;
                }

                BIMAST.setTerminalAddition_AV(planType, PPT, policyTerm);
                terminalAddition_AV = Double.parseDouble(BIMAST
                        .getTerminalAddition_AV());
//				System.out.println("23. terminalAddition_AV "
//						+ terminalAddition_AV);

                BIMAST.setFundValueAtEnd_AW();
                fundValueAtEnd_AQ = Double.parseDouble(BIMAST
                        .getFundValueAtEnd_AW());
                // _fundValueAtEnd_AQ=fundValueAtEnd_AQ;
                // System.out.println("39. fundValueAtEnd_AQ "+BIMAST.getFundValueAtEnd_AQ());
//				System.out
//						.println("39. fundValueAtEnd_AQ " + fundValueAtEnd_AQ);

                if ((_month_E % 12) == 0) {
                    bussIll.append("<FundValAtEnd8Pr"
                            + _year_F
                            + ">"
                            + commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(fundValueAtEnd_AQ))
                            + "</FundValAtEnd8Pr" + _year_F + ">");
                }

                if ((_month_E % 12) == 0) {
                    // double temp =
                    // Math.round(Double.parseDouble(commonForAllProd.getRoundUp(String.valueOf(_fundValueAtEnd_AC)))
                    // + _sum_Y);
                    // System.out.println(commonForAllProd.getRoundUp(String.valueOf(_fundValueAtEnd_AC))
                    // + " + " + _sum_Y + " = " + temp);
                    bussIll.append("<FundBefFMC8Pr"
                            + _year_F
                            + ">"
                            + Math.round(Double.parseDouble(commonForAllProd
                            .getRoundUp(commonForAllProd.getStringWithout_E(fundValueAtEnd_AQ)))
                            + _sum_AM) + "</FundBefFMC8Pr" + _year_F
                            + ">");
                }

                BIMAST.setSurrenderCharges_AX(annualPremium, PPT, planType);
                surrenderCharges_AR = Double.parseDouble(BIMAST
                        .getSurrenderCharges_AX());
                // _surrenderCharges_AR=surrenderCharges_AR;
                // System.out.println("40. surrenderCharges_AR "+BIMAST.getSurrenderCharges_AR());
//				System.out.println("40. surrenderCharges_AR "
//						+ surrenderCharges_AR);

                BIMAST.setServiceTaxOnSurrenderCharges_AY(prop.surrenderCharges, serviceTax);
                ;
                serviceTaxOnSurrenderCharges_AS = Double.parseDouble(BIMAST
                        .getServiceTaxOnSurrenderCharges_AY());
                // _serviceTaxOnSurrenderCharges_AS=serviceTaxOnSurrenderCharges_AS;
                // System.out.println("41. serviceTaxOnSurrenderCharges_AS "+BIMAST.getServiceTaxOnSurrenderCharges_AS());
//				System.out.println("41. serviceTaxOnSurrenderCharges_AS "
//						+ serviceTaxOnSurrenderCharges_AS);

                BIMAST.setSurrenderValue_AZ();
                surrenderValue_AT = Double.parseDouble(BIMAST
                        .getSurrenderValue_AZ());
                // _surrenderValue_AT=surrenderValue_AT;
                // System.out.println("42. surrenderValue_AT "+BIMAST.getSurrenderValue_AT());
//				System.out
//						.println("42. surrenderValue_AT " + surrenderValue_AT);

                if ((_month_E % 12) == 0) {
                    bussIll.append("<SurrVal8Pr"
                            + _year_F
                            + ">"
							/*+ commonForAllProd.getRoundUp(String
									.valueOf(surrenderValue_AT))*/
                            + commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(surrenderValue_AT))
                            + "</SurrVal8Pr" + _year_F + ">");
                }

                BIMAST.setDeathBenefit_BA(policyTerm, sumAssured);
                deathBenefit_AU = Double.parseDouble(BIMAST
                        .getDeathBenefit_BA());
                // _deathBenefit_AU=deathBenefit_AU;
                // System.out.println("43. deathBenefit_AU "+BIMAST.getDeathBenefit_AU());
//				System.out.println("43. deathBenefit_AU " + deathBenefit_AU);
                if ((_month_E % 12) == 0) {

                    bussIll.append("<DeathBen8Pr"
                            + _year_F
                            + ">"
							/*+ commonForAllProd.getRound(String
									.valueOf(deathBenefit_AU))*/
                            + commonForAllProd.getRound(commonForAllProd.getStringWithout_E(deathBenefit_AU))
                            + "</DeathBen8Pr" + _year_F + ">");

                }

                /************ Added by Akshaya ****************************/

                if ((_month_E % 12) == 0) {
                    Commission_BL = BIMAST.getCommission_BL(_sum_I, _sum_J,
                            smartprivilegeBean, premiumPayingTerm);
                    bussIll.append("<CommIfPay8Pr" + _year_F + ">"
                            + Math.round(Commission_BL) + "</CommIfPay8Pr"
                            + _year_F + ">");

                }

            }

        } catch (Exception e) {
            e.printStackTrace();
            // System.out.println("** error **" + e.getMessage());
        }

        String a = commonForAllProd.getStringWithout_E(sumAssured);

        return new String[]{
                (commonForAllProd.getStringWithout_E(sumAssured)),
                commonForAllProd.getRound(commonForAllProd
                        .getStringWithout_E(fundValueAtEnd_AB)),
                commonForAllProd.getRound(commonForAllProd
                        .getStringWithout_E(fundValueAtEnd_AQ)),
                MinesOccuInterest};
        /* Added by Akshaya on 14-Jun-16 end * */
    }

    private void getInput(SmartPrivilegeBean smartPrivilegeBean) {

        inputVal = new StringBuilder();

        String LifeAssured_title = spnr_bi_smart_privilege_life_assured_title
                .getSelectedItem().toString();
        String LifeAssured_firstName = edt_bi_smart_privilege_life_assured_first_name
                .getText().toString();
        String LifeAssured_middleName = edt_bi_smart_privilege_life_assured_middle_name
                .getText().toString();
        String LifeAssured_lastName = edt_bi_smart_privilege_life_assured_last_name
                .getText().toString();
        String LifeAssured_DOB = btn_bi_smart_privilege_life_assured_date
                .getText().toString();
        String LifeAssured_age = edt_bi_smart_privilege_life_assured_age
                .getText().toString();
        String LifeAssured_gender = selGender.getSelectedItem().toString();
        String proposer_title = "";
        String proposer_firstName = "";
        String proposer_middleName = "";
        String proposer_lastName = "";
        String proposer_DOB = "";
        String proposer_age = "";
        String proposer_gender = "";
        if (!spnr_bi_smart_privilege_proposer_title.getSelectedItem()
                .toString().equals("")
                && !spnr_bi_smart_privilege_proposer_title.getSelectedItem()
                .toString().equals("Select Title")) {
            proposer_title = spnr_bi_smart_privilege_proposer_title
                    .getSelectedItem().toString();
            if (proposer_title.equals("Mr."))
                proposer_gender = "Male";
            else
                proposer_gender = "Female";
        }
        if (!edt_bi_smart_privilege_proposer_first_name.getText().toString()
                .equals(""))
            proposer_firstName = edt_bi_smart_privilege_proposer_first_name
                    .getText().toString();
        if (!edt_bi_smart_privilege_proposer_middle_name.getText().toString()
                .equals(""))
            proposer_middleName = edt_bi_smart_privilege_proposer_middle_name
                    .getText().toString();
        if (!edt_bi_smart_privilege_proposer_last_name.getText().toString()
                .equals(""))
            proposer_lastName = edt_bi_smart_privilege_proposer_last_name
                    .getText().toString();
        if (!btn_bi_smart_privilege_proposer_date.getText().toString()
                .equals("Select Date")) {
            Calendar present_date = Calendar.getInstance();
            int tDay = present_date.get(Calendar.DAY_OF_MONTH);
            int tMonth = present_date.get(Calendar.MONTH);
            int tYear = present_date.get(Calendar.YEAR);
            proposer_DOB = btn_bi_smart_privilege_proposer_date.getText()
                    .toString();
            proposer_age = commonForAllProd.calculateMyAge(tYear, tMonth + 1,
                    tDay, getDate1(proposer_DOB)) + "";
        }
        // From GUI Input
        boolean staffDisc = smartPrivilegeBean.getIsForStaffOrNot();
        int policyTerm = smartPrivilegeBean.getPolicyTerm_Basic();

        inputVal.append("<?xml version='1.0' encoding='utf-8' ?><smartPrivilege>");
        inputVal.append("<LifeAssured_title>").append(LifeAssured_title).append("</LifeAssured_title>");
        inputVal.append("<LifeAssured_firstName>").append(LifeAssured_firstName).append("</LifeAssured_firstName>");
        inputVal.append("<LifeAssured_middleName>").append(LifeAssured_middleName).append("</LifeAssured_middleName>");
        inputVal.append("<LifeAssured_lastName>").append(LifeAssured_lastName).append("</LifeAssured_lastName>");
        inputVal.append("<LifeAssured_DOB>").append(LifeAssured_DOB).append("</LifeAssured_DOB>");
        inputVal.append("<LifeAssured_age>").append(LifeAssured_age).append("</LifeAssured_age>");
        inputVal.append("<gender>").append(LifeAssured_gender).append("</gender>");
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

        inputVal.append("<str_kerla_discount>" + str_kerla_discount + "</str_kerla_discount>");
        inputVal.append("<isStaff>").append(staffDisc).append("</isStaff>");
        inputVal.append("<age>").append(ageInYears.getSelectedItem().toString()).append("</age>");
        inputVal.append("<proposer_age>").append(edt_bi_smart_privilege_proposer_age.getText().toString()).append("</proposer_age>");
        double SAMF = smartPrivilegeBean.getSAMF();
        inputVal.append("<SAMF>").append(SAMF).append("</SAMF>");
        // int ageAtEntry = smartPrivilegeBean.getAgeAtEntry();
        plan = smartPrivilegeBean.getPlanType();
        inputVal.append("<plan>").append(plan).append("</plan>");

        inputVal.append("<premFreqMode>").append(selPremiumFrequencyMode.getSelectedItem().toString()).append("</premFreqMode>");

        int premPayingTerm = smartPrivilegeBean.getPremiumPayingTerm();
        inputVal.append("<premPayingTerm>").append(premPayingTerm).append("</premPayingTerm>");
        // int proposerAge = smartPrivilegeBean.getAgeOfProposer();
        inputVal.append("<proposerAge>").append(ageInYears.getSelectedItem().toString()).append("</proposerAge>");
        inputVal.append("<policyTerm>").append(policyTerm).append("</policyTerm>");

        double premiumAmount = smartPrivilegeBean.getPremiumAmount();
        inputVal.append("<premiumAmount>").append(premiumAmount).append("</premiumAmount>");

        inputVal.append("<noOfYrElapsed>").append(noOfYearsElapsedSinceInception.getText().toString()).append("</noOfYrElapsed>");
        inputVal.append("<perInvEquityFund>").append(percent_EquityFund.getText().toString()).append("</perInvEquityFund>");
        inputVal.append("<perInvEquityOptimiserFund>").append(percent_EquityOptFund.getText().toString()).append("</perInvEquityOptimiserFund>");
        inputVal.append("<perInvgrowthFund>").append(percent_GrowthFund.getText().toString()).append("</perInvgrowthFund>");
        inputVal.append("<perInvBalancedFund>").append(percent_BalancedFund.getText().toString()).append("</perInvBalancedFund>");
        inputVal.append("<perInvBondFund>").append(percent_BondFund.getText().toString()).append("</perInvBondFund>");

        inputVal.append("<perInvTop300Fund>").append(percent_Top300Fund.getText().toString()).append("</perInvTop300Fund>");

        inputVal.append("<perInvPureFund>").append(percent_PureFund.getText().toString()).append("</perInvPureFund>");

        inputVal.append("<perInvMidcapFund>").append(percent_MidcapFund.getText().toString()).append("</perInvMidcapFund>");

        inputVal.append("<perInvbondOptimiserFund2>"
                + percent_bondOptimiserFund2.getText().toString()
                + "</perInvbondOptimiserFund2>");

        inputVal.append("<perInvCorporateBondFund>"
                + percent_CorporateBondFund.getText().toString()
                + "</perInvCorporateBondFund>");


        inputVal.append("<perInvmoneyMarketFund2>"
                + percent_moneyMarketFund2.getText().toString()
                + "</perInvmoneyMarketFund2>");

        inputVal.append("<Product_Cat>").append(product_cateogory).append("</Product_Cat>");

        //Added Tushar Kadam Kerla Applicable Tax 7-Jun-2019
        String str_kerla_discount = "N";
        if (cb_kerladisc.isChecked()) {
            str_kerla_discount = "Y";
        }

        inputVal.append("<KFC>" + str_kerla_discount + "</KFC>");
        //End Added Tushar Kadam Kerla Applicable Tax 7-Jun-2019


        inputVal.append("</smartPrivilege>");

    }

    private int calculateMyAge(int year, int month, int day, String date) {
        Calendar nowCal = new GregorianCalendar(year, month, day);

        String[] ProposerDob = date.split("-");
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

    /******************************************************* Start of Reduction in Yield **********************/

    private String[] getOutputReductionInYield(String SheetName,
                                               SmartPrivilegeBean smartprivilegeBean) {// commonForAllProd=new
        // CommonForAllProd();
        SmartPrivilegeProperties prop = new SmartPrivilegeProperties();

        ArrayList<String> List_BF = new ArrayList<>();

        // ouput variable declaration
        int _month_E = 0,
                _year_F = 0,
                _age_H = 0,
                month_BD = 0;

        String _policyInforce_G = "Y";
        // temp variable declaration.
        int month_E = 0, year_F = 0, age_H = 0;

        String policyInforce_G = "Y";

        double premium_I = 0,
                topUpPremium_J = 0,
                premiumAllocationCharge_K = 0,
                topUpCharges_L = 0,
                ServiceTaxOnAllocation_M = 0,
                amountAvailableForInvestment_N = 0,
                sumAssuredRelatedCharges_O = 0,
                riderCharges_P = 0,
                policyAdministrationCharges_Q = 0,
                mortalityCharges_R = 0,
                totalCharges_S = 0,
                serviceTaxExclOfSTOnAllocAndSurr_T = 0,
                totalServiceTax_U = 0,
                AccTPDCharges_S = 0,
                ppwbCharges_O = 0,
                riderBenefitCharges_P = 0,
                AdbNAtpdCharges_Q = 0,
                additionToFundIfAny_V = 0,
                fundBeforeFMC_W = 0,
                terminalAddition_AA = 0,
                guaranteeCharge_Z = 0,
                fundManagementCharge_X = 0,
                serviceTaxOnFMC_Y = 0,
                fundValueAfterFMCBerforeGA_Z = 0,
                guaranteedAddition_AA = 0,
                fundValueAtEnd_AB = 0,
                surrenderCharges_AC = 0,
                serviceTaxOnSurrenderCharges_AD = 0,
                surrenderValue_AE = 0,
                deathBenefit_AF = 0,
                mortalityCharges_AG = 0,
                totalCharges_AH = 0,
                serviceTaxExclOfSTOnAllocAndSurr_AI = 0,
                totalServiceTax_AJ = 0,
                additionToFundIfAny_AK = 0,
                fundBeforeFMC_AL = 0,
                fundManagementCharge_AM = 0,
                serviceTaxOnFMC_AN = 0,
                fundValueAfterFMCBerforeGA_AO = 0,
                guaranteedAddition_AP = 0,
                fundValueAtEnd_AQ = 0,
                surrenderCharges_AR = 0,
                serviceTaxOnSurrenderCharges_AS = 0,
                surrenderValue_AT = 0,
                deathBenefit_AU = 0,
                surrenderCap_AV = 0,
                oneHundredPercentOfCummulativePremium_AW = 0,
                terminalAddition_AV = 0, reductionYield_BF = 0,
                irrAnnual_BF = 0,
                reductionYield = 0,
                netYield = 0;


        double last2YearAvgFundValue_AB = 0;
        double last2YearAvgFundValue_AR = 0;
        // from GUI inputs
        boolean StaffDisc = smartprivilegeBean.getIsForStaffOrNot();
        boolean bancAssuranceDisc = false;
        int ageAtEntry = smartprivilegeBean.getAgeAtEntry();
        int policyTerm = smartprivilegeBean.getPolicyTerm_Basic();
        String planType = smartprivilegeBean.getPlanType();
        //double serviceTax=smartprivilegeBean.getServiceTax();
        double serviceTax = 0;
        boolean isKerlaDisc = smartprivilegeBean.isKerlaDisc();
        String premFreq = smartprivilegeBean.getPremFreq();
        int premiumPayingTerm = smartprivilegeBean.getPremiumPayingTerm();
        double premiumAmt = smartprivilegeBean.getPremiumAmount();
        double SAMF = smartprivilegeBean.getSAMF();
        double percentToBeInvested_EquityFund = smartprivilegeBean
                .getPercentToBeInvested_EquityFund();
        double percentToBeInvested_EquityOptFund = smartprivilegeBean
                .getPercentToBeInvested_EquityOptFund();
        double percentToBeInvested_GrowthFund = smartprivilegeBean
                .getPercentToBeInvested_GrowthFund();
        double percentToBeInvested_BalancedFund = smartprivilegeBean
                .getPercentToBeInvested_BalancedFund();
        double percentToBeInvested_BondFund = smartprivilegeBean
                .getPercentToBeInvested_BondFund();
        double percentToBeInvested_PureFund = smartprivilegeBean
                .getPercentToBeInvested_PureFund();
        double percentToBeInvested_MidCapFund = smartprivilegeBean
                .getPercentToBeInvested_MidcapFund();
        double percentToBeInvested_Top300Fund = smartprivilegeBean.getPercentToBeInvested_Top300Fund();
        double percentToBeInvested_BondOptimiserFundII = smartprivilegeBean.getPercentToBeInvested_bondOptimiserFund2();
        double percentToBeInvested_MoneyMarketFundII = smartprivilegeBean.getPercentToBeInvested_moneyMarketFund2();
        double percentToBeInvested_CorpBondFund = smartprivilegeBean.getPercentToBeInvested_CorpBondFund();
        String transferFundStatus = "No Transfer";
        String addTopUp = "No";

        double annualPremium = getAnnualPremium(premiumAmt, smartprivilegeBean.getPremFreq());
        int PF = getPremFreqMode(smartprivilegeBean.getPremFreq());
        int PPT = calPPT(premiumPayingTerm, policyTerm, smartprivilegeBean.getPlanType());
        //		        double effectiveTopUpPrem=smartprivilegeBean.getEffectiveTopUpPrem();    //*Sheet Name -> Input,*Cell -> Y70
        double sumAssured = (annualPremium * SAMF);

        SmartPrivilegeBusinessLogic BIMAST = new SmartPrivilegeBusinessLogic();
        // String[]
        // forBIArray=BIMAST.getForBIArr(prop.mortalityCharges_AsPercentOfofLICtable);
        int rowNumber = 0, monthNumber = 0;

        String[] forBIArray = BIMAST.getForBIArr(prop.mortalityCharges_AsPercentOfofLICtable);

        for (int i = 0; i <= 11; i++) {

            {
                BIMAST.arrFundValAfterFMCBeforeGA[i] = 0;
            }
        }
        int currentElement = 12;
        for (int i = 0; i <= 11; i++) {

            {
                BIMAST.arrFundValAfterFMCBeforeGA_AR[i] = 0;
            }
        }
        int currentElement_AR = 12;

        try {
            for (int i = 0; i <= (policyTerm * 12); i++)
            // for(int i=0;i<1;i++)
            {
                rowNumber++;

                BIMAST.setMonth_E(rowNumber);
                month_E = Integer.parseInt(BIMAST.getMonth_E());
                // _month_E=month_E;
                // System.out.println("1. month_E "+BIMAST.getMonth_E());
                // System.out.println("1. month_E "+month_E);

                BIMAST.setYear_F();
                year_F = Integer.parseInt(BIMAST.getYear_F());
                // _year_F=year_F;
                // System.out.println("2. year_F "+BIMAST.getYear_F());
                // System.out.println("2. year_F "+year_F);

				/*if(isKerlaDisc == true && year_F <=2){
					serviceTax = 0.19;
				} else {
					serviceTax = 0.18;
				}*/


                String s3 = "30/6/2021";
                Date da1 = new SimpleDateFormat("dd/MM/yyyy").parse(s3);
                Date da = new SimpleDateFormat("dd/MM/yyyy").parse(commonMethods.getCurrentDateSlashFormat());
                int mo1 = obj.getMonthDiff(da1, da);
                if (isKerlaDisc == true && month_E <= (mo1 + 1)) {
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
                // _age_H=age_H;
                // System.out.println("4. age_H "+BIMAST.getAge_H());
                // System.out.println("4. age_H "+age_H);

                BIMAST.setPremium_I(PPT, PF, premiumAmt);
                premium_I = Double.parseDouble(BIMAST.getPremium_I());
                // _premium_I=premium_I;
                // System.out.println("5. premium_I "+BIMAST.getPremium_I());
                // System.out.println("5. premium_I "+premium_I);

                // BIMAST.setTopUpPremium_J(prop.topUpStatus,effectiveTopUpPrem);
                // topUpPremium_J=Double.parseDouble(BIMAST.getTopUpPremium_J());
                // // _topUpPremium_J=topUpPremium_J;
                // //System.out.println("6. topUpPremium_J "+BIMAST.getTopUpPremium_J());
                // System.out.println("6. topUpPremium_J "+topUpPremium_J);

                BIMAST.setPremiumAllocationCharge_J(StaffDisc,
                        bancAssuranceDisc, planType, PPT, premiumAmt);
                premiumAllocationCharge_K = Double.parseDouble(BIMAST.getPremiumAllocationCharge_J());
                // _premiumAllocationCharge_K=premiumAllocationCharge_K;
                // System.out.println("7. premiumAllocationCharge_K "+BIMAST.getPremiumAllocationCharge_K());
                // System.out.println("7. premiumAllocationCharge_K "+premiumAllocationCharge_K);

                // BIMAST.setTopUpCharges_L(prop.topUp);
                // topUpCharges_L=Double.parseDouble(BIMAST.getTopUpCharges_L());
                // // _topUpCharges_L=topUpCharges_L;
                // //System.out.println("8. topUpCharges_L "+BIMAST.getTopUpCharges_L());
                // System.out.println("8. topUpCharges_L "+topUpCharges_L);

                BIMAST.setServiceTaxOnAllocation_K(prop.allocationChargesYield,
                        serviceTax);
                ServiceTaxOnAllocation_M = Double.parseDouble(BIMAST.getServiceTaxOnAllocation_K());
                // _ServiceTaxOnAllocation_M=ServiceTaxOnAllocation_M;
                // System.out.println("9. ServiceTaxOnAllocation_M "+BIMAST.getServiceTaxOnAllocation_M());
                // System.out.println("9. ServiceTaxOnAllocation_M "+ServiceTaxOnAllocation_M);

                BIMAST.setAmountAvailableForInvestment_L();
                amountAvailableForInvestment_N = Double.parseDouble(BIMAST.getAmountAvailableForInvestment_L());
                // _amountAvailableForInvestment_N=amountAvailableForInvestment_N;
                // //System.out.println("10. amountAvailableForInvestment_N "+BIMAST.getAmountAvailableForInvestment_N());
                // System.out.println("10. amountAvailableForInvestment_N "+amountAvailableForInvestment_N);

                // BIMAST.setSumAssuredRelatedCharges_O(policyTerm,sumAssured,prop.charge_SumAssuredBase);
                // sumAssuredRelatedCharges_O=Double.parseDouble(BIMAST.getSumAssuredRelatedCharges_O());
                // _sumAssuredRelatedCharges_O=sumAssuredRelatedCharges_O;
                // //System.out.println("11. sumAssuredRelatedCharges_O "+BIMAST.getSumAssuredRelatedCharges_O());
                // //System.out.println("11. sumAssuredRelatedCharges_O "+sumAssuredRelatedCharges_O);
                //
                // //System.out.println("12. RiderCharges_P "+riderCharges_P);
                //
                BIMAST.setPolicyAdministrationCharge_N();
                policyAdministrationCharges_Q = Double.parseDouble(BIMAST.getPolicyAdministrationCharge_N());
                // _policyAdministrationCharges_Q=policyAdministrationCharges_Q;
                // //System.out.println("13. policyAdministrationCharges_Q "+BIMAST.getPolicyAdministrationCharge_Q());
                // //System.out.println("13. policyAdministrationCharges_Q "+policyAdministrationCharges_Q);
                //
                BIMAST.setPPWBCharge_O(prop.mortalityAndRiderChargesYield, PPT,
                        PF, annualPremium);
                ppwbCharges_O = Double.parseDouble(BIMAST.getPPWBCharge_O());
                // _policyAdministrationCharges_Q=policyAdministrationCharges_Q;
                // //System.out.println("13. policyAdministrationCharges_Q "+BIMAST.getPolicyAdministrationCharge_Q());
                // System.out.println("13. policyAdministrationCharges_Q "+ppwbCharges_O);
                //

                BIMAST.setRiderBenefitCharges_P();
                riderBenefitCharges_P = Double.parseDouble(BIMAST.getRiderBenefitCharges_P());

                BIMAST.setAdd_AdbNAtpdCharges_Q(prop.mortalityChargesYield,
                        sumAssured);
                AdbNAtpdCharges_Q = Double.parseDouble(BIMAST.getAdd_AdbNAtpdCharges_Q());

                BIMAST.setOneHundredPercentOfCummulativePremium_AW(oneHundredPercentOfCummulativePremium_AW);
                oneHundredPercentOfCummulativePremium_AW = Double
                        .parseDouble(BIMAST
                                .getOneHundredPercentOfCummulativePremium_AW());
                // _oneHundredPercentOfCummulativePremium_AW=oneHundredPercentOfCummulativePremium_AW;
                // //System.out.println("45. oneHundredPercentOfCummulativePremium_AW "+BIMAST.getOneHundredPercentOfCummulativePremium_AW());
                // System.out.println("45. oneHundredPercentOfCummulativePremium_AW "+oneHundredPercentOfCummulativePremium_AW);
                //
                BIMAST.setMortalityCharges_R(fundValueAtEnd_AB, policyTerm, forBIArray, sumAssured, prop.mortalityChargesYield);
                mortalityCharges_R = Double.parseDouble(BIMAST.getMortalityCharges_R());
                // _mortalityCharges_R=mortalityCharges_R;
                // System.out.println("14. mortalityCharges_R "+BIMAST.getMortalityCharges_R());
                // System.out.println("14. mortalityCharges_R "+mortalityCharges_R);

                BIMAST.setAccTPDCharges_S(fundValueAtEnd_AB, policyTerm,
                        sumAssured, prop.mortalityChargesYield);
                AccTPDCharges_S = Double.parseDouble(BIMAST.getAccTPDCharges_S());
                // _totalCharges_S=totalCharges_S;
                // //System.out.println("15. totalCharges_S "+BIMAST.getTotalCharges_S());
                // System.out.println("15. AccTPDCharges_S "+AccTPDCharges_S);

                BIMAST.setTotalCharges_T(policyTerm, riderCharges_P);
                totalCharges_S = Double.parseDouble(BIMAST.getTotalCharges_T());
                // _totalCharges_S=totalCharges_S;
                // System.out.println("15. totalCharges_S "+BIMAST.getTotalCharges_S());
                // System.out.println("15. totalCharges_S "+totalCharges_S);

                BIMAST.setServiceTax_exclOfSTonAllocAndSurr_U(serviceTax,
                        prop.mortalityAndRiderChargesYield,
                        prop.administrationChargesYield);
                serviceTaxExclOfSTOnAllocAndSurr_T = Double.parseDouble(BIMAST.getServiceTax_exclOfSTonAllocAndSurr_U());
                // _serviceTaxExclOfSTOnAllocAndSurr_T=serviceTaxExclOfSTOnAllocAndSurr_T;
                // System.out.println("16. serviceTaxExclOfSTOnAllocAndSurr_T "+BIMAST.getServiceTax_exclOfSTonAllocAndSurr_T());
                // System.out.println("16. serviceTaxExclOfSTOnAllocAndSurr_T "+serviceTaxExclOfSTOnAllocAndSurr_T);
                //
                BIMAST.setAdditionToFundIfAny_W(fundValueAtEnd_AB, policyTerm,
                        riderCharges_P);
                additionToFundIfAny_V = Double.parseDouble(BIMAST.getAdditionToFundIfAny_W());
                // _additionToFundIfAny_V=additionToFundIfAny_V;
                // System.out.println("18. additionToFundIfAny_V "+BIMAST.getAdditionToFundIfAny_V());
                // System.out.println("18. additionToFundIfAny_V "+additionToFundIfAny_V);

                BIMAST.setFundBeforeFMC_X(fundValueAtEnd_AB, policyTerm,
                        riderCharges_P);
                fundBeforeFMC_W = Double.parseDouble(BIMAST.getFundBeforeFMC_X());
                // _fundBeforeFMC_W=fundBeforeFMC_W;
                // System.out.println("19. fundBeforeFMC_W "+BIMAST.getFundBeforeFMC_W());
                // System.out.println("19. fundBeforeFMC_W "+fundBeforeFMC_W);

                BIMAST.setFundManagementCharge_Y(policyTerm, percentToBeInvested_EquityFund, percentToBeInvested_EquityOptFund, percentToBeInvested_GrowthFund, percentToBeInvested_BalancedFund, percentToBeInvested_BondFund, percentToBeInvested_PureFund, percentToBeInvested_Top300Fund, percentToBeInvested_MidCapFund, percentToBeInvested_BondOptimiserFundII,
                        percentToBeInvested_MoneyMarketFundII, percentToBeInvested_CorpBondFund);
                fundManagementCharge_X = Double.parseDouble(BIMAST.getFundManagementCharge_Y());
                // _fundManagementCharge_X=fundManagementCharge_X;
                // System.out.println("20. fundManagementCharge_X "+BIMAST.getFundManagementCharge_X());
                // System.out.println("20. fundManagementCharge_X "+fundManagementCharge_X);

                BIMAST.setGuaranteeCharge_Z(policyTerm);
                guaranteeCharge_Z = Double.parseDouble(BIMAST.getGuaranteeCharge_Z());

                BIMAST.setServiceTaxOnFMC_AA(prop.fundManagementChargesYield, percentToBeInvested_EquityFund, percentToBeInvested_EquityOptFund, percentToBeInvested_GrowthFund, percentToBeInvested_BalancedFund, percentToBeInvested_BondFund, percentToBeInvested_PureFund, percentToBeInvested_Top300Fund, percentToBeInvested_MidCapFund, serviceTax, percentToBeInvested_BondOptimiserFundII,
                        percentToBeInvested_MoneyMarketFundII, percentToBeInvested_CorpBondFund);
                serviceTaxOnFMC_Y = Double.parseDouble(BIMAST.getServiceTaxOnFMC_AA());
                // _serviceTaxOnFMC_Y=serviceTaxOnFMC_Y;
                // System.out.println("21. serviceTaxOnFMC_Y "+BIMAST.getServiceTaxOnFMC_Y());
                // System.out.println("21. serviceTaxOnFMC_Y "+serviceTaxOnFMC_Y);

                BIMAST.setFundValueAfterFMCAndBeforeGA_AB(policyTerm);
                fundValueAfterFMCBerforeGA_Z = Double.parseDouble(BIMAST
                        .getFundValueAfterFMCAndBeforeGA_AB());
                // _fundValueAfterFMCBerforeGA_Z=fundValueAfterFMCBerforeGA_Z;
                // System.out.println("22. fundValueAfterFMCBerforeGA_Z "+BIMAST.getFundValueAfterFMCAndBeforeGA_Z());
                // System.out.println("22. fundValueAfterFMCBerforeGA_Z "+fundValueAfterFMCBerforeGA_Z);

                BIMAST.arrFundValAfterFMCBeforeGA[currentElement] = fundValueAfterFMCBerforeGA_Z;
                currentElement++;

                BIMAST.setLast2YearAvgFundValue_AB(
                        BIMAST.arrFundValAfterFMCBeforeGA, i);
                last2YearAvgFundValue_AB = Double.parseDouble(BIMAST.getLast2YearAvgFundValue_AB());
                // System.out.println("40.   last2YearAvgFundValue_AB: "+last2YearAvgFundValue_AB);

                BIMAST.setTotalServiceTax_T(riderCharges_P, serviceTax);
                totalServiceTax_U = Double.parseDouble(BIMAST.getTotalServiceTax_T());
                // _totalServiceTax_U=totalServiceTax_U;
                // System.out.println("17. totalServiceTax_U "+BIMAST.getTotalServiceTax_U());
                // System.out.println("17. totalServiceTax_U "+totalServiceTax_U);

                BIMAST.setGuaranteedAdditionYield_AC(arrGuaranteedAddition_AC[i]);
                guaranteedAddition_AA = Double.parseDouble(BIMAST.getGuaranteedAdditionYield_AC());
                // _guaranteedAddition_AA=guaranteedAddition_AA;
                // System.out.println("23. guaranteedAddition_AA "+BIMAST.getGuaranteedAddition_AA());
                // System.out.println("23. guaranteedAddition_AA "+guaranteedAddition_AA);

                BIMAST.setTerminalAddition_AD(planType, PPT, policyTerm);
                terminalAddition_AA = Double.parseDouble(BIMAST.getTerminalAddition_AD());
                // System.out.println("23. terminalAddition_AA "+terminalAddition_AA);

                BIMAST.setFundValueAtEnd_AE();
                ;
                fundValueAtEnd_AB = Double.parseDouble(BIMAST
                        .getFundValueAtEnd_AE());
                // _fundValueAtEnd_AB=fundValueAtEnd_AB;
                // System.out.println("24. fundValueAtEnd_AB "+BIMAST.getFundValueAtEnd_AB());
                // System.out.println("24. fundValueAtEnd_AB "+fundValueAtEnd_AB);
                //
                BIMAST.setSurrenderCap_AV(annualPremium, planType);
                surrenderCap_AV = Double.parseDouble(BIMAST.getSurrenderCap_AV());
                // // _surrenderCap_AV=surrenderCap_AV;
                // //System.out.println("44. surrenderCap_AV "+BIMAST.getSurrenderCap_AV());
                // System.out.println("44. surrenderCap_AV "+surrenderCap_AV);

                BIMAST.setSurrenderCharges_AF(annualPremium, PPT, planType);
                surrenderCharges_AC = Double.parseDouble(BIMAST.getSurrenderCharges_AF());
                // _surrenderCharges_AC=surrenderCharges_AC;
                // System.out.println("25. surrenderCharges_AC "+BIMAST.getSurrenderCharges_AC());
                // System.out.println("25. surrenderCharges_AC "+surrenderCharges_AC);

                BIMAST.setServiceTaxOnSurrenderCharges_AG(prop.surrenderChargesYield, serviceTax);
                serviceTaxOnSurrenderCharges_AD = Double.parseDouble(BIMAST.getServiceTaxOnSurrenderCharges_AG());
                // _serviceTaxOnSurrenderCharges_AD=serviceTaxOnSurrenderCharges_AD;
                // System.out.println("26. serviceTaxOnSurrenderCharges_AD "+BIMAST.getServiceTaxOnSurrenderCharges_AD());
                // System.out.println("26. serviceTaxOnSurrenderCharges_AD "+serviceTaxOnSurrenderCharges_AD);

                BIMAST.setSurrenderValue_AH();
                surrenderValue_AE = Double.parseDouble(BIMAST.getSurrenderValue_AH());
                // _surrenderValue_AE=surrenderValue_AE;
                // System.out.println("27. surrenderValue_AE "+BIMAST.getSurrenderValue_AE());
                // System.out.println("27. surrenderValue_AE "+surrenderValue_AE);

                BIMAST.setDeathBenefit_AI(policyTerm, sumAssured);
                deathBenefit_AF = Double.parseDouble(BIMAST.getDeathBenefit_AI());
                // _deathBenefit_AF=deathBenefit_AF;
                // System.out.println("28. deathBenefit_AF "+BIMAST.getDeathBenefit_AF());
                // System.out.println("28. deathBenefit_AF "+deathBenefit_AF);
                //
                BIMAST.setMortalityCharges_AJ(fundValueAtEnd_AQ, policyTerm, forBIArray, sumAssured, prop.mortalityChargesYield);
                mortalityCharges_AG = Double.parseDouble(BIMAST.getMortalityCharges_AJ());
                // _mortalityCharges_AG=mortalityCharges_AG;
                // System.out.println("29. mortalityCharges_AG "+BIMAST.getMortalityCharges_AG());
                // System.out.println("29. mortalityCharges_AG "+mortalityCharges_AG);

                BIMAST.setAccTPDCharges_AK(fundValueAtEnd_AQ, policyTerm,
                        sumAssured, prop.mortalityChargesYield);
                AccTPDCharges_S = Double.parseDouble(BIMAST.getAccTPDCharges_AK());
                // _totalCharges_S=totalCharges_S;
                // System.out.println("15. totalCharges_S "+BIMAST.getTotalCharges_S());
                // System.out.println("15. AccTPDCharges_S "+AccTPDCharges_S);

                BIMAST.setTotalCharges_AL(policyTerm, riderCharges_P);
                totalCharges_AH = Double.parseDouble(BIMAST.getTotalCharges_AL());
                // _totalCharges_AH=totalCharges_AH;
                // System.out.println("30. totalCharges_AH "+BIMAST.getTotalCharges_AH());
                // System.out.println("30. totalCharges_AH "+totalCharges_AH);

                BIMAST.setServiceTax_exclOfSTonAllocAndSurr_AM(serviceTax,
                        prop.mortalityAndRiderChargesYield,
                        prop.administrationChargesYield);
                serviceTaxExclOfSTOnAllocAndSurr_AI = Double.parseDouble(BIMAST.getServiceTax_exclOfSTonAllocAndSurr_AM());
                // _serviceTaxExclOfSTOnAllocAndSurr_AI=serviceTaxExclOfSTOnAllocAndSurr_AI;
                // System.out.println("31. serviceTaxExclOfSTOnAllocAndSurr_AI "+BIMAST.getServiceTax_exclOfSTonAllocAndSurr_AI());
                // System.out.println("31. serviceTaxExclOfSTOnAllocAndSurr_AI "+serviceTaxExclOfSTOnAllocAndSurr_AI);

                BIMAST.setAdditionToFundIfAny_AO(fundValueAtEnd_AQ, policyTerm,
                        riderCharges_P);
                additionToFundIfAny_AK = Double.parseDouble(BIMAST.getAdditionToFundIfAny_AO());
                // _additionToFundIfAny_AK=additionToFundIfAny_AK;
                // System.out.println("33. additionToFundIfAny_AK "+BIMAST.getAdditionToFundIfAny_AK());
                // System.out.println("33. additionToFundIfAny_AK "+additionToFundIfAny_AK);

                BIMAST.setFundBeforeFMC_AP(fundValueAtEnd_AQ, policyTerm,
                        riderCharges_P);
                fundBeforeFMC_AL = Double.parseDouble(BIMAST.getFundBeforeFMC_AP());
                // _fundBeforeFMC_AL=fundBeforeFMC_AL;
                // System.out.println("34. fundBeforeFMC_AL "+BIMAST.getFundBeforeFMC_AL());
                // System.out.println("34. fundBeforeFMC_AL "+fundBeforeFMC_AL);

                BIMAST.setFundManagementCharge_AQ(policyTerm, percentToBeInvested_EquityFund, percentToBeInvested_EquityOptFund, percentToBeInvested_GrowthFund, percentToBeInvested_BalancedFund, percentToBeInvested_BondFund, percentToBeInvested_PureFund, percentToBeInvested_Top300Fund, percentToBeInvested_MidCapFund, percentToBeInvested_BondOptimiserFundII,
                        percentToBeInvested_MoneyMarketFundII, percentToBeInvested_CorpBondFund);
                fundManagementCharge_AM = Double.parseDouble(BIMAST.getFundManagementCharge_AQ());
                // _fundManagementCharge_AM=fundManagementCharge_AM;
                // System.out.println("35. fundManagementCharge_AM "+BIMAST.getFundManagementCharge_AM());
                // System.out.println("35. fundManagementCharge_AM "+fundManagementCharge_AM);

                BIMAST.setGuaranteeCharge_AR(policyTerm);
                guaranteeCharge_Z = Double.parseDouble(BIMAST.getGuaranteeCharge_AR());


                BIMAST.setServiceTaxOnFMC_AS(prop.fundManagementChargesYield, percentToBeInvested_EquityFund, percentToBeInvested_EquityOptFund, percentToBeInvested_GrowthFund, percentToBeInvested_BalancedFund, percentToBeInvested_BondFund, percentToBeInvested_PureFund, percentToBeInvested_Top300Fund, percentToBeInvested_MidCapFund, serviceTax, percentToBeInvested_BondOptimiserFundII,
                        percentToBeInvested_MoneyMarketFundII, percentToBeInvested_CorpBondFund);
                serviceTaxOnFMC_AN = Double.parseDouble(BIMAST.getServiceTaxOnFMC_AS());
                // _serviceTaxOnFMC_AN=serviceTaxOnFMC_AN;
                // System.out.println("36. serviceTaxOnFMC_AN "+BIMAST.getServiceTaxOnFMC_AN());
                // System.out.println("36. serviceTaxOnFMC_AN "+serviceTaxOnFMC_AN);

                BIMAST.setFundValueAfterFMCAndBeforeGA_AT(policyTerm);
                fundValueAfterFMCBerforeGA_AO = Double.parseDouble(BIMAST
                        .getFundValueAfterFMCAndBeforeGA_AT());
                // _fundValueAfterFMCBerforeGA_AO=fundValueAfterFMCBerforeGA_AO;
                // System.out.println("37. fundValueAfterFMCBerforeGA_AO "+BIMAST.getFundValueAfterFMCAndBeforeGA_AO());
//				System.out.println("37. fundValueAfterFMCBerforeGA_AO "+fundValueAfterFMCBerforeGA_AO);
                BIMAST.arrFundValAfterFMCBeforeGA_AR[currentElement_AR] = fundValueAfterFMCBerforeGA_AO;
                currentElement_AR++;

                BIMAST.setLast2YearAvgFundValue_AR(
                        BIMAST.arrFundValAfterFMCBeforeGA_AR, i);
                last2YearAvgFundValue_AR = Double.parseDouble(BIMAST.getLast2YearAvgFundValue_AR());
                // _last2YearAvgFundValue_AR=last2YearAvgFundValue_AR;
                // System.out.println("40.   Last2YearAvgFundValue_AR: "+last2YearAvgFundValue_AR);

                BIMAST.setTotalServiceTax_AN(riderCharges_P, serviceTax);
                totalServiceTax_AJ = Double.parseDouble(BIMAST.getTotalServiceTax_AN());
                // _totalServiceTax_AJ=totalServiceTax_AJ;
                // System.out.println("32. totalServiceTax_AJ "+BIMAST.getTotalServiceTax_AJ());
                // System.out.println("32. totalServiceTax_AJ "+totalServiceTax_AJ);

                BIMAST.setGuaranteedAddition_AU(planType, annualPremium, PPT);
                guaranteedAddition_AP = Double.parseDouble(BIMAST.getGuaranteedAddition_AU());
                // _guaranteedAddition_AP=guaranteedAddition_AP;
                // System.out.println("38. guaranteedAddition_AP "+BIMAST.getGuaranteedAddition_AP());
                // System.out.println("38. guaranteedAddition_AP "+guaranteedAddition_AP);

                BIMAST.setTerminalAddition_AV(planType, PPT, policyTerm);
                terminalAddition_AV = Double.parseDouble(BIMAST.getTerminalAddition_AV());
                // System.out.println("23. terminalAddition_AV "+terminalAddition_AV);

                BIMAST.setMonth_BD(monthNumber);
                month_BD = Integer.parseInt(BIMAST.getMonth_BD());
                // _month_BA=month_BA;
                // System.out.println("month_bo "+month_BA);

                BIMAST.setReductionYield_BF(policyTerm, fundValueAtEnd_AQ);
                reductionYield_BF = Double.parseDouble(BIMAST
                        .getReductionYield_BF());
                // _reductionYield_BB=reductionYield_BB;
                // System.out.println("reductionYield_BB "+reductionYield_BB);

                BIMAST.setFundValueAtEnd_AW();
                fundValueAtEnd_AQ = Double.parseDouble(BIMAST
                        .getFundValueAtEnd_AW());
                // _fundValueAtEnd_AQ=fundValueAtEnd_AQ;
                // System.out.println("39. fundValueAtEnd_AQ "+BIMAST.getFundValueAtEnd_AQ());
                // System.out.println("39. fundValueAtEnd_AQ "+fundValueAtEnd_AQ);

                BIMAST.setSurrenderCharges_AX(annualPremium, PPT, planType);
                surrenderCharges_AR = Double.parseDouble(BIMAST.getSurrenderCharges_AX());
                // _surrenderCharges_AR=surrenderCharges_AR;
                // System.out.println("40. surrenderCharges_AR "+BIMAST.getSurrenderCharges_AR());
                // System.out.println("40. surrenderCharges_AR "+surrenderCharges_AR);

                BIMAST.setServiceTaxOnSurrenderCharges_AY(prop.surrenderChargesYield, serviceTax);
                serviceTaxOnSurrenderCharges_AS = Double.parseDouble(BIMAST.getServiceTaxOnSurrenderCharges_AY());
                // _serviceTaxOnSurrenderCharges_AS=serviceTaxOnSurrenderCharges_AS;
                // System.out.println("41. serviceTaxOnSurrenderCharges_AS "+BIMAST.getServiceTaxOnSurrenderCharges_AS());
                // System.out.println("41. serviceTaxOnSurrenderCharges_AS "+serviceTaxOnSurrenderCharges_AS);

                BIMAST.setSurrenderValue_AZ();
                ;
                surrenderValue_AT = Double.parseDouble(BIMAST.getSurrenderValue_AZ());
                // _surrenderValue_AT=surrenderValue_AT;
                // System.out.println("42. surrenderValue_AT "+BIMAST.getSurrenderValue_AT());
                // System.out.println("42. surrenderValue_AT "+surrenderValue_AT);

                BIMAST.setDeathBenefit_BA(policyTerm, sumAssured);
                deathBenefit_AU = Double.parseDouble(BIMAST.getDeathBenefit_BA());
                // _deathBenefit_AU=deathBenefit_AU;
                // System.out.println("43. deathBenefit_AU "+BIMAST.getDeathBenefit_AU());
                // System.out.println("43. deathBenefit_AU "+deathBenefit_AU);

                monthNumber++;

                List_BF.add(commonForAllProd.roundUp_Level2(commonForAllProd
                        .getStringWithout_E(reductionYield_BF)));

            }

            double ans = BIMAST.irr(List_BF, 0.001);

            // System.out.println("irr_BB : "+ans +" & irr_BC : "+ans );

            BIMAST.setIRRAnnual_BF(ans);
            irrAnnual_BF = Double.parseDouble(BIMAST.getIRRAnnual_BF());
            // _irrAnnual_BB=irrAnnual_BB;
            // System.out.println("irrAnnual_BB "+irrAnnual_BF +
            // "   "+(8-irrAnnual_BF));

            netYield = Double.parseDouble(commonForAllProd.getRoundOffLevel2(""
                    + (irrAnnual_BF * 100)));

            reductionYield = Double.parseDouble(commonForAllProd.getRoundOffLevel2("" + ((prop.int2 - irrAnnual_BF) * 100)));
        } catch (Exception e) {
            e.printStackTrace();
            // System.out.println("** error **" + e.getMessage());
        }

        arrGuaranteedAddition_AU = null;
        arrGuaranteedAddition_AC = null;
        BIMAST.arrFundValAfterFMCBeforeGA = null;
        BIMAST.arrFundValAfterFMCBeforeGA_AR = null;
        return new String[]{(commonForAllProd.getStringWithout_E(netYield)),
                commonForAllProd.getStringWithout_E(reductionYield)};
    }

    public double getAnnualPremium(double premium, String premFreq) {
        int premFreqMode = getPremFreqMode(premFreq);
        return (premium * premFreqMode);
    }

    public int getPremFreqMode(String premFreq) {

        if (premFreq.equals("Single") || premFreq.equals("Yearly"))
            return 1;
        else if (premFreq.equals("Half Yearly"))
            return 2;
        else if (premFreq.equals("Quarterly"))
            return 4;
        else
            return 12;
    }

    public int calPPT(int premPayingTerm, int policyTerm, String plan) {
        if (plan.equals("Single")) {
            return 1;
        }
        if (selPlan.getSelectedItem().toString().equals("Regular")) {
            return policyTerm;
        } else {
            return premPayingTerm;
        }

    }

    /***************************************** calculation ends here *************************************************/

    /************************************************* help starts here ********************************************************/
    private void updatePolicyTermLabel() {
        try {
            int minPolicyTerm = 0, temp1 = 0, temp2 = 0, maxPolicyTerm = 0;
            String message = "";
            if (selPlan.getSelectedItem().toString().equals("Limited")) {
                temp1 = 10;

            } else if (selPlan.getSelectedItem().toString().equals("Regular")) {
                temp1 = 10;
            } else {
                temp1 = 5;
            }

            temp2 = 18 - Integer.parseInt(ageInYears.getSelectedItem()
                    .toString());
            minPolicyTerm = Math.max(temp1, temp2);
            maxPolicyTerm = Math.min(30, (70 - Integer.parseInt(ageInYears
                    .getSelectedItem().toString())));
            // if (selPlan.getSelectedItem().toString().equals("Regular")
            // || selPlan.getSelectedItem().toString().equals("Limited")) {
            //
            // if (minPolicyTerm == 10 && maxPolicyTerm == 15)
            // message = "(10 or 15 Years)";
            // else if (minPolicyTerm == 10 && maxPolicyTerm > 15)
            // message = "(10 or 15 to " + maxPolicyTerm + " Years)";
            // else if (minPolicyTerm > 10 && minPolicyTerm < 15
            // && maxPolicyTerm >= 15)
            // message = "(15 to " + maxPolicyTerm + " Years)";
            // else if (minPolicyTerm >= 15 && maxPolicyTerm >= 15)
            // message = "(" + minPolicyTerm + " Years to "
            // + maxPolicyTerm + " Years)";
            // else if (minPolicyTerm == 10 && maxPolicyTerm < 15)
            // message = "(10 Years)";
            //
            // } else {
            // message = "(" + minPolicyTerm + " to " + maxPolicyTerm
            // + " years)";
            // }
            message = "(" + minPolicyTerm + " to " + maxPolicyTerm + " years)";
            help_policyTerm.setText(message);
        } catch (Exception e) {/**/
        }
    }

    private void updatePremiumAmtLabel() {
        try {
            double minPremiumAmt = 0, maxPremiumAmt = prop.maxPremiumAmtLimit;
            double premium = 0;
            if (selPlan.getSelectedItem().toString().equals("Single")) {
                minPremiumAmt = 600000;

            } else if (selPlan.getSelectedItem().toString().equals("Limited")) {
                if (selPremiumFrequencyMode.getSelectedItem().toString()
                        .equals("Yearly"))
                    minPremiumAmt = 600000;
                else if (selPremiumFrequencyMode.getSelectedItem().toString()
                        .equals("Half Yearly"))
                    minPremiumAmt = 300000;
                else if (selPremiumFrequencyMode.getSelectedItem().toString()
                        .equals("Quarterly"))
                    minPremiumAmt = 150000;
                else
                    minPremiumAmt = 50000;
            } else if (selPlan.getSelectedItem().toString().equals("Regular")) {
                if (selPremiumFrequencyMode.getSelectedItem().toString()
                        .equals("Yearly"))
                    minPremiumAmt = 600000;
                else if (selPremiumFrequencyMode.getSelectedItem().toString()
                        .equals("Half Yearly"))
                    minPremiumAmt = 300000;
                else if (selPremiumFrequencyMode.getSelectedItem().toString()
                        .equals("Quarterly"))
                    minPremiumAmt = 150000;
                else
                    minPremiumAmt = 50000;
            }

            // help_premiumAmt.setText("(Rs."+currencyFormat.format(minPremiumAmt)+" to Rs."+currencyFormat.format(maxPremiumAmt)+")");
            help_premiumAmt.setText("( Min Rs. "
                    + currencyFormat.format(minPremiumAmt) + ")");
        } catch (Exception ignored) {
        }
    }

    private void updateSAMFlabel() {
        double minSAMF = 0, maxSAMF = 0;

        if (selPlan.getSelectedItem().toString().equals("Single")) {
            minSAMF = 1.25;
			/*if (Integer.parseInt(ageInYears.getSelectedItem().toString()) >= 0
					&& Integer
					.parseInt(ageInYears.getSelectedItem().toString()) <= 44) {
				minSAMF = 1.25;
				maxSAMF = 1.25;
			} else if (Integer
					.parseInt(ageInYears.getSelectedItem().toString()) >= 45
					&& Integer
					.parseInt(ageInYears.getSelectedItem().toString()) <= 55) {
				minSAMF = 1.25;
				maxSAMF = 1.25;
			}*/
        } else if (selPlan.getSelectedItem().toString().equals("Regular")) {
            minSAMF = 7;
			/*if (Integer.parseInt(ageInYears.getSelectedItem().toString()) >= 0
					&& Integer
					.parseInt(ageInYears.getSelectedItem().toString()) <= 44) {
				minSAMF = Math.max(10, (0.5 * Integer.parseInt(selPolicyTerm
						.getSelectedItem().toString())));
				// minSAMF=10;
				maxSAMF = Math.max(10, (0.5 * Integer.parseInt(selPolicyTerm
						.getSelectedItem().toString())));
			} else if (Integer
					.parseInt(ageInYears.getSelectedItem().toString()) >= 45
					&& Integer
					.parseInt(ageInYears.getSelectedItem().toString()) <= 65) {
				minSAMF = Math.max(10, (0.5 * Integer.parseInt(selPolicyTerm
						.getSelectedItem().toString())));
				// minSAMF=10;
				maxSAMF = Math.max(10, (0.5 * Integer.parseInt(selPolicyTerm
						.getSelectedItem().toString())));
			}*/
        } else if (selPlan.getSelectedItem().toString().equals("Limited")) {
            minSAMF = 7;
			/*if (Integer.parseInt(ageInYears.getSelectedItem().toString()) >= 0
					&& Integer
					.parseInt(ageInYears.getSelectedItem().toString()) <= 44) {
				minSAMF = Math.max(10, (0.5 * Integer.parseInt(selPolicyTerm
						.getSelectedItem().toString())));
				// minSAMF=10;
				maxSAMF = Math.max(10, (0.5 * Integer.parseInt(selPolicyTerm
						.getSelectedItem().toString())));
			} else if (Integer
					.parseInt(ageInYears.getSelectedItem().toString()) >= 45
					&& Integer
					.parseInt(ageInYears.getSelectedItem().toString()) <= 65) {
				minSAMF = Math.max(10, (0.5 * Integer.parseInt(selPolicyTerm
						.getSelectedItem().toString())));
				// minSAMF=10;
				maxSAMF = Math.max(10, (0.5 * Integer.parseInt(selPolicyTerm
						.getSelectedItem().toString())));
			}*/
        }
        // help_SAMF.setText("Sum Assured Multiple Factor[SAMF](min. "+minSAMF+"% & Max. "+maxSAMF+"%)");
        //help_SAMF.setText("(" + minSAMF + " to " + maxSAMF + ")");
        SAMF.setText(minSAMF + "");
    }

    // help for no of yeras elapsed since Inception
    private void updatenoOfYearsElapsedSinceInception() {
        try {
            help_noOfYearsElapsedSinceInception.setText("(5 to "
                    + selPolicyTerm.getSelectedItem().toString() + " years)");
        } catch (Exception ignored) {
        }
    }

    /********************************************* help ends here *****************************************************************************************/

    /******************************************** validation starts here ************************************************************************************/
    // premium amount validation
    private boolean valPremiumAmt() {
        double minPremiumAmt = 0, maxPremiumAmt = prop.maxPremiumAmtLimit;
        double premium = 0;
        String error = "";
        if (selPlan.getSelectedItem().toString().equals("Single")) {
            minPremiumAmt = 600000;

        } else if (selPlan.getSelectedItem().toString().equals("Limited")) {
            if (selPremiumFrequencyMode.getSelectedItem().toString()
                    .equals("Yearly"))
                minPremiumAmt = 600000;
            else if (selPremiumFrequencyMode.getSelectedItem().toString()
                    .equals("Half Yearly"))
                minPremiumAmt = 300000;
            else if (selPremiumFrequencyMode.getSelectedItem().toString()
                    .equals("Quarterly"))
                minPremiumAmt = 150000;
            else
                minPremiumAmt = 50000;
        } else if (selPlan.getSelectedItem().toString().equals("Regular")) {
            if (selPremiumFrequencyMode.getSelectedItem().toString()
                    .equals("Yearly"))
                minPremiumAmt = 600000;
            else if (selPremiumFrequencyMode.getSelectedItem().toString()
                    .equals("Half Yearly"))
                minPremiumAmt = 300000;
            else if (selPremiumFrequencyMode.getSelectedItem().toString()
                    .equals("Quarterly"))
                minPremiumAmt = 150000;
            else
                minPremiumAmt = 50000;
        }
        if (premiumAmt.getText().toString().equals("")) {
            error = "Please enter Premium Amount in Rs. ";
        } else if (!(Double.parseDouble(premiumAmt.getText().toString()) % 100 == 0)) {
            error = "Premium Amount should be multiple of 100";
        } else {
            if (Double.parseDouble(premiumAmt.getText().toString()) < minPremiumAmt) {
                error = "Premium Amount should be greater than Rs. "
                        + currencyFormat.format(minPremiumAmt);
            }

        }

        if (!error.equals("")) {
            showAlert.setMessage(error);
            showAlert.setNeutralButton("OK",
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub
                            premiumAmt.requestFocus();
                        }
                    });
            showAlert.show();

            return false;
        } else
            return true;
    }

    // private void setContentView(Class<layout> class1) {
    // // TODO Auto-generated method stub
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
                minSAMF = 1.25;
				/*if (Integer.parseInt(ageInYears.getSelectedItem().toString()) >= 0
						&& Integer.parseInt(ageInYears.getSelectedItem()
						.toString()) <= 44) {
					minSAMF = 1.25;
					maxSAMF = 1.25;
				} else if (Integer.parseInt(ageInYears.getSelectedItem()
						.toString()) >= 45
						&& Integer.parseInt(ageInYears.getSelectedItem()
						.toString()) <= 55) {
					minSAMF = 1.1;
					maxSAMF = 1.25;
				}*/
            } else if (selPlan.getSelectedItem().toString().equals("Regular")) {
                minSAMF = 7;
				/*if (Integer.parseInt(ageInYears.getSelectedItem().toString()) >= 0
						&& Integer.parseInt(ageInYears.getSelectedItem()
						.toString()) <= 44) {
					minSAMF = Math.max(10, (0.5 * Integer
							.parseInt(selPolicyTerm.getSelectedItem()
									.toString())));
					// minSAMF=10;
					maxSAMF = Math.max(10, (0.5 * Integer
							.parseInt(selPolicyTerm.getSelectedItem()
									.toString())));
				} else if (Integer.parseInt(ageInYears.getSelectedItem()
						.toString()) >= 45
						&& Integer.parseInt(ageInYears.getSelectedItem()
						.toString()) <= 65) {
					minSAMF = Math.max(10, (0.5 * Integer
							.parseInt(selPolicyTerm.getSelectedItem()
									.toString())));
					// minSAMF=10;
					maxSAMF = Math.max(10, (0.5 * Integer
							.parseInt(selPolicyTerm.getSelectedItem()
									.toString())));
				}*/
            } else if (selPlan.getSelectedItem().toString().equals("Limited")) {
                minSAMF = 7;
				/*if (Integer.parseInt(ageInYears.getSelectedItem().toString()) >= 0
						&& Integer.parseInt(ageInYears.getSelectedItem()
						.toString()) <= 44) {
					minSAMF = Math.max(10, (0.5 * Integer
							.parseInt(selPolicyTerm.getSelectedItem()
									.toString())));
					// minSAMF=10;
					maxSAMF = Math.max(10, (0.5 * Integer
							.parseInt(selPolicyTerm.getSelectedItem()
									.toString())));
				} else if (Integer.parseInt(ageInYears.getSelectedItem()
						.toString()) >= 45
						&& Integer.parseInt(ageInYears.getSelectedItem()
						.toString()) <= 65) {
					minSAMF = Math.max(10, (0.5 * Integer
							.parseInt(selPolicyTerm.getSelectedItem()
									.toString())));
					// minSAMF=10;
					maxSAMF = Math.max(10, (0.5 * Integer
							.parseInt(selPolicyTerm.getSelectedItem()
									.toString())));
				}*/
            }
			/*if (Double.parseDouble(SAMF.getText().toString()) < minSAMF
					|| Double.parseDouble(SAMF.getText().toString()) > maxSAMF) {
				error = "Sum Assured Multiple Factor (SAMF) should be in the range of "
						+ minSAMF + " to " + maxSAMF;
			}*/
            if (Double.parseDouble(SAMF.getText().toString()) != minSAMF) {
                error = "Sum Assured Multiple Factor (SAMF) should be "
                        + minSAMF;
            }
        }

        if (!error.equals("")) {
            showAlert.setMessage(error);
            showAlert.setNeutralButton("OK",
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub
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
    // // TODO Auto-generated method stub
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
        int temp1 = 0, temp2 = 0;
        String error = "";

        if (selPlan.getSelectedItem().toString().equals("Limited")) {
            temp1 = 10;

        } else if (selPlan.getSelectedItem().toString().equals("Regular")) {
            temp1 = 10;
        } else {
            temp1 = 5;
        }

        temp2 = 18 - Integer.parseInt(ageInYears.getSelectedItem().toString());
        minPolicyTerm = Math.max(temp1, temp2);
        maxPolicyTerm = Math
                .min(30, (70 - Integer.parseInt(ageInYears.getSelectedItem()
                        .toString())));

        // if(selPlan.getSelectedItem().toString().equals("Regular") ||
        // selPlan.getSelectedItem().toString().equals("Limited"))
        // {
        //
        // if(Integer.parseInt(selPolicyTerm.getSelectedItem().toString()) <
        // minPolicyTerm ||
        // Integer.parseInt(selPolicyTerm.getSelectedItem().toString()) >
        // maxPolicyTerm ||
        // (Integer.parseInt(selPolicyTerm.getSelectedItem().toString())>=11 &&
        // Integer.parseInt(selPolicyTerm.getSelectedItem().toString())<=14))
        // {
        // if(minPolicyTerm ==10 && maxPolicyTerm ==15)
        // error="Policy Term Should be 10 or 15 Years";
        // else if(minPolicyTerm ==10 && maxPolicyTerm >15)
        // error = "Policy Term Should be 10 or between 15 to " + maxPolicyTerm
        // + " Years" ;
        // else if(minPolicyTerm > 10 && minPolicyTerm < 15 && maxPolicyTerm
        // >=15)
        // error = "Policy Term Should be between 15 to " + maxPolicyTerm +
        // " Years";
        // else if( minPolicyTerm >=15 && maxPolicyTerm >=15)
        // error =
        // "Policy Term should be in between "+minPolicyTerm+" Years to "+maxPolicyTerm+" Years";
        // else if(minPolicyTerm ==10 && maxPolicyTerm <15)
        // error = "Policy term should be 10 Years";
        // }
        // }
        // else
        // {
        if ((Integer.parseInt(selPolicyTerm.getSelectedItem().toString()) < minPolicyTerm)
                || (Integer
                .parseInt(selPolicyTerm.getSelectedItem().toString()) > maxPolicyTerm)) {
            error = "Policy Term should be in between " + minPolicyTerm
                    + " Years to " + maxPolicyTerm + " Years";
        }
        // }

        if (!error.equals("")) {
            showAlert.setMessage(error);
            showAlert.setNeutralButton("OK",
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub

                        }
                    });
            showAlert.show();
            return false;
        } else
            return true;
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
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            selPolicyTerm.setSelection(0, false);
                            // apply for focusable
                            setFocusable(btn_bi_smart_privilege_life_assured_date);
                            btn_bi_smart_privilege_life_assured_date
                                    .requestFocus();
                        }
                    });
            showAlert.show();

        }

        // }

    }

    // Addition of invested fund mub be 100%
    public boolean valTotalAllocation() {
        String error;
        double equityFund, equityOptFund, corporateBondFund, growthFund, balancedFund, bondFund, moneyMarketFund, top300Fund, pureFund, midcapFund, bondOptimiserFund2, moneyMarketFund2;

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

        if (!percent_Top300Fund.getText().toString().equals(""))
            top300Fund = Double.parseDouble(percent_Top300Fund.getText()
                    .toString());
        else
            top300Fund = 0;

        if (!percent_PureFund.getText().toString().equals(""))
            pureFund = Double
                    .parseDouble(percent_PureFund.getText().toString());
        else
            pureFund = 0;

        if (!percent_MidcapFund.getText().toString().equals(""))
            midcapFund = Double.parseDouble(percent_MidcapFund.getText()
                    .toString());
        else
            midcapFund = 0;

        if (!percent_CorporateBondFund.getText().toString().equals(""))
            corporateBondFund = Double.parseDouble(percent_CorporateBondFund.getText()
                    .toString());
        else
            corporateBondFund = 0;

        if (!percent_bondOptimiserFund2.getText().toString().equals(""))
            bondOptimiserFund2 = Double.parseDouble(percent_bondOptimiserFund2.getText()
                    .toString());
        else
            bondOptimiserFund2 = 0;

        if (!percent_moneyMarketFund2.getText().toString().equals(""))
            moneyMarketFund2 = Double.parseDouble(percent_moneyMarketFund2.getText()
                    .toString());
        else
            moneyMarketFund2 = 0;

        if ((equityFund + equityOptFund + growthFund + balancedFund + bondFund
                + top300Fund + pureFund + midcapFund + corporateBondFund + bondOptimiserFund2 + moneyMarketFund2) != 100) {
            showAlert
                    .setMessage("Total sum of % to be invested for all fund should be equal to 100%");
            showAlert.setNeutralButton("OK",
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub
                            percent_EquityFund.requestFocus();

                        }
                    });
            showAlert.show();

            return false;
        } else
            return true;
    }

    public boolean valLifeAssuredDetail() {

        if (btn_bi_smart_privilege_proposer_date.getText().toString()
                .equals("")) {
            commonMethods.dialogWarning(context, "Please Select Proposer DOB First", true);
            return false;
        } else if ((Integer
                .parseInt(!edt_bi_smart_privilege_proposer_age.getText()
                        .toString().equals("") ? edt_bi_smart_privilege_proposer_age
                        .getText().toString() : "0")) < 18) {
            if (edt_bi_smart_privilege_life_assured_age.getText().toString()
                    .equals("")) {
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

    public boolean valProposerSameAsLifeAssured() {
        if (!proposer_Is_Same_As_Life_Assured.equals("")) {
            return true;
        } else {
            showAlert
                    .setMessage("Please Select Whether Proposer Is Same As Life Assured");
            showAlert.setNeutralButton("OK",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // apply focusable method

                        }
                    });
            showAlert.show();

            return false;

        }
    }

    public boolean valLifeAssuredProposerDetail() {
        lifeAssured_Title = spnr_bi_smart_privilege_life_assured_title
                .getSelectedItem().toString();
        proposer_Title = spnr_bi_smart_privilege_proposer_title
                .getSelectedItem().toString();

        if (proposer_Is_Same_As_Life_Assured.equalsIgnoreCase("y")) {
            if (lifeAssured_Title.equals("")
                    || lifeAssured_Title.equals("Select Title")
                    || lifeAssured_First_Name.equals("")
                    || lifeAssured_Last_Name.equals("")) {

                showAlert.setMessage("Please Fill Name Detail For LifeAssured");
                showAlert.setNeutralButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                if (lifeAssured_Title.equals("")) {
                                    // apply focusable method
                                    setFocusable(spnr_bi_smart_privilege_life_assured_title);
                                    spnr_bi_smart_privilege_life_assured_title
                                            .requestFocus();
                                } else if (lifeAssured_First_Name.equals("")) {

                                    edt_bi_smart_privilege_life_assured_first_name
                                            .requestFocus();
                                } else {
                                    edt_bi_smart_privilege_life_assured_last_name
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
                                setFocusable(spnr_bi_smart_privilege_life_assured_title);
                                spnr_bi_smart_privilege_life_assured_title
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
                                setFocusable(spnr_bi_smart_privilege_life_assured_title);
                                spnr_bi_smart_privilege_life_assured_title
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
                                setFocusable(spnr_bi_smart_privilege_life_assured_title);
                                spnr_bi_smart_privilege_life_assured_title
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
                    || lifeAssured_Title.equals("Select Title")
                    || lifeAssured_First_Name.equals("")
                    || lifeAssured_Last_Name.equals("")) {

                showAlert.setMessage("Please Fill Name Detail For LifeAssured");
                showAlert.setNeutralButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                if (lifeAssured_Title.equals("")) {
                                    // apply focusable method
                                    setFocusable(spnr_bi_smart_privilege_life_assured_title);
                                    spnr_bi_smart_privilege_life_assured_title
                                            .requestFocus();
                                } else if (lifeAssured_First_Name.equals("")) {

                                    edt_bi_smart_privilege_life_assured_first_name
                                            .requestFocus();
                                } else {
                                    edt_bi_smart_privilege_life_assured_last_name
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
                                setFocusable(spnr_bi_smart_privilege_life_assured_title);
                                spnr_bi_smart_privilege_life_assured_title
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
                                setFocusable(spnr_bi_smart_privilege_life_assured_title);
                                spnr_bi_smart_privilege_life_assured_title
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
                                setFocusable(spnr_bi_smart_privilege_life_assured_title);
                                spnr_bi_smart_privilege_life_assured_title
                                        .requestFocus();
                            }
                        });
                showAlert.show();

                return false;
            } else if (proposer_Title.equals("")
                    || proposer_Title.equals("Select Title")
                    || proposer_First_Name.equals("")
                    || proposer_Last_Name.equals("")) {

                showAlert.setMessage("Please Fill Name Detail For Proposer");
                showAlert.setNeutralButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int whic) {
                                if (proposer_Title.equals("")) {
                                    // apply focusable method
                                    setFocusable(spnr_bi_smart_privilege_proposer_title);
                                    spnr_bi_smart_privilege_proposer_title
                                            .requestFocus();
                                } else if (proposer_First_Name.equals("")) {
                                    edt_bi_smart_privilege_proposer_first_name
                                            .requestFocus();
                                } else {
                                    edt_bi_smart_privilege_proposer_last_name
                                            .requestFocus();
                                }
                            }
                        });
                showAlert.show();

                return false;

            } else if (gender_proposer.equalsIgnoreCase("")) {

                showAlert
                        .setMessage("Please Select Proposer Gender");
                showAlert.setNeutralButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // apply focusable method
                                setFocusable(spnr_bi_smart_privilege_proposer_title);
                                spnr_bi_smart_privilege_proposer_title
                                        .requestFocus();
                            }
                        });
                showAlert.show();

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
                                setFocusable(spnr_bi_smart_privilege_proposer_title);
                                spnr_bi_smart_privilege_proposer_title
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
                                setFocusable(spnr_bi_smart_privilege_proposer_title);
                                spnr_bi_smart_privilege_proposer_title
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
                                setFocusable(spnr_bi_smart_privilege_proposer_title);
                                spnr_bi_smart_privilege_proposer_title
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
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // apply focusable method
                                setFocusable(btn_bi_smart_privilege_life_assured_date);
                                btn_bi_smart_privilege_life_assured_date
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
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // apply focusable method
                                setFocusable(btn_bi_smart_privilege_life_assured_date);
                                btn_bi_smart_privilege_life_assured_date
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
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // apply focusable method
                                setFocusable(btn_bi_smart_privilege_proposer_date);
                                btn_bi_smart_privilege_proposer_date
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
        if (/*
         * (tr_smart_privilege_proposer_detail1.getVisibility() ==
         * View.VISIBLE) ||
         * (tr_smart_privilege_proposer_detail2.getVisibility() ==
         * View.VISIBLE)
         */
                tablelayoutSmartPrivilegeProposerTitle.getVisibility() == View.VISIBLE) {
            if (proposer_Title.equals("") || proposer_First_Name.equals("")
                    || proposer_Last_Name.equals("")) {

                showAlert.setMessage("Please Fill Name Detail For Proposer");
                showAlert.setNeutralButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                if (proposer_Title.equals("")) {
                                    spnr_bi_smart_privilege_proposer_title
                                            .requestFocus();
                                } else if (proposer_First_Name.equals("")) {
                                    edt_bi_smart_privilege_proposer_first_name
                                            .requestFocus();
                                } else {
                                    edt_bi_smart_privilege_proposer_last_name
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
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // apply focusable method
                                setFocusable(btn_bi_smart_privilege_proposer_date);
                                btn_bi_smart_privilege_proposer_date
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
        System.out.println("Current time => " + c.getTime());

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

    // @Override
    // public void onBackPressed() {
    // // TODO Auto-generated method stub
    // super.onBackPressed();
    // d.dismiss();
    // }
    private String getformatedThousandString(double number) {
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
        // TODO Auto-generated method stub
        v.setFocusable(true);
        v.setFocusableInTouchMode(true);
    }

    // method to set a clearing a element
    private void clearFocusable(View v) {
        // TODO Auto-generated method stub
        v.setFocusable(false);
        v.setFocusableInTouchMode(false);
        // v.clearFocus();
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        // TODO Auto-generated method stub
        if (v.getId() == edt_bi_smart_privilege_life_assured_first_name.getId()) {
            // clearFocusable(edt_bi_smart_privilege_life_assured_first_name);
            setFocusable(edt_bi_smart_privilege_life_assured_middle_name);
            edt_bi_smart_privilege_life_assured_middle_name.requestFocus();
        } else if (v.getId() == edt_bi_smart_privilege_life_assured_middle_name
                .getId()) {
            // clearFocusable(edt_bi_smart_privilege_life_assured_middle_name);
            setFocusable(edt_bi_smart_privilege_life_assured_last_name);
            edt_bi_smart_privilege_life_assured_last_name.requestFocus();
        } else if (v.getId() == edt_bi_smart_privilege_life_assured_last_name
                .getId()) {

            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(
                    edt_bi_smart_privilege_life_assured_last_name
                            .getWindowToken(), 0);
            // clearFocusable(edt_bi_smart_privilege_life_assured_last_name);
            setFocusable(btn_bi_smart_privilege_life_assured_date);
            btn_bi_smart_privilege_life_assured_date.requestFocus();
        } else if (v.getId() == edt_bi_smart_privilege_proposer_first_name
                .getId()) {
            // clearFocusable(edt_bi_smart_privilege_proposer_first_name);
            setFocusable(edt_bi_smart_privilege_proposer_middle_name);
            edt_bi_smart_privilege_proposer_middle_name.requestFocus();
        } else if (v.getId() == edt_bi_smart_privilege_proposer_middle_name
                .getId()) {
            // clearFocusable(edt_bi_smart_privilege_proposer_middle_name);
            setFocusable(edt_bi_smart_privilege_proposer_last_name);
            edt_bi_smart_privilege_proposer_last_name.requestFocus();
        } else if (v.getId() == edt_bi_smart_privilege_proposer_last_name
                .getId()) {

            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(
                    edt_bi_smart_privilege_proposer_last_name.getWindowToken(),
                    0);
            // clearFocusable(edt_bi_smart_privilege_proposer_last_name);
            setFocusable(btn_bi_smart_privilege_proposer_date);
            btn_bi_smart_privilege_proposer_date.requestFocus();
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
            // clearFocusable(percent_MoneyMktFund);
            setFocusable(percent_Top300Fund);
            percent_Top300Fund.requestFocus();
        } else if (v.getId() == percent_Top300Fund.getId()) {
            // clearFocusable(percent_Top300Fund);
            setFocusable(percent_PureFund);
            percent_PureFund.requestFocus();
        } else if (v.getId() == percent_PureFund.getId()) {
            // clearFocusable(percent_Top300Fund);
            setFocusable(percent_MidcapFund);
            percent_MidcapFund.requestFocus();
        } else if (v.getId() == percent_MidcapFund.getId()) {
            // clearFocusable(percent_Top300Fund);
            setFocusable(percent_bondOptimiserFund2);
            percent_bondOptimiserFund2.requestFocus();
        } else if (v.getId() == percent_bondOptimiserFund2.getId()) {
            // clearFocusable(percent_Top300Fund);
            setFocusable(percent_CorporateBondFund);
            percent_CorporateBondFund.requestFocus();
        } else if (v.getId() == percent_CorporateBondFund.getId()) {
            // clearFocusable(percent_CorporateBondFund);
            setFocusable(percent_moneyMarketFund2);
            percent_moneyMarketFund2.requestFocus();
        } else if (v.getId() == percent_moneyMarketFund2.getId()) {
            // clearFocusable(percent_Top300Fund);
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
        // TODO Auto-generated method stub
        super.onResume();
        b = 1;

        boolean flagFocus = true;
        if (!flagFocus) {
            sv_smart_privilege_main.requestFocus();
        } else {
            edt_bi_smart_privilege_life_assured_first_name.requestFocus();
        }

    }

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
			commonMethods.dialogWarning(context, "Please Fill Email Id", true);
			edt_proposerdetail_basicdetail_Email_id.requestFocus();
			return false;

		}

		else if (ConfirmEmailId.equals("")) {

			commonMethods.dialogWarning(context, "Please Fill Confirm Email Id", true);
			edt_proposerdetail_basicdetail_ConfirmEmail_id.requestFocus();
			return false;
		} else if (!ConfirmEmailId.equals(emailId)) {
			commonMethods.dialogWarning(context, "Email Id Does Not Match", true);
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

    private void updatePremiumPayingTerm(int policyTerm) {
        try {
            maxPremiumPayingTerm = policyTerm - 1;

            String[] premPayingTermList = new String[maxPremiumPayingTerm
                    - minPremiumPayingTerm + 1];

            for (int i = minPremiumPayingTerm; i <= maxPremiumPayingTerm; i++) {
                premPayingTermList[i - minPremiumPayingTerm] = i + "";
            }
            ArrayAdapter<String> premPayingTermAdapter = new ArrayAdapter<>(
                    getApplicationContext(), R.layout.spinner_item,
                    premPayingTermList);
            premPayingTermAdapter
                    .setDropDownViewResource(R.layout.spinner_item1);
            premPayingTerm.setAdapter(premPayingTermAdapter);
            premPayingTermAdapter.notifyDataSetChanged();

        } catch (Exception ignored) {
        }
    }

    private boolean valPlan() {

        if (selPlan.getSelectedItem().toString().equalsIgnoreCase("single")) {

            if (Integer.parseInt(ageInYears.getSelectedItem().toString()) < 13) {
                showAlert
                        .setMessage("Life Assured Date of Birth Must Be Greater Than 13 years for Single Plan");
                showAlert.setNeutralButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // apply focusable method
                                setFocusable(btn_bi_smart_privilege_life_assured_date);
                                btn_bi_smart_privilege_life_assured_date
                                        .requestFocus();
                                lifeAssured_date_of_birth = "";
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

}