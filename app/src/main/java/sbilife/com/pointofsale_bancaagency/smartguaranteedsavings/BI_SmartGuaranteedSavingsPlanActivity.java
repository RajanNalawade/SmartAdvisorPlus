package sbilife.com.pointofsale_bancaagency.smartguaranteedsavings;

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
import sbilife.com.pointofsale_bancaagency.needanalysis.NeedAnalysisActivity;
import sbilife.com.pointofsale_bancaagency.new_bussiness.ProductBIBean;
import sbilife.com.pointofsale_bancaagency.utility.GridHeight;

@SuppressWarnings("deprecation")
public class BI_SmartGuaranteedSavingsPlanActivity extends AppCompatActivity implements
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

    private Spinner spnr_Age, spnr_Gender;

    private Button btnSubmit, btnback;

    // for BI
    private StringBuilder inputVal;
    private String premPayingMode = "";
    private String policyTerm = "";
    private String ageAtEntry = "";
    private String gender = "";

    private String basicCoverSumAssured = "", basicCoverYearlyPremium = "",
            premiumDetailFYBasicPremium = "", premiumDetailFYServiceTax = "",
            premiumDetailFYPremiumWithServiceTax = "",
            premiumDetailSYBasicPremium = "", premiumDetailSYServiceTax = "",
            premiumDetailSYPremiumWithServiceTax = "";

    private SmartGuaranteedSavingsPlanBean smartGuranteedBean;

    private EditText edt_premiumAmt;

    private Spinner spnrPremPayingTerm, spnrPremPayingMode, spnrPolicyTerm;

    private RadioButton rb_proposerdetail_personaldetail_backdating_yes,
            rb_proposerdetail_personaldetail_backdating_no;

    private LinearLayout ll_backdating1;

    private Button btn_proposerdetail_personaldetail_backdatingdate,
            btn_MarketingOfficalDate;
    private Button btn_PolicyholderDate;

    private ImageButton Ibtn_signatureofMarketing;
    private ImageButton Ibtn_signatureofPolicyHolders;


    private Dialog d;
    private final int SIGNATURE_ACTIVITY = 1;
    private String latestImage = "";

    // List used for The Policy Detail Depend on The policy Term
    private List<M_BI_SmartGuaranteed_Saving_Plan_Adapter> list_data;


    private final int DATE_DIALOG_ID = 1;
    private int DIALOG_ID;
    private int mYear;
    private int mMonth;
    private int mDay;

    private Button btn_bi_smart_guaranteed_saving_plan_life_assured_date;

    private String QuatationNumber = "";
    private String planName = "";


    private String lifeAssured_Title = "", lifeAssured_First_Name = "",
            lifeAssured_Middle_Name = "", lifeAssured_Last_Name = "",
            name_of_life_assured = "", lifeAssured_date_of_birth = "",
            lifeAssuredAge = "";

    private String proposer_Backdating_WishToBackDate_Policy = "";
    private String proposer_Backdating_BackDate = "";

    private String BackDateinterest, BackDateinterestwithGST;

    // Spinner USed
    private Spinner spnr_bi_smart_guaranteed_saving_plan_life_assured_title;

    // edit Text Used
    private EditText edt_bi_smart_guaranteed_saving_plan_life_assured_first_name;
    private EditText edt_bi_smart_guaranteed_saving_plan_life_assured_middle_name;
    private EditText edt_bi_smart_guaranteed_saving_plan_life_assured_last_name;


    // Retrieving value from database and storing
    private String output = "";

    // For Bi Dialog
    private ParseXML prsObj;
    private String name_of_proposer = "";
    private String name_of_person = "";
    private String place2 = "";
    private String date1 = "";
    private String date2 = "";
    private String agent_sign = "";
    private String proposer_sign = "", proposer_Is_Same_As_Life_Assured = "Y";

    // Class Declaration
    private CommonForAllProd commonForAllProd;
    private SmartGuaranteedSavingsPlanProperties prop;

    // Variable Declaration
    private DecimalFormat currencyFormat;
    private AlertDialog.Builder showAlert;

    private StringBuilder bussIll = null;
    private StringBuilder retVal = null;

    private File mypath;

    private ScrollView svSmartGuaranteedMain;

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
    private ImageButton imageButtonSmartGuranteedSavingPlanProposerPhotograph;

    private LinearLayout linearlayoutThirdpartySignature,
            linearlayoutAppointeeSignature;
    private ImageButton Ibtn_signatureofThirdParty, Ibtn_signatureofAppointee;
    private String thirdPartySign = "", appointeeSign = "";
    private Context context;
    private String product_Code, product_UIN, product_cateogory, product_type;
    private String Company_policy_surrender_dec = "";

    /* end */

    private CheckBox cb_kerladisc;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.bi_smartguarateed_savingplanmain);
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
                    planName = "Smart Guaranteed Savings Plan";
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
                QuatationNumber = CommonForAllProd.getquotationNumber30("1X",
                        agentcode, zero + "");

            }
        } else
            needAnalysis_flag = 0;
        // ProductHomePageActivity.path.setText("Benefit Illustrator");

        initialiseDate();

        prsObj = new ParseXML();

        commonForAllProd = new CommonForAllProd();
        prop = new SmartGuaranteedSavingsPlanProperties();

        list_data = new ArrayList<>();

        // Variable Declaration
        currencyFormat = new DecimalFormat("##,##,##,###");
        showAlert = new AlertDialog.Builder(this);

        svSmartGuaranteedMain = findViewById(R.id.sv_bi_smart_guaranteed_main);
        rb_proposerdetail_personaldetail_backdating_yes = findViewById(R.id.rb_smart_guaranteed_saving_backdating_yes);
        rb_proposerdetail_personaldetail_backdating_no = findViewById(R.id.rb_smart_guaranteed_saving_backdating_no);

        ll_backdating1 = findViewById(R.id.ll_backdating1);
        btn_proposerdetail_personaldetail_backdatingdate = findViewById(R.id.btn_smart_guaranteed_saving_backdatingdate);
        edt_premiumAmt = findViewById(R.id.et_bi_smart_guaranteed_saving_plan_premium_amt);
        btn_bi_smart_guaranteed_saving_plan_life_assured_date = findViewById(R.id.btn_bi_smart_guaranteed_saving_plan_life_assured_date);
        spnr_bi_smart_guaranteed_saving_plan_life_assured_title = findViewById(R.id.spnr_bi_smart_guaranteed_saving_plan_life_assured_title);
        edt_bi_smart_guaranteed_saving_plan_life_assured_first_name = findViewById(R.id.edt_bi_smart_guaranteed_saving_plan_life_assured_first_name);
        edt_bi_smart_guaranteed_saving_plan_life_assured_middle_name = findViewById(R.id.edt_bi_smart_guaranteed_saving_plan_life_assured_middle_name);
        edt_bi_smart_guaranteed_saving_plan_life_assured_last_name = findViewById(R.id.edt_bi_smart_guaranteed_saving_plan_life_assured_last_name);
        spnrPremPayingTerm = findViewById(R.id.spnr_bi_smart_guaranteed_saving_plan_premium_paying_term);
        spnrPremPayingMode = findViewById(R.id.spnr_bi_smart_guaranteed_saving_plan_premium_paying_mode);
        spnrPolicyTerm = findViewById(R.id.spnr_bi_smart_guaranteed_saving_plan_policyterm);
        spnr_Gender = findViewById(R.id.spnr_bi_smart_guaranteed_saving_plan_selGender);
        spnr_Gender.setClickable(false);
        spnr_Gender.setEnabled(false);

        spnr_Age = findViewById(R.id.spnr_bi_smart_guaranteed_saving_plan_age);
        spnr_Age.setEnabled(false);

        edt_proposerdetail_basicdetail_contact_no = findViewById(R.id.edt_smart_guaranteed_saving_contact_no);
        edt_proposerdetail_basicdetail_Email_id = findViewById(R.id.edt_guaranteed_saving_Email_id);
        edt_proposerdetail_basicdetail_ConfirmEmail_id = findViewById(R.id.edt_guaranteed_saving_ConfirmEmail_id);

        btnSubmit = findViewById(R.id.btn_bi_smart_guaranteed_saving_plan_btnSubmit);
        btnback = findViewById(R.id.btn_bi_smart_guaranteed_saving_plan_btnback);

        // UI elements
        // inputActivityHeader = (TextView)
        // findViewById(R.id.txt_input_activityheader);
        // Gender

        String[] genderList = {"Male", "Female"};
        ArrayAdapter<String> genderAdapter = new ArrayAdapter<>(getApplicationContext(),
                R.layout.spinner_item, genderList);
        genderAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnr_Gender.setAdapter(genderAdapter);
        genderAdapter.notifyDataSetChanged();

        /*
         * title_list.add("Gen."); title_list.add("Lt.Gen.");
         * title_list.add("Maj.Gen."); title_list.add("Brig.");
         * title_list.add("Col."); title_list.add("Lt.Col.");
         * title_list.add("Major"); title_list.add("Capt.");
         * title_list.add("Lt."); title_list.add("Gr.Capt.");
         * title_list.add("Fr."); title_list.add("Dr.");
         */

        commonMethods.fillSpinnerValue(context,
                spnr_bi_smart_guaranteed_saving_plan_life_assured_title,
                Arrays.asList(getResources().getStringArray(R.array.arr_select_title)));

        // Age

        String[] AgeList = new String[prop.maxAge - prop.minAge + 1];
        for (int i = prop.minAge; i <= prop.maxAge; i++) {
            AgeList[i - prop.minAge] = i + "";
        }
        ArrayAdapter<String> AgeAdapter = new ArrayAdapter<>(
                getApplicationContext(), R.layout.spinner_item, AgeList);
        AgeAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnr_Age.setAdapter(AgeAdapter);
        AgeAdapter.notifyDataSetChanged();
        spnr_Age.setSelection(prop.defaultAge, false);

        String[] policyTermArray = {"15"};

        ArrayAdapter<String> policyTermAdapter = new ArrayAdapter<>(
                getApplicationContext(), R.layout.spinner_item, policyTermArray);

        spnrPolicyTerm.setAdapter(policyTermAdapter);
        spnrPolicyTerm.setEnabled(false);

        String[] premPayingTermArray = {"7"};

        ArrayAdapter<String> premPayingTermAdapter = new ArrayAdapter<>(
                getApplicationContext(), R.layout.spinner_item,
                premPayingTermArray);

        spnrPremPayingTerm.setAdapter(premPayingTermAdapter);
        spnrPremPayingTerm.setEnabled(false);

        String[] premPayingModeArray = {"Yearly"};

        ArrayAdapter<String> premPayingModeAdapter = new ArrayAdapter<>(
                getApplicationContext(), R.layout.spinner_item,
                premPayingModeArray);

        spnrPremPayingMode.setAdapter(premPayingModeAdapter);
        spnrPremPayingMode.setEnabled(false);

        // Calculate premium

        // setBIInputGui();
        edt_bi_smart_guaranteed_saving_plan_life_assured_first_name
                .setOnEditorActionListener(this);
        edt_bi_smart_guaranteed_saving_plan_life_assured_last_name
                .setOnEditorActionListener(this);
        edt_bi_smart_guaranteed_saving_plan_life_assured_middle_name
                .setOnEditorActionListener(this);
        edt_premiumAmt.setOnEditorActionListener(this);

        setSpinnerAndOtherListner();

        setFocusable(edt_bi_smart_guaranteed_saving_plan_life_assured_first_name);

        edt_bi_smart_guaranteed_saving_plan_life_assured_first_name
                .requestFocus();

        if (needAnalysis_flag == 1 && !TextUtils.isEmpty(gender)) {
            spnr_Gender.setSelection(genderAdapter.getPosition(gender));
            onClickLADob(btn_bi_smart_guaranteed_saving_plan_life_assured_date);
        }

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
        // getBasicDetail();
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

        boolean flagFocus = true;
        if (!flagFocus) {
            svSmartGuaranteedMain.requestFocus();
        } else {
            edt_bi_smart_guaranteed_saving_plan_life_assured_first_name
                    .requestFocus();
        }

    }

    private void setSpinnerAndOtherListner() {
        // TODO Auto-generated method stub

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

                            spnr_Age.setSelection(
                                    getIndex(spnr_Age, lifeAssuredAge), false);

                            valMaturityAge();
                            rb_proposerdetail_personaldetail_backdating_yes
                                    .setFocusable(false);

                            clearFocusable(rb_proposerdetail_personaldetail_backdating_no);
                            clearFocusable(rb_proposerdetail_personaldetail_backdating_yes);

                        }
                    }
                });

        spnr_bi_smart_guaranteed_saving_plan_life_assured_title
                .setOnItemSelectedListener(new OnItemSelectedListener() {

                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int position, long id) {
                        // TODO Auto-generated method stub
                        if (position == 0) {
                            lifeAssured_Title = "";
                        } else {
                            lifeAssured_Title = spnr_bi_smart_guaranteed_saving_plan_life_assured_title
                                    .getSelectedItem().toString();
                            if (lifeAssured_Title.equalsIgnoreCase("Mr.")) {
                                spnr_Gender.setSelection(
                                        getIndex(spnr_Gender, "Male"), false);
                            } else if (lifeAssured_Title
                                    .equalsIgnoreCase("Ms.")) {
                                spnr_Gender.setSelection(
                                        getIndex(spnr_Gender, "Female"), false);
                            } else if (lifeAssured_Title
                                    .equalsIgnoreCase("Mrs.")) {
                                spnr_Gender.setSelection(
                                        getIndex(spnr_Gender, "Female"), false);
                            }
                            setFocusable(edt_bi_smart_guaranteed_saving_plan_life_assured_first_name);

                            edt_bi_smart_guaranteed_saving_plan_life_assured_first_name
                                    .requestFocus();
                        }

                    }

                    public void onNothingSelected(AdapterView<?> arg0) {
                        // TODO Auto-generated method stub

                    }
                });
        btnback.setOnClickListener(new OnClickListener() {

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

                lifeAssured_First_Name = edt_bi_smart_guaranteed_saving_plan_life_assured_first_name
                        .getText().toString();
                lifeAssured_Middle_Name = edt_bi_smart_guaranteed_saving_plan_life_assured_middle_name
                        .getText().toString();
                lifeAssured_Last_Name = edt_bi_smart_guaranteed_saving_plan_life_assured_last_name
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
                if (valLifeAssuredProposerDetail() && valDob()
                        && valBasicDetail() && valMaturityAge()
                        && valPremiumAmt() && valBackdate() && TrueBackdate()) {
                    Date();


                    addListenerOnSubmit();
                    Log.e("ouput2:", output + "");
                    getInput(smartGuranteedBean);
                    // insertDataIntoDatabase();
                    if (needAnalysis_flag == 0) {// Display output on next page
                        Intent i = new Intent(
                                BI_SmartGuaranteedSavingsPlanActivity.this,
                                Success.class);
                        i.putExtra(
                                "op",
                                "Yearly Basic Premium is Rs. "
                                        + currencyFormat.format(Double
                                        .parseDouble(prsObj
                                                .parseXmlTag(retVal
                                                                .toString(),
                                                        "yearlyPrem"))));
                        i.putExtra(
                                "op1",
                                "First Year Applicable Taxes is Rs. "
                                        + currencyFormat.format(Double
                                        .parseDouble(prsObj
                                                .parseXmlTag(retVal
                                                                .toString(),
                                                        "FYServTax"))));
                        i.putExtra(
                                "op2",
                                "First Year Premium with Applicable Taxes is Rs. "
                                        + currencyFormat.format(Double
                                        .parseDouble(prsObj
                                                .parseXmlTag(retVal
                                                                .toString(),
                                                        "FYPrem"))));
                        i.putExtra(
                                "op3",
                                "Second Year Onwards Applicable Taxes is Rs. "
                                        + currencyFormat.format(Double
                                        .parseDouble(prsObj
                                                .parseXmlTag(retVal
                                                                .toString(),
                                                        "SYServTax"))));
                        i.putExtra(
                                "op4",
                                "Second Year Onwards Premium with Applicable Taxes is Rs. "
                                        + currencyFormat.format(Double
                                        .parseDouble(prsObj
                                                .parseXmlTag(retVal
                                                                .toString(),
                                                        "SYPrem"))));
                        i.putExtra(
                                "op5",
                                "<b>Sum Assured</b> is Rs. "
                                        + currencyFormat.format(Double
                                        .parseDouble(prsObj
                                                .parseXmlTag(retVal
                                                                .toString(),
                                                        "sumAssured"))));
                        i.putExtra(
                                "op6",
                                "<b>Guaranteed Maturity Benefit</b> is Rs. "
                                        + currencyFormat.format(Double.parseDouble(prsObj
                                        .parseXmlTag(retVal.toString(),
                                                "GuarntdMatBeneft"))));

                        startActivity(i);
                    } else
                        Dialog();

                }
            }

        });

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
    // spnr_bi_smart_guaranteed_saving_plan_life_assured_title
    // .setSelection(
    // getIndex(
    // spnr_bi_smart_guaranteed_saving_plan_life_assured_title,
    // lifeAssured_Title), false);
    // edt_bi_smart_guaranteed_saving_plan_life_assured_first_name
    // .setText(lifeAssuredName[1]);
    // edt_bi_smart_guaranteed_saving_plan_life_assured_middle_name
    // .setText(lifeAssuredName[2]);
    // edt_bi_smart_guaranteed_saving_plan_life_assured_last_name
    // .setText(lifeAssuredName[3]);
    //
    // proposer_Is_Same_As_Life_Assured = data.get(i)
    // .getProposer_Same_As_Life_Assured();
    //
    // lifeAssured_date_of_birth = data.get(i)
    // .getLife_assured_date_of_birth();
    // btn_bi_smart_guaranteed_saving_plan_life_assured_date
    // .setText(getDate(lifeAssured_date_of_birth));
    //
    // String input = data.get(i).getInput();
    //
    // ageAtEntry = prsObj.parseXmlTag(input, "age");
    // spnr_Age.setSelection(getIndex(spnr_Age, ageAtEntry), false);
    //
    // gender = prsObj.parseXmlTag(input, "gender");
    // spnr_Gender.setSelection(getIndex(spnr_Gender, gender), false);
    //
    // policyTerm = prsObj.parseXmlTag(input, "policyTerm");
    // spnrPolicyTerm.setSelection(getIndex(spnrPolicyTerm, policyTerm),
    // false);
    //
    // premPayingMode = prsObj.parseXmlTag(input, "premFreq");
    // spnrPremPayingMode.setSelection(
    // getIndex(spnrPremPayingMode, premPayingMode), false);
    //
    // premPayingTerm = prsObj.parseXmlTag(input, "premPayingTerm");
    // spnrPremPayingTerm.setSelection(
    // getIndex(spnrPremPayingTerm, premPayingTerm), false);
    //
    // basicCoverYearlyPremium = ((int) Double.parseDouble(prsObj
    // .parseXmlTag(input, "premiumAmount"))) + "";
    // edt_premiumAmt.setText(basicCoverYearlyPremium);
    //
    // proposer_Backdating_BackDate = data.get(i).getBackdate();
    //
    // if (proposer_Backdating_WishToBackDate_Policy.equalsIgnoreCase("y")) {
    // rb_proposerdetail_personaldetail_backdating_yes
    // .setChecked(true);
    // ll_backdating1.setVisibility(View.VISIBLE);
    // btn_proposerdetail_personaldetail_backdatingdate
    // .setText(proposer_Backdating_BackDate);
    //
    // } else {
    // rb_proposerdetail_personaldetail_backdating_no.setChecked(true);
    // ll_backdating1.setVisibility(View.GONE);
    //
    // }
    //
    // }
    //
    // }

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
                    assert b != null;
                    b.recycle();
                    if (mFaceBitmap != null) {
                        Bitmap scaled = Bitmap.createScaledBitmap(bmp, 230,
                                200, true);
                        photoBitmap = scaled;
                        imageButtonSmartGuranteedSavingPlanProposerPhotograph
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
                new ColorDrawable(android.graphics.Color.BLACK));

        d.setContentView(R.layout.layout_smart_guaranteed_saving_plan_bi_grid);

        TextView tv_proposername = d
                .findViewById(R.id.tv_smart_guaranteed_saving_plan_proposername);
        TextView tv_proposal_number = d
                .findViewById(R.id.tv_smart_guaranteed_saving_plan_proposal_number);

        TextView tv_life_assured_name = d
                .findViewById(R.id.tv_bi_smart_guaranteed_saving_plan_life_assured_name);

        TextView tv_life_age_at_entry = d
                .findViewById(R.id.tv_bi_smart_guaranteed_saving_plan_life_assured_age);

        TextView tv_life_assured_gender = d
                .findViewById(R.id.tv_bi_smart_guaranteed_saving_plan_life_assured_gender);

        TextView tv_premPaymentfrequency = d
                .findViewById(R.id.tv_bi_smart_guaranteed_saving_plan_life_assured_paying_mode);

        TextView tv_basic_cover_term = d
                .findViewById(R.id.tv_bi_smart_guaranteed_saving_plan_term_basic_cover);

        TextView tv_basic_cover_premPayment_term = d
                .findViewById(R.id.tv_bi_smart_guaranteed_saving_plan_premium_paying_term_basic_cover);

        TextView tv_basic_cover_sum_assured = d
                .findViewById(R.id.tv_bi_smart_guaranteed_saving_plan_sum_assured_basic_cover);

        TextView tv_basic_cover_yearly_premium = d
                .findViewById(R.id.tv_bi_smart_guaranteed_saving_plan_yearly_premium_basic_cover);

        TextView tv_premium_detail_basic_premium_first_year = d
                .findViewById(R.id.tv_bi_smart_guaranteed_saving_plan_basic_prem_first_year);

        TextView tv_premium_detail_basic_premium_second_year = d
                .findViewById(R.id.tv_bi_smart_guaranteed_saving_plan_basic_prem_second_year);

        TextView tv_premium_detail_basic_premium_with_tax_first_year = d
                .findViewById(R.id.tv_bi_smart_guaranteed_saving_plan_premium_with_service_tax_first_year);

        TextView tv_premium_detail_service_tax_first_year = d
                .findViewById(R.id.tv_bi_smart_guaranteed_saving_plan_service_tax_first_year);

        TextView tv_premium_detail_service_tax_second_year = d
                .findViewById(R.id.tv_bi_smart_guaranteed_saving_plan_service_tax_second_year);

        TextView tv_premium_detail_basic_premium_with_tax_second_year = d
                .findViewById(R.id.tv_bi_smart_guaranteed_saving_plan_premium_with_service_tax_second_year);

        TextView tv_bi_smart_guaranteed_saving_plan_swachh_bharat_tax_first_year = d
                .findViewById(R.id.tv_bi_smart_guaranteed_saving_plan_swachh_bharat_tax_first_year);

        TextView tv_bi_smart_guaranteed_saving_plan_swachh_bharat_tax_second_year = d
                .findViewById(R.id.tv_bi_smart_guaranteed_saving_plan_swachh_bharat_tax_second_year);

        TextView tv_bi_smart_guaranteed_saving_plan_krishi_kalyan_tax_first_year = d
                .findViewById(R.id.tv_bi_smart_guaranteed_saving_plan_krishi_kalyan_tax_first_year);

        TextView tv_bi_smart_guaranteed_saving_plan_krishi_kalyan_tax_second_year = d
                .findViewById(R.id.tv_bi_smart_guaranteed_saving_plan_krishi_kalyan_tax_second_year);

        TextView tv_bi_smart_guaranteed_saving_plan_total_service_tax_first_year = d
                .findViewById(R.id.tv_bi_smart_guaranteed_saving_plan_total_service_tax_first_year);

        TextView tv_bi_smart_guaranteed_saving_plan_total_service_tax_second_year = d
                .findViewById(R.id.tv_bi_smart_guaranteed_saving_plan_total_service_tax_second_year);

        GridView gv_userinfo = d
                .findViewById(R.id.gv_smart_guaranteed_saving_plan_userinfo);
        TextView tv_backdatingint = d
                .findViewById(R.id.tv_backdatingint);
        gv_userinfo.setVerticalScrollBarEnabled(true);
        gv_userinfo.setSmoothScrollbarEnabled(true);

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


        /* parivartan changes */
        imageButtonSmartGuranteedSavingPlanProposerPhotograph = d
                .findViewById(R.id.imageButtonSmartGuranteedSavingPlanProposerPhotograph);
        imageButtonSmartGuranteedSavingPlanProposerPhotograph
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
                            + ", having received the information with respect to the above, have understood the above statement before entering into the contract.");
            edt_proposer_name_need_analysis
                    .setText("I, "
                            + name_of_life_assured
                            + ", have undergone the Need Analysis after having reviewed the SBI Life Product options, I have opted for SBI LIFE - Smart Guaranteed Savings Plan.");

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
                            + ", have undergone the Need Analysis after having reviewed the SBI Life Product options, I have opted for SBI LIFE - Smart Guaranteed Savings Plan.");

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
        // } else {
        // edt_Policyholderplace.setText(place2);
        // }
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
            imageButtonSmartGuranteedSavingPlanProposerPhotograph
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
                            imageButtonSmartGuranteedSavingPlanProposerPhotograph
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
                    String productCode = "1XSGS";

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
                            commonForAllProd.getRound(basicCoverSumAssured),
                            commonForAllProd
                                    .getRound(premiumDetailFYPremiumWithServiceTax),
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
                            commonForAllProd.getRound(basicCoverSumAssured),
                            commonForAllProd
                                    .getRound(premiumDetailFYPremiumWithServiceTax),
                            agentEmail, agentMobile, na_input, na_output,
                            premPayingMode, Integer.parseInt(policyTerm), 0,
                            productCode, getDate(lifeAssured_date_of_birth),
                            "", "", mode, inputVal
                            .toString(), retVal.toString().replace(
                            bussIll.toString(), "")));

                    createPdf();

                    NABIObj.serviceHit(
                            BI_SmartGuaranteedSavingsPlanActivity.this,
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
                        setFocusable(imageButtonSmartGuranteedSavingPlanProposerPhotograph);
                        imageButtonSmartGuranteedSavingPlanProposerPhotograph
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

        String input = inputVal.toString();
        output = retVal.toString();

        ageAtEntry = prsObj.parseXmlTag(input, "age");
        tv_life_age_at_entry.setText(ageAtEntry + " Years");

        gender = prsObj.parseXmlTag(input, "gender");
        tv_life_assured_gender.setText(gender);

        premPayingMode = prsObj.parseXmlTag(input, "premFreq");
        tv_premPaymentfrequency.setText(premPayingMode);

        String premPayingTerm = prsObj.parseXmlTag(input, "premPayingTerm");
        tv_basic_cover_premPayment_term.setText(premPayingTerm + " Years");

        policyTerm = prsObj.parseXmlTag(input, "policyTerm");
        tv_basic_cover_term.setText(policyTerm + " Years");

        basicCoverYearlyPremium = ((int) Double.parseDouble(prsObj.parseXmlTag(
                input, "premiumAmount"))) + "";
        tv_basic_cover_yearly_premium.setText("Rs. " + basicCoverYearlyPremium);

        basicCoverSumAssured = (int) Double.parseDouble(prsObj.parseXmlTag(
                output, "sumAssured")) + "";
        tv_basic_cover_sum_assured.setText("Rs. "
                + getformatedThousandString(Integer
                .parseInt(basicCoverSumAssured)));

        premiumDetailFYBasicPremium = ((int) Double
                .parseDouble(basicCoverYearlyPremium)) + "";
        tv_premium_detail_basic_premium_first_year.setText("Rs. "
                + basicCoverYearlyPremium);

        premiumDetailFYServiceTax = ((int) Double.parseDouble(prsObj
                .parseXmlTag(output, "FYServTax"))) + "";

        System.out.println("premiumDetailFYServiceTax........................:"
                + premiumDetailFYServiceTax);
        tv_premium_detail_service_tax_first_year.setText("Rs. "
                + premiumDetailFYServiceTax);

        premiumDetailFYPremiumWithServiceTax = ((int) (Double
                .parseDouble(premiumDetailFYBasicPremium.equals("")
                        || (premiumDetailFYBasicPremium.equals(null)) ? "0"
                        : premiumDetailFYBasicPremium) + Double
                .parseDouble(premiumDetailFYServiceTax.equals("")
                        || (premiumDetailFYServiceTax.equals(null)) ? "0"
                        : premiumDetailFYServiceTax)))
                + "";
        tv_premium_detail_basic_premium_with_tax_first_year.setText("Rs. "
                + premiumDetailFYPremiumWithServiceTax);

        // Amit changes start- 23-5-2016
        String basicServiceTax = prsObj.parseXmlTag(output, "basicServiceTax");
        double basicServiceTaxFirstyear = Double.valueOf(basicServiceTax
                .equals("") ? "0" : basicServiceTax);
        double totalServiceTaxFirstYear = basicServiceTaxFirstyear;
        System.out
                .println("1 totalServiceTaxFirstYear........................:"
                        + totalServiceTaxFirstYear);
        tv_premium_detail_service_tax_first_year.setText("Rs. "
                + commonForAllProd.getRound(commonForAllProd
                .getStringWithout_E(basicServiceTaxFirstyear)));

        String SBCServiceTax = prsObj.parseXmlTag(output, "SBCServiceTax");

        double sbcServiceTaxDouble = Double
                .valueOf((SBCServiceTax == null || SBCServiceTax.equals("")) ? "0"
                        : SBCServiceTax);
        totalServiceTaxFirstYear += sbcServiceTaxDouble;

        tv_bi_smart_guaranteed_saving_plan_swachh_bharat_tax_first_year
                .setText("Rs. "
                        + commonForAllProd.getRound(commonForAllProd
                        .getStringWithout_E(sbcServiceTaxDouble)));

        String KKCServiceTax = prsObj.parseXmlTag(output, "KKCServiceTax");
        double KKCServiceTaxFirstYear = Double
                .valueOf((KKCServiceTax == null || KKCServiceTax.equals("")) ? "0"
                        : KKCServiceTax);

        totalServiceTaxFirstYear += KKCServiceTaxFirstYear;
        tv_bi_smart_guaranteed_saving_plan_krishi_kalyan_tax_first_year
                .setText("Rs. "
                        + commonForAllProd.getRound(commonForAllProd
                        .getStringWithout_E(KKCServiceTaxFirstYear)));

        tv_bi_smart_guaranteed_saving_plan_total_service_tax_first_year
                .setText("Rs. " + premiumDetailFYServiceTax);

        premiumDetailSYBasicPremium = ((int) Double
                .parseDouble(basicCoverYearlyPremium)) + "";
        tv_premium_detail_basic_premium_second_year.setText("Rs. "
                + basicCoverYearlyPremium);

        premiumDetailSYServiceTax = ((int) Double.parseDouble(prsObj
                .parseXmlTag(output, "SYServTax"))) + "";

        premiumDetailSYPremiumWithServiceTax = ((int) (Double
                .parseDouble((premiumDetailSYBasicPremium == null || premiumDetailSYBasicPremium
                        .equals("")) ? "0" : premiumDetailSYBasicPremium) + Double
                .parseDouble(premiumDetailSYServiceTax.equals("") ? "0"
                        : premiumDetailSYServiceTax)))
                + "";
        tv_premium_detail_basic_premium_with_tax_second_year.setText("Rs. "
                + premiumDetailSYPremiumWithServiceTax);

        // Amit changes start- 23-5-2016
        String basicServiceTaxSecondYear = prsObj.parseXmlTag(output,
                "basicServiceTaxSecondYear");

        double basicServiceTaxSecondyearDouble = Double
                .valueOf(basicServiceTaxSecondYear.equals("") ? "0"
                        : basicServiceTaxSecondYear);
        double totalServiceTaxSecondYear = basicServiceTaxSecondyearDouble;
        System.out
                .println("1 totalServiceTaxSecondYear........................:"
                        + totalServiceTaxSecondYear);
        tv_premium_detail_service_tax_second_year.setText("Rs. "
                + commonForAllProd.getRound(commonForAllProd
                .getStringWithout_E(basicServiceTaxSecondyearDouble)));

        String SBCServiceTaxSecondYear = prsObj.parseXmlTag(output,
                "SBCServiceTaxSecondYear");
        double SBCServiceTaxSecondYearDouble = Double
                .valueOf(SBCServiceTaxSecondYear.equals("") ? "0"
                        : SBCServiceTaxSecondYear);

        totalServiceTaxSecondYear += SBCServiceTaxSecondYearDouble;
        System.out
                .println("2 totalServiceTaxSecondYear........................:"
                        + totalServiceTaxSecondYear);
        tv_bi_smart_guaranteed_saving_plan_swachh_bharat_tax_second_year
                .setText("Rs. "
                        + commonForAllProd.getRound(commonForAllProd
                        .getStringWithout_E(SBCServiceTaxSecondYearDouble)));

        String KKCServiceTaxSecondYear = prsObj.parseXmlTag(output,
                "KKCServiceTaxSecondYear");
        double KKCServiceTaxSecondYearDouble = Double
                .valueOf(KKCServiceTaxSecondYear.equals("") ? "0"
                        : KKCServiceTaxSecondYear);
        totalServiceTaxSecondYear += KKCServiceTaxSecondYearDouble;

        System.out.println("totalServiceTaxSecondYear:"
                + totalServiceTaxSecondYear);

        tv_bi_smart_guaranteed_saving_plan_krishi_kalyan_tax_second_year
                .setText("Rs. "
                        + commonForAllProd.getRound(commonForAllProd
                        .getStringWithout_E(KKCServiceTaxSecondYearDouble)));
        // Amit changes end- 23-5-2016

        tv_bi_smart_guaranteed_saving_plan_total_service_tax_second_year
                .setText("Rs. "
                        + commonForAllProd.getRound(commonForAllProd
                        .getStringWithout_E(totalServiceTaxSecondYear)));

        String backdatingInt = prsObj.parseXmlTag(output, "backdateInt");

        tv_backdatingint.setText(" Rs. "
                + commonForAllProd.getRound(commonForAllProd
                .getStringWithout_E(Double.valueOf(backdatingInt
                        .equals("") ? "0" : backdatingInt))));

        TextView tv_Company_policy_surrender_dec = d
                .findViewById(R.id.tv_Company_policy_surrender_dec);

        Company_policy_surrender_dec = "Your SBI Life-Smart Guaranteed Savings Plan (UIN - 111N097V01) is a "
                + " Regular/Limited "
                + " premium policy, for which your first year yearly Premium is Rs. "
                + premiumDetailFYPremiumWithServiceTax
                + " .Your Policy Term is "
                + policyTerm
                + " years"
                + " .Your Premium Payment Term is "
                + premPayingTerm
                + " years"
                + " and Basic Sum Assured is Rs. " +

                basicCoverSumAssured;

        tv_Company_policy_surrender_dec.setText(Company_policy_surrender_dec);

        for (int i = 1; i <= Integer.parseInt(policyTerm); i++) {

            String end_of_year = prsObj
                    .parseXmlTag(output, "policyYr" + i + "");
            String yearly_basic_premium = ((int) Double.parseDouble(prsObj
                    .parseXmlTag(output, "totBasePremPaid" + i + ""))) + "";
            String cumulative_premium = ((int) Double.parseDouble(prsObj
                    .parseXmlTag(output, "cummulatvePrem" + i + ""))) + "";
            String guaranteed_addition = ((int) Double.parseDouble(prsObj
                    .parseXmlTag(output, "guarntdAddtn" + i + ""))) + "";
            String guaranteed_death_benefit = ((int) Double.parseDouble(prsObj
                    .parseXmlTag(output, "guarntdDeathBen" + i + ""))) + "";
            String guaranteed_maturity_benefit = ((int) Double
                    .parseDouble(prsObj.parseXmlTag(output,
                            "guarntdSurvivalBen" + i + "")))
                    + "";
            String guaranteed_surrender_value = ((int) Double
                    .parseDouble(prsObj.parseXmlTag(output, "guarntdSurrndrVal"
                            + i + "")))
                    + "";

            list_data.add(new M_BI_SmartGuaranteed_Saving_Plan_Adapter(
                    end_of_year, yearly_basic_premium, cumulative_premium,
                    guaranteed_addition, guaranteed_death_benefit,
                    guaranteed_maturity_benefit, guaranteed_surrender_value));
        }

        Adapter_BI_SmartGuaranteedSavingPlanGrid adapter = new Adapter_BI_SmartGuaranteedSavingPlanGrid(
                this, list_data);
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

    private void initialiseDate() {
        Calendar calender = Calendar.getInstance();
        mYear = calender.get(Calendar.YEAR);
        mMonth = calender.get(Calendar.MONTH);
        mDay = calender.get(Calendar.DAY_OF_MONTH);

    }

    private void Date() {
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        c.add(Calendar.DATE, 30);
    }


    // Store user input in Bean object
    private void addListenerOnSubmit() {
        smartGuranteedBean = new SmartGuaranteedSavingsPlanBean();
        // Insert data entered by user in an object

        smartGuranteedBean.setAge(Integer.parseInt(spnr_Age.getSelectedItem()
                .toString()));
        smartGuranteedBean.setGender(spnr_Gender.getSelectedItem().toString());
        smartGuranteedBean.setPolicyTerm(Integer.parseInt(spnrPolicyTerm
                .getSelectedItem().toString()));
        smartGuranteedBean.setPremPayingTerm(Integer
                .parseInt(spnrPremPayingTerm.getSelectedItem().toString()));
        smartGuranteedBean.setPremPayingMode(spnrPremPayingMode
                .getSelectedItem().toString());
        smartGuranteedBean.setPremiumAmt(Double.parseDouble(edt_premiumAmt
                .getText().toString()));

        // Show Smart Scholar Output Screen
        showSmartMoneyPlannerOutputPg(smartGuranteedBean);
    }

    private void getInput(SmartGuaranteedSavingsPlanBean smartguranteedBean) {
        inputVal = new StringBuilder();

        String LifeAssured_title = spnr_bi_smart_guaranteed_saving_plan_life_assured_title
                .getSelectedItem().toString();
        String LifeAssured_firstName = edt_bi_smart_guaranteed_saving_plan_life_assured_first_name
                .getText().toString();
        String LifeAssured_middleName = edt_bi_smart_guaranteed_saving_plan_life_assured_middle_name
                .getText().toString();
        String LifeAssured_lastName = edt_bi_smart_guaranteed_saving_plan_life_assured_last_name
                .getText().toString();
        String LifeAssured_DOB = btn_bi_smart_guaranteed_saving_plan_life_assured_date
                .getText().toString();
        String LifeAssured_age = spnr_Age.getSelectedItem().toString();

        String proposer_title = "";
        String proposer_firstName = "";
        String proposer_middleName = "";
        String proposer_lastName = "";
        String proposer_DOB = "";
        String proposer_age = "";
        String proposer_gender = "";

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
        int age = smartguranteedBean.getAge();
        String gender = smartguranteedBean.getGender();
        int policyTerm = smartguranteedBean.getPolicyTerm();
        double premAmount = smartguranteedBean.getPremiumAmount();
        String PremPayingMode = smartguranteedBean.getPremPayingMode();
        int premPayingTerm = smartguranteedBean.getPremPayingTerm();

        inputVal.append("<?xml version='1.0' encoding='utf-8' ?><smartguaranteedsavingplan>");
        inputVal.append("<LifeAssured_title>").append(LifeAssured_title).append("</LifeAssured_title>");
        inputVal.append("<age>").append(age).append("</age>");
        inputVal.append("<LifeAssured_firstName>").append(LifeAssured_firstName).append("</LifeAssured_firstName>");
        inputVal.append("<gender>").append(gender).append("</gender>");
        inputVal.append("<LifeAssured_middleName>").append(LifeAssured_middleName).append("</LifeAssured_middleName>");
        inputVal.append("<LifeAssured_lastName>").append(LifeAssured_lastName).append("</LifeAssured_lastName>");
        inputVal.append("<LifeAssured_DOB>").append(LifeAssured_DOB).append("</LifeAssured_DOB>");
        inputVal.append("<LifeAssured_age>").append(LifeAssured_age).append("</LifeAssured_age>");

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
        inputVal.append("<isJKResident>false</isJKResident>");
        inputVal.append("<isStaff>false</isStaff>");

        // inputVal.append("<gender>" + gender + "</gender>");

        inputVal.append("<policyTerm>").append(policyTerm).append("</policyTerm>");
        inputVal.append("<premFreq>").append(PremPayingMode).append("</premFreq>");
        inputVal.append("<premiumAmount>").append(premAmount).append("</premiumAmount>");
        inputVal.append("<premPayingTerm>").append(premPayingTerm).append("</premPayingTerm>");

        inputVal.append("<Wish_to_backdate_policy>").append(wish_to_backdate_flag).append("</Wish_to_backdate_policy>");
        inputVal.append("<backdating_Date>").append(backdate).append("</backdating_Date>");

        //Added Tushar Kadam Kerla Applicable Taxes 7-Jun-2019
        String str_kerla_discount = "N";
        if (cb_kerladisc.isChecked()) {
            str_kerla_discount = "Y";
        }

        inputVal.append("<KFC>" + str_kerla_discount + "</KFC>");
        //End Added Tushar Kadam Kerla Applicable Taxes 7-Jun-2019


        inputVal.append("</smartguaranteedsavingplan>");

    }

    /********************************** Output starts here **********************************************************/
    // Display Smart Scholar Output Screen
    private void showSmartMoneyPlannerOutputPg(
            SmartGuaranteedSavingsPlanBean SGSPbean) {

        SmartGuaranteedSavingsPlanBusinessLogic smartGuranteedBusinessBean = new SmartGuaranteedSavingsPlanBusinessLogic(
                SGSPbean);

        retVal = new StringBuilder();
        String[] outputArr = getOutput("BI_of_Smart_Guaranteed_Savings_Plan",
                SGSPbean);


        double sumAssured = smartGuranteedBean.getSumAssured();
        Double d = sumAssured;
        String minesOccuInterest = "" + smartGuranteedBusinessBean.getMinesOccuInterest(d.intValue());

        // Intent i = new
        // Intent(getApplicationContext(),com.sbilife.smartguaranteedsavingsplan.SmartGuaranteedSuccess.class);

        try {

            retVal.append("<?xml version='1.0' encoding='utf-8' ?><SmartGuaranteedSavingsPlan>");
            retVal.append("<errCode>0</errCode>");

            /*************** Modified by Akshaya on 05-FEB-2015 start **********/

            retVal.append("<yearlyPrem>").append(outputArr[0]).append("</yearlyPrem>").append("<FYServTax>").append(outputArr[1]).append("</FYServTax>").append("<FYPrem>").append(outputArr[2]).append("</FYPrem>").append("<SYServTax>").append(outputArr[3]).append("</SYServTax>").append("<SYPrem>").append(outputArr[4]).append("</SYPrem>").append("<sumAssured>").append(outputArr[5]).append("</sumAssured>").append("<GuarntdMatBeneft>").append(outputArr[6]).append("</GuarntdMatBeneft>").append("<installmntPrem>").append(outputArr[0]).append("</installmntPrem>").append("<SAMF>").append(outputArr[7]).append("</SAMF>").append("<backdateInt>").append(outputArr[8]).append("</backdateInt>").append("<basicServiceTax>").append(outputArr[9]).append("</basicServiceTax>").append("<SBCServiceTax>").append(outputArr[10]).append("</SBCServiceTax>").append("<KKCServiceTax>").append(outputArr[11]).append("</KKCServiceTax>").append("<basicServiceTaxSecondYear>").append(outputArr[12]).append("</basicServiceTaxSecondYear>").append("<SBCServiceTaxSecondYear>").append(outputArr[13]).append("</SBCServiceTaxSecondYear>").append("<KKCServiceTaxSecondYear>").append(outputArr[14]).append("</KKCServiceTaxSecondYear>").append(bussIll.toString());
            retVal.append("</SmartGuaranteedSavingsPlan>");

            /*************** Modified by Akshaya on 05-FEB-2015 end **********/

        } catch (Exception e) {
            retVal.append("<?xml version='1.0' encoding='utf-8' ?><SmartGuaranteedSavingsPlan>" + "<errCode>1</errCode>" + "<errorMessage>").append(e.getMessage()).append("</errorMessage></SmartGuaranteedSavingsPlan>");
        }

        /******************************************* xml Output *************************************/

        // System.out.println("Final output in xml" + retVal.toString());


        /******************************************* xml Output *************************************/

    }

    /****************************** Output ends here **********************************************************/
    /********************************** Calculations starts from here **********************************************************/

    private String[] getOutput(String sheetName,
                               SmartGuaranteedSavingsPlanBean SGSPbean) {
        bussIll = new StringBuilder();
        int _year_F = 0;

        int year_F = 0;

        double yearlyBasePremiumPaid = 0, GuaranteedDeathBenefit = 0,

                GuaranteedMaturityBenefit = 0, cummulativePremium = 0, GuaranteedAddition = 0, GuaranteedSurrenderValue = 0, cummulativeGuaranteedAddition = 0;

        // From GUI Input

        int age = SGSPbean.getAge();
        double premiumAmt = SGSPbean.getPremiumAmount();

        //
        SmartGuaranteedSavingsPlanBusinessLogic sgspBusinessLogic = new SmartGuaranteedSavingsPlanBusinessLogic(
                SGSPbean);

        /* modified by Akshaya on 20-MAY-16 start **/
        double basicServiceTax = sgspBusinessLogic.getServiceTax(premiumAmt,
                "basic");
        double SBCServiceTax = sgspBusinessLogic.getServiceTax(premiumAmt,
                "SBC");
        double KKCServiceTax = sgspBusinessLogic.getServiceTax(premiumAmt,
                "KKC");

        double fyServiceTax = basicServiceTax + SBCServiceTax + KKCServiceTax;

        double fyPremiumWithST = premiumAmt + fyServiceTax;

        // double fyPremiumWithST = Double.valueOf(sgspBusinessLogic
        // .setPremiumWithST(premiumAmt, prop.fyServiceTax));
        // double fyServiceTax = Double.valueOf(sgspBusinessLogic.setServiceTax(
        // premiumAmt, fyPremiumWithST));

        double basicServiceTaxSecondYear = sgspBusinessLogic
                .getServiceTaxSecondYear(premiumAmt, "basic");
        double SBCServiceTaxSecondYear = sgspBusinessLogic
                .getServiceTaxSecondYear(premiumAmt, "SBC");
        double KKCServiceTaxSecondYear = sgspBusinessLogic
                .getServiceTaxSecondYear(premiumAmt, "KKC");

        double syServiceTax = basicServiceTaxSecondYear
                + SBCServiceTaxSecondYear + KKCServiceTaxSecondYear;

        double syPremiumWithST = premiumAmt + syServiceTax;

        // double syPremiumWithST = Double.valueOf(sgspBusinessLogic
        // .setPremiumWithST(premiumAmt, prop.syServiceTax));
        // double syServiceTax = Double.valueOf(sgspBusinessLogic.setServiceTax(
        // premiumAmt, syPremiumWithST));

        /*************** Modified by Akshaya on 05-FEB-2015 start **********/
        double SAMFRate = sgspBusinessLogic.setSAMFRate(age);
        double sumAssured = Double.valueOf(sgspBusinessLogic.setSumAssured(
                premiumAmt, SAMFRate));

        /*************** Modified by Akshaya on 05-FEB-2015 end **********/

        // System.out.println(fyPremiumWithST+" "+fyServiceTax+"  "+syPremiumWithST+"  "+syServiceTax
        // + " "+sumAssured);
        int rowNumber = 0, j = 0;
        try {
            for (int i = 0; i < prop.policyTerm; i++) {
                rowNumber++;

                year_F = rowNumber;
                _year_F = year_F;
                // System.out.println("1. year_F "+year_F);
                bussIll.append("<policyYr").append(_year_F).append(">").append(_year_F).append("</policyYr").append(_year_F).append(">");

                yearlyBasePremiumPaid = sgspBusinessLogic
                        .setYearlyBasePremiumPaid(year_F, premiumAmt);
                // System.out.println("2.Total Base Premium Paid "+yearlyBasePremiumPaid);
                bussIll.append("<totBasePremPaid").append(_year_F).append(">").append(yearlyBasePremiumPaid).append("</totBasePremPaid").append(_year_F).append(">");

                cummulativePremium = cummulativePremium + yearlyBasePremiumPaid;
                // System.out.println("3.cummulativePremium "+cummulativePremium);
                bussIll.append("<cummulatvePrem").append(_year_F).append(">").append(cummulativePremium).append("</cummulatvePrem").append(_year_F).append(">");

                GuaranteedAddition = sgspBusinessLogic.setGuaranteedAddition(
                        premiumAmt, cummulativePremium);
                cummulativeGuaranteedAddition = cummulativeGuaranteedAddition
                        + GuaranteedAddition;
                // System.out.println("4.Non Guarateed Death Benefit At_8_Percent "+
                // GuaranteedAddition);
                bussIll.append("<guarntdAddtn").append(_year_F).append(">").append(GuaranteedAddition).append("</guarntdAddtn").append(_year_F).append(">");

                GuaranteedDeathBenefit = sgspBusinessLogic
                        .setGuaranteedDeathBenefit(sumAssured, premiumAmt,
                                cummulativeGuaranteedAddition,
                                cummulativePremium);
                // System.out.println("5.Guaranteed Death Benefit "+GuaranteedDeathBenefit);
                bussIll.append("<guarntdDeathBen").append(_year_F).append(">").append(GuaranteedDeathBenefit).append("</guarntdDeathBen").append(_year_F).append(">");

                GuaranteedMaturityBenefit = sgspBusinessLogic
                        .setGuaranteedMaturityBenefit(sumAssured,
                                cummulativeGuaranteedAddition, year_F);
                // System.out.println("6.Guaranteed Survival Benefit "+GuaranteedMaturityBenefit);
                bussIll.append("<guarntdSurvivalBen").append(_year_F).append(">").append(GuaranteedMaturityBenefit).append("</guarntdSurvivalBen").append(_year_F).append(">");

                GuaranteedSurrenderValue = Math.round(sgspBusinessLogic
                        .setGuaranteedSurrenderValue(
                                cummulativeGuaranteedAddition,
                                cummulativePremium, year_F));
                // System.out.println("7.Guaranteed Surrender Value "+GuaranteedSurrenderValue);
                bussIll.append("<guarntdSurrndrVal").append(_year_F).append(">").append(GuaranteedSurrenderValue).append("</guarntdSurrndrVal").append(_year_F).append(">");

            }

            /************************** Added by Akshaya on 03-MAR-15 start **********************/

            if (rb_proposerdetail_personaldetail_backdating_yes.isChecked()) {
                // "16-jan-2014")));
                BackDateinterest = commonForAllProd.getRoundUp(""
                        + (sgspBusinessLogic.getBackDateInterest(
                        fyPremiumWithST,
                        btn_proposerdetail_personaldetail_backdatingdate
                                .getText().toString())));

                BackDateinterestwithGST = commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(
                        Double.parseDouble(BackDateinterest) + (Double.parseDouble(BackDateinterest) * prop.GSTforbackdate)));
            } else {
                BackDateinterestwithGST = "0";
            }

            fyPremiumWithST = fyPremiumWithST
                    + Double.parseDouble(BackDateinterestwithGST);
            /************************** Added by Akshaya on 03-MAR-15 end **********************/

        } catch (Exception e) {
            e.printStackTrace();
        }

        /** modified by Akshaya on 20-MAY-16 start ****/
        return new String[]{
                (commonForAllProd.getStringWithout_E(premiumAmt)),
                commonForAllProd.getStringWithout_E(fyServiceTax),
                commonForAllProd.getStringWithout_E(fyPremiumWithST),
                commonForAllProd.getStringWithout_E(syServiceTax),
                commonForAllProd.getStringWithout_E(syPremiumWithST),
                commonForAllProd.getStringWithout_E(sumAssured),
                commonForAllProd.getStringWithout_E(GuaranteedMaturityBenefit),
                commonForAllProd.getStringWithout_E(SAMFRate),
                BackDateinterestwithGST,
                commonForAllProd.getStringWithout_E(basicServiceTax),
                commonForAllProd.getStringWithout_E(SBCServiceTax),
                commonForAllProd.getStringWithout_E(KKCServiceTax),
                commonForAllProd.getStringWithout_E(basicServiceTaxSecondYear),
                commonForAllProd.getStringWithout_E(SBCServiceTaxSecondYear),
                commonForAllProd.getStringWithout_E(KKCServiceTaxSecondYear)};

    }

    /********************************** Validations starts here **********************************************************/

    // maturity age of policy is 50 years
    private boolean valMaturityAge() {
        int Age = Integer.parseInt(spnr_Age.getSelectedItem().toString());
        int PolicyTerm = Integer.parseInt(spnrPolicyTerm.getSelectedItem()
                .toString());
        if ((Age + PolicyTerm) > 65) {
            showAlert.setMessage("Maturity age is 65 years");
            showAlert.setNeutralButton("OK",
                    new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            // apply focusable method
                            setFocusable(btn_bi_smart_guaranteed_saving_plan_life_assured_date);
                            btn_bi_smart_guaranteed_saving_plan_life_assured_date
                                    .requestFocus();
                        }
                    });
            showAlert.show();

            return false;

        } else
            return true;

    }

    // Premium Amount Validation
    private boolean valPremiumAmt() {
        String error = "";
        if (edt_premiumAmt.getText().toString().equals("")) {
            error = "Please enter Premium Amount in Rs. ";

        } else if (Integer.parseInt(edt_premiumAmt.getText().toString()) > prop.maxPremiumAmt) {
            error = "Premium Amount should not be greater than Rs. "
                    + currencyFormat.format(prop.maxPremiumAmt);

        } else if (Integer.parseInt(edt_premiumAmt.getText().toString()) < prop.minPremiumAmt) {
            error = "Premium Amount should not be less than Rs. "
                    + currencyFormat.format(prop.minPremiumAmt);

        } else {
            if (!(Double.parseDouble(edt_premiumAmt.getText().toString()) % 1000 == 0)) {
                error = "Premium Amount should be multiple of 1000";

            }
        }
        if (!error.equals("")) {
            showAlert(error);
            setFocusable(edt_premiumAmt);
            edt_premiumAmt.requestFocus();
            return false;
        } else
            return true;

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
                                    setFocusable(spnr_bi_smart_guaranteed_saving_plan_life_assured_title);
                                    spnr_bi_smart_guaranteed_saving_plan_life_assured_title
                                            .requestFocus();
                                } else if (lifeAssured_First_Name.equals("")) {
                                    setFocusable(edt_bi_smart_guaranteed_saving_plan_life_assured_first_name);
                                    edt_bi_smart_guaranteed_saving_plan_life_assured_first_name
                                            .requestFocus();
                                } else {
                                    setFocusable(edt_bi_smart_guaranteed_saving_plan_life_assured_last_name);
                                    edt_bi_smart_guaranteed_saving_plan_life_assured_last_name
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
                                setFocusable(btn_bi_smart_guaranteed_saving_plan_life_assured_date);
                                btn_bi_smart_guaranteed_saving_plan_life_assured_date
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

    /********************************** Validations ends here **********************************************************/
    // Alert Dialog Box
    private void showAlert(String error) {
        showAlert.setMessage(error);
        showAlert.setNeutralButton("OK", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
            }
        });
        showAlert.show();
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
                        if (18 <= age && age <= 50) {

                            btn_bi_smart_guaranteed_saving_plan_life_assured_date
                                    .setText(date);

                            spnr_Age.setSelection(getIndex(spnr_Age, final_age),
                                    false);
                            valMaturityAge();
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

                            clearFocusable(btn_bi_smart_guaranteed_saving_plan_life_assured_date);

                            setFocusable(edt_proposerdetail_basicdetail_contact_no);
                            edt_proposerdetail_basicdetail_contact_no
                                    .requestFocus();
                            /*
                             * setFocusable(edt_premiumAmt);
                             * edt_premiumAmt.requestFocus();
                             */
                        } else {
                            commonMethods.BICommonDialog(context,
                                    "Minimum Age should be 18 yrs and Maximum Age should be 50 yrs For LifeAssured");
                            btn_bi_smart_guaranteed_saving_plan_life_assured_date
                                    .setText("Select Date");
                            lifeAssured_date_of_birth = "";

                            clearFocusable(btn_bi_smart_guaranteed_saving_plan_life_assured_date);
                            setFocusable(btn_bi_smart_guaranteed_saving_plan_life_assured_date);

                            btn_bi_smart_guaranteed_saving_plan_life_assured_date
                                    .requestFocus();
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
            spnr_Age.setSelection(getIndex(spnr_Age, str_final_age), false);
            valMaturityAge();

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
        if (!na_dob.equals("") && flag == 0) {
            System.out.println(" na_dob : " + na_dob);
            initialiseDateParameter(na_dob, 35);
            DIALOG_ID = 5;
            updateDisplay(DIALOG_ID);
            flag = 1;
        } else {
            initialiseDateParameter(lifeAssured_date_of_birth, 35);
            DIALOG_ID = 4;
            showDialog(DATE_DIALOG_ID);
        }
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

    private void windowmessagesgin() {

        d = new Dialog(BI_SmartGuaranteedSavingsPlanActivity.this);
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
                Intent intent = new Intent(
                        BI_SmartGuaranteedSavingsPlanActivity.this,
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
        if (v.getId() == edt_bi_smart_guaranteed_saving_plan_life_assured_first_name
                .getId()) {
            setFocusable(edt_bi_smart_guaranteed_saving_plan_life_assured_middle_name);
            edt_bi_smart_guaranteed_saving_plan_life_assured_middle_name
                    .requestFocus();
        } else if (v.getId() == edt_bi_smart_guaranteed_saving_plan_life_assured_middle_name
                .getId()) {
            setFocusable(edt_bi_smart_guaranteed_saving_plan_life_assured_last_name);
            edt_bi_smart_guaranteed_saving_plan_life_assured_last_name
                    .requestFocus();
        } else if (v.getId() == edt_bi_smart_guaranteed_saving_plan_life_assured_last_name
                .getId()) {
            setFocusable(btn_bi_smart_guaranteed_saving_plan_life_assured_date);
            btn_bi_smart_guaranteed_saving_plan_life_assured_date
                    .requestFocus();
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
            // clearFocusable(edt_premiumAmt);
            setFocusable(edt_premiumAmt);
            edt_premiumAmt.requestFocus();
        } else if (v.getId() == edt_premiumAmt.getId()) {
            commonMethods.hideKeyboard(edt_premiumAmt, context);

            /*
             * clearFocusable(edt_premiumAmt);
             * setFocusable(rb_proposerdetail_personaldetail_backdating_yes);
             * rb_proposerdetail_personaldetail_backdating_yes.requestFocus();
             */

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
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] bitMapData = stream.toByteArray();
            Image image = Image.getInstance(bitMapData);
            image.scalePercent(50f);
            image.setAlignment(Element.ALIGN_LEFT);

            document.open();

            PdfPTable table = new PdfPTable(3);
            table.setWidths(new float[]{2.5f, 8.5f, 2f});
            table.setWidthPercentage(100);
            table.getDefaultCell().setPadding(15);

            PdfPCell cell;

            cell = new PdfPCell(image);
            cell.setRowspan(4);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("SBI Life Insurance Co. Ltd",
                    headerBold));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("\n", headerBold));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setRowspan(4);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(
                    new Phrase(
                            "Corporate Office: 'Natraj', M.V. Road and Western Express, Highway Junction, Andheri (East),",
                            headerBold));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);

            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Mumbai  400069.. Regn No. 111",
                    headerBold));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);

            table.addCell(cell);

            cell = new PdfPCell(
                    new Phrase(
                            "SBI Life-Smart Guaranteed Savings Plan (UIN - 111N097V01)",
                            headerBold));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);

            table.addCell(cell);

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
                    "Currently, the two rates of investment return as specified by IRDAI are 4% and 8% per annum.",
                    normal1);
            para5.setAlignment(Element.ALIGN_LEFT | Element.ALIGN_JUSTIFIED);
            Paragraph para6 = new Paragraph(
                    "The main objective of the illustration is that the client is able to appreciate the features of the product and the flow of benefits in different circumstances with some level of quantification. For further information on the product and its benefits, please refer to the sales brochure and/or policy document. Further information will also be available on request.",
                    normal1);
            para6.setAlignment(Element.ALIGN_LEFT | Element.ALIGN_JUSTIFIED);
            Paragraph new_line = new Paragraph("\n");

            Paragraph para7 = new Paragraph("Statutory Warning", small_bold);
            para3.setAlignment(Element.ALIGN_LEFT);
            Paragraph para8 = new Paragraph(
                    "\"Some benefits are guaranteed and some benefits are variable with returns based on the future performance of your Insurer carrying on life insurance business. If your policy offers guaranteed returns then these will be clearly marked guaranteed in the illustration table on this page. If your policy offers variable returns then the illustrations on this page will show two different rates of assumed future investment returns. These assumed rates of return are not guaranteed and they are not the upper or lower limits of what you might get back, as the value of your policy is dependent on a number of factors including future investment performance.\"",
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

            // inputTable here -1

            PdfPTable personalDetail_table = new PdfPTable(4);
            personalDetail_table.setWidths(new float[]{5f, 5f, 5f, 5f});
            personalDetail_table.setWidthPercentage(100f);
            personalDetail_table.setHorizontalAlignment(Element.ALIGN_LEFT);

            // 1st row
            cell = new PdfPCell(new Phrase(
                    "Personal Details of Life to be Assured ", normal1_bold));
            cell.setColspan(4);
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            personalDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase("Name", normal1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            personalDetail_table.addCell(cell);

            // 2 row
            cell = new PdfPCell(new Phrase("Age(last birthday) ", normal1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            personalDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase(" Gender", normal1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            personalDetail_table.addCell(cell);

            // 3 row
            cell = new PdfPCell(new Phrase("Premium Payment Frequency ",
                    normal1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            personalDetail_table.addCell(cell);

            if (proposer_Is_Same_As_Life_Assured.equalsIgnoreCase("y")) {

                cell = new PdfPCell(new Phrase(name_of_life_assured, normal1));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                personalDetail_table.addCell(cell);

            } else if (proposer_Is_Same_As_Life_Assured.equalsIgnoreCase("n")) {
                cell = new PdfPCell(new Phrase(name_of_proposer, normal1));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                personalDetail_table.addCell(cell);
            }

            // cell = new PdfPCell(new Phrase("",normal1));
            // cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            // personalDetail_crtable.addCell(cell);

            // 4 row
            cell = new PdfPCell(new Phrase(ageAtEntry + "", normal1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            personalDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase(gender + "", normal1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);

            personalDetail_table.addCell(cell);

            // 5 row
            cell = new PdfPCell(
                    new Phrase(prop.premiumPayingFreq + "", normal1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            personalDetail_table.addCell(cell);

            // Basic Cover

            PdfPTable basicCover_table = new PdfPTable(4);
            basicCover_table.setWidths(new float[]{5f, 5f, 5f, 5f});
            basicCover_table.setWidthPercentage(100f);
            basicCover_table.setHorizontalAlignment(Element.ALIGN_LEFT);

            // 1st row
            cell = new PdfPCell(new Phrase("Basic Cover", normal1_bold));
            cell.setColspan(4);
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            basicCover_table.addCell(cell);

            cell = new PdfPCell(new Phrase("Term (in years)", normal1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            basicCover_table.addCell(cell);

            // 2 row
            cell = new PdfPCell(new Phrase("Premium Payment Term (in years)",
                    normal1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            basicCover_table.addCell(cell);

            cell = new PdfPCell(new Phrase("Sum Assured (in Rs.)", normal1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            basicCover_table.addCell(cell);

            // 3 row
            cell = new PdfPCell(new Phrase("Yearly Premium (in Rs.)", normal1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            basicCover_table.addCell(cell);

            cell = new PdfPCell(new Phrase(prop.policyTerm + "", normal1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            basicCover_table.addCell(cell);

            // 4 row
            cell = new PdfPCell(
                    new Phrase(prop.premiumPayingTerm + "", normal1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            basicCover_table.addCell(cell);

            cell = new PdfPCell(new Phrase((currencyFormat.format(Double
                    .parseDouble(basicCoverSumAssured))) + "", normal1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            basicCover_table.addCell(cell);

            // 5 row
            cell = new PdfPCell(new Phrase((currencyFormat.format(Double
                    .parseDouble(basicCoverYearlyPremium))) + "", normal1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            basicCover_table.addCell(cell);

            // Premium Details

            PdfPTable premDetail_table = new PdfPTable(4);
            premDetail_table.setWidths(new float[]{5f, 5f, 5f, 5f});
            premDetail_table.setWidthPercentage(100f);
            premDetail_table.setHorizontalAlignment(Element.ALIGN_LEFT);

            // 1st row
            cell = new PdfPCell(new Phrase("Premium details", normal1_bold));
            cell.setColspan(7);
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            premDetail_table.addCell(cell);

            // 2 row
            cell = new PdfPCell(new Phrase("Policy Year", normal1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            premDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase("Basic Premium (in Rs.)", normal1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            premDetail_table.addCell(cell);

            /*
             * cell = new PdfPCell(new Phrase("(a)Basic Applicable Taxes(Rs.)",
             * small_normal));
             * cell.setHorizontalAlignment(Element.ALIGN_CENTER);
             * premDetail_table.addCell(cell);
             *
             * cell = new PdfPCell(new Phrase("(b)Swachh Bharat Cess(Rs.)",
             * small_normal));
             * cell.setHorizontalAlignment(Element.ALIGN_CENTER);
             * premDetail_table.addCell(cell);
             *
             * cell = new PdfPCell(new Phrase("(c)Krishi Kalyan Cess(Rs.)",
             * small_normal));
             * cell.setHorizontalAlignment(Element.ALIGN_CENTER);
             * premDetail_table.addCell(cell);
             */

            cell = new PdfPCell(new Phrase("Applicable Taxes (in Rs.)", normal1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            premDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase("Premium with Applicable Applicable Taxes (in Rs.)",
                    normal1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            premDetail_table.addCell(cell);

            // 3 row
            cell = new PdfPCell(new Phrase("First year", normal1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            premDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase((currencyFormat.format(Double
                    .parseDouble(premiumDetailFYBasicPremium))) + "", normal1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            premDetail_table.addCell(cell);

            // cell = new PdfPCell(new Phrase(
            // commonForAllProd.getRound(commonForAllProd
            // .getStringWithout_E(Double.valueOf(basicServiceTax
            // .equals("") ? "0" : basicServiceTax))),
            // small_normal));
            // cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            // premDetail_table.addCell(cell);
            //
            // cell = new PdfPCell(new Phrase(
            // commonForAllProd.getRound(commonForAllProd
            // .getStringWithout_E(Double.valueOf(SBCServiceTax
            // .equals("") ? "0" : SBCServiceTax))),
            // small_normal));
            // cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            // premDetail_table.addCell(cell);
            //
            // cell = new PdfPCell(new Phrase(
            // commonForAllProd.getRound(commonForAllProd
            // .getStringWithout_E(Double.valueOf(KKCServiceTax
            // .equals("") ? "0" : KKCServiceTax))),
            // small_normal));
            // cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            // premDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase((currencyFormat.format(Double
                    .parseDouble(premiumDetailFYServiceTax))) + "", normal1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);

            premDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase((currencyFormat.format(Double
                    .parseDouble(premiumDetailFYPremiumWithServiceTax))) + "",
                    normal1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            premDetail_table.addCell(cell);

            // 4 row

            cell = new PdfPCell(new Phrase("Second year onwards", normal1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            premDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase((currencyFormat.format(Double
                    .parseDouble(premiumDetailSYBasicPremium))) + "", normal1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            premDetail_table.addCell(cell);

            // cell = new PdfPCell(new Phrase(
            // commonForAllProd.getRound(commonForAllProd
            // .getStringWithout_E(Double
            // .valueOf(basicServiceTaxSecondYear
            // .equals("") ? "0"
            // : basicServiceTaxSecondYear))),
            // small_normal));
            // cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            // premDetail_table.addCell(cell);
            //
            // cell = new PdfPCell(
            // new Phrase(
            // commonForAllProd
            // .getRound(commonForAllProd.getStringWithout_E(Double
            // .valueOf(SBCServiceTaxSecondYear
            // .equals("") ? "0"
            // : SBCServiceTaxSecondYear))),
            // small_normal));
            // cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            // premDetail_table.addCell(cell);
            //
            // cell = new PdfPCell(
            // new Phrase(
            // commonForAllProd
            // .getRound(commonForAllProd.getStringWithout_E(Double
            // .valueOf(KKCServiceTaxSecondYear
            // .equals("") ? "0"
            // : KKCServiceTaxSecondYear))),
            // small_normal));
            // cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            // premDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase((currencyFormat.format(Double
                    .parseDouble(premiumDetailSYServiceTax))) + "", normal1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);

            premDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase((currencyFormat.format(Double
                    .parseDouble(premiumDetailSYPremiumWithServiceTax))) + "",
                    normal1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            premDetail_table.addCell(cell);

			/*PdfPTable Table_backdating_premium_with_service_tax = new PdfPTable(
					2);
			Table_backdating_premium_with_service_tax.setWidthPercentage(100);

			PdfPCell cell_Backdate1 = new PdfPCell(new Paragraph(
					"Backdating Interest", small_normal));
			cell_Backdate1.setPadding(5);
			PdfPCell cell_Backdate2 = new PdfPCell(new Paragraph("Rs."
					+ commonForAllProd.getRound(commonForAllProd
							.getStringWithout_E(Double.valueOf(BackdatingInt
									.equals("") ? "0" : BackdatingInt))),
					small_bold));
			cell_Backdate2.setPadding(5);
			cell_Backdate2.setHorizontalAlignment(Element.ALIGN_CENTER);

			Table_backdating_premium_with_service_tax.addCell(cell_Backdate1);
			Table_backdating_premium_with_service_tax.addCell(cell_Backdate2);*/

            Paragraph note = new Paragraph("Please Note: ",
                    normal1_BoldUnderline);
            para3.setAlignment(Element.ALIGN_LEFT);
            Paragraph note_1 = new Paragraph(
                    "1. The premiums can be paid by giving standing instruction to your bank or you can pay through your credit card (Visa and Master Card).",
                    normal1);
            para4.setAlignment(Element.ALIGN_LEFT | Element.ALIGN_JUSTIFIED);

            Paragraph termsCondition = new Paragraph(
                    "Other Terms and Conditions", normal1_bold);
            para3.setAlignment(Element.ALIGN_LEFT);
            Paragraph terms_1 = new Paragraph(
                    "1. The benefit calculation is based on the age herein indicated and as applicable for healthy individual.",
                    normal1);
            para4.setAlignment(Element.ALIGN_LEFT | Element.ALIGN_JUSTIFIED);
            Paragraph terms_2 = new Paragraph(
                    "2. The Survival/ Death Benefit amount are derived on the assumption that the policies are \"in-force\".",
                    normal1);
            para4.setAlignment(Element.ALIGN_LEFT | Element.ALIGN_JUSTIFIED);
			/*Paragraph terms_3 = new Paragraph(
					"3. Insurance is subject matter of solicitation.", normal1);
			para4.setAlignment(Element.ALIGN_LEFT | Element.ALIGN_JUSTIFIED);*/

            // Table here

            PdfPTable table1 = new PdfPTable(7);
            table1.setWidths(new float[]{4f, 4f, 4f, 4f, 4f, 4f, 4f});
            table1.setWidthPercentage(100);

            // 1st row
            cell = new PdfPCell(
                    new Phrase(
                            "BENEFIT ILLUSTRATION FOR SBI Life - Smart Guaranteed Savings Plan",
                            normal1_bold));
            cell.setColspan(7);
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table1.addCell(cell);

            // 2nd row
            cell = new PdfPCell(new Phrase("End of Year", normal_bold));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);

            cell = new PdfPCell(new Phrase("Yearly Basic Premium paid (Rs.)",
                    normal_bold));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);

            cell = new PdfPCell(new Phrase("Cumulative Premiums paid",
                    normal_bold));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);
            cell = new PdfPCell(new Phrase("Guaranteed additions", normal_bold));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);
            cell = new PdfPCell(new Phrase("Guaranteed Death Benefit",
                    normal_bold));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);
            cell = new PdfPCell(new Phrase("Guaranteed Maturity Benefit",
                    normal_bold));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);
            cell = new PdfPCell(new Phrase("Guaranteed Surrender Value (GSV)",
                    normal_bold));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
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
                                i).getYearly_basic_premium())), normal));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                        | Rectangle.BOTTOM);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table1.addCell(cell);
                cell = new PdfPCell(new Phrase(
                        currencyFormat.format(Double.parseDouble(list_data.get(
                                i).getCumulative_premium())), normal));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                        | Rectangle.BOTTOM);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table1.addCell(cell);
                cell = new PdfPCell(new Phrase(
                        currencyFormat.format(Double.parseDouble(list_data.get(
                                i).getGuaranteed_addition())), normal));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                        | Rectangle.BOTTOM);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table1.addCell(cell);
                cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                        .parseDouble(list_data.get(i)
                                .getGuaranteed_death_benefit())), normal));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                        | Rectangle.BOTTOM);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table1.addCell(cell);

                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                        .parseDouble(list_data.get(i)
                                .getGuaranteed_maturity_benefit())), normal));
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
                table1.addCell(cell);
            }

            Paragraph surrender = new Paragraph("Surrender Value", small_bold);
            para3.setAlignment(Element.ALIGN_LEFT);
            Paragraph surrender_value = new Paragraph(
                    "Surrender value is available if at least 2 full policy years� premiums have been paid.",
                    normal1);
            para4.setAlignment(Element.ALIGN_LEFT | Element.ALIGN_JUSTIFIED);

            Paragraph guarnteedSurrender = new Paragraph(
                    "Guaranteed Surrender Value", small_bold);
            para3.setAlignment(Element.ALIGN_LEFT);
            Paragraph guarnteedSurrender_value = new Paragraph(
                    "The Guaranteed Surrender Value (GSV) will be equal to GSV factors multiplied by the basic premiums paid. Basic premium is equal to total premium less Goods & Services Tax (Applicable Taxes). Surrender value of guaranteed additions is also added to this GSV.",
                    normal1);
            para4.setAlignment(Element.ALIGN_LEFT | Element.ALIGN_JUSTIFIED);

            Paragraph specialSurrender = new Paragraph(
                    "Special Surrender Value", small_bold);
            para3.setAlignment(Element.ALIGN_LEFT);
            Paragraph specialSurrender_value = new Paragraph(
                    "The Special Surrender Value (SSV) are non guaranteed and will be equal to the SSV factors multiplied by the Paid-up Value on maturity. The Paid-up Value on maturity is equal to paid-up sum assured on maturity together with accrued guaranteed additions.",
                    normal1);
            para4.setAlignment(Element.ALIGN_LEFT | Element.ALIGN_JUSTIFIED);

            Paragraph policySurrender = new Paragraph(
                    "Company�s Policy on Surrender", small_bold);
            para3.setAlignment(Element.ALIGN_LEFT);
            Paragraph policySurrender_value = new Paragraph(
                    "In practice, the company may pay a surrender value which could be higher than the guaranteed surrender value.  The surrender value payable may be reviewed from time to time depending on company's experience of the various factors which impact the surrender values that may be paid. The surrender value would be higher of GSV or SSV.",
                    normal1);
            para4.setAlignment(Element.ALIGN_LEFT | Element.ALIGN_JUSTIFIED);

            /*
             * Paragraph para9 = new Paragraph(
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

            // document.add(ls);
            document.add(para3);
            document.add(para4);
            document.add(para5);
            document.add(para6);
            document.add(para7);
            document.add(para8);

            document.add(new_line);

            document.add(table_proposer_name);
            document.add(new_line);
            // document.add(main_table);
            document.add(personalDetail_table);
            document.add(basicCover_table);
            document.add(premDetail_table);
            document.add(new_line);
            //document.add(Table_backdating_premium_with_service_tax);
            document.add(new_line);
            document.add(note);
            document.add(note_1);

            document.add(new_line);
            document.add(termsCondition);
            document.add(terms_1);
            document.add(terms_2);
            //document.add(terms_3);

            document.add(new_line);

            document.add(table1);

            document.add(new_line);
            document.add(surrender);
            document.add(surrender_value);

            document.add(new_line);
            document.add(guarnteedSurrender);
            document.add(guarnteedSurrender_value);

            document.add(new_line);
            document.add(specialSurrender);
            document.add(specialSurrender_value);

            document.add(new_line);
            document.add(policySurrender);
            document.add(policySurrender_value);

            document.add(new_line);

            ///Start BI Change
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

            document.add(new_line);
            //End BI Change

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
                // document.add(table4);
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
