package sbilife.com.pointofsale_bancaagency.flexismartplus;

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
import sbilife.com.pointofsale_bancaagency.common.CommonForAllProd;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;
import sbilife.com.pointofsale_bancaagency.common.ProductInfo;
import sbilife.com.pointofsale_bancaagency.common.SimpleCrypto;
import sbilife.com.pointofsale_bancaagency.common.StorageUtils;
import sbilife.com.pointofsale_bancaagency.common.Success;
import sbilife.com.pointofsale_bancaagency.needanalysis.NeedAnalysisActivity;
import sbilife.com.pointofsale_bancaagency.new_bussiness.ProductBIBean;
import sbilife.com.pointofsale_bancaagency.utility.GridHeight;

@SuppressWarnings({"deprecation", "ResultOfMethodCallIgnored"})
public class BI_FlexiSmartPlusActivity extends AppCompatActivity implements
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
    private int flag = 0;

    private String premiumPaymentMode;
    private String premiumAmount;

    /* For DashBoard */
    // UI elements
    private CheckBox cb_StaffDisc, selHoliday, selTopup, selJkResident;
    private TextView txtHolidayYear, txtHolidayTerm, txtTopupPremium;
    private EditText basicSA, SAMF, HolidayYear, HolidayTerm, TopupPremium;
    private Button btnSubmit, back;
    private Spinner ageInYears, selGender, selBasicTerm, selPremFreq,
            selOption;
    private String effectivePremium = "";
    private AlertDialog.Builder showAlert;
    private DecimalFormat currencyFormat;
    private FlexiSmartPlusBean flexiSmartBean;

    private String QuatationNumber = "";
    private String planName = "";

    // for BI
    private StringBuilder retVal = null;
    private StringBuilder bussIll = null;

    // sTring USed For Bi Grid And PDf Purpose
    private String age_entry = "";
    private String maturity_age = "";
    private String gender = "";
    private String policy_term = "";
    private String annualised_premium = "";
    private String sum_assured = "";
    private String samf = "";
    private String plan = "";
    private String netYield8Pr = "";
    private String redInYieldMat = "";
    private String MatBenefit6 = "";
    private String MatBenefit10 = "";

    private String proposer_Title = "", proposer_First_Name = "",
            proposer_Middle_Name = "", proposer_Last_Name = "";
    // product_name = "";

    // List used for The Policy Detail Depend on The policy Term
    private List<M_BI_FlexiGrid_Adapter> list_data;

    // Spinner USed
    private Spinner spnr_bi_flexi_smart_proposer_title;

    // edit Text Used
    private EditText edt_bi_flexi_smart_proposer_first_name;
    private EditText edt_bi_flexi_smart_proposer_middle_name;
    private EditText edt_bi_flexi_smart_proposer_last_name;

    private EditText edt_bi_flexi_smart_life_assured_first_name,
            edt_bi_flexi_smart_life_assured_middle_name,
            edt_bi_flexi_smart_life_assured_last_name;

    private Spinner spnr_bi_flexi_smart_life_assured_title;

    private TableRow tv_monthly_mode;

    private Button btn_bi_flexi_smart_life_assured_date;

    private String lifeAssured_Title = "", lifeAssured_First_Name = "",
            lifeAssured_Middle_Name = "", lifeAssured_Last_Name = "",
            name_of_life_assured = "", lifeAssured_date_of_birth = "";

    private TableRow tr_flexi_smart_proposer_detail2,
            tr_flexi_smart_proposer_detail1;

    private RadioButton rb_flexi_smart_plus_proposer_same_as_life_assured_yes,
            rb_flexi_smart_plus_proposer_same_as_life_assured_no;

    // For Bi Dialog
    private ParseXML prsObj;
    private String name_of_proposer = "";
    private String name_of_person = "";
    private String place2 = "";
    private String date1 = "";
    private String date2 = "";
    private String agent_sign = "";
    private String proposer_sign = "", proposer_Is_Same_As_Life_Assured = "";

    private File mypath;

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
    private Button btn_bi_flexi_smart_proposer_date;
    private EditText edt_bi_flexi_smart_proposer_age;
    private String proposer_date_of_birth = "";

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

    private String product_Code, product_UIN, product_cateogory, product_type;

    private String bankUserType = "", mode = "";

    private String Check = "";
    private Context context;
    private CommonMethods commonMethods;
    private StorageUtils mStorageUtils;
    private ImageButton imageButtonFlexiSmartPlusProposerPhotograph;

    private LinearLayout linearlayoutThirdpartySignature,
            linearlayoutAppointeeSignature;
    private ImageButton Ibtn_signatureofThirdParty, Ibtn_signatureofAppointee;
    private String thirdPartySign = "", appointeeSign = "", Company_policy_surrender_dec = "";

    private CheckBox cb_kerladisc;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.bi_flexismartplusmain);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);

        dbHelper = new DatabaseHelper(getApplicationContext());

        commonMethods = new CommonMethods();
        context = this;
        mStorageUtils = new StorageUtils();

        NABIObj = new NeedAnalysisBIService(this);
        prsObj = new ParseXML();
        Intent intent = getIntent();

        String na_flag = intent.getStringExtra("NAFlag");

        //Added Tushar Kadam Kerla Applicable Taxes 7-Jun-2019
        TableRow tablerowKerlaDiscount = findViewById(R.id.tablerowKerlaDiscount);
        cb_kerladisc = findViewById(R.id.cb_kerladisc);
        commonMethods.setKerlaDiscount(context, tablerowKerlaDiscount, cb_kerladisc);
        //End Added Tushar Kadam Kerla Applicable Taxes 7-Jun-2019

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

                    planName = "Flexi Smart Plus";

                    // Parivartan Changes
                    ProductInfo prodInfoObj = new ProductInfo();
                    product_Code = prodInfoObj.getProductCode(planName);
                    product_UIN = prodInfoObj.getProductUIN(planName);
                    product_cateogory = prodInfoObj
                            .getProductCategory(planName);
                    product_type = prodInfoObj.getProductType(planName);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                int k = 12 - (agentcode).length();
                StringBuilder zero = new StringBuilder();
                for (int i = 0; i < k; i++) {
                    zero = zero.append("0");
                }
                QuatationNumber = CommonForAllProd.getquotationNumber30("1M",
                        agentcode, zero + "");
            }
        } else {
            needAnalysis_flag = 0;
        }

        initialiseDate();
        obj = new CommonForAllProd();
        list_data = new ArrayList<>();
        prsObj = new ParseXML();

        spnr_bi_flexi_smart_proposer_title = findViewById(R.id.spnr_bi_flexi_smart_proposer_title);
        edt_bi_flexi_smart_proposer_first_name = findViewById(R.id.edt_bi_flexi_smart_proposer_first_name);
        edt_bi_flexi_smart_proposer_middle_name = findViewById(R.id.edt_bi_flexi_smart_proposer_middle_name);
        edt_bi_flexi_smart_proposer_last_name = findViewById(R.id.edt_bi_flexi_smart_proposer_last_name);
        rb_flexi_smart_plus_proposer_same_as_life_assured_yes = findViewById(R.id.rb_flexi_smart_plus_proposer_same_as_life_assured_yes);
        rb_flexi_smart_plus_proposer_same_as_life_assured_no = findViewById(R.id.rb_flexi_smart_plus_proposer_same_as_life_assured_no);
        tv_monthly_mode = findViewById(R.id.tv_monthly_mode);
        spnr_bi_flexi_smart_life_assured_title = findViewById(R.id.spnr_bi_flexi_smart_life_assured_title);
        edt_bi_flexi_smart_life_assured_first_name = findViewById(R.id.edt_bi_flexi_smart_life_assured_first_name);
        edt_bi_flexi_smart_life_assured_middle_name = findViewById(R.id.edt_bi_flexi_smart_life_assured_middle_name);
        edt_bi_flexi_smart_life_assured_last_name = findViewById(R.id.edt_bi_flexi_smart_life_assured_last_name);
        btn_bi_flexi_smart_life_assured_date = findViewById(R.id.btn_bi_flexi_smart_life_assured_date);
        tr_flexi_smart_proposer_detail2 = findViewById(R.id.tr_flexi_smart_proposer_detail2);
        tr_flexi_smart_proposer_detail1 = findViewById(R.id.tr_flexi_smart_proposer_detail1);

        edt_proposerdetail_basicdetail_contact_no = findViewById(R.id.edt_flexi_contact_no);
        edt_proposerdetail_basicdetail_Email_id = findViewById(R.id.edt_flexi_Email_id);
        edt_proposerdetail_basicdetail_ConfirmEmail_id = findViewById(R.id.edt_flexi_ConfirmEmail_id);

        cb_StaffDisc = findViewById(R.id.cb_staffdisc);
        /*
          change as per 1st jan 2014,by Vrushali Chaudhari
         */
        // cb_BankAssuranceDisc= (CheckBox)
        // findViewById(R.id.cb_BankAssuranceDisc);
        /*
          JK Resident added in xml and activity as per 1,Jan,2014 by Vrushali
          Chaudhari
         */
        selJkResident = findViewById(R.id.cb_JkResident);

        // inputActivityHeader = (TextView)
        // findViewById(R.id.txt_input_activityheader);

        commonMethods.fillSpinnerValue(context, spnr_bi_flexi_smart_proposer_title,
                Arrays.asList(getResources().getStringArray(R.array.arr_select_title)));
        commonMethods.fillSpinnerValue(context, spnr_bi_flexi_smart_life_assured_title,
                Arrays.asList(getResources().getStringArray(R.array.arr_select_title)));

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
        selGender.setClickable(false);
        selGender.setEnabled(false);

        ArrayAdapter<String> genderAdapter = new ArrayAdapter<>(
                getApplicationContext(), R.layout.spinner_item,
                getResources().getStringArray(R.array.gender_arrays));

        genderAdapter.setDropDownViewResource(R.layout.spinner_item1);
        selGender.setAdapter(genderAdapter);
        genderAdapter.notifyDataSetChanged();

        // Policy Term
        selBasicTerm = findViewById(R.id.policyterm);
        String[] policyTermList = new String[26];
        for (int i = 5; i <= 30; i++) {
            policyTermList[i - 5] = i + "";
        }
        ArrayAdapter<String> policyTermAdapter = new ArrayAdapter<>(
                getApplicationContext(), R.layout.spinner_item, policyTermList);
        policyTermAdapter.setDropDownViewResource(R.layout.spinner_item1);
        selBasicTerm.setAdapter(policyTermAdapter);
        policyTermAdapter.notifyDataSetChanged();

        // Premium Frequency
        selPremFreq = findViewById(R.id.premiumfreq);
        String[] premFreqList = {"Yearly", "Half Yearly", "Quarterly",
                "Monthly"};
        ArrayAdapter<String> premFreqAdapter = new ArrayAdapter<>(
                getApplicationContext(), R.layout.spinner_item, premFreqList);
        premFreqAdapter.setDropDownViewResource(R.layout.spinner_item1);
        selPremFreq.setAdapter(premFreqAdapter);

        premFreqAdapter.notifyDataSetChanged();
        basicSA = findViewById(R.id.premium_amt);
        SAMF = findViewById(R.id.samf);

        selOption = findViewById(R.id.option);
        String[] optionList = {"GOLD", "PLATINUM"};
        ArrayAdapter<String> optionAdapter = new ArrayAdapter<>(
                getApplicationContext(), R.layout.spinner_item, optionList);
        optionAdapter.setDropDownViewResource(R.layout.spinner_item1);
        selOption.setAdapter(optionAdapter);
        optionAdapter.notifyDataSetChanged();

        selHoliday = findViewById(R.id.holiday);
        txtHolidayYear = findViewById(R.id.txtholiday_year);
        HolidayYear = findViewById(R.id.holiday_year);
        txtHolidayTerm = findViewById(R.id.txtholiday_term);
        HolidayTerm = findViewById(R.id.holiday_term);

        selTopup = findViewById(R.id.topup);
        txtTopupPremium = findViewById(R.id.txttopup_premium);
        TopupPremium = findViewById(R.id.topup_premium);

        back = findViewById(R.id.back);
        btnSubmit = findViewById(R.id.btnSubmit);
        btn_bi_flexi_smart_proposer_date = findViewById(R.id.btn_bi_flexi_smart_proposer_date);
        edt_bi_flexi_smart_proposer_age = findViewById(R.id.edt_bi_flexi_smart_proposer_age);

        showAlert = new AlertDialog.Builder(this);
        currencyFormat = new DecimalFormat("##,##,##,###");
        /*
          Premium holiday and Topup are removed as per 1st jan 2014,by Vrushali
          Chaudhari.
         */
        selHoliday.setVisibility(View.GONE);
        selTopup.setVisibility(View.GONE);
        /********************** Item Listener starts from here ******************************************/

        /*
          Change as per 1st jan 2014,by Vrushali Chaudhari
         */

        if (needAnalysis_flag == 1 && !TextUtils.isEmpty(gender)) {
            selGender.setSelection(genderAdapter.getPosition(gender));
            onClickLADob(btn_bi_flexi_smart_life_assured_date);
        }

        proposer_Is_Same_As_Life_Assured = "y";

        edt_bi_flexi_smart_life_assured_first_name
                .setOnEditorActionListener(this);
        edt_bi_flexi_smart_life_assured_middle_name
                .setOnEditorActionListener(this);
        edt_bi_flexi_smart_life_assured_last_name
                .setOnEditorActionListener(this);
        edt_bi_flexi_smart_proposer_first_name.setOnEditorActionListener(this);
        edt_bi_flexi_smart_proposer_middle_name.setOnEditorActionListener(this);
        edt_bi_flexi_smart_proposer_last_name.setOnEditorActionListener(this);

        basicSA.setOnEditorActionListener(this);
        SAMF.setOnEditorActionListener(this);

        setFocusable(spnr_bi_flexi_smart_life_assured_title);
        spnr_bi_flexi_smart_life_assured_title.requestFocus();

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

        TableRow tr_staff_disc = findViewById(R.id.tr_flexi_staff_disc);
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
        // TODO Auto-generated method stub

        rb_flexi_smart_plus_proposer_same_as_life_assured_yes
                .setOnCheckedChangeListener(new OnCheckedChangeListener() {

                    public void onCheckedChanged(CompoundButton arg0,
                                                 boolean checked) {
                        // TODO Auto-generated method stub
                        if (checked) {
                            proposer_Is_Same_As_Life_Assured = "y";
                            tr_flexi_smart_proposer_detail1
                                    .setVisibility(View.GONE);
                            tr_flexi_smart_proposer_detail2
                                    .setVisibility(View.GONE);
                        }

                    }
                });
        rb_flexi_smart_plus_proposer_same_as_life_assured_no
                .setOnCheckedChangeListener(new OnCheckedChangeListener() {

                    public void onCheckedChanged(CompoundButton arg0,
                                                 boolean checked) {
                        // TODO Auto-generated method stub
                        if (checked) {
                            proposer_Is_Same_As_Life_Assured = "n";
                            tr_flexi_smart_proposer_detail1
                                    .setVisibility(View.VISIBLE);
                            tr_flexi_smart_proposer_detail2
                                    .setVisibility(View.VISIBLE);
                        }

                    }
                });

        spnr_bi_flexi_smart_proposer_title
                .setOnItemSelectedListener(new OnItemSelectedListener() {

                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int position, long id) {
                        // TODO Auto-generated method stub
                        if (position == 0) {
                            proposer_Title = "";
                        } else {
                            proposer_Title = spnr_bi_flexi_smart_proposer_title
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

        spnr_bi_flexi_smart_life_assured_title
                .setOnItemSelectedListener(new OnItemSelectedListener() {

                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int position, long id) {
                        // TODO Auto-generated method stub
                        if (position == 0) {
                            lifeAssured_Title = "";
                        } else {
                            lifeAssured_Title = spnr_bi_flexi_smart_life_assured_title
                                    .getSelectedItem().toString();
                            if (lifeAssured_Title.equalsIgnoreCase("Mr.")) {
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
                            }
                            clearFocusable(spnr_bi_flexi_smart_life_assured_title);
                            setFocusable(edt_bi_flexi_smart_life_assured_first_name);

                            edt_bi_flexi_smart_life_assured_first_name
                                    .requestFocus();
                        }

                    }

                    public void onNothingSelected(AdapterView<?> arg0) {
                        // TODO Auto-generated method stub

                    }
                });

        selPremFreq.setOnItemSelectedListener(new OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                // TODO Auto-generated method stub
                if (selPremFreq.getSelectedItem().toString().equals("Monthly")) {
                    tv_monthly_mode.setVisibility(View.VISIBLE);
                } else {
                    tv_monthly_mode.setVisibility(View.GONE);
                }
                if (b == 1) {
                    edt_bi_flexi_smart_life_assured_first_name.requestFocus();
                    b = 0;
                } else {
                    clearFocusable(selPremFreq);
                    setFocusable(basicSA);
                    basicSA.requestFocus();
                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }

        });

        ageInYears.setOnItemSelectedListener(new OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> arg0, View arg1, int pos,
                                       long id) {
                valMaturityAge();
            }

            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });

        // Basic Term
        selBasicTerm.setOnItemSelectedListener(new OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> arg0, View arg1, int pos,
                                       long id) {
                valMaturityAge();

                clearFocusable(selBasicTerm);
                setFocusable(selPremFreq);
                selPremFreq.requestFocus();

            }

            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });

        // Staff Discount
        cb_StaffDisc.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                if ((cb_StaffDisc.isChecked())) {
                    cb_StaffDisc.setChecked(true);
                    clearFocusable(spnr_bi_flexi_smart_life_assured_title);
                    setFocusable(spnr_bi_flexi_smart_life_assured_title);
                    spnr_bi_flexi_smart_life_assured_title.requestFocus();
                }
            }
        });

        selJkResident.setOnClickListener(new OnClickListener() {

            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if (selJkResident.isChecked()) {
                    selJkResident.setChecked(true);
                } else {
                    selJkResident.setChecked(false);
                }
            }
        });

        // Holioday Year
        selHoliday
                .setOnCheckedChangeListener(new OnCheckedChangeListener() {

                    public void onCheckedChanged(CompoundButton buttonView,
                                                 boolean isChecked) {
                        if (isChecked) {
                            txtHolidayYear.setVisibility(View.VISIBLE);
                            HolidayYear.setVisibility(View.VISIBLE);
                            txtHolidayTerm.setVisibility(View.VISIBLE);
                            HolidayTerm.setVisibility(View.VISIBLE);
                        } else {
                            txtHolidayYear.setVisibility(View.GONE);
                            HolidayYear.setVisibility(View.GONE);
                            txtHolidayTerm.setVisibility(View.GONE);
                            HolidayTerm.setVisibility(View.GONE);
                        }
                    }
                });

        // Topup
        selTopup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if (isChecked) {
                    txtTopupPremium.setVisibility(View.VISIBLE);
                    TopupPremium.setVisibility(View.VISIBLE);
                } else {
                    txtTopupPremium.setVisibility(View.GONE);
                    TopupPremium.setVisibility(View.GONE);
                }
            }
        });

        // Go Home Button
        back.setOnClickListener(new OnClickListener() {

            public void onClick(View arg0) {
                setResult(RESULT_OK);
                finish();
            }
        });

        // Submit Button
        btnSubmit.setOnClickListener(new OnClickListener() {

            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                inputVal = new StringBuilder();
                retVal = new StringBuilder();
                bussIll = new StringBuilder();

                proposer_First_Name = edt_bi_flexi_smart_proposer_first_name
                        .getText().toString();
                proposer_Middle_Name = edt_bi_flexi_smart_proposer_middle_name
                        .getText().toString();
                proposer_Last_Name = edt_bi_flexi_smart_proposer_last_name
                        .getText().toString();

                name_of_proposer = proposer_Title + " " + proposer_First_Name
                        + " " + proposer_Middle_Name + " " + proposer_Last_Name;

                lifeAssured_First_Name = edt_bi_flexi_smart_life_assured_first_name
                        .getText().toString();
                lifeAssured_Middle_Name = edt_bi_flexi_smart_life_assured_middle_name
                        .getText().toString();
                lifeAssured_Last_Name = edt_bi_flexi_smart_life_assured_last_name
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
                if (valProposerSameAsLifeAssured() && valBasicDetail()
                        && valLifeAssuredProposerDetail() && valDob()
                        && valPremiumAmount()) {
                    setEffectivePremium();
                    Date();

                    if (vallidate()) {

                        if (proposer_Is_Same_As_Life_Assured
                                .equalsIgnoreCase("y")) {
                            proposer_Title = "";
                            proposer_First_Name = "";
                            proposer_Middle_Name = "";
                            proposer_Last_Name = "";
                            name_of_proposer = "";
                            proposer_date_of_birth = "";
                        }

                        addListenerOnSubmit();
                        getInput(flexiSmartBean);
                        // insertDataIntoDatabase();

                        if (needAnalysis_flag == 0) {

                            Intent i = new Intent(
                                    BI_FlexiSmartPlusActivity.this,
                                    Success.class);

                            i.putExtra("ProductName",
                                    "Product : SBI Life - Flexi Smart Plus (UIN:111N093V01)");

                            i.putExtra(
                                    "op",
                                    "Sum Assured is Rs. "
                                            + currencyFormat.format(Double.parseDouble(prsObj
                                            .parseXmlTag(
                                                    retVal.toString(),
                                                    "sumAssured"))));
                            i.putExtra(
                                    "op1",
                                    "Fund Value @ 4% is Rs. "
                                            + currencyFormat.format(Double.parseDouble(prsObj
                                            .parseXmlTag(
                                                    retVal.toString(),
                                                    "MatBenefit6"))));
                            i.putExtra(
                                    "op2",
                                    "Fund Value @ 8% is Rs. "
                                            + currencyFormat.format(Double.parseDouble(prsObj
                                            .parseXmlTag(
                                                    retVal.toString(),
                                                    "MatBenefit10"))));
                            i.putExtra("header", "SBI Life - Flexi Smart Plus");
                            i.putExtra("header1", "(UIN:111N093V01)");
                            startActivity(i);
                        } else
                            Dialog();

                        // }
                        // };
                        // t.start();
                    }
                }

            }
        });

    }

    private void Date() {
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());

        // SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // formattedDate = df.format(c.getTime());
        // formattedDate have current date/time
        // Toast.makeText(this, formattedDate, Toast.LENGTH_SHORT).show();
        c.add(Calendar.DATE, 30);
        // ExpiredDate = df.format(c.getTime());
    }

    private void initialiseDate() {
        Calendar calender = Calendar.getInstance();
        mYear = calender.get(Calendar.YEAR);
        mMonth = calender.get(Calendar.MONTH);
        mDay = calender.get(Calendar.DAY_OF_MONTH);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        /*
         * Parivartan Changes
         */
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == commonMethods.SIGNATURE_ACTIVITY) {
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

                        // Ibtn_signatureofPolicyHolders
                        // .setImageBitmap(ProposerCaptureSignature.scaled);
                        // Bitmap signature = ProposerCaptureSignature.scaled;
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
                        Bitmap bmp = BitmapFactory.decodeFile(Photo.getAbsolutePath());

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
                            imageButtonFlexiSmartPlusProposerPhotograph
                                    .setImageBitmap(scaled);
                        } else {
                            photoBitmap = null;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        }
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
                    // ProposerAge = final_age;
                    final_age = Integer.toString(age);
                    if (final_age.contains("-")) {
                        commonMethods.BICommonDialog(context, "Please fill Valid Bith Date");
                    } else {
                        if (18 <= age) {

                            btn_bi_flexi_smart_proposer_date.setText(date);
                            edt_bi_flexi_smart_proposer_age.setText(final_age);
                            proposer_date_of_birth = getDate1(date + "");

                            // ageInYears
                            // .setSelection(getIndex(ageInYears, final_age));
                            // valMaturityAge();
                        } else {
                            commonMethods.BICommonDialog(context, "Minimum age should be 18 yrs for proposer.");
                            btn_bi_flexi_smart_proposer_date.setText("Select Date");
                            edt_bi_flexi_smart_proposer_age.setText("");
                            proposer_date_of_birth = "";
                        }
                    }
                    break;

                case 5:
                    // lifeAssuredAge = final_age;
                    final_age = Integer.toString(age);
                    if (final_age.contains("-")) {
                        commonMethods.BICommonDialog(context, "Please fill Valid Bith Date");
                    } else {
                        if (18 <= age && age <= 60) {

                            btn_bi_flexi_smart_life_assured_date.setText(date);
                            // edt_bi_flexi_smart_life_assured_age.setText(final_age);

                            ageInYears.setSelection(
                                    getIndex(ageInYears, final_age), false);
                            valMaturityAge();
                            lifeAssured_date_of_birth = getDate1(date + "");

                            clearFocusable(btn_bi_flexi_smart_life_assured_date);

                            setFocusable(edt_proposerdetail_basicdetail_contact_no);
                            edt_proposerdetail_basicdetail_contact_no
                                    .requestFocus();
                            /*
                             * setFocusable(selBasicTerm);
                             * selBasicTerm.requestFocus();
                             */
                        } else {
                            commonMethods.BICommonDialog(context, "Minimum Age should be 18 yrs and Maximum Age should be 60 yrs For LifeAssured");
                            btn_bi_flexi_smart_life_assured_date
                                    .setText("Select Date");
                            // edt_bi_flexi_smart_life_assured_age.setText("");
                            lifeAssured_date_of_birth = "";

                            clearFocusable(btn_bi_flexi_smart_life_assured_date);
                            setFocusable(btn_bi_flexi_smart_life_assured_date);
                            btn_bi_flexi_smart_life_assured_date.requestFocus();
                        }
                    }
                    break;

                default:
                    break;
            }
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

    private int getIndex(Spinner s1, String value) {

        int index = 0;

        for (int i = 0; i < s1.getCount(); i++) {
            if (s1.getItemAtPosition(i).equals(value)) {
                index = i;
            }
        }
        return index;
    }

    // public boolean getValueFromDatabase() {
    // List<M_Benefit_Illustration_Detail> data = new
    // ArrayList<M_Benefit_Illustration_Detail>();
    // // retrieving data from database
    // boolean flag = false;
    // data = db.getBIDetail(QuatationNumber);
    // if (data.size() > 0) {
    // int i = 0;
    // output = data.get(i).getOutput();
    // input = data.get(i).getInput();
    // proposal_no = data.get(i).getProposal_no();
    // name_of_life_assured = data.get(i).getName_of_lifeAssured();
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

    // public void parse_Xml()
    // {
    //
    // }

    private void windowmessagesgin() {

        d = new Dialog(BI_FlexiSmartPlusActivity.this);
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
                Intent intent = new Intent(BI_FlexiSmartPlusActivity.this,
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

    private void Dialog() {
        final Dialog d = new Dialog(this);
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);
        d.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.BLACK));

        d.setContentView(R.layout.layout_flexi_plus_bi_grid);

        TextView tv_proposername = d
                .findViewById(R.id.tv_proposername);
        TextView tv_proposal_number = d
                .findViewById(R.id.tv_proposal_number);
        TextView tv_bi_flexi_smart_plus_age_at_entry = d
                .findViewById(R.id.tv_bi_flexi_smart_plus_age_at_entry);
        TextView tv_bi_flexi_smart_plus_maturity_age = d
                .findViewById(R.id.tv_bi_flexi_smart_plus_maturity_age);
        TextView tv_bi_flexi_smart_plus_life_assured_gender = d
                .findViewById(R.id.tv_bi_flexi_smart_plus_life_assured_gender);
        TextView tv_bi_flexi_smart_plus_policy_term = d
                .findViewById(R.id.tv_bi_flexi_smart_plus_policy_term);
        TextView tv_bi_flexi_smart_plus_annualised_premium = d
                .findViewById(R.id.tv_bi_flexi_smart_plus_annualised_premium);
        TextView tv_bi_flexi_smart_plus_sum_assured_main = d
                .findViewById(R.id.tv_bi_flexi_smart_plus_sum_assured_main);
        TextView tv_bi_flexi_smart_plus_samf_main = d
                .findViewById(R.id.tv_bi_flexi_smart_plus_samf_main);
        TextView tv_bi_flexi_smart_plus_option = d
                .findViewById(R.id.tv_bi_flexi_smart_plus_option);
        TextView tv_bi_flexi_smart_plus_gross_yield_of_shadow_account = d
                .findViewById(R.id.tv_bi_flexi_smart_plus_gross_yield_of_shadow_account);
        TextView tv_bi_flexi_smart_plus_net_yield_at_maturity = d
                .findViewById(R.id.tv_bi_flexi_smart_plus_net_yield_at_maturity);
        TextView tv_bi_flexi_smart_plus_reduction_in_yield_at_maturity = d
                .findViewById(R.id.tv_bi_flexi_smart_plus_reduction_in_yield_at_maturity);
        TextView tv_bi_flexi_smart_plus_maturity_benefit_at_the_end_4 = d
                .findViewById(R.id.tv_bi_flexi_smart_plus_maturity_benefit_at_the_end_4);
        TextView tv_bi_flexi_smart_plus_maturity_benefit_at_the_end_8 = d
                .findViewById(R.id.tv_bi_flexi_smart_plus_maturity_benefit_at_the_end_8);

        TextView tv_bi_flexi_smart_plus_premium_tag = d
                .findViewById(R.id.tv_bi_flexi_smart_plus_premium_tag);
        TextView tv_bi_flexi_smart_plus_premium_value = d
                .findViewById(R.id.tv_bi_flexi_smart_plus_premium_value);

        TextView tv_bi_flexi_smart_plus_is_Staff = d
                .findViewById(R.id.tv_bi_flexi_smart_plus_is_Staff);

        TextView tv_bi_is_JK = d.findViewById(R.id.tv_bi_is_JK);

        View view_flexi_plus_premium_row1 = d
                .findViewById(R.id.view_flexi_plus_premium_row1);
        TableRow tr_flexi_plus_premium_row1 = d
                .findViewById(R.id.tr_flexi_plus_premium_row1);
        GridView gv_userinfo = d.findViewById(R.id.gv_userinfo);
        gv_userinfo.setVerticalScrollBarEnabled(true);
        gv_userinfo.setSmoothScrollbarEnabled(true);

        final EditText edt_Policyholderplace = d
                .findViewById(R.id.edt_Policyholderplace);

        final TextView edt_proposer_name = d
                .findViewById(R.id.edt_ProposalName);

        /* Need Analysis */
        final TextView edt_proposer_name_need_analysis = d
                .findViewById(R.id.edt_proposer_name_need_analysis);
        final CheckBox cb_statement = d
                .findViewById(R.id.cb_statement);
        final CheckBox cb_statement_need_analysis = d
                .findViewById(R.id.cb_statement_need_analysis);
        cb_statement_need_analysis.setChecked(true);
        TableRow tr_need_analysis = d
                .findViewById(R.id.tr_need_analysis);

        /* parivatan change */
        imageButtonFlexiSmartPlusProposerPhotograph = d
                .findViewById(R.id.imageButtonFlexiSmartPlusProposerPhotograph);
        imageButtonFlexiSmartPlusProposerPhotograph
                .setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (cb_statement.isChecked()
                                && cb_statement_need_analysis.isChecked()) {
                            Check = "Photo";
                            commonMethods.windowmessage(context,
                                    "_cust1Photo.jpg");
                        } else {
                            commonMethods.dialogWarning(context, "Please Tick on I Agree Clause ", true);
                            setFocusable(cb_statement);
                            cb_statement.requestFocus();
                        }

                    }
                });

        cb_statement.setChecked(false);

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
                            + " have understood the term and conditions, product features and applicable charges (if any) before purchase of the contract, after receipt of all infromation stated above from the insurer.");
            edt_proposer_name_need_analysis
                    .setText("I, "
                            + name_of_life_assured
                            + " have undergone the Need Analysis & after having reviewed the SBI Life Product options, I have opted for SBI LIFE - Flexi Smart Plus.");

            // tv_life_assured_name.setText(name_of_life_assured);
            tv_proposername.setText(name_of_life_assured);
        } else if (proposer_Is_Same_As_Life_Assured.equalsIgnoreCase("n")) {
            edt_proposer_name
                    .setText("I, "
                            + name_of_proposer
                            + " have understood the term and conditions, product features and applicable charges (if any) before purchase of the contract, after receipt of all infromation stated above from the insurer.");
            edt_proposer_name_need_analysis
                    .setText("I, "
                            + name_of_proposer
                            + " have undergone the Need Analysis after having reviewed the SBI Life Product options, I have opted for SBI LIFE - Flexi Smart Plus.");

            tv_proposername.setText(name_of_proposer);
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
            cb_statement_need_analysis.setChecked(true);

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

        /* parivartan changes */
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
        /* end */

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

        // Parivartan Changes
        imageButtonFlexiSmartPlusProposerPhotograph = d
                .findViewById(R.id.imageButtonFlexiSmartPlusProposerPhotograph);
        imageButtonFlexiSmartPlusProposerPhotograph
                .setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (cb_statement.isChecked()
                                && cb_statement_need_analysis.isChecked()) {
                            Check = "Photo";
                            commonMethods.windowmessage(context,
                                    "_cust1Photo.jpg");
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
            imageButtonFlexiSmartPlusProposerPhotograph
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
                            imageButtonFlexiSmartPlusProposerPhotograph
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

        btn_proceed.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                // if (frmProductHome.equals("FALSE")) {
                name_of_person = edt_proposer_name.getText().toString();
                // place1 = edt_MarketingOfficalPlace.getText().toString();
                place2 = edt_Policyholderplace.getText().toString();
                //
                if (!place2.equals("")
                        && !date1.equals("Select Date")
                        && (!date2.equals("Select Date") || bankUserType
                        .equalsIgnoreCase("Y"))
                        && (!agent_sign.equals("") || bankUserType
                        .equalsIgnoreCase("Y"))
                        && (!proposer_sign.equals("") || bankUserType
                        .equalsIgnoreCase("Y"))
                        && (cb_statement.isChecked())
                        // Parivartan Changes
                        && (((photoBitmap != null
                        //remove parivartan validation for third party
								/*&& ((radioButtonDepositPaymentNo.isChecked() == true && !thirdPartySign
                        .equals("")) || radioButtonDepositPaymentYes
										.isChecked() == true)*/
                        //remove parivartan validation for appointee
								/*&& ((radioButtonAppointeeYes.isChecked() == true && !appointeeSign
                        .equals("")) || radioButtonAppointeeNo
								.isChecked() == true)*/
                ) && radioButtonTrasactionModeParivartan.isChecked())
                        || radioButtonTrasactionModeManual.isChecked())) {

                    NeedAnalysisActivity.str_need_analysis = "";

                    if (radioButtonTrasactionModeParivartan.isChecked()) {
                        mode = "Parivartan";
                    } else if (radioButtonTrasactionModeManual.isChecked()) {
                        mode = "Manual";
                    }


                    // String isActive = "0"; { "GOLD", "PLATINUM" };
                    String productCode = "1XSGS";
                    if (flexiSmartBean.getOption().equals("GOLD"))
                        productCode = "VPLUSG";
                    else if (flexiSmartBean.getOption().equals("PLATINUM"))
                        productCode = "VPLUSP";

                    /* parivartan changes */
                    na_cbi_bean = new NA_CBI_bean(QuatationNumber, agentcode,
                            "", userType, "", lifeAssured_Title,
                            lifeAssured_First_Name, lifeAssured_Middle_Name,
                            lifeAssured_Last_Name, planName,
                            obj.getRound(obj.getStringWithout_E(Double
                                    .valueOf(sum_assured.equals("") ? "0"
                                            : sum_assured))), obj
                            .getRound(premiumAmount), emailId,
                            mobileNo, agentEmail, agentMobile, na_input,
                            na_output, premiumPaymentMode, Integer
                            .parseInt(policy_term), 0, productCode,
                            getDate(lifeAssured_date_of_birth), "", inputVal
                            .toString(), retVal.toString().replace(
                            bussIll.toString(), ""));

                    name_of_person = name_of_life_assured;
                    dbHelper.AddNeedAnalysisDashboardDetails(new ProductBIBean(
                            "", QuatationNumber, planName, getCurrentDate(),
                            mobileNo, getCurrentDate(), dbHelper.GetUserCode(),
                            emailId, "", "", agentcode, "", userType, "",
                            lifeAssured_Title, lifeAssured_First_Name,
                            lifeAssured_Middle_Name, lifeAssured_Last_Name,
                            obj.getRound(obj.getStringWithout_E(Double
                                    .valueOf(sum_assured.equals("") ? "0"
                                            : sum_assured))), obj
                            .getRound(premiumAmount), agentEmail,
                            agentMobile, na_input, na_output,
                            premiumPaymentMode, Integer.parseInt(policy_term),
                            0, productCode, getDate(lifeAssured_date_of_birth),
                            "", "", mode, inputVal
                            .toString(), retVal.toString().replace(
                            bussIll.toString(), "")));
                    /* end */

                    CreateFlexiSmartPlusBIPdf();
                    NABIObj.serviceHit(BI_FlexiSmartPlusActivity.this,
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
                    // Parivartan Changes
                    else if (!radioButtonTrasactionModeParivartan.isChecked()
                            && !radioButtonTrasactionModeManual.isChecked()) {
                        commonMethods.dialogWarning(context, "Please Select Transaction Mode", true);
                        setFocusable(linearlayoutTrasactionModeParivartan);
                        linearlayoutTrasactionModeParivartan.requestFocus();
                    } else if (photoBitmap == null) {
                        commonMethods.dialogWarning(context, "Please Capture the Photo", true);
                        setFocusable(imageButtonFlexiSmartPlusProposerPhotograph);
                        imageButtonFlexiSmartPlusProposerPhotograph
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

        String input = inputVal.toString();
        String output = retVal.toString();

        age_entry = prsObj.parseXmlTag(input, "age");
        tv_bi_flexi_smart_plus_age_at_entry.setText(age_entry + " Years");

        maturity_age = prsObj.parseXmlTag(output, "maturityAge");
        tv_bi_flexi_smart_plus_maturity_age.setText(maturity_age + " Years");

        gender = prsObj.parseXmlTag(input, "gender");
        tv_bi_flexi_smart_plus_life_assured_gender.setText(gender);

        policy_term = prsObj.parseXmlTag(input, "policyTerm");

        String staffdiscount = prsObj.parseXmlTag(input, "isStaff");
        // staffdiscount_per = prsObj.parseXmlTag(output,
        // "staffDiscPercentage");

        if (staffdiscount.equalsIgnoreCase("true")) {
            // tr_staff_per.setVisibility(View.VISIBLE);
            tv_bi_flexi_smart_plus_is_Staff.setText("Yes");
        } else {
            tv_bi_flexi_smart_plus_is_Staff.setText("No");
        }

        String isJkResident = prsObj.parseXmlTag(input, "isJKResident");

        if (isJkResident.equalsIgnoreCase("true")) {
            tv_bi_is_JK.setText("Yes");
        } else {
            tv_bi_is_JK.setText("No");
        }

        tv_bi_flexi_smart_plus_policy_term.setText(policy_term + " Years");

        premiumPaymentMode = prsObj.parseXmlTag(input, "premFreq");
        premiumAmount = prsObj.parseXmlTag(input, "premiumAmount");
        if (premiumPaymentMode.equalsIgnoreCase("Half Yearly")) {
            tr_flexi_plus_premium_row1.setVisibility(View.VISIBLE);
            view_flexi_plus_premium_row1.setVisibility(View.VISIBLE);
            tv_bi_flexi_smart_plus_premium_tag.setText("Half Yearly Premium");
            tv_bi_flexi_smart_plus_premium_value
                    .setText("Rs. " + premiumAmount);

        } else if (premiumPaymentMode.equalsIgnoreCase("Quarterly")) {
            tr_flexi_plus_premium_row1.setVisibility(View.VISIBLE);
            view_flexi_plus_premium_row1.setVisibility(View.VISIBLE);
            tv_bi_flexi_smart_plus_premium_tag.setText("Quarterly Premium");
            tv_bi_flexi_smart_plus_premium_value
                    .setText("Rs. " + premiumAmount);
        } else if (premiumPaymentMode.equalsIgnoreCase("Monthly")) {
            tr_flexi_plus_premium_row1.setVisibility(View.VISIBLE);
            view_flexi_plus_premium_row1.setVisibility(View.VISIBLE);
            tv_bi_flexi_smart_plus_premium_tag.setText("Monthly Premium");
            tv_bi_flexi_smart_plus_premium_value
                    .setText("Rs. " + premiumAmount);
        }

        annualised_premium = prsObj.parseXmlTag(output, "AnnPrem");
        tv_bi_flexi_smart_plus_annualised_premium.setText("Rs. "
                + obj.getRound(obj.getStringWithout_E(Double
                .valueOf(annualised_premium.equals("") ? "0"
                        : annualised_premium))));

        sum_assured = prsObj.parseXmlTag(output, "sumAssured");

        tv_bi_flexi_smart_plus_sum_assured_main.setText("Rs. "
                + getformatedThousandString(Long.parseLong(obj.getRound(obj
                .getStringWithout_E(Double.valueOf(sum_assured
                        .equals("") ? "0" : sum_assured))))));
        samf = prsObj.parseXmlTag(input, "SAMF");
        tv_bi_flexi_smart_plus_samf_main.setText(Double.valueOf((samf
                .equals("") || samf == null) ? "0" : samf) + "");

        plan = prsObj.parseXmlTag(input, "plan");
        tv_bi_flexi_smart_plus_option.setText(plan);

        tv_bi_flexi_smart_plus_gross_yield_of_shadow_account.setText("8 %");

        netYield8Pr = prsObj.parseXmlTag(output, "netYield8Pr");
        tv_bi_flexi_smart_plus_net_yield_at_maturity.setText(netYield8Pr + "%");

        redInYieldMat = prsObj.parseXmlTag(output, "redInYieldMat");
        tv_bi_flexi_smart_plus_reduction_in_yield_at_maturity
                .setText(redInYieldMat + "%");

        MatBenefit6 = prsObj.parseXmlTag(output, "MatBenefit6");


        tv_bi_flexi_smart_plus_maturity_benefit_at_the_end_4
                .setText("Maturity Benefit payable at the end of the term of the policy considering gross yield of shadow policy account @ 4% is Rs. "
                        + obj.getRound(obj.getStringWithout_E(Double
                        .valueOf((MatBenefit6.equals("") || MatBenefit6 == null) ? "0"
                                : MatBenefit6))));

        MatBenefit10 = prsObj.parseXmlTag(output, "MatBenefit10");


        tv_bi_flexi_smart_plus_maturity_benefit_at_the_end_8
                .setText("Maturity Benefit payable at the end of the term of the policy considering gross yield of shadow policy account @ 8% is Rs. "
                        + obj.getRound(obj.getStringWithout_E(Double
                        .valueOf((MatBenefit10.equals("") || MatBenefit10 == null) ? "0"
                                : MatBenefit10))));

        TextView tv_Company_policy_surrender_dec = d
                .findViewById(R.id.tv_Company_policy_surrender_dec);

        Company_policy_surrender_dec = "Your SBI LIFE - Flexi Smart Plus (UIN - 111N093V01) is a "
                + "Regular/Limited"
                + " premium policy, for which your first year Yearly premium is Rs. "
                + obj.getRound(obj.getStringWithout_E(Double
                .valueOf(annualised_premium.equals("") ? "0"
                        : annualised_premium)))
                + " .Your Policy Term is "
                + policy_term
                + " years"
                + " , Premium Paying Term is "
                + policy_term
                + " years"
                + " and Basic Sum Assured is Rs. "
                + getformatedThousandString(Long.parseLong(obj.getRound(obj
                .getStringWithout_E(Double.valueOf(sum_assured
                        .equals("") ? "0" : sum_assured))))) + ".";

        tv_Company_policy_surrender_dec.setText(Company_policy_surrender_dec);

        for (int i = 1; i <= Integer.parseInt(policy_term); i++) {

            String policy_year = prsObj
                    .parseXmlTag(output, "policyYr" + i + "");
            String policy_In_Force = "";
            // String policy_In_Force = prsObj.parseXmlTag(output,
            // "policyInForce"
            // + i + "");
            String premium = prsObj.parseXmlTag(output, "AnnPrem" + i + "");

            String topup_premium = "";

            // String topup_premium = prsObj.parseXmlTag(output, "topUpPrem" + i
            // + "");
            String premium_allocation_charge = prsObj.parseXmlTag(output,
                    "PremAllCharge" + i + "");
            String policy_administration_charge = prsObj.parseXmlTag(output,
                    "PolicyAdmCharge" + i + "");
            String service_tax = prsObj.parseXmlTag(output, "serviceTax" + i
                    + "");
            String total_deduction = prsObj.parseXmlTag(output,
                    "totalDeduction" + i + "");
            String opening_balance1 = prsObj.parseXmlTag(output,
                    "openBalance4Pr" + i + "");
            String mortality_charge1 = prsObj.parseXmlTag(output, "MortChrg4Pr"
                    + i + "");
            String service_tax_on_mortality_charge1 = prsObj.parseXmlTag(
                    output, "serTxonMortChrg4Pr" + i + "");
            String guranteed_addition1 = prsObj.parseXmlTag(output,
                    "GuareentedAdd4Pr" + i + "");
            String extra_addition1 = prsObj.parseXmlTag(output, "extraAdd4Pr"
                    + i + "");
            String fmc1 = prsObj.parseXmlTag(output, "FMC4Pr" + i + "");
            String sservice_tax_on_fmc1 = prsObj.parseXmlTag(output,
                    "serTxonFMC4Pr" + i + "");
            String closing_balance1 = prsObj.parseXmlTag(output,
                    "closingBalance4Pr" + i + "");
            String surrender_value1 = prsObj.parseXmlTag(output,
                    "SurrenderVal4Pr" + i + "");
            String death_benefit1 = prsObj.parseXmlTag(output,
                    "DeathBenefit4Pr" + i + "");
            String opening_balance2 = prsObj.parseXmlTag(output,
                    "openBalance8Pr" + i + "");
            String mortality_charge2 = prsObj.parseXmlTag(output, "MortChrg8Pr"
                    + i + "");
            String service_tax_on_mortality_charge2 = prsObj.parseXmlTag(
                    output, "serTxonMortChrg8Pr" + i + "");
            String guranteed_addition2 = prsObj.parseXmlTag(output,
                    "GuareentedAdd8Pr" + i + "");
            String extra_addition2 = prsObj.parseXmlTag(output, "extraAdd8Pr"
                    + i + "");
            String fmc2 = prsObj.parseXmlTag(output, "FMC8Pr" + i + "");
            String sservice_tax_on_fmc2 = prsObj.parseXmlTag(output,
                    "serTxonFMC8Pr" + i + "");
            String closing_balance2 = prsObj.parseXmlTag(output,
                    "closingBalance8Pr" + i + "");
            String surrender_value2 = prsObj.parseXmlTag(output,
                    "SurrenderVal8Pr" + i + "");
            String death_benefit2 = prsObj.parseXmlTag(output,
                    "DeathBenefit8Pr" + i + "");

            String commission = prsObj.parseXmlTag(output, "CommIfPay8Pr" + i
                    + "");

            list_data.add(new M_BI_FlexiGrid_Adapter(policy_year,
                    policy_In_Force, premium, topup_premium,
                    premium_allocation_charge, policy_administration_charge,
                    service_tax, total_deduction, opening_balance1,
                    mortality_charge1, service_tax_on_mortality_charge1,
                    guranteed_addition1, extra_addition1, fmc1,
                    sservice_tax_on_fmc1, closing_balance1, surrender_value1,
                    death_benefit1, opening_balance2, mortality_charge2,
                    service_tax_on_mortality_charge2, guranteed_addition2,
                    extra_addition2, fmc2, sservice_tax_on_fmc2,
                    closing_balance2, surrender_value2, death_benefit2,
                    commission));

        }
        Adapter_BI_FlexiSmartGrid adapter = new Adapter_BI_FlexiSmartGrid(
                BI_FlexiSmartPlusActivity.this, list_data);

        gv_userinfo.setAdapter(adapter);

        GridHeight gh = new GridHeight();
        gh.getheight(gv_userinfo, policy_term);

        LinearLayout ll_signature = d
                .findViewById(R.id.ll_signature);
        TableRow tbrw_quotationNo = d
                .findViewById(R.id.tbrw_quotationNo);
        if (needAnalysis_flag == 1) {
            ll_signature.setVisibility(View.VISIBLE);
            tbrw_quotationNo.setVisibility(View.VISIBLE);

        } else {
            ll_signature.setVisibility(View.GONE);
            tbrw_quotationNo.setVisibility(View.GONE);
        }

        d.show();

    }

    private void CreateFlexiSmartPlusBIPdf() {

        try {

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
                            "Benefit Illustration for SBI LIFE - Flexi Smart Plus(UIN-111N093V01)",
                            headerBold));

            PdfPTable headertable = new PdfPTable(1);
            headertable.setWidthPercentage(100);
            PdfPCell c1 = new PdfPCell(new Phrase(Para_Header));
            c1.setBackgroundColor(BaseColor.DARK_GRAY);
            c1.setPadding(5);
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            headertable.addCell(c1);
            headertable.setHorizontalAlignment(Element.ALIGN_LEFT);
            Paragraph para_address = new Paragraph(
                    "SBI Life Insurance Co. Ltd Corporate Office: 'Natraj', M.V. Road and Western Express, Highway Junction, Andheri (East),Mumbai  400069. Regn No. 111",

                    small_bold);
            para_address.setAlignment(Element.ALIGN_CENTER);
            document.add(para_address);
            document.add(para_img_logo_after_space_1);
            document.add(headertable);

            document.add(para_img_logo_after_space_1);
            document.add(para_img_logo_after_space_1);

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

            // String QuatationNumber = "0001234567891234567";

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
                    "Proposer Name ", small_normal));
            PdfPCell NameofProposal_cell_4 = new PdfPCell(new Paragraph(
                    name_of_proposer, small_bold1));
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
            // PdfPCell BI_Pdftable1_cell1 = new PdfPCell(
            // new Paragraph(
            // "IN THIS POLICY, THE INVESTMENT RISK IN INVESTMENT PORTFOLIO IS BORNE BY THE POLICYHOLDER.",
            // redFont));
            //
            // BI_Pdftable1_cell1.setPadding(5);
            //
            // BI_Pdftable1_cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
            // BI_Pdftable1.addCell(BI_Pdftable1_cell1);
            // document.add(BI_Pdftable1);

            PdfPTable BI_Pdftable2 = new PdfPTable(1);
            BI_Pdftable2.setWidthPercentage(100);
            // CR_table6.setWidths(columnWidths_2);
            PdfPCell BI_Pdftable2_cell1 = new PdfPCell(
                    new Paragraph(
                            "Insurance Regulatory & Development Authority of India(IRDAI) requires all life insurance companies operating in India to provide official illustrations to their customers. The illustrations are based on the investment rates of return as prescribed by IRDAI (Non- Linked Insurance Products), Regulations 2013.  The two rates of investment return currently are 4% and 8% per annum.",
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
                            "Some benefits are guaranteed and some benefits are variable with returns based on the future performance of SBI Life Insurance Company Limited. If your policy offers guaranteed returns then the same will be clearly marked as guaranteed in the illustration table on this page. If your policy offers variable returns then the illustration on this page will show two different rates of assumed future investment returns. These assumed rates of return are not guaranteed and they are not the upper or lower limits of what you might get back, as the value of your policy is dependent on a number of factors including future investment performance."

                            , small_normal));

            BI_Pdftable4_cell1.setPadding(5);

            BI_Pdftable4.addCell(BI_Pdftable4_cell1);
            document.add(BI_Pdftable4);

            document.add(para_img_logo_after_space_1);

            String isStaff = "";
            if (cb_StaffDisc.isChecked()) {
                isStaff = "yes";

                PdfPTable table_staff_disccount = new PdfPTable(2);
                table_staff_disccount.setWidthPercentage(100);

                PdfPCell cell_staff_disccount1 = new PdfPCell(new Paragraph(
                        "Staff Discount", small_normal));
                cell_staff_disccount1.setPadding(5);
                PdfPCell cell_staff_disccount2 = new PdfPCell(new Paragraph(
                        isStaff, small_bold));
                cell_staff_disccount2.setPadding(5);
                cell_staff_disccount2
                        .setHorizontalAlignment(Element.ALIGN_CENTER);

                table_staff_disccount.addCell(cell_staff_disccount1);
                table_staff_disccount.addCell(cell_staff_disccount2);

                document.add(table_staff_disccount);
            }

            String isJK = "";
            if (selJkResident.isChecked()) {
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

            PdfPTable BI_PdftablePlanDetails = new PdfPTable(1);
            BI_PdftablePlanDetails.setWidthPercentage(100);
            PdfPCell BI_PdftablePlanDetails_cell = new PdfPCell(new Paragraph(
                    "Plan Details", small_bold));

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
                    "Life Assured  Age at Entry " + "  ", small_normal));
            cell_lifeAssuredAge1.setPadding(5);
            PdfPCell cell_lifeAssuredAge2 = new PdfPCell(new Paragraph("  "
                    + age_entry + "  Years", small_bold));
            cell_lifeAssuredAge2.setPadding(5);
            cell_lifeAssuredAge2.setHorizontalAlignment(Element.ALIGN_CENTER);

            PdfPCell cell_lifeAssuredAmaturityAge1 = new PdfPCell(
                    new Paragraph("Maturity Age" + "  ", small_normal));
            cell_lifeAssuredAmaturityAge1.setPadding(5);
            PdfPCell cell_lifeAssuredAmaturityAge2 = new PdfPCell(
                    new Paragraph(maturity_age + "  Years", small_bold));
            cell_lifeAssuredAmaturityAge2.setPadding(5);
            cell_lifeAssuredAmaturityAge2
                    .setHorizontalAlignment(Element.ALIGN_CENTER);

            table_lifeAssuredDetails.addCell(cell_lifeAssuredAge1);
            table_lifeAssuredDetails.addCell(cell_lifeAssuredAge2);

            table_lifeAssuredDetails.addCell(cell_lifeAssuredAmaturityAge1);
            table_lifeAssuredDetails.addCell(cell_lifeAssuredAmaturityAge2);

            document.add(table_lifeAssuredDetails);

            PdfPTable tablePolicyDetails1 = new PdfPTable(4);
            tablePolicyDetails1.setWidthPercentage(100);

            PdfPCell cell_lifeAssured_gender1 = new PdfPCell(new Paragraph(
                    "Life Assured Gender", small_normal));
            cell_lifeAssured_gender1.setPadding(5);
            PdfPCell cell_lifeAssured_gender2 = new PdfPCell(new Paragraph("  "
                    + gender, small_bold));
            cell_lifeAssured_gender2.setPadding(5);
            cell_lifeAssured_gender2
                    .setHorizontalAlignment(Element.ALIGN_CENTER);

            PdfPCell cell_termsofPolicy1 = new PdfPCell(new Paragraph(
                    "Term of the policy " + "  ", small_normal));
            cell_lifeAssuredAge1.setPadding(5);
            PdfPCell cell_termsofPolicy2 = new PdfPCell(new Paragraph(
                    policy_term + "  Years", small_bold));
            cell_termsofPolicy2.setPadding(5);
            cell_termsofPolicy2.setHorizontalAlignment(Element.ALIGN_CENTER);

            tablePolicyDetails1.addCell(cell_lifeAssured_gender1);
            tablePolicyDetails1.addCell(cell_lifeAssured_gender2);

            tablePolicyDetails1.addCell(cell_termsofPolicy1);
            tablePolicyDetails1.addCell(cell_termsofPolicy2);

            document.add(tablePolicyDetails1);

            PdfPTable tablePolicyDetails3 = new PdfPTable(4);
            tablePolicyDetails3.setWidthPercentage(100);

            PdfPCell cell_regularPremium1 = new PdfPCell(new Paragraph(
                    "Annual Premium", small_normal));
            cell_regularPremium1.setPadding(5);
            PdfPCell cell_regularPremium2 = new PdfPCell(new Paragraph(" Rs.  "
                    + obj.getRound(obj.getStringWithout_E(Double
                    .valueOf(annualised_premium.equals("") ? "0"
                            : annualised_premium))), small_bold));
            cell_regularPremium2.setPadding(5);
            cell_regularPremium2.setHorizontalAlignment(Element.ALIGN_CENTER);

            PdfPCell cell_sumAssured1 = new PdfPCell(new Paragraph(
                    "Sum assured", small_normal));
            cell_sumAssured1.setPadding(5);
            PdfPCell cell_sumAssured2 = new PdfPCell(new Paragraph(
                    " Rs.  "
                            + obj.getRound(obj.getStringWithout_E(Double
                            .valueOf(sum_assured.equals("") ? "0"
                                    : sum_assured))), small_bold));
            cell_sumAssured2.setPadding(5);
            cell_sumAssured2.setHorizontalAlignment(Element.ALIGN_CENTER);

            tablePolicyDetails3.addCell(cell_regularPremium1);
            tablePolicyDetails3.addCell(cell_regularPremium2);

            tablePolicyDetails3.addCell(cell_sumAssured1);
            tablePolicyDetails3.addCell(cell_sumAssured2);

            document.add(tablePolicyDetails3);

            PdfPTable tablePolicyDetails2 = new PdfPTable(4);
            tablePolicyDetails2.setWidthPercentage(100);

            PdfPCell cell_mode1 = new PdfPCell(new Paragraph("SAMF",
                    small_normal));
            cell_mode1.setPadding(5);
            PdfPCell cell_mode2 = new PdfPCell(new Paragraph(samf, small_bold));
            cell_mode2.setPadding(5);
            cell_mode2.setHorizontalAlignment(Element.ALIGN_CENTER);

            PdfPCell cell_firstYearPremium1 = new PdfPCell(new Paragraph(
                    "Option", small_normal));
            cell_firstYearPremium1.setPadding(5);
            PdfPCell cell_firstYearPremium2 = new PdfPCell(new Paragraph("  "
                    + plan, small_bold));
            cell_firstYearPremium2.setPadding(5);
            cell_firstYearPremium2.setHorizontalAlignment(Element.ALIGN_CENTER);

            tablePolicyDetails2.addCell(cell_mode1);
            tablePolicyDetails2.addCell(cell_mode2);

            tablePolicyDetails2.addCell(cell_firstYearPremium1);
            tablePolicyDetails2.addCell(cell_firstYearPremium2);

            document.add(tablePolicyDetails2);
            document.add(para_img_logo_after_space_1);

            String GrossYieldOfShadowAccount = "8%";

            PdfPTable BI_PdftableGrossYieldOfShadowAccount = new PdfPTable(2);
            BI_PdftableGrossYieldOfShadowAccount.setWidthPercentage(100);

            PdfPCell BI_PdftableGrossYieldOfShadowAccount_cell1 = new PdfPCell(
                    new Paragraph("Gross Yield of Shadow Account", small_normal));
            PdfPCell BI_PdftableGrossYieldOfShadowAccount_cell2 = new PdfPCell(
                    new Paragraph(GrossYieldOfShadowAccount, small_normal));

            BI_PdftableGrossYieldOfShadowAccount_cell1
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableGrossYieldOfShadowAccount_cell1.setPadding(5);

            BI_PdftableGrossYieldOfShadowAccount_cell2
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableGrossYieldOfShadowAccount_cell2.setPadding(5);

            BI_PdftableGrossYieldOfShadowAccount
                    .addCell(BI_PdftableGrossYieldOfShadowAccount_cell1);
            BI_PdftableGrossYieldOfShadowAccount
                    .addCell(BI_PdftableGrossYieldOfShadowAccount_cell2);
            document.add(BI_PdftableGrossYieldOfShadowAccount);

            PdfPTable BI_PdftableNetYieldAtMaturity = new PdfPTable(2);
            BI_PdftableNetYieldAtMaturity.setWidthPercentage(100);

            PdfPCell BI_PdftableNetYieldAtMaturity_cell1 = new PdfPCell(
                    new Paragraph("Net Yield at Maturity", small_normal));
            PdfPCell BI_PdftableNetYieldAtMaturity_cell2 = new PdfPCell(
                    new Paragraph(netYield8Pr + " %", small_normal));

            BI_PdftableNetYieldAtMaturity_cell1
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableNetYieldAtMaturity_cell1.setPadding(5);

            BI_PdftableNetYieldAtMaturity_cell2
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableNetYieldAtMaturity_cell2.setPadding(5);

            BI_PdftableNetYieldAtMaturity
                    .addCell(BI_PdftableNetYieldAtMaturity_cell1);
            BI_PdftableNetYieldAtMaturity
                    .addCell(BI_PdftableNetYieldAtMaturity_cell2);
            document.add(BI_PdftableNetYieldAtMaturity);

            PdfPTable BI_PdftableReductionYieldAtMaturity = new PdfPTable(2);
            BI_PdftableReductionYieldAtMaturity.setWidthPercentage(100);

            PdfPCell BI_PdftableReductionYieldAtMaturity_cell1 = new PdfPCell(
                    new Paragraph("Reduction in Yield at Maturity",
                            small_normal));
            PdfPCell BI_PdftableReductionYieldAtMaturity_cell2 = new PdfPCell(
                    new Paragraph(redInYieldMat + " %", small_normal));

            BI_PdftableReductionYieldAtMaturity_cell1
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableReductionYieldAtMaturity_cell1.setPadding(5);

            BI_PdftableReductionYieldAtMaturity_cell2
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableReductionYieldAtMaturity_cell2.setPadding(5);

            BI_PdftableReductionYieldAtMaturity
                    .addCell(BI_PdftableReductionYieldAtMaturity_cell1);
            BI_PdftableReductionYieldAtMaturity
                    .addCell(BI_PdftableReductionYieldAtMaturity_cell2);
            document.add(BI_PdftableReductionYieldAtMaturity);

            document.newPage();

            PdfPTable BI_PdftableNoofyearselapsedsinceinception = new PdfPTable(
                    4);
            BI_PdftableNoofyearselapsedsinceinception.setWidthPercentage(100);

            PdfPCell BI_PdftableBI_PdftableNoofyearselapsedsinceinception_cell1 = new PdfPCell(
                    new Paragraph(
                            "Maturity Benefit payable at the end of the term of the policy considering gross yield of shadow policy account @ 4% is ",
                            small_normal));

            CommonForAllProd obj = new CommonForAllProd();

            PdfPCell BI_PdftableBI_PdftableNoofyearselapsedsinceinception_cell2 = new PdfPCell(
                    new Paragraph(
                            " Rs.  "
                                    + obj.getRound(obj.getStringWithout_E(Double
                                    .valueOf((MatBenefit6.equals("") || MatBenefit6 == null) ? "0"
                                            : MatBenefit6)))));

            PdfPCell BI_PdftableBI_PdftableNoofyearselapsedsinceinception_cell3 = new PdfPCell(
                    new Paragraph(
                            "Maturity Benefit payable at the end of the term of the policy considering gross yield of shadow policy account @ 8% is ",
                            small_normal));

            PdfPCell BI_PdftableBI_PdftableNoofyearselapsedsinceinception_cell4 = new PdfPCell(
                    new Paragraph(
                            " Rs.  "
                                    + obj.getRound(obj.getStringWithout_E(Double
                                    .valueOf((MatBenefit10.equals("") || MatBenefit10 == null) ? "0"
                                            : MatBenefit10)))));

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
            document.add(BI_PdftableNoofyearselapsedsinceinception);

            document.add(para_img_logo_after_space_1);

            PdfPTable BI_PdftableOutputHeader = new PdfPTable(4);

            BI_PdftableOutputHeader.setWidthPercentage(100);
            float[] columnWidths = {6f, 10f, 10f, 1f};
            BI_PdftableOutputHeader.setWidths(columnWidths);
            PdfPCell BI_PdftableBI_PdftableOutputHeader_cell1 = new PdfPCell(
                    new Paragraph("", small_normal));
            PdfPCell BI_PdftableBI_PdftableOutputHeader_cell2 = new PdfPCell(
                    new Paragraph(
                            "Assuming gross interest rate of 4% pa in shadow policy account",
                            small_bold2));

            PdfPCell BI_PdftableBI_PdftableOutputHeader_cell3 = new PdfPCell(
                    new Paragraph(
                            "Assuming gross interest rate of 8% pa in shadow policy account",
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

            PdfPTable BI_Pdftablecolumnno = new PdfPTable(27);
            BI_Pdftablecolumnno.setWidthPercentage(100);

            PdfPCell BI_Pdftable_columnno_cell1 = new PdfPCell(new Paragraph(
                    "1", small_bold2));
            // PdfPCell BI_Pdftable_columnno_cell2 = new PdfPCell(new Paragraph(
            // "Policy in force", small_bold2));

            PdfPCell BI_Pdftable_columnno_cell3 = new PdfPCell(new Paragraph(
                    "2", small_bold2));

            // PdfPCell BI_Pdftable_columnno_cell4 = new PdfPCell(new Paragraph(
            // "Top-up Premium", small_bold2));
            PdfPCell BI_Pdftable_columnno_cell5 = new PdfPCell(new Paragraph(
                    "3", small_bold2));

            PdfPCell BI_Pdftable_columnno_cell6 = new PdfPCell(new Paragraph(
                    "4", small_bold2));

            PdfPCell BI_Pdftable_columnno_cell7 = new PdfPCell(new Paragraph(
                    "5", small_bold2));
            PdfPCell BI_Pdftable_columnno_cell8 = new PdfPCell(new Paragraph(
                    "6", small_bold2));

            PdfPCell BI_Pdftable_columnno_cell9 = new PdfPCell(new Paragraph(
                    "7", small_bold2));

            PdfPCell BI_Pdftable_columnno_cell10 = new PdfPCell(new Paragraph(
                    "8", small_bold2));

            PdfPCell BI_Pdftable_columnno_cell11 = new PdfPCell(new Paragraph(
                    "9", small_bold2));

            PdfPCell BI_Pdftable_columnno_cell12 = new PdfPCell(new Paragraph(
                    "10", small_bold2));
            PdfPCell BI_Pdftable_columnno_cell13 = new PdfPCell(new Paragraph(
                    "11", small_bold2));

            PdfPCell BI_Pdftable_columnno_cell14 = new PdfPCell(new Paragraph(
                    "12 ", small_bold2));

            PdfPCell BI_Pdftable_columnno_cell15 = new PdfPCell(new Paragraph(
                    "13", small_bold2));
            PdfPCell BI_Pdftable_columnno_cell16 = new PdfPCell(new Paragraph(
                    "14", small_bold2));

            PdfPCell BI_Pdftable_columnno_cell17 = new PdfPCell(new Paragraph(
                    "15", small_bold2));

            PdfPCell BI_Pdftable_columnno_cell18 = new PdfPCell(new Paragraph(
                    "16", small_bold2));

            PdfPCell BI_Pdftable_columnno_cell19 = new PdfPCell(new Paragraph(
                    "17", small_bold2));

            PdfPCell BI_Pdftable_columnno_cell20 = new PdfPCell(new Paragraph(
                    "18", small_bold2));
            PdfPCell BI_Pdftable_columnno_cell21 = new PdfPCell(new Paragraph(
                    "19", small_bold2));

            PdfPCell BI_Pdftable_columnno_cell22 = new PdfPCell(new Paragraph(
                    "20", small_bold2));
            PdfPCell BI_Pdftable_columnno_cell23 = new PdfPCell(new Paragraph(
                    "21", small_bold2));
            PdfPCell BI_Pdftable_columnno_cell24 = new PdfPCell(new Paragraph(
                    "22", small_bold2));
            PdfPCell BI_Pdftable_columnno_cell25 = new PdfPCell(new Paragraph(
                    "23", small_bold2));
            PdfPCell BI_Pdftable_columnno_cell26 = new PdfPCell(new Paragraph(
                    "24", small_bold2));
            PdfPCell BI_Pdftable_columnno_cell27 = new PdfPCell(new Paragraph(
                    "25", small_bold2));

            PdfPCell BI_Pdftable_columnno_cell28 = new PdfPCell(new Paragraph(
                    "26", small_bold2));
            PdfPCell BI_Pdftable_columnno_cell29 = new PdfPCell(new Paragraph(
                    "27", small_bold2));

            BI_Pdftablecolumnno.addCell(BI_Pdftable_columnno_cell1);
            BI_Pdftablecolumnno.addCell(BI_Pdftable_columnno_cell3);

            BI_Pdftablecolumnno.addCell(BI_Pdftable_columnno_cell5);
            BI_Pdftablecolumnno.addCell(BI_Pdftable_columnno_cell6);

            BI_Pdftablecolumnno.addCell(BI_Pdftable_columnno_cell7);
            BI_Pdftablecolumnno.addCell(BI_Pdftable_columnno_cell8);
            BI_Pdftablecolumnno.addCell(BI_Pdftable_columnno_cell9);

            BI_Pdftablecolumnno.addCell(BI_Pdftable_columnno_cell10);
            BI_Pdftablecolumnno.addCell(BI_Pdftable_columnno_cell11);
            BI_Pdftablecolumnno.addCell(BI_Pdftable_columnno_cell12);

            BI_Pdftablecolumnno.addCell(BI_Pdftable_columnno_cell13);
            BI_Pdftablecolumnno.addCell(BI_Pdftable_columnno_cell14);
            BI_Pdftablecolumnno.addCell(BI_Pdftable_columnno_cell15);

            BI_Pdftablecolumnno.addCell(BI_Pdftable_columnno_cell16);
            BI_Pdftablecolumnno.addCell(BI_Pdftable_columnno_cell17);
            BI_Pdftablecolumnno.addCell(BI_Pdftable_columnno_cell18);

            BI_Pdftablecolumnno.addCell(BI_Pdftable_columnno_cell19);
            BI_Pdftablecolumnno.addCell(BI_Pdftable_columnno_cell20);
            BI_Pdftablecolumnno.addCell(BI_Pdftable_columnno_cell21);
            BI_Pdftablecolumnno.addCell(BI_Pdftable_columnno_cell22);
            BI_Pdftablecolumnno.addCell(BI_Pdftable_columnno_cell23);
            BI_Pdftablecolumnno.addCell(BI_Pdftable_columnno_cell24);

            BI_Pdftablecolumnno.addCell(BI_Pdftable_columnno_cell25);
            BI_Pdftablecolumnno.addCell(BI_Pdftable_columnno_cell26);
            BI_Pdftablecolumnno.addCell(BI_Pdftable_columnno_cell27);
            BI_Pdftablecolumnno.addCell(BI_Pdftable_columnno_cell28);
            BI_Pdftablecolumnno.addCell(BI_Pdftable_columnno_cell29);
            document.add(BI_Pdftablecolumnno);

            PdfPTable BI_Pdftableoutput = new PdfPTable(27);
            BI_Pdftableoutput.setWidthPercentage(100);

            PdfPCell BI_Pdftable_output_cell1 = new PdfPCell(new Paragraph(
                    "Policy Year", small_bold2));
            // PdfPCell BI_Pdftable_output_cell2 = new PdfPCell(new Paragraph(
            // "Policy in force", small_bold2));

            PdfPCell BI_Pdftable_output_cell3 = new PdfPCell(new Paragraph(
                    "Premium", small_bold2));

            // PdfPCell BI_Pdftable_output_cell4 = new PdfPCell(new Paragraph(
            // "Top-up Premium", small_bold2));
            PdfPCell BI_Pdftable_output_cell5 = new PdfPCell(new Paragraph(
                    "Premium Allocation Charge", small_bold2));

            PdfPCell BI_Pdftable_output_cell6 = new PdfPCell(new Paragraph(
                    "Policy Administration Charge", small_bold2));

            PdfPCell BI_Pdftable_output_cell7 = new PdfPCell(new Paragraph(
                    "Applicable Taxes (if any) ", small_bold2));
            PdfPCell BI_Pdftable_output_cell8 = new PdfPCell(new Paragraph(
                    "Total deductions", small_bold2));

            PdfPCell BI_Pdftable_output_cell9 = new PdfPCell(new Paragraph(
                    "Opening Balance", small_bold2));

            PdfPCell BI_Pdftable_output_cell10 = new PdfPCell(new Paragraph(
                    "Mortality Charges", small_bold2));

            PdfPCell BI_Pdftable_output_cell11 = new PdfPCell(new Paragraph(
                    "Applicable Taxes(if any) on Mortality Charges", small_bold2));

            PdfPCell BI_Pdftable_output_cell12 = new PdfPCell(new Paragraph(
                    "Guaranteed addition to policy account", small_bold2));
            PdfPCell BI_Pdftable_output_cell13 = new PdfPCell(new Paragraph(
                    "Extra addition to policy account", small_bold2));

            PdfPCell BI_Pdftable_output_cell14 = new PdfPCell(new Paragraph(
                    "FMC ", small_bold2));

            PdfPCell BI_Pdftable_output_cell15 = new PdfPCell(new Paragraph(
                    "Applicable Taxes(if any) on FMC", small_bold2));
            PdfPCell BI_Pdftable_output_cell16 = new PdfPCell(new Paragraph(
                    "Closing Balance", small_bold2));

            PdfPCell BI_Pdftable_output_cell17 = new PdfPCell(new Paragraph(
                    "Surrender Value*", small_bold2));

            PdfPCell BI_Pdftable_output_cell18 = new PdfPCell(new Paragraph(
                    "Death Benefit", small_bold2));

            PdfPCell BI_Pdftable_output_cell19 = new PdfPCell(new Paragraph(
                    "Opening balance", small_bold2));

            PdfPCell BI_Pdftable_output_cell20 = new PdfPCell(new Paragraph(
                    "Mortality Charges", small_bold2));
            PdfPCell BI_Pdftable_output_cell21 = new PdfPCell(new Paragraph(
                    "Applicable Taxes(if any) on Mortality Charges", small_bold2));

            PdfPCell BI_Pdftable_output_cell22 = new PdfPCell(new Paragraph(
                    "Guaranteed addition to policy account", small_bold2));
            PdfPCell BI_Pdftable_output_cell23 = new PdfPCell(new Paragraph(
                    "Extra Addition to policy account", small_bold2));
            PdfPCell BI_Pdftable_output_cell24 = new PdfPCell(new Paragraph(
                    "FMC ", small_bold2));
            PdfPCell BI_Pdftable_output_cell25 = new PdfPCell(new Paragraph(
                    "Applicable Taxes(if any) on FMC", small_bold2));
            PdfPCell BI_Pdftable_output_cell26 = new PdfPCell(new Paragraph(
                    "Closing balance", small_bold2));
            PdfPCell BI_Pdftable_output_cell27 = new PdfPCell(new Paragraph(
                    "Surrender Value*", small_bold2));

            PdfPCell BI_Pdftable_output_cell28 = new PdfPCell(new Paragraph(
                    "Death Benefit", small_bold2));
            PdfPCell BI_Pdftable_output_cell29 = new PdfPCell(new Paragraph(
                    "Commission", small_bold2));

            BI_Pdftableoutput.addCell(BI_Pdftable_output_cell1);
            BI_Pdftableoutput.addCell(BI_Pdftable_output_cell3);

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

            BI_Pdftableoutput.addCell(BI_Pdftable_output_cell25);
            BI_Pdftableoutput.addCell(BI_Pdftable_output_cell26);
            BI_Pdftableoutput.addCell(BI_Pdftable_output_cell27);
            BI_Pdftableoutput.addCell(BI_Pdftable_output_cell28);
            BI_Pdftableoutput.addCell(BI_Pdftable_output_cell29);
            document.add(BI_Pdftableoutput);

            for (int i = 0; i < Integer.parseInt(policy_term); i++) {

                PdfPTable BI_Pdftableoutput_row1 = new PdfPTable(27);
                BI_Pdftableoutput_row1.setWidthPercentage(100);

                PdfPCell BI_Pdftable_output_row1_cell1 = new PdfPCell(
                        new Paragraph(list_data.get(i).getPolicy_year(),
                                small_bold2));
                // PdfPCell BI_Pdftable_output_row1_cell2 = new PdfPCell(
                // new Paragraph(list_data.get(i).getPolicy_In_Force(),
                // small_bold2));
                PdfPCell BI_Pdftable_output_row1_cell3 = new PdfPCell(
                        new Paragraph(list_data.get(i).getPremium(),
                                small_bold2));

                // PdfPCell BI_Pdftable_output_row1_cell4 = new PdfPCell(
                // new Paragraph(list_data.get(i).getTopup_premium(),
                // small_bold2));

                PdfPCell BI_Pdftable_output_row1_cell5 = new PdfPCell(
                        new Paragraph(list_data.get(i)
                                .getPremium_allocation_charge(), small_bold2));
                PdfPCell BI_Pdftable_output_row1_cell6 = new PdfPCell(
                        new Paragraph(list_data.get(i)
                                .getPolicy_administration_charge(), small_bold2));

                PdfPCell BI_Pdftable_output_row1_cell7 = new PdfPCell(
                        new Paragraph(list_data.get(i).getService_tax(),
                                small_bold2));

                PdfPCell BI_Pdftable_output_row1_cell8 = new PdfPCell(
                        new Paragraph(list_data.get(i).getTotal_deduction(),
                                small_bold2));
                PdfPCell BI_Pdftable_output_row1_cell9 = new PdfPCell(
                        new Paragraph(list_data.get(i).getOpening_balance1(),
                                small_bold2));

                PdfPCell BI_Pdftable_output_row1_cell10 = new PdfPCell(
                        new Paragraph(list_data.get(i).getMortality_charge1(),
                                small_bold2));

                PdfPCell BI_Pdftable_output_row1_cell11 = new PdfPCell(
                        new Paragraph(list_data.get(i)
                                .getService_tax_on_mortality_charge1(),
                                small_bold2));
                PdfPCell BI_Pdftable_output_row1_cell12 = new PdfPCell(
                        new Paragraph(
                                list_data.get(i).getGuranteed_addition1(),
                                small_bold2));

                PdfPCell BI_Pdftable_output_row1_cell13 = new PdfPCell(
                        new Paragraph(list_data.get(i).getExtra_addition1(),
                                small_bold2));

                PdfPCell BI_Pdftable_output_row1_cell14 = new PdfPCell(
                        new Paragraph(list_data.get(i).getFmc1(), small_bold2));
                PdfPCell BI_Pdftable_output_row1_cell15 = new PdfPCell(
                        new Paragraph(list_data.get(i)
                                .getSservice_tax_on_fmc1(), small_bold2));

                PdfPCell BI_Pdftable_output_row1_cell16 = new PdfPCell(
                        new Paragraph(list_data.get(i).getClosing_balance1(),
                                small_bold2));

                PdfPCell BI_Pdftable_output_row1_cell17 = new PdfPCell(
                        new Paragraph(list_data.get(i).getSurrender_value1(),
                                small_bold2));
                PdfPCell BI_Pdftable_output_row1_cell18 = new PdfPCell(
                        new Paragraph(list_data.get(i).getDeath_benefit1(),
                                small_bold2));

                PdfPCell BI_Pdftable_output_row1_cell19 = new PdfPCell(
                        new Paragraph(list_data.get(i).getOpening_balance2(),
                                small_bold2));

                PdfPCell BI_Pdftable_output_row1_cell20 = new PdfPCell(
                        new Paragraph(list_data.get(i).getMortality_charge2(),
                                small_bold2));

                PdfPCell BI_Pdftable_output_row1_cell21 = new PdfPCell(
                        new Paragraph(list_data.get(i)
                                .getService_tax_on_mortality_charge2(),
                                small_bold2));

                PdfPCell BI_Pdftable_output_row1_cell22 = new PdfPCell(
                        new Paragraph(
                                list_data.get(i).getGuranteed_addition2(),
                                small_bold2));
                PdfPCell BI_Pdftable_output_row1_cell23 = new PdfPCell(
                        new Paragraph(list_data.get(i).getExtra_addition2(),
                                small_bold2));

                PdfPCell BI_Pdftable_output_row1_cell24 = new PdfPCell(
                        new Paragraph(list_data.get(i).getFmc2(), small_bold2));
                PdfPCell BI_Pdftable_output_row1_cell25 = new PdfPCell(
                        new Paragraph(list_data.get(i)
                                .getSservice_tax_on_fmc2(), small_bold2));

                PdfPCell BI_Pdftable_output_row1_cell26 = new PdfPCell(
                        new Paragraph(list_data.get(i).getClosing_balance2(),
                                small_bold2));
                PdfPCell BI_Pdftable_output_row1_cell27 = new PdfPCell(
                        new Paragraph(list_data.get(i).getSurrender_value2(),
                                small_bold2));
                PdfPCell BI_Pdftable_output_row1_cell28 = new PdfPCell(
                        new Paragraph(list_data.get(i).getDeath_benefit2(),
                                small_bold2));
                PdfPCell BI_Pdftable_output_row1_cell29 = new PdfPCell(
                        new Paragraph(list_data.get(i).getCommission(),
                                small_bold2));

                BI_Pdftableoutput_row1.addCell(BI_Pdftable_output_row1_cell1);
                BI_Pdftableoutput_row1.addCell(BI_Pdftable_output_row1_cell3);

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
                BI_Pdftableoutput_row1.addCell(BI_Pdftable_output_row1_cell25);
                BI_Pdftableoutput_row1.addCell(BI_Pdftable_output_row1_cell26);
                BI_Pdftableoutput_row1.addCell(BI_Pdftable_output_row1_cell27);
                BI_Pdftableoutput_row1.addCell(BI_Pdftable_output_row1_cell28);
                BI_Pdftableoutput_row1.addCell(BI_Pdftable_output_row1_cell29);

                document.add(BI_Pdftableoutput_row1);

            }

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
                            "1) SBI Life - Flexi Smart Plus is an Individual, participating variable insurance plan.",
                            small_normal));

            BI_Pdftable6_cell6.setPadding(5);

            BI_Pdftable6.addCell(BI_Pdftable6_cell6);
            document.add(BI_Pdftable6);

            PdfPTable BI_Pdftable7 = new PdfPTable(1);
            BI_Pdftable7.setWidthPercentage(100);
            PdfPCell BI_Pdftable7_cell1 = new PdfPCell(
                    new Paragraph(
                            "2) Kindly note that above is only an illustration and does not in any way create any rights and/or obligations. The actual experience on the contract may be different from what is illustrated.The non-guaranteed low and high rate mentioned above relate to assumed investment returns at different rates and may vary depending upon market conditions. For more details on risk factors, terms and conditions please read sales brochure carefully before concluding a sale.",
                            small_normal));

            BI_Pdftable7_cell1.setPadding(5);

            BI_Pdftable7.addCell(BI_Pdftable7_cell1);
            document.add(BI_Pdftable7);

            PdfPTable BI_Pdftable8 = new PdfPTable(1);
            BI_Pdftable8.setWidthPercentage(100);
            PdfPCell BI_Pdftable8_cell1 = new PdfPCell(
                    new Paragraph(
                            "3) Please read this benefit illustration in conjunction with the Policy Document to understand all Terms, Conditions & Exclusions carefully.",
                            small_normal));

            BI_Pdftable8_cell1.setPadding(5);

            BI_Pdftable8.addCell(BI_Pdftable8_cell1);
            document.add(BI_Pdftable8);

            PdfPTable BI_Pdftable9 = new PdfPTable(1);
            BI_Pdftable9.setWidthPercentage(100);
            PdfPCell BI_Pdftable9_cell1 = new PdfPCell(
                    new Paragraph(
                            "4) The above BI is subject to payment of stipulated premiums on due date.",
                            small_normal));

            BI_Pdftable9_cell1.setPadding(5);

            BI_Pdftable9.addCell(BI_Pdftable9_cell1);
            document.add(BI_Pdftable9);

            PdfPTable BI_Pdftable10 = new PdfPTable(1);
            BI_Pdftable10.setWidthPercentage(100);
            PdfPCell BI_Pdftable10_cell1 = new PdfPCell(
                    new Paragraph(
                            "5) Surrender value is available on or after 5th policy anniversary and will be paid as per the conditions mentioned in brochure. Year end surrender value as shown below is only for illustration purpose.*",
                            small_normal));

            BI_Pdftable10_cell1.setPadding(5);

            BI_Pdftable10.addCell(BI_Pdftable10_cell1);
            document.add(BI_Pdftable10);

            PdfPTable BI_Pdftable11 = new PdfPTable(1);
            BI_Pdftable11.setWidthPercentage(100);
            PdfPCell BI_Pdftable11_cell1 = new PdfPCell(new Paragraph(
                    "6) The interest rates of 4% and 8 % are gross rates.",
                    small_normal));

            BI_Pdftable11_cell1.setPadding(5);

            BI_Pdftable11.addCell(BI_Pdftable11_cell1);
            document.add(BI_Pdftable11);

            PdfPTable BI_Pdftable12 = new PdfPTable(1);
            BI_Pdftable12.setWidthPercentage(100);
            PdfPCell BI_Pdftable12_cell1 = new PdfPCell(
                    new Paragraph(
                            "7) Acceptance of proposal is subject to Underwriting decision. Mortality Charge is for a healthy person.",
                            small_normal));

            BI_Pdftable12_cell1.setPadding(5);

            BI_Pdftable12.addCell(BI_Pdftable12_cell1);
            document.add(BI_Pdftable12);

            PdfPTable BI_Pdftable13 = new PdfPTable(1);
            BI_Pdftable13.setWidthPercentage(100);
            PdfPCell BI_Pdftable13_cell1 = new PdfPCell(
                    new Paragraph(
                            "8) Applicable Taxes (including surcharge/cess etc), at the rate notified by the Central Government/ State Government / Union Territories of India from time to time and as per the provisions of the prevalent tax laws will be payable on premium/ or any other charges as per the product features. ",

                            small_normal));

            BI_Pdftable13_cell1.setPadding(5);

            BI_Pdftable13.addCell(BI_Pdftable13_cell1);
            document.add(BI_Pdftable13);

            PdfPTable BI_Pdftable_TAX_FMC = new PdfPTable(1);
            BI_Pdftable_TAX_FMC.setWidthPercentage(100);
            PdfPCell BI_Pdftable_TAX_FMC_cell1 = new PdfPCell(
                    new Paragraph(
                            "9) This policy provides minimum bonus interest rate of 1% which is guaranteed for whole term.",

                            small_normal));

            BI_Pdftable_TAX_FMC_cell1.setPadding(5);

            BI_Pdftable_TAX_FMC.addCell(BI_Pdftable_TAX_FMC_cell1);
            document.add(BI_Pdftable_TAX_FMC);

            PdfPTable BI_Pdftable14 = new PdfPTable(1);
            BI_Pdftable14.setWidthPercentage(100);
            PdfPCell BI_Pdftable14_cell1 = new PdfPCell(
                    new Paragraph(
                            "10) At the beginning of each financial year, SBI Life will declare an interim bonus interest rate . The opening policy account value and interim bonus interest rate declared at the beginning of the financial year is the guaranteed floor for the policy account value during that financial year.At the end of each financial year, based on the actual  surplus arising,  SBI Life will declare additional regular bonus interest rate. This additional regular bonus interest rate declared at the end of the financial year will be equal to or more than the interim bonus interest rate. Such additional regular bonus interest rate will be applied on the fund balance every day on  pro-rata basis, to determine the total investment income for the year.",

                            small_normal));

            BI_Pdftable14_cell1.setPadding(5);

            BI_Pdftable14.addCell(BI_Pdftable14_cell1);
            document.add(BI_Pdftable14);

            PdfPTable BI_Pdftable15 = new PdfPTable(1);
            BI_Pdftable15.setWidthPercentage(100);
            PdfPCell BI_Pdftable15_cell1 = new PdfPCell(
                    new Paragraph(
                            "11) At the time of maturity, a terminal bonus interest rate may also be given, which is not guaranteed.",

                            small_normal));

            BI_Pdftable15_cell1.setPadding(5);

            BI_Pdftable15.addCell(BI_Pdftable15_cell1);
            document.add(BI_Pdftable15);

            PdfPTable BI_Pdftable16 = new PdfPTable(1);
            BI_Pdftable16.setWidthPercentage(100);
            PdfPCell BI_Pdftable16_cell1 = new PdfPCell(
                    new Paragraph(
                            "12) Future additional regular bonus interest rate is not guaranteed and will depend on the performance of participating fund of the Company.",

                            small_normal));

            BI_Pdftable16_cell1.setPadding(5);

            BI_Pdftable16.addCell(BI_Pdftable16_cell1);
            document.add(BI_Pdftable16);

            PdfPTable BI_Pdftable17 = new PdfPTable(1);
            BI_Pdftable17.setWidthPercentage(100);
            PdfPCell BI_Pdftable17_cell1 = new PdfPCell(
                    new Paragraph(
                            "13) Please note that additional bonus interest rate includes interim bonus interest rate and is over and above the minimum bonus interest rate.",

                            small_normal));

            BI_Pdftable17_cell1.setPadding(5);

            BI_Pdftable17.addCell(BI_Pdftable17_cell1);
            document.add(BI_Pdftable17);


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
                            "Premium allocation charge is the percentage of premium that would not be allocated to Policy Account.",
                            small_normal));

            BI_Pdftable20_cell1.setPadding(5);

            BI_Pdftable20.addCell(BI_Pdftable20_cell1);
            document.add(BI_Pdftable20);

            PdfPTable BI_Pdftable21 = new PdfPTable(1);
            BI_Pdftable21.setWidthPercentage(100);
            PdfPCell BI_Pdftable21_cell1 = new PdfPCell(
                    new Paragraph(
                            "Policy administration charges a charge of a fixed sum which is applied at the beginning of each policy month by deduction from policy account.",

                            small_normal));

            BI_Pdftable21_cell1.setPadding(5);

            BI_Pdftable21.addCell(BI_Pdftable21_cell1);
            document.add(BI_Pdftable21);

            PdfPTable BI_Pdftable22 = new PdfPTable(1);
            BI_Pdftable22.setWidthPercentage(100);
            PdfPCell BI_Pdftable22_cell1 = new PdfPCell(
                    new Paragraph(
                            "Fund management charge is the deduction made from the policy account at a stated percentage.",

                            small_normal));

            BI_Pdftable22_cell1.setPadding(5);

            BI_Pdftable22.addCell(BI_Pdftable22_cell1);
            document.add(BI_Pdftable22);

            PdfPTable BI_Pdftable23 = new PdfPTable(1);
            BI_Pdftable23.setWidthPercentage(100);
            PdfPCell BI_Pdftable23_cell1 = new PdfPCell(
                    new Paragraph(
                            "Mortality charges are the charges recovered for providing life insurance cover.",

                            small_normal));

            BI_Pdftable23_cell1.setPadding(5);

            BI_Pdftable23.addCell(BI_Pdftable23_cell1);
            document.add(BI_Pdftable23);

            document.add(para_img_logo_after_space_1);
            //Start 10 Jan 2018
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
            document.add(BI_Pdftable_CompanysPolicySurrender4);

            PdfPTable BI_Pdftable_CompanysPolicySurrender5 = new PdfPTable(1);
            BI_Pdftable_CompanysPolicySurrender5.setWidthPercentage(100);
            PdfPCell BI_Pdftable_CompanysPolicySurrender5_cell = new PdfPCell(
                    new Paragraph(Company_policy_surrender_dec, small_normal));

            BI_Pdftable_CompanysPolicySurrender5_cell.setPadding(5);

            BI_Pdftable_CompanysPolicySurrender5
                    .addCell(BI_Pdftable_CompanysPolicySurrender5_cell);
            document.add(BI_Pdftable_CompanysPolicySurrender5);

            //end 10 Jan 2018

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
                                    + "     have understood the term and conditions, product features and applicable charges (if any) before purchase of the contract, after receipt of all infromation stated above from the insurer.",
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
            Log.i(getLocalClassName(), e.toString() + "Error in creating pdf");
        }

    }


    private void getInput(FlexiSmartPlusBean flexiSmartBean) {
        inputVal = new StringBuilder();
        // From GUI Input

        String LifeAssured_title = spnr_bi_flexi_smart_life_assured_title
                .getSelectedItem().toString();
        String LifeAssured_firstName = edt_bi_flexi_smart_life_assured_first_name
                .getText().toString();
        String LifeAssured_middleName = edt_bi_flexi_smart_life_assured_middle_name
                .getText().toString();
        String LifeAssured_lastName = edt_bi_flexi_smart_life_assured_last_name
                .getText().toString();
        String LifeAssured_DOB = btn_bi_flexi_smart_life_assured_date.getText()
                .toString();
        String LifeAssured_age = ageInYears.getSelectedItem().toString();

        String proposer_title = "";
        String proposer_firstName = "";
        String proposer_middleName = "";
        String proposer_lastName = "";
        String proposer_DOB = "";
        String proposer_age = "";
        String proposer_gender = "";

        boolean staffDisc = flexiSmartBean.getIsForStaffOrNot();
        boolean isJKResident = flexiSmartBean.getJkResident();
        int policyTerm = flexiSmartBean.getPolicyTerm();
        String premFreq = flexiSmartBean.getPremFreqMode();
        double premiumAmount = flexiSmartBean.getPremiumAmount();
        double SAMF = flexiSmartBean.getSAMF();
        String isHoliday = flexiSmartBean.getPremiumHolidayStatus();
        int holidayYear = flexiSmartBean.getPolicyYear();
        String holidayTerm = flexiSmartBean.getPremiumHolidayStatus();
        String isTopup = flexiSmartBean.getTopUpStatus();
        double topupPremAmt = flexiSmartBean.getTopUpPremAmt();
        String plan = flexiSmartBean.getOption();

        inputVal.append("<?xml version='1.0' encoding='utf-8' ?><flexiSmartPlus>");

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
        inputVal.append("<isJKResident>").append(isJKResident).append("</isJKResident>");
        inputVal.append("<age>").append(ageInYears.getSelectedItem().toString()).append("</age>");

        inputVal.append("<policyTerm>").append(policyTerm).append("</policyTerm>");
        inputVal.append("<premFreq>").append(premFreq).append("</premFreq>");
        inputVal.append("<premiumAmount>").append(obj.getStringWithout_E(premiumAmount)).append("</premiumAmount>");
        inputVal.append("<SAMF>").append(SAMF).append("</SAMF>");
        inputVal.append("<isHoliday>").append(isHoliday).append("</isHoliday>");
        inputVal.append("<holidayYear>").append(holidayYear).append("</holidayYear>");
        inputVal.append("<holidayTerm>").append(holidayTerm).append("</holidayTerm>");
        inputVal.append("<isTopup>").append(isTopup).append("</isTopup>");
        inputVal.append("<topupPremAmt>").append(topupPremAmt).append("</topupPremAmt>");
        inputVal.append("<plan>").append(plan).append("</plan>");

        //Added Tushar Kadam Kerla Applicable Taxes 7-Jun-2019
        String str_kerla_discount = "N";
        if (cb_kerladisc.isChecked()) {
            str_kerla_discount = "Y";
        }

        inputVal.append("<KFC>" + str_kerla_discount + "</KFC>");
        //End Added Tushar Kadam Kerla Applicable Taxes 7-Jun-2019

        inputVal.append("</flexiSmartPlus>");
    }

    // Store user input in bean object
    private void addListenerOnSubmit() {
        flexiSmartBean = new FlexiSmartPlusBean();

        // System.out.println("* 1 *");
        flexiSmartBean.setIsForStaffOrNot(cb_StaffDisc.isChecked());

        /**
         * Change as per 1st jan 2014, by vrushali chaudhari
         */

        // System.out.println("* 2 *");
        // flexiSmartBean.setIsBancAssuranceDiscOrNot(cb_BankAssuranceDisc.isChecked());

        if (selJkResident.isChecked()) {
            flexiSmartBean.setJkResident(true);
        } else {
            flexiSmartBean.setJkResident(false);
        }

        if (cb_kerladisc.isChecked()) {
            flexiSmartBean.setKerlaDisc(true);
            flexiSmartBean.setServiceTax(true);
        } else {
            flexiSmartBean.setServiceTax(false);
            flexiSmartBean.setKerlaDisc(false);
        }

        // System.out.println("* 3 *");
        flexiSmartBean.setAgeAtEntry(Integer.parseInt(ageInYears
                .getSelectedItem().toString()));

        // System.out.println("* 4 *");
        flexiSmartBean.setGender(selGender.getSelectedItem().toString());

        // System.out.println("* 5 *");
        flexiSmartBean.setPolicyTerm(Integer.parseInt(selBasicTerm
                .getSelectedItem().toString()));

        // System.out.println("* 6 *");
        flexiSmartBean
                .setPremFreqMode(selPremFreq.getSelectedItem().toString());

        // System.out.println("* 7 *");
        flexiSmartBean.setPremiumAmount(Double.parseDouble((basicSA.getText()
                .toString() == "" ? "0.00" : basicSA.getText().toString())));

        // System.out.println("* 8 *");
        flexiSmartBean.setSAMF(Double.parseDouble(SAMF.getText().toString()));
        // Log.d("check","SAMF "+flexiSmartBean.getSAMF());
        flexiSmartBean.setOption((selOption.getSelectedItem().toString()));

        // System.out.println("* 9 *");
        if (selHoliday.isChecked())
            flexiSmartBean.setPremiumHolidayStatus("Yes");
        else
            flexiSmartBean.setPremiumHolidayStatus("No");
        // Log.d("check",String.valueOf(selHoliday.isChecked()));

        if (selHoliday.isChecked()) {
            // System.out.println("* 10 * in if");
            flexiSmartBean.setPolicyYear(Integer.parseInt(HolidayYear.getText()
                    .toString()));
            // System.out.println("* 11 * in if");
            flexiSmartBean.setPremHolidayTerm(Integer.parseInt(HolidayTerm
                    .getText().toString()));

            // Log.d("check","holiday policy yr "+
            // flexiSmartBean.getPolicyYear());
            // Log.d("check","holiday term set "+Integer.parseInt(HolidayTerm.getText().toString()));
            // Log.d("check","holiday term "+flexiSmartBean.getPremHolidayTerm());
        } else {
            // System.out.println("* 10 * in else");
            flexiSmartBean.setPolicyYear(0);
            // System.out.println("* 11 * in else");
            flexiSmartBean.setPremHolidayTerm(0);
            // Log.d("check","holiday policy yr"+
            // flexiSmartBean.getPolicyYear());
            // Log.d("check","holiday term"+flexiSmartBean.getPremHolidayTerm());
        }

        // System.out.println("* 12 *");
        if (selTopup.isChecked())
            flexiSmartBean.setTopUpStatus("Yes");
        else
            flexiSmartBean.setTopUpStatus("No");
        // flexiSmartBean.setTopUpStatus(String.valueOf(selTopup.isChecked()));

        // System.out.println("* 13 *");

        if (selTopup.isChecked())
            flexiSmartBean.setTopUpPremAmt(Integer.parseInt(TopupPremium
                    .getText().toString()));
        else
            flexiSmartBean.setTopUpPremAmt(0);

        if (selPremFreq.getSelectedItem().toString().equals("Yearly")) {
            flexiSmartBean.setPF(1);
        } else if (selPremFreq.getSelectedItem().toString()
                .equals("Half Yearly")) {
            flexiSmartBean.setPF(2);
        } else if (selPremFreq.getSelectedItem().toString().equals("Quarterly")) {
            flexiSmartBean.setPF(4);
        } else if (selPremFreq.getSelectedItem().toString().equals("Monthly")) {
            flexiSmartBean.setPF(12);
        }

        flexiSmartBean
                .setEffectivePremium(Double.parseDouble(effectivePremium));
        // Log.d("check","pf"+flexiSmartBean.getPF());

        showFlexiSmartOutputPg(flexiSmartBean);

    }

    /************************* Output starts from here ******************************************/

    private void showFlexiSmartOutputPg(FlexiSmartPlusBean flexiSmartBean) {
        retVal = new StringBuilder();
        // try
        // {
        String[] redinYieldArr = getOutputReductionInYield(
                "Benefit Illustrator_CAP", flexiSmartBean);
        String[] outputArr = getOutput("BI_Incl_Mort & Ser Tax", flexiSmartBean);

        retVal.append("<?xml version='1.0' encoding='utf-8' ?><flexiSmartPlus>");

        retVal.append("<errCode>0</errCode>" + "<maturityAge>").append(flexiSmartBean.getAgeAtEntry() + flexiSmartBean
                .getPolicyTerm()).append("</maturityAge>").append("<policyTerm>").append(flexiSmartBean.getPolicyTerm()).append("</policyTerm>").append("<AnnPrem>").append(flexiSmartBean.getPremiumAmount() * flexiSmartBean.getPF()).append("</AnnPrem>").append("<sumAssured>").append(Double.parseDouble(outputArr[0])).append("</sumAssured>").append("<MatBenefit6>").append(Double.parseDouble(outputArr[1])).append("</MatBenefit6>").append("<MatBenefit10>").append(Double.parseDouble(outputArr[2])).append("</MatBenefit10>").append("<redInYieldMat>").append(redinYieldArr[0]).append("</redInYieldMat>").append("<netYield8Pr>").append(redinYieldArr[1]).append("</netYield8Pr>");

        int index = flexiSmartBean.getPolicyTerm();
        String closingBalance4Pr = prsObj.parseXmlTag(bussIll.toString(), "closingBalance4Pr" + index + "");
        String closingBalance8Pr = prsObj.parseXmlTag(bussIll.toString(), "closingBalance8Pr" + index + "");

        retVal.append("<closingBalance4Pr" + index + ">" + closingBalance4Pr + "</closingBalance4Pr" + index + ">");
        retVal.append("<closingBalance8Pr" + index + ">" + closingBalance8Pr + "</closingBalance8Pr" + index + ">");

        retVal.append(bussIll.toString());

        retVal.append("</flexiSmartPlus>");
        System.out.println(retVal + "");

        // }
        // catch(Exception e)
        // {
        // retVal.append("<?xml version='1.0' encoding='utf-8' ?><smartElite>"
        // + "<errCode>1</errCode>" +
        // "<errorMessage>" + e.getMessage() + "</errorMessage></smartElite>");
        // }

        /******************************************** xml Output *************************************/

        System.out.println("Final output in xml" + retVal.toString());

        /******************************************** xml Output *************************************/

        // progressDialog.dismiss();

        // ArrayList<String[]> outputList;
        // String[] strArr=new String[3];
        // ArrayList<String> strList=new ArrayList<String>();
        // ArrayList<String[]> arrList=new ArrayList<String[]>();
        // String[]
        // outputArr=getOutput("BI_Incl_Mort & Ser Tax",flexiSmartBean);
        // String[]
        // outputArrReductionYield=getOutputReductionInYield("Benefit Illustrator_CAP",
        // flexiSmartBean);
        // arrList.add(outputArr);
        // arrList.add(outputArrReductionYield);
        //
        // outputList=arrList;
        //
        // // System.out.println("size "+outputList.size());
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
        // i.putExtra("op3","Reduction in Yield at Maturity is " +
        // (strList.get(3))+"%");
        // System.out.println("Done display");
        // i.putExtra("header", inputActivityHeader.getText().toString());
        // System.out.println("After display");
        //
        // progressDialog.dismiss();
        // startActivity(i);

    }

    /* public String[] getOutput(String sheetName,
                                FlexiSmartPlusBean flexiSmartBean) {

         bussIll = new StringBuilder();

         BI_CommonForAllProd commonForAllProd = new BI_CommonForAllProd();
         FlexiSmartPlusProperties prop = new FlexiSmartPlusProperties();
         // Intent intent = new Intent(getApplicationContext(),success.class);

         // Output Variable Declaration
         int _month_E = 0;
         int _year_F = 0;
         String _policyInForce_G = "Y";
         int _age_H = 0;
         int _premiumHolidayFlag_I = 0;

         double _premium_j = 0, _topUpPremium_K = 0, _premiumAllocationCharge_L = 0, _policyAdministrationCharge_M = 0, _serviceTax_N = 0, _totalDeduction_O = 0, _openingBalance_P = 0, _riskPremiumMortality_Q = 0, _serviceTax_R = 0, _guarateedAdditionToPolicyAccount_S = 0, _extraAdditionToPolicyAccount_T = 0, _FMC_U = 0, _serviceTaxOnFMC_V = 0, _closingBalance_W = 0, _surrenderCharges_X = 0, _surrenderValue_Y = 0, _deathBenefit_Z = 0, _openingBalance_AA = 0, _riskPremium_AB = 0, _serviceTax_AC = 0, _guarateedAdditionToPolicyAccount_AD = 0, _extraAdditionToPolicyAccount_AE = 0, _FMC_AF = 0, _serviceTaxOnFMC_AG = 0, _closingBalance_AH = 0, _surrenderCharges_AI = 0, _surrenderValue_AJ = 0, _deathBenefit_AK = 0, _reductionYield_AO;

         // Temp Variable Declaretion
         int month_E = 0;
         int year_F = 0;
         String policyInForce_G = "Y";
         int age_H = 0;
         int premiumHolidayFlag_I = 0;

         double premium_j = 0, topUpPremium_K = 0, premiumAllocationCharge_L = 0, policyAdministrationCharge_M = 0, serviceTax_N = 0, totalDeduction_O = 0, openingBalance_P = 0, riskPremiumMortality_Q = 0, serviceTax_R = 0, guarateedAdditionToPolicyAccount_S = 0, extraAdditionToPolicyAccount_T = 0, FMC_U = 0, serviceTaxOnFMC_V = 0, closingBalance_W = 0, surrenderCharges_X = 0, surrenderValue_Y = 0, deathBenefit_Z = 0, openingBalance_AA = 0, riskPremium_AB = 0, serviceTax_AC = 0, guarateedAdditionToPolicyAccount_AD = 0, extraAdditionToPolicyAccount_AE = 0, FMC_AF = 0, serviceTaxOnFMC_AG = 0, closingBalance_AH = 0, surrenderCharges_AI = 0, surrenderValue_AJ = 0, deathBenefit_AK = 0, reductionYield_AO = 0;

         double sum_J = 0, sum_K = 0, sum_L = 0, sum_M = 0, sum_N = 0, sum_O = 0, sum_P = 0, sum_Q = 0, sum_R = 0, sum_S = 0, sum_T = 0, sum_U = 0, sum_V = 0, sum_W = 0, sum_Y = 0, sum_Z = 0, sum_AA = 0, sum_AB = 0, sum_AC = 0, sum_AD = 0, sum_AE = 0, sum_AF = 0, sum_AG = 0, sum_AH = 0, sum_AJ = 0, sum_AK = 0;

         double _sum_J = 0, _sum_K = 0, _sum_L = 0, _sum_M = 0, _sum_N = 0, _sum_O = 0, _sum_P = 0, _sum_Q = 0, _sum_R = 0, _sum_S = 0, _sum_T = 0, _sum_U = 0, _sum_V = 0, _sum_W = 0, _sum_Y = 0, _sum_Z = 0, _sum_AA = 0, _sum_AB = 0, _sum_AC = 0, _sum_AD = 0, _sum_AE = 0, _sum_AF = 0, _sum_AG = 0, _sum_AH = 0, _sum_AJ = 0, _sum_AK = 0, Commission_AF;

         // From GUI Input
         int ageAtEntry = flexiSmartBean.getAgeAtEntry();
         flexiSmartBean.setSumAssured();
         flexiSmartBean.setEffectiveTopUpPrem(flexiSmartBean.getTopUpPremAmt(),
                 flexiSmartBean.getPremiumAmount());
         double effectiveTopUpPrem = flexiSmartBean.getEffectiveTopUpPrem();

         // Declaration of method Variables/Object required for calculation
         FlexiSmartPlusBusinessLogic BIMAST = new FlexiSmartPlusBusinessLogic();
         String[] forBIArray = BIMAST.getForBIArr(prop.riskPremiumRate);
         int policyTerm = flexiSmartBean.getPolicyTerm();
         boolean jkResident = flexiSmartBean.getJkResident();
         String option = flexiSmartBean.getOption();
         double sumAssured = flexiSmartBean.getSumAssured();

         double serviceTax = 0;
         boolean isKerlaDisc = flexiSmartBean.isKerlaDisc();
         double premiumAmt = flexiSmartBean.getPremiumAmount();
         double sumPremium = 0;
         int rowNumber = 0;
         bussIll.append("<staffDiscPercentage>"
                 + BIMAST.getStaffPercentage(flexiSmartBean.getIsForStaffOrNot())
                 + "</staffDiscPercentage>");

         bussIll.append("<staffDiscCode>"
                 + BIMAST.getStaffDiscCode(flexiSmartBean.getIsForStaffOrNot())
                 + "</staffDiscCode>");

         // for (int i = 1; i < 51; i++)
         for (int i = 1; i <= (policyTerm * 12); i++) {
             rowNumber++;
             // //System.out.println("********************************************* "+i+" Row Output *********************************************");
             BIMAST.setMonth_E(rowNumber);
             month_E = Integer.parseInt(BIMAST.getMonth_E());
             _month_E = month_E;
             System.out.println("1.   Month_E : " + month_E);

             BIMAST.setYear_F();
             year_F = Integer.parseInt(BIMAST.getYear_F());
             _year_F = year_F;
             System.out.println("2.   Year_F : " + year_F);

             if ((_month_E % 12) == 0) {

                 bussIll.append("<policyYr" + _year_F + ">" + _year_F
                         + "</policyYr" + _year_F + ">");

             }
             if (isKerlaDisc == true && _year_F <= 2) {
                 serviceTax =0.19;
             }else{
                 serviceTax =0.18;
             }
             policyInForce_G = BIMAST.getPolicyInForce_G();
             _policyInForce_G = BIMAST.getPolicyInForce_G();
             // System.out.println("3.   PolicyInForce_G : "+BIMAST.getPolicyInForce_G());

             BIMAST.setAge_H(ageAtEntry);
             age_H = Integer.parseInt(BIMAST.getAge_H());
             _age_H = age_H;
             // System.out.println("4.   Age_H : "+age_H);

             BIMAST.setPremiumHolidayFlag_I(
                     flexiSmartBean.getPremiumHolidayStatus(),
                     flexiSmartBean.getPolicyYear(),
                     flexiSmartBean.getPremHolidayTerm());
             premiumHolidayFlag_I = Integer.parseInt(BIMAST
                     .getPremiumHolidayFlag_I());
             _premiumHolidayFlag_I = premiumHolidayFlag_I;
             // System.out.println("5.   PremiumHolidayFlag_I : "+premiumHolidayFlag_I);

             BIMAST.setPremium_J(
                     flexiSmartBean.getPolicyTerm(),
                     flexiSmartBean.getPF(),
                     (flexiSmartBean.getPremiumAmount() * flexiSmartBean.getPF()));
             premium_j = Double.parseDouble(BIMAST.getPremium_J());
             _premium_j = premium_j;
             // System.out.println("6.   Premium_J : "+premium_j);

             sum_J += _premium_j;
             if ((_month_E % 12) == 0) {
                 bussIll.append("<AnnPrem" + _year_F + ">" + commonForAllProd.getStringWithout_E(Math.round(sum_J)) + "</AnnPrem" + _year_F + ">");
                 _sum_J = sum_J;
                 sum_J = 0;
             }

             BIMAST.setTopUpPremium_K(effectiveTopUpPrem, flexiSmartBean);
             topUpPremium_K = Double.parseDouble(BIMAST.getTopUpPremium_K());
             _topUpPremium_K = topUpPremium_K;
             // System.out.println("7.   TopUpPremium_K : "+topUpPremium_K);

             sum_K += _topUpPremium_K;
             if ((_month_E % 12) == 0) {

                 bussIll.append("<topUpPrem" + _year_F + ">" + Math.round(sum_K)
                         + "</topUpPrem" + _year_F + ">");
                 _sum_K = sum_K;
                 sum_K = 0;
             }

             BIMAST.setPremiumAllocationCharge_L(flexiSmartBean);
             premiumAllocationCharge_L = Double.parseDouble(BIMAST
                     .getPremiumAllocationCharge_L());
             _premiumAllocationCharge_L = premiumAllocationCharge_L;
             // System.out.println("8.   premiumAllocationCharge_L : "+premiumAllocationCharge_L);

             sum_L += _premiumAllocationCharge_L;
             if ((_month_E % 12) == 0) {

                 bussIll.append("<PremAllCharge" + _year_F + ">"
                         + Math.round(sum_L) + "</PremAllCharge" + _year_F + ">");
                 _sum_L = sum_L;
                 sum_L = 0;
             }

             BIMAST.setPolicyAdministartionCharge_M();
             policyAdministrationCharge_M = Double.parseDouble(BIMAST
                     .getPolicyAdministrationCharge_M());
             _policyAdministrationCharge_M = policyAdministrationCharge_M;
             // System.out.println("9.   policyAdministrationCharge_M : "+policyAdministrationCharge_M);

             sum_M += _policyAdministrationCharge_M;
             if ((_month_E % 12) == 0) {

                 bussIll.append("<PolicyAdmCharge" + _year_F + ">"
                         + Math.round(sum_M) + "</PolicyAdmCharge" + _year_F
                         + ">");
                 _sum_M = sum_M;
                 sum_M = 0;
             }

             BIMAST.setServiceTax_N(jkResident, flexiSmartBean,serviceTax);
             serviceTax_N = Double.parseDouble(BIMAST.getServiceTax_N());
             _serviceTax_N = serviceTax_N;
             // System.out.println("10.   serviceTax_N : "+serviceTax_N);

             sum_N += _serviceTax_N;
             if ((_month_E % 12) == 0) {

                 bussIll.append("<serviceTax" + _year_F + ">"
                         + Math.round(sum_N) + "</serviceTax" + _year_F + ">");
                 _sum_N = sum_N;
                 sum_N = 0;
             }

             BIMAST.setTotalDeductions_O();
             totalDeduction_O = Double
                     .parseDouble(BIMAST.getTotalDeductions_O());
             _totalDeduction_O = totalDeduction_O;
             // System.out.println("11.   totalDeduction_O : "+totalDeduction_O);

             sum_O += _totalDeduction_O;
             if ((_month_E % 12) == 0) {

                 bussIll.append("<totalDeduction" + _year_F + ">"
                         + Math.round(sum_O) + "</totalDeduction" + _year_F
                         + ">");
                 _sum_O = sum_O;
                 sum_O = 0;
             }

             BIMAST.setOpeningBalance_P(_closingBalance_W);
             openingBalance_P = Double.parseDouble(BIMAST.getOpeningBalance_P());
             _openingBalance_P = openingBalance_P;
             // System.out.println("12.   openingBalance_P : "+openingBalance_P);

             if ((_month_E % 12) == 0) {
                 sum_P = (_sum_J - _sum_L - _sum_M - _sum_N + _sum_W);
                 bussIll.append("<openBalance4Pr" + _year_F + ">"
                         + Math.round(sum_P) + "</openBalance4Pr" + _year_F
                         + ">");
                 _sum_P = sum_P;
                 sum_P = 0;

             }

             BIMAST.setRiskPremiumMortality_Q(flexiSmartBean, forBIArray,
                     option, sumAssured);
             riskPremiumMortality_Q = Double.parseDouble(BIMAST
                     .getRiskPremiumMortality_Q());
             _riskPremiumMortality_Q = riskPremiumMortality_Q;
             // System.out.println("13.   riskPremiumMortality_Q : "+riskPremiumMortality_Q);

             sum_Q += _riskPremiumMortality_Q;
             if ((_month_E % 12) == 0) {

                 bussIll.append("<MortChrg4Pr" + _year_F + ">"
                         + Math.round(sum_Q) + "</MortChrg4Pr" + _year_F + ">");
                 _sum_Q = sum_Q;
                 sum_Q = 0;
             }

             BIMAST.setServiceTax_R(flexiSmartBean,serviceTax);
             serviceTax_R = Double.parseDouble(BIMAST.getServiceTax_R());
             _serviceTax_R = serviceTax_R;
             // System.out.println("14.   serviceTax_R : "+serviceTax_R);

             sum_R += _serviceTax_R;
             if ((_month_E % 12) == 0) {

                 bussIll.append("<serTxonMortChrg4Pr" + _year_F + ">"
                         + Math.round(sum_R) + "</serTxonMortChrg4Pr" + _year_F
                         + ">");
                 _sum_R = sum_R;
                 sum_R = 0;
             }

             BIMAST.setGuarateedAdditionToPolicyAccount_S(flexiSmartBean);
             guarateedAdditionToPolicyAccount_S = Double.parseDouble(BIMAST
                     .getGuarateedAdditionToPolicyAccount_S());
             _guarateedAdditionToPolicyAccount_S = guarateedAdditionToPolicyAccount_S;
             // System.out.println("15.   guarateedAdditionToPolicyAccount_S : "+guarateedAdditionToPolicyAccount_S);

             sum_S += _guarateedAdditionToPolicyAccount_S;
             if ((_month_E % 12) == 0) {

                 bussIll.append("<GuareentedAdd4Pr" + _year_F + ">"
                         + Math.round(sum_S) + "</GuareentedAdd4Pr" + _year_F
                         + ">");
                 _sum_S = sum_S;
                 sum_S = 0;
             }

             BIMAST.setExtraAdditionToPolicyAccount_T(flexiSmartBean);
             extraAdditionToPolicyAccount_T = Double.parseDouble(BIMAST
                     .getExtraAdditionToPolicyAccount_T());
             _extraAdditionToPolicyAccount_T = extraAdditionToPolicyAccount_T;
             // System.out.println("16.   extraAdditionToPolicyAccount_T : "+extraAdditionToPolicyAccount_T);

             sum_T += _extraAdditionToPolicyAccount_T;
             if ((_month_E % 12) == 0) {

                 bussIll.append("<extraAdd4Pr" + _year_F + ">"
                         + Math.round(sum_T) + "</extraAdd4Pr" + _year_F + ">");
                 _sum_T = sum_T;
                 sum_T = 0;
             }

             BIMAST.setFMC_U();
             FMC_U = Double.parseDouble(BIMAST.getFMC_U());
             _FMC_U = FMC_U;
             // System.out.println("17.   FMC_U : "+FMC_U);

             sum_U += _FMC_U;
             if ((_month_E % 12) == 0) {

                 bussIll.append("<FMC4Pr" + _year_F + ">" + Math.round(sum_U)
                         + "</FMC4Pr" + _year_F + ">");
                 _sum_U = sum_U;
                 sum_U = 0;
             }

             BIMAST.setServiceTaxOnFMC_V(jkResident, flexiSmartBean,serviceTax);
             serviceTaxOnFMC_V = Double.parseDouble(BIMAST
                     .getServiceTaxOnFMC_V());
             _serviceTaxOnFMC_V = serviceTaxOnFMC_V;
             // System.out.println("18.   serviceTaxOnFMC_V : "+serviceTaxOnFMC_V);

             sum_V += _serviceTaxOnFMC_V;
             if ((_month_E % 12) == 0) {

                 bussIll.append("<serTxonFMC4Pr" + _year_F + ">"
                         + Math.round(sum_V) + "</serTxonFMC4Pr" + _year_F + ">");
                 _sum_V = sum_V;
                 sum_V = 0;
             }

             BIMAST.setClosingBalance_W();
             closingBalance_W = Double.parseDouble(BIMAST.getClosingBalance_W());
             _closingBalance_W = closingBalance_W;
             // System.out.println("19.   closingBalance_W : "+closingBalance_W);

             if ((_month_E % 12) == 0) {
                 sum_W = _closingBalance_W;
                 bussIll.append("<closingBalance4Pr" + _year_F + ">"
                         + Math.round(sum_W) + "</closingBalance4Pr" + _year_F
                         + ">");
                 _sum_W = sum_W;
                 sum_W = 0;
             }

             BIMAST.setSurrenderCharges_X(premiumAmt);
             surrenderCharges_X = Double.parseDouble(BIMAST
                     .getSurrenderCharges_X());
             _surrenderCharges_X = surrenderCharges_X;
             // System.out.println("20.   surrenderCharges_X : "+surrenderCharges_X);

             BIMAST.setSurrenderValue_Y(flexiSmartBean,serviceTax);
             surrenderValue_Y = Double.parseDouble(BIMAST.getSurrenderValue_Y());
             _surrenderValue_Y = surrenderValue_Y;
             // System.out.println("21.   surrenderValue_Y : "+surrenderValue_Y);

             if ((_month_E % 12) == 0) {
                 sum_Y = _surrenderValue_Y;
                 bussIll.append("<SurrenderVal4Pr" + _year_F + ">"
                         + Math.round(sum_Y) + "</SurrenderVal4Pr" + _year_F
                         + ">");
             }

             if (_month_E == 120) {
                 //				System.out.println("bussIll" +bussIll.toString());
             }
             sumPremium += _premium_j;
             //			System.out.println("sumPremium "+sumPremium);
             BIMAST.setDeathBenefit_Z(option, sumAssured, sumPremium);
             deathBenefit_Z = Double.parseDouble(BIMAST.getDeathBenefit_Z());
             _deathBenefit_Z = deathBenefit_Z;
             //			System.out.println("22.   deathBenefit_Z : "+deathBenefit_Z);

             if ((_month_E % 12) == 0) {
                 sum_Z = _deathBenefit_Z;
                 bussIll.append("<DeathBenefit4Pr" + _year_F + ">"
                         + Math.round(sum_Z) + "</DeathBenefit4Pr" + _year_F
                         + ">");
             }

             BIMAST.setOpeningBalance_AA(_closingBalance_AH);
             openingBalance_AA = Double.parseDouble(BIMAST
                     .getOpeningBalance_AA());
             _openingBalance_AA = openingBalance_AA;
             // System.out.println("23.   openingBalance_AA : "+openingBalance_AA);

             if ((_month_E % 12) == 0) {
                 sum_AA = (_sum_J - _sum_L - _sum_M - _sum_N + _sum_AH);
                 bussIll.append("<openBalance8Pr" + _year_F + ">"
                         + Math.round(sum_AA) + "</openBalance8Pr" + _year_F
                         + ">");
                 _sum_AA = sum_AA;
                 sum_AA = 0;

             }

             BIMAST.setRiskPremiumMortality_AB(flexiSmartBean, forBIArray,
                     option, sumAssured);
             riskPremium_AB = Double.parseDouble(BIMAST
                     .getRiskPremiumMortality_AB());
             _riskPremium_AB = riskPremium_AB;
             // System.out.println("24.   riskPremiumMortality_AB : "+riskPremium_AB);

             sum_AB += _riskPremium_AB;
             if ((_month_E % 12) == 0) {

                 bussIll.append("<MortChrg8Pr" + _year_F + ">"
                         + Math.round(sum_AB) + "</MortChrg8Pr" + _year_F + ">");
                 _sum_AB = sum_AB;
                 sum_AB = 0;
             }

             BIMAST.setServiceTax_AC(flexiSmartBean,serviceTax);
             serviceTax_AC = Double.parseDouble(BIMAST.getServiceTax_AC());
             _serviceTax_AC = serviceTax_AC;
             // System.out.println("25.   serviceTax_AC : "+serviceTax_AC);

             sum_AC += _serviceTax_AC;
             if ((_month_E % 12) == 0) {

                 bussIll.append("<serTxonMortChrg8Pr" + _year_F + ">"
                         + Math.round(sum_AC) + "</serTxonMortChrg8Pr" + _year_F
                         + ">");
                 _sum_AC = sum_AC;
                 sum_AC = 0;
             }

             BIMAST.setGuarateedAdditionToPolicyAccount_AD(flexiSmartBean);
             guarateedAdditionToPolicyAccount_AD = Double.parseDouble(BIMAST
                     .getGuarateedAdditionToPolicyAccount_AD());
             _guarateedAdditionToPolicyAccount_AD = guarateedAdditionToPolicyAccount_AD;
             // System.out.println("26.   guarateedAdditionToPolicyAccount_AD : "+guarateedAdditionToPolicyAccount_AD);

             sum_AD += _guarateedAdditionToPolicyAccount_AD;
             if ((_month_E % 12) == 0) {

                 bussIll.append("<GuareentedAdd8Pr" + _year_F + ">"
                         + Math.round(sum_AD) + "</GuareentedAdd8Pr" + _year_F
                         + ">");
                 _sum_AD = sum_AD;
                 sum_AD = 0;
             }

             BIMAST.setExtraAdditionToPolicyAccount_AE(flexiSmartBean);
             extraAdditionToPolicyAccount_AE = Double.parseDouble(BIMAST
                     .getExtraAdditionToPolicyAccount_AE());
             _extraAdditionToPolicyAccount_AE = extraAdditionToPolicyAccount_AE;
             // System.out.println("27.   extraAdditionToPolicyAccount_AE : "+extraAdditionToPolicyAccount_AE);

             sum_AE += _extraAdditionToPolicyAccount_AE;
             if ((_month_E % 12) == 0) {

                 bussIll.append("<extraAdd8Pr" + _year_F + ">"
                         + Math.round(sum_AE) + "</extraAdd8Pr" + _year_F + ">");
                 _sum_AE = sum_AE;
                 sum_AE = 0;
             }

             BIMAST.setFMC_AF();
             FMC_AF = Double.parseDouble(BIMAST.getFMC_AF());
             _FMC_AF = FMC_AF;
             // System.out.println("28.   FMC_AF : "+FMC_AF);

             sum_AF += _FMC_AF;
             if ((_month_E % 12) == 0) {

                 bussIll.append("<FMC8Pr" + _year_F + ">" + Math.round(sum_AF)
                         + "</FMC8Pr" + _year_F + ">");
                 _sum_AF = sum_AF;
                 sum_AF = 0;
             }

             BIMAST.setServiceTaxOnFMC_AG(jkResident, flexiSmartBean,serviceTax);
             serviceTaxOnFMC_AG = Double.parseDouble(BIMAST
                     .getServiceTaxOnFMC_AG());
             _serviceTaxOnFMC_AG = serviceTaxOnFMC_AG;
             // System.out.println("29.   serviceTaxOnFMC_AG : "+serviceTaxOnFMC_AG);

             sum_AG += _serviceTaxOnFMC_AG;
             if ((_month_E % 12) == 0) {

                 bussIll.append("<serTxonFMC8Pr" + _year_F + ">"
                         + Math.round(sum_AG) + "</serTxonFMC8Pr" + _year_F
                         + ">");
                 _sum_AG = sum_AG;
                 sum_AG = 0;
             }

             BIMAST.setClosingBalance_AH();
             closingBalance_AH = Double.parseDouble(BIMAST
                     .getClosingBalance_AH());
             _closingBalance_AH = closingBalance_AH;
             // System.out.println("30.   closingBalance_AH : "+closingBalance_AH);

             if ((_month_E % 12) == 0) {
                 sum_AH = _closingBalance_AH;
                 bussIll.append("<closingBalance8Pr" + _year_F + ">"
                         + Math.round(sum_AH) + "</closingBalance8Pr" + _year_F
                         + ">");
                 _sum_AH = sum_AH;
                 sum_AH = 0;
             }

             BIMAST.setSurrenderCharges_AI(premiumAmt);
             surrenderCharges_AI = Double.parseDouble(BIMAST
                     .getSurrenderCharges_AI());
             _surrenderCharges_AI = surrenderCharges_AI;
             // System.out.println("31.   surrenderCharges_AI : "+surrenderCharges_AI);

             BIMAST.setSurrenderValue_AJ(flexiSmartBean,serviceTax);
             surrenderValue_AJ = Double.parseDouble(BIMAST
                     .getSurrenderValue_AJ());
             _surrenderValue_AJ = surrenderValue_AJ;
             // System.out.println("32.   surrenderValue_AJ : "+surrenderValue_AJ);

             if ((_month_E % 12) == 0) {
                 sum_AJ = _surrenderValue_AJ;
                 bussIll.append("<SurrenderVal8Pr" + _year_F + ">"
                         + Math.round(sum_AJ) + "</SurrenderVal8Pr" + _year_F
                         + ">");

             }

             BIMAST.setDeathBenefit_AK(option, sumAssured, sumPremium);
             deathBenefit_AK = Double.parseDouble(BIMAST.getDeathBenefit_AK());
             _deathBenefit_AK = deathBenefit_AK;
             // System.out.println("33.   deathBenefit_AK : "+deathBenefit_AK);

             if ((_month_E % 12) == 0) {
                 sum_AK = _deathBenefit_AK;
                 bussIll.append("<DeathBenefit8Pr" + _year_F + ">"
                         + Math.round(sum_AK) + "</DeathBenefit8Pr" + _year_F
                         + ">");

             }

             if ((_month_E % 12) == 0) {
                 //				System.out.println("aetfef"+BIMAST.getCommission_AF(_sum_J,flexiSmartBean));
                 Commission_AF = BIMAST.getCommission_AF(_sum_J, flexiSmartBean);
                 bussIll.append("<CommIfPay8Pr" + _year_F + ">"
                         + Math.round(Commission_AF) + "</CommIfPay8Pr"
                         + _year_F + ">");
             }

             // //System.out.println("***********************************************************************************************************");
         }

         // System.out.println("****************************** Final Output ****************************************");
         // System.out.println("Sum Assured -> "+commonForAllProd.getStringWithout_E(flexiSmartBean.getSumAssured()));
         // System.out.println("Fund Value @6% is Rs.-> "+commonForAllProd.getRoundUp((commonForAllProd.getStringWithout_E(closingBalance_W))));
         // System.out.println("Fund Value @10% -> "+commonForAllProd.getRound((commonForAllProd.getStringWithout_E(closingBalance_AH))));
         // System.out.println("***********************************************************************************");
         //
         //		intent.putExtra("op","Sum Assured is Rs." + currencyFormat.format(Double.parseDouble(commonForAllProd.getStringWithout_E(flexiSmartBean.getSumAssured()))));
         //		intent.putExtra("op1","Maturity Benefit Payable @ 4% is Rs."+  currencyFormat.format(Double.parseDouble(commonForAllProd.getRoundUp((commonForAllProd.getStringWithout_E(closingBalance_W))))));
         //		intent.putExtra("op2","Maturity Benefit Payable @ 8% is Rs." +  currencyFormat.format(Double.parseDouble(commonForAllProd.getRoundUp((commonForAllProd.getStringWithout_E(closingBalance_AH))))));
         // startActivity(intent);

         return new String[]{
                 commonForAllProd.getStringWithout_E(flexiSmartBean
                         .getSumAssured()),
                 commonForAllProd.getRoundUp(commonForAllProd
                         .getStringWithout_E(closingBalance_W)),
                 commonForAllProd.getRoundUp(commonForAllProd
                         .getStringWithout_E(closingBalance_AH))};
     }
 */
    public String[] getOutput(String sheetName, FlexiSmartPlusBean flexiSmartBean) {

        CommonForAllProd commonForAllProd = new CommonForAllProd();
        FlexiSmartPlusProperties prop = new FlexiSmartPlusProperties();
        //		Intent intent = new Intent(getApplicationContext(),success.class);

        //Output Variable Declaration
        int _month_E = 0;
        int _year_F = 0;
        String _policyInForce_G = "Y";
        int _age_H = 0;
        int _premiumHolidayFlag_I = 0;


        double _premium_j = 0,
                _topUpPremium_K = 0,
                _premiumAllocationCharge_L = 0,
                _policyAdministrationCharge_M = 0,
                _serviceTax_N = 0,
                _totalDeduction_O = 0,
                _openingBalance_P = 0,
                _riskPremiumMortality_Q = 0,
                _serviceTax_R = 0,
                _guarateedAdditionToPolicyAccount_S = 0,
                _extraAdditionToPolicyAccount_T = 0,
                _FMC_U = 0,
                _serviceTaxOnFMC_V = 0,
                _closingBalance_W = 0,
                _surrenderCharges_X = 0,
                _surrenderValue_Y = 0,
                _deathBenefit_Z = 0,
                _openingBalance_AA = 0,
                _riskPremium_AB = 0,
                _serviceTax_AC = 0,
                _guarateedAdditionToPolicyAccount_AD = 0,
                _extraAdditionToPolicyAccount_AE = 0,
                _FMC_AF = 0,
                _serviceTaxOnFMC_AG = 0,
                _closingBalance_AH = 0,
                _surrenderCharges_AI = 0,
                _surrenderValue_AJ = 0,
                _deathBenefit_AK = 0,
                _reductionYield_AO;


        //Temp Variable Declaretion
        int month_E = 0;
        int year_F = 0;
        String policyInForce_G = "Y";
        int age_H = 0;
        int premiumHolidayFlag_I = 0;


        double premium_j = 0,
                topUpPremium_K = 0,
                premiumAllocationCharge_L = 0,
                policyAdministrationCharge_M = 0,
                serviceTax_N = 0,
                totalDeduction_O = 0,
                openingBalance_P = 0,
                riskPremiumMortality_Q = 0,
                serviceTax_R = 0,
                guarateedAdditionToPolicyAccount_S = 0,
                extraAdditionToPolicyAccount_T = 0,
                FMC_U = 0,
                serviceTaxOnFMC_V = 0,
                closingBalance_W = 0,
                surrenderCharges_X = 0,
                surrenderValue_Y = 0,
                deathBenefit_Z = 0,
                openingBalance_AA = 0,
                riskPremium_AB = 0,
                serviceTax_AC = 0,
                guarateedAdditionToPolicyAccount_AD = 0,
                extraAdditionToPolicyAccount_AE = 0,
                FMC_AF = 0,
                serviceTaxOnFMC_AG = 0,
                closingBalance_AH = 0,
                surrenderCharges_AI = 0,
                surrenderValue_AJ = 0,
                deathBenefit_AK = 0,
                reductionYield_AO = 0;

        double sum_J = 0,
                sum_K = 0,
                sum_L = 0,
                sum_M = 0,
                sum_N = 0,
                sum_O = 0,
                sum_P = 0,
                sum_Q = 0,
                sum_R = 0,
                sum_S = 0,
                sum_T = 0,
                sum_U = 0,
                sum_V = 0,
                sum_W = 0,
                sum_Y = 0,
                sum_Z = 0,
                sum_AA = 0,
                sum_AB = 0,
                sum_AC = 0,
                sum_AD = 0,
                sum_AE = 0,
                sum_AF = 0,
                sum_AG = 0,
                sum_AH = 0,
                sum_AJ = 0,
                sum_AK = 0;

        double _sum_J = 0,
                _sum_K = 0,
                _sum_L = 0,
                _sum_M = 0,
                _sum_N = 0,
                _sum_O = 0,
                _sum_P = 0,
                _sum_Q = 0,
                _sum_R = 0,
                _sum_S = 0,
                _sum_T = 0,
                _sum_U = 0,
                _sum_V = 0,
                _sum_W = 0,
                _sum_Y = 0,
                _sum_Z = 0,
                _sum_AA = 0,
                _sum_AB = 0,
                _sum_AC = 0,
                _sum_AD = 0,
                _sum_AE = 0,
                _sum_AF = 0,
                _sum_AG = 0,
                _sum_AH = 0,
                _sum_AJ = 0,
                _sum_AK = 0,
                Commission_AF;


        //From GUI Input
        int ageAtEntry = flexiSmartBean.getAgeAtEntry();
        flexiSmartBean.setSumAssured();
        flexiSmartBean.setEffectiveTopUpPrem(flexiSmartBean.getTopUpPremAmt(), flexiSmartBean.getPremiumAmount());
        double effectiveTopUpPrem = flexiSmartBean.getEffectiveTopUpPrem();

        //Declaration of method Variables/Object required for calculation
        FlexiSmartPlusBusinessLogic BIMAST = new FlexiSmartPlusBusinessLogic();
        String[] forBIArray = BIMAST.getForBIArr(prop.riskPremiumRate);
        int policyTerm = flexiSmartBean.getPolicyTerm();
        boolean jkResident = flexiSmartBean.getJkResident();
        double serviceTax = 0;
        boolean state = flexiSmartBean.isKerlaDisc();
        String option = flexiSmartBean.getOption();
        double sumAssured = flexiSmartBean.getSumAssured();
        double premiumAmt = flexiSmartBean.getPremiumAmount();
        double sumPremium = 0;
        int rowNumber = 0;

        //		for (int i = 1; i < 51; i++)
        for (int i = 1; i <= (policyTerm * 12); i++) {
            rowNumber++;
            ////System.out.println("********************************************* "+i+" Row Output *********************************************");
            BIMAST.setMonth_E(rowNumber);
            month_E = Integer.parseInt(BIMAST.getMonth_E());
            _month_E = month_E;
            //			System.out.println("1.   Month_E : "+month_E);

            BIMAST.setYear_F();
            year_F = Integer.parseInt(BIMAST.getYear_F());
            _year_F = year_F;
            //			System.out.println("2.   Year_F : "+year_F);


            if ((_month_E % 12) == 0) {

                bussIll.append("<policyYr" + _year_F + ">" + _year_F + "</policyYr" + _year_F + ">");


            }

//			Added By Saurabh Jain on 20/06/2019 Start
            if (state == true && _year_F <= 2) {
                serviceTax = 0.19;
            } else {
                serviceTax = 0.18;
            }

//			Added By Saurabh Jain on 20/06/2019 End
            policyInForce_G = BIMAST.getPolicyInForce_G();
            _policyInForce_G = BIMAST.getPolicyInForce_G();
            //			System.out.println("3.   PolicyInForce_G : "+BIMAST.getPolicyInForce_G());

            BIMAST.setAge_H(ageAtEntry);
            age_H = Integer.parseInt(BIMAST.getAge_H());
            _age_H = age_H;
            //			System.out.println("4.   Age_H : "+age_H);

            BIMAST.setPremiumHolidayFlag_I(flexiSmartBean.getPremiumHolidayStatus(), flexiSmartBean.getPolicyYear(), flexiSmartBean.getPremHolidayTerm());
            premiumHolidayFlag_I = Integer.parseInt(BIMAST.getPremiumHolidayFlag_I());
            _premiumHolidayFlag_I = premiumHolidayFlag_I;
            //			System.out.println("5.   PremiumHolidayFlag_I : "+premiumHolidayFlag_I);

            BIMAST.setPremium_J(flexiSmartBean.getPolicyTerm(), flexiSmartBean.getPF(), (flexiSmartBean.getPremiumAmount() * flexiSmartBean.getPF()));
            premium_j = Double.parseDouble(BIMAST.getPremium_J());
            _premium_j = premium_j;
            //			System.out.println("6.   Premium_J : "+premium_j);

            sum_J += _premium_j;
            if ((_month_E % 12) == 0) {
                bussIll.append("<AnnPrem" + _year_F + ">" + commonForAllProd.getStringWithout_E(Math.round(sum_J)) + "</AnnPrem" + _year_F + ">");
                _sum_J = sum_J;
                sum_J = 0;
            }

            BIMAST.setTopUpPremium_K(effectiveTopUpPrem, flexiSmartBean);
            topUpPremium_K = Double.parseDouble(BIMAST.getTopUpPremium_K());
            _topUpPremium_K = topUpPremium_K;
            //			System.out.println("7.   TopUpPremium_K : "+topUpPremium_K);

            sum_K += _topUpPremium_K;
            if ((_month_E % 12) == 0) {

                bussIll.append("<topUpPrem" + _year_F + ">" + Math.round(sum_K) + "</topUpPrem" + _year_F + ">");
                _sum_K = sum_K;
                sum_K = 0;
            }

            BIMAST.setPremiumAllocationCharge_L(flexiSmartBean);
            premiumAllocationCharge_L = Double.parseDouble(BIMAST.getPremiumAllocationCharge_L());
            _premiumAllocationCharge_L = premiumAllocationCharge_L;
            //			System.out.println("8.   premiumAllocationCharge_L : "+premiumAllocationCharge_L);

            sum_L += _premiumAllocationCharge_L;
            if ((_month_E % 12) == 0) {

                bussIll.append("<PremAllCharge" + _year_F + ">" + Math.round(sum_L) + "</PremAllCharge" + _year_F + ">");
                _sum_L = sum_L;
                sum_L = 0;
            }

            BIMAST.setPolicyAdministartionCharge_M();
            policyAdministrationCharge_M = Double.parseDouble(BIMAST.getPolicyAdministrationCharge_M());
            _policyAdministrationCharge_M = policyAdministrationCharge_M;
            //			System.out.println("9.   policyAdministrationCharge_M : "+policyAdministrationCharge_M);

            sum_M += _policyAdministrationCharge_M;
            if ((_month_E % 12) == 0) {

                bussIll.append("<PolicyAdmCharge" + _year_F + ">" + Math.round(sum_M) + "</PolicyAdmCharge" + _year_F + ">");
                _sum_M = sum_M;
                sum_M = 0;
            }

            BIMAST.setServiceTax_N(jkResident, flexiSmartBean, serviceTax);
            serviceTax_N = Double.parseDouble(BIMAST.getServiceTax_N());
            _serviceTax_N = serviceTax_N;
            //			System.out.println("10.   serviceTax_N : "+serviceTax_N);

            sum_N += _serviceTax_N;
            if ((_month_E % 12) == 0) {

                bussIll.append("<serviceTax" + _year_F + ">" + Math.round(sum_N) + "</serviceTax" + _year_F + ">");
                _sum_N = sum_N;
                sum_N = 0;
            }

            BIMAST.setTotalDeductions_O();
            totalDeduction_O = Double.parseDouble(BIMAST.getTotalDeductions_O());
            _totalDeduction_O = totalDeduction_O;
            //			System.out.println("11.   totalDeduction_O : "+totalDeduction_O);

            sum_O += _totalDeduction_O;
            if ((_month_E % 12) == 0) {

                bussIll.append("<totalDeduction" + _year_F + ">" + Math.round(sum_O) + "</totalDeduction" + _year_F + ">");
                _sum_O = sum_O;
                sum_O = 0;
            }

            BIMAST.setOpeningBalance_P(_closingBalance_W);
            openingBalance_P = Double.parseDouble(BIMAST.getOpeningBalance_P());
            _openingBalance_P = openingBalance_P;
            //			System.out.println("12.   openingBalance_P : "+openingBalance_P);

            if ((_month_E % 12) == 0) {
                sum_P = (_sum_J - _sum_L - _sum_M - _sum_N + _sum_W);
                bussIll.append("<openBalance4Pr" + _year_F + ">" + Math.round(sum_P) + "</openBalance4Pr" + _year_F + ">");
                _sum_P = sum_P;
                sum_P = 0;

            }

            BIMAST.setRiskPremiumMortality_Q(flexiSmartBean, forBIArray, option, sumAssured);
            riskPremiumMortality_Q = Double.parseDouble(BIMAST.getRiskPremiumMortality_Q());
            _riskPremiumMortality_Q = riskPremiumMortality_Q;
            //			System.out.println("13.   riskPremiumMortality_Q : "+riskPremiumMortality_Q);

            sum_Q += _riskPremiumMortality_Q;
            if ((_month_E % 12) == 0) {

                bussIll.append("<MortChrg4Pr" + _year_F + ">" + Math.round(sum_Q) + "</MortChrg4Pr" + _year_F + ">");
                _sum_Q = sum_Q;
                sum_Q = 0;
            }

            BIMAST.setServiceTax_R(flexiSmartBean, serviceTax);
            serviceTax_R = Double.parseDouble(BIMAST.getServiceTax_R());
            _serviceTax_R = serviceTax_R;
            //			System.out.println("14.   serviceTax_R : "+serviceTax_R);

            sum_R += _serviceTax_R;
            if ((_month_E % 12) == 0) {

                bussIll.append("<serTxonMortChrg4Pr" + _year_F + ">" + Math.round(sum_R) + "</serTxonMortChrg4Pr" + _year_F + ">");
                _sum_R = sum_R;
                sum_R = 0;
            }

            BIMAST.setGuarateedAdditionToPolicyAccount_S(flexiSmartBean);
            guarateedAdditionToPolicyAccount_S = Double.parseDouble(BIMAST.getGuarateedAdditionToPolicyAccount_S());
            _guarateedAdditionToPolicyAccount_S = guarateedAdditionToPolicyAccount_S;
//						System.out.println("15.   guarateedAdditionToPolicyAccount_S : "+guarateedAdditionToPolicyAccount_S);

            sum_S += _guarateedAdditionToPolicyAccount_S;
            if ((_month_E % 12) == 0) {

                bussIll.append("<GuareentedAdd4Pr" + _year_F + ">" + Math.round(sum_S) + "</GuareentedAdd4Pr" + _year_F + ">");
                _sum_S = sum_S;
                sum_S = 0;
            }

            BIMAST.setExtraAdditionToPolicyAccount_T(flexiSmartBean);
            extraAdditionToPolicyAccount_T = Double.parseDouble(BIMAST.getExtraAdditionToPolicyAccount_T());
            _extraAdditionToPolicyAccount_T = extraAdditionToPolicyAccount_T;
//						System.out.println("16.   extraAdditionToPolicyAccount_T : "+extraAdditionToPolicyAccount_T);

            sum_T += _extraAdditionToPolicyAccount_T;
            if ((_month_E % 12) == 0) {

                bussIll.append("<extraAdd4Pr" + _year_F + ">" + Math.round(sum_T) + "</extraAdd4Pr" + _year_F + ">");
                _sum_T = sum_T;
                sum_T = 0;
            }

            BIMAST.setFMC_U();
            FMC_U = Double.parseDouble(BIMAST.getFMC_U());
            _FMC_U = FMC_U;
            //			System.out.println("17.   FMC_U : "+FMC_U);

            sum_U += _FMC_U;
            if ((_month_E % 12) == 0) {

                bussIll.append("<FMC4Pr" + _year_F + ">" + Math.round(sum_U) + "</FMC4Pr" + _year_F + ">");
                _sum_U = sum_U;
                sum_U = 0;
            }

            BIMAST.setServiceTaxOnFMC_V(jkResident, flexiSmartBean, serviceTax);
            serviceTaxOnFMC_V = Double.parseDouble(BIMAST.getServiceTaxOnFMC_V());
            _serviceTaxOnFMC_V = serviceTaxOnFMC_V;
            //			System.out.println("18.   serviceTaxOnFMC_V : "+serviceTaxOnFMC_V);

            sum_V += _serviceTaxOnFMC_V;
            if ((_month_E % 12) == 0) {

                bussIll.append("<serTxonFMC4Pr" + _year_F + ">" + Math.round(sum_V) + "</serTxonFMC4Pr" + _year_F + ">");
                _sum_V = sum_V;
                sum_V = 0;
            }

            BIMAST.setClosingBalance_W();
            closingBalance_W = Double.parseDouble(BIMAST.getClosingBalance_W());
            _closingBalance_W = closingBalance_W;
            //			System.out.println("19.   closingBalance_W : "+closingBalance_W);


            if ((_month_E % 12) == 0) {
                sum_W = _closingBalance_W;
                bussIll.append("<closingBalance4Pr" + _year_F + ">" + Math.round(sum_W) + "</closingBalance4Pr" + _year_F + ">");
                _sum_W = sum_W;
                sum_W = 0;
            }

            BIMAST.setSurrenderCharges_X(premiumAmt);
            surrenderCharges_X = Double.parseDouble(BIMAST.getSurrenderCharges_X());
            _surrenderCharges_X = surrenderCharges_X;
            //			System.out.println("20.   surrenderCharges_X : "+surrenderCharges_X);

            BIMAST.setSurrenderValue_Y(flexiSmartBean, serviceTax);
            surrenderValue_Y = Double.parseDouble(BIMAST.getSurrenderValue_Y());
            _surrenderValue_Y = surrenderValue_Y;
            //			System.out.println("21.   surrenderValue_Y : "+surrenderValue_Y);

            if ((_month_E % 12) == 0) {
                sum_Y = _surrenderValue_Y;
                bussIll.append("<SurrenderVal4Pr" + _year_F + ">" + Math.round(sum_Y) + "</SurrenderVal4Pr" + _year_F + ">");
            }

            if (_month_E == 120) {
                //				System.out.println("bussIll" +bussIll.toString());
            }
            sumPremium += _premium_j;
            //			System.out.println("sumPremium "+sumPremium);
            BIMAST.setDeathBenefit_Z(option, sumAssured, sumPremium);
            deathBenefit_Z = Double.parseDouble(BIMAST.getDeathBenefit_Z());
            _deathBenefit_Z = deathBenefit_Z;
            //			System.out.println("22.   deathBenefit_Z : "+deathBenefit_Z);

            if ((_month_E % 12) == 0) {
                sum_Z = _deathBenefit_Z;
                bussIll.append("<DeathBenefit4Pr" + _year_F + ">" + Math.round(sum_Z) + "</DeathBenefit4Pr" + _year_F + ">");
            }

            BIMAST.setOpeningBalance_AA(_closingBalance_AH);
            openingBalance_AA = Double.parseDouble(BIMAST.getOpeningBalance_AA());
            _openingBalance_AA = openingBalance_AA;
            //			System.out.println("23.   openingBalance_AA : "+openingBalance_AA);

            if ((_month_E % 12) == 0) {
                sum_AA = (_sum_J - _sum_L - _sum_M - _sum_N + _sum_AH);
                bussIll.append("<openBalance8Pr" + _year_F + ">" + Math.round(sum_AA) + "</openBalance8Pr" + _year_F + ">");
                _sum_AA = sum_AA;
                sum_AA = 0;

            }

            BIMAST.setRiskPremiumMortality_AB(flexiSmartBean, forBIArray, option, sumAssured);
            riskPremium_AB = Double.parseDouble(BIMAST.getRiskPremiumMortality_AB());
            _riskPremium_AB = riskPremium_AB;
            //			System.out.println("24.   riskPremiumMortality_AB : "+riskPremium_AB);

            sum_AB += _riskPremium_AB;
            if ((_month_E % 12) == 0) {

                bussIll.append("<MortChrg8Pr" + _year_F + ">" + Math.round(sum_AB) + "</MortChrg8Pr" + _year_F + ">");
                _sum_AB = sum_AB;
                sum_AB = 0;
            }

            BIMAST.setServiceTax_AC(flexiSmartBean, serviceTax);
            serviceTax_AC = Double.parseDouble(BIMAST.getServiceTax_AC());
            _serviceTax_AC = serviceTax_AC;
            //			System.out.println("25.   serviceTax_AC : "+serviceTax_AC);

            sum_AC += _serviceTax_AC;
            if ((_month_E % 12) == 0) {

                bussIll.append("<serTxonMortChrg8Pr" + _year_F + ">" + Math.round(sum_AC) + "</serTxonMortChrg8Pr" + _year_F + ">");
                _sum_AC = sum_AC;
                sum_AC = 0;
            }

            BIMAST.setGuarateedAdditionToPolicyAccount_AD(flexiSmartBean);
            guarateedAdditionToPolicyAccount_AD = Double.parseDouble(BIMAST.getGuarateedAdditionToPolicyAccount_AD());
            _guarateedAdditionToPolicyAccount_AD = guarateedAdditionToPolicyAccount_AD;
            //			System.out.println("26.   guarateedAdditionToPolicyAccount_AD : "+guarateedAdditionToPolicyAccount_AD);

            sum_AD += _guarateedAdditionToPolicyAccount_AD;
            if ((_month_E % 12) == 0) {

                bussIll.append("<GuareentedAdd8Pr" + _year_F + ">" + Math.round(sum_AD) + "</GuareentedAdd8Pr" + _year_F + ">");
                _sum_AD = sum_AD;
                sum_AD = 0;
            }

            BIMAST.setExtraAdditionToPolicyAccount_AE(flexiSmartBean);
            extraAdditionToPolicyAccount_AE = Double.parseDouble(BIMAST.getExtraAdditionToPolicyAccount_AE());
            _extraAdditionToPolicyAccount_AE = extraAdditionToPolicyAccount_AE;
            //			System.out.println("27.   extraAdditionToPolicyAccount_AE : "+extraAdditionToPolicyAccount_AE);

            sum_AE += _extraAdditionToPolicyAccount_AE;
            if ((_month_E % 12) == 0) {

                bussIll.append("<extraAdd8Pr" + _year_F + ">" + Math.round(sum_AE) + "</extraAdd8Pr" + _year_F + ">");
                _sum_AE = sum_AE;
                sum_AE = 0;
            }

            BIMAST.setFMC_AF();
            FMC_AF = Double.parseDouble(BIMAST.getFMC_AF());
            _FMC_AF = FMC_AF;
            //			System.out.println("28.   FMC_AF : "+FMC_AF);

            sum_AF += _FMC_AF;
            if ((_month_E % 12) == 0) {

                bussIll.append("<FMC8Pr" + _year_F + ">" + Math.round(sum_AF) + "</FMC8Pr" + _year_F + ">");
                _sum_AF = sum_AF;
                sum_AF = 0;
            }

            BIMAST.setServiceTaxOnFMC_AG(jkResident, flexiSmartBean, serviceTax);
            serviceTaxOnFMC_AG = Double.parseDouble(BIMAST.getServiceTaxOnFMC_AG());
            _serviceTaxOnFMC_AG = serviceTaxOnFMC_AG;
            //			System.out.println("29.   serviceTaxOnFMC_AG : "+serviceTaxOnFMC_AG);

            sum_AG += _serviceTaxOnFMC_AG;
            if ((_month_E % 12) == 0) {

                bussIll.append("<serTxonFMC8Pr" + _year_F + ">" + Math.round(sum_AG) + "</serTxonFMC8Pr" + _year_F + ">");
                _sum_AG = sum_AG;
                sum_AG = 0;
            }

            BIMAST.setClosingBalance_AH();
            closingBalance_AH = Double.parseDouble(BIMAST.getClosingBalance_AH());
            _closingBalance_AH = closingBalance_AH;
            //			System.out.println("30.   closingBalance_AH : "+closingBalance_AH);

            if ((_month_E % 12) == 0) {
                sum_AH = _closingBalance_AH;
                bussIll.append("<closingBalance8Pr" + _year_F + ">" + Math.round(sum_AH) + "</closingBalance8Pr" + _year_F + ">");
                _sum_AH = sum_AH;
                sum_AH = 0;
            }

            BIMAST.setSurrenderCharges_AI(premiumAmt);
            surrenderCharges_AI = Double.parseDouble(BIMAST.getSurrenderCharges_AI());
            _surrenderCharges_AI = surrenderCharges_AI;
            //			System.out.println("31.   surrenderCharges_AI : "+surrenderCharges_AI);

            BIMAST.setSurrenderValue_AJ(flexiSmartBean, serviceTax);
            surrenderValue_AJ = Double.parseDouble(BIMAST.getSurrenderValue_AJ());
            _surrenderValue_AJ = surrenderValue_AJ;
            //			System.out.println("32.   surrenderValue_AJ : "+surrenderValue_AJ);

            if ((_month_E % 12) == 0) {
                sum_AJ = _surrenderValue_AJ;
                bussIll.append("<SurrenderVal8Pr" + _year_F + ">" + Math.round(sum_AJ) + "</SurrenderVal8Pr" + _year_F + ">");

            }

            BIMAST.setDeathBenefit_AK(option, sumAssured, sumPremium);
            deathBenefit_AK = Double.parseDouble(BIMAST.getDeathBenefit_AK());
            _deathBenefit_AK = deathBenefit_AK;
            //			System.out.println("33.   deathBenefit_AK : "+deathBenefit_AK);

            if ((_month_E % 12) == 0) {
                sum_AK = _deathBenefit_AK;
                bussIll.append("<DeathBenefit8Pr" + _year_F + ">" + Math.round(sum_AK) + "</DeathBenefit8Pr" + _year_F + ">");

            }

            if ((_month_E % 12) == 0) {
                //				System.out.println("aetfef"+BIMAST.getCommission_AF(_sum_J,flexiSmartBean));
                Commission_AF = BIMAST.getCommission_AF(_sum_J, flexiSmartBean);
                bussIll.append("<CommIfPay8Pr" + _year_F + ">" + Math.round(Commission_AF) + "</CommIfPay8Pr" + _year_F + ">");
            }

            ////System.out.println("***********************************************************************************************************");
        }

        //			System.out.println("****************************** Final Output ****************************************");
        //			System.out.println("Sum Assured -> "+commonForAllProd.getStringWithout_E(flexiSmartBean.getSumAssured()));
        //				System.out.println("Fund Value @6% is Rs.-> "+commonForAllProd.getRoundUp((commonForAllProd.getStringWithout_E(closingBalance_W))));
        //				System.out.println("Fund Value @10% -> "+commonForAllProd.getRound((commonForAllProd.getStringWithout_E(closingBalance_AH))));
        //				System.out.println("***********************************************************************************");
        //
        //		intent.putExtra("op","Sum Assured is Rs." + currencyFormat.format(Double.parseDouble(commonForAllProd.getStringWithout_E(flexiSmartBean.getSumAssured()))));
        //		intent.putExtra("op1","Maturity Benefit Payable @ 4% is Rs."+  currencyFormat.format(Double.parseDouble(commonForAllProd.getRoundUp((commonForAllProd.getStringWithout_E(closingBalance_W))))));
        //		intent.putExtra("op2","Maturity Benefit Payable @ 8% is Rs." +  currencyFormat.format(Double.parseDouble(commonForAllProd.getRoundUp((commonForAllProd.getStringWithout_E(closingBalance_AH))))));
        //		startActivity(intent);

        return new String[]{commonForAllProd.getStringWithout_E(flexiSmartBean.getSumAssured()), commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(closingBalance_W)), commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(closingBalance_AH))};
    }

    // public String[] getOutput(String sheetName,
    // FlexiSmartPlusBean flexiSmartBean) {
    //
    // bussIll = new StringBuilder();
    // BI_CommonForAllProd commonForAllProd = new BI_CommonForAllProd();
    // FlexiSmartPlusProperties prop = new FlexiSmartPlusProperties();
    // // Intent intent = new Intent(getApplicationContext(),success.class);
    //
    // // Output Variable Declaration
    // int _month_E = 0;
    // int _year_F = 0;
    // String _policyInForce_G = "Y";
    // int _age_H = 0;
    // int _premiumHolidayFlag_I = 0;
    //
    // double _premium_j = 0, _topUpPremium_K = 0, _premiumAllocationCharge_L =
    // 0, _policyAdministrationCharge_M = 0, _serviceTax_N = 0,
    // _totalDeduction_O = 0, _openingBalance_P = 0, _riskPremiumMortality_Q =
    // 0, _serviceTax_R = 0, _guarateedAdditionToPolicyAccount_S = 0,
    // _extraAdditionToPolicyAccount_T = 0, _FMC_U = 0, _serviceTaxOnFMC_V = 0,
    // _closingBalance_W = 0, _surrenderCharges_X = 0, _surrenderValue_Y = 0,
    // _deathBenefit_Z = 0, _openingBalance_AA = 0, _riskPremium_AB = 0,
    // _serviceTax_AC = 0, _guarateedAdditionToPolicyAccount_AD = 0,
    // _extraAdditionToPolicyAccount_AE = 0, _FMC_AF = 0, _serviceTaxOnFMC_AG =
    // 0, _closingBalance_AH = 0, _surrenderCharges_AI = 0, _surrenderValue_AJ =
    // 0, _deathBenefit_AK = 0, _reductionYield_AO;
    //
    // // Temp Variable Declaretion
    // int month_E = 0;
    // int year_F = 0;
    // String policyInForce_G = "Y";
    // int age_H = 0;
    // int premiumHolidayFlag_I = 0;
    //
    // double premium_j = 0, topUpPremium_K = 0, premiumAllocationCharge_L = 0,
    // policyAdministrationCharge_M = 0, serviceTax_N = 0, totalDeduction_O = 0,
    // openingBalance_P = 0, riskPremiumMortality_Q = 0, serviceTax_R = 0,
    // guarateedAdditionToPolicyAccount_S = 0, extraAdditionToPolicyAccount_T =
    // 0, FMC_U = 0, serviceTaxOnFMC_V = 0, closingBalance_W = 0,
    // surrenderCharges_X = 0, surrenderValue_Y = 0, deathBenefit_Z = 0,
    // openingBalance_AA = 0, riskPremium_AB = 0, serviceTax_AC = 0,
    // guarateedAdditionToPolicyAccount_AD = 0, extraAdditionToPolicyAccount_AE
    // = 0, FMC_AF = 0, serviceTaxOnFMC_AG = 0, closingBalance_AH = 0,
    // surrenderCharges_AI = 0, surrenderValue_AJ = 0, deathBenefit_AK = 0,
    // reductionYield_AO = 0;
    //
    // double sum_J = 0, sum_K = 0, sum_L = 0, sum_M = 0, sum_N = 0, sum_O = 0,
    // sum_P = 0, sum_Q = 0, sum_R = 0, sum_S = 0, sum_T = 0, sum_U = 0, sum_V =
    // 0, sum_W = 0, sum_Y = 0, sum_Z = 0, sum_AA = 0, sum_AB = 0, sum_AC = 0,
    // sum_AD = 0, sum_AE = 0, sum_AF = 0, sum_AG = 0, sum_AH = 0, sum_AJ = 0,
    // sum_AK = 0;
    //
    // double _sum_J = 0, _sum_K = 0, _sum_L = 0, _sum_M = 0, _sum_N = 0, _sum_O
    // = 0, _sum_P = 0, _sum_Q = 0, _sum_R = 0, _sum_S = 0, _sum_T = 0, _sum_U =
    // 0, _sum_V = 0, _sum_W = 0, _sum_Y = 0, _sum_Z = 0, _sum_AA = 0, _sum_AB =
    // 0, _sum_AC = 0, _sum_AD = 0, _sum_AE = 0, _sum_AF = 0, _sum_AG = 0,
    // _sum_AH = 0, _sum_AJ = 0, _sum_AK = 0, Commission_AF;
    //
    // // From GUI Input
    // int ageAtEntry = flexiSmartBean.getAgeAtEntry();
    // flexiSmartBean.setSumAssured();
    // flexiSmartBean.setEffectiveTopUpPrem(flexiSmartBean.getTopUpPremAmt(),
    // flexiSmartBean.getPremiumAmount());
    // double effectiveTopUpPrem = flexiSmartBean.getEffectiveTopUpPrem();
    //
    // // Declaration of method Variables/Object required for calculation
    // FlexiSmartPlusBusinessLogic BIMAST = new FlexiSmartPlusBusinessLogic();
    // String[] forBIArray = BIMAST.getForBIArr(prop.riskPremiumRate);
    // int policyTerm = flexiSmartBean.getPolicyTerm();
    // boolean jkResident = flexiSmartBean.getJkResident();
    // String option = flexiSmartBean.getOption();
    // double sumAssured = flexiSmartBean.getSumAssured();
    // double premiumAmt = flexiSmartBean.getPremiumAmount();
    // double sumPremium = 0;
    // int rowNumber = 0;
    //
    // // for (int i = 1; i < 51; i++)
    // for (int i = 1; i <= (policyTerm * 12); i++) {
    // rowNumber++;
    // //
    // //System.out.println("********************************************* "+i+" Row Output *********************************************");
    // BIMAST.setMonth_E(rowNumber);
    // month_E = Integer.parseInt(BIMAST.getMonth_E());
    // _month_E = month_E;
    // // System.out.println("1.   Month_E : "+month_E);
    //
    // BIMAST.setYear_F();
    // year_F = Integer.parseInt(BIMAST.getYear_F());
    // _year_F = year_F;
    // // System.out.println("2.   Year_F : "+year_F);
    //
    // if ((_month_E % 12) == 0) {
    //
    // bussIll.append("<policyYr" + _year_F + ">" + _year_F
    // + "</policyYr" + _year_F + ">");
    //
    // }
    //
    // policyInForce_G = BIMAST.getPolicyInForce_G();
    // _policyInForce_G = BIMAST.getPolicyInForce_G();
    // //
    // System.out.println("3.   PolicyInForce_G : "+BIMAST.getPolicyInForce_G());
    //
    // BIMAST.setAge_H(ageAtEntry);
    // age_H = Integer.parseInt(BIMAST.getAge_H());
    // _age_H = age_H;
    // // System.out.println("4.   Age_H : "+age_H);
    //
    // BIMAST.setPremiumHolidayFlag_I(
    // flexiSmartBean.getPremiumHolidayStatus(),
    // flexiSmartBean.getPolicyYear(),
    // flexiSmartBean.getPremHolidayTerm());
    // premiumHolidayFlag_I = Integer.parseInt(BIMAST
    // .getPremiumHolidayFlag_I());
    // _premiumHolidayFlag_I = premiumHolidayFlag_I;
    // //
    // System.out.println("5.   PremiumHolidayFlag_I : "+premiumHolidayFlag_I);
    //
    // BIMAST.setPremium_J(
    // flexiSmartBean.getPolicyTerm(),
    // flexiSmartBean.getPF(),
    // (flexiSmartBean.getPremiumAmount() * flexiSmartBean.getPF()));
    // premium_j = Double.parseDouble(BIMAST.getPremium_J());
    // _premium_j = premium_j;
    // // System.out.println("6.   Premium_J : "+premium_j);
    //
    // sum_J += _premium_j;
    // if ((_month_E % 12) == 0) {
    // bussIll.append("<AnnPrem" + _year_F + ">" + Math.round(sum_J)
    // + "</AnnPrem" + _year_F + ">");
    // _sum_J = sum_J;
    // sum_J = 0;
    // }
    //
    // BIMAST.setTopUpPremium_K(effectiveTopUpPrem, flexiSmartBean);
    // topUpPremium_K = Double.parseDouble(BIMAST.getTopUpPremium_K());
    // _topUpPremium_K = topUpPremium_K;
    // // System.out.println("7.   TopUpPremium_K : "+topUpPremium_K);
    //
    // sum_K += _topUpPremium_K;
    // if ((_month_E % 12) == 0) {
    //
    // bussIll.append("<topUpPrem" + _year_F + ">" + Math.round(sum_K)
    // + "</topUpPrem" + _year_F + ">");
    // _sum_K = sum_K;
    // sum_K = 0;
    // }
    //
    // BIMAST.setPremiumAllocationCharge_L(flexiSmartBean);
    // premiumAllocationCharge_L = Double.parseDouble(BIMAST
    // .getPremiumAllocationCharge_L());
    // _premiumAllocationCharge_L = premiumAllocationCharge_L;
    // //
    // System.out.println("8.   premiumAllocationCharge_L : "+premiumAllocationCharge_L);
    //
    // sum_L += _premiumAllocationCharge_L;
    // if ((_month_E % 12) == 0) {
    //
    // bussIll.append("<PremAllCharge" + _year_F + ">"
    // + Math.round(sum_L) + "</PremAllCharge" + _year_F + ">");
    // _sum_L = sum_L;
    // sum_L = 0;
    // }
    //
    // BIMAST.setPolicyAdministartionCharge_M();
    // policyAdministrationCharge_M = Double.parseDouble(BIMAST
    // .getPolicyAdministrationCharge_M());
    // _policyAdministrationCharge_M = policyAdministrationCharge_M;
    // //
    // System.out.println("9.   policyAdministrationCharge_M : "+policyAdministrationCharge_M);
    //
    // sum_M += _policyAdministrationCharge_M;
    // if ((_month_E % 12) == 0) {
    //
    // bussIll.append("<PolicyAdmCharge" + _year_F + ">"
    // + Math.round(sum_M) + "</PolicyAdmCharge" + _year_F
    // + ">");
    // _sum_M = sum_M;
    // sum_M = 0;
    // }
    //
    // BIMAST.setServiceTax_N(jkResident, flexiSmartBean);
    // serviceTax_N = Double.parseDouble(BIMAST.getServiceTax_N());
    // _serviceTax_N = serviceTax_N;
    // // System.out.println("10.   serviceTax_N : "+serviceTax_N);
    //
    // sum_N += _serviceTax_N;
    // if ((_month_E % 12) == 0) {
    //
    // bussIll.append("<serviceTax" + _year_F + ">"
    // + Math.round(sum_N) + "</serviceTax" + _year_F + ">");
    // _sum_N = sum_N;
    // sum_N = 0;
    // }
    //
    // BIMAST.setTotalDeductions_O();
    // totalDeduction_O = Double
    // .parseDouble(BIMAST.getTotalDeductions_O());
    // _totalDeduction_O = totalDeduction_O;
    // // System.out.println("11.   totalDeduction_O : "+totalDeduction_O);
    //
    // sum_O += _totalDeduction_O;
    // if ((_month_E % 12) == 0) {
    //
    // bussIll.append("<totalDeduction" + _year_F + ">"
    // + Math.round(sum_O) + "</totalDeduction" + _year_F
    // + ">");
    // _sum_O = sum_O;
    // sum_O = 0;
    // }
    //
    // BIMAST.setOpeningBalance_P(_closingBalance_W);
    // openingBalance_P = Double.parseDouble(BIMAST.getOpeningBalance_P());
    // _openingBalance_P = openingBalance_P;
    // // System.out.println("12.   openingBalance_P : "+openingBalance_P);
    //
    // if ((_month_E % 12) == 0) {
    // sum_P = (_sum_J - _sum_L - _sum_M - _sum_N + _sum_W);
    // bussIll.append("<openBalance4Pr" + _year_F + ">"
    // + Math.round(sum_P) + "</openBalance4Pr" + _year_F
    // + ">");
    // _sum_P = sum_P;
    // sum_P = 0;
    //
    // }
    //
    // BIMAST.setRiskPremiumMortality_Q(flexiSmartBean, forBIArray,
    // option, sumAssured);
    // riskPremiumMortality_Q = Double.parseDouble(BIMAST
    // .getRiskPremiumMortality_Q());
    // _riskPremiumMortality_Q = riskPremiumMortality_Q;
    // //
    // System.out.println("13.   riskPremiumMortality_Q : "+riskPremiumMortality_Q);
    //
    // sum_Q += _riskPremiumMortality_Q;
    // if ((_month_E % 12) == 0) {
    //
    // bussIll.append("<MortChrg4Pr" + _year_F + ">"
    // + Math.round(sum_Q) + "</MortChrg4Pr" + _year_F + ">");
    // _sum_Q = sum_Q;
    // sum_Q = 0;
    // }
    //
    // BIMAST.setServiceTax_R(flexiSmartBean);
    // serviceTax_R = Double.parseDouble(BIMAST.getServiceTax_R());
    // _serviceTax_R = serviceTax_R;
    // // System.out.println("14.   serviceTax_R : "+serviceTax_R);
    //
    // sum_R += _serviceTax_R;
    // if ((_month_E % 12) == 0) {
    //
    // bussIll.append("<serTxonMortChrg4Pr" + _year_F + ">"
    // + Math.round(sum_R) + "</serTxonMortChrg4Pr" + _year_F
    // + ">");
    // _sum_R = sum_R;
    // sum_R = 0;
    // }
    //
    // BIMAST.setGuarateedAdditionToPolicyAccount_S(flexiSmartBean);
    // guarateedAdditionToPolicyAccount_S = Double.parseDouble(BIMAST
    // .getGuarateedAdditionToPolicyAccount_S());
    // _guarateedAdditionToPolicyAccount_S = guarateedAdditionToPolicyAccount_S;
    // //
    // System.out.println("15.   guarateedAdditionToPolicyAccount_S : "+guarateedAdditionToPolicyAccount_S);
    //
    // sum_S += _guarateedAdditionToPolicyAccount_S;
    // if ((_month_E % 12) == 0) {
    //
    // bussIll.append("<GuareentedAdd4Pr" + _year_F + ">"
    // + Math.round(sum_S) + "</GuareentedAdd4Pr" + _year_F
    // + ">");
    // _sum_S = sum_S;
    // sum_S = 0;
    // }
    //
    // BIMAST.setExtraAdditionToPolicyAccount_T(flexiSmartBean);
    // extraAdditionToPolicyAccount_T = Double.parseDouble(BIMAST
    // .getExtraAdditionToPolicyAccount_T());
    // _extraAdditionToPolicyAccount_T = extraAdditionToPolicyAccount_T;
    // //
    // System.out.println("16.   extraAdditionToPolicyAccount_T : "+extraAdditionToPolicyAccount_T);
    //
    // sum_T += _extraAdditionToPolicyAccount_T;
    // if ((_month_E % 12) == 0) {
    //
    // bussIll.append("<extraAdd4Pr" + _year_F + ">"
    // + Math.round(sum_T) + "</extraAdd4Pr" + _year_F + ">");
    // _sum_T = sum_T;
    // sum_T = 0;
    // }
    //
    // BIMAST.setFMC_U();
    // FMC_U = Double.parseDouble(BIMAST.getFMC_U());
    // _FMC_U = FMC_U;
    // // System.out.println("17.   FMC_U : "+FMC_U);
    //
    // sum_U += _FMC_U;
    // if ((_month_E % 12) == 0) {
    //
    // bussIll.append("<FMC4Pr" + _year_F + ">" + Math.round(sum_U)
    // + "</FMC4Pr" + _year_F + ">");
    // _sum_U = sum_U;
    // sum_U = 0;
    // }
    //
    // BIMAST.setServiceTaxOnFMC_V(jkResident, flexiSmartBean);
    // serviceTaxOnFMC_V = Double.parseDouble(BIMAST
    // .getServiceTaxOnFMC_V());
    // _serviceTaxOnFMC_V = serviceTaxOnFMC_V;
    // // System.out.println("18.   serviceTaxOnFMC_V : "+serviceTaxOnFMC_V);
    //
    // sum_V += _serviceTaxOnFMC_V;
    // if ((_month_E % 12) == 0) {
    //
    // bussIll.append("<serTxonFMC4Pr" + _year_F + ">"
    // + Math.round(sum_V) + "</serTxonFMC4Pr" + _year_F + ">");
    // _sum_V = sum_V;
    // sum_V = 0;
    // }
    //
    // BIMAST.setClosingBalance_W();
    // closingBalance_W = Double.parseDouble(BIMAST.getClosingBalance_W());
    // _closingBalance_W = closingBalance_W;
    // // System.out.println("19.   closingBalance_W : "+closingBalance_W);
    //
    // if ((_month_E % 12) == 0) {
    // sum_W = _closingBalance_W;
    // bussIll.append("<closingBalance4Pr" + _year_F + ">"
    // + Math.round(sum_W) + "</closingBalance4Pr" + _year_F
    // + ">");
    // _sum_W = sum_W;
    // sum_W = 0;
    // }
    //
    // BIMAST.setSurrenderCharges_X(premiumAmt);
    // surrenderCharges_X = Double.parseDouble(BIMAST
    // .getSurrenderCharges_X());
    // _surrenderCharges_X = surrenderCharges_X;
    // // System.out.println("20.   surrenderCharges_X : "+surrenderCharges_X);
    //
    // BIMAST.setSurrenderValue_Y(flexiSmartBean);
    // surrenderValue_Y = Double.parseDouble(BIMAST.getSurrenderValue_Y());
    // _surrenderValue_Y = surrenderValue_Y;
    // // System.out.println("21.   surrenderValue_Y : "+surrenderValue_Y);
    //
    // if ((_month_E % 12) == 0) {
    // sum_Y = _surrenderValue_Y;
    // bussIll.append("<SurrenderVal4Pr" + _year_F + ">"
    // + Math.round(sum_Y) + "</SurrenderVal4Pr" + _year_F
    // + ">");
    // }
    //
    // sumPremium += _premium_j;
    //
    // BIMAST.setDeathBenefit_Z(option, sumAssured, sumPremium);
    // deathBenefit_Z = Double.parseDouble(BIMAST.getDeathBenefit_Z());
    // _deathBenefit_Z = deathBenefit_Z;
    // // System.out.println("22.   deathBenefit_Z : "+deathBenefit_Z);
    //
    // if ((_month_E % 12) == 0) {
    // sum_Z = _deathBenefit_Z;
    // bussIll.append("<DeathBenefit4Pr" + _year_F + ">"
    // + Math.round(sum_Z) + "</DeathBenefit4Pr" + _year_F
    // + ">");
    // }
    //
    // BIMAST.setOpeningBalance_AA(_closingBalance_AH);
    // openingBalance_AA = Double.parseDouble(BIMAST
    // .getOpeningBalance_AA());
    // _openingBalance_AA = openingBalance_AA;
    // // System.out.println("23.   openingBalance_AA : "+openingBalance_AA);
    //
    // if ((_month_E % 12) == 0) {
    // sum_AA = (_sum_J - _sum_L - _sum_M - _sum_N + _sum_AH);
    // bussIll.append("<openBalance8Pr" + _year_F + ">"
    // + Math.round(sum_AA) + "</openBalance8Pr" + _year_F
    // + ">");
    // _sum_AA = sum_AA;
    // sum_AA = 0;
    //
    // }
    //
    // BIMAST.setRiskPremiumMortality_AB(flexiSmartBean, forBIArray,
    // option, sumAssured);
    // riskPremium_AB = Double.parseDouble(BIMAST
    // .getRiskPremiumMortality_AB());
    // _riskPremium_AB = riskPremium_AB;
    // // System.out.println("24.   riskPremiumMortality_AB : "+riskPremium_AB);
    //
    // sum_AB += _riskPremium_AB;
    // if ((_month_E % 12) == 0) {
    //
    // bussIll.append("<MortChrg8Pr" + _year_F + ">"
    // + Math.round(sum_AB) + "</MortChrg8Pr" + _year_F + ">");
    // _sum_AB = sum_AB;
    // sum_AB = 0;
    // }
    //
    // BIMAST.setServiceTax_AC(flexiSmartBean);
    // serviceTax_AC = Double.parseDouble(BIMAST.getServiceTax_AC());
    // _serviceTax_AC = serviceTax_AC;
    // // System.out.println("25.   serviceTax_AC : "+serviceTax_AC);
    //
    // sum_AC += _serviceTax_AC;
    // if ((_month_E % 12) == 0) {
    //
    // bussIll.append("<serTxonMortChrg8Pr" + _year_F + ">"
    // + Math.round(sum_AC) + "</serTxonMortChrg8Pr" + _year_F
    // + ">");
    // _sum_AC = sum_AC;
    // sum_AC = 0;
    // }
    //
    // BIMAST.setGuarateedAdditionToPolicyAccount_AD(flexiSmartBean);
    // guarateedAdditionToPolicyAccount_AD = Double.parseDouble(BIMAST
    // .getGuarateedAdditionToPolicyAccount_AD());
    // _guarateedAdditionToPolicyAccount_AD =
    // guarateedAdditionToPolicyAccount_AD;
    // //
    // System.out.println("26.   guarateedAdditionToPolicyAccount_AD : "+guarateedAdditionToPolicyAccount_AD);
    //
    // sum_AD += _guarateedAdditionToPolicyAccount_AD;
    // if ((_month_E % 12) == 0) {
    //
    // bussIll.append("<GuareentedAdd8Pr" + _year_F + ">"
    // + Math.round(sum_AD) + "</GuareentedAdd8Pr" + _year_F
    // + ">");
    // _sum_AD = sum_AD;
    // sum_AD = 0;
    // }
    //
    // BIMAST.setExtraAdditionToPolicyAccount_AE(flexiSmartBean);
    // extraAdditionToPolicyAccount_AE = Double.parseDouble(BIMAST
    // .getExtraAdditionToPolicyAccount_AE());
    // _extraAdditionToPolicyAccount_AE = extraAdditionToPolicyAccount_AE;
    // //
    // System.out.println("27.   extraAdditionToPolicyAccount_AE : "+extraAdditionToPolicyAccount_AE);
    //
    // sum_AE += _extraAdditionToPolicyAccount_AE;
    // if ((_month_E % 12) == 0) {
    //
    // bussIll.append("<extraAdd8Pr" + _year_F + ">"
    // + Math.round(sum_AE) + "</extraAdd8Pr" + _year_F + ">");
    // _sum_AE = sum_AE;
    // sum_AE = 0;
    // }
    //
    // BIMAST.setFMC_AF();
    // FMC_AF = Double.parseDouble(BIMAST.getFMC_AF());
    // _FMC_AF = FMC_AF;
    // // System.out.println("28.   FMC_AF : "+FMC_AF);
    //
    // sum_AF += _FMC_AF;
    // if ((_month_E % 12) == 0) {
    //
    // bussIll.append("<FMC8Pr" + _year_F + ">" + Math.round(sum_AF)
    // + "</FMC8Pr" + _year_F + ">");
    // _sum_AF = sum_AF;
    // sum_AF = 0;
    // }
    //
    // BIMAST.setServiceTaxOnFMC_AG(jkResident, flexiSmartBean);
    // serviceTaxOnFMC_AG = Double.parseDouble(BIMAST
    // .getServiceTaxOnFMC_AG());
    // _serviceTaxOnFMC_AG = serviceTaxOnFMC_AG;
    // // System.out.println("29.   serviceTaxOnFMC_AG : "+serviceTaxOnFMC_AG);
    //
    // sum_AG += _serviceTaxOnFMC_AG;
    // if ((_month_E % 12) == 0) {
    //
    // bussIll.append("<serTxonFMC8Pr" + _year_F + ">"
    // + Math.round(sum_AG) + "</serTxonFMC8Pr" + _year_F
    // + ">");
    // _sum_AG = sum_AG;
    // sum_AG = 0;
    // }
    //
    // BIMAST.setClosingBalance_AH();
    // closingBalance_AH = Double.parseDouble(BIMAST
    // .getClosingBalance_AH());
    // _closingBalance_AH = closingBalance_AH;
    // // System.out.println("30.   closingBalance_AH : "+closingBalance_AH);
    //
    // if ((_month_E % 12) == 0) {
    // sum_AH = _closingBalance_AH;
    // bussIll.append("<closingBalance8Pr" + _year_F + ">"
    // + Math.round(sum_AH) + "</closingBalance8Pr" + _year_F
    // + ">");
    // _sum_AH = sum_AH;
    // sum_AH = 0;
    // }
    //
    // BIMAST.setSurrenderCharges_AI(premiumAmt);
    // surrenderCharges_AI = Double.parseDouble(BIMAST
    // .getSurrenderCharges_AI());
    // _surrenderCharges_AI = surrenderCharges_AI;
    // //
    // System.out.println("31.   surrenderCharges_AI : "+surrenderCharges_AI);
    //
    // BIMAST.setSurrenderValue_AJ(flexiSmartBean);
    // surrenderValue_AJ = Double.parseDouble(BIMAST
    // .getSurrenderValue_AJ());
    // _surrenderValue_AJ = surrenderValue_AJ;
    // // System.out.println("32.   surrenderValue_AJ : "+surrenderValue_AJ);
    //
    // if ((_month_E % 12) == 0) {
    // sum_AJ = _surrenderValue_AJ;
    // bussIll.append("<SurrenderVal8Pr" + _year_F + ">"
    // + Math.round(sum_AJ) + "</SurrenderVal8Pr" + _year_F
    // + ">");
    //
    // }
    //
    // BIMAST.setDeathBenefit_AK(option, sumAssured, sumPremium);
    // deathBenefit_AK = Double.parseDouble(BIMAST.getDeathBenefit_AK());
    // _deathBenefit_AK = deathBenefit_AK;
    // // System.out.println("33.   deathBenefit_AK : "+deathBenefit_AK);
    //
    // if ((_month_E % 12) == 0) {
    // sum_AK = _deathBenefit_AK;
    // bussIll.append("<DeathBenefit8Pr" + _year_F + ">"
    // + Math.round(sum_AK) + "</DeathBenefit8Pr" + _year_F
    // + ">");
    //
    // }
    //
    // if ((_month_E % 12) == 0) {
    //
    // Commission_AF = BIMAST.getCommission_AF(_sum_J, flexiSmartBean);
    // bussIll.append("<CommIfPay8Pr" + _year_F + ">"
    // + Math.round(Commission_AF) + "</CommIfPay8Pr"
    // + _year_F + ">");
    // }
    //
    // //
    // //System.out.println("***********************************************************************************************************");
    // }
    //
    // //
    // System.out.println("****************************** Final Output ****************************************");
    // //
    // System.out.println("Sum Assured -> "+commonForAllProd.getStringWithout_E(flexiSmartBean.getSumAssured()));
    // //
    // System.out.println("Fund Value @6% is Rs.-> "+commonForAllProd.getRoundUp((commonForAllProd.getStringWithout_E(closingBalance_W))));
    // //
    // System.out.println("Fund Value @10% -> "+commonForAllProd.getRound((commonForAllProd.getStringWithout_E(closingBalance_AH))));
    // //
    // System.out.println("***********************************************************************************");
    // //
    // // intent.putExtra("op","Sum Assured is Rs." +
    // //
    // currencyFormat.format(Double.parseDouble(commonForAllProd.getStringWithout_E(flexiSmartBean.getSumAssured()))));
    // // intent.putExtra("op1","Maturity Benefit Payable @ 4% is Rs."+
    // //
    // currencyFormat.format(Double.parseDouble(commonForAllProd.getRoundUp((commonForAllProd.getStringWithout_E(closingBalance_W))))));
    // // intent.putExtra("op2","Maturity Benefit Payable @ 8% is Rs." +
    // //
    // currencyFormat.format(Double.parseDouble(commonForAllProd.getRoundUp((commonForAllProd.getStringWithout_E(closingBalance_AH))))));
    // // startActivity(intent);
    //
    // return new String[] {
    // commonForAllProd.getStringWithout_E(flexiSmartBean
    // .getSumAssured()),
    // commonForAllProd.getRoundUp(commonForAllProd
    // .getStringWithout_E(closingBalance_W)),
    // commonForAllProd.getRoundUp(commonForAllProd
    // .getStringWithout_E(closingBalance_AH)) };
    // }

    /*********************************** BI starts here *************************************/

  /*  private String[] getOutputReductionInYield(String SheetName,
                                               FlexiSmartPlusBean flexiSmartBean) {

        BI_CommonForAllProd commonForAllProd = new BI_CommonForAllProd();
        FlexiSmartPlusProperties prop = new FlexiSmartPlusProperties();
        // Intent intent = new Intent(getApplicationContext(),success.class);
        ArrayList<String> List_AO = new ArrayList<String>();

        // Output Variable Declaration
        int _month_E = 0;
        int _year_F = 0;
        String _policyInForce_G = "Y";
        int _age_H = 0;
        int _premiumHolidayFlag_I = 0;

        double _premium_j = 0, _topUpPremium_K = 0, _premiumAllocationCharge_L = 0, _policyAdministrationCharge_M = 0, _serviceTax_N = 0, _totalDeduction_O = 0, _openingBalance_P = 0, _riskPremiumMortality_Q = 0, _serviceTax_R = 0, _guarateedAdditionToPolicyAccount_S = 0, _extraAdditionToPolicyAccount_T = 0, _FMC_U = 0, _serviceTaxOnFMC_V = 0, _closingBalance_W = 0, _surrenderCharges_X = 0, _surrenderValue_Y = 0, _deathBenefit_Z = 0, _openingBalance_AA = 0, _riskPremium_AB = 0, _serviceTax_AC = 0, _guarateedAdditionToPolicyAccount_AD = 0, _extraAdditionToPolicyAccount_AE = 0, _FMC_AF = 0, _serviceTaxOnFMC_AG = 0, _closingBalance_AH = 0, _surrenderCharges_AI = 0, _surrenderValue_AJ = 0, _deathBenefit_AK = 0, _month_BB = 0, _reductionYield_AO = 0, _irrAnnual_AP = 0, _reductionInYieldMaturityAt = 0;

        // Temp Variable Declaretion
        int month_E = 0;
        int year_F = 0;
        String policyInForce_G = "Y";
        int age_H = 0;
        int premiumHolidayFlag_I = 0;

        double premium_j = 0, topUpPremium_K = 0, premiumAllocationCharge_L = 0, policyAdministrationCharge_M = 0, serviceTax_N = 0, totalDeduction_O = 0, openingBalance_P = 0, riskPremiumMortality_Q = 0, serviceTax_R = 0, guarateedAdditionToPolicyAccount_S = 0, extraAdditionToPolicyAccount_T = 0, FMC_U = 0, serviceTaxOnFMC_V = 0, closingBalance_W = 0, surrenderCharges_X = 0, surrenderValue_Y = 0, deathBenefit_Z = 0, openingBalance_AA = 0, riskPremium_AB = 0, serviceTax_AC = 0, guarateedAdditionToPolicyAccount_AD = 0, extraAdditionToPolicyAccount_AE = 0, FMC_AF = 0, serviceTaxOnFMC_AG = 0, closingBalance_AH = 0, surrenderCharges_AI = 0, surrenderValue_AJ = 0, deathBenefit_AK = 0, month_BB = 0, reductionYield_AO = 0, irrAnnual_AP = 0, reductionInYieldMaturityAt = 0;

        // From GUI Input
        int ageAtEntry = flexiSmartBean.getAgeAtEntry();
        flexiSmartBean.setSumAssured();
        flexiSmartBean.setEffectiveTopUpPrem(flexiSmartBean.getTopUpPremAmt(),
                flexiSmartBean.getPremiumAmount());
        double effectiveTopUpPrem = flexiSmartBean.getEffectiveTopUpPrem();

        // Declaration of method Variables/Object required for calculation
        FlexiSmartPlusBusinessLogic BIMAST = new FlexiSmartPlusBusinessLogic();
        String[] forBIArray = BIMAST.getForBIArr(prop.riskPremiumRate);
        //double serviceTax=flexiSmartBean.getServiceTax();
        double serviceTax = 0;
        boolean isKerlaDisc = flexiSmartBean.isKerlaDisc();
        int policyTerm = flexiSmartBean.getPolicyTerm();
        boolean jkResident = flexiSmartBean.getJkResident();
        String option = flexiSmartBean.getOption();
        double sumAssured = flexiSmartBean.getSumAssured();
        double premiumAmt = flexiSmartBean.getPremiumAmount();

        int rowNumber = 0, monthNumber = 0;

        // for (int i = 1; i < 3; i++)
        for (int i = 0; i <= (policyTerm * 12); i++) {
            rowNumber++;
            // //System.out.println("********************************************* "+i+" Row Output *********************************************");
            BIMAST.setMonth_E(rowNumber);
            month_E = Integer.parseInt(BIMAST.getMonth_E());
            _month_E = month_E;
            // System.out.println("1.   Month_E : "+month_E);

            BIMAST.setYear_F();
            year_F = Integer.parseInt(BIMAST.getYear_F());
            _year_F = year_F;
            // System.out.println("2.   Year_F : "+year_F);
            if (isKerlaDisc == true && _year_F <= 2) {
				serviceTax =0.19;
			}else{
				serviceTax =0.18;
            }
            policyInForce_G = BIMAST.getPolicyInForce_G();
            _policyInForce_G = BIMAST.getPolicyInForce_G();
            // System.out.println("3.   PolicyInForce_G : "+BIMAST.getPolicyInForce_G());

            BIMAST.setAge_H(ageAtEntry);
            age_H = Integer.parseInt(BIMAST.getAge_H());
            _age_H = age_H;
            // System.out.println("4.   Age_H : "+age_H);

            BIMAST.setPremiumHolidayFlag_I(
                    flexiSmartBean.getPremiumHolidayStatus(),
                    flexiSmartBean.getPolicyYear(),
                    flexiSmartBean.getPremHolidayTerm());
            premiumHolidayFlag_I = Integer.parseInt(BIMAST
                    .getPremiumHolidayFlag_I());
            _premiumHolidayFlag_I = premiumHolidayFlag_I;
            // System.out.println("5.   PremiumHolidayFlag_I : "+premiumHolidayFlag_I);

            BIMAST.setPremium_J(
                    flexiSmartBean.getPolicyTerm(),
                    flexiSmartBean.getPF(),
                    (flexiSmartBean.getPremiumAmount() * flexiSmartBean.getPF()));
            premium_j = Double.parseDouble(BIMAST.getPremium_J());
            _premium_j = premium_j;
            // System.out.println("6.   Premium_J : "+premium_j);

            BIMAST.setTopUpPremium_K(effectiveTopUpPrem, flexiSmartBean);
            topUpPremium_K = Double.parseDouble(BIMAST.getTopUpPremium_K());
            _topUpPremium_K = topUpPremium_K;
            // System.out.println("7.   TopUpPremium_K : "+topUpPremium_K);

            BIMAST.setPremiumAllocationChargeReductionYield_L(flexiSmartBean);
            premiumAllocationCharge_L = Double.parseDouble(BIMAST
                    .getPremiumAllocationChargeReductionYield_L());
            _premiumAllocationCharge_L = premiumAllocationCharge_L;
            // System.out.println("8.   premiumAllocationCharge_L : "+premiumAllocationCharge_L);

            BIMAST.setPolicyAdministartionCharge_M();
            policyAdministrationCharge_M = Double.parseDouble(BIMAST
                    .getPolicyAdministrationCharge_M());
            _policyAdministrationCharge_M = policyAdministrationCharge_M;
            // System.out.println("9.   policyAdministrationCharge_M : "+policyAdministrationCharge_M);

			BIMAST.setServiceTaxRedutionYield_N(flexiSmartBean,serviceTax);
            serviceTax_N = Double.parseDouble(BIMAST
                    .getServiceTaxReductionYield_N());
            _serviceTax_N = serviceTax_N;
            // System.out.println("10.   serviceTax_N : "+serviceTax_N);

            BIMAST.setTotalDeductionsReductionYield_O();
            totalDeduction_O = Double.parseDouble(BIMAST
                    .getTotalDeductionsReductionYield_O());
            _totalDeduction_O = totalDeduction_O;
            // System.out.println("11.   totalDeduction_O : "+totalDeduction_O);

            BIMAST.setOpeningBalanceReductionYield_P(_closingBalance_W);
            openingBalance_P = Double.parseDouble(BIMAST
                    .getOpeningBalanceReductionYield_P());
            _openingBalance_P = openingBalance_P;
            // System.out.println("12.   openingBalance_P : "+openingBalance_P);

            BIMAST.setRiskPremiumMortalityReductionYield_Q(flexiSmartBean,
                    forBIArray, option, sumAssured);
            riskPremiumMortality_Q = Double.parseDouble(BIMAST
                    .getRiskPremiumMortalityReductionYield_Q());
            _riskPremiumMortality_Q = riskPremiumMortality_Q;
            // System.out.println("13.   riskPremiumMortality_Q : "+riskPremiumMortality_Q);

			BIMAST.setServiceTaxReductionYield_R(flexiSmartBean,serviceTax);
            serviceTax_R = Double.parseDouble(BIMAST
                    .getServiceTaxReductionYield_R());
            _serviceTax_R = serviceTax_R;
            // System.out.println("14.   serviceTax_R : "+serviceTax_R);

            BIMAST.setGuarateedAdditionToPolicyAccountReductionYield_S(flexiSmartBean);
            guarateedAdditionToPolicyAccount_S = Double.parseDouble(BIMAST
                    .getGuarateedAdditionToPolicyAccountReductionYield_S());
            _guarateedAdditionToPolicyAccount_S = guarateedAdditionToPolicyAccount_S;
            // System.out.println("15.   guarateedAdditionToPolicyAccount_S : "+guarateedAdditionToPolicyAccount_S);

            BIMAST.setExtraAdditionToPolicyAccountReductionYield_T(flexiSmartBean);
            extraAdditionToPolicyAccount_T = Double.parseDouble(BIMAST
                    .getExtraAdditionToPolicyAccountRedutionYield_T());
            _extraAdditionToPolicyAccount_T = extraAdditionToPolicyAccount_T;
            // System.out.println("16.   extraAdditionToPolicyAccount_T : "+extraAdditionToPolicyAccount_T);

            BIMAST.setFMCReductionYield_U();
            FMC_U = Double.parseDouble(BIMAST.getFMCReductionYield_U());
            _FMC_U = FMC_U;
            // System.out.println("17.   FMC_U : "+FMC_U);

			BIMAST.setServiceTaxOnFMCReductionYield_V(flexiSmartBean,serviceTax);
            serviceTaxOnFMC_V = Double.parseDouble(BIMAST
                    .getServiceTaxOnFMCReductionYield_V());
            _serviceTaxOnFMC_V = serviceTaxOnFMC_V;
            // System.out.println("18.   serviceTaxOnFMC_V : "+serviceTaxOnFMC_V);

            BIMAST.setClosingBalanceReductionYield_W();
            closingBalance_W = Double.parseDouble(BIMAST
                    .getClosingBalanceReductionYield_W());
            _closingBalance_W = closingBalance_W;
            // System.out.println("19.   closingBalance_W : "+closingBalance_W);

            BIMAST.setSurrenderChargesReductionYield_X(premiumAmt);
            surrenderCharges_X = Double.parseDouble(BIMAST
                    .getSurrenderChargesReductionYield_X());
            _surrenderCharges_X = surrenderCharges_X;
            // System.out.println("20.   surrenderCharges_X : "+surrenderCharges_X);

            BIMAST.setSurrenderValueReductionYield_Y(flexiSmartBean);
            surrenderValue_Y = Double.parseDouble(BIMAST
                    .getSurrenderValueReductionYield_Y());
            _surrenderValue_Y = surrenderValue_Y;
            // System.out.println("21.   surrenderValue_Y : "+surrenderValue_Y);

            BIMAST.setDeathBenefitReductionYield_Z(option, sumAssured);
            deathBenefit_Z = Double.parseDouble(BIMAST
                    .getDeathBenefitReductionYield_Z());
            _deathBenefit_Z = deathBenefit_Z;
            // System.out.println("22.   deathBenefit_Z : "+deathBenefit_Z);

            BIMAST.setOpeningBalanceReductionYield_AA(_closingBalance_AH);
            openingBalance_AA = Double.parseDouble(BIMAST
                    .getOpeningBalanceReductionYield_AA());
            _openingBalance_AA = openingBalance_AA;
            // System.out.println("23.   openingBalance_AA : "+openingBalance_AA);

            BIMAST.setRiskPremiumMortalityReductionYield_AB(flexiSmartBean,
                    forBIArray, option, sumAssured);
            riskPremium_AB = Double.parseDouble(BIMAST
                    .getRiskPremiumMortalityReductionYield_AB());
            _riskPremium_AB = riskPremium_AB;
            // System.out.println("24.   riskPremiumMortality_AB : "+riskPremium_AB);

			BIMAST.setServiceTaxReductionYield_AC(flexiSmartBean,serviceTax);
            serviceTax_AC = Double.parseDouble(BIMAST
                    .getServiceTaxReductionYield_AC());
            _serviceTax_AC = serviceTax_AC;
            // System.out.println("25.   serviceTax_AC : "+serviceTax_AC);

            BIMAST.setGuarateedAdditionToPolicyAccountReductionYield_AD(flexiSmartBean);
            guarateedAdditionToPolicyAccount_AD = Double.parseDouble(BIMAST
                    .getGuarateedAdditionToPolicyAccountReductionYield_AD());
            _guarateedAdditionToPolicyAccount_AD = guarateedAdditionToPolicyAccount_AD;
            // System.out.println("26.   guarateedAdditionToPolicyAccount_AD : "+guarateedAdditionToPolicyAccount_AD);

            BIMAST.setExtraAdditionToPolicyAccountReductionYield_AE(flexiSmartBean);
            extraAdditionToPolicyAccount_AE = Double.parseDouble(BIMAST
                    .getExtraAdditionToPolicyAccountReductionYield_AE());
            _extraAdditionToPolicyAccount_AE = extraAdditionToPolicyAccount_AE;
            // System.out.println("27.   extraAdditionToPolicyAccount_AE : "+extraAdditionToPolicyAccount_AE);

            BIMAST.setFMCReductionYield_AF();
            FMC_AF = Double.parseDouble(BIMAST.getFMCReductionYield_AF());
            _FMC_AF = FMC_AF;
            // System.out.println("28.   FMC_AF : "+FMC_AF);

			BIMAST.setServiceTaxOnFMCReductionYield_AG(flexiSmartBean,serviceTax);
            serviceTaxOnFMC_AG = Double.parseDouble(BIMAST
                    .getServiceTaxOnFMCReductionYield_AG());
            _serviceTaxOnFMC_AG = serviceTaxOnFMC_AG;
            // System.out.println("29.   serviceTaxOnFMC_AG : "+serviceTaxOnFMC_AG);

            BIMAST.setMonth_BB(monthNumber);
            month_BB = Integer.parseInt(BIMAST.getMonth_BB());
            _month_BB = month_BB;
            // System.out.println("month_bb "+month_BB);

            BIMAST.setReductionYield_AO(policyTerm, _closingBalance_AH);
            reductionYield_AO = Double.parseDouble(BIMAST
                    .getReductionYield_AO());
            _reductionYield_AO = reductionYield_AO;
            // System.out.println("reductionYield_BQ "+reductionYield_AO);

            BIMAST.setClosingBalanceReductionYield_AH();
            closingBalance_AH = Double.parseDouble(BIMAST
                    .getClosingBalanceReductionYield_AH());
            _closingBalance_AH = closingBalance_AH;
            // System.out.println("30.   closingBalance_AH : "+closingBalance_AH);

            BIMAST.setSurrenderChargesReductionYield_AI(premiumAmt);
            surrenderCharges_AI = Double.parseDouble(BIMAST
                    .getSurrenderChargesReductionYield_AI());
            _surrenderCharges_AI = surrenderCharges_AI;
            // System.out.println("31.   surrenderCharges_AI : "+surrenderCharges_AI);

            BIMAST.setSurrenderValueReductionYield_AJ(flexiSmartBean);
            surrenderValue_AJ = Double.parseDouble(BIMAST
                    .getSurrenderValueReductionYield_AJ());
            _surrenderValue_AJ = surrenderValue_AJ;
            // System.out.println("32.   surrenderValue_AJ : "+surrenderValue_AJ);

            BIMAST.setDeathBenefitReductionYield_AK(option, sumAssured);
            deathBenefit_AK = Double.parseDouble(BIMAST
                    .getDeathBenefitReductionYield_AK());
            _deathBenefit_AK = deathBenefit_AK;
            // System.out.println("33.   deathBenefit_AK : "+deathBenefit_AK);

            monthNumber++;

            List_AO.add(commonForAllProd.roundUp_Level2(commonForAllProd
                    .getStringWithout_E(reductionYield_AO)));
            // //System.out.println("***********************************************************************************************************");
        }

        // System.out.println("List_AO.size "+List_AO.size());
        // System.out.println("List_BD "+List_AO);
        double monthlyIRR = BIMAST.irr(List_AO, 0.004);
        // System.out.println("monthlyIRR "+monthlyIRR);

        BIMAST.setIRRAnnual_AP(monthlyIRR);
        irrAnnual_AP = Double.parseDouble(BIMAST.getIRRAnnual_AP());
        _irrAnnual_AP = irrAnnual_AP;
        // System.out.println("irrAnnual_BQ "+irrAnnual_AP);

        double netYield8Pr = (irrAnnual_AP * 100);

        BIMAST.setReductionInYieldMaturityAt(prop.int2);
        reductionInYieldMaturityAt = Double.parseDouble(BIMAST
                .getReductionInYieldMaturityAt());
        _reductionInYieldMaturityAt = reductionInYieldMaturityAt;
        // System.out.println("reductionInYieldMaturityAt "+reductionInYieldMaturityAt);

        // System.out.println("****************************** Final Output ****************************************");
        // System.out.println("Sum Assured -> "+commonForAllProd.getStringWithout_E(flexiSmartBean.getSumAssured()));
        // System.out.println("Fund Value @6% is Rs.-> "+commonForAllProd.getRoundUp((commonForAllProd.getStringWithout_E(closingBalance_W))));
        // System.out.println("Fund Value @10% -> "+commonForAllProd.getRound((commonForAllProd.getStringWithout_E(closingBalance_AH))));
        // System.out.println("***********************************************************************************");

        //		intent.putExtra("op","Sum Assured is Rs." + currencyFormat.format(Double.parseDouble(commonForAllProd.getStringWithout_E(flexiSmartBean.getSumAssured()))));
        //		intent.putExtra("op1","Maturity Benefit Payable @ 4% is Rs."+  currencyFormat.format(Double.parseDouble(commonForAllProd.getRoundUp((commonForAllProd.getStringWithout_E(closingBalance_W))))));
        //		intent.putExtra("op2","Maturity Benefit Payable @ 8% is Rs." +  currencyFormat.format(Double.parseDouble(commonForAllProd.getRoundUp((commonForAllProd.getStringWithout_E(closingBalance_AH))))));
        // startActivity(intent);

        // System.out.println("Net Yield at Maturity"+commonForAllProd.roundUp_Level2(commonForAllProd.getStringWithout_E(reductionInYieldMaturityAt)));
        // System.out.println("Net Yield at Maturity"+commonForAllProd.roundUp_Level2(commonForAllProd.getStringWithout_E(netYield8Pr)));
        return new String[]{
                commonForAllProd.roundUp_Level2(commonForAllProd
                        .getStringWithout_E(reductionInYieldMaturityAt)),
                commonForAllProd.roundUp_Level2(commonForAllProd
                        .getStringWithout_E(netYield8Pr))};
    }
*/
    private String[] getOutputReductionInYield(String SheetName, FlexiSmartPlusBean flexiSmartBean) {

        CommonForAllProd commonForAllProd = new CommonForAllProd();
        FlexiSmartPlusProperties prop = new FlexiSmartPlusProperties();
        //		Intent intent = new Intent(getApplicationContext(),success.class);
        ArrayList<String> List_AO = new ArrayList<String>();


        //Output Variable Declaration
        int _month_E = 0;
        int _year_F = 0;
        String _policyInForce_G = "Y";
        int _age_H = 0;
        int _premiumHolidayFlag_I = 0;


        double _premium_j = 0,
                _topUpPremium_K = 0,
                _premiumAllocationCharge_L = 0,
                _policyAdministrationCharge_M = 0,
                _serviceTax_N = 0,
                _totalDeduction_O = 0,
                _openingBalance_P = 0,
                _riskPremiumMortality_Q = 0,
                _serviceTax_R = 0,
                _guarateedAdditionToPolicyAccount_S = 0,
                _extraAdditionToPolicyAccount_T = 0,
                _FMC_U = 0,
                _serviceTaxOnFMC_V = 0,
                _closingBalance_W = 0,
                _surrenderCharges_X = 0,
                _surrenderValue_Y = 0,
                _deathBenefit_Z = 0,
                _openingBalance_AA = 0,
                _riskPremium_AB = 0,
                _serviceTax_AC = 0,
                _guarateedAdditionToPolicyAccount_AD = 0,
                _extraAdditionToPolicyAccount_AE = 0,
                _FMC_AF = 0,
                _serviceTaxOnFMC_AG = 0,
                _closingBalance_AH = 0,
                _surrenderCharges_AI = 0,
                _surrenderValue_AJ = 0,
                _deathBenefit_AK = 0,
                _month_BB = 0,
                _reductionYield_AO = 0,
                _irrAnnual_AP = 0,
                _reductionInYieldMaturityAt = 0;


        //Temp Variable Declaretion
        int month_E = 0;
        int year_F = 0;
        String policyInForce_G = "Y";
        int age_H = 0;
        int premiumHolidayFlag_I = 0;


        double premium_j = 0,
                topUpPremium_K = 0,
                premiumAllocationCharge_L = 0,
                policyAdministrationCharge_M = 0,
                serviceTax_N = 0,
                totalDeduction_O = 0,
                openingBalance_P = 0,
                riskPremiumMortality_Q = 0,
                serviceTax_R = 0,
                guarateedAdditionToPolicyAccount_S = 0,
                extraAdditionToPolicyAccount_T = 0,
                FMC_U = 0,
                serviceTaxOnFMC_V = 0,
                closingBalance_W = 0,
                surrenderCharges_X = 0,
                surrenderValue_Y = 0,
                deathBenefit_Z = 0,
                openingBalance_AA = 0,
                riskPremium_AB = 0,
                serviceTax_AC = 0,
                guarateedAdditionToPolicyAccount_AD = 0,
                extraAdditionToPolicyAccount_AE = 0,
                FMC_AF = 0,
                serviceTaxOnFMC_AG = 0,
                closingBalance_AH = 0,
                surrenderCharges_AI = 0,
                surrenderValue_AJ = 0,
                deathBenefit_AK = 0,
                month_BB = 0,
                reductionYield_AO = 0,
                irrAnnual_AP = 0,
                reductionInYieldMaturityAt = 0;


        //From GUI Input
        int ageAtEntry = flexiSmartBean.getAgeAtEntry();
        flexiSmartBean.setSumAssured();
        flexiSmartBean.setEffectiveTopUpPrem(flexiSmartBean.getTopUpPremAmt(), flexiSmartBean.getPremiumAmount());
        double effectiveTopUpPrem = flexiSmartBean.getEffectiveTopUpPrem();

        //Declaration of method Variables/Object required for calculation
        FlexiSmartPlusBusinessLogic BIMAST = new FlexiSmartPlusBusinessLogic();
        String[] forBIArray = BIMAST.getForBIArr(prop.riskPremiumRate);
        int policyTerm = flexiSmartBean.getPolicyTerm();
        boolean jkResident = flexiSmartBean.getJkResident();
        double serviceTax = 0;
        boolean state = flexiSmartBean.isKerlaDisc();
        String option = flexiSmartBean.getOption();
        double sumAssured = flexiSmartBean.getSumAssured();
        double premiumAmt = flexiSmartBean.getPremiumAmount();

        int rowNumber = 0, monthNumber = 0;

        //		for (int i = 1; i < 3; i++)
        for (int i = 0; i <= (policyTerm * 12); i++) {
            rowNumber++;
            ////System.out.println("********************************************* "+i+" Row Output *********************************************");
            BIMAST.setMonth_E(rowNumber);
            month_E = Integer.parseInt(BIMAST.getMonth_E());
            _month_E = month_E;
            //			System.out.println("1.   Month_E : "+month_E);

            BIMAST.setYear_F();
            year_F = Integer.parseInt(BIMAST.getYear_F());
            _year_F = year_F;
            //			System.out.println("2.   Year_F : "+year_F);

//			Added By Saurabh Jain on 20/06/2019 Start
            if (state == true && _year_F <= 2) {
                serviceTax = 0.19;
            } else {
                serviceTax = 0.18;
            }

//			Added By Saurabh Jain on 20/06/2019 End
            policyInForce_G = BIMAST.getPolicyInForce_G();
            _policyInForce_G = BIMAST.getPolicyInForce_G();
            //			System.out.println("3.   PolicyInForce_G : "+BIMAST.getPolicyInForce_G());

            BIMAST.setAge_H(ageAtEntry);
            age_H = Integer.parseInt(BIMAST.getAge_H());
            _age_H = age_H;
            //			System.out.println("4.   Age_H : "+age_H);

            BIMAST.setPremiumHolidayFlag_I(flexiSmartBean.getPremiumHolidayStatus(), flexiSmartBean.getPolicyYear(), flexiSmartBean.getPremHolidayTerm());
            premiumHolidayFlag_I = Integer.parseInt(BIMAST.getPremiumHolidayFlag_I());
            _premiumHolidayFlag_I = premiumHolidayFlag_I;
            //			System.out.println("5.   PremiumHolidayFlag_I : "+premiumHolidayFlag_I);

            BIMAST.setPremium_J(flexiSmartBean.getPolicyTerm(), flexiSmartBean.getPF(), (flexiSmartBean.getPremiumAmount() * flexiSmartBean.getPF()));
            premium_j = Double.parseDouble(BIMAST.getPremium_J());
            _premium_j = premium_j;
            //			System.out.println("6.   Premium_J : "+premium_j);

            BIMAST.setTopUpPremium_K(effectiveTopUpPrem, flexiSmartBean);
            topUpPremium_K = Double.parseDouble(BIMAST.getTopUpPremium_K());
            _topUpPremium_K = topUpPremium_K;
            //			System.out.println("7.   TopUpPremium_K : "+topUpPremium_K);

            BIMAST.setPremiumAllocationChargeReductionYield_L(flexiSmartBean);
            premiumAllocationCharge_L = Double.parseDouble(BIMAST.getPremiumAllocationChargeReductionYield_L());
            _premiumAllocationCharge_L = premiumAllocationCharge_L;
            //			System.out.println("8.   premiumAllocationCharge_L : "+premiumAllocationCharge_L);

            BIMAST.setPolicyAdministartionCharge_M();
            policyAdministrationCharge_M = Double.parseDouble(BIMAST.getPolicyAdministrationCharge_M());
            _policyAdministrationCharge_M = policyAdministrationCharge_M;
            //			System.out.println("9.   policyAdministrationCharge_M : "+policyAdministrationCharge_M);

            BIMAST.setServiceTaxRedutionYield_N(flexiSmartBean, serviceTax);
            serviceTax_N = Double.parseDouble(BIMAST.getServiceTaxReductionYield_N());
            _serviceTax_N = serviceTax_N;
            //			System.out.println("10.   serviceTax_N : "+serviceTax_N);

            BIMAST.setTotalDeductionsReductionYield_O();
            totalDeduction_O = Double.parseDouble(BIMAST.getTotalDeductionsReductionYield_O());
            _totalDeduction_O = totalDeduction_O;
            //			System.out.println("11.   totalDeduction_O : "+totalDeduction_O);

            BIMAST.setOpeningBalanceReductionYield_P(_closingBalance_W);
            openingBalance_P = Double.parseDouble(BIMAST.getOpeningBalanceReductionYield_P());
            _openingBalance_P = openingBalance_P;
            //			System.out.println("12.   openingBalance_P : "+openingBalance_P);

            BIMAST.setRiskPremiumMortalityReductionYield_Q(flexiSmartBean, forBIArray, option, sumAssured);
            riskPremiumMortality_Q = Double.parseDouble(BIMAST.getRiskPremiumMortalityReductionYield_Q());
            _riskPremiumMortality_Q = riskPremiumMortality_Q;
            //			System.out.println("13.   riskPremiumMortality_Q : "+riskPremiumMortality_Q);

            BIMAST.setServiceTaxReductionYield_R(flexiSmartBean, serviceTax);
            serviceTax_R = Double.parseDouble(BIMAST.getServiceTaxReductionYield_R());
            _serviceTax_R = serviceTax_R;
            //			System.out.println("14.   serviceTax_R : "+serviceTax_R);

            BIMAST.setGuarateedAdditionToPolicyAccountReductionYield_S(flexiSmartBean);
            guarateedAdditionToPolicyAccount_S = Double.parseDouble(BIMAST.getGuarateedAdditionToPolicyAccountReductionYield_S());
            _guarateedAdditionToPolicyAccount_S = guarateedAdditionToPolicyAccount_S;
            //			System.out.println("15.   guarateedAdditionToPolicyAccount_S : "+guarateedAdditionToPolicyAccount_S);

            BIMAST.setExtraAdditionToPolicyAccountReductionYield_T(flexiSmartBean);
            extraAdditionToPolicyAccount_T = Double.parseDouble(BIMAST.getExtraAdditionToPolicyAccountRedutionYield_T());
            _extraAdditionToPolicyAccount_T = extraAdditionToPolicyAccount_T;
            //			System.out.println("16.   extraAdditionToPolicyAccount_T : "+extraAdditionToPolicyAccount_T);

            BIMAST.setFMCReductionYield_U();
            FMC_U = Double.parseDouble(BIMAST.getFMCReductionYield_U());
            _FMC_U = FMC_U;
            //			System.out.println("17.   FMC_U : "+FMC_U);

            BIMAST.setServiceTaxOnFMCReductionYield_V(flexiSmartBean, serviceTax);
            serviceTaxOnFMC_V = Double.parseDouble(BIMAST.getServiceTaxOnFMCReductionYield_V());
            _serviceTaxOnFMC_V = serviceTaxOnFMC_V;
            //			System.out.println("18.   serviceTaxOnFMC_V : "+serviceTaxOnFMC_V);

            BIMAST.setClosingBalanceReductionYield_W();
            closingBalance_W = Double.parseDouble(BIMAST.getClosingBalanceReductionYield_W());
            _closingBalance_W = closingBalance_W;
            //			System.out.println("19.   closingBalance_W : "+closingBalance_W);

            BIMAST.setSurrenderChargesReductionYield_X(premiumAmt);
            surrenderCharges_X = Double.parseDouble(BIMAST.getSurrenderChargesReductionYield_X());
            _surrenderCharges_X = surrenderCharges_X;
            //			System.out.println("20.   surrenderCharges_X : "+surrenderCharges_X);

            BIMAST.setSurrenderValueReductionYield_Y(flexiSmartBean);
            surrenderValue_Y = Double.parseDouble(BIMAST.getSurrenderValueReductionYield_Y());
            _surrenderValue_Y = surrenderValue_Y;
            //			System.out.println("21.   surrenderValue_Y : "+surrenderValue_Y);

            BIMAST.setDeathBenefitReductionYield_Z(option, sumAssured);
            deathBenefit_Z = Double.parseDouble(BIMAST.getDeathBenefitReductionYield_Z());
            _deathBenefit_Z = deathBenefit_Z;
            //			System.out.println("22.   deathBenefit_Z : "+deathBenefit_Z);

            BIMAST.setOpeningBalanceReductionYield_AA(_closingBalance_AH);
            openingBalance_AA = Double.parseDouble(BIMAST.getOpeningBalanceReductionYield_AA());
            _openingBalance_AA = openingBalance_AA;
            //			System.out.println("23.   openingBalance_AA : "+openingBalance_AA);

            BIMAST.setRiskPremiumMortalityReductionYield_AB(flexiSmartBean, forBIArray, option, sumAssured);
            riskPremium_AB = Double.parseDouble(BIMAST.getRiskPremiumMortalityReductionYield_AB());
            _riskPremium_AB = riskPremium_AB;
            //			System.out.println("24.   riskPremiumMortality_AB : "+riskPremium_AB);

            BIMAST.setServiceTaxReductionYield_AC(flexiSmartBean, serviceTax);
            serviceTax_AC = Double.parseDouble(BIMAST.getServiceTaxReductionYield_AC());
            _serviceTax_AC = serviceTax_AC;
            //			System.out.println("25.   serviceTax_AC : "+serviceTax_AC);

            BIMAST.setGuarateedAdditionToPolicyAccountReductionYield_AD(flexiSmartBean);
            guarateedAdditionToPolicyAccount_AD = Double.parseDouble(BIMAST.getGuarateedAdditionToPolicyAccountReductionYield_AD());
            _guarateedAdditionToPolicyAccount_AD = guarateedAdditionToPolicyAccount_AD;
            //			System.out.println("26.   guarateedAdditionToPolicyAccount_AD : "+guarateedAdditionToPolicyAccount_AD);

            BIMAST.setExtraAdditionToPolicyAccountReductionYield_AE(flexiSmartBean);
            extraAdditionToPolicyAccount_AE = Double.parseDouble(BIMAST.getExtraAdditionToPolicyAccountReductionYield_AE());
            _extraAdditionToPolicyAccount_AE = extraAdditionToPolicyAccount_AE;
            //			System.out.println("27.   extraAdditionToPolicyAccount_AE : "+extraAdditionToPolicyAccount_AE);

            BIMAST.setFMCReductionYield_AF();
            FMC_AF = Double.parseDouble(BIMAST.getFMCReductionYield_AF());
            _FMC_AF = FMC_AF;
            //			System.out.println("28.   FMC_AF : "+FMC_AF);

            BIMAST.setServiceTaxOnFMCReductionYield_AG(flexiSmartBean, serviceTax);
            serviceTaxOnFMC_AG = Double.parseDouble(BIMAST.getServiceTaxOnFMCReductionYield_AG());
            _serviceTaxOnFMC_AG = serviceTaxOnFMC_AG;
            //			System.out.println("29.   serviceTaxOnFMC_AG : "+serviceTaxOnFMC_AG);

            BIMAST.setMonth_BB(monthNumber);
            month_BB = Integer.parseInt(BIMAST.getMonth_BB());
            _month_BB = month_BB;
            //	           System.out.println("month_bb "+month_BB);


            BIMAST.setReductionYield_AO(policyTerm, _closingBalance_AH);
            reductionYield_AO = Double.parseDouble(BIMAST.getReductionYield_AO());
            _reductionYield_AO = reductionYield_AO;
            //            System.out.println("reductionYield_BQ "+reductionYield_AO);


            BIMAST.setClosingBalanceReductionYield_AH();
            closingBalance_AH = Double.parseDouble(BIMAST.getClosingBalanceReductionYield_AH());
            _closingBalance_AH = closingBalance_AH;
            //			System.out.println("30.   closingBalance_AH : "+closingBalance_AH);

            BIMAST.setSurrenderChargesReductionYield_AI(premiumAmt);
            surrenderCharges_AI = Double.parseDouble(BIMAST.getSurrenderChargesReductionYield_AI());
            _surrenderCharges_AI = surrenderCharges_AI;
            //			System.out.println("31.   surrenderCharges_AI : "+surrenderCharges_AI);

            BIMAST.setSurrenderValueReductionYield_AJ(flexiSmartBean);
            surrenderValue_AJ = Double.parseDouble(BIMAST.getSurrenderValueReductionYield_AJ());
            _surrenderValue_AJ = surrenderValue_AJ;
            //			System.out.println("32.   surrenderValue_AJ : "+surrenderValue_AJ);

            BIMAST.setDeathBenefitReductionYield_AK(option, sumAssured);
            deathBenefit_AK = Double.parseDouble(BIMAST.getDeathBenefitReductionYield_AK());
            _deathBenefit_AK = deathBenefit_AK;
            //			System.out.println("33.   deathBenefit_AK : "+deathBenefit_AK);

            monthNumber++;

            List_AO.add(commonForAllProd.roundUp_Level2(commonForAllProd.getStringWithout_E(reductionYield_AO)));
            ////System.out.println("***********************************************************************************************************");
        }

        //		System.out.println("List_AO.size "+List_AO.size());
        //		System.out.println("List_BD "+List_AO);
        double monthlyIRR = BIMAST.irr(List_AO, 0.004);
//				System.out.println("monthlyIRR "+monthlyIRR);

        BIMAST.setIRRAnnual_AP(monthlyIRR);
        irrAnnual_AP = Double.parseDouble(BIMAST.getIRRAnnual_AP());
        _irrAnnual_AP = irrAnnual_AP;
//				System.out.println("irrAnnual_BQ "+irrAnnual_AP);

        double netYield8Pr = (irrAnnual_AP * 100);

        BIMAST.setReductionInYieldMaturityAt(prop.int2);
        reductionInYieldMaturityAt = Double.parseDouble(BIMAST.getReductionInYieldMaturityAt());
        _reductionInYieldMaturityAt = reductionInYieldMaturityAt;
        //		System.out.println("reductionInYieldMaturityAt "+reductionInYieldMaturityAt);


        //			System.out.println("****************************** Final Output ****************************************");
        //			System.out.println("Sum Assured -> "+commonForAllProd.getStringWithout_E(flexiSmartBean.getSumAssured()));
        //				System.out.println("Fund Value @6% is Rs.-> "+commonForAllProd.getRoundUp((commonForAllProd.getStringWithout_E(closingBalance_W))));
        //				System.out.println("Fund Value @10% -> "+commonForAllProd.getRound((commonForAllProd.getStringWithout_E(closingBalance_AH))));
        //				System.out.println("***********************************************************************************");

        //		intent.putExtra("op","Sum Assured is Rs." + currencyFormat.format(Double.parseDouble(commonForAllProd.getStringWithout_E(flexiSmartBean.getSumAssured()))));
        //		intent.putExtra("op1","Maturity Benefit Payable @ 4% is Rs."+  currencyFormat.format(Double.parseDouble(commonForAllProd.getRoundUp((commonForAllProd.getStringWithout_E(closingBalance_W))))));
        //		intent.putExtra("op2","Maturity Benefit Payable @ 8% is Rs." +  currencyFormat.format(Double.parseDouble(commonForAllProd.getRoundUp((commonForAllProd.getStringWithout_E(closingBalance_AH))))));
        //		startActivity(intent);


//		System.out.println("Net Yield at Maturity"+commonForAllProd.roundUp_Level2(commonForAllProd.getStringWithout_E(reductionInYieldMaturityAt)));
//		System.out.println("Net Yield at Maturity"+commonForAllProd.roundUp_Level2(commonForAllProd.getStringWithout_E(netYield8Pr)));
        return new String[]{commonForAllProd.roundUp_Level2(commonForAllProd.getStringWithout_E(reductionInYieldMaturityAt)), commonForAllProd.roundUp_Level2(commonForAllProd.getStringWithout_E(netYield8Pr))};
    }

    /************************************* BI ends here **************************************/

    // private String[] getOutputReductionInYield(String SheetName,
    // FlexiSmartPlusBean flexiSmartBean) {
    //
    // BI_CommonForAllProd commonForAllProd = new BI_CommonForAllProd();
    // FlexiSmartPlusProperties prop = new FlexiSmartPlusProperties();
    // // Intent intent = new Intent(getApplicationContext(),success.class);
    // ArrayList<String> List_AO = new ArrayList<String>();
    //
    // // Output Variable Declaration
    // int _month_E = 0;
    // int _year_F = 0;
    // String _policyInForce_G = "Y";
    // int _age_H = 0;
    // int _premiumHolidayFlag_I = 0;
    //
    // double _premium_j = 0, _topUpPremium_K = 0, _premiumAllocationCharge_L =
    // 0, _policyAdministrationCharge_M = 0, _serviceTax_N = 0,
    // _totalDeduction_O = 0, _openingBalance_P = 0, _riskPremiumMortality_Q =
    // 0, _serviceTax_R = 0, _guarateedAdditionToPolicyAccount_S = 0,
    // _extraAdditionToPolicyAccount_T = 0, _FMC_U = 0, _serviceTaxOnFMC_V = 0,
    // _closingBalance_W = 0, _surrenderCharges_X = 0, _surrenderValue_Y = 0,
    // _deathBenefit_Z = 0, _openingBalance_AA = 0, _riskPremium_AB = 0,
    // _serviceTax_AC = 0, _guarateedAdditionToPolicyAccount_AD = 0,
    // _extraAdditionToPolicyAccount_AE = 0, _FMC_AF = 0, _serviceTaxOnFMC_AG =
    // 0, _closingBalance_AH = 0, _surrenderCharges_AI = 0, _surrenderValue_AJ =
    // 0, _deathBenefit_AK = 0, _month_BB = 0, _reductionYield_AO = 0,
    // _irrAnnual_AP = 0, _reductionInYieldMaturityAt = 0;
    //
    // // Temp Variable Declaretion
    // int month_E = 0;
    // int year_F = 0;
    // String policyInForce_G = "Y";
    // int age_H = 0;
    // int premiumHolidayFlag_I = 0;
    //
    // double premium_j = 0, topUpPremium_K = 0, premiumAllocationCharge_L = 0,
    // policyAdministrationCharge_M = 0, serviceTax_N = 0, totalDeduction_O = 0,
    // openingBalance_P = 0, riskPremiumMortality_Q = 0, serviceTax_R = 0,
    // guarateedAdditionToPolicyAccount_S = 0, extraAdditionToPolicyAccount_T =
    // 0, FMC_U = 0, serviceTaxOnFMC_V = 0, closingBalance_W = 0,
    // surrenderCharges_X = 0, surrenderValue_Y = 0, deathBenefit_Z = 0,
    // openingBalance_AA = 0, riskPremium_AB = 0, serviceTax_AC = 0,
    // guarateedAdditionToPolicyAccount_AD = 0, extraAdditionToPolicyAccount_AE
    // = 0, FMC_AF = 0, serviceTaxOnFMC_AG = 0, closingBalance_AH = 0,
    // surrenderCharges_AI = 0, surrenderValue_AJ = 0, deathBenefit_AK = 0,
    // month_BB = 0, reductionYield_AO = 0, irrAnnual_AP = 0,
    // reductionInYieldMaturityAt = 0;
    //
    // // From GUI Input
    // int ageAtEntry = flexiSmartBean.getAgeAtEntry();
    // flexiSmartBean.setSumAssured();
    // flexiSmartBean.setEffectiveTopUpPrem(flexiSmartBean.getTopUpPremAmt(),
    // flexiSmartBean.getPremiumAmount());
    // double effectiveTopUpPrem = flexiSmartBean.getEffectiveTopUpPrem();
    //
    // // Declaration of method Variables/Object required for calculation
    // FlexiSmartPlusBusinessLogic BIMAST = new FlexiSmartPlusBusinessLogic();
    // String[] forBIArray = BIMAST.getForBIArr(prop.riskPremiumRate);
    // int policyTerm = flexiSmartBean.getPolicyTerm();
    // boolean jkResident = flexiSmartBean.getJkResident();
    // String option = flexiSmartBean.getOption();
    // double sumAssured = flexiSmartBean.getSumAssured();
    // double premiumAmt = flexiSmartBean.getPremiumAmount();
    //
    // int rowNumber = 0, monthNumber = 0;
    //
    // // for (int i = 1; i < 3; i++)
    // for (int i = 0; i <= (policyTerm * 12); i++) {
    // rowNumber++;
    // //
    // //System.out.println("********************************************* "+i+" Row Output *********************************************");
    // BIMAST.setMonth_E(rowNumber);
    // month_E = Integer.parseInt(BIMAST.getMonth_E());
    // _month_E = month_E;
    // // System.out.println("1.   Month_E : "+month_E);
    //
    // BIMAST.setYear_F();
    // year_F = Integer.parseInt(BIMAST.getYear_F());
    // _year_F = year_F;
    // // System.out.println("2.   Year_F : "+year_F);
    //
    // policyInForce_G = BIMAST.getPolicyInForce_G();
    // _policyInForce_G = BIMAST.getPolicyInForce_G();
    // //
    // System.out.println("3.   PolicyInForce_G : "+BIMAST.getPolicyInForce_G());
    //
    // BIMAST.setAge_H(ageAtEntry);
    // age_H = Integer.parseInt(BIMAST.getAge_H());
    // _age_H = age_H;
    // // System.out.println("4.   Age_H : "+age_H);
    //
    // BIMAST.setPremiumHolidayFlag_I(
    // flexiSmartBean.getPremiumHolidayStatus(),
    // flexiSmartBean.getPolicyYear(),
    // flexiSmartBean.getPremHolidayTerm());
    // premiumHolidayFlag_I = Integer.parseInt(BIMAST
    // .getPremiumHolidayFlag_I());
    // _premiumHolidayFlag_I = premiumHolidayFlag_I;
    // //
    // System.out.println("5.   PremiumHolidayFlag_I : "+premiumHolidayFlag_I);
    //
    // BIMAST.setPremium_J(
    // flexiSmartBean.getPolicyTerm(),
    // flexiSmartBean.getPF(),
    // (flexiSmartBean.getPremiumAmount() * flexiSmartBean.getPF()));
    // premium_j = Double.parseDouble(BIMAST.getPremium_J());
    // _premium_j = premium_j;
    // // System.out.println("6.   Premium_J : "+premium_j);
    //
    // BIMAST.setTopUpPremium_K(effectiveTopUpPrem, flexiSmartBean);
    // topUpPremium_K = Double.parseDouble(BIMAST.getTopUpPremium_K());
    // _topUpPremium_K = topUpPremium_K;
    // // System.out.println("7.   TopUpPremium_K : "+topUpPremium_K);
    //
    // BIMAST.setPremiumAllocationChargeReductionYield_L(flexiSmartBean);
    // premiumAllocationCharge_L = Double.parseDouble(BIMAST
    // .getPremiumAllocationChargeReductionYield_L());
    // _premiumAllocationCharge_L = premiumAllocationCharge_L;
    // //
    // System.out.println("8.   premiumAllocationCharge_L : "+premiumAllocationCharge_L);
    //
    // BIMAST.setPolicyAdministartionCharge_M();
    // policyAdministrationCharge_M = Double.parseDouble(BIMAST
    // .getPolicyAdministrationCharge_M());
    // _policyAdministrationCharge_M = policyAdministrationCharge_M;
    // //
    // System.out.println("9.   policyAdministrationCharge_M : "+policyAdministrationCharge_M);
    //
    // BIMAST.setServiceTaxRedutionYield_N(flexiSmartBean);
    // serviceTax_N = Double.parseDouble(BIMAST
    // .getServiceTaxReductionYield_N());
    // _serviceTax_N = serviceTax_N;
    // // System.out.println("10.   serviceTax_N : "+serviceTax_N);
    //
    // BIMAST.setTotalDeductionsReductionYield_O();
    // totalDeduction_O = Double.parseDouble(BIMAST
    // .getTotalDeductionsReductionYield_O());
    // _totalDeduction_O = totalDeduction_O;
    // // System.out.println("11.   totalDeduction_O : "+totalDeduction_O);
    //
    // BIMAST.setOpeningBalanceReductionYield_P(_closingBalance_W);
    // openingBalance_P = Double.parseDouble(BIMAST
    // .getOpeningBalanceReductionYield_P());
    // _openingBalance_P = openingBalance_P;
    // // System.out.println("12.   openingBalance_P : "+openingBalance_P);
    //
    // BIMAST.setRiskPremiumMortalityReductionYield_Q(flexiSmartBean,
    // forBIArray, option, sumAssured);
    // riskPremiumMortality_Q = Double.parseDouble(BIMAST
    // .getRiskPremiumMortalityReductionYield_Q());
    // _riskPremiumMortality_Q = riskPremiumMortality_Q;
    // //
    // System.out.println("13.   riskPremiumMortality_Q : "+riskPremiumMortality_Q);
    //
    // BIMAST.setServiceTaxReductionYield_R(flexiSmartBean);
    // serviceTax_R = Double.parseDouble(BIMAST
    // .getServiceTaxReductionYield_R());
    // _serviceTax_R = serviceTax_R;
    // // System.out.println("14.   serviceTax_R : "+serviceTax_R);
    //
    // BIMAST.setGuarateedAdditionToPolicyAccountReductionYield_S(flexiSmartBean);
    // guarateedAdditionToPolicyAccount_S = Double.parseDouble(BIMAST
    // .getGuarateedAdditionToPolicyAccountReductionYield_S());
    // _guarateedAdditionToPolicyAccount_S = guarateedAdditionToPolicyAccount_S;
    // //
    // System.out.println("15.   guarateedAdditionToPolicyAccount_S : "+guarateedAdditionToPolicyAccount_S);
    //
    // BIMAST.setExtraAdditionToPolicyAccountReductionYield_T(flexiSmartBean);
    // extraAdditionToPolicyAccount_T = Double.parseDouble(BIMAST
    // .getExtraAdditionToPolicyAccountRedutionYield_T());
    // _extraAdditionToPolicyAccount_T = extraAdditionToPolicyAccount_T;
    // //
    // System.out.println("16.   extraAdditionToPolicyAccount_T : "+extraAdditionToPolicyAccount_T);
    //
    // BIMAST.setFMCReductionYield_U();
    // FMC_U = Double.parseDouble(BIMAST.getFMCReductionYield_U());
    // _FMC_U = FMC_U;
    // // System.out.println("17.   FMC_U : "+FMC_U);
    //
    // BIMAST.setServiceTaxOnFMCReductionYield_V(flexiSmartBean);
    // serviceTaxOnFMC_V = Double.parseDouble(BIMAST
    // .getServiceTaxOnFMCReductionYield_V());
    // _serviceTaxOnFMC_V = serviceTaxOnFMC_V;
    // // System.out.println("18.   serviceTaxOnFMC_V : "+serviceTaxOnFMC_V);
    //
    // BIMAST.setClosingBalanceReductionYield_W();
    // closingBalance_W = Double.parseDouble(BIMAST
    // .getClosingBalanceReductionYield_W());
    // _closingBalance_W = closingBalance_W;
    // // System.out.println("19.   closingBalance_W : "+closingBalance_W);
    //
    // BIMAST.setSurrenderChargesReductionYield_X(premiumAmt);
    // surrenderCharges_X = Double.parseDouble(BIMAST
    // .getSurrenderChargesReductionYield_X());
    // _surrenderCharges_X = surrenderCharges_X;
    // // System.out.println("20.   surrenderCharges_X : "+surrenderCharges_X);
    //
    // BIMAST.setSurrenderValueReductionYield_Y(flexiSmartBean);
    // surrenderValue_Y = Double.parseDouble(BIMAST
    // .getSurrenderValueReductionYield_Y());
    // _surrenderValue_Y = surrenderValue_Y;
    // // System.out.println("21.   surrenderValue_Y : "+surrenderValue_Y);
    //
    // BIMAST.setDeathBenefitReductionYield_Z(option, sumAssured);
    // deathBenefit_Z = Double.parseDouble(BIMAST
    // .getDeathBenefitReductionYield_Z());
    // _deathBenefit_Z = deathBenefit_Z;
    // // System.out.println("22.   deathBenefit_Z : "+deathBenefit_Z);
    //
    // BIMAST.setOpeningBalanceReductionYield_AA(_closingBalance_AH);
    // openingBalance_AA = Double.parseDouble(BIMAST
    // .getOpeningBalanceReductionYield_AA());
    // _openingBalance_AA = openingBalance_AA;
    // // System.out.println("23.   openingBalance_AA : "+openingBalance_AA);
    //
    // BIMAST.setRiskPremiumMortalityReductionYield_AB(flexiSmartBean,
    // forBIArray, option, sumAssured);
    // riskPremium_AB = Double.parseDouble(BIMAST
    // .getRiskPremiumMortalityReductionYield_AB());
    // _riskPremium_AB = riskPremium_AB;
    // // System.out.println("24.   riskPremiumMortality_AB : "+riskPremium_AB);
    //
    // BIMAST.setServiceTaxReductionYield_AC(flexiSmartBean);
    // serviceTax_AC = Double.parseDouble(BIMAST
    // .getServiceTaxReductionYield_AC());
    // _serviceTax_AC = serviceTax_AC;
    // // System.out.println("25.   serviceTax_AC : "+serviceTax_AC);
    //
    // BIMAST.setGuarateedAdditionToPolicyAccountReductionYield_AD(flexiSmartBean);
    // guarateedAdditionToPolicyAccount_AD = Double.parseDouble(BIMAST
    // .getGuarateedAdditionToPolicyAccountReductionYield_AD());
    // _guarateedAdditionToPolicyAccount_AD =
    // guarateedAdditionToPolicyAccount_AD;
    // //
    // System.out.println("26.   guarateedAdditionToPolicyAccount_AD : "+guarateedAdditionToPolicyAccount_AD);
    //
    // BIMAST.setExtraAdditionToPolicyAccountReductionYield_AE(flexiSmartBean);
    // extraAdditionToPolicyAccount_AE = Double.parseDouble(BIMAST
    // .getExtraAdditionToPolicyAccountReductionYield_AE());
    // _extraAdditionToPolicyAccount_AE = extraAdditionToPolicyAccount_AE;
    // //
    // System.out.println("27.   extraAdditionToPolicyAccount_AE : "+extraAdditionToPolicyAccount_AE);
    //
    // BIMAST.setFMCReductionYield_AF();
    // FMC_AF = Double.parseDouble(BIMAST.getFMCReductionYield_AF());
    // _FMC_AF = FMC_AF;
    // // System.out.println("28.   FMC_AF : "+FMC_AF);
    //
    // BIMAST.setServiceTaxOnFMCReductionYield_AG(flexiSmartBean);
    // serviceTaxOnFMC_AG = Double.parseDouble(BIMAST
    // .getServiceTaxOnFMCReductionYield_AG());
    // _serviceTaxOnFMC_AG = serviceTaxOnFMC_AG;
    // // System.out.println("29.   serviceTaxOnFMC_AG : "+serviceTaxOnFMC_AG);
    //
    // BIMAST.setMonth_BB(monthNumber);
    // month_BB = Integer.parseInt(BIMAST.getMonth_BB());
    // _month_BB = month_BB;
    // // System.out.println("month_bb "+month_BB);
    //
    // BIMAST.setReductionYield_AO(policyTerm, _closingBalance_AH);
    // reductionYield_AO = Double.parseDouble(BIMAST
    // .getReductionYield_AO());
    // _reductionYield_AO = reductionYield_AO;
    // // System.out.println("reductionYield_BQ "+reductionYield_AO);
    //
    // BIMAST.setClosingBalanceReductionYield_AH();
    // closingBalance_AH = Double.parseDouble(BIMAST
    // .getClosingBalanceReductionYield_AH());
    // _closingBalance_AH = closingBalance_AH;
    // // System.out.println("30.   closingBalance_AH : "+closingBalance_AH);
    //
    // BIMAST.setSurrenderChargesReductionYield_AI(premiumAmt);
    // surrenderCharges_AI = Double.parseDouble(BIMAST
    // .getSurrenderChargesReductionYield_AI());
    // _surrenderCharges_AI = surrenderCharges_AI;
    // //
    // System.out.println("31.   surrenderCharges_AI : "+surrenderCharges_AI);
    //
    // BIMAST.setSurrenderValueReductionYield_AJ(flexiSmartBean);
    // surrenderValue_AJ = Double.parseDouble(BIMAST
    // .getSurrenderValueReductionYield_AJ());
    // _surrenderValue_AJ = surrenderValue_AJ;
    // // System.out.println("32.   surrenderValue_AJ : "+surrenderValue_AJ);
    //
    // BIMAST.setDeathBenefitReductionYield_AK(option, sumAssured);
    // deathBenefit_AK = Double.parseDouble(BIMAST
    // .getDeathBenefitReductionYield_AK());
    // _deathBenefit_AK = deathBenefit_AK;
    // // System.out.println("33.   deathBenefit_AK : "+deathBenefit_AK);
    //
    // monthNumber++;
    //
    // List_AO.add(commonForAllProd.roundUp_Level2(commonForAllProd
    // .getStringWithout_E(reductionYield_AO)));
    // //
    // //System.out.println("***********************************************************************************************************");
    // }
    //
    // // System.out.println("List_AO.size "+List_AO.size());
    // // System.out.println("List_BD "+List_AO);
    //
    // double monthlyIRR = BIMAST.irr(List_AO, 0.004);
    //
    // // System.out.println("monthlyIRR "+monthlyIRR);
    //
    // BIMAST.setIRRAnnual_AP(monthlyIRR);
    // irrAnnual_AP = Double.parseDouble(BIMAST.getIRRAnnual_AP());
    // _irrAnnual_AP = irrAnnual_AP;
    // // System.out.println("irrAnnual_BQ "+irrAnnual_AP);
    //
    // double netYield8Pr = (irrAnnual_AP * 100);
    //
    // BIMAST.setReductionInYieldMaturityAt(prop.int2);
    // reductionInYieldMaturityAt = Double.parseDouble(BIMAST
    // .getReductionInYieldMaturityAt());
    // _reductionInYieldMaturityAt = reductionInYieldMaturityAt;
    // //
    // System.out.println("reductionInYieldMaturityAt "+reductionInYieldMaturityAt);
    //
    // //
    // System.out.println("****************************** Final Output ****************************************");
    // //
    // System.out.println("Sum Assured -> "+commonForAllProd.getStringWithout_E(flexiSmartBean.getSumAssured()));
    // //
    // System.out.println("Fund Value @6% is Rs.-> "+commonForAllProd.getRoundUp((commonForAllProd.getStringWithout_E(closingBalance_W))));
    // //
    // System.out.println("Fund Value @10% -> "+commonForAllProd.getRound((commonForAllProd.getStringWithout_E(closingBalance_AH))));
    // //
    // System.out.println("***********************************************************************************");
    //
    // // intent.putExtra("op","Sum Assured is Rs." +
    // //
    // currencyFormat.format(Double.parseDouble(commonForAllProd.getStringWithout_E(flexiSmartBean.getSumAssured()))));
    // // intent.putExtra("op1","Maturity Benefit Payable @ 4% is Rs."+
    // //
    // currencyFormat.format(Double.parseDouble(commonForAllProd.getRoundUp((commonForAllProd.getStringWithout_E(closingBalance_W))))));
    // // intent.putExtra("op2","Maturity Benefit Payable @ 8% is Rs." +
    // //
    // currencyFormat.format(Double.parseDouble(commonForAllProd.getRoundUp((commonForAllProd.getStringWithout_E(closingBalance_AH))))));
    // // startActivity(intent);
    // return new String[] {
    // commonForAllProd.getRoundUp_Level2(commonForAllProd
    // .getStringWithout_E(reductionInYieldMaturityAt)),
    // commonForAllProd.getRoundOffLevel2(commonForAllProd
    // .getStringWithout_E(netYield8Pr)) };
    // }

    /************************************* BI ends here **************************************/
    /************************* Output ends here ******************************************/

    /************************* Validation starts from here ******************************************/

    // Calculate effective premium
    private void setEffectivePremium() {
        try {
            int divFactor = 0;

            if (selPremFreq.getSelectedItem().toString().equals("Monthly")) {
                divFactor = 50;
            } else if (selPremFreq.getSelectedItem().toString()
                    .equals("Quarterly")) {
                divFactor = 50;
            } else if (selPremFreq.getSelectedItem().toString()
                    .equals("Half Yearly")) {
                divFactor = 100;
            } else if (selPremFreq.getSelectedItem().toString()
                    .equals("Yearly")) {
                divFactor = 100;
            }

            if ((Double.parseDouble(basicSA.getText().toString()) % divFactor) == 0) {
                effectivePremium = basicSA.getText().toString();
                // //System.out.println("Effective Premium  : "+(Integer.parseInt(premiumAmt.getString())));
            } else {
                effectivePremium = (Double.parseDouble(basicSA.getText()
                        .toString()) - (Double.parseDouble(basicSA.getText()
                        .toString()) % divFactor))
                        + "";
                // //System.out.println("Effective Premium  : "+(Integer.parseInt(premiumAmt.getString())-(Integer.parseInt(premiumAmt.getString())
                // % divFactor)));
            }
        } catch (Exception e) {/**/
        }
    }

    // validate premium amount, SAMF, holiday year , holiday term, topup premium
    // Validation after Submit Button is pressed

    private boolean vallidate() {

        StringBuilder error = new StringBuilder();
        // Premium Amount validation
        double minPremAmt = 0;
        if (selPremFreq.getSelectedItem().toString().equals("Monthly")) {
            minPremAmt = 9000;
        } else if (selPremFreq.getSelectedItem().toString().equals("Quarterly")) {
            minPremAmt = 20000;
        } else if (selPremFreq.getSelectedItem().toString()
                .equals("Half Yearly")) {
            minPremAmt = 30000;
        } else if (selPremFreq.getSelectedItem().toString().equals("Yearly")) {
            minPremAmt = 50000;
        }

        if (basicSA.getText().toString().equals("")) {
            error.append("Please enter Premium Amount");
            basicSA.requestFocus();

        } else if (Double.parseDouble(basicSA.getText().toString()) < minPremAmt) {
            error.append("Premium Amount should not be less than Rs. ").append(minPremAmt);
            basicSA.requestFocus();

            // topup with initial premium

        } else if ((selPremFreq.getSelectedItem().toString().equals("Yearly") || selPremFreq
                .getSelectedItem().toString().equals("Half Yearly"))
                && ((Double
                .parseDouble(basicSA.getText().toString() == "" ? "0.00"
                        : basicSA.getText().toString())) % 100 != 0)) {
            error.append("Premium Amount should be multiple of 100");
            basicSA.requestFocus();
        } else if ((selPremFreq.getSelectedItem().toString().equals("Monthly") || selPremFreq
                .getSelectedItem().toString().equals("Quarterly"))
                && ((Double
                .parseDouble(basicSA.getText().toString() == "" ? "0.00"
                        : basicSA.getText().toString())) % 100 != 0)) {
            error.append("Premium Amount should be multiple of 100");
            basicSA.requestFocus();
        } else {
            if (selTopup.isChecked()) {
                double minTopup = 2000;
                double maxTopup = Double.parseDouble(effectivePremium);

                if (TopupPremium.getText().toString().equals("")
                        || Double
                        .parseDouble(TopupPremium.getText().toString()) < minTopup
                        || Double
                        .parseDouble(TopupPremium.getText().toString()) > maxTopup) {
                    error.append("\nEnter Topup Premium Amount between ").append(currencyFormat.format(minTopup)).append(" and ").append(currencyFormat.format(maxTopup));
                } else if ((Double.parseDouble(TopupPremium.getText()
                        .toString())) % 100 != 0) {
                    error.append("\nTopup Premium Amount should be multiple of 100");
                }
            }
        }

        // SAMF validation
        CommonForAllProd cfap = new CommonForAllProd();
        double minSAMF = 0;
        double maxSAMF = 20;
        double age = Integer.parseInt(ageInYears.getSelectedItem().toString());
        double term = Integer.parseInt(selBasicTerm.getSelectedItem()
                .toString());
        if (age < 45) {
            minSAMF = Math.max(10, cfap.roundDown(term / 2, 2));
        } else {
            minSAMF = Math.max(7, cfap.roundDown(term / 4, 2));
        }

        if (SAMF.getText().toString().equals("")
                || Double.parseDouble(SAMF.getText().toString()) < minSAMF
                || Double.parseDouble(SAMF.getText().toString()) > maxSAMF) {
            error.append("\nEnter Sum Assured Multiple Factor [SAMF] between ").append(minSAMF).append(" and ").append(maxSAMF);
            SAMF.requestFocus();
            // SAMF.setText(minSAMF + "");
        }

        // SAMF validation
        // double minSAMF = 10;
        // double maxSAMF = 20;
        //
        // if (SAMF.getText().toString().equals("")
        // || Double.parseDouble(SAMF.getText().toString()) < minSAMF
        // || Double.parseDouble(SAMF.getText().toString()) > maxSAMF) {
        // error.append("\nEnter Sum Assured Multiple Factor [SAMF] between "
        // + minSAMF + " and " + maxSAMF);
        // }

        // Premium Holiday
        if (selHoliday.isChecked()) {
            // policy year
            double minHolidayYear = 6;
            double maxHolidayYear = Double.parseDouble(selBasicTerm
                    .getSelectedItem().toString());
            if (HolidayYear.getText().toString().equals("")
                    || Double.parseDouble(HolidayYear.getText().toString()) < minHolidayYear
                    || Double.parseDouble(HolidayYear.getText().toString()) > maxHolidayYear) {
                error.append("\nEnter Policy Year for Holiday between ").append(minHolidayYear).append(" and ").append(maxHolidayYear).append(" years");
            }
            // premium term
            int minHolidayTerm = 1;
            int maxHolidayTerm = 3;
            if (HolidayTerm.getText().toString().equals("")
                    || Double.parseDouble(HolidayTerm.getText().toString()) < minHolidayTerm
                    || Double.parseDouble(HolidayTerm.getText().toString()) > maxHolidayTerm) {
                error.append("\nEnter Premium Term for Holiday between ").append(minHolidayTerm).append(" and ").append(maxHolidayTerm).append(" years");
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
        } else
            return true;
    }

    /**
     * maturity age of policy changes from 70 to 65 as per 1st jan 2014,by
     * Vrushali Chaudhari
     */
    // maturity age of policy is 70 years
    private void valMaturityAge() {
        int Age = Integer.parseInt(ageInYears.getSelectedItem().toString());
        int PolicyTerm = Integer.parseInt(selBasicTerm.getSelectedItem()
                .toString());
        if ((Age + PolicyTerm) > 65) {
            showAlert.setMessage("Maturity age is 65 years");
            showAlert.setNeutralButton("OK",
                    new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            selBasicTerm.setSelection(0, false);
                            // apply focusable method
                            setFocusable(btn_bi_flexi_smart_life_assured_date);
                            btn_bi_flexi_smart_life_assured_date.requestFocus();
                        }
                    });
            showAlert.show();

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
                            setFocusable(rb_flexi_smart_plus_proposer_same_as_life_assured_yes);
                            rb_flexi_smart_plus_proposer_same_as_life_assured_yes
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
                                    setFocusable(spnr_bi_flexi_smart_life_assured_title);
                                    spnr_bi_flexi_smart_life_assured_title
                                            .requestFocus();
                                } else if (lifeAssured_First_Name.equals("")) {
                                    edt_bi_flexi_smart_life_assured_first_name
                                            .requestFocus();
                                } else {
                                    edt_bi_flexi_smart_life_assured_last_name
                                            .requestFocus();
                                }
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
                                    setFocusable(spnr_bi_flexi_smart_life_assured_title);
                                    spnr_bi_flexi_smart_life_assured_title
                                            .requestFocus();
                                } else if (lifeAssured_First_Name.equals("")) {
                                    edt_bi_flexi_smart_life_assured_first_name
                                            .requestFocus();
                                } else {
                                    edt_bi_flexi_smart_life_assured_last_name
                                            .requestFocus();
                                }
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
                                    setFocusable(spnr_bi_flexi_smart_proposer_title);
                                    spnr_bi_flexi_smart_proposer_title
                                            .requestFocus();
                                } else if (proposer_First_Name.equals("")) {
                                    edt_bi_flexi_smart_proposer_first_name
                                            .requestFocus();
                                } else {
                                    edt_bi_flexi_smart_proposer_last_name
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
                                setFocusable(btn_bi_flexi_smart_life_assured_date);
                                btn_bi_flexi_smart_life_assured_date
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
                                setFocusable(btn_bi_flexi_smart_life_assured_date);
                                btn_bi_flexi_smart_life_assured_date
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
                                setFocusable(btn_bi_flexi_smart_proposer_date);
                                btn_bi_flexi_smart_proposer_date.requestFocus();
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

    private boolean valPremiumAmount() {

        if (basicSA.getText().toString().equals("")) {
            showAlert.setMessage("Please Enter A Premium Amount");
            showAlert.setNeutralButton("OK",
                    new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {

                            // apply focusable method
                            basicSA.requestFocus();
                        }
                    });
            showAlert.show();

            return false;
        } else {
            return true;
        }
    }


    public void onClickDob(View v) {
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

    private String getformatedThousandString(Long number) {
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
        if (v.getId() == edt_bi_flexi_smart_life_assured_first_name.getId()) {
            // clearFocusable(edt_bi_flexi_smart_life_assured_first_name);
            setFocusable(edt_bi_flexi_smart_life_assured_middle_name);
            edt_bi_flexi_smart_life_assured_middle_name.requestFocus();
        } else if (v.getId() == edt_bi_flexi_smart_life_assured_middle_name
                .getId()) {
            // clearFocusable(edt_bi_flexi_smart_life_assured_middle_name);
            setFocusable(edt_bi_flexi_smart_life_assured_last_name);
            edt_bi_flexi_smart_life_assured_last_name.requestFocus();
        } else if (v.getId() == edt_bi_flexi_smart_life_assured_last_name
                .getId()) {

            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(
                    edt_bi_flexi_smart_life_assured_last_name.getWindowToken(),
                    0);
            // clearFocusable(edt_bi_flexi_smart_life_assured_last_name);
            setFocusable(btn_bi_flexi_smart_life_assured_date);
            btn_bi_flexi_smart_life_assured_date.requestFocus();
        } else if (v.getId() == edt_bi_flexi_smart_proposer_first_name.getId()) {
            // clearFocusable(edt_bi_flexi_smart_proposer_first_name);
            setFocusable(edt_bi_flexi_smart_proposer_middle_name);
            edt_bi_flexi_smart_proposer_middle_name.requestFocus();
        } else if (v.getId() == edt_bi_flexi_smart_proposer_middle_name.getId()) {
            // clearFocusable(edt_bi_flexi_smart_proposer_middle_name);
            setFocusable(edt_bi_flexi_smart_proposer_last_name);
            edt_bi_flexi_smart_proposer_last_name.requestFocus();
        } else if (v.getId() == edt_bi_flexi_smart_proposer_last_name.getId()) {

            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(
                    edt_bi_flexi_smart_proposer_last_name.getWindowToken(), 0);
            // clearFocusable(edt_bi_flexi_smart_proposer_last_name);
            setFocusable(btn_bi_flexi_smart_proposer_date);
            btn_bi_flexi_smart_proposer_date.requestFocus();
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
            clearFocusable(selBasicTerm);
            setFocusable(selBasicTerm);
            selBasicTerm.requestFocus();
        } else if (v.getId() == basicSA.getId()) {
            // clearFocusable(basicSA);
            setFocusable(SAMF);
            SAMF.requestFocus();
        } else if (v.getId() == SAMF.getId()) {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(SAMF.getWindowToken(), 0);

            // clearFocusable(SAMF);
            setFocusable(selOption);
            selOption.requestFocus();
        }

        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        b = 1;
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
        }/* else if (emailId.equals("")) {
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
