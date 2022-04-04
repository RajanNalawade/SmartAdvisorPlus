package sbilife.com.pointofsale_bancaagency.poornsuraksha;

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

public class BI_PoornSurakshaActivity extends AppCompatActivity implements
        OnEditorActionListener {

    private final int SIGNATURE_ACTIVITY = 1;
    private final int DATE_DIALOG_ID = 1;
    private String mode = "";
    private String proposer_date_of_birth = "";
    private int DIALOG_ID;
    // UI Elements
    private CheckBox selStaffDisc;
    private Button btnSubmit, back;
    private Spinner selBasicTerm, selPremFreq, ageInYears, selGender;
    private EditText basicSA;
    private RadioButton rb_proposerdetail_personaldetail_backdating_yes, rb_proposerdetail_personaldetail_backdating_no, rb_poorn_suraksha_life_assured_smoker_yes, rb_poorn_suraksha_life_assured_smoker_no,
            rb_poorn_suraksha_proposer_same_as_life_assured_yes, rb_poorn_suraksha_proposer_same_as_life_assured_no;
    private LinearLayout ll_backdating1;
    private double PremiumRate = 0;
    private double StaffRebate = 0;
    private double SARebate = 0;
    private double LoadingFrequencyPremium = 0;
    private AlertDialog.Builder showAlert;
    private String proposer_Title = "", proposer_First_Name = "",
            proposer_Middle_Name = "", proposer_Last_Name = "",
            lifeAssuredAge = "", proposer_Is_Same_As_Life_Assured = "Y",
            totalInstPrem_exclST_exclDisc = "";
    private Spinner spnr_bi_poorn_suraksha_proposer_title, spnr_bi_poorn_suraksha_life_assured_title;
    private EditText edt_bi_poorn_suraksha_proposer_first_name, edt_bi_poorn_suraksha_proposer_middle_name,
            edt_bi_poorn_suraksha_proposer_last_name, edt_bi_poorn_suraksha_age;
    private EditText edt_bi_poorn_suraksha_life_assured_first_name, edt_bi_poorn_suraksha_life_assured_middle_name,
            edt_bi_poorn_suraksha_life_assured_last_name;
    /*edt_bi_poorn_suraksha_life_assured_age*/
    private Button btn_bi_poorn_suraksha_life_assured_date, btn_proposerdetail_personaldetail_backdatingdate;
    private String lifeAssured_Title = "", lifeAssured_First_Name = "", lifeAssured_Middle_Name = "",
            lifeAssured_Last_Name = "", name_of_life_assured = "", str_lifeAssured_gender = "",
            lifeAssured_date_of_birth = "";
    private TableRow tr_poorn_suraksha_proposer_detail2, tr_poorn_suraksha_proposer_detail1;
    private String age_entry = "", gender = "", premium_paying_frequency = "",
            policy_term = "", sum_assured = "", poorn_suraksha_data = "";
    // For Bi Dialog
    private ParseXML prsObj;
    private String name_of_proposer = "", name_of_person = "";
    //String place1 = "";
    private String place2 = "", date1 = "", date2 = "", agent_sign = "", proposer_sign = "";
    private Button btn_MarketingOfficalDate, btn_PolicyholderDate;
    private Dialog d;
    private String latestImage = "";
    // for BI
    private StringBuilder inputVal;
    // Database Related Variable
    private DatabaseHelper db;
    private Button btn_bi_poorn_suraksha_proposer_date;

    // To Store User Info and sync info
    private String proposer_Backdating_WishToBackDate_Policy = "";
    private String proposer_isSmoker = "";
    private String proposer_Backdating_BackDate = "";
    private CommonForAllProd obj;
    private ScrollView svPoornSurakshaMain;
    private boolean flagFirstFocus = true;
    private EditText edt_proposerdetail_basicdetail_contact_no, edt_proposerdetail_basicdetail_Email_id,
            edt_proposerdetail_basicdetail_ConfirmEmail_id;
    private String mobileNo = "", emailId = "",
            ConfirmEmailId = "", ProposerEmailId = "";
    private Bitmap photoBitmap;
    private String flg_needAnalyis = "";

    /* Basic Details */
    private StringBuilder date;
    private String QuatationNumber;
    private String planName = "", product_Code = "", product_UIN = "", product_cateogory = "", product_type = "";
    private double TotalFinalPremium_ExclST = 0;
    private String PremiumBeforeST = "";
    private String PremiumBeforeST_CI = "",
            TotalPremiumWithoutST_CI = "",
            TotalPremiumWithST_CI = "",
            ServiceTax_CI = "",
            InstallmentPremLifeCover_ExclSt = "",
            ApplicableTaxesLifeCover = "",
            InstallmentPremLifeCover_InclSt = "",
            TotalFinalPremium_IncST = "",
            totInstPrem_exclST_exclDisc = "",
            totInstPrem_exclST_exclDisc_exclFreqLoading = "",
            riderPrem_exclST_exclDisc = "",
            riderPrem_exclST_exclDisc_exclFreqLoading = "", SumAssured_basic_80 = "",
            MinesOccuInterest = "";
    // For xml Output
    private StringBuilder retVal, bussIll;
    private PoornSurakshaBean poornSurakshaBean;
    private List<M_BI_PoornSurakshaGrid_Adpter> list_data;
    private File needAnalysispath, newFile, mypath;
    private int mYear;
    private int mMonth;
    private int mDay;
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
    private boolean validationFla1 = false;
    private String str_usertype = "", agentcode = "", agentMobile = "", agentEmail = "";
    private Context mContext;
    private CommonMethods mCommonMethods;
    private StorageUtils mStorageUtils;
    private NeedAnalysisBIService NABIObj;
    private String na_input = "";
    private String na_output = "";
    private String Check = "";
    private String isSmoker = "";
    private String staffStatus = "";
    private String critiSumAssured = "";
    private String discountPercentage = "";
    private String TotalApplicableTaxes = "";
    private int needAnalysis_flag = 0;
    private NA_CBI_bean na_cbi_bean;
    private ImageButton ib_poorn_suraksha_policy_holder_sign, ib_poorn_suraksha_marketing_sign,
            ib_poorn_suraksha_parivartan_proposer_photo;
    private String bankUserType = "";

    private CheckBox cb_kerladisc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.bi_poornsurakshamain);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);

        mContext = this;
        mCommonMethods = new CommonMethods();
        mStorageUtils = new StorageUtils();
        prsObj = new ParseXML();
        obj = new CommonForAllProd();

        db = new DatabaseHelper(this);
        mCommonMethods.setActionbarLayout(this);
        NABIObj = new NeedAnalysisBIService(this);

        initialiseDate();

        Intent intent = getIntent();

        //Added Tushar Kadam Kerla Applicable Tax 7-Jun-2019
        TableRow tablerowKerlaDiscount = findViewById(R.id.tablerowKerlaDiscount);
        cb_kerladisc = findViewById(R.id.cb_kerladisc);
        mCommonMethods.setKerlaDiscount(mContext, tablerowKerlaDiscount, cb_kerladisc);
        //End Added Tushar Kadam Kerla Applicable Tax 7-Jun-2019

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
                            db.GetUserCode());

                    agentMobile = SimpleCrypto.decrypt("SBIL",
                            db.GetMobileNo());
                    agentEmail = SimpleCrypto.decrypt("SBIL",
                            db.GetEmailId());
                    str_usertype = SimpleCrypto.decrypt("SBIL",
                            db.GetUserType());

                    /* parivartan changes */
                    ProductInfo prodInfoObj = new ProductInfo(mContext);
                    planName = "Poorna Suraksha";
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

        retVal = new StringBuilder();
        poornSurakshaBean = new PoornSurakshaBean();
        list_data = new ArrayList<>();
        initialise_varibles();
        date_comparison();
    }

    /**
     * Used To Change date From mm-dd-yyyy to dd-mm-yyyy
     */
    private String getDate(String OldDate) {
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

    private void initialise_varibles() {
        svPoornSurakshaMain = findViewById(R.id.sv_bi_poorn_suraksha_main);
        btn_bi_poorn_suraksha_proposer_date = findViewById(R.id.btn_bi_poorn_suraksha_proposer_date);
        rb_poorn_suraksha_proposer_same_as_life_assured_yes = findViewById(R.id.rb_poorn_suraksha_proposer_same_as_life_assured_yes);
        rb_poorn_suraksha_proposer_same_as_life_assured_no = findViewById(R.id.rb_poorn_suraksha_proposer_same_as_life_assured_no);

        rb_proposerdetail_personaldetail_backdating_yes = findViewById(R.id.rb_poorn_suraksha_backdating_yes);
        rb_proposerdetail_personaldetail_backdating_no = findViewById(R.id.rb_poorn_suraksha_backdating_no);

        rb_poorn_suraksha_life_assured_smoker_yes = findViewById(R.id.rb_poorn_suraksha_life_assured_smoker_yes);
        rb_poorn_suraksha_life_assured_smoker_no = findViewById(R.id.rb_poorn_suraksha_life_assured_smoker_no);

        ll_backdating1 = findViewById(R.id.ll_backdating1);
        edt_bi_poorn_suraksha_age = findViewById(R.id.edt_bi_poorn_suraksha_proposer_age);
        spnr_bi_poorn_suraksha_proposer_title = findViewById(R.id.spnr_bi_poorn_suraksha_proposer_title);
        edt_bi_poorn_suraksha_proposer_first_name = findViewById(R.id.edt_bi_poorn_suraksha_proposer_first_name);
        edt_bi_poorn_suraksha_proposer_middle_name = findViewById(R.id.edt_bi_poorn_suraksha_proposer_middle_name);
        edt_bi_poorn_suraksha_proposer_last_name = findViewById(R.id.edt_bi_poorn_suraksha_proposer_last_name);

        spnr_bi_poorn_suraksha_life_assured_title = findViewById(R.id.spnr_bi_poorn_suraksha_life_assured_title);
        //edt_bi_poorn_suraksha_life_assured_age = (EditText) findViewById(R.id.edt_bi_poorn_suraksha_life_assured_age);
        edt_bi_poorn_suraksha_life_assured_first_name = findViewById(R.id.edt_bi_poorn_suraksha_life_assured_first_name);
        edt_bi_poorn_suraksha_life_assured_middle_name = findViewById(R.id.edt_bi_poorn_suraksha_life_assured_middle_name);
        edt_bi_poorn_suraksha_life_assured_last_name = findViewById(R.id.edt_bi_poorn_suraksha_life_assured_last_name);
        btn_bi_poorn_suraksha_life_assured_date = findViewById(R.id.btn_bi_poorn_suraksha_life_assured_date);
        btn_proposerdetail_personaldetail_backdatingdate = findViewById(R.id.btn_poorn_suraksha_backdatingdate);
        TableRow tv_monthly_mode = findViewById(R.id.tv_monthly_mode);
        tr_poorn_suraksha_proposer_detail2 = findViewById(R.id.tr_poorn_suraksha_proposer_detail2);
        tr_poorn_suraksha_proposer_detail1 = findViewById(R.id.tr_poorn_suraksha_proposer_detail1);
        TableRow tr_poorn_suraksha_life_assured_detail2 = findViewById(R.id.tr_poorn_suraksha_life_assured_detail2);
        TableRow tr_poorn_suraksha_life_assured_detail1 = findViewById(R.id.tr_poorn_suraksha_life_assured_detail1);

        edt_proposerdetail_basicdetail_contact_no = findViewById(R.id.edt_poorn_suraksha_contact_no);
        edt_proposerdetail_basicdetail_Email_id = findViewById(R.id.edt_poorn_suraksha_Email_id);
        edt_proposerdetail_basicdetail_ConfirmEmail_id = findViewById(R.id.edt_poorn_suraksha_ConfirmEmail_id);

        // Policy Term
        selBasicTerm = findViewById(R.id.policyterm);

        // Gender
        selGender = findViewById(R.id.selGender);

        basicSA = findViewById(R.id.premium_amt);

        // Age
        ageInYears = findViewById(R.id.age);

        // Premium Frequency
        selPremFreq = findViewById(R.id.premiumfreq);

        // UI elements
        selStaffDisc = findViewById(R.id.cb_staffdisc);

        mCommonMethods.fillSpinnerValue(mContext, spnr_bi_poorn_suraksha_proposer_title,
                Arrays.asList(getResources().getStringArray(R.array.arr_select_title)));
        mCommonMethods.fillSpinnerValue(mContext, spnr_bi_poorn_suraksha_life_assured_title,
                Arrays.asList(getResources().getStringArray(R.array.arr_select_title)));

        //get values from database
        //getValueFromDatabase();

        String[] premFreqList = {"Yearly", "Half-Yearly", "Monthly"};
        ArrayAdapter<String> premFreqAdapter = new ArrayAdapter<>(
                getApplicationContext(), R.layout.spinner_item, premFreqList);
        premFreqAdapter.setDropDownViewResource(R.layout.spinner_item1);
        selPremFreq.setAdapter(premFreqAdapter);
        premFreqAdapter.notifyDataSetChanged();

        String[] ageList = new String[48];
        for (int i = 18; i <= 65; i++) {
            ageList[i - 18] = i + "";
        }

        ArrayAdapter<String> ageAdapter = new ArrayAdapter<>(
                getApplicationContext(), R.layout.spinner_item, ageList);
        ageAdapter.setDropDownViewResource(R.layout.spinner_item1);

        ageInYears.setEnabled(false);
        ageInYears.setClickable(false);
        ageInYears.setAdapter(ageAdapter);
        ageAdapter.notifyDataSetChanged();

        ArrayAdapter<String> genderAdapter = new ArrayAdapter<>(
                getApplicationContext(), R.layout.spinner_item, getResources().getStringArray(R.array.gender_all_arrays));
        genderAdapter.setDropDownViewResource(R.layout.spinner_item1);
        selGender.setAdapter(genderAdapter);
        genderAdapter.notifyDataSetChanged();

        showAlert = new AlertDialog.Builder(this);

        String[] policyTermList = {"10", "15", "20", "25", "30"};
        ArrayAdapter<String> policyTermAdapter = new ArrayAdapter<>(
                getApplicationContext(), R.layout.spinner_item, policyTermList);
        policyTermAdapter.setDropDownViewResource(R.layout.spinner_item1);
        selBasicTerm.setAdapter(policyTermAdapter);
        policyTermAdapter.notifyDataSetChanged();

        DecimalFormat currencyFormat = new DecimalFormat("##,##,##,###");
        back = findViewById(R.id.back);
        btnSubmit = findViewById(R.id.btnSubmit);

//		selBancAssuranceDisc.setVisibility(View.GONE);
        selPremFreq.setSelection(2, false);

        //show input if availble
        //setBIInputGui();

        edt_bi_poorn_suraksha_life_assured_first_name.setOnEditorActionListener(this);
        edt_bi_poorn_suraksha_life_assured_middle_name.setOnEditorActionListener(this);
        edt_bi_poorn_suraksha_life_assured_last_name.setOnEditorActionListener(this);

        edt_bi_poorn_suraksha_proposer_first_name.setOnEditorActionListener(this);
        edt_bi_poorn_suraksha_proposer_middle_name.setOnEditorActionListener(this);
        edt_bi_poorn_suraksha_proposer_last_name.setOnEditorActionListener(this);

        basicSA.setOnEditorActionListener(this);

        setFocusable(spnr_bi_poorn_suraksha_life_assured_title);
        spnr_bi_poorn_suraksha_life_assured_title.requestFocus();

        setSpinnerAndOtherListner();
        edt_proposerdetail_basicdetail_contact_no
                .addTextChangedListener(new TextWatcher() {

                    @Override
                    public void onTextChanged(CharSequence arg0, int arg1,
                                              int arg2, int arg3) {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void beforeTextChanged(CharSequence arg0, int arg1,
                                                  int arg2, int arg3) {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void afterTextChanged(Editable arg0) {
                        // TODO Auto-generated method stub
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
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void beforeTextChanged(CharSequence arg0, int arg1,
                                                  int arg2, int arg3) {
                        // TODO Auto-generated method stub

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
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void beforeTextChanged(CharSequence arg0, int arg1,
                                                  int arg2, int arg3) {
                        // TODO Auto-generated method stub

                    }

                    @Override
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

        TableRow tr_staff_disc = findViewById(R.id.tr_poorn_suraksha_staff_disc);

        try {
            str_usertype = SimpleCrypto.decrypt("SBIL",
                    db.GetUserType());

        } catch (Exception e) {
            e.printStackTrace();
        }
        /*if (str_usertype.equalsIgnoreCase("BAP")
                || str_usertype.equalsIgnoreCase("CAG")
                || str_usertype.equalsIgnoreCase("IMF")) {
            tr_staff_disc.setVisibility(View.GONE);
        }*/
    }

    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

        boolean flagFocus = true;
        if (!flagFocus) {
            svPoornSurakshaMain.requestFocus();
        }

    }

    private void setSpinnerAndOtherListner() {
        // TODO Auto-generated method stub

        rb_poorn_suraksha_proposer_same_as_life_assured_yes
                .setOnCheckedChangeListener(new OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton arg0,
                                                 boolean checked) {
                        // TODO Auto-generated method stub
                        if (checked) {
                            proposer_Is_Same_As_Life_Assured = "y";
                            tr_poorn_suraksha_proposer_detail1
                                    .setVisibility(View.GONE);
                            tr_poorn_suraksha_proposer_detail2
                                    .setVisibility(View.GONE);
                        }

                    }
                });
        rb_poorn_suraksha_proposer_same_as_life_assured_no
                .setOnCheckedChangeListener(new OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton arg0,
                                                 boolean checked) {
                        // TODO Auto-generated method stub
                        if (checked) {
                            proposer_Is_Same_As_Life_Assured = "n";
                            tr_poorn_suraksha_proposer_detail1
                                    .setVisibility(View.VISIBLE);
                            tr_poorn_suraksha_proposer_detail2
                                    .setVisibility(View.VISIBLE);
                        }

                    }
                });

        rb_poorn_suraksha_life_assured_smoker_yes.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // TODO Auto-generated method stub
                if (isChecked) {
                    proposer_isSmoker = "Smoker";
                }
            }
        });

        rb_poorn_suraksha_life_assured_smoker_no.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // TODO Auto-generated method stub
                if (isChecked) {
                    proposer_isSmoker = "Non-Smoker";
                }
            }
        });

        rb_proposerdetail_personaldetail_backdating_yes
                .setOnCheckedChangeListener(new OnCheckedChangeListener() {
                    @Override
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
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView,
                                                 boolean isChecked) {
                        // TODO Auto-generated method stub
                        if (isChecked) {

                            proposer_Backdating_WishToBackDate_Policy = "n";
                            proposer_Backdating_BackDate = "";
                            // setDefaultDate();
                            ll_backdating1.setVisibility(View.GONE);

                            ageInYears
                                    .setSelection(
                                            getIndex(ageInYears, lifeAssuredAge),
                                            false);
                            valAge();
                            rb_proposerdetail_personaldetail_backdating_yes
                                    .setFocusable(false);

                            clearFocusable(rb_proposerdetail_personaldetail_backdating_no);
                            clearFocusable(rb_proposerdetail_personaldetail_backdating_yes);
//							setFocusable(selPTARider);
//							selPTARider.requestFocus();

                        }
                    }
                });

        spnr_bi_poorn_suraksha_proposer_title
                .setOnItemSelectedListener(new OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int position, long id) {
                        // TODO Auto-generated method stub
                        if (position == 0) {
                            proposer_Title = "";
                        } else {
                            proposer_Title = spnr_bi_poorn_suraksha_proposer_title
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
                        // TODO Auto-generated method stub

                    }
                });

        // Age
        /*ageInYears.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int pos,
                                       long id) {
                valAge();

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });*/

        // Prem Frequency
        selPremFreq.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int pos,
                                       long id) {
                valAge();
//				if (selPremFreq.getSelectedItem().toString().equals("Monthly")) {
//					tv_monthly_mode.setVisibility(View.GONE);
//				} else {
//					tv_monthly_mode.setVisibility(View.GONE);
//				}
                clearFocusable(selPremFreq);
                setFocusable(basicSA);
                basicSA.requestFocus();

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });


        selBasicTerm.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int position, long id) {
                // TODO Auto-generated method stub
                valMaturityAge();

                clearFocusable(selBasicTerm);

                if (flagFirstFocus) {
                    setFocusable(edt_bi_poorn_suraksha_life_assured_first_name);
                    edt_bi_poorn_suraksha_life_assured_first_name.requestFocus();
                    flagFirstFocus = false;
                } else {
                    setFocusable(selPremFreq);
                    selPremFreq.requestFocus();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });


        // Staff Discount
        selStaffDisc.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selStaffDisc.isChecked()) {
                    selStaffDisc.setChecked(true);
                    clearFocusable(spnr_bi_poorn_suraksha_life_assured_title);
                    setFocusable(spnr_bi_poorn_suraksha_life_assured_title);
                    spnr_bi_poorn_suraksha_life_assured_title.requestFocus();
                }

            }
        });


        spnr_bi_poorn_suraksha_life_assured_title
                .setOnItemSelectedListener(new OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int position, long id) {
                        // TODO Auto-generated method stub
                        if (position == 0) {
                            lifeAssured_Title = "";
                        } else {
                            lifeAssured_Title = spnr_bi_poorn_suraksha_life_assured_title
                                    .getSelectedItem().toString();

                            clearFocusable(spnr_bi_poorn_suraksha_life_assured_title);
                            setFocusable(edt_bi_poorn_suraksha_life_assured_first_name);

                            edt_bi_poorn_suraksha_life_assured_first_name
                                    .requestFocus();
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                        // TODO Auto-generated method stub

                    }
                });
        selGender.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int position, long id) {

                // TODO Auto-generated method stub
                if (position == 0) {
                    str_lifeAssured_gender = "";
                } else {

                    str_lifeAssured_gender = selGender.getSelectedItem()
                            .toString();
                    clearFocusable(selGender);
                    setFocusable(edt_bi_poorn_suraksha_life_assured_first_name);

                    btn_bi_poorn_suraksha_life_assured_date.requestFocus();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });

        // Go Home Button
        back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                setResult(RESULT_OK);
                finish();
            }
        });

        // Submit Button
        btnSubmit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stu

                inputVal = new StringBuilder();
                retVal = new StringBuilder();
                bussIll = new StringBuilder();


                str_lifeAssured_gender = selGender.getSelectedItem().toString();
                lifeAssured_Title = spnr_bi_poorn_suraksha_life_assured_title.getSelectedItem().toString();


                proposer_First_Name = edt_bi_poorn_suraksha_proposer_first_name
                        .getText().toString();
                proposer_Middle_Name = edt_bi_poorn_suraksha_proposer_middle_name
                        .getText().toString();
                proposer_Last_Name = edt_bi_poorn_suraksha_proposer_last_name
                        .getText().toString();

                name_of_proposer = proposer_Title + " " + proposer_First_Name
                        + " " + proposer_Middle_Name + " " + proposer_Last_Name;

                lifeAssured_First_Name = edt_bi_poorn_suraksha_life_assured_first_name
                        .getText().toString();
                lifeAssured_Middle_Name = edt_bi_poorn_suraksha_life_assured_middle_name
                        .getText().toString();
                lifeAssured_Last_Name = edt_bi_poorn_suraksha_life_assured_last_name
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

                Date();

                if (valProposerSameAsLifeAssured() &&
                        valLifeAssuredProposerDetail() && valDob()
                        && valBasicDetail() && valTerm()
                        && valSMOKER()
                        && valSA()
                        && valMaturityAge()
                        && valminPremiumValueAndRider()) {

                    addListenerOnSubmit();
                    getInput(poornSurakshaBean);

                    if (needAnalysis_flag == 0) {
                        Intent i = new Intent(BI_PoornSurakshaActivity.this,
                                Success.class);
                        i.putExtra("op", "Life Cover Basic Premium is Rs. " + InstallmentPremLifeCover_ExclSt);
                        i.putExtra("op1", "Life Cover Applicable Tax is Rs. " + ApplicableTaxesLifeCover);
                        i.putExtra("op2", "Life Cover Premium with Applicable Tax is Rs. " + InstallmentPremLifeCover_InclSt);

                        i.putExtra("op3", "Critical Illness Cover Basic Premium is Rs. " + TotalPremiumWithoutST_CI);
                        i.putExtra("op4", "Critical Illness Cover Basic Applicable Tax is Rs. " + ServiceTax_CI);
                        i.putExtra("op5", "Critical Illness Cover Basic Premium with Applicable Tax is Rs. " + TotalPremiumWithST_CI);

                        //          i.putExtra("op6","Total Basic Premium is Rs."+ installmntPremWithoutSerTx);
                        //          i.putExtra("op7","Total Applicable Tax is Rs."+ servTax);
                        i.putExtra("op6", "Total Premium with Applicable Tax is Rs. " + TotalFinalPremium_IncST);
                        i.putExtra("header", "SBI Life - Poorna Suraksha");
                        i.putExtra("header1", "(UIN: 111N110V03)");
                        startActivity(i);

                    } else
                        Dialog();
                }

            }
        });

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

    /************************* Item Listener ends here ********************************************/

    private void initialiseDate() {
        Calendar calender = Calendar.getInstance();
        mYear = calender.get(Calendar.YEAR);
        mMonth = calender.get(Calendar.MONTH);
        mDay = calender.get(Calendar.DAY_OF_MONTH);

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
                        ib_poorn_suraksha_policy_holder_sign
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
                        //ProductHomePageActivity.customer_Signature = CaptureSignature.scaled;
                    } else if (latestImage.equalsIgnoreCase("agent")) {
                        ib_poorn_suraksha_marketing_sign
                                .setImageBitmap(CaptureSignature.scaled);
                        Bitmap signature = CaptureSignature.scaled;
                        if (signature != null) {
                            ByteArrayOutputStream out = new ByteArrayOutputStream();
                            signature.compress(Bitmap.CompressFormat.PNG, 100,
                                    out);
                            byte[] signByteArray = out.toByteArray();
                            agent_sign = Base64.encodeToString(signByteArray,
                                    Base64.DEFAULT);

                            d.dismiss();
                        }
                        //ProductHomePageActivity.agent_Signature = CaptureSignature.scaled;
                    }
                }
            }
        } else if (requestCode == 3) {
            if (resultCode == RESULT_OK) {
                if (Check.equals("Photo")) {

                    File Photo = mCommonMethods.galleryAddPic(mContext);
                    Bitmap bmp = BitmapFactory.decodeFile(Photo
                            .getAbsolutePath());

                    Bitmap b = null;
                    Uri imageUri;
                    try {

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            imageUri = mCommonMethods.getContentUri(mContext, new File(Photo.toString()));
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
                        ib_poorn_suraksha_parivartan_proposer_photo.setImageBitmap(scaled);
                    } else {
                        photoBitmap = null;
                    }
                }
            }
        }
    }

    public void onClickDob(View v) {
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
            mCommonMethods.dialogWarning(mContext, "Please select a LifeAssured DOB First", true);
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
        } else {
            age = tYear - mYear - 1;
        }

        String final_age = age + " yrs";

        if (final_age.contains("-")) {
            mCommonMethods.dialogWarning(mContext, "Please fill Valid Date", true);
        } else {
            switch (id) {

                case 2:
                    btn_PolicyholderDate.setText(date);
                    break;
                case 3:
                    btn_MarketingOfficalDate.setText(date);
                    break;
                case 4:
                    String proposerAge = final_age;
                    final_age = Integer.toString(age);
                    if (final_age.contains("-")) {
                        mCommonMethods.BICommonDialog(mContext, "Please fill Valid Birth Date");
                    } else {
                        if (18 <= age) {

                            btn_bi_poorn_suraksha_proposer_date.setText(date);
                            edt_bi_poorn_suraksha_age.setText(final_age);
                            proposer_date_of_birth = getDate1(date + "");
                            // ageInYears
                            // .setSelection(getIndex(ageInYears, final_age));
                            // valAge();
                        } else {
                            mCommonMethods.BICommonDialog(mContext, "Minimum age should be 18 yrs for proposer");
                            btn_bi_poorn_suraksha_proposer_date
                                    .setText("Select Date");
                            edt_bi_poorn_suraksha_age.setText("");
                            proposer_date_of_birth = "";
                        }
                    }
                    break;

                case 5:

                    final_age = Integer.toString(age);
                    if (final_age.contains("-")) {
                        mCommonMethods.BICommonDialog(mContext, "Please fill Valid Birth Date");
                    } else {
                        int maxLimit;
                        maxLimit = 65;

                        if (18 <= age && age <= maxLimit) {
                            lifeAssuredAge = final_age;
                            btn_bi_poorn_suraksha_life_assured_date.setText(date);
                            //edt_bi_poorn_suraksha_life_assured_age.setText(final_age);

                            ageInYears.setSelection(
                                    getIndex(ageInYears, final_age), false);
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
                            } else {

                                clearFocusable(btn_bi_poorn_suraksha_life_assured_date);

                                setFocusable(edt_proposerdetail_basicdetail_contact_no);
                                edt_proposerdetail_basicdetail_contact_no
                                        .requestFocus();
                                /*
                                 * setFocusable(selPlan); selPlan.requestFocus();
                                 */
                            }

                        } else {
                            mCommonMethods.BICommonDialog(mContext, "Minimum Age should be 18 yrs and Maximum Age should be "
                                    + maxLimit + " yrs For LifeAssured");
                            btn_bi_poorn_suraksha_life_assured_date
                                    .setText("Select Date");
                            //edt_bi_poorn_suraksha_life_assured_age.setText("");
                            lifeAssured_date_of_birth = "";

                            clearFocusable(btn_bi_poorn_suraksha_life_assured_date);
                            setFocusable(btn_bi_poorn_suraksha_life_assured_date);
                            btn_bi_poorn_suraksha_life_assured_date.requestFocus();
                        }
                    }
                    break;

                case 6:
                    if (age >= 0) {
                        proposer_Backdating_BackDate = date + "";
                        btn_proposerdetail_personaldetail_backdatingdate
                                .setText(proposer_Backdating_BackDate);
                        clearFocusable(btn_proposerdetail_personaldetail_backdatingdate);
                    } else {
                        mCommonMethods.dialogWarning(mContext, "Please Select Valid BackDating Date", true);
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
            String str_final_Age = Integer.toString(Proposerage);
            ageInYears.setSelection(getIndex(ageInYears, str_final_Age), false);
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
        DIALOG_ID = 5;
        showDialog(DATE_DIALOG_ID);
    }

    // public void parse_Xml()
    // {
    //
    // }

    private void windowmessagesgin() {

        d = new Dialog(BI_PoornSurakshaActivity.this);
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
                Intent intent = new Intent(BI_PoornSurakshaActivity.this,
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

    // Dialog USed For Displaying BI

    public void windowmessageProposersgin() {

        d = new Dialog(BI_PoornSurakshaActivity.this);
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
                Intent intent = new Intent(BI_PoornSurakshaActivity.this,
                        ProposerCaptureSignature.class);
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

    private int getIndex(Spinner s1, String value) {

        int index = 0;

        for (int i = 0; i < s1.getCount(); i++) {
            if (s1.getItemAtPosition(i).equals(value)) {
                index = i;
            }
        }
        return index;
    }

    private void Dialog() {
        final Dialog d = new Dialog(this);
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);
        d.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.BLACK));

        d.setContentView(R.layout.layout_poorn_suraksha_bi_grid);

        TextView tv_proposername = d
                .findViewById(R.id.tv_proposername);
        TextView tv_proposal_number = d
                .findViewById(R.id.tv_proposal_number);

        TextView tv_bi_poorn_suraksha_life_assured_name = d
                .findViewById(R.id.tv_bi_poorn_suraksha_life_assured_name);
        TextView tv_bi_poorn_suraksha_life_assured_age = d
                .findViewById(R.id.tv_bi_poorn_suraksha_life_assured_age);
        TextView tv_bi_poorn_suraksha_life_assured_gender = d
                .findViewById(R.id.tv_bi_poorn_suraksha_life_assured_gender);
        TextView tv_bi_poorn_suraksha_life_assured_premium_frequency = d
                .findViewById(R.id.tv_bi_poorn_suraksha_life_assured_premium_frequency);

//		TextView tv_bi_poorn_suraksha_plan_proposed = (TextView) d
//				.findViewById(R.id.tv_bi_poorn_suraksha_plan_proposed);
        TextView tv_bi_poorn_suraksha_term = d
                .findViewById(R.id.tv_bi_poorn_suraksha_term);
        TextView tv_bi_poorn_suraksha_premium_paying_term = d
                .findViewById(R.id.tv_bi_poorn_suraksha_premium_paying_term);
        TextView tv_bi_poorn_suraksha_sum_assured = d
                .findViewById(R.id.tv_bi_poorn_suraksha_sum_assured);
        TextView tv_bi_poorn_suraksha_yearly_premium = d

                .findViewById(R.id.tv_bi_poorn_suraksha_yearly_premium);

        TextView tv_basic_premium_tag = d
                .findViewById(R.id.tv_basic_premium_tag);

        /*TextView tv_bi_poorn_suraksha_basic_premium = (TextView) d
                .findViewById(R.id.tv_bi_poorn_suraksha_basic_premium);
        TextView tv_bi_poorn_suraksha_service_tax = (TextView) d
                .findViewById(R.id.tv_bi_poorn_suraksha_service_tax);*/
        TextView tv_bi_poorn_suraksha_yearly_premium_with_tax = d
                .findViewById(R.id.tv_bi_poorn_suraksha_yearly_premium_with_tax);

        // Second year tables

        final TextView tv_premium_install_rider_type1 = d
                .findViewById(R.id.tv_premium_install_rider_type1);
        final TextView tv_mandatory_bi_poorn_suraksha_yearly_premium_with_tax1 = d
                .findViewById(R.id.tv_mandatory_bi_poorn_suraksha_yearly_premium_with_tax1);

        final TextView tv_mandatory_bi_poorn_suraksha_yearly_premium_with_tax12 = d
                .findViewById(R.id.tv_mandatory_bi_poorn_suraksha_yearly_premium_with_tax12);

        // First year policy
        TextView tv_bi_poorn_suraksha_basic_premium_first_year = d
                .findViewById(R.id.tv_bi_poorn_suraksha_basic_premium_first_year);
        TextView tv_bi_poorn_suraksha_service_tax_first_year = d
                .findViewById(R.id.tv_bi_poorn_suraksha_service_tax_first_year);
        TextView tv_bi_poorn_suraksha_yearly_premium_with_tax_first_year = d
                .findViewById(R.id.tv_bi_poorn_suraksha_yearly_premium_with_tax_first_year);

        //new changes starts
        //Critical Illness
        TextView tv_bi_poorn_suraksha_basic_premium_second_year = d
                .findViewById(R.id.tv_bi_poorn_suraksha_basic_premium_second_year);
        TextView tv_bi_poorn_suraksha_service_tax_second_year = d
                .findViewById(R.id.tv_bi_poorn_suraksha_service_tax_second_year);
        TextView tv_bi_poorn_suraksha_yearly_premium_with_tax_second_year = d
                .findViewById(R.id.tv_bi_poorn_suraksha_yearly_premium_with_tax_second_year);
        TextView tv_bi_poorn_suraksha_total_premium_with_life_critical_illness = d
                .findViewById(R.id.tv_bi_poorn_suraksha_total_premium_with_life_critical_illness);
        TextView tv_poorn_suraksha_data = d
                .findViewById(R.id.tv_poorn_suraksha_data);
        //new changes ends

        final TextView edt_proposer_name = d
                .findViewById(R.id.edt_proposer_name);


        GridView gv_userinfo = d.findViewById(R.id.gv_userinfo);
        final EditText edt_Policyholderplace = d
                .findViewById(R.id.edt_Policyholderplace);


        final TextView tv_premium_type = d
                .findViewById(R.id.tv_premium_type);

        /*final TextView tv_premium_type_rider = (TextView) d
                .findViewById(R.id.tv_premium_type_rider);*/


        TextView tv_bi_is_Staff = d
                .findViewById(R.id.tv_bi_is_Staff);

        TextView tv_bi_is_smoker = d.findViewById(R.id.tv_bi_is_smoker);


        /*TextView tv_bi_poorn_suraksha_basic_service_tax_first_year = (TextView) d
                .findViewById(R.id.tv_bi_poorn_suraksha_basic_service_tax_first_year);*/

        TextView tv_uin_poorn_suraksha = d
                .findViewById(R.id.tv_uin_poorn_suraksha);

        final CheckBox cb_statement = d
                .findViewById(R.id.cb_statement);
        cb_statement.setChecked(false);

        /* parivaratan changes */
        ib_poorn_suraksha_parivartan_proposer_photo = d
                .findViewById(R.id.ib_poorn_suraksha_parivartan_proposer_photo);
        ib_poorn_suraksha_parivartan_proposer_photo
                .setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Check = "Photo";
                        mCommonMethods.windowmessage(mContext, "_cust1Photo.jpg");
                    }
                });
        /* end */

        /* Need Analysis */
        final TextView edt_proposer_name_need_analysis = d
                .findViewById(R.id.edt_proposer_name_need_analysis);

        final CheckBox cb_statement_need_analysis = d
                .findViewById(R.id.cb_statement_need_analysis);
        cb_statement_need_analysis.setChecked(true);
        TableRow tr_need_analysis = d
                .findViewById(R.id.tr_need_analysis);

        if (NeedAnalysisActivity.str_need_analysis != null) {
            if (NeedAnalysisActivity.str_need_analysis.equals("1")) {

                File mypath_old = mStorageUtils.createFileToAppSpecificDir(mContext,"NA" + ".pdf");
                File mypath_new = mStorageUtils.createFileToAppSpecificDir(mContext, QuatationNumber + "_NA"
                        + ".pdf");
                if (mypath_old.exists()) {
                    mypath_old.renameTo(mypath_new);
                }
                flg_needAnalyis = "1";
                tr_need_analysis.setVisibility(View.VISIBLE);
            } else {
                cb_statement_need_analysis.setChecked(true);
                tr_need_analysis.setVisibility(View.GONE);
            }

        }

        LinearLayout ll_endowment = d
                .findViewById(R.id.ll_endowment);
        LinearLayout ll_endowment_with_option = d
                .findViewById(R.id.ll_endowment_with_option);

        Button btn_proceed = d.findViewById(R.id.btn_proceed);

        //parivartan changes
        ib_poorn_suraksha_policy_holder_sign = d.findViewById(R.id.ib_poorn_suraksha_policy_holder_sign);
        ib_poorn_suraksha_marketing_sign = d.findViewById(R.id.ib_poorn_suraksha_marketing_sign);

        btn_PolicyholderDate = d
                .findViewById(R.id.btn_PolicyholderDate);
        btn_MarketingOfficalDate = d
                .findViewById(R.id.btn_MarketingOfficalDate);

        list_data.clear();

        if (!proposer_sign.equals("")) {
            if (flg_needAnalyis.equals("1")) {
                tr_need_analysis.setVisibility(View.VISIBLE);
            } else {
                cb_statement_need_analysis.setChecked(true);
                tr_need_analysis.setVisibility(View.GONE);
            }
        }
        if (proposer_Is_Same_As_Life_Assured.equalsIgnoreCase("y")) {
            edt_proposer_name.setText("I ," + name_of_life_assured + " having received the information with respect to the above, have understood the above statement before entering into the contract.");


            edt_proposer_name_need_analysis.setText("I ," + name_of_life_assured + ", have undergone the Need Analysis after having reviewed the SBI Life Product options, I have opted for SBI LIFE-Poorna Suraksha.");
            tv_proposername.setText(name_of_life_assured);
            tv_bi_poorn_suraksha_life_assured_name.setText(name_of_life_assured);
        } else if (proposer_Is_Same_As_Life_Assured.equalsIgnoreCase("n")) {
            edt_proposer_name.setText("I ," + name_of_proposer + " having received the information with respect to the above, have understood the above statement before entering into the contract.");


            edt_proposer_name_need_analysis.setText("I ," + name_of_proposer + ", have undergone the Need Analysis after having reviewed the SBI Life Product options, I have opted for SBI LIFE-Poorna Suraksha.");
            tv_proposername.setText(name_of_proposer);
            tv_bi_poorn_suraksha_life_assured_name.setText(name_of_proposer);
        }
        tv_proposal_number.setText(QuatationNumber);

        if (!place2.equals("")) {
            edt_Policyholderplace.setText(place2);

        }

        //edt_MarketingOfficalPlace.setText(place1);

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
            ib_poorn_suraksha_marketing_sign.setImageBitmap(bitmap);
        }

        if (proposer_sign != null && !proposer_sign.equals("")) {
            byte[] signByteArray = Base64.decode(proposer_sign, 0);
            Bitmap bitmap = BitmapFactory.decodeByteArray(signByteArray, 0,
                    signByteArray.length);
            ib_poorn_suraksha_policy_holder_sign.setImageBitmap(bitmap);
        }


        ib_poorn_suraksha_marketing_sign
                .setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (cb_statement.isChecked()
                                && cb_statement_need_analysis.isChecked()) {
                            latestImage = "agent";
                            windowmessagesgin();
                        } else {
                            mCommonMethods.dialogWarning(mContext, "Please Tick on I Agree Clause ", true);
                            setFocusable(cb_statement);
                            cb_statement.requestFocus();
                        }
                    }
                });

        ib_poorn_suraksha_policy_holder_sign
                .setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (cb_statement.isChecked()
                                && cb_statement_need_analysis.isChecked()) {
                            latestImage = "proposer";
                            //windowmessagesgin();
                            //windowmessageProposersgin();
                            mCommonMethods.windowmessageProposersgin(mContext,
                                    NeedAnalysisActivity.URN_NO + "_cust1sign");
                        } else {
                            mCommonMethods.dialogWarning(mContext, "Please Tick on I Agree Clause ", true);
                            setFocusable(cb_statement);
                            cb_statement.requestFocus();
                        }

                    }
                });

        final RadioButton rb_poorn_suraksha_trasaction_manual = d
                .findViewById(R.id.rb_poorn_suraksha_trasaction_manual);
        final RadioButton rb_poorn_suraksha_trasaction_parivartan = d
                .findViewById(R.id.rb_poorn_suraksha_trasaction_parivartan);
        final LinearLayout ll_poorn_suraksha_parivartan_transaction = d
                .findViewById(R.id.ll_poorn_suraksha_parivartan_transaction);

        rb_poorn_suraksha_trasaction_parivartan
                .setOnCheckedChangeListener(new OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton buttonView,
                                                 boolean isChecked) {
                        // TODO Auto-generated method stub
                        if (isChecked) {
                            ll_poorn_suraksha_parivartan_transaction
                                    .setVisibility(View.VISIBLE);
                        } else {
                            ll_poorn_suraksha_parivartan_transaction
                                    .setVisibility(View.GONE);

                            String customerPhotoName = NeedAnalysisActivity.URN_NO
                                    + "_cust1Photo.jpg";
                            File customerPhotoFile = mStorageUtils.createFileToAppSpecificDir(mContext,
                                    customerPhotoName);
                            if (customerPhotoFile.exists()) {
                                customerPhotoFile.delete();
                            }

                            photoBitmap = null;
                            ib_poorn_suraksha_parivartan_proposer_photo
                                    .setImageDrawable(getResources()
                                            .getDrawable(
                                                    R.drawable.focus_imagebutton_photo));
                        }
                    }
                });

        btn_proceed.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                String isActive = "0";

                name_of_person = edt_proposer_name.getText().toString();
                //place1 = edt_MarketingOfficalPlace.getText().toString();
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
                        //parivartan changes
                        && (((photoBitmap != null
                        /*//remove parivartan validation
								&& ((radioButtonDepositPaymentNo.isChecked() == true && !thirdPartySign
						.equals("")) || radioButtonDepositPaymentYes
						.isChecked() == true) && ((radioButtonAppointeeYes
						.isChecked() == true && !appointeeSign
						.equals("")) || radioButtonAppointeeNo
								.isChecked() == true)*/
                ) && rb_poorn_suraksha_trasaction_parivartan.isChecked())
                        || rb_poorn_suraksha_trasaction_manual.isChecked())) {

                    NeedAnalysisActivity.str_need_analysis = "";

                    String mode = "";
                    if (rb_poorn_suraksha_trasaction_parivartan.isChecked()) {
                        mode = "Parivartan";
                    } else if (rb_poorn_suraksha_trasaction_manual.isChecked()) {
                        mode = "Manual";
                    }

                    String productCode = "";

                    /*
                      Used To Change date From mm-dd-yyyy to dd/mm/yyyy
                     */
                    String life_assured_dob_formatted = "";
                    if (lifeAssured_date_of_birth != null) {
                        String[] new_date_formatted = lifeAssured_date_of_birth.split("-");
                        life_assured_dob_formatted = new_date_formatted[1] + "/" + new_date_formatted[0]
                                + "/" + new_date_formatted[2];
                    }

                    /* parivartan changes */
                    na_cbi_bean = new NA_CBI_bean(
                            QuatationNumber,
                            agentcode,
                            "",
                            str_usertype,
                            "",
                            lifeAssured_Title,
                            lifeAssured_First_Name,
                            lifeAssured_Middle_Name,
                            lifeAssured_Last_Name,
                            planName,
                            obj.getRound(obj.getStringWithout_E(Double
                                    .valueOf((sum_assured.equals("") || sum_assured == null) ? "0"
                                            : sum_assured))),
                            obj.getRound(obj.getStringWithout_E(Double
                                    .valueOf((PremiumBeforeST.equals("") || PremiumBeforeST == null) ? "0"
                                            : PremiumBeforeST))),
                            emailId, mobileNo, agentEmail, agentMobile,
                            na_input, na_output, premium_paying_frequency, Integer
                            .parseInt(policy_term), 0, productCode,
                            life_assured_dob_formatted, "", inputVal
                            .toString(), retVal.toString().replace(
                            bussIll.toString(), ""));

                    name_of_person = name_of_life_assured;

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
                            str_usertype,
                            "",
                            lifeAssured_Title,
                            lifeAssured_First_Name,
                            lifeAssured_Middle_Name,
                            lifeAssured_Last_Name,
                            obj.getRound(obj.getStringWithout_E(Double
                                    .valueOf((sum_assured.equals("") || sum_assured == null) ? "0"
                                            : sum_assured))),
                            obj.getRound(obj.getStringWithout_E(Double
                                    .valueOf((PremiumBeforeST.equals("") || PremiumBeforeST == null) ? "0"
                                            : PremiumBeforeST))),
                            agentEmail, agentMobile, na_input, na_output,
                            premium_paying_frequency, Integer.parseInt(policy_term), 0,
                            productCode, life_assured_dob_formatted,
                            "", "", mode, inputVal
                            .toString(), retVal.toString().replace(
                            bussIll.toString(), "")));
                    /* end */

                    CreatePoornSurakshaPlusBIPdf();

                    NABIObj.serviceHit(BI_PoornSurakshaActivity.this,
                            na_cbi_bean, newFile, needAnalysispath.getPath(),
                            mypath.getPath(), name_of_person, QuatationNumber, mode);
                    d.dismiss();

                } else {

                    if (place2.equals("")) {
                        mCommonMethods.dialogWarning(mContext, "Please Fill Place Detail", true);
                        setFocusable(edt_Policyholderplace);
                        edt_Policyholderplace.requestFocus();

                    } else if (!cb_statement.isChecked()) {
                        mCommonMethods.dialogWarning(mContext, "Please Tick on I Agree Clause ", true);
                        setFocusable(cb_statement);
                        cb_statement.requestFocus();
                    }
                    /* parivartan changes */
                    else if (!rb_poorn_suraksha_trasaction_parivartan.isChecked()
                            && !rb_poorn_suraksha_trasaction_manual.isChecked()) {
                        mCommonMethods.dialogWarning(mContext, "Please Select Transaction Mode", true);
                        setFocusable(ll_poorn_suraksha_parivartan_transaction);
                        ll_poorn_suraksha_parivartan_transaction.requestFocus();
                    } else if (photoBitmap == null) {
                        mCommonMethods.dialogWarning(mContext, "Please Capture the Photo", true);
                        setFocusable(ib_poorn_suraksha_parivartan_proposer_photo);
                        ib_poorn_suraksha_parivartan_proposer_photo.requestFocus();
                    }
                    // dialog("Please Fill All The Detail", true);
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

        String input = inputVal.toString();
        String output = retVal.toString();

        //dailog
//        tv_uin_poorn_suraksha
//                .setText("Premium Quotation \n"
//                        + "SBI LIFE - Poorna Suraksha \n" +
//                        "(UIN:111N110V03)");
        tv_bi_poorn_suraksha_life_assured_name.setText(name_of_life_assured);
        age_entry = prsObj.parseXmlTag(input, "age");
        tv_bi_poorn_suraksha_life_assured_age.setText(age_entry + " Years");

        gender = prsObj.parseXmlTag(input, "gender");
        tv_bi_poorn_suraksha_life_assured_gender.setText(gender);

        premium_paying_frequency = prsObj.parseXmlTag(input, "premFreq");
        tv_bi_poorn_suraksha_life_assured_premium_frequency
                .setText(premium_paying_frequency);

        // if (premium_paying_frequency.equals("Single")) {
        // tr_poorn_suraksha_surrender_value.setVisibility(View.GONE);
        // }

        String staffdiscount = prsObj.parseXmlTag(input, "isStaff");

        if (staffdiscount.equalsIgnoreCase("true")) {
            tv_bi_is_Staff.setText("Staff");
        } else {
            tv_bi_is_Staff.setText("Non Staff");
        }

        proposer_isSmoker = prsObj.parseXmlTag(input, "isSmoker");

        if (proposer_isSmoker.equalsIgnoreCase("Smoker")) {
            tv_bi_is_smoker.setText("Smoker");
        } else {
            tv_bi_is_smoker.setText("Non-Smoker");
        }


        policy_term = prsObj.parseXmlTag(input, "policyTerm");
        tv_bi_poorn_suraksha_term.setText(policy_term + " Years");

        String payingTerm = "";
        if (premium_paying_frequency.equalsIgnoreCase("Single")) {
            payingTerm = "1 Year";
        } else {
            payingTerm = policy_term + " Years";
        }

        tv_bi_poorn_suraksha_premium_paying_term.setText(payingTerm);

        sum_assured = prsObj.parseXmlTag(input, "sumAssured");

        tv_bi_poorn_suraksha_sum_assured.setText("Rs. "
                + getformatedThousandString(Integer.parseInt(obj.getRound(obj
                .getStringWithout_E(Double.valueOf((sum_assured
                        .equals("") || sum_assured == null) ? "0"
                        : sum_assured))))));

        switch (premium_paying_frequency) {
            case "Yearly":
                tv_premium_type.setText("Yearly " + "premium ");
                //tv_premium_type_rider.setText("Yearly " + "premium with Applicable Tax");

//			tv_premium_type1.setText("Yearly " + "premium ");
//			tv_premium_type2.setText("Yearly " + "premium ");
//			tv_premium_type3.setText("Yearly " + "premium ");
//			tv_basic_premium_tag.setText("Yearly " + "premium ");
                tv_premium_install_rider_type1.setText("Yearly Premium," + "\n" + "excluding applicable taxes" + "\n" + "(in Rs.)");
                tv_mandatory_bi_poorn_suraksha_yearly_premium_with_tax1
                        .setText("Yearly Premium," + "\n" + "including" + "\n" + "applicable taxes" + "\n" + "(in Rs.)");
                tv_mandatory_bi_poorn_suraksha_yearly_premium_with_tax12.setText("Yearly Premium Payable, including" + "\n" + "applicable taxes" + "\n" + "(in Rs.)");

                break;
            case "Half-Yearly":
                tv_premium_type.setText("Half-Yearly " + "premium ");
                //tv_premium_type_rider.setText("Half-Yearly " + "premium with Applicable Tax");

//			tv_premium_type1.setText("Half-Yearly " + "premium ");
//			tv_premium_type2.setText("Half-Yearly " + "premium ");
//			tv_premium_type3.setText("Half-Yearly " + "premium ");
//			tv_basic_premium_tag.setText("Half-Yearly " + "premium ");
                tv_premium_install_rider_type1.setText("Half-Yearly Premium," + "\n" + "excluding applicable taxes" + "\n" + "(in Rs.)");
                tv_mandatory_bi_poorn_suraksha_yearly_premium_with_tax1
                        .setText("Half-Yearly Premium," + "\n" + "including" + "\n" + "applicable taxes" + "\n" + "(in Rs.)");
                tv_mandatory_bi_poorn_suraksha_yearly_premium_with_tax12.setText("Half-Yearly Premium Payable, including" + "\n" + "applicable taxes" + "\n" + "(in Rs.)");
                break;
            case "Monthly":
                tv_premium_type.setText("Monthly " + "premium ");
                //tv_premium_type_rider.setText("Monthly " + "premium with Applicable Tax");

//			tv_premium_type1.setText("Monthly " + "premium ");
//			tv_premium_type2.setText("Monthly " + "premium ");
//			tv_premium_type3.setText("Monthly " + "premium ");
//			tv_basic_premium_tag.setText("Monthly " + "premium ");
                tv_premium_install_rider_type1.setText("Monthly Premium," + "\n" + "excluding applicable taxes" + "\n" + "(in Rs.)");
                tv_mandatory_bi_poorn_suraksha_yearly_premium_with_tax1
                        .setText("Monthly Premium," + "\n" + "including" + "\n" + "applicable taxes" + "\n" + "(in Rs.)");
                tv_mandatory_bi_poorn_suraksha_yearly_premium_with_tax12.setText("Monthly Premium Payable, including" + "\n" + "applicable taxes" + "\n" + "(in Rs.)");
                break;
        }

        TotalFinalPremium_IncST = prsObj.parseXmlTag(output, "TotalFinalPremium_IncST");

        tv_bi_poorn_suraksha_yearly_premium.setText("Rs. "
                + obj.getRound(obj.getStringWithout_E(Double.valueOf((TotalFinalPremium_IncST
                .equals("") || TotalFinalPremium_IncST == null) ? "0" : TotalFinalPremium_IncST))));

        String premium_pdf = TotalFinalPremium_IncST;

        //new changes
        /*basicprem = prsObj.parseXmlTag(output, "installmntPrem");

        tv_bi_poorn_suraksha_basic_premium.setText(" Rs."
                + obj.getRound(obj.getStringWithout_E(Double.valueOf(basicprem
                .equals("") ? "0" : basicprem))));

        servicetax = prsObj.parseXmlTag(output, "servTax");

        tv_bi_poorn_suraksha_service_tax.setText(" Rs."
                + obj.getRound(obj.getStringWithout_E(Double.valueOf(servicetax
                .equals("") ? "0" : servicetax))));

        basicplustax = prsObj.parseXmlTag(output, "installmntPremWithSerTx");

        tv_bi_poorn_suraksha_yearly_premium_with_tax
                .setText(" Rs."
                        + obj.getRound(obj.getStringWithout_E(Double
                        .valueOf(basicplustax.equals("") ? "0"
                                : basicplustax))));*/

        // Second year onwards

//		servcTaxSecondYear = prsObj.parseXmlTag(output, "servTaxSeondYear");
//		premWthSTSecondYear = prsObj.parseXmlTag(output,
//				"installmntPremWithSerTxSecondYear");


        InstallmentPremLifeCover_ExclSt = prsObj.parseXmlTag(output, "InstallmentPremLifeCover_ExclSt");

        tv_bi_poorn_suraksha_basic_premium_first_year.setText(""
                + obj.getRound(obj.getStringWithout_E(Double.valueOf(InstallmentPremLifeCover_ExclSt
                .equals("") ? "0" : InstallmentPremLifeCover_ExclSt))));

        ApplicableTaxesLifeCover = prsObj.parseXmlTag(output, "ApplicableTaxesLifeCover");

        tv_bi_poorn_suraksha_service_tax_first_year.setText(""
                + obj.getRound(obj.getStringWithout_E(Double.valueOf(ApplicableTaxesLifeCover
                .equals("") ? "0" : ApplicableTaxesLifeCover))));

        InstallmentPremLifeCover_InclSt = prsObj.parseXmlTag(output, "InstallmentPremLifeCover_InclSt");

        tv_bi_poorn_suraksha_yearly_premium_with_tax_first_year
                .setText(""
                        + obj.getRound(obj.getStringWithout_E(Double
                        .valueOf(InstallmentPremLifeCover_InclSt.equals("") ? "0"
                                : InstallmentPremLifeCover_InclSt))));

        TotalPremiumWithoutST_CI = prsObj.parseXmlTag(output, "TotalPremiumWithoutST_CI");

        tv_bi_poorn_suraksha_basic_premium_second_year
                .setText(""
                        + obj.getRound(obj.getStringWithout_E(Double
                        .valueOf(TotalPremiumWithoutST_CI.equals("") ? "0"
                                : TotalPremiumWithoutST_CI))));

        ServiceTax_CI = prsObj.parseXmlTag(output, "ServiceTax_CI");

        tv_bi_poorn_suraksha_service_tax_second_year
                .setText(""
                        + obj.getRound(obj.getStringWithout_E(Double
                        .valueOf(ServiceTax_CI.equals("") ? "0"
                                : ServiceTax_CI))));

        TotalPremiumWithST_CI = prsObj.parseXmlTag(output, "TotalPremiumWithST_CI");

        tv_bi_poorn_suraksha_yearly_premium_with_tax_second_year
                .setText(""
                        + obj.getRound(obj.getStringWithout_E(Double
                        .valueOf(TotalPremiumWithST_CI.equals("") ? "0"
                                : TotalPremiumWithST_CI))));


        tv_bi_poorn_suraksha_total_premium_with_life_critical_illness
                .setText(""
                        + obj.getRound(obj.getStringWithout_E(Double
                        .valueOf(TotalFinalPremium_IncST.equals("") ? "0"
                                : TotalFinalPremium_IncST))));

        poorn_suraksha_data = "SBI Life - Poorna Suraksha (UIN: 111N110V03) is a regular premium policy, for which your "
                + premium_paying_frequency
                + " premium is Rs. "
                + obj.getRound(obj.getStringWithout_E(Double
                .valueOf(TotalFinalPremium_IncST.equals("") ? "0"
                        : TotalFinalPremium_IncST)))
                + ". Your Policy Term is "
                + policy_term
                + " years and Base Sum Assured is Rs. "
                + getformatedThousandString(Integer.parseInt(obj.getRound(obj.getStringWithout_E(Double
                .valueOf((sum_assured.equals("") || sum_assured == null) ? "0"
                        : sum_assured)))));

        tv_poorn_suraksha_data
                .setText(poorn_suraksha_data);

        // Amit changes start- 23-5-2016
        // basicServiceTax = prsObj.parseXmlTag(output, "basicServiceTax");
        //basicServiceTax = prsObj.parseXmlTag(output, "servTax");

        /*tv_bi_poorn_suraksha_basic_service_tax_first_year.setText(""
                + obj.getRound(obj.getStringWithout_E(Double
                .valueOf(basicServiceTax.equals("") ? "0"
                        : basicServiceTax))));*/
        // Amit changes end- 23-5-2016


        String str_prem_freq = "";

        if (premium_paying_frequency.equalsIgnoreCase("Single")) {
            str_prem_freq = "Single";
        } else {
            str_prem_freq = "Regular/Limited";
        }

        TextView tv_Company_policy_surrender_dec = d
                .findViewById(R.id.tv_Company_policy_surrender_dec);

        String basicplustax = "";
        String company_policy_surrender_dec = "Your SBI Life Poorna suraksha (UIN: 111N110V03) is a "
                + str_prem_freq
                + " premium policy and you are required to pay one time Premium of Rs. "
                + obj.getRound(obj.getStringWithout_E(Double
                .valueOf(basicplustax.equals("") ? "0" : basicplustax)))
                + " .Your Policy Term is "
                + policy_term
                + " years"
                + " .Your Premium Paying Term is "
                + payingTerm
                + " years"
                + " and Base Sum Assured is Rs. "
                +

                getformatedThousandString(Integer.parseInt(obj.getRound(obj
                        .getStringWithout_E(Double.valueOf((sum_assured
                                .equals("") || sum_assured == null) ? "0"
                                : sum_assured)))));

        tv_Company_policy_surrender_dec.setText(company_policy_surrender_dec);

        for (int i = 1; i <= Integer.parseInt(policy_term); i++) {

            String policy_year = prsObj
                    .parseXmlTag(output, "policyYr" + i + "");
            String total_base_premium_without_tax = prsObj.parseXmlTag(output,
                    "TotalBasePremiumPaidWithoutST" + i + "");
            String death_gurantee = prsObj.parseXmlTag(output,
                    "GuaranteedDeathBenifit" + i + "");
            String critical_illness_benefit_gurantee = prsObj.parseXmlTag(
                    output, "GuaranteedCriticalillnessBenifit" + i + "");

            list_data
                    .add(new M_BI_PoornSurakshaGrid_Adpter(
                            (obj.getRound(obj.getStringWithout_E(Double
                                    .valueOf(policy_year)))) + "",
                            (obj.getRound(obj.getStringWithout_E(Double
                                    .valueOf(total_base_premium_without_tax))))
                                    + "",
                            (obj.getRound(obj.getStringWithout_E(Double
                                    .valueOf(death_gurantee)))) + "",
                            (obj.getRound(obj.getStringWithout_E(Double
                                    .valueOf(critical_illness_benefit_gurantee))))
                                    + ""));

        }
        Adapter_BI_PoornSurakshaGrid adapter = new Adapter_BI_PoornSurakshaGrid(
                BI_PoornSurakshaActivity.this, list_data);
        gv_userinfo.setAdapter(adapter);

        GridHeight gh = new GridHeight();
        gh.getheight(gv_userinfo, policy_term);

        d.show();
    }

    private void CreatePoornSurakshaPlusBIPdf() {

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
            mypath = mStorageUtils.createFileToAppSpecificDir(mContext,QuatationNumber + "BI.pdf");
            needAnalysispath = mStorageUtils.createFileToAppSpecificDir(mContext, "NA.pdf");
            newFile = mStorageUtils.createFileToAppSpecificDir(mContext, QuatationNumber + "P01.pdf");

            Rectangle rect = new Rectangle(594f, 792f);

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
            Para_Header.add(new Paragraph(
                    "Premium Quotation" + "\n" + "SBI LIFE - Poorna Suraksha (UIN No -"
                            + "111N110V03" + ")", headerBold));

            PdfPTable headertable = new PdfPTable(1);
            headertable.setWidthPercentage(100);
            PdfPCell c1 = new PdfPCell(new Phrase(Para_Header));
            c1.setBackgroundColor(BaseColor.DARK_GRAY);
            c1.setPadding(5);
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            headertable.addCell(c1);
            headertable.setHorizontalAlignment(Element.ALIGN_CENTER);
            Paragraph para_address = new Paragraph(
                    "SBI Life Insurance Co. Ltd" + "\n" + "Registered & Corporate Office: 'Natraj', M.V.Road and Western Express Highway Junction, Andheri (East), Mumbai - 400069",
                    small_bold);
            para_address.setAlignment(Element.ALIGN_CENTER);
            Paragraph para_address1 = new Paragraph(
                    "IRDAI Registration No. 111 | Website: www.sbilife.co.in | Email: info@sbilife.co.in | CIN: L99999MH2000PLC129113" + "\n" + "Toll Free: 1800 267 9090 (Between 9.00 am & 9.00 pm)",
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

           /* PdfPTable BI_Pdftable_Introdcution = new PdfPTable(1);
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
                            "The main objective of the illustration is that the client is able to appreciate the features of the product and the flow of benefits in different circumstances with some level of quantification. For further information on the product and its benefits, please refer to the sales brochure and/or policy document. Further information will also be available on request.",
                            small_normal));

            BI_Pdftable2_cell1.setPadding(5);

            BI_Pdftable2.addCell(BI_Pdftable2_cell1);
            document.add(BI_Pdftable2);*/

            /*PdfPTable BI_Pdftable3 = new PdfPTable(1);
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
                                    + "Some benefits are guaranteed and some benefits are variable with returns based on the future performance of your Insurer carrying on life insurance business. If your policy offers guaranteed returns then these will be clearly marked guaranteed in the illustration table on this page. If your policy offers variable returns then the illustrations on this page will show two different rates of assumed future investment returns. These assumed rates of return are not guaranteed and they are not the upper or lower limits of what you might get back, as the value of your policy is dependent on a number of factors including future investment performance.",
                            small_normal));

            BI_Pdftable4_cell1.setPadding(5);

            BI_Pdftable4.addCell(BI_Pdftable4_cell1);
            document.add(BI_Pdftable4);*/
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
                    new Paragraph("Gender", small_normal));
            cell_lifeAssuredAmaturityGender1.setPadding(5);
            PdfPCell cell_lifeAssuredAmaturityGender2 = new PdfPCell(
                    new Paragraph(gender, small_bold));
            cell_lifeAssuredAmaturityGender2.setPadding(5);
            cell_lifeAssuredAmaturityGender2
                    .setHorizontalAlignment(Element.ALIGN_CENTER);

            String isSmoker = "";
            if (rb_poorn_suraksha_life_assured_smoker_yes.isChecked()) {
                isSmoker = "Smoker";
            } else {
                isSmoker = "Non-Smoker";
            }
            PdfPCell cell_lifeAssured_isSmoker1 = new PdfPCell(new Paragraph(
                    "Smoking Status", small_normal));
            cell_lifeAssured_isSmoker1.setPadding(5);
            PdfPCell cell_lifeAssured_isSmoker2 = new PdfPCell(
                    new Paragraph(isSmoker, small_bold));
            cell_lifeAssured_isSmoker2.setPadding(5);
            cell_lifeAssured_isSmoker2
                    .setHorizontalAlignment(Element.ALIGN_CENTER);

            table_lifeAssuredDetails.addCell(cell_lifeAssuredAmaturityGender1);
            table_lifeAssuredDetails.addCell(cell_lifeAssuredAmaturityGender2);
            table_lifeAssuredDetails
                    .addCell(cell_lifeAssured_isSmoker1);
            table_lifeAssuredDetails
                    .addCell(cell_lifeAssured_isSmoker2);
            document.add(table_lifeAssuredDetails);

            String isStaff = "";
            if (selStaffDisc.isChecked()) {
                isStaff = "Staff";
                PdfPTable table_staff_NonStaff = new PdfPTable(2);
                table_staff_NonStaff.setWidthPercentage(100);

                PdfPCell cell_staff_NonStaff1 = new PdfPCell(new Paragraph(
                        "Staff/Non Staff", small_normal));
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
                        "Staff/Non Staff", small_normal));
                cell_staff_NonStaff1.setPadding(5);

                PdfPCell cell_staff_NonStaff2 = new PdfPCell(new Paragraph(
                        "Non Staff", small_bold));
                cell_staff_NonStaff2.setPadding(5);
                cell_staff_NonStaff2
                        .setHorizontalAlignment(Element.ALIGN_CENTER);

                table_staff_NonStaff.addCell(cell_staff_NonStaff1);
                table_staff_NonStaff.addCell(cell_staff_NonStaff2);
                document.add(table_staff_NonStaff);
            }
            /*document.add(para_img_logo_after_space_1);*/

            /*PdfPTable BI_PdftablePremiumforBasicCover = new PdfPTable(1);
            BI_PdftablePremiumforBasicCover.setWidthPercentage(100);
            PdfPCell BI_PdftablePremiumforBasicCovercell = new PdfPCell(
                    new Paragraph("Premium for Basic Cover", small_bold));

            BI_PdftablePremiumforBasicCovercell
                    .setBackgroundColor(BaseColor.LIGHT_GRAY);

            BI_PdftablePremiumforBasicCovercell
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftablePremiumforBasicCovercell.setPadding(5);

            BI_PdftablePremiumforBasicCover
                    .addCell(BI_PdftablePremiumforBasicCovercell);
            document.add(BI_PdftablePremiumforBasicCover);*/

            PdfPTable Table_policyTerm_annualisedPremium_sumAssured = new PdfPTable(
                    4);
            Table_policyTerm_annualisedPremium_sumAssured
                    .setWidthPercentage(100);

            PdfPCell cell_Term1 = new PdfPCell(new Paragraph("Policy Term (Years)",
                    small_normal));
            PdfPCell cell_Term2 = new PdfPCell(new Paragraph(policy_term + " Years", small_bold));
            cell_Term1.setPadding(5);
            cell_Term2.setPadding(5);
            cell_Term2.setHorizontalAlignment(Element.ALIGN_CENTER);

            String payingTerm = "";
            if (premium_paying_frequency.equalsIgnoreCase("Single")) {
                payingTerm = "1 Year";
            } else {
                payingTerm = policy_term + " Years";
            }

            PdfPCell cell_plan1 = new PdfPCell(new Paragraph("Basic Sum Assured (Rs.)",
                    small_normal));
            cell_plan1.setPadding(5);
            PdfPCell cell_plan2 = new PdfPCell(new Paragraph("Rs. "
                    + sum_assured, small_bold));
            cell_plan2.setPadding(5);
            cell_plan2.setHorizontalAlignment(Element.ALIGN_CENTER);






         /*   PdfPCell cell_PremiumPayingTerm1 = new PdfPCell(new Paragraph(
                    "Premium Paying Term", small_normal));
            PdfPCell cell_PremiumPayingTerm2 = new PdfPCell(new Paragraph(
                    isStaff, small_bold));
            cell_PremiumPayingTerm1.setPadding(5);
            cell_PremiumPayingTerm2.setPadding(5);
            cell_PremiumPayingTerm2
                    .setHorizontalAlignment(Element.ALIGN_CENTER);*/

            Table_policyTerm_annualisedPremium_sumAssured.addCell(cell_Term1);
            Table_policyTerm_annualisedPremium_sumAssured.addCell(cell_Term2);

            Table_policyTerm_annualisedPremium_sumAssured
                    .addCell(cell_plan1);
            Table_policyTerm_annualisedPremium_sumAssured
                    .addCell(cell_plan2);

            document.add(Table_policyTerm_annualisedPremium_sumAssured);

            PdfPTable table_plan_premium_payingTerm = new PdfPTable(2);
            table_plan_premium_payingTerm.setWidthPercentage(100);


            PdfPCell cell_lifeAssured_prem_paying_freq1 = new PdfPCell(
                    new Paragraph("Premium Payment Frequency", small_normal));
            cell_lifeAssured_prem_paying_freq1.setPadding(5);
            PdfPCell cell_lifeAssured_prem_paying_freq2 = new PdfPCell(
                    new Paragraph(premium_paying_frequency, small_bold));
            cell_lifeAssured_prem_paying_freq2.setPadding(5);
            cell_lifeAssured_prem_paying_freq2
                    .setHorizontalAlignment(Element.ALIGN_CENTER);

           /* table_plan_premium_payingTerm.addCell(cell_plan1);
            table_plan_premium_payingTerm.addCell(cell_plan2);*/

            table_plan_premium_payingTerm.addCell(cell_lifeAssured_prem_paying_freq1);
            table_plan_premium_payingTerm.addCell(cell_lifeAssured_prem_paying_freq2);
            document.add(table_plan_premium_payingTerm);

            /*PdfPTable table_prem_paying_freq = new PdfPTable(2);
            table_prem_paying_freq.setWidthPercentage(100);
            PdfPCell cell_lifeAssured_prem_paying_freq1 = new PdfPCell(
                    new Paragraph("Premium Payment Frequency", small_normal));
            cell_lifeAssured_prem_paying_freq1.setPadding(5);
            PdfPCell cell_lifeAssured_prem_paying_freq2 = new PdfPCell(
                    new Paragraph(premium_paying_frequency, small_bold));
            cell_lifeAssured_prem_paying_freq2.setPadding(5);
            cell_lifeAssured_prem_paying_freq2
                    .setHorizontalAlignment(Element.ALIGN_CENTER);

            table_prem_paying_freq.addCell(cell_lifeAssured_prem_paying_freq1);
            table_prem_paying_freq.addCell(cell_lifeAssured_prem_paying_freq2);
            document.add(table_prem_paying_freq);*/

            document.newPage();

            document.add(para_img_logo_after_space_1);

            //new changes comments starts
            /*PdfPTable BI_Pdftable_totalPremiumforBaseProduct = new PdfPTable(1);
            BI_Pdftable_totalPremiumforBaseProduct.setWidthPercentage(100);
            PdfPCell BI_Pdftable_totalPremiumforBaseProductcell = new PdfPCell(
                    new Paragraph(
                            "Total Premium for Base Product & Rider (if any) (in Rs)",
                            small_bold));

            BI_Pdftable_totalPremiumforBaseProductcell
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_Pdftable_totalPremiumforBaseProductcell.setPadding(5);

            BI_Pdftable_totalPremiumforBaseProduct
                    .addCell(BI_Pdftable_totalPremiumforBaseProductcell);
            document.add(BI_Pdftable_totalPremiumforBaseProduct);

            PdfPTable Table_AccidetnalAndParmantRider_BasicPremium_servicePremium_yearlyPremium = new PdfPTable(4);
            Table_AccidetnalAndParmantRider_BasicPremium_servicePremium_yearlyPremium
                    .setWidthPercentage(100);

            PdfPCell cell_AccidetnalAndParmantRider_BasicPremium1 = new PdfPCell(
                    new Paragraph("Basic Premium", small_normal));
            cell_AccidetnalAndParmantRider_BasicPremium1.setPadding(5);
            PdfPCell cell_AccidetnalAndParmantRider_BasicPremium2 = new PdfPCell(
                    new Paragraph("Rs ."
                            + obj.getRound(obj.getStringWithout_E(Double
                            .valueOf(basicprem.equals("") ? "0"
                                    : basicprem))), small_bold));
            cell_AccidetnalAndParmantRider_BasicPremium2.setPadding(5);
            cell_AccidetnalAndParmantRider_BasicPremium2
                    .setHorizontalAlignment(Element.ALIGN_CENTER);

            PdfPCell cell_AccidetnalAndParmantRider_ServiceTax1 = new PdfPCell(
                    new Paragraph("Applicable Tax", small_normal));
            PdfPCell cell_AccidetnalAndParmantRider_ServiceTax2 = new PdfPCell(
                    new Paragraph("Rs. "
                            + obj.getRound(obj.getStringWithout_E(Double
                            .valueOf(servicetax.equals("") ? "0"
                                    : servicetax))), small_bold));
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

            // Table_AccidetnalAndParmantRider_BasicPremium_servicePremium_yearlyPremium
            // .addCell(cell_AccidetnalAndParmantRider_YearlyPremium1);
            // Table_AccidetnalAndParmantRider_BasicPremium_servicePremium_yearlyPremium
            // .addCell(cell_AccidetnalAndParmantRider_YearlyPremium2);
            document.add(Table_AccidetnalAndParmantRider_BasicPremium_servicePremium_yearlyPremium);*/
            //new changes comments ends
            document.add(para_img_logo_after_space_1);

            /* Added By - Priyanka Warekar 26-08-2015 - Start *****/
            // PdfPTable FY_SY_premDetail_table = new PdfPTable(7);
            // FY_SY_premDetail_table.setWidths(new float[] { 5f, 5f, 5f, 5f,
            // 5f,
            // 5f, 5f });
            PdfPTable FY_SY_premDetail_table = new PdfPTable(5);
            FY_SY_premDetail_table.setWidths(new float[]{5f, 5f, 5f, 5f, 5f});
            FY_SY_premDetail_table.setWidthPercentage(100f);
            FY_SY_premDetail_table.setHorizontalAlignment(Element.ALIGN_LEFT);

            PdfPCell cell;
           /* // 1st row
            cell = new PdfPCell(
                    new Phrase(
                            "Total Premium for Life Cover & Critical Illness (in Rs.)",
                            small_bold));
            cell.setColspan(5);
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            FY_SY_premDetail_table.addCell(cell);*/

            // 2 row
            cell = new PdfPCell(new Phrase("Cover Details", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            FY_SY_premDetail_table.addCell(cell);

            cell = new PdfPCell(
                    new Phrase(premium_paying_frequency + " Premium, excluding applicable taxes(in Rs.)", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            FY_SY_premDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase("Applicable taxes (in Rs.)", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            FY_SY_premDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase(premium_paying_frequency + " Premium, including applicable taxes(in Rs.)", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            FY_SY_premDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase(premium_paying_frequency + " Premium Payable, including applicable taxes (in Rs.)", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            FY_SY_premDetail_table.addCell(cell);

            // System.out.println("Plan "+plan.substring(6, 8));
            // 3 row
            cell = new PdfPCell(new Phrase("Life Cover", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            FY_SY_premDetail_table.addCell(cell);

            //life cover 2
            cell = new PdfPCell(new Phrase(
                    obj.getRound(obj.getStringWithout_E(Double.valueOf(InstallmentPremLifeCover_ExclSt
                            .equals("") ? "0" : InstallmentPremLifeCover_ExclSt))),
                    small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            FY_SY_premDetail_table.addCell(cell);

            //life cover 3
            cell = new PdfPCell(new Phrase(obj.getRound(obj.getStringWithout_E(Double.valueOf(ApplicableTaxesLifeCover
                    .equals("") ? "0" : ApplicableTaxesLifeCover))), small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            FY_SY_premDetail_table.addCell(cell);

            //life cover 4
            cell = new PdfPCell(new Phrase(
                    obj.getRound(obj.getStringWithout_E(Double
                            .valueOf(InstallmentPremLifeCover_InclSt.equals("") ? "0"
                                    : InstallmentPremLifeCover_InclSt))),
                    small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            FY_SY_premDetail_table.addCell(cell);

            /*cell = new PdfPCell(new Phrase("", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            FY_SY_premDetail_table.addCell(cell)*/

            cell = new PdfPCell(new Phrase(
                    obj.getRound(obj.getStringWithout_E(Double
                            .valueOf(TotalFinalPremium_IncST.equals("") ? "0"
                                    : TotalFinalPremium_IncST))), small_normal));
            cell.setRowspan(2);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            FY_SY_premDetail_table.addCell(cell);

            // 4 row
            cell = new PdfPCell(new Phrase("Critical Illness Cover", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            FY_SY_premDetail_table.addCell(cell);

            //CI cover 2
            cell = new PdfPCell(new Phrase(obj.getRound(obj.getStringWithout_E(Double
                    .valueOf(TotalPremiumWithoutST_CI.equals("") ? "0"
                            : TotalPremiumWithoutST_CI))), small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            FY_SY_premDetail_table.addCell(cell);

            //CI cover 3
            cell = new PdfPCell(new Phrase(obj.getRound(obj.getStringWithout_E(Double
                    .valueOf(ServiceTax_CI.equals("") ? "0"
                            : ServiceTax_CI))), small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            FY_SY_premDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase(
                    obj.getRound(obj.getStringWithout_E(Double
                            .valueOf(TotalPremiumWithST_CI.equals("") ? "0"
                                    : TotalPremiumWithST_CI))), small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            FY_SY_premDetail_table.addCell(cell);


            document.add(FY_SY_premDetail_table);
            document.add(para_img_logo_after_space_1);

            // cell = new PdfPCell(new Phrase(obj.getRound(obj
            // .getStringWithout_E(Double.valueOf(basicServiceTax
            // .equals("") ? "0" : basicServiceTax))),
            // small_normal));
            // cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            // FY_SY_premDetail_table.addCell(cell);

            // cell = new PdfPCell(new Phrase(
            // obj.getRound(obj.getStringWithout_E(Double
            // .valueOf(SBCServiceTax.equals("") ? "0"
            // : SBCServiceTax))), small_normal));
            // cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            // FY_SY_premDetail_table.addCell(cell);
            //
            // cell = new PdfPCell(new Phrase(
            // obj.getRound(obj.getStringWithout_E(Double
            // .valueOf(KKCServiceTax.equals("") ? "0"
            // : KKCServiceTax))), small_normal));
            // cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            // FY_SY_premDetail_table.addCell(cell);

            //new changes starts
            PdfPTable BI_Pdftable19 = new PdfPTable(1);
            BI_Pdftable19.setWidthPercentage(100);
            PdfPCell BI_Pdftable19_cell1 = new PdfPCell(new Paragraph(
                    "Premium Quotation FOR SBI LIFE - Poorna Suraksha",
                    small_bold));
            BI_Pdftable19_cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_Pdftable19_cell1.setPadding(5);

            BI_Pdftable19.addCell(BI_Pdftable19_cell1);
            document.add(BI_Pdftable19);

            PdfPTable Table_BI_Header = new PdfPTable(3);
            float[] columnWidthsBI_Header = {5f, 5f, 10f};
            Table_BI_Header.setWidthPercentage(100);
            Table_BI_Header.setWidths(columnWidthsBI_Header);
            PdfPCell cell_EndOfYear = new PdfPCell(new Paragraph("Policy Year",
                    small_bold2));
            cell_EndOfYear.setPadding(5);
            cell_EndOfYear.setHorizontalAlignment(Element.ALIGN_CENTER);

            PdfPCell cell_TotalBasePremium = new PdfPCell(new Paragraph(
                    "Total Base Premium Paid without Applicable taxes (in Rs.)", small_bold2));
            cell_TotalBasePremium.setPadding(5);
            cell_TotalBasePremium.setHorizontalAlignment(Element.ALIGN_CENTER);

            PdfPCell cell_BenefitPayableOnDeath = new PdfPCell(new Paragraph(
                    "Benefits (Rs.)", small_bold2));
            cell_BenefitPayableOnDeath.setPadding(5);
            cell_BenefitPayableOnDeath
                    .setHorizontalAlignment(Element.ALIGN_CENTER);

            Table_BI_Header.addCell(cell_EndOfYear);
            Table_BI_Header.addCell(cell_TotalBasePremium);
            Table_BI_Header.addCell(cell_BenefitPayableOnDeath);

            document.add(Table_BI_Header);

            PdfPTable Table_BI_Header1 = new PdfPTable(4);
            float[] columnWidthsBI_Header1 = {5f, 5f, 5f, 5f};

            Table_BI_Header1.setWidthPercentage(100);
            Table_BI_Header1.setWidths(columnWidthsBI_Header1);
            PdfPCell cell_EndOfYear1 = new PdfPCell(new Paragraph("",
                    small_bold2));
            cell_EndOfYear1.setPadding(5);
            cell_EndOfYear1.setHorizontalAlignment(Element.ALIGN_CENTER);

            PdfPCell cell_TotalBasePremium2 = new PdfPCell(new Paragraph("",
                    small_bold2));
            cell_TotalBasePremium2.setPadding(5);
            cell_TotalBasePremium2.setHorizontalAlignment(Element.ALIGN_CENTER);

            PdfPCell cell_BenefitPayableOnDeath_Guaranteed = new PdfPCell(
                    new Paragraph(
                            "Benefit Payable on death i.e. Life Cover Sum Assured*",
                            small_bold2));
            cell_BenefitPayableOnDeath_Guaranteed.setPadding(5);
            cell_BenefitPayableOnDeath_Guaranteed
                    .setHorizontalAlignment(Element.ALIGN_CENTER);

            PdfPCell cell_MaturityBenefit2_Guaranteed = new PdfPCell(
                    new Paragraph(
                            "Benefit Payable on Critical Illness(CI) i.e. Critical Ilness Sum Assured",
                            small_bold2));

            cell_MaturityBenefit2_Guaranteed.setPadding(5);
            cell_MaturityBenefit2_Guaranteed
                    .setHorizontalAlignment(Element.ALIGN_CENTER);

            Table_BI_Header1.addCell(cell_EndOfYear1);
            Table_BI_Header1.addCell(cell_TotalBasePremium2);
            Table_BI_Header1.addCell(cell_BenefitPayableOnDeath_Guaranteed);
            Table_BI_Header1.addCell(cell_MaturityBenefit2_Guaranteed);

            document.add(Table_BI_Header1);

            for (int i = 0; i < list_data.size(); i++) {
                PdfPTable Table_BI_Header2 = new PdfPTable(4);

                Table_BI_Header2.setWidthPercentage(100);
                Table_BI_Header2.setWidths(columnWidthsBI_Header1);
                PdfPCell cell_EndOfYear3 = new PdfPCell(new Paragraph(list_data
                        .get(i).getPolicy_year(), small_bold2));
                cell_EndOfYear3.setPadding(5);
                cell_EndOfYear3.setHorizontalAlignment(Element.ALIGN_CENTER);

                PdfPCell cell_TotalBasePremium3 = new PdfPCell(new Paragraph(
                        list_data.get(i).getTotal_base_premium_without_tax(),
                        small_bold2));
                cell_TotalBasePremium3.setPadding(5);
                cell_TotalBasePremium3
                        .setHorizontalAlignment(Element.ALIGN_CENTER);

                PdfPCell cell_BenefitPayableOnDeath3_Guaranteed = new PdfPCell(
                        new Paragraph(list_data.get(i).getDeath_gurantee(),
                                small_bold2));
                cell_BenefitPayableOnDeath3_Guaranteed.setPadding(5);
                cell_BenefitPayableOnDeath3_Guaranteed
                        .setHorizontalAlignment(Element.ALIGN_CENTER);

                PdfPCell cell_MaturityBenefit3_Guaranteed = new PdfPCell(
                        new Paragraph(list_data.get(i)
                                .getcritical_illness_benefit_gurantee(),
                                small_bold2));

                cell_MaturityBenefit3_Guaranteed.setPadding(5);
                cell_MaturityBenefit3_Guaranteed
                        .setHorizontalAlignment(Element.ALIGN_CENTER);

                Table_BI_Header2.addCell(cell_EndOfYear3);
                Table_BI_Header2.addCell(cell_TotalBasePremium3);
                Table_BI_Header2
                        .addCell(cell_BenefitPayableOnDeath3_Guaranteed);
                Table_BI_Header2.addCell(cell_MaturityBenefit3_Guaranteed);
                document.add(Table_BI_Header2);
            }

            /*Paragraph para_note = new Paragraph(
                    "**Benefit payable to the nominee on death", small_normal);
            document.add(para_note);
            document.add(para_img_logo_after_space_1);*/

            /* Modified by Pranprit Gill on 23/10/2017 */

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
                            "1. In case of monthly mode, upto 3 months’ premiums have to be paid in advance and renewal premium payment through Electronic Clearing System (ECS) or Standing Instructions (The premiums can be paid by giving standing instructions to your bank or you can pay through your credit card)."

                            , small_normal));

            BI_Pdftable6_cell6.setPadding(5);

            BI_Pdftable6.addCell(BI_Pdftable6_cell6);
            document.add(BI_Pdftable6);

            PdfPTable BI_Pdftable7 = new PdfPTable(1);
            BI_Pdftable7.setWidthPercentage(100);
            PdfPCell BI_Pdftable7_cell1 = new PdfPCell(
                    new Paragraph(
                            "2. For Monthly Salary Saving Scheme (SSS),upto 2 month premium to be paid in advance and renewal premium payment is allowed only through Salary Deduction.",
                            small_normal));

            BI_Pdftable7_cell1.setPadding(5);

            BI_Pdftable7.addCell(BI_Pdftable7_cell1);
            document.add(BI_Pdftable7);

            PdfPTable BI_Pdftable8 = new PdfPTable(1);
            BI_Pdftable8.setWidthPercentage(100);
            PdfPCell BI_Pdftable8_cell1 = new PdfPCell(
                    new Paragraph(
                            "*Death Benefit will be higher of Effective Life Cover Sum Assured (OR) 10 times the Annualised premium (OR) 105% of the total premiums received up to the date of death.",
                            small_normal));

            BI_Pdftable8_cell1.setPadding(5);

            BI_Pdftable8.addCell(BI_Pdftable8_cell1);
            document.add(BI_Pdftable8);

            PdfPTable BI_Pdftable9 = new PdfPTable(1);
            BI_Pdftable9.setWidthPercentage(100);
            PdfPCell BI_Pdftable9_cell1 = new PdfPCell(
                    new Paragraph(
                            "Annualised Premium means the premium amount payable in a year as chosen by the policy holder, excluding the applicable taxes, rider premiums(if any),the underwriting extra premiums and loadings for modal premiums, if any",
                            small_normal));

            BI_Pdftable9_cell1.setPadding(5);

            BI_Pdftable9.addCell(BI_Pdftable9_cell1);
            document.add(BI_Pdftable9);

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
            document.add(BI_Pdftable_OtherTermCondition);

            PdfPTable BI_PdftableOtherTermCondition1 = new PdfPTable(1);
            BI_PdftableOtherTermCondition1.setWidthPercentage(100);
            PdfPCell BI_PdftableOtherTermCondition1_cell6 = new PdfPCell(
                    new Paragraph(
                            "1. The premium calculation is based on the age herein indicated and as applicable for healthy individual."

                            , small_normal));

            BI_PdftableOtherTermCondition1_cell6.setPadding(5);

            BI_PdftableOtherTermCondition1
                    .addCell(BI_PdftableOtherTermCondition1_cell6);
            document.add(BI_PdftableOtherTermCondition1);

            PdfPTable BI_PdftableOtherTermCondition2 = new PdfPTable(1);
            BI_PdftableOtherTermCondition2.setWidthPercentage(100);
            PdfPCell BI_PdftableOtherTermCondition2_cell1 = new PdfPCell(
                    new Paragraph(
                            "2. The Death Benefit amount is derived on the assumption that the policy is in �full force�.",
                            small_normal));

            BI_PdftableOtherTermCondition2_cell1.setPadding(5);

            BI_PdftableOtherTermCondition2
                    .addCell(BI_PdftableOtherTermCondition2_cell1);
            document.add(BI_PdftableOtherTermCondition2);

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
                            "3. There is no Maturity Benefit and Surrender Value under this product.",
                            small_bold));

            BI_PdftableOtherTermCondition4_cell1.setPadding(5);

            BI_PdftableOtherTermCondition4
                    .addCell(BI_PdftableOtherTermCondition4_cell1);
            document.add(BI_PdftableOtherTermCondition4);

            PdfPTable BI_PdftableOtherTermCondition5 = new PdfPTable(1);
            BI_PdftableOtherTermCondition5.setWidthPercentage(100);
            PdfPCell BI_PdftableOtherTermCondition5_cell1 = new PdfPCell(
                    new Paragraph(
                            "4. The CI Benefit is the effective Critical Illness sum assured for the corresponding year, as on diagnosis of one or more covered critical illnesses.",
                            small_normal));

            BI_PdftableOtherTermCondition5_cell1.setPadding(5);

            BI_PdftableOtherTermCondition5
                    .addCell(BI_PdftableOtherTermCondition5_cell1);
            document.add(BI_PdftableOtherTermCondition5);

            PdfPTable BI_PdftableOtherTermCondition6 = new PdfPTable(1);
            BI_PdftableOtherTermCondition6.setWidthPercentage(100);
            PdfPCell BI_PdftableOtherTermCondition6_cell1 = new PdfPCell(
                    new Paragraph(
                            "5. Once the CI claim is paid, the policy will continue till the end of the policy term with no further change in life cover sum assured. Future premiums would be waived on payment of CI claim.",
                            small_normal));

            BI_PdftableOtherTermCondition6_cell1.setPadding(5);

            BI_PdftableOtherTermCondition6
                    .addCell(BI_PdftableOtherTermCondition6_cell1);
            document.add(BI_PdftableOtherTermCondition6);

            PdfPTable BI_PdftableOtherTermCondition7 = new PdfPTable(1);
            BI_PdftableOtherTermCondition7.setWidthPercentage(100);
            PdfPCell BI_PdftableOtherTermCondition7_cell1 = new PdfPCell(
                    new Paragraph(
                            "6. Survival Period of 14 days would be applicable from the date of diagnosis of a CI condition.",
                            small_normal));

            BI_PdftableOtherTermCondition7_cell1.setPadding(5);

            BI_PdftableOtherTermCondition7
                    .addCell(BI_PdftableOtherTermCondition7_cell1);
            document.add(BI_PdftableOtherTermCondition7);

            PdfPTable BI_PdftableOtherTermCondition8 = new PdfPTable(1);
            BI_PdftableOtherTermCondition8.setWidthPercentage(100);
            PdfPCell BI_PdftableOtherTermCondition8_cell1 = new PdfPCell(
                    new Paragraph(
                            "7.  The exact premium can be determined only at the time of acceptance of risk cover on the life to be assured after taking into consideration any extras required to be imposed.",
                            small_normal));

            BI_PdftableOtherTermCondition8_cell1.setPadding(5);

            BI_PdftableOtherTermCondition8
                    .addCell(BI_PdftableOtherTermCondition8_cell1);
            document.add(BI_PdftableOtherTermCondition8);

            PdfPTable BI_PdftableOtherTermCondition9 = new PdfPTable(1);
            BI_PdftableOtherTermCondition9.setWidthPercentage(100);
            PdfPCell BI_PdftableOtherTermCondition9_cell1 = new PdfPCell(
                    new Paragraph(
                            "8. Applicable taxes and/or any other statutory levy/ duty/ surcharge, at the rate notified by the Central Government/ State Government / Union Territories of India from time to time, as per the provisions of the prevalent tax laws will be payable on basic premium and any other charge as per the product features.",
                            small_normal));

            BI_PdftableOtherTermCondition9_cell1.setPadding(5);

            BI_PdftableOtherTermCondition9
                    .addCell(BI_PdftableOtherTermCondition9_cell1);
            document.add(BI_PdftableOtherTermCondition9);

            document.add(para_img_logo_after_space_1);

            PdfPTable BI_Pdftable_CompanysPolicySurrender2 = new PdfPTable(1);
            BI_Pdftable_CompanysPolicySurrender2.setWidthPercentage(100);
            PdfPCell BI_Pdftable_CompanysPolicySurrender2_cell = new PdfPCell(
                    new Paragraph(
                            "You may receive a Welcome Call from our representative to confirm your proposal details like Date of Birth, Nominee Name, Address, Email ID, Sum Assured, Premium amount, Policy Term etc. You may have to undergo Medical tests based on our underwriting requirements.",
                            small_normal));

            BI_Pdftable_CompanysPolicySurrender2_cell.setPadding(5);

            BI_Pdftable_CompanysPolicySurrender2
                    .addCell(BI_Pdftable_CompanysPolicySurrender2_cell);
            document.add(BI_Pdftable_CompanysPolicySurrender2);

            PdfPTable BI_Pdftable_CompanysPolicySurrender5 = new PdfPTable(1);
            BI_Pdftable_CompanysPolicySurrender5.setWidthPercentage(100);
            PdfPCell BI_Pdftable_CompanysPolicySurrender5_cell = new PdfPCell(
                    new Paragraph(poorn_suraksha_data, small_normal));

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
                                    + "     ,having received the information with respect to the above, have understood the above statement before entering into the contract.",
                            small_bold));

            BI_Pdftable26_cell1.setPadding(5);

            BI_Pdftable26.addCell(BI_Pdftable26_cell1);
            document.add(BI_Pdftable26);
            document.add(BI_Pdftable26_cell1);

            /*PdfPTable BI_Pdftable_eSign = new PdfPTable(1);
            BI_Pdftable_eSign.setWidthPercentage(100);
            // CR_table6.setWidths(columnWidths_26);
            PdfPCell BI_Pdftable_eSign_cell1 = new PdfPCell(new Paragraph(

                    "This document is eSigned by " + name_of_person, small_bold));

            BI_Pdftable_eSign_cell1.setPadding(5);

            BI_Pdftable_eSign.addCell(BI_Pdftable_eSign_cell1);
            document.add(BI_Pdftable_eSign);
            document.add(para_img_logo_after_space_1);*/
            document.add(para_img_logo_after_space_1);

            if (!bankUserType.equalsIgnoreCase("Y")) {
                PdfPTable BI_PdftablePolicyHolder = new PdfPTable(1);
                BI_PdftablePolicyHolder.setWidthPercentage(100);
                PdfPCell BI_PdftablePolicyHolder_signature_cell = new PdfPCell(
                        new Paragraph("Signature of the Prospect/Policyholder:", small_bold));

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


    // Store User input in Bean Object
    private void addListenerOnSubmit() {
        // TODO Auto-generated method stub
        poornSurakshaBean = new PoornSurakshaBean();

        // Input from GUI[Basic Cover]
        if (selStaffDisc.isChecked()) {
            poornSurakshaBean.setIsForStaffOrNot(true);
            staffStatus = "sbi";
        } else {
            poornSurakshaBean.setIsForStaffOrNot(false);
            staffStatus = "none";
        }
        if (cb_kerladisc.isChecked()) {
            poornSurakshaBean.setKerlaDisc(true);
            poornSurakshaBean.setServiceTax(true);
        } else {
            poornSurakshaBean.setServiceTax(false);
            poornSurakshaBean.setKerlaDisc(false);
        }
        if (rb_poorn_suraksha_life_assured_smoker_yes.isChecked()) {
            poornSurakshaBean.setSmoker("Smoker");
            isSmoker = "Y";
        } else if (rb_poorn_suraksha_life_assured_smoker_no.isChecked()) {
            poornSurakshaBean.setSmoker("Non-Smoker");
            isSmoker = "N";
        }

        // System.out.println("* 1 *");
        poornSurakshaBean.setAge(Integer.parseInt(ageInYears.getSelectedItem()
                .toString()));

        // System.out.println("* 1.1 *");
        poornSurakshaBean.setGender(selGender.getSelectedItem().toString());
        // System.out.println("* 1.1 *"+selGender.getSelectedItem().toString());
        poornSurakshaBean.setPolicyterm(Integer.parseInt(selBasicTerm
                .getSelectedItem().toString()));
        // System.out.println("* 1.2 *"+Integer.parseInt(selBasicTerm.getSelectedItem().toString()));
        poornSurakshaBean
                .setPremiumFrequency(selPremFreq.getSelectedItem().toString());
        // System.out.println("* 1.3 *");
        poornSurakshaBean.setSumAssured(Integer.parseInt(basicSA.getText()
                .toString()));
        // System.out.println("* 1.3 *"+Integer.parseInt(basicSA.getText().toString()));

        // Show Output Form
        showPoornaSurakshaOutputPg(poornSurakshaBean);
    }


    /************************* validation starts here ********************************************/

    private void getInput(PoornSurakshaBean poornSurakshaBean) {

        inputVal = new StringBuilder();
        // From GUI Input
        boolean staffDisc = poornSurakshaBean.getIsForStaffOrNot();
        String isSmoker = poornSurakshaBean.getSmoker();
        int policyTerm = poornSurakshaBean.getPolicyterm();
        String premFreq = poornSurakshaBean.getPremiumFrequency();
        int sumAssured = (int) poornSurakshaBean.getSumAssured();


        String LifeAssured_title = spnr_bi_poorn_suraksha_life_assured_title
                .getSelectedItem().toString();
        String LifeAssured_firstName = edt_bi_poorn_suraksha_life_assured_first_name
                .getText().toString();
        String LifeAssured_middleName = edt_bi_poorn_suraksha_life_assured_middle_name
                .getText().toString();
        String LifeAssured_lastName = edt_bi_poorn_suraksha_life_assured_last_name
                .getText().toString();
        String LifeAssured_DOB = btn_bi_poorn_suraksha_life_assured_date.getText().toString();

        String proposer_title = "";
        String proposer_firstName = "";
        String proposer_middleName = "";
        String proposer_lastName = "";
        String proposer_DOB = "";
        String proposer_age = "";
        String proposer_gender = "";
        int premPayingterm = poornSurakshaBean.getPolicyterm();
        String plantype = "";

        inputVal.append("<?xml version='1.0' encoding='utf-8' ?><PoornaSuraksha>");
        inputVal.append("<LifeAssured_title>").append(LifeAssured_title).append("</LifeAssured_title>");
        inputVal.append("<LifeAssured_firstName>").append(LifeAssured_firstName).append("</LifeAssured_firstName>");
        inputVal.append("<LifeAssured_middleName>").append(LifeAssured_middleName).append("</LifeAssured_middleName>");
        inputVal.append("<LifeAssured_lastName>").append(LifeAssured_lastName).append("</LifeAssured_lastName>");
        inputVal.append("<LifeAssured_DOB>").append(LifeAssured_DOB).append("</LifeAssured_DOB>");
        inputVal.append("<LifeAssured_age>").append(ageInYears.getSelectedItem().toString()).append("</LifeAssured_age>");
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
        inputVal.append("<isSmoker>").append(isSmoker).append("</isSmoker>");

        inputVal.append("<age>").append(ageInYears.getSelectedItem().toString()).append("</age>");
        inputVal.append("<policyTerm>").append(policyTerm).append("</policyTerm>");
        inputVal.append("<premPayTerm>").append(premPayingterm).append("</premPayTerm>");
        inputVal.append("<premFreq>").append(premFreq).append("</premFreq>");
        inputVal.append("<sumAssured>").append(sumAssured).append("</sumAssured>");
        inputVal.append("<plan>").append(plantype).append("</plan>");

        inputVal.append("<Product_Cat>").append(product_cateogory).append("</Product_Cat>");

        //Added Tushar Kadam Kerla Applicable Tax 7-Jun-2019
        String str_kerla_discount = "N";
        if (cb_kerladisc.isChecked()) {
            str_kerla_discount = "Y";
        }

        inputVal.append("<KFC>" + str_kerla_discount + "</KFC>");
        //End Added Tushar Kadam Kerla Applicable Tax 7-Jun-2019

        inputVal.append("</PoornaSuraksha>");
    }

    // Validate Minimum premium
    private String valInstPremium(String premiumSingleInstBasic, String premFreq) {
        String error = "";

        if (premFreq.equals("Yearly") &&
                (Integer.parseInt(premiumSingleInstBasic) < 3000)) {
            error =
                    "Minimum Premium (without applicable taxes (if any) on charges ) for Yearly mode under this product is Rs. 3,000 ; Increase your Sum Assured";

        } else if (premFreq.equals("Half-Yearly") &&
                (Integer.parseInt(premiumSingleInstBasic) < 1500)) {
            error =
                    "Minimum Premium (without applicable taxes (if any) on charges ) for Half- Yearly mode under this product is Rs. 1,500 ; Increase your Sum Assured";
        }
        if (premFreq.equals("Quarterly") &&
                (Integer.parseInt(premiumSingleInstBasic) < 1500)) {
            error = "Minimum premium for Quarterly Mode under this product is Rs. 1500";
        } else if (premFreq.contains("Monthly") &&
                (Integer.parseInt(premiumSingleInstBasic) < 250)) {
            error =
                    "Minimum Premium (without applicable taxes (if any) on charges ) for Monthly mode under this product is Rs. 250 ; Increase your Sum Assured";

        }
        return error;
    }

    private void showPoornaSurakshaOutputPg(PoornSurakshaBean poornsurakshaBean) {
        String[] outputArr = getOutput(
                poornsurakshaBean);
        CommonForAllProd commonForAllProd = new CommonForAllProd();

        PoornSurakshaBusinessLogic poornSurakshaBussinesLogic = new PoornSurakshaBusinessLogic(
                poornSurakshaBean);

        if (proposer_isSmoker.equalsIgnoreCase("Smoker")) {
            isSmoker = "Y";
        } else if (proposer_isSmoker.equalsIgnoreCase("Non-Smoker")) {
            isSmoker = "N";
        }

        if (selStaffDisc.isChecked()) {

            staffStatus = "sbi";

        } else {
            staffStatus = "none";
        }


        try {

            retVal.append("<?xml version='1.0' encoding='utf-8' ?><PoornaSuraksha>");
            retVal.append("<errCode>0</errCode>");
            retVal.append("<staffStatus>").append(staffStatus).append("</staffStatus>");
            retVal.append("<staffRebate>").append(discountPercentage).append("</staffRebate>");
            retVal.append("<critiSumAssured>").append(critiSumAssured).append("</critiSumAssured>");
            retVal.append("<smokerOrNot>").append(isSmoker).append("</smokerOrNot>");
            retVal.append("<basicPrem>").append(PremiumBeforeST).append("</basicPrem>").append("<installmntPremWithoutSerTx>").append(commonForAllProd.getRoundUp(obj.getStringWithout_E(TotalFinalPremium_ExclST))).append("</installmntPremWithoutSerTx>").append("<installmntPremWithSerTx>").append(TotalFinalPremium_IncST).append("</installmntPremWithSerTx>").append("<servTax>").append(commonForAllProd.getRoundUp(commonForAllProd.getRoundOffLevel2New_Saral(TotalApplicableTaxes))).append("</servTax>").append("<PremiumBeforeST_CI>").append(PremiumBeforeST_CI).append("</PremiumBeforeST_CI>").append("<TotalPremiumWithoutST_CI>").append(TotalPremiumWithoutST_CI).append("</TotalPremiumWithoutST_CI>").append("<TotalPremiumWithST_CI>").append(TotalPremiumWithST_CI).append("</TotalPremiumWithST_CI>").append("<ServiceTax_CI>").append(ServiceTax_CI).append("</ServiceTax_CI>").append("<InstallmentPremLifeCover_ExclSt>").append(InstallmentPremLifeCover_ExclSt).append("</InstallmentPremLifeCover_ExclSt>").append("<ApplicableTaxesLifeCover>").append(ApplicableTaxesLifeCover).append("</ApplicableTaxesLifeCover>").append("<InstallmentPremLifeCover_InclSt>").append(InstallmentPremLifeCover_InclSt).append("</InstallmentPremLifeCover_InclSt>").append("<TotalFinalPremium_IncST>").append(TotalFinalPremium_IncST).append("</TotalFinalPremium_IncST>").append("<basicPremWithoutDisc>").append(totInstPrem_exclST_exclDisc).append("</basicPremWithoutDisc>").append("<basicPremWithoutDiscSA>").append(totInstPrem_exclST_exclDisc_exclFreqLoading).append("</basicPremWithoutDiscSA>").append("<CritiPremWithoutDisc>").append(riderPrem_exclST_exclDisc).append("</CritiPremWithoutDisc>").append("<CritiPremWithoutDiscSA>").append(riderPrem_exclST_exclDisc_exclFreqLoading).append("</CritiPremWithoutDiscSA>").append("<SumAssuredBasic80>").append(SumAssured_basic_80).append("</SumAssuredBasic80>").append("<MinesOccuInterest>").append(MinesOccuInterest).append("</MinesOccuInterest>").append("<OccuInt>").append(MinesOccuInterest).append("</OccuInt>");
            retVal.append("<totalBasicPremWithoutDisc>").append(totalInstPrem_exclST_exclDisc).append("</totalBasicPremWithoutDisc>");
            retVal.append(bussIll.toString());
            retVal.append("</PoornaSuraksha>");
            System.out.println("output " + retVal.toString());

        } catch (Exception e) {
            retVal.append("<?xml version='1.0' encoding='utf-8' ?><PoornaSuraksha>" + "<errCode>1</errCode>" + "<errorMessage>").append(e.getMessage()).append("</errorMessage></PoornaSuraksha>");
        }
        //new changes end
    }

    private String[] getOutput(PoornSurakshaBean poornsurakshaBean) {

        CommonForAllProd commonForAllProd = new CommonForAllProd();

        PoornSurakshaBusinessLogic poornSurakshaBussinesLogic = new PoornSurakshaBusinessLogic(
                poornSurakshaBean);
        bussIll = new StringBuilder();
        int limit = poornsurakshaBean.getPolicyterm();

        critiSumAssured = commonForAllProd
                .getStringWithout_E(poornSurakshaBussinesLogic
                        .getCirtiSumAssured(poornsurakshaBean.getSumAssured()));
        discountPercentage = poornSurakshaBussinesLogic
                .getStaffRebate(selStaffDisc.isChecked()) + "";

        PremiumRate = poornSurakshaBussinesLogic.getPremiumRate(poornsurakshaBean.getGender(), poornsurakshaBean.getSmoker(), poornsurakshaBean.getAge(), poornsurakshaBean.getPolicyterm());
        StaffRebate = poornSurakshaBussinesLogic.getStaffRebate(poornsurakshaBean.getIsForStaffOrNot());
        SARebate = poornSurakshaBussinesLogic.getSARebate(poornsurakshaBean.getSumAssured());
        LoadingFrequencyPremium = poornSurakshaBussinesLogic.getLoadingFrequencyPremium(poornsurakshaBean.getPremiumFrequency());
        PremiumBeforeST = commonForAllProd.getRoundOffLevel2New(commonForAllProd.getStringWithout_E(poornSurakshaBussinesLogic.getPremiumBeforeST(poornsurakshaBean.getSumAssured())));

//		  TotalPremiumWithoutST =poornasurakshabusinesslogic.getTotalPremiumWithoutST() ;
//		   ServiceTax =commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E( poornasurakshabusinesslogic.getServiceTax(Double.parseDouble(TotalPremiumWithoutST)))) ;
//		   TotalPremiumWithST = commonForAllProd.getStringWithout_E(Double.parseDouble(TotalPremiumWithoutST)+Double.parseDouble(ServiceTax));
//
        double SumAssured_CI = poornSurakshaBussinesLogic.getSumAssured_CI(poornsurakshaBean.getSumAssured());
        double PremiumRate_CI = poornSurakshaBussinesLogic.getCriticalIllnessRate(poornsurakshaBean.getGender(), poornsurakshaBean.getSmoker(), poornsurakshaBean.getAge(), poornsurakshaBean.getPolicyterm());
        PremiumBeforeST_CI = commonForAllProd.getRoundOffLevel2New(commonForAllProd.getStringWithout_E(poornSurakshaBussinesLogic.getPremiumBeforeST_CI(SumAssured_CI)));
        TotalPremiumWithoutST_CI = commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(Double.parseDouble(poornSurakshaBussinesLogic.getTotalPremiumWithoutST_CI())));
        //ServiceTax_CI = commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(poornSurakshaBussinesLogic.getServiceTax_CI()));
        ServiceTax_CI = commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(poornSurakshaBussinesLogic.getServiceTax_CI(poornsurakshaBean.getServiceTax())));
        TotalPremiumWithST_CI = commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(Double.parseDouble(TotalPremiumWithoutST_CI) + Double.parseDouble(ServiceTax_CI)));


        InstallmentPremLifeCover_ExclSt = commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(Double.parseDouble(PremiumBeforeST) - Double.parseDouble(PremiumBeforeST_CI)));
        //ApplicableTaxesLifeCover = commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(poornSurakshaBussinesLogic.getServiceTax(Double.parseDouble(InstallmentPremLifeCover_ExclSt))));
        ApplicableTaxesLifeCover = commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(poornSurakshaBussinesLogic.getServiceTax(Double.parseDouble(InstallmentPremLifeCover_ExclSt), poornsurakshaBean.getServiceTax())));
        InstallmentPremLifeCover_InclSt = commonForAllProd.getRoundOffLevel2New(commonForAllProd.getStringWithout_E(Double.parseDouble(InstallmentPremLifeCover_ExclSt) + Double.parseDouble(ApplicableTaxesLifeCover)));

        TotalFinalPremium_ExclST = Double.parseDouble(InstallmentPremLifeCover_ExclSt) + Double.parseDouble(TotalPremiumWithoutST_CI);
        TotalApplicableTaxes = commonForAllProd.getStringWithout_E(Double.parseDouble(ApplicableTaxesLifeCover) + Double.parseDouble(ServiceTax_CI));
        TotalFinalPremium_IncST = commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(Double.parseDouble(InstallmentPremLifeCover_InclSt) + Double.parseDouble(TotalPremiumWithST_CI)));


//		PremiumRate = poornSurakshaBussinesLogic.getPremiumRate(poornsurakshaBean.getGender(), poornsurakshaBean.getSmoker(), poornsurakshaBean.getAge(), poornsurakshaBean.getPolicyterm());
//		StaffRebate = poornSurakshaBussinesLogic.getStaffRebate(poornsurakshaBean.getIsForStaffOrNot()) ;
//		SARebate = poornSurakshaBussinesLogic.getSARebate(poornsurakshaBean.getSumAssured());
//		LoadingFrequencyPremium = poornSurakshaBussinesLogic.getLoadingFrequencyPremium(poornsurakshaBean.getPremiumFrequency()) ;
//		PremiumBeforeST = commonForAllProd.getRoundOffLevel2New(commonForAllProd.getStringWithout_E(poornSurakshaBussinesLogic.getPremiumBeforeST(poornsurakshaBean.getSumAssured()))) ;
//		TotalPremiumWithoutST =poornSurakshaBussinesLogic.getTotalPremiumWithoutST() ;
//		ServiceTax =commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E( poornSurakshaBussinesLogic.getServiceTax())) ;
//		TotalPremiumWithST = TotalPremiumWithoutST+ServiceTax;
//
//		SumAssured_CI = poornSurakshaBussinesLogic.getSumAssured_CI(poornsurakshaBean.getSumAssured());
//		PremiumRate_CI = poornSurakshaBussinesLogic.getCriticalIllnessRate(poornsurakshaBean.getGender(), poornsurakshaBean.getSmoker(), poornsurakshaBean.getAge(), poornsurakshaBean.getPolicyterm());
//		PremiumBeforeST_CI =commonForAllProd.getRoundOffLevel2New(commonForAllProd.getStringWithout_E( poornSurakshaBussinesLogic.getPremiumBeforeST_CI(SumAssured_CI))) ;
//		TotalPremiumWithoutST_CI=commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(Double.parseDouble(poornSurakshaBussinesLogic.getTotalPremiumWithoutST_CI())));
//		TotalPremiumWithST_CI = commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(Double.parseDouble(poornSurakshaBussinesLogic.getTotalPremiumWithST_CI()))) ;
//		ServiceTax_CI = commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(poornSurakshaBussinesLogic.getServiceTax_CI()));
//
//		InstallmentPremLifeCover_ExclSt = commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(Double.parseDouble(PremiumBeforeST) - Double.parseDouble(PremiumBeforeST_CI))) ;
//		ApplicableTaxesLifeCover = commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(Double.parseDouble(ServiceTax) - Double.parseDouble(ServiceTax_CI))) ;
//		InstallmentPremLifeCover_InclSt = commonForAllProd.getRoundOffLevel2New(commonForAllProd.getStringWithout_E(Double.parseDouble(InstallmentPremLifeCover_ExclSt) + Double.parseDouble(ApplicableTaxesLifeCover))) ;
//
//		TotalFinalPremium_IncST = commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(Double.parseDouble(InstallmentPremLifeCover_InclSt) +  Double.parseDouble(TotalPremiumWithST_CI))) ;

        riderPrem_exclST_exclDisc = commonForAllProd.getStringWithout_E(poornSurakshaBussinesLogic.getPremiumWithoutSTWithoutDisc_CritiIllness(Double.parseDouble(critiSumAssured)));
        riderPrem_exclST_exclDisc_exclFreqLoading = commonForAllProd.getStringWithout_E(poornSurakshaBussinesLogic.getAnnualPremiumWithoutDiscWithoutFreqLoadingWithoutSAZero_CritiIllness(Double.parseDouble(critiSumAssured)));

        totInstPrem_exclST_exclDisc = commonForAllProd.getStringWithout_E((poornSurakshaBussinesLogic.getPremiumWithoutSTWithoutDisc(poornsurakshaBean.getSumAssured())));
        totInstPrem_exclST_exclDisc_exclFreqLoading = commonForAllProd.getStringWithout_E((poornSurakshaBussinesLogic.getAnnualPremiumWithoutDiscWithoutFreqLoadingWithoutSAZero(poornsurakshaBean.getSumAssured())));

        totalInstPrem_exclST_exclDisc = commonForAllProd.getRoundOffLevel2New(commonForAllProd.getStringWithout_E((poornSurakshaBussinesLogic.getTotalPremiumWithoutSTWithoutDisc())));

        SumAssured_basic_80 = commonForAllProd.getStringWithout_E(poornSurakshaBussinesLogic.getSumAssured_basic_80(poornsurakshaBean.getSumAssured()));
        MinesOccuInterest = "" + poornSurakshaBussinesLogic.getMinesOccuInterest(poornsurakshaBean.getSumAssured());

        String servicetax_MinesOccuInterest = ""
                + poornSurakshaBussinesLogic
                .getServiceTaxMines(Double.parseDouble(MinesOccuInterest));

        MinesOccuInterest = "" + (Double.parseDouble(MinesOccuInterest) + Double.parseDouble(servicetax_MinesOccuInterest));
        // }

        String TotalBasePremiumPaidWithoutTaxes = "", GuaranteedDeathBenifit = "",
                GuaranteedCriticalillnessBenifit = "";
        try {
            for (int i = 1; i <= limit; i++) {
                bussIll.append("<policyYr" + i + ">" + i + "</policyYr" + i + ">");


                TotalBasePremiumPaidWithoutTaxes = commonForAllProd.getRoundUp(commonForAllProd.getRoundOffLevel2New(commonForAllProd.getStringWithout_E(poornSurakshaBussinesLogic.getTotalBasePremiumPaidWithoutTaxes(i, poornsurakshaBean.getPremiumFrequency(), TotalFinalPremium_ExclST))));
                bussIll.append("<TotalBasePremiumPaidWithoutST").append(i).append(">").append(TotalBasePremiumPaidWithoutTaxes).append("</TotalBasePremiumPaidWithoutST").append(i).append(">");


                GuaranteedDeathBenifit = commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(poornSurakshaBussinesLogic.getGuaranteedDeathBenifit(i, poornsurakshaBean.getSumAssured(), poornsurakshaBean.getPolicyterm())));
                bussIll.append("<GuaranteedDeathBenifit").append(i).append(">").append(GuaranteedDeathBenifit).append("</GuaranteedDeathBenifit").append(i).append(">");


                GuaranteedCriticalillnessBenifit = commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(poornSurakshaBussinesLogic.getGuaranteedCriticalillnessBenifit(i, poornsurakshaBean.getSumAssured(), poornsurakshaBean.getPolicyterm())));
                bussIll.append("<GuaranteedCriticalillnessBenifit").append(i).append(">").append(GuaranteedCriticalillnessBenifit).append("</GuaranteedCriticalillnessBenifit").append(i).append(">");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new String[]
                {PremiumBeforeST,
                        commonForAllProd.getStringWithout_E(TotalFinalPremium_ExclST),
                        TotalFinalPremium_IncST,
                        TotalApplicableTaxes,
                        PremiumBeforeST_CI,
                        TotalPremiumWithoutST_CI,
                        TotalPremiumWithST_CI,
                        ServiceTax_CI,
                        InstallmentPremLifeCover_ExclSt,
                        ApplicableTaxesLifeCover,
                        InstallmentPremLifeCover_InclSt,
                        TotalFinalPremium_IncST,
                        totInstPrem_exclST_exclDisc,
                        totInstPrem_exclST_exclDisc_exclFreqLoading,
                        riderPrem_exclST_exclDisc,
                        riderPrem_exclST_exclDisc_exclFreqLoading,
                        SumAssured_basic_80,
                        MinesOccuInterest, totalInstPrem_exclST_exclDisc, servicetax_MinesOccuInterest};


    }

    // Validate Sum Assured
    private Boolean valSA() {
        StringBuilder error = new StringBuilder();

        if (basicSA.getText().toString().equals("")) {
            error.append("Please enter Sum Assured.");
            basicSA.requestFocus();
        } else if (Double.parseDouble(basicSA.getText().toString()) % 100000 != 0) {
            error.append("Sum assured should be multiple of 1,00,000");
            basicSA.requestFocus();
        } else if (Double.parseDouble(basicSA.getText().toString()) < 2000000) {
            error.append("Sum assured should be greater than or equal to 20,00,000");
            basicSA.requestFocus();

        } else if (Double.parseDouble(basicSA.getText().toString()) > 25000000) {
            error.append("Sum assured should be less than or equal to 2,50,00,000");
            basicSA.requestFocus();

        }

        if (!error.toString().equals("")) {
            mCommonMethods.showMessageDialog(mContext, error.toString());
            return false;
        } else
            return true;
    }

    // Validate Age
    private void valAge() {
        int maxLimit;
        maxLimit = 65;

        if (Integer.parseInt(ageInYears.getSelectedItem().toString()) > maxLimit) {
            showAlert.setMessage("Enter age between 18 to " + maxLimit);
            showAlert.setNeutralButton("OK",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ageInYears.setSelection(0, false);
                        }
                    });
            btn_bi_poorn_suraksha_life_assured_date.setText("Select Date");
            btn_bi_poorn_suraksha_life_assured_date.requestFocus();
            lifeAssured_date_of_birth = "";

            showAlert.show();
        }
    }

    // Validate policy Term
    private boolean valTerm() {
        int minLimit = 10;
        int maxLimit;
        String message;
        maxLimit = 30;

        if (Integer.parseInt(selBasicTerm.getSelectedItem().toString()) < minLimit
                || Integer.parseInt(selBasicTerm.getSelectedItem().toString()) > maxLimit) {

            message = "Enter Basic Term Between 15 and 30";

            showAlert.setMessage(message);
            showAlert.setNeutralButton("OK",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
            showAlert.show();
            setFocusable(selBasicTerm);
            selBasicTerm.requestFocus();

            return false;
        }

        return true;
    }

    //Validate Maturity Age
    private boolean valMaturityAge() {
        boolean valMaturity = true;

        int Age = Integer.parseInt(ageInYears.getSelectedItem().toString());
        int PolicyTerm = Integer.parseInt(selBasicTerm.getSelectedItem()
                .toString());
        if ((Age + PolicyTerm) > 75) {
            valMaturity = false;
            showAlert.setMessage("Maturity age cannot be greater than 75");
            showAlert.setNeutralButton("OK",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            selBasicTerm.setSelection(0, false);
                        }
                    });
            setFocusable(btn_bi_poorn_suraksha_life_assured_date);
            btn_bi_poorn_suraksha_life_assured_date.requestFocus();
            showAlert.show();
        } else {
            valMaturity = true;
        }
        return valMaturity;
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
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
            showAlert.show();
            return false;
        }
    }

/*	public boolean valDoYouBackdate() {
		if (!proposer_Backdating_WishToBackDate_Policy.equals("")) {
			return true;
		} else {
			showAlert.setMessage("Please Select Do you wish to Backdate ");
			showAlert.setNeutralButton("OK",
					new DialogInterface.OnClickListener() {
						@Override
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
*/

/*	public boolean valBackdate() {
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
			}

			else {
				return true;
			}
		}
		return true;
	}*/

/*	boolean TrueBackdate() {

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
				}

			}

		} catch (Exception e) {
			return false;
		}

		return true;

	}*/

    private boolean valProposerSameAsLifeAssured() {
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
                            setFocusable(rb_poorn_suraksha_proposer_same_as_life_assured_yes);
                            rb_poorn_suraksha_proposer_same_as_life_assured_yes
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
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                if (lifeAssured_Title.equals("")) {
                                    // apply focusable method
                                    setFocusable(spnr_bi_poorn_suraksha_life_assured_title);
                                    spnr_bi_poorn_suraksha_life_assured_title
                                            .requestFocus();
                                } else if (lifeAssured_First_Name.equals("")) {
                                    edt_bi_poorn_suraksha_life_assured_first_name
                                            .requestFocus();
                                } else {
                                    edt_bi_poorn_suraksha_life_assured_last_name
                                            .requestFocus();
                                }
                            }
                        });
                showAlert.show();

                return false;
            } else if (lifeAssured_Title.equalsIgnoreCase("Mr.")
                    && str_lifeAssured_gender.equalsIgnoreCase("Female")) {

                showAlert
                        .setMessage("Life Assured Title and Gender is not Valid");
                showAlert.setNeutralButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // apply focusable method
                                setFocusable(spnr_bi_poorn_suraksha_life_assured_title);
                                spnr_bi_poorn_suraksha_life_assured_title
                                        .requestFocus();
                            }
                        });
                showAlert.show();

                return false;

            } else if (lifeAssured_Title.equalsIgnoreCase("Ms.")
                    && str_lifeAssured_gender.equalsIgnoreCase("Male")) {

                showAlert
                        .setMessage("Life Assured Title and Gender is not Valid");
                showAlert.setNeutralButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // apply focusable method
                                setFocusable(spnr_bi_poorn_suraksha_life_assured_title);
                                spnr_bi_poorn_suraksha_life_assured_title
                                        .requestFocus();
                            }
                        });
                showAlert.show();

                return false;

            } else if (lifeAssured_Title.equalsIgnoreCase("Mrs.")
                    && str_lifeAssured_gender.equalsIgnoreCase("Male")) {

                showAlert
                        .setMessage("Life Assured Title and Gender is not Valid");
                showAlert.setNeutralButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // apply focusable method
                                setFocusable(spnr_bi_poorn_suraksha_life_assured_title);
                                spnr_bi_poorn_suraksha_life_assured_title
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
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                if (lifeAssured_Title.equals("")) {
                                    // apply focusable method
                                    setFocusable(spnr_bi_poorn_suraksha_life_assured_title);
                                    spnr_bi_poorn_suraksha_life_assured_title
                                            .requestFocus();
                                } else if (lifeAssured_First_Name.equals("")) {
                                    edt_bi_poorn_suraksha_life_assured_first_name
                                            .requestFocus();
                                } else {
                                    edt_bi_poorn_suraksha_life_assured_last_name
                                            .requestFocus();
                                }
                            }
                        });
                showAlert.show();

                return false;
            } else if (lifeAssured_Title.equalsIgnoreCase("Mr.")
                    && str_lifeAssured_gender.equalsIgnoreCase("Female")) {

                showAlert
                        .setMessage("Life Assured Title and Gender is not Valid");
                showAlert.setNeutralButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // apply focusable method
                                setFocusable(spnr_bi_poorn_suraksha_life_assured_title);
                                spnr_bi_poorn_suraksha_life_assured_title
                                        .requestFocus();
                            }
                        });
                showAlert.show();

                return false;

            } else if (lifeAssured_Title.equalsIgnoreCase("Ms.")
                    && str_lifeAssured_gender.equalsIgnoreCase("Male")) {

                showAlert
                        .setMessage("Life Assured Title and Gender is not Valid");
                showAlert.setNeutralButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // apply focusable method
                                setFocusable(spnr_bi_poorn_suraksha_life_assured_title);
                                spnr_bi_poorn_suraksha_life_assured_title
                                        .requestFocus();
                            }
                        });
                showAlert.show();

                return false;

            } else if (lifeAssured_Title.equalsIgnoreCase("Mrs.")
                    && str_lifeAssured_gender.equalsIgnoreCase("Male")) {

                showAlert
                        .setMessage("Life Assured Title and Gender is not Valid");
                showAlert.setNeutralButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // apply focusable method
                                setFocusable(spnr_bi_poorn_suraksha_life_assured_title);
                                spnr_bi_poorn_suraksha_life_assured_title
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
                                    setFocusable(spnr_bi_poorn_suraksha_proposer_title);
                                    spnr_bi_poorn_suraksha_proposer_title
                                            .requestFocus();
                                } else if (proposer_First_Name.equals("")) {
                                    edt_bi_poorn_suraksha_proposer_first_name
                                            .requestFocus();
                                } else {
                                    edt_bi_poorn_suraksha_proposer_last_name
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
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // apply focusable method
                                setFocusable(btn_bi_poorn_suraksha_life_assured_date);
                                btn_bi_poorn_suraksha_life_assured_date
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
                                setFocusable(btn_bi_poorn_suraksha_life_assured_date);
                                btn_bi_poorn_suraksha_life_assured_date
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
                                setFocusable(btn_bi_poorn_suraksha_proposer_date);
                                btn_bi_poorn_suraksha_proposer_date
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

    private boolean valminPremiumValueAndRider() {
        PoornSurakshaBean poornSurakshaBean = new PoornSurakshaBean();
        PoornSurakshaBusinessLogic poornSurakshaBussinesLogic = new PoornSurakshaBusinessLogic(
                poornSurakshaBean);
        obj = new CommonForAllProd();
        poornSurakshaBean
                .setPremiumFrequency(selPremFreq.getSelectedItem().toString());

        poornSurakshaBean.setAge(Integer.parseInt(ageInYears.getSelectedItem()
                .toString().trim()));
        poornSurakshaBean.setPolicyterm(Integer.parseInt(selBasicTerm
                .getSelectedItem().toString()));
        poornSurakshaBean.setSumAssured(Integer.parseInt(basicSA.getText()
                .toString()));
        poornSurakshaBean.setGender(selGender.getSelectedItem().toString());

        if (selStaffDisc.isChecked()) {
            poornSurakshaBean.setIsForStaffOrNot(true);
        } else {
            poornSurakshaBean.setIsForStaffOrNot(false);
        }

        if (rb_poorn_suraksha_life_assured_smoker_yes.isChecked()) {
            poornSurakshaBean.setSmoker("Smoker");
        } else if (rb_poorn_suraksha_life_assured_smoker_no.isChecked()) {
            poornSurakshaBean.setSmoker("Non-Smoker");
        }

        PremiumRate = poornSurakshaBussinesLogic.getPremiumRate(poornSurakshaBean.getGender(), poornSurakshaBean.getSmoker(), poornSurakshaBean.getAge(), poornSurakshaBean.getPolicyterm());
        StaffRebate = poornSurakshaBussinesLogic.getStaffRebate(poornSurakshaBean.getIsForStaffOrNot());
        SARebate = poornSurakshaBussinesLogic.getSARebate(poornSurakshaBean.getSumAssured());
        LoadingFrequencyPremium = poornSurakshaBussinesLogic.getLoadingFrequencyPremium(poornSurakshaBean.getPremiumFrequency());
        String premiumBasic = ""
                + poornSurakshaBussinesLogic
                .getPremiumBeforeST(poornSurakshaBean.getSumAssured());

        /* modified by Akshaya on 23-APR-14 **/
        double premBasic = Double.parseDouble(obj
                .getRoundUp(obj.getStringWithout_E(Double
                        .parseDouble(premiumBasic))));

        String valPremiumError = valInstPremium(
                new CommonForAllProd().getRoundUp(premiumBasic),
                poornSurakshaBean.getPremiumFrequency());


        if (!valPremiumError.equals("")) {

            mCommonMethods.showMessageDialog(mContext, valPremiumError);
            return false;
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

    private void setDefaultDate(int id) {
        Calendar present_date = Calendar.getInstance();
        present_date.add(Calendar.YEAR, -id);
        mDay = present_date.get(Calendar.DAY_OF_MONTH);
        mMonth = present_date.get(Calendar.MONTH);
        mYear = present_date.get(Calendar.YEAR);
    }

    public String getDate_DD_MM_YY(String OldDate) {
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

    public String getDate_MM_dd_yy(String OldDate) {
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

    /************************* validation ends here ********************************************/

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
        if (v.getId() == edt_bi_poorn_suraksha_life_assured_first_name.getId()) {
            // clearFocusable(edt_bi_poorn_suraksha_life_assured_first_name);
            setFocusable(edt_bi_poorn_suraksha_life_assured_middle_name);
            edt_bi_poorn_suraksha_life_assured_middle_name.requestFocus();
        } else if (v.getId() == edt_bi_poorn_suraksha_life_assured_middle_name
                .getId()) {
            // clearFocusable(edt_bi_poorn_suraksha_life_assured_middle_name);
            setFocusable(edt_bi_poorn_suraksha_life_assured_last_name);
            edt_bi_poorn_suraksha_life_assured_last_name.requestFocus();
        } else if (v.getId() == edt_bi_poorn_suraksha_life_assured_last_name
                .getId()) {

            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(
                    edt_bi_poorn_suraksha_life_assured_last_name.getWindowToken(),
                    0);
            // clearFocusable(edt_bi_poorn_suraksha_life_assured_last_name);
            setFocusable(btn_bi_poorn_suraksha_life_assured_date);
            btn_bi_poorn_suraksha_life_assured_date.requestFocus();
        } else if (v.getId() == edt_bi_poorn_suraksha_proposer_first_name.getId()) {
            // clearFocusable(edt_bi_poorn_suraksha_proposer_first_name);
            setFocusable(edt_bi_poorn_suraksha_proposer_middle_name);
            edt_bi_poorn_suraksha_proposer_middle_name.requestFocus();
        } else if (v.getId() == edt_bi_poorn_suraksha_proposer_middle_name
                .getId()) {
            // clearFocusable(edt_bi_poorn_suraksha_proposer_middle_name);
            setFocusable(edt_bi_poorn_suraksha_proposer_last_name);
            edt_bi_poorn_suraksha_proposer_last_name.requestFocus();
        } else if (v.getId() == edt_bi_poorn_suraksha_proposer_last_name.getId()) {

            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(
                    edt_bi_poorn_suraksha_proposer_last_name.getWindowToken(), 0);
            // clearFocusable(edt_bi_poorn_suraksha_proposer_last_name);
            setFocusable(btn_bi_poorn_suraksha_proposer_date);
            btn_bi_poorn_suraksha_proposer_date.requestFocus();
        } else if (v.getId() == edt_proposerdetail_basicdetail_contact_no
                .getId()) {
            setFocusable(edt_proposerdetail_basicdetail_Email_id);
            edt_proposerdetail_basicdetail_Email_id.requestFocus();
        } else if (v.getId() == edt_proposerdetail_basicdetail_Email_id.getId()) {
            setFocusable(edt_proposerdetail_basicdetail_ConfirmEmail_id);
            edt_proposerdetail_basicdetail_ConfirmEmail_id.requestFocus();
        }

//		else if (v.getId() == edt_proposerdetail_basicdetail_ConfirmEmail_id
//				.getId()) {
//			clearFocusable(selPlan);
//			setFocusable(selPlan);
//			selPlan.requestFocus();
//		}

        else if (v.getId() == basicSA.getId()) {

            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(basicSA.getWindowToken(), 0);

            // clearFocusable(basicSA);
            setFocusable(rb_proposerdetail_personaldetail_backdating_yes);
            rb_proposerdetail_personaldetail_backdating_yes.requestFocus();
        }


        return true;
    }

    /* Basic Details Method */
    private boolean valSMOKER() {
        if (!rb_poorn_suraksha_life_assured_smoker_yes.isChecked() && !rb_poorn_suraksha_life_assured_smoker_no.isChecked()) {
            showAlert.setMessage("Please select if Life Assured is Smoker or Not");
            showAlert.setNeutralButton("OK",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog,
                                            int which) {
                            // apply focusable method
                            setFocusable(rb_poorn_suraksha_life_assured_smoker_yes);
                            rb_poorn_suraksha_life_assured_smoker_yes
                                    .requestFocus();

                        }
                    });
            showAlert.show();
            return false;
        } else {
            return true;
        }

    }

    private boolean valBasicDetail() {
        if (selGender.getSelectedItem().toString().equals("Select Gender")) {
            mCommonMethods.dialogWarning(mContext, "Please Select Gender", true);
            selGender.requestFocus();
            return false;
        } else if (edt_proposerdetail_basicdetail_contact_no.getText().toString()
                .equals("")) {
            mCommonMethods.dialogWarning(mContext, "Please Fill  Mobile No", true);
            edt_proposerdetail_basicdetail_contact_no.requestFocus();
            return false;
        } else if (edt_proposerdetail_basicdetail_contact_no.getText()
                .toString().length() != 10) {
            mCommonMethods.dialogWarning(mContext, "Please Fill 10 Digit Mobile No", true);
            edt_proposerdetail_basicdetail_contact_no.requestFocus();
            return false;
        } /*else if (emailId.equals("")) {
            mCommonMethods.dialogWarning(mContext,"Please Fill Email Id", true);
            edt_proposerdetail_basicdetail_Email_id.requestFocus();
            return false;

        } else if (ConfirmEmailId.equals("")) {

            mCommonMethods.dialogWarning(mContext,"Please Fill Confirm Email Id", true);
            edt_proposerdetail_basicdetail_ConfirmEmail_id.requestFocus();
            return false;
        } else if (!ConfirmEmailId.equals(emailId)) {
            mCommonMethods.dialogWarning(mContext,"Email Id Does Not Match", true);
            return false;
        } */ else if (!emailId.equals("")) {

            email_id_validation(emailId);
            if (validationFla1) {

                if (ConfirmEmailId.equals("")) {

                    mCommonMethods.dialogWarning(mContext, "Please Fill Confirm Email Id", true);
                    edt_proposerdetail_basicdetail_ConfirmEmail_id.requestFocus();
                    return false;
                } else if (!ConfirmEmailId.equals(emailId)) {
                    mCommonMethods.dialogWarning(mContext, "Email Id Does Not Match", true);
                    return false;
                }

                return true;
            } else {
                mCommonMethods.dialogWarning(mContext, "Please Fill Valid Email Id", true);
                edt_proposerdetail_basicdetail_Email_id.requestFocus();
                return false;
            }
        } else if (!ConfirmEmailId.equals("")) {

            email_id_validation(ConfirmEmailId);
            if (validationFla1) {
                if (emailId.equals("")) {
                    mCommonMethods.dialogWarning(mContext, "Please Fill Email Id", true);
                    edt_proposerdetail_basicdetail_Email_id.requestFocus();
                    return false;

                }
                return true;
            } else {
                mCommonMethods.dialogWarning(mContext, "Please Fill Valid Confirm Email Id", true);
                edt_proposerdetail_basicdetail_ConfirmEmail_id.requestFocus();
                return false;
            }
        } else {
            return true;
        }
    }

    private void mobile_validation(String number) {
        boolean validationFlag3 = false;
        if ((number.length() != 10)) {
            edt_proposerdetail_basicdetail_contact_no
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
            edt_proposerdetail_basicdetail_Email_id
                    .setError("Please provide the correct email address");
            validationFla1 = false;
        } else if (!selStaffDisc.isChecked()
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

    private void setDefaultDate1(int id) {
        Calendar present_date = Calendar.getInstance();
        present_date.add(Calendar.DAY_OF_MONTH, +id);
        mDay = present_date.get(Calendar.DAY_OF_MONTH);
        mMonth = present_date.get(Calendar.MONTH);
        mYear = present_date.get(Calendar.YEAR);

        date = new StringBuilder().append(mDay).append("-").append(mMonth + 1)
                .append("-").append(mYear);
    }

    private void date_comparison() {

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

}
