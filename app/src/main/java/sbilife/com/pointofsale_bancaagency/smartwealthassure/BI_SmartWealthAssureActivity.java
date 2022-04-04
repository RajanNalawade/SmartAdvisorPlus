package sbilife.com.pointofsale_bancaagency.smartwealthassure;

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
import sbilife.com.pointofsale_bancaagency.common.ProductInfo;
import sbilife.com.pointofsale_bancaagency.common.SimpleCrypto;
import sbilife.com.pointofsale_bancaagency.common.StorageUtils;
import sbilife.com.pointofsale_bancaagency.common.Success;
import sbilife.com.pointofsale_bancaagency.needanalysis.NeedAnalysisActivity;
import sbilife.com.pointofsale_bancaagency.new_bussiness.ProductBIBean;
import sbilife.com.pointofsale_bancaagency.utility.GridHeight;

public class BI_SmartWealthAssureActivity extends AppCompatActivity implements
        OnEditorActionListener {

    // To Store User Info and sync info
    private NeedAnalysisBIService NABIObj;
    private NA_CBI_bean na_cbi_bean;
    private File needAnalysispath, newFile;
    private String agentcode, agentMobile, agentEmail, userType;
    private int needAnalysis_flag = 0;
    private String na_input = null;
    private String na_output = null;
    private DatabaseHelper dbHelper;

    private CheckBox cb_staffdisc, cb_bi_smart_wealth_assure_JKResident,
            cb_bi_smart_wealth_assure_adb_rider;

    private EditText edt_bi_smart_wealth_assure_life_assured_first_name,
            edt_bi_smart_wealth_assure_life_assured_middle_name,
            edt_bi_smart_wealth_assure_life_assured_last_name,
            edt_bi_smart_wealth_assure_life_assured_age,
            edt_smart_wealth_assure_contact_no,
            edt_smart_wealth_assure_Email_id,
            edt_smart_wealth_assure_ConfirmEmail_id,
            edt_bi_smart_wealth_assure_sum_assured_amount,
            edt_smart_wealth_assure_samf,
            edt_smart_wealth_assure_noOfYearsElapsedSinceInception,
            edt_smart_wealth_assure_percent_EquityFund,
            percent_BalancedFund,
            percent_MoneyMktFund, percent_BondFund,
            percent_BondOptimiserFund, percent_CorporateBondFund, percent_PureFund,

    edt_smart_wealth_assure_percent_BondFund,
            edt_bi_smart_wealth_assure_adb_rider_sum_assured,
            edt_bi_smart_wealth_assure_proposer_last_name,
            edt_bi_smart_wealth_assure_proposer_middle_name,
            edt_bi_smart_wealth_assure_proposer_first_name;

    private Spinner spnr_bi_smart_wealth_assure_life_assured_title,
            spnr_bi_smart_wealth_assure_selGender,
    //            spinnerProposerGender,
    spnr_bi_smart_wealth_assure_policyterm,
            spnr_bi_smart_wealth_assure_permium_payingterm,
            spnr_bi_smart_wealth_assure_premium_frequency,
            spnr_bi_smart_wealth_assure_adb_rider_term,
            spnr_bi_smart_wealth_assure_proposer_title;

    private TextView adb_txt1, adb_txt2, help_premAmt, help_SAMF, help_adb_term,
            help_adbsa, help_noOfYearsElapsedSinceInception, help_policyTerm, propAge;

    private Button btn_bi_smart_wealth_assure_life_assured_date,
            btn_bi_smart_wealth_assure_proposer_date;
    private Button btnBack, btnSubmit;
    private TableRow tr_bi_smart_wealth_assure_adb_rider,
            tr_bi_smart_wealth_assure_adb_rider_checkbox;
    private LinearLayout linearlayoutProposerDetails;
//    private TableRow tr_smart_wealth_assure_proposer_detail1,
//            tr_smart_wealth_assure_proposer_detail2;

    private String perInvMoneyMarketFund = "";
    private String perInvTop300Fund = "";

    private String perInvBondOptimiserFund = "";
    private String perInvCorporateBondFund = "";
    private String perInvPureFund = "", perInvBalancedFund = "";

    private String QuatationNumber = "", ProposerAge = "";
    private String planName = "";
    private String str_kerla_discount = "No";
    private AlertDialog.Builder showAlert;
    private String premiumAmount;
    // newDBHelper db;
    private ParseXML prsObj;

    private String emailId = "", mobileNo = "", ConfirmEmailId = "",
            ProposerEmailId = "";
    private Dialog d;
    private SmartWealthAssureBean smartWealthAssureBean;
    private CommonForAllProd commonForAllProd, obj;
    private SmartWealthAssureProperties prop;
    private DecimalFormat currencyFormat;

    private Bitmap photoBitmap;
    private final int DATE_DIALOG_ID = 1;
    private int DIALOG_ID;
    private int mYear, mMonth, mDay;

    private Button btn_MarketingOfficalDate, btn_PolicyholderDate;
    private ImageButton Ibtn_signatureofMarketing, Ibtn_signatureofPolicyHolders;
    private String output = "";
    private String input = "";
    private String lifeAssured_Title = "";
    private String lifeAssured_First_Name = "";
    private String lifeAssured_Middle_Name = "";
    private String lifeAssured_Last_Name = "";
    private String name_of_life_assured = "";
    private String lifeAssured_date_of_birth = "";
    private String name_of_proposer = "";
    private String name_of_person = "";
    private String place2 = "";
    private String date1 = "";
    private String date2 = "";
    private String agent_sign = "";
    private String proposer_sign = "";
    private String proposer_date_of_birth = "";
    private String proposer_Title = "";
    private String proposer_First_Name = "";
    private String proposer_Middle_Name = "";
    private String proposer_Last_Name = "";
    private String proposer_Is_Same_As_Life_Assured = "Y";
    private String effectivePremium = "0";
    private String premPayingMode = "";
    private String policyTerm = "";
    private String latestImage = "";
    private String maturityAge = "";
    private String netYield4pa = "";
    private String netYield8pa = "";
    private String annPrem = "";
    private String sumAssured = "";
    private String policy_Frequency = "";
    private String perInvEquityFund = "";
    private String perInvBondFund = "";
    private String noOfYrElapsed = "";
    private String redInYieldNoYr = "";
    private String redInYieldMat = "";
    private String adb_rider_status = "";
    private String sa_adb = "";
    private String age_entry = "";
    private String gender = "";
    //    private String proposerGender  = "";
    private String sum_assured = "";

    private StringBuilder bussIll = null, retVal, inputVal;
    private boolean validationFla1 = false;
    private final int SIGNATURE_ACTIVITY = 1;
    private List<M_BI_SmartWealthAssureAdapterCommon> list_data;
    private List<M_BI_SmartWealthAssureAdapterCommon2> list_data1;
    private List<M_BI_SmartWealthAssureAdapterCommon2> list_data2;
    private File mypath;

    private String product_Code, product_UIN, product_cateogory, product_type;

    private String bankUserType = "", mode = "";

    private Context context;
    private String Check = "";
    private CommonMethods commonMethods;
    private StorageUtils mStorageUtils;

    private LinearLayout linearlayoutThirdpartySignature, linearlayoutAppointeeSignature;
    private ImageButton Ibtn_signatureofThirdParty, Ibtn_signatureofAppointee,
            Ibtn_signatureofLifeAssured, imageButtonSmartWealthAssureProposerPhotograph;
    private String thirdPartySign = "", appointeeSign = "",
            proposerAsLifeAssuredSign = "", Company_policy_surrender_dec = "";

    private CheckBox cb_kerladisc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.bi_smart_wealth_assuremain);
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

        percent_MoneyMktFund = (EditText) findViewById(R.id.moneyMktFund);
        percent_BondFund = (EditText) findViewById(R.id.bondFund);
        percent_BondOptimiserFund = (EditText) findViewById(R.id.bondOptimiserFund);
        percent_CorporateBondFund = (EditText) findViewById(R.id.corporateBondFund);
        percent_PureFund = (EditText) findViewById(R.id.pureFund);
        percent_BalancedFund = (EditText) findViewById(R.id.balancedFund);

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
                    planName = "Smart Wealth Assure";
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
        UI_Declaration();

        prsObj = new ParseXML();
        setSpinner_Value();
        // setBIInputGui();

        edt_bi_smart_wealth_assure_life_assured_first_name
                .setOnEditorActionListener(this);
        edt_bi_smart_wealth_assure_life_assured_middle_name
                .setOnEditorActionListener(this);
        edt_bi_smart_wealth_assure_life_assured_last_name
                .setOnEditorActionListener(this);
        edt_bi_smart_wealth_assure_proposer_first_name
                .setOnEditorActionListener(this);
        edt_bi_smart_wealth_assure_proposer_middle_name
                .setOnEditorActionListener(this);
        edt_bi_smart_wealth_assure_proposer_last_name
                .setOnEditorActionListener(this);
        edt_bi_smart_wealth_assure_sum_assured_amount
                .setOnEditorActionListener(this);
        edt_smart_wealth_assure_contact_no.setOnEditorActionListener(this);
        edt_smart_wealth_assure_Email_id.setOnEditorActionListener(this);
        edt_smart_wealth_assure_ConfirmEmail_id.setOnEditorActionListener(this);
        edt_smart_wealth_assure_samf.setOnEditorActionListener(this);
        edt_smart_wealth_assure_noOfYearsElapsedSinceInception
                .setOnEditorActionListener(this);
        edt_smart_wealth_assure_percent_EquityFund
                .setOnEditorActionListener(this);
        edt_smart_wealth_assure_percent_BondFund
                .setOnEditorActionListener(this);
        percent_BalancedFund.setOnEditorActionListener(this);
        percent_BondFund.setOnEditorActionListener(this);
        percent_MoneyMktFund.setOnEditorActionListener(this);
        percent_BondOptimiserFund.setOnEditorActionListener(this);
        percent_CorporateBondFund.setOnEditorActionListener(this);
        percent_PureFund.setOnEditorActionListener(this);
        edt_bi_smart_wealth_assure_adb_rider_sum_assured
                .setOnEditorActionListener(this);

        setFocusable(spnr_bi_smart_wealth_assure_life_assured_title);
        spnr_bi_smart_wealth_assure_life_assured_title.requestFocus();

        validationOfMoile_EmailId();
        setSpinnerAndOtherListner();
        showAlert = new AlertDialog.Builder(this);
        smartWealthAssureBean = new SmartWealthAssureBean();
        obj = new CommonForAllProd();
        commonForAllProd = new CommonForAllProd();
        prop = new SmartWealthAssureProperties();
        list_data = new ArrayList<M_BI_SmartWealthAssureAdapterCommon>();
        list_data1 = new ArrayList<M_BI_SmartWealthAssureAdapterCommon2>();
        list_data2 = new ArrayList<M_BI_SmartWealthAssureAdapterCommon2>();
        currencyFormat = new DecimalFormat("##,##,##,###");

        TableRow tr_staff_disc = findViewById(R.id.tr_smart_wealth_assure_staffdisc);

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

    private void UI_Declaration() {

        cb_staffdisc = findViewById(R.id.cb_staffdisc);
        cb_bi_smart_wealth_assure_JKResident = findViewById(R.id.cb_jk);

        edt_bi_smart_wealth_assure_life_assured_first_name = findViewById(R.id.edt_bi_smart_wealth_assure_life_assured_first_name);
        edt_bi_smart_wealth_assure_life_assured_middle_name = findViewById(R.id.edt_bi_smart_wealth_assure_life_assured_middle_name);
        edt_bi_smart_wealth_assure_life_assured_last_name = findViewById(R.id.edt_bi_smart_wealth_assure_life_assured_last_name);
        edt_bi_smart_wealth_assure_life_assured_age = findViewById(R.id.edt_bi_smart_wealth_assure_life_assured_age);
        edt_smart_wealth_assure_contact_no = findViewById(R.id.edt_smart_wealth_assure_contact_no);
        edt_smart_wealth_assure_Email_id = findViewById(R.id.edt_smart_wealth_assure_Email_id);
        edt_smart_wealth_assure_ConfirmEmail_id = findViewById(R.id.edt_smart_wealth_assure_ConfirmEmail_id);
        edt_bi_smart_wealth_assure_sum_assured_amount = findViewById(R.id.edt_bi_smart_wealth_assure_sum_assured_amount);

        spnr_bi_smart_wealth_assure_life_assured_title = findViewById(R.id.spnr_bi_smart_wealth_assure_life_assured_title);
        spnr_bi_smart_wealth_assure_selGender = findViewById(R.id.spnr_bi_smart_wealth_assure_selGender);
//        spinnerProposerGender = findViewById(R.id.spinnerProposerGender);
//        spnr_bi_smart_wealth_assure_selGender.setClickable(false);
//        spnr_bi_smart_wealth_assure_selGender.setEnabled(false);

        spnr_bi_smart_wealth_assure_policyterm = findViewById(R.id.spnr_bi_smart_wealth_assure_policyterm);
        spnr_bi_smart_wealth_assure_premium_frequency = findViewById(R.id.spnr_bi_smart_wealth_assure_premium_frequency);

        edt_smart_wealth_assure_samf = findViewById(R.id.samf);
        edt_smart_wealth_assure_noOfYearsElapsedSinceInception = findViewById(R.id.years_elapsed_since_inception);
        edt_smart_wealth_assure_percent_EquityFund = findViewById(R.id.equityFund);
        edt_smart_wealth_assure_percent_BondFund = findViewById(R.id.bondFund);

        percent_BalancedFund = (EditText) findViewById(R.id.balancedFund);
        edt_smart_wealth_assure_percent_BondFund = (EditText) findViewById(R.id.bondFund);


        // Adb
        cb_bi_smart_wealth_assure_adb_rider = findViewById(R.id.cb_bi_smart_wealth_assure_adb_rider);
        edt_bi_smart_wealth_assure_adb_rider_sum_assured = findViewById(R.id.edt_bi_smart_wealth_assure_adb_rider_sum_assured);
        spnr_bi_smart_wealth_assure_adb_rider_term = findViewById(R.id.spnr_bi_smart_wealth_assure_adb_rider_term);

        help_SAMF = findViewById(R.id.help_samf);
        help_premAmt = findViewById(R.id.help_premAmt);
        help_noOfYearsElapsedSinceInception = findViewById(R.id.help_years_elapsed_since_inception);
        propAge = findViewById(R.id.proposerAge);
        help_adbsa = findViewById(R.id.help_adbsa);

        btn_bi_smart_wealth_assure_life_assured_date = findViewById(R.id.btn_bi_smart_wealth_assure_life_assured_date);
        btnBack = findViewById(R.id.btn_bi_smart_wealth_assure_btnback);
        btnSubmit = findViewById(R.id.btn_bi_smart_wealth_assure_btnSubmit);
        tr_bi_smart_wealth_assure_adb_rider = findViewById(R.id.tr_bi_smart_wealth_assure_adb_rider);
        tr_bi_smart_wealth_assure_adb_rider_checkbox = findViewById(R.id.tr_bi_smart_wealth_assure_adb_rider_checkbox);

        // Proposer details

        edt_bi_smart_wealth_assure_proposer_last_name = findViewById(R.id.edt_bi_smart_wealth_assure_proposer_last_name);
        edt_bi_smart_wealth_assure_proposer_middle_name = findViewById(R.id.edt_bi_smart_wealth_assure_proposer_middle_name);
        edt_bi_smart_wealth_assure_proposer_first_name = findViewById(R.id.edt_bi_smart_wealth_assure_proposer_first_name);
        spnr_bi_smart_wealth_assure_proposer_title = findViewById(R.id.spnr_bi_smart_wealth_assure_proposer_title);
//        tr_smart_wealth_assure_proposer_detail1 = findViewById(R.id.tr_smart_wealth_assure_proposer_detail1);
//        tr_smart_wealth_assure_proposer_detail2 = findViewById(R.id.tr_smart_wealth_assure_proposer_detail2);
        linearlayoutProposerDetails = findViewById(R.id.linearlayoutProposerDetails);
        btn_bi_smart_wealth_assure_proposer_date = findViewById(R.id.btn_bi_smart_wealth_assure_proposer_date);

        LinearLayout ll_bi_smart_wealth_assure_main = findViewById(R.id.ll_bi_smart_wealth_assure_main);
    }

    private void setSpinner_Value() {

        // Gender
        String[] genderList = {"Male", "Female", "Third Gender"};
        ArrayAdapter<String> genderAdapter = new ArrayAdapter<>(
                getApplicationContext(), R.layout.spinner_item, genderList);
        genderAdapter.setDropDownViewResource(R.layout.spinner_item1);
        spnr_bi_smart_wealth_assure_selGender.setAdapter(genderAdapter);
        genderAdapter.notifyDataSetChanged();
//        spinnerProposerGender.setAdapter(genderAdapter);
//        genderAdapter.notifyDataSetChanged();

        commonMethods.fillSpinnerValue(context, spnr_bi_smart_wealth_assure_life_assured_title,
                Arrays.asList(getResources().getStringArray(R.array.arr_select_title)));
        commonMethods.fillSpinnerValue(context, spnr_bi_smart_wealth_assure_proposer_title,
                Arrays.asList(getResources().getStringArray(R.array.arr_select_title)));

        // Policy Term
        String[] policyTermList = new String[21];
        for (int i = 10; i <= 30; i++) {
            policyTermList[i - 10] = i + "";
        }
        ArrayAdapter<String> policyTermAdapter = new ArrayAdapter<>(
                getApplicationContext(), R.layout.spinner_item, policyTermList);
        policyTermAdapter.setDropDownViewResource(R.layout.spinner_item1);
        spnr_bi_smart_wealth_assure_policyterm.setAdapter(policyTermAdapter);
        policyTermAdapter.notifyDataSetChanged();

        // Premium Frequency
        String[] premFreqList = {"Single"};
        ArrayAdapter<String> premFreqAdapter = new ArrayAdapter<>(
                getApplicationContext(), R.layout.spinner_item, premFreqList);
        premFreqAdapter.setDropDownViewResource(R.layout.spinner_item1);
        spnr_bi_smart_wealth_assure_premium_frequency
                .setAdapter(premFreqAdapter);
        premFreqAdapter.notifyDataSetChanged();
        spnr_bi_smart_wealth_assure_premium_frequency.setEnabled(false);

        // adb Term
        String[] adbTermList = new String[26];
        for (int i = 5; i <= 30; i++) {
            adbTermList[i - 5] = i + "";
        }
        ArrayAdapter<String> adbTermAdapter = new ArrayAdapter<>(
                getApplicationContext(), R.layout.spinner_item, adbTermList);
        adbTermAdapter.setDropDownViewResource(R.layout.spinner_item1);
        spnr_bi_smart_wealth_assure_adb_rider_term.setAdapter(adbTermAdapter);
        adbTermAdapter.notifyDataSetChanged();
        spnr_bi_smart_wealth_assure_adb_rider_term.setEnabled(false);

    }

    private void setSpinnerAndOtherListner() {

        cb_kerladisc.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cb_kerladisc.isChecked()) {
                    str_kerla_discount = "Yes";
                    clearFocusable(cb_kerladisc);
                    setFocusable(spnr_bi_smart_wealth_assure_life_assured_title);
                    spnr_bi_smart_wealth_assure_life_assured_title.requestFocus();
                } else {
                    str_kerla_discount = "No";
                    clearFocusable(cb_kerladisc);
                    setFocusable(spnr_bi_smart_wealth_assure_life_assured_title);
                    spnr_bi_smart_wealth_assure_life_assured_title.requestFocus();
                }
            }
        });
        // premium amount
        edt_bi_smart_wealth_assure_sum_assured_amount
                .setOnFocusChangeListener(new OnFocusChangeListener() {

                    public void onFocusChange(View arg0, boolean arg1) {
                        // TODO Auto-generated method stub

                        if (!(edt_bi_smart_wealth_assure_sum_assured_amount
                                .getText().toString().equals(""))) {
                            effectivePremium = getEffectivePremium();
                            updateADBsumAssuredLabel();
                            updateSAMFlabel();

                        }
                    }
                });

        edt_bi_smart_wealth_assure_life_assured_age
                .setOnFocusChangeListener(new OnFocusChangeListener() {

                    public void onFocusChange(View arg0, boolean arg1) {
                        // TODO Auto-generated method stub

                        if (!(edt_bi_smart_wealth_assure_life_assured_age
                                .getText().toString().equals(""))) {

                            updateSAMFlabel();
                            valADBRider();
                            if (valPolicyTerm()) {
                                int adbTerm = Math.min(
                                        Integer.parseInt(spnr_bi_smart_wealth_assure_policyterm
                                                .getSelectedItem().toString()),
                                        (70 - Integer
                                                .parseInt(edt_bi_smart_wealth_assure_life_assured_age
                                                        .getText().toString())));
                                spnr_bi_smart_wealth_assure_adb_rider_term
                                        .setSelection(
                                                getIndex(
                                                        spnr_bi_smart_wealth_assure_adb_rider_term,
                                                        String.valueOf(adbTerm)),
                                                false);

                            }

                        }

                    }
                });

        cb_staffdisc.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (cb_staffdisc.isChecked()) {
                    cb_staffdisc.setChecked(true);
                }
                clearFocusable(cb_staffdisc);
                clearFocusable(spnr_bi_smart_wealth_assure_life_assured_title);
                setFocusable(spnr_bi_smart_wealth_assure_life_assured_title);
                spnr_bi_smart_wealth_assure_life_assured_title.requestFocus();
            }
        });

        cb_bi_smart_wealth_assure_JKResident
                .setOnClickListener(new OnClickListener() {

                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        if (cb_bi_smart_wealth_assure_JKResident.isChecked()) {
                            cb_bi_smart_wealth_assure_JKResident
                                    .setChecked(true);
                        } else {
                            cb_bi_smart_wealth_assure_JKResident
                                    .setChecked(false);
                        }
                    }
                });

        // Spinner
        spnr_bi_smart_wealth_assure_proposer_title
                .setOnItemSelectedListener(new OnItemSelectedListener() {

                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int position, long id) {
                        // TODO Auto-generated method stub
                        if (position == 0) {
                            proposer_Title = "";
                        } else {
                            proposer_Title = spnr_bi_smart_wealth_assure_proposer_title
                                    .getSelectedItem().toString();

                            if (proposer_Title.equalsIgnoreCase("Mr.")) {
                                // selGender.setSelection(getIndex(selGender,
                                // "Male"));
                            } else if (proposer_Title.equalsIgnoreCase("Ms.")
                                    || proposer_Title.equalsIgnoreCase("Mrs.")) {
                                // selGender.setSelection(getIndex(selGender,
                                // "Female"));
                            }
                            setFocusable(edt_bi_smart_wealth_assure_proposer_first_name);

                            edt_bi_smart_wealth_assure_proposer_first_name
                                    .requestFocus();

                        }

                    }

                    public void onNothingSelected(AdapterView<?> arg0) {
                        // TODO Auto-generated method stub

                    }
                });

        spnr_bi_smart_wealth_assure_life_assured_title
                .setOnItemSelectedListener(new OnItemSelectedListener() {

                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int position, long id) {
                        // TODO Auto-generated method stub
                        if (position == 0) {
                            lifeAssured_Title = "";
                        } else {
                            lifeAssured_Title = spnr_bi_smart_wealth_assure_life_assured_title
                                    .getSelectedItem().toString();
                           /* if (lifeAssured_Title.equalsIgnoreCase("Mr.")) {
                                spnr_bi_smart_wealth_assure_selGender
                                        .setSelection(
                                                getIndex(
                                                        spnr_bi_smart_wealth_assure_selGender,
                                                        "Male"), false);

                                iv_men.setImageDrawable(getResources().getDrawable(
                                        R.drawable.male_selected));
                                iv_women.setImageDrawable(getResources().getDrawable(
                                        R.drawable.female_nonselected));
                                gender = "Male";


                            } else if (lifeAssured_Title
                                    .equalsIgnoreCase("Ms.")) {
                                spnr_bi_smart_wealth_assure_selGender
                                        .setSelection(
                                                getIndex(
                                                        spnr_bi_smart_wealth_assure_selGender,
                                                        "Female"), false);
                                gender = "Female";
                                iv_women.setImageDrawable(getResources().getDrawable(
                                        R.drawable.female_selected));
                                iv_men.setImageDrawable(getResources().getDrawable(
                                        R.drawable.male_nonselected));
                            } else if (lifeAssured_Title
                                    .equalsIgnoreCase("Mrs.")) {
                                spnr_bi_smart_wealth_assure_selGender
                                        .setSelection(
                                                getIndex(
                                                        spnr_bi_smart_wealth_assure_selGender,
                                                        "Female"), false);
                                gender = "Female";
                                iv_women.setImageDrawable(getResources().getDrawable(
                                        R.drawable.female_selected));
                                iv_men.setImageDrawable(getResources().getDrawable(
                                        R.drawable.male_nonselected));
                            }*/
                            clearFocusable(spnr_bi_smart_wealth_assure_life_assured_title);
                            setFocusable(edt_bi_smart_wealth_assure_life_assured_first_name);
                            edt_bi_smart_wealth_assure_life_assured_first_name
                                    .requestFocus();
                        }
                    }

                    public void onNothingSelected(AdapterView<?> arg0) {
                        // TODO Auto-generated method stub

                    }
                });

        spnr_bi_smart_wealth_assure_policyterm
                .setOnItemSelectedListener(new OnItemSelectedListener() {

                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int position, long id) {

                        spnr_bi_smart_wealth_assure_premium_frequency
                                .setSelection(0, false);
                        if (!(edt_bi_smart_wealth_assure_life_assured_age
                                .getText().toString().equals(""))) {
                            if (valPolicyTerm()) {

                                int adbTerm = Math.min(
                                        Integer.parseInt(spnr_bi_smart_wealth_assure_policyterm
                                                .getSelectedItem().toString()),
                                        (70 - Integer
                                                .parseInt(edt_bi_smart_wealth_assure_life_assured_age
                                                        .getText().toString())));

                                String adbTerm1 = String.valueOf(adbTerm);
                                spnr_bi_smart_wealth_assure_adb_rider_term
                                        .setSelection(
                                                getIndex(
                                                        spnr_bi_smart_wealth_assure_adb_rider_term,
                                                        adbTerm1), false);
                                // int a = 1;
                                //
                                //
                                //
                                // spnr_bi_smart_wealth_assure_adb_rider_term
                                // .setSelection(
                                // getIndex(
                                // spnr_bi_smart_wealth_assure_adb_rider_term,
                                // spnr_bi_smart_wealth_assure_policyterm
                                // .getSelectedItem()
                                // .toString()),
                                // false);
                                spnr_bi_smart_wealth_assure_adb_rider_term
                                        .setEnabled(false);
                            }
                        }
                        updatePremiumAmtLabel();
                        updateSAMFlabel();
                        updatenoOfYearsElapsedSinceInception();

                        if (edt_bi_smart_wealth_assure_life_assured_first_name
                                .getText().toString().equals("")) {
                            clearFocusable(spnr_bi_smart_wealth_assure_policyterm);
                            setFocusable(spnr_bi_smart_wealth_assure_life_assured_title);
                            spnr_bi_smart_wealth_assure_life_assured_title
                                    .requestFocus();
                        } else {
                            clearFocusable(spnr_bi_smart_wealth_assure_policyterm);
                            setFocusable(edt_bi_smart_wealth_assure_sum_assured_amount);
                            edt_bi_smart_wealth_assure_sum_assured_amount
                                    .requestFocus();
                        }
                    }

                    public void onNothingSelected(AdapterView<?> arg0) {
                        // TODO Auto-generated method stub

                    }
                });

        // ADB Rider
        // cb_bi_smart_wealth_assure_adb_rider
        // .setOnClickListener(new OnClickListener() {
        //
        //
        // public void onClick(View v) {
        // // TODO Auto-generated method stub
        //
        // if (cb_bi_smart_wealth_assure_adb_rider.isChecked()) {
        // if (valADBRider()) {
        // cb_bi_smart_wealth_assure_adb_rider
        // .setChecked(true);
        // tr_bi_smart_wealth_assure_adb_rider
        // .setVisibility(View.VISIBLE);
        // help_adbsa.setVisibility(View.VISIBLE);
        // }
        // }else {
        // tr_bi_smart_wealth_assure_adb_rider
        // .setVisibility(View.GONE);
        // help_adbsa.setVisibility(View.GONE);
        // }
        //
        //
        // }
        // });

        cb_bi_smart_wealth_assure_adb_rider
                .setOnCheckedChangeListener(new OnCheckedChangeListener() {
                    public void onCheckedChanged(CompoundButton buttonView,
                                                 boolean isChecked) {
                        if (isChecked) {
                            if (valADBRider()) {
                                cb_bi_smart_wealth_assure_adb_rider
                                        .setChecked(true);
                                tr_bi_smart_wealth_assure_adb_rider
                                        .setVisibility(View.VISIBLE);
                                help_adbsa.setVisibility(View.VISIBLE);
                                clearFocusable(cb_bi_smart_wealth_assure_adb_rider);
                                clearFocusable(edt_bi_smart_wealth_assure_adb_rider_sum_assured);
                                setFocusable(edt_bi_smart_wealth_assure_adb_rider_sum_assured);
                                edt_bi_smart_wealth_assure_adb_rider_sum_assured
                                        .requestFocus();
                            }
                        } else {
                            tr_bi_smart_wealth_assure_adb_rider
                                    .setVisibility(View.GONE);
                            help_adbsa.setVisibility(View.GONE);
                        }
                    }
                });

        spnr_bi_smart_wealth_assure_adb_rider_term
                .setOnItemSelectedListener(new OnItemSelectedListener() {

                    public void onItemSelected(AdapterView<?> adapter,
                                               View arg1, int position, long id) {
                        // TODO Auto-generated method stub

                        clearFocusable(spnr_bi_smart_wealth_assure_adb_rider_term);
                        setFocusable(edt_bi_smart_wealth_assure_adb_rider_sum_assured);
                        edt_bi_smart_wealth_assure_adb_rider_sum_assured
                                .requestFocus();

                    }

                    public void onNothingSelected(AdapterView<?> arg0) {
                        // TODO Auto-generated method stub

                    }
                });

        // //SAMF
        edt_smart_wealth_assure_samf
                .setOnFocusChangeListener(new OnFocusChangeListener() {

                    public void onFocusChange(View v, boolean hasFocus) {
                        // TODO Auto-generated method stub

                        if (!(edt_smart_wealth_assure_samf.getText().toString()
                                .equals(""))) {
                            updateADBsumAssuredLabel();
                        }
                    }
                });

        btnBack.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                setResult(RESULT_OK);
                finish();
            }
        });

        btnSubmit.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {

                gender = spnr_bi_smart_wealth_assure_selGender.getSelectedItem().toString();
//                proposerGender = spinnerProposerGender.getSelectedItem().toString();

                inputVal = new StringBuilder();
                retVal = new StringBuilder();

                proposer_First_Name = edt_bi_smart_wealth_assure_proposer_first_name
                        .getText().toString();
                proposer_Middle_Name = edt_bi_smart_wealth_assure_proposer_middle_name
                        .getText().toString();
                proposer_Last_Name = edt_bi_smart_wealth_assure_proposer_last_name
                        .getText().toString();

                name_of_proposer = proposer_Title + " " + proposer_First_Name
                        + " " + proposer_Middle_Name + " " + proposer_Last_Name;

                lifeAssured_First_Name = edt_bi_smart_wealth_assure_life_assured_first_name
                        .getText().toString();
                lifeAssured_Middle_Name = edt_bi_smart_wealth_assure_life_assured_middle_name
                        .getText().toString();
                lifeAssured_Last_Name = edt_bi_smart_wealth_assure_life_assured_last_name
                        .getText().toString();

                name_of_life_assured = lifeAssured_Title + " "
                        + lifeAssured_First_Name + " "
                        + lifeAssured_Middle_Name + " " + lifeAssured_Last_Name;

                mobileNo = edt_smart_wealth_assure_contact_no.getText()
                        .toString();
                emailId = edt_smart_wealth_assure_Email_id.getText().toString();
                ConfirmEmailId = edt_smart_wealth_assure_ConfirmEmail_id
                        .getText().toString();

                if (valLifeAssuredProposerDetail() && valDob() && valProposerDob()
                        && valBasicDetail() && valPremiumAmt() && valSAMF()
                        && valADBsumAssured() && valTotalAllocation()
                        && valYearsElapsedSinceInception()) {

                    if (proposer_Is_Same_As_Life_Assured.equalsIgnoreCase("Y")) {
                        proposer_Title = "";
                        proposer_First_Name = "";
                        proposer_Middle_Name = "";
                        proposer_Last_Name = "";
                        name_of_proposer = "";
                        proposer_date_of_birth = "";
                    }

                    addListenerOnSubmit();
                    getInput(smartWealthAssureBean);
                    // insertDataIntoDatabase();

                    if (needAnalysis_flag == 0) {
                        Intent i = new Intent(getApplicationContext(),
                                Success.class);

                        i.putExtra("ProductName",
                                "Product : SBI Life - Smart Wealth Assure (UIN:111L077V03)");

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
                        i.putExtra(
                                "op3",
                                "Single Premium (SP) is Rs. "
                                        + currencyFormat.format(Double
                                        .parseDouble(prsObj
                                                .parseXmlTag(retVal
                                                                .toString(),
                                                        "annPrem"))));

                        i.putExtra("header", "SBI Life - Smart Wealth Assure");
                        i.putExtra("header1", "(UIN:111L077V03)");
                        startActivity(i);
                    } else
                        Dialog();

                }

            }
        });

    }


    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        // TODO Auto-generated method stub
        if (v.getId() == edt_bi_smart_wealth_assure_life_assured_first_name
                .getId()) {
            setFocusable(edt_bi_smart_wealth_assure_life_assured_middle_name);
            edt_bi_smart_wealth_assure_life_assured_middle_name.requestFocus();
        } else if (v.getId() == edt_bi_smart_wealth_assure_life_assured_middle_name
                .getId()) {
            setFocusable(edt_bi_smart_wealth_assure_life_assured_last_name);
            edt_bi_smart_wealth_assure_life_assured_last_name.requestFocus();
        } else if (v.getId() == edt_bi_smart_wealth_assure_life_assured_last_name
                .getId()) {

            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(
                    edt_bi_smart_wealth_assure_life_assured_last_name
                            .getWindowToken(), 0);
            // clearFocusable(edt_bi_smart_wealth_assure_life_assured_last_name);
            setFocusable(btn_bi_smart_wealth_assure_life_assured_date);
            btn_bi_smart_wealth_assure_life_assured_date.requestFocus();
        } else if (v.getId() == edt_bi_smart_wealth_assure_proposer_first_name
                .getId()) {
            setFocusable(edt_bi_smart_wealth_assure_proposer_middle_name);
            edt_bi_smart_wealth_assure_proposer_middle_name.requestFocus();
        } else if (v.getId() == edt_bi_smart_wealth_assure_proposer_middle_name
                .getId()) {
            setFocusable(edt_bi_smart_wealth_assure_proposer_last_name);
            edt_bi_smart_wealth_assure_proposer_last_name.requestFocus();
        } else if (v.getId() == edt_bi_smart_wealth_assure_proposer_last_name
                .getId()) {

            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(
                    edt_bi_smart_wealth_assure_proposer_last_name
                            .getWindowToken(), 0);
            // clearFocusable(edt_bi_smart_wealth_assure_proposer_last_name);
            setFocusable(btn_bi_smart_wealth_assure_proposer_date);
            btn_bi_smart_wealth_assure_proposer_date.requestFocus();
        } else if (v.getId() == edt_smart_wealth_assure_contact_no.getId()) {
            // clearFocusable(premiumAmt);
            setFocusable(edt_smart_wealth_assure_Email_id);
            edt_smart_wealth_assure_Email_id.requestFocus();
        } else if (v.getId() == edt_smart_wealth_assure_Email_id.getId()) {
            // clearFocusable(premiumAmt);
            setFocusable(edt_smart_wealth_assure_ConfirmEmail_id);
            edt_smart_wealth_assure_ConfirmEmail_id.requestFocus();
        } else if (v.getId() == edt_smart_wealth_assure_ConfirmEmail_id.getId()) {
            // clearFocusable(premiumAmt);
            setFocusable(spnr_bi_smart_wealth_assure_policyterm);
            spnr_bi_smart_wealth_assure_policyterm.requestFocus();
        } else if (v.getId() == edt_bi_smart_wealth_assure_sum_assured_amount
                .getId()) {
            setFocusable(edt_smart_wealth_assure_samf);
            edt_smart_wealth_assure_samf.requestFocus();

        } else if (v.getId() == edt_smart_wealth_assure_samf.getId()) {
            setFocusable(edt_smart_wealth_assure_noOfYearsElapsedSinceInception);
            edt_smart_wealth_assure_noOfYearsElapsedSinceInception
                    .requestFocus();

        } else if (v.getId() == edt_smart_wealth_assure_noOfYearsElapsedSinceInception
                .getId()) {
            setFocusable(edt_smart_wealth_assure_percent_EquityFund);
            edt_smart_wealth_assure_percent_EquityFund.requestFocus();

        } else if (v.getId() == edt_smart_wealth_assure_percent_EquityFund
                .getId()) {
            setFocusable(edt_smart_wealth_assure_percent_BondFund);
            edt_smart_wealth_assure_percent_BondFund.requestFocus();

        } else if (v.getId() == edt_smart_wealth_assure_percent_BondFund
                .getId()) {
            commonMethods.hideKeyboard(edt_smart_wealth_assure_percent_BondFund, context);
            clearFocusable(cb_bi_smart_wealth_assure_adb_rider);
            setFocusable(cb_bi_smart_wealth_assure_adb_rider);
            cb_bi_smart_wealth_assure_adb_rider.requestFocus();

        } else if (v.getId() == edt_bi_smart_wealth_assure_adb_rider_sum_assured
                .getId()) {
            commonMethods.hideKeyboard(edt_bi_smart_wealth_assure_adb_rider_sum_assured, context);
        }

        return true;
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

        boolean flagFocus = true;
        spnr_bi_smart_wealth_assure_life_assured_title.requestFocus();

    }


    private void addListenerOnSubmit() {
        smartWealthAssureBean = new SmartWealthAssureBean();

        if (cb_staffdisc.isChecked()) {
            smartWealthAssureBean.setIsForStaffOrNot(true);
        } else {
            smartWealthAssureBean.setIsForStaffOrNot(false);
        }
        if (cb_kerladisc.isChecked()) {
            smartWealthAssureBean.setKerlaDisc(true);
            // smartWealthAssureBean.setServiceTax(true);
        } else {
            // smartWealthAssureBean.setServiceTax(false);
            smartWealthAssureBean.setKerlaDisc(false);
        }
        if (cb_bi_smart_wealth_assure_JKResident.isChecked()) {
            smartWealthAssureBean.setJkResident(true);
        } else {
            smartWealthAssureBean.setJkResident(false);
        }

        smartWealthAssureBean.setAgeAtEntry(Integer
                .parseInt(edt_bi_smart_wealth_assure_life_assured_age.getText()
                        .toString()));
        smartWealthAssureBean.setGender(spnr_bi_smart_wealth_assure_selGender
                .getSelectedItem().toString());

        smartWealthAssureBean.setPolicyTerm_Basic(Integer
                .parseInt(spnr_bi_smart_wealth_assure_policyterm
                        .getSelectedItem().toString()));

        smartWealthAssureBean.setPremFreqMode("Single");

        smartWealthAssureBean.setPremiumAmount(Double
                .parseDouble(edt_bi_smart_wealth_assure_sum_assured_amount
                        .getText().toString()));

        smartWealthAssureBean
                .setSAMF(Double.parseDouble(edt_smart_wealth_assure_samf
                        .getText().toString()));

        smartWealthAssureBean
                .setYearsElapsedSinceInception(Integer
                        .parseInt(edt_smart_wealth_assure_noOfYearsElapsedSinceInception
                                .getText().toString()));

        if (!edt_smart_wealth_assure_percent_EquityFund.getText().toString()
                .equals(""))
            smartWealthAssureBean.setPercentToBeInvested_EquityFund(Double
                    .parseDouble(edt_smart_wealth_assure_percent_EquityFund
                            .getText().toString()));
        else
            smartWealthAssureBean.setPercentToBeInvested_EquityFund(0);

        if (!edt_smart_wealth_assure_percent_BondFund.getText().toString()
                .equals(""))
            smartWealthAssureBean.setPercentToBeInvested_BondFund(Double
                    .parseDouble(edt_smart_wealth_assure_percent_BondFund
                            .getText().toString()));
        else
            smartWealthAssureBean.setPercentToBeInvested_BondFund(0);

        smartWealthAssureBean.setEffectivePremium(Double
                .parseDouble(getEffectivePremium()));

        if (!percent_BalancedFund.getText().toString().equals(""))
            smartWealthAssureBean.setPercentToBeInvested_BalancedFund(Double
                    .parseDouble(percent_BalancedFund.getText().toString()));
        else
            smartWealthAssureBean.setPercentToBeInvested_BalancedFund(0);


        if (!percent_MoneyMktFund.getText().toString().equals(""))
            smartWealthAssureBean
                    .setPercentToBeInvested_MoneyMarketFund(Double
                            .parseDouble(percent_MoneyMktFund.getText()
                                    .toString()));
        else
            smartWealthAssureBean.setPercentToBeInvested_MoneyMarketFund(0);


        if (!percent_BondOptimiserFund.getText().toString().equals(""))
            smartWealthAssureBean.setPercentToBeInvested_BondOptimiserFund(Double
                    .parseDouble(percent_BondOptimiserFund.getText().toString()));
        else
            smartWealthAssureBean.setPercentToBeInvested_BondOptimiserFund(0);

        if (!percent_CorporateBondFund.getText().toString().equals(""))
            smartWealthAssureBean.setPercentToBeInvested_CorpBondFund(Double
                    .parseDouble(percent_CorporateBondFund.getText().toString()));
        else
            smartWealthAssureBean.setPercentToBeInvested_CorpBondFund(0);


        if (!percent_PureFund.getText().toString().equals(""))
            smartWealthAssureBean.setPercentToBeInvested_PureFund(Double
                    .parseDouble(percent_PureFund.getText().toString()));
        else
            smartWealthAssureBean.setPercentToBeInvested_PureFund(0);

        // Adb
        // if(cb_bi_smart_wealth_assure_adb_rider.isChecked()==true){
        // smartWealthAssureBean.setSumAssuredADB(Integer.parseInt(edt_bi_smart_wealth_assure_adb_rider_sum_assured.getText().toString()));
        //
        // }else{
        // smartWealthAssureBean.setSumAssuredADB(0);
        // }
        //
        // smartWealthAssureBean.setTermADB(Integer.parseInt(spnr_bi_smart_wealth_assure_adb_rider_term
        // .getSelectedItem().toString()));
        //
        // smartWealthAssureBean.setIsADBchecked(cb_bi_smart_wealth_assure_adb_rider.isChecked());

        if (cb_bi_smart_wealth_assure_adb_rider.isChecked()) {
            smartWealthAssureBean.setIsADBchecked(true);
            smartWealthAssureBean.setSumAssuredADB(Integer
                    .parseInt(edt_bi_smart_wealth_assure_adb_rider_sum_assured
                            .getText().toString()));
            smartWealthAssureBean.setTermADB(Integer
                    .parseInt(spnr_bi_smart_wealth_assure_adb_rider_term
                            .getSelectedItem().toString()));

        } else {
            smartWealthAssureBean.setIsADBchecked(false);
        }

        smartWealthAssureBean.setPF(1);

        showSmartWealthAssureOutputPg(smartWealthAssureBean);

    }

    // Display Smart Wealth Assure Output Screen
    private void showSmartWealthAssureOutputPg(
            SmartWealthAssureBean smartWealthAssureBean) {

        String staffStatus;
        if (smartWealthAssureBean.getIsForStaffOrNot())
            staffStatus = "sbi";
        else
            staffStatus = "none";

        // For BI
        retVal = new StringBuilder();

        String[] outputArr = getOutput("BI_Incl_Mort & Ser Tax",
                smartWealthAssureBean);
        String[] outputReductionYield = getOutputReductionYield(
                "Reduction in Yeild_CAP", smartWealthAssureBean);
        try {
            retVal.append("<?xml version='1.0' encoding='utf-8' ?><smartwealthassure>");
            retVal.append("<errCode>0</errCode>");
            retVal.append("<staffStatus>").append(staffStatus).append("</staffStatus>");
            retVal.append("<maturityAge>").append(smartWealthAssureBean.getAgeAtEntry() + smartWealthAssureBean
                    .getPolicyTerm_Basic()).append("</maturityAge>");

            retVal.append("<annPrem>").append(smartWealthAssureBean.getEffectivePremium()).append("</annPrem>").append("<sumAssured>").append(Double.parseDouble(outputArr[0])).append("</sumAssured>").append("<fundVal4>").append(Double.parseDouble(outputArr[1])).append("</fundVal4>").append("<fundVal8>").append(Double.parseDouble(outputArr[2])).append("</fundVal8>").append("<redInYieldMat>").append(outputReductionYield[0]).append("</redInYieldMat>").append("<redInYieldNoYr>").append(outputReductionYield[1]).append("</redInYieldNoYr>").append("<netYield4Pa>").append(outputReductionYield[2]).append("</netYield4Pa>").append("<netYield8Pa>").append(outputReductionYield[3]).append("</netYield8Pa>");

            int index = smartWealthAssureBean.getPolicyTerm_Basic();
            String FundValAtEnd4Pr = prsObj.parseXmlTag(bussIll.toString(), "FundValAtEnd4Pr" + index + "");
            String FundValAtEnd8Pr = prsObj.parseXmlTag(bussIll.toString(), "FundValAtEnd8Pr" + index + "");

            retVal.append("<FundValAtEnd4Pr" + index + ">" + FundValAtEnd4Pr + "</FundValAtEnd4Pr" + index + ">");
            retVal.append("<FundValAtEnd8Pr" + index + ">" + FundValAtEnd8Pr + "</FundValAtEnd8Pr" + index + ">");

            retVal.append(bussIll.toString());
            retVal.append("</smartwealthassure>");

        } catch (Exception e) {
            retVal.append("<?xml version='1.0' encoding='utf-8' ?><smartwealthassure>" + "<errCode>1</errCode>" + "<errorMessage>").append(e.getMessage()).append("</errorMessage></smartwealthassure>");
        }
        System.out.println("Final output in xml" + retVal.toString());


    }

    /********************************** Calculations starts from here **********************************************************/

    private String[] getOutput(String sheetName,
                               SmartWealthAssureBean smartWealthAssureBean) {
        bussIll = new StringBuilder();

        String[] arrGuaranteedAddition_AM = new String[12];
        String[] arrGuaranteedAddition_BN = new String[12];

        // Temp Variable Declaretion
        int month_E = 0;
        int year_F = 0;
        String policyInForce_G = "Y";
        double fundUnderRGFatEnd_AN = 0;
        double fundsUnderMarketLinkedReturnAtEnd_AO = 0;
        double fundUnderRGFatEnd_BO = 0;
        double fundUnderRGFatStart_Z = 0;
        double fundUnderRGFatStart_BA = 0;
        double fundsUnderMarketLinkedReturnAtStart_AA = 0;
        double fundsUnderMarketLinkedReturnAtStart_BB = 0;
        double fundsUnderMarketLinkedReturnAtEnd_BP = 0;
        double fundUnderRGFafterCharges_AB = 0;
        double fundsUnderMarketLinkedReturnAtStartAfterCharges_AC = 0;
        double fundsUnderMarketLinkedReturnAtStartAfterCharges_BD = 0;
        double fundUnderRGFafterCharges_BC = 0;
        int age_H = 0;
        double premium_I = 0;
        double topUpPremium_J = 0;
        double premiumAllocationCharge_K = 0;
        double guaranteedAddition_AM = 0;
        double guaranteedAddition_BN = 0;
        double topUpCharges_L = 0;
        double serviceTaxOnAllocation_M = 0;
        double amountAvailableForInvestment_N = 0, amountAvailableForInvestment_N1 = 0;
        double transferPercentIfAny_O = 0;
        double allocatedFundToFundsUnderMarketLinkedReturn_P = 0;
        double sumAssuredRelatedCharges_Q = 0;
        double optionCharges_R = 0;
        double policyAdministrationCharge_S = 0;
        double transferOfFundFromRGFtoMarketLinkedFunds_Y = 0;
        double transferOfFundFromRGFtoMarketLinkedFunds_AZ = 0;
        double indexFundAtEnd = 0;
        double transferOfCapitalFromIndexToDailyProtectFund_Z = 0;
        double dailyProtectFundAtStart_AA = 0;
        double dailyProtectFundAtEnd_AO = 0;
        double guaranteeCharge_U = 0;
        double indexFundAtStart_AB = 0;
        double oneHundredPercentOfCummulativePremium_BW = 0;
        double guaranteedAddition_AN = 0;
        double guaranteedAddition_BP = 0;
        double mortalityCharges_T = 0;
        double totalCharges_V = 0;
        double totalServiceTax_exclOfSTonAllocAndSurr_W = 0;
        double dailyProtectFundAfterCharges_AC = 0;
        double indexFundAtStartAfterCharges_AD = 0;
        double additionToIndexFund_AF = 0;
        double additionToDailyProtectFund_AE = 0;
        double additionToFundUnderRGF_AD = 0;
        double additionToFundsUnderMarketLinkedReturn_AE = 0;
        double additionToFundsUnderMarketLinkedReturn_BF = 0;
        double additionToFundUnderRGF_BE = 0;
        double fMCearnedOnFundUnderRGF_AF = 0;
        double fMCearnedOnFundUnderRGF_BG = 0;
        double fMCearnedOnFundsUnderMarketLinkedReturn_AG = 0;
        double fMCearnedOnFundsUnderMarketLinkedReturn_BH = 0;
        double fundBeforeFMC_AI = 0;
        double fundManagementCharge_AJ = 0;
        double serviceTaxOnFMC_AK = 0;
        double totalServiceTax_X = 0;
        double fundValueAfterFMCandBeforeGA_AL = 0;
        double indexFundAtEnd_AP = 0;
        double fundValueAtEnd_AP = 0, _fundValueAtEnd_AP = 0;
        double surrenderCap_BV = 0;
        double surrenderCharges_AQ = 0;
        double surrenderCharges_BR = 0;
        double serviceTaxOnSurrenderCharges_AR = 0;
        double serviceTaxOnSurrenderCharges_BS = 0;
        double surrenderValue_AS = 0;
        double deathBenefit_AT = 0;
        double transferOfCapitalFromIndexToDailyProtectFund_BB = 0;
        double fundValueAtEnd_BQ = 0, _fundValueAtEnd_BQ = 0;
        double mortalityCharges_AU = 0;
        double transferOfIndexFundGainToDailyProtectFund_BA = 0;
        double dailyProtectFundAtEnd_BQ = 0;
        double dailyProtectFundAtStart_BC = 0;
        double guaranteeCharge_AV = 0;
        double totalCharges_AW = 0;
        double totalServiceTax_ExclOfSTonAllocAndsurr_AX = 0;
        double indexFundAtStart_BD = 0;
        double indexFundAtStartAfterCharges_BF = 0;
        double additionToIndexFund_BH = 0;
        double fMCearnedOnIndexFund_BJ = 0;
        double dailyProtectFundAftercharges_BE = 0;
        double additionToDailyProtectFund_BG = 0;
        double fMCearnedOnDailyProtectFund_BI = 0;
        double additionToFundIfAny_AH = 0;
        double additionToFundIfAny_BI = 0;
        double fundBeforeFMC_BJ = 0;
        double fundManagementCharge_BK = 0;
        double serviceTaxOnFMC_BL = 0;
        double fundValueAfterFMCandBeforeGA_BM = 0;
        double indexFundAtEnd_BR = 0;
        double deathBenefit_BU = 0;
        double totalServiceTax_AY = 0;
        double surrenderCharges_BT = 0;
        double serviceTaxOnSurrenderCharges_BU = 0;
        double surrenderValue_BT = 0;

        if (sheetName.equals("BI_Incl_Mort & Ser Tax")) {
            // Input Drop Downs from "BI_Incl_Mort & Ser Tax" sheet
            prop.allocationCharges = true; // *Sheet Name -> BI_Incl_Mort & Ser
            // Tax,*Cell ->B20
            prop.mortalityAndRiderCharges = true; // *Sheet Name -> BI_Incl_Mort
            // & Ser Tax,*Cell ->B21
            prop.administrationAndSArelatedCharges = true; // *Sheet Name ->
            // BI_Incl_Mort &
            // Ser Tax,*Cell
            // ->B22
            prop.fundManagementCharges = true; // *Sheet Name -> BI_Incl_Mort &
            // Ser Tax,*Cell ->B23
            prop.mortalityCharges = true; // *Sheet Name -> BI_Incl_Mort & Ser
            // Tax,*Cell ->B27
            prop.riderCharges = true; // *Sheet Name -> BI_Incl_Mort & Ser
            // Tax,*Cell ->B29
            prop.topUpStatus = true; // *Sheet Name -> BI_Incl_Mort & Ser
            // Tax,*Cell ->B31
            prop.surrenderCharges = true; // *Sheet Name -> BI_Incl_Mort & Ser
            // Tax,*Cell ->B24
            prop.topUpStatus = true;
            prop.guaranteeCharges = false;
        }

        // From GUI Input
        boolean staffDisc = smartWealthAssureBean.getIsForStaffOrNot();

//  Added By Saurabh Jain on 10/06/2019 Start

        double serviceTax = 0;
        boolean state = smartWealthAssureBean.isKerlaDisc();

//  Added By Saurabh Jain on 10/06/2019 End

        boolean bancAssuranceDisc = smartWealthAssureBean
                .getIsBancAssuranceDiscOrNot();
        String premFreqMode = smartWealthAssureBean.getPremFreqMode();
        double SAMF = smartWealthAssureBean.getSAMF();
        double SA_ADB = smartWealthAssureBean.getSumAssuredADB();
        int ageAtEntry = smartWealthAssureBean.getAgeAtEntry();
        int policyTerm = smartWealthAssureBean.getPolicyTerm_Basic();
        // Input Drop Downs from GUI[But not displayed in GUI]
        String transferFundStatus = "No Transfer";
        String addTopUp = "No";
        // Internally Calculated Input Fields
        double effectivePremium = smartWealthAssureBean.getEffectivePremium();
        int PF = smartWealthAssureBean.getPF();
        int premiumPayingTerm = 1;
        int policyTermADB = smartWealthAssureBean.getTermADB();
//		System.out.println(Double.parseDouble(commonForAllProd.getRoundOffLevel2(commonForAllProd.getStringWithout_E(SAMF*effectivePremium)))+"     "+SAMF+"       "+effectivePremium);
        double sumAssured = Double.parseDouble(commonForAllProd.getRoundOffLevel2(commonForAllProd.getStringWithout_E(SAMF * effectivePremium)));
        boolean eligibilityRider = isADBeligible(smartWealthAssureBean.getAgeAtEntry());
        smartWealthAssureBean.setEffectiveTopUpPrem(addTopUp, premFreqMode,
                prop.topUpPremiumAmt);
        double effectiveTopUpPrem = smartWealthAssureBean
                .getEffectiveTopUpPrem();
        // Declaration of method Variables/Object required for calculation
        SmartWealthAssureBusinessLogic BIMAST = new SmartWealthAssureBusinessLogic();
        String[] forBIArray = BIMAST
                .getForBIArr(prop.mortalityCharges_AsPercentOfofLICtable);
        int rowNumber = 0;

        // For BI
        double sum_I = 0, sum_K = 0, sum_N = 0, sum_Q = 0, sum_AM = 0, sum_S = 0, sum_V = 0, sum_AA = 0, sum_AP = 0, Commission_BL = 0, sum_J = 0, sum_U = 0, sum_AB = 0, sum_Z = 0, sum_AI = 0, sum_AL = 0, sum_AO = 0, sum_AS = 0, sum_FY_Premium = 0, sum_C = 0, sum_E = 0, sum_AK = 0, sum_AR = 0;

        double _sum_Y = 0, _sum_AM = 0, _sum_J = 0, _sum_I = 0, sum_R = 0, _sum_AL = 0, sum_X = 0, sum_AH = 0, sum_AJ = 0, sum_BN = 0, sum_BM = 0, _sum_BM = 0, _sum_BN = 0, sum_AU = 0, sum_AW = 0, sum_BI = 0, sum_AY = 0, sum_BK = 0, Commission_AQ = 0;

        double sum_01 = 0, sum_02 = 0, sum_03 = 0, sum1_03 = 0, otherCharges_PartB = 0, otherCharges1_PartB = 0, otherCharges_PartA = 0, otherCharges1_PartA = 0, sum_N1 = 0;

        for (int i = 1; i <= (policyTerm * 12); i++)
        // for (int i = 1; i < 4; i++)
        {
            rowNumber++;
            // System.out.println("********************************************* "+i+" Row Output *********************************************");

            BIMAST.setMonth_E(rowNumber);
            month_E = Integer.parseInt(BIMAST.getMonth_E());
            // _month_E=month_E;
            // System.out.println("1.    Month_E : "+BIMAST.getMonth_E());

            BIMAST.setYear_F();
            year_F = Integer.parseInt(BIMAST.getYear_F());
            // _year_F=year_F;
            // System.out.println("2.   Year_F : "+BIMAST.getYear_F());

            if ((month_E % 12) == 0) {
                bussIll.append("<policyYr").append(year_F).append(">").append(year_F).append("</policyYr").append(year_F).append(">");
            }

//			Added By Saurabh Jain on 20/06/2019 Start
            if (state == true && year_F <= 2) {
                serviceTax = 0.19;
            } else {
                serviceTax = 0.18;
            }
            policyInForce_G = BIMAST.getPolicyInForce_G();
            // _policyInForce_G=BIMAST.getPolicyInForce_G();
            // System.out.println("3.   PolicyInForce_G : "+BIMAST.getPolicyInForce_G());

            BIMAST.setAge_H(ageAtEntry);
            age_H = Integer.parseInt(BIMAST.getAge_H());
            // _age_H=age_H;
            // System.out.println("4.   Age_H : "+BIMAST.getAge_H());

            BIMAST.setPremium_I(premiumPayingTerm, PF, effectivePremium);
            premium_I = Double.parseDouble(BIMAST.getPremium_I());
            // _premium_I=premium_I;
            // System.out.println("5.   Premium_I : "+BIMAST.getPremium_I());

            sum_I += premium_I;
            if ((month_E % 12) == 0) {

                bussIll.append("<AnnPrem" + year_F + ">" + commonForAllProd.getStringWithout_E(sum_I) + "</AnnPrem"
                        + year_F + ">");
                _sum_I = sum_I;
                sum_I = 0;
            }

            BIMAST.setTopUpPremium_J(prop.topUpStatus, effectiveTopUpPrem);
            topUpPremium_J = Double.parseDouble(BIMAST.getTopUpPremium_J());
            // _topUpPremium_J=topUpPremium_J;
            // System.out.println("6.   TopUpPremium_J : "+BIMAST.getTopUpPremium_J());

            BIMAST.setPremiumAllocationCharge_K(staffDisc, bancAssuranceDisc,
                    premFreqMode);
            premiumAllocationCharge_K = Double.parseDouble(BIMAST
                    .getPremiumAllocationCharge_K());
            // _premiumAllocationCharge_K=premiumAllocationCharge_K;
            // System.out.println("7.   PremiumAllocationCharge_K : "+BIMAST.getPremiumAllocationCharge_K());

            sum_K += premiumAllocationCharge_K;
            sum_01 = sum_K;
            if ((month_E % 12) == 0) {

                bussIll.append("<PremAllCharge"
                        + year_F
                        + ">"
                        + commonForAllProd.getRoundUp(commonForAllProd
                        .getStringWithout_E(sum_K)) + "</PremAllCharge"
                        + year_F + ">");
                sum_K = 0;
            }

            BIMAST.setTopUpCharges_L(prop.topUp);
            topUpCharges_L = Double.parseDouble(BIMAST.getTopUpCharges_L());
            // _topUpCharges_L=topUpCharges_L;
            // System.out.println("8.   TopUpCharges_L : "+BIMAST.getTopUpCharges_L());

            BIMAST.setServiceTaxOnAllocation_M(prop.allocationCharges,
                    serviceTax);
            serviceTaxOnAllocation_M = Double.parseDouble(BIMAST
                    .getServiceTaxOnAllocation_M());
            // _serviceTaxOnAllocation_M=serviceTaxOnAllocation_M;
            // System.out.println("9.   ServiceTaxOnAllocation_M : "+BIMAST.getServiceTaxOnAllocation_M());

            BIMAST.setAmountAvailableForInvestment_N();
            amountAvailableForInvestment_N = Double.parseDouble(BIMAST
                    .getAmountAvailableForInvestment_N());
            // _amountAvailableForInvestment_N=amountAvailableForInvestment_N;
            // System.out.println("10.   AmountAvailableForInvestment_N : "+BIMAST.getAmountAvailableForInvestment_N());
            sum_N += amountAvailableForInvestment_N;
			/*if ((month_E % 12) == 0) {
                bussIll.append("<AmtAviFrInv"
                        + year_F
                        + ">"
                        + commonForAllProd.getRoundUp(commonForAllProd
								.getStringWithout_E(sum_N)) + "</AmtAviFrInv"
                        + year_F + ">");
				sum_N = 0;
			}*/

            BIMAST.setAmountAvailableForInvestment_N1();
            amountAvailableForInvestment_N1 = Double.parseDouble(BIMAST
                    .getAmountAvailableForInvestment_N1());
            // _amountAvailableForInvestment_N=amountAvailableForInvestment_N;
            // System.out.println("10.   AmountAvailableForInvestment_N : "+BIMAST.getAmountAvailableForInvestment_N());
            sum_N1 += amountAvailableForInvestment_N1;
            if ((month_E % 12) == 0) {
                bussIll.append("<AmtAviFrInv"
                        + year_F
                        + ">"
                        + commonForAllProd.getRoundUp(commonForAllProd
                        .getStringWithout_E(sum_N1)) + "</AmtAviFrInv"
                        + year_F + ">");
                sum_N1 = 0;
            }

            BIMAST.setTransferPercentIfAny_O(prop.year_TransferOfCapital_W62,
                    prop.year_TransferOfCapital_W63, transferFundStatus);
            transferPercentIfAny_O = Double.parseDouble(BIMAST
                    .getTransferPercentIfAny_O());
            // _transferPercentIfAny_O=transferPercentIfAny_O;
            // System.out.println("11.   TransferPercentIfAny_O : "+BIMAST.getTransferPercentIfAny_O());

            BIMAST.setAllocatedFundToFundsUnderMarketLinkedReturn_P(
                    allocatedFundToFundsUnderMarketLinkedReturn_P,
                    smartWealthAssureBean.getPercentToBeInvested_BondFund());
            allocatedFundToFundsUnderMarketLinkedReturn_P = Double
                    .parseDouble(BIMAST
                            .getAllocatedFundToFundsUnderMarketLinkedReturn_P());
            // _allocatedFundToFundsUnderMarketLinkedReturn_P=allocatedFundToFundsUnderMarketLinkedReturn_P;
            // System.out.println("12.   AllocatedFundToFundsUnderMarketLinkedReturn_P : "+allocatedFundToFundsUnderMarketLinkedReturn_P);

            BIMAST.setSumAssuredRelatedCharges_Q(
                    prop.noOfYearsForSArelatedCharges, sumAssured, SAMF,
                    effectivePremium, prop.charge_SumAssuredBase);
            sumAssuredRelatedCharges_Q = Double.parseDouble(BIMAST
                    .getSumAssuredRelatedCharges_Q());
            // _sumAssuredRelatedCharges_Q=sumAssuredRelatedCharges_Q;
            // System.out.println("13.   SumAssuredRelatedCharges_Q : "+BIMAST.getSumAssuredRelatedCharges_Q());

            BIMAST.setOptionCharges_R(prop.riderCharges, eligibilityRider,
                    policyTermADB, prop.ADBrate, SA_ADB);
            optionCharges_R = Double.parseDouble(BIMAST.getOptionCharges_R());
            // _optionCharges_R=optionCharges_R;
            // System.out.println("14.   OptionCharges_R : "+BIMAST.getOptionCharges_R());

            sum_R += optionCharges_R;
            if ((month_E % 12) == 0) {

                bussIll.append("<optionChrg"
                        + year_F
                        + ">"
                        + commonForAllProd.getRoundUp(commonForAllProd
                        .getStringWithout_E(sum_R)) + "</optionChrg"
                        + year_F + ">");
//				sum_R = 0;
            }

            BIMAST.setPolicyAdministrationCharge_S(
                    policyAdministrationCharge_S, prop.charge_Inflation,
                    premFreqMode, prop.charge_PP_Ren);
            policyAdministrationCharge_S = Double.parseDouble(BIMAST
                    .getPolicyAdministrationCharge_S());
            // _policyAdministrationCharge_S=policyAdministrationCharge_S;
            // System.out.println("15.   PolicyAdministrationCharge_S : "+BIMAST.getPolicyAdministrationCharge_S());

            sum_S += policyAdministrationCharge_S;
            sum_02 = sum_S;
            if ((month_E % 12) == 0) {
                bussIll.append("<PolAdminChrg"
                        + year_F
                        + ">"
                        + commonForAllProd.getRoundUp(commonForAllProd
                        .getStringWithout_E(sum_S)) + "</PolAdminChrg"
                        + year_F + ">");
                sum_S = 0;
            }

            BIMAST.setTransferOfFundFromRGFtoMarketLinkedFunds_Y(
                    fundUnderRGFatEnd_AN,
                    prop.noOfYrsAfterWhichTransferTakePlace,
                    prop.charge_SumAssuredBase);
            transferOfFundFromRGFtoMarketLinkedFunds_Y = Double
                    .parseDouble(BIMAST
                            .getTransferOfFundFromRGFtoMarketLinkedFunds_Y());
            // _transferOfFundFromRGFtoMarketLinkedFunds_Y=transferOfFundFromRGFtoMarketLinkedFunds_Y;
            // System.out.println("21.   TransferOfFundFromRGFtoMarketLinkedFunds_Y : "+transferOfFundFromRGFtoMarketLinkedFunds_Y);

            BIMAST.setFundsUnderMarketLinkedReturnAtStart_AA(fundsUnderMarketLinkedReturnAtEnd_AO);
            fundsUnderMarketLinkedReturnAtStart_AA = Double.parseDouble(BIMAST
                    .getFundsUnderMarketLinkedReturnAtStart_AA());
            // _fundsUnderMarketLinkedReturnAtStart_AA=fundsUnderMarketLinkedReturnAtStart_AA;
            // System.out.println("23.   FundsUnderMarketLinkedReturnAtStart_AA : "+fundsUnderMarketLinkedReturnAtStart_AA);

            BIMAST.setFundUnderRGFatStart_Z(fundUnderRGFatEnd_AN,
                    smartWealthAssureBean
                            .getPercentToBeInvested_GuaranteeFund());
            fundUnderRGFatStart_Z = Double.parseDouble(BIMAST
                    .getFundUnderRGFatStart_Z());
            // _fundUnderRGFatStart_Z=fundUnderRGFatStart_Z;
            // System.out.println("22.   FundUnderRGFatStart_Z : "+fundUnderRGFatStart_Z);

            BIMAST.setGuaranteeCharge_U(prop.guaranteeCharges1,
                    prop.charge_Guarantee);
            guaranteeCharge_U = Double.parseDouble(BIMAST
                    .getGuaranteeCharge_U());
            // _guaranteeCharge_U=guaranteeCharge_U;
            // System.out.println("17.   GuaranteeCharge_U : "+guaranteeCharge_U);

            BIMAST.setOneHundredPercentOfCummulativePremium_BW(oneHundredPercentOfCummulativePremium_BW);
            oneHundredPercentOfCummulativePremium_BW = Double
                    .parseDouble(BIMAST
                            .getOneHundredPercentOfCummulativePremium_BW());
            // _oneHundredPercentOfCummulativePremium_BW=oneHundredPercentOfCummulativePremium_BW;
            // System.out.println("71.   OneHundredPercentOfCummulativePremium_BW : "+oneHundredPercentOfCummulativePremium_BW);

            BIMAST.setGuaranteedAddition_AM();
            guaranteedAddition_AM = Double.parseDouble(BIMAST
                    .getGuaranteedAddition_AM());
            // _guaranteedAddition_AM=guaranteedAddition_AM;
            // System.out.println("35.   GuaranteedAddition_AM : "+guaranteedAddition_AM);

            sum_AM += guaranteedAddition_AM;
            if ((month_E % 12) == 0) {
                _sum_AM = Double.parseDouble(commonForAllProd.getRoundUp(sum_AM
                        + ""));
                // bussIll.append("<guranteedAddtn4Pr"+ _year_F +">"
                // +commonForAllProd.getRoundUp(String.valueOf(_sum_AM)) +
                // "</guranteedAddtn4Pr"+ _year_F +">");
                sum_AM = 0;
                // System.out.println("_sum_AD "+_sum_AD);
            }

            BIMAST.setGuaranteedAddition_BN();
            guaranteedAddition_BN = Double.parseDouble(BIMAST
                    .getGuaranteedAddition_BN());
            // _guaranteedAddition_BN=guaranteedAddition_BN;
            // System.out.println("62.   GuaranteedAddition_BN : "+guaranteedAddition_BN);

            sum_BN += guaranteedAddition_BN;
            if ((month_E % 12) == 0) {
                _sum_BN = Double
                        .parseDouble(commonForAllProd
                                .getRoundUp(commonForAllProd
                                        .getStringWithout_E(sum_BN)));
                // bussIll.append("<guranteedAddtn8Pr"+ _year_F +">"
                // +commonForAllProd.getRoundUp(String.valueOf(_sum_BN)) +
                // "</guranteedAddtn8Pr"+ _year_F +">");

                // System.out.println("_sum_AD "+_sum_AD);
            }
            BIMAST.setMortalityCharges_T(fundValueAtEnd_AP, policyTerm,
                    forBIArray, ageAtEntry, sumAssured, prop.mortalityCharges);
            mortalityCharges_T = Double.parseDouble(BIMAST
                    .getMortalityCharges_T());
            // _mortalityCharges_T=mortalityCharges_T;
            // System.out.println("16.   MortalityCharges_T : "+mortalityCharges_T);

            sum_AK += mortalityCharges_T;
            if ((month_E % 12) == 0) {

                bussIll.append("<MortChrg4Pr"
                        + year_F
                        + ">"
                        + commonForAllProd.getRound(commonForAllProd
                        .getStringWithout_E(sum_AK)) + "</MortChrg4Pr"
                        + year_F + ">");
                sum_AK = 0;
            }

            BIMAST.setTotalCharges_V(policyTerm);
            totalCharges_V = Double.parseDouble(BIMAST.getTotalCharges_V());
            // _totalCharges_V=totalCharges_V;
            // System.out.println("18.   TotalCharges_V : "+totalCharges_V);

            sum_V += totalCharges_V;
            if ((month_E % 12) == 0) {
                bussIll.append("<TotCharg4Pr"
                        + year_F
                        + ">"
                        + commonForAllProd.getRound(commonForAllProd
                        .getStringWithout_E(sum_V)) + "</TotCharg4Pr"
                        + year_F + ">");
                sum_V = 0;
            }

            BIMAST.setTotalServiceTax_exclOfSTonAllocAndSurr_W(serviceTax,
                    prop.mortalityAndRiderCharges,
                    prop.administrationAndSArelatedCharges,
                    prop.guaranteeCharges);
            totalServiceTax_exclOfSTonAllocAndSurr_W = Double
                    .parseDouble(BIMAST
                            .getTotalServiceTax_exclOfSTonAllocAndSurr_W());
            // _totalServiceTax_exclOfSTonAllocAndSurr_W=totalServiceTax_exclOfSTonAllocAndSurr_W;
            // System.out.println("19.   TotalServiceTax_exclOfSTonAllocAndSurr_W : "+totalServiceTax_exclOfSTonAllocAndSurr_W);

            BIMAST.setFundUnderRGFafterCharges_AB(prop.guaranteeCharges,
                    serviceTax);
            fundUnderRGFafterCharges_AB = Double.parseDouble(BIMAST
                    .getFundUnderRGFafterCharges_AB());
            // _fundUnderRGFafterCharges_AB=fundUnderRGFafterCharges_AB;
            // System.out.println("24.   FundUnderRGFafterCharges_AB : "+fundUnderRGFafterCharges_AB);

            BIMAST.setFundsUnderMarketLinkedReturnAtStartAfterCharges_AC(
                    prop.guaranteeCharges, serviceTax);
            fundsUnderMarketLinkedReturnAtStartAfterCharges_AC = Double
                    .parseDouble(BIMAST
                            .getFundsUnderMarketLinkedReturnAtStartAfterCharges_AC());
            // _fundsUnderMarketLinkedReturnAtStartAfterCharges_AC=fundsUnderMarketLinkedReturnAtStartAfterCharges_AC;
            // System.out.println("25.   FundsUnderMarketLinkedReturnAtStartAfterCharges_AC : "+fundsUnderMarketLinkedReturnAtStartAfterCharges_AC);

            BIMAST.setAdditionToFundUnderRGF_AD(policyTerm, prop.int1);
            additionToFundUnderRGF_AD = Double.parseDouble(BIMAST
                    .getAdditionToFundUnderRGF_AD());
            // _additionToFundUnderRGF_AD=additionToFundUnderRGF_AD;
            // System.out.println("26.   AdditionToFundUnderRGF_AD : "+additionToFundUnderRGF_AD);

            BIMAST.setAdditionToFundsUnderMarketLinkedReturn_AE(policyTerm,
                    prop.int1);
            additionToFundsUnderMarketLinkedReturn_AE = Double
                    .parseDouble(BIMAST
                            .getAdditionToFundsUnderMarketLinkedReturn_AE());
            // _additionToFundsUnderMarketLinkedReturn_AE=additionToFundsUnderMarketLinkedReturn_AE;
            // System.out.println("27.   AdditionToFundsUnderMarketLinkedReturn_AE : "+additionToFundsUnderMarketLinkedReturn_AE);

            BIMAST.setAdditionToFundIfAny_AH(policyTerm, _fundValueAtEnd_AP, prop.int1);
            additionToFundIfAny_AH = Double.parseDouble(BIMAST
                    .getAdditionToFundIfAny_AH());
            // _additionToFundIfAny_AH=additionToFundIfAny_AH;
            // System.out.println("30.   AdditionToFundIfAny_AH: "+additionToFundIfAny_AH);

            sum_AH += additionToFundIfAny_AH;
            if ((month_E % 12) == 0) {
                bussIll.append("<addtoFundifAny4Pr"
                        + year_F
                        + ">"
                        + commonForAllProd.getRound(commonForAllProd
                        .getStringWithout_E(sum_AH))
                        + "</addtoFundifAny4Pr" + year_F + ">");
                sum_AH = 0;
            }

            BIMAST.setFMCearnedOnFundUnderRGF_AF(policyTerm, prop.charge_Fund,
                    prop.charge_Fund_Ren);
            fMCearnedOnFundUnderRGF_AF = Double.parseDouble(BIMAST
                    .getFMCearnedOnFundUnderRGF_AF());
            // _fMCearnedOnFundUnderRGF_AF=fMCearnedOnFundUnderRGF_AF;
            // System.out.println("28.   FMCearnedOnFundUnderRGF_AF : "+fMCearnedOnFundUnderRGF_AF);

            BIMAST.setFundUnderRGFatEnd_AN(prop.fundManagementCharges,
                    serviceTax);
            fundUnderRGFatEnd_AN = Double.parseDouble(BIMAST
                    .getFundUnderRGFatEnd_AN());
            // _fundUnderRGFatEnd_AN=fundUnderRGFatEnd_AN;
            // System.out.println("36.   FundUnderRGFatEnd_AN: "+fundUnderRGFatEnd_AN);

            // modified by vrushali
            BIMAST.setFMCearnedOnFundsUnderMarketLinkedReturn_AG(policyTerm,
                    prop.charge_Fund, prop.returnGuaranteeFund, prop.bondFund,
                    prop.equityFund, prop.PEmanagedFund,
                    smartWealthAssureBean.getPercentToBeInvested_BondFund(),
                    smartWealthAssureBean.getPercentToBeInvested_EquityFund(),
                    smartWealthAssureBean.getPercentToBeInvested_BalancedFund(),
                    smartWealthAssureBean.getPercentToBeInvested_BondOptimiserFund(),
                    smartWealthAssureBean.getPercentToBeInvested_MoneyMarketFund(),
                    smartWealthAssureBean.getPercentToBeInvested_CorpBondFund(),
                    smartWealthAssureBean.getPercentToBeInvested_PureFund(),
                    prop.FMC_BalancedFund, prop.FMC_BondOptimiserFund, prop.FMC_MoneyMarketFund, prop.FMC_CorpBondFund, prop.FMC_PureFund);
            fMCearnedOnFundsUnderMarketLinkedReturn_AG = Double
                    .parseDouble(BIMAST
                            .getFMCearnedOnFundsUnderMarketLinkedReturn_AG());
            // _fMCearnedOnFundsUnderMarketLinkedReturn_AG=fMCearnedOnFundsUnderMarketLinkedReturn_AG;
            // System.out.println("29.   FMCearnedOnFundsUnderMarketLinkedReturn_AG : "+fMCearnedOnFundsUnderMarketLinkedReturn_AG);

            BIMAST.setFundBeforeFMC_AI(policyTerm, _fundValueAtEnd_AP);
            fundBeforeFMC_AI = Double.parseDouble(BIMAST.getFundBeforeFMC_AI());

            BIMAST.setFundManagementCharge_AJ();
            fundManagementCharge_AJ = Double.parseDouble(BIMAST
                    .getFundManagementCharge_AJ());
            // _fundManagementCharge_AJ=fundManagementCharge_AJ;
            // System.out.println("32.   FundManagementCharge_AJ : "+fundManagementCharge_AJ);

            sum_AJ += fundManagementCharge_AJ;
            sum_03 = sum_AJ;

            if ((month_E % 12) == 0) {
                // System.out.println(year_F+" sum_Z "+sum_Z);
                bussIll.append("<FundMgmtCharg4Pr"
                        + year_F
                        + ">"
                        + commonForAllProd.getRound(commonForAllProd
                        .getStringWithout_E(sum_AJ))
                        + "</FundMgmtCharg4Pr" + year_F + ">");
                sum_AJ = 0;
            }

            BIMAST.setServiceTaxOnFMC_AK(prop.fundManagementCharges,
                    serviceTax, prop.bondFund,
                    prop.equityFund, prop.PEmanagedFund,
                    smartWealthAssureBean.getPercentToBeInvested_BondFund(),
                    smartWealthAssureBean.getPercentToBeInvested_EquityFund(),
                    smartWealthAssureBean.getPercentToBeInvested_BalancedFund(),
                    smartWealthAssureBean.getPercentToBeInvested_BondOptimiserFund(),
                    smartWealthAssureBean.getPercentToBeInvested_MoneyMarketFund(),
                    smartWealthAssureBean.getPercentToBeInvested_CorpBondFund(),
                    smartWealthAssureBean.getPercentToBeInvested_PureFund(),
                    prop.FMC_BalancedFund, prop.FMC_BondOptimiserFund, prop.FMC_MoneyMarketFund, prop.FMC_CorpBondFund, prop.FMC_PureFund);
            serviceTaxOnFMC_AK = Double.parseDouble(BIMAST
                    .getServiceTaxOnFMC_AK());

            BIMAST.setfundsUnderMarketLinkedReturnAtEnd_AO(
                    prop.fundManagementCharges, serviceTax, policyTerm);
            fundsUnderMarketLinkedReturnAtEnd_AO = Double.parseDouble(BIMAST
                    .getFundsUnderMarketLinkedReturnAtEnd_AO());
            // _fundsUnderMarketLinkedReturnAtEnd_AO=fundsUnderMarketLinkedReturnAtEnd_AO;
            // System.out.println("37.   FundsUnderMarketLinkedReturnAtEnd_AO: "+fundsUnderMarketLinkedReturnAtEnd_AO);

            BIMAST.setFundValueAtEnd_AP();
            fundValueAtEnd_AP = Double.parseDouble(BIMAST
                    .getFundValueAtEnd_AP());
            _fundValueAtEnd_AP = fundValueAtEnd_AP;
            // System.out.println("38.   FundValueAtEnd_AP : "+fundValueAtEnd_AP);

			/*BIMAST.setFundBeforeFMC_AI(policyTerm);
			fundBeforeFMC_AI = Double.parseDouble(BIMAST.getFundBeforeFMC_AI());*/
            // _fundBeforeFMC_AI=fundBeforeFMC_AI;
            // System.out.println("31.   FundBeforeFMC_AI : "+fundBeforeFMC_AI);

			/*BIMAST.setFundManagementCharge_AJ();
            fundManagementCharge_AJ = Double.parseDouble(BIMAST
                    .getFundManagementCharge_AJ());
            // _fundManagementCharge_AJ=fundManagementCharge_AJ;
            // System.out.println("32.   FundManagementCharge_AJ : "+fundManagementCharge_AJ);

            sum_AJ += fundManagementCharge_AJ;
			sum_03 =sum_AJ;

            if ((month_E % 12) == 0) {
                // System.out.println(year_F+" sum_Z "+sum_Z);
                bussIll.append("<FundMgmtCharg4Pr"
                        + year_F
                        + ">"
						+ commonForAllProd.getRound(commonForAllProd
                        .getStringWithout_E(sum_AJ))
                        + "</FundMgmtCharg4Pr" + year_F + ">");
                sum_AJ = 0;
			}*/


            otherCharges_PartA = sum_01 + sum_02 + sum_03 + sum_R;

            if ((month_E % 12) == 0) {

                bussIll.append("<OtherCharges4Pr_PartA" + year_F + ">"
                        + commonForAllProd.getRound(String
                        .valueOf(otherCharges_PartA)) + "</OtherCharges4Pr_PartA" + year_F
                        + ">");
                otherCharges_PartA = 0;
//				sum_R=0;
            }

            if ((month_E % 12) == 0) {

                bussIll.append("<OtherCharges4Pr_PartB" + year_F + ">"
                        + commonForAllProd.getRound(String
                        .valueOf(otherCharges_PartB)) + "</OtherCharges4Pr_PartB" + year_F
                        + ">");
                otherCharges_PartB = 0;
            }

			/*BIMAST.setServiceTaxOnFMC_AK(prop.fundManagementCharges,
                    serviceTax);
            serviceTaxOnFMC_AK = Double.parseDouble(BIMAST
					.getServiceTaxOnFMC_AK());*/
            // _serviceTaxOnFMC_AK=serviceTaxOnFMC_AK;
            // System.out.println("33.   ServiceTaxOnFMC_AK : "+serviceTaxOnFMC_AK);

            BIMAST.setTotalServiceTax_X();
            totalServiceTax_X = Double.parseDouble(BIMAST
                    .getTotalServiceTax_X());
            // _totalServiceTax_X=totalServiceTax_X;
            // System.out.println("20.   TotalServiceTax_X : "+totalServiceTax_X);

            sum_X += totalServiceTax_X;
            if ((month_E % 12) == 0) {
                // System.out.println(year_F+" sum_V "+sum_V);
                bussIll.append("<TotServTax4Pr"
                        + year_F
                        + ">"
                        + commonForAllProd.getRoundUp(commonForAllProd
                        .getStringWithout_E(sum_X)) + "</TotServTax4Pr"
                        + year_F + ">");
                sum_X = 0;
            }

            BIMAST.setFundValueAfterFMCandBeforeGA_AL(policyTerm);
            fundValueAfterFMCandBeforeGA_AL = Double.parseDouble(BIMAST
                    .getFundValueAfterFMCandBeforeGA_AL());
            // _fundValueAfterFMCandBeforeGA_AL=fundValueAfterFMCandBeforeGA_AL;
            // System.out.println("34.   FundValueAfterFMCandBeforeGA_AL : "+fundValueAfterFMCandBeforeGA_AL);

            sum_AL = Double.parseDouble(commonForAllProd
                    .getRoundUp(commonForAllProd.getStringWithout_E(fundValueAfterFMCandBeforeGA_AL)));
            if ((month_E % 12) == 0) {
                _sum_AL = Double.parseDouble(commonForAllProd
                        .getRoundUp(commonForAllProd.getStringWithout_E(sum_AL)));
                // System.out.println("sum_ac "+_sum_AC);

            }

            if ((month_E % 12) == 0) {

                //System.out.println("_sum_AL "+_sum_AL);
                //System.out.println("_sum_AM "+_sum_AM);
                sum_AP = Double.parseDouble("" + (_sum_AL + _sum_AM));
                bussIll.append("<FundValAtEnd4Pr"
                        + year_F
                        + ">"
                        + commonForAllProd.getRound(commonForAllProd
                        .getStringWithout_E(sum_AP))
                        + "</FundValAtEnd4Pr" + year_F + ">");
                //System.out.println("sum_AP "+sum_AP);
                sum_AL = 0;
                sum_AM = 0;

            }

            if ((month_E % 12) == 0) {
                //					double temp = Math.round(Double.parseDouble(commonForAllProd.getRoundUp(String.valueOf(_fundValueAtEnd_AC))) + _sum_Y);
                //					System.out.println(commonForAllProd.getRoundUp(String.valueOf(_fundValueAtEnd_AC)) + " + " + _sum_Y + " = " + temp);
                bussIll.append("<FundBefFMC4Pr" + year_F + ">" + Math.round(Double.parseDouble(commonForAllProd.getRound(commonForAllProd.getStringWithout_E(sum_AP))) + sum_03) + "</FundBefFMC4Pr" + year_F + ">");
            }

            BIMAST.setSurrenderCap_BV(effectivePremium);
            surrenderCap_BV = Double.parseDouble(BIMAST.getSurrenderCap_BV());
            // _surrenderCap_BV=surrenderCap_BV;
            // System.out.println("70.   SurrenderCap_BV : "+surrenderCap_BV);

            BIMAST.setSurrenderCharges_AQ(effectivePremium, premFreqMode);
            surrenderCharges_AQ = Double.parseDouble(BIMAST
                    .getSurrenderCharges_AQ());
            // _surrenderCharges_AQ=surrenderCharges_AQ;
            // System.out.println("39.   SurrenderCharges_AQ : "+surrenderCharges_AQ);

            BIMAST.setServiceTaxOnSurrenderCharges_AR(prop.surrenderCharges,
                    serviceTax);
            serviceTaxOnSurrenderCharges_AR = Double.parseDouble(BIMAST
                    .getServiceTaxOnSurrenderCharges_AR());
            // _serviceTaxOnSurrenderCharges_AR=serviceTaxOnSurrenderCharges_AR;
            // System.out.println("40.   ServiceTaxOnSurrenderCharges_AR : "+serviceTaxOnSurrenderCharges_AR);

            BIMAST.setSurrenderValue_AS();
            surrenderValue_AS = Double.parseDouble(BIMAST
                    .getSurrenderValue_AS());
            // _surrenderValue_AS=surrenderValue_AS;
            // System.out.println("41.   SurrenderValue_AS: "+surrenderValue_AS);

            if ((month_E % 12) == 0) {
                bussIll.append("<SurrVal4Pr"
                        + year_F
                        + ">"
                        + commonForAllProd.getRound(commonForAllProd
                        .getStringWithout_E(surrenderValue_AS))
                        + "</SurrVal4Pr" + year_F + ">");

            }

            BIMAST.setDeathBenefit_AT(policyTerm, sumAssured);
            deathBenefit_AT = Double.parseDouble(BIMAST.getDeathBenefit_AT());
            // _deathBenefit_AT=deathBenefit_AT;
            // System.out.println("42.   DeathBenefit_AT: "+deathBenefit_AT);

            if ((month_E % 12) == 0) {

                bussIll.append("<DeathBen4Pr"
                        + year_F
                        + ">"
                        + commonForAllProd.getRound(commonForAllProd
                        .getStringWithout_E(deathBenefit_AT))
                        + "</DeathBen4Pr" + year_F + ">");

            }
            BIMAST.setMortalityCharges_AU(fundValueAtEnd_BQ, policyTerm,
                    forBIArray, ageAtEntry, sumAssured, prop.mortalityCharges);
            mortalityCharges_AU = Double.parseDouble(BIMAST
                    .getMortalityCharges_AU());
            // _mortalityCharges_AU=mortalityCharges_AU;
            // System.out.println("43.   MortalityCharges_AU : "+mortalityCharges_AU);

            sum_AU += mortalityCharges_AU;
            if ((month_E % 12) == 0) {

                bussIll.append("<MortChrg8Pr"
                        + year_F
                        + ">"
                        + commonForAllProd.getRound(commonForAllProd
                        .getStringWithout_E(sum_AU)) + "</MortChrg8Pr"
                        + year_F + ">");
                sum_AU = 0;
            }

            BIMAST.setTransferOfFundFromRGFtoMarketLinkedFunds_AZ(
                    fundUnderRGFatEnd_BO,
                    prop.noOfYrsAfterWhichTransferTakePlace,
                    prop.charge_SumAssuredBase);
            transferOfFundFromRGFtoMarketLinkedFunds_AZ = Double
                    .parseDouble(BIMAST
                            .getTransferOfFundFromRGFtoMarketLinkedFunds_AZ());
            // _transferOfFundFromRGFtoMarketLinkedFunds_AZ=transferOfFundFromRGFtoMarketLinkedFunds_AZ;
            // System.out.println("48.   TransferOfFundFromRGFtoMarketLinkedFunds_AZ : "+transferOfFundFromRGFtoMarketLinkedFunds_AZ);

            BIMAST.setFundsUnderMarketLinkedReturnAtStart_BB(
                    fundsUnderMarketLinkedReturnAtEnd_BP, smartWealthAssureBean
                            .getPercentToBeInvested_GuaranteeFund());
            fundsUnderMarketLinkedReturnAtStart_BB = Double.parseDouble(BIMAST
                    .getFundsUnderMarketLinkedReturnAtStart_BB());
            // _fundsUnderMarketLinkedReturnAtStart_BB=fundsUnderMarketLinkedReturnAtStart_BB;
            // System.out.println("50.   FundsUnderMarketLinkedReturnAtStart_BB : "+fundsUnderMarketLinkedReturnAtStart_BB);

            BIMAST.setFundUnderRGFatStart_BA(fundUnderRGFatEnd_BO,
                    smartWealthAssureBean
                            .getPercentToBeInvested_GuaranteeFund());
            fundUnderRGFatStart_BA = Double.parseDouble(BIMAST
                    .getFundUnderRGFatStart_BA());
            // _fundUnderRGFatStart_BA=fundUnderRGFatStart_BA;
            // System.out.println("49.   FundUnderRGFatStart_BA : "+fundUnderRGFatStart_BA);

            BIMAST.setGuaranteeCharge_AV(prop.guaranteeCharges1,
                    prop.charge_Guarantee);
            guaranteeCharge_AV = Double.parseDouble(BIMAST
                    .getGuaranteeCharge_AV());
            // _guaranteeCharge_AV=guaranteeCharge_AV;
            // System.out.println("44.   GuaranteeCharge_AV : "+guaranteeCharge_AV);

            BIMAST.setTotalServiceTax_ExclOfSTonAllocAndSurr_AX(
                    serviceTax, prop.mortalityAndRiderCharges,
                    prop.administrationAndSArelatedCharges,
                    prop.guaranteeCharges);
            totalServiceTax_ExclOfSTonAllocAndsurr_AX = Double
                    .parseDouble(BIMAST
                            .getTotalServiceTax_ExclOfSTonAllocAndSurr_AX());
            // _totalServiceTax_ExclOfSTonAllocAndsurr_AX=totalServiceTax_ExclOfSTonAllocAndsurr_AX;
            // System.out.println("46.   TotalServiceTax_ExclOfSTonAllocAndsurr_AX: "+totalServiceTax_ExclOfSTonAllocAndsurr_AX);
            //
            BIMAST.setTotalCharges_AW(policyTerm);
            totalCharges_AW = Double.parseDouble(BIMAST.getTotalCharges_AW());
            // _totalCharges_AW=totalCharges_AW;
            // System.out.println("45.   TotalCharges_AW: "+totalCharges_AW);

            sum_AW += totalCharges_AW;
            if ((month_E % 12) == 0) {
                bussIll.append("<TotCharg8Pr"
                        + year_F
                        + ">"
                        + commonForAllProd.getRound(commonForAllProd
                        .getStringWithout_E(sum_AW)) + "</TotCharg8Pr"
                        + year_F + ">");
                sum_AW = 0;
            }

            BIMAST.setFundUnderRGFafterCharges_BC(prop.guaranteeCharges,
                    serviceTax);
            fundUnderRGFafterCharges_BC = Double.parseDouble(BIMAST
                    .getFundUnderRGFafterCharges_BC());
            // _fundUnderRGFafterCharges_BC=fundUnderRGFafterCharges_BC;
            // System.out.println("51.   FundUnderRGFafterCharges_BC : "+fundUnderRGFafterCharges_BC);

            BIMAST.setFundsUnderMarketLinkedReturnAtStartAfterCharges_BD(
                    prop.guaranteeCharges, serviceTax);
            fundsUnderMarketLinkedReturnAtStartAfterCharges_BD = Double
                    .parseDouble(BIMAST
                            .getFundsUnderMarketLinkedReturnAtStartAfterCharges_BD());
            // _fundsUnderMarketLinkedReturnAtStartAfterCharges_BD=fundsUnderMarketLinkedReturnAtStartAfterCharges_BD;
            // System.out.println("52.   FundsUnderMarketLinkedReturnAtStartAfterCharges_BD : "+fundsUnderMarketLinkedReturnAtStartAfterCharges_BD);

            BIMAST.setAdditionToFundUnderRGF_BE(policyTerm, prop.int2);
            additionToFundUnderRGF_BE = Double.parseDouble(BIMAST
                    .getAdditionToFundUnderRGF_BE());
            // _additionToFundUnderRGF_BE=additionToFundUnderRGF_BE;
            // System.out.println("53.   AdditionToFundUnderRGF_BE : "+additionToFundUnderRGF_BE);

            BIMAST.setAdditionToFundsUnderMarketLinkedReturn_BF(policyTerm,
                    prop.int2);
            additionToFundsUnderMarketLinkedReturn_BF = Double
                    .parseDouble(BIMAST
                            .getAdditionToFundsUnderMarketLinkedReturn_BF());
            // _additionToFundsUnderMarketLinkedReturn_BF=additionToFundsUnderMarketLinkedReturn_BF;
            // System.out.println("54.   AdditionToFundsUnderMarketLinkedReturn_BF : "+additionToFundsUnderMarketLinkedReturn_BF);

            BIMAST.setAdditionToFundIfAny_BI(policyTerm, _fundValueAtEnd_BQ, prop.int2);
            additionToFundIfAny_BI = Double.parseDouble(BIMAST
                    .getAdditionToFundIfAny_BI());
            // _additionToFundIfAny_BI=additionToFundIfAny_BI;
            // System.out.println("57.   AdditionToFundIfAny_BI: "+additionToFundIfAny_BI);

            sum_BI += additionToFundIfAny_BI;
            if ((month_E % 12) == 0) {
                bussIll.append("<addtoFundifAny8Pr"
                        + year_F
                        + ">"
                        + commonForAllProd.getRound(commonForAllProd
                        .getStringWithout_E(sum_BI))
                        + "</addtoFundifAny8Pr" + year_F + ">");
                sum_BI = 0;
            }

            BIMAST.setFMCearnedOnFundUnderRGF_BG(policyTerm, prop.charge_Fund,
                    prop.charge_Fund_Ren);
            fMCearnedOnFundUnderRGF_BG = Double.parseDouble(BIMAST
                    .getFMCearnedOnFundUnderRGF_BG());
            // _fMCearnedOnFundUnderRGF_BG=fMCearnedOnFundUnderRGF_BG;
            // System.out.println("55.   FMCearnedOnFundUnderRGF_BG : "+fMCearnedOnFundUnderRGF_BG);

            BIMAST.setFundUnderRGFatEnd_BO(prop.fundManagementCharges,
                    serviceTax);
            fundUnderRGFatEnd_BO = Double.parseDouble(BIMAST
                    .getFundUnderRGFatEnd_BO());
            // _fundUnderRGFatEnd_BO=fundUnderRGFatEnd_BO;
            // System.out.println("63.   FundUnderRGFatEnd_BO: "+fundUnderRGFatEnd_BO);

            BIMAST.setFMCearnedOnFundsUnderMarketLinkedReturn_BH(policyTerm,
                    prop.charge_Fund, prop.returnGuaranteeFund, prop.bondFund,
                    prop.equityFund, prop.PEmanagedFund, smartWealthAssureBean
                            .getPercentToBeInvested_GuaranteeFund(),
                    smartWealthAssureBean.getPercentToBeInvested_BondFund(),
                    smartWealthAssureBean.getPercentToBeInvested_EquityFund(),
                    smartWealthAssureBean
                            .getPercentToBeInvested_PEmanagedFund(),
                    smartWealthAssureBean.getPercentToBeInvested_BalancedFund(),
                    smartWealthAssureBean.getPercentToBeInvested_BondOptimiserFund(),
                    smartWealthAssureBean.getPercentToBeInvested_MoneyMarketFund(),
                    smartWealthAssureBean.getPercentToBeInvested_CorpBondFund(),
                    smartWealthAssureBean.getPercentToBeInvested_PureFund(),
                    prop.FMC_BalancedFund, prop.FMC_BondOptimiserFund, prop.FMC_MoneyMarketFund, prop.FMC_CorpBondFund, prop.FMC_PureFund);
            fMCearnedOnFundsUnderMarketLinkedReturn_BH = Double
                    .parseDouble(BIMAST
                            .getFMCearnedOnFundsUnderMarketLinkedReturn_BH());
            // _fMCearnedOnFundsUnderMarketLinkedReturn_BH=fMCearnedOnFundsUnderMarketLinkedReturn_BH;
            // System.out.println("56.   FMCearnedOnFundsUnderMarketLinkedReturn_BH : "+fMCearnedOnFundsUnderMarketLinkedReturn_BH);

            BIMAST.setFundBeforeFMC_BJ(policyTerm, _fundValueAtEnd_BQ);
            fundBeforeFMC_BJ = Double.parseDouble(BIMAST.getFundBeforeFMC_BJ());

            BIMAST.setFundManagementCharge_BK();
            fundManagementCharge_BK = Double.parseDouble(BIMAST
                    .getFundManagementCharge_BK());
            // _fundManagementCharge_BK=fundManagementCharge_BK;
//			System.out.println("59.   FundManagementCharge_BK: "
//					+ fundManagementCharge_BK);

            sum_BK += fundManagementCharge_BK;
            sum1_03 = sum_BK;
            if ((month_E % 12) == 0) {
//				System.out.println(year_F
//						+ " FundMgmtCharg8Pr "
//						+ commonForAllProd.getRoundUp(commonForAllProd
//								.getStringWithout_E(sum_BK)));
                bussIll.append("<FundMgmtCharg8Pr"
                        + year_F
                        + ">"
                        + commonForAllProd.getRound(commonForAllProd
                        .getStringWithout_E(sum_BK))
                        + "</FundMgmtCharg8Pr" + year_F + ">");
                sum_BK = 0;
            }

            BIMAST.setServiceTaxOnFMC_BL(prop.fundManagementCharges,
                    serviceTax, prop.bondFund,
                    prop.equityFund, prop.PEmanagedFund,
                    smartWealthAssureBean.getPercentToBeInvested_BondFund(),
                    smartWealthAssureBean.getPercentToBeInvested_EquityFund(),
                    smartWealthAssureBean.getPercentToBeInvested_BalancedFund(),
                    smartWealthAssureBean.getPercentToBeInvested_BondOptimiserFund(),
                    smartWealthAssureBean.getPercentToBeInvested_MoneyMarketFund(),
                    smartWealthAssureBean.getPercentToBeInvested_CorpBondFund(),
                    smartWealthAssureBean.getPercentToBeInvested_PureFund(),
                    prop.FMC_BalancedFund, prop.FMC_BondOptimiserFund, prop.FMC_MoneyMarketFund, prop.FMC_CorpBondFund, prop.FMC_PureFund);
            serviceTaxOnFMC_BL = Double.parseDouble(BIMAST
                    .getServiceTaxOnFMC_BL());

            BIMAST.setfundsUnderMarketLinkedReturnAtEnd_BP(
                    prop.fundManagementCharges, serviceTax, policyTerm);
            fundsUnderMarketLinkedReturnAtEnd_BP = Double.parseDouble(BIMAST
                    .getFundsUnderMarketLinkedReturnAtEnd_BP());
            // _fundsUnderMarketLinkedReturnAtEnd_BP=fundsUnderMarketLinkedReturnAtEnd_BP;
            // System.out.println("64.   FundsUnderMarketLinkedReturnAtEnd_BP: "+fundsUnderMarketLinkedReturnAtEnd_BP);

            BIMAST.setFundValueAtEnd_BQ();
            fundValueAtEnd_BQ = Double.parseDouble(BIMAST
                    .getFundValueAtEnd_BQ());
            _fundValueAtEnd_BQ = fundValueAtEnd_BQ;
            // System.out.println("65.   FundValueAtEnd_BQ : "+fundValueAtEnd_BQ);

            BIMAST.setDeathBenefit_BU(policyTerm, sumAssured);
            deathBenefit_BU = Double.parseDouble(BIMAST.getDeathBenefit_BU());
            // _deathBenefit_BU=deathBenefit_BU;
            // System.out.println("69.   DeathBenefit_BU: "+deathBenefit_BU);

            if ((month_E % 12) == 0) {

                bussIll.append("<DeathBen8Pr"
                        + year_F
                        + ">"
                        + commonForAllProd.getRound(commonForAllProd
                        .getStringWithout_E(deathBenefit_BU))
                        + "</DeathBen8Pr" + year_F + ">");

            }

            BIMAST.setSurrenderCharges_BR(effectivePremium, premFreqMode);
            surrenderCharges_BR = Double.parseDouble(BIMAST
                    .getSurrenderCharges_BR());
            // _surrenderCharges_BR=surrenderCharges_BR;
            // System.out.println("66.   SurrenderCharges_BR : "+surrenderCharges_BR);

            BIMAST.setServiceTaxOnSurrenderCharges_BS(prop.surrenderCharges,
                    serviceTax);
            serviceTaxOnSurrenderCharges_BS = Double.parseDouble(BIMAST
                    .getServiceTaxOnSurrenderCharges_BS());
            // _serviceTaxOnSurrenderCharges_BS=serviceTaxOnSurrenderCharges_BS;
            // System.out.println("67.   ServiceTaxOnSurrenderCharges_BS : "+serviceTaxOnSurrenderCharges_BS);

            BIMAST.setSurrenderValue_BT();
            surrenderValue_BT = Double.parseDouble(BIMAST
                    .getSurrenderValue_BT());
            // _surrenderValue_BT=surrenderValue_BT;
            // System.out.println("68.   SurrenderValue_BT: "+surrenderValue_BT);

            if ((month_E % 12) == 0) {
                bussIll.append("<SurrVal8Pr"
                        + year_F
                        + ">"
                        + commonForAllProd.getRound(commonForAllProd
                        .getStringWithout_E(surrenderValue_BT))
                        + "</SurrVal8Pr" + year_F + ">");
            }
			/*BIMAST.setFundBeforeFMC_BJ(policyTerm);
			fundBeforeFMC_BJ = Double.parseDouble(BIMAST.getFundBeforeFMC_BJ());*/
            // _fundBeforeFMC_BJ=fundBeforeFMC_BJ;
            // System.out.println("58.   FundBeforeFMC_BJ: "+fundBeforeFMC_BJ);

			/*BIMAST.setFundManagementCharge_BK();
            fundManagementCharge_BK = Double.parseDouble(BIMAST
                    .getFundManagementCharge_BK());
            // _fundManagementCharge_BK=fundManagementCharge_BK;
//			System.out.println("59.   FundManagementCharge_BK: "
//					+ fundManagementCharge_BK);

            sum_BK += fundManagementCharge_BK;
			sum1_03 =sum_BK;
            if ((month_E % 12) == 0) {
//				System.out.println(year_F
//						+ " FundMgmtCharg8Pr "
//						+ commonForAllProd.getRoundUp(commonForAllProd
//								.getStringWithout_E(sum_BK)));
                bussIll.append("<FundMgmtCharg8Pr"
                        + year_F
                        + ">"
						+ commonForAllProd.getRound(commonForAllProd
                        .getStringWithout_E(sum_BK))
                        + "</FundMgmtCharg8Pr" + year_F + ">");
                sum_BK = 0;
			}*/


            otherCharges1_PartA = sum_01 + sum_02 + sum1_03 + sum_R;

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
                sum_R = 0;
            }

			/*BIMAST.setServiceTaxOnFMC_BL(prop.fundManagementCharges,
					serviceTax,prop.bondFund,
					prop.equityFund, prop.PEmanagedFund,
					smartWealthAssureBean.getPercentToBeInvested_BondFund(),
					smartWealthAssureBean.getPercentToBeInvested_EquityFund(),
					smartWealthAssureBean.getPercentToBeInvested_BalancedFund(),
					smartWealthAssureBean.getPercentToBeInvested_BondOptimiserFund(),
					smartWealthAssureBean.getPercentToBeInvested_MoneyMarketFund(),
					smartWealthAssureBean.getPercentToBeInvested_CorpBondFund(),
					smartWealthAssureBean.getPercentToBeInvested_PureFund(),
					prop.FMC_BalancedFund,prop.FMC_BondOptimiserFund,prop.FMC_MoneyMarketFund,prop.FMC_CorpBondFund,prop.FMC_PureFund);
			serviceTaxOnFMC_BL = Double.parseDouble(BIMAST
					.getServiceTaxOnFMC_BL());*/
            // _serviceTaxOnFMC_BL=serviceTaxOnFMC_BL;
            // System.out.println("60.   ServiceTaxOnFMC_BL : "+serviceTaxOnFMC_BL);

            BIMAST.setTotalServiceTax_AY();
            totalServiceTax_AY = Double.parseDouble(BIMAST
                    .getTotalServiceTax_AY());
            // _totalServiceTax_AY=totalServiceTax_AY;
//			System.out.println("47.   TotalServiceTax_AY: "
//					+ totalServiceTax_AY);

            sum_AY += totalServiceTax_AY;
//			System.out.println(month_E + " sum_AY : " + sum_AY + "  sum_BK : "
//					+ sum_BK);
            if ((month_E % 12) == 0) {
//				System.out.println(year_F
//						+ " TotServTax8Pr "
//						+ commonForAllProd.getRoundUp(commonForAllProd
//								.getStringWithout_E(sum_AY)));
                bussIll.append("<TotServTax8Pr"
                        + year_F
                        + ">"
                        + commonForAllProd.getRound(commonForAllProd
                        .getStringWithout_E(sum_AY))
                        + "</TotServTax8Pr" + year_F + ">");
                sum_AY = 0;
            }

            BIMAST.setFundValueAfterFMCandBeforeGA_BM(policyTerm);
            fundValueAfterFMCandBeforeGA_BM = Double.parseDouble(BIMAST
                    .getFundValueAfterFMCandBeforeGA_BM());
            // _fundValueAfterFMCandBeforeGA_BM=fundValueAfterFMCandBeforeGA_BM;

            sum_BM = Double.parseDouble(commonForAllProd
                    .getRoundUp(commonForAllProd.getStringWithout_E(fundValueAfterFMCandBeforeGA_BM)));
            if ((month_E % 12) == 0) {
                _sum_BM = Double.parseDouble(commonForAllProd
                        .getRoundUp(commonForAllProd.getStringWithout_E(sum_BM)));
                // System.out.println("sum_ac "+_sum_AC);

            }

            if ((month_E % 12) == 0) {
                sum_BN = Double.parseDouble("" + (_sum_BM + _sum_BN));
                bussIll.append("<FundValAtEnd8Pr"
                        + year_F
                        + ">"
                        + commonForAllProd.getRound(commonForAllProd
                        .getStringWithout_E(sum_BN))
                        + "</FundValAtEnd8Pr" + year_F + ">");
                sum_BM = 0;
                //sum_BN = 0;
            }

            if ((month_E % 12) == 0) {
                //					double temp = Math.round(Double.parseDouble(commonForAllProd.getRoundUp(String.valueOf(_fundValueAtEnd_AC))) + _sum_Y);
                //					System.out.println(commonForAllProd.getRoundUp(String.valueOf(_fundValueAtEnd_AC)) + " + " + _sum_Y + " = " + temp);
                bussIll.append("<FundBefFMC8Pr" + year_F + ">" + Math.round(Double.parseDouble(commonForAllProd.getRound(commonForAllProd.getStringWithout_E(sum_BN))) + sum1_03) + "</FundBefFMC8Pr" + year_F + ">");
                sum_BN = 0;
            }

            if ((month_E % 12) == 0) {
                // System.out.println("aetfef"+BIMAST.getCommission_AQ(_sum_I,sum_J,smartWealthAssureBean));
                Commission_AQ = BIMAST.getCommission_AQ(_sum_I, _sum_J,
                        smartWealthAssureBean);
                // System.out.println(year_F+" Commission_AQ = "+Commission_AQ);
                bussIll.append("<CommIfPay8Pr"
                        + year_F
                        + ">"
                        + commonForAllProd.getRoundUp(commonForAllProd
                        .getStringWithout_E(Commission_AQ))
                        + "</CommIfPay8Pr" + year_F + ">");
                _sum_J = 0;
            }

            // System.out.println("61.   FundValueAfterFMCandBeforeGA_BM: "+fundValueAfterFMCandBeforeGA_BM);

            // System.out.println("***********************************************************************************************************");

        }

        return new String[]{
                (commonForAllProd.getStringWithout_E(sumAssured)),
                commonForAllProd.getRoundUp(commonForAllProd
                        .getStringWithout_E(fundValueAtEnd_AP)),
                commonForAllProd.getRoundUp(commonForAllProd
                        .getStringWithout_E(fundValueAtEnd_BQ)),
                (commonForAllProd.getStringWithout_E(smartWealthAssureBean.getEffectivePremium()))};

    }


    //Returns whether eligibility for ADB rider
    private boolean isADBeligible(int age) {
        if (age >= 18 && age < (64 + 1)) {
            return true;
        } else {
            return false;
        }
    }

    private String[] getOutputReductionYield(String sheetName,
                                             SmartWealthAssureBean smartWealthAssureBean) {
        String[] arrGuaranteedAddition_AM = new String[12];
        String[] arrGuaranteedAddition_BN = new String[12];

        ArrayList<String> List_CC = new ArrayList<String>();
        ArrayList<String> List_CD = new ArrayList<String>();
        ArrayList<String> List_CI = new ArrayList<String>();

        // Temp Variable Declaretion
        int month_E = 0;
        int year_F = 0;
        String policyInForce_G = "Y";
        double fundUnderRGFatEnd_AN = 0;
        double fundsUnderMarketLinkedReturnAtEnd_AO = 0;
        double fundUnderRGFatEnd_BO = 0;
        double fundUnderRGFatStart_Z = 0;
        double fundUnderRGFatStart_BA = 0;
        double fundsUnderMarketLinkedReturnAtStart_AA = 0;
        double fundsUnderMarketLinkedReturnAtStart_BB = 0;
        double fundsUnderMarketLinkedReturnAtEnd_BP = 0;
        double fundUnderRGFafterCharges_AB = 0;
        double fundsUnderMarketLinkedReturnAtStartAfterCharges_AC = 0;
        double fundsUnderMarketLinkedReturnAtStartAfterCharges_BD = 0;
        double fundUnderRGFafterCharges_BC = 0;
        int age_H = 0;
        double premium_I = 0;
        double topUpPremium_J = 0;
        double premiumAllocationCharge_K = 0;
        double guaranteedAddition_AM = 0;
        double guaranteedAddition_BN = 0;
        double topUpCharges_L = 0;
        double serviceTaxOnAllocation_M = 0;
        double amountAvailableForInvestment_N = 0;
        double transferPercentIfAny_O = 0;
        double allocatedFundToFundsUnderMarketLinkedReturn_P = 0;
        double sumAssuredRelatedCharges_Q = 0;
        double optionCharges_R = 0;
        double policyAdministrationCharge_S = 0;
        double transferOfFundFromRGFtoMarketLinkedFunds_Y = 0;
        double transferOfFundFromRGFtoMarketLinkedFunds_AZ = 0;
        double indexFundAtEnd = 0;
        double transferOfCapitalFromIndexToDailyProtectFund_Z = 0;
        double dailyProtectFundAtStart_AA = 0;
        double dailyProtectFundAtEnd_AO = 0;
        double guaranteeCharge_U = 0;
        double indexFundAtStart_AB = 0;
        double oneHundredPercentOfCummulativePremium_BW = 0;
        double guaranteedAddition_AN = 0;
        double guaranteedAddition_BP = 0;
        double mortalityCharges_T = 0;
        double totalCharges_V = 0;
        double totalServiceTax_exclOfSTonAllocAndSurr_W = 0;
        double dailyProtectFundAfterCharges_AC = 0;
        double indexFundAtStartAfterCharges_AD = 0;
        double additionToIndexFund_AF = 0;
        double additionToDailyProtectFund_AE = 0;
        double additionToFundUnderRGF_AD = 0;
        double additionToFundsUnderMarketLinkedReturn_AE = 0;
        double additionToFundsUnderMarketLinkedReturn_BF = 0;
        double additionToFundUnderRGF_BE = 0;
        double fMCearnedOnFundUnderRGF_AF = 0;
        double fMCearnedOnFundUnderRGF_BG = 0;
        double fMCearnedOnFundsUnderMarketLinkedReturn_AG = 0;
        double fMCearnedOnFundsUnderMarketLinkedReturn_BH = 0;
        double fundBeforeFMC_AI = 0;
        double fundManagementCharge_AJ = 0;
        double serviceTaxOnFMC_AK = 0;
        double totalServiceTax_X = 0;
        double fundValueAfterFMCandBeforeGA_AL = 0;
        double indexFundAtEnd_AP = 0;
        double fundValueAtEnd_AP = 0;
        double surrenderCap_BV = 0;
        double surrenderCharges_AQ = 0;
        double surrenderCharges_BR = 0;
        double serviceTaxOnSurrenderCharges_AR = 0;
        double serviceTaxOnSurrenderCharges_BS = 0;
        double surrenderValue_AS = 0;
        double deathBenefit_AT = 0;
        double transferOfCapitalFromIndexToDailyProtectFund_BB = 0;
        double fundValueAtEnd_BQ = 0;
        double mortalityCharges_AU = 0;
        double transferOfIndexFundGainToDailyProtectFund_BA = 0;
        double dailyProtectFundAtEnd_BQ = 0;
        double dailyProtectFundAtStart_BC = 0;
        double guaranteeCharge_AV = 0;
        double totalCharges_AW = 0;
        double totalServiceTax_ExclOfSTonAllocAndsurr_AX = 0;
        double indexFundAtStart_BD = 0;
        double indexFundAtStartAfterCharges_BF = 0;
        double additionToIndexFund_BH = 0;
        double fMCearnedOnIndexFund_BJ = 0;
        double dailyProtectFundAftercharges_BE = 0;
        double additionToDailyProtectFund_BG = 0;
        double fMCearnedOnDailyProtectFund_BI = 0;
        double additionToFundIfAny_AH = 0;
        double additionToFundIfAny_BI = 0;
        double fundBeforeFMC_BJ = 0;
        double fundManagementCharge_BK = 0;
        double serviceTaxOnFMC_BL = 0;
        double fundValueAfterFMCandBeforeGA_BM = 0;
        double indexFundAtEnd_BR = 0;
        double deathBenefit_BU = 0;
        double totalServiceTax_AY = 0;
        double surrenderCharges_BT = 0;
        double serviceTaxOnSurrenderCharges_BU = 0;
        double surrenderValue_BT = 0, reductionYield_CI = 0, reductionYield_CD = 0, reductionYield_CC = 0,
                // _reductionYield_CI=0,
                // _reductionYield_CC=0,
                // _reductionYield_CD=0,
                month_CB = 0,
                // _month_CB=0,
                // _irrAnnual_CC=0,
                // _irrAnnual_CD=0,
                // _irrAnnual_CI=0,
                irrAnnual_CC = 0, irrAnnual_CD = 0, irrAnnual_CI = 0, reductionInYieldMaturityAt = 0,
                // _reductionInYieldMaturityAt=0,
                // _reductionInYieldNumberOfYearsElapsedSinceInception=0,
                reductionInYieldNumberOfYearsElapsedSinceInception = 0, netYield4pa = 0, netYield8pa = 0;

        if (sheetName.equals("Reduction in Yeild_CAP")) {
            // Input Drop Downs from "BI_Incl_Mort & Ser Tax" sheet
            prop.allocationCharges = false; // *Sheet Name -> BI_Incl_Mort & Ser
            // Tax,*Cell ->B20
            prop.mortalityAndRiderCharges = false; // *Sheet Name ->
            // BI_Incl_Mort & Ser
            // Tax,*Cell ->B21
            prop.administrationAndSArelatedCharges = false; // *Sheet Name ->
            // BI_Incl_Mort &
            // Ser Tax,*Cell
            // ->B22
            prop.fundManagementCharges = false; // *Sheet Name -> BI_Incl_Mort &
            // Ser Tax,*Cell ->B23
            prop.mortalityCharges = false; // *Sheet Name -> BI_Incl_Mort & Ser
            // Tax,*Cell ->B27
            prop.riderCharges = false; // *Sheet Name -> BI_Incl_Mort & Ser
            // Tax,*Cell ->B29
            prop.topUpStatus = false; // *Sheet Name -> BI_Incl_Mort & Ser
            // Tax,*Cell ->B31
            prop.surrenderCharges = false; // *Sheet Name -> BI_Incl_Mort & Ser
            // Tax,*Cell ->B24
            prop.topUpStatus = false;
            prop.guaranteeCharges = false;
        }

        // From GUI Input
        boolean staffDisc = smartWealthAssureBean.getIsForStaffOrNot();
        boolean bancAssuranceDisc = smartWealthAssureBean
                .getIsBancAssuranceDiscOrNot();
        //double serviceTax=smartWealthAssureBean.getServiceTax();
        double serviceTax = 0;
        boolean isKerlaDisc = smartWealthAssureBean.isKerlaDisc();
        String premFreqMode = smartWealthAssureBean.getPremFreqMode();
        double SAMF = smartWealthAssureBean.getSAMF();
        double SA_ADB = smartWealthAssureBean.getSumAssuredADB();
        int ageAtEntry = smartWealthAssureBean.getAgeAtEntry();
        int policyTerm = smartWealthAssureBean.getPolicyTerm_Basic();
        // Input Drop Downs from GUI[But not displayed in GUI]
        String transferFundStatus = "No Transfer";
        String addTopUp = "No";
        // Internally Calculated Input Fields
        double effectivePremium = smartWealthAssureBean.getEffectivePremium();
        int PF = smartWealthAssureBean.getPF();
        int premiumPayingTerm = 1;
        int policyTermADB = smartWealthAssureBean.getTermADB();
        double sumAssured = (effectivePremium * SAMF);
        boolean eligibilityRider = isADBeligible(ageAtEntry);
        smartWealthAssureBean.setEffectiveTopUpPrem(addTopUp, premFreqMode,
                prop.topUpPremiumAmt);
        double effectiveTopUpPrem = smartWealthAssureBean
                .getEffectiveTopUpPrem();
        // Declaration of method Variables/Object required for calculation
        SmartWealthAssureBusinessLogic BIMAST = new SmartWealthAssureBusinessLogic();
        String[] forBIArray = BIMAST
                .getForBIArr(prop.mortalityCharges_AsPercentOfofLICtable);
        int rowNumber = 0, monthNumber = 0;
        int noOfYearsElapsedSinceInception = smartWealthAssureBean
                .getYearsElapsedSinceInception();
        for (int i = 0; i <= (policyTerm * 12); i++)
        // for (int i = 1; i < 4; i++)
        {
            rowNumber++;
            // System.out.println("********************************************* "+i+" Row Output *********************************************");

            BIMAST.setMonth_E(rowNumber);
            month_E = Integer.parseInt(BIMAST.getMonth_E());
            // _month_E=month_E;
            // System.out.println("1.    Month_E : "+BIMAST.getMonth_E());

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

            BIMAST.setAge_H(ageAtEntry);
            age_H = Integer.parseInt(BIMAST.getAge_H());
            // _age_H=age_H;
            // System.out.println("4.   Age_H : "+BIMAST.getAge_H());

            BIMAST.setPremium_I(premiumPayingTerm, PF, effectivePremium);
            premium_I = Double.parseDouble(BIMAST.getPremium_I());
            // _premium_I=premium_I;
            // System.out.println("5.   Premium_I : "+BIMAST.getPremium_I());

            BIMAST.setTopUpPremium_J(prop.topUpStatus, effectiveTopUpPrem);
            topUpPremium_J = Double.parseDouble(BIMAST.getTopUpPremium_J());
            // _topUpPremium_J=topUpPremium_J;
            // System.out.println("6.   TopUpPremium_J : "+BIMAST.getTopUpPremium_J());

            BIMAST.setPremiumAllocationCharge_K(staffDisc, bancAssuranceDisc,
                    premFreqMode);
            premiumAllocationCharge_K = Double.parseDouble(BIMAST
                    .getPremiumAllocationCharge_K());
            // _premiumAllocationCharge_K=premiumAllocationCharge_K;
            // System.out.println("7.   PremiumAllocationCharge_K : "+BIMAST.getPremiumAllocationCharge_K());

            BIMAST.setTopUpCharges_L(prop.topUp);
            topUpCharges_L = Double.parseDouble(BIMAST.getTopUpCharges_L());
            // _topUpCharges_L=topUpCharges_L;
            // System.out.println("8.   TopUpCharges_L : "+BIMAST.getTopUpCharges_L());

            BIMAST.setServiceTaxOnAllocation_M(
                    prop.allocationChargesReductionYield, serviceTax);
            serviceTaxOnAllocation_M = Double.parseDouble(BIMAST
                    .getServiceTaxOnAllocation_M());
            // _serviceTaxOnAllocation_M=serviceTaxOnAllocation_M;
            // System.out.println("9.   ServiceTaxOnAllocation_M : "+BIMAST.getServiceTaxOnAllocation_M());

            BIMAST.setAmountAvailableForInvestment_N();
            amountAvailableForInvestment_N = Double.parseDouble(BIMAST
                    .getAmountAvailableForInvestment_N());
            // _amountAvailableForInvestment_N=amountAvailableForInvestment_N;
            // System.out.println("10.   AmountAvailableForInvestment_N : "+BIMAST.getAmountAvailableForInvestment_N());

            BIMAST.setTransferPercentIfAny_O(prop.year_TransferOfCapital_W62,
                    prop.year_TransferOfCapital_W63, transferFundStatus);
            transferPercentIfAny_O = Double.parseDouble(BIMAST
                    .getTransferPercentIfAny_O());
            // _transferPercentIfAny_O=transferPercentIfAny_O;
            // System.out.println("11.   TransferPercentIfAny_O : "+BIMAST.getTransferPercentIfAny_O());

            BIMAST.setAllocatedFundToFundsUnderMarketLinkedReturn_P(
                    allocatedFundToFundsUnderMarketLinkedReturn_P,
                    smartWealthAssureBean.getPercentToBeInvested_BondFund());
            allocatedFundToFundsUnderMarketLinkedReturn_P = Double
                    .parseDouble(BIMAST
                            .getAllocatedFundToFundsUnderMarketLinkedReturn_P());
            // _allocatedFundToFundsUnderMarketLinkedReturn_P=allocatedFundToFundsUnderMarketLinkedReturn_P;
            // System.out.println("12.   AllocatedFundToFundsUnderMarketLinkedReturn_P : "+allocatedFundToFundsUnderMarketLinkedReturn_P);

            BIMAST.setSumAssuredRelatedCharges_Q(
                    prop.noOfYearsForSArelatedCharges, sumAssured, SAMF,
                    effectivePremium, prop.charge_SumAssuredBase);
            sumAssuredRelatedCharges_Q = Double.parseDouble(BIMAST
                    .getSumAssuredRelatedCharges_Q());
            // _sumAssuredRelatedCharges_Q=sumAssuredRelatedCharges_Q;
            // System.out.println("13.   SumAssuredRelatedCharges_Q : "+BIMAST.getSumAssuredRelatedCharges_Q());

            BIMAST.setOptionCharges_R(prop.riderChargesReductionYield,
                    eligibilityRider, policyTermADB, prop.ADBrate, SA_ADB);
            optionCharges_R = Double.parseDouble(BIMAST.getOptionCharges_R());
            // _optionCharges_R=optionCharges_R;
            // System.out.println("14.   OptionCharges_R : "+BIMAST.getOptionCharges_R());

            BIMAST.setPolicyAdministrationCharge_S(
                    policyAdministrationCharge_S, prop.charge_Inflation,
                    premFreqMode, prop.charge_PP_Ren);
            policyAdministrationCharge_S = Double.parseDouble(BIMAST
                    .getPolicyAdministrationCharge_S());
            // _policyAdministrationCharge_S=policyAdministrationCharge_S;
            // System.out.println("15.   PolicyAdministrationCharge_S : "+BIMAST.getPolicyAdministrationCharge_S());

            BIMAST.setMonth_CB(monthNumber);
            month_CB = Integer.parseInt(BIMAST.getMonth_CB());
            // _month_CB=month_CB;
            // System.out.println("month_CB "+month_CB);

            BIMAST.setReductionYield_CC(policyTerm, fundValueAtEnd_AP);
            reductionYield_CC = Double.parseDouble(BIMAST
                    .getReductionYield_CC());
            // _reductionYield_CC=reductionYield_CC;
            // System.out.println("reductionYield_CC "+reductionYield_CC);

            BIMAST.setReductionYield_CD(policyTerm, fundValueAtEnd_BQ);
            reductionYield_CD = Double.parseDouble(BIMAST
                    .getReductionYield_CD());
            // _reductionYield_CD=reductionYield_CD;
            // System.out.println("reductionYield_CD "+reductionYield_CD);

            BIMAST.setReductionYield_CI(noOfYearsElapsedSinceInception,
                    fundValueAtEnd_BQ);
            reductionYield_CI = Double.parseDouble(BIMAST
                    .getReductionYield_CI());
            // _reductionYield_CI=reductionYield_CI;
            // System.out.println("reductionYield_CI "+reductionYield_CI);

            BIMAST.setTransferOfFundFromRGFtoMarketLinkedFunds_Y(
                    fundUnderRGFatEnd_AN,
                    prop.noOfYrsAfterWhichTransferTakePlace,
                    prop.charge_SumAssuredBase);
            transferOfFundFromRGFtoMarketLinkedFunds_Y = Double
                    .parseDouble(BIMAST
                            .getTransferOfFundFromRGFtoMarketLinkedFunds_Y());
            // _transferOfFundFromRGFtoMarketLinkedFunds_Y=transferOfFundFromRGFtoMarketLinkedFunds_Y;
            // System.out.println("21.   TransferOfFundFromRGFtoMarketLinkedFunds_Y : "+transferOfFundFromRGFtoMarketLinkedFunds_Y);

            BIMAST.setFundsUnderMarketLinkedReturnAtStart_AA(fundsUnderMarketLinkedReturnAtEnd_AO);
            fundsUnderMarketLinkedReturnAtStart_AA = Double.parseDouble(BIMAST
                    .getFundsUnderMarketLinkedReturnAtStart_AA());
            // _fundsUnderMarketLinkedReturnAtStart_AA=fundsUnderMarketLinkedReturnAtStart_AA;
            // System.out.println("23.   FundsUnderMarketLinkedReturnAtStart_AA : "+fundsUnderMarketLinkedReturnAtStart_AA);

            BIMAST.setFundUnderRGFatStart_Z(fundUnderRGFatEnd_AN,
                    smartWealthAssureBean
                            .getPercentToBeInvested_GuaranteeFund());
            fundUnderRGFatStart_Z = Double.parseDouble(BIMAST
                    .getFundUnderRGFatStart_Z());
            // _fundUnderRGFatStart_Z=fundUnderRGFatStart_Z;
            // System.out.println("22.   FundUnderRGFatStart_Z : "+fundUnderRGFatStart_Z);

            BIMAST.setGuaranteeCharge_U(prop.guaranteeChargesReductionYield,
                    prop.charge_Guarantee);
            guaranteeCharge_U = Double.parseDouble(BIMAST
                    .getGuaranteeCharge_U());
            // _guaranteeCharge_U=guaranteeCharge_U;
            // System.out.println("17.   GuaranteeCharge_U : "+guaranteeCharge_U);

            BIMAST.setOneHundredPercentOfCummulativePremium_BW(oneHundredPercentOfCummulativePremium_BW);
            oneHundredPercentOfCummulativePremium_BW = Double
                    .parseDouble(BIMAST
                            .getOneHundredPercentOfCummulativePremium_BW());
            // _oneHundredPercentOfCummulativePremium_BW=oneHundredPercentOfCummulativePremium_BW;
            // System.out.println("71.   OneHundredPercentOfCummulativePremium_BW : "+oneHundredPercentOfCummulativePremium_BW);

            BIMAST.setGuaranteedAddition_AM();
            guaranteedAddition_AM = Double.parseDouble(BIMAST
                    .getGuaranteedAddition_AM());
            // _guaranteedAddition_AM=guaranteedAddition_AM;
            // System.out.println("35.   GuaranteedAddition_AM : "+guaranteedAddition_AM);

            BIMAST.setGuaranteedAddition_BN();
            guaranteedAddition_BN = Double.parseDouble(BIMAST
                    .getGuaranteedAddition_BN());
            // _guaranteedAddition_BN=guaranteedAddition_BN;
            // System.out.println("62.   GuaranteedAddition_BN : "+guaranteedAddition_BN);

            BIMAST.setMortalityChargesReductionYield_T(fundValueAtEnd_AP,
                    policyTerm, forBIArray, ageAtEntry, sumAssured,
                    prop.mortalityChargesReductionYield);
            mortalityCharges_T = Double.parseDouble(BIMAST
                    .getMortalityChargesReductionYield_T());
            // _mortalityCharges_T=mortalityCharges_T;
            // System.out.println("16.   MortalityCharges_T : "+mortalityCharges_T);

            BIMAST.setTotalChargesReductionYield_V(policyTerm);
            totalCharges_V = Double.parseDouble(BIMAST
                    .getTotalChargesReductionYield_V());
            // _totalCharges_V=totalCharges_V;
            // System.out.println("18.   TotalCharges_V : "+totalCharges_V);

            BIMAST.setTotalServiceTax_exclOfSTonAllocAndSurrReductionYield_W(
                    serviceTax,
                    prop.mortalityAndRiderChargesReductionYield,
                    prop.administrationAndSArelatedChargesReductionYield,
                    prop.guaranteeChargesReductionYield);
            totalServiceTax_exclOfSTonAllocAndSurr_W = Double
                    .parseDouble(BIMAST
                            .getTotalServiceTax_exclOfSTonAllocAndSurrReductionYield_W());
            // _totalServiceTax_exclOfSTonAllocAndSurr_W=totalServiceTax_exclOfSTonAllocAndSurr_W;
            // System.out.println("19.   TotalServiceTax_exclOfSTonAllocAndSurr_W : "+totalServiceTax_exclOfSTonAllocAndSurr_W);

            BIMAST.setFundUnderRGFafterChargesReductionYield_AB(
                    prop.guaranteeChargesReductionYield1, serviceTax);
            fundUnderRGFafterCharges_AB = Double.parseDouble(BIMAST
                    .getFundUnderRGFafterChargesReductionYield_AB());
            // _fundUnderRGFafterCharges_AB=fundUnderRGFafterCharges_AB;
            // System.out.println("24.   FundUnderRGFafterCharges_AB : "+fundUnderRGFafterCharges_AB);

            BIMAST.setFundsUnderMarketLinkedReturnAtStartAfterChargesReductionYield_AC(
                    prop.guaranteeChargesReductionYield, serviceTax);
            fundsUnderMarketLinkedReturnAtStartAfterCharges_AC = Double
                    .parseDouble(BIMAST
                            .getFundsUnderMarketLinkedReturnAtStartAfterChargesReductionYield_AC());
            // _fundsUnderMarketLinkedReturnAtStartAfterCharges_AC=fundsUnderMarketLinkedReturnAtStartAfterCharges_AC;
            // System.out.println("25.   FundsUnderMarketLinkedReturnAtStartAfterCharges_AC : "+fundsUnderMarketLinkedReturnAtStartAfterCharges_AC);

            BIMAST.setAdditionToFundUnderRGFReductionYield_AD(policyTerm,
                    prop.int1);
            additionToFundUnderRGF_AD = Double.parseDouble(BIMAST
                    .getAdditionToFundUnderRGFReductionYield_AD());
            // _additionToFundUnderRGF_AD=additionToFundUnderRGF_AD;
            // System.out.println("26.   AdditionToFundUnderRGF_AD : "+additionToFundUnderRGF_AD);

            BIMAST.setAdditionToFundsUnderMarketLinkedReturnReductionYield_AE(
                    policyTerm, prop.int1);
            additionToFundsUnderMarketLinkedReturn_AE = Double
                    .parseDouble(BIMAST
                            .getAdditionToFundsUnderMarketLinkedReturnReductionYield_AE());
            // _additionToFundsUnderMarketLinkedReturn_AE=additionToFundsUnderMarketLinkedReturn_AE;
            // System.out.println("27.   AdditionToFundsUnderMarketLinkedReturn_AE : "+additionToFundsUnderMarketLinkedReturn_AE);

            BIMAST.setAdditionToFundIfAnyReductionYield_AH(policyTerm);
            additionToFundIfAny_AH = Double.parseDouble(BIMAST
                    .getAdditionToFundIfAnyReductionYield_AH());
            // _additionToFundIfAny_AH=additionToFundIfAny_AH;
            // System.out.println("30.   AdditionToFundIfAny_AH: "+additionToFundIfAny_AH);

            BIMAST.setFMCearnedOnFundUnderRGFReductionYield_AF(policyTerm,
                    prop.charge_Fund, prop.charge_Fund_Ren);
            fMCearnedOnFundUnderRGF_AF = Double.parseDouble(BIMAST
                    .getFMCearnedOnFundUnderRGFReductionYield_AF());
            // _fMCearnedOnFundUnderRGF_AF=fMCearnedOnFundUnderRGF_AF;
            // System.out.println("28.   FMCearnedOnFundUnderRGF_AF : "+fMCearnedOnFundUnderRGF_AF);

            BIMAST.setFundUnderRGFatEndReductionYield_AN(
                    prop.fundManagementChargesReductionYield, serviceTax);
            fundUnderRGFatEnd_AN = Double.parseDouble(BIMAST
                    .getFundUnderRGFatEndReductionYield_AN());
            // _fundUnderRGFatEnd_AN=fundUnderRGFatEnd_AN;
            // System.out.println("36.   FundUnderRGFatEnd_AN: "+fundUnderRGFatEnd_AN);

            // modified by vrushali
            BIMAST.setFMCearnedOnFundsUnderMarketLinkedReturnReductionYield_AG(
                    policyTerm, prop.charge_Fund, prop.returnGuaranteeFund,
                    prop.bondFund, prop.equityFund, prop.PEmanagedFund,
                    smartWealthAssureBean.getPercentToBeInvested_BondFund(),
                    smartWealthAssureBean.getPercentToBeInvested_EquityFund(),
                    smartWealthAssureBean.getPercentToBeInvested_BalancedFund(),
                    smartWealthAssureBean.getPercentToBeInvested_BondOptimiserFund(),
                    smartWealthAssureBean.getPercentToBeInvested_MoneyMarketFund(),
                    smartWealthAssureBean.getPercentToBeInvested_CorpBondFund(),
                    smartWealthAssureBean.getPercentToBeInvested_PureFund(),
                    prop.FMC_BalancedFund, prop.FMC_BondOptimiserFund, prop.FMC_MoneyMarketFund, prop.FMC_CorpBondFund, prop.FMC_PureFund);
            fMCearnedOnFundsUnderMarketLinkedReturn_AG = Double
                    .parseDouble(BIMAST
                            .getFMCearnedOnFundsUnderMarketLinkedReturnReductionYield_AG());
            // _fMCearnedOnFundsUnderMarketLinkedReturn_AG=fMCearnedOnFundsUnderMarketLinkedReturn_AG;
            // System.out.println("29.   FMCearnedOnFundsUnderMarketLinkedReturn_AG : "+fMCearnedOnFundsUnderMarketLinkedReturn_AG);

            BIMAST.setfundsUnderMarketLinkedReturnAtEndReductionYield_AO(
                    prop.fundManagementChargesReductionYield, serviceTax);
            fundsUnderMarketLinkedReturnAtEnd_AO = Double.parseDouble(BIMAST
                    .getFundsUnderMarketLinkedReturnAtEndReductionYield_AO());
            // _fundsUnderMarketLinkedReturnAtEnd_AO=fundsUnderMarketLinkedReturnAtEnd_AO;
            // System.out.println("37.   FundsUnderMarketLinkedReturnAtEnd_AO: "+fundsUnderMarketLinkedReturnAtEnd_AO);

            BIMAST.setFundValueAtEndReductionYield_AP();
            fundValueAtEnd_AP = Double.parseDouble(BIMAST
                    .getFundValueAtEndReductionYield_AP());
            // _fundValueAtEnd_AP=fundValueAtEnd_AP;
            // System.out.println("38.   FundValueAtEnd_AP : "+fundValueAtEnd_AP);

            BIMAST.setFundBeforeFMCReductionYield_AI(policyTerm);
            fundBeforeFMC_AI = Double.parseDouble(BIMAST
                    .getFundBeforeFMCReductionYield_AI());
            // _fundBeforeFMC_AI=fundBeforeFMC_AI;
            // System.out.println("31.   FundBeforeFMC_AI : "+fundBeforeFMC_AI);

            BIMAST.setFundManagementChargeReductionYield_AJ();
            fundManagementCharge_AJ = Double.parseDouble(BIMAST
                    .getFundManagementChargeReductionYield_AJ());
            // _fundManagementCharge_AJ=fundManagementCharge_AJ;
            // System.out.println("32.   FundManagementCharge_AJ : "+fundManagementCharge_AJ);

            BIMAST.setServiceTaxOnFMCReductionYield_AK(
                    prop.fundManagementChargesReductionYield, serviceTax);
            serviceTaxOnFMC_AK = Double.parseDouble(BIMAST
                    .getServiceTaxOnFMCReductionYield_AK());
            // _serviceTaxOnFMC_AK=serviceTaxOnFMC_AK;
            // System.out.println("33.   ServiceTaxOnFMC_AK : "+serviceTaxOnFMC_AK);

            BIMAST.setTotalServiceTaxReductionYield_X();
            totalServiceTax_X = Double.parseDouble(BIMAST
                    .getTotalServiceTaxReductionYield_X());
            // _totalServiceTax_X=totalServiceTax_X;
            // System.out.println("20.   TotalServiceTax_X : "+totalServiceTax_X);

            BIMAST.setFundValueAfterFMCandBeforeGAReductionYield_AL(policyTerm);
            fundValueAfterFMCandBeforeGA_AL = Double.parseDouble(BIMAST
                    .getFundValueAfterFMCandBeforeGAReductionYield_AL());
            // _fundValueAfterFMCandBeforeGA_AL=fundValueAfterFMCandBeforeGA_AL;
            // System.out.println("34.   FundValueAfterFMCandBeforeGA_AL : "+fundValueAfterFMCandBeforeGA_AL);

            BIMAST.setSurrenderCap_BV(effectivePremium);
            surrenderCap_BV = Double.parseDouble(BIMAST.getSurrenderCap_BV());
            // _surrenderCap_BV=surrenderCap_BV;
            // System.out.println("70.   SurrenderCap_BV : "+surrenderCap_BV);

            BIMAST.setSurrenderChargesReductionYield_AQ(effectivePremium,
                    premFreqMode);
            surrenderCharges_AQ = Double.parseDouble(BIMAST
                    .getSurrenderChargesReductionYield_AQ());
            // _surrenderCharges_AQ=surrenderCharges_AQ;
            // System.out.println("39.   SurrenderCharges_AQ : "+surrenderCharges_AQ);

            BIMAST.setServiceTaxOnSurrenderChargesReductionYield_AR(
                    prop.surrenderChargesReductionYield, serviceTax);
            serviceTaxOnSurrenderCharges_AR = Double.parseDouble(BIMAST
                    .getServiceTaxOnSurrenderChargesReductionYield_AR());
            // _serviceTaxOnSurrenderCharges_AR=serviceTaxOnSurrenderCharges_AR;
            // System.out.println("40.   ServiceTaxOnSurrenderCharges_AR : "+serviceTaxOnSurrenderCharges_AR);

            BIMAST.setSurrenderValueReductionYield_AS();
            surrenderValue_AS = Double.parseDouble(BIMAST
                    .getSurrenderValueReductionYield_AS());
            // _surrenderValue_AS=surrenderValue_AS;
            // System.out.println("41.   SurrenderValue_AS: "+surrenderValue_AS);

            BIMAST.setDeathBenefitReductionYield_AT(policyTerm, sumAssured);
            deathBenefit_AT = Double.parseDouble(BIMAST
                    .getDeathBenefitReductionYield_AT());
            // _deathBenefit_AT=deathBenefit_AT;
            // System.out.println("42.   DeathBenefit_AT: "+deathBenefit_AT);

            BIMAST.setMortalityChargesReductionYield_AU(fundValueAtEnd_BQ,
                    policyTerm, forBIArray, ageAtEntry, sumAssured,
                    prop.mortalityCharges);
            mortalityCharges_AU = Double.parseDouble(BIMAST
                    .getMortalityChargesReductionYield_AU());
            // _mortalityCharges_AU=mortalityCharges_AU;
            // System.out.println("43.   MortalityCharges_AU : "+mortalityCharges_AU);

            BIMAST.setTransferOfFundFromRGFtoMarketLinkedFunds_AZ(
                    fundUnderRGFatEnd_BO,
                    prop.noOfYrsAfterWhichTransferTakePlace,
                    prop.charge_SumAssuredBase);
            transferOfFundFromRGFtoMarketLinkedFunds_AZ = Double
                    .parseDouble(BIMAST
                            .getTransferOfFundFromRGFtoMarketLinkedFunds_AZ());
            // _transferOfFundFromRGFtoMarketLinkedFunds_AZ=transferOfFundFromRGFtoMarketLinkedFunds_AZ;
            // System.out.println("48.   TransferOfFundFromRGFtoMarketLinkedFunds_AZ : "+transferOfFundFromRGFtoMarketLinkedFunds_AZ);

            BIMAST.setFundsUnderMarketLinkedReturnAtStart_BB(
                    fundsUnderMarketLinkedReturnAtEnd_BP, smartWealthAssureBean
                            .getPercentToBeInvested_GuaranteeFund());
            fundsUnderMarketLinkedReturnAtStart_BB = Double.parseDouble(BIMAST
                    .getFundsUnderMarketLinkedReturnAtStart_BB());
            // _fundsUnderMarketLinkedReturnAtStart_BB=fundsUnderMarketLinkedReturnAtStart_BB;
            // System.out.println("50.   FundsUnderMarketLinkedReturnAtStart_BB : "+fundsUnderMarketLinkedReturnAtStart_BB);

            BIMAST.setFundUnderRGFatStart_BA(fundUnderRGFatEnd_BO,
                    smartWealthAssureBean
                            .getPercentToBeInvested_GuaranteeFund());
            fundUnderRGFatStart_BA = Double.parseDouble(BIMAST
                    .getFundUnderRGFatStart_BA());
            // _fundUnderRGFatStart_BA=fundUnderRGFatStart_BA;
            // System.out.println("49.   FundUnderRGFatStart_BA : "+fundUnderRGFatStart_BA);

            BIMAST.setGuaranteeCharge_AV(prop.guaranteeCharges1,
                    prop.charge_Guarantee);
            guaranteeCharge_AV = Double.parseDouble(BIMAST
                    .getGuaranteeCharge_AV());
            // _guaranteeCharge_AV=guaranteeCharge_AV;
            // System.out.println("44.   GuaranteeCharge_AV : "+guaranteeCharge_AV);

            BIMAST.setTotalServiceTax_ExclOfSTonAllocAndSurrReductionYield_AX(
                    serviceTax, prop.mortalityAndRiderCharges,
                    prop.administrationAndSArelatedCharges,
                    prop.guaranteeCharges);
            totalServiceTax_ExclOfSTonAllocAndsurr_AX = Double
                    .parseDouble(BIMAST
                            .getTotalServiceTax_ExclOfSTonAllocAndSurrReductionYield_AX());
            // _totalServiceTax_ExclOfSTonAllocAndsurr_AX=totalServiceTax_ExclOfSTonAllocAndsurr_AX;
            // System.out.println("46.   TotalServiceTax_ExclOfSTonAllocAndsurr_AX: "+totalServiceTax_ExclOfSTonAllocAndsurr_AX);
            //
            BIMAST.setTotalChargesReductionYield_AW(policyTerm);
            totalCharges_AW = Double.parseDouble(BIMAST
                    .getTotalChargesReductionYield_AW());
            // _totalCharges_AW=totalCharges_AW;
            // System.out.println("45.   TotalCharges_AW: "+totalCharges_AW);

            BIMAST.setFundUnderRGFafterChargesReductionYield_BC(
                    prop.guaranteeCharges, serviceTax);
            fundUnderRGFafterCharges_BC = Double.parseDouble(BIMAST
                    .getFundUnderRGFafterChargesReductionYield_BC());
            // _fundUnderRGFafterCharges_BC=fundUnderRGFafterCharges_BC;
            // System.out.println("51.   FundUnderRGFafterCharges_BC : "+fundUnderRGFafterCharges_BC);

            BIMAST.setFundsUnderMarketLinkedReturnAtStartAfterChargesReductionYield_BD(
                    prop.guaranteeCharges, serviceTax);
            fundsUnderMarketLinkedReturnAtStartAfterCharges_BD = Double
                    .parseDouble(BIMAST
                            .getFundsUnderMarketLinkedReturnAtStartAfterChargesReductionYield_BD());
            // _fundsUnderMarketLinkedReturnAtStartAfterCharges_BD=fundsUnderMarketLinkedReturnAtStartAfterCharges_BD;
            // System.out.println("52.   FundsUnderMarketLinkedReturnAtStartAfterCharges_BD : "+fundsUnderMarketLinkedReturnAtStartAfterCharges_BD);

            BIMAST.setAdditionToFundUnderRGFReductionYield_BE(policyTerm,
                    prop.int2);
            additionToFundUnderRGF_BE = Double.parseDouble(BIMAST
                    .getAdditionToFundUnderRGFReductionYield_BE());
            // _additionToFundUnderRGF_BE=additionToFundUnderRGF_BE;
            // System.out.println("53.   AdditionToFundUnderRGF_BE : "+additionToFundUnderRGF_BE);

            BIMAST.setAdditionToFundsUnderMarketLinkedReturnReductionYield_BF(
                    policyTerm, prop.int2);
            additionToFundsUnderMarketLinkedReturn_BF = Double
                    .parseDouble(BIMAST
                            .getAdditionToFundsUnderMarketLinkedReturnReductionYield_BF());
            // _additionToFundsUnderMarketLinkedReturn_BF=additionToFundsUnderMarketLinkedReturn_BF;
            // System.out.println("54.   AdditionToFundsUnderMarketLinkedReturn_BF : "+additionToFundsUnderMarketLinkedReturn_BF);

            BIMAST.setAdditionToFundIfAnyReductionYield_BI(policyTerm);
            additionToFundIfAny_BI = Double.parseDouble(BIMAST
                    .getAdditionToFundIfAnyReductionYield_BI());
            // _additionToFundIfAny_BI=additionToFundIfAny_BI;
            // System.out.println("57.   AdditionToFundIfAny_BI: "+additionToFundIfAny_BI);

            BIMAST.setFMCearnedOnFundUnderRGFReductionYield_BG(policyTerm,
                    prop.charge_Fund, prop.charge_Fund_Ren);
            fMCearnedOnFundUnderRGF_BG = Double.parseDouble(BIMAST
                    .getFMCearnedOnFundUnderRGFReductionYield_BG());
            // _fMCearnedOnFundUnderRGF_BG=fMCearnedOnFundUnderRGF_BG;
            // System.out.println("55.   FMCearnedOnFundUnderRGF_BG : "+fMCearnedOnFundUnderRGF_BG);

            BIMAST.setFundUnderRGFatEndReductionYield_BO(
                    prop.fundManagementCharges, serviceTax);
            fundUnderRGFatEnd_BO = Double.parseDouble(BIMAST
                    .getFundUnderRGFatEndReductionYield_BO());
            // _fundUnderRGFatEnd_BO=fundUnderRGFatEnd_BO;
            // System.out.println("63.   FundUnderRGFatEnd_BO: "+fundUnderRGFatEnd_BO);

            BIMAST.setFMCearnedOnFundsUnderMarketLinkedReturnReductionYield_BH(
                    policyTerm, prop.charge_Fund, prop.returnGuaranteeFund,
                    prop.bondFund, prop.equityFund, prop.PEmanagedFund,
                    smartWealthAssureBean
                            .getPercentToBeInvested_GuaranteeFund(),
                    smartWealthAssureBean.getPercentToBeInvested_BondFund(),
                    smartWealthAssureBean.getPercentToBeInvested_EquityFund(),
                    smartWealthAssureBean
                            .getPercentToBeInvested_PEmanagedFund(),
                    smartWealthAssureBean.getPercentToBeInvested_BalancedFund(),
                    smartWealthAssureBean.getPercentToBeInvested_BondOptimiserFund(),
                    smartWealthAssureBean.getPercentToBeInvested_MoneyMarketFund(),
                    smartWealthAssureBean.getPercentToBeInvested_CorpBondFund(),
                    smartWealthAssureBean.getPercentToBeInvested_PureFund(),
                    prop.FMC_BalancedFund, prop.FMC_BondOptimiserFund, prop.FMC_MoneyMarketFund, prop.FMC_CorpBondFund, prop.FMC_PureFund);
            fMCearnedOnFundsUnderMarketLinkedReturn_BH = Double
                    .parseDouble(BIMAST
                            .getFMCearnedOnFundsUnderMarketLinkedReturnReductionYield_BH());
            // _fMCearnedOnFundsUnderMarketLinkedReturn_BH=fMCearnedOnFundsUnderMarketLinkedReturn_BH;
            // System.out.println("56.   FMCearnedOnFundsUnderMarketLinkedReturn_BH : "+fMCearnedOnFundsUnderMarketLinkedReturn_BH);

            BIMAST.setfundsUnderMarketLinkedReturnAtEndReductionYield_BP(
                    prop.fundManagementCharges, serviceTax);
            fundsUnderMarketLinkedReturnAtEnd_BP = Double.parseDouble(BIMAST
                    .getFundsUnderMarketLinkedReturnAtEndReductionYield_BP());
            // _fundsUnderMarketLinkedReturnAtEnd_BP=fundsUnderMarketLinkedReturnAtEnd_BP;
            // System.out.println("64.   FundsUnderMarketLinkedReturnAtEnd_BP: "+fundsUnderMarketLinkedReturnAtEnd_BP);

            BIMAST.setFundValueAtEndReductionYield_BQ();
            fundValueAtEnd_BQ = Double.parseDouble(BIMAST
                    .getFundValueAtEndReductionYield_BQ());
            // _fundValueAtEnd_BQ=fundValueAtEnd_BQ;
            // System.out.println("65.   FundValueAtEnd_BQ : "+fundValueAtEnd_BQ);

            BIMAST.setDeathBenefitReductionYield_BU(policyTerm, sumAssured);
            deathBenefit_BU = Double.parseDouble(BIMAST
                    .getDeathBenefitReductionYield_BU());
            // _deathBenefit_BU=deathBenefit_BU;
            // System.out.println("69.   DeathBenefit_BU: "+deathBenefit_BU);

            BIMAST.setSurrenderChargesReductionYield_BR(effectivePremium,
                    premFreqMode);
            surrenderCharges_BR = Double.parseDouble(BIMAST
                    .getSurrenderChargesReductionYield_BR());
            // _surrenderCharges_BR=surrenderCharges_BR;
            // System.out.println("66.   SurrenderCharges_BR : "+surrenderCharges_BR);

            BIMAST.setServiceTaxOnSurrenderChargesReductionYield_BS(
                    prop.surrenderCharges, serviceTax);
            serviceTaxOnSurrenderCharges_BS = Double.parseDouble(BIMAST
                    .getServiceTaxOnSurrenderChargesReductionYield_BS());
            // _serviceTaxOnSurrenderCharges_BS=serviceTaxOnSurrenderCharges_BS;
            // System.out.println("67.   ServiceTaxOnSurrenderCharges_BS : "+serviceTaxOnSurrenderCharges_BS);

            BIMAST.setSurrenderValueReductionYield_BT();
            surrenderValue_BT = Double.parseDouble(BIMAST
                    .getSurrenderValueReductionYield_BT());
            // _surrenderValue_BT=surrenderValue_BT;
            // System.out.println("68.   SurrenderValue_BT: "+surrenderValue_BT);

            BIMAST.setFundBeforeFMCReductionYield_BJ(policyTerm);
            fundBeforeFMC_BJ = Double.parseDouble(BIMAST
                    .getFundBeforeFMCReductionYield_BJ());
            // _fundBeforeFMC_BJ=fundBeforeFMC_BJ;
            // System.out.println("58.   FundBeforeFMC_BJ: "+fundBeforeFMC_BJ);

            BIMAST.setFundManagementChargeReductionYield_BK();
            fundManagementCharge_BK = Double.parseDouble(BIMAST
                    .getFundManagementChargeReductionYield_BK());
            // _fundManagementCharge_BK=fundManagementCharge_BK;
            // System.out.println("59.   FundManagementCharge_BK: "+fundManagementCharge_BK);

            BIMAST.setServiceTaxOnFMCReductionYield_BL(
                    prop.fundManagementCharges, serviceTax);
            serviceTaxOnFMC_BL = Double.parseDouble(BIMAST
                    .getServiceTaxOnFMCReductionYield_BL());
            // _serviceTaxOnFMC_BL=serviceTaxOnFMC_BL;
            // System.out.println("60.   ServiceTaxOnFMC_BL : "+serviceTaxOnFMC_BL);

            BIMAST.setTotalServiceTaxReductionYield_AY();
            totalServiceTax_AY = Double.parseDouble(BIMAST
                    .getTotalServiceTaxReductionYield_AY());
            // _totalServiceTax_AY=totalServiceTax_AY;
            // System.out.println("47.   TotalServiceTax_AY: "+totalServiceTax_AY);

            BIMAST.setFundValueAfterFMCandBeforeGAReductionYield_BM(policyTerm);
            fundValueAfterFMCandBeforeGA_BM = Double.parseDouble(BIMAST
                    .getFundValueAfterFMCandBeforeGAReductionYield_BM());
            // _fundValueAfterFMCandBeforeGA_BM=fundValueAfterFMCandBeforeGA_BM;
            // System.out.println("61.   FundValueAfterFMCandBeforeGA_BM: "+fundValueAfterFMCandBeforeGA_BM);

            // System.out.println("***********************************************************************************************************");

            monthNumber++;

            List_CC.add(commonForAllProd.roundUp_Level2(commonForAllProd
                    .getStringWithout_E(reductionYield_CC)));
            List_CD.add(commonForAllProd.roundUp_Level2(commonForAllProd
                    .getStringWithout_E(reductionYield_CD)));
            List_CI.add(commonForAllProd.roundUp_Level2(commonForAllProd
                    .getStringWithout_E(reductionYield_CI)));

        }

        double ans = BIMAST.irr(List_CC, 0.001);
        double ans1 = BIMAST.irr(List_CD, 0.001);
        double ans2 = BIMAST.irr(List_CI, 0.01);

//		System.out.println("irr_CC : " + ans + " & irr_CD : " + ans1);

        BIMAST.setIRRAnnual_CC(ans);
        irrAnnual_CC = Double.parseDouble(BIMAST.getIRRAnnual_CC());
        // _irrAnnual_CC=irrAnnual_CC;
//		System.out.println("irrAnnual_CC " + irrAnnual_CC);

        BIMAST.setIRRAnnual_CD(ans1);
        irrAnnual_CD = Double.parseDouble(BIMAST.getIRRAnnual_CD());
        // _irrAnnual_CD=irrAnnual_CD;
//		System.out.println("irrAnnual_CD " + irrAnnual_CD);

        BIMAST.setIRRAnnual_CI(ans2);
        irrAnnual_CI = Double.parseDouble(BIMAST.getIRRAnnual_CI());
        // _irrAnnual_CI=irrAnnual_CI;
//		System.out.println("irrAnnual_CI " + irrAnnual_CI);

        BIMAST.setReductionInYieldMaturityAt(prop.int2);
        reductionInYieldMaturityAt = Double.parseDouble(BIMAST
                .getReductionInYieldMaturityAt());
        // _reductionInYieldMaturityAt=reductionInYieldMaturityAt;
//		System.out.println("reductionInYieldMaturityAt "
//				+ reductionInYieldMaturityAt);

        BIMAST.setReductionInYieldNumberOfYearsElapsedSinceInception(prop.int2);
        reductionInYieldNumberOfYearsElapsedSinceInception = Double
                .parseDouble(BIMAST
                        .getReductionInYieldNumberOfYearsElapsedSinceInception());
        // _reductionInYieldNumberOfYearsElapsedSinceInception=reductionInYieldNumberOfYearsElapsedSinceInception;
//		System.out
//				.println("reductionInYieldNumberOfYearsElapsedSinceInception "
//						+ reductionInYieldNumberOfYearsElapsedSinceInception);

        BIMAST.setnetYieldAt4Percent();
        netYield4pa = Double.parseDouble(BIMAST.getnetYieldAt4Percent());

//		System.out.println("netYield4pa " + netYield4pa);

        BIMAST.setnetYieldAt8Percent();
        netYield8pa = Double.parseDouble(BIMAST.getnetYieldAt8Percent());
//		System.out.println("netYield8pa " + netYield8pa);

        // return null;

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

    private void getInput(SmartWealthAssureBean smartWealthAssureBean) {

        inputVal = new StringBuilder();
        // From GUI Input

        String LifeAssured_title = spnr_bi_smart_wealth_assure_life_assured_title
                .getSelectedItem().toString();
        String LifeAssured_firstName = edt_bi_smart_wealth_assure_life_assured_first_name
                .getText().toString();
        String LifeAssured_middleName = edt_bi_smart_wealth_assure_life_assured_middle_name
                .getText().toString();
        String LifeAssured_lastName = edt_bi_smart_wealth_assure_life_assured_last_name
                .getText().toString();
        String LifeAssured_DOB = btn_bi_smart_wealth_assure_life_assured_date
                .getText().toString();
        String LifeAssured_age = edt_bi_smart_wealth_assure_life_assured_age
                .getText().toString();
        String LifeAssured_gender = spnr_bi_smart_wealth_assure_selGender
                .getSelectedItem().toString();

        String proposer_title = "";
        String proposer_firstName = "";
        String proposer_middleName = "";
        String proposer_lastName = "";
        String proposer_DOB = "";
        String proposer_age = "";
        String proposer_gender = "";

        if (!spnr_bi_smart_wealth_assure_proposer_title.getSelectedItem()
                .toString().equals("")
                && !spnr_bi_smart_wealth_assure_proposer_title
                .getSelectedItem().toString().equals("Select Title")) {
            proposer_title = spnr_bi_smart_wealth_assure_proposer_title
                    .getSelectedItem().toString();
            if (proposer_title.equals("Mr."))
                proposer_gender = "Male";
            else
                proposer_gender = "Female";
        }
        if (!edt_bi_smart_wealth_assure_proposer_first_name.getText()
                .toString().equals(""))
            proposer_firstName = edt_bi_smart_wealth_assure_proposer_first_name
                    .getText().toString();

        if (!edt_bi_smart_wealth_assure_proposer_middle_name.getText()
                .toString().equals(""))
            proposer_middleName = edt_bi_smart_wealth_assure_proposer_middle_name
                    .getText().toString();
        if (!edt_bi_smart_wealth_assure_proposer_last_name.getText().toString()
                .equals(""))
            proposer_lastName = edt_bi_smart_wealth_assure_proposer_last_name
                    .getText().toString();

        if (!btn_bi_smart_wealth_assure_proposer_date.getText().toString()
                .equals("Select Date")) {
            Calendar present_date = Calendar.getInstance();
            int tDay = present_date.get(Calendar.DAY_OF_MONTH);
            int tMonth = present_date.get(Calendar.MONTH);
            int tYear = present_date.get(Calendar.YEAR);
            proposer_DOB = btn_bi_smart_wealth_assure_proposer_date.getText()
                    .toString();
            proposer_age = commonForAllProd.calculateMyAge(tYear, tMonth + 1,
                    tDay, getDate1(proposer_DOB)) + "";
        }

        boolean staffDisc = smartWealthAssureBean.getIsForStaffOrNot();
        boolean isADBRider = smartWealthAssureBean.getIsADBchecked();
        int policyTerm = smartWealthAssureBean.getPolicyTerm_Basic();
//        double SAMF = smartWealthAssureBean.getSAMF();
//        int ageAtEntry = smartWealthAssureBean.getAgeAtEntry();
//        double premiumAmount = smartWealthAssureBean.getPremiumAmount();
        boolean isJKresident = smartWealthAssureBean.isJkResident();

        inputVal.append("<?xml version='1.0' encoding='utf-8' ?><smartWealthAssureBean>");

        inputVal.append("<LifeAssured_title>").append(LifeAssured_title).append("</LifeAssured_title>");
        inputVal.append("<LifeAssured_firstName>").append(LifeAssured_firstName).append("</LifeAssured_firstName>");
        inputVal.append("<LifeAssured_middleName>").append(LifeAssured_middleName).append("</LifeAssured_middleName>");
        inputVal.append("<LifeAssured_lastName>").append(LifeAssured_lastName).append("</LifeAssured_lastName>");
        inputVal.append("<LifeAssured_DOB>").append(LifeAssured_DOB).append("</LifeAssured_DOB>");
        inputVal.append("<LifeAssured_age>").append(LifeAssured_age).append("</LifeAssured_age>");
        // inputVal.append("<gender>" + LifeAssured_gender + "</gender>");

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
        inputVal.append("<isJKResident>").append(isJKresident).append("</isJKResident>");
        inputVal.append("<age>").append(edt_bi_smart_wealth_assure_life_assured_age.getText()
                .toString()).append("</age>");
        inputVal.append("<gender>").append(spnr_bi_smart_wealth_assure_selGender.getSelectedItem()
                .toString()).append("</gender>");

        double SAMF = smartWealthAssureBean.getSAMF();
        inputVal.append("<SAMF>").append(SAMF).append("</SAMF>");
        int ageAtEntry = smartWealthAssureBean.getAgeAtEntry();

        inputVal.append("<premFreqMode>").append(spnr_bi_smart_wealth_assure_premium_frequency
                .getSelectedItem().toString()).append("</premFreqMode>");

        inputVal.append("<policyTerm>").append(policyTerm).append("</policyTerm>");

        double premiumAmount = smartWealthAssureBean.getPremiumAmount();
        inputVal.append("<premiumAmount>").append(premiumAmount).append("</premiumAmount>");

        inputVal.append("<noOfYrElapsed>").append(edt_smart_wealth_assure_noOfYearsElapsedSinceInception
                .getText().toString()).append("</noOfYrElapsed>");
        inputVal.append("<perInvEquityFund>").append(edt_smart_wealth_assure_percent_EquityFund.getText()
                .toString()).append("</perInvEquityFund>");

        inputVal.append("<perInvBondFund>").append(edt_smart_wealth_assure_percent_BondFund.getText().toString()).append("</perInvBondFund>");
        inputVal.append("<perInvBalancedFund>"
                + percent_BalancedFund.getText().toString()
                + "</perInvBalancedFund>");

        inputVal.append("<perInvMoneyMarketFund>"
                + percent_MoneyMktFund.getText().toString()
                + "</perInvMoneyMarketFund>");


        inputVal.append("<perInvBondOptimiserFund>"
                + percent_BondOptimiserFund.getText().toString()
                + "</perInvBondOptimiserFund>");

        inputVal.append("<perInvCorporateBondFund>"
                + percent_CorporateBondFund.getText().toString()
                + "</perInvCorporateBondFund>");


        inputVal.append("<perInvPureFund>"
                + percent_PureFund.getText().toString()
                + "</perInvPureFund>");
        inputVal.append("<isADBRider>").append(isADBRider).append("</isADBRider>");
        inputVal.append("<adbTerm>").append(spnr_bi_smart_wealth_assure_adb_rider_term.getSelectedItem()
                .toString()).append("</adbTerm>");
        inputVal.append("<adbSA>").append(edt_bi_smart_wealth_assure_adb_rider_sum_assured.getText()
                .toString()).append("</adbSA>");

        //Added Tushar Kadam Kerla Applicable Tax 7-Jun-2019
        String str_kerla_discount = "N";
        if (cb_kerladisc.isChecked()) {
            str_kerla_discount = "Y";
        }

        inputVal.append("<KFC>" + str_kerla_discount + "</KFC>");
        //End Added Tushar Kadam Kerla Applicable Tax 7-Jun-2019


        inputVal.append("</smartWealthAssureBean>");

    }


    private void Dialog() {

        final Dialog d = new Dialog(this);
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);
        d.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.BLACK));

        d.setContentView(R.layout.layout_smart_wealth_assure_bi_grid);

        input = inputVal.toString();
        output = retVal.toString();

        TextView tv_proposername = d
                .findViewById(R.id.tv_proposername);
        TextView tv_proposal_number = d
                .findViewById(R.id.tv_proposal_number);
        TextView tv_bi_smart_wealth_assure_age_entry = d
                .findViewById(R.id.tv_bi_smart_wealth_assure_age_entry);
        TextView tv_bi_smart_wealth_assure_maturity_age = d
                .findViewById(R.id.tv_bi_smart_wealth_assure_maturity_age);
        TextView tv_bi_smart_wealth_assure_life_assured_gender = d
                .findViewById(R.id.tv_bi_smart_wealth_assure_life_assured_gender);
        TextView tv_bi_smart_wealth_assure_policy_term = d
                .findViewById(R.id.tv_bi_smart_wealth_assure_policy_term);
        TextView tv_bi_smart_wealth_assure_annualised_premium = d
                .findViewById(R.id.tv_bi_smart_wealth_assure_annualised_premium);
        TextView tv_bi_smart_wealth_assure_sum_assured_main = d
                .findViewById(R.id.tv_bi_smart_wealth_assure_sum_assured_main);
        TextView tv_bi_smart_wealth_assure_mode = d
                .findViewById(R.id.tv_bi_smart_wealth_assure_mode);
        TextView tv_bi_smart_wealth_assure_premium_paying_term = d
                .findViewById(R.id.tv_bi_smart_wealth_assure_premium_paying_term);

        TextView tv_smart_wealth_assure_equity_fund_allocation = d
                .findViewById(R.id.tv_smart_wealth_assure_equity_fund_allocation);

        TextView tv_smart_wealth_assure_balanced_fund_allocation = d
                .findViewById(R.id.tv_smart_wealth_assure_balanced_fund_allocation);
        TextView tv_smart_wealth_assure_pure_fund_allocation = (TextView) d
                .findViewById(R.id.tv_smart_wealth_assure_pure_fund_allocation);
        TextView tv_smart_wealth_assure_corporate_fund = (TextView) d
                .findViewById(R.id.tv_smart_wealth_assure_corporate_fund);
        TextView tv_smart_wealth_assure_money_market_fund = (TextView) d
                .findViewById(R.id.tv_smart_wealth_assure_money_market_fund);
        TextView tv_smart_wealth_assure_bond_optimiser_fund_fmc = (TextView) d
                .findViewById(R.id.tv_smart_wealth_assure_bond_optimiser_fund_fmc);
        TextView tv_smart_wealth_assure_bond_fund_allocation = d
                .findViewById(R.id.tv_smart_wealth_assure_bond_fund_allocation);

        TextView tv_channel_intermediary = (TextView) d
                .findViewById(R.id.tv_channel_intermediary);
        tv_channel_intermediary.setText(userType);

        TextView tv_smart_wealth_assure_equity_fund_fmc = d
                .findViewById(R.id.tv_smart_wealth_assure_equity_fund_fmc);

        TextView tv_smart_wealth_assure_balanced_fund_fmc = d
                .findViewById(R.id.tv_smart_wealth_assure_balanced_fund_fmc);
        TextView tv_smart_wealth_assure_bond_fund_fmc = d
                .findViewById(R.id.tv_smart_wealth_assure_bond_fund_fmc);

        TextView tv_smart_wealth_assure_no_of_years_elapsed = d
                .findViewById(R.id.tv_smart_wealth_assure_no_of_years_elapsed);
        TextView tv_smart_wealth_assure_reduction_yield = d
                .findViewById(R.id.tv_smart_wealth_assure_reduction_yield);
        TextView tv_smart_wealth_assure_maturity_at = d
                .findViewById(R.id.tv_smart_wealth_assure_maturity_at);
        TextView tv_smart_wealth_assure_reduction_yeild2 = d
                .findViewById(R.id.tv_smart_wealth_assure_reduction_yeild2);
        final TextView edt_proposer_name = d
                .findViewById(R.id.edt_ProposalName);
        final EditText edt_Policyholderplace = d
                .findViewById(R.id.edt_Policyholderplace);

        TextView tv_smart_wealth_assure_death_benefit = d
                .findViewById(R.id.tv_smart_wealth_assure_death_benefit);

        TextView tv_bi_smart_wealth_assure_name_of_proposer = (TextView) d
                .findViewById(R.id.tv_bi_smart_wealth_assure_name_of_proposer);
        if (edt_bi_smart_wealth_assure_proposer_first_name.getText().toString().equalsIgnoreCase("")) {
            tv_bi_smart_wealth_assure_name_of_proposer.setText(name_of_life_assured);
        } else {
            tv_bi_smart_wealth_assure_name_of_proposer.setText(name_of_proposer);

        }

