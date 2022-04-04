package sbilife.com.pointofsale_bancaagency.retiresmart;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
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
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
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
public class BI_RetireSmartActivity extends AppCompatActivity implements
        OnEditorActionListener, OnItemClickListener {

    private final int SIGNATURE_ACTIVITY = 1;
    private final int DATE_DIALOG_ID = 1;
    private NeedAnalysisBIService NABIObj;
    private NA_CBI_bean na_cbi_bean;
    private File needAnalysispath;

    String premPayingTerm = "", premPayingMode = "",
            ageAtEntry = "", proposerageAtEntry = "";

    String premium = "";
    private File newFile;
    private String agentcode;
    private String agentMobile;
    private String agentEmail;
    private String userType;
    private int needAnalysis_flag = 0;
    private String na_input = null;
    private String na_output = null;
    private DatabaseHelper dbHelper;
    private String na_dob = "";
    private int flag = 0;
    private CheckBox isStaffDisc;
    private CheckBox selViewSFIN;
    private Spinner spnrageInYears;
    private Spinner spnrGender;
    private Spinner spnrPolicyTerm;
    private Spinner spnrPremiumFrequencyMode;
    private Spinner spnrPremiumPayingOption;
    private Spinner spnrPremiumPayingTerm;
    private EditText edtpremiumAmount;
    private EditText edtnoOfYearsElapsedSinceInception;
    private Button btnSubmit;
    private Button back;
    private TextView help_policyTerm;
    private TextView help_premiumAmt;
    private TextView help_noOfYearsElapsedSinceInception;
    private TextView help_premPayingTerm;


    // EditText edt_edt_premiumAmount;
    TableRow tbEquityPensionFund, tbEquityOptimiserPensionFund, tbGrowthPensionFund, tbBondPensionFund3, tbMoneyMarketPensionFund3;

    // for BI
    private StringBuilder inputVal;
    private String policyTermStr = "";
    private RadioButton rb_proposerdetail_personaldetail_backdating_yes;
    private Button btn_proposerdetail_personaldetail_backdatingdate;
    private Button btn_MarketingOfficalDate;
    private Button btn_PolicyholderDate;
    private ImageButton Ibtn_signatureofMarketing;
    private ImageButton Ibtn_signatureofPolicyHolders;
    private Dialog d;
    private String latestImage = "";
    // List used for The Policy Detail Depend on The policy Term
    private List<M_BI_RetireSmartAdapterCommon> list_data;
    private List<M_BI_RetireSmartAdapterCommon2> list_data1;
    private List<M_BI_RetireSmartAdapterCommon2> list_data2;
    private int DIALOG_ID;
    private int mYear;
    private int mMonth;
    private int mDay;
    private Button btn_bi_retire_smart_life_assured_date;
    private String QuatationNumber = "";
    private String planName = "";
    private String sumAssured = "";

    private String lifeAssured_Title = "";
    private String lifeAssured_First_Name = "";
    private String lifeAssured_Middle_Name = "";
    private String lifeAssured_Last_Name = "";
    private String name_of_life_assured = "";
    private String lifeAssured_date_of_birth = "";

    private String proposer_Backdating_WishToBackDate_Policy = "";
    private String proposer_Backdating_BackDate = "";

    // Spinner USed
    private Spinner spnr_bi_retire_smart_life_assured_title;

    // edit Text Used
    private EditText edt_bi_retire_smart_life_assured_first_name;
    private EditText edt_bi_retire_smart_life_assured_middle_name;
    private EditText edt_bi_retire_smart_life_assured_last_name;

    // Spinner USed


    // Retrieving value from database and storing
    private String output = "";
    String input = "";

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

    // Variable Declaration
    private DecimalFormat currencyFormat;
    private AlertDialog.Builder showAlert;

    private StringBuilder bussIll = null;
    private StringBuilder retVal = null;

    private File mypath;

    /* String For BI Grid */
    private String gender = "";
    private String age_entry = "";
    private String premium_paying_term = "";

    private String no_of_year_elapsed;
    private String premFreq = "";

    private RetireSmartBean retireSmartBean;
    private ScrollView sv_bi_retire_smart_main;

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

    private CommonForAllProd obj;
    private String PremPayingOption = "";
    private String premiumAmount = "";

    private String bankUserType = "", mode = "";

    String str_kerla_discount = "No";

    /* parivatran changes */
    private String Check = "";
    private Context context;
    private CommonMethods commonMethods;
    private StorageUtils mStorageUtils;
    private ImageButton imageButtonRetireSmartProposerPhotograph;

    private LinearLayout linearlayoutThirdpartySignature,
            linearlayoutAppointeeSignature;
    private ImageButton Ibtn_signatureofThirdParty, Ibtn_signatureofAppointee;

    Spinner spnr_bi_plan_option_choice, spnr_bi_Matuirty_option, spnr_bi_Matuirty_frequency;
    private String thirdPartySign = "", appointeeSign = "";
    private String product_Code, product_UIN, product_cateogory, product_type, Company_policy_surrender_dec = "";
    EditText equity_pension_fund, equity_optimiser_pension_fund, growth_pension_fund, bond_pension_fund3, money_market_pension_fund3;

    String bi_retire_smart_plan_option = "Advantage Plan";

    String percent_equity_pension_fund = "", percent_equity_optimiser_pension_fund = "", percent_growth_pension_fund = "",
            percent_bond_pension_fund3 = "", percent_money_market_pension_fund3 = "";

    String bi_retire_smart_Matuirty_frequency = "", bi_retire_smart_Matuirty_option;
    LinearLayout ll_fundDetails;
    TableRow tr_prem_freq_mode,
            tr_prem_paying_term;

    /* end */
    private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
        String percent_equity_pension_fund = "", percent_equity_optimiser_pension_fund = "", percent_growth_pension_fund = "",
                percent_bond_pension_fund3 = "", percent_money_market_pension_fund3 = "";

        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;
            updateDisplay(DIALOG_ID);
        }
    };

    private CheckBox cb_kerladisc;

    String nonGuaranVestingBenefit_4Percent = "",
            nonGuaranVestingBenefit_8Percent = "",
            guarnVestngBen = "",
            annuityAmount_MiniBen = "", annuityPayout_4_Pr = "", annuityPayout_8_Pr = "";

    String Annuity_rate_dec = "";

    @Override
    public void onCreate(Bundle savedInstatnceState) {
        super.onCreate(savedInstatnceState);

        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.bi_retiresmartmain);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);

        /* parivartan changes */
        context = this;
        commonMethods = new CommonMethods();
        mStorageUtils = new StorageUtils();
        /* end */


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
                // ProductHomePageActivity.path.setText("Benefit Illustrator");

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
                    planName = "Retire Smart";
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
                        product_Code/* "1H" */, agentcode, zero + "");

            }
        } else
            needAnalysis_flag = 0;

        // prsObj = new ParseXML();
        retireSmartBean = new RetireSmartBean();
        retVal = new StringBuilder();
        list_data = new ArrayList<M_BI_RetireSmartAdapterCommon>();
        list_data1 = new ArrayList<M_BI_RetireSmartAdapterCommon2>();
        list_data2 = new ArrayList<M_BI_RetireSmartAdapterCommon2>();
        // db = new newDBHelper(this);
        InitalizeVariable();
        initialiseDate();
        // ProductHomePageActivity.path.setText("Benefit Illustrator");
        obj = new CommonForAllProd();
        currencyFormat = new DecimalFormat("##,##,##,###");
        showAlert = new AlertDialog.Builder(this);

        String[] ageList = new String[41];
        for (int i = 30; i <= 70; i++) {
            ageList[i - 30] = "" + i;
        }
        ArrayAdapter<String> ageAdapter = new ArrayAdapter<>(
                getApplicationContext(), R.layout.spinner_item, ageList);
        ageAdapter.setDropDownViewResource(R.layout.spinner_item1);
        spnrageInYears.setAdapter(ageAdapter);
        ageAdapter.notifyDataSetChanged();

        ArrayAdapter<String> genderAdapter = new ArrayAdapter<>(
                getApplicationContext(), R.layout.spinner_item,
                getResources().getStringArray(R.array.gender_all_arrays));
        genderAdapter.setDropDownViewResource(R.layout.spinner_item1);
        spnrGender.setAdapter(genderAdapter);
        genderAdapter.notifyDataSetChanged();

        String[] policyTermList = new String[26];
        //policyTermList[0] = "" + 10;
        for (int i = 10; i <= 35; i++) {
            policyTermList[i - 10] = "" + i;
        }
        ArrayAdapter<String> policyTermAdapter = new ArrayAdapter<>(
                getApplicationContext(), R.layout.spinner_item, policyTermList);
        policyTermAdapter.setDropDownViewResource(R.layout.spinner_item1);
        spnrPolicyTerm.setAdapter(policyTermAdapter);
        policyTermAdapter.notifyDataSetChanged();

        String[] premFreqModeList = new String[]{"Monthly", "Quarterly",
                "Half Yearly", "Yearly"};
        ArrayAdapter<String> premFreqModeAdapter = new ArrayAdapter<>(
                getApplicationContext(), R.layout.spinner_item,
                premFreqModeList);
        premFreqModeAdapter.setDropDownViewResource(R.layout.spinner_item1);
        spnrPremiumFrequencyMode.setAdapter(premFreqModeAdapter);
        premFreqModeAdapter.notifyDataSetChanged();
        //String[] premPayingOptList = new String[]{"Regular", "LPPT"};
        String[] premPayingOptList = new String[]{"Regular", "LPPT", "Single"};
        ArrayAdapter<String> premPayingOptAdapter = new ArrayAdapter<String>(
                getApplicationContext(), R.layout.spinner_item,
                premPayingOptList);
        premPayingOptAdapter.setDropDownViewResource(R.layout.spinner_item1);
        spnrPremiumPayingOption.setAdapter(premPayingOptAdapter);
        premPayingOptAdapter.notifyDataSetChanged();

        String[] premPayingTermList = new String[31];
        for (int i = 5; i <= 35; i++) {
            premPayingTermList[i - 5] = "" + i;
        }
        ArrayAdapter<String> premPayingTermAdapter = new ArrayAdapter<>(
                getApplicationContext(), R.layout.spinner_item,
                premPayingTermList);
        premPayingTermAdapter.setDropDownViewResource(R.layout.spinner_item1);
        spnrPremiumPayingTerm.setAdapter(premPayingTermAdapter);
        premPayingTermAdapter.notifyDataSetChanged();

        commonMethods.fillSpinnerValue(context, spnr_bi_retire_smart_life_assured_title,
                Arrays.asList(getResources().getStringArray(R.array.arr_select_title)));

        List<String> PlanOption_list = new ArrayList<String>();
        //PlanOption_list.add("Select Plan Option");
        PlanOption_list.add("Advantage Plan");
        PlanOption_list.add("Smart Choice Plan");

        List<String> MaturityOption_list = new ArrayList<String>();
        MaturityOption_list.add("Select Maturity Option");
        MaturityOption_list.add("Option 1.1 Lifetime Income");
        MaturityOption_list.add("Option 1.2 Lifetime Income with Capital Refund");
        MaturityOption_list.add("Option 1.3 Lifetime Income with Capital refund in parts");
        MaturityOption_list.add("Option 1.4 Lifetime Income with Balance Capital Refund");
        MaturityOption_list.add("Option 1.5 Lifetime income with Annual Increase of 3%");
        MaturityOption_list.add("Option 1.6 Lifetime income with Annual Increase of 5%");
        MaturityOption_list.add("Option 1.7 Lifetime income with certain period of 5 years");
        MaturityOption_list.add("Option 1.8 Lifetime income with certain period of 10 years");
        MaturityOption_list.add("Option 1.9 Lifetime income with certain period of 15 years");
        MaturityOption_list.add("Option 1.10 Lifetime income with certain period of 20 years");
        MaturityOption_list.add("Option 2.1 Life and Last  Survivor - 50% Income");
        MaturityOption_list.add("Option 2.2 Life and Last  Survivor - 100% Income");
        MaturityOption_list.add("Option 2.3 Life and Last  Survivor - 50% Income with Capital Refund");
        MaturityOption_list.add("Option 2.4 Life and Last  Survivor - 100% Income with Capital Refund");

        ArrayAdapter<String> AnnuityOptionAdapter = new ArrayAdapter<String>(
                getApplicationContext(), R.layout.spinner_item, MaturityOption_list);

        AnnuityOptionAdapter.setDropDownViewResource(R.layout.spinner_item);
        spnr_bi_Matuirty_option.setAdapter(AnnuityOptionAdapter);
        AnnuityOptionAdapter.notifyDataSetChanged();


        List<String> MaturityFrequency_list = new ArrayList<String>();
        MaturityFrequency_list.add("Select Maturity Frequency");
        MaturityFrequency_list.add("Yearly");
        MaturityFrequency_list.add("Half Yearly");
        MaturityFrequency_list.add("Quarterly");
        MaturityFrequency_list.add("Monthly");


//        fillSpinnerValue(spnr_bi_retire_smart_life_assured_title, title_list);
        commonMethods.fillSpinnerValue(context, spnr_bi_plan_option_choice, PlanOption_list);
        commonMethods.fillSpinnerValue(context, spnr_bi_Matuirty_frequency, MaturityFrequency_list);

        setSpinnerAndOterListner();
        // setBIInputGui();
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

        edt_bi_retire_smart_life_assured_first_name
                .setOnEditorActionListener(this);
        edt_bi_retire_smart_life_assured_middle_name
                .setOnEditorActionListener(this);
        edt_bi_retire_smart_life_assured_last_name
                .setOnEditorActionListener(this);
        edt_proposerdetail_basicdetail_contact_no
                .setOnEditorActionListener(this);
        edt_proposerdetail_basicdetail_Email_id.setOnEditorActionListener(this);
        edt_proposerdetail_basicdetail_ConfirmEmail_id
                .setOnEditorActionListener(this);
        edtnoOfYearsElapsedSinceInception.setOnEditorActionListener(this);
        edtpremiumAmount.setOnEditorActionListener(this);

        setFocusable(spnr_bi_retire_smart_life_assured_title);
        spnr_bi_retire_smart_life_assured_title.requestFocus();

        // getBasicDetail();
        edtnoOfYearsElapsedSinceInception.setText("0");

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

        TableRow tr_staff_disc = findViewById(R.id.tr_retire_smart_staff_disc);

        String str_usertype = "";
        try {
            str_usertype = SimpleCrypto.decrypt("SBIL",
                    dbHelper.GetUserType());

        } catch (Exception e) {
            e.printStackTrace();
        }
       /* if (str_usertype.equalsIgnoreCase("BAP")
                || str_usertype.equalsIgnoreCase("CAG")) {
            tr_staff_disc.setVisibility(View.GONE);
        }*/
        if (needAnalysis_flag == 1 && !TextUtils.isEmpty(gender)) {
            spnrGender.setSelection(genderAdapter.getPosition(gender));
            onClickLADob(btn_bi_retire_smart_life_assured_date);
        }

        bi_retire_smart_plan_option = "Advantage Plan";
        spnr_bi_plan_option_choice.setSelection(getIndex(spnr_bi_plan_option_choice, "Advantage Plan"), false);

    }

    private void setSpinnerAndOterListner() {
        isStaffDisc.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if ((isStaffDisc.isChecked())) {
                    isStaffDisc.setChecked(true);
                }
                clearFocusable(isStaffDisc);
                clearFocusable(spnr_bi_retire_smart_life_assured_title);
                setFocusable(spnr_bi_retire_smart_life_assured_title);
                spnr_bi_retire_smart_life_assured_title.requestFocus();
            }
        });

        cb_kerladisc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cb_kerladisc.isChecked()) {
                    str_kerla_discount = "Yes";
                    clearFocusable(cb_kerladisc);
                    setFocusable(spnr_bi_retire_smart_life_assured_title);
                    spnr_bi_retire_smart_life_assured_title.requestFocus();
                } else {
                    str_kerla_discount = "No";
                    clearFocusable(cb_kerladisc);
                    setFocusable(spnr_bi_retire_smart_life_assured_title);
                    spnr_bi_retire_smart_life_assured_title.requestFocus();
                }
            }
        });

        spnrageInYears.setOnItemSelectedListener(new OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                updatePolicyTerm();
            }

            public void onNothingSelected(AdapterView<?> arg0) {
            }

        });

        spnrPremiumPayingOption
                .setOnItemSelectedListener(new OnItemSelectedListener() {

                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int arg2, long arg3) {
                        updatePremiumPayingTerm();
                        updatePremiumAmount();

                        if (arg2 == 2) {
                            tr_prem_freq_mode.setVisibility(View.GONE);
//                    tr_prem_freq_mode_spnr.setVisibility(View.GONE);
                            tr_prem_paying_term.setVisibility(View.GONE);
//                    tr_prem_paying_term_spnr.setVisibility(View.GONE);

                        } else {
                            tr_prem_freq_mode.setVisibility(View.VISIBLE);
//                    tr_prem_freq_mode_spnr.setVisibility(View.VISIBLE);
                            tr_prem_paying_term.setVisibility(View.VISIBLE);
//                    tr_prem_paying_term_spnr.setVisibility(View.VISIBLE);
                        }

                        if (edt_bi_retire_smart_life_assured_first_name
                                .getText().toString().equals("")) {
                            // clearFocusable(edtpremiumAmount);
                            clearFocusable(spnrPremiumPayingOption);
                            setFocusable(spnr_bi_retire_smart_life_assured_title);
                            spnr_bi_retire_smart_life_assured_title
                                    .requestFocus();
                        } else {
                            // clearFocusable(edtpremiumAmount);
                            clearFocusable(spnrPremiumPayingOption);
                            setFocusable(edtnoOfYearsElapsedSinceInception);
                            edtnoOfYearsElapsedSinceInception.requestFocus();
                        }
                    }

                    public void onNothingSelected(AdapterView<?> arg0) {
                    }
                });

        spnrPremiumFrequencyMode
                .setOnItemSelectedListener(new OnItemSelectedListener() {

                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int arg2, long arg3) {
                        updatePremiumAmount();

                        clearFocusable(spnrPremiumFrequencyMode);
                        setFocusable(edtpremiumAmount);
                        edtpremiumAmount.requestFocus();

                    }

                    public void onNothingSelected(AdapterView<?> arg0) {
                    }
                });

        spnrPolicyTerm.setOnItemSelectedListener(new OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                // TODO Auto-generated method stub
                updatePremiumPayingTerm();
                updatenoOfYearsElapsedSinceInception();

                edtnoOfYearsElapsedSinceInception.setText(spnrPolicyTerm
                        .getSelectedItem().toString());
                clearFocusable(spnrPolicyTerm);
                setFocusable(spnrPremiumFrequencyMode);
                spnrPremiumFrequencyMode.requestFocus();
            }

            public void onNothingSelected(AdapterView<?> arg0) {
            }

        });

        back.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                setResult(RESULT_OK);
                finish();
            }
        });

        spnr_bi_plan_option_choice.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int position, long id) {
                if (position > 0) {

                    bi_retire_smart_plan_option = spnr_bi_plan_option_choice.getSelectedItem().toString();
                    if (spnr_bi_plan_option_choice.getSelectedItem().toString().equalsIgnoreCase("Smart Choice Plan")) {
                        ll_fundDetails.setVisibility(View.VISIBLE);
                    } else {
                        ll_fundDetails.setVisibility(View.GONE);
                    }

                  /*  equity_pension_fund.setText("");
                    equity_optimiser_pension_fund.setText("");
                    growth_pension_fund.setText("");
                    bond_pension_fund3.setText("");
                    money_market_pension_fund3.setText("");*/

                } else {
                    if (position == 0) {
                        ll_fundDetails.setVisibility(View.GONE);
                    }
                    // bi_retire_smart_plan_option = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });
        spnr_bi_Matuirty_frequency.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int position, long id) {
                if (position > 0) {
                    bi_retire_smart_Matuirty_frequency = spnr_bi_Matuirty_frequency.getSelectedItem().toString();


                } else {
                    bi_retire_smart_Matuirty_frequency = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });

        spnr_bi_Matuirty_option.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int position, long id) {
                if (position > 0) {
                    bi_retire_smart_Matuirty_option = spnr_bi_Matuirty_option.getSelectedItem().toString();


                } else {
                    bi_retire_smart_Matuirty_option = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });

        selViewSFIN
                .setOnCheckedChangeListener(new OnCheckedChangeListener() {

                    public void onCheckedChanged(CompoundButton buttonView,
                                                 boolean isChecked) {
                        // TODO Auto-generated method stub

                        if (isChecked) {
                            tbEquityPensionFund.setVisibility(View.VISIBLE);
                            tbEquityOptimiserPensionFund.setVisibility(View.VISIBLE);
                            tbGrowthPensionFund.setVisibility(View.VISIBLE);
                            tbBondPensionFund3.setVisibility(View.VISIBLE);
                            tbMoneyMarketPensionFund3.setVisibility(View.VISIBLE);
                        } else {
                            tbEquityPensionFund.setVisibility(View.GONE);
                            tbEquityOptimiserPensionFund.setVisibility(View.GONE);
                            tbGrowthPensionFund.setVisibility(View.GONE);
                            tbBondPensionFund3.setVisibility(View.GONE);
                            tbMoneyMarketPensionFund3.setVisibility(View.GONE);
                        }
                    }
                });

        spnr_bi_retire_smart_life_assured_title
                .setOnItemSelectedListener(new OnItemSelectedListener() {

                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int position, long id) {
                        // TODO Auto-generated method stub
                        if (position == 0) {
                            lifeAssured_Title = "";
                        } else {
                            lifeAssured_Title = spnr_bi_retire_smart_life_assured_title
                                    .getSelectedItem().toString();
                            /*if (lifeAssured_Title.equalsIgnoreCase("Mr.")) {
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
                            }*/
                            clearFocusable(spnr_bi_retire_smart_life_assured_title);
                            setFocusable(edt_bi_retire_smart_life_assured_first_name);
                            edt_bi_retire_smart_life_assured_first_name
                                    .requestFocus();
                        }

                    }

                    public void onNothingSelected(AdapterView<?> arg0) {
                        // TODO Auto-generated method stub

                    }
                });

        // Calculate premium
        btnSubmit.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {

                inputVal = new StringBuilder();
                retVal = new StringBuilder();
                bussIll = new StringBuilder();

                gender = spnrGender.getSelectedItem().toString();

                lifeAssured_First_Name = edt_bi_retire_smart_life_assured_first_name
                        .getText().toString();
                lifeAssured_Middle_Name = edt_bi_retire_smart_life_assured_middle_name
                        .getText().toString();
                lifeAssured_Last_Name = edt_bi_retire_smart_life_assured_last_name
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

                if (valLifeAssuredProposerDetail() && valDob()
                        && valBasicDetail() && valPremiumAmount()
                        && valPolicyTerm() && valPremiumPayingTerm()
                        && valChoiceOption() && valTotalAllocation() && valMatuirtyFreuqency() && valMatuirtyOption()) {
                    // calculate effective premium

                    Date();

                    addListenerOnSubmit();
                    getInput(retireSmartBean);
                    // insertDataIntoDatabase();
                    if (needAnalysis_flag == 0) {
                        Intent i = new Intent(BI_RetireSmartActivity.this,
                                Success.class);
                        i.putExtra("ProductName",
                                "Product : SBI Life - Retire Smart (UIN : 111L094V02)");
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
                        i.putExtra("header", "SBI Life - Retire Smart");
                        i.putExtra("header1", "(UIN : 111L094V02)");
                        startActivity(i);

                    } else
                        Dialog();

                }
            }

        });

    }

    private void Dialog() {

        final Dialog d = new Dialog(this);
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);
        d.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.BLACK));

        d.setContentView(R.layout.layout_retire_smart_bi_grid);

        final TextView tv_proposername = d
                .findViewById(R.id.tv_proposername);
        final TextView tr_annuity_rate_dec = (TextView) d
                .findViewById(R.id.tr_annuity_rate_dec);


        final TextView tv_retire_smart_distribution_channel = (TextView) d
                .findViewById(R.id.tv_retire_smart_distribution_channel);
        tv_retire_smart_distribution_channel.setText(userType);

        final TextView tv_annuity_rate = d
                .findViewById(R.id.tv_annuity_rate);
        final TextView tv_proposal_number = d
                .findViewById(R.id.tv_proposal_number);
        tv_proposal_number.setText(QuatationNumber);

        TextView tv_bi_retire_smart_age_entry = d
                .findViewById(R.id.tv_bi_retire_smart_age_entry);
        TextView tv_bi_retire_smart_maturity_age = d
                .findViewById(R.id.tv_bi_retire_smart_maturity_age);

        TextView tv_bi_retire_smart_life_assured_gender = d
                .findViewById(R.id.tv_bi_retire_smart_life_assured_gender);
        TextView tv_bi_retire_smart_policy_term = d
                .findViewById(R.id.tv_bi_retire_smart_policy_term);
        TextView tv_bi_retire_smart_premium_payment_option = d
                .findViewById(R.id.tv_bi_retire_smart_premium_payment_option);

        TextView tv_bi_retire_smart_total_first_year_premium = d
                .findViewById(R.id.tv_bi_retire_smart_total_first_year_premium);

        TextView tv_bi_retire_smart_annualised_premium = d
                .findViewById(R.id.tv_bi_retire_smart_annualised_premium);

        TextView tv_bi_retire_smart_payment_mode = d
                .findViewById(R.id.tv_bi_retire_smart_payment_mode);

        TextView tv_bi_retire_smart_premium_paying_term = d
                .findViewById(R.id.tv_bi_retire_smart_premium_paying_term);

        TextView tv_retire_smart_no_of_years_elapsed = d
                .findViewById(R.id.tv_retire_smart_no_of_years_elapsed);

        TextView tv_retire_smart_reduction_yield = d
                .findViewById(R.id.tv_retire_smart_reduction_yield);

        TextView tv_retire_smart_maturity_age2 = d
                .findViewById(R.id.tv_retire_smart_maturity_age2);
        TextView tv_retire_smart_reduction_yeild2 = d
                .findViewById(R.id.tv_retire_smart_reduction_yeild2);

        TextView tv_bi_retire_smart_name_of_proposer = (TextView) d.findViewById(R.id.tv_bi_retire_smart_name_of_proposer);
        tv_bi_retire_smart_name_of_proposer.setText(name_of_life_assured);


        TextView tv_bi_retire_smart_name_of_life_assured = (TextView) d.findViewById(R.id.tv_bi_retire_smart_name_of_life_assured);
        tv_bi_retire_smart_name_of_life_assured.setText(name_of_life_assured);

        TextView tv_bi_is_Staff = (TextView) d
                .findViewById(R.id.tv_bi_is_Staff);

        LinearLayout ll_funddetails = (LinearLayout) d.findViewById(R.id.ll_funddetails);

        TextView tv_smart_elite_equity_elite_fund3_allocation = (TextView) d
                .findViewById(R.id.tv_smart_elite_equity_elite_fund3_allocation);
        TextView tv_smart_elite_balanced_fund2_allocation = (TextView) d
                .findViewById(R.id.tv_smart_elite_balanced_fund2_allocation);
        TextView tv_smart_elite_bond_fund2_allocation = (TextView) d
                .findViewById(R.id.tv_smart_elite_bond_fund2_allocation);
        TextView tv_smart_elite_market_fund2_allocation = (TextView) d
                .findViewById(R.id.tv_smart_elite_market_fund2_allocation);
        TextView tv_smart_elite_bond_optimiser_fund2_allocation = (TextView) d
                .findViewById(R.id.tv_smart_elite_bond_optimiser_fund2_allocation);

        if (bi_retire_smart_plan_option.equals("Select Plan Option") || bi_retire_smart_plan_option.equals("Advantage Plan")) {
            ll_funddetails.setVisibility(View.GONE);
        } else if (bi_retire_smart_plan_option.equals("Smart Choice Plan")) {
            ll_funddetails.setVisibility(View.VISIBLE);
        }

