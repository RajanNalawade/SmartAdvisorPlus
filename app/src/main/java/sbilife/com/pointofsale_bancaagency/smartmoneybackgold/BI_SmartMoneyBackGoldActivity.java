package sbilife.com.pointofsale_bancaagency.smartmoneybackgold;

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
import com.itextpdf.text.Chunk;
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

@SuppressWarnings("deprecation")
public class BI_SmartMoneyBackGoldActivity extends AppCompatActivity implements
        OnEditorActionListener {

    private NeedAnalysisBIService NABIObj;
    private NA_CBI_bean na_cbi_bean;
    private File mypath;
    private File needAnalysispath;
    private File newFile;
    private String agentcode;
    private String agentMobile;
    private String agentEmail;
    private String userType;
    private int needAnalysis_flag = 0;
    private String na_input = null;
    private String na_output = null;

    // String guaranMatBen = null, nonGuaranMatBen_4Percent = null,
    // nonGuaranMatBen_8Percent = null;
    // UI Elements
    private CheckBox CbJkResident;
    private CheckBox cbADBRider;
    private CheckBox cbATPDBRider;
    private CheckBox cbCC13NonLinkedRider;
    private CheckBox cbPTARider;
    private CheckBox cb_staffdisc;
    private Button btnSubmit;
    private Button btnBack;
    // Spinner USed
    private Spinner spnr_bi_smart_money_back_gold_life_assured_title;


    private String PremPaymentTerm = "", premFreqOptions = "";

    // for BI
    private StringBuilder inputVal;
    private String premPayingMode = "";
    private String ptRiderTerm = "";
    private String adbRiderTerm = "";
    private String atpdbRiderTerm = "";
    private String ccnlRiderTerm = "";
    private String policyTerm = "";
    private String ageAtEntry = "";
    private String gender = "";
    private String sumAssured = "";
    private String ptSA = "";
    private String adbSA = "";
    private String atpdbSA = "";
    private String ccnlSA = "";
    private String ccnlRiderYearly = "";
    private String totalRiderYearly = "", ptRiderYearly = "", premiumFreq = "",
            adbRiderYearly = "", atpdbRiderYearly = "",
            totalRiderTerm = "", totalRiderSA = "";

    private String ptRiderStatus = "";
    private String adbRiderStatus = "";
    private String atpdbRiderStatus = "";
    private String ccnlRiderStatus = "";

    private String basicCoverYearlyPremium = "";
    private String planProposedName = "";
    private String totalPremiumYearlyInstallmentPremYearly = "";
    private String totalPremiumYearlyInstallmentPremWithServiceTaxYearly = "";

    private String BackdatingInt;
    private String premWthSTSecondYear = "";

    private SmartMoneyBackGoldBean smartMoneyBackGoldBean;

    private EditText edt_SumAssured;
    private EditText edt_AdbSA;
    private EditText edt_AtpdbSA;
    private EditText edt_PtaSA;
    private EditText edt_Cc13NonLinkedSA;

    private EditText edt_bi_smart_wealth_builder_proposer_age;

    private Spinner spnrPremPayingMode;
    private Spinner spnrPolicyTerm;
    private Spinner spnrPlan;
    private Spinner spnrGender;
    private Spinner spnrAge;
    private Spinner spnr_AdbTerm;
    private Spinner spnr_AtpdbTerm;
    private Spinner spnr_PtaTerm;
    private Spinner spnr_Cc13NonLinkedTerm;

    private TableRow trADBRider;
    private TableRow trATPDBRider;
    private TableRow trCritiCareRider;
    private TableRow trPreffredTermRider;

    private RadioButton rb_proposerdetail_personaldetail_backdating_yes;
    private RadioButton rb_proposerdetail_personaldetail_backdating_no;

    private LinearLayout ll_backdating1;

    private Button btn_proposerdetail_personaldetail_backdatingdate;
    private Button btn_MarketingOfficalDate;
    private Button btn_PolicyholderDate;

    private ImageButton Ibtn_signatureofMarketing;
    private ImageButton Ibtn_signatureofPolicyHolders;

    public String frmProductHome = "";

    private Dialog d;
    private final int SIGNATURE_ACTIVITY = 1;
    private String latestImage = "";

    // List used for The Policy Detail Depend on The policy Term
    private List<M_BI_SmartMoneyBackGold_Adapter> list_data;

    // Database Related Variable
    // newDBHelper db;

    /* For DashBoard */
    // To Store User Info and sync info
    private DatabaseHelper dbHelper;


    // Variable Used For Date Purpose
    private final int DATE_DIALOG_ID = 1;
    private int DIALOG_ID;
    private int mYear;
    private int mMonth;
    private int mDay;

    private Button btn_bi_smart_money_back_gold_life_assured_date;

    private String QuatationNumber = "";
    private String planName = "";

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

    private String proposer_Backdating_WishToBackDate_Policy = "";
    private String proposer_Backdating_BackDate = "";

    // Spinner USed

    private String proposer_date_of_birth = "";

    // edit Text Used
    private EditText edt_bi_smart_money_back_gold_life_assured_first_name;
    private EditText edt_bi_smart_money_back_gold_life_assured_middle_name;
    private EditText edt_bi_smart_money_back_gold_life_assured_last_name;

    private int minAge = 0;
    private int maxAge = 0;


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
    private String proposer_Is_Same_As_Life_Assured = "Y";

    // Class Declaration
    private CommonForAllProd commonForAllProd;

    private AlertDialog.Builder showAlert;
    private DecimalFormat currencyFormat;

    private StringBuilder bussIll = null;
    private StringBuilder retVal = null;
    // File mypath;

    private ScrollView svSmartMoneyBackGoldMain;

    private boolean flagFirstFocus = true;

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

    /* For Proposer Details */
//	private TableRow tr_money_back_gold_proposer_detail1;
    private LinearLayout tr_money_back_gold_proposer_detail2;
    private Spinner spnr_bi_money_back_gold_proposer_title;
    private EditText edt_bi_money_back_gold_proposer_first_name;
    private EditText edt_bi_money_back_gold_proposer_middle_name;
    private EditText edt_bi_money_back_gold_proposer_last_name;
    private Button btn_bi_money_back_gold_proposer_date;
    private LinearLayout ll_riders;

    private String strProposerAge = "";
    private TableRow tr_proposer_gender, tr_proposer_gender2;

    private String product_Code, product_UIN, product_cateogory, product_type;
    private Context context;

    private String bankUserType = "", mode = "";

    private String str_kerla_discount = "No";

    private String Check = "";
    private CommonMethods commonMethods;
    private StorageUtils mStorageUtils;
    private ImageButton imageButtonSmartMoneyBackGoldProposerPhotograph;

    private LinearLayout linearlayoutThirdpartySignature;
    private LinearLayout linearlayoutAppointeeSignature;
    private ImageButton Ibtn_signatureofThirdParty, Ibtn_signatureofAppointee,
            Ibtn_signatureofLifeAssured;
    private String thirdPartySign = "", appointeeSign = "",
            proposerAsLifeAssuredSign = "", Company_policy_surrender_dec = "";
    private CheckBox cb_kerladisc;

    private String servicetax_MinesOccuInterest;

    private Spinner spnrPremPaymentTerm;
    private Spinner selPremFreq;


    private TableRow tr_premium_paying_mode;
    private String guaranteed_death_benefit_str = "";


    private String gender_proposer = "";
    private Spinner spinnerProposerGender;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.bi_smart_money_back_goldmain);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);


        context = this;
        commonMethods = new CommonMethods();
        mStorageUtils = new StorageUtils();
        dbHelper = new DatabaseHelper(getApplicationContext());
        new CommonMethods().setApplicationToolbarMenu(this,
                getString(R.string.app_name));

        NABIObj = new NeedAnalysisBIService(this);
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

                    ProductInfo prodInfoObj = new ProductInfo();
                    product_name = "Smart Money Back Gold";
                    planName = "Smart Money Back Gold";
                    product_Code = prodInfoObj.getProductCode(product_name);
                    product_UIN = prodInfoObj.getProductUIN(product_name);
                    product_cateogory = prodInfoObj
                            .getProductCategory(product_name);
                    product_type = prodInfoObj.getProductType(product_name);
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

        ll_riders = findViewById(R.id.ll_riders);
        svSmartMoneyBackGoldMain = findViewById(R.id.sv_bi_smart_money_back_gold);

        rb_proposerdetail_personaldetail_backdating_yes = findViewById(R.id.rb_smart_money_back_backdating_yes);
        rb_proposerdetail_personaldetail_backdating_no = findViewById(R.id.rb_smart_money_back_backdating_no);

        ll_backdating1 = findViewById(R.id.ll_backdating1);
        btn_proposerdetail_personaldetail_backdatingdate = findViewById(R.id.btn_smart_money_back_backdatingdate);
        edt_SumAssured = findViewById(R.id.et_bi_smart_money_back_gold_sum_assured);
        edt_PtaSA = findViewById(R.id.et_bi_smart_money_back_gold_pta_rider_sum_assured);
        edt_AdbSA = findViewById(R.id.et_bi_smart_money_back_gold_adb_rider_sum_assured);
        edt_AtpdbSA = findViewById(R.id.et_bi_smart_money_back_gold_atpd_rider_sum_assured);
        edt_Cc13NonLinkedSA = findViewById(R.id.et_bi_smart_money_back_gold_ccnl_rider_sum_assured);

        trADBRider = findViewById(R.id.tr_bi_smart_money_back_gold_adb_rider);
        trATPDBRider = findViewById(R.id.tr_bi_smart_money_back_gold_atpd_rider);
        trCritiCareRider = findViewById(R.id.tr_bi_smart_money_back_gold_ccnl_rider);
        trPreffredTermRider = findViewById(R.id.tr_bi_smart_money_back_gold_pta_rider);

        btnSubmit = findViewById(R.id.btn_bi_smart_money_back_gold_btnSubmit);
        btnBack = findViewById(R.id.btn_bi_smart_money_back_gold_btnback);

//		tr_money_back_gold_proposer_detail1 = findViewById(R.id.tr_money_back_gold_proposer_detail1);
        tr_money_back_gold_proposer_detail2 = findViewById(R.id.tr_money_back_gold_proposer_detail2);
        spnr_bi_money_back_gold_proposer_title = findViewById(R.id.spnr_bi_money_back_gold_proposer_title);
        edt_bi_money_back_gold_proposer_first_name = findViewById(R.id.edt_bi_money_back_gold_proposer_first_name);
        edt_bi_money_back_gold_proposer_middle_name = findViewById(R.id.edt_bi_money_back_gold_proposer_middle_name);
        edt_bi_money_back_gold_proposer_last_name = findViewById(R.id.edt_bi_money_back_gold_proposer_last_name);
        btn_bi_money_back_gold_proposer_date = findViewById(R.id.btn_bi_money_back_gold_proposer_date);
        edt_bi_smart_wealth_builder_proposer_age = findViewById(R.id.edt_bi_smart_wealth_builder_proposer_age);
        spinnerProposerGender = findViewById(R.id.spinnerProposerGender);

        btn_bi_smart_money_back_gold_life_assured_date = findViewById(R.id.btn_bi_smart_money_back_gold_life_assured_date);
        spnr_bi_smart_money_back_gold_life_assured_title = findViewById(R.id.spnr_bi_smart_money_back_gold_life_assured_title);
        edt_bi_smart_money_back_gold_life_assured_first_name = findViewById(R.id.edt_bi_smart_money_back_gold_life_assured_first_name);
        edt_bi_smart_money_back_gold_life_assured_middle_name = findViewById(R.id.edt_bi_smart_money_back_gold_life_assured_middle_name);
        edt_bi_smart_money_back_gold_life_assured_last_name = findViewById(R.id.edt_bi_smart_money_back_gold_life_assured_last_name);
        spnrPremPayingMode = findViewById(R.id.spnr_bi_smart_money_back_gold_premium_paying_mode);
        spnrPolicyTerm = findViewById(R.id.spnr_bi_smart_money_back_gold_policyterm);

        spnr_AdbTerm = findViewById(R.id.spnr_bi_smart_money_back_gold_adb_rider_term);
        spnr_AtpdbTerm = findViewById(R.id.spnr_bi_smart_money_back_gold_atpdb_rider_term);
        spnr_PtaTerm = findViewById(R.id.spnr_bi_smart_money_back_gold_pta_rider_term);
        spnr_Cc13NonLinkedTerm = findViewById(R.id.spnr_bi_smart_money_back_gold_ccnl_rider_term);

        spnrPlan = findViewById(R.id.spnr_bi_smart_money_back_gold_plan);
        spnrGender = findViewById(R.id.spnr_bi_smart_money_back_gold_selGender);
//		spnrGender.setClickable(false);
//		spnrGender.setEnabled(false);
        spnrAge = findViewById(R.id.spnr_bi_smart_money_back_gold_age);

        CbJkResident = findViewById(R.id.cb_bi_smart_money_back_gold_JKResident);
        cb_staffdisc = findViewById(R.id.cb_staffdisc);
        cbADBRider = findViewById(R.id.cb_bi_smart_money_back_gold_adb_rider);
        cbPTARider = findViewById(R.id.cb_bi_smart_money_back_gold_pta_rider);
        cbATPDBRider = findViewById(R.id.cb_bi_smart_money_back_gold_atpdb_rider);
        cbCC13NonLinkedRider = findViewById(R.id.cb_bi_smart_money_back_gold_ccnl_rider);

        edt_proposerdetail_basicdetail_contact_no = findViewById(R.id.edt_smart_money_contact_no);
        edt_proposerdetail_basicdetail_Email_id = findViewById(R.id.edt_smart_money_Email_id);
        edt_proposerdetail_basicdetail_ConfirmEmail_id = findViewById(R.id.edt_smart_money_ConfirmEmail_id);

        tr_premium_paying_mode = findViewById(R.id.tr_premium_paying_mode);
        selPremFreq = findViewById(R.id.premiumfreq);
        spnrPremPaymentTerm = findViewById(R.id.spnr_bi_smart_money_back_gold_premium_payment_term);

        // Class Declaration

        // Intent intent = getIntent();
        // frmProductHome = intent.getStringExtra("DashBoard");
        // retVal = new StringBuilder();
        // bussIll = new StringBuilder();
        prsObj = new ParseXML();

        commonForAllProd = new CommonForAllProd();
        SmartMoneyBackGoldProperties prop = new SmartMoneyBackGoldProperties();

        list_data = new ArrayList<>();

        // Plan
        //String[] planList = {"Option 1", "Option 2", "Option 3", "Option 4"};
        String[] planList = {"Option 1", "Option 2", "Option 3"};
        ArrayAdapter<String> planAdapter = new ArrayAdapter<>(
                getApplicationContext(), R.layout.spinner_item, planList);
        planAdapter.setDropDownViewResource(R.layout.spinner_item1);
        spnrPlan.setAdapter(planAdapter);
        planAdapter.notifyDataSetChanged();
        minAge = 14;
        maxAge = 55;

        // Age
        String[] ageList = new String[43];
        for (int i = 13; i <= 55; i++) {
            ageList[i - 13] = i + "";
        }
        ArrayAdapter<String> ageAdapter = new ArrayAdapter<>(
                getApplicationContext(), R.layout.spinner_item, ageList);
        ageAdapter.setDropDownViewResource(R.layout.spinner_item1);
        spnrAge.setAdapter(ageAdapter);
        spnrAge.setEnabled(false);
        ageAdapter.notifyDataSetChanged();


        String[] premiumFrequencyList = {"Single", "Regular", "Limited"};
        ArrayAdapter<String> premiumFrequencyAdapter = new ArrayAdapter<>(
                getApplicationContext(), R.layout.spinner_item,
                premiumFrequencyList);
        premiumFrequencyAdapter.setDropDownViewResource(R.layout.spinner_item1);
        selPremFreq.setAdapter(premiumFrequencyAdapter);
        premiumFrequencyAdapter.notifyDataSetChanged();


        // Premium Frequency
        String[] premFreqList = {"Yearly", "Half Yearly", "Quarterly",
                "Monthly"};
        ArrayAdapter<String> premFreqAdapter = new ArrayAdapter<>(
                getApplicationContext(), R.layout.spinner_item, premFreqList);
        premFreqAdapter.setDropDownViewResource(R.layout.spinner_item1);
        spnrPremPayingMode.setAdapter(premFreqAdapter);
        premFreqAdapter.notifyDataSetChanged();

        // Gender
        String[] genderList = {"Male", "Female", "Third Gender"};
        ArrayAdapter<String> genderAdapter = new ArrayAdapter<>(
                getApplicationContext(), R.layout.spinner_item, genderList);
        genderAdapter.setDropDownViewResource(R.layout.spinner_item1);
        spnrGender.setAdapter(genderAdapter);
        spinnerProposerGender.setAdapter(genderAdapter);
