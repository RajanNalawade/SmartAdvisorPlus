package sbilife.com.pointofsale_bancaagency.saralpension;

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

@SuppressWarnings("deprecation")
public class BI_SaralPensionActivity extends AppCompatActivity implements
        OnEditorActionListener {

    // false,validationFlag3 = false;
    private final int DATE_DIALOG_ID = 1;
    private final String proposer_Is_Same_As_Life_Assured = "Y";
    private final int SIGNATURE_ACTIVITY = 1;
    String ProposerAge = "";
    List<M_BI_SaralPension_AdapterCommon> list_data_new;
    String installmentPremium = "", serviceTax = "", SYservceTax = "", _getGuarDeathBenForMat = "", instalmntPremWTServcTax = "", premInstBasicFirstYear = "",
            SYinstalmntPremWTServcTax = "";
    int minAge = 0, maxAge = 0;
    String guaranteeVestingBenefit = "",
            BonusesNonGuaranVestingBenefit_4Percent = "",
            BonusesNonGuaranVestingBenefit_8Percent = "",
            vestingBenefit_4_pr = "", vestingBenefit_8_pr = "",
            annuityPayout_4_Pr = "", annuityPayout_8_Pr = "",
            guarnVestngBen_AssuredBenefit = "";
    String nonGuaranVestingBenefit_4Percent = "",
            nonGuaranVestingBenefit_8Percent = "",
            guarnVestngBen = "",
            annuityAmount_MiniBen = "", premInstSYRider = "", basicServiceTax = "", basicServiceTaxSecondYear = "", premInstFYRider = "", premInstBasicSecondYear = "";
    String staffStatus;
    String str_kerla_discount = "No";
    String bi_retire_smart_Matuirty_frequency = "", bi_retire_smart_Matuirty_option = "";
    Spinner spnr_bi_Matuirty_option, spnr_bi_Matuirty_frequency;
    private NeedAnalysisBIService NABIObj;
    private NA_CBI_bean na_cbi_bean;
    private File needAnalysispath, newFile;
    private String agentcode, agentMobile, agentEmail, userType;
    private int needAnalysis_flag = 0;
    private String na_input = null;
    private String na_output = null;
    private String UIN_NO;
    // private Utility utilityObj;
    private DatabaseHelper dbHelper;
    private String na_dob = "";
    private ArrayAdapter<String> genderAdapter;
    private int flag = 0;
    private CheckBox cb_staffdisc, cb_bi_saral_pension_JKResident,
            cb_bi_saral_pension_ptr_rider;
    private EditText edt_bi_saral_pension_life_assured_first_name,
            edt_bi_saral_pension_life_assured_middle_name,
            edt_bi_saral_pension_life_assured_last_name,
            edt_bi_saral_pension_life_assured_age,
            edt_saral_pension_contact_no, edt_saral_pension_Email_id,
            edt_saral_pension_ConfirmEmail_id,
            edt_bi_saral_pension_sum_assured_amount,
            edt_bi_saral_pension_ptr_rider_sum_assured;
    private Spinner spnr_bi_saral_pension_life_assured_title,
            spnr_bi_saral_pension_selGender, spnr_bi_saral_pension_policyterm,
            spnr_bi_saral_pension_premium_frequency,
            spnr_bi_saral_pension_ptr_rider_term;
    private Button btn_bi_saral_pension_life_assured_date;
    private Button btnBack, btnSubmit;
    private TableRow tr_bi_saral_pension_pt_rider;
    private String QuatationNumber = "";
    private String planName = "";
    private AlertDialog.Builder showAlert;
    // newDBHelper db;
    private ParseXML prsObj;
    private String emailId = "", mobileNo = "", ConfirmEmailId = "",
            ProposerEmailId = "";
    private Dialog d;
    private boolean validationFla1 = false;// validationFlag2 =
    private int DIALOG_ID;
    private int mYear;
    private int mMonth;
    private int mDay;
    private String lifeAssured_Title = "", lifeAssured_First_Name = "",
            lifeAssured_Middle_Name = "", lifeAssured_Last_Name = "",
            name_of_life_assured = "", lifeAssuredAge = "", lifeAssured_date_of_birth = "";
    // lifeAssuredAge = "", Is_Smoker_or_Not = "";
    private CommonForAllProd obj;
    private StringBuilder bussIll = null;
    private StringBuilder retVal;
    private StringBuilder inputVal;
    private SaralPensionBean saralPensionBean;
    private String name_of_proposer = "", name_of_person = "", place2 = "",
            date1 = "", date2 = "";
    private String agent_sign = "", proposer_sign = "", proposer_date_of_birth = "";
    private DecimalFormat currencyFormat;
    private SaralPensionBean saralpensionbeanstatic;
    private String latestImage = "";
    private Button btn_MarketingOfficalDate;
    private Button btn_PolicyholderDate;
    private ImageButton Ibtn_signatureofMarketing;
    private ImageButton Ibtn_signatureofPolicyHolders;
    private String output = "", input = "";
    private String policyTerm = "";// formattedDate = "",
    private List<M_BI_SaralPensionGrid_Adpter> list_data;
    private List<M_BI_SaralPensionGrid_Adpter> list_data1;
    private String ptr_rider_status = "", term_ptr = "", sa_ptr = "", premPTRWithoutDisc = "", premiumSingleInstBasicRider = "",
            prem_ptr = "", sumAssuredDeath = "", premiumPayingTerm = "", basePremiumExcludingServiceTax = "", intallmentPremWithST = "", premBaiscRider = "";
    private String policy_Frequency = "";
    private String sumAssured = "";
    private String gender = "";
    private String vestingAge = "";
    private String age_entry = "";
    private String installmentPremiumWithserviceTax = "";
    // guarnVestngBen_AssuredBenefit = "";
    private File mypath;
    private String premPayingMode = "";
    private boolean valPremiumError = false;
    private boolean valRiderPremiumError = false;
    private LinearLayout ll_bi_saral_pension_main;
    private String bankUserType = "", mode = "";
    /* parivartan changes */
    private String Check = "";
    private Context context;
    private CommonMethods commonMethods;
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
    private StorageUtils mStorageUtils;
    private ImageButton imageButtonSaralPensionProposerPhotograph;
    private LinearLayout linearlayoutThirdpartySignature,
            linearlayoutAppointeeSignature;
    private ImageButton Ibtn_signatureofThirdParty, Ibtn_signatureofAppointee;
    private String thirdPartySign = "", appointeeSign = "";
    private String product_Code, product_UIN, product_cateogory, product_type, Company_policy_surrender_dec = "";


    /* end */
    private Bitmap photoBitmap;
    private CheckBox cb_kerladisc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.bi_saral_pension);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);
        UI_Declaration();

        context = this;
        commonMethods = new CommonMethods();
        mStorageUtils = new StorageUtils();

        dbHelper = new DatabaseHelper(getApplicationContext());

        new CommonMethods().setApplicationToolbarMenu(this,
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
                    planName = "Saral Retirement Saver";
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
                        product_Code/* "1E" */, agentcode, zero + "");

            }
        } else
            needAnalysis_flag = 0;
        // ProductHomePageActivity.path.setText("Benefit Illustrator");

        prsObj = new ParseXML();
        obj = new CommonForAllProd();
        setSpinner_Value();
        // setBIInputGui();

        cb_kerladisc.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cb_kerladisc.isChecked()) {
                    str_kerla_discount = "Yes";
                    clearFocusable(cb_kerladisc);
                    setFocusable(spnr_bi_saral_pension_life_assured_title);
                    spnr_bi_saral_pension_life_assured_title.requestFocus();
                } else {
                    str_kerla_discount = "No";
                    clearFocusable(cb_kerladisc);
                    setFocusable(spnr_bi_saral_pension_life_assured_title);
                    spnr_bi_saral_pension_life_assured_title.requestFocus();
                }
            }
        });


        edt_bi_saral_pension_life_assured_first_name
                .setOnEditorActionListener(this);
        edt_bi_saral_pension_life_assured_middle_name
                .setOnEditorActionListener(this);
        edt_bi_saral_pension_life_assured_last_name
                .setOnEditorActionListener(this);
        edt_bi_saral_pension_sum_assured_amount.setOnEditorActionListener(this);
        edt_saral_pension_contact_no.setOnEditorActionListener(this);
        edt_saral_pension_Email_id.setOnEditorActionListener(this);
        edt_saral_pension_ConfirmEmail_id.setOnEditorActionListener(this);
        edt_bi_saral_pension_ptr_rider_sum_assured
                .setOnEditorActionListener(this);

        setFocusable(spnr_bi_saral_pension_life_assured_title);
        spnr_bi_saral_pension_life_assured_title.requestFocus();

        validationOfMoile_EmailId();
        setSpinnerAndOtherListner();
        showAlert = new AlertDialog.Builder(this);
        list_data = new ArrayList<M_BI_SaralPensionGrid_Adpter>();
        list_data1 = new ArrayList<M_BI_SaralPensionGrid_Adpter>();
        list_data_new = new ArrayList<M_BI_SaralPension_AdapterCommon>();
        saralpensionbeanstatic = new SaralPensionBean();
        currencyFormat = new DecimalFormat("##,##,##,###");

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

        tr_staff_disc = findViewById(R.id.tr_saral_pension_staff_disc);
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
            spnr_bi_saral_pension_selGender.setSelection(genderAdapter
                    .getPosition(gender));
            onClickLADob(btn_bi_saral_pension_life_assured_date);
        }
    }

    private void UI_Declaration() {

        cb_staffdisc = findViewById(R.id.cb_staffdisc);
        cb_bi_saral_pension_JKResident = findViewById(R.id.cb_bi_saral_pension_JKResident);

        edt_bi_saral_pension_life_assured_first_name = findViewById(R.id.edt_bi_saral_pension_life_assured_first_name);
        edt_bi_saral_pension_life_assured_middle_name = findViewById(R.id.edt_bi_saral_pension_life_assured_middle_name);
        edt_bi_saral_pension_life_assured_last_name = findViewById(R.id.edt_bi_saral_pension_life_assured_last_name);
        edt_bi_saral_pension_life_assured_age = findViewById(R.id.edt_bi_saral_pension_life_assured_age);
        edt_saral_pension_contact_no = findViewById(R.id.edt_saral_pension_contact_no);
        edt_saral_pension_Email_id = findViewById(R.id.edt_saral_pension_Email_id);
        edt_saral_pension_ConfirmEmail_id = findViewById(R.id.edt_saral_pension_ConfirmEmail_id);
        edt_bi_saral_pension_sum_assured_amount = findViewById(R.id.edt_bi_saral_pension_sum_assured_amount);

        spnr_bi_saral_pension_life_assured_title = findViewById(R.id.spnr_bi_saral_pension_life_assured_title);
        spnr_bi_saral_pension_selGender = findViewById(R.id.spnr_bi_saral_pension_selGender);
//        spnr_bi_saral_pension_selGender.setClickable(false);
//        spnr_bi_saral_pension_selGender.setEnabled(false);

        spnr_bi_saral_pension_policyterm = findViewById(R.id.spnr_bi_saral_pension_policyterm);
        spnr_bi_saral_pension_premium_frequency = findViewById(R.id.spnr_bi_saral_pension_premium_frequency);

        // Adb
        cb_bi_saral_pension_ptr_rider = findViewById(R.id.cb_bi_saral_pension_pt_rider);
        edt_bi_saral_pension_ptr_rider_sum_assured = findViewById(R.id.edt_bi_saral_pension_pt_rider_sum_assured);
        spnr_bi_saral_pension_ptr_rider_term = findViewById(R.id.spnr_bi_saral_pension_pt_rider_term);

        btn_bi_saral_pension_life_assured_date = findViewById(R.id.btn_bi_saral_pension_life_assured_date);
        btnBack = findViewById(R.id.btn_bi_saral_pension_btnback);
        btnSubmit = findViewById(R.id.btn_bi_saral_pension_btnSubmit);
        tr_bi_saral_pension_pt_rider = findViewById(R.id.tr_bi_saral_pension_pt_rider);
        ll_bi_saral_pension_main = findViewById(R.id.ll_bi_saral_pension_main);
//        tr_smart_platina_assure_kerla_disc = (TableRow)findViewById(R.id.tr_smart_platina_assure_kerla_disc);
        cb_kerladisc = (CheckBox) findViewById(R.id.cb_kerladisc);

        spnr_bi_Matuirty_option = (Spinner) findViewById(R.id.spnr_bi_Matuirty_option);
        spnr_bi_Matuirty_frequency = (Spinner) findViewById(R.id.spnr_bi_Matuirty_frequency);

    }

    // public void setBIInputGui() {
    // if (getValueFromDatabase()) {
    // int i = 0;
    // List<M_Benefit_Illustration_Detail> data = db
    // .getBIDetail(QuatationNumber);
    //
    // flagFocus = false;
    //
    // name_of_life_assured = data.get(i).getName_of_lifeAssured();
    // String[] lifeAssuredName = name_of_life_assured.split(" ");
    //
    // lifeAssured_Title = lifeAssuredName[0];
    // spnr_bi_saral_pension_life_assured_title.setSelection(
    // getIndex(spnr_bi_saral_pension_life_assured_title,
    // lifeAssured_Title), false);
    // edt_bi_saral_pension_life_assured_first_name
    // .setText(lifeAssuredName[1]);
    // edt_bi_saral_pension_life_assured_middle_name
    // .setText(lifeAssuredName[2]);
    // edt_bi_saral_pension_life_assured_last_name
    // .setText(lifeAssuredName[3]);
    //
    // proposer_Is_Same_As_Life_Assured = data.get(i)
    // .getProposer_Same_As_Life_Assured();
    //
    // lifeAssured_date_of_birth = data.get(i)
    // .getLife_assured_date_of_birth();
    // btn_bi_saral_pension_life_assured_date
    // .setText(getDate(lifeAssured_date_of_birth));
    //
    // String input = data.get(i).getInput();
    //
    // ageAtEntry = prsObj.parseXmlTag(input, "age");
    //
    // edt_bi_saral_pension_life_assured_age.setText(ageAtEntry);
    //
    // gender = prsObj.parseXmlTag(input, "gender");
    // spnr_bi_saral_pension_selGender.setSelection(
    // getIndex(spnr_bi_saral_pension_selGender, gender), false);
    //
    // policyTerm = prsObj.parseXmlTag(input, "policyTerm");
    // spnr_bi_saral_pension_policyterm.setSelection(
    // getIndex(spnr_bi_saral_pension_policyterm, policyTerm),
    // false);
    //
    // premPayingMode = prsObj.parseXmlTag(input, "premFreqMode");
    // spnr_bi_saral_pension_premium_frequency.setSelection(
    // getIndex(spnr_bi_saral_pension_premium_frequency,
    // premPayingMode), false);
    //
    // isJkResident = prsObj.parseXmlTag(input, "isJKResident");
    // if (isJkResident.equalsIgnoreCase("true")) {
    // cb_bi_saral_pension_JKResident.setChecked(true);
    // }else{
    // cb_bi_saral_pension_JKResident.setChecked(false);
    // }
    // ptRiderStatus = prsObj.parseXmlTag(input, "isPTRRider");
    //
    // sumAssured = ((int) Double.parseDouble(prsObj.parseXmlTag(input,
    // "premiumAmount"))) + "";
    // edt_bi_saral_pension_sum_assured_amount.setText(sumAssured);
    //
    // if (ptRiderStatus.equalsIgnoreCase("true")) {
    // tr_bi_saral_pension_pt_rider.setVisibility(View.VISIBLE);
    // cb_bi_saral_pension_ptr_rider.setChecked(true);
    // ptrTerm = prsObj.parseXmlTag(input, "ptrTerm");
    // ptrSA = ((int) Double.parseDouble(prsObj.parseXmlTag(input,
    // "ptrSA"))) + "";
    // edt_bi_saral_pension_ptr_rider_sum_assured.setText(ptrSA);
    // spnr_bi_saral_pension_ptr_rider_term
    // .setSelection(
    // getIndex(spnr_bi_saral_pension_ptr_rider_term,
    // ptrTerm), false);
    //
    // }
    //
    // staffdiscount = prsObj.parseXmlTag(input, "isStaff");
    // if (staffdiscount.equalsIgnoreCase("true")) {
    // cb_staffdisc.setChecked(true);
    // } else {
    // cb_staffdisc.setChecked(false);
    // }
    //
    // proposer_Backdating_BackDate = data.get(i).getBackdate();
    //
    // }
    //
    // }

    private void setSpinner_Value() {

        // Gender
        genderAdapter = new ArrayAdapter<>(getApplicationContext(),
                R.layout.spinner_item, getResources().getStringArray(R.array.gender_all_arrays));
        genderAdapter.setDropDownViewResource(R.layout.spinner_item1);
        spnr_bi_saral_pension_selGender.setAdapter(genderAdapter);
        genderAdapter.notifyDataSetChanged();

        commonMethods.fillSpinnerValue(context, spnr_bi_saral_pension_life_assured_title,
                Arrays.asList(getResources().getStringArray(R.array.arr_select_title)));

        // Policy Term
        String[] policyTermList = new String[36];
        for (int i = 5; i <= 40; i++) {
            policyTermList[i - 5] = i + "";
        }
        ArrayAdapter<String> policyTermAdapter = new ArrayAdapter<>(
                getApplicationContext(), R.layout.spinner_item, policyTermList);
        policyTermAdapter.setDropDownViewResource(R.layout.spinner_item1);
        spnr_bi_saral_pension_policyterm.setAdapter(policyTermAdapter);
        policyTermAdapter.notifyDataSetChanged();

        // Premium Frequency
        String[] premFreqList = {"Yearly", "Half Yearly", "Monthly", "Single"};
        ArrayAdapter<String> premFreqAdapter = new ArrayAdapter<>(
                getApplicationContext(), R.layout.spinner_item, premFreqList);
        premFreqAdapter.setDropDownViewResource(R.layout.spinner_item1);
        spnr_bi_saral_pension_premium_frequency.setAdapter(premFreqAdapter);
        premFreqAdapter.notifyDataSetChanged();

        // preferred Term
        String[] ptrTermList = new String[26];
        for (int i = 5; i <= 30; i++) {
            ptrTermList[i - 5] = i + "";
        }
        ArrayAdapter<String> ptrTermAdapter = new ArrayAdapter<>(
                getApplicationContext(), R.layout.spinner_item, ptrTermList);
        ptrTermAdapter.setDropDownViewResource(R.layout.spinner_item1);
        spnr_bi_saral_pension_ptr_rider_term.setAdapter(ptrTermAdapter);
        ptrTermAdapter.notifyDataSetChanged();


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

        AnnuityOptionAdapter.setDropDownViewResource(R.layout.spinner_item1);
        spnr_bi_Matuirty_option.setAdapter(AnnuityOptionAdapter);
        AnnuityOptionAdapter.notifyDataSetChanged();


        List<String> MaturityFrequency_list = new ArrayList<String>();
        MaturityFrequency_list.add("Select Maturity Frequency");
        MaturityFrequency_list.add("Yearly");
        MaturityFrequency_list.add("Half Yearly");
        MaturityFrequency_list.add("Quarterly");
        MaturityFrequency_list.add("Monthly");

        ArrayAdapter<String> MaturityFrequencyAdapter = new ArrayAdapter<String>(
                getApplicationContext(), R.layout.spinner_item, MaturityFrequency_list);

        MaturityFrequencyAdapter.setDropDownViewResource(R.layout.spinner_item1);
        spnr_bi_Matuirty_frequency.setAdapter(MaturityFrequencyAdapter);
        MaturityFrequencyAdapter.notifyDataSetChanged();

//        fillSpinnerValue(spnr_bi_Matuirty_frequency, MaturityFrequency_list);
    }

    private void setSpinnerAndOtherListner() {

        edt_bi_saral_pension_life_assured_age
                .setOnFocusChangeListener(new OnFocusChangeListener() {

                    public void onFocusChange(View arg0, boolean arg1) {
                        // TODO Auto-generated method stub

                        if (!(edt_bi_saral_pension_life_assured_age.getText()
                                .toString().equals(""))) {

                            valAge();
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
                clearFocusable(spnr_bi_saral_pension_life_assured_title);
                setFocusable(spnr_bi_saral_pension_life_assured_title);
                spnr_bi_saral_pension_life_assured_title.requestFocus();

            }
        });

        cb_bi_saral_pension_JKResident
                .setOnClickListener(new OnClickListener() {

                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        if (cb_bi_saral_pension_JKResident.isChecked()) {
                            cb_bi_saral_pension_JKResident.setChecked(true);
                        } else {
                            cb_bi_saral_pension_JKResident.setChecked(false);
                        }
                    }
                });

        // Spinner
        spnr_bi_saral_pension_life_assured_title
                .setOnItemSelectedListener(new OnItemSelectedListener() {

                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int position, long id) {
                        // TODO Auto-generated method stub
                        if (position == 0) {
                            lifeAssured_Title = "";
                        } else {
                            lifeAssured_Title = spnr_bi_saral_pension_life_assured_title
                                    .getSelectedItem().toString();
						/*	if (lifeAssured_Title.equalsIgnoreCase("Mr.")) {
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
                            clearFocusable(spnr_bi_saral_pension_life_assured_title);
                            setFocusable(edt_bi_saral_pension_life_assured_first_name);
                            edt_bi_saral_pension_life_assured_first_name
                                    .requestFocus();
                        }
                    }

                    public void onNothingSelected(AdapterView<?> arg0) {
                        // TODO Auto-generated method stub

                    }
                });

        spnr_bi_saral_pension_policyterm
                .setOnItemSelectedListener(new OnItemSelectedListener() {

                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int position, long id) {

                        // spnr_bi_saral_pension_premium_frequency.setSelection(3,
                        // true);

                        clearFocusable(spnr_bi_saral_pension_policyterm);
                        setFocusable(spnr_bi_saral_pension_premium_frequency);
                        spnr_bi_saral_pension_premium_frequency.requestFocus();

                    }

                    public void onNothingSelected(AdapterView<?> arg0) {
                        // TODO Auto-generated method stub

                    }
                });

        spnr_bi_saral_pension_premium_frequency
                .setOnItemSelectedListener(new OnItemSelectedListener() {

                    public void onItemSelected(AdapterView<?> adapter,
                                               View arg1, int position, long id) {
                        // TODO Auto-generated method stub

                        if (edt_bi_saral_pension_life_assured_first_name
                                .getText().toString().equals("")) {
                            clearFocusable(spnr_bi_saral_pension_premium_frequency);
                            setFocusable(spnr_bi_saral_pension_life_assured_title);
                            spnr_bi_saral_pension_life_assured_title
                                    .requestFocus();
                        } else {
                            clearFocusable(spnr_bi_saral_pension_premium_frequency);
                            setFocusable(edt_bi_saral_pension_sum_assured_amount);
                            edt_bi_saral_pension_sum_assured_amount
                                    .requestFocus();

                        }
                    }

                    public void onNothingSelected(AdapterView<?> arg0) {
                        // TODO Auto-generated method stub

                    }
                });

        cb_bi_saral_pension_ptr_rider.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                // if(valEntryAgeForRider()){
                if (cb_bi_saral_pension_ptr_rider.isChecked()) {
                    cb_bi_saral_pension_ptr_rider.setChecked(true);
                    tr_bi_saral_pension_pt_rider.setVisibility(View.VISIBLE);
                } else {
                    tr_bi_saral_pension_pt_rider.setVisibility(View.GONE);
                }
                // }else{
                // cb_bi_saral_pension_ptr_rider.setChecked(false);
                // tr_bi_saral_pension_pt_rider.setVisibility(View.GONE);
                // }

            }
        });

        spnr_bi_saral_pension_ptr_rider_term
                .setOnItemSelectedListener(new OnItemSelectedListener() {

                    public void onItemSelected(AdapterView<?> adapter,
                                               View arg1, int position, long id) {
                        // TODO Auto-generated method stub

                        // valADBterm();
                        clearFocusable(spnr_bi_saral_pension_ptr_rider_term);
                        setFocusable(edt_bi_saral_pension_ptr_rider_sum_assured);
                        edt_bi_saral_pension_ptr_rider_sum_assured
                                .requestFocus();

                    }

                    public void onNothingSelected(AdapterView<?> arg0) {
                        // TODO Auto-generated method stub

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
                // TODO Auto-generated method stub

                inputVal = new StringBuilder();
                retVal = new StringBuilder();

                gender = spnr_bi_saral_pension_selGender
                        .getSelectedItem().toString();

                lifeAssured_First_Name = edt_bi_saral_pension_life_assured_first_name
                        .getText().toString();
                lifeAssured_Middle_Name = edt_bi_saral_pension_life_assured_middle_name
                        .getText().toString();
                lifeAssured_Last_Name = edt_bi_saral_pension_life_assured_last_name
                        .getText().toString();

                name_of_life_assured = lifeAssured_Title + " "
                        + lifeAssured_First_Name + " "
                        + lifeAssured_Middle_Name + " " + lifeAssured_Last_Name;

                mobileNo = edt_saral_pension_contact_no.getText().toString();
                emailId = edt_saral_pension_Email_id.getText().toString();
                ConfirmEmailId = edt_saral_pension_ConfirmEmail_id.getText()
                        .toString();

                if (valLifeAssuredProposerDetail() && valDob()
                        && valBasicDetail() && valAge() && valPolicyTerm()
                        && valRiderMaturityAge() && valRiderTerm()
                        && valSumAssured() && valRiderSA() && valMatuirtyFreuqency() && valMatuirtyOption()) {

                    if (proposer_Is_Same_As_Life_Assured.equalsIgnoreCase("y")) {
                        name_of_proposer = "";
                    }
                    Log.e("ouput1:", output + "");
                    addListenerOnSubmit();
                    Log.e("ouput2:", output + "");
                    if (valPremiumError && valRiderPremiumError) {
                        getInput(saralPensionBean);
                        // insertDataIntoDatabase();

                        if (needAnalysis_flag == 0) {

                            Intent i = new Intent(getApplicationContext(),
                                    Success.class);

                            i.putExtra("ProductName",
                                    "Product : SBI Life - Saral Retirement Saver (UIN : 111N088V03)");

                            i.putExtra(
                                    "op",
                                    "Installment Premium is Rs. "
                                            + currencyFormat.format(Double.parseDouble(prsObj
                                            .parseXmlTag(
                                                    retVal.toString(),
                                                    "installmntPrem"))));
                            i.putExtra(
                                    "op1",
                                    "Base Premium Excluding Applicable Tax is Rs. "
                                            + currencyFormat.format(Double.parseDouble(prsObj
                                            .parseXmlTag(
                                                    retVal.toString(),
                                                    "InstmntPrem"))));
                            i.putExtra(
                                    "op2",
                                    "Applicable Tax is Rs. "
                                            + currencyFormat.format(Double.parseDouble(prsObj
                                            .parseXmlTag(
                                                    retVal.toString(),
                                                    "servceTax"))));
                            i.putExtra(
                                    "op3",
                                    "Installment Premium With Applicable Tax is Rs. "
                                            + currencyFormat.format(Double.parseDouble(prsObj.parseXmlTag(
                                            retVal.toString(),
                                            "instalmntPremWTServcTax"))));
                            i.putExtra(
                                    "op4",
                                    "Guaranteed Vesting Benefit is Rs. "
                                            + currencyFormat.format(Double.parseDouble(prsObj
                                            .parseXmlTag(
                                                    retVal.toString(),
                                                    "guarnVestngBen"))));

                            if (cb_bi_saral_pension_ptr_rider.isChecked()) {
                                i.putExtra(
                                        "op5",
                                        "Rider Premium Excluding Applicable Tax is Rs. "
                                                + currencyFormat.format(Double.parseDouble(prsObj
                                                .parseXmlTag(retVal
                                                                .toString(),
                                                        "riderPrem"))));
                            }
                            i.putExtra(
                                    "op6",
                                    "Non-Guaranteed Vesting Benefit with 4%pa is Rs. "
                                            + currencyFormat.format(Double.parseDouble(prsObj
                                            .parseXmlTag(retVal.toString(), "annuityPayout_4_Pr"))));
                            i.putExtra(
                                    "op7",
                                    "Non-Guaranteed Vesting Benefit with 8%pa is Rs. "
                                            + currencyFormat.format(Double.parseDouble(prsObj
                                            .parseXmlTag(retVal.toString(), "annuityPayout_8_Pr"))));

                            i.putExtra("header", "SBI Life - Saral Retirement Saver");
                            i.putExtra("header1", "(UIN : 111N088V03)");

                            startActivity(i);

                        } else
                            Dialog();
                    }
                }

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

    }

    protected void addListenerOnSubmit() {
        saralPensionBean = new SaralPensionBean();
        saralPensionBean.setAnnuityOption(spnr_bi_Matuirty_option.getSelectedItem().toString());

        if (cb_staffdisc.isChecked()) {
            saralPensionBean.setStaffDisc(true);
        } else {
            saralPensionBean.setStaffDisc(false);
        }

        if (cb_kerladisc.isChecked()) {
            saralPensionBean.setKerlaDisc(true);
        } else {
            saralPensionBean.setKerlaDisc(false);
        }

        if (cb_bi_saral_pension_JKResident.isChecked()) {
            saralPensionBean.setJKResident(true);
        } else {
            saralPensionBean.setJKResident(false);
        }

        saralPensionBean.setAge(Integer
                .parseInt(edt_bi_saral_pension_life_assured_age.getText()
                        .toString()));
        saralPensionBean.setGender(spnr_bi_saral_pension_selGender
                .getSelectedItem().toString());

        saralPensionBean.setBasicTerm(Integer
                .parseInt(spnr_bi_saral_pension_policyterm.getSelectedItem()
                        .toString()));

        saralPensionBean.setVestingAge((Integer.parseInt(edt_bi_saral_pension_life_assured_age.getText().toString())
                + Integer.parseInt(spnr_bi_saral_pension_policyterm.getSelectedItem().toString())));

        saralPensionBean.setPremFreq(spnr_bi_saral_pension_premium_frequency
                .getSelectedItem().toString());

        saralPensionBean.setBasicSA(Double
                .parseDouble(edt_bi_saral_pension_sum_assured_amount.getText()
                        .toString()));

        // Ptr
        if (cb_bi_saral_pension_ptr_rider.isChecked()) {
            saralPensionBean.setPtrSA(Integer
                    .parseInt(edt_bi_saral_pension_ptr_rider_sum_assured
                            .getText().toString()));

        } else {
            saralPensionBean.setPtrSA(0);
        }

        saralPensionBean.setPtrTerm(Integer
                .parseInt(spnr_bi_saral_pension_ptr_rider_term
                        .getSelectedItem().toString()));

        saralPensionBean.setCb_ptrRider(cb_bi_saral_pension_ptr_rider
                .isChecked());
        saralPensionBean.setPTR_Status(cb_bi_saral_pension_ptr_rider
                .isChecked());


        showSaralPensionOutputPg(saralPensionBean);

    }

    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        // TODO Auto-generated method stub
        if (v.getId() == edt_bi_saral_pension_life_assured_first_name.getId()) {
            setFocusable(edt_bi_saral_pension_life_assured_middle_name);
            edt_bi_saral_pension_life_assured_middle_name.requestFocus();
        } else if (v.getId() == edt_bi_saral_pension_life_assured_middle_name
                .getId()) {
            setFocusable(edt_bi_saral_pension_life_assured_last_name);
            edt_bi_saral_pension_life_assured_last_name.requestFocus();
        } else if (v.getId() == edt_bi_saral_pension_life_assured_last_name
                .getId()) {
            setFocusable(btn_bi_saral_pension_life_assured_date);
            btn_bi_saral_pension_life_assured_date.requestFocus();
        } else if (v.getId() == edt_saral_pension_contact_no.getId()) {
            // clearFocusable(premiumAmt);
            setFocusable(edt_saral_pension_Email_id);
            edt_saral_pension_Email_id.requestFocus();
        } else if (v.getId() == edt_saral_pension_Email_id.getId()) {
            // clearFocusable(premiumAmt);
            setFocusable(edt_saral_pension_ConfirmEmail_id);
            edt_saral_pension_ConfirmEmail_id.requestFocus();
        } else if (v.getId() == edt_saral_pension_ConfirmEmail_id.getId()) {
            // clearFocusable(premiumAmt);
            clearFocusable(spnr_bi_saral_pension_policyterm);
            setFocusable(spnr_bi_saral_pension_policyterm);
            spnr_bi_saral_pension_policyterm.requestFocus();
        } else if (v.getId() == edt_bi_saral_pension_sum_assured_amount.getId()) {
            commonMethods.hideKeyboard(edt_bi_saral_pension_sum_assured_amount, context);
            clearFocusable(cb_bi_saral_pension_ptr_rider);
            setFocusable(cb_bi_saral_pension_ptr_rider);
            cb_bi_saral_pension_ptr_rider.requestFocus();

        } else if (v.getId() == edt_bi_saral_pension_ptr_rider_sum_assured
                .getId()) {
            commonMethods.hideKeyboard(edt_bi_saral_pension_ptr_rider_sum_assured, context);
        }

        return true;
    }

    // private void getBasicDetail() {
    // List<M_basicDetail> list_BasicDetail = db
    // .getBasicDetails(QuatationNumber);
    //
    // int i = 0;
    // list_BasicDetail = db.getBasicDetails(QuatationNumber);
    // if (list_BasicDetail.size() > 0) {
    // flagFocus = false;
    //
    // edt_saral_pension_contact_no.setText(list_BasicDetail.get(i)
    // .getMobileNo());
    // edt_saral_pension_Email_id.setText(list_BasicDetail.get(i)
    // .getEmailId());
    // edt_saral_pension_ConfirmEmail_id.setText(list_BasicDetail.get(i)
    // .getEmailId());
    //
    // photoByteArrayAsString = list_BasicDetail.get(i).getPhoto();
    // if (!photoByteArrayAsString.equals("")
    // && photoByteArrayAsString != null) {
    // byte[] photoByteArray = Base64
    // .decode(photoByteArrayAsString, 0);
    // Bitmap bitmap = BitmapFactory.decodeByteArray(photoByteArray,
    // 0, photoByteArray.length);
    // photoBitmap = bitmap;
    // }
    // flg_personalDetails = list_BasicDetail.get(i).getCreatedDate();
    // }
    //
    // }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

        boolean flagFocus = true;
        if (!flagFocus) {
            ll_bi_saral_pension_main.requestFocus();
        } else {
            spnr_bi_saral_pension_life_assured_title.requestFocus();
        }

    }

    // public long insertDataIntoDatabase() {
    //
    // String str_need_analysis_output = "";
    // String str_need_analysis_input = "";
    // if (NeedAnalysisActivity.outputlist != null) {
    // str_need_analysis_output = NeedAnalysisActivity.outputlist;
    // str_need_analysis_input = NeedAnalysisActivity.strValue;
    // } else {
    // str_need_analysis_output = "";
    // str_need_analysis_input = "";
    // }
    // M_Benefit_Illustration_Detail data = new M_Benefit_Illustration_Detail(
    // new String(inputVal), new String(retVal), proposal_no,
    // proposer_Is_Same_As_Life_Assured, proposer_Title,
    // proposer_First_Name, proposer_Middle_Name, proposer_Last_Name,
    // name_of_proposer, proposer_date_of_birth, lifeAssured_Title,
    // lifeAssured_First_Name, lifeAssured_Middle_Name,
    // lifeAssured_Last_Name, name_of_life_assured,
    // lifeAssured_date_of_birth, product_name, place1, place2, date1,
    // date2, agent_sign, proposer_sign,
    // proposer_Backdating_WishToBackDate_Policy,
    // proposer_Backdating_BackDate, flg_needAnalyis, createdBy,
    // createdDate, modifiedBy, modifiedDate, isSync, isFlag1,
    // isFlag2, isFlag3, isFlag4);
    // long count = 0;
    // count = db.insertBIDetail(data, QuatationNumber);
    //
    // List<M_basicDetail> dataBasicDetail = new ArrayList<M_basicDetail>();
    //
    // mobileNo = edt_saral_pension_contact_no.getText().toString();
    // emailId = edt_saral_pension_Email_id.getText().toString();
    // ConfirmEmailId = edt_saral_pension_ConfirmEmail_id.getText().toString();
    //
    // String photoByteArrayAsString = "";
    // if (photoBitmap != null) {
    // ByteArrayOutputStream stream = new ByteArrayOutputStream();
    // photoBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
    // byte[] photoByteArray = stream.toByteArray();
    // photoByteArrayAsString = Base64.encodeToString(photoByteArray,
    // Base64.DEFAULT);
    //
    // }
    //
    // Boolean isSync = false;
    // Boolean isFlag1 = true;
    // Boolean isFlag2 = true;
    // Boolean isFlag3 = true;
    // Boolean isFlag4 = true;
    //
    // if (proposer_Is_Same_As_Life_Assured.equalsIgnoreCase("y")) {
    // if (lifeAssured_Title.equalsIgnoreCase("Mr.")) {
    // propsoser_gender = "Male";
    //
    // } else if (lifeAssured_Title.equalsIgnoreCase("Ms.")) {
    // propsoser_gender = "Female";
    //
    // } else if (lifeAssured_Title.equalsIgnoreCase("Mrs.")) {
    // propsoser_gender = "Female";
    // }
    //
    // M_basicDetail model = new M_basicDetail(lifeAssured_Title,
    // lifeAssured_First_Name, lifeAssured_Last_Name,
    // lifeAssured_Middle_Name, name_of_life_assured,
    // propsoser_gender, lifeAssured_date_of_birth, mobileNo,
    // emailId, photoByteArrayAsString, str_need_analysis_output,
    // flg_personalDetails, str_need_analysis_input, modifiedDate,
    // isSync, isFlag1, isFlag2, isFlag3, isFlag4);
    // dataBasicDetail.add(model);
    // }
    //
    // try {
    // long rowId1 = db.insertBasicDetails(dataBasicDetail,
    // QuatationNumber);
    // } catch (Exception e) {
    // // TODO Auto-generated catch block
    // e.printStackTrace();
    // }
    //
    // return count;
    //
    // }

    private void getInput(SaralPensionBean saralPensionBean) {

        inputVal = new StringBuilder();
        // From GUI Input
        boolean staffDisc = saralPensionBean.getStaffDisc();
        boolean isPTRRider = saralPensionBean.getCb_ptrRider();
        int policyTerm = saralPensionBean.getBasicTerm();
        boolean isJKresident = saralPensionBean.getJkResident();

        String LifeAssured_title = spnr_bi_saral_pension_life_assured_title
                .getSelectedItem().toString();
        String LifeAssured_firstName = edt_bi_saral_pension_life_assured_first_name
                .getText().toString();
        String LifeAssured_middleName = edt_bi_saral_pension_life_assured_middle_name
                .getText().toString();
        String LifeAssured_lastName = edt_bi_saral_pension_life_assured_last_name
                .getText().toString();
        String LifeAssured_DOB = btn_bi_saral_pension_life_assured_date
                .getText().toString();
        String LifeAssured_age = edt_bi_saral_pension_life_assured_age
                .getText().toString();
        String LifeAssured_gender = spnr_bi_saral_pension_selGender
                .getSelectedItem().toString();

        String proposer_title = "";
        String proposer_firstName = "";
        String proposer_middleName = "";
        String proposer_lastName = "";
        String proposer_DOB = "";
        String proposer_age = "";
        String proposer_gender = "";

        inputVal.append("<?xml version='1.0' encoding='utf-8' ?><saralPensionBean>");
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
        inputVal.append("<isStaff>").append(staffDisc).append("</isStaff>");
        inputVal.append("<str_kerla_discount>" + str_kerla_discount + "</str_kerla_discount>");
        inputVal.append("<age>").append(edt_bi_saral_pension_life_assured_age.getText().toString()).append("</age>");
        inputVal.append("<isJKResident>").append(isJKresident).append("</isJKResident>");
        /*
         * inputVal.append("<gender>" +
         * spnr_bi_saral_pension_selGender.getSelectedItem().toString() +
         * "</gender>");
         */

        inputVal.append("<premFreqMode>").append(spnr_bi_saral_pension_premium_frequency.getSelectedItem()
                .toString()).append("</premFreqMode>");

        inputVal.append("<policyTerm>").append(policyTerm).append("</policyTerm>");

        double premiumAmount = saralPensionBean.getBasicSA();
        inputVal.append("<premiumAmount>").append(premiumAmount).append("</premiumAmount>");

        inputVal.append("<isPTRRider>").append(isPTRRider).append("</isPTRRider>");
        inputVal.append("<ptrTerm>").append(spnr_bi_saral_pension_ptr_rider_term.getSelectedItem()
                .toString()).append("</ptrTerm>");
        inputVal.append("<ptrSA>").append(edt_bi_saral_pension_ptr_rider_sum_assured.getText()
                .toString()).append("</ptrSA>");
        inputVal.append("<MATUIRTY_OPTION>" + spnr_bi_Matuirty_option.getSelectedItem().toString() + "</MATUIRTY_OPTION>");
        inputVal.append("<MATUIRTY_FREQUENCY>" + spnr_bi_Matuirty_frequency.getSelectedItem().toString() + "</MATUIRTY_FREQUENCY>");

        //Added Tushar Kadam Kerla Applicable Tax 7-Jun-2019
        String str_kerla_discount = "N";
        if (cb_kerladisc.isChecked()) {
            str_kerla_discount = "Y";
        }

        inputVal.append("<KFC>" + str_kerla_discount + "</KFC>");
        //End Added Tushar Kadam Kerla Applicable Tax 7-Jun-2019

        inputVal.append("</saralPensionBean>");

    }

    private void Dialog() {

        final Dialog d = new Dialog(this);
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);
        d.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.BLACK));

        d.setContentView(R.layout.layout_saral_pension_bi_grid);

        TextView tv_premium_tag = (TextView) d
                .findViewById(R.id.tv_premium_tag);

        TextView tv_proposername = d
                .findViewById(R.id.tv_proposername);
        TextView tv_proposal_number = d
                .findViewById(R.id.tv_proposal_number);
        TextView tv_bi_is_Staff = d
                .findViewById(R.id.tv_bi_is_Staff);
        TextView tv_bi_saral_pension_life_assured_state = (TextView) d
                .findViewById(R.id.tv_bi_saral_pension_life_assured_state);

        TextView tv_bi_is_jk = d.findViewById(R.id.tv_bi_is_jk);

        // Plan details
        TextView tv_bi_saral_pension_age_entry = d
                .findViewById(R.id.tv_bi_saral_pension_age_entry);
        TextView tv_bi_saral_pension_life_assured_age_entry = (TextView) d
                .findViewById(R.id.tv_bi_saral_pension_life_assured_age_entry);
        TextView tv_bi_saral_pension_vesting_age = (TextView) d
                .findViewById(R.id.tv_bi_saral_pension_vesting_age);
        TextView tv_bi_saral_pension_life_assured_gender = d
                .findViewById(R.id.tv_bi_saral_pension_life_assured_gender);
        TextView tv_bi_saral_pension_life_assured_gender2 = (TextView) d
                .findViewById(R.id.tv_bi_saral_pension_life_assured_gender2);
        TextView tv_bi_saral_pension_policy_term = (TextView) d
                .findViewById(R.id.tv_bi_saral_pension_policy_term);
        TextView tv_bi_saral_pension_regular_premium = d
                .findViewById(R.id.tv_bi_saral_pension_regular_premium);
        TextView tv_bi_saral_pension_sum_assured_main = d
                .findViewById(R.id.tv_bi_saral_pension_sum_assured_main);
        TextView tv_sum_assured_on_death = (TextView) d
                .findViewById(R.id.tv_sum_assured_on_death);

        TextView tv_rider_premium = (TextView) d
                .findViewById(R.id.tv_rider_premium);


        TextView tv_saral_pension_rate_of_taxes = (TextView) d
                .findViewById(R.id.tv_saral_pension_rate_of_taxes);
        TextView tv_bi_saral_pension_mode = (TextView) d
                .findViewById(R.id.tv_bi_saral_pension_premium_frequency_mode);
        TextView tv_bi_saral_pension_premium_paying_term = d
                .findViewById(R.id.tv_bi_saral_pension_premium_paying_term);
        TextView tv_bi_saral_pension_base_premium_excluding_servicetax = d
                .findViewById(R.id.tv_bi_saral_pension_base_premium_excluding_servicetax);
        TextView tv_saral_pension_base_plan = (TextView) d
                .findViewById(R.id.tv_saral_pension_base_plan);
        TextView tv_saral_pension_total_premium = (TextView) d
                .findViewById(R.id.tv_saral_pension_total_premium);

        // Ptr rider
        LinearLayout ll_basic_rider = d
                .findViewById(R.id.ll_basic_rider);
        LinearLayout ll_basic_ptr_rider = d
                .findViewById(R.id.ll_basic_ptr_rider);

        TextView tv_bi_saral_pension_premium_paying_term_ptr = d
                .findViewById(R.id.tv_bi_saral_pension_premium_paying_term_ptr);
        TextView tv_bi_saral_pension_premium_paying_term_ptr2 = (TextView) d
                .findViewById(R.id.tv_bi_saral_pension_premium_paying_term_ptr2);
        TextView tv_bi_saral_pension_sum_assured_ptr = (TextView) d
                .findViewById(R.id.tv_bi_saral_pension_sum_assured_ptr);
        TextView tv_bi_saral_pension_premium_mode_ptr = d
                .findViewById(R.id.tv_bi_saral_pension_premium_mode_ptr);
        TextView tv_bi_saral_pension_rider_premium_ptr = d
                .findViewById(R.id.tv_bi_saral_pension_rider_premium_ptr);

        TextView tv_basic_rider = (TextView) d
                .findViewById(R.id.tv_basic_rider);


        // Assured benefit

        TextView tv_bi_saral_pension_assured_benefit_guarantee_vesting = d
                .findViewById(R.id.tv_bi_saral_pension_assured_benefit_guarantee_vesting);

        // Total premium for base product
        TextView tv_saral_pension_tpbp_installment_premium_frequency_mode = d
                .findViewById(R.id.tv_saral_pension_tpbp_installment_premium_frequency_mode);
        TextView tv_saral_pension_tpbp_installment_premium_premium = d
                .findViewById(R.id.tv_saral_pension_tpbp_installment_premium_premium);
        TextView tv_saral_pension_tpbp_service_tax_frequency_mode = d
                .findViewById(R.id.tv_saral_pension_tpbp_service_tax_frequency_mode);
        TextView tv_saral_pension_tpbp_service_tax_premium = d
                .findViewById(R.id.tv_saral_pension_tpbp_service_tax_premium);
        TextView tv_saral_pension_tpbp_installment_premium_with_service_tax_frequency_mode = d
                .findViewById(R.id.tv_saral_pension_tpbp_installment_premium_with_service_tax_frequency_mode);
        TextView tv_saral_pension_tpbp_installment_premium_with_service_tax_premium = d
                .findViewById(R.id.tv_saral_pension_tpbp_installment_premium_with_service_tax_premium);

        TextView tv_bi_saral_pension_prospect_name = (TextView) d
                .findViewById(R.id.tv_bi_saral_pension_prospect_name);
        TextView tv_bi_saral_pension_life_assured_name = (TextView) d
                .findViewById(R.id.tv_bi_saral_pension_life_assured_name);

        // FY and SY table
        TextView tv_premium_policy_year_fy_sy1 = d
                .findViewById(R.id.tv_premium_policy_year_fy_sy1);
        TextView tv_premium_policy_year_fy_sy2 = d
                .findViewById(R.id.tv_premium_policy_year_fy_sy2);

        // First year policy
        TextView tv_bi_saral_pension_basic_premium_first_year = d
                .findViewById(R.id.tv_bi_saral_pension_basic_premium_first_year);

        TextView tv_bi_saral_pension_yearly_premium_with_tax_first_year = d
                .findViewById(R.id.tv_bi_saral_pension_premium_with_tax_first_year);


//        TextView tv_bi_saral_pension_total_service_tax_first_year = d
//                .findViewById(R.id.tv_bi_saral_pension_total_service_tax_first_year);
//
//        // Seconf year policy onwards
//        TextView tv_bi_saral_pension_seconnd_year_heading = d
//                .findViewById(R.id.tv_bi_saral_pension_seconnd_year_heading);

        TextView tv_bi_saral_pension_basic_premium_second_year = d
                .findViewById(R.id.tv_bi_saral_pension_basic_premium_second_year);


        TextView tv_bi_saral_pension_yearly_premium_with_tax_second_year = d
                .findViewById(R.id.tv_bi_saral_pension_premium_with_tax_second_year);

        final TextView tv_saral_pension_distribution_channel = (TextView) d
                .findViewById(R.id.tv_saral_pension_distribution_channel);
        tv_saral_pension_distribution_channel.setText(userType);
//        TextView tv_bi_saral_pension_total_service_tax_second_year = d
//                .findViewById(R.id.tv_bi_saral_pension_total_service_tax_second_year);

        // Illustrative benefit on vesting
        TextView tv_illustrative_benefit_on_vesting_policy_year = d
                .findViewById(R.id.tv_illustrative_benefit_on_vesting_policy_year);
        TextView tv_illustrative_benefit_on_vesting_age = d
                .findViewById(R.id.tv_illustrative_benefit_on_vesting_age);
        TextView tv_illustrative_benefit_on_vesting_tbpp_during_the_year = d
                .findViewById(R.id.tv_illustrative_benefit_on_vesting_tbpp_during_the_year);
        TextView tv_illustrative_benefit_on_vesting_tbpp_cumulative = d
                .findViewById(R.id.tv_illustrative_benefit_on_vesting_tbpp_cumulative);
        TextView tv_illustrative_benefit_on_vesting_guaranteed_vesting_benefit = d
                .findViewById(R.id.tv_illustrative_benefit_on_vesting_guaranteed_vesting_benefit);
        TextView tv_illustrative_benefit_on_vesting_bonuses_nonguaranteed_benefit_4pa_1 = d
                .findViewById(R.id.tv_illustrative_benefit_on_vesting_bonuses_nonguaranteed_benefit_4pa_1);
//        TextView tv_illustrative_benefit_on_vesting_bonuses_nonguaranteed_benefit_8pa_1 = d
//                .findViewById(R.id.tv_illustrative_benefit_on_vesting_bonuses_nonguaranteed_benefit_8pa_1);
//        TextView tv_illustrative_benefit_on_vesting_vwsting_higher_of_guaranteed_nonguaranteed_benefit_4pa = d
//                .findViewById(R.id.tv_illustrative_benefit_on_vesting_vwsting_higher_of_guaranteed_nonguaranteed_benefit_4pa);
//        TextView tv_illustrative_benefit_on_vesting_vwsting_higher_of_guaranteed_nonguaranteed_benefit_8pa = d
//                .findViewById(R.id.tv_illustrative_benefit_on_vesting_vwsting_higher_of_guaranteed_nonguaranteed_benefit_8pa);
        TextView tv_illustrative_benefit_on_vesting_bonuses_nonguaranteed_benefit_4pa_2 = d
                .findViewById(R.id.tv_illustrative_benefit_on_vesting_bonuses_nonguaranteed_benefit_4pa_2);
        TextView tv_illustrative_benefit_on_vesting_bonuses_nonguaranteed_benefit_8pa_2 = d
                .findViewById(R.id.tv_illustrative_benefit_on_vesting_bonuses_nonguaranteed_benefit_8pa_2);

        TextView tv_illustrative_benefits_of_vesting_annuity_rate = d
                .findViewById(R.id.tv_illustrative_benefits_of_vesting_annuity_rate);

        TableRow tr_second_year = (TableRow) d
                .findViewById(R.id.tr_second_year);

        final TextView edt_proposer_name = d
                .findViewById(R.id.edt_ProposalName);
        final EditText edt_Policyholderplace = d
                .findViewById(R.id.edt_Policyholderplace);

        GridView gv_userinfo_death = d
                .findViewById(R.id.gv_userinfo_death);
        GridView gv_userinfo_surrender = d
                .findViewById(R.id.gv_userinfo_surrender);

        GridView gv_userinfo_new = (GridView) d.findViewById(R.id.gv_userinfo_new);

        final CheckBox cb_statement = (CheckBox) d
                .findViewById(R.id.cb_statement);
        cb_statement.setChecked(false);

        TextView tv_bi_saral_pension_basic_service_tax_first_year = (TextView) d
                .findViewById(R.id.tv_bi_saral_pension_basic_service_tax_first_year);
        TextView tv_bi_saral_pension_swachh_bharat_cess_first_year = (TextView) d
                .findViewById(R.id.tv_bi_saral_pension_swachh_bharat_cess_first_year);
        TextView tv_bi_saral_pension_krishi_kalyan_cess_first_year = (TextView) d
                .findViewById(R.id.tv_bi_saral_pension_krishi_kalyan_cess_first_year);

        TextView tv_bi_saral_pension_basic_service_tax_second_year = (TextView) d
                .findViewById(R.id.tv_bi_saral_pension_basic_service_tax_second_year);
        TextView tv_bi_saral_pension_swachh_bharat_cess_second_year = (TextView) d
                .findViewById(R.id.tv_bi_saral_pension_swachh_bharat_cess_second_year);
        TextView tv_bi_saral_pension_krishi_kalyan_cess_second_year = (TextView) d
                .findViewById(R.id.tv_bi_saral_pension_krishi_kalyan_cess_second_year);

        TextView tv_grid_annuity_option = (TextView) d
                .findViewById(R.id.tv_grid_annuity_option);


        /* Need Analysis */
        final TextView edt_proposer_name_need_analysis = d
                .findViewById(R.id.edt_proposer_name_need_analysis);

        final CheckBox cb_statement_need_analysis = d
                .findViewById(R.id.cb_statement_need_analysis);
        cb_statement_need_analysis.setChecked(true);
        TableRow tr_need_analysis = d
                .findViewById(R.id.tr_need_analysis);

        /* parivartan chnages */
        imageButtonSaralPensionProposerPhotograph = d
                .findViewById(R.id.imageButtonSaralPensionProposerPhotograph);
        imageButtonSaralPensionProposerPhotograph
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
        list_data_new.clear();

        // getValueFromDatabase();

        tv_bi_saral_pension_prospect_name.setText(name_of_life_assured);
        tv_bi_saral_pension_life_assured_name.setText(name_of_life_assured);
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
                            + ", have undergone the Need Analysis after having reviewed the SBI Life Product options, I have opted for SBI LIFE - Saral Retirement Saver.");

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
                            + ", have undergone the Need Analysis after having reviewed the SBI Life Product options, I have opted for SBI LIFE - Saral Retirement Saver.");

            tv_proposername.setText(name_of_proposer);
        }
        tv_proposal_number.setText(QuatationNumber);

        // M_ChannelDetails list_channel_detail = db.getChannelDetail(sr_Code);
        //
        // String str_cif_city =
        // list_channel_detail.getDistanceMarketing_Flag();
        //
        // if (str_cif_city == null) {
        // str_cif_city = "";
        // }
        // if (place2.equals("")) {
        // edt_Policyholderplace.setText(str_cif_city);
        //
        // } else {
        // edt_Policyholderplace.setText(place2);
        // }
        // edt_MarketingOfficalPlace.setText(place1);
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
        /* Changes Done By Machindra */
        // List<M_Terms_And_Condition> TC_data = db
        // .get_TermsAndCondition(QuatationNumber);
        // int j = 0;
        // String str_sign_of_proposer = "";
        // if (TC_data.size() > 0) {
        // str_sign_of_proposer = TC_data.get(j).getSignature_of_proposer();
        //
        // }
        //
        // if (str_sign_of_proposer != null && !str_sign_of_proposer.equals(""))
        // {
        // byte[] signByteArray = Base64.decode(str_sign_of_proposer, 0);
        // Bitmap bitmap = BitmapFactory.decodeByteArray(signByteArray, 0,
        // signByteArray.length);
        // Ibtn_signatureofPolicyHolders.setImageBitmap(bitmap);
        // }
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
            imageButtonSaralPensionProposerPhotograph
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
                            imageButtonSaralPensionProposerPhotograph
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

            public void onClick(View v) {
                // if (frmProductHome.equals("FALSE")) {
                name_of_person = edt_proposer_name.getText().toString();
                // place1 = edt_MarketingOfficalPlace.getText().toString();
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
								.isChecked() == true)*/) && radioButtonTrasactionModeParivartan
                        .isChecked()) || radioButtonTrasactionModeManual
                        .isChecked())) {
                    NeedAnalysisActivity.str_need_analysis = "";

                    String productCode = "SRPEN";

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
                                            : sumAssured))),
                            obj.getRound(installmentPremiumWithserviceTax),
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
                                    .valueOf((sumAssured.equals("") || sumAssured == null) ? "0"
                                            : sumAssured))),
                            obj.getRound(installmentPremiumWithserviceTax),
                            agentEmail, agentMobile, na_input, na_output,
                            premPayingMode, Integer.parseInt(policyTerm), 0,
                            productCode, getDate(lifeAssured_date_of_birth),
                            "", "", mode, inputVal
                            .toString(), retVal.toString().replace(
                            bussIll.toString(), "")));

                    CreateSaralPensionBIPdf();

                    NABIObj.serviceHit(BI_SaralPensionActivity.this,
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
                    // parivartan chnages
                    else if (!radioButtonTrasactionModeParivartan.isChecked()
                            && !radioButtonTrasactionModeManual.isChecked()) {
                        commonMethods.dialogWarning(context, "Please Select Transaction Mode", true);
                        setFocusable(linearlayoutTrasactionModeParivartan);
                        linearlayoutTrasactionModeParivartan.requestFocus();
                    } else if (photoBitmap == null) {
                        commonMethods.dialogWarning(context, "Please Capture the Photo", true);
                        setFocusable(imageButtonSaralPensionProposerPhotograph);
                        imageButtonSaralPensionProposerPhotograph
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

        input = inputVal.toString();
        output = retVal.toString();

        String staffdiscount = prsObj.parseXmlTag(input, "isStaff");
        premPayingMode = prsObj.parseXmlTag(input, "premFreqMode");
        if (staffdiscount.equalsIgnoreCase("true")) {
            tv_bi_is_Staff.setText("Yes");
        } else {
            tv_bi_is_Staff.setText("No");
        }
        policy_Frequency = prsObj.parseXmlTag(input, "premFreqMode");
        if (cb_kerladisc.isChecked()) {
            tv_bi_saral_pension_life_assured_state.setText("Kerala");


        } else {
            tv_bi_saral_pension_life_assured_state.setText("Non Kerala");
        }

        str_kerla_discount = prsObj.parseXmlTag(input, "str_kerla_discount");
        if (str_kerla_discount.equalsIgnoreCase("Yes")) {
            if (policy_Frequency.equals("Single")) {
                tv_saral_pension_rate_of_taxes.setText("4.75%");
            } else {
                tv_saral_pension_rate_of_taxes.setText(" 4.75% in the 1st policy year and 2.375% from 2nd policy year onwards");
            }
        } else {
            if (policy_Frequency.equals("Single")) {
                tv_saral_pension_rate_of_taxes.setText("4.50%");
            } else {
                tv_saral_pension_rate_of_taxes.setText("4.5% in the 1st policy year and 2.25% from 2nd policy year onwards");
            }
        }

        tv_grid_annuity_option.setText(spnr_bi_Matuirty_option.getSelectedItem().toString());


//		isJkResident = prsObj.parseXmlTag(input, "isJKResident");
//
//        if (isJkResident.equalsIgnoreCase("true")) {
//            tv_bi_is_jk.setText("Yes");
//        } else {
//            tv_bi_is_jk.setText("No");
//        }

        // plan details
        age_entry = prsObj.parseXmlTag(input, "age");
        tv_bi_saral_pension_age_entry.setText(age_entry);
        tv_bi_saral_pension_life_assured_age_entry.setText(age_entry);

        vestingAge = prsObj.parseXmlTag(output, "vestingAge");
        tv_bi_saral_pension_vesting_age.setText(vestingAge + " Years");

        gender = prsObj.parseXmlTag(input, "gender");
        tv_bi_saral_pension_life_assured_gender.setText(gender);
        tv_bi_saral_pension_life_assured_gender2.setText(gender);

        policy_Frequency = prsObj.parseXmlTag(input, "premFreqMode");
        tv_bi_saral_pension_mode.setText(policy_Frequency);

        policyTerm = prsObj.parseXmlTag(input, "policyTerm");
        tv_bi_saral_pension_policy_term.setText(policyTerm + " Years");

        String regularPremium = prsObj.parseXmlTag(input, "premFreqMode");
        // tv_bi_saral_pension_regular_premium.setText(policy_Frequency);
        if (regularPremium.equalsIgnoreCase("Single")) {
            tv_bi_saral_pension_regular_premium.setText("Single");
            tv_premium_tag.setText("Single Premium");
        } else {
            tv_bi_saral_pension_regular_premium.setText("Regular");
            tv_premium_tag.setText("Annualized Premium");
        }

        sumAssured = prsObj.parseXmlTag(input, "premiumAmount");
        tv_bi_saral_pension_sum_assured_main.setText(currencyFormat
                .format(Double.parseDouble(sumAssured)));

        sumAssuredDeath = prsObj.parseXmlTag(output, "guaranDeathBenfit" + "1" + "");
        tv_sum_assured_on_death.setText(sumAssuredDeath);

        premiumPayingTerm = prsObj.parseXmlTag(input, "policyTerm");
        if (policy_Frequency.equalsIgnoreCase("Single")) {
            tv_bi_saral_pension_premium_paying_term.setText("1");
        } else {
            tv_bi_saral_pension_premium_paying_term.setText(premiumPayingTerm);
        }

        basePremiumExcludingServiceTax = prsObj.parseXmlTag(output, "basePremExcludngST");
        premiumSingleInstBasicRider = prsObj.parseXmlTag(output, "premiumSingleInstBasicRider");
        tv_bi_saral_pension_base_premium_excluding_servicetax.setText(basePremiumExcludingServiceTax);
        tv_saral_pension_base_plan.setText(basePremiumExcludingServiceTax);
        intallmentPremWithST = prsObj.parseXmlTag(output, "intallmentPremWithST");
        tv_saral_pension_total_premium.setText(intallmentPremWithST);
        tv_basic_rider.setText(premiumSingleInstBasicRider);

        // Total premium for base product
        tv_saral_pension_tpbp_installment_premium_frequency_mode
                .setText(policy_Frequency);
        String installmentPremium = prsObj.parseXmlTag(output, "installmntPrem");
        tv_saral_pension_tpbp_installment_premium_premium
                .setText(installmentPremium);

        tv_saral_pension_tpbp_service_tax_frequency_mode
                .setText(policy_Frequency);
        String serviceTax = prsObj.parseXmlTag(output, "servceTax");
        tv_saral_pension_tpbp_service_tax_premium.setText(serviceTax);

        tv_saral_pension_tpbp_installment_premium_with_service_tax_frequency_mode
                .setText(policy_Frequency);
        installmentPremiumWithserviceTax = prsObj.parseXmlTag(output,
                "instalmntPremWTServcTax");
        tv_saral_pension_tpbp_installment_premium_with_service_tax_premium
                .setText(installmentPremiumWithserviceTax);

        // Fy and Sy

		/*tv_premium_policy_year_fy_sy1.setText(policy_Frequency
                + " Premium (Rs )");
        tv_premium_policy_year_fy_sy2.setText(policy_Frequency
				+ " Premium with Applicable Taxes(Rs )");*/
        premInstBasicFirstYear = prsObj.parseXmlTag(output, "premInstBasicFirstYear");
        tv_bi_saral_pension_basic_premium_first_year.setText(premInstBasicFirstYear);
        // tv_bi_saral_pension_service_tax_first_year.setText(serviceTax);
        instalmntPremWTServcTax = prsObj.parseXmlTag(output, "instalmntPremWTServcTax");
        tv_bi_saral_pension_yearly_premium_with_tax_first_year.setText(instalmntPremWTServcTax);

        SYservceTax = prsObj.parseXmlTag(output, "SYservceTax");
        SYinstalmntPremWTServcTax = prsObj.parseXmlTag(output,
                "SYinstalmntPremWTServcTax");

        // Amit changes start- 23-5-2016
        // basicServiceTax = prsObj.parseXmlTag(output, "basicServiceTax");
        basicServiceTax = prsObj.parseXmlTag(output, "servceTax");
        premInstFYRider = prsObj.parseXmlTag(output, "premInstFYRider");

        tv_bi_saral_pension_basic_service_tax_first_year.setText(""
                + obj.getRound(obj.getStringWithout_E(Double
                .valueOf(premInstFYRider.equals("") ? "0"
                        : premInstFYRider))));

        //SBCServiceTax = prsObj.parseXmlTag(output, "SBCServiceTax");

		/*tv_bi_saral_pension_swachh_bharat_cess_first_year
				.setText(""
						+ obj.getRound(obj.getStringWithout_E(Double
								.valueOf(SBCServiceTax.equals("") ? "0"
										: SBCServiceTax))));*/

        //KKCServiceTax = prsObj.parseXmlTag(output, "KKCServiceTax");

		/*tv_bi_saral_pension_krishi_kalyan_cess_first_year
				.setText(""
						+ obj.getRound(obj.getStringWithout_E(Double
								.valueOf(KKCServiceTax.equals("") ? "0"
										: KKCServiceTax))));*/
        // Amit changes end- 23-5-2016

        tr_second_year.setVisibility(View.GONE);
        premInstBasicSecondYear = prsObj.parseXmlTag(output, "premInstBasicSecondYear");

        if (!saralPensionBean.getPremFreq().equalsIgnoreCase("Single")) {
            tr_second_year.setVisibility(View.VISIBLE);
            tv_bi_saral_pension_basic_premium_second_year.setText(premInstBasicSecondYear);
            // tv_bi_saral_pension_service_tax_second_year.setText(SYservceTax);
            tv_bi_saral_pension_yearly_premium_with_tax_second_year
                    .setText(SYinstalmntPremWTServcTax);

            // Amit changes start- 23-5-2016
            // basicServiceTaxSecondYear = prsObj.parseXmlTag(output,
            // "basicServiceTaxSecondYear");
            basicServiceTaxSecondYear = prsObj.parseXmlTag(output, "SYservceTax");
            premInstSYRider = prsObj.parseXmlTag(output, "premInstSYRider");

            tv_bi_saral_pension_basic_service_tax_second_year.setText(""
                    + obj.getRound(obj.getStringWithout_E(Double
                    .valueOf(premInstSYRider.equals("") ? "0"
                            : premInstSYRider))));













			/*SBCServiceTaxSecondYear = prsObj.parseXmlTag(output,
					"SBCServiceTaxSecondYear");*/

			/*tv_bi_saral_pension_swachh_bharat_cess_second_year.setText(""
					+ obj.getRound(obj.getStringWithout_E(Double
							.valueOf(SBCServiceTaxSecondYear.equals("") ? "0"
									: SBCServiceTaxSecondYear))));*/

			/*KKCServiceTaxSecondYear = prsObj.parseXmlTag(output,
                    "KKCServiceTaxSecondYear");

			tv_bi_saral_pension_krishi_kalyan_cess_second_year.setText(""
					+ obj.getRound(obj.getStringWithout_E(Double
							.valueOf(KKCServiceTaxSecondYear.equals("") ? "0"
									: KKCServiceTaxSecondYear))));*/
            // Amit changes end- 23-5-2016

        }

        nonGuaranVestingBenefit_4Percent = prsObj.parseXmlTag(output, "FundValueAtVesting4");


        nonGuaranVestingBenefit_4Percent = obj.getRound(obj.getStringWithout_E(Double
                .valueOf((nonGuaranVestingBenefit_4Percent.equals("") || nonGuaranVestingBenefit_4Percent == null) ? "0"
                        : nonGuaranVestingBenefit_4Percent)))
                + "";
        nonGuaranVestingBenefit_8Percent = prsObj.parseXmlTag(output, "FundValueAtVesting8");
        nonGuaranVestingBenefit_8Percent = obj.getRound(obj.getStringWithout_E(Double
                .valueOf((nonGuaranVestingBenefit_8Percent.equals("") || nonGuaranVestingBenefit_8Percent == null) ? "0"
                        : nonGuaranVestingBenefit_8Percent)))
                + "";
        guarnVestngBen = prsObj.parseXmlTag(output, "VestingMinAssBen");

        guarnVestngBen = obj.getRound(obj.getStringWithout_E(Double
                .valueOf((guarnVestngBen.equals("") || guarnVestngBen == null) ? "0"
                        : guarnVestngBen)))
                + "";
        annuityPayout_4_Pr = prsObj.parseXmlTag(output, "annuityPayout_4_Pr");
        annuityPayout_4_Pr = obj.getRound(obj.getStringWithout_E(Double
                .valueOf((annuityPayout_4_Pr.equals("") || annuityPayout_4_Pr == null) ? "0"
                        : annuityPayout_4_Pr)))
                + "";
        annuityPayout_8_Pr = prsObj.parseXmlTag(output, "annuityPayout_8_Pr");

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
        String str_prem_pay = "";
        TextView tv_Company_policy_surrender_dec = (TextView) d
                .findViewById(R.id.tv_Company_policy_surrender_dec);

        if (policy_Frequency.equalsIgnoreCase("Single")) {
            str_prem_pay = "Single";
            Company_policy_surrender_dec = "Your SBI Life - Saral Retirement Saver(UIN: 111N088V03) is a "
                    + str_prem_pay
                    + " premium policy and you are requied to pay One Time Premium is Rs. "
                    + installmentPremiumWithserviceTax
                    + " .Your Policy Term is "
                    + policyTerm
                    + " years"
                    + " and Basic Sum Assured is Rs. "
                    +

                    getformatedThousandString(Integer.parseInt(obj.getRound(obj
                            .getStringWithout_E(Double.valueOf(sumAssured
                                    .equals("") ? "0" : sumAssured)))));
        } else {
            str_prem_pay = "Regular";
            Company_policy_surrender_dec = "Your SBI Life - Saral Retirement Saver(UIN: 111N088V03) is a "
                    + str_prem_pay
                    + " premium policy, for which your first year "
                    + policy_Frequency
                    + " Premium of is "
                    + installmentPremiumWithserviceTax
                    + " .Your Policy Term is "
                    + policyTerm
                    + " years"
                    + " .Your Premium Payment Term is "
                    + policyTerm
                    + " years"
                    + " and Basic Sum Assured is Rs. "
                    +

                    getformatedThousandString(Integer.parseInt(obj.getRound(obj
                            .getStringWithout_E(Double.valueOf(sumAssured
                                    .equals("") ? "0" : sumAssured)))));
        }


        //tv_Company_policy_surrender_dec.setText(Company_policy_surrender_dec);

        // Illustrative benefit on vesting

        annuityPayout_4_Pr = currencyFormat.format(Double.parseDouble(prsObj
                .parseXmlTag(output, "annuityPayout_4_Pr")));
        tv_illustrative_benefit_on_vesting_bonuses_nonguaranteed_benefit_4pa_2
                .setText(annuityPayout_4_Pr);

        annuityPayout_8_Pr = currencyFormat.format(Double.parseDouble(prsObj
                .parseXmlTag(output, "annuityPayout_8_Pr")));
        tv_illustrative_benefit_on_vesting_bonuses_nonguaranteed_benefit_8pa_2
                .setText(annuityPayout_8_Pr);

        String premiumRate = prsObj.parseXmlTag(output, "premiumRate");

        tv_illustrative_benefits_of_vesting_annuity_rate
                .setText("*The annuity rate used is of Single Life Lifetime income options.This illustration is based on an annuity rate of "
                        + premiumRate
                        + " per INR 1000 vesting amount.We do not guarantee the annuity rates.The actual annuity rate may differ and may be lesser or higher than the one shown in the illustration.");

        // Assured Benefit

        guarnVestngBen_AssuredBenefit = prsObj.parseXmlTag(output,
                "guarnVestngBen");

        //
        // tv_bi_saral_pension_assured_benefit_guarantee_vesting
        // .setText(obj.getRoundUp(Double.parseDouble(prsObj.parseXmlTag(output,
        // "guarnVestngBen"))) +"");

        tv_bi_saral_pension_assured_benefit_guarantee_vesting
                .setText(currencyFormat.format(Double.parseDouble(prsObj
                        .parseXmlTag(output, "guarnVestngBen"))));

        // rider
        ptr_rider_status = prsObj.parseXmlTag(input, "isPTRRider");
        premPTRWithoutDisc = prsObj.parseXmlTag(output, "premPTRWithoutDisc");
        premiumSingleInstBasicRider = prsObj.parseXmlTag(output, "premiumSingleInstBasicRider");
        if (ptr_rider_status.equals("false")) {
            ll_basic_rider.setVisibility(View.GONE);

        } else {
            ll_basic_rider.setVisibility(View.VISIBLE);
            if (ptr_rider_status.equals("true")) {
                ll_basic_ptr_rider.setVisibility(View.VISIBLE);
                term_ptr = prsObj.parseXmlTag(input, "ptrTerm");
                sa_ptr = prsObj.parseXmlTag(input, "ptrSA");
                prem_ptr = prsObj.parseXmlTag(output, "riderPrem");

                tv_bi_saral_pension_premium_paying_term_ptr.setText(term_ptr
                        + "");
                if (policy_Frequency.equalsIgnoreCase("Single")) {
                    tv_bi_saral_pension_premium_paying_term_ptr2.setText("Single Premium");
                } else {
                    tv_bi_saral_pension_premium_paying_term_ptr2.setText(term_ptr
                            + "");
                }

                //tv_rider_premium.setText(premPTRWithoutDisc + "");
                tv_rider_premium.setText(prem_ptr + "");

                tv_bi_saral_pension_sum_assured_ptr.setText("Rs "
                        + obj.getRound(obj.getStringWithout_E(Double
                        .valueOf(sa_ptr.equals("") ? "0" : sa_ptr))));
                tv_bi_saral_pension_premium_mode_ptr.setText(policy_Frequency);

                tv_bi_saral_pension_rider_premium_ptr
                        .setText("Rs "
                                + obj.getRound(obj.getStringWithout_E(Double
                                .valueOf(prem_ptr.equals("") ? "0"
                                        : prem_ptr))));
            } else {
                ll_basic_ptr_rider.setVisibility(View.GONE);
            }
        }


        for (int i = 1; i <= Integer.parseInt(policyTerm); i++) {

            String end_of_year = prsObj
                    .parseXmlTag(output, "policyYr" + i + "");
            String total_base_premium = ((int) Double.parseDouble(prsObj
                    .parseXmlTag(output, "AnnulizedPremium" + i + ""))) + "";


            String Guaranteedadd = ((int) Double.parseDouble(prsObj
                    .parseXmlTag(output, "Guaranteedadd" + i + ""))) + "";


            String guaranteed_survival_benefit = ((int) Double
                    .parseDouble(prsObj.parseXmlTag(output, "SurvivalBenefit" + i
                            + "")))
                    + "";


            String guaranteed_surrender_value = ((int) Double
                    .parseDouble(prsObj.parseXmlTag(output, "guaranSurrBenefit"
                            + i + "")))
                    + "";


            String guarntdDeathBenft = ((int) Double.parseDouble(prsObj
                    .parseXmlTag(output, "guaranDeathBenfit" + i + ""))) + "";


            String MaturityBenefit = ((int) Double
                    .parseDouble(prsObj.parseXmlTag(output, "vestingBenefit" + i
                            + "")))
                    + "";


            String ReversionaryBonus4Per = ((int) Double
                    .parseDouble(prsObj.parseXmlTag(output,
                            "nonGuaranVestingBenefit_4Percent" + i + "")))
                    + "";

            String not_guaranteed_surrender_value_4per = ((int) Double
                    .parseDouble(prsObj.parseXmlTag(output,
                            "surrenderBenefit_4Pr" + i + "")))
                    + "";

            String ReversionaryBonus8Per = ((int) Double
                    .parseDouble(prsObj.parseXmlTag(output,
                            "nonGuaranVestingBenefit_8Percent" + i + "")))
                    + "";

            String not_guaranteed_surrender_value_8per = Double
                    .parseDouble(prsObj.parseXmlTag(output,
                            "surrenderBenefit_8Pr" + i + ""))
                    + "";


            String TotalMaturityBenefit4per = ((int) Double
                    .parseDouble(prsObj.parseXmlTag(output,
                            "totalMaturityBen4per" + i + "")))
                    + "";

            String TotalMaturityBenefit8per = ((int) Double
                    .parseDouble(prsObj.parseXmlTag(output,
                            "totalMaturityBen8per" + i + "")))
                    + "";

            String TotalDeathBenefit4per = ((int) Double
                    .parseDouble(prsObj.parseXmlTag(output,
                            "totDeathBen4per" + i + "")))
                    + "";

            String TotalDeathBenefit8per = ((int) Double
                    .parseDouble(prsObj.parseXmlTag(output,
                            "totDeathBen8per" + i + "")))
                    + "";

            list_data_new
                    .add(new M_BI_SaralPension_AdapterCommon(
                            end_of_year,
                            (obj.getRound(obj.getStringWithout_E(Double
                                    .valueOf(total_base_premium))))
                                    + "",
                            (obj.getRound(obj.getStringWithout_E(Double
                                    .valueOf(Guaranteedadd))))
                                    + "",
                            (obj.getRound(obj.getStringWithout_E(Double
                                    .valueOf(guaranteed_survival_benefit))))
                                    + "",
                            (obj.getRound(obj.getStringWithout_E(Double
                                    .valueOf(guaranteed_surrender_value))))
                                    + "",
                            (obj.getRound(obj.getStringWithout_E(Double
                                    .valueOf(guarntdDeathBenft))))
                                    + "",

                            (obj.getRound(obj.getStringWithout_E(Double
                                    .valueOf(MaturityBenefit))))
                                    + "",
                            (obj.getRound(obj.getStringWithout_E(Double
                                    .valueOf(ReversionaryBonus4Per))))
                                    + "",
                            "0",
                            (obj.getRound(obj.getStringWithout_E(Double
                                    .valueOf(not_guaranteed_surrender_value_4per))))
                                    + "",
                            (obj.getRound(obj.getStringWithout_E(Double
                                    .valueOf(ReversionaryBonus8Per))))
                                    + "",
                            "0",
                            (obj.getRound(obj.getStringWithout_E(Double
                                    .valueOf(not_guaranteed_surrender_value_8per))))
                                    + "",
                            (obj.getRound(obj.getStringWithout_E(Double
                                    .valueOf(TotalMaturityBenefit4per))))
                                    + "",
                            (obj.getRound(obj.getStringWithout_E(Double
                                    .valueOf(TotalMaturityBenefit8per))))
                                    + "",
                            (obj.getRound(obj.getStringWithout_E(Double
                                    .valueOf(TotalDeathBenefit4per))))
                                    + "",
                            (obj.getRound(obj.getStringWithout_E(Double
                                    .valueOf(TotalDeathBenefit8per))))
                                    + ""));

        }
        Adapter_BI_SaralPensionGridCommon adapter_new = new Adapter_BI_SaralPensionGridCommon(
                BI_SaralPensionActivity.this, list_data_new);
        gv_userinfo_new.setAdapter(adapter_new);

        GridHeight gh_new = new GridHeight();
        gh_new.getheight(gv_userinfo_new, policyTerm);


        for (int i = 1; i <= Integer.parseInt(policyTerm); i++) {

            String policyYear = prsObj.parseXmlTag(output, "policyYr" + i + "");
            //tv_illustrative_benefit_on_vesting_policy_year.setText(policyYear);

            String age = prsObj.parseXmlTag(output, "age" + i + "");
            // tv_illustrative_benefit_on_vesting_age.setText(age);

            String totalBasePremPaid = prsObj.parseXmlTag(output, "totalBasePremPaid"
                    + i + "");
            //tv_illustrative_benefit_on_vesting_tbpp_during_the_year  .setText(totalBasePremPaid);

            String cummTotBasePrem = currencyFormat.format(Double.parseDouble(prsObj
                    .parseXmlTag(output, "cummTotBasePrem" + i + "")));
//            tv_illustrative_benefit_on_vesting_tbpp_cumulative
//                    .setText(cummTotBasePrem);

            String guaranteeVestingBenefit = currencyFormat.format(Double
                    .parseDouble(prsObj.parseXmlTag(output, "vestingBenefit"
                            + i + "")));
//            tv_illustrative_benefit_on_vesting_guaranteed_vesting_benefit
//                    .setText(guaranteeVestingBenefit);

            String bonusesNonGuaranVestingBenefit_4Percent = currencyFormat
                    .format(Double.parseDouble(prsObj.parseXmlTag(output,
                            "nonGuaranVestingBenefit_4Percent" + i + "")));
//            tv_illustrative_benefit_on_vesting_bonuses_nonguaranteed_benefit_4pa_1
//                    .setText(bonusesNonGuaranVestingBenefit_4Percent);

            String bonusesNonGuaranVestingBenefit_8Percent = currencyFormat
                    .format(Double.parseDouble(prsObj.parseXmlTag(output,
                            "nonGuaranVestingBenefit_8Percent" + i + "")));
//            tv_illustrative_benefit_on_vesting_bonuses_nonguaranteed_benefit_8pa_1
//                    .setText(bonusesNonGuaranVestingBenefit_8Percent);

            String vestingBenefit_4_pr = currencyFormat.format(Double
                    .parseDouble(prsObj.parseXmlTag(output,
                            "vestingBenefit_4_pr" + i + "")));
//            tv_illustrative_benefit_on_vesting_vwsting_higher_of_guaranteed_nonguaranteed_benefit_4pa
//                    .setText(vestingBenefit_4_pr);

            String vestingBenefit_8_pr = currencyFormat.format(Double
                    .parseDouble(prsObj.parseXmlTag(output,
                            "vestingBenefit_8_pr" + i + "")));
//            tv_illustrative_benefit_on_vesting_vwsting_higher_of_guaranteed_nonguaranteed_benefit_8pa
//                    .setText(vestingBenefit_8_pr);

            // String policy_year = prsObj
            // .parseXmlTag(output, "policyYr" + i + "");
            String policy_year = String.valueOf(i);
            String guaranDeathBenfit = prsObj.parseXmlTag(output,
                    "guaranDeathBenfit" + i + "");
            String nonGuaranDeathBenfit_4Percent = prsObj.parseXmlTag(output,
                    "nonGuaranDeathBenfit_4Percent" + i + "");
            String nonGuaranDeathBenfit_8Percent = prsObj.parseXmlTag(output,
                    "nonGuaranDeathBenfit_8Percent" + i + "");
            String deathBenefit_4Percent = prsObj.parseXmlTag(output,
                    "deathBenefit_4Percent" + i + "");
            String deathBenefit_8Percent = prsObj.parseXmlTag(output,
                    "deathBenefit_8Percent" + i + "");

            list_data
                    .add(new M_BI_SaralPensionGrid_Adpter(
                            policy_year,
                            obj.getRound(obj.getStringWithout_E(Double
                                    .valueOf((guaranDeathBenfit.equals("") || guaranDeathBenfit == null) ? "0"
                                            : guaranDeathBenfit)))
                                    + "",
                            obj.getRound(obj.getStringWithout_E(Double.valueOf((nonGuaranDeathBenfit_4Percent
                                    .equals("") || nonGuaranDeathBenfit_4Percent == null) ? "0"
                                    : nonGuaranDeathBenfit_4Percent)))
                                    + "",
                            obj.getRound(obj.getStringWithout_E(Double.valueOf((nonGuaranDeathBenfit_8Percent
                                    .equals("") || nonGuaranDeathBenfit_8Percent == null) ? "0"
                                    : nonGuaranDeathBenfit_8Percent)))
                                    + "",
                            obj.getRound(obj.getStringWithout_E(Double
                                    .valueOf((deathBenefit_4Percent.equals("") || deathBenefit_4Percent == null) ? "0"
                                            : deathBenefit_4Percent)))
                                    + "",
                            obj.getRound(obj.getStringWithout_E(Double
                                    .valueOf((deathBenefit_8Percent.equals("") || deathBenefit_8Percent == null) ? "0"
                                            : deathBenefit_8Percent)))
                                    + ""));

        }

        Adapter_BI_SaralPensionGrid adapter = new Adapter_BI_SaralPensionGrid(
                BI_SaralPensionActivity.this, list_data);

        policyTerm = policyTerm.replaceAll("\\s+", "");

        gv_userinfo_death.setAdapter(adapter);

        GridHeight gh = new GridHeight();
        gh.getheight(gv_userinfo_death, policyTerm);

        for (int i = 1; i <= Integer.parseInt(policyTerm); i++) {
            // String policy_year = prsObj
            // .parseXmlTag(output, "policyYr" + i + "");
            String policy_year = String.valueOf(i);
            String guaranSurrBenefit = prsObj.parseXmlTag(output,
                    "guaranSurrBenefit" + i + "");
            String nonGuaranSurrBen_4Pr = prsObj.parseXmlTag(output,
                    "nonGuaranSurrBen_4Pr" + i + "");
            String nonGuaranSurrBen_8Pr = prsObj.parseXmlTag(output,
                    "nonGuaranSurrBen_8Pr" + i + "");
            String surrenderBenefit_4Pr = prsObj.parseXmlTag(output,
                    "surrenderBenefit_4Pr" + i + "");
            String surrenderBenefit_8Pr = prsObj.parseXmlTag(output,
                    "surrenderBenefit_8Pr" + i + "");

            list_data1
                    .add(new M_BI_SaralPensionGrid_Adpter(
                            policy_year,
                            obj.getRound(obj.getStringWithout_E(Double
                                    .valueOf((guaranSurrBenefit.equals("") || guaranSurrBenefit == null) ? "0"
                                            : guaranSurrBenefit)))
                                    + "",
                            obj.getRound(obj.getStringWithout_E(Double
                                    .valueOf((nonGuaranSurrBen_4Pr.equals("") || nonGuaranSurrBen_4Pr == null) ? "0"
                                            : nonGuaranSurrBen_4Pr)))
                                    + "",
                            obj.getRound(obj.getStringWithout_E(Double
                                    .valueOf((nonGuaranSurrBen_8Pr.equals("") || nonGuaranSurrBen_8Pr == null) ? "0"
                                            : nonGuaranSurrBen_8Pr)))
                                    + "",
                            obj.getRound(obj.getStringWithout_E(Double
                                    .valueOf((surrenderBenefit_4Pr.equals("") || surrenderBenefit_4Pr == null) ? "0"
                                            : surrenderBenefit_4Pr)))
                                    + "",
                            obj.getRound(obj.getStringWithout_E(Double
                                    .valueOf((surrenderBenefit_8Pr.equals("") || surrenderBenefit_8Pr == null) ? "0"
                                            : surrenderBenefit_8Pr)))
                                    + ""));

        }

        Adapter_BI_SaralPensionGrid adapter1 = new Adapter_BI_SaralPensionGrid(
                BI_SaralPensionActivity.this, list_data1);

        gv_userinfo_surrender.setAdapter(adapter1);

        gh.getheight(gv_userinfo_surrender, policyTerm);

        d.show();

    }

    private void CreateSaralPensionBIPdf() {

        // String quotation_Number = ProductHomePageActivity.quotation_Number;
        try {
            Font normal = new Font(Font.FontFamily.TIMES_ROMAN, 5, Font.NORMAL);
            Font headerBold = new Font(Font.FontFamily.TIMES_ROMAN, 11,
                    Font.BOLD, BaseColor.WHITE);
            Font small_bold = new Font(Font.FontFamily.TIMES_ROMAN, 8,
                    Font.BOLD);

            Font small_bold1 = new Font(Font.FontFamily.TIMES_ROMAN, 8,
                    Font.BOLD);

            Font small_normal = new Font(Font.FontFamily.TIMES_ROMAN, 8,
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
                            "Benefit Illustration for SBI LIFE - Saral Retirement Saver (UIN: 111N088V03)",
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
                    small_bold_for_name);
            para_address.setAlignment(Element.ALIGN_CENTER);
            Paragraph para_address1 = new Paragraph(
                    "Registered & Corporate Office: 'Natraj', M.V.Road and Western Express Highway Junction, Andheri (East), Mumbai - 400069 ",
                    small_bold);
            para_address1.setAlignment(Element.ALIGN_CENTER);
            Paragraph para_address2 = new Paragraph(
                    "IRDAI Registration No. 111 | Website: www.sbilife.co.in | Email: info@sbilife.co.in | CIN: L99999MH2000PLC129113",
                    small_bold);
            para_address2.setAlignment(Element.ALIGN_CENTER);
            Paragraph para_address3 = new Paragraph(
                    "Toll Free: 1800 267 9090 (Between 9.00 am & 9.00 pm) ",
                    small_bold);
            para_address3.setAlignment(Element.ALIGN_CENTER);
            Paragraph para_address4 = new Paragraph(
                    "Benefit Illustration (BI) : SBI Life -Saral Retirement Saver (UIN : 111N088V03) | An Individual, Non-Linked, Participating, Savings Pension Product",
                    small_bold);
            para_address4.setAlignment(Element.ALIGN_CENTER);

            document.add(para_address);
            document.add(para_address1);
            document.add(para_address2);
            document.add(para_address3);
            document.add(para_address4);

            document.add(para_img_logo_after_space_1);
            //document.add(headertable);
            //document.add(para_img_logo_after_space_1);

            document.add(para_img_logo_after_space_1);
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
                    QuatationNumber, small_bold1));
            ProposalNumber_cell_2.setHorizontalAlignment(Element.ALIGN_CENTER);
            PdfPCell NameofProposal_cell_3 = new PdfPCell(new Paragraph(
                    "Proposer Name ", small_normal));
            PdfPCell NameofProposal_cell_4 = new PdfPCell(new Paragraph(
                    name_of_proposer, small_bold1));
            NameofProposal_cell_4.setHorizontalAlignment(Element.ALIGN_CENTER);

            NameofProposal_cell_4.setVerticalAlignment(Element.ALIGN_CENTER);

            PdfPCell NameofProposal_cell_5 = new PdfPCell(new Paragraph(
                    "Channel / Intermediary : ", small_normal));
            PdfPCell NameofProposal_cell_6 = new PdfPCell(new Paragraph(
                    userType, small_bold1));
            NameofProposal_cell_5.setHorizontalAlignment(Element.ALIGN_CENTER);

            NameofProposal_cell_6.setVerticalAlignment(Element.ALIGN_CENTER);

            ProposalNumber_cell_1.setPadding(5);
            ProposalNumber_cell_2.setPadding(5);
            NameofProposal_cell_3.setPadding(5);
            NameofProposal_cell_4.setPadding(5);

            table_proposer_name.addCell(ProposalNumber_cell_1);
            table_proposer_name.addCell(ProposalNumber_cell_2);
            table_proposer_name.addCell(NameofProposal_cell_5);
            table_proposer_name.addCell(NameofProposal_cell_6);
            //table_proposer_name.addCell(NameofProposal_cell_3);
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
            document.add(BI_Pdftable2);

            PdfPTable BI_Pdftable21 = new PdfPTable(1);
            BI_Pdftable21.setWidthPercentage(100);
            PdfPCell BI_Pdftable2_cell11 = new PdfPCell(
                    new Paragraph(
                            "The main objective of the illustration is that the client is able to appreciate the features of the product and the flow of benefits in different circumstances with some level of quantification. For further information on the product, its benefits  please refer to the sales brochure and/or policy document.",
                            small_normal));

            BI_Pdftable2_cell11.setPadding(5);

            BI_Pdftable21.addCell(BI_Pdftable2_cell11);
            document.add(BI_Pdftable21);

            // PdfPTable BI_Pdftable_Introdcution = new PdfPTable(1);
            // BI_Pdftable_Introdcution.setWidthPercentage(100);
            // PdfPCell BI_Pdftable_Introdcutioncell = new PdfPCell(new
            // Paragraph(
            // "Introduction", small_bold));
            //
            // BI_Pdftable_Introdcutioncell
            // .setHorizontalAlignment(Element.ALIGN_CENTER);
            // BI_Pdftable_Introdcutioncell.setPadding(5);
            //
            // BI_Pdftable_Introdcution.addCell(BI_Pdftable_Introdcutioncell);
            // document.add(BI_Pdftable_Introdcution);
            //
            // PdfPTable BI_Pdftable2 = new PdfPTable(1);
            // BI_Pdftable2.setWidthPercentage(100);
            // // CR_table6.setWidths(columnWidths_2);
            // PdfPCell BI_Pdftable2_cell1 = new PdfPCell(
            // new Paragraph(
            // "Insurance Regulatory & Development Authority of India(IRDAI) requires all life insurance companies operating in India to provide official illustrations to their customers. The illustrations are based on the investment rates of return set by the Life Insurance Council (constituted under Section 64C(a) of the Insurance Act 1938) and is not intended to reflect the actual investment returns achieved or which may be achieved in future by SBI life Insurance Company Limited. The two rates of investment return currently declared by the Life Insurance Council are 4% and 8% per annum",
            // small_normal));
            //
            // BI_Pdftable2_cell1.setPadding(5);
            //
            // BI_Pdftable2.addCell(BI_Pdftable2_cell1);
            // document.add(BI_Pdftable2);
            //
            // PdfPTable BI_Pdftable3 = new PdfPTable(1);
            // BI_Pdftable3.setWidthPercentage(100);
            // // CR_table6.setWidths(columnWidths_3);
            // PdfPCell BI_Pdftable3_cell1 = new PdfPCell(
            // new Paragraph(
            // "The main objective of the illustration is that the client is able to appreciate the features of the product and the flow of benefits in different circumstances with some level of quantification. For further information on the product, its benefits and applicable charges please refer to the sales brochure and/or policy document. Further information will also be available on request.",
            // small_normal));
            //
            // BI_Pdftable3_cell1.setPadding(5);
            //
            // BI_Pdftable3.addCell(BI_Pdftable3_cell1);
            // document.add(BI_Pdftable3);
            //
            // PdfPTable BI_Pdftable4 = new PdfPTable(1);
            // BI_Pdftable4.setWidthPercentage(100);
            // // CR_table6.setWidths(columnWidths_4);
            // PdfPCell BI_Pdftable4_cell1 = new PdfPCell(
            // new Paragraph(
            // "Statutory Warning-"
            // +
            // "Some benefits are guaranteed and some benefits are variable with returns based on the future performance of your Insurer carrying on life insurance business. If your policy offers guaranteed returns then these will be clearly marked guaranteed in the illustration table on this page. If your policy offers variable returns then the illustrations on this page will show two different rates of assumed future investment returns. These assumed rates of return are not guaranteed and they are not the upper or lower limits of what you might get back, as the value of your policy is dependent on a number of factors including future investment performance.",
            // small_normal));
            //
            // BI_Pdftable4_cell1.setPadding(5);
            //
            // BI_Pdftable4.addCell(BI_Pdftable4_cell1);
            // document.add(BI_Pdftable4);
            // document.add(para_img_logo_after_space_1);

            PdfPTable BI_PdftablePlanDetails = new PdfPTable(1);
            BI_PdftablePlanDetails.setWidthPercentage(100);
            PdfPCell BI_PdftablePlanDetails_cell = new PdfPCell(new Paragraph(
                    "Proposer and Life Assured", small_bold));

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
                    "Name of the Prospect / Policyholder ", small_normal));
            cell_LifeAssuredName1.setPadding(5);
            PdfPCell cell_lLifeAssuredName2 = new PdfPCell(new Paragraph(
                    name_of_proposer, small_bold));
            cell_lLifeAssuredName2.setPadding(5);
            cell_lLifeAssuredName2.setHorizontalAlignment(Element.ALIGN_CENTER);

            PdfPCell cell_lifeAssuredAge1 = new PdfPCell(new Paragraph(
                    "Age of the Prospect / Policyholder (Years)", small_normal));
            cell_lifeAssuredAge1.setPadding(5);
            PdfPCell cell_lifeAssuredAge2 = new PdfPCell(new Paragraph(
                    age_entry + " Years", small_bold));
            cell_lifeAssuredAge2.setPadding(5);
            cell_lifeAssuredAge2.setHorizontalAlignment(Element.ALIGN_CENTER);

            table_lifeAssuredName.addCell(cell_LifeAssuredName1);
            table_lifeAssuredName.addCell(cell_lLifeAssuredName2);
            table_lifeAssuredName.addCell(cell_lifeAssuredAge1);
            table_lifeAssuredName.addCell(cell_lifeAssuredAge2);
            document.add(table_lifeAssuredName);

            PdfPTable table_lifeAssuredDetails = new PdfPTable(4);
            table_lifeAssuredDetails.setWidthPercentage(100);

            PdfPCell cell_lifeAssuredAmaturityGender1 = new PdfPCell(
                    new Paragraph("Gender of the Prospect / Policyholder", small_normal));
            cell_lifeAssuredAmaturityGender1.setPadding(5);
            PdfPCell cell_lifeAssuredAmaturityGender2 = new PdfPCell(
                    new Paragraph(gender, small_bold));
            cell_lifeAssuredAmaturityGender2.setPadding(5);
            cell_lifeAssuredAmaturityGender2
                    .setHorizontalAlignment(Element.ALIGN_CENTER);

            PdfPCell cell_lifeAssuredname = new PdfPCell(
                    new Paragraph("Name of the Life Assured", small_normal));
            cell_lifeAssuredname.setPadding(5);
            PdfPCell cell_lifeAssuredname2 = new PdfPCell(
                    new Paragraph(name_of_proposer, small_bold));
            cell_lifeAssuredname2.setPadding(5);
            cell_lifeAssuredname2
                    .setHorizontalAlignment(Element.ALIGN_CENTER);

            PdfPCell cell_lifeAssuredAge = new PdfPCell(
                    new Paragraph("Age of the Life Assured (Years)", small_normal));
            cell_lifeAssuredAge.setPadding(5);
            PdfPCell cell_lifeAssuredAge22 = new PdfPCell(
                    new Paragraph(age_entry, small_bold));
            cell_lifeAssuredAge22.setPadding(5);
            cell_lifeAssuredAge22
                    .setHorizontalAlignment(Element.ALIGN_CENTER);

            PdfPCell cell_lifeAssuredAmaturityGender3 = new PdfPCell(
                    new Paragraph("Gender of the Life Assured", small_normal));
            cell_lifeAssuredAmaturityGender3.setPadding(5);
            PdfPCell cell_lifeAssuredAmaturityGender4 = new PdfPCell(
                    new Paragraph(gender, small_bold));
            cell_lifeAssuredAmaturityGender4.setPadding(5);
            cell_lifeAssuredAmaturityGender4
                    .setHorizontalAlignment(Element.ALIGN_CENTER);

            PdfPCell cell_lifeAssured_Premium_frequeny1 = new PdfPCell(
                    new Paragraph("Premium Frequency", small_normal));
            cell_lifeAssured_Premium_frequeny1.setPadding(5);
            PdfPCell cell_lifeAssured_Premium_frequeny2 = new PdfPCell(
                    new Paragraph(policy_Frequency, small_bold));
            cell_lifeAssured_Premium_frequeny2.setPadding(5);
            cell_lifeAssured_Premium_frequeny2
                    .setHorizontalAlignment(Element.ALIGN_CENTER);


            vestingAge = prsObj.parseXmlTag(output, "vestingAge");

            PdfPCell vesting_Age = new PdfPCell(
                    new Paragraph("Vesting Age", small_normal));
            vesting_Age.setPadding(5);
            vesting_Age.setColspan(2);
            PdfPCell vesting_Age2 = new PdfPCell(
                    new Paragraph(vestingAge, small_bold));
            vesting_Age2.setPadding(5);
            vesting_Age2
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            vesting_Age2.setColspan(2);

            PdfPCell state = new PdfPCell(
                    new Paragraph("State", small_normal));
            state.setPadding(5);
            PdfPCell state2;
            if (cb_kerladisc.isChecked()) {
                state2 = new PdfPCell(
                        new Paragraph("Kerala", small_bold));
                state2.setPadding(5);
                state2.setHorizontalAlignment(Element.ALIGN_CENTER);

            } else {
                state2 = new PdfPCell(
                        new Paragraph("Non Kerala", small_bold));
                state2.setPadding(5);
                state2.setHorizontalAlignment(Element.ALIGN_CENTER);


            }


            table_lifeAssuredDetails.addCell(cell_lifeAssuredAmaturityGender1);
            table_lifeAssuredDetails.addCell(cell_lifeAssuredAmaturityGender2);
            table_lifeAssuredDetails.addCell(cell_lifeAssuredname);
            table_lifeAssuredDetails.addCell(cell_lifeAssuredname2);
            table_lifeAssuredDetails.addCell(cell_lifeAssuredAge);
            table_lifeAssuredDetails.addCell(cell_lifeAssuredAge22);
            table_lifeAssuredDetails.addCell(cell_lifeAssuredAmaturityGender3);
            table_lifeAssuredDetails.addCell(cell_lifeAssuredAmaturityGender4);
            table_lifeAssuredDetails.addCell(vesting_Age);
            table_lifeAssuredDetails.addCell(vesting_Age2);
            //table_lifeAssuredDetails.addCell(state);
            //table_lifeAssuredDetails.addCell(state2);
            //table_lifeAssuredDetails
            //		.addCell(cell_lifeAssured_Premium_frequeny1);
            //table_lifeAssuredDetails
            //		.addCell(cell_lifeAssured_Premium_frequeny2);
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
                // document.add(para_img_logo_after_space_1);
            } else if (!cb_staffdisc.isChecked()) {
                isStaff = "no";
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
                // document.add(para_img_logo_after_space_1);
            }

            PdfPTable BI_Pdftable_How = new PdfPTable(1);
            BI_Pdftable_How.setWidthPercentage(100);
            PdfPCell BI_Pdftable_read = new PdfPCell(new Paragraph(
                    "How to read and understand this benefit illustration?", small_bold));

            BI_Pdftable_read
                    .setHorizontalAlignment(Element.ALIGN_LEFT);
            BI_Pdftable_read
                    .setBackgroundColor(BaseColor.LIGHT_GRAY);
            BI_Pdftable_read.setPadding(5);

            BI_Pdftable_How.addCell(BI_Pdftable_read);
            document.add(BI_Pdftable_How);

            PdfPTable BI_Pdftableline1 = new PdfPTable(1);
            BI_Pdftableline1.setWidthPercentage(100);
            // CR_table6.setWidths(columnWidths_2);
            PdfPCell BI_Pdftable2_line = new PdfPCell(
                    new Paragraph(
                            "This benefit illustration is intended to show year-wise premiums payable and benefits under the policy, at two assumed rates of interest i.e, 8% p.a and 4% p.a.",
                            small_normal));

            BI_Pdftable2_line.setPadding(5);

            BI_Pdftableline1.addCell(BI_Pdftable2_line);
            document.add(BI_Pdftableline1);

            PdfPTable BI_Pdftable_benefit = new PdfPTable(1);
            BI_Pdftable_benefit.setWidthPercentage(100);
            PdfPCell BI_Pdftable2_benefit_cell11 = new PdfPCell(
                    new Paragraph(
                            "Some benefits are guaranteed and some benefits are variable with returns based on the future performance of your insurer carrying on life insurance business. If your policy offers guaranteed benefits then these will be clearly marked “guaranteed” in the illustration table on this page. If your policy offers variable benefits then the illustrations on this page will show two different rates of assumed future investment returns, of 8% p.a. and 4% p.a. These assumed rates of return are not guaranteed and they are not the upper or lower limits of what you might get back, as the value of your policy is dependent on a number of factors including future investment performance.",
                            small_normal));

            BI_Pdftable2_benefit_cell11.setPadding(5);

            BI_Pdftable_benefit.addCell(BI_Pdftable2_benefit_cell11);
            document.add(BI_Pdftable_benefit);

            String isJK = "";
            if (cb_bi_saral_pension_JKResident.isChecked()) {
                isJK = "yes";
                PdfPTable table_is_JK = new PdfPTable(2);
                table_is_JK.setWidthPercentage(100);

                PdfPCell cell_is_JK1 = new PdfPCell(new Paragraph("J&k",
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

            // ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

            // Basic Cover
            PdfPCell cell;

            PdfPTable basicCover_table = new PdfPTable(4);
            basicCover_table.setWidths(new float[]{5f, 5f, 5f, 5f});
            basicCover_table.setWidthPercentage(100f);
            basicCover_table.setHorizontalAlignment(Element.ALIGN_LEFT);

            // 1st row
            cell = new PdfPCell(new Phrase("Base Policy Details", small_bold));
            cell.setColspan(4);
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            basicCover_table.addCell(cell);

            cell = new PdfPCell(new Phrase("Policy Option",
                    small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            basicCover_table.addCell(cell);

            cell = new PdfPCell(new Phrase("Not Applicable", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            basicCover_table.addCell(cell);

            cell = new PdfPCell(new Phrase("Amount of Instalment Premium (Rs.) ",
                    small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            basicCover_table.addCell(cell);

            cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                    .parseDouble(prsObj.parseXmlTag(output,
                            "basePremExcludngST"))), small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            basicCover_table.addCell(cell);

            cell = new PdfPCell(new Phrase("Policy Term (years)", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            basicCover_table.addCell(cell);
            cell = new PdfPCell(new Phrase(policyTerm + "", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            basicCover_table.addCell(cell);

            cell = new PdfPCell(new Phrase("Sum Assured (Rs )",
                    small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            basicCover_table.addCell(cell);

            cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                    .parseDouble(sumAssured)), small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            basicCover_table.addCell(cell);


            // 2 row
            cell = new PdfPCell(
                    new Phrase("Premium Payment Term (years)", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            basicCover_table.addCell(cell);

            if (policy_Frequency.equalsIgnoreCase("Single"))
                cell = new PdfPCell(new Phrase("1", small_normal));
            else
                cell = new PdfPCell(new Phrase(policyTerm + "", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            basicCover_table.addCell(cell);

            cell = new PdfPCell(new Phrase("Sum Assured on Death (at inception of the policy) (Rs.)",
                    small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            basicCover_table.addCell(cell);

            sumAssuredDeath = prsObj.parseXmlTag(output, "guaranDeathBenfit" + "1" + "");
            cell = new PdfPCell(new Phrase(sumAssuredDeath, small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            basicCover_table.addCell(cell);

            cell = new PdfPCell(new Phrase("Bonus Type",
                    small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            basicCover_table.addCell(cell);

            cell = new PdfPCell(new Phrase("Simple Reversionary Bonus", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            basicCover_table.addCell(cell);

            cell = new PdfPCell(new Phrase("Mode / Frequency of payment of premium ", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            basicCover_table.addCell(cell);

            // 3 row
            cell = new PdfPCell(new Phrase(policy_Frequency, small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            basicCover_table.addCell(cell);

            cell = new PdfPCell(new Phrase("Rate of Applicable Taxes", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            basicCover_table.addCell(cell);

            str_kerla_discount = prsObj.parseXmlTag(input, "str_kerla_discount");
            premPayingMode = prsObj.parseXmlTag(input, "premFreqMode");
            if (str_kerla_discount.equalsIgnoreCase("Yes")) {
                if (premPayingMode.equals("Single")) {
                    cell = new PdfPCell(new Phrase("4.75% ", small_normal));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    basicCover_table.addCell(cell);
                } else {
                    cell = new PdfPCell(new Phrase("4.75% in the 1st policy year and 2.375% from 2nd policy year onwards" + "", small_normal));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    basicCover_table.addCell(cell);
                }
            } else {
                if (premPayingMode.equals("Single")) {
                    cell = new PdfPCell(new Phrase("4.50% ", small_normal));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    basicCover_table.addCell(cell);
                } else {
                    cell = new PdfPCell(new Phrase("4.5% in the 1st policy year and 2.25% from 2nd policy year onwards" + "", small_normal));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    basicCover_table.addCell(cell);
                }
            }

            cell = new PdfPCell(new Phrase("", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            basicCover_table.addCell(cell);
            cell = new PdfPCell(new Phrase("", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            basicCover_table.addCell(cell);

            cell = new PdfPCell(new Phrase("Mode of Premium", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            //basicCover_table.addCell(cell);

            if (policy_Frequency.equalsIgnoreCase("Single"))
                cell = new PdfPCell(new Phrase("Single", small_normal));
            else
                cell = new PdfPCell(new Phrase("Regular", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            //basicCover_table.addCell(cell);


            cell = new PdfPCell(new Phrase("Vesting Age (years)", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            //basicCover_table.addCell(cell);

            cell = new PdfPCell(new Phrase(vestingAge + "", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            //basicCover_table.addCell(cell);
            document.add(basicCover_table);
            document.add(para_img_logo_after_space_1);
            // Premium Details

            if (ptr_rider_status.equals("true")) {
                PdfPTable riderDetail_table = new PdfPTable(5);
                riderDetail_table.setWidths(new float[]{4f, 4f, 4f, 4f, 4f});
                riderDetail_table.setWidthPercentage(100f);
                riderDetail_table.setHorizontalAlignment(Element.ALIGN_LEFT);

                // 1st row
                cell = new PdfPCell(
                        new Phrase(
                                "Rider Details",
                                small_bold));
                cell.setColspan(5);
                cell.setBorder(Rectangle.BOTTOM);
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                riderDetail_table.addCell(cell);

                // 2 row
                cell = new PdfPCell(new Phrase("Rider Name",
                        small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                riderDetail_table.addCell(cell);

                cell = new PdfPCell(new Phrase("Rider Term(Years)",
                        small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                riderDetail_table.addCell(cell);

                cell = new PdfPCell(new Phrase("Rider Sum Assured(Rs.)",
                        small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                riderDetail_table.addCell(cell);

                cell = new PdfPCell(new Phrase("Rider Premium Payment Term(Years)",
                        small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                riderDetail_table.addCell(cell);

                cell = new PdfPCell(new Phrase(
                        "Rider Premiums (Rs.)", small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                riderDetail_table.addCell(cell);

                cell = new PdfPCell(new Phrase(
                        "SBI Life - Preferred Term Rider (UIN:111B014V02)", small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                riderDetail_table.addCell(cell);

                cell = new PdfPCell(new Phrase(term_ptr + "", small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                riderDetail_table.addCell(cell);

                cell = new PdfPCell(new Phrase(sa_ptr + "", small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                riderDetail_table.addCell(cell);

                if (policy_Frequency.equalsIgnoreCase("Single")) {
                    cell = new PdfPCell(new Phrase("Single Premium", small_normal));
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    riderDetail_table.addCell(cell);
                } else {
                    cell = new PdfPCell(new Phrase(term_ptr + "", small_normal));
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    riderDetail_table.addCell(cell);
                }


                cell = new PdfPCell(new Phrase(policy_Frequency + "",
                        small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                //riderDetail_table.addCell(cell);


                cell = new PdfPCell(new Phrase(obj.getRound(obj.getStringWithout_E(Double
                        .valueOf(prem_ptr.equals("") ? "0"
                                : prem_ptr))), small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                riderDetail_table.addCell(cell);
                document.add(riderDetail_table);
                document.add(para_img_logo_after_space_1);
            }

            // document.newPage();
            // Premium Details

            PdfPTable premDetail_table = new PdfPTable(3);
            premDetail_table.setWidths(new float[]{5f, 5f, 5f});
            premDetail_table.setWidthPercentage(100f);
            premDetail_table.setHorizontalAlignment(Element.ALIGN_LEFT);

            cell = new PdfPCell(
                    new Phrase(
                            "Total Premium for Base Product and Rider (if opted) (in Rs )",
                            small_bold));
            cell.setColspan(4);
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            premDetail_table.addCell(cell);

            // 1st row
            cell = new PdfPCell(new Phrase("    ", small_bold));
            cell.setColspan(3);
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            premDetail_table.addCell(cell);

            // 2 row
            cell = new PdfPCell(new Phrase("", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            premDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase("Frequency Mode", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            premDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase("Premium (Rs )", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            premDetail_table.addCell(cell);

            // System.out.println("Plan "+plan.substring(6, 8));
            // 3 row

            cell = new PdfPCell(new Phrase("Installment Premium", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            premDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase(saralPensionBean.getPremFreq(),
                    small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            premDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                    .parseDouble(prsObj.parseXmlTag(output, "installmntPrem"))),
                    small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            premDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase("Applicable Taxes (if any)", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            premDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase(saralPensionBean.getPremFreq(),
                    small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            premDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                    .parseDouble(prsObj.parseXmlTag(output, "servceTax"))),
                    small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            premDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase("Installment Premium with Applicable Taxes",
                    small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            premDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase(saralPensionBean.getPremFreq(),
                    small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            premDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                    .parseDouble(prsObj.parseXmlTag(output,
                            "instalmntPremWTServcTax"))), small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            premDetail_table.addCell(cell);

            /**** Added By - Priyanka Warekar 25-08-2015 - Start *****/
            PdfPTable FY_SY_premDetail_table = new PdfPTable(4);
            FY_SY_premDetail_table.setWidths(new float[]{5f, 5f, 5f, 5f});
            FY_SY_premDetail_table.setWidthPercentage(100f);
            FY_SY_premDetail_table.setHorizontalAlignment(Element.ALIGN_LEFT);

            // 1st row
            cell = new PdfPCell(
                    new Phrase(
                            "Premium Summary",
                            small_bold));
            cell.setColspan(7);
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            FY_SY_premDetail_table.addCell(cell);

            // 2 row
            cell = new PdfPCell(new Phrase("", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            FY_SY_premDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase(" Base Plan", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            FY_SY_premDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase("Rider", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            FY_SY_premDetail_table.addCell(cell);

            // cell = new PdfPCell(new Phrase("(b)Swachh Bharat Cess(Rs.)",
            // small_normal));
            // cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            // FY_SY_premDetail_table.addCell(cell);
            //
            // cell = new PdfPCell(new Phrase("(c)Krishi Kalyan Cess(Rs.)",
            // small_normal));
            // cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            // FY_SY_premDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase(" Total Instalment Premium", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            FY_SY_premDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase("Instalment Premium without Applicable Taxes (Rs.)", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            FY_SY_premDetail_table.addCell(cell);
            cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                    .parseDouble(prsObj.parseXmlTag(output,
                            "basePremExcludngST"))), small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            FY_SY_premDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                    .parseDouble(prsObj.parseXmlTag(output,
                            "premiumSingleInstBasicRider"))), small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            FY_SY_premDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                    .parseDouble(prsObj.parseXmlTag(output,
                            "intallmentPremWithST"))), small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            FY_SY_premDetail_table.addCell(cell);

            // System.out.println("Plan "+plan.substring(6, 8));
            // 3 row
            cell = new PdfPCell(new Phrase("Instalment Premium with First Year Applicable Taxes (Rs.)", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            FY_SY_premDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                    .parseDouble(prsObj.parseXmlTag(output, "premInstBasicFirstYear"))),
                    small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            FY_SY_premDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                    .parseDouble(prsObj.parseXmlTag(output, "premInstFYRider"))),
                    small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            FY_SY_premDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                    .parseDouble(prsObj.parseXmlTag(output,
                            "instalmntPremWTServcTax"))), small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            FY_SY_premDetail_table.addCell(cell);

            // 4 row
            if (!saralPensionBean.getPremFreq().equalsIgnoreCase("Single")) {
                cell = new PdfPCell(new Phrase("Instalment Premium with Applicable Taxes 2nd Year Onwards  (Rs.)",
                        small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                FY_SY_premDetail_table.addCell(cell);

                cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                        .parseDouble(prsObj
                                .parseXmlTag(output, "premInstBasicSecondYear"))),
                        small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                FY_SY_premDetail_table.addCell(cell);

                cell = new PdfPCell(new Phrase(
                        currencyFormat.format(Double.parseDouble(prsObj
                                .parseXmlTag(output, "premInstSYRider"))),
                        small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                FY_SY_premDetail_table.addCell(cell);

                cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                        .parseDouble(prsObj.parseXmlTag(output,
                                "SYinstalmntPremWTServcTax"))), small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                FY_SY_premDetail_table.addCell(cell);
            }

            /** Added By - Priyanka Warekar 25-08-2015 - End *****/

            PdfPTable assuredBenefit_table = new PdfPTable(2);
            assuredBenefit_table.setWidths(new float[]{4f, 4f});
            assuredBenefit_table.setWidthPercentage(100f);
            assuredBenefit_table.setHorizontalAlignment(Element.ALIGN_LEFT);

            // 1st row
            cell = new PdfPCell(new Phrase("Assured Benefit", small_bold));
            cell.setColspan(2);
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            assuredBenefit_table.addCell(cell);

            // 2 row
            cell = new PdfPCell(new Phrase(
                    "Minimum Return on the Premiums Paid", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            assuredBenefit_table.addCell(cell);
            cell = new PdfPCell(new Phrase(
                    "Interest Rate: 0.25% p.a. compounded annually",
                    small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            assuredBenefit_table.addCell(cell);

            cell = new PdfPCell(new Phrase("Guaranteed Vesting Benefit",
                    small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            assuredBenefit_table.addCell(cell);
            cell = new PdfPCell(new Phrase(
                    currencyFormat.format(Double.parseDouble(prsObj
                            .parseXmlTag(output, "guarnVestngBen"))),
                    small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            assuredBenefit_table.addCell(cell);
            cell = new PdfPCell(new Phrase("Guaranteed Surrender Value",
                    small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            assuredBenefit_table.addCell(cell);
            cell = new PdfPCell(new Phrase(
                    "As per Section 113 of Insurance Act, 1938", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            assuredBenefit_table.addCell(cell);
            cell = new PdfPCell(new Phrase("Guaranteed Annuity", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            assuredBenefit_table.addCell(cell);

            cell = new PdfPCell(new Phrase("Not Applicable", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            assuredBenefit_table.addCell(cell);

            // Table here

            PdfPTable table1 = new PdfPTable(9);
            table1.setWidths(new float[]{3f, 3f, 5f, 5f, 5f, 5f, 5f, 5f, 5f});
            table1.setWidthPercentage(100);

            // 1st row
			/*cell = new PdfPCell(new Phrase("Illustrative Benefits on Vesting",
                    small_bold));
            cell.setColspan(9);
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			table1.addCell(cell);*/

            cell = new PdfPCell(new Phrase("Annuity Option* Selected (The option can be changed any time before vesting)", small_normal));
            cell.setColspan(5);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);

            cell = new PdfPCell(new Phrase((spnr_bi_Matuirty_option.getSelectedItem().toString()), small_normal));
            cell.setColspan(4);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);


            cell = new PdfPCell(new Phrase("Fund Value at Vesting", small_normal));
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
                    "Minimum Assured Benefit", small_normal));
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
            cell = new PdfPCell(new Phrase("Based on the Minimum Assured Benefit, Rs.",
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


            nonGuaranVestingBenefit_4Percent = prsObj.parseXmlTag(output, "FundValueAtVesting4");
            nonGuaranVestingBenefit_8Percent = prsObj.parseXmlTag(output, "FundValueAtVesting8");
            guarnVestngBen = prsObj.parseXmlTag(output, "VestingMinAssBen");
            annuityPayout_4_Pr = prsObj.parseXmlTag(output, "annuityPayout_4_Pr");
            annuityPayout_8_Pr = prsObj.parseXmlTag(output, "annuityPayout_8_Pr");
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




		/*	cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                    .parseDouble(prsObj.parseXmlTag(output,
							"nonGuaranVestingBenefit_4Percent"))),
                    small_normal));
            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT | Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table1.addCell(cell);

            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                    .parseDouble(prsObj.parseXmlTag(output,
                            "nonGuaranVestingBenefit_8Percent" + policyTerm))),
                    small_normal));
            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT | Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table1.addCell(cell);

            cell = new PdfPCell(
                    new Phrase(currencyFormat.format(Double.parseDouble(prsObj
                            .parseXmlTag(output, "vestingBenefit_4_pr"
                                    + policyTerm))), small_normal));
            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT | Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table1.addCell(cell);

            cell = new PdfPCell(
                    new Phrase(currencyFormat.format(Double.parseDouble(prsObj
                            .parseXmlTag(output, "vestingBenefit_8_pr"
                                    + policyTerm))), small_normal));
            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT | Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table1.addCell(cell);

            cell = new PdfPCell(
                    new Phrase(
                            "Annuity payable (per annum) based on the total vesting benefit given above and the annuity rates* (Rs )",
                            small_normal));
            cell.setColspan(5);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);

            cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                    .parseDouble(prsObj.parseXmlTag(output,
                            "annuityPayout_4_Pr"))), small_normal));
            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT | Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table1.addCell(cell);

            cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                    .parseDouble(prsObj.parseXmlTag(output,
                            "annuityPayout_8_Pr"))), small_normal));
            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT | Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			table1.addCell(cell);*/


            String premiumRate = prsObj.parseXmlTag(output, "premiumRate");

            // Table here

            PdfPTable table2 = new PdfPTable(6);
            table2.setWidths(new float[]{3f, 5f, 5f, 5f, 5f, 5f});
            table2.setWidthPercentage(100);

            // 1st row
            cell = new PdfPCell(new Phrase("Illustrative Benefits on Death",
                    small_bold));
            cell.setColspan(9);
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table2.addCell(cell);

            cell = new PdfPCell(new Phrase("Year of death", small_normal));
            cell.setRowspan(3);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table2.addCell(cell);

            cell = new PdfPCell(new Phrase("Guaranteed Death Benefit (Rs)",
                    small_normal));
            cell.setRowspan(3);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table2.addCell(cell);

            cell = new PdfPCell(new Phrase("Bonuses/Non- Guaranteed Benefits (Rs)",
                    small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setColspan(2);
            table2.addCell(cell);

            cell = new PdfPCell(
                    new Phrase(
                            "Death benefit (Higher of Guaranteed and Non - guaranteed benefits) (Rs)",
                            small_normal));
            cell.setColspan(2);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table2.addCell(cell);

            // 3rd
            cell = new PdfPCell(new Phrase("Assumed Investment Return",
                    small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setColspan(2);
            table2.addCell(cell);

            cell = new PdfPCell(new Phrase("Assumed Investment Return",
                    small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setColspan(2);
            table2.addCell(cell);

            // 4 row
            cell = new PdfPCell(new Phrase("4% pa", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table2.addCell(cell);

            cell = new PdfPCell(new Phrase("8% pa", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table2.addCell(cell);

            cell = new PdfPCell(new Phrase("4% pa", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table2.addCell(cell);

            cell = new PdfPCell(new Phrase("8% pa", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table2.addCell(cell);

            for (int i = 0; i < Integer.parseInt(policyTerm); i++) {

                cell = new PdfPCell(new Phrase(prsObj.parseXmlTag(output,
                        "policyYr" + (i + 1)), small_normal));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                        | Rectangle.BOTTOM);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table2.addCell(cell);

                cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                        .parseDouble(prsObj.parseXmlTag(output,
                                "guaranDeathBenfit" + (i + 1)))), small_normal));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                        | Rectangle.BOTTOM);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table2.addCell(cell);
                cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                        .parseDouble(prsObj.parseXmlTag(output,
                                "nonGuaranDeathBenfit_4Percent" + (i + 1)))),
                        small_normal));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                        | Rectangle.BOTTOM);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table2.addCell(cell);
                cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                        .parseDouble(prsObj.parseXmlTag(output,
                                "nonGuaranDeathBenfit_8Percent" + (i + 1)))),
                        small_normal));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                        | Rectangle.BOTTOM);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table2.addCell(cell);
                cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                        .parseDouble(prsObj.parseXmlTag(output,
                                "deathBenefit_4Percent" + (i + 1)))),
                        small_normal));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                        | Rectangle.BOTTOM);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table2.addCell(cell);

                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                        .parseDouble(prsObj.parseXmlTag(output,
                                "deathBenefit_8Percent" + (i + 1)))),
                        small_normal));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                        | Rectangle.BOTTOM);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table2.addCell(cell);
            }

            // Table 3- Surrender
            // Table here

            PdfPTable table3 = new PdfPTable(6);
            table3.setWidths(new float[]{3f, 5f, 5f, 5f, 5f, 5f});
            table3.setWidthPercentage(100);

            // 1st row
            cell = new PdfPCell(new Phrase(
                    "Illustrative Benefits on Surrender", small_bold));
            cell.setColspan(9);
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table3.addCell(cell);

            cell = new PdfPCell(new Phrase("Assumed Year of Surrender",
                    small_normal));
            cell.setRowspan(3);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table3.addCell(cell);

            cell = new PdfPCell(new Phrase("Guaranteed Surrender Benefit (Rs)",
                    small_normal));
            cell.setRowspan(3);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table3.addCell(cell);

            cell = new PdfPCell(new Phrase("Bonuses/Non- Guaranteed Benefits (Rs)",
                    small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setColspan(2);
            table3.addCell(cell);

            cell = new PdfPCell(
                    new Phrase(
                            "Surrender benefit (Higher of Guaranteed and Non - guaranteed benefits) (Rs)",
                            small_normal));
            cell.setColspan(2);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table3.addCell(cell);

            // 3rd
            cell = new PdfPCell(new Phrase("Assumed Investment Return",
                    small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setColspan(2);
            table3.addCell(cell);

            cell = new PdfPCell(new Phrase("Assumed Investment Return",
                    small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setColspan(2);
            table3.addCell(cell);

            // 4 row
            cell = new PdfPCell(new Phrase("4% pa", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table3.addCell(cell);

            cell = new PdfPCell(new Phrase("8% pa", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table3.addCell(cell);

            cell = new PdfPCell(new Phrase("4% pa", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table3.addCell(cell);

            cell = new PdfPCell(new Phrase("8% pa", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table3.addCell(cell);

            for (int i = 0; i < Integer.parseInt(policyTerm); i++) {

                cell = new PdfPCell(new Phrase(prsObj.parseXmlTag(output,
                        "policyYr" + (i + 1)), small_normal));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                        | Rectangle.BOTTOM);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table3.addCell(cell);

                cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                        .parseDouble(prsObj.parseXmlTag(output,
                                "guaranSurrBenefit" + (i + 1)))), small_normal));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                        | Rectangle.BOTTOM);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table3.addCell(cell);
                cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                        .parseDouble(prsObj.parseXmlTag(output,
                                "nonGuaranSurrBen_4Pr" + (i + 1)))),
                        small_normal));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                        | Rectangle.BOTTOM);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table3.addCell(cell);
                cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                        .parseDouble(prsObj.parseXmlTag(output,
                                "nonGuaranSurrBen_8Pr" + (i + 1)))),
                        small_normal));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                        | Rectangle.BOTTOM);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table3.addCell(cell);
                cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                        .parseDouble(prsObj.parseXmlTag(output,
                                "surrenderBenefit_4Pr" + (i + 1)))),
                        small_normal));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                        | Rectangle.BOTTOM);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table3.addCell(cell);

                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                        .parseDouble(prsObj.parseXmlTag(output,
                                "surrenderBenefit_8Pr" + (i + 1)))),
                        small_normal));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                        | Rectangle.BOTTOM);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table3.addCell(cell);
            }

            Paragraph new_line = new Paragraph("\n");
            // document.add(table);
            document.add(new_line);

            // document.add(main_table);
            // document.add(personalDetail_table);
            // document.add(basicCover_table);
            // document.add(new_line);

            document.add(new_line);
            document.add(new_line);
            document.add(new_line);
            //document.add(premDetail_table);
            document.add(new_line);

            // Start Total premium for base product
            document.add(FY_SY_premDetail_table);
            document.add(new_line);
            // End Total premium for base product
            PdfPTable BI_Pdftable_note = new PdfPTable(1);
            BI_Pdftable_note.setWidthPercentage(100);
            PdfPCell BI_Pdftable_note_cell1 = new PdfPCell(new Paragraph(
                    "Please Note:", small_bold));
            BI_Pdftable_note_cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
            BI_Pdftable_note_cell1.setPadding(5);

            BI_Pdftable_note.addCell(BI_Pdftable_note_cell1);
            document.add(BI_Pdftable_note);

            PdfPTable BI_Pdftable6 = new PdfPTable(1);
            BI_Pdftable6.setWidthPercentage(100);
            PdfPCell BI_Pdftable6_cell6 = new PdfPCell(
                    new Paragraph(
                            "1. The premiums can be also paid by giving standing instruction to your bank or you can pay through your credit card."

                            , small_normal));

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

            PdfPTable table1_new = new PdfPTable(17);
            table1_new.setWidths(new float[]{2f, 5f, 5f, 5f, 5f, 5f, 5f, 5f, 5f,
                    5f, 5f, 5f, 5f, 5f, 5f, 5f, 5f});
            table1_new.setWidthPercentage(100);

            // 1st row
            cell = new PdfPCell(
                    new Phrase(
                            "BENEFIT ILLUSTRATION FOR SBI LIFE - Saral Retirement Saver (Amount in Rs.)",
                            small_bold));
            cell.setColspan(17);
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table1_new.addCell(cell);

            // 2nd row
            cell = new PdfPCell(new Phrase("Policy Year", small_bold));
            cell.setRowspan(3);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1_new.addCell(cell);
            if (saralPensionBean.getPremFreq().equalsIgnoreCase("Single")) {
                cell = new PdfPCell(new Phrase(
                        "Single premium", small_bold));
            } else {
                cell = new PdfPCell(new Phrase(
                        "Annualized premium", small_bold));
            }


            cell.setRowspan(3);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1_new.addCell(cell);

            cell = new PdfPCell(new Phrase("Guaranteed Benefits",
                    small_bold));
            cell.setColspan(5);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1_new.addCell(cell);

            cell = new PdfPCell(
                    new Phrase("Non-Guaranteed Benefits @ 4% p.a.", small_bold));
            cell.setColspan(3);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1_new.addCell(cell);

            cell = new PdfPCell(
                    new Phrase("Non-Guaranteed Benefits @ 8% p.a.", small_bold));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setColspan(3);
            table1_new.addCell(cell);

            // 3rd
            cell = new PdfPCell(new Phrase("Total benefits Including Guaranteed and Non-Guaranteed benefits", small_bold));
            cell.setColspan(4);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1_new.addCell(cell);

            cell = new PdfPCell(new Phrase(
                    "Guaranteed additions/ Bonus",
                    small_bold));
            cell.setRowspan(2);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1_new.addCell(cell);

            cell = new PdfPCell(new Phrase("Survival benefit", small_bold));
            cell.setRowspan(2);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1_new.addCell(cell);

            cell = new PdfPCell(new Phrase(
                    "Surrender Benefit",
                    small_bold));
            cell.setRowspan(2);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1_new.addCell(cell);

            cell = new PdfPCell(new Phrase("Death Benefit", small_bold));
            cell.setRowspan(2);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1_new.addCell(cell);

            cell = new PdfPCell(new Phrase("Maturity / Vesting Benefit", small_bold));
            cell.setRowspan(2);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1_new.addCell(cell);

            // 4 row
            cell = new PdfPCell(new Phrase("Reversionary bonus", small_bold));
            cell.setRowspan(2);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1_new.addCell(cell);

            cell = new PdfPCell(new Phrase("Cash bonus", small_bold));
            cell.setRowspan(2);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1_new.addCell(cell);

            cell = new PdfPCell(new Phrase("Surrender benefit", small_bold));
            cell.setRowspan(2);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1_new.addCell(cell);

            cell = new PdfPCell(new Phrase("Reversionary bonus", small_bold));
            cell.setRowspan(2);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1_new.addCell(cell);

            cell = new PdfPCell(new Phrase("Cash bonus", small_bold));
            cell.setRowspan(2);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1_new.addCell(cell);

            cell = new PdfPCell(new Phrase("Surrender benefit", small_bold));
            cell.setRowspan(2);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1_new.addCell(cell);

            cell = new PdfPCell(new Phrase("Maturity Benefit", small_bold));
            cell.setColspan(2);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1_new.addCell(cell);

            cell = new PdfPCell(new Phrase("Death Benefit", small_bold));
            cell.setColspan(2);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1_new.addCell(cell);

            cell = new PdfPCell(new Phrase("Total Maturity/Vesting benefit, incl. Terminal bonus, if any, @ 4% (7+8+9)", small_bold));
            cell.setRowspan(1);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1_new.addCell(cell);
            cell = new PdfPCell(new Phrase("Total Maturity/Vesting benefit, incl. Terminal bonus, if any, @ 8% (7+11+12)", small_bold));
            cell.setRowspan(1);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1_new.addCell(cell);
            cell = new PdfPCell(new Phrase("Total Death benefit, incl. Terminal bonus, if any, @ 4% (6+8+9)", small_bold));
            cell.setRowspan(1);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1_new.addCell(cell);

            cell = new PdfPCell(new Phrase("Total Death benefit, incl. Terminal bonus, if any, @ 8% (6+11+12)", small_bold));
            cell.setRowspan(1);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1_new.addCell(cell);


            cell = new PdfPCell(new Phrase("1", normal));
            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                    | Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table1_new.addCell(cell);

            cell = new PdfPCell(new Phrase(
                    "2", normal));
            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                    | Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table1_new.addCell(cell);


            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell = new PdfPCell(new Phrase("3", normal));
            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                    | Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table1_new.addCell(cell);


            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell = new PdfPCell(new Phrase("4", normal));
            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                    | Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table1_new.addCell(cell);


            cell = new PdfPCell(new Phrase("5", normal));
            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                    | Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table1_new.addCell(cell);


            cell = new PdfPCell(new Phrase("6", normal));
            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                    | Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table1_new.addCell(cell);


            cell = new PdfPCell(new Phrase("7", normal));
            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                    | Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table1_new.addCell(cell);


            cell = new PdfPCell(new Phrase("8",
                    normal));
            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                    | Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table1_new.addCell(cell);


              /*  cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                        .parseDouble(list_data.get(i)
                                .getCash_bonus_4per())),
                        normal));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                        | Rectangle.BOTTOM);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table1_new.addCell(cell);*/

            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell = new PdfPCell(new Phrase("9", normal));
            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                    | Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table1_new.addCell(cell);


            cell = new PdfPCell(new Phrase("10",
                    normal));
            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                    | Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table1_new.addCell(cell);


            cell = new PdfPCell(new Phrase("11",
                    normal));
            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                    | Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table1_new.addCell(cell);


              /*  cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                        .parseDouble(list_data.get(i)
                                .getCash_bonus_8per())),
                        normal));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                        | Rectangle.BOTTOM);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table1_new.addCell(cell);*/

            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell = new PdfPCell(new Phrase("12", normal));
            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                    | Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table1_new.addCell(cell);


            cell = new PdfPCell(new Phrase("13",
                    normal));
            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                    | Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table1_new.addCell(cell);


            cell = new PdfPCell(new Phrase("14",
                    normal));
            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                    | Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table1_new.addCell(cell);


            cell = new PdfPCell(new Phrase("15",
                    normal));
            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                    | Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table1_new.addCell(cell);


            cell = new PdfPCell(new Phrase("16",
                    normal));
            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                    | Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table1_new.addCell(cell);
            cell = new PdfPCell(new Phrase("17",
                    normal));
            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                    | Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table1_new.addCell(cell);


            for (int i = 0; i < list_data_new.size(); i++) {

                cell = new PdfPCell(new Phrase(list_data_new.get(i)
                        .getEnd_of_year(), normal));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                        | Rectangle.BOTTOM);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table1_new.addCell(cell);

                cell = new PdfPCell(new Phrase(
                        currencyFormat.format(Double.parseDouble(list_data_new.get(
                                i).getTotal_base_premium())), normal));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                        | Rectangle.BOTTOM);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table1_new.addCell(cell);


                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                        .parseDouble(list_data_new.get(i)
                                .getGuaranteed_additions())), normal));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                        | Rectangle.BOTTOM);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table1_new.addCell(cell);


                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                        .parseDouble(list_data_new.get(i)
                                .getGuaranteed_survival_benefit())), normal));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                        | Rectangle.BOTTOM);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table1_new.addCell(cell);


                cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                        .parseDouble(list_data_new.get(i)
                                .getGuaranteed_surrender_value())), normal));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                        | Rectangle.BOTTOM);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table1_new.addCell(cell);


                cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                        .parseDouble(list_data_new.get(i)
                                .getGuaranteed_death_benefit())), normal));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                        | Rectangle.BOTTOM);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table1_new.addCell(cell);


                cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                        .parseDouble(list_data_new.get(i)
                                .getGuaranteed_maturity_benefit())), normal));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                        | Rectangle.BOTTOM);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table1_new.addCell(cell);


                cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                        .parseDouble(list_data_new.get(i)
                                .getNot_guaranteed_maturity_benefit_4per())),
                        normal));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                        | Rectangle.BOTTOM);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table1_new.addCell(cell);


              /*  cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                        .parseDouble(list_data_new.get(i)
                                .getCash_bonus_4per())),
                        normal));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                        | Rectangle.BOTTOM);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table1_new.addCell(cell);*/

                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell = new PdfPCell(new Phrase("0", normal));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                        | Rectangle.BOTTOM);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table1_new.addCell(cell);


                cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                        .parseDouble(list_data_new.get(i)
                                .getNot_guaranteed_surrender_value_4per())),
                        normal));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                        | Rectangle.BOTTOM);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table1_new.addCell(cell);


                cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                        .parseDouble(list_data_new.get(i)
                                .getNot_guaranteed_maturity_benefit_8per())),
                        normal));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                        | Rectangle.BOTTOM);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table1_new.addCell(cell);


              /*  cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                        .parseDouble(list_data_new.get(i)
                                .getCash_bonus_8per())),
                        normal));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                        | Rectangle.BOTTOM);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table1_new.addCell(cell);*/

                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell = new PdfPCell(new Phrase("0", normal));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                        | Rectangle.BOTTOM);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table1_new.addCell(cell);


                cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                        .parseDouble(list_data_new.get(i)
                                .getNot_guaranteed_surrender_value_8per())),
                        normal));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                        | Rectangle.BOTTOM);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table1_new.addCell(cell);


                cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                        .parseDouble(list_data_new.get(i)
                                .getNot_guaranteed_survival_benefit_4per())),
                        normal));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                        | Rectangle.BOTTOM);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table1_new.addCell(cell);


                cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                        .parseDouble(list_data_new.get(i)
                                .getNot_guaranteed_survival_benefit_8per())),
                        normal));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                        | Rectangle.BOTTOM);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table1_new.addCell(cell);


                cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                        .parseDouble(list_data_new.get(i)
                                .getNot_guaranteed_death_benefit_4per())),
                        normal));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                        | Rectangle.BOTTOM);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table1_new.addCell(cell);
                cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                        .parseDouble(list_data_new.get(i)
                                .getNot_guaranteed_death_benefit_8per())),
                        normal));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                        | Rectangle.BOTTOM);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table1_new.addCell(cell);


            }
            document.add(table1_new);
            document.add(new_line);
            //document.add(assuredBenefit_table);
            //document.add(new_line);
            document.add(table1);
            //document.add(new_line);
            //document.add(table2);
            document.add(new_line);
            //document.add(new_line);
            //document.add(table3);
            document.add(new_line);

            // /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

            document.add(para_img_logo_after_space_1);

			/*Paragraph para_EndowmentOption = new Paragraph(
                    "#For details on Sum Assured on death please refer the Sales Brochure",
                    small_normal);
			document.add(para_EndowmentOption);*/

            document.add(para_img_logo_after_space_1);
            //
            // PdfPTable BI_Pdftable_BonusRates = new PdfPTable(1);
            // BI_Pdftable_BonusRates.setWidthPercentage(100);
            // PdfPCell BI_Pdftable_BonusRates_cell1 = new PdfPCell(new
            // Paragraph(
            // "Bonus Rates", small_bold));
            // BI_Pdftable_BonusRates_cell1
            // .setHorizontalAlignment(Element.ALIGN_CENTER);
            // BI_Pdftable_BonusRates_cell1.setPadding(5);
            //
            // BI_Pdftable_BonusRates.addCell(BI_Pdftable_BonusRates_cell1);
            // document.add(BI_Pdftable_BonusRates);
            //
            // PdfPTable BI_Pdftable_BonusRates1 = new PdfPTable(1);
            // BI_Pdftable_BonusRates1.setWidthPercentage(100);
            // PdfPCell BI_Pdftable_BonusRates_cell2 = new PdfPCell(
            // new Paragraph(
            // "1). This is a with profit plan and participates in the profits of the company life insurance business. It gets a share of the profits in the form of bonuses as a result of the statutory valuation carried out every year based on the applicable IRDA regulations."
            //
            // , small_normal));
            //
            // BI_Pdftable_BonusRates_cell2.setPadding(5);
            //
            // BI_Pdftable_BonusRates1.addCell(BI_Pdftable_BonusRates_cell2);
            // document.add(BI_Pdftable_BonusRates1);
            //
            // PdfPTable BI_Pdftable_BonusRates2 = new PdfPTable(1);
            // BI_Pdftable_BonusRates2.setWidthPercentage(100);
            // PdfPCell BI_Pdftable_BonusRates2_cell2 = new PdfPCell(
            // new Paragraph(
            // "2). Simple reversionary bonuses are declared as a percentage rate, which apply to the sum assured of the basic policy. Once declared, they form a part of the guaranteed benefits of the plan. Terminal bonuses, if any, are declared as a percentage rate, which apply to the vested bonus."
            //
            // , small_normal));
            //
            // BI_Pdftable_BonusRates2_cell2.setPadding(5);
            //
            // BI_Pdftable_BonusRates2.addCell(BI_Pdftable_BonusRates2_cell2);
            // document.add(BI_Pdftable_BonusRates2);
            //
            // PdfPTable BI_Pdftable_BonusRates3 = new PdfPTable(1);
            // BI_Pdftable_BonusRates3.setWidthPercentage(100);
            // PdfPCell BI_Pdftable_BonusRates3_cell2 = new PdfPCell(
            // new Paragraph(
            // "3). The bonus rates in the benefit illustration are constant. However, in practice, future bonuses are not guaranteed and will depend on future profits. Therefore, the bonuses are shown as non-guaranteed benefits and are calculated so that they are consistent with the two projected investment return assumptions of 4% and 8% per annum."
            //
            // , small_normal));
            //
            // BI_Pdftable_BonusRates3_cell2.setPadding(5);
            //
            // BI_Pdftable_BonusRates3.addCell(BI_Pdftable_BonusRates3_cell2);
            // document.add(BI_Pdftable_BonusRates3);
            //
            // PdfPTable BI_Pdftable_BonusRates4 = new PdfPTable(1);
            // BI_Pdftable_BonusRates4.setWidthPercentage(100);
            // PdfPCell BI_Pdftable_BonusRates4_cell2 = new PdfPCell(
            // new Paragraph(
            // "4). Accordingly, for the purpose of guaranteed gurrender value (GSV) in this illustration, the cash value of vested bonuses are not considered at all."
            //
            // , small_normal));
            //
            // BI_Pdftable_BonusRates4_cell2.setPadding(5);
            //
            // BI_Pdftable_BonusRates4.addCell(BI_Pdftable_BonusRates4_cell2);
            // document.add(BI_Pdftable_BonusRates4);
            //
            // document.add(para_img_logo_after_space_1);
            //
            // PdfPTable BI_Pdftable_SurrenderValue = new PdfPTable(1);
            // BI_Pdftable_SurrenderValue.setWidthPercentage(100);
            // PdfPCell BI_Pdftable_SurrenderValue_cell2 = new PdfPCell(
            // new Paragraph(
            // "Surrender Value -Surrender value is available for the basic policy benefits and not for the rider benefits."
            //
            // , small_normal));
            //
            // BI_Pdftable_SurrenderValue_cell2.setPadding(5);
            //
            // BI_Pdftable_BonusRates1.addCell(BI_Pdftable_SurrenderValue_cell2);
            // document.add(BI_Pdftable_SurrenderValue);
            //
            // document.add(para_img_logo_after_space_1);
            //
            // PdfPTable BI_Pdftable_GuaranteedSurrenderValue = new
            // PdfPTable(1);
            // BI_Pdftable_GuaranteedSurrenderValue.setWidthPercentage(100);
            // PdfPCell BI_Pdftable_GuaranteedSurrenderValue_cell1 = new
            // PdfPCell(
            // new Paragraph("Guaranteed Surrender Value", small_bold));
            // BI_Pdftable_GuaranteedSurrenderValue_cell1
            // .setHorizontalAlignment(Element.ALIGN_CENTER);
            // BI_Pdftable_GuaranteedSurrenderValue_cell1.setPadding(5);
            //
            // BI_Pdftable_GuaranteedSurrenderValue
            // .addCell(BI_Pdftable_GuaranteedSurrenderValue_cell1);
            // document.add(BI_Pdftable_GuaranteedSurrenderValue);
            //
            // PdfPTable BI_Pdftable_RegularPremiumPolicies = new PdfPTable(1);
            // BI_Pdftable_RegularPremiumPolicies.setWidthPercentage(100);
            // PdfPCell BI_Pdftable_RegularPremiumPolicies_cell = new PdfPCell(
            // new Paragraph(
            // "1) For Regular Premium Policies:-  The policy will acquire a paid-up and/or surrender value only if premiums have been paid for at least 2 full years for policy term less than 10 years and at least 3 full years for policy term 10 years or more.The Guaranteed Surrender Value (GSV) ) in case of regular premium policies will be equal to GSV factors multiplied by the basic premiums paid. Basic premium is equal to total premium less Applicable Taxes and cess, underwriting extra premiums, extra premium due to modal factors and rider premiums, if any."
            //
            // , small_normal));
            //
            // BI_Pdftable_RegularPremiumPolicies_cell.setPadding(5);
            //
            // BI_Pdftable_RegularPremiumPolicies
            // .addCell(BI_Pdftable_RegularPremiumPolicies_cell);
            // document.add(BI_Pdftable_RegularPremiumPolicies);
            //
            // PdfPTable BI_Pdftable_SinglePremiumPolicies = new PdfPTable(1);
            // BI_Pdftable_SinglePremiumPolicies.setWidthPercentage(100);
            // PdfPCell BI_Pdftable_SinglePremiumPolicies_cell = new PdfPCell(
            // new Paragraph(
            // "2) For Single Premium Policies:-  The policy acquires surrender value after date of commencement of risk.For first three policy years, the Guaranteed Surrender Value (GSV) will be 70% of Single Premium (exclusive of Applicable Taxes) paid excluding extra premiums (underwriting extra) and rider premiums, if any, plus cash value of the allocated bonuses.From fourth policy year onwards, the Guaranteed Surrender Value (GSV) will be 90% of Single Premium (exclusive of Applicable Taxes) paid excluding extra premiums (underwriting extra) and rider premiums, if any, plus cash value of the allocated bonuses."
            //
            // , small_normal));
            //
            // BI_Pdftable_SinglePremiumPolicies_cell.setPadding(5);
            //
            // BI_Pdftable_SinglePremiumPolicies
            // .addCell(BI_Pdftable_SinglePremiumPolicies_cell);
            // document.add(BI_Pdftable_SinglePremiumPolicies);
            //
            // document.add(para_img_logo_after_space_1);
            //
            // PdfPTable BI_Pdftable_CompanysPolicySurrender = new PdfPTable(1);
            // BI_Pdftable_CompanysPolicySurrender.setWidthPercentage(100);
            // PdfPCell BI_Pdftable_CompanysPolicySurrender_cell1 = new
            // PdfPCell(
            // new Paragraph("Company's Policy on Surrender", small_bold));
            // BI_Pdftable_CompanysPolicySurrender_cell1
            // .setHorizontalAlignment(Element.ALIGN_CENTER);
            // BI_Pdftable_CompanysPolicySurrender_cell1.setPadding(5);
            //
            // BI_Pdftable_CompanysPolicySurrender
            // .addCell(BI_Pdftable_CompanysPolicySurrender_cell1);
            // document.add(BI_Pdftable_CompanysPolicySurrender);
            //
            // PdfPTable BI_Pdftable_CompanysPolicySurrender1 = new
            // PdfPTable(1);
            // BI_Pdftable_CompanysPolicySurrender1.setWidthPercentage(100);
            // PdfPCell BI_Pdftable_CompanysPolicySurrender1_cell = new
            // PdfPCell(
            // new Paragraph(
            // "In practice, the company may pay a surrender value which could be higher than the guaranteed surrender value. The benefits payable on surrender reflects the value of your policy, which is assessed based on the past financial/demographic experience of the company with regard to your policy/group of similar policies, as well as the likely future experience. The surrender value payable may be reviewed from time to time depending on company's experience of the various factors which impact the surrender values that may be paid. The surrender value would be higher of GSV or SSV."
            //
            // , small_normal));
            //
            // BI_Pdftable_CompanysPolicySurrender1_cell.setPadding(5);
            //
            // BI_Pdftable_CompanysPolicySurrender1_cell
            // .setHorizontalAlignment(Element.ALIGN_CENTER);
            // BI_Pdftable_CompanysPolicySurrender1
            // .addCell(BI_Pdftable_CompanysPolicySurrender1_cell);
            // document.add(BI_Pdftable_CompanysPolicySurrender1);

            PdfPTable BI_Pdftable_BonusRates = new PdfPTable(1);
            BI_Pdftable_BonusRates.setWidthPercentage(100);
            PdfPCell BI_Pdftable_BonusRates_cell1 = new PdfPCell(new Paragraph(
                    "Notes", small_bold));
            BI_Pdftable_BonusRates_cell1
                    .setHorizontalAlignment(Element.ALIGN_LEFT);
            BI_Pdftable_BonusRates_cell1.setPadding(5);

            BI_Pdftable_BonusRates.addCell(BI_Pdftable_BonusRates_cell1);
            document.add(BI_Pdftable_BonusRates);

            PdfPTable BI_Pdftable_BonusRates1 = new PdfPTable(1);
            BI_Pdftable_BonusRates1.setWidthPercentage(100);
            PdfPCell BI_Pdftable_BonusRates_cell2 = new PdfPCell(
                    new Paragraph(
                            "1. Annualized premium shall be the premium amount payable in a year chosen by the policyholder, excluding the taxes,  rider premiums, underwriting extra premiums and loadings for modal premiums, if any / Single premium shall be the premium amount payable in lumpsum at  inception of the policy as chosen by the policyholder, excluding the taxes, rider premiums and underwriting extra premiums, if any. Refer the sales literature for explanation of terms used in this illustration."

                            , small_normal));

            BI_Pdftable_BonusRates_cell2.setPadding(5);

            BI_Pdftable_BonusRates1.addCell(BI_Pdftable_BonusRates_cell2);
            document.add(BI_Pdftable_BonusRates1);

            PdfPTable BI_Pdftable_BonusRates2 = new PdfPTable(1);
            BI_Pdftable_BonusRates2.setWidthPercentage(100);
            PdfPCell BI_Pdftable_BonusRates2_cell2 = new PdfPCell(
                    new Paragraph(
                            "2. The values shown above are for illustration purpose only. The actual annuity amount receivable depends on the prevailing annuity rates at the time of vesting. The guaranteed values are based on the minimum investment return guaranteed at the outset of the policy, whereas the non-guaranteed values are based on the assumed investment returns of 4% p.a., and 8% p.a. ; these are not upper or lower limits of what you might get back. For more details on risk factors, terms and conditions please read sales brochure carefully."

                            , small_normal));

            BI_Pdftable_BonusRates2_cell2.setPadding(5);

            BI_Pdftable_BonusRates2.addCell(BI_Pdftable_BonusRates2_cell2);
            document.add(BI_Pdftable_BonusRates2);

            PdfPTable BI_Pdftable_BonusRates3 = new PdfPTable(1);
            BI_Pdftable_BonusRates3.setWidthPercentage(100);
            PdfPCell BI_Pdftable_BonusRates3_cell2 = new PdfPCell(
                    new Paragraph(
                            "3. All Benefit amount are derived on the assumption that the policies are ''in-force''",
                            small_normal));

            BI_Pdftable_BonusRates3_cell2.setPadding(5);

            BI_Pdftable_BonusRates3.addCell(BI_Pdftable_BonusRates3_cell2);
            document.add(BI_Pdftable_BonusRates3);

            PdfPTable BI_Pdftable_BonusRates4 = new PdfPTable(1);
            BI_Pdftable_BonusRates4.setWidthPercentage(100);
            PdfPCell BI_Pdftable_BonusRates4_cell2 = new PdfPCell(
                    new Paragraph(
                            "4. In addition to Guaranteed Surrender Benefits (column 5), Surrender value of the vested bonuses will also be paid. For the purpose of guaranteed surrender value (GSV) in this illustration the surrender value of vested bonuses are not considered at all."

                            , small_normal));

            BI_Pdftable_BonusRates4_cell2.setPadding(5);

            BI_Pdftable_BonusRates4.addCell(BI_Pdftable_BonusRates4_cell2);
            document.add(BI_Pdftable_BonusRates4);

            // //////////////

            PdfPTable BI_Pdftable_BonusRates5 = new PdfPTable(1);
            BI_Pdftable_BonusRates5.setWidthPercentage(100);
            PdfPCell BI_Pdftable_BonusRates_cell5 = new PdfPCell(
                    new Paragraph(
                            "5. *In case of Joint life option, we have considered the same age for Primary Annuitants and Secondary Annuitants."

                            , small_normal));

            BI_Pdftable_BonusRates_cell5.setPadding(5);

            BI_Pdftable_BonusRates5.addCell(BI_Pdftable_BonusRates_cell5);
            document.add(BI_Pdftable_BonusRates5);
            document.add(new_line);

            PdfPTable BI_Pdftable_BonusRates6 = new PdfPTable(1);
            BI_Pdftable_BonusRates6.setWidthPercentage(100);
            PdfPCell BI_Pdftable_BonusRates2_cell6 = new PdfPCell(
                    new Paragraph(
                            "Bonus Rates :"

                            , small_bold));
            BI_Pdftable_BonusRates6.setHorizontalAlignment(Element.ALIGN_LEFT);
            BI_Pdftable_BonusRates2_cell6.setPadding(5);

            BI_Pdftable_BonusRates6.addCell(BI_Pdftable_BonusRates2_cell6);
            document.add(BI_Pdftable_BonusRates6);

            PdfPTable BI_Pdftable_BonusRates7 = new PdfPTable(1);
            BI_Pdftable_BonusRates7.setWidthPercentage(100);
            PdfPCell BI_Pdftable_BonusRates3_cell7 = new PdfPCell(
                    new Paragraph(
                            "This is a with profit plan and participates in the profits of the company’s life insurance business. Simple reversionary bonuses are declared as a percentage rate, which apply to the sum assured of the basic policy. Terminal bonuses, if any, are declared as a percentage rate, which apply to the vested reversionary bonus.\n" +
                                    "The bonus rates in the benefit illustration are constant. However, in practice, future bonuses are not guaranteed and will depend on future profits. Therefore, the bonuses are shown as non-guaranteed benefits and are calculated so that they are consistent with the two projected investment return assumptions of 4% and 8% per annum. ",
                            small_normal));

            BI_Pdftable_BonusRates3_cell7.setPadding(5);

            BI_Pdftable_BonusRates7.addCell(BI_Pdftable_BonusRates3_cell7);
            document.add(BI_Pdftable_BonusRates7);
            document.add(new_line);

            PdfPTable BI_Pdftable_BonusRates71 = new PdfPTable(1);
            BI_Pdftable_BonusRates71.setWidthPercentage(100);
            PdfPCell BI_Pdftable_BonusRates3_cell71 = new PdfPCell(
                    new Paragraph(
                            "Important : ",
                            small_normal));

            BI_Pdftable_BonusRates3_cell71.setPadding(5);

            BI_Pdftable_BonusRates71.addCell(BI_Pdftable_BonusRates3_cell71);
            document.add(BI_Pdftable_BonusRates71);

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
                            "You may have to undergo Medical tests based on our underwriting requirements, if SBI Life - Preferred Term Rider has been opted",
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
                                    + "     having received the information with respect to the above, have understood the above statement before entering into the contract.",
                            small_bold));

            BI_Pdftable26_cell1.setPadding(5);

            BI_Pdftable26.addCell(BI_Pdftable26_cell1);
            document.add(BI_Pdftable26);
            document.add(BI_Pdftable26_cell1);
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

        } catch (Exception e) {
            e.printStackTrace();
            Log.i(getLocalClassName(), e.toString() + "Error in creating pdf");
        }
    }

    private void showSaralPensionOutputPg(SaralPensionBean saralPensionBean) {
        bussIll = new StringBuilder();
        double discountPercentage = 0;
        retVal = new StringBuilder();
        TextView op = new TextView(this);
        TextView op1 = new TextView(this);
        TextView op2 = new TextView(this);
        TextView op3 = new TextView(this);
        TextView op4 = new TextView(this);
        TextView op5 = new TextView(this);
        TextView op6 = new TextView(this);
        TextView op7 = new TextView(this);

        SaralPensionBusinessLogic SP_BusinessLogic = new SaralPensionBusinessLogic(
                saralPensionBean);
        CommonForAllProd cfap = new CommonForAllProd();

        int _year_F = 0;
        String premInstBasicFirstYear, premInstBasicSecondYear, premInstFYRider, premInstSYRider, premiumSingleInstBasicRider, ServiceTaxRiderBasic;
        int year_F = 0;
        double _vestingBenefit1 = 0;
        double _nonGuaranVestingBenefit_4Percent1 = 0;
        double _nonGuaranVestingBenefit_4Percent2 = 0;
        double _nonGuaranVestingBenefit_8Percent1 = 0;
        double _nonGuaranVestingBenefit_8Percent2 = 0;
        double vestingBenefit1 = 0;
        double nonGuaranVestingBenefit_4Percent1 = 0;
        double nonGuaranVestingBenefit_4Percent2 = 0;
        double nonGuaranVestingBenefit_8Percent1 = 0;
        double nonGuaranVestingBenefit_8Percent2 = 0;
        double getGuarDeathBenForMat = 0;

        double _vestingBenefit = 0, _vestingBenefit_4_pr = 0, _vestingBenefit_8_pr = 0;
        double _nonGuaranVestingBenefit_4Percent = 0;
        double _nonGuaranVestingBenefit_8Percent = 0;
        double vestingBenefit = 0, vestingBenefit_4_pr = 0, vestingBenefit_8_pr = 0, vestingBenefitMin = 0, vestingBenefit11 = 0;
        double nonGuaranVestingBenefit_4Percent = 0;
        double nonGuaranVestingBenefit_8Percent = 0, totalBasePremiumPaid = 0, cummulativTotalBasePremiumPaid = 0, _totalBasePremiumPaid = 0, _cummulativTotalBasePremiumPaid = 0, guaranDeathBenfit = 0, nonGuaranDeathBenfit_4Percent = 0, nonGuaranDeathBenfit_8Percent = 0, _guaranDeathBenfit = 0, _nonGuaranDeathBenfit_4Percent = 0, _nonGuaranDeathBenfit_8Percent = 0, cal_of_colm_k = 0, _cal_of_colm_k = 0, deathBenefit_4Percent = 0, _deathBenefit_4Percent = 0, _deathBenefit_8Percent = 0, deathBenefit_8Percent = 0, PUV_4Percent = 0, _PUV_4Percent = 0, PUV_8Percent = 0, _PUV_8Percent = 0, guaranSurrBenefit = 0, _guaranSurrBenefit = 0, nonGuaranSurrBen_4Pr = 0, _nonGuaranSurrBen_4Pr = 0, nonGuaranSurrBen_8Pr = 0, _nonGuaranSurrBen_8Pr = 0, surrenderBenefit_4Pr = 0, _surrenderBenefit_4Pr = 0, surrenderBenefit_8Pr = 0, _surrenderBenefit_8Pr = 0, annuityAmount_4Pr = 0, _annuityAmount_4Pr = 0, annuityAmount_MiniBen = 0, annuityAmount_8Pr = 0, _annuityAmount_Min = 0, _annuityAmount_8Pr = 0, maxVestingBenefitMin = 0, maxVestingBenefit_4Pr = 0, maxVestingBenefit_8Pr = 0, totalMaturityBen4per, sumGuaranteedAddition = 0, sumTotalBasePrem = 0, totalMaturity4per = 0, totalMaturity8per = 0, totDeathBen4per = 0, totDeathBen8per = 0;
        double guaranDeathBenfit11 = 0, _guaranDeathBenfit11 = 0;
//		String getGuarDeathBenForMat;

        double basicPremium = SP_BusinessLogic.getBasicPremium();
        // System.out.println("basicprem "+basicPremium);
        double discount = SP_BusinessLogic.getDiscount();
        // System.out.println("discount "+discount);
        double annualPremWithDiscount = (basicPremium - discount);
        System.out.println("annualPremWithDiscount " + annualPremWithDiscount);

        double modalPremiumWithoutDiscount = SP_BusinessLogic
                .getModalLoading(basicPremium);
        // System.out.println("modalPremiumWithoutDiscount "+modalPremiumWithoutDiscount);

        double modalLoading = SP_BusinessLogic.getModalLoading(annualPremWithDiscount);
        String modalLoadingPrem = cfap.getRoundUp(cfap.getStringWithout_E(modalLoading));
        System.out.println("modalLoading " + modalLoading);

        double PTR_Premium = SP_BusinessLogic.getPTRPremium();
        // System.out.println("PTR premium "+PTR_Premium);
        double PTR_Discount = SP_BusinessLogic.getPTRDiscount();
        // System.out.println("PTR discount "+PTR_Discount);
        double annualPTRPremiumWithDiscount = (PTR_Premium - PTR_Discount);
        // System.out.println("annualPTRPremiumWithDiscount "+annualPTRPremiumWithDiscount);
        double PTR_ModalLoading = SP_BusinessLogic
                .getPTRModalLoading(annualPTRPremiumWithDiscount);
        // System.out.println("PTR_ModalLoading "+PTR_ModalLoading);

        /** Added by Vrushali on 07-Aug-2015 start **/
        double PTR_ModalPremiumWithoutDiscount = SP_BusinessLogic
                .getPTRModalLoading(PTR_Premium);
//		System.out.println("PTR_ModalPremiumWithoutDiscount "
//				+ PTR_ModalPremiumWithoutDiscount);

        double totalPremiumWithoutDiscount = modalPremiumWithoutDiscount
                + PTR_ModalPremiumWithoutDiscount;
//		System.out.println("totalPremiumWithoutDiscount "
//				+ totalPremiumWithoutDiscount);
        /** Added by Vrushali on 07-Aug-2015 start **/

        double totalPremiumWithoutST = Double.parseDouble(cfap.getRoundUp(cfap
                .getStringWithout_E(modalPremiumWithoutDiscount + PTR_ModalLoading)));
        System.out.println("totalPremiumWithoutST " + totalPremiumWithoutST);

        double totalPremiumWithoutservicetax = Double.parseDouble(cfap.getRoundUp
                (cfap.getStringWithout_E(modalLoading)));

        /** Modified by Priyanka on 25-08-2015 Start */
        /** Modified by vrushali on 05 Aug 2015 Start */

        /*** modified by Akshaya on 20-MAY-16 start **/
        boolean isKerlaDiscount = saralPensionBean.isKerlaDisc();

		/*double basicServiceTax = SP_BusinessLogic.getServiceTax(
                totalPremiumWithoutST, saralPensionBean.getJkResident(),
                "basic");
        double SBCServiceTax = SP_BusinessLogic.getServiceTax(
                totalPremiumWithoutST, saralPensionBean.getJkResident(), "SBC");
        double KKCServiceTax = SP_BusinessLogic.getServiceTax(
				totalPremiumWithoutST, saralPensionBean.getJkResident(), "KKC");*/

        double basicServiceTax = 0;
        double SBCServiceTax = 0;
        double KKCServiceTax = 0;
        double kerlaServiceTax = 0;
        double KeralaCessServiceTax = 0;

        if (isKerlaDiscount) {
            basicServiceTax = SP_BusinessLogic.getServiceTax(Double.parseDouble(modalLoadingPrem), saralPensionBean.getJkResident(), saralPensionBean.isKerlaDisc(), "basic");
            kerlaServiceTax = SP_BusinessLogic.getServiceTax(Double.parseDouble(modalLoadingPrem), saralPensionBean.getJkResident(), saralPensionBean.isKerlaDisc(), "KERALA");
            KeralaCessServiceTax = SP_BusinessLogic.getServiceTax(Double.parseDouble(modalLoadingPrem), saralPensionBean.getJkResident(), saralPensionBean.isKerlaDisc(), "KKC");
        } else {
            basicServiceTax = SP_BusinessLogic.getServiceTax(Double.parseDouble(modalLoadingPrem), saralPensionBean.getJkResident(), saralPensionBean.isKerlaDisc(), "basic");
            SBCServiceTax = SP_BusinessLogic.getServiceTax(Double.parseDouble(modalLoadingPrem), saralPensionBean.getJkResident(), saralPensionBean.isKerlaDisc(), "SBC");
            KKCServiceTax = SP_BusinessLogic.getServiceTax(Double.parseDouble(modalLoadingPrem), saralPensionBean.getJkResident(), saralPensionBean.isKerlaDisc(), "KKC");
        }


        double FYserviceTax = basicServiceTax + SBCServiceTax + KKCServiceTax + kerlaServiceTax;
        System.out.println("FYserviceTax " + FYserviceTax);

        premInstBasicFirstYear = cfap.getRoundUp(cfap.getStringWithout_E(Double.parseDouble(modalLoadingPrem) + (FYserviceTax)));
        System.out.println("premInstBasicFirstYear " + premInstBasicFirstYear);
        System.out.println("FYserviceTax " + FYserviceTax);

        String premiumBasicRider = cfap.getRound(cfap.getStringWithout_E(PTR_ModalLoading));
        premiumSingleInstBasicRider = cfap.getRound(cfap.getStringWithout_E(Double.parseDouble(premiumBasicRider)));

        //  Added By Saurabh Jain on 14/05/2019 End

        //  Added By Saurabh Jain on 18/01/2020 End

		/*double FYtotalPremiumWithST = Double.parseDouble(cfap.getRoundUp(cfap
                .getStringWithout_E(totalPremiumWithoutST)))
                + Double.parseDouble(cfap.getRoundUp(cfap
						.getStringWithout_E(FYserviceTax)));*/

        double FYtotalPremiumWithST = Double.parseDouble(cfap.getRoundUp(cfap
                .getStringWithout_E(modalLoading)))
                + Double.parseDouble(cfap.getRoundUp(premiumSingleInstBasicRider));

		/*double basicServiceTaxSecondYear = SP_BusinessLogic
                .getServiceTaxSecondYear(totalPremiumWithoutST,
                        saralPensionBean.getJkResident(), "basic");
        double SBCServiceTaxSecondYear = SP_BusinessLogic
                .getServiceTaxSecondYear(totalPremiumWithoutST,
                        saralPensionBean.getJkResident(), "SBC");
        double KKCServiceTaxSecondYear = SP_BusinessLogic
                .getServiceTaxSecondYear(totalPremiumWithoutST,
						saralPensionBean.getJkResident(), "KKC");*/

        double basicServiceTaxSecondYear = 0;
        double SBCServiceTaxSecondYear = 0;
        double KKCServiceTaxSecondYear = 0;

        double kerlaServiceTaxSecondYear = 0;
        double KeralaCessServiceTaxSecondYear = 0;
        double SYserviceTax = 0;
        double SYtotalPremiumWithST = 0;
        if (!saralPensionBean.getPremFreq().equals("Single")) {
//			if(isKerlaDiscount){
//		 basicServiceTaxSecondYear = SP_BusinessLogic.getServiceTaxSecondYear(totalPremiumWithoutST,saralPensionBean.getJkResident(),"basic");
//		 kerlaServiceTaxSecondYear = SP_BusinessLogic.getServiceTaxSecondYear(totalPremiumWithoutST,saralPensionBean.getJkResident(),"KERALA");
//		 KeralaCessServiceTaxSecondYear =kerlaServiceTaxSecondYear-basicServiceTaxSecondYear;

            if (isKerlaDiscount) {
                basicServiceTaxSecondYear = SP_BusinessLogic.getServiceTaxSecondYear(Double.parseDouble(modalLoadingPrem), saralPensionBean.getJkResident(), saralPensionBean.isKerlaDisc(), "basic");
                kerlaServiceTaxSecondYear = SP_BusinessLogic.getServiceTaxSecondYear(Double.parseDouble(modalLoadingPrem), saralPensionBean.getJkResident(), saralPensionBean.isKerlaDisc(), "KERALA");
                KeralaCessServiceTaxSecondYear = SP_BusinessLogic.getServiceTaxSecondYear(Double.parseDouble(modalLoadingPrem), saralPensionBean.getJkResident(), saralPensionBean.isKerlaDisc(), "KKC");
                ;


                SYserviceTax = basicServiceTaxSecondYear + kerlaServiceTaxSecondYear;
                System.out.println("SYserviceTax " + SYserviceTax);

                SYtotalPremiumWithST = Double.parseDouble(cfap.getRoundUp(cfap
                        .getStringWithout_E(Double.parseDouble(modalLoadingPrem))))
                        + Double.parseDouble(cfap.getRoundUp(cfap
                        .getStringWithout_E(SYserviceTax)));
            } else {
                basicServiceTaxSecondYear = SP_BusinessLogic.getServiceTaxSecondYear(Double.parseDouble(modalLoadingPrem), saralPensionBean.getJkResident(), saralPensionBean.isKerlaDisc(), "basic");
                SBCServiceTaxSecondYear = SP_BusinessLogic.getServiceTaxSecondYear(Double.parseDouble(modalLoadingPrem), saralPensionBean.getJkResident(), saralPensionBean.isKerlaDisc(), "SBC");
                KKCServiceTaxSecondYear = SP_BusinessLogic.getServiceTaxSecondYear(Double.parseDouble(modalLoadingPrem), saralPensionBean.getJkResident(), saralPensionBean.isKerlaDisc(), "KKC");


                SYserviceTax = basicServiceTaxSecondYear + SBCServiceTaxSecondYear + KKCServiceTaxSecondYear;

                System.out.println("basicServiceTaxSecondYear " + basicServiceTaxSecondYear);

                SYtotalPremiumWithST = Double.parseDouble(cfap.getRoundUp(cfap
                        .getStringWithout_E(modalLoading)))
                        + Double.parseDouble(cfap.getRoundUp(cfap
                        .getStringWithout_E(SYserviceTax)));
            }
        }


        if (saralPensionBean.getPremFreq().equals("Single")) {
            premInstBasicSecondYear = "0";
        } else {
            premInstBasicSecondYear = cfap.getRoundUp(cfap.getStringWithout_E(Double.parseDouble(modalLoadingPrem) + SYserviceTax));
        }

        /************Rider Service*******************/
        double basicServiceTaxRider = 0;
        double SBCServiceTaxRider = 0;
        double KKCServiceTaxRider = 0;
        double kerlaServiceTaxRider = 0;
        double KeralaCessServiceTaxRider = 0;
        if (isKerlaDiscount) {
            basicServiceTaxRider = SP_BusinessLogic.getServiceTaxRider(Double.parseDouble(premiumSingleInstBasicRider), saralPensionBean.getJkResident(), saralPensionBean.isKerlaDisc(), "basic");
            kerlaServiceTaxRider = SP_BusinessLogic.getServiceTaxRider(Double.parseDouble(premiumSingleInstBasicRider), saralPensionBean.getJkResident(), saralPensionBean.isKerlaDisc(), "KERALA");
            KeralaCessServiceTaxRider = SP_BusinessLogic.getServiceTaxRider(Double.parseDouble(premiumSingleInstBasicRider), saralPensionBean.getJkResident(), saralPensionBean.isKerlaDisc(), "KKC");
            ;
        } else {
            basicServiceTaxRider = SP_BusinessLogic.getServiceTaxRider(Double.parseDouble(premiumSingleInstBasicRider), saralPensionBean.getJkResident(), saralPensionBean.isKerlaDisc(), "basic");
            SBCServiceTaxRider = SP_BusinessLogic.getServiceTaxRider(Double.parseDouble(premiumSingleInstBasicRider), saralPensionBean.getJkResident(), saralPensionBean.isKerlaDisc(), "SBC");
            KKCServiceTaxRider = SP_BusinessLogic.getServiceTaxRider(Double.parseDouble(premiumSingleInstBasicRider), saralPensionBean.getJkResident(), saralPensionBean.isKerlaDisc(), "KKC");
        }

        String serviceTaxRiderFY = cfap.getStringWithout_E(basicServiceTaxRider + SBCServiceTaxRider + KKCServiceTax + kerlaServiceTaxRider);
        System.out.println("serviceTaxRider 1 " + serviceTaxRiderFY);

        /**************************rider 2nd year*************************/

        double basicServiceTaxRiderSecondYear = 0;
        double SBCServiceTaxRiderSecondYear = 0;
        double KKCServiceTaxRiderSecondYear = 0;
        double kerlaServiceTaxRiderSecondYear = 0;
        double KeralaCessServiceTaxRiderSecondYear = 0;
        if (isKerlaDiscount) {
            basicServiceTaxRiderSecondYear = SP_BusinessLogic.getServiceTaxRiderSecondYear(Double.parseDouble(premiumSingleInstBasicRider), saralPensionBean.getJkResident(), saralPensionBean.isKerlaDisc(), "basic");
            kerlaServiceTaxRiderSecondYear = SP_BusinessLogic.getServiceTaxRiderSecondYear(Double.parseDouble(premiumSingleInstBasicRider), saralPensionBean.getJkResident(), saralPensionBean.isKerlaDisc(), "KERALA");
            KeralaCessServiceTaxRiderSecondYear = SP_BusinessLogic.getServiceTaxRider(Double.parseDouble(premiumSingleInstBasicRider), saralPensionBean.getJkResident(), saralPensionBean.isKerlaDisc(), "KKC");
            ;
        } else {
            basicServiceTaxRiderSecondYear = SP_BusinessLogic.getServiceTaxRiderSecondYear(Double.parseDouble(premiumSingleInstBasicRider), saralPensionBean.getJkResident(), saralPensionBean.isKerlaDisc(), "basic");
            SBCServiceTaxRiderSecondYear = SP_BusinessLogic.getServiceTaxRiderSecondYear(Double.parseDouble(premiumSingleInstBasicRider), saralPensionBean.getJkResident(), saralPensionBean.isKerlaDisc(), "SBC");
            KKCServiceTaxRiderSecondYear = SP_BusinessLogic.getServiceTaxRiderSecondYear(Double.parseDouble(premiumSingleInstBasicRider), saralPensionBean.getJkResident(), saralPensionBean.isKerlaDisc(), "KKC");
        }

        String serviceTaxRiderSecondYear = cfap.getStringWithout_E(basicServiceTaxRiderSecondYear + SBCServiceTaxRiderSecondYear + KKCServiceTaxRiderSecondYear + kerlaServiceTaxRiderSecondYear);


        premInstFYRider = cfap.getStringWithout_E(Double.parseDouble(premiumSingleInstBasicRider) + Double.parseDouble(serviceTaxRiderFY));
//		System.out.println("premiumSingleInstBasicRider " + premiumSingleInstBasicRider);
//		System.out.println("serviceTaxRiderFY " + serviceTaxRiderFY);
        System.out.println("premInstFYRider " + premInstFYRider);
        premInstSYRider = cfap.getStringWithout_E(Double.parseDouble(premiumSingleInstBasicRider) + Double.parseDouble(serviceTaxRiderSecondYear));

//		System.out.println("premInstSYRider "+premInstSYRider);


//		double FYtotalPremiumWithST = Double.parseDouble(cfap.getRoundOffLevel2New(cfap
//				.getStringWithout_E(totalPremiumWithoutST)))
//				+ Double.parseDouble(cfap.getRound(cfap
//						.getStringWithout_E(FYserviceTax)));
//


        System.out.println("totalPremiumWithoutST " + totalPremiumWithoutST);


        String premiumSingleInstBasicWithST = cfap.getRound(cfap.getStringWithout_E(Double.parseDouble(premInstBasicFirstYear) + Double.parseDouble(premInstFYRider)));
//		premiumSingleInstBasicWithST= (cfap.getRoundOffLevel2(cfap.getStringWithout_E(modalLoading) + (FYserviceTax)) +
//				(cfap.getRoundOffLevel2(cfap.getStringWithout_E(PTR_ModalLoading)) + (Double.parseDouble(serviceTaxRiderFY))));

        System.out.println("premInstBasicFirstYear " + premInstBasicFirstYear);
//
        System.out.println("premiumSingleInstBasicWithST " + premiumSingleInstBasicWithST);
        String premiumSingleInstBasicWithSTSecondYear = cfap.getStringWithout_E(Double.parseDouble(premInstBasicSecondYear) + Double.parseDouble(premInstSYRider));


        //	System.out.println("serviceTaxRiderSecondYear "+serviceTaxRiderSecondYear);

//		System.out.println("premInstBasicSecondYear "+premInstBasicSecondYear);

        //  Added By Saurabh Jain on 14/05/2019 End

//		double serviceTax=SP_BusinessLogic.getServiceTax(Double.parseDouble(cfap.getRoundUp(""+totalPremiumWithoutST)));
//		System.out.println("service tax "+serviceTax);

//		double totalPremiumWithST=Double.parseDouble(cfap.getRoundUp(""+totalPremiumWithoutST))+Double.parseDouble(cfap.getRoundUp(""+serviceTax));
//		System.out.println("totalPremiumWithST "+totalPremiumWithST);

        /*** modified by Akshaya on 20-MAY-16 end **/

        double guaranteenVestingBenefit = SP_BusinessLogic
                .getGuaranteedVestingBenefit(saralPensionBean.getBasicSA());

        //extra output

//		for(int j=1;j<=saralPensionBean.getBasicTerm();j++)
//		{
//
//			vestingBenefit1=SP_BusinessLogic.getVestingBenefit(j,_vestingBenefit1);
//			_vestingBenefit1=vestingBenefit1;
////			System.out.println("vestingBenefit "+vestingBenefit1);
//
//			nonGuaranVestingBenefit_4Percent1=SP_BusinessLogic.getNonGuaranVestingBenefit("4%",SP_BusinessLogic.getbonusrate4(saralPensionBean.getPremFreq(), saralPensionBean.getBasicTerm()), _vestingBenefit1, j, _nonGuaranVestingBenefit_4Percent1);
//			_nonGuaranVestingBenefit_4Percent1=nonGuaranVestingBenefit_4Percent1;
////			System.out.println("nonGuaranVestingBenefit_4Percent "+_nonGuaranVestingBenefit_4Percent1);
//
//			nonGuaranVestingBenefit_4Percent2 =	_vestingBenefit1 + (SP_BusinessLogic.getNonGuarnteedDeathBenefit4per( year_F, saralPensionBean.getBasicTerm(), saralPensionBean.getBasicSA(),saralPensionBean.getPremFreq()));
//
////					saralPensionBean.getBasicTerm(), saralPensionBean.getBasicSA(), _vestingBenefit1, saralPensionBean.getPremFreq());
//			_nonGuaranVestingBenefit_4Percent2 =nonGuaranVestingBenefit_4Percent2;
//			System.out.println("nonGuaranVestingBenefit_4Percent2 "+_nonGuaranVestingBenefit_4Percent2);
//
//			nonGuaranVestingBenefit_8Percent1=SP_BusinessLogic.getNonGuaranVestingBenefit("8%",SP_BusinessLogic.getbonusrate8(saralPensionBean.getPremFreq(), saralPensionBean.getBasicTerm()), _vestingBenefit1, j, _nonGuaranVestingBenefit_8Percent1);
//			_nonGuaranVestingBenefit_8Percent1=nonGuaranVestingBenefit_8Percent1;
////			System.out.println("nonGuaranVestingBenefit_8Percent "+nonGuaranVestingBenefit_8Percent);
//
//			nonGuaranVestingBenefit_8Percent2=SP_BusinessLogic.getTotalDeathBen8per(_year_F, saralPensionBean.getBasicTerm(), saralPensionBean.getBasicSA(), vestingBenefit1, saralPensionBean.getPremFreq());
//			_nonGuaranVestingBenefit_8Percent2=nonGuaranVestingBenefit_8Percent2;
////			System.out.println("nonGuaranVestingBenefit_8Percent2 "+nonGuaranVestingBenefit_8Percent2);
//		}
        double val = 0, a = 0, val1 = 0;
        int rowNumber = 0;
        for (int j = 1; j <= saralPensionBean.getBasicTerm(); j++) {
            rowNumber++;

            year_F = rowNumber;
            _year_F = year_F;
//			System.out.println("1. year_F " + year_F);
            bussIll.append("<policyYr" + _year_F + ">" + _year_F + "</policyYr"
                    + _year_F + ">");

            bussIll.append("<age" + _year_F + ">"
                    + (saralPensionBean.getAge() + j - 1) + "</age" + _year_F
                    + ">");

            //17-01-2020
            totalBasePremiumPaid = SP_BusinessLogic.getTotalBasePremPaid(Double
                    .parseDouble(cfap.getRoundOffLevel2(cfap
                            .getStringWithout_E(totalPremiumWithoutservicetax))), year_F);
            _totalBasePremiumPaid = totalBasePremiumPaid;
//			System.out.println("2. _totalBasePremiumPaid "
//					+ _totalBasePremiumPaid);

            sumTotalBasePrem = sumTotalBasePrem + totalBasePremiumPaid;

            bussIll.append("<totalBasePremPaid" + _year_F + ">"
                    + Math.round(_totalBasePremiumPaid) + "</totalBasePremPaid"
                    + _year_F + ">");

            //17-01-2020
            String AnnulizedPremium = cfap.getRoundUp(cfap.getStringWithout_E(SP_BusinessLogic
                    .getannulizedPremFinal(_year_F, saralPensionBean.getBasicTerm(), saralPensionBean.getPremFreq(), annualPremWithDiscount)));

            if (year_F == 1) {
                double annualizedPrem = Double.parseDouble(AnnulizedPremium);
            }

            bussIll.append("<AnnulizedPremium" + _year_F + ">" + cfap.getRound(cfap
                    .getStringWithout_E(Double.parseDouble(AnnulizedPremium)))
                    + "</AnnulizedPremium" + _year_F + ">");


//			String GuaranteedAddition = cfap.getRound(cfap.getStringWithout_E(SP_BusinessLogic.getGuaranteedAddition(_year_F, saralPensionBean.getPTR_sumAssured(),sumGuaranteedAddition,saralPensionBean.getBasicTerm())));
            String GuaranteedAddition = cfap.getRound(cfap.getStringWithout_E(SP_BusinessLogic.getGuaranteedAddition(_year_F, saralPensionBean.getBasicSA(), sumGuaranteedAddition, saralPensionBean.getBasicTerm())));
            double GuranAdd = 0;

//			System.out.println("sumGuaranteedAddition " + sumGuaranteedAddition);

            /*double val=0,a=0,val1=0;*/

            double sumassured = saralPensionBean.getBasicSA();
            if (year_F == 1) {
                val = (sumassured * 0.025);
                val1 = val;
                bussIll.append("<Guaranteedadd" + _year_F + ">" + (val) + "</Guaranteedadd" + _year_F + ">");
            } else if (year_F <= 3) {
                val = (val1 + sumassured * 0.025);
                val1 = val;
                bussIll.append("<Guaranteedadd" + _year_F + ">" + (val) + "</Guaranteedadd" + _year_F + ">");

            } else if (year_F <= 5) {

                val = (val1 + sumassured * 0.0275);
                val1 = val;
                bussIll.append("<Guaranteedadd" + _year_F + ">" + (val) + "</Guaranteedadd" + _year_F + ">");

            } else if (year_F <= saralPensionBean.getBasicTerm()) {

                bussIll.append("<Guaranteedadd" + _year_F + ">" + (val) + "</Guaranteedadd" + _year_F + ">");
            }
//			bussIll.append("<Guaranteedadd" + _year_F + ">" + Double.parseDouble(GuaranteedAddition) + "</Guaranteedadd" + _year_F + ">");

//			String SurvivalBenefit =  cfap.getRound(cfap.getStringWithout_E(SP_BusinessLogic.getSurvivalBenefit(_year_F, saralPensionBean.getBasicTerm(), saralPensionBean.getBasicSA())));

            String SurvivalBenefit = "0";

            bussIll.append("<SurvivalBenefit" + _year_F + ">" + (cfap
                    .getStringWithout_E(Double.parseDouble(SurvivalBenefit)))
                    + "</SurvivalBenefit" + _year_F + ">");

            cummulativTotalBasePremiumPaid += totalBasePremiumPaid;
            _cummulativTotalBasePremiumPaid = cummulativTotalBasePremiumPaid;
//			System.out.println("3. _cummulativTotalBasePremiumPaid "
//					+ _cummulativTotalBasePremiumPaid);
            bussIll.append("<cummTotBasePrem" + _year_F + ">"
                    + Math.round(_cummulativTotalBasePremiumPaid)
                    + "</cummTotBasePrem" + _year_F + ">");

            vestingBenefit = SP_BusinessLogic.getVestingBenefit(j,
                    _vestingBenefit);

            vestingBenefit11 = SP_BusinessLogic.getVestingBenefit11(j,
                    _vestingBenefit);

            _vestingBenefit = vestingBenefit;
//			System.out.println("4. vestingBenefit " + vestingBenefit);
            bussIll.append("<vestingBenefit" + _year_F + ">" + vestingBenefit11
                    + "</vestingBenefit" + _year_F + ">");

            nonGuaranVestingBenefit_4Percent = SP_BusinessLogic
                    .getNonGuaranVestingBenefit("4%", SP_BusinessLogic
                                    .getbonusrate4(saralPensionBean.getPremFreq(),
                                            saralPensionBean.getBasicTerm()),
                            _vestingBenefit, j,
                            _nonGuaranVestingBenefit_4Percent);

            _nonGuaranVestingBenefit_4Percent = nonGuaranVestingBenefit_4Percent;
//			System.out.println("5. nonGuaranVestingBenefit_4Percent "
//					+ nonGuaranVestingBenefit_4Percent);
            bussIll.append("<nonGuaranVestingBenefit_4Percent" + _year_F + ">"
                    + _nonGuaranVestingBenefit_4Percent
                    + "</nonGuaranVestingBenefit_4Percent" + _year_F + ">");

            nonGuaranVestingBenefit_8Percent = SP_BusinessLogic
                    .getNonGuaranVestingBenefit("8%", SP_BusinessLogic
                                    .getbonusrate8(saralPensionBean.getPremFreq(),
                                            saralPensionBean.getBasicTerm()),
                            _vestingBenefit, j,
                            _nonGuaranVestingBenefit_8Percent);

            _nonGuaranVestingBenefit_8Percent = nonGuaranVestingBenefit_8Percent;
//			System.out.println("6. nonGuaranVestingBenefit_8Percent "
//					+ nonGuaranVestingBenefit_8Percent);
            bussIll.append("<nonGuaranVestingBenefit_8Percent" + _year_F + ">"
                    + _nonGuaranVestingBenefit_8Percent
                    + "</nonGuaranVestingBenefit_8Percent" + _year_F + ">");

            vestingBenefit_4_pr = Math.max(vestingBenefit,
                    nonGuaranVestingBenefit_4Percent);
            _vestingBenefit_4_pr = vestingBenefit_4_pr;
//			System.out.println("7. vestingBenefit_4_pr " + vestingBenefit_4_pr);
            bussIll.append("<vestingBenefit_4_pr" + _year_F + ">"
                    + _vestingBenefit_4_pr + "</vestingBenefit_4_pr" + _year_F
                    + ">");

            System.out.println("_vestingBenefit_4_pr " + _vestingBenefit_4_pr);
            vestingBenefit_8_pr = Math.max(vestingBenefit,
                    nonGuaranVestingBenefit_8Percent);
            _vestingBenefit_8_pr = vestingBenefit_8_pr;
//			System.out.println("8. vestingBenefit_8_pr " + vestingBenefit_8_pr);
            bussIll.append("<vestingBenefit_8_pr" + _year_F + ">"
                    + _vestingBenefit_8_pr + "</vestingBenefit_8_pr" + _year_F
                    + ">");

            cal_of_colm_k = SP_BusinessLogic.getValueOfK(_totalBasePremiumPaid,
                    _cal_of_colm_k);
            _cal_of_colm_k = cal_of_colm_k;
//			System.out.println("9. K " + _cal_of_colm_k);

            guaranDeathBenfit = SP_BusinessLogic.getGuarnteedDeathBenefit(
                    year_F, cummulativTotalBasePremiumPaid, vestingBenefit,
                    _cal_of_colm_k);
            _guaranDeathBenfit = guaranDeathBenfit;
//			System.out.println("10. guaranDeathBenfit " + guaranDeathBenfit);
			/*bussIll.append("<guaranDeathBenfit" + _year_F + ">"
                    + Math.round(_guaranDeathBenfit) + "</guaranDeathBenfit"
					+ _year_F + ">");*/

//			nonGuaranDeathBenfit_4Percent = SP_BusinessLogic
//					.getGuarnteedDeathBenefit(year_F,
//							cummulativTotalBasePremiumPaid,
//							nonGuaranVestingBenefit_4Percent, _cal_of_colm_k);
//			_nonGuaranDeathBenfit_4Percent = nonGuaranDeathBenfit_4Percent;

            nonGuaranDeathBenfit_4Percent = SP_BusinessLogic.getNonGuarnteedDeathBenefit4per(_year_F, saralPensionBean.getBasicTerm(), saralPensionBean.getBasicSA(), saralPensionBean.getPremFreq());


//			System.out.println("11. nonGuaranDeathBenfit_4Percent "
//					+ nonGuaranDeathBenfit_4Percent);
            bussIll.append("<nonGuaranDeathBenfit_4Percent" + _year_F + ">"
                    + Math.round(nonGuaranDeathBenfit_4Percent)
                    + "</nonGuaranDeathBenfit_4Percent" + _year_F + ">");


            bussIll.append("<cashBonus" + _year_F + ">" + 0 + "</cashBonus" + _year_F + ">");
//			System.out.println("nonGuaranDeathBenfit_4Percent "+nonGuaranDeathBenfit_4Percent);

//			nonGuaranDeathBenfit_8Percent = SP_BusinessLogic
//					.getGuarnteedDeathBenefit(year_F,
//							cummulativTotalBasePremiumPaid,
//							nonGuaranVestingBenefit_8Percent, _cal_of_colm_k);
//			_nonGuaranDeathBenfit_8Percent = nonGuaranDeathBenfit_8Percent;

            nonGuaranDeathBenfit_8Percent = SP_BusinessLogic.getNonGuarnteedDeathBenefit8per(_year_F, saralPensionBean.getBasicTerm(), saralPensionBean.getBasicSA(), saralPensionBean.getPremFreq());
//			System.out.println("12. nonGuaranDeathBenfit_4Percent "
//					+ nonGuaranDeathBenfit_8Percent);
            bussIll.append("<nonGuaranDeathBenfit_8Percent" + _year_F + ">"
                    + Math.round(nonGuaranDeathBenfit_8Percent)
                    + "</nonGuaranDeathBenfit_8Percent" + _year_F + ">");

            deathBenefit_4Percent = Math.max(guaranDeathBenfit,
                    nonGuaranDeathBenfit_4Percent);
            _deathBenefit_4Percent = deathBenefit_4Percent;
//			System.out.println("13. deathBenefit_4Percent "
//					+ deathBenefit_4Percent);
            bussIll.append("<deathBenefit_4Percent" + _year_F + ">"
                    + Math.round(_deathBenefit_4Percent)
                    + "</deathBenefit_4Percent" + _year_F + ">");

            deathBenefit_8Percent = Math.max(guaranDeathBenfit,
                    nonGuaranDeathBenfit_8Percent);
            _deathBenefit_8Percent = deathBenefit_8Percent;
//			System.out.println("14. deathBenefit_8Percent "
//					+ deathBenefit_8Percent);
            bussIll.append("<deathBenefit_8Percent" + _year_F + ">"
                    + Math.round(_deathBenefit_8Percent)
                    + "</deathBenefit_8Percent" + _year_F + ">");

            PUV_4Percent = SP_BusinessLogic.getPUV(year_F,
                    nonGuaranVestingBenefit_4Percent);
            _PUV_4Percent = PUV_4Percent;
//			System.out.println("15. PUV_4Percent " + PUV_4Percent);

            PUV_8Percent = SP_BusinessLogic.getPUV(year_F,
                    nonGuaranVestingBenefit_8Percent);
            _PUV_8Percent = PUV_8Percent;
//			System.out.println("16. PUV_8Percent " + PUV_8Percent);

            nonGuaranSurrBen_4Pr = SP_BusinessLogic
                    .getNonGuarnteedSurrenderBenefit(year_F, saralPensionBean.getBasicTerm(), saralPensionBean.getBasicSA(), _vestingBenefit_4_pr);
            _nonGuaranSurrBen_4Pr = nonGuaranSurrBen_4Pr;
            System.out.println("17. nonGuaranSurrBen_4Pr "
                    + nonGuaranSurrBen_4Pr);
            bussIll.append("<nonGuaranSurrBen_4Pr" + _year_F + ">"
                    + Math.round(_nonGuaranSurrBen_4Pr)
                    + "</nonGuaranSurrBen_4Pr" + _year_F + ">");


            //17-01-2020
            nonGuaranSurrBen_8Pr = SP_BusinessLogic
                    .getNonGuarnteedSurrenderBenefit8per(year_F, saralPensionBean.getBasicTerm(), saralPensionBean.getBasicSA(), _vestingBenefit_4_pr);
            _nonGuaranSurrBen_8Pr = nonGuaranSurrBen_8Pr;
            System.out.println("18. nonGuaranSurrBen_8Pr "
                    + nonGuaranSurrBen_8Pr);
            bussIll.append("<nonGuaranSurrBen_8Pr" + _year_F + ">"
                    + Math.round(_nonGuaranSurrBen_8Pr)
                    + "</nonGuaranSurrBen_8Pr" + _year_F + ">");

            guaranSurrBenefit = SP_BusinessLogic.getGuarnteedSurrenderBenefit(
                    year_F, cummulativTotalBasePremiumPaid, vestingBenefit, sumTotalBasePrem);
            _guaranSurrBenefit = guaranSurrBenefit;
//			System.out.println("18. guaranSurrBenefit " + _guaranSurrBenefit);
            bussIll.append("<guaranSurrBenefit" + _year_F + ">"
                    + cfap.getRoundUp(cfap.getStringWithout_E(_guaranSurrBenefit)) + "</guaranSurrBenefit"
                    + _year_F + ">");

			/*surrenderBenefit_4Pr = Math.max(guaranSurrBenefit,
					nonGuaranSurrBen_4Pr);*/

            surrenderBenefit_4Pr = SP_BusinessLogic.getSurrenderBenefit_4Pr(year_F, _nonGuaranVestingBenefit_4Percent);

            _surrenderBenefit_4Pr = surrenderBenefit_4Pr;
//			System.out.println("19. surrenderBenefit_4Pr "
//					+ surrenderBenefit_4Pr);
            bussIll.append("<surrenderBenefit_4Pr" + _year_F + ">"
                    + cfap.getRoundUp(cfap.getStringWithout_E(_surrenderBenefit_4Pr))
                    + "</surrenderBenefit_4Pr" + _year_F + ">");

			/*surrenderBenefit_8Pr = Math.max(guaranSurrBenefit,
					nonGuaranSurrBen_8Pr);*/

            surrenderBenefit_8Pr = SP_BusinessLogic.getSurrenderBenefit_8Pr(year_F, _nonGuaranVestingBenefit_8Percent);

            _surrenderBenefit_8Pr = surrenderBenefit_8Pr;
//			System.out.println("20. surrenderBenefit_8Pr "
//					+ surrenderBenefit_8Pr);
            bussIll.append("<surrenderBenefit_8Pr" + _year_F + ">"
                    + cfap.getRoundUp(cfap.getStringWithout_E(_surrenderBenefit_8Pr))
                    + "</surrenderBenefit_8Pr" + _year_F + ">");

            //17-01-2020
            getGuarDeathBenForMat = (SP_BusinessLogic.getGuarDeathBenForMat(_year_F, saralPensionBean.getBasicTerm(), _totalBasePremiumPaid, getGuarDeathBenForMat));
            _getGuarDeathBenForMat = cfap.getRound(cfap.getStringWithout_E(getGuarDeathBenForMat));
//			System.out.println("getGuarDeathBenForMat " + _getGuarDeathBenForMat);

            //17-01-2020
            totalMaturity4per = SP_BusinessLogic.getTotalMaturityBenefit4per(_year_F,
                    saralPensionBean.getBasicTerm(), saralPensionBean.getBasicSA(), _nonGuaranVestingBenefit_4Percent, Double.parseDouble(_getGuarDeathBenForMat), saralPensionBean.getPremFreq());
            guaranDeathBenfit11 = SP_BusinessLogic.getGuarnteedDeathBenefit11(
                    year_F, cummulativTotalBasePremiumPaid, val,
                    _cal_of_colm_k, _getGuarDeathBenForMat);
            _guaranDeathBenfit11 = guaranDeathBenfit11;
//			System.out.println("10. guaranDeathBenfit " + guaranDeathBenfit);
            bussIll.append("<guaranDeathBenfit" + _year_F + ">"
                    + Math.round(_guaranDeathBenfit11) + "</guaranDeathBenfit"
                    + _year_F + ">");
            bussIll.append("<totalMaturityBen4per" + _year_F + ">"
                    + Math.round(totalMaturity4per)
                    + "</totalMaturityBen4per" + _year_F + ">");

            totalMaturity8per = SP_BusinessLogic.getTotalMaturityBenefit8per(_year_F,
                    saralPensionBean.getBasicTerm(), saralPensionBean.getBasicSA(), _nonGuaranVestingBenefit_8Percent, Double.parseDouble(_getGuarDeathBenForMat), saralPensionBean.getPremFreq());

            bussIll.append("<totalMaturityBen8per" + _year_F + ">"
                    + Math.round(totalMaturity8per)
                    + "</totalMaturityBen8per" + _year_F + ">");

            totDeathBen4per = SP_BusinessLogic.getTotalDeathBen4per(_year_F, saralPensionBean.getBasicTerm(), Double.parseDouble(_getGuarDeathBenForMat), _nonGuaranVestingBenefit_4Percent, sumTotalBasePrem);

            bussIll.append("<totDeathBen4per" + _year_F + ">"
                    + (cfap.getRoundUp(cfap.getStringWithout_E(totDeathBen4per)))
                    + "</totDeathBen4per" + _year_F + ">");

            totDeathBen8per = SP_BusinessLogic.getTotalDeathBen8per(_year_F, saralPensionBean.getBasicTerm(), Double.parseDouble(_getGuarDeathBenForMat), _nonGuaranVestingBenefit_8Percent, sumTotalBasePrem);

            bussIll.append("<totDeathBen8per" + _year_F + ">"
                    + (cfap.getRoundUp(cfap.getStringWithout_E(totDeathBen8per)))
                    + "</totDeathBen8per" + _year_F + ">");


            if (maxVestingBenefit_4Pr < vestingBenefit_4_pr)
                maxVestingBenefit_4Pr = vestingBenefit_4_pr;
            if (maxVestingBenefit_8Pr < vestingBenefit_8_pr)
                maxVestingBenefit_8Pr = vestingBenefit_8_pr;
            if (maxVestingBenefitMin < vestingBenefit)
                maxVestingBenefitMin = vestingBenefit;
        }

        annuityAmount_4Pr = SP_BusinessLogic.getAnnuityAmount(totalMaturity4per);
        _annuityAmount_4Pr = annuityAmount_4Pr;
//		System.out.println(" annuityAmount_4Pr " + _annuityAmount_4Pr);

        annuityAmount_8Pr = SP_BusinessLogic.getAnnuityAmount(totalMaturity8per);
        _annuityAmount_8Pr = annuityAmount_8Pr;
//		System.out.println(" annuityAmount_8Pr " + _annuityAmount_8Pr);

        annuityAmount_MiniBen = SP_BusinessLogic.getAnnuityAmount(vestingBenefit11);
        _annuityAmount_Min = annuityAmount_MiniBen;
        System.out.println("_annuityAmount_Min " + _annuityAmount_Min);

        //String valPremiumError=valBasicPremium(modalLoading,saralPensionBean.getPremiumFreq());
        //String valRiderPremiumError=valRiderPremium(basicPremium,PTR_Premium );

        //added by sujata on 13-01-2020
        for (int j = 1; j <= saralPensionBean.getBasicTerm(); j++) {
            vestingBenefit1 = SP_BusinessLogic.getVestingBenefit(j, _vestingBenefit1);
            _vestingBenefit1 = vestingBenefit1;
//			System.out.println("vestingBenefit "+vestingBenefit1);

            nonGuaranVestingBenefit_4Percent1 = SP_BusinessLogic.getNonGuaranVestingBenefit("4%", SP_BusinessLogic.getbonusrate4(saralPensionBean.getPremFreq(), saralPensionBean.getBasicTerm()), _vestingBenefit1, j, _nonGuaranVestingBenefit_4Percent1);
            _nonGuaranVestingBenefit_4Percent1 = nonGuaranVestingBenefit_4Percent1;
//			System.out.println("nonGuaranVestingBenefit_4Percent "+_nonGuaranVestingBenefit_4Percent1);

            nonGuaranVestingBenefit_4Percent2 = _vestingBenefit1 + (SP_BusinessLogic.getNonGuarnteedDeathBenefit4per(year_F, saralPensionBean.getBasicTerm(), saralPensionBean.getBasicSA(), saralPensionBean.getPremFreq()));

//			saralPensionBean.getBasicTerm(), saralPensionBean.getBasicSA(), _vestingBenefit1, saralPensionBean.getPremFreq());
            _nonGuaranVestingBenefit_4Percent2 = nonGuaranVestingBenefit_4Percent2;
//			System.out.println("nonGuaranVestingBenefit_4Percent2 "+_nonGuaranVestingBenefit_4Percent2);

            nonGuaranVestingBenefit_8Percent1 = SP_BusinessLogic.getNonGuaranVestingBenefit("8%", SP_BusinessLogic.getbonusrate8(saralPensionBean.getPremFreq(), saralPensionBean.getBasicTerm()), _vestingBenefit1, j, _nonGuaranVestingBenefit_8Percent1);
            _nonGuaranVestingBenefit_8Percent1 = nonGuaranVestingBenefit_8Percent1;
//			System.out.println("nonGuaranVestingBenefit_8Percent "+nonGuaranVestingBenefit_8Percent);
            // double _getGuarDeathBenForMat ,double _nonGuaranVestingBenefit_8Percent, double sumTotalBasePrem
            nonGuaranVestingBenefit_8Percent2 = SP_BusinessLogic.getTotalDeathBen8per(_year_F, saralPensionBean.getBasicTerm(), Double.parseDouble(_getGuarDeathBenForMat), _nonGuaranVestingBenefit_8Percent, sumTotalBasePrem);
            _nonGuaranVestingBenefit_8Percent2 = nonGuaranVestingBenefit_8Percent2;
//			System.out.println("nonGuaranVestingBenefit_8Percent2 "+_nonGuaranVestingBenefit_8Percent2);

//			getGuarDeathBenForMat = getGuarDeathBenForMat + (SP_BusinessLogic.getGuarDeathBenForMat(_year_F, saralPensionBean.getBasicTerm(), _totalBasePremiumPaid));
//			_getGuarDeathBenForMat =  cfap.getRound(cfap.getStringWithout_E(getGuarDeathBenForMat));
//			System.out.println("getGuarDeathBenForMat " + _getGuarDeathBenForMat);
        }
        /************************************* output Starts here *******************************************************/

        if (saralPensionBean.getStaffDisc()) {
            staffStatus = "sbi";
            // disc_Basic_SelFreq
        } else
            staffStatus = "none";

        discountPercentage = SP_BusinessLogic.getStaffRebate();
        /* Added by Akshaya on 06-AUG-15 end ***/

        try {
            retVal.append("<?xml version='1.0' encoding='utf-8' ?><SaralPension>");
            retVal.append("<errCode>0</errCode>");
            retVal.append("<staffStatus>" + staffStatus + "</staffStatus>");

            retVal.append("<FundValueAtVesting4>" + cfap.getStringWithout_E(totalMaturity4per) + "</FundValueAtVesting4>");
            //		System.out.println("nonGuaranVestingBen4 " + nonGuaranVestingBenefit_4Percent2 );
            retVal.append("<FundValueAtVesting8>" + cfap.getStringWithout_E(totalMaturity8per) + "</FundValueAtVesting8>");

            /* Added by Vrushali on 06-AUG-15 start ***/
            retVal.append("<staffStatus>").append(staffStatus).append("</staffStatus>");
            retVal.append("<staffRebate>").append(cfap.getStringWithout_E(discountPercentage)).append("</staffRebate>");
            retVal.append("<InstmntPrem>").append(cfap.getRoundUp(cfap
                    .getStringWithout_E(totalPremiumWithoutDiscount))).append("</InstmntPrem>");
            retVal.append("<basicPremWithoutDisc>").append(cfap.getRoundOffLevel2(cfap
                    .getStringWithout_E(modalPremiumWithoutDiscount))).append("</basicPremWithoutDisc>");
            retVal.append("<basicPremWithoutDiscSA>").append(cfap.getRoundOffLevel2(cfap
                    .getStringWithout_E(basicPremium))).append("</basicPremWithoutDiscSA>");
            retVal.append("<premPTRWithoutDisc>").append(cfap.getRoundOffLevel2(cfap
                    .getStringWithout_E(PTR_ModalPremiumWithoutDiscount))).append("</premPTRWithoutDisc>");
            retVal.append("<premPTRWithoutDiscSA>").append(cfap.getRoundOffLevel2(cfap
                    .getStringWithout_E(PTR_Premium))).append("</premPTRWithoutDiscSA>");
            /* Added by Vrushali on 06-AUG-15 end ***/

            retVal.append("<premiumSingleInstBasicRider>" + premiumSingleInstBasicRider + "</premiumSingleInstBasicRider>");
            retVal.append("<premInstBasicFirstYear>" + premInstBasicFirstYear + "</premInstBasicFirstYear>");
            retVal.append("<premInstBasicSecondYear>" + premInstBasicSecondYear + "</premInstBasicSecondYear>");
            retVal.append("<premInstFYRider>" + premInstFYRider + "</premInstFYRider>");
            retVal.append("<premInstSYRider>" + premInstSYRider + "</premInstSYRider>");
            retVal.append("<VestingMinAssBen>" + cfap.getStringWithout_E(vestingBenefit11) + "</VestingMinAssBen>");

            retVal.append("<premPTRWithoutDiscSA>"
                    + cfap.getRoundOffLevel2(cfap
                    .getStringWithout_E(PTR_Premium))
                    + "</premPTRWithoutDiscSA>");

            /* Modified by Vrushali on 06-Aug-15 **/
            retVal.append("<installmntPrem>").append(cfap.getRoundUp(cfap
                    .getStringWithout_E(totalPremiumWithoutST)))
                    .append("</installmntPrem>")
                    .append("<servceTax>")
                    .append(Math.round(FYserviceTax))
                    .append("</servceTax>")
                    .append("<instalmntPremWTServcTax>")
                    .append(cfap.getRoundUp(cfap
                            .getStringWithout_E((Double.parseDouble(premiumSingleInstBasicWithST)))))
                    .append("</instalmntPremWTServcTax>").append("<SYservceTax>").append(Math.round(SYserviceTax)).append("</SYservceTax>")
                    .append("<SYinstalmntPremWTServcTax>")
                    .append(cfap.getRoundUp(cfap.getStringWithout_E(
                            Double.parseDouble(premiumSingleInstBasicWithSTSecondYear))))
                    .append("</SYinstalmntPremWTServcTax>").append("<basePremExcludngST>").append(cfap.getRoundUp(cfap.getStringWithout_E(modalLoading))).append("</basePremExcludngST>").append("<guarnVestngBen>").append(cfap.getStringWithout_E(guaranteenVestingBenefit)).append("</guarnVestngBen>")
                    .append("<riderPrem>").append(cfap.getRoundUp_Level2(cfap.getStringWithout_E(PTR_ModalLoading)))
                    .append("</riderPrem>").append("<vestingAge>")
                    .append(saralPensionBean.getAge() + saralPensionBean.getBasicTerm()).append("</vestingAge>")
                    .append("<annuityPayout_4_Pr>").append(cfap.getStringWithout_E(annuityAmount_4Pr))
                    .append("</annuityPayout_4_Pr>").append("<annuityPayout_8_Pr>")
                    .append(cfap.getStringWithout_E(annuityAmount_8Pr)).append("</annuityPayout_8_Pr>")
                    .append("<premiumRate>").append(SP_BusinessLogic.getPremiumRate())
                    .append("</premiumRate>").append("<basicServiceTax>")
                    .append(cfap.getStringWithout_E(basicServiceTax)).append("</basicServiceTax>")
                    .append("<SBCServiceTax>").append(cfap.getStringWithout_E(SBCServiceTax))
                    .append("</SBCServiceTax>").append("<KKCServiceTax>")
                    .append(cfap.getStringWithout_E(KKCServiceTax)).append("</KKCServiceTax>")
                    .append("<basicServiceTaxSecondYear>").append(cfap.getStringWithout_E(basicServiceTaxSecondYear))
                    .append("</basicServiceTaxSecondYear>").append("<SBCServiceTaxSecondYear>")
                    .append(cfap.getStringWithout_E(SBCServiceTaxSecondYear)).append("</SBCServiceTaxSecondYear>")
                    .append("<KKCServiceTaxSecondYear>").append(cfap.getStringWithout_E(KKCServiceTaxSecondYear))
                    .append("</KKCServiceTaxSecondYear>")
                    .append("<KeralaCessServiceTax>" + cfap.getStringWithout_E(KeralaCessServiceTax) + "</KeralaCessServiceTax>")
                    .append("<KeralaCessServiceTaxSecondYear>" + cfap.getStringWithout_E(KeralaCessServiceTaxSecondYear) + "</KeralaCessServiceTaxSecondYear>");

            int index = saralPensionBean.getBasicTerm();
            String nonGuaranVestingBenefit_4Percent_tag = prsObj.parseXmlTag(bussIll.toString(), "nonGuaranVestingBenefit_4Percent" + index + "");
            String nonGuaranVestingBenefit_8Percent_tag = prsObj.parseXmlTag(bussIll.toString(), "nonGuaranVestingBenefit_8Percent" + index + "");

            retVal.append("<nonGuaranVestingBenefit_4Percent" + index + ">" + nonGuaranVestingBenefit_4Percent_tag + "</nonGuaranVestingBenefit_4Percent" + index + ">");
            retVal.append("<nonGuaranVestingBenefit_8Percent" + index + ">" + nonGuaranVestingBenefit_8Percent_tag + "</nonGuaranVestingBenefit_8Percent" + index + ">");
            retVal.append("<annuityAmount_MiniBen>" + cfap.getStringWithout_E(annuityAmount_MiniBen) + "</annuityAmount_MiniBen>");

            retVal.append("<intallmentPremWithST>" + cfap.getStringWithout_E(FYtotalPremiumWithST) + "</intallmentPremWithST>");
            retVal.append(bussIll.toString());
            retVal.append("</SaralPension>");

        } catch (Exception e) {
            retVal.append("<?xml version='1.0' encoding='utf-8' ?><SaralPension>" + "<errCode>1</errCode>" + "<errorMessage>").append(e.getMessage()).append("</errorMessage></SaralPension>");
        }

        System.out.println(" Final Result : " + retVal.toString());
        saralpensionbeanstatic = saralPensionBean;
        valPremiumError = valBasicPremium(modalLoading);
        valRiderPremiumError = valRiderPremium(basicPremium, PTR_Premium);
        if (valPremiumError && valRiderPremiumError) {
            op.setText("Installment Premium is Rs "
                    + currencyFormat.format(Double.parseDouble(cfap
                    .getRoundUp(cfap
                            .getStringWithout_E(totalPremiumWithoutST)))));
            op1.setText("Base Premium Excluding Applicable Taxes is Rs "
                    + currencyFormat.format(Double.parseDouble(cfap
                    .getRoundUp(cfap.getStringWithout_E(modalLoading)))));
            op2.setText("Applicable Taxes is Rs "
                    + currencyFormat.format(Double.parseDouble(cfap
                    .getRoundUp(cfap.getStringWithout_E(FYserviceTax)))));
            op3.setText("Installment Premium With Applicable Taxes is Rs "
                    + currencyFormat.format(Double.parseDouble(cfap
                    .getStringWithout_E(FYtotalPremiumWithST))));
            op4.setText("Guaranteed Vesting Benefit is Rs "
                    + currencyFormat.format(guaranteenVestingBenefit));
            if (cb_bi_saral_pension_ptr_rider.isChecked()) {
                // System.out.println("inside rider");
                op5.setText("Rider Premium Excluding Applicable Taxes is Rs "
                        + currencyFormat.format(PTR_ModalLoading));
                // i.putExtra("op5", op5.getText().toString());
            }
            op6.setText("Non-Guaranteed Vesting Benefit with 4%pa is Rs "
                    + currencyFormat.format(nonGuaranVestingBenefit_4Percent));
            op7.setText("Non-Guaranteed Vesting Benefit with 8%pa is Rs "
                    + currencyFormat.format(nonGuaranVestingBenefit_8Percent));

            // i.putExtra("op", op.getText().toString());
            // i.putExtra("op1", op1.getText().toString());
            // i.putExtra("op2", op2.getText().toString());
            // i.putExtra("op3", op3.getText().toString());
            // i.putExtra("op4", op4.getText().toString());
            // i.putExtra("op6", op6.getText().toString());
            // i.putExtra("op7", op7.getText().toString());
            // i.putExtra("header",inputActivityHeader.getText().toString());
            // i.putExtra("pdf", retVal.toString());
            // startActivity(i);
        }
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

        String final_age = Integer.toString(age) + " yrs";

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
                    ProposerAge = final_age;
                    final_age = Integer.toString(age);
                    if (final_age.contains("-")) {
                        commonMethods.BICommonDialog(context, "Please fill Valid Birth Date");
                    } else {
                        if (18 <= age) {

                            btn_bi_saral_pension_life_assured_date.setText(date);
                            edt_bi_saral_pension_life_assured_age
                                    .setText(final_age);
                            proposer_date_of_birth = getDate1(date + "");
                            // ageInYears
                            // .setSelection(getIndex(ageInYears, final_age));
                            // valAge();
                        } else {
                            commonMethods.BICommonDialog(context, "Minimum age should be 18 yrs for proposer");
                            btn_bi_saral_pension_life_assured_date
                                    .setText("Select Date");
                            edt_bi_saral_pension_life_assured_age.setText("");
                            proposer_date_of_birth = "";
                        }
                    }
                    break;

                case 5:

                    final_age = Integer.toString(age);
                    if (final_age.contains("-")) {
                        commonMethods.dialogWarning(context, "Please fill Valid Birth Date", true);
                    } else {
                        if (cb_bi_saral_pension_ptr_rider.isChecked()) {
                            if (spnr_bi_saral_pension_premium_frequency.getSelectedItem().toString().equals("Single")) {
                                minAge = 18;
                                maxAge = 55;
                            } else {
                                minAge = 18;
                                maxAge = 50;
                            }
                        } else {
                            if (spnr_bi_saral_pension_premium_frequency
                                    .getSelectedItem().toString().equals("Single")) {
                                minAge = 18;
                                maxAge = 65;
                            } else {
                                minAge = 18;
                                maxAge = 60;
                            }
                        }


                        if (minAge <= age && age <= maxAge) {
                            lifeAssuredAge = final_age;
                            btn_bi_saral_pension_life_assured_date.setText(date);
                            edt_bi_saral_pension_life_assured_age
                                    .setText(final_age);

                            edt_bi_saral_pension_life_assured_age
                                    .setText(final_age);
                            lifeAssured_date_of_birth = getDate1(date + "");
                            clearFocusable(btn_bi_saral_pension_life_assured_date);
                            setFocusable(edt_saral_pension_contact_no);
                            edt_saral_pension_contact_no.requestFocus();

                        } else {
                            commonMethods.BICommonDialog(context, "Minimum Age should be " + minAge
                                    + "yrs and Maximum Age should be " + maxAge
                                    + " yrs For LifeAssured");
                            btn_bi_saral_pension_life_assured_date
                                    .setText("Select Date");
                            edt_bi_saral_pension_life_assured_age.setText("");
                            lifeAssured_date_of_birth = "";

                            clearFocusable(btn_bi_saral_pension_life_assured_date);
                            setFocusable(btn_bi_saral_pension_life_assured_date);
                            btn_bi_saral_pension_life_assured_date.requestFocus();
                        }
                    }
                    break;

                case 6:
                    // if (age >= 0) {
                    // proposer_Backdating_BackDate = date + "";
                    // btn_proposerdetail_personaldetail_backdatingdate
                    // .setText(proposer_Backdating_BackDate);
                    // clearFocusable(btn_proposerdetail_personaldetail_backdatingdate);
                    //
                    // setFocusable(selPTARider);
                    // selPTARider.requestFocus();
                    //
                    // } else {
                    // dialog("Please Select Valid BackDating Date", true);
                    // btn_proposerdetail_personaldetail_backdatingdate
                    // .setText("Select Date");
                    // proposer_Backdating_BackDate = "";
                    // }
                    //
                    // break;

                default:
                    break;

            }

        }
    }

    // public boolean getValueFromDatabase() {
    // // retrieving data from database
    // boolean flag = false;
    // List<M_Benefit_Illustration_Detail> data = db
    // .getBIDetail(QuatationNumber);
    // if (data.size() > 0) {
    // int i = 0;
    // output = data.get(i).getOutput();
    // input = data.get(i).getInput();
    // proposal_no = data.get(i).getProposal_no();
    // name_of_life_assured = data.get(i).getName_of_lifeAssured();
    // lifeAssured_date_of_birth = data.get(i)
    // .getLife_assured_date_of_birth();
    // proposer_date_of_birth = data.get(i).getProposer_dob();
    //
    // proposer_Is_Same_As_Life_Assured = data.get(i)
    // .getProposer_Same_As_Life_Assured();
    // proposer_Title = data.get(i).getProposer_Title();
    //
    // proposer_First_Name = data.get(i).getProposer_FirstName();
    // proposer_Middle_Name = data.get(i).getProposer_MiddleName();
    // proposer_Last_Name = data.get(i).getProposer_LastName();
    // lifeAssured_Title = data.get(i).getLifeAssured_Title();
    // lifeAssured_First_Name = data.get(i).getLifeAssured_FirstName();
    // lifeAssured_Middle_Name = data.get(i).getLifeAssured_MiddleName();
    // lifeAssured_Last_Name = data.get(i).getLifeAssured_LastName();
    // product_name = data.get(i).getProduct_name();
    // proposer_Backdating_WishToBackDate_Policy = data.get(i)
    // .getWish_to_backdate_policy();
    // proposer_Backdating_BackDate = data.get(i).getBackdate();
    //
    // name_of_proposer = data.get(i).getName_of_proposer();
    // place1 = data.get(i).getPlace1();
    // place2 = data.get(i).getPlace2();
    // date1 = data.get(i).getDate1();
    // date2 = data.get(i).getDate2();
    // agent_sign = data.get(i).getAgent_sign();
    // proposer_sign = data.get(i).getProposer_sign();
    // flg_needAnalyis = data.get(i).getneedAnalyisDone();
    // // name_of_person = data.get(i).getName_of_person();
    //
    // flag = true;
    //
    // }
    // return flag;
    // }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        /* parivartan changes */
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
                        b = MediaStore.Images.Media.getBitmap(
                                getContentResolver(), imageUri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Bitmap mFaceBitmap = b != null ? b.copy(Bitmap.Config.RGB_565, true) : null;
                    b.recycle();
                    if (mFaceBitmap != null) {
                        Bitmap scaled = Bitmap.createScaledBitmap(bmp, 230,
                                200, true);
                        photoBitmap = scaled;
                        imageButtonSaralPensionProposerPhotograph
                                .setImageBitmap(scaled);
                    } else {
                        photoBitmap = null;
                    }
                }
            }
        }
        /* end */
    }

    private void windowmessagesgin() {

        d = new Dialog(BI_SaralPensionActivity.this);
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
                Intent intent = new Intent(BI_SaralPensionActivity.this,
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


    private void validationOfMoile_EmailId() {

        edt_saral_pension_contact_no.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
                // TODO Auto-generated method stub

            }

            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
                // TODO Auto-generated method stub

            }

            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
                String abc = edt_saral_pension_contact_no.getText().toString();
                mobile_validation(abc);

            }
        });

        edt_saral_pension_Email_id.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
                // TODO Auto-generated method stub

            }

            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
                // TODO Auto-generated method stub

            }

            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
                ProposerEmailId = edt_saral_pension_Email_id.getText()
                        .toString();
                //email_id_validation(ProposerEmailId);

            }
        });

        edt_saral_pension_ConfirmEmail_id
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
                        String proposer_confirm_emailId = edt_saral_pension_ConfirmEmail_id
                                .getText().toString();
                        //      confirming_email_id(proposer_confirm_emailId);

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
            edt_saral_pension_contact_no
                    .setError("Please provide correct 10-digit mobile number");
        } else if ((number.length() == 10)) {
        }
    }

    private void email_id_validation(String email_id) {

        String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email_id);
        if (!(matcher.matches())) {
            edt_saral_pension_Email_id
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

    // Validation start here

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
                                    setFocusable(spnr_bi_saral_pension_life_assured_title);
                                    spnr_bi_saral_pension_life_assured_title
                                            .requestFocus();
                                } else if (lifeAssured_First_Name.equals("")) {
                                    setFocusable(edt_bi_saral_pension_life_assured_first_name);
                                    edt_bi_saral_pension_life_assured_first_name
                                            .requestFocus();
                                } else {
                                    setFocusable(edt_bi_saral_pension_life_assured_last_name);
                                    edt_bi_saral_pension_life_assured_last_name
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
                                setFocusable(spnr_bi_saral_pension_life_assured_title);
                                spnr_bi_saral_pension_life_assured_title
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
                                setFocusable(spnr_bi_saral_pension_life_assured_title);
                                spnr_bi_saral_pension_life_assured_title
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
                                setFocusable(spnr_bi_saral_pension_life_assured_title);
                                spnr_bi_saral_pension_life_assured_title
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

                            public void onClick(DialogInterface dialog,
                                                int which) {
                                if (lifeAssured_Title.equals("")) {
                                    // apply focusable method
                                    setFocusable(spnr_bi_saral_pension_life_assured_title);
                                    spnr_bi_saral_pension_life_assured_title
                                            .requestFocus();
                                } else if (lifeAssured_First_Name.equals("")) {

                                    edt_bi_saral_pension_life_assured_first_name
                                            .requestFocus();
                                } else {
                                    edt_bi_saral_pension_life_assured_last_name
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

                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // apply focusable method
                                setFocusable(btn_bi_saral_pension_life_assured_date);
                                btn_bi_saral_pension_life_assured_date
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

    private boolean valBasicDetail() {

        if (edt_saral_pension_contact_no.getText().toString().equals("")) {
            commonMethods.dialogWarning(context, "Please Fill  Mobile No", true);
            edt_saral_pension_contact_no.requestFocus();
            return false;
        } else if (edt_saral_pension_contact_no.getText().toString().length() != 10) {
            commonMethods.dialogWarning(context, "Please Fill 10 Digit Mobile No", true);
            edt_saral_pension_contact_no.requestFocus();
            return false;
        }/* else if (emailId.equals("")) {
            commonMethods.dialogWarning(context, "Please Fill Email Id", true);
            edt_saral_pension_Email_id.requestFocus();
            return false;

        } else if (ConfirmEmailId.equals("")) {

            commonMethods.dialogWarning(context, "Please Fill Confirm Email Id", true);
            edt_saral_pension_ConfirmEmail_id.requestFocus();
            return false;
        } else if (!ConfirmEmailId.equals(emailId)) {
            commonMethods.dialogWarning(context, "Email Id Does Not Match", true);
            return false;
        }*/ else if (!emailId.equals("")) {

            email_id_validation(emailId);
            if (validationFla1) {

                if (ConfirmEmailId.equals("")) {

                    commonMethods.dialogWarning(context, "Please Fill Confirm Email Id", true);
                    edt_saral_pension_ConfirmEmail_id.requestFocus();
                    return false;
                } else if (!ConfirmEmailId.equals(emailId)) {
                    commonMethods.dialogWarning(context, "Email Id Does Not Match", true);
                    return false;
                }

                return true;
            } else {
                commonMethods.dialogWarning(context, "Please Fill Valid Email Id", true);
                edt_saral_pension_Email_id.requestFocus();
                return false;
            }
        } else if (!ConfirmEmailId.equals("")) {

            email_id_validation(ConfirmEmailId);
            if (validationFla1) {

                if (emailId.equals("")) {
                    commonMethods.dialogWarning(context, "Please Fill Email Id", true);
                    edt_saral_pension_Email_id.requestFocus();
                    return false;

                }

                return true;
            } else {
                commonMethods.dialogWarning(context, "Please Fill Valid Confirm Email Id", true);
                edt_saral_pension_ConfirmEmail_id.requestFocus();
                return false;
            }
        } else {
            return true;
        }
    }

    // Validation
    private boolean valAge() {
        int minAge = 18;
        int maxAge = 0;
        if (spnr_bi_saral_pension_premium_frequency.getSelectedItem()
                .toString().equals("Single")) {
            maxAge = 65;
        } else {
            maxAge = 60;
        }
        if (Integer.parseInt(edt_bi_saral_pension_life_assured_age.getText()
                .toString()) < minAge
                || Integer.parseInt(edt_bi_saral_pension_life_assured_age
                .getText().toString()) > maxAge) {
            showAlert.setMessage("Enter age between " + minAge + " to "
                    + maxAge);
            showAlert.setNeutralButton("OK",
                    new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface arg0, int arg1) {
                            // TODO Auto-generated method stub
                            edt_bi_saral_pension_life_assured_age.setText("");
                        }
                    });
            showAlert.show();
            return false;
        }
        return true;
    }

    boolean valEntryAgeForRider() {

        String error = "";
        if (!(edt_bi_saral_pension_life_assured_age.getText().toString()
                .equals(""))) {
            if (Integer.parseInt(edt_bi_saral_pension_life_assured_age
                    .getText().toString()) > 50
                    && !spnr_bi_saral_pension_premium_frequency
                    .getSelectedItem().toString().equals("Single")) {
                error = "Maximum Entry Age For Preferred Term Rider is 50 years";
            } else if (Integer.parseInt(edt_bi_saral_pension_life_assured_age
                    .getText().toString()) > 55
                    && spnr_bi_saral_pension_premium_frequency
                    .getSelectedItem().toString().equals("Single")) {
                error = "Maximum Entry Age For Preferred Term Rider is 55 years";
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
            } else
                return true;
        } else {

            return true;
        }
    }

    // For PolicyTerm
    private boolean valPolicyTerm() {
        int minTerm = 0, maxTerm = 0;
        if (spnr_bi_saral_pension_premium_frequency.getSelectedItem()
                .toString().equals("Single")) {
            minTerm = Math.max(5, (40 - Integer
                    .parseInt(edt_bi_saral_pension_life_assured_age.getText()
                            .toString())));
        } else {
            minTerm = Math.max(10, (40 - Integer
                    .parseInt(edt_bi_saral_pension_life_assured_age.getText()
                            .toString())));
        }

        maxTerm = Math.min(40, (70 - Integer
                .parseInt(edt_bi_saral_pension_life_assured_age.getText()
                        .toString())));

        if (Integer.parseInt(spnr_bi_saral_pension_policyterm.getSelectedItem()
                .toString()) < minTerm
                || Integer.parseInt(spnr_bi_saral_pension_policyterm
                .getSelectedItem().toString()) > maxTerm) {
            showAlert.setMessage("Enter Policy Term between " + minTerm
                    + " to " + maxTerm);
            showAlert.setNeutralButton("OK",
                    new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface arg0, int arg1) {
                            // TODO Auto-generated method stub
                            spnr_bi_saral_pension_policyterm.setSelection(0);
                        }
                    });
            showAlert.show();
            return false;
        }
        return true;
    }

    // For SumAssured
    private boolean valSumAssured() {
        double minSA = 100000;
        String error = "";
        if (edt_bi_saral_pension_sum_assured_amount.getText().toString()
                .equals("")) {
            error = "Please enter Sum Assured.";
        } else if (Double.parseDouble(edt_bi_saral_pension_sum_assured_amount
                .getText().toString()) % 1000 != 0) {
            error = "Sum Assured should be multiple of 1,000.";
        } else if (Double.parseDouble(edt_bi_saral_pension_sum_assured_amount
                .getText().toString()) < minSA) {
            error = "Sum Assured should not be less than " + minSA + ".";
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

    // For rider term
    private boolean valRiderTerm() {
        int minRiderTerm = 0, maxRiderTerm = 0;
        if (cb_bi_saral_pension_ptr_rider.isChecked()) {

            if (spnr_bi_saral_pension_premium_frequency.getSelectedItem()
                    .toString().equals("Single")) {
                minRiderTerm = 5;
            } else {
                minRiderTerm = 10;
            }

            maxRiderTerm = Math.min(30, Math.min(Integer
                    .parseInt(spnr_bi_saral_pension_policyterm
                            .getSelectedItem().toString()), (60 - Integer
                    .parseInt(edt_bi_saral_pension_life_assured_age.getText()
                            .toString()))));
            if (Integer.parseInt(spnr_bi_saral_pension_ptr_rider_term
                    .getSelectedItem().toString()) < minRiderTerm
                    || Integer.parseInt(spnr_bi_saral_pension_ptr_rider_term
                    .getSelectedItem().toString()) > maxRiderTerm) {
                showAlert.setMessage("Enter Rider Term between " + minRiderTerm
                        + " to " + maxRiderTerm);
                showAlert.setNeutralButton("OK",
                        new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface arg0, int arg1) {
                                // TODO Auto-generated method stub
                                spnr_bi_saral_pension_ptr_rider_term
                                        .setSelection(0);
                            }
                        });
                showAlert.show();
                return false;
            }
        }
        return true;
    }

    // rider SA
    private boolean valRiderSA() {
        double minRiderLimit, maxRiderLimit;
        StringBuilder error = new StringBuilder();
        if (cb_bi_saral_pension_ptr_rider.isChecked()) {
            minRiderLimit = 25000;
            maxRiderLimit = Math.min(5000000, Double
                    .parseDouble(edt_bi_saral_pension_sum_assured_amount
                            .getText().toString()));
            if (edt_bi_saral_pension_ptr_rider_sum_assured.getText().toString()
                    .equals("")) {
                error.append("Please enter Preferred Term Rider Sum Assured in Rs. ");

            } else if (Double
                    .parseDouble(edt_bi_saral_pension_ptr_rider_sum_assured
                            .getText().toString()) % 1000 != 0) {
                error.append("\nEnter Sum assured for Preferred Term Assurance Rider in multiples of 1,000");
            } else if (Double
                    .parseDouble(edt_bi_saral_pension_ptr_rider_sum_assured
                            .getText().toString()) < minRiderLimit
                    || Double
                    .parseDouble(edt_bi_saral_pension_ptr_rider_sum_assured
                            .getText().toString()) > maxRiderLimit) {

                error.append("\nEnter Sum assured for Preferred Term Rider between "
                        + currencyFormat.format(minRiderLimit)
                        + " and "
                        + currencyFormat.format(maxRiderLimit));
            }

        }

        if (!error.toString().equals("")) {
            showAlert.setMessage(error.toString());
            showAlert.setNeutralButton("OK",
                    new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface arg0, int arg1) {
                            // TODO Auto-generated method stub

                        }

                    });
            showAlert.show();
            return false;
        }

        return true;
    }

    private boolean valBasicPremium(double modalLoading) {
        if (spnr_bi_saral_pension_premium_frequency.getSelectedItem()
                .toString().equals("Yearly")
                && modalLoading < 7500) {
            showAlert
                    .setMessage("Minimum Premium for Yearly mode under this product is Rs. 7,500");
            showAlert.setNeutralButton("OK",
                    new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
            showAlert.show();

            return false;
        } else if (spnr_bi_saral_pension_premium_frequency.getSelectedItem()
                .toString().equals("Half Yearly")
                && modalLoading < 3800) {
            showAlert
                    .setMessage("Minimum Premium for Half-Yearly mode under this product is Rs. 3,800");
            showAlert.setNeutralButton("OK",
                    new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
            showAlert.show();

            return false;
        } else if (spnr_bi_saral_pension_premium_frequency.getSelectedItem()
                .toString().equals("Monthly")
                && modalLoading < 700) {
            showAlert
                    .setMessage("Minimum Premium for Monthly mode under this product is Rs. 700");
            showAlert.setNeutralButton("OK",
                    new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
            showAlert.show();
            return false;
        } else {
            return true;
        }

    }

    public boolean valRiderPremium(double premBasic, double sumOfRiders) {
        if ((premBasic * 0.30) < sumOfRiders) {
            showAlert
                    .setMessage("Total of Rider Premium should not be greater than 30% of the Base Premium");
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

    private boolean valRiderMaturityAge() {
        if (cb_bi_saral_pension_ptr_rider.isChecked()) {

            if (!(edt_bi_saral_pension_life_assured_age.getText().toString()
                    .equals(""))) {

                if ((Integer.parseInt(edt_bi_saral_pension_life_assured_age
                        .getText().toString()) + Integer
                        .parseInt(spnr_bi_saral_pension_ptr_rider_term
                                .getSelectedItem().toString())) > 60) {
                    showAlert
                            .setMessage("Maximum maturity age for Preferred Term Rider is 60 years");
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

    public void Date() {
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());

        c.add(Calendar.DATE, 30);
    }

    public boolean valMatuirtyOption() {
        String error = "";
        if (bi_retire_smart_Matuirty_option.equals("")) {
            error = "Please Select Maturity Option";
        }

        if (!error.equals("")) {
            showAlert.setMessage(error.toString());
            showAlert.setNeutralButton("OK", new DialogInterface.OnClickListener() {
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
            showAlert.setNeutralButton("OK", new DialogInterface.OnClickListener() {
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

}