// Illustrative benefit on vesting
        TextView tv_illustrative_benefit_on_vesting_policy_year = (TextView) d
                .findViewById(R.id.tv_illustrative_benefit_on_vesting_policy_year);
        TextView tv_illustrative_benefit_on_vesting_age = (TextView) d
                .findViewById(R.id.tv_illustrative_benefit_on_vesting_age);
        TextView tv_illustrative_benefit_on_vesting_tbpp_during_the_year = (TextView) d
                .findViewById(R.id.tv_illustrative_benefit_on_vesting_tbpp_during_the_year);
        TextView tv_illustrative_benefit_on_vesting_tbpp_cumulative = (TextView) d
                .findViewById(R.id.tv_illustrative_benefit_on_vesting_tbpp_cumulative);
        TextView tv_illustrative_benefit_on_vesting_guaranteed_vesting_benefit = (TextView) d
                .findViewById(R.id.tv_illustrative_benefit_on_vesting_guaranteed_vesting_benefit);
        TextView tv_illustrative_benefit_on_vesting_bonuses_nonguaranteed_benefit_4pa_1 = (TextView) d
                .findViewById(R.id.tv_illustrative_benefit_on_vesting_bonuses_nonguaranteed_benefit_4pa_1);


        TextView tv_grid_annuity_option = (TextView) d
                .findViewById(R.id.tv_grid_annuity_option);

     /*   TextView tv_bi_retire_smart_annuity_payable_rates_at_4 = d
                .findViewById(R.id.tv_bi_retire_smart_annuity_payable_rates_at_4);

        TextView tv_bi_retire_smart_annuity_payable_rates_at_8 = d
                .findViewById(R.id.tv_bi_retire_smart_annuity_payable_rates_at_8);
*/
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

        /* parivaratan changes */
        imageButtonRetireSmartProposerPhotograph = d
                .findViewById(R.id.imageButtonRetireSmartProposerPhotograph);
        imageButtonRetireSmartProposerPhotograph
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Check = "Photo";
                        commonMethods.windowmessage(context, "_cust1Photo.jpg");
                    }
                });
        /* end */

        String flg_needAnalyis = "0";
        if (NeedAnalysisActivity.str_need_analysis != null) {
            if (NeedAnalysisActivity.str_need_analysis.equals("1")) {
                flg_needAnalyis = "1";
                tr_need_analysis.setVisibility(View.VISIBLE);
            } else {
                cb_statement_need_analysis.setChecked(true);
                tr_need_analysis.setVisibility(View.GONE);
            }

        }
        TextView tv_bi_retire_smart_annuity_payable_at_8 = d
                .findViewById(R.id.tv_bi_retire_smart_annuity_payable_at_8);

      /*  TextView tv_bi_retire_smart_annuity_payable_at_4 = d
                .findViewById(R.id.tv_bi_retire_smart_annuity_payable_at_4);*/

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
                            + ", have undergone the Need Analysis  after having reviewed the SBI Life Product options, I have opted for SBI LIFE - Retire Smart.");

            // tv_life_assured_name.setText(name_of_life_assured);
            tv_proposername.setText(name_of_life_assured);
        } else if (proposer_Is_Same_As_Life_Assured.equalsIgnoreCase("n")) {
            edt_proposer_name
                    .setText("I, "
                            + name_of_proposer
                            + ", having received the information with respect to the above, have understood the above statement before entering into the contract.");
            edt_proposer_name_need_analysis
                    .setText("I, "
                            + name_of_proposer
                            + ", have undergone the Need Analysis  after having reviewed the SBI Life Product options, I have opted for SBI LIFE - Retire Smart.");

            tv_proposername.setText(name_of_proposer);
        }

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

        GridView gv_userinfo = d.findViewById(R.id.gv_userinfo);
        GridView gv_userinfo2 = (GridView) d.findViewById(R.id.gv_userinfo2);
        GridView gv_userinfo3 = (GridView) d.findViewById(R.id.gv_userinfo3);
        Ibtn_signatureofMarketing
                .setOnClickListener(new View.OnClickListener() {

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
                .setOnClickListener(new View.OnClickListener() {

                    public void onClick(View view) {
                        if (cb_statement.isChecked()
                                && cb_statement_need_analysis.isChecked()) {
                            latestImage = "proposer";
                            // windowmessagesgin();
                            // windowmessageProposersgin();
                            commonMethods.windowmessageProposersgin(context,
                                    NeedAnalysisActivity.URN_NO + "_cust1sign");
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
                .setOnClickListener(new View.OnClickListener() {

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

                            // String thirdyPartySignName =
                            // NeedAnalysisActivity.URN_NO +"_thirdParty.png";

                            String appointeeSignName = NeedAnalysisActivity.URN_NO
                                    + "_appointee.png";

                            /*
                             * String extStorageDirectory = Environment
                             * .getExternalStorageDirectory().toString(); String
                             * direct = "/SBI-Smart Advisor";
                             */

                            // File thirdyPartySignFile = new File(folder,
                            // thirdyPartySignName);

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

        Ibtn_signatureofAppointee
                .setOnClickListener(new View.OnClickListener() {

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
                            imageButtonRetireSmartProposerPhotograph
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

        if (mode.equalsIgnoreCase("Manual")) {
            radioButtonTrasactionModeManual.setChecked(true);
        } else if (mode.equalsIgnoreCase("Parivartan")) {
            radioButtonTrasactionModeParivartan.setChecked(true);
        }

        if (!TextUtils.isEmpty(place2)) {
            edt_Policyholderplace.setText(place2);
        }

        btn_proceed.setOnClickListener(new View.OnClickListener() {

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


                    if (radioButtonTrasactionModeParivartan.isChecked()) {
                        mode = "Parivartan";
                    } else if (radioButtonTrasactionModeManual.isChecked()) {
                        mode = "Manual";
                    }

                    // String isActive = "0";
                    String productCode;

                    if (PremPayingOption.equals("Regular"))
                        productCode = "SRSRP0";
                    else
                        productCode = "SRSLP0";

                    /* parivartan changes */
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
                                    .valueOf((sumAssured.equals("") || sumAssured == null) ? "0"
                                            : sumAssured))), premiumAmount,
                            emailId, mobileNo, agentEmail, agentMobile,
                            na_input, na_output, premFreq, Integer
                            .parseInt(policyTermStr), 0, productCode,
                            getDate(lifeAssured_date_of_birth), "", inputVal
                            .toString(), retVal.toString().replace(
                            bussIll.toString(), ""));

                    name_of_person = name_of_life_assured;
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
                            obj.getRound(obj.getStringWithout_E(Double
                                    .valueOf((sumAssured.equals("") || sumAssured == null) ? "0"
                                            : sumAssured))), premiumAmount,
                            agentEmail, agentMobile, na_input, na_output,
                            premFreq, Integer.parseInt(policyTermStr), 0,
                            productCode, getDate(lifeAssured_date_of_birth),
                            "", "", mode, inputVal
                            .toString(), retVal.toString().replace(
                            bussIll.toString(), "")));
                    /* end */

                    createPdf();
                    NABIObj.serviceHit(BI_RetireSmartActivity.this,
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
                    }
                    /* parivartan changes */
                    else if (!radioButtonTrasactionModeParivartan.isChecked()
                            && !radioButtonTrasactionModeManual.isChecked()) {
                        commonMethods.dialogWarning(context, "Please Select Transaction Mode", true);
                        setFocusable(linearlayoutTrasactionModeParivartan);
                        linearlayoutTrasactionModeParivartan.requestFocus();
                    } else if (photoBitmap == null) {
                        commonMethods.dialogWarning(context, "Please Capture the Photo", true);
                        setFocusable(imageButtonRetireSmartProposerPhotograph);
                        imageButtonRetireSmartProposerPhotograph.requestFocus();
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

        if (photoBitmap != null) {
            imageButtonRetireSmartProposerPhotograph
                    .setImageBitmap(photoBitmap);
        }
        btn_PolicyholderDate.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                DIALOG_ID = 2;
                showDialog(DATE_DIALOG_ID);
            }
        });

        btn_MarketingOfficalDate.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                DIALOG_ID = 3;
                showDialog(DATE_DIALOG_ID);

            }
        });

        input = inputVal.toString();
        output = retVal.toString();

        sumAssured = prsObj.parseXmlTag(output, "sumAssured");
        policyTermStr = prsObj.parseXmlTag(input, "policyTerm");
        gender = prsObj.parseXmlTag(input, "gender");
        String staffdiscount = prsObj.parseXmlTag(input, "isStaff");
        premFreq = prsObj.parseXmlTag(input, "premFreq");
        String netYield4 = prsObj.parseXmlTag(output, "netYeild4pr");
        String netYield8 = prsObj.parseXmlTag(output, "netYeild8pr");
        String maturity_age = prsObj.parseXmlTag(output, "maturityAge");
        age_entry = prsObj.parseXmlTag(input, "age");
        String annualised_premium = prsObj.parseXmlTag(output, "annPrem");
        premium_paying_term = prsObj.parseXmlTag(input, "premPayingTerm");
        premiumAmount = prsObj.parseXmlTag(input, "premiumAmount");

        premiumAmount = obj
                .getRound(obj.getStringWithout_E(Double.valueOf((premiumAmount
                        .equals("") || premiumAmount == null) ? "0"
                        : premiumAmount)));

        String red_in_year_at_8_maturity_age = prsObj.parseXmlTag(output,
                "redInYieldMat");
        no_of_year_elapsed = prsObj.parseXmlTag(input, "noOfYrElapsed");
        String red_in_year_at_8_no_of_year = prsObj.parseXmlTag(output,
                "redInYieldNoYr");
        PremPayingOption = prsObj.parseXmlTag(input, "premPayingOption");

        TextView tv_bi_retire_smart_ppt = (TextView) d.findViewById(R.id.tv_bi_retire_smart_ppt);
        premPayingMode = prsObj.parseXmlTag(input, "premFreq");

        if (PremPayingOption.equalsIgnoreCase("Single")) {
            tv_bi_retire_smart_ppt.setText("One time at the inception of the policy");
            tv_bi_retire_smart_premium_payment_option.setText("One time at the inception of the policy");
        } else if (PremPayingOption.equalsIgnoreCase("Regular")) {
            tv_bi_retire_smart_ppt.setText("Same as Policy Term");
            tv_bi_retire_smart_premium_payment_option.setText(premPayingMode);
        } else if (PremPayingOption.equalsIgnoreCase("LPPT")) {
            tv_bi_retire_smart_ppt.setText(premium_paying_term);
            tv_bi_retire_smart_premium_payment_option.setText(premPayingMode);
        }


        TextView tv_bi_retire_smart_amount_of_intsal_premium = (TextView) d.findViewById(R.id.tv_bi_retire_smart_amount_of_intsal_premium);
        premium = ((int) Double.parseDouble(prsObj.parseXmlTag(input,
                "premiumAmount"))) + "";
        tv_bi_retire_smart_amount_of_intsal_premium.setText(premium);
        String totalFstYearPrem = prsObj.parseXmlTag(output, "totFyPrem");
        String annuityPay4pa = prsObj.parseXmlTag(output, "annuityPay4pa");
        String annuityPay8pa = prsObj.parseXmlTag(output, "annuityPay8pa");
        String annuityRates = prsObj.parseXmlTag(output, "annuityRates");

        if (staffdiscount.equalsIgnoreCase("true")) {
            // tr_staff_per.setVisibility(View.VISIBLE);
            tv_bi_is_Staff.setText("Yes");
            // tv_bi_flexi_smart_plus_staff_per.setText(staffdiscount_per);
        } else {
            // .setVisibility(View.GONE);
            tv_bi_is_Staff.setText("No");
        }

        tv_grid_annuity_option.setText(spnr_bi_Matuirty_option.getSelectedItem().toString());

        //tv_bi_retire_smart_premium_payment_option.setText(PremPayingOption);
        tv_bi_retire_smart_policy_term.setText(policyTermStr + " Years");
        tv_bi_retire_smart_life_assured_gender.setText(gender);

        tv_bi_retire_smart_age_entry.setText(age_entry + " Years");

        tv_bi_retire_smart_maturity_age.setText(maturity_age + " Years");

        tv_bi_retire_smart_annualised_premium
                .setText("Rs. "
                        + obj.getRound(obj.getStringWithout_E(Double
                        .valueOf((annualised_premium.equals("") || annualised_premium == null) ? "0"
                                : annualised_premium))));

        tv_bi_retire_smart_premium_paying_term.setText(premium_paying_term
                + "Years");
        tv_bi_retire_smart_payment_mode.setText(premFreq);
        tv_bi_retire_smart_premium_paying_term.setText(premium_paying_term
                + "Years");

        tv_bi_retire_smart_annualised_premium.setText("Rs. "
                + annualised_premium);

        tv_retire_smart_no_of_years_elapsed.setText(no_of_year_elapsed
                + " Years ");

        tv_retire_smart_reduction_yield.setText(red_in_year_at_8_no_of_year
                + " % ");

        tv_retire_smart_reduction_yeild2.setText(red_in_year_at_8_maturity_age
                + " % ");

        // changes done by amit 21-3-2015
        tv_retire_smart_maturity_age2.setText((spnrPolicyTerm.getSelectedItem()
                .toString() + " Years"));

        //tv_bi_retire_smart_annuity_payable_rates_at_4.setText(annuityPay4pa);

        // tv_bi_retire_smart_annuity_payable_rates_at_8.setText(annuityPay8pa);

        //tv_bi_retire_smart_annuity_payable_at_4.setText(netYield4 + "%");
        tv_bi_retire_smart_annuity_payable_at_8.setText(netYield8 + "%");
        tv_bi_retire_smart_total_first_year_premium.setText("Rs. " + totalFstYearPrem);

        tv_annuity_rate
                .setText("** The annuity rate used is of Life annuity option. This illustration is based on an annuity rate of "
                        + annuityRates
                        + " per INR 1000 vesting amount. We do not guarantee the annuity rates. The actual annuity rate may differ and may be lesser or higher than the one shown in this illustration.");

        TextView tv_Company_policy_surrender_dec = (TextView) d
                .findViewById(R.id.tv_Company_policy_surrender_dec);


        percent_equity_pension_fund = prsObj.parseXmlTag(input, "percent_equity_pension_fund");
        tv_smart_elite_equity_elite_fund3_allocation
                .setText((percent_equity_pension_fund.equals("") ? "0"
                        : percent_equity_pension_fund) + " % ");

        percent_equity_optimiser_pension_fund = prsObj.parseXmlTag(input, "percent_equity_optimiser_pension_fund");
        tv_smart_elite_balanced_fund2_allocation
                .setText((percent_equity_optimiser_pension_fund.equals("") ? "0"
                        : percent_equity_optimiser_pension_fund) + " % ");

        percent_growth_pension_fund = prsObj.parseXmlTag(input, "percent_growth_pension_fund");
        tv_smart_elite_bond_fund2_allocation
                .setText((percent_growth_pension_fund.equals("") ? "0"
                        : percent_growth_pension_fund) + " % ");

        percent_bond_pension_fund3 = prsObj.parseXmlTag(input, "percent_bond_pension_fund3");
        tv_smart_elite_market_fund2_allocation
                .setText((percent_bond_pension_fund3.equals("") ? "0"
                        : percent_bond_pension_fund3) + " % ");

        percent_money_market_pension_fund3 = prsObj.parseXmlTag(input, "percent_money_market_pension_fund3");
        tv_smart_elite_bond_optimiser_fund2_allocation
                .setText((percent_money_market_pension_fund3.equals("") ? "0"
                        : percent_money_market_pension_fund3) + " % ");


        nonGuaranVestingBenefit_4Percent = prsObj.parseXmlTag(output, "fundVal4");


        nonGuaranVestingBenefit_4Percent = obj.getRound(obj.getStringWithout_E(Double
                .valueOf((nonGuaranVestingBenefit_4Percent.equals("") || nonGuaranVestingBenefit_4Percent == null) ? "0"
                        : nonGuaranVestingBenefit_4Percent)))
                + "";
        nonGuaranVestingBenefit_8Percent = prsObj.parseXmlTag(output, "fundVal8");
        nonGuaranVestingBenefit_8Percent = obj.getRound(obj.getStringWithout_E(Double
                .valueOf((nonGuaranVestingBenefit_8Percent.equals("") || nonGuaranVestingBenefit_8Percent == null) ? "0"
                        : nonGuaranVestingBenefit_8Percent)))
                + "";
        guarnVestngBen = prsObj.parseXmlTag(output, "VestingMinAssBen");

        guarnVestngBen = obj.getRound(obj.getStringWithout_E(Double
                .valueOf((guarnVestngBen.equals("") || guarnVestngBen == null) ? "0"
                        : guarnVestngBen)))
                + "";
        annuityPayout_4_Pr = prsObj.parseXmlTag(output, "annuityPay4pa");
        annuityPayout_4_Pr = obj.getRound(obj.getStringWithout_E(Double
                .valueOf((annuityPayout_4_Pr.equals("") || annuityPayout_4_Pr == null) ? "0"
                        : annuityPayout_4_Pr)))
                + "";
        annuityPayout_8_Pr = prsObj.parseXmlTag(output, "annuityPay8pa");

        annuityPayout_8_Pr = obj.getRound(obj.getStringWithout_E(Double
                .valueOf((annuityPayout_8_Pr.equals("") || annuityPayout_8_Pr == null) ? "0"
                        : annuityPayout_8_Pr)))
                + "";
        annuityAmount_MiniBen = prsObj.parseXmlTag(output, "annuityAmount_MiniBen");
        annuityAmount_MiniBen = obj.getRound(obj.getStringWithout_E(Double
                .valueOf((annuityAmount_MiniBen.equals("") || annuityAmount_MiniBen == null) ? "0"
                        : annuityAmount_MiniBen)))
                + "";


        tv_illustrative_benefit_on_vesting_policy_year.setText(nonGuaranVestingBenefit_4Percent);
        tv_illustrative_benefit_on_vesting_age.setText(nonGuaranVestingBenefit_8Percent);
        tv_illustrative_benefit_on_vesting_tbpp_during_the_year.setText(guarnVestngBen);
        tv_illustrative_benefit_on_vesting_tbpp_cumulative.setText(annuityPayout_4_Pr);
        tv_illustrative_benefit_on_vesting_guaranteed_vesting_benefit.setText(annuityPayout_8_Pr);
        tv_illustrative_benefit_on_vesting_bonuses_nonguaranteed_benefit_4pa_1.setText(annuityAmount_MiniBen);

        String str_prem_freq = "";
        if (PremPayingOption.equalsIgnoreCase("Regular")) {
            str_prem_freq = "Regular";
            Company_policy_surrender_dec = "Your SBI LIFE -Retire Smart (UIN: 111L094V02) is a "
                    + str_prem_freq
                    + " Premium Policy and you are required to pay  "
                    + premPayingMode
                    + " Premium of Rs "
                    + getformatedThousandString(Integer.parseInt(obj.getRound(obj.getStringWithout_E(Double
                    .valueOf((premiumAmount.equals("") || premiumAmount == null) ? "0"
                            : premiumAmount)))))
                    + " .Your Policy Term is "
                    + policyTermStr
                    + " years, Premium Payment Term is same as policy term years.";
        } else if (PremPayingOption.equalsIgnoreCase("LPPT")) {

            str_prem_freq = "LPPT";
            Company_policy_surrender_dec = "Your SBI LIFE -Retire Smart (111L094V02) is a "
                    + str_prem_freq
                    + " Premium Policy and you are required to pay  "
                    + premFreq
                    + " Premium of Rs. "
                    + getformatedThousandString(Integer.parseInt(obj.getRound(obj.getStringWithout_E(Double
                    .valueOf((premiumAmount.equals("") || premiumAmount == null) ? "0"
                            : premiumAmount)))))
                    + " .Your Policy Term is "
                    + policyTermStr
                    + " years"
                    + ", Premium Payment Term is "
                    + premium_paying_term
                    + " years";
        } else {

            str_prem_freq = "Single";
            Company_policy_surrender_dec = "Your SBI LIFE -Retire Smart (111L094V02) is a "
                    + str_prem_freq
                    + " Premium Policy and you are required to pay  "
                    // + premFreq
                    + " premium once at the inception of the policy of Rs. "
                    + getformatedThousandString(Integer.parseInt(obj.getRound(obj.getStringWithout_E(Double
                    .valueOf((premiumAmount.equals("") || premiumAmount == null) ? "0"
                            : premiumAmount)))))
                    + " .Your Policy Term is "
                    + policyTermStr
                    + " years"
                    + ", Premium Payment Term is Not Applicable years.";
            // + premium_paying_term
            // + " years";
        }
        if (!spnr_bi_Matuirty_option.getSelectedItem().toString().contains("Life and Last  Survivor")) {
            Annuity_rate_dec = "The values shown above are for illustration purpose only. This illustration is based on an annuity rate of " + annuityRates + " per INR 1000 vesting amount. We do not guarantee the annuity rates.  The actual annuity amount depends on the prevailing annuity rates at the time of vesting. The amounts of annuity based on the assumed investment return of 8% p.a & 4% p.a. are not upper or lower limits of what you might get back. For details on risk factors, terms and conditions, please read sales brochure carefully.";
        } else {
            Annuity_rate_dec = "The values shown above are for illustration purpose only. This illustration is based on an annuity rate of " + annuityRates + " per INR 1000 vesting amount. We do not guarantee the annuity rates.  The actual annuity amount depends on the prevailing annuity rates at the time of vesting. The amounts of annuity based on the assumed investment return of 8% p.a & 4% p.a. are not upper or lower limits of what you might get back. For details on risk factors, terms and conditions, please read sales brochure carefully." + "\n" + "For the calculation of annuity rate, we have considered the same age for primary Annuitants and secondary Annuitants.";
        }


        tr_annuity_rate_dec.setText(Annuity_rate_dec);

        tv_Company_policy_surrender_dec.setText(Company_policy_surrender_dec);


        for (int i = 1; i <= Integer.parseInt(policyTermStr); i++) {
            String policy_year = prsObj
                    .parseXmlTag(output, "policyYr" + i + "");
            String age = prsObj.parseXmlTag(output, "age" + i + "");
            String premium = prsObj.parseXmlTag(output, "AnnPrem" + i + "");
            String cummprem = prsObj.parseXmlTag(output, "CumPrem" + i + "");
            String premium_allocation_charge = prsObj.parseXmlTag(output,
                    "PremAllCharge" + i + "");
            String annulize_premium_allocation_charge = prsObj.parseXmlTag(output,
                    "AnnPremPremiumAllCharge" + i + "");

            String guranteeCharge4Pr = prsObj.parseXmlTag(output,
                    "guranteeCharge4Pr" + i + "");

            String guranteeCharge8Pr = prsObj.parseXmlTag(output,
                    "guranteeCharge8Pr" + i + "");

            String guranteedAddtn4Pr = prsObj.parseXmlTag(output,
                    "guranteedAddtn4Pr" + i + "");

            String guranteedAddtn8Pr = prsObj.parseXmlTag(output,
                    "guranteedAddtn8Pr" + i + "");


            String terminalAddtn4Pr = prsObj.parseXmlTag(output,
                    "terminalAddtn4Pr" + i + "");

            String terminalAddtn8Pr = prsObj.parseXmlTag(output,
                    "terminalAddtn8Pr" + i + "");


            String AdditionsToTheFund4Pr = prsObj.parseXmlTag(output,
                    "additionToFund4Pr" + i + "");

            String AdditionsToTheFund8Pr = prsObj.parseXmlTag(output,
                    "additionToFund8Pr" + i + "");
            String policy_administration_charge = prsObj.parseXmlTag(output,
                    "PolAdminChrg" + i + "");
            String mortality_charge1 = prsObj.parseXmlTag(output, "MortChrg4Pr"
                    + i + "");
            String total_charge1A = prsObj.parseXmlTag(output, "OtherCharges4Pr" + i
                    + "");
            String total_charge2A = prsObj.parseXmlTag(output, "OtherCharges8Pr" + i
                    + "");
            String service_tax_on_mortality_charge1 = prsObj.parseXmlTag(
                    output, "TotServTax4Pr" + i + "");
            String fund_before_fmc1 = prsObj.parseXmlTag(output,
                    "fundBeforeFMC4Pr" + i + "");
            String fund_management_charge1 = prsObj.parseXmlTag(output,
                    "FundMgmtCharg4Pr" + i + "");

            String fund_value_at_end1 = prsObj.parseXmlTag(output,
                    "FundValAtEnd4Pr" + i + "");
            String surrender_value1 = prsObj.parseXmlTag(output, "SurrVal4Pr"
                    + i + "");
            String death_benefit1 = prsObj.parseXmlTag(output, "DeathBen4Pr"
                    + i + "");
            String mortality_charge2 = prsObj.parseXmlTag(output, "MortChrg8Pr"
                    + i + "");
            String total_charge1B = prsObj.parseXmlTag(output, "otherCharge4Pr_partB" + i
                    + "");
            String total_charge2B = prsObj.parseXmlTag(output, "otherCharge8Pr_partB" + i
                    + "");
            String service_tax_on_mortality_charge2 = prsObj.parseXmlTag(
                    output, "TotServTxOnCharg8Pr" + i + "");
            String fund_before_fmc2 = prsObj.parseXmlTag(output,
                    "fundBeforeFMC8Pr" + i + "");
            String fund_management_charge2 = prsObj.parseXmlTag(output,
                    "FundMgmtCharg8Pr" + i + "");
            String fund_value_at_end2 = prsObj.parseXmlTag(output,
                    "FundValAtEnd8Pr" + i + "");
            String surrender_value2 = prsObj.parseXmlTag(output, "SurrVal8Pr"
                    + i + "");
            String death_benefit2 = prsObj.parseXmlTag(output, "DeathBen8Pr"
                    + i + "");
           /* String commission = prsObj.parseXmlTag(output, "CommIfPay8Pr" + i
                    + "");*/
            String commission = prsObj.parseXmlTag(output, "Commision" + i
                    + "");


            list_data.add(new M_BI_RetireSmartAdapterCommon(
                    policy_year,
                    obj.getRound(obj.getStringWithout_E(Double.valueOf((premium.equals("") || premium == null) ? "0" : premium))) + "",
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


            list_data1.add(new M_BI_RetireSmartAdapterCommon2(
                    policy_year,
                    obj.getRound(obj.getStringWithout_E(Double.valueOf((premium.equals("") || premium == null) ? "0" : premium))) + "",
                    obj.getRound(obj.getStringWithout_E(Double.valueOf((premium_allocation_charge.equals("") || premium_allocation_charge == null) ? "0" : premium_allocation_charge))) + "",
                    obj.getRound(obj.getStringWithout_E(Double.valueOf((annulize_premium_allocation_charge.equals("") || annulize_premium_allocation_charge == null) ? "0" : annulize_premium_allocation_charge))) + "",
                    obj.getRound(obj.getStringWithout_E(Double.valueOf((mortality_charge2.equals("") || mortality_charge2 == null) ? "0" : mortality_charge2))) + "",
                    obj.getRound(obj.getStringWithout_E(Double.valueOf((service_tax_on_mortality_charge2.equals("") || service_tax_on_mortality_charge2 == null) ? "0" : service_tax_on_mortality_charge2))) + "",
                    obj.getRound(obj.getStringWithout_E(Double.valueOf((policy_administration_charge.equals("") || policy_administration_charge == null) ? "0" : policy_administration_charge))) + "",
                    guranteeCharge8Pr,
                    obj.getRound(obj.getStringWithout_E(Double.valueOf((total_charge2B.equals("") || total_charge2B == null) ? "0" : total_charge2B))) + "",
                    obj.getRound(obj.getStringWithout_E(Double.valueOf((AdditionsToTheFund8Pr.equals("") || AdditionsToTheFund8Pr == null) ? "0" : AdditionsToTheFund8Pr))) + "",
                    guranteedAddtn8Pr,
                    terminalAddtn8Pr,
                    obj.getRound(obj.getStringWithout_E(Double.valueOf((fund_before_fmc2.equals("") || fund_before_fmc2 == null) ? "0" : fund_before_fmc2))) + "",
                    obj.getRound(obj.getStringWithout_E(Double.valueOf((fund_management_charge2.equals("") || fund_management_charge2 == null) ? "0" : fund_management_charge2))) + "",
                    obj.getRound(obj.getStringWithout_E(Double.valueOf((fund_value_at_end2.equals("") || fund_value_at_end2 == null) ? "0" : fund_value_at_end2))) + "",
                    obj.getRound(obj.getStringWithout_E(Double.valueOf((surrender_value2.equals("") || surrender_value2 == null) ? "0" : surrender_value2))) + "",
                    obj.getRound(obj.getStringWithout_E(Double.valueOf((death_benefit2.equals("") || death_benefit2 == null) ? "0" : death_benefit2))) + ""));


            list_data2.add(new M_BI_RetireSmartAdapterCommon2(
                    policy_year,
                    obj.getRound(obj.getStringWithout_E(Double.valueOf((premium.equals("") || premium == null) ? "0" : premium))) + "",
                    obj.getRound(obj.getStringWithout_E(Double.valueOf((premium_allocation_charge.equals("") || premium_allocation_charge == null) ? "0" : premium_allocation_charge))) + "",
                    obj.getRound(obj.getStringWithout_E(Double.valueOf((annulize_premium_allocation_charge.equals("") || annulize_premium_allocation_charge == null) ? "0" : annulize_premium_allocation_charge))) + "",
                    obj.getRound(obj.getStringWithout_E(Double.valueOf((mortality_charge1.equals("") || mortality_charge1 == null) ? "0" : mortality_charge1))) + "",
                    obj.getRound(obj.getStringWithout_E(Double.valueOf((service_tax_on_mortality_charge1.equals("") || service_tax_on_mortality_charge1 == null) ? "0" : service_tax_on_mortality_charge1))) + "",
                    obj.getRound(obj.getStringWithout_E(Double.valueOf((policy_administration_charge.equals("") || policy_administration_charge == null) ? "0" : policy_administration_charge))) + "",
                    guranteeCharge4Pr,
                    obj.getRound(obj.getStringWithout_E(Double.valueOf((total_charge1B.equals("") || total_charge1B == null) ? "0" : total_charge1B))) + "",
                    obj.getRound(obj.getStringWithout_E(Double.valueOf((AdditionsToTheFund4Pr.equals("") || AdditionsToTheFund4Pr == null) ? "0" : AdditionsToTheFund4Pr))) + "",
                    guranteedAddtn4Pr,
                    terminalAddtn4Pr,
                    obj.getRound(obj.getStringWithout_E(Double.valueOf((fund_before_fmc1.equals("") || fund_before_fmc1 == null) ? "0" : fund_before_fmc1))) + "",
                    obj.getRound(obj.getStringWithout_E(Double.valueOf((fund_management_charge1.equals("") || fund_management_charge1 == null) ? "0" : fund_management_charge1))) + "",
                    obj.getRound(obj.getStringWithout_E(Double.valueOf((fund_value_at_end1.equals("") || fund_value_at_end1 == null) ? "0" : fund_value_at_end1))) + "",
                    obj.getRound(obj.getStringWithout_E(Double.valueOf((surrender_value1.equals("") || surrender_value1 == null) ? "0" : surrender_value1))) + "",
                    obj.getRound(obj.getStringWithout_E(Double.valueOf((death_benefit1.equals("") || death_benefit1 == null) ? "0" : death_benefit1))) + ""));
        }


        Adapter_BI_RetireSmartGridCommon adapter = new Adapter_BI_RetireSmartGridCommon(
                BI_RetireSmartActivity.this, list_data);
        gv_userinfo.setAdapter(adapter);

        Adapter_BI_RetireSmartGridCommon2 adapter1 = new Adapter_BI_RetireSmartGridCommon2(
                BI_RetireSmartActivity.this, list_data1);
        gv_userinfo2.setAdapter(adapter1);

        Adapter_BI_RetireSmartGridCommon2 adapter2 = new Adapter_BI_RetireSmartGridCommon2(
                BI_RetireSmartActivity.this, list_data2);
        gv_userinfo3.setAdapter(adapter2);


        GridHeight gh = new GridHeight();
        gh.getheight(gv_userinfo, policyTermStr);
        gh.getheight(gv_userinfo2, policyTermStr);
        gh.getheight(gv_userinfo3, policyTermStr);

        d.show();

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
                    try {
                        File Photo = commonMethods.galleryAddPic(context);
                        Bitmap bmp = BitmapFactory.decodeFile(Photo
                                .getAbsolutePath());

                        Bitmap b = null;
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
                            imageButtonRetireSmartProposerPhotograph
                                    .setImageBitmap(scaled);
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

    private void getInput(RetireSmartBean retireSmartBean) {
        inputVal = new StringBuilder();

        int age = retireSmartBean.getAge();
        String gender = retireSmartBean.getGender();
        int policyTerm = retireSmartBean.getPolicyTerm();
        String PremPayingMode = retireSmartBean.getPremFrequencyMode();
        String PremPayingOption = retireSmartBean.getPremPayingOption();
        int premPayingTerm = retireSmartBean.getPremiumPayingTerm();
        double premiumAmount = retireSmartBean.getPremiumAmount();
        // boolean JandKResident = smartScholarBean.getIsJKResidentDiscOrNot();
        boolean isStaffOrNot = retireSmartBean.getStaffDisc();

        String LifeAssured_title = spnr_bi_retire_smart_life_assured_title
                .getSelectedItem().toString();
        String LifeAssured_firstName = edt_bi_retire_smart_life_assured_first_name
                .getText().toString();
        String LifeAssured_middleName = edt_bi_retire_smart_life_assured_middle_name
                .getText().toString();
        String LifeAssured_lastName = edt_bi_retire_smart_life_assured_last_name
                .getText().toString();
        String LifeAssured_DOB = btn_bi_retire_smart_life_assured_date
                .getText().toString();
        String LifeAssured_age = spnrageInYears.getSelectedItem().toString();
        String LifeAssured_gender = spnrGender.getSelectedItem().toString();

        String proposer_title = "";
        String proposer_firstName = "";
        String proposer_middleName = "";
        String proposer_lastName = "";
        String proposer_DOB = "";
        String proposer_age = "";
        String proposer_gender = "";

        inputVal.append("<?xml version='1.0' encoding='utf-8' ?><retiresmart>");

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
        /* parivartan changes */
        inputVal.append("<product_Code>").append(product_Code).append("</product_Code>");
        inputVal.append("<product_UIN>").append(product_UIN).append("</product_UIN>");
        inputVal.append("<product_cateogory>").append(product_cateogory).append("</product_cateogory>");
        inputVal.append("<product_type>").append(product_type).append("</product_type>");
        /* end */

        inputVal.append("<proposer_Is_Same_As_Life_Assured>").append(proposer_Is_Same_As_Life_Assured).append("</proposer_Is_Same_As_Life_Assured>");

        inputVal.append("<age>").append(age).append("</age>");
        // inputVal.append("<gender>" + gender + "</gender>");

        inputVal.append("<policyTerm>").append(policyTerm).append("</policyTerm>");
        // inputVal.append("<isJKResident>" + JandKResident +
        // "</isJKResident>");
        inputVal.append("<str_kerla_discount>" + str_kerla_discount + "</str_kerla_discount>");
        inputVal.append("<isStaff>").append(isStaffOrNot).append("</isStaff>");
        inputVal.append("<premFreq>").append(PremPayingMode).append("</premFreq>");
        inputVal.append("<premPayingOption>").append(PremPayingOption).append("</premPayingOption>");
        inputVal.append("<premiumAmount>").append(premiumAmount).append("</premiumAmount>");
        inputVal.append("<noOfYrElapsed>").append(edtnoOfYearsElapsedSinceInception.getText().toString()).append("</noOfYrElapsed>");

        inputVal.append("<premPayingTerm>").append(premPayingTerm).append("</premPayingTerm>");
        inputVal.append("<percent_equity_pension_fund>" + equity_pension_fund.getText().toString() + "</percent_equity_pension_fund>");
        inputVal.append("<percent_equity_optimiser_pension_fund>" + equity_optimiser_pension_fund.getText().toString() + "</percent_equity_optimiser_pension_fund>");
        inputVal.append("<percent_growth_pension_fund>" + growth_pension_fund.getText().toString() + "</percent_growth_pension_fund>");
        inputVal.append("<percent_bond_pension_fund3>" + bond_pension_fund3.getText().toString() + "</percent_bond_pension_fund3>");
        inputVal.append("<percent_money_market_pension_fund3>" + money_market_pension_fund3.getText().toString() + "</percent_money_market_pension_fund3>");
        inputVal.append("<bi_retire_smart_plan_option>" + spnr_bi_plan_option_choice.getSelectedItem().toString() + "</bi_retire_smart_plan_option>");
        inputVal.append("<MATUIRTY_OPTION>" + spnr_bi_Matuirty_option.getSelectedItem().toString() + "</MATUIRTY_OPTION>");
        inputVal.append("<MATUIRTY_FREQUENCY>" + spnr_bi_Matuirty_frequency.getSelectedItem().toString() + "</MATUIRTY_FREQUENCY>");

        //Added Tushar Kadam Kerla Applicable Tax 7-Jun-2019
        String str_kerla_discount = "N";
        if (cb_kerladisc.isChecked()) {
            str_kerla_discount = "Y";
        }

        inputVal.append("<KFC>" + str_kerla_discount + "</KFC>");
        //End Added Tushar Kadam Kerla Applicable Tax 7-Jun-2019


        inputVal.append("</retiresmart>");

    }

    private void InitalizeVariable() {

        sv_bi_retire_smart_main = findViewById(R.id.sv_bi_retire_smart_main);
        isStaffDisc = findViewById(R.id.cb_StaffDisc);
        spnrGender = findViewById(R.id.gender);
//        spnrGender.setClickable(false);
//        spnrGender.setEnabled(false);

        spnrageInYears = findViewById(R.id.age);
        spnrageInYears.setEnabled(false);
        spnrageInYears.setClickable(false);

        spnrPolicyTerm = findViewById(R.id.policyTerm);
        spnrPremiumFrequencyMode = findViewById(R.id.premFreqMode);
        edtpremiumAmount = findViewById(R.id.premiumAmt);
        spnrPremiumPayingOption = findViewById(R.id.premPayingOption);
        spnrPremiumPayingTerm = findViewById(R.id.premPayingTerm);
        edtnoOfYearsElapsedSinceInception = findViewById(R.id.years_elapsed_since_inception);
        btnSubmit = findViewById(R.id.btnSubmit);
        back = findViewById(R.id.back);
        help_policyTerm = findViewById(R.id.help_policyterm);
        help_premiumAmt = findViewById(R.id.help_premAmt);
        help_noOfYearsElapsedSinceInception = findViewById(R.id.help_years_elapsed_since_inception);
        help_premPayingTerm = findViewById(R.id.help_premPayingTerm);
        // SFIN of funds
        selViewSFIN = findViewById(R.id.selViewSFIN);
        tbEquityPensionFund = (TableRow) findViewById(R.id.tbEquityPensionFund);
        tbEquityOptimiserPensionFund = (TableRow) findViewById(R.id.tbEquityOptimiserPensionFund);
        tbGrowthPensionFund = (TableRow) findViewById(R.id.tbGrowthPensionFund);
        tbBondPensionFund3 = (TableRow) findViewById(R.id.tbBondPensionFund3);
        tbMoneyMarketPensionFund3 = (TableRow) findViewById(R.id.tbMoneyMarketPensionFund3);
        btn_bi_retire_smart_life_assured_date = findViewById(R.id.btn_bi_retire_smart_life_assured_date);
        spnr_bi_retire_smart_life_assured_title = findViewById(R.id.spnr_bi_retire_smart_life_assured_title);
        edt_bi_retire_smart_life_assured_first_name = findViewById(R.id.edt_bi_retire_smart_life_assured_first_name);
        edt_bi_retire_smart_life_assured_middle_name = findViewById(R.id.edt_bi_retire_smart_life_assured_middle_name);
        edt_bi_retire_smart_life_assured_last_name = findViewById(R.id.edt_bi_retire_smart_life_assured_last_name);

        edt_proposerdetail_basicdetail_contact_no = findViewById(R.id.edt_retire_smart_contact_no);
        edt_proposerdetail_basicdetail_Email_id = findViewById(R.id.edt_retire_smart_Email_id);
        edt_proposerdetail_basicdetail_ConfirmEmail_id = findViewById(R.id.edt_retire_smart_ConfirmEmail_id);

        spnr_bi_plan_option_choice = (Spinner) findViewById(R.id.spnr_bi_plan_option_choice);
        ll_fundDetails = (LinearLayout) findViewById(R.id.ll_fundDetails);

        equity_pension_fund = (EditText) findViewById(R.id.equity_pension_fund);
        equity_optimiser_pension_fund = (EditText) findViewById(R.id.equity_optimiser_pension_fund);
        growth_pension_fund = (EditText) findViewById(R.id.growth_pension_fund);
        bond_pension_fund3 = (EditText) findViewById(R.id.bond_pension_fund3);
        money_market_pension_fund3 = (EditText) findViewById(R.id.money_market_pension_fund3);

        spnr_bi_Matuirty_option = (Spinner) findViewById(R.id.spnr_bi_Matuirty_option);
        spnr_bi_Matuirty_frequency = (Spinner) findViewById(R.id.spnr_bi_Matuirty_frequency);

        tr_prem_freq_mode = (TableRow) findViewById(R.id.tr_prem_freq_mode);
//        tr_prem_freq_mode_spnr = (TableRow) findViewById(R.id.tr_prem_freq_mode_spnr);
        tr_prem_paying_term = (TableRow) findViewById(R.id.tr_prem_paying_term);
//        tr_prem_paying_term_spnr = (TableRow) findViewById(R.id.tr_prem_paying_term_spnr);


    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

        boolean flagFocus = true;
        if (!flagFocus) {
            sv_bi_retire_smart_main.requestFocus();
        } else {
            spnr_bi_retire_smart_life_assured_title.requestFocus();
        }

    }

    private void initialiseDate() {
        Calendar calender = Calendar.getInstance();
        mYear = calender.get(Calendar.YEAR);
        mMonth = calender.get(Calendar.MONTH);
        mDay = calender.get(Calendar.DAY_OF_MONTH);

    }

    /**
     * Setting input variable in bean object
     */
    private void addListenerOnSubmit() {
        retireSmartBean = new RetireSmartBean();
        if (isStaffDisc.isChecked()) {
            retireSmartBean.setStaffDisc(true);
        } else {
            retireSmartBean.setStaffDisc(false);
        }

        if (cb_kerladisc.isChecked()) {
            retireSmartBean.setKerlaDisc(true);
            //retireSmartBean.setServiceTax(true);
        } else {
            retireSmartBean.setKerlaDisc(false);
            //retireSmartBean.setServiceTax(false);
        }

        retireSmartBean.setAnnuityOption(spnr_bi_Matuirty_option.getSelectedItem().toString());


        retireSmartBean.setAge(Integer.parseInt(spnrageInYears
                .getSelectedItem().toString()));

        retireSmartBean.setGender(spnrGender.getSelectedItem().toString());

        retireSmartBean.setPolicyTerm(Integer.parseInt(spnrPolicyTerm
                .getSelectedItem().toString()));

        retireSmartBean.setVestingAge((Integer.parseInt(spnrageInYears.getSelectedItem().toString())
                + Integer.parseInt(spnrPolicyTerm.getSelectedItem().toString())));

        if ((spnrPremiumPayingOption.getSelectedItem().toString()).equalsIgnoreCase("Single")) {
            retireSmartBean.setPremFrequencyMode("Single");
        } else {
            retireSmartBean.setPremFrequencyMode(spnrPremiumFrequencyMode
                    .getSelectedItem().toString());
        }


        retireSmartBean.setPremiumAmount(Double.parseDouble(edtpremiumAmount
                .getText().toString()));

        retireSmartBean.setPremPayingOption(spnrPremiumPayingOption
                .getSelectedItem().toString());

        if ((spnrPremiumPayingOption.getSelectedItem().toString()).equalsIgnoreCase("Single")) {
            retireSmartBean.setPremiumPayingTerm(1);
        } else {
            retireSmartBean.setPremiumPayingTerm(Integer.parseInt(spnrPremiumPayingTerm.getSelectedItem().toString()));
        }

        retireSmartBean.setNoOfYearsElapsedSinceInception(Integer
                .parseInt(edtnoOfYearsElapsedSinceInception.getText()
                        .toString()));

        if ((spnrPremiumPayingOption.getSelectedItem().toString()).equalsIgnoreCase("Single")) {
            retireSmartBean.setPF(1);
        } else if (spnrPremiumFrequencyMode.getSelectedItem().toString()
                .equals("Yearly")) {
            retireSmartBean.setPF(1);
        } else if (spnrPremiumFrequencyMode.getSelectedItem().toString()
                .equals("Half Yearly")) {
            retireSmartBean.setPF(2);
        } else if (spnrPremiumFrequencyMode.getSelectedItem().toString()
                .equals("Quarterly")) {
            retireSmartBean.setPF(4);
        } else if (spnrPremiumFrequencyMode.getSelectedItem().toString()
                .equals("Monthly")) {
            retireSmartBean.setPF(12);
        }

        /*if (bi_retire_smart_plan_option.equalsIgnoreCase("Advantage Plan")) {
                retireSmartBean.setPercentToBeInvested_EquityPensionFund();
                retireSmartBean.setPercentToBeInvested_BondPensionFund();
                retireSmartBean.setPercentToBeInvested_MoneyMarketPensionFund();


        } else {
*/

        if (!equity_pension_fund.getText().toString().equals("")) {
            retireSmartBean.setPercentToBeInvested_EquityPensionFund(Double.parseDouble(equity_pension_fund.getText().toString()));
        }
        if (!equity_optimiser_pension_fund.getText().toString().equals("")) {
            retireSmartBean.setPercentToBeInvested_EquityOptPensionFund(Double.parseDouble(equity_optimiser_pension_fund.getText().toString()));
        }
        if (!growth_pension_fund.getText().toString().equals("")) {
            retireSmartBean.setPercentToBeInvested_GrowthPensionFund(Double.parseDouble(growth_pension_fund.getText().toString()));
        }
        if (!bond_pension_fund3.getText().toString().equals("")) {
            retireSmartBean.setPercentToBeInvested_BondPensionFund(Double.parseDouble(bond_pension_fund3.getText().toString()));
        }
        if (!money_market_pension_fund3.getText().toString().equals("")) {
            retireSmartBean.setPercentToBeInvested_MoneyMarketPensionFund(Double.parseDouble(money_market_pension_fund3.getText().toString()));
        }
        // }
        retireSmartBean.setBi_retire_smart_plan_option(spnr_bi_plan_option_choice.getSelectedItem().toString());


        showRetireSmartOutputPage(retireSmartBean);

    }

    private void showRetireSmartOutputPage(RetireSmartBean retireSmartBean) {
        // For BI
        String staffStatus = "";
        // RetireSmartBIPdf obj = new RetireSmartBIPdf();
        // String[] output = obj.getOutput("", retireSmartBean);
        String[] output = getOutput("", retireSmartBean);
        String[] outputReductionYield = getOutputReductionYield("",
                retireSmartBean);
        if (retireSmartBean.getStaffDisc())
            staffStatus = "sbi";
        else
            staffStatus = "none";
        System.out.println(outputReductionYield[2] + " "
                + outputReductionYield[3]);
        try {
            retVal.append("<?xml version='1.0' encoding='utf-8' ?><RetireSmart>");
            retVal.append("<errCode>0</errCode>");
            retVal.append("<staffStatus>").append(staffStatus).append("</staffStatus>");
            retVal.append("<maturityAge>").append(retireSmartBean.getAge() + retireSmartBean
                    .getPolicyTerm()).append("</maturityAge>");

            retVal.append("<redInYieldMat>").append(outputReductionYield[0])
                    .append("</redInYieldMat>").append("<redInYieldNoYr>")
                    .append(outputReductionYield[1]).append("</redInYieldNoYr>")
                    .append("<netYeild4pr>").append(outputReductionYield[2])
                    .append("</netYeild4pr>").append("<netYeild8pr>")
                    .append(outputReductionYield[3]).append("</netYeild8pr>")
                    .append("<sumAssured>").append(output[0])
                    .append("</sumAssured>").append("<fundVal4>")
                    .append(output[1]).append("</fundVal4>")
                    .append("<fundVal8>").append(output[2])
                    .append("</fundVal8>").append("<VestingMinAssBen>")
                    .append(output[8]).append("</VestingMinAssBen>")
                    .append("<annPrem>")
                    .append(output[3]).append("</annPrem>")
                    .append("<totFyPrem>").append(output[4])
                    .append("</totFyPrem>").append("<annuityPay4pa>")
                    .append(output[5]).append("</annuityPay4pa>")
                    .append("<annuityPay8pa>").append(output[6])
                    .append("</annuityPay8pa>").append("<annuityAmount_MiniBen>")
                    .append(output[9])
                    .append("</annuityAmount_MiniBen>").append("<annuityRates>")
                    .append(output[7]).append("</annuityRates>");

            int index = retireSmartBean.getPolicyTerm();
            String FundValAtEnd4Pr = prsObj.parseXmlTag(bussIll.toString(), "FundValAtEnd4Pr" + index + "");
            String FundValAtEnd8Pr = prsObj.parseXmlTag(bussIll.toString(), "FundValAtEnd8Pr" + index + "");

            retVal.append("<FundValAtEnd4Pr" + index + ">" + FundValAtEnd4Pr + "</FundValAtEnd4Pr" + index + ">");
            retVal.append("<FundValAtEnd8Pr" + index + ">" + FundValAtEnd8Pr + "</FundValAtEnd8Pr" + index + ">");

            retVal.append(bussIll.toString());
            // retVal.append(RetireSmartBIPdf.strGridXml);
            retVal.append("</RetireSmart>");

            System.out.println("Final output in xml" + retVal.toString());


        } catch (Exception e) {
            retVal.append("<?xml version='1.0' encoding='utf-8' ?><RetireSmart>" + "<errCode>1</errCode>" + "<errorMessage>").append(e.getMessage()).append("</errorMessage></RetireSmart>");
        }
    }

    // validation of premium amount
    private boolean valPremiumAmount() {
        String error = "", mode = "";
        double minPremiumAmount = 0, maxPremiumAmount = 0;

        if (edtpremiumAmount.getText().toString().equals("")) {
            error = "Please Enter Premium Amount in Rs. ";
        } else if (Integer.parseInt(edtpremiumAmount.getText().toString()) % 100 != 0) {
            error = "Please enter Premium Amount in multiple of 100";
        } else {
            // if(selPremiumFrequencyMode.getSelectedItem().equals("Yearly") )
            // {
            // mode="Yearly";
            // maxPremiumAmount=99999999;
            // }
            // else
            // if(selPremiumFrequencyMode.getSelectedItem().equals("Half Yearly")
            // )
            // {
            // mode="Half Yearly";
            // maxPremiumAmount=49999999;
            // }
            // else
            // if(selPremiumFrequencyMode.getSelectedItem().equals("Quarterly"))
            // {
            // mode="Quarterly";
            // maxPremiumAmount=24999999;
            // }
            // else
            // if(selPremiumFrequencyMode.getSelectedItem().equals("Monthly"))
            // {
            // mode="Monthly";
            // maxPremiumAmount=5999999;
            // }

            if (spnrPremiumPayingOption.getSelectedItem().equals("Regular")) {
                if (spnrPremiumFrequencyMode.getSelectedItem().equals("Yearly")) {
                    mode = "Yearly";
                    minPremiumAmount = 24000;
                } else if (spnrPremiumFrequencyMode.getSelectedItem().equals(
                        "Half Yearly")) {
                    mode = "Half Yearly";
                    minPremiumAmount = 15000;
                } else if (spnrPremiumFrequencyMode.getSelectedItem().equals(
                        "Quarterly")) {
                    mode = "Quarterly";
                    minPremiumAmount = 7500;
                } else if (spnrPremiumFrequencyMode.getSelectedItem().equals(
                        "Monthly")) {
                    mode = "Monthly";
                    minPremiumAmount = 2500;
                }
            } else if (spnrPremiumPayingOption.getSelectedItem().equals("LPPT")) {
                if (spnrPremiumFrequencyMode.getSelectedItem().equals("Yearly")) {
                    mode = "Yearly";
                    minPremiumAmount = 40000;
                } else if (spnrPremiumFrequencyMode.getSelectedItem().equals(
                        "Half Yearly")) {
                    mode = "Half Yearly";
                    minPremiumAmount = 20000;
                } else if (spnrPremiumFrequencyMode.getSelectedItem().equals(
                        "Quarterly")) {
                    mode = "Quarterly";
                    minPremiumAmount = 10000;
                } else if (spnrPremiumFrequencyMode.getSelectedItem().equals(
                        "Monthly")) {
                    mode = "Monthly";
                    minPremiumAmount = 5000;
                }
            } else if (spnrPremiumPayingOption.getSelectedItem().equals("Single")) {
                mode = "Single";
                minPremiumAmount = 100000;
            }

            // if(Double.parseDouble(premiumAmount.getText().toString()) <
            // minPremiumAmount ||
            // Double.parseDouble(premiumAmount.getText().toString()) >
            // maxPremiumAmount)
            // {
            // error="For "+mode+" mode Premium Amount should be in the range of Rs."+currencyFormat.format(minPremiumAmount)+" to Rs."+currencyFormat.format(maxPremiumAmount);
            // }

            if (Double.parseDouble(edtpremiumAmount.getText().toString()) < minPremiumAmount) {
                error = "For " + mode + " mode minimum Premium Amount is Rs. "
                        + currencyFormat.format(minPremiumAmount);
            }

        }

        if (!error.equals("")) {
            showAlert.setMessage(error);
            showAlert.setNeutralButton("OK", new OnClickListener() {

                public void onClick(DialogInterface arg0, int arg1) {
                    // TODO Auto-generated method stub

                }
            });
            showAlert.show();
            return false;
        }
        return true;
    }

    // validation of policy term
    private boolean valPolicyTerm() {
        int minPolicyTerm = 10;
        int maxPolicyTerm = Math.min(35, Math.max(10, (80 - Integer
                .parseInt(spnrageInYears.getSelectedItem().toString()))));
        String error = "";
        if (Integer.parseInt(spnrPolicyTerm.getSelectedItem().toString()) < minPolicyTerm
                || Integer
                .parseInt(spnrPolicyTerm.getSelectedItem().toString()) > maxPolicyTerm) {
            error = "Policy Term should be in between " + minPolicyTerm
                    + " years to " + maxPolicyTerm + " years";
        }

        if (!error.equals("")) {
            showAlert.setMessage(error);
            showAlert.setNeutralButton("OK",
                    new OnClickListener() {

                        public void onClick(DialogInterface arg0, int arg1) {
                            // TODO Auto-generated method stub

                        }
                    });
            showAlert.show();
            return false;
        }
        return true;
    }

    // validaion of premium paying term
    private boolean valPremiumPayingTerm() {
        String error = "";
        int minPremPayingTerm = 0, maxPremPayingTerm = 0;
        if (spnrPremiumPayingOption.getSelectedItem().equals("Regular")) {
            if (Integer.parseInt(spnrPremiumPayingTerm.getSelectedItem()
                    .toString()) != Integer.parseInt(spnrPolicyTerm
                    .getSelectedItem().toString()))
                error = "Please enter Premium Paying Term between "
                        + spnrPolicyTerm.getSelectedItem().toString() + " and "
                        + spnrPolicyTerm.getSelectedItem().toString();
        } else if (spnrPremiumPayingOption.getSelectedItem().equals("LPPT")) {
            if (Integer.parseInt(spnrPolicyTerm.getSelectedItem().toString()) >= 10 && Integer.parseInt(spnrPolicyTerm.getSelectedItem().toString()) <= 14) {
                if (Integer.parseInt(spnrPremiumPayingTerm.getSelectedItem()
                        .toString()) != 5
                        && Integer.parseInt(spnrPremiumPayingTerm
                        .getSelectedItem().toString()) != 8) {
                    error = "Please enter Premium Paying Term either 5 or 8";
                }

            } else if (Integer.parseInt(spnrPolicyTerm.getSelectedItem()
                    .toString()) >= 15) {
                if (Integer.parseInt(spnrPremiumPayingTerm.getSelectedItem()
                        .toString()) != 5
                        && Integer.parseInt(spnrPremiumPayingTerm
                        .getSelectedItem().toString()) != 8
                        && Integer.parseInt(spnrPremiumPayingTerm
                        .getSelectedItem().toString()) != 10
                        && Integer.parseInt(spnrPremiumPayingTerm
                        .getSelectedItem().toString()) != 15) {
                    error = "Please enter Premium Paying Term 5,8,10 or 15";
                }
            }
        }
        if (!error.equals("")) {
            showAlert.setMessage(error);
            showAlert.setNeutralButton("Ok",
                    new OnClickListener() {

                        public void onClick(DialogInterface arg0, int arg1) {
                            // TODO Auto-generated method stub

                        }
                    });
            showAlert.show();
            return false;
        }
        return true;
    }

    // validating no. of years since inception
    private boolean valYearsElapsedSinceInception() {
        String error = "";
        if (edtnoOfYearsElapsedSinceInception.getText().toString().equals("")) {
            error = "Enter No. of Years Elapsed Since Inception.";
        } else if (Integer.parseInt(edtnoOfYearsElapsedSinceInception.getText()
                .toString()) < 5) {
            error = "No. of Years Elapsed Since Inception should not be less than 5 Years ";
        } else if (Integer.parseInt(edtnoOfYearsElapsedSinceInception.getText()
                .toString()) > Integer.parseInt(spnrPolicyTerm
                .getSelectedItem().toString())) {
            error = "Please enter no. of Years in the range of 5 to "
                    + spnrPolicyTerm.getSelectedItem().toString() + "Years.";
        }

        if (!error.equals("")) {
            showAlert.setMessage(error);
            showAlert.setNeutralButton("OK",
                    new OnClickListener() {

                        public void onClick(DialogInterface arg0, int arg1) {
                            // TODO Auto-generated method stub

                        }
                    });
            showAlert.show();
            return false;
        }
        return true;
    }

    /************************************************************** Help Starts here ********************************************************/
    // help for premium amount
    private void updatePremiumAmount() {
        double minPremiumAmount = 0, maxPremiumAmount = 0;

        try {

            if (spnrPremiumPayingOption.getSelectedItem().equals("Regular")) {
                if (spnrPremiumFrequencyMode.getSelectedItem().equals("Yearly")) {
                    mode = "Yearly";
                    minPremiumAmount = 24000;
                } else if (spnrPremiumFrequencyMode.getSelectedItem().equals(
                        "Half Yearly")) {
                    mode = "Half Yearly";
                    minPremiumAmount = 15000;
                } else if (spnrPremiumFrequencyMode.getSelectedItem().equals(
                        "Quarterly")) {
                    mode = "Quarterly";
                    minPremiumAmount = 7500;
                } else if (spnrPremiumFrequencyMode.getSelectedItem().equals(
                        "Monthly")) {
                    mode = "Monthly";
                    minPremiumAmount = 2500;
                }
            } else if (spnrPremiumPayingOption.getSelectedItem().equals("LPPT")) {
                if (spnrPremiumFrequencyMode.getSelectedItem().equals("Yearly")) {
                    minPremiumAmount = 40000;
                } else if (spnrPremiumFrequencyMode.getSelectedItem().equals(
                        "Half Yearly")) {
                    mode = "Half Yearly";
                    minPremiumAmount = 20000;
                } else if (spnrPremiumFrequencyMode.getSelectedItem().equals(
                        "Quarterly")) {
                    mode = "Quarterly";
                    minPremiumAmount = 10000;
                } else if (spnrPremiumFrequencyMode.getSelectedItem().equals(
                        "Monthly")) {
                    mode = "Monthly";
                    minPremiumAmount = 5000;
                }
            } else if (spnrPremiumPayingOption.getSelectedItem().equals("Single")) {
                mode = "Single";
                minPremiumAmount = 100000;
            }

            help_premiumAmt.setText("Min (Rs. "
                    + currencyFormat.format(minPremiumAmount) + ")");
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    // help for policy term
    private void updatePolicyTerm() {
        try {
            int minPolicyTerm = 10;
            int maxPolicyTerm = Math.min(35, Math.max(10, (80 - Integer
                    .parseInt(spnrageInYears.getSelectedItem().toString()))));
            help_policyTerm.setText("(" + minPolicyTerm + " to "
                    + maxPolicyTerm + ")");
        } catch (Exception ignored) {
        }
    }

    // help for no of yeras elapsed since Inception
    private void updatenoOfYearsElapsedSinceInception() {
        try {
            help_noOfYearsElapsedSinceInception.setText("(5 to "
                    + spnrPolicyTerm.getSelectedItem().toString() + " years)");
        } catch (Exception ignored) {
        }
    }

   /* // help for premium paying term
    public void updatePremiumPayingTerm() {
        String error = "";
        int minPremPayingTerm = 0, maxPremPayingTerm = 0;
        if (spnrPremiumPayingOption.getSelectedItem().equals("Regular")) {
            help_premPayingTerm.setText("("
                    + spnrPolicyTerm.getSelectedItem().toString() + " to "
                    + spnrPolicyTerm.getSelectedItem().toString() + ")");
        } else if (spnrPremiumPayingOption.getSelectedItem().equals("LPPT")) {
           *//* if (Integer.parseInt(spnrPolicyTerm.getSelectedItem().toString()) == 10) {
                help_premPayingTerm.setText("(5 Or 8)");

            } else if (Integer.parseInt(spnrPolicyTerm.getSelectedItem()
                    .toString()) > 10) {
                help_premPayingTerm.setText("(5,8,10 Or 15)");
            }
*//*
            if (Integer.parseInt(spnrPolicyTerm.getSelectedItem().toString()) >= 10 && Integer.parseInt(spnrPolicyTerm.getSelectedItem().toString()) <= 14) {
                help_premPayingTerm.setText("(5 Or 8)");
            } else if (Integer.parseInt(spnrPolicyTerm.getSelectedItem()
                    .toString()) >= 15 && Integer.parseInt(spnrPolicyTerm.getSelectedItem().toString()) < 20) {
                help_premPayingTerm.setText("(5,8,10)");
            } else if (Integer.parseInt(spnrPolicyTerm.getSelectedItem()
                    .toString()) >= 20) {
                help_premPayingTerm.setText("(5,8,10 Or 15)");
            }
        }

    }
*/
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


    private void windowmessagesgin() {

        d = new Dialog(BI_RetireSmartActivity.this);
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);
        d.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.BLACK));

        d.setContentView(R.layout.window_message_signature);
        final Button btn_save = d.findViewById(R.id.save);
        final Button btn_cancel = d.findViewById(R.id.cancel);

        Button btn_takeSign = d.findViewById(R.id.takesignature);

        btn_takeSign.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                btn_save.setVisibility(View.INVISIBLE);
                Intent intent = new Intent(BI_RetireSmartActivity.this,
                        CaptureSignature.class);
                startActivityForResult(intent, SIGNATURE_ACTIVITY);

            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                d.dismiss();
            }
        });
        d.show();

    }

    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        // TODO Auto-generated method stub

        if (v.getId() == edt_bi_retire_smart_life_assured_first_name.getId()) {
            setFocusable(edt_bi_retire_smart_life_assured_middle_name);
            edt_bi_retire_smart_life_assured_middle_name.requestFocus();
        } else if (v.getId() == edt_bi_retire_smart_life_assured_middle_name
                .getId()) {
            setFocusable(edt_bi_retire_smart_life_assured_last_name);
            edt_bi_retire_smart_life_assured_last_name.requestFocus();
        } else if (v.getId() == edt_bi_retire_smart_life_assured_last_name
                .getId()) {
            setFocusable(btn_bi_retire_smart_life_assured_date);
            btn_bi_retire_smart_life_assured_date.requestFocus();
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
            clearFocusable(spnrPolicyTerm);
            setFocusable(spnrPolicyTerm);
            spnrPolicyTerm.requestFocus();
        } else if (v.getId() == edtpremiumAmount.getId()) {
            clearFocusable(edtpremiumAmount);
            clearFocusable(spnrPremiumPayingOption);
            setFocusable(spnrPremiumPayingOption);
            spnrPremiumPayingOption.requestFocus();
        } else if (v.getId() == edtnoOfYearsElapsedSinceInception.getId()) {
            setFocusable(equity_pension_fund);
            equity_pension_fund.requestFocus();
        } else if (v.getId() == equity_pension_fund.getId()) {
            setFocusable(equity_optimiser_pension_fund);
            equity_optimiser_pension_fund.requestFocus();
        } else if (v.getId() == equity_optimiser_pension_fund.getId()) {
            setFocusable(growth_pension_fund);
            growth_pension_fund.requestFocus();
        } else if (v.getId() == growth_pension_fund.getId()) {
            setFocusable(bond_pension_fund3);
            bond_pension_fund3.requestFocus();
        } else if (v.getId() == bond_pension_fund3.getId()) {
            setFocusable(money_market_pension_fund3);
            money_market_pension_fund3.requestFocus();
        } else if (v.getId() == money_market_pension_fund3.getId()) {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(money_market_pension_fund3.getWindowToken(), 0);
        }
        return true;
    }

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

    /************************************************************** Help Ends here ********************************************************/

    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        // TODO Auto-generated method stub

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

    public boolean valDoYouBackdate() {
        if (!proposer_Backdating_WishToBackDate_Policy.equals("")) {
            return true;
        } else {
            showAlert.setMessage("Please Select Do you wish to Backdate ");
            showAlert.setNeutralButton("OK",
                    new OnClickListener() {

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
                        new OnClickListener() {

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
                            new OnClickListener() {

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
        if (proposer_Is_Same_As_Life_Assured.equalsIgnoreCase("y")) {
            if (lifeAssured_Title.equals("")
                    || lifeAssured_First_Name.equals("")
                    || lifeAssured_Last_Name.equals("")) {

                showAlert.setMessage("Please Fill Name Detail For LifeAssured");
                showAlert.setNeutralButton("OK",
                        new OnClickListener() {

                            public void onClick(DialogInterface dialog,
                                                int which) {
                                if (lifeAssured_Title.equals("")) {
                                    // apply focusable method
                                    setFocusable(spnr_bi_retire_smart_life_assured_title);
                                    spnr_bi_retire_smart_life_assured_title
                                            .requestFocus();
                                } else if (lifeAssured_First_Name.equals("")) {
                                    setFocusable(edt_bi_retire_smart_life_assured_first_name);
                                    edt_bi_retire_smart_life_assured_first_name
                                            .requestFocus();
                                } else {
                                    setFocusable(edt_bi_retire_smart_life_assured_last_name);
                                    edt_bi_retire_smart_life_assured_last_name
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
                        new OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // apply focusable method
                                setFocusable(spnr_bi_retire_smart_life_assured_title);
                                spnr_bi_retire_smart_life_assured_title
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
                        new OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // apply focusable method
                                setFocusable(spnr_bi_retire_smart_life_assured_title);
                                spnr_bi_retire_smart_life_assured_title
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
                        new OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // apply focusable method
                                setFocusable(spnr_bi_retire_smart_life_assured_title);
                                spnr_bi_retire_smart_life_assured_title
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
                            new OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    if (lifeAssured_Title.equals("")) {
                                        // apply focusable method
                                        setFocusable(spnr_bi_retire_smart_life_assured_title);
                                        spnr_bi_retire_smart_life_assured_title
                                                .requestFocus();
                                    } else if (lifeAssured_First_Name.equals("")) {

                                        edt_bi_retire_smart_life_assured_first_name
                                                .requestFocus();
                                    } else {
                                        edt_bi_retire_smart_life_assured_last_name
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
                            new OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    // apply focusable method
                                    setFocusable(spnr_bi_retire_smart_life_assured_title);
                                    spnr_bi_retire_smart_life_assured_title
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
                            new OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    // apply focusable method
                                    setFocusable(spnr_bi_retire_smart_life_assured_title);
                                    spnr_bi_retire_smart_life_assured_title
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
                            new OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    // apply focusable method
                                    setFocusable(spnr_bi_retire_smart_life_assured_title);
                                    spnr_bi_retire_smart_life_assured_title
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

    public boolean valChoiceOption() {
        String error = "";
        if (spnr_bi_plan_option_choice.getSelectedItem().toString().equals("Select Plan Option")) {
            error = "Please Select Plan Option";
        }

        if (!error.equals("")) {
            showAlert.setMessage(error.toString());
            showAlert.setNeutralButton("OK", new OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    spnr_bi_plan_option_choice.requestFocus();
                }
            });
            showAlert.show();

            return false;
        } else {
            return true;
        }
    }

    public boolean valMatuirtyOption() {
        String error = "";
        if (bi_retire_smart_Matuirty_option.equals("")) {
            error = "Please Select Maturity Option";
        }

        if (!error.equals("")) {
            showAlert.setMessage(error.toString());
            showAlert.setNeutralButton("OK", new OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    spnr_bi_Matuirty_option.requestFocus();
                }
            });
            showAlert.show();

            return false;
        } else {
            return true;
        }
    }

    public boolean valMatuirtyFreuqency() {
        String error = "";
        if (bi_retire_smart_Matuirty_frequency.equals("")) {
            error = "Please Select Maturity Frequency";
        }

        if (!error.equals("")) {
            showAlert.setMessage(error.toString());
            showAlert.setNeutralButton("OK", new OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    spnr_bi_Matuirty_frequency.requestFocus();
                }
            });
            showAlert.show();

            return false;
        } else {
            return true;
        }
    }

    public boolean valTotalAllocation() {

        String error = "";
        double percent_equity_pension_fund, percent_equity_optimiser_pension_fund, percent_growth_pension_fund,
                percent_bond_pension_fund3, percent_money_market_pension_fund3;

        if (!spnr_bi_plan_option_choice.getSelectedItem().toString().equals("Smart Choice Plan")) {
            return true;
        } else {
            if (!equity_pension_fund.getText().toString().equals(""))
                percent_equity_pension_fund = Double.parseDouble(equity_pension_fund.getText().toString());
            else
                percent_equity_pension_fund = 0.0;

            if (!equity_optimiser_pension_fund.getText().toString().equals(""))
                percent_equity_optimiser_pension_fund = Double.parseDouble(equity_optimiser_pension_fund.getText().toString());
            else
                percent_equity_optimiser_pension_fund = 0.0;

            if (!growth_pension_fund.getText().toString().equals(""))
                percent_growth_pension_fund = Double.parseDouble(growth_pension_fund.getText().toString());
            else
                percent_growth_pension_fund = 0.0;

            if (!bond_pension_fund3.getText().toString().equals(""))
                percent_bond_pension_fund3 = Double.parseDouble(bond_pension_fund3.getText().toString());
            else
                percent_bond_pension_fund3 = 0.0;

            if (!money_market_pension_fund3.getText().toString().equals(""))
                percent_money_market_pension_fund3 = Double.parseDouble(money_market_pension_fund3.getText().toString());
            else
                percent_money_market_pension_fund3 = 0.0;

            if ((percent_equity_pension_fund + percent_equity_optimiser_pension_fund + percent_growth_pension_fund
                    + percent_bond_pension_fund3 + percent_money_market_pension_fund3) != 100)
                error = "Total sum of % to be invested for all fund should be equal to 100%";

            if (!error.equals("")) {
                showAlert.setMessage(error.toString());
                showAlert.setNeutralButton("OK", new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        equity_pension_fund.requestFocus();
                    }
                });
                showAlert.show();

                return false;
            } else {
                return true;
            }


        }

    }

    public boolean valDob() {

        if (proposer_Is_Same_As_Life_Assured.equalsIgnoreCase("y")) {

            if (lifeAssured_date_of_birth.equals("")
                    || lifeAssured_date_of_birth
                    .equalsIgnoreCase("select Date")) {
                showAlert
                        .setMessage("Please Select Valid Date Of Birth For LifeAssured");
                showAlert.setNeutralButton("OK",
                        new OnClickListener() {

                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // apply focusable method
                                setFocusable(btn_bi_retire_smart_life_assured_date);
                                btn_bi_retire_smart_life_assured_date
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

                case 5:

                    final_age = Integer.toString(age);
                    if (final_age.contains("-")) {
                        commonMethods.BICommonDialog(context, "Please fill Valid Bith Date");
                    } else {

                        if (30 <= age && age <= 70) {

                            btn_bi_retire_smart_life_assured_date.setText(date);

                            spnrageInYears.setSelection(
                                    getIndex(spnrageInYears, final_age), false);
                            lifeAssured_date_of_birth = getDate1(date + "");
                            clearFocusable(btn_bi_retire_smart_life_assured_date);
                            setFocusable(edt_proposerdetail_basicdetail_contact_no);
                            edt_proposerdetail_basicdetail_contact_no
                                    .requestFocus();

                            /*
                             * setFocusable(spnrPlan); spnrPlan.requestFocus();
                             */

                            // setFocusable(edt_premiumAmt);
                            // edt_premiumAmt.requestFocus();
                        } else {
                            commonMethods.BICommonDialog(context, "Minimum Age should be 30"
                                    + "yrs and Maximum Age should be 70"
                                    + " yrs For LifeAssured");
                            btn_bi_retire_smart_life_assured_date
                                    .setText("Select Date");
                            lifeAssured_date_of_birth = "";
                            clearFocusable(btn_bi_retire_smart_life_assured_date);
                            setFocusable(btn_bi_retire_smart_life_assured_date);
                            btn_bi_retire_smart_life_assured_date.requestFocus();
                        }
                    }
                    break;

                case 6:

                default:
                    break;
            }
        }

        if (proposer_Backdating_WishToBackDate_Policy.equalsIgnoreCase("y")
                && !proposer_Backdating_BackDate.equals("")) {

            int Proposerage = calculateMyAge(mYear, Integer.parseInt(mont),
                    Integer.parseInt(day));

            String str_final_age = Integer.toString(Proposerage);
            spnrageInYears.setSelection(
                    getIndex(spnrageInYears, str_final_age), false);

        }
    }

    private int calculateMyAge(int year, int month, int day) {
        Calendar nowCal = new GregorianCalendar(year, month, day);

        String[] ProposerDob = getDate(lifeAssured_date_of_birth).split("/");
        // int age = Integer.parseInt(ProposerDob[3]) -
        // birthCal.get(Calendar.YEAR);

        int age = nowCal.get(Calendar.YEAR) - Integer.parseInt(ProposerDob[2]);

        boolean isMonthGreater = Integer.parseInt(ProposerDob[1]) >= nowCal
                .get(Calendar.MONTH);

        boolean isMonthSameButDayGreater = Integer.parseInt(ProposerDob[1]) == nowCal
                .get(Calendar.MONTH)
                && Integer.parseInt(ProposerDob[1]) > nowCal
                .get(Calendar.DAY_OF_MONTH);

        if (isMonthGreater || isMonthSameButDayGreater) {
            age = age - 1;
        }
        return age;
    }

    public void onClickLADob(View v) {
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

    private void Date() {
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }

    private void createPdf() {
        try {

            ParseXML prsObj = new ParseXML();

            String maturityAge = prsObj.parseXmlTag(output, "maturityAge");
            String annPrem = prsObj.parseXmlTag(output, "annPrem");
            String redInYieldMat = prsObj.parseXmlTag(output, "redInYieldMat");
            String redInYieldNoYr = prsObj
                    .parseXmlTag(output, "redInYieldNoYr");
            String sumAssured = prsObj.parseXmlTag(output, "sumAssured");
            String totFYPrem = prsObj.parseXmlTag(output, "totFyPrem");
            String fundVal4 = prsObj.parseXmlTag(output, "fundVal4");
            String fundVal8 = prsObj.parseXmlTag(output, "fundVal8");

            String netYield4Pr = prsObj.parseXmlTag(output, "netYeild4pr");
            String netYield8Pr = prsObj.parseXmlTag(output, "netYeild8pr");
            String annuityPay4pa = prsObj.parseXmlTag(output, "annuityPay4pa");
            String annuityPay8pa = prsObj.parseXmlTag(output, "annuityPay8pa");
            String annuityRates = prsObj.parseXmlTag(output, "annuityRates");

            int policyterm = Integer.parseInt(policyTermStr);
            System.out.println(policyterm + "  " + maturityAge + "  " + annPrem
                    + " " + sumAssured);

            Font headerBold = new Font(Font.FontFamily.TIMES_ROMAN, 8,
                    Font.BOLD);
            Font small_bold = new Font(Font.FontFamily.TIMES_ROMAN, 6,
                    Font.BOLD);
            Font small_normal2 = new Font(Font.FontFamily.TIMES_ROMAN, 6,
                    Font.NORMAL);
            Font small_bold1 = new Font(Font.FontFamily.TIMES_ROMAN, 8,
                    Font.BOLD);
            Font small_bold2 = new Font(Font.FontFamily.TIMES_ROMAN, 4,
                    Font.BOLD);
            Font small_normal = new Font(Font.FontFamily.TIMES_ROMAN, 6,
                    Font.NORMAL);
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
            image.scalePercent(50f);
            image.setAlignment(Element.ALIGN_LEFT);
            image.scaleToFit(80, 50);
            document.open();

            PdfPTable table = new PdfPTable(1);
            table.setWidths(new float[]{13f});
            table.setWidthPercentage(100);
            table.getDefaultCell().setPadding(15);

            PdfPCell cell;

            cell = new PdfPCell(new Phrase("\n", headerBold));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);


            cell = new PdfPCell(new Phrase("\n", headerBold));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setRowspan(4);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(image);
            cell.setRowspan(3);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("SBI Life Insurance Co. Ltd",
                    headerBold));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(
                    new Phrase(
                            "Registered & Corporate Office: 'Natraj', M.V.Road and Western Express Highway Junction, Andheri (East), Mumbai - 400069",
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

            cell = new PdfPCell(new Phrase("Customised Benefit Illustration (CBI) ",
                    headerBold));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);

            table.addCell(cell);

            cell = new PdfPCell(new Phrase("SBI Life - Retire Smart (111L094V02) ",
                    headerBold));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);

            table.addCell(cell);

            cell = new PdfPCell(
                    new Phrase("An Individual, Unit-linked, Non-Participating, Pension Savings Product",
                            headerBold));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);

            table.addCell(cell);

            cell = new PdfPCell(
                    new Phrase("IN THIS POLICY, THE INVESTMENT RISK IN INVESTMENT PORTFOLIO IS BORNE BY THE POLICYHOLDER.",
                            headerBold));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);

            table.addCell(cell);

            cell = new PdfPCell(new Phrase("\n", normal));
            cell.setColspan(3);
            cell.setBorder(Rectangle.BOTTOM);
            cell.setBorderWidth(1.2f);
            table.addCell(cell);

            if (proposer_Is_Same_As_Life_Assured.equalsIgnoreCase("y")) {
                name_of_proposer = name_of_life_assured;
            }
            PdfPTable table_proposer_name = new PdfPTable(4);
            // float[] columnWidths_table_proposer_name = { 2f, 2f, 1f, 2f };
            // table_proposer_name.setWidths(columnWidths_table_proposer_name);
            table_proposer_name.setWidthPercentage(100);

            PdfPCell ProposalNumber_cell_1 = new PdfPCell(new Paragraph(
                    "Proposal No.:", small_normal));
            PdfPCell ProposalNumber_cell_2 = new PdfPCell(new Paragraph(
                    QuatationNumber, normal1_bold));
            ProposalNumber_cell_2.setHorizontalAlignment(Element.ALIGN_CENTER);
            PdfPCell NameofProposal_cell_3 = new PdfPCell(new Paragraph(
                    "Channel/Intermediary", small_normal));
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

            // table.setWidths(new float[] { 3f, 7f, 4f });
            // table.setWidthPercentage(100);
            // table.getDefaultCell().setPadding(15);
            //
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

            LineSeparator ls = new LineSeparator();
            PdfPTable BI_Pdftable2 = new PdfPTable(1);
            BI_Pdftable2.setWidthPercentage(100);
            // CR_table6.setWidths(columnWidths_2);
            PdfPCell BI_Pdftable2_cell1 = new PdfPCell(
                    new Paragraph(
                            "Insurance Regulatory & Development Authority of India (IRDAI) requires all life insurance companies operating in India to provide official illustrations to their customers. The illustrations are based on the investment rates of return set by the Life Insurance Council (constituted under Section 64C(a) of the Insurance Act 1938) and is not intended to reflect the actual investment returns achieved or which may be achieved in future by SBI life Insurance Company Limited.  All life insurance companies use the same rates in their benefit illustrations.",
                            small_normal));

            BI_Pdftable2_cell1.setPadding(5);

            BI_Pdftable2.addCell(BI_Pdftable2_cell1);


            PdfPTable BI_Pdftable3 = new PdfPTable(1);
            BI_Pdftable3.setWidthPercentage(100);
            PdfPCell BI_Pdftable3_cell1 = new PdfPCell(
                    new Paragraph(
                            "The main objective of the illustration is that the client is able to appreciate the features of the product and the flow of benefits in different circumstances with some level of quantification. For further information on the product, its benefits and applicable charges please refer to the sales brochure and/or policy document.",
                            small_normal));

            BI_Pdftable3_cell1.setPadding(5);

            BI_Pdftable3.addCell(BI_Pdftable3_cell1);


            PdfPTable BI_Pdftable4 = new PdfPTable(1);
            BI_Pdftable4.setWidthPercentage(100);
            PdfPCell BI_Pdftable4_cell1 = new PdfPCell(
                    new Paragraph(
                            "Some benefits are guaranteed and some benefits are variable with returns based on the future fund performance of SBI Life Insurance Company Limited. If your policy offers guaranteed returns then the same will be clearly marked as “guaranteed” in the illustration table. If your policy offers variable returns then the illustration will show two different rates of assumed future investment returns. These assumed rates of return are not guaranteed and they are not the upper or lower limits of what you might get back, as the value of your policy is dependent on a number of factors including future fund investment performance.",
                            small_normal));

            BI_Pdftable4_cell1.setPadding(5);

            BI_Pdftable4.addCell(BI_Pdftable4_cell1);
            //document.add(BI_Pdftable4);

            Paragraph para3 = new Paragraph(
                    "IN THIS POLICY, THE INVESTMENT RISK IN INVESTMENT PORTFOLIO IS BORNE BY THE POLICYHOLDER.",
                    small_bold);
            para3.setAlignment(Element.ALIGN_CENTER);
            Paragraph para4 = new Paragraph(
                    "Insurance Regulatory & Development Authority of India (IRDAI) requires all life insurance companies operating in India to provide official illustrations to their customers. The illustrations are based on the investment rates of return set by the Life Insurance Council (constituted under Section 64C(a) of the Insurance Act 1938) and is not intended to reflect the actual investment returns achieved or which may be achieved in future by SBI life Insurance Company Limited. The two rates of investment return currently declared by the Life Insurance Council are 4% and 8% per annum.",
                    normal1);
            para4.setAlignment(Element.ALIGN_LEFT | Element.ALIGN_JUSTIFIED);
            Paragraph para5 = new Paragraph(
                    "The main objective of the illustration is that the client is able to appreciate the features of the product and the flow of benefits in different circumstances with some level of quantification. For further information on the product, its benefits and applicable charges please refer to the sales brochure and/or policy document. Further information will also be available on request",
                    normal1);
            para5.setAlignment(Element.ALIGN_LEFT | Element.ALIGN_JUSTIFIED);
            Paragraph para6 = new Paragraph(
                    "Some benefits are guaranteed and some benefits are variable with returns based on the future performance of SBI Life Insurance Company Limited. If your policy offers guaranteed returns then the same will be clearly marked as guaranteed in the illustration table on this page. If your policy offers variable returns then the illustration on this page will show two different rates of assumed future investment returns. These assumed rates of return are not guaranteed and they are not the upper or lower limits of what you might get back, as the value of your policy is dependent on a number of factors including future investment performance.",
                    normal1);
            para6.setAlignment(Element.ALIGN_LEFT | Element.ALIGN_JUSTIFIED);
            Paragraph new_line = new Paragraph("\n");

            PdfPTable BI_Pdftablefundtable = new PdfPTable(1);
            BI_Pdftablefundtable.setWidthPercentage(100);
            PdfPCell BI_Pdftablefundtable_cell = new PdfPCell(new Paragraph(
                    "Proposer, Life Assured and Plan Details", small_bold));

            BI_Pdftablefundtable_cell
                    .setBackgroundColor(BaseColor.LIGHT_GRAY);

            BI_Pdftablefundtable_cell
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_Pdftablefundtable_cell.setPadding(5);

            BI_Pdftablefundtable.addCell(BI_Pdftablefundtable_cell);
            // inputTable here -1

            PdfPTable input_table = new PdfPTable(2);
            input_table.setWidths(new float[]{3f, 2f});
            input_table.setWidthPercentage(80f);
            input_table.setHorizontalAlignment(Element.ALIGN_LEFT);

            cell = new PdfPCell(new Phrase("Proposer, Life Assured and Plan Details ", normal1_bold));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            //cell.setBorder(Rectangle.NO_BORDER);
            cell.setColspan(2);
            // input_table.addCell(cell);

            //1st row
            cell = new PdfPCell(new Phrase("Name of Proposer", normal1));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            // input_table.addCell(cell);

            cell = new PdfPCell(new Phrase(name_of_life_assured, normal1));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            // input_table.addCell(cell);

            cell = new PdfPCell(new Phrase("Age of Proposer", normal1));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            // input_table.addCell(cell);

            cell = new PdfPCell(new Phrase(age_entry, normal1));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            //input_table.addCell(cell);

            cell = new PdfPCell(new Phrase("Name of the Life Assured", normal1));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            input_table.addCell(cell);

            cell = new PdfPCell(new Phrase(name_of_life_assured, normal1));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            input_table.addCell(cell);

            // 2nd  row
            cell = new PdfPCell(new Phrase("Age of the  Life Assured  ",
                    normal1));
            //cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            input_table.addCell(cell);

            cell = new PdfPCell(new Phrase(age_entry + "", normal1));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            // cell.setBorder(Rectangle.NO_BORDER);
            input_table.addCell(cell);

            // 3rd row
            cell = new PdfPCell(new Phrase("Vesting Age  ", normal1));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            //cell.setBorder(Rectangle.NO_BORDER);
            input_table.addCell(cell);

            cell = new PdfPCell(new Phrase(maturityAge + " Years", normal1));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            //  cell.setBorder(Rectangle.NO_BORDER);
            input_table.addCell(cell);

            //4th
            cell = new PdfPCell(new Phrase("Sum Assured ", normal1));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            //cell.setBorder(Rectangle.NO_BORDER);
            input_table.addCell(cell);

            cell = new PdfPCell(new Phrase("0", normal1));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            //  cell.setBorder(Rectangle.NO_BORDER);
            input_table.addCell(cell);

            cell = new PdfPCell(new Phrase("Premium Payment Term", normal1));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            //cell.setBorder(Rectangle.NO_BORDER);
            input_table.addCell(cell);

            PremPayingOption = prsObj.parseXmlTag(input, "premPayingOption");
            premium_paying_term = prsObj.parseXmlTag(input, "premPayingTerm");


            if (PremPayingOption.equalsIgnoreCase("Single")) {
                cell = new PdfPCell(new Phrase("One time at the inception of the policy", normal1));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                //  cell.setBorder(Rectangle.NO_BORDER);
                input_table.addCell(cell);

            } else if (PremPayingOption.equalsIgnoreCase("Regular")) {
                cell = new PdfPCell(new Phrase("Same as Policy Term", normal1));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                //  cell.setBorder(Rectangle.NO_BORDER);
                input_table.addCell(cell);

            } else if (PremPayingOption.equalsIgnoreCase("LPPT")) {
                cell = new PdfPCell(new Phrase(premium_paying_term, normal1));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                //  cell.setBorder(Rectangle.NO_BORDER);
                input_table.addCell(cell);

            }


            // 5 row
            cell = new PdfPCell(new Phrase("Life Assured Gender : ", normal1));
            // cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            //input_table.addCell(cell);

            cell = new PdfPCell(new Phrase(gender, normal1));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            //cell.setBorder(Rectangle.NO_BORDER);
            //input_table.addCell(cell);

            // 6th row
            cell = new PdfPCell(new Phrase("Policy Term  ", normal1));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            // cell.setBorder(Rectangle.NO_BORDER);
            input_table.addCell(cell);

            cell = new PdfPCell(new Phrase(policyTermStr + " Years", normal1));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            // cell.setBorder(Rectangle.NO_BORDER);
            input_table.addCell(cell);

            // 5 row
            cell = new PdfPCell(new Phrase("Yearly Premium* : ", normal1));
            // cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            //input_table.addCell(cell);

            cell = new PdfPCell(new Phrase("Rs. "
                    + currencyFormat.format(Double.parseDouble(annPrem)),
                    normal1));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            //cell.setBorder(Rectangle.NO_BORDER);
            // input_table.addCell(cell);

            // 6 row
            cell = new PdfPCell(new Phrase("Premium Payment Term : ", normal1));
            //cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            //input_table.addCell(cell);

            cell = new PdfPCell(new Phrase(premium_paying_term + " Years",
                    normal1));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            // cell.setBorder(Rectangle.NO_BORDER);
            // input_table.addCell(cell);

            // 7 row
            cell = new PdfPCell(new Phrase("Amount of Installment Premium", normal1));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            input_table.addCell(cell);

            premium = ((int) Double.parseDouble(prsObj.parseXmlTag(input,
                    "premiumAmount"))) + "";

            cell = new PdfPCell(new Phrase(""
                    + currencyFormat.format(Double.parseDouble(premium)),
                    normal1));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            input_table.addCell(cell);
            ////////////////////////////////////////////////////////
            cell = new PdfPCell(new Phrase("Mode / Frequency of Premium Payment ", normal1));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            // cell.setBorder(Rectangle.NO_BORDER);
            input_table.addCell(cell);

            if (PremPayingOption.equalsIgnoreCase("Single")) {
                cell = new PdfPCell(new Phrase("One time at the inception of the policy", normal1));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                //cell.setBorder(Rectangle.NO_BORDER);
                input_table.addCell(cell);

            } else if (PremPayingOption.equalsIgnoreCase("Regular")) {
                cell = new PdfPCell(new Phrase(premFreq, normal1));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                //cell.setBorder(Rectangle.NO_BORDER);
                input_table.addCell(cell);
            } else if (PremPayingOption.equalsIgnoreCase("LPPT")) {
                cell = new PdfPCell(new Phrase(premFreq, normal1));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                //cell.setBorder(Rectangle.NO_BORDER);
                input_table.addCell(cell);
            }


            //////////////////////////////////////////////////
            cell = new PdfPCell(new Phrase(" Total First Year Premium ", normal1));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            // cell.setBorder(Rectangle.NO_BORDER);
            input_table.addCell(cell);

            cell = new PdfPCell(new Phrase("Rs. "
                    + currencyFormat.format(Double.parseDouble(totFYPrem)),
                    normal1));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            //cell.setBorder(Rectangle.NO_BORDER);
            input_table.addCell(cell);

            cell = new PdfPCell(new Phrase(
                    "Rate of Applicable Taxes  ", normal1));

            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            input_table.addCell(cell);

            cell = new PdfPCell(new Phrase(
                    "18% ", normal1_bold));

            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            input_table.addCell(cell);
            // fund table here -3

            PdfPTable fund_table = new PdfPTable(4);
            fund_table.setWidths(new float[]{6f, 1.9f, 2f, 2f});
            fund_table.setWidthPercentage(80f);
            fund_table.setHorizontalAlignment(Element.ALIGN_LEFT);

            // 1st row
            cell = new PdfPCell(new Phrase(
                    "Rate of Applicable Taxes : ", normal1));
            cell.setColspan(2);

            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            fund_table.addCell(cell);

            cell = new PdfPCell(new Phrase(
                    "18% ", normal1_bold));
            cell.setColspan(2);

            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            fund_table.addCell(cell);

            // 2 row
            cell = new PdfPCell(new Phrase("Plan Option", normal1));
            cell.setColspan(2);

            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            fund_table.addCell(cell);

            cell = new PdfPCell(new Phrase("0", normal1));
            cell.setColspan(2);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            fund_table.addCell(cell);


//            document.add(para_img_logo_after_space_1);


            // first year premium Table here -2

            PdfPTable FyPrem_table = new PdfPTable(2);
            FyPrem_table.setWidths(new float[]{3.2f, 2.5f});
            FyPrem_table.setWidthPercentage(80f);
            FyPrem_table.setHorizontalAlignment(Element.ALIGN_CENTER);

            // 1st row
            cell = new PdfPCell(
                    new Phrase("Premium Payment Option : ", normal1));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            // FyPrem_table.addCell(cell);
            // cell = new PdfPCell(new Phrase(PremPayingOption, normal1));
            // cell = new PdfPCell(new
            // Phrase("Rs. "+currencyFormat.format(Double.parseDouble(totFYPrem)),normal1));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            //FyPrem_table.addCell(cell);

            // 2nd row
            cell = new PdfPCell(new Phrase("Total First Year Premium (RP) : ",
                    normal1));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            // FyPrem_table.addCell(cell);
            cell = new PdfPCell(new Phrase("Rs. "
                    + currencyFormat.format(Double.parseDouble(totFYPrem)),
                    normal1));
            // cell = new PdfPCell(new
            // Phrase("Rs. "+currencyFormat.format(Double.parseDouble(totFYPrem)),normal1));
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
            cell = new PdfPCell(new Phrase("Reduction in Yield @ 8%", normal1));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            //reductionInYeild_table.addCell(cell);

            // 2 row
            cell = new PdfPCell(new Phrase("" + no_of_year_elapsed, normal1));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            // reductionInYeild_table.addCell(cell);
            cell = new PdfPCell(new Phrase(redInYieldNoYr + "%", normal1));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            //reductionInYeild_table.addCell(cell);

            // reduction in yeild for maturity Table here -4b

            PdfPTable reductionInYeild_maturity_table = new PdfPTable(2);
            reductionInYeild_maturity_table.setWidths(new float[]{3.5f, 2f});
            reductionInYeild_maturity_table.setWidthPercentage(80f);
            reductionInYeild_maturity_table
                    .setHorizontalAlignment(Element.ALIGN_RIGHT);

            // 1st row
            cell = new PdfPCell(new Phrase("Maturity at", normal1));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            // reductionInYeild_maturity_table.addCell(cell);
            cell = new PdfPCell(new Phrase("Reduction in Yield @ 8%", normal1));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            //reductionInYeild_maturity_table.addCell(cell);

            // 2 row
            cell = new PdfPCell(new Phrase("" + policyterm + " Years", normal1));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            // reductionInYeild_maturity_table.addCell(cell);
            cell = new PdfPCell(new Phrase(redInYieldMat + "%", normal1));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            // reductionInYeild_maturity_table.addCell(cell);

            // main table of 4 tables
            PdfPTable main_table = new PdfPTable(4);
            main_table.setWidths(new float[]{4f, 2.8f, 5f, 3.4f});
            main_table.setWidthPercentage(100);
            main_table.getDefaultCell().setPadding(20f);

            cell = new PdfPCell(input_table);
            // cell.setRowspan(4);
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
            //main_table.addCell(cell);


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
                            "Net yield mentioned corresponds to the gross investment return of 8% p.a., net of all charges but does not consider guarantee charges. It demonstrates the impact of charges exclusive of taxes on the net yield.",

                            small_normal));

            BI_Pdftable102_cell1.setPadding(5);

            BI_Pdftable102.addCell(BI_Pdftable102_cell1);

            PdfPTable BI_Pdftable103 = new PdfPTable(1);
            BI_Pdftable103.setWidthPercentage(100);
            PdfPCell BI_Pdftable103_cell1 = new PdfPCell(
                    new Paragraph(
                            "The actual returns can vary depending on the performance of the chosen fund. The investment risk in this policy is borne by the policyholder, hence, for more details on terms and conditions please read the sales literature carefully.",

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


            Paragraph note = new Paragraph("Notes :", normal_bold);
            note.setAlignment(Element.ALIGN_LEFT);
            // notes Table here

            PdfPTable table21 = new PdfPTable(2);
            table21.setWidths(new float[]{0.3f, 9f});
            table21.setWidthPercentage(100);
            table21.setHorizontalAlignment(Element.ALIGN_LEFT);

            // 1st note
            cell = new PdfPCell(new Phrase("1)", normal1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table21.addCell(cell);
            cell = new PdfPCell(
                    new Phrase(
                            "Refer the sales literature for explanation of terms in this illustration.",
                            normal1));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT
                    | Element.ALIGN_JUSTIFIED);
            cell.setBorder(Rectangle.NO_BORDER);
            table21.addCell(cell);

            // 2 note
            cell = new PdfPCell(new Phrase("2)", normal1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table21.addCell(cell);
            cell = new PdfPCell(
                    new Phrase(
                            "Please read this benefit illustration in conjunction with Sales Brochure and the Policy Document to understand all Terms, Conditions & Exclusions carefully.",
                            normal1));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            table21.addCell(cell);

            // 3 note
            cell = new PdfPCell(new Phrase("3)", normal1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table21.addCell(cell);
            cell = new PdfPCell(
                    new Phrase(
                            "Kindly note that above is only an illustration and does not in any way create any rights and/or obligations. The actual experience on the contract may be different from what is illustrated. The non-guaranteed low and high rate mentioned above relate to assumed investment returns at different rates and may vary depending upon market conditions. For more details on risk factors, terms and conditions please read sales brochure carefully.",
                            normal1));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            table21.addCell(cell);
            // 4 note
            cell = new PdfPCell(new Phrase("4)", normal1));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table21.addCell(cell);
            cell = new PdfPCell(
                    new Phrase(
                            "The unit values may go up as well as down and past performance is no indication of future performance on the part of SBI Life Insurance Co. Ltd. We would request you to appreciate the associated risk under this plan vis-à-vis the likely future returns before taking your investment decision.",
                            normal1));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            table21.addCell(cell);
            // 5 note
            cell = new PdfPCell(new Phrase("5)", normal1));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table21.addCell(cell);
            cell = new PdfPCell(
                    new Phrase(
                            "It is assumed that the policy is in force throughout the term.",
                            normal1));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            table21.addCell(cell);
            // 6 note
            cell = new PdfPCell(new Phrase("6)", normal1));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table21.addCell(cell);
            cell = new PdfPCell(
                    new Phrase(
                            "Fund management charge is based on the plan option",
                            normal1));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            table21.addCell(cell);

            // 7 note
            cell = new PdfPCell(new Phrase("7)", normal1));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table21.addCell(cell);
            cell = new PdfPCell(
                    new Phrase(
                            "Surrender Value equals the Fund Value at the end of the year minus Discontinuance Charges. Surrender value is available on or after 5th policy anniversary.",
                            normal1));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            table21.addCell(cell);

            // 8 note
            cell = new PdfPCell(new Phrase("8)", normal1));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table21.addCell(cell);
            cell = new PdfPCell(
                    new Phrase(
                            "Acceptance of proposal is subject to Underwriting decision. ",
                            normal1));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT
                    | Element.ALIGN_JUSTIFIED);
            cell.setBorder(Rectangle.NO_BORDER);
            table21.addCell(cell);

            // 9 note
            cell = new PdfPCell(new Phrase("9)", normal1));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table21.addCell(cell);

            cell = new PdfPCell(
                    new Phrase(
                            "Applicable Taxes (including surcharge/cess etc), at the rate notified by the Central Government/ State Government / Union Territories of India from time to time and as per the provisions of the prevalent tax laws will be payable on premium/ or any other charges as per the product features.",
                            normal1));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table21.addCell(cell);

            // 10 note
            cell = new PdfPCell(new Phrase("10)", normal1));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            // table21.addCell(cell);
            cell = new PdfPCell(
                    new Phrase(
                            "This policy provides guaranteed death benefit of 105% of the total premiums received upto the date of death less partial withdrawals if any in the last 2 years immediately preceding the death of the Life assured.",
                            normal1));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            //  table21.addCell(cell);

            // 11 note
            cell = new PdfPCell(new Phrase("11)", normal1));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            //table21.addCell(cell);
            cell = new PdfPCell(
                    new Phrase(
                            "This policy provides guaranteed addition of 10% of annual premium on every policy anniversary from the 15th Anniversary till the time of death or maturity/ vesting, provided the policy is in force.",
                            normal1));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            //table21.addCell(cell);

            // 12 note
            cell = new PdfPCell(new Phrase("12)", normal1));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            //table21.addCell(cell);
            cell = new PdfPCell(
                    new Phrase(
                            "This policy provides addition of units at the time of death or maturity/ vesting of 1.50% of Fund Value.",
                            normal1));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            //table21.addCell(cell);

            // 13 note
            cell = new PdfPCell(new Phrase("13)", normal1));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            // table21.addCell(cell);
            cell = new PdfPCell(
                    new Phrase(
                            "Net Yields have been calculated after applying all the charges (except applicable taxes, education cess, guarantee charge, if any).",
                            normal1));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            //table21.addCell(cell);

            // 14 note
            cell = new PdfPCell(new Phrase("14)", normal1));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            // table21.addCell(cell);
            cell = new PdfPCell(
                    new Phrase(
                            "This illustration has been prepared in compliance with IRDAI (Linked Insurance Products) Regulations, 2013.",
                            normal1));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            //table21.addCell(cell);

            // 15 note
            cell = new PdfPCell(new Phrase("15)", normal1));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            // table21.addCell(cell);
            cell = new PdfPCell(
                    new Phrase(
                            "Col (27) gives the commission payable to the agent/ broker in respect of the base policy .This amount is included in total charges mentioned in col (5) or col (9)",
                            normal1));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            // table21.addCell(cell);

            // 16 note
            cell = new PdfPCell(new Phrase("16)", normal1));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            //table21.addCell(cell);
            cell = new PdfPCell(
                    new Phrase(
                            "The annuity at the time of vesting will be provided by the above Insurance Company only.",
                            normal1));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            //table21.addCell(cell);

            // 17 note
            cell = new PdfPCell(new Phrase("17)", normal1));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            //table21.addCell(cell);
            cell = new PdfPCell(
                    new Phrase(
                            "The actual annuity amount receivable depends on the prevailing annuity rates at the time of vesting.",
                            normal1));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            //table21.addCell(cell);

            PdfPTable table3 = new PdfPTable(3);
            table3.setWidths(new float[]{0.5f, 3f, 9f});
            table3.setWidthPercentage(80);
            table3.setHorizontalAlignment(Element.ALIGN_LEFT);

            cell = new PdfPCell(new Phrase("Definition of Various Charges:",
                    normal1_bold));
            cell.setColspan(3);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            table3.addCell(cell);
            cell = new PdfPCell(new Phrase("", normal1));
            cell.setRowspan(5);
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table3.addCell(cell);

            // 1st charges row
            cell = new PdfPCell(
                    new Phrase("1)Policy Administration Charges:", normal1));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table3.addCell(cell);
            cell = new PdfPCell(
                    new Phrase(
                            "a charge of a fixed sum which is applied at the beginning of each policy month by cancelling units for equivalent amount, deducted for maintaining the policy.",
                            normal1));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table3.addCell(cell);

            // 2 charges row
            cell = new PdfPCell(new Phrase("2)Premium Allocation Charges:",
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
            cell = new PdfPCell(new Phrase("3)Fund Management Charge :", normal1));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table3.addCell(cell);
            cell = new PdfPCell(
                    new Phrase(
                            "is the deduction made from the fund at a stated percentage before the computation of the NAV of the fund.",
                            normal1));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table3.addCell(cell);

            // 4 charges row
            cell = new PdfPCell(new Phrase("Guarantee Charges", normal1));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            // table3.addCell(cell);
            cell = new PdfPCell(
                    new Phrase(
                            "are the charges recovered for providing minimum guaranteed Maturity Value and are deducted before the computation of the NAV of the fund.",
                            normal1));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            //table3.addCell(cell);

            /*
             * Paragraph para7 = new Paragraph(
             * "I,_____________________________________________, having received the information with respect to the above, have understood the above statement before entering into the contract."
             * , normal1_bold);
             */

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
            document.add(new_line);
            document.add(BI_Pdftable2);
            document.add(BI_Pdftable3);
            //  document.add(BI_Pdftable4);
            // document.add(ls);
            // document.add(para3);
            // document.add(para4);
            // document.add(para5);
            // document.add(para6);
            document.add(new_line);
            document.add(BI_Pdftablefundtable);
            document.add(ls);
            document.add(new_line);

            document.add(main_table);
            document.add(new_line);

            if (bi_retire_smart_plan_option.equals("Advantage Plan")) {
                PdfPTable BI_PdftableRate = new PdfPTable(4);
                BI_PdftableRate.setWidthPercentage(100);

                PdfPCell BI_PdftableRate_cell1 = new PdfPCell(new Paragraph(
                        "Rate of Applicable Taxes", small_bold1));
                PdfPCell BI_PdftableRate_cell2 = new PdfPCell(new Paragraph(
                        "18%", small_bold1));

                BI_PdftableRate_cell1
                        .setHorizontalAlignment(Element.ALIGN_CENTER);
                BI_PdftableRate_cell1.setPadding(5);

                BI_PdftableRate_cell2
                        .setHorizontalAlignment(Element.ALIGN_CENTER);
                BI_PdftableRate_cell2.setPadding(5);

                BI_PdftableRate.addCell(BI_PdftableRate_cell1);
                BI_PdftableRate.addCell(BI_PdftableRate_cell2);
                document.add(BI_PdftableRate);

                PdfPTable BI_PdftableRate2 = new PdfPTable(4);
                BI_PdftableRate2.setWidthPercentage(100);

                PdfPCell BI_PdftableRate_cell12 = new PdfPCell(new Paragraph(
                        "Investment Plan Opted For", small_bold1));
                PdfPCell BI_PdftableRate_cell22 = new PdfPCell(new Paragraph(
                        "Smart Choice Plan", small_bold1));

                BI_PdftableRate_cell12
                        .setHorizontalAlignment(Element.ALIGN_CENTER);
                BI_PdftableRate_cell12.setPadding(5);

                BI_PdftableRate_cell22
                        .setHorizontalAlignment(Element.ALIGN_CENTER);
                BI_PdftableRate_cell22.setPadding(5);

                BI_PdftableRate2.addCell(BI_PdftableRate_cell12);
                BI_PdftableRate2.addCell(BI_PdftableRate_cell22);
                document.add(BI_PdftableRate2);

                PdfPTable BI_PdftableRate23 = new PdfPTable(4);
                BI_PdftableRate23.setWidthPercentage(100);

                PdfPCell BI_PdftableRate_cell123 = new PdfPCell(new Paragraph(
                        "Investment Strategy Opted For", small_bold1));
                PdfPCell BI_PdftableRate_cell223 = new PdfPCell(new Paragraph(
                        "Trigger Fund Strategy", small_bold1));

                BI_PdftableRate_cell123
                        .setHorizontalAlignment(Element.ALIGN_CENTER);
                BI_PdftableRate_cell123.setPadding(5);

                BI_PdftableRate_cell223
                        .setHorizontalAlignment(Element.ALIGN_CENTER);
                BI_PdftableRate_cell223.setPadding(5);

                BI_PdftableRate23.addCell(BI_PdftableRate_cell123);
                BI_PdftableRate23.addCell(BI_PdftableRate_cell223);
                document.add(BI_PdftableRate23);

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

                PdfPCell plan_option = new PdfPCell(new Paragraph(
                        "Plan Option", small_bold1));
                plan_option.setColspan(2);
                plan_option.setHorizontalAlignment(Element.ALIGN_CENTER);
                PdfPCell plan_option2 = new PdfPCell(new Paragraph(
                        "Advantage Plan", small_bold1));
                plan_option2.setColspan(2);
                plan_option2.setHorizontalAlignment(Element.ALIGN_CENTER);

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

                BI_PdftableFundTypes.addCell(plan_option);
                BI_PdftableFundTypes.addCell(plan_option2);
                BI_PdftableFundTypes.addCell(BI_PdftableFundTypes_cell1);
                BI_PdftableFundTypes.addCell(BI_PdftableFundTypes_cell2);
                BI_PdftableFundTypes.addCell(BI_PdftableFundTypes_cell3);
                BI_PdftableFundTypes.addCell(BI_PdftableFundTypes_cell4);
                document.add(BI_PdftableFundTypes);

                String EquityEliteFundIIFMC = "1.35%";
                if (percent_equity_pension_fund.equalsIgnoreCase("")) {
                    percent_equity_pension_fund = "0";

                }

                PdfPTable BI_PdftableEquityEliteFundII = new PdfPTable(4);
                BI_PdftableEquityEliteFundII.setWidthPercentage(100);

                PdfPCell BI_PdftableEquityEliteFundII_cell1 = new PdfPCell(
                        new Paragraph(
                                "Equity Pension Fund II (SFIN : ULIF027300513PEEQIT2FND111)\t\t\t\n",
                                small_normal2));
                PdfPCell BI_PdftableEquityEliteFundII_cell2 = new PdfPCell(
                        new Paragraph("100% of Fund Value will be",
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

                String BalancedFundFMC = "1.35%";
                if (percent_equity_optimiser_pension_fund.equalsIgnoreCase("")) {
                    percent_equity_optimiser_pension_fund = "0";

                }

                PdfPTable BI_PdftableBalancedFund = new PdfPTable(4);
                BI_PdftableBalancedFund.setWidthPercentage(100);

                PdfPCell BI_PdftableBalancedFund_cell1 = new PdfPCell(
                        new Paragraph(
                                "Equity Optimiser Pension Fund (SFIN: ULIF011210108PEEQOPTFND111)",
                                small_normal2));
                PdfPCell BI_PdftableBalancedFund_cell2 = new PdfPCell(
                        new Paragraph(percent_equity_optimiser_pension_fund + " %", small_normal));

                PdfPCell BI_PdftableBalancedFund_cell3 = new PdfPCell(
                        new Paragraph(BalancedFundFMC, small_normal));
                PdfPCell BI_PdftableBalancedFund_cell4 = new PdfPCell(
                        new Paragraph("High", small_normal));

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
                //document.add(BI_PdftableBalancedFund);

                String BondFundFMC = "1.35%";
                if (percent_growth_pension_fund.equalsIgnoreCase("")) {
                    percent_growth_pension_fund = "0";

                }

                PdfPTable BI_PdftableBondFund = new PdfPTable(4);
                BI_PdftableBondFund.setWidthPercentage(100);

                PdfPCell BI_PdftableBondFund_cell1 = new PdfPCell(new Paragraph(
                        "Growth Pension Fund (SFIN: ULIF008150207PEGRWTHFND111)",
                        small_normal2));
                PdfPCell BI_PdftableBondFund_cell2 = new PdfPCell(new Paragraph(
                        percent_growth_pension_fund + " %", small_normal));

                PdfPCell BI_PdftableBondFund_cell3 = new PdfPCell(new Paragraph(
                        BondFundFMC, small_normal));
                PdfPCell BI_PdftableBondFund_cell4 = new PdfPCell(new Paragraph(
                        "Medium to High", small_normal));

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
                //document.add(BI_PdftableBondFund);

                String MoneyMarketFundFMC = "1.00%";
                if (percent_bond_pension_fund3.equalsIgnoreCase("")) {
                    percent_bond_pension_fund3 = "0";

                }

                PdfPTable BI_PdftableMoneyMarketFund = new PdfPTable(4);
                BI_PdftableMoneyMarketFund.setWidthPercentage(100);

                PdfPCell BI_PdftableMoneyMarketFund_cell1 = new PdfPCell(
                        new Paragraph(
                                "Bond Pension Fund II (SFIN : ULIF028300513PENBON2FND111)",
                                small_normal2));
                PdfPCell BI_PdftableMoneyMarketFund_cell2 = new PdfPCell(
                        new Paragraph("distributed among the three funds",
                                small_normal));

                PdfPCell BI_PdftableMoneyMarketFund_cell3 = new PdfPCell(
                        new Paragraph(MoneyMarketFundFMC, small_normal));
                PdfPCell BI_PdftableMoneyMarketFund_cell4 = new PdfPCell(
                        new Paragraph("Low to Medium", small_normal));

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


                String bond_optimiser_fundFMC = "0.25%";
                if (percent_money_market_pension_fund3.equalsIgnoreCase("")) {
                    percent_money_market_pension_fund3 = "0";

                }

                PdfPTable BI_Pdftablebond_optimiser_fund = new PdfPTable(4);
                BI_Pdftablebond_optimiser_fund.setWidthPercentage(100);

                PdfPCell BI_Pdftablebond_optimiser_fund_cell1 = new PdfPCell(
                        new Paragraph(
                                "Money Market Pension Fund II (SFIN : ULIF029300513PEMNYM2FND111)",
                                small_normal2));
                PdfPCell BI_Pdftablebond_optimiser_fund_cell2 = new PdfPCell(
                        new Paragraph("as per term to maturity",
                                small_normal));

                PdfPCell BI_Pdftablebond_optimiser_fund_cell3 = new PdfPCell(
                        new Paragraph(bond_optimiser_fundFMC, small_normal));
                PdfPCell BI_Pdftablebond_optimiser_fund_cell4 = new PdfPCell(
                        new Paragraph("Low", small_normal));

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

            }


            document.add(BI_Pdftablereadunderstand);
            document.add(BI_Pdftable101);
            document.add(BI_Pdftable102);
            document.add(BI_Pdftable103);
            document.add(BI_Pdftable104);

            document.add(new_line);
            //document.add(table1);


            // Table here

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
                    "Commission payable to intermediaries (Rs)", small_bold2));

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
            PdfPCell BI_Pdftable_output_no_cell14_1 = new PdfPCell(new Paragraph(
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

            BI_Pdftableoutput_no.addCell(BI_Pdftable_output_no_cell14_1);

            document.add(BI_Pdftableoutput_no);

            for (int i = 0; i < Integer.parseInt(policyTermStr); i++) {

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

            Calendar present_date = Calendar.getInstance();
            int mDay = present_date.get(Calendar.DAY_OF_MONTH);
            int mMonth = present_date.get(Calendar.MONTH);
            int mYear = present_date.get(Calendar.YEAR);

            String CurrentDate = mDay + "-" + (mMonth + 1) + "-" + mYear;
            /*PdfPTable BI_Pdftable26 = new PdfPTable(1);
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
            document.add(BI_Pdftable26_cell1);*/
//			document.add(new_line);

            /*if (!bankUserType.equalsIgnoreCase("Y")) {
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
//			document.add(new_line);

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

                PdfPTable BI_PdftableMarketing_signature = new PdfPTable(3);

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
            }*/





          /*  PdfPTable t2 = new PdfPTable(1);
            t2.setWidthPercentage(100);
            PdfPCell cell2 = new PdfPCell(new Paragraph("PART B", small_bold));
            t2.addCell(cell2);
            document.add(t2);
*/

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


            //document.add(BI_PdftableOutputHeader833);

            //document.add(BI_Pdftable2611);
            //document.add(BI_Pdftable_eSign1);
            //document.add(BI_PdftableMarketing1);
            //document.add(BI_PdftableMarketing_signature1);


            PdfPTable table1 = new PdfPTable(9);
            table1.setWidths(new float[]{3f, 3f, 5f, 5f, 5f, 5f, 5f, 5f, 5f});
            table1.setWidthPercentage(100);

            // 1st row

            cell = new PdfPCell(new Phrase("Annuity Option Selected (The option can be changed any time before vesting)", small_normal));
            cell.setColspan(5);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);

            cell = new PdfPCell(new Phrase((spnr_bi_Matuirty_option.getSelectedItem().toString()), small_normal));
            cell.setColspan(4);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);


            cell = new PdfPCell(new Phrase("Fund Value (FV) at Vesting", small_normal));
            cell.setColspan(5);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);

            cell = new PdfPCell(new Phrase("Annuity Payable p.a.", small_normal));
            cell.setColspan(4);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);

            cell = new PdfPCell(new Phrase("Accumulated at 4% p.a.Rs.", small_normal));
            cell.setRowspan(3);
            cell.setColspan(2);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);
            // 2nd row
            cell = new PdfPCell(new Phrase("Accumulated at 8% p.a.Rs.", small_normal));
            cell.setRowspan(3);
            cell.setColspan(1);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);

            cell = new PdfPCell(new Phrase(
                    "Minimum Assured Benefit, if any", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setColspan(2);
            cell.setRowspan(2);
            table1.addCell(cell);

            cell = new PdfPCell(new Phrase("Based on FV accumulated at 4% p.a.Rs.",
                    small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setRowspan(3);
            cell.setColspan(1);
            table1.addCell(cell);

            cell = new PdfPCell(new Phrase("Based on FV accumulated at 8% p.a.Rs.",
                    small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setRowspan(3);
            cell.setColspan(1);
            table1.addCell(cell);
            cell = new PdfPCell(new Phrase("Based on the Minimum Assured Benefit, if any(Rs.)",
                    small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setRowspan(3);
            cell.setColspan(2);
            table1.addCell(cell);

            cell = new PdfPCell(new Phrase(
                    "Rs.", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setColspan(1);
            cell.setRowspan(1);
            table1.addCell(cell);

            cell = new PdfPCell(new Phrase(
                    "Minimum return on the premiums paid % p.a.", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setColspan(1);
            cell.setRowspan(1);
            table1.addCell(cell);


            nonGuaranVestingBenefit_4Percent = prsObj.parseXmlTag(output, "fundVal4");
            nonGuaranVestingBenefit_8Percent = prsObj.parseXmlTag(output, "fundVal8");
            guarnVestngBen = prsObj.parseXmlTag(output, "VestingMinAssBen");
            annuityPayout_4_Pr = prsObj.parseXmlTag(output, "annuityPay4pa");
            annuityPayout_8_Pr = prsObj.parseXmlTag(output, "annuityPay8pa");
            annuityAmount_MiniBen = prsObj.parseXmlTag(output, "annuityAmount_MiniBen");


            nonGuaranVestingBenefit_4Percent = obj.getRound(obj.getStringWithout_E(Double
                    .valueOf((nonGuaranVestingBenefit_4Percent.equals("") || nonGuaranVestingBenefit_4Percent == null) ? "0"
                            : nonGuaranVestingBenefit_4Percent)))
                    + "";
            nonGuaranVestingBenefit_8Percent = obj.getRound(obj.getStringWithout_E(Double
                    .valueOf((nonGuaranVestingBenefit_8Percent.equals("") || nonGuaranVestingBenefit_8Percent == null) ? "0"
                            : nonGuaranVestingBenefit_8Percent)))
                    + "";

            guarnVestngBen = obj.getRound(obj.getStringWithout_E(Double
                    .valueOf((guarnVestngBen.equals("") || guarnVestngBen == null) ? "0"
                            : guarnVestngBen)))
                    + "";
            annuityPayout_4_Pr = obj.getRound(obj.getStringWithout_E(Double
                    .valueOf((annuityPayout_4_Pr.equals("") || annuityPayout_4_Pr == null) ? "0"
                            : annuityPayout_4_Pr)))
                    + "";

            annuityPayout_8_Pr = obj.getRound(obj.getStringWithout_E(Double
                    .valueOf((annuityPayout_8_Pr.equals("") || annuityPayout_8_Pr == null) ? "0"
                            : annuityPayout_8_Pr)))
                    + "";
            annuityAmount_MiniBen = obj.getRound(obj.getStringWithout_E(Double
                    .valueOf((annuityAmount_MiniBen.equals("") || annuityAmount_MiniBen == null) ? "0"
                            : annuityAmount_MiniBen)))
                    + "";


            cell = new PdfPCell(new Phrase(nonGuaranVestingBenefit_4Percent, small_normal));
            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT | Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setColspan(2);
            table1.addCell(cell);

            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell = new PdfPCell(new Phrase(nonGuaranVestingBenefit_8Percent, small_normal));
            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT | Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setColspan(1);
            table1.addCell(cell);

            cell = new PdfPCell(new Phrase(guarnVestngBen, small_normal));
            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT | Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setColspan(2);
            table1.addCell(cell);

            cell = new PdfPCell(new Phrase(annuityPayout_4_Pr, small_normal));
            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT | Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setColspan(1);
            table1.addCell(cell);


            cell = new PdfPCell(new Phrase(annuityPayout_8_Pr, small_normal));
            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT | Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setColspan(1);
            table1.addCell(cell);

            cell = new PdfPCell(new Phrase(annuityAmount_MiniBen, small_normal));
            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT | Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setColspan(2);
            table1.addCell(cell);


            document.add(table1);


            PdfPTable t22 = new PdfPTable(1);
            t22.setWidthPercentage(100);
            PdfPCell cell22 = new PdfPCell(new Paragraph("* See Part B for details", small_bold2));
            t22.addCell(cell22);
            document.add(t22);


            PdfPTable t2144 = new PdfPTable(1);
            t2144.setWidthPercentage(100);
            PdfPCell cell2144 = new PdfPCell(new Paragraph(Annuity_rate_dec, small_bold2));
            t2144.addCell(cell2144);
            document.add(t2144);


            PdfPTable t21 = new PdfPTable(1);
            t21.setWidthPercentage(100);
            PdfPCell cell21 = new PdfPCell(new Paragraph("IN THIS POLICY, THE INVESTMENT RISK IS BORNE BY THE POLICYHOLDER AND THE ABOVE INTEREST RATES ARE ONLY FOR ILLUSTRATIVE PURPOSE", small_bold));
            t21.addCell(cell21);
            document.add(t21);

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
                    new Paragraph("Net Yield " + netYield8Pr + "%", small_bold2));

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

            PdfPCell BI_Pdftable_output_cell78_1 = new PdfPCell(new Paragraph(
                    "Guarantee charge", small_bold2));

            PdfPCell BI_Pdftable_output_cell781 = new PdfPCell(new Paragraph(
                    "Other charges*", small_bold2));
            PdfPCell BI_Pdftable_output_cell88 = new PdfPCell(new Paragraph(
                    "Additions to the fund*", small_bold2));
            PdfPCell BI_Pdftable_output_cell88_2 = new PdfPCell(new Paragraph(
                    "Guaranteed Addition", small_bold2));
            PdfPCell BI_Pdftable_output_cell88_3 = new PdfPCell(new Paragraph(
                    "Terminal Addition", small_bold2));


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
            BI_Pdftableoutput8.addCell(BI_Pdftable_output_cell781);
            BI_Pdftableoutput8.addCell(BI_Pdftable_output_cell88);
            BI_Pdftableoutput8.addCell(BI_Pdftable_output_cell88_2);
            BI_Pdftableoutput8.addCell(BI_Pdftable_output_cell88_3);
            BI_Pdftableoutput8.addCell(BI_Pdftable_output_cell98);

            BI_Pdftableoutput8.addCell(BI_Pdftable_output_cell108);
            BI_Pdftableoutput8.addCell(BI_Pdftable_output_cell118);
            BI_Pdftableoutput8.addCell(BI_Pdftable_output_cell128);

            BI_Pdftableoutput8.addCell(BI_Pdftable_output_cell138);

            document.add(BI_Pdftableoutput8);

            PdfPTable BI_Pdftableoutput_no8 = new PdfPTable(14);
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

            //document.add(BI_Pdftableoutput_no8);

            for (int i = 0; i < Integer.parseInt(policyTermStr); i++) {

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

                PdfPCell BI_Pdftable_output_row1_cell58_1 = new PdfPCell(
                        new Paragraph(list_data1.get(i)
                                .getGuarantee_charge(), small_bold2));


                PdfPCell BI_Pdftable_output_row1_cell78 = new PdfPCell(
                        new Paragraph(list_data1.get(i).getTotal_charge1(),
                                small_bold2));

                PdfPCell BI_Pdftable_output_row1_cell881 = new PdfPCell(
                        new Paragraph(list_data1.get(i)
                                .getStr_AddToTheFund4Pr(),
                                small_bold2));

                PdfPCell BI_Pdftable_output_row1_cell881_2 = new PdfPCell(
                        new Paragraph(list_data1.get(i)
                                .getGuaranteed_Addition(),
                                small_bold2));

                PdfPCell BI_Pdftable_output_row1_cell881_3 = new PdfPCell(
                        new Paragraph(list_data1.get(i)
                                .getTerminal_Addition(),
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
                BI_Pdftableoutput_row18.addCell(BI_Pdftable_output_row1_cell881_2);
                BI_Pdftableoutput_row18.addCell(BI_Pdftable_output_row1_cell881_3);

                BI_Pdftableoutput_row18.addCell(BI_Pdftable_output_row1_cell98);

                BI_Pdftableoutput_row18.addCell(BI_Pdftable_output_row1_cell108);
                BI_Pdftableoutput_row18.addCell(BI_Pdftable_output_row1_cell128);

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

            PdfPCell BI_Pdftable_output_cell7814_1 = new PdfPCell(new Paragraph(
                    "Guarantee charge", small_bold2));

            PdfPCell BI_Pdftable_output_cell7811 = new PdfPCell(new Paragraph(
                    "Other charges*", small_bold2));
            PdfPCell BI_Pdftable_output_cell8814 = new PdfPCell(new Paragraph(
                    "Additions to the fund*", small_bold2));
            PdfPCell BI_Pdftable_output_cell8814_2 = new PdfPCell(new Paragraph(
                    "Guaranteed Addition", small_bold2));
            PdfPCell BI_Pdftable_output_cell8814_3 = new PdfPCell(new Paragraph(
                    "Terminal Addition", small_bold2));
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
            BI_Pdftableoutput4.addCell(BI_Pdftable_output_cell7811);
            BI_Pdftableoutput4.addCell(BI_Pdftable_output_cell8814);
            BI_Pdftableoutput4.addCell(BI_Pdftable_output_cell8814_2);
            BI_Pdftableoutput4.addCell(BI_Pdftable_output_cell8814_3);
            BI_Pdftableoutput4.addCell(BI_Pdftable_output_cell981);

            BI_Pdftableoutput4.addCell(BI_Pdftable_output_cell1081);
            BI_Pdftableoutput4.addCell(BI_Pdftable_output_cell1181);
            BI_Pdftableoutput4.addCell(BI_Pdftable_output_cell1281);

            BI_Pdftableoutput4.addCell(BI_Pdftable_output_cell1381);

            document.add(BI_Pdftableoutput4);

            PdfPTable BI_Pdftableoutput_no4 = new PdfPTable(14);
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

            //document.add(BI_Pdftableoutput_no4);

            for (int i = 0; i < Integer.parseInt(policyTermStr); i++) {

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

                PdfPCell BI_Pdftable_output_row1_cell581_1 = new PdfPCell(
                        new Paragraph(list_data2.get(i)
                                .getGuarantee_charge(), small_bold2));

                PdfPCell BI_Pdftable_output_row1_cell781 = new PdfPCell(
                        new Paragraph(list_data2.get(i).getTotal_charge1(),
                                small_bold2));

                PdfPCell BI_Pdftable_output_row1_cell8811 = new PdfPCell(
                        new Paragraph(list_data2.get(i)
                                .getStr_AddToTheFund4Pr(),
                                small_bold2));

                PdfPCell BI_Pdftable_output_row1_cell8811_2 = new PdfPCell(
                        new Paragraph(list_data2.get(i)
                                .getGuaranteed_Addition(),
                                small_bold2));

                PdfPCell BI_Pdftable_output_row1_cell8811_3 = new PdfPCell(
                        new Paragraph(list_data2.get(i)
                                .getTerminal_Addition(),
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
                BI_Pdftableoutput_row184.addCell(BI_Pdftable_output_row1_cell8811_2);
                BI_Pdftableoutput_row184.addCell(BI_Pdftable_output_row1_cell8811_3);

                BI_Pdftableoutput_row184.addCell(BI_Pdftable_output_row1_cell981);

                BI_Pdftableoutput_row184.addCell(BI_Pdftable_output_row1_cell1081);
                BI_Pdftableoutput_row184.addCell(BI_Pdftable_output_row1_cell1281);

                BI_Pdftableoutput_row184.addCell(BI_Pdftable_output_row1_cell1381);
                BI_Pdftableoutput_row184.addCell(BI_Pdftable_output_row1_cell1481);

                document.add(BI_Pdftableoutput_row184);

            }
            document.add(new_line);
//            document.add(table2);
            document.add(note);
            document.add(table21);
            document.add(new_line);
            document.add(ls);

            document.add(table3);
            document.add(new_line);

            PdfPTable BI_Pdftable_important = new PdfPTable(1);
            BI_Pdftable_important.setWidthPercentage(100);
            PdfPCell BI_Pdftable_important_cell = new PdfPCell(
                    new Paragraph(
                            "Important:",
                            normal1_bold));

            BI_Pdftable_important_cell.setPadding(5);
            BI_Pdftable_important.addCell(BI_Pdftable_important_cell);
            document.add(BI_Pdftable_important);

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
            //  document.add(BI_Pdftable_CompanysPolicySurrender3);

            PdfPTable BI_Pdftable_CompanysPolicySurrender4 = new PdfPTable(1);
            BI_Pdftable_CompanysPolicySurrender4.setWidthPercentage(100);
            PdfPCell BI_Pdftable_CompanysPolicySurrender4_cell = new PdfPCell(
                    new Paragraph(
                            "You have to submit Proof of source of Fund.",
                            small_normal));

            BI_Pdftable_CompanysPolicySurrender4_cell.setPadding(5);

            BI_Pdftable_CompanysPolicySurrender4
                    .addCell(BI_Pdftable_CompanysPolicySurrender4_cell);
            // document.add(BI_Pdftable_CompanysPolicySurrender4);

            PdfPTable BI_Pdftable_CompanysPolicySurrender5 = new PdfPTable(1);
            BI_Pdftable_CompanysPolicySurrender5.setWidthPercentage(100);
            PdfPCell BI_Pdftable_CompanysPolicySurrender5_cell = new PdfPCell(
                    new Paragraph(Company_policy_surrender_dec, small_normal));

            BI_Pdftable_CompanysPolicySurrender5_cell.setPadding(5);

            BI_Pdftable_CompanysPolicySurrender5
                    .addCell(BI_Pdftable_CompanysPolicySurrender5_cell);
            document.add(BI_Pdftable_CompanysPolicySurrender5);

            document.add(new_line);

          /*  Calendar present_date = Calendar.getInstance();
            int mDay = present_date.get(Calendar.DAY_OF_MONTH);
            int mMonth = present_date.get(Calendar.MONTH);
            int mYear = present_date.get(Calendar.YEAR);

            String CurrentDate = mDay + "-" + (mMonth + 1) + "-" + mYear;*/
            PdfPTable BI_Pdftable261 = new PdfPTable(1);
            BI_Pdftable261.setWidthPercentage(100);
            // CR_table6.setWidths(columnWidths_26);
            PdfPCell BI_Pdftable261_cell1 = new PdfPCell(
                    new Paragraph(
                            "I, "
                                    + name_of_person
                                    + "     having received the information with respect to the above, have understood the above statement before entering into the contract.",
                            small_bold));

            BI_Pdftable261_cell1.setPadding(5);

            BI_Pdftable261.addCell(BI_Pdftable261_cell1);
            document.add(BI_Pdftable261);
            document.add(BI_Pdftable261_cell1);
            document.add(new_line);

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

            PdfPTable BI_PdftablePolicyHolder_signature1;

            if (!bankUserType.equalsIgnoreCase("Y")) {
                BI_PdftablePolicyHolder_signature1 = new PdfPTable(3);
            } else {
                BI_PdftablePolicyHolder_signature1 = new PdfPTable(2);
            }

            BI_PdftablePolicyHolder_signature1.setWidthPercentage(100);

            PdfPCell BI_PdftablePolicyHolder_signature_11 = new PdfPCell(
                    new Paragraph("Place :" + place2, small_normal));
            PdfPCell BI_PdftablePolicyHolder_signature_21 = new PdfPCell(
                    new Paragraph("Date  :" + CurrentDate, small_normal));

            PdfPCell BI_PdftablePolicyHolder_signature_31 = new PdfPCell();
            if (!bankUserType.equalsIgnoreCase("Y")) {
                byte[] fbyt_Proposer = Base64.decode(proposer_sign, 0);
                Bitmap Proposerbitmap = BitmapFactory.decodeByteArray(
                        fbyt_Proposer, 0, fbyt_Proposer.length);

                BI_PdftablePolicyHolder_signature_31.setFixedHeight(60f);
                ByteArrayOutputStream PolicyHolder_signature_stream = new ByteArrayOutputStream();

                (Proposerbitmap).compress(Bitmap.CompressFormat.PNG, 50,
                        PolicyHolder_signature_stream);
                Image PolicyHolder_signature = Image
                        .getInstance(PolicyHolder_signature_stream
                                .toByteArray());

                BI_PdftablePolicyHolder_signature_31
                        .setImage(PolicyHolder_signature);
            }

            BI_PdftablePolicyHolder_signature_11.setPadding(5);
            BI_PdftablePolicyHolder_signature_21.setPadding(5);

            BI_PdftablePolicyHolder_signature1
                    .addCell(BI_PdftablePolicyHolder_signature_11);
            BI_PdftablePolicyHolder_signature1
                    .addCell(BI_PdftablePolicyHolder_signature_21);

            if (!bankUserType.equalsIgnoreCase("Y")) {
                BI_PdftablePolicyHolder_signature1
                        .addCell(BI_PdftablePolicyHolder_signature_31);
            }

            document.add(BI_PdftablePolicyHolder_signature1);
            document.add(new_line);

            if (!bankUserType.equalsIgnoreCase("Y")) {
                PdfPTable BI_PdftableMarketing = new PdfPTable(1);
                BI_PdftableMarketing.setWidthPercentage(100);
                PdfPCell BI_PdftableMarketing_signature_cell = new PdfPCell(
                        new Paragraph("Marketing official's Signature & Company Seal",
                                small_bold));
                BI_PdftableMarketing_signature_cell
                        .setHorizontalAlignment(Element.ALIGN_CENTER);
                BI_PdftableMarketing_signature_cell
                        .setBackgroundColor(BaseColor.LIGHT_GRAY);
                BI_PdftableMarketing_signature_cell.setPadding(5);

                BI_PdftableMarketing
                        .addCell(BI_PdftableMarketing_signature_cell);
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

        } catch (Exception ignored) {
            ignored.printStackTrace();
        }
    }

    /************************************************************** calculation starts here *************************************************************/

    private String[] getOutput(String sheetName, RetireSmartBean retireSmartBean) {
        bussIll = new StringBuilder();
        RetireSmartProperties prop = new RetireSmartProperties();
        // ouput variable declaration
        int _month_E = 0, _year_F = 0, _age_H = 0;

        String _policyInforce_G = "Y";

        // double _premium_I=0,
        // _topUpPremium_J=0,
        // _premiumAllocationCharge_K=0,
        // _topUpCharges_L=0,
        // _ServiceTaxOnAllocation_M=0,
        // _amountAvailableForInvestment_N=0,
        // _sumAssuredRelatedCharges_O=0,
        // _riderCharges_P=0,
        // _policyAdministrationCharges_Q=0,
        // _serviceTaxOnPolicyAdminisrtationCharge_R=0,
        // _mortalityCharges_S=0,
        // _accTPDCharges_T=0,
        // _totalCharges_U=0,
        // _serviceTaxExclOfSTOnAllocAndSurr_V=0,
        // _totalServiceTax_W=0,
        // _additionToFundIfAny_X=0,
        // _fundBeforeFMC_Y=0,
        // _fundManagementCharge_Z=0,
        // _guranteeCharge_AA=0,
        // _serviceTaxOnFMC_AB=0,
        // _fundValueAfterFMCBerforeGA_AC=0,
        // _guaranteedAddition_AD=0,
        // _loyaltiAddition_AE=0,
        // _fundValueAtEnd_AF=0,
        // _surrenderCharges_AG=0,
        // _serviceTaxOnSurrenderCharges_AH=0,
        // _surrenderValue_AI=0,
        // _deathBenefit_AJ=0,
        // _mortalityCharges_AK=0,
        // _accTPDCharges_AL=0,
        // _totalCharges_AM=0,
        // _serviceTaxExclOfSTOnAllocAndSurr_AN=0,
        // _totalServiceTax_AO=0,
        // _additionToFundIfAny_AP=0,
        // _fundBeforeFMC_AQ=0,
        // _fundManagementCharge_AR=0,
        // _guranteeCharge_AS=0,
        // _serviceTaxOnFMC_AT=0,
        // _fundValueAfterFMCBerforeGA_AU=0,
        // _guaranteedAddition_AV=0,
        // _loyaltiAddition_AW=0,
        // _fundValueAtEnd_AX=0,
        // _surrenderCharges_AY=0,
        // _serviceTaxOnSurrenderCharges_AZ=0,
        // _surrenderValue_BA=0,
        // _deathBenefit_BB=0,
        // _surrenderCap_BC=0,
        // _oneHundredPercentOfCummulativePremium_BD=0;

        // temp variable declaration
        int month_E = 0, year_F = 0, age_H = 0;

        String policyInforce_G = "Y";

        double premium_I = 0, topUpPremium_J = 0, premiumAllocationCharge_K = 0, topUpCharges_L = 0, ServiceTaxOnAllocation_M = 0, amountAvailableForInvestment_N = 0, sumAssuredRelatedCharges_O = 0, riderCharges_P = 0, policyAdministrationCharges_Q = 0, serviceTaxOnPolicyAdminisrtationCharge_R = 0, mortalityCharges_S = 0, accTPDCharges_T = 0, totalCharges_U = 0, serviceTaxExclOfSTOnAllocAndSurr_V = 0, totalServiceTax_W = 0, additionToFundIfAny_X = 0, fundBeforeFMC_Y = 0, fundManagementCharge_Z = 0, guranteeCharge_AA = 0, serviceTaxOnFMC_AB = 0, fundValueAfterFMCBerforeGA_AC = 0, guaranteedAddition_AD = 0, loyaltiAddition_AE = 0, fundValueAtEnd_AF = 0, surrenderCharges_AG = 0, serviceTaxOnSurrenderCharges_AH = 0, surrenderValue_AI = 0, deathBenefit_AJ = 0, mortalityCharges_AK = 0, accTPDCharges_AL = 0, totalCharges_AM = 0, serviceTaxExclOfSTOnAllocAndSurr_AN = 0, totalServiceTax_AO = 0, additionToFundIfAny_AP = 0, fundBeforeFMC_AQ = 0, fundManagementCharge_AR = 0, guranteeCharge_AS = 0, serviceTaxOnFMC_AT = 0, fundValueAfterFMCBerforeGA_AU = 0, guaranteedAddition_AV = 0, loyaltiAddition_AW = 0, fundValueAtEnd_AX = 0, surrenderCharges_AY = 0, serviceTaxOnSurrenderCharges_AZ = 0, surrenderValue_BA = 0, deathBenefit_BB = 0, surrenderCap_BC = 0, oneHundredPercentOfCummulativePremium_BD = 0;
        double annuityRates = 0;
        double percentToBeInvested_EquityPensionFund = 0, percentToBeInvested_EquityOptPensionFund = 0, percentToBeInvested_GrowthPensionFund = 0, percentToBeInvested_BondPensionFund = 0, percentToBeInvested_MoneyMarketPesionFund = 0;


        // GUI input
        double SAMF = 1;
        boolean staffDisc = retireSmartBean.getStaffDisc();
        int ageAtEntry = retireSmartBean.getAge();
        int policyTerm = retireSmartBean.getPolicyTerm();
        String premFreqMode = retireSmartBean.getPremFrequencyMode();
        double premiumAmount = retireSmartBean.getPremiumAmount();
        String premPayingOption = retireSmartBean.getPremPayingOption();
        int premPayingTerm = retireSmartBean.getPremiumPayingTerm();
        String fundOption = retireSmartBean.getBi_retire_smart_plan_option();
        percentToBeInvested_EquityPensionFund = retireSmartBean.getPercentToBeInvested_EquityPensionFund();
        percentToBeInvested_EquityOptPensionFund = retireSmartBean.getPercentToBeInvested_EquityOptPensionFund();
        percentToBeInvested_BondPensionFund = retireSmartBean.getPercentToBeInvested_BondPensionFund();
        percentToBeInvested_GrowthPensionFund = retireSmartBean.getPercentToBeInvested_GrowthPensionFund();
        percentToBeInvested_MoneyMarketPesionFund = retireSmartBean.getPercentToBeInvested_MoneyMarketPensionFund();

        double serviceTax = 0;
        //double serviceTax=0;
        boolean isKerlaDisc = retireSmartBean.isKerlaDisc();
        int noOfYearsElapsedSinceInception = retireSmartBean
                .getNoOfYearsElapsedSinceInception();
        int PF = retireSmartBean.getPF();
        double annualPremium = premiumAmount * PF;
        double sumAssured = annualPremium * SAMF;
        String addTopUp = "No";
        double effectiveTopUpPrem = retireSmartBean.getEffectiveTopUpPrem();
        RetireSmartBusinessLogic BIMAST = new RetireSmartBusinessLogic(retireSmartBean);
        String[] forBIArray = BIMAST
                .getForBIArr(prop.mortalityCharges_AsPercentOfofLICtable);

        // BIMAST.setABC(policyTerm, ageAtEntry);

        int rowNumber = 0, age = 0;
        String premPaymentOption = "NO";
        double sumOfPremium = 0;
        String fundValAt4_str = "", fundValAt8_str = "";
        double sum_AC = 0, sum_AD = 0, sum_AE = 0, sum_AF = 0, _sum_AC = 0, _sum_AD = 0, _sum_AE = 0, _sum_AV = 0, _sum_AW = 0, _sum_AU = 0, _sum_z = 0, _sum_AR = 0, _sum_K = 0, _sum_Q = 0, _sum_AA = 0, otherCharges4Pr = 0, otherCharges8Pr = 0, _sum_AS = 0;

        // BI Variables
        double sum_I = 0, _sum_I = 0, sum_J = 0, _sum_J = 0, sum_K = 0, sum_N = 0, sum_Q = 0, sum_S = 0, sum_U = 0, sum_W = 0, sum_Z = 0, sum_AA = 0, sum_AK = 0, sum_AM = 0, sum_AO = 0, sum_AR = 0, sum_AS = 0, sum_AV = 0, sum_AU = 0, sum_AW = 0, sum_AX = 0, sum_I1 = 0, cumprem = 0;
        double annuityPay4pa = 0, totalFYPremium = 0, annuityPay8pa = 0, sum_FY_Premium = 0, sum_C = 0, sum_E = 0, Commission_AQ = 0, sum_AdditionToFund_4Pr = 0, sum_AdditionToFund_8Pr = 0, fundBeforeFMC4Pr = 0, fundBeforeFMC8Pr = 0;
        double GuaranteedVestingBenefit = 0, NonGuaranteedVestingBenefit = 0, TotalVestingbenifit4 = 0, GuaranteedDeathBenefit4 = 0, NonGuaranteedDeathBenefit = 0, TotalDeathBenifit = 0, GuaranteedSurrenderBenifit = 0, TotalSurrenderBenifit4 = 0, Commisionrate = 0,
                Commision4 = 0, FundValAtEnd8Pr = 0, FundValAtEnd4Pr = 0, GuaranteedVestingBenefit8 = 0, NonGuaranteedVestingBenefit8 = 0, TotalVestingbenifit8 = 0,
                GuaranteedDeathBenefit8 = 0, NonGuaranteedDeathBenefit8 = 0, TotalDeathBenifit8 = 0, GuaranteedSurrenderBenifit8 = 0, TotalSurrenderBenifit8 = 0;
        double vestingBenefit11 = 0, annuityAmount_MiniBen = 0;
        try {
            // calcultion of fields
            // for(int i=0; i <= 24; i++)
            for (int i = 0; i < (policyTerm * 12); i++) {
                rowNumber++;

                BIMAST.setMonth_E(rowNumber);
                month_E = Integer.parseInt(BIMAST.getMonth_E());
                _month_E = month_E;
                // System.out.println("1. month_E " +BIMAST.getMonth_E());

                BIMAST.setYear_F();
                year_F = Integer.parseInt(BIMAST.getYear_F());
                _year_F = year_F;
                // System.out.println("2. year_F "+BIMAST.getYear_F());

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
                _policyInforce_G = BIMAST.getPolicyInForce_G();
                // System.out.println("3. policyInforce_G "+BIMAST.getPolicyInForce_G());

                BIMAST.setAge_H(ageAtEntry);
                age_H = Integer.parseInt(BIMAST.getAge_H());
                _age_H = age_H;
//				System.out.println("4.   Age_H : "+BIMAST.getAge_H());

                // System.out.println("4. age_H "+BIMAST.getAge_H());
                if ((_month_E % 12) == 0) {
                    bussIll.append("<age" + _year_F + ">" + _age_H
                            + "</age" + _year_F + ">");
                }

                BIMAST.setPremium_I(premPayingTerm, PF, annualPremium);
                premium_I = Double.parseDouble(BIMAST.getPremium_I());

                // _premium_I=premium_I;
                // System.out.println("5. premium_I "+BIMAST.getPremium_I());

                sum_I += premium_I;
                sum_I1 = Double.parseDouble("" + (sum_I1 + premium_I));


                if ((_month_E % 12) == 0) {


                    bussIll.append("<AnnPrem" + _year_F + ">"
                            + obj.getStringWithout_E(sum_I) + "</AnnPrem"
                            + _year_F + ">");

                    _sum_I = sum_I;
                    sum_I = 0;
                }
                /***** Added by Tushar Kotian on 13/08/2018 *****/

                if ((_month_E % 12) == 0) {

                    if (_month_E == 12) {
                        cumprem = _sum_I;
                    } else {
                        cumprem = _sum_I + cumprem;
                    }
                    bussIll.append("<CumPrem" + _year_F + ">"
                            + obj.getStringWithout_E(cumprem) + "</CumPrem"
                            + _year_F + ">");

                }

                /***** Added by Tushar Kotian on 13/08/2018 *****/


                BIMAST.setTopUpPremium_J(prop.topUpStatus, effectiveTopUpPrem,
                        addTopUp);
                topUpPremium_J = Double.parseDouble(BIMAST.getTopUpPremium_J());
                // _topUpPremium_J=topUpPremium_J;
                // System.out.println("6. Top up premium charges "+BIMAST.getTopUpPremium_J());

                sum_J += topUpPremium_J;
                if ((_month_E % 12) == 0) {

                    // bussIll.append("<AnnPrem"+ _year_F +">" + sum_J +
                    // "</AnnPrem"+ _year_F +">");
                    _sum_J = sum_J;
                    sum_J = 0;
                }
                //
                BIMAST.setPremiumAllocationCharge_K(policyTerm, staffDisc, false, annualPremium, premPayingOption, premPayingTerm);
                premiumAllocationCharge_K = Double.parseDouble(BIMAST
                        .getPremiumAllocationCharge_K());
                // _premiumAllocationCharge_K=premiumAllocationCharge_K;
                // System.out.println("7. premiumAllocationCharge_K "+premiumAllocationCharge_K);

                double annPremPremiumAllCharge = 0;
                sum_K += premiumAllocationCharge_K;
                if ((_month_E % 12) == 0) {

                    bussIll.append("<PremAllCharge" + _year_F + ">"
                            + obj.getRoundUp(obj.getStringWithout_E(sum_K))
                            + "</PremAllCharge" + _year_F + ">");
                    annPremPremiumAllCharge = _sum_I - sum_K;
                    _sum_K = sum_K;
                    bussIll.append("<AnnPremPremiumAllCharge" + _year_F + ">"
                            + obj.getRoundUp(obj.getStringWithout_E(annPremPremiumAllCharge))
                            + "</AnnPremPremiumAllCharge" + _year_F + ">");
                    sum_K = 0;
                }
                //
                BIMAST.setTopUpCharges_L(prop.topUp);
                topUpCharges_L = Double.parseDouble(BIMAST.getTopUpCharges_L());
                // _topUpCharges_L=topUpCharges_L;
                // System.out.println("8. topUpCharges_L "+topUpCharges_L);

                BIMAST.setServiceTaxOnAllocation_M(prop.allocationCharges,
                        serviceTax);
                ServiceTaxOnAllocation_M = Double.parseDouble(BIMAST
                        .getServiceTaxOnAllocation_M());
                // _ServiceTaxOnAllocation_M=ServiceTaxOnAllocation_M;
                // System.out.println("9. ServiceTaxOnAllocation_M "+ServiceTaxOnAllocation_M);

                BIMAST.setAmountAvailableForInvestment_N(policyTerm);
                amountAvailableForInvestment_N = Double.parseDouble(BIMAST
                        .getAmountAvailableForInvestment_N());
                // _amountAvailableForInvestment_N=amountAvailableForInvestment_N;
                // System.out.println("10. amountAvailableForInvestment_N "+amountAvailableForInvestment_N);

                sum_N += amountAvailableForInvestment_N;
                /*if ((_month_E % 12) == 0) {

                    bussIll.append("<AmtAviFrInv" + _year_F + ">"
                            + obj.getRoundUp(obj.getStringWithout_E(sum_N))
                            + "</AmtAviFrInv" + _year_F + ">");
                    sum_N = 0;
				}*/

                // sumAssuredRelatedCharges_O=Double.parseDouble("0");
                // _sumAssuredRelatedCharges_O=Double.parseDouble("0");

                // BIMAST.setSumAssuredRelatedCharges_O1(policyTerm, sumAssured,
                // prop.charge_SumAssuredBase);
                // sumAssuredRelatedCharges_O=Double.parseDouble(BIMAST.getSumAssuredRelatedCharges_O1());
                // _sumAssuredRelatedCharges_O=sumAssuredRelatedCharges_O;

                BIMAST.setSumAssuredRelatedCharges_O(policyTerm, sumAssured,
                        prop.charge_SumAssuredBase);
                sumAssuredRelatedCharges_O = Double.parseDouble(BIMAST
                        .getSumAssuredRelatedCharges_O());
                // _sumAssuredRelatedCharges_O=sumAssuredRelatedCharges_O;
                // test.append("policyTerm "+policyTerm);
                // test.append("sumAssured "+sumAssured);
                // test.append("charge_SumAssuredBase "+prop.charge_SumAssuredBase);

                // test.append("_sumAssuredRelatedCharges_O "+_sumAssuredRelatedCharges_O);
                // System.out.println("11. sumAssuredRelatedCharges_O "+sumAssuredRelatedCharges_O);

                BIMAST.setRiderCharges_P();
                riderCharges_P = Double.parseDouble(BIMAST.getRiderCharges_P());
                // _riderCharges_P=riderCharges_P;
                // System.out.println("12. _riderCharges_P "+_riderCharges_P);

                // BIMAST.setPolicyAdministrationCharge_Q(_policyAdministrationCharges_Q,
                // prop.charge_Inflation,prop.fixedMonthlyExp_RP,prop.inflation_pa_RP);
                // policyAdministrationCharges_Q=Double.parseDouble(BIMAST.getPolicyAdministrationCharge_Q());
                // _policyAdministrationCharges_Q=policyAdministrationCharges_Q;
                // //
                // System.out.println("13. policyAdministrationCharges_Q "+policyAdministrationCharges_Q);

                BIMAST.setPolicyAdministrationCharge_Q(
                        policyAdministrationCharges_Q, prop.charge_Inflation,
                        prop.fixedMonthlyExp_RP, prop.inflation_pa_RP);
                policyAdministrationCharges_Q = Double.parseDouble(BIMAST
                        .getPolicyAdministrationCharge_Q());
                // _policyAdministrationCharges_Q=policyAdministrationCharges_Q;
//				System.out.println("policyAdministrationCharges_Q "+policyAdministrationCharges_Q);


                sum_Q += policyAdministrationCharges_Q;
                if ((_month_E % 12) == 0) {

                    bussIll.append("<PolAdminChrg" + _year_F + ">"
                            + obj.getRoundUp(obj.getStringWithout_E(sum_Q))
                            + "</PolAdminChrg" + _year_F + ">");
                    _sum_Q = sum_Q;
                    sum_Q = 0;
                }

                if (year_F == 1) {
                    sum_C += riderCharges_P;
                    sum_E += topUpPremium_J;
                    sum_FY_Premium += premium_I;
                }
                BIMAST.setServiceTaxOnPolicyAdministrationCharges_R(
                        prop.administrationCharge, serviceTax);
                serviceTaxOnPolicyAdminisrtationCharge_R = Double
                        .parseDouble(BIMAST
                                .getServiceTaxOnPolicyAdministrationCharges_R());
                // _serviceTaxOnPolicyAdminisrtationCharge_R=serviceTaxOnPolicyAdminisrtationCharge_R;
                // System.out.println("14. serviceTaxOnPolicyAdminisrtationCharge_R "+serviceTaxOnPolicyAdminisrtationCharge_R);

                BIMAST.setOneHundredPercentOfCummulativePremium_BD(oneHundredPercentOfCummulativePremium_BD);
                oneHundredPercentOfCummulativePremium_BD = Double
                        .parseDouble(BIMAST
                                .getOneHundredPercentOfCummulativePremium_BD());
                // _oneHundredPercentOfCummulativePremium_BD=oneHundredPercentOfCummulativePremium_BD;
                // System.out.println("46. oneHundredPercentOfCummulativePremium_BD "+oneHundredPercentOfCummulativePremium_BD);

                BIMAST.setMortalityCharges_S(ageAtEntry, sumAssured,
                        forBIArray, policyTerm, prop.mortalityCharges,
                        fundValueAtEnd_AF, premPaymentOption);
                mortalityCharges_S = Double.parseDouble(BIMAST
                        .getMortalityCharges_S());
                // _mortalityCharges_S=mortalityCharges_S;
                // System.out.println("15. mortalityCharges_S "+mortalityCharges_S);

                sum_S += mortalityCharges_S;
                if ((_month_E % 12) == 0) {

                    bussIll.append("<MortChrg4Pr" + _year_F + ">"
                            + obj.getRoundUp(obj.getStringWithout_E(sum_S))
                            + "</MortChrg4Pr" + _year_F + ">");
                    sum_S = 0;
                }

                BIMAST.setAccTPDCharge_T(policyTerm, prop.accTPD_Charge,
                        sumAssured, prop.mortalityCharges, fundValueAtEnd_AF,
                        premPaymentOption);
                accTPDCharges_T = Double
                        .parseDouble(BIMAST.getAccTPDCharge_T());
                // _accTPDCharges_T=accTPDCharges_T;
                // System.out.println("16. accTPDCharges_T "+accTPDCharges_T);

                BIMAST.setTotalCharges_U(policyTerm);
                totalCharges_U = Double.parseDouble(BIMAST.getTotalCharges_U());
                // _totalCharges_U=totalCharges_U;
                // System.out.println("17. totalCharges_U "+totalCharges_U);

                sum_U += totalCharges_U;
                /*if ((_month_E % 12) == 0) {

                    bussIll.append("<TotCharg4Pr" + _year_F + ">"
                            + Math.round(sum_U) + "</TotCharg4Pr" + _year_F
                            + ">");
                    sum_U = 0;
				}*/

                BIMAST.setServiceTaxExclOfSTOnAllocAndSurr_V(
                        prop.mortalityAndRiderCharges,
                        prop.administrationCharges, serviceTax);
                serviceTaxExclOfSTOnAllocAndSurr_V = Double.parseDouble(BIMAST
                        .getServiceTaxExclOfSTOnAllocAndSurr_V());
                // _serviceTaxExclOfSTOnAllocAndSurr_V=serviceTaxExclOfSTOnAllocAndSurr_V;
                // System.out.println("18. serviceTaxExclOfSTOnAllocAndSurr_V "+serviceTaxExclOfSTOnAllocAndSurr_V);

//				System.out.println("_month_E"+_month_E);

                BIMAST.setAdditionToFundIfAny_X(fundValueAtEnd_AF, policyTerm,
                        prop.int1);
                additionToFundIfAny_X = Double.parseDouble(BIMAST
                        .getAdditionToFundIfAny_X());
                // _additionToFundIfAny_X=additionToFundIfAny_X;
                // System.out.println("20. additionToFundIfAny_X "+additionToFundIfAny_X);

                sum_AdditionToFund_4Pr += additionToFundIfAny_X;
                if ((_month_E % 12) == 0) {

                    bussIll.append("<additionToFund4Pr" + _year_F + ">"
                            + Math.round(sum_AdditionToFund_4Pr) + "</additionToFund4Pr" + _year_F
                            + ">");
                    sum_AdditionToFund_4Pr = 0;
                }

                BIMAST.setFundBeforeFMC_Y(fundValueAtEnd_AF, policyTerm);
                fundBeforeFMC_Y = Double.parseDouble(BIMAST
                        .getFundBeforeFMC_Y());
                // fundBeforeFMC_Y=fundBeforeFMC_Y;
                // System.out.println("21. fundBeforeFMC_Y "+fundBeforeFMC_Y);
                /***** Added by Tushar Kotian on 13/08/2018 *****/

//				if ((_month_E % 12) == 0) {
//
//					bussIll.append("<fundBeforeFMC4Pr" + _year_F + ">"
//							+ obj.getRoundUp(obj.getStringWithout_E(fundBeforeFMC_Y))
//							+ "</fundBeforeFMC4Pr" + _year_F + ">");
////					sum_Z = 0;
//				}
                /***** Added by Tushar Kotian on 13/08/2018 *****/

                BIMAST.setFundManagementCharge_Z(policyTerm,
                        percentToBeInvested_EquityPensionFund,
                        percentToBeInvested_EquityOptPensionFund,
                        percentToBeInvested_GrowthPensionFund,
                        percentToBeInvested_BondPensionFund,
                        percentToBeInvested_MoneyMarketPesionFund, fundOption);
                fundManagementCharge_Z = Double.parseDouble(BIMAST
                        .getFundManagementCharge_Z());
                // fundManagementCharge_Z=fundManagementCharge_Z;
//				 System.out.println(month_E+"22. fundManagementCharge_Z "+fundManagementCharge_Z);

                sum_Z += fundManagementCharge_Z;
                /*if ((_month_E % 12) == 0) {

//					 System.out.println(year_F+"22. sum_Z "+sum_Z);
                    bussIll.append("<FundMgmtCharg4Pr" + _year_F + ">"
                            + obj.getRoundUp(obj.getStringWithout_E(sum_Z))
                            + "</FundMgmtCharg4Pr" + _year_F + ">");
					_sum_z=sum_Z;
					sum_Z = 0;
				}*/

                if ((_month_E % 12) == 0) {

//					 System.out.println(year_F+"22. sum_Z "+sum_Z);
                    bussIll.append("<FundMgmtCharg4Pr" + _year_F + ">"
                            + obj.getRound(obj.getStringWithout_E(sum_Z))
                            + "</FundMgmtCharg4Pr" + _year_F + ">");
                    _sum_z = sum_Z;
                    sum_Z = 0;
                }

                BIMAST.setGuaranteeCharge_AA(prop.guarantee_chg, policyTerm,
                        prop.guaranteeCharge, fundOption);
                guranteeCharge_AA = Double.parseDouble(BIMAST
                        .getGuaranteeCharge_AA());
                // _guranteeCharge_AA=guranteeCharge_AA;
                // System.out.println("23. guranteeCharge_AA "+guranteeCharge_AA);

                sum_AA += guranteeCharge_AA;
				/*if ((_month_E % 12) == 0) {

                    bussIll.append("<guranteeCharge4Pr" + _year_F + ">"
                            + obj.getRoundUp(obj.getStringWithout_E(sum_AA))
                            + "</guranteeCharge4Pr" + _year_F + ">");
					_sum_AA=sum_AA;
					bussIll.append("<otherCharge4Pr_partB" + _year_F + ">0</otherCharge4Pr_partB" + _year_F + ">");
					sum_AA = 0;
				}*/

                if ((_month_E % 12) == 0) {

                    bussIll.append("<guranteeCharge4Pr" + _year_F + ">"
                            + obj.getRound(obj.getStringWithout_E(sum_AA))
                            + "</guranteeCharge4Pr" + _year_F + ">");
                    _sum_AA = sum_AA;
                    bussIll.append("<otherCharge4Pr_partB" + _year_F + ">0</otherCharge4Pr_partB" + _year_F + ">");
                    sum_AA = 0;
                }

                BIMAST.setServiceTaxOnFMC_AB(prop.fundManagementCharges,
                        serviceTax, percentToBeInvested_EquityPensionFund,
                        percentToBeInvested_EquityOptPensionFund,
                        percentToBeInvested_GrowthPensionFund,
                        percentToBeInvested_BondPensionFund,
                        percentToBeInvested_MoneyMarketPesionFund, fundOption, policyTerm);
                serviceTaxOnFMC_AB = Double.parseDouble(BIMAST
                        .getServiceTaxOnFMC_AB());
                // _serviceTaxOnFMC_AB=serviceTaxOnFMC_AB;
//				 System.out.println("24. _serviceTaxOnFMC_AB "+serviceTaxOnFMC_AB);

                BIMAST.setTotalServiceTax_W(prop.riderCharges, serviceTax);
                totalServiceTax_W = Double.parseDouble(BIMAST
                        .getTotalServiceTax_W());
                // _totalServiceTax_W=totalServiceTax_W;
                // System.out.println("19. totalServiceTax_W "+totalServiceTax_W);

                if ((_month_E % 12) == 0) {
                    otherCharges4Pr = _sum_K + _sum_Q + _sum_AA + _sum_z;
//					 System.out.println(year_F+"22. sum_Z "+sum_Z);
                    bussIll.append("<OtherCharges4Pr" + _year_F + ">"
                            + obj.getRound(obj.getStringWithout_E(otherCharges4Pr))
                            + "</OtherCharges4Pr" + _year_F + ">");

                }

                sum_W += totalServiceTax_W;
				/*if ((_month_E % 12) == 0) {
					bussIll.append("<TotServTax4Pr" + _year_F + ">"
							+ obj.getRoundUp(obj.getStringWithout_E(sum_W))
							+ "</TotServTax4Pr" + _year_F + ">");
					sum_W = 0;
				}*/

                if ((_month_E % 12) == 0) {
                    bussIll.append("<TotServTax4Pr" + _year_F + ">"
                            + obj.getRound(obj.getStringWithout_E(sum_W))
                            + "</TotServTax4Pr" + _year_F + ">");
                    sum_W = 0;
                }

                BIMAST.setFundValueAfterFMCandBeforeGA_AC(serviceTax,
                        policyTerm);
                fundValueAfterFMCBerforeGA_AC = Double.parseDouble(BIMAST
                        .getFundValueAfterFMCandBeforeGA_AC());
                //*****Added By Tushar Kotian on 13/08/2018*** Start

				/*if ((_month_E % 12) == 0) {
                    bussIll.append("<fundValueAfterFMCBerforeGA_4Pr" + _year_F + ">"
                            + obj.getRoundUp(obj.getStringWithout_E(fundValueAfterFMCBerforeGA_AC))
                            + "</fundValueAfterFMCBerforeGA_4Pr" + _year_F + ">");

				}*/
                //Added By Tushar Kotian on 13/08/2018*** End

                // _fundValueAfterFMCBerforeGA_AC=fundValueAfterFMCBerforeGA_AC;
                // System.out.println("25 fundValueAfterFMCBerforeGA_AC "+fundValueAfterFMCBerforeGA_AC);

                sum_AC = Double.parseDouble(obj.getRoundUp(obj
                        .getStringWithout_E(fundValueAfterFMCBerforeGA_AC)));
                if ((_month_E % 12) == 0) {
                    _sum_AC = Double.parseDouble(obj.getRoundUp(obj
                            .getStringWithout_E(sum_AC)));
//					System.out.println("sum_ac " + sum_AC);
//					System.out.println("sum_ac " + _sum_AC);

                }

                BIMAST.setGuaranteedAddition_AD(policyTerm, annualPremium, premPayingOption);
                guaranteedAddition_AD = Double.parseDouble(BIMAST
                        .getGuaranteedAdditon_AD());
                // _guaranteedAddition_AD=guaranteedAddition_AD;
                // System.out.println("26. guaranteedAddition_AD "+guaranteedAddition_AD);

                sum_AD = guaranteedAddition_AD;
                if ((_month_E % 12) == 0) {
                    _sum_AD = Double.parseDouble(obj.getRoundUp(obj
                            .getStringWithout_E(sum_AD)));
                    bussIll.append("<guranteedAddtn4Pr" + _year_F + ">"
                            + obj.getRoundUp(obj.getStringWithout_E(sum_AD))
                            + "</guranteedAddtn4Pr" + _year_F + ">");
                    // System.out.println("_sum_AD "+_sum_AD);
                    sum_AD = 0;
                }

                BIMAST.setLoyaltyAddition_AE(prop.loyaltyAddition, policyTerm);
                loyaltiAddition_AE = Double.parseDouble(BIMAST
                        .getLoyaltyAddition_AE());
                // _loyaltiAddition_AE=loyaltiAddition_AE;
                // System.out.println("27. loyaltiAddition_AE "+loyaltiAddition_AE);
                //

                sum_AE = loyaltiAddition_AE;
                if ((_month_E % 12) == 0) {
                    _sum_AE = Double.parseDouble(obj.getRoundUp(obj
                            .getStringWithout_E(sum_AE)));
                    bussIll.append("<terminalAddtn4Pr" + _year_F + ">"
                            + obj.getRoundUp(obj.getStringWithout_E(sum_AE))
                            + "</terminalAddtn4Pr" + _year_F + ">");
                    // System.out.println("_sum_AE "+_sum_AE);
                    sum_AE = 0;
                }

                BIMAST.setFundValueAtEnd_AF();
                fundValueAtEnd_AF = Double.parseDouble(BIMAST
                        .getFundValueAtEnd_AF());
                // _fundValueAtEnd_AF=fundValueAtEnd_AF;
                // System.out.println("28. fundValueAtEnd_AF "+fundValueAtEnd_AF);

                // System.out.println("ae "+ _sum_AE);
                // System.out.println("ad "+_sum_AD);
                // System.out.println("ac "+sum_AC);
                // sum_AF =_sum_AC + _sum_AD + _sum_AE;
                if ((_month_E % 12) == 0) {

                    sum_AF = _sum_AD + _sum_AE + _sum_AC;

					/*bussIll.append("<FundValAtEnd4Pr" + _year_F + ">"
                            + obj.getRoundUp(obj.getStringWithout_E(sum_AF))
							+ "</FundValAtEnd4Pr" + _year_F + ">");*/

                    bussIll.append("<FundValAtEnd4Pr" + _year_F + ">"
                            + obj.getRound(obj.getStringWithout_E(sum_AF))
                            + "</FundValAtEnd4Pr" + _year_F + ">");
                    FundValAtEnd4Pr = sum_AF;
                    fundBeforeFMC4Pr = sum_AF + _sum_z;
                    bussIll.append("<fundBeforeFMC4Pr" + _year_F + ">"
                            + obj.getRound(obj.getStringWithout_E(fundBeforeFMC4Pr))
                            + "</fundBeforeFMC4Pr" + _year_F + ">");
                    fundValAt4_str = obj.getRoundUp(obj.getStringWithout_E(sum_AF));
                    sum_AF = 0;

                    // System.out.println("sum_AF "+sum_AF);
                }


                /***** Added by Tushar Kotian on 13/08/2018 *****/
				/*if ((_month_E % 12) == 0) {

                    if(year_F!= retireSmartBean.getPolicyTerm())
                    {
                        GuaranteedVestingBenefit = 0;
                    }
                    else
                    {
                        GuaranteedVestingBenefit = cumprem * 1.01 ;
                    }

                    bussIll.append("<GuaranteedVestingBenefit4Pr"
                            + _year_F
                            + ">"
                            + obj.getRoundUp(obj
                            .getStringWithout_E(GuaranteedVestingBenefit))
                            + "</GuaranteedVestingBenefit4Pr" + _year_F + ">");

                }
*/



				/*if ((_month_E % 12) == 0) {

                    if (year_F != retireSmartBean.getPolicyTerm()) {
                        NonGuaranteedVestingBenefit = 0;
                    } else {

                        if (year_F == retireSmartBean.getPolicyTerm()) {
                            NonGuaranteedVestingBenefit = FundValAtEnd4Pr * (1 + 0)
                                    - GuaranteedVestingBenefit;
                        } else {
                            NonGuaranteedVestingBenefit = FundValAtEnd4Pr * (1 + prop.loyaltyAddition)
                                    - GuaranteedVestingBenefit;
                        }
                    }

                    bussIll.append("<NonGuaranteedVestingBenefit4Pr"
                            + _year_F
                            + ">"
                            + obj.getRoundUp(obj
                            .getStringWithout_E(NonGuaranteedVestingBenefit))
                            + "</NonGuaranteedVestingBenefit4Pr" + _year_F
                            + ">");

                }

                if ((_month_E % 12) == 0)
                {
                    TotalVestingbenifit4 = GuaranteedVestingBenefit + NonGuaranteedVestingBenefit ;

                    bussIll.append("<TotalVestingbenifit4Pr"
                            + _year_F
                            + ">"
                            + obj.getRoundUp(obj
                            .getStringWithout_E(TotalVestingbenifit4))
                            + "</TotalVestingbenifit4Pr" + _year_F + ">");
                }

                if ((_month_E % 12) == 0) {

                    if (year_F >retireSmartBean.getPolicyTerm()) {
                        GuaranteedDeathBenefit4 = 0;
                    } else {
                        GuaranteedDeathBenefit4 = cumprem * 1.05;
                    }

                    bussIll.append("<GuaranteedDeathBenefit4Pr"
                            + _year_F
                            + ">"
                            + obj.getRoundUp(obj
                            .getStringWithout_E(GuaranteedDeathBenefit4))
                            + "</GuaranteedDeathBenefit4Pr" + _year_F + ">");

                }

                if ((_month_E % 12) == 0) {

                    if (year_F > retireSmartBean.getPolicyTerm()) {
                        NonGuaranteedDeathBenefit = 0;
                    } else {


                        double val1 = 0;
                        double val2 = 0;

                        if (year_F == retireSmartBean.getPolicyTerm()) {
                            val1 = FundValAtEnd4Pr * (1 + 0)
                                    - GuaranteedDeathBenefit4;
                        } else {
                            val1 = FundValAtEnd4Pr * (1 + prop.loyaltyAddition)
                                    - GuaranteedDeathBenefit4;
                        }

                        NonGuaranteedDeathBenefit = Math.max(val1, val2);
                    }

                    bussIll.append("<NonGuaranteedDeathBenefit4Pr"
                            + _year_F
                            + ">"
                            + obj.getRoundUp(obj
                            .getStringWithout_E(NonGuaranteedDeathBenefit))
                            + "</NonGuaranteedDeathBenefit4Pr" + _year_F + ">");



                }


                if ((_month_E % 12) == 0) {

                    TotalDeathBenifit = GuaranteedDeathBenefit4 + NonGuaranteedDeathBenefit ;

                    bussIll.append("<TotalDeathBenifit4Pr"
                            + _year_F
                            + ">"
                            + obj.getRoundUp(obj
                            .getStringWithout_E(TotalDeathBenifit))
                            + "</TotalDeathBenifit4Pr" + _year_F + ">");



                }



                if ((_month_E % 12) == 0) {

                    GuaranteedSurrenderBenifit = 0 ;

                    bussIll.append("<GuaranteedSurrenderBenifit4Pr"
                            + _year_F
                            + ">"
                            + obj.getRoundUp(obj
                            .getStringWithout_E(GuaranteedSurrenderBenifit))
                            + "</GuaranteedSurrenderBenifit4Pr" + _year_F + ">");



					}*/


                /***** Added by Tushar Kotian on 13/08/2018 *****/

                BIMAST.setSurrenderCap_BC(annualPremium, premPayingOption);
                surrenderCap_BC = Double.parseDouble(BIMAST
                        .getSurrenderCap_BC());
                // _surrenderCap_BC=surrenderCap_BC;
                // System.out.println("51. surrenderCap_BC "+surrenderCap_BC);

                BIMAST.setSurrenderCharges_AG(annualPremium, premPayingTerm, premPayingOption);
                surrenderCharges_AG = Double.parseDouble(BIMAST
                        .getSurrenderCharges_AG());
                // _surrenderCharges_AG=surrenderCharges_AG;
                // System.out.println("29. surrenderCharges_AG "+surrenderCharges_AG);

                BIMAST.setServiceTaxOnSurrenderCharges_AH(
                        prop.surrenderCharges, serviceTax);
                serviceTaxOnSurrenderCharges_AH = Double.parseDouble(BIMAST
                        .getServiceTaxOnSurrenderCharges_AH());
                // _serviceTaxOnSurrenderCharges_AH=serviceTaxOnSurrenderCharges_AH;
                // System.out.println("30. serviceTaxOnSurrenderCharges_AH "+serviceTaxOnSurrenderCharges_AH);

                BIMAST.setSurrenderValue_AI();
                surrenderValue_AI = Double.parseDouble(BIMAST
                        .getSurrenderValue_AI());
                // _surrenderValue_AI=surrenderValue_AI;
                // System.out.println("31. surrenderValue_AI "+surrenderValue_AI);

                if ((_month_E % 12) == 0) {
                    bussIll.append("<SurrVal4Pr"
                            + _year_F
                            + ">"
                            + obj.getRoundUp(obj
                            .getStringWithout_E(surrenderValue_AI))
                            + "</SurrVal4Pr" + _year_F + ">");

                }
                /***** Added by Tushar Kotian on 13/08/2018 *****/

				/*if ((_month_E % 12) == 0) {


                    TotalSurrenderBenifit4 = GuaranteedSurrenderBenifit + surrenderValue_AI;
                    bussIll.append("<TotalSurrenderBenifit4Pr"
                            + _year_F
                            + ">"
                            + obj.getRoundUp(obj
                            .getStringWithout_E(TotalSurrenderBenifit4))
                            + "</TotalSurrenderBenifit4Pr" + _year_F + ">");

				}*/


                if ((_month_E % 12) == 0) {

                    Commisionrate = 0;

                    if (retireSmartBean.getStaffDisc()) {
                        Commisionrate = 0;
                    } else if (premPayingOption.equals("Single")) {

                        {
                            Commisionrate = 0.02;
                        }
                    } else {

                        if (year_F == 1) {
                            if (retireSmartBean.getPolicyTerm() < 15) {
                                Commisionrate = 0.05;
                            } else {
                                Commisionrate = 0.075;

                            }
                        } else {
                            Commisionrate = 0.02;
                        }

                    }
                    Commision4 = (Commisionrate * _sum_I) + (topUpPremium_J * 0.01);


                    bussIll.append("<Commision"
                            + _year_F
                            + ">"
                            + obj.getRoundUp(obj
                            .getStringWithout_E(Commision4))
                            + "</Commision" + _year_F + ">");

                }

                /***** Added by Tushar Kotian on 13/08/2018 *****/

                sumOfPremium += premium_I;

                BIMAST.setDeathBenefit_AJ(sumOfPremium, prop.minGuarantee,
                        prop.loyaltyAddition, policyTerm);
                deathBenefit_AJ = Double.parseDouble(BIMAST
                        .getDeathBenefit_AJ());
                // _deathBenefit_AJ=deathBenefit_AJ;
                // System.out.println("32. deathBenefit_AJ "+deathBenefit_AJ);

                if ((_month_E % 12) == 0) {

                    bussIll.append("<DeathBen4Pr"
                            + _year_F
                            + ">"
                            + obj.getRoundUp(obj
                            .getStringWithout_E(deathBenefit_AJ))
                            + "</DeathBen4Pr" + _year_F + ">");

                }

                // System.out.println("**************** 8% *************************************");

                BIMAST.setMortalityCharges_AK(ageAtEntry, sumAssured,
                        forBIArray, policyTerm, prop.mortalityCharges,
                        fundValueAtEnd_AF, premPaymentOption);
                mortalityCharges_AK = Double.parseDouble(BIMAST
                        .getMortalityCharges_AK());
                // _mortalityCharges_AK=mortalityCharges_AK;
                // System.out.println("15. mortalityCharges_AK "+mortalityCharges_AK);
                sum_AK += mortalityCharges_AK;
                if ((_month_E % 12) == 0) {
                    bussIll.append("<MortChrg8Pr" + _year_F + ">"
                            + obj.getRoundUp(obj.getStringWithout_E(sum_AK))
                            + "</MortChrg8Pr" + _year_F + ">");
                    sum_AK = 0;
                }

                BIMAST.setAccTPDCharge_AL(policyTerm, prop.accTPD_Charge,
                        sumAssured, prop.mortalityCharges, fundValueAtEnd_AF,
                        premPaymentOption);
                accTPDCharges_AL = Double.parseDouble(BIMAST
                        .getAccTPDCharge_AL());
                // _accTPDCharges_AL=accTPDCharges_AL;
                // System.out.println("16. accTPDCharges_AL "+accTPDCharges_AL);

                BIMAST.setTotalCharges_AM(policyTerm);
                totalCharges_AM = Double.parseDouble(BIMAST
                        .getTotalCharges_AM());
                // _totalCharges_AM=totalCharges_AM;
                // System.out.println("17. totalCharges_AM "+totalCharges_AM);
                sum_AM += totalCharges_AM;
				/*if ((_month_E % 12) == 0) {
                    bussIll.append("<TotCharg8Pr" + _year_F + ">"
                            + Math.round(sum_AM) + "</TotCharg8Pr" + _year_F
                            + ">");
                    sum_AM = 0;
				}*/

                BIMAST.setServiceTaxExclOfSTOnAllocAndSurr_AN(
                        prop.mortalityAndRiderCharges,
                        prop.administrationCharges, serviceTax);
                serviceTaxExclOfSTOnAllocAndSurr_AN = Double.parseDouble(BIMAST
                        .getServiceTaxExclOfSTOnAllocAndSurr_AN());
                // _serviceTaxExclOfSTOnAllocAndSurr_AN=serviceTaxExclOfSTOnAllocAndSurr_AN;
                // System.out.println("18. serviceTaxExclOfSTOnAllocAndSurr_AN "+serviceTaxExclOfSTOnAllocAndSurr_AN);

                BIMAST.setAdditionToFundIfAny_AP(fundValueAtEnd_AX, policyTerm,
                        prop.int2);
                additionToFundIfAny_AP = Double.parseDouble(BIMAST
                        .getAdditionToFundIfAny_AP());
                // _additionToFundIfAny_AP=additionToFundIfAny_AP;
                // System.out.println("20. additionToFundIfAny_AP "+additionToFundIfAny_AP);

                sum_AdditionToFund_8Pr += additionToFundIfAny_AP;
                if ((_month_E % 12) == 0) {
                    bussIll.append("<additionToFund8Pr" + _year_F + ">"
                            + Math.round(sum_AdditionToFund_8Pr) + "</additionToFund8Pr" + _year_F
                            + ">");
                    sum_AdditionToFund_8Pr = 0;
                }

                BIMAST.setFundBeforeFMC_AQ(fundValueAtEnd_AX, policyTerm);
                fundBeforeFMC_AQ = Double.parseDouble(BIMAST
                        .getFundBeforeFMC_AQ());
                // _fundBeforeFMC_AQ=fundBeforeFMC_AQ;
                // System.out.println("21. fundBeforeFMC_AQ "+fundBeforeFMC_AQ);

                BIMAST.setFundManagementCharge_AR(policyTerm,
                        percentToBeInvested_EquityPensionFund,
                        percentToBeInvested_EquityOptPensionFund,
                        percentToBeInvested_GrowthPensionFund,
                        percentToBeInvested_BondPensionFund,
                        percentToBeInvested_MoneyMarketPesionFund, fundOption);
                fundManagementCharge_AR = Double.parseDouble(BIMAST
                        .getFundManagementCharge_AR());
                // _fundManagementCharge_AR=fundManagementCharge_AR;
                // System.out.println("22. fundManagementCharge_AR "+fundManagementCharge_AR);


                /***** Added by Tushar Kotian on 13/08/2018 *****/

//				if ((_month_E % 12) == 0) {
//
//					bussIll.append("<fundBeforeFMC8Pr" + _year_F + ">"
//							+ obj.getRoundUp(obj.getStringWithout_E(fundBeforeFMC_AQ))
//							+ "</fundBeforeFMC8Pr" + _year_F + ">");
//					sum_Z = 0;
//				}
                /***** Added by Tushar Kotian on 13/08/2018 *****/

                sum_AR += fundManagementCharge_AR;
				/*if ((_month_E % 12) == 0) {

                    bussIll.append("<FundMgmtCharg8Pr" + _year_F + ">"
                            + obj.getRoundUp(obj.getStringWithout_E(sum_AR))
                            + "</FundMgmtCharg8Pr" + _year_F + ">");
					_sum_AR=sum_AR;
                    sum_AR = 0;
				}*/

                if ((_month_E % 12) == 0) {

                    bussIll.append("<FundMgmtCharg8Pr" + _year_F + ">"
                            + obj.getRound(obj.getStringWithout_E(sum_AR))
                            + "</FundMgmtCharg8Pr" + _year_F + ">");
                    _sum_AR = sum_AR;
                    sum_AR = 0;
                }

                BIMAST.setGuaranteeCharge_AS(prop.guarantee_chg, policyTerm,
                        prop.guaranteeCharge, fundOption);
                guranteeCharge_AS = Double.parseDouble(BIMAST
                        .getGuaranteeCharge_AS());
                // _guranteeCharge_AS=guranteeCharge_AS;
                // System.out.println("23. guranteeCharge_AS "+guranteeCharge_AS);
                sum_AS += guranteeCharge_AS;
				/*if ((_month_E % 12) == 0) {

                    bussIll.append("<guranteeCharge8Pr" + _year_F + ">"
                            + obj.getRoundUp(obj.getStringWithout_E(sum_AS))
                            + "</guranteeCharge8Pr" + _year_F + ">");
					_sum_AS=sum_AS;
                    bussIll.append("<otherCharge8Pr_partB" + _year_F + ">0</otherCharge8Pr_partB" + _year_F + ">");
                    sum_AS = 0;
				}*/

                if ((_month_E % 12) == 0) {

                    bussIll.append("<guranteeCharge8Pr" + _year_F + ">"
                            + obj.getRound(obj.getStringWithout_E(sum_AS))
                            + "</guranteeCharge8Pr" + _year_F + ">");
                    _sum_AS = sum_AS;
                    bussIll.append("<otherCharge8Pr_partB" + _year_F + ">0</otherCharge8Pr_partB" + _year_F + ">");
                    sum_AS = 0;
                }

                BIMAST.setServiceTaxOnFMC_AT(prop.fundManagementCharges,
                        serviceTax, percentToBeInvested_EquityPensionFund,
                        percentToBeInvested_EquityOptPensionFund,
                        percentToBeInvested_GrowthPensionFund,
                        percentToBeInvested_BondPensionFund,
                        percentToBeInvested_MoneyMarketPesionFund, fundOption, policyTerm);
                serviceTaxOnFMC_AT = Double.parseDouble(BIMAST
                        .getServiceTaxOnFMC_AT());
                // _serviceTaxOnFMC_AT=serviceTaxOnFMC_AT;
//				 System.out.println("24. _serviceTaxOnFMC_AT "+serviceTaxOnFMC_AT);

                if ((_month_E % 12) == 0) {
                    otherCharges8Pr = _sum_K + _sum_Q + _sum_AS + _sum_AR;
//					 System.out.println(year_F+"22. sum_Z "+sum_Z);
                    bussIll.append("<OtherCharges8Pr" + _year_F + ">"
                            + obj.getRound(obj.getStringWithout_E(otherCharges8Pr))
                            + "</OtherCharges8Pr" + _year_F + ">");

                }

                BIMAST.setTotalServiceTax_AO(prop.riderCharges, serviceTax);
                totalServiceTax_AO = Double.parseDouble(BIMAST
                        .getTotalServiceTax_AO());
                // _totalServiceTax_AO=totalServiceTax_AO;
                // System.out.println("19. totalServiceTax_AO "+totalServiceTax_AO);

                sum_AO += totalServiceTax_AO;
				/*if ((_month_E % 12) == 0) {
                    bussIll.append("<TotServTxOnCharg8Pr" + _year_F + ">"
                            + obj.getRoundUp(obj.getStringWithout_E(sum_AO))
                            + "</TotServTxOnCharg8Pr" + _year_F + ">");
                    sum_AO = 0;
				}*/

                if ((_month_E % 12) == 0) {
                    bussIll.append("<TotServTxOnCharg8Pr" + _year_F + ">"
                            + obj.getRound(obj.getStringWithout_E(sum_AO))
                            + "</TotServTxOnCharg8Pr" + _year_F + ">");
                    sum_AO = 0;
                }

                BIMAST.setFundValueAfterFMCandBeforeGA_AU(serviceTax,
                        policyTerm);
                fundValueAfterFMCBerforeGA_AU = Double.parseDouble(BIMAST
                        .getFundValueAfterFMCandBeforeGA_AU());
                // _fundValueAfterFMCBerforeGA_AU=fundValueAfterFMCBerforeGA_AU;
                // System.out.println("25 fundValueAfterFMCBerforeGA_AU "+fundValueAfterFMCBerforeGA_AU);

                // *****Added By Tushar Kotian on 13/08/2018*** Start

//				if ((_month_E % 12) == 0) {
//					bussIll.append("<fundValueAfterFMCBerforeGA_8Pr"
//							+ _year_F
//							+ ">"
//							+ obj.getRoundUp(obj
//									.getStringWithout_E(fundValueAfterFMCBerforeGA_AU))
//							+ "</fundValueAfterFMCBerforeGA_8Pr" + _year_F
//							+ ">");
//
//				}
                // Added By Tushar Kotian on 13/08/2018*** End

                sum_AU = Double.parseDouble(obj.getRoundUp(obj
                        .getStringWithout_E(fundValueAfterFMCBerforeGA_AU)));
                if ((_month_E % 12) == 0) {
                    _sum_AU = Double.parseDouble(obj.getRoundUp(obj
                            .getStringWithout_E(sum_AU)));
//					System.out.println("sum_au " + sum_AU);
//					System.out.println("sum_au " + _sum_AU);

                }

                BIMAST.setGuaranteedAddition_AV(policyTerm, annualPremium, premPayingOption);
                guaranteedAddition_AV = Double.parseDouble(BIMAST
                        .getGuaranteedAdditon_AV());
                // _guaranteedAddition_AV=guaranteedAddition_AV;
                // System.out.println("26. guaranteedAddition_AV "+guaranteedAddition_AV);

//				sum_AV += guaranteedAddition_AV;
                sum_AV = guaranteedAddition_AV;
                if ((_month_E % 12) == 0) {
                    _sum_AV = Double.parseDouble(obj.getRoundUp(obj
                            .getStringWithout_E(sum_AV)));
                    bussIll.append("<guranteedAddtn8Pr" + _year_F + ">"
                            + obj.getRoundUp(obj.getStringWithout_E(sum_AV))
                            + "</guranteedAddtn8Pr" + _year_F + ">");
                    // System.out.println("_sum_AV "+_sum_AV);
                    sum_AV = 0;
                }

                BIMAST.setLoyaltyAddition_AW(prop.loyaltyAddition, policyTerm);
                loyaltiAddition_AW = Double.parseDouble(BIMAST
                        .getLoyaltyAddition_AW());
                // _loyaltiAddition_AW=loyaltiAddition_AW;
                // System.out.println("27. loyaltiAddition_AW "+loyaltiAddition_AW);

                sum_AW += loyaltiAddition_AW;
				/*if ((_month_E % 12) == 0) {
                    _sum_AW = Double.parseDouble(obj.getRoundUp(obj
                            .getStringWithout_E(sum_AW)));
                    bussIll.append("<terminalAddtn8Pr" + _year_F + ">"
                            + obj.getRoundUp(obj.getStringWithout_E(sum_AW))
                            + "</terminalAddtn8Pr" + _year_F + ">");
//					 System.out.println("_sum_AW "+_sum_AW);
                    sum_AW = 0;
				}*/

                if ((_month_E % 12) == 0) {
                    _sum_AW = Double.parseDouble(obj.getRoundUp(obj
                            .getStringWithout_E(sum_AW)));
                    bussIll.append("<terminalAddtn8Pr" + _year_F + ">"
                            + obj.getRound(obj.getStringWithout_E(sum_AW))
                            + "</terminalAddtn8Pr" + _year_F + ">");
                    // System.out.println("_sum_AW "+_sum_AW);
                    sum_AW = 0;
                }

                BIMAST.setFundValueAtEnd_AX();
                fundValueAtEnd_AX = Double.parseDouble(BIMAST
                        .getFundValueAtEnd_AX());
                // _fundValueAtEnd_AX=fundValueAtEnd_AX;
                // System.out.println("28. fundValueAtEnd_AX "+fundValueAtEnd_AX);

                if ((_month_E % 12) == 0) {

                    sum_AX = _sum_AU + _sum_AV + _sum_AW;

					/*bussIll.append("<FundValAtEnd8Pr" + _year_F + ">"
							+ obj.getRoundUp(obj.getStringWithout_E(sum_AX))
							+ "</FundValAtEnd8Pr" + _year_F + ">");*/

                    bussIll.append("<FundValAtEnd8Pr" + _year_F + ">"
                            + obj.getRound(obj.getStringWithout_E(sum_AX))
                            + "</FundValAtEnd8Pr" + _year_F + ">");
                    FundValAtEnd8Pr = sum_AX;
                    fundBeforeFMC8Pr = sum_AX + _sum_AR;
                    bussIll.append("<fundBeforeFMC8Pr" + _year_F + ">"
                            + obj.getRound(obj.getStringWithout_E(fundBeforeFMC8Pr))
                            + "</fundBeforeFMC8Pr" + _year_F + ">");
                    fundValAt8_str = obj.getRoundUp(obj.getStringWithout_E(sum_AX));
                    sum_AF = 0;

                    // System.out.println("sum_AF "+sum_AF);
                }


                /***** Added by Tushar Kotian on 13/08/2018 *****/
				/*if ((_month_E % 12) == 0) {

                    if(year_F!= retireSmartBean.getPolicyTerm())
                    {
                        GuaranteedVestingBenefit8 = 0;
                    }
                    else
                    {
                        GuaranteedVestingBenefit8 = cumprem * 1.01 ;
                    }

                    bussIll.append("<GuaranteedVestingBenefit8Pr"
                            + _year_F
                            + ">"
                            + obj.getRoundUp(obj
                            .getStringWithout_E(GuaranteedVestingBenefit8))
                            + "</GuaranteedVestingBenefit8Pr" + _year_F + ">");

                }




                if ((_month_E % 12) == 0) {

                    if (year_F != retireSmartBean.getPolicyTerm()) {
                        NonGuaranteedVestingBenefit8 = 0;
                    } else {

                        if (year_F == retireSmartBean.getPolicyTerm()) {
                            NonGuaranteedVestingBenefit8 = FundValAtEnd8Pr * (1 + 0)
                                    - GuaranteedVestingBenefit8;
                        } else {
                            NonGuaranteedVestingBenefit8 = FundValAtEnd8Pr *  (1 + prop.loyaltyAddition)
                                    - GuaranteedVestingBenefit8;
                        }
                    }

                    bussIll.append("<NonGuaranteedVestingBenefit8Pr"
                            + _year_F
                            + ">"
                            + obj.getRoundUp(obj
                            .getStringWithout_E(NonGuaranteedVestingBenefit8))
                            + "</NonGuaranteedVestingBenefit8Pr" + _year_F
                            + ">");

                }

                if ((_month_E % 12) == 0)
                {
                    TotalVestingbenifit8 = GuaranteedVestingBenefit8 + NonGuaranteedVestingBenefit8 ;

                    bussIll.append("<TotalVestingbenifit8Pr"
                            + _year_F
                            + ">"
                            + obj.getRoundUp(obj
                            .getStringWithout_E(TotalVestingbenifit8))
                            + "</TotalVestingbenifit8Pr" + _year_F + ">");
                }

                if ((_month_E % 12) == 0) {

                    if (year_F >retireSmartBean.getPolicyTerm()) {
                        GuaranteedDeathBenefit8 = 0;
                    } else {
                        GuaranteedDeathBenefit8 = cumprem * 1.05;
                    }

                    bussIll.append("<GuaranteedDeathBenefit8Pr"
                            + _year_F
                            + ">"
                            + obj.getRoundUp(obj
                            .getStringWithout_E(GuaranteedDeathBenefit8))
                            + "</GuaranteedDeathBenefit8Pr" + _year_F + ">");

                }

                if ((_month_E % 12) == 0) {

                    if (year_F > retireSmartBean.getPolicyTerm()) {
                        NonGuaranteedDeathBenefit8 = 0;
                    } else {


                        double val1 = 0;
                        double val2 = 0;

                        if (year_F == retireSmartBean.getPolicyTerm()) {
                            val1 = FundValAtEnd8Pr * (1 + 0)
                                    - GuaranteedDeathBenefit8;
                        } else {
                            val1 = FundValAtEnd8Pr * (1 + prop.loyaltyAddition)
                                    - GuaranteedDeathBenefit8;
                        }

                        NonGuaranteedDeathBenefit8 = Math.max(val1, val2);
                    }

                    bussIll.append("<NonGuaranteedDeathBenefit8Pr"
                            + _year_F
                            + ">"
                            + obj.getRoundUp(obj
                            .getStringWithout_E(NonGuaranteedDeathBenefit8))
                            + "</NonGuaranteedDeathBenefit8Pr" + _year_F + ">");



                }


                if ((_month_E % 12) == 0) {

                    TotalDeathBenifit8 = GuaranteedDeathBenefit8 + NonGuaranteedDeathBenefit8 ;

                    bussIll.append("<TotalDeathBenifit8Pr"
                            + _year_F
                            + ">"
                            + obj.getRoundUp(obj
                            .getStringWithout_E(TotalDeathBenifit8))
                            + "</TotalDeathBenifit8Pr" + _year_F + ">");



                }



                if ((_month_E % 12) == 0) {

                    GuaranteedSurrenderBenifit8 = 0 ;

                    bussIll.append("<GuaranteedSurrenderBenifit8Pr"
                            + _year_F
                            + ">"
                            + obj.getRoundUp(obj
                            .getStringWithout_E(GuaranteedSurrenderBenifit8))
                            + "</GuaranteedSurrenderBenifit8Pr" + _year_F + ">");



					}*/


                /***** Added by Tushar Kotian on 13/08/2018 *****/
                BIMAST.setSurrenderCharges_AY(annualPremium, premPayingTerm, premPayingOption);
                surrenderCharges_AY = Double.parseDouble(BIMAST
                        .getSurrenderCharges_AY());
                // _surrenderCharges_AY=surrenderCharges_AY;
                // System.out.println("29. surrenderCharges_AY "+surrenderCharges_AY);

                BIMAST.setServiceTaxOnSurrenderCharges_AZ(
                        prop.surrenderCharges, serviceTax);
                serviceTaxOnSurrenderCharges_AZ = Double.parseDouble(BIMAST
                        .getServiceTaxOnSurrenderCharges_AZ());
                // _serviceTaxOnSurrenderCharges_AZ=serviceTaxOnSurrenderCharges_AZ;
                // System.out.println("30. serviceTaxOnSurrenderCharges_AZ "+serviceTaxOnSurrenderCharges_AZ);

                BIMAST.setSurrenderValue_BA();
                surrenderValue_BA = Double.parseDouble(BIMAST
                        .getSurrenderValue_BA());
                // _surrenderValue_BA=surrenderValue_BA;
                // System.out.println("31. surrenderValue_BA "+surrenderValue_BA);

                if ((_month_E % 12) == 0) {
                    bussIll.append("<SurrVal8Pr"
                            + _year_F
                            + ">"
                            + obj.getRoundUp(obj
                            .getStringWithout_E(surrenderValue_BA))
                            + "</SurrVal8Pr" + _year_F + ">");

                }

                /***** Added by Tushar Kotian on 13/08/2018 *****/

				/*if ((_month_E % 12) == 0) {


                    TotalSurrenderBenifit8 = GuaranteedSurrenderBenifit8 + surrenderValue_BA;
                    bussIll.append("<TotalSurrenderBenifit8Pr"
                            + _year_F
                            + ">"
                            + obj.getRoundUp(obj
                            .getStringWithout_E(TotalSurrenderBenifit8))
                            + "</TotalSurrenderBenifit8Pr" + _year_F + ">");


					*//***** Added by Tushar Kotian on 13/08/2018 *****//*

				}*/


                BIMAST.setDeathBenefit_BB(sumOfPremium, prop.minGuarantee,
                        prop.loyaltyAddition, policyTerm);
                deathBenefit_BB = Double.parseDouble(BIMAST
                        .getDeathBenefit_BB());
                // _deathBenefit_BB=deathBenefit_BB;
                // System.out.println("32. deathBenefit_BB "+deathBenefit_BB);

                if ((_month_E % 12) == 0) {

                    bussIll.append("<DeathBen8Pr"
                            + _year_F
                            + ">"
                            + obj.getRoundUp(obj
                            .getStringWithout_E(deathBenefit_BB))
                            + "</DeathBen8Pr" + _year_F + ">");

                }

                if ((_month_E % 12) == 0) {
                    Commission_AQ = BIMAST.getCommission_AQ(_sum_I, sum_J,
                            retireSmartBean);
                    bussIll.append("<CommIfPay8Pr"
                            + _year_F
                            + ">"
                            + obj.getRoundUp(obj
                            .getStringWithout_E(Commission_AQ))
                            + "</CommIfPay8Pr" + _year_F + ">");

                }
            }

//			System.out.println("policyTerm "+policyTerm);
//			System.out.println("ageAtEntry "+ageAtEntry);
//			System.out.println("premFreqMode "+premFreqMode);
//			System.out.println("fundValueAtEnd_AF "+ Double
//					.parseDouble(obj.getRoundUp(obj
//							.getStringWithout_E(fundValueAtEnd_AF))));
//			System.out.println(fundValueAtEnd_AF);

            BIMAST.setAnnuityPay4pa(fundValueAtEnd_AF);
            annuityPay4pa = Double.parseDouble(BIMAST.getAnnuityPay4pa());

            BIMAST.setAnnuityPay8pa(fundValueAtEnd_AX);
            annuityPay8pa = Double.parseDouble(BIMAST.getAnnuityPay8pa());

            // System.out.println("Annuity Payable : "+
            // annuityPay4pa+"  &  "+annuityPay8pa);
            totalFYPremium = BIMAST.getTotalFirstYearPremium(sum_C, sum_E,
                    sum_FY_Premium);

            BIMAST.setAnnuityRates(policyTerm, ageAtEntry);
            annuityRates = (BIMAST.getpremiumRate_Monthly());

            vestingBenefit11 = cumprem * 1.01;

            annuityAmount_MiniBen = BIMAST.getAnnuityAmount(vestingBenefit11);
//			System.out.println(vestingBenefit11);

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

        return new String[]{(obj.getStringWithout_E(sumAssured)),
                obj.getRoundUp(obj.getStringWithout_E(fundValueAtEnd_AF)),
                obj.getRoundUp(obj.getStringWithout_E(fundValueAtEnd_AX)),
                obj.getRoundUp(obj.getStringWithout_E(annualPremium)),
                obj.getRoundUp(obj.getStringWithout_E(totalFYPremium)),
                obj.getStringWithout_E(Math.round(annuityPay4pa)),
                obj.getStringWithout_E(Math.round(annuityPay8pa)),
                obj.getRoundOffLevel2(String.valueOf(annuityRates)),
                obj.getStringWithout_E(vestingBenefit11),
                obj.getStringWithout_E(annuityAmount_MiniBen)};

    }

    /************************************************************* Reduction In Yield Starts Here *******************************************************/

    private String[] getOutputReductionYield(String sheetName,
                                             RetireSmartBean retireSmartBean) {
        // RetireSmartProperties prop=new RetireSmartProperties();
        RetireSmartBusinessLogic BIMAST = new RetireSmartBusinessLogic(retireSmartBean);

        RetireSmartProperties prop = new RetireSmartProperties();
        // ouput variable declaration
        int _month_E = 0, _year_F = 0, _age_H = 0, _month_BT = 0;

        String _policyInforce_G = "Y";

        ArrayList<String> List_BV = new ArrayList<>();
        ArrayList<String> List_BZ = new ArrayList<>();
        ArrayList<String> List_BU = new ArrayList<>();

        // double _premium_I=0,
        // _topUpPremium_J=0,
        // _premiumAllocationCharge_K=0,
        // _topUpCharges_L=0,
        // _ServiceTaxOnAllocation_M=0,
        // _amountAvailableForInvestment_N=0,
        // _sumAssuredRelatedCharges_O=0,
        // _riderCharges_P=0,
        // _policyAdministrationCharges_Q=0,
        // _serviceTaxOnPolicyAdminisrtationCharge_R=0,
        // _mortalityCharges_S=0,
        // _accTPDCharges_T=0,
        // _totalCharges_U=0,
        // _serviceTaxExclOfSTOnAllocAndSurr_V=0,
        // _totalServiceTax_W=0,
        // _additionToFundIfAny_X=0,
        // _fundBeforeFMC_Y=0,
        // _fundManagementCharge_Z=0,
        // _guranteeCharge_AA=0,
        // _serviceTaxOnFMC_AB=0,
        // _fundValueAfterFMCBerforeGA_AC=0,
        // _guaranteedAddition_AD=0,
        // _loyaltiAddition_AE=0,
        // _fundValueAtEnd_AF=0,
        // _surrenderCharges_AG=0,
        // _serviceTaxOnSurrenderCharges_AH=0,
        // _surrenderValue_AI=0,
        // _deathBenefit_AJ=0,
        // _mortalityCharges_AK=0,
        // _accTPDCharges_AL=0,
        // _totalCharges_AM=0,
        // _serviceTaxExclOfSTOnAllocAndSurr_AN=0,
        // _totalServiceTax_AO=0,
        // _additionToFundIfAny_AP=0,
        // _fundBeforeFMC_AQ=0,
        // _fundManagementCharge_AR=0,
        // _guranteeCharge_AS=0,
        // _serviceTaxOnFMC_AT=0,
        // _fundValueAfterFMCBerforeGA_AU=0,
        // _guaranteedAddition_AV=0,
        // _loyaltiAddition_AW=0,
        // _fundValueAtEnd_AX=0,
        // _surrenderCharges_AY=0,
        // _serviceTaxOnSurrenderCharges_AZ=0,
        // _surrenderValue_BA=0,
        // _deathBenefit_BB=0,
        // _surrenderCap_BC=0,
        // _oneHundredPercentOfCummulativePremium_BD=0,
        // _reductionYield_BZ=0,
        // _reductionYield_BV=0,
        // _irrAnnual_BV=0,
        // _irrAnnual_BZ=0,
        // _reductionInYieldMaturityAt=0,
        // _reductionInYieldNumberOfYearsElapsedSinceInception=0;

        // temp variable declaration
        int month_E = 0, year_F = 0, age_H = 0, month_BT = 0;

        String policyInforce_G = "Y";

        double premium_I = 0, topUpPremium_J = 0, premiumAllocationCharge_K = 0, topUpCharges_L = 0, ServiceTaxOnAllocation_M = 0, amountAvailableForInvestment_N = 0, sumAssuredRelatedCharges_O = 0, riderCharges_P = 0, policyAdministrationCharges_Q = 0, serviceTaxOnPolicyAdminisrtationCharge_R = 0, mortalityCharges_S = 0, accTPDCharges_T = 0, totalCharges_U = 0, serviceTaxExclOfSTOnAllocAndSurr_V = 0, totalServiceTax_W = 0, additionToFundIfAny_X = 0, fundBeforeFMC_Y = 0, fundManagementCharge_Z = 0, guranteeCharge_AA = 0, serviceTaxOnFMC_AB = 0, fundValueAfterFMCBerforeGA_AC = 0, guaranteedAddition_AD = 0, loyaltiAddition_AE = 0, fundValueAtEnd_AF = 0, surrenderCharges_AG = 0, serviceTaxOnSurrenderCharges_AH = 0, surrenderValue_AI = 0, deathBenefit_AJ = 0, mortalityCharges_AK = 0, accTPDCharges_AL = 0, totalCharges_AM = 0, serviceTaxExclOfSTOnAllocAndSurr_AN = 0, totalServiceTax_AO = 0, additionToFundIfAny_AP = 0, fundBeforeFMC_AQ = 0, fundManagementCharge_AR = 0, guranteeCharge_AS = 0, serviceTaxOnFMC_AT = 0, fundValueAfterFMCBerforeGA_AU = 0, guaranteedAddition_AV = 0, loyaltiAddition_AW = 0, fundValueAtEnd_AX = 0, surrenderCharges_AY = 0, serviceTaxOnSurrenderCharges_AZ = 0, surrenderValue_BA = 0, deathBenefit_BB = 0, surrenderCap_BC = 0, oneHundredPercentOfCummulativePremium_BD = 0, reductionYield_BZ = 0, reductionYield_BV = 0, irrAnnual_BV = 0, irrAnnual_BZ = 0, reductionInYieldMaturityAt = 0, reductionInYieldNumberOfYearsElapsedSinceInception = 0, netYield4pa = 0, netYield8pa = 0,
                // _irrAnnual_BU=0,
                irrAnnual_BU = 0, reductionYield_BU = 0;
        // _reductionYield_BU=0;

        // GUI input
        double SAMF = 1;
        boolean staffDisc = retireSmartBean.getStaffDisc();
        int ageAtEntry = retireSmartBean.getAge();
        int policyTerm = retireSmartBean.getPolicyTerm();
        String premFreqMode = retireSmartBean.getPremFrequencyMode();
        double premiumAmount = retireSmartBean.getPremiumAmount();
        String premPayingOption = retireSmartBean.getPremPayingOption();
        int premPayingTerm = retireSmartBean.getPremiumPayingTerm();
        int noOfYearsElapsedSinceInception = retireSmartBean
                .getNoOfYearsElapsedSinceInception();
        String fundOption = retireSmartBean.getBi_retire_smart_plan_option();
//  Added By Saurabh Jain on 10/06/2019 Start

        double serviceTax = 0;
        //double serviceTax=0;
        boolean isKerlaDisc = retireSmartBean.isKerlaDisc();

        int PF = retireSmartBean.getPF();
        double annualPremium = premiumAmount * PF;
        double sumAssured = annualPremium * SAMF;
        String addTopUp = "No";
        double effectiveTopUpPrem = retireSmartBean.getEffectiveTopUpPrem();
        // RetireSmartBusinessLogic BIMAST=new RetireSmartBusinessLogic();
        String[] forBIArray = BIMAST
                .getForBIArr(prop.mortalityCharges_AsPercentOfofLICtable);
        int rowNumber = 0;
        String premPaymentOption = "NO";
        double percentToBeInvested_EquityPensionFund = 0, percentToBeInvested_EquityOptPensionFund = 0, percentToBeInvested_GrowthPensionFund = 0, percentToBeInvested_BondPensionFund = 0, percentToBeInvested_MoneyMarketPesionFund = 0;
        double sumOfPremium = 0;
        int monthNumber = 0;

        try {
            // calcultion of fields
            // for(int i=0; i <= 2; i++)
            for (int i = 0; i <= (policyTerm * 12); i++) {
                rowNumber++;

                BIMAST.setMonth_E(rowNumber);
                month_E = Integer.parseInt(BIMAST.getMonth_E());
                _month_E = month_E;
                // System.out.println("1. month_E " +BIMAST.getMonth_E());

                BIMAST.setYear_F();
                year_F = Integer.parseInt(BIMAST.getYear_F());
                _year_F = year_F;
                // System.out.println("2. year_F "+BIMAST.getYear_F());

              /*  policyInforce_G = BIMAST.getPolicyInForce_G();
                _policyInforce_G = BIMAST.getPolicyInForce_G();*/
                // System.out.println("3. policyInforce_G "+BIMAST.getPolicyInForce_G());
                if (isKerlaDisc == true && _year_F <= 2) {
                    serviceTax = 0.19;
                } else {
                    serviceTax = 0.18;
                }

//				Added By Saurabh Jain on 20/06/2019 End

                policyInforce_G = BIMAST.getPolicyInForce_G();
                _policyInforce_G = BIMAST.getPolicyInForce_G();
                // System.out.println("3. policyInforce_G "+BIMAST.getPolicyInForce_G());

                BIMAST.setAge_H(ageAtEntry);
                age_H = Integer.parseInt(BIMAST.getAge_H());
                _age_H = age_H;
                // System.out.println("4. age_H "+BIMAST.getAge_H());

                BIMAST.setPremium_I(premPayingTerm, PF, annualPremium);
                premium_I = Double.parseDouble(BIMAST.getPremium_I());
                // _premium_I=premium_I;
                // System.out.println("5. premium_I "+BIMAST.getPremium_I());

                BIMAST.setTopUpPremium_J(prop.topUpStatus, effectiveTopUpPrem,
                        addTopUp);
                topUpPremium_J = Double.parseDouble(BIMAST.getTopUpPremium_J());
                // _topUpPremium_J=topUpPremium_J;
                // System.out.println("6. Top up premium charges "+BIMAST.getTopUpPremium_J());

                BIMAST.setPremiumAllocationCharge_K(policyTerm, staffDisc, false, annualPremium, premPayingOption, premPayingTerm);
                premiumAllocationCharge_K = Double.parseDouble(BIMAST
                        .getPremiumAllocationCharge_K());
                // _premiumAllocationCharge_K=premiumAllocationCharge_K;
                // System.out.println("7. premiumAllocationCharge_K "+premiumAllocationCharge_K);

                BIMAST.setTopUpCharges_L(prop.topUp);
                topUpCharges_L = Double.parseDouble(BIMAST.getTopUpCharges_L());
                // _topUpCharges_L=topUpCharges_L;
                // System.out.println("8. topUpCharges_L "+topUpCharges_L);

                BIMAST.setServiceTaxOnAllocation_M(
                        prop.allocationChargesReductionYield, serviceTax);
                ServiceTaxOnAllocation_M = Double.parseDouble(BIMAST
                        .getServiceTaxOnAllocation_M());
                // _ServiceTaxOnAllocation_M=ServiceTaxOnAllocation_M;
                // System.out.println("9. ServiceTaxOnAllocation_M "+ServiceTaxOnAllocation_M);

                BIMAST.setAmountAvailableForInvestment_N(policyTerm);
                amountAvailableForInvestment_N = Double.parseDouble(BIMAST
                        .getAmountAvailableForInvestment_N());
                // _amountAvailableForInvestment_N=amountAvailableForInvestment_N;
                // System.out.println("10. amountAvailableForInvestment_N "+amountAvailableForInvestment_N);

                BIMAST.setSumAssuredRelatedCharges_O(policyTerm, sumAssured,
                        prop.charge_SumAssuredBase);
                sumAssuredRelatedCharges_O = Double.parseDouble(BIMAST
                        .getSumAssuredRelatedCharges_O());
                // _sumAssuredRelatedCharges_O=sumAssuredRelatedCharges_O;
                // System.out.println("11. sumAssuredRelatedCharges_O "+sumAssuredRelatedCharges_O);

                BIMAST.setRiderCharges_P();
                riderCharges_P = Double.parseDouble(BIMAST.getRiderCharges_P());
                // _riderCharges_P=riderCharges_P;
                // System.out.println("12. _riderCharges_P "+_riderCharges_P);

                BIMAST.setPolicyAdministrationCharge_Q(
                        policyAdministrationCharges_Q, prop.charge_Inflation,
                        prop.fixedMonthlyExp_RP, prop.inflation_pa_RP);
                policyAdministrationCharges_Q = Double.parseDouble(BIMAST
                        .getPolicyAdministrationCharge_Q());
                // _policyAdministrationCharges_Q=policyAdministrationCharges_Q;
                // System.out.println("13. policyAdministrationCharges_Q "+policyAdministrationCharges_Q);

                BIMAST.setServiceTaxOnPolicyAdministrationCharges_R(
                        prop.administrationChargesReductionYield,
                        serviceTax);
                serviceTaxOnPolicyAdminisrtationCharge_R = Double
                        .parseDouble(BIMAST
                                .getServiceTaxOnPolicyAdministrationCharges_R());
                // _serviceTaxOnPolicyAdminisrtationCharge_R=serviceTaxOnPolicyAdminisrtationCharge_R;
                // System.out.println("14. serviceTaxOnPolicyAdminisrtationCharge_R "+serviceTaxOnPolicyAdminisrtationCharge_R);

                BIMAST.setOneHundredPercentOfCummulativePremium_BD(oneHundredPercentOfCummulativePremium_BD);
                oneHundredPercentOfCummulativePremium_BD = Double
                        .parseDouble(BIMAST
                                .getOneHundredPercentOfCummulativePremium_BD());
                // _oneHundredPercentOfCummulativePremium_BD=oneHundredPercentOfCummulativePremium_BD;
                // System.out.println("46. oneHundredPercentOfCummulativePremium_BD "+oneHundredPercentOfCummulativePremium_BD);

                BIMAST.setMortalityCharges_S(ageAtEntry, sumAssured,
                        forBIArray, policyTerm, prop.mortalityCharges,
                        fundValueAtEnd_AF, premPaymentOption);
                mortalityCharges_S = Double.parseDouble(BIMAST
                        .getMortalityCharges_S());
                // _mortalityCharges_S=mortalityCharges_S;
                // System.out.println("15. mortalityCharges_S "+mortalityCharges_S);

                BIMAST.setAccTPDCharge_T(policyTerm, prop.accTPD_Charge,
                        sumAssured, prop.mortalityCharges, fundValueAtEnd_AF,
                        premPaymentOption);
                accTPDCharges_T = Double
                        .parseDouble(BIMAST.getAccTPDCharge_T());
                // _accTPDCharges_T=accTPDCharges_T;
                // System.out.println("16. accTPDCharges_T "+accTPDCharges_T);

                BIMAST.setTotalCharges_U(policyTerm);
                totalCharges_U = Double.parseDouble(BIMAST.getTotalCharges_U());
                // _totalCharges_U=totalCharges_U;
                // System.out.println("17. totalCharges_U "+totalCharges_U);

                BIMAST.setServiceTaxExclOfSTOnAllocAndSurr_V(
                        prop.mortalityAndRiderChargesReductionYield,
                        prop.administrationChargesReductionYield,
                        serviceTax);
                serviceTaxExclOfSTOnAllocAndSurr_V = Double.parseDouble(BIMAST
                        .getServiceTaxExclOfSTOnAllocAndSurr_V());
                // _serviceTaxExclOfSTOnAllocAndSurr_V=serviceTaxExclOfSTOnAllocAndSurr_V;
                // System.out.println("18. serviceTaxExclOfSTOnAllocAndSurr_V "+serviceTaxExclOfSTOnAllocAndSurr_V);

                BIMAST.setAdditionToFundIfAny_X(fundValueAtEnd_AF, policyTerm,
                        prop.int1);
                additionToFundIfAny_X = Double.parseDouble(BIMAST
                        .getAdditionToFundIfAny_X());
                // _additionToFundIfAny_X=additionToFundIfAny_X;
                // System.out.println("20. additionToFundIfAny_X "+additionToFundIfAny_X);

                BIMAST.setFundBeforeFMCReductionYield_Y(fundValueAtEnd_AF,
                        policyTerm);
                fundBeforeFMC_Y = Double.parseDouble(BIMAST
                        .getFundBeforeFMCReductionYield_Y());
                // _fundBeforeFMC_Y=fundBeforeFMC_Y;
                // System.out.println("21. fundBeforeFMC_Y "+fundBeforeFMC_Y);

                BIMAST.setFundManagementChargeReductionYield_Z(policyTerm,
                        percentToBeInvested_EquityPensionFund,
                        percentToBeInvested_EquityOptPensionFund,
                        percentToBeInvested_GrowthPensionFund,
                        percentToBeInvested_BondPensionFund,
                        percentToBeInvested_MoneyMarketPesionFund, fundOption);
                fundManagementCharge_Z = Double.parseDouble(BIMAST
                        .getFundManagementChargeReductionYield_Z());
                // _fundManagementCharge_Z=fundManagementCharge_Z;
                // System.out.println("22. fundManagementCharge_Z "+fundManagementCharge_Z);

                BIMAST.setGuaranteeChargeReductionYield_AA(prop.guarantee_chg,
                        policyTerm, prop.guaranteeChargeReductionYield, fundOption);
                guranteeCharge_AA = Double.parseDouble(BIMAST
                        .getGuaranteeChargeReductionYield_AA());
                // _guranteeCharge_AA=guranteeCharge_AA;
                // System.out.println("23. guranteeCharge_AA "+guranteeCharge_AA);

                BIMAST.setServiceTaxOnFMCReductionYield_AB(
                        prop.fundManagementChargesReductionYield,
                        serviceTax, percentToBeInvested_EquityPensionFund,
                        percentToBeInvested_EquityOptPensionFund,
                        percentToBeInvested_GrowthPensionFund,
                        percentToBeInvested_BondPensionFund,
                        percentToBeInvested_MoneyMarketPesionFund, fundOption, policyTerm);
                serviceTaxOnFMC_AB = Double.parseDouble(BIMAST
                        .getServiceTaxOnFMCReductionYield_AB());
                // _serviceTaxOnFMC_AB=serviceTaxOnFMC_AB;
                // System.out.println("24. _serviceTaxOnFMC_AB "+_serviceTaxOnFMC_AB);

                BIMAST.setTotalServiceTaxReductionYield_W(serviceTax,
                        prop.riderCharges);
                totalServiceTax_W = Double.parseDouble(BIMAST
                        .getTotalServiceTaxReductionYield_W());
                // _totalServiceTax_W=totalServiceTax_W;
                // System.out.println("19. totalServiceTax_W "+totalServiceTax_W);

                BIMAST.setFundValueAfterFMCandBeforeGAReductionYield_AC(
                        serviceTax, policyTerm);
                fundValueAfterFMCBerforeGA_AC = Double.parseDouble(BIMAST
                        .getFundValueAfterFMCandBeforeGAReductionYield_AC());
                // _fundValueAfterFMCBerforeGA_AC=fundValueAfterFMCBerforeGA_AC;
                // System.out.println("25 fundValueAfterFMCBerforeGA_AC "+fundValueAfterFMCBerforeGA_AC);

                BIMAST.setGuaranteedAdditionReductionYield_AD(policyTerm,
                        annualPremium, prop.guaranteedAddition);
                guaranteedAddition_AD = Double.parseDouble(BIMAST
                        .getGuaranteedAdditonReductionYield_AD());
                // _guaranteedAddition_AD=guaranteedAddition_AD;
                // System.out.println("26. guaranteedAddition_AD "+guaranteedAddition_AD);

                BIMAST.setLoyaltyAdditionReductionYield_AE(
                        prop.loyaltyAddition, policyTerm, prop.terminalAddition);
                loyaltiAddition_AE = Double.parseDouble(BIMAST
                        .getLoyaltyAdditionReductionYield_AE());
                // _loyaltiAddition_AE=loyaltiAddition_AE;
                // System.out.println("27. loyaltiAddition_AE "+loyaltiAddition_AE);

                BIMAST.setMonth_BT(monthNumber);
                month_BT = Integer.parseInt(BIMAST.getMonth_BT());
                _month_BT = month_BT;
                // System.out.println("month_BT "+month_BT);

                BIMAST.setReductionYield_BU(policyTerm, fundValueAtEnd_AF);
                reductionYield_BU = Double.parseDouble(BIMAST
                        .getReductionYield_BU());
                // _reductionYield_BU=reductionYield_BU;
                // System.out.println("reductionYield_BU "+reductionYield_BU);

                BIMAST.setFundValueAtEndReductionYield_AF();
                fundValueAtEnd_AF = Double.parseDouble(BIMAST
                        .getFundValueAtEndReductionYield_AF());
                // _fundValueAtEnd_AF=fundValueAtEnd_AF;
                // System.out.println("28. fundValueAtEnd_AF "+fundValueAtEnd_AF);

                BIMAST.setSurrenderCap_BC(annualPremium, premPayingOption);
                surrenderCap_BC = Double.parseDouble(BIMAST
                        .getSurrenderCap_BC());
                // _surrenderCap_BC=surrenderCap_BC;
                // System.out.println("51. surrenderCap_BC "+surrenderCap_BC);

                BIMAST.setSurrenderChargesReductionYield_AG(annualPremium,
                        premPayingTerm, premPayingOption);
                surrenderCharges_AG = Double.parseDouble(BIMAST
                        .getSurrenderChargesRedutionYield_AG());
                // _surrenderCharges_AG=surrenderCharges_AG;
                // System.out.println("29. surrenderCharges_AG "+surrenderCharges_AG);

                BIMAST.setServiceTaxOnSurrenderChargesReductionYield_AH(
                        prop.surrenderChargesReductionYield, serviceTax);
                serviceTaxOnSurrenderCharges_AH = Double.parseDouble(BIMAST
                        .getServiceTaxOnSurrenderChargesReductionYield_AH());
                // _serviceTaxOnSurrenderCharges_AH=serviceTaxOnSurrenderCharges_AH;
                // System.out.println("30. serviceTaxOnSurrenderCharges_AH "+serviceTaxOnSurrenderCharges_AH);

                BIMAST.setSurrenderValueReductionYield_AI();
                surrenderValue_AI = Double.parseDouble(BIMAST
                        .getSurrenderValueReductionYield_AI());
                // _surrenderValue_AI=surrenderValue_AI;
                // System.out.println("31. surrenderValue_AI "+surrenderValue_AI);

                sumOfPremium += premium_I;

                BIMAST.setDeathBenefitReductionYield_AJ(sumOfPremium,
                        prop.minGuarantee, prop.loyaltyAddition, policyTerm);
                deathBenefit_AJ = Double.parseDouble(BIMAST
                        .getDeathBenefitReductionYield_AJ());
                // _deathBenefit_AJ=deathBenefit_AJ;
                // System.out.println("32. deathBenefit_AJ "+deathBenefit_AJ);

                // System.out.println("**************** 8% *************************************");

                BIMAST.setMortalityCharges_AK(ageAtEntry, sumAssured,
                        forBIArray, policyTerm, prop.mortalityCharges,
                        fundValueAtEnd_AX, premPaymentOption);
                mortalityCharges_AK = Double.parseDouble(BIMAST
                        .getMortalityCharges_AK());
                // _mortalityCharges_AK=mortalityCharges_AK;
                // System.out.println("15. mortalityCharges_AK "+mortalityCharges_AK);

                BIMAST.setAccTPDCharge_AL(policyTerm, prop.accTPD_Charge,
                        sumAssured, prop.mortalityCharges, fundValueAtEnd_AX,
                        premPaymentOption);
                accTPDCharges_AL = Double.parseDouble(BIMAST
                        .getAccTPDCharge_AL());
                // _accTPDCharges_AL=accTPDCharges_AL;
                // System.out.println("16. accTPDCharges_AL "+accTPDCharges_AL);

                BIMAST.setTotalCharges_AM(policyTerm);
                totalCharges_AM = Double.parseDouble(BIMAST
                        .getTotalCharges_AM());
                // _totalCharges_AM=totalCharges_AM;
                // System.out.println("17. totalCharges_AM "+totalCharges_AM);

                BIMAST.setServiceTaxExclOfSTOnAllocAndSurr_AN(
                        prop.mortalityAndRiderChargesReductionYield,
                        prop.administrationChargesReductionYield,
                        serviceTax);
                serviceTaxExclOfSTOnAllocAndSurr_AN = Double.parseDouble(BIMAST
                        .getServiceTaxExclOfSTOnAllocAndSurr_AN());
                // _serviceTaxExclOfSTOnAllocAndSurr_AN=serviceTaxExclOfSTOnAllocAndSurr_AN;
                // System.out.println("18. serviceTaxExclOfSTOnAllocAndSurr_AN "+serviceTaxExclOfSTOnAllocAndSurr_AN);

                BIMAST.setAdditionToFundIfAny_AP(fundValueAtEnd_AX, policyTerm,
                        prop.int2);
                additionToFundIfAny_AP = Double.parseDouble(BIMAST
                        .getAdditionToFundIfAny_AP());
                // _additionToFundIfAny_AP=additionToFundIfAny_AP;
                // System.out.println("20. additionToFundIfAny_AP "+additionToFundIfAny_AP);

                BIMAST.setFundBeforeFMC_AQ(fundValueAtEnd_AX, policyTerm);
                fundBeforeFMC_AQ = Double.parseDouble(BIMAST
                        .getFundBeforeFMC_AQ());
                // _fundBeforeFMC_AQ=fundBeforeFMC_AQ;
                // System.out.println("21. fundBeforeFMC_AQ "+fundBeforeFMC_AQ);

                BIMAST.setFundManagementCharge_AR(policyTerm,
                        percentToBeInvested_EquityPensionFund,
                        percentToBeInvested_EquityOptPensionFund,
                        percentToBeInvested_GrowthPensionFund,
                        percentToBeInvested_BondPensionFund,
                        percentToBeInvested_MoneyMarketPesionFund, fundOption);
                fundManagementCharge_AR = Double.parseDouble(BIMAST
                        .getFundManagementCharge_AR());
                // _fundManagementCharge_AR=fundManagementCharge_AR;
                // System.out.println("22. fundManagementCharge_AR "+fundManagementCharge_AR);

                BIMAST.setGuaranteeCharge_AS(prop.guarantee_chg, policyTerm,
                        prop.guaranteeChargeReductionYield, fundOption);
                guranteeCharge_AS = Double.parseDouble(BIMAST
                        .getGuaranteeCharge_AS());
                // _guranteeCharge_AS=guranteeCharge_AS;
                // System.out.println("23. guranteeCharge_AS "+guranteeCharge_AS);

                BIMAST.setServiceTaxOnFMC_AT(
                        prop.fundManagementChargesReductionYield,
                        serviceTax, percentToBeInvested_EquityPensionFund,
                        percentToBeInvested_EquityOptPensionFund,
                        percentToBeInvested_GrowthPensionFund,
                        percentToBeInvested_BondPensionFund,
                        percentToBeInvested_MoneyMarketPesionFund, fundOption, policyTerm);
                serviceTaxOnFMC_AT = Double.parseDouble(BIMAST
                        .getServiceTaxOnFMC_AT());
                // _serviceTaxOnFMC_AT=serviceTaxOnFMC_AT;
                // System.out.println("24. _serviceTaxOnFMC_AT "+_serviceTaxOnFMC_AT);

                BIMAST.setTotalServiceTaxReductionYield_AO(prop.riderCharges,
                        serviceTax);
                totalServiceTax_AO = Double.parseDouble(BIMAST
                        .getTotalServiceTaxReductionYield_AO());
                // _totalServiceTax_AO=totalServiceTax_AO;
                // System.out.println("19. totalServiceTax_AO "+totalServiceTax_AO);

                BIMAST.setFundValueAfterFMCandBeforeGAReductionYield_AU(
                        serviceTax, policyTerm);
                fundValueAfterFMCBerforeGA_AU = Double.parseDouble(BIMAST
                        .getFundValueAfterFMCandBeforeGAReductionYield_AU());
                // _fundValueAfterFMCBerforeGA_AU=fundValueAfterFMCBerforeGA_AU;
                // System.out.println("25 fundValueAfterFMCBerforeGA_AU "+fundValueAfterFMCBerforeGA_AU);

                BIMAST.setGuaranteedAdditionReductionYield_AV(policyTerm,
                        annualPremium, prop.guaranteedAddition, premPayingOption);
                guaranteedAddition_AV = Double.parseDouble(BIMAST
                        .getGuaranteedAdditonReductionYield_AV());
                // _guaranteedAddition_AV=guaranteedAddition_AV;
                // System.out.println("26. guaranteedAddition_AV "+guaranteedAddition_AV);

                BIMAST.setLoyaltyAdditionReductionYield_AW(
                        prop.loyaltyAddition, policyTerm, prop.terminalAddition);
                loyaltiAddition_AW = Double.parseDouble(BIMAST
                        .getLoyaltyAdditionReductionYield_AW());
                // _loyaltiAddition_AW=loyaltiAddition_AW;
                // System.out.println("27. loyaltiAddition_AW "+loyaltiAddition_AW);

                BIMAST.setReductionYield_BZ(noOfYearsElapsedSinceInception,
                        fundValueAtEnd_AX);
                reductionYield_BZ = Double.parseDouble(BIMAST
                        .getReductionYield_BZ());
                // _reductionYield_BZ=reductionYield_BZ;
                // System.out.println("reductionYield_BZ "+reductionYield_BZ);

                BIMAST.setReductionYield_BV(policyTerm, fundValueAtEnd_AX);
                reductionYield_BV = Double.parseDouble(BIMAST
                        .getReductionYield_BV());
                // _reductionYield_BV=reductionYield_BV;
                // System.out.println("reductionYield_BV "+reductionYield_BV);

                BIMAST.setFundValueAtEndReductionYield_AX();
                fundValueAtEnd_AX = Double.parseDouble(BIMAST
                        .getFundValueAtEndReductionYield_AX());
                // _fundValueAtEnd_AX=fundValueAtEnd_AX;
                // System.out.println("28. fundValueAtEnd_AX "+fundValueAtEnd_AX);

                BIMAST.setSurrenderChargesReductionYield_AY(annualPremium,
                        premPayingTerm, premPayingOption);
                surrenderCharges_AY = Double.parseDouble(BIMAST
                        .getSurrenderChargesReductionYield_AY());
                // _surrenderCharges_AY=surrenderCharges_AY;
                // System.out.println("29. surrenderCharges_AY "+surrenderCharges_AY);

                BIMAST.setServiceTaxOnSurrenderChargesReductionYield_AZ(
                        prop.surrenderChargesReductionYield, serviceTax);
                serviceTaxOnSurrenderCharges_AZ = Double.parseDouble(BIMAST
                        .getServiceTaxOnSurrenderChargesReductionYield_AZ());
                // _serviceTaxOnSurrenderCharges_AZ=serviceTaxOnSurrenderCharges_AZ;
                // System.out.println("30. serviceTaxOnSurrenderCharges_AZ "+serviceTaxOnSurrenderCharges_AZ);

                BIMAST.setSurrenderValueReductionYield_BA();
                surrenderValue_BA = Double.parseDouble(BIMAST
                        .getSurrenderValueReductionYield_BA());
                // _surrenderValue_BA=surrenderValue_BA;
                // System.out.println("31. surrenderValue_BA "+surrenderValue_BA);

                BIMAST.setDeathBenefitReductionYield_BB(sumOfPremium,
                        prop.minGuarantee, prop.loyaltyAddition, policyTerm);
                deathBenefit_BB = Double.parseDouble(BIMAST
                        .getDeathBenefitReductionYield_BB());
                // _deathBenefit_BB=deathBenefit_BB;
                // System.out.println("32. deathBenefit_BB "+deathBenefit_BB);

                monthNumber++;

                List_BV.add(obj.roundUp_Level2(obj
                        .getStringWithout_E(reductionYield_BV)));
                List_BZ.add(obj.roundUp_Level2(obj
                        .getStringWithout_E(reductionYield_BZ)));
                List_BU.add(obj.roundUp_Level2(obj
                        .getStringWithout_E(reductionYield_BU)));

            }

            // System.out.println("List_BV.size "+List_BV.size());
            // System.out.println("List_BV "+List_BV);
            // System.out.println("List_BZ.size "+List_BZ.size());
            // System.out.println("List_BZ "+List_BZ);

			/*double ans = BIMAST.irr(List_BV, 0.001);
            double ans1 = BIMAST.irr(List_BZ, 0.001);
			double ans2 = BIMAST.irr(List_BU, 0.001);*/

            double ans = BIMAST.irr(List_BV, 0.01);
            double ans1 = BIMAST.irr(List_BZ, 0.01);
            double ans2 = BIMAST.irr(List_BU, 0.01);
            // System.out.println("ans_BV "+ans);
            // System.out.println("ans1_BZ "+ans1);

            BIMAST.setIRRAnnual_BV(ans);
            irrAnnual_BV = Double.parseDouble(BIMAST.getIRRAnnual_BV());
            // _irrAnnual_BV=irrAnnual_BV;
            // System.out.println("irrAnnual_BV "+irrAnnual_BV);

            BIMAST.setIRRAnnual_BZ(ans1);
            irrAnnual_BZ = Double.parseDouble(BIMAST.getIRRAnnual_BZ());
            // _irrAnnual_BZ=irrAnnual_BZ;
            // System.out.println("irrAnnual_BZ "+irrAnnual_BZ);

            BIMAST.setIRRAnnual_BU(ans2);
            irrAnnual_BU = Double.parseDouble(BIMAST.getIRRAnnual_BU());
            // _irrAnnual_BU=irrAnnual_BU;
            // System.out.println("irrAnnual_BZ "+irrAnnual_BZ);

            BIMAST.setReductionInYieldMaturityAt(prop.int2);
            reductionInYieldMaturityAt = Double.parseDouble(BIMAST
                    .getReductionInYieldMaturityAt());
            // _reductionInYieldMaturityAt=reductionInYieldMaturityAt;
            // System.out.println("reductionInYieldMaturityAt "+reductionInYieldMaturityAt);

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

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

        return new String[]{
                obj.getRoundOffLevel2(obj
                        .getStringWithout_E(reductionInYieldMaturityAt)),
                obj.getRoundOffLevel2(obj
                        .getStringWithout_E(reductionInYieldNumberOfYearsElapsedSinceInception)),
                obj.roundUp_Level2(obj.getStringWithout_E(netYield4pa)),
                obj.roundUp_Level2(obj.getStringWithout_E(netYield8pa))};

    }

    /***************************************************************** Reduction In Yield Ends here ********************************************************************/

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

    public void updatePremiumPayingTerm() {
        String error = "";
        int minPremPayingTerm = 0, maxPremPayingTerm = 0;
        if (spnrPremiumPayingOption.getSelectedItem().equals("Regular")) {
            help_premPayingTerm.setText("("
                    + spnrPolicyTerm.getSelectedItem().toString() + " to "
                    + spnrPolicyTerm.getSelectedItem().toString() + ")");
        } else if (spnrPremiumPayingOption.getSelectedItem().equals("LPPT")) {
            if (Integer.parseInt(spnrPolicyTerm.getSelectedItem().toString()) == 10) {
                help_premPayingTerm.setText("(5 Or 8)");

            } else if (Integer.parseInt(spnrPolicyTerm.getSelectedItem()
                    .toString()) > 10) {
                help_premPayingTerm.setText("(5,8,10 Or 15)");
            }
        }

    }
}