//		spnrGender.setEnabled(false);
        genderAdapter.notifyDataSetChanged();


        /*
         * title_list.add("Gen."); title_list.add("Lt.Gen.");
         * title_list.add("Maj.Gen."); title_list.add("Brig.");
         * title_list.add("Col."); title_list.add("Lt.Col.");
         * title_list.add("Major"); title_list.add("Capt.");
         * title_list.add("Lt."); title_list.add("Gr.Capt.");
         * title_list.add("Fr."); title_list.add("Dr.");
         */

        commonMethods.fillSpinnerValue(context, spnr_bi_smart_money_back_gold_life_assured_title,
                Arrays.asList(getResources().getStringArray(R.array.arr_select_title)));

        commonMethods.fillSpinnerValue(context, spnr_bi_money_back_gold_proposer_title,
                Arrays.asList(getResources().getStringArray(R.array.arr_select_title)));

        showAlert = new AlertDialog.Builder(this);

        // selPTATerm_txt1 = (TextView) findViewById(R.id.selPTATerm_txt1);
        // selPTATerm_txt2 = (TextView) findViewById(R.id.selPTATerm_txt2);

        // policy Term
        String[] policyTermList = new String[14];
        for (int i = 0; i <= 13; i++) {
            policyTermList[i] = (i + 12) + "";
        }
        ArrayAdapter<String> policyTermAdapter = new ArrayAdapter<>(
                getApplicationContext(), R.layout.spinner_item, policyTermList);
        policyTermAdapter.setDropDownViewResource(R.layout.spinner_item1);
        spnrPolicyTerm.setAdapter(policyTermAdapter);
        spnrPolicyTerm.setEnabled(false);
        policyTermAdapter.notifyDataSetChanged();

        // pta Term
        String[] ptaTermList = new String[26];
        for (int i = 5; i <= 30; i++) {
            ptaTermList[i - 5] = i + "";
        }
        ArrayAdapter<String> ptaTermAdapter = new ArrayAdapter<>(
                getApplicationContext(), R.layout.spinner_item, ptaTermList);
        ptaTermAdapter.setDropDownViewResource(R.layout.spinner_item1);
        spnr_PtaTerm.setAdapter(ptaTermAdapter);
        ptaTermAdapter.notifyDataSetChanged();


        String[] spnrPremPaymentTermList = {"1", "8", "10", "12", "15", "20", "25"};
        ArrayAdapter<String> adapterPPTList = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item, spnrPremPaymentTermList);
        adapterPPTList.setDropDownViewResource(R.layout.spinner_item1);
        spnrPremPaymentTerm.setAdapter(adapterPPTList);
        spnrPremPaymentTerm.setEnabled(false);
        adapterPPTList.notifyDataSetChanged();

        // adb_txt1 = (TextView) findViewById(R.id.adb_txt1);
        // adb_txt2 = (TextView) findViewById(R.id.adb_txt2);

        // adb Term
        String[] adbTermList = new String[26];
        for (int i = 5; i <= 30; i++) {
            adbTermList[i - 5] = i + "";
        }
        ArrayAdapter<String> adbTermAdapter = new ArrayAdapter<>(
                getApplicationContext(), R.layout.spinner_item, adbTermList);
        adbTermAdapter.setDropDownViewResource(R.layout.spinner_item1);
        spnr_AdbTerm.setAdapter(adbTermAdapter);
        adbTermAdapter.notifyDataSetChanged();

        // atpdb_txt1 = (TextView) findViewById(R.id.atpdb_txt1);
        // atpdb_txt2 = (TextView) findViewById(R.id.atpdb_txt2);

        // atpdb Term
        String[] atpdbTermList = new String[26];
        for (int i = 5; i <= 30; i++) {
            atpdbTermList[i - 5] = i + "";
        }
        ArrayAdapter<String> atpdbTermAdapter = new ArrayAdapter<>(
                getApplicationContext(), R.layout.spinner_item, atpdbTermList);
        atpdbTermAdapter.setDropDownViewResource(R.layout.spinner_item1);
        spnr_AtpdbTerm.setAdapter(atpdbTermAdapter);
        atpdbTermAdapter.notifyDataSetChanged();

        // cc 13 Term
        String[] cc13TermList = new String[26];
        for (int i = 5; i <= 30; i++) {
            cc13TermList[i - 5] = i + "";
        }
        ArrayAdapter<String> cc13TermAdapter = new ArrayAdapter<>(
                getApplicationContext(), R.layout.spinner_item, cc13TermList);
        cc13TermAdapter.setDropDownViewResource(R.layout.spinner_item1);
        spnr_Cc13NonLinkedTerm.setAdapter(cc13TermAdapter);
        cc13TermAdapter.notifyDataSetChanged();

        currencyFormat = new DecimalFormat("##,##,##,###");

        // For Staff Discount

        edt_bi_smart_money_back_gold_life_assured_first_name
                .setOnEditorActionListener(this);
        edt_bi_smart_money_back_gold_life_assured_middle_name
                .setOnEditorActionListener(this);
        edt_bi_smart_money_back_gold_life_assured_last_name
                .setOnEditorActionListener(this);
        edt_SumAssured.setOnEditorActionListener(this);
        edt_PtaSA.setOnEditorActionListener(this);
        edt_AdbSA.setOnEditorActionListener(this);
        edt_AtpdbSA.setOnEditorActionListener(this);
        edt_Cc13NonLinkedSA.setOnEditorActionListener(this);

        setSpinnerAndOtherListner();

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

        TableRow tr_staff_disc = findViewById(R.id.tr_smart_money_staff_disc);
        String str_usertype = "";
        try {
            str_usertype = SimpleCrypto.decrypt("SBIL",
                    dbHelper.GetUserType());

        } catch (Exception e) {
            e.printStackTrace();
        }
       /* if (str_usertype.equalsIgnoreCase("BAP")
				|| str_usertype.equalsIgnoreCase("CAG")
				|| str_usertype.equalsIgnoreCase("IMF")) {
			tr_staff_disc.setVisibility(View.GONE);
        }*/
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

        boolean flagFocus = true;
        if (!flagFocus) {
            svSmartMoneyBackGoldMain.requestFocus();
        }

    }

    private void setSpinnerAndOtherListner() {
        // TODO Auto-generated method stub

        cb_kerladisc.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cb_kerladisc.isChecked()) {
                    str_kerla_discount = "Yes";
                    clearFocusable(cb_kerladisc);
                    setFocusable(spnr_bi_smart_money_back_gold_life_assured_title);
                    spnr_bi_smart_money_back_gold_life_assured_title.requestFocus();
                } else {
                    str_kerla_discount = "No";
                    clearFocusable(cb_kerladisc);
                    setFocusable(spnr_bi_smart_money_back_gold_life_assured_title);
                    spnr_bi_smart_money_back_gold_life_assured_title.requestFocus();
                }
            }
        });

        rb_proposerdetail_personaldetail_backdating_yes
                .setOnCheckedChangeListener(new OnCheckedChangeListener() {

                    public void onCheckedChanged(CompoundButton buttonView,
                                                 boolean isChecked) {
                        // TODO Auto-generated method stub
                        if (isChecked) {
                            proposer_Backdating_WishToBackDate_Policy = "y";
                            ll_backdating1.setVisibility(View.VISIBLE);
                            btn_proposerdetail_personaldetail_backdatingdate
                                    .setText("Select Date");
                            proposer_Backdating_BackDate = "";

                            rb_proposerdetail_personaldetail_backdating_yes
                                    .setFocusable(false);
                            clearFocusable(rb_proposerdetail_personaldetail_backdating_yes);
                            setFocusable(btn_proposerdetail_personaldetail_backdatingdate);
                            btn_proposerdetail_personaldetail_backdatingdate
                                    .requestFocus();

                        }
                    }
                });

        rb_proposerdetail_personaldetail_backdating_no
                .setOnCheckedChangeListener(new OnCheckedChangeListener() {

                    public void onCheckedChanged(CompoundButton buttonView,
                                                 boolean isChecked) {
                        // TODO Auto-generated method stub
                        if (isChecked) {

                            proposer_Backdating_WishToBackDate_Policy = "n";
                            proposer_Backdating_BackDate = "";
                            // setDefaultDate();
                            ll_backdating1.setVisibility(View.GONE);

                            spnrAge.setSelection(
                                    getIndex(spnrAge, lifeAssuredAge), false);
                            valAge();
                            rb_proposerdetail_personaldetail_backdating_yes
                                    .setFocusable(false);

                            clearFocusable(rb_proposerdetail_personaldetail_backdating_no);
                            clearFocusable(rb_proposerdetail_personaldetail_backdating_yes);
                            setFocusable(cbPTARider);
                            cbPTARider.requestFocus();

                        }
                    }
                });

        // PTA Rider
        cbPTARider
                .setOnCheckedChangeListener(new OnCheckedChangeListener() {
                    public void onCheckedChanged(CompoundButton buttonView,
                                                 boolean isChecked) {

                        if (isChecked) {
                            if (valEligibilityOfRider()) {
                                trPreffredTermRider.setVisibility(View.VISIBLE);
                                clearFocusable(cbPTARider);
                                clearFocusable(spnr_PtaTerm);
                                setFocusable(spnr_PtaTerm);
                                spnr_PtaTerm.requestFocus();

                            }
                        } else {
                            clearFocusable(cbPTARider);
                            trPreffredTermRider.setVisibility(View.GONE);
                            setFocusable(cbADBRider);
                            cbADBRider.requestFocus();

                        }
                    }
                });

        // ADB Rider

        cbADBRider
                .setOnCheckedChangeListener(new OnCheckedChangeListener() {

                    public void onCheckedChanged(CompoundButton buttonView,
                                                 boolean isChecked) {
                        if (isChecked) {
                            if (valEligibilityOfRider()) {

                                clearFocusable(cbADBRider);
                                trADBRider.setVisibility(View.VISIBLE);
                                clearFocusable(spnr_AdbTerm);
                                setFocusable(spnr_AdbTerm);
                                spnr_AdbTerm.requestFocus();
                            }
                        } else {
                            clearFocusable(cbADBRider);
                            trADBRider.setVisibility(View.GONE);
                            setFocusable(cbATPDBRider);
                            cbATPDBRider.requestFocus();

                        }
                    }
                });

        // ATPDB Rider

        cbATPDBRider
                .setOnCheckedChangeListener(new OnCheckedChangeListener() {
                    public void onCheckedChanged(CompoundButton buttonView,
                                                 boolean isChecked) {
                        if (isChecked) {
                            if (valEligibilityOfRider()) {

                                clearFocusable(cbATPDBRider);
                                trATPDBRider.setVisibility(View.VISIBLE);
                                clearFocusable(spnr_AtpdbTerm);
                                setFocusable(spnr_AtpdbTerm);
                                spnr_AtpdbTerm.requestFocus();
                            }
                        } else {
                            clearFocusable(cbATPDBRider);
                            trATPDBRider.setVisibility(View.GONE);
                            setFocusable(cbCC13NonLinkedRider);
                            cbCC13NonLinkedRider.requestFocus();
                        }
                    }
                });

        // CC13 Non linked Rider

        cbCC13NonLinkedRider
                .setOnCheckedChangeListener(new OnCheckedChangeListener() {

                    public void onCheckedChanged(CompoundButton buttonView,
                                                 boolean isChecked) {
                        if (isChecked) {
                            if (valEligibilityOfRider()) {
                                clearFocusable(cbCC13NonLinkedRider);
                                trCritiCareRider.setVisibility(View.VISIBLE);
                                clearFocusable(spnr_Cc13NonLinkedTerm);
                                setFocusable(spnr_Cc13NonLinkedTerm);
                                spnr_Cc13NonLinkedTerm.requestFocus();
                            }
                        } else {
                            clearFocusable(cbCC13NonLinkedRider);
                            trCritiCareRider.setVisibility(View.GONE);
                        }
                    }
                });

        // Staff Discount
        cb_staffdisc.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                if ((cb_staffdisc.isChecked())) {
                    cb_staffdisc.setChecked(true);

                    clearFocusable(spnr_bi_smart_money_back_gold_life_assured_title);
                    setFocusable(spnr_bi_smart_money_back_gold_life_assured_title);
                    spnr_bi_smart_money_back_gold_life_assured_title
                            .requestFocus();
                }

            }
        });

        CbJkResident.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (CbJkResident.isChecked()) {
                    CbJkResident.setChecked(true);
                } else {
                    CbJkResident.setChecked(false);
                }
            }
        });

        // Plan
        spnrPlan.setOnItemSelectedListener(new OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> arg0, View arg1, int pos,
                                       long id) {

               /* if (pos == 0) {
					spnrPolicyTerm.setSelection(getIndex(spnrPolicyTerm, "12"),
							false);
					minAge = 15;
					maxAge = 55;
                }
                else*/
                if (selPremFreq.getSelectedItem().toString().equals("Regular")) {
                    maxAge = 45;
                } else {
                    maxAge = 55;
                }
                if (pos == 0) {
                    spnrPolicyTerm.setSelection(getIndex(spnrPolicyTerm, "15"),
                            false);
                    minAge = 14;


                    if (selPremFreq.getSelectedItem().toString().equals("Single")) {
                        spnrPremPaymentTerm.setSelection(getIndex(spnrPremPaymentTerm, "1"));
                    } else if (selPremFreq.getSelectedItem().toString().equals("Limited")) {
                        spnrPremPaymentTerm.setSelection(getIndex(spnrPremPaymentTerm, "8"));
                    } else if (selPremFreq.getSelectedItem().toString().equals("Regular")) {
                        spnrPremPaymentTerm.setSelection(getIndex(spnrPremPaymentTerm, "15"));
                    }

                } else if (pos == 1) {
                    spnrPolicyTerm.setSelection(getIndex(spnrPolicyTerm, "20"),
                            false);
                    minAge = 14;

                    if (selPremFreq.getSelectedItem().toString().equals("Single")) {
                        spnrPremPaymentTerm.setSelection(getIndex(spnrPremPaymentTerm, "1"));
                    } else if (selPremFreq.getSelectedItem().toString().equals("Limited")) {
                        spnrPremPaymentTerm.setSelection(getIndex(spnrPremPaymentTerm, "10"));
                    } else if (selPremFreq.getSelectedItem().toString().equals("Regular")) {
                        spnrPremPaymentTerm.setSelection(getIndex(spnrPremPaymentTerm, "20"));
                    }

                } else if (pos == 2) {
                    spnrPolicyTerm.setSelection(getIndex(spnrPolicyTerm, "25"),
                            false);
                    minAge = 14;

                    if (selPremFreq.getSelectedItem().toString().equals("Single")) {
                        spnrPremPaymentTerm.setSelection(getIndex(spnrPremPaymentTerm, "1"));
                    } else if (selPremFreq.getSelectedItem().toString().equals("Limited")) {
                        spnrPremPaymentTerm.setSelection(getIndex(spnrPremPaymentTerm, "12"));
                    } else if (selPremFreq.getSelectedItem().toString().equals("Regular")) {
                        spnrPremPaymentTerm.setSelection(getIndex(spnrPremPaymentTerm, "25"));
                    }

                }
                clearFocusable(spnrPlan);
                // validate rider term after basic term is set
                /*if (valTermRider())
                {
					setFocusable(spnrPremPayingMode);
					spnrPremPayingMode.requestFocus();
                }*/
            }

            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        spnrPremPaymentTerm.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // PTA Term
        spnr_PtaTerm.setOnItemSelectedListener(new OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> arg0, View arg1, int pos,
                                       long id) {
                if (valTermRider()) {
                    clearFocusable(spnr_PtaTerm);
                    setFocusable(edt_PtaSA);
                    edt_PtaSA.requestFocus();
                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });

        // ADB Term
        spnr_AdbTerm.setOnItemSelectedListener(new OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> arg0, View arg1, int pos,
                                       long id) {
                if (valTermRider()) {
                    clearFocusable(spnr_AdbTerm);
                    setFocusable(edt_AdbSA);
                    edt_AdbSA.requestFocus();
                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });

        // ATPDB Term
        spnr_AtpdbTerm.setOnItemSelectedListener(new OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> arg0, View arg1, int pos,
                                       long id) {
                if (valTermRider()) {
                    clearFocusable(spnr_AtpdbTerm);
                    setFocusable(edt_AtpdbSA);
                    edt_AtpdbSA.requestFocus();
                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });

        // CC13 Non linked Rider Term
        spnr_Cc13NonLinkedTerm
                .setOnItemSelectedListener(new OnItemSelectedListener() {

                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int pos, long id) {
                        if (valTermRider()) {
                            clearFocusable(spnr_Cc13NonLinkedTerm);
                            setFocusable(edt_Cc13NonLinkedSA);
                            edt_Cc13NonLinkedSA.requestFocus();
                        }
                    }

                    public void onNothingSelected(AdapterView<?> arg0) {
                        // TODO Auto-generated method stub

                    }
                });

        selPremFreq.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                premFreqOptions = selPremFreq.getSelectedItem().toString();

                if (spnrPlan.getSelectedItem().toString().equals("Option 1")) {
                    minAge = 14;
                } else if (spnrPlan.getSelectedItem().toString().equals("Option 2")) {
                    minAge = 14;
                } else if (spnrPlan.getSelectedItem().toString().equals("Option 3")) {
                    minAge = 14;
                }

                if (position == 0) {
                    tr_premium_paying_mode.setVisibility(View.GONE);

                    spnrPremPaymentTerm.setSelection(getIndex(spnrPremPaymentTerm, "1"));
                    maxAge = 55;
                } else if (position == 1) {
                    tr_premium_paying_mode.setVisibility(View.VISIBLE);
                    maxAge = 45;

                    if (spnrPlan.getSelectedItem().toString().equals("Option 1")) {
                        spnrPremPaymentTerm.setSelection(getIndex(spnrPremPaymentTerm, "15"));
                    } else if (spnrPlan.getSelectedItem().toString().equals("Option 2")) {
                        spnrPremPaymentTerm.setSelection(getIndex(spnrPremPaymentTerm, "20"));
                    } else if (spnrPlan.getSelectedItem().toString().equals("Option 3")) {
                        spnrPremPaymentTerm.setSelection(getIndex(spnrPremPaymentTerm, "25"));
                    }
                } else if (position == 2) {
                    tr_premium_paying_mode.setVisibility(View.VISIBLE);
                    maxAge = 55;

                    if (spnrPlan.getSelectedItem().toString().equals("Option 1")) {
                        spnrPremPaymentTerm.setSelection(getIndex(spnrPremPaymentTerm, "8"));
                    } else if (spnrPlan.getSelectedItem().toString().equals("Option 2")) {
                        spnrPremPaymentTerm.setSelection(getIndex(spnrPremPaymentTerm, "10"));
                    } else if (spnrPlan.getSelectedItem().toString().equals("Option 3")) {
                        spnrPremPaymentTerm.setSelection(getIndex(spnrPremPaymentTerm, "12"));
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spnrPremPayingMode
                .setOnItemSelectedListener(new OnItemSelectedListener() {

                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int pos, long id) {

                        clearFocusable(spnrPremPayingMode);
                        if (flagFirstFocus) {
                            setFocusable(edt_bi_smart_money_back_gold_life_assured_first_name);
                            edt_bi_smart_money_back_gold_life_assured_first_name
                                    .requestFocus();
                            flagFirstFocus = false;

                        } else {

                            setFocusable(edt_SumAssured);
                            edt_SumAssured.requestFocus();
                        }
                    }

                    public void onNothingSelected(AdapterView<?> arg0) {
                    }
                });
        spnr_bi_money_back_gold_proposer_title
                .setOnItemSelectedListener(new OnItemSelectedListener() {

                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int position, long id) {
                        // TODO Auto-generated method stub
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
                            setFocusable(edt_bi_money_back_gold_proposer_first_name);

                            edt_bi_money_back_gold_proposer_first_name
                                    .requestFocus();

                        }

                    }

                    public void onNothingSelected(AdapterView<?> arg0) {
                        // TODO Auto-generated method stub

                    }
                });
        spnr_bi_smart_money_back_gold_life_assured_title
                .setOnItemSelectedListener(new OnItemSelectedListener() {

                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int position, long id) {
                        // TODO Auto-generated method stub
                        if (position == 0) {
                            lifeAssured_Title = "";
                        } else {
                            lifeAssured_Title = spnr_bi_smart_money_back_gold_life_assured_title
                                    .getSelectedItem().toString();
                           /* if (lifeAssured_Title.equalsIgnoreCase("Mr.")) {
                                gender = "Male";
                                iv_men.setImageDrawable(getResources().getDrawable(
                                        R.drawable.male_selected));
                                iv_women.setImageDrawable(getResources().getDrawable(
                                        R.drawable.female_nonselected));
							} else if (lifeAssured_Title
									.equalsIgnoreCase("Ms.")) {
								spnrGender.setSelection(
										getIndex(spnrGender, "Female"), false);
							} else if (lifeAssured_Title
									.equalsIgnoreCase("Mrs.")) {
                                gender = "Female";
                                iv_women.setImageDrawable(getResources().getDrawable(
                                        R.drawable.female_selected));
                                iv_men.setImageDrawable(getResources().getDrawable(
                                        R.drawable.male_nonselected));
                            }*/
                            clearFocusable(spnr_bi_smart_money_back_gold_life_assured_title);
                            setFocusable(edt_bi_smart_money_back_gold_life_assured_first_name);

                            edt_bi_smart_money_back_gold_life_assured_first_name
                                    .requestFocus();
                        }

                    }

                    public void onNothingSelected(AdapterView<?> arg0) {
                        // TODO Auto-generated method stub

                    }
                });

        // Back Button
        btnBack.setOnClickListener(new OnClickListener() {
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

                gender_proposer = spinnerProposerGender.getSelectedItem().toString();
                gender = spnrGender.getSelectedItem().toString();

                proposer_First_Name = edt_bi_money_back_gold_proposer_first_name
                        .getText().toString().trim();
                proposer_Middle_Name = edt_bi_money_back_gold_proposer_middle_name
                        .getText().toString().trim();
                proposer_Last_Name = edt_bi_money_back_gold_proposer_last_name
                        .getText().toString().trim();

                name_of_proposer = proposer_Title + " " + proposer_First_Name
                        + " " + proposer_Middle_Name + " " + proposer_Last_Name;

                lifeAssured_First_Name = edt_bi_smart_money_back_gold_life_assured_first_name
                        .getText().toString().trim();
                lifeAssured_Middle_Name = edt_bi_smart_money_back_gold_life_assured_middle_name
                        .getText().toString().trim();
                lifeAssured_Last_Name = edt_bi_smart_money_back_gold_life_assured_last_name
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

                // calculate effective premium
                if (valLifeAssuredProposerDetail() && valDob() && valAge()
                        && valBasicDetail() && valEligibilityOfRider()
                        && valTermRider() && valSA() && valBackdate()
                        && TrueBackdate()) {
                    Date();

                    if (proposer_Is_Same_As_Life_Assured.equalsIgnoreCase("y")) {
                        proposer_Title = "";
                        proposer_First_Name = "";
                        proposer_Middle_Name = "";
                        proposer_Last_Name = "";
                        name_of_proposer = "";
                        proposer_date_of_birth = "";
                    }
                    Log.e("ouput:", output + "");
                    boolean flg = addListenerOnSubmit();
                    Log.e("ouput:1", output + "");
                    if (flg) {
                        getInput(smartMoneyBackGoldBean);
                        System.out.println("policyterm:" + policyTerm);
                        String policyTerm = smartMoneyBackGoldBean
                                .getPolicyTerm() + "";
                        System.out.println("policyterm:" + policyTerm);
                        if (needAnalysis_flag == 0) {
                            Intent i = new Intent(
                                    BI_SmartMoneyBackGoldActivity.this,
                                    Success.class);

                            i.putExtra("ProductName",
                                    "Product : SBI Life - Smart Money Back Gold (UIN : 111N096V03)");

                            i.putExtra(
                                    "op",
                                    "Basic Premium is Rs. "
                                            + currencyFormat.format(Double.parseDouble(prsObj
                                            .parseXmlTag(
                                                    retVal.toString(),
                                                    "yearlyPrem"))));
                            i.putExtra(
                                    "op1",
                                    "Installment Premium without Applicable Taxes is Rs. "
                                            + currencyFormat.format(Double.parseDouble(prsObj
                                            .parseXmlTag(
                                                    retVal.toString(),
                                                    "premInst"))));
                            i.putExtra(
                                    "op2",
                                    "Applicable Taxes is Rs. "
                                            + currencyFormat.format(Double.parseDouble(prsObj
                                            .parseXmlTag(
                                                    retVal.toString(),
                                                    "servcTax"))));
                            i.putExtra(
                                    "op3",
                                    "Installment Premium with Applicable Taxes is Rs. "
                                            + currencyFormat.format(Double.parseDouble(prsObj
                                            .parseXmlTag(
                                                    retVal.toString(),
                                                    "premWthST"))));

                            i.putExtra(
                                    "op4",
                                    "Guaranteed Maturity Benefit is Rs. "
                                            + currencyFormat.format(Double.parseDouble(prsObj.parseXmlTag(
                                            retVal.toString(),
                                            "guaranMatBen" + policyTerm))));
                            // System.out.println("nonGuaranMatBen_4Percent:"+nonGuaranMatBen_4Percent);
                            i.putExtra(
                                    "op5",
                                    "Non-guaranteed Maturity Benefit With 4%pa is Rs. "
                                            + currencyFormat.format(Double.parseDouble(prsObj.parseXmlTag(
                                            retVal.toString(),
                                            "nonGuaranMatBen_4Percent"
                                                    + policyTerm))));
                            i.putExtra(
                                    "op6",
                                    "Non-guaranteed Maturity Benefit With 8%pa is Rs. "
                                            + currencyFormat.format(Double.parseDouble(prsObj.parseXmlTag(
                                            retVal.toString(),
                                            "nonGuaranMatBen_8Percent"
                                                    + policyTerm))));

                            if (cbPTARider.isChecked()) {
                                i.putExtra(
                                        "op7",
                                        "Preferred Term Assurance Rider Premium is Rs. "
                                                + currencyFormat.format(Double.parseDouble(prsObj
                                                .parseXmlTag(retVal
                                                                .toString(),
                                                        "premPTA"))));
                            }

                            if (cbADBRider.isChecked()) {
                                i.putExtra(
                                        "op8",
                                        "Accidental Death Benefit Rider Premium is Rs. "
                                                + currencyFormat.format(Double.parseDouble(prsObj
                                                .parseXmlTag(retVal
                                                                .toString(),
                                                        "premADB"))));
                            }

                            if (cbATPDBRider.isChecked()) {
                                i.putExtra(
                                        "op9",
                                        "Accidental Total & Permanent Disability Benefit Rider Premium is Rs. "
                                                + currencyFormat.format(Double.parseDouble(prsObj
                                                .parseXmlTag(retVal
                                                                .toString(),
                                                        "premATPDB"))));
                            }

                            if (cbCC13NonLinkedRider.isChecked()) {
                                i.putExtra(
                                        "op10",
                                        "Criti Care13 Premium is Rs. "
                                                + currencyFormat.format(Double.parseDouble(prsObj
                                                .parseXmlTag(retVal
                                                                .toString(),
                                                        "premCC13"))));
                            }

                            i.putExtra("header",
                                    "SBI Life - Smart Money Back Gold");
                            i.putExtra("header1", "(UIN : 111N096V03)");

                            startActivity(i);

                        } else
                            Dialog();
                    }
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
                    try {
                        File Photo = commonMethods.galleryAddPic(context);
                        Bitmap bmp = BitmapFactory.decodeFile(Photo.getAbsolutePath());

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
                            imageButtonSmartMoneyBackGoldProposerPhotograph
                                    .setImageBitmap(scaled);
                        } else {
                            photoBitmap = null;
                        }
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

        d.setContentView(R.layout.layout_smart_money_back_gold_bi_grid);


        TextView tv_bi_smart_money_back_gold_proposer_age = d
                .findViewById(R.id.tv_bi_smart_money_back_gold_proposer_age);

        TextView tv_premium_tag = d
                .findViewById(R.id.tv_premium_tag);

        TextView tv_proposername = d
                .findViewById(R.id.tv_smart_money_back_gold_proposername);
        TextView tv_proposal_number = d
                .findViewById(R.id.tv_smart_money_back_gold_proposal_number);

        TextView tv_life_assured_name = d
                .findViewById(R.id.tv_bi_smart_money_back_gold_life_assured_name);
        TextView tv_life_age_at_entry = d
                .findViewById(R.id.tv_bi_smart_money_back_gold_life_assured_age);

        TextView tv_life_assured_gender = d
                .findViewById(R.id.tv_bi_smart_money_back_gold_life_assured_gender);

        TextView tv_premPaymentfrequency = d
                .findViewById(R.id.tv_bi_smart_money_back_gold_premium_paying_frequency);
        TextView tv_bi_smart_money_back_gold_premium_payment_options = d
                .findViewById(R.id.tv_bi_smart_money_back_gold_premium_payment_options);

        TextView tv_bi_smart_money_back_gold_premium_payment_term = d
                .findViewById(R.id.tv_bi_smart_money_back_gold_premium_payment_term);


        TextView tv_basic_cover_plan = d
                .findViewById(R.id.tv_bi_smart_money_back_gold_plan_proposed_basic_cover);

        TextView tv_basic_cover_term = d
                .findViewById(R.id.tv_bi_smart_money_back_gold_term_basic_cover);

        TextView tv_basic_cover_premPayment_term = d
                .findViewById(R.id.tv_bi_smart_money_back_gold_premium_paying_term_basic_cover);

        TextView tv_basic_cover_sum_assured = d
                .findViewById(R.id.tv_bi_smart_money_back_gold_sum_assured_basic_cover);

        TextView tv_basic_cover_yearly_premium = d
                .findViewById(R.id.tv_bi_smart_money_back_gold_yearly_premium_basic_cover);

        TextView tv_bi_smart_money_back_gold_sa_on_death = d
                .findViewById(R.id.tv_bi_smart_money_back_gold_sa_on_death);


        TextView tv_PTRider_Term = d
                .findViewById(R.id.tv_smart_money_back_gold_pt_term);

        TextView tv_PTRider_SA = d
                .findViewById(R.id.tv_smart_money_back_gold_pt_sum_assured);

        TextView tv_PTRider_Yearly = d
                .findViewById(R.id.tv_smart_money_back_gold_pt_yearly);

        TextView tv_ADBRider_Term = d
                .findViewById(R.id.tv_smart_money_back_gold_adb_term);

        TextView tv_ADBRider_SA = d
                .findViewById(R.id.tv_smart_money_back_gold_adb_sum_assured);

        TextView tv_ADBRider_Yearly = d
                .findViewById(R.id.tv_smart_money_back_gold_adb_yearly);

        TextView tv_ATPDBRider_Term = d
                .findViewById(R.id.tv_smart_money_back_gold_atpd_term);

        TextView tv_ATPDBRider_SA = d
                .findViewById(R.id.tv_smart_money_back_gold_atpd_sum_assured);

        TextView tv_ATPDBRider_Yearly = d
                .findViewById(R.id.tv_smart_money_back_gold_atpd_yearly);

        TextView tv_CCNLRider_Term = d
                .findViewById(R.id.tv_smart_money_back_gold_cc13_term);

        TextView tv_CCNLRider_SA = d
                .findViewById(R.id.tv_smart_money_back_gold_cc13_sum_assured);

        TextView tv_CCNLRider_Yearly = d
                .findViewById(R.id.tv_smart_money_back_gold_cc13_yearly);

        TextView tv_Rider_TotalTerm = d
                .findViewById(R.id.tv_smart_money_back_gold_total_term);

        TextView tv_Rider_TotalSA = d
                .findViewById(R.id.tv_smart_money_back_gold_total_sum_assured);

        TextView tv_Rider_TotalYearly = d
                .findViewById(R.id.tv_smart_money_back_gold_total_yearly);

        TextView tv_bi_smart_money_back_gold_premium_tag_basic_cover = d
                .findViewById(R.id.tv_bi_smart_money_back_gold_premium_tag_basic_cover);

        TextView tv_bi_smart_money_back_gold_year_tag_total_premium = d
                .findViewById(R.id.tv_bi_smart_money_back_gold_year_tag_total_premium);

        TextView tv_bi_smart_money_back_gold_pemium_with_service_tag_total_premium = d
                .findViewById(R.id.tv_bi_smart_money_back_gold_pemium_with_service_tag_total_premium);

        TextView tv_bi_smart_money_back_gold_pemium_tag_total_premium = d
                .findViewById(R.id.tv_bi_smart_money_back_gold_pemium_tag_total_premium);

        TextView tv_bi_smart_money_back_gold_year_tag_rider = d
                .findViewById(R.id.tv_bi_smart_money_back_gold_year_tag_rider);

        // Second year tables

        final TextView tv_premium_install_rider_type1 = d
                .findViewById(R.id.tv_premium_install_rider_type1);
        final TextView tv_mandatory_bi_smart_money_back_gold_yearly_premium_with_tax1 = d
                .findViewById(R.id.tv_mandatory_bi_smart_money_back_gold_yearly_premium_with_tax1);

        // First year policy
        TextView tv_bi_smart_money_back_gold_basic_premium_first_year = d
                .findViewById(R.id.tv_bi_smart_money_back_gold_basic_premium_first_year);

        TextView tv_bi_smart_money_back_gold_service_tax_first_year = d
                .findViewById(R.id.tv_bi_smart_money_back_gold_service_tax_first_year);

        TextView tv_bi_smart_money_back_gold_yearly_premium_with_tax_first_year = d
                .findViewById(R.id.tv_bi_smart_money_back_gold_yearly_premium_with_tax_first_year);

       /* TextView tv_bi_smart_money_back_gold_swachh_bharat_tax_first_year = d
				.findViewById(R.id.tv_bi_smart_money_back_gold_swachh_bharat_tax_first_year);

		TextView tv_bi_smart_money_back_gold_krishi_kalyan_tax_first_year = d
				.findViewById(R.id.tv_bi_smart_money_back_gold_krishi_kalyan_tax_first_year);

		TextView tv_bi_smart_money_back_gold_total_service_tax_first_year = d
                .findViewById(R.id.tv_bi_smart_money_back_gold_total_service_tax_first_year);*/

        // Seconf year policy onwards

//        TextView tv_bi_smart_money_back_gold_second_year_heading = d
//                .findViewById(R.id.tv_bi_smart_money_back_gold_second_year_heading);

        TextView tv_bi_smart_money_back_gold_basic_premium_second_year = d
                .findViewById(R.id.tv_bi_smart_money_back_gold_basic_premium_second_year);

        TextView tv_bi_smart_money_back_gold_service_tax_second_year = d
                .findViewById(R.id.tv_bi_smart_money_back_gold_service_tax_second_year);

        TextView tv_bi_smart_money_back_gold_yearly_premium_with_tax_second_year = d
                .findViewById(R.id.tv_bi_smart_money_back_gold_yearly_premium_with_tax_second_year);

        TextView tv_bi_smart_money_back_gold_yearly_premium = d
                .findViewById(R.id.tv_bi_smart_money_back_gold_yearly_premium);

        TextView tv_bi_smart_money_back_gold_yearly_premium2 = d
                .findViewById(R.id.tv_bi_smart_money_back_gold_yearly_premium2);


        TableRow tr_second_year = d
                .findViewById(R.id.tr_second_year);

        TableRow trADBRider = d
                .findViewById(R.id.tr_bi_smart_money_back_gold_adb_rider);
        TableRow trPTRider = d
                .findViewById(R.id.tr_bi_smart_money_back_gold_pt_rider);
        TableRow trATPDBRider = d
                .findViewById(R.id.tr_bi_smart_money_back_gold_atpdb_rider);
        TableRow trCCNLRider = d
                .findViewById(R.id.tr_bi_smart_money_back_gold_ccnl_rider);

        View viewADBRider = d
                .findViewById(R.id.view_bi_smart_money_back_gold_adb_rider);
        View viewPTRider = d
                .findViewById(R.id.view_bi_smart_money_back_gold_pt_rider);
        View viewATPDBRider = d
                .findViewById(R.id.view_bi_smart_money_back_gold_atpdb_rider);
        View viewCCNLRider = d
                .findViewById(R.id.view_bi_smart_money_back_gold_ccnl_rider);

        LinearLayout llRider = d
                .findViewById(R.id.ll_bi_smart_money_back_gold_rider);

        TextView tv_total_premium_yearly_installment_plan = d
                .findViewById(R.id.tv_smart_money_back_gold_total_premium_yearly_premium_plan_name);

        TextView tv_total_premium_yearly_installment_yearly = d
                .findViewById(R.id.tv_smart_money_back_gold_total_premium_yearly_premium_yearly);

        TextView tv_total_premium_service_tax_plan = d
                .findViewById(R.id.tv_smart_money_back_gold_total_premium_service_tax_plan_name);

        TextView tv_total_premium_service_tax_yearly = d
                .findViewById(R.id.tv_smart_money_back_gold_total_premium_service_tax_yearly);

        TextView tv_total_premium_yearly_installment_with_tax_plan = d
                .findViewById(R.id.tv_smart_money_back_gold_total_premium_yearly_premium_with_tax_plan_name);

        TextView tv_total_premium_yearly_installment_with_tax_yearly = d
                .findViewById(R.id.tv_smart_money_back_gold_total_premium_yearly_premium_with_tax_yearly);

        GridView gv_userinfo = d
                .findViewById(R.id.gv_smart_money_back_gold_userinfo);

        TextView tv_staff_or_not = d
                .findViewById(R.id.tv_bi_smart_money_back_gold_staff_or_not);

        TextView tv_bi_is_JK = d.findViewById(R.id.tv_bi_is_JK);

        TextView tv_bi_smart_money_back_gold_basic_service_tax_first_year = d
                .findViewById(R.id.tv_bi_smart_money_back_gold_basic_service_tax_first_year);
        TextView tv_bi_smart_money_back_gold_swachh_bharat_cess_first_year = d
                .findViewById(R.id.tv_bi_smart_money_back_gold_swachh_bharat_cess_first_year);
        TextView tv_bi_smart_money_back_gold_krishi_kalyan_cess_first_year = d
                .findViewById(R.id.tv_bi_smart_money_back_gold_krishi_kalyan_cess_first_year);

        TextView tv_bi_smart_money_back_gold_basic_service_tax_second_year = d
                .findViewById(R.id.tv_bi_smart_money_back_gold_basic_service_tax_second_year);
        TextView tv_bi_smart_money_back_gold_swachh_bharat_cess_second_year = d
                .findViewById(R.id.tv_bi_smart_money_back_gold_swachh_bharat_cess_second_year);
        TextView tv_bi_smart_money_back_gold_krishi_kalyan_cess_second_year = d
                .findViewById(R.id.tv_bi_smart_money_back_gold_krishi_kalyan_cess_second_year);
        /*TextView tv_uin_smart_money_back_gold = (TextView) d
                .findViewById(R.id.tv_uin_smart_money_back_gold);*/

        TextView tv_backdatingint = d
                .findViewById(R.id.tv_backdatingint);
        TextView applicable_tax_rate = d
                .findViewById(R.id.applicable_tax_rate);


        gv_userinfo.setVerticalScrollBarEnabled(true);
        gv_userinfo.setSmoothScrollbarEnabled(true);

        final EditText edt_Policyholderplace = d
                .findViewById(R.id.edt_Policyholderplace);

        final TextView edt_proposer_name = d
                .findViewById(R.id.edt_ProposalName);
        final CheckBox cb_statement = d
                .findViewById(R.id.cb_statement);
        final TextView edt_agent_name_e_sign = d
                .findViewById(R.id.edt_agent_name_e_sign);

        /* Need Analysis */
        final TextView edt_proposer_name_need_analysis = d
                .findViewById(R.id.edt_proposer_name_need_analysis);

        final CheckBox cb_statement_need_analysis = d
                .findViewById(R.id.cb_statement_need_analysis);
        cb_statement_need_analysis.setChecked(true);
        TableRow tr_need_analysis = d
                .findViewById(R.id.tr_need_analysis);

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
            edt_proposer_name
                    .setText("I, "
                            + name_of_life_assured
                            + ", having received the information with respect to the above, have understood the above statement before entering into the contract.");
            edt_proposer_name_need_analysis
                    .setText("I, "
                            + name_of_life_assured
                            + ", have undergone the Need Analysis  after having reviewed the SBI Life Product options, I have opted for SBI LIFE - Smart Money Back Gold.");

            tv_life_assured_name.setText(name_of_life_assured);
            tv_proposername.setText(name_of_life_assured);
        } else if (proposer_Is_Same_As_Life_Assured.equalsIgnoreCase("n")) {
            edt_proposer_name
                    .setText("I, "
                            + name_of_proposer
                            + ", having received the information with respect to the above, have understood the above statement before entering into the contract.");
            edt_proposer_name_need_analysis
                    .setText("I, "
                            + name_of_proposer
                            + ", have undergone the Need Analysis  after having reviewed the SBI Life Product options, I have opted for SBI LIFE - Smart Money Back Gold.");

            tv_proposername.setText(name_of_proposer);
            tv_life_assured_name.setText(name_of_life_assured);
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

        edt_agent_name_e_sign.setText("I, "
                + commonMethods.getUserName(context)
                + ", have explained the premiums and benefits under the product fully to the prospect/policyholder.	");

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

        imageButtonSmartMoneyBackGoldProposerPhotograph = d
                .findViewById(R.id.imageButtonSmartMoneyBackGoldProposerPhotograph);
        imageButtonSmartMoneyBackGoldProposerPhotograph
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
                                && cb_statement_need_analysis.isChecked()) {
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
                                && cb_statement_need_analysis.isChecked()) {
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

                            /*
                             * String extStorageDirectory = Environment
                             * .getExternalStorageDirectory().toString(); String
                             * direct = "/SBI-Smart Advisor";
                             */
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
            imageButtonSmartMoneyBackGoldProposerPhotograph
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
                            imageButtonSmartMoneyBackGoldProposerPhotograph
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

            public void onClick(View v) {
                // if (frmProductHome.equals("FALSE")) {
                name_of_person = edt_proposer_name.getText().toString();
                // place1 = edt_MarketingOfficalPlace.getText().toString();
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
                    String productCode = "";
                    switch (smartMoneyBackGoldBean.getPlanName()) {
                        case "Option 1":
                            productCode = "GSMBP1";
                            break;
                        case "Option 2":
                            productCode = "GSMBP2";
                            break;
                        case "Option 3":
                            productCode = "GSMBP3";
                            break;
                        case "Option 4":
                            productCode = "GSMBP4";
                            break;
                    }

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
                                    .valueOf((sumAssured.equals("") || sumAssured == null) ? "0"
                                            : sumAssured))),
                            commonForAllProd
                                    .getRound(totalPremiumYearlyInstallmentPremWithServiceTaxYearly),
                            emailId, mobileNo, agentEmail, agentMobile,
                            na_input, na_output, premPayingMode, Integer
                            .parseInt(policyTerm), 0, productCode,
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
                            lifeAssured_Title,
                            lifeAssured_First_Name,
                            lifeAssured_Middle_Name,
                            lifeAssured_Last_Name,
                            commonForAllProd.getRound(commonForAllProd.getStringWithout_E(Double
                                    .valueOf((sumAssured.equals("") || sumAssured == null) ? "0"
                                            : sumAssured))),
                            commonForAllProd
                                    .getRound(totalPremiumYearlyInstallmentPremWithServiceTaxYearly),
                            agentEmail, agentMobile, na_input, na_output,
                            premPayingMode, Integer.parseInt(policyTerm), 0,
                            productCode, getDate(lifeAssured_date_of_birth),
                            "", "", mode, inputVal
                            .toString(), retVal.toString().replace(
                            bussIll.toString(), "")));

                    createPdf();
                    NABIObj.serviceHit(BI_SmartMoneyBackGoldActivity.this,
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
                    } else if (!radioButtonTrasactionModeParivartan.isChecked()
                            && !radioButtonTrasactionModeManual.isChecked()) {
                        commonMethods.dialogWarning(context, "Please Select Transaction Mode", true);
                        setFocusable(linearlayoutTrasactionModeParivartan);
                        linearlayoutTrasactionModeParivartan.requestFocus();
                    } else if (photoBitmap == null) {
                        commonMethods.dialogWarning(context, "Please Capture the Photo", true);
                        setFocusable(imageButtonSmartMoneyBackGoldProposerPhotograph);
                        imageButtonSmartMoneyBackGoldProposerPhotograph
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
        ageAtEntry = prsObj.parseXmlTag(input, "age");
        tv_life_age_at_entry.setText(ageAtEntry + " Years");


        if (proposer_Is_Same_As_Life_Assured.equalsIgnoreCase("y")) {
            tv_bi_smart_money_back_gold_proposer_age.setText(ageAtEntry + " Years");
        } else if (proposer_Is_Same_As_Life_Assured.equalsIgnoreCase("n")) {
            tv_bi_smart_money_back_gold_proposer_age.setText(strProposerAge + " Years");
        }


        gender = prsObj.parseXmlTag(input, "gender");
        tv_life_assured_gender.setText(gender);

        premFreqOptions = prsObj.parseXmlTag(input, "premFreqOptions");
        tv_bi_smart_money_back_gold_premium_payment_options.setText(premFreqOptions);

        PremPaymentTerm = prsObj.parseXmlTag(input, "PremPaymentTerm");
        tv_bi_smart_money_back_gold_premium_payment_term.setText(PremPaymentTerm);

        premPayingMode = prsObj.parseXmlTag(input, "premFreq");
        tv_premPaymentfrequency.setText(premPayingMode);

        if (premFreqOptions.equalsIgnoreCase("Regular") || premFreqOptions.equalsIgnoreCase("Limited")) {

            if (premPayingMode.equalsIgnoreCase("Half Yearly")) {
               /* tv_bi_smart_money_back_gold_premium_tag_basic_cover
                        .setText("Premium Half Yearly");*/
                tv_bi_smart_money_back_gold_year_tag_total_premium
                        .setText("Half Yearly");
                tv_bi_smart_money_back_gold_pemium_with_service_tag_total_premium
                        .setText("Half Yearly Installment Premium with Applicable Taxes");
                tv_bi_smart_money_back_gold_pemium_tag_total_premium
                        .setText("Half Yearly Installment Premium");
                tv_bi_smart_money_back_gold_year_tag_rider.setText("Half Yearly" + "(Rs)");
                // tv_premium_install_rider_type1.setText("Half Yearly Premium (Rs.)");
               /* tv_mandatory_bi_smart_money_back_gold_yearly_premium_with_tax1
                        .setText("Half Yearly Premium with Applicable Taxes(Rs.)");*/
            } else if (premPayingMode.equalsIgnoreCase("Quarterly")) {
               /* tv_bi_smart_money_back_gold_premium_tag_basic_cover
                        .setText("Premium Quarterly");*/
                tv_bi_smart_money_back_gold_year_tag_total_premium
                        .setText("Quarterly");
                tv_bi_smart_money_back_gold_pemium_with_service_tag_total_premium
                        .setText("Quarterly Installment Premium with Applicable Taxes");
                tv_bi_smart_money_back_gold_pemium_tag_total_premium
                        .setText("Quarterly Installment Premium");
                tv_bi_smart_money_back_gold_year_tag_rider.setText("Quarterly" + "(Rs)");
                //tv_premium_install_rider_type1.setText("Quarterly Premium (Rs.)");
               /* tv_mandatory_bi_smart_money_back_gold_yearly_premium_with_tax1
                        .setText("Quarterly Premium with Applicable Taxes(Rs.)");*/
            } else if (premPayingMode.equalsIgnoreCase("Monthly")) {
               /* tv_bi_smart_money_back_gold_premium_tag_basic_cover
                        .setText("Premium Monthly");*/
                tv_bi_smart_money_back_gold_year_tag_total_premium
                        .setText("Monthly");
                tv_bi_smart_money_back_gold_pemium_with_service_tag_total_premium
                        .setText("Monthly Installment Premium with Applicable Taxes");
                tv_bi_smart_money_back_gold_pemium_tag_total_premium
                        .setText("Monthly Installment Premium");
                tv_bi_smart_money_back_gold_year_tag_rider.setText("Monthly" + "(Rs)");
                // tv_premium_install_rider_type1.setText("Monthly Premium (Rs.)");
              /*  tv_mandatory_bi_smart_money_back_gold_yearly_premium_with_tax1
                        .setText("Monthly Premium with Applicable Taxes(Rs.)");*/
            }
        } else {
           /* tv_bi_smart_money_back_gold_premium_tag_basic_cover
                    .setText("Premium Single");*/
            tv_bi_smart_money_back_gold_year_tag_total_premium
                    .setText("Single");
            tv_bi_smart_money_back_gold_pemium_with_service_tag_total_premium
                    .setText("Single Premium with Applicable Taxes");
            tv_bi_smart_money_back_gold_pemium_tag_total_premium
                    .setText("Single Premium");
            tv_bi_smart_money_back_gold_year_tag_rider.setText("Single" + "(Rs)");
            // tv_premium_install_rider_type1.setText("Single Premium (Rs.)");
            /*tv_mandatory_bi_smart_money_back_gold_yearly_premium_with_tax1
                    .setText("Single Premium with Applicable Taxes(Rs.)");*/
        }

        String isJkResident = prsObj.parseXmlTag(input, "isJKResident");
        ptRiderStatus = prsObj.parseXmlTag(input, "isPTRider");
        adbRiderStatus = prsObj.parseXmlTag(input, "isADBRider");
        atpdbRiderStatus = prsObj.parseXmlTag(input, "isATPDBRider");
        ccnlRiderStatus = prsObj.parseXmlTag(input, "isCCNLRider");

        String staffdiscount = prsObj.parseXmlTag(input, "isStaff");
        if (staffdiscount.equals("true")) {
            tv_staff_or_not.setText("Staff");
        } else {
            tv_staff_or_not.setText("Non - Staff");
        }

        isJkResident = prsObj.parseXmlTag(input, "isJKResident");

        if (isJkResident.equalsIgnoreCase("true")) {
            tv_bi_is_JK.setText("Yes");
        } else {
            tv_bi_is_JK.setText("No");
        }

        str_kerla_discount = prsObj.parseXmlTag(input, "str_kerla_discount");

        if (str_kerla_discount.equalsIgnoreCase("Yes")) {
            if (selPremFreq.getSelectedItem().toString().equals("Single")) {
                applicable_tax_rate.setText("4.75%");
            } else {
                applicable_tax_rate.setText("1st policy year : 4.75% ; 2nd policy year onwards : 2.375%");
            }
        } else {
            if (selPremFreq.getSelectedItem().toString().equals("Single")) {
                applicable_tax_rate.setText("4.50%");
            } else {
                applicable_tax_rate.setText("1st policy year : 4.5% ; 2nd policy year onwards : 2.25%");
            }
        }

        policyTerm = prsObj.parseXmlTag(input, "policyTerm");
        tv_basic_cover_term.setText(policyTerm + " Years");
        tv_basic_cover_premPayment_term.setText(PremPaymentTerm + " Years");


        sumAssured = prsObj.parseXmlTag(input, "sumAssured");

        planProposedName = prsObj.parseXmlTag(input, "plan");
        // tv_basic_cover_plan.setText(planProposedName);
        if (planProposedName.equalsIgnoreCase("Option 1")) {
            tv_basic_cover_plan.setText("Option 1");
            tv_total_premium_yearly_installment_plan.setText("Plan 1");
            tv_total_premium_service_tax_plan.setText("Plan 1");
            tv_total_premium_yearly_installment_with_tax_plan.setText("Plan 1");
        } else if (planProposedName.equalsIgnoreCase("Option 2")) {
            tv_basic_cover_plan.setText("Option 2");
            tv_total_premium_yearly_installment_plan.setText("Plan 2");
            tv_total_premium_service_tax_plan.setText("Plan 2");
            tv_total_premium_yearly_installment_with_tax_plan.setText("Plan 2");
        } else if (planProposedName.equalsIgnoreCase("Option 3")) {
            tv_basic_cover_plan.setText("Option 3");
            tv_total_premium_yearly_installment_plan.setText("Plan 3");
            tv_total_premium_service_tax_plan.setText("Plan 3");
            tv_total_premium_yearly_installment_with_tax_plan.setText("Plan 3");
        } /*else if (planProposedName.equalsIgnoreCase("Option 4")) {
			tv_basic_cover_plan.setText("Option 4");
			tv_total_premium_yearly_installment_plan.setText("Plan 4");
			tv_total_premium_service_tax_plan.setText("Plan 4");
			tv_total_premium_yearly_installment_with_tax_plan.setText("Plan 4");
        }*/

        if (ptRiderStatus.equalsIgnoreCase("true")
                || adbRiderStatus.equalsIgnoreCase("true")
                || atpdbRiderStatus.equalsIgnoreCase("true")
                || ccnlRiderStatus.equalsIgnoreCase("true")) {
            llRider.setVisibility(View.VISIBLE);

            if (ptRiderStatus.equalsIgnoreCase("true")) {
                trPTRider.setVisibility(View.VISIBLE);
                viewPTRider.setVisibility(View.VISIBLE);
                ptRiderYearly = commonForAllProd.getRound(commonForAllProd.getStringWithout_E(Double.valueOf(prsObj.parseXmlTag(
                        output, "premPTA")))) + "";
                ptRiderTerm = prsObj.parseXmlTag(input, "ptTerm");
                ptSA = ((int) Double.parseDouble(prsObj.parseXmlTag(input,
                        "ptSA"))) + "";
                tv_PTRider_Term.setText(ptRiderTerm);
                tv_PTRider_SA.setText(getformatedThousandString(Integer.parseInt(ptSA)));
                tv_PTRider_Yearly.setText(getformatedThousandString(Integer.parseInt(ptRiderYearly)));
            }

            if (adbRiderStatus.equalsIgnoreCase("true")) {
                trADBRider.setVisibility(View.VISIBLE);
                viewADBRider.setVisibility(View.VISIBLE);
                adbRiderYearly = commonForAllProd.getRound(commonForAllProd.getStringWithout_E(Double.valueOf(prsObj.parseXmlTag(
                        output, "premADB")))) + "";
                adbRiderTerm = prsObj.parseXmlTag(input, "adbTerm");
                adbSA = ((int) Double.parseDouble(prsObj.parseXmlTag(input,
                        "adbSA"))) + "";
                tv_ADBRider_Term.setText(adbRiderTerm);
                tv_ADBRider_SA.setText(getformatedThousandString(Integer.parseInt(adbSA)));
                tv_ADBRider_Yearly.setText(getformatedThousandString(Integer.parseInt(adbRiderYearly)));

            }

            if (atpdbRiderStatus.equalsIgnoreCase("true")) {
                trATPDBRider.setVisibility(View.VISIBLE);
                viewATPDBRider.setVisibility(View.VISIBLE);
                atpdbRiderYearly = commonForAllProd.getRound(commonForAllProd.getStringWithout_E(Double.valueOf(prsObj
                        .parseXmlTag(output, "premATPDB")))) + "";
                atpdbRiderTerm = prsObj.parseXmlTag(input, "atpdbTerm");
                atpdbSA = ((int) Double.parseDouble(prsObj.parseXmlTag(input,
                        "atpdbSA"))) + "";

                tv_ATPDBRider_Term.setText(atpdbRiderTerm);
                tv_ATPDBRider_SA.setText(getformatedThousandString(Integer.parseInt(atpdbSA)));
                tv_ATPDBRider_Yearly.setText(getformatedThousandString(Integer.parseInt(atpdbRiderYearly)));

            }

            if (ccnlRiderStatus.equalsIgnoreCase("true")) {
                trCCNLRider.setVisibility(View.VISIBLE);
                viewCCNLRider.setVisibility(View.VISIBLE);
                ccnlRiderYearly = ((int) Double.parseDouble(prsObj.parseXmlTag(
                        output, "premCC13"))) + "";
                ccnlRiderTerm = prsObj.parseXmlTag(input, "ccnlTerm");
                ccnlSA = ((int) Double.parseDouble(prsObj.parseXmlTag(input,
                        "ccnlSA"))) + "";
                tv_CCNLRider_Term.setText(ccnlRiderTerm);
                tv_CCNLRider_SA.setText(getformatedThousandString(Integer.parseInt(ccnlSA)));
                tv_CCNLRider_Yearly.setText(getformatedThousandString(Integer.parseInt(ccnlRiderYearly)));

            }
            totalRiderYearly = commonForAllProd.getRound(commonForAllProd.getStringWithout_E(Double.valueOf(prsObj.parseXmlTag(
                    output, "sumOfRiders")))) + "";
            tv_Rider_TotalYearly.setText(totalRiderYearly);

        } else {
            llRider.setVisibility(View.GONE);
        }

        basicCoverYearlyPremium = ((int) Double.parseDouble(prsObj.parseXmlTag(
                output, "yearlyPrem"))) + "";
        tv_basic_cover_yearly_premium.setText("Rs. " + getformatedThousandString(Integer
                .parseInt(basicCoverYearlyPremium)));

        guaranteed_death_benefit_str = ((int) Double.parseDouble(prsObj
                .parseXmlTag(output, "guarntdDeathBenft" + policyTerm + ""))) + "";

        tv_bi_smart_money_back_gold_sa_on_death.setText("Rs. " + getformatedThousandString(Integer
                .parseInt(guaranteed_death_benefit_str)));

        String basicCoverSumAssured = (int) Double.parseDouble(sumAssured) + "";
        tv_basic_cover_sum_assured.setText("Rs. "
                + getformatedThousandString(Integer
                .parseInt(basicCoverSumAssured)));

        totalPremiumYearlyInstallmentPremYearly = ((int) Double
                .parseDouble(prsObj.parseXmlTag(output, "premInst"))) + "";

        String totalPremiumServiceTaxYearly = ((int) Double.parseDouble(prsObj
                .parseXmlTag(output, "servcTax"))) + "";

        totalPremiumYearlyInstallmentPremWithServiceTaxYearly = ((int) Double
                .parseDouble(prsObj.parseXmlTag(output, "premWthST"))) + "";

        tv_total_premium_yearly_installment_yearly
                .setText(getformatedThousandString(Integer.parseInt(totalPremiumYearlyInstallmentPremYearly)));
        tv_total_premium_service_tax_yearly
                .setText(getformatedThousandString(Integer.parseInt(totalPremiumServiceTaxYearly)));
        tv_total_premium_yearly_installment_with_tax_yearly
                .setText(getformatedThousandString(Integer.parseInt(totalPremiumYearlyInstallmentPremWithServiceTaxYearly)));


        tv_bi_smart_money_back_gold_yearly_premium.setText(getformatedThousandString(Integer.parseInt(totalPremiumYearlyInstallmentPremYearly)));
        tv_bi_smart_money_back_gold_yearly_premium2.setText(getformatedThousandString(Integer.parseInt(totalPremiumYearlyInstallmentPremYearly)));

        // Second year onwards

        String servcTaxSecondYear = prsObj.parseXmlTag(output, "servcTaxSecondYear");
        premWthSTSecondYear = prsObj.parseXmlTag(output, "premWthSTSecondYear");

        tv_bi_smart_money_back_gold_basic_premium_first_year
                .setText(getformatedThousandString(Integer.parseInt(totalPremiumYearlyInstallmentPremWithServiceTaxYearly)));

        tv_bi_smart_money_back_gold_service_tax_first_year
                .setText("0");

        tv_bi_smart_money_back_gold_yearly_premium_with_tax_first_year
                .setText(getformatedThousandString(Integer.parseInt(totalPremiumYearlyInstallmentPremWithServiceTaxYearly)));







       /* tv_bi_smart_money_back_gold_basic_premium_first_year
                .setText(getformatedThousandString(Integer.parseInt(totalPremiumYearlyInstallmentPremYearly)));*/
        /*if (selPremFreq.getSelectedItem().toString().equals("Single"))
        {
            tv_bi_smart_money_back_gold_basic_premium_first_year
                    .setText("NA");
       *//* tv_bi_smart_money_back_gold_service_tax_first_year
                .setText(getformatedThousandString(Integer.parseInt(totalPremiumServiceTaxYearly)));*//*
            tv_bi_smart_money_back_gold_service_tax_first_year
                    .setText("NA");

            tv_bi_smart_money_back_gold_yearly_premium_with_tax_first_year
                    .setText("NA");
        } else {
        tv_bi_smart_money_back_gold_basic_premium_first_year
                .setText(getformatedThousandString(Integer.parseInt(totalPremiumYearlyInstallmentPremWithServiceTaxYearly)));

       *//* tv_bi_smart_money_back_gold_service_tax_first_year
                .setText(getformatedThousandString(Integer.parseInt(totalPremiumServiceTaxYearly)));*//*
        tv_bi_smart_money_back_gold_service_tax_first_year
                .setText("0");

        tv_bi_smart_money_back_gold_yearly_premium_with_tax_first_year
                .setText(getformatedThousandString(Integer.parseInt(totalPremiumYearlyInstallmentPremWithServiceTaxYearly)));

        }*/


        // Amit changes start- 23-5-2016
        // basicServiceTax = prsObj.parseXmlTag(output, "basicServiceTax");
        String basicServiceTax = prsObj.parseXmlTag(output, "servcTax");

        tv_bi_smart_money_back_gold_basic_service_tax_first_year.setText("Rs. "
                + commonForAllProd.getRound(commonForAllProd
                .getStringWithout_E(Double.valueOf(basicServiceTax
                        .equals("") ? "0" : basicServiceTax))));

        String SBCServiceTax = prsObj.parseXmlTag(output, "SBCServiceTax");

        tv_bi_smart_money_back_gold_swachh_bharat_cess_first_year.setText(""
                + commonForAllProd.getRound(commonForAllProd
                .getStringWithout_E(Double.valueOf(SBCServiceTax
                        .equals("") ? "0" : SBCServiceTax))));

        String KKCServiceTax = prsObj.parseXmlTag(output, "KKCServiceTax");

        tv_bi_smart_money_back_gold_krishi_kalyan_cess_first_year.setText(""
                + commonForAllProd.getRound(commonForAllProd
                .getStringWithout_E(Double.valueOf(KKCServiceTax
                        .equals("") ? "0" : KKCServiceTax))));
        // Amit changes end- 23-5-2016

        // tr_second_year.setVisibility(View.GONE);

        if (!smartMoneyBackGoldBean.getPremiumFreq().equalsIgnoreCase("Single")) {
            // tr_second_year.setVisibility(View.VISIBLE);
            tv_bi_smart_money_back_gold_basic_premium_second_year
                    .setText(""
                            + getformatedThousandString(Integer.parseInt(commonForAllProd.getRound(commonForAllProd
                            .getStringWithout_E(Double
                                    .valueOf(premWthSTSecondYear
                                            .equals("") ? "0"
                                            : premWthSTSecondYear))))));

          /*  tv_bi_smart_money_back_gold_service_tax_second_year
                    .setText(""
                            + getformatedThousandString(Integer.parseInt(commonForAllProd.getRound(commonForAllProd
                            .getStringWithout_E(Double
                                    .valueOf(servcTaxSecondYear
                                            .equals("") ? "0"
                                            : servcTaxSecondYear))))));*/
            tv_bi_smart_money_back_gold_service_tax_second_year
                    .setText("0");

            tv_bi_smart_money_back_gold_yearly_premium_with_tax_second_year
                    .setText(""
                            + getformatedThousandString(Integer.parseInt(commonForAllProd.getRound(commonForAllProd
                            .getStringWithout_E(Double
                                    .valueOf(premWthSTSecondYear
                                            .equals("") ? "0"
                                            : premWthSTSecondYear))))));

            // Amit changes start- 23-5-2016
            // basicServiceTaxSecondYear = prsObj.parseXmlTag(output,
            // "basicServiceTaxSecondYear");
            String basicServiceTaxSecondYear = prsObj.parseXmlTag(output,
                    "servcTaxSecondYear");

            tv_bi_smart_money_back_gold_basic_service_tax_second_year
                    .setText(""
                            + getformatedThousandString(Integer.parseInt(commonForAllProd.getRound(commonForAllProd.getStringWithout_E(Double
                            .valueOf(basicServiceTaxSecondYear
                                    .equals("") ? "0"
                                    : basicServiceTaxSecondYear))))));

            String SBCServiceTaxSecondYear = prsObj.parseXmlTag(output,
                    "SBCServiceTaxSecondYear");

            tv_bi_smart_money_back_gold_swachh_bharat_cess_second_year
                    .setText(""
                            + commonForAllProd.getRound(commonForAllProd
                            .getStringWithout_E(Double
                                    .valueOf(SBCServiceTaxSecondYear
                                            .equals("") ? "0"
                                            : SBCServiceTaxSecondYear))));

            String KKCServiceTaxSecondYear = prsObj.parseXmlTag(output,
                    "KKCServiceTaxSecondYear");

            tv_bi_smart_money_back_gold_krishi_kalyan_cess_second_year
                    .setText(""
                            + commonForAllProd.getRound(commonForAllProd
                            .getStringWithout_E(Double
                                    .valueOf(KKCServiceTaxSecondYear
                                            .equals("") ? "0"
                                            : KKCServiceTaxSecondYear))));
            // Amit changes end- 23-5-2016

        }


        if (selPremFreq.getSelectedItem().toString().equals("Single")) {
            tv_bi_smart_money_back_gold_basic_premium_second_year.setText("Not Applicable");
            tv_bi_smart_money_back_gold_service_tax_second_year.setText("Not Applicable");
            tv_bi_smart_money_back_gold_yearly_premium_with_tax_second_year.setText("Not Applicable");
        }

        BackdatingInt = prsObj.parseXmlTag(output, "backdateInt");
        tv_backdatingint.setText("Rs. "
                + getformatedThousandString(Integer.parseInt(commonForAllProd.getRound(commonForAllProd
                .getStringWithout_E(Double.valueOf(BackdatingInt
                        .equals("") ? "0" : BackdatingInt))))));

        TextView tv_Company_policy_surrender_dec = d
                .findViewById(R.id.tv_Company_policy_surrender_dec);

        String str_prem_freq = "";
        if (premPayingMode.equalsIgnoreCase("Single")) {
            str_prem_freq = "Single";
            tv_premium_tag.setText("Single Premium");
        } else {
            tv_premium_tag.setText("Annualized Premium");
            str_prem_freq = "Regular";
        }

        Company_policy_surrender_dec = "Your SBI Life Smart Money Back Gold (UIN - 111N096V03) is a "
                + str_prem_freq
                + " premium policy, for which your first year " + premPayingMode + " Premium is Rs "
                + currencyFormat.format(Double
                .parseDouble(totalPremiumYearlyInstallmentPremWithServiceTaxYearly))
                + " .Your Policy Term is "
                + policyTerm
                + " years"
                + " , Premium Payment Term is "
                + policyTerm
                + " years"
                + " and Basic Sum Assured is Rs. "
                +

                getformatedThousandString(Integer
                        .parseInt(basicCoverSumAssured));

        tv_Company_policy_surrender_dec.setText(Company_policy_surrender_dec);


        for (int i = 1; i <= Integer.parseInt(policyTerm); i++) {

            String end_of_year = prsObj
                    .parseXmlTag(output, "policyYr" + i + "");
            String total_base_premium = ((int) Double.parseDouble(prsObj
                    .parseXmlTag(output, "AnnulizedPremium" + i + ""))) + "";


            String GuarateedAddition = ((int) Double.parseDouble(prsObj
                    .parseXmlTag(output, "GuarateedAddition" + i + ""))) + "";


            String guaranteed_survival_benefit = ((int) Double
                    .parseDouble(prsObj.parseXmlTag(output, "getSurvivalBenefit" + i
                            + "")))
                    + "";

            String guaranteed_surrender_value = ((int) Double
                    .parseDouble(prsObj.parseXmlTag(output, "GSV_surrendr_val"
                            + i + "")))
                    + "";


            String guarntdDeathBenft = ((int) Double.parseDouble(prsObj
                    .parseXmlTag(output, "guarntdDeathBenft" + i + ""))) + "";


            String MaturityBenefit = ((int) Double
                    .parseDouble(prsObj.parseXmlTag(output, "MaturityBenefit" + i
                            + "")))
                    + "";


            String ReversionaryBonus4Per = ((int) Double
                    .parseDouble(prsObj.parseXmlTag(output,
                            "ReversionaryBonus4Per" + i + "")))
                    + "";

            String not_guaranteed_surrender_value_4per = ((int) Double
                    .parseDouble(prsObj.parseXmlTag(output,
                            "NonGSV_surrndr_val_4Percent" + i + "")))
                    + "";

            String ReversionaryBonus8Per = ((int) Double
                    .parseDouble(prsObj.parseXmlTag(output,
                            "ReversionaryBonus8Per" + i + "")))
                    + "";

            String not_guaranteed_surrender_value_8per = ((int) Double
                    .parseDouble(prsObj.parseXmlTag(output,
                            "NonGSV_surrndr_val_8Percent" + i + "")))
                    + "";


            String TotalMaturityBenefit4per = ((int) Double
                    .parseDouble(prsObj.parseXmlTag(output,
                            "TotalMaturityBenefit4per" + i + "")))
                    + "";

            String TotalMaturityBenefit8per = ((int) Double
                    .parseDouble(prsObj.parseXmlTag(output,
                            "TotalMaturityBenefit8per" + i + "")))
                    + "";

            String TotalDeathBenefit4per = ((int) Double
                    .parseDouble(prsObj.parseXmlTag(output,
                            "TotalDeathBenefit4per" + i + "")))
                    + "";

            String TotalDeathBenefit8per = ((int) Double
                    .parseDouble(prsObj.parseXmlTag(output,
                            "TotalDeathBenefit8per" + i + "")))
                    + "";

            list_data.add(new M_BI_SmartMoneyBackGold_Adapter(end_of_year,
                    total_base_premium,
                    GuarateedAddition,
                    guaranteed_survival_benefit,
                    guaranteed_surrender_value,
                    guarntdDeathBenft,
                    MaturityBenefit,
                    ReversionaryBonus4Per,
                    "0",
                    not_guaranteed_surrender_value_4per,
                    ReversionaryBonus8Per,
                    "0",
                    not_guaranteed_surrender_value_8per,
                    TotalMaturityBenefit4per,
                    TotalMaturityBenefit8per,
                    TotalDeathBenefit4per,
                    TotalDeathBenefit8per));
        }

        Adapter_BI_SmartMoneyBackGoldGrid adapter = new Adapter_BI_SmartMoneyBackGoldGrid(
                this, list_data);
        gv_userinfo.setAdapter(adapter);
        GridHeight gh = new GridHeight();
        gh.getheight(gv_userinfo, policyTerm);

       /* LinearLayout ll_signature = d
                .findViewById(R.id.ll_signature);*/
        /*TableRow tbrw_quotationNo = d
				.findViewById(R.id.tbrw_quotationNo);
		if (needAnalysis_flag == 1) {
			ll_signature.setVisibility(View.VISIBLE);
			tbrw_quotationNo.setVisibility(View.VISIBLE);

		} else {
			ll_signature.setVisibility(View.GONE);
			tbrw_quotationNo.setVisibility(View.GONE);
        }*/
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

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.ENGLISH);
        String formattedDate = df.format(c.getTime());
        // formattedDate have current date/time
        // Toast.makeText(this, formattedDate, Toast.LENGTH_SHORT).show();
        c.add(Calendar.DATE, 30);
        String expiredDate = df.format(c.getTime());
    }

    private boolean addListenerOnSubmit() {
        // Insert data entered by user in an object
        smartMoneyBackGoldBean = new SmartMoneyBackGoldBean();

        if (cb_staffdisc.isChecked()) {
            smartMoneyBackGoldBean.setStaffDisc(true);
        } else {

            smartMoneyBackGoldBean.setStaffDisc(false);

        }
        if (cb_kerladisc.isChecked()) {
            smartMoneyBackGoldBean.setKerlaDisc(true);
        } else {
            smartMoneyBackGoldBean.setKerlaDisc(false);
        }
        if (CbJkResident.isChecked()) {
            smartMoneyBackGoldBean.setJkResident(true);
        } else {
            smartMoneyBackGoldBean.setJkResident(false);
        }
        smartMoneyBackGoldBean.setPlanName(spnrPlan.getSelectedItem()
                .toString());
        smartMoneyBackGoldBean.setAge(Integer.parseInt(spnrAge
                .getSelectedItem().toString()));
        smartMoneyBackGoldBean.setStrProposerAge(strProposerAge);
        smartMoneyBackGoldBean.setGender(spnrGender.getSelectedItem()
                .toString());
        smartMoneyBackGoldBean.setGender_proposer(gender_proposer);
        smartMoneyBackGoldBean.setPolicyTerm(Integer.parseInt(spnrPolicyTerm
                .getSelectedItem().toString()));

        smartMoneyBackGoldBean.setPremFreqOptions(selPremFreq.getSelectedItem().toString());

        if (selPremFreq.getSelectedItem().toString().equals("Single")) {
            smartMoneyBackGoldBean.setPremiumFreq("Single");
        } else {
            smartMoneyBackGoldBean.setPremiumFreq(spnrPremPayingMode.getSelectedItem().toString());
        }

        smartMoneyBackGoldBean.setPremPaymentTerm(spnrPremPaymentTerm.getSelectedItem().toString());

        smartMoneyBackGoldBean.setSumAssured_Basic(Integer
                .parseInt(edt_SumAssured.getText().toString()));
        // PT Rider
        if (cbPTARider.isChecked()) {
            smartMoneyBackGoldBean.setPT_Status(true);
            smartMoneyBackGoldBean.setPolicyTerm_PT(Integer
                    .parseInt(spnr_PtaTerm.getSelectedItem().toString()));
            smartMoneyBackGoldBean.setSumAssured_PT(Integer.parseInt(edt_PtaSA
                    .getText().toString()));

        } else {
            smartMoneyBackGoldBean.setPT_Status(false);
        }
        // ADB Rider
        if (cbADBRider.isChecked()) {
            smartMoneyBackGoldBean.setADB_Status(true);
            smartMoneyBackGoldBean.setPolicyTerm_ADB(Integer
                    .parseInt(spnr_AdbTerm.getSelectedItem().toString()));
            smartMoneyBackGoldBean.setSumAssured_ADB(Integer.parseInt(edt_AdbSA
                    .getText().toString()));
        } else {
            smartMoneyBackGoldBean.setADB_Status(false);
        }
        // ATPDB Rider
        if (cbATPDBRider.isChecked()) {
            smartMoneyBackGoldBean.setATPDB_Status(true);
            smartMoneyBackGoldBean.setPolicyTerm_ATPDB(Integer
                    .parseInt(spnr_AtpdbTerm.getSelectedItem().toString()));
            smartMoneyBackGoldBean.setSumAssured_ATPDB(Integer
                    .parseInt(edt_AtpdbSA.getText().toString()));
        } else {
            smartMoneyBackGoldBean.setATPDB_Status(false);
        }
        // CC13 Non-Linked Rider
        if (cbCC13NonLinkedRider.isChecked()) {
            smartMoneyBackGoldBean.setCC13NonLinked_Status(true);
            smartMoneyBackGoldBean.setPolicyTerm_CC13NonLinked(Integer
                    .parseInt(spnr_Cc13NonLinkedTerm.getSelectedItem()
                            .toString()));
            smartMoneyBackGoldBean.setSumAssured_CC13NonLinked(Integer
                    .parseInt(edt_Cc13NonLinkedSA.getText().toString()));
        } else {
            smartMoneyBackGoldBean.setCC13NonLinked_Status(false);
        }

        // Show Output Form

        return showSmartMoneyBackGoldOutputPg(smartMoneyBackGoldBean);

    }

    private void getInput(SmartMoneyBackGoldBean smartMoneyBackGoldBean) {
        inputVal = new StringBuilder();
        String LifeAssured_title = spnr_bi_smart_money_back_gold_life_assured_title
                .getSelectedItem().toString();
        String LifeAssured_firstName = edt_bi_smart_money_back_gold_life_assured_first_name
                .getText().toString();
        String LifeAssured_middleName = edt_bi_smart_money_back_gold_life_assured_middle_name
                .getText().toString();
        String LifeAssured_lastName = edt_bi_smart_money_back_gold_life_assured_last_name
                .getText().toString();
        String LifeAssured_DOB = btn_bi_smart_money_back_gold_life_assured_date
                .getText().toString();
        String LifeAssured_age = spnrAge.getSelectedItem().toString();

        String proposer_title = "";
        String proposer_firstName = "";
        String proposer_middleName = "";
        String proposer_lastName = "";
        String proposer_DOB = "";
        String proposer_age = "";
        String proposer_gender = "";

        if (!spnr_bi_money_back_gold_proposer_title.getSelectedItem()
                .toString().equals("")
                && !spnr_bi_money_back_gold_proposer_title.getSelectedItem()
                .toString().equals("Select Title")) {
            proposer_title = spnr_bi_money_back_gold_proposer_title
                    .getSelectedItem().toString();
            if (proposer_title.equals("Mr."))
                proposer_gender = "Male";
            else
                proposer_gender = "Female";
        }

        if (!edt_bi_money_back_gold_proposer_first_name.getText().toString()
                .equals(""))
            proposer_firstName = edt_bi_money_back_gold_proposer_first_name
                    .getText().toString();

        if (!edt_bi_money_back_gold_proposer_middle_name.getText().toString()
                .equals(""))
            proposer_middleName = edt_bi_money_back_gold_proposer_middle_name
                    .getText().toString();
        if (!edt_bi_money_back_gold_proposer_last_name.getText().toString()
                .equals(""))
            proposer_lastName = edt_bi_money_back_gold_proposer_last_name
                    .getText().toString();

        if (!btn_bi_money_back_gold_proposer_date.getText().toString()
                .equals("Select Date")) {
            Calendar present_date = Calendar.getInstance();
            int tDay = present_date.get(Calendar.DAY_OF_MONTH);
            int tMonth = present_date.get(Calendar.MONTH);
            int tYear = present_date.get(Calendar.YEAR);
            proposer_DOB = btn_bi_money_back_gold_proposer_date.getText()
                    .toString();
            proposer_age = commonForAllProd.calculateMyAge(tYear, tMonth + 1,
                    tDay, getDate1(proposer_DOB)) + "";
        }

        String wish_to_backdate_flag = "";
        if (rb_proposerdetail_personaldetail_backdating_yes.isChecked())
            wish_to_backdate_flag = "y";
        else
            wish_to_backdate_flag = "n";
        String backdate = "";
        if (wish_to_backdate_flag.equals("y"))
            backdate = btn_proposerdetail_personaldetail_backdatingdate
                    .getText().toString();
        else
            backdate = "";
        int age = smartMoneyBackGoldBean.getAge();
        String strProposerAge = smartMoneyBackGoldBean.getStrProposerAge();
        String planName = smartMoneyBackGoldBean.getPlanName();
        String gender = smartMoneyBackGoldBean.getGender();
        String gender_proposer = smartMoneyBackGoldBean.getGender_proposer();
        int basicPolicyTerm = smartMoneyBackGoldBean.getPolicyTerm();
        int ptTerm = smartMoneyBackGoldBean.getPolicyTerm_PT();
        int adbTerm = smartMoneyBackGoldBean.getPolicyTerm_ADB();
        int atpdbTerm = smartMoneyBackGoldBean.getPolicyTerm_ATPDB();
        int ccnlTerm = smartMoneyBackGoldBean.getPolicyTerm_CC13NonLinked();

        double basicSumAssured = smartMoneyBackGoldBean.getSumAssured_Basic();
        double ccnlSumAssured = smartMoneyBackGoldBean
                .getSumAssured_CC13NonLinked();
        double ptSumAssured = smartMoneyBackGoldBean.getSumAssured_PT();
        double adbSumAssured = smartMoneyBackGoldBean.getSumAssured_ADB();
        double atpdbSumAssured = smartMoneyBackGoldBean.getSumAssured_ATPDB();
        String PremPayingMode = smartMoneyBackGoldBean.getPremiumFreq();
        String premFreqOptions = smartMoneyBackGoldBean.getPremFreqOptions();
        String PremPaymentTerm = smartMoneyBackGoldBean.getPremPaymentTerm();

        boolean isJKresident = smartMoneyBackGoldBean.getJkResident();
        boolean isStaffOrNot = smartMoneyBackGoldBean.getStaffDisc();
        boolean statusPT = smartMoneyBackGoldBean.getPT_Status();
        boolean statusADB = smartMoneyBackGoldBean.getADB_Status();
        boolean statusATPDB = smartMoneyBackGoldBean.getATPDB_Status();
        boolean statusCCNL = smartMoneyBackGoldBean.getCC13NonLinked_Status();

        inputVal.append("<?xml version='1.0' encoding='utf-8' ?><smartmoneybackgold>");

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

        inputVal.append("<product_name>").append(product_name).append("</product_name>");
        inputVal.append("<product_Code>").append(product_Code).append("</product_Code>");
        inputVal.append("<product_UIN>").append(product_UIN).append("</product_UIN>");
        inputVal.append("<product_cateogory>").append(product_cateogory).append("</product_cateogory>");
        inputVal.append("<product_type>").append(product_type).append("</product_type>");

        inputVal.append("<proposer_Is_Same_As_Life_Assured>").append(proposer_Is_Same_As_Life_Assured).append("</proposer_Is_Same_As_Life_Assured>");

        inputVal.append("<isJKResident>").append(isJKresident).append("</isJKResident>");
        inputVal.append("<isStaff>").append(isStaffOrNot).append("</isStaff>");
        inputVal.append("<str_kerla_discount>").append(str_kerla_discount).append("</str_kerla_discount>");
        inputVal.append("<age>").append(age).append("</age>");
        inputVal.append("<plan>").append(planName).append("</plan>");

        inputVal.append("<policyTerm>").append(basicPolicyTerm).append("</policyTerm>");

        inputVal.append("<isPTRider>").append(statusPT).append("</isPTRider>");
        inputVal.append("<isADBRider>").append(statusADB).append("</isADBRider>");
        inputVal.append("<isATPDBRider>").append(statusATPDB).append("</isATPDBRider>");
        inputVal.append("<isCCNLRider>").append(statusCCNL).append("</isCCNLRider>");

        inputVal.append("<ptTerm>").append(ptTerm).append("</ptTerm>");
        inputVal.append("<adbTerm>").append(adbTerm).append("</adbTerm>");
        inputVal.append("<atpdbTerm>").append(atpdbTerm).append("</atpdbTerm>");
        inputVal.append("<ccnlTerm>").append(ccnlTerm).append("</ccnlTerm>");
        inputVal.append("<premFreqOptions>").append(premFreqOptions).append("</premFreqOptions>");
        inputVal.append("<PremPaymentTerm>").append(PremPaymentTerm).append("</PremPaymentTerm>");

        inputVal.append("<premFreq>").append(PremPayingMode).append("</premFreq>");
        inputVal.append("<sumAssured>").append(basicSumAssured).append("</sumAssured>");
        inputVal.append("<ptSA>").append(ptSumAssured).append("</ptSA>");
        inputVal.append("<adbSA>").append(adbSumAssured).append("</adbSA>");
        inputVal.append("<atpdbSA>").append(atpdbSumAssured).append("</atpdbSA>");
        inputVal.append("<ccnlSA>").append(ccnlSumAssured).append("</ccnlSA>");
        inputVal.append("<Wish_to_backdate_policy>").append(wish_to_backdate_flag).append("</Wish_to_backdate_policy>");
        inputVal.append("<backdating_Date>").append(backdate).append("</backdating_Date>");

        //Added Tushar Kadam Kerla Applicable Taxes 7-Jun-2019
        str_kerla_discount = "N";
        if (cb_kerladisc.isChecked()) {
            str_kerla_discount = "Y";
        }

        inputVal.append("<KFC>").append(str_kerla_discount).append("</KFC>");
        //End Added Tushar Kadam Kerla Applicable Taxes 7-Jun-2019

        inputVal.append("</smartmoneybackgold>");

    }

    private boolean showSmartMoneyBackGoldOutputPg(
            SmartMoneyBackGoldBean smartMoneyBackGoldBean1) {
        bussIll = new StringBuilder();
        retVal = new StringBuilder();
        int _year_F = 0;

        int year_F = 0;

        TextView op = new TextView(this);
        TextView op1 = new TextView(this);
        TextView op2 = new TextView(this);
        TextView op3 = new TextView(this);
        TextView op4 = new TextView(this);
        TextView op5 = new TextView(this);
        TextView op6 = new TextView(this);
        TextView op7 = new TextView(this);
        TextView op8 = new TextView(this);
        TextView op9 = new TextView(this);
        TextView op10 = new TextView(this);

        String nonGuaranMatBen_4Percent = null, nonGuaranMatBen_8Percent = null, totaBasePrem = null, guarntdDeathBenft = null, nongrntdDeathNenft_4Percent = null, nongrntdDeathNenft_8Percent = null, GSV_surrendr_val = null, NonGSV_surrndr_val_4Percent = null, NonGSV_surrndr_val_8Percent = null;

        String premiumPTA = "0", premiumWithoutDiscPTA = "0", premiumWithoutDiscSAPTA = "0";
        String premiumADB = "0", premiumWithotDiscADB = "0", premiumWithotDiscSAADB = "0";
        String premiumATPDB = "0", premiumWithoutDiscATPDB = "0", premiumWithoutDiscSAATPDB = "0";
        String premiumCC13NonLinked = "0", premiumWithoutDiscCC13NonLinked = "0", premiumWithoutDiscSACC13NonLinked = "0";
        //added by sujata
        String guaranMatBen = "0", getSurvivalBenefit = null, TotalMaturityBenefit8per = null, TotalMaturityBenefit4per = null, TotalDeathBenefit8per = null, TotalDeathBenefit4per = null, ReversionaryBonus8Per = null, ReversionaryBonus4Per = null, MaturityBenefit = null, GuaranteedDeathBenefit = null, GuarateedAddition = null, AnnulizedPremium = null;

        SmartMoneyBackGoldBusinessLogic smartMoneyBackGoldBusinessLogic = new SmartMoneyBackGoldBusinessLogic(
                smartMoneyBackGoldBean1);
        CommonForAllProd commonForAllProd = new CommonForAllProd();
        SmartMoneyBackGoldProperties prop = new SmartMoneyBackGoldProperties();

        String premiumBasic_NotRounded = ""
                + smartMoneyBackGoldBusinessLogic
                .get_Premium_Basic_WithoutST_NotRounded();
        // System.out.println("premiumBasic_NotRounded "+premiumBasic_NotRounded);
        String premiumBasic_Rounded = commonForAllProd
                .getRound(commonForAllProd.getStringWithout_E(Double
                        .parseDouble(premiumBasic_NotRounded)));
        System.out.println("premiumBasic_Rounded " + premiumBasic_Rounded);
        int premBasic = Integer.parseInt(premiumBasic_Rounded);
        double totBasePrem = smartMoneyBackGoldBusinessLogic.getTotBasePrem(
                Double.parseDouble(premiumBasic_NotRounded),
                smartMoneyBackGoldBean.getPremiumFreq());
        double staffrbte = smartMoneyBackGoldBusinessLogic
                .getStaffRebate("basic");

        String staffRebate = commonForAllProd.getRoundOffLevel2("" + staffrbte * 100);

        bussIll.append("<staffRebate>").append(staffRebate).append("</staffRebate>");

        staffRebate = "" + staffrbte;

        // guaranMatBen=commonForAllProd.getRoundUp(""+(smartMoneyBackGoldBusinessLogic.getGuaranMatBenMultiplyFactor()*smartMoneyBackGoldBean.getSumAssured_Basic()));
        // System.out.println("guaranMatBen "+guaranMatBen);
        // nonGuaranMatBen_4Percent=commonForAllProd.getRoundUp(smartMoneyBackGoldBusinessLogic.getNonGuaranMatBen("4%",Integer.parseInt(selPolicyTerm.getText().toString()
        // )));
        // System.out.println("nonGuaranMatBen_4Percent "+nonGuaranMatBen_4Percent);
        // nonGuaranMatBen_8Percent=commonForAllProd.getRoundUp(smartMoneyBackGoldBusinessLogic.getNonGuaranMatBen("8%",Integer.parseInt(selPolicyTerm.getText().toString()
        // )));
        // System.out.println("nonGuaranMatBen_8Percent "+nonGuaranMatBen_8Percent);
        //

        // modified by Priyanka Warekar - dated 9-12-2014

        int rowNumber = 0;

        double sumGuaranteedSurvivalBen = 0;
        boolean valPremiumError = false, valRiderPremiumError = false;
        double sumOfRiders = 0;
        //added by sujata
        double sumAnnualizedPrem = 0, sumSurvivalBenefit = 0;
        String annualizedPrem = null;
        /*	for (int k = 0; k < smartMoneyBackGoldBean.getPolicyTerm(); k++) {
			rowNumber++;
			year_F = rowNumber;
			_year_F = year_F; */


        for (int j = 0; j < smartMoneyBackGoldBean.getPolicyTerm(); j++) {
            rowNumber++;

            year_F = rowNumber;
            _year_F = year_F;
            //System.out.println("1. year_F " + year_F);
            bussIll.append("<policyYr").append(_year_F).append(">").append(_year_F).append("</policyYr").append(_year_F).append(">");
//added by sujata
			/*totaBasePrem = commonForAllProd.getRoundOffLevel2(commonForAllProd
					.getStringWithout_E(Double.parseDouble(""
							+ smartMoneyBackGoldBusinessLogic
							.get_Total_Base_Premium_Paid(year_F,
									totBasePrem,
											smartMoneyBackGoldBean
                                            .getPolicyTerm(),

													smartMoneyBackGoldBean
													.getPremiumFreq()))));*/
            //added by sujata
            totaBasePrem = commonForAllProd.getRoundOffLevel2(commonForAllProd
                    .getStringWithout_E(smartMoneyBackGoldBusinessLogic
                            .get_Annualized_Premium(year_F,
                                    totBasePrem,
                                    smartMoneyBackGoldBean
                                            .getPolicyTerm(), smartMoneyBackGoldBean
                                            .getPremiumFreq(), Integer.parseInt(smartMoneyBackGoldBean.getPremPaymentTerm()))));
//			System.out.println("totaBasePrem " + totaBasePrem);

            //added by sujata on 06-05-2019
            AnnulizedPremium = commonForAllProd.getRound(commonForAllProd.getStringWithout_E(smartMoneyBackGoldBusinessLogic
                    .getannulizedPremFinal(year_F, Integer.parseInt(smartMoneyBackGoldBean.getPremPaymentTerm()))));

            //added by sujata on 08-11-2019
            GuarateedAddition = commonForAllProd.getRoundOffLevel2(commonForAllProd
                    .getStringWithout_E(smartMoneyBackGoldBusinessLogic
                            .get_Guarateed_add(year_F,
                                    smartMoneyBackGoldBean
                                            .getPolicyTerm()

                            )));

            //added by sujata
            MaturityBenefit = commonForAllProd.getRoundOffLevel2(commonForAllProd
                    .getStringWithout_E(smartMoneyBackGoldBusinessLogic
                            .Maturity_Benefit(year_F,
                                    smartMoneyBackGoldBean
                                            .getPolicyTerm())));


            //25-11-2019
            //added by sujata
            ReversionaryBonus4Per = commonForAllProd.getRoundOffLevel2(commonForAllProd
                    .getStringWithout_E(smartMoneyBackGoldBusinessLogic
                            .Reversionary_Bonus(year_F,
                                    smartMoneyBackGoldBean
                                            .getPolicyTerm(),
                                    smartMoneyBackGoldBean.getSumAssured_Basic()
                                    , "4%", smartMoneyBackGoldBean.getPlanName()

                            )));
            //added by sujata

            ReversionaryBonus8Per = commonForAllProd.getRoundOffLevel2(commonForAllProd
                    .getStringWithout_E(smartMoneyBackGoldBusinessLogic
                            .Reversionary_Bonus8per(year_F,
                                    smartMoneyBackGoldBean
                                            .getPolicyTerm(),
                                    smartMoneyBackGoldBean.getSumAssured_Basic()

                            )));
            //25-11-2019
            if (_year_F == 1) {
                //annualizedPrem=Double.parseDouble(AnnulizedPremium);
                annualizedPrem = (commonForAllProd.getStringWithout_E(smartMoneyBackGoldBusinessLogic
                        .getannulizedPremFinal(year_F, Integer.parseInt(smartMoneyBackGoldBean.getPremPaymentTerm()))));
                //System.out.println("annualizedPrem 1 "+annualizedPrem);
            }

            //added by sujata on 06-12-2019
            sumAnnualizedPrem = sumAnnualizedPrem + Double.parseDouble(totaBasePrem);
            //System.out.println("sumAnnualizedPrem" + sumAnnualizedPrem);

            //added by sujata on 05-12-2019
            TotalDeathBenefit4per = commonForAllProd.getRound(commonForAllProd
                    .getStringWithout_E(smartMoneyBackGoldBusinessLogic
                            .TotalDeathBenefit4per(year_F,
                                    smartMoneyBackGoldBean
                                            .getPolicyTerm(), smartMoneyBackGoldBean.getAge()
                                    , "4%", smartMoneyBackGoldBean.getPlanName(),
                                    smartMoneyBackGoldBean.getSumAssured_Basic()

                                    , Double.parseDouble(premiumBasic_Rounded), Double.parseDouble(totaBasePrem)
                                    , Double.parseDouble(annualizedPrem), sumAnnualizedPrem

                            )));

            TotalDeathBenefit8per = commonForAllProd.getRound(commonForAllProd
                    .getStringWithout_E(smartMoneyBackGoldBusinessLogic
                            .TotalDeathBenefit8per(year_F,
                                    smartMoneyBackGoldBean
                                            .getPolicyTerm(), smartMoneyBackGoldBean.getAge()
                                    , "4%", smartMoneyBackGoldBean.getPlanName(),
                                    smartMoneyBackGoldBean.getSumAssured_Basic()

                                    , Double.parseDouble(premiumBasic_Rounded), Double.parseDouble(totaBasePrem)
                                    , Double.parseDouble(annualizedPrem), sumAnnualizedPrem)));

            TotalMaturityBenefit4per = commonForAllProd.getRoundOffLevel2(commonForAllProd
                    .getStringWithout_E(smartMoneyBackGoldBusinessLogic
                            .TotalMaturityBenefit4per(year_F,
                                    smartMoneyBackGoldBean
                                            .getPolicyTerm(),
                                    smartMoneyBackGoldBean.getSumAssured_Basic()
                                    , "4%", smartMoneyBackGoldBean.getPlanName()
                            )));

            TotalMaturityBenefit8per = commonForAllProd.getRoundOffLevel2(commonForAllProd
                    .getStringWithout_E(smartMoneyBackGoldBusinessLogic
                            .TotalMaturityBenefit8per(year_F,
                                    smartMoneyBackGoldBean
                                            .getPolicyTerm(),
                                    smartMoneyBackGoldBean.getSumAssured_Basic())));

            getSurvivalBenefit = commonForAllProd.getRoundOffLevel2(commonForAllProd
                    .getStringWithout_E(smartMoneyBackGoldBusinessLogic
                            .getSurvivalBenefit(year_F)));


            bussIll.append("<totaBasePrem").append(_year_F).append(">").append(commonForAllProd.getRound(commonForAllProd
                    .getStringWithout_E(Double.parseDouble("" + totaBasePrem)))).append("</totaBasePrem").append(_year_F).append(">");

            //added by sujata on 06-12-2019
            bussIll.append("<AnnulizedPremium").append(_year_F).append(">").append(commonForAllProd.getRound(commonForAllProd
                    .getStringWithout_E(Double.parseDouble(AnnulizedPremium)))).append("</AnnulizedPremium").append(_year_F).append(">");

            bussIll.append("<getSurvivalBenefit").append(_year_F).append(">").append(commonForAllProd.getRound(commonForAllProd
                    .getStringWithout_E(Double.parseDouble(getSurvivalBenefit)))).append("</getSurvivalBenefit").append(_year_F).append(">");


            guarntdDeathBenft = commonForAllProd.getRound(commonForAllProd
                    .getStringWithout_E(Double.parseDouble(""
                            + smartMoneyBackGoldBusinessLogic
                            .getGuarntedDeathBenefit(_year_F, smartMoneyBackGoldBean.getSumAssured_Basic(), smartMoneyBackGoldBean.getPolicyTerm(), smartMoneyBackGoldBean.getAge(), Double.parseDouble(premiumBasic_Rounded), Double.parseDouble(totaBasePrem), Double.parseDouble(annualizedPrem)))));
//			System.out.println("guarntdDeathBenft " + guarntdDeathBenft);
            //25-11-2019 added by sujata
            bussIll.append("<cashBonus>" + 0 + "</cashBonus>");
            //26-11-2019
			/*bussIll.append("<GuaranteedDeathBenefit" + _year_F+ ">"
			+ GuaranteedDeathBenefit + "</GuaranteedDeathBenefit" + _year_F + ">");*/
            //added by sujata
            bussIll.append("<GuarateedAddition").append(_year_F).append(">").append(GuarateedAddition).append("</GuarateedAddition").append(_year_F).append(">");

            //added by sujata
            bussIll.append("<MaturityBenefit").append(_year_F).append(">").append(MaturityBenefit).append("</MaturityBenefit").append(_year_F).append(">");

            //added by sujata
            bussIll.append("<ReversionaryBonus4Per").append(_year_F).append(">").append(ReversionaryBonus4Per).append("</ReversionaryBonus4Per").append(_year_F).append(">");
            //added by sujata
            bussIll.append("<ReversionaryBonus8Per").append(_year_F).append(">").append(ReversionaryBonus8Per).append("</ReversionaryBonus8Per").append(_year_F).append(">");
            //added by sujata
            bussIll.append("<TotalDeathBenefit4per").append(_year_F).append(">").append(TotalDeathBenefit4per).append("</TotalDeathBenefit4per").append(_year_F).append(">");
            //added by sujata
            bussIll.append("<TotalDeathBenefit8per").append(_year_F).append(">").append(TotalDeathBenefit8per).append("</TotalDeathBenefit8per").append(_year_F).append(">");
            //added by sujata
            bussIll.append("<TotalMaturityBenefit4per").append(_year_F).append(">").append(TotalMaturityBenefit4per).append("</TotalMaturityBenefit4per").append(_year_F).append(">");

            //added by sujata
            bussIll.append("<TotalMaturityBenefit8per").append(_year_F).append(">").append(TotalMaturityBenefit8per).append("</TotalMaturityBenefit8per").append(_year_F).append(">");

            //end
            bussIll.append("<guarntdDeathBenft").append(_year_F).append(">").append(guarntdDeathBenft).append("</guarntdDeathBenft").append(_year_F).append(">");

            nongrntdDeathNenft_4Percent = commonForAllProd
                    .getRoundUp(commonForAllProd.getStringWithout_E(Double
                            .parseDouble(""
                                    + smartMoneyBackGoldBusinessLogic
                                    .getNonGuarntedDeathBenefit(year_F,
                                            "4%"))));
            System.out.println("nongrntdDeathNenft_4Percent "
                    + nongrntdDeathNenft_4Percent);
            bussIll.append("<nongrntdDeathNenft_4Percent").append(_year_F).append(">").append(nongrntdDeathNenft_4Percent).append("</nongrntdDeathNenft_4Percent").append(_year_F).append(">");

            nongrntdDeathNenft_8Percent = commonForAllProd
                    .getRoundUp(commonForAllProd.getStringWithout_E(Double
                            .parseDouble(""
                                    + smartMoneyBackGoldBusinessLogic
                                    .getNonGuarntedDeathBenefit(year_F,
                                            "8%"))));
            System.out.println("nongrntdDeathNenft_8Percent  "
                    + nongrntdDeathNenft_8Percent);
            bussIll.append("<nongrntdDeathNenft_8Percent").append(_year_F).append(">").append(nongrntdDeathNenft_8Percent).append("</nongrntdDeathNenft_8Percent").append(_year_F).append(">");

            sumSurvivalBenefit = Double.parseDouble(guaranMatBen) + sumSurvivalBenefit;
            guaranMatBen = ""
                    + (smartMoneyBackGoldBusinessLogic.getGuaranMatBen(year_F));
            System.out.println("guaranMatBen " + guaranMatBen);
            bussIll.append("<guaranMatBen").append(_year_F).append(">").append(guaranMatBen).append("</guaranMatBen").append(_year_F).append(">");

            nonGuaranMatBen_4Percent = commonForAllProd
                    .getRoundUp(commonForAllProd.getStringWithout_E(smartMoneyBackGoldBusinessLogic
                            .getNonGuaranMatBen(year_F, Double
                                    .parseDouble(nongrntdDeathNenft_4Percent))));
            System.out.println("nonGuaranMatBen_4Percent "
                    + nonGuaranMatBen_4Percent);
            bussIll.append("<nonGuaranMatBen_4Percent").append(_year_F).append(">").append(nonGuaranMatBen_4Percent).append("</nonGuaranMatBen_4Percent").append(_year_F).append(">");

            nonGuaranMatBen_8Percent = commonForAllProd
                    .getRoundUp(commonForAllProd.getStringWithout_E(smartMoneyBackGoldBusinessLogic
                            .getNonGuaranMatBen(year_F, Double
                                    .parseDouble(nongrntdDeathNenft_8Percent))));
            System.out.println("nonGuaranMatBen_8Percent "
                    + nonGuaranMatBen_8Percent);
            bussIll.append("<nonGuaranMatBen_8Percent").append(_year_F).append(">").append(nonGuaranMatBen_8Percent).append("</nonGuaranMatBen_8Percent").append(_year_F).append(">");

		/*	GSV_surrendr_val = commonForAllProd.getRound(commonForAllProd
                    .getStringWithout_E((smartMoneyBackGoldBusinessLogic
                            .getGSV_SurrenderValue(year_F,
                                    Double.parseDouble(totaBasePrem),
									sumGuaranteedSurvivalBen))));*/


//			//added by sujata on 05-12-2019
//			sumAnnualizedPrem=sumAnnualizedPrem+Double.parseDouble(totaBasePrem);
//			//System.out.println("sumAnnualizedPrem" + sumAnnualizedPrem);

            GSV_surrendr_val = commonForAllProd.getRound(commonForAllProd
                    .getStringWithout_E((smartMoneyBackGoldBusinessLogic
                            .getGSV_SurrenderValue(year_F,
                                    sumAnnualizedPrem, sumSurvivalBenefit, smartMoneyBackGoldBean.getPlanName()
                                    , smartMoneyBackGoldBean.getPremFreqOptions(), smartMoneyBackGoldBean.getPolicyTerm()))));
//			System.out.println("GSV_surrendr_val " + GSV_surrendr_val);
            bussIll.append("<GSV_surrendr_val").append(_year_F).append(">").append(GSV_surrendr_val).append("</GSV_surrendr_val").append(_year_F).append(">");


            NonGSV_surrndr_val_4Percent = commonForAllProd.getRound(""
                    + (commonForAllProd
                    .getStringWithout_E(smartMoneyBackGoldBusinessLogic.getNonGSV_surrndr_val_SSV_4(year_F, smartMoneyBackGoldBean.getPolicyTerm(), smartMoneyBackGoldBean.getPremFreqOptions(), smartMoneyBackGoldBean.getSumAssured_Basic()
                            , Integer.parseInt(smartMoneyBackGoldBean.getPremPaymentTerm()), smartMoneyBackGoldBean.getAge(), sumSurvivalBenefit, "4%", smartMoneyBackGoldBean.getPlanName()))));


//			NonGSV_surrndr_val_4Percent = commonForAllProd.getRound(""
//					+ (smartMoneyBackGoldBusinessLogic.getNonGSV_surrndr_val_SSV_4(smartMoneyBackGoldBean.getPlanName(), smartMoneyBackGoldBean.getpremPayOption(), smartMoneyBackGoldBean.getPolicyTerm(), year_F, smartMoneyBackGoldBean.getSumAssured_Basic(), nongrntdDeathNenft_4Percent)));
//
            //System.out.println("NonGSV_surrndr_val_4Percent " + NonGSV_surrndr_val_4Percent);


//            NonGSV_surrndr_val_4Percent = ""
//					+ (smartMoneyBackGoldBusinessLogic.getNonGSV_surrndr_val(
//                    year_F,
//							Double.parseDouble(nongrntdDeathNenft_4Percent)));
//			System.out.println("NonGSV_surrndr_val_4Percent "
//					+ NonGSV_surrndr_val_4Percent);
            bussIll.append("<NonGSV_surrndr_val_4Percent").append(_year_F).append(">").append(NonGSV_surrndr_val_4Percent).append("</NonGSV_surrndr_val_4Percent").append(_year_F).append(">");


            NonGSV_surrndr_val_8Percent = commonForAllProd.getRound(commonForAllProd
                    .getStringWithout_E((smartMoneyBackGoldBusinessLogic
                            .getNonGSV_surrndr_val_SSV_8(year_F
                                    , smartMoneyBackGoldBean.getPolicyTerm()
                                    , smartMoneyBackGoldBean.getPremFreqOptions()
                                    , smartMoneyBackGoldBean.getSumAssured_Basic()
                                    , Integer.parseInt(smartMoneyBackGoldBean.getPremPaymentTerm()), smartMoneyBackGoldBean.getAge()
                                    , sumSurvivalBenefit))));

//			System.out.println("NonGSV_surrndr_val_8Percent "
//					+ NonGSV_surrndr_val_8Percent);
            bussIll.append("<NonGSV_surrndr_val_8Percent").append(_year_F).append(">").append(NonGSV_surrndr_val_8Percent).append("</NonGSV_surrndr_val_8Percent").append(_year_F).append(">");
            sumGuaranteedSurvivalBen = sumGuaranteedSurvivalBen
                    + Double.parseDouble(guaranMatBen);
        }

        String guaranMatBen1 = commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(smartMoneyBackGoldBusinessLogic.getGuaranMatBenMultiplyFactor() * smartMoneyBackGoldBean.getSumAssured_Basic()));

