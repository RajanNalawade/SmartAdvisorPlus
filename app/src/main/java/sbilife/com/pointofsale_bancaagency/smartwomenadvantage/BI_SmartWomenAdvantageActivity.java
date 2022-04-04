package sbilife.com.pointofsale_bancaagency.smartwomenadvantage;

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
public class BI_SmartWomenAdvantageActivity extends AppCompatActivity implements
        OnEditorActionListener {

    private final int SIGNATURE_ACTIVITY = 1;
    private final String proposer_Is_Same_As_Life_Assured = "Y";
    private final int DATE_DIALOG_ID = 1;
    private NeedAnalysisBIService NABIObj;
    private NA_CBI_bean na_cbi_bean;
    private File newFile;
    private File needAnalysispath;
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
    private CheckBox cb_bi_smart_women_advantage_JKResident;
    private CheckBox cb_bi_smart_women_advantage_apc_ca_option;
    private EditText edt_bi_smart_women_advantage_life_assured_first_name;
    private EditText edt_bi_smart_women_advantage_life_assured_middle_name;
    private EditText edt_bi_smart_women_advantage_life_assured_last_name;
    private EditText edt_bi_smart_women_advantage_life_assured_age;
    private EditText edt_smart_women_advantage_contact_no;
    private EditText edt_smart_women_advantage_Email_id;
    private EditText edt_smart_women_advantage_ConfirmEmail_id;
    private EditText edt_bi_smart_women_advantage_sum_assured_amount;
    private EditText edt_bi_smart_women_advantage_critical_illness_sum_assured;
    private EditText edt_bi_smart_women_advantage_apc_ca_option_sum_assured;
    private Spinner spnr_bi_smart_women_advantage_life_assured_title;
    private Spinner spnr_bi_smart_women_advantage_selGender;
    private Spinner spnr_bi_smart_women_advantage_plan;
    private Spinner spnr_bi_smart_women_advantage_policyterm;
    private Spinner spnr_bi_smart_women_advantage_critical_illness_option;
    private Spinner spnr_bi_smart_women_advantage_premium_frequency;
    private TableRow tr_apc_ca_option_sum_assured;
    private Button btn_bi_smart_women_advantage_life_assured_date;
    private Button btnBack;
    private Button btnSubmit;
    private Button btn_MarketingOfficalDate;
    private Button btn_PolicyholderDate;
    private ImageButton Ibtn_signatureofMarketing;
    private ImageButton Ibtn_signatureofPolicyHolders;
    private RadioButton rb_smart_women_advantage_backdating_yes;
    private RadioButton rb_smart_women_advantage_backdating_no;
    private LinearLayout ll_smart_women_advantage_backdating;
    private Button btn_smart_women_advantage_backdatingdate;
    private String QuatationNumber = "";
    private String planName = "";
    // newDBHelper db;
    private ParseXML prsObj;
    private DecimalFormat currencyFormat;
    private CommonForAllProd obj;
    private CommonForAllProd commonForAllProd = null;
    private AlertDialog.Builder showAlert;
    private SmartWomenAdvantageBean smartWomenAdvantageBean;
    private SmartWomenAdvantageProperties prop;
    private String emailId = "";
    private String mobileNo = "";
    private String ConfirmEmailId = "";
    private String ProposerEmailId = "";
    private boolean validationFla1 = false;
    private Dialog d;
    private Bitmap photoBitmap;
    private StringBuilder retVal;
    private StringBuilder inputVal;
    private String lifeAssured_Title = "";
    private String lifeAssured_First_Name = "";
    private String lifeAssured_Middle_Name = "";
    private String lifeAssured_Last_Name = "";
    private String name_of_life_assured = "";
    private String lifeAssured_date_of_birth = "";
    private String lifeAssuredAge = "";
    private String output = "";
    private String name_of_proposer = "";
    private String name_of_person = "";
    private String place2 = "";
    private String date1 = "";
    private String date2 = "";
    private String agent_sign = "";
    private String proposer_sign = "";
    private String proposer_Backdating_WishToBackDate_Policy = "";
    private String proposer_Backdating_BackDate = "";
    private int mYear;
    private int mMonth;
    private int mDay;
    private String gender = "";
    private String premium_paying_frequency = "";
    private String policy_term = "";
    private String sum_assured = "";
    private List<M_BI_SmartWomenAdvantageGrid_Adpter> list_data;
    private int DIALOG_ID;
    private StringBuilder bussIll = null;
    private String yearlypremium_with_servicetax_first_year = "";
    private File mypath;
    private boolean valPremAmount = false;
    private String product_Code, product_UIN, product_cateogory, product_type;
    private String latestImage = "";
    private String bankUserType = "", mode = "";
    private Context context;

    private String Check = "";
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
    private ImageButton imageButtonSmartWomenAdvantageProposerPhotograph;
    private LinearLayout linearlayoutThirdpartySignature,
            linearlayoutAppointeeSignature;
    private ImageButton Ibtn_signatureofThirdParty, Ibtn_signatureofAppointee;
    private String thirdPartySign = "", appointeeSign = "", Company_policy_surrender_dec = "";
    private CheckBox cb_kerladisc;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.bi_smart_women_advantagemain);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);


        dbHelper = new DatabaseHelper(getApplicationContext());

        NABIObj = new NeedAnalysisBIService(this);
        prsObj = new ParseXML();
        Intent intent = getIntent();

        context = this;
        commonMethods = new CommonMethods();
        mStorageUtils = new StorageUtils();
        commonMethods.setActionbarLayout(this);

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

                    ProductInfo prodInfoObj = new ProductInfo(context);
                    planName = "Smart Women Advantage";
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
        // ProductHomePageActivity.path.setText("Benefit Illustrator");

        UI_Declaration();

        prsObj = new ParseXML();
        setSpinner_Value();
        // setBIInputGui();

        if (needAnalysis_flag == 1 && !TextUtils.isEmpty(gender)) {
            spnr_bi_smart_women_advantage_selGender.setSelection(genderAdapter
                    .getPosition(gender));
            onClickLADob(btn_bi_smart_women_advantage_life_assured_date);
        }
        edt_bi_smart_women_advantage_life_assured_first_name
                .setOnEditorActionListener(this);
        edt_bi_smart_women_advantage_life_assured_middle_name
                .setOnEditorActionListener(this);
        edt_bi_smart_women_advantage_life_assured_last_name
                .setOnEditorActionListener(this);
        edt_bi_smart_women_advantage_sum_assured_amount
                .setOnEditorActionListener(this);
        edt_smart_women_advantage_contact_no.setOnEditorActionListener(this);
        edt_smart_women_advantage_Email_id.setOnEditorActionListener(this);
        edt_smart_women_advantage_ConfirmEmail_id
                .setOnEditorActionListener(this);

        setFocusable(spnr_bi_smart_women_advantage_life_assured_title);
        spnr_bi_smart_women_advantage_life_assured_title.requestFocus();

        validationOfMoile_EmailId();
        setSpinnerAndOtherListner();
        showAlert = new AlertDialog.Builder(this);
        obj = new CommonForAllProd();
        commonForAllProd = new CommonForAllProd();
        prop = new SmartWomenAdvantageProperties();
        smartWomenAdvantageBean = new SmartWomenAdvantageBean();
        list_data = new ArrayList<>();
        currencyFormat = new DecimalFormat("##,##,##,###");
        // getBasicDetail();
    }

    private void UI_Declaration() {
        ScrollView sv_bi_smart_women_advantage_main = findViewById(R.id.sv_bi_smart_women_advantage_main);
        cb_staffdisc = findViewById(R.id.cb_staffdisc);
        cb_bi_smart_women_advantage_JKResident = findViewById(R.id.cb_bi_smart_women_advantage_JKResident);
        cb_bi_smart_women_advantage_apc_ca_option = findViewById(R.id.cb_bi_smart_women_advantage_apc_ca_option);

        edt_bi_smart_women_advantage_life_assured_first_name = findViewById(R.id.edt_bi_smart_women_advantage_life_assured_first_name);
        edt_bi_smart_women_advantage_life_assured_middle_name = findViewById(R.id.edt_bi_smart_women_advantage_life_assured_middle_name);
        edt_bi_smart_women_advantage_life_assured_last_name = findViewById(R.id.edt_bi_smart_women_advantage_life_assured_last_name);
        edt_bi_smart_women_advantage_life_assured_age = findViewById(R.id.edt_bi_smart_women_advantage_life_assured_age);
        edt_smart_women_advantage_contact_no = findViewById(R.id.edt_smart_women_advantage_contact_no);
        edt_smart_women_advantage_Email_id = findViewById(R.id.edt_smart_women_advantage_Email_id);
        edt_smart_women_advantage_ConfirmEmail_id = findViewById(R.id.edt_smart_women_advantage_ConfirmEmail_id);
        edt_bi_smart_women_advantage_sum_assured_amount = findViewById(R.id.edt_bi_smart_women_advantage_sum_assured_amount);
        edt_bi_smart_women_advantage_critical_illness_sum_assured = findViewById(R.id.edt_bi_smart_women_advantage_critical_illness_sum_assured);
        edt_bi_smart_women_advantage_critical_illness_sum_assured
                .setClickable(false);
        edt_bi_smart_women_advantage_critical_illness_sum_assured
                .setEnabled(false);

        edt_bi_smart_women_advantage_apc_ca_option_sum_assured = findViewById(R.id.edt_bi_smart_women_advantage_apc_ca_option_sum_assured);
        edt_bi_smart_women_advantage_apc_ca_option_sum_assured
                .setClickable(false);
        edt_bi_smart_women_advantage_apc_ca_option_sum_assured
                .setEnabled(false);
        spnr_bi_smart_women_advantage_life_assured_title = findViewById(R.id.spnr_bi_smart_women_advantage_life_assured_title);
        spnr_bi_smart_women_advantage_selGender = findViewById(R.id.spnr_bi_smart_women_advantage_selGender);
        spnr_bi_smart_women_advantage_selGender.setClickable(false);
        spnr_bi_smart_women_advantage_selGender.setEnabled(false);

        spnr_bi_smart_women_advantage_plan = findViewById(R.id.spnr_bi_smart_women_advantage_plan);
        spnr_bi_smart_women_advantage_policyterm = findViewById(R.id.spnr_bi_smart_women_advantage_policyterm);
        spnr_bi_smart_women_advantage_critical_illness_option = findViewById(R.id.spnr_bi_smart_women_advantage_critical_illness_option);
        spnr_bi_smart_women_advantage_premium_frequency = findViewById(R.id.spnr_bi_smart_women_advantage_premium_frequency);

        btn_bi_smart_women_advantage_life_assured_date = findViewById(R.id.btn_bi_smart_women_advantage_life_assured_date);

        btnBack = findViewById(R.id.btn_bi_smart_women_advantage_btnback);
        btnSubmit = findViewById(R.id.btn_bi_smart_women_advantage_btnSubmit);

        tr_apc_ca_option_sum_assured = findViewById(R.id.tr_apc_ca_option_sum_assured);
        LinearLayout ll_bi_smart_women_advantage_main = findViewById(R.id.ll_bi_smart_women_advantage_main);

        btn_smart_women_advantage_backdatingdate = findViewById(R.id.btn_smart_women_advantage_backdatingdate);
        rb_smart_women_advantage_backdating_yes = findViewById(R.id.rb_smart_women_advantage_backdating_yes);
        rb_smart_women_advantage_backdating_no = findViewById(R.id.rb_smart_women_advantage_backdating_no);
        ll_smart_women_advantage_backdating = findViewById(R.id.ll_smart_women_advantage_backdating);

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
        if ((lifeAssured_date_of_birth != null || !lifeAssured_date_of_birth
                .equals(""))) {
            showDialog(DATE_DIALOG_ID);
        } else {
            commonMethods.dialogWarning(context, "Please select a LifeAssured DOB", true);
        }
    }

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
                    String proposerAge = final_age;
                    final_age = Integer.toString(age);
                    if (final_age.contains("-")) {
                        commonMethods.BICommonDialog(context, "Please fill Valid Birth Date");
                    } else {
                        if (18 <= age) {

                            btn_bi_smart_women_advantage_life_assured_date
                                    .setText(date);
                            edt_bi_smart_women_advantage_life_assured_age
                                    .setText(final_age);
                        } else {
                            commonMethods.BICommonDialog(context, "Minimum age should be 18 yrs for proposer");
                            btn_bi_smart_women_advantage_life_assured_date
                                    .setText("Select Date");
                            edt_bi_smart_women_advantage_life_assured_age
                                    .setText("");
                        }
                    }
                    break;

                case 5:
                    // ProposerAge = final_age;
                    final_age = Integer.toString(age);
                    if (final_age.contains("-")) {
                        commonMethods.BICommonDialog(context, "Please fill Valid Birth Date");
                    } else {

                        if (18 <= age && age <= 50) {
                            lifeAssuredAge = final_age;
                            btn_bi_smart_women_advantage_life_assured_date
                                    .setText(date);
                            edt_bi_smart_women_advantage_life_assured_age
                                    .setText(final_age);

                            edt_bi_smart_women_advantage_life_assured_age
                                    .setText(final_age);
                            lifeAssured_date_of_birth = getDate1(date + "");
                            valEligibilityACPnCA();
                            valMaturityAge();

                            if (!proposer_Backdating_BackDate.equals("")) {
                                if (proposer_Backdating_WishToBackDate_Policy
                                        .equalsIgnoreCase("y")) {
                                    rb_smart_women_advantage_backdating_no
                                            .setChecked(true);
                                    ll_smart_women_advantage_backdating
                                            .setVisibility(View.GONE);
                                }
                                proposer_Backdating_BackDate = "";
                                btn_smart_women_advantage_backdatingdate
                                        .setText("Select Date");
                            } else {

                                clearFocusable(edt_smart_women_advantage_contact_no);
                                setFocusable(edt_smart_women_advantage_contact_no);
                                edt_smart_women_advantage_contact_no.requestFocus();
                            }
                            // clearFocusable(btn_bi_smart_women_advantage_life_assured_date);

                        } else {
                            commonMethods.BICommonDialog(context, "Minimum Age should be 18 yrs and Maximum Age should be "
                                    + 50 + " yrs For LifeAssured");
                            btn_bi_smart_women_advantage_life_assured_date
                                    .setText("Select Date");
                            edt_bi_smart_women_advantage_life_assured_age
                                    .setText("");
                            lifeAssured_date_of_birth = "";

                            clearFocusable(btn_bi_smart_women_advantage_life_assured_date);
                            setFocusable(btn_bi_smart_women_advantage_life_assured_date);
                            btn_bi_smart_women_advantage_life_assured_date
                                    .requestFocus();
                        }
                    }
                    break;

                case 6:
                    if (age >= 0) {
                        proposer_Backdating_BackDate = date + "";
                        btn_smart_women_advantage_backdatingdate
                                .setText(proposer_Backdating_BackDate);
                        clearFocusable(btn_smart_women_advantage_backdatingdate);

                    } else {
                        commonMethods.dialogWarning(context, "Please Select Valid BackDating Date", true);
                        btn_smart_women_advantage_backdatingdate
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
            edt_bi_smart_women_advantage_life_assured_age
                    .setText(str_final_Age);
            // valAge();

        }
    }

    private int calculateMyAge(int year, int month, int day, String Value) {
        Calendar nowCal = new GregorianCalendar(year, month, day);

        String[] ProposerDob = getDate(Value).split("/");
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

    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

        boolean flagFocus = true;
        spnr_bi_smart_women_advantage_life_assured_title.requestFocus();

    }

    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

        // TODO Auto-generated method stub
        if (v.getId() == edt_bi_smart_women_advantage_life_assured_first_name
                .getId()) {
            setFocusable(edt_bi_smart_women_advantage_life_assured_middle_name);
            edt_bi_smart_women_advantage_life_assured_middle_name
                    .requestFocus();
        } else if (v.getId() == edt_bi_smart_women_advantage_life_assured_middle_name
                .getId()) {
            setFocusable(edt_bi_smart_women_advantage_life_assured_last_name);
            edt_bi_smart_women_advantage_life_assured_last_name.requestFocus();
        } else if (v.getId() == edt_bi_smart_women_advantage_life_assured_last_name
                .getId()) {

            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(
                    edt_bi_smart_women_advantage_life_assured_last_name
                            .getWindowToken(), 0);
            setFocusable(btn_bi_smart_women_advantage_life_assured_date);
            btn_bi_smart_women_advantage_life_assured_date.requestFocus();
        } else if (v.getId() == edt_smart_women_advantage_contact_no.getId()) {
            setFocusable(edt_smart_women_advantage_Email_id);
            edt_smart_women_advantage_Email_id.requestFocus();
        } else if (v.getId() == edt_smart_women_advantage_Email_id.getId()) {
            setFocusable(edt_smart_women_advantage_ConfirmEmail_id);
            edt_smart_women_advantage_ConfirmEmail_id.requestFocus();
        } else if (v.getId() == edt_smart_women_advantage_ConfirmEmail_id
                .getId()) {
            clearFocusable(spnr_bi_smart_women_advantage_plan);
            setFocusable(spnr_bi_smart_women_advantage_plan);
            spnr_bi_smart_women_advantage_plan.requestFocus();
        } else if (v.getId() == edt_bi_smart_women_advantage_sum_assured_amount
                .getId()) {

            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(
                    edt_bi_smart_women_advantage_sum_assured_amount
                            .getWindowToken(), 0);
        }
        return true;

    }

    private void setSpinner_Value() {

        // Gender
        String[] genderList = {"Female"};
        genderAdapter = new ArrayAdapter<>(getApplicationContext(),
                R.layout.spinner_item, genderList);
        genderAdapter.setDropDownViewResource(R.layout.spinner_item1);
        spnr_bi_smart_women_advantage_selGender.setAdapter(genderAdapter);
        genderAdapter.notifyDataSetChanged();

        List<String> title_list = new ArrayList<>();
        title_list.add("Select Title");
        title_list.add("Ms.");
        title_list.add("Mrs.");
        commonMethods.fillSpinnerValue(context, spnr_bi_smart_women_advantage_life_assured_title,
                title_list);

        // Plan
        String[] planList = {"Gold", "Platinum"};
        ArrayAdapter<String> planAdapter = new ArrayAdapter<>(
                getApplicationContext(), R.layout.spinner_item, planList);
        planAdapter.setDropDownViewResource(R.layout.spinner_item1);
        spnr_bi_smart_women_advantage_plan.setAdapter(planAdapter);
        planAdapter.notifyDataSetChanged();

        // Policy Term
        String[] policyTermList = {"10", "15"};
        ArrayAdapter<String> policyTermAdapter = new ArrayAdapter<>(
                getApplicationContext(), R.layout.spinner_item, policyTermList);
        policyTermAdapter.setDropDownViewResource(R.layout.spinner_item1);
        spnr_bi_smart_women_advantage_policyterm.setAdapter(policyTermAdapter);
        policyTermAdapter.notifyDataSetChanged();

        // Critical Illness Option
        String[] criticalIllnessOpionList = {"1", "2", "3"};
        ArrayAdapter<String> criticalIllnessOpionAdapter = new ArrayAdapter<>(
                getApplicationContext(), R.layout.spinner_item,
                criticalIllnessOpionList);
        criticalIllnessOpionAdapter
                .setDropDownViewResource(R.layout.spinner_item1);
        spnr_bi_smart_women_advantage_critical_illness_option
                .setAdapter(criticalIllnessOpionAdapter);
        criticalIllnessOpionAdapter.notifyDataSetChanged();

        // Premium Frequency
        String[] premFreqList = {"Yearly", "Half Yearly", "Quarterly",
                "Monthly"};
        ArrayAdapter<String> premFreqAdapter = new ArrayAdapter<>(
                getApplicationContext(), R.layout.spinner_item, premFreqList);
        premFreqAdapter.setDropDownViewResource(R.layout.spinner_item1);
        spnr_bi_smart_women_advantage_premium_frequency
                .setAdapter(premFreqAdapter);
        premFreqAdapter.notifyDataSetChanged();

    }


    private void windowmessagesgin() {

        d = new Dialog(BI_SmartWomenAdvantageActivity.this);
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
                Intent intent = new Intent(BI_SmartWomenAdvantageActivity.this,
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

    public boolean valDoYouBackdate() {
        if (!proposer_Backdating_WishToBackDate_Policy.equals("")) {
            return true;
        } else {
            showAlert.setMessage("Please Select Do you wish to Backdate ");
            showAlert.setNeutralButton("OK",
                    new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            // apply focusable method
                            setFocusable(rb_smart_women_advantage_backdating_yes);
                            rb_smart_women_advantage_backdating_yes
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
                                setFocusable(btn_smart_women_advantage_backdatingdate);
                                btn_smart_women_advantage_backdatingdate
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

            if (rb_smart_women_advantage_backdating_yes.isChecked()) {

                final Calendar c = Calendar.getInstance();
                final int currYear = c.get(Calendar.YEAR);
                final int currMonth = c.get(Calendar.MONTH) + 1;

                SimpleDateFormat dateformat1 = new SimpleDateFormat(
                        "dd-MM-yyyy");
                Date dtBackDate = dateformat1
                        .parse(btn_smart_women_advantage_backdatingdate
                                .getText().toString());
                Date currentDate = c.getTime();

                Date finYerEndDate = null;

                /* Added by Vrushali on19-11-2015 start **/
                Date launchDate = dateformat1.parse("15-07-2015");
                /* Added by Vrushali on 19-11-2015 end **/

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
                /* Added by Akshaya on 24-08-2015 start **/
                else if (dtBackDate.before(launchDate)
                        && finYerEndDate.before(launchDate)) {
                    error = "Please enter Backdation date between "
                            + dateformat1.format(launchDate) + " and "
                            + dateformat1.format(currentDate);
                }
                /* Added by Akshaya on 24-08-2015 end **/
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

    }

    private String getformatedThousandString(int number) {
        return NumberFormat.getNumberInstance(Locale.US)
                .format(number);
    }

    private String getformatedThousandString(Long number) {
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
            edt_smart_women_advantage_contact_no
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
            edt_smart_women_advantage_Email_id
                    .setError("Please provide the correct email address");
            validationFla1 = false;
        } else if ((matcher.matches())) {
            validationFla1 = true;
        }
    }

    private void validationOfMoile_EmailId() {

        edt_smart_women_advantage_contact_no
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
                        String abc = edt_smart_women_advantage_contact_no
                                .getText().toString();
                        mobile_validation(abc);

                    }
                });

        edt_smart_women_advantage_Email_id
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
                        ProposerEmailId = edt_smart_women_advantage_Email_id
                                .getText().toString();
                        //email_id_validation(ProposerEmailId);

                    }
                });

        edt_smart_women_advantage_ConfirmEmail_id
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
                        String proposer_confirm_emailId = edt_smart_women_advantage_ConfirmEmail_id
                                .getText().toString();
                        //	confirming_email_id(proposer_confirm_emailId);

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
                clearFocusable(spnr_bi_smart_women_advantage_life_assured_title);
                setFocusable(spnr_bi_smart_women_advantage_life_assured_title);
                spnr_bi_smart_women_advantage_life_assured_title.requestFocus();
            }
        });

        cb_bi_smart_women_advantage_JKResident
                .setOnClickListener(new OnClickListener() {

                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        if (cb_bi_smart_women_advantage_JKResident.isChecked()) {
                            cb_bi_smart_women_advantage_JKResident
                                    .setChecked(true);
                        } else {
                            cb_bi_smart_women_advantage_JKResident
                                    .setChecked(false);
                        }
                    }
                });

        // Spinner
        spnr_bi_smart_women_advantage_life_assured_title
                .setOnItemSelectedListener(new OnItemSelectedListener() {

                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int position, long id) {
                        // TODO Auto-generated method stub
                        if (position == 0) {
                            lifeAssured_Title = "";
                        } else {
                            lifeAssured_Title = spnr_bi_smart_women_advantage_life_assured_title
                                    .getSelectedItem().toString();
                            if (lifeAssured_Title.equalsIgnoreCase("Ms.")) {
                                spnr_bi_smart_women_advantage_selGender
                                        .setSelection(
                                                getIndex(
                                                        spnr_bi_smart_women_advantage_selGender,
                                                        "Female"), false);
                            } else if (lifeAssured_Title
                                    .equalsIgnoreCase("Mrs.")) {
                                spnr_bi_smart_women_advantage_selGender
                                        .setSelection(
                                                getIndex(
                                                        spnr_bi_smart_women_advantage_selGender,
                                                        "Female"), false);
                            }
                            clearFocusable(spnr_bi_smart_women_advantage_life_assured_title);
                            setFocusable(edt_bi_smart_women_advantage_life_assured_first_name);
                            edt_bi_smart_women_advantage_life_assured_first_name
                                    .requestFocus();
                        }
                    }

                    public void onNothingSelected(AdapterView<?> arg0) {
                        // TODO Auto-generated method stub

                    }
                });

        edt_bi_smart_women_advantage_sum_assured_amount
                .addTextChangedListener(new TextWatcher() {

                    public void afterTextChanged(Editable s) {

                        // you can call or do what you want with your EditText
                        // here
                        updateCritiSumAssured();
                        updateAPCnCPSumAssured();
                    }

                    public void beforeTextChanged(CharSequence s, int start,
                                                  int count, int after) {
                    }

                    public void onTextChanged(CharSequence s, int start,
                                              int before, int count) {
                    }
                });

        cb_bi_smart_women_advantage_apc_ca_option
                .setOnCheckedChangeListener(new OnCheckedChangeListener() {

                    public void onCheckedChanged(CompoundButton buttonView,
                                                 boolean isChecked) {
                        if (isChecked) {
                            if (valEligibilityACPnCA()) {
                                tr_apc_ca_option_sum_assured
                                        .setVisibility(View.VISIBLE);
                                updateAPCnCPSumAssured();
                            }
                        } else {
                            tr_apc_ca_option_sum_assured
                                    .setVisibility(View.GONE);
                        }
                    }
                });

        spnr_bi_smart_women_advantage_critical_illness_option
                .setOnItemSelectedListener(new OnItemSelectedListener() {

                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int pos, long id) {

                        updateCritiSumAssured();

                        clearFocusable(spnr_bi_smart_women_advantage_critical_illness_option);
                        setFocusable(spnr_bi_smart_women_advantage_plan);
                        spnr_bi_smart_women_advantage_plan.requestFocus();

                    }

                    public void onNothingSelected(AdapterView<?> arg0) {
                        // TODO Auto-generated method stub

                    }
                });

        spnr_bi_smart_women_advantage_plan
                .setOnItemSelectedListener(new OnItemSelectedListener() {

                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int position, long id) {
                        // TODO Auto-generated method stub

                        clearFocusable(spnr_bi_smart_women_advantage_plan);
                        setFocusable(spnr_bi_smart_women_advantage_policyterm);
                        spnr_bi_smart_women_advantage_policyterm.requestFocus();
                    }

                    public void onNothingSelected(AdapterView<?> arg0) {
                        // TODO Auto-generated method stub

                    }
                });

        spnr_bi_smart_women_advantage_policyterm
                .setOnItemSelectedListener(new OnItemSelectedListener() {

                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int pos, long id) {

                        clearFocusable(spnr_bi_smart_women_advantage_policyterm);
                        setFocusable(spnr_bi_smart_women_advantage_premium_frequency);
                        spnr_bi_smart_women_advantage_premium_frequency
                                .requestFocus();

                        valEligibilityACPnCA();
                        valMaturityAge();
                    }

                    public void onNothingSelected(AdapterView<?> arg0) {
                        // TODO Auto-generated method stub

                    }
                });

        spnr_bi_smart_women_advantage_premium_frequency
                .setOnItemSelectedListener(new OnItemSelectedListener() {

                    public void onItemSelected(AdapterView<?> adapter,
                                               View arg1, int position, long id) {
                        // TODO Auto-generated method stub

                        if (edt_bi_smart_women_advantage_life_assured_first_name
                                .getText().toString().equals("")) {
                            clearFocusable(spnr_bi_smart_women_advantage_premium_frequency);
                            setFocusable(spnr_bi_smart_women_advantage_life_assured_title);
                            spnr_bi_smart_women_advantage_life_assured_title
                                    .requestFocus();
                        } else {
                            clearFocusable(spnr_bi_smart_women_advantage_premium_frequency);
                            setFocusable(edt_bi_smart_women_advantage_sum_assured_amount);
                            edt_bi_smart_women_advantage_sum_assured_amount
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

                // clearFocusable(edt_bi_smart_women_advantage_sum_assured_amount);
                inputVal = new StringBuilder();
                retVal = new StringBuilder();

                lifeAssured_First_Name = edt_bi_smart_women_advantage_life_assured_first_name
                        .getText().toString();
                lifeAssured_Middle_Name = edt_bi_smart_women_advantage_life_assured_middle_name
                        .getText().toString();
                lifeAssured_Last_Name = edt_bi_smart_women_advantage_life_assured_last_name
                        .getText().toString();

                name_of_life_assured = lifeAssured_Title + " "
                        + lifeAssured_First_Name + " "
                        + lifeAssured_Middle_Name + " " + lifeAssured_Last_Name;

                mobileNo = edt_smart_women_advantage_contact_no.getText()
                        .toString();
                emailId = edt_smart_women_advantage_Email_id.getText()
                        .toString();
                ConfirmEmailId = edt_smart_women_advantage_ConfirmEmail_id
                        .getText().toString();

                if (valLifeAssuredProposerDetail() && valDob()
                        && valBasicDetail() && valSumAssured()
                        && valCritiSumAssured() && valMaturityAge()
                        && valEligibilityACPnCA() && valBackdate()
                        && TrueBackdate()) {

                    Log.e("ouput:", output + "");
                    addListenerOnSubmit();
                    Log.e("ouput:", output + "");
                    if (valPremAmount) {
                        getInput(smartWomenAdvantageBean);
                        if (needAnalysis_flag == 0) {
                            Intent i = new Intent(getApplicationContext(),
                                    Success.class);
                            i.putExtra(
                                    "op",
                                    spnr_bi_smart_women_advantage_premium_frequency
                                            .getSelectedItem().toString()
                                            + " Premium is Rs. "
                                            + currencyFormat.format(Double.parseDouble(prsObj
                                            .parseXmlTag(
                                                    retVal.toString(),
                                                    "finalPremBasicSA"))));
                            i.putExtra(
                                    "op1",
                                    "Premium for Critical Illness is Rs. "
                                            + currencyFormat.format(Double.parseDouble(prsObj.parseXmlTag(
                                            retVal.toString(),
                                            "finalPremBasicCritiIllness"))));
                            if (cb_bi_smart_women_advantage_apc_ca_option
                                    .isChecked())
                                i.putExtra(
                                        "op2",
                                        "Premium for APC & CA is Rs. "
                                                + currencyFormat.format(Double.parseDouble(prsObj.parseXmlTag(
                                                retVal.toString(),
                                                "finalPremBasicAPCnCA"))));
                            i.putExtra(
                                    "op3",
                                    "Total Premium is Rs. "
                                            + currencyFormat.format(Double.parseDouble(prsObj
                                            .parseXmlTag(
                                                    retVal.toString(),
                                                    "yearlyPrem"))));
                            i.putExtra(
                                    "op4",
                                    "First Year Premium with Applicable Taxes  is Rs. "
                                            + currencyFormat.format(Double.parseDouble(prsObj
                                            .parseXmlTag(
                                                    retVal.toString(),
                                                    "premWthST"))));
                            i.putExtra(
                                    "op5",
                                    "First Year Applicable Taxes is Rs. "
                                            + currencyFormat.format(Double.parseDouble(prsObj
                                            .parseXmlTag(
                                                    retVal.toString(),
                                                    "servcTax"))));
                            /*
                             * if (cb_bi_smart_women_advantage_JKResident
                             * .isChecked() == false) i.putExtra( "op6",
                             * "First Year Cess is Rs." +
                             * currencyFormat.format(Double
                             * .parseDouble(prsObj.parseXmlTag(
                             * retVal.toString(), "cessFirstYear"))));
                             */
                            i.putExtra(
                                    "op6",
                                    "Second Year Premium with Applicable Taxes  is Rs. "
                                            + currencyFormat.format(Double.parseDouble(prsObj.parseXmlTag(
                                            retVal.toString(),
                                            "premWthSTSecondYear"))));
                            i.putExtra(
                                    "op7",
                                    "Second Year Applicable Taxes is Rs. "
                                            + currencyFormat.format(Double.parseDouble(prsObj.parseXmlTag(
                                            retVal.toString(),
                                            "servcTaxSeondYear"))));
                            /*
                             * if (cb_bi_smart_women_advantage_JKResident
                             * .isChecked() == false) i.putExtra( "op9",
                             * "Second Year Cess is Rs." +
                             * currencyFormat.format(
                             * Double.parseDouble(prsObj.parseXmlTag(
                             * retVal.toString(), "cessSeondYear"))));
                             */
                            i.putExtra(
                                    "op8",
                                    "Guaranteed Death Benefit is Rs. "
                                            + currencyFormat.format(Double.parseDouble(prsObj
                                            .parseXmlTag(
                                                    retVal.toString(),
                                                    "guarMatBen"))));
                            i.putExtra(
                                    "op9",
                                    "Non-Guaranteed  Survival Benefit at 4% (on Maturity) is Rs. "
                                            + currencyFormat.format(Double.parseDouble(prsObj
                                            .parseXmlTag(
                                                    retVal.toString(),
                                                    "nonguraMatBen4"))));
                            i.putExtra(
                                    "op10",
                                    "Non-Guaranteed  Survival Benefit at 8% (on Maturity) is Rs. "
                                            + currencyFormat.format(Double.parseDouble(prsObj
                                            .parseXmlTag(
                                                    retVal.toString(),
                                                    "nonguraMatBen8"))));
                            i.putExtra("header",
                                    "SBI Life - Smart Women Advantage");
                            i.putExtra("header1", "(UIN : 111N106V01)");
                            startActivity(i);
                        } else
                            Dialog();
                    }

                }

            }
        });

        rb_smart_women_advantage_backdating_yes
                .setOnCheckedChangeListener(new OnCheckedChangeListener() {

                    public void onCheckedChanged(CompoundButton buttonView,
                                                 boolean isChecked) {
                        // TODO Auto-generated method stub
                        if (isChecked) {

                            if (!(lifeAssured_date_of_birth.equals(""))) {
                                proposer_Backdating_WishToBackDate_Policy = "y";
                                rb_smart_women_advantage_backdating_yes
                                        .setChecked(true);
                                rb_smart_women_advantage_backdating_no
                                        .setChecked(false);
                                ll_smart_women_advantage_backdating
                                        .setVisibility(View.VISIBLE);
                                btn_smart_women_advantage_backdatingdate
                                        .setText("Select Date");
                                proposer_Backdating_BackDate = "";
                            } else {

                                if (lifeAssured_date_of_birth.equals("")) {
                                    showAlert
                                            .setMessage("Please Select Life Assure Dob First");
                                    showAlert
                                            .setNeutralButton(
                                                    "OK",
                                                    new DialogInterface.OnClickListener() {

                                                        public void onClick(
                                                                DialogInterface dialog,
                                                                int which) {
                                                            rb_smart_women_advantage_backdating_yes
                                                                    .setChecked(false);
                                                            rb_smart_women_advantage_backdating_no
                                                                    .setChecked(true);
                                                        }
                                                    });
                                    showAlert.show();
                                }
                            }

                        }
                    }
                });

        rb_smart_women_advantage_backdating_no
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

                            // if (proposer_Backdating_WishToBackDate_Policy
                            // .equalsIgnoreCase("Y")) {
                            //
                            // btn_bi_smart_women_advantage_life_assured_date
                            // .setText("Select Date");
                            // //
                            // btn_bi_smart_women_advantage_proposer_date.setText("Select Date");
                            // edt_bi_smart_women_advantage_life_assured_age
                            // .setText("");
                            // //
                            // edt_bi_smart_women_advantage_proposer_age.setText("");
                            // lifeAssured_date_of_birth = "";
                            // lifeAssuredAge = "";
                            // ProposerAge = "";
                            // proposer_Backdating_BackDate = "";
                            // proposer_Backdating_WishToBackDate_Policy = "n";
                            // rb_smart_women_advantage_backdating_no
                            // .setChecked(true);
                            // rb_smart_women_advantage_backdating_yes
                            // .setChecked(false);
                            // ll_smart_women_advantage_backdating
                            // .setVisibility(View.GONE);
                            //
                            // }
                            // } else {

                            proposer_Backdating_WishToBackDate_Policy = "n";
                            rb_smart_women_advantage_backdating_no
                                    .setChecked(true);
                            rb_smart_women_advantage_backdating_yes
                                    .setChecked(false);
                            proposer_Backdating_BackDate = "";
                            // setDefaultDate();
                            ll_smart_women_advantage_backdating
                                    .setVisibility(View.GONE);

                            edt_bi_smart_women_advantage_life_assured_age
                                    .setText(lifeAssuredAge);
                            // edt_bi_smart_women_advantage_proposer_age.setText(ProposerAge);

                            rb_smart_women_advantage_backdating_yes
                                    .setFocusable(false);

                            clearFocusable(rb_smart_women_advantage_backdating_no);
                            clearFocusable(rb_smart_women_advantage_backdating_yes);
                            // }
                        }
                    }
                });

    }

    private void Dialog() {

        final Dialog d = new Dialog(this);
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);
        d.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.BLACK));

        d.setContentView(R.layout.layout_smart_women_advantage_bi_grid);

        TextView tv_proposername = d
                .findViewById(R.id.tv_proposername);
        TextView tv_proposal_number = d
                .findViewById(R.id.tv_proposal_number);

        TextView tv_bi_smart_women_advantage_ci_multiple = d
                .findViewById(R.id.tv_bi_smart_women_advantage_ci_multiple);
        TextView tv_bi_smart_women_advantage_life_assured_age = d
                .findViewById(R.id.tv_bi_smart_women_advantage_life_assured_age);
        TextView tv_bi_smart_women_advantage_life_assured_gender = d
                .findViewById(R.id.tv_bi_smart_women_advantage_life_assured_gender);
        TextView tv_bi_smart_women_advantage_life_assured_premium_frequency = d
                .findViewById(R.id.tv_bi_smart_women_advantage_life_assured_premium_frequency);

        TextView tv_bi_smart_women_advantage_plan_proposed = d
                .findViewById(R.id.tv_bi_smart_women_advantage_plan_proposed);
        TextView tv_bi_smart_women_advantage_term = d
                .findViewById(R.id.tv_bi_smart_women_advantage_term);
        TextView tv_bi_smart_women_advantage_term_first = d.findViewById(R.id.tv_bi_smart_women_advantage_term_first);
        TextView tv_bi_smart_women_advantage_term_second = d.findViewById(R.id.tv_bi_smart_women_advantage_term_second);

        TextView tv_bi_smart_women_advantage_sum_assured = d
                .findViewById(R.id.tv_bi_smart_women_advantage_sum_assured);
        TextView tv_bi_smart_women_advantage_ci_sum_assured = d
                .findViewById(R.id.tv_bi_smart_women_advantage_ci_sum_assured);

        TextView tv_bi_smart_women_advantage_premium_for_saving = d
                .findViewById(R.id.tv_bi_smart_women_advantage_premium_for_saving);
        TextView tv_bi_smart_women_advantage_premium_for_saving_second = d
                .findViewById(R.id.tv_bi_smart_women_advantage_premium_for_saving_second);

        TextView tv_bi_smart_women_advantage_premium_for_critical_illness_title = d
                .findViewById(R.id.tv_bi_smart_women_advantage_premium_for_critical_illness_title);
        TextView tv_bi_smart_women_advantage_premium_for_critical_illness = d
                .findViewById(R.id.tv_bi_smart_women_advantage_premium_for_critical_illness);

        TextView tv_bi_smart_women_advantage_premium_for_critical_illness_second = d
                .findViewById(R.id.tv_bi_smart_women_advantage_premium_for_critical_illness_second);

        TextView tv_bi_smart_women_advantage_premium_for_apc_ca_second_title = d
                .findViewById(R.id.tv_bi_smart_women_advantage_premium_for_apc_ca_second_title);

        TextView tv_bi_smart_women_advantage_premium_for_apc_ca_second = d
                .findViewById(R.id.tv_bi_smart_women_advantage_premium_for_apc_ca_second);

        // First year policy
        TextView tv_bi_smart_women_advantage_basic_premium_first_year = d
                .findViewById(R.id.tv_bi_smart_women_advantage_basic_premium_first_year);
        /*
         * TextView tv_bi_smart_women_advantage_service_tax_first_year =
         * (TextView) d
         * .findViewById(R.id.tv_bi_smart_women_advantage_service_tax_first_year
         * );
         */

        TextView tv_bi_smart_women_advantage_yearly_premium_with_tax_first_year = d
                .findViewById(R.id.tv_bi_smart_women_advantage_yearly_premium_with_tax_first_year);
        /*
         * TextView tv_bi_smart_women_advantage_cess_first_year = (TextView) d
         * .findViewById(R.id.tv_bi_smart_women_advantage_cess_first_year);
         */

        /*
         * TextView
         * tv_mandatory_bi_smart_women_advantage_swachh_bharat_tax_first_year =
         * (TextView) d .findViewById(R.id.
         * tv_mandatory_bi_smart_women_advantage_swachh_bharat_tax_first_year);
         */

        /*
         * TextView
         * tv_mandatory_bi_smart_women_advantage_krishi_kalyan_tax_first_year =
         * (TextView) d .findViewById(R.id.
         * tv_mandatory_bi_smart_women_advantage_krishi_kalyan_tax_first_year);
         */

        TextView tv_mandatory_bi_smart_women_advantage_total_service_tax_first_year = d
                .findViewById(R.id.tv_mandatory_bi_smart_women_advantage_total_service_tax_first_year);

        // Second year policy onwards
        TextView tv_bi_smart_women_advantage_second_year_heading = d
                .findViewById(R.id.tv_bi_smart_women_advantage_second_year_heading);

        TextView tv_bi_smart_women_advantage_basic_premium_second_year = d
                .findViewById(R.id.tv_bi_smart_women_advantage_basic_premium_second_year);
        /*
         * TextView tv_bi_smart_women_advantage_service_tax_second_year =
         * (TextView) d
         * .findViewById(R.id.tv_bi_smart_women_advantage_service_tax_second_year
         * );
         */

        TextView tv_bi_smart_women_advantage_yearly_premium_with_tax_second_year = d
                .findViewById(R.id.tv_bi_smart_women_advantage_yearly_premium_with_tax_second_year);
        /*
         * TextView tv_bi_smart_women_advantage_cess_second_year = (TextView) d
         * .findViewById(R.id.tv_bi_smart_women_advantage_cess_second_year);
         */

        /*
         * TextView
         * tv_mandatory_bi_smart_women_advantage_swachh_bharat_tax_second_year =
         * (TextView) d .findViewById(R.id.
         * tv_mandatory_bi_smart_women_advantage_swachh_bharat_tax_second_year);
         *
         * TextView
         * tv_mandatory_bi_smart_women_advantage_krishi_kalyan_tax_second_year =
         * (TextView) d .findViewById(R.id.
         * tv_mandatory_bi_smart_women_advantage_krishi_kalyan_tax_second_year);
         */

        TextView tv_mandatory_bi_smart_women_advantage_total_service_tax_second_year = d
                .findViewById(R.id.tv_mandatory_bi_smart_women_advantage_total_service_tax_second_year);

        final TextView edt_proposer_name = d
                .findViewById(R.id.edt_ProposalName);
        GridView gv_userinfo = d.findViewById(R.id.gv_userinfo);
        final EditText edt_Policyholderplace = d
                .findViewById(R.id.edt_Policyholderplace);

        final TextView tv_premium_install_rider_type1 = d
                .findViewById(R.id.tv_premium_install_rider_type1);
        TextView tv_mandatory_bi_smart_women_advantage_yearly_premium_with_tax1 = d
                .findViewById(R.id.tv_mandatory_bi_smart_women_advantage_yearly_premium_with_tax1);
        TextView tv_mandatory_bi_smart_women_advantage_premium_saving = d
                .findViewById(R.id.tv_mandatory_bi_smart_women_advantage_premium_saving);

        final EditText edt_MarketingOfficalPlace = d
                .findViewById(R.id.edt_MarketingOfficalPlace);

        TextView tv_bi_smart_women_advantage_premium_for_apc_ca = d
                .findViewById(R.id.tv_bi_smart_women_advantage_premium_for_apc_ca);

        TextView tv_bi_is_Staff = d
                .findViewById(R.id.tv_bi_is_Staff);
        //TextView tv_bi_is_JK = (TextView) d.findViewById(R.id.tv_bi_is_JK);

        TextView tv_bi_smart_women_advantage_apc_ca_riders_sum_assured = d
                .findViewById(R.id.tv_bi_smart_women_advantage_apc_ca_riders_sum_assured);

        TextView tv_smart_women_advantage_sbi_life_details = d
                .findViewById(R.id.tv_smart_women_advantage_sbi_life_details);

        // TableRow tr_second_year = (TableRow) d
        // .findViewById(R.id.tr_second_year);

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
                            + " have understood the term and conditions, product features and applicable charges (if any) before purchase of the contract, after receipt of all information stated above from the insurer.");
            edt_proposer_name_need_analysis
                    .setText("I, "
                            + name_of_life_assured
                            + " have undergone the Need Analysis  after having reviewed the SBI Life Product options, I have opted for SBI LIFE - Smart Women Advantage");
            tv_proposername.setText(name_of_life_assured);

            // tv_life_assured_name.setText(name_of_life_assured);
            tv_proposername.setText(name_of_life_assured);
        } else if (proposer_Is_Same_As_Life_Assured.equalsIgnoreCase("n")) {
            edt_proposer_name
                    .setText("I, "
                            + name_of_proposer
                            + " have understood the term and conditions, product features and applicable charges (if any) before purchase of the contract, after receipt of all information stated above from the insurer.");
            edt_proposer_name_need_analysis
                    .setText("I, "
                            + name_of_proposer
                            + " have undergone the Need Analysis after having reviewed the SBI Life Product options, I have opted for SBI LIFE - Smart Women Advantage.");

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

        imageButtonSmartWomenAdvantageProposerPhotograph = d
                .findViewById(R.id.imageButtonSmartWomenAdvantageProposerPhotograph);
        imageButtonSmartWomenAdvantageProposerPhotograph
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
            imageButtonSmartWomenAdvantageProposerPhotograph
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
                            imageButtonSmartWomenAdvantageProposerPhotograph
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
                date1 = btn_MarketingOfficalDate.getText().toString();
                date2 = btn_PolicyholderDate.getText().toString();

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
										.isChecked() == true) && ((radioButtonAppointeeYes
								.isChecked() == true && !appointeeSign
								.equals("")) || radioButtonAppointeeNo
								.isChecked() == true)*/) && radioButtonTrasactionModeParivartan
                        .isChecked()) || radioButtonTrasactionModeManual
                        .isChecked())) {

                    NeedAnalysisActivity.str_need_analysis = "";

                    String productCode = "";

                    if (smartWomenAdvantageBean.getSpnr_Plan().equals("Gold")
                            && smartWomenAdvantageBean
                            .getSpnr_criticalIllnesOpt().equals("1"))
                        productCode = "2CSWAG1";
                    else if (smartWomenAdvantageBean.getSpnr_Plan().equals(
                            "Gold")
                            && smartWomenAdvantageBean
                            .getSpnr_criticalIllnesOpt().equals("2"))
                        productCode = "2CSWAG2";
                    else if (smartWomenAdvantageBean.getSpnr_Plan().equals(
                            "Gold")
                            && smartWomenAdvantageBean
                            .getSpnr_criticalIllnesOpt().equals("3"))
                        productCode = "2CSWAG3";
                    else if (smartWomenAdvantageBean.getSpnr_Plan().equals(
                            "Platinum")
                            && smartWomenAdvantageBean
                            .getSpnr_criticalIllnesOpt().equals("1"))
                        productCode = "2CSWAP1";
                    else if (smartWomenAdvantageBean.getSpnr_Plan().equals(
                            "Platinum")
                            && smartWomenAdvantageBean
                            .getSpnr_criticalIllnesOpt().equals("2"))
                        productCode = "2CSWAP2";
                    else if (smartWomenAdvantageBean.getSpnr_Plan().equals(
                            "Platinum")
                            && smartWomenAdvantageBean
                            .getSpnr_criticalIllnesOpt().equals("3"))
                        productCode = "2CSWAP3";

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
                            obj.getRound(sum_assured),
                            obj.getRound(yearlypremium_with_servicetax_first_year),
                            emailId, mobileNo, agentEmail, agentMobile,
                            na_input, na_output, premium_paying_frequency,
                            Integer.parseInt(policy_term), 0, productCode,
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
                            obj.getRound(sum_assured),
                            obj.getRound(yearlypremium_with_servicetax_first_year),
                            agentEmail, agentMobile, na_input, na_output,
                            premium_paying_frequency, Integer
                            .parseInt(policy_term), 0, productCode,
                            getDate(lifeAssured_date_of_birth), "", "", mode, inputVal
                            .toString(), retVal.toString().replace(
                            bussIll.toString(), "")));

                    CreateSmartWomenAdvantageBIPdf(smartWomenAdvantageBean);

                    NABIObj.serviceHit(BI_SmartWomenAdvantageActivity.this,
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
                        setFocusable(imageButtonSmartWomenAdvantageProposerPhotograph);
                        imageButtonSmartWomenAdvantageProposerPhotograph
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
        output = retVal.toString();

        // tv_bi_smart_women_advantage_life_assured_name
        // .setText(name_of_life_assured);
        String ci_multiple = prsObj.parseXmlTag(input, "criticalIllnesOpt");
        tv_bi_smart_women_advantage_ci_multiple.setText(ci_multiple);

        String age_entry = prsObj.parseXmlTag(input, "age");
        tv_bi_smart_women_advantage_life_assured_age.setText(age_entry
                + " Years");

        gender = prsObj.parseXmlTag(input, "gender");
        tv_bi_smart_women_advantage_life_assured_gender.setText(gender);

        premium_paying_frequency = prsObj.parseXmlTag(input, "premFreq");
        tv_bi_smart_women_advantage_life_assured_premium_frequency
                .setText(premium_paying_frequency);

        String staffdiscount = prsObj.parseXmlTag(input, "isStaff");

        if (staffdiscount.equalsIgnoreCase("true")) {
            tv_bi_is_Staff.setText("Yes");
        } else {
            tv_bi_is_Staff.setText("No");
        }

        String isJkResident = prsObj.parseXmlTag(input, "isJKResident");

		/*if (isJkResident.equalsIgnoreCase("true")) {
			tv_bi_is_JK.setText("Yes");
		} else {
			tv_bi_is_JK.setText("No");
		}*/

        String apcSumAssured = prsObj.parseXmlTag(input, "apcSumAssured");
        tv_bi_smart_women_advantage_apc_ca_riders_sum_assured.setText("Rs. "
                + getformatedThousandString(Integer.parseInt(obj.getRound(obj
                .getStringWithout_E(Double.valueOf((apcSumAssured
                        .equals("") || apcSumAssured == null) ? "0"
                        : apcSumAssured))))));

        String plan = prsObj.parseXmlTag(input, "plan");
        tv_bi_smart_women_advantage_plan_proposed.setText(plan);

        policy_term = prsObj.parseXmlTag(input, "policyTerm");
        tv_bi_smart_women_advantage_term.setText(policy_term + " Years");
        tv_bi_smart_women_advantage_term_first.setText(policy_term + " Years");
        tv_bi_smart_women_advantage_term_second.setText(policy_term + " Years");

        String ci_sum_assured = prsObj.parseXmlTag(input, "critiSumAssured");

        tv_bi_smart_women_advantage_ci_sum_assured.setText("Rs. "
                + getformatedThousandString(Integer.parseInt(obj.getRound(obj
                .getStringWithout_E(Double.valueOf((ci_sum_assured
                        .equals("") || ci_sum_assured == null) ? "0"
                        : ci_sum_assured))))));

        sum_assured = prsObj.parseXmlTag(input, "sumAssured");

        tv_bi_smart_women_advantage_sum_assured.setText("Rs. "
                + getformatedThousandString(Integer.parseInt(obj.getRound(obj
                .getStringWithout_E(Double.valueOf((sum_assured
                        .equals("") || sum_assured == null) ? "0"
                        : sum_assured))))));
        String final_PremBasic_APCnCA = prsObj.parseXmlTag(output,
                "finalPremBasicAPCnCA");
        tv_bi_smart_women_advantage_premium_for_apc_ca
                .setText("Rs. "
                        + getformatedThousandString(Integer.parseInt(obj.getRound(obj.getStringWithout_E(Double
                        .valueOf((final_PremBasic_APCnCA == null || final_PremBasic_APCnCA
                                .equals("")) ? "0"
                                : final_PremBasic_APCnCA))))));

        tv_bi_smart_women_advantage_premium_for_apc_ca_second.setText("Rs. "
                + getformatedThousandString(Integer.parseInt(obj.getRound(obj.getStringWithout_E(Double
                .valueOf((final_PremBasic_APCnCA == null || final_PremBasic_APCnCA
                        .equals("")) ? "0"
                        : final_PremBasic_APCnCA))))));

        switch (premium_paying_frequency) {
            case "Yearly":
                tv_mandatory_bi_smart_women_advantage_premium_saving
                        .setText("Yearly Premium for Savings");

                tv_premium_install_rider_type1.setText("Yearly premium(Rs.)");
                tv_mandatory_bi_smart_women_advantage_yearly_premium_with_tax1
                        .setText("Yearly premium including Applicable Taxes(in Rs.)");
                break;
            case "Half Yearly":
                tv_mandatory_bi_smart_women_advantage_premium_saving
                        .setText("Half Yearly Premium for Savings");
                tv_premium_install_rider_type1.setText("Half Yearly premium(Rs.)");
                tv_mandatory_bi_smart_women_advantage_yearly_premium_with_tax1
                        .setText("Half Yearly premium including Applicable Taxes(in Rs.)");

                break;
            case "Quarterly":
                tv_mandatory_bi_smart_women_advantage_premium_saving
                        .setText("Quarterly Premium for Savings");
                tv_premium_install_rider_type1.setText("Quarterly premium(Rs.)");
                tv_mandatory_bi_smart_women_advantage_yearly_premium_with_tax1
                        .setText("Quarterly premium including Applicable Taxes(in Rs.)");

                break;
            case "Monthly":
                tv_mandatory_bi_smart_women_advantage_premium_saving
                        .setText("Monthly Premium for Savings");
                tv_premium_install_rider_type1.setText("Monthly premium(Rs.)");
                tv_mandatory_bi_smart_women_advantage_yearly_premium_with_tax1
                        .setText("Monthly premium including Applicable Taxes(in Rs.)");

                break;
            case "Single":
                tv_mandatory_bi_smart_women_advantage_premium_saving
                        .setText("Single Premium for Savings");
                tv_premium_install_rider_type1.setText("Single premium(Rs.)");
                tv_mandatory_bi_smart_women_advantage_yearly_premium_with_tax1
                        .setText("Single premium including Applicable Taxes(in Rs.)");
                break;
        }

        tv_bi_smart_women_advantage_premium_for_critical_illness_title.
                setText(premium_paying_frequency + " Premium for Critical Illness");
        tv_mandatory_bi_smart_women_advantage_premium_saving.
                setText(premium_paying_frequency + " Premium for Savings");

        tv_bi_smart_women_advantage_premium_for_apc_ca_second_title.
                setText(premium_paying_frequency + " Premium for APC & CA");

        tv_premium_install_rider_type1.
                setText("Total " + premium_paying_frequency + " premium excluding Applicable Taxes");

        String premium_saving = prsObj.parseXmlTag(output, "finalPremBasicSA");
        tv_bi_smart_women_advantage_premium_for_saving.setText("Rs. "
                + obj.getRound(obj.getStringWithout_E(Double
                .valueOf((premium_saving == null || premium_saving
                        .equals("")) ? "0" : premium_saving))));

        tv_bi_smart_women_advantage_premium_for_saving_second.setText("Rs. "
                + obj.getRound(obj.getStringWithout_E(Double
                .valueOf((premium_saving == null || premium_saving
                        .equals("")) ? "0" : premium_saving))));

        String premium_critical_illness = prsObj.parseXmlTag(output,
                "finalPremBasicCritiIllness");
        tv_bi_smart_women_advantage_premium_for_critical_illness
                .setText("Rs. "
                        + obj.getRound(obj.getStringWithout_E(Double
                        .valueOf((premium_critical_illness == null || premium_critical_illness
                                .equals("")) ? "0"
                                : premium_critical_illness))));

        tv_bi_smart_women_advantage_premium_for_critical_illness_second.setText("Rs. "
                + obj.getRound(obj.getStringWithout_E(Double
                .valueOf((premium_critical_illness == null || premium_critical_illness
                        .equals("")) ? "0"
                        : premium_critical_illness))));


        // first

        String yearlyPrem = prsObj.parseXmlTag(output, "yearlyPrem");
        String service_tax_first_year = prsObj.parseXmlTag(output, "servcTax");

        yearlypremium_with_servicetax_first_year = prsObj.parseXmlTag(output,
                "premWthST");

        String servicetax_second_year = prsObj
                .parseXmlTag(output, "servcTaxSeondYear");

        String yearlypremium_with_servicetax_second_year = prsObj.parseXmlTag(output,
                "premWthSTSecondYear");

        String cess_first_year = prsObj.parseXmlTag(output, "cessFirstYear");
        String cess_second_year = prsObj.parseXmlTag(output, "cessSeondYear");

        tv_bi_smart_women_advantage_basic_premium_first_year
                .setText(" Rs. "
                        + obj.getRound(obj.getStringWithout_E(Double
                        .valueOf((yearlyPrem == null || yearlyPrem
                                .equals("")) ? "0" : yearlyPrem))));

        /* basicServiceTax = prsObj.parseXmlTag(output, "basicServiceTax"); */
        String basicServiceTax = prsObj.parseXmlTag(output, "servcTax");

        /*
         * tv_bi_smart_women_advantage_service_tax_first_year.setText("Rs." +
         * obj.getRound(obj.getStringWithout_E(Double .valueOf((basicServiceTax
         * == null || basicServiceTax .equals("")) ? "0" : basicServiceTax))));
         */

        /*
         * double cessFirstyear=Double .valueOf((cess_first_year == null ||
         * cess_first_year .equals("")) ? "0" : cess_first_year);
         * tv_bi_smart_women_advantage_cess_first_year.setText(" Rs " +
         * obj.getRound(obj.getStringWithout_E(cessFirstyear)));
         */

        String SBCServiceTax = prsObj.parseXmlTag(output, "SBCServiceTax");
        double sbcServiceTaxDouble = Double
                .valueOf((SBCServiceTax == null || SBCServiceTax.equals("")) ? "0"
                        : SBCServiceTax);

        /*
         * tv_mandatory_bi_smart_women_advantage_swachh_bharat_tax_first_year
         * .setText("Rs." + obj.getRound(obj
         * .getStringWithout_E(sbcServiceTaxDouble)));
         */

        String KKCServiceTax = prsObj.parseXmlTag(output, "KKCServiceTax");
        double kkcserviceTaxDouble = Double
                .valueOf((KKCServiceTax == null || KKCServiceTax.equals("")) ? "0"
                        : KKCServiceTax);
        /*
         * tv_mandatory_bi_smart_women_advantage_krishi_kalyan_tax_first_year
         * .setText("Rs." + obj.getRound(obj
         * .getStringWithout_E(kkcserviceTaxDouble)));
         */

        double serviceTaxDouble = Double.parseDouble(prsObj.parseXmlTag(output,
                "servcTax"));

        tv_mandatory_bi_smart_women_advantage_total_service_tax_first_year
                .setText("Rs. "
                        + obj.getRound(obj.getStringWithout_E(serviceTaxDouble)));

        double totalPremiumWithAllTaxes = Double
                .valueOf((yearlypremium_with_servicetax_first_year == null || yearlypremium_with_servicetax_first_year
                        .equals("")) ? "0"
                        : yearlypremium_with_servicetax_first_year);

        tv_bi_smart_women_advantage_yearly_premium_with_tax_first_year
                .setText("Rs. "
                        + obj.getRound(obj
                        .getStringWithout_E(totalPremiumWithAllTaxes)));

        tv_bi_smart_women_advantage_second_year_heading
                .setVisibility(View.INVISIBLE);
        tv_bi_smart_women_advantage_basic_premium_second_year
                .setVisibility(View.INVISIBLE);
        /*
         * tv_bi_smart_women_advantage_service_tax_second_year
         * .setVisibility(View.INVISIBLE);
         */
        tv_bi_smart_women_advantage_yearly_premium_with_tax_second_year
                .setVisibility(View.INVISIBLE);
        // tv_bi_smart_women_advantage_cess_second_year.setVisibility(View.INVISIBLE);

        /*
         * tv_mandatory_bi_smart_women_advantage_swachh_bharat_tax_second_year
         * .setVisibility(View.INVISIBLE);
         * tv_mandatory_bi_smart_women_advantage_krishi_kalyan_tax_second_year
         * .setVisibility(View.INVISIBLE);
         */
        tv_mandatory_bi_smart_women_advantage_total_service_tax_second_year
                .setVisibility(View.INVISIBLE);

        if (!smartWomenAdvantageBean.getSpnr_PremFreq().equalsIgnoreCase(
                "Single")) {

            tv_bi_smart_women_advantage_second_year_heading
                    .setVisibility(View.VISIBLE);
            tv_bi_smart_women_advantage_basic_premium_second_year
                    .setVisibility(View.VISIBLE);
            /*
             * tv_bi_smart_women_advantage_service_tax_second_year
             * .setVisibility(View.VISIBLE);
             */
            tv_bi_smart_women_advantage_yearly_premium_with_tax_second_year
                    .setVisibility(View.VISIBLE);
            // tv_bi_smart_women_advantage_cess_second_year.setVisibility(View.VISIBLE);

            /*
             * tv_mandatory_bi_smart_women_advantage_swachh_bharat_tax_second_year
             * .setVisibility(View.VISIBLE);
             * tv_mandatory_bi_smart_women_advantage_krishi_kalyan_tax_second_year
             * .setVisibility(View.VISIBLE);
             */
            tv_mandatory_bi_smart_women_advantage_total_service_tax_second_year
                    .setVisibility(View.VISIBLE);

            tv_bi_smart_women_advantage_basic_premium_second_year.setText("Rs. "
                    + obj.getRound(obj.getStringWithout_E(Double
                    .valueOf((yearlyPrem == null || yearlyPrem
                            .equals("")) ? "0" : yearlyPrem))));

            /*
             * basicServiceTaxSecondYear = prsObj.parseXmlTag(output,
             * "basicServiceTaxSecondYear");
             */
            String basicServiceTaxSecondYear = prsObj.parseXmlTag(output,
                    "servcTaxSeondYear");

            double serviceTaxSecondYearDouble = Double
                    .valueOf((basicServiceTaxSecondYear == null || basicServiceTaxSecondYear
                            .equals("")) ? "0" : basicServiceTaxSecondYear);

            /*
             * tv_bi_smart_women_advantage_service_tax_second_year.setText("Rs."
             * + obj.getRound(obj
             * .getStringWithout_E(serviceTaxSecondYearDouble)));
             */

            // double cessSecondyearDouble=Double .valueOf((cess_second_year ==
            // null || cess_second_year .equals("")) ? "0" : cess_second_year);
            // tv_bi_smart_women_advantage_cess_second_year .setText(" Rs " +
            // obj.getRound(obj.getStringWithout_E(cessSecondyearDouble)));

            String SBCServiceTaxSecondYear = prsObj.parseXmlTag(output,
                    "SBCServiceTaxSecondYear");
            double SBCServiceTaxSecondYearDouble = Double
                    .valueOf((SBCServiceTaxSecondYear == null || SBCServiceTaxSecondYear
                            .equals("")) ? "0" : SBCServiceTaxSecondYear);
            /*
             * tv_mandatory_bi_smart_women_advantage_swachh_bharat_tax_second_year
             * .setText("Rs." + commonForAllProd.getRound(commonForAllProd
             * .getStringWithout_E(SBCServiceTaxSecondYearDouble)));
             */

            String KKCServiceTaxSecondYear = prsObj.parseXmlTag(output,
                    "KKCServiceTaxSecondYear");
            double KKCServiceTaxSecondYearDouble = Double
                    .valueOf((KKCServiceTaxSecondYear == null || KKCServiceTaxSecondYear
                            .equals("")) ? "0" : KKCServiceTaxSecondYear);
            /*
             * tv_mandatory_bi_smart_women_advantage_krishi_kalyan_tax_second_year
             * .setText("Rs." + obj.getRound(obj
             * .getStringWithout_E(KKCServiceTaxSecondYearDouble)));
             */

            double totalServiceTax = Double
                    .valueOf((servicetax_second_year == null || servicetax_second_year
                            .equals("")) ? "0" : servicetax_second_year);

            tv_mandatory_bi_smart_women_advantage_total_service_tax_second_year
                    .setText("Rs. "
                            + obj.getRound(obj
                            .getStringWithout_E(totalServiceTax)));

            double secondYearTotalServiceTaxWithPremium = Double
                    .valueOf((yearlypremium_with_servicetax_second_year == null || yearlypremium_with_servicetax_second_year
                            .equals("")) ? "0"
                            : yearlypremium_with_servicetax_second_year);

            tv_bi_smart_women_advantage_yearly_premium_with_tax_second_year
                    .setText("Rs. "
                            + obj.getRound(obj
                            .getStringWithout_E(secondYearTotalServiceTaxWithPremium)));

        }

        Company_policy_surrender_dec = "Your SBI LIFE - Smart Women Advantage(UIN: 111N106V01) is a Regular/Limited premium policy,for which your first year "
                + premium_paying_frequency
                + " premium is Rs. "
                + getformatedThousandString(Long.parseLong(obj.getRound(obj.getStringWithout_E(Double
                .valueOf((yearlypremium_with_servicetax_first_year.equals("") || yearlypremium_with_servicetax_first_year == null) ? "0"
                        : yearlypremium_with_servicetax_first_year)))))
                + ". Your policy Term is "
                + policy_term
                + " years,Premium Payment Term is "
                + policy_term
                + " years and Basic Sum Assured is Rs. "
                + getformatedThousandString(Long.parseLong(obj.getRound(obj.getStringWithout_E(Double
                .valueOf((sum_assured.equals("") || sum_assured == null) ? "0"
                        : sum_assured)))));


        tv_smart_women_advantage_sbi_life_details.setText(Company_policy_surrender_dec);


        for (int i = 1; i <= Integer.parseInt(policy_term); i++) {
            try {
                String policy_year = prsObj
                        .parseXmlTag(output, "policyYr" + i + "");

                String death_gurantee = prsObj.parseXmlTag(output,
                        "guarntedDeathBenft" + i + "");
                String benefit_payable_at_death_4_percentage = prsObj.parseXmlTag(
                        output, "nonGuarntedDeathBenft_4_pr" + i + "");
                String benefit_payable_at_death_8_percentage = prsObj.parseXmlTag(
                        output, "nonGuarntedDeathBenft_8_pr" + i + "");
                String critical_illness_benefit_gurantee = prsObj.parseXmlTag(
                        output, "guarateedCriticalIllnessBenefitAt_Minor" + i + "");
                String critical_illness_benefit_non_gurantee_4_percentage = prsObj
                        .parseXmlTag(output,
                                "guarateedCriticalIllnessBenefitAt_Major" + i + "");
                String critical_illness_benefit_non_gurantee_8_percentage = prsObj
                        .parseXmlTag(output,
                                "guarateedCriticalIllnessBenefitAt_Advanced" + i
                                        + "");
                String maturity_benefit_gurantee = prsObj.parseXmlTag(output,
                        "guaranteedSurvivalBenefit" + i + "");
                String maturity_benefit_non_gurantee_4_percentage = prsObj
                        .parseXmlTag(output,
                                "nonGuarateedSurvivalBenefitAt_4_Percent" + i + "");
                String maturity_benefit_non_gurantee_8_percentage = prsObj
                        .parseXmlTag(output,
                                "nonGuarateedSurvivalBenefitAt_8_Percent" + i + "");
                String surrender_value_gurantee = prsObj.parseXmlTag(output,
                        "GSV_surrendr_val" + i + "");
                String surrender_value_ssv_4_percentage = prsObj.parseXmlTag(
                        output, "NonGSV_surrndr_val_4Percent" + i + "");
                String surrender_value_ssv_8_percentage = prsObj.parseXmlTag(
                        output, "NonGSV_surrndr_val_8Percent" + i + "");

                list_data
                        .add(new M_BI_SmartWomenAdvantageGrid_Adpter(
                                (obj.getRound(obj.getStringWithout_E(Double
                                        .valueOf(policy_year)))) + "",
                                (obj.getRound(obj.getStringWithout_E(Double
                                        .valueOf(death_gurantee)))) + "",
                                (obj.getRound(obj.getStringWithout_E(Double
                                        .valueOf(benefit_payable_at_death_4_percentage))))
                                        + "",
                                (obj.getRound(obj.getStringWithout_E(Double
                                        .valueOf(benefit_payable_at_death_8_percentage))))
                                        + "",
                                (obj.getRound(obj.getStringWithout_E(Double
                                        .valueOf(critical_illness_benefit_gurantee))))
                                        + "",
                                (obj.getRound(obj.getStringWithout_E(Double
                                        .valueOf(critical_illness_benefit_non_gurantee_4_percentage))))
                                        + "",
                                (obj.getRound(obj.getStringWithout_E(Double
                                        .valueOf(critical_illness_benefit_non_gurantee_8_percentage))))
                                        + "",
                                (obj.getRound(obj.getStringWithout_E(Double
                                        .valueOf(maturity_benefit_gurantee)))) + "",
                                (obj.getRound(obj.getStringWithout_E(Double
                                        .valueOf(maturity_benefit_non_gurantee_4_percentage))))
                                        + "",
                                (obj.getRound(obj.getStringWithout_E(Double
                                        .valueOf(maturity_benefit_non_gurantee_8_percentage))))
                                        + "",
                                (obj.getRound(obj.getStringWithout_E(Double
                                        .valueOf(surrender_value_gurantee)))) + "",
                                (obj.getRound(obj.getStringWithout_E(Double
                                        .valueOf(surrender_value_ssv_4_percentage))))
                                        + "",
                                (obj.getRound(obj.getStringWithout_E(Double
                                        .valueOf(surrender_value_ssv_8_percentage))))
                                        + ""));
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        Adapter_BI_SmartWomenAdvantageGrid adapter = new Adapter_BI_SmartWomenAdvantageGrid(
                BI_SmartWomenAdvantageActivity.this, list_data);
        gv_userinfo.setAdapter(adapter);

        GridHeight gh = new GridHeight();
        gh.getheight(gv_userinfo, policy_term);

		/*if (Integer.parseInt(policy_term) <= 5) {
			gv_userinfo.getLayoutParams().height = 270;

		} else if (Integer.parseInt(policy_term) <= 10) {

			gv_userinfo.getLayoutParams().height = 270 * 2;

		}

		else if (Integer.parseInt(policy_term) <= 15) {

			gv_userinfo.getLayoutParams().height = 270 * 3;

		}

		else if (Integer.parseInt(policy_term) <= 20) {

			gv_userinfo.getLayoutParams().height = 270 * 4;

		}

		else if (Integer.parseInt(policy_term) <= 25) {

			gv_userinfo.getLayoutParams().height = 270 * 5;

		}

		else if (Integer.parseInt(policy_term) <= 30) {

			gv_userinfo.getLayoutParams().height = 270 * 6;

		}*/

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
                        imageButtonSmartWomenAdvantageProposerPhotograph
                                .setImageBitmap(scaled);
                    } else {
                        photoBitmap = null;
                    }
                }
            }
        }
    }

    private void CreateSmartWomenAdvantageBIPdf(
            SmartWomenAdvantageBean smartwomenbean) {

        try {
            String finalPremBasicSA = prsObj.parseXmlTag(output,
                    "finalPremBasicSA");
            String finalPremBasicCritiIllness = prsObj.parseXmlTag(output,
                    "finalPremBasicCritiIllness");
            String finalPremBasicAPCnCA = prsObj.parseXmlTag(output,
                    "finalPremBasicAPCnCA");
            String yearlyPrem = prsObj.parseXmlTag(output, "yearlyPrem");
            String servcTax = prsObj.parseXmlTag(output, "servcTax");
            String premWthST = prsObj.parseXmlTag(output, "premWthST");
            String servcTaxSeondYear = prsObj.parseXmlTag(output,
                    "servcTaxSeondYear");
            String premWthSTSecondYear = prsObj.parseXmlTag(output,
                    "premWthSTSecondYear");

            Font headerBold = new Font(Font.FontFamily.TIMES_ROMAN, 11,
                    Font.BOLD, BaseColor.WHITE);
            Font small_bold = new Font(Font.FontFamily.TIMES_ROMAN, 8,
                    Font.BOLD);
            Font small_bold_for_name = new Font(Font.FontFamily.TIMES_ROMAN,
                    10, Font.BOLD);
            Font small_bold1 = new Font(Font.FontFamily.TIMES_ROMAN, 6,
                    Font.BOLD);
            Font small_normal1 = new Font(Font.FontFamily.TIMES_ROMAN, 6,
                    Font.NORMAL);
            Font small_normal = new Font(Font.FontFamily.TIMES_ROMAN, 8,
                    Font.NORMAL);

            // float[] columnWidths2 = { 2f, 1f };
            // float[] columnWidths4 = { 2f, 1f, 2f, 1f };
            // File mypath = new File(folder, PropserNumber +
            // "Proposalno_p02.pdf");
            mypath = mStorageUtils.createFileToAppSpecificDir(context, QuatationNumber + "BI.pdf");
            needAnalysispath = mStorageUtils.createFileToAppSpecificDir(context, "NA.pdf");
            // needAnalysis_Tabular_path = new File(folder, "NA_Tabular.pdf");
            newFile = mStorageUtils.createFileToAppSpecificDir(context, QuatationNumber + "P01.pdf");
            PdfPCell cell;

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
                            "Benefit Illustration for SBI LIFE - Smart Women Advantage (UIN : 111N106V01)",
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
                    "SBI Life Insurance Co. Ltd Corporate Office: 'Natraj', M.V. Road and Western Express, Highway Junction, Andheri (East),Mumbai 400069. Regn No. 111",
                    small_bold_for_name);
            para_address.setAlignment(Element.ALIGN_CENTER);

            Paragraph para_address1 = new Paragraph(
                    "Website: www.sbilife.co.in | Email: info@sbilife.co.in | CIN: L99999MH2000PLC129113. Toll Free: 1800 267 9090 (Between 9.00 am & 9.00 pm)",
                    small_bold);
            // Paragraph para_address1 = new Paragraph(
            // "Waart",
            // urFont_gujarati_saral);

            // Paragraph para_address1 = new Paragraph(
            // "Aimat kumaar caaOhana",
            // urFont_shivaji);

            para_address1.setAlignment(Element.ALIGN_CENTER);
            document.add(para_address);
            document.add(para_address1);
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
            BI_Pdftable_Introdcutioncell
                    .setBackgroundColor(BaseColor.LIGHT_GRAY);
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
                            "For SBI Life – Smart Women Advantage (UIN: 111N106V01) the two rates of investment return we choose to use for this purpose are 4% and 8% per annum.",
                            small_normal));
            BI_Pdftable3_cell1.setPadding(5);

            BI_Pdftable3.addCell(BI_Pdftable3_cell1);
            document.add(BI_Pdftable3);

            PdfPTable BI_Pdftable5 = new PdfPTable(1);
            BI_Pdftable5.setWidthPercentage(100);
            // CR_table6.setWidths(columnWidths_3);
            PdfPCell BI_Pdftable5_cell1 = new PdfPCell(
                    new Paragraph(
                            "The main objective of the illustration is that the client is able to appreciate the features of the product and the flow of benefits in different circumstances with some level of quantification. For further information on the product and its benefits, please refer to the sales brochure and/or policy document. Further information will also be available on request.",
                            small_normal));

            BI_Pdftable5_cell1.setPadding(5);

            BI_Pdftable5.addCell(BI_Pdftable5_cell1);
            document.add(BI_Pdftable5);

            PdfPTable BI_Pdftable4 = new PdfPTable(1);
            BI_Pdftable4.setWidthPercentage(100);
            // CR_table6.setWidths(columnWidths_4);
            PdfPCell BI_Pdftable4_cell1 = new PdfPCell(
                    new Paragraph(
                            "Statutory Warning-"
                                    + "Some benefits are guaranteed and some benefits are variable with returns based on the future performance of your Insurer carrying on life insurance business. If your policy offers guaranteed returns then these will be clearly marked “guaranteed” in the illustration table on this page. If your policy offers variable returns then the illustrations on this page will show two different rates of assumed future investment returns. These assumed rates of return are not guaranteed and they are not the upper or lower limits of what you might get back, as the value of your policy is dependent on a number of factors including future investment performance.",
                            small_bold));

            BI_Pdftable4_cell1.setPadding(5);

            BI_Pdftable4.addCell(BI_Pdftable4_cell1);
            document.add(BI_Pdftable4);
            document.add(para_img_logo_after_space_1);

            // inputTable here -1

            PdfPTable personalDetail_table = new PdfPTable(4);
            personalDetail_table.setWidths(new float[]{5f, 5f, 5f, 5f});
            personalDetail_table.setWidthPercentage(100f);

            personalDetail_table.setHorizontalAlignment(Element.ALIGN_LEFT);

            // 1st row
            cell = new PdfPCell(new Phrase(
                    "Personal Details of Life to be Assured ", small_bold));
            cell.setColspan(4);
            cell.setPadding(5);
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            cell.setBorder(Rectangle.BOTTOM | Rectangle.LEFT | Rectangle.RIGHT
                    | Rectangle.TOP);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            personalDetail_table.addCell(cell);

            // //2nd row
            // cell = new PdfPCell(new Phrase("Name",small_normal));
            // cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            // cell.setPadding(5);
            // personalDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase("Policy Term", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setPadding(5);
            personalDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase(smartwomenbean.getSpnr_policyterm()
                    + " Years", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setPadding(5);
            personalDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase("Age(last birthday) ", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setPadding(5);
            personalDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase(smartwomenbean.getSpnr_Age() + " Years",
                    small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setPadding(5);
            personalDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase("Premium Payment Frequency",
                    small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setPadding(5);
            personalDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase(smartwomenbean.getSpnr_PremFreq()
                    + "", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setPadding(5);
            personalDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase("Plan", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setPadding(5);
            personalDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase(smartwomenbean.getSpnr_Plan() + "",
                    small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setPadding(5);
            personalDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase("Base Sum Assured (in Rs.)",
                    small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setPadding(5);
            personalDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase(currencyFormat.format(smartwomenbean
                    .getEdt_SumAssured()) + "", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setPadding(5);
            personalDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase("SAMF", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setPadding(5);
            personalDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase(
                    smartwomenbean.getSpnr_criticalIllnesOpt() + "",
                    small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setPadding(5);
            personalDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase("CI Sum Assured (in Rs.)",
                    small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setPadding(5);
            personalDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase(currencyFormat.format(smartwomenbean
                    .getEdt_critiSumAssured()) + "", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setPadding(5);
            personalDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase("Staff/Non-Staff ", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setPadding(5);
            personalDetail_table.addCell(cell);

            if (smartwomenbean.getIsStaffDisc())
                cell = new PdfPCell(new Phrase("Staff", small_normal));
            else
                cell = new PdfPCell(new Phrase("Non-Staff", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setPadding(5);
            personalDetail_table.addCell(cell);

            // ///
            cell = new PdfPCell(new Phrase("Gender ", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setPadding(5);
            personalDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase(
                    smartwomenbean.getSpnr_Gender() + "", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setPadding(5);
            personalDetail_table.addCell(cell);

			/*cell = new PdfPCell(new Phrase("IsJ&K Resident ", small_normal));
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell.setPadding(5);
			personalDetail_table.addCell(cell);

			if (smartwomenbean.getIsjkResident())
				cell = new PdfPCell(new Phrase("Yes", small_normal));
			else
				cell = new PdfPCell(new Phrase("No", small_normal));
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell.setPadding(5);
			personalDetail_table.addCell(cell);*/

            // ///
            cell = new PdfPCell(new Phrase("APC & CA Sum Assured (in Rs.)",
                    small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setPadding(5);
            personalDetail_table.addCell(cell);

            if (smartwomenbean.getIsAPCnCAoption()) {
                cell = new PdfPCell(new Phrase(
                        currencyFormat.format(smartwomenbean
                                .getEdt_APCSumAssured()) + "", small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setPadding(5);
                personalDetail_table.addCell(cell);
            } else {
                cell = new PdfPCell(new Phrase("0", small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setPadding(5);
                personalDetail_table.addCell(cell);
            }
            // Basic Cover
            document.add(personalDetail_table);

            document.add(para_img_logo_after_space_1);
			/*PdfPTable premiumDetail_table = new PdfPTable(4);
			premiumDetail_table.setWidths(new float[] { 5f, 4f, 5f, 4f });
			premiumDetail_table.setWidthPercentage(100f);
			premiumDetail_table.setHorizontalAlignment(Element.ALIGN_LEFT);

			// 1st row
			cell = new PdfPCell(new Phrase("Premium", small_bold));
			cell.setColspan(4);
			cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
			cell.setBorder(Rectangle.BOTTOM | Rectangle.LEFT | Rectangle.RIGHT
					| Rectangle.TOP);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setPadding(5);

			premiumDetail_table.addCell(cell);

			// 2 row
			cell = new PdfPCell(new Phrase(smartwomenbean.getSpnr_PremFreq()
					+ " Premium for Savings (in Rs.)", small_normal));
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell.setPadding(5);
			premiumDetail_table.addCell(cell);

			cell = new PdfPCell(new Phrase(currencyFormat.format(Double
					.parseDouble(finalPremBasicSA)) + "", small_normal));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setPadding(5);
			premiumDetail_table.addCell(cell);

			cell = new PdfPCell(new Phrase(
					"Premium for Critical illness (in Rs.)", small_normal));
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell.setPadding(5);
			premiumDetail_table.addCell(cell);

			cell = new PdfPCell(new Phrase(currencyFormat.format(Double
					.parseDouble(finalPremBasicCritiIllness)) + "",
					small_normal));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setPadding(5);
			premiumDetail_table.addCell(cell);

			if (smartwomenbean.getIsAPCnCAoption()) {
				cell = new PdfPCell(new Phrase("Premium for APC & CA (in Rs.)",
						small_normal));
				cell.setHorizontalAlignment(Element.ALIGN_LEFT);
				cell.setPadding(5);
				cell.setColspan(2);
				premiumDetail_table.addCell(cell);

				cell = new PdfPCell(new Phrase(currencyFormat.format(Double
						.parseDouble(finalPremBasicAPCnCA)) + "", small_normal));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell.setPadding(5);
				cell.setColspan(2);
				premiumDetail_table.addCell(cell);
			}


			document.add(premiumDetail_table);*/

            document.add(para_img_logo_after_space_1);

            // Amit changes start 24-5-2016

            PdfPTable FY_SY_premDetail_table = new PdfPTable(8);
            FY_SY_premDetail_table.setWidths(new float[]{5f, 5f, 5f, 5f, 5f, 5f, 5f, 5f});
            FY_SY_premDetail_table.setWidthPercentage(100f);
            FY_SY_premDetail_table.setHorizontalAlignment(Element.ALIGN_LEFT);

            // 1st row
            cell = new PdfPCell(new Phrase("Premium",
                    small_bold));
            cell.setColspan(8);
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            FY_SY_premDetail_table.addCell(cell);

            // 2 row
            cell = new PdfPCell(new Phrase("Policy Year", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            FY_SY_premDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase("Policy Term", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            FY_SY_premDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase(smartwomenbean.getSpnr_PremFreq()
                    + " Premium for Savings (in Rs.)", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            FY_SY_premDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase(smartwomenbean.getSpnr_PremFreq()
                    + " Premium for Critical illness (in Rs.)", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            FY_SY_premDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase(smartwomenbean.getSpnr_PremFreq()
                    + " Premium for APC & CA(in Rs.)", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            FY_SY_premDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase("Total " + smartwomenbean.getSpnr_PremFreq()
                    + " Premium excluding Applicable Taxes (in Rs.)", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            FY_SY_premDetail_table.addCell(cell);

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

            cell = new PdfPCell(new Phrase("Applicable Taxes(in Rs.)", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            FY_SY_premDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase("Total " + smartwomenbean.getSpnr_PremFreq()
                    + " Premium with Applicable Taxes (in Rs.)", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            FY_SY_premDetail_table.addCell(cell);

            // System.out.println("Plan "+plan.substring(6, 8));
            // 3 row
            cell = new PdfPCell(new Phrase("First year", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            FY_SY_premDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase(smartwomenbean.getSpnr_policyterm() + "", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            FY_SY_premDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                    .parseDouble(finalPremBasicSA)) + "", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            FY_SY_premDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                    .parseDouble(finalPremBasicCritiIllness)) + "",
                    small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            FY_SY_premDetail_table.addCell(cell);

            if (smartwomenbean.getIsAPCnCAoption()) {
                cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                        .parseDouble(finalPremBasicAPCnCA)) + "", small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                FY_SY_premDetail_table.addCell(cell);
            } else {
                cell = new PdfPCell(new Phrase("0", small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                FY_SY_premDetail_table.addCell(cell);
            }

            cell = new PdfPCell(new Phrase(
                    commonForAllProd.getRound(commonForAllProd
                            .getStringWithout_E(Double.valueOf(yearlyPrem
                                    .equals("") ? "0" : yearlyPrem))),
                    small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            FY_SY_premDetail_table.addCell(cell);

            // cell = new PdfPCell(new Phrase(
            // commonForAllProd.getRound(commonForAllProd
            // .getStringWithout_E(Double.valueOf(basicServiceTax
            // .equals("") ? "0" : basicServiceTax))),
            // small_normal));
            // cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            // FY_SY_premDetail_table.addCell(cell);
            //
            // cell = new PdfPCell(new Phrase(
            // commonForAllProd.getRound(commonForAllProd
            // .getStringWithout_E(Double.valueOf(SBCServiceTax
            // .equals("") ? "0" : SBCServiceTax))),
            // small_normal));
            // cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            // FY_SY_premDetail_table.addCell(cell);
            //
            // cell = new PdfPCell(new Phrase(
            // commonForAllProd.getRound(commonForAllProd
            // .getStringWithout_E(Double.valueOf(KKCServiceTax
            // .equals("") ? "0" : KKCServiceTax))),
            // small_normal));
            // cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            // FY_SY_premDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase(
                    commonForAllProd.getRound(commonForAllProd
                            .getStringWithout_E(Double.valueOf(servcTax
                                    .equals("") ? "0" : servcTax))),
                    small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            FY_SY_premDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase(
                    commonForAllProd.getRound(commonForAllProd
                            .getStringWithout_E(Double.valueOf(premWthST
                                    .equals("") ? "0" : premWthST))),
                    small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            FY_SY_premDetail_table.addCell(cell);

            // 4 row
            if (!smartwomenbean.getSpnr_PremFreq().equalsIgnoreCase("Single")) {
                cell = new PdfPCell(new Phrase("Second year onwards",
                        small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                FY_SY_premDetail_table.addCell(cell);

                cell = new PdfPCell(new Phrase(smartwomenbean.getSpnr_policyterm() + "", small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                FY_SY_premDetail_table.addCell(cell);

                cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                        .parseDouble(finalPremBasicSA)) + "", small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                FY_SY_premDetail_table.addCell(cell);

                cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                        .parseDouble(finalPremBasicCritiIllness)) + "",
                        small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                FY_SY_premDetail_table.addCell(cell);

                if (smartwomenbean.getIsAPCnCAoption()) {
                    cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                            .parseDouble(finalPremBasicAPCnCA)) + "", small_normal));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    FY_SY_premDetail_table.addCell(cell);
                } else {
                    cell = new PdfPCell(new Phrase("0", small_normal));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    FY_SY_premDetail_table.addCell(cell);
                }

                cell = new PdfPCell(new Phrase(
                        commonForAllProd.getRound(commonForAllProd
                                .getStringWithout_E(Double.valueOf(yearlyPrem
                                        .equals("") ? "0" : yearlyPrem))),
                        small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                FY_SY_premDetail_table.addCell(cell);

                // cell = new PdfPCell(new Phrase(
                // commonForAllProd.getRound(commonForAllProd
                // .getStringWithout_E(Double
                // .valueOf(basicServiceTaxSecondYear
                // .equals("") ? "0"
                // : basicServiceTaxSecondYear))),
                // small_normal));
                // cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                // FY_SY_premDetail_table.addCell(cell);
                //
                // cell = new PdfPCell(new Phrase(
                // commonForAllProd.getRound(commonForAllProd
                // .getStringWithout_E(Double
                // .valueOf(SBCServiceTaxSecondYear
                // .equals("") ? "0"
                // : SBCServiceTaxSecondYear))),
                // small_normal));
                // cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                // FY_SY_premDetail_table.addCell(cell);
                //
                // cell = new PdfPCell(new Phrase(
                // commonForAllProd.getRound(commonForAllProd
                // .getStringWithout_E(Double
                // .valueOf(KKCServiceTaxSecondYear
                // .equals("") ? "0"
                // : KKCServiceTaxSecondYear))),
                // small_normal));
                // cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                // FY_SY_premDetail_table.addCell(cell);

                cell = new PdfPCell(
                        new Phrase(
                                commonForAllProd.getRound(commonForAllProd
                                        .getStringWithout_E(Double
                                                .valueOf(servcTaxSeondYear
                                                        .equals("") ? "0"
                                                        : servcTaxSeondYear))),
                                small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                FY_SY_premDetail_table.addCell(cell);

                cell = new PdfPCell(
                        new Phrase(
                                commonForAllProd
                                        .getRound(commonForAllProd.getStringWithout_E(Double
                                                .valueOf(premWthSTSecondYear
                                                        .equals("") ? "0"
                                                        : premWthSTSecondYear))),
                                small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                FY_SY_premDetail_table.addCell(cell);
            }
            document.add(FY_SY_premDetail_table);
            // Amit changes end 24-5-2016
            document.add(para_img_logo_after_space_1);

            // Table here
            PdfPTable table1 = new PdfPTable(13);
            table1.setWidths(new float[]{2f, 5f, 5f, 5f, 5f, 5f, 5f, 5f, 5f,
                    5f, 5f, 5f, 5f});
            table1.setWidthPercentage(100);

            // 1st row
            cell = new PdfPCell(
                    new Phrase(
                            "BENEFIT ILLUSTRATION FOR SBI LIFE – Smart Women Advantage",
                            small_bold));
            cell.setColspan(13);
            cell.setPadding(5);
            cell.setBorder(Rectangle.BOTTOM | Rectangle.LEFT | Rectangle.RIGHT
                    | Rectangle.TOP);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);

            // 2nd row
            cell = new PdfPCell(new Phrase("End of Year", small_bold1));
            cell.setRowspan(3);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);

            cell = new PdfPCell(new Phrase("Benefit payable on death (in Rs.)",
                    small_bold1));
            cell.setColspan(3);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);

            cell = new PdfPCell(
                    new Phrase("Benefit Payable on Critical illness (in Rs.)",
                            small_bold1));
            cell.setColspan(3);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);

            cell = new PdfPCell(new Phrase(
                    "Benefit Payable at Maturity (in Rs.)", small_bold1));
            cell.setColspan(3);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);

            cell = new PdfPCell(new Phrase(
                    "Benefit Payable at Surrender (in Rs.)", small_bold1));
            cell.setColspan(3);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);

            // 3rd
            cell = new PdfPCell(new Phrase("Guaranteed", small_bold1));
            cell.setRowspan(2);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);

            cell = new PdfPCell(new Phrase("Non Guaranteed Rate of Investment Return", small_bold1));
            cell.setColspan(2);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);

            cell = new PdfPCell(new Phrase("Guaranteed", small_bold1));
            cell.setColspan(3);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);

            cell = new PdfPCell(new Phrase("Guaranteed", small_bold1));
            cell.setRowspan(2);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);

            cell = new PdfPCell(new Phrase("Non Guaranteed Rate of Investment Return", small_bold1));
            cell.setColspan(2);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);

            cell = new PdfPCell(new Phrase("Guaranteed (GSV)", small_bold1));
            cell.setRowspan(2);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);

            cell = new PdfPCell(new Phrase("Non Guaranteed Rate of Investment Return (SSV)", small_bold1));
            cell.setColspan(2);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);

            // 4 row
            cell = new PdfPCell(new Phrase("4% pa", small_bold1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);

            cell = new PdfPCell(new Phrase("8% pa", small_bold1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);

            cell = new PdfPCell(new Phrase("Minor Stage", small_bold1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);

            cell = new PdfPCell(new Phrase("Major Stage *", small_bold1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);

            cell = new PdfPCell(new Phrase("Advanced Stage *", small_bold1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);

            cell = new PdfPCell(new Phrase("4% pa", small_bold1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);

            cell = new PdfPCell(new Phrase("8% pa", small_bold1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);

            cell = new PdfPCell(new Phrase("4% pa", small_bold1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);

            cell = new PdfPCell(new Phrase("8% pa", small_bold1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);

            for (int i = 0; i < smartwomenbean.getSpnr_policyterm(); i++) {
                cell = new PdfPCell(new Phrase(prsObj.parseXmlTag(output,
                        "policyYr" + (i + 1)), small_normal1));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                        | Rectangle.BOTTOM);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table1.addCell(cell);

                cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                        .parseDouble(prsObj.parseXmlTag(output,
                                "guarntedDeathBenft" + (i + 1)))),
                        small_normal1));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                        | Rectangle.BOTTOM);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table1.addCell(cell);

                cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                        .parseDouble(prsObj.parseXmlTag(output,
                                "nonGuarntedDeathBenft_4_pr" + (i + 1)))),
                        small_normal1));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                        | Rectangle.BOTTOM);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table1.addCell(cell);

                cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                        .parseDouble(prsObj.parseXmlTag(output,
                                "nonGuarntedDeathBenft_8_pr" + (i + 1)))),
                        small_normal1));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                        | Rectangle.BOTTOM);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table1.addCell(cell);

                cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                        .parseDouble(prsObj.parseXmlTag(output,
                                "guarateedCriticalIllnessBenefitAt_Minor"
                                        + (i + 1)))), small_normal1));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                        | Rectangle.BOTTOM);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table1.addCell(cell);

                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                        .parseDouble(prsObj.parseXmlTag(output,
                                "guarateedCriticalIllnessBenefitAt_Major"
                                        + (i + 1)))), small_normal1));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                        | Rectangle.BOTTOM);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table1.addCell(cell);

                cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                        .parseDouble(prsObj.parseXmlTag(output,
                                "guarateedCriticalIllnessBenefitAt_Advanced"
                                        + (i + 1)))), small_normal1));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                        | Rectangle.BOTTOM);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table1.addCell(cell);

                cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                        .parseDouble(prsObj.parseXmlTag(output,
                                "guaranteedSurvivalBenefit" + (i + 1)))),
                        small_normal1));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                        | Rectangle.BOTTOM);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table1.addCell(cell);

                cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                        .parseDouble(prsObj.parseXmlTag(output,
                                "nonGuarateedSurvivalBenefitAt_4_Percent"
                                        + (i + 1)))), small_normal1));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                        | Rectangle.BOTTOM);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table1.addCell(cell);

                cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                        .parseDouble(prsObj.parseXmlTag(output,
                                "nonGuarateedSurvivalBenefitAt_8_Percent"
                                        + (i + 1)))), small_normal1));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                        | Rectangle.BOTTOM);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table1.addCell(cell);

                cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                        .parseDouble(prsObj.parseXmlTag(output,
                                "GSV_surrendr_val" + (i + 1)))), small_normal1));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                        | Rectangle.BOTTOM);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table1.addCell(cell);

                cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                        .parseDouble(prsObj.parseXmlTag(output,
                                "NonGSV_surrndr_val_4Percent" + (i + 1)))),
                        small_normal1));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                        | Rectangle.BOTTOM);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table1.addCell(cell);
                cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                        .parseDouble(prsObj.parseXmlTag(output,
                                "NonGSV_surrndr_val_8Percent" + (i + 1)))),
                        small_normal1));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                        | Rectangle.BOTTOM);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table1.addCell(cell);
            }

            document.add(table1);
            PdfPTable BI_Pdftable_bottom = new PdfPTable(1);
            BI_Pdftable_bottom.setWidthPercentage(100);
            PdfPCell BI_Pdftable_bottom_cell1 = new PdfPCell(
                    new Paragraph(
                            "*Any previously paid claim under critical illness would be deducted from Major stage and Advanced stage benefit",
                            small_bold));
            BI_Pdftable_bottom_cell1
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_Pdftable_bottom_cell1.setPadding(5);
            BI_Pdftable_bottom.addCell(BI_Pdftable_bottom_cell1);
            document.add(BI_Pdftable_bottom);

            document.add(para_img_logo_after_space_1);

            // document.add(new_line);
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
                            "1. In case of monthly mode 3 months premiums have to be paid in advance. ",
                            small_normal));
            BI_Pdftable6_cell6.setPadding(5);
            BI_Pdftable6.addCell(BI_Pdftable6_cell6);
            document.add(BI_Pdftable6);

            PdfPTable BI_Pdftable8 = new PdfPTable(1);
            BI_Pdftable8.setWidthPercentage(100);
            PdfPCell BI_Pdftable8_cell1 = new PdfPCell(
                    new Paragraph(
                            "2. The premiums can be paid by giving standing instructions to your bank or you can pay through your credit card.",
                            small_normal));
            BI_Pdftable8_cell1.setPadding(5);
            BI_Pdftable8.addCell(BI_Pdftable8_cell1);
            document.add(BI_Pdftable8);

            PdfPTable BI_Pdftable9 = new PdfPTable(1);
            BI_Pdftable9.setWidthPercentage(100);
            PdfPCell BI_Pdftable9_cell1 = new PdfPCell(
                    new Paragraph(
                            "3. In case of a Major stage claim, future premiums will be waived.",
                            small_normal));
            BI_Pdftable9_cell1.setPadding(5);
            BI_Pdftable9.addCell(BI_Pdftable9_cell1);
            document.add(BI_Pdftable9);

            PdfPTable BI_Pdftable10 = new PdfPTable(1);
            BI_Pdftable10.setWidthPercentage(100);
            PdfPCell BI_Pdftable10_cell1 = new PdfPCell(
                    new Paragraph(
                            "4. The premium towards health benefits are used entirely for providing health cover and would not contribute towards providing maturity  and surrender benefits. Only the savings premium would result into maturity benefit.",
                            small_normal));
            BI_Pdftable10_cell1.setPadding(5);
            BI_Pdftable10.addCell(BI_Pdftable10_cell1);
            document.add(BI_Pdftable10);

            PdfPTable taxes_desc = new PdfPTable(1);
            taxes_desc.setWidthPercentage(100);
            PdfPCell taxes_desc_cell = new PdfPCell(
                    new Paragraph(
                            "5. Applicable Taxes (including surcharge/cess etc), at the rate notified by the Central Government/ State Government / Union Territories of India from time to time and as per the provisions of the prevalent tax laws will be payable on premium/ or any other charges as per the product features. ",

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
            document.add(BI_Pdftable_OtherTermCondition);

            PdfPTable BI_PdftableOtherTermCondition1 = new PdfPTable(1);
            BI_PdftableOtherTermCondition1.setWidthPercentage(100);
            PdfPCell BI_PdftableOtherTermCondition1_cell6 = new PdfPCell(
                    new Paragraph(
                            "1.The benefit calculation is based on the age herein indicated and as applicable for healthy individual."

                            , small_normal));

            BI_PdftableOtherTermCondition1_cell6.setPadding(5);

            BI_PdftableOtherTermCondition1
                    .addCell(BI_PdftableOtherTermCondition1_cell6);
            document.add(BI_PdftableOtherTermCondition1);

            PdfPTable BI_PdftableOtherTermCondition2 = new PdfPTable(1);
            BI_PdftableOtherTermCondition2.setWidthPercentage(100);
            PdfPCell BI_PdftableOtherTermCondition2_cell1 = new PdfPCell(
                    new Paragraph(
                            "2.The Maturity/ Death / Critical Illness / Surrender Benefit amount are derived on the assumption that the policies are in \"full force\"",
                            small_normal));

            BI_PdftableOtherTermCondition2_cell1.setPadding(5);

            BI_PdftableOtherTermCondition2
                    .addCell(BI_PdftableOtherTermCondition2_cell1);
            document.add(BI_PdftableOtherTermCondition2);

            // PdfPTable BI_PdftableOtherTermCondition3 = new PdfPTable(1);
            // BI_PdftableOtherTermCondition3.setWidthPercentage(100);
            // PdfPCell BI_PdftableOtherTermCondition3_cell1 = new PdfPCell(
            // new Paragraph(
            // "3.  Insurance is subject matter of solicitation. ",
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
                            "3.The surrender values shown above are applicable for in-force policies and values are shown at the end of policy year but before the payment of survival benefit of that year.",
                            small_normal));
            BI_PdftableOtherTermCondition4_cell1.setPadding(5);
            BI_PdftableOtherTermCondition4
                    .addCell(BI_PdftableOtherTermCondition4_cell1);
            document.add(BI_PdftableOtherTermCondition4);

            document.add(para_img_logo_after_space_1);

            // Paragraph note = new
            // Paragraph("Please Note: ",normal1_BoldUnderline);
            // note.setAlignment(Element.ALIGN_LEFT);
            // document.add(note);
            //
            // Paragraph note_1 = new
            // Paragraph("1. In case of monthly mode 3 months’ premiums have to be paid in advance. ",small_normal);
            // note_1.setAlignment(Element.ALIGN_LEFT|Element.ALIGN_JUSTIFIED);
            // document.add(note_1);
            //
            // Paragraph note_2 = new
            // Paragraph("2. The premiums can be paid by giving standing instructions to your bank or you can pay through your credit card.",small_normal);
            // note_2.setAlignment(Element.ALIGN_LEFT|Element.ALIGN_JUSTIFIED);
            // document.add(note_2);
            //
            //
            //
            // document.add(new_line);
            // Paragraph termsCondition = new
            // Paragraph("Other Terms and Conditions",small_bold);
            // termsCondition.setAlignment(Element.ALIGN_LEFT);
            // document.add(termsCondition);
            // Paragraph terms_1 = new
            // Paragraph("1.  The benefit calculation is based on the age herein indicated of both lives assured and as applicable for healthy individual.",small_normal);
            // terms_1.setAlignment(Element.ALIGN_LEFT|Element.ALIGN_JUSTIFIED);
            // document.add(terms_1);
            // Paragraph terms_2 = new
            // Paragraph("2.  The Maturity/ Death Benefit amount are derived on the assumption that the policies are in “full force.",small_normal);
            // terms_2.setAlignment(Element.ALIGN_LEFT|Element.ALIGN_JUSTIFIED);
            // document.add(terms_2);
            // Paragraph terms_3 = new
            // Paragraph("3.  In case of simultaneous death, the death benefit of both lives, as applicable, would be paid.",small_normal);
            // terms_3.setAlignment(Element.ALIGN_LEFT|Element.ALIGN_JUSTIFIED);
            // document.add(terms_3);
            // Paragraph terms_4 = new
            // Paragraph("4.  Insurance is subject matter of solicitation. ",small_normal);
            // terms_4.setAlignment(Element.ALIGN_LEFT|Element.ALIGN_JUSTIFIED);
            // document.add(terms_4);
            // Paragraph terms_5 = new
            // Paragraph("5.  The surrender values shown above are applicable for in-force policies and values are shown at the end of policy year.",small_normal);
            // terms_5.setAlignment(Element.ALIGN_LEFT|Element.ALIGN_JUSTIFIED);
            // document.add(terms_5);

            // Paragraph benefitsofDeath = new
            // Paragraph("Benefit payable to the nominee on death",small_bold);
            // benefitsofDeath.setAlignment(Element.ALIGN_LEFT);
            // Paragraph benefitsofDeath1 = new
            // Paragraph("Higher of i) Sum Assured on death# + Vested Simple Reversionary Bonuses + Terminal bonus, if any. OR ii) 105% of all the basic premiums paid.",small_normal);
            // benefitsofDeath.setAlignment(Element.ALIGN_LEFT|Element.ALIGN_JUSTIFIED);
            // Paragraph benefitsofDeath2 = new
            // Paragraph("#For details on Sum Assured on death please refer the Sales Brochure  ",small_normal);
            // benefitsofDeath2.setAlignment(Element.ALIGN_LEFT|Element.ALIGN_JUSTIFIED);

            // document.add(new_line);

            PdfPTable BI_Pdftable_BonusRates = new PdfPTable(1);
            BI_Pdftable_BonusRates.setWidthPercentage(100);
            PdfPCell BI_Pdftable_BonusRates_cell1 = new PdfPCell(new Paragraph(
                    "Bonus Rates", small_bold));
            BI_Pdftable_BonusRates_cell1
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_Pdftable_BonusRates_cell1.setPadding(5);

            BI_Pdftable_BonusRates.addCell(BI_Pdftable_BonusRates_cell1);
            document.add(BI_Pdftable_BonusRates);

            PdfPTable BI_Pdftable_BonusRates1 = new PdfPTable(1);
            BI_Pdftable_BonusRates1.setWidthPercentage(100);
            PdfPCell BI_Pdftable_BonusRates_cell2 = new PdfPCell(
                    new Paragraph(
                            "This is a with profit plan and participates in the profits of the company’s life insurance business. It gets a share of the profits in the form of bonuses as a result of the statutory valuation carried out every year based on the applicable IRDAI regulations.",
                            small_normal));
            BI_Pdftable_BonusRates_cell2.setPadding(5);
            BI_Pdftable_BonusRates1.addCell(BI_Pdftable_BonusRates_cell2);
            document.add(BI_Pdftable_BonusRates1);

            PdfPTable BI_Pdftable_BonusRates2 = new PdfPTable(1);
            BI_Pdftable_BonusRates2.setWidthPercentage(100);
            PdfPCell BI_Pdftable_BonusRates2_cell2 = new PdfPCell(
                    new Paragraph(
                            "Simple reversionary bonuses are declared as a percentage rate, which apply to the basic sum assured of the basic policy. Once declared, they form a part of the guaranteed benefits of the plan. Terminal bonuses, if any, are declared as a percentage rate, which apply to the vested reversionary bonus.",
                            small_normal));
            BI_Pdftable_BonusRates2_cell2.setPadding(5);
            BI_Pdftable_BonusRates2.addCell(BI_Pdftable_BonusRates2_cell2);
            document.add(BI_Pdftable_BonusRates2);

            PdfPTable BI_Pdftable_BonusRates3 = new PdfPTable(1);
            BI_Pdftable_BonusRates3.setWidthPercentage(100);
            PdfPCell BI_Pdftable_BonusRates3_cell2 = new PdfPCell(
                    new Paragraph(
                            "The bonus rates in the benefit illustration are constant. However, in practice, future bonuses are not guaranteed and will depend on future profits. Therefore, the bonuses are shown as non-guaranteed benefits and are calculated so that they are consistent with the two projected investment return assumptions of 4% and 8% per annum.",
                            small_normal));
            BI_Pdftable_BonusRates3_cell2.setPadding(5);
            BI_Pdftable_BonusRates3.addCell(BI_Pdftable_BonusRates3_cell2);
            document.add(BI_Pdftable_BonusRates3);

            PdfPTable BI_Pdftable_BonusRates4 = new PdfPTable(1);
            BI_Pdftable_BonusRates4.setWidthPercentage(100);
            PdfPCell BI_Pdftable_BonusRates4_cell2 = new PdfPCell(
                    new Paragraph(
                            "Accordingly, for the purpose of guaranteed surrender value (GSV) in this illustration, the surrender value of vested bonuses is not considered at all.",
                            small_normal));
            BI_Pdftable_BonusRates4_cell2.setPadding(5);
            BI_Pdftable_BonusRates4.addCell(BI_Pdftable_BonusRates4_cell2);
            document.add(BI_Pdftable_BonusRates4);

            document.add(para_img_logo_after_space_1);

            PdfPTable BI_Pdftable_SurrenderValue = new PdfPTable(1);
            BI_Pdftable_SurrenderValue.setWidthPercentage(100);
            PdfPCell BI_Pdftable_SurrenderValue_cell1 = new PdfPCell(
                    new Paragraph("Surrender Value for Policy", small_bold));
            BI_Pdftable_SurrenderValue_cell1
                    .setHorizontalAlignment(Element.ALIGN_LEFT);
            BI_Pdftable_SurrenderValue_cell1.setPadding(5);

            BI_Pdftable_SurrenderValue
                    .addCell(BI_Pdftable_SurrenderValue_cell1);
            document.add(BI_Pdftable_SurrenderValue);

            // PdfPTable BI_Pdftable_SurrenderValue1 = new PdfPTable(1);
            // BI_Pdftable_SurrenderValue1.setWidthPercentage(100);
            // PdfPCell BI_Pdftable_SurrenderValue1_cell = new PdfPCell(
            // new Paragraph(
            // "Surrender value is available for the basic policy benefits and not for the rider benefits.",
            // small_normal));
            //
            //
            // BI_Pdftable_SurrenderValue1_cell.setPadding(5);
            //
            //
            // BI_Pdftable_SurrenderValue1_cell
            // .setHorizontalAlignment(Element.ALIGN_CENTER);
            // BI_Pdftable_SurrenderValue1
            // .addCell(BI_Pdftable_SurrenderValue1_cell);
            // document.add(BI_Pdftable_SurrenderValue1);
            //
            //
            // document.add(para_img_logo_after_space_1);

            PdfPTable BI_Pdftable_GuaranteedSurrenderValue = new PdfPTable(1);
            BI_Pdftable_GuaranteedSurrenderValue.setWidthPercentage(100);
            PdfPCell BI_Pdftable_GuaranteedSurrenderValue_cell1 = new PdfPCell(
                    new Paragraph("Guaranteed Surrender Value", small_bold));
            BI_Pdftable_GuaranteedSurrenderValue_cell1
                    .setHorizontalAlignment(Element.ALIGN_LEFT);
            BI_Pdftable_GuaranteedSurrenderValue_cell1.setPadding(5);

            BI_Pdftable_GuaranteedSurrenderValue
                    .addCell(BI_Pdftable_GuaranteedSurrenderValue_cell1);
            document.add(BI_Pdftable_GuaranteedSurrenderValue);

            PdfPTable BI_Pdftable_RegularPremiumPolicies = new PdfPTable(1);
            BI_Pdftable_RegularPremiumPolicies.setWidthPercentage(100);
            PdfPCell BI_Pdftable_RegularPremiumPolicies_cell = new PdfPCell(
                    new Paragraph(
                            "The policy will acquire a paid-up and/or surrender value only if premiums have been paid for at least 3 full years. ",
                            small_normal));

            BI_Pdftable_RegularPremiumPolicies_cell.setPadding(5);

            BI_Pdftable_RegularPremiumPolicies
                    .addCell(BI_Pdftable_RegularPremiumPolicies_cell);
            document.add(BI_Pdftable_RegularPremiumPolicies);

            PdfPTable BI_Pdftable_SinglePremiumPolicies = new PdfPTable(1);
            BI_Pdftable_SinglePremiumPolicies.setWidthPercentage(100);
            PdfPCell BI_Pdftable_SinglePremiumPolicies_cell = new PdfPCell(
                    new Paragraph(
                            "The Guaranteed Surrender Value (GSV) will be equal to GSV  factors multiplied by the premiums paid. The premiums in this case will exclude Applicable Taxes, Critical Illness premiums, APC&CA Premiums and extra premium,if any.",
                            small_normal));

            BI_Pdftable_SinglePremiumPolicies_cell.setPadding(5);

            BI_Pdftable_SinglePremiumPolicies
                    .addCell(BI_Pdftable_SinglePremiumPolicies_cell);
            document.add(BI_Pdftable_SinglePremiumPolicies);

            document.add(para_img_logo_after_space_1);

            PdfPTable BI_Pdftable_CompanysPolicySurrender = new PdfPTable(1);
            BI_Pdftable_CompanysPolicySurrender.setWidthPercentage(100);
            PdfPCell BI_Pdftable_CompanysPolicySurrender_cell1 = new PdfPCell(
                    new Paragraph("Company's Policy on Surrender", small_bold));
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
                            "In practice, the company may pay a surrender value which could be higher than the guaranteed surrender value. The benefits payable on surrender reflects the value of your policy, which is assessed based on the past financial/demographic experience of the company with regard to your policy/group of similar policies, as well as the likely future experience. The surrender value payable may be reviewed from time to time depending on company's experience of the various factors which impact the surrender values that may be paid. The surrender value would be higher of GSV or SSV.",
                            small_normal));

            BI_Pdftable_CompanysPolicySurrender1_cell.setPadding(5);

            BI_Pdftable_CompanysPolicySurrender1
                    .addCell(BI_Pdftable_CompanysPolicySurrender1_cell);
            document.add(para_img_logo_after_space_1);

            //Start 16 Jan 2018
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

            PdfPCell BI_Pdftable_CompanysPolicySurrender4_cell = new PdfPCell(
                    new Paragraph(
                            "You have to submit Proof of source of fund.",
                            small_normal));

            BI_Pdftable_CompanysPolicySurrender4_cell.setPadding(5);


            PdfPCell BI_Pdftable_CompanysPolicySurrender5_cell = new PdfPCell(
                    new Paragraph(Company_policy_surrender_dec,
                            small_normal));

            BI_Pdftable_CompanysPolicySurrender5_cell.setPadding(5);

            BI_Pdftable_CompanysPolicySurrender1
                    .addCell(BI_Pdftable_CompanysPolicySurrender4_cell);
            BI_Pdftable_CompanysPolicySurrender1
                    .addCell(BI_Pdftable_CompanysPolicySurrender5_cell);

            document.add(BI_Pdftable_CompanysPolicySurrender1);
            //End 16 Jan 2018

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
            // TODO: handle exception
            // Log.e(getLocalClassName(), e.toString() +
            // "Error in creating Pdf");
            System.out.println("error " + e.getMessage());
            e.printStackTrace();

        }
    }

    private void getInput(SmartWomenAdvantageBean smartWomenAdvantageBean) {

        inputVal = new StringBuilder();

        String LifeAssured_title = spnr_bi_smart_women_advantage_life_assured_title
                .getSelectedItem().toString();
        String LifeAssured_firstName = edt_bi_smart_women_advantage_life_assured_first_name
                .getText().toString();
        String LifeAssured_middleName = edt_bi_smart_women_advantage_life_assured_middle_name
                .getText().toString();
        String LifeAssured_lastName = edt_bi_smart_women_advantage_life_assured_last_name
                .getText().toString();
        String LifeAssured_DOB = btn_bi_smart_women_advantage_life_assured_date
                .getText().toString();
        String LifeAssured_age = edt_bi_smart_women_advantage_life_assured_age
                .getText().toString();
        String LifeAssured_gender = spnr_bi_smart_women_advantage_selGender
                .getSelectedItem().toString();

        String proposer_title = "";
        String proposer_firstName = "";
        String proposer_middleName = "";
        String proposer_lastName = "";
        String proposer_DOB = "";
        String proposer_age = "";
        String proposer_gender = "";

        int age = smartWomenAdvantageBean.getSpnr_Age();

        String gender = smartWomenAdvantageBean.getSpnr_Gender();
        String criticalIllnesOpt = smartWomenAdvantageBean
                .getSpnr_criticalIllnesOpt();
        String plan = smartWomenAdvantageBean.getSpnr_Plan();
        int basicPolicyTerm = smartWomenAdvantageBean.getSpnr_policyterm();
        String PremPayingMode = smartWomenAdvantageBean.getSpnr_PremFreq();
        double basicSumAssured = smartWomenAdvantageBean.getEdt_SumAssured();
        double critiSumAssured = smartWomenAdvantageBean
                .getEdt_critiSumAssured();
        double apcSumAssured = smartWomenAdvantageBean.getEdt_APCSumAssured();
        boolean isJKresident = smartWomenAdvantageBean.getIsjkResident();
        boolean isStaffOrNot = smartWomenAdvantageBean.getIsStaffDisc();
        boolean isAPCnCAoption = smartWomenAdvantageBean.getIsAPCnCAoption();

        boolean smokerOrNot = false;

        String is_Smoker_or_Not = "";
        smokerOrNot = is_Smoker_or_Not.equalsIgnoreCase("Smoker");

        inputVal.append("<?xml version='1.0' encoding='utf-8' ?><smartWomenAdvantageBean>");
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
        inputVal.append("<product_Code>").append(product_Code).append("</product_Code>");
        inputVal.append("<product_UIN>").append(product_UIN).append("</product_UIN>");
        inputVal.append("<product_cateogory>").append(product_cateogory).append("</product_cateogory>");
        inputVal.append("<product_type>").append(product_type).append("</product_type>");

        inputVal.append("<proposer_Is_Same_As_Life_Assured>").append(proposer_Is_Same_As_Life_Assured).append("</proposer_Is_Same_As_Life_Assured>");

        inputVal.append("<isJKResident>").append(isJKresident).append("</isJKResident>");
        inputVal.append("<isStaff>").append(isStaffOrNot).append("</isStaff>");
        inputVal.append("<isSmoker>").append(smokerOrNot).append("</isSmoker>");
        inputVal.append("<age>").append(age).append("</age>");
        inputVal.append("<gender>").append(gender).append("</gender>");
        inputVal.append("<plan>").append(plan).append("</plan>");

        inputVal.append("<policyTerm>").append(basicPolicyTerm).append("</policyTerm>");
        inputVal.append("<criticalIllnesOpt>").append(criticalIllnesOpt).append("</criticalIllnesOpt>");

        inputVal.append("<premFreq>").append(PremPayingMode).append("</premFreq>");
        inputVal.append("<sumAssured>").append(basicSumAssured).append("</sumAssured>");
        inputVal.append("<critiSumAssured>").append(critiSumAssured).append("</critiSumAssured>");
        inputVal.append("<apcSumAssured>").append(apcSumAssured).append("</apcSumAssured>");
        inputVal.append("<isAPCnCAoption>").append(isAPCnCAoption).append("</isAPCnCAoption>");

        inputVal.append("<Product_Cat>").append(product_cateogory).append("</Product_Cat>");

        //Added Tushar Kadam Kerla Applicable Taxes 7-Jun-2019
        String str_kerla_discount = "N";
        if (cb_kerladisc.isChecked()) {
            str_kerla_discount = "Y";
        }

        inputVal.append("<KFC>" + str_kerla_discount + "</KFC>");
        //End Added Tushar Kadam Kerla Applicable Taxes 7-Jun-2019


        inputVal.append("</smartWomenAdvantageBean>");

    }

    private void addListenerOnSubmit() {

        smartWomenAdvantageBean = new SmartWomenAdvantageBean();

        if (cb_staffdisc.isChecked()) {
            smartWomenAdvantageBean.setIsStaffDisc(true);
        } else {
            smartWomenAdvantageBean.setIsStaffDisc(false);
        }

        if (cb_kerladisc.isChecked()) {
            smartWomenAdvantageBean.setKerlaDisc(true);
        } else {
            smartWomenAdvantageBean.setKerlaDisc(false);
        }

        if (cb_bi_smart_women_advantage_JKResident.isChecked()) {
            smartWomenAdvantageBean.setIsjkResident(true);

        } else {
            smartWomenAdvantageBean.setIsjkResident(false);
        }

        smartWomenAdvantageBean
                .setSpnr_Gender(spnr_bi_smart_women_advantage_selGender
                        .getSelectedItem().toString());

        smartWomenAdvantageBean.setSpnr_Age(Integer
                .parseInt(edt_bi_smart_women_advantage_life_assured_age
                        .getText().toString()));

        smartWomenAdvantageBean
                .setSpnr_criticalIllnesOpt(spnr_bi_smart_women_advantage_critical_illness_option
                        .getSelectedItem().toString());
        smartWomenAdvantageBean.setSpnr_Plan(spnr_bi_smart_women_advantage_plan
                .getSelectedItem().toString());

        smartWomenAdvantageBean.setSpnr_policyterm(Integer
                .parseInt(spnr_bi_smart_women_advantage_policyterm
                        .getSelectedItem().toString()));

        smartWomenAdvantageBean
                .setSpnr_PremFreq(spnr_bi_smart_women_advantage_premium_frequency
                        .getSelectedItem().toString());

        smartWomenAdvantageBean.setEdt_SumAssured(Integer
                .parseInt(edt_bi_smart_women_advantage_sum_assured_amount
                        .getText().toString()));

        smartWomenAdvantageBean
                .setEdt_critiSumAssured(Integer
                        .parseInt(edt_bi_smart_women_advantage_critical_illness_sum_assured
                                .getText().toString()));

        if (cb_bi_smart_women_advantage_apc_ca_option.isChecked()) {
            smartWomenAdvantageBean.setIsAPCnCAoption(true);

        } else {
            smartWomenAdvantageBean.setIsAPCnCAoption(false);
        }

        if (cb_bi_smart_women_advantage_apc_ca_option.isChecked()) {
            smartWomenAdvantageBean
                    .setEdt_APCSumAssured(Integer
                            .parseInt(edt_bi_smart_women_advantage_apc_ca_option_sum_assured
                                    .getText().toString()));
        } else {
            smartWomenAdvantageBean.setEdt_APCSumAssured(0);
        }

        showsmartWomenAdvantageOutputPg(smartWomenAdvantageBean);

    }

    private void showsmartWomenAdvantageOutputPg(
            SmartWomenAdvantageBean smartWomenAdvantageBean) {

        String[] outputArr = getOutput("BI_of_Smart_Women_Advantage",
                smartWomenAdvantageBean);
        valPremAmount = valPremAmount(Double.parseDouble(outputArr[3]));
        if (valPremAmount) {
            try {
                retVal.append("<?xml version='1.0' encoding='utf-8' ?><SmartWomenAdvantage>");
                retVal.append("<errCode>0</errCode>");

                /******************* added by Akshaya on 26-Feb-2016 Start ********************/

                if (cb_staffdisc.isChecked()) {

                    retVal.append("<staffStatus>sbi</staffStatus>" + "<staffRebate>").append(outputArr[13]).append("</staffRebate>");
                } else {
                    retVal.append("<staffStatus>none</staffStatus>"
                            + "<staffRebate>" + "0" + "</staffRebate>");
                }

                retVal.append("<FYTotServiceTax>").append(outputArr[14]).append("</FYTotServiceTax>");
                retVal.append("<InstmntPrem>").append(outputArr[15]).append("</InstmntPrem>");
                retVal.append("<basicPremWithoutDisc>").append(outputArr[16]).append("</basicPremWithoutDisc>");
                retVal.append("<CritiPremWithoutDisc>").append(outputArr[17]).append("</CritiPremWithoutDisc>");
                retVal.append("<ApcPremWithoutDisc>").append(outputArr[18]).append("</ApcPremWithoutDisc>");
                retVal.append("<basicPremWithoutDiscSA>").append(outputArr[19]).append("</basicPremWithoutDiscSA>");
                retVal.append("<CritiPremWithoutDiscSA>").append(outputArr[21]).append("</CritiPremWithoutDiscSA>");
                retVal.append("<ApcPremWithoutDiscSA>").append(outputArr[22]).append("</ApcPremWithoutDiscSA>");
                retVal.append("<OccuInt>").append(outputArr[20]).append("</OccuInt>");
                retVal.append("<KeralaCessServiceTax>" + outputArr[29] + "</KeralaCessServiceTax>");
                retVal.append("<KeralaCessServiceTaxSecondYear>" + outputArr[30] + "</KeralaCessServiceTaxSecondYear>");

                /******************* added by Akshaya on 26-Feb-2016 End ********************/

                retVal.append("<finalPremBasicSA>").append(outputArr[0]).append("</finalPremBasicSA>").append("<finalPremBasicCritiIllness>").append(outputArr[1]).append("</finalPremBasicCritiIllness>").append("<finalPremBasicAPCnCA>").append(outputArr[2]).append("</finalPremBasicAPCnCA>").append("<yearlyPrem>").append(outputArr[3]).append("</yearlyPrem>").append("<servcTax>").append(outputArr[6]).append("</servcTax>").append("<premWthST>").append(outputArr[4]).append("</premWthST>").append("<servcTaxSeondYear>").append(outputArr[7]).append("</servcTaxSeondYear>").append("<guarMatBen>").append(outputArr[8]).append("</guarMatBen>")
                        .append("<nonguraMatBen4>").append(outputArr[9]).append("</nonguraMatBen4>")
                        .append("<nonguraMatBen8>").append(outputArr[10]).append("</nonguraMatBen8>").append("<premWthSTSecondYear>").append(outputArr[5]).append("</premWthSTSecondYear>").append("<cessFirstYear>").append(outputArr[11] == null ? 0 : outputArr[11]).append("</cessFirstYear>").append("<cessSeondYear>").append(outputArr[12] == null ? 0 : outputArr[12]).append("</cessSeondYear>").append("<basicServiceTax>").append(outputArr[23]).append("</basicServiceTax>").append("<SBCServiceTax>").append(outputArr[24]).append("</SBCServiceTax>").append("<KKCServiceTax>").append(outputArr[25]).append("</KKCServiceTax>").append("<basicServiceTaxSecondYear>").append(outputArr[26]).append("</basicServiceTaxSecondYear>").append("<SBCServiceTaxSecondYear>").append(outputArr[27]).append("</SBCServiceTaxSecondYear>").append("<KKCServiceTaxSecondYear>").append(outputArr[28]).append("</KKCServiceTaxSecondYear>");

                int index = smartWomenAdvantageBean.getSpnr_policyterm();

                retVal.append("<nonGuarateedSurvivalBenefitAt_4_Percent" + index + ">" + outputArr[9] + "</nonGuarateedSurvivalBenefitAt_4_Percent" + index + ">");
                retVal.append("<nonGuarateedSurvivalBenefitAt_8_Percent" + index + ">" + outputArr[10] + "</nonGuarateedSurvivalBenefitAt_8_Percent" + index + ">");

                retVal.append(bussIll.toString());
                retVal.append("</SmartWomenAdvantage>");
                System.out.println("output " + retVal.toString());
            } catch (Exception e) {
                retVal.append("<?xml version='1.0' encoding='utf-8' ?><SmartWomenAdvantage>" + "<errCode>1</errCode>" + "<errorMessage>").append(e.getMessage()).append("</errorMessage></SmartWomenAdvantage>");
            }
        }
    }

    /********************************** Output ends here **********************************************************/

    private String[] getOutput(String sheetName,
                               SmartWomenAdvantageBean smartwomenadvantagebean) {
        try {
            int _year_F = 0;
            bussIll = new StringBuilder();

            int year_F = 0;

            String totalBasePremiumPaid = null, guaranteedDeathBenefit = null, nonGuarateedDeathBenefitAt_4_Percent = null, nonGuarateedDeathBenefitAt_8_Percent = null, guaranteedSurvivalBenefit = null, nonGuarateedSurvivalBenefitAt_4_Percent = null, nonGuarateedSurvivalBenefitAt_8_Percent = null, guarateedCriticalIllnessBenefitAt_Minor = null, guarateedCriticalIllnessBenefitAt_Major = null, guarateedCriticalIllnessBenefitAt_Advanced = null, paidUpValue = null, GSV_surrendr_val = null, NonGSV_surrndr_val_4Percent = null, NonGSV_surrndr_val_8Percent = null, FY_ST = null, FY_SBCserviceTax = "0", SY_ST = null, SY_SBCserviceTax = "0", SAMFguaranteedDeathBenefit = null;

            SmartWomenAdvantageBusinessLogic swaBusinessLogic = new SmartWomenAdvantageBusinessLogic(
                    smartwomenadvantagebean);
            SmartWomenAdvantageProperties prop = new SmartWomenAdvantageProperties();

            /********************** added by Akshaya on 26-Feb-2016 Start ********************/
            String MinesOccuInterest = "", servicetax_MinesOccuInterest = "";
            String staffRebate = String
                    .valueOf(swaBusinessLogic.getStaffDisc());
            /******************* added by Akshaya on 26-Feb-2016 End ********************/

            String finalPremBasicSA = swaBusinessLogic.setFinalPremiumBasicSA();
            String finalPremBasicCritiIllness = swaBusinessLogic
                    .setFinalPremiumBasicCritiIllness();
            String finalPremBasicAPCnCA = swaBusinessLogic
                    .setFinalPremiumBasicAPCnCA();

            /******************* added by Akshaya on 26-Feb-2016 Start ********************/

            String finalPremBasicSA_withoutStaff = swaBusinessLogic
                    .setFinalPremiumBasicSA_withoutStaff();
            String finalPremBasicCritiIllness_withoutStaff = swaBusinessLogic
                    .setFinalPremiumBasicCritiIllness_withoutStaff();
            String finalPremBasicAPCnCA_withoutStaff = swaBusinessLogic
                    .setFinalPremiumBasicAPCnCA_withoutStaff();

            String totalFinalPremium__withoutStaff = ""
                    + (Double.parseDouble(finalPremBasicSA_withoutStaff)
                    + Double.parseDouble(finalPremBasicCritiIllness_withoutStaff) + Double
                    .parseDouble(finalPremBasicAPCnCA_withoutStaff));

            String finalPremBasicSA_withoutStaffSA = commonForAllProd
                    .getRoundOffLevel2(swaBusinessLogic
                            .setFinalPremiumBasicSA_withoutStaffSA());

            /******************* added by Akshaya on 26-Feb-2016 End ********************/

            /******************* added by Akshaya on 26-APR-2016 Start ********************/
            String finalPremBasicCritiIllness_withoutStaffSA = commonForAllProd
                    .getRoundOffLevel2(swaBusinessLogic
                            .setFinalPremiumBasicCritiIllness_withoutStaffSA());

            String finalPremBasicAPCnCA_withoutStaffSA = swaBusinessLogic
                    .setFinalPremiumBasicAPCnCA_withoutStaffSA();

            // String finalPremBasicAPCnCA_withoutStaffSA = commonForAllProd
            // .getRoundOffLevel2(swaBusinessLogic
            // .setFinalPremiumBasicAPCnCA_withoutStaffSA());
            /******************* added by Akshaya on 26-APR-2016 End ********************/

            String finalPremBasicSAWithRoundup = commonForAllProd
                    .getRoundUp(commonForAllProd
                            .getRoundOffLevel2(commonForAllProd
                                    .getStringWithout_E(Double
                                            .parseDouble(finalPremBasicSA))));
            String finalPremBasicCritiIllnessWithRoundup = commonForAllProd
                    .getRoundUp(commonForAllProd.getRoundOffLevel2(commonForAllProd
                            .getStringWithout_E(Double
                                    .parseDouble(finalPremBasicCritiIllness))));
            String finalPremBasicAPCnCAWithRoundup = commonForAllProd
                    .getRoundUp(commonForAllProd.getRoundOffLevel2(commonForAllProd
                            .getStringWithout_E(Double
                                    .parseDouble(finalPremBasicAPCnCA))));

            String totalFinalPremium = commonForAllProd
                    .getRoundUp(commonForAllProd.getStringWithout_E((Double
                            .parseDouble(finalPremBasicSAWithRoundup)
                            + Double.parseDouble(finalPremBasicCritiIllnessWithRoundup) + Double
                            .parseDouble(finalPremBasicAPCnCAWithRoundup))));

            /*************************** Added by Akshaya on 03-MAR-15 start **********************/

            // if(selOccupationMines.isChecked())
            // {
            MinesOccuInterest = ""
                    + swaBusinessLogic
                    .getMinesOccuInterest(smartwomenadvantagebean
                            .getEdt_SumAssured());

            servicetax_MinesOccuInterest = ""
                    + swaBusinessLogic
                    .getServiceTaxMines(Double.parseDouble(MinesOccuInterest));

            MinesOccuInterest = "" + (Double.parseDouble(MinesOccuInterest) + Double.parseDouble(servicetax_MinesOccuInterest));
            // }
            // else
            // {
            // MinesOccuInterest="0";
            // }
            //
            // totalFinalPremium =
            // commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(Double.parseDouble(totalFinalPremium)
            // + Double.parseDouble(MinesOccuInterest)));

            /*************************** Added by Akshaya on 03-MAR-15 end **********************/

            String premiumWithST = null, premiumWithSTSecondYear = null, FYServiceTax = null, SYServiceTax = null;

            /*** modified by Akshaya on 20-MAY-16 start **/
			/*double basicServiceTax = swaBusinessLogic.getServiceTax(
					Double.parseDouble(totalFinalPremium),
					smartwomenadvantagebean.getIsjkResident(), "basic");
			double SBCServiceTax = swaBusinessLogic.getServiceTax(
					Double.parseDouble(totalFinalPremium),
					smartwomenadvantagebean.getIsjkResident(), "SBC");
			double KKCServiceTax = swaBusinessLogic.getServiceTax(
					Double.parseDouble(totalFinalPremium),
					smartwomenadvantagebean.getIsjkResident(), "KKC");*/

            double basicServiceTax = 0;
            double SBCServiceTax = 0;
            double KKCServiceTax = 0;

            double KerlaServiceTax = 0;
            double KeralaCessServiceTax = 0;
            boolean isKerlaDiscount = smartwomenadvantagebean.isKerlaDisc();

            if (isKerlaDiscount) {
                basicServiceTax = swaBusinessLogic.getServiceTax(Double.parseDouble(totalFinalPremium), smartwomenadvantagebean.getIsjkResident(), "basic");
                KerlaServiceTax = swaBusinessLogic.getServiceTax(Double.parseDouble(totalFinalPremium), smartwomenadvantagebean.getIsjkResident(), "KERALA");
                KeralaCessServiceTax = KerlaServiceTax - basicServiceTax;
            } else {
                basicServiceTax = swaBusinessLogic.getServiceTax(Double.parseDouble(totalFinalPremium), smartwomenadvantagebean.getIsjkResident(), "basic");
                SBCServiceTax = swaBusinessLogic.getServiceTax(Double.parseDouble(totalFinalPremium), smartwomenadvantagebean.getIsjkResident(), "SBC");
                KKCServiceTax = swaBusinessLogic.getServiceTax(Double.parseDouble(totalFinalPremium), smartwomenadvantagebean.getIsjkResident(), "KKC");
            }


            FY_ST = commonForAllProd.getStringWithout_E(basicServiceTax
                    + SBCServiceTax + KKCServiceTax + KerlaServiceTax);

            premiumWithST = commonForAllProd
                    .getStringWithout_E(Double.parseDouble(FY_ST)
                            + Double.parseDouble(totalFinalPremium));

			/*double basicServiceTaxSecondYear = swaBusinessLogic
					.getServiceTaxSecondYear(
							Double.parseDouble(totalFinalPremium),
							smartwomenadvantagebean.getIsjkResident(), "basic");
			double SBCServiceTaxSecondYear = swaBusinessLogic
					.getServiceTaxSecondYear(
							Double.parseDouble(totalFinalPremium),
							smartwomenadvantagebean.getIsjkResident(), "SBC");
			double KKCServiceTaxSecondYear = swaBusinessLogic
					.getServiceTaxSecondYear(
							Double.parseDouble(totalFinalPremium),
							smartwomenadvantagebean.getIsjkResident(), "KKC");*/

            double basicServiceTaxSecondYear = 0;
            double SBCServiceTaxSecondYear = 0;
            double KKCServiceTaxSecondYear = 0;

            double kerlaServiceTaxSecondYear = 0;
            double KeralaCessServiceTaxSecondYear = 0;

            if (isKerlaDiscount) {
                basicServiceTaxSecondYear = swaBusinessLogic.getServiceTaxSecondYear(Double.parseDouble(totalFinalPremium), smartwomenadvantagebean.getIsjkResident(), "basic");
                kerlaServiceTaxSecondYear = swaBusinessLogic.getServiceTaxSecondYear(Double.parseDouble(totalFinalPremium), smartwomenadvantagebean.getIsjkResident(), "KERALA");
                KeralaCessServiceTaxSecondYear = kerlaServiceTaxSecondYear - basicServiceTaxSecondYear;
            } else {
                basicServiceTaxSecondYear = swaBusinessLogic.getServiceTaxSecondYear(Double.parseDouble(totalFinalPremium), smartwomenadvantagebean.getIsjkResident(), "basic");
                SBCServiceTaxSecondYear = swaBusinessLogic.getServiceTaxSecondYear(Double.parseDouble(totalFinalPremium), smartwomenadvantagebean.getIsjkResident(), "SBC");
                KKCServiceTaxSecondYear = swaBusinessLogic.getServiceTaxSecondYear(Double.parseDouble(totalFinalPremium), smartwomenadvantagebean.getIsjkResident(), "KKC");
            }


            SY_ST = commonForAllProd
                    .getStringWithout_E(basicServiceTaxSecondYear
                            + SBCServiceTaxSecondYear + KKCServiceTaxSecondYear + kerlaServiceTaxSecondYear);

            premiumWithSTSecondYear = commonForAllProd
                    .getStringWithout_E(Double.parseDouble(SY_ST)
                            + Double.parseDouble(totalFinalPremium));

            // if (smartwomenadvantagebean.getIsjkResident() == true) {
            // premiumWithST = commonForAllProd
            // .getRound((prop.serviceTaxJKResident + 1)
            // * Double.parseDouble(totalFinalPremium) + "");
            // premiumWithSTSecondYear = commonForAllProd
            // .getRound((prop.serviceTaxJKResident + 1)
            // * Double.parseDouble(totalFinalPremium) + "");
            // FY_ST = (Integer.parseInt(premiumWithST) - Integer
            // .parseInt(totalFinalPremium)) + "";
            // SY_ST = (Integer.parseInt(premiumWithSTSecondYear) - Integer
            // .parseInt(totalFinalPremium)) + "";
            // } else {
            // FY_ST = commonForAllProd
            // .getRound(""
            // + (Double.parseDouble(totalFinalPremium) * prop.FY_serviceTax));
            // FY_SBCserviceTax = commonForAllProd
            // .getRound(""
            // + (Double.parseDouble(totalFinalPremium) *
            // prop.FY_CessServiceTax));
            //
            // FYServiceTax = Integer.parseInt(FY_ST)
            // + Integer.parseInt(FY_SBCserviceTax) + "";
            // premiumWithST = (Integer.parseInt(totalFinalPremium) + Integer
            // .parseInt(FYServiceTax)) + "";
            //
            // SY_ST = commonForAllProd
            // .getRound(""
            // + (Double.parseDouble(totalFinalPremium) * prop.SY_serviceTax));
            // SY_SBCserviceTax = commonForAllProd
            // .getRound(""
            // + (Double.parseDouble(totalFinalPremium) *
            // prop.SY_CessServiceTax));
            //
            // SYServiceTax = Integer.parseInt(SY_ST)
            // + Integer.parseInt(SY_SBCserviceTax) + "";
            // premiumWithSTSecondYear = (Integer.parseInt(totalFinalPremium) +
            // Integer
            // .parseInt(SYServiceTax)) + "";
            // }

            /* modified by Akshaya on 20-MAY-16 End **/

            int rowNumber = 0;
            double totalBasePrem = Double.parseDouble(totalFinalPremium)
                    * swaBusinessLogic.getPremiumMultiFactor();

            for (int i = 0; i < smartwomenadvantagebean.getSpnr_policyterm(); i++) {
                rowNumber++;

                year_F = rowNumber;
                _year_F = year_F;
                System.out.println("1. year_F " + year_F);

                swaBusinessLogic.setTotalBasePremiumPaid(totalBasePrem, year_F);
                totalBasePremiumPaid = commonForAllProd
                        .stringWithoutE(swaBusinessLogic
                                .getTotalBasePremiumPaid() + "");
                System.out.println("2.Total Base Premium Paid "
                        + totalBasePremiumPaid);
                bussIll.append("<policyYr").append(_year_F).append(">").append(_year_F).append("</policyYr").append(_year_F).append(">");

                swaBusinessLogic.setSAMFGuaranteedDeathBenefit(year_F);
                SAMFguaranteedDeathBenefit = commonForAllProd
                        .stringWithoutE(swaBusinessLogic
                                .getGuaranteedDeathBenefit() + "");
                System.out.println("3.Guaranteed Death Benefit "
                        + SAMFguaranteedDeathBenefit);

                swaBusinessLogic.setGuaranteedDeathBenefit(year_F,
                        finalPremBasicSAWithRoundup);
                guaranteedDeathBenefit = commonForAllProd
                        .stringWithoutE(swaBusinessLogic
                                .getGuaranteedDeathBenefit() + "");
                System.out.println("3. SAMF Guaranteed Death Benefit "
                        + guaranteedDeathBenefit);
                bussIll.append("<guarntedDeathBenft").append(_year_F).append(">").append(guaranteedDeathBenefit).append("</guarntedDeathBenft").append(_year_F).append(">");

                swaBusinessLogic
                        .setNonGuarateedDeathBenefitAt_4_Percent(year_F);
                nonGuarateedDeathBenefitAt_4_Percent = commonForAllProd
                        .getRound(commonForAllProd.stringWithoutE(swaBusinessLogic
                                .getNonGuarateedDeathBenefitAt_4_Percent() + ""));
                System.out
                        .println("4.Non Guarateed Death Benefit At_4_Percent "
                                + nonGuarateedDeathBenefitAt_4_Percent);
                bussIll.append("<nonGuarntedDeathBenft_4_pr").append(_year_F).append(">").append(nonGuarateedDeathBenefitAt_4_Percent).append("</nonGuarntedDeathBenft_4_pr").append(_year_F).append(">");

                swaBusinessLogic
                        .setNonGuarateedDeathBenefitAt_8_Percent(year_F);
                nonGuarateedDeathBenefitAt_8_Percent = commonForAllProd
                        .getRound(commonForAllProd.stringWithoutE(swaBusinessLogic
                                .getNonGuarateedDeathBenefitAt_8_Percent() + ""));
                System.out
                        .println("5.Non Guarateed Death Benefit At_8_Percent "
                                + nonGuarateedDeathBenefitAt_8_Percent);
                bussIll.append("<nonGuarntedDeathBenft_8_pr").append(_year_F).append(">").append(nonGuarateedDeathBenefitAt_8_Percent).append("</nonGuarntedDeathBenft_8_pr").append(_year_F).append(">");

                swaBusinessLogic
                        .setGuarateedCriticalIllnessBenefitAt_Minor(year_F);
                guarateedCriticalIllnessBenefitAt_Minor = commonForAllProd
                        .stringWithoutE(swaBusinessLogic
                                .getGuarateedCriticalIllnessBenefitAt_Minor()
                                + "");
                System.out
                        .println("6. guarateedCriticalIllnessBenefitAt_Minor "
                                + guarateedCriticalIllnessBenefitAt_Minor);
                bussIll.append("<guarateedCriticalIllnessBenefitAt_Minor").append(_year_F).append(">").append(guarateedCriticalIllnessBenefitAt_Minor).append("</guarateedCriticalIllnessBenefitAt_Minor").append(_year_F).append(">");

                swaBusinessLogic
                        .setGuarateedCriticalIllnessBenefitAt_Major(year_F);
                guarateedCriticalIllnessBenefitAt_Major = commonForAllProd
                        .stringWithoutE(swaBusinessLogic
                                .getGuarateedCriticalIllnessBenefitAt_Major()
                                + "");
                System.out
                        .println("7. guarateedCriticalIllnessBenefitAt_Major  "
                                + guarateedCriticalIllnessBenefitAt_Major);
                bussIll.append("<guarateedCriticalIllnessBenefitAt_Major").append(_year_F).append(">").append(guarateedCriticalIllnessBenefitAt_Major).append("</guarateedCriticalIllnessBenefitAt_Major").append(_year_F).append(">");

                swaBusinessLogic
                        .setGuarateedCriticalIllnessBenefitAt_Advanced(year_F);
                guarateedCriticalIllnessBenefitAt_Advanced = commonForAllProd
                        .stringWithoutE(swaBusinessLogic
                                .getGuarateedCriticalIllnessBenefitAt_Advanced()
                                + "");
                System.out
                        .println("8.guarateedCriticalIllnessBenefitAt_Advanced "
                                + guarateedCriticalIllnessBenefitAt_Advanced);
                bussIll.append("<guarateedCriticalIllnessBenefitAt_Advanced").append(_year_F).append(">").append(guarateedCriticalIllnessBenefitAt_Advanced).append("</guarateedCriticalIllnessBenefitAt_Advanced").append(_year_F).append(">");

                swaBusinessLogic.setGuaranteedSurvivalBenefit(year_F);
                guaranteedSurvivalBenefit = commonForAllProd
                        .stringWithoutE(swaBusinessLogic
                                .getGuaranteedSurvivalBenefit() + "");
                System.out.println("9.Guaranteed Survival Benefit "
                        + guaranteedSurvivalBenefit);
                bussIll.append("<guaranteedSurvivalBenefit").append(_year_F).append(">").append(guaranteedSurvivalBenefit).append("</guaranteedSurvivalBenefit").append(_year_F).append(">");

                swaBusinessLogic
                        .setNonGuarateedSurvivalBenefitAt_4_Percent(year_F);
                nonGuarateedSurvivalBenefitAt_4_Percent = commonForAllProd
                        .stringWithoutE(swaBusinessLogic
                                .getNonGuarateedSurvivalBenefitAt_4_Percent()
                                + "");
                System.out
                        .println("10.Non Guarateed Survival Benefit At_4_Percent "
                                + nonGuarateedSurvivalBenefitAt_4_Percent);
                bussIll.append("<nonGuarateedSurvivalBenefitAt_4_Percent").append(_year_F).append(">").append(nonGuarateedSurvivalBenefitAt_4_Percent).append("</nonGuarateedSurvivalBenefitAt_4_Percent").append(_year_F).append(">");

                swaBusinessLogic
                        .setNonGuarateedSurvivalBenefitAt_8_Percent(year_F);
                nonGuarateedSurvivalBenefitAt_8_Percent = commonForAllProd
                        .stringWithoutE(swaBusinessLogic
                                .getNonGuarateedSurvivalBenefitAt_8_Percent()
                                + "");
                System.out
                        .println("11.Non Guarateed Survival Benefit At_8_Percent "
                                + nonGuarateedSurvivalBenefitAt_8_Percent);
                bussIll.append("<nonGuarateedSurvivalBenefitAt_8_Percent").append(_year_F).append(">").append(nonGuarateedSurvivalBenefitAt_8_Percent).append("</nonGuarateedSurvivalBenefitAt_8_Percent").append(_year_F).append(">");

                swaBusinessLogic.setPaidUpValue(year_F);
                paidUpValue = commonForAllProd.stringWithoutE(swaBusinessLogic
                        .getPaidUpValue() + "");
                System.out.println("12.paidUpValue " + paidUpValue);

                swaBusinessLogic.setGSV_SurrenderValue(year_F,
                        Double.parseDouble(finalPremBasicSAWithRoundup));
                GSV_surrendr_val = commonForAllProd.getRoundUp(commonForAllProd
                        .getRoundOffLevel2(commonForAllProd
                                .stringWithoutE(swaBusinessLogic
                                        .getGSV_SurrenderValue() + "")));
                System.out.println("GSV_surrendr_val " + GSV_surrendr_val);
                bussIll.append("<GSV_surrendr_val").append(_year_F).append(">").append(GSV_surrendr_val).append("</GSV_surrendr_val").append(_year_F).append(">");

                swaBusinessLogic.setNonGSV_surrndr_val_4_Percent(year_F);
                NonGSV_surrndr_val_4Percent = commonForAllProd
                        .getRoundUp(commonForAllProd
                                .stringWithoutE(swaBusinessLogic
                                        .getNonGSV_surrndr_val_4_Percent() + ""));
                System.out.println("NonGSV_surrndr_val_4Percent "
                        + NonGSV_surrndr_val_4Percent);
                bussIll.append("<NonGSV_surrndr_val_4Percent").append(_year_F).append(">").append(NonGSV_surrndr_val_4Percent).append("</NonGSV_surrndr_val_4Percent").append(_year_F).append(">");

                swaBusinessLogic.setNonGSV_surrndr_val_8_Percent(year_F);
                NonGSV_surrndr_val_8Percent = commonForAllProd
                        .getRoundUp(commonForAllProd
                                .stringWithoutE(swaBusinessLogic
                                        .getNonGSV_surrndr_val_8_Percent() + ""));
                System.out.println("NonGSV_surrndr_val_8Percent "
                        + NonGSV_surrndr_val_8Percent);
                bussIll.append("<NonGSV_surrndr_val_8Percent").append(_year_F).append(">").append(NonGSV_surrndr_val_8Percent).append("</NonGSV_surrndr_val_8Percent").append(_year_F).append(">");

            }

            /******************* added by Akshaya on 26-Feb-2016 Start ********************/

            String FYTotServiceTax = String.valueOf(Double.parseDouble(FY_ST)
                    + Double.parseDouble(FY_SBCserviceTax));

            /******************* added by Akshaya on 26-Feb-2016 Start ********************/
            return new String[]{
                    finalPremBasicSAWithRoundup,
                    finalPremBasicCritiIllnessWithRoundup,
                    finalPremBasicAPCnCAWithRoundup,
                    totalFinalPremium,
                    premiumWithST,
                    premiumWithSTSecondYear,
                    FY_ST,
                    SY_ST,
                    guaranteedDeathBenefit,
                    nonGuarateedSurvivalBenefitAt_4_Percent,
                    nonGuarateedSurvivalBenefitAt_8_Percent,
                    FY_SBCserviceTax,
                    SY_SBCserviceTax,
                    staffRebate,
                    FYTotServiceTax,
                    totalFinalPremium__withoutStaff,
                    finalPremBasicSA_withoutStaff,
                    finalPremBasicCritiIllness_withoutStaff,
                    finalPremBasicAPCnCA_withoutStaff,
                    finalPremBasicSA_withoutStaffSA,
                    MinesOccuInterest,
                    finalPremBasicCritiIllness_withoutStaffSA,
                    finalPremBasicAPCnCA_withoutStaffSA,
                    commonForAllProd.getStringWithout_E(basicServiceTax),
                    commonForAllProd.getStringWithout_E(SBCServiceTax),
                    commonForAllProd.getStringWithout_E(KKCServiceTax),
                    commonForAllProd
                            .getStringWithout_E(basicServiceTaxSecondYear),
                    commonForAllProd
                            .getStringWithout_E(SBCServiceTaxSecondYear),
                    commonForAllProd
                            .getStringWithout_E(KKCServiceTaxSecondYear),
                    servicetax_MinesOccuInterest,
                    commonForAllProd.getStringWithout_E(KeralaCessServiceTax),
                    commonForAllProd.getStringWithout_E(KeralaCessServiceTaxSecondYear)
            };

            /******************* added by Akshaya on 26-Feb-2016 End ********************/

            // return new
            // String[]{finalPremBasicSAWithRoundup,finalPremBasicCritiIllnessWithRoundup,finalPremBasicAPCnCAWithRoundup,totalFinalPremium,premiumWithST,premiumWithSTSecondYear,FY_ST,SY_ST,guaranteedDeathBenefit,nonGuarateedSurvivalBenefitAt_4_Percent,nonGuarateedSurvivalBenefitAt_8_Percent,FY_SBCserviceTax,SY_SBCserviceTax};
        } catch (Exception e) {

            e.printStackTrace();

        }
        return null;
    }

    private void updateCritiSumAssured() {
        try {
            if (!edt_bi_smart_women_advantage_sum_assured_amount.getText()
                    .toString().equals("")) {
                int sumAssured = Integer
                        .parseInt(edt_bi_smart_women_advantage_sum_assured_amount
                                .getText().toString());
                if (spnr_bi_smart_women_advantage_critical_illness_option
                        .getSelectedItem().toString().equals("1"))
                    edt_bi_smart_women_advantage_critical_illness_sum_assured
                            .setText(sumAssured + "");
                else if (spnr_bi_smart_women_advantage_critical_illness_option
                        .getSelectedItem().toString().equals("2"))
                    edt_bi_smart_women_advantage_critical_illness_sum_assured
                            .setText((sumAssured * 2) + "");
                else if (spnr_bi_smart_women_advantage_critical_illness_option
                        .getSelectedItem().toString().equals("3"))
                    edt_bi_smart_women_advantage_critical_illness_sum_assured
                            .setText((sumAssured * 3) + "");
            } else
                edt_bi_smart_women_advantage_critical_illness_sum_assured
                        .setText(0 + "");
        } catch (Exception ignored) {
        }
    }

    private boolean valEligibilityACPnCA() {
        String error = "";
        int age = 0;
        if (!(edt_bi_smart_women_advantage_life_assured_age.getText()
                .toString().equalsIgnoreCase(""))) {
            age = Integer
                    .parseInt(edt_bi_smart_women_advantage_life_assured_age
                            .getText().toString());
        }

        if (cb_bi_smart_women_advantage_apc_ca_option.isChecked()) {
            if (age >= 18
                    && age <= 40
                    && (age + Integer
                    .parseInt(spnr_bi_smart_women_advantage_policyterm
                            .getSelectedItem().toString())) < 46)
                error = "";
            else
                // error = "Not Eligible for APC & CA Option.";
                error = "Maturity age for APC & CA option is 45 Years";
        }

        if (!error.equals("")) {
            showAlert.setMessage(error);
            showAlert.setNeutralButton("OK",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            cb_bi_smart_women_advantage_apc_ca_option
                                    .setChecked(false);
                        }
                    });
            showAlert.setCancelable(false);
            showAlert.show();
            return false;
        } else
            return true;

    }

    private void updateAPCnCPSumAssured() {
        try {
            if (!edt_bi_smart_women_advantage_sum_assured_amount.getText()
                    .toString().equals("")) {
                double result = Double
                        .parseDouble(edt_bi_smart_women_advantage_sum_assured_amount
                                .getText().toString()) * 0.2;
                edt_bi_smart_women_advantage_apc_ca_option_sum_assured
                        .setText((obj.getRound(result + "")));
            } else
                edt_bi_smart_women_advantage_apc_ca_option_sum_assured
                        .setText(0 + "");
        } catch (Exception ignored) {
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

    private boolean valSumAssured() {
        String error = "";
        if (edt_bi_smart_women_advantage_sum_assured_amount.getText()
                .toString().equals("")) {
            error = "Please enter Sum Assured in Rs.";

        } else if (Integer
                .parseInt(edt_bi_smart_women_advantage_sum_assured_amount
                        .getText().toString()) > prop.maxSumAssured) {
            error = "Sum Assured should not be greater than Rs."
                    + currencyFormat.format(prop.maxSumAssured);

        } else if (Integer
                .parseInt(edt_bi_smart_women_advantage_sum_assured_amount
                        .getText().toString()) < prop.minSumAssured) {
            error = "Sum Assured should not be less than Rs."
                    + currencyFormat.format(prop.minSumAssured);

        } else {
            if (!(Double
                    .parseDouble(edt_bi_smart_women_advantage_sum_assured_amount
                            .getText().toString()) % 1000 == 0)) {
                error = "Sum Assured should be multiple of 1000";
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
            return false;
        } else
            return true;

    }

    // Critical illness Sum Assured Validation
    private boolean valCritiSumAssured() {
        String error = "";
        if (edt_bi_smart_women_advantage_critical_illness_sum_assured.getText()
                .toString().equals("0")) {
            error = "Please enter Sum Assured in Rs. ";

        } else if (Integer
                .parseInt(edt_bi_smart_women_advantage_critical_illness_sum_assured
                        .getText().toString()) > 2000000) {
            error = "Please change the Critical Illess Option or Basic Sum Assured such that Critical Illess Sum Assured should not be greater than Rs. 20,00,000";

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

    private boolean valPremAmount(double premiumwithRoundUP) {
        String error = "";
        if (spnr_bi_smart_women_advantage_premium_frequency.getSelectedItem()
                .toString().equals("Yearly")) {
            if (premiumwithRoundUP < prop.minYearlyPrem) {
                error = "Minimum Premium for Yearly mode under this product is Rs. "
                        + currencyFormat.format(prop.minYearlyPrem);
            }
        } else if (spnr_bi_smart_women_advantage_premium_frequency
                .getSelectedItem().toString().equals("Half Yearly")) {
            if (premiumwithRoundUP < prop.minHalfYearlyPrem) {
                error = "Minimum Premium for Half-Yearly mode under this product is Rs. "
                        + currencyFormat.format(prop.minHalfYearlyPrem);
            }
        } else if (spnr_bi_smart_women_advantage_premium_frequency
                .getSelectedItem().toString().equals("Quarterly")) {
            if (premiumwithRoundUP < prop.minQuarterleyPrem) {
                error = "Minimum Premium for Quarterly mode under this product is Rs. "
                        + currencyFormat.format(prop.minQuarterleyPrem);
            }
        } else if (spnr_bi_smart_women_advantage_premium_frequency
                .getSelectedItem().toString().equals("Monthly")) {
            if (premiumwithRoundUP < prop.minMonthleyPrem) {
                error = "Minimum Premium for "
                        + spnr_bi_smart_women_advantage_premium_frequency
                        .getSelectedItem().toString()
                        + " mode under this product is Rs. "
                        + currencyFormat.format(prop.minMonthleyPrem);
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
            return false;
        } else
            return true;

    }

    private boolean valMaturityAge() {
        int age = 0;
        String error = "";
        if ((!edt_bi_smart_women_advantage_life_assured_age.getText()
                .toString().equalsIgnoreCase(""))) {
            age = Integer
                    .parseInt(edt_bi_smart_women_advantage_life_assured_age
                            .getText().toString());
        }

        if ((age + Integer.parseInt(spnr_bi_smart_women_advantage_policyterm
                .getSelectedItem().toString())) <= 60)
            error = "";
        else
            error = "Maximum maturity age of Life Assured is 60 Years.";

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
                                    setFocusable(spnr_bi_smart_women_advantage_life_assured_title);
                                    spnr_bi_smart_women_advantage_life_assured_title
                                            .requestFocus();
                                } else if (lifeAssured_First_Name.equals("")) {
                                    setFocusable(edt_bi_smart_women_advantage_life_assured_first_name);
                                    edt_bi_smart_women_advantage_life_assured_first_name
                                            .requestFocus();
                                } else {
                                    setFocusable(edt_bi_smart_women_advantage_life_assured_last_name);
                                    edt_bi_smart_women_advantage_life_assured_last_name
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

        // if (proposer_Is_Same_As_Life_Assured.equalsIgnoreCase("y")) {

        if (lifeAssured_date_of_birth.equals("")
                || lifeAssured_date_of_birth.equalsIgnoreCase("select Date")) {
            showAlert
                    .setMessage("Please Select Valid Date Of Birth For LifeAssured");
            showAlert.setNeutralButton("OK",
                    new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            // apply focusable method
                            setFocusable(btn_bi_smart_women_advantage_life_assured_date);
                            btn_bi_smart_women_advantage_life_assured_date
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

        if (edt_smart_women_advantage_contact_no.getText().toString()
                .equals("")) {
            commonMethods.dialogWarning(context, "Please Fill  Mobile No", true);
            edt_smart_women_advantage_contact_no.requestFocus();
            return false;
        } else if (edt_smart_women_advantage_contact_no.getText().toString()
                .length() != 10) {
            commonMethods.dialogWarning(context, "Please Fill 10 Digit Mobile No", true);
            edt_smart_women_advantage_contact_no.requestFocus();
            return false;
        }

		/*else if (emailId.equals("")) {
			commonMethods.dialogWarning(context, "Please Fill Email Id", true);
			edt_smart_women_advantage_Email_id.requestFocus();
			return false;

		}

		else if (ConfirmEmailId.equals("")) {

			commonMethods.dialogWarning(context, "Please Fill Confirm Email Id", true);
			edt_smart_women_advantage_ConfirmEmail_id.requestFocus();
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
                    edt_smart_women_advantage_ConfirmEmail_id.requestFocus();
                    return false;
                } else if (!ConfirmEmailId.equals(emailId)) {
                    commonMethods.dialogWarning(context, "Email Id Does Not Match", true);
                    return false;
                }

                return true;
            } else {
                commonMethods.dialogWarning(context, "Please Fill Valid Email Id", true);
                edt_smart_women_advantage_Email_id.requestFocus();
                return false;
            }
        } else if (!ConfirmEmailId.equals("")) {

            email_id_validation(ConfirmEmailId);
            if (validationFla1) {

                if (emailId.equals("")) {
                    commonMethods.dialogWarning(context, "Please Fill Email Id", true);
                    edt_smart_women_advantage_Email_id.requestFocus();
                    return false;

                }

                return true;
            } else {
                commonMethods.dialogWarning(context, "Please Fill Valid Confirm Email Id", true);
                edt_smart_women_advantage_ConfirmEmail_id.requestFocus();
                return false;
            }
        } else {
            return true;
        }
    }

    // Validate Sum Assured
    boolean valSA() {
        StringBuilder error = new StringBuilder();
        if (edt_bi_smart_women_advantage_sum_assured_amount.getText()
                .toString().equals("")) {
            error.append("Please enter Sum Assured in Rs. ");

        } else if (Double
                .parseDouble(edt_bi_smart_women_advantage_sum_assured_amount
                        .getText().toString()) % 1000 != 0) {
            error.append("Sum assured should be multiple of 1,000");
        } else if (Integer
                .parseInt(edt_bi_smart_women_advantage_sum_assured_amount
                        .getText().toString()) > prop.maxSumAssured) {
            error.append("Sum Assured should not be greater than Rs. ").append(currencyFormat.format(prop.maxSumAssured));

        } else if (Integer
                .parseInt(edt_bi_smart_women_advantage_sum_assured_amount
                        .getText().toString()) < prop.minSumAssured) {
            error.append("Sum Assured should not be less than Rs. ").append(currencyFormat.format(prop.minSumAssured));

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

    public void Date() {
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = df.format(c.getTime());
        // formattedDate have current date/time
        // Toast.makeText(this, formattedDate, Toast.LENGTH_SHORT).show();
        c.add(Calendar.DATE, 30);
        String expiredDate = df.format(c.getTime());
    }
}
