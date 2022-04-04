package sbilife.com.pointofsale_bancaagency.products.saraljeevanbima;

import android.annotation.SuppressLint;
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

public class BI_SaralJeevanBimaActivity extends AppCompatActivity implements
        OnEditorActionListener {

    private final int SIGNATURE_ACTIVITY = 1;
    private int DIALOG_ID;
    private Context context;

    private double totInstPrem_exclST = 0, premPTA = 0, premBasic = 0,
            premADB = 0, premATPDB = 0, premCC13NonLinked = 0;
    // UI Elements
    private CheckBox cbADBRider, cbATPDBRider, cbCC13NonLinkedRider,
            cb_staffdisc;
    private Button btnSubmit, btnBack;
    private StringBuilder inputVal;
    private String premFreqOptions = "";
    private String premPayingMode = "";
    private String policyTerm = "";
    private String ageAtEntry = "";
    private String gender = "";
    private String sumAssured = "";
    private SaralJeevanBimaBean saralJeevanBimaBean;
    private EditText edt_SumAssured, edt_AdbSA, edt_AtpdbSA, edt_PtaSA,
            edt_Cc13NonLinkedSA;
    private Spinner spnr_bi_smart_money_back_gold_life_assured_title, spinnerLifeAssuredGender, spnrPremPaymentTerm, selPremFreq, spnrPremPayingMode, spnrPolicyTerm, spnrPlan, spnrAge,
            spnr_AdbTerm, spnr_AtpdbTerm, spnr_PtaTerm, spnr_Cc13NonLinkedTerm;
    private TableRow trADBRider, trADBRider2, trATPDBRider, trATPDBRider2, trCritiCareRider, trCritiCareRider2, trPreffredTermRider, trPreffredTermRider2;
    private RadioButton rb_proposerdetail_personaldetail_backdating_yes,
            rb_proposerdetail_personaldetail_backdating_no;
    private LinearLayout ll_backdating1;
    private Button btn_proposerdetail_personaldetail_backdatingdate,
            btn_MarketingOfficalDate, btn_PolicyholderDate;
    private ImageButton Ibtn_signatureofMarketing, Ibtn_signatureofPolicyHolders;
    private Dialog d;
    private DatabaseHelper db;
    private String proposer_Title = "";
    private String proposer_First_Name = "";
    private String proposer_Middle_Name = "";
    private String proposer_Last_Name = "";
    private String lifeAssured_Title = "";
    private String lifeAssured_First_Name = "";
    private String lifeAssured_Middle_Name = "";
    private String lifeAssured_Last_Name = "";
    private String name_of_life_assured = "";
    private String lifeAssured_date_of_birth = "";
    private String lifeAssuredAge = "";
    private String proposer_Backdating_WishToBackDate_Policy = "";
    private String proposer_Backdating_BackDate = "";
    private TextView btn_bi_smart_money_back_gold_life_assured_date;
    private EditText edt_bi_smart_money_back_gold_life_assured_first_name, edt_bi_smart_money_back_gold_life_assured_middle_name,
            edt_bi_smart_money_back_gold_life_assured_last_name;
    private int minAge = 0, maxAge = 0;
    private String proposer_date_of_birth = "";
    private ParseXML prsObj;
    private String proposal_no = "", name_of_proposer = "", name_of_person = "", place1 = "",
            place2 = "", date1 = "", date2 = "", agent_sign = "",
            proposer_sign = "", proposer_Is_Same_As_Life_Assured = "Y";
    private DecimalFormat currencyFormat;
    private StringBuilder bussIll = null;
    private boolean flagFirstFocus = true;
    private EditText edt_proposerdetail_basicdetail_contact_no,
            edt_proposerdetail_basicdetail_Email_id,
            edt_proposerdetail_basicdetail_ConfirmEmail_id;
    private String mobileNo = "";
    private String emailId = "";
    private String ConfirmEmailId = "";
    /* Basic Details */
    private String ProposerEmailId = "";
    private Bitmap photoBitmap;
    /* For Proposer Details */
    private String strProposerAge = "";
    private StringBuilder date;
    private String Company_policy_surrender_dec = "";
    private CheckBox cb_kerladisc, cbPTARider;
    private String str_kerla_discount = "No";
    private final String gender_proposer = "";
    private String STR_MAT_VALUE_PROD_CHANGE = "", STR_MAT_VALUE_PROD_QUT_NO = "";
    private TableRow tr_premium_paying_mode;
    private String ProposalMode = "";
    private EditText edt_bi_money_back_gold_proposer_age;
    private String str_agent_name = "";
    private Spinner spnr_is_defence_occupation, spnr_age_proof, spnr_occupation_type, spnr_occupation_rank, spnr_occupation_location;
    private String str_is_defence_occupation = "", str_age_proof = "", str_occupation_type = "", str_occupation_rank = "", str_occupation_location = "";
    private LinearLayout ll_is_defence;
    private String FLAG_OCCUPATION_EXTRA = "N";
    private String BasicPremium = "";
    private String ApplicableTax = "";
    private String PremiumwithST = "";
    private String BasicPremium_PDF = "";
    private String serviceTax;
    private TableRow tr_premium_paying_term;
    private final CommonForAllProd obj = new CommonForAllProd();
    private StringBuilder retVal = new StringBuilder();
    private int mYear;
    private int mMonth;
    private int mDay;
    private String QuatationNumber = "";
    private String planName = "";
    private boolean validationFla1 = false;
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

    private void showDateDialog() {
        DatePickerDialog dialog = new DatePickerDialog(
                context,
                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                mDateSetListener,
                mYear, mMonth, mDay);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    private CommonMethods commonMethods;
    private StorageUtils mStorageUtils;

    private String agentcode, agentMobile, agentEmail, userType;
    private String product_Code, product_UIN, product_cateogory, product_type;
    private int needAnalysis_flag = 0;
    private String na_input = null;
    private String na_output = null;
    private String bankUserType = "", transactionMode = "";
    private NeedAnalysisBIService NABIObj;
    private NA_CBI_bean na_cbi_bean;
    private File needAnalysispath, newFile, mypath;
    private ImageButton imageButtonProposerPhotograph;
    private String Check = "";
    private String latestImage = "";

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.bi_saral_jeevan_bima);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);

        commonMethods = new CommonMethods();
        mStorageUtils = new StorageUtils();
        commonMethods.setActionbarLayout(this);
        context = this;
        NABIObj = new NeedAnalysisBIService(this);
        db = new DatabaseHelper(this);
        currencyFormat = new DecimalFormat("##,##,##,###");
        Intent intent = getIntent();
        String na_flag = intent.getStringExtra("NAFlag");

        //Added Tushar Kadam Kerla Applicable Tax 7-Jun-2019
        TableRow tablerowKerlaDiscount = findViewById(R.id.tablerowKerlaDiscount);
        cb_kerladisc = findViewById(R.id.cb_kerladisc);
        cbPTARider = findViewById(R.id.cb_bi_smart_money_back_gold_pta_rider);
        commonMethods.setKerlaDiscount(context, tablerowKerlaDiscount, cb_kerladisc);
        //End Added Tushar Kadam Kerla Applicable Tax 7-Jun-2019
        try {
            agentcode = SimpleCrypto.decrypt("SBIL",
                    db.GetUserCode());

            agentMobile = SimpleCrypto.decrypt("SBIL",
                    db.GetMobileNo());
            agentEmail = SimpleCrypto.decrypt("SBIL",
                    db.GetEmailId());
            userType = SimpleCrypto.decrypt("SBIL",
                    db.GetUserType());

            /* parivartan changes */
            ProductInfo prodInfoObj = new ProductInfo(context);
            planName = getString(R.string.sbi_life_saral_jeevan_bima);
            product_Code = getString(R.string.sbi_life_saral_jeevan_bima_code);
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
                String na_dob = intent.getStringExtra("custDOB");
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


        initialiseDate();

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
        trADBRider2 = findViewById(R.id.tr_bi_smart_money_back_gold_adb_rider2);
        trATPDBRider = findViewById(R.id.tr_bi_smart_money_back_gold_atpd_rider);
        trATPDBRider2 = findViewById(R.id.tr_bi_smart_money_back_gold_atpd_rider2);
        trCritiCareRider = findViewById(R.id.tr_bi_smart_money_back_gold_ccnl_rider);
        trCritiCareRider2 = findViewById(R.id.tr_bi_smart_money_back_gold_ccnl_rider2);
        trPreffredTermRider = findViewById(R.id.tr_bi_smart_money_back_gold_pta_rider);
        trPreffredTermRider2 = findViewById(R.id.tr_bi_smart_money_back_gold_pta_rider2);

        btnSubmit = findViewById(R.id.btnSubmit);
        btnBack = findViewById(R.id.back);

        /*tr_money_back_gold_proposer_detail1 = findViewById(R.id.tr_money_back_gold_proposer_detail1);
        tr_money_back_gold_proposer_detail12 = findViewById(R.id.tr_money_back_gold_proposer_detail12);
        tr_money_back_gold_proposer_detail13 = findViewById(R.id.tr_money_back_gold_proposer_detail13);
        tr_money_back_gold_proposer_detail2 = findViewById(R.id.tr_money_back_gold_proposer_detail2);
        tr_money_back_gold_proposer_detail22 = findViewById(R.id.tr_money_back_gold_proposer_detail22);*/

        btn_bi_smart_money_back_gold_life_assured_date = findViewById(R.id.btn_bi_smart_money_back_gold_life_assured_date);
        spnr_bi_smart_money_back_gold_life_assured_title = findViewById(R.id.spnr_bi_smart_money_back_gold_life_assured_title);
        edt_bi_smart_money_back_gold_life_assured_first_name = findViewById(R.id.edt_bi_smart_money_back_gold_life_assured_first_name);
        edt_bi_smart_money_back_gold_life_assured_middle_name = findViewById(R.id.edt_bi_smart_money_back_gold_life_assured_middle_name);
        edt_bi_smart_money_back_gold_life_assured_last_name = findViewById(R.id.edt_bi_smart_money_back_gold_life_assured_last_name);
        spnrPremPayingMode = findViewById(R.id.spnr_bi_smart_money_back_gold_premium_paying_mode);
        spnrPolicyTerm = findViewById(R.id.spnr_bi_smart_money_back_gold_policyterm);


        //spnrPlan = findViewById(R.id.spnr_bi_smart_money_back_gold_plan);
        cb_kerladisc = findViewById(R.id.cb_kerladisc);
        //edt_bi_money_back_gold_proposer_age = findViewById(R.id.edt_bi_money_back_gold_proposer_age);
        tr_premium_paying_term = findViewById(R.id.tr_premium_paying_term);

        spinnerLifeAssuredGender = findViewById(R.id.spinnerLifeAssuredGender);
//        spinnerLifeAssuredGender.setClickable(false);
//        spinnerLifeAssuredGender.setEnabled(false);

        ArrayAdapter<String> genderAdapter = new ArrayAdapter<>(
                getApplicationContext(), R.layout.spinner_item, getResources().getStringArray(R.array.gender_all_arrays));
        genderAdapter.setDropDownViewResource(R.layout.spinner_item1);
        spinnerLifeAssuredGender.setAdapter(genderAdapter);
        genderAdapter.notifyDataSetChanged();


        spnrAge = findViewById(R.id.spnr_bi_smart_money_back_gold_age);

        cb_staffdisc = findViewById(R.id.cb_staffdisc);
        cbADBRider = findViewById(R.id.cb_bi_smart_money_back_gold_adb_rider);
        cbATPDBRider = findViewById(R.id.cb_bi_smart_money_back_gold_atpdb_rider);
        cbCC13NonLinkedRider = findViewById(R.id.cb_bi_smart_money_back_gold_ccnl_rider);

        edt_proposerdetail_basicdetail_contact_no = findViewById(R.id.edt_smart_money_contact_no);
        edt_proposerdetail_basicdetail_Email_id = findViewById(R.id.edt_smart_money_Email_id);
        edt_proposerdetail_basicdetail_ConfirmEmail_id = findViewById(R.id.edt_smart_money_ConfirmEmail_id);

        tr_premium_paying_mode = findViewById(R.id.tr_premium_paying_mode);
        selPremFreq = findViewById(R.id.premiumfreq);
        spnrPremPaymentTerm = findViewById(R.id.spnr_bi_smart_money_back_gold_premium_payment_term);

        ll_is_defence = findViewById(R.id.ll_is_defence);
        spnr_is_defence_occupation = findViewById(R.id.spnr_is_defence_occupation);
        spnr_age_proof = findViewById(R.id.spnr_age_proof);
        spnr_occupation_type = findViewById(R.id.spnr_occupation_type);
        spnr_occupation_rank = findViewById(R.id.spnr_occupation_rank);
        spnr_occupation_location = findViewById(R.id.spnr_occupation_location);

        // Class Declaration

        ProposalMode = intent.getStringExtra("ProposalMode");
        STR_MAT_VALUE_PROD_CHANGE = intent.getStringExtra("MAT_VALUE_PROD_CHANGE");
        STR_MAT_VALUE_PROD_QUT_NO = intent.getStringExtra("MAT_VALUE_PROD_QUT_NO");

        if (STR_MAT_VALUE_PROD_CHANGE == null) {
            STR_MAT_VALUE_PROD_CHANGE = "";
        }
        if (STR_MAT_VALUE_PROD_QUT_NO == null) {
            STR_MAT_VALUE_PROD_QUT_NO = "";
        }
        if (ProposalMode == null) {
            ProposalMode = "";
        }
        // retVal = new StringBuilder();
        // bussIll = new StringBuilder();
        prsObj = new ParseXML();

        // Class Declaration
        CommonForAllProd commonForAllProd = new CommonForAllProd();
        SaralJeevanBimaProperties prop = new SaralJeevanBimaProperties();


        // Plan
        //String[] planList = {"Option 1", "Option 2", "Option 3", "Option 4"};
       /* String[] planList = {"Option 1", "Option 2", "Option 3"};
        ArrayAdapter<String> planAdapter = new ArrayAdapter<>(
                getApplicationContext(), R.layout.spinner_item, planList);
        planAdapter.setDropDownViewResource(R.layout.spinner_item1);
        spnrPlan.setAdapter(planAdapter);
        planAdapter.notifyDataSetChanged();*/


        minAge = 18;
        maxAge = 65;

        // Age
        String[] ageList = new String[49];
        for (int i = 17; i <= 65; i++) {
            ageList[i - 17] = i + "";
        }
        ArrayAdapter<String> ageAdapter = new ArrayAdapter<>(
                getApplicationContext(), R.layout.spinner_item, ageList);
        ageAdapter.setDropDownViewResource(R.layout.spinner_item1);
        spnrAge.setAdapter(ageAdapter);
        spnrAge.setEnabled(false);
        ageAdapter.notifyDataSetChanged();


        String[] premiumFrequencyList = {"Select", "Single", "Regular", "LPPT"};
        ArrayAdapter<String> premiumFrequencyAdapter = new ArrayAdapter<>(
                getApplicationContext(), R.layout.spinner_item,
                premiumFrequencyList);
        premiumFrequencyAdapter.setDropDownViewResource(R.layout.spinner_item1);
        selPremFreq.setAdapter(premiumFrequencyAdapter);
        premiumFrequencyAdapter.notifyDataSetChanged();


        // Premium Frequency
        String[] premFreqList = {"Yearly", "Half Yearly", "Monthly"};
        ArrayAdapter<String> premFreqAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item, premFreqList);
        premFreqAdapter.setDropDownViewResource(R.layout.spinner_item1);
        spnrPremPayingMode.setAdapter(premFreqAdapter);
        premFreqAdapter.notifyDataSetChanged();

        // Gender
