package sbilife.com.pointofsale_bancaagency.smarthumsafar;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
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
import sbilife.com.pointofsale_bancaagency.Utility;
import sbilife.com.pointofsale_bancaagency.common.BIPdfMail;
import sbilife.com.pointofsale_bancaagency.common.CommonForAllProd;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;
import sbilife.com.pointofsale_bancaagency.common.ProductInfo;
import sbilife.com.pointofsale_bancaagency.common.SimpleCrypto;
import sbilife.com.pointofsale_bancaagency.common.StorageUtils;
import sbilife.com.pointofsale_bancaagency.needanalysis.NeedAnalysisActivity;
import sbilife.com.pointofsale_bancaagency.new_bussiness.ProductBIBean;
import sbilife.com.pointofsale_bancaagency.utility.GridHeight;

@SuppressWarnings("deprecation")
public class BI_SmartHumsafarActivity extends AppCompatActivity implements
        OnEditorActionListener {

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
    private CheckBox cb_bi_smart_humsafar_JKResident;
    private CheckBox cb_bi_smart_humsafar_adb_rider;

    private EditText edt_bi_smart_humsafar_life_assured_first_name;
    private EditText edt_bi_smart_humsafar_life_assured_middle_name;
    private EditText edt_bi_smart_humsafar_life_assured_last_name;
    private EditText edt_bi_smart_humsafar_life_assured_age;
    private EditText edt_smart_humsafar_contact_no;
    private EditText edt_smart_humsafar_Email_id;
    private EditText edt_smart_humsafar_ConfirmEmail_id;
    private EditText edt_bi_smart_humsafar_sum_assured_amount;
    private EditText edt_bi_smart_humsafar_adb_rider_sum_assured;
    private EditText edt_bi_smart_humsafar_spouse_rider_sum_assured;
    private EditText edt_bi_smart_humsafar_proposer_last_name;
    private EditText edt_bi_smart_humsafar_proposer_age;
    private EditText edt_bi_smart_humsafar_proposer_middle_name;
    private EditText edt_bi_smart_humsafar_proposer_first_name;

    private Spinner spnr_bi_smart_humsafar_life_assured_title;
    private Spinner spnr_bi_smart_humsafar_policyterm;
    private Spinner spnr_bi_smart_humsafar_selGender;
    private Spinner spnr_bi_smart_humsafar_premium_paying_mode;
    private Spinner spnr_bi_smart_humsafar_adb_rider_term;
    private Spinner spnr_bi_smart_humsafar_spouse_rider_term;
    private Spinner spnr_bi_smart_humsafar_proposer_title;
    private Spinner spnr_bi_smart_humsafar_proposer_selGender;
    private Spinner spnr_bi_smart_humsafar_applicable_for;

    private TableRow tr_bi_smart_humsafar_applicable_for_rider;
    private TableRow tr_bi_smart_humsafar_adb_rider;
    private TableRow tr_bi_smart_humsafar_spouse_rider;
    private TableRow tr_bi_smart_humsafar_life_tobe_assured_rider;
    private TableRow tr_bi_smart_humsafar_spouse_life_tobe_assured;

    private Button btn_bi_smart_humsafar_life_assured_date;
    private Button btn_bi_smart_humsafar_proposer_date;

    private Button btnBack;
    private Button btnSubmit;
    private LinearLayout ll_bi_smart_humsafar_main;

    private Button btn_MarketingOfficalDate;
    private Button btn_PolicyholderDate;
    private ImageButton Ibtn_signatureofMarketing;
    private ImageButton Ibtn_signatureofPolicyHolders;

    private RadioButton rb_smart_humsafar_backdating_yes,
            rb_smart_humsafar_backdating_no;
    private LinearLayout ll_smart_humsafar_backdating;
    private Button btn_smart_humsafar_backdatingdate;

    private String QuatationNumber = "";
    private String planName = "";
    // newDBHelper db;
    private ParseXML prsObj;
    private int mYear;
    private int mMonth;
    private int mDay;
    private Dialog d;
    private final int SIGNATURE_ACTIVITY = 1;
    private SmartHumsafarProperties prop = null;
    private SmartHumsafarBean smartHumsafarBean = null;
    private CommonForAllProd obj;
    private CommonForAllProd commonForAllProd = null;
    private DecimalFormat currencyFormat;
    private DecimalFormat decimalCurrencyFormat;
    private String latestImage = "";
    private String agent_sign = "";
    private String proposer_sign = "";
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
    private boolean validationFla1 = false;
    List<M_BI_SmartHumsafar_AdapterCommon> list_data;
    List<M_BI_SmartHumsafar_AdapterCommon> list_data2;
    String gender_la = "", gender_spouse = "";
    String life_to_be_assured_term = "", life_to_be_assured_sum_assured = "",
            life_to_be_assured_sum_assured_premium = "",
            spouse_life_to_be_assured_term = "",
            spouse_life_to_be_assured_sum_assured = "",
            spouse_life_to_be_assured_sum_assured_premium = "",
            adb_rider_status = "", applicable_for = "", basicprem = "", YearlyPrem = "",
            servicetax = "", installmntPremWithSerTx = "", InstBasicRider = "", premiumInstWithSTFirstYearRider = "",
            totalInstPremSecondYearSY = "", totalInstPremFirstYear = "", premiumInstWithSTSecondYearRiderSY = "",
            yearlypremium_with_servicetax = "", BasicInstPremSecondYear = "", premium = "", installmntPrem = "",
            sum_assured = "", benefit_payable_on_first_death = "", benefit_payable_on_second_death = "",
            policy_term = "", premium_paying_frequency = "", age_entry = "";

    private final String proposer_Is_Same_As_Life_Assured = "Y";
    private String name_of_proposer = "";
    private String proposer_date_of_birth = "";
    private String spouse_date_of_birth = "";
    private String emailId = "";
    private String mobileNo = "";
    private String ConfirmEmailId = "";
    private String ProposerEmailId = "";
    private String ProposerAge = "";
    private int maxPolicyTermRider;
    private final int DATE_DIALOG_ID = 1;
    private int DIALOG_ID;
    private StringBuilder bussIll = null;
    private CommonForAllProd cfap = null;
    private String name_of_person = "";
    private String place2 = "";
    private String date1 = "";
    private String date2 = "";
    private String proposer_Backdating_WishToBackDate_Policy = "";
    private String proposer_Backdating_BackDate = "";
    private String spouse_Title = "";
    private String spouse_First_Name = "";
    private String spouse_Middle_Name = "";
    private String spouse_Last_Name = "";
    private String fullname_of_spouse = "";
    private String output = "", input = "";
    private File mypath;

    private String BackDateinterest = "0.0", BackDateinterestwithGST = "";
    //private String BackdatingInt;

    String MinesOccuInterest = "", servicetax_MinesOccuInterest = "";
    String BackdatingInt;
    String basicServiceTax = "", SBCServiceTax = "", KKCServiceTax = "";
    private String bankUserType = "", mode = "";

    /* parivartan changes */
    private String Check = "";
    private Context context;
    private CommonMethods commonMethods;
    private StorageUtils mStorageUtils;

    private ImageButton Ibtn_signatureofLifeAssured;
    private String proposerAsLifeAssuredSign = "";
    private String product_Code, product_UIN, product_cateogory, product_type;
    private ImageButton imageButtonSmartHumsafarLifeAssuredPhotograph,
            imageButtonSmartHumsafarProposerPhotograph;
    private Bitmap proposerBitmap;

    /* end */
    private CheckBox cb_kerladisc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.bi_smart_humsafarmain);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);

        Utility utilityObj = new Utility();
        context = this;
        commonMethods = new CommonMethods();
        mStorageUtils = new StorageUtils();


        dbHelper = new DatabaseHelper(getApplicationContext());

        new CommonMethods().setApplicationToolbarMenu(this,
                getString(R.string.app_name));

        NABIObj = new NeedAnalysisBIService(this);
        BIPdfMail objBIPdfMail = new BIPdfMail();
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
                gender_la = intent.getStringExtra("custGender");
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

                    /* parivartan chages */
                    ProductInfo prodInfoObj = new ProductInfo();
                    planName = "Smart Humsafar";
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
                        product_Code/* "1W" */, agentcode, zero + "");
            }
        } else
            needAnalysis_flag = 0;
        // ProductHomePageActivity.path.setText("Benefit Illustrator");

        UI_Declaration();
        initialiseDate();
        // db = new newDBHelper(this);
        Date();
        prsObj = new ParseXML();
        prop = new SmartHumsafarProperties();
        maxPolicyTermRider = prop.maxPolicyTermRider;
        obj = new CommonForAllProd();

        setSpinner_Value();
        // setBIInputGui();

        edt_bi_smart_humsafar_life_assured_first_name
                .setOnEditorActionListener(this);
        edt_bi_smart_humsafar_life_assured_middle_name
                .setOnEditorActionListener(this);
        edt_bi_smart_humsafar_life_assured_last_name
                .setOnEditorActionListener(this);
        edt_bi_smart_humsafar_proposer_first_name
                .setOnEditorActionListener(this);
        edt_bi_smart_humsafar_proposer_middle_name
                .setOnEditorActionListener(this);
        edt_bi_smart_humsafar_proposer_last_name
                .setOnEditorActionListener(this);
        edt_bi_smart_humsafar_sum_assured_amount
                .setOnEditorActionListener(this);
        edt_smart_humsafar_contact_no.setOnEditorActionListener(this);
        edt_smart_humsafar_Email_id.setOnEditorActionListener(this);
        edt_smart_humsafar_ConfirmEmail_id.setOnEditorActionListener(this);
        edt_bi_smart_humsafar_adb_rider_sum_assured
                .setOnEditorActionListener(this);
        edt_bi_smart_humsafar_spouse_rider_sum_assured
                .setOnEditorActionListener(this);

        commonForAllProd = new CommonForAllProd();

        validationOfMoile_EmailId();
        setSpinnerAndOtherListner();
        cfap = new CommonForAllProd();
        smartHumsafarBean = new SmartHumsafarBean();
        list_data = new ArrayList<>();
        list_data2 = new ArrayList<>();
        currencyFormat = new DecimalFormat("##,##,##,###");
        decimalCurrencyFormat = new DecimalFormat("##,##,##,###.##");
        // getBasicDetail();

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

        TableRow tr_staff_disc = findViewById(R.id.tr_smart_humsafar_staff_disc);
        tr_staff_disc.setVisibility(View.GONE);

        if (needAnalysis_flag == 1 && !TextUtils.isEmpty(gender_la)) {
            spnr_bi_smart_humsafar_selGender.setSelection(genderAdapter
                    .getPosition(gender_la));
            onClickLADob(btn_bi_smart_humsafar_life_assured_date);
        }
    }

    private void UI_Declaration() {

        cb_staffdisc = findViewById(R.id.cb_staffdisc);
        cb_bi_smart_humsafar_JKResident = findViewById(R.id.cb_bi_smart_humsafar_JKResident);
        cb_bi_smart_humsafar_adb_rider = findViewById(R.id.cb_bi_smart_humsafar_adb_rider);

        edt_bi_smart_humsafar_life_assured_first_name = findViewById(R.id.edt_bi_smart_humsafar_life_assured_first_name);
        edt_bi_smart_humsafar_life_assured_middle_name = findViewById(R.id.edt_bi_smart_humsafar_life_assured_middle_name);
        edt_bi_smart_humsafar_life_assured_last_name = findViewById(R.id.edt_bi_smart_humsafar_life_assured_last_name);
        edt_bi_smart_humsafar_life_assured_age = findViewById(R.id.edt_bi_smart_humsafar_life_assured_age);
        edt_smart_humsafar_contact_no = findViewById(R.id.edt_smart_humsafar_contact_no);
        edt_smart_humsafar_Email_id = findViewById(R.id.edt_smart_humsafar_Email_id);
        edt_smart_humsafar_ConfirmEmail_id = findViewById(R.id.edt_smart_humsafar_ConfirmEmail_id);
        edt_bi_smart_humsafar_sum_assured_amount = findViewById(R.id.edt_bi_smart_humsafar_sum_assured_amount);
        edt_bi_smart_humsafar_adb_rider_sum_assured = findViewById(R.id.edt_bi_smart_humsafar_adb_rider_sum_assured);
        edt_bi_smart_humsafar_spouse_rider_sum_assured = findViewById(R.id.edt_bi_smart_humsafar_spouse_rider_sum_assured);

        spnr_bi_smart_humsafar_life_assured_title = findViewById(R.id.spnr_bi_smart_humsafar_life_assured_title);
        spnr_bi_smart_humsafar_selGender = findViewById(R.id.spnr_bi_smart_humsafar_selGender);
        /*spnr_bi_smart_humsafar_selGender.setClickable(false);
        spnr_bi_smart_humsafar_selGender.setEnabled(false);*/

        spnr_bi_smart_humsafar_policyterm = findViewById(R.id.spnr_bi_smart_humsafar_policyterm);
        spnr_bi_smart_humsafar_premium_paying_mode = findViewById(R.id.spnr_bi_smart_humsafar_premium_paying_mode);
        spnr_bi_smart_humsafar_adb_rider_term = findViewById(R.id.spnr_bi_smart_humsafar_adb_rider_term);
        spnr_bi_smart_humsafar_spouse_rider_term = findViewById(R.id.spnr_bi_smart_humsafar_spouse_rider_term);
        spnr_bi_smart_humsafar_applicable_for = findViewById(R.id.spnr_bi_smart_humsafar_applicable_for);

        btn_bi_smart_humsafar_life_assured_date = findViewById(R.id.btn_bi_smart_humsafar_life_assured_date);
        btnBack = findViewById(R.id.btn_bi_smart_humsafar_btnback);
        btnSubmit = findViewById(R.id.btn_bi_smart_humsafar_btnSubmit);

        // Spouse details

        spnr_bi_smart_humsafar_proposer_title = findViewById(R.id.spnr_bi_smart_humsafar_proposer_title);
        edt_bi_smart_humsafar_proposer_first_name = findViewById(R.id.edt_bi_smart_humsafar_proposer_first_name);
        edt_bi_smart_humsafar_proposer_middle_name = findViewById(R.id.edt_bi_smart_humsafar_proposer_middle_name);
        edt_bi_smart_humsafar_proposer_last_name = findViewById(R.id.edt_bi_smart_humsafar_proposer_last_name);
        btn_bi_smart_humsafar_proposer_date = findViewById(R.id.btn_bi_smart_humsafar_proposer_date);
        edt_bi_smart_humsafar_proposer_age = findViewById(R.id.edt_bi_smart_humsafar_proposer_age);
        spnr_bi_smart_humsafar_proposer_selGender = findViewById(R.id.spnr_bi_smart_humsafar_proposer_selGender);
        /*spnr_bi_smart_humsafar_proposer_selGender.setClickable(false);
        spnr_bi_smart_humsafar_proposer_selGender.setEnabled(false);*/

        tr_bi_smart_humsafar_applicable_for_rider = findViewById(R.id.tr_bi_smart_humsafar_applicable_for_rider);
        tr_bi_smart_humsafar_adb_rider = findViewById(R.id.tr_bi_smart_humsafar_adb_rider);
        tr_bi_smart_humsafar_spouse_rider = findViewById(R.id.tr_bi_smart_humsafar_spouse_rider);
        tr_bi_smart_humsafar_life_tobe_assured_rider = findViewById(R.id.tr_bi_smart_humsafar_life_tobe_assured_rider);
        tr_bi_smart_humsafar_spouse_life_tobe_assured = findViewById(R.id.tr_bi_smart_humsafar_spouse_life_tobe_assured);
        ll_bi_smart_humsafar_main = findViewById(R.id.ll_bi_smart_humsafar_main);

        TextView help_premAmt = findViewById(R.id.help_premAmt);

        btn_smart_humsafar_backdatingdate = findViewById(R.id.btn_smart_humsafar_backdatingdate);
        help_premAmt = findViewById(R.id.help_premAmt);
        rb_smart_humsafar_backdating_yes = findViewById(R.id.rb_smart_humsafar_backdating_yes);
        rb_smart_humsafar_backdating_no = findViewById(R.id.rb_smart_humsafar_backdating_no);
        ll_smart_humsafar_backdating = findViewById(R.id.ll_smart_humsafar_backdating);
    }


    private void setSpinner_Value() {

        // Gender
        genderAdapter = new ArrayAdapter<>(getApplicationContext(),
                R.layout.spinner_item, getResources().getStringArray(R.array.gender_all_arrays));
        genderAdapter.setDropDownViewResource(R.layout.spinner_item1);
        spnr_bi_smart_humsafar_selGender.setAdapter(genderAdapter);
        genderAdapter.notifyDataSetChanged();

        List<String> title_list = new ArrayList<>();
        title_list.add("Select Title");
        title_list.add("Mr.");
        title_list.add("Mrs.");
        commonMethods.fillSpinnerValue(context, spnr_bi_smart_humsafar_life_assured_title, title_list);

        ArrayAdapter<String> spouse_genderAdapter = new ArrayAdapter<>(
                getApplicationContext(), R.layout.spinner_item,
                getResources().getStringArray(R.array.gender_all_arrays));
        spouse_genderAdapter.setDropDownViewResource(R.layout.spinner_item1);
        spnr_bi_smart_humsafar_proposer_selGender
                .setAdapter(spouse_genderAdapter);
        spouse_genderAdapter.notifyDataSetChanged();

        commonMethods.fillSpinnerValue(context, spnr_bi_smart_humsafar_proposer_title, title_list);

        // // Policy Term
        String[] policyTermList = new String[21];
        for (int i = 10; i <= 30; i++) {
            policyTermList[i - 10] = i + "";
        }
        ArrayAdapter<String> policyTermAdapter = new ArrayAdapter<>(
                getApplicationContext(), R.layout.spinner_item, policyTermList);
        policyTermAdapter.setDropDownViewResource(R.layout.spinner_item1);
        spnr_bi_smart_humsafar_policyterm.setAdapter(policyTermAdapter);
        policyTermAdapter.notifyDataSetChanged();

        // Premium Frequency
        String[] premFreqList = {"Monthly", "Quarterly", "Half Yearly",
                "Yearly"};
        ArrayAdapter<String> premFreqAdapter = new ArrayAdapter<>(
                getApplicationContext(), R.layout.spinner_item, premFreqList);
        premFreqAdapter.setDropDownViewResource(R.layout.spinner_item1);
        spnr_bi_smart_humsafar_premium_paying_mode.setAdapter(premFreqAdapter);
        premFreqAdapter.notifyDataSetChanged();

        // Applicable For
        String[] applicablefor_List = {"Life to be Assured",
                "Spouse Life to be Assured", "Both Lives Assured"};
        ArrayAdapter<String> applicablefor_Adapter = new ArrayAdapter<>(
                getApplicationContext(), R.layout.spinner_item,
                applicablefor_List);
        applicablefor_Adapter.setDropDownViewResource(R.layout.spinner_item1);
        spnr_bi_smart_humsafar_applicable_for.setAdapter(applicablefor_Adapter);
        applicablefor_Adapter.notifyDataSetChanged();
    }

    private void initialiseDate() {
        Calendar calender = Calendar.getInstance();
        mYear = calender.get(Calendar.YEAR);
        mMonth = calender.get(Calendar.MONTH);
        mDay = calender.get(Calendar.DAY_OF_MONTH);

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


    private void windowmessagesgin() {

        d = new Dialog(BI_SmartHumsafarActivity.this);
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
                Intent intent = new Intent(BI_SmartHumsafarActivity.this,
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

    public String getCurrentDate1() {
        Calendar present_date = Calendar.getInstance();
        int mDay = present_date.get(Calendar.DAY_OF_MONTH);
        int mMonth = present_date.get(Calendar.MONTH);
        int mYear = present_date.get(Calendar.YEAR);

        String mont = (mMonth + 1 < 10 ? "0" : "") + (mMonth + 1);
        String day = (mDay < 10 ? "0" : "") + mDay;

        StringBuilder date = new StringBuilder().append(day).append("-")
                .append(mont).append("-").append(mYear);

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

    private void email_id_validation(String email_id) {

        String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email_id);
        validationFla1 = false;
        if (!(matcher.matches())) {
            edt_smart_humsafar_Email_id
                    .setError("Please provide the correct email address");
            validationFla1 = false;
        } else if ((matcher.matches())) {
            validationFla1 = true;
        }
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

    private void setSpinnerAndOtherListner() {

        cb_staffdisc.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (cb_staffdisc.isChecked()) {
                    cb_staffdisc.setChecked(true);
                }
                clearFocusable(cb_staffdisc);
                clearFocusable(spnr_bi_smart_humsafar_life_assured_title);
                setFocusable(spnr_bi_smart_humsafar_life_assured_title);
                spnr_bi_smart_humsafar_life_assured_title.requestFocus();

            }
        });

        cb_bi_smart_humsafar_JKResident
                .setOnClickListener(new OnClickListener() {

                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        if (cb_bi_smart_humsafar_JKResident.isChecked()) {
                            cb_bi_smart_humsafar_JKResident.setChecked(true);
                        } else {
                            cb_bi_smart_humsafar_JKResident.setChecked(false);
                        }
                    }
                });

        cb_bi_smart_humsafar_adb_rider
                .setOnClickListener(new OnClickListener() {

                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        if (cb_bi_smart_humsafar_adb_rider.isChecked()) {
                            cb_bi_smart_humsafar_adb_rider.setChecked(true);
                            tr_bi_smart_humsafar_adb_rider
                                    .setVisibility(View.VISIBLE);
                            tr_bi_smart_humsafar_applicable_for_rider
                                    .setVisibility(View.VISIBLE);
                            tr_bi_smart_humsafar_life_tobe_assured_rider
                                    .setVisibility(View.VISIBLE);
                            updateRiderPolicyTerm();

                            clearFocusable(cb_bi_smart_humsafar_adb_rider);
                            setFocusable(spnr_bi_smart_humsafar_applicable_for);
                            spnr_bi_smart_humsafar_applicable_for
                                    .requestFocus();
                        } else {
                            tr_bi_smart_humsafar_applicable_for_rider
                                    .setVisibility(View.GONE);
                            tr_bi_smart_humsafar_adb_rider
                                    .setVisibility(View.GONE);
                            tr_bi_smart_humsafar_life_tobe_assured_rider
                                    .setVisibility(View.GONE);
                            tr_bi_smart_humsafar_spouse_life_tobe_assured
                                    .setVisibility(View.GONE);
                            tr_bi_smart_humsafar_spouse_rider
                                    .setVisibility(View.GONE);
                        }

                    }
                });

        // Spinner

        spnr_bi_smart_humsafar_proposer_title
                .setOnItemSelectedListener(new OnItemSelectedListener() {

                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int position, long id) {
                        // TODO Auto-generated method stub
                        if (position == 0) {
                            spouse_Title = "";
                        } else {
                            spouse_Title = spnr_bi_smart_humsafar_proposer_title
                                    .getSelectedItem().toString();

                            if (spouse_Title.equalsIgnoreCase("Mr.")) {
                                spnr_bi_smart_humsafar_proposer_selGender
                                        .setSelection(getIndex(
                                                spnr_bi_smart_humsafar_proposer_selGender,
                                                "Male"));

                                spnr_bi_smart_humsafar_life_assured_title
                                        .setSelection(
                                                getIndex(
                                                        spnr_bi_smart_humsafar_life_assured_title,
                                                        "Mrs."), false);
                                gender_spouse = "Male";
                            } else if (spouse_Title.equalsIgnoreCase("Ms.")
                                    || spouse_Title.equalsIgnoreCase("Mrs.")) {
                                spnr_bi_smart_humsafar_proposer_selGender
                                        .setSelection(getIndex(
                                                spnr_bi_smart_humsafar_proposer_selGender,
                                                "Female"));

                                spnr_bi_smart_humsafar_life_assured_title
                                        .setSelection(
                                                getIndex(
                                                        spnr_bi_smart_humsafar_life_assured_title,
                                                        "Mr."), false);
                                gender_spouse = "Female";
                            }

                            if (edt_bi_smart_humsafar_life_assured_first_name
                                    .getText().toString().equals("")) {
                                clearFocusable(spnr_bi_smart_humsafar_proposer_title);
                                setFocusable(edt_bi_smart_humsafar_life_assured_first_name);
                                edt_bi_smart_humsafar_life_assured_first_name
                                        .requestFocus();
                            } else {
                                clearFocusable(spnr_bi_smart_humsafar_proposer_title);
                                setFocusable(edt_bi_smart_humsafar_proposer_first_name);
                                edt_bi_smart_humsafar_proposer_first_name
                                        .requestFocus();
                            }

                        }

                    }

                    public void onNothingSelected(AdapterView<?> arg0) {
                        // TODO Auto-generated method stub

                    }
                });

        spnr_bi_smart_humsafar_life_assured_title
                .setOnItemSelectedListener(new OnItemSelectedListener() {

                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int position, long id) {
                        // TODO Auto-generated method stub
                        if (position == 0) {
                            lifeAssured_Title = "";
                        } else {
                            lifeAssured_Title = spnr_bi_smart_humsafar_life_assured_title
                                    .getSelectedItem().toString();
                            if (lifeAssured_Title.equalsIgnoreCase("Mr.")) {
                                spnr_bi_smart_humsafar_selGender
                                        .setSelection(
                                                getIndex(
                                                        spnr_bi_smart_humsafar_selGender,
                                                        "Male"), false);
                                spnr_bi_smart_humsafar_proposer_title
                                        .setSelection(
                                                getIndex(
                                                        spnr_bi_smart_humsafar_proposer_title,
                                                        "Mrs."), false);
                                gender_la = "Male";
                            } else if (lifeAssured_Title
                                    .equalsIgnoreCase("Mrs.")) {
                                spnr_bi_smart_humsafar_selGender
                                        .setSelection(
                                                getIndex(
                                                        spnr_bi_smart_humsafar_selGender,
                                                        "Female"), false);

                                spnr_bi_smart_humsafar_proposer_title
                                        .setSelection(
                                                getIndex(
                                                        spnr_bi_smart_humsafar_proposer_title,
                                                        "Mr."), false);
                                gender_la = "Female";
                            }

                            if (edt_bi_smart_humsafar_life_assured_first_name
                                    .getText().toString().equals("")) {
                                clearFocusable(spnr_bi_smart_humsafar_life_assured_title);
                                setFocusable(edt_bi_smart_humsafar_life_assured_first_name);
                                edt_bi_smart_humsafar_life_assured_first_name
                                        .requestFocus();
                            } else {
                                clearFocusable(spnr_bi_smart_humsafar_life_assured_title);
                                setFocusable(edt_bi_smart_humsafar_life_assured_first_name);
                                edt_bi_smart_humsafar_life_assured_first_name
                                        .requestFocus();

                            }

                        }
                    }

                    public void onNothingSelected(AdapterView<?> arg0) {
                        // TODO Auto-generated method stub

                    }
                });

        spnr_bi_smart_humsafar_policyterm
                .setOnItemSelectedListener(new OnItemSelectedListener() {

                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int pos, long id) {

                        updateRiderPolicyTerm();
                        clearFocusable(spnr_bi_smart_humsafar_policyterm);
                        setFocusable(spnr_bi_smart_humsafar_premium_paying_mode);
                        spnr_bi_smart_humsafar_premium_paying_mode
                                .requestFocus();

                    }

                    public void onNothingSelected(AdapterView<?> arg0) {
                        // TODO Auto-generated method stub

                    }
                });

        spnr_bi_smart_humsafar_premium_paying_mode
                .setOnItemSelectedListener(new OnItemSelectedListener() {

                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int pos, long id) {

                        if (edt_bi_smart_humsafar_life_assured_first_name
                                .getText().toString().equals("")) {
                            clearFocusable(spnr_bi_smart_humsafar_premium_paying_mode);
                            setFocusable(spnr_bi_smart_humsafar_life_assured_title);
                            spnr_bi_smart_humsafar_life_assured_title
                                    .requestFocus();
                        } else {
                            clearFocusable(spnr_bi_smart_humsafar_premium_paying_mode);
                            setFocusable(edt_bi_smart_humsafar_sum_assured_amount);
                            edt_bi_smart_humsafar_sum_assured_amount
                                    .requestFocus();
                        }

                    }

                    public void onNothingSelected(AdapterView<?> arg0) {
                        // TODO Auto-generated method stub

                    }
                });

        // ADB Term
        spnr_bi_smart_humsafar_adb_rider_term
                .setOnItemSelectedListener(new OnItemSelectedListener() {

                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int pos, long id) {
                        clearFocusable(spnr_bi_smart_humsafar_adb_rider_term);
                        setFocusable(edt_bi_smart_humsafar_adb_rider_sum_assured);
                        edt_bi_smart_humsafar_adb_rider_sum_assured
                                .requestFocus();
                    }

                    public void onNothingSelected(AdapterView<?> arg0) {
                        // TODO Auto-generated method stub

                    }
                });

        // Spouse ADB Term
        spnr_bi_smart_humsafar_spouse_rider_term
                .setOnItemSelectedListener(new OnItemSelectedListener() {

                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int pos, long id) {
                        clearFocusable(spnr_bi_smart_humsafar_spouse_rider_term);
                        setFocusable(edt_bi_smart_humsafar_spouse_rider_sum_assured);
                        edt_bi_smart_humsafar_spouse_rider_sum_assured
                                .requestFocus();
                    }

                    public void onNothingSelected(AdapterView<?> arg0) {
                        // TODO Auto-generated method stub

                    }
                });

        spnr_bi_smart_humsafar_applicable_for
                .setOnItemSelectedListener(new OnItemSelectedListener() {

                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int pos, long id) {

                        if (pos == 0) {
                            tr_bi_smart_humsafar_adb_rider
                                    .setVisibility(View.VISIBLE);
                            tr_bi_smart_humsafar_life_tobe_assured_rider
                                    .setVisibility(View.VISIBLE);
                            tr_bi_smart_humsafar_spouse_life_tobe_assured
                                    .setVisibility(View.GONE);
                            tr_bi_smart_humsafar_spouse_rider
                                    .setVisibility(View.GONE);

                        } else if (pos == 1) {
                            tr_bi_smart_humsafar_adb_rider
                                    .setVisibility(View.GONE);
                            tr_bi_smart_humsafar_life_tobe_assured_rider
                                    .setVisibility(View.GONE);
                            tr_bi_smart_humsafar_spouse_life_tobe_assured
                                    .setVisibility(View.VISIBLE);
                            tr_bi_smart_humsafar_spouse_rider
                                    .setVisibility(View.VISIBLE);
                        } else {
                            tr_bi_smart_humsafar_adb_rider
                                    .setVisibility(View.VISIBLE);
                            tr_bi_smart_humsafar_life_tobe_assured_rider
                                    .setVisibility(View.VISIBLE);
                            tr_bi_smart_humsafar_spouse_life_tobe_assured
                                    .setVisibility(View.VISIBLE);
                            tr_bi_smart_humsafar_spouse_rider
                                    .setVisibility(View.VISIBLE);
                        }
                        updateRiderPolicyTerm();

                        clearFocusable(spnr_bi_smart_humsafar_applicable_for);
                        setFocusable(edt_bi_smart_humsafar_spouse_rider_sum_assured);
                        edt_bi_smart_humsafar_spouse_rider_sum_assured
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

                spouse_Title = spnr_bi_smart_humsafar_proposer_title
                        .getSelectedItem().toString();

                spouse_First_Name = edt_bi_smart_humsafar_proposer_first_name
                        .getText().toString().trim();
                spouse_Middle_Name = edt_bi_smart_humsafar_proposer_middle_name
                        .getText().toString().trim();
                spouse_Last_Name = edt_bi_smart_humsafar_proposer_last_name
                        .getText().toString().trim();

                fullname_of_spouse = spouse_Title + " " + spouse_First_Name
                        + " " + spouse_Middle_Name + " " + spouse_Last_Name;

                lifeAssured_First_Name = edt_bi_smart_humsafar_life_assured_first_name
                        .getText().toString().trim();
                lifeAssured_Middle_Name = edt_bi_smart_humsafar_life_assured_middle_name
                        .getText().toString().trim();
                lifeAssured_Last_Name = edt_bi_smart_humsafar_life_assured_last_name
                        .getText().toString().trim();

                gender_la = spnr_bi_smart_humsafar_selGender.getSelectedItem().toString();
                gender_spouse = spnr_bi_smart_humsafar_proposer_selGender.getSelectedItem().toString();
                name_of_life_assured = lifeAssured_Title + " "
                        + lifeAssured_First_Name + " "
                        + lifeAssured_Middle_Name + " " + lifeAssured_Last_Name;

                mobileNo = edt_smart_humsafar_contact_no.getText().toString();
                emailId = edt_smart_humsafar_Email_id.getText().toString();
                ConfirmEmailId = edt_smart_humsafar_ConfirmEmail_id.getText()
                        .toString();

                if (valLifeAssuredProposerDetail()
                        && valLifeAssuredSpouseDetail() && valDob()
                        && valBasicDetail() && valProposerDob() && valInput()
                        && valAgeDiff() && valRiderLifeAssured()
                        && valRiderSpouse() && valDoYouBackdate()
                        && valBackdate() && TrueBackdate()) {

                    boolean flag = addListenerOnSubmit();

                    if (flag) {
                        getInput(smartHumsafarBean);
                        if (needAnalysis_flag == 0) {// Display output on next
                            // page
                            Intent i = new Intent(
                                    getApplicationContext(),
                                    Success.class);

                            i.putExtra("ProductName",
                                    "Product : SBI Life - Smart Humsafar (UIN:111N103V03)");

                            System.out.println(spnr_bi_smart_humsafar_premium_paying_mode
                                    .getSelectedItem().toString()
                                    + " :installmntPrem:"
                                    + prsObj.parseXmlTag(retVal.toString(),
                                    "installmntPrem"));

                            i.putExtra(
                                    "op",
                                    spnr_bi_smart_humsafar_premium_paying_mode
                                            .getSelectedItem().toString()
                                            + " Premium is Rs. "
                                            + currencyFormat.format(Double.parseDouble(prsObj
                                            .parseXmlTag(
                                                    retVal.toString(),
                                                    "installmntPrem"))));
                            i.putExtra(
                                    "op1",
                                    spnr_bi_smart_humsafar_premium_paying_mode
                                            .getSelectedItem().toString()
                                            + " Basic Premium with Rider (if any) is Rs. "
                                            + currencyFormat.format(Double.parseDouble(prsObj.parseXmlTag(
                                            retVal.toString(),
                                            "basicPremiumWtRider"))));
                            i.putExtra(
                                    "op2",
                                    "Applicable Taxes is Rs. "
                                            + currencyFormat.format(Double.parseDouble(prsObj
                                            .parseXmlTag(
                                                    retVal.toString(),
                                                    "serviceTax"))));
                            i.putExtra(
                                    "op3",
                                    spnr_bi_smart_humsafar_premium_paying_mode
                                            .getSelectedItem().toString()
                                            + " Basic Premium with Applicable Taxes is Rs. "
                                            + currencyFormat.format(Double.parseDouble(prsObj.parseXmlTag(
                                            retVal.toString(),
                                            "basicPremiumWtRiderWthST"))));

                            i.putExtra(
                                    "op4",
                                    "Guaranteed Maturity Benefit is Rs. "
                                            + currencyFormat.format(Double.parseDouble(prsObj.parseXmlTag(
                                            retVal.toString(),
                                            "GuaranteedSurvivalBenefit"
                                                    + spnr_bi_smart_humsafar_policyterm
                                                    .getSelectedItem()
                                                    .toString()))));

                            i.putExtra(
                                    "op5",
                                    "Non-Guaranteed Maturity Benefit at 4% is Rs. "
                                            + currencyFormat.format(Double.parseDouble(prsObj.parseXmlTag(
                                            retVal.toString(),
                                            "NonGuarateedSurvivalBenefitAt_4_Percent"
                                                    + spnr_bi_smart_humsafar_policyterm
                                                    .getSelectedItem()
                                                    .toString()))));
                            i.putExtra(
                                    "op6",
                                    "Non-Guaranteed Maturity Benefit at 8% is Rs. "
                                            + currencyFormat.format(Double.parseDouble(prsObj.parseXmlTag(
                                            retVal.toString(),
                                            "NonGuarateedSurvivalBenefitAt_8_Percent"
                                                    + spnr_bi_smart_humsafar_policyterm
                                                    .getSelectedItem()
                                                    .toString()))));

                            if (cb_bi_smart_humsafar_adb_rider.isChecked()) {
                                if (spnr_bi_smart_humsafar_applicable_for
                                        .getSelectedItem().toString()
                                        .equals("Life to be Assured")
                                        || spnr_bi_smart_humsafar_applicable_for
                                        .getSelectedItem().toString()
                                        .equals("Both Lives Assured"))
                                    i.putExtra(
                                            "op7",
                                            "ADB Rider Premium for Life to be Assured is Rs. "
                                                    + currencyFormat.format(Double.parseDouble(prsObj.parseXmlTag(
                                                    retVal.toString(),
                                                    "AdbRiderPremLifeAssured"))));

                                if (spnr_bi_smart_humsafar_applicable_for
                                        .getSelectedItem().toString()
                                        .equals("Spouse Life to be Assured")
                                        || spnr_bi_smart_humsafar_applicable_for
                                        .getSelectedItem().toString()
                                        .equals("Both Lives Assured"))
                                    i.putExtra(
                                            "op8",
                                            "ADB Rider Premium for Spouse Life to be Assured is Rs. "
                                                    + currencyFormat.format(Double.parseDouble(prsObj.parseXmlTag(
                                                    retVal.toString(),
                                                    "AdbRiderPremSpouse"))));
                            }

                            i.putExtra("header", "SBI Life - Smart Humsafar");
                            i.putExtra("header1", "(UIN:111N103V03)");

                            startActivity(i);
                        } else
                            Dialog();
                    }

                }

            }
        });

        rb_smart_humsafar_backdating_yes
                .setOnCheckedChangeListener(new OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView,
                                                 boolean isChecked) {
                        if (isChecked) {

                            if (!(lifeAssured_date_of_birth.equals(""))
                                    && !(spouse_date_of_birth.equals(""))) {
                                proposer_Backdating_WishToBackDate_Policy = "y";
								/*rb_smart_humsafar_backdating_yes
                                        .setChecked(true);
                                rb_smart_humsafar_backdating_no
										.setChecked(false);*/
                                ll_smart_humsafar_backdating
                                        .setVisibility(View.VISIBLE);
                                btn_smart_humsafar_backdatingdate
                                        .setText("Select Date");
                                proposer_Backdating_BackDate = "";

                            } else {

                                if (lifeAssured_date_of_birth.equals("")) {
                                    commonMethods.showMessageDialog(context, "Please Select Life Assure Dob First");
                                    rb_smart_humsafar_backdating_yes
                                            .setChecked(false);
                                    rb_smart_humsafar_backdating_no
                                            .setChecked(true);
                                } else if (spouse_date_of_birth.equals("")) {
                                    commonMethods.showMessageDialog(context, "Please Select Spouse Dob First");
                                    rb_smart_humsafar_backdating_yes
                                            .setChecked(false);
                                    rb_smart_humsafar_backdating_no
                                            .setChecked(true);
                                }
                            }

                        }
                    }
                });

        rb_smart_humsafar_backdating_no
                .setOnCheckedChangeListener(new OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView,
                                                 boolean isChecked) {
                        // TODO Auto-generated method stub
                        if (isChecked) {

                            if (proposer_Backdating_WishToBackDate_Policy
                                    .equalsIgnoreCase("Y")) {

                                btn_bi_smart_humsafar_life_assured_date
                                        .setText("Select Date");
                                btn_bi_smart_humsafar_proposer_date
                                        .setText("Select Date");
                                edt_bi_smart_humsafar_life_assured_age
                                        .setText("");
                                edt_bi_smart_humsafar_proposer_age.setText("");
                                lifeAssured_date_of_birth = "";
                                spouse_date_of_birth = "";
                                lifeAssuredAge = "";
                                ProposerAge = "";
                                proposer_Backdating_BackDate = "";
                                ll_smart_humsafar_backdating
                                        .setVisibility(View.GONE);

                            }
                            proposer_Backdating_WishToBackDate_Policy = "n";
                        }
                    }
                });
    }

    public boolean valDoYouBackdate() {
        if (!proposer_Backdating_WishToBackDate_Policy.equals("")) {
            return true;
        } else {
            commonMethods.showMessageDialog(context, "Please Select Do you wish to Backdate ");


            setFocusable(rb_smart_humsafar_backdating_yes);
            rb_smart_humsafar_backdating_yes.requestFocus();
            return false;
        }
    }

    public boolean valBackdate() {
        if (proposer_Backdating_WishToBackDate_Policy.equals("y")) {
            if (proposer_Backdating_BackDate.equals("")) {
                commonMethods.showMessageDialog(context, "Please Select Backdate ");
                setFocusable(btn_smart_humsafar_backdatingdate);
                btn_smart_humsafar_backdatingdate
                        .requestFocus();
                return false;
            } else {
                return true;
            }
        }
        return true;
    }

    boolean TrueBackdate() {
        try {
            String error = "";
            if (rb_smart_humsafar_backdating_yes.isChecked()) {
                final Calendar c = Calendar.getInstance();
                final int currYear = c.get(Calendar.YEAR);
                final int currMonth = c.get(Calendar.MONTH) + 1;
                SimpleDateFormat dateformat1 = new SimpleDateFormat(
                        "dd-MM-yyyy");
                Date dtBackDate = dateformat1
                        .parse(btn_smart_humsafar_backdatingdate.getText()
                                .toString());
                Date currentDate = c.getTime();
                Date finYerEndDate = null;

                /** Added by Vrushali on19-11-2015 start **/
                Date launchDate = dateformat1.parse("15-01-2020");
                /** Added by Vrushali on 19-11-2015 end **/

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
                    commonMethods.showMessageDialog(context, error);
                    return false;
                }
            }
        } catch (Exception e) {
            return false;
        }
        return true;

    }

    protected boolean addListenerOnSubmit() {

        smartHumsafarBean = new SmartHumsafarBean();

        if (cb_staffdisc.isChecked()) {
            smartHumsafarBean.setIsStaffDiscOrNot(true);
        } else {
            smartHumsafarBean.setIsStaffDiscOrNot(false);
        }

        if (cb_kerladisc.isChecked()) {
            smartHumsafarBean.setKerlaDisc(true);
        } else {
            smartHumsafarBean.setKerlaDisc(false);
        }

        if (cb_bi_smart_humsafar_JKResident.isChecked()) {
            smartHumsafarBean.setIsJKResidentDiscOrNot(true);
        } else {
            smartHumsafarBean.setIsJKResidentDiscOrNot(false);
        }


        smartHumsafarBean.setSpouse_title(spnr_bi_smart_humsafar_proposer_title
                .getSelectedItem().toString());
        smartHumsafarBean.setSpouse_fullName(fullname_of_spouse);
        smartHumsafarBean
                .setSpouse_firstName(edt_bi_smart_humsafar_proposer_first_name
                        .getText().toString());
        smartHumsafarBean
                .setSpouse_middleName(edt_bi_smart_humsafar_proposer_middle_name
                        .getText().toString());
        smartHumsafarBean
                .setSpouse_lastName(edt_bi_smart_humsafar_proposer_last_name
                        .getText().toString());
        smartHumsafarBean.setSpouse_dob(spouse_date_of_birth);

        if (cb_bi_smart_humsafar_adb_rider.isChecked()) {
            smartHumsafarBean.setIsApplicableForADBRider(true);
            if (spnr_bi_smart_humsafar_applicable_for.getSelectedItem()
                    .toString().equalsIgnoreCase("Life to be Assured")) {
                smartHumsafarBean.setApplicableFor("Life to be Assured");
                smartHumsafarBean.setADB_PolicyTermLifeAssured(Integer
                        .parseInt(spnr_bi_smart_humsafar_adb_rider_term
                                .getSelectedItem().toString()));
                smartHumsafarBean
                        .setADBsumAssuredLA(Double
                                .parseDouble(edt_bi_smart_humsafar_adb_rider_sum_assured
                                        .getText().toString()));
            } else if (spnr_bi_smart_humsafar_applicable_for.getSelectedItem()
                    .toString().equalsIgnoreCase("Spouse Life to be Assured")) {
                smartHumsafarBean.setApplicableFor("Spouse Life to be Assured");
                smartHumsafarBean.setADB_PolicyTermSpouse(Integer
                        .parseInt(spnr_bi_smart_humsafar_spouse_rider_term
                                .getSelectedItem().toString()));
                smartHumsafarBean
                        .setADBsumAssuredSpouse(Double
                                .parseDouble(edt_bi_smart_humsafar_spouse_rider_sum_assured
                                        .getText().toString()));

            } else if (spnr_bi_smart_humsafar_applicable_for.getSelectedItem()
                    .toString().equalsIgnoreCase("Both Lives Assured")) {
                smartHumsafarBean.setApplicableFor("Both Lives Assured");
                smartHumsafarBean.setADB_PolicyTermLifeAssured(Integer
                        .parseInt(spnr_bi_smart_humsafar_adb_rider_term
                                .getSelectedItem().toString()));
                smartHumsafarBean.setADB_PolicyTermSpouse(Integer
                        .parseInt(spnr_bi_smart_humsafar_spouse_rider_term
                                .getSelectedItem().toString()));
                smartHumsafarBean
                        .setADBsumAssuredLA(Double
                                .parseDouble(edt_bi_smart_humsafar_adb_rider_sum_assured
                                        .getText().toString()));
                smartHumsafarBean
                        .setADBsumAssuredSpouse(Double
                                .parseDouble(edt_bi_smart_humsafar_spouse_rider_sum_assured
                                        .getText().toString()));
            }
        }

        smartHumsafarBean.setAgeLA(Integer
                .parseInt(edt_bi_smart_humsafar_life_assured_age.getText()
                        .toString()));
        smartHumsafarBean.setAgeSpouse(Integer
                .parseInt(edt_bi_smart_humsafar_proposer_age.getText()
                        .toString()));

        smartHumsafarBean.setGenderLA(gender_la);
        smartHumsafarBean
                .setGenderSpouse(gender_spouse);

        smartHumsafarBean.setPolicyTerm_Basic(Integer
                .parseInt(spnr_bi_smart_humsafar_policyterm.getSelectedItem()
                        .toString()));
        smartHumsafarBean
                .setPremFreqMode(spnr_bi_smart_humsafar_premium_paying_mode
                        .getSelectedItem().toString());
        smartHumsafarBean.setsumAssured(Integer
                .parseInt(edt_bi_smart_humsafar_sum_assured_amount.getText()
                        .toString()));
        return showsmartHumsafarOutputPg(smartHumsafarBean);
    }

    private void getInput(SmartHumsafarBean smartHumsafarBean) {
        inputVal = new StringBuilder();
        // From GUI Input
        String LifeAssured_DOB = btn_bi_smart_humsafar_life_assured_date
                .getText().toString();
        String LifeAssured_age = edt_bi_smart_humsafar_life_assured_age
                .getText().toString();

        String proposer_DOB = "";
        String proposer_age = "";
        String wish_to_backdate_flag = "";
        if (rb_smart_humsafar_backdating_yes.isChecked())
            wish_to_backdate_flag = "y";
        else
            wish_to_backdate_flag = "n";
        String backdate = "";
        if (wish_to_backdate_flag.equals("y"))
            backdate = btn_smart_humsafar_backdatingdate.getText().toString();
        else
            backdate = "";

        Calendar present_date = Calendar.getInstance();
        int tDay = present_date.get(Calendar.DAY_OF_MONTH);
        int tMonth = present_date.get(Calendar.MONTH);
        int tYear = present_date.get(Calendar.YEAR);
        proposer_DOB = LifeAssured_DOB;
        proposer_age = commonForAllProd.calculateMyAge(tYear, tMonth + 1, tDay,
                getDate1(proposer_DOB)) + "";

        int basicPolicyTerm = smartHumsafarBean.getPolicyTerm_Basic();
        String PremPayingMode = smartHumsafarBean.getPremFreqMode();
        double basicSumAssured = smartHumsafarBean.getsumAssured();

        String applicablefor = smartHumsafarBean.getApplicableFor();
        applicablefor = applicablefor == null ? "" : applicablefor;
        int adbTermRider_la = smartHumsafarBean.getADB_PolicyTermLifeAssured();
        int adbTermRider_spouse = smartHumsafarBean.getADB_PolicyTermSpouse();
        double adbSumAssured_la = smartHumsafarBean.getADBsumAssuredLA();
        double adbSumAssured_spouse = smartHumsafarBean
                .getADBsumAssuredSpouse();

        boolean isJKresident = smartHumsafarBean.getIsJKResidentDiscOrNot();
        boolean isStaffOrNot = smartHumsafarBean.getIsStaffDiscOrNot();
        boolean isAdbRider = smartHumsafarBean.getIsApplicableForADBRider();
        boolean smokerOrNot = false;
        String spouse_full_name = smartHumsafarBean.getSpouse_fullName();
        String spouse_dob = smartHumsafarBean.getSpouse_dob();

        inputVal.append("<?xml version='1.0' encoding='utf-8' ?><smarthumsafar>");

        inputVal.append("<LifeAssured_title>").append(lifeAssured_Title).append("</LifeAssured_title>");
        inputVal.append("<LifeAssured_firstName>").append(lifeAssured_First_Name).append("</LifeAssured_firstName>");
        inputVal.append("<LifeAssured_middleName>").append(lifeAssured_Middle_Name).append("</LifeAssured_middleName>");
        inputVal.append("<LifeAssured_lastName>").append(lifeAssured_Last_Name).append("</LifeAssured_lastName>");
        inputVal.append("<LifeAssured_DOB>").append(LifeAssured_DOB).append("</LifeAssured_DOB>");
        inputVal.append("<LifeAssured_age>").append(LifeAssured_age).append("</LifeAssured_age>");
        inputVal.append("<proposer_title>").append(lifeAssured_Title).append("</proposer_title>");
        inputVal.append("<proposer_firstName>").append(lifeAssured_First_Name).append("</proposer_firstName>");
        inputVal.append("<proposer_middleName>").append(lifeAssured_Middle_Name).append("</proposer_middleName>");
        inputVal.append("<proposer_lastName>").append(lifeAssured_Last_Name).append("</proposer_lastName>");
        inputVal.append("<proposer_DOB>").append(proposer_DOB).append("</proposer_DOB>");
        inputVal.append("<proposer_age>").append(proposer_age).append("</proposer_age>");
        inputVal.append("<proposer_gender>").append(gender_la).append("</proposer_gender>");
        inputVal.append("<product_name>").append(planName).append("</product_name>");
        /* parivartan changes */
        inputVal.append("<product_Code>").append(product_Code).append("</product_Code>");
        inputVal.append("<product_UIN>").append(product_UIN).append("</product_UIN>");
        inputVal.append("<product_cateogory>").append(product_cateogory).append("</product_cateogory>");
        inputVal.append("<product_type>").append(product_type).append("</product_type>");
        /* end */
        inputVal.append("<proposer_Is_Same_As_Life_Assured>"
                + proposer_Is_Same_As_Life_Assured
                + "</proposer_Is_Same_As_Life_Assured>");

        inputVal.append("<isJKResident>").append(isJKresident).append("</isJKResident>");
        inputVal.append("<isStaff>").append(isStaffOrNot).append("</isStaff>");
        // inputVal.append("<isSmoker>" + String.valueOf(smokerOrNot)
        // + "</isSmoker>");
        inputVal.append("<spouse_full_name>").append(spouse_full_name).append("</spouse_full_name>");
        inputVal.append("<spouse_title>").append(spouse_Title).append("</spouse_title>");
        inputVal.append("<spouse_first_name>").append(spouse_First_Name).append("</spouse_first_name>");
        inputVal.append("<spouse_middle_name>").append(spouse_Middle_Name).append("</spouse_middle_name>");
        inputVal.append("<spouse_last_name>").append(spouse_Last_Name).append("</spouse_last_name>");
        inputVal.append("<spouse_dob>").append(spouse_dob).append("</spouse_dob>");

        inputVal.append("<age_la>").append(edt_bi_smart_humsafar_life_assured_age.getText().toString()).append("</age_la>");
        inputVal.append("<age_spouse>").append(edt_bi_smart_humsafar_proposer_age.getText().toString()).append("</age_spouse>");
        inputVal.append("<gender>").append(gender_la).append("</gender>");
        inputVal.append("<gender_spouse>").append(gender_spouse).append("</gender_spouse>");

        inputVal.append("<policyTerm>").append(basicPolicyTerm).append("</policyTerm>");
        inputVal.append("<premFreq>").append(PremPayingMode).append("</premFreq>");
        inputVal.append("<sumAssured>").append(basicSumAssured).append("</sumAssured>");

        inputVal.append("<isAdbRider>").append(isAdbRider).append("</isAdbRider>");
        inputVal.append("<applicablefor>").append(applicablefor).append("</applicablefor>");
        inputVal.append("<adbTerm_la>").append(adbTermRider_la).append("</adbTerm_la>");
        inputVal.append("<adbTerm_spouse>").append(adbTermRider_spouse).append("</adbTerm_spouse>");
        inputVal.append("<adbSumassured_la>").append(adbSumAssured_la).append("</adbSumassured_la>");
        inputVal.append("<adbSumassured_spouse>").append(adbSumAssured_spouse).append("</adbSumassured_spouse>");

        inputVal.append("<Wish_to_backdate_policy>").append(wish_to_backdate_flag).append("</Wish_to_backdate_policy>");
        inputVal.append("<backdating_Date>").append(backdate).append("</backdating_Date>");

        //Added Tushar Kadam Kerla Applicable Taxes 7-Jun-2019
        String str_kerla_discount = "N";
        if (cb_kerladisc.isChecked()) {
            str_kerla_discount = "Y";
        }

        inputVal.append("<KFC>" + str_kerla_discount + "</KFC>");
        //End Added Tushar Kadam Kerla Applicable Taxes 7-Jun-2019

        inputVal.append("</smarthumsafar>");

    }

    private void Dialog() {

        final Dialog d = new Dialog(this);
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);
        d.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.BLACK));

        d.setContentView(R.layout.layout_smart_humsafar_bi_grid);

        TextView tv_proposername = (TextView) d
                .findViewById(R.id.tv_proposername);
        TextView tv_spousename = (TextView) d.findViewById(R.id.tv_spousename);
        TextView tv_proposal_number = (TextView) d
                .findViewById(R.id.tv_proposal_number);
        TextView tv_channel_type = (TextView) d
                .findViewById(R.id.tv_channel_type);

        TextView tv_bi_is_Staff = (TextView) d
                .findViewById(R.id.tv_bi_is_Staff);
        TextView tv_bi_smart_humsafar_life_assured_state = (TextView) d
                .findViewById(R.id.tv_bi_smart_humsafar_life_assured_state);
        TextView tv_bi_smart_humsafar_rate_of_app_taxes = (TextView) d
                .findViewById(R.id.tv_bi_smart_humsafar_rate_of_app_taxes);

        TextView tv_bi_is_JK = (TextView) d.findViewById(R.id.tv_bi_is_JK);

        TextView tv_bi_smart_humsafar_life_assured_age = (TextView) d
                .findViewById(R.id.tv_bi_smart_humsafar_life_assured_age);
        TextView tv_bi_smart_humsafar_life_assured_gender = (TextView) d
                .findViewById(R.id.tv_bi_smart_humsafar_life_assured_gender);
        TextView tv_bi_smart_humsafar_life_assured_is_staff_or_not = (TextView) d
                .findViewById(R.id.tv_bi_smart_humsafar_life_assured_is_staff_or_not);
        TextView tv_bi_smart_humsafar_spouse_name = (TextView) d
                .findViewById(R.id.tv_bi_smart_humsafar_spouse_name);

        TextView tv_bi_smart_humsafar_life_assured_name = (TextView) d
                .findViewById(R.id.tv_bi_smart_humsafar_life_assured_name);

        TextView tv_bi_smart_humsafar_life_assured_age2 = (TextView) d
                .findViewById(R.id.tv_bi_smart_humsafar_life_assured_age2);
        TextView tv_bi_smart_humsafar_life_assured_gender2 = (TextView) d
                .findViewById(R.id.tv_bi_smart_humsafar_life_assured_gender2);

        TextView tv_bi_smart_humsafar_life_assured_premium_frequency = (TextView) d
                .findViewById(R.id.tv_bi_smart_humsafar_life_assured_premium_frequency);
        TextView tv_bi_smart_humsafar_life_assured_equivalent_age = (TextView) d
                .findViewById(R.id.tv_bi_smart_humsafar_life_assured_equivalent_age);

        TextView tv_bi_smart_humsafar_term = (TextView) d
                .findViewById(R.id.tv_bi_smart_humsafar_term);
        TextView tv_bi_smart_humsafar_term2 = (TextView) d
                .findViewById(R.id.tv_bi_smart_humsafar_term2);
        TextView tv_bi_smart_humsafar_sum_assured = (TextView) d
                .findViewById(R.id.tv_bi_smart_humsafar_sum_assured);
        TextView tv_bi_smart_humsafar_sum_assured2 = (TextView) d
                .findViewById(R.id.tv_bi_smart_humsafar_sum_assured2);
        TextView tv_bi_smart_humsafar_sum_assured3 = (TextView) d
                .findViewById(R.id.tv_bi_smart_humsafar_sum_assured3);
        TextView tv_bi_smart_humsafar_yearly_premium = (TextView) d
                .findViewById(R.id.tv_bi_smart_humsafar_yearly_premium);

        TextView tv_bi_smart_humsafar_basic_premium = (TextView) d
                .findViewById(R.id.tv_bi_smart_humsafar_basic_premium);
        TextView tv_bi_smart_humsafar_service_tax = (TextView) d
                .findViewById(R.id.tv_bi_smart_humsafar_service_tax);
        TextView tv_bi_smart_humsafar_yearly_premium_with_tax = (TextView) d
                .findViewById(R.id.tv_bi_smart_humsafar_yearly_premium_with_tax);

        TextView inst_prem_first_year_base_prem = (TextView) d
                .findViewById(R.id.inst_prem_first_year_base_prem);
        TextView inst_prem_first_year_rider = (TextView) d
                .findViewById(R.id.inst_prem_first_year_rider);
        TextView inst_prem_first_year_total_prem = (TextView) d
                .findViewById(R.id.inst_prem_first_year_total_prem);


        TextView inst_prem_second_year_base_prem = (TextView) d
                .findViewById(R.id.inst_prem_second_year_base_prem);
        TextView inst_prem_second_year_rider = (TextView) d
                .findViewById(R.id.inst_prem_second_year_rider);
        TextView inst_prem_second_year_total_prem = (TextView) d
                .findViewById(R.id.inst_prem_second_year_total_prem);


        // First year policy
        TextView tv_bi_smart_humsafar_life_to_be_assured_term = (TextView) d
                .findViewById(R.id.tv_bi_smart_humsafar_life_to_be_assured_term);
        TextView tv_bi_smart_humsafar_life_to_be_assured_term2 = (TextView) d
                .findViewById(R.id.tv_bi_smart_humsafar_life_to_be_assured_term2);
        TextView tv_bi_smart_humsafar_name_of_proposer = (TextView) d
                .findViewById(R.id.tv_bi_smart_humsafar_name_of_proposer);

        TextView tv_bi_smart_humsafar_life_assured_name_rider = (TextView) d
                .findViewById(R.id.tv_bi_smart_humsafar_life_assured_name_rider);
        TextView tv_bi_smart_humsafar_life_to_be_assured_sum_assured = (TextView) d
                .findViewById(R.id.tv_bi_smart_humsafar_life_to_be_assured_sum_assured);
        TextView tv_bi_smart_humsafar_life_to_be_assured_sum_assured_premium = (TextView) d
                .findViewById(R.id.tv_bi_smart_humsafar_life_to_be_assured_sum_assured_premium);

        // Seconf year policy onwards
        TextView tv_bi_smart_humsafar_spouse_life_to_be_assured_term = (TextView) d
                .findViewById(R.id.tv_bi_smart_humsafar_spouse_life_to_be_assured_term);
        TextView tv_bi_smart_humsafar_spouse_life_to_be_assured_term2 = (TextView) d
                .findViewById(R.id.tv_bi_smart_humsafar_spouse_life_to_be_assured_term2);
        TextView tv_bi_smart_humsafar_spouse_life_to_be_assured_sum_assured = (TextView) d
                .findViewById(R.id.tv_bi_smart_humsafar_spouse_life_to_be_assured_sum_assured);
        TextView tv_bi_smart_humsafar_spouse_life_to_be_assured_sum_assured_premium = (TextView) d
                .findViewById(R.id.tv_bi_smart_humsafar_spouse_life_to_be_assured_sum_assured_premium);
        TextView tv_bi_smart_humsafar_backdating_interest = (TextView) d
                .findViewById(R.id.tv_bi_smart_humsafar_backdating_interest);
        final TextView edt_proposer_name = d
                .findViewById(R.id.edt_ProposalName);

        TextView tv_smart_humsafar_sbi_life_details = (TextView) d
                .findViewById(R.id.tv_smart_humsafar_sbi_life_details);


        GridView gv_userinfo = (GridView) d.findViewById(R.id.gv_userinfo);
        final EditText edt_Policyholderplace = (EditText) d
                .findViewById(R.id.edt_Policyholderplace);

        final TextView tv_premium_type = (TextView) d
                .findViewById(R.id.tv_premium_type);

        final TextView tv_premium_install_type = (TextView) d
                .findViewById(R.id.tv_premium_install_type);

        final TextView tv_premium_install_type1 = (TextView) d
                .findViewById(R.id.tv_premium_install_rider_type1);

        final EditText edt_MarketingOfficalPlace = (EditText) d
                .findViewById(R.id.edt_MarketingOfficalPlace);

        TextView tv_mandatory_bi_smart_humsafar_yearly_premium_with_tax1 = (TextView) d
                .findViewById(R.id.tv_mandatory_bi_smart_humsafar_yearly_premium_with_tax1);

        TableRow tr_bi_smart_humsafar_adb_rider = (TableRow) d
                .findViewById(R.id.tr_bi_smart_humsafar_adb_rider);
        TableRow tr_bi_smart_humsafar_adb_rider_applicable_for = (TableRow) d
                .findViewById(R.id.tr_bi_smart_humsafar_adb_rider_applicable_for);
        TableRow tr_bi_smart_humsafar_adb_rider_life_to_be_assured = (TableRow) d
                .findViewById(R.id.tr_bi_smart_humsafar_adb_rider_life_to_be_assured);
        TableRow tr_bi_smart_humsafar_adb_rider_spouse_life_to_be_assured = (TableRow) d
                .findViewById(R.id.tr_bi_smart_humsafar_adb_rider_spouse_life_to_be_assured);

        final CheckBox cb_statement = (CheckBox) d
                .findViewById(R.id.cb_statement);
        cb_statement.setChecked(false);

        TextView tv_bi_smart_humsafar_basic_service_tax_first_year = (TextView) d
                .findViewById(R.id.tv_bi_smart_humsafar_basic_service_tax_first_year);
        TextView tv_bi_smart_humsafar_swachh_bharat_cess_first_year = (TextView) d
                .findViewById(R.id.tv_bi_smart_humsafar_swachh_bharat_cess_first_year);
        TextView tv_bi_smart_humsafar_krishi_kalyan_cess_first_year = (TextView) d
                .findViewById(R.id.tv_bi_smart_humsafar_krishi_kalyan_cess_first_year);

        TextView tv_uin_smart_humsafar = (TextView) d
                .findViewById(R.id.tv_uin_smart_humsafar);
        /* Need Analysis */
        final TextView edt_proposer_name_need_analysis = d
                .findViewById(R.id.edt_proposer_name_need_analysis);

        final CheckBox cb_statement_need_analysis = d
                .findViewById(R.id.cb_statement_need_analysis);
        cb_statement_need_analysis.setChecked(true);
        TableRow tr_need_analysis = (TableRow) d
                .findViewById(R.id.tr_need_analysis);
        final CheckBox checkboxAgentStatement = d.findViewById(R.id.checkboxAgentStatement);
        checkboxAgentStatement.setChecked(false);
        /* parivartan changes */
        imageButtonSmartHumsafarLifeAssuredPhotograph = d
                .findViewById(R.id.imageButtonSmartHumsafarLifeAssuredPhotograph);
        imageButtonSmartHumsafarProposerPhotograph = d
                .findViewById(R.id.imageButtonSmartHumsafarProposerPhotograph);

        imageButtonSmartHumsafarLifeAssuredPhotograph
                .setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Check = "Photo";
                        commonMethods.windowmessage(context, "_cust1Photo.jpg");
                    }
                });

        imageButtonSmartHumsafarProposerPhotograph
                .setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Check = "ProposerPhoto";
                        commonMethods.windowmessage(context, "_cust2Photo.jpg");
                    }
                });
        /* end */

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
        list_data2.clear();

        input = inputVal.toString();
        output = retVal.toString();
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
                            + ", having received the information with respect to the above, have understood the above statement before entering into a contract.");
            edt_proposer_name_need_analysis
                    .setText("I, "
                            + name_of_life_assured
                            + ", have undergone the Need Analysis  after having reviewed the SBI Life Product options, I have opted for SBI LIFE - Smart Humsafar.");

            // tv_life_assured_name.setText(name_of_life_assured);
            tv_proposername.setText(name_of_life_assured);
            //tv_spousename.setText(fullname_of_spouse);
        } else if (proposer_Is_Same_As_Life_Assured.equalsIgnoreCase("n")) {
            edt_proposer_name
                    .setText("I, "
                            + name_of_proposer
                            + ", having received the information with respect to the above, have understood the above statement before entering into a contract.");
            edt_proposer_name_need_analysis
                    .setText("I, "
                            + name_of_proposer
                            + ", have undergone the Need Analysis  after having reviewed the SBI Life Product options, I have opted for SBI LIFE - Smart Humsafar.");

            tv_proposername.setText(name_of_life_assured);
            //tv_spousename.setText(fullname_of_spouse);
        }
        tv_spousename.setText(fullname_of_spouse);
        tv_proposal_number.setText(QuatationNumber);
        tv_channel_type.setText(userType);
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
                            imageButtonSmartHumsafarLifeAssuredPhotograph
                                    .setImageDrawable(getResources()
                                            .getDrawable(
                                                    R.drawable.focus_imagebutton_photo));

                            String customerTwoPhotoName = NeedAnalysisActivity.URN_NO
                                    + "_cust2Photo.jpg";
                            File customerTwoPhotoFile = mStorageUtils.createFileToAppSpecificDir(context,
                                    customerTwoPhotoName);
                            if (customerTwoPhotoFile.exists()) {
                                customerTwoPhotoFile.delete();
                            }

                            proposerBitmap = null;
                            imageButtonSmartHumsafarProposerPhotograph
                                    .setImageDrawable(getResources()
                                            .getDrawable(
                                                    R.drawable.focus_imagebutton_photo));

                        }
                    }
                });


        if (photoBitmap != null) {
            imageButtonSmartHumsafarLifeAssuredPhotograph
                    .setImageBitmap(photoBitmap);
        }

        if (proposerBitmap != null) {
            imageButtonSmartHumsafarProposerPhotograph
                    .setImageBitmap(proposerBitmap);
        }
        Ibtn_signatureofLifeAssured = d
                .findViewById(R.id.Ibtn_signatureofLifeAssured);

        if (proposerAsLifeAssuredSign != null
                && !proposerAsLifeAssuredSign.equals("")) {
            byte[] signByteArray = Base64.decode(proposerAsLifeAssuredSign, 0);
            Bitmap bitmap = BitmapFactory.decodeByteArray(signByteArray, 0,
                    signByteArray.length);
            Ibtn_signatureofLifeAssured.setImageBitmap(bitmap);
        }

        Ibtn_signatureofLifeAssured
                .setOnClickListener(new OnClickListener() {

                    public void onClick(View view) {

                        if (cb_statement.isChecked()
                                && cb_statement_need_analysis.isChecked()) {
                            latestImage = "proposerSignOnLifeAssured";
                            commonMethods.windowmessageProposersgin(context,
                                    NeedAnalysisActivity.URN_NO + "_cust2sign");
                        } else {
                            commonMethods.dialogWarning(context, "Please Tick on I Agree Clause ", true);
                            setFocusable(cb_statement);
                            cb_statement.requestFocus();
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
                        && (((proposerBitmap != null
                        && photoBitmap != null
                ) && radioButtonTrasactionModeParivartan
                        .isChecked()) || radioButtonTrasactionModeManual
                        .isChecked())) {
                    NeedAnalysisActivity.str_need_analysis = "";

                    // String isActive = "0";
                    String productCode = "1WHS";

                    na_cbi_bean = new NA_CBI_bean(QuatationNumber, agentcode,
                            "", userType, "", lifeAssured_Title,
                            lifeAssured_First_Name, lifeAssured_Middle_Name,
                            lifeAssured_Last_Name, planName,
                            obj.getRound(obj.getStringWithout_E(Double
                                    .valueOf(sum_assured.equals("") ? "0"
                                            : sum_assured))), obj
                            .getRound(yearlypremium_with_servicetax),
                            emailId, mobileNo, agentEmail, agentMobile,
                            na_input, na_output, premium_paying_frequency,
                            Integer.parseInt(policy_term), 0, productCode,
                            getDate(lifeAssured_date_of_birth),
                            getDate(proposer_date_of_birth), inputVal
                            .toString(), retVal.toString().replace(
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
                            obj.getRound(obj.getStringWithout_E(Double
                                    .valueOf(sum_assured.equals("") ? "0"
                                            : sum_assured))), obj
                            .getRound(yearlypremium_with_servicetax),
                            agentEmail, agentMobile, na_input, na_output,
                            premium_paying_frequency, Integer
                            .parseInt(policy_term), 0, productCode,
                            getDate(lifeAssured_date_of_birth),
                            getDate(proposer_date_of_birth), "", mode, inputVal
                            .toString(), retVal.toString().replace(
                            bussIll.toString(), "")));

                    CreateSmartHumsafarPlusBIPdf(smartHumsafarBean);

                    NABIObj.serviceHit(BI_SmartHumsafarActivity.this,
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
                        commonMethods.dialogWarning(context, "Please Capture Life Assured Photo", true);
                        setFocusable(imageButtonSmartHumsafarLifeAssuredPhotograph);
                        imageButtonSmartHumsafarLifeAssuredPhotograph
                                .requestFocus();
                    } else if (proposerBitmap == null) {
                        commonMethods.dialogWarning(context, "Please Capture Spouse Photo", true);
                        setFocusable(imageButtonSmartHumsafarProposerPhotograph);
                        imageButtonSmartHumsafarProposerPhotograph
                                .requestFocus();
                    } else if (proposerAsLifeAssuredSign.equals("")) {
                        commonMethods.dialogWarning(context, "Please Make Signature for Life Assured ", true);
                        setFocusable(Ibtn_signatureofLifeAssured);
                        Ibtn_signatureofLifeAssured.requestFocus();
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

        tv_uin_smart_humsafar
                .setText("Benefit Illustration (BI) : SBI Life -Smart Humsafar (UIN : 111N103V03) | An Individual, Non-Linked, Participating, Life Insurance Savings Product ");
        age_entry = prsObj.parseXmlTag(input, "age_la");
        tv_bi_smart_humsafar_life_assured_age.setText(age_entry + " Years");

        tv_bi_smart_humsafar_life_assured_gender.setText(gender_la);

        tv_bi_smart_humsafar_life_assured_name.setText(name_of_life_assured);
        tv_bi_smart_humsafar_life_assured_name_rider.setText(name_of_life_assured);

        staffdiscount = prsObj.parseXmlTag(input, "isStaff");
        if (staffdiscount.equalsIgnoreCase("true")) {
            tv_bi_smart_humsafar_life_assured_is_staff_or_not.setText("Yes");
        } else {
            tv_bi_smart_humsafar_life_assured_is_staff_or_not.setText("No");
        }

        if (cb_kerladisc.isChecked()) {
            tv_bi_smart_humsafar_life_assured_state.setText("Kerala");
            tv_bi_smart_humsafar_rate_of_app_taxes.setText(" 4.75% in the 1st policy year and 2.375% from 2nd policy year onwards");

        } else {
            tv_bi_smart_humsafar_life_assured_state.setText("Non Kerala");
            tv_bi_smart_humsafar_rate_of_app_taxes.setText(" 4.5% in the 1st policy year and 2.25% from 2nd policy year onwards ");


        }

        String age2 = prsObj.parseXmlTag(input, "age_spouse");
        tv_bi_smart_humsafar_life_assured_age2.setText(age2 + " Years");

        tv_bi_smart_humsafar_life_assured_gender2.setText(gender_spouse);


        tv_bi_smart_humsafar_spouse_name.setText(spouse_Title + " " + spouse_First_Name + " " + spouse_Middle_Name
                + " " + spouse_Last_Name);

        String equivalent_age = prsObj.parseXmlTag(output, "equivalentAge");
        tv_bi_smart_humsafar_life_assured_equivalent_age
                .setText(equivalent_age);

        premium_paying_frequency = prsObj.parseXmlTag(input, "premFreq");
        tv_bi_smart_humsafar_life_assured_premium_frequency
                .setText(premium_paying_frequency);

        policy_term = prsObj.parseXmlTag(input, "policyTerm");
        tv_bi_smart_humsafar_term.setText(policy_term + " Years");
        tv_bi_smart_humsafar_term2.setText(policy_term + " Years");


        sum_assured = prsObj.parseXmlTag(input, "sumAssured");

        benefit_payable_on_first_death = prsObj.parseXmlTag(output,
                "GuaranteedDeathBenefitFirstDeath" + 1 + "");
        benefit_payable_on_second_death = prsObj.parseXmlTag(output,
                "GuaranteedDeathBenefitSecondDeath" + 1 + "");

        tv_bi_smart_humsafar_sum_assured.setText(""
                + getformatedThousandString(Integer.parseInt(obj.getRound(obj
                .getStringWithout_E(Double.valueOf((sum_assured
                        .equals("") || sum_assured == null) ? "0"
                        : sum_assured))))));
        tv_bi_smart_humsafar_sum_assured2.setText(""
                + getformatedThousandString(Integer.parseInt(obj.getRound(obj
                .getStringWithout_E(Double.valueOf((benefit_payable_on_first_death
                        .equals("") || benefit_payable_on_first_death == null) ? "0"
                        : benefit_payable_on_first_death))))));
        tv_bi_smart_humsafar_sum_assured3.setText(" "
                + getformatedThousandString(Integer.parseInt(obj.getRound(obj
                .getStringWithout_E(Double.valueOf((benefit_payable_on_second_death
                        .equals("") || benefit_payable_on_second_death == null) ? "0"
                        : benefit_payable_on_second_death))))));

        installmntPrem = prsObj.parseXmlTag(output, "installmntPrem");
        YearlyPrem = prsObj.parseXmlTag(output, "YearlyPrem");
        tv_bi_smart_humsafar_yearly_premium.setText("Rs "
                + obj.getRound(obj.getStringWithout_E(Double.valueOf((YearlyPrem
                .equals("") || YearlyPrem == null) ? "0" : YearlyPrem))));

        basicprem = prsObj.parseXmlTag(output, "basicPremiumWtRider");

        tv_bi_smart_humsafar_basic_premium.setText(""
                + getformatedThousandString(Integer.parseInt(obj.getRound(obj.getStringWithout_E(Double.valueOf((YearlyPrem
                .equals("") || YearlyPrem == null) ? "0" : YearlyPrem))))));

        servicetax = prsObj.parseXmlTag(output, "serviceTax");
        installmntPremWithSerTx = prsObj.parseXmlTag(output, "installmntPremWithSerTx");
        premiumInstWithSTFirstYearRider = prsObj.parseXmlTag(output, "premiumInstWithSTFirstYearRider");
        totalInstPremFirstYear = prsObj.parseXmlTag(output, "totalInstPremFirstYear");
        InstBasicRider = prsObj.parseXmlTag(output, "InstBasicRider");
        tv_bi_smart_humsafar_service_tax
                .setText(""
                        + obj.getRound(obj.getStringWithout_E(Double
                        .valueOf((InstBasicRider.equals("") || InstBasicRider == null) ? "0"
                                : InstBasicRider))));

        yearlypremium_with_servicetax = prsObj.parseXmlTag(output,
                "basicPremiumWtRiderWthST");
        BasicInstPremSecondYear = prsObj.parseXmlTag(output, "BasicInstPremSecondYear");
        premiumInstWithSTSecondYearRiderSY = prsObj.parseXmlTag(output, "premiumInstWithSTSecondYearRiderSY");
        totalInstPremSecondYearSY = prsObj.parseXmlTag(output, "totalInstPremSecondYearSY");

        tv_bi_smart_humsafar_yearly_premium_with_tax.setText(""
                + getformatedThousandString(Integer.parseInt(obj.getRound(obj.getStringWithout_E(Double.valueOf((installmntPrem
                .equals("") || installmntPrem == null) ? "0"
                : installmntPrem))))));


//first year premium
        inst_prem_first_year_base_prem.setText(""
                + getformatedThousandString(Integer.parseInt(obj.getRound(obj.getStringWithout_E(Double.valueOf((installmntPremWithSerTx
                .equals("") || installmntPremWithSerTx == null) ? "0"
                : installmntPremWithSerTx))))));
        inst_prem_first_year_rider.setText(""
                + obj.getRound(obj.getStringWithout_E(Double.valueOf((premiumInstWithSTFirstYearRider
                .equals("") || premiumInstWithSTFirstYearRider == null) ? "0"
                : premiumInstWithSTFirstYearRider))));
        inst_prem_first_year_total_prem.setText(""
                + getformatedThousandString(Integer.parseInt(obj.getRound(obj.getStringWithout_E(Double.valueOf((totalInstPremFirstYear
                .equals("") || totalInstPremFirstYear == null) ? "0"
                : totalInstPremFirstYear))))));
//second year premium
        inst_prem_second_year_base_prem.setText(""
                + getformatedThousandString(Integer.parseInt(obj.getRound(obj.getStringWithout_E(Double.valueOf((BasicInstPremSecondYear
                .equals("") || BasicInstPremSecondYear == null) ? "0"
                : BasicInstPremSecondYear))))));
        inst_prem_second_year_rider.setText(""
                + obj.getRound(obj.getStringWithout_E(Double.valueOf((premiumInstWithSTSecondYearRiderSY
                .equals("") || premiumInstWithSTSecondYearRiderSY == null) ? "0"
                : premiumInstWithSTSecondYearRiderSY))));
        inst_prem_second_year_total_prem.setText(""
                + getformatedThousandString(Integer.parseInt(obj.getRound(obj.getStringWithout_E(Double.valueOf((totalInstPremSecondYearSY
                .equals("") || totalInstPremSecondYearSY == null) ? "0"
                : totalInstPremSecondYearSY))))));


        // Amit changes start- 23-5-2016
        // basicServiceTax = prsObj.parseXmlTag(output, "basicServiceTax");
        basicServiceTax = prsObj.parseXmlTag(output, "serviceTax");

        tv_bi_smart_humsafar_basic_service_tax_first_year.setText(""
                + obj.getRound(obj.getStringWithout_E(Double
                .valueOf(InstBasicRider.equals("") ? "0"
                        : InstBasicRider))));

        SBCServiceTax = prsObj.parseXmlTag(output, "SBCServiceTax");

        tv_bi_smart_humsafar_swachh_bharat_cess_first_year
                .setText(""
                        + obj.getRound(obj.getStringWithout_E(Double
                        .valueOf(SBCServiceTax.equals("") ? "0"
                                : SBCServiceTax))));

        KKCServiceTax = prsObj.parseXmlTag(output, "KKCServiceTax");

        tv_bi_smart_humsafar_krishi_kalyan_cess_first_year
                .setText(""
                        + obj.getRound(obj.getStringWithout_E(Double
                        .valueOf(KKCServiceTax.equals("") ? "0"
                                : KKCServiceTax))));

        // Amit changes end- 23-5-2016

        if (premium_paying_frequency.equals("Yearly")) {
            // tv_premium_type.setText("Yearly premium(Rs.) ");
            //tv_premium_install_type.setText("Yearly premium(Rs.) ");
            // tv_mandatory_bi_smart_humsafar_yearly_premium_with_tax1
            //         .setText("Yearly premium (Rs.)");
            //tv_premium_install_type1.setText("Yearly Premium with Applicable Tax(Rs.)");

        } else if (premium_paying_frequency.equals("Half Yearly")) {
            // tv_premium_type.setText("Half Yearly premium(Rs.)");
            //tv_premium_install_type.setText("Half Yearly premium(Rs.)");
            //  tv_mandatory_bi_smart_humsafar_yearly_premium_with_tax1
            //          .setText("Half Yearly premium(Rs.)");
            //tv_premium_install_type1
            //        .setText("Half Yearly Premium with Applicable Tax(Rs.)");

        } else if (premium_paying_frequency.equals("Quarterly")) {
            //tv_premium_type.setText("Quarterly premium(Rs.)");
            //tv_premium_install_type.setText("Quarterly premium(Rs.)");
            // tv_mandatory_bi_smart_humsafar_yearly_premium_with_tax1
            //         .setText("Quarterly premium(Rs.)");
            //tv_premium_install_type1.setText("Quarterly Premium with Applicable Tax(Rs.)");
        } else if (premium_paying_frequency.equals("Monthly")) {
            // tv_premium_type.setText("Monthly premium(Rs.)");
            //tv_premium_install_type.setText("Monthly premium(Rs.)");
            //  tv_mandatory_bi_smart_humsafar_yearly_premium_with_tax1
            //         .setText("Monthly premium(Rs.)");
            // tv_premium_install_type1.setText("Monthly Premium with Applicable Tax(Rs.)");

        } else if (premium_paying_frequency.equals("Single")) {
            //tv_premium_type.setText("Single premium(Rs.)");
            //tv_premium_install_type.setText("Single premium(Rs.)");
            // tv_mandatory_bi_smart_humsafar_yearly_premium_with_tax1
            //         .setText("Single premium(Rs.)");
            // tv_premium_install_type1.setText("Single Premium with Applicable Tax(Rs.)");
        }

        adb_rider_status = prsObj.parseXmlTag(input, "isAdbRider");
        applicable_for = prsObj.parseXmlTag(input, "applicablefor");
        if (adb_rider_status.equals("true")) {
            tr_bi_smart_humsafar_adb_rider.setVisibility(View.VISIBLE);
            tr_bi_smart_humsafar_adb_rider_applicable_for
                    .setVisibility(View.VISIBLE);
            if (applicable_for.equals("Life to be Assured")) {
                tr_bi_smart_humsafar_adb_rider_life_to_be_assured
                        .setVisibility(View.VISIBLE);
            } else if (applicable_for.equals("Spouse Life to be Assured")) {
                tr_bi_smart_humsafar_adb_rider_spouse_life_to_be_assured
                        .setVisibility(View.VISIBLE);
            } else if (applicable_for.equals("Both Lives Assured")) {
                tr_bi_smart_humsafar_adb_rider_life_to_be_assured
                        .setVisibility(View.VISIBLE);
                tr_bi_smart_humsafar_adb_rider_spouse_life_to_be_assured
                        .setVisibility(View.VISIBLE);
            }

        } else {
            tr_bi_smart_humsafar_adb_rider.setVisibility(View.GONE);
            tr_bi_smart_humsafar_adb_rider_applicable_for
                    .setVisibility(View.GONE);
            tr_bi_smart_humsafar_adb_rider_life_to_be_assured
                    .setVisibility(View.GONE);
            tr_bi_smart_humsafar_adb_rider_spouse_life_to_be_assured
                    .setVisibility(View.GONE);
        }

        if (adb_rider_status.equals("true")) {
            life_to_be_assured_term = prsObj.parseXmlTag(input, "adbTerm_la");
            life_to_be_assured_sum_assured = prsObj.parseXmlTag(input,
                    "adbSumassured_la");
            life_to_be_assured_sum_assured_premium = prsObj.parseXmlTag(output,
                    "AdbRiderPremLifeAssured");
            spouse_life_to_be_assured_term = prsObj.parseXmlTag(input,
                    "adbTerm_spouse");
            spouse_life_to_be_assured_sum_assured = prsObj.parseXmlTag(input,
                    "adbSumassured_spouse");
            spouse_life_to_be_assured_sum_assured_premium = prsObj.parseXmlTag(
                    output, "AdbRiderPremSpouse");

            tv_bi_smart_humsafar_name_of_proposer.setText(spouse_Title + " " + spouse_First_Name + " " + spouse_Middle_Name
                    + " " + spouse_Last_Name);

            tv_bi_smart_humsafar_life_to_be_assured_term.setText(obj
                    .getRound(obj.getStringWithout_E(Double
                            .valueOf(life_to_be_assured_term.equals("") ? "0"
                                    : life_to_be_assured_term))));

            tv_bi_smart_humsafar_life_to_be_assured_term2.setText(obj
                    .getRound(obj.getStringWithout_E(Double
                            .valueOf(life_to_be_assured_term.equals("") ? "0"
                                    : life_to_be_assured_term))));

            tv_bi_smart_humsafar_life_to_be_assured_sum_assured
                    .setText(getformatedThousandString(Integer.parseInt(obj.getRound(obj.getStringWithout_E(Double.valueOf(life_to_be_assured_sum_assured
                            .equals("") ? "0" : life_to_be_assured_sum_assured))))));

            tv_bi_smart_humsafar_life_to_be_assured_sum_assured_premium
                    .setText(obj.getRound(obj.getStringWithout_E(Double
                            .valueOf(life_to_be_assured_sum_assured_premium
                                    .equals("") ? "0"
                                    : life_to_be_assured_sum_assured_premium))));

            tv_bi_smart_humsafar_spouse_life_to_be_assured_term
                    .setText(obj.getRound(obj.getStringWithout_E(Double.valueOf(spouse_life_to_be_assured_term
                            .equals("") ? "0" : spouse_life_to_be_assured_term))));
            tv_bi_smart_humsafar_spouse_life_to_be_assured_term2
                    .setText(obj.getRound(obj.getStringWithout_E(Double.valueOf(spouse_life_to_be_assured_term
                            .equals("") ? "0" : spouse_life_to_be_assured_term))));

            tv_bi_smart_humsafar_spouse_life_to_be_assured_sum_assured
                    .setText(getformatedThousandString(Integer.parseInt(obj.getRound(obj.getStringWithout_E(Double
                            .valueOf(spouse_life_to_be_assured_sum_assured
                                    .equals("") ? "0"
                                    : spouse_life_to_be_assured_sum_assured))))));

            tv_bi_smart_humsafar_spouse_life_to_be_assured_sum_assured_premium
                    .setText(obj.getRound(obj.getStringWithout_E(Double
                            .valueOf(spouse_life_to_be_assured_sum_assured_premium
                                    .equals("") ? "0"
                                    : spouse_life_to_be_assured_sum_assured_premium))));
        }

        BackdatingInt = prsObj.parseXmlTag(output, "backdateInt");

        tv_bi_smart_humsafar_backdating_interest
                .setText(" Rs "
                        + obj.getRound(obj.getStringWithout_E(Double
                        .valueOf(BackdatingInt.equals("") ? "0"
                                : BackdatingInt))));

      /*  tv_smart_humsafar_sbi_life_details
                .setText("Your SBI LIFE - Smart Humsafar(UIN: 111N103V03) is a Regular/Limited premium policy,for which your first year "
                + premium_paying_frequency
                + " premium is Rs. "
                + yearlypremium_with_servicetax
                        + " Your policy Term is "
                + policy_term
                + " years,Premium Payment Term is "
                + policy_term
                + " years and Basic Sum Assured is Rs. "
                + getformatedThousandString(Integer.parseInt(obj.getRound(obj.getStringWithout_E(Double
                .valueOf((sum_assured.equals("") || sum_assured == null) ? "0"
                                : sum_assured))))));*/

        for (int i = 1; i <= Integer.parseInt(policy_term); i++) {

            String policy_year = prsObj
                    .parseXmlTag(output, "policyYr" + i + "");
            String total_base_premium_without_tax = prsObj.parseXmlTag(output,
                    "totalBasePremiumPaid" + i + "");
            String annulizedPremium = prsObj.parseXmlTag(output,
                    "annulizedPremium" + i + "");
            String GuaranteedAddition = prsObj.parseXmlTag(output,
                    "GuaranteedAddition" + i + "");
            String benefit_payable_on_first_death = prsObj.parseXmlTag(output,
                    "GuaranteedDeathBenefitFirstDeath" + i + "");
            String death_gurantee = prsObj.parseXmlTag(output,
                    "GuaranteedDeathBenefitSecondDeath" + i + "");
            String GuaranteedMaturityBenefitBenefit = prsObj.parseXmlTag(output,
                    "GuaranteedMaturityBenefitBenefit" + i + "");

            String ReversionaryBonus4Per = prsObj.parseXmlTag(
                    output, "NonGuarateedDeathBenefitAt_4_Percent" + i + "");
            String ReversionaryBonus8Per = prsObj.parseXmlTag(
                    output, "NonGuarateedDeathBenefitAt_8_Percent" + i + "");
            String guaranteed_survival_benefit = prsObj.parseXmlTag(output,
                    "GuaranteedSurvivalBenefit" + i + "");
            String maturity_benefit_non_gurantee_4_percentage = prsObj
                    .parseXmlTag(output,
                            "NonGuarateedSurvivalBenefitAt_4_Percent" + i + "");
            String maturity_benefit_non_gurantee_8_percentage = prsObj
                    .parseXmlTag(output,
                            "NonGuarateedSurvivalBenefitAt_8_Percent" + i + "");
            String guaranteed_surrender_value = prsObj.parseXmlTag(output,
                    "GSV_surrendr_val" + i + "");
            String surrender_value_ssv_4_percentage = prsObj.parseXmlTag(
                    output, "NonGSV_surrndr_val_4Percent" + i + "");
            String surrender_value_ssv_8_percentage = prsObj.parseXmlTag(
                    output, "NonGSV_surrndr_val_8Percent" + i + "");

            String TotalMaturityBenefit4percent = prsObj.parseXmlTag(
                    output, "TotalMaturityBenefit4percent" + i + "");

            String TotalMaturityBenefit8percent = prsObj.parseXmlTag(
                    output, "TotalMaturityBenefit8percent" + i + "");
            String TotalDeathBenefit4perecentFirst = prsObj.parseXmlTag(
                    output, "TotalDeathBenefit4perecentFirst" + i + "");
            String TotalDeathBenefit8perecentFirst = prsObj.parseXmlTag(
                    output, "TotalDeathBenefit8perecentFirst" + i + "");


            String TotalDeathBenefit4perecentSecond = prsObj.parseXmlTag(
                    output, "TotalDeathBenefit4perecentSecond" + i + "");
            String TotalDeathBenefit8perecentSecond = prsObj.parseXmlTag(
                    output, "TotalDeathBenefit8perecentSecond" + i + "");


            list_data.add(new M_BI_SmartHumsafar_AdapterCommon(policy_year,
                    (obj.getRound(obj.getStringWithout_E(Double
                            .valueOf(annulizedPremium))))
                            + "",
                    (obj.getRound(obj.getStringWithout_E(Double
                            .valueOf(GuaranteedAddition))))
                            + "",
                    (obj.getRound(obj.getStringWithout_E(Double
                            .valueOf(guaranteed_survival_benefit))))
                            + "",
                    (obj.getRound(obj.getStringWithout_E(Double
                            .valueOf(guaranteed_surrender_value))))
                            + "",
                    (obj.getRound(obj.getStringWithout_E(Double
                            .valueOf(benefit_payable_on_first_death))))
                            + "",
                    (obj.getRound(obj.getStringWithout_E(Double
                            .valueOf(death_gurantee)))) + "",
                    (obj.getRound(obj.getStringWithout_E(Double
                            .valueOf(GuaranteedMaturityBenefitBenefit))))
                            + "",
                    (obj.getRound(obj.getStringWithout_E(Double
                            .valueOf(ReversionaryBonus4Per))))
                            + "",
                    "0",
                    (obj.getRound(obj.getStringWithout_E(Double
                            .valueOf(surrender_value_ssv_4_percentage))))
                            + "",
                    (obj.getRound(obj.getStringWithout_E(Double
                            .valueOf(ReversionaryBonus8Per))))
                            + "",
                    "0",
                    (obj.getRound(obj.getStringWithout_E(Double
                            .valueOf(surrender_value_ssv_8_percentage))))
                            + "",
                    (obj.getRound(obj.getStringWithout_E(Double
                            .valueOf(TotalMaturityBenefit4percent))))
                            + "",
                    (obj.getRound(obj.getStringWithout_E(Double
                            .valueOf(TotalMaturityBenefit8percent))))
                            + "",
                    (obj.getRound(obj.getStringWithout_E(Double
                            .valueOf(TotalDeathBenefit4perecentFirst))))
                            + "",
                    (obj.getRound(obj.getStringWithout_E(Double
                            .valueOf(TotalDeathBenefit8perecentFirst))))
                            + "",
                    (obj.getRound(obj.getStringWithout_E(Double
                            .valueOf(TotalDeathBenefit4perecentSecond))))
                            + "",
                    (obj.getRound(obj.getStringWithout_E(Double
                            .valueOf(TotalDeathBenefit8perecentSecond))))
                            + ""
            ));
        }

        Adapter_BI_SmartHumsafarGridCommon adapter = new Adapter_BI_SmartHumsafarGridCommon(
                this, list_data);
        gv_userinfo.setAdapter(adapter);
        GridHeight gh = new GridHeight();
        gh.getheight(gv_userinfo, policy_term);


        d.show();

    }

    private void CreateSmartHumsafarPlusBIPdf(SmartHumsafarBean smarthumsafarbean) {

        // SmartHumsafarBean smarthumsafarbean=new SmartHumsafarBean();
        // smarthumsafarbean=SmartHumsafarActivity.smarthumsafarbeanStatic ;
        try {

            ParseXML prsObj = new ParseXML();
            System.out.println("Final output in BIPDF" + output);
            String installmntPrem = prsObj
                    .parseXmlTag(output, "installmntPrem");
            String basicPremiumWtRider = prsObj.parseXmlTag(output,
                    "basicPremiumWtRider");
            String serviceTax = prsObj.parseXmlTag(output, "serviceTax");
            String basicPremiumWtRiderWthST = prsObj.parseXmlTag(output,
                    "basicPremiumWtRiderWthST");
            String equivalentAge = prsObj.parseXmlTag(output, "equivalentAge");

            String AdbRiderPremLifeAssured = prsObj.parseXmlTag(output,
                    "AdbRiderPremLifeAssured");
            String AdbRiderPremSpouse = prsObj.parseXmlTag(output,
                    "AdbRiderPremSpouse");
            String BASE_FONT_BOLD = "Trebuchet MS B";

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
            Font small_bold_for_name = new Font(Font.FontFamily.TIMES_ROMAN,
                    10, Font.BOLD);

            Font small_bold1 = new Font(Font.FontFamily.TIMES_ROMAN, 6,
                    Font.BOLD);

            Font small_normal1 = new Font(Font.FontFamily.TIMES_ROMAN, 6,
                    Font.NORMAL);
            Font small_normal = new Font(Font.FontFamily.TIMES_ROMAN, 8,
                    Font.NORMAL);
            Font normal1_bold = new Font(Font.FontFamily.TIMES_ROMAN, 6,
                    Font.BOLD);

            Font normal_bold = new Font(Font.FontFamily.TIMES_ROMAN, 5,
                    Font.BOLD);
            Font normal = new Font(Font.FontFamily.TIMES_ROMAN, 5, Font.NORMAL);

            float[] columnWidths2 = {2f, 1f};
            float[] columnWidths4 = {2f, 1f, 2f, 1f};
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
                            "Benefit Illustration (BI) : SBI Life -Smart Humsafar (UIN : 111N103V03) | An Individual, Non-Linked, Participating, Life Insurance Savings Product "
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

            document.add(para_img_logo_after_space_1);
            if (proposer_Is_Same_As_Life_Assured.equalsIgnoreCase("Y")) {
                name_of_proposer = name_of_life_assured;
            }

            PdfPTable table_proposer_name = new PdfPTable(2);
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

            PdfPCell NameofSpouse_cell_5 = new PdfPCell(new Paragraph(
                    "Spouse Name ", small_normal));
            PdfPCell NameofSpouse_cell_6 = new PdfPCell(new Paragraph(
                    fullname_of_spouse, small_bold1));
            NameofProposal_cell_4.setHorizontalAlignment(Element.ALIGN_CENTER);

            NameofProposal_cell_4.setVerticalAlignment(Element.ALIGN_CENTER);
            NameofSpouse_cell_6.setHorizontalAlignment(Element.ALIGN_CENTER);

            NameofSpouse_cell_6.setVerticalAlignment(Element.ALIGN_CENTER);

            ProposalNumber_cell_1.setPadding(5);
            ProposalNumber_cell_2.setPadding(5);
            NameofProposal_cell_3.setPadding(5);
            NameofProposal_cell_4.setPadding(5);
            // NameofSpouse_cell_5.setPadding(5);
            // NameofSpouse_cell_6.setPadding(5);

            table_proposer_name.addCell(ProposalNumber_cell_1);
            table_proposer_name.addCell(ProposalNumber_cell_2);
            table_proposer_name.addCell(NameofProposal_cell_3);
            table_proposer_name.addCell(NameofProposal_cell_4);
            //table_proposer_name.addCell(NameofSpouse_cell_5);
            //table_proposer_name.addCell(NameofSpouse_cell_6);
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
                            "Insurance Regulatory & Development Authority of India (IRDAI) requires all life insurance companies operating in India to provide official illustrations to their customers. The illustrations are based on the investment rates of return set by the Life Insurance Council (constituted under Section 64C(a) of the Insurance Act 1938) and is not intended to reflect the actual investment returns achieved or which may be achieved in future by SBI life Insurance Company Limited.  All life insurance companies use the same rates in their benefit illustrations.",
                            small_normal));

            BI_Pdftable2_cell1.setPadding(5);

            BI_Pdftable2.addCell(BI_Pdftable2_cell1);
            document.add(BI_Pdftable2);

            PdfPTable BI_Pdftable3 = new PdfPTable(1);
            BI_Pdftable3.setWidthPercentage(100);
            // CR_table6.setWidths(columnWidths_3);
            PdfPCell BI_Pdftable3_cell1 = new PdfPCell(
                    new Paragraph(
                            "For SBI Life  Smart Humsafar (UIN: 111N103V03) the two rates of investment return we choose to use for this purpose are 4% and 8% per annum.",
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

            BI_Pdftable5_cell1.setPadding(5);

            BI_Pdftable5.addCell(BI_Pdftable5_cell1);
            document.add(BI_Pdftable5);

            PdfPTable BI_Pdftable4 = new PdfPTable(1);
            BI_Pdftable4.setWidthPercentage(100);
            // CR_table6.setWidths(columnWidths_4);
            PdfPCell BI_Pdftable4_cell1 = new PdfPCell(
                    new Paragraph(
                            "Statutory Warning-"
                                    + "Some benefits are guaranteed and some benefits are variable with returns based on the future performance of your Insurer carrying on life insurance business. If your policy offers guaranteed returns then these will be clearly marked 'guaranteed' in the illustration table on this page. If your policy offers variable returns then the illustrations on this page will show two different rates of assumed future investment returns. These assumed rates of return are not guaranteed and they are not the upper or lower limits of what you might get back, as the value of your policy is dependent on a number of factors including future investment performance.",
                            small_bold));

            BI_Pdftable4_cell1.setPadding(5);

            BI_Pdftable4.addCell(BI_Pdftable4_cell1);
            //document.add(BI_Pdftable4);
            document.add(para_img_logo_after_space_1);

            // inputTable here -1
            PdfPCell cell;

            PdfPTable personalDetail_table = new PdfPTable(4);
            personalDetail_table.setWidths(new float[]{5f, 5f, 5f, 5f});
            personalDetail_table.setWidthPercentage(100f);

            personalDetail_table.setHorizontalAlignment(Element.ALIGN_LEFT);

            // 1st row
            cell = new PdfPCell(new Phrase(
                    "Proposer & Life Assured Details", small_bold));
            cell.setColspan(4);
            cell.setPadding(5);
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            cell.setBorder(Rectangle.BOTTOM | Rectangle.LEFT | Rectangle.RIGHT
                    | Rectangle.TOP);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            personalDetail_table.addCell(cell);

            //0th row
            cell = new PdfPCell(new Phrase("Name of the Life Assured", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setPadding(5);
            personalDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase(name_of_life_assured, small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setPadding(5);
            personalDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase("Equivalent Age (Years) ", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setPadding(5);
            personalDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase(equivalentAge + "", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setPadding(5);
            personalDetail_table.addCell(cell);

            //1st Row
            cell = new PdfPCell(new Phrase("Age (Years) of the Life Assured", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setPadding(5);
            personalDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase(age_entry, small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setPadding(5);
            personalDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase("Staff ", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setPadding(5);
            personalDetail_table.addCell(cell);

            if (smarthumsafarbean.getIsStaffDiscOrNot())
                cell = new PdfPCell(new Phrase("Yes", small_normal));
            else
                cell = new PdfPCell(new Phrase("No", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setPadding(5);
            personalDetail_table.addCell(cell);

            //2nd row
            cell = new PdfPCell(new Phrase("Gender of the Life Assured", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setPadding(5);
            personalDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase(
                    smarthumsafarbean.getGenderLA() + "", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setPadding(5);
            personalDetail_table.addCell(cell);

            /*cell = new PdfPCell(new Phrase("State", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setPadding(5);
            //personalDetail_table.addCell(cell);

            if (cb_kerladisc.isChecked()) {
                cell = new PdfPCell(new Phrase("Kerala", small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setPadding(5);
                //personalDetail_table.addCell(cell);

            } else {
                cell = new PdfPCell(new Phrase("Non Kerala", small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setPadding(5);
                //personalDetail_table.addCell(cell);
            }*/

            cell = new PdfPCell(new Phrase("Name of the Spouse Life Assured ", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setPadding(5);
            personalDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase(spouse_Title + " " + spouse_First_Name + " " + spouse_Middle_Name
                    + " " + spouse_Last_Name, small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setPadding(5);
            personalDetail_table.addCell(cell);

            //3rd Row
            cell = new PdfPCell(new Phrase("Age (Years) of the Spouse Life Assured  ", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setPadding(5);
            personalDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase(smarthumsafarbean.getAgeSpouse() + "",
                    small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setPadding(5);
            personalDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase("Gender of the Spouse Life Assured  ", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setPadding(5);
            personalDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase(
                    smarthumsafarbean.getGenderSpouse() + "", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setPadding(5);
            personalDetail_table.addCell(cell);

            // Basic Cover
            document.add(personalDetail_table);

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


            PdfPTable personalDetail_table2 = new PdfPTable(4);
            personalDetail_table2.setWidths(new float[]{5f, 5f, 5f, 5f});
            personalDetail_table2.setWidthPercentage(100f);

            personalDetail_table2.setHorizontalAlignment(Element.ALIGN_LEFT);

            // 1st row
            cell = new PdfPCell(new Phrase("Policy Details",
                    small_bold));
            cell.setColspan(4);
            cell.setPadding(5);
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            cell.setBorder(Rectangle.BOTTOM | Rectangle.LEFT | Rectangle.RIGHT
                    | Rectangle.TOP);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            personalDetail_table2.addCell(cell);

            cell = new PdfPCell(new Phrase("Policy Option", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setPadding(5);
            personalDetail_table2.addCell(cell);

            cell = new PdfPCell(new Phrase(
                    "Not Applicable", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setPadding(5);
            personalDetail_table2.addCell(cell);

            cell = new PdfPCell(new Phrase("Amount of Installment Premium (Rs.)", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setPadding(5);
            personalDetail_table2.addCell(cell);

            cell = new PdfPCell(new Phrase(
                    currencyFormat.format(Double
                            .parseDouble(YearlyPrem)), small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setPadding(5);
            personalDetail_table2.addCell(cell);

            cell = new PdfPCell(new Phrase("Premium Payment Option", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setPadding(5);
            personalDetail_table2.addCell(cell);

            cell = new PdfPCell(new Phrase(
                    "Regular", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setPadding(5);
            personalDetail_table2.addCell(cell);

            cell = new PdfPCell(new Phrase("Bonus Type", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setPadding(5);
            personalDetail_table2.addCell(cell);

            cell = new PdfPCell(new Phrase(
                    "Simple Reversionary Bonus", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setPadding(5);
            personalDetail_table2.addCell(cell);

            cell = new PdfPCell(new Phrase("Policy Term (Years)", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setPadding(5);
            personalDetail_table2.addCell(cell);

            cell = new PdfPCell(new Phrase(
                    smarthumsafarbean.getPolicyTerm_Basic() + "", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setPadding(5);
            personalDetail_table2.addCell(cell);

            cell = new PdfPCell(new Phrase("Sum Assured (Rs.) ", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setPadding(5);
            personalDetail_table2.addCell(cell);

            cell = new PdfPCell(new Phrase(
                    currencyFormat.format(smarthumsafarbean.getsumAssured())
                            + "", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setPadding(5);
            personalDetail_table2.addCell(cell);

            cell = new PdfPCell(new Phrase("Premium Payment Term (Years)", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setPadding(5);
            personalDetail_table2.addCell(cell);

            cell = new PdfPCell(new Phrase(
                    smarthumsafarbean.getPolicyTerm_Basic() + "", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setPadding(5);
            personalDetail_table2.addCell(cell);


            cell = new PdfPCell(new Phrase("Sum Assured on First Death (at inception of the policy) (Rs.)", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setPadding(5);
            personalDetail_table2.addCell(cell);

            cell = new PdfPCell(new Phrase(
                    currencyFormat.format(Double.parseDouble(benefit_payable_on_first_death))
                            + "", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setPadding(5);
            personalDetail_table2.addCell(cell);

            cell = new PdfPCell(new Phrase("Mode / Frequency of Premium Payment ",
                    small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setPadding(5);
            personalDetail_table2.addCell(cell);

            cell = new PdfPCell(new Phrase(smarthumsafarbean.getPremFreqMode()
                    + "", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setPadding(5);
            personalDetail_table2.addCell(cell);

            cell = new PdfPCell(new Phrase("Sum Assured on Second Death (at inception of the policy) (Rs.)", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setPadding(5);
            personalDetail_table2.addCell(cell);

            cell = new PdfPCell(new Phrase(
                    currencyFormat.format(Double.parseDouble(benefit_payable_on_second_death))
                            + "", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setPadding(5);
            personalDetail_table2.addCell(cell);


            cell = new PdfPCell(new Phrase("",
                    small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setPadding(5);
            personalDetail_table2.addCell(cell);

            cell = new PdfPCell(new Phrase("", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setPadding(5);
            personalDetail_table2.addCell(cell);

            cell = new PdfPCell(new Phrase("Rate of Applicable Taxes",
                    small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setPadding(5);
            personalDetail_table2.addCell(cell);

            if (cb_kerladisc.isChecked()) {
                cell = new PdfPCell(new Phrase("4.75% in the 1st policy year and 2.375% from 2nd policy year onwards", small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setPadding(5);
                personalDetail_table2.addCell(cell);


            } else {
                cell = new PdfPCell(new Phrase(" 4.5% in the 1st policy year and 2.25% from 2nd policy year onwards ", small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setPadding(5);
                personalDetail_table2.addCell(cell);


            }

            document.add(personalDetail_table2);

            if (smarthumsafarbean.getIsApplicableForADBRider()) {
                document.add(para_img_logo_after_space_1);
                PdfPTable riderDetail_table = new PdfPTable(6);
                riderDetail_table.setWidths(new float[]{9f, 4f, 4f, 4f, 4f, 4f});
                riderDetail_table.setWidthPercentage(100f);
                riderDetail_table.setHorizontalAlignment(Element.ALIGN_LEFT);

                // 1st row
                cell = new PdfPCell(
                        new Phrase("Rider Details", small_bold));
                cell.setColspan(6);
                cell.setBorder(Rectangle.BOTTOM | Rectangle.LEFT
                        | Rectangle.RIGHT | Rectangle.TOP);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setPadding(6);
                riderDetail_table.addCell(cell);

                // 2 row
                cell = new PdfPCell(new Phrase("Rider Name", small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setPadding(5);
                riderDetail_table.addCell(cell);

                cell = new PdfPCell(new Phrase("Applicable For", small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setPadding(5);
                riderDetail_table.addCell(cell);

                cell = new PdfPCell(new Phrase("Rider Policy Term(Years)", small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setPadding(5);
                riderDetail_table.addCell(cell);

                cell = new PdfPCell(new Phrase("Rider Sum Assured(Rs.)", small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setPadding(5);
                riderDetail_table.addCell(cell);

                cell = new PdfPCell(new Phrase("Rider Premium Payment Term(Years)", small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setPadding(5);
                riderDetail_table.addCell(cell);

                cell = new PdfPCell(new Phrase(
                        "Rider Premium (Rs.)",
                        small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setPadding(5);
                riderDetail_table.addCell(cell);

                if (smarthumsafarbean.getApplicableFor().equals(
                        "Both Lives Assured")) {
                    cell = new PdfPCell(
                            new Phrase(
                                    "SBI Life - Accidental Death Benefit Rider (UIN:111B015V03)",
                                    small_normal));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER
                            | Element.ALIGN_MIDDLE);
                    cell.setPadding(5);
                    cell.setRowspan(2);
                    riderDetail_table.addCell(cell);
                } else {
                    cell = new PdfPCell(
                            new Phrase(
                                    "SBI Life - Accidental Death Benefit Rider (UIN:111B015V03)",
                                    small_normal));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell.setPadding(5);
                    riderDetail_table.addCell(cell);
                }
                // 3rd
                if (smarthumsafarbean.getApplicableFor().equals(
                        "Life to be Assured")
                        || smarthumsafarbean.getApplicableFor().equals(
                        "Both Lives Assured")) {
                    cell = new PdfPCell(new Phrase(name_of_life_assured,
                            small_normal));
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    cell.setPadding(5);
                    riderDetail_table.addCell(cell);

                    cell = new PdfPCell(new Phrase(
                            smarthumsafarbean.getADB_PolicyTermLifeAssured()
                                    + "", small_normal));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell.setPadding(5);
                    riderDetail_table.addCell(cell);

                    cell = new PdfPCell(new Phrase(
                            (currencyFormat.format(smarthumsafarbean
                                    .getADBsumAssuredLA())), small_normal));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell.setPadding(5);
                    riderDetail_table.addCell(cell);

                    cell = new PdfPCell(new Phrase(
                            smarthumsafarbean.getADB_PolicyTermLifeAssured()
                                    + "", small_normal));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell.setPadding(5);
                    riderDetail_table.addCell(cell);

                    cell = new PdfPCell(
                            new Phrase(
                                    decimalCurrencyFormat.format(Double
                                            .parseDouble(AdbRiderPremLifeAssured))
                                            + "", small_normal));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell.setPadding(5);
                    riderDetail_table.addCell(cell);
                }

                // 4rd
                if (smarthumsafarbean.getApplicableFor().equals(
                        "Spouse Life to be Assured")
                        || smarthumsafarbean.getApplicableFor().equals(
                        "Both Lives Assured")) {
                    cell = new PdfPCell(new Phrase(spouse_Title + " " + spouse_First_Name + " " + spouse_Middle_Name
                            + " " + spouse_Last_Name,
                            small_normal));
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    cell.setPadding(5);
                    riderDetail_table.addCell(cell);

                    cell = new PdfPCell(new Phrase(
                            smarthumsafarbean.getADB_PolicyTermSpouse() + "",
                            small_normal));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell.setPadding(5);
                    riderDetail_table.addCell(cell);

                    cell = new PdfPCell(new Phrase(
                            (currencyFormat.format(smarthumsafarbean
                                    .getADBsumAssuredSpouse())), small_normal));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell.setPadding(5);
                    riderDetail_table.addCell(cell);

                    cell = new PdfPCell(new Phrase(
                            smarthumsafarbean.getADB_PolicyTermSpouse() + "",
                            small_normal));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell.setPadding(5);
                    riderDetail_table.addCell(cell);


                    cell = new PdfPCell(new Phrase(
                            decimalCurrencyFormat.format(Double
                                    .parseDouble(AdbRiderPremSpouse)) + "",
                            small_normal));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell.setPadding(5);
                    riderDetail_table.addCell(cell);
                }

                document.add(riderDetail_table);
            }
            document.add(para_img_logo_after_space_1);

            // Table here

            // PdfPTable FY_SY_premDetail_table = new PdfPTable(7);
            // FY_SY_premDetail_table.setWidths(new float[] { 5f, 5f, 5f, 5f,
            // 5f,
            // 5f, 5f });
            PdfPTable FY_SY_premDetail_table = new PdfPTable(4);
            FY_SY_premDetail_table.setWidths(new float[]{5f, 5f, 5f, 5f});
            FY_SY_premDetail_table.setWidthPercentage(100f);
            FY_SY_premDetail_table.setHorizontalAlignment(Element.ALIGN_LEFT);

            // 1st row
            cell = new PdfPCell(new Phrase("Premium Summary",
                    small_bold));
            cell.setColspan(7);
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            FY_SY_premDetail_table.addCell(cell);

            // 2 row
            cell = new PdfPCell(new Phrase("", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            FY_SY_premDetail_table.addCell(cell);

            /*cell = new PdfPCell(new Phrase(smarthumsafarbean.getPremFreqMode()
                    + " Premium (Rs )", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            FY_SY_premDetail_table.addCell(cell);*/
            cell = new PdfPCell(new Phrase("Base Plan (Rs.)", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            FY_SY_premDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase("Riders (Rs.)", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            FY_SY_premDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase(" Total Installment Premium (Rs.)", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            FY_SY_premDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase("Installment Premium without Applicable Taxes (Rs.)", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            FY_SY_premDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase(getformatedThousandString(Integer.parseInt(obj.getRound(obj
                    .getStringWithout_E(Double.valueOf(YearlyPrem
                            .equals("") ? "0" : YearlyPrem))))),
                    small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            FY_SY_premDetail_table.addCell(cell);

            cell = new PdfPCell(
                    new Phrase(
                            obj.getRound(obj.getStringWithout_E(Double
                                    .valueOf(InstBasicRider.equals("") ? "0"
                                            : InstBasicRider))), small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            FY_SY_premDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase(getformatedThousandString(Integer.parseInt(obj.getRound(obj
                    .getStringWithout_E(Double.valueOf(installmntPrem
                            .equals("") ? "0" : installmntPrem))))),
                    small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            FY_SY_premDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase("Installment Premium with First Year Applicable Taxes (Rs.)", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            FY_SY_premDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase(getformatedThousandString(Integer.parseInt(obj.getRound(obj
                    .getStringWithout_E(Double.valueOf(installmntPremWithSerTx
                            .equals("") ? "0" : installmntPremWithSerTx))))), small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            FY_SY_premDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase(obj.getRound(obj
                    .getStringWithout_E(Double.valueOf(premiumInstWithSTFirstYearRider
                            .equals("") ? "0" : premiumInstWithSTFirstYearRider))), small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            FY_SY_premDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase(getformatedThousandString(Integer.parseInt(obj.getRound(obj
                    .getStringWithout_E(Double.valueOf(totalInstPremFirstYear
                            .equals("") ? "0" : totalInstPremFirstYear))))), small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            FY_SY_premDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase("Installment Premium with Applicable Taxes 2nd Onwards (Rs.)", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            FY_SY_premDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase(getformatedThousandString(Integer.parseInt(obj.getRound(obj
                    .getStringWithout_E(Double.valueOf(BasicInstPremSecondYear
                            .equals("") ? "0" : BasicInstPremSecondYear))))), small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            FY_SY_premDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase(obj.getRound(obj
                    .getStringWithout_E(Double.valueOf(premiumInstWithSTSecondYearRiderSY
                            .equals("") ? "0" : premiumInstWithSTSecondYearRiderSY))), small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            FY_SY_premDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase(getformatedThousandString(Integer.parseInt(obj.getRound(obj
                    .getStringWithout_E(Double.valueOf(totalInstPremSecondYearSY
                            .equals("") ? "0" : totalInstPremSecondYearSY))))), small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            FY_SY_premDetail_table.addCell(cell);


            document.add(FY_SY_premDetail_table);
            document.add(para_img_logo_after_space_1);

            PdfPTable BI_Pdftable_note1 = new PdfPTable(1);
            BI_Pdftable_note1.setWidthPercentage(100);
            PdfPCell BI_Pdftable_note_cell11 = new PdfPCell(new Paragraph(
                    "Please Note", small_bold));
            BI_Pdftable_note_cell11.setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_Pdftable_note_cell11.setPadding(5);

            BI_Pdftable_note1.addCell(BI_Pdftable_note_cell11);
            document.add(BI_Pdftable_note1);

            PdfPTable BI_Pdftable61 = new PdfPTable(1);
            BI_Pdftable61.setWidthPercentage(100);
            PdfPCell BI_Pdftable6_cell61 = new PdfPCell(new Paragraph(
                    "1. The premiums can also be paid by giving standing instruction to your bank or you can pay through your credit card."

                    , small_normal));

            BI_Pdftable6_cell61.setPadding(5);

            BI_Pdftable61.addCell(BI_Pdftable6_cell61);
            document.add(BI_Pdftable61);

            PdfPTable BI_Pdftable71 = new PdfPTable(1);
            BI_Pdftable71.setWidthPercentage(100);
            PdfPCell BI_Pdftable7_cell1 = new PdfPCell(
                    new Paragraph(
                            "2. Applicable Taxes (including surcharge/cess etc), at the rate notified by the Central Government/ State Government / Union Territories of India from time to time and as per the provisions of the prevalent tax laws will be payable on premium as per the product features. ",
                            small_normal));

            BI_Pdftable7_cell1.setPadding(5);

            BI_Pdftable71.addCell(BI_Pdftable7_cell1);
            document.add(BI_Pdftable71);

            // ///////////////

            // 5 row
            if (proposer_Backdating_WishToBackDate_Policy.equalsIgnoreCase("y")) {

                PdfPTable basicCover_table = new PdfPTable(2);
                basicCover_table.setWidths(new float[]{5f, 5f});
                basicCover_table.setWidthPercentage(100f);
                basicCover_table.setHorizontalAlignment(Element.ALIGN_CENTER);

                // 1 row
                cell = new PdfPCell(new Phrase("Backdating Interest",
                        small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                basicCover_table.addCell(cell);

                cell = new PdfPCell(new Phrase(obj.getRound(obj
                        .getStringWithout_E(Double.valueOf(BackdatingInt
                                .equals("") ? "0" : BackdatingInt)))
                        + "", small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                basicCover_table.addCell(cell);
                document.add(basicCover_table);

                document.add(para_img_logo_after_space_1);
            }

            // Table here


            PdfPTable table1 = new PdfPTable(20);
            table1.setWidths(new float[]{2f, 5f, 5f, 5f, 5f, 5f, 5f, 5f, 5f,
                    5f, 5f, 5f, 5f, 5f, 5f, 5f, 5f, 5f, 5f, 5f});
            table1.setWidthPercentage(100);

            // 1st row
            cell = new PdfPCell(
                    new Phrase(
                            "Benefit Illustration for SBI Life- Smart Humsafar (Amount in Rs.)",
                            normal1_bold));
            cell.setColspan(23);
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table1.addCell(cell);

            // 2nd row
            cell = new PdfPCell(new Phrase("Policy Year", normal_bold));
            cell.setRowspan(5);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);

            cell = new PdfPCell(new Phrase(
                    "Annualized Premium", normal_bold));
            cell.setRowspan(5);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);

            cell = new PdfPCell(new Phrase("Guaranteed Benefits",
                    normal_bold));
            cell.setColspan(6);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);
            cell = new PdfPCell(
                    new Phrase("Non-Guaranteed Benefits @ 4% p.a.", normal_bold));
            cell.setColspan(3);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);

            cell = new PdfPCell(
                    new Phrase("Non-Guaranteed Benefits @ 8% p.a.", normal_bold));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setColspan(3);
            table1.addCell(cell);

            // 3rd
            cell = new PdfPCell(new Phrase("Total benefits (Including Guaranteed and Non-Guaranteed benefits)", normal_bold));
            cell.setColspan(6);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);

            cell = new PdfPCell(new Phrase(
                    "Guaranteed additions",
                    normal_bold));
            cell.setRowspan(4);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);

            cell = new PdfPCell(new Phrase("Survival benefit", normal_bold));
            cell.setRowspan(4);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);

            cell = new PdfPCell(new Phrase(
                    "Surrender Benefit",
                    normal_bold));
            cell.setRowspan(4);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);

            cell = new PdfPCell(new Phrase("Death Benefit on First Death", normal_bold));
            cell.setRowspan(4);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);

            cell = new PdfPCell(new Phrase("Death Benefit on Second Death", normal_bold));
            cell.setRowspan(4);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);

            cell = new PdfPCell(new Phrase("Maturity Benefit", normal_bold));
            cell.setRowspan(4);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);

            // 4 row
            cell = new PdfPCell(new Phrase("Reversionary bonus", normal_bold));
            cell.setRowspan(4);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);

            cell = new PdfPCell(new Phrase("Cash bonus", normal_bold));
            cell.setRowspan(4);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);


            cell = new PdfPCell(new Phrase("Surrender benefit", normal_bold));
            cell.setRowspan(4);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);

            cell = new PdfPCell(new Phrase("Reversionary bonus", normal_bold));
            cell.setRowspan(4);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);

            cell = new PdfPCell(new Phrase("Cash bonus", normal_bold));
            cell.setRowspan(4);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);


            cell = new PdfPCell(new Phrase("Surrender benefit", normal_bold));
            cell.setRowspan(4);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);


            cell = new PdfPCell(new Phrase("Maturity Benefit", normal_bold));
            cell.setRowspan(2);
            cell.setColspan(2);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);
            cell = new PdfPCell(new Phrase("Death Benefit on First Death", normal_bold));
            cell.setRowspan(2);
            cell.setColspan(2);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);

            cell = new PdfPCell(new Phrase("Death Benefit on Second Death", normal_bold));
            cell.setRowspan(2);
            cell.setColspan(2);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);


            cell = new PdfPCell(new Phrase("Total Maturity benefit, incl.Terminal bonus, if any, @ 4%(8+9+10)", normal_bold));
            cell.setRowspan(2);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);
            cell = new PdfPCell(new Phrase("Total Maturity benefit, incl.Terminal bonus, if any, @ 8% (8+12+13)", normal_bold));
            cell.setRowspan(2);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);

            cell = new PdfPCell(new Phrase("Total Death benefit, incl.Terminal bonus, if any, @ 4% (6)", normal_bold));
            cell.setRowspan(2);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);
            cell = new PdfPCell(new Phrase("Total Death benefit, incl.Terminal bonus, if any, @ 8%(6)", normal_bold));
            cell.setRowspan(2);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);
            cell = new PdfPCell(new Phrase("Total Death benefit, incl.Terminal bonus, if any, @ 4% (7+9+10)", normal_bold));
            cell.setRowspan(2);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);

            cell = new PdfPCell(new Phrase("Total Death benefit, incl.Terminal bonus, if any, @ 8%(7+12+13)", normal_bold));
            cell.setRowspan(2);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);

            cell = new PdfPCell(new Phrase("1", normal_bold));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);
            cell = new PdfPCell(new Phrase("2", normal_bold));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);
            cell = new PdfPCell(new Phrase("3", normal_bold));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);
            cell = new PdfPCell(new Phrase("4", normal_bold));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);
            cell = new PdfPCell(new Phrase("5", normal_bold));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);
            cell = new PdfPCell(new Phrase("6", normal_bold));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);
            cell = new PdfPCell(new Phrase("7", normal_bold));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);
            cell = new PdfPCell(new Phrase("8", normal_bold));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);
            cell = new PdfPCell(new Phrase("9", normal_bold));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);
            cell = new PdfPCell(new Phrase("10", normal_bold));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);
            cell = new PdfPCell(new Phrase("11", normal_bold));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);
            cell = new PdfPCell(new Phrase("12", normal_bold));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);
            cell = new PdfPCell(new Phrase("13", normal_bold));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);
            cell = new PdfPCell(new Phrase("14", normal_bold));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);
            cell = new PdfPCell(new Phrase("15", normal_bold));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);
            cell = new PdfPCell(new Phrase("16", normal_bold));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);
            cell = new PdfPCell(new Phrase("17", normal_bold));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);
            cell = new PdfPCell(new Phrase("18", normal_bold));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);
            cell = new PdfPCell(new Phrase("19", normal_bold));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell);
            cell = new PdfPCell(new Phrase("20", normal_bold));

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
                                i).getTotal_base_premium())), normal));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                        | Rectangle.BOTTOM);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table1.addCell(cell);


                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                        .parseDouble(list_data.get(i)
                                .getGuaranteed_additions())), normal));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                        | Rectangle.BOTTOM);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table1.addCell(cell);


                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                        .parseDouble(list_data.get(i)
                                .getGuaranteed_survival_benefit())), normal));
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
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table1.addCell(cell);


                cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                        .parseDouble(list_data.get(i)
                                .getGuaranteed_death_benefit())), normal));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                        | Rectangle.BOTTOM);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table1.addCell(cell);

                cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                        .parseDouble(list_data.get(i)
                                .getGuaranteed_death_benefit2())), normal));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                        | Rectangle.BOTTOM);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table1.addCell(cell);

                cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                        .parseDouble(list_data.get(i)
                                .getGuaranteed_maturity_benefit())), normal));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                        | Rectangle.BOTTOM);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table1.addCell(cell);


                cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                        .parseDouble(list_data.get(i)
                                .getNot_guaranteed_maturity_benefit_4per())),
                        normal));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                        | Rectangle.BOTTOM);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table1.addCell(cell);

                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell = new PdfPCell(new Phrase("0", normal));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                        | Rectangle.BOTTOM);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table1.addCell(cell);


                cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                        .parseDouble(list_data.get(i)
                                .getNot_guaranteed_surrender_value_4per())),
                        normal));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                        | Rectangle.BOTTOM);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table1.addCell(cell);

                cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                        .parseDouble(list_data.get(i)
                                .getNot_guaranteed_maturity_benefit_8per())),
                        normal));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                        | Rectangle.BOTTOM);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table1.addCell(cell);


                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell = new PdfPCell(new Phrase("0", normal));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                        | Rectangle.BOTTOM);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table1.addCell(cell);


                cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                        .parseDouble(list_data.get(i)
                                .getNot_guaranteed_surrender_value_8per())),
                        normal));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                        | Rectangle.BOTTOM);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table1.addCell(cell);

                cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                        .parseDouble(list_data.get(i)
                                .getTotal_survival_benefit_4per())),
                        normal));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                        | Rectangle.BOTTOM);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table1.addCell(cell);

                cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                        .parseDouble(list_data.get(i)
                                .getTotal_survival_benefit_8per())),
                        normal));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                        | Rectangle.BOTTOM);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table1.addCell(cell);


                cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                        .parseDouble(list_data.get(i)
                                .getNot_guaranteed_survival_benefit_4per())),
                        normal));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                        | Rectangle.BOTTOM);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table1.addCell(cell);

                cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                        .parseDouble(list_data.get(i)
                                .getNot_guaranteed_survival_benefit_8per())),
                        normal));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                        | Rectangle.BOTTOM);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table1.addCell(cell);

                cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                        .parseDouble(list_data.get(i)
                                .getNot_guaranteed_death_benefit_4per())),
                        normal));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                        | Rectangle.BOTTOM);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table1.addCell(cell);

                cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                        .parseDouble(list_data.get(i)
                                .getNot_guaranteed_death_benefit_8per())),
                        normal));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                        | Rectangle.BOTTOM);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table1.addCell(cell);
            }

            document.add(table1);
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
                            "1. Annualized premium shall be the premium amount payable in a year chosen by the policyholder, excluding  taxes, rider premiums,underwriting extra premiums and loadings for modal premiums, if any. Refer sales literature for explanation of terms used in this illustration.",
                            small_normal));
            BI_Pdftable6_cell6.setPadding(5);
            BI_Pdftable6.addCell(BI_Pdftable6_cell6);
            document.add(BI_Pdftable6);

            PdfPTable BI_Pdftable8 = new PdfPTable(1);
            BI_Pdftable8.setWidthPercentage(100);
            PdfPCell BI_Pdftable8_cell1 = new PdfPCell(
                    new Paragraph(
                            "2. All Benefit amount are derived on the assumption that the policies are ''in-force''",
                            small_normal));
            BI_Pdftable8_cell1.setPadding(5);
            BI_Pdftable8.addCell(BI_Pdftable8_cell1);
            document.add(BI_Pdftable8);

            PdfPTable taxes_desc = new PdfPTable(1);
            taxes_desc.setWidthPercentage(100);
            PdfPCell taxes_desc_cell = new PdfPCell(
                    new Paragraph(
                            "3. The above BI is subject to payment of stipulated premiums on due date.",

                            small_normal));

            PdfPCell taxes_desc_cell2 = new PdfPCell(
                    new Paragraph(
                            "4. In case First Death claim is paid, future premiums would be waived off for the rest of the premium payment term.",

                            small_normal));

            PdfPCell taxes_desc_cell3 = new PdfPCell(
                    new Paragraph(
                            "5. In case of simultaneous death, the death benefit of both lives, as applicable, would be paid.",

                            small_normal));
            PdfPCell taxes_desc_cell4 = new PdfPCell(
                    new Paragraph(
                            "6. In addition to Guaranteed Surrender Benefits (column 5), Surrender value of the vested bonuses will also be paid. For the purpose of guaranteed surrender value (GSV) in this illustration the surrender value of vested bonuses are not considered at all.",

                            small_normal));

            PdfPCell taxes_desc_cell5 = new PdfPCell(
                    new Paragraph(
                            "7.If rider is applicable, please refer to specific rider benefit. For more details, refer to the rider document.",

                            small_normal));

            taxes_desc_cell.setPadding(5);

            taxes_desc.addCell(taxes_desc_cell);
            taxes_desc.addCell(taxes_desc_cell2);

            taxes_desc.addCell(taxes_desc_cell3);
            taxes_desc.addCell(taxes_desc_cell4);
            taxes_desc.addCell(taxes_desc_cell5);

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
                            "1.  The benefit calculation is based on the age herein indicated of both lives assured and as applicable for healthy individual."

                            , small_normal));

            BI_PdftableOtherTermCondition1_cell6.setPadding(5);

            BI_PdftableOtherTermCondition1
                    .addCell(BI_PdftableOtherTermCondition1_cell6);
            // document.add(BI_PdftableOtherTermCondition1);

            PdfPTable BI_PdftableOtherTermCondition2 = new PdfPTable(1);
            BI_PdftableOtherTermCondition2.setWidthPercentage(100);
            PdfPCell BI_PdftableOtherTermCondition2_cell1 = new PdfPCell(
                    new Paragraph(
                            "2.  The Maturity/ Death Benefit amount are derived on the assumption that the policies are in 'full force'.",
                            small_normal));

            BI_PdftableOtherTermCondition2_cell1.setPadding(5);

            BI_PdftableOtherTermCondition2
                    .addCell(BI_PdftableOtherTermCondition2_cell1);
            // document.add(BI_PdftableOtherTermCondition2);

            PdfPTable BI_PdftableOtherTermCondition3 = new PdfPTable(1);
            BI_PdftableOtherTermCondition3.setWidthPercentage(100);
            PdfPCell BI_PdftableOtherTermCondition3_cell1 = new PdfPCell(
                    new Paragraph(
                            "3. The Minimum death benefit on first death and / or second death is 105% of all the premiums paid. ",
                            small_normal));

            BI_PdftableOtherTermCondition3_cell1.setPadding(5);

            BI_PdftableOtherTermCondition3
                    .addCell(BI_PdftableOtherTermCondition3_cell1);
            // document.add(BI_PdftableOtherTermCondition3);

            PdfPTable BI_PdftableOtherTermCondition4 = new PdfPTable(1);
            BI_PdftableOtherTermCondition4.setWidthPercentage(100);
            PdfPCell BI_PdftableOtherTermCondition4_cell1 = new PdfPCell(
                    new Paragraph(
                            "4.  In case of simultaneous death, the death benefit of both lives, as applicable, would be paid.",
                            small_normal));

            BI_PdftableOtherTermCondition4_cell1.setPadding(5);

            BI_PdftableOtherTermCondition4
                    .addCell(BI_PdftableOtherTermCondition4_cell1);
            //document.add(BI_PdftableOtherTermCondition4);

            PdfPTable BI_PdftableOtherTermCondition5 = new PdfPTable(1);
            BI_PdftableOtherTermCondition5.setWidthPercentage(100);
            PdfPCell BI_PdftableOtherTermCondition5_cell1 = new PdfPCell(
                    new Paragraph(
                            "5.  Insurance is subject matter of solicitation. ",
                            small_normal));

            BI_PdftableOtherTermCondition5_cell1.setPadding(5);

            BI_PdftableOtherTermCondition5
                    .addCell(BI_PdftableOtherTermCondition5_cell1);
            // document.add(BI_PdftableOtherTermCondition5);

            PdfPTable BI_PdftableOtherTermCondition6 = new PdfPTable(1);
            BI_PdftableOtherTermCondition6.setWidthPercentage(100);
            PdfPCell BI_PdftableOtherTermCondition6_cell1 = new PdfPCell(
                    new Paragraph(
                            "6.  The surrender values shown above are applicable for in-force policies and values are shown at the end of policy year.",
                            small_normal));

            BI_PdftableOtherTermCondition6_cell1.setPadding(5);

            BI_PdftableOtherTermCondition6
                    .addCell(BI_PdftableOtherTermCondition6_cell1);
            //document.add(BI_PdftableOtherTermCondition6);

            document.add(para_img_logo_after_space_1);

            // Paragraph note = new
            // Paragraph("Please Note: ",normal1_BoldUnderline);
            // note.setAlignment(Element.ALIGN_LEFT);
            // document.add(note);
            //
            // Paragraph note_1 = new
            // Paragraph("1. In case of monthly mode 3 months premiums have to be paid in advance. ",small_normal);
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
            // // document.add(new_line);
            // Paragraph termsCondition = new
            // Paragraph("Other Terms and Conditions",small_bold);
            // termsCondition.setAlignment(Element.ALIGN_LEFT);
            // document.add(termsCondition);
            // Paragraph terms_1 = new
            // Paragraph("1.  The benefit calculation is based on the age herein indicated of both lives assured and as applicable for healthy individual.",small_normal);
            // terms_1.setAlignment(Element.ALIGN_LEFT|Element.ALIGN_JUSTIFIED);
            // document.add(terms_1);
            // Paragraph terms_2 = new
            // Paragraph("2.  The Maturity/ Death Benefit amount are derived on the assumption that the policies are in 'full force.",small_normal);
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
                            "This is a with profit plan and participates in the profits of the company’s life insurance business. Simple reversionary bonuses are declared as a percentage rate, which apply to the sum assured of the basic policy. Terminal bonuses, if any, are declared as a percentage rate, which apply to the vested reversionary bonus."

                            , small_normal));

            BI_Pdftable_BonusRates_cell2.setPadding(5);

            BI_Pdftable_BonusRates1.addCell(BI_Pdftable_BonusRates_cell2);
            document.add(BI_Pdftable_BonusRates1);

            PdfPTable BI_Pdftable_BonusRates2 = new PdfPTable(1);
            BI_Pdftable_BonusRates2.setWidthPercentage(100);
            PdfPCell BI_Pdftable_BonusRates2_cell2 = new PdfPCell(
                    new Paragraph(
                            "Simple reversionary bonuses are declared as a percentage rate, which apply to the basic sum assured of the basic policy. Once declared, they form a part of the guaranteed benefits of the plan. Terminal bonuses, if any, are declared as a percentage rate, which apply to the vested reversionary bonus."

                            , small_normal));

            BI_Pdftable_BonusRates2_cell2.setPadding(5);

            BI_Pdftable_BonusRates2.addCell(BI_Pdftable_BonusRates2_cell2);
            //document.add(BI_Pdftable_BonusRates2);

            PdfPTable BI_Pdftable_BonusRates3 = new PdfPTable(1);
            BI_Pdftable_BonusRates3.setWidthPercentage(100);
            PdfPCell BI_Pdftable_BonusRates3_cell2 = new PdfPCell(
                    new Paragraph(
                            "The bonus rates in the benefit illustration are constant. However, in practice, future bonuses are not guaranteed and will depend on future profits. Therefore, the bonuses are shown as non-guaranteed benefits and are calculated so that they are consistent with the two projected investment return assumptions of 4% and 8% per annum."

                            , small_normal));

            BI_Pdftable_BonusRates3_cell2.setPadding(5);

            BI_Pdftable_BonusRates3.addCell(BI_Pdftable_BonusRates3_cell2);
            document.add(BI_Pdftable_BonusRates3);

            PdfPTable BI_Pdftable_BonusRates4 = new PdfPTable(1);
            BI_Pdftable_BonusRates4.setWidthPercentage(100);
            PdfPCell BI_Pdftable_BonusRates4_cell2 = new PdfPCell(
                    new Paragraph(
                            "Accordingly, for the purpose of guaranteed surrender value (GSV) in this illustration, the surrender value of vested bonuses is not considered at all."

                            , small_normal));

            BI_Pdftable_BonusRates4_cell2.setPadding(5);

            BI_Pdftable_BonusRates4.addCell(BI_Pdftable_BonusRates4_cell2);
            // document.add(BI_Pdftable_BonusRates4);

            document.add(para_img_logo_after_space_1);

            PdfPTable BI_Pdftable_SurrenderValue = new PdfPTable(1);
            BI_Pdftable_SurrenderValue.setWidthPercentage(100);
            PdfPCell BI_Pdftable_SurrenderValue_cell1 = new PdfPCell(
                    new Paragraph("Surrender Value", small_bold));
            BI_Pdftable_SurrenderValue_cell1
                    .setHorizontalAlignment(Element.ALIGN_LEFT);
            BI_Pdftable_SurrenderValue_cell1.setPadding(5);

            BI_Pdftable_SurrenderValue
                    .addCell(BI_Pdftable_SurrenderValue_cell1);
            // document.add(BI_Pdftable_SurrenderValue);

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
            // document.add(BI_Pdftable_GuaranteedSurrenderValue);

            PdfPTable BI_Pdftable_RegularPremiumPolicies = new PdfPTable(1);
            BI_Pdftable_RegularPremiumPolicies.setWidthPercentage(100);
            PdfPCell BI_Pdftable_RegularPremiumPolicies_cell = new PdfPCell(
                    new Paragraph(
                            "The policy will acquire a paid-up and/or surrender value only if premiums have been paid for at least 3 full years.",
                            small_normal));

            BI_Pdftable_RegularPremiumPolicies_cell.setPadding(5);

            BI_Pdftable_RegularPremiumPolicies
                    .addCell(BI_Pdftable_RegularPremiumPolicies_cell);
            // document.add(BI_Pdftable_RegularPremiumPolicies);

            PdfPTable BI_Pdftable_SinglePremiumPolicies = new PdfPTable(1);
            BI_Pdftable_SinglePremiumPolicies.setWidthPercentage(100);
            PdfPCell BI_Pdftable_SinglePremiumPolicies_cell = new PdfPCell(
                    new Paragraph(
                            "The Guaranteed Surrender Value (GSV) will be equal to GSV factors multiplied by the basic premiums paid. Basic premium is equal to total premium less Applicable Tax, underwriting extra premiums, if any.",
                            small_normal));

            BI_Pdftable_SinglePremiumPolicies_cell.setPadding(5);

            BI_Pdftable_SinglePremiumPolicies
                    .addCell(BI_Pdftable_SinglePremiumPolicies_cell);
            // document.add(BI_Pdftable_SinglePremiumPolicies);

            // document.add(para_img_logo_after_space_1);

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
                            "In practice, the company may pay a surrender value which could be higher than the guaranteed surrender value. The benefits payable on surrender reflects the value of your policy, which is assessed based on the past financial/demographic experience of the company with regard to your policy/group of similar policies, as well as the likely future experience. The surrender value payable may be reviewed from time to time depending on company's experience of the various factors which impact the surrender values that may be paid. The surrender value would be higher of GSV or SSV.",
                            small_normal));

            BI_Pdftable_CompanysPolicySurrender1_cell.setPadding(5);

            //  BI_Pdftable_CompanysPolicySurrender1
            //         .addCell(BI_Pdftable_CompanysPolicySurrender1_cell);

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

            PdfPCell BI_Pdftable_CompanysPolicySurrender4_cell = new PdfPCell(
                    new Paragraph(
                            "Your SBI LIFE - Smart Humsafar(UIN: 111N103V03) is a Regular/Limited premium policy,for which your first year "
                                    + premium_paying_frequency
                                    + " premium is Rs. "
                                    + yearlypremium_with_servicetax
                                    + " Your policy Term is "
                                    + policy_term
                                    + " years,Premium Payment Term is "
                                    + policy_term
                                    + " years and Basic Sum Assured is Rs. "
                                    + getformatedThousandString(Integer.parseInt(obj.getRound(obj.getStringWithout_E(Double
                                    .valueOf((sum_assured.equals("") || sum_assured == null) ? "0"
                                            : sum_assured))))),
                            small_normal));

            BI_Pdftable_CompanysPolicySurrender4_cell.setPadding(5);

            //  BI_Pdftable_CompanysPolicySurrender1
            //         .addCell(BI_Pdftable_CompanysPolicySurrender4_cell);

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
                                    + name_of_person
                                    + " , having received the information with respect to the above, have understood the above statement before entering into the contract.",
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
                                        + " , have explained the premiums and benefits under the product fully to the prospect/policyholder.",
                                small_bold));

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
            // TODO: handle exception
            Log.i("Error", e.toString() + "Error in creating pdf");

        }
    }

    private boolean showsmartHumsafarOutputPg(SmartHumsafarBean smartHumsafarBean) {

        String[] outputArr = getOutput("BI_of_Smart_Humsafar",
                smartHumsafarBean);

        boolean validate_premAmount = valPremAmount(Double.parseDouble(outputArr[0]));
        if (validate_premAmount) {
            try {

                retVal.append("<?xml version='1.0' encoding='utf-8' ?><SmartHumsafar>");
                retVal.append("<errCode>0</errCode>");

                retVal.append("<basicPrem>" + outputArr[0] + "</basicPrem>");
                retVal.append("<YearlyPrem>" + commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(Double.parseDouble(outputArr[0]))) + "</YearlyPrem>");
//InstBasicRider
                retVal.append("<InstBasicRider>" + outputArr[29] + "</InstBasicRider>");
                retVal.append("<totalInstPremFirstYear>" + outputArr[32] + "</totalInstPremFirstYear>");
                retVal.append("<equivalentAge>" + outputArr[11]
                        + "</equivalentAge>" + "<installmntPrem>"
                        + outputArr[1] + "</installmntPrem>"
                        + "<basicPremiumWtRider>" + outputArr[1]
                        + "</basicPremiumWtRider>" + "<serviceTax>"
                        + outputArr[3] + "</serviceTax>"
                        + "<basicPremiumWtRiderWthST>" + outputArr[2]
                        + "</basicPremiumWtRiderWthST>");


                /******** Added by Vrushali on 12-Nov-2015 *************/
                retVal.append("<staffStatus>").append(outputArr[16]).append("</staffStatus>");
                retVal.append("<staffRebate>").append(outputArr[17]).append("</staffRebate>");
                retVal.append("<InstmntPrem>").append(outputArr[15]).append("</InstmntPrem>");
                retVal.append("<basicPremWithoutDisc>").append(outputArr[12]).append("</basicPremWithoutDisc>");
                retVal.append("<basicPremWithoutDiscSA>").append(outputArr[18]).append("</basicPremWithoutDiscSA>");
                retVal.append("<premADBRLAWithoutDisc>").append(outputArr[13]).append("</premADBRLAWithoutDisc>");
                retVal.append("<premADBRLAWithoutDiscSA>").append(outputArr[19]).append("</premADBRLAWithoutDiscSA>");
                retVal.append("<premADBRSpouseWithoutDisc>").append(outputArr[14]).append("</premADBRSpouseWithoutDisc>");
                retVal.append("<premADBRSpouseWithoutDiscSA>").append(outputArr[20]).append("</premADBRSpouseWithoutDiscSA>");
                retVal.append("<OccuInt>").append(outputArr[21]).append("</OccuInt>");
                retVal.append("<backdateInt>").append(outputArr[22]).append("</backdateInt>");
                /******** Added by Vrushali on 12-Nov-2015 *************/
                /* modified by Akshaya on 20-MAY-16 start **/

                retVal.append("<basicServiceTax>").append(outputArr[23]).append("</basicServiceTax>")
                        .append("<SBCServiceTax>").append(outputArr[24]).append("</SBCServiceTax>")
                        .append("<KKCServiceTax>").append(outputArr[25]).append("</KKCServiceTax>");
                retVal.append("<KeralaCessServiceTax>" + outputArr[27] + "</KeralaCessServiceTax>");

                //PremiumWithSTSecondYear
                retVal.append("<BasicInstPremSecondYear>" + outputArr[34] + "</BasicInstPremSecondYear>");
                retVal.append("<premiumInstWithSTFirstYearRider>" + outputArr[30] + "</premiumInstWithSTFirstYearRider>");
                retVal.append("<installmntPremWithSerTx>" + outputArr[2] + "</installmntPremWithSerTx>");
                retVal.append("<premiumInstWithSTSecondYearRiderSY>" + outputArr[31] + "</premiumInstWithSTSecondYearRiderSY>");
                retVal.append("<totalInstPremSecondYearSY>" + outputArr[33] + "</totalInstPremSecondYearSY>");

                /* modified by Akshaya on 20-MAY-16 end **/
                int index = smartHumsafarBean.getPolicyTerm_Basic();
                String NonGuarateedSurvivalBenefitAt_4_Percent = prsObj.parseXmlTag(bussIll.toString(), "NonGuarateedSurvivalBenefitAt_4_Percent" + index + "");
                String NonGuarateedSurvivalBenefitAt_8_Percent = prsObj.parseXmlTag(bussIll.toString(), "NonGuarateedSurvivalBenefitAt_8_Percent" + index + "");

                retVal.append("<NonGuarateedSurvivalBenefitAt_4_Percent" + index + ">" + NonGuarateedSurvivalBenefitAt_4_Percent + "</NonGuarateedSurvivalBenefitAt_4_Percent" + index + ">");
                retVal.append("<NonGuarateedSurvivalBenefitAt_8_Percent" + index + ">" + NonGuarateedSurvivalBenefitAt_8_Percent + "</NonGuarateedSurvivalBenefitAt_8_Percent" + index + ">");

                if (cb_bi_smart_humsafar_adb_rider.isChecked()) {
                    retVal.append("<AdbRiderPremLifeAssured>").append(outputArr[4]).append("</AdbRiderPremLifeAssured>").append("<AdbRiderPremSpouse>").append(outputArr[5]).append("</AdbRiderPremSpouse>").append(bussIll.toString());
                } else {
                    retVal.append(bussIll.toString());
                }

                retVal.append("</SmartHumsafar>");

                System.out.println("output " + retVal.toString());

            } catch (Exception e) {
                retVal.append("<?xml version='1.0' encoding='utf-8' ?><SmartHumsafar>" + "<errCode>1</errCode>" + "<errorMessage>").append(e.getMessage()).append("</errorMessage></SmartHumsafar>");
            }

            return true;
        } else {
            return false;
        }

    }

    /********************************** Output ends here **********************************************************/

    private String[] getOutput(String sheetName,
                               SmartHumsafarBean smarthumsafarbean) {
        bussIll = new StringBuilder();

        String premiumWithoutStwithotRoundup = null,
                premiumWithoutStwtRoundup = null,
                premiumWithoutStwtRoundupofBasicprem = null,
                PremiumWITHOUTroundup = null,
                BasicPremiumRound = null,
                premiumADBwithoutRoundUp = null,
                premiumADBwithRoundUp = null,
                PremiumWithST = null,
                PremiumWithSTWITHOUTRound = null,
                PremiumWithSTRound = null,
                PremiumWithSTRoundSY = null,
                PremiumWithSTSecondYear = null,
                PremiumWithSTSecondYearWITHOUTRoundup = null,
                serviceTax = null,
                serviceTaxSecondYear = null,
                premiumAdbRiderLAWithoutRoundup = null,
                premiumAdbRiderLAWithRound = null,
                getPremiumAdbRiderSpouseWithRound = null,
                InstBasicRider = null,
                InstbasicriderWITHOUTRoundup = null,
                premiumAdbRiderSpouseWithoutRoundup = null,
                premiumAdbRiderLAWithRoundup = null,
                premiumAdbRiderSpouseWithRoundup = null,
                basePremiumWithRiderWithRoundUP = null,
                totalBasePremiumPaid = null,
                sumtotalfreqLoading = null,
                annulizedPremium = null,
                PremiumwithoutModelLoading = null,
                GuaranteedDeathBenefitSecondDeath = null,
                NonGuarateedDeathBenefitAt_4_Percent = null,
                NonGuarateedDeathBenefitAt_4_PercentFinal = null,
                NonGuarateedDeathBenefitAt_8_PercentFinal = null,
                NonGuarateedDeathBenefitAt_8_Percent = null,
                GuaranteedSurvivalBenefit = null,
                NonGuarateedSurvivalBenefitAt_4_Percent = null,
                NonGuarateedSurvivalBenefitAt_8_Percent = null,
                GuaranteedDeathBenefitFirstDeath = null,
                GSV_surrendr_val = null,
                NonGSV_surrndr_val_4Percent = null,
                NonGSV_surrndr_val_8Percent = null,
                TotalMaturityBenefit4percent = null,
                TotalMaturityBenefit8percent = null,
                TotalDeathBenefit4perecentFirst = null,
                TotalDeathBenefit8perecentFirst = null,
                TotalDeathBenefit4perecentSecond = null,
                TotalDeathBenefit8perecentSecond = null,

                // added by vrushali on 13-Nov-2015
                premiumWithoutStWithoutDiscwithotRoundup = null,
                premiumWithoutStWithoutDiscwtRoundup = null,
                premiumAdbRiderSpouseWithoutDiscWithoutRoundup = null,
                premiumAdbRiderLAWithoutDiscWithoutRoundup = null,
                basePremiumWithoutDiscWithRiderWithRoundUP = null,
                staffRebate = null,
                basicPremiumWithoutRoundup = null,
                basicPremiumWithRoundup = null,
                basicPremiumAdbRiderLAWithoutRoundup = null,
                basicPremiumAdbRiderSpouseWithoutRoundup = null,
                staffStatus = null;

        int rowNumber = 0, year_F = 0;
        double sumTotalBasePrem = 0, firstyearofdeath4per = 0;

        /******** Added by Vrushali on 12-Nov-15 start */
        if (smarthumsafarbean.getIsStaffDiscOrNot())
            staffStatus = "sbi";
        else
            staffStatus = "none";
        /******** Added by Vrushali on 12-Nov-15 end */

        SmartHumsafarBusinessLogic objSmartHumsafarBussLogic = new SmartHumsafarBusinessLogic(
                smarthumsafarbean);
        String equivalentAge = objSmartHumsafarBussLogic.getEquivalentAge(
                smarthumsafarbean.getAgeLA(), smarthumsafarbean.getAgeSpouse())
                + "";
        objSmartHumsafarBussLogic.setPremiumWithoutSTwithoutRoundup();
        premiumWithoutStwithotRoundup = objSmartHumsafarBussLogic
                .getPremiumWithoutSTwithoutRoundup();
//		System.out.println("** premiumWithoutStwithotRoundup  "+premiumWithoutStwithotRoundup);

        objSmartHumsafarBussLogic.setPremiumWithoutSTwithRoundup();
        premiumWithoutStwtRoundup = objSmartHumsafarBussLogic
                .getPremiumWithoutSTwithRoundup();
//		System.out.println("** premiumWithoutStwtRoundup"+premiumWithoutStwtRoundup);
        //added by sujata on 26-02-2020
        objSmartHumsafarBussLogic.setPremiumWithoutSTwithRoundupBasicPrem();
        premiumWithoutStwtRoundupofBasicprem = objSmartHumsafarBussLogic.getPremiumWithoutSTwithRoundupBasicPrem();

        //added by sujata on 29-02-2020
        objSmartHumsafarBussLogic.setPremiumWithoutSTWITHOUTroundup();
        PremiumWITHOUTroundup = objSmartHumsafarBussLogic.getPremiumWithoutSTWITHOUTroundup();

        //end
        //System.out.println("premiumWithoutStwtRoundupofBasicprem "+premiumWithoutStwtRoundupofBasicprem);
        objSmartHumsafarBussLogic.setPremiumRound();
        BasicPremiumRound = objSmartHumsafarBussLogic.getPremiumRound();
        //end


        // added by vrushali on 06 nov 2015
        objSmartHumsafarBussLogic
                .setPremiumWithoutSTWithoutDiscwithoutRoundup();
        premiumWithoutStWithoutDiscwithotRoundup = objSmartHumsafarBussLogic
                .getPremiumWithoutSTWithoutDiscwithoutRoundup();
//		System.out.println("** premiumWithoutStWithoutDiscwithotRoundup  "+ premiumWithoutStWithoutDiscwithotRoundup);

        objSmartHumsafarBussLogic.setPremiumWithoutSTWithoutDiscwithRoundup();
        premiumWithoutStWithoutDiscwtRoundup = objSmartHumsafarBussLogic
                .getPremiumWithoutSTWithoutDiscwithRoundup();
//		System.out.println("** premiumWithoutStWithoutDiscwtRoundup" + premiumWithoutStWithoutDiscwtRoundup);
        // End


        // added by sujata on 26-02-2020
        objSmartHumsafarBussLogic.setBasicPremiumWithoutRoundup();
        basicPremiumWithoutRoundup = objSmartHumsafarBussLogic
                .getBasicPremiumWithoutRoundup();
//		System.out.println("** basicPremiumWithoutRoundup  "+ basicPremiumWithoutRoundup);

        objSmartHumsafarBussLogic.setBasicPremiumwithRoundup();
        basicPremiumWithRoundup = objSmartHumsafarBussLogic
                .getBasicPremiumwithRoundup();
//		System.out.println("** basicPremiumWithRoundup  "+ basicPremiumWithRoundup);


//		objSmartHumsafarBussLogic.setPremiumRound();
//		basicPremiumWithRoundup = objSmartHumsafarBussLogic.getPremiumRound();

        // end

        objSmartHumsafarBussLogic.setPremiumAdbRiderLAWithoutRoundup();
        premiumAdbRiderLAWithoutRoundup = objSmartHumsafarBussLogic
                .getPremiumAdbRiderLAWithoutRoundup();
//		System.out.println("** premiumAdbRiderLAWithoutRoundup  "+premiumAdbRiderLAWithoutRoundup);

        //added by sujata on 22-02-2020
        premiumAdbRiderLAWithRound = objSmartHumsafarBussLogic.getPremiumAdbRiderLAWithRoundup();
        //	System.out.println(" premiumAdbRiderLAWithoutRoundup  "+premiumAdbRiderLAWithoutRoundup);

        objSmartHumsafarBussLogic.setPremiumAdbRiderSpouseWithoutRoundup();
        premiumAdbRiderSpouseWithoutRoundup = objSmartHumsafarBussLogic
                .getPremiumAdbRiderSpouseWithoutRoundup();
//		System.out.println("** premiumAdbRiderSpouseWithoutRoundup  "+premiumAdbRiderSpouseWithoutRoundup);

        getPremiumAdbRiderSpouseWithRound = objSmartHumsafarBussLogic.getPremiumAdbRiderSpouseWithRound();
        //	System.out.println("getPremiumAdbRiderSpouseWithRound "+getPremiumAdbRiderSpouseWithRound);

        objSmartHumsafarBussLogic.setInstbasicrider();

        //InstBasicRider = (objSmartHumsafarBussLogic.getPremiumAdbRiderLAWithRoundup() + objSmartHumsafarBussLogic.getPremiumAdbRiderSpouseWithRound());
        InstBasicRider = (objSmartHumsafarBussLogic.getInstbasicrider());


        objSmartHumsafarBussLogic.setInstbasicriderWITHOUTRoundup();
        InstbasicriderWITHOUTRoundup = objSmartHumsafarBussLogic.getInstbasicriderWITHOUTRoundup();
        //	System.out.println("InstBasicRider "+InstBasicRider);
//		System.out.println("premiumAdbRiderLAWithRound "+premiumAdbRiderLAWithRound);
//		System.out.println("getPremiumAdbRiderSpouseWithRound "+getPremiumAdbRiderSpouseWithRound);

        // added by vrushali on 09 Nov 2015 start
        objSmartHumsafarBussLogic
                .setPremiumAdbRiderLAWithoutDiscWithoutRoundup();
        premiumAdbRiderLAWithoutDiscWithoutRoundup = objSmartHumsafarBussLogic
                .getPremiumAdbRiderLAWithoutDiscWithoutRoundup();
//		System.out.println("** premiumAdbRiderLAWithoutDiscWithoutRoundup  "
//				+ premiumAdbRiderLAWithoutDiscWithoutRoundup);

        objSmartHumsafarBussLogic
                .setPremiumAdbRiderSpouseWithoutDiscWithoutRoundup();
        premiumAdbRiderSpouseWithoutDiscWithoutRoundup = objSmartHumsafarBussLogic
                .getPremiumAdbRiderSpouseWithoutDiscWithoutRoundup();
//		System.out.println("** premiumAdbRiderSpouseWithoutDiscWithoutRoundup  "+ premiumAdbRiderSpouseWithoutDiscWithoutRoundup);

        objSmartHumsafarBussLogic.setBasicPremiumAdbRiderLAWithoutRoundup();
        basicPremiumAdbRiderLAWithoutRoundup = objSmartHumsafarBussLogic
                .getBasicPremiumAdbRiderLAWithoutRoundup();
//		System.out.println("** basicPremiumAdbRiderLAWithoutRoundup  "+ basicPremiumAdbRiderLAWithoutRoundup);

        objSmartHumsafarBussLogic.setBasicPremiumAdbRiderSpouseWithoutRoundup();
        basicPremiumAdbRiderSpouseWithoutRoundup = objSmartHumsafarBussLogic
                .getBasicPremiumAdbRiderSpouseWithoutRoundup();
//		System.out.println("** basicPremiumAdbRiderSpouseWithoutRoundup  "+ basicPremiumAdbRiderSpouseWithoutRoundup);
        // End

        objSmartHumsafarBussLogic.setBasicPremiumWithRiderWithRoundUP();
        basePremiumWithRiderWithRoundUP = objSmartHumsafarBussLogic
                .getBasicPremiumWithRiderWithRoundUP();
        //System.out.println("** basePremiumWithRiderWithRoundUP  "+basePremiumWithRiderWithRoundUP);


        // added by vrushali on 12 Nov 2015
        objSmartHumsafarBussLogic
                .setBasicPremiumWithoutDiscWithRiderWithRoundUP();
        basePremiumWithoutDiscWithRiderWithRoundUP = objSmartHumsafarBussLogic
                .getBasicPremiumWithoutDiscWithRiderWithRoundUP();
//		System.out.println("** basePremiumWithoutDiscWithRiderWithRoundUP  "+ basePremiumWithoutDiscWithRiderWithRoundUP);
        // end

        /*** modified by Akshaya on 20-MAY-16 start **/

        //  Added By Saurabh Jain on 16/05/2019 Start


        double basicServiceTax = 0;
        double SBCServiceTax = 0;
        double KKCServiceTax = 0;
        double kerlaServiceTax = 0;
        double KeralaCessServiceTax = 0;
        boolean isKerlaDisc = smarthumsafarbean.isKerlaDisc();

        if (isKerlaDisc) {
            basicServiceTax = objSmartHumsafarBussLogic.getServiceTax(Double.parseDouble(premiumWithoutStwtRoundupofBasicprem), smarthumsafarbean.getIsJKResidentDiscOrNot(), "basic", smarthumsafarbean.isKerlaDisc());
            kerlaServiceTax = objSmartHumsafarBussLogic.getServiceTax(Double.parseDouble(premiumWithoutStwtRoundupofBasicprem), smarthumsafarbean.getIsJKResidentDiscOrNot(), "KERALA", smarthumsafarbean.isKerlaDisc());
            KeralaCessServiceTax = kerlaServiceTax - basicServiceTax;
        } else {
            basicServiceTax = objSmartHumsafarBussLogic.getServiceTax(Double.parseDouble(premiumWithoutStwtRoundupofBasicprem), smarthumsafarbean.getIsJKResidentDiscOrNot(), "basic", smarthumsafarbean.isKerlaDisc());
            SBCServiceTax = objSmartHumsafarBussLogic.getServiceTax(Double.parseDouble(premiumWithoutStwtRoundupofBasicprem), smarthumsafarbean.getIsJKResidentDiscOrNot(), "SBC", smarthumsafarbean.isKerlaDisc());
            KKCServiceTax = objSmartHumsafarBussLogic.getServiceTax(Double.parseDouble(premiumWithoutStwtRoundupofBasicprem), smarthumsafarbean.getIsJKResidentDiscOrNot(), "KKC", smarthumsafarbean.isKerlaDisc());
        }

        serviceTax = commonForAllProd.getStringWithout_E(basicServiceTax + SBCServiceTax + KKCServiceTax + kerlaServiceTax);


        PremiumWithST = commonForAllProd.getStringWithout_E(Double.parseDouble(premiumWithoutStwtRoundupofBasicprem) + Double.parseDouble(serviceTax));

        PremiumWithSTWITHOUTRound = commonForAllProd.getStringWithout_E(Double.parseDouble(PremiumWITHOUTroundup) + Double.parseDouble(serviceTax));

//		System.out.println("PremiumWithSTWITHOUTRound  "+PremiumWithSTWITHOUTRound);
//		System.out.println("premiumWithoutStwtRoundupofBasicprem "+premiumWithoutStwtRoundupofBasicprem);
//		System.out.println("serviceTax "+serviceTax);

        PremiumWithSTRound = commonForAllProd.getStringWithout_E(Double.parseDouble(BasicPremiumRound) + Double.parseDouble(serviceTax));

//		System.out.println("PremiumWithST "+PremiumWithST);
//		System.out.println("PremiumWithSTRound "+PremiumWithSTRound);

        //added by sujata on 25-02-2020 for Second Year Service tax
        double basicServiceTaxSecondYear = 0;
        double SBCServiceTaxSecondYear = 0;
        double KKCServiceTaxSecondYear = 0;
        double kerlaServiceTaxSecondYear = 0;
        double KeralaCessServiceTaxSecondYear = 0;

        if (isKerlaDisc) {
            basicServiceTaxSecondYear = objSmartHumsafarBussLogic.getServiceTaxSecondYear(Double.parseDouble(premiumWithoutStwtRoundupofBasicprem), smarthumsafarbean.getIsJKResidentDiscOrNot(), "basic", smarthumsafarbean.isKerlaDisc());
            kerlaServiceTaxSecondYear = objSmartHumsafarBussLogic.getServiceTaxSecondYear(Double.parseDouble(premiumWithoutStwtRoundupofBasicprem), smarthumsafarbean.getIsJKResidentDiscOrNot(), "KERALA", smarthumsafarbean.isKerlaDisc());
            KeralaCessServiceTaxSecondYear = kerlaServiceTax - basicServiceTax;
        } else {
            basicServiceTaxSecondYear = objSmartHumsafarBussLogic.getServiceTaxSecondYear(Double.parseDouble(premiumWithoutStwtRoundupofBasicprem), smarthumsafarbean.getIsJKResidentDiscOrNot(), "basic", smarthumsafarbean.isKerlaDisc());
            SBCServiceTaxSecondYear = objSmartHumsafarBussLogic.getServiceTaxSecondYear(Double.parseDouble(premiumWithoutStwtRoundupofBasicprem), smarthumsafarbean.getIsJKResidentDiscOrNot(), "SBC", smarthumsafarbean.isKerlaDisc());
            KKCServiceTaxSecondYear = objSmartHumsafarBussLogic.getServiceTaxSecondYear(Double.parseDouble(premiumWithoutStwtRoundupofBasicprem), smarthumsafarbean.getIsJKResidentDiscOrNot(), "KKC", smarthumsafarbean.isKerlaDisc());
        }

        serviceTaxSecondYear = commonForAllProd.getStringWithout_E(basicServiceTaxSecondYear + SBCServiceTaxSecondYear + KKCServiceTaxSecondYear + kerlaServiceTaxSecondYear);

        PremiumWithSTSecondYear = commonForAllProd.getStringWithout_E(Double.parseDouble(premiumWithoutStwtRoundupofBasicprem) + Double.parseDouble(serviceTaxSecondYear));
        PremiumWithSTSecondYearWITHOUTRoundup = commonForAllProd.getStringWithout_E(Double.parseDouble(PremiumWITHOUTroundup) + Double.parseDouble(serviceTaxSecondYear));


        PremiumWithSTRoundSY = commonForAllProd.getStringWithout_E(Double.parseDouble(BasicPremiumRound) + Double.parseDouble(serviceTaxSecondYear));
        //System.out.println("PremiumWithSTRoundSY "+PremiumWithSTRoundSY);

        //End

        /*****************start*********/
        /**************sujata 19-02-2020***************for rider**/

        //added by sujata on 19-02-2020 for rider first year
        double basicServiceTaxRider = 0;
        double SBCServiceTaxRider = 0;
        double KKCServiceTaxRider = 0;
        double kerlaServiceTaxRider = 0;
        double KeralaCessServiceTaxRider = 0;

        if (isKerlaDisc) {
            basicServiceTaxRider = objSmartHumsafarBussLogic.getServiceTaxRider(Double.parseDouble(InstBasicRider), smarthumsafarbean.getIsJKResidentDiscOrNot(), "basic", smarthumsafarbean.isKerlaDisc());
            kerlaServiceTaxRider = objSmartHumsafarBussLogic.getServiceTaxRider(Double.parseDouble(InstBasicRider), smarthumsafarbean.getIsJKResidentDiscOrNot(), "KERALA", smarthumsafarbean.isKerlaDisc());
            KeralaCessServiceTax = kerlaServiceTax - basicServiceTax;
        } else {
            basicServiceTaxRider = objSmartHumsafarBussLogic.getServiceTaxRider(Double.parseDouble(InstBasicRider), smarthumsafarbean.getIsJKResidentDiscOrNot(), "basic", smarthumsafarbean.isKerlaDisc());
            SBCServiceTaxRider = objSmartHumsafarBussLogic.getServiceTaxRider(Double.parseDouble(InstBasicRider), smarthumsafarbean.getIsJKResidentDiscOrNot(), "SBC", smarthumsafarbean.isKerlaDisc());
            KKCServiceTaxRider = objSmartHumsafarBussLogic.getServiceTaxRider(Double.parseDouble(InstBasicRider), smarthumsafarbean.getIsJKResidentDiscOrNot(), "KKC", smarthumsafarbean.isKerlaDisc());
        }

        String serviceTaxFirstYearRider = commonForAllProd.getStringWithout_E(basicServiceTaxRider + SBCServiceTaxRider + KKCServiceTaxRider + kerlaServiceTaxRider);
        //System.out.println("serviceTaxFirstYear "+serviceTaxFirstYear);

        String premiumInstWithSTFirstYearRider = commonForAllProd.getStringWithout_E(Double.parseDouble(InstBasicRider) + Double.parseDouble(serviceTaxFirstYearRider));

        String premiumInstWithSTFirstYearRiderWITHOUTRound = commonForAllProd.getStringWithout_E(Double.parseDouble(InstbasicriderWITHOUTRoundup) + Double.parseDouble(serviceTaxFirstYearRider));

        //System.out.println("premiumInstWithSTFirstYearRiderWITHOUTRound "+premiumInstWithSTFirstYearRiderWITHOUTRound);

        //InstbasicriderWITHOUTRoundup
//		System.out.println("InstBasicRider "+InstBasicRider);
//		System.out.println("serviceTaxFirstYearRider "+serviceTaxFirstYearRider);


        //End of riders first year

        //String totalInstPremFirstYear = commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(Double.parseDouble(premiumInstWithSTFirstYearRiderWITHOUTRound) + Double.parseDouble(PremiumWithSTWITHOUTRound)));

//		System.out.println("\n premiumInstWithSTFirstYearRider "+premiumInstWithSTFirstYearRider);
//		System.out.println("PremiumWithST " + PremiumWithST);
//		System.out.println("totalInstPremFirstYear "+totalInstPremFirstYear);

        //String totalInstPreSecondtYear = commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(Double.parseDouble(premiumInstWithSTFirstYearRider) + Double.parseDouble(premiumInstWithSTFirstYear)));

        //added by sujata on 19-02-2020 for rider Second year
        double basicServiceTaxRiderSY = 0;
        double SBCServiceTaxRiderSY = 0;
        double KKCServiceTaxRiderSY = 0;
        double kerlaServiceTaxRiderSY = 0;
        double KeralaCessServiceTaxRiderSY = 0;

        if (isKerlaDisc) {
            basicServiceTaxRiderSY = objSmartHumsafarBussLogic.getServiceTaxRiderSY(Double.parseDouble(InstBasicRider), smarthumsafarbean.getIsJKResidentDiscOrNot(), "basic", smarthumsafarbean.isKerlaDisc());
            kerlaServiceTaxRiderSY = objSmartHumsafarBussLogic.getServiceTaxRiderSY(Double.parseDouble(InstBasicRider), smarthumsafarbean.getIsJKResidentDiscOrNot(), "KERALA", smarthumsafarbean.isKerlaDisc());
            KeralaCessServiceTaxRiderSY = kerlaServiceTaxRiderSY - basicServiceTaxRiderSY;
        } else {
            basicServiceTaxRiderSY = objSmartHumsafarBussLogic.getServiceTaxRiderSY(Double.parseDouble(InstBasicRider), smarthumsafarbean.getIsJKResidentDiscOrNot(), "basic", smarthumsafarbean.isKerlaDisc());
            SBCServiceTaxRiderSY = objSmartHumsafarBussLogic.getServiceTaxRiderSY(Double.parseDouble(InstBasicRider), smarthumsafarbean.getIsJKResidentDiscOrNot(), "SBC", smarthumsafarbean.isKerlaDisc());
            KKCServiceTaxRiderSY = objSmartHumsafarBussLogic.getServiceTaxRiderSY(Double.parseDouble(InstBasicRider), smarthumsafarbean.getIsJKResidentDiscOrNot(), "KKC", smarthumsafarbean.isKerlaDisc());
        }

        String serviceTaxSecondYearRiderSY = commonForAllProd.getStringWithout_E(basicServiceTaxRiderSY + SBCServiceTaxRiderSY + KKCServiceTaxRiderSY + kerlaServiceTaxRiderSY);
        //System.out.println("serviceTaxFirstYear "+serviceTaxFirstYear);

        String premiumInstWithSTSecondYearRiderSY = commonForAllProd.getStringWithout_E(Double.parseDouble(InstBasicRider) + Double.parseDouble(serviceTaxSecondYearRiderSY));

        String premiumInstWithSTSecondYearRiderSYWITHOUTRound = commonForAllProd.getStringWithout_E(Double.parseDouble(InstbasicriderWITHOUTRoundup) + Double.parseDouble(serviceTaxSecondYearRiderSY));

        //System.out.println("premiumInstWithSTSecondYearRiderSYWITHOUTRound "+premiumInstWithSTSecondYearRiderSYWITHOUTRound);


        //End of riders
        String totalInstPremSecondYearSY = commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(Double.parseDouble(premiumInstWithSTSecondYearRiderSY) + Double.parseDouble(PremiumWithSTSecondYear)));

        //String totalInstPremSecondYearSY = commonForAllProd.getRound(commonForAllProd.getStringWithout_E(Double.parseDouble(premiumInstWithSTSecondYearRiderSYWITHOUTRound) + Double.parseDouble(PremiumWithSTSecondYearWITHOUTRoundup)));
        //System.out.println("totalInstPremSecondYearSY " + totalInstPremSecondYearSY);
        //end


        /******************end**********/


        /*** modified by Akshaya on 20-MAY-16 start **/

        double staffDiscValue = objSmartHumsafarBussLogic
                .getStaffRebate(smarthumsafarbean.getIsStaffDiscOrNot());
        staffRebate = String.valueOf(staffDiscValue);
        // System.out.println("staffRebate " + staffRebate);

        /****************************** Added by Vrushali on 19-Nov-15 start **********************/

        MinesOccuInterest = ""
                + objSmartHumsafarBussLogic
                .getMinesOccuInterest(smarthumsafarbean.getsumAssured());

        servicetax_MinesOccuInterest = ""
                + objSmartHumsafarBussLogic
                .getServiceTaxMines(Double.parseDouble(MinesOccuInterest));

        MinesOccuInterest = "" + (Double.parseDouble(MinesOccuInterest) + Double.parseDouble(servicetax_MinesOccuInterest));


        /** for testing **/
        // basePremiumWithoutDiscWithRiderWithRoundUP =
        // cfap.getStringWithout_E(Double.parseDouble(basePremiumWithoutDiscWithRiderWithRoundUP)
        // + Double.parseDouble(MinesOccuInterest));
        /** End **/

        /****************************** Added by Akshaya on 03-MAR-15 start **********************/

        if (rb_smart_humsafar_backdating_yes.isChecked()) {
            // "16-jan-2014")));
            /*BackDateinterest = cfap.getRoundUp(""
                    + (objSmartHumsafarBussLogic.getBackDateInterest(Double
                            .parseDouble(PremiumWithST),
                    btn_smart_humsafar_backdatingdate.getText()
                            .toString())));*/
            BackDateinterest = cfap.getRoundUp(""
                    + (objSmartHumsafarBussLogic.getBackDateInterest(Double
                            .parseDouble(PremiumWithSTRound),
                    btn_smart_humsafar_backdatingdate.getText()
                            .toString())));

            BackDateinterestwithGST = commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(Double.parseDouble(BackDateinterest)
                    + (Double.parseDouble(BackDateinterest) * prop.GSTforbackdate)));
        } else {
            BackDateinterestwithGST = "0";
        }

        PremiumWithST = cfap.getRoundUp(cfap.getStringWithout_E(Double
                .parseDouble(PremiumWithST)
                + Double.parseDouble(BackDateinterestwithGST)));

        PremiumWithSTRound = cfap.getRoundUp(cfap.getStringWithout_E(Double
                .parseDouble(PremiumWithSTRound)
                + Double.parseDouble(BackDateinterestwithGST)));
        String totalInstPremFirstYear = commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(Double.parseDouble(premiumInstWithSTFirstYearRiderWITHOUTRound) + Double.parseDouble(PremiumWithSTWITHOUTRound)));
        /****************************** Added by Akshaya on 03-MAR-15 end **********************/

        /****************************** Added by Vrushali on 19-Nov-15 end **********************/

        try {
            for (int i = 0; i < smarthumsafarbean.getPolicyTerm_Basic(); i++)
            // for(int i=0;i<1;i++)
            {
                rowNumber++;

                year_F = rowNumber;
                bussIll.append("<policyYr" + year_F + ">" + year_F
                        + "</policyYr" + year_F + ">");

//				System.out.println("1. year_F " + year_F);

                objSmartHumsafarBussLogic.setTotalBasePremiumPaid(year_F);
                totalBasePremiumPaid = objSmartHumsafarBussLogic
                        .getTotalBasePremiumPaid();
                //	System.out.println("2.Total Base Premium Paid "			+ totalBasePremiumPaid);


                sumTotalBasePrem += Double.parseDouble(totalBasePremiumPaid);
                //	System.out.println("sumTotalBasePrem "+sumTotalBasePrem);

                bussIll.append("<totalBasePremiumPaid" + year_F + ">" + totalBasePremiumPaid + "</totalBasePremiumPaid" + year_F + ">");
                //System.out.println("totalBasePremiumPaid "+totalBasePremiumPaid);

                objSmartHumsafarBussLogic.setPremiumWithoutSTFrequencyLoading();
                sumtotalfreqLoading = objSmartHumsafarBussLogic.getPremiumWithoutSTFrequencyLoading();


                objSmartHumsafarBussLogic.setannulizedPremFinal(year_F, smarthumsafarbean.getPolicyTerm_Basic(), smarthumsafarbean.getPremFreqMode());
                annulizedPremium = objSmartHumsafarBussLogic.getannulizedPremFinal();
                bussIll.append("<annulizedPremium" + year_F + ">" + commonForAllProd.getRound(commonForAllProd.getStringWithout_E(Double.parseDouble(annulizedPremium))) + "</annulizedPremium" + year_F + ">");

                bussIll.append("<GuaranteedAddition" + year_F + ">" + 0 + "</GuaranteedAddition" + year_F + ">");
//				bussIll.append("<GuaranteedSurvivalBenefit" + year_F + ">"	+ 0 + "</SurvivalBenefit"+ year_F + ">");

                //added by sujata on 27-02-2020
                objSmartHumsafarBussLogic.setPremiumwithoutModelLoading();
                PremiumwithoutModelLoading = objSmartHumsafarBussLogic.getPremiumwithoutModelLoading();
                //System.out.println("\n PremiumwithoutModelLoading "+PremiumwithoutModelLoading);

                //end

                objSmartHumsafarBussLogic.setGuaranteedBenefitFirstDeath(sumTotalBasePrem, Double.parseDouble(PremiumwithoutModelLoading), year_F);
                GuaranteedDeathBenefitFirstDeath = objSmartHumsafarBussLogic.getGuaranteedBenefitFirstDeath();
//				System.out.println("3.Guaranteed Death Benefit First Death "+ GuaranteedDeathBenefitFirstDeath);
                bussIll.append("<GuaranteedDeathBenefitFirstDeath" + year_F + ">" + commonForAllProd.getStringWithout_E(Double.parseDouble(GuaranteedDeathBenefitFirstDeath)) + "</GuaranteedDeathBenefitFirstDeath" + year_F + ">");

                objSmartHumsafarBussLogic.setGuaranteedBenefitSecondDeath(sumTotalBasePrem, Double.parseDouble(PremiumwithoutModelLoading), year_F);
                GuaranteedDeathBenefitSecondDeath = objSmartHumsafarBussLogic
                        .getGuaranteedBenefitSecondDeath();
//				System.out.println("3.Guaranteed Death Benefit Second Death"
//						+ GuaranteedDeathBenefitSecondDeath);
                //	System.out.println("sumTotalBasePrem "+( commonForAllProd.getStringWithout_E(sumTotalBasePrem)));

                bussIll.append("<GuaranteedDeathBenefitSecondDeath"
                        + year_F
                        + ">"
                        + commonForAllProd.getStringWithout_E(Double
                        .parseDouble(GuaranteedDeathBenefitSecondDeath))
                        + "</GuaranteedDeathBenefitSecondDeath" + year_F + ">");

                objSmartHumsafarBussLogic
                        .setNonGuarateedDeathBenefitAt_4_Percent(year_F);
                NonGuarateedDeathBenefitAt_4_Percent = objSmartHumsafarBussLogic
                        .getNonGuarateedDeathBenefitAt_4_Percent();


                objSmartHumsafarBussLogic.setNonGuarateedDeathBenefitAt_4_PercentFinal(year_F);
                NonGuarateedDeathBenefitAt_4_PercentFinal = objSmartHumsafarBussLogic
                        .getNonGuarateedDeathBenefitAt_4_PercentFinal();


                objSmartHumsafarBussLogic.setNonGuarateedDeathBenefitAt_8_PercentFinal(year_F);
                NonGuarateedDeathBenefitAt_8_PercentFinal = objSmartHumsafarBussLogic
                        .getNonGuarateedDeathBenefitAt_8_PercentFinal();
//				System.out
//						.println("4.Non Guarateed Death Benefit At_4_Percent "
//								+ NonGuarateedDeathBenefitAt_4_Percent);


                bussIll.append("<GuaranteedSurvivalBenefit" + year_F + ">" + 0 + "</GuaranteedSurvivalBenefit" + year_F + ">");

                bussIll.append("<NonGuarateedDeathBenefitAt_4_Percent"
                        + year_F
                        + ">"
                        + commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(Double
                        .parseDouble(NonGuarateedDeathBenefitAt_4_PercentFinal)))
                        + "</NonGuarateedDeathBenefitAt_4_Percent" + year_F
                        + ">");

                objSmartHumsafarBussLogic
                        .setNonGuarateedDeathBenefitAt_8_Percent(year_F);
                NonGuarateedDeathBenefitAt_8_Percent = objSmartHumsafarBussLogic
                        .getNonGuarateedDeathBenefitAt_8_Percent();
//				System.out
//						.println("5.Non Guarateed Death Benefit At_8_Percent "
//								+ NonGuarateedDeathBenefitAt_8_Percent);
                bussIll.append("<NonGuarateedDeathBenefitAt_8_Percent"
                        + year_F
                        + ">"
                        + commonForAllProd.getStringWithout_E(Math.round(Double
                        .parseDouble(NonGuarateedDeathBenefitAt_8_PercentFinal)))
                        + "</NonGuarateedDeathBenefitAt_8_Percent" + year_F
                        + ">");

                objSmartHumsafarBussLogic.setGuaranteedSurvivalBenefit(year_F);
                GuaranteedSurvivalBenefit = objSmartHumsafarBussLogic
                        .getGuaranteedSurvivalBenefit();
//				System.out.println("6.Guaranteed Survival Benefit "
//						+ GuaranteedSurvivalBenefit);


                bussIll.append("<GuaranteedMaturityBenefitBenefit"
                        + year_F
                        + ">"
                        + commonForAllProd.getStringWithout_E(Double
                        .parseDouble(GuaranteedSurvivalBenefit))
                        + "</GuaranteedMaturityBenefitBenefit" + year_F + ">");

                objSmartHumsafarBussLogic
                        .setNonGuarateedSurvivalBenefitAt_4_Percent(year_F);
                NonGuarateedSurvivalBenefitAt_4_Percent = objSmartHumsafarBussLogic
                        .getNonGuarateedSurvivalBenefitAt_4_Percent();
//				System.out
//						.println("7.Non Guarateed Survival Benefit At_4_Percent "
//								+ NonGuarateedSurvivalBenefitAt_4_Percent);
                bussIll.append("<NonGuarateedSurvivalBenefitAt_4_Percent"
                        + year_F
                        + ">"
                        + commonForAllProd.getStringWithout_E(Double
                        .parseDouble(NonGuarateedSurvivalBenefitAt_4_Percent))
                        + "</NonGuarateedSurvivalBenefitAt_4_Percent" + year_F
                        + ">");

                objSmartHumsafarBussLogic
                        .setNonGuarateedSurvivalBenefitAt_8_Percent(year_F);
                NonGuarateedSurvivalBenefitAt_8_Percent = objSmartHumsafarBussLogic
                        .getNonGuarateedSurvivalBenefitAt_8_Percent();
//				System.out
//						.println("8.Non Guarateed Survival Benefit At_8_Percent "
//								+ NonGuarateedSurvivalBenefitAt_8_Percent);
                bussIll.append("<NonGuarateedSurvivalBenefitAt_8_Percent"
                        + year_F
                        + ">"
                        + commonForAllProd.getStringWithout_E(Double
                        .parseDouble(NonGuarateedSurvivalBenefitAt_8_Percent))
                        + "</NonGuarateedSurvivalBenefitAt_8_Percent" + year_F
                        + ">");

                objSmartHumsafarBussLogic.setGSV_SurrenderValue(year_F,
                        sumTotalBasePrem);
                GSV_surrendr_val = objSmartHumsafarBussLogic
                        .getGSV_SurrenderValue();
//				System.out.println("GSV_surrendr_val " + GSV_surrendr_val);


//				if(year_F<=smarthumsafarbean.getPolicyTerm_Basic())
//				{
//					sumTotalbasePremiumPaid=(sumTotalbasePremiumPaid + Math.round(sumTotalBasePrem));
//					//System.out.println("sumTotalbasePremiumPaid "+sumTotalbasePremiumPaid);
//				}

                bussIll.append("<GSV_surrendr_val"
                        + year_F
                        + ">"
                        + commonForAllProd.getRound(commonForAllProd.getStringWithout_E(Double
                        .parseDouble(GSV_surrendr_val)))
                        + "</GSV_surrendr_val" + year_F + ">");

                objSmartHumsafarBussLogic
                        .setNonGSV_surrndr_val_4_Percent(year_F);
                NonGSV_surrndr_val_4Percent = objSmartHumsafarBussLogic
                        .getNonGSV_surrndr_val_4_Percent();

//				System.out.println("NonGSV_surrndr_val_4Percent "
//						+ NonGSV_surrndr_val_4Percent);
                bussIll.append("<NonGSV_surrndr_val_4Percent"
                        + year_F
                        + ">"
                        + commonForAllProd.getStringWithout_E(Double
                        .parseDouble(NonGSV_surrndr_val_4Percent))
                        + "</NonGSV_surrndr_val_4Percent" + year_F + ">");

                objSmartHumsafarBussLogic
                        .setNonGSV_surrndr_val_8_Percent(year_F);
                NonGSV_surrndr_val_8Percent = objSmartHumsafarBussLogic
                        .getNonGSV_surrndr_val_8_Percent();
//				System.out.println("NonGSV_surrndr_val_8Percent "
//						+ NonGSV_surrndr_val_8Percent);
                bussIll.append("<NonGSV_surrndr_val_8Percent"
                        + year_F
                        + ">"
                        + commonForAllProd.getStringWithout_E(Double
                        .parseDouble(NonGSV_surrndr_val_8Percent))
                        + "</NonGSV_surrndr_val_8Percent" + year_F + ">");


                TotalMaturityBenefit4percent = commonForAllProd.getRound(commonForAllProd
                        .getStringWithout_E(objSmartHumsafarBussLogic.getTotalMaturityBenefit4percent(year_F, smarthumsafarbean.getPolicyTerm_Basic(), Double.parseDouble(GuaranteedSurvivalBenefit), Double.parseDouble(NonGuarateedDeathBenefitAt_4_PercentFinal))));

                bussIll.append("<TotalMaturityBenefit4percent" + year_F + ">"
                        + TotalMaturityBenefit4percent
                        + "</TotalMaturityBenefit4percent" + year_F + ">");

                TotalMaturityBenefit8percent = commonForAllProd.getRound(commonForAllProd
                        .getStringWithout_E(objSmartHumsafarBussLogic.getTotalMaturityBenefit8percent(year_F, smarthumsafarbean.getPolicyTerm_Basic(), Double.parseDouble(GuaranteedSurvivalBenefit), Double.parseDouble(NonGuarateedDeathBenefitAt_8_PercentFinal))));

                bussIll.append("<TotalMaturityBenefit8percent" + year_F + ">"
                        + TotalMaturityBenefit8percent
                        + "</TotalMaturityBenefit8percent" + year_F + ">");


                if (year_F == 1) {
                    firstyearofdeath4per = Double.parseDouble(GuaranteedDeathBenefitFirstDeath);
                    //System.out.println("firstyearofdeath4per "+firstyearofdeath4per);
                }


                TotalDeathBenefit4perecentFirst = commonForAllProd.getRound(commonForAllProd
                        .getStringWithout_E(objSmartHumsafarBussLogic.getTotalDeathBenefit4perecentFirst(year_F, smarthumsafarbean.getPolicyTerm_Basic(), firstyearofdeath4per)));

                bussIll.append("<TotalDeathBenefit4perecentFirst" + year_F + ">"
                        + TotalDeathBenefit4perecentFirst
                        + "</TotalDeathBenefit4perecentFirst" + year_F + ">");

                TotalDeathBenefit8perecentFirst = commonForAllProd.getRound(commonForAllProd
                        .getStringWithout_E(objSmartHumsafarBussLogic.getTotalDeathBenefit8perecentSecond(year_F, smarthumsafarbean.getPolicyTerm_Basic(), firstyearofdeath4per)));

                bussIll.append("<TotalDeathBenefit8perecentFirst" + year_F + ">"
                        + TotalDeathBenefit8perecentFirst
                        + "</TotalDeathBenefit8perecentFirst" + year_F + ">");


                TotalDeathBenefit4perecentSecond = commonForAllProd.getRound(commonForAllProd
                        .getStringWithout_E(objSmartHumsafarBussLogic.getTotalDeathBenefit4percent(year_F, smarthumsafarbean.getPolicyTerm_Basic(), Double.parseDouble(GuaranteedDeathBenefitSecondDeath), Double.parseDouble(NonGuarateedDeathBenefitAt_4_PercentFinal), Math.round(sumTotalBasePrem))));

                //System.out.println("TotalDeathBenefit4perecentSecond "+TotalDeathBenefit4perecentSecond);

                bussIll.append("<TotalDeathBenefit4perecentSecond" + year_F + ">"
                        + TotalDeathBenefit4perecentSecond
                        + "</TotalDeathBenefit4perecentSecond" + year_F + ">");

                TotalDeathBenefit8perecentSecond = commonForAllProd.getRound(commonForAllProd
                        .getStringWithout_E(objSmartHumsafarBussLogic.getTotalDeathBenefit8percent(year_F, smarthumsafarbean.getPolicyTerm_Basic(), Double.parseDouble(GuaranteedDeathBenefitSecondDeath), Double.parseDouble(NonGuarateedDeathBenefitAt_8_PercentFinal), Math.round(sumTotalBasePrem))));

                bussIll.append("<TotalDeathBenefit8perecentSecond" + year_F + ">"
                        + TotalDeathBenefit8perecentSecond
                        + "</TotalDeathBenefit8perecentSecond" + year_F + ">");


            }
        } catch (Exception e) {
            e.printStackTrace();
        }


