package sbilife.com.pointofsale_bancaagency.smartincomeprotect;

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
import sbilife.com.pointofsale_bancaagency.needanalysis.NeedAnalysisActivity;
import sbilife.com.pointofsale_bancaagency.new_bussiness.ProductBIBean;
import sbilife.com.pointofsale_bancaagency.utility.GridHeight;

@SuppressWarnings("deprecation")
public class BI_SmartIncomeProtectActivity extends AppCompatActivity implements
        OnEditorActionListener {

    private NeedAnalysisBIService NABIObj;
    private NA_CBI_bean na_cbi_bean;
    private File needAnalysispath, newFile;
    private String agentcode, agentMobile, agentEmail, userType;
    private int needAnalysis_flag = 0;
    private String na_input = null;
    private String na_output = null;
    private String UIN_NO, lumpSumAmount = "N";
    private DatabaseHelper dbHelper;
    private CheckBox cb_staffdisc;
    private CheckBox cb_bi_smart_income_protect_JKResident;
    private CheckBox cb_bi_smart_income_protect_adb_rider;
    private CheckBox cb_bi_smart_income_protect_atpdb_rider;
    private CheckBox cb_bi_smart_income_protect_cc13nl_rider;
    private CheckBox cb_bi_smart_income_protect_pt_rider;

    private CheckBox cbLumpSumpAmount;

    private EditText edt_bi_smart_income_protect_life_assured_first_name,
            edt_bi_smart_income_protect_life_assured_middle_name,
            edt_bi_smart_income_protect_life_assured_last_name,
            edt_bi_smart_income_protect_life_assured_age,
            edt_smart_income_protect_contact_no,
            edt_smart_income_protect_Email_id,
            edt_smart_income_protect_ConfirmEmail_id,
            edt_bi_smart_income_protect_sum_assured_amount,
            edt_bi_smart_income_protect_pt_rider_sum_assured,
            edt_bi_smart_income_protect_adb_rider_sum_assured,
            edt_bi_smart_income_protect_atpbd_rider_sum_assured,
            edt_bi_smart_income_protect_cc13nl_rider_sum_assured,
            edt_bi_smart_income_protect_proposer_last_name,
            edt_bi_smart_income_protect_proposer_middle_name,
            edt_bi_smart_income_protect_proposer_first_name;

    private Spinner spnr_bi_smart_income_protect_life_assured_title,
            spnr_bi_smart_income_protect_selGender, spinnerProposerGender,
    // spnr_bi_smart_income_protect_plan,
    spnr_bi_smart_income_protect_policyterm,
            spnr_bi_smart_income_protect_premium_paying_mode,
            spnr_bi_smart_income_protect_pt_rider_term,
            spnr_bi_smart_income_protect_adb_rider_term,
            spnr_bi_smart_income_protect_atpdb_rider_term,
            spnr_bi_smart_income_protect_cc13nl_rider_term,
            spnr_bi_smart_income_protect_proposer_title;

    private TableRow tr_PT_rider, tr_ADB_rider, tr_ATPDB_Rider,
            tr_CC13NL_Rider;
    private LinearLayout linearlayoutProposerDetails;
    private TextView textviewProposerAge;
    private TableRow tr_bi_smart_income_protect_pt_rider_checkbox,
            tr_bi_smart_income_protect_adb_rider_checkbox,
            tr_bi_smart_income_protect_atpd_rider_checkbox,
            tr_bi_smart_income_protect_cc13nl_rider_checkbox;
    private Button btn_bi_smart_income_protect_life_assured_date,
            btn_bi_smart_income_protect_proposer_date;
    // btn_plandetail_loan_1st_emi_date,
    // btn_plandetail_loan_last_emi_date;
    private Button btnBack, btnSubmit;

    private Button btn_MarketingOfficalDate;
    private Button btn_PolicyholderDate;
    private ImageButton Ibtn_signatureofMarketing,
            Ibtn_signatureofPolicyHolders;

    // newDBHelper db;
    private ParseXML prsObj;

    private String QuatationNumber = "";
    // private String currentRecordId = "";
    private String planName = "";
    // private String sr_Code = "";

    private String lifeAssured_Title = "";
    private String lifeAssured_First_Name = "";
    private String lifeAssured_Middle_Name = "";
    private String lifeAssured_Last_Name = "";
    private String name_of_life_assured = "";
    private String lifeAssured_date_of_birth = "";
    private String lifeAssuredAge = "";

    private String proposer_Title = "", proposer_First_Name = "",
            proposer_Middle_Name = "", proposer_Last_Name = "",
            proposer_Is_Same_As_Life_Assured = "Y", name_of_proposer = "",
            proposer_date_of_birth = "", emailId = "", mobileNo = "",
            ConfirmEmailId = "";// product_name = "",

    private StringBuilder retVal;
    private StringBuilder inputVal;
    private AlertDialog.Builder showAlert;
    private Dialog d;

    private boolean validationFla1 = false;
    private DecimalFormat currencyFormat;
    private DecimalFormat decimalCurrencyFormat;
    private SmartIncomeProtectBean smartIncomeProtectBean;
    private SmartIncomeProtectProperties prop = null;
    private StringBuilder bussIll = null;
    private CommonForAllProd commonForAllProd = null;
    private double premPT = 0;
    private double premADB = 0;
    private double premATPDB = 0;
    private double premCC13NonLinked = 0;
    private CommonForAllProd obj;
    private List<M_BI_SmartIncomeProtect_AdapterCommon> list_data;

    // For Bi Dialog
    private String name_of_person = "";
    private String place2 = "";
    private String date1 = "";
    private String date2 = "";
    private String agent_sign = "";
    private String proposer_sign = "";
    private String proposer_Backdating_WishToBackDate_Policy = "";
    private String proposer_Backdating_BackDate = "";

    private Bitmap photoBitmap;
    private String propsoser_gender = "";
    private String latestImage = "";
    private final int SIGNATURE_ACTIVITY = 1;
    private final int DATE_DIALOG_ID = 1;
    private int DIALOG_ID;
    private int mYear;
    private int mMonth;
    private int mDay;
    private String ProposerAge = "";

    private String output = "";
    private String age_entry = "";
    private String gender = "";
    private String premium_paying_frequency = "", sum_assured_on_death;
    private String policy_term = "";
    private String sum_assured = "";
    private String adb_rider_status = "";
    private String atpdb_rider_status = "";
    private String ptr_rider_status = "";
    private String cc13nl_rider_status = "";
    private String term_adb = "";
    private String sa_adb = "";
    private String prem_adb = "";
    private String term_atpdb = "";
    private String sa_atpdb = "";
    private String prem_atpdb = "";
    private String term_pta = "";
    private String sa_pta = "";
    private String prem_pta = "";
    private String basicprem = "";
    private String term_cc13nl = "";
    private String sa_pta_cc13nl = "";
    private String prem_pta_cc13nl = "";
    private String servcTax = "";
    private String premWthST = "";

    private int age;
    private String BackdatingInt;
    private String Premium_pdf;
    private String Today;
    private File mypath;

    private RadioButton rb_smart_income_protect_backdating_yes;
    private RadioButton rb_smart_income_protect_backdating_no;
    private LinearLayout ll_smart_income_protect_backdating;
    private Button btn_smart_income_protect_backdatingdate;

    /*** Added by Akshaya on 04-AUG-15 start ***/
    private int minAge = 0;
    private int maxAge = 0;
    private LinearLayout ll_bi_smart_income_protect_main;

    /*** Added by Akshaya on 04-AUG-15 end ***/
    private int flg_focus = 0;

    private String bankUserType = "", mode = "";
    /* parivartan changes */
    private String product_Code, product_UIN, product_cateogory, product_type;
    private String Check = "";
    private CommonMethods commonMethods;
    private StorageUtils mStorageUtils;
    private Context context;
    private ImageButton imageButtonSmartIncomeProtectProposerPhotograph;

    private String Company_policy_surrender_dec = "";
    private Double AnnulizedPremium = 0.0;
    /* end */
    private CheckBox cb_kerladisc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.bi_smart_income_protectmain);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);

        dbHelper = new DatabaseHelper(getApplicationContext());
        context = this;
        commonMethods = new CommonMethods();
        mStorageUtils = new StorageUtils();

        commonMethods.setApplicationToolbarMenu(this, getString(R.string.app_name));

        NABIObj = new NeedAnalysisBIService(this);
        // objBIPdfMail=new BIPdfMail();
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
                // na_dob=intent.getStringExtra("custDOB");
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
                    planName = "Smart Income Protect";
                    product_Code = prodInfoObj.getProductCode(planName);
                    product_UIN = prodInfoObj.getProductUIN(planName);
                    product_cateogory = prodInfoObj
                            .getProductCategory(planName);
                    product_type = prodInfoObj.getProductType(planName);
                    /* end */
                    // sr_Code = agentcode;
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
                        product_Code/* "1B" */, agentcode, zero + "");

            }
        } else
            needAnalysis_flag = 0;
        // ProductHomePageActivity.path.setText("Benefit Illustrator");

        UI_Declaration();

        prsObj = new ParseXML();
        setSpinner_Value();
        // setBIInputGui();

        edt_bi_smart_income_protect_life_assured_first_name
                .setOnEditorActionListener(this);
        edt_bi_smart_income_protect_life_assured_middle_name
                .setOnEditorActionListener(this);
        edt_bi_smart_income_protect_life_assured_last_name
                .setOnEditorActionListener(this);
        edt_bi_smart_income_protect_proposer_first_name
                .setOnEditorActionListener(this);
        edt_bi_smart_income_protect_proposer_middle_name
                .setOnEditorActionListener(this);
        edt_bi_smart_income_protect_proposer_last_name
                .setOnEditorActionListener(this);
        edt_bi_smart_income_protect_sum_assured_amount
                .setOnEditorActionListener(this);
        edt_smart_income_protect_contact_no.setOnEditorActionListener(this);
        edt_smart_income_protect_Email_id.setOnEditorActionListener(this);
        edt_smart_income_protect_ConfirmEmail_id
                .setOnEditorActionListener(this);
        edt_bi_smart_income_protect_pt_rider_sum_assured
                .setOnEditorActionListener(this);
        edt_bi_smart_income_protect_adb_rider_sum_assured
                .setOnEditorActionListener(this);
        edt_bi_smart_income_protect_atpbd_rider_sum_assured
                .setOnEditorActionListener(this);
        edt_bi_smart_income_protect_cc13nl_rider_sum_assured
                .setOnEditorActionListener(this);

        setFocusable(spnr_bi_smart_income_protect_proposer_title);
        spnr_bi_smart_income_protect_proposer_title.requestFocus();

        prop = new SmartIncomeProtectProperties();
        commonForAllProd = new CommonForAllProd();
        obj = new CommonForAllProd();
        list_data = new ArrayList<>();
        validationOfMoile_EmailId();
        setSpinnerAndOtherListner();

        showAlert = new AlertDialog.Builder(this);
        smartIncomeProtectBean = new SmartIncomeProtectBean();
        currencyFormat = new DecimalFormat("##,##,##,###");
        decimalCurrencyFormat = new DecimalFormat("##,##,##,###.##");

    }

    private void UI_Declaration() {

        cb_staffdisc = findViewById(R.id.cb_staffdisc);
        cb_bi_smart_income_protect_JKResident = findViewById(R.id.cb_bi_smart_income_protect_JKResident);
        cb_bi_smart_income_protect_pt_rider = findViewById(R.id.cb_bi_smart_income_protect_pt_rider);
        cb_bi_smart_income_protect_adb_rider = findViewById(R.id.cb_bi_smart_income_protect_adb_rider);
        cb_bi_smart_income_protect_atpdb_rider = findViewById(R.id.cb_bi_smart_income_protect_atpdb_rider);
        cb_bi_smart_income_protect_cc13nl_rider = findViewById(R.id.cb_bi_smart_income_protect_cc13nl_rider);

        edt_bi_smart_income_protect_life_assured_first_name = findViewById(R.id.edt_bi_smart_income_protect_life_assured_first_name);
        edt_bi_smart_income_protect_life_assured_middle_name = findViewById(R.id.edt_bi_smart_income_protect_life_assured_middle_name);
        edt_bi_smart_income_protect_life_assured_last_name = findViewById(R.id.edt_bi_smart_income_protect_life_assured_last_name);
        edt_bi_smart_income_protect_life_assured_age = findViewById(R.id.edt_bi_smart_income_protect_life_assured_age);
        edt_smart_income_protect_contact_no = findViewById(R.id.edt_smart_income_protect_contact_no);
        edt_smart_income_protect_Email_id = findViewById(R.id.edt_smart_income_protect_Email_id);
        edt_smart_income_protect_ConfirmEmail_id = findViewById(R.id.edt_smart_income_protect_ConfirmEmail_id);
        edt_bi_smart_income_protect_sum_assured_amount = findViewById(R.id.edt_bi_smart_income_protect_sum_assured_amount);
        edt_bi_smart_income_protect_pt_rider_sum_assured = findViewById(R.id.edt_bi_smart_income_protect_pt_rider_sum_assured);
        edt_bi_smart_income_protect_adb_rider_sum_assured = findViewById(R.id.edt_bi_smart_income_protect_adb_rider_sum_assured);
        edt_bi_smart_income_protect_atpbd_rider_sum_assured = findViewById(R.id.edt_bi_smart_income_protect_atpbd_rider_sum_assured);
        edt_bi_smart_income_protect_cc13nl_rider_sum_assured = findViewById(R.id.edt_bi_smart_income_protect_cc13nl_rider_sum_assured);

        spnr_bi_smart_income_protect_life_assured_title = findViewById(R.id.spnr_bi_smart_income_protect_life_assured_title);
        spnr_bi_smart_income_protect_selGender = findViewById(R.id.spnr_bi_smart_income_protect_selGender);
        spinnerProposerGender = findViewById(R.id.spinnerProposerGender);

        spnr_bi_smart_income_protect_policyterm = findViewById(R.id.spnr_bi_smart_income_protect_policyterm);
        spnr_bi_smart_income_protect_premium_paying_mode = findViewById(R.id.spnr_bi_smart_income_protect_premium_paying_mode);
        spnr_bi_smart_income_protect_pt_rider_term = findViewById(R.id.spnr_bi_smart_income_protect_pt_rider_term);
        spnr_bi_smart_income_protect_adb_rider_term = findViewById(R.id.spnr_bi_smart_income_protect_adb_rider_term);
        spnr_bi_smart_income_protect_atpdb_rider_term = findViewById(R.id.spnr_bi_smart_income_protect_atpdb_rider_term);
        spnr_bi_smart_income_protect_cc13nl_rider_term = findViewById(R.id.spnr_bi_smart_income_protect_cc13nl_rider_term);

        tr_PT_rider = findViewById(R.id.tr_bi_smart_income_protect_pt_rider);
        tr_ADB_rider = findViewById(R.id.tr_bi_smart_income_protect_adb_rider);
        tr_ATPDB_Rider = findViewById(R.id.tr_bi_smart_income_protect_atpd_rider);
        tr_CC13NL_Rider = findViewById(R.id.tr_bi_smart_income_protect_cc13nl_rider);

        btn_bi_smart_income_protect_life_assured_date = findViewById(R.id.btn_bi_smart_income_protect_life_assured_date);
        btn_smart_income_protect_backdatingdate = findViewById(R.id.btn_smart_income_protect_backdatingdate);
        rb_smart_income_protect_backdating_yes = findViewById(R.id.rb_smart_income_protect_backdating_yes);
        rb_smart_income_protect_backdating_no = findViewById(R.id.rb_smart_income_protect_backdating_no);
        ll_smart_income_protect_backdating = findViewById(R.id.ll_smart_income_protect_backdating);

        btnBack = findViewById(R.id.btn_bi_smart_income_protect_btnback);
        btnSubmit = findViewById(R.id.btn_bi_smart_income_protect_btnSubmit);

        // Proposer details

        edt_bi_smart_income_protect_proposer_last_name = findViewById(R.id.edt_bi_smart_income_protect_proposer_last_name);
        edt_bi_smart_income_protect_proposer_middle_name = findViewById(R.id.edt_bi_smart_income_protect_proposer_middle_name);
        edt_bi_smart_income_protect_proposer_first_name = findViewById(R.id.edt_bi_smart_income_protect_proposer_first_name);
        spnr_bi_smart_income_protect_proposer_title = findViewById(R.id.spnr_bi_smart_income_protect_proposer_title);
        linearlayoutProposerDetails = findViewById(R.id.linearlayoutProposerDetails);
        textviewProposerAge = findViewById(R.id.textviewProposerAge);
        textviewProposerAge.setClickable(false);

        tr_bi_smart_income_protect_pt_rider_checkbox = findViewById(R.id.tr_bi_smart_income_protect_pt_rider_checkbox);
        tr_bi_smart_income_protect_adb_rider_checkbox = findViewById(R.id.tr_bi_smart_income_protect_adb_rider_checkbox);
        tr_bi_smart_income_protect_atpd_rider_checkbox = findViewById(R.id.tr_bi_smart_income_protect_atpd_rider_checkbox);
        tr_bi_smart_income_protect_cc13nl_rider_checkbox = findViewById(R.id.tr_bi_smart_income_protect_cc13nl_rider_checkbox);

        btn_bi_smart_income_protect_proposer_date = findViewById(R.id.btn_bi_smart_income_protect_proposer_date);
        ll_bi_smart_income_protect_main = findViewById(R.id.ll_bi_smart_income_protect_main);

        cbLumpSumpAmount = findViewById(R.id.cbLumpSumpAmount);
        cbLumpSumpAmount.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cbLumpSumpAmount.isChecked()) {
                    lumpSumAmount = "Y";
                } else {
                    lumpSumAmount = "N";
                }

            }
        });
    }

    private void setSpinner_Value() {

        // Gender

        ArrayAdapter<String> genderAdapter = new ArrayAdapter<>(
                getApplicationContext(), R.layout.spinner_item,
                getResources().getStringArray(R.array.gender_all_arrays));
        genderAdapter.setDropDownViewResource(R.layout.spinner_item1);
        spnr_bi_smart_income_protect_selGender.setAdapter(genderAdapter);
        genderAdapter.notifyDataSetChanged();

        spinnerProposerGender.setAdapter(genderAdapter);
        genderAdapter.notifyDataSetChanged();

        commonMethods.fillSpinnerValue(context, spnr_bi_smart_income_protect_life_assured_title,
                Arrays.asList(getResources().getStringArray(R.array.arr_select_title)));
        commonMethods.fillSpinnerValue(context, spnr_bi_smart_income_protect_proposer_title,
                Arrays.asList(getResources().getStringArray(R.array.arr_select_title)));

        // Policy Term
        String[] policyTermList = {"7", "12", "15"};

        ArrayAdapter<String> policyTermAdapter = new ArrayAdapter<>(
                getApplicationContext(), R.layout.spinner_item, policyTermList);
        policyTermAdapter.setDropDownViewResource(R.layout.spinner_item1);
        spnr_bi_smart_income_protect_policyterm.setAdapter(policyTermAdapter);
        policyTermAdapter.notifyDataSetChanged();

        // Premium Frequency

        String[] premFreqList = {"Monthly", "Quarterly", "Half Yearly",
                "Yearly"};
        ArrayAdapter<String> premFreqAdapter = new ArrayAdapter<>(
                getApplicationContext(), R.layout.spinner_item, premFreqList);
        premFreqAdapter.setDropDownViewResource(R.layout.spinner_item1);
        spnr_bi_smart_income_protect_premium_paying_mode
                .setAdapter(premFreqAdapter);
        premFreqAdapter.notifyDataSetChanged();

        // preferred Term
        String[] ptTermList = {"7", "12", "15"};
        ArrayAdapter<String> ptTermAdapter = new ArrayAdapter<>(
                getApplicationContext(), R.layout.spinner_item, ptTermList);
        ptTermAdapter.setDropDownViewResource(R.layout.spinner_item1);
        spnr_bi_smart_income_protect_pt_rider_term.setAdapter(ptTermAdapter);
        ptTermAdapter.notifyDataSetChanged();

        // adb Term
        String[] adbTermList = {"7", "12", "15"};
        ArrayAdapter<String> adbTermAdapter = new ArrayAdapter<>(
                getApplicationContext(), R.layout.spinner_item, adbTermList);
        adbTermAdapter.setDropDownViewResource(R.layout.spinner_item1);
        spnr_bi_smart_income_protect_adb_rider_term.setAdapter(adbTermAdapter);
        adbTermAdapter.notifyDataSetChanged();

        // atpdb Term
        String[] atpdbTermList = {"7", "12", "15"};
        ArrayAdapter<String> atpdbTermAdapter = new ArrayAdapter<>(
                getApplicationContext(), R.layout.spinner_item, atpdbTermList);
        atpdbTermAdapter.setDropDownViewResource(R.layout.spinner_item1);
        spnr_bi_smart_income_protect_atpdb_rider_term
                .setAdapter(atpdbTermAdapter);
        atpdbTermAdapter.notifyDataSetChanged();

        // Criti Care Term
        String[] cc13nlTermList = {"7", "12", "15"};
        ArrayAdapter<String> cc13nlTermAdapter = new ArrayAdapter<>(
                getApplicationContext(), R.layout.spinner_item, cc13nlTermList);
        cc13nlTermAdapter.setDropDownViewResource(R.layout.spinner_item1);
        spnr_bi_smart_income_protect_cc13nl_rider_term
                .setAdapter(cc13nlTermAdapter);
        cc13nlTermAdapter.notifyDataSetChanged();

    }

    private void setSpinnerAndOtherListner() {

        cb_staffdisc.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (cb_staffdisc.isChecked()) {
                    cb_staffdisc.setChecked(true);
                }
                clearFocusable(cb_staffdisc);
                clearFocusable(spnr_bi_smart_income_protect_life_assured_title);
                setFocusable(spnr_bi_smart_income_protect_life_assured_title);
                spnr_bi_smart_income_protect_life_assured_title.requestFocus();

            }
        });

        cb_bi_smart_income_protect_JKResident
                .setOnClickListener(new OnClickListener() {

                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        if (cb_bi_smart_income_protect_JKResident.isChecked()) {
                            cb_bi_smart_income_protect_JKResident
                                    .setChecked(true);
                        } else {
                            cb_bi_smart_income_protect_JKResident
                                    .setChecked(false);
                        }
                    }
                });

        // Spinner

        spnr_bi_smart_income_protect_proposer_title
                .setOnItemSelectedListener(new OnItemSelectedListener() {

                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int position, long id) {
                        // TODO Auto-generated method stub
                        if (position == 0) {
                            proposer_Title = "";
                        } else {
                            proposer_Title = spnr_bi_smart_income_protect_proposer_title
                                    .getSelectedItem().toString();

                            if (proposer_Title.equalsIgnoreCase("Mr.")) {
                                // selGender.setSelection(getIndex(selGender,
                                // "Male"));
                            } else if (proposer_Title.equalsIgnoreCase("Ms.")
                                    || proposer_Title.equalsIgnoreCase("Mrs.")) {
                                // selGender.setSelection(getIndex(selGender,
                                // "Female"));
                            }
                            setFocusable(edt_bi_smart_income_protect_proposer_first_name);

                            edt_bi_smart_income_protect_proposer_first_name
                                    .requestFocus();

                        }

                    }

                    public void onNothingSelected(AdapterView<?> arg0) {
                        // TODO Auto-generated method stub

                    }
                });

        spnr_bi_smart_income_protect_life_assured_title
                .setOnItemSelectedListener(new OnItemSelectedListener() {

                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int position, long id) {
                        // TODO Auto-generated method stub
                        if (position == 0) {
                            lifeAssured_Title = "";
                        } else {
                            lifeAssured_Title = spnr_bi_smart_income_protect_life_assured_title
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
                            clearFocusable(spnr_bi_smart_income_protect_life_assured_title);
                            setFocusable(edt_bi_smart_income_protect_life_assured_first_name);
                            edt_bi_smart_income_protect_life_assured_first_name
                                    .requestFocus();
                        }
                    }

                    public void onNothingSelected(AdapterView<?> arg0) {
                        // TODO Auto-generated method stub

                    }
                });

        spnr_bi_smart_income_protect_policyterm
                .setOnItemSelectedListener(new OnItemSelectedListener() {

                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int pos, long id) {

                        if (pos == 0) {
                            spnr_bi_smart_income_protect_pt_rider_term
                                    .setSelection(
                                            getIndex(
                                                    spnr_bi_smart_income_protect_pt_rider_term,
                                                    "7"), false);
                            spnr_bi_smart_income_protect_pt_rider_term
                                    .setEnabled(false);
                            spnr_bi_smart_income_protect_adb_rider_term
                                    .setSelection(
                                            getIndex(
                                                    spnr_bi_smart_income_protect_pt_rider_term,
                                                    "7"), false);
                            spnr_bi_smart_income_protect_adb_rider_term
                                    .setEnabled(false);
                            spnr_bi_smart_income_protect_atpdb_rider_term
                                    .setSelection(
                                            getIndex(
                                                    spnr_bi_smart_income_protect_pt_rider_term,
                                                    "7"), false);
                            spnr_bi_smart_income_protect_atpdb_rider_term
                                    .setEnabled(false);
                            spnr_bi_smart_income_protect_cc13nl_rider_term
                                    .setSelection(
                                            getIndex(
                                                    spnr_bi_smart_income_protect_pt_rider_term,
                                                    "7"), false);
                            spnr_bi_smart_income_protect_cc13nl_rider_term
                                    .setEnabled(false);

                        } else if (pos == 1) {
                            spnr_bi_smart_income_protect_pt_rider_term
                                    .setSelection(
                                            getIndex(
                                                    spnr_bi_smart_income_protect_pt_rider_term,
                                                    "12"), false);
                            spnr_bi_smart_income_protect_pt_rider_term
                                    .setEnabled(false);
                            spnr_bi_smart_income_protect_adb_rider_term
                                    .setSelection(
                                            getIndex(
                                                    spnr_bi_smart_income_protect_pt_rider_term,
                                                    "12"), false);
                            spnr_bi_smart_income_protect_adb_rider_term
                                    .setEnabled(false);
                            spnr_bi_smart_income_protect_atpdb_rider_term
                                    .setSelection(
                                            getIndex(
                                                    spnr_bi_smart_income_protect_pt_rider_term,
                                                    "12"), false);
                            spnr_bi_smart_income_protect_atpdb_rider_term
                                    .setEnabled(false);
                            spnr_bi_smart_income_protect_cc13nl_rider_term
                                    .setSelection(
                                            getIndex(
                                                    spnr_bi_smart_income_protect_pt_rider_term,
                                                    "12"), false);
                            spnr_bi_smart_income_protect_cc13nl_rider_term
                                    .setEnabled(false);
                        } else if (pos == 2) {
                            spnr_bi_smart_income_protect_pt_rider_term
                                    .setSelection(
                                            getIndex(
                                                    spnr_bi_smart_income_protect_pt_rider_term,
                                                    "15"), false);
                            spnr_bi_smart_income_protect_pt_rider_term
                                    .setEnabled(false);
                            spnr_bi_smart_income_protect_adb_rider_term
                                    .setSelection(
                                            getIndex(
                                                    spnr_bi_smart_income_protect_pt_rider_term,
                                                    "15"), false);
                            spnr_bi_smart_income_protect_adb_rider_term
                                    .setEnabled(false);
                            spnr_bi_smart_income_protect_atpdb_rider_term
                                    .setSelection(
                                            getIndex(
                                                    spnr_bi_smart_income_protect_pt_rider_term,
                                                    "15"), false);
                            spnr_bi_smart_income_protect_atpdb_rider_term
                                    .setEnabled(false);
                            spnr_bi_smart_income_protect_cc13nl_rider_term
                                    .setSelection(
                                            getIndex(
                                                    spnr_bi_smart_income_protect_pt_rider_term,
                                                    "15"), false);
                            spnr_bi_smart_income_protect_cc13nl_rider_term
                                    .setEnabled(false);
                        }

                        spnr_bi_smart_income_protect_premium_paying_mode
                                .setEnabled(true);
                        spnr_bi_smart_income_protect_premium_paying_mode
                                .setSelection(3, false);
                        if (!(lifeAssured_date_of_birth.equals(""))) {
                            valTermRider();
                        }

                        if (flg_focus == 1) {
                            clearFocusable(spnr_bi_smart_income_protect_policyterm);
                            setFocusable(spnr_bi_smart_income_protect_premium_paying_mode);
                            spnr_bi_smart_income_protect_premium_paying_mode
                                    .requestFocus();
                        }
                    }

                    public void onNothingSelected(AdapterView<?> arg0) {
                        // TODO Auto-generated method stub

                    }
                });

        spnr_bi_smart_income_protect_premium_paying_mode
                .setOnItemSelectedListener(new OnItemSelectedListener() {

                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int pos, long id) {
                        if (flg_focus == 1) {
                            clearFocusable(spnr_bi_smart_income_protect_premium_paying_mode);
                            setFocusable(edt_bi_smart_income_protect_sum_assured_amount);
                            edt_bi_smart_income_protect_sum_assured_amount
                                    .requestFocus();
                        }
                    }

                    public void onNothingSelected(AdapterView<?> arg0) {
                        // TODO Auto-generated method stub

                    }
                });

        // PT Term
        spnr_bi_smart_income_protect_pt_rider_term
                .setOnItemSelectedListener(new OnItemSelectedListener() {

                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int pos, long id) {
                        if (valTermRider()) {
                            clearFocusable(spnr_bi_smart_income_protect_pt_rider_term);
                            setFocusable(edt_bi_smart_income_protect_pt_rider_sum_assured);
                            edt_bi_smart_income_protect_pt_rider_sum_assured
                                    .requestFocus();
                        }
                    }

                    public void onNothingSelected(AdapterView<?> arg0) {
                        // TODO Auto-generated method stub

                    }
                });

        // ADB Term
        spnr_bi_smart_income_protect_adb_rider_term
                .setOnItemSelectedListener(new OnItemSelectedListener() {

                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int pos, long id) {
                        if (valTermRider()) {
                            clearFocusable(spnr_bi_smart_income_protect_adb_rider_term);
                            setFocusable(edt_bi_smart_income_protect_adb_rider_sum_assured);
                            edt_bi_smart_income_protect_adb_rider_sum_assured
                                    .requestFocus();
                        }
                    }

                    public void onNothingSelected(AdapterView<?> arg0) {
                        // TODO Auto-generated method stub

                    }
                });

        // ATPDB Term
        spnr_bi_smart_income_protect_atpdb_rider_term
                .setOnItemSelectedListener(new OnItemSelectedListener() {

                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int pos, long id) {
                        if (valTermRider()) {
                            clearFocusable(spnr_bi_smart_income_protect_atpdb_rider_term);
                            setFocusable(edt_bi_smart_income_protect_atpbd_rider_sum_assured);
                            edt_bi_smart_income_protect_atpbd_rider_sum_assured
                                    .requestFocus();
                        }
                    }

                    public void onNothingSelected(AdapterView<?> arg0) {
                        // TODO Auto-generated method stub

                    }
                });

        // Criti Case Term
        spnr_bi_smart_income_protect_cc13nl_rider_term
                .setOnItemSelectedListener(new OnItemSelectedListener() {

                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int pos, long id) {
                        if (valTermRider()) {
                            clearFocusable(spnr_bi_smart_income_protect_cc13nl_rider_term);
                            setFocusable(edt_bi_smart_income_protect_cc13nl_rider_sum_assured);
                            edt_bi_smart_income_protect_cc13nl_rider_sum_assured
                                    .requestFocus();
                        }
                    }

                    public void onNothingSelected(AdapterView<?> arg0) {
                        // TODO Auto-generated method stub

                    }
                });

        // PT Rider
        // cb_bi_smart_income_protect_pt_rider
        // .setOnCheckedChangeListener(new
        // CompoundButton.OnCheckedChangeListener() {
        // public void onCheckedChanged(CompoundButton buttonView,
        // boolean isChecked) {
        // if (isChecked == true) {
        // tr_PT_rider.setVisibility(View.VISIBLE);
        // spnr_bi_smart_income_protect_pt_rider_term
        // .setSelection(getIndex(spnr_bi_smart_income_protect_pt_rider_term,spnr_bi_smart_income_protect_policyterm
        // .getSelectedItem().toString()), false);
        // spnr_bi_smart_income_protect_pt_rider_term
        // .setEnabled(false);
        // clearFocusable(cb_bi_smart_income_protect_pt_rider);
        // clearFocusable(spnr_bi_smart_income_protect_pt_rider_term);
        // setFocusable(spnr_bi_smart_income_protect_pt_rider_term);
        // spnr_bi_smart_income_protect_pt_rider_term
        // .requestFocus();
        // } else {
        //
        // tr_PT_rider.setVisibility(View.GONE);
        // }
        // }
        // });

        cb_bi_smart_income_protect_pt_rider
                .setOnClickListener(new OnClickListener() {

                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        if (cb_bi_smart_income_protect_pt_rider.isChecked()) {
                            cb_bi_smart_income_protect_pt_rider
                                    .setChecked(true);
                            tr_PT_rider.setVisibility(View.VISIBLE);
                            spnr_bi_smart_income_protect_pt_rider_term
                                    .setEnabled(false);
                            clearFocusable(cb_bi_smart_income_protect_pt_rider);
                            clearFocusable(edt_bi_smart_income_protect_pt_rider_sum_assured);
                            setFocusable(edt_bi_smart_income_protect_pt_rider_sum_assured);
                            edt_bi_smart_income_protect_pt_rider_sum_assured
                                    .requestFocus();
                        } else {
                            tr_PT_rider.setVisibility(View.GONE);
                        }

                    }
                });

        // ADB Rider

        cb_bi_smart_income_protect_adb_rider
                .setOnClickListener(new OnClickListener() {

                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        if (cb_bi_smart_income_protect_adb_rider.isChecked()) {
                            cb_bi_smart_income_protect_adb_rider
                                    .setChecked(true);
                            tr_ADB_rider.setVisibility(View.VISIBLE);
                            spnr_bi_smart_income_protect_adb_rider_term
                                    .setEnabled(false);
                            clearFocusable(cb_bi_smart_income_protect_adb_rider);
                            clearFocusable(edt_bi_smart_income_protect_adb_rider_sum_assured);
                            setFocusable(edt_bi_smart_income_protect_adb_rider_sum_assured);
                            edt_bi_smart_income_protect_adb_rider_sum_assured
                                    .requestFocus();
                        } else {
                            tr_ADB_rider.setVisibility(View.GONE);
                        }

                    }
                });

        // ATPDB Rider
        cb_bi_smart_income_protect_atpdb_rider
                .setOnClickListener(new OnClickListener() {

                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        if (cb_bi_smart_income_protect_atpdb_rider.isChecked()) {
                            cb_bi_smart_income_protect_atpdb_rider
                                    .setChecked(true);
                            tr_ATPDB_Rider.setVisibility(View.VISIBLE);
                            spnr_bi_smart_income_protect_atpdb_rider_term
                                    .setEnabled(false);
                            clearFocusable(cb_bi_smart_income_protect_atpdb_rider);
                            clearFocusable(edt_bi_smart_income_protect_atpbd_rider_sum_assured);
                            setFocusable(edt_bi_smart_income_protect_atpbd_rider_sum_assured);
                            edt_bi_smart_income_protect_atpbd_rider_sum_assured
                                    .requestFocus();
                        } else {
                            tr_ATPDB_Rider.setVisibility(View.GONE);
                        }

                    }
                });

        // Criti Care 13 Non-Linked
        cb_bi_smart_income_protect_cc13nl_rider
                .setOnClickListener(new OnClickListener() {

                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        if (cb_bi_smart_income_protect_cc13nl_rider.isChecked()) {
                            cb_bi_smart_income_protect_cc13nl_rider
                                    .setChecked(true);
                            tr_CC13NL_Rider.setVisibility(View.VISIBLE);
                            spnr_bi_smart_income_protect_cc13nl_rider_term
                                    .setEnabled(false);
                            clearFocusable(cb_bi_smart_income_protect_cc13nl_rider);
                            clearFocusable(edt_bi_smart_income_protect_cc13nl_rider_sum_assured);
                            setFocusable(edt_bi_smart_income_protect_cc13nl_rider_sum_assured);
                            edt_bi_smart_income_protect_cc13nl_rider_sum_assured
                                    .requestFocus();
                        } else {
                            tr_CC13NL_Rider.setVisibility(View.GONE);
                        }

                    }
                });

        rb_smart_income_protect_backdating_yes
                .setOnCheckedChangeListener(new OnCheckedChangeListener() {

                    public void onCheckedChanged(CompoundButton buttonView,
                                                 boolean isChecked) {
                        // TODO Auto-generated method stub
                        if (isChecked) {
                            proposer_Backdating_WishToBackDate_Policy = "Y";
                            ll_smart_income_protect_backdating
                                    .setVisibility(View.VISIBLE);
                            btn_smart_income_protect_backdatingdate
                                    .setText("Select Date");
                            proposer_Backdating_BackDate = "";

                        }
                    }
                });

        rb_smart_income_protect_backdating_no
                .setOnCheckedChangeListener(new OnCheckedChangeListener() {

                    public void onCheckedChanged(CompoundButton buttonView,
                                                 boolean isChecked) {
                        // TODO Auto-generated method stub
                        if (isChecked) {
                            // List<M_Benefit_Illustration_Detail> data = db
                            // .getBIDetail(QuatationNumber);
                            // if (data.size() > 0) {
                            // proposer_Backdating_WishToBackDate_Policy = data
                            // .get(0).getWish_to_backdate_policy();
                            // }
                            if (proposer_Backdating_WishToBackDate_Policy
                                    .equalsIgnoreCase("Y")) {

                                showAlert
                                        .setMessage("Please Select Life Assure Dob First");
                                showAlert.setNeutralButton("OK",
                                        new DialogInterface.OnClickListener() {

                                            public void onClick(
                                                    DialogInterface dialog,
                                                    int which) {
                                                // apply focusable method
                                                btn_bi_smart_income_protect_life_assured_date
                                                        .setText("Select Date");
                                                lifeAssured_date_of_birth = "";
                                                edt_bi_smart_income_protect_life_assured_age
                                                        .setText("");
                                                ll_smart_income_protect_backdating
                                                        .setVisibility(View.GONE);
                                            }
                                        });
                                showAlert.show();

                                proposer_Backdating_WishToBackDate_Policy = "N";
                                ll_smart_income_protect_backdating
                                        .setVisibility(View.GONE);
                            } else {

                                proposer_Backdating_WishToBackDate_Policy = "N";
                                proposer_Backdating_BackDate = "";
                                // setDefaultDate();
                                ll_smart_income_protect_backdating
                                        .setVisibility(View.GONE);

                                edt_bi_smart_income_protect_life_assured_age
                                        .setText(lifeAssuredAge);

                                // valAge();
                            }
                        }
                    }
                });

        btnBack.setOnClickListener(new OnClickListener() {
            @Override
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

                proposer_First_Name = edt_bi_smart_income_protect_proposer_first_name
                        .getText().toString().trim();
                proposer_Middle_Name = edt_bi_smart_income_protect_proposer_middle_name
                        .getText().toString().trim();
                proposer_Last_Name = edt_bi_smart_income_protect_proposer_last_name
                        .getText().toString().trim();


                propsoser_gender = spinnerProposerGender.getSelectedItem().toString();
                gender = spnr_bi_smart_income_protect_selGender.getSelectedItem().toString();

                name_of_proposer = proposer_Title + " " + proposer_First_Name
                        + " " + proposer_Middle_Name + " " + proposer_Last_Name;

                lifeAssured_First_Name = edt_bi_smart_income_protect_life_assured_first_name
                        .getText().toString().trim();
                lifeAssured_Middle_Name = edt_bi_smart_income_protect_life_assured_middle_name
                        .getText().toString().trim();
                lifeAssured_Last_Name = edt_bi_smart_income_protect_life_assured_last_name
                        .getText().toString().trim();

                name_of_life_assured = lifeAssured_Title + " "
                        + lifeAssured_First_Name + " "
                        + lifeAssured_Middle_Name + " " + lifeAssured_Last_Name;

                mobileNo = edt_smart_income_protect_contact_no.getText()
                        .toString();
                emailId = edt_smart_income_protect_Email_id.getText()
                        .toString();
                ConfirmEmailId = edt_smart_income_protect_ConfirmEmail_id
                        .getText().toString();

                if (valLifeAssuredProposerDetail() && valDob()
                        && valProposerDob() && valBackdatingDate()
                        && valBasicDetail() && valSA() && valAge()
                        && valMaturityAge() && valTermRider()
                        && valDoYouBackdate() && valBackdate() && valMaturityBenefit()) {

                    if (proposer_Is_Same_As_Life_Assured.equalsIgnoreCase("Y")) {
                        proposer_Title = "";
                        proposer_First_Name = "";
                        proposer_Middle_Name = "";
                        proposer_Last_Name = "";
                        name_of_proposer = "";
                        proposer_date_of_birth = "";
                    }
                    Log.e("ouput:", output + "");
                    boolean flag = addListenerOnSubmit();
                    Log.e("ouput:", output + "");
                    // if (valPremiumError && valRiderPremiumError) {

                    // insertDataIntoDatabase();
                    String policyTerm = smartIncomeProtectBean.getBasicTerm()
                            + "";
                    System.out.println("+policy_term:" + policyTerm);
                    if (flag) {
                        getInput(smartIncomeProtectBean);
                        if (needAnalysis_flag == 0) {// Display output on next
                            // page
                            Intent i = new Intent(
                                    BI_SmartIncomeProtectActivity.this,
                                    Success.class);

                            i.putExtra(
                                    "op",
                                    spnr_bi_smart_income_protect_premium_paying_mode
                                            .getSelectedItem().toString()
                                            + " Basic Premium is Rs. "
                                            + decimalCurrencyFormat.format(Double.parseDouble(prsObj
                                            .parseXmlTag(
                                                    retVal.toString(),
                                                    "yearlyPrem"))));
                            // Total Result
                            i.putExtra(
                                    "op1",
                                    spnr_bi_smart_income_protect_premium_paying_mode
                                            .getSelectedItem().toString()
                                            + " Installment Premium without Applicable Taxes is Rs. "
                                            + currencyFormat.format(Double.parseDouble(prsObj
                                            .parseXmlTag(
                                                    retVal.toString(),
                                                    "premInst"))));
                            i.putExtra(
                                    "op2",
                                    "applicable tax is Rs. "
                                            + currencyFormat.format(Double.parseDouble(prsObj
                                            .parseXmlTag(
                                                    retVal.toString(),
                                                    "servcTax"))));
                            i.putExtra(
                                    "op3",
                                    spnr_bi_smart_income_protect_premium_paying_mode
                                            .getSelectedItem().toString()
                                            + " Installment Premium with Applicable Taxes is Rs. "
                                            + currencyFormat.format(Double.parseDouble(prsObj
                                            .parseXmlTag(
                                                    retVal.toString(),
                                                    "premWthST"))));
                            // Maturity Benefit

                            i.putExtra(
                                    "op4",
                                    "Guaranteed Maturity Benefit for next 15 years after Maturity is Rs. "
                                            + currencyFormat.format(Double.parseDouble(prsObj.parseXmlTag(
                                            retVal.toString(),
                                            "guaranMatBen"
                                                    + (Integer
                                                    .parseInt(policyTerm) + 1)))));
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

                            // PTA
                            if (cb_bi_smart_income_protect_pt_rider.isChecked()) {
                                // i.putExtra("op7","Preferred Term Assurance Rider Premium is Rs."+
                                // currencyFormat.format(Double.parseDouble(commonForAllProd.getRoundUp(premiumPT))));
                                i.putExtra(
                                        "op7",
                                        "Preferred Term Assurance Rider Premium is Rs. "
                                                + decimalCurrencyFormat.format(Double.parseDouble(prsObj
                                                .parseXmlTag(retVal
                                                                .toString(),
                                                        "premPTA"))));

                            }
                            // ADB
                            if (cb_bi_smart_income_protect_adb_rider
                                    .isChecked()) {
                                // i.putExtra("op8","Accidental Death Benefit Rider Premium is Rs."+
                                // currencyFormat.format(Double.parseDouble(commonForAllProd.getRoundUp(premiumADB))));
                                i.putExtra(
                                        "op8",
                                        "Accidental Death Benefit Rider Premium is Rs. "
                                                + decimalCurrencyFormat.format(Double.parseDouble(prsObj
                                                .parseXmlTag(retVal
                                                                .toString(),
                                                        "premADB"))));

                            }
                            // ATPDB
                            if (cb_bi_smart_income_protect_atpdb_rider
                                    .isChecked()) {
                                // i.putExtra("op9","Accidental Total & Permanent Disability Benefit Rider Premium is Rs."+
                                // currencyFormat.format(Double.parseDouble(commonForAllProd.getRoundUp(premiumATPDB))));
                                i.putExtra(
                                        "op9",
                                        "Accidental Total & Permanent Disability Benefit Rider Premium is Rs. "
                                                + decimalCurrencyFormat.format(Double.parseDouble(prsObj
                                                .parseXmlTag(retVal
                                                                .toString(),
                                                        "premATPDB"))));

                            }
                            // CC 13 NonLinked
                            if (cb_bi_smart_income_protect_cc13nl_rider
                                    .isChecked()) {
                                // i.putExtra("op10","Criti Care13 Premium is Rs."+
                                // currencyFormat.format(Double.parseDouble(commonForAllProd.getRoundUp(premiumCC13NonLinked))));
                                i.putExtra(
                                        "op10",
                                        "Criti Care13 Premium is Rs. "
                                                + decimalCurrencyFormat.format(Double.parseDouble(prsObj
                                                .parseXmlTag(retVal
                                                                .toString(),
                                                        "premCC13"))));

                            }

                            startActivity(i);
                        } else
                            Dialog();
                    }
                }

            }
        });

    }

    private boolean addListenerOnSubmit() {

        smartIncomeProtectBean = new SmartIncomeProtectBean();

        if (cb_staffdisc.isChecked()) {
            smartIncomeProtectBean.setStaffDisc(true);
        } else {
            smartIncomeProtectBean.setStaffDisc(false);
        }

        if (cb_kerladisc.isChecked()) {
            smartIncomeProtectBean.setKerlaDisc(true);
        } else {
            smartIncomeProtectBean.setKerlaDisc(false);
        }

        if (cb_bi_smart_income_protect_JKResident.isChecked()) {
            smartIncomeProtectBean.setJKResident(true);
        } else {
            smartIncomeProtectBean.setJKResident(false);
        }

        // smartIncomeProtectBean.setStaffDisc(cb_bi_smart_income_protect_JKResident.isChecked());

        if (cb_bi_smart_income_protect_pt_rider.isChecked()) {
            smartIncomeProtectBean.setPreferredStatus(true);
            smartIncomeProtectBean.setPreferredTerm(Integer
                    .parseInt(spnr_bi_smart_income_protect_pt_rider_term
                            .getSelectedItem().toString()));
            smartIncomeProtectBean.setPreferredSA(Integer
                    .parseInt(edt_bi_smart_income_protect_pt_rider_sum_assured
                            .getText().toString()));
        } else {
            smartIncomeProtectBean.setPreferredStatus(false);
            smartIncomeProtectBean.setPreferredSA(0);
        }

        if (cb_bi_smart_income_protect_adb_rider.isChecked()) {
            smartIncomeProtectBean.setADB_Status(true);
            smartIncomeProtectBean.setADB_Term(Integer
                    .parseInt(spnr_bi_smart_income_protect_adb_rider_term
                            .getSelectedItem().toString()));
            smartIncomeProtectBean.setADB_SA(Integer
                    .parseInt(edt_bi_smart_income_protect_adb_rider_sum_assured
                            .getText().toString()));
        } else {
            smartIncomeProtectBean.setADB_Status(false);
            smartIncomeProtectBean.setADB_SA(0);
        }

        if (cb_bi_smart_income_protect_atpdb_rider.isChecked()) {
            smartIncomeProtectBean.setATPDB_Status(true);
            smartIncomeProtectBean.setATPDB_Term(Integer
                    .parseInt(spnr_bi_smart_income_protect_atpdb_rider_term
                            .getSelectedItem().toString()));
            smartIncomeProtectBean
                    .setATPDB_SA(Integer
                            .parseInt(edt_bi_smart_income_protect_atpbd_rider_sum_assured
                                    .getText().toString()));
        } else {
            smartIncomeProtectBean.setATPDB_Status(false);
            smartIncomeProtectBean.setATPDB_SA(0);
        }

        if (cb_bi_smart_income_protect_cc13nl_rider.isChecked()) {
            smartIncomeProtectBean.setCc13nlStatus(true);
            smartIncomeProtectBean.setCc13nlTerm(Integer
                    .parseInt(spnr_bi_smart_income_protect_cc13nl_rider_term
                            .getSelectedItem().toString()));
            smartIncomeProtectBean
                    .setCc13nlSA(Integer
                            .parseInt(edt_bi_smart_income_protect_cc13nl_rider_sum_assured
                                    .getText().toString()));
        } else {
            smartIncomeProtectBean.setCc13nlStatus(false);
            smartIncomeProtectBean.setCc13nlSA(0);
        }

        smartIncomeProtectBean.setAge(Integer
                .parseInt(edt_bi_smart_income_protect_life_assured_age
                        .getText().toString()));

        smartIncomeProtectBean.setBasicSA(Integer
                .parseInt(edt_bi_smart_income_protect_sum_assured_amount
                        .getText().toString()));
        smartIncomeProtectBean.setBasicTerm(Integer
                .parseInt(spnr_bi_smart_income_protect_policyterm
                        .getSelectedItem().toString()));
        smartIncomeProtectBean.setGender(spnr_bi_smart_income_protect_selGender
                .getSelectedItem().toString());
        smartIncomeProtectBean
                .setPremFreq(spnr_bi_smart_income_protect_premium_paying_mode
                        .getSelectedItem().toString());

        smartIncomeProtectBean.setMaturityOption(lumpSumAmount);
        // smartIncomeProtectBean.setType(Is_Smoker_or_Not);
        return showsmartIncomeProtectOutputPg(smartIncomeProtectBean);
    }

    private boolean showsmartIncomeProtectOutputPg(
            SmartIncomeProtectBean smartIncomeProtectBean) {

        bussIll = new StringBuilder();
        retVal = new StringBuilder();
        int _year_F;

        int year_F;
        String guaranMatBen = null,
                nonGuaranMatBen_4Percent,
                nonGuaranMatBen_8Percent,
                totaBasePrem,
                annulizedprem,
                survivalBen,
                guarntdDeathBenft,
                nongrntdDeathNenft_4Percent,
                nongrntdDeathNenft_8Percent,
                GSV_surrendr_val,
                NonGSV_surrndr_val_4Percent,
                NonGSV_surrndr_val_8Percent,
                TotalMaturity_4percent,
                TotalMaturity_8percent,
                TotalDeathBenefit4percent,
                TotalDeathBenefit8percent,
                _nonGuaranMatBen_8Percent = null,
                _nonGuaranMatBen_4Percent = null;

        double nongrntdDeathNenft_4PercentWithoutRound, nongrntdDeathNenft_8PercentWithoutRound = 0;
        // Create object of SmartIncomeProtectBussinesLogic class
        SmartIncomeProtectBusinessLogic smartIncomeProtectBusinessLogic = new SmartIncomeProtectBusinessLogic(
                smartIncomeProtectBean);

        double premiumCC13NonLinkedNotRounded = 0, premiumADBNotRounded = 0, premiumATPDBNotRounded = 0, premiumPTNotRounded = 0, sumOfRiders;
        String sumofridersRoundup;

        // Basic Premium
        String premiumBasic_NotRounded = commonForAllProd
                .getRoundOffLevel2(commonForAllProd.getStringWithout_E(Double.parseDouble(""
                        + smartIncomeProtectBusinessLogic
                        .get_Premium_Basic_WithoutST_NotRounded())));

        String premiumBasic_Rounded = commonForAllProd.getRoundUp(commonForAllProd.getRoundOffLevel2(commonForAllProd.getStringWithout_E(Double.parseDouble(premiumBasic_NotRounded))));
        //System.out.println("premiumBasic_NotRounded "+premiumBasic_NotRounded);

        String basicPremWithoutDisc = commonForAllProd
                .getRoundOffLevel2(commonForAllProd.getStringWithout_E(Double.parseDouble(""
                        + smartIncomeProtectBusinessLogic
                        .get_Premium_Basic_WithoutStaffDisc_NotRounded())));

        String basicPremWithoutDiscSA = commonForAllProd
                .getRoundOffLevel2(commonForAllProd.getStringWithout_E(Double.parseDouble(""
                        + smartIncomeProtectBusinessLogic
                        .get_Premium_Basic_WithoutStaffDiscSA_NotRounded())));

        //added by sujata 20-02-2020
        String tot_base_Prem_Paid = commonForAllProd.getStringWithout_E(smartIncomeProtectBusinessLogic.getYearlyTotBasePremPaid(Double.parseDouble(premiumBasic_Rounded)));

        int rowNumber = 0;
        double sumGuaranteedSurvivalBen = 0, sumrevesionaryBonus4per = 0;

        String getFrequencyLoading = commonForAllProd
                .getRound(commonForAllProd.getStringWithout_E(Double.parseDouble("" + smartIncomeProtectBusinessLogic.get_premium_withoutFreqLoading())));


        for (int j = 0; j < smartIncomeProtectBean.getBasicTerm() + 15; j++) {
            rowNumber++;

            year_F = rowNumber;
            _year_F = year_F;
            //System.out.println("1. year_F "+year_F);
            bussIll.append("<policyYr").append(_year_F).append(">").append(_year_F).append("</policyYr").append(_year_F).append(">");

            totaBasePrem = commonForAllProd.getRoundOffLevel2(commonForAllProd.getStringWithout_E(Double.parseDouble("" + smartIncomeProtectBusinessLogic.get_Total_Base_Premium_Paid(year_F, Double.parseDouble(tot_base_Prem_Paid)))));
            //	System.out.println("totaBasePrem "+totaBasePrem + "  "+ Double.parseDouble(""+smartIncomeProtectBusinessLogic.get_Total_Base_Premium_Paid(year_F,Double.parseDouble(tot_base_Prem_Paid))));
            bussIll.append("<totaBasePrem").append(_year_F).append(">").append(commonForAllProd.getRound(commonForAllProd.getStringWithout_E(Double.parseDouble("" + totaBasePrem)))).append("</totaBasePrem").append(_year_F).append(">");

            //added by sujata on 19-02-2020

            annulizedprem = commonForAllProd.getRound(commonForAllProd.getStringWithout_E(Double.parseDouble("" + smartIncomeProtectBusinessLogic.getannulizedPremFinal(_year_F, smartIncomeProtectBean.getBasicTerm(), smartIncomeProtectBean.getPremFreq()))));

            bussIll.append("<annulizedprem").append(_year_F).append(">").append(commonForAllProd.getRound(commonForAllProd.getStringWithout_E(Double.parseDouble("" + annulizedprem)))).append("</annulizedprem").append(_year_F).append(">");


            bussIll.append("<Guarnteedaddition").append(_year_F).append(">").append(0).append("</Guarnteedaddition").append(_year_F).append(">");

            survivalBen = commonForAllProd.getRound(commonForAllProd.getStringWithout_E(Double.parseDouble("" + smartIncomeProtectBusinessLogic.getSurvivalBenefit(_year_F, smartIncomeProtectBean.getBasicTerm(), smartIncomeProtectBean.getBasicSA()))));

            bussIll.append("<survivalBen").append(_year_F).append(">").append(commonForAllProd.getRound(commonForAllProd.getStringWithout_E(Double.parseDouble("" + survivalBen)))).append("</survivalBen").append(_year_F).append(">");

            //added by sujata on 19-02-2020
            sumGuaranteedSurvivalBen = sumGuaranteedSurvivalBen + Double.parseDouble(totaBasePrem);
            //System.out.println("sumGuaranteedSurvivalBen "+sumGuaranteedSurvivalBen);


            guarntdDeathBenft = commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(Double.parseDouble("" + smartIncomeProtectBusinessLogic.getGuarntedDeathBenefit(year_F, smartIncomeProtectBean.getBasicTerm(), smartIncomeProtectBean.getAge(), smartIncomeProtectBean.getBasicSA(), Double.parseDouble(totaBasePrem)))));
            //System.out.println("guarntdDeathBenft "+guarntdDeathBenft);
            bussIll.append("<guarntdDeathBenft").append(_year_F).append(">").append(guarntdDeathBenft).append("</guarntdDeathBenft").append(_year_F).append(">");

            //nongrntdDeathNenft_4Percent=commonForAllProd.getRoundUp(commonForAllProd.getRoundOffLevel2(commonForAllProd.getStringWithout_E(Double.parseDouble(""+smartIncomeProtectBusinessLogic.getNonGuarntedDeathBenefit(year_F,"4%")))));
            nongrntdDeathNenft_4Percent = commonForAllProd.getStringWithout_E(Math.round(Double.parseDouble(commonForAllProd.getRoundOffLevel2(commonForAllProd.getStringWithout_E(Double.parseDouble("" + smartIncomeProtectBusinessLogic.getNonGuarntedDeathBenefit(year_F, "4%", smartIncomeProtectBean.getBasicTerm(), smartIncomeProtectBean.getAge(), smartIncomeProtectBean.getBasicSA(), Double.parseDouble(totaBasePrem))))))));

//			sumrevesionaryBonus4per = sumrevesionaryBonus4per + Double.parseDouble(nongrntdDeathNenft_4Percent);
//			System.out.println("sumrevesionaryBonus4per "+sumrevesionaryBonus4per);

            //System.out.println("nongrntdDeathNenft_4Percent "+nongrntdDeathNenft_4Percent +  "  "+smartIncomeProtectBusinessLogic.getNonGuarntedDeathBenefit(year_F,"4%"));
            bussIll.append("<nongrntdDeathNenft_4Percent").append(_year_F).append(">").append(nongrntdDeathNenft_4Percent).append("</nongrntdDeathNenft_4Percent").append(_year_F).append(">");
            nongrntdDeathNenft_4PercentWithoutRound = Double.parseDouble(commonForAllProd.getRoundOffLevel2(commonForAllProd.getStringWithout_E(Double.parseDouble("" + smartIncomeProtectBusinessLogic.getNonGuarntedDeathBenefit(year_F, "4%", smartIncomeProtectBean.getBasicTerm(), smartIncomeProtectBean.getAge(), smartIncomeProtectBean.getBasicSA(), Double.parseDouble(totaBasePrem))))));

            bussIll.append("<cashbonus").append(_year_F).append(">").append(0).append("</cashbonus").append(_year_F).append(">");

            nongrntdDeathNenft_8Percent = commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(Double.parseDouble("" + smartIncomeProtectBusinessLogic.getNonGuarntedDeathBenefit(year_F, "8%", smartIncomeProtectBean.getBasicTerm(), smartIncomeProtectBean.getAge(), smartIncomeProtectBean.getBasicSA(), Double.parseDouble(totaBasePrem)))));
            //System.out.println("nongrntdDeathNenft_8Percent  "+nongrntdDeathNenft_8Percent);
            bussIll.append("<nongrntdDeathNenft_8Percent").append(_year_F).append(">").append(nongrntdDeathNenft_8Percent).append("</nongrntdDeathNenft_8Percent").append(_year_F).append(">");


            guaranMatBen = commonForAllProd.getStringWithout_E(smartIncomeProtectBusinessLogic.getGuaranMatBen(year_F));
            //System.out.println("guaranMatBen "+guaranMatBen);
            bussIll.append("<guaranMatBen").append(_year_F).append(">").append(guaranMatBen).append("</guaranMatBen").append(_year_F).append(">");

            nonGuaranMatBen_4Percent = commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(Double.parseDouble("" + smartIncomeProtectBusinessLogic.getNonGuaranMatBen(year_F, Double.parseDouble(nongrntdDeathNenft_4Percent)))));
            //System.out.println("nonGuaranMatBen_4Percent "+nonGuaranMatBen_4Percent);
            bussIll.append("<nonGuaranMatBen_4Percent").append(_year_F).append(">").append(nonGuaranMatBen_4Percent).append("</nonGuaranMatBen_4Percent").append(_year_F).append(">");

            nonGuaranMatBen_8Percent = commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(Double.parseDouble("" + smartIncomeProtectBusinessLogic.getNonGuaranMatBen(year_F, Double.parseDouble(nongrntdDeathNenft_8Percent)))));
            //System.out.println("nonGuaranMatBen_8Percent "+nonGuaranMatBen_8Percent);
            bussIll.append("<nonGuaranMatBen_8Percent").append(_year_F).append(">").append(nonGuaranMatBen_8Percent).append("</nonGuaranMatBen_8Percent").append(_year_F).append(">");

            if (year_F == smartIncomeProtectBean.getBasicTerm()) {
                _nonGuaranMatBen_4Percent = nonGuaranMatBen_4Percent;
                _nonGuaranMatBen_8Percent = nonGuaranMatBen_8Percent;
            }

            GSV_surrendr_val = commonForAllProd.getRound(commonForAllProd.getStringWithout_E(Double.parseDouble("" + (smartIncomeProtectBusinessLogic.getGSV_SurrenderValue(year_F, Double.parseDouble(totaBasePrem))))));
            //System.out.println("GSV_surrendr_val "+GSV_surrendr_val);
            bussIll.append("<GSV_surrendr_val").append(_year_F).append(">").append(GSV_surrendr_val).append("</GSV_surrendr_val").append(_year_F).append(">");


            //	smartIncomeProtectBusinessLogic.get_Total_Base_Premium_Paid(year_F, premBasic)

            NonGSV_surrndr_val_4Percent = commonForAllProd.getRound(commonForAllProd.getStringWithout_E((smartIncomeProtectBusinessLogic.getNonGSV_surrndr_val4percent(year_F, smartIncomeProtectBean.getBasicTerm(), smartIncomeProtectBean.getBasicSA(), nongrntdDeathNenft_4PercentWithoutRound, Double.parseDouble(nongrntdDeathNenft_4Percent)))));
            //System.out.println("NonGSV_surrndr_val_4Percent "+NonGSV_surrndr_val_4Percent);
            bussIll.append("<NonGSV_surrndr_val_4Percent").append(_year_F).append(">").append(NonGSV_surrndr_val_4Percent).append("</NonGSV_surrndr_val_4Percent").append(_year_F).append(">");

            NonGSV_surrndr_val_8Percent = commonForAllProd.getRound(commonForAllProd.getStringWithout_E(Double.parseDouble("" + (smartIncomeProtectBusinessLogic.getNonGSV_surrndr_val8percent(year_F, smartIncomeProtectBean.getBasicTerm(), smartIncomeProtectBean.getBasicSA(), Double.parseDouble(nongrntdDeathNenft_8Percent), Double.parseDouble(nongrntdDeathNenft_8Percent))))));
            //System.out.println("NonGSV_surrndr_val_8Percent "+NonGSV_surrndr_val_8Percent);
            bussIll.append("<NonGSV_surrndr_val_8Percent").append(_year_F).append(">").append(NonGSV_surrndr_val_8Percent).append("</NonGSV_surrndr_val_8Percent").append(_year_F).append(">");

            TotalMaturity_4percent = commonForAllProd.getRound(commonForAllProd.getStringWithout_E(Double.parseDouble("" + (smartIncomeProtectBusinessLogic.gettotalMaturityBenfit(year_F, smartIncomeProtectBean.getBasicTerm(), Double.parseDouble(nongrntdDeathNenft_4Percent))))));

            bussIll.append("<TotalMaturity_4percent").append(_year_F).append(">").append(TotalMaturity_4percent).append("</TotalMaturity_4percent").append(_year_F).append(">");

            TotalMaturity_8percent = commonForAllProd.getRound(commonForAllProd.getStringWithout_E(Double.parseDouble("" + (smartIncomeProtectBusinessLogic.gettotalMaturityBenfit(year_F, smartIncomeProtectBean.getBasicTerm(), Double.parseDouble(nongrntdDeathNenft_8Percent))))));

            bussIll.append("<TotalMaturity_8percent").append(_year_F).append(">").append(TotalMaturity_8percent).append("</TotalMaturity_8percent").append(_year_F).append(">");

            double prempaid = 0;
            prempaid = prempaid + Double.parseDouble(totaBasePrem);

            TotalDeathBenefit4percent = commonForAllProd.getRound(commonForAllProd.getStringWithout_E(Double.parseDouble("" + (smartIncomeProtectBusinessLogic.getTotalDeathBenefit4percent(year_F, smartIncomeProtectBean.getBasicTerm(), Double.parseDouble(guarntdDeathBenft), Double.parseDouble(nongrntdDeathNenft_4Percent), prempaid)))));

            bussIll.append("<TotalDeathBenefit4percent").append(_year_F).append(">").append(TotalDeathBenefit4percent).append("</TotalDeathBenefit4percent").append(_year_F).append(">");

            TotalDeathBenefit8percent = commonForAllProd.getRound(commonForAllProd.getStringWithout_E(Double.parseDouble("" + (smartIncomeProtectBusinessLogic.getTotalDeathBenefit4percent(year_F, smartIncomeProtectBean.getBasicTerm(), Double.parseDouble(guarntdDeathBenft), Double.parseDouble(nongrntdDeathNenft_8Percent), prempaid)))));

            bussIll.append("<TotalDeathBenefit8percent").append(_year_F).append(">").append(TotalDeathBenefit8percent).append("</TotalDeathBenefit8percent").append(_year_F).append(">");


        }

        // Rider premiums
        String premiumPT = "0", premiumPTWithoutDisc = "0", premiumPTWithoutDiscSA = "0";
        if (cb_bi_smart_income_protect_pt_rider.isChecked()) {
            premiumPTNotRounded = smartIncomeProtectBusinessLogic
                    .get_Premium_PT_WithoutST_NotRounded();

            //	//System.out.println(Double.parseDouble(""+smartIncomeProtectBusinessLogic.get_Premium_PT_WithoutST_NotRounded()) + "   "+commonForAllProd.getStringWithout_E(Double.parseDouble(""+smartIncomeProtectBusinessLogic.get_Premium_PT_WithoutST_NotRounded())));
            premiumPT = commonForAllProd.getRoundOffLevel2(commonForAllProd.getStringWithout_E(Double.parseDouble("" + smartIncomeProtectBusinessLogic.get_Premium_PT_WithoutST_NotRounded())));
            //	            //System.out.println("***** premiumPTA -> " + premiumPT);
            premiumPTWithoutDisc = commonForAllProd
                    .getRoundOffLevel2(commonForAllProd.getStringWithout_E(Double.parseDouble(""
                            + smartIncomeProtectBusinessLogic
                            .get_Premium_PT_WithoutStaffDisc_NotRounded())));

            /* Modified by Akshaya on 31-AUG-15 Start ***/

            premiumPTWithoutDiscSA = commonForAllProd
                    .getRoundOffLevel2(commonForAllProd.getStringWithout_E(Double.parseDouble(""
                            + smartIncomeProtectBusinessLogic
                            .get_Premium_PT_WithoutStaffDiscSA_NotRounded())));

            /* Modified by Akshaya on 31-AUG-15 End ***/

        }
        String premiumADB = "0", premiumADBWithoutDisc = "0", premiumADBWithoutDiscSA = "0";
        if (cb_bi_smart_income_protect_adb_rider.isChecked()) {
            premiumADBNotRounded = smartIncomeProtectBusinessLogic
                    .get_Premium_ADB_WithoutST_NotRounded();

            premiumADB = commonForAllProd.getRoundOffLevel2(commonForAllProd.getStringWithout_E(Double.parseDouble("" + smartIncomeProtectBusinessLogic.get_Premium_ADB_WithoutST_NotRounded())));
            //	            //System.out.println("***** premiumADB -> " + premiumADB);

            premiumADBWithoutDisc = commonForAllProd
                    .getRoundOffLevel2(commonForAllProd.getStringWithout_E(Double.parseDouble(""
                            + smartIncomeProtectBusinessLogic
                            .get_Premium_ADB_WithoutStaffDisc_NotRounded())));

            /* Modified by Akshaya on 31-AUG-15 Start ***/

            premiumADBWithoutDiscSA = commonForAllProd
                    .getRoundOffLevel2(commonForAllProd.getStringWithout_E(Double.parseDouble(""
                            + smartIncomeProtectBusinessLogic
                            .get_Premium_ADB_WithoutStaffDiscSA_NotRounded())));

            /* Modified by Akshaya on 31-AUG-15 End ***/

        }

        String premiumATPDB = "0", premiumATPDBWithoutDisc = "0", premiumATPDBWithoutDiscSA = "0";
        if (cb_bi_smart_income_protect_atpdb_rider.isChecked()) {
            premiumATPDBNotRounded = smartIncomeProtectBusinessLogic
                    .get_Premium_ATPDB_WithoutST_NotRounded();

            premiumATPDB = commonForAllProd.getRoundOffLevel2(commonForAllProd.getStringWithout_E(Double.parseDouble("" + smartIncomeProtectBusinessLogic.get_Premium_ATPDB_WithoutST_NotRounded())));
            //	            //System.out.println("***** premiumATPDB -> " + premiumATPDB);

            premiumATPDBWithoutDisc = commonForAllProd
                    .getRoundOffLevel2(commonForAllProd.getStringWithout_E(Double.parseDouble(""
                            + smartIncomeProtectBusinessLogic
                            .get_Premium_ATPDB_WithoutStaffDisc_NotRounded())));

            premiumATPDBWithoutDiscSA = commonForAllProd
                    .getRoundOffLevel2(commonForAllProd.getStringWithout_E(Double.parseDouble(""
                            + smartIncomeProtectBusinessLogic
                            .get_Premium_ATPDB_WithoutStaffDiscSA_NotRounded())));

        }

        String premiumCC13NonLinked = "0", premiumCCNonLinkedWithoutDisc = "0", premiumCCNonLinkedWithoutDiscSA = "0";
        if (cb_bi_smart_income_protect_cc13nl_rider.isChecked()) {
            premiumCC13NonLinkedNotRounded = smartIncomeProtectBusinessLogic
                    .get_Premium_CC13NonLinked_WithoutST_NotRounded();

            premiumCC13NonLinked = commonForAllProd.getRoundOffLevel2(commonForAllProd.getStringWithout_E(Double.parseDouble("" + smartIncomeProtectBusinessLogic.get_Premium_CC13NonLinked_WithoutST_NotRounded())));
            //	            //System.out.println("***** premiumCCNonLinked -> " + premiumCC13NonLinked);

            premiumCCNonLinkedWithoutDisc = commonForAllProd
                    .getRoundOffLevel2(commonForAllProd.getStringWithout_E(Double.parseDouble(""
                            + smartIncomeProtectBusinessLogic
                            .get_Premium_CC13NonLinked_WithoutStaffDisc_NotRounded())));

            premiumCCNonLinkedWithoutDiscSA = commonForAllProd
                    .getRoundOffLevel2(commonForAllProd.getStringWithout_E(Double.parseDouble(""
                            + smartIncomeProtectBusinessLogic
                            .get_Premium_CC13NonLinked_WithoutStaffDiscSA_NotRounded())));

        }

        /* added by Akshaya on 04-AUG-15 start */
        double discountPercentage = smartIncomeProtectBusinessLogic
                .getStaffRebate("basic");
        String InstmntPrem = commonForAllProd.getRoundUp(commonForAllProd
                .getStringWithout_E(Double.parseDouble(basicPremWithoutDisc)
                        + Double.parseDouble(premiumPTWithoutDisc)
                        + Double.parseDouble(premiumADBWithoutDisc)
                        + Double.parseDouble(premiumATPDBWithoutDisc)
                        + Double.parseDouble(premiumCCNonLinkedWithoutDisc)));

        //System.out.println(" E : "+commonForAllProd.getStringWithout_E((Double.parseDouble(premiumBasic_NotRounded)+Double.parseDouble(premiumPT)+Double.parseDouble(premiumADB)+Double.parseDouble(premiumATPDB)+Double.parseDouble(premiumCC13NonLinked))));
        //		String premiumInst=commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E((Double.parseDouble(premiumBasic_NotRounded)+Double.parseDouble(premiumPT)+Double.parseDouble(premiumADB)+Double.parseDouble(premiumATPDB)+Double.parseDouble(premiumCC13NonLinked))));

