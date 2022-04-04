package sbilife.com.pointofsale_bancaagency.smartmoneyplanner;

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
import com.itextpdf.text.Chunk;
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

@SuppressWarnings("deprecation")
public class BI_SmartMoneyPlannerActivity extends AppCompatActivity implements
        OnEditorActionListener {

    private NeedAnalysisBIService NABIObj;
    private NA_CBI_bean na_cbi_bean;
    private File needAnalysispath, newFile;
    private String agentcode, agentMobile, agentEmail, userType;
    private int needAnalysis_flag = 0;
    private String na_input = null;
    private String na_output = null;
    private DatabaseHelper dbHelper;
    private int flag = 0;
    private final double GuaranteedSurvivalBenefitPremCalci = 0;
    private final double NonGuarateedSurvivalBenefitAt_4_PercentPremCalci = 0;
    private final double NonGuarateedSurvivalBenefitAt_8_PercentPremCalci = 0;

    private String na_dob = "";
    private ArrayAdapter<String> genderAdapter;
    // private ScrollView sv_bi_smart_money_planner_main;
    private CheckBox cb_staffdisc, cb_bi_smart_money_planner_JKResident;

    private EditText edt_bi_smart_money_planner_life_assured_first_name,
            edt_bi_smart_money_planner_life_assured_middle_name,
            edt_bi_smart_money_planner_life_assured_last_name,
            edt_bi_smart_money_planner_life_assured_age,
            edt_smart_money_planner_contact_no,
            edt_smart_money_planner_Email_id,
            edt_smart_money_planner_ConfirmEmail_id,
            edt_bi_smart_money_planner_sum_assured_amount;

    private Spinner spnr_bi_smart_money_planner_life_assured_title,
            spnr_bi_smart_money_planner_selGender,
            spnr_bi_smart_money_planner_plan,
            spnr_bi_smart_money_planner_policyterm,
            spnr_bi_smart_money_planner_permium_payingterm,
            spnr_bi_smart_money_planner_premium_frequency;
    private TableRow tr_premium_paying_term;

    private Button btn_bi_smart_money_planner_life_assured_date;
    private Button btnBack;
    private Button btnSubmit;

    private Button btn_MarketingOfficalDate;
    private Button btn_PolicyholderDate;
    private ImageButton Ibtn_signatureofMarketing;
    private ImageButton Ibtn_signatureofPolicyHolders;

    private String QuatationNumber = "";
    private String planName = "";
    private AlertDialog.Builder showAlert;

    // newDBHelper db;
    private ParseXML prsObj;

    private String lifeAssured_Title = "";
    private String lifeAssured_First_Name = "";
    private String lifeAssured_Middle_Name = "";
    private String lifeAssured_Last_Name = "";
    private String name_of_life_assured = "";
    private String lifeAssured_date_of_birth = "";
    private String lifeAssuredAge = "";

    private String emailId = "";
    private String mobileNo = "";
    private String ConfirmEmailId = "";
    private String ProposerEmailId = "";
    private boolean validationFla1 = false;
    private SmartMoneyPlannerBean smartMoneyPlannerBean;
    private CommonForAllProd obj;
    private StringBuilder retVal;
    private StringBuilder inputVal;
    private DecimalFormat currencyFormat;

    private String name_of_proposer = "";
    private String name_of_person = "";
    private String place2 = "";
    private String date1 = "";
    private String date2 = "";
    private String agent_sign = "";
    private String proposer_sign = "";
    private String proposer_Backdating_WishToBackDate_Policy = "";
    private String proposer_Backdating_BackDate = "";

    private final String proposer_Is_Same_As_Life_Assured = "Y";

    private StringBuilder bussIll = null;
    private Dialog d;
    private SmartMoneyPlannerProperties prop;
    private final int SIGNATURE_ACTIVITY = 1;
    private String latestImage = "";
    private Bitmap photoBitmap;
    private String output = "";
    private final int DATE_DIALOG_ID = 1;
    private int DIALOG_ID;
    private int mYear;
    private int mMonth;
    private int mDay;

    private String age_entry = "";
    private String gender = "";
    private String premium_paying_frequency = "";
    private String plan = "";
    private String policy_term = "";
    private String premium_paying_term = "";
    private String sum_assured = "";
    private String sum_assured_on_death = "";
    private String basicprem = "";
    private String servicetax = "";
    private String basicplustax = "";
    private String BackdatingInt = "";

    private String premPayingMode = "";
    private String policyTerm = "";
    private String plan_Name = "";
    private List<M_BI_SmartMoneyPlanner_AdapterCommon> list_data;
    private File mypath;
    private String premiumPayingTerm = "";

    private RadioButton rb_smart_money_planner_backdating_yes;
    private RadioButton rb_smart_money_planner_backdating_no;
    private LinearLayout ll_smart_money_planner_backdating;
    private Button btn_smart_money_planner_backdatingdate;

    /*** Added by Akshaya on 04-AUG-15 end ***/
    private int maxAge;
    private String product_Code, product_UIN, product_cateogory, product_type;

    private String bankUserType = "", mode = "";
    private Context context;

    private String Check = "";
    private CommonMethods commonMethods;
    private StorageUtils mStorageUtils;
    private ImageButton imageButtonSmartMoneyPlannerProposerPhotograph;

    private String Company_policy_surrender_dec = "";

    private CheckBox cb_kerladisc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.bi_smart_money_plannermain);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);

        dbHelper = new DatabaseHelper(getApplicationContext());
        context = this;
        commonMethods = new CommonMethods();
        mStorageUtils = new StorageUtils();
        commonMethods.setActionbarLayout(this);

        NABIObj = new NeedAnalysisBIService(this);
        // objBIPdfMail=new BIPdfMail();
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

                    ProductInfo prodInfoObj = new ProductInfo();
                    planName = "Smart Money Planner";
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
                QuatationNumber = CommonForAllProd.getquotationNumber30("1R",
                        agentcode, zero + "");

            }
        } else
            needAnalysis_flag = 0;
        // ProductHomePageActivity.path.setText("Benefit Illustrator");

        UI_Declaration();

        prsObj = new ParseXML();
        setSpinner_Value();
        // setBIInputGui();

        edt_bi_smart_money_planner_life_assured_first_name
                .setOnEditorActionListener(this);
        edt_bi_smart_money_planner_life_assured_middle_name
                .setOnEditorActionListener(this);
        edt_bi_smart_money_planner_life_assured_last_name
                .setOnEditorActionListener(this);
        edt_bi_smart_money_planner_sum_assured_amount
                .setOnEditorActionListener(this);
        edt_smart_money_planner_contact_no.setOnEditorActionListener(this);
        edt_smart_money_planner_Email_id.setOnEditorActionListener(this);
        edt_smart_money_planner_ConfirmEmail_id.setOnEditorActionListener(this);

        setFocusable(spnr_bi_smart_money_planner_life_assured_title);
        spnr_bi_smart_money_planner_life_assured_title.requestFocus();

        validationOfMoile_EmailId();
        setSpinnerAndOtherListner();
        showAlert = new AlertDialog.Builder(this);
        obj = new CommonForAllProd();
        prop = new SmartMoneyPlannerProperties();
        smartMoneyPlannerBean = new SmartMoneyPlannerBean();
        list_data = new ArrayList<>();
        currencyFormat = new DecimalFormat("##,##,##,###");
        // getBasicDetail();

        if (needAnalysis_flag == 1 && !TextUtils.isEmpty(gender)) {
            spnr_bi_smart_money_planner_selGender.setSelection(genderAdapter
                    .getPosition(gender));
            onClickLADob(btn_bi_smart_money_planner_life_assured_date);
        }
    }

    private void UI_Declaration() {
        // sv_bi_smart_money_planner_main = (ScrollView)
        // findViewById(R.id.sv_bi_smart_money_planner_main);
        cb_staffdisc = findViewById(R.id.cb_staffdisc);
        cb_bi_smart_money_planner_JKResident = findViewById(R.id.cb_bi_smart_money_planner_JKResident);

        edt_bi_smart_money_planner_life_assured_first_name = findViewById(R.id.edt_bi_smart_money_planner_life_assured_first_name);
        edt_bi_smart_money_planner_life_assured_middle_name = findViewById(R.id.edt_bi_smart_money_planner_life_assured_middle_name);
        edt_bi_smart_money_planner_life_assured_last_name = findViewById(R.id.edt_bi_smart_money_planner_life_assured_last_name);
        edt_bi_smart_money_planner_life_assured_age = findViewById(R.id.edt_bi_smart_money_planner_life_assured_age);
        edt_smart_money_planner_contact_no = findViewById(R.id.edt_smart_money_planner_contact_no);
        edt_smart_money_planner_Email_id = findViewById(R.id.edt_smart_money_planner_Email_id);
        edt_smart_money_planner_ConfirmEmail_id = findViewById(R.id.edt_smart_money_planner_ConfirmEmail_id);
        edt_bi_smart_money_planner_sum_assured_amount = findViewById(R.id.edt_bi_smart_money_planner_sum_assured_amount);

        spnr_bi_smart_money_planner_life_assured_title = findViewById(R.id.spnr_bi_smart_money_planner_life_assured_title);
        spnr_bi_smart_money_planner_selGender = findViewById(R.id.spnr_bi_smart_money_planner_selGender);
//		spnr_bi_smart_money_planner_selGender.setClickable(false);
//		spnr_bi_smart_money_planner_selGender.setEnabled(false);

        spnr_bi_smart_money_planner_plan = findViewById(R.id.spnr_bi_smart_money_planner_plan);
        spnr_bi_smart_money_planner_policyterm = findViewById(R.id.spnr_bi_smart_money_planner_policyterm);
        spnr_bi_smart_money_planner_permium_payingterm = findViewById(R.id.spnr_bi_smart_money_planner_permium_payingterm);
        spnr_bi_smart_money_planner_premium_frequency = findViewById(R.id.spnr_bi_smart_money_planner_premium_frequency);

        btn_bi_smart_money_planner_life_assured_date = findViewById(R.id.btn_bi_smart_money_planner_life_assured_date);

        btnBack = findViewById(R.id.btn_bi_smart_money_planner_btnback);
        btnSubmit = findViewById(R.id.btn_bi_smart_money_planner_btnSubmit);

        btn_smart_money_planner_backdatingdate = findViewById(R.id.btn_smart_money_planner_backdatingdate);
        rb_smart_money_planner_backdating_yes = findViewById(R.id.rb_smart_money_planner_backdating_yes);
        rb_smart_money_planner_backdating_no = findViewById(R.id.rb_smart_money_planner_backdating_no);
        ll_smart_money_planner_backdating = findViewById(R.id.ll_smart_money_planner_backdating);
        tr_premium_paying_term = findViewById(R.id.tr_premium_paying_term);
    }

    private void setSpinner_Value() {

        // Gender
        String[] genderList = {"Male", "Female", "Third Gender"};
        genderAdapter = new ArrayAdapter<>(getApplicationContext(),
                R.layout.spinner_item, genderList);
        genderAdapter.setDropDownViewResource(R.layout.spinner_item1);
        spnr_bi_smart_money_planner_selGender.setAdapter(genderAdapter);
        genderAdapter.notifyDataSetChanged();

        commonMethods.fillSpinnerValue(context, spnr_bi_smart_money_planner_life_assured_title,
                Arrays.asList(getResources().getStringArray(R.array.arr_select_title)));

        // Plan
        String[] planList = {"Plan 1", "Plan 2", "Plan 3", "Plan 4"};
        ArrayAdapter<String> planAdapter = new ArrayAdapter<>(
                getApplicationContext(), R.layout.spinner_item, planList);
        planAdapter.setDropDownViewResource(R.layout.spinner_item1);
        spnr_bi_smart_money_planner_plan.setAdapter(planAdapter);
        planAdapter.notifyDataSetChanged();

        // Policy Term
        String[] policyTermList = {"15", "20", "20", "25"};
        ArrayAdapter<String> policyTermAdapter = new ArrayAdapter<>(
                getApplicationContext(), R.layout.spinner_item, policyTermList);
        policyTermAdapter.setDropDownViewResource(R.layout.spinner_item1);
        spnr_bi_smart_money_planner_policyterm.setAdapter(policyTermAdapter);
        policyTermAdapter.notifyDataSetChanged();

        // Premium paying term
        String[] premPayingTermList = {"6", "6", "10", "10"};
        ArrayAdapter<String> premPayingTermAdapter = new ArrayAdapter<>(
                getApplicationContext(), R.layout.spinner_item,
                premPayingTermList);
        premPayingTermAdapter.setDropDownViewResource(R.layout.spinner_item1);
        spnr_bi_smart_money_planner_permium_payingterm
                .setAdapter(premPayingTermAdapter);
        premPayingTermAdapter.notifyDataSetChanged();

        // Premium Frequency
        String[] premFreqList = {"Yearly", "Half Yearly", "Quarterly",
                "Monthly", "Single"};
        ArrayAdapter<String> premFreqAdapter = new ArrayAdapter<>(
                getApplicationContext(), R.layout.spinner_item, premFreqList);
        premFreqAdapter.setDropDownViewResource(R.layout.spinner_item1);
        spnr_bi_smart_money_planner_premium_frequency
                .setAdapter(premFreqAdapter);
        premFreqAdapter.notifyDataSetChanged();

    }


    private void validationOfMoile_EmailId() {

        edt_smart_money_planner_contact_no
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
                        String abc = edt_smart_money_planner_contact_no
                                .getText().toString();
                        mobile_validation(abc);

                    }
                });

        edt_smart_money_planner_Email_id
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
                        ProposerEmailId = edt_smart_money_planner_Email_id
                                .getText().toString();
                        //email_id_validation(ProposerEmailId);

                    }
                });

        edt_smart_money_planner_ConfirmEmail_id
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
						/*String proposer_confirm_emailId = edt_smart_money_planner_ConfirmEmail_id
								.getText().toString();*/
                        //confirming_email_id(proposer_confirm_emailId);

                    }
                });

    }

    private void setSpinnerAndOtherListner() {

        cb_staffdisc.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (cb_staffdisc.isChecked()) {
                    cb_staffdisc.setChecked(true);
                }

                clearFocusable(cb_staffdisc);
                clearFocusable(spnr_bi_smart_money_planner_life_assured_title);
                setFocusable(spnr_bi_smart_money_planner_life_assured_title);
                spnr_bi_smart_money_planner_life_assured_title.requestFocus();
            }
        });

        cb_bi_smart_money_planner_JKResident
                .setOnClickListener(new OnClickListener() {

                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        if (cb_bi_smart_money_planner_JKResident.isChecked()) {
                            cb_bi_smart_money_planner_JKResident
                                    .setChecked(true);
                        } else {
                            cb_bi_smart_money_planner_JKResident
                                    .setChecked(false);
                        }
                    }
                });

        // Spinner
        spnr_bi_smart_money_planner_life_assured_title
                .setOnItemSelectedListener(new OnItemSelectedListener() {

                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int position, long id) {
                        // TODO Auto-generated method stub
                        if (position == 0) {
                            lifeAssured_Title = "";
                        } else {
                            lifeAssured_Title = spnr_bi_smart_money_planner_life_assured_title
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
                            clearFocusable(spnr_bi_smart_money_planner_life_assured_title);
                            setFocusable(edt_bi_smart_money_planner_life_assured_first_name);
                            edt_bi_smart_money_planner_life_assured_first_name
                                    .requestFocus();
                        }
                    }

                    public void onNothingSelected(AdapterView<?> arg0) {
                        // TODO Auto-generated method stub

                    }
                });

        spnr_bi_smart_money_planner_plan
                .setOnItemSelectedListener(new OnItemSelectedListener() {

                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int position, long id) {
                        // TODO Auto-generated method stub
                        updatePolicyTermLabel();
                        updatePremPayingTermLabel();
                        updateMaximumAge();
                        if (position == 0) {

                            spnr_bi_smart_money_planner_policyterm
                                    .setEnabled(false);
                            spnr_bi_smart_money_planner_policyterm
                                    .setSelection(0, false);

                            spnr_bi_smart_money_planner_permium_payingterm
                                    .setEnabled(false);
                            spnr_bi_smart_money_planner_permium_payingterm
                                    .setSelection(0, false);

                        } else if (position == 1) {

                            spnr_bi_smart_money_planner_policyterm
                                    .setEnabled(false);
                            spnr_bi_smart_money_planner_policyterm
                                    .setSelection(1, false);

                            spnr_bi_smart_money_planner_permium_payingterm
                                    .setEnabled(false);
                            spnr_bi_smart_money_planner_permium_payingterm
                                    .setSelection(1, false);
                        } else if (position == 2) {

                            spnr_bi_smart_money_planner_policyterm
                                    .setEnabled(false);
                            spnr_bi_smart_money_planner_policyterm
                                    .setSelection(2, false);

                            spnr_bi_smart_money_planner_permium_payingterm
                                    .setEnabled(false);
                            spnr_bi_smart_money_planner_permium_payingterm
                                    .setSelection(2, false);

                        } else {
                            spnr_bi_smart_money_planner_policyterm
                                    .setEnabled(false);
                            spnr_bi_smart_money_planner_policyterm
                                    .setSelection(3, false);

                            spnr_bi_smart_money_planner_permium_payingterm
                                    .setEnabled(false);
                            spnr_bi_smart_money_planner_permium_payingterm
                                    .setSelection(3, false);
                        }

                        // spnr_bi_smart_money_planner_premium_frequency
                        // .setSelection(4, true);
                        spnr_bi_smart_money_planner_premium_frequency
                                .setEnabled(true);
                        clearFocusable(spnr_bi_smart_money_planner_plan);
                        // clearFocusable(spnr_bi_smart_money_planner_premium_frequency);
                        setFocusable(spnr_bi_smart_money_planner_premium_frequency);
                        spnr_bi_smart_money_planner_premium_frequency
                                .requestFocus();
                    }

                    public void onNothingSelected(AdapterView<?> arg0) {
                        // TODO Auto-generated method stub

                    }
                });

        spnr_bi_smart_money_planner_policyterm
                .setOnItemSelectedListener(new OnItemSelectedListener() {

                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int pos, long id) {

                        clearFocusable(spnr_bi_smart_money_planner_policyterm);
                        setFocusable(spnr_bi_smart_money_planner_life_assured_title);
                        spnr_bi_smart_money_planner_life_assured_title
                                .requestFocus();

                    }

                    public void onNothingSelected(AdapterView<?> arg0) {
                        // TODO Auto-generated method stub

                    }
                });

        spnr_bi_smart_money_planner_permium_payingterm
                .setOnItemSelectedListener(new OnItemSelectedListener() {

                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int pos, long id) {
                        clearFocusable(spnr_bi_smart_money_planner_permium_payingterm);
                        setFocusable(spnr_bi_smart_money_planner_life_assured_title);
                        spnr_bi_smart_money_planner_life_assured_title
                                .requestFocus();

                    }

                    public void onNothingSelected(AdapterView<?> arg0) {
                        // TODO Auto-generated method stub

                    }
                });

        spnr_bi_smart_money_planner_premium_frequency
                .setOnItemSelectedListener(new OnItemSelectedListener() {

                    public void onItemSelected(AdapterView<?> adapter,
                                               View arg1, int position, long id) {
                        // TODO Auto-generated method stub
                        updatePremPayingTermLabel();
                        if (position == 4) {
                            tr_premium_paying_term.setVisibility(View.GONE);

                        } else {
                            tr_premium_paying_term.setVisibility(View.VISIBLE);

                        }

                        if (edt_bi_smart_money_planner_life_assured_first_name
                                .getText().toString().equals("")) {
                            clearFocusable(spnr_bi_smart_money_planner_premium_frequency);
                            setFocusable(spnr_bi_smart_money_planner_life_assured_title);
                            spnr_bi_smart_money_planner_life_assured_title
                                    .requestFocus();
                        } else {
                            clearFocusable(spnr_bi_smart_money_planner_premium_frequency);
                            setFocusable(edt_bi_smart_money_planner_sum_assured_amount);
                            edt_bi_smart_money_planner_sum_assured_amount
                                    .requestFocus();
                        }

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

                // clearFocusable(edt_bi_smart_money_planner_sum_assured_amount);
                inputVal = new StringBuilder();
                retVal = new StringBuilder();

                lifeAssured_First_Name = edt_bi_smart_money_planner_life_assured_first_name
                        .getText().toString().trim();
                lifeAssured_Middle_Name = edt_bi_smart_money_planner_life_assured_middle_name
                        .getText().toString().trim();
                lifeAssured_Last_Name = edt_bi_smart_money_planner_life_assured_last_name
                        .getText().toString().trim();
                gender = spnr_bi_smart_money_planner_selGender.getSelectedItem().toString();
                name_of_life_assured = lifeAssured_Title + " "
                        + lifeAssured_First_Name + " "
                        + lifeAssured_Middle_Name + " " + lifeAssured_Last_Name;

                mobileNo = edt_smart_money_planner_contact_no.getText()
                        .toString();
                emailId = edt_smart_money_planner_Email_id.getText().toString();
                ConfirmEmailId = edt_smart_money_planner_ConfirmEmail_id
                        .getText().toString();

                if (valLifeAssuredProposerDetail() && valDob()
                        && valBackdatingDate() && valBasicDetail() && valSA()
                        && valAge() && valDoYouBackdate() && valBackdate()) {

                    if (proposer_Is_Same_As_Life_Assured.equalsIgnoreCase("Y")) {
                        name_of_proposer = "";
                    }

                    Log.e("ouput:", output + "");
                    addListenerOnSubmit();
                    Log.e("ouput:", output + "");
                    getInput(smartMoneyPlannerBean);
                    // insertDataIntoDatabase();
                    if (needAnalysis_flag == 0) {// Display output on next page

                        Intent i = new Intent(getApplicationContext(),
                                Success.class);
                        i.putExtra(
                                "op",
                                spnr_bi_smart_money_planner_premium_frequency
                                        .getSelectedItem().toString()
                                        + " Premium is Rs. "
                                        + currencyFormat.format(Double
                                        .parseDouble(prsObj
                                                .parseXmlTag(retVal
                                                                .toString(),
                                                        "premInst"))));
                        i.putExtra(
                                "op1",
                                "Applicable Taxes is Rs. "
                                        + currencyFormat.format(Double
                                        .parseDouble(prsObj
                                                .parseXmlTag(retVal
                                                                .toString(),
                                                        "servcTax"))));
                        i.putExtra(
                                "op2",
                                spnr_bi_smart_money_planner_premium_frequency
                                        .getSelectedItem().toString()
                                        + " Premium with Applicable Taxes is Rs. "
                                        + currencyFormat.format(Double
                                        .parseDouble(prsObj
                                                .parseXmlTag(retVal
                                                                .toString(),
                                                        "premWthST"))));
                        if (spnr_bi_smart_money_planner_plan.getSelectedItem()
                                .toString().equals("1"))
                            i.putExtra(
                                    "op3",
                                    "Guaranteed Survival Benefit (for Policy Year 11 - 15) is Rs. "
                                            + currencyFormat
                                            .format(GuaranteedSurvivalBenefitPremCalci));
                        else if (spnr_bi_smart_money_planner_plan
                                .getSelectedItem().toString().equals("2"))
                            i.putExtra(
                                    "op3",
                                    "Guaranteed Survival Benefit (for Policy Year 11 - 20) is Rs. "
                                            + currencyFormat
                                            .format(GuaranteedSurvivalBenefitPremCalci));
                        else if (spnr_bi_smart_money_planner_plan
                                .getSelectedItem().toString().equals("3"))
                            i.putExtra(
                                    "op3",
                                    "Guaranteed Survival Benefit (for Policy Year 16 - 20) is Rs. "
                                            + currencyFormat
                                            .format(GuaranteedSurvivalBenefitPremCalci));
                        else if (spnr_bi_smart_money_planner_plan
                                .getSelectedItem().toString().equals("4"))
                            i.putExtra(
                                    "op3",
                                    "Guaranteed Survival Benefit (for Policy Year 16 - 25) is Rs. "
                                            + currencyFormat
                                            .format(GuaranteedSurvivalBenefitPremCalci));

                        i.putExtra(
                                "op4",
                                "Non-Guaranteed  Survival Benefit at 4% (on Maturity) is Rs. "
                                        + currencyFormat
                                        .format(NonGuarateedSurvivalBenefitAt_4_PercentPremCalci));
                        i.putExtra(
                                "op5",
                                "Non-Guaranteed  Survival Benefit at 8% (on Maturity) is Rs. "
                                        + currencyFormat
                                        .format(NonGuarateedSurvivalBenefitAt_8_PercentPremCalci));
                        i.putExtra("header", "SBI Life - Smart Money Planner");
                        i.putExtra("header1", "(UIN : 111N101V03)");
                        startActivity(i);
                    } else
                        Dialog();

                }

            }
        });

        rb_smart_money_planner_backdating_yes
                .setOnCheckedChangeListener(new OnCheckedChangeListener() {

                    public void onCheckedChanged(CompoundButton buttonView,
                                                 boolean isChecked) {
                        // TODO Auto-generated method stub
                        if (isChecked) {
                            proposer_Backdating_WishToBackDate_Policy = "y";
                            ll_smart_money_planner_backdating
                                    .setVisibility(View.VISIBLE);
                            btn_smart_money_planner_backdatingdate
                                    .setText("Select Date");
                            proposer_Backdating_BackDate = "";

                        }
                    }
                });

        rb_smart_money_planner_backdating_no
                .setOnCheckedChangeListener(new OnCheckedChangeListener() {

                    public void onCheckedChanged(CompoundButton buttonView,
                                                 boolean isChecked) {
                        // TODO Auto-generated method stub
                        if (isChecked) {

                            proposer_Backdating_WishToBackDate_Policy = "N";
                            proposer_Backdating_BackDate = "";
                            ll_smart_money_planner_backdating
                                    .setVisibility(View.GONE);

                        }
                    }
                });

    }

    private void addListenerOnSubmit() {

        smartMoneyPlannerBean = new SmartMoneyPlannerBean();

        if (cb_staffdisc.isChecked()) {
            smartMoneyPlannerBean.setStaffDisc(true);
        } else {
            smartMoneyPlannerBean.setStaffDisc(false);
        }
        if (cb_kerladisc.isChecked()) {
            smartMoneyPlannerBean.setKerlaDisc(true);
        } else {
            smartMoneyPlannerBean.setKerlaDisc(false);
        }
        if (cb_bi_smart_money_planner_JKResident.isChecked()) {
            smartMoneyPlannerBean.setJKResident(true);
        } else {
            smartMoneyPlannerBean.setJKResident(false);
        }

        smartMoneyPlannerBean.setAge(Integer
                .parseInt(edt_bi_smart_money_planner_life_assured_age.getText()
                        .toString()));

        smartMoneyPlannerBean.setBasicSA(Integer
                .parseInt(edt_bi_smart_money_planner_sum_assured_amount
                        .getText().toString()));
        smartMoneyPlannerBean.setBasicTerm(Integer
                .parseInt(spnr_bi_smart_money_planner_policyterm
                        .getSelectedItem().toString()));
        smartMoneyPlannerBean.setGender(spnr_bi_smart_money_planner_selGender
                .getSelectedItem().toString());
        // smartMoneyPlannerBean.setPlanName(spnr_bi_smart_money_planner_plan.getSelectedItem().toString());
        if (spnr_bi_smart_money_planner_plan.getSelectedItem().toString()
                .equals("Plan 1"))
            smartMoneyPlannerBean.setPlan(1);
        else if (spnr_bi_smart_money_planner_plan.getSelectedItem().toString()
                .equals("Plan 2"))
            smartMoneyPlannerBean.setPlan(2);
        else if (spnr_bi_smart_money_planner_plan.getSelectedItem().toString()
                .equals("Plan 3"))
            smartMoneyPlannerBean.setPlan(3);
        else {
            smartMoneyPlannerBean.setPlan(4);
        }

        smartMoneyPlannerBean
                .setPremFreq(spnr_bi_smart_money_planner_premium_frequency
                        .getSelectedItem().toString());

        if (spnr_bi_smart_money_planner_premium_frequency.getSelectedItem()
                .toString().equalsIgnoreCase("Single"))
            smartMoneyPlannerBean.setPremPayingTerm(1);
        else
            smartMoneyPlannerBean.setPremPayingTerm(Integer
                    .parseInt(spnr_bi_smart_money_planner_permium_payingterm
                            .getSelectedItem().toString()));

        showsmartMoneyPlannerOutputPg(smartMoneyPlannerBean);

    }

    private void showsmartMoneyPlannerOutputPg(
            SmartMoneyPlannerBean smartMoneyPlannerBean) {

        String[] outputArr = getOutput("BI_of_Smart_Money_Planner",
                smartMoneyPlannerBean);

        try {

            /* Added by Akshaya on 04-AUG-15 start ***/

            String staffStatus;
            if (cb_staffdisc.isChecked()) {
                staffStatus = "sbi";
                // disc_Basic_SelFreq
            } else
                staffStatus = "none";
            /* Added by Akshaya on 04-AUG-15 end ***/

            retVal.append("<?xml version='1.0' encoding='utf-8' ?><SmartIncomeProtect>");
            retVal.append("<errCode>0</errCode>");

            /* Added by Akshaya on 04-AUG-15 start ***/
            retVal.append("<staffStatus>").append(staffStatus).append("</staffStatus>");
            retVal.append("<staffRebate>").append(outputArr[6]).append("</staffRebate>");
            retVal.append("<basicPremWithoutDisc>").append(outputArr[8]).append("</basicPremWithoutDisc>");
            retVal.append("<InstmntPrem>").append(outputArr[8]).append("</InstmntPrem>");
            retVal.append("<OccuInt>").append(outputArr[9]).append("</OccuInt>");
            retVal.append("<OccuIntServiceTax>" + outputArr[19] + "</OccuIntServiceTax>");
            retVal.append("<backdateInt>").append(outputArr[10]).append("</backdateInt>");
            /* Added by Akshaya on 04-AUG-15 end ***/

            retVal.append("<premInst>").append(outputArr[0]).append("</premInst>").
                    append("<servcTax>").append(outputArr[1]).append("</servcTax>").
                    append("<premWthST>").append(outputArr[2]).append("</premWthST>").
                    append("<premInstSecondYear>").append(outputArr[0]).
                    append("</premInstSecondYear>").append("<servcTaxSecondYear>").
                    append(outputArr[11]).append("</servcTaxSecondYear>").
                    append("<premWthSTSecondYear>").append(outputArr[12]).
                    append("</premWthSTSecondYear>").append("<basicServiceTax>").
                    append(outputArr[13]).append("</basicServiceTax>").
                    append("<SBCServiceTax>").append(outputArr[14]).
                    append("</SBCServiceTax>").append("<KKCServiceTax>").
                    append(outputArr[15]).append("</KKCServiceTax>").
                    append("<basicServiceTaxSecondYear>").append(outputArr[16]).
                    append("</basicServiceTaxSecondYear>").
                    append("<SBCServiceTaxSecondYear>").append(outputArr[17]).
                    append("</SBCServiceTaxSecondYear>").
                    append("<KKCServiceTaxSecondYear>").append(outputArr[18]).
                    append("</KKCServiceTaxSecondYear>").
                    append("<KeralaCessServiceTax>").append(outputArr[20]).
                    append("</KeralaCessServiceTax>").
                    append("<KeralaCessServiceTaxSecondYear>").append(outputArr[21]).
                    append("</KeralaCessServiceTaxSecondYear>");

            int index = smartMoneyPlannerBean.getBasicTerm();
            String nonGuaranMatBen_4Percent = prsObj.parseXmlTag(bussIll.toString(), "nonGuaranMatBen_4Percent" + index + "");
            String nonGuaranMatBen_8Percent = prsObj.parseXmlTag(bussIll.toString(), "nonGuaranMatBen_8Percent" + index + "");

            retVal.append("<nonGuaranMatBen_4Percent").append(index).append(">").append(nonGuaranMatBen_4Percent).append("</nonGuaranMatBen_4Percent").append(index).append(">");
            retVal.append("<nonGuaranMatBen_8Percent").append(index).append(">").append(nonGuaranMatBen_8Percent).append("</nonGuaranMatBen_8Percent").append(index).append(">");

            retVal.append(bussIll.toString());
            retVal.append("</SmartIncomeProtect>");

        } catch (Exception e) {
            retVal.append("<?xml version='1.0' encoding='utf-8' ?><SmartIncomeProtect>" + "<errCode>1</errCode>" + "<errorMessage>").append(e.getMessage()).append("</errorMessage></SmartIncomeProtect>");
        }

    }

    private String[] getOutput(String sheetName,
                               SmartMoneyPlannerBean smartMoneyPlannerBean) {
        bussIll = new StringBuilder();
        retVal = new StringBuilder();
        CommonForAllProd commonForAllProd = new CommonForAllProd();

        int _year_F = 0;

        int year_F;

        double totalBasePremiumPaid, totalBasePremiumPaidPPT, AnnulizedPrem, GuaranteedDeathBenefit, NonGuarateedDeathBenefitAt_4_Percent, NonGuarateedDeathBenefitAt_4_PercentSURR, NonGuarateedDeathBenefitAt_8_PercentSURR, NonGuarateedDeathBenefitAt_8_Percent, GuaranteedSurvivalBenefit = 0, GuaranteedMaturityBenefit, SurvivalBenefit, NonGuarateedSurvivalBenefitAt_4_Percent = 0, NonGuarateedSurvivalBenefitAt_8_Percent = 0, TotalSurvivalBenefitAt_4_Percent = 0, TotalSurvivalBenefitAt_8_Percent = 0, yearlyPremiumWithOutST, premiumwithRoundUP, premiumWithST,


                FirstYear_ServiceTax, premiumWithSTSecondYear, SecondYear_ServiceTax,
                /*** Modified By - Priyanka Warekar - 26-08-2015 - End ****/
                basePremiumwithRoundUP, sumGuarnSurvBen = 0;

        double basePremiumWithST, yearlyPremiumWithST = 0, baseST = 0;

        String GSV_surrendr_val, TotalMaturityBenefit4percent, TotalMaturityBenefit8Percent, TotalDeathBenefit4percent, TotalDeathBenefit8percent, NonGSV_surrndr_val_4Percent, NonGSV_surrndr_val_8Percent, TotalMaturityBenefit = null;
        double maxNonGuarateedDeathBenefitAt_4_Percent = 0, maxNonGuarateedDeathBenefitAt_8_Percent = 0;
        int rowNumber = 0, j = 0;
        double discountPercentage;
        // From GUI Input
        boolean staffDisc = smartMoneyPlannerBean.getStaffDisc();
        boolean JKResidentDisc = smartMoneyPlannerBean.isJKResident();
        String premFreqMode = smartMoneyPlannerBean.getPremFreq();
        int premiumPayingTerm = smartMoneyPlannerBean.getPremPayingTerm();
        int age = smartMoneyPlannerBean.getAge();
        int plan = smartMoneyPlannerBean.getPlan();
        int policyTerm = smartMoneyPlannerBean.getBasicTerm();
        double sumAssured = smartMoneyPlannerBean.getBasicSA();
        // System.out.println(staffDisc+" "+JKResidentDisc+" "+premFreqMode+" "+premiumPayingTerm+" "+age+" "+plan+" "+policyTerm+" "+sumAssured);
        // showAlert(staffDisc+" "+JKResidentDisc+" "+premFreqMode+" "+premiumPayingTerm+" "+ageOfChild+" "+ageOfProposer+" "+policyTerm);
        double[] deathbenefitArr = new double[policyTerm * 5];
        SmartMoneyPlannerBusinessLogic smpBusinessLogic = new SmartMoneyPlannerBusinessLogic(
                smartMoneyPlannerBean);

        discountPercentage = smpBusinessLogic.getStaffRebate(premFreqMode,
                staffDisc);

        smpBusinessLogic.setPremiumWithoutSTwithoutRoundUP(age, plan,
                sumAssured, staffDisc, premFreqMode);

        double premiumWithoutST = smpBusinessLogic
                .getPremiumWithoutSTwithoutRoundUP();

        smpBusinessLogic.setPremiumWithoutSTwithoutStaffwithoutRoundUP(age,
                plan, sumAssured, staffDisc, premFreqMode);
        String premiumWithoutSTwithoutStaffWithoutRound = smpBusinessLogic
                .getPremiumWithoutSTwithoutStaffwithoutRoundUP();

        smpBusinessLogic.setPremiumWithoutSTwithoutStaffwithRoundUP();
        String premiumWithoutSTwithoutStaffwithRoundUP = smpBusinessLogic
                .getPremiumWithoutSTwithoutStaffwithRoundUP();

        double PTARiderPremium = smpBusinessLogic.preferredTARiderPremium();
        double ATPDRiderPremium = smpBusinessLogic.ATPDRiderPremium();
        double criticarePremium = smpBusinessLogic.critiCareRiderPremium();
        double ADBRiderPremium = smpBusinessLogic.ADBRiderPremium();

        smpBusinessLogic.setPremiumWithoutSTwithRoundUP(premiumWithoutST,
                PTARiderPremium, ADBRiderPremium, ATPDRiderPremium,
                criticarePremium);
        premiumwithRoundUP = smpBusinessLogic.getPremiumWithoutSTwithRoundUP();

        // if (selOccupationMines.isChecked()) {
        String MinesOccuInterest = ""
                + smpBusinessLogic.getMinesOccuInterest(smartMoneyPlannerBean
                .getBasicSA());

        String servicetax_MinesOccuInterest = ""
                + smpBusinessLogic
                .getServiceTaxMines(Double.parseDouble(MinesOccuInterest));

        MinesOccuInterest = "" + (Double.parseDouble(MinesOccuInterest) + Double.parseDouble(servicetax_MinesOccuInterest));

        // } else {
        // MinesOccuInterest = "0";
        // }

        // premiumwithRoundUP = premiumwithRoundUP
        // + Double.parseDouble(MinesOccuInterest);

		/*double basicServiceTax = smpBusinessLogic.getServiceTax(
				premiumwithRoundUP, JKResidentDisc, "basic");
		double SBCServiceTax = smpBusinessLogic.getServiceTax(
				premiumwithRoundUP, JKResidentDisc, "SBC");
		double KKCServiceTax = smpBusinessLogic.getServiceTax(
				premiumwithRoundUP, JKResidentDisc, "KKC");*/

        double basicServiceTax;
        double SBCServiceTax = 0;
        double KKCServiceTax = 0;

        double kerlaServiceTax = 0;
        double KeralaCessServiceTax = 0;
        boolean isKerlaDiscount = smartMoneyPlannerBean.isKerlaDisc();

        if (isKerlaDiscount) {
            basicServiceTax = smpBusinessLogic.getServiceTax(
                    premiumwithRoundUP, JKResidentDisc, "basic");
            kerlaServiceTax = smpBusinessLogic.getServiceTax(
                    premiumwithRoundUP, JKResidentDisc, "KERALA");
            KeralaCessServiceTax = kerlaServiceTax - basicServiceTax;
        } else {
            basicServiceTax = smpBusinessLogic.getServiceTax(
                    premiumwithRoundUP, JKResidentDisc, "basic");
            SBCServiceTax = smpBusinessLogic.getServiceTax(
                    premiumwithRoundUP, JKResidentDisc, "SBC");
            KKCServiceTax = smpBusinessLogic.getServiceTax(
                    premiumwithRoundUP, JKResidentDisc, "KKC");
        }

        FirstYear_ServiceTax = basicServiceTax + SBCServiceTax + KKCServiceTax + kerlaServiceTax;

        //  Added By Saurabh Jain on 15/05/2019 End

        premiumWithST = premiumwithRoundUP + FirstYear_ServiceTax;

        // smpBusinessLogic.setPremiumWithSTFirstYear(premiumwithRoundUP,
        // JKResidentDisc);
        // premiumWithST = smpBusinessLogic.getPremiumWithSTFirstYear();

        //  Added By Saurabh Jain on 15/05/2019 Start
        double basicServiceTaxSecondYear;
        double SBCServiceTaxSecondYear = 0;
        double KKCServiceTaxSecondYear = 0;

        double kerlaServiceTaxSecondYear = 0;
        double KeralaCessServiceTaxSecondYear = 0;

        if (isKerlaDiscount) {
            basicServiceTaxSecondYear = smpBusinessLogic
                    .getServiceTaxSecondYear(premiumwithRoundUP, JKResidentDisc,
                            "basic");
            kerlaServiceTaxSecondYear = smpBusinessLogic
                    .getServiceTaxSecondYear(premiumwithRoundUP, JKResidentDisc,
                            "KERALA");
            KeralaCessServiceTaxSecondYear = kerlaServiceTaxSecondYear - basicServiceTaxSecondYear;
        } else {
            basicServiceTaxSecondYear = smpBusinessLogic
                    .getServiceTaxSecondYear(premiumwithRoundUP, JKResidentDisc,
                            "basic");
            SBCServiceTaxSecondYear = smpBusinessLogic
                    .getServiceTaxSecondYear(premiumwithRoundUP, JKResidentDisc,
                            "SBC");
            KKCServiceTaxSecondYear = smpBusinessLogic
                    .getServiceTaxSecondYear(premiumwithRoundUP, JKResidentDisc,
                            "KKC");
        }

        SecondYear_ServiceTax = basicServiceTaxSecondYear
                + SBCServiceTaxSecondYear + KKCServiceTaxSecondYear + kerlaServiceTaxSecondYear;

        //  Added By Saurabh Jain on 15/05/2019 End

        premiumWithSTSecondYear = premiumwithRoundUP + SecondYear_ServiceTax;

        // smpBusinessLogic.setPremiumWithSTSecondYear(premiumwithRoundUP,
        // JKResidentDisc);
        // premiumWithSTSecondYear =
        // smpBusinessLogic.getPremiumWithSTSecondYear();

//		System.out.println(" premiumwithRoundUP : " + premiumwithRoundUP + " "
//				+ premiumWithST + "  " + premiumWithSTSecondYear);

        // smpBusinessLogic.setServiceTaxFirstYear(premiumWithST,
        // premiumwithRoundUP);
        // FirstYear_ServiceTax = smpBusinessLogic.getServiceTaxFirstYear();
        // smpBusinessLogic.setServiceTaxSecondYear(premiumWithSTSecondYear,
        // premiumwithRoundUP);
        // SecondYear_ServiceTax = smpBusinessLogic.getServiceTaxSecondYear();

        String BackDateinterestwithGST;
        if (rb_smart_money_planner_backdating_yes.isChecked()) {
            // BackDateinterest=commonForAllProd.getRoundUp(""+(shubhNiveshBussinesLogic.getBackDateInterest(Double.parseDouble(premiumSingleInstBasicWithST),
            // "16-jan-2014")));
            String BackDateinterest = commonForAllProd.getRoundUp(""
                    + (smpBusinessLogic.getBackDateInterest(premiumWithST,
                    btn_smart_money_planner_backdatingdate.getText()
                            .toString())));

            BackDateinterestwithGST = commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(Double.parseDouble(BackDateinterest) + (Double.parseDouble(BackDateinterest) * prop.GSTforbackdate)));
        } else {
            BackDateinterestwithGST = "0";
        }

        premiumWithST = premiumWithST + Double.parseDouble(BackDateinterestwithGST);
        /****************************** Added by Akshaya on 03-MAR-15 end **********************/

        // System.out.println(premiumWithoutST+" "+premiumWithST+"  "+ ST+" " +
        // premiumwithRoundUP);

        smpBusinessLogic.setBasePremiumWithoutSTwithRoundUP(premiumWithoutST);
        basePremiumwithRoundUP = smpBusinessLogic
                .getBasePremiumWithoutSTwithRoundUP();

        smpBusinessLogic.setBasePremiumWithST(basePremiumwithRoundUP,
                JKResidentDisc);
        basePremiumWithST = smpBusinessLogic.getBasePremiumWithST();

        smpBusinessLogic.setBaseServiceTax(premiumWithST, premiumwithRoundUP);
        baseST = smpBusinessLogic.getBaseServiceTax();

        smpBusinessLogic
                .setYearlyPremiumWithST(basePremiumWithST, premFreqMode);
        yearlyPremiumWithST = smpBusinessLogic.getYearlyPremiumWithST();

        int growthPeriod = smpBusinessLogic.getGrowthPeriod(age, premFreqMode,
                plan);
        int BPP = smpBusinessLogic.setBenefitPayingTerm(plan);