//		Systm.out.println("guaranMatBen "+guaranMatBen);

        String nonGuaranMatBen_4Percent1 = commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(smartMoneyBackGoldBusinessLogic.getNonGuaranMatBen("4%", smartMoneyBackGoldBean.getPolicyTerm())));

//		System.out.eprintln("nonGuaranMatBen_4Percent "+nonGuaranMatBen_4Percent);
        String nonGuaranMatBen_8Percent1 = commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(smartMoneyBackGoldBusinessLogic.getNonGuaranMatBen("8%", smartMoneyBackGoldBean.getPolicyTerm())));
//		System.out.println("nonGuaranMatBen_8Percent "+nonGuaranMatBen_8Percent);

        //String premiumPTA="0";

        if (cbPTARider.isChecked()) {
            // premiumPTA = commonForAllProd.getRoundUp(""
            // + smartMoneyBackGoldBusinessLogic
            // .get_Premium_PTA_WithoutST_NotRounded());
            /* modified by Akshaya on 20-MAY-16 start **/
            premiumPTA = ""
                    + smartMoneyBackGoldBusinessLogic
                    .get_Premium_PTA_WithoutST_NotRounded();
            /* modified by Akshaya on 20-MAY-16 end **/

            premiumWithoutDiscPTA = ""
                    + smartMoneyBackGoldBusinessLogic
                    .get_Premium_PTA_withoutDisc_WithoutST_NotRounded();
            premiumWithoutDiscSAPTA = ""
                    + smartMoneyBackGoldBusinessLogic
                    .get_Premium_PTA_withoutDiscAndSA_WithoutST_NotRounded();
            // System.out.println("premiumPTA "+premiumPTA);
        }
        // added by vrushali on 03/09/15
        else {
            premiumPTA = "0";
            premiumWithoutDiscPTA = "0";
            premiumWithoutDiscSAPTA = "0";
        }
        if (cbADBRider.isChecked()) {
            // premiumADB = commonForAllProd.getRoundUp(""
            // + smartMoneyBackGoldBusinessLogic
            // .get_Premium_ADB_WithoutST_NotRounded());
            /* modified by Akshaya on 20-MAY-16 start **/
            premiumADB = ""
                    + smartMoneyBackGoldBusinessLogic
                    .get_Premium_ADB_WithoutST_NotRounded();
            /* modified by Akshaya on 20-MAY-16 end **/
            premiumWithotDiscADB = ""
                    + smartMoneyBackGoldBusinessLogic
                    .get_Premium_WithoutADB_withoutDisc_WithoutST_NotRounded();
            premiumWithotDiscSAADB = ""
                    + smartMoneyBackGoldBusinessLogic
                    .get_Premium_WithoutADB_withoutDiscAndSA_WithoutST_NotRounded();
            // System.out.println("premiumADB "+premiumADB);
        }
        // added by vrushali on 03/09/15
        else {
            premiumADB = "0";
            premiumWithotDiscADB = "0";
            premiumWithotDiscSAADB = "0";

        }
        if (cbATPDBRider.isChecked()) {
            // premiumATPDB = commonForAllProd.getRoundUp(""
            // + smartMoneyBackGoldBusinessLogic
            // .get_Premium_ATPDB_WithoutST_NotRounded());

            premiumATPDB = ""
                    + smartMoneyBackGoldBusinessLogic
                    .get_Premium_ATPDB_WithoutST_NotRounded();

            premiumWithoutDiscATPDB = ""
                    + smartMoneyBackGoldBusinessLogic
                    .get_Premium_ATPDB_WithoutDisc_WithoutST_NotRounded();
            premiumWithoutDiscSAATPDB = ""
                    + smartMoneyBackGoldBusinessLogic
                    .get_Premium_ATPDB_WithoutDiscAndSA_WithoutST_NotRounded();
            System.out.println("premiumATPDB " + premiumATPDB);
            // System.out.println("premiumATPDB "+premiumATPDB);
        }
        // added by vrushali on 03/09/15
        else {
            premiumATPDB = "0";
            premiumWithoutDiscATPDB = "0";
            premiumWithoutDiscSAATPDB = "0";
        }
        if (cbCC13NonLinkedRider.isChecked()) {
            // premiumCC13NonLinked = commonForAllProd.getRoundUp(""
            // + smartMoneyBackGoldBusinessLogic
            // .get_Premium_CC13NonLinked_WithoutST_NotRounded());

            premiumCC13NonLinked = ""
                    + smartMoneyBackGoldBusinessLogic
                    .get_Premium_CC13NonLinked_WithoutST_NotRounded();

            premiumWithoutDiscCC13NonLinked = ""
                    + smartMoneyBackGoldBusinessLogic
                    .get_Premium_CC13NonLinked_WithoutDisc_WithoutST_NotRounded();
            premiumWithoutDiscSACC13NonLinked = ""
                    + smartMoneyBackGoldBusinessLogic
                    .get_Premium_CC13NonLinked_WithoutDiscAndSA_WithoutST_NotRounded();
            // System.out.println("premiumCC13NonLinked "+premiumCC13NonLinked);
        }
        // added by vrushali on 03/09/15
        else {
            premiumCC13NonLinked = "0";
            premiumWithoutDiscCC13NonLinked = "0";
            premiumWithoutDiscSACC13NonLinked = "0";
        }

        String premiumInst = commonForAllProd.getRoundUp(commonForAllProd
                .getStringWithout_E(Double.parseDouble(""
                        + (Double.parseDouble(premiumBasic_NotRounded)
                        + Double.parseDouble(premiumPTA)
                        + Double.parseDouble(premiumADB)
                        + Double.parseDouble(premiumATPDB) + Double
                        .parseDouble(premiumCC13NonLinked)))));
        System.out.println("premiumInst " + premiumInst);

        String minesOccuInterest = ""
                + smartMoneyBackGoldBusinessLogic
                .getMinesOccuInterest(smartMoneyBackGoldBean
                        .getSumAssured_Basic());

        String servicetax_MinesOccuInterest = ""
                + smartMoneyBackGoldBusinessLogic
                .getServiceTaxMines(Double.parseDouble(minesOccuInterest));

        minesOccuInterest = "" + (Double.parseDouble(minesOccuInterest) + Double.parseDouble(servicetax_MinesOccuInterest));

        // premiumInst = commonForAllProd.getStringWithout_E(Double
        // .parseDouble(premiumInst)
        // + Double.parseDouble(MinesOccuInterest));

        String premiumBasicWithoutDisc_NotRounded = ""
                + smartMoneyBackGoldBusinessLogic
                .get_Premium_Basic_WithoutST_WithoutDisc_NotRounded();

        String premWithoutDiscWithoutst = commonForAllProd
                .getRoundUp(commonForAllProd.getStringWithout_E(Double.parseDouble(""
                        + (Double
                        .parseDouble(premiumBasicWithoutDisc_NotRounded)
                        + Double.parseDouble(premiumWithoutDiscPTA)
                        + Double.parseDouble(premiumWithotDiscADB)
                        + Double.parseDouble(premiumWithoutDiscATPDB) + Double
                        .parseDouble(premiumWithoutDiscCC13NonLinked)))));

        String premWithoutDiscAndSAWithoutst = ""
                + smartMoneyBackGoldBusinessLogic
                .get_Premium_Basic_WithoutST_withoutStaffAndSARebate_NotRounded();

        // String premiumInstWithST = commonForAllProd
        // .getRoundUp(commonForAllProd.getStringWithout_E(Double
        // .parseDouble(""
        // + (Double.parseDouble(premiumInst) * (smartMoneyBackGoldBusinessLogic
        // .getServiceTax() + 1)))));
        // System.out.println("premiumInstWithST "+premiumInstWithST);

        /* double basicServiceTax = smartMoneyBackGoldBusinessLogic.getServiceTax(
				Double.parseDouble(premiumInst),
				smartMoneyBackGoldBean1.getJkResident(), "basic");
		double SBCServiceTax = smartMoneyBackGoldBusinessLogic.getServiceTax(
				Double.parseDouble(premiumInst),
				smartMoneyBackGoldBean1.getJkResident(), "SBC");
		double KKCServiceTax = smartMoneyBackGoldBusinessLogic.getServiceTax(
				Double.parseDouble(premiumInst),
                smartMoneyBackGoldBean1.getJkResident(), "KKC");*/

        double basicServiceTax = 0;
        double SBCServiceTax = 0;
        double KKCServiceTax = 0;

        double kerlaServiceTax = 0;
        double KeralaCessServiceTax = 0;
        boolean isKerlaDiscount = smartMoneyBackGoldBean1.isKerlaDisc();

        if (isKerlaDiscount) {
            basicServiceTax = smartMoneyBackGoldBusinessLogic.getServiceTax(Double.parseDouble(premiumInst), smartMoneyBackGoldBean.getJkResident(), smartMoneyBackGoldBean1.isKerlaDisc(), "basic");
            kerlaServiceTax = smartMoneyBackGoldBusinessLogic.getServiceTax(Double.parseDouble(premiumInst), smartMoneyBackGoldBean.getJkResident(), smartMoneyBackGoldBean1.isKerlaDisc(), "KERALA");
            // KeralaCessServiceTax =kerlaServiceTax-basicServiceTax;
            KeralaCessServiceTax = kerlaServiceTax;
        } else {
            basicServiceTax = smartMoneyBackGoldBusinessLogic.getServiceTax(Double.parseDouble(premiumInst), smartMoneyBackGoldBean.getJkResident(), smartMoneyBackGoldBean1.isKerlaDisc(), "basic");
            SBCServiceTax = smartMoneyBackGoldBusinessLogic.getServiceTax(Double.parseDouble(premiumInst), smartMoneyBackGoldBean.getJkResident(), smartMoneyBackGoldBean1.isKerlaDisc(), "SBC");
            //kerlaServiceTax = smartMoneyBackGoldBusinessLogic.getServiceTax(Double.parseDouble(premiumInst),smartMoneyBackGoldBean.getJkResident(),"KERALA");
            KKCServiceTax = smartMoneyBackGoldBusinessLogic.getServiceTax(Double.parseDouble(premiumInst), smartMoneyBackGoldBean.getJkResident(), smartMoneyBackGoldBean1.isKerlaDisc(), "KKC");
        }


        String serviceTax = String.valueOf(basicServiceTax + SBCServiceTax
                + KKCServiceTax + kerlaServiceTax);
        //System.out.println("premiumInst "+premiumInst);