//		String premiumInst = commonForAllProd
//				.getRoundUp(commonForAllProd.getStringWithout_E((Double
//						.parseDouble(premiumBasic_NotRounded)
//						+ premiumPTNotRounded
//						+ premiumADBNotRounded
//						+ premiumATPDBNotRounded + premiumCC13NonLinkedNotRounded)));

        sumOfRiders = Double.parseDouble(premiumADB) + Double.parseDouble(premiumPT) + Double.parseDouble(premiumATPDB) + Double.parseDouble(premiumCC13NonLinked);

        sumofridersRoundup = commonForAllProd
                .getRoundOffLevel2(commonForAllProd.getStringWithout_E(sumOfRiders));

        //System.out.println("sumOfRiders "+sumofridersRoundup);

        String premiumInst = commonForAllProd
                .getRoundUp(commonForAllProd.getStringWithout_E((Double
                        .parseDouble(premiumBasic_NotRounded)
                        + Double.parseDouble(sumofridersRoundup))));

        String InstRider = sumofridersRoundup;
        //System.out.println("premiumInst "+premiumInst);
        //		String premiumInstWithST = commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E((smartIncomeProtectBusinessLogic.getPremiumInstWithST(Double.parseDouble(premiumInst)))));
        //		String serviceTax=commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E((Double.parseDouble(premiumInstWithST)-Double.parseDouble(premiumInst))));

        String minesOccuInterest = ""
                + smartIncomeProtectBusinessLogic
                .getMinesOccuInterest(smartIncomeProtectBean
                        .getBasicSA());

        String servicetax_MinesOccuInterest = ""
                + smartIncomeProtectBusinessLogic
                .getServiceTaxMines(Double.parseDouble(minesOccuInterest));

        minesOccuInterest = "" + (Double.parseDouble(minesOccuInterest) + Double.parseDouble(servicetax_MinesOccuInterest));

        premiumInst = commonForAllProd.getStringWithout_E(Double
                .parseDouble(premiumInst));

        // String premiumInstWithST =
        // commonForAllProd.getRoundUp(""+(Double.parseDouble(premiumInst)*(prop.serviceTax+1)));

        /* Modified By - Priyanka Warekar - 26-08-2015 - Start */
        // String premiumInstWithST = commonForAllProd
        // .getRoundUp(commonForAllProd
        // .getStringWithout_E((smartIncomeProtectBusinessLogic
        // .getPremiumInstWithST(Double
        // .parseDouble(premiumInst)))));
        // String serviceTax = commonForAllProd
        // .getRoundUp(commonForAllProd.getStringWithout_E((Double
        // .parseDouble(premiumInstWithST) - Double
        // .parseDouble(premiumInst))));

        /*double basicServiceTax = smartIncomeProtectBusinessLogic.getServiceTax(
				Double.parseDouble(premiumInst),
				smartIncomeProtectBean.getJKResident(), "basic");
		double SBCServiceTax = smartIncomeProtectBusinessLogic.getServiceTax(
				Double.parseDouble(premiumInst),
				smartIncomeProtectBean.getJKResident(), "SBC");
		double KKCServiceTax = smartIncomeProtectBusinessLogic.getServiceTax(
				Double.parseDouble(premiumInst),
				smartIncomeProtectBean.getJKResident(), "KKC");*/

        boolean isKerlaDisc = smartIncomeProtectBean.isKerlaDisc();
        double tax;
        if (isKerlaDisc) {
            tax = smartIncomeProtectBusinessLogic.getServiceTaxBI(Double.parseDouble(premiumInst), smartIncomeProtectBean.getJKResident(), "basic", smartIncomeProtectBean.isKerlaDisc());


        } else {
            tax = smartIncomeProtectBusinessLogic.getServiceTaxBI(Double.parseDouble(premiumInst), smartIncomeProtectBean.getJKResident(), "basic", smartIncomeProtectBean.isKerlaDisc());

        }

        double taxSecond;
        if (isKerlaDisc) {
            taxSecond = smartIncomeProtectBusinessLogic.getServiceTaxBISecondYear(Double.parseDouble(premiumInst), smartIncomeProtectBean.getJKResident(), "basic", smartIncomeProtectBean.isKerlaDisc());


        } else {
            taxSecond = smartIncomeProtectBusinessLogic.getServiceTaxBISecondYear(Double.parseDouble(premiumInst), smartIncomeProtectBean.getJKResident(), "basic", smartIncomeProtectBean.isKerlaDisc());

        }

        double basicServiceTax;
        double SBCServiceTax = 0;
        double KKCServiceTax = 0;

        double kerlaServiceTax = 0;
        double KeralaCessServiceTax = 0;

        if (isKerlaDisc) {
            basicServiceTax = smartIncomeProtectBusinessLogic.getServiceTax(Double.parseDouble(premiumBasic_NotRounded), smartIncomeProtectBean.getJKResident(), "basic", smartIncomeProtectBean.isKerlaDisc());
            kerlaServiceTax = smartIncomeProtectBusinessLogic.getServiceTax(Double.parseDouble(premiumBasic_NotRounded), smartIncomeProtectBean.getJKResident(), "KERALA", smartIncomeProtectBean.isKerlaDisc());
            KeralaCessServiceTax = kerlaServiceTax - basicServiceTax;
        } else {
            basicServiceTax = smartIncomeProtectBusinessLogic.getServiceTax(Double.parseDouble(premiumBasic_NotRounded), smartIncomeProtectBean.getJKResident(), "basic", smartIncomeProtectBean.isKerlaDisc());
            SBCServiceTax = smartIncomeProtectBusinessLogic.getServiceTax(Double.parseDouble(premiumBasic_NotRounded), smartIncomeProtectBean.getJKResident(), "SBC", smartIncomeProtectBean.isKerlaDisc());
            KKCServiceTax = smartIncomeProtectBusinessLogic.getServiceTax(Double.parseDouble(premiumBasic_NotRounded), smartIncomeProtectBean.getJKResident(), "KKC", smartIncomeProtectBean.isKerlaDisc());
        }


        String serviceTaxFirstYear = commonForAllProd.getStringWithout_E(basicServiceTax + SBCServiceTax + KKCServiceTax + kerlaServiceTax);
        System.out.println("serviceTaxFirstYear " + serviceTaxFirstYear);

        String premiumInstWithSTFirstYear = commonForAllProd.getStringWithout_E(Double.parseDouble(premiumBasic_NotRounded) + Double.parseDouble(serviceTaxFirstYear));
