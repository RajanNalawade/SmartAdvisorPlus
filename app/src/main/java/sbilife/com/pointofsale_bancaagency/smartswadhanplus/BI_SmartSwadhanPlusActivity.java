package sbilife.com.pointofsale_bancaagency.smartswadhanplus;

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

public class BI_SmartSwadhanPlusActivity extends AppCompatActivity implements
        OnEditorActionListener {

    private final String proposer_Is_Same_As_Life_Assured = "Y";
    private final int DATE_DIALOG_ID = 1;
    private final int SIGNATURE_ACTIVITY = 1;
    private NeedAnalysisBIService NABIObj;
    private NA_CBI_bean na_cbi_bean;
    private File needAnalysispath, newFile;
    private String agentcode, agentMobile, agentEmail, userType;
    private int needAnalysis_flag = 0;
    private String na_input = null;
    private String na_output = null;
    private DatabaseHelper dbHelper;
    private String na_dob = "";
    private ArrayAdapter<String> genderAdapter;
    private int flag = 0;
    private CommonForAllProd obj;
    private CheckBox cb_staffdisc, cb_bi_smart_swadhan_plus_JKResident;
    private EditText edt_bi_smart_swadhan_plus_life_assured_first_name,
            edt_bi_smart_swadhan_plus_life_assured_middle_name,
            edt_bi_smart_swadhan_plus_life_assured_last_name,
            edt_bi_smart_swadhan_plus_life_assured_age,
            edt_smart_swadhan_plus_contact_no, edt_smart_swadhan_plus_Email_id,
            edt_smart_swadhan_plus_ConfirmEmail_id,
            edt_bi_smart_swadhan_plus_sum_assured_amount;
    private Spinner spnr_bi_smart_swadhan_plus_life_assured_title,
            spnr_bi_smart_swadhan_plus_selGender,
            spnr_bi_smart_swadhan_plus_plan,
            spnr_bi_smart_swadhan_plus_policyterm,
            spnr_bi_smart_swadhan_plus_permium_payingterm,
            spnr_bi_smart_swadhan_plus_premium_frequency;
    private TableRow tr_premium_paying_term, tr_premium_frequency_mode;
    private Button btn_bi_smart_swadhan_plus_life_assured_date,
            btnBack, btnSubmit, btn_MarketingOfficalDate,
            btn_PolicyholderDate;
    private ImageButton Ibtn_signatureofMarketing;
    private ImageButton Ibtn_signatureofPolicyHolders;
    private String QuatationNumber = "";
    private String planName = "";
    private AlertDialog.Builder showAlert;
    /**
     * End
     */
    // newDBHelper db;
    private ParseXML prsObj;
    private DecimalFormat currencyFormat;
    private String emailId = "", mobileNo = "", ConfirmEmailId = "",
            ProposerEmailId = "";
    private boolean validationFla1 = false;
    private LinearLayout ll_bi_smart_swadhan_plus_main;
    private Bitmap photoBitmap;
    private SmartSwadhanPlusBean smartSwadhanPlusBean;
    private sbilife.com.pointofsale_bancaagency.common.CommonForAllProd CommonForAllProd;
    private StringBuilder retVal;
    private StringBuilder inputVal;
    private String lifeAssured_Title = "";
    private String lifeAssured_First_Name = "";
    private String lifeAssured_Middle_Name = "";
    private String lifeAssured_Last_Name = "";
    private String name_of_life_assured = "";
    private String lifeAssured_date_of_birth = "";
    private String lifeAssuredAge = "";
    private String name_of_proposer = "";
    private StringBuilder bussIll = null;
    // Variable declaration
    private String totInstPrem_exclST = "", SYServiceTax = "",
            FYServiceTax = "";
    private String SYtotInstPrem_inclST = "";
    private boolean valPremiumError = false;
    private String name_of_person = "", place2 = "",
            date1 = "", date2 = "";
    private String agent_sign = "", proposer_sign = "";
    private String proposer_Backdating_WishToBackDate_Policy = "";
    private String proposer_Backdating_BackDate = "";
    private String latestImage = "";
    private List<M_BI_Smart_Swadhan_Plus_Adapter> list_data;
    private String output = "";
    private int DIALOG_ID;
    private int mYear;
    private int mMonth;
    private int mDay;
    private Dialog d;
    private String staffdiscount = "";
    private String gender = "";
    private String premium_paying_frequency = "";
    private String policy_term = "";
    private String premium_paying_term = "0";
    private String sum_assured = "";
    private String basicprem = "";
    private String premWthST = "";
    private String input = "";

    private String premWthSTSecondYear = "";
    private File mypath;

    private RadioButton rb_smart_swadhan_plus_backdating_yes,
            rb_smart_swadhan_plus_backdating_no;
    private LinearLayout ll_smart_swadhan_plus_backdating;
    private Button btn_smart_swadhan_plus_backdatingdate;
    private String BackdatingInt;
    private String product_Code, product_UIN, product_cateogory, product_type;

    private String bankUserType = "", mode = "";

    private String Check = "";
    private CommonMethods commonMethods;
    private StorageUtils mStorageUtils;
    private Context context;
    private final DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;
            updateDisplay(DIALOG_ID);
        }
    };
    private ImageButton imageButtonSmartSwadhanPlusProposerPhotograph;
    private CheckBox cb_kerladisc;
    private Spinner spnr_bi_smart_swadhan_plus_distribution_channel;
    private String distribution_channel = "";
    private String str_kerla_discount = "No";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.bi_smartswadhanplusmain);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);

        obj = new CommonForAllProd();

        dbHelper = new DatabaseHelper(getApplicationContext());

        context = this;
        commonMethods = new CommonMethods();
        mStorageUtils = new StorageUtils();
        commonMethods.setActionbarLayout(this);

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

                    ProductInfo prodInfoObj = new ProductInfo();
                    planName = "Smart Swadhan Plus";
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
                QuatationNumber = sbilife.com.pointofsale_bancaagency.common.CommonForAllProd
                        .getquotationNumber30("1Z", agentcode, zero + "");

            }
        } else
            needAnalysis_flag = 0;
        // ProductHomePageActivity.path.setText("Benefit Illustrator");

        UI_Declaration();


        prsObj = new ParseXML();
        setSpinner_Value();
        // setBIInputGui();

        edt_bi_smart_swadhan_plus_life_assured_first_name
                .setOnEditorActionListener(this);
        edt_bi_smart_swadhan_plus_life_assured_middle_name
                .setOnEditorActionListener(this);
        edt_bi_smart_swadhan_plus_life_assured_last_name
                .setOnEditorActionListener(this);
        edt_bi_smart_swadhan_plus_sum_assured_amount
                .setOnEditorActionListener(this);
        edt_smart_swadhan_plus_contact_no.setOnEditorActionListener(this);
        edt_smart_swadhan_plus_Email_id.setOnEditorActionListener(this);
        edt_smart_swadhan_plus_ConfirmEmail_id.setOnEditorActionListener(this);

        setFocusable(spnr_bi_smart_swadhan_plus_life_assured_title);
        spnr_bi_smart_swadhan_plus_life_assured_title.requestFocus();

        validationOfMoile_EmailId();
        setSpinnerAndOtherListner();
        showAlert = new AlertDialog.Builder(this);
        CommonForAllProd = new CommonForAllProd();

        smartSwadhanPlusBean = new SmartSwadhanPlusBean();
        list_data = new ArrayList<>();
        currencyFormat = new DecimalFormat("##,##,##,###");

        if (needAnalysis_flag == 1 && !TextUtils.isEmpty(gender)) {
            spnr_bi_smart_swadhan_plus_selGender.setSelection(genderAdapter
                    .getPosition(gender));
            onClickLADob(btn_bi_smart_swadhan_plus_life_assured_date);
        }

        rb_smart_swadhan_plus_backdating_yes
                .setOnCheckedChangeListener(new OnCheckedChangeListener() {

                    public void onCheckedChanged(CompoundButton buttonView,
                                                 boolean isChecked) {
                        // TODO Auto-generated method stub
                        if (isChecked) {

                            if (!(lifeAssured_date_of_birth.equals(""))) {
                                proposer_Backdating_WishToBackDate_Policy = "y";
                                rb_smart_swadhan_plus_backdating_yes
                                        .setChecked(true);
                                rb_smart_swadhan_plus_backdating_no
                                        .setChecked(false);
                                ll_smart_swadhan_plus_backdating
                                        .setVisibility(View.VISIBLE);
                                btn_smart_swadhan_plus_backdatingdate
                                        .setText("Select Date");
                                proposer_Backdating_BackDate = "";
                            } else {

                                showAlert
                                        .setMessage("Please Select Life Assure Dob First");
                                showAlert.setNeutralButton("OK",
                                        new DialogInterface.OnClickListener() {

                                            public void onClick(
                                                    DialogInterface dialog,
                                                    int which) {
                                                rb_smart_swadhan_plus_backdating_yes
                                                        .setChecked(false);
                                                rb_smart_swadhan_plus_backdating_no
                                                        .setChecked(true);
                                            }
                                        });
                                showAlert.show();
                            }

                        }
                    }
                });

        rb_smart_swadhan_plus_backdating_no
                .setOnCheckedChangeListener(new OnCheckedChangeListener() {

                    public void onCheckedChanged(CompoundButton buttonView,
                                                 boolean isChecked) {
                        // TODO Auto-generated method stub
                        if (isChecked) {

                            proposer_Backdating_WishToBackDate_Policy = "n";
                            proposer_Backdating_BackDate = "";
                            // setDefaultDate();
                            ll_smart_swadhan_plus_backdating
                                    .setVisibility(View.GONE);

                            edt_bi_smart_swadhan_plus_life_assured_age
                                    .setText(lifeAssuredAge);
                            rb_smart_swadhan_plus_backdating_no
                                    .setChecked(true);
                            rb_smart_swadhan_plus_backdating_yes
                                    .setFocusable(false);

                            clearFocusable(rb_smart_swadhan_plus_backdating_no);
                            clearFocusable(rb_smart_swadhan_plus_backdating_yes);
                            // }
                        }
                    }
                });
    }

    private void UI_Declaration() {
        cb_staffdisc = findViewById(R.id.cb_staffdisc);
        cb_bi_smart_swadhan_plus_JKResident = findViewById(R.id.cb_bi_smart_swadhan_plus_JKResident);

        edt_bi_smart_swadhan_plus_life_assured_first_name = findViewById(R.id.edt_bi_smart_swadhan_plus_life_assured_first_name);
        edt_bi_smart_swadhan_plus_life_assured_middle_name = findViewById(R.id.edt_bi_smart_swadhan_plus_life_assured_middle_name);
        edt_bi_smart_swadhan_plus_life_assured_last_name = findViewById(R.id.edt_bi_smart_swadhan_plus_life_assured_last_name);
        edt_bi_smart_swadhan_plus_life_assured_age = findViewById(R.id.edt_bi_smart_swadhan_plus_life_assured_age);
        edt_smart_swadhan_plus_contact_no = findViewById(R.id.edt_smart_swadhan_plus_contact_no);
        edt_smart_swadhan_plus_Email_id = findViewById(R.id.edt_smart_swadhan_plus_Email_id);
        edt_smart_swadhan_plus_ConfirmEmail_id = findViewById(R.id.edt_smart_swadhan_plus_ConfirmEmail_id);
        edt_bi_smart_swadhan_plus_sum_assured_amount = findViewById(R.id.edt_bi_smart_swadhan_plus_sum_assured_amount);

        spnr_bi_smart_swadhan_plus_life_assured_title = findViewById(R.id.spnr_bi_smart_swadhan_plus_life_assured_title);
        spnr_bi_smart_swadhan_plus_selGender = findViewById(R.id.spnr_bi_smart_swadhan_plus_selGender);
        spnr_bi_smart_swadhan_plus_selGender.setClickable(true);
        spnr_bi_smart_swadhan_plus_selGender.setEnabled(true);

        spnr_bi_smart_swadhan_plus_plan = findViewById(R.id.spnr_bi_smart_swadhan_plus_plan);
        spnr_bi_smart_swadhan_plus_policyterm = findViewById(R.id.spnr_bi_smart_swadhan_plus_policyterm);
        spnr_bi_smart_swadhan_plus_permium_payingterm = findViewById(R.id.spnr_bi_smart_swadhan_plus_permium_payingterm);
        spnr_bi_smart_swadhan_plus_premium_frequency = findViewById(R.id.spnr_bi_smart_swadhan_plus_premium_frequency);

        btn_bi_smart_swadhan_plus_life_assured_date = findViewById(R.id.btn_bi_smart_swadhan_plus_life_assured_date);

        btnBack = findViewById(R.id.btn_bi_smart_swadhan_plus_btnback);
        btnSubmit = findViewById(R.id.btn_bi_smart_swadhan_plus_btnSubmit);

        tr_premium_frequency_mode = findViewById(R.id.tr_premium_frequency_mode);
        tr_premium_paying_term = findViewById(R.id.tr_premium_paying_term);
        ll_bi_smart_swadhan_plus_main = findViewById(R.id.ll_bi_smart_swadhan_plus_main);

        btn_smart_swadhan_plus_backdatingdate = findViewById(R.id.btn_smart_swadhan_plus_backdatingdate);
        rb_smart_swadhan_plus_backdating_yes = findViewById(R.id.rb_smart_swadhan_plus_backdating_yes);
        rb_smart_swadhan_plus_backdating_no = findViewById(R.id.rb_smart_swadhan_plus_backdating_no);
        ll_smart_swadhan_plus_backdating = findViewById(R.id.ll_smart_swadhan_plus_backdating);

        spnr_bi_smart_swadhan_plus_distribution_channel = findViewById(R.id.spnr_bi_smart_swadhan_plus_distribution_channel);

    }

    private void validationOfMoile_EmailId() {

        edt_smart_swadhan_plus_contact_no
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
                        String abc = edt_smart_swadhan_plus_contact_no
                                .getText().toString();
                        mobile_validation(abc);

                    }
                });

        edt_smart_swadhan_plus_Email_id
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
                        ProposerEmailId = edt_smart_swadhan_plus_Email_id
                                .getText().toString();
                        //email_id_validation(ProposerEmailId);

                    }
                });

        edt_smart_swadhan_plus_ConfirmEmail_id
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
                        String proposer_confirm_emailId = edt_smart_swadhan_plus_ConfirmEmail_id
                                .getText().toString();
                        //confirming_email_id(proposer_confirm_emailId);

                    }
                });

    }

    private void setSpinnerAndOtherListner() {

        cb_kerladisc.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cb_kerladisc.isChecked()) {
                    str_kerla_discount = "Yes";
                    clearFocusable(cb_kerladisc);
                    setFocusable(spnr_bi_smart_swadhan_plus_life_assured_title);
                    spnr_bi_smart_swadhan_plus_life_assured_title.requestFocus();
                } else {
                    str_kerla_discount = "No";
                    clearFocusable(cb_kerladisc);
                    setFocusable(spnr_bi_smart_swadhan_plus_life_assured_title);
                    spnr_bi_smart_swadhan_plus_life_assured_title.requestFocus();
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
                clearFocusable(spnr_bi_smart_swadhan_plus_life_assured_title);
                setFocusable(spnr_bi_smart_swadhan_plus_life_assured_title);
                spnr_bi_smart_swadhan_plus_life_assured_title.requestFocus();
            }
        });

        cb_bi_smart_swadhan_plus_JKResident
                .setOnClickListener(new OnClickListener() {

                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        if (cb_bi_smart_swadhan_plus_JKResident.isChecked()) {
                            cb_bi_smart_swadhan_plus_JKResident
                                    .setChecked(true);
                        } else {
                            cb_bi_smart_swadhan_plus_JKResident
                                    .setChecked(false);
                        }
                    }
                });

        // Spinner
        spnr_bi_smart_swadhan_plus_life_assured_title
                .setOnItemSelectedListener(new OnItemSelectedListener() {

                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int position, long id) {
                        // TODO Auto-generated method stub
                        if (position == 0) {
                            lifeAssured_Title = "";
                        } else {
                            lifeAssured_Title = spnr_bi_smart_swadhan_plus_life_assured_title
                                    .getSelectedItem().toString();
							/*if (lifeAssured_Title.equalsIgnoreCase("Mr.")) {
								spnr_bi_smart_swadhan_plus_selGender
										.setSelection(
												getIndex(
														spnr_bi_smart_swadhan_plus_selGender,
														"Male"), false);
							} else if (lifeAssured_Title
									.equalsIgnoreCase("Ms.")) {
								spnr_bi_smart_swadhan_plus_selGender
										.setSelection(
												getIndex(
														spnr_bi_smart_swadhan_plus_selGender,
														"Female"), false);
							} else if (lifeAssured_Title
									.equalsIgnoreCase("Mrs.")) {
								spnr_bi_smart_swadhan_plus_selGender
										.setSelection(
												getIndex(
														spnr_bi_smart_swadhan_plus_selGender,
														"Female"), false);
							}
							clearFocusable(spnr_bi_smart_swadhan_plus_life_assured_title);
							setFocusable(edt_bi_smart_swadhan_plus_life_assured_first_name);
							edt_bi_smart_swadhan_plus_life_assured_first_name
									.requestFocus();*/
                        }
                    }

                    public void onNothingSelected(AdapterView<?> arg0) {
                        // TODO Auto-generated method stub

                    }
                });

        spnr_bi_smart_swadhan_plus_plan
                .setOnItemSelectedListener(new OnItemSelectedListener() {

                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int position, long id) {
                        // TODO Auto-generated method stub
                        if (position == 0) {

                            tr_premium_frequency_mode.setVisibility(View.GONE);
                            tr_premium_paying_term.setVisibility(View.GONE);

                            clearFocusable(spnr_bi_smart_swadhan_plus_plan);
                            setFocusable(spnr_bi_smart_swadhan_plus_policyterm);
                            spnr_bi_smart_swadhan_plus_policyterm
                                    .requestFocus();

                        } else if (position == 1) {

                            tr_premium_frequency_mode
                                    .setVisibility(View.VISIBLE);
                            tr_premium_paying_term.setVisibility(View.GONE);
                            clearFocusable(spnr_bi_smart_swadhan_plus_plan);
                            setFocusable(spnr_bi_smart_swadhan_plus_premium_frequency);
                            spnr_bi_smart_swadhan_plus_premium_frequency
                                    .requestFocus();

                        } else if (position == 2) {

                            tr_premium_frequency_mode
                                    .setVisibility(View.VISIBLE);
                            tr_premium_paying_term.setVisibility(View.VISIBLE);
                            clearFocusable(spnr_bi_smart_swadhan_plus_plan);
                            setFocusable(spnr_bi_smart_swadhan_plus_premium_frequency);
                            spnr_bi_smart_swadhan_plus_premium_frequency
                                    .requestFocus();
                        }

                    }

                    public void onNothingSelected(AdapterView<?> arg0) {
                        // TODO Auto-generated method stub

                    }
                });

        spnr_bi_smart_swadhan_plus_policyterm
                .setOnItemSelectedListener(new OnItemSelectedListener() {

                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int pos, long id) {

                        if (edt_bi_smart_swadhan_plus_life_assured_first_name
                                .getText().toString().equals("")) {
                            clearFocusable(spnr_bi_smart_swadhan_plus_premium_frequency);
                            setFocusable(spnr_bi_smart_swadhan_plus_life_assured_title);
                            spnr_bi_smart_swadhan_plus_life_assured_title
                                    .requestFocus();
                        } else {
                            clearFocusable(spnr_bi_smart_swadhan_plus_policyterm);
                            setFocusable(edt_bi_smart_swadhan_plus_sum_assured_amount);
                            edt_bi_smart_swadhan_plus_sum_assured_amount
                                    .requestFocus();
                        }

                    }

                    public void onNothingSelected(AdapterView<?> arg0) {
                        // TODO Auto-generated method stub

                    }
                });

        spnr_bi_smart_swadhan_plus_permium_payingterm
                .setOnItemSelectedListener(new OnItemSelectedListener() {

                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int pos, long id) {

                        valPPT();
                        clearFocusable(spnr_bi_smart_swadhan_plus_permium_payingterm);
                        setFocusable(edt_bi_smart_swadhan_plus_sum_assured_amount);
                        edt_bi_smart_swadhan_plus_sum_assured_amount
                                .requestFocus();

                    }

                    public void onNothingSelected(AdapterView<?> arg0) {
                        // TODO Auto-generated method stub

                    }
                });

        spnr_bi_smart_swadhan_plus_premium_frequency
                .setOnItemSelectedListener(new OnItemSelectedListener() {

                    public void onItemSelected(AdapterView<?> adapter,
                                               View arg1, int position, long id) {
                        // TODO Auto-generated method stub

                        clearFocusable(spnr_bi_smart_swadhan_plus_premium_frequency);
                        setFocusable(spnr_bi_smart_swadhan_plus_policyterm);
                        spnr_bi_smart_swadhan_plus_policyterm.requestFocus();

                    }

                    public void onNothingSelected(AdapterView<?> arg0) {
                        // TODO Auto-generated method stub

                    }
                });

        spnr_bi_smart_swadhan_plus_distribution_channel
                .setOnItemSelectedListener(new OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> adapter,
                                               View arg1, int position, long id) {
                        // TODO Auto-generated method stub

                        distribution_channel = spnr_bi_smart_swadhan_plus_distribution_channel.getSelectedItem().toString();

                        clearFocusable(spnr_bi_smart_swadhan_plus_distribution_channel);
                        setFocusable(cb_kerladisc);
                        cb_kerladisc.requestFocus();

                    }

                    @Override
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

                // clearFocusable(edt_bi_smart_swadhan_plus_sum_assured_amount);
                inputVal = new StringBuilder();
                retVal = new StringBuilder();

                lifeAssured_First_Name = edt_bi_smart_swadhan_plus_life_assured_first_name
                        .getText().toString();
                lifeAssured_Middle_Name = edt_bi_smart_swadhan_plus_life_assured_middle_name
                        .getText().toString();
                lifeAssured_Last_Name = edt_bi_smart_swadhan_plus_life_assured_last_name
                        .getText().toString();

                gender = spnr_bi_smart_swadhan_plus_selGender.getSelectedItem().toString();

                name_of_life_assured = lifeAssured_Title + " "
                        + lifeAssured_First_Name + " "
                        + lifeAssured_Middle_Name + " " + lifeAssured_Last_Name;

                mobileNo = edt_smart_swadhan_plus_contact_no.getText()
                        .toString();
                emailId = edt_smart_swadhan_plus_Email_id.getText().toString();
                ConfirmEmailId = edt_smart_swadhan_plus_ConfirmEmail_id
                        .getText().toString();

                if (valLifeAssuredProposerDetail() && valDob()
                        && valBasicDetail() && valMaturityAge()
                        && valSumAssured() && valPPTOnProceed()
                        && valDoYouBackdate() && valBackdate()
                        && TrueBackdate()) {

                    name_of_proposer = "";
                    System.out.println("Output1:" + output);
                    addListenerOnSubmit();
                    System.out.println("Output2:" + output);
                    getInput(smartSwadhanPlusBean);
                    // insertDataIntoDatabase();
                    if (valPremiumError) {

                        if (needAnalysis_flag == 0) {
                            Intent i = new Intent(getApplicationContext(),
                                    success.class);

                            i.putExtra("ProductName",
                                    "Product : SBI Life - Smart Swadhan Plus (UIN:111N104V02)");

                            i.putExtra(
                                    "op",
                                    "Basic Premium is Rs. "
                                            + currencyFormat.format(Double.parseDouble(prsObj.parseXmlTag(
                                            retVal.toString(),
                                            "totInstPrem_exclST"))));
                            i.putExtra(
                                    "op1",
                                    "First Year Applicable Tax is Rs. "
                                            + currencyFormat.format(Double.parseDouble(prsObj
                                            .parseXmlTag(
                                                    retVal.toString(),
                                                    "FYServiceTax"))));
                            i.putExtra(
                                    "op2",
                                    "First Year Premium with Applicable Tax is Rs. "
                                            + currencyFormat.format(Double.parseDouble(prsObj.parseXmlTag(
                                            retVal.toString(),
                                            "FYtotInstPrem_inclST"))));

                            if (!(spnr_bi_smart_swadhan_plus_plan
                                    .getSelectedItem().toString()
                                    .equals("Single"))) {
                                i.putExtra(
                                        "op3",
                                        "Second Year Applicable Tax is Rs. "
                                                + currencyFormat.format(Double.parseDouble(prsObj
                                                .parseXmlTag(retVal
                                                                .toString(),
                                                        "SYServiceTax"))));
                                i.putExtra(
                                        "op3",
                                        "Second Year Premium with Applicable Tax is Rs. "
                                                + currencyFormat.format(Double.parseDouble(prsObj.parseXmlTag(
                                                retVal.toString(),
                                                "SYtotInstPrem_inclST"))));

                            }

                            // i.putExtra("ProductDescName","SBI Life - Smart Swadhan Plus");

                            i.putExtra("header",
                                    "SBI Life - Smart Swadhan Plus");
                            i.putExtra("header1", "(UIN:111N104V02)");

                            startActivity(i);
                        } else
                            Dialog();
                    }
                }

            }
        });

    }

    private void addListenerOnSubmit() {

        smartSwadhanPlusBean = new SmartSwadhanPlusBean();

        smartSwadhanPlusBean.setDistribution_channel(spnr_bi_smart_swadhan_plus_distribution_channel.getSelectedItem().toString());

		/*if (cb_staffdisc.isChecked()) {
			smartSwadhanPlusBean.setStaffDisc(true);
		} else {
			smartSwadhanPlusBean.setStaffDisc(false);
		}*/

        if (cb_kerladisc.isChecked()) {
            smartSwadhanPlusBean.setKerlaDisc(true);
        } else {
            smartSwadhanPlusBean.setKerlaDisc(false);
        }

        if (cb_bi_smart_swadhan_plus_JKResident.isChecked()) {
            smartSwadhanPlusBean.setJkResident(true);

        } else {
            smartSwadhanPlusBean.setJkResident(false);
        }

        smartSwadhanPlusBean.setAge(Integer
                .parseInt(edt_bi_smart_swadhan_plus_life_assured_age.getText()
                        .toString()));

        smartSwadhanPlusBean.setSumAssured(Integer
                .parseInt(edt_bi_smart_swadhan_plus_sum_assured_amount
                        .getText().toString()));
        smartSwadhanPlusBean.setPolicyTerm(Integer
                .parseInt(spnr_bi_smart_swadhan_plus_policyterm
                        .getSelectedItem().toString()));
        smartSwadhanPlusBean.setPlanType(spnr_bi_smart_swadhan_plus_plan
                .getSelectedItem().toString());

        smartSwadhanPlusBean.setGender(spnr_bi_smart_swadhan_plus_selGender
                .getSelectedItem().toString());

        if (spnr_bi_smart_swadhan_plus_plan.getSelectedItem().toString()
                .equals("Single")) {
            smartSwadhanPlusBean.setPremiumFreq("Single");
            smartSwadhanPlusBean.setPremiumPayingTerm(Integer
                    .parseInt("1"));
           /* smartSwadhanPlusBean.setPremiumPayingTerm(Integer
					.parseInt(spnr_bi_smart_swadhan_plus_policyterm
                            .getSelectedItem().toString()));*/
        } else if (spnr_bi_smart_swadhan_plus_plan.getSelectedItem().toString()
                .equals("Regular")) {
            smartSwadhanPlusBean
                    .setPremiumFreq(spnr_bi_smart_swadhan_plus_premium_frequency
                            .getSelectedItem().toString());
            smartSwadhanPlusBean.setPremiumPayingTerm(Integer
                    .parseInt(spnr_bi_smart_swadhan_plus_policyterm
                            .getSelectedItem().toString()));
        } else if (spnr_bi_smart_swadhan_plus_plan.getSelectedItem().toString()
                .equals("LPPT")) {
            smartSwadhanPlusBean
                    .setPremiumFreq(spnr_bi_smart_swadhan_plus_premium_frequency
                            .getSelectedItem().toString());
            smartSwadhanPlusBean.setPremiumPayingTerm(Integer
                    .parseInt(spnr_bi_smart_swadhan_plus_permium_payingterm
                            .getSelectedItem().toString()));
        }

        showsmartSwadhanPlusOutputPg(smartSwadhanPlusBean);

    }

    private void getInput(SmartSwadhanPlusBean smartSwadhanPlusBean) {

        inputVal = new StringBuilder();

        String distribution_channel = smartSwadhanPlusBean.getDistribution_channel();


        String LifeAssured_title = spnr_bi_smart_swadhan_plus_life_assured_title
                .getSelectedItem().toString();
        String LifeAssured_firstName = edt_bi_smart_swadhan_plus_life_assured_first_name
                .getText().toString();
        String LifeAssured_middleName = edt_bi_smart_swadhan_plus_life_assured_middle_name
                .getText().toString();
        String LifeAssured_lastName = edt_bi_smart_swadhan_plus_life_assured_last_name
                .getText().toString();
        String LifeAssured_DOB = btn_bi_smart_swadhan_plus_life_assured_date
                .getText().toString();
        String LifeAssured_age = edt_bi_smart_swadhan_plus_life_assured_age
                .getText().toString();
        /*
         * if (LifeAssured_title.equals("Mr.")) LifeAssured_gender = "Male";
         * else LifeAssured_gender = "Female";
         */
        String proposer_title = "";
        String proposer_firstName = "";
        String proposer_middleName = "";
        String proposer_lastName = "";
        String proposer_DOB = "";
        String proposer_age = "";
        String proposer_gender = "";

        int age = smartSwadhanPlusBean.getAge();
        String plan = smartSwadhanPlusBean.getPlanType();

        String gender = smartSwadhanPlusBean.getGender();
        int basicPolicyTerm = smartSwadhanPlusBean.getPolicyTerm();
        int PremiumPayingTerm = smartSwadhanPlusBean.getPremiumPayingTerm();
        double basicSumAssured = smartSwadhanPlusBean.getSumAssured();
        String PremPayingMode = smartSwadhanPlusBean.getPremiumFreq();

        boolean isJKresident = smartSwadhanPlusBean.getJkResident();
        /*boolean isStaffOrNot = smartSwadhanPlusBean.getStaffDisc();*/
        boolean isStaffOrNot = false;
        boolean smokerOrNot;

        String is_Smoker_or_Not = "";
        smokerOrNot = is_Smoker_or_Not.equalsIgnoreCase("Smoker");

        String wish_to_backdate_flag;
        if (rb_smart_swadhan_plus_backdating_yes.isChecked())
            wish_to_backdate_flag = "y";
        else
            wish_to_backdate_flag = "n";
        String backdate;
        if (wish_to_backdate_flag.equals("y"))
            backdate = btn_smart_swadhan_plus_backdatingdate.getText()
                    .toString();
        else
            backdate = "";

        inputVal.append("<?xml version='1.0' encoding='utf-8' ?><smartSwadhanPlusBean>");

        inputVal.append("<LifeAssured_title>").append(LifeAssured_title).append("</LifeAssured_title>");
        inputVal.append("<LifeAssured_firstName>").append(LifeAssured_firstName).append("</LifeAssured_firstName>");
        inputVal.append("<LifeAssured_middleName>").append(LifeAssured_middleName).append("</LifeAssured_middleName>");
        inputVal.append("<LifeAssured_lastName>").append(LifeAssured_lastName).append("</LifeAssured_lastName>");
        inputVal.append("<LifeAssured_DOB>").append(LifeAssured_DOB).append("</LifeAssured_DOB>");
        inputVal.append("<LifeAssured_age>").append(LifeAssured_age).append("</LifeAssured_age>");
        inputVal.append("<gender>").append(gender).append("</gender>");
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

        inputVal.append("<isJKResident>").append(isJKresident).append("</isJKResident>");
        inputVal.append("<isStaff>").append(isStaffOrNot).append("</isStaff>");
        inputVal.append("<str_kerla_discount>").append(str_kerla_discount).append("</str_kerla_discount>");
        inputVal.append("<isSmoker>").append(smokerOrNot).append("</isSmoker>");
        inputVal.append("<age>").append(age).append("</age>");
        inputVal.append("<plan>").append(plan).append("</plan>");

        inputVal.append("<policyTerm>").append(basicPolicyTerm).append("</policyTerm>");
        inputVal.append("<premiumPayingTerm>").append(PremiumPayingTerm).append("</premiumPayingTerm>");

        inputVal.append("<premFreq>").append(PremPayingMode).append("</premFreq>");
        inputVal.append("<sumAssured>").append(basicSumAssured).append("</sumAssured>");

        inputVal.append("<Wish_to_backdate_policy>").append(wish_to_backdate_flag).append("</Wish_to_backdate_policy>");
        inputVal.append("<distribution_channel>").append(distribution_channel).append("</distribution_channel>");
        inputVal.append("<backdating_Date>").append(backdate).append("</backdating_Date>");

        //Added Tushar Kadam Kerla Applicable Tax 7-Jun-2019
        String str_kerla_discount = "N";
        if (cb_kerladisc.isChecked()) {
            str_kerla_discount = "Y";
        }

        inputVal.append("<KFC>").append(str_kerla_discount).append("</KFC>");
        //End Added Tushar Kadam Kerla Applicable Tax 7-Jun-2019

        inputVal.append("</smartSwadhanPlusBean>");

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

                case 5:
                    // ProposerAge = final_age;
                    final_age = Integer.toString(age);
                    if (final_age.contains("-")) {
                        commonMethods.BICommonDialog(context, "Please fill Valid Birth Date");
                    } else {

                        if (18 <= age && age <= 65) {
                            lifeAssuredAge = final_age;
                            btn_bi_smart_swadhan_plus_life_assured_date
                                    .setText(date);
                            edt_bi_smart_swadhan_plus_life_assured_age
                                    .setText(final_age);

                            lifeAssured_date_of_birth = getDate1(date + "");

                            if (!proposer_Backdating_BackDate.equals("")) {
                                if (proposer_Backdating_WishToBackDate_Policy
                                        .equalsIgnoreCase("y")) {
                                    rb_smart_swadhan_plus_backdating_no
                                            .setChecked(true);
                                    ll_smart_swadhan_plus_backdating
                                            .setVisibility(View.GONE);
                                }
                                proposer_Backdating_BackDate = "";
                                btn_smart_swadhan_plus_backdatingdate
                                        .setText("Select Date");
                            } else {

                                // clearFocusable(btn_bi_smart_humsafar_life_assured_date);
                                // setFocusable(spnr_bi_smart_humsafar_proposer_title);
                                // spnr_bi_smart_humsafar_proposer_title
                                // .requestFocus();
                                clearFocusable(btn_bi_smart_swadhan_plus_life_assured_date);
                                setFocusable(edt_smart_swadhan_plus_contact_no);
                                edt_smart_swadhan_plus_contact_no.requestFocus();

                            }

                        } else {
                            commonMethods.BICommonDialog(context, "Minimum Age should be 18 yrs and Maximum Age should be "
                                    + 65 + " yrs For LifeAssured");
                            btn_bi_smart_swadhan_plus_life_assured_date
                                    .setText("Select Date");
                            edt_bi_smart_swadhan_plus_life_assured_age.setText("");
                            lifeAssured_date_of_birth = "";

                            clearFocusable(btn_bi_smart_swadhan_plus_life_assured_date);
                            setFocusable(btn_bi_smart_swadhan_plus_life_assured_date);
                            btn_bi_smart_swadhan_plus_life_assured_date
                                    .requestFocus();
                        }
                    }
                    break;

                case 6:
                    if (age >= 0) {
                        proposer_Backdating_BackDate = date + "";
                        btn_smart_swadhan_plus_backdatingdate
                                .setText(proposer_Backdating_BackDate);
                        clearFocusable(btn_smart_swadhan_plus_backdatingdate);

                    } else {
                        commonMethods.dialogWarning(context, "Please Select Valid BackDating Date", true);
                        btn_smart_swadhan_plus_backdatingdate
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

            int life_assured_age = calculateMyAge(mYear,
                    Integer.parseInt(mont), Integer.parseInt(day),
                    lifeAssured_date_of_birth);
            String str_final_Age = Integer.toString(life_assured_age);
            edt_bi_smart_swadhan_plus_life_assured_age.setText(str_final_Age);

        }
    }

    private int calculateMyAge(int year, int month, int day, String Value) {
        Calendar nowCal = new GregorianCalendar(year, month, day);
        System.out.println(" value : " + Value);
        String[] ProposerDob = getDate(Value).split("/");

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

    private void Dialog() {

        final Dialog d = new Dialog(this);
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);
        d.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.BLACK));

        d.setContentView(R.layout.layout_smart_swadhan_plus_bi_grid);

        TextView tv_proposername = d
                .findViewById(R.id.tv_smart_swadhan_plus_proposername);
        TextView tv_lifeassured = (TextView) d
                .findViewById(R.id.tv_smart_swadhan_plus_lifeassuredname);
        TextView tv_proposal_number = d
                .findViewById(R.id.tv_smart_swadhan_plus_proposal_number);
        TextView tv_smart_swadhan_plus_distribution_channel = d
                .findViewById(R.id.tv_smart_swadhan_plus_distribution_channel);
        TextView tv_bi_smart_swadhan_plus_plan_proposed = d
                .findViewById(R.id.tv_bi_smart_swadhan_plus_plan_proposed);
        TextView tv_bi_smart_swadhan_plus_life_assured_age = d
                .findViewById(R.id.tv_bi_smart_swadhan_plus_life_assured_age);
        TextView tv_bi_smart_swadhan_plus_life_assured_Age = d
                .findViewById(R.id.tv_bi_smart_swadhan_plus_life_assured_Age);
        TextView tv_bi_smart_swadhan_plus_life_assured_gender = d
                .findViewById(R.id.tv_bi_smart_swadhan_plus_life_assured_gender);
        TextView tv_bi_smart_swadhan_plus_life_assured_gender2 = d
                .findViewById(R.id.tv_bi_smart_swadhan_plus_life_assured_gender2);
        TextView tv_bi_smart_swadhan_plus_life_assured_premium_frequency = d
                .findViewById(R.id.tv_bi_smart_swadhan_plus_life_assured_premium_frequency);

        TextView tv_bi_smart_swadhan_plus_life_assured_state = d
                .findViewById(R.id.tv_bi_smart_swadhan_plus_life_assured_state);
        TextView tv_bi_smart_swadhan_plus_term = d
                .findViewById(R.id.tv_bi_smart_swadhan_plus_term);
        TextView tv_bi_smart_swadhan_plus_premium_paying_term = d
                .findViewById(R.id.tv_bi_smart_swadhan_plus_premium_paying_term);
        TextView tv_bi_smart_swadhan_plus_sum_assured = d
                .findViewById(R.id.tv_bi_smart_swadhan_plus_sum_assured);

        TextView tv_bi_smart_swadhan_plus_sum_assured_on_death = d
                .findViewById(R.id.tv_bi_smart_swadhan_plus_sum_assured_on_death);

        TextView tv_bi_smart_swadhan_plus_rate_applicabletax = d
                .findViewById(R.id.tv_bi_smart_swadhan_plus_rate_applicabletax);


        TextView tv_bi_smart_swadhan_plus_yearly_premium = d
                .findViewById(R.id.tv_bi_smart_swadhan_plus_yearly_premium);

        TextView tv_bi_smart_swadhan_plus_basic_prem2 = d
                .findViewById(R.id.tv_bi_smart_swadhan_plus_basic_prem2);


        TextView tv_bi_smart_swadhan_plus_basic_prem = d
                .findViewById(R.id.tv_bi_smart_swadhan_plus_basic_prem);

        // First year policy
        TextView tv_bi_smart_swadhan_plus_basic_premium_first_year = d
                .findViewById(R.id.tv_bi_smart_swadhan_plus_basic_prem_first_year);

        TextView tv_bi_smart_swadhan_plus_service_tax_first_year = (TextView) d
                .findViewById(R.id.tv_bi_smart_swadhan_plus_service_tax_first_year);
        TextView tv_bi_smart_swadhan_plus_yearly_premium_with_tax_first_year = d
                .findViewById(R.id.tv_bi_smart_swadhan_plus_premium_with_service_tax_first_year);

        // Seconf year policy onwards
        TextView tv_bi_smart_swadhan_plus_basic_premium_second_year = d
                .findViewById(R.id.tv_bi_smart_swadhan_plus_basic_prem_second_year);
        TextView tv_bi_smart_swadhan_plus_service_tax_second_year = d
                .findViewById(R.id.tv_bi_smart_swadhan_plus_service_tax_second_year);
        TextView tv_bi_smart_swadhan_plus_yearly_premium_with_tax_second_year = d
                .findViewById(R.id.tv_bi_smart_swadhan_plus_premium_with_service_tax_second_year);

        TableRow tr_prem_great_then_onelakh = (TableRow) d.findViewById(R.id.tr_prem_great_then_onelakh);
        final TextView edt_proposer_name = d
                .findViewById(R.id.edt_ProposalName);
        GridView gv_userinfo = d
                .findViewById(R.id.gv_smart_swadhan_plus_userinfo);
        final EditText edt_Policyholderplace = d
                .findViewById(R.id.edt_Policyholderplace);

        TextView tv_smart_swadhan_plus_sbi_life_details = d
                .findViewById(R.id.tv_smart_swadhan_plus_sbi_life_details);

        final TextView tv_premium_type = (TextView) d
                .findViewById(R.id.tv_premium_type);
        final TextView tv_premium_install_type1 = (TextView) d
                .findViewById(R.id.tv_premium_install_rider_type1);
        final TextView tv_mandatory_bi_smart_swadhan_plus_yearly_premium_with_tax1 = (TextView) d
                .findViewById(R.id.tv_mandatory_bi_smart_swadhan_plus_yearly_premium_with_tax1);

        TextView tv_bi_is_Staff = d
                .findViewById(R.id.tv_bi_is_Staff);
        TextView tv_bi_is_JK = d.findViewById(R.id.tv_bi_is_JK);

        TableRow tr_second_year = d
                .findViewById(R.id.tr_second_year);

        TextView tv_bi_smart_swadhan_plus_basic_service_tax_first_year = d
                .findViewById(R.id.tv_bi_smart_swadhan_plus_basic_service_tax_first_year);

        TextView tv_bi_smart_swadhan_plus_basic_service_tax_second_year = d
                .findViewById(R.id.tv_bi_smart_swadhan_plus_basic_service_tax_second_year);


        TextView tv_ann_prem = d
                .findViewById(R.id.tv_ann_prem);

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

        TextView tv_bi_smart_swadhan_plus_backdating_interest = d
                .findViewById(R.id.tv_bi_smart_swadhan_plus_backdating_interest);


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

        String plan = prsObj.parseXmlTag(input, "plan");
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
                            + " having received the information with respect to the above and have understood the above statement before entering into a contract.");
            edt_proposer_name_need_analysis
                    .setText("I, "
                            + name_of_life_assured
                            + " have undergone the Need Analysis after having reviewed the SBI Life Product options, I have opted for SBI LIFE - Smart Swadhan Plus");

            tv_lifeassured.setText(name_of_life_assured);
            tv_proposername.setText(name_of_life_assured);
        } else if (proposer_Is_Same_As_Life_Assured.equalsIgnoreCase("n")) {
            edt_proposer_name
                    .setText("I," + name_of_life_assured +
                            ", having received the information with respect to the above and have understood the above statement before entering into a contract.");
            edt_proposer_name_need_analysis
                    .setText("I, "
                            + name_of_proposer
                            + " have undergone the Need Analysis after having reviewed the SBI Life Product options, I have opted for SBI LIFE - Smart Swadhan Plus.");

            tv_proposername.setText(name_of_proposer);
            tv_lifeassured.setText(name_of_proposer);
        }

        TextView textviewAGentStatement = d.findViewById(R.id.textviewAGentStatement);
        textviewAGentStatement.setText(commonMethods.getAgentDeclaration(context));

        tv_proposal_number.setText(QuatationNumber);

        if (cb_kerladisc.isChecked()) {
            tv_bi_smart_swadhan_plus_life_assured_state.setText("Kerala");
        } else {
            tv_bi_smart_swadhan_plus_life_assured_state.setText("Non Kerala");
        }

        distribution_channel = prsObj.parseXmlTag(input, "distribution_channel");
        //tv_smart_swadhan_plus_distribution_channel.setText(distribution_channel);
        tv_smart_swadhan_plus_distribution_channel.setText(userType);

