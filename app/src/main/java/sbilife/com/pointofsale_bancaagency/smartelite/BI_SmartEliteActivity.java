package sbilife.com.pointofsale_bancaagency.smartelite;

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
public class BI_SmartEliteActivity extends AppCompatActivity implements
        OnEditorActionListener {

    private NeedAnalysisBIService NABIObj;
    private NA_CBI_bean na_cbi_bean;
    private File needAnalysispath, newFile;
    private String agentcode, agentMobile, agentEmail, userType;
    private int needAnalysis_flag = 0;
    private String na_input = null;
    private String na_output = null;
    private DatabaseHelper dbHelper;
    private String na_dob = "";
    private int flag1 = 0;


    private String premiumAmount;
    /* For DashBoard */
    private int flag;
    private String QuatationNumber = "";
    private String planName = "";

    // UI Elements
    private CheckBox isStaffDisc, selViewSFIN;
    private Spinner ageInYears, selGender, selPremiumFrequncyMode, selPolicyTerm,
            selPremFreq, selPlan, premPayingTerm,
            spnr_bi_smart_elite_proposer_title;
    private EditText edt_bi_smart_elite_proposer_first_name,
            edt_bi_smart_elite_proposer_middle_name,
            edt_bi_smart_elite_proposer_last_name, premiumAmt, SAMF,
            noOfYearsElapsedSinceInception, percent_EquityEliteIIFund,
            percent_BalancedFund, percent_BondFund, percent_MoneyMarketFund,
            Percent_PureFund,
            Percent_MidcapFund,
            Percent_BondOptimiserFund,
            percent_CorporateBondFund;

    private EditText edt_bi_smart_elite_life_assured_first_name,
            edt_bi_smart_elite_life_assured_middle_name,
            edt_bi_smart_elite_life_assured_last_name;

    private TableRow tv_monthly_mode;

    private Spinner spnr_bi_smart_elite_life_assured_title;

    private Button btn_bi_smart_elite_life_assured_date;

    private TextView txt_premium_frequency_mode, txt_premPayingTerm, help_premiumAmt,
            help_SAMF, help_noOfYearsElapsedSinceInception;
    private Button btnSubmit, back;

    private RadioButton rb_smart_elite_proposer_same_as_life_assured_yes,
            rb_smart_elite_proposer_same_as_life_assured_no;
    private TableRow tbEquityEliteIIFund, tbBondFund, tbBalancedFund, tbSFINCorporateBondFund, tbIndexFund,
            tbMoneyMarketFund, tbPEManaged, tableRow7;

    private TableRow tr_smart_elite_proposer_detail2, tr_smart_elite_proposer_detail1;
    // Class Declaration
    private CommonForAllProd commonForAllProd;
    private BI_SmartEliteProperties prop;
    private BI_SmartEliteBean smartEliteBean;
    private File mypath;
    // Variable Declaration
    private DecimalFormat currencyFormat;
    private AlertDialog.Builder showAlert;
    private String effectivePremium = "0";
    private int PF = 0;
    private String proposer_Title = "", proposer_First_Name = "",
            proposer_Middle_Name = "", proposer_Last_Name = "";

    private String lifeAssured_Title = "", lifeAssured_First_Name = "",
            lifeAssured_Middle_Name = "", lifeAssured_Last_Name = "";

    String output = "";
    private String input = "";
    private String netYield4 = "";
    private String netYield8 = "";

    // For Bi Dialog
    private ParseXML prsObj;
    private String name_of_proposer = "";
    private String name_of_life_assured = "";
    private String name_of_person = "";
    private String place2 = "";
    private String date1 = "";
    private String date2 = "";
    private String agent_sign = "";
    private String proposer_sign = "";

    private Button btn_MarketingOfficalDate;
    private Button btn_PolicyholderDate;

    private ImageButton Ibtn_signatureofMarketing;
    private ImageButton Ibtn_signatureofPolicyHolders;

    /* String For BI Grid */
    private String gender, age_entry, maturity_age, annualised_premium, policy_term,
            sum_assured, plan, premium_paying_term,
            accidental_benefit_sumassured, percentage_equity_elite_fund3,
            percentage_bond_fund2, percentage_balanced_fund2,
            percentage_money_market_fund2, percent_PureFund,
            percent_MidcapFund,
            percent_BondOptimiserFund2,
            perInvCorporateBondFund,
            percent_EquityFund, no_of_year_elapsed,
            red_in_year_at_8_no_of_year, red_in_year_at_8_maturity_age;
    private String premFreqMod = "";
    private String AnnualInstallment = "";
    private final String premFreq = "";
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
    private StringBuilder retVal;
    private StringBuilder inputVal;
    private StringBuilder bussIll = null;

    /*
     * List for getting value from database to display on grid depending upon
     * policy term
     */
    List<M_BI_EliteGrid_AdapterCommon> list_data;
    List<M_BI_EliteGrid_AdapterCommon2> list_data1;
    List<M_BI_EliteGrid_AdapterCommon2> list_data2;

    private Button btn_bi_smart_elite_proposer_date;
    private EditText edt_bi_smart_elite_proposer_age,
            edt_bi_smart_elite_life_assured_age;
    private String proposer_date_of_birth = "", lifeAssured_date_of_birth = "";
    private String proposer_Is_Same_As_Life_Assured = "";
    private CommonForAllProd obj;
    private int b;

    /* Basic Details */

    private EditText edt_proposerdetail_basicdetail_contact_no,
            edt_proposerdetail_basicdetail_Email_id,
            edt_proposerdetail_basicdetail_ConfirmEmail_id;

    private String mobileNo = "";
    private String emailId = "";
    private String ConfirmEmailId = "";
    private boolean validationFla1 = false;
    private String ProposerEmailId = "";
    private Bitmap photoBitmap;

    private String bankUserType = "", mode = "";

    /* parivartan changes */
    private String Check = "";
    private CommonMethods commonMethods;
    private StorageUtils mStorageUtils;
    private ImageButton imageButtonSmartEliteProposerPhotograph;


    private String thirdPartySign = "", appointeeSign = "";
    private Context context;
    private String product_Code;
    private String product_UIN;
    private String product_cateogory;
    private String product_type, Company_policy_surrender_dec = "", agentdeclaration;

    private CheckBox cb_kerladisc;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.bi_smartelitemain);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);

        context = this;
        commonMethods = new CommonMethods();
        mStorageUtils = new StorageUtils();

        dbHelper = new DatabaseHelper(getApplicationContext());
        commonMethods.setApplicationToolbarMenu(this,
                getString(R.string.app_name));

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
                na_dob = intent.getStringExtra("custDOB");
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

                    /* parivartan changes */
                    ProductInfo prodInfoObj = new ProductInfo();
                    planName = "Smart Elite";
                    product_Code = prodInfoObj.getProductCode(planName);
                    product_UIN = prodInfoObj.getProductUIN(planName);
                    product_cateogory = prodInfoObj
                            .getProductCategory(planName);
                    product_type = prodInfoObj.getProductType(planName);
                    /* end */
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
                        product_Code/* "53" */, agentcode, zero + "");

            }
        } else
            needAnalysis_flag = 0;
        // ProductHomePageActivity.path.setText("Benefit Illustrator");

        initialiseDate();
        obj = new CommonForAllProd();
        list_data = new ArrayList<>();
        list_data1 = new ArrayList<>();
        list_data2 = new ArrayList<>();
        inputVal = new StringBuilder();

        prsObj = new ParseXML();
        LinearLayout llSmartEliteMain = findViewById(R.id.ll_bi_smart_elite_main);
        spnr_bi_smart_elite_proposer_title = findViewById(R.id.spnr_bi_smart_elite_proposer_title);
        edt_bi_smart_elite_proposer_first_name = findViewById(R.id.edt_bi_smart_elite_proposer_first_name);
        edt_bi_smart_elite_proposer_middle_name = findViewById(R.id.edt_bi_smart_elite_proposer_middle_name);
        edt_bi_smart_elite_proposer_last_name = findViewById(R.id.edt_bi_smart_elite_proposer_last_name);
        tr_smart_elite_proposer_detail2 = findViewById(R.id.tr_smart_elite_proposer_detail2);
        tr_smart_elite_proposer_detail1 = findViewById(R.id.tr_smart_elite_proposer_detail1);
        // findViewById(R.id.tr_smart_elite_life_assured_detail1);
        edt_bi_smart_elite_life_assured_first_name = findViewById(R.id.edt_bi_smart_elite_life_assured_first_name);
        edt_bi_smart_elite_life_assured_middle_name = findViewById(R.id.edt_bi_smart_elite_life_assured_middle_name);
        edt_bi_smart_elite_life_assured_last_name = findViewById(R.id.edt_bi_smart_elite_life_assured_last_name);
        spnr_bi_smart_elite_life_assured_title = findViewById(R.id.spnr_bi_smart_elite_life_assured_title);
        btn_bi_smart_elite_life_assured_date = findViewById(R.id.btn_bi_smart_elite_life_assured_date);
        tv_monthly_mode = findViewById(R.id.tv_monthly_mode);
        btn_bi_smart_elite_proposer_date = findViewById(R.id.btn_bi_smart_elite_proposer_date);
        edt_bi_smart_elite_proposer_age = findViewById(R.id.edt_bi_smart_elite_proposer_age);
        edt_bi_smart_elite_life_assured_age = findViewById(R.id.edt_bi_smart_elite_life_assured_age);
        rb_smart_elite_proposer_same_as_life_assured_yes = findViewById(R.id.rb_smart_elite_proposer_same_as_life_assured_yes);
        rb_smart_elite_proposer_same_as_life_assured_no = findViewById(R.id.rb_smart_elite_proposer_same_as_life_assured_no);
        back = findViewById(R.id.back);
        btnSubmit = findViewById(R.id.btnSubmit);

        edt_proposerdetail_basicdetail_contact_no = findViewById(R.id.edt_smart_elite_contact_no);
        edt_proposerdetail_basicdetail_Email_id = findViewById(R.id.edt_smart_elite_Email_id);
        edt_proposerdetail_basicdetail_ConfirmEmail_id = findViewById(R.id.edt_smart_elite_ConfirmEmail_id);

        // QuatationNumber = ProductHomePageActivity.quotation_Number;
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


        commonMethods.fillSpinnerValue(context, spnr_bi_smart_elite_proposer_title,
                Arrays.asList(getResources().getStringArray(R.array.arr_select_title)));

        commonMethods.fillSpinnerValue(context, spnr_bi_smart_elite_life_assured_title,
                Arrays.asList(getResources().getStringArray(R.array.arr_select_title)));

        List<String> Strategy_list = new ArrayList<String>();
        //Strategy_list.add("Select Strategy");
        Strategy_list.add("Smart Choice Strategy");
        Strategy_list.add("Life Stage Based Strategy");
        Strategy_list.add("Trigger Fund Strategy");

        // Class Declaration
        commonForAllProd = new CommonForAllProd();
        prop = new BI_SmartEliteProperties();

        // Variable Declaration
        currencyFormat = new DecimalFormat("##,##,##,###");
        showAlert = new AlertDialog.Builder(this);

        // UI elements
        isStaffDisc = findViewById(R.id.cb_staffdisc);

        // Age
        ageInYears = findViewById(R.id.age);
        ageInYears.setEnabled(false);
        String[] ageList = new String[43];
        for (int i = 18; i <= 60; i++) {
            ageList[i - 18] = i + "";
        }
        ArrayAdapter<String> ageAdapter = new ArrayAdapter<>(
                getApplicationContext(), R.layout.spinner_item, ageList);
        ageAdapter.setDropDownViewResource(R.layout.spinner_item1);
        ageInYears.setAdapter(ageAdapter);
        ageAdapter.notifyDataSetChanged();

        // Gender
        selGender = findViewById(R.id.selGender);
        ArrayAdapter<String> genderAdapter = new ArrayAdapter<>(
                getApplicationContext(), R.layout.spinner_item,
                getResources().getStringArray(R.array.gender_all_arrays));
        genderAdapter.setDropDownViewResource(R.layout.spinner_item1);

        selGender.setAdapter(genderAdapter);
        genderAdapter.notifyDataSetChanged();

        // Policy Term
        selPolicyTerm = findViewById(R.id.policyterm);
        String[] policyTermList = new String[26];
        for (int i = 5; i <= 30; i++) {
            policyTermList[i - 5] = i + "";
        }
        ArrayAdapter<String> policyTermAdapter = new ArrayAdapter<>(
                getApplicationContext(), R.layout.spinner_item, policyTermList);
        policyTermAdapter.setDropDownViewResource(R.layout.spinner_item1);
        selPolicyTerm.setAdapter(policyTermAdapter);
        policyTermAdapter.notifyDataSetChanged();

        txt_premium_frequency_mode = findViewById(R.id.txt_premium_frequency_mode);
        selPremFreq = findViewById(R.id.premiumfreq);
        String[] premiumFrequencyList = {"Single", "Limited"};
        ArrayAdapter<String> premiumFrequencyAdapter = new ArrayAdapter<>(
                getApplicationContext(), R.layout.spinner_item,
                premiumFrequencyList);
        premiumFrequencyAdapter.setDropDownViewResource(R.layout.spinner_item1);
        selPremFreq.setAdapter(premiumFrequencyAdapter);
        premiumFrequencyAdapter.notifyDataSetChanged();

        // premium Frequency Mode
        selPremiumFrequncyMode = findViewById(R.id.premium_frequency_mode);
        String[] premiumFrequencyModeList = {"Yearly", "Half Yearly",
                "Quarterly", "Monthly"};
        ArrayAdapter<String> premiumFrequencyModeAdapter = new ArrayAdapter<>(
                getApplicationContext(), R.layout.spinner_item,
                premiumFrequencyModeList);
        premiumFrequencyModeAdapter
                .setDropDownViewResource(R.layout.spinner_item1);
        selPremiumFrequncyMode.setAdapter(premiumFrequencyModeAdapter);
        premiumFrequencyModeAdapter.notifyDataSetChanged();

        // Premium Amount
        premiumAmt = findViewById(R.id.premium_amt);

        // SAMF
        SAMF = findViewById(R.id.samf);
        SAMF.setFilters(new InputFilter[]{new DesimalDigitsInputFilter(2)});

        // Plan
        selPlan = findViewById(R.id.selplan);
        String[] selPlanList = {"Gold", "Platinum"};
        ArrayAdapter<String> selPlanAdapter = new ArrayAdapter<>(
                getApplicationContext(), R.layout.spinner_item, selPlanList);
        selPlanAdapter.setDropDownViewResource(R.layout.spinner_item1);
        selPlan.setAdapter(selPlanAdapter);
        selPlanAdapter.notifyDataSetChanged();

        // Premium Paying Term
        txt_premPayingTerm = findViewById(R.id.txt_premiumpayingterm);
        premPayingTerm = findViewById(R.id.premiumpayingterm);
        //String[] premiumPayingTermList = {"5", "8", "10"};
        String[] premiumPayingTermList = {"7", "10", "12"};
        ArrayAdapter<String> premiumPayingTermAdapter = new ArrayAdapter<>(
                getApplicationContext(), R.layout.spinner_item,
                premiumPayingTermList);
        premiumPayingTermAdapter
                .setDropDownViewResource(R.layout.spinner_item1);
        premPayingTerm.setAdapter(premiumPayingTermAdapter);
        premiumPayingTermAdapter.notifyDataSetChanged();

        // YearsElapsedSinceInception
        noOfYearsElapsedSinceInception = findViewById(R.id.years_elapsed_since_inception);

        // Funds
        percent_EquityEliteIIFund = findViewById(R.id.equity_elite_ii_fund);
        percent_BalancedFund = findViewById(R.id.balancedfund);
        percent_BondFund = findViewById(R.id.bondfund);
        percent_MoneyMarketFund = findViewById(R.id.moneymarketfund);
        // removed by vrushali chaudhari

        /*New Fund*/
        Percent_PureFund = (EditText) findViewById(R.id.purefund);
        Percent_MidcapFund = (EditText) findViewById(R.id.midcapfund);
        Percent_BondOptimiserFund = (EditText) findViewById(R.id.bondoptimiserfund);
        percent_CorporateBondFund = (EditText) findViewById(R.id.corporateBondFund);
        // help declaration
        help_SAMF = findViewById(R.id.help_samf);
        help_premiumAmt = findViewById(R.id.help_prem_amt);
        help_noOfYearsElapsedSinceInception = findViewById(R.id.help_years_elapsed_since_inception);

        // SFIN of Funds
        selViewSFIN = findViewById(R.id.selViewSFIN);
        tbEquityEliteIIFund = findViewById(R.id.tbEquityEliteIIFund);
        tbBondFund = findViewById(R.id.tbBondFund);
        tbBalancedFund = findViewById(R.id.tbBalancedFund);
        tbMoneyMarketFund = findViewById(R.id.tbMoneyMarketFund);
        // tbIndexFund = (TableRow)findViewById(R.id.tbIndexFund);
        // tbPEManaged = (TableRow)findViewById(R.id.tbPEManagedFund);

        /*********************** Item Listener starts from here ******************************************/

        // removed by vrushali chaudhari

        // Go Home Button

        Date();
        // setBIInputGui();
        proposer_Is_Same_As_Life_Assured = "y";
        edt_bi_smart_elite_life_assured_first_name
                .setOnEditorActionListener(this);
        edt_bi_smart_elite_life_assured_middle_name
                .setOnEditorActionListener(this);
        edt_bi_smart_elite_life_assured_last_name
                .setOnEditorActionListener(this);
        edt_bi_smart_elite_proposer_first_name.setOnEditorActionListener(this);
        edt_bi_smart_elite_proposer_middle_name.setOnEditorActionListener(this);
        edt_bi_smart_elite_proposer_last_name.setOnEditorActionListener(this);

        premiumAmt.setOnEditorActionListener(this);
        SAMF.setOnEditorActionListener(this);
        noOfYearsElapsedSinceInception.setOnEditorActionListener(this);
        percent_EquityEliteIIFund.setOnEditorActionListener(this);
        percent_BalancedFund.setOnEditorActionListener(this);
        percent_MoneyMarketFund.setOnEditorActionListener(this);
        percent_BondFund.setOnEditorActionListener(this);
        percent_BondFund.setOnEditorActionListener(this);
        percent_CorporateBondFund.setOnEditorActionListener(this);


        // edt_bi_smart_elite_life_assured_first_name.requestFocus();
        setFocusable(spnr_bi_smart_elite_life_assured_title);
        spnr_bi_smart_elite_life_assured_title.requestFocus();

        if (needAnalysis_flag == 1 && !TextUtils.isEmpty(gender)) {
            selGender.setSelection(genderAdapter.getPosition(gender));
            onClickLADob(btn_bi_smart_elite_life_assured_date);
        }

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
                        //	email_id_validation(ProposerEmailId);

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

        String str_usertype = "";
        TableRow tr_staff_disc;

        // try {
        // Map<String, String> data_logindetail = new HashMap<String, String>();
        // data_logindetail = db.getLoginDetailByAgentId(sr_Code);
        // str_usertype = data_logindetail.get("usertype");
        //
        // } catch (Exception e) {
        // Log.e("Terms_and_ConditionsActivity", e.toString()
        // + "Error  in getting in Login Table");
        // e.printStackTrace();
        // }

        tr_staff_disc = findViewById(R.id.tr_smart_elite_staff_disc);
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
        /**************************** Item Listener ends here *********************************************/
    }

    private void setSpinnerAndOtherListner() {
        // TODO Auto-generated method stub

        rb_smart_elite_proposer_same_as_life_assured_yes
                .setOnCheckedChangeListener(new OnCheckedChangeListener() {

                    public void onCheckedChanged(CompoundButton arg0,
                                                 boolean checked) {
                        // TODO Auto-generated method stub
                        if (checked) {
                            proposer_Is_Same_As_Life_Assured = "y";
                            tr_smart_elite_proposer_detail1
                                    .setVisibility(View.GONE);
                            tr_smart_elite_proposer_detail2
                                    .setVisibility(View.GONE);
                        }

                    }
                });
        rb_smart_elite_proposer_same_as_life_assured_no
                .setOnCheckedChangeListener(new OnCheckedChangeListener() {

                    public void onCheckedChanged(CompoundButton arg0,
                                                 boolean checked) {
                        // TODO Auto-generated method stub
                        if (checked) {
                            proposer_Is_Same_As_Life_Assured = "n";
                            tr_smart_elite_proposer_detail1
                                    .setVisibility(View.VISIBLE);
                            tr_smart_elite_proposer_detail2
                                    .setVisibility(View.VISIBLE);
                        }

                    }
                });

        spnr_bi_smart_elite_proposer_title
                .setOnItemSelectedListener(new OnItemSelectedListener() {

                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int position, long id) {
                        // TODO Auto-generated method stub
                        if (position == 0) {
                            proposer_Title = "";
                        } else {
                            proposer_Title = spnr_bi_smart_elite_proposer_title
                                    .getSelectedItem().toString();
                            if (proposer_Title.equalsIgnoreCase("Mr.")) {
                                // selGender.setSelection(getIndex(selGender,
                                // "Male"));
                            } else if (proposer_Title.equalsIgnoreCase("Ms.")
                                    || proposer_Title.equalsIgnoreCase("Mrs.")) {
                                // selGender.setSelection(getIndex(selGender,
                                // "Female"));
                            }

                        }
                    }

                    public void onNothingSelected(AdapterView<?> arg0) {
                        // TODO Auto-generated method stub

                    }
                });
        spnr_bi_smart_elite_life_assured_title
                .setOnItemSelectedListener(new OnItemSelectedListener() {

                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int position, long id) {
                        // TODO Auto-generated method stub
                        if (flag != 0) {
                            if (flag != 3) {
                                flag = 2;
                            }
                        }

                        if (position == 0) {
                            lifeAssured_Title = "";
                        } else {
                            lifeAssured_Title = spnr_bi_smart_elite_life_assured_title
                                    .getSelectedItem().toString();
                           /* if (lifeAssured_Title.equalsIgnoreCase("Mr.")) {
                                gender = "Male";
                                iv_men.setImageDrawable(getResources().getDrawable(
                                        R.drawable.male_selected));
                                iv_women.setImageDrawable(getResources().getDrawable(
                                        R.drawable.female_nonselected));
							} else if (lifeAssured_Title
									.equalsIgnoreCase("Ms.")) {
                                gender = "Female";
                                iv_women.setImageDrawable(getResources().getDrawable(
                                        R.drawable.female_selected));
                                iv_men.setImageDrawable(getResources().getDrawable(
                                        R.drawable.male_nonselected));
							} else if (lifeAssured_Title
									.equalsIgnoreCase("Mrs.")) {
                                gender = "Female";
                                iv_women.setImageDrawable(getResources().getDrawable(
                                        R.drawable.female_selected));
                                iv_men.setImageDrawable(getResources().getDrawable(
                                        R.drawable.male_nonselected));
							}
                        */
                            clearFocusable(spnr_bi_smart_elite_life_assured_title);
                            setFocusable(edt_bi_smart_elite_life_assured_first_name);

                            edt_bi_smart_elite_life_assured_first_name
                                    .requestFocus();

                        }
                    }

                    public void onNothingSelected(AdapterView<?> arg0) {
                        // TODO Auto-generated method stub

                    }
                });

        // age
        ageInYears.setOnItemSelectedListener(new OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> arg0, View arg1, int pos,
                                       long id) {
                if (flag != 1) {
                    updateSAMFlabel();
                    valPolicyTerm();
                }

            }

            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        // policy term
        selPolicyTerm.setOnItemSelectedListener(new OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> arg0, View arg1, int pos,
                                       long id) {
                if (flag != 1) {
                    valMaturityAge();
                    updatenoOfYearsElapsedSinceInception();
                    valPPT();
                    valPolicyTerm();
                    noOfYearsElapsedSinceInception.setText(selPolicyTerm
                            .getSelectedItem().toString().trim());
                    // removed by vrushali chaudhari
                    // noOfYearsElapsedSinceInception.setText(selPolicyTerm.getSelectedItem().toString());
                    clearFocusable(selPolicyTerm);
                    setFocusable(selPremFreq);
                    selPremFreq.requestFocus();
                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        // Premium Frequency
        selPremFreq.setOnItemSelectedListener(new OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> arg0, View arg1, int pos,
                                       long id) {
                if (flag != 1) {
                    if (flag != 2) {
                        effectivePremium = getEffectivePremium();
                        updatePremiumAmtLabel();
                        updateSAMFlabel();

                        if (pos == 1) {
                            txt_premium_frequency_mode
                                    .setVisibility(View.VISIBLE);
                            selPremiumFrequncyMode.setVisibility(View.VISIBLE);
                            selPremiumFrequncyMode.setSelection(0, false);
                            txt_premPayingTerm.setVisibility(View.VISIBLE);
                            premPayingTerm.setVisibility(View.VISIBLE);
                        } else {
                            txt_premium_frequency_mode.setVisibility(View.GONE);
                            selPremiumFrequncyMode.setVisibility(View.GONE);
                            txt_premPayingTerm.setVisibility(View.GONE);
                            premPayingTerm.setVisibility(View.GONE);
                            tv_monthly_mode.setVisibility(View.GONE);
                        }
                    }
                    flag = 3;
                }

                if (premFreq.equalsIgnoreCase("Single")) {
                    flag = 3;

                }

                if (selPremFreq.getSelectedItem().toString()
                        .equalsIgnoreCase("Single")) {
                    flag = 3;
                    clearFocusable(selPremFreq);
                    setFocusable(premiumAmt);
                    premiumAmt.requestFocus();

                } else if (selPremFreq.getSelectedItem().toString()
                        .equalsIgnoreCase("Limited")) {
                    clearFocusable(selPremFreq);
                    setFocusable(premPayingTerm);
                    premPayingTerm.requestFocus();
                }

            }

            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        // Premium Frequency Mode
        selPremiumFrequncyMode
                .setOnItemSelectedListener(new OnItemSelectedListener() {

                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int pos, long id) {
                        if (flag != 1) {
                            effectivePremium = getEffectivePremium();
                            updatePremiumAmtLabel();
                            if (selPremiumFrequncyMode.getSelectedItem()
                                    .toString().equals("Monthly")) {
                                tv_monthly_mode.setVisibility(View.VISIBLE);
                            } else {
                                tv_monthly_mode.setVisibility(View.GONE);
                            }
                        }
                        clearFocusable(selPremiumFrequncyMode);
                        setFocusable(premiumAmt);
                        premiumAmt.requestFocus();
                    }

                    public void onNothingSelected(AdapterView<?> arg0) {
                        // TODO Auto-generated method stub

                    }
                });

        // Premium Amount
        premiumAmt.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            public void onFocusChange(View arg0, boolean arg1) {
                // TODO Auto-generated method stub
                if (!premiumAmt.getText().toString().equals("")) {

                    effectivePremium = getEffectivePremium();
                }
            }
        });

        premPayingTerm.setOnItemSelectedListener(new OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> arg0, View arg1, int pos,
                                       long id) {

                clearFocusable(premPayingTerm);
                setFocusable(selPremiumFrequncyMode);
                selPremiumFrequncyMode.requestFocus();
                // if (flag != 1) {
                valPPT();
                // }
            }

            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        // modified by vrushali chaudhari
        // SFIN of Funds
        selViewSFIN
                .setOnCheckedChangeListener(new OnCheckedChangeListener() {

                    public void onCheckedChanged(CompoundButton buttonView,
                                                 boolean isChecked) {
                        if (isChecked) {
                            tbEquityEliteIIFund.setVisibility(View.VISIBLE);
                            tbBondFund.setVisibility(View.VISIBLE);
                            tbBalancedFund.setVisibility(View.VISIBLE);
                            tbMoneyMarketFund.setVisibility(View.VISIBLE);
                            // tbIndexFund.setVisibility(View.VISIBLE);
                            // tbPEManaged.setVisibility(View.VISIBLE);
                        } else {
                            tbEquityEliteIIFund.setVisibility(View.GONE);
                            tbBondFund.setVisibility(View.GONE);
                            tbBalancedFund.setVisibility(View.GONE);
                            tbMoneyMarketFund.setVisibility(View.GONE);
                            // tbIndexFund.setVisibility(View.GONE);
                            // tbPEManaged.setVisibility(View.GONE);

                        }
                    }
                });

        selPlan.setOnItemSelectedListener(new OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                // TODO Auto-generated method stub

                if (b == 1) {
                    edt_bi_smart_elite_life_assured_first_name.requestFocus();
                    b = 0;
                } else {
                    clearFocusable(selPlan);
                    setFocusable(noOfYearsElapsedSinceInception);
                    noOfYearsElapsedSinceInception.requestFocus();
                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });

        // For Staff Discount
        isStaffDisc.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                if ((isStaffDisc.isChecked())) {
                    isStaffDisc.setChecked(true);

                    clearFocusable(spnr_bi_smart_elite_life_assured_title);
                    setFocusable(spnr_bi_smart_elite_life_assured_title);
                    spnr_bi_smart_elite_life_assured_title.requestFocus();
                }
            }
        });

        back.setOnClickListener(new OnClickListener() {

            public void onClick(View arg0) {
                setResult(RESULT_OK);
                finish();
            }
        });

        // Calculate premium

        btnSubmit.setOnClickListener(new OnClickListener() {

            public void onClick(View arg0) {
                inputVal = new StringBuilder();
                retVal = new StringBuilder();
                bussIll = new StringBuilder();

                proposer_First_Name = edt_bi_smart_elite_proposer_first_name
                        .getText().toString().trim();
                proposer_Middle_Name = edt_bi_smart_elite_proposer_middle_name
                        .getText().toString().trim();
                proposer_Last_Name = edt_bi_smart_elite_proposer_last_name
                        .getText().toString().trim();

                name_of_proposer = proposer_Title + " " + proposer_First_Name
                        + " " + proposer_Middle_Name + " " + proposer_Last_Name;

                lifeAssured_First_Name = edt_bi_smart_elite_life_assured_first_name
                        .getText().toString().trim();
                lifeAssured_Middle_Name = edt_bi_smart_elite_life_assured_middle_name
                        .getText().toString().trim();
                lifeAssured_Last_Name = edt_bi_smart_elite_life_assured_last_name
                        .getText().toString().trim();

                name_of_life_assured = lifeAssured_Title + " "
                        + lifeAssured_First_Name + " "
                        + lifeAssured_Middle_Name + " " + lifeAssured_Last_Name;

                gender = selGender.getSelectedItem().toString();
                mobileNo = edt_proposerdetail_basicdetail_contact_no.getText()
                        .toString();
                emailId = edt_proposerdetail_basicdetail_Email_id.getText()
                        .toString();
                ConfirmEmailId = edt_proposerdetail_basicdetail_ConfirmEmail_id
                        .getText().toString();

                //
                if (valProposerSameAsLifeAssured()
                        && valLifeAssuredProposerDetail() && valDob()
                        && valBasicDetail() && valPremiumAmt() && valSAMF()
                        && valTotalAllocation() && valPolicyTerm() && valPPT()) {
                    addListenerOnSubmit();
                    getInput(smartEliteBean);

                    if (proposer_Is_Same_As_Life_Assured.equalsIgnoreCase("y")) {
                        proposer_Title = "";
                        proposer_First_Name = "";
                        proposer_Middle_Name = "";
                        proposer_Last_Name = "";
                        name_of_proposer = "";
                        proposer_date_of_birth = "";
                    }

                    if (needAnalysis_flag == 0) {
                        Intent i = new Intent(BI_SmartEliteActivity.this,
                                Success.class);

                        i.putExtra("ProductName",
                                "Product : SBI Life - Smart Elite (UIN:111L072V04)");

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
                                                        "fundVal6"))));
                        i.putExtra(
                                "op2",
                                "Fund Value @ 8% is Rs. "
                                        + currencyFormat.format(Double
                                        .parseDouble(prsObj
                                                .parseXmlTag(retVal
                                                                .toString(),
                                                        "fundVal10"))));
                        if (selPremFreq.getSelectedItem().toString()
                                .equals("Single"))
                            i.putExtra(
                                    "op3",
                                    "Single Premium (SP) is Rs. "
                                            + currencyFormat.format(Double.parseDouble(prsObj
                                            .parseXmlTag(
                                                    retVal.toString(),
                                                    "AnnPrem"))));
                        else
                            i.putExtra(
                                    "op3",
                                    "Annualised Premium (LPPT) is Rs. "
                                            + currencyFormat.format(Double.parseDouble(prsObj
                                            .parseXmlTag(
                                                    retVal.toString(),
                                                    "AnnPrem"))));
                        i.putExtra("header", "SBI Life - Smart Elite");
                        i.putExtra("header1", "(UIN:111L072V04)");
                        startActivity(i);
                    } else
                        Dialog();

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

        /* parivartan changes */
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
                    Uri imageUri;
                    try {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            imageUri = commonMethods.getContentUri(context, new File(Photo.toString()));
                        } else {
                            imageUri = Uri.fromFile(new File(Photo.toString()));
                        }

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
                        imageButtonSmartEliteProposerPhotograph
                                .setImageBitmap(scaled);
                    } else {
                        photoBitmap = null;
                    }
                }
            }
        }
        /* end */
    }

    // Store user input in Bean object
    private void addListenerOnSubmit() {
        // Insert data entered by user in an object
        smartEliteBean = new BI_SmartEliteBean();
        effectivePremium = getEffectivePremium();
        if (isStaffDisc.isChecked()) {
            smartEliteBean.setIsForStaffOrNot(true);
        } else {
            smartEliteBean.setIsForStaffOrNot(false);
        }
        if (cb_kerladisc.isChecked()) {
            smartEliteBean.setKerlaDisc(true);
            //smartEliteBean.setServiceTax(true);
        } else {
            smartEliteBean.setKerlaDisc(false);
            //smartEliteBean.setServiceTax(false);
        }


        // Removed by vrushali chaudhari
        // if(isBancAssuaranceDisc.isChecked())
        // {SmartEliteBean.setIsBancAssuranceDiscOrNot(true);}
        // else{SmartEliteBean.setIsBancAssuranceDiscOrNot(false);}

        smartEliteBean.setAgeAtEntry(Integer.parseInt(ageInYears
                .getSelectedItem().toString()));

        smartEliteBean.setGender(selGender.getSelectedItem().toString());

        smartEliteBean.setPolicyTerm_Basic(Integer.parseInt(selPolicyTerm
                .getSelectedItem().toString()));

        smartEliteBean.setPremFreq(selPremFreq.getSelectedItem().toString());

        if (selPremFreq.getSelectedItem().toString().equals("Single")) {
            smartEliteBean.setPremiumPayingTerm(1);
        } else {
            smartEliteBean.setPremFreqMode(selPremiumFrequncyMode
                    .getSelectedItem().toString());
            smartEliteBean.setPremiumPayingTerm(Integer.parseInt(premPayingTerm
                    .getSelectedItem().toString()));

        }

        //smartEliteBean.setEffectivePremium(Double.parseDouble(effectivePremium));
        smartEliteBean.setEffectivePremium(Double.parseDouble(getEffectivePremium
                ((selPremFreq.getSelectedItem().toString())
                        , String.valueOf(Double.parseDouble(premiumAmt.getText()
                                .toString().equals("") ? "0.00" : premiumAmt.getText()
                                .toString())),
                        (selPremiumFrequncyMode.getSelectedItem().toString()))));

        smartEliteBean.setPremiumAmount(Double.parseDouble(premiumAmt.getText()
                .toString().equals("") ? "0.00" : premiumAmt.getText()
                .toString()));
        smartEliteBean.setSAMF(Double.parseDouble(SAMF.getText().toString()));

        smartEliteBean.setPlanOption(selPlan.getSelectedItem().toString());

        smartEliteBean.setYearsElapsedSinceInception(Integer
                .parseInt(noOfYearsElapsedSinceInception.getText().toString()));
        if (selPremFreq.getSelectedItem().toString().equals("Limited")) {
            if (selPremiumFrequncyMode.getSelectedItem().toString()
                    .equals("Yearly")) {
                smartEliteBean.setPF(1);
            } else if (selPremiumFrequncyMode.getSelectedItem().toString()
                    .equals("Half Yearly")) {
                smartEliteBean.setPF(2);
            } else if (selPremiumFrequncyMode.getSelectedItem().toString()
                    .equals("Quarterly")) {
                smartEliteBean.setPF(4);
            } else if (selPremiumFrequncyMode.getSelectedItem().toString()
                    .equals("Monthly")) {
                smartEliteBean.setPF(12);
            }
        }
        // For Sinle Mode of Premmium Frequency
        else {
            smartEliteBean.setPF(1);
        }

        if (!percent_EquityEliteIIFund.getText().toString().equals(""))
            smartEliteBean
                    .setPercentToBeInvested_EquityEliteIIFund(Double
                            .parseDouble(percent_EquityEliteIIFund.getText()
                                    .toString()));
        else
            smartEliteBean.setPercentToBeInvested_EquityEliteIIFund(0.0);

        if (!percent_BalancedFund.getText().toString().equals(""))
            smartEliteBean.setPercentToBeInvested_BalancedFund(Double
                    .parseDouble(percent_BalancedFund.getText().toString()));
        else
            smartEliteBean.setPercentToBeInvested_BalancedFund(0.0);

        if (!percent_BondFund.getText().toString().equals(""))
            smartEliteBean.setPercentToBeInvested_BondFund(Double
                    .parseDouble(percent_BondFund.getText().toString()));
        else
            smartEliteBean.setPercentToBeInvested_BondFund(0.0);

        if (!percent_MoneyMarketFund.getText().toString().equals(""))
            smartEliteBean.setPercentToBeInvested_MoneyMarketFund(Double
                    .parseDouble(percent_MoneyMarketFund.getText().toString()));
        else
            smartEliteBean.setPercentToBeInvested_MoneyMarketFund(0.0);


        if (!Percent_PureFund.getText().toString().equals(""))
            smartEliteBean.setPercent_PureFund(Double
                    .parseDouble(Percent_PureFund.getText().toString()));
        else
            smartEliteBean.setPercent_PureFund(0.0);


        if (!Percent_MidcapFund.getText().toString().equals(""))
            smartEliteBean.setPercent_MidcapFund(Double
                    .parseDouble(Percent_MidcapFund.getText().toString()));
        else
            smartEliteBean.setPercent_MidcapFund(0.0);


        if (!Percent_BondOptimiserFund.getText().toString().equals(""))
            smartEliteBean.setPercent_BondOptimiserFund(Double
                    .parseDouble(Percent_BondOptimiserFund.getText().toString()));
        else
            smartEliteBean.setPercent_BondOptimiserFund(0.0);


        smartEliteBean.setPercent_EquityFund(0.0);

        if (!percent_CorporateBondFund.getText().toString().equals(""))
            smartEliteBean.setPercentToBeInvested_CorpBondFund(Double
                    .parseDouble(percent_CorporateBondFund.getText().toString()));
        else
            smartEliteBean.setPercentToBeInvested_CorpBondFund(0);

        smartEliteBean.setPercent_Discontinuedpolicyfund(0.0);
        // removed by vrushali chaudhari
        // if(!percent_IndexFund.getText().toString().equals(""))
        // smartEliteBean.setPercentToBeInvested_IndexFund(Double.parseDouble(percent_IndexFund.getText().toString()));
        // else
        // smartEliteBean.setPercentToBeInvested_IndexFund(0.0);
        //
        // if(!percent_PEmanagedFund.getText().toString().equals(""))
        // smartEliteBean.setPercentToBeInvested_PEmanagedFund(Double.parseDouble(percent_PEmanagedFund.getText().toString()));
        // else
        // smartEliteBean.setPercentToBeInvested_PEmanagedFund(0.0);
        // Show Smart Elite Output Screen

        showSmartEliteOutputPg(smartEliteBean);

    }

    /********************************** Input starts from here **********************************************************/
    private void getInput(BI_SmartEliteBean smartEliteBean) {
        inputVal = new StringBuilder();
        try {
            String LifeAssured_title = spnr_bi_smart_elite_life_assured_title
                    .getSelectedItem().toString();
            String LifeAssured_firstName = edt_bi_smart_elite_life_assured_first_name
                    .getText().toString();
            String LifeAssured_middleName = edt_bi_smart_elite_life_assured_middle_name
                    .getText().toString();
            String LifeAssured_lastName = edt_bi_smart_elite_life_assured_last_name
                    .getText().toString();
            String LifeAssured_DOB = btn_bi_smart_elite_life_assured_date
                    .getText().toString();
            String LifeAssured_age = ageInYears.getSelectedItem().toString();

            String proposer_title = "";
            String proposer_firstName = "";
            String proposer_middleName = "";
            String proposer_lastName = "";
            String proposer_DOB = "";
            String proposer_age = "";
            String proposer_gender = "";

            boolean staffDisc = smartEliteBean.getIsForStaffOrNot();
            boolean bancAssuranceDisc = smartEliteBean
                    .getIsBancAssuranceDiscOrNot();

            inputVal.append("<?xml version='1.0' encoding='utf-8' ?><smartelite>");
            inputVal.append("<LifeAssured_title>").append(LifeAssured_title).append("</LifeAssured_title>");
            inputVal.append("<LifeAssured_firstName>").append(LifeAssured_firstName).append("</LifeAssured_firstName>");
            inputVal.append("<LifeAssured_middleName>").append(LifeAssured_middleName).append("</LifeAssured_middleName>");
            inputVal.append("<LifeAssured_lastName>").append(LifeAssured_lastName).append("</LifeAssured_lastName>");
            inputVal.append("<LifeAssured_DOB>").append(LifeAssured_DOB).append("</LifeAssured_DOB>");
            inputVal.append("<LifeAssured_age>").append(LifeAssured_age).append("</LifeAssured_age>");
            inputVal.append("<gender>").append(selGender.getSelectedItem().toString()).append("</gender>");

            inputVal.append("<proposer_title>").append(proposer_title).append("</proposer_title>");
            inputVal.append("<proposer_firstName>").append(proposer_firstName).append("</proposer_firstName>");
            inputVal.append("<proposer_middleName>").append(proposer_middleName).append("</proposer_middleName>");
            inputVal.append("<proposer_lastName>").append(proposer_lastName).append("</proposer_lastName>");
            inputVal.append("<proposer_DOB>").append(proposer_DOB).append("</proposer_DOB>");
            inputVal.append("<proposer_age>").append(proposer_age).append("</proposer_age>");
            inputVal.append("<proposer_gender>").append(proposer_gender).append("</proposer_gender>");

            inputVal.append("<product_name>").append(planName).append("</product_name>");
            /* parivartan changes */
            inputVal.append("<product_Code>").append(product_Code).append("</product_Code>");
            inputVal.append("<product_UIN>").append(product_UIN).append("</product_UIN>");
            inputVal.append("<product_cateogory>").append(product_cateogory).append("</product_cateogory>");
            inputVal.append("<product_type>").append(product_type).append("</product_type>");
            /* end */

            inputVal.append("<proposer_Is_Same_As_Life_Assured>").append(proposer_Is_Same_As_Life_Assured).append("</proposer_Is_Same_As_Life_Assured>");
            inputVal.append("<isStaff>").append(staffDisc).append("</isStaff>");
            inputVal.append("<isBancAssuranceDisc>").append(bancAssuranceDisc).append("</isBancAssuranceDisc>");
            // Added For my Refernce
            // inputVal.append("<dob>"+
            // ageInYears.getSelectedItem().toString()+"</dob>");
            inputVal.append("<age>").append(ageInYears.getSelectedItem().toString()).append("</age>");
            inputVal.append("<SAMF>").append(SAMF.getText().toString()).append("</SAMF>");

            inputVal.append("<premiumAmount>").append(premiumAmt.getText().toString()).append("</premiumAmount>");
            inputVal.append("<noOfYrElapsed>").append(noOfYearsElapsedSinceInception.getText().toString()).append("</noOfYrElapsed>");
            String premFreq = smartEliteBean.getPremFreq();
            inputVal.append("<premFreq>").append(premFreq).append("</premFreq>");
            inputVal.append("<premFreqMode>").append(selPremiumFrequncyMode.getSelectedItem().toString()).append("</premFreqMode>");
            smartEliteBean.getSAMF();
            smartEliteBean.getAgeAtEntry();
            String planOption = smartEliteBean.getPlanOption();
            inputVal.append("<plan>").append(planOption).append("</plan>");
            // Internally Calculated Input Fields
            double effectivePremium = smartEliteBean.getEffectivePremium();
            smartEliteBean.getPF();
            inputVal.append("<effectivePremium>").append(effectivePremium).append("</effectivePremium>");
            int premiumPayingTerm = smartEliteBean.getPremiumPayingTerm();
            inputVal.append("<premPayingTerm>").append(premiumPayingTerm).append("</premPayingTerm>");
            int policyTerm = smartEliteBean.getPolicyTerm_Basic();
            inputVal.append("<policyTerm>").append(policyTerm).append("</policyTerm>");
            inputVal.append("<perInvEquityEliteIIFund>").append(percent_EquityEliteIIFund.getText().toString()).append("</perInvEquityEliteIIFund>");
            inputVal.append("<perInvBondFund>").append(percent_BondFund.getText().toString()).append("</perInvBondFund>");
            inputVal.append("<perInvBalancedFund>").append(percent_BalancedFund.getText().toString()).append("</perInvBalancedFund>");
            inputVal.append("<perInvMoneyMarketFund>").append(percent_MoneyMarketFund.getText().toString()).append("</perInvMoneyMarketFund>");
            inputVal.append("<perInvPureFund>"
                    + Percent_PureFund.getText().toString()
                    + "</perInvPureFund>");
            inputVal.append("<perInvMidcapFund>"
                    + Percent_MidcapFund.getText().toString()
                    + "</perInvMidcapFund>");

            inputVal.append("<perInvBondOptimiserFund>"
                    + Percent_BondOptimiserFund.getText().toString()
                    + "</perInvBondOptimiserFund>");


            inputVal.append("<perInvEquityFund></perInvEquityFund>");

            inputVal.append("<perInvCorporateBondFund>"
                    + percent_CorporateBondFund.getText().toString()
                    + "</perInvCorporateBondFund>");

            inputVal.append("<perInvDiscontinuedpolicyfund></perInvDiscontinuedpolicyfund>");

            //Added Tushar Kadam Kerla Applicable Tax 7-Jun-2019
            String str_kerla_discount = "N";
            if (cb_kerladisc.isChecked()) {
                str_kerla_discount = "Y";
            }

            inputVal.append("<KFC>" + str_kerla_discount + "</KFC>");
            //End Added Tushar Kadam Kerla Applicable Tax 7-Jun-2019

            inputVal.append("<BIVERSION>" + "3" + "</BIVERSION>");
            inputVal.append("</smartelite>");

            //////////////////////////////////////////////////
            System.out.println("input : " + inputVal.toString());
        } catch (Exception e) {
            inputVal.append("<?xml version='1.0' encoding='utf-8' ?><smartelite>" + "<errCode>1</errCode>" + "<errorMessage>").append(e.getMessage()).append("</errorMessage></smartelite>");
        }
    }

    /******************************** Input starts from here **********************************************************/
    /******************************** Output starts from here **********************************************************/

    /********************************** Output starts from here **********************************************************/

    // Display Smart Elite Output Screen
    private void showSmartEliteOutputPg(BI_SmartEliteBean smartEliteBean1) {

        retVal = new StringBuilder();

        // String premFreq="";
        try {

            String premiumType = "";
            if (smartEliteBean1.getPremFreq().equals("Single"))
                premiumType = "singlePrem";
            else
                premiumType = "annualisedPrem";

            String[] outputArr = getOutput("BI_Incl_Mort & Ser Tax",
                    smartEliteBean1);

            String[] redinYieldArr = getOutputRedutionInYield(
                    "Benefit Illustrator_CAP", smartEliteBean1);


            retVal.append("<?xml version='1.0' encoding='utf-8' ?><smartElite>");

			/*
			  <fundVal4> changed to <fundVal6> <fundVal8> changed to
			  <fundVal10> by Akshaya
			 */

            retVal.append("<errCode>0</errCode>" + "<maturityAge>")
                    .append(smartEliteBean1.getAgeAtEntry() + smartEliteBean1
                            .getPolicyTerm_Basic()).append("</maturityAge>")
                    .append("<policyTerm>").append(smartEliteBean1.getPolicyTerm_Basic())
                    .append("</policyTerm>")
                    .append("<AnnPrem>").
                    append(commonForAllProd.getRound(commonForAllProd
                            .getStringWithout_E(smartEliteBean1
                                    .getEffectivePremium()))).append("</AnnPrem>")
                    .append("<sumAssured>")
                    .append(commonForAllProd.getRound(outputArr[0]))
                    .append("</sumAssured>")
                    .append("<accBenfSumAssured>")
                    .append(commonForAllProd.getRound(""
                            + Math.min(Double.parseDouble(outputArr[0]),
                            5000000))).append("</accBenfSumAssured>")
                    .append("<fundVal6>")
                    .append(Double.parseDouble(outputArr[1]))
                    .append("</fundVal6>")
                    .append("<fundVal10>")
                    .append(Double.parseDouble(outputArr[2]))
                    .append("</fundVal10>").append("<").append(premiumType).append(">")
                    .append(Double.parseDouble(outputArr[3]))
                    .append("</")
                    .append(premiumType).append(">")
                    .append("<redInYieldMat>")
                    .append(redinYieldArr[0]).append("</redInYieldMat>")
                    .append("<redInYieldNoYr>").append(redinYieldArr[1])
                    .append("</redInYieldNoYr>")
                    .append("<netYield4Pr>")
                    .append(redinYieldArr[2]).append("</netYield4Pr>")
                    .append("<netYield8Pr>").append(redinYieldArr[3])
                    .append("</netYield8Pr>").append("<AnnualInstallment>")
                    .append(commonForAllProd.getRound(""
                            + (Math.min(Double.parseDouble(outputArr[0]),
                            5000000) * 0.1))).append("</AnnualInstallment>");

            int index = smartEliteBean1.getPolicyTerm_Basic();
            String FundValAtEnd4Pr = prsObj.parseXmlTag(bussIll.toString(), "FundValAtEnd4Pr" + index + "");
            String FundValAtEnd8Pr = prsObj.parseXmlTag(bussIll.toString(), "FundValAtEnd8Pr" + index + "");

            retVal.append("<FundValAtEnd4Pr" + index + ">" + FundValAtEnd4Pr + "</FundValAtEnd4Pr" + index + ">");
            retVal.append("<FundValAtEnd8Pr" + index + ">" + FundValAtEnd8Pr + "</FundValAtEnd8Pr" + index + ">");

            retVal.append(bussIll.toString());

            retVal.append("</smartElite>");
            System.out.println(retVal.toString());

            System.out.println();
            // insertDataIntoDatabase();

        } catch (Exception e) {
            retVal.append("<?xml version='1.0' encoding='utf-8' ?><smartElite>" + "<errCode>1</errCode>" + "<errorMessage>").append(e.getMessage()).append("</errorMessage></smartElite>");
        }

    }

    /********************************** Output ends here **********************************************************/

    // Dialog USed For Displaying BI
    private void Dialog() {
        final Dialog d = new Dialog(this);
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);
        d.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.BLACK));

        input = inputVal.toString();
        output = retVal.toString();

        d.setContentView(R.layout.layout_smart_elite_bi_grid_common);

        TextView tv_guaranted_death = (TextView) d
                .findViewById(R.id.tv_guaranted_death);

        final TextView tv_proposername = (TextView) d
                .findViewById(R.id.tv_proposername);
        final TextView tv_proposal_number = (TextView) d
                .findViewById(R.id.tv_proposal_number);
        tv_proposal_number.setText(QuatationNumber);


        TextView tv_smart_elite_distribution_channel = (TextView) d
                .findViewById(R.id.tv_smart_elite_distribution_channel);

        tv_smart_elite_distribution_channel.setText(userType);

        TextView tv_bi_smart_elite_age_entry = (TextView) d
                .findViewById(R.id.tv_bi_smart_elite_age_entry);
        TextView tv_age_of_proposer = (TextView) d
                .findViewById(R.id.tv_age_of_proposer);
        TextView tv_bi_smart_elite_maturity_age = (TextView) d
                .findViewById(R.id.tv_bi_smart_elite_maturity_age);
        TextView tv_bi_smart_elite_life_assured_gender = (TextView) d
                .findViewById(R.id.tv_bi_smart_elite_life_assured_gender);
        TextView tv_bi_smart_elite_policy_term = (TextView) d
                .findViewById(R.id.tv_bi_smart_elite_policy_term);
        TextView tv_bi_smart_elite_annualised_premium = (TextView) d
                .findViewById(R.id.tv_bi_smart_elite_annualised_premium);
        TextView tv_bi_smart_elite_sum_assured = (TextView) d
                .findViewById(R.id.tv_bi_smart_elite_sum_assured);
        TextView tv_bi_smart_elite_plan = (TextView) d
                .findViewById(R.id.tv_bi_smart_elite_plan);
        TextView tv_bi_smart_elite_premium_paying_term = (TextView) d
                .findViewById(R.id.tv_bi_smart_elite_premium_paying_term);
        TextView tv_bi_smart_elite_accidental_benefit_sum_assured = (TextView) d
                .findViewById(R.id.tv_bi_smart_elite_accidental_benefit_sum_assured);
        TextView tv_bi_smart_elite_yearly_premium = (TextView) d
                .findViewById(R.id.tv_bi_smart_elite_yearly_premium);
        TextView tv_bi_smart_elite_premium_frequency_mode = (TextView) d
                .findViewById(R.id.tv_bi_smart_elite_premium_frequency_mode);
        TextView tv_smart_elite_equity_elite_fund3_allocation = (TextView) d
                .findViewById(R.id.tv_smart_elite_equity_elite_fund3_allocation);

        TextView tv_smart_elite_balanced_fund2_allocation = (TextView) d
                .findViewById(R.id.tv_smart_elite_balanced_fund2_allocation);

        TextView tv_smart_elite_bond_fund2_allocation = (TextView) d
                .findViewById(R.id.tv_smart_elite_bond_fund2_allocation);

        TextView tv_smart_elite_market_fund2_allocation = (TextView) d
                .findViewById(R.id.tv_smart_elite_market_fund2_allocation);

        TextView tv_smart_elite_no_of_years_elapsed = (TextView) d
                .findViewById(R.id.tv_smart_elite_no_of_years_elapsed);
        TextView tv_smart_elite_reduction_yield = (TextView) d
                .findViewById(R.id.tv_smart_elite_reduction_yield);
        TextView tv_smart_elite_maturity_age2 = (TextView) d
                .findViewById(R.id.tv_smart_elite_maturity_age2);
        TextView tv_smart_elite_reduction_yield2 = (TextView) d
                .findViewById(R.id.tv_smart_elite_reduction_yeild2);

        TextView tv_mandatory_bi_smart_elite_life_assured_name = (TextView) d
                .findViewById(R.id.tv_mandatory_bi_smart_elite_life_assured_name);

        tv_mandatory_bi_smart_elite_life_assured_name.setText(name_of_life_assured);

        TextView tv_name_of_proposer = (TextView) d
                .findViewById(R.id.tv_name_of_proposer);

        tv_name_of_proposer.setText(name_of_life_assured);

       /* TextView tv_bi_smart_elite_strategy_opted_for = (TextView) d
                .findViewById(R.id.tv_bi_smart_elite_strategy_opted_for);

        tv_bi_smart_elite_strategy_opted_for.setText(bi_smart_elite_Strategy);*/


        TextView tv_smart_elite_pure_fund_allocation = (TextView) d
                .findViewById(R.id.tv_smart_elite_pure_fund_allocation);
        TextView tv_smart_elite_bond_optimiser_fund2_allocation = (TextView) d
                .findViewById(R.id.tv_smart_elite_bond_optimiser_fund2_allocation);
        TextView tv_smart_elite_midcap_fund_allocation = (TextView) d
                .findViewById(R.id.tv_smart_elite_midcap_fund_allocation);
        TextView tv_power_insurance_corporate_fund_allocation = (TextView) d.findViewById(R.id.tv_power_insurance_corporate_fund_allocation);


        TextView tv_bi_is_Staff = (TextView) d
                .findViewById(R.id.tv_bi_is_Staff);

        LinearLayout ll_plan_type = (LinearLayout) d
                .findViewById(R.id.ll_plan_type);
        final EditText edt_Policyholderplace = (EditText) d
                .findViewById(R.id.edt_Policyholderplace);

        // M_ChannelDetails list_channel_detail = db.getChannelDetail(sr_Code);
        //
        // edt_Policyholderplace.setText(list_channel_detail.getBranchName());

        final TextView edt_proposer_name = d
                .findViewById(R.id.edt_ProposalName);

        final CheckBox cb_statement = d
                .findViewById(R.id.cb_statement);
        cb_statement.setChecked(false);
        TextView tv_accidental_death_benefit = d
                .findViewById(R.id.tv_accidental_death_benefit);
        // TextView tv_accidental_total_and_permanent_death_benefit = (TextView)
        // d
        // .findViewById(R.id.tv_accidental_total_and_permanent_death_benefit);

        TextView tv_smart_elite_premium_tag = d
                .findViewById(R.id.tv_smart_elite_premium_tag);

        // TextView tv_accidental_sum_assured_installment = (TextView) d
        // .findViewById(R.id.tv_accidental_sum_assured_installment);
        // TextView tv_accidental_death_benefit1 = (TextView) d
        // .findViewById(R.id.tv_accidental_death_benefit1);

        LinearLayout ll_smart_elite_premium_frequency_mode = (LinearLayout) d
                .findViewById(R.id.ll_smart_elite_premium_frequency_mode);
        LinearLayout ll_smart_elite_premium_paying_term = (LinearLayout) d
                .findViewById(R.id.ll_smart_elite_premium_paying_term);
        LinearLayout ll_smart_elite_premium_amount = (LinearLayout) d
                .findViewById(R.id.ll_smart_elite_premium_amount);

        TextView tv_smart_elite_net_yield_8 = (TextView) d
                .findViewById(R.id.tv_smart_elite_net_yield_8);

        TextView tv_smart_elite_net_yield_4 = (TextView) d
                .findViewById(R.id.tv_smart_elite_net_yield_4);

        TextView tv_ppt_elite = (TextView) d
                .findViewById(R.id.tv_ppt_elite);



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
        /* parivartan changes */
        imageButtonSmartEliteProposerPhotograph = d
                .findViewById(R.id.imageButtonSmartEliteProposerPhotograph);
        imageButtonSmartEliteProposerPhotograph
                .setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Check = "Photo";
                        commonMethods.windowmessage(context, "_cust1Photo.jpg");
                    }
                });
        /* end */

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
        netYield8 = prsObj.parseXmlTag(output, "netYield8Pr");
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
                            + ", have received the information with respect to the above and have understood the above statement before entering into the contract.");
            edt_proposer_name_need_analysis
                    .setText("I, "
                            + name_of_life_assured
                            + ", have undergone the Need Analysis after having reviewed the SBI Life Product options, I have opted for SBI LIFE - Smart Elite.");

            // tv_life_assured_name.setText(name_of_life_assured);
            tv_proposername.setText(name_of_life_assured);
        } else if (proposer_Is_Same_As_Life_Assured.equalsIgnoreCase("n")) {
            edt_proposer_name
                    .setText("I, "
                            + name_of_proposer
                            + ", have received the information with respect to the above and have understood the above statement before entering into the contract.");
            edt_proposer_name_need_analysis
                    .setText("I, "
                            + name_of_proposer
                            + ", have undergone the Need Analysis after having reviewed the SBI Life Product options, I have opted for SBI LIFE - Smart Elite.");

            tv_proposername.setText(name_of_proposer);
        }

        agentdeclaration = "I, " + commonMethods.getUserName(context)
                + " have explained the premiums, charges and benefits under the policy fully to the prospect/policyholder.";

        TextView textviewAGentStatement = d.findViewById(R.id.textviewAGentStatement);
        textviewAGentStatement.setText(agentdeclaration);


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
                            // windowmessagesgin();
                            // windowmessageProposersgin();
                            /* parivartan changes */
                            commonMethods.windowmessageProposersgin(context,
                                    NeedAnalysisActivity.URN_NO + "_cust1sign");
                            /* end */
                        } else {
                            commonMethods.dialogWarning(context, "Please Tick on I Agree Clause ", true);
                            setFocusable(cb_statement);
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

        /* parivartan changes */


        if (photoBitmap != null) {
            imageButtonSmartEliteProposerPhotograph.setImageBitmap(photoBitmap);
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

                            String customerPhotoName = NeedAnalysisActivity.URN_NO
                                    + "_cust1Photo.jpg";
                            File customerPhotoFile = mStorageUtils.createFileToAppSpecificDir(context,
                                    customerPhotoName);
                            if (customerPhotoFile.exists()) {
                                customerPhotoFile.delete();
                            }

                            photoBitmap = null;
                            imageButtonSmartEliteProposerPhotograph
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

        premiumAmount = prsObj.parseXmlTag(input, "premiumAmount");
        btn_proceed.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
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
                        /* parivartan changes */
                        && (((photoBitmap != null
                        //remove parivartan validation
								/*&& ((radioButtonDepositPaymentNo.isChecked() == true && !thirdPartySign
										.equals("")) || radioButtonDepositPaymentYes
										.isChecked() == true) && ((radioButtonAppointeeYes
								.isChecked() == true && !appointeeSign
								.equals("")) || radioButtonAppointeeNo
								.isChecked() == true)*/
                ) && radioButtonTrasactionModeParivartan
                        .isChecked()) || radioButtonTrasactionModeManual
                        .isChecked())) {
                    NeedAnalysisActivity.str_need_analysis = "";

                    // String isActive = "0";
                    String productCode = "";

                    if (smartEliteBean.getPlanOption().equals("Gold"))
                        productCode = "UPE3PGC0";
                    else
                        productCode = "UPE3PPC0";

                    na_cbi_bean = new NA_CBI_bean(QuatationNumber, agentcode,
                            "", userType, "", lifeAssured_Title,
                            lifeAssured_First_Name, lifeAssured_Middle_Name,
                            lifeAssured_Last_Name, planName, obj
                            .getRound(sum_assured), obj
                            .getRound(premiumAmount), emailId,
                            mobileNo, agentEmail, agentMobile, na_input,
                            na_output, premFreqMod, Integer
                            .parseInt(policy_term), 0, productCode,
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
                            "", QuatationNumber, planName, getCurrentDate(),
                            mobileNo, getCurrentDate(), dbHelper.GetUserCode(),
                            emailId, "", "", agentcode, "", userType, "",
                            lifeAssured_Title, lifeAssured_First_Name,
                            lifeAssured_Middle_Name, lifeAssured_Last_Name, obj
                            .getRound(sum_assured), obj
                            .getRound(premiumAmount), agentEmail,
                            agentMobile, na_input, na_output, premFreqMod,
                            Integer.parseInt(policy_term), 0, productCode,
                            getDate(lifeAssured_date_of_birth), "", "", mode, inputVal
                            .toString(), retVal.toString().replace(
                            bussIll.toString(), "")));

                    CreateSmartEliteBIPDf();

                    NABIObj.serviceHit(BI_SmartEliteActivity.this, na_cbi_bean,
                            newFile, needAnalysispath.getPath(),
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
                    }
                    /* parivartan changes */
                    else if (!radioButtonTrasactionModeParivartan.isChecked()
                            && !radioButtonTrasactionModeManual.isChecked()) {
                        commonMethods.dialogWarning(context, "Please Select Transaction Mode", true);
                        setFocusable(linearlayoutTrasactionModeParivartan);
                        linearlayoutTrasactionModeParivartan.requestFocus();
                    } else if (photoBitmap == null) {
                        commonMethods.dialogWarning(context, "Please Capture the Photo", true);
                        setFocusable(imageButtonSmartEliteProposerPhotograph);
                        imageButtonSmartEliteProposerPhotograph.requestFocus();
                    }
                    /* end */
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


        gender = prsObj.parseXmlTag(input, "gender");
        tv_bi_smart_elite_life_assured_gender.setText(gender);

        String staffdiscount = prsObj.parseXmlTag(input, "isStaff");

        if (staffdiscount.equalsIgnoreCase("true")) {
            // tr_staff_per.setVisibility(View.VISIBLE);
            tv_bi_is_Staff.setText("Yes");
            // tv_bi_flexi_smart_plus_staff_per.setText(staffdiscount_per);
        } else {

            // .setVisibility(View.GONE);
            tv_bi_is_Staff.setText("No");

        }
        premFreqMod = prsObj.parseXmlTag(input, "premFreqMode");

        premium_paying_term = prsObj.parseXmlTag(input, "premPayingTerm");
        if (selPremFreq.getSelectedItem().toString().equals("Single")) {
            tv_ppt_elite.setText("One time at the inception of the policy");
            tv_bi_smart_elite_premium_frequency_mode.setText("One time at the inception of the policy");
        } else {
            tv_ppt_elite.setText(premium_paying_term);
            tv_bi_smart_elite_premium_frequency_mode.setText(premFreqMod);
        }

        String premFreq = prsObj.parseXmlTag(input, "premFreq");
        if (premFreq.equalsIgnoreCase("single")) {
            // ll_smart_elite_premium_frequency_mode.setVisibility(View.GONE);
            // ll_smart_elite_premium_paying_term.setVisibility(View.INVISIBLE);
            ll_smart_elite_premium_amount.setVisibility(View.INVISIBLE);
            tv_smart_elite_premium_tag.setText("Amount of Installment Premium");
        } else if (premFreq.equalsIgnoreCase("Limited")) {
            ll_smart_elite_premium_frequency_mode.setVisibility(View.VISIBLE);
            ll_smart_elite_premium_paying_term.setVisibility(View.VISIBLE);
            ll_smart_elite_premium_amount.setVisibility(View.VISIBLE);
            tv_smart_elite_premium_tag.setText("Amount of Installment Premium");

            tv_bi_smart_elite_premium_frequency_mode.setText(premFreqMod);
        }

        netYield4 = prsObj.parseXmlTag(output, "netYield4Pr");
        netYield8 = prsObj.parseXmlTag(output, "netYield8Pr");

        tv_smart_elite_net_yield_4.setText(netYield4 + "%");
        tv_smart_elite_net_yield_8.setText(netYield8 + "%");

        age_entry = prsObj.parseXmlTag(input, "age");
        tv_bi_smart_elite_age_entry.setText(age_entry + " Years");
        tv_age_of_proposer.setText(age_entry + " Years");

        maturity_age = prsObj.parseXmlTag(output, "maturityAge");
        tv_bi_smart_elite_maturity_age.setText(maturity_age + " Years");

        annualised_premium = prsObj.parseXmlTag(output, "AnnPrem");
        tv_bi_smart_elite_annualised_premium
                .setText("Rs. "
                        + obj.getRound(obj.getStringWithout_E(Double
                        .valueOf((premiumAmount.equals("") || premiumAmount == null) ? "0"
                                : premiumAmount))));

        AnnualInstallment = prsObj.parseXmlTag(output, "AnnualInstallment");
        // tv_accidental_sum_assured_installment.setText("Rs."
        // + obj.getRound(obj.getStringWithout_E(Double
        // .valueOf(AnnualInstallment.equals("") ? "0"
        // : AnnualInstallment))));

        policy_term = prsObj.parseXmlTag(output, "policyTerm");
        tv_bi_smart_elite_policy_term.setText(policy_term + " Years ");
        tv_smart_elite_maturity_age2.setText(policy_term + " Years ");

        sum_assured = prsObj.parseXmlTag(output, "sumAssured");
        tv_bi_smart_elite_sum_assured.setText("Rs. "
                + getformatedThousandString(Integer.parseInt(sum_assured)));
        tv_guaranted_death.setText(" Rs." + sum_assured);

        plan = prsObj.parseXmlTag(input, "plan");
        tv_bi_smart_elite_plan.setText(plan);

        premium_paying_term = prsObj.parseXmlTag(input, "premPayingTerm");
        tv_bi_smart_elite_premium_paying_term.setText(premium_paying_term
                + " Years");


        tv_bi_smart_elite_yearly_premium.setText("Rs. " + premiumAmount);

        accidental_benefit_sumassured = prsObj.parseXmlTag(output,
                "accBenfSumAssured");
        tv_bi_smart_elite_accidental_benefit_sum_assured.setText("Rs. "
                + obj.getRound(obj.getStringWithout_E(Double
                .valueOf(accidental_benefit_sumassured.equals("") ? "0"
                        : accidental_benefit_sumassured))));

        // tv_accidental_death_benefit
        // .setText("Rs."
        // +
        // obj.getRound(obj.getStringWithout_E(Double.valueOf((accidental_benefit_sumassured
        // .equals("") || accidental_benefit_sumassured == null) ? "0"
        // : accidental_benefit_sumassured))) + ".  ");

        tv_accidental_death_benefit
                .setText("13) In case of accidental death or accidental total  and permanent disability, whichever  is earlier,  this policy provides additional guaranteed benefit of Rs. "
                        + currencyFormat.format(Math.min(Double
                        .parseDouble(prsObj.parseXmlTag(output,
                                "accBenfSumAssured")), 5000000))
                        + ". In case of accidental death Rs. "
                        + currencyFormat.format(Math.min(Double
                        .parseDouble(prsObj.parseXmlTag(output,
                                "accBenfSumAssured")), 5000000))
                        + " will be paid as lump sum where as in case of accidental total and permenant disability Rs. "
                        + currencyFormat.format(Math.min(Double
                        .parseDouble(prsObj.parseXmlTag(output,
                                "accBenfSumAssured")), 5000000))
                        + " would be paid in 10  equal annual installments  of Rs. "
                        + currencyFormat.format(Math.min(Double
                        .parseDouble(prsObj.parseXmlTag(output,
                                "accBenfSumAssured")), 5000000) * 0.1)
                        + ".");


        percentage_equity_elite_fund3 = (prsObj.parseXmlTag(input,
                "perInvEquityEliteIIFund"));
        tv_smart_elite_equity_elite_fund3_allocation
                .setText((percentage_equity_elite_fund3.equals("") ? "0"
                        : percentage_equity_elite_fund3) + " % ");

        percentage_bond_fund2 = (prsObj.parseXmlTag(input, "perInvBondFund"));
        tv_smart_elite_bond_fund2_allocation.setText((percentage_bond_fund2
                .equals("") ? "0" : percentage_bond_fund2) + " % ");
        //tv_smart_elite_bond_plan_fmc.setText("1.00 % ");

        percentage_balanced_fund2 = (prsObj.parseXmlTag(input,
                "perInvBalancedFund"));
        tv_smart_elite_balanced_fund2_allocation
                .setText((percentage_balanced_fund2.equals("") ? "0"
                        : percentage_balanced_fund2) + " % ");
        //tv_smart_elite_balanced_fund_fmc.setText("1.25 % ");

        // String percentage_index_fund = prsObj.parseXmlTag(input,
        // "perInvIndexFund");

        percentage_money_market_fund2 = (prsObj.parseXmlTag(input,
                "perInvMoneyMarketFund"));
        tv_smart_elite_market_fund2_allocation
                .setText((percentage_money_market_fund2.equals("") ? "0"
                        : percentage_money_market_fund2) + " % ");
        //tv_smart_elite_market_fund_fmc.setText("0.25 % ");


        percent_PureFund = (prsObj.parseXmlTag(input,
                "perInvPureFund"));

        percent_MidcapFund = (prsObj.parseXmlTag(input,
                "perInvMidcapFund"));
        percent_BondOptimiserFund2 = (prsObj.parseXmlTag(input,
                "perInvBondOptimiserFund"));

        percent_EquityFund = (prsObj.parseXmlTag(input,
                "perInvEquityFund"));
        tv_smart_elite_bond_optimiser_fund2_allocation
                .setText((percent_BondOptimiserFund2.equals("") ? "0"
                        : percent_BondOptimiserFund2) + " % ");
        //tv_smart_elite_market_fund_fmc.setText("0.25 % ");
        tv_smart_elite_midcap_fund_allocation
                .setText((percent_MidcapFund.equals("") ? "0"
                        : percent_MidcapFund) + " % ");
        tv_smart_elite_pure_fund_allocation
                .setText((percent_PureFund.equals("") ? "0"
                        : percent_PureFund) + " % ");
        perInvCorporateBondFund = (prsObj.parseXmlTag(input, "perInvCorporateBondFund"));
        tv_power_insurance_corporate_fund_allocation
                .setText((perInvCorporateBondFund.equals("") ? "0"
                        : perInvCorporateBondFund) + " %");
        no_of_year_elapsed = prsObj.parseXmlTag(input, "noOfYrElapsed");
        tv_smart_elite_no_of_years_elapsed.setText(no_of_year_elapsed
                + " Years ");

        red_in_year_at_8_no_of_year = prsObj.parseXmlTag(output,
                "redInYieldNoYr");
        tv_smart_elite_reduction_yield.setText(red_in_year_at_8_no_of_year
                + " % ");

        red_in_year_at_8_maturity_age = prsObj.parseXmlTag(output,
                "redInYieldMat");
        tv_smart_elite_reduction_yield2.setText(red_in_year_at_8_maturity_age
                + " % ");

        TextView tv_Company_policy_surrender_dec = (TextView) d
                .findViewById(R.id.tv_Company_policy_surrender_dec);

        if (premFreq.equalsIgnoreCase("Single")) {

            Company_policy_surrender_dec = "Your SBI LIFE - Smart Elite  (111L072V04) is a "
                    + "Single"
                    + " Premium Policy and you are required to pay premium once "

                    + "at the inception of the policy of Rs. "
                    + obj.getRound(obj.getStringWithout_E(Double
                    .valueOf((annualised_premium.equals("") || annualised_premium == null) ? "0"
                            : annualised_premium)))
                    + " .Your Policy Term is "
                    + policy_term
                    + " years"
                    + " ,Premium Payment Term is "
                    + " Not Applicable" + " and Sum Assured is Rs. " +

                    getformatedThousandString(Integer.parseInt(sum_assured));


        } else if (premFreq.equalsIgnoreCase("Limited")) {

            Company_policy_surrender_dec = "Your SBI LIFE - Smart Elite  (111L072V04) is a "
                    + "Limited"
                    + " Premium Policy and you are required to pay "

                    + premFreqMod + " premium of Rs."
                    + obj.getRound(obj.getStringWithout_E(Double
                    .valueOf((annualised_premium.equals("") || annualised_premium == null) ? "0"
                            : annualised_premium)))
                    + " .Your Policy Term is "
                    + policy_term
                    + " years"
                    + " , Premium Payment Term is "
                    + premium_paying_term
                    + " years" + " and Sum Assured is Rs. " +

                    getformatedThousandString(Integer.parseInt(sum_assured));
        }

        tv_Company_policy_surrender_dec.setText(Company_policy_surrender_dec);
        for (int i = 1; i <= Integer.parseInt(policy_term); i++) {
            String policy_year = prsObj
                    .parseXmlTag(output, "policyYr" + i + "");
            String premium = prsObj.parseXmlTag(output, "AnnPrem" + i + "");
            String premium_allocation_charge = prsObj.parseXmlTag(output,
                    "PremAllCharge" + i + "");
            String amount_available_for_investment = prsObj.parseXmlTag(output,
                    "AmtAvlForInvst" + i + "");
            String policy_administration_charge = prsObj.parseXmlTag(output,
                    "PolicyAdmCharge" + i + "");
            String mortality_charge1 = prsObj.parseXmlTag(output, "MortChrg4Pr"
                    + i + "");
            String total_charge1 = prsObj.parseXmlTag(output, "OtherCharges4Pr_PartA"
                    + i + "");
            String total_service_tax1 = prsObj.parseXmlTag(output,
                    "TotalSerTax4Pr" + i + "");
            String addition_to_fund1 = prsObj.parseXmlTag(output,
                    "AddToFundIfAny4Pr" + i + "");

            String fund_before_fmc1 = prsObj.parseXmlTag(output,
                    "FundBefFMC4Pr" + i + "");
            String fund_before_fmc2 = prsObj.parseXmlTag(output,
                    "FundBefFMC8Pr" + i + "");
            String fund_management_charge1 = prsObj.parseXmlTag(output,
                    "FundMgmtChrg4Pr" + i + "");
            String guranteed_addition1 = prsObj.parseXmlTag(output,
                    "GuareentedAdd4Pr" + i + "");
            String fund_value_at_end1 = prsObj.parseXmlTag(output,
                    "FundValAtEnd4Pr" + i + "");
            String surrender_value1 = prsObj.parseXmlTag(output,
                    "SurrenderVal4Pr" + i + "");
            String death_benefit1 = prsObj.parseXmlTag(output,
                    "DeathBen4Pr" + i + "");
            String mortality_charge2 = prsObj.parseXmlTag(output, "MortChrg8Pr"
                    + i + "");
            String total_charge2 = prsObj.parseXmlTag(output, "OtherCharges8Pr_PartA"
                    + i + "");
            String total_service_tax2 = prsObj.parseXmlTag(output,
                    "TotalSerTax8Pr" + i + "");
            String addition_to_fund2 = prsObj.parseXmlTag(output,
                    "AddToFundIfAny8Pr" + i + "");
            String fund_management_charge2 = prsObj.parseXmlTag(output,
                    "FundMgmtChrg8Pr" + i + "");
            String guranteed_addition2 = prsObj.parseXmlTag(output,
                    "GuaranteedAdd8Pr" + i + "");
            String fund_value_at_end2 = prsObj.parseXmlTag(output,
                    "FundValAtEnd8Pr" + i + "");
            String surrender_value2 = prsObj.parseXmlTag(output,
                    "SurrenderVal8Pr" + i + "");
            String death_benefit2 = prsObj.parseXmlTag(output,
                    "DeathBen8Pr" + i + "");
            String commission = prsObj.parseXmlTag(output, "CommIfPay8Pr" + i
                    + "");

            String ATPDChrg = prsObj.parseXmlTag(output, "ATPDChrg" + i
                    + "");


            list_data.add(new M_BI_EliteGrid_AdapterCommon(policy_year, obj
                    .getRound(obj.getStringWithout_E(Double.valueOf(premium)))
                    + "", mortality_charge1, total_charge1,
                    total_service_tax1, fund_value_at_end1, surrender_value1, death_benefit1,
                    mortality_charge2, total_charge2, total_service_tax2, fund_value_at_end2,
                    surrender_value2, death_benefit2, commission));


            list_data1.add(new M_BI_EliteGrid_AdapterCommon2(policy_year, obj
                    .getRound(obj.getStringWithout_E(Double.valueOf(premium)))
                    + "", premium_allocation_charge,
                    amount_available_for_investment, mortality_charge2, total_service_tax2, policy_administration_charge, ATPDChrg,
                    "0", addition_to_fund2, fund_before_fmc2, fund_management_charge2,
                    fund_value_at_end2, surrender_value2, death_benefit2));


            list_data2.add(new M_BI_EliteGrid_AdapterCommon2(policy_year, obj
                    .getRound(obj.getStringWithout_E(Double.valueOf(premium)))
                    + "", premium_allocation_charge,
                    amount_available_for_investment, mortality_charge1, total_service_tax1,
                    policy_administration_charge, ATPDChrg,
                    "0", addition_to_fund1, fund_before_fmc1, fund_management_charge1,
                    fund_value_at_end1, surrender_value1, death_benefit1));


        }


        Adapter_BI_ElitegridCommon adapter = new Adapter_BI_ElitegridCommon(
                BI_SmartEliteActivity.this, list_data);
        gv_userinfo.setAdapter(adapter);

        Adapter_BI_ElitegridCommon2 adapter1 = new Adapter_BI_ElitegridCommon2(
                BI_SmartEliteActivity.this, list_data1);
        gv_userinfo2.setAdapter(adapter1);

        Adapter_BI_ElitegridCommon2 adapter2 = new Adapter_BI_ElitegridCommon2(
                BI_SmartEliteActivity.this, list_data2);
        gv_userinfo3.setAdapter(adapter2);


        GridHeight gh = new GridHeight();
        gh.getheight(gv_userinfo, policy_term);
        gh.getheight(gv_userinfo2, policy_term);
        gh.getheight(gv_userinfo3, policy_term);

        d.show();

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
                    final_age = Integer.toString(age);
                    if (final_age.contains("-")) {
                        commonMethods.BICommonDialog(context, "Please fill Valid Bith Date");
                    } else {
                        if (18 <= age) {

                            btn_bi_smart_elite_proposer_date.setText(date);
                            edt_bi_smart_elite_proposer_age.setText(final_age);
                            proposer_date_of_birth = getDate1(date + "");
                            // ageInYears
                            // .setSelection(getIndex(ageInYears, final_age));
                            // updateSAMFlabel();
                            // valPolicyTerm();
                        } else {
                            commonMethods.BICommonDialog(context, "Minimum age should be 18 yrs for proposer");
                            btn_bi_smart_elite_proposer_date.setText("Select Date");
                            edt_bi_smart_elite_proposer_age.setText("");
                            proposer_date_of_birth = "";
                        }
                    }
                    break;

                case 5:
                    final_age = Integer.toString(age);
                    if (final_age.contains("-")) {
                        commonMethods.BICommonDialog(context, "Please fill Valid Bith Date");
                    } else {
                        if (18 <= age && age <= 60 && (selPremFreq.getSelectedItem().toString().equals("Single"))) {
                            btn_bi_smart_elite_life_assured_date.setText(date);
                            edt_bi_smart_elite_life_assured_age.setText(final_age);
                            ageInYears.setSelection(
                                    getIndex(ageInYears, final_age), false);
                            lifeAssured_date_of_birth = getDate1(date + "");
                            updateSAMFlabel();
                            valPolicyTerm();
                            clearFocusable(btn_bi_smart_elite_life_assured_date);

                            setFocusable(edt_proposerdetail_basicdetail_contact_no);
                            edt_proposerdetail_basicdetail_contact_no
                                    .requestFocus();
                        } else if (18 <= age && age <= 55 && (selPremFreq.getSelectedItem().toString().equals("Limited"))) {
                            btn_bi_smart_elite_life_assured_date.setText(date);
                            edt_bi_smart_elite_life_assured_age.setText(final_age);
                            ageInYears.setSelection(
                                    getIndex(ageInYears, final_age), false);
                            lifeAssured_date_of_birth = getDate1(date + "");
                            updateSAMFlabel();
                            valPolicyTerm();
                            clearFocusable(btn_bi_smart_elite_life_assured_date);

                            setFocusable(edt_proposerdetail_basicdetail_contact_no);
                            edt_proposerdetail_basicdetail_contact_no
                                    .requestFocus();
                        } else {
                            if (selPremFreq.getSelectedItem().toString().equals("Single")) {
                                commonMethods.BICommonDialog(context, "Minimum Age should be 18 yrs and Maximum Age should be 60 yrs For LifeAssured");
                                btn_bi_smart_elite_life_assured_date
                                        .setText("Select Date");
                                edt_bi_smart_elite_life_assured_age.setText("");
                                lifeAssured_date_of_birth = "";
                                clearFocusable(btn_bi_smart_elite_life_assured_date);
                                setFocusable(btn_bi_smart_elite_life_assured_date);
                                btn_bi_smart_elite_life_assured_date.requestFocus();
                            } else {
                                commonMethods.BICommonDialog(context, "Minimum Age should be 18 yrs and Maximum Age should be 55 yrs For LifeAssured");
                                btn_bi_smart_elite_life_assured_date
                                        .setText("Select Date");
                                edt_bi_smart_elite_life_assured_age.setText("");
                                lifeAssured_date_of_birth = "";
                                clearFocusable(btn_bi_smart_elite_life_assured_date);
                                setFocusable(btn_bi_smart_elite_life_assured_date);
                                btn_bi_smart_elite_life_assured_date.requestFocus();
                            }


                        }
                    }
                    break;

                default:
                    break;
            }
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

    private void windowmessagesgin() {

        d = new Dialog(BI_SmartEliteActivity.this);
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
                Intent intent = new Intent(BI_SmartEliteActivity.this,
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

    public void windowmessageProposersgin() {

        d = new Dialog(BI_SmartEliteActivity.this);
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
                Intent intent = new Intent(BI_SmartEliteActivity.this,
                        ProposerCaptureSignature.class);
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

    /********************************** Calculations starts from here **********************************************************/

    private String[] getOutput(String sheetName,
                               BI_SmartEliteBean smartEliteBean1) {

        try {
            smartEliteBean = smartEliteBean1;
            bussIll = new StringBuilder();

            String[] arrGuaranteedAddition_AA = new String[12];
            String[] arrGuaranteedAddition_AP = new String[12];

            // Output Variable Declaration
            double _oneHundredPercentOfCummulativePremium_AW = 0;
            double _policyAdministrationCharge_Q = 0;
            double _guaranteedAddition_AA = 0;
            double _guaranteedAddition_AP = 0;
            double _fundValueAfterFMCandBeforeGA_Z = 0;
            double _fundValueAtEnd_AB = 0;
            double _fundValueAtEnd_AQ = 0;
            double _fundValueAfterFMCAndBeforeGA_AO = 0;
            int _month_E = 0;
            int _year_F = 0;
            String _policyInForce_G = "Y";
            int _age_H = 0;
            double _premium_I = 0;
            double _topUpPremium_J = 0;
            double _premiumAllocationCharge_K = 0;
            double _topUpCharges_L = 0;
            double _serviceTaxOnAllocation_M = 0;
            double _amountAvailableForInvestment_N = 0, _amountAvailableForInvestment_N1 = 0;
            double _riderCharges_P = 0;
            double _sumAssuredRelatedCharges_O = 0;
            double _mortalityCharges_R = 0;
            double _totalCharges_S = 0;
            double _totalServiceTax_ExclOfSTonAllocAndSurr_T = 0;
            double _additionToFundIfAny_V = 0;
            double _fundBeforeFMC_W = 0;
            double _fundManagementCharge_X = 0;
            double _serviceTaxOnFMC_Y = 0;
            double _totalServiceTax_U = 0;
            double _surrenderCap_AV = 0;
            double _surrenderCharges_AC = 0;
            double _serviceTaxOnSurrenderCharges_AD = 0;
            double _surrenderValue_AE = 0;
            double _deathBenefit_AF = 0;
            double _mortalityCharges_AG = 0;
            double _totalCharges_AH = 0;
            double _totalServiceTax_ExclOfSTonAllocAndsurr_AI = 0;
            double _additionToFundIfAny_AK = 0;
            double _fundBeforeFMC_AL = 0;
            double _fundManagementCharge_AM = 0;
            double _serviceTaxOnFMC_AN = 0;
            double _deathBenefit_AU = 0;
            double _totalServiceTax_AJ = 0;
            double _surrenderCharges_AR = 0;
            double _serviceTaxOnSurrenderCharges_AS = 0;
            double _surrenderValue_AT = 0;
            // //Temp Variable Declaretion
            int month_E = 0;
            int year_F = 0;
            String policyInForce_G = "Y";
            int age_H = 0;
            double premium_I = 0;
            double topUpPremium_J = 0;
            double premiumAllocationCharge_K = 0;
            double topUpCharges_L = 0;
            double serviceTaxOnAllocation_M = 0;
            double amountAvailableForInvestment_N = 0, amountAvailableForInvestment_N1 = 0;
            double riderCharges_P = 0;
            double sumAssuredRelatedCharges_O = 0;
            double policyAdministrationCharge_Q = 0;
            double oneHundredPercentOfCummulativePremium_AW = 0;
            double guaranteedAddition_AA = 0;
            double guaranteedAddition_AP = 0;
            double mortalityCharges_R = 0;
            double totalCharges_S = 0;
            double totalServiceTax_ExclOfSTonAllocAndSurr_T = 0;
            double additionToFundIfAny_V = 0;
            double fundBeforeFMC_W = 0;
            double fundManagementCharge_X = 0;
            double serviceTaxOnFMC_Y = 0;
            double totalServiceTax_U = 0;
            double fundValueAfterFMCandBeforeGA_Z = 0;
            double fundValueAtEnd_AB = 0;
            double fundValueAtEnd_AQ = 0;
            double surrenderCap_AV = 0;
            double surrenderCharges_AC = 0;
            double serviceTaxOnSurrenderCharges_AD = 0;
            double surrenderValue_AE = 0;
            double deathBenefit_AF = 0;
            double mortalityCharges_AG = 0;
            double totalCharges_AH = 0;
            double totalServiceTax_ExclOfSTonAllocAndsurr_AI = 0;
            double additionToFundIfAny_AK = 0;
            double fundBeforeFMC_AL = 0;
            double fundManagementCharge_AM = 0;
            double serviceTaxOnFMC_AN = 0;
            double fundValueAfterFMCAndBeforeGA_AO = 0;
            double deathBenefit_AU = 0;
            double totalServiceTax_AJ = 0;
            double surrenderCharges_AR = 0;
            double serviceTaxOnSurrenderCharges_AS = 0;
            double surrenderValue_AT = 0;
            double Commission_AQ = 0;

            // for BI
            double sum_I = 0,
                    sum_K = 0,
                    sum_N = 0,
                    sum_N1 = 0,
                    sum_Q = 0,
                    sum_R = 0,
                    sum_S = 0,
                    sum_U = 0,
                    sum_V = 0,
                    sum_X = 0,
                    sum_AA = 0,
                    sum_AB = 0,
                    sum_AE = 0,
                    sum_AF = 0,
                    sum_AG = 0,
                    sum_AH = 0,
                    sum_AJ = 0,
                    sum_AK = 0,
                    sum_AL = 0,
                    sum_AM = 0,
                    sum_AP = 0,
                    sum_AQ = 0,
                    sum_AT = 0,
                    sum_AU = 0,
                    sum_J = 0,
                    sum_Z = 0, sum_01 = 0, sum_02 = 0, sum_03 = 0, sum1_03 = 0,
                    sum_AO = 0, otherCharges_PartB = 0, otherCharges1_PartB = 0, otherCharges_PartA = 0, otherCharges1_PartA = 0;

            double _sum_X = 0,
                    _sum_AM = 0,
                    _sum_J = 0,
                    _sum_I = 0;

            // From GUI Input
            boolean staffDisc = smartEliteBean1.getIsForStaffOrNot();
            boolean bancAssuranceDisc = smartEliteBean1
                    .getIsBancAssuranceDiscOrNot();

            String addTopUp = "No";
            String premFreq = smartEliteBean1.getPremFreq();
            double serviceTax = 0;
            boolean isKerlaDisc = smartEliteBean.isKerlaDisc();
            double SAMF = smartEliteBean1.getSAMF();
            int ageAtEntry = smartEliteBean1.getAgeAtEntry();
            String planOption = smartEliteBean1.getPlanOption();

            // Internally Calculated Input Fields
            double effectivePremium = smartEliteBean1.getEffectivePremium();
            int PF = smartEliteBean1.getPF();
            int policyTerm = smartEliteBean1.getPolicyTerm_Basic();
            int premiumPayingTerm = smartEliteBean1.getPremiumPayingTerm();


            double sumAssured = (effectivePremium * SAMF);
            smartEliteBean1.setEffectiveTopUpPrem(addTopUp, premFreq,
                    prop.topUpPremiumAmt);
            double effectiveTopUpPrem = smartEliteBean1.getEffectiveTopUpPrem();
            smartEliteBean1.setAccidentBenefitSumAssured(sumAssured);
//        double accidentBenefitSumAssured = smartEliteBean1
//                .getAccidentBenefitSumAsssured();
            // Declaration of method Variables/Object required for calculation
            BI_SmartEliteBusinessLogic BIMAST = new BI_SmartEliteBusinessLogic();
            String[] forBIArray = BIMAST
                    .getForBIArr(prop.mortalityCharges_AsPercentOfofLICtable);
            int rowNumber = 0;

	/*	double percentToBeInvested_EquityEliteIIFund =smartEliteBean.getPercentToBeInvested_EquityEliteIIFund();
            double percentToBeInvested_BalancedFund = smartEliteBean.getPercentToBeInvested_BalancedFund();
            double percentToBeInvested_BondFund = smartEliteBean.getPercentToBeInvested_BondFund();
            double percentToBeInvested_MoneyMarketFund = smartEliteBean.getPercentToBeInvested_MoneyMarketFund();
            double percentToBeInvested_BondOptimiserFund = smartEliteBean.getPercentToBeInvested_BondOptimiserFund();
            double percentToBeInvested_EquityFund = smartEliteBean.getPercentToBeInvested_CorpBondFund();
            double percentToBeInvested_MidcapFund = smartEliteBean.getPercentToBeInvested_MidcapFund();
		double percentToBeInvested_PureFund=smartEliteBean.getPercentToBeInvested_PureFund();
*/


            double percentToBeInvested_EquityEliteIIFund = smartEliteBean.getPercentToBeInvested_EquityEliteIIFund();
            double percentToBeInvested_BalancedFund = smartEliteBean.getPercentToBeInvested_BalancedFund();
            double percentToBeInvested_BondFund = smartEliteBean.getPercentToBeInvested_BondFund();
            double percentToBeInvested_MoneyMarketFund = smartEliteBean.getPercentToBeInvested_MoneyMarketFund();
            double percentToBeInvested_BondOptimiserFund = smartEliteBean.getPercent_BondOptimiserFund();
            double percentToBeInvested_EquityFund = smartEliteBean.getPercentToBeInvested_CorpBondFund();
            double percentToBeInvested_MidcapFund = smartEliteBean.getPercent_MidcapFund();
            double percentToBeInvested_PureFund = smartEliteBean.getPercent_PureFund();

            /*
             * changes to be made in connect life on 30/12/2014 add in getoutput
             */
            double staffDiscPercentage = BIMAST.getStaffDiscPercentage(staffDisc,
                    premiumPayingTerm, premFreq);
//        System.out.println("percentage " + staffDiscPercentage);
            bussIll.append("<staffDiscPercentage>" + staffDiscPercentage
                    + "</staffDiscPercentage>");

            bussIll.append("<staffDiscCode>" + BIMAST.getStaffDiscCode(staffDisc)
                    + "</staffDiscCode>");
            for (int i = 0; i < (policyTerm * 12); i++)
            // for(int i=1;i<73;i++)
            {
                rowNumber++;
                // System.out.println("********************************************* "+i+" Row Output *********************************************");
                BIMAST.setMonth_E(rowNumber);
                month_E = Integer.parseInt(BIMAST.getMonth_E());
                _month_E = month_E;
                // System.out.println("1.   Month_E : "+month_E);

                BIMAST.setYear_F();
                year_F = Integer.parseInt(BIMAST.getYear_F());
                _year_F = year_F;
                // System.out.println("2.   Year_F : "+year_F);

                if ((_month_E % 12) == 0) {
                    // System.out.println("_year_F "+_year_F);
                    bussIll.append("<policyYr" + _year_F + ">" + _year_F
                            + "</policyYr" + _year_F + ">");
                    // System.out.println("_year_F "+_year_F);

                }
               /* if (isKerlaDisc == true && _year_F <= 2) {
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


                policyInForce_G = BIMAST.getPolicyInForce_G();
                _policyInForce_G = BIMAST.getPolicyInForce_G();
                // System.out.println("3.   PolicyInForce_G : "+policyInForce_G);

                BIMAST.setAge_H(ageAtEntry);
                age_H = Integer.parseInt(BIMAST.getAge_H());
                _age_H = age_H;
                // System.out.println("4.   Age_H : "+age_H);

                BIMAST.setPremium_I(premiumPayingTerm, PF, effectivePremium);
                premium_I = Double.parseDouble(BIMAST.getPremium_I());
                _premium_I = premium_I;
                // System.out.println("5.   Premium_I : "+premium_I);

                sum_I += _premium_I;
                if ((_month_E % 12) == 0) {
                    bussIll.append("<AnnPrem" + _year_F + ">"
                            + commonForAllProd.getStringWithout_E(sum_I)
                            + "</AnnPrem" + _year_F + ">");
                    _sum_I = sum_I;
                    sum_I = 0;
                }

                BIMAST.setTopUpPremium_J(prop.topUpStatus, effectiveTopUpPrem,
                        addTopUp);
                topUpPremium_J = Double.parseDouble(BIMAST.getTopUpPremium_J());
                _topUpPremium_J = topUpPremium_J;
                // System.out.println("6.   TopUpPremium_J : "+topUpPremium_J);

                sum_J += _topUpPremium_J;
                if ((_month_E % 12) == 0) {

                    // bussIll.append("<AnnPrem"+ _year_F +">" + sum_J +
                    // "</AnnPrem"+ _year_F +">");
                    _sum_J = sum_J;
                    sum_J = 0;
                }

                BIMAST.setPremiumAllocationCharge_K(staffDisc, bancAssuranceDisc,
                        premFreq);
                premiumAllocationCharge_K = Double.parseDouble(BIMAST
                        .getPremiumAllocationCharge_K());
                _premiumAllocationCharge_K = premiumAllocationCharge_K;
                // System.out.println("7.   PremiumAllocationCharge_K : "+premiumAllocationCharge_K);

                sum_K += _premiumAllocationCharge_K;
                sum_01 = sum_K;
                if ((_month_E % 12) == 0) {

                    bussIll.append("<PremAllCharge"
                            + _year_F
                            + ">"
                            + commonForAllProd.getRoundUp(commonForAllProd
                            .getStringWithout_E(sum_K)) + "</PremAllCharge"
                            + _year_F + ">");
                    sum_K = 0;
                }

                BIMAST.setTopUpCharges_L(prop.topUp);
                topUpCharges_L = Double.parseDouble(BIMAST.getTopUpCharges_L());
                _topUpCharges_L = topUpCharges_L;
                // System.out.println("8.   TopUpCharges_L : "+topUpCharges_L);

                BIMAST.setServiceTaxOnAllocation_M(prop.allocationCharges,
                        serviceTax);
                serviceTaxOnAllocation_M = Double.parseDouble(BIMAST
                        .getServiceTaxOnAllocation_M());
                _serviceTaxOnAllocation_M = serviceTaxOnAllocation_M;
                // System.out.println("9.   ServiceTaxOnAllocation_M : "+serviceTaxOnAllocation_M);

                BIMAST.setAmountAvailableForInvestment_N();
                amountAvailableForInvestment_N = Double.parseDouble(BIMAST
                        .getAmountAvailableForInvestment_N());
                _amountAvailableForInvestment_N = amountAvailableForInvestment_N;
                // System.out.println("10.   AmountAvailableForInvestment_N : "+amountAvailableForInvestment_N);

                BIMAST.setAmountAvailableForInvestment_N1();
                amountAvailableForInvestment_N1 = Double.parseDouble(BIMAST.getAmountAvailableForInvestment_N1());
                _amountAvailableForInvestment_N1 = amountAvailableForInvestment_N1;
                sum_N1 += _amountAvailableForInvestment_N1;

                sum_N += _amountAvailableForInvestment_N;
                if ((_month_E % 12) == 0) {
                    bussIll.append("<AmtAvlForInvst"
                            + _year_F
                            + ">"
                            + commonForAllProd.getRoundUp(commonForAllProd
                            .getStringWithout_E(sum_N1))
                            + "</AmtAvlForInvst" + _year_F + ">");
                    sum_N1 = 0;
                }

                BIMAST.setSumAssuredRelatedCharges_O(
                        prop.noOfYearsForSArelatedCharges, sumAssured, SAMF,
                        effectivePremium, prop.charge_SumAssuredBase);
                sumAssuredRelatedCharges_O = Double.parseDouble(BIMAST
                        .getSumAssuredRelatedCharges_O());
                _sumAssuredRelatedCharges_O = sumAssuredRelatedCharges_O;
                // System.out.println("11.   Sum/AssuredRelatedCharges_O : "+sumAssuredRelatedCharges_O);

                BIMAST.setRiderCharges_P(sumAssured);
                riderCharges_P = Double.parseDouble(BIMAST.getRiderCharges_P());
                _riderCharges_P = _riderCharges_P + riderCharges_P;
                // System.out.println("12.   RiderCharges_P : "+riderCharges_P);

                if ((_month_E % 12) == 0) {
                    bussIll.append("<ATPDChrg"
                            + _year_F
                            + ">"
                            + commonForAllProd.getRound(commonForAllProd
                            .getStringWithout_E(_riderCharges_P))
                            + "</ATPDChrg" + _year_F + ">");
//				_riderCharges_P =0;
                }

                BIMAST.setPolicyAdministrationCharge_Q(
                        _policyAdministrationCharge_Q, prop.charge_Inflation,
                        prop.fixedMonthlyExp_SP, prop.fixedMonthlyExp_RP,
                        prop.inflation_pa_SP, prop.inflation_pa_RP, premFreq);
                policyAdministrationCharge_Q = Double.parseDouble(BIMAST
                        .getPolicyAdministrationCharge_Q());
                _policyAdministrationCharge_Q = policyAdministrationCharge_Q;
                // System.out.println("13.   PolicyAdministrationCharge_Q : "+policyAdministrationCharge_Q);

                sum_Q += _policyAdministrationCharge_Q;
                sum_02 = sum_Q;
                if ((_month_E % 12) == 0) {
                    bussIll.append("<PolicyAdmCharge"
                            + _year_F
                            + ">"
                            + commonForAllProd.getRoundUp(commonForAllProd
                            .getStringWithout_E(sum_Q))
                            + "</PolicyAdmCharge" + _year_F + ">");
                    sum_Q = 0;
                }

                BIMAST.setOneHundredPercentOfCummulativePremium_AW(_oneHundredPercentOfCummulativePremium_AW);
                oneHundredPercentOfCummulativePremium_AW = Double
                        .parseDouble(BIMAST
                                .getOneHundredPercentOfCummulativePremium_AW());
                _oneHundredPercentOfCummulativePremium_AW = oneHundredPercentOfCummulativePremium_AW;
                // System.out.println("45.   OneHundredPercentOfCummulativePremium_AW : "+oneHundredPercentOfCummulativePremium_AW);

                BIMAST.setMortalityCharges_R(_fundValueAtEnd_AB, policyTerm,
                        forBIArray, ageAtEntry, sumAssured, prop.mortalityCharges, smartEliteBean1.getPlanOption());
                mortalityCharges_R = Double.parseDouble(BIMAST
                        .getMortalityCharges_R());
                _mortalityCharges_R = mortalityCharges_R;
                // System.out.println("14.   MortalityCharges_R : "+mortalityCharges_R);

                sum_R += _mortalityCharges_R;
                if ((_month_E % 12) == 0) {
                    bussIll.append("<MortChrg4Pr"
                            + _year_F
                            + ">"
//						+ commonForAllProd.getRoundUp(commonForAllProd
                            + commonForAllProd.getRound(commonForAllProd
                            .getStringWithout_E(sum_R)) + "</MortChrg4Pr"
                            + _year_F + ">");
                    sum_R = 0;
                }

                BIMAST.setTotalCharges_S(policyTerm);
                totalCharges_S = Double.parseDouble(BIMAST.getTotalCharges_S());
                _totalCharges_S = totalCharges_S;
                // System.out.println("15.   TotalCharges_S : "+totalCharges_S);

                sum_S += _totalCharges_S;
                if ((_month_E % 12) == 0) {
                    bussIll.append("<TotalCharges4Pr"
                            + _year_F
                            + ">"
                            + commonForAllProd.getRoundUp(commonForAllProd
                            .getStringWithout_E(sum_S))
                            + "</TotalCharges4Pr" + _year_F + ">");
                    sum_S = 0;
                }

                BIMAST.setTotalServiceTax_exclOfSTonAllocAndSurr_T(serviceTax,
                        prop.mortalityAndRiderCharges,
                        prop.administrationAndSArelatedCharges);
                totalServiceTax_ExclOfSTonAllocAndSurr_T = Double
                        .parseDouble(BIMAST
                                .getTotalServiceTax_ExclOfSTonAllocAndSurr_T());
                _totalServiceTax_ExclOfSTonAllocAndSurr_T = totalServiceTax_ExclOfSTonAllocAndSurr_T;
                // System.out.println("16.   TotalServiceTax_ExclOfSTonAllocAndSurr_T : "+totalServiceTax_ExclOfSTonAllocAndSurr_T);

                BIMAST.setAdditionToFundIfAny_V(_fundValueAtEnd_AB, policyTerm);
                additionToFundIfAny_V = Double.parseDouble(BIMAST
                        .getAdditionToFundIfAny_V());
                _additionToFundIfAny_V = additionToFundIfAny_V;
                // System.out.println("18.   AdditionToFundIfAny_V : "+additionToFundIfAny_V);

                sum_V += _additionToFundIfAny_V;
                if ((_month_E % 12) == 0) {
                    bussIll.append("<AddToFundIfAny4Pr"
                            + _year_F
                            + ">"
                            + commonForAllProd.getRoundUp(commonForAllProd
                            .getStringWithout_E(sum_V))
                            + "</AddToFundIfAny4Pr" + _year_F + ">");
                    sum_V = 0;
                }

                BIMAST.setFundBeforeFMC_W(_fundValueAtEnd_AB, policyTerm);
                fundBeforeFMC_W = Double.parseDouble(BIMAST.getFundBeforeFMC_W());
                _fundBeforeFMC_W = fundBeforeFMC_W;
                // System.out.println("19.   FundBeforeFMC_W : "+fundBeforeFMC_W);

                BIMAST.setFundManagementCharge_X(policyTerm,
                        percentToBeInvested_EquityEliteIIFund,
                        percentToBeInvested_BalancedFund,
                        percentToBeInvested_BondFund,
                        percentToBeInvested_MoneyMarketFund,
                        percentToBeInvested_BondOptimiserFund,
                        percentToBeInvested_EquityFund,
                        percentToBeInvested_MidcapFund,
                        percentToBeInvested_PureFund);
                fundManagementCharge_X = Double.parseDouble(BIMAST
                        .getFundManagementCharge_X());
                _fundManagementCharge_X = fundManagementCharge_X;
                // System.out.println("20.   FundManagementCharge_X : "+fundManagementCharge_X);

                sum_X += _fundManagementCharge_X;

                sum_03 = sum_X;
                if ((_month_E % 12) == 0) {
                    bussIll.append("<FundMgmtChrg4Pr"
                            + _year_F
                            + ">"
                            + commonForAllProd.getRound(commonForAllProd
                            .getStringWithout_E(sum_X))
                            + "</FundMgmtChrg4Pr" + _year_F + ">");
                    sum_X = 0;
                }

                otherCharges_PartA = sum_01 + sum_02 + sum_03 + _riderCharges_P;

                if ((_month_E % 12) == 0) {

                    bussIll.append("<OtherCharges4Pr_PartA" + _year_F + ">"
                            + commonForAllProd.getRound(String
                            .valueOf(otherCharges_PartA)) + "</OtherCharges4Pr_PartA" + _year_F
                            + ">");
                    otherCharges_PartA = 0;
//				_riderCharges_P =0;
                }

                if ((_month_E % 12) == 0) {

                    bussIll.append("<OtherCharges4Pr_PartB" + _year_F + ">"
                            + commonForAllProd.getRound(String
                            .valueOf(otherCharges_PartB)) + "</OtherCharges4Pr_PartB" + _year_F
                            + ">");
                    otherCharges_PartB = 0;
                }

                BIMAST.setServiceTaxOnFMC_Y(prop.fundManagementCharges,
                        serviceTax, prop.indexFund,
                        percentToBeInvested_EquityEliteIIFund,
                        percentToBeInvested_BalancedFund,
                        percentToBeInvested_BondFund,
                        percentToBeInvested_MoneyMarketFund,
                        percentToBeInvested_BondOptimiserFund,
                        percentToBeInvested_EquityFund,
                        percentToBeInvested_MidcapFund,
                        percentToBeInvested_PureFund);
                serviceTaxOnFMC_Y = Double.parseDouble(BIMAST
                        .getServiceTaxOnFMC_Y());
                _serviceTaxOnFMC_Y = serviceTaxOnFMC_Y;
                // System.out.println("21.   ServiceTaxOnFMC_Y : "+serviceTaxOnFMC_Y);

                BIMAST.setTotalServiceTax_U(serviceTax);
                totalServiceTax_U = Double.parseDouble(BIMAST
                        .getTotalServiceTax_U());
                _totalServiceTax_U = totalServiceTax_U;
                // System.out.println("17.   TotalServiceTax_U : "+totalServiceTax_U);

                sum_U += _totalServiceTax_U;
                if ((_month_E % 12) == 0) {
                    bussIll.append("<TotalSerTax4Pr"
                            + _year_F
                            + ">"
//						+ commonForAllProd.getRoundUp(commonForAllProd
                            + commonForAllProd.getRound(commonForAllProd
                            .getStringWithout_E(sum_U))
                            + "</TotalSerTax4Pr" + _year_F + ">");
                    sum_U = 0;
                }

                BIMAST.setGuaranteedAddition_AA();
                guaranteedAddition_AA = Double.parseDouble(BIMAST
                        .getGuaranteedAddition_AA());
                _guaranteedAddition_AA = guaranteedAddition_AA;
                // System.out.println("23.   GuaranteedAddition_AA : "+guaranteedAddition_AA);

                sum_AA += _guaranteedAddition_AA;
                if ((_month_E % 12) == 0) {
                    bussIll.append("<GuareentedAdd4Pr"
                            + _year_F
                            + ">"
                            + commonForAllProd.getStringWithout_E(Math
                            .round(sum_AA)) + "</GuareentedAdd4Pr"
                            + _year_F + ">");
                    sum_AA = 0;
                }

                BIMAST.setFundValueAfterFMCandBeforeGA_Z(policyTerm);
                fundValueAfterFMCandBeforeGA_Z = Double.parseDouble(BIMAST
                        .getFundValueAfterFMCandBeforeGA_Z());
                _fundValueAfterFMCandBeforeGA_Z = fundValueAfterFMCandBeforeGA_Z;
                // System.out.println("22.   FundValueAfterFMCandBeforeGA_Z : "+fundValueAfterFMCandBeforeGA_Z);

                if ((_month_E % 12) == 0) {
                    sum_Z = _fundValueAfterFMCandBeforeGA_Z;
                }

                BIMAST.setFundValueAtEnd_AB();
                fundValueAtEnd_AB = Double.parseDouble(BIMAST
                        .getFundValueAtEnd_AB());
                _fundValueAtEnd_AB = fundValueAtEnd_AB;
                // System.out.println("24.   FundValueAtEnd_AB : "+fundValueAtEnd_AB);

                // sum_AB += _fundValueAtEnd_AB;
                if ((_month_E % 12) == 0) {
                    sum_AB = (sum_AA + sum_Z);
                    bussIll.append("<FundValAtEnd4Pr"
                            + _year_F
                            + ">"
                            + commonForAllProd.getRoundUp(commonForAllProd
                            .getStringWithout_E(sum_AB))
                            + "</FundValAtEnd4Pr" + _year_F + ">");
                    // sum_AB=0;
                }

                if ((_month_E % 12) == 0) {

                    bussIll.append("<FundBefFMC4Pr"
                            + _year_F
                            + ">"
                            + Math.round(Double.parseDouble(commonForAllProd
                            .getRound(commonForAllProd.getStringWithout_E(fundValueAtEnd_AB)))
                            + sum_03) + "</FundBefFMC4Pr" + _year_F
                            + ">");
                }

                BIMAST.setSurrenderCap_AV(effectivePremium, premFreq);
                surrenderCap_AV = Double.parseDouble(BIMAST.getSurrenderCap_AV());
                _surrenderCap_AV = surrenderCap_AV;
                // System.out.println("44.   SurrenderCap_AV : "+surrenderCap_AV);

                BIMAST.setSurrenderCharges_AC(effectivePremium, premFreq);
                surrenderCharges_AC = Double.parseDouble(BIMAST
                        .getSurrenderCharges_AC());
                _surrenderCharges_AC = surrenderCharges_AC;
                // System.out.println("25.   SurrenderCharges_AC : "+surrenderCharges_AC);

                BIMAST.setServiceTaxOnSurrenderCharges_AD(prop.surrenderCharges,
                        serviceTax);
                serviceTaxOnSurrenderCharges_AD = Double.parseDouble(BIMAST
                        .getServiceTaxOnSurrenderCharges_AD());
                _serviceTaxOnSurrenderCharges_AD = serviceTaxOnSurrenderCharges_AD;
                // System.out.println("26.   ServiceTaxOnSurrenderCharges_AD : "+serviceTaxOnSurrenderCharges_AD);

                BIMAST.setSurrenderValue_AE();
                surrenderValue_AE = Double.parseDouble(BIMAST
                        .getSurrenderValue_AE());
                _surrenderValue_AE = surrenderValue_AE;
                // System.out.println("27.   SurrenderValue_AE: "+surrenderValue_AE);

                // sum_AE += _surrenderValue_AE;
                if ((_month_E % 12) == 0) {
                    sum_AE = _surrenderValue_AE;
                    bussIll.append("<SurrenderVal4Pr"
                            + _year_F
                            + ">"
                            + commonForAllProd.getRoundUp(commonForAllProd
                            .getStringWithout_E(sum_AE))
                            + "</SurrenderVal4Pr" + _year_F + ">");
                    // sum_AE=0;
                }

//			BIMAST.setDeathBenefit_AF(policyTerm, sumAssured, planOption);
                BIMAST.setDeathBenefit_AF(policyTerm, sumAssured, smartEliteBean1.getPlanOption());
                deathBenefit_AF = Double.parseDouble(BIMAST.getDeathBenefit_AF());
                _deathBenefit_AF = deathBenefit_AF;
                // System.out.println("28.   DeathBenefit_AF: "+deathBenefit_AF);

                // sum_AF += _deathBenefit_AF;
            /*if ((_month_E % 12) == 0) {
                if (smartEliteBean.getPlanOption().equals("Gold")) {
					sum_AF = (Math.max(sumAssured, sum_AB));
				} else {
					sum_AF = (sumAssured + sum_AB);
				}
				bussIll.append("<DeathBenefit4Pr"
						+ _year_F
						+ ">"
						+ commonForAllProd.getRoundUp(commonForAllProd
						.getStringWithout_E(sum_AF))
						+ "</DeathBenefit4Pr" + _year_F + ">");
				// sum_AF=0;
			}*/

                if ((_month_E % 12) == 0) {

                    bussIll.append("<DeathBen4Pr" + _year_F + ">" + commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(_deathBenefit_AF)) + "</DeathBen4Pr" + _year_F + ">");

                }

                BIMAST.setMortalityCharges_AG(_fundValueAtEnd_AQ, policyTerm,
                        forBIArray, ageAtEntry, sumAssured, prop.mortalityCharges, smartEliteBean1.getPlanOption());
                mortalityCharges_AG = Double.parseDouble(BIMAST
                        .getMortalityCharges_AG());
                _mortalityCharges_AG = mortalityCharges_AG;
                // System.out.println("29.   MortalityCharges_AG : "+mortalityCharges_AG);

                sum_AG += _mortalityCharges_AG;
                if ((_month_E % 12) == 0) {
                    bussIll.append("<MortChrg8Pr"
                            + _year_F
                            + ">"
                            + commonForAllProd.getRoundUp(commonForAllProd
                            .getStringWithout_E(sum_AG)) + "</MortChrg8Pr"
                            + _year_F + ">");
                    sum_AG = 0;
                }

                BIMAST.setTotalCharges_AH(policyTerm);
                totalCharges_AH = Double.parseDouble(BIMAST.getTotalCharges_AH());
                _totalCharges_AH = totalCharges_AH;
                // System.out.println("30.   TotalCharges_AH: "+totalCharges_AH);

                sum_AH += _totalCharges_AH;
                if ((_month_E % 12) == 0) {
                    bussIll.append("<TotalChrg8Pr"
                            + _year_F
                            + ">"
                            + commonForAllProd.getRoundUp(commonForAllProd
                            .getStringWithout_E(sum_AH)) + "</TotalChrg8Pr"
                            + _year_F + ">");
                    sum_AH = 0;
                }

                BIMAST.setTotalServiceTax_ExclOfSTonAllocAndSurr_AI(
                        serviceTax, prop.mortalityAndRiderCharges,
                        prop.administrationAndSArelatedCharges);
                totalServiceTax_ExclOfSTonAllocAndsurr_AI = Double
                        .parseDouble(BIMAST
                                .getTotalServiceTax_ExclOfSTonAllocAndSurr_AI());
                _totalServiceTax_ExclOfSTonAllocAndsurr_AI = totalServiceTax_ExclOfSTonAllocAndsurr_AI;
                // System.out.println("31.   TotalServiceTax_ExclOfSTonAllocAndsurr_AI: "+totalServiceTax_ExclOfSTonAllocAndsurr_AI);

                BIMAST.setAdditionToFundIfAny_AK(_fundValueAtEnd_AQ, policyTerm);
                additionToFundIfAny_AK = Double.parseDouble(BIMAST
                        .getAdditionToFundIfAny_AK());
                _additionToFundIfAny_AK = additionToFundIfAny_AK;
                // System.out.println("33.   AdditionToFundIfAny_AK: "+additionToFundIfAny_AK);

                sum_AK += _additionToFundIfAny_AK;
                if ((_month_E % 12) == 0) {
                    bussIll.append("<AddToFundIfAny8Pr"
                            + _year_F
                            + ">"
                            + commonForAllProd.getRoundUp(commonForAllProd
                            .getStringWithout_E(sum_AK))
                            + "</AddToFundIfAny8Pr" + _year_F + ">");
                    sum_AK = 0;
                }

                BIMAST.setFundBeforeFMC_AL(_fundValueAtEnd_AQ, policyTerm);
                fundBeforeFMC_AL = Double.parseDouble(BIMAST.getFundBeforeFMC_AL());
                _fundBeforeFMC_AL = fundBeforeFMC_AL;
                // System.out.println("34.   FundBeforeFMC_AL: "+fundBeforeFMC_AL);

                BIMAST.setFundManagementCharge_AM(policyTerm,
                        percentToBeInvested_EquityEliteIIFund,
                        percentToBeInvested_BalancedFund,
                        percentToBeInvested_BondFund,
                        percentToBeInvested_MoneyMarketFund,
                        percentToBeInvested_BondOptimiserFund,
                        percentToBeInvested_EquityFund,
                        percentToBeInvested_MidcapFund,
                        percentToBeInvested_PureFund);
                fundManagementCharge_AM = Double.parseDouble(BIMAST
                        .getFundManagementCharge_AM());
                _fundManagementCharge_AM = fundManagementCharge_AM;
                // System.out.println("35.   FundManagementCharge_AM: "+fundManagementCharge_AM);

                sum_AM += _fundManagementCharge_AM;
                sum1_03 = sum_AM;
                if ((_month_E % 12) == 0) {
                    bussIll.append("<FundMgmtChrg8Pr"
                            + _year_F
                            + ">"
                            + commonForAllProd.getRoundUp(commonForAllProd
                            .getStringWithout_E(sum_AM))
                            + "</FundMgmtChrg8Pr" + _year_F + ">");
                    sum_AM = 0;
                }

                otherCharges1_PartA = sum_01 + sum_02 + sum1_03 + _riderCharges_P;

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
                    _riderCharges_P = 0;
                }

                BIMAST.setGuaranteedAddition_AP();
                guaranteedAddition_AP = Double.parseDouble(BIMAST
                        .getGuaranteedAddition_AP());
                _guaranteedAddition_AP = guaranteedAddition_AP;
                // System.out.println("38.   GuaranteedAddition_AP : "+guaranteedAddition_AP);

                sum_AP += _guaranteedAddition_AP;
                if ((_month_E % 12) == 0) {
                    bussIll.append("<GuaranteedAdd8Pr"
                            + _year_F
                            + ">"
                            + commonForAllProd.getStringWithout_E(Math
                            .round(sum_AP)) + "</GuaranteedAdd8Pr"
                            + _year_F + ">");
                    sum_AP = 0;
                }

                BIMAST.setServiceTaxOnFMC_AN(prop.fundManagementCharges,
                        serviceTax, prop.indexFund,
                        percentToBeInvested_EquityEliteIIFund,
                        percentToBeInvested_BalancedFund,
                        percentToBeInvested_BondFund,
                        percentToBeInvested_MoneyMarketFund,
                        percentToBeInvested_BondOptimiserFund,
                        percentToBeInvested_EquityFund,
                        percentToBeInvested_MidcapFund,
                        percentToBeInvested_PureFund);
                serviceTaxOnFMC_AN = Double.parseDouble(BIMAST
                        .getServiceTaxOnFMC_AN());
                _serviceTaxOnFMC_AN = serviceTaxOnFMC_AN;
                // System.out.println("36.   ServiceTaxOnFMC_AN: "+serviceTaxOnFMC_AN);

                BIMAST.setFundValueAfterFMCandBeforeGA_AO(policyTerm);
                fundValueAfterFMCAndBeforeGA_AO = Double.parseDouble(BIMAST
                        .getFundValueAfterFMCandBeforeGA_AO());
                _fundValueAfterFMCAndBeforeGA_AO = fundValueAfterFMCAndBeforeGA_AO;
                // System.out.println("37.   FundValueAfterFMCAndBeforeGA_AO: "+fundValueAfterFMCAndBeforeGA_AO);

                if ((_month_E % 12) == 0) {
                    sum_AO = _fundValueAfterFMCAndBeforeGA_AO;
                }

                BIMAST.setFundValueAtEnd_AQ();
                fundValueAtEnd_AQ = Double.parseDouble(BIMAST
                        .getFundValueAtEnd_AQ());
                _fundValueAtEnd_AQ = fundValueAtEnd_AQ;
                // System.out.println("39.   FundValueAtEnd_AQ : "+fundValueAtEnd_AQ);

                // sum_AQ += _fundValueAtEnd_AQ;
                if ((_month_E % 12) == 0) {
                    sum_AQ = (sum_AO + sum_AP);
                    bussIll.append("<FundValAtEnd8Pr"
                            + _year_F
                            + ">"
                            + commonForAllProd.getRoundUp(commonForAllProd
                            .getStringWithout_E(sum_AQ))
                            + "</FundValAtEnd8Pr" + _year_F + ">");
                    // sum_AB=0;
                }

                if ((_month_E % 12) == 0) {

                    bussIll.append("<FundBefFMC8Pr"
                            + _year_F
                            + ">"
                            + Math.round(Double.parseDouble(commonForAllProd
                            .getRound(commonForAllProd.getStringWithout_E(fundValueAtEnd_AQ)))
                            + sum1_03) + "</FundBefFMC8Pr" + _year_F
                            + ">");
                }
                ;

                BIMAST.setDeathBenefit_AU(policyTerm, sumAssured, smartEliteBean1.getPlanOption());
                deathBenefit_AU = Double.parseDouble(BIMAST.getDeathBenefit_AU());
                _deathBenefit_AU = deathBenefit_AU;
                // System.out.println("43.   DeathBenefit_AU: "+deathBenefit_AU);

                // sum_AU += _deathBenefit_AU;
            /*if ((_month_E % 12) == 0) {
                if (smartEliteBean.getPlanOption().equals("Gold")) {
					sum_AU = (Math.max(sumAssured, sum_AQ));
				} else {
					sum_AU = (sumAssured + sum_AQ);
				}
				bussIll.append("<DeathBenefit8Pr"
						+ _year_F
						+ ">"
						+ commonForAllProd.getRoundUp(commonForAllProd
						.getStringWithout_E(sum_AU))
						+ "</DeathBenefit8Pr" + _year_F + ">");

			}*/

                if ((_month_E % 12) == 0) {

                    bussIll.append("<DeathBen8Pr" + _year_F + ">" + commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(_deathBenefit_AU)) + "</DeathBen8Pr" + _year_F + ">");

                }

                BIMAST.setTotalServiceTax_AJ(serviceTax);
                totalServiceTax_AJ = Double.parseDouble(BIMAST
                        .getTotalServiceTax_AJ());
                _totalServiceTax_AJ = totalServiceTax_AJ;
                // System.out.println("32.   TotalServiceTax_AJ: "+totalServiceTax_AJ);

                sum_AJ += _totalServiceTax_AJ;
                if ((_month_E % 12) == 0) {
                    bussIll.append("<TotalSerTax8Pr"
                            + _year_F
                            + ">"
                            + commonForAllProd.getRoundUp(commonForAllProd
                            .getStringWithout_E(sum_AJ))
                            + "</TotalSerTax8Pr" + _year_F + ">");
                    sum_AJ = 0;
                }

                BIMAST.setSurrenderCharges_AR(effectivePremium, premFreq);
                surrenderCharges_AR = Double.parseDouble(BIMAST
                        .getSurrenderCharges_AR());
                _surrenderCharges_AR = surrenderCharges_AR;
                // System.out.println("40.   SurrenderCharges_AR: "+surrenderCharges_AR);

                BIMAST.setServiceTaxOnSurrenderCharges_AS(prop.surrenderCharges,
                        serviceTax);
                serviceTaxOnSurrenderCharges_AS = Double.parseDouble(BIMAST
                        .getServiceTaxOnSurrenderCharges_AS());
                _serviceTaxOnSurrenderCharges_AS = serviceTaxOnSurrenderCharges_AS;
                // System.out.println("41.   ServiceTaxOnSurrenderCharges_AS: "+serviceTaxOnSurrenderCharges_AS);

                BIMAST.setSurrenderValue_AT();
                surrenderValue_AT = Double.parseDouble(BIMAST
                        .getSurrenderValue_AT());
                _surrenderValue_AT = surrenderValue_AT;
                // System.out.println("42.   SurrenderValue_AT: "+surrenderValue_AT);

                // sum_AT += _surrenderValue_AT;

                if ((_month_E % 12) == 0) {
                    sum_AT = _surrenderValue_AT;
                    bussIll.append("<SurrenderVal8Pr"
                            + _year_F
                            + ">"
                            + commonForAllProd.getRoundUp(commonForAllProd
                            .getStringWithout_E(sum_AT))
                            + "</SurrenderVal8Pr" + _year_F + ">");

                }

                // System.out.println("***********************************************************************************************************");

                if ((_month_E % 12) == 0) {
                    // System.out.println("aetfef"+BIMAST.getCommission_AQ(_sum_I,_sum_J,smartEliteBean1));
                    Commission_AQ = BIMAST.getCommission_AQ(_sum_I, _sum_J,
                            smartEliteBean);
                    bussIll.append("<CommIfPay8Pr"
                            + _year_F
                            + ">"
                            + commonForAllProd.getRoundUp(commonForAllProd
                            .getStringWithout_E(Commission_AQ))
                            + "</CommIfPay8Pr" + _year_F + ">");
                }

            }
//			if((((policyTerm*12)-11)==i))
//			{
//				arrGuaranteedAddition_AA[0]=""+(_guaranteedAddition_AA);
//				arrGuaranteedAddition_AP[0]=""+(_guaranteedAddition_AP);
//			}
//			else if((((policyTerm*12)-10)==i))
//			{
//				arrGuaranteedAddition_AA[1]=""+(_guaranteedAddition_AA);
//				arrGuaranteedAddition_AP[1]=""+(_guaranteedAddition_AP);
//			}
//			else if((((policyTerm*12)-9)==i))
//			{
//				arrGuaranteedAddition_AA[2]=""+(_guaranteedAddition_AA);
//				arrGuaranteedAddition_AP[2]=""+(_guaranteedAddition_AP);
//			}
//			else if((((policyTerm*12)-8)==i))
//			{
//				arrGuaranteedAddition_AA[3]=""+(_guaranteedAddition_AA);
//				arrGuaranteedAddition_AP[3]=""+(_guaranteedAddition_AP);
//			}
//			else if((((policyTerm*12)-7)==i))
//			{
//				arrGuaranteedAddition_AA[4]=""+(_guaranteedAddition_AA);
//				arrGuaranteedAddition_AP[4]=""+(_guaranteedAddition_AP);
//			}
//			else if((((policyTerm*12)-6)==i))
//			{
//				arrGuaranteedAddition_AA[5]=""+(_guaranteedAddition_AA);
//				arrGuaranteedAddition_AP[5]=""+(_guaranteedAddition_AP);
//			}
//			else if((((policyTerm*12)-5)==i))
//			{
//				arrGuaranteedAddition_AA[6]=""+(_guaranteedAddition_AA);
//				arrGuaranteedAddition_AP[6]=""+(_guaranteedAddition_AP);
//			}
//			else if((((policyTerm*12)-4)==i))
//			{
//				arrGuaranteedAddition_AA[7]=""+(_guaranteedAddition_AA);
//				arrGuaranteedAddition_AP[7]=""+(_guaranteedAddition_AP);
//			}
//			else if((((policyTerm*12)-3)==i))
//			{
//				arrGuaranteedAddition_AA[8]=""+(_guaranteedAddition_AA);
//				arrGuaranteedAddition_AP[8]=""+(_guaranteedAddition_AP);
//			}
//			else if((((policyTerm*12)-2)==i))
//			{
//				arrGuaranteedAddition_AA[9]=""+(_guaranteedAddition_AA);
//				arrGuaranteedAddition_AP[9]=""+(_guaranteedAddition_AP);
//			}
//			else if((((policyTerm*12)-1)==i))
//			{
//				arrGuaranteedAddition_AA[10]=""+(_guaranteedAddition_AA);
//				arrGuaranteedAddition_AP[10]=""+(_guaranteedAddition_AP);
//			}
//			else if((((policyTerm*12))==i))
//			{
//				arrGuaranteedAddition_AA[11]=""+(_guaranteedAddition_AA);
//				arrGuaranteedAddition_AP[11]=""+(_guaranteedAddition_AP);
//			}
//		}
//
//		String U=commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(_fundValueAfterFMCandBeforeGA_Z));
//		String V=commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(Double.parseDouble(arrGuaranteedAddition_AA[0])+Double.parseDouble(arrGuaranteedAddition_AA[1])+Double.parseDouble(arrGuaranteedAddition_AA[2])+Double.parseDouble(arrGuaranteedAddition_AA[3])+Double.parseDouble(arrGuaranteedAddition_AA[4])+Double.parseDouble(arrGuaranteedAddition_AA[5])+Double.parseDouble(arrGuaranteedAddition_AA[6])+Double.parseDouble(arrGuaranteedAddition_AA[7])+Double.parseDouble(arrGuaranteedAddition_AA[8])+Double.parseDouble(arrGuaranteedAddition_AA[9])+Double.parseDouble(arrGuaranteedAddition_AA[10])+Double.parseDouble(arrGuaranteedAddition_AA[11])));
//		//System.out.println("U -> "+U);
//		//System.out.println("V -> "+V);
//		String AJ=commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(_fundValueAfterFMCAndBeforeGA_AO));
//		String AK=commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(Double.parseDouble(arrGuaranteedAddition_AP[0])+Double.parseDouble(arrGuaranteedAddition_AP[1])+Double.parseDouble(arrGuaranteedAddition_AP[2])+Double.parseDouble(arrGuaranteedAddition_AP[3])+Double.parseDouble(arrGuaranteedAddition_AP[4])+Double.parseDouble(arrGuaranteedAddition_AP[5])+Double.parseDouble(arrGuaranteedAddition_AP[6])+Double.parseDouble(arrGuaranteedAddition_AP[7])+Double.parseDouble(arrGuaranteedAddition_AP[8])+Double.parseDouble(arrGuaranteedAddition_AP[9])+Double.parseDouble(arrGuaranteedAddition_AP[10])+Double.parseDouble(arrGuaranteedAddition_AP[11])));
//		//System.out.println("AJ -> "+AJ);
//		//System.out.println("AK -> "+AK);
//		//System.out.println("****************************** Final Output ****************************************");
//		//System.out.println("Sum Assured -> "+(sumAssured));
//		//System.out.println("Fund Value @6% [W] -> "+(Double.parseDouble(U)+Double.parseDouble(V)));
//		//System.out.println("Fund Value @10% [AL] -> "+(Double.parseDouble(AJ)+Double.parseDouble(AK)));
//		//System.out.println("***********************************************************************************");
//		return new String[]{(commonForAllProd.getStringWithout_E(sumAssured)),(commonForAllProd.getStringWithout_E(Double.parseDouble(U)+Double.parseDouble(V))),(commonForAllProd.getStringWithout_E(Double.parseDouble(AJ)+Double.parseDouble(AK))),(String.valueOf(smartEliteBean.getEffectivePremium()))};
            return new String[]{(commonForAllProd.getStringWithout_E(sumAssured)), commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(fundValueAtEnd_AB)), commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(fundValueAtEnd_AQ)), (String.valueOf(smartEliteBean.getEffectivePremium()))};


        } catch (Exception e) {
            e.printStackTrace();
            // System.out.println("** error **" + e.getMessage());
        }
        return null;
    }

    private String[] getOutputRedutionInYield(String sheetName,
                                              BI_SmartEliteBean smartEliteBean1) {

        String[] arrGuaranteedAddition_AA = new String[12];
        String[] arrGuaranteedAddition_AP = new String[12];
        ArrayList<String> List_BD = new ArrayList<String>();
        ArrayList<String> List_BI = new ArrayList<String>();
        ArrayList<String> List_BC = new ArrayList<String>();

        // Output Variable Declaration
        double _oneHundredPercentOfCummulativePremium_AW = 0;
        double _policyAdministrationCharge_Q = 0;
        double _guaranteedAddition_AA = 0;
        double _guaranteedAddition_AP = 0;
        double _fundValueAfterFMCandBeforeGA_Z = 0;
        double _fundValueAtEnd_AB = 0;
        double _fundValueAtEnd_AQ = 0;
        double _fundValueAfterFMCAndBeforeGA_AO = 0;
        int _month_E = 0;
        int _year_F = 0;
        String _policyInForce_G = "Y";
        int _age_H = 0;
        int _month_BG = 0;
        double _premium_I = 0;
        double _topUpPremium_J = 0;
        double _premiumAllocationCharge_K = 0;
        double _topUpCharges_L = 0;
        double _serviceTaxOnAllocation_M = 0;
        double _amountAvailableForInvestment_N = 0;
        double _riderCharges_P = 0;
        double _sumAssuredRelatedCharges_O = 0;
        double _mortalityCharges_R = 0;
        double _totalCharges_S = 0;
        double _totalServiceTax_ExclOfSTonAllocAndSurr_T = 0;
        double _additionToFundIfAny_V = 0;
        double _fundBeforeFMC_W = 0;
        double _fundManagementCharge_X = 0;
        double _serviceTaxOnFMC_Y = 0;
        double _totalServiceTax_U = 0;
        double _surrenderCap_AV = 0;
        double _surrenderCharges_AC = 0;
        double _serviceTaxOnSurrenderCharges_AD = 0;
        double _surrenderValue_AE = 0;
        double _deathBenefit_AF = 0;
        double _mortalityCharges_AG = 0;
        double _totalCharges_AH = 0;
        double _totalServiceTax_ExclOfSTonAllocAndsurr_AI = 0;
        double _additionToFundIfAny_AK = 0;
        double _fundBeforeFMC_AL = 0;
        double _fundManagementCharge_AM = 0;
        double _serviceTaxOnFMC_AN = 0;
        double _deathBenefit_AU = 0;
        double _totalServiceTax_AJ = 0;
        double _surrenderCharges_AR = 0;
        double _serviceTaxOnSurrenderCharges_AS = 0;
        double _surrenderValue_AT = 0;
        double _reductionYield_BI = 0;
        double _reductionYield_BD = 0;
        double _irrAnnual_BD = 0;
        double _irrAnnual_BI = 0;
        double _reductionInYieldMaturityAt = 0;
        double _reductionInYieldNumberOfYearsElapsedSinceInception = 0;
        double _reductionYield_BC = 0;
        double _irrAnnual_BC = 0;
        double _netYield4Pr = 0;

        // //Temp Variable Declaretion
        int month_E = 0;
        int year_F = 0;
        String policyInForce_G = "Y";
        int age_H = 0;
        int month_BG = 0;
        double premium_I = 0;
        double topUpPremium_J = 0;
        double premiumAllocationCharge_K = 0;
        double topUpCharges_L = 0;
        double serviceTaxOnAllocation_M = 0;
        double amountAvailableForInvestment_N = 0;
        double riderCharges_P = 0;
        double sumAssuredRelatedCharges_O = 0;
        double policyAdministrationCharge_Q = 0;
        double oneHundredPercentOfCummulativePremium_AW = 0;
        double guaranteedAddition_AA = 0;
        double guaranteedAddition_AP = 0;
        double mortalityCharges_R = 0;
        double totalCharges_S = 0;
        double totalServiceTax_ExclOfSTonAllocAndSurr_T = 0;
        double additionToFundIfAny_V = 0;
        double fundBeforeFMC_W = 0;
        double fundManagementCharge_X = 0;
        double serviceTaxOnFMC_Y = 0;
        double totalServiceTax_U = 0;
        double fundValueAfterFMCandBeforeGA_Z = 0;
        double fundValueAtEnd_AB = 0;
        double fundValueAtEnd_AQ = 0;
        double surrenderCap_AV = 0;
        double surrenderCharges_AC = 0;
        double serviceTaxOnSurrenderCharges_AD = 0;
        double surrenderValue_AE = 0;
        double deathBenefit_AF = 0;
        double mortalityCharges_AG = 0;
        double totalCharges_AH = 0;
        double totalServiceTax_ExclOfSTonAllocAndsurr_AI = 0;
        double additionToFundIfAny_AK = 0;
        double fundBeforeFMC_AL = 0;
        double fundManagementCharge_AM = 0;
        double serviceTaxOnFMC_AN = 0;
        double fundValueAfterFMCAndBeforeGA_AO = 0;
        double deathBenefit_AU = 0;
        double totalServiceTax_AJ = 0;
        double surrenderCharges_AR = 0;
        double serviceTaxOnSurrenderCharges_AS = 0;
        double surrenderValue_AT = 0;
        double reductionYield_BI = 0;
        double reductionYield_BD = 0;
        double irrAnnual_BD = 0;
        double irrAnnual_BI = 0;
        double reductionInYieldMaturityAt = 0;
        double reductionInYieldNumberOfYearsElapsedSinceInception = 0;
        double reductionYield_BC = 0;
        double irrAnnual_BC = 0;
        double netYield4Pr = 0;
        double netYield8Pr = 0;

        // From GUI Input
        boolean staffDisc = smartEliteBean1.getIsForStaffOrNot();
        //double serviceTax=smartEliteBean1.getServiceTax();
        double serviceTax = 0;
        boolean isKerlaDisc = smartEliteBean.isKerlaDisc();
        boolean bancAssuranceDisc = smartEliteBean1
                .getIsBancAssuranceDiscOrNot();
        String premFreq = smartEliteBean1.getPremFreq();
        double SAMF = smartEliteBean1.getSAMF();
        int ageAtEntry = smartEliteBean1.getAgeAtEntry();
        //String planOption = smartEliteBean1.getPlanOption();
        String addTopUp = "No";

        // Internally Calculated Input Fields
        double effectivePremium = smartEliteBean1.getEffectivePremium();
        int PF = smartEliteBean1.getPF();
        int premiumPayingTerm = smartEliteBean1.getPremiumPayingTerm();
        int noOfYearsElapsedSinceInception = smartEliteBean1
                .getYearsElapsedSinceInception();
        // System.out.println("noOfYearsElapsedSinceInception "+noOfYearsElapsedSinceInception);

        int policyTerm = smartEliteBean1.getPolicyTerm_Basic();
        double sumAssured = (effectivePremium * SAMF);
        smartEliteBean1.setEffectiveTopUpPrem(addTopUp, premFreq,
                prop.topUpPremiumAmt);
        double effectiveTopUpPrem = smartEliteBean1.getEffectiveTopUpPrem();
        // Declaration of method Variables/Object required for calculation
        BI_SmartEliteBusinessLogic BIMAST = new BI_SmartEliteBusinessLogic();
        String[] forBIArray = BIMAST
                .getForBIArr(prop.mortalityCharges_AsPercentOfofLICtable);
        int rowNumber = 0, monthNumber = 0;

        double percentToBeInvested_EquityEliteIIFund = smartEliteBean.getPercentToBeInvested_EquityEliteIIFund();
        double percentToBeInvested_BalancedFund = smartEliteBean.getPercentToBeInvested_BalancedFund();
        double percentToBeInvested_BondFund = smartEliteBean.getPercentToBeInvested_BondFund();
        double percentToBeInvested_MoneyMarketFund = smartEliteBean.getPercentToBeInvested_MoneyMarketFund();
        double percentToBeInvested_BondOptimiserFund = smartEliteBean.getPercent_BondOptimiserFund();
        double percentToBeInvested_EquityFund = smartEliteBean.getPercentToBeInvested_CorpBondFund();
        double percentToBeInvested_MidcapFund = smartEliteBean.getPercent_MidcapFund();
        double percentToBeInvested_PureFund = smartEliteBean.getPercent_PureFund();

        for (int i = 0; i <= (policyTerm * 12); i++)
        // for(int i=1;i<3;i++)
        {
            rowNumber++;
            // System.out.println("********************************************* "+i+" Row Output *********************************************");
            BIMAST.setMonth_E(rowNumber);
            month_E = Integer.parseInt(BIMAST.getMonth_E());
            _month_E = month_E;
            // System.out.println("1.   Month_E : "+month_E);

            BIMAST.setYear_F();
            year_F = Integer.parseInt(BIMAST.getYear_F());
            _year_F = year_F;
            // System.out.println("2.   Year_F : "+year_F);
           /* if (isKerlaDisc == true && _year_F <= 2) {
                serviceTax = 0.19;
            } else {
                serviceTax = 0.18;
            }*/
            try {
                String s3 = "30/6/2021";
                Date da1 = new SimpleDateFormat("dd/MM/yyyy").parse(s3);
                Date da = new SimpleDateFormat("dd/MM/yyyy").parse(commonMethods.getCurrentDateSlashFormat());
                int mo1 = obj.getMonthDiff(da1, da);
                if (isKerlaDisc == true && month_E <= (mo1 + 1)) {
                    serviceTax = 0.19;
                } else {
                    serviceTax = 0.18;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            policyInForce_G = BIMAST.getPolicyInForce_G();
            _policyInForce_G = BIMAST.getPolicyInForce_G();
            // System.out.println("3.   PolicyInForce_G : "+policyInForce_G);

            BIMAST.setAge_H(ageAtEntry);
            age_H = Integer.parseInt(BIMAST.getAge_H());
            _age_H = age_H;
            // System.out.println("4.   Age_H : "+age_H);

            BIMAST.setPremium_I(premiumPayingTerm, PF, effectivePremium);
            premium_I = Double.parseDouble(BIMAST.getPremium_I());
            _premium_I = premium_I;
            // System.out.println("5.   Premium_I : "+premium_I);

            BIMAST.setTopUpPremium_J(prop.topUpStatus, effectiveTopUpPrem,
                    addTopUp);
            topUpPremium_J = Double.parseDouble(BIMAST.getTopUpPremium_J());
            _topUpPremium_J = topUpPremium_J;
            // System.out.println("6.   TopUpPremium_J : "+topUpPremium_J);

            BIMAST.setPremiumAllocationCharge_K(staffDisc, bancAssuranceDisc,
                    premFreq);
            premiumAllocationCharge_K = Double.parseDouble(BIMAST.getPremiumAllocationCharge_K());
            _premiumAllocationCharge_K = premiumAllocationCharge_K;
            // System.out.println("7.   PremiumAllocationCharge_K : "+premiumAllocationCharge_K);

            BIMAST.setTopUpCharges_L(prop.topUp);
            topUpCharges_L = Double.parseDouble(BIMAST.getTopUpCharges_L());
            _topUpCharges_L = topUpCharges_L;
            // System.out.println("8.   TopUpCharges_L : "+topUpCharges_L);

            BIMAST.setServiceTaxOnAllocation_M(
                    prop.allocationChargesReductionYield, serviceTax);
            serviceTaxOnAllocation_M = Double.parseDouble(BIMAST.getServiceTaxOnAllocation_M());
            _serviceTaxOnAllocation_M = serviceTaxOnAllocation_M;
            // System.out.println("9.   ServiceTaxOnAllocation_M : "+serviceTaxOnAllocation_M);

            BIMAST.setAmountAvailableForInvestment_N();
            amountAvailableForInvestment_N = Double.parseDouble(BIMAST.getAmountAvailableForInvestment_N());
            _amountAvailableForInvestment_N = amountAvailableForInvestment_N;
            // System.out.println("10.   AmountAvailableForInvestment_N : "+amountAvailableForInvestment_N);

            BIMAST.setSumAssuredRelatedCharges_O(policyTerm, sumAssured, SAMF,
                    effectivePremium, prop.charge_SumAssuredBase);
            sumAssuredRelatedCharges_O = Double.parseDouble(BIMAST.getSumAssuredRelatedCharges_O());
            _sumAssuredRelatedCharges_O = sumAssuredRelatedCharges_O;
            // System.out.println("11.   Sum/AssuredRelatedCharges_O : "+sumAssuredRelatedCharges_O);

            BIMAST.setRiderCharges_P(sumAssured);
            riderCharges_P = Double.parseDouble(BIMAST.getRiderCharges_P());
            _riderCharges_P = riderCharges_P;
            // System.out.println("12.   RiderCharges_P : "+riderCharges_P);

            BIMAST.setPolicyAdministrationCharge_Q(_policyAdministrationCharge_Q, prop.charge_Inflation, prop.fixedMonthlyExp_SP, prop.fixedMonthlyExp_RP, prop.inflation_pa_SP, prop.inflation_pa_RP, premFreq);
            policyAdministrationCharge_Q = Double.parseDouble(BIMAST.getPolicyAdministrationCharge_Q());
            _policyAdministrationCharge_Q = policyAdministrationCharge_Q;
            // System.out.println("13.   PolicyAdministrationCharge_Q : "+policyAdministrationCharge_Q);

            BIMAST.setOneHundredPercentOfCummulativePremium_AW(_oneHundredPercentOfCummulativePremium_AW);
            oneHundredPercentOfCummulativePremium_AW = Double.parseDouble(BIMAST.getOneHundredPercentOfCummulativePremium_AW());
            _oneHundredPercentOfCummulativePremium_AW = oneHundredPercentOfCummulativePremium_AW;
            // System.out.println("45.   OneHundredPercentOfCummulativePremium_AW : "+oneHundredPercentOfCummulativePremium_AW);

            BIMAST.setGuaranteedAddition_AP();
            guaranteedAddition_AP = Double.parseDouble(BIMAST.getGuaranteedAddition_AP());
            _guaranteedAddition_AP = guaranteedAddition_AP;
            // System.out.println("38.   GuaranteedAddition_AP : "+guaranteedAddition_AP);

            BIMAST.setSurrenderCap_AV(effectivePremium, premFreq);
            surrenderCap_AV = Double.parseDouble(BIMAST.getSurrenderCap_AV());
            _surrenderCap_AV = surrenderCap_AV;
            // System.out.println("44.   SurrenderCap_AV : "+surrenderCap_AV);

            BIMAST.setMortalityChargesReductionYield_AG(_fundValueAtEnd_AQ, policyTerm, forBIArray, ageAtEntry, sumAssured, prop.mortalityChargesReductionYield);
            mortalityCharges_AG = Double.parseDouble(BIMAST.getMortalityChargesReductionYield_AG());
            _mortalityCharges_AG = mortalityCharges_AG;
            // System.out.println("29.   MortalityCharges_AG : "+mortalityCharges_AG);

            BIMAST.setTotalChargesReductionYield_AH(policyTerm);
            totalCharges_AH = Double.parseDouble(BIMAST.getTotalChargesReductionYield_AH());
            _totalCharges_AH = totalCharges_AH;
            // System.out.println("30.   TotalCharges_AH: "+totalCharges_AH);

            BIMAST.setTotalServiceTax_ExclOfSTonAllocAndSurrReductionYield_AI(
                    serviceTax,
                    prop.mortalityAndRiderChargesReductionYield,
                    prop.administrationAndSArelatedChargesReductionYield);
            totalServiceTax_ExclOfSTonAllocAndsurr_AI = Double.parseDouble(BIMAST.getTotalServiceTax_ExclOfSTonAllocAndSurrReductionYield_AI());
            _totalServiceTax_ExclOfSTonAllocAndsurr_AI = totalServiceTax_ExclOfSTonAllocAndsurr_AI;
            // System.out.println("31.   TotalServiceTax_ExclOfSTonAllocAndsurr_AI: "+totalServiceTax_ExclOfSTonAllocAndsurr_AI);

            BIMAST.setAdditionToFundIfAnyReductionYield_AK(_fundValueAtEnd_AQ, policyTerm);
            additionToFundIfAny_AK = Double.parseDouble(BIMAST.getAdditionToFundIfAnyReductionYield_AK());
            _additionToFundIfAny_AK = additionToFundIfAny_AK;
            // System.out.println("33.   AdditionToFundIfAny_AK: "+additionToFundIfAny_AK);

            BIMAST.setFundBeforeFMCReductionYield_AL(_fundValueAtEnd_AQ, policyTerm);
            fundBeforeFMC_AL = Double.parseDouble(BIMAST.getFundBeforeFMCReductionYield_AL());
            _fundBeforeFMC_AL = fundBeforeFMC_AL;
            // System.out.println("34.   FundBeforeFMC_AL: "+fundBeforeFMC_AL);

            BIMAST.setFundManagementChargeReductionYield_AM(policyTerm, percentToBeInvested_EquityEliteIIFund,
                    percentToBeInvested_BalancedFund,
                    percentToBeInvested_BondFund,
                    percentToBeInvested_MoneyMarketFund,
                    percentToBeInvested_BondOptimiserFund,
                    percentToBeInvested_EquityFund,
                    percentToBeInvested_MidcapFund,
                    percentToBeInvested_PureFund);
            fundManagementCharge_AM = Double.parseDouble(BIMAST.getFundManagementChargeReductionYield_AM());
            _fundManagementCharge_AM = fundManagementCharge_AM;
            // System.out.println("35.   FundManagementCharge_AM: "+fundManagementCharge_AM);

            BIMAST.setServiceTaxOnFMCReductionYield_AN(prop.fundManagementChargesReductionYield, serviceTax, prop.indexFund, percentToBeInvested_EquityEliteIIFund,
                    percentToBeInvested_BalancedFund,
                    percentToBeInvested_BondFund,
                    percentToBeInvested_MoneyMarketFund,
                    percentToBeInvested_BondOptimiserFund,
                    percentToBeInvested_EquityFund,
                    percentToBeInvested_MidcapFund,
                    percentToBeInvested_PureFund);
            serviceTaxOnFMC_AN = Double.parseDouble(BIMAST.getServiceTaxOnFMCReductionYield_AN());
            _serviceTaxOnFMC_AN = serviceTaxOnFMC_AN;
            // System.out.println("36.   ServiceTaxOnFMC_AN: "+serviceTaxOnFMC_AN);

            BIMAST.setFundValueAfterFMCandBeforeGAReductionYield_AO(policyTerm);
            fundValueAfterFMCAndBeforeGA_AO = Double.parseDouble(BIMAST.getFundValueAfterFMCandBeforeGAReductionYield_AO());
            _fundValueAfterFMCAndBeforeGA_AO = fundValueAfterFMCAndBeforeGA_AO;
            // System.out.println("37.   FundValueAfterFMCAndBeforeGA_AO: "+fundValueAfterFMCAndBeforeGA_AO);

            BIMAST.setMonth_BG(monthNumber);
            month_BG = Integer.parseInt(BIMAST.getMonth_BG());
            _month_BG = month_BG;
            // System.out.println("month_bb "+month_BG);

            BIMAST.setReductionYield_BI(noOfYearsElapsedSinceInception, _fundValueAtEnd_AQ);
            reductionYield_BI = Double.parseDouble(BIMAST.getReductionYield_BI());
            _reductionYield_BI = reductionYield_BI;
            // System.out.println("reductionYield_BU "+reductionYield_BI);

            BIMAST.setReductionYield_BD(policyTerm, _fundValueAtEnd_AQ);
            reductionYield_BD = Double.parseDouble(BIMAST.getReductionYield_BD());
            _reductionYield_BD = reductionYield_BD;
//             System.out.println("reductionYield_BD "+reductionYield_BD);

            BIMAST.setFundValueAtEndReductionYield_AQ();
            fundValueAtEnd_AQ = Double.parseDouble(BIMAST
                    .getFundValueAtEndReductionYield_AQ());
            _fundValueAtEnd_AQ = fundValueAtEnd_AQ;
            // System.out.println("39.   FundValueAtEnd_AQ : "+fundValueAtEnd_AQ);

            BIMAST.setDeathBenefitReductionYield_AU(policyTerm, sumAssured);
            deathBenefit_AU = Double.parseDouble(BIMAST.getDeathBenefitReductionYield_AU());
            _deathBenefit_AU = deathBenefit_AU;
            // System.out.println("43.   DeathBenefit_AU: "+deathBenefit_AU);

            BIMAST.setTotalServiceTaxReductionYield_AJ(serviceTax);
            totalServiceTax_AJ = Double.parseDouble(BIMAST.getTotalServiceTaxReductionYield_AJ());
            _totalServiceTax_AJ = totalServiceTax_AJ;
            // System.out.println("32.   TotalServiceTax_AJ: "+totalServiceTax_AJ);

            BIMAST.setSurrenderChargesReductionYield_AR(effectivePremium,
                    premFreq);
            surrenderCharges_AR = Double.parseDouble(BIMAST.getSurrenderChargesReductionYield_AR());
            _surrenderCharges_AR = surrenderCharges_AR;
            // System.out.println("40.   SurrenderCharges_AR: "+surrenderCharges_AR);

            BIMAST.setServiceTaxOnSurrenderChargesReductionYield_AS(
                    prop.surrenderChargesReductionYield, serviceTax);
            serviceTaxOnSurrenderCharges_AS = Double.parseDouble(BIMAST.getServiceTaxOnSurrenderChargesReductionYield_AS());
            _serviceTaxOnSurrenderCharges_AS = serviceTaxOnSurrenderCharges_AS;
            // System.out.println("41.   ServiceTaxOnSurrenderCharges_AS: "+serviceTaxOnSurrenderCharges_AS);

            BIMAST.setSurrenderValueReductionYield_AT();
            surrenderValue_AT = Double.parseDouble(BIMAST.getSurrenderValueReductionYield_AT());
            _surrenderValue_AT = surrenderValue_AT;
            // System.out.println("42.   SurrenderValue_AT: "+surrenderValue_AT);

            BIMAST.setGuaranteedAddition_AA();
            guaranteedAddition_AA = Double.parseDouble(BIMAST.getGuaranteedAddition_AA());
            _guaranteedAddition_AA = guaranteedAddition_AA;
            // System.out.println("38.   GuaranteedAddition_AA : "+guaranteedAddition_AA);


            BIMAST.setMortalityChargesReductionYield_R(_fundValueAtEnd_AB, policyTerm, forBIArray, ageAtEntry, sumAssured, prop.mortalityChargesReductionYield);
            mortalityCharges_R = Double.parseDouble(BIMAST.getMortalityChargesReductionYield_R());
            _mortalityCharges_R = mortalityCharges_R;
            // System.out.println("29.   MortalityCharges_R : "+mortalityCharges_R);

            BIMAST.setTotalChargesReductionYield_S(policyTerm);
            totalCharges_S = Double.parseDouble(BIMAST.getTotalChargesReductionYield_S());
            _totalCharges_S = totalCharges_S;
            // System.out.println("30.   TotalCharges_S: "+totalCharges_S);

            BIMAST.setTotalServiceTax_ExclOfSTonAllocAndSurrReductionYield_T(
                    serviceTax,
                    prop.mortalityAndRiderChargesReductionYield,
                    prop.administrationAndSArelatedChargesReductionYield);
            totalServiceTax_ExclOfSTonAllocAndSurr_T = Double.parseDouble(BIMAST.getTotalServiceTax_ExclOfSTonAllocAndSurrReductionYield_T());
            _totalServiceTax_ExclOfSTonAllocAndSurr_T = totalServiceTax_ExclOfSTonAllocAndSurr_T;
            // System.out.println("31.   totalServiceTax_ExclOfSTonAllocAndsurrReductionYield_T: "+totalServiceTax_ExclOfSTonAllocAndSurr_T);

            BIMAST.setAdditionToFundIfAnyReductionYield_V(_fundValueAtEnd_AB, policyTerm);
            additionToFundIfAny_V = Double.parseDouble(BIMAST.getAdditionToFundIfAnyReductionYield_V());
            _additionToFundIfAny_V = additionToFundIfAny_V;
            // System.out.println("33.   AdditionToFundIfAny_V: "+additionToFundIfAny_V);

            BIMAST.setFundBeforeFMCReductionYield_W(_fundValueAtEnd_AB, policyTerm);
            fundBeforeFMC_W = Double.parseDouble(BIMAST.getFundBeforeFMCReductionYield_W());
            _fundBeforeFMC_W = fundBeforeFMC_W;
            // System.out.println("34.   FundBeforeFMC_W: "+fundBeforeFMC_W);

            BIMAST.setFundManagementChargeReductionYield_X(policyTerm, percentToBeInvested_EquityEliteIIFund,
                    percentToBeInvested_BalancedFund,
                    percentToBeInvested_BondFund,
                    percentToBeInvested_MoneyMarketFund,
                    percentToBeInvested_BondOptimiserFund,
                    percentToBeInvested_EquityFund,
                    percentToBeInvested_MidcapFund,
                    percentToBeInvested_PureFund);
            fundManagementCharge_X = Double.parseDouble(BIMAST.getFundManagementChargeReductionYield_X());
            _fundManagementCharge_X = fundManagementCharge_X;
            // System.out.println("35.   FundManagementCharge_X: "+fundManagementCharge_X);

            BIMAST.setServiceTaxOnFMCReductionYield_Y(prop.fundManagementChargesReductionYield, serviceTax, prop.indexFund, percentToBeInvested_EquityEliteIIFund,
                    percentToBeInvested_BalancedFund,
                    percentToBeInvested_BondFund,
                    percentToBeInvested_MoneyMarketFund,
                    percentToBeInvested_BondOptimiserFund,
                    percentToBeInvested_EquityFund,
                    percentToBeInvested_MidcapFund,
                    percentToBeInvested_PureFund);
            serviceTaxOnFMC_Y = Double.parseDouble(BIMAST.getServiceTaxOnFMCReductionYield_Y());
            _serviceTaxOnFMC_Y = serviceTaxOnFMC_Y;
            // System.out.println("36.   ServiceTaxOnFMC_Y: "+serviceTaxOnFMC_Y);

            BIMAST.setFundValueAfterFMCandBeforeGAReductionYield_Z(policyTerm);
            fundValueAfterFMCandBeforeGA_Z = Double.parseDouble(BIMAST.getFundValueAfterFMCandBeforeGAReductionYield_Z());
            _fundValueAfterFMCandBeforeGA_Z = fundValueAfterFMCandBeforeGA_Z;
            // System.out.println("37.   FundValueAfterFMCAndBeforeGA_AO: "+fundValueAfterFMCAndBeforeGA_AO);

            BIMAST.setMonth_BG(monthNumber);
            month_BG = Integer.parseInt(BIMAST.getMonth_BG());
            _month_BG = month_BG;
            // System.out.println("month_bb "+month_BG);


            BIMAST.setReductionYield_BC(policyTerm, _fundValueAtEnd_AB);
            reductionYield_BC = Double.parseDouble(BIMAST.getReductionYield_BC());
            _reductionYield_BC = reductionYield_BC;
            // System.out.println("reductionYield_BC "+reductionYield_BC);

            BIMAST.setFundValueAtEndReductionYield_AB();
            fundValueAtEnd_AB = Double.parseDouble(BIMAST
                    .getFundValueAtEndReductionYield_AB());
            _fundValueAtEnd_AB = fundValueAtEnd_AB;
            // System.out.println("39.   FundValueAtEnd_AB : "+fundValueAtEnd_AB);

            BIMAST.setDeathBenefitReductionYield_AF(policyTerm, sumAssured);
            deathBenefit_AF = Double.parseDouble(BIMAST.getDeathBenefitReductionYield_AF());
            _deathBenefit_AF = deathBenefit_AF;
            // System.out.println("43.   DeathBenefit_AF: "+deathBenefit_AF);

            BIMAST.setTotalServiceTaxReductionYield_U(serviceTax);
            totalServiceTax_U = Double.parseDouble(BIMAST.getTotalServiceTaxReductionYield_U());
            _totalServiceTax_U = totalServiceTax_U;
            // System.out.println("32.   TotalServiceTax_U: "+totalServiceTax_U);

            BIMAST.setSurrenderChargesReductionYield_AC(effectivePremium,
                    premFreq);
            surrenderCharges_AC = Double.parseDouble(BIMAST.getSurrenderChargesReductionYield_AC());
            _surrenderCharges_AC = surrenderCharges_AC;
            // System.out.println("40.   SurrenderCharges_AC: "+surrenderCharges_AC);

            BIMAST.setServiceTaxOnSurrenderChargesReductionYield_AD(
                    prop.surrenderChargesReductionYield, serviceTax);
            serviceTaxOnSurrenderCharges_AD = Double.parseDouble(BIMAST.getServiceTaxOnSurrenderChargesReductionYield_AD());
            _serviceTaxOnSurrenderCharges_AD = serviceTaxOnSurrenderCharges_AD;
            // System.out.println("41.   ServiceTaxOnSurrenderCharges_AD: "+serviceTaxOnSurrenderCharges_AD);

            BIMAST.setSurrenderValueReductionYield_AE();
            surrenderValue_AE = Double.parseDouble(BIMAST.getSurrenderValueReductionYield_AE());
            _surrenderValue_AE = surrenderValue_AE;
            // System.out.println("42.   SurrenderValue_AE: "+surrenderValue_AE);

            // System.out.println("***********************************************************************************************************");
            monthNumber++;

            List_BD.add(commonForAllProd.roundUp_Level2(commonForAllProd
                    .getStringWithout_E(reductionYield_BD)));
            List_BI.add(commonForAllProd.roundUp_Level2(commonForAllProd
                    .getStringWithout_E(reductionYield_BI)));
            List_BC.add(commonForAllProd.roundUp_Level2(commonForAllProd
                    .getStringWithout_E(reductionYield_BC)));

        }

        // System.out.println("List_BC.size "+List_BC.size());
        // System.out.println("List_BC "+List_BC);
        // System.out.println("List_BI.size "+List_BI.size());
        // System.out.println("List_BI "+List_BI);
        double ans_BD = BIMAST.irr(List_BD, 0.01);
        double ans_BI = BIMAST.irr(List_BI, 0.01);
        double ans_BC = BIMAST.irr(List_BC, 0.001);
        // System.out.println("ans_BC "+ans_BC);
        // System.out.println("ans1_BI "+ans1);

        BIMAST.setIRRAnnual_BD(ans_BD);
        irrAnnual_BD = Double.parseDouble(BIMAST.getIRRAnnual_BD());
        _irrAnnual_BD = irrAnnual_BD;
        // System.out.println("irrAnnual_BQ "+irrAnnual_BD);

        BIMAST.setIRRAnnual_BI(ans_BI);
        irrAnnual_BI = Double.parseDouble(BIMAST.getIRRAnnual_BI());
        _irrAnnual_BI = irrAnnual_BI;
        // System.out.println("irrAnnual_BU "+irrAnnual_BI);

        BIMAST.setIRRAnnual_BC(ans_BC);
        irrAnnual_BC = Double.parseDouble(BIMAST.getIRRAnnual_BC());
        _irrAnnual_BC = irrAnnual_BC;
        // System.out.println("irrAnnual_BC "+irrAnnual_BC);

        netYield4Pr = (irrAnnual_BC * 100);
        netYield8Pr = (irrAnnual_BD * 100);

        // System.out.println("netYield4Pr "+netYield4Pr);
        // System.out.println("netYield8Pr "+netYield8Pr);

        BIMAST.setReductionInYieldMaturityAt(prop.int2);
        reductionInYieldMaturityAt = Double.parseDouble(BIMAST
                .getReductionInYieldMaturityAt());
        _reductionInYieldMaturityAt = reductionInYieldMaturityAt;

        BIMAST.setReductionInYieldNumberOfYearsElapsedSinceInception(prop.int2);
        reductionInYieldNumberOfYearsElapsedSinceInception = Double
                .parseDouble(BIMAST
                        .getReductionInYieldNumberOfYearsElapsedSinceInception());
        _reductionInYieldNumberOfYearsElapsedSinceInception = reductionInYieldNumberOfYearsElapsedSinceInception;
        // System.out.println("reductionInYieldNumberOfYearsElapsedSinceInception "+reductionInYieldNumberOfYearsElapsedSinceInception);


//		return new String[]{commonForAllProd.getRoundOffLevel2(commonForAllProd.getStringWithout_E(reductionInYieldMaturityAt)),commonForAllProd.getRoundOffLevel2(commonForAllProd.getStringWithout_E(reductionInYieldNumberOfYearsElapsedSinceInception)),commonForAllProd.getRoundOffLevel2(commonForAllProd.getStringWithout_E(netYield4Pr)),commonForAllProd.getRoundOffLevel2(commonForAllProd.getStringWithout_E(netYield8Pr))};
        return new String[]{commonForAllProd.roundUp_Level2(commonForAllProd.getStringWithout_E(reductionInYieldMaturityAt)), commonForAllProd.roundUp_Level2(commonForAllProd.getStringWithout_E(reductionInYieldNumberOfYearsElapsedSinceInception)), commonForAllProd.roundUp_Level2(commonForAllProd.getStringWithout_E(netYield4Pr)), commonForAllProd.roundUp_Level2(commonForAllProd.getStringWithout_E(netYield8Pr))};

    }

    public String getEffectivePremium(String planType, String premiumAmount, String premFreqMode) {
        double effPrem = 0;
		/*int PF = 1;
		double premium=0;
		try
		{
			if(planType.equals("Single"))
			{premium=65000;}
			else if(planType.equals("Limited"))
			{premium=40000;}
			else if(planType.equals("Regular"))
			{premium=30000;}

			if(((Math.max(premium,Double.parseDouble(premiumAmount))*PF) %  100)==0)
			{effPrem=(Math.max(premium,Double.parseDouble(premiumAmount))*PF);}
			else
			{effPrem=(Math.max(premium,Double.parseDouble(premiumAmount))*PF-((Math.max(premium,Double.parseDouble(premiumAmount))*PF%100)));}
	}
		catch(Exception e){}*/

        if (planType.equals("Limited")) {
            if (premFreqMode.equals("Yearly")) {

                effPrem = Double.parseDouble(premiumAmount);
            } else if (premFreqMode.equals("Half Yearly")) {

                effPrem = Double.parseDouble(premiumAmount) * 2;
            } else if (premFreqMode.equals("Quarterly")) {
                effPrem = Double.parseDouble(premiumAmount) * 4;
            } else if (premFreqMode.equals("Monthly")) {
                effPrem = Double.parseDouble(premiumAmount) * 12;
            }
        } else {
            effPrem = Double.parseDouble(premiumAmount);

        }
        return "" + effPrem;
    }
   /* public String getEffectivePremium(String planType, String premiumAmount) {
        double effPrem = 0;
        int PF = 1;
        double premium = 0;
        try {
            if (planType.equals("Single")) {
                premium = 65000;
            } else if (planType.equals("Limited")) {
                premium = 40000;
            } else if (planType.equals("Regular")) {
                premium = 30000;
            }

            if (((Math.max(premium, Double.parseDouble(premiumAmount)) * PF) % 100) == 0) {
                effPrem = (Math.max(premium, Double.parseDouble(premiumAmount)) * PF);
            } else {
                effPrem = (Math.max(premium, Double.parseDouble(premiumAmount)) * PF - ((Math.max(premium, Double.parseDouble(premiumAmount)) * PF % 100)));
            }
        } catch (Exception e) {
        }
        return "" + effPrem;
    }*/


    /********************************** Calculation ends here **********************************************************/

    public String getEffectivePremium() {

        if (selPremFreq.getSelectedItem().toString().equals("Single")) {
            PF = 1;
        } else {
            if (selPremiumFrequncyMode.getSelectedItem().toString()
                    .equals("Yearly")) {
                PF = 1;
            } else if (selPremiumFrequncyMode.getSelectedItem().toString()
                    .equals("Half Yearly")) {
                PF = 2;
            } else if (selPremiumFrequncyMode.getSelectedItem().toString()
                    .equals("Quarterly")) {
                PF = 4;
            } else if (selPremiumFrequncyMode.getSelectedItem().toString()
                    .equals("Monthly")) {
                PF = 12;
            }
        }

        if (selPremFreq.getSelectedItem().toString().equals("Single")
                && (!premiumAmt.getText().toString().equals(""))) {
            return premiumAmt.getText().toString();
        }
        // For Mode=LPPT
        else {
            if (!premiumAmt.getText().toString().equals("")) {
                if (((Integer.parseInt(premiumAmt.getText().toString()) * PF) % 100) == 0) {
                    return ""
                            + (Integer
                            .parseInt(premiumAmt.getText().toString()) * PF);
                } else {
                    return ""
                            + (Integer
                            .parseInt(premiumAmt.getText().toString())
                            * PF - ((Integer.parseInt(premiumAmt
                            .getText().toString()) * PF) % 100));
                }
            } else {
                return "";
            }
        }
    }

    /********************************** validations starts from here **********************************************************/

    // Validate Premium Amount
    private boolean valPremiumAmt() {
        String errorMessage = "";
        if (premiumAmt.getText().toString().equals("")) {
            errorMessage = "Please enter Premium Amount in Rs. ";
        } else if (!(Double.parseDouble(premiumAmt.getText().toString()) % 100 == 0)) {
            errorMessage = "Premium Amount should be multiple of 100";
        } else {
            // For Single Premium
            if (selPremFreq.getSelectedItem().toString().equals("Single")) {
                if (Double.parseDouble(premiumAmt.getText().toString()) < 250000) {
                    errorMessage = "Premium Amount should not be less than Rs. 2,50,000";
                }
            }
            // For Regular Frequency Mode
            else {
                if (selPremiumFrequncyMode.getSelectedItem().toString()
                        .equals("Monthly")) {
                    if (Double.parseDouble(premiumAmt.getText().toString()) < 21000) {
                        errorMessage = "Premium Amount should not be less than Rs. 21,000";
                    }
                } else if (selPremiumFrequncyMode.getSelectedItem().toString()
                        .equals("Quarterly")) {
                    if (Double.parseDouble(premiumAmt.getText().toString()) < 62500) {
                        errorMessage = "Premium Amount should not be less than Rs. 62,500";
                    }
                } else if (selPremiumFrequncyMode.getSelectedItem().toString()
                        .equals("Half Yearly")) {
                    if (Double.parseDouble(premiumAmt.getText().toString()) < 125000) {
                        errorMessage = "Premium Amount should not be less than Rs. 1,25,000";
                    }
                } else if (selPremiumFrequncyMode.getSelectedItem().toString()
                        .equals("Yearly")) {
                    if (Double.parseDouble(premiumAmt.getText().toString()) < 250000) {
                        errorMessage = "Premium Amount should not be less than Rs. 2,50,000";
                    }
                }
            }
        }

        if (!errorMessage.equals("")) {
            showAlert.setMessage(errorMessage);
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

    private void valMaturityAge() {
        int Age = Integer.parseInt(ageInYears.getSelectedItem().toString());
        int PolicyTerm = Integer.parseInt(selPolicyTerm.getSelectedItem()
                .toString());
        if ((Age + PolicyTerm) > 65) {
            showAlert.setMessage("Maturity age is 65 years");
            showAlert.setNeutralButton("OK",
                    new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            selPolicyTerm.setSelection(0, false);
                        }
                    });
            showAlert.show();
            btn_bi_smart_elite_life_assured_date.requestFocus();
        }

    }

    // Validate SAMF
    private boolean valSAMF() {
        String errorMessage = "";
        if (SAMF.getText().toString().equals("")) {
            errorMessage = "Please enter Sum Assured Multiple Factor (SAMF)";
            SAMF.requestFocus();

        } else if (selPremFreq.getSelectedItem().toString().equals("Single")) {
            double maxSAMFforSingleFreq = 0, minSAMFforSinglefreq = 0;

           /* if (Integer.parseInt(ageInYears.getSelectedItem().toString()) < 45) {
				minSAMFforSinglefreq = 1.25;
			} else {
				minSAMFforSinglefreq = 1.1;
            }*/
            minSAMFforSinglefreq = 1.25;
            maxSAMFforSingleFreq = 1.25;

            if (Double.parseDouble(SAMF.getText().toString()) < minSAMFforSinglefreq
                    || Double.parseDouble(SAMF.getText().toString()) > maxSAMFforSingleFreq) {
                errorMessage = "Sum Assured Multiple Factor (SAMF) should be in the range of "
                        + minSAMFforSinglefreq
                        + " to "
                        + maxSAMFforSingleFreq
                        + " for Single Frequency Mode";

            }

        }
        // For Regular Frequency Mode
        else {
            double minSAMFforLPPTfreq = 0, maxSAMFforLPPTfreq = 0;
           /* if (Integer.parseInt(ageInYears.getSelectedItem().toString()) < 45) {
				minSAMFforLPPTfreq = 10;
			} else {
				minSAMFforLPPTfreq = 7;
            }*/
            minSAMFforLPPTfreq = 7;
            maxSAMFforLPPTfreq = 7;

            if (Double.parseDouble(SAMF.getText().toString()) < minSAMFforLPPTfreq
                    || Double.parseDouble(SAMF.getText().toString()) > maxSAMFforLPPTfreq) {
                errorMessage = "Sum Assured Multiple Factor (SAMF) should be in the range of "
                        + minSAMFforLPPTfreq
                        + " to "
                        + maxSAMFforLPPTfreq
                        + " for LPPT Frequency Mode";
            }
        }

        if (!errorMessage.equals("")) {
            showAlert.setMessage(errorMessage);
            showAlert.setNeutralButton("OK",
                    new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            SAMF.requestFocus();
                        }
                    });
            showAlert.show();
            return false;
        } else
            return true;
    }

    // Validation of No. of years elapsed since Inception
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
                            noOfYearsElapsedSinceInception.requestFocus();
                        }
                    });
            showAlert.show();

            return false;

        }
        return true;
    }

    // Addition of invested fund must be 100%
    private boolean valTotalAllocation() {
        String error = "";
        double equityEliteIIFund, balancedFund, bondFund, moneyMarketFund,
                PureFund,
                MidcapFund, BondOptimiserFund, corporateBondFund;

        if (!percent_EquityEliteIIFund.getText().toString().equals(""))
            equityEliteIIFund = Double.parseDouble(percent_EquityEliteIIFund
                    .getText().toString());
        else
            equityEliteIIFund = 0.0;

        if (!percent_BalancedFund.getText().toString().equals(""))
            balancedFund = Double.parseDouble(percent_BalancedFund.getText()
                    .toString());
        else
            balancedFund = 0.0;

        if (!percent_BondFund.getText().toString().equals(""))
            bondFund = Double
                    .parseDouble(percent_BondFund.getText().toString());
        else
            bondFund = 0.0;

        if (!percent_MoneyMarketFund.getText().toString().equals(""))
            moneyMarketFund = Double.parseDouble(percent_MoneyMarketFund
                    .getText().toString());
        else
            moneyMarketFund = 0.0;


        if (!Percent_PureFund.getText().toString().equals(""))
            PureFund = Double
                    .parseDouble(Percent_PureFund.getText().toString());
        else
            PureFund = 0.0;


        if (!Percent_MidcapFund.getText().toString().equals(""))
            MidcapFund = Double
                    .parseDouble(Percent_MidcapFund.getText().toString());
        else
            MidcapFund = 0.0;


        if (!Percent_BondOptimiserFund.getText().toString().equals(""))
            BondOptimiserFund = Double
                    .parseDouble(Percent_BondOptimiserFund.getText().toString());
        else
            BondOptimiserFund = 0.0;

        if (!percent_CorporateBondFund.getText().toString().equals(""))
            corporateBondFund = Double.parseDouble(percent_CorporateBondFund.getText()
                    .toString());
        else
            corporateBondFund = 0;


        if ((equityEliteIIFund + balancedFund + bondFund + moneyMarketFund + PureFund + MidcapFund + BondOptimiserFund + corporateBondFund) != 100)
            error = "Total sum of % to be invested for all fund should be equal to 100%";

        // display alert message
        if (!error.equals("")) {
            showAlert.setMessage(error);
            showAlert.setNeutralButton("OK",
                    new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            percent_EquityEliteIIFund.requestFocus();
                        }
                    });
            showAlert.show();

            return false;
        } else
            return true;
    }


    // Policy term [Maximum Policy Term based on age validation]

    // Validate Premium Paying Term
    private boolean valPPT() {
        int PT = Integer.parseInt(selPolicyTerm.getSelectedItem().toString());
        if (Integer.parseInt(premPayingTerm.getSelectedItem().toString()) > Integer
                .parseInt(selPolicyTerm.getSelectedItem().toString())) {
            premPayingTerm.setSelection(0, false);
            // Show error message if premium frequency is 'Limited' otherwise
            // only reset premium paying term
            if (selPremFreq.getSelectedItem().toString().equals("Limited")) {
                showAlert
                        .setMessage("Premium Paying Term should be less than or equal to Policy Term");
                showAlert.setNeutralButton("OK",
                        new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // apply focusable method
                                setFocusable(selPremFreq);
                                selPremFreq.requestFocus();

                            }
                        });
                showAlert.show();
                return false;
            } else {
                return true;
            }


        } else if (selPremFreq.getSelectedItem().toString().equals("Limited") && (Integer.parseInt(premPayingTerm.getSelectedItem().toString()) == 7) && (PT < 10)) {

            showAlert
                    .setMessage("For PPT " + premPayingTerm.getSelectedItem().toString() + " Applicable Policy Term is from 10 to 30");
            showAlert.setNeutralButton("OK",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog,
                                            int which) {
                            // apply focusable method
                            setFocusable(selPremFreq);
                            selPremFreq.requestFocus();

                        }
                    });
            showAlert.show();

            return false;
        } else if (selPremFreq.getSelectedItem().toString().equals("Limited") && (Integer.parseInt(premPayingTerm.getSelectedItem().toString()) == 10) && (PT < 12)) {

            showAlert
                    .setMessage("For PPT " + premPayingTerm.getSelectedItem().toString() + " Applicable Policy Term is from 12 to 30");
            showAlert.setNeutralButton("OK",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog,
                                            int which) {
                            // apply focusable method
                            setFocusable(selPremFreq);
                            selPremFreq.requestFocus();

                        }
                    });
            showAlert.show();

            return false;
        } else if (selPremFreq.getSelectedItem().toString().equals("Limited") && Integer.parseInt(premPayingTerm.getSelectedItem().toString()) == 12 && (PT < 15)) {

            showAlert
                    .setMessage("For PPT " + premPayingTerm.getSelectedItem().toString() + " Applicable Policy Term is from 15 to 30");
            showAlert.setNeutralButton("OK",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog,
                                            int which) {
                            // apply focusable method
                            setFocusable(selPremFreq);
                            selPremFreq.requestFocus();

                        }
                    });
            showAlert.show();

            return false;
        } else {
            return true;
        }
    }

    public boolean valPolicyTerm() {
        int minPloicyTerm = 0;
        if (selPremFreq.getSelectedItem().toString().equals("Single")) {
            minPloicyTerm = 5;
        } else {
            minPloicyTerm = 10;
        }

        int maxPolicyTerm = Math
                .min(30, (65 - Integer.parseInt(ageInYears.getSelectedItem()
                        .toString())));
        if ((Integer.parseInt(selPolicyTerm.getSelectedItem().toString()) > maxPolicyTerm)
                || (((Integer.parseInt(selPolicyTerm.getSelectedItem().toString()) < 5) &&
                selPremFreq.getSelectedItem().toString().equals("Single")) ||
                ((Integer.parseInt(selPolicyTerm.getSelectedItem().toString()) < 10) &&
                        selPremFreq.getSelectedItem().toString().equals("Limited")))) {

            // showAlert.setMessage("Max Policy Term limit is "+ maxPolicyTerm+
            // " Years");

            showAlert
                    .setMessage("Maximum Maturity Age is 65 years .Please enter Policy Term between " + minPloicyTerm + " to "
                            + maxPolicyTerm + " years");
            showAlert.setNeutralButton("OK",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            selPolicyTerm.setSelection(0, false);
                        }
                    });
            showAlert.show();
            selPolicyTerm.requestFocus();
            return false;
        } else if (Integer.parseInt(selPolicyTerm.getSelectedItem().toString()) < 10 &&
                selPremFreq.getSelectedItem().toString().equals("Limited")) {

            // showAlert.setMessage("Max Policy Term limit is "+ maxPolicyTerm+
            // " Years");

            showAlert
                    .setMessage("Maximum Maturity Age is 65 years .Please enter Policy Term between " + minPloicyTerm + " to "
                            + maxPolicyTerm + " years");
            showAlert.setNeutralButton("OK",
                    new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            selPolicyTerm.setSelection(0, false);
                        }
                    });
            showAlert.show();
            selPolicyTerm.requestFocus();
            return false;
        } else {
            return true;
        }
    }

    // Proper Name Validation
    public boolean valName() {
        if (!proposer_Title.equals("") && !proposer_First_Name.equals("")
                && !proposer_Last_Name.equals("")) {
            return true;
        } else {
            showAlert.setMessage("Please Fill The Detail For Name");
            showAlert.setNeutralButton("OK",
                    new DialogInterface.OnClickListener() {

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
                            // apply focusable method
                            setFocusable(rb_smart_elite_proposer_same_as_life_assured_yes);
                            rb_smart_elite_proposer_same_as_life_assured_yes
                                    .requestFocus();
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
                                    setFocusable(spnr_bi_smart_elite_life_assured_title);
                                    spnr_bi_smart_elite_life_assured_title
                                            .requestFocus();
                                } else if (lifeAssured_First_Name.equals("")) {
                                    setFocusable(edt_bi_smart_elite_life_assured_first_name);
                                    edt_bi_smart_elite_life_assured_first_name
                                            .requestFocus();
                                } else {
                                    setFocusable(edt_bi_smart_elite_life_assured_last_name);
                                    edt_bi_smart_elite_life_assured_last_name
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
                                setFocusable(spnr_bi_smart_elite_life_assured_title);
                                spnr_bi_smart_elite_life_assured_title
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
                                setFocusable(spnr_bi_smart_elite_life_assured_title);
                                spnr_bi_smart_elite_life_assured_title
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
                                setFocusable(spnr_bi_smart_elite_life_assured_title);
                                spnr_bi_smart_elite_life_assured_title
                                        .requestFocus();
                            }
                        });
                showAlert.show();

                return false;
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
                                        // apply focusable methokd
                                        setFocusable(spnr_bi_smart_elite_life_assured_title);
                                        spnr_bi_smart_elite_life_assured_title
                                                .requestFocus();
                                    } else if (lifeAssured_First_Name.equals("")) {
                                        edt_bi_smart_elite_life_assured_first_name
                                                .requestFocus();
                                    } else {
                                        edt_bi_smart_elite_life_assured_last_name
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
                                    setFocusable(spnr_bi_smart_elite_life_assured_title);
                                    spnr_bi_smart_elite_life_assured_title
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
                                    setFocusable(spnr_bi_smart_elite_life_assured_title);
                                    spnr_bi_smart_elite_life_assured_title
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
                                    setFocusable(spnr_bi_smart_elite_life_assured_title);
                                    spnr_bi_smart_elite_life_assured_title
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

        } else
            return true;
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
                                setFocusable(btn_bi_smart_elite_life_assured_date);
                                btn_bi_smart_elite_life_assured_date
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
                                setFocusable(btn_bi_smart_elite_life_assured_date);
                                btn_bi_smart_elite_life_assured_date
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
                                setFocusable(btn_bi_smart_elite_proposer_date);
                                btn_bi_smart_elite_proposer_date.requestFocus();
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

    /********************************** help start ************************************************************/

    // Help for premium amount
    private void updatePremiumAmtLabel() {
        try {

            if (selPremFreq.getSelectedItem().toString().equals("Single")) {
                help_premiumAmt.setText("(Min. Rs.2,50,000)");
            }
            // For Regular Frequency Mode
            else {
                if (selPremiumFrequncyMode.getSelectedItem().toString()
                        .equals("Monthly")) {
                    help_premiumAmt.setText("(Min. Rs.21,000)");
                } else if (selPremiumFrequncyMode.getSelectedItem().toString()
                        .equals("Quarterly")) {
                    help_premiumAmt.setText("(Min. Rs.62,500)");
                } else if (selPremiumFrequncyMode.getSelectedItem().toString()
                        .equals("Half Yearly")) {
                    help_premiumAmt.setText("(Min. Rs.1,25,000)");
                } else if (selPremiumFrequncyMode.getSelectedItem().toString()
                        .equals("Yearly")) {
                    help_premiumAmt.setText("(Min. Rs.2,50,000)");
                }
            }

        } catch (Exception ignored) {
        }
    }

    // Help for SAMF
    private void updateSAMFlabel() {
        try {

            if (selPremFreq.getSelectedItem().toString().equals("Single")) {
                /** Modified by Akshaya on 3-APR-2014 start ***/
                double maxSAMFforSingleFreq = 0, minSAMFforSinglefreq = 0;

               /* if (Integer.parseInt(ageInYears.getSelectedItem().toString()) < 45) {
					minSAMFforSinglefreq = 1.25;
				} else {
					minSAMFforSinglefreq = 1.1;
                }*/

                //maxSAMFforSingleFreq = 5;

                minSAMFforSinglefreq = 1.25;
                maxSAMFforSingleFreq = 1.25;

                help_SAMF.setText("(" + minSAMFforSinglefreq + " to "
                        + maxSAMFforSingleFreq + ")");

                /** Modified by Akshaya on 3-APR-2014 end ***/

            }
            // For Regular Frequency Mode
            else {
                double minSAMFforLPPTfreq = 0, maxSAMFforLPPTfreq = 0;
              /*  if (Integer.parseInt(ageInYears.getSelectedItem().toString()) < 45) {
					minSAMFforLPPTfreq = 10;
				} else {
					minSAMFforLPPTfreq = 7;
				}

                maxSAMFforLPPTfreq = 20;*/
                minSAMFforLPPTfreq = 7;
                maxSAMFforLPPTfreq = 7;

                help_SAMF.setText("(" + minSAMFforLPPTfreq + " to "
                        + maxSAMFforLPPTfreq + ")");
                // SAMF.setText(minSAMFforLPPTfreq + "");
            }

        } catch (Exception e) {
        }
    }

    // help for no of yeras elapsed since Inception
    private void updatenoOfYearsElapsedSinceInception() {
        try {
            help_noOfYearsElapsedSinceInception.setText("(5 to "
                    + selPolicyTerm.getSelectedItem().toString() + " years)");
            // noOfYearsElapsedSinceInception.setText("5");
        } catch (Exception ignored) {
        }
    }

    /********************************** help end ************************************************************/

    private void CreateSmartEliteBIPDf() {
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
            Font small_normal2 = new Font(Font.FontFamily.TIMES_ROMAN, 6,
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

            // For the BI Smart Elite Table Header(Grey One)
            Paragraph Para_Header = new Paragraph();
            Para_Header
                    .add(new Paragraph(
                            "IN THIS POLICY, THE INVESTMENT RISK IN INVESTMENT PORTFOLIO IS BORNE BY THE POLICYHOLDER.",
                            headerBold));

            PdfPTable headertable = new PdfPTable(1);
            headertable.setWidthPercentage(100);
            PdfPCell c1 = new PdfPCell(new Phrase(Para_Header));
            c1.setBackgroundColor(BaseColor.WHITE);
            c1.setPadding(5);
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            headertable.addCell(c1);
            headertable.setHorizontalAlignment(Element.ALIGN_CENTER);
            Paragraph para_address = new Paragraph(
                    "SBI Life Insurance Co. Ltd ",
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
                    "SBI Life - Smart Elite (111L072V04)",
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
            document.add(para_address8);
            //document.add(headertable);
            document.add(para_img_logo_after_space_1);


            document.add(para_img_logo_after_space_1);
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
                            "Insurance Regulatory and Development authority of India (IRDAI) requires all life insurance companies operating in India to provide official illustrations to their customers. The illustrations are based on the investment rates of return set by the Life Insurance Council (constituted under Section 64C(a) of the Insurance Act 1938) and is not intended to reflect the actual investment returns achieved or which may be achieved in future by SBI life Insurance Company Limited.",
                            small_normal));

            BI_Pdftable1_cell1.setPadding(5);

            BI_Pdftable1_cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
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

            BI_Pdftable2_cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_Pdftable2.addCell(BI_Pdftable2_cell1);
            document.add(BI_Pdftable2);

            PdfPTable BI_Pdftable3 = new PdfPTable(1);
            BI_Pdftable3.setWidthPercentage(100);
            // CR_table6.setWidths(columnWidths_3);
            PdfPCell BI_Pdftable3_cell1 = new PdfPCell(
                    new Paragraph(
                            "Some benefits are guaranteed and some benefits are variable with returns based on the future fund performance of SBI Life Insurance Company Limited. If your policy offers guaranteed returns then the same will be clearly marked as “guaranteed” in the illustration table on this page. If your policy offers variable returns then the illustration on this page will show two different rates of assumed future investment returns. These assumed rates of return are not guaranteed and they are not the upper or lower limits of what you might get back, as the value of your policy is dependent on a number of factors including future fund investment performance.",
                            small_normal));

            BI_Pdftable3_cell1.setPadding(5);

            BI_Pdftable3_cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_Pdftable3.addCell(BI_Pdftable3_cell1);
            document.add(BI_Pdftable3);

            PdfPTable BI_Pdftable4 = new PdfPTable(1);
            BI_Pdftable4.setWidthPercentage(100);
            // CR_table6.setWidths(columnWidths_4);
            PdfPCell BI_Pdftable4_cell1 = new PdfPCell(
                    new Paragraph(
                            "Some benefits are guaranteed and some benefits are variable with returns based on the future fund performance of SBI Life Insurance Company Limited. If your policy offers guaranteed returns then the same will be clearly marked as �guaranteed� in the illustration table on this page. If your policy offers variable returns then the illustration on this page will show two different rates of assumed future investment returns. These assumed rates of return are not guaranteed and they are not the upper or lower limits of what you might get back, as the value of your policy is dependent on a number of factors including future fund investment performance.",

                            small_normal));

            BI_Pdftable4_cell1.setPadding(5);

            BI_Pdftable4_cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_Pdftable4.addCell(BI_Pdftable4_cell1);
            //document.add(BI_Pdftable4);

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

            PdfPTable table_lifeAssuredDetails = new PdfPTable(4);
            table_lifeAssuredDetails.setWidthPercentage(100);

            PdfPCell cell_lifeAssuredAge1 = new PdfPCell(new Paragraph(
                    "Age of the Life Assured", small_normal));
            cell_lifeAssuredAge1.setPadding(5);
            PdfPCell cell_lifeAssuredAge2 = new PdfPCell(new Paragraph(
                    age_entry + " Years", small_bold));
            cell_lifeAssuredAge2.setPadding(5);
            cell_lifeAssuredAge2.setHorizontalAlignment(Element.ALIGN_CENTER);

            PdfPCell cell_lifeAssuredAmaturityAge1 = new PdfPCell(
                    new Paragraph("Maturity Age" + "  ", small_normal));
            cell_lifeAssuredAmaturityAge1.setPadding(5);
            PdfPCell cell_lifeAssuredAmaturityAge2 = new PdfPCell(
                    new Paragraph(maturity_age + " Years", small_bold));
            cell_lifeAssuredAmaturityAge2.setPadding(5);
            cell_lifeAssuredAmaturityAge2
                    .setHorizontalAlignment(Element.ALIGN_CENTER);

            PdfPCell cell_Proposername = new PdfPCell(new Paragraph(
                    "Name of the Proposer", small_normal));
            cell_Proposername.setPadding(5);
            PdfPCell cell_Proposername2 = new PdfPCell(new Paragraph(
                    name_of_life_assured, small_bold));
            cell_Proposername2.setPadding(5);
            cell_Proposername2.setHorizontalAlignment(Element.ALIGN_CENTER);

            PdfPCell cell_Proposername3 = new PdfPCell(new Paragraph(
                    "Age of the Proposer", small_normal));
            cell_Proposername3.setPadding(5);
            PdfPCell cell_Proposername4 = new PdfPCell(new Paragraph(
                    age_entry + " Years", small_bold));
            cell_Proposername4.setPadding(5);
            cell_Proposername4.setHorizontalAlignment(Element.ALIGN_CENTER);

            PdfPCell cell_Proposername5 = new PdfPCell(new Paragraph(
                    "Name of the Life Assured", small_normal));
            cell_Proposername5.setPadding(5);
            PdfPCell cell_Proposername6 = new PdfPCell(new Paragraph(
                    name_of_life_assured, small_bold));
            cell_Proposername6.setPadding(5);
            cell_Proposername6.setHorizontalAlignment(Element.ALIGN_CENTER);

            table_lifeAssuredDetails.addCell(cell_Proposername);
            table_lifeAssuredDetails.addCell(cell_Proposername2);
            table_lifeAssuredDetails.addCell(cell_Proposername3);
            table_lifeAssuredDetails.addCell(cell_Proposername4);
            table_lifeAssuredDetails.addCell(cell_Proposername5);
            table_lifeAssuredDetails.addCell(cell_Proposername6);
            table_lifeAssuredDetails.addCell(cell_lifeAssuredAge1);
            table_lifeAssuredDetails.addCell(cell_lifeAssuredAge2);

            //table_lifeAssuredDetails.addCell(cell_lifeAssuredAmaturityAge1);
            //table_lifeAssuredDetails.addCell(cell_lifeAssuredAmaturityAge2);

            document.add(table_lifeAssuredDetails);

            PdfPTable table_lifeAssuredDetails_gender_policy_term = new PdfPTable(
                    4);
            table_lifeAssuredDetails_gender_policy_term.setWidthPercentage(100);

            PdfPCell cell_lifeAssured_gender1 = new PdfPCell(new Paragraph(
                    "Life Assured Gender" + "  ", small_normal));
            cell_lifeAssured_gender1.setPadding(5);
            PdfPCell cell_lifeAssured_gender2 = new PdfPCell(new Paragraph(
                    gender, small_bold));
            cell_lifeAssured_gender2.setPadding(5);
            cell_lifeAssured_gender2
                    .setHorizontalAlignment(Element.ALIGN_CENTER);

            PdfPCell cell_termsofPolicy1 = new PdfPCell(new Paragraph(
                    "Policy Term  ", small_normal));

            PdfPCell cell_termsofPolicy2 = new PdfPCell(new Paragraph("  "
                    + policy_term + " Years", small_bold));
            cell_termsofPolicy2.setPadding(5);
            cell_termsofPolicy2.setHorizontalAlignment(Element.ALIGN_CENTER);

            PdfPCell cell_termsofPolicy12 = new PdfPCell(new Paragraph(
                    "Premium Payment Term ", small_normal));
            PdfPCell cell_termsofPolicy22;
            if (selPremFreq.getSelectedItem().toString().equals("Single")) {

                cell_termsofPolicy22 = new PdfPCell(new Paragraph(" One time at the inception of the policy ", small_bold));
                cell_termsofPolicy22.setPadding(5);
                cell_termsofPolicy22.setHorizontalAlignment(Element.ALIGN_CENTER);


            } else {
                cell_termsofPolicy22 = new PdfPCell(new Paragraph(premium_paying_term + "", small_bold));
                cell_termsofPolicy22.setPadding(5);
                cell_termsofPolicy22.setHorizontalAlignment(Element.ALIGN_CENTER);
            }

            // table_lifeAssuredDetails_gender_policy_term
            //       .addCell(cell_lifeAssured_gender1);
            // table_lifeAssuredDetails_gender_policy_term
            //        .addCell(cell_lifeAssured_gender2);

            table_lifeAssuredDetails_gender_policy_term
                    .addCell(cell_termsofPolicy1);
            table_lifeAssuredDetails_gender_policy_term
                    .addCell(cell_termsofPolicy2);

            table_lifeAssuredDetails_gender_policy_term
                    .addCell(cell_termsofPolicy12);
            table_lifeAssuredDetails_gender_policy_term
                    .addCell(cell_termsofPolicy22);


            document.add(table_lifeAssuredDetails_gender_policy_term);

            String Ann_singlePremium = "";
            // if (premFreq.equalsIgnoreCase("Limited")) {
            // Ann_singlePremium = "Annualised Premium";
            // }
            String premFreq = prsObj.parseXmlTag(input, "premFreq");
            String premiumAmount = prsObj.parseXmlTag(input, "premiumAmount");
            if (premFreq.equalsIgnoreCase("Single")) {
                Ann_singlePremium = "Amount of Installment Premium ";
            } else {
                Ann_singlePremium = "Amount of Installment Premium ";

            }

            PdfPTable table_lifeAssuredDetails_premium_sum_assured = new PdfPTable(
                    4);
            table_lifeAssuredDetails_premium_sum_assured
                    .setWidthPercentage(100);

            PdfPCell cell_regularPremium1 = new PdfPCell(new Paragraph(
                    Ann_singlePremium, small_normal));
            cell_lifeAssuredAmaturityAge1.setPadding(5);
            PdfPCell cell_regularPremium2 = new PdfPCell(new Paragraph("Rs. "
                    + premiumAmount, small_bold));
            cell_regularPremium2.setPadding(5);
            cell_regularPremium2.setHorizontalAlignment(Element.ALIGN_CENTER);

            PdfPCell mode = new PdfPCell(new Paragraph(
                    "Mode / Frequency of Premium Payment " + "  ", small_normal));
            PdfPCell mode2;
            String premFreqMod_str = prsObj.parseXmlTag(input, "premFreqMode");
            if (selPremFreq.getSelectedItem().toString().equals("Single")) {
                mode2 = new PdfPCell(new Paragraph("One time at the inception of the policy", small_bold));
                mode2.setPadding(5);
                mode2.setHorizontalAlignment(Element.ALIGN_CENTER);

            } else {
                mode2 = new PdfPCell(new Paragraph(premFreqMod_str, small_bold));
                mode2.setPadding(5);
                mode2.setHorizontalAlignment(Element.ALIGN_CENTER);


            }


            PdfPCell cell_sumAssured1 = new PdfPCell(new Paragraph(
                    "Sum assured" + "  ", small_normal));
            PdfPCell cell_sumAssured2 = new PdfPCell(new Paragraph("Rs. "
                    + sum_assured, small_bold));
            cell_sumAssured2.setPadding(5);
            cell_sumAssured2.setHorizontalAlignment(Element.ALIGN_CENTER);

            PdfPCell cell_sumAssured11 = new PdfPCell(new Paragraph(
                    "" + "  ", small_normal));
            PdfPCell cell_sumAssured12 = new PdfPCell(new Paragraph(
                    "" + "  ", small_normal));

            table_lifeAssuredDetails_premium_sum_assured
                    .addCell(cell_regularPremium1);
            table_lifeAssuredDetails_premium_sum_assured
                    .addCell(cell_regularPremium2);

            table_lifeAssuredDetails_premium_sum_assured
                    .addCell(mode);
            table_lifeAssuredDetails_premium_sum_assured
                    .addCell(mode2);

            table_lifeAssuredDetails_premium_sum_assured
                    .addCell(cell_sumAssured1);
            table_lifeAssuredDetails_premium_sum_assured
                    .addCell(cell_sumAssured2);

            table_lifeAssuredDetails_premium_sum_assured
                    .addCell(cell_sumAssured11);
            table_lifeAssuredDetails_premium_sum_assured
                    .addCell(cell_sumAssured12);
            document.add(table_lifeAssuredDetails_premium_sum_assured);

            PdfPTable table_plan_premium_payingTerm = new PdfPTable(4);
            table_plan_premium_payingTerm.setWidthPercentage(100);

            PdfPCell cell_plan1 = new PdfPCell(new Paragraph("Plan",
                    small_normal));
            cell_plan1.setPadding(5);
            PdfPCell cell_plan2 = new PdfPCell(new Paragraph(plan, small_bold));
            cell_plan2.setPadding(5);
            cell_plan2.setHorizontalAlignment(Element.ALIGN_CENTER);

            PdfPCell cell_premium_paying_term1 = new PdfPCell(new Paragraph(
                    "Premium Paying Term", small_normal));
            String payingTerm = "";
            if (premFreq.equalsIgnoreCase("single")) {
                payingTerm = "Single";
            } else {
                payingTerm = premium_paying_term + " Years";
            }
            cell_premium_paying_term1.setPadding(5);


            PdfPCell cell_premium_paying_term2 = new PdfPCell(new Paragraph(
                    payingTerm, small_bold));
            cell_premium_paying_term2.setPadding(5);
            cell_premium_paying_term2
                    .setHorizontalAlignment(Element.ALIGN_CENTER);

            PdfPCell cell_benefit_sum_assured1 = new PdfPCell(new Paragraph(
                    "Accident Benefit Sum Assured", small_normal));
            cell_benefit_sum_assured1.setPadding(5);
            PdfPCell cell_benefit_sum_assured2 = new PdfPCell(new Paragraph(
                    "Rs. " + accidental_benefit_sumassured, small_bold));
            cell_benefit_sum_assured2.setPadding(5);
            cell_benefit_sum_assured2
                    .setHorizontalAlignment(Element.ALIGN_CENTER);

            //table_plan_premium_payingTerm.addCell(cell_plan1);
            // table_plan_premium_payingTerm.addCell(cell_plan2);

            //  table_plan_premium_payingTerm.addCell(cell_premium_paying_term1);
            // table_plan_premium_payingTerm.addCell(cell_premium_paying_term2);

            // table_plan_premium_payingTerm.addCell(cell_benefit_sum_assured1);
            //table_plan_premium_payingTerm.addCell(cell_benefit_sum_assured2);

            document.add(table_plan_premium_payingTerm);

            // PdfPTable table_benefit_sumAssured_yearly_premium = new
            // PdfPTable(2);
            // table_benefit_sumAssured_yearly_premium.setWidthPercentage(100);
            //
            // PdfPCell cell_benefit_sum_assured1 = new PdfPCell(new Paragraph(
            // "Accident Benefit Sum Assured", small_normal));
            // cell_benefit_sum_assured1.setPadding(5);
            // PdfPCell cell_benefit_sum_assured2 = new PdfPCell(new Paragraph(
            // "Rs. " + accidental_benefit_sumassured, small_bold));
            // cell_benefit_sum_assured2.setPadding(5);
            // cell_benefit_sum_assured2
            // .setHorizontalAlignment(Element.ALIGN_CENTER);
            //
            // table_benefit_sumAssured_yearly_premium
            // .addCell(cell_benefit_sum_assured1);
            // table_benefit_sumAssured_yearly_premium
            // .addCell(cell_benefit_sum_assured2);
            //
            // document.add(table_benefit_sumAssured_yearly_premium);

        /*    if (!premFreq.equalsIgnoreCase("Single")) {

                PdfPTable Table_Premium = new PdfPTable(4);
                Table_Premium.setWidthPercentage(100);
				String premFreqMod = prsObj.parseXmlTag(input, "premFreqMode");
                PdfPCell cell_Premium1 = new PdfPCell(new Paragraph(
                        "Premium Freq Mode Premium ", small_normal));
                cell_lifeAssuredAge1.setPadding(5);
                PdfPCell cell_Premium2 = new PdfPCell(new Paragraph("  "
                        + premFreqMod, small_bold));
                cell_Premium2.setPadding(5);
                cell_Premium2.setHorizontalAlignment(Element.ALIGN_CENTER);

				PdfPCell cell_Premium3 = new PdfPCell(new Paragraph(
                        "Premium Amount", small_normal));
				cell_lifeAssuredAmaturityAge1.setPadding(5);

				String premiumAmount = prsObj.parseXmlTag(input,
						"premiumAmount");
				PdfPCell cell_Premium4 = new PdfPCell(new Paragraph("Rs. "
						+ premiumAmount, small_bold));
				cell_Premium4.setPadding(5);
				cell_Premium4.setHorizontalAlignment(Element.ALIGN_CENTER);

                // PdfPCell cell_Premium5 = new PdfPCell(new Paragraph(
                // "Premium Paying Term", small_normal));
                // cell_lifeAssuredAmaturityAge1.setPadding(5);
                // PdfPCell cell_Premium6 = new PdfPCell(new Paragraph(
                // premium_paying_term + " Years", small_bold));
                // cell_Premium5.setPadding(5);
                // cell_Premium5.setHorizontalAlignment(Element.ALIGN_CENTER);

                Table_Premium.addCell(cell_Premium1);
                Table_Premium.addCell(cell_Premium2);

				Table_Premium.addCell(cell_Premium3);
				Table_Premium.addCell(cell_Premium4);
				// Table_Premium.addCell(cell_Premium5);
				// Table_Premium.addCell(cell_Premium6);

				document.add(Table_Premium);
            }*/

            PdfPTable BI_PdftableFundDetails2 = new PdfPTable(2);
            BI_PdftableFundDetails2.setWidthPercentage(100);

            PdfPCell BI_PdftableFundDetails_cell23 = new PdfPCell(new Paragraph(
                    "Rate of Applicable Taxes", small_bold));

            PdfPCell BI_PdftableFundDetails_cell2 = new PdfPCell(new Paragraph(
                    "18%", small_bold));

            BI_PdftableFundDetails_cell23
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableFundDetails_cell2
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableFundDetails_cell23.setPadding(5);
            BI_PdftableFundDetails_cell2.setPadding(5);

            BI_PdftableFundDetails2.addCell(BI_PdftableFundDetails_cell23);
            BI_PdftableFundDetails2.addCell(BI_PdftableFundDetails_cell2);
            document.add(para_img_logo_after_space_1);
            document.add(BI_PdftableFundDetails2);


            document.add(para_img_logo_after_space_1);
            PdfPTable BI_PdftableFundDetails = new PdfPTable(2);
            BI_PdftableFundDetails.setWidthPercentage(100);

            PdfPCell BI_PdftableFundDetails_cell = new PdfPCell(new Paragraph(
                    "Plan Option", small_bold));
            PdfPCell BI_PdftableFundStratefy = new PdfPCell(new Paragraph(
                    plan, small_bold));


            BI_PdftableFundDetails_cell
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableFundDetails_cell.setPadding(5);

            BI_PdftableFundDetails.addCell(BI_PdftableFundDetails_cell);
            BI_PdftableFundDetails.addCell(BI_PdftableFundStratefy);
            document.add(BI_PdftableFundDetails);

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

            String bi_smart_elite_Strategy = "Smart Choice Strategy";
            if (bi_smart_elite_Strategy.equalsIgnoreCase("Smart Choice Strategy")) {

                String EquityEliteFundIIFMC = "1.25%";
                if (percentage_equity_elite_fund3.equalsIgnoreCase("")) {
                    percentage_equity_elite_fund3 = "0";

                }

                PdfPTable BI_PdftableEquityEliteFundII = new PdfPTable(4);
                BI_PdftableEquityEliteFundII.setWidthPercentage(100);

                PdfPCell BI_PdftableEquityEliteFundII_cell1 = new PdfPCell(
                        new Paragraph(
                                "Equity Elite Fund II(SFIN : ULIF019100210EQTELI2FND111)",
                                small_normal2));
                PdfPCell BI_PdftableEquityEliteFundII_cell2 = new PdfPCell(
                        new Paragraph(percentage_equity_elite_fund3 + " %",
                                small_normal));

                PdfPCell BI_PdftableEquityEliteFundII_cell3 = new PdfPCell(
                        new Paragraph(EquityEliteFundIIFMC, small_normal));
                PdfPCell BI_PdftableEquityEliteFundII_cell4 = new PdfPCell(
                        new Paragraph("High", small_normal));

                BI_PdftableEquityEliteFundII_cell1
                        .setHorizontalAlignment(Element.ALIGN_CENTER);
                BI_PdftableEquityEliteFundII_cell1.setPadding(5);

                BI_PdftableEquityEliteFundII_cell2
                        .setHorizontalAlignment(Element.ALIGN_CENTER);
                BI_PdftableEquityEliteFundII_cell2.setPadding(5);

                BI_PdftableEquityEliteFundII_cell3
                        .setHorizontalAlignment(Element.ALIGN_CENTER);
                BI_PdftableEquityEliteFundII_cell3.setPadding(5);

                BI_PdftableEquityEliteFundII_cell4
                        .setHorizontalAlignment(Element.ALIGN_CENTER);
                BI_PdftableEquityEliteFundII_cell4.setPadding(5);

                BI_PdftableEquityEliteFundII
                        .addCell(BI_PdftableEquityEliteFundII_cell1);
                BI_PdftableEquityEliteFundII
                        .addCell(BI_PdftableEquityEliteFundII_cell2);
                BI_PdftableEquityEliteFundII
                        .addCell(BI_PdftableEquityEliteFundII_cell3);
                BI_PdftableEquityEliteFundII
                        .addCell(BI_PdftableEquityEliteFundII_cell4);
                document.add(BI_PdftableEquityEliteFundII);

                String BalancedFundFMC = "1.25%";
                if (percentage_balanced_fund2.equalsIgnoreCase("")) {
                    percentage_balanced_fund2 = "0";

                }

                PdfPTable BI_PdftableBalancedFund = new PdfPTable(4);
                BI_PdftableBalancedFund.setWidthPercentage(100);

                PdfPCell BI_PdftableBalancedFund_cell1 = new PdfPCell(
                        new Paragraph(
                                "Balanced Fund(SFIN: ULIF004051205BALANCDFND111)",
                                small_normal2));
                PdfPCell BI_PdftableBalancedFund_cell2 = new PdfPCell(
                        new Paragraph(percentage_balanced_fund2 + " %", small_normal));

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
                BI_PdftableBalancedFund_cell4
                        .setHorizontalAlignment(Element.ALIGN_CENTER);
                BI_PdftableBalancedFund_cell4.setPadding(5);

                BI_PdftableBalancedFund.addCell(BI_PdftableBalancedFund_cell1);
                BI_PdftableBalancedFund.addCell(BI_PdftableBalancedFund_cell2);
                BI_PdftableBalancedFund.addCell(BI_PdftableBalancedFund_cell3);
                BI_PdftableBalancedFund.addCell(BI_PdftableBalancedFund_cell4);
                document.add(BI_PdftableBalancedFund);

                String BondFundFMC = "1.00%";
                if (percentage_bond_fund2.equalsIgnoreCase("")) {
                    percentage_bond_fund2 = "0";

                }

                PdfPTable BI_PdftableBondFund = new PdfPTable(4);
                BI_PdftableBondFund.setWidthPercentage(100);

                PdfPCell BI_PdftableBondFund_cell1 = new PdfPCell(new Paragraph(
                        "Bond Fund(SFIN: ULIF002100105BONDULPFND111)",
                        small_normal2));
                PdfPCell BI_PdftableBondFund_cell2 = new PdfPCell(new Paragraph(
                        percentage_bond_fund2 + " %", small_normal));

                PdfPCell BI_PdftableBondFund_cell3 = new PdfPCell(new Paragraph(
                        BondFundFMC, small_normal));
                PdfPCell BI_PdftableBondFund_cell4 = new PdfPCell(new Paragraph(
                        "Low to Medium", small_normal));

                BI_PdftableBondFund_cell1
                        .setHorizontalAlignment(Element.ALIGN_CENTER);
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

                String MoneyMarketFundFMC = "0.25%";
                if (percentage_money_market_fund2.equalsIgnoreCase("")) {
                    percentage_money_market_fund2 = "0";

                }

                PdfPTable BI_PdftableMoneyMarketFund = new PdfPTable(4);
                BI_PdftableMoneyMarketFund.setWidthPercentage(100);

                PdfPCell BI_PdftableMoneyMarketFund_cell1 = new PdfPCell(
                        new Paragraph(
                                "Money Market Fund(SFIN: ULIF005010206MONYMKTFND111)",
                                small_normal2));
                PdfPCell BI_PdftableMoneyMarketFund_cell2 = new PdfPCell(
                        new Paragraph(percentage_money_market_fund2 + " %",
                                small_normal));

                PdfPCell BI_PdftableMoneyMarketFund_cell3 = new PdfPCell(
                        new Paragraph(MoneyMarketFundFMC, small_normal));
                PdfPCell BI_PdftableMoneyMarketFund_cell4 = new PdfPCell(
                        new Paragraph("Low", small_normal));

                BI_PdftableMoneyMarketFund_cell1
                        .setHorizontalAlignment(Element.ALIGN_CENTER);
                BI_PdftableMoneyMarketFund_cell1.setPadding(5);

                BI_PdftableMoneyMarketFund_cell2
                        .setHorizontalAlignment(Element.ALIGN_CENTER);
                BI_PdftableMoneyMarketFund_cell2.setPadding(5);

                BI_PdftableMoneyMarketFund_cell3
                        .setHorizontalAlignment(Element.ALIGN_CENTER);
                BI_PdftableMoneyMarketFund_cell3.setPadding(5);
                BI_PdftableMoneyMarketFund_cell4
                        .setHorizontalAlignment(Element.ALIGN_CENTER);
                BI_PdftableMoneyMarketFund_cell4.setPadding(5);

                BI_PdftableMoneyMarketFund
                        .addCell(BI_PdftableMoneyMarketFund_cell1);
                BI_PdftableMoneyMarketFund
                        .addCell(BI_PdftableMoneyMarketFund_cell2);
                BI_PdftableMoneyMarketFund
                        .addCell(BI_PdftableMoneyMarketFund_cell3);
                BI_PdftableMoneyMarketFund
                        .addCell(BI_PdftableMoneyMarketFund_cell4);
                document.add(BI_PdftableMoneyMarketFund);


                String bond_optimiser_fundFMC = "1.15%";
                if (percent_BondOptimiserFund2.equalsIgnoreCase("")) {
                    percent_BondOptimiserFund2 = "0";

                }

                PdfPTable BI_Pdftablebond_optimiser_fund = new PdfPTable(4);
                BI_Pdftablebond_optimiser_fund.setWidthPercentage(100);

                PdfPCell BI_Pdftablebond_optimiser_fund_cell1 = new PdfPCell(
                        new Paragraph(
                                "Bond Optimiser Fund  (SFIN : ULIF032290618BONDOPTFND111)",
                                small_normal2));
                PdfPCell BI_Pdftablebond_optimiser_fund_cell2 = new PdfPCell(
                        new Paragraph(percent_BondOptimiserFund2 + " %",
                                small_normal));

                PdfPCell BI_Pdftablebond_optimiser_fund_cell3 = new PdfPCell(
                        new Paragraph(bond_optimiser_fundFMC, small_normal));
                PdfPCell BI_Pdftablebond_optimiser_fund_cell4 = new PdfPCell(
                        new Paragraph("Low to Medium", small_normal));

                BI_Pdftablebond_optimiser_fund_cell1
                        .setHorizontalAlignment(Element.ALIGN_CENTER);
                BI_Pdftablebond_optimiser_fund_cell1.setPadding(5);

                BI_Pdftablebond_optimiser_fund_cell2
                        .setHorizontalAlignment(Element.ALIGN_CENTER);
                BI_Pdftablebond_optimiser_fund_cell2.setPadding(5);

                BI_Pdftablebond_optimiser_fund_cell3
                        .setHorizontalAlignment(Element.ALIGN_CENTER);
                BI_Pdftablebond_optimiser_fund_cell3.setPadding(5);
                BI_Pdftablebond_optimiser_fund_cell4
                        .setHorizontalAlignment(Element.ALIGN_CENTER);
                BI_Pdftablebond_optimiser_fund_cell4.setPadding(5);

                BI_Pdftablebond_optimiser_fund
                        .addCell(BI_Pdftablebond_optimiser_fund_cell1);
                BI_Pdftablebond_optimiser_fund
                        .addCell(BI_Pdftablebond_optimiser_fund_cell2);
                BI_Pdftablebond_optimiser_fund
                        .addCell(BI_Pdftablebond_optimiser_fund_cell3);
                BI_Pdftablebond_optimiser_fund
                        .addCell(BI_Pdftablebond_optimiser_fund_cell4);
                document.add(BI_Pdftablebond_optimiser_fund);


                String bond_midcap_fundFMC = "1.35%";
                if (percent_MidcapFund.equalsIgnoreCase("")) {
                    percent_MidcapFund = "0";

                }

                PdfPTable BI_Pdftablebond_midcap_fund = new PdfPTable(4);
                BI_Pdftablebond_midcap_fund.setWidthPercentage(100);

                PdfPCell BI_Pdftablebond_midcap_fund_cell1 = new PdfPCell(
                        new Paragraph(
                                "Midcap Fund (SFIN : ULIF031290915MIDCAPFUND111)",
                                small_normal2));
                PdfPCell BI_Pdftablebond_midcap_fund_cell2 = new PdfPCell(
                        new Paragraph(percent_MidcapFund + " %",
                                small_normal));

                PdfPCell BI_Pdftablebond_midcap_fund_cell3 = new PdfPCell(
                        new Paragraph(bond_midcap_fundFMC, small_normal));
                PdfPCell BI_Pdftablebond_midcap_fund_cell4 = new PdfPCell(
                        new Paragraph("High", small_normal));

                BI_Pdftablebond_midcap_fund_cell1
                        .setHorizontalAlignment(Element.ALIGN_CENTER);
                BI_Pdftablebond_midcap_fund_cell1.setPadding(5);

                BI_Pdftablebond_midcap_fund_cell2
                        .setHorizontalAlignment(Element.ALIGN_CENTER);
                BI_Pdftablebond_midcap_fund_cell2.setPadding(5);

                BI_Pdftablebond_midcap_fund_cell3
                        .setHorizontalAlignment(Element.ALIGN_CENTER);
                BI_Pdftablebond_midcap_fund_cell3.setPadding(5);
                BI_Pdftablebond_midcap_fund_cell4
                        .setHorizontalAlignment(Element.ALIGN_CENTER);
                BI_Pdftablebond_midcap_fund_cell4.setPadding(5);

                BI_Pdftablebond_midcap_fund
                        .addCell(BI_Pdftablebond_midcap_fund_cell1);
                BI_Pdftablebond_midcap_fund
                        .addCell(BI_Pdftablebond_midcap_fund_cell2);
                BI_Pdftablebond_midcap_fund
                        .addCell(BI_Pdftablebond_midcap_fund_cell3);
                BI_Pdftablebond_midcap_fund
                        .addCell(BI_Pdftablebond_midcap_fund_cell4);
                document.add(BI_Pdftablebond_midcap_fund);


                String bond_pure_fundFMC = "1.35%";
                if (percent_PureFund.equalsIgnoreCase("")) {
                    percent_PureFund = "0";

                }

                PdfPTable BI_Pdftablebond_pure_fund = new PdfPTable(4);
                BI_Pdftablebond_pure_fund.setWidthPercentage(100);

                PdfPCell BI_Pdftablebond_pure_fund_cell1 = new PdfPCell(
                        new Paragraph(
                                "Pure Fund (SFIN : ULIF030290915PUREULPFND111)",
                                small_normal2));
                PdfPCell BI_Pdftablebond_pure_fund_cell2 = new PdfPCell(
                        new Paragraph(percent_PureFund + " %",
                                small_normal));

                PdfPCell BI_Pdftablebond_pure_fund_cell3 = new PdfPCell(
                        new Paragraph(bond_pure_fundFMC, small_normal));
                PdfPCell BI_Pdftablebond_pure_fund_cell4 = new PdfPCell(
                        new Paragraph("High", small_normal));

                BI_Pdftablebond_pure_fund_cell1
                        .setHorizontalAlignment(Element.ALIGN_CENTER);
                BI_Pdftablebond_pure_fund_cell1.setPadding(5);

                BI_Pdftablebond_pure_fund_cell2
                        .setHorizontalAlignment(Element.ALIGN_CENTER);
                BI_Pdftablebond_pure_fund_cell2.setPadding(5);

                BI_Pdftablebond_pure_fund_cell3
                        .setHorizontalAlignment(Element.ALIGN_CENTER);
                BI_Pdftablebond_pure_fund_cell3.setPadding(5);
                BI_Pdftablebond_pure_fund_cell4
                        .setHorizontalAlignment(Element.ALIGN_CENTER);
                BI_Pdftablebond_pure_fund_cell4.setPadding(5);

                BI_Pdftablebond_pure_fund
                        .addCell(BI_Pdftablebond_pure_fund_cell1);
                BI_Pdftablebond_pure_fund
                        .addCell(BI_Pdftablebond_pure_fund_cell2);
                BI_Pdftablebond_pure_fund
                        .addCell(BI_Pdftablebond_pure_fund_cell3);
                BI_Pdftablebond_pure_fund
                        .addCell(BI_Pdftablebond_pure_fund_cell4);
                document.add(BI_Pdftablebond_pure_fund);


                String corporate_fundFMC = "1.15%";
                if (perInvCorporateBondFund.equalsIgnoreCase("")) {
                    perInvCorporateBondFund = "0";

                }

                PdfPTable BI_Pdftablebond_corporate_fund = new PdfPTable(4);
                BI_Pdftablebond_corporate_fund.setWidthPercentage(100);

                PdfPCell BI_Pdftablebond_corporate_fund_cell1 = new PdfPCell(
                        new Paragraph(
                                "Corporate Bond Fund (SFIN : ULIF033290618CORBONDFND111)",
                                small_normal2));
                PdfPCell BI_Pdftablebond_corporate_fund_cell12 = new PdfPCell(
                        new Paragraph(perInvCorporateBondFund + " %",
                                small_normal));

                PdfPCell BI_Pdftablebond_corporate_fund_cell13 = new PdfPCell(
                        new Paragraph(corporate_fundFMC, small_normal));
                PdfPCell BI_Pdftablebond_corporate_fund_cell14 = new PdfPCell(
                        new Paragraph("Low to Medium", small_normal));

                BI_Pdftablebond_corporate_fund_cell1
                        .setHorizontalAlignment(Element.ALIGN_CENTER);
                BI_Pdftablebond_corporate_fund_cell1.setPadding(5);

                BI_Pdftablebond_corporate_fund_cell12
                        .setHorizontalAlignment(Element.ALIGN_CENTER);
                BI_Pdftablebond_corporate_fund_cell12.setPadding(5);

                BI_Pdftablebond_corporate_fund_cell13
                        .setHorizontalAlignment(Element.ALIGN_CENTER);
                BI_Pdftablebond_corporate_fund_cell13.setPadding(5);
                BI_Pdftablebond_corporate_fund_cell14
                        .setHorizontalAlignment(Element.ALIGN_CENTER);
                BI_Pdftablebond_corporate_fund_cell14.setPadding(5);

                BI_Pdftablebond_corporate_fund
                        .addCell(BI_Pdftablebond_corporate_fund_cell1);
                BI_Pdftablebond_corporate_fund
                        .addCell(BI_Pdftablebond_corporate_fund_cell12);
                BI_Pdftablebond_corporate_fund
                        .addCell(BI_Pdftablebond_corporate_fund_cell13);
                BI_Pdftablebond_corporate_fund
                        .addCell(BI_Pdftablebond_corporate_fund_cell14);
                document.add(BI_Pdftablebond_corporate_fund);


                String equity_fundFMC = "1.35%";
                if (percent_EquityFund.equalsIgnoreCase("")) {
                    percent_EquityFund = "0";

                }


            /*    PdfPTable BI_Pdftableequity_fund = new PdfPTable(3);
                BI_Pdftableequity_fund.setWidthPercentage(100);

                PdfPCell BI_Pdftableequity_fund_cell1 = new PdfPCell(
                        new Paragraph(
                                "Equity Fund (SFIN : ULIF001100105EQUITY-FND111)",
                                small_normal2));
                PdfPCell BI_Pdftableequity_fund_cell2 = new PdfPCell(
                        new Paragraph(percent_EquityFund + " %",
                                small_normal));

                PdfPCell BI_Pdftableequity_fund_cell3 = new PdfPCell(
                        new Paragraph(equity_fundFMC, small_normal));

                BI_Pdftableequity_fund_cell1
                        .setHorizontalAlignment(Element.ALIGN_CENTER);
                BI_Pdftableequity_fund_cell1.setPadding(5);

                BI_Pdftableequity_fund_cell2
                        .setHorizontalAlignment(Element.ALIGN_CENTER);
                BI_Pdftableequity_fund_cell2.setPadding(5);

                BI_Pdftableequity_fund_cell3
                        .setHorizontalAlignment(Element.ALIGN_CENTER);
                BI_Pdftableequity_fund_cell3.setPadding(5);

                BI_Pdftableequity_fund
                        .addCell(BI_Pdftableequity_fund_cell1);
                BI_Pdftableequity_fund
                        .addCell(BI_Pdftableequity_fund_cell2);
                BI_Pdftableequity_fund
                        .addCell(BI_Pdftableequity_fund_cell3);
                document.add(BI_Pdftableequity_fund);*/

            } else if (bi_smart_elite_Strategy.equalsIgnoreCase("Life Stage Based Strategy")) {
                String equity_fundFMC = "1.35%";
                if (percent_EquityFund.equalsIgnoreCase("")) {
                    percent_EquityFund = "0";

                }

                PdfPTable BI_Pdftableequity_fund = new PdfPTable(4);
                BI_Pdftableequity_fund.setWidthPercentage(100);

                PdfPCell BI_Pdftableequity_fund_cell1 = new PdfPCell(
                        new Paragraph(
                                "Equity Fund (SFIN : ULIF001100105EQUITY-FND111)",
                                small_normal2));
               /* PdfPCell BI_Pdftableequity_fund_cell2 = new PdfPCell(
                        new Paragraph(percent_EquityFund + " %",
                                small_normal));*/
                PdfPCell BI_Pdftableequity_fund_cell2 = new PdfPCell(
                        new Paragraph("100% of Fund Value will be",
                                small_normal));


                PdfPCell BI_Pdftableequity_fund_cell3 = new PdfPCell(
                        new Paragraph(equity_fundFMC, small_normal));
                PdfPCell BI_Pdftableequity_fund_cell4 = new PdfPCell(
                        new Paragraph("High", small_normal));

                BI_Pdftableequity_fund_cell1
                        .setHorizontalAlignment(Element.ALIGN_CENTER);
                BI_Pdftableequity_fund_cell1.setPadding(5);

                BI_Pdftableequity_fund_cell2
                        .setHorizontalAlignment(Element.ALIGN_CENTER);
                BI_Pdftableequity_fund_cell2.setPadding(5);

                BI_Pdftableequity_fund_cell3
                        .setHorizontalAlignment(Element.ALIGN_CENTER);
                BI_Pdftableequity_fund_cell3.setPadding(5);
                BI_Pdftableequity_fund_cell4
                        .setHorizontalAlignment(Element.ALIGN_CENTER);
                BI_Pdftableequity_fund_cell4.setPadding(5);

                BI_Pdftableequity_fund
                        .addCell(BI_Pdftableequity_fund_cell1);
                BI_Pdftableequity_fund
                        .addCell(BI_Pdftableequity_fund_cell2);
                BI_Pdftableequity_fund
                        .addCell(BI_Pdftableequity_fund_cell3);
                BI_Pdftableequity_fund
                        .addCell(BI_Pdftableequity_fund_cell4);
                document.add(BI_Pdftableequity_fund);

                String BondFundFMC = "1.35%";
                if (percentage_bond_fund2.equalsIgnoreCase("")) {
                    percentage_bond_fund2 = "0";

                }

                PdfPTable BI_PdftableBondFund = new PdfPTable(4);
                BI_PdftableBondFund.setWidthPercentage(100);

                PdfPCell BI_PdftableBondFund_cell1 = new PdfPCell(new Paragraph(
                        "Bond Fund II (SFIN : ULIF034160919BONDULFND2111)",
                        small_normal2));
               /* PdfPCell BI_PdftableBondFund_cell2 = new PdfPCell(new Paragraph(
                        percentage_bond_fund2 + " %", small_normal));*/
                PdfPCell BI_PdftableBondFund_cell2 = new PdfPCell(new Paragraph(
                        "distributed among these funds based on the age of life assured and as per term to maturity", small_normal));

                PdfPCell BI_PdftableBondFund_cell3 = new PdfPCell(new Paragraph(
                        BondFundFMC, small_normal));
                PdfPCell BI_PdftableBondFund_cell4 = new PdfPCell(new Paragraph(
                        "Low to Medium", small_normal));

                BI_PdftableBondFund_cell1
                        .setHorizontalAlignment(Element.ALIGN_CENTER);
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


            } else if (bi_smart_elite_Strategy.equalsIgnoreCase("Trigger Fund Strategy")) {
                String equity_fundFMC = "1.35%";
                if (percent_EquityFund.equalsIgnoreCase("")) {
                    percent_EquityFund = "0";

                }

                PdfPTable BI_Pdftableequity_fund = new PdfPTable(4);
                BI_Pdftableequity_fund.setWidthPercentage(100);

                PdfPCell BI_Pdftableequity_fund_cell1 = new PdfPCell(
                        new Paragraph(
                                "Equity Fund (SFIN : ULIF001100105EQUITY-FND111)",
                                small_normal2));
               /* PdfPCell BI_Pdftableequity_fund_cell2 = new PdfPCell(
                        new Paragraph(percent_EquityFund + " %",
                                small_normal));*/
                PdfPCell BI_Pdftableequity_fund_cell2 = new PdfPCell(
                        new Paragraph("80%",
                                small_normal));


                PdfPCell BI_Pdftableequity_fund_cell3 = new PdfPCell(
                        new Paragraph(equity_fundFMC, small_normal));
                PdfPCell BI_Pdftableequity_fund_cell4 = new PdfPCell(
                        new Paragraph("High", small_normal));

                BI_Pdftableequity_fund_cell1
                        .setHorizontalAlignment(Element.ALIGN_CENTER);
                BI_Pdftableequity_fund_cell1.setPadding(5);

                BI_Pdftableequity_fund_cell2
                        .setHorizontalAlignment(Element.ALIGN_CENTER);
                BI_Pdftableequity_fund_cell2.setPadding(5);

                BI_Pdftableequity_fund_cell3
                        .setHorizontalAlignment(Element.ALIGN_CENTER);
                BI_Pdftableequity_fund_cell3.setPadding(5);
                BI_Pdftableequity_fund_cell4
                        .setHorizontalAlignment(Element.ALIGN_CENTER);
                BI_Pdftableequity_fund_cell4.setPadding(5);

                BI_Pdftableequity_fund
                        .addCell(BI_Pdftableequity_fund_cell1);
                BI_Pdftableequity_fund
                        .addCell(BI_Pdftableequity_fund_cell2);
                BI_Pdftableequity_fund
                        .addCell(BI_Pdftableequity_fund_cell3);
                BI_Pdftableequity_fund
                        .addCell(BI_Pdftableequity_fund_cell4);
                document.add(BI_Pdftableequity_fund);

                String BondFundFMC = "1.35%";
                if (percentage_bond_fund2.equalsIgnoreCase("")) {
                    percentage_bond_fund2 = "0";

                }

                PdfPTable BI_PdftableBondFund = new PdfPTable(4);
                BI_PdftableBondFund.setWidthPercentage(100);

                PdfPCell BI_PdftableBondFund_cell1 = new PdfPCell(new Paragraph(
                        "Bond Fund II (SFIN : ULIF034160919BONDULFND2111)",
                        small_normal2));
               /* PdfPCell BI_PdftableBondFund_cell2 = new PdfPCell(new Paragraph(
                        percentage_bond_fund2 + " %", small_normal));*/
                PdfPCell BI_PdftableBondFund_cell2 = new PdfPCell(new Paragraph(
                        "20%", small_normal));

                PdfPCell BI_PdftableBondFund_cell3 = new PdfPCell(new Paragraph(
                        BondFundFMC, small_normal));
                PdfPCell BI_PdftableBondFund_cell4 = new PdfPCell(new Paragraph(
                        "Low to Medium", small_normal));

                BI_PdftableBondFund_cell1
                        .setHorizontalAlignment(Element.ALIGN_CENTER);
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


            }
            /*else if (bi_smart_elite_Strategy.equalsIgnoreCase("Trigger Fund Strategy")) {
                String equity_fundFMC = "1.35%";
                if (percent_EquityFund.equalsIgnoreCase("")) {
                    percent_EquityFund = "0";

                }

                PdfPTable BI_Pdftableequity_fund = new PdfPTable(3);
                BI_Pdftableequity_fund.setWidthPercentage(100);

                PdfPCell BI_Pdftableequity_fund_cell1 = new PdfPCell(
                        new Paragraph(
                                "Equity Fund (SFIN : ULIF001100105EQUITY-FND111)",
                                small_normal2));
                PdfPCell BI_Pdftableequity_fund_cell2 = new PdfPCell(
                        new Paragraph(percent_EquityFund + " %",
                                small_normal));

                PdfPCell BI_Pdftableequity_fund_cell3 = new PdfPCell(
                        new Paragraph(equity_fundFMC, small_normal));

                BI_Pdftableequity_fund_cell1
                        .setHorizontalAlignment(Element.ALIGN_CENTER);
                BI_Pdftableequity_fund_cell1.setPadding(5);

                BI_Pdftableequity_fund_cell2
                        .setHorizontalAlignment(Element.ALIGN_CENTER);
                BI_Pdftableequity_fund_cell2.setPadding(5);

                BI_Pdftableequity_fund_cell3
                        .setHorizontalAlignment(Element.ALIGN_CENTER);
                BI_Pdftableequity_fund_cell3.setPadding(5);

                BI_Pdftableequity_fund
                        .addCell(BI_Pdftableequity_fund_cell1);
                BI_Pdftableequity_fund
                        .addCell(BI_Pdftableequity_fund_cell2);
                BI_Pdftableequity_fund
                        .addCell(BI_Pdftableequity_fund_cell3);
                document.add(BI_Pdftableequity_fund);

                String BondFundFMC = "1.00%";
                if (percentage_bond_fund2.equalsIgnoreCase("")) {
                    percentage_bond_fund2 = "0";

                }

                PdfPTable BI_PdftableBondFund = new PdfPTable(3);
                BI_PdftableBondFund.setWidthPercentage(100);

                PdfPCell BI_PdftableBondFund_cell1 = new PdfPCell(new Paragraph(
                        "Bond Fund(SFIN : ULIF034160919BONDULFND2111)",
                        small_normal2));
                PdfPCell BI_PdftableBondFund_cell2 = new PdfPCell(new Paragraph(
                        percentage_bond_fund2 + " %", small_normal));

                PdfPCell BI_PdftableBondFund_cell3 = new PdfPCell(new Paragraph(
                        BondFundFMC, small_normal));

                BI_PdftableBondFund_cell1
                        .setHorizontalAlignment(Element.ALIGN_CENTER);
                BI_PdftableBondFund_cell1.setPadding(5);

                BI_PdftableBondFund_cell2
                        .setHorizontalAlignment(Element.ALIGN_CENTER);
                BI_PdftableBondFund_cell2.setPadding(5);

                BI_PdftableBondFund_cell3
                        .setHorizontalAlignment(Element.ALIGN_CENTER);
                BI_PdftableBondFund_cell3.setPadding(5);

                BI_PdftableBondFund.addCell(BI_PdftableBondFund_cell1);
                BI_PdftableBondFund.addCell(BI_PdftableBondFund_cell2);
                BI_PdftableBondFund.addCell(BI_PdftableBondFund_cell3);
                document.add(BI_PdftableBondFund);


            }*/
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
                            "The actual returns can vary depending on the performance of the chosen strategy, charges towards mortality, underwriting extra etc. The investment risk in this policy is borne by the policyholder, hence, for more details on terms and conditions please read the sales literature carefully.",

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
            PdfPTable BI_PdftableNoofyearselapsedsinceinception = new PdfPTable(
                    4);
            BI_PdftableNoofyearselapsedsinceinception.setWidthPercentage(100);

            PdfPCell BI_PdftableBI_PdftableNoofyearselapsedsinceinception_cell1 = new PdfPCell(
                    new Paragraph("No. of years elapsed since inception",
                            small_normal));
            PdfPCell BI_PdftableBI_PdftableNoofyearselapsedsinceinception_cell2 = new PdfPCell(
                    new Paragraph(no_of_year_elapsed + " Years", small_bold));

            PdfPCell BI_PdftableBI_PdftableNoofyearselapsedsinceinception_cell3 = new PdfPCell(
                    new Paragraph("Reduction in Yield @ 8%", small_normal));

            PdfPCell BI_PdftableBI_PdftableNoofyearselapsedsinceinception_cell4 = new PdfPCell(
                    new Paragraph(red_in_year_at_8_no_of_year + " %",
                            small_bold));

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
                    new Paragraph(policy_term + " Years", small_bold));

            PdfPCell BI_PdftableBI_PdftableMaturityAt_cell3 = new PdfPCell(
                    new Paragraph("Reduction in Yield @ 8%", small_normal));

            PdfPCell BI_PdftableBI_PdftableMaturityAt_cell4 = new PdfPCell(
                    new Paragraph(red_in_year_at_8_maturity_age + " %",
                            small_bold));

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
            // document.add(BI_PdftableMaturityAt);
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
                                    + "    have received the information with respect to the above and have understood the above statement before entering into the contract.",
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
                    new Paragraph(agentdeclaration, small_bold));

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
                    new Paragraph("Net Yield " + netYield8 + "%", small_bold2));


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

            PdfPCell BI_Pdftable_output_cell78_1 = new PdfPCell(new Paragraph(
                    "Add. ADB & ATPD Charges", small_bold2));

            /*PdfPCell BI_Pdftable_output_cell781 = new PdfPCell(new Paragraph(
                    "Guarantee charge", small_bold2));*/
            PdfPCell BI_Pdftable_output_cell88 = new PdfPCell(new Paragraph(
                    "Other charges*", small_bold2));
            PdfPCell BI_Pdftable_output_cell881 = new PdfPCell(new Paragraph(
                    "Additions to the fund*", small_bold2));

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
            BI_Pdftableoutput8.addCell(BI_Pdftable_output_cell78_1);
            // BI_Pdftableoutput8.addCell(BI_Pdftable_output_cell781);
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

            for (int i = 0; i < Integer.parseInt(policy_term); i++) {

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

                PdfPCell BI_Pdftable_output_row1_cell58_1 = new PdfPCell(
                        new Paragraph(list_data1.get(i)
                                .getATPDChrg(), small_bold2));


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
                BI_Pdftableoutput_row18.addCell(BI_Pdftable_output_row1_cell58_1);
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
                    new Paragraph("Net Yield -  " + netYield4 + " %", small_bold2));

            PdfPCell BI_PdftableBI_PdftableNetYield_cell3 = new PdfPCell(
                    new Paragraph("Reduction in yield -  " + red_in_year_at_8_maturity_age + " %",
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

            PdfPCell BI_Pdftable_output_cell7814_1 = new PdfPCell(new Paragraph(
                    "Add. ADB & ATPD Charges", small_bold2));

          /*  PdfPCell BI_Pdftable_output_cell7811 = new PdfPCell(new Paragraph(
                    "Guarantee charge", small_bold2));*/
            PdfPCell BI_Pdftable_output_cell8814 = new PdfPCell(new Paragraph(
                    "Other charges*", small_bold2));
            PdfPCell BI_Pdftable_output_cell8811 = new PdfPCell(new Paragraph(
                    "Additions to the fund*", small_bold2));

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
            BI_Pdftableoutput4.addCell(BI_Pdftable_output_cell7814_1);
            //BI_Pdftableoutput4.addCell(BI_Pdftable_output_cell7811);
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

            for (int i = 0; i < Integer.parseInt(policy_term); i++) {

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

                PdfPCell BI_Pdftable_output_row1_cell581_1 = new PdfPCell(
                        new Paragraph(list_data2.get(i)
                                .getATPDChrg(), small_bold2));

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
                BI_Pdftableoutput_row184.addCell(BI_Pdftable_output_row1_cell581_1);
                BI_Pdftableoutput_row184.addCell(BI_Pdftable_output_row1_cell781);
                BI_Pdftableoutput_row184.addCell(BI_Pdftable_output_row1_cell8811);

                BI_Pdftableoutput_row184.addCell(BI_Pdftable_output_row1_cell981);

                BI_Pdftableoutput_row184.addCell(BI_Pdftable_output_row1_cell1081);
                BI_Pdftableoutput_row184.addCell(BI_Pdftable_output_row1_cell1281);

                BI_Pdftableoutput_row184.addCell(BI_Pdftable_output_row1_cell1381);
                BI_Pdftableoutput_row184.addCell(BI_Pdftable_output_row1_cell1481);

                document.add(BI_Pdftableoutput_row184);

            }





























/*
			PdfPTable BI_PdftableOutputHeader = new PdfPTable(4);

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
					"Premium*", small_bold2));

			PdfPCell BI_Pdftable_output_cell3 = new PdfPCell(new Paragraph(
					"Premium Allocation Charge", small_bold2));

			PdfPCell BI_Pdftable_output_cell4 = new PdfPCell(new Paragraph(
					"Amount available for investment", small_bold2));
			PdfPCell BI_Pdftable_output_cell5 = new PdfPCell(new Paragraph(
					"Policy Administration Charge", small_bold2));

			PdfPCell BI_Pdftable_output_cell6 = new PdfPCell(new Paragraph(
					"Mortality And Morbidity Charges", small_bold2));

			PdfPCell BI_Pdftable_output_cell7 = new PdfPCell(new Paragraph(
					"Total Charges", small_bold2));

			PdfPCell BI_Pdftable_output_cell8 = new PdfPCell(new Paragraph(
					"Total Applicable taxes (if any) on Charges", small_bold2));

			PdfPCell BI_Pdftable_output_cell9 = new PdfPCell(new Paragraph(
					"Addition to fund If Any", small_bold2));
			PdfPCell BI_Pdftable_output_cell10 = new PdfPCell(new Paragraph(
					"Fund Management Charge", small_bold2));

			PdfPCell BI_Pdftable_output_cell11 = new PdfPCell(new Paragraph(
					"Guaranteed Addition", small_bold2));

			PdfPCell BI_Pdftable_output_cell12 = new PdfPCell(new Paragraph(
					"Fund Value at end", small_bold2));
			PdfPCell BI_Pdftable_output_cell13 = new PdfPCell(new Paragraph(
					"Surrender Value", small_bold2));

			PdfPCell BI_Pdftable_output_cell14 = new PdfPCell(new Paragraph(
					"Death Benefit", small_bold2));

			PdfPCell BI_Pdftable_output_cell15 = new PdfPCell(new Paragraph(
					"Mortality And Morbidity Charges", small_bold2));
			PdfPCell BI_Pdftable_output_cell16 = new PdfPCell(new Paragraph(
					"Total Charges", small_bold2));

			PdfPCell BI_Pdftable_output_cell17 = new PdfPCell(new Paragraph(
					"Total Applicable taxes (if any) on Charges", small_bold2));

			PdfPCell BI_Pdftable_output_cell18 = new PdfPCell(new Paragraph(
					"Addition to Fund  If Any", small_bold2));

			PdfPCell BI_Pdftable_output_cell19 = new PdfPCell(new Paragraph(
					"Fund Management Charge", small_bold2));

			PdfPCell BI_Pdftable_output_cell20 = new PdfPCell(new Paragraph(
					"Guaranteed Addition", small_bold2));
			PdfPCell BI_Pdftable_output_cell21 = new PdfPCell(new Paragraph(
					"Fund Value at end", small_bold2));

			PdfPCell BI_Pdftable_output_cell22 = new PdfPCell(new Paragraph(
					"Surrender Value", small_bold2));
			PdfPCell BI_Pdftable_output_cell23 = new PdfPCell(new Paragraph(
					"Dealth Benefit", small_bold2));

			PdfPCell BI_Pdftable_output_cell24 = new PdfPCell(new Paragraph(
					"Commission Payable", small_bold2));

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

			for (int i = 0; i < Integer.parseInt(policy_term); i++) {

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
								.getAmount_available_for_investment(),
								small_bold2));
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
						new Paragraph(list_data.get(i).getTotal_service_tax1(),
								small_bold2));

				PdfPCell BI_Pdftable_output_row1_cell9 = new PdfPCell(
						new Paragraph(list_data.get(i).getAddition_to_fund1(),
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
						new Paragraph(list_data.get(i).getTotal_service_tax2(),
								small_bold2));

				PdfPCell BI_Pdftable_output_row1_cell18 = new PdfPCell(
						new Paragraph(list_data.get(i).getAddition_to_fund2(),
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
			}
            */














  /*          PdfPTable BI_PdftableNetYield = new PdfPTable(4);

			BI_PdftableNetYield.setWidthPercentage(100);
			float[] columnWidthsNetYield = { 5f, 9f, 9f, 1f };
			BI_PdftableNetYield.setWidths(columnWidthsNetYield);
			PdfPCell BI_PdftableBI_PdftableNetYield_cell1 = new PdfPCell(
					new Paragraph("", small_normal));
			PdfPCell BI_PdftableBI_PdftableNetYield_cell2 = new PdfPCell(
					new Paragraph("Net Yield   " + netYield4 + " %",
							small_bold2));

			PdfPCell BI_PdftableBI_PdftableNetYield_cell3 = new PdfPCell(
					new Paragraph("Net Yield   " + netYield8 + " %",
							small_bold2));

			PdfPCell BI_PdftableBI_PdftableNetYield_cell4 = new PdfPCell(
					new Paragraph("", small_bold));

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

			document.add(BI_PdftableNetYield);
*/
            PdfPTable BI_Pdftable_base_premium = new PdfPTable(1);
            BI_Pdftable_base_premium.setWidthPercentage(100);
            PdfPCell BI_Pdftable_base_premium_cell1 = new PdfPCell(
                    new Paragraph("*It is a base premium", small_bold2));

            BI_Pdftable_base_premium_cell1.setPadding(5);

            BI_Pdftable_base_premium.addCell(BI_Pdftable_base_premium_cell1);
            //document.add(BI_Pdftable_base_premium);

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
                            "2)  Please read this benefit illustration in conjunction with Sales Brochure and the Policy Document to understand all Terms, Conditions & Exclusions carefully.",
                            small_normal));

            BI_Pdftable7_cell1.setPadding(5);

            BI_Pdftable7.addCell(BI_Pdftable7_cell1);
            document.add(BI_Pdftable7);

            PdfPTable BI_Pdftable8 = new PdfPTable(1);
            BI_Pdftable8.setWidthPercentage(100);
            PdfPCell BI_Pdftable8_cell1 = new PdfPCell(
                    new Paragraph(
                            "3)Kindly note that above is only an illustration and does not in any way create any rights and/or obligations. The actual experience on the contract may be different from what is illustrated. The non-guaranteed low and high rate mentioned above relate to assumed investment returns at different rates and may vary depending upon market conditions. For more details on risk factors, terms and conditions please read sales brochure carefully.",

                            small_normal));

            BI_Pdftable8_cell1.setPadding(5);

            BI_Pdftable8.addCell(BI_Pdftable8_cell1);
            document.add(BI_Pdftable8);

            PdfPTable BI_Pdftable9 = new PdfPTable(1);
            BI_Pdftable9.setWidthPercentage(100);
            PdfPCell BI_Pdftable9_cell1 = new PdfPCell(
                    new Paragraph(
                            "4)  The unit values may go up as well as down and past performance is no indication of future performance on the part of SBI Life Insurance Co. Ltd. We would request you to appreciate the associated risk under this plan vis-à-vis the likely future returns before taking your investment decision.",
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
                            "6)Fund management charge is based on the specific  investment strategy / fund option(s) chosen.",
                            small_normal));

            BI_Pdftable11_cell1.setPadding(5);

            BI_Pdftable11.addCell(BI_Pdftable11_cell1);
            document.add(BI_Pdftable11);

            PdfPTable BI_Pdftable12 = new PdfPTable(1);
            BI_Pdftable12.setWidthPercentage(100);
            PdfPCell BI_Pdftable12_cell1 = new PdfPCell(
                    new Paragraph(
                            "7)  Surrender Value equals the Fund Value at the end of the year minus Discontinuance Charges. Surrender value is available on or after 5th policy anniversary.",
                            small_normal));

            BI_Pdftable12_cell1.setPadding(5);

            BI_Pdftable12.addCell(BI_Pdftable12_cell1);
            document.add(BI_Pdftable12);

            PdfPTable BI_Pdftable13 = new PdfPTable(1);
            BI_Pdftable13.setWidthPercentage(100);
            PdfPCell BI_Pdftable13_cell1 = new PdfPCell(
                    new Paragraph(
                            "8)  Acceptance of proposal is subject to Underwriting decision. Mortality charges are for a healthy person.",
                            small_normal));

            BI_Pdftable13_cell1.setPadding(5);

            BI_Pdftable13.addCell(BI_Pdftable13_cell1);
            document.add(BI_Pdftable13);

            PdfPTable BI_Pdftable149 = new PdfPTable(1);
            BI_Pdftable149.setWidthPercentage(100);
            PdfPCell BI_Pdftable149_cell1 = new PdfPCell(
                    new Paragraph(
                            "9)  Applicable Taxes (including surcharge/cess etc), at the rate notified by the Central Government/ State Government / Union Territories of India from time to time and as per the provisions of the prevalent tax laws will be payable on premium/ or any other charges as per the product features.",

                            small_normal));

            BI_Pdftable149_cell1.setPadding(5);

            BI_Pdftable149.addCell(BI_Pdftable149_cell1);
            document.add(BI_Pdftable149);

            PdfPTable BI_Pdftable14 = new PdfPTable(1);
            BI_Pdftable14.setWidthPercentage(100);
            PdfPCell BI_Pdftable14_cell1 = new PdfPCell(new Paragraph(
                    "10)	This policy provides guaranteed death benefit of Rs.  "
                            + sum_assured, small_normal));

            BI_Pdftable14_cell1.setPadding(5);

            BI_Pdftable14.addCell(BI_Pdftable14_cell1);
            document.add(BI_Pdftable14);

            PdfPTable BI_Pdftable16 = new PdfPTable(1);
            BI_Pdftable16.setWidthPercentage(100);
            PdfPCell BI_Pdftable16_cell1 = new PdfPCell(
                    new Paragraph(
                            "11) Net Yield have been calculated after applying all the charges (except GST, mortality charges).",

                            small_normal));

            BI_Pdftable16_cell1.setPadding(5);

            BI_Pdftable16.addCell(BI_Pdftable16_cell1);
            document.add(BI_Pdftable16);

            PdfPTable BI_Pdftable17 = new PdfPTable(1);
            BI_Pdftable17.setWidthPercentage(100);
            PdfPCell BI_Pdftable17_cell1 = new PdfPCell(
                    new Paragraph(
                            "12) In case rider charges are collected explicitly through collection of rider premium, and not by way of cancellation of units, then, such charges are not considered in this illustration. In other cases, rider charges are included in other charges.",
                            small_normal));

            BI_Pdftable17_cell1.setPadding(5);

            //BI_Pdftable17.addCell(BI_Pdftable17_cell1);
            //document.add(BI_Pdftable17);

            PdfPTable BI_Pdftable18 = new PdfPTable(1);
            BI_Pdftable18.setWidthPercentage(100);
            PdfPCell BI_Pdftable18_cell1 = new PdfPCell(
                    new Paragraph(
                            "12)Col (24) gives the commission payable to the agent/ broker in respect of the base policy .  This amount is included in total charges mentioned in col (7) or col (16).",

                            small_normal));

            BI_Pdftable18_cell1.setPadding(5);

            //BI_Pdftable18.addCell(BI_Pdftable18_cell1);
            //document.add(BI_Pdftable18);

            PdfPTable BI_Pdftable_foraccidental_death = new PdfPTable(1);
            BI_Pdftable_foraccidental_death.setWidthPercentage(100);
            PdfPCell BI_Pdftableforaccidental_death = new PdfPCell(
                    new Paragraph(
                            "13) In case of accidental death or accidental total and permanent disability, whichever is earlier, this policy provides additional guaranteed benefit of Rs. "
                                    + accidental_benefit_sumassured
                                    + " In case of accidental death Rs. "
                                    + accidental_benefit_sumassured
                                    + " will be paid as lump sum where as in case of accidental total and permenant disability Rs. "
                                    + accidental_benefit_sumassured
                                    + " would be paid in 10 annual installments of Rs. "
                                    + obj.getRound(obj.getStringWithout_E(Double
                                    .valueOf(AnnualInstallment
                                            .equals("") ? "0"
                                            : AnnualInstallment))),
                            small_normal));

            BI_Pdftableforaccidental_death.setPadding(5);

            BI_Pdftable_foraccidental_death
                    .addCell(BI_Pdftableforaccidental_death);
            //document.add(BI_Pdftable_foraccidental_death);

            document.add(para_img_logo_after_space_1);
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
            //
            PdfPTable BI_Pdftable24 = new PdfPTable(1);
            BI_Pdftable24.setWidthPercentage(100);
            PdfPCell BI_Pdftable24_cell1 = new PdfPCell(
                    new Paragraph(
                            "Accident Benefit charges a charge of a fixed sum based on the sum assured chosen, which is applied at the beginning of each policy month by canceling units for equivalent amount.",
                            small_normal));

            BI_Pdftable24_cell1.setPadding(5);

            BI_Pdftable24.addCell(BI_Pdftable24_cell1);
            //document.add(BI_Pdftable24);

            document.add(para_img_logo_after_space_1);

            PdfPTable BI_Pdftable191 = new PdfPTable(1);
            BI_Pdftable191.setWidthPercentage(100);
            PdfPCell BI_Pdftable191_cell1 = new PdfPCell(new Paragraph(
                    "Important", small_bold));
            BI_Pdftable191_cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
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
            document.add(BI_Pdftable_CompanysPolicySurrender4);

            PdfPTable BI_Pdftable_CompanysPolicySurrender5 = new PdfPTable(1);
            BI_Pdftable_CompanysPolicySurrender5.setWidthPercentage(100);
            PdfPCell BI_Pdftable_CompanysPolicySurrender5_cell = new PdfPCell(
                    new Paragraph(Company_policy_surrender_dec, small_normal));

            BI_Pdftable_CompanysPolicySurrender5_cell.setPadding(5);

            BI_Pdftable_CompanysPolicySurrender5
                    .addCell(BI_Pdftable_CompanysPolicySurrender5_cell);
            document.add(BI_Pdftable_CompanysPolicySurrender5);
            //document.add(BI_Pdftable_CompanysPolicySurrender4);

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

    private void Date() {
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        c.add(Calendar.DATE, 30);
    }

    public void onClickLADob(View v) {
        if (!na_dob.equals("") && flag1 == 0) {
            System.out.println(" na_dob : " + na_dob);
            initialiseDateParameter(na_dob, 35);
            DIALOG_ID = 5;
            updateDisplay(DIALOG_ID);
            flag1 = 1;
        } else {
            initialiseDateParameter(lifeAssured_date_of_birth, 35);
            DIALOG_ID = 5;
            showDialog(DATE_DIALOG_ID);
        }
    }

    public void onClickDob(View v) {
        // setDefaultDate();
        DIALOG_ID = 4;
        showDialog(DATE_DIALOG_ID);
    }

    private void setDefaultDate(int id) {
        Calendar present_date = Calendar.getInstance();
        present_date.add(Calendar.YEAR, -id);
        mDay = present_date.get(Calendar.DAY_OF_MONTH);
        mMonth = present_date.get(Calendar.MONTH);
        mYear = present_date.get(Calendar.YEAR);
    }

    //
    // public void onBackPressed() {
    // // TODO Auto-generated method stub
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

    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        // TODO Auto-generated method stub
        if (v.getId() == edt_bi_smart_elite_life_assured_first_name.getId()) {
            // clearFocusable(edt_bi_smart_elite_life_assured_first_name);
            setFocusable(edt_bi_smart_elite_life_assured_middle_name);
            edt_bi_smart_elite_life_assured_middle_name.requestFocus();
        } else if (v.getId() == edt_bi_smart_elite_life_assured_middle_name
                .getId()) {
            // clearFocusable(edt_bi_smart_elite_life_assured_middle_name);
            setFocusable(edt_bi_smart_elite_life_assured_last_name);
            edt_bi_smart_elite_life_assured_last_name.requestFocus();
        } else if (v.getId() == edt_bi_smart_elite_life_assured_last_name
                .getId()) {

            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(
                    edt_bi_smart_elite_life_assured_last_name.getWindowToken(),
                    0);
            // clearFocusable(edt_bi_smart_elite_life_assured_last_name);
            setFocusable(btn_bi_smart_elite_life_assured_date);
            btn_bi_smart_elite_life_assured_date.requestFocus();
        } else if (v.getId() == edt_bi_smart_elite_proposer_first_name.getId()) {
            // clearFocusable(edt_bi_smart_elite_proposer_first_name);
            setFocusable(edt_bi_smart_elite_proposer_middle_name);
            edt_bi_smart_elite_proposer_middle_name.requestFocus();
        } else if (v.getId() == edt_bi_smart_elite_proposer_middle_name.getId()) {
            // clearFocusable(edt_bi_smart_elite_proposer_middle_name);
            setFocusable(edt_bi_smart_elite_proposer_last_name);
            edt_bi_smart_elite_proposer_last_name.requestFocus();
        } else if (v.getId() == edt_bi_smart_elite_proposer_last_name.getId()) {

            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(
                    edt_bi_smart_elite_proposer_last_name.getWindowToken(), 0);
            // clearFocusable(edt_bi_smart_elite_proposer_last_name);
            setFocusable(btn_bi_smart_elite_proposer_date);
            btn_bi_smart_elite_proposer_date.requestFocus();
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
            clearFocusable(selPolicyTerm);
            setFocusable(selPolicyTerm);
            selPolicyTerm.requestFocus();
        } else if (v.getId() == premiumAmt.getId()) {

            // clearFocusable(premiumAmt);
            setFocusable(SAMF);
            SAMF.requestFocus();
        } else if (v.getId() == SAMF.getId()) {

            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(SAMF.getWindowToken(), 0);
            // clearFocusable(SAMF);
            setFocusable(selPlan);
            selPlan.requestFocus();
        } else if (v.getId() == noOfYearsElapsedSinceInception.getId()) {
            // clearFocusable(noOfYearsElapsedSinceInception);
            setFocusable(percent_EquityEliteIIFund);
            percent_EquityEliteIIFund.requestFocus();
        } else if (v.getId() == percent_EquityEliteIIFund.getId()) {
            // clearFocusable(percent_EquityEliteIIFund);
            setFocusable(percent_BalancedFund);
            percent_BalancedFund.requestFocus();
        } else if (v.getId() == percent_BalancedFund.getId()) {
            // clearFocusable(percent_BalancedFund);
            setFocusable(percent_BondFund);
            percent_BondFund.requestFocus();
        } else if (v.getId() == percent_BondFund.getId()) {
            // clearFocusable(percent_MoneyMarketFund);
            setFocusable(Percent_PureFund);
            Percent_PureFund.requestFocus();
        } else if (v.getId() == Percent_PureFund.getId()) {
            // clearFocusable(percent_MoneyMarketFund);
            setFocusable(Percent_MidcapFund);
            Percent_MidcapFund.requestFocus();

        } else if (v.getId() == Percent_MidcapFund.getId()) {
            // clearFocusable(percent_MoneyMarketFund);
            setFocusable(Percent_BondOptimiserFund);
            Percent_BondOptimiserFund.requestFocus();

        } else if (v.getId() == Percent_BondOptimiserFund.getId()) {
            setFocusable(percent_MoneyMarketFund);
            percent_MoneyMarketFund.requestFocus();

        } else if (v.getId() == percent_MoneyMarketFund.getId()) {
            // clearFocusable(percent_MoneyMarketFund);
            setFocusable(percent_CorporateBondFund);
            percent_CorporateBondFund.requestFocus();

        } else if (v.getId() == percent_CorporateBondFund.getId()) {
            // clearFocusable(percent_MoneyMarketFund);
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(
                    percent_CorporateBondFund.getWindowToken(), 0);

        }

        return true;
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        b = 1;

        boolean flagFocus = true;

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