//		return new String[]{premiumWithoutStwtRoundup,basePremiumWithRiderWithRoundUP,PremiumWithST,serviceTax,premiumAdbRiderLAWithoutRoundup,premiumAdbRiderSpouseWithoutRoundup,
//		GuaranteedDeathBenefitFirstDeath,GuaranteedDeathBenefitSecondDeath,NonGuarateedSurvivalBenefitAt_4_Percent,NonGuarateedSurvivalBenefitAt_8_Percent,
//		GuaranteedSurvivalBenefit};

        return new String[]{premiumWithoutStwtRoundup,
                basePremiumWithRiderWithRoundUP, PremiumWithSTRound, serviceTax,
                premiumAdbRiderLAWithoutRoundup,
                premiumAdbRiderSpouseWithoutRoundup,
                GuaranteedDeathBenefitFirstDeath,
                GuaranteedDeathBenefitSecondDeath,
                commonForAllProd.getStringWithout_E(Double.parseDouble(NonGuarateedSurvivalBenefitAt_4_Percent)),
                commonForAllProd.getStringWithout_E(Double.parseDouble(NonGuarateedSurvivalBenefitAt_8_Percent)),
                commonForAllProd.getStringWithout_E(Double.parseDouble(GuaranteedSurvivalBenefit)),
                equivalentAge,
                premiumWithoutStWithoutDiscwtRoundup,
                premiumAdbRiderLAWithoutDiscWithoutRoundup,
                premiumAdbRiderSpouseWithoutDiscWithoutRoundup,
                basePremiumWithoutDiscWithRiderWithRoundUP, staffStatus,
                staffRebate, basicPremiumWithRoundup,
                basicPremiumAdbRiderLAWithoutRoundup,
                basicPremiumAdbRiderSpouseWithoutRoundup,
                MinesOccuInterest,
                BackDateinterestwithGST,
                commonForAllProd.getStringWithout_E(basicServiceTax),
                commonForAllProd.getStringWithout_E(SBCServiceTax),
                commonForAllProd.getStringWithout_E(KKCServiceTax),
                commonForAllProd.getStringWithout_E(KeralaCessServiceTax),
                premiumAdbRiderLAWithRound, getPremiumAdbRiderSpouseWithRound, InstBasicRider
                , premiumInstWithSTFirstYearRider, premiumInstWithSTSecondYearRiderSY,
                totalInstPremFirstYear, totalInstPremSecondYearSY, PremiumWithSTRoundSY,

//                cfap.getStringWithout_E(basicServiceTax),
//                cfap.getStringWithout_E(SBCServiceTax),
//                cfap.getStringWithout_E(KKCServiceTax),
//                servicetax_MinesOccuInterest,
//                commonForAllProd.getStringWithout_E(KeralaCessServiceTax)
        };
    }

    private boolean valLifeAssuredProposerDetail() {
        if (proposer_Is_Same_As_Life_Assured.equalsIgnoreCase("Y")) {
            if (lifeAssured_Title.equals("")
                    || lifeAssured_First_Name.equals("")
                    || lifeAssured_Last_Name.equals("")) {

                commonMethods.showMessageDialog(context, "Please Fill Name Detail For LifeAssured");
                if (lifeAssured_Title.equals("")) {
                    // apply focusable method
                    setFocusable(spnr_bi_smart_humsafar_life_assured_title);
                    spnr_bi_smart_humsafar_life_assured_title
                            .requestFocus();
                } else if (lifeAssured_First_Name.equals("")) {
                    setFocusable(edt_bi_smart_humsafar_life_assured_first_name);
                    edt_bi_smart_humsafar_life_assured_first_name
                            .requestFocus();
                } else {
                    setFocusable(edt_bi_smart_humsafar_life_assured_last_name);
                    edt_bi_smart_humsafar_life_assured_last_name
                            .requestFocus();
                }

                return false;
            } else if (gender_la.equalsIgnoreCase("")) {

                commonMethods.showMessageDialog(context, "Please Select Gender");
                setFocusable(spnr_bi_smart_humsafar_life_assured_title);
                spnr_bi_smart_humsafar_life_assured_title
                        .requestFocus();

                return false;

            } else if (lifeAssured_Title.equalsIgnoreCase("Mr.")
                    && gender_la.equalsIgnoreCase("Female")) {

                commonMethods.showMessageDialog(context, "Life Assured Title and Gender is not Valid");
                setFocusable(spnr_bi_smart_humsafar_life_assured_title);
                spnr_bi_smart_humsafar_life_assured_title
                        .requestFocus();

                return false;

            } else if (lifeAssured_Title.equalsIgnoreCase("Ms.")
                    && gender_la.equalsIgnoreCase("Male")) {

                commonMethods.showMessageDialog(context, "Life Assured Title and Gender is not Valid");
                setFocusable(spnr_bi_smart_humsafar_life_assured_title);
                spnr_bi_smart_humsafar_life_assured_title
                        .requestFocus();

                return false;

            } else if (lifeAssured_Title.equalsIgnoreCase("Mrs.")
                    && gender_la.equalsIgnoreCase("Male")) {

                commonMethods.showMessageDialog(context, "Life Assured Title and Gender is not Valid");
                setFocusable(spnr_bi_smart_humsafar_life_assured_title);
                spnr_bi_smart_humsafar_life_assured_title
                        .requestFocus();

                return false;
            } else {
                return true;
            }

        } else if (proposer_Is_Same_As_Life_Assured.equalsIgnoreCase("N")) {

            if (lifeAssured_Title.equals("")
                    || lifeAssured_First_Name.equals("")
                    || lifeAssured_Last_Name.equals("")) {

                commonMethods.showMessageDialog(context, "Please Fill Name Detail For LifeAssured");
                if (lifeAssured_Title.equals("")) {
                    // apply focusable method
                    setFocusable(spnr_bi_smart_humsafar_life_assured_title);
                    spnr_bi_smart_humsafar_life_assured_title
                            .requestFocus();
                } else if (lifeAssured_First_Name.equals("")) {

                    edt_bi_smart_humsafar_life_assured_first_name
                            .requestFocus();
                } else {
                    edt_bi_smart_humsafar_life_assured_last_name
                            .requestFocus();
                }

                return false;

            } else {
                return true;
            }
        } else {
            return true;
        }

    }

    public boolean valLifeAssuredSpouseDetail() {

        if (spouse_Title.equals("") || spouse_First_Name.equals("")
                || spouse_Last_Name.equals("")) {

            commonMethods.showMessageDialog(context, "Please Fill Name Detail For Spouse");
            if (spouse_Title.equals("")) {
                // apply focusable method
                setFocusable(spnr_bi_smart_humsafar_proposer_title);
                spnr_bi_smart_humsafar_proposer_title
                        .requestFocus();
            } else if (spouse_First_Name.equals("")) {
                edt_bi_smart_humsafar_proposer_first_name
                        .requestFocus();
            } else {
                edt_bi_smart_humsafar_proposer_last_name
                        .requestFocus();
            }
            return false;

        } else if (gender_spouse.equalsIgnoreCase("")) {

            commonMethods.showMessageDialog(context, "Please Select Spouse Gender");
            setFocusable(spnr_bi_smart_humsafar_proposer_title);
            spnr_bi_smart_humsafar_proposer_title
                    .requestFocus();

            return false;

        } else if (spouse_Title.equalsIgnoreCase("Mr.")
                && gender_spouse.equalsIgnoreCase("Female")) {

            commonMethods.showMessageDialog(context, "Spouse Title and Gender is not Valid");
            setFocusable(spnr_bi_smart_humsafar_proposer_title);
            spnr_bi_smart_humsafar_proposer_title
                    .requestFocus();

            return false;

        } else if (spouse_Title.equalsIgnoreCase("Ms.")
                && gender_spouse.equalsIgnoreCase("Male")) {

            commonMethods.showMessageDialog(context, "Spouse Title and Gender is not Valid");
            setFocusable(spnr_bi_smart_humsafar_proposer_title);
            spnr_bi_smart_humsafar_proposer_title
                    .requestFocus();

            return false;

        } else if (spouse_Title.equalsIgnoreCase("Mrs.")
                && gender_spouse.equalsIgnoreCase("Male")) {

            commonMethods.showMessageDialog(context, "Spouse Title and Gender is not Valid");
            setFocusable(spnr_bi_smart_humsafar_proposer_title);
            spnr_bi_smart_humsafar_proposer_title
                    .requestFocus();

            return false;
        } else {
            return true;
        }
    }

    private boolean valBasicDetail() {
        if (edt_smart_humsafar_contact_no.getText().toString()
                .equals("")) {
            commonMethods.dialogWarning(context, "Please Fill  Mobile No", true);
            edt_smart_humsafar_contact_no.requestFocus();
            return false;
        } else if (edt_smart_humsafar_contact_no.getText()
                .toString().length() != 10) {
            commonMethods.dialogWarning(context, "Please Fill 10 Digit Mobile No", true);
            edt_smart_humsafar_contact_no.requestFocus();
            return false;
        } /*else if (emailId.equals("")) {
            commonMethods.dialogWarning(context, "Please Fill Email Id", true);
            edt_smart_humsafar_Email_id.requestFocus();
            return false;

        } else if (ConfirmEmailId.equals("")) {
            commonMethods.dialogWarning(context, "Please Fill Confirm Email Id", true);
            edt_smart_humsafar_ConfirmEmail_id.requestFocus();
            return false;
        } else if (!ConfirmEmailId.equals(emailId)) {
            commonMethods.dialogWarning(context, "Email Id Does Not Match", true);
            return false;
        }*/ else if (!emailId.equals("")) {

            email_id_validation(emailId);
            if (validationFla1) {

                if (ConfirmEmailId.equals("")) {
                    commonMethods.dialogWarning(context, "Please Fill Confirm Email Id", true);
                    edt_smart_humsafar_ConfirmEmail_id.requestFocus();
                    return false;
                } else if (!ConfirmEmailId.equals(emailId)) {
                    commonMethods.dialogWarning(context, "Email Id Does Not Match", true);
                    return false;
                }

                return true;
            } else {
                commonMethods.dialogWarning(context, "Please Fill Valid Email Id", true);
                edt_smart_humsafar_Email_id.requestFocus();
                return false;
            }
        } else if (!ConfirmEmailId.equals("")) {

            email_id_validation(ConfirmEmailId);
            if (validationFla1) {

                if (emailId.equals("")) {
                    commonMethods.dialogWarning(context, "Please Fill Email Id", true);
                    edt_smart_humsafar_Email_id.requestFocus();
                    return false;

                }

                return true;
            } else {
                commonMethods.dialogWarning(context, "Please Fill Valid Confirm Email Id", true);
                edt_smart_humsafar_ConfirmEmail_id.requestFocus();
                return false;
            }
        } else {
            return true;
        }

    }

    private boolean valDob() {

        // if (proposer_Is_Same_As_Life_Assured.equalsIgnoreCase("Y")) {

        if ((lifeAssured_date_of_birth.equals("") || lifeAssured_date_of_birth
                .equalsIgnoreCase("Select Date"))
                && (lifeAssuredAge.equals(""))) {
            commonMethods.showMessageDialog(context, "Please Select Valid Date Of Birth For LifeAssured");
            btn_bi_smart_humsafar_life_assured_date
                    .setText("Select Date");
            setFocusable(btn_bi_smart_humsafar_life_assured_date);
            btn_bi_smart_humsafar_life_assured_date
                    .requestFocus();

            return false;
        } else {
            return true;
        }
        // } else
        // return true;
    }

    private boolean valProposerDob() {

        if ((spouse_date_of_birth.equals("") || spouse_date_of_birth
                .equalsIgnoreCase("Select Date")) || (ProposerAge.equals(""))) {
            commonMethods.showMessageDialog(context, "Please Select Valid Date Of Birth For Spouse");
            btn_bi_smart_humsafar_proposer_date
                    .setText("Select Date");
            setFocusable(btn_bi_smart_humsafar_proposer_date);
            btn_bi_smart_humsafar_proposer_date.requestFocus();

            return false;
        } else {
            return true;
        }

    }

    public void onClickLADob(View v) {
        if (!na_dob.equals("") && flag == 0) {
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

    public void onClickProposerDob(View v) {
        initialiseDateParameter(spouse_date_of_birth, 35);
        DIALOG_ID = 4;
        showDialog(DATE_DIALOG_ID);
    }


    public void onClickBackDating(View v) {
        String backdate = getDate1(proposer_Backdating_BackDate);
        initialiseDateParameter(backdate, 0);
        DIALOG_ID = 6;
        if ((lifeAssured_date_of_birth != null || !lifeAssured_date_of_birth
                .equals(""))
                && (spouse_date_of_birth != null || !spouse_date_of_birth
                .equals(""))) {
            showDialog(DATE_DIALOG_ID);
        } else {
            commonMethods.dialogWarning(context, "Please select a LifeAssured DOB and Spouse DOB First", true);
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

        int flg_focus = 1;
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

        String final_age = Integer.toString(age);// + " yrs";

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
                        if (18 <= age && age <= 46) {

                            btn_bi_smart_humsafar_proposer_date.setText(date);
                            edt_bi_smart_humsafar_proposer_age.setText(final_age);
                            spouse_date_of_birth = getDate1(date + "");
                            updatePolicyTerm();

                            if (!proposer_Backdating_BackDate.equals("")) {
                                if (proposer_Backdating_WishToBackDate_Policy
                                        .equalsIgnoreCase("y")) {
                                    rb_smart_humsafar_backdating_no
                                            .setChecked(true);
                                    ll_smart_humsafar_backdating
                                            .setVisibility(View.GONE);
                                }
                                proposer_Backdating_BackDate = "";
                                btn_smart_humsafar_backdatingdate
                                        .setText("Select Date");
                            } else {

                                clearFocusable(btn_bi_smart_humsafar_proposer_date);
                                setFocusable(edt_smart_humsafar_contact_no);
                                edt_smart_humsafar_contact_no.requestFocus();
                            }

                        } else {
                            commonMethods.BICommonDialog(context,
                                    "Minimum Age should be 18yrs and Maximum Age should be 46yrs For Spouse");
                            btn_bi_smart_humsafar_proposer_date
                                    .setText("Select Date");
                            edt_bi_smart_humsafar_proposer_age.setText("");
                            spouse_date_of_birth = "";
                            clearFocusable(btn_bi_smart_humsafar_proposer_date);
                            setFocusable(btn_bi_smart_humsafar_proposer_date);
                            btn_bi_smart_humsafar_proposer_date.requestFocus();
                        }
                    }
                    break;

                case 5:
                    final_age = Integer.toString(age);
                    if (final_age.contains("-")) {
                        commonMethods.BICommonDialog(context, "Please fill Valid Birth Date");
                    } else {

                        if (18 <= age && age <= 46) {

                            lifeAssuredAge = final_age;
                            btn_bi_smart_humsafar_life_assured_date.setText(date);
                            edt_bi_smart_humsafar_life_assured_age
                                    .setText(final_age);
                            lifeAssured_date_of_birth = getDate1(date + "");
                            updatePolicyTerm();

                            if (!proposer_Backdating_BackDate.equals("")) {
                                if (proposer_Backdating_WishToBackDate_Policy
                                        .equalsIgnoreCase("y")) {
                                    rb_smart_humsafar_backdating_no
                                            .setChecked(true);
                                    ll_smart_humsafar_backdating
                                            .setVisibility(View.GONE);
                                }
                                proposer_Backdating_BackDate = "";
                                btn_smart_humsafar_backdatingdate
                                        .setText("Select Date");
                            } else {

                                clearFocusable(btn_bi_smart_humsafar_life_assured_date);
                                setFocusable(spnr_bi_smart_humsafar_proposer_title);
                                spnr_bi_smart_humsafar_proposer_title
                                        .requestFocus();
                            }

                        } else {
                            commonMethods.BICommonDialog(context,
                                    "Minimum Age should be 18yrs and Maximum Age should be 46yrs For LifeAssured");
                            btn_bi_smart_humsafar_life_assured_date
                                    .setText("Select Date");
                            edt_bi_smart_humsafar_life_assured_age.setText("");
                            lifeAssured_date_of_birth = "";

                            clearFocusable(btn_bi_smart_humsafar_life_assured_date);
                            setFocusable(btn_bi_smart_humsafar_life_assured_date);
                            btn_bi_smart_humsafar_life_assured_date.requestFocus();
                        }
                    }
                    break;

                case 6:
                    if (age >= 0) {
                        proposer_Backdating_BackDate = date + "";
                        btn_smart_humsafar_backdatingdate
                                .setText(proposer_Backdating_BackDate);
                        clearFocusable(btn_smart_humsafar_backdatingdate);

                        setFocusable(cb_bi_smart_humsafar_adb_rider);
                        cb_bi_smart_humsafar_adb_rider.requestFocus();

                    } else {
                        commonMethods.dialogWarning(context, "Please Select Valid BackDating Date", true);
                        btn_smart_humsafar_backdatingdate.setText("Select Date");
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

            if (life_assured_age < 18) {
                commonMethods.dialogWarning(context, "Please Select Valid BackDating Date", true);
                btn_smart_humsafar_backdatingdate.setText("Select Date");
                proposer_Backdating_BackDate = "";
            } else {
                String str_final_Age = Integer.toString(life_assured_age);
                edt_bi_smart_humsafar_life_assured_age.setText(str_final_Age);
                // valAge();

                int spouse_assured_age = calculateMyAge(mYear,
                        Integer.parseInt(mont), Integer.parseInt(day),
                        spouse_date_of_birth);
                String str_final_Age1 = Integer.toString(spouse_assured_age);
                edt_bi_smart_humsafar_proposer_age.setText(str_final_Age1);

            }
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

    private void updatePolicyTerm() {
        SmartHumsafarBusinessLogic obj = new SmartHumsafarBusinessLogic(null);
        try {
            int equivalentAge = obj.getEquivalentAge(Integer
                    .parseInt(edt_bi_smart_humsafar_life_assured_age.getText()
                            .toString()), Integer
                    .parseInt(edt_bi_smart_humsafar_proposer_age.getText()
                            .toString()));
            int maxPolicyTerm = Math.min(prop.maxPolicyTerm,
                    Math.min(prop.maxPolicyTerm, 65 - equivalentAge));

            String[] policyTerm = new String[maxPolicyTerm - prop.minPolicyTerm
                    + 1];
            for (int i = prop.minPolicyTerm; i <= maxPolicyTerm; i++) {
                policyTerm[i - prop.minPolicyTerm] = "" + i;
            }
            ArrayAdapter<String> policyTermAdapter = new ArrayAdapter<>(
                    getApplicationContext(), R.layout.spinner_item, policyTerm);
            policyTermAdapter.setDropDownViewResource(R.layout.spinner_item1);
            spnr_bi_smart_humsafar_policyterm.setAdapter(policyTermAdapter);
            policyTermAdapter.notifyDataSetChanged();
            updateRiderPolicyTerm();

        } catch (Exception ignored) {
        }
    }

    private void updateRiderPolicyTerm() {
        if (cb_bi_smart_humsafar_adb_rider.isChecked()) {
            maxPolicyTermRider = Math.min(Integer
                            .parseInt(spnr_bi_smart_humsafar_policyterm
                                    .getSelectedItem().toString()),
                    prop.maxPolicyTermRider);

            if (spnr_bi_smart_humsafar_applicable_for.getSelectedItem()
                    .toString().equalsIgnoreCase("Life to be Assured")
                    || spnr_bi_smart_humsafar_applicable_for.getSelectedItem()
                    .toString().equalsIgnoreCase("Both Lives Assured")) {
                String[] policyTermRiderLife = new String[maxPolicyTermRider
                        - prop.minPolicyTermRider + 1];
                for (int i = prop.minPolicyTermRider; i <= maxPolicyTermRider; i++) {
                    policyTermRiderLife[i - prop.minPolicyTermRider] = "" + i;
                }
                // ArrayAdapter<String> policyTermAdapterLife = new
                // ArrayAdapter<String>(getApplicationContext(),
                // R.layout.spinner_item,policyTermRiderLife);
                // policyTermAdapterLife.setDropDownViewResource(android.R.layout.spinner_item1);
                // spnr_bi_smart_humsafar_adb_rider_term.setAdapter(policyTermAdapterLife);
                // policyTermAdapterLife.notifyDataSetChanged();

                ArrayAdapter<String> policyTermAdapterLife = new ArrayAdapter<>(
                        getApplicationContext(), R.layout.spinner_item,
                        policyTermRiderLife);
                policyTermAdapterLife
                        .setDropDownViewResource(R.layout.spinner_item1);
                spnr_bi_smart_humsafar_adb_rider_term
                        .setAdapter(policyTermAdapterLife);
                policyTermAdapterLife.notifyDataSetChanged();
            }

            if (spnr_bi_smart_humsafar_applicable_for.getSelectedItem()
                    .toString().equalsIgnoreCase("Spouse Life to be Assured")
                    || spnr_bi_smart_humsafar_applicable_for.getSelectedItem()
                    .toString().equalsIgnoreCase("Both Lives Assured")) {
                String[] policyTermRiderSpouse = new String[maxPolicyTermRider
                        - prop.minPolicyTermRider + 1];
                for (int i = prop.minPolicyTermRider; i <= maxPolicyTermRider; i++) {
                    policyTermRiderSpouse[i - prop.minPolicyTermRider] = "" + i;
                }
                // ArrayAdapter<String> policyTermAdapterSpouse = new
                // ArrayAdapter<String>(getApplicationContext(),
                // R.layout.spinner_item,policyTermRiderSpouse);
                // policyTermAdapterSpouse.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                // spnr_bi_smart_humsafar_spouse_rider_term.setAdapter(policyTermAdapterSpouse);
                // policyTermAdapterSpouse.notifyDataSetChanged();

                ArrayAdapter<String> policyTermAdapterSpouse = new ArrayAdapter<>(
                        getApplicationContext(), R.layout.spinner_item,
                        policyTermRiderSpouse);
                policyTermAdapterSpouse
                        .setDropDownViewResource(R.layout.spinner_item1);
                spnr_bi_smart_humsafar_spouse_rider_term
                        .setAdapter(policyTermAdapterSpouse);
                policyTermAdapterSpouse.notifyDataSetChanged();
            }
        }
    }

    private void validationOfMoile_EmailId() {

        edt_smart_humsafar_contact_no.addTextChangedListener(new TextWatcher() {

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
                String abc = edt_smart_humsafar_contact_no.getText().toString();
                mobile_validation(abc);

            }
        });

        edt_smart_humsafar_Email_id.addTextChangedListener(new TextWatcher() {

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
                ProposerEmailId = edt_smart_humsafar_Email_id.getText()
                        .toString();
                //email_id_validation(ProposerEmailId);

            }
        });

        edt_smart_humsafar_ConfirmEmail_id
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
                        String proposer_confirm_emailId = edt_smart_humsafar_ConfirmEmail_id
                                .getText().toString();
                        //      confirming_email_id(proposer_confirm_emailId);

                    }
                });

    }


    private void mobile_validation(String number) {
        boolean validationFlag3 = false;
        if ((number.length() != 10)) {
            edt_smart_humsafar_contact_no
                    .setError("Please provide correct 10-digit mobile number");
            validationFlag3 = false;
        } else if ((number.length() == 10)) {
            validationFlag3 = true;
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

                    } else if (latestImage
                            .equalsIgnoreCase("proposerSignOnLifeAssured")) {

                        Ibtn_signatureofLifeAssured
                                .setImageBitmap(ProposerCaptureSignature.scaled);

                        Bitmap signature = ProposerCaptureSignature.scaled;
                        if (signature != null) {
                            ByteArrayOutputStream out = new ByteArrayOutputStream();
                            signature.compress(Bitmap.CompressFormat.PNG, 100,
                                    out);
                            byte[] signByteArray = out.toByteArray();
                            proposerAsLifeAssuredSign = Base64.encodeToString(
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
                        imageButtonSmartHumsafarLifeAssuredPhotograph
                                .setImageBitmap(scaled);
                    } else {
                        photoBitmap = null;
                    }
                } else if (Check.equals("ProposerPhoto")) {

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
                        proposerBitmap = scaled;
                        imageButtonSmartHumsafarProposerPhotograph
                                .setImageBitmap(scaled);
                    } else {
                        proposerBitmap = null;
                    }
                }

            }
        }
        /* end */
    }

    // Validation

    private boolean valInput() {

        String error = "";
        if (edt_bi_smart_humsafar_sum_assured_amount.getText().toString()
                .equals("")) {
            error = "Please enter Sum Assured in Rs.";

        } else if (Integer.parseInt(edt_bi_smart_humsafar_sum_assured_amount
                .getText().toString()) > prop.maxSumAssured) {
            error = "Sum Assured should not be greater than Rs."
                    + currencyFormat.format(prop.maxSumAssured);

        } else if (Integer.parseInt(edt_bi_smart_humsafar_sum_assured_amount
                .getText().toString()) < prop.minSumAssured) {
            error = "Sum Assured should not be less than Rs."
                    + currencyFormat.format(prop.minSumAssured);

        } else if (Integer.parseInt(edt_bi_smart_humsafar_sum_assured_amount
                .getText().toString()) < prop.minSumAssured) {
            error = "Sum Assured should not be less than Rs."
                    + currencyFormat.format(prop.minSumAssured);

        } else if (!(Double.parseDouble(edt_bi_smart_humsafar_sum_assured_amount.getText().toString()) % 1000 == 0)) {
            error = "Sum Assured should be multiple of 1000";
        }

        if (!error.equals("")) {
            commonMethods.showMessageDialog(context, error);
            return false;
        } else
            return true;
    }


    private boolean valAgeDiff() {
        String error = "";
        int ageDiff = 0;
        if (Integer.parseInt(edt_bi_smart_humsafar_life_assured_age.getText()
                .toString()) >= Integer
                .parseInt(edt_bi_smart_humsafar_proposer_age.getText()
                        .toString()))
            ageDiff = Integer.parseInt(edt_bi_smart_humsafar_life_assured_age
                    .getText().toString())
                    - Integer.parseInt(edt_bi_smart_humsafar_proposer_age
                    .getText().toString());
        else
            ageDiff = Integer.parseInt(edt_bi_smart_humsafar_proposer_age
                    .getText().toString())
                    - Integer.parseInt(edt_bi_smart_humsafar_life_assured_age
                    .getText().toString());
        if (ageDiff > 20)
            error = "Maximum age different allowed in between two lives assured is 20 years.";
        else
            error = "";
        if (!error.equals("")) {
            commonMethods.showMessageDialog(context, error);
            return false;
        } else
            return true;
    }

    private boolean valRiderLifeAssured() {
        String error = "";
        double min = Math.min(prop.minADBT_SA, Double
                .parseDouble(edt_bi_smart_humsafar_sum_assured_amount.getText()
                        .toString()));
        double max = Math.min(prop.maxADBT_SA, Double
                .parseDouble(edt_bi_smart_humsafar_sum_assured_amount.getText()
                        .toString()));
        if (cb_bi_smart_humsafar_adb_rider.isChecked()) {
            if (spnr_bi_smart_humsafar_applicable_for.getSelectedItem()
                    .toString().equalsIgnoreCase("Life to be Assured")
                    || spnr_bi_smart_humsafar_applicable_for.getSelectedItem()
                    .toString().equalsIgnoreCase("Both Lives Assured")) {
                if (edt_bi_smart_humsafar_adb_rider_sum_assured.getText()
                        .toString().equals("")) {
                    error = "Please enter ADB Rider Sum Assured for Life Assured in Rs.";
                } else if (Double
                        .parseDouble(edt_bi_smart_humsafar_adb_rider_sum_assured
                                .getText().toString()) > max) {
                    error = "ADB Rider Sum Assured for Life Assured should not be greater than Rs."
                            + currencyFormat.format(max);
                } else if (Double
                        .parseDouble(edt_bi_smart_humsafar_adb_rider_sum_assured
                                .getText().toString()) < min) {
                    error = "ADB Rider Sum Assured for Life Assured should not be less than Rs."
                            + currencyFormat.format(prop.minADBT_SA);
                } else if (!(Double.parseDouble(edt_bi_smart_humsafar_adb_rider_sum_assured.getText().toString()) % 1000 == 0)) {
                    error = "Sum Assured should be multiple of 1000";
                } else
                    error = "";

                if (!error.equals("")) {
                    commonMethods.showMessageDialog(context, error);
                    return false;
                } else
                    return true;
            } else
                return true;
        } else
            return true;
    }

    private boolean valRiderSpouse() {
        String error = "";
        double min = Math.min(prop.minADBT_SA, Double
                .parseDouble(edt_bi_smart_humsafar_sum_assured_amount.getText()
                        .toString()));
        double max = Math.min(prop.maxADBT_SA, Double
                .parseDouble(edt_bi_smart_humsafar_sum_assured_amount.getText()
                        .toString()));
        if (cb_bi_smart_humsafar_adb_rider.isChecked()) {
            if (spnr_bi_smart_humsafar_applicable_for.getSelectedItem()
                    .toString().equalsIgnoreCase("Spouse Life to be Assured")
                    || spnr_bi_smart_humsafar_applicable_for.getSelectedItem()
                    .toString().equalsIgnoreCase("Both Lives Assured")) {
                if (edt_bi_smart_humsafar_spouse_rider_sum_assured.getText()
                        .toString().equals("")) {
                    error = "Please enter ADB Rider Sum Assured for Spouse Life to be Assured in Rs.";
                } else if (Double
                        .parseDouble(edt_bi_smart_humsafar_spouse_rider_sum_assured
                                .getText().toString()) > max) {
                    error = "ADB Rider Sum Assured for Spouse Life should not be greater than Rs."
                            + currencyFormat.format(max);
                } else if (Double
                        .parseDouble(edt_bi_smart_humsafar_spouse_rider_sum_assured
                                .getText().toString()) < min) {
                    error = "ADB Rider Sum Assured for Spouse Life should not be less than Rs."
                            + currencyFormat.format(prop.minADBT_SA);
                } else if (!(Double.parseDouble(edt_bi_smart_humsafar_spouse_rider_sum_assured.getText().toString()) % 1000 == 0)) {
                    error = "Sum Assured should be multiple of 1000";
                } else
                    error = "";

                if (!error.equals("")) {
                    commonMethods.showMessageDialog(context, error);
                    return false;
                } else
                    return true;
            } else
                return true;
        } else
            return true;
    }

    private boolean valPremAmount(double premiumwithRoundUP) {
        String error = "";
        if (spnr_bi_smart_humsafar_premium_paying_mode.getSelectedItem()
                .toString().equals("Yearly")) {
            if (premiumwithRoundUP < prop.minYearlyPrem) {
                error = "Minimum Premium for Yearly mode under this product is Rs."
                        + currencyFormat.format(prop.minYearlyPrem);
            }
        } else if (spnr_bi_smart_humsafar_premium_paying_mode.getSelectedItem()
                .toString().equals("Half Yearly")) {
            if (premiumwithRoundUP < prop.minHalfYearlyPrem) {
                error = "Minimum Premium for Half-Yearly mode under this product is Rs."
                        + currencyFormat.format(prop.minHalfYearlyPrem);
            }
        } else if (spnr_bi_smart_humsafar_premium_paying_mode.getSelectedItem()
                .toString().equals("Quarterly")) {
            if (premiumwithRoundUP < prop.minQuarterleyPrem) {
                error = "Minimum Premium for Quarterly mode under this product is Rs."
                        + currencyFormat.format(prop.minQuarterleyPrem);
            }
        } else if (spnr_bi_smart_humsafar_premium_paying_mode.getSelectedItem()
                .toString().equals("Monthly")) {
            if (premiumwithRoundUP < prop.minMonthleyPrem) {
                error = "Minimum Premium for "
                        + spnr_bi_smart_humsafar_premium_paying_mode
                        .getSelectedItem().toString()
                        + " mode under this product is Rs."
                        + currencyFormat.format(prop.minMonthleyPrem);
            }
        }

        if (!error.equals("")) {
            commonMethods.showMessageDialog(context, error);
            return false;
        } else
            return true;

    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

        boolean flagFocus = true;
        if (!flagFocus) {
            ll_bi_smart_humsafar_main.requestFocus();
        } else {
            spnr_bi_smart_humsafar_life_assured_title.requestFocus();
        }

    }

    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

        // TODO Auto-generated method stub
        if (v.getId() == edt_bi_smart_humsafar_life_assured_first_name.getId()) {
            setFocusable(edt_bi_smart_humsafar_life_assured_middle_name);
            edt_bi_smart_humsafar_life_assured_middle_name.requestFocus();
        } else if (v.getId() == edt_bi_smart_humsafar_life_assured_middle_name
                .getId()) {
            setFocusable(edt_bi_smart_humsafar_life_assured_last_name);
            edt_bi_smart_humsafar_life_assured_last_name.requestFocus();
        } else if (v.getId() == edt_bi_smart_humsafar_life_assured_last_name
                .getId()) {

            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(
                    edt_bi_smart_humsafar_life_assured_last_name
                            .getWindowToken(), 0);
            // clearFocusable(edt_bi_smart_humsafar_life_assured_last_name);
            setFocusable(btn_bi_smart_humsafar_life_assured_date);
            btn_bi_smart_humsafar_life_assured_date.requestFocus();
        } else if (v.getId() == edt_bi_smart_humsafar_proposer_first_name.getId()) {
            setFocusable(edt_bi_smart_humsafar_proposer_middle_name);
            edt_bi_smart_humsafar_proposer_middle_name.requestFocus();
        } else if (v.getId() == edt_bi_smart_humsafar_proposer_middle_name
                .getId()) {
            setFocusable(edt_bi_smart_humsafar_proposer_last_name);
            edt_bi_smart_humsafar_proposer_last_name.requestFocus();
        } else if (v.getId() == edt_bi_smart_humsafar_proposer_last_name
                .getId()) {

            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(
                    edt_bi_smart_humsafar_proposer_last_name.getWindowToken(),
                    0);
            // clearFocusable(edt_bi_smart_humsafar_proposer_last_name);
            setFocusable(btn_bi_smart_humsafar_proposer_date);
            btn_bi_smart_humsafar_proposer_date.requestFocus();
        } else if (v.getId() == edt_smart_humsafar_contact_no.getId()) {
            // clearFocusable(premiumAmt);
            setFocusable(edt_smart_humsafar_Email_id);
            edt_smart_humsafar_Email_id.requestFocus();
        } else if (v.getId() == edt_smart_humsafar_Email_id.getId()) {
            // clearFocusable(premiumAmt);
            setFocusable(edt_smart_humsafar_ConfirmEmail_id);
            edt_smart_humsafar_ConfirmEmail_id.requestFocus();
        } else if (v.getId() == edt_smart_humsafar_ConfirmEmail_id.getId()) {
            setFocusable(spnr_bi_smart_humsafar_policyterm);
            spnr_bi_smart_humsafar_policyterm.requestFocus();
        } else if (v.getId() == edt_bi_smart_humsafar_sum_assured_amount.getId()) {
            commonMethods.hideKeyboard(edt_bi_smart_humsafar_sum_assured_amount, context);
            clearFocusable(cb_bi_smart_humsafar_adb_rider);
            setFocusable(cb_bi_smart_humsafar_adb_rider);
            cb_bi_smart_humsafar_adb_rider.requestFocus();

        } else if (v.getId() == edt_bi_smart_humsafar_adb_rider_sum_assured
                .getId()) {
            setFocusable(edt_bi_smart_humsafar_spouse_rider_sum_assured);
            edt_bi_smart_humsafar_spouse_rider_sum_assured.requestFocus();
        } else if (v.getId() == edt_bi_smart_humsafar_spouse_rider_sum_assured
                .getId()) {
            commonMethods.hideKeyboard(edt_bi_smart_humsafar_spouse_rider_sum_assured, context);
        }

        return true;

    }


}