//		System.out.println("service taxx : "+serviceTax);
//		System.out.println("basicServiceTax "+basicServiceTax);
//		System.out.println("SBCServiceTax "+SBCServiceTax);
//		System.out.println("kerlaServiceTax "+kerlaServiceTax);
        //  Added By Saurabh Jain on 14/05/2019 End

        String premiumInstWithST = commonForAllProd.getStringWithout_E(Double.parseDouble(premiumInst) + Double.parseDouble(serviceTax));

        //  Added By Saurabh Jain on 14/05/2019 Start
        double basicServiceTaxSecondYear = 0;
        double SBCServiceTaxSecondYear = 0;
        double KKCServiceTaxSecondYear = 0;
        double kerlaServiceTaxSecondYear = 0;
        double KeralaCessServiceTaxSecondYear = 0;

        if (isKerlaDiscount) {
            basicServiceTaxSecondYear = smartMoneyBackGoldBusinessLogic.getServiceTaxSecondYear(Double.parseDouble(premiumInst), smartMoneyBackGoldBean.getJkResident(), smartMoneyBackGoldBean1.isKerlaDisc(), "basic");
            kerlaServiceTaxSecondYear = smartMoneyBackGoldBusinessLogic.getServiceTaxSecondYear(Double.parseDouble(premiumInst), smartMoneyBackGoldBean.getJkResident(), smartMoneyBackGoldBean1.isKerlaDisc(), "KERALA");
            // KeralaCessServiceTaxSecondYear =kerlaServiceTaxSecondYear-basicServiceTaxSecondYear;
            KeralaCessServiceTaxSecondYear = 0;
        } else {
            basicServiceTaxSecondYear = smartMoneyBackGoldBusinessLogic.getServiceTaxSecondYear(Double.parseDouble(premiumInst), smartMoneyBackGoldBean.getJkResident(), smartMoneyBackGoldBean1.isKerlaDisc(), "basic");
            SBCServiceTaxSecondYear = smartMoneyBackGoldBusinessLogic.getServiceTaxSecondYear(Double.parseDouble(premiumInst), smartMoneyBackGoldBean.getJkResident(), smartMoneyBackGoldBean1.isKerlaDisc(), "SBC");
            KKCServiceTaxSecondYear = smartMoneyBackGoldBusinessLogic.getServiceTaxSecondYear(Double.parseDouble(premiumInst), smartMoneyBackGoldBean.getJkResident(), smartMoneyBackGoldBean1.isKerlaDisc(), "KKC");
        }

        String serviceTaxSecondYear = String.valueOf(basicServiceTaxSecondYear + SBCServiceTaxSecondYear + KKCServiceTaxSecondYear);
        //  Added By Saurabh Jain on 14/05/2019 End

        String premiumInstWithSTSecondYear = commonForAllProd
                .getStringWithout_E(Double.parseDouble(premiumInst)
                        + Double.parseDouble(serviceTaxSecondYear));

        double totInstPrem_exclST = premBasic;

        String backDateinterest, BackDateinterestwithGST;
        if (rb_proposerdetail_personaldetail_backdating_yes.isChecked()) {
            // BackDateinterest=commonForAllProd.getRoundUp(""+(shubhNiveshBussinesLogic.getBackDateInterest(Double.parseDouble(premiumSingleInstBasicWithST),
            // "16-jan-2014")));
            backDateinterest = commonForAllProd.getRoundUp(""
                    + (smartMoneyBackGoldBusinessLogic.getBackDateInterest(
                    Double.parseDouble(premiumInstWithST),
                    btn_proposerdetail_personaldetail_backdatingdate
                            .getText().toString())));

            BackDateinterestwithGST = commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(
                    Double.parseDouble(backDateinterest) + (Double.parseDouble(backDateinterest) * prop.GSTforbackdate)));

        } else {
            BackDateinterestwithGST = "0";
        }

        premiumInstWithST = commonForAllProd.getRoundUp(commonForAllProd
                .getStringWithout_E(Double.parseDouble(premiumInstWithST)
                        + Double.parseDouble(BackDateinterestwithGST)));

        // System.out.println("serviceTax "+serviceTax);
        try {

            retVal.append("<?xml version='1.0' encoding='utf-8' ?><SmartMoneyBackGold>");
            retVal.append("<errCode>0</errCode>");

            if (cb_staffdisc.isChecked()) {
                retVal.append("<staffStatus>sbi</staffStatus>");
            } else {
                retVal.append("<staffStatus>none</staffStatus>");
            }
            retVal.append("<staffRebate>").append(staffRebate).append("</staffRebate>");
            // PTA
            // Variable Declaration
            double premPTA = 0;
            if (cbPTARider.isChecked()) {
                /* Modified by Akshaya on 20-MAY-2016 **/
                premPTA = Double.parseDouble(commonForAllProd
                        .roundUp_Level2(premiumPTA));
                op7.setText("Preferred Term Assurance Rider Premium is Rs. "
                        + (premPTA));

                retVal.append("<premPTA>").append(premPTA).append("</premPTA>");
                retVal.append("<premPTAWithoutDisc>").append(commonForAllProd
                        .roundUp_Level2(premiumWithoutDiscPTA)).append("</premPTAWithoutDisc>");
                retVal.append("<premPTAWithoutDiscSA>").append(commonForAllProd
                        .roundUp_Level2(premiumWithoutDiscSAPTA)).append("</premPTAWithoutDiscSA>");
            } else {
                // added by vrushali on 03/09/15
                premPTA = 0;
                retVal.append("<premPTA>" + "0" + "</premPTA>");
                retVal.append("<premPTAWithoutDisc>" + "0"
                        + "</premPTAWithoutDisc>");

            }
            // ADB
            double premADB = 0;
            if (cbADBRider.isChecked()) {
                /* Modified by Akshaya on 20-MAY-2016 **/
                premADB = Double.parseDouble(commonForAllProd
                        .roundUp_Level2(premiumADB));
                op8.setText("Accidental Death Benefit Rider Premium is Rs. "
                        + (premADB));
                retVal.append("<premADB>").append(premADB).append("</premADB>");
                retVal.append("<premADBWithoutDisc>").append(commonForAllProd.roundUp_Level2(premiumWithotDiscADB)).append("</premADBWithoutDisc>");
                retVal.append("<premADBWithoutDiscSA>").append(commonForAllProd
                        .roundUp_Level2(premiumWithotDiscSAADB)).append("</premADBWithoutDiscSA>");

            } else {
                // added by vrushali on 03/09/15
                premADB = 0;
                retVal.append("<premADB>" + "0" + "</premADB>");
                retVal.append("<premADBWithoutDisc>" + "0"
                        + "</premADBWithoutDisc>");
            }
            // ATPDB
            double premATPDB = 0;
            if (cbATPDBRider.isChecked()) {
                /* Modified by Akshaya on 20-MAY-2016 **/
                premATPDB = Double.parseDouble(commonForAllProd
                        .roundUp_Level2(premiumATPDB));
                op9.setText("Accidental Total & Permanent Disability Benefit Rider Premium is Rs. "
                        + (premATPDB));
                retVal.append("<premATPDB>").append(premATPDB).append("</premATPDB>");
                retVal.append("<premATPDBWithoutDisc>").append(commonForAllProd
                        .roundUp_Level2(premiumWithoutDiscATPDB)).append("</premATPDBWithoutDisc>");
                retVal.append("<premATPDBWithoutDiscSA>").append(commonForAllProd
                        .roundUp_Level2(premiumWithoutDiscSAATPDB)).append("</premATPDBWithoutDiscSA>");
            } else {
                // added by vrushali on 03/09/15
                premATPDB = 0;
                retVal.append("<premATPDB>" + "0" + "</premATPDB>");
                retVal.append("<premATPDBWithoutDisc>" + "0"
                        + "</premATPDBWithoutDisc>");
            }
            // CC 13 NonLinked
            double premCC13NonLinked = 0;
            if (cbCC13NonLinkedRider.isChecked()) {
                /* Modified by Akshaya on 20-MAY-2016 **/
                premCC13NonLinked = Double.parseDouble(commonForAllProd
                        .roundUp_Level2(premiumCC13NonLinked));
                op10.setText("Criti Care13 Premium is Rs. "
                        + (premCC13NonLinked));
                // System.out.println("**criti care putextra " +
                // premCC13NonLinked);
                retVal.append("<premCC13>").append(premCC13NonLinked).append("</premCC13>");
                retVal.append("<premCC13WithoutDisc>").append(commonForAllProd
                        .roundUp_Level2(premiumWithoutDiscCC13NonLinked)).append("</premCC13WithoutDisc>");
                retVal.append("<premCC13WithoutDiscSA>").append(commonForAllProd
                        .roundUp_Level2(premiumWithoutDiscSACC13NonLinked)).append("</premCC13WithoutDiscSA>");
            } else {
                // added by vrushali on 03/09/15
                premCC13NonLinked = 0;
                retVal.append("<premCC13>" + "0" + "</premCC13>");
                retVal.append("<premCC13WithoutDisc>" + "0"
                        + "</premCC13WithoutDisc>");
            }

            // Total Result

            sumOfRiders = premPTA + premATPDB + premADB + premCC13NonLinked;
			/*sumOfRiders = +Double.parseDouble(premiumPTA)
					+ Double.parseDouble(premiumADB)
					+ Double.parseDouble(premiumATPDB)
					+ Double.parseDouble(premiumCC13NonLinked);*/

            valPremiumError = valInstPremium(premiumBasic_Rounded);
//			valRiderPremiumError = valRiderPremium(
//					Double.parseDouble(premiumBasicWithoutDisc_NotRounded),
//					sumOfRiders);

            retVal.append("<yearlyPrem>").append(premiumBasic_Rounded).append("</yearlyPrem>").append("<basicPremWithoutDisc>").append(commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(Double
                    .parseDouble(premiumBasicWithoutDisc_NotRounded)))).append("</basicPremWithoutDisc>").append("<GuarateedAddition>").append(commonForAllProd.getStringWithout_E(Double.parseDouble(GuarateedAddition))).append("</GuarateedAddition>").append("<MaturityBenefit>").append(commonForAllProd.getStringWithout_E(Double.parseDouble(MaturityBenefit))).append("</MaturityBenefit>").append("<ReversionaryBonus4Per>").append(commonForAllProd.getStringWithout_E(Double.parseDouble(ReversionaryBonus4Per))).append("</ReversionaryBonus4Per>").append("<ReversionaryBonus8Per>").append(commonForAllProd.getStringWithout_E(Double.parseDouble(ReversionaryBonus8Per))).append("</ReversionaryBonus8Per>").append("<TotalDeathBenefit4per>").append(commonForAllProd.getStringWithout_E(Double.parseDouble(TotalDeathBenefit4per))).append("</TotalDeathBenefit4per>").append("<TotalDeathBenefit8per>").append(commonForAllProd.getStringWithout_E(Double.parseDouble(TotalDeathBenefit8per))).append("</TotalDeathBenefit8per>").append("<TotalMaturityBenefit4per>").append(commonForAllProd.getStringWithout_E(Double.parseDouble(TotalMaturityBenefit4per))).append("</TotalMaturityBenefit4per>").append("<TotalMaturityBenefit8per>").append(commonForAllProd.getStringWithout_E(Double.parseDouble(TotalMaturityBenefit8per))).append("</TotalMaturityBenefit8per>").append("<basicPremWithoutDiscSA>").append(commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(Double
                    .parseDouble(premWithoutDiscAndSAWithoutst)))).append("</basicPremWithoutDiscSA>").append("<premInst>").append(premiumInst).append("</premInst>").append("<servcTax>").append(serviceTax).append("</servcTax>").append("<premWthST>").append(premiumInstWithST).append("</premWthST>").append("<servcTaxSecondYear>").append(serviceTaxSecondYear).append("</servcTaxSecondYear>").append("<premWthSTSecondYear>").append(premiumInstWithSTSecondYear).append("</premWthSTSecondYear>").append("<sumOfRiders>").append(sumOfRiders).append("</sumOfRiders>").append("<backdateInt>").append(BackDateinterestwithGST).append("</backdateInt>").append("<OccuInt>").append(minesOccuInterest).append("</OccuInt>").append("<OccuIntServiceTax>").append(servicetax_MinesOccuInterest).append("</OccuIntServiceTax>").append("<basicServiceTax>").append(commonForAllProd.getStringWithout_E(basicServiceTax)).append("</basicServiceTax>").append("<SBCServiceTax>").append(commonForAllProd.getStringWithout_E(SBCServiceTax)).append("</SBCServiceTax>").append("<KKCServiceTax>").append(commonForAllProd.getStringWithout_E(KKCServiceTax)).append("</KKCServiceTax>").append("<basicServiceTaxSecondYear>").append(commonForAllProd
                    .getStringWithout_E(basicServiceTaxSecondYear)).append("</basicServiceTaxSecondYear>").append("<SBCServiceTaxSecondYear>").append(commonForAllProd
                    .getStringWithout_E(SBCServiceTaxSecondYear)).append("</SBCServiceTaxSecondYear>").append("<KKCServiceTaxSecondYear>").append(commonForAllProd
                    .getStringWithout_E(KKCServiceTaxSecondYear)).append("</KKCServiceTaxSecondYear>").append("<KeralaCessServiceTax>").append(commonForAllProd.getStringWithout_E(KeralaCessServiceTax)).append("</KeralaCessServiceTax>").append("<KeralaCessServiceTaxSecondYear>").append(commonForAllProd.getStringWithout_E(KeralaCessServiceTaxSecondYear)).append("</KeralaCessServiceTaxSecondYear>").append(bussIll.toString());
            retVal.append("</SmartMoneyBackGold>");
        } catch (Exception e) {
            retVal.append("<?xml version='1.0' encoding='utf-8' ?><SmartMoneyBackGold>" + "<errCode>1</errCode>" + "<errorMessage>").append(e.getMessage()).append("</errorMessage></SmartMoneyBackGold>");
        }
        if (valPremiumError) {
            // if (valPremiumError && valRiderPremiumError) {
           /* op.setText("Basic Premium is Rs."
					+ currencyFormat.format(Double
					.parseDouble(premiumBasic_Rounded)));
			op1.setText("Installment Premium without Applicable Taxes is Rs."
					+ currencyFormat.format(Double.parseDouble(premiumInst)));
			op2.setText("Applicable Taxes is Rs."
					+ currencyFormat.format(Double.parseDouble(serviceTax)));
			op3.setText("Installment Premium with Applicable Taxes is Rs."
					+ currencyFormat.format(Double
					.parseDouble(premiumInstWithST)));
			op4.setText("Guaranteed Maturity Benefit is Rs. "
					+ currencyFormat.format(Double.parseDouble(guaranMatBen)));
			op5.setText("Non-guaranteed Maturity Benefit With 4%pa is Rs. "
					+ currencyFormat.format(Double
					.parseDouble(nonGuaranMatBen_4Percent)));
			op6.setText("Non-guaranteed Maturity Benefit With 8%pa is Rs. "
					+ currencyFormat.format(Double
                    .parseDouble(nonGuaranMatBen_8Percent)));*/
            return true;
            // i.putExtra("header", inputActivityHeader.getText().toString());

            // input

        } else {
            return false;
        }

        // modified by Priyanka Warekar - dated 9-12-2014

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

    private boolean valBackdate() {
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

    private boolean TrueBackdate() {

        try {

            String error = "";

            if (rb_proposerdetail_personaldetail_backdating_yes.isChecked()) {

                final Calendar c = Calendar.getInstance();
                final int currYear = c.get(Calendar.YEAR);
                final int currMonth = c.get(Calendar.MONTH) + 1;

                SimpleDateFormat dateformat1 = new SimpleDateFormat(
                        "dd-MM-yyyy",Locale.ENGLISH);
                Date dtBackDate = dateformat1
                        .parse(btn_proposerdetail_personaldetail_backdatingdate
                                .getText().toString());
                Date currentDate = c.getTime();

                Date finYerEndDate = null;

                // Date launchDate = dateformat1.parse("18-03-2014");

                Date launchDate = dateformat1.parse("01-10-2019");

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
                    showAlert.setMessage(error);
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

                showAlert.setMessage("Please Fill Name Detail For LifeAssured");
                showAlert.setNeutralButton("OK",
                        new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog,
                                                int which) {
                                if (lifeAssured_Title.equals("")) {
                                    // apply focusable method
                                    setFocusable(spnr_bi_smart_money_back_gold_life_assured_title);
                                    spnr_bi_smart_money_back_gold_life_assured_title
                                            .requestFocus();
                                } else if (lifeAssured_First_Name.equals("")) {
                                    setFocusable(edt_bi_smart_money_back_gold_life_assured_first_name);
                                    edt_bi_smart_money_back_gold_life_assured_first_name
                                            .requestFocus();
                                } else {
                                    setFocusable(edt_bi_smart_money_back_gold_life_assured_last_name);
                                    edt_bi_smart_money_back_gold_life_assured_last_name
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
                                setFocusable(spnr_bi_smart_money_back_gold_life_assured_title);
                                spnr_bi_smart_money_back_gold_life_assured_title
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
                                setFocusable(spnr_bi_smart_money_back_gold_life_assured_title);
                                spnr_bi_smart_money_back_gold_life_assured_title
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
                                setFocusable(spnr_bi_smart_money_back_gold_life_assured_title);
                                spnr_bi_smart_money_back_gold_life_assured_title
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
                                    setFocusable(spnr_bi_smart_money_back_gold_life_assured_title);
                                    spnr_bi_smart_money_back_gold_life_assured_title
                                            .requestFocus();
                                } else if (lifeAssured_First_Name.equals("")) {

                                    edt_bi_smart_money_back_gold_life_assured_first_name
                                            .requestFocus();
                                } else {
                                    edt_bi_smart_money_back_gold_life_assured_last_name
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
                                setFocusable(spnr_bi_smart_money_back_gold_life_assured_title);
                                spnr_bi_smart_money_back_gold_life_assured_title
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
                                setFocusable(spnr_bi_smart_money_back_gold_life_assured_title);
                                spnr_bi_smart_money_back_gold_life_assured_title
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
                                setFocusable(spnr_bi_smart_money_back_gold_life_assured_title);
                                spnr_bi_smart_money_back_gold_life_assured_title
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
                                    setFocusable(spnr_bi_money_back_gold_proposer_title);
                                    spnr_bi_money_back_gold_proposer_title
                                            .requestFocus();
                                } else if (proposer_First_Name.equals("")) {
                                    edt_bi_money_back_gold_proposer_first_name
                                            .requestFocus();
                                } else {
                                    edt_bi_money_back_gold_proposer_last_name
                                            .requestFocus();
                                }
                            }
                        });
                showAlert.show();

                return false;

            } else if (gender_proposer.equalsIgnoreCase("")) {
                showAlert.setMessage("Please Select Proposer Gender");
                showAlert.setNeutralButton("OK",
                        new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog,
                                                int which) {
                                if (proposer_Title.equals("")) {
                                    // apply focusable method
                                    setFocusable(spnr_bi_money_back_gold_proposer_title);
                                    spnr_bi_money_back_gold_proposer_title
                                            .requestFocus();
                                } else if (proposer_First_Name.equals("")) {
                                    edt_bi_money_back_gold_proposer_first_name
                                            .requestFocus();
                                } else {
                                    edt_bi_money_back_gold_proposer_last_name
                                            .requestFocus();
                                }
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
                                setFocusable(spnr_bi_money_back_gold_proposer_title);
                                spnr_bi_money_back_gold_proposer_title
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
                                setFocusable(spnr_bi_money_back_gold_proposer_title);
                                spnr_bi_money_back_gold_proposer_title
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
                                setFocusable(spnr_bi_money_back_gold_proposer_title);
                                spnr_bi_money_back_gold_proposer_title
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
                                setFocusable(btn_bi_smart_money_back_gold_life_assured_date);
                                btn_bi_smart_money_back_gold_life_assured_date
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

    private boolean valEligibilityOfRider() {
        StringBuilder error = new StringBuilder();
        if (!((Integer.parseInt(spnrAge.getSelectedItem().toString()) >= 18) && (Integer
                .parseInt(spnrAge.getSelectedItem().toString()) <= 60))) {
            if (cbPTARider.isChecked()) {
                cbPTARider.setChecked(false);
                if (trPreffredTermRider.getVisibility() == View.VISIBLE) {
                    trPreffredTermRider.setVisibility(View.GONE);
                }

                error.append("To avail Preferred Term Rider,age should be in the range of 18 to 60 Years. \n");

            }

            if (cbADBRider.isChecked()) {
                cbADBRider.setChecked(false);
                if (trADBRider.getVisibility() == View.VISIBLE) {
                    trADBRider.setVisibility(View.GONE);
                }
                error.append("To avail Accidental Death Benefit Rider,age should be in the range of 18 to 60 Years. \n");

            }

            if (cbATPDBRider.isChecked()) {
                cbATPDBRider.setChecked(false);
                if (trATPDBRider.getVisibility() == View.VISIBLE) {
                    trATPDBRider.setVisibility(View.GONE);
                }
                error.append("To avail Accidental Total & Permanent Disability Benefit Rider,age should be in the range of 18 to 60 Years. \n");
            }

        }
        if (!((Integer.parseInt(spnrAge.getSelectedItem().toString()) >= 18 && (Integer
                .parseInt(spnrAge.getSelectedItem().toString()) <= 55)))) {
            if (cbCC13NonLinkedRider.isChecked()) {
                cbCC13NonLinkedRider.setChecked(false);
                if (trCritiCareRider.getVisibility() == View.VISIBLE) {
                    trCritiCareRider.setVisibility(View.GONE);
                }

                error.append("To avail Criti Care 13 Non - Linked Rider,age should be in the range of 18 to 55 Years. \n");
            }
        }

        if (!error.toString().equals("")) {
            showAlert.setMessage(error.toString());
            showAlert.setNeutralButton("OK",
                    new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
            showAlert.show();
            setFocusable(btn_bi_smart_money_back_gold_life_assured_date);
            btn_bi_smart_money_back_gold_life_assured_date.requestFocus();
            return false;
        } else
            return true;
    }

    // Validate Rider Term
    private boolean valTermRider() {
        int minLimit = 5;
        int maxLimit = Integer.parseInt(spnrPolicyTerm.getSelectedItem()
                .toString());
        StringBuilder error = new StringBuilder();

        if (cbPTARider.isChecked()
                && Integer.parseInt(spnr_PtaTerm.getSelectedItem().toString()) > maxLimit) {
            spnr_PtaTerm.setSelection(0, false);
            error.append("Please enter Preferred Term Rider term between 5 and ").append(maxLimit).append(" years");

            setFocusable(spnr_PtaTerm);
            spnr_PtaTerm.requestFocus();
        }

        if (cbADBRider.isChecked()) {
            maxLimit = Math.min(Integer.parseInt(spnrPolicyTerm
                    .getSelectedItem().toString()), 30);
            if (Integer.parseInt(spnr_AdbTerm.getSelectedItem().toString()) > maxLimit) {
                spnr_AdbTerm.setSelection(0, false);
                error.append("Please enter Accidental Death Benefit Rider term between 5 and ").append(maxLimit).append(" years");
                setFocusable(spnr_AdbTerm);
                spnr_AdbTerm.requestFocus();
            }
        }

        if (cbATPDBRider.isChecked()
                && Integer
                .parseInt(spnr_AtpdbTerm.getSelectedItem().toString()) > maxLimit) {
            spnr_AtpdbTerm.setSelection(0, false);
            error.append("Please enter Accidental Total and Permenent Disability Rider term between 5 and ").append(maxLimit).append(" years");
            setFocusable(spnr_AtpdbTerm);
            spnr_AtpdbTerm.requestFocus();
        }

        if (cbCC13NonLinkedRider.isChecked()) {
            maxLimit = Math.min(Integer.parseInt(spnrPolicyTerm
                    .getSelectedItem().toString()), (64 - Integer
                    .parseInt(spnrAge.getSelectedItem().toString())));
            if (Integer.parseInt(spnr_Cc13NonLinkedTerm.getSelectedItem()
                    .toString()) > maxLimit) {
                spnr_Cc13NonLinkedTerm.setSelection(0, false);
                error.append("Please enter CritiCare 13 Non-Linked Rider term between 5 and ").append(maxLimit).append(" years");

                setFocusable(spnr_Cc13NonLinkedTerm);
                spnr_Cc13NonLinkedTerm.requestFocus();
            }
        }
        if (!error.toString().equals("")) {

            showAlert.setMessage(error.toString());
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

    // Age Validation after CALCULATE Button is pressed
    private boolean valAge() {

        int Age = Integer.parseInt(spnrAge.getSelectedItem().toString());
        int PolicyTerm = Integer.parseInt(spnrPolicyTerm.getSelectedItem()
                .toString());

        boolean stat = true;
        int maxAge = 0;
        if (selPremFreq.getSelectedItem().toString().equals("Regular")) {
            maxAge = 45;
        } else {
            maxAge = 55;
        }
        try {
           /* if (spnrPlan.getSelectedItem().toString().equals("Option 1")
					&& ((Integer.parseInt(spnrAge.getSelectedItem().toString()) < 15) || (Integer
					.parseInt(spnrAge.getSelectedItem().toString()) > 55))) {
				showAlert
						.setMessage("For Option 1, age should be in the range of 15 Years to 55 Years");
				showAlert.setNeutralButton("OK",
						new DialogInterface.OnClickListener() {
                            @Override
							public void onClick(DialogInterface dialog,
												int which) {
								spnrAge.setSelection(0, false);
								// Toast.makeText(getApplicationContext(),
								// "Ok button Clicked ",
								// Toast.LENGTH_LONG).show();
							}
						});

				setFocusable(btn_bi_smart_money_back_gold_life_assured_date);
				btn_bi_smart_money_back_gold_life_assured_date.requestFocus();
				showAlert.show();
				stat = false;
            }
            else*/
            if (spnrPlan.getSelectedItem().toString().equals("Option 1")
                    && ((Integer.parseInt(spnrAge.getSelectedItem().toString()) < 14) || (Integer
                    .parseInt(spnrAge.getSelectedItem().toString()) > maxAge))) {
                showAlert
                        .setMessage("For Option 1, age should be in the range of 14 Years to "+maxAge+" Years");
                showAlert.setNeutralButton("OK",
                        new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog,
                                                int which) {
                                spnrAge.setSelection(0, false);
                                // Toast.makeText(getApplicationContext(),
                                // "Ok button Clicked ",
                                // Toast.LENGTH_LONG).show();
                            }
                        });
                setFocusable(btn_bi_smart_money_back_gold_life_assured_date);
                btn_bi_smart_money_back_gold_life_assured_date.requestFocus();
                showAlert.show();
                stat = false;

            } else if (spnrPlan.getSelectedItem().toString().equals("Option 2")
                    && ((Integer.parseInt(spnrAge.getSelectedItem().toString()) < 14) || (Integer
                    .parseInt(spnrAge.getSelectedItem().toString()) > maxAge))) {
                // System.out.println("* 5.3 *");
                showAlert
                        .setMessage("For Option 2, age should be in the range of 14 Years to "+maxAge+" Years");
                showAlert.setNeutralButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                spnrAge.setSelection(0, false);
                                // Toast.makeText(getApplicationContext(),
                                // "Ok button Clicked ",
                                // Toast.LENGTH_LONG).show();
                            }
                        });
                setFocusable(btn_bi_smart_money_back_gold_life_assured_date);
                btn_bi_smart_money_back_gold_life_assured_date.requestFocus();
                showAlert.show();
                stat = false;
            } else if (spnrPlan.getSelectedItem().toString().equals("Option 3")
                    && ((Integer.parseInt(spnrAge.getSelectedItem().toString()) < 14) || (Integer
                    .parseInt(spnrAge.getSelectedItem().toString()) > maxAge))) {
                // System.out.println("* 5.4 *");
                showAlert
                        .setMessage("For Option 3, age should be in the range of 14 Years to "+maxAge+" Years");
                showAlert.setNeutralButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                spnrAge.setSelection(0, false);
                            }
                        });
                setFocusable(btn_bi_smart_money_back_gold_life_assured_date);
                btn_bi_smart_money_back_gold_life_assured_date.requestFocus();
                showAlert.show();
                stat = false;
            }

            if ((Age + PolicyTerm) > 70) {
                showAlert
                        .setMessage("Max. Maturity age is 70 years");
                showAlert.setNeutralButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                spnrAge.setSelection(0, false);
                            }
                        });
                setFocusable(btn_bi_smart_money_back_gold_life_assured_date);
                btn_bi_smart_money_back_gold_life_assured_date.requestFocus();
                spnrAge.setSelection(0, false);
                btn_bi_smart_money_back_gold_life_assured_date
                        .setText("Select Date");
                lifeAssured_date_of_birth = "";
                showAlert.show();
                stat = false;
            }
        } catch (Exception ignored) {
        }
        return stat;
    }

    private Boolean valSA() {
        double minRiderLimit, maxRiderLimit;
        StringBuilder error = new StringBuilder();
        // System.out.println("basic sa "+Double.parseDouble(basicSA.getText().toString()));
        if (edt_SumAssured.getText().toString().equals("")
                || Double.parseDouble(edt_SumAssured.getText().toString()) < 200000) {
            error.append("Sum assured should be greater than or equal to 2,00,000");

        } else if (Double.parseDouble(edt_SumAssured.getText().toString()) % 1000 != 0) {
            error.append("Sum assured should be multiple of 1,000");
        } else {

            if (cbPTARider.isChecked()) {
                minRiderLimit = 25000;
                maxRiderLimit = Math
                        .min(5000000, Double.parseDouble(edt_SumAssured
                                .getText().toString()));

                if (edt_PtaSA.getText().toString().equals("")
                        || Double.parseDouble(edt_PtaSA.getText().toString()) < minRiderLimit
                        || Double.parseDouble(edt_PtaSA.getText().toString()) > maxRiderLimit)
                    error.append("\nEnter Sum assured for Preferred Term Assurance Rider between ").append(currencyFormat.format(minRiderLimit)).append(" and ").append(currencyFormat.format(maxRiderLimit));
                else if (Double.parseDouble(edt_PtaSA.getText().toString()) % 1000 != 0)
                    error.append("\nEnter Sum assured for Preferred Term Assurance Rider should be multiple of 1,000");
            }

            if (cbADBRider.isChecked()) {
                minRiderLimit = 25000;
                maxRiderLimit = Math
                        .min(5000000, Double.parseDouble(edt_SumAssured
                                .getText().toString()));

                if (edt_AdbSA.getText().toString().equals("")
                        || Double.parseDouble(edt_AdbSA.getText().toString()) < minRiderLimit
                        || Double.parseDouble(edt_AdbSA.getText().toString()) > maxRiderLimit)
                    error.append("\nEnter Sum assured for Accidental Death Benefit Rider between ").append(currencyFormat.format(minRiderLimit)).append(" and ").append(currencyFormat.format(maxRiderLimit));
                else if (Double.parseDouble(edt_AdbSA.getText().toString()) % 1000 != 0)
                    error.append("\nEnter Sum assured for Accidental Death Benefit Rider should be multiple of 1,000");
            }
            if (cbATPDBRider.isChecked()) {
                minRiderLimit = 25000;
                maxRiderLimit = Math
                        .min(5000000, Double.parseDouble(edt_SumAssured
                                .getText().toString()));
                if (edt_AtpdbSA.getText().toString().equals("")
                        || Double.parseDouble(edt_AtpdbSA.getText().toString()) < minRiderLimit
                        || Double.parseDouble(edt_AtpdbSA.getText().toString()) > maxRiderLimit)
                    error.append("\nEnter Sum assured for Accidental Total and Permenent Disability Benefit Rider between ").append(currencyFormat.format(minRiderLimit)).append(" and ").append(currencyFormat.format(maxRiderLimit));
                else if (Double.parseDouble(edt_AtpdbSA.getText().toString()) % 1000 != 0)
                    error.append("\nEnter Sum assured for Accidental Total and Permenent Disability Benefit Rider should be multiple of 1,000");

            }
            if (cbCC13NonLinkedRider.isChecked()) {

                /* Changes done by vrushali on 17/4/2015 */
                minRiderLimit = 25000;
                maxRiderLimit = Math
                        .min(2000000, Double.parseDouble(edt_SumAssured
                                .getText().toString()));
                if (edt_Cc13NonLinkedSA.getText().toString().equals("")
                        || Double.parseDouble(edt_Cc13NonLinkedSA.getText()
                        .toString()) < minRiderLimit
                        || Double.parseDouble(edt_Cc13NonLinkedSA.getText()
                        .toString()) > maxRiderLimit)
                    error.append("\nEnter Sum assured for CC13 Nonlinked Rider between ").append(currencyFormat.format(minRiderLimit)).append(" and ").append(currencyFormat.format(maxRiderLimit));
                else if (Double.parseDouble(edt_Cc13NonLinkedSA.getText()
                        .toString()) % 1000 != 0)
                    error.append("\nEnter Sum assured for CC13 Nonlinked Rider between 1,000");
            }

        }

        if (!error.toString().equals("")) {
            showAlert.setMessage(error.toString());
            showAlert.setNeutralButton("OK",
                    new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
            setFocusable(edt_SumAssured);
            edt_SumAssured.requestFocus();
            showAlert.show();
            return false;
        } else
            return true;
    }

    // Validate Minimum Premium
    private boolean valInstPremium(String premiumBasic_Rounded) {

        if (spnrPremPayingMode.getSelectedItem().toString().equals("Yearly")
                && (Integer.parseInt(premiumBasic_Rounded) < 9500)
                && ((selPremFreq.getSelectedItem().toString().equals("Regular"))
                || (selPremFreq.getSelectedItem().toString().equals("Limited")))) {
            showAlert
                    .setMessage("Minimum premium for Yearly Mode under this product is Rs.9500");
            showAlert.setNeutralButton("OK",
                    new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
            setFocusable(edt_SumAssured);
            edt_SumAssured.requestFocus();
            showAlert.show();

            return false;

        } else if (spnrPremPayingMode.getSelectedItem().toString()
                .equals("Half Yearly")
                && (Integer.parseInt(premiumBasic_Rounded) < 5000)
                && ((selPremFreq.getSelectedItem().toString().equals("Regular"))
                || (selPremFreq.getSelectedItem().toString().equals("Limited")))) {
            showAlert
                    .setMessage("Minimum premium for Half Yearly Mode under this product is Rs.5000");
            showAlert.setNeutralButton("OK",
                    new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
            setFocusable(edt_SumAssured);
            edt_SumAssured.requestFocus();
            showAlert.show();

            return false;
        } else if (spnrPremPayingMode.getSelectedItem().toString().equals("Quarterly")
                && (Integer.parseInt(premiumBasic_Rounded) < 2500)
                && ((selPremFreq.getSelectedItem().toString().equals("Regular"))
                || (selPremFreq.getSelectedItem().toString().equals("Limited")))) {
            showAlert
                    .setMessage("Minimum premium for Quarterly Mode under this product is Rs.2500");
            showAlert.setNeutralButton("OK",
                    new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
            setFocusable(edt_SumAssured);
            edt_SumAssured.requestFocus();
            showAlert.show();
            return false;
        } else if (spnrPremPayingMode.getSelectedItem().toString()
                .equals("Monthly")
                && (Integer.parseInt(premiumBasic_Rounded) < 800)
                && ((selPremFreq.getSelectedItem().toString().equals("Regular"))
                || (selPremFreq.getSelectedItem().toString().equals("Limited")))) {
            showAlert
                    .setMessage("Minimum premium for Monthly Mode under this product is Rs.800");
            showAlert.setNeutralButton("OK",
                    new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
            setFocusable(edt_SumAssured);
            edt_SumAssured.requestFocus();
            showAlert.show();

            return false;
        } else if (selPremFreq.getSelectedItem().toString().equals("Single")
                && (Integer.parseInt(premiumBasic_Rounded) < 125000)) {
            showAlert
                    .setMessage("Minimum premium for Single Mode under this product is Rs.1,25,000");
            showAlert.setNeutralButton("OK",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
            setFocusable(edt_SumAssured);
            edt_SumAssured.requestFocus();
            showAlert.show();

            return false;
        } else {
            return true;
        }
    }

    // Validate Rider Premium
    private boolean valRiderPremium(double premBasic, double sumOfRiders) {
        if ((premBasic * 0.3) < sumOfRiders) {
            showAlert
                    .setMessage("Total of Rider Premium should not be greater than 30% of the Base Premium");
            showAlert.setNeutralButton("OK",
                    new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
            setFocusable(edt_SumAssured);
            edt_SumAssured.requestFocus();
            showAlert.show();

            return false;
        } else
            return true;
    }

    // ****************************** Validation ends Here
    // *******************************************//

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

                        if (minAge <= age && age <= maxAge) {

                            if (Integer.parseInt(final_age) < 18) {
                                ll_riders.setVisibility(View.GONE);
                                proposer_Is_Same_As_Life_Assured = "N";
//								tr_money_back_gold_proposer_detail1
//										.setVisibility(View.VISIBLE);
                                tr_money_back_gold_proposer_detail2
                                        .setVisibility(View.VISIBLE);

                                btn_bi_smart_money_back_gold_life_assured_date
                                        .setText(date);

                                spnrAge.setSelection(getIndex(spnrAge, final_age),
                                        false);
                                valAge();
                                lifeAssured_date_of_birth = getDate1(date + "");
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

                                clearFocusable(btn_bi_smart_money_back_gold_life_assured_date);
                                setFocusable(spnr_bi_money_back_gold_proposer_title);
                                spnr_bi_money_back_gold_proposer_title
                                        .requestFocus();
                            } else {
//								ll_riders.setVisibility(View.VISIBLE);
                                proposer_Is_Same_As_Life_Assured = "Y";
//								tr_money_back_gold_proposer_detail1
//										.setVisibility(View.GONE);
                                tr_money_back_gold_proposer_detail2
                                        .setVisibility(View.GONE);
                                btn_bi_smart_money_back_gold_life_assured_date
                                        .setText(date);

                                spnrAge.setSelection(getIndex(spnrAge, final_age),
                                        false);
                                valAge();
                                lifeAssured_date_of_birth = getDate1(date + "");

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

                                clearFocusable(btn_bi_smart_money_back_gold_life_assured_date);
                                setFocusable(edt_proposerdetail_basicdetail_contact_no);
                                edt_proposerdetail_basicdetail_contact_no
                                        .requestFocus();
                            }

                            /*
                             * setFocusable(spnrPlan); spnrPlan.requestFocus();
                             */

                            // setFocusable(edt_premiumAmt);
                            // edt_premiumAmt.requestFocus();
                        } else {
                            commonMethods.BICommonDialog(context, "Minimum Age should be " + minAge
                                    + "yrs and Maximum Age should be" + maxAge
                                    + " yrs For LifeAssured");
                            btn_bi_smart_money_back_gold_life_assured_date
                                    .setText("Select Date");
                            lifeAssured_date_of_birth = "";
                            clearFocusable(btn_bi_smart_money_back_gold_life_assured_date);
                            setFocusable(btn_bi_smart_money_back_gold_life_assured_date);
                            btn_bi_smart_money_back_gold_life_assured_date
                                    .requestFocus();
                        }
                    }
                    break;
                case 5:
                    strProposerAge = final_age;
                    final_age = Integer.toString(age);
                    if (final_age.contains("-") || final_age == "" || final_age == null) {
                        commonMethods.BICommonDialog(context, "Please fill Valid Bith Date");
                    } else {
                        if (18 <= age && age <= maxAge) {

                            btn_bi_money_back_gold_proposer_date.setText(date);
                            edt_bi_smart_wealth_builder_proposer_age
                                    .setText(final_age);
                            proposer_date_of_birth = getDate1(date + "");

                            clearFocusable(btn_bi_money_back_gold_proposer_date);

                            setFocusable(edt_proposerdetail_basicdetail_contact_no);
                            edt_proposerdetail_basicdetail_contact_no
                                    .requestFocus();

                        } else {
                            commonMethods.BICommonDialog(context, "Minimum age should be 18 yrs for proposer");
                            btn_bi_money_back_gold_proposer_date
                                    .setText("Select Date");
                            proposer_date_of_birth = "";
                            clearFocusable(btn_bi_money_back_gold_proposer_date);
                            setFocusable(btn_bi_money_back_gold_proposer_date);
                            btn_bi_money_back_gold_proposer_date.requestFocus();

                        }
                    }
                    break;

                case 6:
                    if (age >= 0) {
                        proposer_Backdating_BackDate = date + "";
                        btn_proposerdetail_personaldetail_backdatingdate
                                .setText(proposer_Backdating_BackDate);
                        clearFocusable(btn_proposerdetail_personaldetail_backdatingdate);

                        setFocusable(cbPTARider);
                        cbPTARider.requestFocus();

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

            int Proposerage = calculateMyAge(mYear, Integer.parseInt(mont),
                    Integer.parseInt(day));

            String str_final_age = Integer.toString(Proposerage);
            spnrAge.setSelection(getIndex(spnrAge, str_final_age), false);
            valAge();

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
        initialiseDateParameter(lifeAssured_date_of_birth, 35);
        DIALOG_ID = 4;
        showDialog(DATE_DIALOG_ID);

    }

    public void onClickDob(View v) {
        // setDefaultDate();
        DIALOG_ID = 5;
        showDialog(DATE_DIALOG_ID);
    }

    public void onClickBackDating(View v) {
        String backdate = getDate1(proposer_Backdating_BackDate);
        initialiseDateParameter(backdate, 0);
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
            DateFormat userDateFormat = new SimpleDateFormat("MM-dd-yyyy",Locale.ENGLISH);
            DateFormat dateFormatNeeded = new SimpleDateFormat("dd/MM/yyyy",Locale.ENGLISH);
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
            DateFormat userDateFormat = new SimpleDateFormat("dd-MM-yyyy",Locale.ENGLISH);
            DateFormat dateFormatNeeded = new SimpleDateFormat("MM-dd-yyyy",Locale.ENGLISH);
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

    private void windowmessagesgin() {

        d = new Dialog(BI_SmartMoneyBackGoldActivity.this);
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
                Intent intent = new Intent(BI_SmartMoneyBackGoldActivity.this,
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
        if (v.getId() == edt_bi_smart_money_back_gold_life_assured_first_name
                .getId()) {
            setFocusable(edt_bi_smart_money_back_gold_life_assured_middle_name);
            edt_bi_smart_money_back_gold_life_assured_middle_name
                    .requestFocus();
        } else if (v.getId() == edt_bi_smart_money_back_gold_life_assured_middle_name
                .getId()) {
            setFocusable(edt_bi_smart_money_back_gold_life_assured_last_name);
            edt_bi_smart_money_back_gold_life_assured_last_name.requestFocus();
        } else if (v.getId() == edt_bi_smart_money_back_gold_life_assured_last_name
                .getId()) {
            setFocusable(btn_bi_smart_money_back_gold_life_assured_date);
            btn_bi_smart_money_back_gold_life_assured_date.requestFocus();
        } else if (v.getId() == edt_proposerdetail_basicdetail_contact_no
                .getId()) {
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
            clearFocusable(spnrPlan);
            setFocusable(spnrPlan);
            spnrPlan.requestFocus();
        } else if (v.getId() == edt_SumAssured.getId()) {

            commonMethods.hideKeyboard(edt_SumAssured, context);

            /*
             * clearFocusable(edt_SumAssured);
             * setFocusable(rb_proposerdetail_personaldetail_backdating_yes);
             * rb_proposerdetail_personaldetail_backdating_yes.requestFocus();
             */

            setFocusable(cbPTARider);
            cbPTARider.requestFocus();
        } else if (v.getId() == edt_PtaSA.getId()) {
            setFocusable(cbADBRider);
            cbADBRider.requestFocus();
        } else if (v.getId() == edt_AdbSA.getId()) {
            setFocusable(cbATPDBRider);
            cbATPDBRider.requestFocus();
        } else if (v.getId() == edt_AtpdbSA.getId()) {
            setFocusable(cbCC13NonLinkedRider);
            cbCC13NonLinkedRider.requestFocus();
        } else if (v.getId() == edt_Cc13NonLinkedSA.getId()) {

            commonMethods.hideKeyboard(edt_Cc13NonLinkedSA, context);

        }

        return true;
    }

    private boolean createPdf() {
        try {

            // System.out.println("  "+maturityAge+
            // "  "+annPrem+" "+sumAssured);


            Font headerBold = new Font(Font.FontFamily.TIMES_ROMAN, 8,
                    Font.BOLD);
            Font small_bold = new Font(Font.FontFamily.TIMES_ROMAN, 6,
                    Font.BOLD);
            Font small_normal = new Font(Font.FontFamily.TIMES_ROMAN, 6,
                    Font.NORMAL);
            Font normal1_BoldUnderline = new Font(Font.FontFamily.TIMES_ROMAN,
                    6, Font.UNDERLINE | Font.BOLD);
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

            // newBIFile=new File(folder, QuatationNumber+"P01_Tabular.pdf");

            Rectangle rect = new Rectangle(594f, 792f);

            Document document = new Document(rect, 50, 50, 50, 50);
            // Document document = new Document(PageSize.A4, 50, 50, 50, 50);
            @SuppressWarnings("unused")
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

            cell = new PdfPCell(new Phrase("\n", headerBold));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("SBI Life Insurance Co. Ltd",
                    headerBold));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("\n", headerBold));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setRowspan(9);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(image);
            cell.setRowspan(3);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(
                    new Phrase(
                            "Registered & Corporate Office: SBI Life Insurance Co. Ltd, “Natraj”, M.V. Road & Western Express Highway Junction, Andheri (East),,",
                            normal1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);

            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Mumbai - 400 069. IRDAI Registration No. 111",
                    normal1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);

            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Website: www.sbilife.co.in | Email: info@sbilife.co.in | CIN: L99999MH2000PLC129113" + "\n" + "Toll Free: 1800 267 9090 (Between 9.00 am & 9.00 pm)" + "\n",
                    normal1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);

            table.addCell(cell);

            cell = new PdfPCell(new Phrase("\n", headerBold));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setRowspan(9);
            cell.setBorder(Rectangle.NO_BORDER);

            table.addCell(cell);

            cell = new PdfPCell(new Phrase(
                    "Benefit Illustration:   SBI LIFE – Smart Money Back Gold (UIN - 111N096V03)" + "\n" + "(Individual, Non-Linked, Participating, Life Insurance Savings Product)", headerBold));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);

            table.addCell(cell);

           /* cell = new PdfPCell(new Phrase(
                    "(Individual, Non-Linked, Participating, Life Insurance Savings Product) ", headerBold));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);

            table.addCell(cell);*/

            cell = new PdfPCell(new Phrase("\n", normal));
            cell.setColspan(3);
            cell.setBorder(Rectangle.BOTTOM);
            cell.setBorderWidth(1.2f);
            table.addCell(cell);

            Paragraph para3 = new Paragraph("Introduction", small_bold);
            para3.setAlignment(Element.ALIGN_LEFT);
            Paragraph para4 = new Paragraph(
                    "Insurance Regulatory & Development Authority of India (IRDAI) requires all life insurance companies operating in India to provide official illustrations to their customers.  The illustrations are based on the investment rates of return set by the Life Insurance Council (constituted under Section 64C(a) of the Insurance Act 1938) and is not intended to reflect the actual investment returns achieved or which may be achieved in future by SBI Life Insurance Company Limited. All life insurance companies use the same rates in their benefit illustrations.",
                    normal1);
            para4.setAlignment(Element.ALIGN_LEFT | Element.ALIGN_JUSTIFIED);
            Paragraph para5 = new Paragraph(
                    "The two rates of investment return declared by the Life Insurance Council are 4% and 8% per annum.",
                    normal1);
            para5.setAlignment(Element.ALIGN_LEFT | Element.ALIGN_JUSTIFIED);
            Paragraph para6 = new Paragraph(
                    "The main objective of the illustration is that the client is able to appreciate the features of the product and the flow of benefits in different circumstances with some level of quantification. For further information on the product and its benefits, please refer to the sales brochure and/or policy document. \n",
                    normal1);
            para6.setAlignment(Element.ALIGN_LEFT | Element.ALIGN_JUSTIFIED);

            Paragraph para31 = new Paragraph("How to read and understand this benefit illustration?", small_bold);
            para31.setAlignment(Element.ALIGN_LEFT);

            Paragraph para81 = new Paragraph(
                    "This benefit illustration is intended to show year-wise premiums payable and benefits under the policy, at two assumed rates of interest i.e., 8% p.a. and 4% p.a.",
                    normal1);
            Paragraph para8 = new Paragraph(
                    "Some benefits are guaranteed and some benefits are variable with returns based on the future performance of your Insurer carrying on life insurance business. If your policy offers guaranteed benefits then these will be clearly marked 'guaranteed' in the illustration table on this page. If your policy offers variable benefits then the illustrations on this page will show two different rates of assumed future investment returns, of 8% p.a. and 4% p.a.. These assumed rates of return are not guaranteed and they are not the upper or lower limits of what you might get back, as the value of your policy is dependent on a number of factors including future investment performance.",
                    normal1);
            para4.setAlignment(Element.ALIGN_LEFT | Element.ALIGN_JUSTIFIED);

            PdfPTable table_proposer_name = new PdfPTable(2);
            // float[] columnWidths_table_proposer_name = { 2f, 2f, 1f, 2f };
            // table_proposer_name.setWidths(columnWidths_table_proposer_name);
            table_proposer_name.setWidthPercentage(100);

            PdfPCell ProposalNumber_cell_1 = new PdfPCell(new Paragraph(
                    "Quotation Number", small_normal));
            PdfPCell ProposalNumber_cell_2 = new PdfPCell(new Paragraph(
                    QuatationNumber, normal1_bold));
            ProposalNumber_cell_2.setHorizontalAlignment(Element.ALIGN_CENTER);

            ProposalNumber_cell_1.setPadding(5);
            ProposalNumber_cell_2.setPadding(5);

            table_proposer_name.addCell(ProposalNumber_cell_1);
            table_proposer_name.addCell(ProposalNumber_cell_2);
            PdfPTable table_proposer_name1 = new PdfPTable(2);
           /* if (proposer_Is_Same_As_Life_Assured.equalsIgnoreCase("n")) {

				// float[] columnWidths_table_proposer_name = { 2f, 2f, 1f, 2f
				// };
				// table_proposer_name.setWidths(columnWidths_table_proposer_name);
				table_proposer_name1.setWidthPercentage(100);

				PdfPCell ProposalNumber1_cell_1 = new PdfPCell(new Paragraph(
						"Proposer Name", small_normal));
				PdfPCell ProposalNumber1_cell_2 = new PdfPCell(new Paragraph(
						name_of_proposer, normal1_bold));
				ProposalNumber1_cell_2
						.setHorizontalAlignment(Element.ALIGN_CENTER);

				ProposalNumber1_cell_1.setPadding(5);
				ProposalNumber1_cell_2.setPadding(5);

				table_proposer_name1.addCell(ProposalNumber1_cell_1);
				table_proposer_name1.addCell(ProposalNumber1_cell_2);

            }*/

            // inputTable here -1

            PdfPTable personalDetail_table = new PdfPTable(10);
            personalDetail_table.setWidths(new float[]{5f, 5f, 5f, 5f, 5f, 5f, 5f, 5f, 5f, 5f});
            personalDetail_table.setWidthPercentage(100f);
            personalDetail_table.setHorizontalAlignment(Element.ALIGN_LEFT);

            // 1st row
            cell = new PdfPCell(new Phrase(
                    "Personal Details : ", normal1_bold));
            cell.setColspan(10);
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            personalDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase("Name of the Prospect/ Policyholder", normal1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            personalDetail_table.addCell(cell);


            // 2 row
            cell = new PdfPCell(new Phrase("Age (last birthday)", normal1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            personalDetail_table.addCell(cell);


            cell = new PdfPCell(new Phrase("Name of the Life Assured", normal1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            personalDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase("Age (last birthday) ", normal1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            personalDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase(" Gender (Life Assured)", normal1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            personalDetail_table.addCell(cell);


            cell = new PdfPCell(new Phrase(" Policy Term(Yrs)", normal1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            personalDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase("Premium Payment Term (Yrs)", normal1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            personalDetail_table.addCell(cell);


            cell = new PdfPCell(new Phrase("Mode of Payment of Premium ",
                    normal1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            personalDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase("Amount of Instalment premium",
                    normal1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            personalDetail_table.addCell(cell);


            cell = new PdfPCell(new Phrase("Applicable Tax Rate (%)", normal1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            personalDetail_table.addCell(cell);

            if (proposer_Is_Same_As_Life_Assured.equalsIgnoreCase("y")) {

                cell = new PdfPCell(new Phrase(name_of_life_assured, normal1));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                personalDetail_table.addCell(cell);

                cell = new PdfPCell(new Phrase(ageAtEntry + "", normal1));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                personalDetail_table.addCell(cell);

                cell = new PdfPCell(new Phrase(name_of_life_assured + "", normal1));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                personalDetail_table.addCell(cell);

                cell = new PdfPCell(new Phrase(ageAtEntry + "", normal1));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                personalDetail_table.addCell(cell);

                cell = new PdfPCell(new Phrase(gender + "", normal1));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                personalDetail_table.addCell(cell);

            } else if (proposer_Is_Same_As_Life_Assured.equalsIgnoreCase("n")) {
                cell = new PdfPCell(new Phrase(name_of_proposer, normal1));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                personalDetail_table.addCell(cell);

                cell = new PdfPCell(new Phrase(strProposerAge + "", normal1));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                personalDetail_table.addCell(cell);

                cell = new PdfPCell(new Phrase(name_of_life_assured + "", normal1));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                personalDetail_table.addCell(cell);

                cell = new PdfPCell(new Phrase(ageAtEntry + "", normal1));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                personalDetail_table.addCell(cell);

                cell = new PdfPCell(new Phrase(gender + "", normal1));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                personalDetail_table.addCell(cell);
            }

            cell = new PdfPCell(new Phrase(policyTerm + "", normal1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            personalDetail_table.addCell(cell);

            // 4 row
            cell = new PdfPCell(new Phrase(PremPaymentTerm + "", normal1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            personalDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase(premPayingMode + "", normal1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            personalDetail_table.addCell(cell);

            /*if (staffdiscount.equalsIgnoreCase("true")) {
				cell = new PdfPCell(new Phrase("Yes", normal1));
			} else {
				cell = new PdfPCell(new Phrase("No", normal1));
            }*/

            cell = new PdfPCell(new Phrase((currencyFormat.format(Double
                    .parseDouble(basicCoverYearlyPremium))) + "", normal1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            personalDetail_table.addCell(cell);

            str_kerla_discount = prsObj.parseXmlTag(input, "str_kerla_discount");


            if (str_kerla_discount.equalsIgnoreCase("Yes")) {
                if (selPremFreq.getSelectedItem().toString().equals("Single")) {
                    cell = new PdfPCell(new Phrase("4.75%", normal1));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    personalDetail_table.addCell(cell);
                } else {
                    cell = new PdfPCell(new Phrase("1st policy year : 4.75% ; 2nd policy year onwards : 2.375%", normal1));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    personalDetail_table.addCell(cell);
                }
            } else {
                if (selPremFreq.getSelectedItem().toString().equals("Single")) {
                    cell = new PdfPCell(new Phrase("4.50%", normal1));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    personalDetail_table.addCell(cell);
                } else {
                    cell = new PdfPCell(new Phrase("1st policy year : 4.5% ; 2nd policy year onwards : 2.25%", normal1));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    personalDetail_table.addCell(cell);
                }
            }

            // J&k
            String isJk = "";
            if (CbJkResident.isChecked()) {
                isJk = "yes";
            }

            PdfPTable table_is_JK = new PdfPTable(2);
            table_is_JK.setWidthPercentage(100);

            PdfPCell cell_is_JK1 = new PdfPCell(new Paragraph("J&K",
                    small_normal));
            cell_is_JK1.setPadding(5);

            PdfPCell cell_is_JK2 = new PdfPCell(new Paragraph(isJk, small_bold));
            cell_is_JK2.setPadding(5);
            cell_is_JK2.setHorizontalAlignment(Element.ALIGN_CENTER);

            table_is_JK.addCell(cell_is_JK1);
            table_is_JK.addCell(cell_is_JK2);

            // Basic Cover

            PdfPTable basicCover_table = new PdfPTable(4);
            basicCover_table.setWidths(new float[]{5f, 5f, 5f, 5f});
            basicCover_table.setWidthPercentage(100f);
            basicCover_table.setHorizontalAlignment(Element.ALIGN_LEFT);

            // 1st row
            cell = new PdfPCell(new Phrase("Policy Details", normal1_bold));
            cell.setColspan(5);
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            basicCover_table.addCell(cell);

            // 2 row
            cell = new PdfPCell(new Phrase("Plan Option", normal1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            basicCover_table.addCell(cell);

            cell = new PdfPCell(new Phrase("Sum Assured Rs.", normal1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            basicCover_table.addCell(cell);

            cell = new PdfPCell(new Phrase("Bonus Type", normal1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            basicCover_table.addCell(cell);


            cell = new PdfPCell(new Phrase("Sum Assured on Death (at inception of the policy) Rs.", normal1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            basicCover_table.addCell(cell);


            // 3 row
            cell = new PdfPCell(new Phrase(planProposedName + "", normal1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            basicCover_table.addCell(cell);


            cell = new PdfPCell(new Phrase((currencyFormat.format(Double
                    .parseDouble(sumAssured))) + "", normal1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            basicCover_table.addCell(cell);

            cell = new PdfPCell(new Phrase("Simple Reversionary Bonus", normal1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            basicCover_table.addCell(cell);


            cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                    .parseDouble(guaranteed_death_benefit_str)) + "", normal1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            basicCover_table.addCell(cell);

            // 5 row


          /*  PdfPTable Table_bouns_type = new PdfPTable(
                    2);
            Table_bouns_type.setWidthPercentage(100);

            PdfPCell cell_bouns_type1 = new PdfPCell(new Paragraph(
                    "Bonus Type", small_normal));
            cell_bouns_type1.setPadding(5);
            PdfPCell cell_bouns_type2 = new PdfPCell(new Paragraph("Simple Reversionary Bonus", small_bold));
            cell_bouns_type2.setPadding(5);
            cell_bouns_type2.setHorizontalAlignment(Element.ALIGN_CENTER);

            Table_bouns_type.addCell(cell_bouns_type1);
            Table_bouns_type.addCell(cell_bouns_type2);

            PdfPTable Table_bouns_type2 = new PdfPTable(
                    2);
            Table_bouns_type2.setWidthPercentage(100);

            PdfPCell cell_bouns_type11 = new PdfPCell(new Paragraph(
                    "Sum Assured on Death (at inception of the policy)   Rs. ", small_normal));
            cell_bouns_type11.setPadding(5);
            PdfPCell cell_bouns_type22 = new PdfPCell(new Paragraph(guaranteed_death_benefit_str, small_bold));
            cell_bouns_type22.setPadding(5);
            cell_bouns_type22.setHorizontalAlignment(Element.ALIGN_CENTER);

            Table_bouns_type2.addCell(cell_bouns_type11);
            Table_bouns_type2.addCell(cell_bouns_type22);
*/

            // Premium Details

            PdfPTable riderDetail_table = new PdfPTable(4);
            riderDetail_table.setWidths(new float[]{9f, 4f, 4f, 4f});
            riderDetail_table.setWidthPercentage(100f);
            riderDetail_table.setHorizontalAlignment(Element.ALIGN_LEFT);

            // 1st row
            cell = new PdfPCell(new Phrase("Riders", normal1_bold));
            cell.setColspan(4);
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            riderDetail_table.addCell(cell);

            // 2 row
            cell = new PdfPCell(new Phrase("", normal1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            riderDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase("Term(yrs)", normal1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            riderDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase("Sum Assured(Rs)", normal1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            riderDetail_table.addCell(cell);
            //cell = new PdfPCell(new Phrase("Yearly (Rs)", normal1));
            cell = new PdfPCell(new Phrase(premPayingMode + " (Rs)", normal1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            riderDetail_table.addCell(cell);

            // 3rd
            if (ptRiderStatus.equalsIgnoreCase("true")) {
                cell = new PdfPCell(new Phrase(
                        "SBI Life - Preferred Term Rider (UIN: 111B014V02)",
                        normal1));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                riderDetail_table.addCell(cell);

                cell = new PdfPCell(new Phrase(ptRiderTerm + "", normal1));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                riderDetail_table.addCell(cell);

                cell = new PdfPCell(new Phrase((currencyFormat.format(Double
                        .parseDouble(ptSA))), normal1));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                riderDetail_table.addCell(cell);

                cell = new PdfPCell(new Phrase((currencyFormat.format(Double
                        .parseDouble(ptRiderYearly + ""))), normal1));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                riderDetail_table.addCell(cell);
            }

            // 4rd
            if (adbRiderStatus.equalsIgnoreCase("true")) {
                cell = new PdfPCell(
                        new Phrase(
                                "SBI Life - Accidental Death Benefit (ADB) Rider (UIN: 111B015V02)",
                                normal1));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                riderDetail_table.addCell(cell);

                cell = new PdfPCell(new Phrase(adbRiderTerm + "", normal1));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                riderDetail_table.addCell(cell);

                cell = new PdfPCell(new Phrase((currencyFormat.format(Double
                        .parseDouble(adbSA))) + "", normal1));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                riderDetail_table.addCell(cell);

                cell = new PdfPCell(new Phrase((currencyFormat.format(Double
                        .parseDouble(adbRiderYearly + ""))), normal1));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                riderDetail_table.addCell(cell);
            }

            // 4rd
            if (atpdbRiderStatus.equalsIgnoreCase("true")) {
                cell = new PdfPCell(
                        new Phrase(
                                "SBI Life - Accidental Total & Permanent Disability (ATPD) Benefit Rider (UIN: 111B016V02)",
                                normal1));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                riderDetail_table.addCell(cell);

                cell = new PdfPCell(new Phrase(atpdbRiderTerm + "", normal1));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                riderDetail_table.addCell(cell);

                cell = new PdfPCell(new Phrase((currencyFormat.format(Double
                        .parseDouble(atpdbSA))) + "", normal1));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                riderDetail_table.addCell(cell);

                cell = new PdfPCell(new Phrase((currencyFormat.format(Double
                        .parseDouble(atpdbRiderYearly + ""))), normal1));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                riderDetail_table.addCell(cell);
            }

            // 4rd
            if (ccnlRiderStatus.equalsIgnoreCase("true")) {
                cell = new PdfPCell(
                        new Phrase(
                                "SBI Life - Criti Care 13 Non - Linked Rider(UIN : 111B025V02)",
                                normal1));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                riderDetail_table.addCell(cell);

                cell = new PdfPCell(new Phrase(ccnlRiderTerm + "", normal1));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                riderDetail_table.addCell(cell);

                cell = new PdfPCell(new Phrase((currencyFormat.format(Double
                        .parseDouble(ccnlSA))) + "", normal1));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                riderDetail_table.addCell(cell);

                cell = new PdfPCell(new Phrase((currencyFormat.format(Double
                        .parseDouble(ccnlRiderYearly + ""))), normal1));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                riderDetail_table.addCell(cell);
            }

            cell = new PdfPCell(new Phrase("Total Rider Premium", normal1));
            cell.setColspan(3);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            riderDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase(totalRiderYearly, normal1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            riderDetail_table.addCell(cell);

            // Premium Details

            PdfPTable premDetail_table = new PdfPTable(3);
            premDetail_table.setWidths(new float[]{5f, 5f, 5f});
            premDetail_table.setWidthPercentage(100f);
            premDetail_table.setHorizontalAlignment(Element.ALIGN_LEFT);

            // 1st row
   /*         cell = new PdfPCell(
					new Phrase(
							"Total Premium for Base Product and Rider (if any) (in Rs.)",
							normal1_bold));
			cell.setColspan(3);
			cell.setBorder(Rectangle.BOTTOM);
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			premDetail_table.addCell(cell);

			// 2 row
			cell = new PdfPCell(new Phrase("", normal1));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			premDetail_table.addCell(cell);

			cell = new PdfPCell(new Phrase("", normal1));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			premDetail_table.addCell(cell);

			cell = new PdfPCell(new Phrase(premPayingMode + "", normal1));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			premDetail_table.addCell(cell);

			System.out.println("Plan " + planProposedName.substring(6, 8));
			// 3 row
			cell = new PdfPCell(new Phrase(premPayingMode
					+ " Installment Premium", normal1));
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			premDetail_table.addCell(cell);

			cell = new PdfPCell(new Phrase("Plan"
					+ planProposedName.substring(6, 8), normal1));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			premDetail_table.addCell(cell);

			cell = new PdfPCell(new Phrase((currencyFormat.format(Double
					.parseDouble(totalPremiumYearlyInstallmentPremYearly)))
					+ "", normal1));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			premDetail_table.addCell(cell);

			cell = new PdfPCell(new Phrase("Applicable Taxes", normal1));
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			premDetail_table.addCell(cell);

			cell = new PdfPCell(new Phrase("Plan "
					+ planProposedName.substring(6, 8), normal1));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			premDetail_table.addCell(cell);

			cell = new PdfPCell(new Phrase((currencyFormat.format(Double
					.parseDouble(totalPremiumServiceTaxYearly))) + "", normal1));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			premDetail_table.addCell(cell);

			cell = new PdfPCell(new Phrase(premPayingMode
					+ " Installment Premium with Applicable Taxes", normal1));
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			premDetail_table.addCell(cell);

			cell = new PdfPCell(new Phrase("Plan "
					+ planProposedName.substring(6, 8), normal1));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			premDetail_table.addCell(cell);

			cell = new PdfPCell(
					new Phrase(
							(currencyFormat.format(Double
									.parseDouble(totalPremiumYearlyInstallmentPremWithServiceTaxYearly)))
									+ "", normal1));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            premDetail_table.addCell(cell);*/

            // PdfPTable FY_SY_premDetail_table = new PdfPTable(7);
            // FY_SY_premDetail_table.setWidths(new float[] { 5f, 5f, 5f, 5f,
            // 5f,
            // 5f, 5f });
            PdfPTable FY_SY_premDetail_table = new PdfPTable(4);
            FY_SY_premDetail_table.setWidths(new float[]{5f, 5f, 5f, 5f});
            FY_SY_premDetail_table.setWidthPercentage(100f);
            FY_SY_premDetail_table.setHorizontalAlignment(Element.ALIGN_LEFT);

            // 1st row
            cell = new PdfPCell(
                    new Phrase(
                            "Premium Summary:  ",
                            small_bold));
            cell.setColspan(7);
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            FY_SY_premDetail_table.addCell(cell);

            // 2 row
            cell = new PdfPCell(new Phrase("Policy Year", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            FY_SY_premDetail_table.addCell(cell);

            cell = new PdfPCell(
                    new Phrase("Base Plan   (Rs.)", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            FY_SY_premDetail_table.addCell(cell);

            // cell = new PdfPCell(new Phrase("(a)Basic Applicable Taxes(Rs.)",
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

            cell = new PdfPCell(new Phrase("Riders   (Rs.)", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            FY_SY_premDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase(
                    "Total Instalment Premium (Rs.)", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            FY_SY_premDetail_table.addCell(cell);


            cell = new PdfPCell(new Phrase("Instalment Premium without applicable taxes", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            FY_SY_premDetail_table.addCell(cell);

            cell = new PdfPCell(
                    new Phrase(""
                            + getformatedThousandString(Integer.parseInt(commonForAllProd
                            .getRound(commonForAllProd.getStringWithout_E(Double
                                    .valueOf(totalPremiumYearlyInstallmentPremYearly
                                            .equals("") ? "0"
                                            : totalPremiumYearlyInstallmentPremYearly))))),
                            small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            FY_SY_premDetail_table.addCell(cell);


            cell = new PdfPCell(new Phrase("0", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            FY_SY_premDetail_table.addCell(cell);

            cell = new PdfPCell(
                    new Phrase(""
                            + getformatedThousandString(Integer.parseInt(commonForAllProd
                            .getRound(commonForAllProd.getStringWithout_E(Double
                                    .valueOf(totalPremiumYearlyInstallmentPremYearly
                                            .equals("") ? "0"
                                            : totalPremiumYearlyInstallmentPremYearly))))),
                            small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            FY_SY_premDetail_table.addCell(cell);


            // 3 row
            cell = new PdfPCell(new Phrase("Instalment Premium with applicable taxes for 1st Year", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            FY_SY_premDetail_table.addCell(cell);


            cell = new PdfPCell(
                    new Phrase(""
                            + getformatedThousandString(Integer.parseInt(commonForAllProd
                            .getRound(commonForAllProd.getStringWithout_E(Double
                                    .valueOf(totalPremiumYearlyInstallmentPremWithServiceTaxYearly
                                            .equals("") ? "0"
                                            : totalPremiumYearlyInstallmentPremWithServiceTaxYearly))))),
                            small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            FY_SY_premDetail_table.addCell(cell);


            cell = new PdfPCell(new Phrase("0", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            FY_SY_premDetail_table.addCell(cell);

            cell = new PdfPCell(
                    new Phrase(""
                            + getformatedThousandString(Integer.parseInt(commonForAllProd
                            .getRound(commonForAllProd.getStringWithout_E(Double
                                    .valueOf(totalPremiumYearlyInstallmentPremWithServiceTaxYearly
                                            .equals("") ? "0"
                                            : totalPremiumYearlyInstallmentPremWithServiceTaxYearly))))),
                            small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            FY_SY_premDetail_table.addCell(cell);

            // 4 row
            if (!smartMoneyBackGoldBean.getPremiumFreq().equalsIgnoreCase(
                    "Single")) {
                cell = new PdfPCell(new Phrase("Instalment Premium with applicable taxes for 2nd Year onwards",
                        small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                FY_SY_premDetail_table.addCell(cell);

                cell = new PdfPCell(
                        new Phrase(""
                                + getformatedThousandString(Integer.parseInt(commonForAllProd
                                .getRound(commonForAllProd.getStringWithout_E(Double
                                        .valueOf(premWthSTSecondYear
                                                .equals("") ? "0"
                                                : premWthSTSecondYear))))),
                                small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                FY_SY_premDetail_table.addCell(cell);

                cell = new PdfPCell(
                        new Phrase("0",
                                small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                FY_SY_premDetail_table.addCell(cell);

                cell = new PdfPCell(
                        new Phrase(""
                                + getformatedThousandString(Integer.parseInt(commonForAllProd
                                .getRound(commonForAllProd.getStringWithout_E(Double
                                        .valueOf(premWthSTSecondYear
                                                .equals("") ? "0"
                                                : premWthSTSecondYear))))),
                                small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                FY_SY_premDetail_table.addCell(cell);
            }

            if (premPayingMode.equalsIgnoreCase("Single")) {
                // System.out.println("Plan "+plan.substring(6, 8));
                // 3 row
                cell = new PdfPCell(new Phrase("Instalment Premium with applicable taxes for 2nd Year onwards", small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                FY_SY_premDetail_table.addCell(cell);


                cell = new PdfPCell(
                        new Phrase("Not Applicable"
                                ,
                                small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                FY_SY_premDetail_table.addCell(cell);


                cell = new PdfPCell(new Phrase("Not Applicable", small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                FY_SY_premDetail_table.addCell(cell);

                cell = new PdfPCell(
                        new Phrase("Not Applicable"
                                ,
                                small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                FY_SY_premDetail_table.addCell(cell);
            }

            // document.add(FY_SY_premDetail_table);
            /* Added By - Priyanka Warekar - 26-08-2015 - End ****/

            PdfPTable Table_backdating_premium_with_service_tax = new PdfPTable(
                    2);
            Table_backdating_premium_with_service_tax.setWidthPercentage(100);

            PdfPCell cell_Backdate1 = new PdfPCell(new Paragraph(
                    "Backdating Interest", small_normal));
            cell_Backdate1.setPadding(5);
            PdfPCell cell_Backdate2 = new PdfPCell(new Paragraph("Rs. "
                    + commonForAllProd.getRound(commonForAllProd
                    .getStringWithout_E(Double.valueOf(BackdatingInt
                            .equals("") ? "0" : BackdatingInt))),
                    small_bold));
            cell_Backdate2.setPadding(5);
            cell_Backdate2.setHorizontalAlignment(Element.ALIGN_CENTER);

            Table_backdating_premium_with_service_tax.addCell(cell_Backdate1);
            Table_backdating_premium_with_service_tax.addCell(cell_Backdate2);

            Paragraph note = new Paragraph("Please Note: ",
                    normal1_BoldUnderline);
            note.setAlignment(Element.ALIGN_LEFT);
            Paragraph note_1 = new Paragraph(
                    "1. The premiums can be also paid by giving standing instruction to your bank or you can pay through your credit card.",
                    normal1);
            note_1.setAlignment(Element.ALIGN_LEFT | Element.ALIGN_JUSTIFIED);
            Paragraph note_2 = new Paragraph(
                    "2. Tax deduction under Section 80 C is available. However in case the premium paid during the financial year, exceeds 10% of the sum assured, the benefit will be limited up to 10% of the sum assured. Tax exemption under Section 10(10D) is available at the time of maturity/surrender, subject to the premium not exceeding 10% of the sum assured in any of the years during the term of the policy. However, death proceeds are completely exempt.",
                    normal1);
            note_2.setAlignment(Element.ALIGN_LEFT | Element.ALIGN_JUSTIFIED);
            Paragraph note_3 = new Paragraph(
                    "2. Applicable Taxes (including surcharge/cess etc), at the rate notified by the Central Government/ State Government / Union Territories of India from time to time and as per the provisions of the prevalent tax laws will be payable on premium/ or any other charges as per the product features.",
                    normal1);
            note_3.setAlignment(Element.ALIGN_LEFT | Element.ALIGN_JUSTIFIED);
            Paragraph note_4 = new Paragraph(
                    "4.Tax deduction under Section 80 C is available. However in case the premium paid during the financial year, exceeds 10% of the sum assured, the benefit will be limited up to 10% of the sum assured. Tax exemption under Section 10(10D) is available at the time of maturity/surrender, subject to the premium not exceeding 10% of the sum assured in any of the years during the term of the policy. However, death proceeds are completely exempt. Tax benefits, are as per the provisions of the Income Tax laws & are subject to change from time to time. Please consult your tax advisor for details.",
                    normal1);
            note_4.setAlignment(Element.ALIGN_LEFT | Element.ALIGN_JUSTIFIED);


            Paragraph termsCondition = new Paragraph(
                    "Other Terms and Conditions", normal1_bold);
            termsCondition.setAlignment(Element.ALIGN_LEFT);
            Paragraph terms_1 = new Paragraph(
                    "1. The benefit calculation is based on the age herein indicated and as applicable for healthy individual.",
                    normal1);
            terms_1.setAlignment(Element.ALIGN_LEFT | Element.ALIGN_JUSTIFIED);
            Paragraph terms_2 = new Paragraph(
                    "2. The above BI is subject to payment of stipulated premiums on due date.",
                    normal1);
            terms_2.setAlignment(Element.ALIGN_LEFT | Element.ALIGN_JUSTIFIED);
            // Paragraph terms_3 = new Paragraph(
            // "3. Insurance is subject matter of solicitation.", normal1);
            // terms_3.setAlignment(Element.ALIGN_LEFT |
            // Element.ALIGN_JUSTIFIED);
            Paragraph terms_4 = new Paragraph(
                    "3. If riders are applicable, please refer to specific rider benefit.",
                    normal1);
            terms_4.setAlignment(Element.ALIGN_LEFT | Element.ALIGN_JUSTIFIED);

            // Table here

            PdfPTable table1 = new PdfPTable(17);
            table1.setWidths(new float[]{2f, 5f, 5f, 5f, 5f, 5f, 5f, 5f, 5f,
                    5f, 5f, 5f, 5f, 5f, 5f, 5f, 5f});
            table1.setWidthPercentage(100);

            // 1st row
            cell = new PdfPCell(
                    new Phrase(
                            "BENEFIT ILLUSTRATION FOR SBI LIFE - Smart Money Back Gold (Amount in Rs.)",
                            normal1_bold));
            cell.setColspan(17);
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table1.addCell(cell);

            // 2nd row
            cell = new PdfPCell(new Phrase("Policy Year", normal_bold));
            cell.setRowspan(3);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);
            if (premPayingMode.equalsIgnoreCase("Single")) {
                cell = new PdfPCell(new Phrase(
                        "Single premium", normal_bold));
            } else {
                cell = new PdfPCell(new Phrase(
                        "Annualized premium", normal_bold));
            }


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
            cell.setColspan(3);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);

            cell = new PdfPCell(
                    new Phrase("Non-Guaranteed Benefits @ 8% p.a.", normal_bold));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setColspan(3);
            table1.addCell(cell);

            // 3rd
            cell = new PdfPCell(new Phrase("Total benefits Including Guaranteed and Non-Guaranteed benefits", normal_bold));
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

            cell = new PdfPCell(new Phrase("Total Maturity benefit, incl. Terminal bonus, if any, @ 4% (7+8+9)", normal_bold));
            cell.setRowspan(1);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);
            cell = new PdfPCell(new Phrase("Total Maturity benefit, incl. Terminal bonus, if any, @ 8% (7+11+12)", normal_bold));
            cell.setRowspan(1);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);
            cell = new PdfPCell(new Phrase("Total Death benefit, incl. Terminal bonus, if any, @ 4% (6+8+9)", normal_bold));
            cell.setRowspan(1);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);

            cell = new PdfPCell(new Phrase("Total Death benefit, incl. Terminal bonus, if any, @ 8% (6+11+12)", normal_bold));
            cell.setRowspan(1);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);


            cell = new PdfPCell(new Phrase("1", normal));
            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                    | Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table1.addCell(cell);

            cell = new PdfPCell(new Phrase(
                    "2", normal));
            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                    | Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table1.addCell(cell);


            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell = new PdfPCell(new Phrase("3", normal));
            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                    | Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table1.addCell(cell);


            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell = new PdfPCell(new Phrase("4", normal));
            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                    | Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table1.addCell(cell);


            cell = new PdfPCell(new Phrase("5", normal));
            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                    | Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table1.addCell(cell);


            cell = new PdfPCell(new Phrase("6", normal));
            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                    | Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table1.addCell(cell);


            cell = new PdfPCell(new Phrase("7", normal));
            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                    | Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table1.addCell(cell);


            cell = new PdfPCell(new Phrase("8",
                    normal));
            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                    | Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table1.addCell(cell);


              /*  cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                        .parseDouble(list_data.get(i)
                                .getCash_bonus_4per())),
                        normal));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                        | Rectangle.BOTTOM);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table1.addCell(cell);*/

            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell = new PdfPCell(new Phrase("9", normal));
            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                    | Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table1.addCell(cell);


            cell = new PdfPCell(new Phrase("10",
                    normal));
            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                    | Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table1.addCell(cell);


            cell = new PdfPCell(new Phrase("11",
                    normal));
            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                    | Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table1.addCell(cell);


              /*  cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                        .parseDouble(list_data.get(i)
                                .getCash_bonus_8per())),
                        normal));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                        | Rectangle.BOTTOM);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table1.addCell(cell);*/

            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell = new PdfPCell(new Phrase("12", normal));
            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                    | Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table1.addCell(cell);


            cell = new PdfPCell(new Phrase("13",
                    normal));
            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                    | Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table1.addCell(cell);


            cell = new PdfPCell(new Phrase("14",
                    normal));
            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                    | Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table1.addCell(cell);


            cell = new PdfPCell(new Phrase("15",
                    normal));
            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                    | Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table1.addCell(cell);


            cell = new PdfPCell(new Phrase("16",
                    normal));
            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                    | Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table1.addCell(cell);
            cell = new PdfPCell(new Phrase("17",
                    normal));
            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                    | Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
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


              /*  cell = new PdfPCell(new Phrase(currencyFormat.format(Double
						.parseDouble(list_data.get(i)
								.getNot_guaranteed_death_benefit_8per())),
						normal));
				cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
						| Rectangle.BOTTOM);
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table1.addCell(cell);*/

                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell = new PdfPCell(new Phrase("0", normal));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                        | Rectangle.BOTTOM);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table1.addCell(cell);


                cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                        .parseDouble(list_data.get(i)
                                .getNot_guaranteed_surrender_value_4per())),
                        normal));
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


              /*  cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                        .parseDouble(list_data.get(i)
                                .getCash_bonus_8per())),
						normal));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                        | Rectangle.BOTTOM);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table1.addCell(cell);*/

                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell = new PdfPCell(new Phrase("0", normal));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                        | Rectangle.BOTTOM);
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

            Paragraph benefitsofDeath = new Paragraph(
                    "Notes:  Annualized premium excludes underwriting extra premium, frequency loadings on premiums, the premiums paid towards the riders, if any / Single premium shall be the premium amount payable in lumpsum at inception of the policy as chosen by the policyholder, excluding the taxes and underwriting extra premiums, if any. Refer sales literature for explanation of terms used in this illustration.", small_bold);
            benefitsofDeath.setAlignment(Element.ALIGN_LEFT);

            Chunk normaltext = new Chunk("Higher of (i)Sum Assured on Death ", small_normal);
            Chunk superScript = new Chunk("#", small_normal);
            superScript.setTextRise(1f);
            Chunk moreNormalText = new Chunk(" + Vested Simple Reversionary Bonuses + Terminal bonus, if any. OR ii) 105% of all the basic premiums paid.", small_normal);

            Paragraph benefitsofDeath1 = new Paragraph();
            benefitsofDeath1.add(normaltext);
            benefitsofDeath1.add(superScript);
            benefitsofDeath1.add(moreNormalText);

            benefitsofDeath.setAlignment(Element.ALIGN_LEFT
                    | Element.ALIGN_JUSTIFIED);
            Paragraph benefitsofDeath2 = new Paragraph(
                    "#For details on Sum Assured on death please refer the Sales Brochure.",
                    small_normal);
            benefitsofDeath2.setAlignment(Element.ALIGN_LEFT
                    | Element.ALIGN_JUSTIFIED);

            Paragraph bonusRate = new Paragraph("Bonus Rates", small_bold);
            bonusRate.setAlignment(Element.ALIGN_LEFT);
            Paragraph bonusRate1 = new Paragraph(
                    "This is a with profit plan and participates in the profits of the company’s life insurance business. Simple reversionary bonuses are declared as a percentage rate, which apply to the sum assured of the basic policy. Terminal bonuses, if any, are declared as a percentage rate, which apply to the vested reversionary bonus. The bonus rates in the benefit illustration are constant. However, in practice, future bonuses are not guaranteed and will depend on future profits. Therefore, the bonuses are shown as non-guaranteed benefits and are calculated so that they are consistent with the two projected investment return assumptions of 4% and 8% per annum. Accordingly, for the purpose of guaranteed surrender value (GSV) in this illustration, the cash values of vested bonuses are not considered at all.",
                    normal1);
            bonusRate1.setAlignment(Element.ALIGN_LEFT
                    | Element.ALIGN_JUSTIFIED);
            Paragraph bonusRate2 = new Paragraph(
                    "Simple reversionary bonuses are declared as a percentage rate, which apply to the sum assured of the basic policy. Once declared, they form a part of the guaranteed benefits of the plan. Terminal bonuses, if any, are declared as a percentage rate, which apply to the vested bonus.",
                    normal1);
            bonusRate2.setAlignment(Element.ALIGN_LEFT
                    | Element.ALIGN_JUSTIFIED);
            Paragraph bonusRate3 = new Paragraph(
                    "The bonus rates in the benefit illustration are constant. However, in practice, future bonuses are not guaranteed and will depend on future profits. Therefore, the bonuses are shown as non-guaranteed benefits and are calculated so that they are consistent with the two projected investment return assumptions of 4% and 8% per annum.",
                    normal1);
            bonusRate3.setAlignment(Element.ALIGN_LEFT
                    | Element.ALIGN_JUSTIFIED);
            Paragraph bonusRate4 = new Paragraph(
                    "Accordingly, for the purpose of guaranteed Surrender value (GSV) in this illustration, the cash value of vested bonuses are not considered at all.",
                    normal1);
            bonusRate4.setAlignment(Element.ALIGN_LEFT
                    | Element.ALIGN_JUSTIFIED);

            Paragraph surrender = new Paragraph("Surrender Value", small_bold);
            surrender.setAlignment(Element.ALIGN_LEFT);
            Paragraph surrender_value = new Paragraph(
                    "Surrender value is available on the basic policy benefit and not on the rider benefits.",
                    normal1);
            surrender_value.setAlignment(Element.ALIGN_LEFT
                    | Element.ALIGN_JUSTIFIED);

            Paragraph guarnteedSurrender = new Paragraph(
                    "Guaranteed Surrender Value", small_bold);
            guarnteedSurrender.setAlignment(Element.ALIGN_LEFT);
            Paragraph guarnteedSurrender_value1 = new Paragraph(
                    "The policy will acquire a paid-up and/or surrender value only if premiums have been paid for at least 3 full years.",
                    normal1);
            guarnteedSurrender_value1.setAlignment(Element.ALIGN_LEFT
                    | Element.ALIGN_JUSTIFIED);
            Paragraph guarnteedSurrender_value2 = new Paragraph(
                    "The Guaranteed Surrender Value (GSV) in case of regular premium policies will be equal to GSV factors multiplied by the basic premiums paid. Basic premium is equal to total premium less Applicable Taxes, underwriting extra premiums, extra premium due to modal factors and rider premiums, if any.",
                    normal1);
            guarnteedSurrender_value2.setAlignment(Element.ALIGN_LEFT
                    | Element.ALIGN_JUSTIFIED);

            Paragraph policySurrender = new Paragraph(
                    "Company's Policy on Surrender", small_bold);
            policySurrender.setAlignment(Element.ALIGN_LEFT);
            Paragraph policySurrender_value = new Paragraph(
                    "In practice, the company may pay a surrender value which could be higher than the guaranteed surrender value. The benefits payable on surrender reflects the value of your policy, which is assessed based on the past financial/demographic experience of the company with regard to your policy/group of similar policies, as well as the likely future experience. The surrender value payable may be reviewed from time to time depending on company's experience of the various factors which impact the surrender values that may be paid.",
                    normal1);
            policySurrender_value.setAlignment(Element.ALIGN_LEFT
                    | Element.ALIGN_JUSTIFIED);

            Paragraph para9 = new Paragraph(
                    "I,_____________________________________________, , have understood the term and conditions ,product features and applicable charges (if any) before purchase of the contract, after receipt of all information stated above from the insurer.",
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
            Paragraph new_line = new Paragraph("\n");

            document.add(table);

            document.add(table_proposer_name);
            if (proposer_Is_Same_As_Life_Assured.equalsIgnoreCase("n")) {
                document.add(table_proposer_name1);
            }
            document.add(new_line);

            // document.add(ls);
            document.add(para3);
            document.add(para4);
            //document.add(para5);
            document.add(para6);


            document.add(new_line);

            // document.add(main_table);


            document.add(personalDetail_table);
            document.add(new_line);
            document.add(para31);
            document.add(para81);
            document.add(para8);

            document.add(new_line);
            document.add(basicCover_table);
            //document.add(Table_bouns_type);
            // document.add(Table_bouns_type2);
            if (atpdbRiderStatus.equalsIgnoreCase("true")
                    || ptRiderStatus.equalsIgnoreCase("true")
                    || adbRiderStatus.equalsIgnoreCase("true")
                    || ccnlRiderStatus.equalsIgnoreCase("true"))
                document.add(riderDetail_table);
            document.add(premDetail_table);
            document.add(new_line);
            if (CbJkResident.isChecked()) {
                document.add(table_is_JK);
            }
            document.add(new_line);
            document.add(FY_SY_premDetail_table);
            document.add(new_line);
            //document.add(Table_backdating_premium_with_service_tax);
            document.add(new_line);
            document.add(note);
            document.add(note_1);
            //document.add(note_2);
            document.add(note_3);
            //document.add(note_4);

            document.add(new_line);
            //document.add(termsCondition);
            //document.add(terms_1);
            //document.add(terms_2);
            // document.add(terms_3);
            //document.add(terms_4);

            document.add(new_line);

            document.add(new_line);

            document.add(table1);

            document.add(new_line);
            document.add(benefitsofDeath);
            //document.add(benefitsofDeath1);
            //document.add(benefitsofDeath2);

            document.add(new_line);
            document.add(bonusRate);
            document.add(bonusRate1);
            //document.add(bonusRate2);
            // document.add(bonusRate3);
            //document.add(bonusRate4);

            //document.add(new_line);
            //document.add(surrender);
            //document.add(surrender_value);

            document.add(new_line);

            // document.add(para9);

            document.add(new_line);

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
                                    + "     , having received the information with respect to the above, have understood the above statement before entering into the contract.",
                            small_bold));

            BI_Pdftable26_cell1.setPadding(5);

            BI_Pdftable26.addCell(BI_Pdftable26_cell1);
            document.add(BI_Pdftable26);

            document.add(new_line);
            if (!bankUserType.equalsIgnoreCase("Y")) {
                PdfPTable BI_PdftablePolicyHolder = new PdfPTable(1);
                BI_PdftablePolicyHolder.setWidthPercentage(100);
                PdfPCell BI_PdftablePolicyHolder_signature_cell = new PdfPCell(
                        new Paragraph("Signature of Prospect/Policyholder", small_bold));

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


                PdfPTable BI_Pdftable27 = new PdfPTable(1);
                BI_Pdftable27.setWidthPercentage(100);
                PdfPCell BI_Pdftable27_cell11 = new PdfPCell(
                        new Paragraph(
                                "I, "
                                        + commonMethods.getUserName(context)
                                        + "     ,  have explained the premiums and benefits under the product fully to the prospect/ policyholder.",
                                small_bold));

                BI_Pdftable27_cell11.setPadding(5);

                BI_Pdftable27.addCell(BI_Pdftable27_cell11);
                document.add(BI_Pdftable27);

           /* M_ChannelDetails list_channel_detail = db.getChannelDetail(sr_Code);
            String code = list_channel_detail.getCode();
            String name = list_channel_detail.getName();
            String str_sign_header = "";
            if (str_usertype.equals("AGENT")) {
                str_sign_header = "(IA code- " + code + ")" + "\n"
                        + "Name of IA- " + name + "\n"
                        + "Authenticated by Id & Password";
            } else if (str_usertype.equals("CIF")) {
                str_sign_header = "(CIF code- " + code + ")" + "\n"
                        + "Name of CIF- " + name + "\n"
                        + "Authenticated by Id & Password";

            } else if (str_usertype.equals("BAP")) {
                str_sign_header = "(BAP code- " + code + ")" + "\n"
                        + "Name of Broker- " + name + "\n"
                        + "Authenticated by Id & Password";

            } else if (str_usertype.equals("CAG")) {
                str_sign_header = "(CAG code- " + code + ")" + "\n"
                        + "Name of Corporate Agent- " + name + "\n"
                        + "Authenticated by Id & Password";

            } else if (str_usertype.equals("IMF")) {
                str_sign_header = "(ISP code- " + code + ")" + "\n"
                        + "ISP Name- " + name + "\n"
                        + "Authenticated by Id & Password";

            }*/
                PdfPTable BI_PdftableMarketing_signature = new PdfPTable(3);

                BI_PdftableMarketing_signature.setWidthPercentage(100);

                PdfPCell BI_PdftableMarketing_signature_1 = new PdfPCell(
                        new Paragraph("Place :" + place2, small_normal));
                PdfPCell BI_PdftableMarketing_signature_2 = new PdfPCell(
                        new Paragraph("Date  :" + CurrentDate, small_normal));

                byte[] fbyt_agent = Base64.decode(agent_sign, 0);
                Bitmap Agentbitmap = BitmapFactory.decodeByteArray(fbyt_agent,
                        0, fbyt_agent.length);

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
            // document.add(table4);

            document.close();

            return true;
        } catch (Exception e) {
            // TODO: handle exception
            // Log.e(getLocalClassName(), e.toString() +
            // "Error in creating Pdf");
            System.out.println("error " + e.getMessage());
            e.printStackTrace();
            // Toast.makeText(getApplicationContext(), e.getMessage(),
            // Toast.LENGTH_SHORT);
            return false;

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
