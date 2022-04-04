package sbilife.com.pointofsale_bancaagency.saralmahaanand;

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

public class BI_SaralMahaAnandActivity extends AppCompatActivity implements
        OnEditorActionListener {

    private final int DATE_DIALOG_ID = 1;
    private final int SIGNATURE_ACTIVITY = 1;
    String place1 = "";
    String proposer_Backdating_WishToBackDate_Policy = "";
    String proposer_Backdating_BackDate = "";
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
    private String na_dob = "";
    private ArrayAdapter<String> genderAdapter;
    private int flag = 0;
    private CheckBox cb_staffdisc;
    private CheckBox cb_bi_saral_maha_anand_JKResident;
    private CheckBox cb_bi_saral_maha_anand_adb_rider;
    private EditText edt_bi_saral_maha_anand_life_assured_first_name;
    private EditText edt_bi_saral_maha_anand_life_assured_middle_name;
    private EditText edt_bi_saral_maha_anand_life_assured_last_name;
    private EditText edt_bi_saral_maha_anand_life_assured_age;
    private EditText edt_saral_maha_anand_contact_no;
    private EditText edt_saral_maha_anand_Email_id;
    private EditText edt_saral_maha_anand_ConfirmEmail_id;
    private EditText edt_bi_saral_maha_anand_sum_assured_amount, edt_saral_maha_anand_samf, edt_saral_maha_anand_noOfYearsElapsedSinceInception, edt_saral_maha_anand_percent_EquityFund,
            edt_saral_maha_anand_percent_BalancedFund, edt_saral_maha_anand_percent_BondFund,
            edt_bi_saral_maha_anand_adb_rider_sum_assured;
    private Spinner spnr_bi_saral_maha_anand_life_assured_title;
    private Spinner spnr_bi_saral_maha_anand_selGender;
    private Spinner spnr_bi_saral_maha_anand_policyterm,
            spnr_bi_saral_maha_anand_premium_frequency, spnr_bi_saral_maha_anand_adb_rider_term;
    private TextView help_premAmt;
    private TextView help_SAMF;
    private TextView help_adb_term;
    private TextView help_adbsa;
    private TextView help_noOfYearsElapsedSinceInception;
    private Button btn_bi_saral_maha_anand_life_assured_date;
    private Button btnBack;
    private Button btnSubmit;
    private TableRow tr_bi_saral_maha_anand_adb_rider;
    private String QuatationNumber = "";
    private String planName = "";
    private AlertDialog.Builder showAlert;
    // newDBHelper db;
    private ParseXML prsObj;
    private String emailId = "";
    private String mobileNo = "";
    private String ConfirmEmailId = "";
    private String ProposerEmailId = "";
    private Dialog d;
    private boolean validationFla1 = false;
    private String lifeAssured_Title = "";
    private String lifeAssured_First_Name = "";
    private String lifeAssured_Middle_Name = "";
    private String lifeAssured_Last_Name = "";
    private String name_of_life_assured = "";
    private String lifeAssured_date_of_birth = "";
    private CommonForAllProd commonForAllProd;
    private CommonForAllProd obj;
    private StringBuilder bussIll = null;
    private StringBuilder retVal;
    private StringBuilder inputVal;
    private String name_of_proposer = "";
    private String name_of_person = "";
    private String place2 = "";
    private String date1 = "";
    private String date2 = "";
    private String agent_sign = "";
    private String proposer_sign = "";
    private String proposer_Is_Same_As_Life_Assured = "Y";
    private int PF = 0;
    private DecimalFormat currencyFormat;
    private SaralMahaAnandProperties prop;
    private SaralMahaAnandBean saralMahaAnandBean;
    private Double effectivePremium;
    private Bitmap photoBitmap;
    private int DIALOG_ID;
    private int mYear;
    private int mMonth;
    private int mDay;
    private Button btn_MarketingOfficalDate;
    private Button btn_PolicyholderDate;
    private ImageButton Ibtn_signatureofMarketing;
    private ImageButton Ibtn_signatureofPolicyHolders;
    private String output = "";
    private String age_entry = "";
    private String gender = "";
    private String premPayingMode = "";
    private String policyTerm = "";
    private String latestImage = "";
    private List<M_BI_SaralMahaAnandAdapter> list_data;

    private String maturityAge = "";
    private String netYield4pa = "";
    private String netYield8pa = "";
    private String annPrem = "";
    private String sumAssured = "";
    private String policy_Frequency = "";
    private String perInvEquityFund = "";
    private String perInvBalancedFund = "";
    private String perInvBondFund = "";
    private String noOfYrElapsed = "";
    private String redInYieldNoYr = "";
    private String redInYieldMat = "";

    private String adb_rider_status = "";

    private String sa_adb = "";
    private String premiumAmt = "";

    private File mypath;

    /******** Added by Akshaya on 03-AUG-15 end */
    private LinearLayout ll_bi_saral_maha_anand_main;

    private String bankUserType = "", mode = "";

    /* parivartan changes */
    private String Check = "";
    private Context context;
    private CommonMethods commonMethods;
    private StorageUtils mStorageUtils;
    private ImageButton imageButtonSaralMahaAnandProposerPhotograph;

    private LinearLayout linearlayoutThirdpartySignature,
            linearlayoutAppointeeSignature;
    private ImageButton Ibtn_signatureofThirdParty, Ibtn_signatureofAppointee;
    private String thirdPartySign = "", appointeeSign = "";
    private String product_Code, product_UIN, product_cateogory, product_type, Company_policy_surrender_dec = "";

    /* end */
    private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;
            updateDisplay(DIALOG_ID);
        }
    };

    private CheckBox cb_kerladisc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.bi_saral_maha_anandmain);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);

        UI_Declaration();


        /* parivartan changes */
        context = this;
        commonMethods = new CommonMethods();
        mStorageUtils = new StorageUtils();
        /* end */

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
                    planName = "Saral Maha Anand";
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
                        product_Code/* "50" */, agentcode, zero + "");

            }
        } else
            needAnalysis_flag = 0;
        // ProductHomePageActivity.path.setText("Benefit Illustrator");

        prsObj = new ParseXML();
        setSpinner_Value();
        // setBIInputGui();

        edt_bi_saral_maha_anand_life_assured_first_name
                .setOnEditorActionListener(this);
        edt_bi_saral_maha_anand_life_assured_middle_name
                .setOnEditorActionListener(this);
        edt_bi_saral_maha_anand_life_assured_last_name
                .setOnEditorActionListener(this);
        edt_bi_saral_maha_anand_sum_assured_amount
                .setOnEditorActionListener(this);
        edt_saral_maha_anand_contact_no.setOnEditorActionListener(this);
        edt_saral_maha_anand_Email_id.setOnEditorActionListener(this);
        edt_saral_maha_anand_ConfirmEmail_id.setOnEditorActionListener(this);
        edt_saral_maha_anand_samf.setOnEditorActionListener(this);
        edt_saral_maha_anand_noOfYearsElapsedSinceInception
                .setOnEditorActionListener(this);
        edt_saral_maha_anand_percent_EquityFund.setOnEditorActionListener(this);
        edt_saral_maha_anand_percent_BalancedFund
                .setOnEditorActionListener(this);
        edt_saral_maha_anand_percent_BondFund.setOnEditorActionListener(this);
        edt_bi_saral_maha_anand_adb_rider_sum_assured
                .setOnEditorActionListener(this);

        setFocusable(spnr_bi_saral_maha_anand_life_assured_title);
        spnr_bi_saral_maha_anand_life_assured_title.requestFocus();

        validationOfMoile_EmailId();
        setSpinnerAndOtherListner();
        showAlert = new AlertDialog.Builder(this);
        saralMahaAnandBean = new SaralMahaAnandBean();
        obj = new CommonForAllProd();
        commonForAllProd = new CommonForAllProd();
        prop = new SaralMahaAnandProperties();
        list_data = new ArrayList<>();
        currencyFormat = new DecimalFormat("##,##,##,###");


        TableRow tr_staff_disc = findViewById(R.id.tr_saral_maha_anand_staff_disc);

        String str_usertype = "";
        try {
            str_usertype = SimpleCrypto.decrypt("SBIL",
                    dbHelper.GetUserType());

        } catch (Exception e) {
            e.printStackTrace();
        }
        if (str_usertype.equalsIgnoreCase("BAP")
                || str_usertype.equalsIgnoreCase("CAG")) {
            tr_staff_disc.setVisibility(View.GONE);
        }
        if (needAnalysis_flag == 1 && !TextUtils.isEmpty(gender)) {
            spnr_bi_saral_maha_anand_selGender.setSelection(genderAdapter
                    .getPosition(gender));
            onClickLADob(btn_bi_saral_maha_anand_life_assured_date);
        }

    }

    private void UI_Declaration() {

        cb_staffdisc = findViewById(R.id.cb_staffdisc);
        cb_bi_saral_maha_anand_JKResident = findViewById(R.id.cb_jk);

        edt_bi_saral_maha_anand_life_assured_first_name = findViewById(R.id.edt_bi_saral_maha_anand_life_assured_first_name);
        edt_bi_saral_maha_anand_life_assured_middle_name = findViewById(R.id.edt_bi_saral_maha_anand_life_assured_middle_name);
        edt_bi_saral_maha_anand_life_assured_last_name = findViewById(R.id.edt_bi_saral_maha_anand_life_assured_last_name);
        edt_bi_saral_maha_anand_life_assured_age = findViewById(R.id.edt_bi_saral_maha_anand_life_assured_age);
        edt_saral_maha_anand_contact_no = findViewById(R.id.edt_saral_maha_anand_contact_no);
        edt_saral_maha_anand_Email_id = findViewById(R.id.edt_saral_maha_anand_Email_id);
        edt_saral_maha_anand_ConfirmEmail_id = findViewById(R.id.edt_saral_maha_anand_ConfirmEmail_id);
        edt_bi_saral_maha_anand_sum_assured_amount = findViewById(R.id.edt_bi_saral_maha_anand_sum_assured_amount);

        spnr_bi_saral_maha_anand_life_assured_title = findViewById(R.id.spnr_bi_saral_maha_anand_life_assured_title);
        spnr_bi_saral_maha_anand_selGender = findViewById(R.id.spnr_bi_saral_maha_anand_selGender);
        spnr_bi_saral_maha_anand_selGender.setClickable(false);
        spnr_bi_saral_maha_anand_selGender.setEnabled(false);

        spnr_bi_saral_maha_anand_policyterm = findViewById(R.id.spnr_bi_saral_maha_anand_policyterm);
        spnr_bi_saral_maha_anand_premium_frequency = findViewById(R.id.spnr_bi_saral_maha_anand_premium_frequency);

        edt_saral_maha_anand_samf = findViewById(R.id.samf);
        edt_saral_maha_anand_noOfYearsElapsedSinceInception = findViewById(R.id.years_elapsed_since_inception);
        edt_saral_maha_anand_percent_EquityFund = findViewById(R.id.equityFund);
        edt_saral_maha_anand_percent_BalancedFund = findViewById(R.id.balancedFund);
        edt_saral_maha_anand_percent_BondFund = findViewById(R.id.bondFund);

        // Adb
        cb_bi_saral_maha_anand_adb_rider = findViewById(R.id.cb_bi_saral_maha_anand_adb_rider);
        edt_bi_saral_maha_anand_adb_rider_sum_assured = findViewById(R.id.edt_bi_saral_maha_anand_adb_rider_sum_assured);
        spnr_bi_saral_maha_anand_adb_rider_term = findViewById(R.id.spnr_bi_saral_maha_anand_adb_rider_term);

        help_SAMF = findViewById(R.id.help_samf);
        help_premAmt = findViewById(R.id.help_premAmt);
        help_noOfYearsElapsedSinceInception = findViewById(R.id.help_years_elapsed_since_inception);
        help_adb_term = findViewById(R.id.help_adb_term);
        help_adbsa = findViewById(R.id.help_adbsa);

        btn_bi_saral_maha_anand_life_assured_date = findViewById(R.id.btn_bi_saral_maha_anand_life_assured_date);
        btnBack = findViewById(R.id.btn_bi_saral_maha_anand_btnback);
        btnSubmit = findViewById(R.id.btn_bi_saral_maha_anand_btnSubmit);
        tr_bi_saral_maha_anand_adb_rider = findViewById(R.id.tr_bi_saral_maha_anand_adb_rider);

        ll_bi_saral_maha_anand_main = findViewById(R.id.ll_bi_saral_maha_anand_main);

    }

    private void setSpinner_Value() {

        // Gender
        genderAdapter = new ArrayAdapter<>(getApplicationContext(),
                R.layout.spinner_item, getResources().getStringArray(R.array.gender_arrays));
        genderAdapter.setDropDownViewResource(R.layout.spinner_item1);
        spnr_bi_saral_maha_anand_selGender.setAdapter(genderAdapter);
        genderAdapter.notifyDataSetChanged();

        commonMethods.fillSpinnerValue(context, spnr_bi_saral_maha_anand_life_assured_title,
                Arrays.asList(getResources().getStringArray(R.array.arr_select_title)));

        // Policy Term
        String[] policyTermList = {"10", "15", "20"};
        ArrayAdapter<String> policyTermAdapter = new ArrayAdapter<>(
                getApplicationContext(), R.layout.spinner_item, policyTermList);
        policyTermAdapter.setDropDownViewResource(R.layout.spinner_item1);
        spnr_bi_saral_maha_anand_policyterm.setAdapter(policyTermAdapter);
        policyTermAdapter.notifyDataSetChanged();

        // Premium Frequency
        String[] premFreqList = {"Yearly", "Half Yearly", "Quarterly",
                "Monthly"};
        ArrayAdapter<String> premFreqAdapter = new ArrayAdapter<>(
                getApplicationContext(), R.layout.spinner_item, premFreqList);
        premFreqAdapter.setDropDownViewResource(R.layout.spinner_item1);
        spnr_bi_saral_maha_anand_premium_frequency.setAdapter(premFreqAdapter);
        premFreqAdapter.notifyDataSetChanged();

        // adb Term
        String[] adbTermList = new String[16];
        for (int i = 5; i <= 20; i++) {
            adbTermList[i - 5] = i + "";
        }
        ArrayAdapter<String> adbTermAdapter = new ArrayAdapter<>(
                getApplicationContext(), R.layout.spinner_item, adbTermList);
        adbTermAdapter.setDropDownViewResource(R.layout.spinner_item1);
        spnr_bi_saral_maha_anand_adb_rider_term.setAdapter(adbTermAdapter);
        adbTermAdapter.notifyDataSetChanged();

    }

    private void validationOfMoile_EmailId() {

        edt_saral_maha_anand_contact_no
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
                        String abc = edt_saral_maha_anand_contact_no.getText()
                                .toString();
                        mobile_validation(abc);

                    }
                });

        edt_saral_maha_anand_Email_id.addTextChangedListener(new TextWatcher() {

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
                ProposerEmailId = edt_saral_maha_anand_Email_id.getText()
                        .toString();
                //email_id_validation(ProposerEmailId);

            }
        });

        edt_saral_maha_anand_ConfirmEmail_id
                .addTextChangedListener(new TextWatcher() {

                    public void onTextChanged(CharSequence arg0, int arg1,
                                              int arg2, int arg3) {
                    }

                    public void beforeTextChanged(CharSequence arg0, int arg1,
                                                  int arg2, int arg3) {
                    }

                    public void afterTextChanged(Editable arg0) {
                        String proposer_confirm_emailId = edt_saral_maha_anand_ConfirmEmail_id
                                .getText().toString();
                        //confirming_email_id(proposer_confirm_emailId);

                    }
                });

    }

    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (v.getId() == edt_bi_saral_maha_anand_life_assured_first_name
                .getId()) {
            setFocusable(edt_bi_saral_maha_anand_life_assured_middle_name);
            edt_bi_saral_maha_anand_life_assured_middle_name.requestFocus();
        } else if (v.getId() == edt_bi_saral_maha_anand_life_assured_middle_name
                .getId()) {
            setFocusable(edt_bi_saral_maha_anand_life_assured_last_name);
            edt_bi_saral_maha_anand_life_assured_last_name.requestFocus();
        } else if (v.getId() == edt_bi_saral_maha_anand_life_assured_last_name
                .getId()) {

            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(
                    edt_bi_saral_maha_anand_life_assured_last_name
                            .getWindowToken(), 0);
            // clearFocusable(edt_bi_saral_maha_anand_life_assured_last_name);
            setFocusable(btn_bi_saral_maha_anand_life_assured_date);
            btn_bi_saral_maha_anand_life_assured_date.requestFocus();
        } else if (v.getId() == edt_saral_maha_anand_contact_no.getId()) {
            // clearFocusable(premiumAmt);
            setFocusable(edt_saral_maha_anand_Email_id);
            edt_saral_maha_anand_Email_id.requestFocus();
        } else if (v.getId() == edt_saral_maha_anand_Email_id.getId()) {
            // clearFocusable(premiumAmt);
            setFocusable(edt_saral_maha_anand_ConfirmEmail_id);
            edt_saral_maha_anand_ConfirmEmail_id.requestFocus();
        } else if (v.getId() == edt_saral_maha_anand_ConfirmEmail_id.getId()) {
            // clearFocusable(premiumAmt);
            clearFocusable(spnr_bi_saral_maha_anand_policyterm);
            setFocusable(spnr_bi_saral_maha_anand_policyterm);
            spnr_bi_saral_maha_anand_policyterm.requestFocus();
        } else if (v.getId() == edt_bi_saral_maha_anand_sum_assured_amount
                .getId()) {
            setFocusable(edt_saral_maha_anand_samf);
            edt_saral_maha_anand_samf.requestFocus();

        } else if (v.getId() == edt_saral_maha_anand_samf.getId()) {
            setFocusable(edt_saral_maha_anand_noOfYearsElapsedSinceInception);
            edt_saral_maha_anand_noOfYearsElapsedSinceInception.requestFocus();

        } else if (v.getId() == edt_saral_maha_anand_noOfYearsElapsedSinceInception
                .getId()) {
            setFocusable(edt_saral_maha_anand_percent_EquityFund);
            edt_saral_maha_anand_percent_EquityFund.requestFocus();

        } else if (v.getId() == edt_saral_maha_anand_percent_EquityFund.getId()) {
            setFocusable(edt_saral_maha_anand_percent_BalancedFund);
            edt_saral_maha_anand_percent_BalancedFund.requestFocus();

        } else if (v.getId() == edt_saral_maha_anand_percent_BalancedFund
                .getId()) {
            setFocusable(edt_saral_maha_anand_percent_BondFund);
            edt_saral_maha_anand_percent_BondFund.requestFocus();
        } else if (v.getId() == edt_saral_maha_anand_percent_BondFund.getId()) {

            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(
                    edt_saral_maha_anand_percent_BondFund.getWindowToken(), 0);
        }
        return true;
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

        boolean flagFocus = true;
        if (!flagFocus) {
            ll_bi_saral_maha_anand_main.requestFocus();
        } else {
            spnr_bi_saral_maha_anand_life_assured_title.requestFocus();
        }

    }

    private void setSpinnerAndOtherListner() {

        // // premium amount
        edt_bi_saral_maha_anand_sum_assured_amount
                .setOnFocusChangeListener(new OnFocusChangeListener() {

                    public void onFocusChange(View arg0, boolean arg1) {
                        // TODO Auto-generated method stub

                        if (!(edt_bi_saral_maha_anand_sum_assured_amount
                                .getText().toString().equals(""))) {
                            effectivePremium = getEffectivePremium();
                            updateSAMFlabel();
                            updateSumAssuredADBlabel();
                        }
                    }
                });

        edt_bi_saral_maha_anand_life_assured_age
                .setOnFocusChangeListener(new OnFocusChangeListener() {

                    public void onFocusChange(View arg0, boolean arg1) {
                        // TODO Auto-generated method stub

                        if (!(edt_bi_saral_maha_anand_life_assured_age
                                .getText().toString().equals(""))) {

                            updateSAMFlabel();
                            updateTermADBlabel();
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
                clearFocusable(spnr_bi_saral_maha_anand_life_assured_title);
                setFocusable(spnr_bi_saral_maha_anand_life_assured_title);
                spnr_bi_saral_maha_anand_life_assured_title.requestFocus();

            }
        });

        cb_bi_saral_maha_anand_JKResident
                .setOnClickListener(new OnClickListener() {

                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        if (cb_bi_saral_maha_anand_JKResident.isChecked()) {
                            cb_bi_saral_maha_anand_JKResident.setChecked(true);
                        } else {
                            cb_bi_saral_maha_anand_JKResident.setChecked(false);
                        }
                    }
                });

        // Spinner
        spnr_bi_saral_maha_anand_life_assured_title
                .setOnItemSelectedListener(new OnItemSelectedListener() {

                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int position, long id) {
                        // TODO Auto-generated method stub
                        if (position == 0) {
                            lifeAssured_Title = "";
                        } else {
                            lifeAssured_Title = spnr_bi_saral_maha_anand_life_assured_title
                                    .getSelectedItem().toString();
                            if (lifeAssured_Title.equalsIgnoreCase("Mr.")) {
                                spnr_bi_saral_maha_anand_selGender
                                        .setSelection(
                                                getIndex(
                                                        spnr_bi_saral_maha_anand_selGender,
                                                        "Male"), false);
                            } else if (lifeAssured_Title
                                    .equalsIgnoreCase("Ms.")) {
                                spnr_bi_saral_maha_anand_selGender
                                        .setSelection(
                                                getIndex(
                                                        spnr_bi_saral_maha_anand_selGender,
                                                        "Female"), false);
                            } else if (lifeAssured_Title
                                    .equalsIgnoreCase("Mrs.")) {
                                spnr_bi_saral_maha_anand_selGender
                                        .setSelection(
                                                getIndex(
                                                        spnr_bi_saral_maha_anand_selGender,
                                                        "Female"), false);
                            }
                            clearFocusable(spnr_bi_saral_maha_anand_life_assured_title);
                            setFocusable(edt_bi_saral_maha_anand_life_assured_first_name);
                            edt_bi_saral_maha_anand_life_assured_first_name
                                    .requestFocus();
                        }
                    }

                    public void onNothingSelected(AdapterView<?> arg0) {
                        // TODO Auto-generated method stub

                    }
                });

        spnr_bi_saral_maha_anand_policyterm
                .setOnItemSelectedListener(new OnItemSelectedListener() {

                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int position, long id) {

                        // spnr_bi_saral_maha_anand_premium_frequency
                        // .setSelection(3, true);
                        if (!(edt_bi_saral_maha_anand_life_assured_age
                                .getText().toString().equals(""))) {
                            valADBterm();
                        }

                        updateSAMFlabel();
                        updateTermADBlabel();
                        updatenoOfYearsElapsedSinceInception();

                        clearFocusable(spnr_bi_saral_maha_anand_policyterm);
                        setFocusable(spnr_bi_saral_maha_anand_premium_frequency);
                        spnr_bi_saral_maha_anand_premium_frequency
                                .requestFocus();

                    }

                    public void onNothingSelected(AdapterView<?> arg0) {
                        // TODO Auto-generated method stub

                    }
                });

        spnr_bi_saral_maha_anand_premium_frequency
                .setOnItemSelectedListener(new OnItemSelectedListener() {

                    public void onItemSelected(AdapterView<?> adapter,
                                               View arg1, int position, long id) {
                        // TODO Auto-generated method stub
                        updatePremiumAmtLabel();
                        updateSAMFlabel();
                        updateSumAssuredADBlabel();
                        if (edt_bi_saral_maha_anand_life_assured_first_name
                                .getText().toString().equals("")) {
                            clearFocusable(spnr_bi_saral_maha_anand_premium_frequency);
                            setFocusable(spnr_bi_saral_maha_anand_life_assured_title);
                            spnr_bi_saral_maha_anand_life_assured_title
                                    .requestFocus();

                        } else {

                            clearFocusable(spnr_bi_saral_maha_anand_premium_frequency);
                            setFocusable(edt_bi_saral_maha_anand_sum_assured_amount);
                            edt_bi_saral_maha_anand_sum_assured_amount
                                    .requestFocus();
                        }
                    }

                    public void onNothingSelected(AdapterView<?> arg0) {
                        // TODO Auto-generated method stub

                    }
                });

        // ADB Rider
        cb_bi_saral_maha_anand_adb_rider
                .setOnCheckedChangeListener(new OnCheckedChangeListener() {
                    public void onCheckedChanged(CompoundButton buttonView,
                                                 boolean isChecked) {
                        if (isChecked) {

                            cb_bi_saral_maha_anand_adb_rider.setChecked(true);
                            tr_bi_saral_maha_anand_adb_rider
                                    .setVisibility(View.VISIBLE);
                            updateSumAssuredADBlabel();
                            help_adbsa.setVisibility(View.VISIBLE);

                        } else {
                            tr_bi_saral_maha_anand_adb_rider
                                    .setVisibility(View.GONE);
                            help_adbsa.setVisibility(View.GONE);
                        }
                    }
                });

        // cb_bi_saral_maha_anand_adb_rider
        // .setOnClickListener(new OnClickListener() {
        //
        //
        // public void onClick(View v) {
        // // TODO Auto-generated method stub
        // if (cb_bi_saral_maha_anand_adb_rider.isChecked()) {
        // cb_bi_saral_maha_anand_adb_rider.setChecked(true);
        // tr_bi_saral_maha_anand_adb_rider
        // .setVisibility(View.VISIBLE);
        // } else {
        // tr_bi_saral_maha_anand_adb_rider
        // .setVisibility(View.GONE);
        // }
        //
        // }
        // });

        spnr_bi_saral_maha_anand_adb_rider_term
                .setOnItemSelectedListener(new OnItemSelectedListener() {

                    public void onItemSelected(AdapterView<?> adapter,
                                               View arg1, int position, long id) {
                        // TODO Auto-generated method stub

                        if (!(edt_bi_saral_maha_anand_life_assured_age
                                .getText().toString().equals(""))) {
                            valADBterm();
                        }
                        clearFocusable(spnr_bi_saral_maha_anand_adb_rider_term);
                        setFocusable(edt_bi_saral_maha_anand_adb_rider_sum_assured);
                        edt_bi_saral_maha_anand_adb_rider_sum_assured
                                .requestFocus();

                    }

                    public void onNothingSelected(AdapterView<?> arg0) {
                        // TODO Auto-generated method stub

                    }
                });

        // //SAMF
        edt_saral_maha_anand_samf
                .setOnFocusChangeListener(new OnFocusChangeListener() {

                    public void onFocusChange(View v, boolean hasFocus) {
                        // TODO Auto-generated method stub

                        if (!(edt_saral_maha_anand_samf.getText().toString()
                                .equals(""))) {
                            updateSumAssuredADBlabel();
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
                // TODO Auto-generated method stub

                inputVal = new StringBuilder();
                retVal = new StringBuilder();

                lifeAssured_First_Name = edt_bi_saral_maha_anand_life_assured_first_name
                        .getText().toString();
                lifeAssured_Middle_Name = edt_bi_saral_maha_anand_life_assured_middle_name
                        .getText().toString();
                lifeAssured_Last_Name = edt_bi_saral_maha_anand_life_assured_last_name
                        .getText().toString();

                name_of_life_assured = lifeAssured_Title + " "
                        + lifeAssured_First_Name + " "
                        + lifeAssured_Middle_Name + " " + lifeAssured_Last_Name;

                mobileNo = edt_saral_maha_anand_contact_no.getText().toString();
                emailId = edt_saral_maha_anand_Email_id.getText().toString();
                ConfirmEmailId = edt_saral_maha_anand_ConfirmEmail_id.getText()
                        .toString();

                if (valLifeAssuredProposerDetail() && valDob()
                        && valBasicDetail() && valSA() && valSAMF()
                        && valYearsElapsedSinceInception()
                        && valADBsumAssured() && valTotalAllocation()
                        && valPolicyTermPlusAge()) {


                    addListenerOnSubmit();
                    getInput(saralMahaAnandBean);
                    // insertDataIntoDatabase();

                    if (needAnalysis_flag == 0) {

                        Intent i = new Intent(BI_SaralMahaAnandActivity.this,
                                Success.class);

                        i.putExtra("ProductName",
                                "Product : SBI Life - Saral Maha Anand (UIN:111L070V02)");

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
                        i.putExtra(
                                "op3",
                                "Regular Premium (RP) is Rs. "
                                        + currencyFormat.format(Double.parseDouble(prsObj
                                        .parseXmlTag(retVal.toString(),
                                                "AnnualInstallment"))));

						/*
						  Removed by Akshaya Mirajkar on 22,Jan,2014
						 */
                        // if(selADBRider.isChecked())
                        // {
                        // i.putExtra("op4","Accidental Death Benefit Linked Rider Premium is Rs. "
                        // +
                        // currencyFormat.format(Double.parseDouble(adbRiderPrem))
                        // );
                        // i.putExtra("op5","Total First Year Premium is Rs." +
                        // currencyFormat.format(Double.parseDouble(totFirstYrPrem)));
                        // }

                        i.putExtra("header", "SBI Life - Saral Maha Anand");
                        i.putExtra("header1", "(UIN:111L070V02)");
                        startActivity(i);

                    } else
                        Dialog();

                }

            }
        });

    }

    private void addListenerOnSubmit() {
        saralMahaAnandBean = new SaralMahaAnandBean();

        if (cb_staffdisc.isChecked()) {
            saralMahaAnandBean.setIsForStaffOrNot(true);
        } else {
            saralMahaAnandBean.setIsForStaffOrNot(false);
        }

        if (cb_kerladisc.isChecked()) {
            saralMahaAnandBean.setKerlaDisc(true);
            //saralMahaAnandBean.setServiceTax(true);
        } else {
            saralMahaAnandBean.setKerlaDisc(false);
            //saralMahaAnandBean.setServiceTax(false);
        }


        saralMahaAnandBean.setAgeAtEntry(Integer
                .parseInt(edt_bi_saral_maha_anand_life_assured_age.getText()
                        .toString()));
        saralMahaAnandBean.setGender(spnr_bi_saral_maha_anand_selGender
                .getSelectedItem().toString());

        saralMahaAnandBean.setPolicyTerm_Basic(Integer
                .parseInt(spnr_bi_saral_maha_anand_policyterm.getSelectedItem()
                        .toString()));

//        saralMahaAnandBean.setPremFreq(spnr_bi_saral_maha_anand_policyterm
//                .getSelectedItem().toString());

        saralMahaAnandBean.setPremiumAmount(Double
                .parseDouble(edt_bi_saral_maha_anand_sum_assured_amount
                        .getText().toString()));

        saralMahaAnandBean.setSAMF(Double.parseDouble(edt_saral_maha_anand_samf
                .getText().toString()));

        saralMahaAnandBean.setYearsElapsedSinceInception(Integer
                .parseInt(edt_saral_maha_anand_noOfYearsElapsedSinceInception
                        .getText().toString()));

        if (!edt_saral_maha_anand_percent_EquityFund.getText().toString()
                .equals(""))
            saralMahaAnandBean.setPercentToBeInvested_EquityFund(Double
                    .parseDouble(edt_saral_maha_anand_percent_EquityFund
                            .getText().toString()));
        else {
            saralMahaAnandBean.setPercentToBeInvested_EquityFund(0);
        }

        if (!edt_saral_maha_anand_percent_BalancedFund.getText().toString()
                .equals(""))
            saralMahaAnandBean.setPercentToBeInvested_BalancedFund(Double
                    .parseDouble(edt_saral_maha_anand_percent_BalancedFund
                            .getText().toString()));
        else {
            saralMahaAnandBean.setPercentToBeInvested_BalancedFund(0);

        }
        if (!edt_saral_maha_anand_percent_BondFund.getText().toString()
                .equals(""))
            saralMahaAnandBean.setPercentToBeInvested_BondFund(Double
                    .parseDouble(edt_saral_maha_anand_percent_BondFund
                            .getText().toString()));
        else {
            saralMahaAnandBean.setPercentToBeInvested_BondFund(0);
        }

        double a = getEffectivePremium();
        saralMahaAnandBean.setEffectivePremium(getEffectivePremium());

        // Adb
        if (cb_bi_saral_maha_anand_adb_rider.isChecked()) {
            saralMahaAnandBean.setSumAssuredADB(Integer
                    .parseInt(edt_bi_saral_maha_anand_adb_rider_sum_assured
                            .getText().toString()));

        } else {
            saralMahaAnandBean.setSumAssuredADB(0);
        }

        saralMahaAnandBean.setTermADB(Integer
                .parseInt(spnr_bi_saral_maha_anand_adb_rider_term
                        .getSelectedItem().toString()));

        saralMahaAnandBean.setIsADBchecked(cb_bi_saral_maha_anand_adb_rider
                .isChecked());
        if (spnr_bi_saral_maha_anand_premium_frequency.getSelectedItem()
                .toString().equals("Yearly")) {
            saralMahaAnandBean.setPF(1);
        } else if (spnr_bi_saral_maha_anand_premium_frequency.getSelectedItem()
                .toString().equals("Half Yearly")) {
            saralMahaAnandBean.setPF(2);
        } else if (spnr_bi_saral_maha_anand_premium_frequency.getSelectedItem()
                .toString().equals("Quarterly")) {
            saralMahaAnandBean.setPF(4);
        } else if (spnr_bi_saral_maha_anand_premium_frequency.getSelectedItem()
                .toString().equals("Monthly")) {
            saralMahaAnandBean.setPF(12);
        } else {
            saralMahaAnandBean.setPF(1);
        }

        showSaralMahaAnandBeanOutputPg(saralMahaAnandBean);

    }

    /************************* Output ends here ************************************************************/

    private void showSaralMahaAnandBeanOutputPg(SaralMahaAnandBean SaralMahaAnandBean) {

        String staffStatus = "";
        try {
            /******** Added by Akshaya on 03-AUG-15 start */
            if (SaralMahaAnandBean.getIsForStaffOrNot())
                staffStatus = "sbi";
            else
                staffStatus = "none";
            /****** Added by Akshaya on 03-AUG-15 end */

            String[] outputArr = getOutput("BI_Incl_Mort & Ser Tax", SaralMahaAnandBean);
            String[] redinYieldArr = getOutputReductionYield(
                    "Reduction in Yeild_CAP", SaralMahaAnandBean);

            retVal.append("<?xml version='1.0' encoding='utf-8' ?><SaralMahaAnand>");

            retVal.append("<errCode>0</errCode>" + "<staffStatus>")
                    .append(staffStatus).append("</staffStatus>")
                    .append("<maturityAge>").append(saralMahaAnandBean.getAgeAtEntry() + saralMahaAnandBean
                    .getPolicyTerm_Basic()).append("</maturityAge>")
                    .append("<AnnPrem>").append(commonForAllProd.getRound(commonForAllProd
                    .getStringWithout_E(saralMahaAnandBean
                            .getEffectivePremium()))).append("</AnnPrem>")
                    .append("<sumAssured>").append(commonForAllProd.getStringWithout_E(Double
                    .parseDouble(outputArr[0]))).append("</sumAssured>")
                    .append("<fundVal6>").append(commonForAllProd.getStringWithout_E(Double.parseDouble(outputArr[1]))).append("</fundVal6>")
                    .append("<fundVal10>").append(commonForAllProd.getStringWithout_E(Double.parseDouble(outputArr[2]))).append("</fundVal10>")
                    .append("<redInYieldMat>").append(redinYieldArr[0]).append("</redInYieldMat>")
                    .append("<redInYieldNoYr>").append(redinYieldArr[1]).append("</redInYieldNoYr>")
                    .append("<netYield4Pa>").append(redinYieldArr[2]).append("</netYield4Pa>")
                    .append("<netYield8Pa>").append(redinYieldArr[3]).append("</netYield8Pa>")
                    .append("<adbRiderPrem>").append(commonForAllProd.getStringWithout_E(Double.parseDouble(outputArr[4]))).append("</adbRiderPrem>")
                    .append("<AnnualInstallment>").append(commonForAllProd.getRound(""
                    + (Math.min(Double.parseDouble(outputArr[0]), 5000000) * 0.1))).append("</AnnualInstallment>");

            int index = SaralMahaAnandBean.getPolicyTerm_Basic();
            String FundValAtEnd4Pr = prsObj.parseXmlTag(bussIll.toString(), "FundValAtEnd4Pr" + index + "");
            String FundValAtEnd8Pr = prsObj.parseXmlTag(bussIll.toString(), "FundValAtEnd8Pr" + index + "");

            retVal.append("<FundValAtEnd4Pr" + index + ">" + FundValAtEnd4Pr + "</FundValAtEnd4Pr" + index + ">");
            retVal.append("<FundValAtEnd8Pr" + index + ">" + FundValAtEnd8Pr + "</FundValAtEnd8Pr" + index + ">");

            retVal.append(bussIll.toString());

            retVal.append("</SaralMahaAnand>");

            System.out.println("Final output in xml" + retVal.toString());

        } catch (Exception e) {
            // TODO: handle exception
            retVal.append("<?xml version='1.0' encoding='utf-8' ?><saralMahaAnand>" + "<errCode>1</errCode>" +
                    "<errorMessage>" + e.getMessage() + "</errorMessage></saralMahaAnand>");
        }

    }

    /********************************** Calculations starts from here **********************************************************/
    private String[] getOutput(String sheetName,
                               SaralMahaAnandBean SaralMahaAnandBean) {
        bussIll = new StringBuilder();

        String[] arrGuaranteedAddition_AA = new String[12];
        String[] arrGuaranteedAddition_AP = new String[12];

        // Output Variable Declaration
        int _month_E = 0, _year_F = 0, _age_H = 0;

        // for BI
        double sum_I = 0, sum_K = 0, sum_N = 0, sum_Q = 0, sum_R = 0, sum_S = 0, sum_U = 0, sum_V = 0, sum_X = 0, sum_AA = 0, sum_AB = 0, sum_AE = 0, sum_AF = 0, sum_AG = 0, sum_AH = 0, sum_AJ = 0, sum_AK = 0, sum_AL = 0, sum_AM = 0, sum_AP = 0, sum_AQ = 0, sum_AT = 0, sum_AU = 0, sum_J = 0, sum_Z = 0, sum_AO = 0, sum_AS = 0, sum_P = 0;

        double _sum_X = 0, _sum_AM = 0, _sum_J = 0, _sum_I = 0;

        // Temp Variable Declaretion
        int month_E = 0;
        int year_F = 0;
//        String policyInForce_G = "Y";
        int age_H = 0;
        String premium_I = "0";
        String topUpPremium_J = "0";
        String premiumAllocationCharge_K = "0";
//        String topUpCharges_L = "0";
//        String serviceTaxOnAllocation_M = "0";
        String amountAvailableForInvestment_N = "0";
//        String transferPercentIfAny_O = "0";
//        String allocatedFundToIndexFund_P = "0";
//        String sumAssuredRelatedCharges_O = "0";
        String riderCharges_P = "0";
//        String optionCharges_R = "0";
        String policyAdministrationCharge_Q = "0";
//        String transferOfIndexFundGainToDailyProtectFund_Y = "0";
//        String indexFundAtEnd = "0";
//        String transferOfCapitalFromIndexToDailyProtectFund_Z = "0";
//        String dailyProtectFundAtStart_AA = "0";
//        String dailyProtectFundAtEnd_AO = "0";
//        String guaranteeCharge_U = "0";
//        String indexFundAtStart_AB = "0";
        String oneHundredPercentOfCummulativePremium_AW = "0";
        String guaranteedAddition_AA = "0";
        String guaranteedAddition_AP = "0";
        String mortalityCharges_R = "0";
        String totalCharges_S = "0";
//        String totalServiceTax_exclOfSTonAllocAndSurr_T = "0";
//        String dailyProtectFundAfterCharges_AC = "0";
//        String indexFundAtStartAfterCharges_AD = "0";
//        String additionToIndexFund_AF = "0";
//        String additionToDailyProtectFund_AE = "0";
        String additionToFundIfAny_V = "0";
//        String fMCearnedOnDailyProtectFund_AG = "0";
//        String fMCearnedOnIndexFund_AH = "0";
//        String fundBeforeFMC_W = "0";
        String fundManagementCharge_X = "0";
        String serviceTaxOnFMC_Y = "0";
        String totalServiceTax_U = "0";
        String fundValueAfterFMCandBeforeGA_Z = "0";
//        String indexFundAtEnd_AP = "0";
        String fundValueAtEnd_AB = "0";
        String surrenderCap_AV = "0";
        String surrenderCharges_AC = "0";
//        String serviceTaxOnSurrenderCharges_AD = "0";
        String surrenderValue_AE = "0";
        String deathBenefit_AF = "0";
//        String transferOfCapitalFromIndexToDailyProtectFund_BB = "0";
        String fundValueAtEnd_AQ = "0";
        String mortalityCharges_AG = "0";
//        String transferOfIndexFundGainToDailyProtectFund_BA = "0";
//        String dailyProtectFundAtEnd_BQ = "0";
//        String dailyProtectFundAtStart_BC = "0";
//        String guaranteeCharge_AW = "0";
        String totalCharges_AH = "0";
        String totalServiceTax_ExclOfSTonAllocAndsurr_AI = "0";
//        String indexFundAtStart_BD = "0";
//        String indexFundAtStartAfterCharges_BF = "0";
//        String additionToIndexFund_BH = "0";
//        String fMCearnedOnIndexFund_BJ = "0";
//        String dailyProtectFundAftercharges_BE = "0";
//        String additionToDailyProtectFund_BG = "0";
//        String fMCearnedOnDailyProtectFund_BI = "0";
        String additionToFundIfAny_AK = "0";
        String fundBeforeFMC_AL = "0";
        String fundManagementCharge_AM = "0";
        String serviceTaxOnFMC_AN = "0";
        String fundValueAfterFMCAndBeforeGA_AO = "0";
//        String indexFundAtEnd_BR = "0";
        String deathBenefit_AU = "0";
        String totalServiceTax_AJ = "0";
        String surrenderCharges_AR = "0";
        String serviceTaxOnSurrenderCharges_AS = "0";
        String surrenderValue_AT = "0", /*surrenderCharges_AO = "0",*/ _surrenderCharges_AO = "0", /*surrenderCharges_Y = "0",*/ _surrenderCharges_Y = "0", commission_AS = "0", _commission_AS = "0";

        if (sheetName.equals("BI_Incl_Mort & Ser Tax")) {
            // Input Drop Downs from "BI_Incl_Mort & Ser Tax" sheet
            prop.allocationCharges = true; // *Sheet Name -> BI_Incl_Mort & Ser
            // Tax,*Cell ->B20
            prop.mortalityAndRiderCharges = true; // *Sheet Name -> BI_Incl_Mort
            // & Ser Tax,*Cell ->B21
            prop.fundManagementCharges = true;
            prop.administrationCharges = true;// *Sheet Name -> BI_Incl_Mort &
            // Ser Tax,*Cell ->B23
            prop.surrenderCharges = true; // *Sheet Name -> BI_Incl_Mort & Ser
            // Tax,*Cell ->B24
            prop.mortalityCharges = true; // *Sheet Name -> BI_Incl_Mort & Ser
            // Tax,*Cell ->B27
            prop.riderCharges = true; // *Sheet Name -> BI_Incl_Mort & Ser
            // Tax,*Cell ->B29
            prop.topUpStatus = true; // *Sheet Name -> BI_Incl_Mort & Ser
            // Tax,*Cell ->B31

        }
        // From GUI Input
        boolean staffDisc = SaralMahaAnandBean.getIsForStaffOrNot();
        boolean bancAssuranceDisc = SaralMahaAnandBean.getIsBancAssuranceDiscOrNot();
        double SAMF = SaralMahaAnandBean.getSAMF();
        int ageAtEntry = SaralMahaAnandBean.getAgeAtEntry();

        //double serviceTax=SaralMahaAnandBean.getServiceTax();
        double serviceTax = 0;
        boolean isKerlaDisc = SaralMahaAnandBean.isKerlaDisc();

        // Input Drop Downs from GUI[But not displayed in GUI]
        String addTopUp = "No";
        // Internally Calculated Input Fields
        double effectivePremium = SaralMahaAnandBean.getEffectivePremium();
        int PF = SaralMahaAnandBean.getPF();
        int policyTerm = SaralMahaAnandBean.getPolicyTerm_Basic();
        double sumAssured = (effectivePremium * SAMF);
        SaralMahaAnandBean
                .setEffectiveTopUpPrem(addTopUp, prop.topUpPremiumAmt);
        double effectiveTopUpPrem = SaralMahaAnandBean.getEffectiveTopUpPrem();
        // Declaration of method Variables/Object required for calculation
        SaralMahaAnandBusinessLogic BIMAST = new SaralMahaAnandBusinessLogic();
        String[] forBIArray = BIMAST
                .getForBIArr(prop.mortalityCharges_AsPercentOfofLICtable);
        int rowNumber = 0;

        // ADB Rider premium
        double adbRiderPremium = 0;
        double sumOfriderCharges_P = 0;

        // for (int i = 1; i < 5; i++)
        for (int i = 1; i <= (policyTerm * 12); i++) {
            rowNumber++;
            // //System.out.println("********************************************* "+i+" Row Output *********************************************");
            BIMAST.setMonth_E(rowNumber);
            month_E = Integer.parseInt(BIMAST.getMonth_E());
            _month_E = month_E;
            // //System.out.println("1.   Month_E : "+BIMAST.getMonth_E());

            BIMAST.setYear_F();
            year_F = Integer.parseInt(BIMAST.getYear_F());
            _year_F = year_F;
            // //System.out.println("2.   Year_F : "+BIMAST.getYear_F());

            if ((_month_E % 12) == 0) {
                // //System.out.println("_year_F "+_year_F);
                bussIll.append("<policyYr" + _year_F + ">" + _year_F
                        + "</policyYr" + _year_F + ">");
                // //System.out.println("_year_F "+_year_F);

            }
            if (isKerlaDisc == true && _year_F <= 2) {
                serviceTax = 0.19;
            } else {
                serviceTax = 0.18;
            }
//            policyInForce_G = BIMAST.getPolicyInForce_G();
            // _policyInForce_G=BIMAST.getPolicyInForce_G();
            // //System.out.println("3.   PolicyInForce_G : "+BIMAST.getPolicyInForce_G());

            BIMAST.setAge_H(ageAtEntry);
            age_H = Integer.parseInt(BIMAST.getAge_H());
            _age_H = age_H;
            // //System.out.println("4.   Age_H : "+BIMAST.getAge_H());

            BIMAST.setPremium_I(policyTerm, PF, effectivePremium);
            premium_I = BIMAST.getPremium_I();
            // _premium_I=premium_I;
            // //System.out.println("5.   Premium_I : "+BIMAST.getPremium_I());

            sum_I += Double.parseDouble(premium_I);
            if ((_month_E % 12) == 0) {
                // //System.out.println("value"
                // +commonForAllProd.getStringWithout_E(_premium_I));
                // //System.out.println("value" +_premium_I);
                bussIll.append("<regPrem" + _year_F + ">"
                        + commonForAllProd.getStringWithout_E(sum_I)
                        + "</regPrem" + _year_F + ">");

            }

            BIMAST.setTopUpPremium_J(prop.topUpStatus, effectiveTopUpPrem,
                    addTopUp);
            topUpPremium_J = BIMAST.getTopUpPremium_J();
            // _topUpPremium_J=topUpPremium_J;
            // //System.out.println("6.   TopUpPremium_J : "+BIMAST.getTopUpPremium_J());
            sum_J += Double.parseDouble(topUpPremium_J);

            BIMAST.setPremiumAllocationCharge_K(staffDisc, bancAssuranceDisc);
            premiumAllocationCharge_K = BIMAST.getPremiumAllocationCharge_K();
            // _premiumAllocationCharge_K=premiumAllocationCharge_K;
            // //System.out.println("7.   PremiumAllocationCharge_K : "+BIMAST.getPremiumAllocationCharge_K());

            sum_K += Double.parseDouble(premiumAllocationCharge_K);
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
//            topUpCharges_L = BIMAST.getTopUpCharges_L();
            // _topUpCharges_L=topUpCharges_L;
            // //System.out.println("8.   TopUpCharges_L : "+BIMAST.getTopUpCharges_L());

            BIMAST.setServiceTaxOnAllocation_M(prop.allocationCharges,
                    serviceTax);
//            serviceTaxOnAllocation_M = BIMAST.getServiceTaxOnAllocation_M();
            // _serviceTaxOnAllocation_M=serviceTaxOnAllocation_M;
            // //System.out.println("9.   ServiceTaxOnAllocation_M : "+BIMAST.getServiceTaxOnAllocation_M());

            BIMAST.setAmountAvailableForInvestment_N();
            amountAvailableForInvestment_N = BIMAST.getAmountAvailableForInvestment_N();
            // _amountAvailableForInvestment_N=amountAvailableForInvestment_N;
            // //System.out.println("10.   AmountAvailableForInvestment_N : "+BIMAST.getAmountAvailableForInvestment_N());

            sum_N += Double.parseDouble(amountAvailableForInvestment_N);
            if ((_month_E % 12) == 0) {
                bussIll.append("<AmtAvlForInvst"
                        + _year_F
                        + ">"
                        + commonForAllProd.getRoundUp(commonForAllProd
                        .getStringWithout_E(sum_N))
                        + "</AmtAvlForInvst" + _year_F + ">");
                sum_N = 0;
            }

            BIMAST.setSumAssuredRelatedCharges_O(policyTerm, sumAssured, SAMF,
                    effectivePremium, prop.charge_SumAssuredBase);
//            sumAssuredRelatedCharges_O = BIMAST
//                    .getSumAssuredRelatedCharges_O();
            // _sumAssuredRelatedCharges_O=sumAssuredRelatedCharges_O;
            // //System.out.println("11.   SumAssuredRelatedCharges_O : "+sumAssuredRelatedCharges_O);

            BIMAST.setRiderCharges_P(prop.riderCharges,
                    SaralMahaAnandBean.getIsADBchecked(), ageAtEntry, staffDisc,
                    bancAssuranceDisc, policyTerm, PF,
                    SaralMahaAnandBean.getTermADB(),
                    SaralMahaAnandBean.getSumAssuredADB());
            riderCharges_P = BIMAST.getRiderCharges_P();
            // _riderCharges_P=riderCharges_P;
            // //System.out.println("12.   RiderCharges_P : "+riderCharges_P);

            // Calculate ADB Rider Premium

            sumOfriderCharges_P += Double.parseDouble(riderCharges_P);
            sum_P += Double.parseDouble(riderCharges_P);
            if ((_month_E % 12) == 0) {
                bussIll.append("<riderchrg4pr" + _year_F + ">"
                        + Math.round(sum_P) + "</riderchrg4pr" + _year_F + ">");
                bussIll.append("<riderchrg8pr" + _year_F + ">"
                        + Math.round(sum_P) + "</riderchrg8pr" + _year_F + ">");
                sum_P = 0;
            }

            BIMAST.setPolicyAdministrationCharge_Q(Double.parseDouble(policyAdministrationCharge_Q), prop.charge_Inflation, prop.fixedMonthlyExp_RP, prop.inflation_pa_RP);
            policyAdministrationCharge_Q = BIMAST
                    .getPolicyAdministrationCharge_Q();
            // _policyAdministrationCharge_Q=policyAdministrationCharge_Q;
            // //System.out.println("13.   PolicyAdministrationCharge_Q : "+policyAdministrationCharge_Q);

            sum_Q += Double.parseDouble(policyAdministrationCharge_Q);
            if ((_month_E % 12) == 0) {
                bussIll.append("<PolicyAdmCharge"
                        + _year_F
                        + ">"
                        + commonForAllProd.getRoundUp(commonForAllProd
                        .getStringWithout_E(sum_Q))
                        + "</PolicyAdmCharge" + _year_F + ">");
                sum_Q = 0;
            }

            BIMAST.setOneHundredPercentOfCummulativePremium_AW(Double.parseDouble(oneHundredPercentOfCummulativePremium_AW));
            oneHundredPercentOfCummulativePremium_AW = BIMAST.getOneHundredPercentOfCummulativePremium_AW();
            // _oneHundredPercentOfCummulativePremium_AW=oneHundredPercentOfCummulativePremium_AW;
            // //System.out.println("45.   OneHundredPercentOfCummulativePremium_AW : "+oneHundredPercentOfCummulativePremium_AW);

            BIMAST.setGuaranteedAddition_AA(effectivePremium, policyTerm,
                    prop.asPercentOfFirstYrPremium);
            guaranteedAddition_AA = BIMAST
                    .getGuaranteedAddition_AA();
            // _guaranteedAddition_AA=guaranteedAddition_AA;
            // //System.out.println("23.   GuaranteedAddition_AA : "+guaranteedAddition_AA);

            sum_AA += Double.parseDouble(guaranteedAddition_AA);

            if ((_month_E % 12) == 0) {
                sum_AA = Double
                        .parseDouble(commonForAllProd
                                .getRoundUp(commonForAllProd
                                        .getStringWithout_E(sum_AA)));
                bussIll.append("<GuareentedAdd4Pr"
                        + _year_F
                        + ">"
                        + commonForAllProd.getStringWithout_E(Math
                        .round(sum_AA)) + "</GuareentedAdd4Pr"
                        + _year_F + ">");

            }

            BIMAST.setMortalityCharges_R(Double.parseDouble(fundValueAtEnd_AB), policyTerm,
                    forBIArray, ageAtEntry, sumAssured, prop.mortalityCharges);
            mortalityCharges_R = BIMAST
                    .getMortalityCharges_R();
            // _mortalityCharges_R=mortalityCharges_R;
            // //System.out.println("14.   MortalityCharges_R : "+mortalityCharges_R);

            sum_R += Double.parseDouble(mortalityCharges_R);
            if ((_month_E % 12) == 0) {
                bussIll.append("<MortChrg4Pr"
                        + _year_F
                        + ">"
                        + commonForAllProd.getRoundUp(commonForAllProd
                        .getStringWithout_E(sum_R)) + "</MortChrg4Pr"
                        + _year_F + ">");
                sum_R = 0;
            }

            BIMAST.setTotalCharges_S(policyTerm);
            totalCharges_S = BIMAST.getTotalCharges_S();
            // _totalCharges_S=totalCharges_S;
            // //System.out.println("15.   TotalCharges_S : "+totalCharges_S);

            sum_S += Double.parseDouble(totalCharges_S);
            if ((_month_E % 12) == 0) {
                bussIll.append("<TotalCharges4Pr"
                        + _year_F
                        + ">"
                        + commonForAllProd.getRoundUp(commonForAllProd
                        .getStringWithout_E(sum_S))
                        + "</TotalCharges4Pr" + _year_F + ">");
                sum_S = 0;
            }

            BIMAST.setTotalServiceTax_ExclOfSTonAllocAndSurr_T(serviceTax,
                    prop.mortalityAndRiderCharges, prop.administrationCharges,
                    prop.riderCharges);
//            totalServiceTax_exclOfSTonAllocAndSurr_T = BIMAST.getTotalServiceTax_ExclOfSTonAllocAndSurr_T();
            // _totalServiceTax_exclOfSTonAllocAndSurr_T=totalServiceTax_exclOfSTonAllocAndSurr_T;
            // //System.out.println("16.   TotalServiceTax_exclOfSTonAllocAndSurr_T : "+totalServiceTax_exclOfSTonAllocAndSurr_T);

            BIMAST.setAdditionToFundIfAny_V(Double.parseDouble(fundValueAtEnd_AB), policyTerm, prop.int1);
            additionToFundIfAny_V = BIMAST
                    .getAdditionToFundIfAny_V();
            // _additionToFundIfAny_V=additionToFundIfAny_V;
            // //System.out.println("18.   AdditionToFundIfAny_V : "+additionToFundIfAny_V);

            sum_V += Double.parseDouble(additionToFundIfAny_V);
            if ((_month_E % 12) == 0) {
                bussIll.append("<AddToFundIfAny4Pr"
                        + _year_F
                        + ">"
                        + commonForAllProd.getRoundUp(commonForAllProd.getRoundOffLevel2(commonForAllProd
                        .getStringWithout_E(sum_V)))
                        + "</AddToFundIfAny4Pr" + _year_F + ">");
                sum_V = 0;
            }
            BIMAST.setFundBeforeFMC_W(Double.parseDouble(fundValueAtEnd_AB), policyTerm);
//            fundBeforeFMC_W = BIMAST.getFundBeforeFMC_W();
            // _fundBeforeFMC_W=fundBeforeFMC_W;
            // //System.out.println("19.   FundBeforeFMC_W : "+fundBeforeFMC_W);

            BIMAST.setFundManagementCharge_X(policyTerm,
                    SaralMahaAnandBean.getPercentToBeInvested_EquityFund(),
                    SaralMahaAnandBean.getPercentToBeInvested_BondFund(),
                    SaralMahaAnandBean.getPercentToBeInvested_BalancedFund(),
                    SaralMahaAnandBean.getPercentToBeInvested_IndexFund());
            fundManagementCharge_X = BIMAST
                    .getFundManagementCharge_X();
            // _fundManagementCharge_X=fundManagementCharge_X;
            // //System.out.println("20.   FundManagementCharge_X : "+fundManagementCharge_X);

            sum_X += Double.parseDouble(fundManagementCharge_X);
            if ((_month_E % 12) == 0) {
                bussIll.append("<FundMgmtChrg4Pr"
                        + _year_F
                        + ">"
                        + commonForAllProd.getRoundUp(commonForAllProd
                        .getStringWithout_E(sum_X))
                        + "</FundMgmtChrg4Pr" + _year_F + ">");
                sum_X = 0;
            }

            BIMAST.setServiceTaxOnFMC_Y(prop.fundManagementCharges,
                    serviceTax,
                    SaralMahaAnandBean.getPercentToBeInvested_EquityFund(),
                    SaralMahaAnandBean.getPercentToBeInvested_BondFund(),
                    SaralMahaAnandBean.getPercentToBeInvested_BalancedFund(),
                    SaralMahaAnandBean.getPercentToBeInvested_IndexFund());
            serviceTaxOnFMC_Y = BIMAST
                    .getServiceTaxOnFMC_Y();
            // _serviceTaxOnFMC_Y=serviceTaxOnFMC_Y;
            // //System.out.println("21.   ServiceTaxOnFMC_Y : "+serviceTaxOnFMC_Y);

            BIMAST.setTotalServiceTax_U(prop.riderCharges);
            totalServiceTax_U = BIMAST
                    .getTotalServiceTax_U();
            // _totalServiceTax_U=totalServiceTax_U;
            // //System.out.println("17.   TotalServiceTax_U : "+totalServiceTax_U);

            sum_U += Double.parseDouble(totalServiceTax_U);
            if ((_month_E % 12) == 0) {
                bussIll.append("<TotalSerTax4Pr"
                        + _year_F
                        + ">"
                        + commonForAllProd.getRoundUp(commonForAllProd
                        .getStringWithout_E(sum_U))
                        + "</TotalSerTax4Pr" + _year_F + ">");
                sum_U = 0;
            }

            BIMAST.setFundValueAfterFMCandBeforeGA_Z(policyTerm);
            fundValueAfterFMCandBeforeGA_Z = BIMAST
                    .getFundValueAfterFMCandBeforeGA_Z();
            // _fundValueAfterFMCandBeforeGA_Z=fundValueAfterFMCandBeforeGA_Z;
//			 System.out.println("22.   FundValueAfterFMCandBeforeGA_Z : "+fundValueAfterFMCandBeforeGA_Z);

            if ((_month_E % 12) == 0) {
                sum_Z = Double.parseDouble(commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(Double.parseDouble(fundValueAfterFMCandBeforeGA_Z))));
                bussIll.append("<fundaftrFMCbeforeGA4Pr"
                        + _year_F
                        + ">"
                        + commonForAllProd.getRoundUp(commonForAllProd
                        .getStringWithout_E(sum_Z))
                        + "</fundaftrFMCbeforeGA4Pr" + _year_F + ">");

            }
            BIMAST.setFundValueAtEnd_AB();
            fundValueAtEnd_AB = BIMAST
                    .getFundValueAtEnd_AB();
            // _fundValueAtEnd_AB=fundValueAtEnd_AB;
            // //System.out.println("24.   FundValueAtEnd_AB : "+fundValueAtEnd_AB);

            if ((_month_E % 12) == 0) {
                // String AB=String.valueOf(sum_AA+sum_Z);
//				System.out.println(sum_AA + "+" + sum_Z + "="
//						+ (sum_AA + sum_Z));
                sum_AB = (sum_AA + sum_Z);
                bussIll.append("<FundValAtEnd4Pr"
                        + _year_F
                        + ">"
                        + commonForAllProd.getRoundUp(commonForAllProd
                        .getStringWithout_E(sum_AB))
                        + "</FundValAtEnd4Pr" + _year_F + ">");
                sum_AA = 0;
                // sum_AB=0;
            }
            BIMAST.setSurrenderCap_AV(effectivePremium);
            surrenderCap_AV = BIMAST.getSurrenderCap_AV();
            // _surrenderCap_AV=surrenderCap_AV;
            // //System.out.println("44.   SurrenderCap_AV : "+surrenderCap_AV);

            BIMAST.setSurrenderCharges_AC(effectivePremium, policyTerm);
            surrenderCharges_AC = BIMAST
                    .getSurrenderCharges_AC();
            // _surrenderCharges_AC=surrenderCharges_AC;
            // //System.out.println("25.   SurrenderCharges_AC : "+surrenderCharges_AC);
            // sum_AC+=_surrenderCharges_AC;
            if ((_month_E % 12) == 0) {
                // BIMAST.setSurrenderCharges_Y(fundValueAtEnd_AB, policyTerm);
                // surrenderCharges_Y = BIMAST
                // .getSurrenderCharges_Y());
                /***** Modified By - Priyanka Warekar - 27-08-2015 - Start */
                BIMAST.setSurrenderCharges_Y(Double.parseDouble(fundValueAtEnd_AB), policyTerm,
                        sum_I, SaralMahaAnandBean.getPremiumAmount());
                /***** Modified By - Priyanka Warekar - 27-08-2015 - End */

                _surrenderCharges_Y = BIMAST
                        .getSurrenderCharges_Y();
                // sum_AE = _surrenderValue_AE;
                bussIll.append("<SurrenderChrg4Pr"
                        + _year_F
                        + ">"
                        + commonForAllProd.getRoundUp(commonForAllProd
                        .getStringWithout_E(Double.parseDouble(_surrenderCharges_Y)))
                        + "</SurrenderChrg4Pr" + _year_F + ">");
                // sum_AE=0;
            }

            BIMAST.setServiceTaxOnSurrenderCharges_AD(prop.surrenderCharges,
                    serviceTax);
//            serviceTaxOnSurrenderCharges_AD = BIMAST
//                    .getServiceTaxOnSurrenderCharges_AD();
            // _serviceTaxOnSurrenderCharges_AD=serviceTaxOnSurrenderCharges_AD;
            // //System.out.println("26.   ServiceTaxOnSurrenderCharges_AD : "+serviceTaxOnSurrenderCharges_AD);

            BIMAST.setSurrenderValue_AE();
            surrenderValue_AE = BIMAST
                    .getSurrenderValue_AE();
            // _surrenderValue_AE=surrenderValue_AE;
            // //System.out.println("27.   SurrenderValue_AE: "+surrenderValue_AE);

            if ((_month_E % 12) == 0) {
                sum_AE = Double.parseDouble(surrenderValue_AE);
                bussIll.append("<SurrenderVal4Pr"
                        + _year_F
                        + ">"
                        + commonForAllProd.getRoundUp(commonForAllProd
                        .getStringWithout_E(sum_AE))
                        + "</SurrenderVal4Pr" + _year_F + ">");
                // sum_AE=0;
            }

            BIMAST.setDeathBenefit_AF(policyTerm, sumAssured);
            deathBenefit_AF = BIMAST.getDeathBenefit_AF();
            // _deathBenefit_AF=deathBenefit_AF;
            // //System.out.println("28.   DeathBenefit_AF: "+deathBenefit_AF);

            if ((_month_E % 12) == 0) {

                bussIll.append("<DeathBen4Pr"
                        + _year_F
                        + ">"
                        + commonForAllProd.getRoundUp(String
                        .valueOf(deathBenefit_AF)) + "</DeathBen4Pr"
                        + _year_F + ">");

            }

            BIMAST.setMortalityCharges_AG(Double.parseDouble(fundValueAtEnd_AQ),
                    prop.mortalityCharges, sumAssured, policyTerm, forBIArray,
                    ageAtEntry);
            mortalityCharges_AG = BIMAST
                    .getMortalityCharges_AG();
            // _mortalityCharges_AG=mortalityCharges_AG;
            // //System.out.println("29.   MortalityCharges_AG: "+mortalityCharges_AG);

            sum_AG += Double.parseDouble(mortalityCharges_AG);
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
            totalCharges_AH = BIMAST.getTotalCharges_AH();
            // _totalCharges_AH=totalCharges_AH;
            // //System.out.println("30.   TotalCharges_AH: "+totalCharges_AH);

            sum_AH += Double.parseDouble(totalCharges_AH);
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
                    prop.administrationCharges, prop.riderCharges);
            totalServiceTax_ExclOfSTonAllocAndsurr_AI = BIMAST.getTotalServiceTax_ExclOfSTonAllocAndSurr_AI();
            // _totalServiceTax_ExclOfSTonAllocAndsurr_AI=totalServiceTax_ExclOfSTonAllocAndsurr_AI;
            // //System.out.println("31.   TotalServiceTax_ExclOfSTonAllocAndsurr_AI: "+totalServiceTax_ExclOfSTonAllocAndsurr_AI);

            BIMAST.setAdditionToFundIfAny_AK(Double.parseDouble(fundValueAtEnd_AQ), policyTerm, prop.int2);
            additionToFundIfAny_AK = BIMAST
                    .getAdditionToFundIfAny_AK();
            // _additionToFundIfAny_AK=additionToFundIfAny_AK;
            // //System.out.println("33.   AdditionToFundIfAny_AK: "+additionToFundIfAny_AK);

            sum_AK += Double.parseDouble(additionToFundIfAny_AK);
            if ((_month_E % 12) == 0) {
                bussIll.append("<AddToFundIfAny8Pr"
                        + _year_F
                        + ">"
                        + commonForAllProd.getRoundUp(commonForAllProd
                        .getStringWithout_E(sum_AK))
                        + "</AddToFundIfAny8Pr" + _year_F + ">");
                sum_AK = 0;
            }

            BIMAST.setFundBeforeFMC_AL(Double.parseDouble(fundValueAtEnd_AQ), policyTerm);
            fundBeforeFMC_AL = BIMAST.getFundBeforeFMC_AL();
            // _fundBeforeFMC_AL=fundBeforeFMC_AL;
            // //System.out.println("34.   FundBeforeFMC_AL: "+fundBeforeFMC_AL);

            BIMAST.setFundManagementCharge_AM(policyTerm,
                    SaralMahaAnandBean.getPercentToBeInvested_EquityFund(),
                    SaralMahaAnandBean.getPercentToBeInvested_BondFund(),
                    SaralMahaAnandBean.getPercentToBeInvested_BalancedFund(),
                    SaralMahaAnandBean.getPercentToBeInvested_IndexFund());
            fundManagementCharge_AM = BIMAST
                    .getFundManagementCharge_AM();
            // _fundManagementCharge_AM=fundManagementCharge_AM;
            // //System.out.println("35.   FundManagementCharge_AM: "+fundManagementCharge_AM);

            sum_AM += Double.parseDouble(fundManagementCharge_AM);
            if ((_month_E % 12) == 0) {

                bussIll.append("<FundMgmtChrg8Pr"
                        + _year_F
                        + ">"
                        + commonForAllProd.getRoundUp(commonForAllProd
                        .getStringWithout_E(sum_AM))
                        + "</FundMgmtChrg8Pr" + _year_F + ">");
                sum_AM = 0;
            }

            BIMAST.setServiceTaxOnFMC_AN(prop.fundManagementCharges,
                    serviceTax,
                    SaralMahaAnandBean.getPercentToBeInvested_EquityFund(),
                    SaralMahaAnandBean.getPercentToBeInvested_BondFund(),
                    SaralMahaAnandBean.getPercentToBeInvested_BalancedFund(),
                    SaralMahaAnandBean.getPercentToBeInvested_IndexFund());
            serviceTaxOnFMC_AN = BIMAST
                    .getServiceTaxOnFMC_AN();
            // _serviceTaxOnFMC_AN=serviceTaxOnFMC_AN;
            // //System.out.println("36.   ServiceTaxOnFMC_AN: "+serviceTaxOnFMC_AN);

            BIMAST.setFundValueAfterFMCandBeforeGA_AO(policyTerm);
            fundValueAfterFMCAndBeforeGA_AO = BIMAST
                    .getFundValueAfterFMCandBeforeGA_AO();
            // _fundValueAfterFMCAndBeforeGA_AO=fundValueAfterFMCAndBeforeGA_AO;
            // //System.out.println("37.   FundValueAfterFMCAndBeforeGA_AO: "+fundValueAfterFMCAndBeforeGA_AO);

            if ((_month_E % 12) == 0) {
                sum_AO = Double
                        .parseDouble(commonForAllProd.getRoundUp(commonForAllProd
                                .getStringWithout_E(Double.parseDouble(fundValueAfterFMCAndBeforeGA_AO))));
                bussIll.append("<fundaftrFMCbeforeGA8Pr"
                        + _year_F
                        + ">"
                        + commonForAllProd.getRoundUp(commonForAllProd
                        .getStringWithout_E(sum_AO))
                        + "</fundaftrFMCbeforeGA8Pr" + _year_F + ">");

            }

            BIMAST.setGuaranteedAddition_AP(effectivePremium, policyTerm,
                    prop.asPercentOfFirstYrPremium);
            guaranteedAddition_AP = BIMAST
                    .getGuaranteedAddition_AP();
            // _guaranteedAddition_AP=guaranteedAddition_AP;
            // //System.out.println("38.   GuaranteedAddition_AP : "+guaranteedAddition_AP);

            sum_AP += Double.parseDouble(guaranteedAddition_AP);
            if ((_month_E % 12) == 0) {
                sum_AP = Double
                        .parseDouble(commonForAllProd
                                .getRoundUp(commonForAllProd
                                        .getStringWithout_E(sum_AP)));
                bussIll.append("<GuaranteedAdd8Pr"
                        + _year_F
                        + ">"
                        + commonForAllProd.getStringWithout_E(Math
                        .round(sum_AP)) + "</GuaranteedAdd8Pr"
                        + _year_F + ">");

            }
            BIMAST.setFundValueAtEnd_AQ();
            fundValueAtEnd_AQ = BIMAST
                    .getFundValueAtEnd_AQ();
            // _fundValueAtEnd_AQ=fundValueAtEnd_AQ;
            // //System.out.println("39.   FundValueAtEnd_AQ : "+fundValueAtEnd_AQ);

            if ((_month_E % 12) == 0) {
                // String AQ=""+(sum_AO+sum_AP);
//				System.out.println(sum_AO
//						+ "+"
//						+ sum_AP
//						+ "="
//						+ (commonForAllProd
//								.getRoundUp(commonForAllProd
//										.getStringWithout_E(sum_AO))) + Double
//								.parseDouble(commonForAllProd
//										.getRoundUp(commonForAllProd
//												.getStringWithout_E(sum_AP)))));
                sum_AQ = (sum_AO + sum_AP);
                bussIll.append("<FundValAtEnd8Pr"
                        + _year_F
                        + ">"
                        + commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E((Double
                        .parseDouble(commonForAllProd
                                .getRoundUp(commonForAllProd
                                        .getStringWithout_E(sum_AO))) + Double
                        .parseDouble(commonForAllProd
                                .getRoundUp(commonForAllProd
                                        .getStringWithout_E(sum_AP))))))
                        + "</FundValAtEnd8Pr" + _year_F + ">");
                sum_AP = 0;
                // sum_AB=0;
            }

            BIMAST.setDeathBenefit_AU(policyTerm, sumAssured);
            deathBenefit_AU = BIMAST.getDeathBenefit_AU();
            // _deathBenefit_AU=deathBenefit_AU;
            // //System.out.println("43.   DeathBenefit_AU: "+deathBenefit_AU);

            if ((_month_E % 12) == 0) {
                bussIll.append("<DeathBen8Pr"
                        + _year_F
                        + ">"
                        + commonForAllProd.getRoundUp(String
                        .valueOf(deathBenefit_AU)) + "</DeathBen8Pr"
                        + _year_F + ">");
            }

            BIMAST.setTotalServiceTax_AJ(prop.riderCharges);
            totalServiceTax_AJ = BIMAST
                    .getTotalServiceTax_AJ();
            // _totalServiceTax_AJ=totalServiceTax_AJ;
            // //System.out.println("32.   TotalServiceTax_AJ: "+totalServiceTax_AJ);

            sum_AJ += Double.parseDouble(totalServiceTax_AJ);
            if ((_month_E % 12) == 0) {
                bussIll.append("<TotalSerTax8Pr"
                        + _year_F
                        + ">"
                        + commonForAllProd.getRoundUp(commonForAllProd
                        .getStringWithout_E(sum_AJ))
                        + "</TotalSerTax8Pr" + _year_F + ">");
                sum_AJ = 0;
            }

            BIMAST.setSurrenderCharges_AR(effectivePremium, policyTerm);
            surrenderCharges_AR = BIMAST
                    .getSurrenderCharges_AR();
            // _surrenderCharges_AR=surrenderCharges_AR;
            // //System.out.println("40.   SurrenderCharges_AR : "+surrenderCharges_AR);

            if ((_month_E % 12) == 0) {
                /***** Modified By - Priyanka Warekar - 27-08-2015 - Start */
                // BIMAST.setSurrenderCharges_AO(fundValueAtEnd_AQ, policyTerm);
                // surrenderCharges_AO = BIMAST
                // .getSurrenderCharges_AO());

                BIMAST.setSurrenderCharges_AO(Double.parseDouble(fundValueAtEnd_AQ), policyTerm, sum_I, SaralMahaAnandBean.getPremiumAmount());
                /***** Modified By - Priyanka Warekar - 27-08-2015 - End */

                _surrenderCharges_AO = BIMAST
                        .getSurrenderCharges_AO();
                // sum_AE = _surrenderValue_AE;
                bussIll.append("<SurrenderChrg8Pr"
                        + _year_F
                        + ">"
                        + commonForAllProd.getRoundUp(commonForAllProd
                        .getStringWithout_E(Double.parseDouble(_surrenderCharges_AO)))
                        + "</SurrenderChrg8Pr" + _year_F + ">");
                // sum_AE=0;
            }

            BIMAST.setServiceTaxOnSurrenderCharges_AS(serviceTax,
                    prop.surrenderCharges);
            serviceTaxOnSurrenderCharges_AS = BIMAST
                    .getServiceTaxOnSurrenderCharges_AS();
            // _serviceTaxOnSurrenderCharges_AS=serviceTaxOnSurrenderCharges_AS;
            // //System.out.println("41.   ServiceTaxOnSurrenderCharges_AS: "+serviceTaxOnSurrenderCharges_AS);

            BIMAST.setSurrenderValue_AT();
            surrenderValue_AT = BIMAST
                    .getSurrenderValue_AT();
            // _surrenderValue_AT=surrenderValue_AT;
            if ((_month_E % 12) == 0) {
                sum_AT = Double.parseDouble(surrenderValue_AT);
                bussIll.append("<SurrenderVal8Pr"
                        + _year_F
                        + ">"
                        + commonForAllProd.getRoundUp(commonForAllProd
                        .getStringWithout_E(sum_AT))
                        + "</SurrenderVal8Pr" + _year_F + ">");

            }
            // //System.out.println("42.   SurrenderValue_AT: "+surrenderValue_AT);

            commission_AS = "" + BIMAST.getCommission_BL(policyTerm, sum_I, sum_J, staffDisc);
            _commission_AS = commission_AS;
            // //System.out.println("Commission "+ _commission_BL);
            sum_AS += Double.parseDouble(_commission_AS);
            if ((_month_E % 12) == 0) {

                bussIll.append("<Commission" + _year_F + ">"
                        + commonForAllProd.getRoundUp(String.valueOf(sum_AS))
                        + "</Commission" + _year_F + ">");
                sum_AS = 0;
                sum_I = 0;
                sum_J = 0;
            }

        }


        // Calculate ADB Rider Premium
        if (SaralMahaAnandBean.getIsADBchecked())
            adbRiderPremium = sumOfriderCharges_P * (1 + serviceTax);

        // //System.out.println("adb 1 " +
        // commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(adbRiderPremium)));

        // First Year Premium
        String totFirstYrPremium = commonForAllProd.getRoundUp(commonForAllProd
                .getStringWithout_E(adbRiderPremium
                        + SaralMahaAnandBean.getEffectivePremium()));

        // return new
        // String[]{(commonForAllProd.getStringWithout_E(sumAssured)),(commonForAllProd.getStringWithout_E(Double.parseDouble(U)+Double.parseDouble(V))),(commonForAllProd.getStringWithout_E(Double.parseDouble(AJ)+Double.parseDouble(AK))),(String.valueOf(SaralMahaAnandBean.getEffectivePremium())),(commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(adbRiderPremium))),(String.valueOf(totFirstYrPremium))};
        return new String[]{
                (commonForAllProd.getStringWithout_E(sumAssured)),
                commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(Double.parseDouble(fundValueAtEnd_AB))),
                commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(Double.parseDouble(fundValueAtEnd_AQ))),
                (String.valueOf(SaralMahaAnandBean.getEffectivePremium())),
                (commonForAllProd.getRoundUp(commonForAllProd
                        .getStringWithout_E(adbRiderPremium))),
                (String.valueOf(totFirstYrPremium))};

    }

    public String[] getOutputReductionYield(String sheetName,
                                            SaralMahaAnandBean SaralMahaAnandBean) {

        String[] arrGuaranteedAddition_AA = new String[12];
        String[] arrGuaranteedAddition_AP = new String[12];

        ArrayList<String> List_BN = new ArrayList<String>();
        ArrayList<String> List_BO = new ArrayList<String>();
        ArrayList<String> List_BS = new ArrayList<String>();

        // Output Variable Declaration
        // int _month_E=0,
        // _year_F=0,
        // _age_H=0;

        // String _policyInForce_G="Y";
        //
        // double
        // _oneHundredPercentOfCummulativePremium_AW=0,
        // _policyAdministrationCharge_Q=0,
        // _allocatedFundToIndexFund_P=0,
        // _guaranteedAddition_AA=0,
        // _guaranteedAddition_AP=0,
        // _dailyProtectFundAtEnd_AO=0,
        // _indexFundAtEnd_AP=0,
        // _fundValueAfterFMCandBeforeGA_Z=0,
        // _fundValueAtEnd_AB=0,
        // _indexFundAtEnd_BR=0,
        // _fundValueAfterFMCAndBeforeGA_AO=0,
        // _riderCharges_P=0,
        // _premium_I=0,
        // _topUpPremium_J=0,
        // _premiumAllocationCharge_K=0,
        // _topUpCharges_L=0,
        // _serviceTaxOnAllocation_M=0,
        // _amountAvailableForInvestment_N=0,
        // _transferPercentIfAny_O=0,
        // _sumAssuredRelatedCharges_O=0,
        // _optionCharges_R=0,
        // _transferOfIndexFundGainToDailyProtectFund_Y=0,
        // _transferOfCapitalFromIndexToDailyProtectFund_Z=0,
        // _dailyProtectFundAtStart_AA=0,
        // _guaranteeCharge_U=0,
        // _indexFundAtStart_AB=0,
        // _mortalityCharges_R=0,
        // _totalCharges_S=0,
        // _totalServiceTax_exclOfSTonAllocAndSurr_T=0,
        // _dailyProtectFundAfterCharges_AC=0,
        // _indexFundAtStartAfterCharges_AD=0,
        // _additionToIndexFund_AF=0,
        // _additionToDailyProtectFund_AE=0,
        // _additionToFundIfAny_V=0,
        // _fMCearnedOnDailyProtectFund_AG=0,
        // _fMCearnedOnIndexFund_AH=0,
        // _fundBeforeFMC_W=0,
        // _fundManagementCharge_X=0,
        // _serviceTaxOnFMC_Y=0,
        // _totalServiceTax_U=0,
        // _surrenderCap_AV=0,
        // _surrenderCharges_AC=0,
        // _serviceTaxOnSurrenderCharges_AD=0,
        // _surrenderValue_AE=0,
        // _deathBenefit_AF=0,
        // _transferOfCapitalFromIndexToDailyProtectFund_BB=0,
        // _fundValueAtEnd_AQ=0,
        // _mortalityCharges_AG=0,
        // _transferOfIndexFundGainToDailyProtectFund_BA=0,
        // _dailyProtectFundAtEnd_BQ=0,
        // _dailyProtectFundAtStart_BC=0,
        // _guaranteeCharge_AW=0,
        // _totalCharges_AH=0,
        // _totalServiceTax_ExclOfSTonAllocAndsurr_AI=0,
        // _indexFundAtStart_BD=0,
        // _indexFundAtStartAfterCharges_BF=0,
        // _additionToIndexFund_BH=0,
        // _fMCearnedOnIndexFund_BJ=0,
        // _dailyProtectFundAftercharges_BE=0,
        // _additionToDailyProtectFund_BG=0,
        // _fMCearnedOnDailyProtectFund_BI=0,
        // _additionToFundIfAny_AK=0,
        // _fundBeforeFMC_AL=0,
        // _fundManagementCharge_AM=0,
        // _serviceTaxOnFMC_AN=0,
        // _dailyProtectFundAtend_BQ=0,
        // _deathBenefit_AU=0,
        // _totalServiceTax_AJ=0,
        // _surrenderCharges_AR=0,
        // _serviceTaxOnSurrenderCharges_AS=0,
        // _surrenderValue_AT=0;
        //

        // Temp Variable Declaretion
//        int month_E = 0, month_BM = 0;
//        // _month_BM=0;
        int year_F = 0;
//        String policyInForce_G = "Y";
//        int age_H = 0;
//        double premium_I = 0;
//        double topUpPremium_J = 0;
//        double premiumAllocationCharge_K = 0;
//        double topUpCharges_L = 0;
//        double serviceTaxOnAllocation_M = 0;
//        double amountAvailableForInvestment_N = 0;
//        double transferPercentIfAny_O = 0;
//        double allocatedFundToIndexFund_P = 0;
//        double sumAssuredRelatedCharges_O = 0;
//        double riderCharges_P = 0;
//        double optionCharges_R = 0;
        double policyAdministrationCharge_Q = 0;
//        double transferOfIndexFundGainToDailyProtectFund_Y = 0;
//        double indexFundAtEnd = 0;
//        double transferOfCapitalFromIndexToDailyProtectFund_Z = 0;
//        double dailyProtectFundAtStart_AA = 0;
//        double dailyProtectFundAtEnd_AO = 0;
//        double guaranteeCharge_U = 0;
//        double indexFundAtStart_AB = 0;
        double oneHundredPercentOfCummulativePremium_AW = 0;
//        double guaranteedAddition_AA = 0;
//        double guaranteedAddition_AP = 0;
//        double mortalityCharges_R = 0;
//        double totalCharges_S = 0;
//        double totalServiceTax_exclOfSTonAllocAndSurr_T = 0;
//        double dailyProtectFundAfterCharges_AC = 0;
//        double indexFundAtStartAfterCharges_AD = 0;
//        double additionToIndexFund_AF = 0;
//        double additionToDailyProtectFund_AE = 0;
//        double additionToFundIfAny_V = 0;
//        double fMCearnedOnDailyProtectFund_AG = 0;
//        double fMCearnedOnIndexFund_AH = 0;
//        double fundBeforeFMC_W = 0;
//        double fundManagementCharge_X = 0;
//        double serviceTaxOnFMC_Y = 0;
//        double totalServiceTax_U = 0;
//        double fundValueAfterFMCandBeforeGA_Z = 0;
//        double indexFundAtEnd_AP = 0;
        double fundValueAtEnd_AB = 0;
//        double surrenderCap_AV = 0;
//        double surrenderCharges_AC = 0;
//        double serviceTaxOnSurrenderCharges_AD = 0;
//        double surrenderValue_AE = 0;
//        double deathBenefit_AF = 0;
//        double transferOfCapitalFromIndexToDailyProtectFund_BB = 0;
        double fundValueAtEnd_AQ = 0;
//        double mortalityCharges_AG = 0;
//        double transferOfIndexFundGainToDailyProtectFund_BA = 0;
//        double dailyProtectFundAtEnd_BQ = 0;
//        double dailyProtectFundAtStart_BC = 0;
//        double guaranteeCharge_AW = 0;
//        double totalCharges_AH = 0;
//        double totalServiceTax_ExclOfSTonAllocAndsurr_AI = 0;
//        double indexFundAtStart_BD = 0;
//        double indexFundAtStartAfterCharges_BF = 0;
//        double additionToIndexFund_BH = 0;
//        double fMCearnedOnIndexFund_BJ = 0;
//        double dailyProtectFundAftercharges_BE = 0;
//        double additionToDailyProtectFund_BG = 0;
//        double fMCearnedOnDailyProtectFund_BI = 0;
//        double additionToFundIfAny_AK = 0;
//        double fundBeforeFMC_AL = 0;
//        double fundManagementCharge_AM = 0;
//        double serviceTaxOnFMC_AN = 0;
//        double fundValueAfterFMCAndBeforeGA_AO = 0;
//        double indexFundAtEnd_BR = 0;
//        double deathBenefit_AU = 0;
//        double totalServiceTax_AJ = 0;
//        double surrenderCharges_AR = 0;
//        double serviceTaxOnSurrenderCharges_AS = 0;
//        double surrenderValue_AT = 0;

        double reductionYield_BN = 0,
                // _reductionYield_BN=0,
                reductionYield_BO = 0,
                // _reductionYield_BO=0,
                reductionYield_BS = 0,
                // _reductionYield_BS=0,
                irrAnnual_BN = 0,
                // _irrAnnual_BN=0,
                irrAnnual_BO = 0,
                // _irrAnnual_BO=0,
                irrAnnual_BS = 0,
                // _irrAnnual_BS=0,
                reductionInYieldMaturityAt = 0,
                // _reductionInYieldMaturityAt=0,
                reductionInYieldNumberOfYearsElapsedSinceInception = 0,
                // _reductionInYieldNumberOfYearsElapsedSinceInception=0,
                netYield4pa = 0, netYield8pa = 0;

        if (sheetName.equals("Reduction in Yeild_CAP")) {
            // Input Drop Downs from "BI_Incl_Mort & Ser Tax" sheet
            prop.allocationCharges = false; // *Sheet Name -> BI_Incl_Mort & Ser
            // Tax,*Cell ->B20
            prop.mortalityAndRiderCharges = false; // *Sheet Name ->
            // BI_Incl_Mort & Ser
            // Tax,*Cell ->B21
            prop.fundManagementCharges = false;
            prop.administrationCharges = false;// *Sheet Name -> BI_Incl_Mort &
            // Ser Tax,*Cell ->B23
            prop.surrenderCharges = false; // *Sheet Name -> BI_Incl_Mort & Ser
            // Tax,*Cell ->B24
            prop.mortalityCharges = false; // *Sheet Name -> BI_Incl_Mort & Ser
            // Tax,*Cell ->B27
            prop.riderCharges = false; // *Sheet Name -> BI_Incl_Mort & Ser
            // Tax,*Cell ->B29
            prop.topUpStatus = false; // *Sheet Name -> BI_Incl_Mort & Ser
            // Tax,*Cell ->B31

        }

        // From GUI Input
        boolean staffDisc = SaralMahaAnandBean.getIsForStaffOrNot();
        boolean bancAssuranceDisc = SaralMahaAnandBean.getIsBancAssuranceDiscOrNot();
        double SAMF = SaralMahaAnandBean.getSAMF();
        int ageAtEntry = SaralMahaAnandBean.getAgeAtEntry();

        //double serviceTax=SaralMahaAnandBean.getServiceTax();
        double serviceTax = 0;
        boolean isKerlaDisc = SaralMahaAnandBean.isKerlaDisc();

        // Input Drop Downs from GUI[But not displayed in GUI]
        String addTopUp = "No";
        // Internally Calculated Input Fields
        double effectivePremium = SaralMahaAnandBean.getEffectivePremium();
        int PF = SaralMahaAnandBean.getPF();
        int policyTerm = SaralMahaAnandBean.getPolicyTerm_Basic();
        double sumAssured = (effectivePremium * SAMF);
        SaralMahaAnandBean
                .setEffectiveTopUpPrem(addTopUp, prop.topUpPremiumAmt);
        double effectiveTopUpPrem = SaralMahaAnandBean.getEffectiveTopUpPrem();
        // Declaration of method Variables/Object required for calculation
        SaralMahaAnandBusinessLogic BIMAST = new SaralMahaAnandBusinessLogic();
        String[] forBIArray = BIMAST
                .getForBIArr(prop.mortalityCharges_AsPercentOfofLICtable);
        int rowNumber = 0, monthNumber = 0;
        int noOfYearsElapsedSinceInception = SaralMahaAnandBean
                .getYearsElapsedSinceInception();

        // ADB Rider premium
        double adbRiderPremium = 0;
        double sumOfriderCharges_P = 0;

        // for (int i = 1; i < 5; i++)
        for (int i = 0; i <= (policyTerm * 12); i++) {
            rowNumber++;
            // //System.out.println("********************************************* "+i+" Row Output *********************************************");
            BIMAST.setMonth_E(rowNumber);
//            month_E = Integer.parseInt(BIMAST.getMonth_E());
            // _month_E=month_E;
            // System.out.println("1.   Month_E : "+BIMAST.getMonth_E());

            BIMAST.setYear_F();
            year_F = Integer.parseInt(BIMAST.getYear_F());
            // _year_F=year_F;
            // System.out.println("2.   Year_F : "+BIMAST.getYear_F());
            if (isKerlaDisc == true && year_F <= 2) {
                serviceTax = 0.19;
            } else {
                serviceTax = 0.18;
            }
//            policyInForce_G = BIMAST.getPolicyInForce_G();
            // _policyInForce_G=BIMAST.getPolicyInForce_G();
            // System.out.println("3.   PolicyInForce_G : "+BIMAST.getPolicyInForce_G());

            BIMAST.setAge_H(ageAtEntry);
//            age_H = Integer.parseInt(BIMAST.getAge_H());
            // _age_H=age_H;
            // System.out.println("4.   Age_H : "+BIMAST.getAge_H());

            BIMAST.setPremium_I(policyTerm, PF, effectivePremium);
//            premium_I = Double.parseDouble(BIMAST.getPremium_I());
            // _premium_I=premium_I;
            // System.out.println("5.   Premium_I : "+BIMAST.getPremium_I());

            BIMAST.setTopUpPremium_J(prop.topUpStatus, effectiveTopUpPrem,
                    addTopUp);
//            topUpPremium_J = Double.parseDouble(BIMAST.getTopUpPremium_J());
            // _topUpPremium_J=topUpPremium_J;
            // System.out.println("6.   TopUpPremium_J : "+BIMAST.getTopUpPremium_J());

            BIMAST.setPremiumAllocationCharge_K(staffDisc, bancAssuranceDisc);
//            premiumAllocationCharge_K = Double.parseDouble(BIMAST
//                    .getPremiumAllocationCharge_K());
            // _premiumAllocationCharge_K=premiumAllocationCharge_K;
            // System.out.println("7.   PremiumAllocationCharge_K : "+BIMAST.getPremiumAllocationCharge_K());

            BIMAST.setTopUpCharges_L(prop.topUp);
//            topUpCharges_L = Double.parseDouble(BIMAST.getTopUpCharges_L());
            // _topUpCharges_L=topUpCharges_L;
            // System.out.println("8.   TopUpCharges_L : "+BIMAST.getTopUpCharges_L());

            BIMAST.setServiceTaxOnAllocation_M(prop.allocationCharges,
                    serviceTax);
//            serviceTaxOnAllocation_M = Double.parseDouble(BIMAST
//                    .getServiceTaxOnAllocation_M());
            // _serviceTaxOnAllocation_M=serviceTaxOnAllocation_M;
            // System.out.println("9.   ServiceTaxOnAllocation_M : "+BIMAST.getServiceTaxOnAllocation_M());

            BIMAST.setAmountAvailableForInvestment_N();
//            amountAvailableForInvestment_N = Double.parseDouble(BIMAST
//                    .getAmountAvailableForInvestment_N());
            // _amountAvailableForInvestment_N=amountAvailableForInvestment_N;
            // System.out.println("10.   AmountAvailableForInvestment_N : "+BIMAST.getAmountAvailableForInvestment_N());

            BIMAST.setSumAssuredRelatedCharges_O(policyTerm, sumAssured, SAMF,
                    effectivePremium, prop.charge_SumAssuredBase);
//            sumAssuredRelatedCharges_O = Double.parseDouble(BIMAST
//                    .getSumAssuredRelatedCharges_O());
            // _sumAssuredRelatedCharges_O=sumAssuredRelatedCharges_O;
            // System.out.println("11.   SumAssuredRelatedCharges_O : "+sumAssuredRelatedCharges_O);

            BIMAST.setRiderChargesReductionYield_P(prop.riderCharges,
                    SaralMahaAnandBean.getIsADBchecked(), ageAtEntry, staffDisc,
                    bancAssuranceDisc, policyTerm, PF,
                    SaralMahaAnandBean.getTermADB(),
                    SaralMahaAnandBean.getSumAssuredADB());
//            riderCharges_P = Double.parseDouble(BIMAST
//                    .getRiderChargesReductionYield_P());
            // _riderCharges_P=riderCharges_P;
            // System.out.println("12.   RiderCharges_P : "+riderCharges_P);

            BIMAST.setPolicyAdministrationCharge_Q(
                    policyAdministrationCharge_Q, prop.charge_Inflation,
                    prop.fixedMonthlyExp_RP, prop.inflation_pa_RP);
            policyAdministrationCharge_Q = Double.parseDouble(BIMAST
                    .getPolicyAdministrationCharge_Q());
            // _policyAdministrationCharge_Q=policyAdministrationCharge_Q;
            // System.out.println("13.   PolicyAdministrationCharge_Q : "+policyAdministrationCharge_Q);

            BIMAST.setMonth_BM(monthNumber);
//            month_BM = Integer.parseInt(BIMAST.getMonth_BM());
            // _month_BM=month_BM;
            // System.out.println("month_CB "+month_BM);

            BIMAST.setReductionYield_BN(policyTerm, fundValueAtEnd_AB);
            reductionYield_BN = Double.parseDouble(BIMAST
                    .getReductionYield_BN());
            // _reductionYield_BN=reductionYield_BN;
            // System.out.println("reductionYield_BN "+reductionYield_BN);

            BIMAST.setReductionYield_BO(policyTerm, fundValueAtEnd_AQ);
            reductionYield_BO = Double.parseDouble(BIMAST
                    .getReductionYield_BO());
            // _reductionYield_BO=reductionYield_BO;
            // System.out.println("reductionYield_BO "+reductionYield_BO);

            BIMAST.setReductionYield_BS(noOfYearsElapsedSinceInception,
                    fundValueAtEnd_AQ);
            reductionYield_BS = Double.parseDouble(BIMAST
                    .getReductionYield_BS());
            // _reductionYield_BS=reductionYield_BS;
            // System.out.println("reductionYield_BS "+reductionYield_BS);

            BIMAST.setOneHundredPercentOfCummulativePremium_AW(oneHundredPercentOfCummulativePremium_AW);
            oneHundredPercentOfCummulativePremium_AW = Double
                    .parseDouble(BIMAST
                            .getOneHundredPercentOfCummulativePremium_AW());
            // _oneHundredPercentOfCummulativePremium_AW=oneHundredPercentOfCummulativePremium_AW;
            // System.out.println("45.   OneHundredPercentOfCummulativePremium_AW : "+oneHundredPercentOfCummulativePremium_AW);

            BIMAST.setGuaranteedAddition_AA(effectivePremium, policyTerm,
                    prop.asPercentOfFirstYrPremium);
//            guaranteedAddition_AA = Double.parseDouble(BIMAST
//                    .getGuaranteedAddition_AA());
            // _guaranteedAddition_AA=guaranteedAddition_AA;
            // System.out.println("23.   GuaranteedAddition_AA : "+guaranteedAddition_AA);

            BIMAST.setMortalityChargesReductionYield_R(fundValueAtEnd_AB,
                    policyTerm, forBIArray, ageAtEntry, sumAssured,
                    prop.mortalityCharges);
//            mortalityCharges_R = Double.parseDouble(BIMAST
//                    .getMortalityChargesReductionYield_R());
            // _mortalityCharges_R=mortalityCharges_R;
            // System.out.println("14.   MortalityCharges_R : "+mortalityCharges_R);

            BIMAST.setTotalChargesReductionYield_S(policyTerm);
//            totalCharges_S = Double.parseDouble(BIMAST
//                    .getTotalChargesReductionYield_S());
            // _totalCharges_S=totalCharges_S;
            // System.out.println("15.   TotalCharges_S : "+totalCharges_S);

            BIMAST.setTotalServiceTax_ExclOfSTonAllocAndSurrReductionYield_T(
                    serviceTax, prop.mortalityAndRiderCharges,
                    prop.administrationCharges, prop.riderCharges);
//            totalServiceTax_exclOfSTonAllocAndSurr_T = Double
//                    .parseDouble(BIMAST
//                            .getTotalServiceTax_ExclOfSTonAllocAndSurrReductionYield_T());
            // ////////
            // _totalServiceTax_exclOfSTonAllocAndSurr_T=totalServiceTax_exclOfSTonAllocAndSurr_T;
            // System.out.println("16.   TotalServiceTax_exclOfSTonAllocAndSurr_T : "+totalServiceTax_exclOfSTonAllocAndSurr_T);

            BIMAST.setAdditionToFundIfAnyReductionYield_V(fundValueAtEnd_AB,
                    policyTerm, prop.int1);
//            additionToFundIfAny_V = Double.parseDouble(BIMAST
//                    .getAdditionToFundIfAnyReductionYield_V());
            // ////////////////////////////////////////////////
            // _additionToFundIfAny_V=additionToFundIfAny_V;
            // System.out.println("18.   AdditionToFundIfAny_V : "+additionToFundIfAny_V);

            BIMAST.setFundBeforeFMCReductionYield_W(fundValueAtEnd_AB,
                    policyTerm);
//            fundBeforeFMC_W = Double.parseDouble(BIMAST
//                    .getFundBeforeFMCReductionYield_W());
            // _fundBeforeFMC_W=fundBeforeFMC_W;
            // System.out.println("19.   FundBeforeFMC_W : "+fundBeforeFMC_W);

            BIMAST.setFundManagementChargeReductionYield_X(policyTerm,
                    SaralMahaAnandBean.getPercentToBeInvested_EquityFund(),
                    SaralMahaAnandBean.getPercentToBeInvested_BondFund(),
                    SaralMahaAnandBean.getPercentToBeInvested_BalancedFund(),
                    SaralMahaAnandBean.getPercentToBeInvested_IndexFund());
//            fundManagementCharge_X = Double.parseDouble(BIMAST
//                    .getFundManagementChargeReductionYield_X());
            // _fundManagementCharge_X=fundManagementCharge_X;
            // System.out.println("20.   FundManagementCharge_X : "+fundManagementCharge_X);

            BIMAST.setServiceTaxOnFMCReductionYield_Y(
                    prop.fundManagementCharges, serviceTax,
                    SaralMahaAnandBean.getPercentToBeInvested_EquityFund(),
                    SaralMahaAnandBean.getPercentToBeInvested_BondFund(),
                    SaralMahaAnandBean.getPercentToBeInvested_BalancedFund(),
                    SaralMahaAnandBean.getPercentToBeInvested_IndexFund());
//            serviceTaxOnFMC_Y = Double.parseDouble(BIMAST
//                    .getServiceTaxOnFMCReductionYield_Y());
            // _serviceTaxOnFMC_Y=serviceTaxOnFMC_Y;
            // System.out.println("21.   ServiceTaxOnFMC_Y : "+serviceTaxOnFMC_Y);

            BIMAST.setTotalServiceTaxReductionYield_U(prop.riderCharges, serviceTax);
//            totalServiceTax_U = Double.parseDouble(BIMAST
//                    .getTotalServiceTaxReductionYield_U());
            // _totalServiceTax_U=totalServiceTax_U;
            // System.out.println("17.   TotalServiceTax_U : "+totalServiceTax_U);

            BIMAST.setFundValueAfterFMCandBeforeGAReductionYield_Z(policyTerm);
//            fundValueAfterFMCandBeforeGA_Z = Double.parseDouble(BIMAST
//                    .getFundValueAfterFMCandBeforeGAReductionYield_Z());
            // _fundValueAfterFMCandBeforeGA_Z=fundValueAfterFMCandBeforeGA_Z;
            // System.out.println("22.   FundValueAfterFMCandBeforeGA_Z : "+fundValueAfterFMCandBeforeGA_Z);

            BIMAST.setFundValueAtEndReductionYield_AB();
            fundValueAtEnd_AB = Double.parseDouble(BIMAST
                    .getFundValueAtEndReductionYield_AB());
            // _fundValueAtEnd_AB=fundValueAtEnd_AB;
            // System.out.println("24.   FundValueAtEnd_AB : "+fundValueAtEnd_AB);

            BIMAST.setSurrenderCap_AV(effectivePremium);
//            surrenderCap_AV = Double.parseDouble(BIMAST.getSurrenderCap_AV());
            // _surrenderCap_AV=surrenderCap_AV;
            // System.out.println("44.   SurrenderCap_AV : "+surrenderCap_AV);

            BIMAST.setSurrenderChargesReductionYield_AC(effectivePremium,
                    policyTerm);
//            surrenderCharges_AC = Double.parseDouble(BIMAST
//                    .getSurrenderChargesReductionYield_AC());
            // _surrenderCharges_AC=surrenderCharges_AC;
            // System.out.println("25.   SurrenderCharges_AC : "+surrenderCharges_AC);

            BIMAST.setServiceTaxOnSurrenderChargesReductionYield_AD(
                    prop.surrenderCharges, serviceTax);
//            serviceTaxOnSurrenderCharges_AD = Double.parseDouble(BIMAST
//                    .getServiceTaxOnSurrenderChargesReductionYield_AD());
            // _serviceTaxOnSurrenderCharges_AD=serviceTaxOnSurrenderCharges_AD;
            // System.out.println("26.   ServiceTaxOnSurrenderCharges_AD : "+serviceTaxOnSurrenderCharges_AD);

            BIMAST.setSurrenderValueReductionYield_AE();
//            surrenderValue_AE = Double.parseDouble(BIMAST
//                    .getSurrenderValueReductionYield_AE());
            // _surrenderValue_AE=surrenderValue_AE;
            // System.out.println("27.   SurrenderValue_AE: "+surrenderValue_AE);

            BIMAST.setDeathBenefitReductionYield_AF(policyTerm, sumAssured);
//            deathBenefit_AF = Double.parseDouble(BIMAST
//                    .getDeathBenefitReductionYield_AF());
            // _deathBenefit_AF=deathBenefit_AF;
            // System.out.println("28.   DeathBenefit_AF: "+deathBenefit_AF);

            BIMAST.setMortalityChargesReductionYield_AG(fundValueAtEnd_AQ,
                    prop.mortalityCharges, sumAssured, policyTerm, forBIArray,
                    ageAtEntry);
//            mortalityCharges_AG = Double.parseDouble(BIMAST
//                    .getMortalityChargesReductionYield_AG());
            // _mortalityCharges_AG=mortalityCharges_AG;
            // System.out.println("29.   MortalityCharges_AG: "+mortalityCharges_AG);

            BIMAST.setTotalChargesReductionYield_AH(policyTerm);
//            totalCharges_AH = Double.parseDouble(BIMAST
//                    .getTotalChargesReductionYield_AH());
            // _totalCharges_AH=totalCharges_AH;
            // System.out.println("30.   TotalCharges_AH: "+totalCharges_AH);

            BIMAST.setTotalServiceTax_ExclOfSTonAllocAndSurrReductionYield_AI(
                    serviceTax, prop.mortalityAndRiderCharges,
                    prop.administrationCharges, prop.riderCharges);
//            totalServiceTax_ExclOfSTonAllocAndsurr_AI = Double
//                    .parseDouble(BIMAST
//                            .getTotalServiceTax_ExclOfSTonAllocAndSurrReductionYield_AI());
            // _totalServiceTax_ExclOfSTonAllocAndsurr_AI=totalServiceTax_ExclOfSTonAllocAndsurr_AI;
            // System.out.println("31.   TotalServiceTax_ExclOfSTonAllocAndsurr_AI: "+totalServiceTax_ExclOfSTonAllocAndsurr_AI);

            BIMAST.setAdditionToFundIfAnyReductionYield_AK(fundValueAtEnd_AQ,
                    policyTerm, prop.int2);
//            additionToFundIfAny_AK = Double.parseDouble(BIMAST
//                    .getAdditionToFundIfAnyReductionYield_AK());
            // _additionToFundIfAny_AK=additionToFundIfAny_AK;
            // System.out.println("33.   AdditionToFundIfAny_AK: "+additionToFundIfAny_AK);

            BIMAST.setFundBeforeFMCReductionYield_AL(fundValueAtEnd_AQ,
                    policyTerm);
//            fundBeforeFMC_AL = Double.parseDouble(BIMAST
//                    .getFundBeforeFMCReductionYield_AL());
            // _fundBeforeFMC_AL=fundBeforeFMC_AL;
            // System.out.println("34.   FundBeforeFMC_AL: "+fundBeforeFMC_AL);

            BIMAST.setFundManagementChargeReductionYield_AM(policyTerm,
                    SaralMahaAnandBean.getPercentToBeInvested_EquityFund(),
                    SaralMahaAnandBean.getPercentToBeInvested_BondFund(),
                    SaralMahaAnandBean.getPercentToBeInvested_BalancedFund(),
                    SaralMahaAnandBean.getPercentToBeInvested_IndexFund());
//            fundManagementCharge_AM = Double.parseDouble(BIMAST
//                    .getFundManagementChargeReductionYield_AM());
            // _fundManagementCharge_AM=fundManagementCharge_AM;
            // System.out.println("35.   FundManagementCharge_AM: "+fundManagementCharge_AM);

            BIMAST.setServiceTaxOnFMCReductionYield_AN(
                    prop.fundManagementCharges, serviceTax,
                    SaralMahaAnandBean.getPercentToBeInvested_EquityFund(),
                    SaralMahaAnandBean.getPercentToBeInvested_BondFund(),
                    SaralMahaAnandBean.getPercentToBeInvested_BalancedFund(),
                    SaralMahaAnandBean.getPercentToBeInvested_IndexFund());
//            serviceTaxOnFMC_AN = Double.parseDouble(BIMAST
//                    .getServiceTaxOnFMCReductionYield_AN());
            // _serviceTaxOnFMC_AN=serviceTaxOnFMC_AN;
            // System.out.println("36.   ServiceTaxOnFMC_AN: "+serviceTaxOnFMC_AN);

            BIMAST.setFundValueAfterFMCandBeforeGAReductionYield_AO(policyTerm);
//            fundValueAfterFMCAndBeforeGA_AO = Double.parseDouble(BIMAST
//                    .getFundValueAfterFMCandBeforeGAReductionYield_AO());
            // _fundValueAfterFMCAndBeforeGA_AO=fundValueAfterFMCAndBeforeGA_AO;
            // System.out.println("37.   FundValueAfterFMCAndBeforeGA_AO: "+fundValueAfterFMCAndBeforeGA_AO);

            BIMAST.setGuaranteedAddition_AP(effectivePremium, policyTerm,
                    prop.asPercentOfFirstYrPremium);
//            guaranteedAddition_AP = Double.parseDouble(BIMAST
//                    .getGuaranteedAddition_AP());
            // _guaranteedAddition_AP=guaranteedAddition_AP;
            // System.out.println("38.   GuaranteedAddition_AP : "+guaranteedAddition_AP);

            BIMAST.setFundValueAtEndReductionYield_AQ();
            fundValueAtEnd_AQ = Double.parseDouble(BIMAST
                    .getFundValueAtEndReductionYield_AQ());
            // _fundValueAtEnd_AQ=fundValueAtEnd_AQ;
            // System.out.println("39.   FundValueAtEnd_AQ : "+fundValueAtEnd_AQ);

            BIMAST.setDeathBenefitReductionYield_AU(policyTerm, sumAssured);
//            deathBenefit_AU = Double.parseDouble(BIMAST
//                    .getDeathBenefitReductionYield_AU());
            // _deathBenefit_AU=deathBenefit_AU;
            // System.out.println("43.   DeathBenefit_AU: "+deathBenefit_AU);

            BIMAST.setTotalServiceTaxReductionYield_AJ(prop.riderCharges, serviceTax);
//            totalServiceTax_AJ = Double.parseDouble(BIMAST
//                    .getTotalServiceTaxReductionYield_AJ());
            // _totalServiceTax_AJ=totalServiceTax_AJ;
            // System.out.println("32.   TotalServiceTax_AJ: "+totalServiceTax_AJ);

            BIMAST.setSurrenderChargesReductionYield_AR(effectivePremium,
                    policyTerm);
//            surrenderCharges_AR = Double.parseDouble(BIMAST
//                    .getSurrenderChargesReductionYield_AR());
            // _surrenderCharges_AR=surrenderCharges_AR;
            // System.out.println("40.   SurrenderCharges_AR : "+surrenderCharges_AR);

            BIMAST.setServiceTaxOnSurrenderChargesReductionYield_AS(
                    serviceTax, prop.surrenderCharges);
//            serviceTaxOnSurrenderCharges_AS = Double.parseDouble(BIMAST
//                    .getServiceTaxOnSurrenderChargesReductionYield_AS());
            // _serviceTaxOnSurrenderCharges_AS=serviceTaxOnSurrenderCharges_AS;
            // System.out.println("41.   ServiceTaxOnSurrenderCharges_AS: "+serviceTaxOnSurrenderCharges_AS);

            BIMAST.setSurrenderValueReductionYield_AT();
//            surrenderValue_AT = Double.parseDouble(BIMAST
//                    .getSurrenderValueReductionYield_AT());
            // _surrenderValue_AT=surrenderValue_AT;

            // System.out.println("42.   SurrenderValue_AT: "+surrenderValue_AT);

            monthNumber++;

            List_BN.add(commonForAllProd.roundUp_Level2(commonForAllProd
                    .getStringWithout_E(reductionYield_BN)));
            List_BO.add(commonForAllProd.roundUp_Level2(commonForAllProd
                    .getStringWithout_E(reductionYield_BO)));
            List_BS.add(commonForAllProd.roundUp_Level2(commonForAllProd
                    .getStringWithout_E(reductionYield_BS)));

        }

        // //System.out.println("***********************************************************************************************************");

        double ans = BIMAST.irr(List_BN, 0.001);
        double ans1 = BIMAST.irr(List_BO, 0.01);
        double ans2 = BIMAST.irr(List_BS, 0.01);

        // System.out.println("irr_BN : "+ans +" & irr_BO : "+ans1 );

        BIMAST.setIRRAnnual_BN(ans);
        irrAnnual_BN = Double.parseDouble(BIMAST.getIRRAnnual_BN());
        // _irrAnnual_BN=irrAnnual_BN;
        // System.out.println("irrAnnual_BN "+irrAnnual_BN);

        BIMAST.setIRRAnnual_BO(ans1);
        irrAnnual_BO = Double.parseDouble(BIMAST.getIRRAnnual_BO());
        // _irrAnnual_BO=irrAnnual_BO;
        // System.out.println("irrAnnual_BO "+irrAnnual_BO);

        BIMAST.setIRRAnnual_BS(ans2);
        irrAnnual_BS = Double.parseDouble(BIMAST.getIRRAnnual_BS());
        // _irrAnnual_BS=irrAnnual_BS;
        // System.out.println("irrAnnual_BS "+irrAnnual_BS);

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

        // System.out.println("netYield4pa "+netYield4pa);

        BIMAST.setnetYieldAt8Percent();
        netYield8pa = Double.parseDouble(BIMAST.getnetYieldAt8Percent());
        // System.out.println("netYield8pa "+netYield8pa);

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

        //
        // //Calculate ADB Rider Premium
        // if(SaralMahaAnandBean.getIsADBchecked())
        // adbRiderPremium = sumOfriderCharges_P*(1+serviceTax);
        //
        // // //System.out.println("adb 1 " +
        // commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(adbRiderPremium)));
        //
        // //First Year Premium
        // String totFirstYrPremium =
        // commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(
        // adbRiderPremium + SaralMahaAnandBean.getEffectivePremium()));
        //
        // // return new
        // String[]{(commonForAllProd.getStringWithout_E(sumAssured)),(commonForAllProd.getStringWithout_E(Double.parseDouble(U)+Double.parseDouble(V))),(commonForAllProd.getStringWithout_E(Double.parseDouble(AJ)+Double.parseDouble(AK))),(String.valueOf(SaralMahaAnandBean.getEffectivePremium())),(commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(adbRiderPremium))),(String.valueOf(totFirstYrPremium))};
        // return new
        // String[]{(commonForAllProd.getStringWithout_E(sumAssured)),commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(fundValueAtEnd_AB)),commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(fundValueAtEnd_AQ)),(String.valueOf(SaralMahaAnandBean.getEffectivePremium())),(commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(adbRiderPremium))),(String.valueOf(totFirstYrPremium))};

    }

    private void getInput(SaralMahaAnandBean saralMahaAnandBean) {

        inputVal = new StringBuilder();
        // From GUI Input
        boolean staffDisc = saralMahaAnandBean.getIsForStaffOrNot();
        boolean isADBRider = saralMahaAnandBean.getIsADBchecked();
        int policyTerm = saralMahaAnandBean.getPolicyTerm_Basic();
        boolean isJKresident = saralMahaAnandBean.isJKResident();

        String LifeAssured_title = spnr_bi_saral_maha_anand_life_assured_title
                .getSelectedItem().toString();
        String LifeAssured_firstName = edt_bi_saral_maha_anand_life_assured_first_name
                .getText().toString();
        String LifeAssured_middleName = edt_bi_saral_maha_anand_life_assured_middle_name
                .getText().toString();
        String LifeAssured_lastName = edt_bi_saral_maha_anand_life_assured_last_name
                .getText().toString();
        String LifeAssured_DOB = btn_bi_saral_maha_anand_life_assured_date
                .getText().toString();
        String LifeAssured_age = edt_bi_saral_maha_anand_life_assured_age
                .getText().toString();
        String LifeAssured_gender = spnr_bi_saral_maha_anand_selGender
                .getSelectedItem().toString();

        String proposer_title = "";
        String proposer_firstName = "";
        String proposer_middleName = "";
        String proposer_lastName = "";
        String proposer_DOB = "";
        String proposer_age = "";
        String proposer_gender = "";

        inputVal.append("<?xml version='1.0' encoding='utf-8' ?><saralMahaAnandBean>");
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
        inputVal.append("<age>").append(edt_bi_saral_maha_anand_life_assured_age.getText().toString()).append("</age>");
        inputVal.append("<isJKResident>").append(isJKresident).append("</isJKResident>");

        double SAMF = saralMahaAnandBean.getSAMF();
        inputVal.append("<SAMF>").append(SAMF).append("</SAMF>");
        int ageAtEntry = saralMahaAnandBean.getAgeAtEntry();

        inputVal.append("<premFreqMode>").append(spnr_bi_saral_maha_anand_premium_frequency.getSelectedItem()
                .toString()).append("</premFreqMode>");

        inputVal.append("<policyTerm>").append(policyTerm).append("</policyTerm>");

        double premiumAmount = saralMahaAnandBean.getPremiumAmount();
        inputVal.append("<premiumAmount>").append(premiumAmount).append("</premiumAmount>");

        inputVal.append("<noOfYrElapsed>").append(edt_saral_maha_anand_noOfYearsElapsedSinceInception.getText()
                .toString()).append("</noOfYrElapsed>");
        inputVal.append("<perInvEquityFund>").append(edt_saral_maha_anand_percent_EquityFund.getText().toString()).append("</perInvEquityFund>");

        inputVal.append("<perInvBalancedFund>").append(edt_saral_maha_anand_percent_BalancedFund.getText()
                .toString()).append("</perInvBalancedFund>");
        inputVal.append("<perInvBondFund>").append(edt_saral_maha_anand_percent_BondFund.getText().toString()).append("</perInvBondFund>");

        inputVal.append("<isADBRider>").append(isADBRider).append("</isADBRider>");
        inputVal.append("<adbTerm>").append(spnr_bi_saral_maha_anand_adb_rider_term.getSelectedItem()
                .toString()).append("</adbTerm>");
        inputVal.append("<adbSA>").append(edt_bi_saral_maha_anand_adb_rider_sum_assured.getText()
                .toString()).append("</adbSA>");

        //Added Tushar Kadam Kerla Applicable Tax 7-Jun-2019
        String str_kerla_discount = "N";
        if (cb_kerladisc.isChecked()) {
            str_kerla_discount = "Y";
        }

        inputVal.append("<KFC>" + str_kerla_discount + "</KFC>");
        //End Added Tushar Kadam Kerla Applicable Tax 7-Jun-2019

        inputVal.append("</saralMahaAnandBean>");

    }

    private void Dialog() {
        final Dialog d = new Dialog(this);
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);
        d.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.BLACK));

        d.setContentView(R.layout.layout_saral_maha_anand_bi_grid);

        TextView tv_proposername = d
                .findViewById(R.id.tv_proposername);
        TextView tv_proposal_number = d
                .findViewById(R.id.tv_proposal_number);
        TextView tv_bi_saral_maha_anand_age_entry = d
                .findViewById(R.id.tv_bi_saral_maha_anand_age_entry);
        TextView tv_bi_saral_maha_anand_maturity_age = d
                .findViewById(R.id.tv_bi_saral_maha_anand_maturity_age);
        TextView tv_bi_saral_maha_anand_life_assured_gender = d
                .findViewById(R.id.tv_bi_saral_maha_anand_life_assured_gender);
        TextView tv_bi_saral_maha_anand_policy_term = d
                .findViewById(R.id.tv_bi_saral_maha_anand_policy_term);
        TextView tv_bi_saral_maha_anand_annualised_premium = d
                .findViewById(R.id.tv_bi_saral_maha_anand_annualised_premium);
        TextView tv_bi_saral_maha_anand_sum_assured_main = d
                .findViewById(R.id.tv_bi_saral_maha_anand_sum_assured_main);
        TextView tv_bi_saral_maha_anand_mode = d
                .findViewById(R.id.tv_bi_saral_maha_anand_mode);
        TextView tv_bi_saral_maha_anand_premium_paying_term = d
                .findViewById(R.id.tv_bi_saral_maha_anand_premium_paying_term);

        TextView tv_saral_maha_anand_equity_fund_allocation = d
                .findViewById(R.id.tv_saral_maha_anand_equity_fund_allocation);

        TextView tv_saral_maha_anand_balanced_fund_allocation = d
                .findViewById(R.id.tv_saral_maha_anand_balanced_fund_allocation);

        TextView tv_saral_maha_anand_bond_fund_allocation = d
                .findViewById(R.id.tv_saral_maha_anand_bond_fund_allocation);

        TextView tv_saral_maha_anand_equity_fund_fmc = d
                .findViewById(R.id.tv_saral_maha_anand_equity_fund_fmc);

        TextView tv_saral_maha_anand_balanced_fund_fmc = d
                .findViewById(R.id.tv_saral_maha_anand_balanced_fund_fmc);

        TextView tv_saral_maha_anand_bond_fund_fmc = d
                .findViewById(R.id.tv_saral_maha_anand_bond_fund_fmc);

        TextView tv_saral_maha_anand_no_of_years_elapsed = d
                .findViewById(R.id.tv_saral_maha_anand_no_of_years_elapsed);
        TextView tv_saral_maha_anand_reduction_yield = d
                .findViewById(R.id.tv_saral_maha_anand_reduction_yield);
        TextView tv_saral_maha_anand_maturity_at = d
                .findViewById(R.id.tv_saral_maha_anand_maturity_at);
        TextView tv_saral_maha_anand_reduction_yeild2 = d
                .findViewById(R.id.tv_saral_maha_anand_reduction_yeild2);
        final TextView edt_proposer_name = d
                .findViewById(R.id.edt_ProposalName);
        final EditText edt_Policyholderplace = d
                .findViewById(R.id.edt_Policyholderplace);

        TextView tv_saral_maha_anand_death_benefit = d
                .findViewById(R.id.tv_saral_maha_anand_death_benefit);

        TextView tv_saral_maha_anand_net_yield4 = d
                .findViewById(R.id.tv_saral_maha_anand_net_yield_4);

        TextView tv_saral_maha_anand_net_yield8 = d
                .findViewById(R.id.tv_saral_maha_anand_net_yield_8);

        TextView tv_bi_is_Staff = d
                .findViewById(R.id.tv_bi_is_Staff);

        TextView tv_bi_is_jk = d.findViewById(R.id.tv_bi_is_jk);

        LinearLayout ll_basic_rider = d
                .findViewById(R.id.ll_basic_rider);

        LinearLayout ll_basic_adb_rider = d
                .findViewById(R.id.ll_basic_adb_rider);

        TextView tv_bi_saral_maha_anand_premium_paying_term_adb = d
                .findViewById(R.id.tv_bi_saral_maha_anand_premium_paying_term_adb);
        TextView tv_bi_saral_maha_anand_sum_assured_adb = d
                .findViewById(R.id.tv_bi_saral_maha_anand_sum_assured_adb);
        TextView tv_bi_saral_maha_anand_yearly_premium_adb = d
                .findViewById(R.id.tv_bi_saral_maha_anand_yearly_premium_adb);

        // final TextView tv_premium_type =
        // (TextView)d.findViewById(R.id.tv_premium_type);
        final EditText edt_MarketingOfficalPlace = d
                .findViewById(R.id.edt_MarketingOfficalPlace);

        GridView gv_userinfo = d.findViewById(R.id.gv_userinfo);

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

        /* parivartan chnages */
        imageButtonSaralMahaAnandProposerPhotograph = d
                .findViewById(R.id.imageButtonSaralMahaAnandProposerPhotograph);
        imageButtonSaralMahaAnandProposerPhotograph
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
        if (proposer_Is_Same_As_Life_Assured.equalsIgnoreCase("y")) {
            edt_proposer_name
                    .setText("I, "
                            + name_of_life_assured
                            + ", have understood the term and conditions, product features and applicable charges (if any) before purchase of the contract, after receipt of all information stated above from the insurer.");
            edt_proposer_name_need_analysis
                    .setText("I, "
                            + name_of_life_assured
                            + ", have undergone the Need Analysis after having reviewed the SBI Life Product options, I have opted for SBI LIFE - Saral Maha Anand.");

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
                            + ", have undergone the Need Analysis after having reviewed the SBI Life Product options, I have opted for SBI LIFE - Saral Maha Anand.");

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
            imageButtonSaralMahaAnandProposerPhotograph
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
                            imageButtonSaralMahaAnandProposerPhotograph
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
                String isActive = "0";
                // TODO Auto-generated method stub
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
								.isChecked() == true)*/
                ) && radioButtonTrasactionModeParivartan
                        .isChecked()) || radioButtonTrasactionModeManual
                        .isChecked())) {
                    NeedAnalysisActivity.str_need_analysis = "";

                    // String isActive = "0";
                    String productCode = "UPMAP30";

                    na_cbi_bean = new NA_CBI_bean(QuatationNumber, agentcode,
                            "", userType, "", lifeAssured_Title,
                            lifeAssured_First_Name, lifeAssured_Middle_Name,
                            lifeAssured_Last_Name, planName, commonForAllProd
                            .getRound(sumAssured), commonForAllProd
                            .getRound(premiumAmt), emailId, mobileNo,
                            agentEmail, agentMobile, na_input, na_output,
                            premPayingMode, Integer.parseInt(policyTerm), 0,
                            productCode, getDate(lifeAssured_date_of_birth),
                            "", inputVal.toString(), retVal.toString().replace(
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
                            lifeAssured_Middle_Name, lifeAssured_Last_Name,
                            commonForAllProd.getRound(sumAssured),
                            commonForAllProd.getRound(premiumAmt), agentEmail,
                            agentMobile, na_input, na_output, premPayingMode,
                            Integer.parseInt(policyTerm), 0, productCode,
                            getDate(lifeAssured_date_of_birth), "", "", mode, inputVal
                            .toString(), retVal.toString().replace(
                            bussIll.toString(), "")));

                    CreateSaralMahaAnandBIPdf();

                    NABIObj.serviceHit(BI_SaralMahaAnandActivity.this,
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
                    // dialog("Please Fill All The Detail", true);
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
        output = retVal.toString();

        premiumAmt = prsObj.parseXmlTag(input, "premiumAmount");
        String staffdiscount = prsObj.parseXmlTag(input, "isStaff");
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
                String prem_adb = prsObj.parseXmlTag(output, "adbRiderPrem");

                tv_bi_saral_maha_anand_premium_paying_term_adb.setText(term_adb
                        + " Years");

                tv_bi_saral_maha_anand_sum_assured_adb.setText("Rs. "
                        + obj.getRound(obj.getStringWithout_E(Double
                        .valueOf(sa_adb.equals("") ? "0" : sa_adb))));

                tv_bi_saral_maha_anand_yearly_premium_adb
                        .setText("Rs. "
                                + obj.getRound(obj.getStringWithout_E(Double
                                .valueOf(prem_adb.equals("") ? "0"
                                        : prem_adb))));
            } else {
                ll_basic_adb_rider.setVisibility(View.GONE);
            }
        }
        age_entry = prsObj.parseXmlTag(input, "age");
        tv_bi_saral_maha_anand_age_entry.setText(age_entry + " Years");

        maturityAge = prsObj.parseXmlTag(output, "maturityAge");
        tv_bi_saral_maha_anand_maturity_age.setText(maturityAge + " Years");

        gender = prsObj.parseXmlTag(input, "gender");
        tv_bi_saral_maha_anand_life_assured_gender.setText(gender);
        TextView tv_bi_saral_maha_anand_premium_type = d
                .findViewById(R.id.tv_bi_saral_maha_anand_premium_type);

        netYield4pa = prsObj.parseXmlTag(output, "netYield4Pa");
        tv_saral_maha_anand_net_yield4.setText(netYield4pa + " %");

        netYield8pa = prsObj.parseXmlTag(output, "netYield8Pa");
        tv_saral_maha_anand_net_yield8.setText(netYield8pa + " %");

        annPrem = prsObj.parseXmlTag(output, "AnnPrem");

        tv_bi_saral_maha_anand_annualised_premium.setText("Rs. "
                + obj.getRound(obj.getStringWithout_E(Double.valueOf((annPrem
                .equals("") || annPrem == null) ? "0" : annPrem))));

        sumAssured = prsObj.parseXmlTag(output, "sumAssured");


        tv_saral_maha_anand_death_benefit.setText("9) This policy provides guaranteed death benefit of  Rs. "
                + obj.getRound(obj.getStringWithout_E(Double.valueOf(sumAssured
                .equals("") ? "0" : sumAssured))) + ".");

        tv_bi_saral_maha_anand_sum_assured_main.setText("Rs. "
                + getformatedThousandString(Integer.parseInt(obj.getRound(obj
                .getStringWithout_E(Double.valueOf(sumAssured
                        .equals("") ? "0" : sumAssured))))));

        policy_Frequency = prsObj.parseXmlTag(input, "premFreqMode");
        tv_bi_saral_maha_anand_mode.setText(policy_Frequency);

        policyTerm = prsObj.parseXmlTag(input, "policyTerm");
        tv_saral_maha_anand_maturity_at.setText(policyTerm + " Years");
        tv_bi_saral_maha_anand_policy_term.setText(policyTerm + " Years");

        perInvEquityFund = (prsObj.parseXmlTag(input, "perInvEquityFund"));
        perInvBalancedFund = (prsObj.parseXmlTag(input, "perInvBalancedFund"));
        perInvBondFund = (prsObj.parseXmlTag(input, "perInvBondFund"));

        tv_saral_maha_anand_equity_fund_allocation.setText((perInvEquityFund
                .equals("") ? "0" : perInvEquityFund) + " %");

        tv_saral_maha_anand_balanced_fund_allocation
                .setText((perInvBalancedFund.equals("") ? "0"
                        : perInvBalancedFund) + " %");

        tv_saral_maha_anand_bond_fund_allocation.setText((perInvBondFund
                .equals("") ? "0" : perInvBondFund) + " %");

        noOfYrElapsed = prsObj.parseXmlTag(input, "noOfYrElapsed");
        tv_saral_maha_anand_no_of_years_elapsed.setText(noOfYrElapsed
                + " Years");

        String str_prem_pay = "";

        if (policy_Frequency.equalsIgnoreCase("Single")) {
            str_prem_pay = "Single";
        } else {
            str_prem_pay = "Regular/Limited";
        }

        TextView tv_Company_policy_surrender_dec = d
                .findViewById(R.id.tv_Company_policy_surrender_dec);

        Company_policy_surrender_dec = "Your SBI LIFE - Saral Maha Anand  (UIN No: 111L070V02) is a "
                + str_prem_pay
                + " premium policy, for which your first year "
                + policy_Frequency
                + " Premium is Rs. "
                + getformatedThousandString(Integer.parseInt(obj.getRound(obj.getStringWithout_E(Double.valueOf((premiumAmt
                .equals("") || premiumAmt == null) ? "0" : premiumAmt)))))
                + " .Your Policy Term is "
                + policyTerm
                + " years"
                + " , Premium Payment Term is "
                + policyTerm
                + " years"
                + " and Basic Sum Assured is Rs. "
                +

                getformatedThousandString(Integer.parseInt(obj.getRound(obj
                        .getStringWithout_E(Double.valueOf(sumAssured
                                .equals("") ? "0" : sumAssured)))));

        tv_Company_policy_surrender_dec.setText(Company_policy_surrender_dec);

        tv_saral_maha_anand_equity_fund_fmc.setText("1.35 %");
        tv_saral_maha_anand_balanced_fund_fmc.setText("1.25 %");
        tv_saral_maha_anand_bond_fund_fmc.setText("1.00 %");

        redInYieldNoYr = prsObj.parseXmlTag(output, "redInYieldNoYr");
        tv_saral_maha_anand_reduction_yield.setText(redInYieldNoYr + " %");

        redInYieldMat = prsObj.parseXmlTag(output, "redInYieldMat");
        tv_saral_maha_anand_reduction_yeild2.setText(redInYieldMat + " %");

        for (int i = 1; i <= Integer.parseInt(policyTerm); i++) {
            String policy_year = prsObj
                    .parseXmlTag(output, "policyYr" + i + "");
            String premium = prsObj.parseXmlTag(output, "regPrem" + i + "");
            String premium_allocation_charge = prsObj.parseXmlTag(output,
                    "PremAllCharge" + i + "");
            String amount_for_investment = prsObj.parseXmlTag(output,
                    "AmtAvlForInvst" + i + "");
            String policy_administration_charge = prsObj.parseXmlTag(output,
                    "PolicyAdmCharge" + i + "");
            String mortality_charge1 = prsObj.parseXmlTag(output, "MortChrg4Pr"
                    + i + "");
            String total_charge1 = prsObj.parseXmlTag(output, "TotalCharges4Pr"
                    + i + "");
            String service_tax_on_mortality_charge1 = prsObj.parseXmlTag(
                    output, "TotalSerTax4Pr" + i + "");
            String fund_before_fmc1 = prsObj.parseXmlTag(output,
                    "fundaftrFMCbeforeGA4Pr" + i + "");
            String fund_management_charge1 = prsObj.parseXmlTag(output,
                    "FundMgmtChrg4Pr" + i + "");
            String guranteed_addition1 = prsObj.parseXmlTag(output,
                    "GuareentedAdd4Pr" + i + "");
            String fund_value_at_end1 = prsObj.parseXmlTag(output,
                    "FundValAtEnd4Pr" + i + "");

            String riderchrg4pr = prsObj.parseXmlTag(output, "riderchrg4pr" + i
                    + "");
            String riderchrg8pr = prsObj.parseXmlTag(output, "riderchrg8pr" + i
                    + "");

            String addition_to_fund1 = prsObj.parseXmlTag(output,
                    "AddToFundIfAny4Pr" + i + "");

            String addition_to_fund2 = prsObj.parseXmlTag(output,
                    "AddToFundIfAny8Pr" + i + "");

            String surrender_charge1 = prsObj.parseXmlTag(output,
                    "SurrenderChrg4Pr" + i + "");
            String surrender_charge2 = prsObj.parseXmlTag(output,
                    "SurrenderChrg8Pr" + i + "");

            String surrender_value1 = prsObj.parseXmlTag(output,
                    "SurrenderVal4Pr" + i + "");
            String death_benefit1 = prsObj.parseXmlTag(output, "DeathBen4Pr"
                    + i + "");

            String mortality_charge2 = prsObj.parseXmlTag(output, "MortChrg8Pr"
                    + i + "");
            String total_charge2 = prsObj.parseXmlTag(output, "TotalChrg8Pr"
                    + i + "");
            String service_tax_on_mortality_charge2 = prsObj.parseXmlTag(
                    output, "TotalSerTax8Pr" + i + "");
            String fund_before_fmc2 = prsObj.parseXmlTag(output,
                    "fundaftrFMCbeforeGA8Pr" + i + "");
            String fund_management_charge2 = prsObj.parseXmlTag(output,
                    "FundMgmtChrg8Pr" + i + "");
            String guranteed_addition2 = prsObj.parseXmlTag(output,
                    "GuaranteedAdd8Pr" + i + "");
            String fund_value_at_end2 = prsObj.parseXmlTag(output,
                    "FundValAtEnd8Pr" + i + "");
            String surrender_value2 = prsObj.parseXmlTag(output,
                    "SurrenderVal8Pr" + i + "");
            String death_benefit2 = prsObj.parseXmlTag(output, "DeathBen8Pr"
                    + i + "");

            String commission = prsObj.parseXmlTag(output, "Commission" + i
                    + "");

            list_data
                    .add(new M_BI_SaralMahaAnandAdapter(
                            policy_year,
                            obj.getRound(obj.getStringWithout_E(Double
                                    .valueOf((premium.equals("") || premium == null) ? "0"
                                            : premium)))
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
                                    .valueOf((mortality_charge1.equals("") || mortality_charge1 == null) ? "0"
                                            : mortality_charge1)))
                                    + "",
                            obj.getRound(obj.getStringWithout_E(Double
                                    .valueOf((riderchrg4pr.equals("") || riderchrg4pr == null) ? "0"
                                            : riderchrg4pr)))
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
                                    .valueOf((addition_to_fund1.equals("") || addition_to_fund1 == null) ? "0"
                                            : addition_to_fund1)))
                                    + "",

                            obj.getRound(obj.getStringWithout_E(Double.valueOf((fund_management_charge1
                                    .equals("") || fund_management_charge1 == null) ? "0"
                                    : fund_management_charge1)))
                                    + "",
                            obj.getRound(obj.getStringWithout_E(Double
                                    .valueOf((fund_before_fmc1.equals("") || fund_before_fmc1 == null) ? "0"
                                            : fund_before_fmc1)))
                                    + "",
                            obj.getRound(obj.getStringWithout_E(Double
                                    .valueOf((guranteed_addition1.equals("") || guranteed_addition1 == null) ? "0"
                                            : guranteed_addition1)))
                                    + "",
                            obj.getRound(obj.getStringWithout_E(Double
                                    .valueOf((fund_value_at_end1.equals("") || fund_value_at_end1 == null) ? "0"
                                            : fund_value_at_end1)))
                                    + "",
                            obj.getRound(obj.getStringWithout_E(Double
                                    .valueOf((surrender_charge1.equals("") || surrender_charge1 == null) ? "0"
                                            : surrender_charge1)))
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
                                    .valueOf((riderchrg8pr.equals("") || riderchrg8pr == null) ? "0"
                                            : riderchrg8pr)))
                                    + "",
                            obj.getRound(obj.getStringWithout_E(Double
                                    .valueOf((addition_to_fund2.equals("") || addition_to_fund2 == null) ? "0"
                                            : addition_to_fund2)))
                                    + "",
                            obj.getRound(obj.getStringWithout_E(Double.valueOf((fund_management_charge2
                                    .equals("") || fund_management_charge2 == null) ? "0"
                                    : fund_management_charge2)))
                                    + "",
                            obj.getRound(obj.getStringWithout_E(Double
                                    .valueOf((fund_before_fmc2.equals("") || fund_before_fmc2 == null) ? "0"
                                            : fund_before_fmc2)))
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
                                    .valueOf((surrender_charge2.equals("") || surrender_charge2 == null) ? "0"
                                            : surrender_charge2)))
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

        }

        // tv_saral_maha_anand_death_benefit.setText("Rs." + )

        Adapter_BI_SaralMahaAnandGrid adapter = new Adapter_BI_SaralMahaAnandGrid(
                BI_SaralMahaAnandActivity.this, list_data);

        gv_userinfo.setAdapter(adapter);

        GridHeight gh = new GridHeight();
        gh.getheight(gv_userinfo, policyTerm);

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

    private void CreateSaralMahaAnandBIPdf() {

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
            float[] columnWidths2 = {2f, 1f};
            float[] columnWidths4 = {2f, 1f, 2f, 1f};
            // File mypath = new File(folder, PropserNumber +
            // "Proposalno_p02.pdf");
            mypath = mStorageUtils.createFileToAppSpecificDir(context, QuatationNumber + "BI.pdf");
            needAnalysispath = mStorageUtils.createFileToAppSpecificDir(context, "NA.pdf");
            // needAnalysis_Tabular_path = new File(folder, "NA_Tabular.pdf");
            newFile = mStorageUtils.createFileToAppSpecificDir(context, QuatationNumber + "P01.pdf");

            Rectangle rect = new Rectangle(594f, 792f);

            Document document = new Document(rect, 20, 20, 20, 20);
            // Document document = new Document(rect, 50, 50, 50, 50);
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
                            "ULIP Benefit Illustration for SBI LIFE - Saral Maha Anand (UIN No - 111L070V02)",
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
                    "SBI Life Insurance Co. Ltd Corporate Office: 'Natraj', M.V. Road and Western Express, Highway Junction, Andheri (East),Mumbai ? 400069. Regn No. 111",
                    small_bold_for_name);
            para_address.setAlignment(Element.ALIGN_CENTER);
            Paragraph para_address1 = new Paragraph(
                    "Website: www.sbilife.co.in | Email: info@sbilife.co.in | CIN: L99999MH2000PLC129113. Toll Free: 1800 267 9090 (Between 9.00 am & 9.00 pm)",
                    small_bold);
            para_address1.setAlignment(Element.ALIGN_CENTER);
            document.add(para_address);
            document.add(para_address1);
            document.add(para_img_logo_after_space_1);
            document.add(headertable);
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
                            "Insurance Regulatory & Development Authority of India(IRDAI) requires all life insurance companies operating in India to provide official illustrations to their customers. The illustrations are based on the investment rates of return set by the Life Insurance Council (constituted under Section 64C(a) of the Insurance Act 1938) and is not intended to reflect the actual investment returns achieved or which may be achieved in future by SBI life Insurance Company Limited. The two rates of investment return currently declared by the Life Insurance Council are 4% and 8% per annum",
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
                            "Statutory Warning-"
                                    + "Some benefits are guaranteed and some benefits are variable with returns based on the future performance of your Insurer carrying on life insurance business. If your policy offers guaranteed returns then these will be clearly marked ?guaranteed? in the illustration table on this page. If your policy offers variable returns then the illustrations on this page will show two different rates of assumed future investment returns. These assumed rates of return are not guaranteed and they are not the upper or lower limits of what you might get back, as the value of your policy is dependent on a number of factors including future investment performance.?",
                            small_normal));

            BI_Pdftable4_cell1.setPadding(5);

            BI_Pdftable4.addCell(BI_Pdftable4_cell1);
            document.add(BI_Pdftable4);
            document.add(para_img_logo_after_space_1);

            PdfPTable BI_PdftablePlanDetails = new PdfPTable(1);
            BI_PdftablePlanDetails.setWidthPercentage(100);
            PdfPCell BI_PdftablePlanDetails_cell = new PdfPCell(new Paragraph(
                    "Personal Details of Life to be Assured", small_bold));

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
                    "Life Assured Name", small_normal));
            cell_LifeAssuredName1.setPadding(5);
            PdfPCell cell_lLifeAssuredName2 = new PdfPCell(new Paragraph(
                    name_of_proposer, small_bold));
            cell_lLifeAssuredName2.setPadding(5);
            cell_lLifeAssuredName2.setHorizontalAlignment(Element.ALIGN_CENTER);

            PdfPCell cell_lifeAssuredAge1 = new PdfPCell(new Paragraph(
                    "Life Assured  Age", small_normal));
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

            table_lifeAssuredDetails.addCell(cell_lifeAssuredAmaturityGender1);
            table_lifeAssuredDetails.addCell(cell_lifeAssuredAmaturityGender2);
            table_lifeAssuredDetails
                    .addCell(cell_lifeAssured_Premium_frequeny1);
            table_lifeAssuredDetails
                    .addCell(cell_lifeAssured_Premium_frequeny2);
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
            if (cb_bi_saral_maha_anand_JKResident.isChecked()) {
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
            input_table.setWidths(new float[]{5f, 5f});
            input_table.setWidthPercentage(70f);
            input_table.setHorizontalAlignment(Element.ALIGN_CENTER);

            cell = new PdfPCell(new Phrase("Plan Details : ", small_bold));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            input_table.addCell(cell);

            cell = new PdfPCell(new Phrase(" ", small_bold));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            input_table.addCell(cell);

            // 1st row
            cell = new PdfPCell(new Phrase("Life Assured Age at Entry : ",
                    small_normal));
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
            cell = new PdfPCell(new Phrase("Term of Policy : ", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            input_table.addCell(cell);

            cell = new PdfPCell(new Phrase(policyTerm + " Years", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            input_table.addCell(cell);

            // 5 row
            if (policy_Frequency.equalsIgnoreCase("Single")) {
                cell = new PdfPCell(new Phrase("Single Premium* : ",
                        small_normal));
            } else {
                cell = new PdfPCell(new Phrase("Regular Premium* : ",
                        small_normal));
            }
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

            // fund table here -3

            PdfPTable fund_table = new PdfPTable(3);
            fund_table.setWidths(new float[]{3f, 1.9f, 1.5f});
            fund_table.setWidthPercentage(70f);
            fund_table.setHorizontalAlignment(Element.ALIGN_LEFT);

            // 1st row
            cell = new PdfPCell(new Phrase(
                    "Allocation and FMC to the Funds chosen : ", small_bold));
            cell.setColspan(3);

            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            fund_table.addCell(cell);

            // 2 row
            cell = new PdfPCell(new Phrase("Fund Option", small_normal));

            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            fund_table.addCell(cell);

            cell = new PdfPCell(new Phrase("Amount invested(%)", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            fund_table.addCell(cell);

            cell = new PdfPCell(new Phrase("FMC", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            fund_table.addCell(cell);

            // 4 row
            cell = new PdfPCell(new Phrase(
                    "Equity Fund (SFIN:ULIF001100105EQUITY-FND111)",
                    small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            fund_table.addCell(cell);

            cell = new PdfPCell(
                    new Phrase(perInvEquityFund + "%", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            fund_table.addCell(cell);

            cell = new PdfPCell(new Phrase("1.35%p.a.", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            fund_table.addCell(cell);
            // 3 row
            cell = new PdfPCell(
                    new Phrase("Bond Fund (SFIN:ULIF002100105BONDULPFND111)",
                            small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            fund_table.addCell(cell);

            cell = new PdfPCell(new Phrase(perInvBondFund + "%", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            fund_table.addCell(cell);

            cell = new PdfPCell(new Phrase("1.00%p.a.", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            fund_table.addCell(cell);
            // 4 row
            cell = new PdfPCell(new Phrase(
                    "Balanced Fund (SFIN: ULIF004051205BALANCDFND111)",
                    small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            fund_table.addCell(cell);

            cell = new PdfPCell(new Phrase(perInvBalancedFund + "%",
                    small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            fund_table.addCell(cell);

            cell = new PdfPCell(new Phrase("1.25%p.a.", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            fund_table.addCell(cell);

            // first year premium Table here -2

            PdfPTable FyPrem_table = new PdfPTable(2);
            FyPrem_table.setWidths(new float[]{3.2f, 2.5f});
            FyPrem_table.setWidthPercentage(80f);
            FyPrem_table.setHorizontalAlignment(Element.ALIGN_CENTER);

            if (adb_rider_status.equalsIgnoreCase("true")) {

                // 1st row
                cell = new PdfPCell(new Phrase("Rider# ", small_bold));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                FyPrem_table.addCell(cell);

                cell = new PdfPCell(new Phrase("", small_bold));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                FyPrem_table.addCell(cell);

                // 2nd row
                cell = new PdfPCell(new Phrase("SBI Life - ADB Linked Rider: ",
                        small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                FyPrem_table.addCell(cell);
                cell = new PdfPCell(new Phrase("Rs. "
                        + currencyFormat.format(Double.parseDouble(sa_adb)),
                        small_normal));
                // cell = new PdfPCell(new
                // Phrase("Rs. "+currencyFormat.format(Double.parseDouble(totFYPrem)),small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                FyPrem_table.addCell(cell);

            }

            // reduction in yeild for elapsed year since inception Table here
            // -4a

            PdfPTable reductionInYeild_table = new PdfPTable(2);
            reductionInYeild_table.setWidths(new float[]{3.5f, 2f});
            reductionInYeild_table.setWidthPercentage(70f);
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
            // cell = new PdfPCell(new Phrase(redInYieldNoYr+"%",small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            reductionInYeild_table.addCell(cell);

            // 1st row
            cell = new PdfPCell(new Phrase("Maturity at", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            reductionInYeild_table.addCell(cell);
            cell = new PdfPCell(new Phrase("Reduction in Yeild @ 8%",
                    small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            reductionInYeild_table.addCell(cell);

            // 2 row
            cell = new PdfPCell(new Phrase("" + policyTerm + " Years",
                    small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            reductionInYeild_table.addCell(cell);
            cell = new PdfPCell(new Phrase(redInYieldMat + "%", small_normal));
            // cell = new PdfPCell(new Phrase(redInYieldMat+"%",small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            reductionInYeild_table.addCell(cell);

            PdfPTable main_table;

            // main table of 4 tables
            if (adb_rider_status.equalsIgnoreCase("true")) {

                main_table = new PdfPTable(4);
                main_table.setWidths(new float[]{4f, 2.8f, 5f, 3.4f});
                main_table.setWidthPercentage(100);
                main_table.getDefaultCell().setPadding(20f);

                cell = new PdfPCell(input_table);
                // cell.setRowspan(2);
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setVerticalAlignment(Element.ALIGN_TOP);
                main_table.addCell(cell);

                cell = new PdfPCell(FyPrem_table);
                // cell.setRowspan(2);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setBorder(Rectangle.NO_BORDER);
                main_table.addCell(cell);

                cell = new PdfPCell(fund_table);
                // cell.setRowspan(2);
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);

                cell.setVerticalAlignment(Element.ALIGN_TOP);
                main_table.addCell(cell);

                cell = new PdfPCell(reductionInYeild_table);
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                main_table.addCell(cell);

                // cell = new PdfPCell(reductionInYeild_maturity_table);
                // cell.setBorder(Rectangle.NO_BORDER);
                // cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                // cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                // main_table.addCell(cell);
                //
                // cell = new PdfPCell(new Phrase("\n"));
                // cell.setBorder(Rectangle.NO_BORDER);
                // cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                // main_table.addCell(cell);
                //
                // cell = new PdfPCell(new Phrase("\n"));
                // cell.setBorder(Rectangle.NO_BORDER);
                // cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                // main_table.addCell(cell);
                // cell = new PdfPCell(new Phrase("\n"));
                // cell.setBorder(Rectangle.NO_BORDER);
                // cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                // main_table.addCell(cell);

            } else {
                main_table = new PdfPTable(3);
                main_table.setWidths(new float[]{4f, 6f, 3f});
                main_table.setWidthPercentage(100);
                main_table.getDefaultCell().setPadding(20f);

                cell = new PdfPCell(input_table);
                cell.setRowspan(4);
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setVerticalAlignment(Element.ALIGN_TOP);
                main_table.addCell(cell);

                cell = new PdfPCell(fund_table);
                cell.setRowspan(3);
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setVerticalAlignment(Element.ALIGN_TOP);
                main_table.addCell(cell);
                //
                // cell = new PdfPCell(new Phrase("\n"));
                // cell.setBorder(Rectangle.NO_BORDER);
                // cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                // main_table.addCell(cell);
                //

                cell = new PdfPCell(reductionInYeild_table);
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                main_table.addCell(cell);

                // cell = new PdfPCell(reductionInYeild_maturity_table);
                // cell.setBorder(Rectangle.NO_BORDER);
                // cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                // cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                // main_table.addCell(cell);
                //
                // cell = new PdfPCell(new Phrase("\n"));
                // cell.setBorder(Rectangle.NO_BORDER);
                // cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                // main_table.addCell(cell);
                // cell = new PdfPCell(new Phrase("\n"));
                // cell.setBorder(Rectangle.NO_BORDER);
                // cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                // main_table.addCell(cell);
            }

            // Table here

            PdfPTable table1 = new PdfPTable(30);
            table1.setWidths(new float[]{1.5f, 3f, 2.5f, 3f, 2.1f, 2.3f,
                    2.3f, 3f, 3f, 3f, 3f, 3f, 3f, 3f, 3f, 3f, 3f, 3f, 3f, 3f,
                    3f, 3f, 3f, 3f, 3f, 3f, 3f, 3f, 3f, 2.5f});
            table1.setWidthPercentage(100);

            // 1st row
            cell = new PdfPCell(new Phrase(""));
            cell.setColspan(5);
            // cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);
            cell = new PdfPCell(new Phrase(
                    "Assuming gross interest rate of 4% pa", small_bold));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setColspan(12);
            table1.addCell(cell);
            cell = new PdfPCell(new Phrase(
                    "Assuming gross interest rate of 8% pa", small_bold));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setColspan(12);
            table1.addCell(cell);
            cell = new PdfPCell(new Phrase(""));
            // cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);

            // 2nd row
            cell = new PdfPCell(new Phrase("Policy Year", small_bold));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);
            cell = new PdfPCell(new Phrase("Regular Premium", small_bold));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);
            cell = new PdfPCell(new Phrase("Premium Allocation Charge",
                    small_bold));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);
            cell = new PdfPCell(new Phrase(
                    "Amount available for investment (out of premium)",
                    small_bold));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);
            cell = new PdfPCell(new Phrase("Policy Administration Charge",
                    small_bold));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);
            cell = new PdfPCell(new Phrase("Mortality Charges", small_bold));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);
            cell = new PdfPCell(new Phrase("Rider charges", small_bold));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);
            cell = new PdfPCell(new Phrase("Total Charges", small_bold));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);
            cell = new PdfPCell(new Phrase("Total Applicable taxes (if any) on Charges", small_bold));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);
            cell = new PdfPCell(new Phrase("Addition to fund (if any)",
                    small_bold));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);
            cell = new PdfPCell(
                    new Phrase("Fund Management Charge", small_bold));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);
            cell = new PdfPCell(new Phrase("Fund Value after FMC & before GA",
                    small_bold));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);
            cell = new PdfPCell(new Phrase("Guaranteed Addition", small_bold));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);
            cell = new PdfPCell(new Phrase("Fund value at end", small_bold));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);
            cell = new PdfPCell(new Phrase("Surrender Charges", small_bold));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);
            cell = new PdfPCell(new Phrase("Surrender Value", small_bold));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);
            cell = new PdfPCell(new Phrase("Death Benefit", small_bold));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);

            cell = new PdfPCell(new Phrase("Mortality Charges", small_bold));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);
            cell = new PdfPCell(new Phrase("Rider charges", small_bold));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);
            cell = new PdfPCell(new Phrase("Total Charges", small_bold));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);
            cell = new PdfPCell(new Phrase("Total Applicable taxes (if any) on Charges", small_bold));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);
            cell = new PdfPCell(new Phrase("Addition to fund (if any)",
                    small_bold));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);
            cell = new PdfPCell(
                    new Phrase("Fund Management Charge", small_bold));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);
            cell = new PdfPCell(new Phrase("Fund Value after FMC & before GA",
                    small_bold));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);
            cell = new PdfPCell(new Phrase("Guaranteed Addition", small_bold));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);
            cell = new PdfPCell(new Phrase("Fund value at end", small_bold));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);
            cell = new PdfPCell(new Phrase("Surrender Charges", small_bold));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);
            cell = new PdfPCell(new Phrase("Surrender Value", small_bold));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);
            cell = new PdfPCell(new Phrase("Death Benefit", small_bold));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);
            cell = new PdfPCell(new Phrase("Commission/ Brokerage if payable",
                    small_bold));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);

            for (int i = 0; i < 30; i++) {
                cell = new PdfPCell(new Phrase("" + (i + 1), small_normal));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                        | Rectangle.BOTTOM);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table1.addCell(cell);
            }
            for (int i = 0; i < Integer.parseInt(policyTerm); i++) {
                cell = new PdfPCell(new Phrase(prsObj.parseXmlTag(output,
                        "policyYr" + (i + 1)), small_normal));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                        | Rectangle.BOTTOM);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table1.addCell(cell);

                cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                        .parseDouble(prsObj.parseXmlTag(output, "regPrem"
                                + (i + 1)))), small_normal));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                        | Rectangle.BOTTOM);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table1.addCell(cell);
                cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                        .parseDouble(prsObj.parseXmlTag(output, "PremAllCharge"
                                + (i + 1)))), small_normal));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                        | Rectangle.BOTTOM);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table1.addCell(cell);
                cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                        .parseDouble(prsObj.parseXmlTag(output,
                                "AmtAvlForInvst" + (i + 1)))), small_normal));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                        | Rectangle.BOTTOM);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table1.addCell(cell);
                cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                        .parseDouble(prsObj.parseXmlTag(output,
                                "PolicyAdmCharge" + (i + 1)))), small_normal));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                        | Rectangle.BOTTOM);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table1.addCell(cell);

                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                        .parseDouble(prsObj.parseXmlTag(output, "MortChrg4Pr"
                                + (i + 1)))), small_normal));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                        | Rectangle.BOTTOM);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table1.addCell(cell);

                cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                        .parseDouble(prsObj.parseXmlTag(output, "riderchrg4pr"
                                + (i + 1)))), small_normal));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                        | Rectangle.BOTTOM);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table1.addCell(cell);
                cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                        .parseDouble(prsObj.parseXmlTag(output,
                                "TotalCharges4Pr" + (i + 1)))), small_normal));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                        | Rectangle.BOTTOM);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table1.addCell(cell);
                cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                        .parseDouble(prsObj.parseXmlTag(output,
                                "TotalSerTax4Pr" + (i + 1)))), small_normal));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                        | Rectangle.BOTTOM);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table1.addCell(cell);

                cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                        .parseDouble(prsObj.parseXmlTag(output,
                                "AddToFundIfAny4Pr" + (i + 1)))), small_normal));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                        | Rectangle.BOTTOM);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table1.addCell(cell);
                cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                        .parseDouble(prsObj.parseXmlTag(output,
                                "FundMgmtChrg4Pr" + (i + 1)))), small_normal));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                        | Rectangle.BOTTOM);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table1.addCell(cell);
                cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                        .parseDouble(prsObj.parseXmlTag(output,
                                "fundaftrFMCbeforeGA4Pr" + (i + 1)))),
                        small_normal));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                        | Rectangle.BOTTOM);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table1.addCell(cell);
                cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                        .parseDouble(prsObj.parseXmlTag(output,
                                "GuareentedAdd4Pr" + (i + 1)))), small_normal));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                        | Rectangle.BOTTOM);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table1.addCell(cell);
                cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                        .parseDouble(prsObj.parseXmlTag(output,
                                "FundValAtEnd4Pr" + (i + 1)))), small_normal));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                        | Rectangle.BOTTOM);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table1.addCell(cell);
                cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                        .parseDouble(prsObj.parseXmlTag(output,
                                "SurrenderChrg4Pr" + (i + 1)))), small_normal));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                        | Rectangle.BOTTOM);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table1.addCell(cell);
                cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                        .parseDouble(prsObj.parseXmlTag(output,
                                "SurrenderVal4Pr" + (i + 1)))), small_normal));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                        | Rectangle.BOTTOM);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table1.addCell(cell);
                cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                        .parseDouble(prsObj.parseXmlTag(output, "DeathBen4Pr"
                                + (i + 1)))), small_normal));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                        | Rectangle.BOTTOM);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table1.addCell(cell);
                cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                        .parseDouble(prsObj.parseXmlTag(output, "MortChrg8Pr"
                                + (i + 1)))), small_normal));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                        | Rectangle.BOTTOM);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table1.addCell(cell);

                cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                        .parseDouble(prsObj.parseXmlTag(output, "riderchrg8pr"
                                + (i + 1)))), small_normal));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                        | Rectangle.BOTTOM);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table1.addCell(cell);

                cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                        .parseDouble(prsObj.parseXmlTag(output, "TotalChrg8Pr"
                                + (i + 1)))), small_normal));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                        | Rectangle.BOTTOM);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table1.addCell(cell);

                cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                        .parseDouble(prsObj.parseXmlTag(output,
                                "TotalSerTax8Pr" + (i + 1)))), small_normal));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                        | Rectangle.BOTTOM);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table1.addCell(cell);

                cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                        .parseDouble(prsObj.parseXmlTag(output,
                                "AddToFundIfAny8Pr" + (i + 1)))), small_normal));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                        | Rectangle.BOTTOM);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table1.addCell(cell);
                cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                        .parseDouble(prsObj.parseXmlTag(output,
                                "FundMgmtChrg8Pr" + (i + 1)))), small_normal));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                        | Rectangle.BOTTOM);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table1.addCell(cell);
                cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                        .parseDouble(prsObj.parseXmlTag(output,
                                "fundaftrFMCbeforeGA8Pr" + (i + 1)))),
                        small_normal));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                        | Rectangle.BOTTOM);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table1.addCell(cell);
                cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                        .parseDouble(prsObj.parseXmlTag(output,
                                "GuaranteedAdd8Pr" + (i + 1)))), small_normal));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                        | Rectangle.BOTTOM);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table1.addCell(cell);
                cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                        .parseDouble(prsObj.parseXmlTag(output,
                                "FundValAtEnd8Pr" + (i + 1)))), small_normal));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                        | Rectangle.BOTTOM);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table1.addCell(cell);

                cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                        .parseDouble(prsObj.parseXmlTag(output,
                                "SurrenderChrg8Pr" + (i + 1)))), small_normal));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                        | Rectangle.BOTTOM);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table1.addCell(cell);

                cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                        .parseDouble(prsObj.parseXmlTag(output,
                                "SurrenderVal8Pr" + (i + 1)))), small_normal));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                        | Rectangle.BOTTOM);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table1.addCell(cell);

                cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                        .parseDouble(prsObj.parseXmlTag(output, "DeathBen8Pr"
                                + (i + 1)))), small_normal));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                        | Rectangle.BOTTOM);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table1.addCell(cell);
                cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                        .parseDouble(prsObj.parseXmlTag(output, "Commission"
                                + (i + 1)))), small_normal));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                        | Rectangle.BOTTOM);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table1.addCell(cell);

            }
            cell = new PdfPCell(new Phrase(" ", small_normal));
            cell.setColspan(5);
            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT | Rectangle.BOTTOM);
            // cell.setBorderWidthBottom(1.2f);
            table1.addCell(cell);

            cell = new PdfPCell(new Phrase("Net Yeild : ", small_normal));
            cell.setColspan(2);
            cell.setBorder(Rectangle.LEFT | Rectangle.BOTTOM);
            table1.addCell(cell);

            cell = new PdfPCell(new Phrase(netYield4pa + "%", small_normal));
            // cell = new PdfPCell(new Phrase(netYield8Pr+"%",small_normal));
            // cell.setColspan(7);
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table1.addCell(cell);

            cell = new PdfPCell(new Phrase("", small_normal));
            cell.setColspan(9);
            cell.setBorder(Rectangle.RIGHT | Rectangle.BOTTOM);
            table1.addCell(cell);

            cell = new PdfPCell(new Phrase("Net Yeild : ", small_normal));
            cell.setColspan(2);
            cell.setBorder(Rectangle.LEFT | Rectangle.BOTTOM);
            table1.addCell(cell);

            cell = new PdfPCell(new Phrase(netYield8pa + "%", small_normal));
            // cell = new PdfPCell(new Phrase(netYield8Pr+"%",small_normal));
            // cell.setColspan(7);
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table1.addCell(cell);

            cell = new PdfPCell(new Phrase("", small_normal));
            cell.setColspan(9);
            cell.setBorder(Rectangle.RIGHT | Rectangle.BOTTOM);
            table1.addCell(cell);

            cell = new PdfPCell(new Phrase("", small_normal));

            cell.setBorder(Rectangle.RIGHT | Rectangle.BOTTOM);
            table1.addCell(cell);

            Paragraph base_para = new Paragraph(" *It is a base premium ",
                    small_normal);
            base_para.setAlignment(Element.ALIGN_LEFT);

            Paragraph note = new Paragraph("Notes :", small_bold);
            note.setAlignment(Element.ALIGN_LEFT);
            // notes Table here

            PdfPTable table2 = new PdfPTable(2);
            table2.setWidths(new float[]{0.3f, 9f});
            table2.setWidthPercentage(100);
            table2.setHorizontalAlignment(Element.ALIGN_LEFT);

            // 1st note
            cell = new PdfPCell(new Phrase("1)", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table2.addCell(cell);
            cell = new PdfPCell(
                    new Phrase(
                            " Kindly note that above is only an illustration and does not in any way create any rights and/or obligations. The actual experience on the contract may be different from what is illustrated. The non-guaranteed low and high rate mentioned above relate to assumed investment returns at different rates and may vary depending upon market conditions. For more details on risk factors, terms and conditions please read sales brochure carefully.",
                            small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT
                    | Element.ALIGN_JUSTIFIED);
            cell.setBorder(Rectangle.NO_BORDER);
            table2.addCell(cell);

            // 2 note
            cell = new PdfPCell(new Phrase("2)", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table2.addCell(cell);
            cell = new PdfPCell(
                    new Phrase(
                            "The unit values may go up as well as down and past performance is no indication of future performance on the part of SBI Life Insurance Co. Ltd. We would request you to appreciate the associated risk under this plan vis-?-vis the likely future returns before taking your investment decision.",
                            small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            table2.addCell(cell);

            // 3 note
            cell = new PdfPCell(new Phrase("3)", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table2.addCell(cell);
            cell = new PdfPCell(
                    new Phrase(
                            "The interest rates of 4% and 8% are gross rates i.e. taken before the deduction of Fund Management Charges (FMC).",
                            small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            table2.addCell(cell);
            // 4 note
            cell = new PdfPCell(new Phrase("4)", small_normal));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table2.addCell(cell);
            cell = new PdfPCell(
                    new Phrase(
                            "Please read this benefit illustration in conjunction with Sales Brochure and the Policy Document to understand all Terms, Conditions & Exclusions carefully.",
                            small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            table2.addCell(cell);
            // 5 note
            cell = new PdfPCell(new Phrase("5)", small_normal));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table2.addCell(cell);
            cell = new PdfPCell(
                    new Phrase(
                            "It is assumed that the policy is in force throughout the term.",
                            small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            table2.addCell(cell);
            // 6 note
            cell = new PdfPCell(new Phrase("6)", small_normal));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table2.addCell(cell);
            cell = new PdfPCell(
                    new Phrase(
                            " Surrender Value equals the  Fund Value at the end of the year minus Discontinuance Charges as mentioned in the Brochure. Surrender value is available on or after 5th policy anniversary.",
                            small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            table2.addCell(cell);

            // 7 note
            cell = new PdfPCell(new Phrase("7)", small_normal));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table2.addCell(cell);
            cell = new PdfPCell(
                    new Phrase(
                            "Acceptance of proposal is subject to Underwriting decision. Mortality charges/Rider charges if any, are for a healthy person.",
                            small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            table2.addCell(cell);

            // 8 note
            cell = new PdfPCell(new Phrase("8)", small_normal));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table2.addCell(cell);
            cell = new PdfPCell(
                    new Phrase(
                            "Applicable Taxes (including surcharge/cess etc), at the rate notified by the Central Government/ State Government / Union Territories of India from time to time and as per the provisions of the prevalent tax laws will be payable on premium/ or any other charges as per the product features. ",
                            small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT
                    | Element.ALIGN_JUSTIFIED);
            cell.setBorder(Rectangle.NO_BORDER);
            table2.addCell(cell);

            // 9 note
            cell = new PdfPCell(new Phrase("9)", small_normal));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table2.addCell(cell);

            cell = new PdfPCell(new Phrase(
                    "This policy provides guaranteed death benefit of Rs. " +
                            getformatedThousandString(Integer.parseInt(obj.getRound(obj
                                    .getStringWithout_E(Double.valueOf(sumAssured
                                            .equals("") ? "0" : sumAssured))))),
                    small_normal));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table2.addCell(cell);

            // 10 note
            cell = new PdfPCell(new Phrase("10)", small_normal));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table2.addCell(cell);
            cell = new PdfPCell(
                    new Phrase(
                            " Rider charges are not taken into consideration for Net Yield calculation.",
                            small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            table2.addCell(cell);

            // 11 note
            cell = new PdfPCell(new Phrase("11)", small_normal));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table2.addCell(cell);
            cell = new PdfPCell(
                    new Phrase(
                            "Net Yields have been calculated after applying all the charges (except Applicable taxes , education cess, mortality charge and rider charges, if any).",
                            small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            table2.addCell(cell);

            // 12 note
            cell = new PdfPCell(new Phrase("12)", small_normal));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table2.addCell(cell);
            cell = new PdfPCell(
                    new Phrase(
                            " This illustration has been prepared in compliance with the IRDAI (Linked Products) Regulations, 2013.",
                            small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            table2.addCell(cell);

            // 13 note
            cell = new PdfPCell(new Phrase("13)", small_normal));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table2.addCell(cell);
            cell = new PdfPCell(
                    new Phrase(
                            "Col (27) gives the commission payable to the agent/ broker in respect of the base policy .  This amount is included in total charges mentioned in col (8) or col (20).",
                            small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            table2.addCell(cell);

            PdfPTable table3 = new PdfPTable(3);
            table3.setWidths(new float[]{0.5f, 3f, 9f});
            table3.setWidthPercentage(80);
            table3.setHorizontalAlignment(Element.ALIGN_LEFT);

            cell = new PdfPCell(new Phrase("Definition of Various Charges:",
                    small_bold));
            cell.setColspan(3);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            table3.addCell(cell);
            cell = new PdfPCell(new Phrase("", small_normal));
            cell.setRowspan(5);
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table3.addCell(cell);

            // charges row
            cell = new PdfPCell(new Phrase("Accident Benefit charges",
                    small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table3.addCell(cell);
            cell = new PdfPCell(
                    new Phrase(
                            "a charge of a fixed sum based on the sum assured chosen, which is applied at the beginning of each policy month by cancelling units for equivalent amount.",
                            small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table3.addCell(cell);

            // 1st charges row
            cell = new PdfPCell(new Phrase("Premium allocation charge",
                    small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table3.addCell(cell);
            cell = new PdfPCell(
                    new Phrase(
                            "is the percentage of premium that would not be utilised to purchase units.",
                            small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table3.addCell(cell);

            // 2 charges row
            cell = new PdfPCell(new Phrase("Policy administration charges",
                    small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table3.addCell(cell);
            cell = new PdfPCell(
                    new Phrase(
                            "a charge of a fixed sum which is applied at the beginning of each policy month by canceling units for equivalent amount.",
                            small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table3.addCell(cell);

            // 3 charges row
            cell = new PdfPCell(new Phrase("Mortality charges", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table3.addCell(cell);
            cell = new PdfPCell(
                    new Phrase(
                            "are the charges recovered for providing life insurance cover.",
                            small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table3.addCell(cell);

            // 4 charges row
            cell = new PdfPCell(new Phrase("Fund management charge",
                    small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table3.addCell(cell);
            cell = new PdfPCell(
                    new Phrase(
                            "is the deduction made from the fund at a stated percentage before the computation of the NAV of the fund.",
                            small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table3.addCell(cell);

            Paragraph new_line = new Paragraph("\n");
            // document.add(table);
            //
            // // document.add(ls);
            // document.add(para3);
            // document.add(para4);
            // document.add(para5);
            // document.add(para6);
            // document.add(input_table);

            document.add(new_line);

            document.add(main_table);
            document.add(new_line);
            document.add(table1);
            document.add(base_para);
            document.add(new_line);
            document.add(note);
            document.add(table2);
            // document.add(new_line);
            // document.add(ls);
            document.add(new_line);
            document.add(table3);
            document.add(new_line);

            // document.add(para7);
            // document.add(new_line);
            // document.add(new_line);
            // document.add(table4);
            //Start 10 Jan 18
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
            document.add(BI_Pdftable_CompanysPolicySurrender5);

            //End 10 Jan 18

            document.add(para_img_logo_after_space_1);

            // ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

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
                                    + "     have understood the term and conditions, product features and applicable charges (if any) before purchase of the contract, after receipt of all information stated above from the insurer.",
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
            }

            PdfPTable BI_PdftableMarketing_signature = null;

            BI_PdftableMarketing_signature = new PdfPTable(3);

            BI_PdftableMarketing_signature.setWidthPercentage(100);

            PdfPCell BI_PdftableMarketing_signature_1 = new PdfPCell(
                    new Paragraph("Place :" + place2, small_normal));
            PdfPCell BI_PdftableMarketing_signature_2 = new PdfPCell(
                    new Paragraph("Date  :" + CurrentDate, small_normal));

            PdfPCell BI_PdftableMarketing_signature_3 = new PdfPCell();
            if (!bankUserType.equalsIgnoreCase("Y")) {
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
            }

            BI_PdftableMarketing_signature_1.setPadding(5);
            BI_PdftableMarketing_signature_2.setPadding(5);

            BI_PdftableMarketing_signature
                    .addCell(BI_PdftableMarketing_signature_1);
            BI_PdftableMarketing_signature
                    .addCell(BI_PdftableMarketing_signature_2);

            if (!bankUserType.equalsIgnoreCase("Y")) {
                BI_PdftableMarketing_signature
                        .addCell(BI_PdftableMarketing_signature_3);
            }

            document.add(BI_PdftableMarketing_signature);

            document.close();

        } catch (Exception e) {
            Log.i(getLocalClassName(), e.toString() + "Error in creating pdf");
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

                            btn_bi_saral_maha_anand_life_assured_date.setText(date);
                            edt_bi_saral_maha_anand_life_assured_age
                                    .setText(final_age);
                            // ageInYears
                            // .setSelection(getIndex(ageInYears, final_age));
                            // valAge();
                        } else {
                            commonMethods.BICommonDialog(context, "Minimum age should be 18 yrs for proposer");
                            btn_bi_saral_maha_anand_life_assured_date
                                    .setText("Select Date");
                            edt_bi_saral_maha_anand_life_assured_age.setText("");
                        }
                    }
                    break;

                case 5:

                    final_age = Integer.toString(age);
                    if (final_age.contains("-")) {
                        commonMethods.BICommonDialog(context, "Please fill Valid Birth Date");
                    } else {
                        int maxLimit;
                        maxLimit = 55;

                        if (18 <= age && age <= maxLimit) {
                            btn_bi_saral_maha_anand_life_assured_date.setText(date);
                            edt_bi_saral_maha_anand_life_assured_age
                                    .setText(final_age);

                            edt_bi_saral_maha_anand_life_assured_age
                                    .setText(final_age);
                            lifeAssured_date_of_birth = getDate1(date + "");
                            clearFocusable(btn_bi_saral_maha_anand_life_assured_date);
                            setFocusable(edt_saral_maha_anand_contact_no);
                            edt_saral_maha_anand_contact_no.requestFocus();

                        } else {
                            commonMethods.BICommonDialog(context, "Minimum Age should be 18 yrs and Maximum Age should be "
                                    + maxLimit + " yrs For LifeAssured");
                            btn_bi_saral_maha_anand_life_assured_date
                                    .setText("Select Date");
                            edt_bi_saral_maha_anand_life_assured_age.setText("");
                            lifeAssured_date_of_birth = "";

                            clearFocusable(btn_bi_saral_maha_anand_life_assured_date);
                            setFocusable(edt_saral_maha_anand_contact_no);
                            btn_bi_saral_maha_anand_life_assured_date
                                    .requestFocus();
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
                        imageButtonSaralMahaAnandProposerPhotograph
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

        d = new Dialog(BI_SaralMahaAnandActivity.this);
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
                Intent intent = new Intent(BI_SaralMahaAnandActivity.this,
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

        d = new Dialog(BI_SaralMahaAnandActivity.this);
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
                Intent intent = new Intent(BI_SaralMahaAnandActivity.this,
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
        boolean validationFlag3 = false;
        if ((number.length() != 10)) {
            edt_saral_maha_anand_contact_no
                    .setError("Please provide correct 10-digit mobile number");
            validationFlag3 = false;
        } else if ((number.length() == 10)) {
            validationFlag3 = true;
        }
    }

    private void email_id_validation(String email_id) {

        String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email_id);
        if (!(matcher.matches())) {
            edt_saral_maha_anand_Email_id
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

    // Validate maximum allowed (policy term + age) at Entry
    private boolean valPolicyTermPlusAge() {
        if ((Integer.parseInt(edt_bi_saral_maha_anand_life_assured_age
                .getText().toString()) + Integer
                .parseInt(spnr_bi_saral_maha_anand_policyterm.getSelectedItem()
                        .toString())) > 65) {
            showAlert
                    .setMessage("Addition of Policy Term & Age at Entry should be less than or equal to 65 Years");
            showAlert.setNeutralButton("OK",
                    new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            spnr_bi_saral_maha_anand_policyterm.setSelection(0);
                        }
                    });
            showAlert.show();

            return false;
        } else {
            return true;
        }
    }

    // Validate ADB Term
    private boolean valADBterm() {

        int minADBterm = 0, maxADBterm = 0;

        minADBterm = prop.minADBterm;
        maxADBterm = Math.min(Math.min(Integer
                        .parseInt(spnr_bi_saral_maha_anand_policyterm.getSelectedItem()
                                .toString()), (prop.maxAgeAtMaturity - Integer
                        .parseInt(edt_bi_saral_maha_anand_life_assured_age.getText()
                                .toString()))),
                (prop.maxAgeAtMaturity - prop.minAgeADB));

        if (Double.parseDouble(spnr_bi_saral_maha_anand_adb_rider_term
                .getSelectedItem().toString()) < minADBterm
                || Double.parseDouble(spnr_bi_saral_maha_anand_adb_rider_term
                .getSelectedItem().toString()) > maxADBterm) {
            // Show alert message if rider is selected
            if (cb_bi_saral_maha_anand_adb_rider.isChecked()) {
                showAlert
                        .setMessage("Accidental Death Benefit Linked Rider Term should be in the range of "
                                + minADBterm
                                + " Years to "
                                + maxADBterm
                                + " Years");
                showAlert.setNeutralButton("OK",
                        new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog,
                                                int which) {
                                spnr_bi_saral_maha_anand_adb_rider_term
                                        .setSelection(0);
                            }
                        });
                showAlert.show();
                return false;
            }
            // Do not show alert message if rider is not selected.
            // Only reset the term
            else
                spnr_bi_saral_maha_anand_adb_rider_term.setSelection(0);
        }
        return true;
    }

    // Validation of No. of years elapsed since Inception
    private boolean valYearsElapsedSinceInception() {
        if (edt_saral_maha_anand_noOfYearsElapsedSinceInception.getText()
                .toString().equals("")
                || Double
                .parseDouble(edt_saral_maha_anand_noOfYearsElapsedSinceInception
                        .getText().toString()) < 5
                || Double
                .parseDouble(edt_saral_maha_anand_noOfYearsElapsedSinceInception
                        .getText().toString()) > Double
                .parseDouble(spnr_bi_saral_maha_anand_policyterm
                        .getSelectedItem().toString())) {
            showAlert
                    .setMessage("Enter No. of Years Elapsed Since Inception between 5 to "
                            + spnr_bi_saral_maha_anand_policyterm
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

    // Help for SAMF
    private void updateSAMFlabel() {
        try {
            double minSAMF = 0, temp1 = 0, temp2 = 0;
            if (Integer.parseInt(edt_bi_saral_maha_anand_life_assured_age
                    .getText().toString()) < 45) {
                temp1 = 10;
            } else {
                temp1 = 7;
            }
            temp2 = calMinSA() / getEffectivePremium();
            minSAMF = Math.max(temp1, temp2);
            help_SAMF.setText("(" + minSAMF + " to " + prop.maxSAMF + ")");
        } catch (Exception ignored) {
        }
    }

    // Help for ADB Term
    private void updateTermADBlabel() {
        try {
            if (cb_bi_saral_maha_anand_adb_rider.isChecked()) {
                int minADBterm = 0, maxADBterm = 0;
                minADBterm = prop.minADBterm;
                maxADBterm = Math
                        .min(Math.min(
                                Integer.parseInt(spnr_bi_saral_maha_anand_policyterm
                                        .getSelectedItem().toString()),
                                (prop.maxAgeAtMaturity - Integer
                                        .parseInt(edt_bi_saral_maha_anand_life_assured_age
                                                .getText().toString()))),
                                (prop.maxAgeAtMaturity - prop.minAgeADB));
                help_adb_term.setText("(" + minADBterm + " to " + maxADBterm
                        + " years)");
            }
        } catch (Exception e) {
            help_adb_term.setText("");
        }

    }

    private void updatePremiumAmtLabel() {
        try {
            if (spnr_bi_saral_maha_anand_premium_frequency.getSelectedItem()
                    .toString().equals("Yearly")) {
                help_premAmt.setText("(Rs. "
                        + currencyFormat.format(prop.minPremAmtYearly)
                        + " to Rs. "
                        + currencyFormat.format(prop.maxPremAmtYearly) + ")");
            } else if (spnr_bi_saral_maha_anand_premium_frequency
                    .getSelectedItem().toString().equals("Half Yearly")) {
                help_premAmt.setText("(Rs. "
                        + currencyFormat.format(prop.minPremAmtHalfYearly)
                        + " to Rs. "
                        + currencyFormat.format(prop.maxPremAmtHalfYearly)
                        + ")");
            } else if (spnr_bi_saral_maha_anand_premium_frequency
                    .getSelectedItem().toString().equals("Quarterly")) {
                help_premAmt
                        .setText("(Rs. "
                                + currencyFormat
                                .format(prop.minPremAmtQuarterly)
                                + " to Rs. "
                                + currencyFormat
                                .format(prop.maxPremAmtQuarterly) + ")");
            } else if (spnr_bi_saral_maha_anand_premium_frequency
                    .getSelectedItem().toString().equals("Monthly")) {
                help_premAmt.setText("(Rs. "
                        + currencyFormat.format(prop.minPremAmtMonthly)
                        + " to Rs. "
                        + currencyFormat.format(prop.maxPremAmtMonthly) + ")");
            }
        } catch (Exception ignored) {
        }
    }

    private void updateSumAssuredADBlabel() {

        try {
            if (cb_bi_saral_maha_anand_adb_rider.isChecked()) {
                double minADB_SA = 0, maxADB_SA = 0, new_maxADB_SA = 0;
                minADB_SA = Math.max(Math.min(25000,
                        (getEffectivePremium() * Double
                                .parseDouble(edt_saral_maha_anand_samf
                                        .getText().toString()))), 25000);
                maxADB_SA = getEffectivePremium()
                        * Double.parseDouble(edt_saral_maha_anand_samf
                        .getText().toString());
                if ((maxADB_SA % 1000) != 0) {
                    double remainder = maxADB_SA % 1000;
                    new_maxADB_SA = maxADB_SA - remainder;
                } else {
                    new_maxADB_SA = maxADB_SA;
                }
                help_adbsa.setText("(Rs. " + currencyFormat.format(minADB_SA)
                        + " to Rs. " + currencyFormat.format(new_maxADB_SA)
                        + ")");
            }
        } catch (Exception e) {
            help_adbsa.setText("");
        }
    }

    // help for no of yeras elapsed since Inception
    private void updatenoOfYearsElapsedSinceInception() {
        try {
            help_noOfYearsElapsedSinceInception.setText("(5 to "
                    + spnr_bi_saral_maha_anand_policyterm.getSelectedItem()
                    .toString() + " years)");
        } catch (Exception ignored) {
        }
    }

    // Function used in valSAMF() to calculate minimum SAMF
    private double calMinSA() {
        double temp = 0;

        if (Integer.parseInt(edt_bi_saral_maha_anand_life_assured_age.getText()
                .toString()) < 45) {
            temp = 0.5;
        } else {
            temp = 0.25;
        }
        return getEffectivePremium()
                * (Integer.parseInt(spnr_bi_saral_maha_anand_policyterm
                .getSelectedItem().toString())) * temp;
    }

    // Returns Effective Premium
    private double getEffectivePremium() {

        if (spnr_bi_saral_maha_anand_premium_frequency.getSelectedItem()
                .toString().equals("Yearly")) {
            PF = 1;
        } else if (spnr_bi_saral_maha_anand_premium_frequency.getSelectedItem()
                .toString().equals("Half Yearly")) {
            PF = 2;
        } else if (spnr_bi_saral_maha_anand_premium_frequency.getSelectedItem()
                .toString().equals("Quarterly")) {
            PF = 4;
        } else if (spnr_bi_saral_maha_anand_premium_frequency.getSelectedItem()
                .toString().equals("Monthly")) {
            PF = 12;
        }
        return Double.parseDouble(edt_bi_saral_maha_anand_sum_assured_amount
                .getText().toString()) * PF;
    }

    // Validtion
    private boolean valSA() {
        String errorMessage = "";

        if (edt_bi_saral_maha_anand_sum_assured_amount.getText().toString()
                .equals("")) {
            errorMessage = "Please enter Premium Amount in Rs. ";
        } else if (Double
                .parseDouble(edt_bi_saral_maha_anand_sum_assured_amount
                        .getText().toString()) % 100 != 0) {
            errorMessage = "Please Enter Premium Amount in multiples of 1000.";
        } else if ((spnr_bi_saral_maha_anand_premium_frequency
                .getSelectedItem().toString().equals("Yearly"))
                && ((Double
                .parseDouble(edt_bi_saral_maha_anand_sum_assured_amount
                        .getText().toString()) < prop.minPremAmtYearly) || (Double
                .parseDouble(edt_bi_saral_maha_anand_sum_assured_amount
                        .getText().toString()) > prop.maxPremAmtYearly))) {
            errorMessage = "Premium Amount should be in the range of Rs. "
                    + currencyFormat.format(prop.minPremAmtYearly) + " to Rs. "
                    + currencyFormat.format(prop.maxPremAmtYearly)
                    + " for Yearly Mode";
        } else if ((spnr_bi_saral_maha_anand_premium_frequency
                .getSelectedItem().toString().equals("Half Yearly"))
                && ((Double
                .parseDouble(edt_bi_saral_maha_anand_sum_assured_amount
                        .getText().toString()) < prop.minPremAmtHalfYearly) || (Double
                .parseDouble(edt_bi_saral_maha_anand_sum_assured_amount
                        .getText().toString()) > prop.maxPremAmtHalfYearly))) {
            errorMessage = "Premium Amount should be in the range of Rs. "
                    + currencyFormat.format(prop.minPremAmtHalfYearly)
                    + " to Rs. "
                    + currencyFormat.format(prop.maxPremAmtHalfYearly)
                    + " for Half Yearly Mode";
        } else if ((spnr_bi_saral_maha_anand_premium_frequency
                .getSelectedItem().toString().equals("Quarterly"))
                && ((Double
                .parseDouble(edt_bi_saral_maha_anand_sum_assured_amount
                        .getText().toString()) < prop.minPremAmtQuarterly) || (Double
                .parseDouble(edt_bi_saral_maha_anand_sum_assured_amount
                        .getText().toString()) > prop.maxPremAmtQuarterly))) {
            errorMessage = "Premium Amount should be in the range of Rs. "
                    + currencyFormat.format(prop.minPremAmtQuarterly)
                    + " to Rs. "
                    + currencyFormat.format(prop.maxPremAmtQuarterly)
                    + " for Quarterly Mode";
        } else if ((spnr_bi_saral_maha_anand_premium_frequency
                .getSelectedItem().toString().equals("Monthly"))
                && ((Double
                .parseDouble(edt_bi_saral_maha_anand_sum_assured_amount
                        .getText().toString()) < prop.minPremAmtMonthly) || (Double
                .parseDouble(edt_bi_saral_maha_anand_sum_assured_amount
                        .getText().toString()) > prop.maxPremAmtMonthly))) {
            errorMessage = "Premium Amount should be in the range of Rs. "
                    + currencyFormat.format(prop.minPremAmtMonthly) + " to Rs. "
                    + currencyFormat.format(prop.maxPremAmtMonthly)
                    + " for Monthly Mode";
        }

        if (!errorMessage.equals("")) {
            showAlert.setMessage(errorMessage);
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

    // Validate SAMF
    private boolean valSAMF() {
        double minSAMF = 0, temp1 = 0, temp2 = 0;
        if (Integer.parseInt(edt_bi_saral_maha_anand_life_assured_age.getText()
                .toString()) < 45) {
            temp1 = 10;
        } else {
            temp1 = 7;
        }
        temp2 = calMinSA() / getEffectivePremium();
        minSAMF = Math.max(temp1, temp2);

        if (edt_saral_maha_anand_samf.getText().toString().equals("")
                || Double.parseDouble(edt_saral_maha_anand_samf.getText()
                .toString()) < minSAMF
                || Double.parseDouble(edt_saral_maha_anand_samf.getText()
                .toString()) > prop.maxSAMF) {
            showAlert
                    .setMessage("Sum Assured Multiple Factor (SAMF) should be in the range of "
                            + minSAMF + " to " + prop.maxSAMF);
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

    // Validate SDB Sum Assured
    private boolean valADBsumAssured() {
        if (cb_bi_saral_maha_anand_adb_rider.isChecked()) {
            double minADB_SA = 0, maxADB_SA = 0;
            minADB_SA = Math.max(Math.min(25000,
                    (getEffectivePremium() * Double
                            .parseDouble(edt_saral_maha_anand_samf.getText()
                                    .toString()))), 25000);
            maxADB_SA = getEffectivePremium()
                    * Double.parseDouble(edt_saral_maha_anand_samf.getText()
                    .toString());
            String errorMessage = "";

            if (edt_bi_saral_maha_anand_adb_rider_sum_assured.getText()
                    .toString().equals(""))
                errorMessage = "Please enter Accidental Death Benefit Linked Rider Sum Assured in Rs. ";
            else if (Double
                    .parseDouble(edt_bi_saral_maha_anand_adb_rider_sum_assured
                            .getText().toString()) % 1000 != 0)
                errorMessage = "Accidental Death Benefit Linked Rider Sum Assured in multiples of 1000.";
            else if (Double
                    .parseDouble(edt_bi_saral_maha_anand_adb_rider_sum_assured
                            .getText().toString()) % 1000 != 0
                    || Double
                    .parseDouble(edt_bi_saral_maha_anand_adb_rider_sum_assured
                            .getText().toString()) < minADB_SA
                    || Double
                    .parseDouble(edt_bi_saral_maha_anand_adb_rider_sum_assured
                            .getText().toString()) > maxADB_SA)
                errorMessage = "Accidental Death Benefit Linked Rider Sum Assured should be in the range of Rs. "
                        + currencyFormat.format(minADB_SA)
                        + " to Rs. "
                        + currencyFormat.format(maxADB_SA);

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
    private boolean valTotalAllocation() {
        String error = "";
        double equityFund, bondFund, balancedFund, indexFund;
        if (!edt_saral_maha_anand_percent_EquityFund.getText().toString()
                .equals(""))
            equityFund = Double
                    .parseDouble(edt_saral_maha_anand_percent_EquityFund
                            .getText().toString());
        else
            equityFund = 0;

        if (!edt_saral_maha_anand_percent_BondFund.getText().toString()
                .equals(""))
            bondFund = Double.parseDouble(edt_saral_maha_anand_percent_BondFund
                    .getText().toString());
        else
            bondFund = 0;

        if (!edt_saral_maha_anand_percent_BalancedFund.getText().toString()
                .equals(""))
            balancedFund = Double
                    .parseDouble(edt_saral_maha_anand_percent_BalancedFund
                            .getText().toString());
        else
            balancedFund = 0;

        if ((equityFund + bondFund + balancedFund) != 100)
            error = "Total sum of % to be invested for all fund should be equal to 100%";

        // display alert message
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
                                    setFocusable(spnr_bi_saral_maha_anand_life_assured_title);
                                    spnr_bi_saral_maha_anand_life_assured_title
                                            .requestFocus();
                                } else if (lifeAssured_First_Name.equals("")) {
                                    setFocusable(edt_bi_saral_maha_anand_life_assured_first_name);
                                    edt_bi_saral_maha_anand_life_assured_first_name
                                            .requestFocus();
                                } else {
                                    setFocusable(edt_bi_saral_maha_anand_life_assured_last_name);
                                    edt_bi_saral_maha_anand_life_assured_last_name
                                            .requestFocus();
                                }
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
                                setFocusable(btn_bi_saral_maha_anand_life_assured_date);
                                btn_bi_saral_maha_anand_life_assured_date
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

        if (edt_saral_maha_anand_contact_no.getText().toString().equals("")) {
            commonMethods.dialogWarning(context, "Please Fill  Mobile No", true);
            edt_saral_maha_anand_contact_no.requestFocus();
            return false;
        } else if (edt_saral_maha_anand_contact_no.getText().toString()
                .length() != 10) {
            commonMethods.dialogWarning(context, "Please Fill 10 Digit Mobile No", true);
            edt_saral_maha_anand_contact_no.requestFocus();
            return false;
        } /*else if (emailId.equals("")) {
            commonMethods.dialogWarning(context,"Please Fill Email Id", true);
            edt_saral_maha_anand_Email_id.requestFocus();
            return false;

        } else if (ConfirmEmailId.equals("")) {

            commonMethods.dialogWarning(context,"Please Fill Confirm Email Id", true);
            edt_saral_maha_anand_ConfirmEmail_id.requestFocus();
            return false;
        } else if (!ConfirmEmailId.equals(emailId)) {
            commonMethods.dialogWarning(context,"Email Id Does Not Match", true);
            return false;
        }*/ else if (!emailId.equals("")) {

            email_id_validation(emailId);
            if (validationFla1) {

                if (ConfirmEmailId.equals("")) {

                    commonMethods.dialogWarning(context, "Please Fill Confirm Email Id", true);
                    edt_saral_maha_anand_ConfirmEmail_id.requestFocus();
                    return false;
                } else if (!ConfirmEmailId.equals(emailId)) {
                    commonMethods.dialogWarning(context, "Email Id Does Not Match", true);
                    return false;
                }

                return true;
            } else {
                commonMethods.dialogWarning(context, "Please Fill Valid Email Id", true);
                edt_saral_maha_anand_Email_id.requestFocus();
                return false;
            }
        } else if (!ConfirmEmailId.equals("")) {

            email_id_validation(ConfirmEmailId);
            if (validationFla1) {

                if (emailId.equals("")) {
                    commonMethods.dialogWarning(context, "Please Fill Email Id", true);
                    edt_saral_maha_anand_Email_id.requestFocus();
                    return false;

                }

                return true;
            } else {
                commonMethods.dialogWarning(context, "Please Fill Valid Confirm Email Id", true);
                edt_saral_maha_anand_ConfirmEmail_id.requestFocus();
                return false;
            }
        } else {
            return true;
        }
    }

}