//		System.out.println("growthPeriod : " + growthPeriod);
        smpBusinessLogic.setYearlyPremiumWithoutST(premiumWithoutST,
                premFreqMode);
        yearlyPremiumWithOutST = Double.parseDouble(commonForAllProd
                .getRoundUp(commonForAllProd
                        .getStringWithout_E(smpBusinessLogic
                                .getYearlyPremiumWithoutST())));

        // System.out.println(yearlyPremiumWithST+"  "+
        // yearlyPremiumWithOutST+"  "+ BPP+"  "+ growthPeriod);


        double sumtotalBasePremiumPaidforSurr = 0, firstYearoftotalbasePrem = 0;
        double sumSurvivalbenefit = 0;
        double annualprem = 0;

        try {
            for (int i = 0; i < policyTerm; i++)
            // for(int i=0;i<1;i++)
            {
                rowNumber++;

                year_F = rowNumber;
                _year_F = year_F;
                System.out.println("1. year_F " + year_F);
                bussIll.append("<policyYr").append(_year_F).append(">").append(_year_F).append("</policyYr").append(_year_F).append(">");

                smpBusinessLogic.setTotalBasePremiumPaid(
                        yearlyPremiumWithOutST, year_F, premiumPayingTerm,
                        premFreqMode, policyTerm);
                totalBasePremiumPaid = smpBusinessLogic
                        .getTotalBasePremiumPaid();
                // System.out.println("2.Total Base Premium Paid "+totalBasePremiumPaid);
                bussIll.append("<totaBasePrem").append(_year_F).append(">").append(totalBasePremiumPaid).append("</totaBasePrem").append(_year_F).append(">");


                smpBusinessLogic.settotalbaseofppt(year_F, premiumPayingTerm, yearlyPremiumWithOutST);

                //smpBusinessLogic.settotalbaseofppt(year_F, premiumPayingTerm, BasePremiumPaid);
                totalBasePremiumPaidPPT = smpBusinessLogic.gettotalbaseofppt();

//				if(year_F<=premiumPayingTerm)
//				{
//					sumtotalBasePremiumPaidforSurr=(sumtotalBasePremiumPaidforSurr + Math.round(totalBasePremiumPaid));
//					//System.out.println("sumtotalBasePremiumPaidforSurr "+sumtotalBasePremiumPaidforSurr);
//				}


                AnnulizedPrem = smpBusinessLogic.getAnnulizedPremium(year_F, premiumPayingTerm);
                //			System.out.println("AnnulizedPrem "+AnnulizedPrem);
                bussIll.append("<AnnulizedPrem").append(_year_F).append(">").append(AnnulizedPrem).append("</AnnulizedPrem").append(_year_F).append(">");


                if (year_F == 1) {
                    annualprem = AnnulizedPrem;
                }
                bussIll.append("<GuaranteedAdd").append(_year_F).append(">").append(0).append("</GuaranteedAdd").append(_year_F).append(">");

                smpBusinessLogic.setGuaranteedDeathBenefit(sumAssured, annualprem, year_F,
                        policyTerm);
                GuaranteedDeathBenefit = smpBusinessLogic
                        .getGuaranteedDeathBenefit();
                // System.out.println("3.Guaranteed Death Benefit "+GuaranteedDeathBenefit);
                bussIll.append("<guarntdDeathBenft").append(_year_F).append(">").append(GuaranteedDeathBenefit).append("</guarntdDeathBenft").append(_year_F).append(">");

                smpBusinessLogic.setNonGuarateedDeathBenefitAt_4_Percent(
                        sumAssured, year_F, policyTerm);
                NonGuarateedDeathBenefitAt_4_Percent = smpBusinessLogic
                        .getNonGuarateedDeathBenefitAt_4_Percent();
                // System.out.println("4.Non Guarateed Death Benefit At_4_Percent "+NonGuarateedDeathBenefitAt_4_Percent);
                bussIll.append("<nongrntdDeathNenft_4Percent").append(_year_F).append(">").append(NonGuarateedDeathBenefitAt_4_Percent).append("</nongrntdDeathNenft_4Percent").append(_year_F).append(">");

                smpBusinessLogic.setNonGuarateedDeathBenefitAt_8_Percent(
                        sumAssured, year_F, policyTerm);// change parameter from GuaranteedDeathBenefit to sumAssured
                NonGuarateedDeathBenefitAt_8_Percent = smpBusinessLogic
                        .getNonGuarateedDeathBenefitAt_8_Percent();


                // System.out.println("5.Non Guarateed Death Benefit At_8_Percent "+NonGuarateedDeathBenefitAt_8_Percent);
                bussIll.append("<nongrntdDeathNenft_8Percent").append(_year_F).append(">").append(NonGuarateedDeathBenefitAt_8_Percent).append("</nongrntdDeathNenft_8Percent").append(_year_F).append(">");


                bussIll.append("<cashBonus").append(_year_F).append(">").append(0).append("</cashBonus").append(_year_F).append(">");

                sumGuarnSurvBen += GuaranteedSurvivalBenefit;
                smpBusinessLogic.setGuaranteedSurvivalBenefit(sumAssured,
                        growthPeriod, policyTerm, year_F, BPP);
                GuaranteedSurvivalBenefit = smpBusinessLogic
                        .getGuaranteedSurvivalBenefit();
                // System.out.println("6.Guaranteed Survival Benefit "+GuaranteedSurvivalBenefit);


                //added by sujata on 24-02-2020

                smpBusinessLogic.setGuaranteedMaturityBenefit(year_F, policyTerm, sumAssured, BPP);
                GuaranteedMaturityBenefit = smpBusinessLogic.getGuaranteedMaturityBenefit();


                bussIll.append("<guaranMatBen").append(_year_F).append(">").append(GuaranteedMaturityBenefit).append("</guaranMatBen").append(_year_F).append(">");


                SurvivalBenefit = smpBusinessLogic.getGuaranteedSurvivalBenefitFinal(policyTerm, year_F);
                //	System.out.println("SurvivalBenefit "+SurvivalBenefit);
                bussIll.append("<guaranSurvivalBenefit").append(_year_F).append(">").append(SurvivalBenefit).append("</guaranSurvivalBenefit").append(_year_F).append(">");


                //sumSurvivalbenefit = sumSurvivalbenefit + SurvivalBenefit;
                //System.out.println("sumSurvivalbenefit "+sumSurvivalbenefit );

                smpBusinessLogic.setNonGuarateedSurvivalBenefitAt_4_Percent(
                        NonGuarateedDeathBenefitAt_4_Percent, policyTerm,
                        year_F);
                NonGuarateedSurvivalBenefitAt_4_Percent = smpBusinessLogic
                        .getNonGuarateedSurvivalBenefitAt_4_Percent();// System.out.println("7.Non Guarateed Survival Benefit At_4_Percent "+NonGuarateedSurvivalBenefitAt_4_Percent);
                bussIll.append("<nonGuaranMatBen_4Percent").append(_year_F).append(">").append(NonGuarateedSurvivalBenefitAt_4_Percent).append("</nonGuaranMatBen_4Percent").append(_year_F).append(">");

                smpBusinessLogic.setNonGuarateedSurvivalBenefitAt_8_Percent(
                        NonGuarateedDeathBenefitAt_8_Percent, policyTerm,
                        year_F);
                NonGuarateedSurvivalBenefitAt_8_Percent = smpBusinessLogic
                        .getNonGuarateedSurvivalBenefitAt_8_Percent();
                // System.out.println("8.Non Guarateed Survival Benefit At_8_Percent "+NonGuarateedSurvivalBenefitAt_8_Percent);
                bussIll.append("<nonGuaranMatBen_8Percent").append(_year_F).append(">").append(NonGuarateedSurvivalBenefitAt_8_Percent).append("</nonGuaranMatBen_8Percent").append(_year_F).append(">");


                if (year_F == 1) {
                    firstYearoftotalbasePrem = Math.round(totalBasePremiumPaidPPT);
                }

				/*smpBusinessLogic.setGSV_SurrenderValue(year_F,
						sumSurvivalbenefit,firstYearoftotalbasePrem ,sumtotalBasePremiumPaidforSurr);

				GSV_surrendr_val = commonForAllProd.getRound(commonForAllProd
						.getStringWithout_E(smpBusinessLogic
								.getGSV_SurrenderValue()));
//				System.out.println("GSV_surrendr_val " + GSV_surrendr_val);
				bussIll.append("<GSV_surrendr_val" + _year_F + ">"
						+ GSV_surrendr_val + "</GSV_surrendr_val" + _year_F
						+ ">");*/

                //	System.out.println("GSV_surrendr_val "+GSV_surrendr_val);

                if (year_F <= premiumPayingTerm) {
                    sumtotalBasePremiumPaidforSurr = (sumtotalBasePremiumPaidforSurr + Math.round(totalBasePremiumPaidPPT));
                    //	System.out.println("totalBasePremiumPaidPPT "+totalBasePremiumPaidPPT);
                }

                smpBusinessLogic.setGSV_SurrenderValue(year_F,
                        sumSurvivalbenefit, firstYearoftotalbasePrem, sumtotalBasePremiumPaidforSurr);

                GSV_surrendr_val = commonForAllProd.getRound(commonForAllProd
                        .getStringWithout_E(smpBusinessLogic
                                .getGSV_SurrenderValue()));
                //System.out.println("GSV_surrendr_val " + GSV_surrendr_val);
                bussIll.append("<GSV_surrendr_val" + _year_F + ">"
                        + GSV_surrendr_val + "</GSV_surrendr_val" + _year_F
                        + ">");


                sumSurvivalbenefit = sumSurvivalbenefit + SurvivalBenefit;

                //added by sujata on 22-02-2020
                smpBusinessLogic.setNonGuarateedDeathBenefitAt_4_PercentSur(sumAssured, year_F, policyTerm);
                NonGuarateedDeathBenefitAt_4_PercentSURR = smpBusinessLogic.getNonGuarateedDeathBenefitAt_4_PercentSur();

                //System.out.println("NonGuarateedDeathBenefitAt_4_PercentSURR "+NonGuarateedDeathBenefitAt_4_PercentSURR);
                smpBusinessLogic.setNonGuarateedDeathBenefitAt_8_PercentSur(sumAssured, year_F, policyTerm);
                NonGuarateedDeathBenefitAt_8_PercentSURR = smpBusinessLogic.getNonGuarateedDeathBenefitAt_8_PercentSur();
                //System.out.println("NonGuarateedDeathBenefitAt_8_PercentSURR "+NonGuarateedDeathBenefitAt_8_PercentSURR);

                smpBusinessLogic.setNonGSV_surrndr_val_4_Percent(year_F,
                        NonGuarateedDeathBenefitAt_4_Percent);
                NonGSV_surrndr_val_4Percent = commonForAllProd
                        .getStringWithout_E((smpBusinessLogic
                                .getNonGSV_surrndr_val_4_Percent()));
//				System.out.println("NonGSV_surrndr_val_4Percent "
//						+ NonGSV_surrndr_val_4Percent);
                bussIll.append("<NonGSV_surrndr_val_4Percent").append(_year_F).append(">").append(NonGSV_surrndr_val_4Percent).append("</NonGSV_surrndr_val_4Percent").append(_year_F).append(">");

                smpBusinessLogic.setNonGSV_surrndr_val_8_Percent(year_F,
                        NonGuarateedDeathBenefitAt_8_Percent);// NonGuarateedDeathBenefitAt_8_PercentSURR
                NonGSV_surrndr_val_8Percent = commonForAllProd
                        .getStringWithout_E(Double.parseDouble(""
                                + (smpBusinessLogic
                                .getNonGSV_surrndr_val_8_Percent())));
//				System.out.println("NonGSV_surrndr_val_8Percent "
//						+ NonGSV_surrndr_val_8Percent);
                bussIll.append("<NonGSV_surrndr_val_8Percent").append(_year_F).append(">").append(NonGSV_surrndr_val_8Percent).append("</NonGSV_surrndr_val_8Percent").append(_year_F).append(">");


                smpBusinessLogic.getTotalMaturityBenefit4percent(year_F, policyTerm, GuaranteedSurvivalBenefit, NonGuarateedDeathBenefitAt_4_Percent);
                TotalMaturityBenefit4percent = commonForAllProd.getRound(commonForAllProd
                        .getStringWithout_E(smpBusinessLogic
                                .getTotalMaturityBenefit4percent(year_F, policyTerm, GuaranteedSurvivalBenefit, NonGuarateedDeathBenefitAt_4_Percent)));

                bussIll.append("<TotalMaturityBenefit4percent").append(_year_F).append(">").append(TotalMaturityBenefit4percent).append("</TotalMaturityBenefit4percent").append(_year_F).append(">");


                smpBusinessLogic.getTotalMaturityBenefit8percent(year_F, policyTerm, GuaranteedSurvivalBenefit, NonGuarateedDeathBenefitAt_8_Percent);
                TotalMaturityBenefit8Percent = commonForAllProd.getRound(commonForAllProd
                        .getStringWithout_E(smpBusinessLogic
                                .getTotalMaturityBenefit8percent(year_F, policyTerm, GuaranteedSurvivalBenefit, NonGuarateedDeathBenefitAt_8_Percent)));

                bussIll.append("<TotalMaturityBenefit8Percent").append(_year_F).append(">").append(TotalMaturityBenefit8Percent).append("</TotalMaturityBenefit8Percent").append(_year_F).append(">");


                TotalDeathBenefit4percent = commonForAllProd.getRound(commonForAllProd
                        .getStringWithout_E(smpBusinessLogic.getTotalDeathBenefit4percent(year_F, policyTerm, GuaranteedDeathBenefit, NonGuarateedDeathBenefitAt_4_Percent, sumtotalBasePremiumPaidforSurr)));

                bussIll.append("<TotalDeathBenefit4percent").append(_year_F).append(">").append(TotalDeathBenefit4percent).append("</TotalDeathBenefit4percent").append(_year_F).append(">");

                TotalDeathBenefit8percent = commonForAllProd.getRound(commonForAllProd
                        .getStringWithout_E(smpBusinessLogic.getTotalDeathBenefit8percent(year_F, policyTerm, GuaranteedDeathBenefit, NonGuarateedDeathBenefitAt_8_Percent, sumtotalBasePremiumPaidforSurr)));

                bussIll.append("<TotalDeathBenefit8percent").append(_year_F).append(">").append(TotalDeathBenefit8percent).append("</TotalDeathBenefit8percent").append(_year_F).append(">");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new String[]{
                (commonForAllProd.getStringWithout_E(premiumwithRoundUP)),
                commonForAllProd.getStringWithout_E(FirstYear_ServiceTax),
                commonForAllProd.getStringWithout_E(premiumWithST),

                commonForAllProd.getStringWithout_E(GuaranteedSurvivalBenefit),
                (commonForAllProd
                        .getStringWithout_E(NonGuarateedSurvivalBenefitAt_4_Percent)),
                commonForAllProd
                        .getStringWithout_E(NonGuarateedSurvivalBenefitAt_8_Percent),
                commonForAllProd.getStringWithout_E(discountPercentage),
                premiumWithoutSTwithoutStaffWithoutRound,
                premiumWithoutSTwithoutStaffwithRoundUP, MinesOccuInterest,
                BackDateinterestwithGST,

                commonForAllProd.getStringWithout_E(SecondYear_ServiceTax),
                commonForAllProd.getStringWithout_E(premiumWithSTSecondYear),
                commonForAllProd.getStringWithout_E(basicServiceTax),
                commonForAllProd.getStringWithout_E(SBCServiceTax),
                commonForAllProd.getStringWithout_E(KKCServiceTax),
                commonForAllProd.getStringWithout_E(basicServiceTaxSecondYear),
                commonForAllProd.getStringWithout_E(SBCServiceTaxSecondYear),
                commonForAllProd.getStringWithout_E(KKCServiceTaxSecondYear),
                servicetax_MinesOccuInterest,
                commonForAllProd.getStringWithout_E(KeralaCessServiceTax),
                commonForAllProd.getStringWithout_E(KeralaCessServiceTaxSecondYear)

        };
        // return null;
    }

    // /********************************** Added by Akshaya on 04-AUG-2015 start
    // ***************************/
    // boolean valBackdate() {
    //
    // try {
    //
    // String error = "";
    //
    // if (rb_smart_money_planner_backdating_yes.isChecked()) {
    //
    // final Calendar c = Calendar.getInstance();
    // final int currYear = c.get(Calendar.YEAR);
    // final int currMonth = c.get(Calendar.MONTH) + 1;
    //
    // SimpleDateFormat dateformat1 = new SimpleDateFormat(
    // "dd-MM-yyyy");
    // Date dtBackDate = dateformat1
    // .parse(btn_smart_money_planner_backdatingdate.getText()
    // .toString());
    // Date currentDate = c.getTime();
    //
    // Date finYerEndDate = null;
    //
    // if (currMonth >= 4) {
    // finYerEndDate = dateformat1.parse("11-05-" + currYear);
    // } else {
    // finYerEndDate = dateformat1
    // .parse("11-05-" + (currYear - 1));
    // }
    //
    // if (currentDate.before(dtBackDate)) {
    // error = "Please enter backdation date between "
    // + dateformat1.format(finYerEndDate) + " and "
    // + dateformat1.format(currentDate);
    // } else if (dtBackDate.before(finYerEndDate)) {
    // error = "Please enter Backdation date between "
    // + dateformat1.format(finYerEndDate) + " and "
    // + dateformat1.format(currentDate);
    // }
    //
    // if (!error.equals("")) {
    // showAlert.setMessage(error);
    // showAlert.setNeutralButton("OK",
    // new DialogInterface.OnClickListener() {
    // @Override
    // public void onClick(DialogInterface dialog,
    // int which) {
    // }
    // });
    // showAlert.show();
    // return false;
    // }
    //
    // }
    //
    // } catch (Exception e) {
    // e.printStackTrace();
    // return false;
    // }
    //
    // return true;
    //
    // }

    private boolean valBackdate() {

        try {

            String error = "";

            if (rb_smart_money_planner_backdating_yes.isChecked()) {

                final Calendar c = Calendar.getInstance();
                final int currYear = c.get(Calendar.YEAR);
                final int currMonth = c.get(Calendar.MONTH) + 1;

                SimpleDateFormat dateformat1 = new SimpleDateFormat(
                        "dd-MM-yyyy");
                Date dtBackDate = dateformat1
                        .parse(btn_smart_money_planner_backdatingdate.getText()
                                .toString());
                Date currentDate = c.getTime();

                Date finYerEndDate;

                Date launchDate = dateformat1.parse("15-01-2020");

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

    }

    /********************************** Added by Akshaya on 04-AUG-2015 end ***************************/

    private void getInput(SmartMoneyPlannerBean smartMoneyPlannerBean) {

        inputVal = new StringBuilder();


        String LifeAssured_DOB = btn_bi_smart_money_planner_life_assured_date
                .getText().toString();
        String LifeAssured_age = edt_bi_smart_money_planner_life_assured_age
                .getText().toString();
        // String
        // LifeAssured_gender=spnr_bi_smart_money_planner_selGender.getSelectedItem().toString();

        String wish_to_backdate_flag;
        if (rb_smart_money_planner_backdating_yes.isChecked())
            wish_to_backdate_flag = "y";
        else
            wish_to_backdate_flag = "n";
        String backdate;
        if (wish_to_backdate_flag.equals("y"))
            backdate = btn_smart_money_planner_backdatingdate.getText()
                    .toString();
        else
            backdate = "";

        int age = smartMoneyPlannerBean.getAge();
        int plan = smartMoneyPlannerBean.getPlan();
        if (plan == 1) {
            plan_Name = "Plan 1";
        } else if (plan == 2) {
            plan_Name = "Plan 2";
        } else if (plan == 3) {
            plan_Name = "Plan 3";
        } else if (plan == 4) {
            plan_Name = "Plan 4";
        }
        int basicPolicyTerm = smartMoneyPlannerBean.getBasicTerm();
        int PremiumPayingTerm = smartMoneyPlannerBean.getPremPayingTerm();
        double basicSumAssured = smartMoneyPlannerBean.getBasicSA();
        String PremPayingMode = smartMoneyPlannerBean.getPremFreq();

        boolean isJKresident = smartMoneyPlannerBean.isJKResident();
        // boolean isJKresident = false;
        boolean isStaffOrNot = smartMoneyPlannerBean.getStaffDisc();
        boolean smokerOrNot;

        String is_Smoker_or_Not = "";
        smokerOrNot = is_Smoker_or_Not.equalsIgnoreCase("Smoker");

        inputVal.append("<?xml version='1.0' encoding='utf-8' ?><smartMoneyPlannerBean>");

        inputVal.append("<LifeAssured_title>").append(lifeAssured_Title).append("</LifeAssured_title>");
        inputVal.append("<LifeAssured_firstName>").append(lifeAssured_First_Name).append("</LifeAssured_firstName>");
        inputVal.append("<LifeAssured_middleName>").append(lifeAssured_Middle_Name).append("</LifeAssured_middleName>");
        inputVal.append("<LifeAssured_lastName>").append(lifeAssured_Last_Name).append("</LifeAssured_lastName>");
        inputVal.append("<LifeAssured_DOB>").append(LifeAssured_DOB).append("</LifeAssured_DOB>");
        inputVal.append("<LifeAssured_age>").append(LifeAssured_age).append("</LifeAssured_age>");
        inputVal.append("<gender>").append(gender).append("</gender>");

        inputVal.append("<proposer_title>").append("").append("</proposer_title>");
        inputVal.append("<proposer_firstName>").append("").append("</proposer_firstName>");
        inputVal.append("<proposer_middleName>").append("").append("</proposer_middleName>");
        inputVal.append("<proposer_lastName>").append("").append("</proposer_lastName>");
        inputVal.append("<proposer_DOB>").append("").append("</proposer_DOB>");
        inputVal.append("<proposer_age>").append("").append("</proposer_age>");
        inputVal.append("<proposer_gender>").append("").append("</proposer_gender>");

        inputVal.append("<product_name>").append(planName).append("</product_name>");
        inputVal.append("<product_Code>").append(product_Code).append("</product_Code>");
        inputVal.append("<product_UIN>").append(product_UIN).append("</product_UIN>");
        inputVal.append("<product_cateogory>").append(product_cateogory).append("</product_cateogory>");
        inputVal.append("<product_type>").append(product_type).append("</product_type>");

        inputVal.append("<proposer_Is_Same_As_Life_Assured>"
                + proposer_Is_Same_As_Life_Assured
                + "</proposer_Is_Same_As_Life_Assured>");

        inputVal.append("<isJKResident>").append(isJKresident).append("</isJKResident>");
        inputVal.append("<isStaff>").append(isStaffOrNot).append("</isStaff>");
        inputVal.append("<isSmoker>").append(smokerOrNot).append("</isSmoker>");
        inputVal.append("<age>").append(age).append("</age>");
        inputVal.append("<plan>").append(plan_Name).append("</plan>");

        inputVal.append("<policyTerm>").append(basicPolicyTerm).append("</policyTerm>");
        inputVal.append("<premiumPayingTerm>").append(PremiumPayingTerm).append("</premiumPayingTerm>");

        inputVal.append("<premFreq>").append(PremPayingMode).append("</premFreq>");
        inputVal.append("<sumAssured>").append(basicSumAssured).append("</sumAssured>");

        inputVal.append("<Wish_to_backdate_policy>").append(wish_to_backdate_flag).append("</Wish_to_backdate_policy>");
        inputVal.append("<backdating_Date>").append(backdate).append("</backdating_Date>");

        //Added Tushar Kadam Kerla Applicable Tax 7-Jun-2019
        String str_kerla_discount = "N";
        if (cb_kerladisc.isChecked()) {
            str_kerla_discount = "Y";
        }

        inputVal.append("<KFC>").append(str_kerla_discount).append("</KFC>");
        //End Added Tushar Kadam Kerla Applicable Tax 7-Jun-2019

        inputVal.append("</smartMoneyPlannerBean>");

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

                            btn_bi_smart_money_planner_life_assured_date
                                    .setText(date);
                            edt_bi_smart_money_planner_life_assured_age
                                    .setText(final_age);
                            // ageInYears
                            // .setSelection(getIndex(ageInYears, final_age));
                            // valAge();
                        } else {
                            commonMethods.BICommonDialog(context, "Minimum age should be 18 yrs for proposer");
                            btn_bi_smart_money_planner_life_assured_date
                                    .setText("Select Date");
                            edt_bi_smart_money_planner_life_assured_age.setText("");
                        }
                    }
                    break;

                case 5:
                    final_age = Integer.toString(age);
                    if (final_age.contains("-")) {
                        commonMethods.BICommonDialog(context, "Please fill Valid Birth Date");
                    } else {

                        updateMaximumAge();
                        // valAge();

                        if (18 <= age && age <= maxAge) {
                            lifeAssuredAge = final_age;
                            btn_bi_smart_money_planner_life_assured_date
                                    .setText(date);
                            edt_bi_smart_money_planner_life_assured_age
                                    .setText(final_age);

                            lifeAssured_date_of_birth = getDate1(date + "");
                            if (!proposer_Backdating_BackDate.equals("")) {
                                if (proposer_Backdating_WishToBackDate_Policy
                                        .equalsIgnoreCase("y")) {
                                    rb_smart_money_planner_backdating_no
                                            .setChecked(true);
                                    ll_smart_money_planner_backdating
                                            .setVisibility(View.GONE);
                                }
                                proposer_Backdating_BackDate = "";
                                btn_smart_money_planner_backdatingdate
                                        .setText("Select Date");
                            } else {

                                clearFocusable(btn_bi_smart_money_planner_life_assured_date);

                                setFocusable(edt_smart_money_planner_contact_no);
                                edt_smart_money_planner_contact_no.requestFocus();
                                /*
                                 * setFocusable(selPlan); selPlan.requestFocus();
                                 */
                            }

                        } else {
                            commonMethods.BICommonDialog(context, "Minimum Age should be 18 yrs and Maximum Age should be "
                                    + maxAge + " yrs For LifeAssured");
                            btn_bi_smart_money_planner_life_assured_date
                                    .setText("Select Date");
                            edt_bi_smart_money_planner_life_assured_age.setText("");
                            lifeAssured_date_of_birth = "";

                            clearFocusable(btn_bi_smart_money_planner_life_assured_date);
                            setFocusable(btn_bi_smart_money_planner_life_assured_date);
                            btn_bi_smart_money_planner_life_assured_date
                                    .requestFocus();
                        }
                    }
                    break;

                case 6:
                    if (age >= 0) {
                        proposer_Backdating_BackDate = date + "";
                        btn_smart_money_planner_backdatingdate
                                .setText(proposer_Backdating_BackDate);
                        clearFocusable(btn_smart_money_planner_backdatingdate);

                    } else {
                        commonMethods.dialogWarning(context, "Please Select Valid BackDating Date", true);
                        btn_smart_money_planner_backdatingdate
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
            // ageInYears.setSelection(getIndex(ageInYears, str_final_Age),
            // false);
            edt_bi_smart_money_planner_life_assured_age.setText(str_final_Age);
            valAge();

        }
    }

    private void Dialog() {

        final Dialog d = new Dialog(this);
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);
        d.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.BLACK));

        d.setContentView(R.layout.layout_smart_money_planner_bi_grid);

        TextView tv_proposername = d
                .findViewById(R.id.tv_proposername);
        TextView tv_proposal_number = d
                .findViewById(R.id.tv_proposal_number);
        TextView tv_proposal_channel = d
                .findViewById(R.id.tv_str_usertype);

        TextView tv_bi_smart_money_planner_life_assured_name = d
                .findViewById(R.id.tv_bi_smart_money_planner_life_assured_name);
        TextView tv_bi_smart_money_planner_life_assured_name2 = d
                .findViewById(R.id.tv_bi_smart_money_planner_life_assured_name2);
        TextView tv_bi_smart_money_planner_life_assured_age = d
                .findViewById(R.id.tv_bi_smart_money_planner_life_assured_age);
        TextView tv_bi_smart_money_planner_life_assured_age2 = d
                .findViewById(R.id.tv_bi_smart_money_planner_life_assured_age2);
        TextView tv_bi_smart_money_planner_life_assured_gender = d
                .findViewById(R.id.tv_bi_smart_money_planner_life_assured_gender);
        TextView tv_bi_smart_money_planner_life_assured_gender2 = d
                .findViewById(R.id.tv_bi_smart_money_planner_life_assured_gender2);

        TextView tv_bi_smart_money_planner_mode = d
                .findViewById(R.id.tv_bi_smart_money_planner_mode);

        TextView tv_bi_smart_money_planner_rate_of_taxes = d
                .findViewById(R.id.tv_bi_smart_money_planner_rate_of_taxes);
        TextView tv_premium_tag = d
                .findViewById(R.id.tv_premium_tag);


        TextView tv_bi_smart_money_planner_premium_pay_option = d
                .findViewById(R.id.tv_bi_smart_money_planner_premium_pay_option);
        TextView tv_bi_smart_money_planner_life_assured_state = d
                .findViewById(R.id.tv_bi_smart_money_planner_life_assured_state);
        TextView tv_bi_smart_money_planner_life_assured_premium_frequency = d
                .findViewById(R.id.tv_bi_smart_money_planner_life_assured_premium_frequency);

        TextView tv_bi_smart_money_planner_plan_proposed = d
                .findViewById(R.id.tv_bi_smart_money_planner_plan_proposed);
        TextView tv_bi_smart_money_planner_term = d
                .findViewById(R.id.tv_bi_smart_money_planner_term);
        TextView tv_bi_smart_money_planner_premium_paying_term = d
                .findViewById(R.id.tv_bi_smart_money_planner_premium_paying_term);
        TextView tv_bi_smart_money_planner_sum_assured = d
                .findViewById(R.id.tv_bi_smart_money_planner_sum_assured);
        TextView tv_bi_smart_money_planner_sum_assured_at_inception = d
                .findViewById(R.id.tv_bi_smart_money_planner_sum_assured_at_inception);
        TextView tv_bi_smart_money_planner_yearly_premium = d
                .findViewById(R.id.tv_bi_smart_money_planner_yearly_premium);

        TextView tv_bi_smart_money_planner_basic_premium = d
                .findViewById(R.id.tv_bi_smart_money_planner_basic_premium);
        TextView tv_bi_smart_money_planner_service_tax = d
                .findViewById(R.id.tv_bi_smart_money_planner_service_tax);
        TextView tv_bi_smart_money_planner_yearly_premium_with_tax = d
                .findViewById(R.id.tv_bi_smart_money_planner_yearly_premium_with_tax);

        // First year policy
        TextView tv_bi_smart_money_planner_basic_premium_first_year = d
                .findViewById(R.id.tv_bi_smart_money_planner_basic_premium_first_year);
        TextView tv_bi_smart_money_planner_service_tax_first_year = d
                .findViewById(R.id.tv_bi_smart_money_planner_service_tax_first_year);
        TextView tv_bi_smart_money_planner_yearly_premium_with_tax_first_year = d
                .findViewById(R.id.tv_bi_smart_money_planner_yearly_premium_with_tax_first_year);

        // Seconf year policy onwards
        TextView tv_bi_smart_money_planner_basic_premium_second_year = d
                .findViewById(R.id.tv_bi_smart_money_planner_basic_premium_second_year);
        TextView tv_bi_smart_money_planner_service_tax_second_year = d
                .findViewById(R.id.tv_bi_smart_money_planner_service_tax_second_year);
        TextView tv_bi_smart_money_planner_yearly_premium_with_tax_second_year = d
                .findViewById(R.id.tv_bi_smart_money_planner_yearly_premium_with_tax_second_year);


        TextView tv_bi_smart_money_planner_basic_premium_second_year_2 = d
                .findViewById(R.id.tv_bi_smart_money_planner_basic_premium_second_year_2);
        TextView tv_bi_smart_money_planner_yearly_premium_with_tax_second_year_2 = d
                .findViewById(R.id.tv_bi_smart_money_planner_yearly_premium_with_tax_second_year_2);


        TableRow tr_smart_money_planner_surrender_value = d
                .findViewById(R.id.tr_smart_money_planner_surrender_value);
        final TextView edt_proposer_name = d
                .findViewById(R.id.edt_ProposalName);
        GridView gv_userinfo = d.findViewById(R.id.gv_userinfo_new);
        final EditText edt_Policyholderplace = d
                .findViewById(R.id.edt_Policyholderplace);

        //
        // M_ChannelDetails list_channel_detail = db.getChannelDetail(sr_Code);
        //
        // edt_Policyholderplace.setText(list_channel_detail.getBranchName());

        final TextView tv_premium_type = d
                .findViewById(R.id.tv_premium_type);
        final TextView tv_premium_install_type = d
                .findViewById(R.id.tv_premium_install_rider_type);
        final TextView tv_premium_install_type1 = d
                .findViewById(R.id.tv_premium_install_rider_type1);

        TextView tv_premium_type_rider = d
                .findViewById(R.id.tv_premium_type_rider);


        TextView tv_bi_is_Staff = d
                .findViewById(R.id.tv_bi_is_Staff);
        TextView tv_bi_is_JK = d.findViewById(R.id.tv_bi_is_JK);

        TextView tv_bi_smart_money_planner_backdating_interest = d
                .findViewById(R.id.tv_bi_smart_money_planner_backdating_interest);

        TextView tv_mandatory_bi_smart_money_planner_yearly_premium_with_tax = d
                .findViewById(R.id.tv_mandatory_bi_smart_money_planner_yearly_premium_with_tax);

        TextView tv_bi_smart_money_planner_plan_option_1 = d.findViewById(R.id.tv_bi_smart_money_planner_plan_option_1);
        TextView tv_bi_smart_money_planner_plan_option_2 = d.findViewById(R.id.tv_bi_smart_money_planner_plan_option_2);
        TextView tv_bi_smart_money_planner_plan_option_3 = d.findViewById(R.id.tv_bi_smart_money_planner_plan_option_3);
        TextView tv_bi_smart_money_planner_plan_option_4 = d.findViewById(R.id.tv_bi_smart_money_planner_plan_option_4);


        TextView tv_mandatory_bi_smart_money_planner_yearly_premium_with_tax1 = d
                .findViewById(R.id.tv_mandatory_bi_smart_money_planner_yearly_premium_with_tax1);

        TableRow tr_second_year = d
                .findViewById(R.id.tr_second_year);

        final CheckBox cb_statement = d
                .findViewById(R.id.cb_statement);
        cb_statement.setChecked(false);
        TextView tv_bi_smart_money_planner_basic_service_tax_first_year = d
                .findViewById(R.id.tv_bi_smart_money_planner_basic_service_tax_first_year);
        TextView tv_bi_smart_money_planner_swachh_bharat_cess_first_year = d
                .findViewById(R.id.tv_bi_smart_money_planner_swachh_bharat_cess_first_year);
        TextView tv_bi_smart_money_planner_krishi_kalyan_cess_first_year = d
                .findViewById(R.id.tv_bi_smart_money_planner_krishi_kalyan_cess_first_year);

        TextView tv_bi_smart_money_planner_basic_service_tax_second_year = d
                .findViewById(R.id.tv_bi_smart_money_planner_basic_service_tax_second_year);
        TextView tv_bi_smart_money_planner_swachh_bharat_cess_second_year = d
                .findViewById(R.id.tv_bi_smart_money_planner_swachh_bharat_cess_second_year);
        TextView tv_bi_smart_money_planner_krishi_kalyan_cess_second_year = d
                .findViewById(R.id.tv_bi_smart_money_planner_krishi_kalyan_cess_second_year);

        TextView tv_uin_smart_money_planner = d
                .findViewById(R.id.tv_uin_smart_money_planner);
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
        Button btn_proceed = d.findViewById(R.id.btn_proceed);

        Ibtn_signatureofMarketing = d
                .findViewById(R.id.Ibtn_signatureofMarketing);
        Ibtn_signatureofPolicyHolders = d
                .findViewById(R.id.Ibtn_signatureofPolicyHolders);

        btn_MarketingOfficalDate = d
                .findViewById(R.id.btn_MarketingOfficalDate);
        btn_PolicyholderDate = d
                .findViewById(R.id.btn_PolicyholderDate);
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
                            + ", have undergone the Need Analysis  after having reviewed the SBI Life Product options, I have opted for SBI LIFE - Smart Money Planner.");
            tv_proposername.setText(name_of_life_assured);
            tv_bi_smart_money_planner_life_assured_name
                    .setText(name_of_life_assured);
            tv_bi_smart_money_planner_life_assured_name2
                    .setText(name_of_life_assured);

        } else if (proposer_Is_Same_As_Life_Assured.equalsIgnoreCase("n")) {
            edt_proposer_name
                    .setText("I, "
                            + name_of_proposer
                            + ", having received the information with respect to the above, have understood the above statement before entering into the contract.");
            edt_proposer_name_need_analysis
                    .setText("I, "
                            + name_of_proposer
                            + ", have undergone the Need Analysis  after having reviewed the SBI Life Product options, I have opted for SBI LIFE - Smart Money Planner.");

            tv_proposername.setText(name_of_proposer);
            tv_bi_smart_money_planner_life_assured_name
                    .setText(name_of_proposer);
            tv_bi_smart_money_planner_life_assured_name2
                    .setText(name_of_proposer);
        }
        tv_proposal_number.setText(QuatationNumber);
        tv_proposal_channel.setText(userType);
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

        imageButtonSmartMoneyPlannerProposerPhotograph = d
                .findViewById(R.id.imageButtonSmartMoneyPlannerProposerPhotograph);
        imageButtonSmartMoneyPlannerProposerPhotograph
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


        if (photoBitmap != null) {
            imageButtonSmartMoneyPlannerProposerPhotograph
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
                            imageButtonSmartMoneyPlannerProposerPhotograph
                                    .setImageDrawable(getResources()
                                            .getDrawable(
                                                    R.drawable.focus_imagebutton_photo));
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
                // if (frmProductHome.equals("FALSE")) {
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
                        && (((photoBitmap != null) && radioButtonTrasactionModeParivartan.isChecked())
                        || radioButtonTrasactionModeManual.isChecked())) {
                    NeedAnalysisActivity.str_need_analysis = "";

                    // String isActive = "0";
                    String productCode = "";

                    if (smartMoneyPlannerBean.getPlan() == 1)
                        productCode = "SMP1";
                    else if (smartMoneyPlannerBean.getPlan() == 2)
                        productCode = "SMP2";
                    else if (smartMoneyPlannerBean.getPlan() == 3)
                        productCode = "SMP3";
                    else if (smartMoneyPlannerBean.getPlan() == 4)
                        productCode = "SMP4";

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
                            .getRound(basicplustax), emailId, mobileNo,
                            agentEmail, agentMobile, na_input, na_output,
                            premPayingMode, Integer.parseInt(policyTerm),
                            Integer.parseInt(premiumPayingTerm), productCode,
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
                            .getRound(basicplustax), agentEmail,
                            agentMobile, na_input, na_output, premPayingMode,
                            Integer.parseInt(policyTerm), Integer
                            .parseInt(premiumPayingTerm), productCode,
                            getDate(lifeAssured_date_of_birth), "", "", mode, inputVal
                            .toString(), retVal.toString().replace(
                            bussIll.toString(), "")));

                    CreateSmartMoneyPlannerPlusBIPdf();
                    NABIObj.serviceHit(BI_SmartMoneyPlannerActivity.this,
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
                        setFocusable(imageButtonSmartMoneyPlannerProposerPhotograph);
                        imageButtonSmartMoneyPlannerProposerPhotograph
                                .requestFocus();
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
        tv_uin_smart_money_planner
                .setText("Benefit Illustration for SBI LIFE - Smart Money Planner (UIN No -"
                        + "111N101V03" + ")");

        String input = inputVal.toString();
        output = retVal.toString();

        policyTerm = prsObj.parseXmlTag(input, "policyTerm");
        premiumPayingTerm = prsObj.parseXmlTag(input, "premiumPayingTerm");
        premPayingMode = prsObj.parseXmlTag(input, "premFreq");
        tv_bi_smart_money_planner_life_assured_name
                .setText(name_of_life_assured);
        age_entry = prsObj.parseXmlTag(input, "age");
        tv_bi_smart_money_planner_life_assured_age
                .setText(age_entry + " Years");
        tv_bi_smart_money_planner_life_assured_age2
                .setText(age_entry + " Years");

        premium_paying_frequency = prsObj.parseXmlTag(input, "premFreq");
        tv_bi_smart_money_planner_life_assured_gender.setText(gender);
        tv_bi_smart_money_planner_life_assured_gender2.setText(gender);

        if (spnr_bi_smart_money_planner_premium_frequency.getSelectedItem().toString().equalsIgnoreCase("Single")) {
            tv_bi_smart_money_planner_premium_pay_option.setText("Single");
            tv_premium_tag.setText("Single Premium");
        } else {
            tv_bi_smart_money_planner_premium_pay_option.setText("Limited");

        }

        premPayingMode = prsObj.parseXmlTag(input, "premFreq");
        tv_bi_smart_money_planner_mode.setText(premPayingMode);

        if (cb_kerladisc.isChecked()) {
            tv_bi_smart_money_planner_life_assured_state.setText("Kerala");
            tv_bi_smart_money_planner_rate_of_taxes.setText(" 4.75%  ");
        } else {
            tv_bi_smart_money_planner_life_assured_state.setText("Non Kerala");
            tv_bi_smart_money_planner_rate_of_taxes.setText(" 4.5% ");

        }

        if (cb_kerladisc.isChecked()) {
            if (spnr_bi_smart_money_planner_premium_frequency.getSelectedItem().toString().equalsIgnoreCase("Single")) {
                tv_bi_smart_money_planner_rate_of_taxes.setText(" 4.75%  ");
            } else {
                tv_bi_smart_money_planner_rate_of_taxes.setText("  4.75% in the 1st policy year and 2.375% from 2nd policy year onwards  ");
            }
        } else {
            if (spnr_bi_smart_money_planner_premium_frequency.getSelectedItem().toString().equalsIgnoreCase("Single")) {
                tv_bi_smart_money_planner_rate_of_taxes.setText(" 4.50%  ");
            } else {
                tv_bi_smart_money_planner_rate_of_taxes.setText(" 4.50% in the 1st policy year and 2.25% from 2nd policy year onwards ");
            }
        }


        tv_bi_smart_money_planner_life_assured_premium_frequency
                .setText(premium_paying_frequency);

        if (premium_paying_frequency.equals("Single")) {
            tr_smart_money_planner_surrender_value.setVisibility(View.GONE);
        }

        String staffdiscount = prsObj.parseXmlTag(input, "isStaff");

        if (staffdiscount.equalsIgnoreCase("true")) {
            tv_bi_is_Staff.setText("Yes");
        } else {
            tv_bi_is_Staff.setText("No");
        }

        String isJkResident = prsObj.parseXmlTag(input, "isJKResident");

        if (isJkResident.equalsIgnoreCase("true")) {
            tv_bi_is_JK.setText("Yes");
        } else {
            tv_bi_is_JK.setText("No");
        }

        plan = prsObj.parseXmlTag(input, "plan");

        if (plan.equals("Plan 1")) {
            tv_bi_smart_money_planner_plan_proposed.setText(plan);
        } else if (plan.equals("Plan 2")) {
            tv_bi_smart_money_planner_plan_proposed.setText(plan);
        } else if (plan.equals("Plan 3")) {
            tv_bi_smart_money_planner_plan_proposed.setText(plan);
        } else if (plan.equals("Plan 4")) {
            tv_bi_smart_money_planner_plan_proposed.setText(plan);
        }

        policy_term = prsObj.parseXmlTag(input, "policyTerm");
        tv_bi_smart_money_planner_term.setText(policy_term + " Years");

        premium_paying_term = prsObj.parseXmlTag(input, "premiumPayingTerm");

        if (!(premium_paying_frequency.equalsIgnoreCase("Single"))) {
            tv_bi_smart_money_planner_premium_paying_term
                    .setText(premium_paying_term + " ");
        } else {
            tv_bi_smart_money_planner_premium_paying_term.setText("1 ");
        }

        // premiumPaymentMode = prsObj.parseXmlTag(input, "premFreq");
        // String payingTerm = "";
        // if (premium_paying_frequency.equalsIgnoreCase("Single")) {
        // payingTerm = "1 Year";
        // } else {
        // payingTerm = policy_term + " Years";
        // }

        // tv_bi_smart_money_planner_premium_paying_term.setText(payingTerm);

        sum_assured = prsObj.parseXmlTag(input, "sumAssured");
        sum_assured_on_death = prsObj.parseXmlTag(output, "guarntdDeathBenft1");

        tv_bi_smart_money_planner_sum_assured.setText(""
                + getformatedThousandString(Integer.parseInt(obj.getRound(obj
                .getStringWithout_E(Double.valueOf((sum_assured
                        .equals("") || sum_assured == null) ? "0"
                        : sum_assured))))));
        tv_bi_smart_money_planner_sum_assured_at_inception.setText(""
                + getformatedThousandString(Integer.parseInt(obj.getRound(obj
                .getStringWithout_E(Double.valueOf((sum_assured_on_death
                        .equals("") || sum_assured_on_death == null) ? "0"
                        : sum_assured_on_death))))));

        if (premium_paying_frequency.equals("Yearly")) {
            //tv_premium_type.setText("Yearly " + "premium ");
            tv_premium_type_rider.setText("Yearly " + "premium with Applicable Taxes");
            tv_premium_install_type.setText("Yearly " + " Premium");
            tv_mandatory_bi_smart_money_planner_yearly_premium_with_tax
                    .setText("Yearly " + " Premium with Applicable Taxes");
            //tv_premium_install_type1.setText("Yearly " + " Premium (Rs)");
            tv_mandatory_bi_smart_money_planner_yearly_premium_with_tax1
                    .setText("Yearly " + " Premium with Applicable Taxes (Rs.)");

        } else if (premium_paying_frequency.equals("Half Yearly")) {
            //tv_premium_type.setText("Half Yearly " + "premium ");
            tv_premium_type_rider.setText("Half-Yearly " + "premium with Applicable Taxes");
            tv_premium_install_type.setText("Half Yearly "
                    + " Premium");
            tv_mandatory_bi_smart_money_planner_yearly_premium_with_tax
                    .setText("Half Yearly " + " Premium with Applicable Taxes");
				/*tv_premium_install_type1.setText("Half Yearly "
						+ " Premium (Rs)");*/
            tv_mandatory_bi_smart_money_planner_yearly_premium_with_tax1
                    .setText("Half Yearly " + " Premium with Applicable Taxes (Rs.)");

        } else if (premium_paying_frequency.equals("Quarterly")) {
            //tv_premium_type.setText("Quarterly " + "premium ");
            tv_premium_type_rider.setText("Quarterly " + "premium with Applicable Taxes");
            tv_premium_install_type.setText("Quarterly "
                    + " Premium");
            tv_mandatory_bi_smart_money_planner_yearly_premium_with_tax
                    .setText("Quarterly " + " Premium with Applicable Taxes");
				/*tv_premium_install_type1.setText("Quarterly "
						+ " Premium (Rs)");*/
            tv_mandatory_bi_smart_money_planner_yearly_premium_with_tax1
                    .setText("Quarterly " + " Premium with Applicable Taxes (Rs.)");

        } else if (premium_paying_frequency.equals("Monthly")) {
            //tv_premium_type.setText("Monthly " + "premium ");
            tv_premium_type_rider.setText("Monthly " + "premium with Applicable Taxes");
            tv_premium_install_type.setText("Monthly " + " Premium");
            tv_mandatory_bi_smart_money_planner_yearly_premium_with_tax
                    .setText("Monthly " + " Premium with Applicable Taxes");
				/*tv_premium_install_type1
						.setText("Monthly " + " Premium (Rs)");*/
            tv_mandatory_bi_smart_money_planner_yearly_premium_with_tax1
                    .setText("Monthly " + " Premium with Applicable Taxes (Rs.)");

        } else if (premium_paying_frequency.equals("Monthly ECS")) {
            //tv_premium_type.setText("Monthly-ECS " + "premium ");
            tv_premium_type_rider.setText("Monthly-ECS " + "premium with Applicable Taxes");
            tv_premium_install_type.setText("Monthly ECS "
                    + " Premium");

        } else if (premium_paying_frequency.equals("Monthly-SI/CC")) {
            tv_premium_type.setText("Monthly-SI/CC " + "premium ");
            tv_premium_type_rider
                    .setText("Monthly-SI/CC " + "premium with Applicable Taxes");
            tv_premium_install_type.setText("Monthly-SI/CC "
                    + " Premium");

        } else if (premium_paying_frequency.equals("Single")) {
            tv_premium_type.setText("Single " + "premium ");
            tv_premium_type_rider.setText("Single " + "premium with Applicable Taxes");
            tv_premium_install_type.setText("Single " + " Premium");
            tv_mandatory_bi_smart_money_planner_yearly_premium_with_tax
                    .setText("Single " + " Premium with Applicable Taxes");
            //tv_premium_install_type1.setText("Single " + " Premium (Rs)");
            tv_mandatory_bi_smart_money_planner_yearly_premium_with_tax1
                    .setText("Single " + " Premium with Applicable Taxes (Rs.)");
        }

        String premium = prsObj.parseXmlTag(output, "premInst");
        tv_bi_smart_money_planner_yearly_premium.setText("Rs "
                + getformatedThousandString(Integer.parseInt(obj.getRound(obj.getStringWithout_E(Double.valueOf((premium
                .equals("") || premium == null) ? "0" : premium))))));

        basicprem = prsObj.parseXmlTag(output, "premInst");


        tv_bi_smart_money_planner_plan_option_1.setText(plan);
        tv_bi_smart_money_planner_plan_option_2.setText(plan);
        tv_bi_smart_money_planner_plan_option_3.setText(plan);
        tv_bi_smart_money_planner_plan_option_4.setText(plan);


        tv_bi_smart_money_planner_basic_premium.setText(" Rs "
                + getformatedThousandString(Integer.parseInt(obj.getRound(obj.getStringWithout_E(Double.valueOf(
                basicprem
                        .equals("") ? "0" : basicprem))))));
        tv_bi_smart_money_planner_basic_premium_first_year.setText(""
                + getformatedThousandString(Integer.parseInt(obj.getRound(obj.getStringWithout_E(Double.valueOf(
                basicprem
                        .equals("") ? "0" : basicprem))))));

        servicetax = prsObj.parseXmlTag(output, "servcTax");

        String servicetax_second_year = prsObj.parseXmlTag(output,
                "servcTaxSecondYear");

        tv_bi_smart_money_planner_service_tax.setText(" Rs "
                + getformatedThousandString(Integer.parseInt(obj.getRound(obj.getStringWithout_E(Double.valueOf(servicetax
                .equals("") ? "0" : servicetax))))));
        tv_bi_smart_money_planner_service_tax_first_year.setText(""
                + getformatedThousandString(Integer.parseInt(obj.getRound(obj.getStringWithout_E(Double.valueOf(servicetax
                .equals("") ? "0" : servicetax))))));

        basicplustax = prsObj.parseXmlTag(output, "premWthST");

        String yearlypremium_with_servicetax_second_year = prsObj.parseXmlTag(output,
                "premWthSTSecondYear");

        tv_bi_smart_money_planner_yearly_premium_with_tax
                .setText(" Rs "
                        + getformatedThousandString(Integer.parseInt(obj.getRound(obj.getStringWithout_E(Double
                        .valueOf(basicplustax.equals("") ? "0"
                                : basicplustax))))));
        tv_bi_smart_money_planner_yearly_premium_with_tax_first_year
                .setText(""
                        + getformatedThousandString(Integer.parseInt(obj.getRound(obj.getStringWithout_E(Double
                        .valueOf(basicprem.equals("") ? "0"
                                : basicprem))))));

        // Amit changes start- 23-5-2016
        // basicServiceTax = prsObj.parseXmlTag(output, "basicServiceTax");
        String basicServiceTax = prsObj.parseXmlTag(output, "servcTax");

        tv_bi_smart_money_planner_basic_service_tax_first_year.setText(""
                + obj.getRound(obj.getStringWithout_E(Double
                .valueOf(basicServiceTax.equals("") ? "0"
                        : basicServiceTax))));

        String SBCServiceTax = prsObj.parseXmlTag(output, "SBCServiceTax");

        tv_bi_smart_money_planner_swachh_bharat_cess_first_year
                .setText(""
                        + obj.getRound(obj.getStringWithout_E(Double
                        .valueOf(SBCServiceTax.equals("") ? "0"
                                : SBCServiceTax))));

        String KKCServiceTax = prsObj.parseXmlTag(output, "KKCServiceTax");

        tv_bi_smart_money_planner_krishi_kalyan_cess_first_year
                .setText(""
                        + obj.getRound(obj.getStringWithout_E(Double
                        .valueOf(KKCServiceTax.equals("") ? "0"
                                : KKCServiceTax))));
        // Amit changes end- 23-5-2016

        // tr_second_year.setVisibility(View.GONE);
        tv_bi_smart_money_planner_basic_premium_second_year.setText(""
                + getformatedThousandString(Integer.parseInt(obj.getRound(obj.getStringWithout_E(Double
                .valueOf(basicplustax.equals("") ? "0"
                        : basicplustax))))));

        tv_bi_smart_money_planner_service_tax_second_year.setText(""
                + getformatedThousandString(Integer.parseInt(obj.getRound(obj.getStringWithout_E(Double
                .valueOf(servicetax_second_year.equals("") ? "0"
                        : servicetax_second_year))))));

        tv_bi_smart_money_planner_yearly_premium_with_tax_second_year
                .setText(""
                        + getformatedThousandString(Integer.parseInt(obj.getRound(obj.getStringWithout_E(Double
                        .valueOf(basicplustax
                                .equals("") ? "0"
                                : basicplustax))))));

        tr_second_year.setVisibility(View.VISIBLE);
        if (!smartMoneyPlannerBean.getPremFreq().equalsIgnoreCase("Single")) {

            tv_bi_smart_money_planner_basic_premium_second_year_2.setText(""
                    + getformatedThousandString(Integer.parseInt(obj.getRound(obj.getStringWithout_E(Double
                    .valueOf(yearlypremium_with_servicetax_second_year.equals("") ? "0"
                            : yearlypremium_with_servicetax_second_year))))));

            tv_bi_smart_money_planner_yearly_premium_with_tax_second_year_2.setText(""
                    + getformatedThousandString(Integer.parseInt(obj.getRound(obj.getStringWithout_E(Double
                    .valueOf(yearlypremium_with_servicetax_second_year.equals("") ? "0"
                            : yearlypremium_with_servicetax_second_year))))));

            String basicServiceTaxSecondYear = prsObj.parseXmlTag(output,
                    "servcTaxSecondYear");

            tv_bi_smart_money_planner_basic_service_tax_second_year.setText(""
                    + getformatedThousandString(Integer.parseInt(obj.getRound(obj.getStringWithout_E(Double
                    .valueOf(basicServiceTaxSecondYear.equals("") ? "0"
                            : basicServiceTaxSecondYear))))));

            String SBCServiceTaxSecondYear = prsObj.parseXmlTag(output,
                    "SBCServiceTaxSecondYear");

            tv_bi_smart_money_planner_swachh_bharat_cess_second_year.setText(""
                    + obj.getRound(obj.getStringWithout_E(Double
                    .valueOf(SBCServiceTaxSecondYear.equals("") ? "0"
                            : SBCServiceTaxSecondYear))));

            String KKCServiceTaxSecondYear = prsObj.parseXmlTag(output,
                    "KKCServiceTaxSecondYear");

            tv_bi_smart_money_planner_krishi_kalyan_cess_second_year.setText(""
                    + obj.getRound(obj.getStringWithout_E(Double
                    .valueOf(KKCServiceTaxSecondYear.equals("") ? "0"
                            : KKCServiceTaxSecondYear))));
            // Amit changes end- 23-5-2016
        } else {
            TextView tvRiderSecondYear = d.findViewById(R.id.tvRiderSecondYear);
            tv_bi_smart_money_planner_basic_premium_second_year_2.setText("NA");
            tv_bi_smart_money_planner_yearly_premium_with_tax_second_year_2.setText("NA");
            tv_bi_smart_money_planner_basic_service_tax_second_year.setText("NA");
            tv_bi_smart_money_planner_swachh_bharat_cess_second_year.setText("NA");
            tv_bi_smart_money_planner_krishi_kalyan_cess_second_year.setText("NA");
            tvRiderSecondYear.setText("NA");

        }

        BackdatingInt = prsObj.parseXmlTag(output, "backdateInt");

        tv_bi_smart_money_planner_backdating_interest
                .setText(" Rs "
                        + getformatedThousandString(Integer.parseInt(obj.getRound(obj.getStringWithout_E(Double
                        .valueOf(BackdatingInt.equals("") ? "0"
                                : BackdatingInt))))));

        TextView tv_Company_policy_surrender_dec = d
                .findViewById(R.id.tv_Company_policy_surrender_dec);

        String str_prem_pay = "";

        if (premium_paying_frequency.equalsIgnoreCase("Single")) {
            str_prem_pay = "Single";
            Company_policy_surrender_dec = "Your SBI Life - Smart Money Planner (UIN : 111N101V03) is a "
                    + "Single"
                    + " premium policy and you are required to pay One Time Premium of Rs "
                    + getformatedThousandString(Integer.parseInt(obj.getRound(obj.getStringWithout_E(Double
                    .valueOf(basicplustax.equals("") ? "0" : basicplustax)))))
                    + " .Your Policy Term is "
                    + policy_term
                    + " years"
                    //+ " .Your Premium Payment Term is "
                    //+ premium_paying_term
                    + " and Basic Sum Assured is Rs. "
                    + getformatedThousandString(Integer.parseInt(obj.getRound(obj
                    .getStringWithout_E(Double.valueOf((sum_assured
                            .equals("") || sum_assured == null) ? "0"
                            : sum_assured)))));
        } else {
            str_prem_pay = "Regular/Limited";
            Company_policy_surrender_dec = "Your SBI Life - Smart Money Planner (UIN : 111N101V03) is a "
                    + "Limited"
                    + " premium policy, for which your first year " + premium_paying_frequency + " Premium is Rs "
                    + getformatedThousandString(Integer.parseInt(obj.getRound(obj.getStringWithout_E(Double
                    .valueOf(basicplustax.equals("") ? "0" : basicplustax)))))
                    + " .Your Policy Term is "
                    + policy_term
                    + " years"
                    + " .Your Premium Payment Term is "
                    + premium_paying_term
                    + " years and Basic Sum Assured is Rs. "
                    + getformatedThousandString(Integer.parseInt(obj.getRound(obj
                    .getStringWithout_E(Double.valueOf((sum_assured
                            .equals("") || sum_assured == null) ? "0"
                            : sum_assured)))));
        }


        //tv_Company_policy_surrender_dec.setText(Company_policy_surrender_dec);

        for (int i = 1; i <= Integer.parseInt(policy_term); i++) {

            String death_gurantee = prsObj.parseXmlTag(output,
                    "guarntdDeathBenft" + i + "");

            String maturity_benefit_gurantee = prsObj.parseXmlTag(output,
                    "guaranMatBen" + i + "");

            String surrender_value_gurantee = prsObj.parseXmlTag(output,
                    "GSV_surrendr_val" + i + "");

            String policy_year = prsObj
                    .parseXmlTag(output, "policyYr" + i + "");
            String total_base_premium_without_tax = prsObj.parseXmlTag(output,
                    "totaBasePrem" + i + "");
            String annulizedprem = prsObj.parseXmlTag(output,
                    "AnnulizedPrem" + i + "");

            String Guarnteedaddition = prsObj.parseXmlTag(output,
                    "GuaranteedAdd" + i + "");
            String survivalBen = prsObj.parseXmlTag(output,
                    "guaranSurvivalBenefit" + i + "");
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
            String nongrntdDeathNenft_4Percent = prsObj
                    .parseXmlTag(output, "nongrntdDeathNenft_4Percent" + i + "");
            String nongrntdDeathNenft_8Percent = prsObj
                    .parseXmlTag(output, "nongrntdDeathNenft_8Percent" + i + "");

            String surrender_value_ssv_4_percentage = prsObj.parseXmlTag(
                    output, "NonGSV_surrndr_val_4Percent" + i + "");
            String surrender_value_ssv_8_percentage = prsObj.parseXmlTag(
                    output, "NonGSV_surrndr_val_8Percent" + i + "");


            String TotalMaturity_4percent = prsObj.parseXmlTag(
                    output, "TotalMaturityBenefit4percent" + i + "");
            String TotalMaturity_8percent = prsObj.parseXmlTag(
                    output, "TotalMaturityBenefit8Percent" + i + "");

            String TotalDeathBenefit4percent = prsObj.parseXmlTag(
                    output, "TotalDeathBenefit4percent" + i + "");

            String TotalDeathBenefit8percent = prsObj.parseXmlTag(
                    output, "TotalDeathBenefit8percent" + i + "");

            list_data
                    .add(new M_BI_SmartMoneyPlanner_AdapterCommon(
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
                                    .valueOf(nongrntdDeathNenft_4Percent))))
                                    + "",
                            "0",
                            (obj.getRound(obj.getStringWithout_E(Double
                                    .valueOf(surrender_value_ssv_4_percentage))))
                                    + "",
                            (obj.getRound(obj.getStringWithout_E(Double
                                    .valueOf(nongrntdDeathNenft_8Percent))))
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
        Adapter_BI_SmartMoneyPlannertGridCommon adapter = new Adapter_BI_SmartMoneyPlannertGridCommon(
                BI_SmartMoneyPlannerActivity.this, list_data);
        gv_userinfo.setAdapter(adapter);

        GridHeight gh = new GridHeight();
        gh.getheight(gv_userinfo, policy_term);

        d.show();

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
                        d.dismiss();

                    }

                }
            }
        } else if (requestCode == 3) {
            if (resultCode == RESULT_OK) {
                if (Check.equals("Photo")) {

                    File Photo = commonMethods.galleryAddPic(context);
                    Bitmap bmp = BitmapFactory.decodeFile(Photo.getAbsolutePath());

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
                        imageButtonSmartMoneyPlannerProposerPhotograph
                                .setImageBitmap(scaled);
                    } else {
                        photoBitmap = null;
                    }
                }
            }
        }
    }

    private void CreateSmartMoneyPlannerPlusBIPdf() {

        // String quotation_Number = ProductHomePageActivity.quotation_Number;
        try {

            Font normal = new Font(Font.FontFamily.TIMES_ROMAN, 5, Font.NORMAL);
            Font normal_bold = new Font(Font.FontFamily.TIMES_ROMAN, 5,
                    Font.BOLD);
            Font normal1_bold = new Font(Font.FontFamily.TIMES_ROMAN, 6,
                    Font.BOLD);
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
                            "Benefit Illustration (BI) : SBI Life - Smart Money Planner (UIN :  111N101V03) | An Individual, Non-linked, Participating, Life Insurance Savings Product"
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
                    "Registered & Corporate Office: 'Natraj', M.V.Road and Western Express Highway Junction, Andheri (East), Mumbai - 400069 ",
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
            // CR_table6.setWidths(columnWidths_2);
            PdfPCell BI_Pdftable2_cell11 = new PdfPCell(
                    new Paragraph(
                            "The two rates of investment return declared by the Life Insurance Council are 4% and 8% per annum.",
                            small_normal));

            BI_Pdftable2_cell11.setPadding(5);

            BI_Pdftable21.addCell(BI_Pdftable2_cell11);
            //document.add(BI_Pdftable21);


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
                            "Some benefits are guaranteed and some benefits are variable with returns based on the future performance of your Insurer carrying on life insurance business. If your policy offers guaranteed returns then these will be clearly marked 'guaranteed' in the illustration table on this page. If your policy offers variable returns then the illustrations on this page will show two different rates of assumed future investment returns. These assumed rates of return are not guaranteed and they are not the upper or lower limits of what you might get back, as the value of your policy is dependent on a number of factors including future investment performance.",
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
            PdfPCell cell_lLifeAssuredName2 = new PdfPCell(new Paragraph(
                    name_of_proposer, small_bold));
            cell_lLifeAssuredName2.setPadding(5);
            cell_lLifeAssuredName2.setHorizontalAlignment(Element.ALIGN_CENTER);

            PdfPCell cell_LifeAssuredName11 = new PdfPCell(new Paragraph(
                    "Name of the Life Assured", small_normal));
            cell_LifeAssuredName11.setPadding(5);
            PdfPCell cell_lLifeAssuredName22 = new PdfPCell(new Paragraph(
                    name_of_proposer, small_bold));
            cell_lLifeAssuredName22.setPadding(5);
            cell_lLifeAssuredName22.setHorizontalAlignment(Element.ALIGN_CENTER);

            PdfPCell cell_lifeAssuredAge1 = new PdfPCell(new Paragraph(
                    "Age (Years)", small_normal));
            cell_lifeAssuredAge1.setPadding(5);
            PdfPCell cell_lifeAssuredAge2 = new PdfPCell(new Paragraph(
                    age_entry + "", small_bold));
            cell_lifeAssuredAge2.setPadding(5);
            cell_lifeAssuredAge2.setHorizontalAlignment(Element.ALIGN_CENTER);

            PdfPCell cell_lifeAssuredAge11 = new PdfPCell(new Paragraph(
                    "Age (Years)", small_normal));
            cell_lifeAssuredAge11.setPadding(5);
            PdfPCell cell_lifeAssuredAge22 = new PdfPCell(new Paragraph(
                    age_entry + "", small_bold));
            cell_lifeAssuredAge22.setPadding(5);
            cell_lifeAssuredAge22.setHorizontalAlignment(Element.ALIGN_CENTER);

            table_lifeAssuredName.addCell(cell_LifeAssuredName1);
            table_lifeAssuredName.addCell(cell_lLifeAssuredName2);
            table_lifeAssuredName.addCell(cell_LifeAssuredName11);
            table_lifeAssuredName.addCell(cell_lLifeAssuredName22);
            table_lifeAssuredName.addCell(cell_lifeAssuredAge1);
            table_lifeAssuredName.addCell(cell_lifeAssuredAge2);
            table_lifeAssuredName.addCell(cell_lifeAssuredAge11);
            table_lifeAssuredName.addCell(cell_lifeAssuredAge22);
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

            PdfPCell cell_lifeAssuredAmaturityGender11 = new PdfPCell(
                    new Paragraph("Gender", small_normal));
            cell_lifeAssuredAmaturityGender11.setPadding(5);
            PdfPCell cell_lifeAssuredAmaturityGender22 = new PdfPCell(
                    new Paragraph(gender, small_bold));
            cell_lifeAssuredAmaturityGender22.setPadding(5);
            cell_lifeAssuredAmaturityGender22
                    .setHorizontalAlignment(Element.ALIGN_CENTER);


            PdfPCell cell_lifeAssured_Premium_frequeny1 = new PdfPCell(
                    new Paragraph("Premium Payment Frequency", small_normal));
            cell_lifeAssured_Premium_frequeny1.setPadding(5);
            PdfPCell cell_lifeAssured_Premium_frequeny2 = new PdfPCell(
                    new Paragraph(premium_paying_frequency, small_bold));
            cell_lifeAssured_Premium_frequeny2.setPadding(5);
            cell_lifeAssured_Premium_frequeny2
                    .setHorizontalAlignment(Element.ALIGN_CENTER);

            table_lifeAssuredDetails.addCell(cell_lifeAssuredAmaturityGender1);
            table_lifeAssuredDetails.addCell(cell_lifeAssuredAmaturityGender2);
            table_lifeAssuredDetails.addCell(cell_lifeAssuredAmaturityGender11);
            table_lifeAssuredDetails.addCell(cell_lifeAssuredAmaturityGender22);
            //table_lifeAssuredDetails
            //		.addCell(cell_lifeAssured_Premium_frequeny1);
            //table_lifeAssuredDetails
            //	.addCell(cell_lifeAssured_Premium_frequeny2);
            document.add(table_lifeAssuredDetails);

            /*PdfPTable statetable = new PdfPTable(2);
            statetable.setWidthPercentage(100);

            PdfPCell state1 = new PdfPCell(
                    new Paragraph("State", small_normal));
            state1.setPadding(5);
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
            statetable.addCell(state1);
            statetable.addCell(state2);
            document.add(statetable);*/


            String isStaff;
            if (cb_staffdisc.isChecked()) {
                isStaff = "Yes";
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
            } else if (!cb_staffdisc.isChecked()) {
                isStaff = "No";
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

            String isJK;
            if (cb_bi_smart_money_planner_JKResident.isChecked()) {
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
            document.add(para_img_logo_after_space_1);


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

            PdfPCell cell_PlanProposed1 = new PdfPCell(new Paragraph(
                    "Policy Option", small_normal));
            cell_PlanProposed1.setPadding(5);
            PdfPCell cell_tPlanProposed2 = new PdfPCell(new Paragraph(plan,
                    small_bold));
            cell_tPlanProposed2.setPadding(5);
            cell_tPlanProposed2.setHorizontalAlignment(Element.ALIGN_CENTER);

            PdfPCell cell_premium_paying_term1 = new PdfPCell(new Paragraph(
                    " Amount of Installment Premium (Rs.)", small_normal));
            cell_premium_paying_term1.setPadding(5);
            PdfPCell cell_premium_paying_term2 = new PdfPCell(new Paragraph(
                    " "
                            + getformatedThousandString(Integer.parseInt(obj.getRound(obj.getStringWithout_E(Double
                            .valueOf(basicprem.equals("") ? "0"
                                    : basicprem))))), small_bold));

            cell_premium_paying_term2.setPadding(5);
            cell_premium_paying_term2
                    .setHorizontalAlignment(Element.ALIGN_CENTER);

            PdfPCell ppo = new PdfPCell(new Paragraph(
                    "Premium Payment Option", small_normal));
            ppo.setPadding(5);
            PdfPCell ppo2;
            if (spnr_bi_smart_money_planner_premium_frequency.getSelectedItem().toString().equalsIgnoreCase("Single")) {
                ppo2 = new PdfPCell(new Paragraph("Single",
                        small_bold));
                ppo2.setPadding(5);
                ppo2.setHorizontalAlignment(Element.ALIGN_CENTER);
            } else {
                ppo2 = new PdfPCell(new Paragraph("Limited",
                        small_bold));
                ppo2.setPadding(5);
                ppo2.setHorizontalAlignment(Element.ALIGN_CENTER);
            }

            PdfPCell bonus_type = new PdfPCell(new Paragraph(
                    "Bonus Type", small_normal));
            bonus_type.setPadding(5);
            PdfPCell bonus_type2 = new PdfPCell(new Paragraph("Simple Reversionary Bonus",
                    small_bold));
            bonus_type2.setPadding(5);
            bonus_type2.setHorizontalAlignment(Element.ALIGN_CENTER);

            PdfPCell cell_Term1 = new PdfPCell(new Paragraph("Policy Term (Years)",
                    small_normal));
            PdfPCell cell_Term2 = new PdfPCell(new Paragraph(policy_term
                    + "", small_bold));
            cell_Term1.setPadding(5);
            cell_Term2.setPadding(5);
            cell_Term2.setHorizontalAlignment(Element.ALIGN_CENTER);

            PdfPCell cell_plan1 = new PdfPCell(new Paragraph("Sum Assured (Rs.) ",
                    small_normal));
            cell_plan1.setPadding(5);
            PdfPCell cell_plan2 = new PdfPCell(new Paragraph(""
                    + getformatedThousandString(Integer.parseInt(obj.getRound(obj.getStringWithout_E(Double
                    .parseDouble(sum_assured))))), small_bold));
            cell_plan2.setPadding(5);
            cell_plan2.setHorizontalAlignment(Element.ALIGN_CENTER);

            PdfPCell cell_PremiumPayingTerm1 = new PdfPCell(new Paragraph(
                    "Premium Payment Term (Years)", small_normal));
            String premiumpayingterm;
            if (!(premium_paying_frequency.equals("Single"))) {

                premiumpayingterm = premium_paying_term;
            } else {
                premiumpayingterm = "1";
            }

            PdfPCell cell_PremiumPayingTerm2 = new PdfPCell(new Paragraph(
                    premiumpayingterm + " ", small_bold));
            cell_PremiumPayingTerm1.setPadding(5);
            cell_PremiumPayingTerm2.setPadding(5);
            cell_PremiumPayingTerm2
                    .setHorizontalAlignment(Element.ALIGN_CENTER);


            PdfPCell cell_plan11 = new PdfPCell(new Paragraph("Sum Assured on Death (at inception of the policy) (Rs.)",
                    small_normal));
            cell_plan11.setPadding(5);
            PdfPCell cell_plan22 = new PdfPCell(new Paragraph(""
                    + getformatedThousandString(Integer.parseInt(obj.getRound(obj.getStringWithout_E(Double
                    .parseDouble(sum_assured_on_death))))), small_bold));
            cell_plan22.setPadding(5);
            cell_plan22.setHorizontalAlignment(Element.ALIGN_CENTER);

            PdfPCell mode = new PdfPCell(new Paragraph(
                    "Mode / Frequency of Premium Payment", small_normal));
            mode.setPadding(5);
            premPayingMode = prsObj.parseXmlTag(inputVal.toString(), "premFreq");

            PdfPCell mode2 = new PdfPCell(new Paragraph(premPayingMode,
                    small_bold));
            mode2.setPadding(5);
            mode2.setHorizontalAlignment(Element.ALIGN_CENTER);

            PdfPCell rate = new PdfPCell(new Paragraph(
                    "Rate of Applicable Taxes", small_normal));
            rate.setPadding(5);
            PdfPCell rate2;
            if (cb_kerladisc.isChecked()) {
                if (spnr_bi_smart_money_planner_premium_frequency.getSelectedItem().toString().equalsIgnoreCase("Single")) {
                    rate2 = new PdfPCell(new Paragraph("  4.75%   ",
                            small_bold));
                    rate2.setPadding(5);
                    rate2.setHorizontalAlignment(Element.ALIGN_CENTER);
                } else {
                    rate2 = new PdfPCell(new Paragraph("  4.75% in the 1st policy year and 2.375% from 2nd policy year onwards  ",
                            small_bold));
                    rate2.setPadding(5);
                    rate2.setHorizontalAlignment(Element.ALIGN_CENTER);
                }


            } else {
                if (spnr_bi_smart_money_planner_premium_frequency.getSelectedItem().toString().equalsIgnoreCase("Single")) {
                    rate2 = new PdfPCell(new Paragraph(" 4.5%  ",
                            small_bold));
                    rate2.setPadding(5);
                    rate2.setHorizontalAlignment(Element.ALIGN_CENTER);
                } else {
                    rate2 = new PdfPCell(new Paragraph(" 4.5% in the 1st policy year and 2.25% from 2nd policy year onwards ",
                            small_bold));
                    rate2.setPadding(5);
                    rate2.setHorizontalAlignment(Element.ALIGN_CENTER);
                }


            }


            Table_policyTerm_annualisedPremium_sumAssured
                    .addCell(cell_PlanProposed1);
            Table_policyTerm_annualisedPremium_sumAssured
                    .addCell(cell_tPlanProposed2);

            Table_policyTerm_annualisedPremium_sumAssured
                    .addCell(cell_premium_paying_term1);
            Table_policyTerm_annualisedPremium_sumAssured
                    .addCell(cell_premium_paying_term2);

            Table_policyTerm_annualisedPremium_sumAssured
                    .addCell(ppo);
            Table_policyTerm_annualisedPremium_sumAssured
                    .addCell(ppo2);

            Table_policyTerm_annualisedPremium_sumAssured
                    .addCell(bonus_type);
            Table_policyTerm_annualisedPremium_sumAssured
                    .addCell(bonus_type2);

            Table_policyTerm_annualisedPremium_sumAssured.addCell(cell_Term1);
            Table_policyTerm_annualisedPremium_sumAssured.addCell(cell_Term2);


            Table_policyTerm_annualisedPremium_sumAssured
                    .addCell(cell_plan1);
            Table_policyTerm_annualisedPremium_sumAssured
                    .addCell(cell_plan2);


            Table_policyTerm_annualisedPremium_sumAssured
                    .addCell(cell_PremiumPayingTerm1);
            Table_policyTerm_annualisedPremium_sumAssured
                    .addCell(cell_PremiumPayingTerm2);


            Table_policyTerm_annualisedPremium_sumAssured
                    .addCell(cell_plan11);
            Table_policyTerm_annualisedPremium_sumAssured
                    .addCell(cell_plan22);

            Table_policyTerm_annualisedPremium_sumAssured
                    .addCell(mode);
            Table_policyTerm_annualisedPremium_sumAssured
                    .addCell(mode2);

            Table_policyTerm_annualisedPremium_sumAssured
                    .addCell(rate);
            Table_policyTerm_annualisedPremium_sumAssured
                    .addCell(rate2);

            document.add(Table_policyTerm_annualisedPremium_sumAssured);

            PdfPTable table_plan_premium_payingTerm = new PdfPTable(4);
            table_plan_premium_payingTerm.setWidthPercentage(100);


            //table_plan_premium_payingTerm.addCell(cell_plan1);
            //table_plan_premium_payingTerm.addCell(cell_plan2);

            //table_plan_premium_payingTerm.addCell(cell_premium_paying_term1);
            //table_plan_premium_payingTerm.addCell(cell_premium_paying_term2);
            //document.add(table_plan_premium_payingTerm);

            document.add(para_img_logo_after_space_1);

            PdfPTable BI_Pdftable_totalPremiumforBaseProduct = new PdfPTable(1);
            BI_Pdftable_totalPremiumforBaseProduct.setWidthPercentage(100);
            PdfPCell BI_Pdftable_totalPremiumforBaseProductcell = new PdfPCell(
                    new Paragraph(
                            "Total Premium for Base Product (in Rs)",
                            small_bold));

            BI_Pdftable_totalPremiumforBaseProductcell
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_Pdftable_totalPremiumforBaseProductcell.setPadding(5);

            BI_Pdftable_totalPremiumforBaseProduct
                    .addCell(BI_Pdftable_totalPremiumforBaseProductcell);
            //	document.add(BI_Pdftable_totalPremiumforBaseProduct);


            PdfPTable BI_Pdftable_totalPremiumforBaseProduct1 = new PdfPTable(3);
            BI_Pdftable_totalPremiumforBaseProduct1.setWidthPercentage(100);


            PdfPCell BI_Pdftable_totalPremiumforBaseProductcell21 = new PdfPCell(
                    new Paragraph(
                            "",
                            small_bold));

            BI_Pdftable_totalPremiumforBaseProductcell21
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_Pdftable_totalPremiumforBaseProductcell21.setPadding(5);

            PdfPCell BI_Pdftable_totalPremiumforBaseProductcell2 = new PdfPCell(
                    new Paragraph(
                            "",
                            small_bold));

            BI_Pdftable_totalPremiumforBaseProductcell2
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_Pdftable_totalPremiumforBaseProductcell2.setPadding(5);

            PdfPCell BI_Pdftable_totalPremiumforBaseProductcell3 = new PdfPCell(
                    new Paragraph(
                            premium_paying_frequency,
                            small_bold));

            BI_Pdftable_totalPremiumforBaseProductcell3
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_Pdftable_totalPremiumforBaseProductcell3.setPadding(5);

            BI_Pdftable_totalPremiumforBaseProduct1
                    .addCell(BI_Pdftable_totalPremiumforBaseProductcell21);
            BI_Pdftable_totalPremiumforBaseProduct1
                    .addCell(BI_Pdftable_totalPremiumforBaseProductcell2);
            BI_Pdftable_totalPremiumforBaseProduct1
                    .addCell(BI_Pdftable_totalPremiumforBaseProductcell3);
            //document.add(BI_Pdftable_totalPremiumforBaseProduct1);


            PdfPTable Table_AccidetnalAndParmantRider_BasicPremium_servicePremium_yearlyPremium = new PdfPTable(
                    3);
            Table_AccidetnalAndParmantRider_BasicPremium_servicePremium_yearlyPremium
                    .setWidthPercentage(100);

            PdfPCell cell_AccidetnalAndParmantRider_BasicPremium1 = new PdfPCell(
                    new Paragraph(premium_paying_frequency + " Premium",
                            small_normal));
            cell_AccidetnalAndParmantRider_BasicPremium1.setPadding(5);

            PdfPCell cell_AccidetnalAndParmantRider_BasicPremium12 = new PdfPCell(
                    new Paragraph(plan,
                            small_normal));
            cell_AccidetnalAndParmantRider_BasicPremium12.setPadding(5);
            PdfPCell cell_AccidetnalAndParmantRider_BasicPremium2 = new PdfPCell(
                    new Paragraph("Rs "
                            + getformatedThousandString(Integer.parseInt(obj.getRound(obj.getStringWithout_E(Double
                            .valueOf(basicprem.equals("") ? "0"
                                    : basicprem))))), small_bold));
            cell_AccidetnalAndParmantRider_BasicPremium2.setPadding(5);
            cell_AccidetnalAndParmantRider_BasicPremium2
                    .setHorizontalAlignment(Element.ALIGN_CENTER);


            Table_AccidetnalAndParmantRider_BasicPremium_servicePremium_yearlyPremium
                    .addCell(cell_AccidetnalAndParmantRider_BasicPremium1);
            Table_AccidetnalAndParmantRider_BasicPremium_servicePremium_yearlyPremium
                    .addCell(cell_AccidetnalAndParmantRider_BasicPremium12);
            Table_AccidetnalAndParmantRider_BasicPremium_servicePremium_yearlyPremium
                    .addCell(cell_AccidetnalAndParmantRider_BasicPremium2);

            //document.add(Table_AccidetnalAndParmantRider_BasicPremium_servicePremium_yearlyPremium);

            PdfPTable Table_AccidetnalAndParmantRider_BasicPremium_servicePremium_yearlyPremium1 = new PdfPTable(
                    3);
            Table_AccidetnalAndParmantRider_BasicPremium_servicePremium_yearlyPremium1
                    .setWidthPercentage(100);


            PdfPCell cell_AccidetnalAndParmantRider_ServiceTax1 = new PdfPCell(
                    new Paragraph("Applicable Taxes", small_normal));
            PdfPCell cell_AccidetnalAndParmantRider_BasicPremium121 = new PdfPCell(
                    new Paragraph(plan,
                            small_normal));
            cell_AccidetnalAndParmantRider_BasicPremium121.setPadding(5);
            PdfPCell cell_AccidetnalAndParmantRider_ServiceTax2 = new PdfPCell(
                    new Paragraph("Rs  "
                            + getformatedThousandString(Integer.parseInt(obj.getRound(obj.getStringWithout_E(Double
                            .valueOf(servicetax.equals("") ? "0"
                                    : servicetax))))), small_bold));
            cell_AccidetnalAndParmantRider_ServiceTax1.setPadding(5);
            cell_AccidetnalAndParmantRider_ServiceTax2.setPadding(5);
            cell_AccidetnalAndParmantRider_ServiceTax2
                    .setHorizontalAlignment(Element.ALIGN_CENTER);

            Table_AccidetnalAndParmantRider_BasicPremium_servicePremium_yearlyPremium1
                    .addCell(cell_AccidetnalAndParmantRider_ServiceTax1);
            Table_AccidetnalAndParmantRider_BasicPremium_servicePremium_yearlyPremium1
                    .addCell(cell_AccidetnalAndParmantRider_BasicPremium121);
            Table_AccidetnalAndParmantRider_BasicPremium_servicePremium_yearlyPremium1
                    .addCell(cell_AccidetnalAndParmantRider_ServiceTax2);


            //document.add(Table_AccidetnalAndParmantRider_BasicPremium_servicePremium_yearlyPremium1);

            PdfPTable Table_backdating_premium_with_service_tax = new PdfPTable(
                    3);
            Table_backdating_premium_with_service_tax.setWidthPercentage(100);

            PdfPCell cell_Backdate1 = new PdfPCell(new Paragraph(
                    "Backdating Interest", small_normal));
            cell_Backdate1.setPadding(5);
            PdfPCell cell_Backdate12 = new PdfPCell(new Paragraph(
                    plan, small_normal));
            cell_Backdate12.setPadding(5);
            PdfPCell cell_Backdate2 = new PdfPCell(new Paragraph("Rs "
                    + getformatedThousandString(Integer.parseInt(obj.getRound(obj.getStringWithout_E(Double
                    .valueOf(BackdatingInt.equals("") ? "0"
                            : BackdatingInt))))), small_bold));
            cell_Backdate2.setPadding(5);
            cell_Backdate2.setHorizontalAlignment(Element.ALIGN_CENTER);

            PdfPCell cell_AccidetnalAndParmantRider_YearlyPremium1 = new PdfPCell(
                    new Paragraph(premium_paying_frequency
                            + "  Premium with Applicable Taxes", small_normal));
            PdfPCell cell_AccidetnalAndParmantRider_YearlyPremium12 = new PdfPCell(
                    new Paragraph(plan, small_normal));
            PdfPCell cell_AccidetnalAndParmantRider_YearlyPremium2 = new PdfPCell(
                    new Paragraph("Rs  "
                            + getformatedThousandString(Integer.parseInt(obj.getRound(obj.getStringWithout_E(Double
                            .valueOf(basicplustax.equals("") ? "0"
                                    : basicplustax))))), small_bold));
            cell_AccidetnalAndParmantRider_YearlyPremium1.setPadding(5);
            cell_AccidetnalAndParmantRider_YearlyPremium12.setPadding(5);
            cell_AccidetnalAndParmantRider_YearlyPremium2.setPadding(5);
            cell_AccidetnalAndParmantRider_YearlyPremium2
                    .setHorizontalAlignment(Element.ALIGN_CENTER);

            Table_backdating_premium_with_service_tax.addCell(cell_Backdate1);
            Table_backdating_premium_with_service_tax.addCell(cell_Backdate12);
            Table_backdating_premium_with_service_tax.addCell(cell_Backdate2);

            Table_backdating_premium_with_service_tax
                    .addCell(cell_AccidetnalAndParmantRider_YearlyPremium1);
            Table_backdating_premium_with_service_tax
                    .addCell(cell_AccidetnalAndParmantRider_YearlyPremium12);
            Table_backdating_premium_with_service_tax
                    .addCell(cell_AccidetnalAndParmantRider_YearlyPremium2);
            //document.add(Table_backdating_premium_with_service_tax);

            document.add(para_img_logo_after_space_1);

            //document.newPage();

            // ///////////////////

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

            cell = new PdfPCell(new Phrase("Base Plan (Rs.)", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            FY_SY_premDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase(smartMoneyPlannerBean.getPremFreq()
                    + " Premium  (Rs )", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            //FY_SY_premDetail_table.addCell(cell);

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

            cell = new PdfPCell(new Phrase("Riders (Rs.)", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            FY_SY_premDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase("Total Installment Premium (Rs.)", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            FY_SY_premDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase(smartMoneyPlannerBean.getPremFreq()
                    + " Premium with Applicable Taxes (Rs )", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);

            cell = new PdfPCell(new Phrase("Installment Premium without Applicable Taxes (Rs.)", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            FY_SY_premDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                    .parseDouble(prsObj.parseXmlTag(output, "premInst"))),
                    small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            FY_SY_premDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase("NA", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            FY_SY_premDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                    .parseDouble(prsObj.parseXmlTag(output, "premInst"))),
                    small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            FY_SY_premDetail_table.addCell(cell);

            // 4 row

            cell = new PdfPCell(new Phrase("Installment Premium with Applicable Taxes : 1st Year (Rs.)",
                    small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            FY_SY_premDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                    .parseDouble(prsObj.parseXmlTag(output,
                            "premWthST"))), small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            FY_SY_premDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase("NA", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            FY_SY_premDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                    .parseDouble(prsObj.parseXmlTag(output,
                            "premWthST"))), small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            FY_SY_premDetail_table.addCell(cell);

            if (!smartMoneyPlannerBean.getPremFreq().equalsIgnoreCase("Single")) {
                cell = new PdfPCell(new Phrase("Installment Premium with Applicable Taxes : 2nd Year onwards (Rs.)", small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                FY_SY_premDetail_table.addCell(cell);

                String premWthSTSecondYear = prsObj.parseXmlTag(output,
                        "premWthSTSecondYear");
                premWthSTSecondYear = premWthSTSecondYear == null ? "" : premWthSTSecondYear;

                if (!TextUtils.isEmpty(premWthSTSecondYear)) {
                    double premWthSTSecondYearDouble = Double.parseDouble(premWthSTSecondYear);
                    premWthSTSecondYear = currencyFormat.format(premWthSTSecondYearDouble);
                }


                cell = new PdfPCell(new Phrase(premWthSTSecondYear, small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                FY_SY_premDetail_table.addCell(cell);

                cell = new PdfPCell(new Phrase("NA", small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                FY_SY_premDetail_table.addCell(cell);

                cell = new PdfPCell(new Phrase(premWthSTSecondYear, small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                FY_SY_premDetail_table.addCell(cell);
            } else {
                cell = new PdfPCell(new Phrase("Installment Premium with Applicable Taxes : 2nd Year onwards (Rs.)", small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                FY_SY_premDetail_table.addCell(cell);


                cell = new PdfPCell(new Phrase("NA", small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                FY_SY_premDetail_table.addCell(cell);

                cell = new PdfPCell(new Phrase("NA", small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                FY_SY_premDetail_table.addCell(cell);

                cell = new PdfPCell(new Phrase("NA", small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                FY_SY_premDetail_table.addCell(cell);
            }

            document.add(FY_SY_premDetail_table);

            document.add(para_img_logo_after_space_1);

            // //////////////////

            PdfPTable BI_Pdftable_note = new PdfPTable(1);
            BI_Pdftable_note.setWidthPercentage(100);
            PdfPCell BI_Pdftable_note_cell1 = new PdfPCell(new Paragraph(
                    "Please Note:", small_bold));
            BI_Pdftable_note_cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_Pdftable_note_cell1.setPadding(5);

            BI_Pdftable_note.addCell(BI_Pdftable_note_cell1);
            document.add(BI_Pdftable_note);

            PdfPTable BI_Pdftable6 = new PdfPTable(1);
            BI_Pdftable6.setWidthPercentage(100);
            PdfPCell BI_Pdftable6_cell6 = new PdfPCell(new Paragraph(
                    "1. The premiums can also be paid by giving standing instruction to your bank or you can pay through your credit card. "

                    , small_normal));

            BI_Pdftable6_cell6.setPadding(5);

            BI_Pdftable6.addCell(BI_Pdftable6_cell6);
            document.add(BI_Pdftable6);

            PdfPTable BI_Pdftable = new PdfPTable(1);
            BI_Pdftable.setWidthPercentage(100);
            PdfPCell BI_Pdftable6_cell = new PdfPCell(
                    new Paragraph(
                            "2. The premiums can be paid by giving standing instruction to your bank or you can pay through your credit card (Visa and Master Card). "

                            , small_normal));

            BI_Pdftable6_cell.setPadding(5);

            BI_Pdftable.addCell(BI_Pdftable6_cell);
            //document.add(BI_Pdftable);

            PdfPTable BI_Pdftable7 = new PdfPTable(1);
            BI_Pdftable7.setWidthPercentage(100);
            PdfPCell BI_Pdftable7_cell1 = new PdfPCell(
                    new Paragraph(
                            "3. For Monthly Salary Saving Scheme (SSS), 2 month premium to be paid in advance and renewal premium payment is allowed only through Salary Deduction.",
                            small_normal));

            BI_Pdftable7_cell1.setPadding(5);

            BI_Pdftable7.addCell(BI_Pdftable7_cell1);
            //document.add(BI_Pdftable7);

            PdfPTable BI_Pdftable8 = new PdfPTable(1);
            BI_Pdftable8.setWidthPercentage(100);
            PdfPCell BI_Pdftable8_cell1 = new PdfPCell(
                    new Paragraph(
                            "4. Tax deduction under Section 80 C is available. However in case the premium paid during the financial year, exceeds 10% of the sum assured, the benefit will be limited up to 10% of the sum assured. Tax exemption under Section 10(10D) is available at the time of maturity/surrender, subject to the premium not exceeding 10% of the sum assured in any of the years during the term of the policy. However, death proceeds are completely exempt. Tax benefits, are as per the provisions of the Income Tax laws & are subject to change from time to time. Please consult your tax advisor for details.",
                            small_normal));

            BI_Pdftable8_cell1.setPadding(5);

            BI_Pdftable8.addCell(BI_Pdftable8_cell1);
            //document.add(BI_Pdftable8);

            PdfPTable taxes_desc = new PdfPTable(1);
            taxes_desc.setWidthPercentage(100);
            PdfPCell taxes_desc_cell = new PdfPCell(
                    new Paragraph(
                            "2. Applicable Taxes (including surcharge/cess etc), at the rate notified by the Central Government/ State Government / Union Territories of India from time to time and as per the provisions of the prevalent tax laws will be payable on premium/ or any other charges as per the product features. ",

                            small_normal));

            taxes_desc_cell.setPadding(5);

            taxes_desc.addCell(taxes_desc_cell);
            document.add(taxes_desc);


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
                            "2.The Maturity/ Death Benefit amount are derived on the assumption that the policies are in “full force“.",
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

            // PdfPTable BI_PdftableOtherTermCondition5 = new PdfPTable(1);
            // BI_PdftableOtherTermCondition5.setWidthPercentage(100);
            // PdfPCell BI_PdftableOtherTermCondition5_cell1 = new PdfPCell(
            // new Paragraph(
            // "5. I hereby declare that the product features and the benefits have been fully and thoroughly explained to me along with benefit illustrations and I have fully understood the same.",
            // small_normal));
            //
            // BI_PdftableOtherTermCondition5_cell1.setPadding(5);
            //
            // BI_PdftableOtherTermCondition5
            // .addCell(BI_PdftableOtherTermCondition5_cell1);
            // document.add(BI_PdftableOtherTermCondition5);

            document.add(para_img_logo_after_space_1);


            PdfPTable table1_new = new PdfPTable(17);
            table1_new.setWidths(new float[]{2f, 5f, 5f, 5f, 5f, 5f, 5f, 5f, 5f,
                    5f, 5f, 5f, 5f, 5f, 5f, 5f, 5f});
            table1_new.setWidthPercentage(100);

            // 1st row
            cell = new PdfPCell(
                    new Phrase(
                            "Benefit Illustration for SBI Life - Smart Money Planner (Amount in Rs.)",
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
            if (smartMoneyPlannerBean.getPremFreq().equalsIgnoreCase("Single")) {
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

            cell = new PdfPCell(new Phrase("Maturity Benefit", normal_bold));
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


            document.add(para_img_logo_after_space_1);
            PdfPTable BI_Pdftable_totalPremiumforBaseProduct33 = new PdfPTable(1);
            BI_Pdftable_totalPremiumforBaseProduct33.setWidthPercentage(100);
            PdfPCell BI_Pdftable_totalPremiumforBaseProductcel33 = new PdfPCell(
                    new Paragraph(
                            "Benefit payable to the nominee on death",
                            small_bold));

            BI_Pdftable_totalPremiumforBaseProductcel33
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_Pdftable_totalPremiumforBaseProductcel33.setPadding(5);

            BI_Pdftable_totalPremiumforBaseProduct33
                    .addCell(BI_Pdftable_totalPremiumforBaseProductcel33);
            //document.add(BI_Pdftable_totalPremiumforBaseProduct33);

            Chunk normaltext1 = new Chunk("Higher of i) Sum Assured on death ", small_normal);
            Chunk superScript = new Chunk("#", small_normal);
            superScript.setTextRise(2f);
            Chunk moreNormalText = new Chunk(" + Vested Simple Reversionary Bonuses + Terminal bonus, if any. OR ii) 105% of all the basic premiums paid.", small_normal);

            Paragraph para_note = new Paragraph();
            para_note.add(normaltext1);
            para_note.add(superScript);
            para_note.add(moreNormalText);

            //document.add(para_note);

            Chunk superScript1 = new Chunk("#", small_normal);
            superScript1.setTextRise(2f);
            Chunk moreNormalText1 = new Chunk("For details on Sum Assured on death please refer the Sales Brochure", small_normal);

            Paragraph para_note1 = new Paragraph();
            para_note1.add(superScript);
            para_note1.add(moreNormalText1);

            //document.add(para_note1);

            //document.add(para_img_logo_after_space_1);


            if (plan.equals("Endowment Option")) {
                PdfPTable BI_Pdftable_EndowmentOption = new PdfPTable(1);
                BI_Pdftable_EndowmentOption.setWidthPercentage(100);
                PdfPCell BI_Pdftable_EndowmentOption_cell1 = new PdfPCell(
                        new Paragraph("Endowment Option", small_bold));
                BI_Pdftable_EndowmentOption_cell1
                        .setHorizontalAlignment(Element.ALIGN_CENTER);
                BI_Pdftable_EndowmentOption_cell1.setPadding(5);

                BI_Pdftable_EndowmentOption
                        .addCell(BI_Pdftable_EndowmentOption_cell1);
                //document.add(BI_Pdftable_EndowmentOption);

                PdfPTable BI_Pdftable_EndowmentOption1 = new PdfPTable(1);
                BI_Pdftable_EndowmentOption1.setWidthPercentage(100);
                PdfPCell BI_Pdftable_EndowmentOption_cell2 = new PdfPCell(
                        new Paragraph(
                                "1. For Regular Premium: Higher of i) Sum Assured on death# + Vested Simple Reversionary Bonuses + Terminal bonus, if any. OR ii) 105% of all the basic premiums paid."

                                , small_normal));

                BI_Pdftable_EndowmentOption_cell2.setPadding(5);

                BI_Pdftable_EndowmentOption1
                        .addCell(BI_Pdftable_EndowmentOption_cell2);
                //document.add(BI_Pdftable_EndowmentOption1);

                PdfPTable BI_Pdftable_EndowmentOption2 = new PdfPTable(1);
                BI_Pdftable_EndowmentOption2.setWidthPercentage(100);
                PdfPCell BI_Pdftable_EndowmentOption2_cell2 = new PdfPCell(
                        new Paragraph(
                                "2. For Single Premium: Sum Assured on death# + Vested Simple Reversionary Bonuses + Terminal bonus, if any."

                                , small_normal));

                BI_Pdftable_EndowmentOption2_cell2.setPadding(5);

                BI_Pdftable_EndowmentOption2
                        .addCell(BI_Pdftable_EndowmentOption2_cell2);
                //document.add(BI_Pdftable_EndowmentOption2);
            } else if (plan.equals("Endowment with Whole Life Option")) {
                PdfPTable BI_Pdftable_EndowmentOption = new PdfPTable(1);
                BI_Pdftable_EndowmentOption.setWidthPercentage(100);
                PdfPCell BI_Pdftable_EndowmentOption_cell1 = new PdfPCell(
                        new Paragraph("Endowment with Whole Life Option",
                                small_bold));
                BI_Pdftable_EndowmentOption_cell1
                        .setHorizontalAlignment(Element.ALIGN_CENTER);
                BI_Pdftable_EndowmentOption_cell1.setPadding(5);

                BI_Pdftable_EndowmentOption
                        .addCell(BI_Pdftable_EndowmentOption_cell1);
                //document.add(BI_Pdftable_EndowmentOption);

                PdfPTable BI_Pdftable_EndowmentOption1 = new PdfPTable(1);
                BI_Pdftable_EndowmentOption1.setWidthPercentage(100);
                PdfPCell BI_Pdftable_EndowmentOption_cell2 = new PdfPCell(
                        new Paragraph(
                                "1. Death before completion of the endowment term: same as for Endowment Option for Single Premium: Sum Assured on death# + Vested Simple Reversionary Bonuses + Terminal bonus, if any. and for Regular Premium: Higher of i) Sum Assured on death# + Vested Simple Reversionary Bonuses + Terminal bonus, if any. OR ii) 105% of all the basic premiums paid.plus additional point as below",
                                small_normal));

                BI_Pdftable_EndowmentOption_cell2.setPadding(5);

                BI_Pdftable_EndowmentOption1
                        .addCell(BI_Pdftable_EndowmentOption_cell2);
                //document.add(BI_Pdftable_EndowmentOption1);

                PdfPTable BI_Pdftable_EndowmentOption2 = new PdfPTable(1);
                BI_Pdftable_EndowmentOption2.setWidthPercentage(100);
                PdfPCell BI_Pdftable_EndowmentOption2_cell2 = new PdfPCell(
                        new Paragraph(
                                "2. Death after completion of the endowment term and up to 100 years of age: Basic Sum Assured",
                                small_normal));

                BI_Pdftable_EndowmentOption2_cell2.setPadding(5);

                BI_Pdftable_EndowmentOption2
                        .addCell(BI_Pdftable_EndowmentOption2_cell2);
                //	document.add(BI_Pdftable_EndowmentOption2);
            }

            document.add(para_img_logo_after_space_1);

            PdfPTable notestable = new PdfPTable(1);
            notestable.setWidthPercentage(100);
            PdfPCell notes = new PdfPCell(new Paragraph(
                    "Notes:", small_bold));
            notes
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            notes.setPadding(5);

            PdfPCell notes1 = new PdfPCell(new Paragraph(
                    "1. Annualized premium shall be the premium amount payable in a year chosen by the policyholder, excluding the taxes,  underwriting extra premiums and loading for modal premiums, if any / Single premium shall be the premium amount payable in lumpsum at  inception of the policy as chosen by the policyholder, excluding the taxes and underwriting extra premiums, if any. Refer sales literature for explanation of terms used in this illustration.", small_normal));
            notes1
                    .setHorizontalAlignment(Element.ALIGN_LEFT);
            notes1.setPadding(5);

            PdfPCell notes2 = new PdfPCell(new Paragraph(
                    "2. All Benefit amount are derived on the assumption that the policies are ''in-force''", small_normal));
            notes2
                    .setHorizontalAlignment(Element.ALIGN_LEFT);
            notes2.setPadding(5);

            PdfPCell notes3 = new PdfPCell(new Paragraph(
                    "3. The above BI is subject to payment of stipulated premiums on due date.", small_normal));
            notes3
                    .setHorizontalAlignment(Element.ALIGN_LEFT);
            notes3.setPadding(5);

            PdfPCell notes4 = new PdfPCell(new Paragraph(
                    "4. In addition to Guaranteed Surrender Benefits (column 5), Surrender value of the vested bonuses will also be paid. For the purpose of guaranteed surrender value (GSV) in this illustration the surrender value of vested bonuses are not considered at all.", small_normal));
            notes4
                    .setHorizontalAlignment(Element.ALIGN_LEFT);
            notes4.setPadding(5);

            notestable.addCell(notes);
            notestable.addCell(notes1);
            notestable.addCell(notes2);
            notestable.addCell(notes3);
            notestable.addCell(notes4);


            document.add(notestable);

            PdfPTable BI_Pdftable_BonusRates = new PdfPTable(1);
            BI_Pdftable_BonusRates.setWidthPercentage(100);
            PdfPCell BI_Pdftable_BonusRates_cell1 = new PdfPCell(new Paragraph(
                    "Bonus Rates", small_bold));
            BI_Pdftable_BonusRates_cell1
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_Pdftable_BonusRates_cell1.setPadding(5);

            BI_Pdftable_BonusRates.addCell(BI_Pdftable_BonusRates_cell1);
            document.add(BI_Pdftable_BonusRates);

            PdfPTable BI_Pdftable_BonusRates12 = new PdfPTable(1);
            BI_Pdftable_BonusRates12.setWidthPercentage(100);
            PdfPCell BI_Pdftable_BonusRates_cell2 = new PdfPCell(
                    new Paragraph(
                            "This is a with profit plan and participates in the profits of the company’s life insurance business. Simple reversionary bonuses are declared as a percentage rate, which apply to the sum assured of the basic policy." +
                                    "" + "\n " +
                                    "Terminal bonuses, if any, are declared as a percentage rate, which apply to the vested reversionary bonus. The bonus rates in the benefit illustration are constant. However, in practice, future bonuses are not guaranteed and will depend on future profits. Therefore, the bonuses are shown as non-guaranteed benefits and are calculated so that they are consistent with the two projected investment return assumptions of 4% and 8% per annum."

                            , small_normal));

            BI_Pdftable_BonusRates_cell2.setPadding(5);

            BI_Pdftable_BonusRates12.addCell(BI_Pdftable_BonusRates_cell2);
            document.add(BI_Pdftable_BonusRates12);

            PdfPTable BI_Pdftable_BonusRates2 = new PdfPTable(1);
            BI_Pdftable_BonusRates2.setWidthPercentage(100);
            PdfPCell BI_Pdftable_BonusRates2_cell2 = new PdfPCell(
                    new Paragraph(
                            "2). Simple reversionary bonuses are declared as a percentage rate, which apply to the sum assured of the basic policy. Once declared, they form a part of the guaranteed benefits of the plan. Terminal bonuses, if any, are declared as a percentage rate, which apply to the vested bonus."

                            , small_normal));

            BI_Pdftable_BonusRates2_cell2.setPadding(5);

            BI_Pdftable_BonusRates2.addCell(BI_Pdftable_BonusRates2_cell2);
            //document.add(BI_Pdftable_BonusRates2);

            PdfPTable BI_Pdftable_BonusRates3 = new PdfPTable(1);
            BI_Pdftable_BonusRates3.setWidthPercentage(100);
            PdfPCell BI_Pdftable_BonusRates3_cell2 = new PdfPCell(
                    new Paragraph(
                            "3). The bonus rates in the benefit illustration are constant. However, in practice, future bonuses are not guaranteed and will depend on future profits. Therefore, the bonuses are shown as non-guaranteed benefits and are calculated so that they are consistent with the two projected investment return assumptions of 4% and 8% per annum."

                            , small_normal));

            BI_Pdftable_BonusRates3_cell2.setPadding(5);

            BI_Pdftable_BonusRates3.addCell(BI_Pdftable_BonusRates3_cell2);
            //document.add(BI_Pdftable_BonusRates3);

            PdfPTable BI_Pdftable_BonusRates4 = new PdfPTable(1);
            BI_Pdftable_BonusRates4.setWidthPercentage(100);
            PdfPCell BI_Pdftable_BonusRates4_cell2 = new PdfPCell(
                    new Paragraph(
                            "4). Accordingly, for the purpose of guaranteed surrender value (GSV) in this illustration, the cash value of vested bonuses are not considered at all."

                            , small_normal));

            BI_Pdftable_BonusRates4_cell2.setPadding(5);

            BI_Pdftable_BonusRates4.addCell(BI_Pdftable_BonusRates4_cell2);
            //document.add(BI_Pdftable_BonusRates4);

            document.add(para_img_logo_after_space_1);

            PdfPTable BI_Pdftable_SurrenderValue = new PdfPTable(1);
            BI_Pdftable_SurrenderValue.setWidthPercentage(100);
            PdfPCell BI_Pdftable_SurrenderValue_cell2 = new PdfPCell(
                    new Paragraph(
                            "Surrender Value ",
                            small_bold));
            BI_Pdftable_SurrenderValue_cell2.setPadding(5);
            BI_Pdftable_SurrenderValue
                    .addCell(BI_Pdftable_SurrenderValue_cell2);
            PdfPCell BI_Pdftable_SurrenderValue_cell21 = new PdfPCell(
                    new Paragraph(
                            "Surrender value is available on the basic policy benefits and not on the rider benefits.",
                            small_normal));
            BI_Pdftable_SurrenderValue_cell21.setPadding(5);
            BI_Pdftable_SurrenderValue
                    .addCell(BI_Pdftable_SurrenderValue_cell21);
            //document.add(BI_Pdftable_SurrenderValue);

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
                            "1) For LPPT Policies:-  The policy will acquire a surrender value only if premiums have been paid for at least 2 full years for PPT less than 10 years and at least 3 full years for PPT 10 years or more.The Guaranteed Surrender Value (GSV) in case of limited premium payment policies will be equal to GSV factors multiplied by the basic premiums paid. Basic premium is equal to total premium less Applicable Taxes, underwriting extra premiums, extra premium due to modal factors  if any."

                            , small_normal));

            BI_Pdftable_RegularPremiumPolicies_cell.setPadding(5);

            BI_Pdftable_RegularPremiumPolicies
                    .addCell(BI_Pdftable_RegularPremiumPolicies_cell);
            //document.add(BI_Pdftable_RegularPremiumPolicies);

            PdfPTable BI_Pdftable_SinglePremiumPolicies = new PdfPTable(1);
            BI_Pdftable_SinglePremiumPolicies.setWidthPercentage(100);
            PdfPCell BI_Pdftable_SinglePremiumPolicies_cell = new PdfPCell(
                    new Paragraph(
                            "2) For Single Premium Policies:-  The policy acquires surrender value after date of commencement of risk.For first three policy years, the Guaranteed Surrender Value (GSV) will be 70% of Single Premium (exclusive of Applicable Taxes) paid excluding extra premiums (underwriting extra) if any, plus cash value of the allocated bonuses.From fourth policy year onwards, the Guaranteed Surrender Value (GSV) will be 90% of Single Premium (exclusive of Applicable Taxes) paid excluding extra premiums (underwriting extra)  if any, plus cash value of the allocated bonuses."

                            , small_normal));

            BI_Pdftable_SinglePremiumPolicies_cell.setPadding(5);

            BI_Pdftable_SinglePremiumPolicies
                    .addCell(BI_Pdftable_SinglePremiumPolicies_cell);
            //document.add(BI_Pdftable_SinglePremiumPolicies);

            //document.add(para_img_logo_after_space_1);

            PdfPTable BI_Pdftable_CompanysPolicySurrender = new PdfPTable(1);
            BI_Pdftable_CompanysPolicySurrender.setWidthPercentage(100);
            PdfPCell BI_Pdftable_CompanysPolicySurrender_cell1 = new PdfPCell(
                    new Paragraph("Important:", small_bold));
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

            //BI_Pdftable_CompanysPolicySurrender5
            //		.addCell(BI_Pdftable_CompanysPolicySurrender5_cell);
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
                                    + ", having received the information with respect to the above, have understood the above statement before entering into the contract.",
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

                PdfPCell BI_Pdftable26_cell11 = new PdfPCell(
                        new Paragraph(
                                "I, "
                                        + commonMethods.getUserName(context)
                                        + "  , have explained the premiums and benefits under the product fully to the prospect/policyholder.",
                                small_bold));

                BI_Pdftable26_cell11.setPadding(5);

                BI_PdftableMarketing.addCell(BI_PdftableMarketing_signature_cell);
                BI_PdftableMarketing.addCell(BI_Pdftable26_cell11);

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

    private void windowmessagesgin() {

        d = new Dialog(BI_SmartMoneyPlannerActivity.this);
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
                Intent intent = new Intent(BI_SmartMoneyPlannerActivity.this,
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


    private void mobile_validation(String number) {
        boolean validationFlag3 = false;
        if ((number.length() != 10)) {
            edt_smart_money_planner_contact_no
                    .setError("Please provide correct 10-digit mobile number");
            validationFlag3 = false;
        } else if ((number.length() == 10)) {
        }
    }

    private void email_id_validation(String email_id) {

        String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email_id);
        if (!(matcher.matches())) {
            edt_smart_money_planner_Email_id
                    .setError("Please provide the correct email address");
            validationFla1 = false;
        } else if ((matcher.matches())) {
            validationFla1 = true;
        }
    }

    // validations

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
                                    setFocusable(spnr_bi_smart_money_planner_life_assured_title);
                                    spnr_bi_smart_money_planner_life_assured_title
                                            .requestFocus();
                                } else if (lifeAssured_First_Name.equals("")) {
                                    setFocusable(edt_bi_smart_money_planner_life_assured_first_name);
                                    edt_bi_smart_money_planner_life_assured_first_name
                                            .requestFocus();
                                } else {
                                    setFocusable(edt_bi_smart_money_planner_life_assured_last_name);
                                    edt_bi_smart_money_planner_life_assured_last_name
                                            .requestFocus();
                                }
                            }
                        });
                showAlert.show();

                return false;
            } else if (gender.equalsIgnoreCase("")) {

                showAlert
                        .setMessage("Please Fill Gender Details");
                showAlert.setNeutralButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // apply focusable method
                                setFocusable(spnr_bi_smart_money_planner_life_assured_title);
                                spnr_bi_smart_money_planner_life_assured_title
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
                                setFocusable(spnr_bi_smart_money_planner_life_assured_title);
                                spnr_bi_smart_money_planner_life_assured_title
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
                                setFocusable(spnr_bi_smart_money_planner_life_assured_title);
                                spnr_bi_smart_money_planner_life_assured_title
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
                                setFocusable(spnr_bi_smart_money_planner_life_assured_title);
                                spnr_bi_smart_money_planner_life_assured_title
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

    private boolean valDob() {

        // if (proposer_Is_Same_As_Life_Assured.equalsIgnoreCase("y")) {

        if (lifeAssured_date_of_birth.equals("")
                || lifeAssured_date_of_birth.equalsIgnoreCase("select Date")) {
            showAlert
                    .setMessage("Please Select Valid Date Of Birth For LifeAssured");
            showAlert.setNeutralButton("OK",
                    new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            // apply focusable method
                            setFocusable(btn_bi_smart_money_planner_life_assured_date);
                            btn_bi_smart_money_planner_life_assured_date
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
                                setFocusable(btn_smart_money_planner_backdatingdate);
                                btn_smart_money_planner_backdatingdate
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

    private boolean valAge() {

        if (18 <= Integer.parseInt(lifeAssuredAge)
                && Integer.parseInt(lifeAssuredAge) <= maxAge) {
            return true;
        } else {
            // if (Integer.parseInt(str_age) <= maxAge) {
            showAlert
                    .setMessage("Minimum Age should be 18 yrs and Maximum Age should be "
                            + maxAge + " yrs For LifeAssured");
            showAlert.setNeutralButton("OK",
                    new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            // apply focusable method
                            setFocusable(btn_bi_smart_money_planner_life_assured_date);
                            btn_bi_smart_money_planner_life_assured_date
                                    .requestFocus();
                        }
                    });
            showAlert.show();

            return false;
        }

    }

    private boolean valBasicDetail() {

        if (edt_smart_money_planner_contact_no.getText().toString().equals("")) {
            commonMethods.dialogWarning(context, "Please Fill  Mobile No", true);
            edt_smart_money_planner_contact_no.requestFocus();
            return false;
        } else if (edt_smart_money_planner_contact_no.getText().toString()
                .length() != 10) {
            commonMethods.dialogWarning(context, "Please Fill 10 Digit Mobile No", true);
            edt_smart_money_planner_contact_no.requestFocus();
            return false;
        }

		/*else if (emailId.equals("")) {
			commonMethods.dialogWarning(context,"Please Fill Email Id", true);
			edt_smart_money_planner_Email_id.requestFocus();
			return false;

		}

		else if (ConfirmEmailId.equals("")) {

			commonMethods.dialogWarning(context, "Please Fill Confirm Email Id", true);
			edt_smart_money_planner_ConfirmEmail_id.requestFocus();
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
                    edt_smart_money_planner_ConfirmEmail_id.requestFocus();
                    return false;
                } else if (!ConfirmEmailId.equals(emailId)) {
                    commonMethods.dialogWarning(context, "Email Id Does Not Match", true);
                    return false;
                }

                return true;
            } else {
                commonMethods.dialogWarning(context, "Please Fill Valid Email Id", true);
                edt_smart_money_planner_Email_id.requestFocus();
                return false;
            }
        } else if (!ConfirmEmailId.equals("")) {

            email_id_validation(ConfirmEmailId);
            if (validationFla1) {

                if (emailId.equals("")) {
                    commonMethods.dialogWarning(context, "Please Fill Email Id", true);
                    edt_smart_money_planner_Email_id.requestFocus();
                    return false;

                }

                return true;
            } else {
                commonMethods.dialogWarning(context, "Please Fill Valid Confirm Email Id", true);
                edt_smart_money_planner_ConfirmEmail_id.requestFocus();
                return false;
            }
        } else {
            return true;
        }
    }

    // Validate Sum Assured
    private boolean valSA() {
        StringBuilder error = new StringBuilder();
        if (edt_bi_smart_money_planner_sum_assured_amount.getText().toString()
                .equals("")) {
            error.append("Please enter Sum Assured in Rs ");

        } else if (Double
                .parseDouble(edt_bi_smart_money_planner_sum_assured_amount
                        .getText().toString()) % 1000 != 0) {
            error.append("Sum assured should be multiple of 1,000");
        } else if (Integer
                .parseInt(edt_bi_smart_money_planner_sum_assured_amount
                        .getText().toString()) > prop.maxSumAssured) {
            error.append("Sum Assured should not be greater than Rs ").append(currencyFormat.format(prop.maxSumAssured));

        } else if (Integer
                .parseInt(edt_bi_smart_money_planner_sum_assured_amount
                        .getText().toString()) < prop.minSumAssured) {
            error.append("Sum Assured should not be less than Rs ").append(currencyFormat.format(prop.minSumAssured));

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

    /********************************** Updation of policy term n premium paying term starts here **********************************************************/

    // Policy Term Help
    private void updatePolicyTermLabel() {
        try {
            int PolicyTerm;
            if (spnr_bi_smart_money_planner_plan.getSelectedItem().toString()
                    .equals("Plan 1"))
                PolicyTerm = 15;
            else if (spnr_bi_smart_money_planner_plan.getSelectedItem()
                    .toString().equals("Plan 2"))
                PolicyTerm = 20;
            else if (spnr_bi_smart_money_planner_plan.getSelectedItem()
                    .toString().equals("Plan 3"))
                PolicyTerm = 20;
            else
                PolicyTerm = 25;

            spnr_bi_smart_money_planner_policyterm.setSelection(
                    getIndex(spnr_bi_smart_money_planner_policyterm,
                            String.valueOf(PolicyTerm)), false);
        } catch (Exception ignored) {
        }
    }

    // premium paying term help
    private void updatePremPayingTermLabel() {
        try {
            int PremiumPayingTerm;
            if (!spnr_bi_smart_money_planner_premium_frequency
                    .getSelectedItem().toString().equals("Single")) {

                if (spnr_bi_smart_money_planner_plan.getSelectedItem()
                        .toString().equals("Plan 1"))
                    PremiumPayingTerm = 6;
                else if (spnr_bi_smart_money_planner_plan.getSelectedItem()
                        .toString().equals("Plan 2"))
                    PremiumPayingTerm = 6;
                else if (spnr_bi_smart_money_planner_plan.getSelectedItem()
                        .toString().equals("Plan 3"))
                    PremiumPayingTerm = 10;
                else
                    PremiumPayingTerm = 10;

                spnr_bi_smart_money_planner_permium_payingterm.setSelection(
                        getIndex(
                                spnr_bi_smart_money_planner_permium_payingterm,
                                String.valueOf(PremiumPayingTerm)), false);

            }
            /* added by Akshaya on 04-AUG-15 start **/
            else // If Single
            {
                PremiumPayingTerm = 0;
                spnr_bi_smart_money_planner_permium_payingterm.setSelection(
                        getIndex(
                                spnr_bi_smart_money_planner_permium_payingterm,
                                String.valueOf(PremiumPayingTerm)), false);
            }
            /* added by Akshaya on 04-AUG-15 end **/

        } catch (Exception ignored) {
        }
    }

    // Policy Term Help
    private void updateMaximumAge() {

        try {
            if (spnr_bi_smart_money_planner_plan.getSelectedItem().toString()
                    .equals("Plan 1"))

                maxAge = 60;

            else if (spnr_bi_smart_money_planner_plan.getSelectedItem()
                    .toString().equals("Plan 2"))
                maxAge = 55;
            else if (spnr_bi_smart_money_planner_plan.getSelectedItem()
                    .toString().equals("Plan 3"))
                maxAge = 55;
            else
                maxAge = 50;

            prop.maxAge = maxAge;
        } catch (Exception ignored) {
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
                            setFocusable(rb_smart_money_planner_backdating_yes);
                            rb_smart_money_planner_backdating_yes
                                    .requestFocus();
                        }
                    });
            showAlert.show();

            return false;

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

    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (v.getId() == edt_bi_smart_money_planner_life_assured_first_name
                .getId()) {
            setFocusable(edt_bi_smart_money_planner_life_assured_middle_name);
            edt_bi_smart_money_planner_life_assured_middle_name.requestFocus();
        } else if (v.getId() == edt_bi_smart_money_planner_life_assured_middle_name
                .getId()) {
            setFocusable(edt_bi_smart_money_planner_life_assured_last_name);
            edt_bi_smart_money_planner_life_assured_last_name.requestFocus();
        } else if (v.getId() == edt_bi_smart_money_planner_life_assured_last_name
                .getId()) {

            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(
                    edt_bi_smart_money_planner_life_assured_last_name
                            .getWindowToken(), 0);
            // clearFocusable(edt_bi_smart_money_planner_life_assured_last_name);
            setFocusable(btn_bi_smart_money_planner_life_assured_date);
            btn_bi_smart_money_planner_life_assured_date.requestFocus();
        } else if (v.getId() == edt_smart_money_planner_contact_no.getId()) {
            // clearFocusable(premiumAmt);
            setFocusable(edt_smart_money_planner_Email_id);
            edt_smart_money_planner_Email_id.requestFocus();
        } else if (v.getId() == edt_smart_money_planner_Email_id.getId()) {
            // clearFocusable(premiumAmt);
            setFocusable(edt_smart_money_planner_ConfirmEmail_id);
            edt_smart_money_planner_ConfirmEmail_id.requestFocus();
        } else if (v.getId() == edt_smart_money_planner_ConfirmEmail_id.getId()) {
            // clearFocusable(premiumAmt);
            clearFocusable(spnr_bi_smart_money_planner_plan);
            setFocusable(spnr_bi_smart_money_planner_plan);
            spnr_bi_smart_money_planner_plan.requestFocus();
        } else if (v.getId() == edt_bi_smart_money_planner_sum_assured_amount
                .getId()) {

            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(
                    edt_bi_smart_money_planner_sum_assured_amount
                            .getWindowToken(), 0);
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();

        spnr_bi_smart_money_planner_life_assured_title.requestFocus();

    }
}