//		String[] genderList = { "Male", "Female" };
//		ArrayAdapter<String> genderAdapter = new ArrayAdapter<String>(
//				getApplicationContext(), R.layout.spinner_item, genderList);
//		genderAdapter.setDropDownViewResource(R.layout.spinner_item1);
//		spnrGender.setAdapter(genderAdapter);
//		spnrGender.setEnabled(false);
//		genderAdapter.notifyDataSetChanged();

        //String[] is_defence_list = {"Select", "Defence"};
        String[] is_defence_list = {"Select", "Defence", "Others"};
        ArrayAdapter<String> isDefenceAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item, is_defence_list);
        isDefenceAdapter.setDropDownViewResource(R.layout.spinner_item1);
        spnr_is_defence_occupation.setAdapter(isDefenceAdapter);
        isDefenceAdapter.notifyDataSetChanged();

        String[] age_proof_list = {"Select", "Aadhar card with complete DOB", "Aadhar card with incomplete DOB", "Birth Certificate", "School/College Certificate",
                "Passport", "Service Extract (PSU)", "Identity Card (Defence)", "Driving License", "Pancard", "Voter s Identity Card"};
        ArrayAdapter<String> ageProofAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item, age_proof_list);
        ageProofAdapter.setDropDownViewResource(R.layout.spinner_item1);
        spnr_age_proof.setAdapter(ageProofAdapter);
        ageProofAdapter.notifyDataSetChanged();

        String[] occ_type_list = {"Select", "Army", "Air Force", "Navy", "RPF, CISF, Indian Home Guard, Defence Security Guards",
                "BSF, CRPF, RAF, SSB, DSC, SFF, SAP", "Coastal Guard"};
        ArrayAdapter<String> occTypeAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item, occ_type_list);
        occTypeAdapter.setDropDownViewResource(R.layout.spinner_item1);
        spnr_occupation_type.setAdapter(occTypeAdapter);
        occTypeAdapter.notifyDataSetChanged();

        String[] occ_rank_list = {"Select", "Sub Lieutenant/ Lieutenant/Flight Lieutenant/ Lt. Col/ Lt. Gen", "Captain/Group Captain",
                "Major/ Major Gen", "Brigadier", "Lieutenant Commander/Commander/ Wing Commander", "Rear Admiral/Vice Admiral, Admiral",
                "Pilot officer/Flying officer", "Suquadron Leader", "Air Commodore/captain Commodore", "Air Vice Marshal/Air Marshal/Air Chief Marshal",
                "Sepoy/Hawaldar", "Lance Naik/Naik", "Subhedar/Naib Subhedar/Subhedar Major", "Master Chief Petty officer/Chief Petty officer/petty officer",
                "Seaman", "Aircraftsman", "Coporal", "Sergeant", "Warrant officer", "Others"};
        ArrayAdapter<String> occRankAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item, occ_rank_list);
        occRankAdapter.setDropDownViewResource(R.layout.spinner_item1);
        spnr_occupation_rank.setAdapter(occRankAdapter);
        occRankAdapter.notifyDataSetChanged();

        String[] occ_location = {"Select", "Border areas of J & K or North East", "Maoist prone areas of Chattisgarh or Jharkhand", "Others"};
        ArrayAdapter<String> occLocationAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item, occ_location);
        occLocationAdapter.setDropDownViewResource(R.layout.spinner_item1);
        spnr_occupation_location.setAdapter(occLocationAdapter);
        occLocationAdapter.notifyDataSetChanged();

        List<String> title_list = new ArrayList<>();

        title_list.add("Select Title");
        title_list.add("Mr.");
        title_list.add("Ms.");
        title_list.add("Mrs.");

        commonMethods.fillSpinnerValue(context, spnr_bi_smart_money_back_gold_life_assured_title,
                title_list);


        // selPTATerm_txt1 = (TextView) findViewById(R.id.selPTATerm_txt1);
        // selPTATerm_txt2 = (TextView) findViewById(R.id.selPTATerm_txt2);

        // policy Term
        String[] policyTermList = new String[36];
        for (int i = 0; i <= 35; i++) {
            policyTermList[i] = (i + 5) + "";
        }
        ArrayAdapter<String> policyTermAdapter = new ArrayAdapter<>(
                getApplicationContext(), R.layout.spinner_item, policyTermList);
        policyTermAdapter.setDropDownViewResource(R.layout.spinner_item1);
        spnrPolicyTerm.setAdapter(policyTermAdapter);
        //spnrPolicyTerm.setEnabled(false);
        policyTermAdapter.notifyDataSetChanged();


        String[] spnrPremPaymentTermList = new String[36];
        for (int i = 0; i <= 35; i++) {
            spnrPremPaymentTermList[i] = (i + 5) + "";
        }
        ArrayAdapter<String> adapterPPTList = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item, spnrPremPaymentTermList);
        adapterPPTList.setDropDownViewResource(R.layout.spinner_item1);
        spnrPremPaymentTerm.setAdapter(adapterPPTList);
        // spnrPremPaymentTerm.setEnabled(false);
        adapterPPTList.notifyDataSetChanged();

        // adb_txt1 = (TextView) findViewById(R.id.adb_txt1);
        // adb_txt2 = (TextView) findViewById(R.id.adb_txt2);


        currencyFormat = new DecimalFormat("##,##,##,###");

        // For Staff Discount

        //Added by Pranprit Gill on 18/05/2018


        edt_bi_smart_money_back_gold_life_assured_first_name
                .setOnEditorActionListener(this);
        edt_bi_smart_money_back_gold_life_assured_middle_name
                .setOnEditorActionListener(this);
        edt_bi_smart_money_back_gold_life_assured_last_name
                .setOnEditorActionListener(this);
        edt_SumAssured.setOnEditorActionListener(this);


        setSpinnerAndOtherListner();

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
                        email_id_validation(ProposerEmailId);

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
                        confirming_email_id(proposer_confirm_emailId);

                    }
                });

        edt_proposerdetail_basicdetail_contact_no
                .setOnEditorActionListener(this);
        edt_proposerdetail_basicdetail_Email_id.setOnEditorActionListener(this);
        edt_proposerdetail_basicdetail_ConfirmEmail_id
                .setOnEditorActionListener(this);

        date_comparison();


    }


    private void setSpinnerAndOtherListner() {


        spnr_is_defence_occupation.setOnItemSelectedListener
                (
                        new OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                if (position == 0) {
                                    str_is_defence_occupation = "";
                                    ll_is_defence.setVisibility(View.GONE);
                                    str_occupation_type = "";
                                    str_occupation_rank = "";
                                    str_occupation_location = "";
                                    spnr_occupation_type.setSelection(getIndex(spnr_occupation_type, "Select"), false);
                                    spnr_occupation_rank.setSelection(getIndex(spnr_occupation_rank, "Select"), false);
                                    spnr_occupation_location.setSelection(getIndex(spnr_occupation_location, "Select"), false);
                                } else if (position == 1) {
                                    str_is_defence_occupation = spnr_is_defence_occupation.getSelectedItem().toString();
                                    ll_is_defence.setVisibility(View.VISIBLE);
                                } else if (position == 2) {
                                    str_is_defence_occupation = spnr_is_defence_occupation.getSelectedItem().toString();
                                    ll_is_defence.setVisibility(View.GONE);
                                    str_occupation_type = "";
                                    str_occupation_rank = "";
                                    str_occupation_location = "";
                                    spnr_occupation_type.setSelection(getIndex(spnr_occupation_type, "Select"), false);
                                    spnr_occupation_rank.setSelection(getIndex(spnr_occupation_rank, "Select"), false);
                                    spnr_occupation_location.setSelection(getIndex(spnr_occupation_location, "Select"), false);
                                }
                                commonMethods.clearFocusable(spnr_is_defence_occupation);
                                commonMethods.setFocusable(spnr_occupation_type);
                                spnr_occupation_type.requestFocus();

                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        }
                );

        spnr_age_proof.setOnItemSelectedListener
                (
                        new OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                if (position == 0) {
                                    str_age_proof = "";
                                } else if (position > 0) {
                                    str_age_proof = spnr_age_proof.getSelectedItem().toString();
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        }
                );


        spnr_occupation_type.setOnItemSelectedListener
                (
                        new OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                if (position == 0) {
                                    str_occupation_type = "";
                                } else if (position > 0) {
                                    str_occupation_type = spnr_occupation_type.getSelectedItem().toString();
                                    updateDegsignationSpinner();
                                }
                                commonMethods.clearFocusable(spnr_occupation_type);
                                commonMethods.setFocusable(spnr_occupation_rank);
                                spnr_occupation_rank.requestFocus();
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        }
                );

        spnr_occupation_rank.setOnItemSelectedListener
                (
                        new OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                if (position == 0) {
                                    str_occupation_rank = "";
                                } else if (position > 0) {
                                    str_occupation_rank = spnr_occupation_rank.getSelectedItem().toString();
                                }
                                commonMethods.clearFocusable(spnr_occupation_rank);
                                commonMethods.setFocusable(spnr_occupation_location);
                                spnr_occupation_location.requestFocus();
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        }
                );

        spnr_occupation_location.setOnItemSelectedListener
                (
                        new OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                if (position == 0) {
                                    str_occupation_location = "";
                                } else if (position > 0) {
                                    str_occupation_location = spnr_occupation_location.getSelectedItem().toString();
                                }
                                commonMethods.clearFocusable(spnr_occupation_location);
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        }
                );

        cb_kerladisc.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cb_kerladisc.isChecked()) {
                    str_kerla_discount = "Yes";
                    commonMethods.clearFocusable(cb_kerladisc);
                    commonMethods.setFocusable(spnr_bi_smart_money_back_gold_life_assured_title);
                    spnr_bi_smart_money_back_gold_life_assured_title.requestFocus();
                } else {
                    str_kerla_discount = "No";
                    commonMethods.clearFocusable(cb_kerladisc);
                    commonMethods.setFocusable(spnr_bi_smart_money_back_gold_life_assured_title);
                    spnr_bi_smart_money_back_gold_life_assured_title.requestFocus();
                }
            }
        });

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
                            commonMethods.clearFocusable(rb_proposerdetail_personaldetail_backdating_yes);
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

                            spnrAge.setSelection(
                                    getIndex(spnrAge, lifeAssuredAge), false);
                            valAge();
                            rb_proposerdetail_personaldetail_backdating_yes
                                    .setFocusable(false);

                            commonMethods.clearFocusable(rb_proposerdetail_personaldetail_backdating_no);
                            commonMethods.clearFocusable(rb_proposerdetail_personaldetail_backdating_yes);
                            commonMethods.setFocusable(cbPTARider);
                            cbPTARider.requestFocus();

                        }
                    }
                });

        // PTA Rider
        cbPTARider
                .setOnCheckedChangeListener(new OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView,
                                                 boolean isChecked) {

                        if (isChecked == true) {
                            if (valEligibilityOfRider()) {
                                trPreffredTermRider.setVisibility(View.VISIBLE);
                                trPreffredTermRider2.setVisibility(View.VISIBLE);
                                commonMethods.clearFocusable(cbPTARider);
                                commonMethods.clearFocusable(spnr_PtaTerm);
                                commonMethods.setFocusable(spnr_PtaTerm);
                                spnr_PtaTerm.requestFocus();

                            }
                        } else {
                            commonMethods.clearFocusable(cbPTARider);
                            trPreffredTermRider.setVisibility(View.GONE);
                            trPreffredTermRider2.setVisibility(View.GONE);
                            commonMethods.setFocusable(cbADBRider);
                            cbADBRider.requestFocus();

                        }
                    }
                });

        // ADB Rider

        cbADBRider
                .setOnCheckedChangeListener(new OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton buttonView,
                                                 boolean isChecked) {
                        if (isChecked == true) {
                            if (valEligibilityOfRider()) {

                                commonMethods.clearFocusable(cbADBRider);
                                trADBRider.setVisibility(View.VISIBLE);
                                trADBRider2.setVisibility(View.VISIBLE);
                                commonMethods.clearFocusable(spnr_AdbTerm);
                                commonMethods.setFocusable(spnr_AdbTerm);
                                spnr_AdbTerm.requestFocus();
                            }
                        } else {
                            commonMethods.clearFocusable(cbADBRider);
                            trADBRider.setVisibility(View.GONE);
                            trADBRider2.setVisibility(View.GONE);
                            commonMethods.setFocusable(cbATPDBRider);
                            cbATPDBRider.requestFocus();

                        }
                    }
                });

        // ATPDB Rider

        cbATPDBRider
                .setOnCheckedChangeListener(new OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView,
                                                 boolean isChecked) {
                        if (isChecked == true) {
                            if (valEligibilityOfRider()) {

                                commonMethods.clearFocusable(cbATPDBRider);
                                trATPDBRider.setVisibility(View.VISIBLE);
                                trATPDBRider2.setVisibility(View.VISIBLE);
                                commonMethods.clearFocusable(spnr_AtpdbTerm);
                                commonMethods.setFocusable(spnr_AtpdbTerm);
                                spnr_AtpdbTerm.requestFocus();
                            }
                        } else {
                            commonMethods.clearFocusable(cbATPDBRider);
                            trATPDBRider.setVisibility(View.GONE);
                            trATPDBRider2.setVisibility(View.GONE);
                            commonMethods.setFocusable(cbCC13NonLinkedRider);
                            cbCC13NonLinkedRider.requestFocus();
                        }
                    }
                });

        // CC13 Non linked Rider

        cbCC13NonLinkedRider
                .setOnCheckedChangeListener(new OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton buttonView,
                                                 boolean isChecked) {
                        if (isChecked) {
                            if (valEligibilityOfRider()) {
                                commonMethods.clearFocusable(cbCC13NonLinkedRider);
                                trCritiCareRider.setVisibility(View.VISIBLE);
                                trCritiCareRider2.setVisibility(View.VISIBLE);
                                commonMethods.clearFocusable(spnr_Cc13NonLinkedTerm);
                                commonMethods.setFocusable(spnr_Cc13NonLinkedTerm);
                                spnr_Cc13NonLinkedTerm.requestFocus();
                            }
                        } else {
                            commonMethods.clearFocusable(cbCC13NonLinkedRider);
                            trCritiCareRider.setVisibility(View.GONE);
                            trCritiCareRider2.setVisibility(View.GONE);
                        }
                    }
                });

        // Staff Discount
        cb_staffdisc.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((cb_staffdisc.isChecked())) {
                    cb_staffdisc.setChecked(true);

                    commonMethods.clearFocusable(spnr_bi_smart_money_back_gold_life_assured_title);
                    commonMethods.setFocusable(spnr_bi_smart_money_back_gold_life_assured_title);
                    spnr_bi_smart_money_back_gold_life_assured_title
                            .requestFocus();
                }

            }
        });


        // Plan
        /*spnrPlan.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int pos,
                                       long id) {

                if (pos == 0) {
                    //spnrPolicyTerm.setSelection(getIndex(spnrPolicyTerm, "15"), false);


                    if (selPremFreq.getSelectedItem().toString().equals("Single")) {
                        //  spnrPremPaymentTerm.setSelection(getIndex(spnrPremPaymentTerm, "1"));
                    } else if (selPremFreq.getSelectedItem().toString().equals("LPPT")) {
                        //spnrPremPaymentTerm.setSelection(getIndex(spnrPremPaymentTerm, "8"));
                    } else if (selPremFreq.getSelectedItem().toString().equals("Regular")) {
                        //spnrPremPaymentTerm.setSelection(getIndex(spnrPremPaymentTerm, "15"));
                    }

                } else if (pos == 1) {
                    //  spnrPolicyTerm.setSelection(getIndex(spnrPolicyTerm, "20"),false);

                    if (selPremFreq.getSelectedItem().toString().equals("Single")) {
                        //   spnrPremPaymentTerm.setSelection(getIndex(spnrPremPaymentTerm, "1"));
                    } else if (selPremFreq.getSelectedItem().toString().equals("LPPT")) {
                        //   spnrPremPaymentTerm.setSelection(getIndex(spnrPremPaymentTerm, "10"));
                    } else if (selPremFreq.getSelectedItem().toString().equals("Regular")) {
                        //  spnrPremPaymentTerm.setSelection(getIndex(spnrPremPaymentTerm, "20"));
                    }

                } else if (pos == 2) {
                    //  spnrPolicyTerm.setSelection(getIndex(spnrPolicyTerm, "25"),false);

                    if (selPremFreq.getSelectedItem().toString().equals("Single")) {
                        //      spnrPremPaymentTerm.setSelection(getIndex(spnrPremPaymentTerm, "1"));
                    } else if (selPremFreq.getSelectedItem().toString().equals("LPPT")) {
                        //    spnrPremPaymentTerm.setSelection(getIndex(spnrPremPaymentTerm, "12"));
                    } else if (selPremFreq.getSelectedItem().toString().equals("Regular")) {
                        //  spnrPremPaymentTerm.setSelection(getIndex(spnrPremPaymentTerm, "25"));
                    }

                }
//                commonMethods.clearFocusable(spnrPlan);
                // validate rider term after basic term is set
                *//*if (valTermRider())
                {
                    commonMethods.setFocusable(spnrPremPayingMode);
                    spnrPremPayingMode.requestFocus();
                }*//*
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });*/

        spnrPremPaymentTerm.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        selPremFreq.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                premFreqOptions = selPremFreq.getSelectedItem().toString();

                /*if (premFreqOptions.equalsIgnoreCase("Regular")) {

                    if (Integer
                            .parseInt(spnrAge.getSelectedItem().toString()) > 45) {
                        lifeAssured_date_of_birth = "";
                    }


                }
*/

                if (position == 0) {
                    premFreqOptions = "";
                    tr_premium_paying_mode.setVisibility(View.GONE);
                    tr_premium_paying_term.setVisibility(View.GONE);
                }
                if (position == 1) {
                    tr_premium_paying_mode.setVisibility(View.GONE);
                    String[] spnrPremPaymentTermList = new String[]{"1"};
                    ArrayAdapter<String> adapterPPTList = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item, spnrPremPaymentTermList);
                    adapterPPTList.setDropDownViewResource(R.layout.spinner_item1);
                    spnrPremPaymentTerm.setAdapter(adapterPPTList);
                    spnrPremPaymentTerm.setSelection(getIndex(spnrPremPaymentTerm, "1"));
                    tr_premium_paying_term.setVisibility(View.GONE);
                } else if (position == 2) {
                    tr_premium_paying_mode.setVisibility(View.VISIBLE);
                    String[] spnrPremPaymentTermList = new String[36];
                    for (int i = 0; i <= 35; i++) {
                        spnrPremPaymentTermList[i] = (i + 5) + "";
                    }
                    ArrayAdapter<String> adapterPPTList = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item, spnrPremPaymentTermList);
                    adapterPPTList.setDropDownViewResource(R.layout.spinner_item1);
                    spnrPremPaymentTerm.setAdapter(adapterPPTList);
                    adapterPPTList.notifyDataSetChanged();
                    tr_premium_paying_term.setVisibility(View.GONE);
                } else if (position == 3) {
                    tr_premium_paying_mode.setVisibility(View.VISIBLE);
                    String[] spnrPremPaymentTermList = new String[]{"5", "10"};
                    ArrayAdapter<String> adapterPPTList = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item, spnrPremPaymentTermList);
                    adapterPPTList.setDropDownViewResource(R.layout.spinner_item1);
                    spnrPremPaymentTerm.setAdapter(adapterPPTList);
                    adapterPPTList.notifyDataSetChanged();
                    tr_premium_paying_term.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spnrPremPayingMode
                .setOnItemSelectedListener(new OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int pos, long id) {

                        commonMethods.clearFocusable(spnrPremPayingMode);
                        if (flagFirstFocus) {
                            commonMethods.setFocusable(edt_bi_smart_money_back_gold_life_assured_first_name);
                            edt_bi_smart_money_back_gold_life_assured_first_name
                                    .requestFocus();
                            flagFirstFocus = false;

                        } else {

                            commonMethods.setFocusable(edt_SumAssured);
                            edt_SumAssured.requestFocus();
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                    }
                });
       /* spnr_bi_money_back_gold_proposer_title
                .setOnItemSelectedListener(new OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int position, long id) {

                        if (position > 0) {
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

                        }

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {


                    }
                });*/
        spnr_bi_smart_money_back_gold_life_assured_title
                .setOnItemSelectedListener(new OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int position, long id) {

                        if (position > 0) {
                            lifeAssured_Title = spnr_bi_smart_money_back_gold_life_assured_title
                                    .getSelectedItem().toString();

                            commonMethods.clearFocusable(spnr_bi_smart_money_back_gold_life_assured_title);
                            commonMethods.setFocusable(edt_bi_smart_money_back_gold_life_assured_first_name);

                            edt_bi_smart_money_back_gold_life_assured_first_name
                                    .requestFocus();
                        }

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {


                    }
                });

        // Back Button
        btnBack.setOnClickListener(new OnClickListener() {
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

                gender = spinnerLifeAssuredGender.getSelectedItem().toString();
                // calculate effective premium
                if (valLifeAssuredProposerDetail() && valDob() && valPremiumpaymentOption() && valAge() && valPremiumPayingTerm()
                        && valBasicDetail() && valSA()) {// && valBackdate() && TrueBackdate()) {
                    Date();
                    addListenerOnSubmit();
                    getInput(saralJeevanBimaBean);

                    if (needAnalysis_flag == 0) {
                        Intent i = new Intent(BI_SaralJeevanBimaActivity.this,
                                Success.class);
                        i.putExtra("op", "Premium Before Applicable Taxes : Rs. " + currencyFormat.format(Double.parseDouble(BasicPremium)));
                        i.putExtra("op1", "Applicable Taxes 1st Year : Rs. " + currencyFormat.format(Double.parseDouble(serviceTax)));
                        i.putExtra("op3", "Premium with Applicable Taxes 1st year : Rs. " + currencyFormat.format(Double.parseDouble(PremiumwithST)));
                       /* i.putExtra("op5", "Matuarity Benifit Guaranteed : Rs. " + currencyFormat.format(Double.parseDouble(GuaranteedMaturityBen)));
                        i.putExtra("op6", "Total Matuarity Benifit (Guaranteed+Non-Guaranteed) 4% : Rs. " + currencyFormat.format(Double.parseDouble(totalmaturity4per)));
                        i.putExtra("op7", "Total Matuarity Benifit (Guaranteed+Non-Guaranteed) 8% : Rs. " + currencyFormat.format(Double.parseDouble(totalmaturity8per)));*/
                        i.putExtra("header", getString(R.string.sbi_life_saral_jeevan_bima));
                        i.putExtra("header1", "(UIN:" + getString(R.string.sbi_life_saral_jeevan_bima_uin) + ")");
                        startActivity(i);

                    } else
                        Dialog();
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
                        imageButtonProposerPhotograph
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
                new ColorDrawable(Color.BLACK));

        d.setContentView(R.layout.layout_saral_jeevan_bima_bi_grid);


        TextView tv_bi_smart_money_back_gold_proposer_age = d
                .findViewById(R.id.tv_bi_smart_money_back_gold_proposer_age);

        TextView tv_premium_tag = d
                .findViewById(R.id.tv_premium_tag);


        TextView tv_bi_saral_jeevan_bima_state = d
                .findViewById(R.id.tv_bi_saral_jeevan_bima_state);

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


        TextView tv_saral_jeevan_bima_term_basic_cover = d
                .findViewById(R.id.tv_saral_jeevan_bima_term_basic_cover);


        TextView tv_saral_jeevan_bima_sum_assured_basic_cover = d
                .findViewById(R.id.tv_saral_jeevan_bima_sum_assured_basic_cover);

        TextView tv_saral_jeevan_bima_premium_basic_cover = d
                .findViewById(R.id.tv_saral_jeevan_bima_premium_basic_cover);

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


        TextView tv_for_frequency_premium = d
                .findViewById(R.id.tv_for_frequency_premium);


        TextView tv_bi_saral_jeevan_bima_installment_premium = d
                .findViewById(R.id.tv_bi_saral_jeevan_bima_installment_premium);


        TextView tv_bi_saral_jeevan_bima_basic_service_tax_first_year = d
                .findViewById(R.id.tv_bi_saral_jeevan_bima_basic_service_tax_first_year);


        TextView tv_for_frequency_premium_for_service_Tax = d
                .findViewById(R.id.tv_for_frequency_premium_for_service_Tax);


        TextView tv_bi_saral_jeevan_bima_installment_premium_with_tax = d
                .findViewById(R.id.tv_bi_saral_jeevan_bima_installment_premium_with_tax);


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

        // Seconf year policy onwards

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

        final TextView tvCbStatementFirst = d.findViewById(R.id.tvCbStatementFirst);

        final EditText edt_Policyholderplace = d
                .findViewById(R.id.edt_Policyholderplace);


        final CheckBox cb_statement = d
                .findViewById(R.id.cb_statement);


        /* Need Analysis */
        final TextView txt_proposer_name_need_analysis = d
                .findViewById(R.id.txt_proposer_name_need_analysis);

        final CheckBox cb_statement_need_analysis = d
                .findViewById(R.id.cb_statement_need_analysis);
        cb_statement_need_analysis.setChecked(true);
        TableRow tr_need_analysis = d
                .findViewById(R.id.tr_need_analysis);


        final CheckBox checkboxAgentStatement = d
                .findViewById(R.id.checkboxAgentStatement);
        checkboxAgentStatement.setChecked(false);

        if (cb_kerladisc.isChecked()) {
            tv_bi_saral_jeevan_bima_state.setText("Kerala");
        } else {
            tv_bi_saral_jeevan_bima_state.setText("Non-Kerala");
        }


        Button btn_proceed = d
                .findViewById(R.id.btn_proceed);

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

        if (!proposer_sign.equals("")) {
            String flg_needAnalyis = "";
            if (flg_needAnalyis.equals("1")) {
                tr_need_analysis.setVisibility(View.VISIBLE);
            } else {
                cb_statement_need_analysis.setChecked(true);
                tr_need_analysis.setVisibility(View.GONE);
            }
        }

        String input = inputVal.toString();
        String output = retVal.toString();
        if (proposer_Is_Same_As_Life_Assured.equalsIgnoreCase("y")) {
            //having received the information with respect to the above,
            // have understood the above statement before entering into the contract.
            tvCbStatementFirst
                    .setText("I, "
                            + name_of_life_assured
                            + ",having received the information with respect to the above, have understood the above statement before entering into a contract.");

            txt_proposer_name_need_analysis
                    .setText("I, "
                            + name_of_life_assured
                            + ", have undergone the Need Analysis and after having reviewed the SBI Life Product options, I have opted for 'SBI LIFE - " +
                            getString(R.string.sbi_life_saral_jeevan_bima));

            tv_proposername.setText(name_of_life_assured);
            tv_life_assured_name.setText(name_of_life_assured);
            tv_proposername.setText(name_of_life_assured);
        } else if (proposer_Is_Same_As_Life_Assured.equalsIgnoreCase("n")) {
            tvCbStatementFirst
                    .setText("I, "
                            + name_of_proposer
                            + ",having received the information with respect to the above, have understood the above statement before entering into a contract.");

            txt_proposer_name_need_analysis
                    .setText("I, "
                            + name_of_proposer
                            + ", have undergone the Need Analysis and after having reviewed the SBI Life Product options, I have opted for 'SBI LIFE - " +
                            getString(R.string.sbi_life_saral_jeevan_bima));

            tv_life_assured_name.setText(name_of_life_assured);
            tv_proposername.setText(name_of_proposer);
        }

        tv_proposal_number.setText(QuatationNumber);
        TextView textviewAGentStatement = d.findViewById(R.id.textviewAGentStatement);
        textviewAGentStatement.setText("I, " + commonMethods.getUserName(context) + " have explained the premiums and benefits under the product fully to the prospect/policyholder.");


        if (place2.equals("")) {
            edt_Policyholderplace.setText(place2);
        } else {
            edt_Policyholderplace.setText(place2);
        }

        if (!date2.equals("")) {
            btn_PolicyholderDate.setText(commonMethods.getDDMMYYYYDate(date2));
        } else {
            date2 = commonMethods.getMMDDYYYYDatabaseDate(getCurrentDate());
            btn_PolicyholderDate.setText(getCurrentDate());
        }

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
                    @Override
                    public void onClick(View view) {
                        if (cb_statement.isChecked()
                                && cb_statement_need_analysis.isChecked()
                                && checkboxAgentStatement.isChecked()) {
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
            imageButtonProposerPhotograph
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
                            imageButtonProposerPhotograph
                                    .setImageDrawable(getResources()
                                            .getDrawable(
                                                    R.drawable.focus_imagebutton_photo));
                        }
                    }
                });

        radioButtonTrasactionModeManual
                .setOnCheckedChangeListener(new OnCheckedChangeListener() {

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
        btn_proceed.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                place2 = edt_Policyholderplace.getText().toString();
                date1 = btn_MarketingOfficalDate.getText().toString();
                date2 = btn_PolicyholderDate.getText().toString();

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
                    String productCode = getString(R.string.sbi_life_smart_future_choices_code);


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
                            obj.getRound(obj.getStringWithout_E(Double
                                    .valueOf(BasicPremium))),
                            emailId, mobileNo, agentEmail, agentMobile,
                            na_input, na_output, saralJeevanBimaBean.getPremiumFreq(), Integer
                            .parseInt(policyTerm), Integer.parseInt(saralJeevanBimaBean.getPremPaymentTerm()), productCode,
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
                    db.AddNeedAnalysisDashboardDetails(new ProductBIBean(
                            "",
                            QuatationNumber,
                            planName,
                            getCurrentDate(),
                            mobileNo,
                            getCurrentDate(),
                            db.GetUserCode(),
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
                            obj.getRound(obj.getStringWithout_E(Double
                                    .valueOf(BasicPremium))),
                            agentEmail, agentMobile, na_input, na_output,
                            saralJeevanBimaBean.getPremiumFreq(), Integer.parseInt(policyTerm),
                            Integer.parseInt(saralJeevanBimaBean.getPremPaymentTerm()),
                            productCode, commonMethods.getDDMMYYYYDate(lifeAssured_date_of_birth),
                            commonMethods.getDDMMYYYYDate(proposer_date_of_birth), "", transactionMode, inputVal
                            .toString(), retVal.toString().replace(
                            bussIll.toString(), "")));
                    /* end */

                    createPdf();

                    NABIObj.serviceHit(BI_SaralJeevanBimaActivity.this,
                            na_cbi_bean, newFile, needAnalysispath.getPath(),
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
                    } else if (!checkboxAgentStatement.isChecked()) {
                        commonMethods.dialogWarning(context, "Please Tick on I Agree Clause ", true);
                        commonMethods.setFocusable(checkboxAgentStatement);
                        checkboxAgentStatement.requestFocus();
                    } else if (!radioButtonTrasactionModeParivartan.isChecked()
                            && !radioButtonTrasactionModeManual.isChecked()) {
                        commonMethods.dialogWarning(context, "Please Select Transaction Mode", true);
                        commonMethods.setFocusable(linearlayoutTrasactionModeParivartan);
                        linearlayoutTrasactionModeParivartan.requestFocus();
                    } else if (photoBitmap == null) {
                        commonMethods.dialogWarning(context, "Please Capture the Photo", true);
                        commonMethods.setFocusable(imageButtonProposerPhotograph);
                        imageButtonProposerPhotograph
                                .requestFocus();
                    }
                }


            }

        });

        btn_PolicyholderDate.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {


                DIALOG_ID = 2;
                showDateDialog();
            }
        });

        btn_MarketingOfficalDate.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                DIALOG_ID = 3;
                showDateDialog();

            }
        });

        /*tv_uin_smart_money_back_gold
                .setText("Benefit Illustration for SBI LIFE - Smart Money Back Gold (UIN No -"
                        + "111N096V03" + ")");*/

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

        String premPaymentTerm = prsObj.parseXmlTag(input, "PremPaymentTerm");
        tv_bi_smart_money_back_gold_premium_payment_term.setText(premPaymentTerm);

        premPayingMode = prsObj.parseXmlTag(input, "premFreq");
        tv_premPaymentfrequency.setText(premPayingMode);

        if (premFreqOptions.equalsIgnoreCase("Regular") || premFreqOptions.equalsIgnoreCase("LPPT")) {

            if (premPayingMode.equalsIgnoreCase("Half Yearly")) {
               /* tv_bi_smart_money_back_gold_premium_tag_basic_cover
                        .setText("Premium Half Yearly");*/
                tv_bi_smart_money_back_gold_year_tag_total_premium
                        .setText("Half Yearly");
                tv_bi_smart_money_back_gold_pemium_with_service_tag_total_premium
                        .setText("Half Yearly Installment Premium with Applicable Taxes");
                tv_bi_smart_money_back_gold_pemium_tag_total_premium
                        .setText("Half Yearly Installment Premium");

                tv_for_frequency_premium_for_service_Tax.setText("Half Yearly Installment Premium with Applicable Taxes");
                tv_for_frequency_premium.setText("Half Yearly Installment Premium");
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

                tv_for_frequency_premium_for_service_Tax.setText("Quarterly Installment Premium with Applicable Taxes");
                tv_bi_smart_money_back_gold_pemium_tag_total_premium
                        .setText("Quarterly Installment Premium");

                tv_for_frequency_premium.setText("Quarterly Installment Premium");
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

                tv_for_frequency_premium_for_service_Tax.setText("Monthly Installment Premium with Applicable Taxes");
                tv_bi_smart_money_back_gold_pemium_tag_total_premium
                        .setText("Monthly Installment Premium");
                tv_for_frequency_premium.setText("Monthly Installment Premium");
                tv_bi_smart_money_back_gold_year_tag_rider.setText("Monthly" + "(Rs)");
                // tv_premium_install_rider_type1.setText("Monthly Premium (Rs.)");
              /*  tv_mandatory_bi_smart_money_back_gold_yearly_premium_with_tax1
                        .setText("Monthly Premium with Applicable Taxes(Rs.)");*/
            } else if (premPayingMode.equalsIgnoreCase("Yearly")) {
               /* tv_bi_smart_money_back_gold_premium_tag_basic_cover
                        .setText("Premium Monthly");*/
                tv_bi_smart_money_back_gold_year_tag_total_premium
                        .setText("Yearly");
                tv_bi_smart_money_back_gold_pemium_with_service_tag_total_premium
                        .setText("Yearly Installment Premium with Applicable Taxes");

                tv_for_frequency_premium_for_service_Tax.setText("Yearly Installment Premium with Applicable Taxes");
                tv_bi_smart_money_back_gold_pemium_tag_total_premium
                        .setText("Yearly Installment Premium");
                tv_for_frequency_premium.setText("Yearly Installment Premium");
                tv_bi_smart_money_back_gold_year_tag_rider.setText("Yearly" + "(Rs)");
                // tv_premium_install_rider_type1.setText("Monthly Premium (Rs.)");
              /*  tv_mandatory_bi_smart_money_back_gold_yearly_premium_with_tax1
                        .setText("Monthly Premium with Applicable Taxes(Rs.)");*/
            }
        } else {
           /* tv_bi_smart_money_back_gold_premium_tag_basic_cover
                    .setText("Premium Single");*/


            tv_for_frequency_premium.setText("Single Installment Premium");
            tv_bi_smart_money_back_gold_year_tag_total_premium
                    .setText("Single");
            tv_bi_smart_money_back_gold_pemium_with_service_tag_total_premium
                    .setText("Single Premium with Applicable Taxes");
            tv_for_frequency_premium_for_service_Tax.setText("Single Installment Premium with Applicable Taxes");
            tv_bi_smart_money_back_gold_pemium_tag_total_premium
                    .setText("Single Premium");
            tv_bi_smart_money_back_gold_year_tag_rider.setText("Single" + "(Rs)");
            // tv_premium_install_rider_type1.setText("Single Premium (Rs.)");
            /*tv_mandatory_bi_smart_money_back_gold_yearly_premium_with_tax1
                    .setText("Single Premium with Applicable Taxes(Rs.)");*/
        }

        String isJkResident = prsObj.parseXmlTag(input, "isJKResident");


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


        policyTerm = prsObj.parseXmlTag(input, "policyTerm");
        tv_basic_cover_term.setText(policyTerm + " Years");

        tv_saral_jeevan_bima_term_basic_cover.setText(policyTerm + " Years");
        tv_basic_cover_premPayment_term.setText(premPaymentTerm);


        sumAssured = prsObj.parseXmlTag(input, "sumAssured");


        tv_saral_jeevan_bima_sum_assured_basic_cover.setText(getformatedThousandString(Integer.parseInt(obj
                .getRound(obj.getStringWithout_E(Double
                        .valueOf(sumAssured))))));


        ///////////////// // sral jeevan bima values


        BasicPremium = ((int) Double.parseDouble(prsObj.parseXmlTag(
                output, "BasicPremium"))) + "";
        BasicPremium_PDF = prsObj.parseXmlTag(
                output, "basicPrem");


        tv_basic_cover_yearly_premium.setText("Rs. " + getformatedThousandString(Integer.parseInt(obj
                .getRound(obj.getStringWithout_E(Double
                        .valueOf(BasicPremium))))));

        tv_bi_saral_jeevan_bima_installment_premium.setText("Rs. " + getformatedThousandString(Integer.parseInt(obj
                .getRound(obj.getStringWithout_E(Double
                        .valueOf(BasicPremium))))));
      /*  tv_saral_jeevan_bima_premium_basic_cover.setText(getformatedThousandString(Integer.parseInt(obj
                .getRound(obj.getStringWithout_E(Double
                        .valueOf(BasicPremium))))));*/
        tv_saral_jeevan_bima_premium_basic_cover.setText(BasicPremium_PDF);
        ApplicableTax = ((int) Double.parseDouble(prsObj.parseXmlTag(
                output, "ApplicableTax"))) + "";
        applicable_tax_rate.setText("Rs. " + getformatedThousandString(Integer.parseInt(obj
                .getRound(obj.getStringWithout_E(Double
                        .valueOf(ApplicableTax))))));


        tv_bi_saral_jeevan_bima_basic_service_tax_first_year.setText("Rs. " + getformatedThousandString(Integer.parseInt(obj
                .getRound(obj.getStringWithout_E(Double
                        .valueOf(ApplicableTax))))));

        PremiumwithST = ((int) Double.parseDouble(prsObj.parseXmlTag(
                output, "PremiumwithST"))) + "";
        tv_bi_smart_money_back_gold_sa_on_death.setText("Rs. " + getformatedThousandString(Integer.parseInt(obj
                .getRound(obj.getStringWithout_E(Double
                        .valueOf(PremiumwithST))))));

        tv_bi_saral_jeevan_bima_installment_premium_with_tax.setText("Rs. " + getformatedThousandString(Integer.parseInt(obj
                .getRound(obj.getStringWithout_E(Double
                        .valueOf(PremiumwithST))))));
        ///////////////// // sral jeevan bima values


        TextView tv_Company_policy_surrender_dec = d
                .findViewById(R.id.tv_Company_policy_surrender_dec);

        String str_prem_freq = "";
        if (premFreqOptions.equalsIgnoreCase("Single")) {
            str_prem_freq = "Single";
        } else if (premFreqOptions.equalsIgnoreCase("Regular")) {
            str_prem_freq = "Regular";
        } else {
            str_prem_freq = "Limited";
        }


        if (premFreqOptions.equalsIgnoreCase("Single")) {
            Company_policy_surrender_dec = "Your SBI Life - Saral Jeevan Bima (UIN - 111N128V01) is a "
                    + str_prem_freq
                    + " premium policy and you are required to pay Single Premium of Rs. "
                    + currencyFormat.format(Integer.parseInt(obj
                    .getRound(obj.getStringWithout_E(Double
                            .valueOf(PremiumwithST)))))
                    + " .Your Policy Term is "
                    + policyTerm
                    + " years"
                    + " ,Premium Payment Term is "
                    + "1"
                    + " years"
                    + " and Basic Sum Assured is Rs. "
                    +

                    getformatedThousandString(Integer.parseInt(obj
                            .getRound(obj.getStringWithout_E(Double
                                    .valueOf(sumAssured)))));
        } else {
            Company_policy_surrender_dec = "Your SBI Life - Saral Jeevan Bima (UIN - 111N128V01) is a "

                    + str_prem_freq
                    + " premium policy and you are required to pay " + premPayingMode + " Premium of Rs. "
                    + currencyFormat.format(Integer.parseInt(obj
                    .getRound(obj.getStringWithout_E(Double
                            .valueOf(PremiumwithST)))))
                    + " .Your Policy Term is "
                    + policyTerm
                    + " years"
                    + " ,Premium Payment Term is "
                    + premPaymentTerm
                    + " years"
                    + " and Basic Sum Assured is Rs. "
                    +

                    getformatedThousandString(Integer.parseInt(obj
                            .getRound(obj.getStringWithout_E(Double
                                    .valueOf(sumAssured)))));
        }
        tv_Company_policy_surrender_dec.setText(Company_policy_surrender_dec);

        d.show();

    }


    private void initialiseDate() {
        Calendar calender = Calendar.getInstance();
        mYear = calender.get(Calendar.YEAR);
        mMonth = calender.get(Calendar.MONTH);
        mDay = calender.get(Calendar.DAY_OF_MONTH);

    }

    public void Date() {
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // formattedDate have current date/time
        // Toast.makeText(this, formattedDate, Toast.LENGTH_SHORT).show();
        c.add(Calendar.DATE, 30);
    }

    protected void addListenerOnSubmit() {
        // Insert data entered by user in an object
        saralJeevanBimaBean = new SaralJeevanBimaBean();

        if (cb_staffdisc.isChecked() == true) {
            saralJeevanBimaBean.setStaffDisc(true);
        } else {

            saralJeevanBimaBean.setStaffDisc(false);

        }


        if (cb_kerladisc.isChecked()) {
            saralJeevanBimaBean.setKerlaDisc(true);
        } else {
            saralJeevanBimaBean.setKerlaDisc(false);
        }

        saralJeevanBimaBean.setJkResident(false);
       /* saralJeevanBimaBean.setPlanName(spnrPlan.getSelectedItem()
                .toString());*/
        saralJeevanBimaBean.setAge(Integer.parseInt(spnrAge
                .getSelectedItem().toString()));

        saralJeevanBimaBean.setStrProposerAge(strProposerAge);
        saralJeevanBimaBean.setGender(gender);
        saralJeevanBimaBean.setGender_proposer(gender_proposer);
        saralJeevanBimaBean.setPolicyTerm(Integer.parseInt(spnrPolicyTerm
                .getSelectedItem().toString()));

        saralJeevanBimaBean.setPremFreqOptions(selPremFreq.getSelectedItem().toString());

        if (selPremFreq.getSelectedItem().toString().equals("Single")) {
            saralJeevanBimaBean.setPremiumFreq("Single");
        } else {
            saralJeevanBimaBean.setPremiumFreq(spnrPremPayingMode.getSelectedItem().toString());
        }

        if (selPremFreq.getSelectedItem().toString().equalsIgnoreCase("Regular")) {
            saralJeevanBimaBean.setPremPaymentTerm(spnrPolicyTerm.getSelectedItem().toString());
        } else if (selPremFreq.getSelectedItem().toString().equalsIgnoreCase("Single")) {
            saralJeevanBimaBean.setPremPaymentTerm("1");
        } else {
            saralJeevanBimaBean.setPremPaymentTerm(spnrPremPaymentTerm.getSelectedItem().toString());
        }
        saralJeevanBimaBean.setSumAssured_Basic(Integer
                .parseInt(edt_SumAssured.getText().toString()));
        // PT Rider
        if (cbPTARider.isChecked() == true) {
            saralJeevanBimaBean.setPT_Status(true);
            saralJeevanBimaBean.setPolicyTerm_PT(Integer
                    .parseInt(spnr_PtaTerm.getSelectedItem().toString()));
            saralJeevanBimaBean.setSumAssured_PT(Integer.parseInt(edt_PtaSA
                    .getText().toString()));

        } else {
            saralJeevanBimaBean.setPT_Status(false);
        }
        // ADB Rider
        if (cbADBRider.isChecked() == true) {
            saralJeevanBimaBean.setADB_Status(true);
            saralJeevanBimaBean.setPolicyTerm_ADB(Integer
                    .parseInt(spnr_AdbTerm.getSelectedItem().toString()));
            saralJeevanBimaBean.setSumAssured_ADB(Integer.parseInt(edt_AdbSA
                    .getText().toString()));
        } else {
            saralJeevanBimaBean.setADB_Status(false);
        }
        // ATPDB Rider
        if (cbATPDBRider.isChecked() == true) {
            saralJeevanBimaBean.setATPDB_Status(true);
            saralJeevanBimaBean.setPolicyTerm_ATPDB(Integer
                    .parseInt(spnr_AtpdbTerm.getSelectedItem().toString()));
            saralJeevanBimaBean.setSumAssured_ATPDB(Integer
                    .parseInt(edt_AtpdbSA.getText().toString()));
        } else {
            saralJeevanBimaBean.setATPDB_Status(false);
        }
        // CC13 Non-Linked Rider
        if (cbCC13NonLinkedRider.isChecked() == true) {
            saralJeevanBimaBean.setCC13NonLinked_Status(true);
            saralJeevanBimaBean.setPolicyTerm_CC13NonLinked(Integer
                    .parseInt(spnr_Cc13NonLinkedTerm.getSelectedItem()
                            .toString()));
            saralJeevanBimaBean.setSumAssured_CC13NonLinked(Integer
                    .parseInt(edt_Cc13NonLinkedSA.getText().toString()));
        } else {
            saralJeevanBimaBean.setCC13NonLinked_Status(false);
        }

        // Show Output Form
        showSmartMoneyBackGoldOutputPg(saralJeevanBimaBean);

    }

    private void getInput(SaralJeevanBimaBean saralJeevanBimaBean) {
        inputVal = new StringBuilder();

        int age = saralJeevanBimaBean.getAge();
        String strProposerAge = saralJeevanBimaBean.getStrProposerAge();
        String gender = saralJeevanBimaBean.getGender();
        String gender_proposer = saralJeevanBimaBean.getGender_proposer();
        int basicPolicyTerm = saralJeevanBimaBean.getPolicyTerm();
        int ptTerm = saralJeevanBimaBean.getPolicyTerm_PT();
        int adbTerm = saralJeevanBimaBean.getPolicyTerm_ADB();
        int atpdbTerm = saralJeevanBimaBean.getPolicyTerm_ATPDB();
        int ccnlTerm = saralJeevanBimaBean.getPolicyTerm_CC13NonLinked();

        double basicSumAssured = saralJeevanBimaBean.getSumAssured_Basic();
        double ccnlSumAssured = saralJeevanBimaBean
                .getSumAssured_CC13NonLinked();
        double ptSumAssured = saralJeevanBimaBean.getSumAssured_PT();
        double adbSumAssured = saralJeevanBimaBean.getSumAssured_ADB();
        double atpdbSumAssured = saralJeevanBimaBean.getSumAssured_ATPDB();
        String PremPayingMode = saralJeevanBimaBean.getPremiumFreq();
        String premFreqOptions = saralJeevanBimaBean.getPremFreqOptions();
        String PremPaymentTerm = saralJeevanBimaBean.getPremPaymentTerm();

        boolean isJKresident = saralJeevanBimaBean.getJkResident();
        boolean isStaffOrNot = saralJeevanBimaBean.getStaffDisc();
        boolean statusPT = saralJeevanBimaBean.getPT_Status();
        boolean statusADB = saralJeevanBimaBean.getADB_Status();
        boolean statusATPDB = saralJeevanBimaBean.getATPDB_Status();
        boolean statusCCNL = saralJeevanBimaBean.getCC13NonLinked_Status();

        inputVal.append("<?xml version='1.0' encoding='utf-8' ?><saraljeevanbima>");

        inputVal.append("<LifeAssured_title>").append(lifeAssured_Title).append("</LifeAssured_title>");
        inputVal.append("<LifeAssured_firstName>").append(lifeAssured_First_Name).append("</LifeAssured_firstName>");
        inputVal.append("<LifeAssured_middleName>").append(lifeAssured_Middle_Name).append("</LifeAssured_middleName>");
        inputVal.append("<LifeAssured_lastName>").append(lifeAssured_Last_Name).append("</LifeAssured_lastName>");
        inputVal.append("<LifeAssured_DOB>").append(lifeAssured_date_of_birth).append("</LifeAssured_DOB>");
        inputVal.append("<LifeAssured_age>").append(age).append("</LifeAssured_age>");


        inputVal.append("<isJKResident>").append(isJKresident).append("</isJKResident>");
        inputVal.append("<isStaff>").append(isStaffOrNot).append("</isStaff>");
        inputVal.append("<str_kerla_discount>").append(str_kerla_discount).append("</str_kerla_discount>");
        inputVal.append("<age>").append(age).append("</age>");
        inputVal.append("<strProposerAge>").append(strProposerAge).append("</strProposerAge>");
        inputVal.append("<gender>").append(gender).append("</gender>");
        inputVal.append("<gender_proposer>").append(gender_proposer).append("</gender_proposer>");
        String proposerAge = "";
        inputVal.append("<ProposerAge>").append(proposerAge).append("</ProposerAge>");

        inputVal.append("<proposer_title>").append(proposer_Title).append("</proposer_title>");
        inputVal.append("<proposer_firstName>").append(proposer_First_Name).append("</proposer_firstName>");
        inputVal.append("<proposer_middleName>").append(proposer_Middle_Name).append("</proposer_middleName>");
        inputVal.append("<proposer_lastName>").append(proposer_Last_Name).append("</proposer_lastName>");
        inputVal.append("<proposer_DOB>").append(proposer_date_of_birth).append("</proposer_DOB>");
        inputVal.append("<proposer_age>").append("").append("</proposer_age>");
        inputVal.append("<proposer_gender>").append("").append("</proposer_gender>");

        inputVal.append("<product_name>").append(planName).append("</product_name>");
        /* parivartan changes */
        inputVal.append("<product_Code>").append(product_Code).append("</product_Code>");
        inputVal.append("<product_UIN>").append(product_UIN).append("</product_UIN>");
        inputVal.append("<product_cateogory>").append(product_cateogory).append("</product_cateogory>");
        inputVal.append("<product_type>").append(product_type).append("</product_type>");
        inputVal.append("<gender_proposer>").append(gender_proposer).append("</gender_proposer>");

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

        inputVal.append("<str_is_defence_occupation>").append(str_is_defence_occupation).append("</str_is_defence_occupation>");
        inputVal.append("<str_age_proof>").append(str_age_proof).append("</str_age_proof>");
        inputVal.append("<str_occupation_type>").append(str_occupation_type).append("</str_occupation_type>");
        inputVal.append("<str_occupation_rank>").append(str_occupation_rank).append("</str_occupation_rank>");
        inputVal.append("<str_occupation_location>").append(str_occupation_location).append("</str_occupation_location>");
        inputVal.append("<FLAG_OCCUPATION_EXTRA>").append(FLAG_OCCUPATION_EXTRA).append("</FLAG_OCCUPATION_EXTRA>");
        inputVal.append("<proposer_Is_Same_As_Life_Assured>").append(proposer_Is_Same_As_Life_Assured).append("</proposer_Is_Same_As_Life_Assured>");


        inputVal.append("<Product_Cat>").append(product_cateogory).append("</Product_Cat>");
        inputVal.append("<MAT_VALUE_PROD_CHANGE>").append(STR_MAT_VALUE_PROD_CHANGE).append("</MAT_VALUE_PROD_CHANGE>");
        inputVal.append("<MAT_VALUE_PROD_QUT_NO>").append(STR_MAT_VALUE_PROD_QUT_NO).append("</MAT_VALUE_PROD_QUT_NO>");
        inputVal.append("<ProposalMode>").append(ProposalMode).append("</ProposalMode>");

        inputVal.append("<BIVERSION>" + "1" + "</BIVERSION>");
        //Added Tushar Kadam Kerla Applicable Tax 7-Jun-2019
        String str_kerla_discount = "N";
        if (cb_kerladisc.isChecked()) {
            str_kerla_discount = "Y";
        }
        inputVal.append("<KFC>" + str_kerla_discount + "</KFC>");
        inputVal.append("</saraljeevanbima>");

        System.out.println("inputVal = " + inputVal);

    }

    public void showSmartMoneyBackGoldOutputPg(
            SaralJeevanBimaBean saralJeevanBimaBean1) {

        String BI = "";

        SaralJeevanBimaBusinessLogic saraljeevanbimabusinessLogic = new SaralJeevanBimaBusinessLogic(saralJeevanBimaBean1);
        SaralJeevanBimaProperties saralprop = new SaralJeevanBimaProperties();
        CommonForAllProd commonForAllProd = new CommonForAllProd();
//		String[] outputArr = getOutput("BI_of_SaralJeevanBima",saralJeevanBimaBean1);
//		String valMinMaxPremiumError = valMinMaxPremium(Double.parseDouble(BasicPremium));


        String staffStatus = "";
        if (cb_staffdisc.isChecked()) {
            staffStatus = "sbi";
            // disc_Basic_SelFreq
        } else {
            staffStatus = "none";
        }
        String staffRebate = saraljeevanbimabusinessLogic
                .getStaffRebate(cb_staffdisc.isChecked()) + "";

        double basicPremium = 0;
        String LargeSa = commonForAllProd.getStringWithout_E(saraljeevanbimabusinessLogic.getlargeSA(saralJeevanBimaBean.getSumAssured_Basic()));
        BasicPremium = commonForAllProd.getRoundOffLevel2(commonForAllProd.getStringWithout_E(saraljeevanbimabusinessLogic.get_Premium_without_tax()));

        String Premium = (commonForAllProd.getStringWithout_E(saraljeevanbimabusinessLogic.get_Premium_without_tax()));
        String premRoundUp = commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(Double.parseDouble(BasicPremium)));

        double basicServiceTax = 0;

        if (cb_kerladisc.isChecked()) {
            basicServiceTax = saraljeevanbimabusinessLogic.get_premium_withtotalServiceTax(Double.parseDouble(BasicPremium));
        } else {
            basicServiceTax = saraljeevanbimabusinessLogic.get_premium_withtotalServiceTax(Double.parseDouble(BasicPremium));
        }

        serviceTax = commonForAllProd.getStringWithout_E(basicServiceTax);

        PremiumwithST = commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E((Double.parseDouble(premRoundUp)) + Double.parseDouble(serviceTax)));
        System.out.println("PremiumwithST " + PremiumwithST);
        boolean valPremiumError = valInstPremium(BasicPremium);
        if (valPremiumError == true) {
            String basicPremiumST = commonForAllProd.getStringWithout_E(saraljeevanbimabusinessLogic.get_Premium_without_tax());
            String backDateinterest = "0";
            basicPremiumST = commonForAllProd
                    .getStringWithout_E(Double
                            .parseDouble(basicPremiumST)
                            + Double.parseDouble(backDateinterest));

            BI = getOutput("Illustration", saralJeevanBimaBean1);

            retVal.append("<?xml version='1.0' encoding='utf-8' ?><SaralJeevanBima>");
            retVal.append("<errCode>0</errCode>");

            retVal.append("<staffDiscCode>").append(saraljeevanbimabusinessLogic.getStaffRebate()).append("</staffDiscCode>");

            retVal.append("<basicPrem>").append(commonForAllProd.getRoundOffLevel2(commonForAllProd.getStringWithout_E(Double.parseDouble(Premium)))).append("</basicPrem>");
            retVal.append("<installmentPrem>").append(commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(Double.parseDouble(BasicPremium)))).append("</installmentPrem>");
            retVal.append("<SumAssured>").append(commonForAllProd.getRound(commonForAllProd.getStringWithout_E(saralJeevanBimaBean1.getSumAssured_Basic()))).append("</SumAssured>");
            retVal.append("<ApplicableTax>").append(commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(saraljeevanbimabusinessLogic.get_premium_withtotalServiceTax(Double.parseDouble((Premium)))))).append("</ApplicableTax>");
            retVal.append("<PremiumwithST>").append(commonForAllProd.getStringWithout_E(Double.parseDouble(PremiumwithST))).append("</PremiumwithST>");
            retVal.append("<staffStatus>").append(staffStatus).append("</staffStatus>");
            retVal.append("<staffRebate>").append(staffRebate).append("</staffRebate>");

            retVal.append(bussIll.toString());

            retVal.append("</SaralJeevanBima>");
        }
        //return retVal.toString();
    }

    public String getOutput(String sheetName,
                            SaralJeevanBimaBean saraljeevanbimabean) {
        SaralJeevanBimaBusinessLogic saraljeevanbimabusinessLogic = new SaralJeevanBimaBusinessLogic(saraljeevanbimabean);
        bussIll = new StringBuilder();
        CommonForAllProd commonForAllProd = new CommonForAllProd();
        int _year_F = 0;


        try {
            String policyTerm1 = (commonForAllProd.getStringWithout_E(saraljeevanbimabean.getPolicyTerm()));
            String sumAssured1 = commonForAllProd.getStringWithout_E(saraljeevanbimabean.getSumAssured_Basic());
            BasicPremium = commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(saraljeevanbimabusinessLogic.get_Premium_without_tax()));
            ApplicableTax = (commonForAllProd.getStringWithout_E(saraljeevanbimabusinessLogic.getbasicServicetax(Double.parseDouble(BasicPremium))));
            PremiumwithST = (commonForAllProd.getStringWithout_E(Double.parseDouble(PremiumwithST)));

            bussIll.append("<PolicyTerm>").append(policyTerm1).append("</PolicyTerm>");
            bussIll.append("<SumAssured>").append(sumAssured1).append("</SumAssured>");
            bussIll.append("<BasicPremium>").append(BasicPremium).append("</BasicPremium>");
            bussIll.append("<ApplicableTax>").append(ApplicableTax).append("</ApplicableTax>");
            bussIll.append("<PremiumwithST>").append(PremiumwithST).append("</PremiumwithST>");

        } catch (Exception e) {
            e.printStackTrace();
        }

        return bussIll.toString();
    }



   /* public boolean valBackdate() {
        if (proposer_Backdating_WishToBackDate_Policy.equals("y")) {

            if (proposer_Backdating_BackDate.equals("")) {
                showAlert.setMessage("Please Select Backdate ");
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
                showAlert.show();
                return false;
            } else {
                return true;
            }
        }
        return true;
    }*/

    /*boolean TrueBackdate() {
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
                                @Override
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

    }*/

    /********************************** Added by Akshaya on 03-Mar-2015 end ***************************/

    public boolean valLifeAssuredProposerDetail() {
        if (proposer_Is_Same_As_Life_Assured.equalsIgnoreCase("y")) {
            if (lifeAssured_Title.equals("")
                    || lifeAssured_First_Name.equals("")
                    || lifeAssured_Last_Name.equals("")) {

                commonMethods.showMessageDialog(context, "Please Fill Name Detail For LifeAssured");

                if (lifeAssured_Title.equals("")) {
                    // apply focusable method
                    commonMethods.setFocusable(spnr_bi_smart_money_back_gold_life_assured_title);
                    spnr_bi_smart_money_back_gold_life_assured_title
                            .requestFocus();
                } else if (lifeAssured_First_Name.equals("")) {
                    commonMethods.setFocusable(edt_bi_smart_money_back_gold_life_assured_first_name);
                    edt_bi_smart_money_back_gold_life_assured_first_name
                            .requestFocus();
                } else {
                    commonMethods.setFocusable(edt_bi_smart_money_back_gold_life_assured_last_name);
                    edt_bi_smart_money_back_gold_life_assured_last_name
                            .requestFocus();
                }

                return false;
            } else if (lifeAssured_Title.equalsIgnoreCase("Mr.")
                    && gender.equalsIgnoreCase("Female")) {

                commonMethods.showMessageDialog(context, "Life Assured Title and Gender is not Valid");
                commonMethods.setFocusable(spnr_bi_smart_money_back_gold_life_assured_title);
                spnr_bi_smart_money_back_gold_life_assured_title
                        .requestFocus();

                return false;

            } else if (lifeAssured_Title.equalsIgnoreCase("Ms.")
                    && gender.equalsIgnoreCase("Male")) {

                commonMethods.showMessageDialog(context, "Life Assured Title and Gender is not Valid");
                // apply focusable method
                commonMethods.setFocusable(spnr_bi_smart_money_back_gold_life_assured_title);
                spnr_bi_smart_money_back_gold_life_assured_title
                        .requestFocus();

                return false;

            } else if (lifeAssured_Title.equalsIgnoreCase("Mrs.")
                    && gender.equalsIgnoreCase("Male")) {

                commonMethods.showMessageDialog(context, "Life Assured Title and Gender is not Valid");
                commonMethods.setFocusable(spnr_bi_smart_money_back_gold_life_assured_title);
                spnr_bi_smart_money_back_gold_life_assured_title
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
                    commonMethods.setFocusable(spnr_bi_smart_money_back_gold_life_assured_title);
                    spnr_bi_smart_money_back_gold_life_assured_title
                            .requestFocus();
                } else if (lifeAssured_First_Name.equals("")) {

                    edt_bi_smart_money_back_gold_life_assured_first_name
                            .requestFocus();
                } else {
                    edt_bi_smart_money_back_gold_life_assured_last_name
                            .requestFocus();
                }

                return false;
            } else if (lifeAssured_Title.equalsIgnoreCase("Mr.")
                    && gender.equalsIgnoreCase("Female")) {

                commonMethods.showMessageDialog(context, "Life Assured Title and Gender is not Valid");
                // apply focusable method
                commonMethods.setFocusable(spnr_bi_smart_money_back_gold_life_assured_title);
                spnr_bi_smart_money_back_gold_life_assured_title
                        .requestFocus();

                return false;

            } else if (lifeAssured_Title.equalsIgnoreCase("Ms.")
                    && gender.equalsIgnoreCase("Male")) {

                commonMethods.showMessageDialog(context, "Life Assured Title and Gender is not Valid");
                commonMethods.setFocusable(spnr_bi_smart_money_back_gold_life_assured_title);
                spnr_bi_smart_money_back_gold_life_assured_title
                        .requestFocus();
                return false;

            } else if (lifeAssured_Title.equalsIgnoreCase("Mrs.")
                    && gender.equalsIgnoreCase("Male")) {

                commonMethods.showMessageDialog(context, "Life Assured Title and Gender is not Valid");
                commonMethods.setFocusable(spnr_bi_smart_money_back_gold_life_assured_title);
                spnr_bi_smart_money_back_gold_life_assured_title
                        .requestFocus();

                return false;
            } else if (proposer_Title.equals("")
                    || proposer_First_Name.equals("")
                    || proposer_Last_Name.equals("")) {

                commonMethods.showMessageDialog(context, "Please Fill Name Detail For Proposer");
                if (proposer_Title.equals("")) {
                    // apply focusable method
                                    /*commonMethods.setFocusable(spnr_bi_money_back_gold_proposer_title);
                                    spnr_bi_money_back_gold_proposer_title
                                            .requestFocus();*/
                } else if (proposer_First_Name.equals("")) {

                } else {
                }

                return false;

            } else if (gender_proposer.equalsIgnoreCase("")) {
                commonMethods.dialogWarning(context, "Please Select Proposer Gender", true);

                return false;
            } else if (proposer_Title.equalsIgnoreCase("Mr.")
                    && gender_proposer.equalsIgnoreCase("Female")) {

                commonMethods.showMessageDialog(context, "Proposer Title and Gender is not Valid");

                return false;

            } else if (proposer_Title.equalsIgnoreCase("Ms.")
                    && gender_proposer.equalsIgnoreCase("Male")) {

                commonMethods.showMessageDialog(context, "Proposer Title and Gender is not Valid");
                return false;

            } else if (proposer_Title.equalsIgnoreCase("Mrs.")
                    && gender_proposer.equalsIgnoreCase("Male")) {

                commonMethods.showMessageDialog(context, "Proposer Title and Gender is not Valid");

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
                // apply focusable method
                commonMethods.setFocusable(btn_bi_smart_money_back_gold_life_assured_date);
                btn_bi_smart_money_back_gold_life_assured_date
                        .requestFocus();
                return false;
            } else {
                return true;
            }
        } else
            return true;
    }


    public boolean valPremiumpaymentOption() {


        if (premFreqOptions.equals("")) {
            commonMethods.showMessageDialog(context, "Please Select Premium Payment Option");
            commonMethods.setFocusable(selPremFreq);
            selPremFreq
                    .requestFocus();
            return false;
        } else {
            return true;
        }

    }

    boolean valEligibilityOfRider() {
        StringBuilder error = new StringBuilder();
        if (!((Integer.parseInt(spnrAge.getSelectedItem().toString()) >= 18) && (Integer
                .parseInt(spnrAge.getSelectedItem().toString()) <= 60))) {
            if (cbPTARider.isChecked()) {
                cbPTARider.setChecked(false);
                if (trPreffredTermRider.getVisibility() == View.VISIBLE) {
                    trPreffredTermRider.setVisibility(View.GONE);
                    trPreffredTermRider2.setVisibility(View.GONE);
                }

                error.append("To avail Preferred Term Rider,age should be in the range of 18 to 60 Years. \n");

            }

            if (cbADBRider.isChecked()) {
                cbADBRider.setChecked(false);
                if (trADBRider.getVisibility() == View.VISIBLE) {
                    trADBRider.setVisibility(View.GONE);
                    trADBRider2.setVisibility(View.GONE);
                }
                error.append("To avail Accidental Death Benefit Rider,age should be in the range of 18 to 60 Years. \n");

            }

            if (cbATPDBRider.isChecked()) {
                cbATPDBRider.setChecked(false);
                if (trATPDBRider.getVisibility() == View.VISIBLE) {
                    trATPDBRider.setVisibility(View.GONE);
                    trATPDBRider2.setVisibility(View.GONE);
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
                    trCritiCareRider2.setVisibility(View.GONE);
                }

                error.append("To avail Criti Care 13 Non - Linked Rider,age should be in the range of 18 to 55 Years. \n");
            }
        }

        if (!error.toString().equals("")) {
            commonMethods.showMessageDialog(context, error.toString());
            commonMethods.setFocusable(btn_bi_smart_money_back_gold_life_assured_date);
            btn_bi_smart_money_back_gold_life_assured_date.requestFocus();
            return false;
        } else
            return true;
    }

    // Validate Rider Term
    boolean valTermRider() {
        int maxLimit = Integer.parseInt(spnrPolicyTerm.getSelectedItem()
                .toString());
        StringBuilder error = new StringBuilder();

        if (cbPTARider.isChecked()
                && Integer.parseInt(spnr_PtaTerm.getSelectedItem().toString()) > maxLimit) {
            spnr_PtaTerm.setSelection(0, false);
            error.append("Please enter Preferred Term Rider term between 5 and ").append(maxLimit).append(" years");

            commonMethods.setFocusable(spnr_PtaTerm);
            spnr_PtaTerm.requestFocus();
        }

        if (cbADBRider.isChecked()) {
            maxLimit = Math.min(Integer.parseInt(spnrPolicyTerm
                    .getSelectedItem().toString()), 30);
            if (Integer.parseInt(spnr_AdbTerm.getSelectedItem().toString()) > maxLimit) {
                spnr_AdbTerm.setSelection(0, false);
                error.append("Please enter Accidental Death Benefit Rider term between 5 and ").append(maxLimit).append(" years");
                commonMethods.setFocusable(spnr_AdbTerm);
                spnr_AdbTerm.requestFocus();
            }
        }

        if (cbATPDBRider.isChecked()
                && Integer
                .parseInt(spnr_AtpdbTerm.getSelectedItem().toString()) > maxLimit) {
            spnr_AtpdbTerm.setSelection(0, false);
            error.append("Please enter Accidental Total and Permenent Disability Rider term between 5 and ").append(maxLimit).append(" years");
            commonMethods.setFocusable(spnr_AtpdbTerm);
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

                commonMethods.setFocusable(spnr_Cc13NonLinkedTerm);
                spnr_Cc13NonLinkedTerm.requestFocus();
            }
        }
        if (!error.toString().equals("")) {

            commonMethods.showMessageDialog(context, error.toString());
            return false;
        }
        return true;
    }

    // Age Validation after CALCULATE Button is pressed
    public boolean valAge() {
        int Age = Integer.parseInt(spnrAge.getSelectedItem().toString());
        int PolicyTerm = Integer.parseInt(spnrPolicyTerm.getSelectedItem()
                .toString());
        boolean stat = true;

        try {

            if ((Age + PolicyTerm) > 70) {
                commonMethods.showMessageDialog(context, "Max. Maturity age is 70 years");
                spnrAge.setSelection(0, false);
                commonMethods.setFocusable(btn_bi_smart_money_back_gold_life_assured_date);
                btn_bi_smart_money_back_gold_life_assured_date.requestFocus();
                spnrAge.setSelection(0, false);
                btn_bi_smart_money_back_gold_life_assured_date
                        .setText("Select Date");
                lifeAssured_date_of_birth = "";
                stat = false;
            }


        } catch (Exception e) {
        }
        return stat;
    }

    Boolean valSA() {
        StringBuilder error = new StringBuilder();
        // System.out.println("basic sa "+Double.parseDouble(basicSA.getText().toString()));
        if (edt_SumAssured.getText().toString().equals("")
                || Double.parseDouble(edt_SumAssured.getText().toString()) < 500000) {
            error.append("Sum assured should be greater than or equal to 5,00,000");

        }
        if (edt_SumAssured.getText().toString().equals("")
                || Double.parseDouble(edt_SumAssured.getText().toString()) > 2500000) {
            error.append("Sum assured should be less than or equal to 25,00,000");

        } else if (Double.parseDouble(edt_SumAssured.getText().toString()) % 50000 != 0) {
            error.append("Sum assured should be multiple of 50,000");
        } else {

        }

        if (!error.toString().equals("")) {
            commonMethods.showMessageDialog(context, error.toString());
            commonMethods.setFocusable(edt_SumAssured);
            edt_SumAssured.requestFocus();
            return false;
        } else
            return true;
    }

    // ****************************** Validation ends Here
    // *******************************************//


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
                        commonMethods.showMessageDialog(context, "Please fill Valid Bith Date");
                    } else {

                        if (minAge <= age && age <= maxAge) {

                            if (Integer.parseInt(final_age) < 18) {
                                proposer_Is_Same_As_Life_Assured = "N";
                                /*tr_money_back_gold_proposer_detail1
                                        .setVisibility(View.VISIBLE);
                                tr_money_back_gold_proposer_detail12
                                        .setVisibility(View.VISIBLE);
                                tr_money_back_gold_proposer_detail13
                                        .setVisibility(View.VISIBLE);
                                tr_money_back_gold_proposer_detail2
                                        .setVisibility(View.VISIBLE);
                                tr_money_back_gold_proposer_detail22
                                        .setVisibility(View.VISIBLE);
                                tr_proposer_gender.setVisibility(View.VISIBLE);
                                tr_proposer_gender2.setVisibility(View.VISIBLE);*/

                                btn_bi_smart_money_back_gold_life_assured_date
                                        .setText(date);

                                spnrAge.setSelection(getIndex(spnrAge, final_age),
                                        false);
                                valAge();
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

                                commonMethods.clearFocusable(btn_bi_smart_money_back_gold_life_assured_date);

                            } else {
                                proposer_Is_Same_As_Life_Assured = "Y";
                                /*tr_money_back_gold_proposer_detail1
                                        .setVisibility(View.GONE);
                                tr_money_back_gold_proposer_detail12
                                        .setVisibility(View.GONE);
                                tr_money_back_gold_proposer_detail13
                                        .setVisibility(View.GONE);
                                tr_money_back_gold_proposer_detail2
                                        .setVisibility(View.GONE);
                                tr_money_back_gold_proposer_detail22
                                        .setVisibility(View.GONE);
                                tr_proposer_gender.setVisibility(View.GONE);
                                tr_proposer_gender2.setVisibility(View.GONE);*/

                                btn_bi_smart_money_back_gold_life_assured_date
                                        .setText(date);

                                spnrAge.setSelection(getIndex(spnrAge, final_age),
                                        false);
                                valAge();
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

                                commonMethods.clearFocusable(btn_bi_smart_money_back_gold_life_assured_date);
                                commonMethods.setFocusable(edt_proposerdetail_basicdetail_contact_no);
                                edt_proposerdetail_basicdetail_contact_no
                                        .requestFocus();
                            }

                            /*
                             * commonMethods.setFocusable(spnrPlan); spnrPlan.requestFocus();
                             */

                            // commonMethods.setFocusable(edt_premiumAmt);
                            // edt_premiumAmt.requestFocus();
                        } else {
                            commonMethods.showMessageDialog(context, "Minimum Age should be " + minAge
                                    + " yrs and Maximum Age should be " + maxAge
                                    + " yrs For LifeAssured");
                            btn_bi_smart_money_back_gold_life_assured_date
                                    .setText("Select Date");
                            lifeAssured_date_of_birth = "";
                            commonMethods.clearFocusable(btn_bi_smart_money_back_gold_life_assured_date);
                            commonMethods.setFocusable(btn_bi_smart_money_back_gold_life_assured_date);
                            btn_bi_smart_money_back_gold_life_assured_date
                                    .requestFocus();
                        }
                    }
                    break;
                case 5:
                    strProposerAge = final_age;
                    final_age = Integer.toString(age);
                    if (final_age.contains("-")) {
                        commonMethods.showMessageDialog(context, "Please fill Valid Bith Date");
                    } else {
                        if (18 <= age && age <= 100) {


                            commonMethods.setFocusable(edt_proposerdetail_basicdetail_contact_no);
                            edt_proposerdetail_basicdetail_contact_no
                                    .requestFocus();

                            // ageInYears
                            // .setSelection(getIndex(ageInYears, final_age));
                            // updatePolicyTermLabel();
                            // updateSAMFlabel();
                        } else {
                            commonMethods.showMessageDialog(context, "Minimum age should be 18 yrs for proposer");
                        }
                    }
                    break;

                case 6:
                    if (age >= 0) {
                        proposer_Backdating_BackDate = date + "";
                        btn_proposerdetail_personaldetail_backdatingdate
                                .setText(proposer_Backdating_BackDate);
                        commonMethods.clearFocusable(btn_proposerdetail_personaldetail_backdatingdate);

                        commonMethods.setFocusable(cbPTARider);
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

        String[] ProposerDob = getDate(lifeAssured_date_of_birth).split("-");
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
        showDateDialog();

    }

    public void onClickDob(View v) {
        // setDefaultDate();
        DIALOG_ID = 5;
        showDateDialog();
    }

    public void onClickBackDating(View v) {
        String backdate = commonMethods.getMMDDYYYYDatabaseDate(proposer_Backdating_BackDate);
        initialiseDateParameter(backdate, 0);
        DIALOG_ID = 6;
        if (lifeAssured_date_of_birth != null
                && !lifeAssured_date_of_birth.equals("")) {
            showDateDialog();
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

    public void setDefaultDate(int id) {
        Calendar present_date = Calendar.getInstance();
        present_date.add(Calendar.YEAR, -id);
        mDay = present_date.get(Calendar.DAY_OF_MONTH);
        mMonth = present_date.get(Calendar.MONTH);
        mYear = present_date.get(Calendar.YEAR);
    }

    public String getformatedThousandString(int number) {
        return NumberFormat.getNumberInstance(Locale.US)
                .format(number);
    }

    public String getCurrentDate() {
        Calendar present_date = Calendar.getInstance();
        int mDay = present_date.get(Calendar.DAY_OF_MONTH);
        int mMonth = present_date.get(Calendar.MONTH) + 1;
        int mYear = present_date.get(Calendar.YEAR);

        return mDay + "-" + mMonth + "-" + mYear;

    }

    /**
     * Used To Change date From mm-dd-yyyy to dd-mm-yyyy
     */
    public String getDate(String OldDate) {
        String NewDate = "";
        try {
            DateFormat userDateFormat = new SimpleDateFormat("MM-dd-yyyy");
            DateFormat dateFormatNeeded = new SimpleDateFormat("dd-MM-yyyy");
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

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

        if (v.getId() == edt_bi_smart_money_back_gold_life_assured_first_name
                .getId()) {
            commonMethods.setFocusable(edt_bi_smart_money_back_gold_life_assured_middle_name);
            edt_bi_smart_money_back_gold_life_assured_middle_name
                    .requestFocus();
        } else if (v.getId() == edt_bi_smart_money_back_gold_life_assured_middle_name
                .getId()) {
            commonMethods.setFocusable(edt_bi_smart_money_back_gold_life_assured_last_name);
            edt_bi_smart_money_back_gold_life_assured_last_name.requestFocus();
        } else if (v.getId() == edt_bi_smart_money_back_gold_life_assured_last_name
                .getId()) {
            commonMethods.setFocusable(btn_bi_smart_money_back_gold_life_assured_date);
            btn_bi_smart_money_back_gold_life_assured_date.requestFocus();
        } else if (v.getId() == edt_proposerdetail_basicdetail_contact_no
                .getId()) {
            commonMethods.setFocusable(edt_proposerdetail_basicdetail_Email_id);
            edt_proposerdetail_basicdetail_Email_id.requestFocus();
        } else if (v.getId() == edt_proposerdetail_basicdetail_Email_id.getId()) {
            commonMethods.setFocusable(edt_proposerdetail_basicdetail_ConfirmEmail_id);
            edt_proposerdetail_basicdetail_ConfirmEmail_id.requestFocus();
        } else if (v.getId() == edt_proposerdetail_basicdetail_ConfirmEmail_id
                .getId()) {
            commonMethods.clearFocusable(spnrPlan);
            commonMethods.setFocusable(spnrPlan);
            spnrPlan.requestFocus();
        } else if (v.getId() == edt_SumAssured.getId()) {

            commonMethods.hideKeyboard(edt_SumAssured, context);

            /*
             * commonMethods.clearFocusable(edt_SumAssured);
             * commonMethods.setFocusable(rb_proposerdetail_personaldetail_backdating_yes);
             * rb_proposerdetail_personaldetail_backdating_yes.requestFocus();
             */

            commonMethods.setFocusable(cbPTARider);
            cbPTARider.requestFocus();
        } else if (v.getId() == edt_PtaSA.getId()) {
            commonMethods.setFocusable(cbADBRider);
            cbADBRider.requestFocus();
        } else if (v.getId() == edt_AdbSA.getId()) {
            commonMethods.setFocusable(cbATPDBRider);
            cbATPDBRider.requestFocus();
        } else if (v.getId() == edt_AtpdbSA.getId()) {
            commonMethods.setFocusable(cbCC13NonLinkedRider);
            cbCC13NonLinkedRider.requestFocus();
        } else if (v.getId() == edt_Cc13NonLinkedSA.getId()) {

            //UtilityMain.hideKeyboard(BI_SaralJeevanBimaActivity.this);

        }

        return true;
    }

    public boolean createPdf() {
        try {

            // System.out.println("  "+maturityAge+
            // "  "+annPrem+" "+sumAssured);
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
            Font normal_italic = new Font(Font.FontFamily.TIMES_ROMAN, 12,
                    Font.ITALIC);
            Font normal_bolditalic = new Font(Font.FontFamily.TIMES_ROMAN, 12,
                    Font.BOLDITALIC);
            Font normal1_BoldUnderline = new Font(Font.FontFamily.TIMES_ROMAN,
                    6, Font.UNDERLINE | Font.BOLD);
            Font normal_bold = new Font(Font.FontFamily.TIMES_ROMAN, 5,
                    Font.BOLD);
            Font normal = new Font(Font.FontFamily.TIMES_ROMAN, 5, Font.NORMAL);
            Font normal1_bold = new Font(Font.FontFamily.TIMES_ROMAN, 6,
                    Font.BOLD);
            Font normal1 = new Font(Font.FontFamily.TIMES_ROMAN, 6, Font.NORMAL);

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

            cell = new PdfPCell(new Phrase("\n", headerBold));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("SBI Life Insurance Co. Ltd",
                    normal1));
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
                            "Registered Corporate Office: 'Natraj', M.V. Road and Western Express, Highway Junction, Andheri (East)",
                            normal1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);

            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Mumbai – 400069. Registration No. 111",
                    normal1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);

            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Website: www.sbilife.co.in | Email: info@sbilife.co.in | CIN: L99999MH2000PLC129113" + "\n" + "Toll Free: 1800 267 9090 (Between 9.00 am to 9.00 pm)" + "\n" + "Premium Quotation" + "\n",
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
                    "SBI Life- Saral Jeevan Bima(111N128V01) | An Individual, Non- Linked, Non-Participating Life Insurance Pure Risk Premium Product", normal1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);

            table.addCell(cell);

            cell = new PdfPCell(new Phrase(
                    "SBI Life- Saral Jeevan Bima(111N128V01)", headerBold));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);

            table.addCell(cell);

            cell = new PdfPCell(new Phrase("\n", normal));
            cell.setColspan(3);
            cell.setBorder(Rectangle.BOTTOM);
            cell.setBorderWidth(1.2f);
            table.addCell(cell);

            LineSeparator ls = new LineSeparator();
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
                    "Some benefits are guaranteed and some benefits are variable with returns based on the future performance of your insurer carrying on life insurance business. If your policy offers guaranteed benefits then these will be clearly marked “guaranteed” in the illustration table on this page. If your policy offers variable benefits then the illustrations on this page will show two different rates of assumed future investment returns, of 8% p.a. and 4% p.a. These assumed rates of return are not guaranteed and they are not the upper or lower limits of what you might get back, as the value of your policy is dependent on a number of factors including future investment performance.",
                    normal1);
            para4.setAlignment(Element.ALIGN_LEFT | Element.ALIGN_JUSTIFIED);


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
                    "Proposer Name ", small_normal));
            PdfPCell NameofProposal_cell_4 = new PdfPCell(new Paragraph(
                    name_of_life_assured, normal1_bold));
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


            PdfPTable personalDetail_table = new PdfPTable(2);
            personalDetail_table.setWidths(new float[]{5f, 5f});
            personalDetail_table.setWidthPercentage(100f);
            personalDetail_table.setHorizontalAlignment(Element.ALIGN_CENTER);

            // 1 row
            cell = new PdfPCell(new Phrase(" Product ", normal1));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            personalDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase(
                    "SBI Life - Saral Jeevan Bima (UIN: 111N128V01)", normal1));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            personalDetail_table.addCell(cell);


            // 3 row
            cell = new PdfPCell(new Phrase(" Age", normal1));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            personalDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase(" " + ageAtEntry, normal1));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            personalDetail_table.addCell(cell);

            // 4 row
            cell = new PdfPCell(new Phrase(" Gender", normal1));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            personalDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase("" + gender, normal1));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            personalDetail_table.addCell(cell);


            // 6 row


            // 6 row
            /*cell = new PdfPCell(new Phrase("State", normal1));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            personalDetail_table.addCell(cell);
            if (cb_kerladisc.isChecked()) {
                cell = new PdfPCell(new Phrase("Kerala ", normal1));
            } else {
                cell = new PdfPCell(new Phrase("Non-Kerala ", normal1));
            }
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            personalDetail_table.addCell(cell);*/


            cell = new PdfPCell(new Phrase(" Frequency", normal1));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            personalDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase(" " + premPayingMode, normal1));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            personalDetail_table.addCell(cell);
            // Basic Cover

            PdfPTable basicCover_table = new PdfPTable(4);
            basicCover_table.setWidths(new float[]{15f, 5f, 5f, 5f});
            basicCover_table.setWidthPercentage(100f);
            basicCover_table.setHorizontalAlignment(Element.ALIGN_LEFT);

            // 1st row
            cell = new PdfPCell(new Phrase("", normal1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            basicCover_table.addCell(cell);

            cell = new PdfPCell(new Phrase("Term (Years)", normal1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            basicCover_table.addCell(cell);

            cell = new PdfPCell(new Phrase("Sum Assured (Rs.)", normal1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            basicCover_table.addCell(cell);

            cell = new PdfPCell(new Phrase("Premium (Rs.)", normal1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            basicCover_table.addCell(cell);

            // 2 row
            cell = new PdfPCell(new Phrase(" Basic Cover", normal1));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            basicCover_table.addCell(cell);

            cell = new PdfPCell(new Phrase("" + policyTerm, normal1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            basicCover_table.addCell(cell);

            cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                    .parseDouble(obj.getStringWithout_E(Double
                            .valueOf(sumAssured)))), normal1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            basicCover_table.addCell(cell);

            cell = new PdfPCell(new Phrase(BasicPremium_PDF, normal1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            basicCover_table.addCell(cell);
            // Basic Cover


            PdfPTable FY_SY_premDetail_table = new PdfPTable(3);
            FY_SY_premDetail_table.setWidths(new float[]{5f, 5f, 5f});
            FY_SY_premDetail_table.setWidthPercentage(100f);
            FY_SY_premDetail_table.setHorizontalAlignment(Element.ALIGN_LEFT);

            // 1st row
            cell = new PdfPCell(new Phrase("",
                    small_bold));
            cell.setColspan(7);
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            FY_SY_premDetail_table.addCell(cell);

            // 2 row

            cell = new PdfPCell(new Phrase(premPayingMode + " Installment Premium",
                    small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            FY_SY_premDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase("Applicable Taxes", small_normal));
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

            cell = new PdfPCell(new Phrase(premPayingMode
                    + " Installment Premium with Applicable Taxes", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            FY_SY_premDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                    .parseDouble(BasicPremium)),
                    small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            FY_SY_premDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                    .parseDouble(ApplicableTax)),
                    small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            FY_SY_premDetail_table.addCell(cell);


            cell = new PdfPCell(new Phrase(
                    currencyFormat.format(Double.parseDouble(PremiumwithST)),
                    small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            FY_SY_premDetail_table.addCell(cell);

            Paragraph disclaimers = new Paragraph("     Disclaimers :-",
                    normal1_bold);
            disclaimers.setAlignment(Element.ALIGN_LEFT);

            PdfPTable table2 = new PdfPTable(2);
            table2.setWidths(new float[]{0.3f, 9f});
            table2.setWidthPercentage(100);
            table2.setHorizontalAlignment(Element.ALIGN_LEFT);

           /* // 1st note
            cell = new PdfPCell(new Phrase("*", normal1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table2.addCell(cell);
            cell = new PdfPCell(
                    new Phrase(
                            "The premium calculation is after large sum assured rebates, wherever applicable. ",
                            normal1));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT
                    | Element.ALIGN_JUSTIFIED);
            cell.setBorder(Rectangle.NO_BORDER);
            table2.addCell(cell);*/

            // 2 note
            cell = new PdfPCell(new Phrase("1", normal1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table2.addCell(cell);
            cell = new PdfPCell(
                    new Phrase(
                            "The premium calculation is after large sum assured discount, wherever applicable ",
                            normal1));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            table2.addCell(cell);

            // 3 note
            cell = new PdfPCell(new Phrase("2", normal1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table2.addCell(cell);
            cell = new PdfPCell(
                    new Phrase(
                            "For more details on risk factors, terms and conditions please read sales brochure carefully before concluding a sale. ",
                            normal1));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            table2.addCell(cell);
            // 1st note
         /*   cell = new PdfPCell(new Phrase("*", normal1));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table2.addCell(cell);
            cell = new PdfPCell(
                    new Phrase(
                            "The Premium calculation shown may change if there are changes in the levies like Applicable Taxes, education or any other cess or any other statutory levies required to be collected from the policyholder.",
                            normal1));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            table2.addCell(cell);
*/

            cell = new PdfPCell(new Phrase("3", normal1));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table2.addCell(cell);
            cell = new PdfPCell(
                    new Phrase(
                            "The exact premium can be determined only at the time of acceptance of risk cover on the life to be assured after taking into consideration any extras required to be imposed. ",
                            normal1));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT
                    | Element.ALIGN_JUSTIFIED);
            cell.setBorder(Rectangle.NO_BORDER);
            table2.addCell(cell);

            cell = new PdfPCell(new Phrase("4", normal1));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table2.addCell(cell);

            cell = new PdfPCell(
                    new Phrase(
                            "Tax benefits, are as per the Income Tax laws & are subject to change from time to time. Please consult your tax advisor for details.",
                            normal1));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT
                    | Element.ALIGN_JUSTIFIED);
            cell.setBorder(Rectangle.NO_BORDER);
            table2.addCell(cell);

            cell = new PdfPCell(new Phrase("5", normal1));
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

            document.add(new_line);

            // document.add(main_table);
            document.add(table_proposer_name);
            document.add(new_line);
            document.add(personalDetail_table);
            document.add(new_line);
            document.add(basicCover_table);
            document.add(new_line);
            document.add(FY_SY_premDetail_table);
            document.add(new_line);
            document.add(disclaimers);
            document.add(table2);
            document.add(new_line);
            document.add(new_line);

            // document.add(para7);
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
            document.add(new_line);
           /* PdfPTable BI_Pdftable_eSign = new PdfPTable(1);
            BI_Pdftable_eSign.setWidthPercentage(100);
            // CR_table6.setWidths(columnWidths_26);
            PdfPCell BI_Pdftable_eSign_cell1 = new PdfPCell(new Paragraph(

                    "This document is eSigned by " + name_of_person, small_bold));

            BI_Pdftable_eSign_cell1.setPadding(5);

            BI_Pdftable_eSign.addCell(BI_Pdftable_eSign_cell1);
            document.add(BI_Pdftable_eSign);
            document.add(new_line);
            document.add(new_line);*/

            PdfPTable agentDeclarationTable = new PdfPTable(1);
            agentDeclarationTable.setWidthPercentage(100);
            PdfPCell agentDeclaration = new PdfPCell(
                    new Paragraph("I, " + commonMethods.getUserName(context)
                            + " have explained the premiums and benefits under the product fully to the prospect/policyholder.",
                            small_bold));

            agentDeclaration.setPadding(5);
            agentDeclarationTable.addCell(agentDeclaration);
            document.add(agentDeclarationTable);

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

    }

    public void mobile_validation(String number) {
        boolean validationFlag3 = false;
        if ((number.length() != 10)) {
            edt_proposerdetail_basicdetail_contact_no
                    .setError("Please provide correct 10-digit mobile number");
            validationFlag3 = false;
        } else if ((number.length() == 10)) {
            validationFlag3 = true;
        }
    }

    public void email_id_validation(String email_id) {

        String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email_id);
        if (!(matcher.matches())) {
            edt_proposerdetail_basicdetail_Email_id
                    .setError("Please provide the correct email address");
            validationFla1 = false;
        } else if (!cb_staffdisc.isChecked()
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

    public void confirming_email_id(String email_id) {

        if (!(email_id.equals(ProposerEmailId))) {
            edt_proposerdetail_basicdetail_ConfirmEmail_id
                    .setError("Email id does not match");
        } else if ((email_id.equals(ProposerEmailId))) {
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

    public void setDefaultDate1(int id) {
        Calendar present_date = Calendar.getInstance();
        present_date.add(Calendar.DAY_OF_MONTH, +id);
        mDay = present_date.get(Calendar.DAY_OF_MONTH);
        mMonth = present_date.get(Calendar.MONTH);
        mYear = present_date.get(Calendar.YEAR);

        date = new StringBuilder().append(mDay).append("-").append(mMonth + 1)
                .append("-").append(mYear);
    }

    public void date_comparison() {

        setDefaultDate1(0);
        String current_date = date + "";
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        try {
            Date date1 = sdf.parse("08-08-2016");
            Date date2 = sdf.parse(current_date);

            System.out.println(sdf.format(date1));
            System.out.println(sdf.format(date2));
            if (date1.compareTo(date2) <= 0) {

                boolean product_update = true;

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }


    public String getDate_MM_DD_YYYY(String OldDate) {
        String NewDate = "";
        try {

            DateFormat userDateFormat = new SimpleDateFormat("dd/MM/yyyy");
            DateFormat dateFormatNeeded = new SimpleDateFormat("MM-dd-yyyy");
            Date date = userDateFormat.parse(OldDate);

            NewDate = dateFormatNeeded.format(date);

        } catch (Exception e) {
            // TODO: handle exception
            Log.e("Integrated Service", "Error in Getting Date");
        }

        return NewDate;
    }


    public void updateDegsignationSpinner() {
        if (str_occupation_type.equals("Army")) {
            String[] occ_rank_list = {"Select", "Lieutenant", "Captain", "Major, Lt.Col", "Colonel", "Brigadier",
                    "Maj. Gen. Lt. Gen.", "Sepoy", "Lance Naik", "Naik", "Hawaldar", "Naib Subhedar", "Subhedar", "Subhedar Major"};
            ArrayAdapter<String> occRankAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item, occ_rank_list);
            occRankAdapter.setDropDownViewResource(R.layout.spinner_item1);
            spnr_occupation_rank.setAdapter(occRankAdapter);
            occRankAdapter.notifyDataSetChanged();

            spnr_occupation_rank.setEnabled(true);
            spnr_occupation_rank.setClickable(true);
        } else if (str_occupation_type.equals("Air Force")) {
            String[] occ_rank_list = {"Select", "Pilot officer", "Flying officer", "Flight Lieutenant", "Suquadron Leader",
                    "Wing Commander", "Group Captain", "Air Commodore", "Air Vice Marshal", "Air Marshal", "Air Chief Marshal", "Aircraftsman",
                    "Coporal", "Sergeant", "Warrant officer"};
            ArrayAdapter<String> occRankAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item, occ_rank_list);
            occRankAdapter.setDropDownViewResource(R.layout.spinner_item1);
            spnr_occupation_rank.setAdapter(occRankAdapter);
            occRankAdapter.notifyDataSetChanged();

            spnr_occupation_rank.setEnabled(true);
            spnr_occupation_rank.setClickable(true);
        } else if (str_occupation_type.equals("Navy")) {
            String[] occ_rank_list = {"Select", "Sub Lieutenant", "Lieutenant", "Lieutenant Commander", "Commander", "Captain Commodore",
                    "Rear Admiral", "Vice Admiral, Admiral", "Master Chief Petty officer", "Chief Petty officer", "Petty officer", "Seaman"};
            ArrayAdapter<String> occRankAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item, occ_rank_list);
            occRankAdapter.setDropDownViewResource(R.layout.spinner_item1);
            spnr_occupation_rank.setAdapter(occRankAdapter);
            occRankAdapter.notifyDataSetChanged();

            spnr_occupation_rank.setEnabled(true);
            spnr_occupation_rank.setClickable(true);
        } else if (str_occupation_type.equals("RPF, CISF, Indian Home Guard, Defence Security Guards")
                || str_occupation_type.equals("BSF, CRPF, RAF, SSB, DSC, SFF, SAP")
                || str_occupation_type.equals("Coastal Guard")) {
            String[] occ_rank_list = {"Select", "Others"};
            ArrayAdapter<String> occRankAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item, occ_rank_list);
            occRankAdapter.setDropDownViewResource(R.layout.spinner_item1);
            spnr_occupation_rank.setAdapter(occRankAdapter);
            occRankAdapter.notifyDataSetChanged();

            spnr_occupation_rank.setEnabled(false);
            spnr_occupation_rank.setClickable(false);
        }
    }

    public void proceedCode() {

        Date();

        if (proposer_Is_Same_As_Life_Assured.equalsIgnoreCase("y")) {
            proposer_Title = "";
            proposer_First_Name = "";
            proposer_Middle_Name = "";
            proposer_Last_Name = "";
            name_of_proposer = "";
            proposer_date_of_birth = "";
        }

        addListenerOnSubmit();


        FLAG_OCCUPATION_EXTRA = "Y";
        String str_Reason_extra = "OCCUPATION_EXTRA";
        getInput(saralJeevanBimaBean);
        Dialog();


    }


    public boolean valPremiumPayingTerm() {


        String error = "";
        int PPT = Integer.parseInt(spnrPremPaymentTerm.getSelectedItem().toString());
        if (selPremFreq.getSelectedItem().toString().equals("LPPT")) {
            if (PPT == 5) {
                if (Integer.parseInt(spnrPolicyTerm.getSelectedItem()
                        .toString()) < 10) {
                    error = "Please select Policy term from 10 to 40";
                }

            } else if (PPT == 10) {
                if (Integer.parseInt(spnrPolicyTerm.getSelectedItem()
                        .toString()) < 15) {
                    error = "Please select Policy term from 15 to 40";
                }
            }
        }
        if (!error.equals("")) {
            commonMethods.showMessageDialog(context, error);
            return false;
        }
        return true;
    }

    // Validate Minimum premium
    public boolean valInstPremium(String premiumBasicWithoutDisc_ForSelFreq) {

        if (selPremFreq.getSelectedItem().toString()
                .equals("Single")
                && (Double.parseDouble(premiumBasicWithoutDisc_ForSelFreq) < 5480 || Double.parseDouble(premiumBasicWithoutDisc_ForSelFreq) > 415475)) {
            commonMethods.showMessageDialog(context, "Minimum premium for Single Mode is Rs. 5480 and maximum premium is Rs. 415475");

            return false;
        } else if (spnrPremPayingMode.getSelectedItem().toString().equals("Yearly") && !selPremFreq.getSelectedItem().toString()
                .equals("Single")
                && (Double.parseDouble(premiumBasicWithoutDisc_ForSelFreq) < 1415 || Double.parseDouble(premiumBasicWithoutDisc_ForSelFreq) > 101025)) {
            commonMethods.showMessageDialog(context, "Minimum premium for Yearly Mode is Rs. 1415 and maximum premium is Rs. 101025");

            return false;

        } else if (spnrPremPayingMode.getSelectedItem().toString().equals("Half Yearly") && !selPremFreq.getSelectedItem().toString()
                .equals("Single")
                && (Double.parseDouble(premiumBasicWithoutDisc_ForSelFreq) < 721.65 || Double.parseDouble(premiumBasicWithoutDisc_ForSelFreq) > 51522.75)) {
            commonMethods.showMessageDialog(context, "Minimum premium for Half Yearly Mode is Rs. 721.65 and maximum premium is Rs. 51522.75");

            return false;
        } else if (spnrPremPayingMode.getSelectedItem().toString()
                .equals("Monthly") && !selPremFreq.getSelectedItem().toString()
                .equals("Single")
                && (Double.parseDouble(premiumBasicWithoutDisc_ForSelFreq) < 120.28 || Double.parseDouble(premiumBasicWithoutDisc_ForSelFreq) > 8587.13)) {
            commonMethods.showMessageDialog(context, "Minimum premium for Monthly Mode is Rs. 120.28 and maximum premium is Rs. 8587.13");

            return false;
        } else {
            return true;
        }
    }


}