//		System.out.println("premiumInstWithSTFirstYear "+premiumInstWithSTFirstYear);
//		System.out.println("premiumBasic_NotRounded "+premiumBasic_NotRounded);


        //  Added By sujata on 19-02-2020 Start

        double basicServiceTaxSecondYear;
        double SBCServiceTaxSecondYear = 0;
        double KKCServiceTaxSecondYear = 0;

        double kerlaServiceTaxSecondYear = 0;
        double KeralaCessServiceTaxSecondYear = 0;

        if (isKerlaDisc) {
            basicServiceTaxSecondYear = smartIncomeProtectBusinessLogic.getServiceTaxSecondYear(Double.parseDouble(premiumBasic_NotRounded), smartIncomeProtectBean.getJKResident(), "basic", smartIncomeProtectBean.isKerlaDisc());
            kerlaServiceTaxSecondYear = smartIncomeProtectBusinessLogic.getServiceTaxSecondYear(Double.parseDouble(premiumBasic_NotRounded), smartIncomeProtectBean.getJKResident(), "KERALA", smartIncomeProtectBean.isKerlaDisc());
            KeralaCessServiceTaxSecondYear = kerlaServiceTaxSecondYear - basicServiceTaxSecondYear;
        } else {
            basicServiceTaxSecondYear = smartIncomeProtectBusinessLogic.getServiceTaxSecondYear(Double.parseDouble(premiumBasic_NotRounded), smartIncomeProtectBean.getJKResident(), "basic", smartIncomeProtectBean.isKerlaDisc());
            SBCServiceTaxSecondYear = smartIncomeProtectBusinessLogic.getServiceTaxSecondYear(Double.parseDouble(premiumBasic_NotRounded), smartIncomeProtectBean.getJKResident(), "SBC", smartIncomeProtectBean.isKerlaDisc());
            KKCServiceTaxSecondYear = smartIncomeProtectBusinessLogic.getServiceTaxSecondYear(Double.parseDouble(premiumBasic_NotRounded), smartIncomeProtectBean.getJKResident(), "KKC", smartIncomeProtectBean.isKerlaDisc());
        }

        String serviceTaxSecondYear = commonForAllProd.getStringWithout_E(basicServiceTaxSecondYear + SBCServiceTaxSecondYear + KKCServiceTaxSecondYear + kerlaServiceTaxSecondYear);

        String premiumInstWithSTSecondYear = commonForAllProd.getStringWithout_E(Double.parseDouble(premiumBasic_NotRounded) + Double.parseDouble(serviceTaxSecondYear));

        //added by sujata on 19-02-2020 for rider first year
        double basicServiceTaxRider;
        double SBCServiceTaxRider = 0;
        double KKCServiceTaxRider = 0;
        double kerlaServiceTaxRider = 0;
        double KeralaCessServiceTaxRider = 0;

        if (isKerlaDisc) {
            basicServiceTaxRider = smartIncomeProtectBusinessLogic.getServiceTax(Double.parseDouble(InstRider), smartIncomeProtectBean.getJKResident(), "basic", smartIncomeProtectBean.isKerlaDisc());
            kerlaServiceTaxRider = smartIncomeProtectBusinessLogic.getServiceTax(Double.parseDouble(InstRider), smartIncomeProtectBean.getJKResident(), "KERALA", smartIncomeProtectBean.isKerlaDisc());
            KeralaCessServiceTax = kerlaServiceTax - basicServiceTax;
        } else {
            basicServiceTaxRider = smartIncomeProtectBusinessLogic.getServiceTax(Double.parseDouble(InstRider), smartIncomeProtectBean.getJKResident(), "basic", smartIncomeProtectBean.isKerlaDisc());
            SBCServiceTaxRider = smartIncomeProtectBusinessLogic.getServiceTax(Double.parseDouble(InstRider), smartIncomeProtectBean.getJKResident(), "SBC", smartIncomeProtectBean.isKerlaDisc());
            KKCServiceTaxRider = smartIncomeProtectBusinessLogic.getServiceTax(Double.parseDouble(InstRider), smartIncomeProtectBean.getJKResident(), "KKC", smartIncomeProtectBean.isKerlaDisc());
        }

        String serviceTaxFirstYearRider = commonForAllProd.getStringWithout_E(basicServiceTaxRider + SBCServiceTaxRider + KKCServiceTaxRider + kerlaServiceTaxRider);
        //System.out.println("serviceTaxFirstYear "+serviceTaxFirstYear);

        String premiumInstWithSTFirstYearRider = commonForAllProd.getStringWithout_E(Double.parseDouble(InstRider) + Double.parseDouble(serviceTaxFirstYearRider));
        //System.out.println("premiumInstWithSTFirstYearRider "+premiumInstWithSTFirstYearRider);


        //End of riders first year


        //System.out.println("totalInstPremFirstYear " + totalInstPremFirstYear);totalInstPremFirstYear

        //String totalInstPreSecondtYear = commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(Double.parseDouble(premiumInstWithSTFirstYearRider) + Double.parseDouble(premiumInstWithSTFirstYear)));

        //added by sujata on 19-02-2020 for rider Second year
        double basicServiceTaxRiderSY;
        double SBCServiceTaxRiderSY = 0;
        double KKCServiceTaxRiderSY = 0;
        double kerlaServiceTaxRiderSY = 0;
        double KeralaCessServiceTaxRiderSY = 0;

        if (isKerlaDisc) {
            basicServiceTaxRiderSY = smartIncomeProtectBusinessLogic.getServiceTaxRiderSY(Double.parseDouble(InstRider), smartIncomeProtectBean.getJKResident(), "basic", smartIncomeProtectBean.isKerlaDisc());
            kerlaServiceTaxRiderSY = smartIncomeProtectBusinessLogic.getServiceTaxRiderSY(Double.parseDouble(InstRider), smartIncomeProtectBean.getJKResident(), "KERALA", smartIncomeProtectBean.isKerlaDisc());
        } else {
            basicServiceTaxRiderSY = smartIncomeProtectBusinessLogic.getServiceTaxRiderSY(Double.parseDouble(InstRider), smartIncomeProtectBean.getJKResident(), "basic", smartIncomeProtectBean.isKerlaDisc());
            SBCServiceTaxRiderSY = smartIncomeProtectBusinessLogic.getServiceTaxRiderSY(Double.parseDouble(InstRider), smartIncomeProtectBean.getJKResident(), "SBC", smartIncomeProtectBean.isKerlaDisc());
            KKCServiceTaxRiderSY = smartIncomeProtectBusinessLogic.getServiceTaxRiderSY(Double.parseDouble(InstRider), smartIncomeProtectBean.getJKResident(), "KKC", smartIncomeProtectBean.isKerlaDisc());
        }

        String serviceTaxSecondYearRiderSY = commonForAllProd.getStringWithout_E(basicServiceTaxRiderSY + SBCServiceTaxRiderSY + KKCServiceTaxRiderSY + kerlaServiceTaxRiderSY);
        //System.out.println("serviceTaxFirstYear "+serviceTaxFirstYear);

        String premiumInstWithSTSecondYearRiderSY = commonForAllProd.getStringWithout_E(Double.parseDouble(InstRider) + Double.parseDouble(serviceTaxSecondYearRiderSY));
        //System.out.println("premiumInstWithSTSecondYearRiderSY "+premiumInstWithSTSecondYearRiderSY);


        //End of riders

        String totalInstPremSecondYearSY = commonForAllProd.getStringWithout_E(Double.parseDouble(premiumInstWithSTSecondYearRiderSY) + Double.parseDouble(premiumInstWithSTSecondYear));
        //System.out.println("totalInstPremFirstYearsy " + totalInstPremFirstYearSY);
        //end

        String backDateinterestwithGST;
        if (rb_smart_income_protect_backdating_yes.isChecked()) {
            // "16-jan-2014")));
            String backDateinterest = commonForAllProd.getRoundUp(""
                    + (smartIncomeProtectBusinessLogic.getBackDateInterest(
                    Double.parseDouble(premiumInstWithSTFirstYear),
                    btn_smart_income_protect_backdatingdate.getText()
                            .toString())));

            backDateinterestwithGST = commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(
                    Double.parseDouble(backDateinterest) + (Double.parseDouble(backDateinterest) * prop.GSTforbackdate)));
        } else {
            backDateinterestwithGST = "0";
        }


        String totalInstPremFirstYear = commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(Double.parseDouble(premiumInstWithSTFirstYearRider) + Double.parseDouble(premiumInstWithSTFirstYear) + Double.parseDouble(backDateinterestwithGST)));
        premiumInstWithSTFirstYear = commonForAllProd
                .getRoundUp(commonForAllProd.getStringWithout_E(Double
                        .parseDouble(premiumInstWithSTFirstYear)
                        + Double.parseDouble(backDateinterestwithGST)));

        // {

        // Intent i = new Intent(getApplicationContext(),success.class);
        //
        // //Basic
        // //i.putExtra("op","Basic Premium is Rs " +
        // currencyFormat.format(Double.parseDouble(premiumBasic_NotRounded)));
        // i.putExtra("op",smartIncomeProtectBean.getPremFreq()
        // +" Basic Premium is Rs " +
        // decimalCurrencyFormat.format(Double.parseDouble(premiumBasic_NotRounded)));
        // //Total Result
        // i.putExtra("op1",smartIncomeProtectBean.getPremFreq()
        // +" Installment Premium without Applicable Taxes is Rs " +
        // currencyFormat.format(Double.parseDouble(premiumInst)));
        // i.putExtra("op2","Applicable Taxes is Rs "+
        // currencyFormat.format(Double.parseDouble(serviceTax)));
        // i.putExtra("op3",smartIncomeProtectBean.getPremFreq()
        // +" Installment Premium with Applicable Taxes is Rs " +
        // currencyFormat.format(Double.parseDouble(premiumInstWithST)));
        // //Maturity Benefit
        // i.putExtra("op4","Guaranteed Maturity Benefit in Installments is Rs "+
        // currencyFormat.format(Double.parseDouble(guaranMatBen)) );
        // i.putExtra("op5","Non-guaranteed Maturity Benefit With 4%pa is Rs " +
        // currencyFormat.format(Double.parseDouble(_nonGuaranMatBen_4Percent))
        // );
        // i.putExtra("op6","Non-guaranteed Maturity Benefit With 8%pa is Rs "+
        // currencyFormat.format(Double.parseDouble(_nonGuaranMatBen_8Percent))
        // );
        /* Added by Akshaya on 04-AUG-15 start ***/
        String staffStatus;
        if (cb_staffdisc.isChecked()) {
            staffStatus = "sbi";
            // disc_Basic_SelFreq
        } else
            staffStatus = "none";
        /* Added by Akshaya on 04-AUG-15 end ***/

        try {

            retVal.append("<?xml version='1.0' encoding='utf-8' ?><SmartIncomeProtect>");
            retVal.append("<errCode>0</errCode>");

            /* Added by Akshaya on 04-AUG-15 start ***/
            retVal.append("<staffStatus>").append(staffStatus).append("</staffStatus>");
            retVal.append("<staffRebate>").append(discountPercentage).append("</staffRebate>");
            retVal.append("<basicPrem>").append(premiumBasic_NotRounded).append("</basicPrem>");

            //added by sujata on 19-02-2020
            retVal.append("<InsrPremWithoutTx>").append(premiumInst).append("</InsrPremWithoutTx>");

            retVal.append("<InstRider>").append(InstRider).append("</InstRider>");
            //System.out.println("InstRider "+InstRider);
            //end
            retVal.append("<ServTx>").append(serviceTaxFirstYear).append("</ServTx>");

            retVal.append("<premInstBasicFirstYear>").append(commonForAllProd.getRoundOffLevel2(commonForAllProd.getStringWithout_E(Double.parseDouble(premiumInstWithSTFirstYear)))).append("</premInstBasicFirstYear>");
            retVal.append("<totalInstPremFirstYear>").append(commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(Double.parseDouble(totalInstPremFirstYear)))).append("</totalInstPremFirstYear>");
            retVal.append("<totalInstPremSecondYear>").append(commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(Double.parseDouble(totalInstPremSecondYearSY)))).append("</totalInstPremSecondYear>");

            retVal.append("<GuaMatBen>").append(guaranMatBen).append("</GuaMatBen>");
            retVal.append("<nonGua4pa>").append(_nonGuaranMatBen_4Percent).append("</nonGua4pa>");
            retVal.append("<nonGua8pa>").append(_nonGuaranMatBen_8Percent).append("</nonGua8pa>");
            retVal.append("<premPTA>").append(premiumPT).append("</premPTA>");
            retVal.append("<premADB>").append(premiumADB).append("</premADB>");
            retVal.append("<premATPDB>").append(premiumATPDB).append("</premATPDB>");
            retVal.append("<premCC13NonLinked>").append(premiumCC13NonLinked).append("</premCC13NonLinked>");

            retVal.append("<basicPremWithoutDisc>").append(basicPremWithoutDisc).append("</basicPremWithoutDisc>");
            retVal.append("<premPTAWithoutDisc>").append(premiumPTWithoutDisc).append("</premPTAWithoutDisc>");
            retVal.append("<premADBWithoutDisc>").append(premiumADBWithoutDisc).append("</premADBWithoutDisc>");
            retVal.append("<premATPDBWithoutDisc>").append(premiumATPDBWithoutDisc).append("</premATPDBWithoutDisc>");
            retVal.append("<premCC13WithoutDisc>").append(premiumCCNonLinkedWithoutDisc).append("</premCC13WithoutDisc>");
            retVal.append("<basicPremWithoutDiscSA>").append(basicPremWithoutDiscSA).append("</basicPremWithoutDiscSA>");

            retVal.append("<premPTAWithoutDiscSA>").append(premiumPTWithoutDiscSA).append("</premPTAWithoutDiscSA>");
            retVal.append("<premADBWithoutDiscSA>").append(premiumADBWithoutDiscSA).append("</premADBWithoutDiscSA>");
            retVal.append("<premATPDBWithoutDiscSA>").append(premiumATPDBWithoutDiscSA).append("</premATPDBWithoutDiscSA>");
            retVal.append("<premCC13WithoutDiscSA>").append(premiumCCNonLinkedWithoutDiscSA).append("</premCC13WithoutDiscSA>");
            retVal.append("<InstmntPrem>").append(InstmntPrem).append("</InstmntPrem>");
            retVal.append("<OccuInt>").append(minesOccuInterest).append("</OccuInt>");
            retVal.append("<backdateInt>").append(backDateinterestwithGST).append("</backdateInt>");
            retVal.append("<OccuIntServiceTax>").append(servicetax_MinesOccuInterest).append("</OccuIntServiceTax>");

            /* Added by Akshaya on 04-AUG-15 end ***/

            /* Added by Akshaya on 04-AUG-15 end ***/
            // PTA
            if (cb_bi_smart_income_protect_pt_rider.isChecked()) {
                // i.putExtra("op7","Preferred Term Assurance Rider Premium is Rs "+
                // currencyFormat.format(Double.parseDouble(commonForAllProd.getRoundUp(premiumPT))));
                // i.putExtra(
                // "op7",
                // "Preferred Term Assurance Rider Premium is Rs "
                // + decimalCurrencyFormat.format(Double
                // .parseDouble(premiumPT)));
                premPT = Double.parseDouble(premiumPT);
                retVal.append("<premPTA>").append(premPT).append("</premPTA>");
            }
            // ADB
            if (cb_bi_smart_income_protect_adb_rider.isChecked()) {
                // i.putExtra("op8","Accidental Death Benefit Rider Premium is Rs "+
                // currencyFormat.format(Double.parseDouble(commonForAllProd.getRoundUp(premiumADB))));
                // i.putExtra(
                // "op8",
                // "Accidental Death Benefit Rider Premium is Rs "
                // + decimalCurrencyFormat.format(Double
                // .parseDouble(premiumADB)));
                premADB = Double.parseDouble(premiumADB);
                retVal.append("<premADB>").append(premADB).append("</premADB>");
            }
            // ATPDB
            if (cb_bi_smart_income_protect_atpdb_rider.isChecked()) {
                // i.putExtra("op9","Accidental Total & Permanent Disability Benefit Rider Premium is Rs "+
                // currencyFormat.format(Double.parseDouble(commonForAllProd.getRoundUp(premiumATPDB))));
                // i.putExtra(
                // "op9",
                // "Accidental Total & Permanent Disability Benefit Rider Premium is Rs "
                // + decimalCurrencyFormat.format(Double
                // .parseDouble(premiumATPDB)));
                premATPDB = Double.parseDouble(premiumATPDB);
                retVal.append("<premATPDB>").append(premATPDB).append("</premATPDB>");
            }
            // CC 13 NonLinked
            if (cb_bi_smart_income_protect_cc13nl_rider.isChecked()) {
                // i.putExtra("op10","Criti Care13 Premium is Rs "+
                // currencyFormat.format(Double.parseDouble(commonForAllProd.getRoundUp(premiumCC13NonLinked))));
                // i.putExtra(
                // "op10",
                // "Criti Care13 Premium is Rs "
                // + decimalCurrencyFormat.format(Double
                // .parseDouble(premiumCC13NonLinked)));
                premCC13NonLinked = Double.parseDouble(premiumCC13NonLinked);
                retVal.append("<premCC13>").append(premCC13NonLinked).append("</premCC13>");
            }
            sumOfRiders = premPT + premADB + premATPDB + premCC13NonLinked;

            retVal.append("<yearlyPrem>").append(premiumBasic_Rounded).append("</yearlyPrem>").append(
                    // "<premInst>" + premiumInst
                    // + "</premInst>" + "<servcTax>" + serviceTax +
                    // "</servcTax>"
                    // + "<premWthST>" + premiumInstWithST + "</premWthST>"
                    "<premInst>"
            ).append(premiumInst).append("</premInst>").append("<servcTax>").append(serviceTaxFirstYear).append("</servcTax>").append("<premWthST>").append(premiumInstWithSTFirstYear).append("</premWthST>").append("<servcTaxSecondYear>").append(serviceTaxSecondYear).append("</servcTaxSecondYear>").append("<premWthSTSecondYear>").append(commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(Double.parseDouble(premiumInstWithSTSecondYear)))).append("</premWthSTSecondYear>").append("<sumOfRiders>").append(sumOfRiders).append("</sumOfRiders>").append("<InstWithSTFirstYearRider>").append(premiumInstWithSTFirstYearRider).append("</InstWithSTFirstYearRider>").append("<InstWithSTSecondYearRider>").append(premiumInstWithSTSecondYearRiderSY).append("</InstWithSTSecondYearRider>").append("<basicServiceTax>").append(commonForAllProd.getStringWithout_E(tax)).append("</basicServiceTax>").append("<SBCServiceTax>").append(commonForAllProd.getStringWithout_E(SBCServiceTax)).append("</SBCServiceTax>").append("<KKCServiceTax>").append(commonForAllProd.getStringWithout_E(KKCServiceTax)).append("</KKCServiceTax>").append("<basicServiceTaxSecondYear>").append(commonForAllProd
                    .getStringWithout_E(taxSecond)).append("</basicServiceTaxSecondYear>").append("<SBCServiceTaxSecondYear>").append(commonForAllProd
                    .getStringWithout_E(SBCServiceTaxSecondYear)).append("</SBCServiceTaxSecondYear>").append("<KKCServiceTaxSecondYear>").append(commonForAllProd
                    .getStringWithout_E(KKCServiceTaxSecondYear)).append("</KKCServiceTaxSecondYear>").append("<KeralaCessServiceTax>").append(commonForAllProd.getStringWithout_E(KeralaCessServiceTax)).append("</KeralaCessServiceTax>").append("<KeralaCessServiceTaxSecondYear>").append(commonForAllProd.getStringWithout_E(KeralaCessServiceTaxSecondYear)).append("</KeralaCessServiceTaxSecondYear>").append(bussIll.toString());
            retVal.append("</SmartIncomeProtect>");

            System.out.println("bussIllincome:" + retVal.toString());
            Log.e("retVal:", retVal.toString() + "");


        } catch (Exception e) {
            retVal.append("<?xml version='1.0' encoding='utf-8' ?><SmartIncomeProtect>" + "<errCode>1</errCode>" + "<errorMessage>").append(e.getMessage()).append("</errorMessage></SmartIncomeProtect>");
        }

        // }

        System.out.println("Final output in xml" + retVal.toString());
        // i.putExtra("pdf", retVal.toString());
        // startActivity(i);
        return true;
    }

    private void validationOfMoile_EmailId() {

        edt_smart_income_protect_contact_no
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
                        String abc = edt_smart_income_protect_contact_no
                                .getText().toString();
                        mobile_validation(abc);

                    }
                });

        edt_smart_income_protect_Email_id
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
                        /*ProposerEmailId = edt_smart_income_protect_Email_id
                                .getText().toString();*/
                        //email_id_validation(ProposerEmailId);

                    }
                });

        edt_smart_income_protect_ConfirmEmail_id
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
                        String proposer_confirm_emailId = edt_smart_income_protect_ConfirmEmail_id
                                .getText().toString();
                        //confirming_email_id(proposer_confirm_emailId);

                    }
                });

    }


    private void mobile_validation(String number) {
        boolean validationFlag3 = false;
        if ((number.length() != 10)) {
            edt_smart_income_protect_contact_no
                    .setError("Please provide correct 10-digit mobile number");
        } else if ((number.length() == 10)) {
        }
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
                    try {
                        File Photo = commonMethods.galleryAddPic(context);
                        Bitmap bmp = BitmapFactory.decodeFile(Photo.getAbsolutePath());

                        Bitmap b;
                        Uri imageUri;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            imageUri = commonMethods.getContentUri(context, new File(Photo.toString()));
                        } else {
                            imageUri = Uri.fromFile(new File(Photo.toString()));
                        }

                        b = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);

                        Bitmap mFaceBitmap = b != null ? b.copy(Bitmap.Config.RGB_565, true) : null;
                        b.recycle();
                        if (mFaceBitmap != null) {
                            Bitmap scaled = Bitmap.createScaledBitmap(bmp, 230,
                                    200, true);
                            photoBitmap = scaled;
                            imageButtonSmartIncomeProtectProposerPhotograph
                                    .setImageBitmap(scaled);
                        } else {
                            photoBitmap = null;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (Error e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        /* end */
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

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub

        if (id == DATE_DIALOG_ID) {
            return new DatePickerDialog(this, R.style.datepickerstyle,
                    mDateSetListener, mDay, mMonth, mYear);
        }

        return null;
    }

    @Override
    protected void onPrepareDialog(int id, Dialog dialog) {
        // TODO Auto-generated method stub
        if (id == DATE_DIALOG_ID) {
            ((DatePickerDialog) dialog).updateDate(mYear, mMonth, mDay);
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

        flg_focus = 1;
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
                    ProposerAge = final_age;
                    final_age = Integer.toString(age);
                    if (final_age.contains("-")) {
                        commonMethods.BICommonDialog(context, "Please fill Valid Birth Date");
                    } else {
                        if (18 <= age) {

                            btn_bi_smart_income_protect_proposer_date.setText(date);
                            // edt_bi_smart_income_protect_life_assured_age
                            // .setText(final_age);
                            proposer_date_of_birth = getDate1(date + "");
                            textviewProposerAge
                                    .setText(final_age);
                            clearFocusable(btn_bi_smart_income_protect_proposer_date);
                            setFocusable(edt_smart_income_protect_contact_no);
                            edt_smart_income_protect_contact_no.requestFocus();

                        } else {
                            commonMethods.BICommonDialog(context, "Minimum age should be 18 yrs for proposer");
                            btn_bi_smart_income_protect_proposer_date
                                    .setText("Select Date");
                            textviewProposerAge.setText("");
                            proposer_date_of_birth = "";
                            clearFocusable(btn_bi_smart_income_protect_proposer_date);
                            setFocusable(btn_bi_smart_income_protect_proposer_date);
                            btn_bi_smart_income_protect_proposer_date
                                    .requestFocus();
                        }
                    }
                    break;

                case 5:

                    final_age = Integer.toString(age);
                    if (final_age.contains("-")) {
                        commonMethods.BICommonDialog(context, "Please fill Valid Birth Date");
                    } else {
                        if (spnr_bi_smart_income_protect_policyterm
                                .getSelectedItem().toString().equals("7")) {
                            minAge = 11;
                            maxAge = 58;
                        } else if (spnr_bi_smart_income_protect_policyterm
                                .getSelectedItem().toString().equals("12")) {
                            minAge = 8;
                            maxAge = 53;
                        } else if (spnr_bi_smart_income_protect_policyterm
                                .getSelectedItem().toString().equals("15")) {
                            minAge = 8;
                            maxAge = 50;
                        }

                        if (minAge <= age && age <= maxAge) {

                            if (Integer.parseInt(final_age) < 18) {

                                tr_bi_smart_income_protect_pt_rider_checkbox
                                        .setVisibility(View.GONE);
                                tr_bi_smart_income_protect_adb_rider_checkbox
                                        .setVisibility(View.GONE);
                                tr_bi_smart_income_protect_atpd_rider_checkbox
                                        .setVisibility(View.GONE);
                                tr_bi_smart_income_protect_cc13nl_rider_checkbox
                                        .setVisibility(View.GONE);
                                tr_PT_rider.setVisibility(View.GONE);
                                tr_ADB_rider.setVisibility(View.GONE);
                                tr_ATPDB_Rider.setVisibility(View.GONE);
                                tr_CC13NL_Rider.setVisibility(View.GONE);
                                cb_bi_smart_income_protect_pt_rider
                                        .setChecked(false);
                                cb_bi_smart_income_protect_adb_rider
                                        .setChecked(false);
                                cb_bi_smart_income_protect_atpdb_rider
                                        .setChecked(false);
                                cb_bi_smart_income_protect_cc13nl_rider
                                        .setChecked(false);

                                proposer_Is_Same_As_Life_Assured = "N";

                                linearlayoutProposerDetails
                                        .setVisibility(View.VISIBLE);

                                proposer_Title = "";
                                spnr_bi_smart_income_protect_proposer_title
                                        .setSelection(0);
                                proposer_First_Name = "";
                                edt_bi_smart_income_protect_proposer_first_name
                                        .setText("");
                                proposer_Middle_Name = "";
                                edt_bi_smart_income_protect_proposer_middle_name
                                        .setText("");
                                proposer_Last_Name = "";
                                edt_bi_smart_income_protect_proposer_last_name
                                        .setText("");
                                proposer_date_of_birth = "";
                                btn_bi_smart_income_protect_proposer_date
                                        .setText("Select Date");

                                lifeAssuredAge = final_age;
                                btn_bi_smart_income_protect_life_assured_date
                                        .setText(date);
                                edt_bi_smart_income_protect_life_assured_age
                                        .setText(final_age);
                                lifeAssured_date_of_birth = getDate1(date + "");

                                if (!proposer_Backdating_BackDate.equals("")) {
                                    if (proposer_Backdating_WishToBackDate_Policy
                                            .equalsIgnoreCase("Y")) {
                                        rb_smart_income_protect_backdating_no
                                                .setChecked(true);
                                        ll_smart_income_protect_backdating
                                                .setVisibility(View.GONE);
                                    }
                                    proposer_Backdating_BackDate = "";
                                    btn_smart_income_protect_backdatingdate
                                            .setText("Select Date");
                                }

                            } else {
                                tr_bi_smart_income_protect_pt_rider_checkbox
                                        .setVisibility(View.VISIBLE);
                                tr_bi_smart_income_protect_adb_rider_checkbox
                                        .setVisibility(View.VISIBLE);
                                tr_bi_smart_income_protect_atpd_rider_checkbox
                                        .setVisibility(View.VISIBLE);
                                //tr_bi_smart_income_protect_cc13nl_rider_checkbox.setVisibility(View.VISIBLE);

                                proposer_Is_Same_As_Life_Assured = "Y";
                                linearlayoutProposerDetails
                                        .setVisibility(View.GONE);

                                lifeAssuredAge = final_age;
                                btn_bi_smart_income_protect_life_assured_date
                                        .setText(date);
                                edt_bi_smart_income_protect_life_assured_age
                                        .setText(final_age);
                                lifeAssured_date_of_birth = getDate1(date + "");

                                if (!proposer_Backdating_BackDate.equals("")) {
                                    if (proposer_Backdating_WishToBackDate_Policy
                                            .equalsIgnoreCase("Y")) {
                                        rb_smart_income_protect_backdating_no
                                                .setChecked(true);
                                        ll_smart_income_protect_backdating
                                                .setVisibility(View.GONE);
                                    }
                                    proposer_Backdating_BackDate = "";
                                    btn_smart_income_protect_backdatingdate
                                            .setText("Select Date");
                                }
                            }

                            if (Integer.parseInt(final_age) < 18) {
                                clearFocusable(btn_bi_smart_income_protect_life_assured_date);
                                setFocusable(spnr_bi_smart_income_protect_proposer_title);
                                spnr_bi_smart_income_protect_proposer_title
                                        .requestFocus();
                            } else {

                                clearFocusable(edt_bi_smart_income_protect_life_assured_last_name);
                                setFocusable(edt_smart_income_protect_contact_no);
                                edt_smart_income_protect_contact_no.requestFocus();
                            }

                        } else {
                            commonMethods.BICommonDialog(context, "Minimum Age should be " + minAge
                                    + "yrs and Maximum Age should be " + maxAge
                                    + " yrs For LifeAssured");
                            btn_bi_smart_income_protect_life_assured_date
                                    .setText("Select Date");
                            edt_bi_smart_income_protect_life_assured_age
                                    .setText("");
                            lifeAssured_date_of_birth = "";

                            clearFocusable(btn_bi_smart_income_protect_life_assured_date);
                            setFocusable(btn_bi_smart_income_protect_life_assured_date);
                            btn_bi_smart_income_protect_life_assured_date
                                    .requestFocus();
                        }
                    }
                    break;

                case 6:
                    if (age >= 0) {
                        proposer_Backdating_BackDate = date + "";
                        btn_smart_income_protect_backdatingdate
                                .setText(proposer_Backdating_BackDate);
                        clearFocusable(btn_smart_income_protect_backdatingdate);

                        setFocusable(cb_bi_smart_income_protect_pt_rider);
                        cb_bi_smart_income_protect_pt_rider.requestFocus();

                    } else {
                        commonMethods.dialogWarning(context, "Please Select Valid BackDating Date", true);
                        btn_smart_income_protect_backdatingdate
                                .setText("Select Date");
                        proposer_Backdating_BackDate = "";
                    }

                    break;

                default:
                    break;

            }

        }

        if (proposer_Backdating_WishToBackDate_Policy.equalsIgnoreCase("Y")
                && !proposer_Backdating_BackDate.equals("")) {

            if (id != 4) {

                int Proposerage = calculateMyAge(mYear, Integer.parseInt(mont),
                        Integer.parseInt(day));
                String str_final_Age = Integer.toString(Proposerage);
                // ageInYears.setSelection(getIndex(ageInYears, str_final_Age),
                // false);

                if (valBackdatingAge(Proposerage)) {
                    edt_bi_smart_income_protect_life_assured_age
                            .setText(str_final_Age);

                    if (Integer.parseInt(str_final_Age) < 18) {

                        tr_bi_smart_income_protect_pt_rider_checkbox
                                .setVisibility(View.GONE);
                        tr_bi_smart_income_protect_adb_rider_checkbox
                                .setVisibility(View.GONE);
                        tr_bi_smart_income_protect_atpd_rider_checkbox
                                .setVisibility(View.GONE);
                        //tr_bi_smart_income_protect_cc13nl_rider_checkbox.setVisibility(View.GONE);
                        tr_PT_rider.setVisibility(View.GONE);
                        tr_ADB_rider.setVisibility(View.GONE);
                        tr_ATPDB_Rider.setVisibility(View.GONE);
                        tr_CC13NL_Rider.setVisibility(View.GONE);
                        cb_bi_smart_income_protect_pt_rider.setChecked(false);
                        cb_bi_smart_income_protect_adb_rider.setChecked(false);
                        cb_bi_smart_income_protect_atpdb_rider.setChecked(false);
                        cb_bi_smart_income_protect_cc13nl_rider.setChecked(false);

                        linearlayoutProposerDetails
                                .setVisibility(View.VISIBLE);

                        proposer_Is_Same_As_Life_Assured = "N";

                        // proposer_date_of_birth = "";
                    } else {
                        tr_bi_smart_income_protect_pt_rider_checkbox
                                .setVisibility(View.VISIBLE);
                        tr_bi_smart_income_protect_adb_rider_checkbox
                                .setVisibility(View.VISIBLE);
                        tr_bi_smart_income_protect_atpd_rider_checkbox
                                .setVisibility(View.VISIBLE);
                        //tr_bi_smart_income_protect_cc13nl_rider_checkbox.setVisibility(View.VISIBLE);

                        linearlayoutProposerDetails
                                .setVisibility(View.GONE);

                        proposer_Is_Same_As_Life_Assured = "Y";
                    }
                } else {
                    commonMethods.dialogWarning(context, "Please Select Valid BackDating Date", true);
                    btn_smart_income_protect_backdatingdate
                            .setText("Select Date");
                    proposer_Backdating_BackDate = "";
                }
            }
        }

    }

    private void getInput(SmartIncomeProtectBean smartIncomeProtectBean) {
        inputVal = new StringBuilder();

        String proposer_title = "";
        String proposer_firstName = "";
        String proposer_middleName = "";
        String proposer_lastName = "";
        String proposer_DOB = "";
        String proposer_age = "";

        if (!spnr_bi_smart_income_protect_proposer_title.getSelectedItem()
                .toString().equals("")
                && !spnr_bi_smart_income_protect_proposer_title
                .getSelectedItem().toString().equals("Select Title")) {
            proposer_title = spnr_bi_smart_income_protect_proposer_title
                    .getSelectedItem().toString();

        }
        if (!edt_bi_smart_income_protect_proposer_first_name.getText()
                .toString().equals(""))
            proposer_firstName = edt_bi_smart_income_protect_proposer_first_name
                    .getText().toString().trim();

        if (!edt_bi_smart_income_protect_proposer_middle_name.getText()
                .toString().equals(""))
            proposer_middleName = edt_bi_smart_income_protect_proposer_middle_name
                    .getText().toString().trim();
        if (!edt_bi_smart_income_protect_proposer_last_name.getText()
                .toString().equals(""))
            proposer_lastName = edt_bi_smart_income_protect_proposer_last_name
                    .getText().toString().trim();

        if (!btn_bi_smart_income_protect_proposer_date.getText().toString()
                .equals("Select Date")) {
            Calendar present_date = Calendar.getInstance();
            int tDay = present_date.get(Calendar.DAY_OF_MONTH);
            int tMonth = present_date.get(Calendar.MONTH);
            int tYear = present_date.get(Calendar.YEAR);
            proposer_DOB = btn_bi_smart_income_protect_proposer_date.getText()
                    .toString();
            proposer_age = commonForAllProd.calculateMyAge(tYear, tMonth + 1,
                    tDay, getDate1(proposer_DOB)) + "";
        }

        String wish_to_backdate_flag;
        if (rb_smart_income_protect_backdating_yes.isChecked())
            wish_to_backdate_flag = "y";
        else
            wish_to_backdate_flag = "n";
        String backdate;
        if (wish_to_backdate_flag.equals("y"))
            backdate = btn_smart_income_protect_backdatingdate.getText()
                    .toString();
        else
            backdate = "";

        String gender = smartIncomeProtectBean.getGender();
        int basicPolicyTerm = smartIncomeProtectBean.getBasicTerm();
        int ptTerm = smartIncomeProtectBean.getPreferredTerm();
        int adbTerm = smartIncomeProtectBean.getADB_Term();
        int atpdbTerm = smartIncomeProtectBean.getATPDB_Term();
        int cc13nlTerm = smartIncomeProtectBean.getCc13nlTerm();

        double basicSumAssured = smartIncomeProtectBean.getBasicSA();
        double ptSumAssured = smartIncomeProtectBean.getPreferredSA();
        double adbSumAssured = smartIncomeProtectBean.getADB_SA();
        double atpdbSumAssured = smartIncomeProtectBean.getATPDB_SA();
        double cc13nlSumAssured = smartIncomeProtectBean.getCc13nlSA();
        String PremPayingMode = smartIncomeProtectBean.getPremFreq();

        boolean isJKresident = smartIncomeProtectBean.getJKResident();
        boolean isStaffOrNot = smartIncomeProtectBean.getStaffDisc();
        boolean statusPT = smartIncomeProtectBean.getPreferredStatus();
        boolean statusADB = smartIncomeProtectBean.getADB_Status();
        boolean statusATPDB = smartIncomeProtectBean.getATPDB_Status();
        boolean statusCC13NL = smartIncomeProtectBean.getCc13nlStatus();
        boolean smokerOrNot;

        String is_Smoker_or_Not = "";
        smokerOrNot = is_Smoker_or_Not.equalsIgnoreCase("Smoker");

        inputVal.append("<?xml version='1.0' encoding='utf-8' ?><smartincomeprotect>");

        inputVal.append("<LifeAssured_title>").append(lifeAssured_Title).append("</LifeAssured_title>");
        inputVal.append("<LifeAssured_firstName>").append(lifeAssured_First_Name).append("</LifeAssured_firstName>");
        inputVal.append("<LifeAssured_middleName>").append(lifeAssured_Middle_Name).append("</LifeAssured_middleName>");
        inputVal.append("<LifeAssured_lastName>").append(lifeAssured_Last_Name).append("</LifeAssured_lastName>");
        inputVal.append("<LifeAssured_DOB>").append(lifeAssured_date_of_birth).append("</LifeAssured_DOB>");
        inputVal.append("<LifeAssured_age>").append(lifeAssuredAge).append("</LifeAssured_age>");
        inputVal.append("<gender>").append(gender).append("</gender>");

        inputVal.append("<proposer_title>").append(proposer_title).append("</proposer_title>");
        inputVal.append("<proposer_firstName>").append(proposer_firstName).append("</proposer_firstName>");
        inputVal.append("<proposer_middleName>").append(proposer_middleName).append("</proposer_middleName>");
        inputVal.append("<proposer_lastName>").append(proposer_lastName).append("</proposer_lastName>");
        inputVal.append("<proposer_DOB>").append(proposer_DOB).append("</proposer_DOB>");
        inputVal.append("<proposer_age>").append(proposer_age).append("</proposer_age>");
        inputVal.append("<proposer_gender>").append(propsoser_gender).append("</proposer_gender>");

        inputVal.append("<product_name>").append(planName).append("</product_name>");
        /* parivartan changes */
        inputVal.append("<product_Code>").append(product_Code).append("</product_Code>");
        inputVal.append("<product_UIN>").append(product_UIN).append("</product_UIN>");
        inputVal.append("<product_cateogory>").append(product_cateogory).append("</product_cateogory>");
        inputVal.append("<product_type>").append(product_type).append("</product_type>");
        /* end */

        inputVal.append("<proposer_Is_Same_As_Life_Assured>").append(proposer_Is_Same_As_Life_Assured).append("</proposer_Is_Same_As_Life_Assured>");

        inputVal.append("<isJKResident>").append(isJKresident).append("</isJKResident>");
        inputVal.append("<isStaff>").append(isStaffOrNot).append("</isStaff>");
        inputVal.append("<isSmoker>").append(smokerOrNot).append("</isSmoker>");
        inputVal.append("<age>").append(edt_bi_smart_income_protect_life_assured_age.getText()
                .toString()).append("</age>");

        inputVal.append("<policyTerm>").append(basicPolicyTerm).append("</policyTerm>");
        inputVal.append("<isPTRider>").append(statusPT).append("</isPTRider>");
        inputVal.append("<isADBRider>").append(statusADB).append("</isADBRider>");
        inputVal.append("<isATPDBRider>").append(statusATPDB).append("</isATPDBRider>");
        inputVal.append("<isCC13NLRider>").append(statusCC13NL).append("</isCC13NLRider>");

        inputVal.append("<ptTerm>").append(ptTerm).append("</ptTerm>");
        inputVal.append("<adbTerm>").append(adbTerm).append("</adbTerm>");
        inputVal.append("<atpdbTerm>").append(atpdbTerm).append("</atpdbTerm>");
        inputVal.append("<cc13nlTerm>").append(cc13nlTerm).append("</cc13nlTerm>");

        inputVal.append("<premFreq>").append(PremPayingMode).append("</premFreq>");
        inputVal.append("<sumAssured>").append(basicSumAssured).append("</sumAssured>");
        inputVal.append("<ptSA>").append(ptSumAssured).append("</ptSA>");
        inputVal.append("<adbSA>").append(adbSumAssured).append("</adbSA>");
        inputVal.append("<atpdbSA>").append(atpdbSumAssured).append("</atpdbSA>");
        inputVal.append("<cc13nlSA>").append(cc13nlSumAssured).append("</cc13nlSA>");
        inputVal.append("<Wish_to_backdate_policy>").append(wish_to_backdate_flag).append("</Wish_to_backdate_policy>");
        inputVal.append("<backdating_Date>").append(backdate).append("</backdating_Date>");


        //Added Tushar Kadam Kerla Applicable Taxes 7-Jun-2019
        String str_kerla_discount = "N";
        if (cb_kerladisc.isChecked()) {
            str_kerla_discount = "Y";
        }

        inputVal.append("<KFC>").append(str_kerla_discount).append("</KFC>");
        inputVal.append("<mat_benefit_lump_sum>").append(lumpSumAmount).append("</mat_benefit_lump_sum>");
        //End Added Tushar Kadam Kerla Applicable Taxes 7-Jun-2019

        inputVal.append("</smartincomeprotect>");

    }


    private void Dialog() {

        final Dialog d = new Dialog(this);
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);
        d.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.BLACK));

        d.setContentView(R.layout.layout_smart_income_protect_bi_grid);

        TextView tv_proposername = d
                .findViewById(R.id.tv_proposername);
        TextView tv_proposal_number = d
                .findViewById(R.id.tv_proposal_number);
        TextView tv_proposal_channel = d
                .findViewById(R.id.tv_proposal_channel);
        TextView tv_bi_smart_income_protect_life_assured_state = d
                .findViewById(R.id.tv_bi_smart_income_protect_life_assured_state);
        TextView tv_rate_of_taxes = d
                .findViewById(R.id.tv_rate_of_taxes);


        TextView tv_bi_smart_income_protect_life_assured_name = d
                .findViewById(R.id.tv_bi_smart_income_protect_life_assured_name);
        TextView tv_bi_smart_income_protect_life_assured_name2 = d
                .findViewById(R.id.tv_bi_smart_income_protect_life_assured_name2);
        TextView tv_bi_smart_income_protect_life_assured_age = d
                .findViewById(R.id.tv_bi_smart_income_protect_life_assured_age);
        TextView tv_bi_smart_income_protect_life_assured_age2 = d
                .findViewById(R.id.tv_bi_smart_income_protect_life_assured_age2);
        TextView tv_bi_smart_income_protect_life_assured_gender = d
                .findViewById(R.id.tv_bi_smart_income_protect_life_assured_gender);
        TextView tv_bi_smart_income_protect_life_assured_gender2 = d
                .findViewById(R.id.tv_bi_smart_income_protect_life_assured_gender2);
        TextView tv_bi_smart_income_protect_life_assured_premium_frequency = d
                .findViewById(R.id.tv_bi_smart_income_protect_life_assured_premium_frequency);

        TextView tv_bi_smart_income_protect_term = d
                .findViewById(R.id.tv_bi_smart_income_protect_term);
        TextView tv_bi_smart_income_protect_premium_paying_term = d
                .findViewById(R.id.tv_bi_smart_income_protect_premium_paying_term);
        TextView tv_bi_smart_income_protect_sum_assured = d
                .findViewById(R.id.tv_bi_smart_income_protect_sum_assured);

        TextView tv_bi_smart_income_protect_maturity_ben_option = d
                .findViewById(R.id.tv_bi_smart_income_protect_maturity_ben_option);


        TextView tv_bi_smart_income_protect_sum_assured_on_death = d
                .findViewById(R.id.tv_bi_smart_income_protect_sum_assured_on_death);

        TextView tv_bi_smart_income_protect_yearly_premium = d
                .findViewById(R.id.tv_bi_smart_income_protect_yearly_premium);

        TextView tv_bi_smart_income_protect_basic_premium = d
                .findViewById(R.id.tv_bi_smart_income_protect_basic_premium);
        TextView tv_bi_smart_income_protect_service_tax = d
                .findViewById(R.id.tv_bi_smart_income_protect_service_tax);
        TextView tv_bi_smart_income_protect_yearly_premium_with_tax = d
                .findViewById(R.id.tv_bi_smart_income_protect_yearly_premium_with_tax);

        // First year policy
        TextView tv_bi_smart_income_protect_basic_premium_first_year = d
                .findViewById(R.id.tv_bi_smart_income_protect_basic_premium_first_year);
        TextView tv_bi_smart_income_protect_service_tax_first_year = d
                .findViewById(R.id.tv_bi_smart_income_protect_service_tax_first_year);
        TextView tv_bi_smart_income_protect_yearly_premium_with_tax_first_year = d
                .findViewById(R.id.tv_bi_smart_income_protect_yearly_premium_with_tax_first_year);

        // Seconf year policy onwards
        TextView tv_bi_smart_income_protect_basic_premium_second_year = d
                .findViewById(R.id.tv_bi_smart_income_protect_basic_premium_second_year);

        TextView tv_bi_smart_income_protect_basic_premium_second_year2 = d
                .findViewById(R.id.tv_bi_smart_income_protect_basic_premium_second_year2);
        TextView tv_bi_smart_income_protect_service_tax_second_year = d
                .findViewById(R.id.tv_bi_smart_income_protect_service_tax_second_year);
        TextView tv_bi_smart_income_protect_service_tax_second_year2 = d
                .findViewById(R.id.tv_bi_smart_income_protect_service_tax_second_year2);
        TextView tv_bi_smart_income_protect_yearly_premium_with_tax_second_year = d
                .findViewById(R.id.tv_bi_smart_income_protect_yearly_premium_with_tax_second_year);
        TextView tv_bi_smart_income_protect_yearly_premium_with_tax_second_year2 = d
                .findViewById(R.id.tv_bi_smart_income_protect_yearly_premium_with_tax_second_year2);

        TextView tv_bi_smart_income_protect_backdating_interest = d
                .findViewById(R.id.tv_bi_smart_income_protect_backdating_interest);

        TableRow tr_smart_income_protect_surrender_value = d
                .findViewById(R.id.tr_smart_income_protect_surrender_value);
        final TextView edt_proposer_name = d
                .findViewById(R.id.edt_ProposalName);
        GridView gv_userinfo = d.findViewById(R.id.gv_userinfo_new);
        final EditText edt_Policyholderplace = d
                .findViewById(R.id.edt_Policyholderplace);

        final TextView tv_premium_install_type = d
                .findViewById(R.id.tv_premium_install_rider_type);
        final TextView tv_premium_install_type1 = d
                .findViewById(R.id.tv_premium_install_rider_type1);
        final TextView tv_mandatory_bi_smart_income_protect_yearly_premium_with_tax1 = d
                .findViewById(R.id.tv_mandatory_bi_smart_income_protect_yearly_premium_with_tax1);

        final TextView tv_premium_type = d
                .findViewById(R.id.tv_premium_type);

        final TextView tv_premium_type1 = d
                .findViewById(R.id.tv_premium_type1);
        final TextView tv_premium_type2 = d
                .findViewById(R.id.tv_premium_type2);
        final TextView tv_premium_type3 = d
                .findViewById(R.id.tv_premium_type3);
        final EditText edt_MarketingOfficalPlace = d
                .findViewById(R.id.edt_MarketingOfficalPlace);
        LinearLayout ll_basic_pta_rider = d
                .findViewById(R.id.ll_basic_pta_rider);
        LinearLayout ll_basic_rider = d
                .findViewById(R.id.ll_basic_rider);
        LinearLayout ll_basic_adb_rider = d
                .findViewById(R.id.ll_basic_adb_rider);
        LinearLayout ll_basic_atpdb_rider = d
                .findViewById(R.id.ll_basic_atpdb_rider);
        LinearLayout ll_basic_cc13nl_rider = d
                .findViewById(R.id.ll_basic_cc13nl_rider);

        TextView tv_premium_type_rider = d
                .findViewById(R.id.tv_premium_type_rider);

        TextView tv_bi_smart_income_protect_premium_paying_term_pta = d
                .findViewById(R.id.tv_bi_smart_income_protect_premium_paying_term_pta);


        TextView tv_bi_smart_income_protect_premium_paying_term_pta2 = d
                .findViewById(R.id.tv_bi_smart_income_protect_premium_paying_term_pta2);
        TextView tv_bi_smart_income_protect_premium_paying_term_adb2 = d
                .findViewById(R.id.tv_bi_smart_income_protect_premium_paying_term_adb2);
        TextView tv_bi_smart_income_protect_premium_paying_term_adptb2 = d
                .findViewById(R.id.tv_bi_smart_income_protect_premium_paying_term_adptb2);


        TextView tv_bi_smart_income_protect_sum_assured_pta = d
                .findViewById(R.id.tv_bi_smart_income_protect_sum_assured_pta);
        TextView tv_bi_smart_income_protect_yearly_premium_pta = d
                .findViewById(R.id.tv_bi_smart_income_protect_yearly_premium_pta);

        TextView tv_bi_smart_income_protect_premium_paying_term_adb = d
                .findViewById(R.id.tv_bi_smart_income_protect_premium_paying_term_adb);
        TextView tv_bi_smart_income_protect_sum_assured_adb = d
                .findViewById(R.id.tv_bi_smart_income_protect_sum_assured_adb);
        TextView tv_bi_smart_income_protect_yearly_premium_adb = d
                .findViewById(R.id.tv_bi_smart_income_protect_yearly_premium_adb);

        TextView tv_bi_smart_income_protect_premium_paying_term_adptb = d
                .findViewById(R.id.tv_bi_smart_income_protect_premium_paying_term_adptb);
        TextView tv_bi_smart_income_protect_sum_assured_adptb = d
                .findViewById(R.id.tv_bi_smart_income_protect_sum_assured_adptb);
        TextView tv_bi_smart_income_protect_yearly_premium_adptb = d
                .findViewById(R.id.tv_bi_smart_income_protect_yearly_premium_adptb);

        TextView tv_bi_smart_income_protect_premium_paying_term_cc13nl = d
                .findViewById(R.id.tv_bi_smart_income_protect_premium_paying_term_cc13nl);
        TextView tv_bi_smart_income_protect_sum_assured_cc13nl = d
                .findViewById(R.id.tv_bi_smart_income_protect_sum_assured_cc13nl);
        TextView tv_bi_smart_income_protect_yearly_premium_cc13nl = d
                .findViewById(R.id.tv_bi_smart_income_protect_yearly_premium_cc13nl);

        TextView tv_bi_is_Staff = d
                .findViewById(R.id.tv_bi_is_Staff);
        TextView tv_bi_is_jk = d.findViewById(R.id.tv_bi_is_jk);

        TextView tv_bi_smart_income_protect_basic_service_tax_first_year = d
                .findViewById(R.id.tv_bi_smart_income_protect_basic_service_tax_first_year);
        TextView tv_bi_smart_income_protect_swachh_bharat_cess_first_year = d
                .findViewById(R.id.tv_bi_smart_income_protect_swachh_bharat_cess_first_year);
        TextView tv_bi_smart_income_protect_krishi_kalyan_cess_first_year = d
                .findViewById(R.id.tv_bi_smart_income_protect_krishi_kalyan_cess_first_year);

        TextView tv_bi_smart_income_protect_basic_service_tax_second_year = d
                .findViewById(R.id.tv_bi_smart_income_protect_basic_service_tax_second_year);
        TextView tv_bi_smart_income_protect_swachh_bharat_cess_second_year = d
                .findViewById(R.id.tv_bi_smart_income_protect_swachh_bharat_cess_second_year);
        TextView tv_bi_smart_income_protect_krishi_kalyan_cess_second_year = d
                .findViewById(R.id.tv_bi_smart_income_protect_krishi_kalyan_cess_second_year);

        TextView tv_uin_smart_income_protect = d
                .findViewById(R.id.tv_uin_smart_income_protect);

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

        /* parivartan changes */
        imageButtonSmartIncomeProtectProposerPhotograph = d
                .findViewById(R.id.imageButtonSmartIncomeProtectProposerPhotograph);
        imageButtonSmartIncomeProtectProposerPhotograph
                .setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Check = "Photo";
                        commonMethods.windowmessage(context, "_cust1Photo.jpg");
                    }
                });
        /* end */

        TableRow tr_second_year = d
                .findViewById(R.id.tr_second_year);


        LinearLayout ll_endowment = d
                .findViewById(R.id.ll_endowment);
        LinearLayout ll_endowment_with_option = d
                .findViewById(R.id.ll_endowment_with_option);
        Button btn_proceed = d.findViewById(R.id.btn_proceed);

        Ibtn_signatureofMarketing = d
                .findViewById(R.id.Ibtn_signatureofMarketing);
        Ibtn_signatureofPolicyHolders = d
                .findViewById(R.id.Ibtn_signatureofPolicyHolders);

        btn_MarketingOfficalDate = d
                .findViewById(R.id.btn_MarketingOfficalDate);
        btn_PolicyholderDate = d
                .findViewById(R.id.btn_PolicyholderDate);
        Calendar calender = Calendar.getInstance();
        int Year = calender.get(Calendar.YEAR);
        int Month = calender.get(Calendar.MONTH);
        int Day = calender.get(Calendar.DAY_OF_MONTH);
        list_data.clear();

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

        String input = inputVal.toString();

        age_entry = prsObj.parseXmlTag(input, "age");
        if (proposer_Is_Same_As_Life_Assured.equalsIgnoreCase("y")) {
            edt_proposer_name
                    .setText("I, "
                            + name_of_life_assured
                            + ", having received the information with respect to the above, have understood the above statement before entering into the contract.");
            edt_proposer_name_need_analysis
                    .setText("I, "
                            + name_of_life_assured
                            + ", have undergone the Need Analysis after having reviewed the SBI Life Product options, I have opted for SBI LIFE - Smart Income Protect.");
            tv_proposername.setText(name_of_life_assured);
            tv_bi_smart_income_protect_life_assured_name
                    .setText(name_of_life_assured);
            tv_bi_smart_income_protect_life_assured_name2.setText(name_of_life_assured);
            tv_bi_smart_income_protect_life_assured_age.setText(age_entry
                    + "");
            tv_bi_smart_income_protect_life_assured_age2.setText(age_entry
                    + "");

            tv_bi_smart_income_protect_life_assured_gender.setText(gender);
            tv_bi_smart_income_protect_life_assured_gender2.setText(gender);
        } else if (proposer_Is_Same_As_Life_Assured.equalsIgnoreCase("n")) {
            edt_proposer_name
                    .setText("I, "
                            + name_of_proposer
                            + ", having received the information with respect to the above, have understood the above statement before entering into the contract.");
            edt_proposer_name_need_analysis
                    .setText("I, "
                            + name_of_proposer
                            + ", have undergone the Need Analysis after having reviewed the SBI Life Product options, I have opted for SBI LIFE - Smart Income Protect.");
            tv_proposername.setText(name_of_proposer);
            tv_bi_smart_income_protect_life_assured_name
                    .setText(name_of_proposer);
            tv_bi_smart_income_protect_life_assured_name2
                    .setText(name_of_life_assured);

            tv_bi_smart_income_protect_life_assured_age.setText(ProposerAge
                    + "");
            tv_bi_smart_income_protect_life_assured_age2.setText(age_entry
                    + "");
            tv_bi_smart_income_protect_life_assured_gender.setText(propsoser_gender);

            tv_bi_smart_income_protect_life_assured_gender2.setText(gender);
        }


        tv_proposal_number.setText(QuatationNumber);
        tv_proposal_channel.setText(userType);
        if (cb_kerladisc.isChecked()) {
            tv_bi_smart_income_protect_life_assured_state.setText("Kerala");
            tv_rate_of_taxes.setText("4.75% in the 1st policy year and 2.375% from 2nd policy year onwards");
        } else {
            tv_bi_smart_income_protect_life_assured_state.setText("Non Kerala");
            tv_rate_of_taxes.setText("4.5% in the 1st policy year and 2.25% from 2nd policy year onwards");
        }

        /* end */

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


        if (photoBitmap != null) {
            imageButtonSmartIncomeProtectProposerPhotograph
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
                            imageButtonSmartIncomeProtectProposerPhotograph
                                    .setImageDrawable(getResources()
                                            .getDrawable(
                                                    R.drawable.focus_imagebutton_photo));


                            if (proposer_Is_Same_As_Life_Assured
                                    .equalsIgnoreCase("n")) {

                                String lifeassuredSignName = NeedAnalysisActivity.URN_NO
                                        + "_cust2sign.png";

                                File lifeassuredSignFile = mStorageUtils.createFileToAppSpecificDir(context,
                                        lifeassuredSignName);

                                if (lifeassuredSignFile.exists()) {
                                    lifeassuredSignFile.delete();
                                }

                            }
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
                        && (((photoBitmap != null
                ) && radioButtonTrasactionModeParivartan
                        .isChecked()) || radioButtonTrasactionModeManual
                        .isChecked())) {
                    NeedAnalysisActivity.str_need_analysis = "";

                    // String isActive = "0";
                    String productCode = "SIP";

                    AnnulizedPremium = 0.0;

                    int gridsize;
                    String maturity_benefit_gurantee_str;
                    double maturity_benefit_gurantee_int = 0.0;

                    String TotalMaturity_4percent_str;
                    double TotalMaturity_4percent_int = 0.0;

                    if (lumpSumAmount.equalsIgnoreCase("Y")) {

                        gridsize = Integer.parseInt(policy_term);

                        for (int i = 1; i <= gridsize; i++) {
                            maturity_benefit_gurantee_str = prsObj.parseXmlTag(output, "survivalBen" + i + "");
                            maturity_benefit_gurantee_int = maturity_benefit_gurantee_int + Double.parseDouble(maturity_benefit_gurantee_str);


                            TotalMaturity_4percent_str = prsObj
                                    .parseXmlTag(output, "TotalMaturity_4percent" + i + "");
                            TotalMaturity_4percent_int = TotalMaturity_4percent_int + Double.parseDouble(TotalMaturity_4percent_str);


                            String total_base_premium_without_tax = prsObj.parseXmlTag(output,
                                    "annulizedprem" + i + "");
                            AnnulizedPremium = AnnulizedPremium + Double.parseDouble(total_base_premium_without_tax);


                        }
                    } else {
                        gridsize = Integer.parseInt(policy_term) + 15;
                        for (int i = 1; i <= gridsize; i++) {
                            maturity_benefit_gurantee_str = prsObj.parseXmlTag(output, "guaranMatBen" + i + "");
                            maturity_benefit_gurantee_int = maturity_benefit_gurantee_int + Double.parseDouble(maturity_benefit_gurantee_str);
                            TotalMaturity_4percent_str = prsObj
                                    .parseXmlTag(output, "TotalMaturity_4percent" + i + "");
                            TotalMaturity_4percent_int = TotalMaturity_4percent_int + Double.parseDouble(TotalMaturity_4percent_str);

                            String total_base_premium_without_tax = prsObj.parseXmlTag(output,
                                    "annulizedprem" + i + "");
                            AnnulizedPremium = AnnulizedPremium + Double.parseDouble(total_base_premium_without_tax);

                        }
                    }


                    AnnulizedPremium = 0.0;

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
                                    .valueOf((sum_assured.equals("") || sum_assured == null) ? "0"
                                            : sum_assured))), obj
                            .getRound(premWthST), emailId, mobileNo,
                            agentEmail, agentMobile, na_input, na_output,
                            premium_paying_frequency, Integer
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
                                    .valueOf((sum_assured.equals("") || sum_assured == null) ? "0"
                                            : sum_assured))), obj
                            .getRound(premWthST), agentEmail,
                            agentMobile, na_input, na_output,
                            premium_paying_frequency, Integer
                            .parseInt(policy_term), 0, productCode,
                            getDate(lifeAssured_date_of_birth), "", "", mode, inputVal
                            .toString(), retVal.toString().replace(
                            bussIll.toString(), "")));

                    CreateSmartIncomeProtectBIPdf();

                    NABIObj.serviceHit(BI_SmartIncomeProtectActivity.this,
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
                        setFocusable(imageButtonSmartIncomeProtectProposerPhotograph);
                        imageButtonSmartIncomeProtectProposerPhotograph
                                .requestFocus();
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
        tv_uin_smart_income_protect
                .setText("Benefit Illustration (BI) : SBI Life - Smart Income Protect  (UIN :  111N085V04) | An Individual, Non-linked, Participating, Life Insurance Savings Product"
                        + "" + "");


        output = retVal.toString();


        gender = prsObj.parseXmlTag(input, "gender");

        premium_paying_frequency = prsObj.parseXmlTag(input, "premFreq");
        tv_bi_smart_income_protect_life_assured_premium_frequency
                .setText(premium_paying_frequency);

        if (premium_paying_frequency.equals("Single")) {
            tr_smart_income_protect_surrender_value.setVisibility(View.GONE);
        }

        String staffdiscount = prsObj.parseXmlTag(input, "isStaff");

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


        policy_term = prsObj.parseXmlTag(input, "policyTerm");
        tv_bi_smart_income_protect_term.setText(policy_term + " ");

        String payingTerm;
        if (premium_paying_frequency.equalsIgnoreCase("Single")) {
            payingTerm = "1 ";
        } else {
            payingTerm = policy_term + " ";
        }

        tv_bi_smart_income_protect_premium_paying_term.setText(payingTerm);

        sum_assured = prsObj.parseXmlTag(input, "sumAssured");
        sum_assured_on_death = prsObj.parseXmlTag(output, "guarntdDeathBenft1");


        tv_bi_smart_income_protect_sum_assured.setText("Rs "
                + getformatedThousandString(Integer.parseInt(obj.getRound(obj
                .getStringWithout_E(Double.valueOf((sum_assured
                        .equals("") || sum_assured == null) ? "0"
                        : sum_assured))))));

        tv_bi_smart_income_protect_sum_assured_on_death.setText("Rs "
                + getformatedThousandString(Integer.parseInt(obj.getRound(obj
                .getStringWithout_E(Double.valueOf((sum_assured_on_death
                        .equals("") || sum_assured_on_death == null) ? "0"
                        : sum_assured_on_death))))));

        lumpSumAmount = prsObj.parseXmlTag(input, "mat_benefit_lump_sum");
        if (lumpSumAmount.equalsIgnoreCase("Y")) {
            tv_bi_smart_income_protect_maturity_ben_option.setText("Lump sum");
        } else {
            tv_bi_smart_income_protect_maturity_ben_option.setText("Installments");
        }


        if (premium_paying_frequency.equals("Yearly")) {
            //tv_premium_type.setText("Yearly " + "premium ");
            tv_premium_install_type.setText("Yearly " + "premium ");
            tv_premium_type_rider.setText("Yearly " + "premium with Applicable Taxes");
            tv_premium_type1.setText("Yearly " + "premium ");
            tv_premium_type2.setText("Yearly " + "premium ");
            tv_premium_type3.setText("Yearly " + "premium ");
            //tv_premium_install_type1.setText("Yearly " + "Installment Premium");
            //tv_mandatory_bi_smart_income_protect_yearly_premium_with_tax1
            //	.setText("Yearly " + "Installment Premium with Applicable Taxes");

        } else if (premium_paying_frequency.equals("Half Yearly")) {
            //tv_premium_type.setText("Half Yearly " + "premium ");
            tv_premium_install_type.setText("Half Yearly " + "premium ");
            tv_premium_type_rider.setText("Half-Yearly " + "premium with Applicable Taxes");
            tv_premium_type1.setText("Half-Yearly " + "premium ");
            tv_premium_type2.setText("Half-Yearly " + "premium ");
            tv_premium_type3.setText("Half-Yearly " + "premium ");
            //tv_premium_install_type1.setText("Half Yearly " + "premium ");
            //tv_mandatory_bi_smart_income_protect_yearly_premium_with_tax1
            //	.setText("Half-Yearly " + "premium with Applicable Taxes");
        } else if (premium_paying_frequency.equals("Quarterly")) {
            //tv_premium_type.setText("Quarterly " + "premium ");
            tv_premium_install_type.setText("Quarterly " + "premium ");
            tv_premium_type_rider.setText("Quarterly " + "premium with Applicable Taxes");
            tv_premium_type1.setText("Quarterly " + "premium ");
            tv_premium_type2.setText("Quarterly " + "premium ");
            tv_premium_type3.setText("Quarterly " + "premium ");
            //tv_premium_install_type1.setText("Quarterly " + "premium ");
            //	tv_mandatory_bi_smart_income_protect_yearly_premium_with_tax1
            //		.setText("Quarterly " + "premium with Applicable Taxes");
        } else if (premium_paying_frequency.equals("Monthly")) {
            //	tv_premium_type.setText("Monthly " + "premium ");
            tv_premium_install_type.setText("Monthly " + "premium ");
            tv_premium_type_rider.setText("Monthly " + "premium with Applicable Taxes");
            tv_premium_type1.setText("Monthly " + "premium ");
            tv_premium_type2.setText("Monthly " + "premium ");
            tv_premium_type3.setText("Monthly " + "premium ");
            //tv_premium_install_type1.setText("Monthly " + "premium ");
            //tv_mandatory_bi_smart_income_protect_yearly_premium_with_tax1
            //		.setText("Monthly " + "premium with Applicable Taxes");
        } else if (premium_paying_frequency.equals("Monthly ECS")) {
            //tv_premium_type.setText("Monthly-ECS " + "premium ");
            tv_premium_install_type.setText("Monthly-ECS " + "premium ");
            tv_premium_type_rider.setText("Monthly-ECS " + "premium with Applicable Taxes");
            tv_premium_type1.setText("Monthly-ECS " + "premium ");
            tv_premium_type2.setText("Monthly-ECS " + "premium ");
            tv_premium_type3.setText("Monthly-ECS " + "premium ");
        } else if (premium_paying_frequency.equals("Monthly-SI/CC")) {
            //tv_premium_type.setText("Monthly-SI/CC " + "premium ");
            tv_premium_install_type.setText("Monthly-SI/CC " + "premium ");
            tv_premium_type_rider
                    .setText("Monthly-SI/CC " + "premium with Applicable Taxes");
            tv_premium_type1.setText("Monthly-SI/CC " + "premium ");
            tv_premium_type2.setText("Monthly-SI/CC " + "premium ");
            tv_premium_type3.setText("Monthly-SI/CC " + "premium ");
        } else if (premium_paying_frequency.equals("Single")) {
            //tv_premium_type.setText("Single " + "premium ");
            tv_premium_install_type.setText("Single " + "premium ");
            tv_premium_type_rider.setText("Single " + "premium with Applicable Taxes");
            tv_premium_type1.setText("Single " + "premium ");
            tv_premium_type2.setText("Single " + "premium ");
            tv_premium_type3.setText("Single " + "premium ");
            //tv_premium_install_type1.setText("Single " + "premium ");
            //	tv_mandatory_bi_smart_income_protect_yearly_premium_with_tax1
            //		.setText("Single " + "premium with Applicable Taxes");
        }
        String premium = prsObj.parseXmlTag(output, "yearlyPrem");
        tv_bi_smart_income_protect_yearly_premium.setText("Rs "
                + getformatedThousandString(Integer.parseInt(obj.getRound(obj.getStringWithout_E(Double.valueOf((premium
                .equals("") || premium == null) ? "0" : premium))))));

        Premium_pdf = premium;

        adb_rider_status = prsObj.parseXmlTag(input, "isADBRider");
        atpdb_rider_status = prsObj.parseXmlTag(input, "isATPDBRider");
        ptr_rider_status = prsObj.parseXmlTag(input, "isPTRider");
        cc13nl_rider_status = prsObj.parseXmlTag(input, "isCC13NLRider");

        if (adb_rider_status.equals("false")
                && atpdb_rider_status.equals("false")
                && ptr_rider_status.equals("false")
                && cc13nl_rider_status.equals("false")) {
            ll_basic_rider.setVisibility(View.GONE);

        } else {
            ll_basic_rider.setVisibility(View.VISIBLE);
            if (adb_rider_status.equals("true")) {
                ll_basic_adb_rider.setVisibility(View.VISIBLE);
                term_adb = prsObj.parseXmlTag(input, "adbTerm");
                sa_adb = prsObj.parseXmlTag(input, "adbSA");
                prem_adb = prsObj.parseXmlTag(output, "premADB");

                tv_bi_smart_income_protect_premium_paying_term_adb
                        .setText(term_adb + " Years");

                tv_bi_smart_income_protect_premium_paying_term_adb2
                        .setText(term_adb + " Years");

                tv_bi_smart_income_protect_sum_assured_adb.setText("Rs "
                        + getformatedThousandString(Integer.parseInt(obj.getRound(obj.getStringWithout_E(Double
                        .valueOf(sa_adb.equals("") ? "0" : sa_adb))))));

                tv_bi_smart_income_protect_yearly_premium_adb
                        .setText("Rs "
                                + getformatedThousandString(Integer.parseInt(obj.getRound(obj.getStringWithout_E(Double
                                .valueOf(prem_adb.equals("") ? "0"
                                        : prem_adb))))));
            } else {
                ll_basic_adb_rider.setVisibility(View.GONE);
            }
            if (atpdb_rider_status.equals("true")) {
                ll_basic_atpdb_rider.setVisibility(View.VISIBLE);
                term_atpdb = prsObj.parseXmlTag(input, "atpdbTerm");

                sa_atpdb = prsObj.parseXmlTag(input, "atpdbSA");
                prem_atpdb = prsObj.parseXmlTag(output, "premATPDB");

                tv_bi_smart_income_protect_premium_paying_term_adptb
                        .setText(term_atpdb + " Years");

                tv_bi_smart_income_protect_premium_paying_term_adptb2
                        .setText(term_atpdb + " Years");

                tv_bi_smart_income_protect_sum_assured_adptb
                        .setText("Rs "
                                + getformatedThousandString(Integer.parseInt(obj.getRound(obj.getStringWithout_E(Double
                                .valueOf(sa_atpdb.equals("") ? "0"
                                        : sa_atpdb))))));

                tv_bi_smart_income_protect_yearly_premium_adptb.setText("Rs "
                        + getformatedThousandString(Integer.parseInt(obj.getRound(obj.getStringWithout_E(Double
                        .valueOf(prem_atpdb.equals("") ? "0"
                                : prem_atpdb))))));
            } else {
                ll_basic_atpdb_rider.setVisibility(View.GONE);
            }
            if (ptr_rider_status.equals("true")) {
                ll_basic_pta_rider.setVisibility(View.VISIBLE);
                term_pta = prsObj.parseXmlTag(input, "ptTerm");
                sa_pta = prsObj.parseXmlTag(input, "ptSA");
                prem_pta = prsObj.parseXmlTag(output, "premPTA");

                tv_bi_smart_income_protect_premium_paying_term_pta
                        .setText(term_pta + " Years");

                tv_bi_smart_income_protect_premium_paying_term_pta2
                        .setText(term_pta + " Years");

                tv_bi_smart_income_protect_sum_assured_pta.setText("Rs "
                        + getformatedThousandString(Integer.parseInt(obj.getRound(obj.getStringWithout_E(Double
                        .valueOf(sa_pta.equals("") ? "0" : sa_pta))))));

                tv_bi_smart_income_protect_yearly_premium_pta
                        .setText("Rs "
                                + getformatedThousandString(Integer.parseInt(obj.getRound(obj.getStringWithout_E(Double
                                .valueOf(prem_pta.equals("") ? "0"
                                        : prem_pta))))));
            } else {
                ll_basic_pta_rider.setVisibility(View.GONE);
            }

            if (cc13nl_rider_status.equals("true")) {
                ll_basic_cc13nl_rider.setVisibility(View.VISIBLE);
                term_cc13nl = prsObj.parseXmlTag(input, "cc13nlTerm");
                sa_pta_cc13nl = prsObj.parseXmlTag(input, "cc13nlSA");
                prem_pta_cc13nl = prsObj.parseXmlTag(output, "premCC13");

                tv_bi_smart_income_protect_premium_paying_term_cc13nl
                        .setText(term_cc13nl + " Years");

                tv_bi_smart_income_protect_sum_assured_cc13nl.setText("Rs "
                        + getformatedThousandString(Integer.parseInt(obj.getRound(obj.getStringWithout_E(Double
                        .valueOf(sa_pta_cc13nl.equals("") ? "0"
                                : sa_pta_cc13nl))))));

                tv_bi_smart_income_protect_yearly_premium_cc13nl.setText("Rs "
                        + getformatedThousandString(Integer.parseInt(obj.getRound(obj.getStringWithout_E(Double
                        .valueOf(prem_pta_cc13nl.equals("") ? "0"
                                : prem_pta_cc13nl))))));
            } else {
                ll_basic_cc13nl_rider.setVisibility(View.GONE);
            }
        }

        basicprem = prsObj.parseXmlTag(output, "premInst");
        String basicPrem = prsObj.parseXmlTag(output, "basicPrem");
        servcTax = prsObj.parseXmlTag(output, "servcTax");
        String instRider = prsObj.parseXmlTag(output, "InstRider");
        String instWithSTFirstYearRider = prsObj.parseXmlTag(output, "InstWithSTFirstYearRider");
        String instWithSTSecondYearRider = prsObj.parseXmlTag(output, "InstWithSTSecondYearRider");
        premWthST = prsObj.parseXmlTag(output, "premWthST");

        String premInstBasicFirstYear = prsObj.parseXmlTag(output, "premInstBasicFirstYear");
        String totalInstPremFirstYear = prsObj.parseXmlTag(output, "totalInstPremFirstYear");

        String servcTaxSecondYear = prsObj.parseXmlTag(output, "servcTaxSecondYear");
        String premWthSTSecondYear = prsObj.parseXmlTag(output, "premWthSTSecondYear");
        String totalInstPremSecondYear = prsObj.parseXmlTag(output, "totalInstPremSecondYear");
        tv_bi_smart_income_protect_basic_premium.setText(" Rs "
                + getformatedThousandString(Integer.parseInt(obj.getRound(obj.getStringWithout_E(Double.valueOf(basicprem
                .equals("") ? "0" : basicprem))))));

        tv_bi_smart_income_protect_basic_premium_first_year.setText(""
                + getformatedThousandString(Integer.parseInt(obj.getRound(obj.getStringWithout_E(Double.valueOf(Premium_pdf
                .equals("") ? "0" : Premium_pdf))))));

        tv_bi_smart_income_protect_service_tax.setText("Rs "
                + getformatedThousandString(Integer.parseInt(obj.getRound(obj.getStringWithout_E(Double.valueOf(servcTax
                .equals("") ? "0" : servcTax))))));

        tv_bi_smart_income_protect_service_tax_first_year.setText(""
                + getformatedThousandString(Integer.parseInt(obj.getRound(obj.getStringWithout_E(Double.valueOf(instRider
                .equals("") ? "0" : instRider))))));

        tv_bi_smart_income_protect_yearly_premium_with_tax.setText(" Rs "
                + getformatedThousandString(Integer.parseInt(obj.getRound(obj.getStringWithout_E(Double.valueOf(premWthST
                .equals("") ? "0" : premWthST))))));
        tv_bi_smart_income_protect_yearly_premium_with_tax_first_year
                .setText(""
                        + getformatedThousandString(Integer.parseInt(obj.getRound(obj.getStringWithout_E(Double
                        .valueOf(basicprem.equals("") ? "0" : basicprem))))));

        // Amit changes start- 23-5-2016
        // basicServiceTax = prsObj.parseXmlTag(output, "basicServiceTax");
        String basicServiceTax = prsObj.parseXmlTag(output, "servcTax");

        tv_bi_smart_income_protect_basic_service_tax_first_year.setText(""
                + getformatedThousandString(Integer.parseInt(obj.getRound(obj.getStringWithout_E(Double
                .valueOf(basicServiceTax.equals("") ? "0"
                        : basicServiceTax))))));

        String SBCServiceTax = prsObj.parseXmlTag(output, "SBCServiceTax");

        tv_bi_smart_income_protect_swachh_bharat_cess_first_year
                .setText(""
                        + getformatedThousandString(Integer.parseInt(obj.getRound(obj.getStringWithout_E(Double
                        .valueOf(SBCServiceTax.equals("") ? "0"
                                : SBCServiceTax))))));

        String KKCServiceTax = prsObj.parseXmlTag(output, "KKCServiceTax");

        tv_bi_smart_income_protect_krishi_kalyan_cess_first_year
                .setText(""
                        + getformatedThousandString(Integer.parseInt(obj.getRound(obj.getStringWithout_E(Double
                        .valueOf(KKCServiceTax.equals("") ? "0"
                                : KKCServiceTax))))));
        // Amit changes end- 23-5-2016

        //tr_second_year.setVisibility(View.GONE);

        if (!smartIncomeProtectBean.getPremFreq().equalsIgnoreCase("Single")) {
            //tr_second_year.setVisibility(View.VISIBLE);
            tv_bi_smart_income_protect_basic_premium_second_year.setText(""
                    + getformatedThousandString(Integer.parseInt(obj.getRound(obj.getStringWithout_E(Double
                    .valueOf(premInstBasicFirstYear.equals("") ? "0" : premInstBasicFirstYear))))));

            tv_bi_smart_income_protect_basic_premium_second_year2.setText(""
                    + getformatedThousandString(Integer.parseInt(obj.getRound(obj.getStringWithout_E(Double
                    .valueOf(premWthSTSecondYear.equals("") ? "0" : premWthSTSecondYear))))));

            tv_bi_smart_income_protect_service_tax_second_year.setText(""
                    + getformatedThousandString(Integer.parseInt(obj.getRound(obj.getStringWithout_E(Double
                    .valueOf(instWithSTFirstYearRider.equals("") ? "0"
                            : instWithSTFirstYearRider))))));

            tv_bi_smart_income_protect_service_tax_second_year2.setText(""
                    + getformatedThousandString(Integer.parseInt(obj.getRound(obj.getStringWithout_E(Double
                    .valueOf(instWithSTSecondYearRider.equals("") ? "0"
                            : instWithSTSecondYearRider))))));

            tv_bi_smart_income_protect_yearly_premium_with_tax_second_year
                    .setText(""
                            + getformatedThousandString(Integer.parseInt(obj.getRound(obj.getStringWithout_E(Double.valueOf(totalInstPremFirstYear
                            .equals("") ? "0" : totalInstPremFirstYear))))));

            tv_bi_smart_income_protect_yearly_premium_with_tax_second_year2
                    .setText(""
                            + getformatedThousandString(Integer.parseInt(obj.getRound(obj.getStringWithout_E(Double.valueOf(totalInstPremSecondYear
                            .equals("") ? "0" : totalInstPremSecondYear))))));

            // Amit changes start- 23-5-2016
            // basicServiceTaxSecondYear = prsObj.parseXmlTag(output,
            // "basicServiceTaxSecondYear");
            String basicServiceTaxSecondYear = prsObj.parseXmlTag(output,
                    "servcTaxSecondYear");

            tv_bi_smart_income_protect_basic_service_tax_second_year.setText(""
                    + getformatedThousandString(Integer.parseInt(obj.getRound(obj.getStringWithout_E(Double
                    .valueOf(basicServiceTaxSecondYear.equals("") ? "0"
                            : basicServiceTaxSecondYear))))));

            String SBCServiceTaxSecondYear = prsObj.parseXmlTag(output,
                    "SBCServiceTaxSecondYear");

            tv_bi_smart_income_protect_swachh_bharat_cess_second_year
                    .setText(""
                            + getformatedThousandString(Integer.parseInt(obj.getRound(obj.getStringWithout_E(Double.valueOf(SBCServiceTaxSecondYear
                            .equals("") ? "0" : SBCServiceTaxSecondYear))))));

            String KKCServiceTaxSecondYear = prsObj.parseXmlTag(output,
                    "KKCServiceTaxSecondYear");

            tv_bi_smart_income_protect_krishi_kalyan_cess_second_year
                    .setText(""
                            + getformatedThousandString(Integer.parseInt(obj.getRound(obj.getStringWithout_E(Double.valueOf(KKCServiceTaxSecondYear
                            .equals("") ? "0" : KKCServiceTaxSecondYear))))));
            // Amit changes end- 23-5-2016
        }

        BackdatingInt = prsObj.parseXmlTag(output, "backdateInt");

        tv_bi_smart_income_protect_backdating_interest
                .setText(" Rs "
                        + getformatedThousandString(Integer.parseInt(obj.getRound(obj.getStringWithout_E(Double
                        .valueOf(BackdatingInt.equals("") ? "0"
                                : BackdatingInt))))));

        String str_premPay;
        if (premium_paying_frequency.equalsIgnoreCase("Single")) {
            str_premPay = "Single";
        } else {
            str_premPay = "Regular";
        }

        TextView tv_Company_policy_surrender_dec = d
                .findViewById(R.id.tv_Company_policy_surrender_dec);

        Company_policy_surrender_dec = "Your SBI Life - Smart Income Protect " +
                "(UIN: 111N085V04) is a "
                + str_premPay
                + " premium policy, for which your first year " + premium_paying_frequency + " Premium is Rs "
                + getformatedThousandString(Integer.parseInt(obj.getRound(obj.getStringWithout_E(Double.valueOf(premWthST
                .equals("") ? "0" : premWthST)))))
                + " .Your Policy Term is "
                + policy_term
                + " years"
                + " .Your Premium Payment Term is "
                + policy_term
                + " years"
                + " and Basic Sum Assured is Rs. "
                +

                getformatedThousandString(Integer.parseInt(obj.getRound(obj
                        .getStringWithout_E(Double.valueOf((sum_assured
                                .equals("") || sum_assured == null) ? "0"
                                : sum_assured)))));

        //tv_Company_policy_surrender_dec.setText(Company_policy_surrender_dec);
        int gridsize;
        if (lumpSumAmount.equalsIgnoreCase("Y")) {
            gridsize = Integer.parseInt(policy_term);
        } else {
            gridsize = Integer.parseInt(policy_term) + 15;
        }

        for (int i = 1; i <= gridsize; i++) {

            String policy_year = prsObj
                    .parseXmlTag(output, "policyYr" + i + "");
            String total_base_premium_without_tax = prsObj.parseXmlTag(output,
                    "totaBasePrem" + i + "");
            String annulizedprem = prsObj.parseXmlTag(output,
                    "annulizedprem" + i + "");


            String Guarnteedaddition = prsObj.parseXmlTag(output,
                    "Guarnteedaddition" + i + "");
            String survivalBen = prsObj.parseXmlTag(output,
                    "survivalBen" + i + "");
            String guarntdDeathBenft = prsObj.parseXmlTag(output,
                    "guarntdDeathBenft" + i + "");
            String GSV_surrendr_val = prsObj.parseXmlTag(output,
                    "GSV_surrendr_val" + i + "");


            String benefit_payable_at_death_4_percentage = prsObj.parseXmlTag(
                    output, "nongrntdDeathNenft_4Percent" + i + "");
            String benefit_payable_at_death_8_percentage = prsObj.parseXmlTag(
                    output, "nongrntdDeathNenft_8Percent" + i + "");
            String guaranMatBen = prsObj.parseXmlTag(output,
                    "guaranMatBen" + i + "");
            String maturity_benefit_non_gurantee_4_percentage = prsObj
                    .parseXmlTag(output, "nonGuaranMatBen_4Percent" + i + "");
            String maturity_benefit_non_gurantee_8_percentage = prsObj
                    .parseXmlTag(output, "nonGuaranMatBen_8Percent" + i + "");

            String surrender_value_ssv_4_percentage = prsObj.parseXmlTag(
                    output, "NonGSV_surrndr_val_4Percent" + i + "");
            String surrender_value_ssv_8_percentage = prsObj.parseXmlTag(
                    output, "NonGSV_surrndr_val_8Percent" + i + "");


            String TotalMaturity_4percent = prsObj.parseXmlTag(
                    output, "TotalMaturity_4percent" + i + "");
            String TotalMaturity_8percent = prsObj.parseXmlTag(
                    output, "TotalMaturity_8percent" + i + "");

            String TotalDeathBenefit4percent = prsObj.parseXmlTag(
                    output, "TotalDeathBenefit4percent" + i + "");

            String TotalDeathBenefit8percent = prsObj.parseXmlTag(
                    output, "TotalDeathBenefit8percent" + i + "");


            list_data
                    .add(new M_BI_SmartIncomeProtect_AdapterCommon(
                            policy_year,
                            (obj.getRound(obj.getStringWithout_E(Double
                                    .valueOf(annulizedprem))))
                                    + "",
                            (obj.getRound(obj.getStringWithout_E(Double
                                    .valueOf(Guarnteedaddition))))
                                    + "",
                            (obj.getRound(obj.getStringWithout_E(Double
                                    .valueOf(survivalBen))))
                                    + "",
                            (obj.getRound(obj.getStringWithout_E(Double
                                    .valueOf(GSV_surrendr_val))))
                                    + "",
                            (obj.getRound(obj.getStringWithout_E(Double
                                    .valueOf(guarntdDeathBenft))))
                                    + "",

                            (obj.getRound(obj.getStringWithout_E(Double
                                    .valueOf(guaranMatBen))))
                                    + "",
                            (obj.getRound(obj.getStringWithout_E(Double
                                    .valueOf(benefit_payable_at_death_4_percentage))))
                                    + "",
                            "0",
                            (obj.getRound(obj.getStringWithout_E(Double
                                    .valueOf(surrender_value_ssv_4_percentage))))
                                    + "",
                            (obj.getRound(obj.getStringWithout_E(Double
                                    .valueOf(benefit_payable_at_death_8_percentage))))
                                    + "",
                            "0",
                            (obj.getRound(obj.getStringWithout_E(Double
                                    .valueOf(surrender_value_ssv_8_percentage))))
                                    + "",
                            (obj.getRound(obj.getStringWithout_E(Double
                                    .valueOf(TotalMaturity_4percent))))
                                    + "",
                            (obj.getRound(obj.getStringWithout_E(Double
                                    .valueOf(TotalMaturity_8percent))))
                                    + "",
                            (obj.getRound(obj.getStringWithout_E(Double
                                    .valueOf(TotalDeathBenefit4percent))))
                                    + "",
                            (obj.getRound(obj.getStringWithout_E(Double
                                    .valueOf(TotalDeathBenefit8percent))))
                                    + ""));

        }

        Adapter_BI_SmartIncomeProtectGridCommon adapter_new = new Adapter_BI_SmartIncomeProtectGridCommon(
                BI_SmartIncomeProtectActivity.this, list_data);
        gv_userinfo.setAdapter(adapter_new);

        GridHeight gh_new = new GridHeight();
        gh_new.getheight(gv_userinfo, list_data.size() + "");

        d.show();

    }

    private void email_id_validation(String email_id) {

        String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email_id);
        if (!(matcher.matches())) {
            edt_smart_income_protect_Email_id
                    .setError("Please provide the correct email address");
            validationFla1 = false;
        } else if ((matcher.matches())) {
            validationFla1 = true;
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

    public void windowmessageProposersgin() {

        d = new Dialog(BI_SmartIncomeProtectActivity.this);
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
                Intent intent = new Intent(BI_SmartIncomeProtectActivity.this,
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

    private void windowmessagesgin() {

        d = new Dialog(BI_SmartIncomeProtectActivity.this);
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
                Intent intent = new Intent(BI_SmartIncomeProtectActivity.this,
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

    private String getCurrentDate1() {
        Calendar present_date = Calendar.getInstance();
        int mDay = present_date.get(Calendar.DAY_OF_MONTH);
        int mMonth = present_date.get(Calendar.MONTH);
        int mYear = present_date.get(Calendar.YEAR);

        String mont = (mMonth + 1 < 10 ? "0" : "") + (mMonth + 1);
        String day = (mDay < 10 ? "0" : "") + mDay;

        String date = day + "-" +
                mont + "-" + mYear;
        return getDate1(date + "");

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

    private void setDefaultDate(int id) {
        Calendar present_date = Calendar.getInstance();
        present_date.add(Calendar.YEAR, -id);
        mDay = present_date.get(Calendar.DAY_OF_MONTH);
        mMonth = present_date.get(Calendar.MONTH);
        mYear = present_date.get(Calendar.YEAR);
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
                                    setFocusable(spnr_bi_smart_income_protect_life_assured_title);
                                    spnr_bi_smart_income_protect_life_assured_title
                                            .requestFocus();
                                } else if (lifeAssured_First_Name.equals("")) {
                                    setFocusable(edt_bi_smart_income_protect_life_assured_first_name);
                                    edt_bi_smart_income_protect_life_assured_first_name
                                            .requestFocus();
                                } else {
                                    setFocusable(edt_bi_smart_income_protect_life_assured_last_name);
                                    edt_bi_smart_income_protect_life_assured_last_name
                                            .requestFocus();
                                }
                            }
                        });
                showAlert.show();

                return false;
            } else if (gender.equalsIgnoreCase("")) {
                showAlert
                        .setMessage("Please select Gender");
                showAlert.setNeutralButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // apply focusable method
                                setFocusable(spnr_bi_smart_income_protect_selGender);
                                spnr_bi_smart_income_protect_selGender.requestFocus();
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
                                setFocusable(spnr_bi_smart_income_protect_life_assured_title);
                                spnr_bi_smart_income_protect_life_assured_title
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
                                setFocusable(spnr_bi_smart_income_protect_life_assured_title);
                                spnr_bi_smart_income_protect_life_assured_title
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
                                setFocusable(spnr_bi_smart_income_protect_life_assured_title);
                                spnr_bi_smart_income_protect_life_assured_title
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
                                    setFocusable(spnr_bi_smart_income_protect_life_assured_title);
                                    spnr_bi_smart_income_protect_life_assured_title
                                            .requestFocus();
                                } else if (lifeAssured_First_Name.equals("")) {

                                    edt_bi_smart_income_protect_life_assured_first_name
                                            .requestFocus();
                                } else {
                                    edt_bi_smart_income_protect_life_assured_last_name
                                            .requestFocus();
                                }
                            }
                        });
                showAlert.show();

                return false;
            } else if (gender.equalsIgnoreCase("")) {

                showAlert
                        .setMessage("please select gender");
                showAlert.setNeutralButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // apply focusable method
                                setFocusable(spnr_bi_smart_income_protect_life_assured_title);
                                spnr_bi_smart_income_protect_life_assured_title
                                        .requestFocus();
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
                                setFocusable(spnr_bi_smart_income_protect_life_assured_title);
                                spnr_bi_smart_income_protect_life_assured_title
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
                                setFocusable(spnr_bi_smart_income_protect_life_assured_title);
                                spnr_bi_smart_income_protect_life_assured_title
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
                                setFocusable(spnr_bi_smart_income_protect_life_assured_title);
                                spnr_bi_smart_income_protect_life_assured_title
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
                                    setFocusable(spnr_bi_smart_income_protect_proposer_title);
                                    spnr_bi_smart_income_protect_proposer_title
                                            .requestFocus();
                                } else if (proposer_First_Name.equals("")) {
                                    edt_bi_smart_income_protect_proposer_first_name
                                            .requestFocus();
                                } else {
                                    edt_bi_smart_income_protect_proposer_last_name
                                            .requestFocus();
                                }
                            }
                        });
                showAlert.show();

                return false;

            } else if (propsoser_gender.equalsIgnoreCase("")) {
                commonMethods.dialogWarning(context, "Please Select Proposer Gender", true);

                return false;
            } else if (proposer_Title.equalsIgnoreCase("Mr.")
                    && propsoser_gender.equalsIgnoreCase("Female")) {

                showAlert
                        .setMessage("Proposer Title and Gender is not Valid");
                showAlert.setNeutralButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // apply focusable method
                                setFocusable(spnr_bi_smart_income_protect_proposer_title);
                                spnr_bi_smart_income_protect_proposer_title
                                        .requestFocus();
                            }
                        });
                showAlert.show();

                return false;

            } else if (proposer_Title.equalsIgnoreCase("Ms.")
                    && propsoser_gender.equalsIgnoreCase("Male")) {

                showAlert
                        .setMessage("Proposer Title and Gender is not Valid");
                showAlert.setNeutralButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // apply focusable method
                                setFocusable(spnr_bi_smart_income_protect_proposer_title);
                                spnr_bi_smart_income_protect_proposer_title
                                        .requestFocus();
                            }
                        });
                showAlert.show();

                return false;

            } else if (proposer_Title.equalsIgnoreCase("Mrs.")
                    && propsoser_gender.equalsIgnoreCase("Male")) {

                showAlert
                        .setMessage("Proposer Title and Gender is not Valid");
                showAlert.setNeutralButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // apply focusable method
                                setFocusable(spnr_bi_smart_income_protect_proposer_title);
                                spnr_bi_smart_income_protect_proposer_title
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

        // if (proposer_Is_Same_As_Life_Assured.equalsIgnoreCase("Y")) {

        if (lifeAssured_date_of_birth.equals("")
                || lifeAssured_date_of_birth.equalsIgnoreCase("Select Date")) {
            showAlert
                    .setMessage("Please Select Valid Date Of Birth For LifeAssured");
            showAlert.setNeutralButton("OK",
                    new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            // apply focusable method
                            setFocusable(btn_bi_smart_income_protect_life_assured_date);
                            btn_bi_smart_income_protect_life_assured_date
                                    .requestFocus();
                        }
                    });
            showAlert.show();

            return false;
        } else {
            return true;
        }
        // } else
        // return true;
    }

    private boolean valProposerDob() {

        if (proposer_Is_Same_As_Life_Assured.equalsIgnoreCase("N")) {

            if (proposer_date_of_birth.equals("")
                    || proposer_date_of_birth.equalsIgnoreCase("Select Date")) {
                showAlert
                        .setMessage("Please Select Valid Date Of Birth For Proposer");
                showAlert.setNeutralButton("OK",
                        new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // apply focusable method
                                setFocusable(btn_bi_smart_income_protect_proposer_date);
                                btn_bi_smart_income_protect_proposer_date
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

    private boolean valBackdatingDate() {

        if (proposer_Backdating_WishToBackDate_Policy.equalsIgnoreCase("Y")) {

            if (proposer_Backdating_BackDate.equals("")
                    || proposer_Backdating_BackDate
                    .equalsIgnoreCase("Select Date")) {
                showAlert.setMessage("Please Select Backdating Date");
                showAlert.setNeutralButton("OK",
                        new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // apply focusable method
                                setFocusable(btn_smart_income_protect_backdatingdate);
                                btn_smart_income_protect_backdatingdate
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

        if (edt_smart_income_protect_contact_no.getText().toString().equals("")) {
            commonMethods.dialogWarning(context,
                    "Please Fill  Mobile No", true);
            edt_smart_income_protect_contact_no.requestFocus();
            return false;
        } else if (edt_smart_income_protect_contact_no.getText().toString()
                .length() != 10) {
            commonMethods.dialogWarning(context,
                    "Please Fill 10 Digit Mobile No", true);
            edt_smart_income_protect_contact_no.requestFocus();
            return false;
        }

		/*else if (emailId.equals("")) {
			commonMethods.dialogWarning(context,
					"Please Fill Email Id", true);
			edt_smart_income_protect_Email_id.requestFocus();
			return false;

		}

		else if (ConfirmEmailId.equals("")) {

			commonMethods.dialogWarning(context,"Please Fill Confirm Email Id", true);
			edt_smart_income_protect_ConfirmEmail_id.requestFocus();
			return false;
		} else if (!ConfirmEmailId.equals(emailId)) {
			commonMethods.dialogWarning(context,
					"Email Id Does Not Match", true);
			return false;
		}*/

        else if (!emailId.equals("")) {

            email_id_validation(emailId);
            if (validationFla1) {

                if (ConfirmEmailId.equals("")) {

                    commonMethods.dialogWarning(context, "Please Fill Confirm Email Id", true);
                    edt_smart_income_protect_ConfirmEmail_id.requestFocus();
                    return false;
                } else if (!ConfirmEmailId.equals(emailId)) {
                    commonMethods.dialogWarning(context,
                            "Email Id Does Not Match", true);
                    return false;
                }

                return true;
            } else {
                commonMethods.dialogWarning(context,
                        "Please Fill Valid Email Id", true);
                edt_smart_income_protect_Email_id.requestFocus();
                return false;
            }
        } else if (!ConfirmEmailId.equals("")) {

            email_id_validation(ConfirmEmailId);
            if (validationFla1) {

                if (emailId.equals("")) {
                    commonMethods.dialogWarning(context,
                            "Please Fill Email Id", true);
                    edt_smart_income_protect_Email_id.requestFocus();
                    return false;

                }

                return true;
            } else {
                commonMethods.dialogWarning(context,
                        "Please Fill Valid Confirm Email Id", true);
                edt_smart_income_protect_ConfirmEmail_id.requestFocus();
                return false;
            }
        } else {
            return true;
        }
    }

    // Validate Sum Assured
    private Boolean valSA() {
        double minRiderLimit, maxRiderLimit;
        StringBuilder error = new StringBuilder();

        // Basic
        if (edt_bi_smart_income_protect_sum_assured_amount.getText().toString()
                .equals("")
                || Double
                .parseDouble(edt_bi_smart_income_protect_sum_assured_amount
                        .getText().toString()) < prop.minSA) {
            error.append("Sum assured should be greater than or equal ").append(currencyFormat.format(prop.minSA));

        } else if (Double
                .parseDouble(edt_bi_smart_income_protect_sum_assured_amount
                        .getText().toString()) % 1000 != 0) {
            error.append("Sum assured should be multiple of 1,000");
        } else {

            // PTA
            if (cb_bi_smart_income_protect_pt_rider.isChecked()) {
                minRiderLimit = 25000;
                maxRiderLimit = Math
                        .min(5000000,
                                Double.parseDouble(edt_bi_smart_income_protect_sum_assured_amount
                                        .getText().toString()));

                if (edt_bi_smart_income_protect_pt_rider_sum_assured.getText()
                        .toString().equals("")
                        || Double
                        .parseDouble(edt_bi_smart_income_protect_pt_rider_sum_assured
                                .getText().toString()) < minRiderLimit
                        || Double
                        .parseDouble(edt_bi_smart_income_protect_pt_rider_sum_assured
                                .getText().toString()) > maxRiderLimit)
                    error.append("\nEnter Sum assured for Preferred Term Assurance Rider between ").append(currencyFormat.format(minRiderLimit)).append(" and ").append(currencyFormat.format(maxRiderLimit));
                else if (Double
                        .parseDouble(edt_bi_smart_income_protect_pt_rider_sum_assured
                                .getText().toString()) % 1000 != 0)
                    error.append("\nEnter Sum assured for Preferred Term Assurance Rider in multiples of 1,000");
            }
            // ADB
            if (cb_bi_smart_income_protect_adb_rider.isChecked()) {
                minRiderLimit = 25000;
                maxRiderLimit = Math
                        .min(5000000,
                                Double.parseDouble(edt_bi_smart_income_protect_sum_assured_amount
                                        .getText().toString()));

                if (edt_bi_smart_income_protect_adb_rider_sum_assured.getText()
                        .toString().equals("")
                        || Double
                        .parseDouble(edt_bi_smart_income_protect_adb_rider_sum_assured
                                .getText().toString()) < minRiderLimit
                        || Double
                        .parseDouble(edt_bi_smart_income_protect_adb_rider_sum_assured
                                .getText().toString()) > maxRiderLimit)
                    error.append("\nEnter Sum assured for Accidental Death Benefit Rider between ").append(currencyFormat.format(minRiderLimit)).append(" and ").append(currencyFormat.format(maxRiderLimit));
                else if (Double
                        .parseDouble(edt_bi_smart_income_protect_adb_rider_sum_assured
                                .getText().toString()) % 1000 != 0)
                    error.append("\nEnter Sum assured for Accidental Death Benefit Rider in multiples of 1,000");
            }
            // ATPDB
            if (cb_bi_smart_income_protect_atpdb_rider.isChecked()) {
                minRiderLimit = 25000;
                maxRiderLimit = Math
                        .min(5000000,
                                Double.parseDouble(edt_bi_smart_income_protect_sum_assured_amount
                                        .getText().toString()));
                if (edt_bi_smart_income_protect_atpbd_rider_sum_assured
                        .getText().toString().equals("")
                        || Double
                        .parseDouble(edt_bi_smart_income_protect_atpbd_rider_sum_assured
                                .getText().toString()) < minRiderLimit
                        || Double
                        .parseDouble(edt_bi_smart_income_protect_atpbd_rider_sum_assured
                                .getText().toString()) > maxRiderLimit)
                    error.append("\nEnter Sum assured for Accidental Total and Permenent Disability Benefit Rider between ").append(currencyFormat.format(minRiderLimit)).append(" and ").append(currencyFormat.format(maxRiderLimit));
                else if (Double
                        .parseDouble(edt_bi_smart_income_protect_atpbd_rider_sum_assured
                                .getText().toString()) % 1000 != 0)
                    error.append("\nEnter Sum assured for Accidental Total and Permenent Disability Benefit Rider in multiples of 1,000");

            }
            // CC13Nonlinked
            if (cb_bi_smart_income_protect_cc13nl_rider.isChecked()) {
                minRiderLimit = 25000;
                maxRiderLimit = Math
                        .min(2000000,
                                Double.parseDouble(edt_bi_smart_income_protect_sum_assured_amount
                                        .getText().toString()));
                if (edt_bi_smart_income_protect_cc13nl_rider_sum_assured
                        .getText().toString().equals("")
                        || Double
                        .parseDouble(edt_bi_smart_income_protect_cc13nl_rider_sum_assured
                                .getText().toString()) < minRiderLimit
                        || Double
                        .parseDouble(edt_bi_smart_income_protect_cc13nl_rider_sum_assured
                                .getText().toString()) > maxRiderLimit)
                    error.append("\nEnter Sum assured for CC13 Nonlinked Rider between ").append(currencyFormat.format(minRiderLimit)).append(" and ").append(currencyFormat.format(maxRiderLimit));
                else if (Double
                        .parseDouble(edt_bi_smart_income_protect_cc13nl_rider_sum_assured
                                .getText().toString()) % 1000 != 0)
                    error.append("\nEnter Sum assured for CC13 Nonlinked Rider in multiples of 1,000");
            }

        }

        Log.d("error", error.toString());

        if (!error.toString().equals("")) {
            showAlert.setMessage(error.toString());
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

    // Validate Policy term
    boolean valTerm() {
        int maxLimit = Math.min(30, 65 - Integer
                .parseInt(edt_bi_smart_income_protect_life_assured_age
                        .getText().toString()));
        if (Integer.parseInt(spnr_bi_smart_income_protect_policyterm
                .getSelectedItem().toString()) > maxLimit) {
            showAlert.setMessage("Please enter Policy Term between 5 and "
                    + maxLimit);
            showAlert.setNeutralButton("OK",
                    new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            spnr_bi_smart_income_protect_policyterm
                                    .setSelection(0, false);
                        }
                    });
            showAlert.show();

            return false;
        } else {
            return true;
        }
    }

    // Validate rider term
    // boolean valTermRider() {
    // int minLimit = 5;
    // boolean flagTermRider = false;
    // int maxLimit = Integer.parseInt(spnr_bi_smart_income_protect_policyterm
    // .getSelectedItem().toString());
    // StringBuilder error = new StringBuilder();
    //
    // if (cb_bi_smart_income_protect_adb_rider.isChecked()
    // && Integer.parseInt(spnr_bi_smart_income_protect_adb_rider_term
    // .getSelectedItem().toString()) > maxLimit) {
    // spnr_bi_smart_income_protect_adb_rider_term.setSelection(0, false);
    // error.append("Please enter Policy Term of Accidental Death Benefit Rider between 5 and "
    // + maxLimit);
    // }
    //
    // if (cb_bi_smart_income_protect_atpdb_rider.isChecked()
    // && Integer
    // .parseInt(spnr_bi_smart_income_protect_atpdb_rider_term
    // .getSelectedItem().toString()) > maxLimit) {
    // spnr_bi_smart_income_protect_atpdb_rider_term
    // .setSelection(0, false);
    // error.append("Please enter Policy Term of Accidental Total and Permanent Disability Benefit Rider between 5 and "
    // + maxLimit);
    // }
    //
    // if (cb_bi_smart_income_protect_pt_rider.isChecked()
    // && Integer.parseInt(spnr_bi_smart_income_protect_pt_rider_term
    // .getSelectedItem().toString()) > maxLimit) {
    // spnr_bi_smart_income_protect_pt_rider_term.setSelection(0, false);
    // error.append("Please enter Policy Term of Preferred Term Rider between 5 and "
    // + maxLimit);
    // }
    //
    // if (cb_bi_smart_income_protect_cc13nl_rider.isChecked()
    // && Integer
    // .parseInt(spnr_bi_smart_income_protect_cc13nl_rider_term
    // .getSelectedItem().toString()) > maxLimit) {
    // spnr_bi_smart_income_protect_cc13nl_rider_term.setSelection(0,
    // false);
    // error.append("Please enter Policy Term of Criti Care 13 Non-Linked Rider between 5 and "
    // + maxLimit);
    // }
    //
    // if (!error.toString().equals("")) {
    //
    // showAlert.setMessage(error.toString());
    // showAlert.setNeutralButton("OK",
    // new DialogInterface.OnClickListener() {
    //
    // public void onClick(DialogInterface dialog, int which) {
    //
    // }
    // });
    // showAlert.show();
    // }
    // if (error.toString().equals("")) {
    // flagTermRider = true;
    // }
    //
    // return flagTermRider;
    // }

    // public boolean valTermRider() {
    // Today = getCurrentDate1();
    // // System.out.println("Inside val Term Rider");
    // int term = Integer.parseInt(spnr_bi_smart_income_protect_policyterm
    // .getSelectedItem().toString());
    // if (!(lifeAssured_date_of_birth.equals(""))) {
    // age = commonForAllProd.getAge(lifeAssured_date_of_birth, Today);
    // }
    // // System.out.println("Age " +age);
    // StringBuilder error = new StringBuilder();
    //
    // // Modified by Priyanka Warekar - 31-12-2014
    // // if(age<18 || age>55 ||(age+term)>64 )
    // if (age < 18 || age > 58 || (age + term) > 65) {
    // // PTA
    // if (cb_bi_smart_income_protect_pt_rider.isChecked()) {
    //
    // cb_bi_smart_income_protect_pt_rider.setChecked(false);
    // if (spnr_bi_smart_income_protect_pt_rider_term.getVisibility() ==
    // View.VISIBLE) {
    // tr_PT_rider.setVisibility(View.GONE);
    // }
    // error.append("Min age limit for Preferred Term Assurance Rider is 18 to "
    // + (65 - term) + " and maturity age is 65.\n");
    // }
    //
    // // ADB
    // if (cb_bi_smart_income_protect_adb_rider.isChecked()) {
    //
    // cb_bi_smart_income_protect_adb_rider.setChecked(false);
    // if (spnr_bi_smart_income_protect_adb_rider_term.getVisibility() ==
    // View.VISIBLE) {
    // tr_ADB_rider.setVisibility(View.GONE);
    //
    // }
    // error.append("Min age limit for Accidental Death Benefit Rider is 18 to "
    // + (65 - term) + " and maturity age is 65.\n");
    // }
    // // ATPDB
    // if (cb_bi_smart_income_protect_atpdb_rider.isChecked()) {
    //
    // cb_bi_smart_income_protect_atpdb_rider.setChecked(false);
    // if (spnr_bi_smart_income_protect_atpdb_rider_term
    // .getVisibility() == View.VISIBLE) {
    // tr_ATPDB_Rider.setVisibility(View.GONE);
    // }
    // error.append("Min age limit for Accidental Total & Permanent Disability Rider is 18 to "
    // + (65 - term) + " and maturity age is 65.\n");
    // }
    //
    // }
    //
    // // Modified by Priyanka Warekar - 31-12-2014
    // // CC13NonLinked
    // // if(age<18 || age>55 || (age+term)>64 )
    // if (age < 18 || age > 57 || (age + term) > 64) {
    // if (cb_bi_smart_income_protect_cc13nl_rider.isChecked()) {
    // cb_bi_smart_income_protect_cc13nl_rider.setChecked(false);
    // if (spnr_bi_smart_income_protect_cc13nl_rider_term
    // .getVisibility() == View.VISIBLE) {
    // tr_CC13NL_Rider.setVisibility(View.GONE);
    // }
    // error.append("Age limit for Criti Care 13 Non - Linked Rider is 18 to "
    // + (64 - term) + " ");
    // }
    // }
    //
    // if (!error.toString().equals("")) {
    // Log.d("error1", "*" + error.toString() + "*");
    // showAlert.setMessage(error.toString());
    // showAlert.setNeutralButton("OK",
    // new DialogInterface.OnClickListener() {
    //
    // public void onClick(DialogInterface dialog, int which) {
    // }
    // });
    // showAlert.show();
    // return false;
    // }
    //
    // return true;
    // }

    private boolean valTermRider() {
        Today = getCurrentDate1();
        // System.out.println("Inside val Term Rider");
        int term = Integer.parseInt(spnr_bi_smart_income_protect_policyterm
                .getSelectedItem().toString());
        if (!(lifeAssured_date_of_birth.equals(""))) {
            age = commonForAllProd.getAge(lifeAssured_date_of_birth, Today);
        }
        // System.out.println("Age " +age);
        StringBuilder error = new StringBuilder();

        // Modified by Priyanka Warekar - 31-12-2014
        // if(age<18 || age>55 ||(age+term)>64 )
        if (age < 18 || age > 58 || (age + term) > 65) {
            // PTA
            if (cb_bi_smart_income_protect_pt_rider.isChecked()) {

                cb_bi_smart_income_protect_pt_rider.setChecked(false);
                if (spnr_bi_smart_income_protect_pt_rider_term.getVisibility() == View.VISIBLE) {
                    tr_PT_rider.setVisibility(View.GONE);
                }
                error.append("Min age limit for Preferred Term Assurance Rider is 18 to ").append(65 - term).append(" and maturity age is 65.\n");
            }

            // ADB
            if (cb_bi_smart_income_protect_adb_rider.isChecked()) {

                cb_bi_smart_income_protect_adb_rider.setChecked(false);
                if (spnr_bi_smart_income_protect_adb_rider_term.getVisibility() == View.VISIBLE) {
                    tr_ADB_rider.setVisibility(View.GONE);

                }
                error.append("Min age limit for Accidental Death Benefit Rider is 18 to ").append(65 - term).append(" and maturity age is 65.\n");
            }
            // ATPDB
            if (cb_bi_smart_income_protect_atpdb_rider.isChecked()) {

                cb_bi_smart_income_protect_atpdb_rider.setChecked(false);
                if (spnr_bi_smart_income_protect_atpdb_rider_term
                        .getVisibility() == View.VISIBLE) {
                    tr_ATPDB_Rider.setVisibility(View.GONE);
                }
                error.append("Min age limit for Accidental Total & Permanent Disability Rider is 18 to ").append(65 - term).append(" and maturity age is 65.\n");
            }

        }

        // Modified by Priyanka Warekar - 31-12-2014
        // CC13NonLinked
        // if(age<18 || age>55 || (age+term)>64 )

        if (cb_bi_smart_income_protect_cc13nl_rider.isChecked()) {
            if (!(edt_bi_smart_income_protect_life_assured_age.getText()
                    .toString().equals(""))) {
                if (spnr_bi_smart_income_protect_policyterm.getSelectedItem()
                        .toString().equals("7")
                        && (age < 18 || (age + term) > 62)) {

                    error.append("Age limit for Criti Care 13 Non - Linked Rider is 18 to ").append(62 - term).append(" ");

                } else {
                    if ((age < 18 || (age + term) > 64))
                        error.append("Age limit for Criti Care 13 Non - Linked Rider is 18 to ").append(64 - term).append(" ");
                }
            }

        }

        if (!error.toString().equals("")) {
            Log.d("error1", "*" + error.toString() + "*");
            showAlert.setMessage(error.toString());
            showAlert.setNeutralButton("OK",
                    new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {

                            if (cb_bi_smart_income_protect_cc13nl_rider
                                    .isChecked()) {
                                cb_bi_smart_income_protect_cc13nl_rider
                                        .setChecked(false);
                                if (spnr_bi_smart_income_protect_cc13nl_rider_term
                                        .getVisibility() == View.VISIBLE) {
                                    tr_CC13NL_Rider.setVisibility(View.GONE);
                                }

                            }
                        }
                    });
            showAlert.show();
            return false;
        }

        return true;
    }

    // Age Validation
    private boolean valAge() {
        Today = getCurrentDate1();
        if (!(lifeAssured_date_of_birth.equals(""))) {
            age = commonForAllProd.getAge(lifeAssured_date_of_birth, Today);
        }
        if ((age < 11 || age > 58)
                && spnr_bi_smart_income_protect_policyterm.getSelectedItem()
                .toString().equalsIgnoreCase("7")) {
            showAlert
                    .setMessage("Min Age limit is 11 years and Max Age limit is  58  years");
            showAlert.setNeutralButton("OK",
                    new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
            showAlert.show();
            return false;
        } else if ((age < 8 || age > 53)
                && spnr_bi_smart_income_protect_policyterm.getSelectedItem()
                .toString().equalsIgnoreCase("12")) {
            showAlert
                    .setMessage("Min Age limit is 8 years and Max Age limit is  53  years");
            showAlert.setNeutralButton("OK",
                    new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
            showAlert.show();
            return false;
        } else if ((age < 8 || age > 50)
                && spnr_bi_smart_income_protect_policyterm.getSelectedItem()
                .toString().equalsIgnoreCase("15")) {
            showAlert
                    .setMessage("Min Age limit is 8 years and Max Age limit is  50  years");
            showAlert.setNeutralButton("OK",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
            showAlert.show();
            return false;
        } else

            return true;
    }


    // Age Validation
    private boolean valBackdatingAge(int age) {
		/*if (!(lifeAssured_date_of_birth.equals(""))) {
			age = commonForAllProd.getAge(lifeAssured_date_of_birth, Today);
		}*/
        if ((age < 11 || age > 58)
                && spnr_bi_smart_income_protect_policyterm.getSelectedItem()
                .toString().equalsIgnoreCase("7")) {
            showAlert
                    .setMessage("Min Age limit is 11 years and Max Age limit is  58  years");
            showAlert.setNeutralButton("OK",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
            showAlert.show();
            return false;
        } else if ((age < 8 || age > 53)
                && spnr_bi_smart_income_protect_policyterm.getSelectedItem()
                .toString().equalsIgnoreCase("12")) {
            showAlert
                    .setMessage("Min Age limit is 8 years and Max Age limit is  53  years");
            showAlert.setNeutralButton("OK",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
            showAlert.show();
            return false;
        } else if ((age < 8 || age > 50)
                && spnr_bi_smart_income_protect_policyterm.getSelectedItem()
                .toString().equalsIgnoreCase("15")) {
            showAlert
                    .setMessage("Min Age limit is 8 years and Max Age limit is  50  years");
            showAlert.setNeutralButton("OK",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
            showAlert.show();
            return false;
        } else

            return true;
    }

    // Maturity Age Validation
    private boolean valMaturityAge() {
        Today = getCurrentDate1();
        age = commonForAllProd.getAge(lifeAssured_date_of_birth, Today);
        if ((age + Integer.parseInt(spnr_bi_smart_income_protect_policyterm
                .getSelectedItem().toString())) < 18
                || (age
                + Integer
                .parseInt(spnr_bi_smart_income_protect_policyterm
                        .getSelectedItem().toString()) > 65)) {
            showAlert
                    .setMessage("Please Enter Maturity Age between 18 years and 65 years");
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

    private void CreateSmartIncomeProtectBIPdf() {

        // String quotation_Number = ProductHomePageActivity.quotation_Number;
        try {

            Font headerBold = new Font(Font.FontFamily.TIMES_ROMAN, 11,
                    Font.BOLD, BaseColor.WHITE);
            Font small_bold = new Font(Font.FontFamily.TIMES_ROMAN, 8,
                    Font.BOLD);
            Font small_bold_for_name = new Font(Font.FontFamily.TIMES_ROMAN,
                    10, Font.BOLD);

            Font small_bold1 = new Font(Font.FontFamily.TIMES_ROMAN, 8,
                    Font.BOLD);

            Font small_normal = new Font(Font.FontFamily.TIMES_ROMAN, 8,
                    Font.NORMAL);
            Font normal1_bold = new Font(Font.FontFamily.TIMES_ROMAN, 6,
                    Font.BOLD);
            Font normal_bold = new Font(Font.FontFamily.TIMES_ROMAN, 5,
                    Font.BOLD);
            Font normal = new Font(Font.FontFamily.TIMES_ROMAN, 5, Font.NORMAL);

            // File mypath = new File(folder, PropserNumber +
            // "Proposalno_p02.pdf");
            mypath = mStorageUtils.createFileToAppSpecificDir(context,QuatationNumber + "BI.pdf");
            needAnalysispath = mStorageUtils.createFileToAppSpecificDir(context, "NA.pdf");
            // needAnalysis_Tabular_path = new File(folder, "NA_Tabular.pdf");
            newFile = mStorageUtils.createFileToAppSpecificDir(context,
                    QuatationNumber + "P01.pdf");

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
                            "Benefit Illustration (BI) : SBI Life - Smart Income Protect  (UIN :  111N085V04) | An Individual, Non-linked, Participating, Life Insurance Savings Product"
                                    + "" + "", headerBold));

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
                    "Registered & Corporate Office: 'Natraj', M.V.Road and Western Express Highway Junction, Andheri (East), Mumbai - 400069",
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
            document.add(para_address);
            document.add(para_address1);
            document.add(para_address2);
            document.add(para_address3);
            document.add(para_img_logo_after_space_1);
            document.add(headertable);
            document.add(para_img_logo_after_space_1);

            document.add(para_img_logo_after_space_1);
            //if (proposer_Is_Same_As_Life_Assured.equalsIgnoreCase("Y")) {
            //		name_of_proposer = name_of_life_assured;
            //}
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

            PdfPTable BI_Pdftable3 = new PdfPTable(1);
            BI_Pdftable3.setWidthPercentage(100);
            // CR_table6.setWidths(columnWidths_3);
            PdfPCell BI_Pdftable3_cell1 = new PdfPCell(
                    new Paragraph(
                            "The main objective of the illustration is that the client is able to appreciate the features of the product and the flow of benefits in different circumstances with some level of quantification. For further information on the product, its benefits  please refer to the sales brochure and/or policy document.",
                            small_normal));

            BI_Pdftable3_cell1.setPadding(5);

            BI_Pdftable3.addCell(BI_Pdftable3_cell1);
            document.add(BI_Pdftable3);

            PdfPTable BI_Pdftable4 = new PdfPTable(1);
            BI_Pdftable4.setWidthPercentage(100);
            // CR_table6.setWidths(columnWidths_4);
            PdfPCell BI_Pdftable4_cell1 = new PdfPCell(
                    new Paragraph(
                            "Statutory Warning-"
                                    + "Some benefits are guaranteed and some benefits are variable with returns based on the future performance of your Insurer carrying on life insurance business. If your policy offers guaranteed returns then these will be clearly marked 'guaranteed' in the illustration table on this page. If your policy offers variable returns then the illustrations on this page will show two different rates of assumed future investment returns. These assumed rates of return are not guaranteed and they are not the upper or lower limits of what you might get back, as the value of your policy is dependent on a number of factors including future investment performance.",
                            small_normal));

            BI_Pdftable4_cell1.setPadding(5);

            BI_Pdftable4.addCell(BI_Pdftable4_cell1);
            //document.add(BI_Pdftable4);
            document.add(para_img_logo_after_space_1);

            PdfPTable BI_PdftablePlanDetails = new PdfPTable(1);
            BI_PdftablePlanDetails.setWidthPercentage(100);
            PdfPCell BI_PdftablePlanDetails_cell = new PdfPCell(new Paragraph(
                    "Proposer and Life Assured Details", small_bold));

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
                    "Name of the Prospect/Policyholder", small_normal));
            cell_LifeAssuredName1.setPadding(5);
            PdfPCell cell_lLifeAssuredName2;

            if (proposer_Is_Same_As_Life_Assured.equalsIgnoreCase("Y")) {
                cell_lLifeAssuredName2 = new PdfPCell(new Paragraph(
                        name_of_life_assured, small_bold));
                cell_lLifeAssuredName2.setPadding(5);
                cell_lLifeAssuredName2.setHorizontalAlignment(Element.ALIGN_CENTER);

                table_lifeAssuredName.addCell(cell_LifeAssuredName1);
                table_lifeAssuredName.addCell(cell_lLifeAssuredName2);
            } else if (proposer_Is_Same_As_Life_Assured.equalsIgnoreCase("N")) {
                cell_lLifeAssuredName2 = new PdfPCell(new Paragraph(
                        name_of_proposer, small_bold));
                cell_lLifeAssuredName2.setPadding(5);
                cell_lLifeAssuredName2.setHorizontalAlignment(Element.ALIGN_CENTER);

                table_lifeAssuredName.addCell(cell_LifeAssuredName1);
                table_lifeAssuredName.addCell(cell_lLifeAssuredName2);
            }


            PdfPCell cell_LifeAssuredName11 = new PdfPCell(new Paragraph(
                    "Name of the Life Assured", small_normal));
            cell_LifeAssuredName11.setPadding(5);
            PdfPCell cell_lLifeAssuredName22;

            if (proposer_Is_Same_As_Life_Assured.equalsIgnoreCase("Y")) {
                cell_lLifeAssuredName22 = new PdfPCell(new Paragraph(
                        name_of_life_assured, small_bold));
                cell_lLifeAssuredName22.setPadding(5);
                cell_lLifeAssuredName22.setHorizontalAlignment(Element.ALIGN_CENTER);

                table_lifeAssuredName.addCell(cell_LifeAssuredName11);
                table_lifeAssuredName.addCell(cell_lLifeAssuredName22);
            } else if (proposer_Is_Same_As_Life_Assured.equalsIgnoreCase("N")) {
                cell_lLifeAssuredName22 = new PdfPCell(new Paragraph(
                        name_of_life_assured, small_bold));
                cell_lLifeAssuredName22.setPadding(5);
                cell_lLifeAssuredName22.setHorizontalAlignment(Element.ALIGN_CENTER);

                table_lifeAssuredName.addCell(cell_LifeAssuredName11);
                table_lifeAssuredName.addCell(cell_lLifeAssuredName22);
            }

            PdfPCell cell_lifeAssuredAge1 = new PdfPCell(new Paragraph(
                    "Age (Years)", small_normal));
            cell_lifeAssuredAge1.setPadding(5);
            PdfPCell cell_lifeAssuredAge2;

            if (proposer_Is_Same_As_Life_Assured.equalsIgnoreCase("Y")) {
                cell_lifeAssuredAge2 = new PdfPCell(new Paragraph(
                        age_entry + "", small_bold));
                cell_lifeAssuredAge2.setPadding(5);
                cell_lifeAssuredAge2.setHorizontalAlignment(Element.ALIGN_CENTER);
                table_lifeAssuredName.addCell(cell_lifeAssuredAge1);
                table_lifeAssuredName.addCell(cell_lifeAssuredAge2);
            } else if (proposer_Is_Same_As_Life_Assured.equalsIgnoreCase("N")) {
                cell_lifeAssuredAge2 = new PdfPCell(new Paragraph(
                        ProposerAge + "", small_bold));
                cell_lifeAssuredAge2.setPadding(5);
                cell_lifeAssuredAge2.setHorizontalAlignment(Element.ALIGN_CENTER);
                table_lifeAssuredName.addCell(cell_lifeAssuredAge1);
                table_lifeAssuredName.addCell(cell_lifeAssuredAge2);
            }

            PdfPCell cell_lifeAssuredAge11 = new PdfPCell(new Paragraph(
                    "Age (Years)", small_normal));
            cell_lifeAssuredAge11.setPadding(5);

            PdfPCell cell_lifeAssuredAge22;

            if (proposer_Is_Same_As_Life_Assured.equalsIgnoreCase("Y")) {
                cell_lifeAssuredAge22 = new PdfPCell(new Paragraph(
                        age_entry + "", small_bold));
                cell_lifeAssuredAge22.setPadding(5);
                cell_lifeAssuredAge22.setHorizontalAlignment(Element.ALIGN_CENTER);
                table_lifeAssuredName.addCell(cell_lifeAssuredAge11);
                table_lifeAssuredName.addCell(cell_lifeAssuredAge22);
            } else if (proposer_Is_Same_As_Life_Assured.equalsIgnoreCase("N")) {
                cell_lifeAssuredAge22 = new PdfPCell(new Paragraph(
                        age_entry + "", small_bold));
                cell_lifeAssuredAge22.setPadding(5);
                cell_lifeAssuredAge22.setHorizontalAlignment(Element.ALIGN_CENTER);
                table_lifeAssuredName.addCell(cell_lifeAssuredAge11);
                table_lifeAssuredName.addCell(cell_lifeAssuredAge22);
            }


            document.add(table_lifeAssuredName);

            PdfPTable table_lifeAssuredDetails = new PdfPTable(4);
            table_lifeAssuredDetails.setWidthPercentage(100);

            PdfPCell cell_lifeAssuredAmaturityGender1 = new PdfPCell(
                    new Paragraph("Gender", small_normal));
            cell_lifeAssuredAmaturityGender1.setPadding(5);
            PdfPCell cell_lifeAssuredAmaturityGender2;
            if (proposer_Is_Same_As_Life_Assured.equalsIgnoreCase("Y")) {

                cell_lifeAssuredAmaturityGender2 = new PdfPCell(
                        new Paragraph(gender, small_bold));
                cell_lifeAssuredAmaturityGender2.setPadding(5);
                cell_lifeAssuredAmaturityGender2
                        .setHorizontalAlignment(Element.ALIGN_CENTER);
                table_lifeAssuredDetails.addCell(cell_lifeAssuredAmaturityGender1);
                table_lifeAssuredDetails.addCell(cell_lifeAssuredAmaturityGender2);
            } else if (proposer_Is_Same_As_Life_Assured.equalsIgnoreCase("N")) {
                cell_lifeAssuredAmaturityGender2 = new PdfPCell(
                        new Paragraph(propsoser_gender, small_bold));
                cell_lifeAssuredAmaturityGender2.setPadding(5);
                cell_lifeAssuredAmaturityGender2
                        .setHorizontalAlignment(Element.ALIGN_CENTER);

                table_lifeAssuredDetails.addCell(cell_lifeAssuredAmaturityGender1);
                table_lifeAssuredDetails.addCell(cell_lifeAssuredAmaturityGender2);
            }

            PdfPCell cell_lifeAssuredAmaturityGender11 = new PdfPCell(
                    new Paragraph("Gender", small_normal));
            cell_lifeAssuredAmaturityGender11.setPadding(5);
            PdfPCell cell_lifeAssuredAmaturityGender22;
            if (proposer_Is_Same_As_Life_Assured.equalsIgnoreCase("Y")) {

                cell_lifeAssuredAmaturityGender22 = new PdfPCell(
                        new Paragraph(gender, small_bold));
                cell_lifeAssuredAmaturityGender22.setPadding(5);
                cell_lifeAssuredAmaturityGender22
                        .setHorizontalAlignment(Element.ALIGN_CENTER);
                table_lifeAssuredDetails.addCell(cell_lifeAssuredAmaturityGender11);
                table_lifeAssuredDetails.addCell(cell_lifeAssuredAmaturityGender22);
            } else if (proposer_Is_Same_As_Life_Assured.equalsIgnoreCase("N")) {
                cell_lifeAssuredAmaturityGender22 = new PdfPCell(
                        new Paragraph(gender, small_bold));
                cell_lifeAssuredAmaturityGender22.setPadding(5);
                cell_lifeAssuredAmaturityGender22
                        .setHorizontalAlignment(Element.ALIGN_CENTER);
                table_lifeAssuredDetails.addCell(cell_lifeAssuredAmaturityGender11);
                table_lifeAssuredDetails.addCell(cell_lifeAssuredAmaturityGender22);
            }


            //table_lifeAssuredDetails
            //		.addCell(cell_lifeAssured_Premium_frequeny1);
            //table_lifeAssuredDetails
            //		.addCell(cell_lifeAssured_Premium_frequeny2);
            document.add(table_lifeAssuredDetails);

            String isStaff;
            if (cb_staffdisc.isChecked()) {
                isStaff = "yes";
                PdfPTable table_staff_NonStaff = new PdfPTable(2);
                table_staff_NonStaff.setWidthPercentage(100);

                PdfPCell cell_staff_NonStaff1 = new PdfPCell(new Paragraph(
                        "Staff", small_normal));
                cell_staff_NonStaff1.setPadding(5);

                PdfPCell cell_staff_NonStaff2 = new PdfPCell(new Paragraph(
                        isStaff, small_bold));
                cell_staff_NonStaff2.setPadding(5);
                cell_staff_NonStaff2
                        .setHorizontalAlignment(Element.ALIGN_CENTER);

                table_staff_NonStaff.addCell(cell_staff_NonStaff1);
                table_staff_NonStaff.addCell(cell_staff_NonStaff2);
                document.add(table_staff_NonStaff);

            } else {
                PdfPTable table_staff_NonStaff = new PdfPTable(2);
                table_staff_NonStaff.setWidthPercentage(100);

                PdfPCell cell_staff_NonStaff1 = new PdfPCell(new Paragraph(
                        "Staff", small_normal));
                cell_staff_NonStaff1.setPadding(5);
                PdfPCell cell_staff_NonStaff2 = new PdfPCell(new Paragraph(
                        "No", small_bold));
                cell_staff_NonStaff2.setPadding(5);
                cell_staff_NonStaff2
                        .setHorizontalAlignment(Element.ALIGN_CENTER);

                table_staff_NonStaff.addCell(cell_staff_NonStaff1);
                table_staff_NonStaff.addCell(cell_staff_NonStaff2);
                document.add(table_staff_NonStaff);
            }

            /*String isState;
            if (cb_kerladisc.isChecked()) {
                isState = "Kerala";
                PdfPTable Statetable = new PdfPTable(2);
                Statetable.setWidthPercentage(100);

                PdfPCell state1 = new PdfPCell(new Paragraph(
                        "State", small_normal));
                state1.setPadding(5);

                PdfPCell state2 = new PdfPCell(new Paragraph(
                        isState, small_bold));
                state2.setPadding(5);
                state2
                        .setHorizontalAlignment(Element.ALIGN_CENTER);

                Statetable.addCell(state1);
                Statetable.addCell(state2);
                document.add(Statetable);

            } else {
                PdfPTable Statetable = new PdfPTable(2);
                Statetable.setWidthPercentage(100);

                PdfPCell state = new PdfPCell(new Paragraph(
                        "State", small_normal));
                state.setPadding(5);
                PdfPCell state2 = new PdfPCell(new Paragraph(
                        "Non Kerala", small_bold));
                state2.setPadding(5);
                state2.setHorizontalAlignment(Element.ALIGN_CENTER);

                Statetable.addCell(state);
                Statetable.addCell(state2);
                document.add(Statetable);
            }*/

            String isJk;
            if (cb_bi_smart_income_protect_JKResident.isChecked()) {
                isJk = "yes";
                PdfPTable table_is_JK = new PdfPTable(2);
                table_is_JK.setWidthPercentage(100);

                PdfPCell cell_is_JK1 = new PdfPCell(new Paragraph("J&K",
                        small_normal));
                cell_is_JK1.setPadding(5);

                PdfPCell cell_is_JK2 = new PdfPCell(new Paragraph(isJk,
                        small_bold));
                cell_is_JK2.setPadding(5);
                cell_is_JK2.setHorizontalAlignment(Element.ALIGN_CENTER);

                table_is_JK.addCell(cell_is_JK1);
                table_is_JK.addCell(cell_is_JK2);
                document.add(table_is_JK);

            }

            document.add(para_img_logo_after_space_1);

            PdfPTable BI_Pdftable_How = new PdfPTable(1);
            BI_Pdftable_How.setWidthPercentage(100);
            PdfPCell BI_Pdftable_read = new PdfPCell(new Paragraph(
                    "How to read and understand this benefit illustration?", small_bold));

            BI_Pdftable_read
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
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


            PdfPTable BI_PdftablePremiumforBasicCover = new PdfPTable(1);
            BI_PdftablePremiumforBasicCover.setWidthPercentage(100);
            PdfPCell BI_PdftablePremiumforBasicCovercell = new PdfPCell(
                    new Paragraph("Policy Details", small_bold));

            BI_PdftablePremiumforBasicCovercell
                    .setBackgroundColor(BaseColor.LIGHT_GRAY);

            BI_PdftablePremiumforBasicCovercell
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftablePremiumforBasicCovercell.setPadding(5);

            BI_PdftablePremiumforBasicCover
                    .addCell(BI_PdftablePremiumforBasicCovercell);
            document.add(BI_PdftablePremiumforBasicCover);

            PdfPTable Table_policyTerm_annualisedPremium_sumAssured = new PdfPTable(
                    4);
            Table_policyTerm_annualisedPremium_sumAssured
                    .setWidthPercentage(100);

            PdfPCell policy_option = new PdfPCell(new Paragraph("Policy Option",
                    small_normal));
            PdfPCell policy_option2 = new PdfPCell(new Paragraph("Not Applicable"
                    + "", small_bold));
            policy_option2.setPadding(5);
            policy_option2.setPadding(5);
            policy_option2.setHorizontalAlignment(Element.ALIGN_CENTER);

            PdfPCell cell_premium_paying_term1 = new PdfPCell(new Paragraph(
                    " Amount of Installment Premium (Rs.)", small_normal));
            cell_premium_paying_term1.setPadding(5);
            PdfPCell cell_premium_paying_term2 = new PdfPCell(new Paragraph(
                    ""
                            + getformatedThousandString(Integer.parseInt(obj.getRound(obj.getStringWithout_E(Double
                            .valueOf((Premium_pdf.equals("") || Premium_pdf == null) ? "0"
                                    : Premium_pdf)))))
                    , small_bold));

            cell_premium_paying_term2.setPadding(5);
            cell_premium_paying_term2
                    .setHorizontalAlignment(Element.ALIGN_CENTER);

            PdfPCell ppo = new PdfPCell(new Paragraph("Premium Payment Option", small_normal));
            String str_premPay;
            if (premium_paying_frequency.equalsIgnoreCase("Single")) {
                str_premPay = "Single";
            } else {
                str_premPay = "Regular";
            }

            PdfPCell ppo2 = new PdfPCell(new Paragraph(str_premPay + "", small_bold));
            ppo2.setPadding(5);
            ppo2.setPadding(5);
            ppo2.setHorizontalAlignment(Element.ALIGN_CENTER);

            PdfPCell srb = new PdfPCell(new Paragraph("Bonus Type",
                    small_normal));
            PdfPCell srb2 = new PdfPCell(new Paragraph("Simple Rversionary Bonus"
                    + "", small_bold));
            srb2.setPadding(5);
            srb2.setPadding(5);
            srb2.setHorizontalAlignment(Element.ALIGN_CENTER);


            PdfPCell cell_Term1 = new PdfPCell(new Paragraph("Policy Term (Years)",
                    small_normal));
            PdfPCell cell_Term2 = new PdfPCell(new Paragraph(policy_term
                    + "", small_bold));
            cell_Term1.setPadding(5);
            cell_Term2.setPadding(5);
            cell_Term2.setHorizontalAlignment(Element.ALIGN_CENTER);


            PdfPCell cell_plan1 = new PdfPCell(new Paragraph("Sum Assured (Rs.)",
                    small_normal));
            cell_plan1.setPadding(5);
            PdfPCell cell_plan2 = new PdfPCell(
                    new Paragraph(
                            ""
                                    + getformatedThousandString(Integer.parseInt(obj.getRound(obj.getStringWithout_E(Double
                                    .valueOf((sum_assured.equals("") || sum_assured == null) ? "0"
                                            : sum_assured))))),
                            small_bold));

            cell_plan2.setPadding(5);
            cell_plan2.setHorizontalAlignment(Element.ALIGN_CENTER);


            String payingTerm;
            if (premium_paying_frequency.equalsIgnoreCase("Single")) {
                payingTerm = "1 ";
            } else {
                payingTerm = policy_term + " ";
            }

            PdfPCell cell_PremiumPayingTerm1 = new PdfPCell(new Paragraph(
                    "Premium Payment Term (Years)", small_normal));
            PdfPCell cell_PremiumPayingTerm2 = new PdfPCell(new Paragraph(
                    payingTerm, small_bold));
            cell_PremiumPayingTerm1.setPadding(5);
            cell_PremiumPayingTerm2.setPadding(5);
            cell_PremiumPayingTerm2
                    .setHorizontalAlignment(Element.ALIGN_CENTER);

            PdfPCell cell_plan11 = new PdfPCell(new Paragraph("Sum Assured on Death (at inception of the policy) (Rs.) ",
                    small_normal));
            cell_plan11.setPadding(5);
            PdfPCell cell_plan22 = new PdfPCell(new Paragraph(getformatedThousandString(Integer.parseInt(obj.getRound(obj
                    .getStringWithout_E(Double.valueOf((sum_assured_on_death
                            .equals("") || sum_assured_on_death == null) ? "0"
                            : sum_assured_on_death))))),
                    small_normal));
            cell_plan22.setPadding(5);


            PdfPCell cell_lifeAssured_Premium_frequeny1 = new PdfPCell(
                    new Paragraph("Mode / Frequency of Premium Payment", small_normal));
            cell_lifeAssured_Premium_frequeny1.setPadding(5);
            PdfPCell cell_lifeAssured_Premium_frequeny2 = new PdfPCell(
                    new Paragraph(premium_paying_frequency, small_bold));
            cell_lifeAssured_Premium_frequeny2.setPadding(5);
            cell_lifeAssured_Premium_frequeny2
                    .setHorizontalAlignment(Element.ALIGN_CENTER);

            PdfPCell rate_of_appl_taxes = new PdfPCell(new Paragraph("Rate of Applicable Taxes", small_normal));
            PdfPCell rate_of_appl_taxes2;
            if (cb_kerladisc.isChecked()) {
                rate_of_appl_taxes2 = new PdfPCell(new Paragraph("4.75% in the 1st policy year and 2.375% from 2nd policy year onwards", small_normal));

            } else {
                rate_of_appl_taxes2 = new PdfPCell(new Paragraph("4.5% in the 1st policy year and 2.25% from 2nd policy year onwards", small_normal));
            }

            PdfPCell maturity_ben_option = new PdfPCell(new Paragraph("Maturity Benefit option", small_normal));
            PdfPCell maturity_ben_option2;
            if (lumpSumAmount.equalsIgnoreCase("Y")) {
                maturity_ben_option2 = new PdfPCell(new Paragraph("Lump sum", small_normal));

            } else {
                maturity_ben_option2 = new PdfPCell(new Paragraph("Installments", small_normal));
            }

            PdfPCell maturity_ben_option3 = new PdfPCell(new Paragraph("", small_normal));
            PdfPCell maturity_ben_option4 = new PdfPCell(new Paragraph("", small_normal));

            Table_policyTerm_annualisedPremium_sumAssured.addCell(policy_option);
            Table_policyTerm_annualisedPremium_sumAssured.addCell(policy_option2);
            Table_policyTerm_annualisedPremium_sumAssured.addCell(cell_premium_paying_term1);
            Table_policyTerm_annualisedPremium_sumAssured.addCell(cell_premium_paying_term2);
            Table_policyTerm_annualisedPremium_sumAssured.addCell(ppo);
            Table_policyTerm_annualisedPremium_sumAssured.addCell(ppo2);
            Table_policyTerm_annualisedPremium_sumAssured.addCell(srb);
            Table_policyTerm_annualisedPremium_sumAssured.addCell(srb2);

            Table_policyTerm_annualisedPremium_sumAssured.addCell(cell_Term1);
            Table_policyTerm_annualisedPremium_sumAssured.addCell(cell_Term2);

            Table_policyTerm_annualisedPremium_sumAssured.addCell(cell_plan1);
            Table_policyTerm_annualisedPremium_sumAssured.addCell(cell_plan2);


            Table_policyTerm_annualisedPremium_sumAssured
                    .addCell(cell_PremiumPayingTerm1);
            Table_policyTerm_annualisedPremium_sumAssured
                    .addCell(cell_PremiumPayingTerm2);

            Table_policyTerm_annualisedPremium_sumAssured.addCell(cell_plan11);
            Table_policyTerm_annualisedPremium_sumAssured.addCell(cell_plan22);

            Table_policyTerm_annualisedPremium_sumAssured.addCell(cell_lifeAssured_Premium_frequeny1);
            Table_policyTerm_annualisedPremium_sumAssured.addCell(cell_lifeAssured_Premium_frequeny2);

            Table_policyTerm_annualisedPremium_sumAssured.addCell(rate_of_appl_taxes);
            Table_policyTerm_annualisedPremium_sumAssured.addCell(rate_of_appl_taxes2);

            Table_policyTerm_annualisedPremium_sumAssured.addCell(maturity_ben_option);
            Table_policyTerm_annualisedPremium_sumAssured.addCell(maturity_ben_option2);
            Table_policyTerm_annualisedPremium_sumAssured.addCell(maturity_ben_option3);
            Table_policyTerm_annualisedPremium_sumAssured.addCell(maturity_ben_option4);

            document.add(Table_policyTerm_annualisedPremium_sumAssured);

            PdfPTable table_plan_premium_payingTerm = new PdfPTable(4);
            table_plan_premium_payingTerm.setWidthPercentage(100);


            //table_plan_premium_payingTerm.addCell(cell_plan1);
            //table_plan_premium_payingTerm.addCell(cell_plan2);

            //table_plan_premium_payingTerm.addCell(cell_premium_paying_term1);
            //table_plan_premium_payingTerm.addCell(cell_premium_paying_term2);
            document.add(table_plan_premium_payingTerm);

            if (ptr_rider_status.equals("true")) {
                document.add(para_img_logo_after_space_1);
                PdfPTable BI_pdftable_prefferedTermAssuredRider = new PdfPTable(
                        1);
                BI_pdftable_prefferedTermAssuredRider.setWidthPercentage(100);
                PdfPCell BI_pdftable_prefferedTermAssuredRider_cell = new PdfPCell(
                        new Paragraph(
                                "Rider Details",
                                small_bold));

                BI_pdftable_prefferedTermAssuredRider_cell
                        .setBackgroundColor(BaseColor.LIGHT_GRAY);

                BI_pdftable_prefferedTermAssuredRider_cell
                        .setHorizontalAlignment(Element.ALIGN_CENTER);
                BI_pdftable_prefferedTermAssuredRider_cell.setPadding(5);

                BI_pdftable_prefferedTermAssuredRider
                        .addCell(BI_pdftable_prefferedTermAssuredRider_cell);
                document.add(BI_pdftable_prefferedTermAssuredRider);

                PdfPTable Table_Preferred_term_assured_policyTerm_annualisedPremium_sumAssured = new PdfPTable(
                        5);
                Table_Preferred_term_assured_policyTerm_annualisedPremium_sumAssured
                        .setWidthPercentage(100);

                PdfPCell rider_name = new PdfPCell(new Paragraph("Rider Name", small_normal));
                PdfPCell rider_name2 = new PdfPCell(new Paragraph("Rider Policy Term (years)", small_normal));
                PdfPCell rider_name3 = new PdfPCell(new Paragraph("Rider Sum Assured (Rs.)", small_normal));
                PdfPCell rider_name4 = new PdfPCell(new Paragraph("Rider Premium payment Term (years)", small_normal));
                PdfPCell rider_name5 = new PdfPCell(new Paragraph("Rider Premium (Rs.)", small_normal));

                PdfPCell cell_Preferred_term_assured_PlanProposed1 = new PdfPCell(
                        new Paragraph("SBI Life -Preferred Term Rider (UIN:111B014V02)", small_normal));
                cell_Preferred_term_assured_PlanProposed1.setPadding(5);
                PdfPCell cell_Preferred_term_assured_tPlanProposed2 = new PdfPCell(
                        new Paragraph(term_pta + "", small_bold));
                cell_Preferred_term_assured_tPlanProposed2.setPadding(5);
                cell_Preferred_term_assured_tPlanProposed2
                        .setHorizontalAlignment(Element.ALIGN_CENTER);


                PdfPCell cell_Preferred_term_assured_Term2 = new PdfPCell(
                        new Paragraph(" "
                                + getformatedThousandString(Integer.parseInt(obj.getRound(obj.getStringWithout_E(Double
                                .valueOf(sa_pta.equals("") ? "0"
                                        : sa_pta))))), small_bold));
                cell_Preferred_term_assured_Term2.setPadding(5);
                cell_Preferred_term_assured_Term2
                        .setHorizontalAlignment(Element.ALIGN_CENTER);

                PdfPCell rider_premium_payment_term = new PdfPCell(
                        new Paragraph(term_pta + "", small_normal));
                rider_premium_payment_term.setPadding(5);

				/*PdfPCell cell_Preferred_term_assured_PremiumPayingTerm1 = new PdfPCell(
						new Paragraph(premium_paying_frequency + " Premium",
								small_normal));*/
                PdfPCell cell_Preferred_term_assured_PremiumPayingTerm2 = new PdfPCell(
                        new Paragraph(" "
                                + getformatedThousandString(Integer.parseInt(obj.getRound(obj.getStringWithout_E(Double
                                .valueOf(prem_pta.equals("") ? "0"
                                        : prem_pta))))), small_bold));
                cell_Preferred_term_assured_PremiumPayingTerm2.setPadding(5);
                cell_Preferred_term_assured_PremiumPayingTerm2
                        .setHorizontalAlignment(Element.ALIGN_CENTER);

                Table_Preferred_term_assured_policyTerm_annualisedPremium_sumAssured
                        .addCell(rider_name);
                Table_Preferred_term_assured_policyTerm_annualisedPremium_sumAssured
                        .addCell(rider_name2);
                Table_Preferred_term_assured_policyTerm_annualisedPremium_sumAssured
                        .addCell(rider_name3);
                Table_Preferred_term_assured_policyTerm_annualisedPremium_sumAssured
                        .addCell(rider_name4);
                Table_Preferred_term_assured_policyTerm_annualisedPremium_sumAssured
                        .addCell(rider_name5);


                Table_Preferred_term_assured_policyTerm_annualisedPremium_sumAssured
                        .addCell(cell_Preferred_term_assured_PlanProposed1);
                Table_Preferred_term_assured_policyTerm_annualisedPremium_sumAssured
                        .addCell(cell_Preferred_term_assured_tPlanProposed2);


                Table_Preferred_term_assured_policyTerm_annualisedPremium_sumAssured
                        .addCell(cell_Preferred_term_assured_Term2);

                Table_Preferred_term_assured_policyTerm_annualisedPremium_sumAssured
                        .addCell(rider_premium_payment_term);

                //Table_Preferred_term_assured_policyTerm_annualisedPremium_sumAssured
                //		.addCell(cell_Preferred_term_assured_PremiumPayingTerm1);
                Table_Preferred_term_assured_policyTerm_annualisedPremium_sumAssured
                        .addCell(cell_Preferred_term_assured_PremiumPayingTerm2);

                document.add(Table_Preferred_term_assured_policyTerm_annualisedPremium_sumAssured);
            }

            if (adb_rider_status.equals("true")) {
                //document.add(para_img_logo_after_space_1);
                PdfPTable BI_pdftable_AccidetnalDeathBenefitRider = new PdfPTable(
                        1);
                BI_pdftable_AccidetnalDeathBenefitRider.setWidthPercentage(100);

                PdfPCell BI_pdftable_AccidetnalDeathBenefitRider_cell = new PdfPCell(
                        new Paragraph(
                                "SBI Life - Accidental Death Benefit Rider(UIN:111B015V02)",
                                small_bold));

                BI_pdftable_AccidetnalDeathBenefitRider_cell
                        .setBackgroundColor(BaseColor.LIGHT_GRAY);

                BI_pdftable_AccidetnalDeathBenefitRider_cell
                        .setHorizontalAlignment(Element.ALIGN_CENTER);
                BI_pdftable_AccidetnalDeathBenefitRider_cell.setPadding(5);

                BI_pdftable_AccidetnalDeathBenefitRider
                        .addCell(BI_pdftable_AccidetnalDeathBenefitRider_cell);
                //document.add(BI_pdftable_AccidetnalDeathBenefitRider);

                PdfPTable Table_AccidetnalDeathBenefitRider_policyTerm_annualisedPremium_sumAssured = new PdfPTable(
                        5);
                Table_AccidetnalDeathBenefitRider_policyTerm_annualisedPremium_sumAssured
                        .setWidthPercentage(100);

                PdfPCell cell_AccidetnalDeathBenefitRider_PlanProposed1 = new PdfPCell(
                        new Paragraph("SBI Life -Accidental Death Benefit Rider(UIN: 111B015V03)", small_normal));
                cell_AccidetnalDeathBenefitRider_PlanProposed1.setPadding(5);
                PdfPCell cell_AccidetnalDeathBenefitRider_tPlanProposed2 = new PdfPCell(
                        new Paragraph(term_adb + "", small_bold));
                cell_AccidetnalDeathBenefitRider_tPlanProposed2.setPadding(5);
                cell_AccidetnalDeathBenefitRider_tPlanProposed2
                        .setHorizontalAlignment(Element.ALIGN_CENTER);


                PdfPCell cell_AccidetnalDeathBenefitRider_Term2 = new PdfPCell(
                        new Paragraph(""
                                + getformatedThousandString(Integer.parseInt(obj.getRound(obj.getStringWithout_E(Double
                                .valueOf(sa_adb.equals("") ? "0"
                                        : sa_adb))))), small_bold));
                cell_AccidetnalDeathBenefitRider_Term2.setPadding(5);
                cell_AccidetnalDeathBenefitRider_Term2
                        .setHorizontalAlignment(Element.ALIGN_CENTER);

                PdfPCell rider_premium_payment_term = new PdfPCell(
                        new Paragraph(term_pta + "", small_normal));
                rider_premium_payment_term.setPadding(5);

				/*PdfPCell cell_AccidetnalDeathBenefitRider_PremiumPayingTerm1 = new PdfPCell(
						new Paragraph(premium_paying_frequency + " Premium",
								small_normal));*/
                PdfPCell cell_AccidetnalDeathBenefitRider_PremiumPayingTerm2 = new PdfPCell(
                        new Paragraph(" "
                                + getformatedThousandString(Integer.parseInt(obj.getRound(obj.getStringWithout_E(Double
                                .valueOf(prem_adb.equals("") ? "0"
                                        : prem_adb))))), small_bold));
                //cell_AccidetnalDeathBenefitRider_PremiumPayingTerm1
                //		.setPadding(5);
                cell_AccidetnalDeathBenefitRider_PremiumPayingTerm2
                        .setPadding(5);
                cell_AccidetnalDeathBenefitRider_PremiumPayingTerm2
                        .setHorizontalAlignment(Element.ALIGN_CENTER);

                Table_AccidetnalDeathBenefitRider_policyTerm_annualisedPremium_sumAssured
                        .addCell(cell_AccidetnalDeathBenefitRider_PlanProposed1);
                Table_AccidetnalDeathBenefitRider_policyTerm_annualisedPremium_sumAssured
                        .addCell(cell_AccidetnalDeathBenefitRider_tPlanProposed2);


                Table_AccidetnalDeathBenefitRider_policyTerm_annualisedPremium_sumAssured
                        .addCell(cell_AccidetnalDeathBenefitRider_Term2);

                Table_AccidetnalDeathBenefitRider_policyTerm_annualisedPremium_sumAssured
                        .addCell(rider_premium_payment_term);

                Table_AccidetnalDeathBenefitRider_policyTerm_annualisedPremium_sumAssured
                        .addCell(cell_AccidetnalDeathBenefitRider_PremiumPayingTerm2);
                document.add(Table_AccidetnalDeathBenefitRider_policyTerm_annualisedPremium_sumAssured);
            }

            if (atpdb_rider_status.equals("true")) {
                //document.add(para_img_logo_after_space_1);
                PdfPTable BI_pdftable_AccidetnalAndParmantRider = new PdfPTable(
                        1);
                BI_pdftable_AccidetnalAndParmantRider.setWidthPercentage(100);

                PdfPCell BI_pdftable_AccidetnalAndParmantRider_cell = new PdfPCell(
                        new Paragraph(
                                "SBI Life - Accidental Total And Permanent Disability Benefit Rider(UIN:111B016V02)",
                                small_bold));

                BI_pdftable_AccidetnalAndParmantRider_cell
                        .setBackgroundColor(BaseColor.LIGHT_GRAY);

                BI_pdftable_AccidetnalAndParmantRider_cell
                        .setHorizontalAlignment(Element.ALIGN_CENTER);
                BI_pdftable_AccidetnalAndParmantRider_cell.setPadding(5);

                BI_pdftable_AccidetnalAndParmantRider
                        .addCell(BI_pdftable_AccidetnalAndParmantRider_cell);
                //document.add(BI_pdftable_AccidetnalAndParmantRider);

                PdfPTable Table_AccidetnalAndParmantRider_policyTerm_annualisedPremium_sumAssured = new PdfPTable(
                        5);
                Table_AccidetnalAndParmantRider_policyTerm_annualisedPremium_sumAssured
                        .setWidthPercentage(100);

                PdfPCell cell_AccidetnalAndParmantRider_PlanProposed1 = new PdfPCell(
                        new Paragraph("SBI Life -Accidental Total & Permanent Disability Benefit Rider(UIN: 111B016V03)", small_normal));
                cell_AccidetnalAndParmantRider_PlanProposed1.setPadding(5);
                PdfPCell cell_AccidetnalAndParmantRider_tPlanProposed2 = new PdfPCell(
                        new Paragraph(term_atpdb + "", small_bold));
                cell_AccidetnalAndParmantRider_tPlanProposed2.setPadding(5);
                cell_AccidetnalAndParmantRider_tPlanProposed2
                        .setHorizontalAlignment(Element.ALIGN_CENTER);


                PdfPCell cell_AccidetnalAndParmantRider_Term2 = new PdfPCell(
                        new Paragraph(" "
                                + getformatedThousandString(Integer.parseInt(obj.getRound(obj.getStringWithout_E(Double
                                .valueOf(sa_atpdb.equals("") ? "0"
                                        : sa_atpdb))))), small_bold));
                cell_AccidetnalAndParmantRider_Term2.setPadding(5);
                cell_AccidetnalAndParmantRider_Term2
                        .setHorizontalAlignment(Element.ALIGN_CENTER);

				/*PdfPCell cell_AccidetnalAndParmantRider_PremiumPayingTerm1 = new PdfPCell(
						new Paragraph(premium_paying_frequency + " Premium",
								small_normal));*/

                PdfPCell rider_premium_payment_term = new PdfPCell(
                        new Paragraph(term_pta + "", small_normal));
                rider_premium_payment_term.setPadding(5);

                PdfPCell cell_AccidetnalAndParmantRider_PremiumPayingTerm2 = new PdfPCell(
                        new Paragraph(" "
                                + getformatedThousandString(Integer.parseInt(obj.getRound(obj.getStringWithout_E(Double
                                .valueOf(prem_atpdb.equals("") ? "0"
                                        : prem_atpdb))))), small_bold));
                cell_AccidetnalAndParmantRider_PremiumPayingTerm2.setPadding(5);
                cell_AccidetnalAndParmantRider_PremiumPayingTerm2
                        .setHorizontalAlignment(Element.ALIGN_CENTER);

                Table_AccidetnalAndParmantRider_policyTerm_annualisedPremium_sumAssured
                        .addCell(cell_AccidetnalAndParmantRider_PlanProposed1);
                Table_AccidetnalAndParmantRider_policyTerm_annualisedPremium_sumAssured
                        .addCell(cell_AccidetnalAndParmantRider_tPlanProposed2);


                Table_AccidetnalAndParmantRider_policyTerm_annualisedPremium_sumAssured
                        .addCell(cell_AccidetnalAndParmantRider_Term2);

                Table_AccidetnalAndParmantRider_policyTerm_annualisedPremium_sumAssured
                        .addCell(rider_premium_payment_term);

                Table_AccidetnalAndParmantRider_policyTerm_annualisedPremium_sumAssured
                        .addCell(cell_AccidetnalAndParmantRider_PremiumPayingTerm2);
                document.add(Table_AccidetnalAndParmantRider_policyTerm_annualisedPremium_sumAssured);
            }

            if (cc13nl_rider_status.equals("true")) {

                document.add(para_img_logo_after_space_1);
                PdfPTable BI_pdftable_critiCare_NonLinkedTermAssuredRider = new PdfPTable(
                        1);
                BI_pdftable_critiCare_NonLinkedTermAssuredRider
                        .setWidthPercentage(100);
                PdfPCell BI_pdftable_critiCare_NonLinkedTermAssuredRider_cell = new PdfPCell(
                        new Paragraph(
                                "SBI Life - Criti Care 13 Non Linked Rider(UIN:111B025V02)",
                                small_bold));

                BI_pdftable_critiCare_NonLinkedTermAssuredRider_cell
                        .setBackgroundColor(BaseColor.LIGHT_GRAY);

                BI_pdftable_critiCare_NonLinkedTermAssuredRider_cell
                        .setHorizontalAlignment(Element.ALIGN_CENTER);
                BI_pdftable_critiCare_NonLinkedTermAssuredRider_cell
                        .setPadding(5);

                BI_pdftable_critiCare_NonLinkedTermAssuredRider
                        .addCell(BI_pdftable_critiCare_NonLinkedTermAssuredRider_cell);
                document.add(BI_pdftable_critiCare_NonLinkedTermAssuredRider);

                PdfPTable Table_CritiCare_term_assured_policyTerm_annualisedPremium_sumAssured = new PdfPTable(
                        6);
                Table_CritiCare_term_assured_policyTerm_annualisedPremium_sumAssured
                        .setWidthPercentage(100);

                PdfPCell cell_CritiCare_term_assured_PlanProposed1 = new PdfPCell(
                        new Paragraph("Term", small_normal));
                cell_CritiCare_term_assured_PlanProposed1.setPadding(5);
                PdfPCell cell_CritiCare_term_assured_tPlanProposed2 = new PdfPCell(
                        new Paragraph(term_cc13nl + "Years", small_bold));
                cell_CritiCare_term_assured_tPlanProposed2.setPadding(5);
                cell_CritiCare_term_assured_tPlanProposed2
                        .setHorizontalAlignment(Element.ALIGN_CENTER);

                PdfPCell cell_CritiCare_term_assured_Term1 = new PdfPCell(
                        new Paragraph("Sum Assured", small_normal));
                PdfPCell cell_CritiCare_term_assured_Term2 = new PdfPCell(
                        new Paragraph("Rs  "
                                + obj.getRound(obj.getStringWithout_E(Double
                                .valueOf(sa_pta_cc13nl.equals("") ? "0"
                                        : sa_pta_cc13nl))), small_bold));

                cell_CritiCare_term_assured_Term1.setPadding(5);
                cell_CritiCare_term_assured_Term2.setPadding(5);
                cell_CritiCare_term_assured_Term2
                        .setHorizontalAlignment(Element.ALIGN_CENTER);

                PdfPCell cell_CritiCare_term_assured_PremiumPayingTerm1 = new PdfPCell(
                        new Paragraph(premium_paying_frequency + " Premium",
                                small_normal));
                PdfPCell cell_CritiCare_term_assured_PremiumPayingTerm2 = new PdfPCell(
                        new Paragraph(
                                "Rs  "
                                        + obj.getRound(obj.getStringWithout_E(Double
                                        .valueOf(prem_pta_cc13nl
                                                .equals("") ? "0"
                                                : prem_pta_cc13nl))),
                                small_bold));
                cell_CritiCare_term_assured_PremiumPayingTerm1.setPadding(5);
                cell_CritiCare_term_assured_PremiumPayingTerm2.setPadding(5);
                cell_CritiCare_term_assured_PremiumPayingTerm2
                        .setHorizontalAlignment(Element.ALIGN_CENTER);

                Table_CritiCare_term_assured_policyTerm_annualisedPremium_sumAssured
                        .addCell(cell_CritiCare_term_assured_PlanProposed1);
                Table_CritiCare_term_assured_policyTerm_annualisedPremium_sumAssured
                        .addCell(cell_CritiCare_term_assured_tPlanProposed2);

                Table_CritiCare_term_assured_policyTerm_annualisedPremium_sumAssured
                        .addCell(cell_CritiCare_term_assured_Term1);
                Table_CritiCare_term_assured_policyTerm_annualisedPremium_sumAssured
                        .addCell(cell_CritiCare_term_assured_Term2);

                Table_CritiCare_term_assured_policyTerm_annualisedPremium_sumAssured
                        .addCell(cell_CritiCare_term_assured_PremiumPayingTerm1);
                Table_CritiCare_term_assured_policyTerm_annualisedPremium_sumAssured
                        .addCell(cell_CritiCare_term_assured_PremiumPayingTerm2);

                document.add(Table_CritiCare_term_assured_policyTerm_annualisedPremium_sumAssured);
            }
            document.add(para_img_logo_after_space_1);

            PdfPTable BI_Pdftable_totalPremiumforBaseProduct = new PdfPTable(1);
            BI_Pdftable_totalPremiumforBaseProduct.setWidthPercentage(100);
            PdfPCell BI_Pdftable_totalPremiumforBaseProductcell = new PdfPCell(
                    new Paragraph(
                            "Total Premium for Base Product & Rider (if any) (in Rs )",
                            small_bold));

            BI_Pdftable_totalPremiumforBaseProductcell
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_Pdftable_totalPremiumforBaseProductcell.setPadding(5);

            BI_Pdftable_totalPremiumforBaseProduct
                    .addCell(BI_Pdftable_totalPremiumforBaseProductcell);
            //document.add(BI_Pdftable_totalPremiumforBaseProduct);

            PdfPTable Table_AccidetnalAndParmantRider_BasicPremium_servicePremium_yearlyPremium = new PdfPTable(
                    4);
            Table_AccidetnalAndParmantRider_BasicPremium_servicePremium_yearlyPremium
                    .setWidthPercentage(100);

            PdfPCell cell_AccidetnalAndParmantRider_BasicPremium1 = new PdfPCell(
                    new Paragraph(premium_paying_frequency
                            + " Installment Base Premium", small_normal));
            cell_AccidetnalAndParmantRider_BasicPremium1.setPadding(5);
            PdfPCell cell_AccidetnalAndParmantRider_BasicPremium2 = new PdfPCell(
                    new Paragraph("Rs "
                            + obj.getRound(obj.getStringWithout_E(Double
                            .valueOf(basicprem.equals("") ? "0"
                                    : basicprem))), small_bold));
            cell_AccidetnalAndParmantRider_BasicPremium2.setPadding(5);
            cell_AccidetnalAndParmantRider_BasicPremium2
                    .setHorizontalAlignment(Element.ALIGN_CENTER);

            PdfPCell cell_AccidetnalAndParmantRider_ServiceTax1 = new PdfPCell(
                    new Paragraph("Applicable Taxes", small_normal));
            PdfPCell cell_AccidetnalAndParmantRider_ServiceTax2 = new PdfPCell(
                    new Paragraph("Rs  "
                            + obj.getRound(obj.getStringWithout_E(Double
                            .valueOf(servcTax.equals("") ? "0"
                                    : servcTax))), small_bold));
            cell_AccidetnalAndParmantRider_ServiceTax1.setPadding(5);
            cell_AccidetnalAndParmantRider_ServiceTax2.setPadding(5);
            cell_AccidetnalAndParmantRider_ServiceTax2
                    .setHorizontalAlignment(Element.ALIGN_CENTER);

            Table_AccidetnalAndParmantRider_BasicPremium_servicePremium_yearlyPremium
                    .addCell(cell_AccidetnalAndParmantRider_BasicPremium1);
            Table_AccidetnalAndParmantRider_BasicPremium_servicePremium_yearlyPremium
                    .addCell(cell_AccidetnalAndParmantRider_BasicPremium2);

            Table_AccidetnalAndParmantRider_BasicPremium_servicePremium_yearlyPremium
                    .addCell(cell_AccidetnalAndParmantRider_ServiceTax1);
            Table_AccidetnalAndParmantRider_BasicPremium_servicePremium_yearlyPremium
                    .addCell(cell_AccidetnalAndParmantRider_ServiceTax2);

            //document.add(Table_AccidetnalAndParmantRider_BasicPremium_servicePremium_yearlyPremium);

            PdfPTable Table_backdating_premium_with_service_tax = new PdfPTable(
                    2);
            Table_backdating_premium_with_service_tax.setWidthPercentage(100);

            PdfPCell cell_Backdate1 = new PdfPCell(new Paragraph(
                    "Backdating Interest", small_normal));
            cell_Backdate1.setPadding(5);

            PdfPCell cell_Backdate2 = new PdfPCell(new Paragraph("Rs "
                    + obj.getRound(obj.getStringWithout_E(Double
                    .valueOf(BackdatingInt.equals("") ? "0"
                            : BackdatingInt))), small_bold));
            cell_Backdate2.setPadding(5);
            cell_Backdate2.setHorizontalAlignment(Element.ALIGN_CENTER);

            PdfPCell cell_AccidetnalAndParmantRider_YearlyPremium1 = new PdfPCell(
                    new Paragraph(premium_paying_frequency
                            + "  Installment Base Premium with Applicable Taxes",
                            small_normal));
            PdfPCell cell_AccidetnalAndParmantRider_YearlyPremium2 = new PdfPCell(
                    new Paragraph("Rs  "
                            + obj.getRound(obj.getStringWithout_E(Double
                            .valueOf(premWthST.equals("") ? "0"
                                    : premWthST))), small_bold));
            cell_AccidetnalAndParmantRider_YearlyPremium1.setPadding(5);
            cell_AccidetnalAndParmantRider_YearlyPremium2.setPadding(5);
            cell_AccidetnalAndParmantRider_YearlyPremium2
                    .setHorizontalAlignment(Element.ALIGN_CENTER);

            Table_backdating_premium_with_service_tax.addCell(cell_Backdate1);
            Table_backdating_premium_with_service_tax.addCell(cell_Backdate2);

            Table_backdating_premium_with_service_tax
                    .addCell(cell_AccidetnalAndParmantRider_YearlyPremium1);
            Table_backdating_premium_with_service_tax
                    .addCell(cell_AccidetnalAndParmantRider_YearlyPremium2);
            //document.add(Table_backdating_premium_with_service_tax);

            document.add(para_img_logo_after_space_1);

            // PdfPTable FY_SY_premDetail_table = new PdfPTable(7);
            // FY_SY_premDetail_table.setWidths(new float[] { 5f, 5f, 5f, 5f,
            // 5f,
            // 5f, 5f });
            PdfPTable FY_SY_premDetail_table = new PdfPTable(4);
            FY_SY_premDetail_table.setWidths(new float[]{5f, 5f, 5f, 5f});
            FY_SY_premDetail_table.setWidthPercentage(100f);
            FY_SY_premDetail_table.setHorizontalAlignment(Element.ALIGN_LEFT);

            PdfPCell cell;

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

			/*cell = new PdfPCell(new Phrase(smartIncomeProtectBean.getPremFreq()
					+ " Premium  (Rs )", small_normal));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			FY_SY_premDetail_table.addCell(cell);*/

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

            cell = new PdfPCell(new Phrase("Base Plan (Rs.)", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            FY_SY_premDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase("Riders (Rs.)", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            FY_SY_premDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase("Total Installment Premium (Rs.)", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            FY_SY_premDetail_table.addCell(cell);

			/*cell = new PdfPCell(new Phrase(smartIncomeProtectBean.getPremFreq()
					+ " Premium with Applicable Taxes (Rs )", small_normal));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			FY_SY_premDetail_table.addCell(cell);*/

            // 3 row
            cell = new PdfPCell(new Phrase("Installment Premium without Applicable Taxes (Rs.)", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            FY_SY_premDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                    .parseDouble(prsObj.parseXmlTag(output, "yearlyPrem"))),
                    small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            FY_SY_premDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                    .parseDouble(prsObj.parseXmlTag(output, "InstRider"))),
                    small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            FY_SY_premDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                    .parseDouble(prsObj.parseXmlTag(output, "premInst"))),
                    small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            FY_SY_premDetail_table.addCell(cell);

            // 4 row
            if (!smartIncomeProtectBean.getPremFreq()
                    .equalsIgnoreCase("Single")) {
                cell = new PdfPCell(new Phrase("Installment Premium with Applicable Taxes : 1st Year (Rs.)",
                        small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                FY_SY_premDetail_table.addCell(cell);

                cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                        .parseDouble(prsObj.parseXmlTag(output, "premInstBasicFirstYear"))),
                        small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                FY_SY_premDetail_table.addCell(cell);

                cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                        .parseDouble(prsObj.parseXmlTag(output,
                                "InstWithSTFirstYearRider"))), small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                FY_SY_premDetail_table.addCell(cell);

                cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                        .parseDouble(prsObj.parseXmlTag(output,
                                "totalInstPremFirstYear"))), small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                FY_SY_premDetail_table.addCell(cell);
            }
            cell = new PdfPCell(new Phrase("Installment Premium with Applicable Taxes : 2nd Year onwards (Rs.)", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            FY_SY_premDetail_table.addCell(cell);


            cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                    .parseDouble(prsObj.parseXmlTag(output,
                            "premWthSTSecondYear"))), small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            FY_SY_premDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                    .parseDouble(prsObj.parseXmlTag(output,
                            "InstWithSTSecondYearRider"))), small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            FY_SY_premDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                    .parseDouble(prsObj.parseXmlTag(output,
                            "totalInstPremSecondYear"))), small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            FY_SY_premDetail_table.addCell(cell);

            document.add(FY_SY_premDetail_table);
            // PdfPTable
            // Table_AccidetnalAndParmantRider_BasicPremium_servicePremium_yearlyPremium3
            // = new PdfPTable(
            // 4);
            // Table_AccidetnalAndParmantRider_BasicPremium_servicePremium_yearlyPremium3
            // .setWidthPercentage(100);
            // int base_premium = Integer
            // .parseInt(obj.getRound(obj.getStringWithout_E(Double
            // .valueOf(basicprem.equals("") ? "0" : basicprem))));
            //
            // PdfPCell cell_AccidetnalAndParmantRider_BasicPremium_Second_year1
            // = new PdfPCell(
            // new Paragraph("Second year onwards", small_normal));
            // cell_AccidetnalAndParmantRider_BasicPremium_Second_year1
            // .setPadding(5);
            // PdfPCell cell_AccidetnalAndParmantRider_BasicPremium_Second_year2
            // = new PdfPCell(
            // new Paragraph("Rs  "
            // + obj.getRound(obj.getStringWithout_E(Double
            // .valueOf(basicprem.equals("") ? "0"
            // : basicprem))), small_bold));
            // cell_AccidetnalAndParmantRider_BasicPremium_Second_year2
            // .setPadding(5);
            // cell_AccidetnalAndParmantRider_BasicPremium_Second_year2
            // .setHorizontalAlignment(Element.ALIGN_CENTER);
            //
            // double Second_year_serviceTax = Double
            // .parseDouble(obj.getRound(obj.getStringWithout_E(Double
            // String temp_Second_year_serviceTax = commonForAllProd
            // .getRoundUp(Second_year_serviceTax / 2);
            // PdfPCell cell_AccidetnalAndParmantRider_BasicPremium_Second_year3
            // = new PdfPCell(
            // new Paragraph("Rs  "
            // + Integer.parseInt(temp_Second_year_serviceTax),
            // small_bold));
            //
            // int temp_Second_year_yearly_premium_with_serviceTax =
            // base_premium
            // + Integer.parseInt(temp_Second_year_serviceTax);
            //
            // PdfPCell cell_AccidetnalAndParmantRider_BasicPremium_Second_year4
            // = new PdfPCell(
            // new Paragraph("Rs  "
            // + temp_Second_year_yearly_premium_with_serviceTax,
            // small_bold));
            //
            // cell_AccidetnalAndParmantRider_BasicPremium_Second_year3
            // .setPadding(5);
            // cell_AccidetnalAndParmantRider_BasicPremium_Second_year4
            // .setPadding(5);
            // cell_AccidetnalAndParmantRider_BasicPremium_Second_year4
            // .setHorizontalAlignment(Element.ALIGN_CENTER);
            //
            // Table_AccidetnalAndParmantRider_BasicPremium_servicePremium_yearlyPremium3
            // .addCell(cell_AccidetnalAndParmantRider_BasicPremium_Second_year1);
            // Table_AccidetnalAndParmantRider_BasicPremium_servicePremium_yearlyPremium3
            // .addCell(cell_AccidetnalAndParmantRider_BasicPremium_Second_year2);
            //
            // Table_AccidetnalAndParmantRider_BasicPremium_servicePremium_yearlyPremium3
            // .addCell(cell_AccidetnalAndParmantRider_BasicPremium_Second_year3);
            // Table_AccidetnalAndParmantRider_BasicPremium_servicePremium_yearlyPremium3
            // .addCell(cell_AccidetnalAndParmantRider_BasicPremium_Second_year4);
            //
            // document.add(Table_AccidetnalAndParmantRider_BasicPremium_servicePremium_yearlyPremium3);
            document.add(para_img_logo_after_space_1);

            PdfPTable BI_Pdftable_note = new PdfPTable(1);
            BI_Pdftable_note.setWidthPercentage(100);
            PdfPCell BI_Pdftable_note_cell1 = new PdfPCell(new Paragraph(
                    "Please Note", small_bold));
            BI_Pdftable_note_cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_Pdftable_note_cell1.setPadding(5);

            BI_Pdftable_note.addCell(BI_Pdftable_note_cell1);
            document.add(BI_Pdftable_note);

            PdfPTable BI_Pdftable6 = new PdfPTable(1);
            BI_Pdftable6.setWidthPercentage(100);
            PdfPCell BI_Pdftable6_cell6 = new PdfPCell(new Paragraph(
                    "1. The premiums can also be paid by giving standing instruction to your bank or you can pay through your credit card."

                    , small_normal));

            BI_Pdftable6_cell6.setPadding(5);

            BI_Pdftable6.addCell(BI_Pdftable6_cell6);
            document.add(BI_Pdftable6);

            PdfPTable BI_Pdftable7 = new PdfPTable(1);
            BI_Pdftable7.setWidthPercentage(100);
            PdfPCell BI_Pdftable7_cell1 = new PdfPCell(
                    new Paragraph(
                            "2. Applicable Taxes (including surcharge/cess etc), at the rate notified by the Central Government/ State Government / Union Territories of India from time to time and as per the provisions of the prevalent tax laws will be payable on premium as per the product features. ",
                            small_normal));

            BI_Pdftable7_cell1.setPadding(5);

            BI_Pdftable7.addCell(BI_Pdftable7_cell1);
            document.add(BI_Pdftable7);

            PdfPTable BI_Pdftable8 = new PdfPTable(1);
            BI_Pdftable8.setWidthPercentage(100);
            PdfPCell BI_Pdftable8_cell1 = new PdfPCell(
                    new Paragraph(
                            "3. Tax deduction under Section 80D is available for premium paid towards SBI Life-Criti Care 13 Non Linked Rider. However in case the premium paid during the financial year, exceeds 10% of the sum assured, the benefit will be limited up to 10% of the sum assured. Tax deduction under Section 80D is available for premium paid towards SBI Life- Criti Care 13 Non Linked rider. Tax exemption under Section 10(10D) is available at the time of maturity/surrender, subject to the premium not exceeding 10% of the sum assured in any of the years during the term of the policy. However, death proceeds are completely exempt.Tax benefits, are as per the provisions of the Income Tax laws & are subject to change from time to time.Please consult your tax advisor for details.",
                            small_normal));

            BI_Pdftable8_cell1.setPadding(5);

            BI_Pdftable8.addCell(BI_Pdftable8_cell1);
            //document.add(BI_Pdftable8);

            PdfPTable BI_Pdftable9 = new PdfPTable(1);
            BI_Pdftable9.setWidthPercentage(100);
            PdfPCell BI_Pdftable9_cell9 = new PdfPCell(
                    new Paragraph(
                            "4. For Monthly Salary Saving Scheme (SSS), 2 month premium to be paid in advance and renewal premium payment is allowed only through Salary Deduction",
                            small_normal));

            BI_Pdftable9_cell9.setPadding(5);

            BI_Pdftable9.addCell(BI_Pdftable9_cell9);
            //document.add(BI_Pdftable9);


            PdfPTable taxes_desc = new PdfPTable(1);
            taxes_desc.setWidthPercentage(100);
            PdfPCell taxes_desc_cell = new PdfPCell(
                    new Paragraph(
                            "5) Applicable Taxes (including surcharge/cess etc), at the rate notified by the Central Government/ State Government / Union Territories of India from time to time and as per the provisions of the prevalent tax laws will be payable on premium/ or any other charges as per the product features. ",

                            small_normal));

            taxes_desc_cell.setPadding(5);

            taxes_desc.addCell(taxes_desc_cell);
            //document.add(taxes_desc);

            document.add(para_img_logo_after_space_1);

            PdfPTable BI_Pdftable_OtherTermCondition = new PdfPTable(1);
            BI_Pdftable_OtherTermCondition.setWidthPercentage(100);
            PdfPCell BI_Pdftable_OtherTermCondition_cell1 = new PdfPCell(
                    new Paragraph("Other Terms and Conditions", small_bold));
            BI_Pdftable_OtherTermCondition_cell1
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_Pdftable_OtherTermCondition_cell1.setPadding(5);

            BI_Pdftable_OtherTermCondition
                    .addCell(BI_Pdftable_OtherTermCondition_cell1);
            //document.add(BI_Pdftable_OtherTermCondition);

            PdfPTable BI_PdftableOtherTermCondition1 = new PdfPTable(1);
            BI_PdftableOtherTermCondition1.setWidthPercentage(100);
            PdfPCell BI_PdftableOtherTermCondition1_cell6 = new PdfPCell(
                    new Paragraph(
                            "1. The benefit calculation is based on the age herein indicated and as applicable for healthy individual."

                            , small_normal));

            BI_PdftableOtherTermCondition1_cell6.setPadding(5);

            BI_PdftableOtherTermCondition1
                    .addCell(BI_PdftableOtherTermCondition1_cell6);
            //document.add(BI_PdftableOtherTermCondition1);

            PdfPTable BI_PdftableOtherTermCondition2 = new PdfPTable(1);
            BI_PdftableOtherTermCondition2.setWidthPercentage(100);
            PdfPCell BI_PdftableOtherTermCondition2_cell1 = new PdfPCell(
                    new Paragraph(
                            "2.The Survival/ Death Benefit amount are derived on the assumption that the policies are in full force.",
                            small_normal));

            BI_PdftableOtherTermCondition2_cell1.setPadding(5);

            BI_PdftableOtherTermCondition2
                    .addCell(BI_PdftableOtherTermCondition2_cell1);
            //document.add(BI_PdftableOtherTermCondition2);

            // PdfPTable BI_PdftableOtherTermCondition3 = new PdfPTable(1);
            // BI_PdftableOtherTermCondition3.setWidthPercentage(100);
            // PdfPCell BI_PdftableOtherTermCondition3_cell1 = new PdfPCell(
            // new Paragraph(
            // "3. Insurance is subject matter of solicitation.",
            // small_normal));
            //
            // BI_PdftableOtherTermCondition3_cell1.setPadding(5);
            //
            // BI_PdftableOtherTermCondition3
            // .addCell(BI_PdftableOtherTermCondition3_cell1);
            // document.add(BI_PdftableOtherTermCondition3);

            PdfPTable BI_PdftableOtherTermCondition4 = new PdfPTable(1);
            BI_PdftableOtherTermCondition4.setWidthPercentage(100);
            PdfPCell BI_PdftableOtherTermCondition4_cell1 = new PdfPCell(
                    new Paragraph(
                            "3. If riders are applicable, please refer to specific rider benefit.",
                            small_normal));

            BI_PdftableOtherTermCondition4_cell1.setPadding(5);

            BI_PdftableOtherTermCondition4
                    .addCell(BI_PdftableOtherTermCondition4_cell1);
            //document.add(BI_PdftableOtherTermCondition4);

            PdfPTable BI_PdftableOtherTermCondition5 = new PdfPTable(1);
            BI_PdftableOtherTermCondition5.setWidthPercentage(100);
            PdfPCell BI_PdftableOtherTermCondition5_cell1 = new PdfPCell(
                    new Paragraph(
                            "4. I hereby declare that the product features and the benefits have been fully and thoroughly explained to me along with benefit illustrations and I have fully understood the same.",
                            small_normal));

            BI_PdftableOtherTermCondition5_cell1.setPadding(5);

            BI_PdftableOtherTermCondition5
                    .addCell(BI_PdftableOtherTermCondition5_cell1);
            //document.add(BI_PdftableOtherTermCondition5);

            //document.add(para_img_logo_after_space_1);


            PdfPTable table1_new = new PdfPTable(17);
            table1_new.setWidths(new float[]{2f, 5f, 5f, 5f, 5f, 5f, 5f, 5f, 5f,
                    5f, 5f, 5f, 5f, 5f, 5f, 5f, 5f});
            table1_new.setWidthPercentage(100);

            // 1st row
            cell = new PdfPCell(
                    new Phrase(
                            "Benefit Illustration for SBI Life - Smart Income Protect  (Amount in Rs.)",
                            normal1_bold));
            cell.setColspan(17);
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table1_new.addCell(cell);

            // 2nd row
            cell = new PdfPCell(new Phrase("Policy Year", normal_bold));
            cell.setRowspan(3);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1_new.addCell(cell);
            if (smartIncomeProtectBean.getPremFreq().equalsIgnoreCase("Single")) {
                cell = new PdfPCell(new Phrase(
                        "Single premium", normal_bold));
            } else {
                cell = new PdfPCell(new Phrase(
                        "Annualized premium", normal_bold));
            }


            cell.setRowspan(3);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1_new.addCell(cell);

            cell = new PdfPCell(new Phrase("Guaranteed Benefits",
                    normal_bold));
            cell.setColspan(5);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1_new.addCell(cell);

            cell = new PdfPCell(
                    new Phrase("Non-Guaranteed Benefits @ 4% p.a.", normal_bold));
            cell.setColspan(3);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1_new.addCell(cell);

            cell = new PdfPCell(
                    new Phrase("Non-Guaranteed Benefits @ 8% p.a.", normal_bold));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setColspan(3);
            table1_new.addCell(cell);

            // 3rd
            cell = new PdfPCell(new Phrase("Total benefits Including Guaranteed and Non-Guaranteed benefits", normal_bold));
            cell.setColspan(4);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1_new.addCell(cell);

            cell = new PdfPCell(new Phrase(
                    "Guaranteed additions",
                    normal_bold));
            cell.setRowspan(2);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1_new.addCell(cell);

            cell = new PdfPCell(new Phrase("Survival benefit", normal_bold));
            cell.setRowspan(2);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1_new.addCell(cell);

            cell = new PdfPCell(new Phrase(
                    "Surrender Benefit",
                    normal_bold));
            cell.setRowspan(2);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1_new.addCell(cell);

            cell = new PdfPCell(new Phrase("Death Benefit", normal_bold));
            cell.setRowspan(2);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1_new.addCell(cell);

            cell = new PdfPCell(new Phrase("Maturity  Benefit", normal_bold));
            cell.setRowspan(2);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1_new.addCell(cell);

            // 4 row
            cell = new PdfPCell(new Phrase("Reversionary bonus", normal_bold));
            cell.setRowspan(2);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1_new.addCell(cell);

            cell = new PdfPCell(new Phrase("Cash bonus", normal_bold));
            cell.setRowspan(2);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1_new.addCell(cell);

            cell = new PdfPCell(new Phrase("Surrender benefit", normal_bold));
            cell.setRowspan(2);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1_new.addCell(cell);

            cell = new PdfPCell(new Phrase("Reversionary bonus", normal_bold));
            cell.setRowspan(2);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1_new.addCell(cell);

            cell = new PdfPCell(new Phrase("Cash bonus", normal_bold));
            cell.setRowspan(2);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1_new.addCell(cell);

            cell = new PdfPCell(new Phrase("Surrender benefit", normal_bold));
            cell.setRowspan(2);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1_new.addCell(cell);

            cell = new PdfPCell(new Phrase("Maturity Benefit", normal_bold));
            cell.setColspan(2);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1_new.addCell(cell);

            cell = new PdfPCell(new Phrase("Death Benefit", normal_bold));
            cell.setColspan(2);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1_new.addCell(cell);

            cell = new PdfPCell(new Phrase("Total Maturity benefit, incl. Terminal bonus, if any, @ 4% (7+8+9)", normal_bold));
            cell.setRowspan(1);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1_new.addCell(cell);
            cell = new PdfPCell(new Phrase("Total Maturity benefit, incl. Terminal bonus, if any, @ 8% (7+11+12)", normal_bold));
            cell.setRowspan(1);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1_new.addCell(cell);
            cell = new PdfPCell(new Phrase("Total Death benefit, incl. Terminal bonus, if any, @ 4% (6+8+9)", normal_bold));
            cell.setRowspan(1);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1_new.addCell(cell);

            cell = new PdfPCell(new Phrase("Total Death benefit, incl. Terminal bonus, if any, @ 8% (6+11+12)", normal_bold));
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


            for (int i = 0; i < list_data.size(); i++) {

                cell = new PdfPCell(new Phrase(list_data.get(i)
                        .getEnd_of_year(), normal));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                        | Rectangle.BOTTOM);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table1_new.addCell(cell);

                cell = new PdfPCell(new Phrase(
                        currencyFormat.format(Double.parseDouble(list_data.get(
                                i).getTotal_base_premium())), normal));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                        | Rectangle.BOTTOM);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table1_new.addCell(cell);


                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                        .parseDouble(list_data.get(i)
                                .getGuaranteed_additions())), normal));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                        | Rectangle.BOTTOM);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table1_new.addCell(cell);


                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                        .parseDouble(list_data.get(i)
                                .getGuaranteed_survival_benefit())), normal));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                        | Rectangle.BOTTOM);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table1_new.addCell(cell);


                cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                        .parseDouble(list_data.get(i)
                                .getGuaranteed_surrender_value())), normal));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                        | Rectangle.BOTTOM);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table1_new.addCell(cell);


                cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                        .parseDouble(list_data.get(i)
                                .getGuaranteed_death_benefit())), normal));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                        | Rectangle.BOTTOM);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table1_new.addCell(cell);


                cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                        .parseDouble(list_data.get(i)
                                .getGuaranteed_maturity_benefit())), normal));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                        | Rectangle.BOTTOM);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table1_new.addCell(cell);


                cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                        .parseDouble(list_data.get(i)
                                .getNot_guaranteed_maturity_benefit_4per())),
                        normal));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                        | Rectangle.BOTTOM);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table1_new.addCell(cell);


                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell = new PdfPCell(new Phrase("0", normal));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                        | Rectangle.BOTTOM);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table1_new.addCell(cell);


                cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                        .parseDouble(list_data.get(i)
                                .getNot_guaranteed_surrender_value_4per())),
                        normal));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                        | Rectangle.BOTTOM);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table1_new.addCell(cell);


                cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                        .parseDouble(list_data.get(i)
                                .getNot_guaranteed_maturity_benefit_8per())),
                        normal));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                        | Rectangle.BOTTOM);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table1_new.addCell(cell);


                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell = new PdfPCell(new Phrase("0", normal));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                        | Rectangle.BOTTOM);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table1_new.addCell(cell);


                cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                        .parseDouble(list_data.get(i)
                                .getNot_guaranteed_surrender_value_8per())),
                        normal));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                        | Rectangle.BOTTOM);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table1_new.addCell(cell);


                cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                        .parseDouble(list_data.get(i)
                                .getNot_guaranteed_survival_benefit_4per())),
                        normal));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                        | Rectangle.BOTTOM);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table1_new.addCell(cell);


                cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                        .parseDouble(list_data.get(i)
                                .getNot_guaranteed_survival_benefit_8per())),
                        normal));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                        | Rectangle.BOTTOM);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table1_new.addCell(cell);


                cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                        .parseDouble(list_data.get(i)
                                .getNot_guaranteed_death_benefit_4per())),
                        normal));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                        | Rectangle.BOTTOM);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table1_new.addCell(cell);
                cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                        .parseDouble(list_data.get(i)
                                .getNot_guaranteed_death_benefit_8per())),
                        normal));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                        | Rectangle.BOTTOM);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table1_new.addCell(cell);


            }
            document.add(table1_new);


            Paragraph para_note = new Paragraph(
                    "Benefit Payable to the nominee on death Higher of i) Sum Assured on death# + Vested Simple Reversionary Bonuses + Terminal bonus, if any. OR ii) 105% of all the basic premiums paid.",
                    small_normal);
            //document.add(para_note);
            //document.add(para_img_logo_after_space_1);

            Paragraph para_EndowmentOption = new Paragraph(
                    "#For details on Sum Assured on death please refer the Sales Brochure",
                    small_normal);
            //document.add(para_EndowmentOption);

            //document.add(para_img_logo_after_space_1);

            PdfPTable BI_Pdftable_BonusRates = new PdfPTable(1);
            BI_Pdftable_BonusRates.setWidthPercentage(100);
            PdfPCell BI_Pdftable_BonusRates_cell1 = new PdfPCell(new Paragraph(
                    "Notes :", small_bold));
            BI_Pdftable_BonusRates_cell1
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_Pdftable_BonusRates_cell1.setPadding(5);

            BI_Pdftable_BonusRates.addCell(BI_Pdftable_BonusRates_cell1);
            document.add(BI_Pdftable_BonusRates);

            PdfPTable BI_Pdftable_BonusRates1 = new PdfPTable(1);
            BI_Pdftable_BonusRates1.setWidthPercentage(100);
            PdfPCell BI_Pdftable_BonusRates_cell2 = new PdfPCell(
                    new Paragraph(
                            "1. Annualized premium shall be the premium amount payable in a year chosen by the policyholder, excluding the taxes,  rider premiums, underwriting extra premiums and loading for modal premiums, if any. Refer sales literature for explanation of terms used in this illustration."
                            , small_normal));

            BI_Pdftable_BonusRates_cell2.setPadding(5);

            BI_Pdftable_BonusRates1.addCell(BI_Pdftable_BonusRates_cell2);
            document.add(BI_Pdftable_BonusRates1);

            PdfPTable BI_Pdftable_BonusRates2 = new PdfPTable(1);
            BI_Pdftable_BonusRates2.setWidthPercentage(100);
            PdfPCell BI_Pdftable_BonusRates2_cell2 = new PdfPCell(
                    new Paragraph(
                            "2. All Benefit amount are derived on the assumption that the policies are ''in-force''"

                            , small_normal));

            BI_Pdftable_BonusRates2_cell2.setPadding(5);

            BI_Pdftable_BonusRates2.addCell(BI_Pdftable_BonusRates2_cell2);
            document.add(BI_Pdftable_BonusRates2);

            PdfPTable BI_Pdftable_BonusRates3 = new PdfPTable(1);
            BI_Pdftable_BonusRates3.setWidthPercentage(100);
            PdfPCell BI_Pdftable_BonusRates3_cell2 = new PdfPCell(
                    new Paragraph(
                            "3. The above BI is subject to payment of stipulated premiums on due date."

                            , small_normal));

            BI_Pdftable_BonusRates3_cell2.setPadding(5);

            BI_Pdftable_BonusRates3.addCell(BI_Pdftable_BonusRates3_cell2);
            document.add(BI_Pdftable_BonusRates3);

            PdfPTable BI_Pdftable_BonusRates4 = new PdfPTable(1);
            BI_Pdftable_BonusRates4.setWidthPercentage(100);
            PdfPCell BI_Pdftable_BonusRates4_cell2 = new PdfPCell(
                    new Paragraph(
                            "4. If riders are applicable, please refer to specific rider benefit.For more details,Please refer to the concern rider document."

                            , small_normal));

            BI_Pdftable_BonusRates4_cell2.setPadding(5);

            BI_Pdftable_BonusRates4.addCell(BI_Pdftable_BonusRates4_cell2);
            document.add(BI_Pdftable_BonusRates4);

            PdfPTable BI_Pdftable_BonusRates5 = new PdfPTable(1);
            BI_Pdftable_BonusRates5.setWidthPercentage(100);
            PdfPCell BI_Pdftable_BonusRates5_cell2 = new PdfPCell(
                    new Paragraph(
                            "5. In addition to Guaranteed Surrender Benefits (column 5), Surrender value of the vested bonuses will also be paid. For the purpose of guaranteed surrender value (GSV) in this illustration the surrender value of vested bonuses are not considered at all."

                            , small_normal));

            BI_Pdftable_BonusRates5_cell2.setPadding(5);

            BI_Pdftable_BonusRates5.addCell(BI_Pdftable_BonusRates5_cell2);
            document.add(BI_Pdftable_BonusRates5);

            PdfPTable BI_Pdftable_BonusRates6 = new PdfPTable(1);
            BI_Pdftable_BonusRates6.setWidthPercentage(100);
            PdfPCell BI_Pdftable_BonusRates6_cell2 = new PdfPCell(
                    new Paragraph(
                            "6.There is an option to received maturity benefits in lump sum which would be 110% of the basic sum assured plus vested reversionary bonuses plus terminal bonus, if any.This option is to be exercise while filling the proposal from."

                            , small_normal));

            BI_Pdftable_BonusRates6_cell2.setPadding(5);

            BI_Pdftable_BonusRates6.addCell(BI_Pdftable_BonusRates6_cell2);
            //document.add(BI_Pdftable_BonusRates6);

            document.add(para_img_logo_after_space_1);

            PdfPTable BI_Pdftable_SurrenderValue = new PdfPTable(1);
            BI_Pdftable_SurrenderValue.setWidthPercentage(100);
            PdfPCell BI_Pdftable_SurrenderValue_cell1 = new PdfPCell(
                    new Paragraph("Bonus Rates", small_bold));
            BI_Pdftable_SurrenderValue_cell1
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_Pdftable_SurrenderValue_cell1.setPadding(5);

            BI_Pdftable_SurrenderValue
                    .addCell(BI_Pdftable_SurrenderValue_cell1);
            document.add(BI_Pdftable_SurrenderValue);

            PdfPTable BI_Pdftable_SurrenderValue1 = new PdfPTable(1);
            BI_Pdftable_SurrenderValue1.setWidthPercentage(100);
            PdfPCell BI_Pdftable_SurrenderValue1_cell = new PdfPCell(
                    new Paragraph(
                            "This is a with profit plan and participates in the profits of the company’s life insurance business. Simple reversionary bonuses are declared as a percentage rate, which apply to the sum assured of the basic policy. ",
                            small_normal));

            PdfPCell BI_Pdftable_SurrenderValue1_cell2 = new PdfPCell(
                    new Paragraph(
                            "Terminal bonuses, if any, are declared as a percentage rate, which apply to the vested reversionary bonus. The bonus rates in the benefit illustration are constant. However, in practice, future bonuses are not guaranteed and will depend on future profits. Therefore, the bonuses are shown as non-guaranteed benefits and are calculated so that they are consistent with the two projected investment return assumptions of 4% and 8% per annum.",
                            small_normal));

            BI_Pdftable_SurrenderValue1_cell.setPadding(5);

            BI_Pdftable_SurrenderValue1_cell
                    .setHorizontalAlignment(Element.ALIGN_LEFT);
            BI_Pdftable_SurrenderValue1
                    .addCell(BI_Pdftable_SurrenderValue1_cell);
            BI_Pdftable_SurrenderValue1
                    .addCell(BI_Pdftable_SurrenderValue1_cell2);
            document.add(BI_Pdftable_SurrenderValue1);

            document.add(para_img_logo_after_space_1);

            PdfPTable BI_Pdftable_GuaranteedSurrenderValue = new PdfPTable(1);
            BI_Pdftable_GuaranteedSurrenderValue.setWidthPercentage(100);
            PdfPCell BI_Pdftable_GuaranteedSurrenderValue_cell1 = new PdfPCell(
                    new Paragraph("Guaranteed Surrender Value", small_bold));
            BI_Pdftable_GuaranteedSurrenderValue_cell1
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_Pdftable_GuaranteedSurrenderValue_cell1.setPadding(5);

            BI_Pdftable_GuaranteedSurrenderValue
                    .addCell(BI_Pdftable_GuaranteedSurrenderValue_cell1);
            //document.add(BI_Pdftable_GuaranteedSurrenderValue);

            PdfPTable BI_Pdftable_RegularPremiumPolicies = new PdfPTable(1);
            BI_Pdftable_RegularPremiumPolicies.setWidthPercentage(100);
            PdfPCell BI_Pdftable_RegularPremiumPolicies_cell = new PdfPCell(
                    new Paragraph(
                            "1)The policy will acquire a paid-up and/or surrender value only if premiums have been paid for at least 2 full years for policy term 7 years and at least 3 full years for policy term 12 years & 15 years. ",
                            small_normal));

            BI_Pdftable_RegularPremiumPolicies_cell.setPadding(5);

            BI_Pdftable_RegularPremiumPolicies
                    .addCell(BI_Pdftable_RegularPremiumPolicies_cell);
            //document.add(BI_Pdftable_RegularPremiumPolicies);

            PdfPTable BI_Pdftable_SinglePremiumPolicies = new PdfPTable(1);
            BI_Pdftable_SinglePremiumPolicies.setWidthPercentage(100);
            PdfPCell BI_Pdftable_SinglePremiumPolicies_cell = new PdfPCell(
                    new Paragraph(
                            "2)The Guaranteed Surrender Value (GSV) in case of regular premium policies will be equal to GSV factors multiplied by the basic premiums paid. Basic premium is equal to total premium less Applicable Taxes, underwriting extra premiums, extra premium due to modal factors and rider premiums, if any. ",
                            small_normal));

            BI_Pdftable_SinglePremiumPolicies_cell.setPadding(5);

            BI_Pdftable_SinglePremiumPolicies
                    .addCell(BI_Pdftable_SinglePremiumPolicies_cell);
            //document.add(BI_Pdftable_SinglePremiumPolicies);

            //document.add(para_img_logo_after_space_1);

            PdfPTable BI_Pdftable_CompanysPolicySurrender = new PdfPTable(1);
            BI_Pdftable_CompanysPolicySurrender.setWidthPercentage(100);
            PdfPCell BI_Pdftable_CompanysPolicySurrender_cell1 = new PdfPCell(
                    new Paragraph("Important :", small_bold));
            BI_Pdftable_CompanysPolicySurrender_cell1
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_Pdftable_CompanysPolicySurrender_cell1.setPadding(5);

            BI_Pdftable_CompanysPolicySurrender
                    .addCell(BI_Pdftable_CompanysPolicySurrender_cell1);
            document.add(BI_Pdftable_CompanysPolicySurrender);

            PdfPTable BI_Pdftable_CompanysPolicySurrender1 = new PdfPTable(1);
            BI_Pdftable_CompanysPolicySurrender1.setWidthPercentage(100);
            PdfPCell BI_Pdftable_CompanysPolicySurrender1_cell = new PdfPCell(
                    new Paragraph(
                            "In practice, the company may pay a surrender value which could be higher than the guaranteed surrender value.The benefits payable on surrender reflects the value of your policy, which is assessed based on the past financial/demographic experience of the company with regard to your policy/group of similar policies, as well as the likely future experience. The surrender value payable may be reviewed from time to time depending on company's experience of the various factors which impact the surrender values that may be paid. The surrender value would be higher of GSV or SSV.",
                            small_normal));

            BI_Pdftable_CompanysPolicySurrender1_cell.setPadding(5);

            BI_Pdftable_CompanysPolicySurrender1
                    .addCell(BI_Pdftable_CompanysPolicySurrender1_cell);
            //document.add(BI_Pdftable_CompanysPolicySurrender1);

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
            //document.add(BI_Pdftable_CompanysPolicySurrender4);

            PdfPTable BI_Pdftable_CompanysPolicySurrender5 = new PdfPTable(1);
            BI_Pdftable_CompanysPolicySurrender5.setWidthPercentage(100);
            PdfPCell BI_Pdftable_CompanysPolicySurrender5_cell = new PdfPCell(
                    new Paragraph(Company_policy_surrender_dec, small_normal));

            BI_Pdftable_CompanysPolicySurrender5_cell.setPadding(5);

            BI_Pdftable_CompanysPolicySurrender5
                    .addCell(BI_Pdftable_CompanysPolicySurrender5_cell);
            //document.add(BI_Pdftable_CompanysPolicySurrender5);

            document.add(para_img_logo_after_space_1);

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
                                    + "    , having received the information with respect to the above, have understood the above statement before entering into the contract.",
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

                PdfPCell str_agent = new PdfPCell(
                        new Paragraph(
                                "I, "
                                        + commonMethods.getUserName(context)
                                        + "  , have explained the premiums and benefits under the product fully to the prospect/policyholder.",
                                small_bold));

                str_agent.setPadding(5);

                BI_PdftableMarketing.addCell(BI_PdftableMarketing_signature_cell);
                BI_PdftableMarketing.addCell(str_agent);

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
            Log.i(getLocalClassName(), e.toString() + "Error in creating pdf");
        }
    }

    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        // TODO Auto-generated method stub
        if (v.getId() == edt_bi_smart_income_protect_life_assured_first_name
                .getId()) {
            setFocusable(edt_bi_smart_income_protect_life_assured_middle_name);
            edt_bi_smart_income_protect_life_assured_middle_name.requestFocus();
        } else if (v.getId() == edt_bi_smart_income_protect_life_assured_middle_name
                .getId()) {
            setFocusable(edt_bi_smart_income_protect_life_assured_last_name);
            edt_bi_smart_income_protect_life_assured_last_name.requestFocus();
        } else if (v.getId() == edt_bi_smart_income_protect_life_assured_last_name
                .getId()) {

            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(
                    edt_bi_smart_income_protect_life_assured_last_name
                            .getWindowToken(), 0);
            // clearFocusable(edt_bi_smart_income_protect_life_assured_last_name);
            setFocusable(btn_bi_smart_income_protect_life_assured_date);
            btn_bi_smart_income_protect_life_assured_date.requestFocus();
        } else if (v.getId() == edt_bi_smart_income_protect_proposer_first_name
                .getId()) {
            setFocusable(edt_bi_smart_income_protect_proposer_middle_name);
            edt_bi_smart_income_protect_proposer_middle_name.requestFocus();
        } else if (v.getId() == edt_bi_smart_income_protect_proposer_middle_name
                .getId()) {
            setFocusable(edt_bi_smart_income_protect_proposer_last_name);
            edt_bi_smart_income_protect_proposer_last_name.requestFocus();
        } else if (v.getId() == edt_bi_smart_income_protect_proposer_last_name
                .getId()) {

            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(
                    edt_bi_smart_income_protect_proposer_last_name
                            .getWindowToken(), 0);
            // clearFocusable(edt_bi_smart_income_protect_proposer_last_name);
            setFocusable(btn_bi_smart_income_protect_proposer_date);
            btn_bi_smart_income_protect_proposer_date.requestFocus();
        } else if (v.getId() == edt_smart_income_protect_contact_no.getId()) {
            // clearFocusable(premiumAmt);
            setFocusable(edt_smart_income_protect_Email_id);
            edt_smart_income_protect_Email_id.requestFocus();
        } else if (v.getId() == edt_smart_income_protect_Email_id.getId()) {
            // clearFocusable(premiumAmt);
            setFocusable(edt_smart_income_protect_ConfirmEmail_id);
            edt_smart_income_protect_ConfirmEmail_id.requestFocus();
        } else if (v.getId() == edt_smart_income_protect_ConfirmEmail_id
                .getId()) {
            // clearFocusable(premiumAmt);
            setFocusable(spnr_bi_smart_income_protect_policyterm);
            spnr_bi_smart_income_protect_policyterm.requestFocus();
        }

        // else if (v.getId() ==
        // edt_bi_smart_income_protect_sum_assured_amount.getId()) {
        //
        // clearFocusable(cb_bi_smart_income_protect_pt_rider);
        // setFocusable(cb_bi_smart_income_protect_pt_rider);
        // cb_bi_smart_income_protect_pt_rider.requestFocus();
        // }
        //
        // else if (v.getId() ==
        // edt_bi_smart_income_protect_pt_rider_sum_assured.getId()) {
        // clearFocusable(cb_bi_smart_income_protect_pt_rider);
        // setFocusable(cb_bi_smart_income_protect_pt_rider);
        // cb_bi_smart_income_protect_pt_rider.requestFocus();
        // } else if (v.getId() ==
        // edt_bi_smart_income_protect_adb_rider_sum_assured.getId()) {
        // clearFocusable(cb_bi_smart_income_protect_adb_rider);
        // setFocusable(cb_bi_smart_income_protect_adb_rider);
        // cb_bi_smart_income_protect_adb_rider.requestFocus();
        // } else if (v.getId() ==
        // edt_bi_smart_income_protect_atpbd_rider_sum_assured.getId()) {
        // clearFocusable(cb_bi_smart_income_protect_atpdb_rider);
        // setFocusable(cb_bi_smart_income_protect_atpdb_rider);
        // cb_bi_smart_income_protect_atpdb_rider.requestFocus();
        // }else if (v.getId() ==
        // edt_bi_smart_income_protect_cc13nl_rider_sum_assured.getId()) {
        // clearFocusable(cb_bi_smart_income_protect_cc13nl_rider);
        // setFocusable(cb_bi_smart_income_protect_cc13nl_rider);
        // cb_bi_smart_income_protect_cc13nl_rider.requestFocus();
        // }else if (v.getId() ==
        // edt_bi_smart_income_protect_cc13nl_rider_sum_assured.getId()) {
        //
        // // InputMethodManager imm = (InputMethodManager)
        // getSystemService(INPUT_METHOD_SERVICE);
        // // imm.hideSoftInputFromWindow(
        // //
        // edt_bi_smart_income_protect_cc13nl_rider_sum_assured.getWindowToken(),
        // 0);
        // }
        //

        else if (v.getId() == edt_bi_smart_income_protect_sum_assured_amount
                .getId()) {
            commonMethods.hideKeyboard(edt_bi_smart_income_protect_sum_assured_amount, context);
            clearFocusable(cb_bi_smart_income_protect_pt_rider);
            setFocusable(cb_bi_smart_income_protect_pt_rider);
            cb_bi_smart_income_protect_pt_rider.requestFocus();

        } else if (v.getId() == edt_bi_smart_income_protect_pt_rider_sum_assured
                .getId()) {
            clearFocusable(cb_bi_smart_income_protect_adb_rider);
            setFocusable(cb_bi_smart_income_protect_adb_rider);
            cb_bi_smart_income_protect_adb_rider.requestFocus();
        } else if (v.getId() == edt_bi_smart_income_protect_adb_rider_sum_assured
                .getId()) {
            clearFocusable(cb_bi_smart_income_protect_atpdb_rider);
            setFocusable(cb_bi_smart_income_protect_atpdb_rider);
            cb_bi_smart_income_protect_atpdb_rider.requestFocus();
        } else if (v.getId() == edt_bi_smart_income_protect_atpbd_rider_sum_assured
                .getId()) {
            clearFocusable(cb_bi_smart_income_protect_cc13nl_rider);
            setFocusable(cb_bi_smart_income_protect_cc13nl_rider);
            cb_bi_smart_income_protect_cc13nl_rider.requestFocus();
        } else if (v.getId() == edt_bi_smart_income_protect_cc13nl_rider_sum_assured
                .getId()) {
            commonMethods.hideKeyboard(edt_bi_smart_income_protect_cc13nl_rider_sum_assured, context);
        }

        return true;
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

        boolean flagFocus = true;
        if (!flagFocus) {
            ll_bi_smart_income_protect_main.requestFocus();
        } else {
            spnr_bi_smart_income_protect_life_assured_title.requestFocus();
        }

    }


    private boolean valDoYouBackdate() {
        if (!proposer_Backdating_WishToBackDate_Policy.equals("")) {
            return true;
        } else {
            showAlert.setMessage("Please Select Do you wish to Backdate ");
            showAlert.setNeutralButton("OK",
                    new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            // apply focusable method
                            setFocusable(rb_smart_income_protect_backdating_yes);
                            rb_smart_income_protect_backdating_yes
                                    .requestFocus();
                        }
                    });
            showAlert.show();

            return false;

        }
    }

    private boolean valBackdate() {

        try {

            String error = "";

            if (rb_smart_income_protect_backdating_yes.isChecked()) {

                final Calendar c = Calendar.getInstance();
                final int currYear = c.get(Calendar.YEAR);
                final int currMonth = c.get(Calendar.MONTH) + 1;

                SimpleDateFormat dateformat1 = new SimpleDateFormat(
                        "dd-MM-yyyy");
                Date dtBackDate = dateformat1
                        .parse(btn_smart_income_protect_backdatingdate
                                .getText().toString());
                Date currentDate = c.getTime();

                Date launchDate = dateformat1.parse("15-01-2020");

                Date finYerEndDate;

                if (currMonth >= 4) {
                    finYerEndDate = dateformat1.parse("01-04-" + currYear);
                } else {
                    finYerEndDate = dateformat1
                            .parse("01-04-" + (currYear - 1));
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
            e.printStackTrace();
            return false;
        }

        return true;

    }


    private boolean valMaturityBenefit() {
        if (lumpSumAmount.equalsIgnoreCase("")) {
            commonMethods.dialogWarning(context, "Please select maturity benefit in lump sum", true);
            cbLumpSumpAmount.requestFocus();
            return false;
        } else {
            return true;
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

}