//        M_ChannelDetails list_channel_detail = db.getChannelDetail(sr_Code);

//        String str_cif_city = list_channel_detail.getDistanceMarketing_Flag();

//        if (str_cif_city == null) {
//            str_cif_city = "";
//        }
//        if (place2.equals("")) {
//            edt_Policyholderplace.setText(str_cif_city);
//
//        } else {
//            edt_Policyholderplace.setText(place2);
//        }
//        edt_MarketingOfficalPlace.setText(place1);

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

        commonMethods = new CommonMethods();
        imageButtonSmartSwadhanPlusProposerPhotograph = d
                .findViewById(R.id.imageButtonSmartSwadhanPlusProposerPhotograph);
        imageButtonSmartSwadhanPlusProposerPhotograph
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
                            commonMethods.windowmessageProposersgin(context,
                                    NeedAnalysisActivity.URN_NO + "_cust1sign");
                        } else {
                            commonMethods.dialogWarning(context, "Please Tick on I Agree Clause ", true);
                            setFocusable(cb_statement);
                            cb_statement.requestFocus();
                        }

                    }
                });


        // ////


        if (photoBitmap != null) {
            imageButtonSmartSwadhanPlusProposerPhotograph
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
                            imageButtonSmartSwadhanPlusProposerPhotograph
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
                        && checkboxAgentStatement.isChecked()
                        && (((photoBitmap != null
                        //remove parivartan validation
                ) && radioButtonTrasactionModeParivartan
                        .isChecked()) || radioButtonTrasactionModeManual
                        .isChecked())) {
                    NeedAnalysisActivity.str_need_analysis = "";

                    String productCode;

                    if (smartSwadhanPlusBean.getPlanType().equals("Regular")
                            || smartSwadhanPlusBean.getPlanType().equals(
                            "Single"))
                        productCode = "1ZSSPRP";
                    else
                        productCode = "1ZSSPLP";

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
                            .parseInt(policy_term), Integer
                            .parseInt(premium_paying_term),
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
                            .parseInt(policy_term), Integer
                            .parseInt(premium_paying_term),
                            productCode, getDate(lifeAssured_date_of_birth),
                            "", "", mode, inputVal
                            .toString(), retVal.toString().replace(
                            bussIll.toString(), "")));

                    CreateSmartSwadhanPlusBIPdf(smartSwadhanPlusBean);


                    NABIObj.serviceHit(BI_SmartSwadhanPlusActivity.this,
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
                        commonMethods.setFocusable(checkboxAgentStatement);
                        checkboxAgentStatement.requestFocus();
                    } else if (!radioButtonTrasactionModeParivartan.isChecked()
                            && !radioButtonTrasactionModeManual.isChecked()) {
                        commonMethods.dialogWarning(context, "Please Select Transaction Mode", true);
                        setFocusable(linearlayoutTrasactionModeParivartan);
                        linearlayoutTrasactionModeParivartan.requestFocus();
                    } else if (photoBitmap == null) {
                        commonMethods.dialogWarning(context, "Please Capture the Photo", true);
                        setFocusable(imageButtonSmartSwadhanPlusProposerPhotograph);
                        imageButtonSmartSwadhanPlusProposerPhotograph
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

        input = inputVal.toString();
        output = retVal.toString();

        distribution_channel = prsObj.parseXmlTag(input, "distribution_channel");
        if (distribution_channel != null && !distribution_channel.equals("")) {
            spnr_bi_smart_swadhan_plus_distribution_channel.setSelection(getIndex(spnr_bi_smart_swadhan_plus_distribution_channel, distribution_channel), false);
        }

        staffdiscount = prsObj.parseXmlTag(input, "isStaff");

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


        str_kerla_discount = prsObj.parseXmlTag(input, "str_kerla_discount");

        if (str_kerla_discount.equalsIgnoreCase("Yes")) {
            if (smartSwadhanPlusBean.getPlanType().equalsIgnoreCase("Single")) {
                tv_bi_smart_swadhan_plus_rate_applicabletax.setText("4.75%");
            } else {
                tv_bi_smart_swadhan_plus_rate_applicabletax.setText(" 4.75% in the 1st policy year and 2.375% from 2nd policy year onwards");
            }
        } else {
            if (smartSwadhanPlusBean.getPlanType().equalsIgnoreCase("Single")) {
                tv_bi_smart_swadhan_plus_rate_applicabletax.setText("4.50%");
            } else {
                tv_bi_smart_swadhan_plus_rate_applicabletax.setText("4.5% in the 1st policy year and 2.25% from 2nd policy year onwards");
            }
        }

        String age_entry = prsObj.parseXmlTag(input, "age");
        tv_bi_smart_swadhan_plus_life_assured_age.setText(age_entry + " Years");
        tv_bi_smart_swadhan_plus_life_assured_Age.setText(age_entry + " Years");

        gender = prsObj.parseXmlTag(input, "gender");
        tv_bi_smart_swadhan_plus_life_assured_gender.setText(gender);
        tv_bi_smart_swadhan_plus_life_assured_gender2.setText(gender);

        policy_term = prsObj.parseXmlTag(input, "policyTerm");
        tv_bi_smart_swadhan_plus_term.setText(policy_term + " Years");

        plan = prsObj.parseXmlTag(input, "plan");
        premium_paying_term = prsObj.parseXmlTag(input, "premiumPayingTerm");
        premium_paying_frequency = prsObj.parseXmlTag(input, "premFreq");

        switch (plan) {
            case "Single":
                tv_bi_smart_swadhan_plus_plan_proposed.setText(plan);
                tv_bi_smart_swadhan_plus_life_assured_premium_frequency
                        .setText(plan);
                tv_bi_smart_swadhan_plus_premium_paying_term.setText("1 Year");
                tv_ann_prem.setText("Single Premium");

                break;
            case "Regular":
                tv_bi_smart_swadhan_plus_plan_proposed.setText(plan);
                tv_bi_smart_swadhan_plus_life_assured_premium_frequency
                        .setText(premium_paying_frequency);
                tv_bi_smart_swadhan_plus_premium_paying_term.setText(policy_term
                );
                tv_ann_prem.setText("Annualized premium");

                break;
            case "LPPT":
                tv_bi_smart_swadhan_plus_plan_proposed.setText(plan);
                tv_bi_smart_swadhan_plus_life_assured_premium_frequency
                        .setText(premium_paying_frequency);
                tv_bi_smart_swadhan_plus_premium_paying_term
                        .setText(premium_paying_term + " Years");
                tv_ann_prem.setText("Annualized premium");
                break;
        }

        sum_assured = prsObj.parseXmlTag(input, "sumAssured");

        tv_bi_smart_swadhan_plus_sum_assured
                .setText("Rs. "
                        + getformatedThousandString(Integer.parseInt(CommonForAllProd.getRound(CommonForAllProd.getStringWithout_E(Double
                        .valueOf((sum_assured.equals("") || sum_assured == null) ? "0"
                                : sum_assured))))));

        tv_bi_smart_swadhan_plus_sum_assured_on_death.setText("Rs "
                + getformatedThousandString(Integer.parseInt(CommonForAllProd.getRound(CommonForAllProd.getStringWithout_E(Double
                .valueOf((sum_assured.equals("") || sum_assured == null) ? "0"
                        : sum_assured))))));

        str_kerla_discount = prsObj.parseXmlTag(input, "str_kerla_discount");
       /* if (str_kerla_discount.equalsIgnoreCase("Yes")) {
            tv_bi_smart_swadhan_plus_rate_applicabletax.setText(" 4.75% in the 1st policy year and 2.375% from 2nd policy year onwards");
        } else {
            tv_bi_smart_swadhan_plus_rate_applicabletax.setText("4.5% in the 1st policy year and 2.25% from 2nd policy year onwards");
        }*/


     /*   if (premium_paying_frequency.equals("Yearly")) {
				tv_premium_type.setText("Yearly " + "premium ");
            //tv_premium_install_type1.setText("Yearly " + "premium (Rs.)");
            *//*tv_mandatory_bi_smart_swadhan_plus_yearly_premium_with_tax1
                    .setText("Yearly " + "premium with Applicable Taxes (Rs.)");*//*

        } else if (premium_paying_frequency.equals("Half Yearly")) {
				tv_premium_type.setText("Half Yearly " + "premium ");
            //tv_premium_install_type1.setText("Half Yearly " + "premium (Rs.)");
            *//*tv_mandatory_bi_smart_swadhan_plus_yearly_premium_with_tax1
                    .setText("Half Yearly " + "premium with Applicable Taxes (Rs.)");*//*

        } else if (premium_paying_frequency.equals("Quarterly")) {
				tv_premium_type.setText("Quarterly " + "premium ");
            //tv_premium_install_type1.setText("Quarterly " + "premium (Rs.)");
           *//* tv_mandatory_bi_smart_swadhan_plus_yearly_premium_with_tax1
                    .setText("Quarterly " + "premium with Applicable Taxes (Rs.)");*//*
        } else if (premium_paying_frequency.equals("Monthly")) {
				tv_premium_type.setText("Monthly " + "premium ");
            //tv_premium_install_type1.setText("Monthly " + "premium (Rs.)");
            *//*tv_mandatory_bi_smart_swadhan_plus_yearly_premium_with_tax1
                    .setText("Monthly " + "premium with Applicable Taxes (Rs.)");*//*
        } else if (premium_paying_frequency.equals("Single")) {
				tv_premium_type.setText("Single " + "premium ");
            //tv_premium_install_type1.setText("Single " + "premium (Rs.)");
            *//*tv_mandatory_bi_smart_swadhan_plus_yearly_premium_with_tax1
                    .setText("Single " + "premium with Applicable Taxes (Rs.)");*//*
        }*/

        if (!smartSwadhanPlusBean.getPlanType().equals("Single")) {
            retVal.append("<SYServiceTax>").append(SYServiceTax).append("</SYServiceTax>").append("<SYtotInstPrem_inclST>").append(SYtotInstPrem_inclST).append("</SYtotInstPrem_inclST>");
        }


        String servcTax = prsObj.parseXmlTag(output, "FYServiceTax");
        premWthST = prsObj.parseXmlTag(output, "FYtotInstPrem_inclST");

        String servcTaxSecondYear = prsObj.parseXmlTag(output, "SYServiceTax");
        premWthSTSecondYear = prsObj
                .parseXmlTag(output, "SYtotInstPrem_inclST");

        basicprem = prsObj.parseXmlTag(output, "totInstPrem_exclST");

        tv_bi_smart_swadhan_plus_basic_prem
                .setText("" + CommonForAllProd.getRound(CommonForAllProd
                        .getStringWithout_E(Double.valueOf(basicprem
                                .equals("") ? "0" : basicprem))));

        tv_bi_smart_swadhan_plus_basic_prem2
                .setText("" + CommonForAllProd.getRound(CommonForAllProd
                        .getStringWithout_E(Double.valueOf(basicprem
                                .equals("") ? "0" : basicprem))));

        tv_bi_smart_swadhan_plus_yearly_premium
                .setText(""
                        + CommonForAllProd.getRound(CommonForAllProd
                        .getStringWithout_E(Double.valueOf(basicprem
                                .equals("") ? "0" : basicprem))));

        if (Double.parseDouble(CommonForAllProd.getRound(CommonForAllProd
                .getStringWithout_E(Double.valueOf(basicprem
                        .equals("") ? "0" : basicprem)))) > 100000) {
            // tr_prem_great_then_onelakh.setVisibility(View.VISIBLE);
        }

        tv_bi_smart_swadhan_plus_basic_premium_first_year
                .setText(CommonForAllProd.getRound(CommonForAllProd.getStringWithout_E(Double
                        .valueOf(premWthST.equals("") ? "0" : premWthST))));

        /*tv_bi_smart_swadhan_plus_service_tax_first_year
                .setText(CommonForAllProd.getRound(CommonForAllProd.getStringWithout_E(Double
                        .valueOf(servcTax.equals("") ? "0" : servcTax))));*/

        tv_bi_smart_swadhan_plus_yearly_premium_with_tax_first_year
                .setText("" + CommonForAllProd.getRound(CommonForAllProd.getStringWithout_E(Double
                        .valueOf(premWthST.equals("") ? "0" : premWthST))));

        // Amit changes start- 23-5-2016
        // basicServiceTax = prsObj.parseXmlTag(output, "basicServiceTax");
        String basicServiceTax = prsObj.parseXmlTag(output, "FYServiceTax");

        tv_bi_smart_swadhan_plus_basic_service_tax_first_year.setText(""
                + CommonForAllProd.getRound(CommonForAllProd
                .getStringWithout_E(Double.valueOf(basicServiceTax
                        .equals("") ? "0" : basicServiceTax))));

//        SBCServiceTax = prsObj.parseXmlTag(output, "SBCServiceTax");

//        tv_bi_smart_swadhan_plus_swachh_bharat_cess_first_year.setText(""
//                + CommonForAllProd.getRound(CommonForAllProd
//                .getStringWithout_E(Double.valueOf(SBCServiceTax
//                        .equals("") ? "0" : SBCServiceTax))));

//        tv_bi_smart_swadhan_plus_swachh_bharat_tax_first_year.setText("Rs. "
//                + CommonForAllProd.getRound(CommonForAllProd
//                .getStringWithout_E(sbcServiceTaxDouble)));

        String KKCServiceTax = prsObj.parseXmlTag(output, "KKCServiceTax");
//        tv_bi_smart_swadhan_plus_krishi_kalyan_tax_first_year.setText("Rs. "
//                + CommonForAllProd.getRound(CommonForAllProd
//                .getStringWithout_E(kkcserviceTaxDouble)));

        // servcTax = prsObj.parseXmlTag(output, "FYServiceTax");

//        tv_bi_smart_swadhan_plus_total_service_tax_first_year.setText("Rs. "
//                + CommonForAllProd.getRound(CommonForAllProd
//                .getStringWithout_E(totalServiceTaxFirstYear)));

        double totalPremiumWithAllTaxes = Double
                .valueOf((premWthST == null || premWthST.equals("")) ? "0"
                        : premWthST);

        tv_bi_smart_swadhan_plus_yearly_premium_with_tax_first_year
                .setText(""
                        + CommonForAllProd.getRound(CommonForAllProd
                        .getStringWithout_E(totalPremiumWithAllTaxes)));

//        TextView tv_bi_smart_swadhan_plus_swachh_bharat_tax_second_year = d
//                .findViewById(R.id.tv_bi_smart_swadhan_plus_swachh_bharat_tax_second_year);
//        TextView tv_bi_smart_swadhan_plus_krishi_kalyan_tax_second_year = d
//                .findViewById(R.id.tv_bi_smart_swadhan_plus_krishi_kalyan_tax_second_year);
//        TextView tv_bi_smart_swadhan_plus_total_service_tax_second_year = d
//                .findViewById(R.id.tv_bi_smart_swadhan_plus_total_service_tax_second_year);

//        tv_bi_smart_swadhan_plus_second_year_heading
//                .setVisibility(View.INVISIBLE);
//        tv_bi_smart_swadhan_plus_basic_premium_second_year
//                .setVisibility(View.INVISIBLE);
//        tv_bi_smart_swadhan_plus_service_tax_second_year
//                .setVisibility(View.INVISIBLE);

//        tv_bi_smart_swadhan_plus_swachh_bharat_tax_second_year
//                .setVisibility(View.INVISIBLE);
//        tv_bi_smart_swadhan_plus_krishi_kalyan_tax_second_year
//                .setVisibility(View.INVISIBLE);
//        tv_bi_smart_swadhan_plus_total_service_tax_second_year
//                .setVisibility(View.INVISIBLE);

        // tr_second_year.setVisibility(View.GONE);

        if (!smartSwadhanPlusBean.getPremiumFreq().equalsIgnoreCase("Single")) {

            tr_second_year.setVisibility(View.VISIBLE);
            tv_bi_smart_swadhan_plus_basic_premium_second_year
                    .setText("" + CommonForAllProd.getRound(CommonForAllProd.getStringWithout_E(Double
                            .valueOf(premWthSTSecondYear.equals("") ? "0"
                                    : premWthSTSecondYear))));

            /*tv_bi_smart_swadhan_plus_service_tax_second_year
                    .setText(CommonForAllProd.getRound(CommonForAllProd.getStringWithout_E(Double
                            .valueOf(servcTaxSecondYear.equals("") ? "0"
                                    : servcTaxSecondYear))));*/

            tv_bi_smart_swadhan_plus_yearly_premium_with_tax_second_year
                    .setText("" + CommonForAllProd.getRound(CommonForAllProd.getStringWithout_E(Double
                            .valueOf(premWthSTSecondYear.equals("") ? "0"
                                    : premWthSTSecondYear))));

            // Amit changes start- 23-5-2016
            // basicServiceTaxSecondYear = prsObj.parseXmlTag(output,
            // "basicServiceTaxSecondYear");
            String basicServiceTaxSecondYear = prsObj.parseXmlTag(output,
                    "SYServiceTax");

            tv_bi_smart_swadhan_plus_basic_service_tax_second_year.setText(""
                    + CommonForAllProd.getRound(CommonForAllProd
                    .getStringWithout_E(Double
                            .valueOf(basicServiceTaxSecondYear
                                    .equals("") ? "0"
                                    : basicServiceTaxSecondYear))));

            String SBCServiceTaxSecondYear = prsObj.parseXmlTag(output,
                    "SBCServiceTaxSecondYear");
//            tv_bi_smart_swadhan_plus_swachh_bharat_tax_second_year
//                    .setText("Rs. "
//                            + CommonForAllProd.getRound(CommonForAllProd
//                            .getStringWithout_E(SBCServiceTaxSecondYearDouble)));

            String KKCServiceTaxSecondYear = prsObj.parseXmlTag(output,
                    "KKCServiceTaxSecondYear");
//            tv_bi_smart_swadhan_plus_krishi_kalyan_tax_second_year
//                    .setText("Rs. "
//                            + CommonForAllProd.getRound(CommonForAllProd
//                            .getStringWithout_E(KKCServiceTaxSecondYearDouble)));


//            tv_bi_smart_swadhan_plus_total_service_tax_second_year
//                    .setText("Rs. "
//                            + CommonForAllProd.getRound(CommonForAllProd
//                            .getStringWithout_E(totalServiceTax)));

            double secondYearTotalServiceTaxWithPremium = Double
                    .valueOf((premWthSTSecondYear == null || premWthSTSecondYear
                            .equals("")) ? "0" : premWthSTSecondYear);

            tv_bi_smart_swadhan_plus_yearly_premium_with_tax_second_year
                    .setText(""
                            + CommonForAllProd.getRound(CommonForAllProd
                            .getStringWithout_E(secondYearTotalServiceTaxWithPremium)));
        } else if (smartSwadhanPlusBean.getPremiumFreq().equalsIgnoreCase("Single")) {
            tr_second_year.setVisibility(View.VISIBLE);
            tv_bi_smart_swadhan_plus_basic_premium_second_year.setText("Not Applicable");
            tv_bi_smart_swadhan_plus_service_tax_second_year.setText("");
            tv_bi_smart_swadhan_plus_yearly_premium_with_tax_second_year.setText("Not Applicable");
        }

        // Back dating
        BackdatingInt = prsObj.parseXmlTag(output, "backdateInt");

        tv_bi_smart_swadhan_plus_backdating_interest.setText(" Rs. "
                + CommonForAllProd.getRound(CommonForAllProd
                .getStringWithout_E(Double.valueOf(BackdatingInt
                        .equals("") ? "0" : BackdatingInt))));

        // tv_bi_smart_swadhan_plus_backdating_interest.setText("Hardcoded");

        tv_smart_swadhan_plus_sbi_life_details
                .setText("Your SBI LIFE - Smart Swadhan Plus(UIN: 111N104V02) is a Regular/Limited premium policy,for which your first year "
                        + premium_paying_frequency
                        + " premium is Rs. "
                        + CommonForAllProd.getRound(CommonForAllProd
                        .getStringWithout_E(Double.valueOf(premWthST
                                .equals("") ? "0" : premWthST)))
                        + " Your policy Term is "
                        + policy_term
                        + " years,Premium Payment Term is "
                        + premium_paying_term
                        + " years and Basic Sum Assured is Rs. "
                        + getformatedThousandString(Integer.parseInt(CommonForAllProd.getRound(CommonForAllProd.getStringWithout_E(Double
                        .valueOf((sum_assured.equals("") || sum_assured == null) ? "0"
                                : sum_assured))))));

        for (int i = 1; i <= Integer.parseInt(policy_term); i++) {

            String end_of_year = prsObj
                    .parseXmlTag(output, "policyYr" + i + "");
            String yearly_basic_premium = ((int) Double.parseDouble(prsObj
                    .parseXmlTag(output, "AnnPrem" + i + ""))) + "";
            String SurvivalBenefits = ((int) Double.parseDouble(prsObj
                    .parseXmlTag(output, "SurvivalBenefits" + i + "")))
                    + "";
            String OtherBenefitsifAny = ((int) Double.parseDouble(prsObj
                    .parseXmlTag(output, "OtherBenefitsifAny" + i + "")))
                    + "";
            String guaranteed_maturity_benefit = ((int) Double
                    .parseDouble(prsObj.parseXmlTag(output,
                            "benefitPaybleAtMaturity" + i + "")))
                    + "";
            //String cumulative_premium = "";
            // String guaranteed_addition = ((int) Double.parseDouble(prsObj
            // .parseXmlTag(output, "guarntdAddtn" + i + ""))) + "";
            String guaranteed_death_benefit = ((int) Double.parseDouble(prsObj
                    .parseXmlTag(output, "guarantedDeathBenefit" + i + "")))
                    + "";

            String guaranteed_surrender_value = ((int) Double
                    .parseDouble(prsObj.parseXmlTag(output,
                            "guaranSurrenderValue" + i + "")))
                    + "";

            String nonGuaranSurrenderValue = ((int) Double
                    .parseDouble(prsObj.parseXmlTag(output,
                            "nonGuaranSurrenderValue" + i + "")))
                    + "";

            list_data.add(new M_BI_Smart_Swadhan_Plus_Adapter(end_of_year,
                    yearly_basic_premium, SurvivalBenefits, OtherBenefitsifAny,
                    guaranteed_maturity_benefit, guaranteed_death_benefit,
                    guaranteed_surrender_value, nonGuaranSurrenderValue));
        }

        Adapter_BI_SmartSwadhanPlusGrid adapter = new Adapter_BI_SmartSwadhanPlusGrid(
                this, list_data);
        gv_userinfo.setAdapter(adapter);
        GridHeight gh = new GridHeight();
        gh.getheight(gv_userinfo, policy_term);



		/*else if (Integer.parseInt(policy_term) <= 30) {

			gv_userinfo.getLayoutParams().height = 270 * 6;

		} else if (Integer.parseInt(policy_term) <= 35) {

			gv_userinfo.getLayoutParams().hei
			ght = 270 * 7;

		}*/

     /*   LinearLayout ll_signature = d
				.findViewById(R.id.ll_signature);
		TableRow tbrw_quotationNo = d
				.findViewById(R.id.tbrw_quotationNo);
		if (needAnalysis_flag == 1) {
			ll_signature.setVisibility(View.VISIBLE);
			tbrw_quotationNo.setVisibility(View.VISIBLE);

		} else {
			ll_signature.setVisibility(View.GONE);
			tbrw_quotationNo.setVisibility(View.GONE);
        }*/
        d.show();
    }


    private void CreateSmartSwadhanPlusBIPdf(SmartSwadhanPlusBean sspBean) {

        currencyFormat = new DecimalFormat("##,##,##,###");
        try {

            Font headerBold = new Font(Font.FontFamily.TIMES_ROMAN, 11,
                    Font.BOLD, BaseColor.WHITE);
            Font small_bold = new Font(Font.FontFamily.TIMES_ROMAN, 8,
                    Font.BOLD);
            Font small_bold_for_name = new Font(Font.FontFamily.TIMES_ROMAN,
                    10, Font.BOLD);

            Font small_bold1 = new Font(Font.FontFamily.TIMES_ROMAN, 8,
                    Font.BOLD);

            Font small_bold2 = new Font(Font.FontFamily.TIMES_ROMAN, 6,
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

            // For the BI Smart Elite Table Header(Grey One)
            Paragraph Para_Header = new Paragraph();
            Para_Header
                    .add(new Paragraph(
                            "Benefit Illustration for SBI LIFE - Smart Swadhan Plus Plan (UIN - 111N104V02)",
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
                    small_normal);
            para_address.setAlignment(Element.ALIGN_CENTER);
            Paragraph para_address2 = new Paragraph(
                    "Registered & Corporate Office: 'Natraj', M.V.Road and Western Express Highway Junction, Andheri (East), Mumbai - 400069",
                    small_normal);
            para_address2.setAlignment(Element.ALIGN_CENTER);
            Paragraph para_address3 = new Paragraph(
                    "IRDAI Registration No. 111 | Website: www.sbilife.co.in | Email: info@sbilife.co.in | CIN: L99999MH2000PLC129113",
                    small_normal);
            para_address3.setAlignment(Element.ALIGN_CENTER);
            Paragraph para_address4 = new Paragraph(
                    "Toll Free: 1800 267 9090 (Between 9.00 am & 9.00 pm)",
                    small_normal);
            para_address4.setAlignment(Element.ALIGN_CENTER);
            Paragraph para_address1 = new Paragraph("Customised Benefit Illustration (CBI) ", small_bold);
            para_address1.setAlignment(Element.ALIGN_CENTER);
            Paragraph para_address11 = new Paragraph("Benefit Illustration(BI): SBI Life - Smart Swadhan Plus  (UIN :  111N104V02)", small_bold_for_name);
            para_address11.setAlignment(Element.ALIGN_CENTER);
            Paragraph para_address111 = new Paragraph("An Individual, Non-linked, Non-Participating, Life Insurance Savings Product with Return of Premium ", small_bold_for_name);
            para_address111.setAlignment(Element.ALIGN_CENTER);
            document.add(para_address);
            document.add(para_address2);
            document.add(para_address3);
            document.add(para_address4);
            document.add(para_img_logo_after_space_1);
            //document.add(para_address1);
            document.add(para_address11);
            document.add(para_address111);
            document.add(para_img_logo_after_space_1);
            //document.add(headertable);
            document.add(para_img_logo_after_space_1);

            document.add(para_img_logo_after_space_1);
            if (proposer_Is_Same_As_Life_Assured.equalsIgnoreCase("Y")) {
                name_of_proposer = name_of_life_assured;
            }

            PdfPTable table_proposer_name = new PdfPTable(2);
            // float[] columnWidths_table_proposer_name = { 2f, 2f, 1f, 2f };
            // table_proposer_name.setWidths(columnWidths_table_proposer_name);
            table_proposer_name.setWidthPercentage(100);

            PdfPCell ProposalNumber_cell_1 = new PdfPCell(new Paragraph(
                    "Quotation Number", small_normal));
            PdfPCell ProposalNumber_cell_2 = new PdfPCell(new Paragraph(
                    QuatationNumber, small_bold1));
            ProposalNumber_cell_2.setHorizontalAlignment(Element.ALIGN_CENTER);


            ProposalNumber_cell_1.setPadding(5);
            ProposalNumber_cell_2.setPadding(5);

            table_proposer_name.addCell(ProposalNumber_cell_1);
            table_proposer_name.addCell(ProposalNumber_cell_2);
            document.add(table_proposer_name);


            PdfPTable table_proposer_name1 = new PdfPTable(2);

            table_proposer_name1.setWidthPercentage(100);

            PdfPCell ProposalNumber_cell_11 = new PdfPCell(new Paragraph(
                    "Channel / Intermediary", small_normal));
            PdfPCell ProposalNumber_cell_21 = new PdfPCell(new Paragraph(
                    userType, small_bold1));
            ProposalNumber_cell_21.setHorizontalAlignment(Element.ALIGN_CENTER);

            ProposalNumber_cell_11.setPadding(5);
            ProposalNumber_cell_21.setPadding(5);


            table_proposer_name1.addCell(ProposalNumber_cell_11);
            table_proposer_name1.addCell(ProposalNumber_cell_21);

            document.add(table_proposer_name1);


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

//            BI_Pdftable2.addCell(BI_Pdftable2_cell1);
//            document.add(BI_Pdftable2);

            PdfPTable BI_Pdftable3 = new PdfPTable(1);
            BI_Pdftable3.setWidthPercentage(100);
            // CR_table6.setWidths(columnWidths_3);
            PdfPCell BI_Pdftable3_cell1 = new PdfPCell(
                    new Paragraph(
                            "Currently, the two rates of investment return as specified by IRDAI are 4% and 8% per annum.",
                            small_normal));

            BI_Pdftable3_cell1.setPadding(5);

            BI_Pdftable3.addCell(BI_Pdftable3_cell1);
            //document.add(BI_Pdftable3);

            PdfPTable BI_Pdftable5 = new PdfPTable(1);
            BI_Pdftable5.setWidthPercentage(100);
            // CR_table6.setWidths(columnWidths_3);
            PdfPCell BI_Pdftable5_cell1 = new PdfPCell(
                    new Paragraph(
                            "The main objective of the illustration is that the client is able to appreciate the features of the product and the flow of benefits in different circumstances with some level of quantification. For further information on the product and its benefits, please refer to the sales brochure and/or policy document. Further information will also be available on request.",
                            small_normal));

            BI_Pdftable3_cell1.setPadding(5);

            BI_Pdftable5.addCell(BI_Pdftable5_cell1);
            //document.add(BI_Pdftable5);

            PdfPTable BI_Pdftable4 = new PdfPTable(1);
            BI_Pdftable4.setWidthPercentage(100);
            // CR_table6.setWidths(columnWidths_4);
            PdfPCell BI_Pdftable4_cell1 = new PdfPCell(
                    new Paragraph(
                            "The main objective of the illustration is that the client is able to appreciate the features of the product and the flow of benefits in different circumstances with some level of quantification. For further information on the product and its benefits,  please refer to the sales brochure and/or policy document.",
                            small_normal));

            BI_Pdftable4_cell1.setPadding(5);

            BI_Pdftable4.addCell(BI_Pdftable4_cell1);
            document.add(BI_Pdftable4);
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

            PdfPCell cell_LifeAssuredName12 = new PdfPCell(new Paragraph(
                    "Name of the Life Assured", small_normal));
            cell_LifeAssuredName12.setPadding(5);
            PdfPCell cell_lLifeAssuredName22 = new PdfPCell(new Paragraph(
                    name_of_proposer, small_bold));
            cell_lLifeAssuredName22.setPadding(5);
            cell_lLifeAssuredName22.setHorizontalAlignment(Element.ALIGN_CENTER);

            PdfPCell cell_lifeAssuredAge1 = new PdfPCell(new Paragraph(
                    "Age (Years)", small_normal));
            cell_lifeAssuredAge1.setPadding(5);
            PdfPCell cell_lifeAssuredAge2 = new PdfPCell(new Paragraph(
                    sspBean.getAge() + " Years", small_bold));
            cell_lifeAssuredAge2.setPadding(5);
            cell_lifeAssuredAge2.setHorizontalAlignment(Element.ALIGN_CENTER);

            PdfPCell cell_lifeAssuredAge11 = new PdfPCell(new Paragraph(
                    "Age (Years)", small_normal));
            cell_lifeAssuredAge11.setPadding(5);
            PdfPCell cell_lifeAssuredAge21 = new PdfPCell(new Paragraph(
                    sspBean.getAge() + " Years", small_bold));
            cell_lifeAssuredAge21.setPadding(5);
            cell_lifeAssuredAge21.setHorizontalAlignment(Element.ALIGN_CENTER);

            table_lifeAssuredName.addCell(cell_LifeAssuredName1);
            table_lifeAssuredName.addCell(cell_lLifeAssuredName2);
            table_lifeAssuredName.addCell(cell_LifeAssuredName12);
            table_lifeAssuredName.addCell(cell_lLifeAssuredName22);
            table_lifeAssuredName.addCell(cell_lifeAssuredAge1);
            table_lifeAssuredName.addCell(cell_lifeAssuredAge2);
            table_lifeAssuredName.addCell(cell_lifeAssuredAge11);
            table_lifeAssuredName.addCell(cell_lifeAssuredAge21);
            document.add(table_lifeAssuredName);

            PdfPTable table_lifeAssuredDetails = new PdfPTable(4);
            table_lifeAssuredDetails.setWidthPercentage(100);

            PdfPCell cell_lifeAssuredAmaturityGender1 = new PdfPCell(
                    new Paragraph("Gender", small_normal));
            cell_lifeAssuredAmaturityGender1.setPadding(5);

            PdfPCell cell_lifeAssuredAmaturityGender2 = new PdfPCell(
                    new Paragraph(sspBean.getGender(), small_bold));
            cell_lifeAssuredAmaturityGender2.setPadding(5);
            cell_lifeAssuredAmaturityGender2
                    .setHorizontalAlignment(Element.ALIGN_CENTER);

            PdfPCell cell_lifeAssuredAmaturityGender11 = new PdfPCell(
                    new Paragraph("Gender", small_normal));
            cell_lifeAssuredAmaturityGender11.setPadding(5);
            PdfPCell cell_lifeAssuredAmaturityGender21 = new PdfPCell(
                    new Paragraph(sspBean.getGender(), small_bold));
            cell_lifeAssuredAmaturityGender21.setPadding(5);
            cell_lifeAssuredAmaturityGender21
                    .setHorizontalAlignment(Element.ALIGN_CENTER);

            String str_plan_type = "";
            if (sspBean.getPlanType().equalsIgnoreCase("Single")) {
                str_plan_type = "Single Premium (SP)";
            } else if (sspBean.getPlanType().equalsIgnoreCase("Regular")) {
                str_plan_type = "Regular Premium (RP)";
            } else if (sspBean.getPlanType().equalsIgnoreCase("LPPT")) {
                str_plan_type = "Limited Premium Payment Term (LPPT)";
            }

            PdfPCell cell_lifeAssured_Premium_frequeny1 = new PdfPCell(
                    new Paragraph("Premium Payment Option", small_normal));
            cell_lifeAssured_Premium_frequeny1.setPadding(5);
            PdfPCell cell_lifeAssured_Premium_frequeny2 = new PdfPCell(
                    new Paragraph(str_plan_type, small_bold));
            cell_lifeAssured_Premium_frequeny2.setPadding(5);
            cell_lifeAssured_Premium_frequeny2
                    .setHorizontalAlignment(Element.ALIGN_CENTER);

            PdfPCell staff = new PdfPCell(
                    new Paragraph("Staff", small_normal));
            staff.setPadding(5);
            PdfPCell staff2;
            if (staffdiscount.equalsIgnoreCase("true")) {
                staff2 = new PdfPCell(
                        new Paragraph("Yes", small_bold));
                staff2.setPadding(5);
                staff2.setHorizontalAlignment(Element.ALIGN_CENTER);
            } else {
                staff2 = new PdfPCell(
                        new Paragraph("No", small_bold));
                staff2.setPadding(5);
                staff2.setHorizontalAlignment(Element.ALIGN_CENTER);
            }

            PdfPCell blank = new PdfPCell(
                    new Paragraph("", small_normal));
            blank.setPadding(5);
            PdfPCell blank2 = new PdfPCell(
                    new Paragraph("", small_bold));
            blank2.setPadding(5);
            blank2.setHorizontalAlignment(Element.ALIGN_CENTER);

            /*PdfPCell state = new PdfPCell(
                    new Paragraph("State", small_normal));
            state.setPadding(5);
            PdfPCell state2;
            if (cb_kerladisc.isChecked()) {
                state2 = new PdfPCell(
                        new Paragraph("Kerala", small_bold));
                state2.setPadding(5);
                state2
                        .setHorizontalAlignment(Element.ALIGN_CENTER);
            } else {
                state2 = new PdfPCell(
                        new Paragraph("Non Kerala", small_bold));
                state2.setPadding(5);
                state2
                        .setHorizontalAlignment(Element.ALIGN_CENTER);
            }*/


            table_lifeAssuredDetails.addCell(cell_lifeAssuredAmaturityGender1);
            table_lifeAssuredDetails.addCell(cell_lifeAssuredAmaturityGender2);
            table_lifeAssuredDetails.addCell(cell_lifeAssuredAmaturityGender11);
            table_lifeAssuredDetails.addCell(cell_lifeAssuredAmaturityGender21);
            table_lifeAssuredDetails
                    .addCell(cell_lifeAssured_Premium_frequeny1);
            table_lifeAssuredDetails
                    .addCell(cell_lifeAssured_Premium_frequeny2);
            table_lifeAssuredDetails
                    .addCell(staff);
            table_lifeAssuredDetails
                    .addCell(staff2);
           /* table_lifeAssuredDetails
                    .addCell(blank);
            table_lifeAssuredDetails
                    .addCell(blank2);
            table_lifeAssuredDetails
                    .addCell(state);
            table_lifeAssuredDetails
                    .addCell(state2);*/

            PdfPTable BI_Pdftable41 = new PdfPTable(1);
            BI_Pdftable41.setWidthPercentage(100);
            // CR_table6.setWidths(columnWidths_4);
            PdfPCell BI_Pdftable41_cell1 = new PdfPCell(
                    new Paragraph(
                            "This benefit illustration is intended to show year-wise premiums payable and benefits under the policy.",
                            small_normal));

            BI_Pdftable41_cell1.setPadding(5);

            BI_Pdftable41.addCell(BI_Pdftable41_cell1);
            document.add(table_lifeAssuredDetails);
            document.add(para_img_logo_after_space_1);
            document.add(BI_Pdftable41);
            document.add(para_img_logo_after_space_1);

            // String isStaff = "";
            // if (sspBean.getStaffDisc()) {
            // isStaff = "yes";
            // PdfPTable table_staff_NonStaff = new PdfPTable(2);
            // table_staff_NonStaff.setWidthPercentage(100);
            //
            // PdfPCell cell_staff_NonStaff1 = new PdfPCell(new Paragraph(
            // "Staff Discount", small_normal));
            // cell_staff_NonStaff1.setPadding(5);
            //
            // PdfPCell cell_staff_NonStaff2 = new PdfPCell(new Paragraph(
            // isStaff, small_bold));
            // cell_staff_NonStaff2.setPadding(5);
            // cell_staff_NonStaff2
            // .setHorizontalAlignment(Element.ALIGN_CENTER);
            //
            // table_staff_NonStaff.addCell(cell_staff_NonStaff1);
            // table_staff_NonStaff.addCell(cell_staff_NonStaff2);
            // document.add(table_staff_NonStaff);
            // }

            // String isJk = "";
            // if (cb_bi_smart_income_protect_JKResident.isChecked()) {
            // isJk = "yes";
            // PdfPTable table_is_JK = new PdfPTable(2);
            // table_is_JK.setWidthPercentage(100);
            //
            // PdfPCell cell_is_JK1 = new PdfPCell(new Paragraph("J&K",
            // small_normal));
            // cell_is_JK1.setPadding(5);
            //
            // PdfPCell cell_is_JK2 = new PdfPCell(new Paragraph(isJk,
            // small_bold));
            // cell_is_JK2.setPadding(5);
            // cell_is_JK2.setHorizontalAlignment(Element.ALIGN_CENTER);
            //
            // table_is_JK.addCell(cell_is_JK1);
            // table_is_JK.addCell(cell_is_JK2);
            // document.add(table_is_JK);
            // }

            String isJk = "";
            if (sspBean.getJkResident()) {
                isJk = "yes";
                PdfPTable cell_is_JK1 = new PdfPTable(2);
                cell_is_JK1.setWidthPercentage(100);

                PdfPCell cell_is_JK2 = new PdfPCell(new Paragraph(
                        "Is J&K Resident", small_normal));
                cell_is_JK2.setPadding(5);
                PdfPCell cell_Backdate2 = new PdfPCell(new Paragraph(isJk,
                        small_bold));

                cell_Backdate2.setPadding(5);
                cell_Backdate2.setHorizontalAlignment(Element.ALIGN_CENTER);

                cell_is_JK1.addCell(cell_is_JK2);
                cell_is_JK1.addCell(cell_Backdate2);
                document.add(cell_is_JK1);
            }

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

            PdfPCell policy_option1 = new PdfPCell(new Paragraph(
                    "Policy Option", small_normal));
            policy_option1.setPadding(5);
            PdfPCell policy_option2 = new PdfPCell(new Paragraph("Not Applicable",
                    small_bold));

            policy_option2.setPadding(5);
            policy_option2.setHorizontalAlignment(Element.ALIGN_CENTER);

            Table_policyTerm_annualisedPremium_sumAssured.addCell(policy_option1);
            Table_policyTerm_annualisedPremium_sumAssured.addCell(policy_option2);

            PdfPCell cell_premium_paying_term1 = new PdfPCell(new Paragraph(
                    " Amount of Installment Premium (Rs.)", small_normal));
            cell_premium_paying_term1.setPadding(5);
            PdfPCell cell_premium_paying_term2 = new PdfPCell(new Phrase(
                    (currencyFormat.format(Double.parseDouble(basicprem))),
                    small_bold));

            cell_premium_paying_term2.setPadding(5);
            cell_premium_paying_term2
                    .setHorizontalAlignment(Element.ALIGN_CENTER);

            Table_policyTerm_annualisedPremium_sumAssured.addCell(cell_premium_paying_term1);
            Table_policyTerm_annualisedPremium_sumAssured.addCell(cell_premium_paying_term2);

            PdfPCell cell_Term1 = new PdfPCell(new Paragraph("Policy Term (Years)",
                    small_normal));
            PdfPCell cell_Term2 = new PdfPCell(new Paragraph(
                    sspBean.getPolicyTerm() + "", small_bold));
            cell_Term1.setPadding(5);
            cell_Term2.setPadding(5);
            cell_Term2.setHorizontalAlignment(Element.ALIGN_CENTER);

            String payingTerm = "";
            if (sspBean.getPlanType().equalsIgnoreCase("Single")) {
                payingTerm = "1";
            } else {
                payingTerm = sspBean.getPremiumPayingTerm() + "";
            }

            PdfPCell cell_plan1 = new PdfPCell(new Paragraph("Sum Assured (Rs.)",
                    small_normal));
            cell_plan1.setPadding(5);
            PdfPCell cell_plan2 = new PdfPCell(new Phrase(
                    (currencyFormat.format(sspBean.getSumAssured())),
                    small_bold));
            cell_plan2.setPadding(5);
            cell_plan2.setHorizontalAlignment(Element.ALIGN_CENTER);

            PdfPCell cell_PremiumPayingTerm1 = new PdfPCell(new Paragraph(
                    "Premium Payment Term (Years)", small_normal));
            PdfPCell cell_PremiumPayingTerm2 = new PdfPCell(new Paragraph(
                    payingTerm, small_bold));
            cell_PremiumPayingTerm1.setPadding(5);
            cell_PremiumPayingTerm2.setPadding(5);
            cell_PremiumPayingTerm2
                    .setHorizontalAlignment(Element.ALIGN_CENTER);

            Table_policyTerm_annualisedPremium_sumAssured.addCell(cell_Term1);
            Table_policyTerm_annualisedPremium_sumAssured.addCell(cell_Term2);

            Table_policyTerm_annualisedPremium_sumAssured.addCell(cell_plan1);
            Table_policyTerm_annualisedPremium_sumAssured.addCell(cell_plan2);


            document.add(Table_policyTerm_annualisedPremium_sumAssured);

            PdfPTable table_plan_premium_payingTerm = new PdfPTable(4);
            table_plan_premium_payingTerm.setWidthPercentage(100);


            //table_plan_premium_payingTerm.addCell(cell_premium_paying_term1);
            //table_plan_premium_payingTerm.addCell(cell_premium_paying_term2);
            //document.add(table_plan_premium_payingTerm);


            PdfPTable table_plan_premium_payingTerm2 = new PdfPTable(4);
            table_plan_premium_payingTerm2.setWidthPercentage(100);

            PdfPCell cell_plan11 = new PdfPCell(new Paragraph("Sum Assured on Death (at inception of the policy) (Rs.)",
                    small_normal));
            cell_plan11.setPadding(5);
            PdfPCell cell_plan22 = new PdfPCell(new Phrase(
                    (currencyFormat.format(sspBean.getSumAssured())),
                    small_bold));
            cell_plan22.setPadding(5);
            cell_plan22.setHorizontalAlignment(Element.ALIGN_CENTER);


            table_plan_premium_payingTerm2.addCell(cell_PremiumPayingTerm1);
            table_plan_premium_payingTerm2.addCell(cell_PremiumPayingTerm2);

            table_plan_premium_payingTerm2.addCell(cell_plan11);
            table_plan_premium_payingTerm2.addCell(cell_plan22);

            // table_plan_premium_payingTerm2.addCell(cell_premium_paying_term11);
            // table_plan_premium_payingTerm2.addCell(cell_premium_paying_term22);
            document.add(table_plan_premium_payingTerm2);


            PdfPTable table_plan_premium_payingTerm22 = new PdfPTable(4);
            table_plan_premium_payingTerm22.setWidthPercentage(100);

            PdfPCell cell_plan111 = new PdfPCell(new Paragraph("Mode / Frequency of Premium Payment",
                    small_normal));
            cell_plan111.setPadding(5);
            PdfPCell cell_plan222 = new PdfPCell(new Phrase(
                    (premium_paying_frequency),
                    small_bold));
            cell_plan222.setPadding(5);
            cell_plan222.setHorizontalAlignment(Element.ALIGN_CENTER);

            PdfPCell cell_premium_paying_term11 = new PdfPCell(new Paragraph(
                    " Rate of Applicable Taxes", small_normal));
            cell_premium_paying_term11.setPadding(5);

            String rate_of_applicable_tax = "";
            /*if (str_kerla_discount.equalsIgnoreCase("Yes")) {
                rate_of_applicable_tax = "4.75% in the 1st policy year and 2.375% from 2nd policy year onwards";
            } else {
                rate_of_applicable_tax = "4.5% in the 1st policy year and 2.25% from 2nd policy year onwards";
            }*/


            str_kerla_discount = prsObj.parseXmlTag(input, "str_kerla_discount");


            if (str_kerla_discount.equalsIgnoreCase("Yes")) {
                if (sspBean.getPlanType().equalsIgnoreCase("Single")) {
                    rate_of_applicable_tax = "4.75%";
                } else {
                    rate_of_applicable_tax = " 4.75% in the 1st policy year and 2.375% from 2nd policy year onwards";
                }
            } else {
                if (sspBean.getPlanType().equalsIgnoreCase("Single")) {
                    rate_of_applicable_tax = "4.50%";
                } else {
                    rate_of_applicable_tax = "4.5% in the 1st policy year and 2.25% from 2nd policy year onwards";
                }
            }


            PdfPCell cell_premium_paying_term22 = new PdfPCell(new Phrase(
                    rate_of_applicable_tax,
                    small_bold));

            cell_premium_paying_term22.setPadding(5);
            cell_premium_paying_term22
                    .setHorizontalAlignment(Element.ALIGN_CENTER);

            table_plan_premium_payingTerm22.addCell(cell_plan111);
            table_plan_premium_payingTerm22.addCell(cell_plan222);
            table_plan_premium_payingTerm22.addCell(cell_premium_paying_term11);
            table_plan_premium_payingTerm22.addCell(cell_premium_paying_term22);
            document.add(table_plan_premium_payingTerm22);


            PdfPTable table_plan_backdating = new PdfPTable(2);
            table_plan_backdating.setWidthPercentage(100);

            PdfPCell cell_Backdate1 = new PdfPCell(new Paragraph(
                    "Backdating Interest", small_normal));
            cell_Backdate1.setPadding(5);
            PdfPCell cell_Backdate2 = new PdfPCell(new Paragraph("Rs. "
                    + CommonForAllProd.getRound(CommonForAllProd
                    .getStringWithout_E(Double.valueOf(BackdatingInt
                            .equals("") ? "0" : BackdatingInt))),
                    small_bold));

            cell_Backdate2.setPadding(5);
            cell_Backdate2.setHorizontalAlignment(Element.ALIGN_CENTER);

            table_plan_backdating.addCell(cell_Backdate1);
            table_plan_backdating.addCell(cell_Backdate2);
            //document.add(table_plan_backdating);

            PdfPTable Table_Preferred_term_assured_policyTerm_annualisedPremium_sumAssured = new PdfPTable(
                    6);
            Table_Preferred_term_assured_policyTerm_annualisedPremium_sumAssured
                    .setWidthPercentage(100);

            PdfPCell cell_Preferred_term_assured_PlanProposed1 = new PdfPCell(
                    new Paragraph("Premium Paying Term", small_normal));
            cell_Preferred_term_assured_PlanProposed1.setPadding(5);
            PdfPCell cell_Preferred_term_assured_tPlanProposed2 = new PdfPCell(
                    new Paragraph(sspBean.getPremiumPayingTerm() + " Years",
                            small_bold));
            cell_Preferred_term_assured_tPlanProposed2.setPadding(5);
            cell_Preferred_term_assured_tPlanProposed2
                    .setHorizontalAlignment(Element.ALIGN_CENTER);

            PdfPCell cell_Preferred_term_assured_Term1 = new PdfPCell(
                    new Paragraph("Sum Assured", small_normal));
            PdfPCell cell_Preferred_term_assured_Term2 = new PdfPCell(
                    new Phrase(
                            (currencyFormat.format(sspBean.getSumAssured())),
                            small_bold));
            cell_Preferred_term_assured_Term1.setPadding(5);
            cell_Preferred_term_assured_Term2.setPadding(5);
            cell_Preferred_term_assured_Term2
                    .setHorizontalAlignment(Element.ALIGN_CENTER);

            PdfPCell cell_Preferred_term_assured_PremiumPayingTerm1 = new PdfPCell(
                    new Paragraph(sspBean.getPremiumFreq() + " Premium",
                            small_normal));
            PdfPCell cell_Preferred_term_assured_PremiumPayingTerm2 = new PdfPCell(
                    new Phrase((currencyFormat.format(Double
                            .parseDouble(totInstPrem_exclST))), small_bold));
            cell_Preferred_term_assured_PremiumPayingTerm1.setPadding(5);
            cell_Preferred_term_assured_PremiumPayingTerm2.setPadding(5);
            cell_Preferred_term_assured_PremiumPayingTerm2
                    .setHorizontalAlignment(Element.ALIGN_CENTER);

            // Table_Preferred_term_assured_policyTerm_annualisedPremium_sumAssured
            // .addCell(cell_Preferred_term_assured_PlanProposed1);
            // Table_Preferred_term_assured_policyTerm_annualisedPremium_sumAssured
            // .addCell(cell_Preferred_term_assured_tPlanProposed2);
            //
            Table_Preferred_term_assured_policyTerm_annualisedPremium_sumAssured
                    .addCell(cell_Preferred_term_assured_Term1);
            Table_Preferred_term_assured_policyTerm_annualisedPremium_sumAssured
                    .addCell(cell_Preferred_term_assured_Term2);
            //
            Table_Preferred_term_assured_policyTerm_annualisedPremium_sumAssured
                    .addCell(cell_Preferred_term_assured_PremiumPayingTerm1);
            Table_Preferred_term_assured_policyTerm_annualisedPremium_sumAssured
                    .addCell(cell_Preferred_term_assured_PremiumPayingTerm2);
            //
            // document.add(Table_Preferred_term_assured_policyTerm_annualisedPremium_sumAssured);
            document.add(para_img_logo_after_space_1);

            // PdfPTable BI_Pdftable_totalPremiumforBaseProduct = new
            // PdfPTable(1);
            // BI_Pdftable_totalPremiumforBaseProduct.setWidthPercentage(100);
            // PdfPCell BI_Pdftable_totalPremiumforBaseProductcell = new
            // PdfPCell(
            // new Paragraph(
            // "Total Premium for Base Product & Rider (if any) (in Rs )",
            // small_bold));
            //
            // BI_Pdftable_totalPremiumforBaseProductcell
            // .setHorizontalAlignment(Element.ALIGN_CENTER);
            // BI_Pdftable_totalPremiumforBaseProductcell.setPadding(5);
            //
            // BI_Pdftable_totalPremiumforBaseProduct
            // .addCell(BI_Pdftable_totalPremiumforBaseProductcell);
            // document.add(BI_Pdftable_totalPremiumforBaseProduct);
            //
            // PdfPTable
            // Table_AccidetnalAndParmantRider_BasicPremium_servicePremium_yearlyPremium
            // = new PdfPTable(
            // 4);
            // Table_AccidetnalAndParmantRider_BasicPremium_servicePremium_yearlyPremium
            // .setWidthPercentage(100);
            //
            // PdfPCell cell_AccidetnalAndParmantRider_BasicPremium1 = new
            // PdfPCell(
            // new Paragraph(premium_paying_frequency
            // + " Installment Base Premium", small_normal));
            // cell_AccidetnalAndParmantRider_BasicPremium1.setPadding(5);
            // PdfPCell cell_AccidetnalAndParmantRider_BasicPremium2 = new
            // PdfPCell(
            // new Paragraph("Rs "
            // + obj.getRound(obj.getStringWithout_E(Double
            // .valueOf(basicprem.equals("") ? "0"
            // : basicprem))), small_bold));
            // cell_AccidetnalAndParmantRider_BasicPremium2.setPadding(5);
            // cell_AccidetnalAndParmantRider_BasicPremium2
            // .setHorizontalAlignment(Element.ALIGN_CENTER);
            //
            // PdfPCell cell_AccidetnalAndParmantRider_ServiceTax1 = new
            // PdfPCell(
            // new Paragraph("Applicable Tax", small_normal));
            // PdfPCell cell_AccidetnalAndParmantRider_ServiceTax2 = new
            // PdfPCell(
            // new Paragraph("Rs  "
            // + obj.getRound(obj.getStringWithout_E(Double
            // .valueOf(servcTax.equals("") ? "0"
            // : servcTax))), small_bold));
            // cell_AccidetnalAndParmantRider_ServiceTax1.setPadding(5);
            // cell_AccidetnalAndParmantRider_ServiceTax2.setPadding(5);
            // cell_AccidetnalAndParmantRider_ServiceTax2
            // .setHorizontalAlignment(Element.ALIGN_CENTER);
            //
            // Table_AccidetnalAndParmantRider_BasicPremium_servicePremium_yearlyPremium
            // .addCell(cell_AccidetnalAndParmantRider_BasicPremium1);
            // Table_AccidetnalAndParmantRider_BasicPremium_servicePremium_yearlyPremium
            // .addCell(cell_AccidetnalAndParmantRider_BasicPremium2);
            //
            // Table_AccidetnalAndParmantRider_BasicPremium_servicePremium_yearlyPremium
            // .addCell(cell_AccidetnalAndParmantRider_ServiceTax1);
            // Table_AccidetnalAndParmantRider_BasicPremium_servicePremium_yearlyPremium
            // .addCell(cell_AccidetnalAndParmantRider_ServiceTax2);
            //
            // document.add(Table_AccidetnalAndParmantRider_BasicPremium_servicePremium_yearlyPremium);
            //
            // PdfPTable Table_backdating_premium_with_service_tax = new
            // PdfPTable(
            // 2);
            // Table_backdating_premium_with_service_tax.setWidthPercentage(100);
            //
            // PdfPCell cell_Backdate1 = new PdfPCell(new Paragraph(
            // "Backdating Interest", small_normal));
            // cell_Backdate1.setPadding(5);
            //
            // PdfPCell cell_Backdate2 = new PdfPCell(new Paragraph("Rs "
            // + obj.getRound(obj.getStringWithout_E(Double
            // .valueOf(BackdatingInt.equals("") ? "0"
            // : BackdatingInt))), small_bold));
            // cell_Backdate2.setPadding(5);
            // cell_Backdate2.setHorizontalAlignment(Element.ALIGN_CENTER);
            //
            // PdfPCell cell_AccidetnalAndParmantRider_YearlyPremium1 = new
            // PdfPCell(
            // new Paragraph(premium_paying_frequency
            // + "  Installment Base Premium with Applicable Tax",
            // small_normal));
            // PdfPCell cell_AccidetnalAndParmantRider_YearlyPremium2 = new
            // PdfPCell(
            // new Paragraph("Rs  "
            // + obj.getRound(obj.getStringWithout_E(Double
            // .valueOf(premWthST.equals("") ? "0"
            // : premWthST))), small_bold));
            // cell_AccidetnalAndParmantRider_YearlyPremium1.setPadding(5);
            // cell_AccidetnalAndParmantRider_YearlyPremium2.setPadding(5);
            // cell_AccidetnalAndParmantRider_YearlyPremium2
            // .setHorizontalAlignment(Element.ALIGN_CENTER);
            //
            // Table_backdating_premium_with_service_tax.addCell(cell_Backdate1);
            // Table_backdating_premium_with_service_tax.addCell(cell_Backdate2);
            //
            // Table_backdating_premium_with_service_tax
            // .addCell(cell_AccidetnalAndParmantRider_YearlyPremium1);
            // Table_backdating_premium_with_service_tax
            // .addCell(cell_AccidetnalAndParmantRider_YearlyPremium2);
            // document.add(Table_backdating_premium_with_service_tax);

            document.add(para_img_logo_after_space_1);

            PdfPTable FY_SY_premDetail_table = new PdfPTable(4);
            FY_SY_premDetail_table.setWidths(new float[]{5f, 5f, 5f, 5f});
            FY_SY_premDetail_table.setWidthPercentage(100f);
            FY_SY_premDetail_table.setHorizontalAlignment(Element.ALIGN_LEFT);

            PdfPCell cell;

            // 1st row
            cell = new PdfPCell(new Phrase(
                    "Premium Summary ", small_bold));
            cell.setColspan(7);
            cell.setPadding(5);
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.BOTTOM | Rectangle.LEFT | Rectangle.RIGHT
                    | Rectangle.TOP);
            FY_SY_premDetail_table.addCell(cell);

            // 2 row
            cell = new PdfPCell(new Phrase("", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setPadding(5);
            FY_SY_premDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase(" Base Plan ", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setPadding(5);
            FY_SY_premDetail_table.addCell(cell);

            // cell = new PdfPCell(new Phrase("(a)Basic GST(Rs.)",
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

            cell = new PdfPCell(new Phrase("Riders", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setPadding(5);
            FY_SY_premDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase(" Total Installment Premium ", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setPadding(5);
            FY_SY_premDetail_table.addCell(cell);


            cell = new PdfPCell(new Phrase("Installment Premium without Applicable Taxes (Rs.)  ", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setPadding(5);
            FY_SY_premDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                    .parseDouble(totInstPrem_exclST)), small_bold));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setPadding(5);
            FY_SY_premDetail_table.addCell(cell);


            cell = new PdfPCell(new Phrase("Not Applicable", small_bold));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setPadding(5);
            FY_SY_premDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                    .parseDouble(totInstPrem_exclST)), small_bold));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setPadding(5);
            FY_SY_premDetail_table.addCell(cell);


            // 3 row
            cell = new PdfPCell(new Phrase("Installment Premium with 1st Year Applicable Taxes (Rs.) ", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setPadding(5);
            FY_SY_premDetail_table.addCell(cell);

           /* cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                    .parseDouble(totInstPrem_exclST)), small_bold));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setPadding(5);
            FY_SY_premDetail_table.addCell(cell);*/
            cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                    .parseDouble(premWthST)), small_bold));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setPadding(5);
            FY_SY_premDetail_table.addCell(cell);

            // cell = new PdfPCell(new Phrase(
            // CommonForAllProd.getRound(CommonForAllProd
            // .getStringWithout_E(Double.valueOf(basicServiceTax
            // .equals("") ? "0" : basicServiceTax))),
            // small_normal));
            // cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            // FY_SY_premDetail_table.addCell(cell);
            //
            // cell = new PdfPCell(new Phrase(
            // CommonForAllProd.getRound(CommonForAllProd
            // .getStringWithout_E(Double.valueOf(SBCServiceTax
            // .equals("") ? "0" : SBCServiceTax))),
            // small_normal));
            // cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            // FY_SY_premDetail_table.addCell(cell);
            //
            // cell = new PdfPCell(new Phrase(
            // CommonForAllProd.getRound(CommonForAllProd
            // .getStringWithout_E(Double.valueOf(KKCServiceTax
            // .equals("") ? "0" : KKCServiceTax))),
            // small_normal));
            // cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            // FY_SY_premDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase("Not Applicable", small_bold));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setPadding(5);
            FY_SY_premDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                    .parseDouble(premWthST)), small_bold));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setPadding(5);
            FY_SY_premDetail_table.addCell(cell);

            // 4 row
            if (!sspBean.getPlanType().equalsIgnoreCase("Single")) {
                cell = new PdfPCell(new Phrase("Installment Premium with Applicable Taxes 2nd Year onwards(Rs.)",
                        small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setPadding(5);
                FY_SY_premDetail_table.addCell(cell);

                cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                        .parseDouble(premWthSTSecondYear)), small_bold));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setPadding(5);
                FY_SY_premDetail_table.addCell(cell);

                cell = new PdfPCell(new Phrase("Not Applicable"
                        , small_bold));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setPadding(5);
                FY_SY_premDetail_table.addCell(cell);

                cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                        .parseDouble(premWthSTSecondYear)), small_bold));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setPadding(5);
                FY_SY_premDetail_table.addCell(cell);
            } else if (sspBean.getPlanType().equalsIgnoreCase("Single")) {
                cell = new PdfPCell(new Phrase("Installment Premium with Applicable Taxes 2nd Year onwards (Rs.)",
                        small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setPadding(5);
                FY_SY_premDetail_table.addCell(cell);

                cell = new PdfPCell(new Phrase("Not Applicable", small_bold));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setPadding(5);
                FY_SY_premDetail_table.addCell(cell);

                cell = new PdfPCell(new Phrase(""
                        , small_bold));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setPadding(5);
                FY_SY_premDetail_table.addCell(cell);

                cell = new PdfPCell(new Phrase("Not Applicable", small_bold));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setPadding(5);
                FY_SY_premDetail_table.addCell(cell);
            }

            /* Added By - Priyanka Warekar 26-08-2015 - End *****/

            /* Added By - Priyanka Warekar - 26-08-2015 - Start ****/
            document.add(FY_SY_premDetail_table);
            /* Added By - Priyanka Warekar - 26-08-2015 - End ****/

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
            PdfPCell BI_Pdftable6_cell6 = new PdfPCell(
                    new Paragraph(
                            "1. The premiums can be also paid by giving standing instruction to your bank or you can pay through your credit card.  ",
                            small_normal));

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

            PdfPTable taxes_desc = new PdfPTable(1);
            taxes_desc.setWidthPercentage(100);
            PdfPCell taxes_desc_cell = new PdfPCell(
                    new Paragraph(
                            "3. Applicable Taxes (including surcharge/cess etc), at the rate notified by the Central Government/ State Government / Union Territories of India from time to time and as per the provisions of the prevalent tax laws will be payable on premium/ or any other charges as per the product features. ",

                            small_normal));

            taxes_desc_cell.setPadding(5);

            taxes_desc.addCell(taxes_desc_cell);
            //document.add(taxes_desc);


            document.add(para_img_logo_after_space_1);

            document.add(para_img_logo_after_space_1);
            PdfPTable BI_Pdftable19 = new PdfPTable(1);
            BI_Pdftable19.setWidthPercentage(100);
            PdfPCell BI_Pdftable19_cell1 = new PdfPCell(new Paragraph(
                    "BENEFIT ILLUSTRATION FOR SBI LIFE - Smart Swadhan Plus",
                    small_bold));
            BI_Pdftable19_cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_Pdftable19_cell1.setPadding(5);

            BI_Pdftable19.addCell(BI_Pdftable19_cell1);
            document.add(BI_Pdftable19);

            PdfPTable Table_BI_Header = new PdfPTable(8);
            Table_BI_Header.setWidthPercentage(100);

            PdfPCell cellAmountInRupees = new PdfPCell(new Paragraph(
                    "Amount in Rupees", small_bold2));
            cellAmountInRupees.setPadding(5);
            cellAmountInRupees.setHorizontalAlignment(Element.ALIGN_CENTER);
            cellAmountInRupees.setColspan(2);

            PdfPCell cell_EndOfYear = new PdfPCell(new Paragraph("policy Year",
                    small_bold2));
            cell_EndOfYear.setPadding(5);
            cell_EndOfYear.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell_EndOfYear.setRowspan(2);

            String str_ann_prem = "";
            if (sspBean.getPlanType().equalsIgnoreCase("Single")) {
                str_ann_prem = "Single Premium";
            } else {
                str_ann_prem = " Annualized premium";
            }

            PdfPCell cell_YearlyPremiumPaid = new PdfPCell(new Paragraph(
                    str_ann_prem, small_bold2));
            cell_YearlyPremiumPaid.setPadding(5);
            cell_YearlyPremiumPaid.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell_YearlyPremiumPaid.setRowspan(2);

            PdfPCell cell_YearlyPremiumPaid2 = new PdfPCell(new Paragraph(
                    "Guaranteed", small_bold2));
            cell_YearlyPremiumPaid2.setPadding(5);
            cell_YearlyPremiumPaid2.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell_YearlyPremiumPaid2.setColspan(5);
            PdfPCell cell_YearlyPremiumPaid3 = new PdfPCell(new Paragraph(
                    "Non- Guaranteed", small_bold2));
            cell_YearlyPremiumPaid3.setPadding(5);
            cell_YearlyPremiumPaid3.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell_YearlyPremiumPaid3.setColspan(1);


            PdfPCell cell_CummulativePremiumPaid = new PdfPCell(new Paragraph(
                    "Survival Benefits / Loyalty Additions", small_bold2));
            cell_CummulativePremiumPaid.setPadding(5);
            cell_CummulativePremiumPaid
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            cell_CummulativePremiumPaid.setRowspan(2);
            PdfPCell cell_CummulativePremiumPaid2 = new PdfPCell(new Paragraph(
                    "Other Benefits, if any", small_bold2));
            cell_CummulativePremiumPaid2.setPadding(5);
            cell_CummulativePremiumPaid2
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            cell_CummulativePremiumPaid2.setRowspan(2);
            PdfPCell cell_GuarantedDeathBenefit = new PdfPCell(new Paragraph(
                    "Death benefit", small_bold2));

            cell_GuarantedDeathBenefit.setPadding(5);
            cell_GuarantedDeathBenefit
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            cell_GuarantedDeathBenefit.setRowspan(2);

            PdfPCell cell_GuarantedMaturityBenefit = new PdfPCell(
                    new Paragraph("Maturity Benefit",
                            small_bold2));

            cell_GuarantedMaturityBenefit.setPadding(5);
            cell_GuarantedMaturityBenefit
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            cell_GuarantedMaturityBenefit.setRowspan(2);
            PdfPCell cell_GuarantedSurrenderValue = new PdfPCell(new Paragraph(
                    "Minimum Guaranteed Surrender Value", small_bold2));

            cell_GuarantedSurrenderValue.setPadding(5);
            cell_GuarantedSurrenderValue
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            cell_GuarantedSurrenderValue.setRowspan(2);
            PdfPCell cell_GuarantedSurrenderValue2 = new PdfPCell(new Paragraph(
                    "Special Surrender Value", small_bold2));

            cell_GuarantedSurrenderValue2.setPadding(5);
            cell_GuarantedSurrenderValue2
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            cell_GuarantedSurrenderValue2.setRowspan(2);

            Table_BI_Header.addCell(cellAmountInRupees);
            Table_BI_Header.addCell(cell_YearlyPremiumPaid2);
            Table_BI_Header.addCell(cell_YearlyPremiumPaid3);

            Table_BI_Header.addCell(cell_EndOfYear);
            Table_BI_Header.addCell(cell_YearlyPremiumPaid);

            Table_BI_Header.addCell(cell_CummulativePremiumPaid);
            Table_BI_Header.addCell(cell_CummulativePremiumPaid2);
            Table_BI_Header.addCell(cell_GuarantedMaturityBenefit);
            Table_BI_Header.addCell(cell_GuarantedDeathBenefit);
            Table_BI_Header.addCell(cell_GuarantedSurrenderValue);
            Table_BI_Header.addCell(cell_GuarantedSurrenderValue2);
            document.add(Table_BI_Header);

            float[] columnWidthsBI_Header1 = {1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f};
            // for (int i = 0; i < list_data.size(); i++) {
            for (int i = 0; i < sspBean.getPolicyTerm(); i++) {

                PdfPTable Table_BI_Header2 = new PdfPTable(8);

                Table_BI_Header2.setWidthPercentage(100);
                Table_BI_Header2.setWidths(columnWidthsBI_Header1);
                // PdfPCell cell_EndOfYear3 = new PdfPCell(new
                // Paragraph(list_data
                // .get(i).getPolicy_year(), small_bold2));
                PdfPCell cell_EndOfYear3 = new PdfPCell(new Phrase(
                        prsObj.parseXmlTag(output, "policyYr" + (i + 1)),
                        small_bold2));
                cell_EndOfYear3.setPadding(5);
                cell_EndOfYear3.setHorizontalAlignment(Element.ALIGN_CENTER);

                PdfPCell cell_AnnPrem = new PdfPCell(new Phrase(
                        currencyFormat.format(Double.parseDouble(prsObj
                                .parseXmlTag(output, "AnnPrem" + (i + 1)))),
                        small_bold2));
                cell_AnnPrem.setPadding(5);
                cell_AnnPrem.setHorizontalAlignment(Element.ALIGN_CENTER);

                PdfPCell cell_cummulativePremiumPaid = new PdfPCell(new Phrase(
                        currencyFormat.format(Double.parseDouble(prsObj
                                .parseXmlTag(output, "SurvivalBenefits"
                                        + (i + 1)))), small_bold2));
                cell_cummulativePremiumPaid.setPadding(5);
                cell_cummulativePremiumPaid
                        .setHorizontalAlignment(Element.ALIGN_CENTER);

                PdfPCell cell_cummulativePremiumPaid2 = new PdfPCell(new Phrase(
                        currencyFormat.format(Double.parseDouble(prsObj
                                .parseXmlTag(output, "OtherBenefitsifAny"
                                        + (i + 1)))), small_bold2));
                cell_cummulativePremiumPaid2.setPadding(5);
                cell_cummulativePremiumPaid2
                        .setHorizontalAlignment(Element.ALIGN_CENTER);

                PdfPCell cell_guarantedDeathBenefit = new PdfPCell(new Phrase(
                        currencyFormat.format(Double.parseDouble(prsObj
                                .parseXmlTag(output, "guarantedDeathBenefit"
                                        + (i + 1)))), small_bold2));
                cell_guarantedDeathBenefit.setPadding(5);
                cell_guarantedDeathBenefit
                        .setHorizontalAlignment(Element.ALIGN_CENTER);

                PdfPCell cell_BenefitPayableAtMaturity = new PdfPCell(
                        new Phrase(currencyFormat.format(Double
                                .parseDouble(prsObj.parseXmlTag(output,
                                        "benefitPaybleAtMaturity" + (i + 1)))),
                                small_bold2));
                cell_BenefitPayableAtMaturity.setPadding(5);
                cell_BenefitPayableAtMaturity
                        .setHorizontalAlignment(Element.ALIGN_CENTER);

                PdfPCell cell_guarantedSurrenderValue = new PdfPCell(
                        new Phrase(currencyFormat.format(Double
                                .parseDouble(prsObj.parseXmlTag(output,
                                        "guaranSurrenderValue" + (i + 1)))),
                                small_bold2));
                cell_guarantedSurrenderValue.setPadding(5);
                cell_guarantedSurrenderValue
                        .setHorizontalAlignment(Element.ALIGN_CENTER);

                PdfPCell cell_guarantedSurrenderValue2 = new PdfPCell(
                        new Phrase(currencyFormat.format(Double
                                .parseDouble(prsObj.parseXmlTag(output,
                                        "nonGuaranSurrenderValue" + (i + 1)))),
                                small_bold2));
                cell_guarantedSurrenderValue2.setPadding(5);
                cell_guarantedSurrenderValue2
                        .setHorizontalAlignment(Element.ALIGN_CENTER);

                Table_BI_Header2.addCell(cell_EndOfYear3);
                Table_BI_Header2.addCell(cell_AnnPrem);
                Table_BI_Header2.addCell(cell_cummulativePremiumPaid);
                Table_BI_Header2.addCell(cell_cummulativePremiumPaid2);
                Table_BI_Header2.addCell(cell_BenefitPayableAtMaturity);
                Table_BI_Header2.addCell(cell_guarantedDeathBenefit);
                Table_BI_Header2.addCell(cell_guarantedSurrenderValue);
                Table_BI_Header2.addCell(cell_guarantedSurrenderValue2);

                document.add(Table_BI_Header2);
            }

            // Paragraph para_note = new Paragraph(
            // "Benefit Payable to the nominee on death Higher of i) Sum Assured on death# + Vested Simple Reversionary Bonuses + Terminal bonus, if any. OR ii) 105% of all the basic premiums paid.",
            // small_normal);
            // document.add(para_note);
            // document.add(para_img_logo_after_space_1);
            //
            // Paragraph para_EndowmentOption = new Paragraph(
            // "#For details on Sum Assured on death please refer the Sales Brochure",
            // small_normal);
            // document.add(para_EndowmentOption);
            //
            document.add(para_img_logo_after_space_1);


            PdfPTable BI_Pdftable_note2 = new PdfPTable(1);
            BI_Pdftable_note2.setWidthPercentage(100);
            PdfPCell BI_Pdftable_note_cell12 = new PdfPCell(new Paragraph(
                    " Notes", small_bold));
            BI_Pdftable_note_cell12.setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_Pdftable_note_cell12.setPadding(5);

            BI_Pdftable_note2.addCell(BI_Pdftable_note_cell12);
            document.add(BI_Pdftable_note2);

            PdfPTable BI_Pdftable62 = new PdfPTable(1);
            BI_Pdftable62.setWidthPercentage(100);
            PdfPCell BI_Pdftable6_cell62 = new PdfPCell(
                    new Paragraph(
                            "1. Annualized premium shall be the premium amount payable in a year chosen by the policyholder, excluding the taxes,  underwriting extra premiums and loading for modal premiums, if any / Single premium shall be the premium amount payable in lumpsum at  inception of the policy as chosen by the policyholder, excluding the taxes and underwriting extra premiums, if any. Refer sales literature for explanation of terms used in this illustration.",
                            small_normal));

            BI_Pdftable6_cell62.setPadding(5);

            BI_Pdftable62.addCell(BI_Pdftable6_cell62);
            document.add(BI_Pdftable62);

            PdfPTable BI_Pdftable624 = new PdfPTable(1);
            BI_Pdftable624.setWidthPercentage(100);
            PdfPCell BI_Pdftable6_cell624 = new PdfPCell(
                    new Paragraph(
                            "2. All Benefit amount are derived on the assumption that the policies are 'in-force'",
                            small_normal));

            BI_Pdftable6_cell624.setPadding(5);

            BI_Pdftable624.addCell(BI_Pdftable6_cell624);
            document.add(BI_Pdftable624);

            PdfPTable BI_Pdftable625 = new PdfPTable(1);
            BI_Pdftable625.setWidthPercentage(100);
            PdfPCell BI_Pdftable6_cell625 = new PdfPCell(
                    new Paragraph(
                            "3. The benefit calculation is based on the age herein indicated and as applicable for a healthy individual.  ",
                            small_normal));

            BI_Pdftable6_cell625.setPadding(5);

            BI_Pdftable625.addCell(BI_Pdftable6_cell625);
            //  document.add(BI_Pdftable625);

            PdfPTable BI_Pdftable626 = new PdfPTable(1);
            BI_Pdftable626.setWidthPercentage(100);
            PdfPCell BI_Pdftable6_cell626 = new PdfPCell(
                    new Paragraph(
                            "4. The Guaranteed Surrender Value (GSV) will be equal to GSV factors multiplied by the total premiums paid.   ",
                            small_normal));

            BI_Pdftable6_cell626.setPadding(5);

            BI_Pdftable626.addCell(BI_Pdftable6_cell626);
            //document.add(BI_Pdftable626);

            PdfPTable BI_Pdftable628 = new PdfPTable(1);
            BI_Pdftable628.setWidthPercentage(100);
            PdfPCell BI_Pdftable6_cell628 = new PdfPCell(
                    new Paragraph(
                            "5. In practice, the company may pay a surrender value which could be higher than the guaranteed surrender value.  The surrender value payable may be reviewed from time to time depending on company's experience of the various factors which impact the surrender values that may be paid. The surrender value would be higher of GSV or SSV.",
                            small_normal));

            BI_Pdftable6_cell628.setPadding(5);

            BI_Pdftable628.addCell(BI_Pdftable6_cell628);
            //document.add(BI_Pdftable628);

            PdfPTable BI_Pdftable629 = new PdfPTable(1);
            BI_Pdftable629.setWidthPercentage(100);
            PdfPCell BI_Pdftable6_cell629 = new PdfPCell(
                    new Paragraph(
                            "6. For further information on the product and its benefits, please refer to the sales brochure and/or policy document. Further information will also be available on request.  ",
                            small_normal));

            BI_Pdftable6_cell629.setPadding(5);

            BI_Pdftable629.addCell(BI_Pdftable6_cell629);
            //document.add(BI_Pdftable629);


            PdfPTable BI_Pdftable_note23 = new PdfPTable(1);
            BI_Pdftable_note23.setWidthPercentage(100);
            PdfPCell BI_Pdftable_note_cell123 = new PdfPCell(new Paragraph(
                    " Important: ", small_bold));
            BI_Pdftable_note_cell123.setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_Pdftable_note_cell123.setPadding(5);

            BI_Pdftable_note23.addCell(BI_Pdftable_note_cell123);
            document.add(BI_Pdftable_note23);

            PdfPTable BI_Pdftable_CompanysPolicySurrender1 = new PdfPTable(1);
            BI_Pdftable_CompanysPolicySurrender1.setWidthPercentage(100);


            // ///
            PdfPCell BI_Pdftable_CompanysPolicySurrender2_cell = new PdfPCell(
                    new Paragraph(
                            "You may receive a welcome call from our representative to confirm your proposal details like Date of Birth,Nominee Name,Address,Email Id,Sum Assured,Premium amount,Premium Payment Term etc.",
                            small_normal));

            BI_Pdftable_CompanysPolicySurrender2_cell.setPadding(5);

            BI_Pdftable_CompanysPolicySurrender1
                    .addCell(BI_Pdftable_CompanysPolicySurrender2_cell);

            PdfPCell BI_Pdftable_CompanysPolicySurrender3_cell = new PdfPCell(
                    new Paragraph(
                            "You may have to undergo Medical tests based on our underwriting requirements.",
                            small_normal));

            BI_Pdftable_CompanysPolicySurrender3_cell.setPadding(5);

            BI_Pdftable_CompanysPolicySurrender1
                    .addCell(BI_Pdftable_CompanysPolicySurrender3_cell);

            PdfPCell BI_Pdftable_CompanysPolicySurrender1_cell = new PdfPCell(
                    new Paragraph(
                            "You have to submit Proof of source of Fund",
                            small_normal));

            BI_Pdftable_CompanysPolicySurrender1_cell.setPadding(5);

            //  BI_Pdftable_CompanysPolicySurrender1.addCell(BI_Pdftable_CompanysPolicySurrender1_cell);



		/*	PdfPCell BI_Pdftable_CompanysPolicySurrender4_cell = new PdfPCell(
					new Paragraph(
							"Your SBI LIFE - Smart Swadhan Plus(UIN: 111N104V02) is a Regular/Limited premium policy,for which your first year "
									+ premium_paying_frequency
									+ " premium is Rs. "
									+ CommonForAllProd.getRound(CommonForAllProd
											.getStringWithout_E(Double
													.valueOf(premWthST
															.equals("") ? "0"
															: premWthST)))
									+ " Your policy Term is "
									+ policy_term
									+ " years,Premium Payment Term is "
									+ policy_term
									+ " years and Basic Sum Assured is Rs. "
									+ getformatedThousandString(Integer.parseInt(CommonForAllProd.getRound(CommonForAllProd.getStringWithout_E(Double
											.valueOf((sum_assured.equals("") || sum_assured == null) ? "0"
													: sum_assured))))),
							small_normal));

			BI_Pdftable_CompanysPolicySurrender4_cell.setPadding(5);

			BI_Pdftable_CompanysPolicySurrender1
					.addCell(BI_Pdftable_CompanysPolicySurrender4_cell);*/

            // ////

            document.add(BI_Pdftable_CompanysPolicySurrender1);

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
                                    + name_of_proposer
                                    + "     having received the information with respect to the above, have understood the above statement before entering into the contract.",
                            small_bold));

            BI_Pdftable26_cell1.setPadding(5);

            BI_Pdftable26.addCell(BI_Pdftable26_cell1);
            document.add(BI_Pdftable26);
            document.add(BI_Pdftable26_cell1);
            document.add(para_img_logo_after_space_1);
            document.add(para_img_logo_after_space_1);
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


                PdfPTable agentDeclarationTable = new PdfPTable(1);
                agentDeclarationTable.setWidthPercentage(100);
                PdfPCell agentDeclaration = new PdfPCell(
                        new Paragraph(commonMethods.getAgentDeclaration(context), small_bold));

                agentDeclaration.setPadding(5);
                agentDeclarationTable.addCell(agentDeclaration);
                document.add(agentDeclarationTable);


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
            Log.i("Error", e.toString() + "Error in creating pdf");
            e.printStackTrace();
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
                            imageButtonSmartSwadhanPlusProposerPhotograph
                                    .setImageBitmap(scaled);
                        } else {
                            photoBitmap = null;
                        }
                    } catch (Error error) {
                        error.printStackTrace();
                    }
                }
            }
        }
    }

    private void showsmartSwadhanPlusOutputPg(
            SmartSwadhanPlusBean smartSwadhanPlusBean) {

        bussIll = new StringBuilder();
        retVal = new StringBuilder();
        int _year_F = 0;

        int year_F = 0;
        double survival_Benefits = 0, other_Benefits_if_any = 0;
        double _annualPrem = 0, _annualPrem1 = 0, ab = 0, cummulativePremiumPaid = 0, sumcummulativePremiumPaid = 0, guarantedMatBenefit = 0, guaranSurrenderValue = 0, guarantedDeathBenefit = 0, nonGuaranSurrenderValue = 0;

//	Added By Saurabh Jain on 08/11/2019 Start

//	Added By Saurabh Jain on 08/11/2019 End
        double basicServiceTaxSecondYear = 0, SBCServiceTaxSecondYear = 0, KKCServiceTaxSecondYear = 0;
        double kerlaServiceTaxSecondYear = 0;
        double KeralaCessServiceTaxSecondYear = 0;
        // TODO Auto-generated method stub
        // TODO Auto-generated method stub
        // Intent Coding
        TextView op = new TextView(this);
        TextView op1 = new TextView(this);
        TextView op3 = new TextView(this);
        TextView op5 = new TextView(this);
        TextView op6 = new TextView(this);

        // Create object of SaralShieldBussinesLogic class
        SmartSwadhanPlusBusinessLogic smartSwadhanPlusBusinessLogic = new SmartSwadhanPlusBusinessLogic(
                smartSwadhanPlusBean);
        SmartSwadhanPlusProperties prop = new SmartSwadhanPlusProperties();
        CommonForAllProd commonForAllProd = new CommonForAllProd();
        // Display Result Data common to all plans
        try {
            /* totInstPrem_exclST = commonForAllProd.getRoundUp(commonForAllProd
					.getRoundOffLevel2(commonForAllProd
							.getStringWithout_E(smartSwadhanPlusBusinessLogic
                                    .getPremiumWithoutST())));*/
            totInstPrem_exclST = commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(smartSwadhanPlusBusinessLogic.getPremiumWithoutST()));


            System.out.println("totInstPrem_exclST " + totInstPrem_exclST);

            /* modified by Akshaya on 20-MAY-16 start **/

			/*double basicServiceTax = smartSwadhanPlusBusinessLogic
					.getServiceTax(Double.parseDouble(totInstPrem_exclST),
							smartSwadhanPlusBean.getJkResident(), "basic");
			double SBCServiceTax = smartSwadhanPlusBusinessLogic.getServiceTax(
					Double.parseDouble(totInstPrem_exclST),
					smartSwadhanPlusBean.getJkResident(), "SBC");
			double KKCServiceTax = smartSwadhanPlusBusinessLogic.getServiceTax(
					Double.parseDouble(totInstPrem_exclST),
					smartSwadhanPlusBean.getJkResident(), "KKC");*/

            double basicServiceTax = 0;
            double SBCServiceTax = 0;
            double KKCServiceTax = 0;

            double kerlaServiceTax = 0;
            double KeralaCessServiceTax = 0;
            boolean isKerlaDiscount = smartSwadhanPlusBean.isKerlaDisc();

            if (isKerlaDiscount) {
                basicServiceTax = smartSwadhanPlusBusinessLogic.getServiceTax(Double.parseDouble(totInstPrem_exclST), smartSwadhanPlusBean.getJKResidentDisc(), "basic");
                kerlaServiceTax = smartSwadhanPlusBusinessLogic.getServiceTax(Double.parseDouble(totInstPrem_exclST), smartSwadhanPlusBean.getJKResidentDisc(), "KERALA");
                KeralaCessServiceTax = kerlaServiceTax - basicServiceTax;
            } else {
                basicServiceTax = smartSwadhanPlusBusinessLogic.getServiceTax(Double.parseDouble(totInstPrem_exclST), smartSwadhanPlusBean.getJKResidentDisc(), "basic");
                SBCServiceTax = smartSwadhanPlusBusinessLogic.getServiceTax(Double.parseDouble(totInstPrem_exclST), smartSwadhanPlusBean.getJKResidentDisc(), "SBC");
                KKCServiceTax = smartSwadhanPlusBusinessLogic.getServiceTax(Double.parseDouble(totInstPrem_exclST), smartSwadhanPlusBean.getJKResidentDisc(), "KKC");
            }


            FYServiceTax = commonForAllProd.getStringWithout_E(basicServiceTax + SBCServiceTax + KKCServiceTax + kerlaServiceTax);

            //  Added By Saurabh Jain on 15/05/2019 End

            String FYtotInstPrem_inclST = commonForAllProd.getStringWithout_E(Double
                    .parseDouble(totInstPrem_exclST)
                    + Double.parseDouble(FYServiceTax));
            // System.out.println("FYtotInstPrem_inclST " +
            // FYtotInstPrem_inclST);

            if (smartSwadhanPlusBean.getPlanType().equals("Single")) {
                SYServiceTax = "0";
            } else if (isKerlaDiscount) {


                basicServiceTaxSecondYear = smartSwadhanPlusBusinessLogic.getServiceTaxSecondYear(Double.parseDouble(totInstPrem_exclST), smartSwadhanPlusBean.getJKResidentDisc(), "basic");
                kerlaServiceTaxSecondYear = smartSwadhanPlusBusinessLogic.getServiceTaxSecondYear(Double.parseDouble(totInstPrem_exclST), smartSwadhanPlusBean.getJKResidentDisc(), "KERALA");
                KeralaCessServiceTaxSecondYear = kerlaServiceTaxSecondYear - basicServiceTaxSecondYear;

                SYServiceTax = commonForAllProd.getStringWithout_E(basicServiceTaxSecondYear + kerlaServiceTaxSecondYear);

            } else {
                basicServiceTaxSecondYear = smartSwadhanPlusBusinessLogic.getServiceTaxSecondYear(Double.parseDouble(totInstPrem_exclST), smartSwadhanPlusBean.getJKResidentDisc(), "basic");
                SBCServiceTaxSecondYear = smartSwadhanPlusBusinessLogic.getServiceTaxSecondYear(Double.parseDouble(totInstPrem_exclST), smartSwadhanPlusBean.getJKResidentDisc(), "SBC");
                KKCServiceTaxSecondYear = smartSwadhanPlusBusinessLogic.getServiceTaxSecondYear(Double.parseDouble(totInstPrem_exclST), smartSwadhanPlusBean.getJKResidentDisc(), "KKC");

                SYServiceTax = commonForAllProd.getStringWithout_E(basicServiceTaxSecondYear + SBCServiceTaxSecondYear + KKCServiceTaxSecondYear);
            }
            //  Added By Saurabh Jain on 15/05/2019 End
            if (smartSwadhanPlusBean.getPlanType().equals("Single")) {
                SYtotInstPrem_inclST = "" + 0;
            } else {
                SYtotInstPrem_inclST = commonForAllProd
                        .getStringWithout_E(Double
                                .parseDouble(totInstPrem_exclST)
                                + Double.parseDouble(SYServiceTax));
            }
//					System.out.println("SYtotInstPrem_inclST " + SYtotInstPrem_inclST);

            /* modified by Akshaya on 20-MAY-16 end **/

            double totalBasePremiumPaidYearly = smartSwadhanPlusBusinessLogic
                    .setTotalBasePremiumPaidYearly(Double
                            .parseDouble(totInstPrem_exclST));


            double totalBasePremiumPaidYearly1 = smartSwadhanPlusBusinessLogic
                    .setTotalBasePremiumPaidYearly1(Double
                            .parseDouble(totInstPrem_exclST));


            valPremiumError = valInstPremium(totInstPrem_exclST);

            String staffStatus = "none";
			/*if (smartSwadhanPlusBean.getStaffDisc())
				staffStatus = "sbi";
			else
				staffStatus = "none";*/

            String staffRebate = String.valueOf(smartSwadhanPlusBusinessLogic
                    .getStaffRebate());
            System.out.println("staffRebate " + staffRebate);
            /*
      added by vrushali on 10-Dec-2015
     */
            String totInstPrem_exclST_exclDisc = commonForAllProd
                    .getStringWithout_E(smartSwadhanPlusBusinessLogic
                            .getPremiumWithoutSTWithoutDisc());
            System.out.println("totInstPrem_exclST_exclDisc "
                    + totInstPrem_exclST_exclDisc);
            String totInstPrem_exclST_exclDisc_exclFreqLoading = commonForAllProd
                    .getStringWithout_E(smartSwadhanPlusBusinessLogic
                            .getPremiumWithoutSTWithoutDiscWithoutFreqLoading());
            System.out.println("totInstPrem_exclST_exclDisc_exclFreqLoading "
                    + totInstPrem_exclST_exclDisc_exclFreqLoading);

            String MinesOccuInterest = ""
                    + smartSwadhanPlusBusinessLogic
                    .getMinesOccuInterest(smartSwadhanPlusBean
                            .getSumAssured());

            String servicetax_MinesOccuInterest = ""
                    + smartSwadhanPlusBusinessLogic
                    .getServiceTaxMines(Double.parseDouble(MinesOccuInterest));

            MinesOccuInterest = "" + (Double.parseDouble(MinesOccuInterest) + Double.parseDouble(servicetax_MinesOccuInterest));

            // /** for testing **/
            // totInstPrem_exclST_exclDisc = commonForAllProd
            // .getStringWithout_E(Double
            // .parseDouble(totInstPrem_exclST_exclDisc));
            /* End **/

            String BackDateinterest, BackDateinterestwithGST;
            if (rb_smart_swadhan_plus_backdating_yes.isChecked()) {
                // "16-jan-2014")));
                BackDateinterest = commonForAllProd.getRoundUp(""
                        + (smartSwadhanPlusBusinessLogic.getBackDateInterest(
                        Double.parseDouble(FYtotInstPrem_inclST),
                        btn_smart_swadhan_plus_backdatingdate.getText()
                                .toString())));

                BackDateinterestwithGST = commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(Double.parseDouble(BackDateinterest) + (Double.parseDouble(BackDateinterest) * prop.GSTforbackdate)));
            } else {
                BackDateinterestwithGST = "0";
            }

            FYtotInstPrem_inclST = commonForAllProd.getRoundUp(commonForAllProd
                    .getStringWithout_E(Double
                            .parseDouble(FYtotInstPrem_inclST)
                            + Double.parseDouble(BackDateinterestwithGST)));

            int rowNumber = 0;
            double maxguaranSurrenderValue = 0, sumcummulativePremiumPaid1_to_10 = 0;

            for (int j = 0; j < smartSwadhanPlusBean.getPolicyTerm(); j++) {
                rowNumber++;

                year_F = rowNumber;
                _year_F = year_F;
                System.out.println("1. year_F " + year_F);
                bussIll.append("<policyYr").append(_year_F).append(">").append(_year_F).append("</policyYr").append(_year_F).append(">");

                _annualPrem = smartSwadhanPlusBusinessLogic.setAnnualPrem(
                        totalBasePremiumPaidYearly, year_F);

                _annualPrem1 = smartSwadhanPlusBusinessLogic.setAnnualPrem(
                        totalBasePremiumPaidYearly1, year_F);
                System.out.println("2.Annual Premium:  " + _annualPrem);
                bussIll.append("<AnnPrem").append(_year_F).append(">").append(commonForAllProd.getRound(commonForAllProd.getStringWithout_E(_annualPrem1))).append("</AnnPrem").append(_year_F).append(">");

                sumcummulativePremiumPaid = sumcummulativePremiumPaid
                        + _annualPrem;
                cummulativePremiumPaid = smartSwadhanPlusBusinessLogic
                        .setCummulativePremiumPaid(sumcummulativePremiumPaid,
                                year_F);
//						System.out.println("2.cummulativePremiumPaid : "
//								+ cummulativePremiumPaid);

//						Commented By saurabh Jain on 08/11/2019 Start
						/*bussIll.append("<cummulativePremiumPaid"
						+ _year_F
						+ ">"
						+ commonForAllProd
								.getStringWithout_E(cummulativePremiumPaid)
								+ "</cummulativePremiumPaid" + _year_F + ">");*/

//						Commented By saurabh Jain on 08/11/2019 End


//						Added By Saurabh Jain on 08/11/2019 Start
                bussIll.append("<SurvivalBenefits").append(_year_F).append(">").append(commonForAllProd.getStringWithout_E(survival_Benefits)).append("</SurvivalBenefits").append(_year_F).append(">");

                bussIll.append("<OtherBenefitsifAny").append(_year_F).append(">").append(commonForAllProd.getStringWithout_E(other_Benefits_if_any)).append("</OtherBenefitsifAny").append(_year_F).append(">");

//						Added By Saurabh Jain on 08/11/2019 End

                guarantedMatBenefit = smartSwadhanPlusBusinessLogic
                        .setGuaranteedMaturityBenefit(
                                sumcummulativePremiumPaid,
                                totalBasePremiumPaidYearly, year_F);
                System.out.println("4.benefit Payble At Maturity : "
                        + guarantedMatBenefit);
                bussIll.append("<benefitPaybleAtMaturity").append(_year_F).append(">").append(commonForAllProd
                        .getStringWithout_E(guarantedMatBenefit)).append("</benefitPaybleAtMaturity").append(_year_F).append(">");

                guaranSurrenderValue = smartSwadhanPlusBusinessLogic
                        .setGuaranteedSurrenderValue(sumcummulativePremiumPaid,
                                year_F);
//						System.out.println("5.benefit Payble At Maturity : "
//								+ guaranSurrenderValue);
                bussIll.append("<guaranSurrenderValue").append(_year_F).append(">"
//								+ commonForAllProd.getRoundUp(""
                ).append(commonForAllProd.getRound(""
                        + commonForAllProd
                        .getStringWithout_E(guaranSurrenderValue))).append("</guaranSurrenderValue").append(_year_F).append(">");

//						Added By Saurabh Jain on 08/11/2019 Start

                if (smartSwadhanPlusBean.getPlanType().equals("Single")) {
                    if (year_F == 1) {

                        ab = _annualPrem;
                    }

                } else if (smartSwadhanPlusBean.getPlanType().equals("Regular")) {

                    ab = (smartSwadhanPlusBean.getPolicyTerm()) * _annualPrem;

                } else {
                    if (year_F == 1) {
                        ab = (smartSwadhanPlusBean.getPremiumPayingTerm()) * _annualPrem;
                    }
                }

                nonGuaranSurrenderValue = smartSwadhanPlusBusinessLogic
                        .setNonGuaranteedSurrenderValue(year_F, ab);
//						System.out.println("6.nonGuaranSurrenderValue : "
//								+ nonGuaranSurrenderValue);
                bussIll.append("<nonGuaranSurrenderValue").append(_year_F).append(">"
//								+ commonForAllProd.getRoundUp(""
                ).append(commonForAllProd.getRound(""
                        + commonForAllProd
                        .getStringWithout_E(nonGuaranSurrenderValue))).append("</nonGuaranSurrenderValue").append(_year_F).append(">");

                if (guaranSurrenderValue > maxguaranSurrenderValue)
                    maxguaranSurrenderValue = guaranSurrenderValue;

                if (year_F <= 10)
                    sumcummulativePremiumPaid1_to_10 = sumcummulativePremiumPaid1_to_10
                            + _annualPrem;
            }

            rowNumber = 0;
            for (int k = 0; k < smartSwadhanPlusBean.getPolicyTerm(); k++) {
                rowNumber++;
                year_F = rowNumber;
                _year_F = year_F;

                guarantedDeathBenefit = smartSwadhanPlusBusinessLogic
                        .setGuaranteedDeathBenefit(year_F,
                                sumcummulativePremiumPaid1_to_10,
                                maxguaranSurrenderValue,
                                Double.parseDouble(totInstPrem_exclST),
                                totalBasePremiumPaidYearly);
                System.out.println("3.benefit Payble At Maturity : "
                        + guarantedDeathBenefit);
                bussIll.append("<guarantedDeathBenefit").append(_year_F).append(">").append(commonForAllProd.getRoundUp(""
                        + commonForAllProd
                        .getStringWithout_E(guarantedDeathBenefit))).append("</guarantedDeathBenefit").append(_year_F).append(">");
            }

            retVal.append("<?xml version='1.0' encoding='utf-8' ?><SmartSwadhanPlus>");
            retVal.append("<errCode>0</errCode>");
            retVal.append("<staffStatus>").append(staffStatus).append("</staffStatus>");
            retVal.append("<staffRebate>").append(staffRebate).append("</staffRebate>");
            retVal.append("<InstmntPrem>").append(totInstPrem_exclST_exclDisc).append("</InstmntPrem>");
            retVal.append("<basicPremWithoutDisc>").append(totInstPrem_exclST_exclDisc).append("</basicPremWithoutDisc>");
            retVal.append("<basicPremWithoutDiscSA>").append(totInstPrem_exclST_exclDisc_exclFreqLoading).append("</basicPremWithoutDiscSA>");
            retVal.append("<OccuInt>").append(MinesOccuInterest).append("</OccuInt>");
            retVal.append("<OccuIntServiceTax>").append(servicetax_MinesOccuInterest).append("</OccuIntServiceTax>");
            retVal.append("<backdateInt>").append(BackDateinterestwithGST).append("</backdateInt>");

            retVal.append("<totInstPrem_exclST>").append(totInstPrem_exclST).append("</totInstPrem_exclST>").append("<FYServiceTax>").append(FYServiceTax).append("</FYServiceTax>").append("<FYtotInstPrem_inclST>").append(FYtotInstPrem_inclST).append("</FYtotInstPrem_inclST>");

            /* modified by Akshaya on 20-MAY-16 start **/

            retVal.append("<basicServiceTax>").append(commonForAllProd.getStringWithout_E(basicServiceTax)).append("</basicServiceTax>").append("<SBCServiceTax>").append(commonForAllProd.getStringWithout_E(SBCServiceTax)).append("</SBCServiceTax>")
                    .append("<KKCServiceTax>").append(commonForAllProd.getStringWithout_E(KKCServiceTax)).append("</KKCServiceTax>").append("<KeralaCessServiceTax>").append(commonForAllProd.getStringWithout_E(KeralaCessServiceTax)).append("</KeralaCessServiceTax>");

            if (!smartSwadhanPlusBean.getPlanType().equals("Single")) {
                retVal.append("<SYServiceTax>").append(SYServiceTax).append("</SYServiceTax>").append("<SYtotInstPrem_inclST>").append(SYtotInstPrem_inclST).append("</SYtotInstPrem_inclST>");

                retVal.append("<basicServiceTaxSecondYear>").append(commonForAllProd
                        .getStringWithout_E(basicServiceTaxSecondYear)).append("</basicServiceTaxSecondYear>").append("<SBCServiceTaxSecondYear>").append(commonForAllProd
                        .getStringWithout_E(SBCServiceTaxSecondYear)).append("</SBCServiceTaxSecondYear>").append("<KKCServiceTaxSecondYear>").append(commonForAllProd
                        .getStringWithout_E(KKCServiceTaxSecondYear)).append("</KKCServiceTaxSecondYear>").append("<KeralaCessServiceTaxSecondYear>").append(commonForAllProd.getStringWithout_E(KeralaCessServiceTaxSecondYear)).append("</KeralaCessServiceTaxSecondYear>");
            } else {
                retVal.append("<syServiceTax>" + "0" + "</syServiceTax>");
                retVal.append("<syPremium>" + "0" + "</syPremium>");

                retVal.append("<basicServiceTaxSecondYear>" + "0" + "</basicServiceTaxSecondYear>" +
                        "<SBCServiceTaxSecondYear>" + "0" + "</SBCServiceTaxSecondYear>" +
                        "<KKCServiceTaxSecondYear>" + "0" + "</KKCServiceTaxSecondYear>" +
                        "<KeralaCessServiceTaxSecondYear>" + "0" + "</KeralaCessServiceTaxSecondYear>");
            }

            /* modified by Akshaya on 20-MAY-16 end **/
            retVal.append(bussIll.toString());
            retVal.append("</SmartSwadhanPlus>");

        } catch (Exception e) {
            retVal.append("<?xml version='1.0' encoding='utf-8' ?><SmartSwadhanPlus>" + "<errCode>1</errCode>" + "<errorMessage>").append(e.getMessage()).append("</errorMessage></SmartSwadhanPlus>");
        }

        System.out.println("Final output in xml" + retVal.toString());
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
        if ((number.length() != 10)) {
            edt_smart_swadhan_plus_contact_no
                    .setError("Please provide correct 10-digit mobile number");
        } else if ((number.length() == 10)) {
        }
    }

    private void email_id_validation(String email_id) {

        String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email_id);
        if (!(matcher.matches())) {
            edt_smart_swadhan_plus_Email_id
                    .setError("Please provide the correct email address");
            validationFla1 = false;
        } else if ((matcher.matches())) {
            validationFla1 = true;
        }
    }

    private void setSpinner_Value() {

        String[] distribution_channelList = {"Corporate Agents", "Brokers", "Individual Agents", "Direct Marketing"};
        ArrayAdapter<String> distribution_channelAdapter = new ArrayAdapter<>(
                getApplicationContext(), R.layout.spinner_item, distribution_channelList);
        distribution_channelAdapter.setDropDownViewResource(R.layout.spinner_item1);
        spnr_bi_smart_swadhan_plus_distribution_channel.setAdapter(distribution_channelAdapter);
        distribution_channelAdapter.notifyDataSetChanged();

        // Gender
        String[] genderList = {"Male", "Female", "Third Gender"};
        genderAdapter = new ArrayAdapter<>(getApplicationContext(),
                R.layout.spinner_item, genderList);
        genderAdapter.setDropDownViewResource(R.layout.spinner_item1);
        spnr_bi_smart_swadhan_plus_selGender.setAdapter(genderAdapter);
        genderAdapter.notifyDataSetChanged();

        commonMethods.fillSpinnerValue(context, spnr_bi_smart_swadhan_plus_life_assured_title,
                Arrays.asList(getResources().getStringArray(R.array.arr_select_title)));

        // Plan
        String[] planList = {"Single", "Regular", "LPPT"};
        ArrayAdapter<String> planAdapter = new ArrayAdapter<>(
                getApplicationContext(), R.layout.spinner_item, planList);
        planAdapter.setDropDownViewResource(R.layout.spinner_item1);
        spnr_bi_smart_swadhan_plus_plan.setAdapter(planAdapter);
        planAdapter.notifyDataSetChanged();

        // Policy Term
        String[] policyTermList = new String[21];
        for (int i = 10; i <= 30; i++) {
            policyTermList[i - 10] = i + "";
        }
        ArrayAdapter<String> policyTermAdapter = new ArrayAdapter<>(
                getApplicationContext(), R.layout.spinner_item, policyTermList);
        policyTermAdapter.setDropDownViewResource(R.layout.spinner_item1);
        spnr_bi_smart_swadhan_plus_policyterm.setAdapter(policyTermAdapter);
        policyTermAdapter.notifyDataSetChanged();

        // Premium Payment term
        String[] premPayingTermList = {"5", "10", "15"};
        ArrayAdapter<String> premPayingTermAdapter = new ArrayAdapter<>(
                getApplicationContext(), R.layout.spinner_item,
                premPayingTermList);
        premPayingTermAdapter.setDropDownViewResource(R.layout.spinner_item1);
        spnr_bi_smart_swadhan_plus_permium_payingterm
                .setAdapter(premPayingTermAdapter);
        premPayingTermAdapter.notifyDataSetChanged();

        // Premium Frequency
        String[] premFreqList = {"Yearly", "Half Yearly", "Quarterly",
                "Monthly"};
        ArrayAdapter<String> premFreqAdapter = new ArrayAdapter<>(
                getApplicationContext(), R.layout.spinner_item, premFreqList);
        premFreqAdapter.setDropDownViewResource(R.layout.spinner_item1);
        spnr_bi_smart_swadhan_plus_premium_frequency
                .setAdapter(premFreqAdapter);
        premFreqAdapter.notifyDataSetChanged();

    }


    private void valPPT() {
        String planType = spnr_bi_smart_swadhan_plus_plan.getSelectedItem()
                .toString();
        int policyTerm = Integer.parseInt(spnr_bi_smart_swadhan_plus_policyterm
                .getSelectedItem().toString());
        int PPT = Integer
                .parseInt(spnr_bi_smart_swadhan_plus_permium_payingterm
                        .getSelectedItem().toString());
        String error = "";
        if (planType.equals("LPPT")) {
            if (policyTerm >= 15 && policyTerm < 20 && PPT == 15) {
                error = "Please enter Premium Paying Term as 5 Or 10.";
            } else if (policyTerm >= 10 && policyTerm < 15 && PPT != 5) {
                error = "Please enter Premium Paying Term as 5.";
            }
        }

        if (!error.equals("")) {
            showAlert.setMessage(error);
            showAlert.setNeutralButton("OK",
                    new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
            showAlert.show();
        }

    }

    // Validate minimum premium
    private boolean valInstPremium(String basicPremium) {
        // System.out.println("premfreq"+selPremFreq.getSelectedItem().toString());
        // System.out.println("instPremium"+totalInstPremiumWithDiscWithoutST);
        if (spnr_bi_smart_swadhan_plus_premium_frequency.getSelectedItem()
                .toString().equals("Yearly")
                && (Integer.parseInt(basicPremium) < 2300)) {
            showAlert
                    .setMessage("Minimum premium for Yearly Mode under this product is Rs. 2300");
            showAlert.setNeutralButton("OK",
                    new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
            showAlert.show();

            return false;

        } else if (spnr_bi_smart_swadhan_plus_premium_frequency
                .getSelectedItem().toString().equals("Half Yearly")
                && (Integer.parseInt(basicPremium) < 1200)) {
            showAlert
                    .setMessage("Minimum premium for Half Yearly Mode under this product is Rs. 1200");
            showAlert.setNeutralButton("OK",
                    new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
            showAlert.show();

            return false;
        }
        if (spnr_bi_smart_swadhan_plus_premium_frequency.getSelectedItem()
                .toString().equals("Quarterly")
                && (Integer.parseInt(basicPremium) < 650)) {
            showAlert
                    .setMessage("Minimum premium for Quarterly Mode under this product is Rs. 650");
            showAlert.setNeutralButton("OK",
                    new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            // Toast.makeText(getApplicationContext(),
                            // "Ok button Clicked ", Toast.LENGTH_LONG).show();
                        }
                    });
            showAlert.show();
            return false;
        } else if (spnr_bi_smart_swadhan_plus_premium_frequency
                .getSelectedItem().toString().equals("Monthly")
                && (Integer.parseInt(basicPremium) < 250)) {
            showAlert
                    .setMessage("Minimum premium for Monthly Mode under this product is Rs. 250");
            showAlert.setNeutralButton("OK",
                    new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            // Toast.makeText(getApplicationContext(),
                            // "Ok button Clicked ", Toast.LENGTH_LONG).show();
                        }
                    });
            showAlert.show();

            return false;
        } else if (spnr_bi_smart_swadhan_plus_plan.getSelectedItem().toString()
                .equals("Single")
                && (Integer.parseInt(basicPremium) < 21000)) {
            showAlert
                    .setMessage("Minimum premium for Single Mode under this product is Rs. 21000");
            showAlert.setNeutralButton("OK",
                    new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            // Toast.makeText(getApplicationContext(),
                            // "Ok button Clicked ", Toast.LENGTH_LONG).show();
                        }
                    });
            showAlert.show();

            return false;
        } else {
            return true;
        }
    }

    private boolean valMaturityAge() {
        int age = 0;
        if (!(edt_bi_smart_swadhan_plus_life_assured_age.getText().toString()
                .equals(""))) {
            age = Integer.parseInt(edt_bi_smart_swadhan_plus_life_assured_age
                    .getText().toString());
        }
        int policyTerm = Integer.parseInt(spnr_bi_smart_swadhan_plus_policyterm
                .getSelectedItem().toString());
        if ((age + policyTerm) > 75) {
            showAlert.setMessage("Maximum maturity age is 75 years.");
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

    private boolean valBackdate() {
        if (proposer_Backdating_WishToBackDate_Policy.equals("y")) {

            if (proposer_Backdating_BackDate.equals("")) {
                showAlert.setMessage("Please Select Backdate ");
                showAlert.setNeutralButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // apply focusable method
                                setFocusable(btn_smart_swadhan_plus_backdatingdate);
                                btn_smart_swadhan_plus_backdatingdate
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

            if (rb_smart_swadhan_plus_backdating_yes.isChecked()) {

                final Calendar c = Calendar.getInstance();
                final int currYear = c.get(Calendar.YEAR);
                final int currMonth = c.get(Calendar.MONTH) + 1;

                SimpleDateFormat dateformat1 = new SimpleDateFormat(
                        "dd-MM-yyyy", Locale.ENGLISH);
                Date dtBackDate = dateformat1
                        .parse(btn_smart_swadhan_plus_backdatingdate.getText()
                                .toString());
                Date currentDate = c.getTime();

                Date finYerEndDate = null;

                //  Date launchDate = dateformat1.parse("17-08-2015");
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

    }

    // Validate policy term
    private boolean valSumAssured() {
        StringBuilder error = new StringBuilder();

        String sumaAssured = edt_bi_smart_swadhan_plus_sum_assured_amount
                .getText().toString();
        if (TextUtils.isEmpty(sumaAssured)) {
            error.append("Please enter Sum Assured");
        } else if (Integer.parseInt(sumaAssured) < 500000) {
            error.append("Minimum Sum Assured is 500000.");
        } else if ((Integer
                .parseInt(edt_bi_smart_swadhan_plus_sum_assured_amount
                        .getText().toString()) % 1000) != 0) {
            error.append("Sum Assured should be multiple of 1000.");
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
                                    setFocusable(spnr_bi_smart_swadhan_plus_life_assured_title);
                                    spnr_bi_smart_swadhan_plus_life_assured_title
                                            .requestFocus();
                                } else if (lifeAssured_First_Name.equals("")) {
                                    setFocusable(edt_bi_smart_swadhan_plus_life_assured_first_name);
                                    edt_bi_smart_swadhan_plus_life_assured_first_name
                                            .requestFocus();
                                } else {
                                    setFocusable(edt_bi_smart_swadhan_plus_life_assured_last_name);
                                    edt_bi_smart_swadhan_plus_life_assured_last_name
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
                                setFocusable(spnr_bi_smart_swadhan_plus_life_assured_title);
                                spnr_bi_smart_swadhan_plus_life_assured_title
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
                                setFocusable(spnr_bi_smart_swadhan_plus_life_assured_title);
                                spnr_bi_smart_swadhan_plus_life_assured_title
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
                                setFocusable(spnr_bi_smart_swadhan_plus_life_assured_title);
                                spnr_bi_smart_swadhan_plus_life_assured_title
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
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    if (lifeAssured_Title.equals("")) {
                                        // apply focusable method
                                        setFocusable(spnr_bi_smart_swadhan_plus_life_assured_title);
                                        spnr_bi_smart_swadhan_plus_life_assured_title
                                                .requestFocus();
                                    } else if (lifeAssured_First_Name.equals("")) {

                                        edt_bi_smart_swadhan_plus_life_assured_first_name
                                                .requestFocus();
                                    } else {
                                        edt_bi_smart_swadhan_plus_life_assured_last_name
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
                                    setFocusable(spnr_bi_smart_swadhan_plus_life_assured_title);
                                    spnr_bi_smart_swadhan_plus_life_assured_title
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
                                    setFocusable(spnr_bi_smart_swadhan_plus_life_assured_title);
                                    spnr_bi_smart_swadhan_plus_life_assured_title
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
                                    setFocusable(spnr_bi_smart_swadhan_plus_life_assured_title);
                                    spnr_bi_smart_swadhan_plus_life_assured_title
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
                            setFocusable(btn_bi_smart_swadhan_plus_life_assured_date);
                            btn_bi_smart_swadhan_plus_life_assured_date
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

    private boolean valBasicDetail() {

        if (edt_smart_swadhan_plus_contact_no.getText().toString().equals("")) {
            commonMethods.dialogWarning(context, "Please Fill  Mobile No", true);
            edt_smart_swadhan_plus_contact_no.requestFocus();
            return false;
        } else if (edt_smart_swadhan_plus_contact_no.getText().toString()
                .length() != 10) {
            commonMethods.dialogWarning(context, "Please Fill 10 Digit Mobile No", true);
            edt_smart_swadhan_plus_contact_no.requestFocus();
            return false;
        }

		/*else if (emailId.equals("")) {
			commonMethods.dialogWarning(context, "Please Fill Email Id", true);
			edt_smart_swadhan_plus_Email_id.requestFocus();
			return false;

		}

		else if (ConfirmEmailId.equals("")) {

			commonMethods.dialogWarning(context, "Please Fill Confirm Email Id", true);
			edt_smart_swadhan_plus_ConfirmEmail_id.requestFocus();
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
                    edt_smart_swadhan_plus_ConfirmEmail_id.requestFocus();
                    return false;
                } else if (!ConfirmEmailId.equals(emailId)) {
                    commonMethods.dialogWarning(context, "Email Id Does Not Match", true);
                    return false;
                }

                return true;
            } else {
                commonMethods.dialogWarning(context, "Please Fill Valid Email Id", true);
                edt_smart_swadhan_plus_Email_id.requestFocus();
                return false;
            }
        } else if (!ConfirmEmailId.equals("")) {

            email_id_validation(ConfirmEmailId);
            if (validationFla1) {

                if (emailId.equals("")) {
                    commonMethods.dialogWarning(context, "Please Fill Email Id", true);
                    edt_smart_swadhan_plus_Email_id.requestFocus();
                    return false;

                }

                return true;
            } else {
                commonMethods.dialogWarning(context, "Please Fill Valid Confirm Email Id", true);
                edt_smart_swadhan_plus_ConfirmEmail_id.requestFocus();
                return false;
            }
        } else {
            return true;
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
            DateFormat userDateFormat = new SimpleDateFormat("MM-dd-yyyy", Locale.ENGLISH);
            DateFormat dateFormatNeeded = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
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
            DateFormat userDateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
            DateFormat dateFormatNeeded = new SimpleDateFormat("MM-dd-yyyy", Locale.ENGLISH);
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

    private void windowmessagesgin() {

        d = new Dialog(BI_SmartSwadhanPlusActivity.this);
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
                Intent intent = new Intent(BI_SmartSwadhanPlusActivity.this,
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

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

        boolean flagFocus = true;
        if (!flagFocus) {
            ll_bi_smart_swadhan_plus_main.requestFocus();
        } else {
            spnr_bi_smart_swadhan_plus_life_assured_title.requestFocus();
        }

    }

    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        // TODO Auto-generated method stub
        if (v.getId() == edt_bi_smart_swadhan_plus_life_assured_first_name
                .getId()) {
            setFocusable(edt_bi_smart_swadhan_plus_life_assured_middle_name);
            edt_bi_smart_swadhan_plus_life_assured_middle_name.requestFocus();
        } else if (v.getId() == edt_bi_smart_swadhan_plus_life_assured_middle_name
                .getId()) {
            setFocusable(edt_bi_smart_swadhan_plus_life_assured_last_name);
            edt_bi_smart_swadhan_plus_life_assured_last_name.requestFocus();
        } else if (v.getId() == edt_bi_smart_swadhan_plus_life_assured_last_name
                .getId()) {

            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(
                    edt_bi_smart_swadhan_plus_life_assured_last_name
                            .getWindowToken(), 0);
            // clearFocusable(edt_bi_smart_swadhan_plus_life_assured_last_name);
            setFocusable(btn_bi_smart_swadhan_plus_life_assured_date);
            btn_bi_smart_swadhan_plus_life_assured_date.requestFocus();
        } else if (v.getId() == edt_smart_swadhan_plus_contact_no.getId()) {
            // clearFocusable(premiumAmt);
            setFocusable(edt_smart_swadhan_plus_Email_id);
            edt_smart_swadhan_plus_Email_id.requestFocus();
        } else if (v.getId() == edt_smart_swadhan_plus_Email_id.getId()) {
            // clearFocusable(premiumAmt);
            setFocusable(edt_smart_swadhan_plus_ConfirmEmail_id);
            edt_smart_swadhan_plus_ConfirmEmail_id.requestFocus();
        } else if (v.getId() == edt_smart_swadhan_plus_ConfirmEmail_id.getId()) {
            // clearFocusable(premiumAmt);
            clearFocusable(spnr_bi_smart_swadhan_plus_plan);
            setFocusable(spnr_bi_smart_swadhan_plus_plan);
            spnr_bi_smart_swadhan_plus_plan.requestFocus();
        } else if (v.getId() == edt_bi_smart_swadhan_plus_sum_assured_amount
                .getId()) {

            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(
                    edt_bi_smart_swadhan_plus_sum_assured_amount
                            .getWindowToken(), 0);
        }
        return true;
    }

    private boolean valPPTOnProceed() {
        String planType = spnr_bi_smart_swadhan_plus_plan.getSelectedItem()
                .toString();
        int policyTerm = Integer.parseInt(spnr_bi_smart_swadhan_plus_policyterm
                .getSelectedItem().toString());
        int PPT = Integer
                .parseInt(spnr_bi_smart_swadhan_plus_permium_payingterm
                        .getSelectedItem().toString());
        String error = "";
        if (planType.equals("LPPT")) {
            if (policyTerm >= 15 && policyTerm < 20 && PPT == 15) {
                error = "Please enter Premium Payment Term as 5 Or 10.";
            } else if (policyTerm >= 10 && policyTerm < 15 && PPT != 5) {
                error = "Please enter Premium Payment Term as 5.";
            }
        }

        if (!error.equals("")) {

            showAlert.setMessage(error);
            showAlert.setCancelable(false);
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

    private boolean valDoYouBackdate() {
        if (!proposer_Backdating_WishToBackDate_Policy.equals("")) {
            return true;
        } else {
            showAlert.setMessage("Please Select Do you wish to Backdate ");
            showAlert.setNeutralButton("OK",
                    new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            // apply focusable method
                            setFocusable(rb_smart_swadhan_plus_backdating_yes);
                            rb_smart_swadhan_plus_backdating_yes.requestFocus();
                        }
                    });
            showAlert.show();

            return false;

        }
    }

   /* private boolean valBackdate() {
		if (proposer_Backdating_WishToBackDate_Policy.equals("y")) {

			if (proposer_Backdating_BackDate.equals("")) {
				showAlert.setMessage("Please Select Backdate ");
				showAlert.setNeutralButton("OK",
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
												int which) {
								// apply focusable method
								setFocusable(btn_smart_swadhan_plus_backdatingdate);
								btn_smart_swadhan_plus_backdatingdate
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

   /* private boolean TrueBackdate() {
		try {
			String error = "";

			if (rb_smart_swadhan_plus_backdating_yes.isChecked()) {

				final Calendar c = Calendar.getInstance();
				final int currYear = c.get(Calendar.YEAR);
				final int currMonth = c.get(Calendar.MONTH) + 1;

				SimpleDateFormat dateformat1 = new SimpleDateFormat(
						"dd-MM-yyyy");
				Date dtBackDate = dateformat1
						.parse(btn_smart_swadhan_plus_backdatingdate.getText()
								.toString());
				Date currentDate = c.getTime();

				Date finYerEndDate = null;

				Date launchDate = dateformat1.parse("17-08-2015");

				if (currMonth >= 4) {
					finYerEndDate = dateformat1.parse("1-4-" + currYear);
				} else {
					finYerEndDate = dateformat1.parse("1-4-" + (currYear - 1));
				}

				if (currentDate.before(dtBackDate)) {
					error = "Please enter backdation date between "
							+ dateformat1.format(finYerEndDate) + " and "
							+ dateformat1.format(currentDate);
				}
                *//* Added by Akshaya on 24-08-2015 start **//*
				else if (dtBackDate.before(launchDate)
						&& finYerEndDate.before(launchDate)) {
					error = "Please enter Backdation date between "
							+ dateformat1.format(launchDate) + " and "
							+ dateformat1.format(currentDate);
				}
                *//* Added by Akshaya on 24-08-2015 end **//*
				else if (dtBackDate.before(finYerEndDate)) {
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

    }*/

}