//        TextView tv_smart_wealth_assure_net_yield4 = d
//                .findViewById(R.id.tv_smart_wealth_assure_net_yield_4);

        TextView tv_smart_wealth_assure_net_yield8 = d
                .findViewById(R.id.tv_smart_wealth_assure_net_yield_8);

        TextView tv_bi_smart_wealth_assure_age_of_proposer = (TextView) d
                .findViewById(R.id.tv_bi_smart_wealth_assure_age_of_proposer);
        age_entry = prsObj.parseXmlTag(input, "age");

        if (ProposerAge.equalsIgnoreCase("")) {
            tv_bi_smart_wealth_assure_age_of_proposer.setText(age_entry);
        } else {
            tv_bi_smart_wealth_assure_age_of_proposer.setText(ProposerAge);
        }

        TableRow tr_submit_id_proof = (TableRow) d
                .findViewById(R.id.tr_submit_id_proof);
        if (Double.parseDouble(edt_bi_smart_wealth_assure_sum_assured_amount.getText().toString()) > 100000) {
            tr_submit_id_proof.setVisibility(View.VISIBLE);
        } else {
            tr_submit_id_proof.setVisibility(View.GONE);
        }
        TextView tv_bi_smart_insure_wealth_plus_age_name_of_life_assured = (TextView) d
                .findViewById(R.id.tv_bi_smart_insure_wealth_plus_age_name_of_life_assured);
        tv_bi_smart_insure_wealth_plus_age_name_of_life_assured.setText(name_of_life_assured);


        TextView tv_bi_is_Staff = d
                .findViewById(R.id.tv_bi_is_Staff);

        TextView tv_bi_is_jk = d.findViewById(R.id.tv_bi_is_jk);

        LinearLayout ll_basic_rider = d
                .findViewById(R.id.ll_basic_rider);

        LinearLayout ll_basic_adb_rider = d
                .findViewById(R.id.ll_basic_adb_rider);

        TextView tv_bi_smart_wealth_assure_premium_paying_term_adb = d
                .findViewById(R.id.tv_bi_smart_wealth_assure_premium_paying_term_adb);
        TextView tv_bi_smart_wealth_assure_sum_assured_adb = d
                .findViewById(R.id.tv_bi_smart_wealth_assure_sum_assured_adb);
        TextView tv_bi_smart_wealth_assure_yearly_premium_adb = d
                .findViewById(R.id.tv_bi_smart_wealth_assure_yearly_premium_adb);

        // final TextView tv_premium_type =
        // (TextView)d.findViewById(R.id.tv_premium_type);
        final EditText edt_MarketingOfficalPlace = d
                .findViewById(R.id.edt_MarketingOfficalPlace);

        GridView gv_userinfo = (GridView) d.findViewById(R.id.gv_userinfo);
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
        if (proposer_Is_Same_As_Life_Assured.equalsIgnoreCase("y")) {
            edt_proposer_name
                    .setText("I, "
                            + name_of_life_assured
                            + ", have understood the term and conditions, product features and applicable charges (if any) before purchase of the contract, after receipt of all information stated above from the insurer.");
            edt_proposer_name_need_analysis
                    .setText("I, "
                            + name_of_life_assured
                            + ", have undergone the Need Analysis  after having reviewed the SBI Life Product options, I have opted for SBI LIFE - Smart Wealth Assure.");

            // tv_life_assured_name.setText(name_of_life_assured);
            tv_proposername.setText(name_of_life_assured);
        } else if (proposer_Is_Same_As_Life_Assured.equalsIgnoreCase("n")) {
            edt_proposer_name
                    .setText("I, "
                            + name_of_proposer
                            + ", have understood the term and conditions, product features and applicable charges (if any) before purchase of the contract, after receipt of all information stated above from the insurer.");
            edt_proposer_name_need_analysis
                    .setText("I, "
                            + name_of_proposer
                            + ", have undergone the Need Analysis  after having reviewed the SBI Life Product options, I have opted for SBI LIFE - Smart Wealth Assure.");

            tv_proposername.setText(name_of_proposer);

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

        imageButtonSmartWealthAssureProposerPhotograph = d
                .findViewById(R.id.imageButtonSmartWealthAssureProposerPhotograph);
        imageButtonSmartWealthAssureProposerPhotograph
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
            imageButtonSmartWealthAssureProposerPhotograph
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
                            imageButtonSmartWealthAssureProposerPhotograph
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
                String isActive = "0";
                // TODO Auto-generated method stub
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
                    String productCode = "SSW0";

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
                            obj.getRound(obj.getStringWithout_E(Double
                                    .valueOf((sumAssured.equals("") || sum_assured == null) ? "0"
                                            : sumAssured))), premiumAmount,
                            agentEmail, agentMobile, na_input, na_output,
                            premPayingMode, Integer.parseInt(policyTerm), 0,
                            productCode, getDate(lifeAssured_date_of_birth),
                            "", "", mode, inputVal
                            .toString(), retVal.toString().replace(
                            bussIll.toString(), "")));

                    CreateSmartWealthAssureBIPdf();


                    NABIObj.serviceHit(BI_SmartWealthAssureActivity.this,
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
                        setFocusable(imageButtonSmartWealthAssureProposerPhotograph);
                        imageButtonSmartWealthAssureProposerPhotograph
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


        String staffdiscount = prsObj.parseXmlTag(input, "isStaff");

        policyTerm = prsObj.parseXmlTag(input, "policyTerm");

        premPayingMode = prsObj.parseXmlTag(input, "premFreqMode");
        if (staffdiscount.equalsIgnoreCase("true")) {
            tv_bi_is_Staff.setText("Yes");
        } else {
            tv_bi_is_Staff.setText("No");
        }

        String isJkResident = prsObj.parseXmlTag(input, "isJKResident");

        if (isJkResident.equalsIgnoreCase("true")) {
            tv_bi_is_jk.setText("Yes");
        } else {
            tv_bi_is_jk.setText("No");
        }

        adb_rider_status = prsObj.parseXmlTag(input, "isADBRider");

        if (adb_rider_status.equals("false")) {
            ll_basic_rider.setVisibility(View.GONE);

        } else {
            ll_basic_rider.setVisibility(View.VISIBLE);
            if (adb_rider_status.equals("true")) {
                ll_basic_adb_rider.setVisibility(View.VISIBLE);
                String term_adb = prsObj.parseXmlTag(input, "adbTerm");
                sa_adb = prsObj.parseXmlTag(input, "adbSA");

                tv_bi_smart_wealth_assure_premium_paying_term_adb
                        .setText(term_adb + " Years");

                tv_bi_smart_wealth_assure_sum_assured_adb.setText("Rs. "
                        + obj.getRound(obj.getStringWithout_E(Double
                        .valueOf(sa_adb.equals("") ? "0" : sa_adb))));

            } else {
                ll_basic_adb_rider.setVisibility(View.GONE);
            }
        }
        age_entry = prsObj.parseXmlTag(input, "age");
        tv_bi_smart_wealth_assure_age_entry.setText(age_entry + " Years");

        maturityAge = prsObj.parseXmlTag(output, "maturityAge");
        tv_bi_smart_wealth_assure_maturity_age.setText(maturityAge + " Years");
        premiumAmount = prsObj.parseXmlTag(input, "premiumAmount");

        premiumAmount = obj
                .getRound(obj.getStringWithout_E(Double.valueOf((premiumAmount
                        .equals("") || premiumAmount == null) ? "0"
                        : premiumAmount)));

        gender = prsObj.parseXmlTag(input, "gender");
        tv_bi_smart_wealth_assure_life_assured_gender.setText(gender);
        TextView tv_bi_smart_wealth_assure_premium_type = d
                .findViewById(R.id.tv_bi_smart_wealth_assure_premium_type);

        netYield4pa = prsObj.parseXmlTag(output, "netYield4Pa");
//        tv_smart_wealth_assure_net_yield4.setText(netYield4pa + " %");

        netYield8pa = prsObj.parseXmlTag(output, "netYield8Pa");
        tv_smart_wealth_assure_net_yield8.setText(netYield8pa + " %");

        annPrem = prsObj.parseXmlTag(output, "annPrem");

        tv_bi_smart_wealth_assure_annualised_premium.setText("Rs. "
                + obj.getRound(obj.getStringWithout_E(Double.valueOf((annPrem
                .equals("") || annPrem == null) ? "0" : annPrem))));

        sumAssured = prsObj.parseXmlTag(output, "sumAssured");
        tv_smart_wealth_assure_death_benefit.setText("Rs."
                + obj.getRound(obj.getStringWithout_E(Double.valueOf(sumAssured
                .equals("") ? "0" : sumAssured))));

        tv_bi_smart_wealth_assure_sum_assured_main.setText("Rs. "
                + getformatedThousandString(Integer.parseInt(obj.getRound(obj
                .getStringWithout_E(Double.valueOf(sumAssured
                        .equals("") ? "0" : sumAssured))))));

        policy_Frequency = prsObj.parseXmlTag(input, "premFreqMode");
        tv_bi_smart_wealth_assure_mode.setText(policy_Frequency);

        policyTerm = prsObj.parseXmlTag(input, "policyTerm");
        tv_smart_wealth_assure_maturity_at.setText(policyTerm + " Years");
        tv_bi_smart_wealth_assure_policy_term.setText(policyTerm + " Years");

        perInvEquityFund = (prsObj.parseXmlTag(input, "perInvEquityFund"));
        perInvBondFund = (prsObj.parseXmlTag(input, "perInvBondFund"));

        tv_smart_wealth_assure_equity_fund_allocation.setText((perInvEquityFund
                .equals("") ? "0" : perInvEquityFund) + " %");

        TextView tv_Company_policy_surrender_dec = d
                .findViewById(R.id.tv_Company_policy_surrender_dec);


        Company_policy_surrender_dec = "Your SBI LIFE - Smart Wealth Assure (111L077V03) is a "
                + policy_Frequency
                + " Premium Policy and you are required to pay premium once at the inception of the policy of Rs "
                + obj.getRound(obj.getStringWithout_E(Double.valueOf((annPrem
                .equals("") || annPrem == null) ? "0" : annPrem)))
                + " .Your Policy Term is "
                + policyTerm
                + " years, Premium Payment Term is Not Applicable "

                + " and Sum Assured is Rs. "
                +

                getformatedThousandString(Integer.parseInt(obj.getRound(obj
                        .getStringWithout_E(Double.valueOf(sumAssured
                                .equals("") ? "0" : sumAssured)))));

        tv_Company_policy_surrender_dec.setText(Company_policy_surrender_dec);
        perInvBalancedFund = (prsObj.parseXmlTag(input, "perInvBalancedFund"));

        tv_smart_wealth_assure_balanced_fund_allocation
                .setText((perInvBalancedFund.equals("") ? "0" : perInvBalancedFund) +
                        " %");

        perInvBondOptimiserFund = (prsObj.parseXmlTag(input, "perInvBondOptimiserFund"));
        tv_smart_wealth_assure_bond_optimiser_fund_fmc
                .setText((perInvBondOptimiserFund.equals("") ? "0" : perInvBondOptimiserFund) +
                        " %");
        perInvMoneyMarketFund = (prsObj.parseXmlTag(input, "perInvMoneyMarketFund"));
        tv_smart_wealth_assure_money_market_fund
                .setText((perInvMoneyMarketFund.equals("") ? "0" : perInvMoneyMarketFund) +
                        " %");
        perInvPureFund = (prsObj.parseXmlTag(input, "perInvPureFund"));
        tv_smart_wealth_assure_pure_fund_allocation
                .setText((perInvPureFund.equals("") ? "0" : perInvPureFund) +
                        " %");
        perInvCorporateBondFund = (prsObj.parseXmlTag(input, "perInvCorporateBondFund"));
        tv_smart_wealth_assure_corporate_fund
                .setText((perInvCorporateBondFund.equals("") ? "0" : perInvCorporateBondFund) +
                        " %");

        tv_smart_wealth_assure_bond_fund_allocation.setText((perInvBondFund
                .equals("") ? "0" : perInvBondFund) + " %");

        noOfYrElapsed = prsObj.parseXmlTag(input, "noOfYrElapsed");
        tv_smart_wealth_assure_no_of_years_elapsed.setText(noOfYrElapsed
                + " Years");

        tv_smart_wealth_assure_equity_fund_fmc.setText("1.35 %");
        // tv_smart_wealth_assure_balanced_fund_fmc.setText("1.25 %");
        tv_smart_wealth_assure_bond_fund_fmc.setText("1.00 %");

        redInYieldNoYr = prsObj.parseXmlTag(output, "redInYieldNoYr");
        tv_smart_wealth_assure_reduction_yield.setText(redInYieldNoYr + " %");

        redInYieldMat = prsObj.parseXmlTag(output, "redInYieldMat");
        tv_smart_wealth_assure_reduction_yeild2.setText(redInYieldMat + " %");

        for (int i = 1; i <= Integer.parseInt(policyTerm); i++) {
            String policy_year = prsObj
                    .parseXmlTag(output, "policyYr" + i + "");
            String premium = prsObj.parseXmlTag(output, "AnnPrem" + i + "");
            String mortality_charge1 = prsObj.parseXmlTag(output, "MortChrg4Pr" + i + "");
            String OtherCharges4Pr_PartA = prsObj.parseXmlTag(output, "OtherCharges4Pr_PartA" + i + "");
            String OtherCharges4Pr_PartB = prsObj.parseXmlTag(output, "OtherCharges4Pr_PartB" + i + "");
            String service_tax_on_mortality_charge1 = prsObj.parseXmlTag(output, "TotServTax4Pr" + i + "");
            String fund_value_at_end1 = prsObj.parseXmlTag(output, "FundValAtEnd4Pr" + i + "");
            String surrender_value1 = prsObj.parseXmlTag(output, "SurrVal4Pr" + i + "");
            String death_benefit1 = prsObj.parseXmlTag(output, "DeathBen4Pr" + i + "");
            String mortality_charge2 = prsObj.parseXmlTag(output, "MortChrg8Pr" + i + "");
            String OtherCharges8Pr_PartA = prsObj.parseXmlTag(output, "OtherCharges8Pr_PartA" + i + "");
            String OtherCharges8Pr_PartB = prsObj.parseXmlTag(output, "OtherCharges8Pr_PartB" + i + "");
            String service_tax_on_mortality_charge2 = prsObj.parseXmlTag(output, "TotServTax8Pr" + i + "");
            String fund_value_at_end2 = prsObj.parseXmlTag(output, "FundValAtEnd8Pr" + i + "");
            String surrender_value2 = prsObj.parseXmlTag(output, "SurrVal8Pr" + i + "");
            String death_benefit2 = prsObj.parseXmlTag(output, "DeathBen8Pr" + i + "");
            String premium_allocation_charge = prsObj.parseXmlTag(output, "PremAllCharge" + i + "");
            String annulise_premium_allocation_charge = prsObj.parseXmlTag(output, "AmtAviFrInv" + i + "");
            String policy_administration_charge = prsObj.parseXmlTag(output, "PolAdminChrg" + i + "");
            String str_AddToTheFund8Pr = prsObj.parseXmlTag(output, "addtoFundifAny8Pr" + i + "");
            String fund_before_fmc2 = prsObj.parseXmlTag(output, "FundBefFMC8Pr" + i + "");
            String fund_management_charge2 = prsObj.parseXmlTag(output, "FundMgmtCharg8Pr" + i + "");
            String guranteed_addition2 = prsObj.parseXmlTag(output, "optionChrg" + i + "");
            String commission = prsObj.parseXmlTag(output, "CommIfPay8Pr" + i + "");
            String str_AddToTheFund4Pr = prsObj.parseXmlTag(output, "addtoFundifAny4Pr" + i + "");
            String fund_before_fmc1 = prsObj.parseXmlTag(output, "FundBefFMC4Pr" + i + "");
            String fund_management_charge1 = prsObj.parseXmlTag(output, "FundMgmtCharg4Pr" + i + "");
            String guranteed_addition1 = prsObj.parseXmlTag(output, "optionChrg" + i + "");

            list_data.add(new M_BI_SmartWealthAssureAdapterCommon(policy_year, premium, mortality_charge1, OtherCharges4Pr_PartA,
                    service_tax_on_mortality_charge1, fund_value_at_end1, surrender_value1, death_benefit1,
                    mortality_charge2, OtherCharges8Pr_PartA, service_tax_on_mortality_charge2, fund_value_at_end2,
                    surrender_value2, death_benefit2, commission));
            list_data1.add(new M_BI_SmartWealthAssureAdapterCommon2(policy_year, premium, premium_allocation_charge,
                    annulise_premium_allocation_charge, mortality_charge2, service_tax_on_mortality_charge2, policy_administration_charge,
                    guranteed_addition2, OtherCharges8Pr_PartB, str_AddToTheFund8Pr, fund_before_fmc2, fund_management_charge2,
                    fund_value_at_end2, surrender_value2, death_benefit2));
            list_data2.add(new M_BI_SmartWealthAssureAdapterCommon2(policy_year, premium, premium_allocation_charge,
                    annulise_premium_allocation_charge, mortality_charge1, service_tax_on_mortality_charge1, policy_administration_charge,
                    guranteed_addition1, OtherCharges4Pr_PartB, str_AddToTheFund4Pr, fund_before_fmc1, fund_management_charge1,
                    fund_value_at_end1, surrender_value1, death_benefit1));


                /*list_data.add(new M_BI_SmartWealthAssureAdapterCommon(policy_year, premium, mortality_charge1, OtherCharges4Pr_PartA,
                        service_tax_on_mortality_charge1, fund_value_at_end1, surrender_value1, death_benefit1,
                        mortality_charge2, OtherCharges8Pr_PartA, service_tax_on_mortality_charge2, fund_value_at_end2,
                        surrender_value2, death_benefit2, commission));


            // String riderchrg4pr = prsObj.parseXmlTag(output,
            // "riderchrg4pr" + i + "");
            // String riderchrg8pr = prsObj.parseXmlTag(output,
            // "riderchrg8pr" + i + "");

            String addition_to_fund1 = prsObj.parseXmlTag(output,
                    "addtoFundifAny4Pr" + i + "");

               /* list_data.add(new M_BI_SmartWealthAssureAdapterCommon("", "", "", "", "", "", "", "", "", "", "", "", "", "", "" ));
                list_data1.add(new M_BI_SmartWealthAssureAdapterCommon2("", "", "", "", "", "", "", "", "", "", "", "", "", "", "" ));
                list_data2.add(new M_BI_SmartWealthAssureAdapterCommon2("", "", "", "", "", "", "", "", "", "", "", "", "", "", "" ));
*/
        }

        // String surrender_charge1 = prsObj.parseXmlTag(output,
        // "SurrVal4Pr"
        // + i + "");
        // String surrender_charge2 = prsObj.parseXmlTag(output,
        // "SurrenderChrg8Pr"
        // + i + "");

        policyTerm = policyTerm.replaceAll("\\s+", "");
        Adapter_BI_SmartWealthAssureGridCommon adapter = new Adapter_BI_SmartWealthAssureGridCommon(
                BI_SmartWealthAssureActivity.this, list_data);
        gv_userinfo.setAdapter(adapter);
        Adapter_BI_SmartWealthAssureGridCommon2 adapter1 = new Adapter_BI_SmartWealthAssureGridCommon2(
                BI_SmartWealthAssureActivity.this, list_data1);
        gv_userinfo2.setAdapter(adapter1);

        Adapter_BI_SmartWealthAssureGridCommon2 adapter2 = new Adapter_BI_SmartWealthAssureGridCommon2(
                BI_SmartWealthAssureActivity.this, list_data2);
        gv_userinfo3.setAdapter(adapter2);


        GridHeight gh = new GridHeight();
        gh.getheight(gv_userinfo, policyTerm);
        gh.getheight(gv_userinfo2, policyTerm);
        gh.getheight(gv_userinfo3, policyTerm);


        d.show();

    }

    private void CreateSmartWealthAssureBIPdf() {

        // String quotation_Number = ProductHomePageActivity.quotation_Number;
        try {
            Font small_bold3 = new Font(Font.FontFamily.TIMES_ROMAN, 5,
                    Font.BOLD);
            Font redFont = new Font(Font.FontFamily.TIMES_ROMAN, 12,
                    Font.NORMAL, BaseColor.RED);
            Font sub_headerBold = new Font(Font.FontFamily.TIMES_ROMAN, 11,
                    Font.BOLD);

            Font headerBold = new Font(Font.FontFamily.TIMES_ROMAN, 11,
                    Font.BOLD, BaseColor.WHITE);
            Font small_bold = new Font(Font.FontFamily.TIMES_ROMAN, 5,
                    Font.BOLD);

            Font small_bold1 = new Font(Font.FontFamily.TIMES_ROMAN, 8,
                    Font.BOLD);

            Font small_bold2 = new Font(Font.FontFamily.TIMES_ROMAN, 4,
                    Font.BOLD);
            Font small_normal = new Font(Font.FontFamily.TIMES_ROMAN, 6,
                    Font.NORMAL);
            Font small_bold_for_name = new Font(Font.FontFamily.TIMES_ROMAN,
                    10, Font.BOLD);
            // File mypath = new File(folder, PropserNumber +
            // "Proposalno_p02.pdf");
            mypath = mStorageUtils.createFileToAppSpecificDir(context, QuatationNumber + "BI.pdf");
            needAnalysispath = mStorageUtils.createFileToAppSpecificDir(context, "NA.pdf");
            // needAnalysis_Tabular_path = new File(folder, "NA_Tabular.pdf");
            newFile = mStorageUtils.createFileToAppSpecificDir(context, QuatationNumber + "P01.pdf");

            Rectangle rect = new Rectangle(594f, 792f);

            /*Document document = new Document(PageSize.LETTER.rotate(), 20, 20,
                    20, 20);*/
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
                    "SBI Life Insurance Co. Ltd ",
                    small_bold_for_name);
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
                    "SBI Life - Smart Wealth Assure (111L077V03)",
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

            document.add(para_img_logo_after_space_1);
            if (proposer_Is_Same_As_Life_Assured.equalsIgnoreCase("Y")) {
                name_of_proposer = name_of_life_assured;
            }
            PdfPTable table_proposer_name = new PdfPTable(4);
            // float[] columnWidths_table_proposer_name = { 2f, 2f, 1f, 2f };
            // table_proposer_name.setWidths(columnWidths_table_proposer_name);
            table_proposer_name.setWidthPercentage(100);

            PdfPCell ProposalNumber_cell_1 = new PdfPCell(new Paragraph(
                    "Proposal No.:", small_normal));
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

            table_proposer_name.addCell(ProposalNumber_cell_1);
            table_proposer_name.addCell(ProposalNumber_cell_2);
            table_proposer_name.addCell(ProposalNumber_cell_5);
            table_proposer_name.addCell(ProposalNumber_cell_6);
            // table_proposer_name.addCell(NameofProposal_cell_3);
            //table_proposer_name.addCell(NameofProposal_cell_4);
            document.add(table_proposer_name);
            document.add(para_img_logo_after_space_1);

            PdfPTable BI_Pdftable_Introdcution = new PdfPTable(1);
            BI_Pdftable_Introdcution.setWidthPercentage(100);
            PdfPCell BI_Pdftable_Introdcutioncell = new PdfPCell(new Paragraph(
                    "Introduction", small_bold));

            BI_Pdftable_Introdcutioncell
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_Pdftable_Introdcutioncell.setPadding(5);

            BI_Pdftable_Introdcution.addCell(BI_Pdftable_Introdcutioncell);
            //document.add(BI_Pdftable_Introdcution);

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
            // CR_table6.setWidths(columnWidths_3);
            PdfPCell BI_Pdftable3_cell1 = new PdfPCell(
                    new Paragraph(
                            "The main objective of the illustration is that the client is able to appreciate the features of the product and the flow of benefits in different circumstances with some level of quantification. For further information on the product, its benefits and applicable charges please refer to the sales brochure and/or policy document. Further information will also be available on request.",
                            small_normal));

            BI_Pdftable3_cell1.setPadding(5);

            BI_Pdftable3.addCell(BI_Pdftable3_cell1);
            document.add(BI_Pdftable3);

            PdfPTable BI_Pdftable4 = new PdfPTable(1);
            BI_Pdftable4.setWidthPercentage(100);
            // CR_table6.setWidths(columnWidths_4);
            PdfPCell BI_Pdftable4_cell1 = new PdfPCell(
                    new Paragraph(
                            "Some benefits are guaranteed and some benefits are variable with returns based on the future fund performance of SBI Life Insurance Company Limited. If your policy offers guaranteed returns then the same will be clearly marked as “guaranteed” in the illustration table. If your policy offers variable returns then the illustration will show two different rates of assumed future investment returns. These assumed rates of return are not guaranteed and they are not the upper or lower limits of what you might get back, as the value of your policy is dependent on a number of factors including future fund investment performance.",
                            small_normal));

            BI_Pdftable4_cell1.setPadding(5);

            BI_Pdftable4.addCell(BI_Pdftable4_cell1);
            document.add(BI_Pdftable4);
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

            PdfPTable table_lifeAssuredName = new PdfPTable(4);
            table_lifeAssuredName.setWidthPercentage(100);

            PdfPCell name_of_proposer1 = new PdfPCell(new Paragraph(
                    "Name of Proposer", small_normal));
            name_of_proposer1.setPadding(5);
            PdfPCell name_of_proposer2;
            if (edt_bi_smart_wealth_assure_proposer_first_name.getText().toString().equalsIgnoreCase("")) {
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
            age_entry = prsObj.parseXmlTag(input, "age");

            PdfPCell Age_of_prop2;
            if (ProposerAge.equalsIgnoreCase("")) {
                Age_of_prop2 = new PdfPCell(new Paragraph(age_entry, small_bold));
                Age_of_prop2.setPadding(5);
                Age_of_prop2.setHorizontalAlignment(Element.ALIGN_CENTER);
            } else {
                Age_of_prop2 = new PdfPCell(new Paragraph(ProposerAge, small_bold));
                Age_of_prop2.setPadding(5);
                Age_of_prop2.setHorizontalAlignment(Element.ALIGN_CENTER);

            }

            PdfPCell cell_LifeAssuredName1 = new PdfPCell(new Paragraph(
                    "Name of the Life Assured", small_normal));
            cell_LifeAssuredName1.setPadding(5);
            PdfPCell cell_lLifeAssuredName2 = new PdfPCell(new Paragraph(
                    name_of_life_assured, small_bold));
            cell_lLifeAssuredName2.setPadding(5);
            cell_lLifeAssuredName2.setHorizontalAlignment(Element.ALIGN_CENTER);

            PdfPCell cell_lifeAssuredAge1 = new PdfPCell(new Paragraph(
                    "Age of the Life Assured", small_normal));
            cell_lifeAssuredAge1.setPadding(5);
            PdfPCell cell_lifeAssuredAge2 = new PdfPCell(new Paragraph(
                    age_entry + "", small_bold));
            cell_lifeAssuredAge2.setPadding(5);
            cell_lifeAssuredAge2.setHorizontalAlignment(Element.ALIGN_CENTER);

            PdfPCell cell_termsofPolicy1 = new PdfPCell(new Paragraph(
                    "Policy Term " + "  ", small_normal));
            cell_termsofPolicy1.setPadding(5);
            PdfPCell cell_termsofPolicy2 = new PdfPCell(new Paragraph("  "
                    + policyTerm + "", small_bold));
            cell_termsofPolicy2.setPadding(5);
            cell_termsofPolicy2.setHorizontalAlignment(Element.ALIGN_CENTER);

            PdfPCell premium_payment_term1 = new PdfPCell(new Paragraph(
                    "Premium Payment Term ", small_normal));
            premium_payment_term1.setPadding(5);
            PdfPCell premium_payment_term2 = new PdfPCell(new Paragraph(" One time at the inception of the policy ", small_bold));
            premium_payment_term2.setPadding(5);
            premium_payment_term2
                    .setHorizontalAlignment(Element.ALIGN_CENTER);

            PdfPCell cell_monthly_prem1 = new PdfPCell(new Paragraph("Amount of Installment Premium ", small_normal));
            cell_monthly_prem1.setPadding(5);
            PdfPCell cell_monthly_prem2 = new PdfPCell(new Paragraph("" + currencyFormat.format(Double.parseDouble(annPrem)), small_bold));
            cell_monthly_prem2.setPadding(5);
            cell_monthly_prem2.setHorizontalAlignment(Element.ALIGN_CENTER);

            PdfPCell mode1 = new PdfPCell(new Paragraph("Mode / Frequency of Premium Payment ", small_normal));
            mode1.setPadding(5);
            PdfPCell mode2 = new PdfPCell(new Paragraph("One time at the inception of the policy", small_bold));
            mode2.setPadding(5);
            mode2.setHorizontalAlignment(Element.ALIGN_CENTER);

            table_lifeAssuredName.addCell(name_of_proposer1);
            table_lifeAssuredName.addCell(name_of_proposer2);
            table_lifeAssuredName.addCell(Age_of_prop1);
            table_lifeAssuredName.addCell(Age_of_prop2);
            table_lifeAssuredName.addCell(cell_LifeAssuredName1);
            table_lifeAssuredName.addCell(cell_lLifeAssuredName2);
            table_lifeAssuredName.addCell(cell_lifeAssuredAge1);
            table_lifeAssuredName.addCell(cell_lifeAssuredAge2);
            table_lifeAssuredName.addCell(cell_termsofPolicy1);
            table_lifeAssuredName.addCell(cell_termsofPolicy2);
            table_lifeAssuredName.addCell(premium_payment_term1);
            table_lifeAssuredName.addCell(premium_payment_term2);
            table_lifeAssuredName.addCell(cell_monthly_prem1);
            table_lifeAssuredName.addCell(cell_monthly_prem2);
            table_lifeAssuredName.addCell(mode1);
            table_lifeAssuredName.addCell(mode2);
            document.add(table_lifeAssuredName);

            PdfPTable table_lifeAssuredDetails = new PdfPTable(2);
            table_lifeAssuredDetails.setWidthPercentage(100);

            PdfPCell sumAssured1 = new PdfPCell(
                    new Paragraph("Sum Assured", small_normal));
            sumAssured1.setPadding(5);
            PdfPCell sumAssured2 = new PdfPCell(
                    new Paragraph(currencyFormat.format(Double.parseDouble(sumAssured)), small_bold));
            sumAssured2.setPadding(5);
            sumAssured2
                    .setHorizontalAlignment(Element.ALIGN_CENTER);


            PdfPCell rate = new PdfPCell(new Paragraph(
                    "Rate of Applicable Taxes", small_bold));

            PdfPCell rate2 = new PdfPCell(new Paragraph(
                    "18%", small_bold));
            rate2.setHorizontalAlignment(Element.ALIGN_CENTER);

            PdfPCell cell_lifeAssuredAmaturityGender1 = new PdfPCell(
                    new Paragraph("Life Assured Gender", small_normal));
            cell_lifeAssuredAmaturityGender1.setPadding(5);
            PdfPCell cell_lifeAssuredAmaturityGender2 = new PdfPCell(
                    new Paragraph(gender, small_bold));
            cell_lifeAssuredAmaturityGender2.setPadding(5);
            cell_lifeAssuredAmaturityGender2
                    .setHorizontalAlignment(Element.ALIGN_CENTER);

            PdfPCell cell_lifeAssured_Premium_frequeny1 = new PdfPCell(
                    new Paragraph("Premium Payment Frequency", small_normal));
            cell_lifeAssured_Premium_frequeny1.setPadding(5);
            PdfPCell cell_lifeAssured_Premium_frequeny2 = new PdfPCell(
                    new Paragraph(policy_Frequency, small_bold));
            cell_lifeAssured_Premium_frequeny2.setPadding(5);
            cell_lifeAssured_Premium_frequeny2
                    .setHorizontalAlignment(Element.ALIGN_CENTER);

            table_lifeAssuredDetails.addCell(sumAssured1);
            table_lifeAssuredDetails.addCell(sumAssured2);
            table_lifeAssuredDetails.addCell(rate);
            table_lifeAssuredDetails.addCell(rate2);
            //table_lifeAssuredDetails.addCell(cell_lifeAssuredAmaturityGender1);
            //table_lifeAssuredDetails.addCell(cell_lifeAssuredAmaturityGender2);
            // table_lifeAssuredDetails
            //        .addCell(cell_lifeAssured_Premium_frequeny1);
            // table_lifeAssuredDetails
            //         .addCell(cell_lifeAssured_Premium_frequeny2);
            document.add(table_lifeAssuredDetails);

            String isStaff = "";
            if (cb_staffdisc.isChecked()) {
                isStaff = "yes";

                PdfPTable table_staff_NonStaff = new PdfPTable(2);
                table_staff_NonStaff.setWidthPercentage(100);

                PdfPCell cell_staff_NonStaff1 = new PdfPCell(new Paragraph(
                        "Staff Discount", small_normal));
                cell_staff_NonStaff1.setPadding(5);

                PdfPCell cell_staff_NonStaff2 = new PdfPCell(new Paragraph(
                        isStaff, small_bold));
                cell_staff_NonStaff2.setPadding(5);
                cell_staff_NonStaff2
                        .setHorizontalAlignment(Element.ALIGN_CENTER);

                table_staff_NonStaff.addCell(cell_staff_NonStaff1);
                table_staff_NonStaff.addCell(cell_staff_NonStaff2);
                document.add(table_staff_NonStaff);
            }

            String isJK = "";
            if (cb_bi_smart_wealth_assure_JKResident.isChecked()) {
                isJK = "yes";

                PdfPTable table_is_JK = new PdfPTable(2);
                table_is_JK.setWidthPercentage(100);

                PdfPCell cell_is_JK1 = new PdfPCell(new Paragraph("J&K",
                        small_normal));
                cell_is_JK1.setPadding(5);

                PdfPCell cell_is_JK2 = new PdfPCell(new Paragraph(isJK,
                        small_bold));
                cell_is_JK2.setPadding(5);
                cell_is_JK2.setHorizontalAlignment(Element.ALIGN_CENTER);

                table_is_JK.addCell(cell_is_JK1);
                table_is_JK.addCell(cell_is_JK2);
                document.add(table_is_JK);
            }

            document.add(para_img_logo_after_space_1);

            // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            PdfPCell cell;

            PdfPTable input_table = new PdfPTable(2);
            input_table.setWidths(new float[]{3f, 2f});
            input_table.setWidthPercentage(80f);
            input_table.setHorizontalAlignment(Element.ALIGN_LEFT);

            cell = new PdfPCell(new Phrase("Plan Details : ", small_bold));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            input_table.addCell(cell);
            cell = new PdfPCell(new Phrase("", small_bold));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            input_table.addCell(cell);

            // 1st row
            cell = new PdfPCell(new Phrase("Life Assured Age at Entry : ",
                    small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            input_table.addCell(cell);

            cell = new PdfPCell(new Phrase(age_entry + " Years", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            input_table.addCell(cell);

            // 2 row
            cell = new PdfPCell(new Phrase("Maturity Age : ", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            input_table.addCell(cell);

            cell = new PdfPCell(
                    new Phrase(maturityAge + " Years", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            input_table.addCell(cell);

            // 3 row
            cell = new PdfPCell(new Phrase("Life Assured Gender : ",
                    small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            input_table.addCell(cell);

            cell = new PdfPCell(new Phrase(gender, small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            input_table.addCell(cell);

            // 4 row
            cell = new PdfPCell(new Phrase("Policy Term ", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            input_table.addCell(cell);

            cell = new PdfPCell(new Phrase(policyTerm + " Years", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            input_table.addCell(cell);

            // 5 row
            cell = new PdfPCell(new Phrase("Single Premium* : ", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            input_table.addCell(cell);

            cell = new PdfPCell(new Phrase("Rs. "
                    + currencyFormat.format(Double.parseDouble(annPrem)),
                    small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            input_table.addCell(cell);

            // 6 row
            cell = new PdfPCell(new Phrase("Sum Assured : ", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            input_table.addCell(cell);

            cell = new PdfPCell(new Phrase("Rs. "
                    + currencyFormat.format(Double.parseDouble(sumAssured)),
                    small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            input_table.addCell(cell);

            // 6 row
            cell = new PdfPCell(new Phrase("Premium Paying Term : ",
                    small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            input_table.addCell(cell);

            cell = new PdfPCell(new Phrase("Single", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            input_table.addCell(cell);

            if (adb_rider_status.equalsIgnoreCase("true")) {

                // 6 row
                cell = new PdfPCell(
                        new Phrase("Accidental Death Benefit Sum Assured : ",
                                small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                input_table.addCell(cell);

                cell = new PdfPCell(new Phrase("Rs. "
                        + currencyFormat.format(Double.parseDouble(sa_adb)),
                        small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                input_table.addCell(cell);

            }

            // fund table here -3

            PdfPTable fund_table = new PdfPTable(4);
            fund_table.setWidths(new float[]{5f, 3f, 3f, 4f});
            fund_table.setWidthPercentage(80f);
            fund_table.setHorizontalAlignment(Element.ALIGN_CENTER);

            // 1st row
            cell = new PdfPCell(new Phrase(
                    "Investment Strategy Opted For", small_bold));
            cell.setColspan(2);

            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            fund_table.addCell(cell);
            cell = new PdfPCell(new Phrase(
                    "NA", small_bold));
            cell.setColspan(2);

            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            fund_table.addCell(cell);


            // 2 row
            cell = new PdfPCell(new Phrase("Fund Name (SFIN Name)", small_normal));

            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            fund_table.addCell(cell);

            cell = new PdfPCell(new Phrase("% Allocation)", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            fund_table.addCell(cell);

            cell = new PdfPCell(new Phrase("FMC", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            fund_table.addCell(cell);

            cell = new PdfPCell(new Phrase("Risk Level", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            fund_table.addCell(cell);
            // 3 row
            cell = new PdfPCell(new Phrase(
                    "Equity Fund (SFIN:ULIF001100105EQUITY-FND111)",
                    small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            fund_table.addCell(cell);

            String perInvEquityFund_percent = perInvEquityFund.equals("") ? "0"
                    : perInvEquityFund;

            cell = new PdfPCell(new Phrase(perInvEquityFund_percent + "%",
                    small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            fund_table.addCell(cell);

            cell = new PdfPCell(new Phrase("1.35%", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            fund_table.addCell(cell);

            cell = new PdfPCell(new Phrase("High", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            fund_table.addCell(cell);


            cell = new PdfPCell(
                    new Phrase("Bond Fund (SFIN:ULIF002100105BONDULPFND111)",
                            small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            fund_table.addCell(cell);

            String perInvBondFund_percent = perInvBondFund.equals("") ? "0"
                    : perInvBondFund;

            cell = new PdfPCell(new Phrase(perInvBondFund_percent + "%",
                    small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            fund_table.addCell(cell);

            cell = new PdfPCell(new Phrase("1.00%", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            fund_table.addCell(cell);

            cell = new PdfPCell(new Phrase("Low to Medium", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            fund_table.addCell(cell);
            // 4 row
            cell = new PdfPCell(
                    new Phrase("Balanced Fund (SFIN : ULIF004051205BALANCDFND111)",
                            small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            fund_table.addCell(cell);

            String perInvBalencedFund = perInvBalancedFund.equals("") ? "0"
                    : perInvBalancedFund;

            cell = new PdfPCell(new Phrase(perInvBalencedFund + "%",
                    small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            fund_table.addCell(cell);

            cell = new PdfPCell(new Phrase("1.25%", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            fund_table.addCell(cell);

            cell = new PdfPCell(new Phrase("Medium", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            fund_table.addCell(cell);

            cell = new PdfPCell(
                    new Phrase("Bond Optimiser Fund (SFIN : ULIF032290618BONDOPTFND111)",
                            small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            fund_table.addCell(cell);

            String perInvBondOptimiser = perInvBondOptimiserFund.equals("") ? "0"
                    : perInvBondOptimiserFund;

            cell = new PdfPCell(new Phrase(perInvBondOptimiser + "%",
                    small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            fund_table.addCell(cell);

            cell = new PdfPCell(new Phrase("1.15%", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            fund_table.addCell(cell);

            cell = new PdfPCell(new Phrase("Low to Medium", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            fund_table.addCell(cell);

            cell = new PdfPCell(
                    new Phrase("Money Market Fund (SFIN : ULIF005010206MONYMKTFND111)",
                            small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            fund_table.addCell(cell);

            String perInvMoneyMarket = perInvMoneyMarketFund.equals("") ? "0"
                    : perInvMoneyMarketFund;

            cell = new PdfPCell(new Phrase(perInvMoneyMarket + "%",
                    small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            fund_table.addCell(cell);

            cell = new PdfPCell(new Phrase("0.25%", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            fund_table.addCell(cell);

            cell = new PdfPCell(new Phrase("Low", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            fund_table.addCell(cell);

            cell = new PdfPCell(
                    new Phrase("Corporate Bond Fund (SFIN : ULIF033290618CORBONDFND111)",
                            small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            fund_table.addCell(cell);

            String perInvCoporateBondFund = perInvCorporateBondFund.equals("") ? "0"
                    : perInvCorporateBondFund;

            cell = new PdfPCell(new Phrase(perInvCoporateBondFund + "%",
                    small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            fund_table.addCell(cell);

            cell = new PdfPCell(new Phrase("1.15%", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            fund_table.addCell(cell);

            cell = new PdfPCell(new Phrase("Low to Medium", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            fund_table.addCell(cell);

            cell = new PdfPCell(
                    new Phrase("Pure Fund (SFIN : ULIF030290915PUREULPFND111)",
                            small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            fund_table.addCell(cell);

            String perInvPureFund2 = perInvPureFund.equals("") ? "0"
                    : perInvPureFund;

            cell = new PdfPCell(new Phrase(perInvPureFund2 + "%",
                    small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            fund_table.addCell(cell);

            cell = new PdfPCell(new Phrase("1.35%", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            fund_table.addCell(cell);

            cell = new PdfPCell(new Phrase("High", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            fund_table.addCell(cell);

            // reduction in yeild for elapsed year since inception Table here
            // -4a

            PdfPTable reductionInYeild_table = new PdfPTable(2);
            reductionInYeild_table.setWidths(new float[]{3.5f, 2f});
            reductionInYeild_table.setWidthPercentage(80f);
            reductionInYeild_table.setHorizontalAlignment(Element.ALIGN_RIGHT);

            // 1st row
            cell = new PdfPCell(new Phrase(
                    "No of years elapsed since inception", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            reductionInYeild_table.addCell(cell);
            cell = new PdfPCell(new Phrase("Reduction in Yeild @ 8%",
                    small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            reductionInYeild_table.addCell(cell);

            // 2 row
            cell = new PdfPCell(new Phrase("" + noOfYrElapsed, small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            reductionInYeild_table.addCell(cell);
            cell = new PdfPCell(new Phrase(redInYieldNoYr + "%", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            reductionInYeild_table.addCell(cell);

            // reduction in yeild for maturity Table here -4b

            PdfPTable reductionInYeild_maturity_table = new PdfPTable(2);
            reductionInYeild_maturity_table.setWidths(new float[]{3.5f, 2f});
            reductionInYeild_maturity_table.setWidthPercentage(80f);
            reductionInYeild_maturity_table
                    .setHorizontalAlignment(Element.ALIGN_RIGHT);

            // 1st row
            cell = new PdfPCell(new Phrase("Maturity at", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            reductionInYeild_maturity_table.addCell(cell);
            cell = new PdfPCell(new Phrase("Reduction in Yeild @ 8%",
                    small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            reductionInYeild_maturity_table.addCell(cell);

            // 2 row
            cell = new PdfPCell(new Phrase("" + policyTerm + " Years",
                    small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            reductionInYeild_maturity_table.addCell(cell);
            cell = new PdfPCell(new Phrase(redInYieldMat + "%", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            reductionInYeild_maturity_table.addCell(cell);

            PdfPTable main_table;

            main_table = new PdfPTable(1);
            //  main_table.setWidths(new float[]{4f, 6f, 3f});
            main_table.setWidthPercentage(100);
            main_table.getDefaultCell().setPadding(20f);

            // cell = new PdfPCell(input_table);
            // cell.setRowspan(4);
            //cell.setBorder(Rectangle.NO_BORDER);
            // cell.setVerticalAlignment(Element.ALIGN_TOP);
            // main_table.addCell(cell);

            cell = new PdfPCell(fund_table);
            cell.setRowspan(3);
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setVerticalAlignment(Element.ALIGN_TOP);
            main_table.addCell(cell);

           /* cell = new PdfPCell(new Phrase("\n"));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            main_table.addCell(cell);

            //cell = new PdfPCell(reductionInYeild_table);
           // cell.setBorder(Rectangle.NO_BORDER);
           // cell.setVerticalAlignment(Element.ALIGN_TOP);
           // main_table.addCell(cell);
           // cell = new PdfPCell(reductionInYeild_maturity_table);
           // cell.setBorder(Rectangle.NO_BORDER);
           // cell.setVerticalAlignment(Element.ALIGN_TOP);
            //main_table.addCell(cell);
            cell = new PdfPCell(new Phrase("\n"));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            main_table.addCell(cell);
            cell = new PdfPCell(new Phrase("\n"));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            main_table.addCell(cell);*/


            Paragraph base_para = new Paragraph(" *It is a base premium ",
                    small_normal);
            base_para.setAlignment(Element.ALIGN_LEFT);

            Paragraph base_para2 = new Paragraph("# Accidental Death Benefit",
                    small_normal);
            base_para2.setAlignment(Element.ALIGN_LEFT);

            Paragraph note = new Paragraph("Notes :", small_bold);
            note.setAlignment(Element.ALIGN_LEFT);
            // notes Table here

            PdfPTable table2 = new PdfPTable(2);
            table2.setWidths(new float[]{0.3f, 9f});
            table2.setWidthPercentage(100);
            table2.setHorizontalAlignment(Element.ALIGN_LEFT);

            // 1st note
            cell = new PdfPCell(new Phrase("", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table2.addCell(cell);
            cell = new PdfPCell(
                    new Phrase(
                            "1). Refer the sales literature for explanation of terms used in this illustration",
                            small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT
                    | Element.ALIGN_JUSTIFIED);
            cell.setBorder(Rectangle.NO_BORDER);
            table2.addCell(cell);

            // 2 note
            cell = new PdfPCell(new Phrase("", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table2.addCell(cell);
            cell = new PdfPCell(
                    new Phrase(
                            "2) Please read this benefit illustration in conjunction with Sales Brochure and the Policy Document to understand all Terms, Conditions & Exclusions carefully.",
                            small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            table2.addCell(cell);

            // 3 note
            cell = new PdfPCell(new Phrase("", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table2.addCell(cell);
            cell = new PdfPCell(
                    new Phrase(
                            "3) Kindly note that above is only an illustration and does not in any way create any rights and/or obligations. The actual experience on the contract may be different from what is illustrated. The non-guaranteed low and high rate mentioned above relate to assumed investment returns at different rates and may vary depending upon market conditions. For more details on risk factors, terms and conditions please read sales brochure carefully.",
                            small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            table2.addCell(cell);
            // 4 note
            cell = new PdfPCell(new Phrase("", small_normal));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table2.addCell(cell);
            cell = new PdfPCell(
                    new Phrase(
                            "4) The unit values may go up as well as down and past performance is no indication of future performance on the part of SBI Life Insurance Co. Ltd. We would request you to appreciate the associated risk under this plan vis-à-vis the likely future returns before taking your investment decision.",
                            small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            table2.addCell(cell);
            // 5 note
            cell = new PdfPCell(new Phrase("", small_normal));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table2.addCell(cell);
            cell = new PdfPCell(
                    new Phrase(
                            "5) It is assumed that the policy is in force throughout the term.",
                            small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            table2.addCell(cell);
            // 6 note
            cell = new PdfPCell(new Phrase("", small_normal));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table2.addCell(cell);
            cell = new PdfPCell(
                    new Phrase(
                            "6) Fund management charge is based on the specific  investment strategy / fund option(s) chosen.",
                            small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            table2.addCell(cell);

            // 7 note
            cell = new PdfPCell(new Phrase("", small_normal));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table2.addCell(cell);
            cell = new PdfPCell(
                    new Phrase(
                            "7) Surrender Value equals the Fund Value at the end of the year minus Discontinuance Charges. Surrender value is available on or after 5th policy anniversary.",
                            small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            table2.addCell(cell);

            // 8 note
            cell = new PdfPCell(new Phrase("", small_normal));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table2.addCell(cell);
            cell = new PdfPCell(
                    new Phrase(
                            "8) Acceptance of proposal is subject to Underwriting decision. Mortality charges are for a healthy person.",
                            small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT
                    | Element.ALIGN_JUSTIFIED);
            cell.setBorder(Rectangle.NO_BORDER);
            table2.addCell(cell);

            // 9 note


            // 10 note
            cell = new PdfPCell(new Phrase("", small_normal));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table2.addCell(cell);

            cell = new PdfPCell(new Phrase(
                    "9) Applicable Taxes (including surcharge/cess etc), at the rate notified by the Central Government/ State Government / Union Territories of India from time to time and as per the provisions of the prevalent tax laws will be payable on premium/ or any other charges as per the product features.",
                    small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            table2.addCell(cell);

            cell = new PdfPCell(new Phrase("", small_normal));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table2.addCell(cell);
            cell = new PdfPCell(
                    new Phrase(
                            "10) This policy provides guaranteed death benefit of Rs. "
                                    + currencyFormat.format(Double
                                    .parseDouble(sumAssured)), small_normal));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table2.addCell(cell);

            // 11 note
            cell = new PdfPCell(new Phrase("", small_normal));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table2.addCell(cell);
            cell = new PdfPCell(
                    new Phrase(
                            "11) Net Yield have been calculated after applying all the charges (except GST, mortality charges).",
                            small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            table2.addCell(cell);

            PdfPTable table3 = new PdfPTable(1);
            //table3.setWidths(new float[]{0.5f, 3f, 9f});
            table3.setWidthPercentage(80);
            table3.setHorizontalAlignment(Element.ALIGN_LEFT);

            cell = new PdfPCell(new Phrase("Definition of Various Charges:",
                    small_bold));
            cell.setColspan(1);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            table3.addCell(cell);
            cell = new PdfPCell(new Phrase("", small_normal));
            cell.setRowspan(5);
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table3.addCell(cell);

            // charges row
            cell = new PdfPCell(new Phrase("",
                    small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            //table3.addCell(cell);
            cell = new PdfPCell(
                    new Phrase(
                            "1)Policy Administration Charges : a charge of a fixed sum which is applied at the beginning of each policy month by cancelling units for equivalent amount, deducted for maintaining the policy.",
                            small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table3.addCell(cell);

            // 1st charges row
            cell = new PdfPCell(new Phrase("",
                    small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            // table3.addCell(cell);
            cell = new PdfPCell(
                    new Phrase(
                            "2)Premium Allocation Charge : is the percentage of premium that would not be utilised to purchase units.",
                            small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table3.addCell(cell);

            // 2 charges row
            cell = new PdfPCell(new Phrase("",
                    small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            // table3.addCell(cell);
            cell = new PdfPCell(
                    new Phrase(
                            "3)Mortality Charges : are the charges recovered for providing life insurance cover, deducted at the beginning of each policy month by cancelling units for equivalent amount.",
                            small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table3.addCell(cell);

            // 3 charges row
            cell = new PdfPCell(new Phrase("",
                    small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            // table3.addCell(cell);
            cell = new PdfPCell(
                    new Phrase(
                            "4)Fund Management Charge (FMC) : is the deduction made from the fund at a stated percentage before the computation of the NAV of the fund.",
                            small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table3.addCell(cell);

            // 4 charges row
            cell = new PdfPCell(new Phrase("Mortality charges", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            //  table3.addCell(cell);
            cell = new PdfPCell(
                    new Phrase(
                            " are the charges recovered for providing life insurance cover.",
                            small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            // table3.addCell(cell);

            // 5 charges row
            cell = new PdfPCell(new Phrase("Option charges", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            //table3.addCell(cell);
            cell = new PdfPCell(
                    new Phrase(
                            " are the charges recovered for providing Accident Death Benefit cover.",
                            small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            //table3.addCell(cell);

            Paragraph new_line = new Paragraph("\n");
            LineSeparator ls = new LineSeparator();

            document.add(new_line);

            document.add(main_table);
            document.add(new_line);
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

            //document.add(new_line);
            //document.add(new_line);
            // document.add(new_line);
            // document.add(new_line);
            // document.add(new_line);
            document.add(new_line);


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
                                    + "    have received the information with respect to the above and have understood the above statement before entering into a contract.",
                            small_bold));

            BI_Pdftable26_cell1.setPadding(5);

            BI_Pdftable26.addCell(BI_Pdftable26_cell1);
            document.add(BI_Pdftable26);
            document.add(BI_Pdftable26_cell1);
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
            }

            PdfPTable t2 = new PdfPTable(1);
            t2.setWidthPercentage(100);
            PdfPCell cell2 = new PdfPCell(new Paragraph("PART B", small_bold));
            t2.addCell(cell2);
            document.add(t2);


           /* document.add(new_line);
            document.add(new_line);
            document.add(new_line);
            document.add(new_line);
            document.add(new_line);
            document.add(table1);
            document.add(base_para);
            if (adb_rider_status.equalsIgnoreCase("true")) {
                document.add(base_para2);
            }*/


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

            PdfPCell BI_Pdftable_output_cell781 = new PdfPCell(new Paragraph(
                    "ADB option charge", small_bold2));
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
            // document.add(BI_Pdftableoutput_no8);

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

            PdfPCell BI_Pdftable_output_cell7811 = new PdfPCell(new Paragraph(
                    "ADB option charge", small_bold2));
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
            // document.add(BI_Pdftableoutput_no4);

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


            document.add(new_line);
            document.add(note);
            document.add(table2);
            document.add(new_line);
            document.add(ls);
            document.add(table3);

            document.add(new_line);
            //Start 10 Jan 18
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

            if (Double.parseDouble(edt_bi_smart_wealth_assure_sum_assured_amount.getText().toString()) > 100000) {
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
            //End 10 jan18
            // ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

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
                                    + "     have understood the term and conditions, product features and applicable charges (if any) before purchase of the contract, after receipt of all information stated above from the insurer.",
                            small_bold));

            BI_Pdftable261_cell1.setPadding(5);

            BI_Pdftable261.addCell(BI_Pdftable261_cell1);
            document.add(BI_Pdftable261);
            document.add(BI_Pdftable261_cell1);
            document.add(para_img_logo_after_space_1);

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
            }
            document.close();

        } catch (Exception e) {
            Log.i(getLocalClassName(), e.toString() + "Error in creating pdf");
        }

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
                        imageButtonSmartWealthAssureProposerPhotograph
                                .setImageBitmap(scaled);
                    } else {
                        photoBitmap = null;
                    }
                }
            }
        }
    }

    private void windowmessagesgin() {

        d = new Dialog(BI_SmartWealthAssureActivity.this);
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
                Intent intent = new Intent(BI_SmartWealthAssureActivity.this,
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

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub

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
        // TODO Auto-generated method stub
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
            // TODO Auto-generated method stub
            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;
            updateDisplay(DIALOG_ID);
        }
    };

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
                    final_age = Integer.toString(age);
                    if (final_age.contains("-")) {
                        commonMethods.BICommonDialog(context, "Please fill Valid Birth Date");
                    } else {
                        if (18 <= age) {
                            ProposerAge = final_age;
                            btn_bi_smart_wealth_assure_proposer_date.setText(date);
                            propAge.setText(ProposerAge);
                            // edt_bi_smart_wealth_assure_life_assured_age.setText(final_age);
                            proposer_date_of_birth = getDate1(date + "");
                            clearFocusable(btn_bi_smart_wealth_assure_proposer_date);
                            setFocusable(edt_smart_wealth_assure_contact_no);
                            edt_smart_wealth_assure_contact_no.requestFocus();

                            // valAge();
                        } else {
                            commonMethods.BICommonDialog(context, "Minimum age should be 18 yrs for proposer");
                            btn_bi_smart_wealth_assure_proposer_date
                                    .setText("Select Date");
                            // edt_bi_smart_wealth_assure_life_assured_age.setText("");
                            proposer_date_of_birth = "";
                            clearFocusable(btn_bi_smart_wealth_assure_proposer_date);
                            setFocusable(btn_bi_smart_wealth_assure_proposer_date);
                            btn_bi_smart_wealth_assure_proposer_date.requestFocus();
                        }
                    }
                    break;

                case 5:

                    final_age = Integer.toString(age);
                    if (final_age.contains("-")) {
                        commonMethods.BICommonDialog(context, "Please fill Valid Birth Date");
                    } else {
                        int maxLimit;
                        // maxLimit = 65;
                        maxLimit = 60;
                        if (8 <= age && age <= maxLimit) {

                            if (Integer.parseInt(final_age) < 18) {

                                tr_bi_smart_wealth_assure_adb_rider_checkbox
                                        .setVisibility(View.GONE);
                                tr_bi_smart_wealth_assure_adb_rider
                                        .setVisibility(View.GONE);
                                cb_bi_smart_wealth_assure_adb_rider
                                        .setChecked(false);
                                proposer_Is_Same_As_Life_Assured = "N";
                                linearlayoutProposerDetails.setVisibility(View.VISIBLE);
//                                tr_smart_wealth_assure_proposer_detail1
//                                        .setVisibility(View.VISIBLE);
//                                tr_smart_wealth_assure_proposer_detail2
//                                        .setVisibility(View.VISIBLE);

                                btn_bi_smart_wealth_assure_life_assured_date
                                        .setText(date);
                                edt_bi_smart_wealth_assure_life_assured_age
                                        .setText(final_age);
                                lifeAssured_date_of_birth = getDate1(date + "");

                            } else {
                                tr_bi_smart_wealth_assure_adb_rider_checkbox
                                        .setVisibility(View.VISIBLE);
                                proposer_Is_Same_As_Life_Assured = "Y";
                                linearlayoutProposerDetails.setVisibility(View.GONE);

//                                tr_smart_wealth_assure_proposer_detail1
//                                        .setVisibility(View.GONE);
//                                tr_smart_wealth_assure_proposer_detail2
//                                        .setVisibility(View.GONE);

                                btn_bi_smart_wealth_assure_life_assured_date
                                        .setText(date);
                                edt_bi_smart_wealth_assure_life_assured_age
                                        .setText(final_age);
                                lifeAssured_date_of_birth = getDate1(date + "");

                                if (valPolicyTerm()) {
                                    int adbTerm = Math
                                            .min(Integer
                                                            .parseInt(spnr_bi_smart_wealth_assure_policyterm
                                                                    .getSelectedItem()
                                                                    .toString()),
                                                    (70 - Integer
                                                            .parseInt(edt_bi_smart_wealth_assure_life_assured_age
                                                                    .getText()
                                                                    .toString())));

                                    String adbTerm1 = String.valueOf(adbTerm);
                                    spnr_bi_smart_wealth_assure_adb_rider_term
                                            .setSelection(
                                                    getIndex(
                                                            spnr_bi_smart_wealth_assure_adb_rider_term,
                                                            adbTerm1), false);
                                    // int a = 1;

                                }
                            }

                            if (Integer.parseInt(final_age) < 18) {
                                clearFocusable(btn_bi_smart_wealth_assure_life_assured_date);
                                setFocusable(spnr_bi_smart_wealth_assure_proposer_title);
                                spnr_bi_smart_wealth_assure_proposer_title
                                        .requestFocus();
                            } else {
                                clearFocusable(edt_bi_smart_wealth_assure_life_assured_last_name);
                                setFocusable(edt_smart_wealth_assure_contact_no);
                                edt_smart_wealth_assure_contact_no.requestFocus();
                            }

                        } else {
                            commonMethods.BICommonDialog(context, "Minimum Age should be 8 yrs and Maximum Age should be "
                                    + maxLimit + " yrs For LifeAssured");
                            btn_bi_smart_wealth_assure_life_assured_date
                                    .setText("Select Date");
                            edt_bi_smart_wealth_assure_life_assured_age.setText("");
                            lifeAssured_date_of_birth = "";

                            clearFocusable(btn_bi_smart_wealth_assure_life_assured_date);
                            setFocusable(btn_bi_smart_wealth_assure_life_assured_date);
                            btn_bi_smart_wealth_assure_life_assured_date
                                    .requestFocus();
                        }
                    }
                    break;

                default:
                    break;

            }

        }
    }

    /*private boolean isADBeligible(int age) {
        if (age >= 18 && age < (64 + 1)) {
            return true;
        } else {
            return false;
    }
    }*/

    private void validationOfMoile_EmailId() {

        edt_smart_wealth_assure_contact_no
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
                        String abc = edt_smart_wealth_assure_contact_no
                                .getText().toString();
                        mobile_validation(abc);

                    }
                });

        edt_smart_wealth_assure_Email_id
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
                        ProposerEmailId = edt_smart_wealth_assure_Email_id
                                .getText().toString();
                        //email_id_validation(ProposerEmailId);

                    }
                });

        edt_smart_wealth_assure_ConfirmEmail_id
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
                        String proposer_confirm_emailId = edt_smart_wealth_assure_ConfirmEmail_id
                                .getText().toString();
                        //confirming_email_id(proposer_confirm_emailId);

                    }
                });

    }

    private String getformatedThousandString(int number) {
        return NumberFormat.getNumberInstance(Locale.US)
                .format(number);
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
    }

    private void setDefaultDate(int id) {
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


    private void mobile_validation(String number) {
        if ((number.length() != 10)) {
            edt_smart_wealth_assure_contact_no
                    .setError("Please provide correct 10-digit mobile number");
        } else if ((number.length() == 10)) {
        }
    }

    private void email_id_validation(String email_id) {

        String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email_id);
        if (!(matcher.matches())) {
            edt_smart_wealth_assure_Email_id
                    .setError("Please provide the correct email address");
            validationFla1 = false;
        } else if ((matcher.matches())) {
            validationFla1 = true;
        }
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

    // validation
    private String getEffectivePremium() {
        if (!edt_bi_smart_wealth_assure_sum_assured_amount.getText().toString()
                .equals(""))
            return edt_bi_smart_wealth_assure_sum_assured_amount.getText()
                    .toString();

        else
            return "";
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
                                    setFocusable(spnr_bi_smart_wealth_assure_life_assured_title);
                                    spnr_bi_smart_wealth_assure_life_assured_title
                                            .requestFocus();
                                } else if (lifeAssured_First_Name.equals("")) {
                                    setFocusable(edt_bi_smart_wealth_assure_life_assured_first_name);
                                    edt_bi_smart_wealth_assure_life_assured_first_name
                                            .requestFocus();
                                } else {
                                    setFocusable(edt_bi_smart_wealth_assure_life_assured_last_name);
                                    edt_bi_smart_wealth_assure_life_assured_last_name
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
                                setFocusable(spnr_bi_smart_wealth_assure_life_assured_title);
                                spnr_bi_smart_wealth_assure_life_assured_title
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
                                setFocusable(spnr_bi_smart_wealth_assure_life_assured_title);
                                spnr_bi_smart_wealth_assure_life_assured_title
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
                                setFocusable(spnr_bi_smart_wealth_assure_life_assured_title);
                                spnr_bi_smart_wealth_assure_life_assured_title
                                        .requestFocus();
                            }
                        });
                showAlert.show();

                return false;
            } else {
                return true;
            }

        } else if (proposer_Is_Same_As_Life_Assured.equalsIgnoreCase("N")) {

            if (lifeAssured_Title.equals("")
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
                                    setFocusable(spnr_bi_smart_wealth_assure_life_assured_title);
                                    spnr_bi_smart_wealth_assure_life_assured_title
                                            .requestFocus();
                                } else if (lifeAssured_First_Name.equals("")) {

                                    edt_bi_smart_wealth_assure_life_assured_first_name
                                            .requestFocus();
                                } else {
                                    edt_bi_smart_wealth_assure_life_assured_last_name
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
                                setFocusable(spnr_bi_smart_wealth_assure_life_assured_title);
                                spnr_bi_smart_wealth_assure_life_assured_title
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
                                setFocusable(spnr_bi_smart_wealth_assure_life_assured_title);
                                spnr_bi_smart_wealth_assure_life_assured_title
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
                                setFocusable(spnr_bi_smart_wealth_assure_life_assured_title);
                                spnr_bi_smart_wealth_assure_life_assured_title
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
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                if (proposer_Title.equals("")) {
                                    // apply focusable method
                                    setFocusable(spnr_bi_smart_wealth_assure_proposer_title);
                                    spnr_bi_smart_wealth_assure_proposer_title
                                            .requestFocus();
                                } else if (proposer_First_Name.equals("")) {
                                    edt_bi_smart_wealth_assure_proposer_first_name
                                            .requestFocus();
                                } else {
                                    edt_bi_smart_wealth_assure_proposer_last_name
                                            .requestFocus();
                                }
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

        if (proposer_Is_Same_As_Life_Assured.equalsIgnoreCase("Y")) {

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
                                setFocusable(btn_bi_smart_wealth_assure_life_assured_date);
                                btn_bi_smart_wealth_assure_life_assured_date
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

    private boolean valProposerDob() {

        if (proposer_Is_Same_As_Life_Assured.equalsIgnoreCase("N")) {

            if (proposer_date_of_birth.equals("")
                    || proposer_date_of_birth.equalsIgnoreCase("Select Date")) {
                showAlert
                        .setMessage("Please Select Valid Date Of Birth For Proposer");
                showAlert.setNeutralButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // apply focusable method
                                setFocusable(btn_bi_smart_wealth_assure_proposer_date);
                                btn_bi_smart_wealth_assure_proposer_date
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

    public void updatePremiumAmtLabel() {
        try {
            help_premAmt.setText("(Min. Rs. "
                    + currencyFormat.format(prop.minPremAmt) + ")");
        } catch (Exception ignored) {
        }
    }

    private boolean valBasicDetail() {

        if (edt_smart_wealth_assure_contact_no.getText().toString().equals("")) {
            commonMethods.dialogWarning(context, "Please Fill  Mobile No", true);
            edt_smart_wealth_assure_contact_no.requestFocus();
            return false;
        } else if (edt_smart_wealth_assure_contact_no.getText().toString()
                .length() != 10) {
            commonMethods.dialogWarning(context, "Please Fill 10 Digit Mobile No", true);
            edt_smart_wealth_assure_contact_no.requestFocus();
            return false;
        }

		/*else if (emailId.equals("")) {
			commonMethods.dialogWarning(context, "Please Fill Email Id", true);
			edt_smart_wealth_assure_Email_id.requestFocus();
			return false;

		}

		else if (ConfirmEmailId.equals("")) {

			commonMethods.dialogWarning(context, "Please Fill Confirm Email Id", true);
			edt_smart_wealth_assure_ConfirmEmail_id.requestFocus();
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
                    edt_smart_wealth_assure_ConfirmEmail_id.requestFocus();
                    return false;
                } else if (!ConfirmEmailId.equals(emailId)) {
                    commonMethods.dialogWarning(context, "Email Id Does Not Match", true);
                    return false;
                }

                return true;
            } else {
                commonMethods.dialogWarning(context, "Please Fill Valid Email Id", true);
                edt_smart_wealth_assure_Email_id.requestFocus();
                return false;
            }
        } else if (!ConfirmEmailId.equals("")) {

            email_id_validation(ConfirmEmailId);
            if (validationFla1) {

                if (emailId.equals("")) {
                    commonMethods.dialogWarning(context, "Please Fill Email Id", true);
                    edt_smart_wealth_assure_Email_id.requestFocus();
                    return false;

                }

                return true;
            } else {
                commonMethods.dialogWarning(context, "Please Fill Valid Confirm Email Id", true);
                edt_smart_wealth_assure_ConfirmEmail_id.requestFocus();
                return false;
            }
        } else {
            return true;
        }
    }

    private void updateADBsumAssuredLabel() {
        try {
            double new_maxADB_SA = 0;
            double minADBsumAssured = Math.max(
                    Math.min(25000.0, Double.parseDouble(effectivePremium)),
                    25000.0);
            double a = 0;
            if ((Double.parseDouble(effectivePremium) * Double
                    .parseDouble(edt_smart_wealth_assure_samf.getText()
                            .toString())) == 0) {
                a = 5000000;
            } else {
                a = (Double.parseDouble(effectivePremium) * Double
                        .parseDouble(edt_smart_wealth_assure_samf.getText()
                                .toString()));
            }
            double maxADBsumAssured = Math.min(5000000.0, a);
            if ((maxADBsumAssured % 1000) != 0) {
                double remainder = maxADBsumAssured % 1000;
                new_maxADB_SA = maxADBsumAssured - remainder;
            } else {
                new_maxADB_SA = maxADBsumAssured;
            }
            help_adbsa.setText("Rs. " + currencyFormat.format(minADBsumAssured)
                    + " to Rs. " + currencyFormat.format(new_maxADB_SA) + ")");
        } catch (Exception ignored) {
        }
    }

    // help for no of yeras elapsed since Inception
    private void updatenoOfYearsElapsedSinceInception() {
        try {
            help_noOfYearsElapsedSinceInception.setText("(5 to "
                    + spnr_bi_smart_wealth_assure_policyterm.getSelectedItem()
                    .toString() + " years)");
        } catch (Exception ignored) {
        }
    }

    // Help for SAMF
    public void updateSAMFlabel() {
        double minSAMFforSingleFreq = 0, maxSAMFforSingleFreq = 0;
        try {
           /* double minSAMFforSingleFreq = 0, maxSAMFforSingleFreq = 0;
            if (Integer.parseInt(edt_bi_smart_wealth_assure_life_assured_age
                    .getText().toString()) < 45) {
                minSAMFforSingleFreq = 1.25;
                maxSAMFforSingleFreq = 5;
            } else {
                minSAMFforSingleFreq = 1.1;
                maxSAMFforSingleFreq = 3;
            }*/
            minSAMFforSingleFreq = 1.25;
            maxSAMFforSingleFreq = 1.25;
            help_SAMF.setText("(" + minSAMFforSingleFreq + " to "
                    + maxSAMFforSingleFreq + ")");
        } catch (Exception ignored) {
        }
    }

    // validate Eligibility for ADB Rider
    private boolean valADBRider() {

        if (!(edt_bi_smart_wealth_assure_life_assured_age.getText().toString()
                .equals(""))) {
            if (Integer.parseInt(edt_bi_smart_wealth_assure_life_assured_age
                    .getText().toString()) < 18
                    || Integer
                    .parseInt(edt_bi_smart_wealth_assure_life_assured_age
                            .getText().toString()) > 65) {
                if (cb_bi_smart_wealth_assure_adb_rider.isChecked()) {

                    cb_bi_smart_wealth_assure_adb_rider.setChecked(false);
                    // if
                    // (spnr_bi_smart_wealth_assure_adb_rider_term.getVisibility()
                    // == View.VISIBLE) {
                    //
                    // spnr_bi_smart_wealth_assure_adb_rider_term
                    // .setVisibility(View.GONE);
                    // edt_bi_smart_wealth_assure_adb_rider_sum_assured
                    // .setVisibility(View.GONE);
                    // // help_adbsa.setVisibility(View.GONE);
                    // }
                    showAlert
                            .setMessage("Age limit for Accidental Death Benefit Option is 18 to 64.\n");
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
        }

        return true;

    }

    // Validate SumAssured of ADB Rider
    private boolean valADBsumAssured() {
        if (cb_bi_smart_wealth_assure_adb_rider.isChecked()) {
            String errorMessage = "";

            double minADBsumAssured = Math.max(
                    Math.min(25000.0, Double.parseDouble(effectivePremium)),
                    25000.0);
            double a = 0;
            if ((Double.parseDouble(effectivePremium) * Double
                    .parseDouble(edt_smart_wealth_assure_samf.getText()
                            .toString())) == 0) {
                a = 5000000;
            } else {
                a = (Double.parseDouble(effectivePremium) * Double
                        .parseDouble(edt_smart_wealth_assure_samf.getText()
                                .toString()));
            }
            double maxADBsumAssured = Math.min(5000000.0, a);
            if (edt_bi_smart_wealth_assure_adb_rider_sum_assured.getText()
                    .toString().equals(""))
                errorMessage = "Please enter Accidental Death Benefit Option Sum Assured.";
            else if (Double
                    .parseDouble(edt_bi_smart_wealth_assure_adb_rider_sum_assured
                            .getText().toString()) % 1000 != 0)
                errorMessage = "Accidental Death Benefit (ADB) Option Sum Assured should be in multiple of 1000.";
            else if ((Double
                    .parseDouble(edt_bi_smart_wealth_assure_adb_rider_sum_assured
                            .getText().toString()) < minADBsumAssured || Double
                    .parseDouble(edt_bi_smart_wealth_assure_adb_rider_sum_assured
                            .getText().toString()) > maxADBsumAssured))
                errorMessage = "Accidental Death Benefit (ADB) Option Sum Assured should be in the range of "
                        + currencyFormat.format(minADBsumAssured)
                        + " Rs. to "
                        + currencyFormat.format(maxADBsumAssured) + " Rs.";

            if (!errorMessage.equals("")) {
                showAlert.setMessage(errorMessage);
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

    // Addition of invested fund must be 100%
    public boolean valTotalAllocation() {
        double equityFund, bondFund, peManagedFund, guaranteeFund, balancedFund, moneyMarketFund,
                bondOptimiserFund, corporateBondFund, pureFund;

        if (!edt_smart_wealth_assure_percent_BondFund.getText().toString()
                .equals(""))
            bondFund = Double
                    .parseDouble(edt_smart_wealth_assure_percent_BondFund
                            .getText().toString());
        else
            bondFund = 0;

        if (!edt_smart_wealth_assure_percent_EquityFund.getText().toString()
                .equals(""))
            equityFund = Double
                    .parseDouble(edt_smart_wealth_assure_percent_EquityFund
                            .getText().toString());
        else
            equityFund = 0;

        if (!percent_BalancedFund.getText().toString().equals(""))
            balancedFund = Double.parseDouble(percent_BalancedFund.getText()
                    .toString());
        else
            balancedFund = 0;

        if (!percent_MoneyMktFund.getText().toString().equals(""))
            moneyMarketFund = Double.parseDouble(percent_MoneyMktFund.getText()
                    .toString());
        else
            moneyMarketFund = 0;

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

        if (!percent_PureFund.getText().toString().equals(""))
            pureFund = Double.parseDouble(percent_PureFund.getText()
                    .toString());
        else
            pureFund = 0;

        if ((equityFund + bondFund + balancedFund + moneyMarketFund + bondOptimiserFund + corporateBondFund + pureFund) != 100) {
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

    private boolean valYearsElapsedSinceInception() {
        if (edt_smart_wealth_assure_noOfYearsElapsedSinceInception.getText()
                .toString().equals("")
                || Double
                .parseDouble(edt_smart_wealth_assure_noOfYearsElapsedSinceInception
                        .getText().toString()) < 5
                || Double
                .parseDouble(edt_smart_wealth_assure_noOfYearsElapsedSinceInception
                        .getText().toString()) > Double
                .parseDouble(spnr_bi_smart_wealth_assure_policyterm
                        .getSelectedItem().toString())) {

            showAlert
                    .setMessage("Enter No. of Years Elapsed Since Inception between 5 to "
                            + spnr_bi_smart_wealth_assure_policyterm
                            .getSelectedItem().toString() + " Years");
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

    public boolean valPolicyTerm() {
        int maxPolicyTerm = Math.min(Math.max(10, (70 - Integer
                .parseInt(edt_bi_smart_wealth_assure_life_assured_age.getText()
                        .toString()))), 30);
        int minPolicyTerm = prop.minPolicyTerm;
        if (Integer.parseInt(spnr_bi_smart_wealth_assure_policyterm
                .getSelectedItem().toString()) < minPolicyTerm
                || Integer.parseInt(spnr_bi_smart_wealth_assure_policyterm
                .getSelectedItem().toString()) > maxPolicyTerm) {
            showAlert.setMessage("Policy Term should be in the range of "
                    + minPolicyTerm + " Years to " + maxPolicyTerm + " years");
            showAlert.setNeutralButton("OK",
                    new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            spnr_bi_smart_wealth_assure_policyterm
                                    .setSelection(0);
                        }
                    });
            showAlert.show();
            return false;
        }

        return true;
    }

    // public boolean valAdbRider(){
    //
    // if(cb_bi_smart_wealth_assure_adb_rider.isChecked()){
    // int adbTerm = Math
    // .min(Integer
    // .parseInt(spnr_bi_smart_wealth_assure_policyterm
    // .getSelectedItem().toString()),
    // (70 - Integer.parseInt(edt_bi_smart_wealth_assure_life_assured_age
    // .getText().toString())));
    //
    // String adbTerm1 = String.valueOf(adbTerm);
    // spnr_bi_smart_wealth_assure_adb_rider_term
    // .setSelection(getIndex(spnr_bi_smart_wealth_assure_adb_rider_term,
    // adbTerm1), false);
    //
    // }
    // return true;
    // }

    // Loan Amount Validation
    private boolean valPremiumAmt() {
        String error = "";
        if (edt_bi_smart_wealth_assure_sum_assured_amount.getText().toString()
                .equals("")) {
            error = "Please enter Premium Amount in Rs. ";
        } else if (!(Double
                .parseDouble(edt_bi_smart_wealth_assure_sum_assured_amount
                        .getText().toString()) % 100 == 0)) {
            error = "Premium Amount should be multiple of 100";
        } else if (Double
                .parseDouble(edt_bi_smart_wealth_assure_sum_assured_amount
                        .getText().toString()) < 50000) {
            error = "Premium Amount should not be less than Rs. 50,000";
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

    // SAMF
    private boolean valSAMF() {
        double minSAMFforSingleFreq = 0, maxSAMFforSingleFreq = 0;
      /*  if (Integer.parseInt(edt_bi_smart_wealth_assure_life_assured_age
                .getText().toString()) < 45) {
            minSAMFforSingleFreq = 1.25;
            maxSAMFforSingleFreq = 5;
        } else {
            minSAMFforSingleFreq = 1.1;
            maxSAMFforSingleFreq = 3;
        }*/
        minSAMFforSingleFreq = 1.25;
        maxSAMFforSingleFreq = 1.25;
        if (edt_smart_wealth_assure_samf.getText().toString().equals("")
                || Double.parseDouble(edt_smart_wealth_assure_samf.getText()
                .toString()) < minSAMFforSingleFreq
                || Double.parseDouble(edt_smart_wealth_assure_samf.getText()
                .toString()) > maxSAMFforSingleFreq) {
            showAlert
                    .setMessage("Sum Assured Multiple Factor (SAMF) should be in the range of "
                            + minSAMFforSingleFreq
                            + " to "
                            + maxSAMFforSingleFreq);
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
