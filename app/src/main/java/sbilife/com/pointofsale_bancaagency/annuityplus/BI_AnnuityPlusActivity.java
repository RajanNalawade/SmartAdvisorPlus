package sbilife.com.pointofsale_bancaagency.annuityplus;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.Html;
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

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
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
import sbilife.com.pointofsale_bancaagency.ServiceURL;
import sbilife.com.pointofsale_bancaagency.common.CommonForAllProd;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;
import sbilife.com.pointofsale_bancaagency.common.ProductInfo;
import sbilife.com.pointofsale_bancaagency.common.SimpleCrypto;
import sbilife.com.pointofsale_bancaagency.common.StorageUtils;
import sbilife.com.pointofsale_bancaagency.needanalysis.NeedAnalysisActivity;
import sbilife.com.pointofsale_bancaagency.new_bussiness.ProductBIBean;
import sbilife.com.pointofsale_bancaagency.utility.GridHeight;

public class BI_AnnuityPlusActivity extends AppCompatActivity implements OnEditorActionListener {

    private CheckBox cb_staffdisc;
    private CheckBox cb_bi_annuity_plus_advance_annuity_payout;
    private CheckBox cb_bi_annuity_plus_adb_rider;

    private EditText edt_bi_annuity_plus_life_assured_first_name;
    private EditText edt_bi_annuity_plus_life_assured_middle_name;
    private EditText edt_bi_annuity_plus_life_assured_last_name;
    private EditText edt_bi_annuity_plus_life_assured_age;
    private EditText edt_bi_annuity_plus_life_assured_first_name_second_annuitant;
    private EditText edt_bi_annuity_plus_life_assured_middle_name_second_annuitant;
    private EditText edt_bi_annuity_plus_life_assured_last_name_second_annuitant;
    private EditText edt_bi_annuity_plus_life_assured_age_second_annuitant;
    private EditText edt_annuity_plus_contact_no;
    private EditText edt_annuity_plus_Email_id;
    private EditText edt_annuity_plus_ConfirmEmail_id;
    private EditText edt_annuity_plus_annuity_amount;
    private EditText edt_annuity_plus_vesting_amount;
    private EditText edt_annuity_plus_additional_amount_if_any;

    private Spinner
            spnr_bi_annuity_plus_life_assured_title;
    private Spinner spnr_bi_annuity_plus_life_assured_title_second_annuitant;
    private Spinner spnr_bi_annuity_plus_source_of_business;
    private Spinner spnr_bi_annuity_plus_channel_details;
    private Spinner spnr_bi_annuity_plus_mode_of_annuity_payouts;
    private Spinner spnr_bi_annuity_plus_annuity_option;
    private Spinner spnr_bi_annuity_plus_opt_for;
    private Spinner spnr_bi_annuity_plus_applicable_for;


    private TextView btn_bi_annuity_plus_life_assured_date, btn_bi_annuity_plus_life_assured_date_second_annuitant, btn_bi_annuity_plus_advance_annuity_payout_from_which_date;
    private TextView btn_bi_annuity_plus_proposal_date;
    private Button btnBack;
    private Button btnSubmit;
    private TableRow tr_bi_annuity_plus_advance_annuity_payout;
    private TableRow tr_checkbox_advance_annuity_payout;
    private TableRow tr_additional_amount_if_any;
    private TableRow tr_annuity_amount;
    private TableRow tr_vesting_amount;
    private TableRow tr_bi_annuity_plus_adb_rider;
    private LinearLayout ll_bi_annuity_plus_main;
    private LinearLayout linearlayoutSecondAnnuitant;

    private String QuatationNumber;
    private String planName = "";

    private DatabaseHelper dbHelper;
    private ParseXML prsObj;

    private String emailId = "";
    private String mobileNo = "";
    private String ConfirmEmailId = "";
    private String ProposerEmailId = "";
    private Dialog d;
    private boolean validationFla1 = false;
    private int DIALOG_ID;
    private int mYear;
    private int mMonth;
    private int mDay;
    private String name_of_second_annuitant = "";
    private String name_of_person = "";
    private String place2 = "";
    private String date1 = "";
    private String date2 = "";
    private String agent_sign = "";
    private String proposer_sign = "";
    private String flg_needAnalyis = "";

    private String second_Annuitant_First_Name = "";
    private String second_Annuitant_Middle_Name = "";
    private String second_Annuitant_Last_Name = "";
    private String is_Second_Annuitant = "N";
    private String second_Annuitant_date_of_birth = "";
    private String lifeAssured_Title = "";
    private String lifeAssured_First_Name = "";
    private String lifeAssured_Middle_Name = "";
    private String lifeAssured_Last_Name = "";
    private String name_of_life_assured = "";
    private String lifeAssured_date_of_birth = "";
    private String annuity_plus_advance_annuity_payout_from_which_date = "";
    private String proposal_date = "";

    private String second_Annuitant_Proposer_Title = "";
    private CommonForAllProd commonForAllProd;
    private StringBuilder retVal;
    private StringBuilder inputVal;
    private AnnuityPlusProperties Prop;
    private AnnuityPlusBean annuityPlusBean;
    private Date idealDate;
    private long noOfDaysForIntCalculation = 0;
    private String proposer_Title = "";
    private String proposer_First_Name = "";
    private String proposer_Middle_Name = "";
    private String proposer_Last_Name = "";
    private String name_of_proposer = "";
    private String proposer_date_of_birth = "";
    private Bitmap photoBitmap;
    private String propsoser_gender = "";
    private String latestImage = "";
    private Button btn_MarketingOfficalDate;
    private Button btn_PolicyholderDate;
    private ImageButton Ibtn_signatureofMarketing;
    private ImageButton Ibtn_signatureofPolicyHolders;
    private final int SIGNATURE_ACTIVITY = 1;
    private String first_annuitant_age = "";
    private String second_annuitant_age = "";
    private String source_of_business = "";
    private String nameof_secondannuitant = "";
    private String annuity_option = "";
    private String mode_of_annuity_payout = "";
    private String annuity_amount = "";
    private String purchase_price = "";
    private String vesting_amount = "";
    private String opt_for = "";
    private String advance_premium_payable = "";
    private String total_premium = "";
    private String AnnuityOption = "";
    private String TitleSecondAnnuitant = "";
    private String gender = "";
    private String gender_2 = "";
    private String ModeOfAnnuityPayout = "";
    private String OptFor = "";

    private boolean val_annuity_payout_date = false;
    private boolean val_annuity_amount = false;
    private boolean flag = false;
    private DecimalFormat currencyFormat;
    private String isadvance_annuityPayout = "";
    private String annuity_payoutDate = "";
    private File mypath;

    private Spinner spnr_bi_annuity_plus_purchase_annuity_for;
    private Spinner spnr_bi_annuity_plus_immediate_annuity_plan_for;
    private Spinner selGender;
    private Spinner selFirstAnnutantGender;
    private Spinner selSecondAnnutantGender;
    private String str_annuity_plus_purchase_annuity_for = "";
    private String str_annuity_plus_immediate_annuity_plan_for = "";

    private EditText edt_bi_annuity_plus_proposer_first_name;
    private EditText edt_bi_annuity_plus_proposer_middle_name;
    private EditText edt_bi_annuity_plus_proposer_last_name;
    private TextView btn_bi_annuity_plus_proposer_date;
    private TextView tvProposerAge;
    private CheckBox cb_different_from_proposer;
    private Spinner spnr_bi_annuity_plus_proposer_title;
    private String proposer_age = "";
    private String Proposer_is_same_as_Annuitant = "N";
    private double StaffRebate = 0;
    private String annuity_amount_payable = "";
    private String modeOfAnnuityPayout = "";
    private String annuityAmtPayable = "";
    private String purchasePrice = "";
    private String totalPurchasePrice = "";
    private String policyTerm = "";
    private String riderSumAssured = "";
    private String riderPrem = "";
    private String riderPrem1 = "";
    private String totServiceTax = "";
    private String advancePremPayable = "";
    private String totalPrem = "";
    private String totalPremReq = "";
    private String addPremPayable = "";
    private String staffStatus = "";
    private String netPremium = "";
    private String basicServiceTax = "";
    private String SBC_serviceTax = "";
    private String KKC_serviceTax = "";
    private List<M_BI_Annuity_Plus_Adapter> list_data;
    private String result_grid = "";
    private String SumAssured = "";
    private String SumAssuredonDeath = "";
    private String SurvivalBenefits_annuity_plus_annuity_amount_payable = "";

    private Context context;
    private CommonMethods commonMethods;
    private StorageUtils mStorageUtils;
    private ImageButton imageButtonShubhNiveshProposerPhotograph;
    private CheckBox cb_kerladisc;
    private String agentcode, agentMobile, agentEmail, userType;
    private String na_input = null;
    private String na_output = null;
    private String bankUserType = "";
    private String mode = "";
    private NeedAnalysisBIService NABIObj;
    private NA_CBI_bean na_cbi_bean;
    private String Check = "";
    private File needAnalysispath, newFile;
    private String product_Code = "", product_UIN, product_cateogory, product_type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.bi_annuity_plusmain);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);

        dbHelper = new DatabaseHelper(this);
        context = this;
        commonMethods = new CommonMethods();
        mStorageUtils = new StorageUtils();
        UI_Declaration();
        commonMethods.setApplicationToolbarMenu(this, getString(R.string.app_name));
        Date();
        Intent intent = getIntent();
        prsObj = new ParseXML();
        setSpinner_Value();
        edt_bi_annuity_plus_life_assured_first_name.setOnEditorActionListener(this);
        edt_bi_annuity_plus_life_assured_middle_name.setOnEditorActionListener(this);
        edt_bi_annuity_plus_life_assured_last_name.setOnEditorActionListener(this);
        edt_bi_annuity_plus_life_assured_first_name_second_annuitant.setOnEditorActionListener(this);
        edt_bi_annuity_plus_life_assured_middle_name_second_annuitant.setOnEditorActionListener(this);
        edt_bi_annuity_plus_life_assured_last_name_second_annuitant.setOnEditorActionListener(this);
        edt_annuity_plus_contact_no.setOnEditorActionListener(this);
        edt_annuity_plus_Email_id.setOnEditorActionListener(this);
        edt_annuity_plus_ConfirmEmail_id.setOnEditorActionListener(this);
        edt_annuity_plus_additional_amount_if_any.setOnEditorActionListener(this);
        edt_annuity_plus_annuity_amount.setOnEditorActionListener(this);
        edt_annuity_plus_vesting_amount.setOnEditorActionListener(this);

        list_data = new ArrayList<M_BI_Annuity_Plus_Adapter>();

        validationOfMoile_EmailId();
        setSpinnerAndOtherListner();
        annuityPlusBean = new AnnuityPlusBean();
        Prop = new AnnuityPlusProperties();
        currencyFormat = new DecimalFormat("##,##,##,###");
        commonForAllProd = new CommonForAllProd();

        NABIObj = new NeedAnalysisBIService(this);
        prsObj = new ParseXML();

        //Added Tushar Kadam Kerla Applicable Tax 7-Jun-2019
        TableRow tablerowKerlaDiscount = findViewById(R.id.tablerowKerlaDiscount);
        cb_kerladisc = findViewById(R.id.cb_kerladisc);
        commonMethods.setKerlaDiscount(context, tablerowKerlaDiscount, cb_kerladisc);
        //End Added Tushar Kadam Kerla Applicable Tax 7-Jun-2019

        String na_flag = intent.getStringExtra("NAFlag");

        int needAnalysis_flag = 0;
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
                //URNNumber = intent.getStringExtra("URNNumber");


                try {
                    agentcode = SimpleCrypto.decrypt("SBIL", dbHelper.GetUserCode());

                    agentMobile = SimpleCrypto.decrypt("SBIL", dbHelper.GetMobileNo());
                    agentEmail = SimpleCrypto.decrypt("SBIL", dbHelper.GetEmailId());
                    userType = SimpleCrypto.decrypt("SBIL", dbHelper.GetUserType());

                    /*parivartan changes*/
                    ProductInfo prodInfoObj = new ProductInfo();
                    planName = "Annuity Plus";
                    product_Code = prodInfoObj.getProductCode(planName);
                    product_UIN = prodInfoObj.getProductUIN(planName);
                    product_cateogory = prodInfoObj.getProductCategory(planName);
                    product_type = prodInfoObj.getProductType(planName);
                    /*end*/
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                int k = 12 - (agentcode).length();
                StringBuilder zero = new StringBuilder();
                for (int i = 0; i < k; i++) {
                    zero = zero.append("0");
                }
                QuatationNumber = CommonForAllProd.getquotationNumber30(product_Code, agentcode,
                        zero + "");


            }
        } else
            needAnalysis_flag = 0;


    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

        boolean flagFocus = true;
        if (!flagFocus) {
            ll_bi_annuity_plus_main.requestFocus();
        } else {
            edt_bi_annuity_plus_proposer_first_name.requestFocus();
        }

    }


    private void UI_Declaration() {

        cb_staffdisc = findViewById(R.id.cb_ann_plus_staffdisc);
        cb_bi_annuity_plus_advance_annuity_payout = findViewById(R.id.cb_bi_annuity_plus_advance_annuity_payout);
        cb_bi_annuity_plus_adb_rider = findViewById(R.id.cb_bi_annuity_plus_adb_rider);

        edt_bi_annuity_plus_life_assured_first_name = findViewById(R.id.edt_bi_annuity_plus_life_assured_first_name);
        edt_bi_annuity_plus_life_assured_middle_name = findViewById(R.id.edt_bi_annuity_plus_life_assured_middle_name);
        edt_bi_annuity_plus_life_assured_last_name = findViewById(R.id.edt_bi_annuity_plus_life_assured_last_name);
        edt_bi_annuity_plus_life_assured_age = findViewById(R.id.edt_bi_annuity_plus_life_assured_age);

        edt_bi_annuity_plus_life_assured_first_name_second_annuitant = findViewById(R.id.edt_bi_annuity_plus_life_assured_first_name_second_annuitant);
        edt_bi_annuity_plus_life_assured_middle_name_second_annuitant = findViewById(R.id.edt_bi_annuity_plus_life_assured_middle_name_second_annuitant);
        edt_bi_annuity_plus_life_assured_last_name_second_annuitant = findViewById(R.id.edt_bi_annuity_plus_life_assured_last_name_second_annuitant);
        edt_bi_annuity_plus_life_assured_age_second_annuitant = findViewById(R.id.edt_bi_annuity_plus_life_assured_age_second_annuitant);

        edt_annuity_plus_contact_no = findViewById(R.id.edt_annuity_plus_contact_no);
        edt_annuity_plus_Email_id = findViewById(R.id.edt_annuity_plus_Email_id);
        edt_annuity_plus_ConfirmEmail_id = findViewById(R.id.edt_annuity_plus_ConfirmEmail_id);
        edt_annuity_plus_annuity_amount = findViewById(R.id.edt_annuity_plus_annuity_amount);
        edt_annuity_plus_vesting_amount = findViewById(R.id.edt_annuity_plus_vesting_amount);
        edt_annuity_plus_additional_amount_if_any = findViewById(R.id.edt_annuity_plus_additional_amount_if_any);

        spnr_bi_annuity_plus_life_assured_title = findViewById(R.id.spnr_bi_annuity_plus_life_assured_title);
        spnr_bi_annuity_plus_life_assured_title_second_annuitant = findViewById(R.id.spnr_bi_annuity_plus_life_assured_title_second_annuitant);
        spnr_bi_annuity_plus_source_of_business = findViewById(R.id.spnr_bi_annuity_plus_source_of_business);
        spnr_bi_annuity_plus_channel_details = findViewById(R.id.spnr_bi_annuity_plus_channel_details);
        spnr_bi_annuity_plus_mode_of_annuity_payouts = findViewById(R.id.spnr_bi_annuity_plus_mode_of_annuity_payouts);
        spnr_bi_annuity_plus_annuity_option = findViewById(R.id.spnr_bi_annuity_plus_annuity_option);
        spnr_bi_annuity_plus_opt_for = findViewById(R.id.spnr_bi_annuity_plus_opt_for);
        spnr_bi_annuity_plus_applicable_for = findViewById(R.id.spnr_bi_annuity_plus_applicable_for);

        btn_bi_annuity_plus_life_assured_date = findViewById(R.id.btn_bi_annuity_plus_life_assured_date);
        btn_bi_annuity_plus_life_assured_date_second_annuitant = findViewById(R.id.btn_bi_annuity_plus_life_assured_date_second_annuitant);
        btn_bi_annuity_plus_proposal_date = findViewById(R.id.btn_bi_annuity_plus_proposal_date);
        proposal_date = commonMethods.getMMDDYYYYDatabaseDate(getCurrentDate());
        btn_bi_annuity_plus_proposal_date.setText(getCurrentDate());
        btn_bi_annuity_plus_advance_annuity_payout_from_which_date = findViewById(R.id.btn_bi_annuity_plus_advance_annuity_payout_from_which_date);
        btnBack = findViewById(R.id.btn_bi_annuity_plus_btnback);
        btnSubmit = findViewById(R.id.btn_bi_annuity_plus_btnSubmit);

        tr_bi_annuity_plus_advance_annuity_payout = findViewById(R.id.tr_bi_annuity_plus_advance_annuity_payout);
        tr_checkbox_advance_annuity_payout = findViewById(R.id.tr_checkbox_advance_annuity_payout);
        tr_annuity_amount = findViewById(R.id.tr_annuity_amount);
        tr_vesting_amount = findViewById(R.id.tr_vesting_amount);
        tr_additional_amount_if_any = findViewById(R.id.tr_additional_amount_if_any);
        tr_bi_annuity_plus_adb_rider = findViewById(R.id.tr_bi_annuity_plus_adb_rider);
        linearlayoutSecondAnnuitant = findViewById(R.id.linearlayoutSecondAnnuitant);

        ll_bi_annuity_plus_main = findViewById(R.id.ll_bi_annuity_plus_main);


        selGender = findViewById(R.id.selGender);
        selFirstAnnutantGender = findViewById(R.id.selFirstAnnutantGender);
        selSecondAnnutantGender = findViewById(R.id.selSecondAnnutantGender);

        spnr_bi_annuity_plus_purchase_annuity_for = findViewById(R.id.spnr_bi_annuity_plus_purchase_annuity_for);
        spnr_bi_annuity_plus_immediate_annuity_plan_for = findViewById(R.id.spnr_bi_annuity_plus_immediate_annuity_plan_for);

        edt_bi_annuity_plus_proposer_first_name = findViewById(R.id.edt_bi_annuity_plus_proposer_first_name);
        edt_bi_annuity_plus_proposer_middle_name = findViewById(R.id.edt_bi_annuity_plus_proposer_middle_name);
        edt_bi_annuity_plus_proposer_last_name = findViewById(R.id.edt_bi_annuity_plus_proposer_last_name);


        btn_bi_annuity_plus_proposer_date = findViewById(R.id.btn_bi_annuity_plus_proposer_date);

        tvProposerAge = findViewById(R.id.tvProposerAge);

        cb_different_from_proposer = findViewById(R.id.cb_different_from_proposer);

        spnr_bi_annuity_plus_proposer_title = findViewById(R.id.spnr_bi_annuity_plus_proposer_title);

        cb_kerladisc = findViewById(R.id.cb_kerladisc);

    }


    private void setSpinner_Value() {


        commonMethods.fillSpinnerValue(context, spnr_bi_annuity_plus_life_assured_title,
                Arrays.asList(getResources().getStringArray(R.array.arr_select_title)));

        commonMethods.fillSpinnerValue(context, spnr_bi_annuity_plus_proposer_title,
                Arrays.asList(getResources().getStringArray(R.array.arr_select_title)));

        String[] genderList = {"Male", "Female", "Third Gender"};
        ArrayAdapter<String> genderAdapter = new ArrayAdapter<>(
                getApplicationContext(), R.layout.spinner_item, genderList);
        genderAdapter.setDropDownViewResource(R.layout.spinner_item1);

        selGender.setAdapter(genderAdapter);
        selFirstAnnutantGender.setAdapter(genderAdapter);
        selSecondAnnutantGender.setAdapter(genderAdapter);
        genderAdapter.notifyDataSetChanged();

        String[] SourceOfBusinessList = {"New Proposal"};
        ArrayAdapter<String> SourceOfBusinessAdapter = new ArrayAdapter<String>(
                getApplicationContext(), R.layout.spinner_item, SourceOfBusinessList);
        SourceOfBusinessAdapter.setDropDownViewResource(R.layout.spinner_item1);
        spnr_bi_annuity_plus_source_of_business.setAdapter(SourceOfBusinessAdapter);
        SourceOfBusinessAdapter.notifyDataSetChanged();

        // Channel Details
        String[] channelDetailList = {"Retail Agency", "Bancaassurance", "Broking",
                "Corporate Agency", "Corporate Solution", "Direct", "Others"};
        ArrayAdapter<String> channelDetailsAdapter = new ArrayAdapter<String>(
                getApplicationContext(), R.layout.spinner_item, channelDetailList);
        channelDetailsAdapter.setDropDownViewResource(R.layout.spinner_item1);
        spnr_bi_annuity_plus_channel_details.setAdapter(channelDetailsAdapter);
        channelDetailsAdapter.notifyDataSetChanged();

        // Mode of annuity Payouts
        String[] modeOfAnnuityPayoutsList = {"Select", "Monthly", "Quarterly", "Half Yearly", "Yearly"};
        ArrayAdapter<String> modeOfAnnuityPayoutsAdapter = new ArrayAdapter<String>(
                getApplicationContext(), R.layout.spinner_item, modeOfAnnuityPayoutsList);
        modeOfAnnuityPayoutsAdapter.setDropDownViewResource(R.layout.spinner_item1);
        spnr_bi_annuity_plus_mode_of_annuity_payouts.setAdapter(modeOfAnnuityPayoutsAdapter);
        modeOfAnnuityPayoutsAdapter.notifyDataSetChanged();

        // Annuity Option
       /* String[] AnnuityOptionList = {"Select", "Lifetime Income", "Lifetime Income with Capital Refund",
                "Lifetime Income with Capital Refund in Parts",
                "Lifetime Income with Balance Capital Refund", "Lifetime Income with Annual Increase of 3%",
                "Lifetime Income with Annual Increase of 5%", "Lifetime Income with Certain Period of 5 Years",
                "Lifetime Income with Certain Period of 10 Years", "Lifetime Income with Certain Period of 15 Years",
                "Lifetime Income with Certain Period of 20 Years", "Life and Last Survivor - 50% Income",
                "Life and Last Survivor - 100% Income", "Life and Last Survivor with Capital Refund - 50% Income",
                "Life and Last Survivor with Capital Refund - 100% Income"};
        ArrayAdapter<String> AnnuityOptionAdapter = new ArrayAdapter<String>(
                getApplicationContext(), R.layout.spinner_item, AnnuityOptionList);

        AnnuityOptionAdapter.setDropDownViewResource(R.layout.spinner_item1);
        spnr_bi_annuity_plus_annuity_option.setAdapter(AnnuityOptionAdapter);
        AnnuityOptionAdapter.notifyDataSetChanged();*/

        spnr_bi_annuity_plus_annuity_option.setAdapter(ArrayAdapter.createFromResource(this,
                R.array.annuityPlusOptionArray,
                R.layout.spinner_large_words));

        // Applicable For
        String[] ApplicableForList = {"First Annuitant"};
        ArrayAdapter<String> ApplicableForAdapter = new ArrayAdapter<String>(
                getApplicationContext(), R.layout.spinner_item, ApplicableForList);
        ApplicableForAdapter.setDropDownViewResource(R.layout.spinner_item1);
        spnr_bi_annuity_plus_applicable_for.setAdapter(ApplicableForAdapter);
        ApplicableForAdapter.notifyDataSetChanged();


        List<String> title_second_annuitant_list = new ArrayList<String>();
        title_second_annuitant_list.add("Select Title");
        title_second_annuitant_list.add("Mr.");
        title_second_annuitant_list.add("Ms.");
        title_second_annuitant_list.add("Mrs.");
        commonMethods.fillSpinnerValue(context, spnr_bi_annuity_plus_life_assured_title_second_annuitant, title_second_annuitant_list);

        // Opt For
        String[] optForList = {"Select", "Annuity Payout Amount", "Premium Amount"};
        ArrayAdapter<String> optForAdapter = new ArrayAdapter<String>(
                getApplicationContext(), R.layout.spinner_item, optForList);
        optForAdapter.setDropDownViewResource(R.layout.spinner_item1);
        spnr_bi_annuity_plus_opt_for.setAdapter(optForAdapter);
        optForAdapter.notifyDataSetChanged();

        String[] purchaseAnnuityList = {"Select", "Single Life", "Two Lives"};
        ArrayAdapter<String> purchaseAnnuityAdapter = new ArrayAdapter<String>
                (getApplicationContext(), R.layout.spinner_item, purchaseAnnuityList);
        purchaseAnnuityAdapter.setDropDownViewResource(R.layout.spinner_item1);
        spnr_bi_annuity_plus_purchase_annuity_for.setAdapter(purchaseAnnuityAdapter);
        purchaseAnnuityAdapter.notifyDataSetChanged();

        String[] immediateAnnuityPlanList = {"Select", "Self", "Another Individual"};
        ArrayAdapter<String> immediateAnnuityPlanAdapter = new ArrayAdapter<String>
                (getApplicationContext(), R.layout.spinner_item, immediateAnnuityPlanList);
        immediateAnnuityPlanAdapter.setDropDownViewResource(R.layout.spinner_item1);
        spnr_bi_annuity_plus_immediate_annuity_plan_for.setAdapter(immediateAnnuityPlanAdapter);
        immediateAnnuityPlanAdapter.notifyDataSetChanged();


    }

    private void validationOfMoile_EmailId() {

        edt_annuity_plus_contact_no
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
                        String abc = edt_annuity_plus_contact_no
                                .getText().toString();
                        mobile_validation(abc);
                    }
                });

        edt_annuity_plus_Email_id
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
                        ProposerEmailId = edt_annuity_plus_Email_id
                                .getText().toString();
                        email_id_validation(ProposerEmailId);

                    }
                });

        edt_annuity_plus_ConfirmEmail_id
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
                        String proposer_confirm_emailId = edt_annuity_plus_ConfirmEmail_id
                                .getText().toString();
                        confirming_email_id(proposer_confirm_emailId);

                    }
                });

    }

    private void setSpinnerAndOtherListner() {


        cb_kerladisc.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cb_kerladisc.isChecked()) {
                    commonMethods.clearFocusable(cb_kerladisc);
                    commonMethods.setFocusable(spnr_bi_annuity_plus_life_assured_title);
                    spnr_bi_annuity_plus_life_assured_title.requestFocus();
                } else {
                    commonMethods.clearFocusable(cb_kerladisc);
                    commonMethods.setFocusable(spnr_bi_annuity_plus_life_assured_title);
                    spnr_bi_annuity_plus_life_assured_title.requestFocus();
                }
            }
        });

        cb_different_from_proposer.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cb_different_from_proposer.isChecked()) {
                    String str_proposerAge;
                    str_proposerAge = tvProposerAge.getText().toString();
                    propsoser_gender = selGender.getSelectedItem().toString();
                    if (str_proposerAge != null && !str_proposerAge.equals("")) {
                        if ((!edt_bi_annuity_plus_proposer_first_name.getText().toString().trim().equals(""))
                                && (!edt_bi_annuity_plus_proposer_first_name.getText().toString().trim().equals(""))
                                && !proposer_Title.equals("") && !propsoser_gender.equals("") && val_proposer_dob()) {
                            if ((Integer.parseInt(str_proposerAge)) >= 40 && (Integer.parseInt(str_proposerAge)) <= 80) {
                                Proposer_is_same_as_Annuitant = "Y";
                                spnr_bi_annuity_plus_life_assured_title.setSelection(
                                        getIndex(spnr_bi_annuity_plus_life_assured_title,
                                                proposer_Title), false);

                                edt_bi_annuity_plus_life_assured_first_name.setText(edt_bi_annuity_plus_proposer_first_name.getText().toString());
                                edt_bi_annuity_plus_life_assured_middle_name.setText(edt_bi_annuity_plus_proposer_middle_name.getText().toString());
                                edt_bi_annuity_plus_life_assured_last_name.setText(edt_bi_annuity_plus_proposer_last_name.getText().toString());


                                if (propsoser_gender.equalsIgnoreCase("Male")) {
                                    gender = "Male";
                                    selFirstAnnutantGender.setSelection(0, false);
                                }
                                if (propsoser_gender.equalsIgnoreCase("Female")) {
                                    gender = "Female";
                                    selFirstAnnutantGender.setSelection(1, false);
                                }
                                if (propsoser_gender.equalsIgnoreCase("Third Gender")) {
                                    gender = "Third Gender";
                                    selFirstAnnutantGender.setSelection(2, false);
                                }

                                btn_bi_annuity_plus_life_assured_date.setText(btn_bi_annuity_plus_proposer_date.getText().toString());
                                lifeAssured_date_of_birth = proposer_date_of_birth;
                                edt_bi_annuity_plus_life_assured_age.setText(proposer_age);

                                spnr_bi_annuity_plus_life_assured_title.setClickable(false);
                                edt_bi_annuity_plus_life_assured_first_name.setClickable(false);
                                edt_bi_annuity_plus_life_assured_middle_name.setClickable(false);
                                edt_bi_annuity_plus_life_assured_last_name.setClickable(false);
                                btn_bi_annuity_plus_life_assured_date.setClickable(false);

                                spnr_bi_annuity_plus_life_assured_title.setEnabled(false);
                                edt_bi_annuity_plus_life_assured_first_name.setEnabled(false);
                                edt_bi_annuity_plus_life_assured_middle_name.setEnabled(false);
                                edt_bi_annuity_plus_life_assured_last_name.setEnabled(false);
                                btn_bi_annuity_plus_life_assured_date.setEnabled(false);
                            } else {
                                cb_different_from_proposer.setChecked(false);
                                commonMethods.showMessageDialog(context, "Minimum Age of First Annitant should be 40 and Maximum Age of First Annitant should be 80");
                                commonMethods.setFocusable(btn_bi_annuity_plus_life_assured_date);
                                btn_bi_annuity_plus_life_assured_date
                                        .requestFocus();
                            }

                        } else {
                            cb_different_from_proposer.setChecked(false);
                            commonMethods.showMessageDialog(context, "Please Fill Name Detail and Gender For Proposer");
                            if (proposer_Title.equals("")) {
                                // apply focusable method
                                commonMethods.setFocusable(spnr_bi_annuity_plus_proposer_title);
                                spnr_bi_annuity_plus_life_assured_title
                                        .requestFocus();
                            } else if (proposer_First_Name.equals("")) {
                                commonMethods.setFocusable(edt_bi_annuity_plus_proposer_first_name);
                                edt_bi_annuity_plus_proposer_first_name
                                        .requestFocus();
                            } else {
                                commonMethods.setFocusable(edt_bi_annuity_plus_proposer_last_name);
                                edt_bi_annuity_plus_proposer_last_name
                                        .requestFocus();
                            }
                        }
                    } else {
                        cb_different_from_proposer.setChecked(false);
                        commonMethods.showMessageDialog(context, "Please Fill Name Detail For Proposer");
                        if (proposer_Title.equals("")) {
                            // apply focusable method
                            commonMethods.setFocusable(spnr_bi_annuity_plus_proposer_title);
                            spnr_bi_annuity_plus_life_assured_title
                                    .requestFocus();
                        } else if (proposer_First_Name.equals("")) {
                            commonMethods.setFocusable(edt_bi_annuity_plus_proposer_first_name);
                            edt_bi_annuity_plus_proposer_first_name
                                    .requestFocus();
                        } else if (propsoser_gender.equals("")) {
                            commonMethods.setFocusable(selGender);
                            selGender
                                    .requestFocus();
                        } else {
                            commonMethods.setFocusable(edt_bi_annuity_plus_proposer_last_name);
                            edt_bi_annuity_plus_proposer_last_name
                                    .requestFocus();
                        }
                    }

                } else if (!cb_different_from_proposer.isChecked()) {
                    Proposer_is_same_as_Annuitant = "N";
                    edt_bi_annuity_plus_life_assured_first_name.setText("");
                    edt_bi_annuity_plus_life_assured_middle_name.setText("");
                    edt_bi_annuity_plus_life_assured_last_name.setText("");
                    lifeAssured_First_Name = "";
                    lifeAssured_Middle_Name = "";
                    lifeAssured_Last_Name = "";

                    spnr_bi_annuity_plus_life_assured_title.setSelection(
                            getIndex(spnr_bi_annuity_plus_life_assured_title,
                                    "Select Title"), false);
                    lifeAssured_Title = "";
                    gender = "";

                    btn_bi_annuity_plus_life_assured_date.setText("Select Date");
                    lifeAssured_date_of_birth = "";
                    edt_bi_annuity_plus_life_assured_age.setText("");


                    spnr_bi_annuity_plus_life_assured_title.setClickable(true);
                    edt_bi_annuity_plus_life_assured_first_name.setClickable(true);
                    edt_bi_annuity_plus_life_assured_middle_name.setClickable(true);
                    edt_bi_annuity_plus_life_assured_last_name.setClickable(true);
                    btn_bi_annuity_plus_life_assured_date.setClickable(true);

                    spnr_bi_annuity_plus_life_assured_title.setEnabled(true);
                    edt_bi_annuity_plus_life_assured_first_name.setEnabled(true);
                    edt_bi_annuity_plus_life_assured_middle_name.setEnabled(true);
                    edt_bi_annuity_plus_life_assured_last_name.setEnabled(true);
                    btn_bi_annuity_plus_life_assured_date.setEnabled(true);
                }
            }
        });


        //Spinner


        spnr_bi_annuity_plus_proposer_title
                .setOnItemSelectedListener(new OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int position, long id) {
                        // TODO Auto-generated method stub
                        if (position > 0) {
                            proposer_Title = spnr_bi_annuity_plus_proposer_title
                                    .getSelectedItem().toString();
                            commonMethods.clearFocusable(spnr_bi_annuity_plus_proposer_title);
                            commonMethods.setFocusable(edt_bi_annuity_plus_proposer_first_name);
                            edt_bi_annuity_plus_proposer_first_name.requestFocus();
                        } else {
                            proposer_Title = "";
                            if (cb_different_from_proposer.isChecked()) {
                                cb_different_from_proposer.setChecked(false);
                                Proposer_is_same_as_Annuitant = "N";
                                edt_bi_annuity_plus_life_assured_first_name.setText("");
                                edt_bi_annuity_plus_life_assured_middle_name.setText("");
                                edt_bi_annuity_plus_life_assured_last_name.setText("");
                                lifeAssured_First_Name = "";
                                lifeAssured_Middle_Name = "";
                                lifeAssured_Last_Name = "";


                                spnr_bi_annuity_plus_life_assured_title.setSelection(
                                        getIndex(spnr_bi_annuity_plus_life_assured_title,
                                                "Select Title"), false);
                                lifeAssured_Title = "";
                                gender = "";

                                btn_bi_annuity_plus_life_assured_date.setText("Select Date");
                                lifeAssured_date_of_birth = "";
                                edt_bi_annuity_plus_life_assured_age.setText("");


                                spnr_bi_annuity_plus_life_assured_title.setClickable(true);
                                edt_bi_annuity_plus_life_assured_first_name.setClickable(true);
                                edt_bi_annuity_plus_life_assured_middle_name.setClickable(true);
                                edt_bi_annuity_plus_life_assured_last_name.setClickable(true);
                                btn_bi_annuity_plus_life_assured_date.setClickable(true);

                                spnr_bi_annuity_plus_life_assured_title.setEnabled(true);
                                edt_bi_annuity_plus_life_assured_first_name.setEnabled(true);
                                edt_bi_annuity_plus_life_assured_middle_name.setEnabled(true);
                                edt_bi_annuity_plus_life_assured_last_name.setEnabled(true);
                                btn_bi_annuity_plus_life_assured_date.setEnabled(true);
                            }
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                        // TODO Auto-generated method stub

                    }
                });

        spnr_bi_annuity_plus_life_assured_title
                .setOnItemSelectedListener(new OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int position, long id) {
                        // TODO Auto-generated method stub
                        if (position > 0) {
                            lifeAssured_Title = spnr_bi_annuity_plus_life_assured_title
                                    .getSelectedItem().toString();
                            commonMethods.clearFocusable(spnr_bi_annuity_plus_life_assured_title);
                            commonMethods.setFocusable(edt_bi_annuity_plus_life_assured_first_name);
                            edt_bi_annuity_plus_life_assured_first_name.requestFocus();
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                        // TODO Auto-generated method stub

                    }
                });


        spnr_bi_annuity_plus_life_assured_title_second_annuitant
                .setOnItemSelectedListener(new OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int position, long id) {
                        // TODO Auto-generated method stub
                        if (position > 0) {
                            second_Annuitant_Proposer_Title = spnr_bi_annuity_plus_life_assured_title_second_annuitant
                                    .getSelectedItem().toString();
                            commonMethods.clearFocusable(spnr_bi_annuity_plus_life_assured_title_second_annuitant);
                            commonMethods.setFocusable(edt_bi_annuity_plus_life_assured_first_name_second_annuitant);
                            edt_bi_annuity_plus_life_assured_first_name_second_annuitant.requestFocus();
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                        // TODO Auto-generated method stub

                    }
                });

        //ADB Rider
//    			cb_bi_annuity_plus_adb_rider.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//    				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
//    				{
//    					if ( isChecked == true && valRider())
//    					{
//
//    						tr_bi_annuity_plus_adb_rider.setVisibility(View.VISIBLE);
//    					}
//    					else
//    					{
//    						tr_bi_annuity_plus_adb_rider.setVisibility(View.GONE);
//    					}
//    				}
//    			});


        cb_bi_annuity_plus_adb_rider.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (cb_bi_annuity_plus_adb_rider.isChecked()) {
                    cb_bi_annuity_plus_adb_rider.setChecked(true);
                    if (valRider()) {
                        tr_bi_annuity_plus_adb_rider.setVisibility(View.VISIBLE);

                        if (spnr_bi_annuity_plus_annuity_option.getSelectedItem().toString().equals("Life and Last Survivor - 50% Income")
                                || spnr_bi_annuity_plus_annuity_option.getSelectedItem().toString().equals("Life and Last Survivor - 100% Income")
                                || spnr_bi_annuity_plus_annuity_option.getSelectedItem().toString().equals("Life and Last Survivor with Capital Refund - 50% Income")
                                || spnr_bi_annuity_plus_annuity_option.getSelectedItem().toString().equals("Life and Last Survivor with Capital Refund - 100% Income")) {
                            if (Integer.parseInt(edt_bi_annuity_plus_life_assured_age.getText().toString()) < Prop.maxAgeOfAnnuitantWhenRider
                                    && Integer.parseInt(edt_bi_annuity_plus_life_assured_age_second_annuitant.getText().toString()) > Prop.maxAgeOfAnnuitantWhenRider) {
                                String[] applicableForList = {"First Annuitant"};
                                ArrayAdapter<String> applicableForAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, applicableForList);
                                applicableForAdapter.setDropDownViewResource(R.layout.spinner_item1);
                                spnr_bi_annuity_plus_applicable_for.setAdapter(applicableForAdapter);
                                applicableForAdapter.notifyDataSetChanged();
                            } else if (Integer.parseInt(edt_bi_annuity_plus_life_assured_age.getText().toString()) < Prop.maxAgeOfAnnuitantWhenRider
                                    && Integer.parseInt(edt_bi_annuity_plus_life_assured_age_second_annuitant.getText().toString()) < Prop.maxAgeOfAnnuitantWhenRider) {
                                String[] applicableForList = {"First Annuitant", "Both Annuitant"};
                                ArrayAdapter<String> applicableForAdapter = new ArrayAdapter<String>(
                                        getApplicationContext(), R.layout.spinner_item, applicableForList);
                                applicableForAdapter.setDropDownViewResource(R.layout.spinner_item1);
                                spnr_bi_annuity_plus_applicable_for.setAdapter(applicableForAdapter);
                                applicableForAdapter.notifyDataSetChanged();
                            }
                        } else {
                            String[] applicableForList = {"First Annuitant"};
                            ArrayAdapter<String> applicableForAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, applicableForList);
                            applicableForAdapter.setDropDownViewResource(R.layout.spinner_item1);
                            spnr_bi_annuity_plus_applicable_for.setAdapter(applicableForAdapter);
                            applicableForAdapter.notifyDataSetChanged();
                        }


                    } else {
                        cb_bi_annuity_plus_adb_rider.setChecked(false);
                        tr_bi_annuity_plus_adb_rider.setVisibility(View.GONE);
                    }

                } else {
                    cb_bi_annuity_plus_adb_rider.setChecked(false);
                    tr_bi_annuity_plus_adb_rider.setVisibility(View.GONE);
                }
            }
        });

        // Modes of annuity payouts
        spnr_bi_annuity_plus_mode_of_annuity_payouts.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int position, long arg3) {
                if (position > 0) {
                    ModeOfAnnuityPayout = spnr_bi_annuity_plus_mode_of_annuity_payouts.getSelectedItem().toString();
                    // TODO Auto-generated method stub
                    if (ModeOfAnnuityPayout.equals("Yearly") || ModeOfAnnuityPayout.equals("Half Yearly")) {

                        tr_checkbox_advance_annuity_payout.setVisibility(View.VISIBLE);
                        //tr_bi_annuity_plus_advance_annuity_payout.setVisibility(View.VISIBLE);
                    } else {
                        tr_checkbox_advance_annuity_payout.setVisibility(View.GONE);
                        tr_bi_annuity_plus_advance_annuity_payout.setVisibility(View.GONE);
                        annuity_plus_advance_annuity_payout_from_which_date = "";


                    }
                    commonMethods.clearFocusable(spnr_bi_annuity_plus_mode_of_annuity_payouts);
                    commonMethods.setFocusable(spnr_bi_annuity_plus_annuity_option);
                    spnr_bi_annuity_plus_annuity_option
                            .requestFocus();
                } else {
                    ModeOfAnnuityPayout = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });

        //Advance annuity payout
        cb_bi_annuity_plus_advance_annuity_payout.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //	txtAdvAnnPayoutDate.setVisibility(View.VISIBLE);
                    tr_bi_annuity_plus_advance_annuity_payout.setVisibility(View.VISIBLE);
                } else {
                    //		txtAdvAnnPayoutDate.setVisibility(View.GONE);
                    tr_bi_annuity_plus_advance_annuity_payout.setVisibility(View.GONE);
                    annuity_plus_advance_annuity_payout_from_which_date = "";
                    btn_bi_annuity_plus_advance_annuity_payout_from_which_date.setText("Select Date");
                }
            }
        });


        //Annuity Option
        spnr_bi_annuity_plus_annuity_option.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int position, long arg3) {
                // TODO Auto-generated method stub


                if (position > 0) {
                    AnnuityOption = spnr_bi_annuity_plus_annuity_option.getSelectedItem().toString();
                    updateField_FirstOrBothAnnuitant();
                    addOrRemoveSecAnnuitantFields();

                    if (is_Second_Annuitant.equalsIgnoreCase("Y")) {
                        commonMethods.clearFocusable(spnr_bi_annuity_plus_annuity_option);
                        commonMethods.setFocusable(spnr_bi_annuity_plus_life_assured_title_second_annuitant);
                        spnr_bi_annuity_plus_life_assured_title_second_annuitant
                                .requestFocus();
                    } else {
                        commonMethods.clearFocusable(spnr_bi_annuity_plus_annuity_option);
                        commonMethods.setFocusable(btn_bi_annuity_plus_proposal_date);
                        btn_bi_annuity_plus_proposal_date
                                .requestFocus();
                    }
                } else {
                    AnnuityOption = "";
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });

        //Source of Business
        spnr_bi_annuity_plus_source_of_business.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                // TODO Auto-generated method stub

                deleteAndAddFieldsUnderOptForField();
                commonMethods.clearFocusable(spnr_bi_annuity_plus_source_of_business);
                commonMethods.setFocusable(spnr_bi_annuity_plus_channel_details);
                spnr_bi_annuity_plus_channel_details
                        .requestFocus();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });


        //Source of Business
        spnr_bi_annuity_plus_channel_details.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                // TODO Auto-generated method stub

                deleteAndAddFieldsUnderOptForField();
                commonMethods.clearFocusable(spnr_bi_annuity_plus_channel_details);
                commonMethods.setFocusable(spnr_bi_annuity_plus_mode_of_annuity_payouts);
                spnr_bi_annuity_plus_mode_of_annuity_payouts
                        .requestFocus();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });

        //Opt for
        spnr_bi_annuity_plus_opt_for.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int postion, long arg3) {
                // TODO Auto-generated method stub
                if (postion > 0) {
                    OptFor = spnr_bi_annuity_plus_opt_for.getSelectedItem().toString();
                    deleteAndAddFieldsUnderOptForField();


                    if (OptFor.equalsIgnoreCase("Annuity Payout Amount")) {
                        edt_annuity_plus_annuity_amount.requestFocus();
                        edt_annuity_plus_vesting_amount.setText("");
                    }

                } else {
                    OptFor = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });


        spnr_bi_annuity_plus_purchase_annuity_for.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    str_annuity_plus_purchase_annuity_for = spnr_bi_annuity_plus_purchase_annuity_for.getSelectedItem().toString();
                } else {
                    str_annuity_plus_purchase_annuity_for = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spnr_bi_annuity_plus_immediate_annuity_plan_for.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    str_annuity_plus_immediate_annuity_plan_for = spnr_bi_annuity_plus_immediate_annuity_plan_for.getSelectedItem().toString();
                } else {
                    str_annuity_plus_immediate_annuity_plan_for = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        //First Annuitant Age
        edt_bi_annuity_plus_life_assured_age.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View arg0, boolean arg1) {
                // TODO Auto-generated method stub

                if (!(edt_bi_annuity_plus_life_assured_age.getText().toString().equals(""))) {

                    valRider();
                }

            }
        });


        //Second Annuitant Age
        edt_bi_annuity_plus_life_assured_age_second_annuitant.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View arg0, boolean arg1) {
                // TODO Auto-generated method stub

                if (!(edt_bi_annuity_plus_life_assured_age.getText().toString().equals(""))) {

                    valRider();
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

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                inputVal = new StringBuilder();
                retVal = new StringBuilder();

                proposer_First_Name = edt_bi_annuity_plus_proposer_first_name.getText().toString().trim();
                proposer_Middle_Name = edt_bi_annuity_plus_proposer_middle_name.getText().toString().trim();
                proposer_Last_Name = edt_bi_annuity_plus_proposer_last_name.getText().toString().trim();

                name_of_proposer = proposer_Title + " " + proposer_First_Name + " " + proposer_Middle_Name + " " + proposer_Last_Name;

                propsoser_gender = selGender.getSelectedItem().toString();
                gender = selFirstAnnutantGender.getSelectedItem().toString();
                gender_2 = selSecondAnnutantGender.getSelectedItem().toString();

                lifeAssured_First_Name = edt_bi_annuity_plus_life_assured_first_name
                        .getText().toString().trim();
                lifeAssured_Middle_Name = edt_bi_annuity_plus_life_assured_middle_name
                        .getText().toString().trim();
                lifeAssured_Last_Name = edt_bi_annuity_plus_life_assured_last_name
                        .getText().toString().trim();

                name_of_life_assured = lifeAssured_Title + " "
                        + lifeAssured_First_Name + " "
                        + lifeAssured_Middle_Name + " " + lifeAssured_Last_Name;


                second_Annuitant_First_Name = edt_bi_annuity_plus_life_assured_first_name_second_annuitant
                        .getText().toString().trim();
                second_Annuitant_Middle_Name = edt_bi_annuity_plus_life_assured_middle_name_second_annuitant
                        .getText().toString().trim();
                second_Annuitant_Last_Name = edt_bi_annuity_plus_life_assured_last_name_second_annuitant
                        .getText().toString().trim();

                name_of_second_annuitant = second_Annuitant_Proposer_Title + " " + second_Annuitant_First_Name
                        + " " + second_Annuitant_Middle_Name + " " + second_Annuitant_Last_Name;

                mobileNo = edt_annuity_plus_contact_no.getText()
                        .toString();
                emailId = edt_annuity_plus_Email_id.getText()
                        .toString();
                ConfirmEmailId = edt_annuity_plus_ConfirmEmail_id
                        .getText().toString();

                if (valProposerDetail() && valLifeAssuredProposerDetail() && valSecondAnnuitantDetails() && val_proposer_dob()
                        && valSpinner() && val_first_Annuitant() && val_second_annuitant() &&
                        val_advance_annuity_payout() && val_proposal_date() && valBasicDetail() && valInputScreen() && valJointLifeAnnityOptionDob()) {
                    addListenerOnSubmit_New();
                }

            }
        });

    }


    private void Dialog() {

        final Dialog d = new Dialog(this);
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);
        d.getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.BLACK));
        d.setContentView(R.layout.layout_annuity_plus_bi_grid);

        TextView tv_bi_annuity_plus_proposer_name = d
                .findViewById(R.id.tv_bi_annuity_plus_proposer_name);

        TextView tv_gst_rate = d
                .findViewById(R.id.tv_gst_rate);
        /*if (cb_kerladisc.isChecked()) {
            tv_gst_rate.setText("1.9 %");
        }*/


        TextView tv_bi_annuity_plus_first_annuitant_name = d
                .findViewById(R.id.tv_bi_annuity_plus_first_annuitant_name);
        TextView tv_bi_annuity_plus_second_annuitant_name = d
                .findViewById(R.id.tv_bi_annuity_plus_second_annuitant_name);
        TextView
                tv_amount_installment_premium = d
                .findViewById(R.id.
                        tv_amount_installment_premium);

        TextView tv_sum_assured_on_death = d
                .findViewById(R.id.tv_sum_assured_on_death);


        TextView tv_bi_annuity_plus_loan_outstanding_loan_amount = d
                .findViewById(R.id.tv_bi_annuity_plus_loan_outstanding_loan_amount);

        TextView tv_source_of_funds = d
                .findViewById(R.id.tv_source_of_funds);


        TextView tv_base_plan_premium_without_tax = d
                .findViewById(R.id.tv_base_plan_premium_without_tax);
        TextView tv_interest_advance_annuity_without_tax = d
                .findViewById(R.id.tv_interest_advance_annuity_without_tax);
        TextView tv_total_installment_premium_without_tax = d
                .findViewById(R.id.tv_total_installment_premium_without_tax);

        TextView tv_base_plan_premium_first_year_with_tax = d
                .findViewById(R.id.tv_base_plan_premium_first_year_with_tax);
        TextView tv_interest_advance_annuity_first_year_with_tax = d
                .findViewById(R.id.tv_interest_advance_annuity_first_year_with_tax);
        TextView tv_total_installment_premium_first_year_with_tax = d
                .findViewById(R.id.tv_total_installment_premium_first_year_with_tax);

        TextView tv_base_plan_premium_second_year_with_tax = d
                .findViewById(R.id.tv_base_plan_premium_second_year_with_tax);
        TextView tv_interest_advance_annuity_second_year_with_tax = d
                .findViewById(R.id.tv_interest_advance_annuity_second_year_with_tax);
        TextView tv_total_installment_premium_second_year_with_tax = d
                .findViewById(R.id.tv_total_installment_premium_second_year_with_tax);


        TextView tv_proposername = d
                .findViewById(R.id.tv_annuity_plus_proposername);
        TextView tv_proposal_number = d
                .findViewById(R.id.tv_annuity_plus_proposal_number);
        TextView tv_bi_is_Staff = d
                .findViewById(R.id.tv_bi_is_Staff);
        TextView tv_bi_is_jk = d
                .findViewById(R.id.tv_bi_is_jk);


        TextView tv_bi_annuity_plus_proposer_age = d.findViewById(R.id.tv_bi_annuity_plus_proposer_age);
        TextView tv_bi_annuity_plus_first_annuitant_age = d
                .findViewById(R.id.tv_bi_annuity_plus_first_annuitant_age);
        TextView tv_bi_annuity_plus_second_annuitant_age = d
                .findViewById(R.id.tv_bi_annuity_plus_second_annuitant_age);
        TextView tv_bi_annuity_plus_source_of_business = d
                .findViewById(R.id.tv_bi_annuity_plus_source_of_business);
        TextView tv_bi_annuity_plus_annuity_option_opted = d
                .findViewById(R.id.tv_bi_annuity_plus_annuity_option_opted);
        TextView tv_bi_annuity_plus_mode_of_annuity_payout = d
                .findViewById(R.id.tv_bi_annuity_plus_mode_of_annuity_payout);
        TextView tv_bi_annuity_plus_annuity_amount_payable = d
                .findViewById(R.id.tv_bi_annuity_plus_annuity_amount_payable);
        TextView tv_bi_annuity_plus_annuity_amount_start_date = d
                .findViewById(R.id.tv_bi_annuity_plus_annuity_amount_start_date);
        TextView tv_bi_annuity_plus_purchase_price = d
                .findViewById(R.id.tv_bi_annuity_plus_purchase_price);
        TextView tv_bi_annuity_plus_total_premium_required = d
                .findViewById(R.id.tv_bi_annuity_plus_total_premium_required);
        TextView tv_bi_annuity_plus_rider_sum_assured = d
                .findViewById(R.id.tv_bi_annuity_plus_rider_sum_assured);
        TextView tv_bi_annuity_plus_rider_premium = d
                .findViewById(R.id.tv_bi_annuity_plus_rider_premium);
        TextView tv_bi_annuity_plus_vesting_amount = d
                .findViewById(R.id.tv_bi_annuity_plus_vesting_amount);
        TextView tv_bi_annuity_plus_total_service_tax = d
                .findViewById(R.id.tv_bi_annuity_plus_total_service_tax);
        TextView tv_bi_annuity_plus_advance_premium_payable = d
                .findViewById(R.id.tv_bi_annuity_plus_advance_premium_payable);
        TextView tv_bi_annuity_plus_additional_premium_payable = d
                .findViewById(R.id.tv_bi_annuity_plus_additional_premium_payable);
        TextView tv_bi_annuity_plus_total_premium = d
                .findViewById(R.id.tv_bi_annuity_plus_total_premium);
        GridView gv_userinfo = d
                .findViewById(R.id.gv_annuity_plus_userinfo);

        //staff discount
        TableRow tr_bi_annuity_plus_rider = d
                .findViewById(R.id.tr_bi_annuity_plus_rider);
        final EditText edt_Policyholderplace = d
                .findViewById(R.id.edt_Policyholderplace);
        final TextView txt_annuity_plus_proposer_name = d
                .findViewById(R.id.txt_annuity_plus_proposer_name);
        final CheckBox cb_statement = d
                .findViewById(R.id.cb_annuity_plus_statement);


        /* Need Analysis */
        final TextView txt_proposer_name_need_analysis = d
                .findViewById(R.id.txt_proposer_name_need_analysis);

        final CheckBox cb_statement_need_analysis = d
                .findViewById(R.id.cb_statement_need_analysis);
        cb_statement_need_analysis.setChecked(true);
        TableRow tr_need_analysis = d
                .findViewById(R.id.tr_need_analysis);

        final CheckBox checkboxAgentStatement = d.findViewById(R.id.checkboxAgentStatement);
        checkboxAgentStatement.setChecked(false);

        if (NeedAnalysisActivity.str_need_analysis != null) {
            if (NeedAnalysisActivity.str_need_analysis.equals("1")) {


                flg_needAnalyis = "1";
                tr_need_analysis.setVisibility(View.VISIBLE);
            } else {
                cb_statement_need_analysis.setChecked(true);
                tr_need_analysis.setVisibility(View.GONE);
            }

        }

        Button btn_proceed = d
                .findViewById(R.id.btn_annuity_plus_proceed);

        btn_MarketingOfficalDate = d
                .findViewById(R.id.btn_MarketingOfficalDate);
        btn_PolicyholderDate = d
                .findViewById(R.id.btn_PolicyholderDate);

        Ibtn_signatureofMarketing = d
                .findViewById(R.id.Ibtn_signatureofMarketing);
        Ibtn_signatureofPolicyHolders = d
                .findViewById(R.id.Ibtn_signatureofPolicyHolders);

        list_data.clear();

        /*parivartan changes*/
        imageButtonShubhNiveshProposerPhotograph = d.findViewById(R.id.imageButtonShubhNiveshProposerPhotograph);
        imageButtonShubhNiveshProposerPhotograph.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                Check = "Photo";
                commonMethods.windowmessage(context, "_cust1Photo.jpg");
            }
        });

        if (!proposer_sign.equals("")) {
            if (flg_needAnalyis.equals("1")) {
                tr_need_analysis.setVisibility(View.VISIBLE);
            } else {
                cb_statement_need_analysis.setChecked(true);
                tr_need_analysis.setVisibility(View.GONE);
            }
        }

        txt_annuity_plus_proposer_name.setText("I, " + name_of_proposer +
                ", having received the information with respect to the above, have understood the above statement before entering into the contract.");
        name_of_person = name_of_proposer;
        txt_proposer_name_need_analysis.setText("I, " + name_of_proposer
                + " have undergone the Need Analysis &amp; after having reviewed the SBI Life Product options, I have opted for &apos;SBI LIFE-Annuity Plus&apos;.");

        TextView textviewAGentStatement = d.findViewById(R.id.textviewAGentStatement);

        textviewAGentStatement.setText(commonMethods.getAgentDeclaration(context));

        tv_proposername.setText(name_of_proposer);
        tv_proposal_number.setText(QuatationNumber);


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

        Ibtn_signatureofMarketing
                .setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (cb_statement.isChecked()
                                && cb_statement_need_analysis.isChecked()
                                && checkboxAgentStatement.isChecked()) {
                            latestImage = "agent";
                            windowmessagesgin();
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
                            /*end*/
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

        /*parivartan changes*/


        if (photoBitmap != null) {
            imageButtonShubhNiveshProposerPhotograph.setImageBitmap(photoBitmap);
        }


        final RadioButton radioButtonTrasactionModeManual = d.findViewById(R.id.radioButtonTrasactionModeManual);
        final RadioButton radioButtonTrasactionModeParivartan = d.findViewById(R.id.radioButtonTrasactionModeParivartan);
        final LinearLayout linearlayoutTrasactionModeParivartan = d.findViewById(R.id.linearlayoutTrasactionModeParivartan);

        radioButtonTrasactionModeParivartan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                // TODO Auto-generated method stub
                if (isChecked) {
                    linearlayoutTrasactionModeParivartan.setVisibility(View.VISIBLE);
                } else {
                    linearlayoutTrasactionModeParivartan.setVisibility(View.GONE);

                    String appointeeSignName = NeedAnalysisActivity.URN_NO + "_appointee.png";
                    File appointeeSignFile = mStorageUtils.createFileToAppSpecificDir(context,
                            appointeeSignName);
                    if (appointeeSignFile.exists()) {
                        appointeeSignFile.delete();
                    }

                    String thirdyPartySignName = NeedAnalysisActivity.URN_NO + "_thirdParty.png";

                    File thirdyPartySignFile = mStorageUtils.createFileToAppSpecificDir(context,
                            thirdyPartySignName);

                    if (thirdyPartySignFile.exists()) {
                        thirdyPartySignFile.delete();
                    }


                    String customerPhotoName = NeedAnalysisActivity.URN_NO + "_cust1Photo.jpg";
                    File customerPhotoFile = mStorageUtils.createFileToAppSpecificDir(context,
                            customerPhotoName);
                    if (customerPhotoFile.exists()) {
                        customerPhotoFile.delete();
                    }

                    photoBitmap = null;
                    imageButtonShubhNiveshProposerPhotograph.setImageDrawable
                            (getResources().getDrawable(R.drawable.focus_imagebutton_photo));

                }
            }
        });
        /*end*/

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
                commonMethods.hideKeyboard(edt_annuity_plus_annuity_amount, context);
                // if (frmProductHome.equals("FALSE")) {
                name_of_person = txt_annuity_plus_proposer_name.getText().toString();
                //				place1 = edt_MarketingOfficalPlace.getText().toString();
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
						/*&& ((radioButtonDepositPaymentNo.isChecked() == true
								&& !thirdPartySign.equals(""))
								||radioButtonDepositPaymentYes.isChecked() == true)
						&&((radioButtonAppointeeYes.isChecked() == true && !appointeeSign.equals(""))
								||radioButtonAppointeeNo.isChecked() == true)*/
                ) && radioButtonTrasactionModeParivartan.isChecked())
                        || radioButtonTrasactionModeManual.isChecked())) {
                    NeedAnalysisActivity.str_need_analysis = "";

                    //				String isActive = "0";
                    String productCode = "APLUS";
                    String sum_assured = commonForAllProd.getRound(commonForAllProd.getStringWithout_E(Double
                            .valueOf(purchase_price.equals("") ? "0" : purchase_price)));
                    String basicPlusTax = commonForAllProd.getRound(commonForAllProd.getStringWithout_E(Double
                            .valueOf(totalPurchasePrice.equals("") ? "0" : totalPurchasePrice)));

                    na_cbi_bean = new NA_CBI_bean(QuatationNumber, agentcode, "", userType, "",
                            lifeAssured_Title, lifeAssured_First_Name, lifeAssured_Middle_Name,
                            lifeAssured_Last_Name, planName, sum_assured, basicPlusTax,
                            emailId, mobileNo, agentEmail, agentMobile, na_input, na_output,
                            ModeOfAnnuityPayout, Integer.parseInt(policyTerm), 0, productCode,
                            commonMethods.getDDMMYYYYDate(lifeAssured_date_of_birth),
                            commonMethods.getDDMMYYYYDate(proposer_date_of_birth), inputVal
                            .toString(), retVal.toString());

                    name_of_person = name_of_life_assured;

                    if (radioButtonTrasactionModeParivartan.isChecked()) {
                        mode = "Parivartan";
                    } else if (radioButtonTrasactionModeManual.isChecked()) {
                        mode = "Manual";
                    }
                    dbHelper.AddNeedAnalysisDashboardDetails(new ProductBIBean("", QuatationNumber, planName, getCurrentDate(), mobileNo, getCurrentDate(),
                            dbHelper.GetUserCode(), emailId, "", "",
                            agentcode, "", userType, "",
                            lifeAssured_Title, lifeAssured_First_Name, lifeAssured_Middle_Name, lifeAssured_Last_Name,
                            sum_assured, basicPlusTax,
                            agentEmail, agentMobile, na_input, na_output,
                            ModeOfAnnuityPayout, Integer.parseInt(policyTerm), 0, productCode,
                            commonMethods.getDDMMYYYYDate(lifeAssured_date_of_birth),
                            commonMethods.getDDMMYYYYDate(proposer_date_of_birth), "", mode, inputVal
                            .toString(), retVal.toString()));

                    createPdf(annuityPlusBean);


                    NABIObj.serviceHit(BI_AnnuityPlusActivity.this, na_cbi_bean, newFile, needAnalysispath.getPath(),
                            mypath.getPath(), name_of_person, QuatationNumber, mode);
                    d.dismiss();

                } else {

                    if (proposer_sign.equals("") && !bankUserType
                            .equalsIgnoreCase("Y")) {
                        commonMethods.dialogWarning(context, "Please Make Signature for Proposer ", true);
                        commonMethods.setFocusable(Ibtn_signatureofPolicyHolders);
                        Ibtn_signatureofPolicyHolders.requestFocus();
                    } else if (place2.equals("")) {
                        commonMethods.dialogWarning(context, "Please Fill Place Detail", true);
                        commonMethods.setFocusable(edt_Policyholderplace);
                        edt_Policyholderplace.requestFocus();

                    } else if (agent_sign.equals("") && !bankUserType
                            .equalsIgnoreCase("Y")) {
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
                    } else if (!radioButtonTrasactionModeParivartan.isChecked() && !radioButtonTrasactionModeManual.isChecked()) {
                        commonMethods.dialogWarning(context, "Please Select Transaction Mode", true);
                        commonMethods.setFocusable(linearlayoutTrasactionModeParivartan);
                        linearlayoutTrasactionModeParivartan.requestFocus();
                    } else if (photoBitmap == null) {
                        commonMethods.dialogWarning(context, "Please Capture the Photo", true);
                        commonMethods.setFocusable(imageButtonShubhNiveshProposerPhotograph);
                        imageButtonShubhNiveshProposerPhotograph.requestFocus();
                    }

                    /*end*/
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

        String input = inputVal.toString();
        String output = retVal.toString();

        String isJKResident = prsObj.parseXmlTag(input, "isJKResident");
        String adbRiderStatus = prsObj.parseXmlTag(input, "isADBRider");
        String staffdiscount = prsObj.parseXmlTag(input, "isStaff");

        if (isJKResident.equalsIgnoreCase("true")) {
            tv_bi_is_jk.setText("Yes");
        } else {
            tv_bi_is_jk.setText("No");
        }

        if (staffdiscount.equalsIgnoreCase("true")) {
            tv_bi_is_Staff.setText("Yes");
        } else {
            tv_bi_is_Staff.setText("No");
        }

        nameof_secondannuitant = prsObj.parseXmlTag(input, "NameOfSecondAnnuitant");
        TitleSecondAnnuitant = prsObj.parseXmlTag(input, "TitleSecondAnnuitant");
        isadvance_annuityPayout = prsObj.parseXmlTag(input, "isAdvanceAnnuityPayout");

        annuity_option = prsObj.parseXmlTag(input, "AnnuityOption");
        tv_bi_annuity_plus_annuity_option_opted.setText(annuity_option);

        proposer_age = prsObj.parseXmlTag(input, "proposer_age");
        tv_bi_annuity_plus_proposer_age.setText(proposer_age);


        first_annuitant_age = prsObj.parseXmlTag(input, "AgeFirstAnnuitant");
        tv_bi_annuity_plus_first_annuitant_age.setText(first_annuitant_age);

        second_annuitant_age = prsObj.parseXmlTag(input, "AgeSecondAnnuitant");

        if (annuity_option.equalsIgnoreCase("Life and Last Survivor - 50% Income") || annuity_option.equalsIgnoreCase("Life and Last Survivor - 100% Income") ||
                annuity_option.equalsIgnoreCase("Life and Last Survivor with Capital Refund - 50% Income") || annuity_option.equalsIgnoreCase("Life and Last Survivor with Capital Refund - 100% Income")) {
            tv_bi_annuity_plus_second_annuitant_age.setText(second_annuitant_age);
        } else {
            tv_bi_annuity_plus_second_annuitant_age.setText("NA");
        }

        source_of_business = prsObj.parseXmlTag(input, "SourceOfBusiness");
        tv_bi_annuity_plus_source_of_business.setText(source_of_business);


        mode_of_annuity_payout = prsObj.parseXmlTag(input, "ModeOfAnnuityPayout");
        tv_bi_annuity_plus_mode_of_annuity_payout.setText(mode_of_annuity_payout);

        annuity_payoutDate = prsObj.parseXmlTag(input, "AnnuityPayoutDate");
        if (!annuity_payoutDate.equalsIgnoreCase("")) {
            tv_bi_annuity_plus_annuity_amount_start_date.setText(commonMethods.getDDMMYYYYDate(annuity_payoutDate));
        } else {
            tv_bi_annuity_plus_annuity_amount_start_date.setText("NA");
        }


        opt_for = prsObj.parseXmlTag(input, "OptFor");

        annuity_amount = prsObj.parseXmlTag(input, "AnnuityAmount");

        vesting_amount = prsObj.parseXmlTag(input, "VestingAmount");
        annuity_amount_payable = prsObj.parseXmlTag(output, "annuityAmtPayable");


        SurvivalBenefits_annuity_plus_annuity_amount_payable = commonForAllProd.getStringWithout_E(Double
                .valueOf(((prsObj.parseXmlTag(result_grid,
                        "Survival_Benefit" + 1 + "") == null) || (prsObj
                        .parseXmlTag(result_grid,
                                "Survival_Benefit" + 1 + "")
                        .equals(""))) ? "0" : prsObj
                        .parseXmlTag(result_grid,
                                "Survival_Benefit" + 1 + "")))
                + "";


        if (source_of_business.equalsIgnoreCase("Vesting of SBI Life's Pension Policy")) {
            if (opt_for.equalsIgnoreCase("Annuity Payout Amount")) {
                if (!annuity_amount.equalsIgnoreCase("") && !annuity_amount.equalsIgnoreCase("0")) {
                    tv_bi_annuity_plus_annuity_amount_payable.setText(annuity_amount);
                } else {
                    tv_bi_annuity_plus_annuity_amount_payable.setText("NA");
                }
                if (!vesting_amount.equalsIgnoreCase("") && !vesting_amount.equalsIgnoreCase("0")) {
                    tv_bi_annuity_plus_vesting_amount.setText(vesting_amount);
                } else {
                    tv_bi_annuity_plus_vesting_amount.setText("NA");
                }


            } else if (opt_for.equalsIgnoreCase("Premium Amount")) {
                if (!annuity_amount_payable.equalsIgnoreCase("") && !annuity_amount_payable.equalsIgnoreCase("0")) {
                    tv_bi_annuity_plus_annuity_amount_payable.setText(annuity_amount_payable);
                } else {
                    tv_bi_annuity_plus_annuity_amount_payable.setText("NA");
                }
                if (!vesting_amount.equalsIgnoreCase("") && !vesting_amount.equalsIgnoreCase("0")) {
                    tv_bi_annuity_plus_vesting_amount.setText(vesting_amount);
                } else {
                    tv_bi_annuity_plus_vesting_amount.setText("NA");
                }


            }
        } else {
            if (opt_for.equalsIgnoreCase("Annuity Payout Amount")) {
                if (!annuity_amount.equalsIgnoreCase("") && !annuity_amount.equalsIgnoreCase("0")) {
                    // tv_bi_annuity_plus_annuity_amount_payable.setText(annuity_amount);
                    tv_bi_annuity_plus_annuity_amount_payable.setText(SurvivalBenefits_annuity_plus_annuity_amount_payable);


                } else {
                    //tv_bi_annuity_plus_annuity_amount_payable.setText("NA");
                    tv_bi_annuity_plus_annuity_amount_payable.setText(SurvivalBenefits_annuity_plus_annuity_amount_payable);
                }
                if (!vesting_amount.equalsIgnoreCase("") && !vesting_amount.equalsIgnoreCase("0")) {
                    tv_bi_annuity_plus_vesting_amount.setText(vesting_amount);
                } else {
                    tv_bi_annuity_plus_vesting_amount.setText("NA");
                }
            } else if (opt_for.equalsIgnoreCase("Premium Amount")) {
                if (!annuity_amount_payable.equalsIgnoreCase("") && !annuity_amount_payable.equalsIgnoreCase("0")) {
                    // tv_bi_annuity_plus_annuity_amount_payable.setText(annuity_amount_payable);
                    tv_bi_annuity_plus_annuity_amount_payable.setText(SurvivalBenefits_annuity_plus_annuity_amount_payable);
                } else {
                    // tv_bi_annuity_plus_annuity_amount_payable.setText("NA");
                    tv_bi_annuity_plus_annuity_amount_payable.setText(SurvivalBenefits_annuity_plus_annuity_amount_payable);
                }
                if (!vesting_amount.equalsIgnoreCase("") && !vesting_amount.equalsIgnoreCase("0")) {
                    tv_bi_annuity_plus_vesting_amount.setText(vesting_amount);
                } else {
                    tv_bi_annuity_plus_vesting_amount.setText("NA");
                }
            }
        }


        purchase_price = prsObj.parseXmlTag(output, "purchasePrice");

        tv_bi_annuity_plus_purchase_price.setText(commonForAllProd.getRound(commonForAllProd.getStringWithout_E(Double
                .valueOf(purchase_price.equals("") ? "0" : purchase_price))));

        //total_premium_required = prsObj.parseXmlTag(output, "totPremRequird");
        String total_premium_required = prsObj.parseXmlTag(output, "totalPremReq");


        tv_bi_annuity_plus_total_premium_required.setText(commonForAllProd.getRound(commonForAllProd.getStringWithout_E(Double
                .valueOf(total_premium_required.equals("") ? "0" : total_premium_required))));


        //total_service_tax = prsObj.parseXmlTag(output, "totalServTax");
        String total_service_tax = prsObj.parseXmlTag(output, "totServiceTax");

        tv_bi_annuity_plus_total_service_tax.setText(commonForAllProd.getRound(commonForAllProd.getStringWithout_E(Double
                .valueOf(total_service_tax.equals("") ? "0" : total_service_tax))));

        String additional_premium_payable = prsObj.parseXmlTag(output, "addPremPayable");

        tv_bi_annuity_plus_additional_premium_payable.setText(commonForAllProd.getRound(commonForAllProd.getStringWithout_E(Double
                .valueOf(additional_premium_payable.equals("") ? "0" : additional_premium_payable))));

        advance_premium_payable = prsObj.parseXmlTag(output, "advancePremPayable");

        if (!advance_premium_payable.equalsIgnoreCase("") && !advance_premium_payable.equalsIgnoreCase("0")) {
            tv_bi_annuity_plus_advance_premium_payable.setText(commonForAllProd.getRound(commonForAllProd.getStringWithout_E(Double
                    .valueOf(advance_premium_payable.equals("") ? "0" : advance_premium_payable))));

            tv_interest_advance_annuity_without_tax.setText(commonForAllProd.getRound(commonForAllProd.getStringWithout_E(Double
                    .valueOf(advance_premium_payable.equals("") ? "0" : advance_premium_payable))));

            tv_interest_advance_annuity_first_year_with_tax.setText(commonForAllProd.getRound(commonForAllProd.getStringWithout_E(Double
                    .valueOf(advance_premium_payable.equals("") ? "0" : advance_premium_payable))));

        } else {
            tv_bi_annuity_plus_advance_premium_payable.setText("NA");
            tv_interest_advance_annuity_without_tax.setText("NA");
            tv_interest_advance_annuity_first_year_with_tax.setText("NA");
            advance_premium_payable = "0";
        }


        total_premium = prsObj.parseXmlTag(output, "totalPrem");


        totalPurchasePrice = prsObj.parseXmlTag(output, "totalPurchasePrice");


        tv_bi_annuity_plus_total_premium.setText(commonForAllProd.getRound
                (commonForAllProd.getStringWithout_E(Double
                        .valueOf(total_premium.equals("") ? "0" : total_premium))));


        tv_bi_annuity_plus_proposer_name.setText(name_of_proposer);
        tv_bi_annuity_plus_first_annuitant_name.setText(
                name_of_life_assured);


        String str_name_of_second_annuitant;
        String str_second_annuitant_age = "";
        if (annuity_option.equalsIgnoreCase("Life and Last Survivor - 50% Income") || annuity_option.equalsIgnoreCase("Life and Last Survivor - 100% Income") ||
                annuity_option.equalsIgnoreCase("Life and Last Survivor with Capital Refund - 50% Income") || annuity_option.equalsIgnoreCase("Life and Last Survivor with Capital Refund - 100% Income")) {
            str_name_of_second_annuitant = name_of_second_annuitant;
        } else {
            str_name_of_second_annuitant = "-";
        }


        tv_bi_annuity_plus_second_annuitant_name.setText(
                str_name_of_second_annuitant);
        tv_amount_installment_premium.setText(total_premium);


        if (
                annuity_option.equalsIgnoreCase("Life and Last Survivor with Capital Refund - 50% Income") ||
                        annuity_option.equalsIgnoreCase("Life and Last Survivor with Capital Refund - 100% Income") ||
                        annuity_option.equalsIgnoreCase("Lifetime Income with Capital Refund") ||

                        annuity_option.equalsIgnoreCase("Lifetime Income with Capital Refund in Parts") ||
                        annuity_option.equalsIgnoreCase("Lifetime Income with Balance Capital Refund")) {
            SumAssured = purchase_price;
            SumAssuredonDeath = purchase_price;
            tv_sum_assured_on_death.setText(SumAssured);
            tv_bi_annuity_plus_loan_outstanding_loan_amount.setText(SumAssuredonDeath);
        } else {

            SumAssured = "0";
            SumAssuredonDeath = "0";
            tv_sum_assured_on_death.setText(SumAssured);
            tv_bi_annuity_plus_loan_outstanding_loan_amount.setText(SumAssuredonDeath);
        }
        tv_source_of_funds.setText(source_of_business);

        tv_base_plan_premium_without_tax.setText(commonForAllProd.getRound(commonForAllProd.getStringWithout_E((Double.parseDouble(purchase_price)))));
        // tv_interest_advance_annuity_without_tax.setText("0");
        tv_total_installment_premium_without_tax.setText(commonForAllProd.getRound(commonForAllProd.getStringWithout_E((Double.parseDouble(purchase_price) + Double.parseDouble(advance_premium_payable)))));

        tv_base_plan_premium_first_year_with_tax.setText(commonForAllProd.getRound(commonForAllProd.getStringWithout_E((Double.parseDouble(totalPurchasePrice)))));
        //   tv_interest_advance_annuity_first_year_with_tax.setText("0");
        //tv_total_installment_premium_first_year_with_tax.setText(total_premium);
        tv_total_installment_premium_first_year_with_tax.setText(commonForAllProd.getRound(commonForAllProd.getStringWithout_E((Double.parseDouble(totalPurchasePrice) + Double.parseDouble(advance_premium_payable)))));

        tv_base_plan_premium_second_year_with_tax.setText("Not Applicable");
        tv_interest_advance_annuity_second_year_with_tax.setText("Not Applicable");
        tv_total_installment_premium_second_year_with_tax.setText("Not Applicable");


        if (adbRiderStatus.equalsIgnoreCase("true")) {
            tr_bi_annuity_plus_rider.setVisibility(View.VISIBLE);
            String sa_adb = prsObj.parseXmlTag(output, "riderSumAssured");
            String prem_adb = prsObj.parseXmlTag(output, "riderPrem");

            tv_bi_annuity_plus_rider_sum_assured.setText(commonForAllProd.getRound(commonForAllProd.getStringWithout_E(Double
                    .valueOf(sa_adb.equals("") ? "0" : sa_adb))));

            tv_bi_annuity_plus_rider_premium
                    .setText(commonForAllProd.getRound(commonForAllProd.getStringWithout_E(Double
                            .valueOf(prem_adb.equals("") ? "0" : prem_adb))));

        }

        createPdf(annuityPlusBean);


        //list_data.clear();
        for (int i = 1; i <= 25; i++) {
            String end_of_year = prsObj.parseXmlTag(result_grid, "policyYr" + i + "");
            if (i == 25) {
                end_of_year = "Till Death";
            }




          /*  String yearly_basic_premium = commonForAllProd
                    .getRound(commonForAllProd.getStringWithout_E(Double
                            .valueOf(((prsObj.parseXmlTag(result_grid,
                                    "Annualized_premium" + i + "") == null) || (prsObj
                                    .parseXmlTag(result_grid,
                                            "Annualized_premium" + i + "")
                                    .equals(""))) ? "0" : prsObj
                                    .parseXmlTag(result_grid,
                                            "Annualized_premium" + i + ""))))
                    + "";*/

            String yearly_basic_premium = commonForAllProd.getStringWithout_E(Double
                    .valueOf(((prsObj.parseXmlTag(result_grid,
                            "Annualized_premium" + i + "") == null) || (prsObj
                            .parseXmlTag(result_grid,
                                    "Annualized_premium" + i + "")
                            .equals(""))) ? "0" : prsObj
                            .parseXmlTag(result_grid,
                                    "Annualized_premium" + i + "")))
                    + "";



          /*  String SurvivalBenefits = commonForAllProd
                    .getRound(commonForAllProd.getStringWithout_E(Double
                            .valueOf(((prsObj.parseXmlTag(result_grid,
                                    "Survival_Benefit" + i + "") == null) || (prsObj
                                    .parseXmlTag(result_grid,
                                            "Survival_Benefit" + i + "")
                                    .equals(""))) ? "0" : prsObj
                                    .parseXmlTag(result_grid,
                                            "Survival_Benefit" + i + ""))))
                    + "";*/
            String SurvivalBenefits = commonForAllProd.getStringWithout_E(Double
                    .valueOf(((prsObj.parseXmlTag(result_grid,
                            "Survival_Benefit" + i + "") == null) || (prsObj
                            .parseXmlTag(result_grid,
                                    "Survival_Benefit" + i + "")
                            .equals(""))) ? "0" : prsObj
                            .parseXmlTag(result_grid,
                                    "Survival_Benefit" + i + "")))
                    + "";

         /*   String   OtherBenefitsifAny = commonForAllProd
                    .getRound(commonForAllProd.getStringWithout_E(Double
                            .valueOf(((prsObj.parseXmlTag(result_grid,
                                    "Other_Benifits" + i + "") == null) || (prsObj
                                    .parseXmlTag(result_grid,
                                            "Other_Benifits" + i + "")
                                    .equals(""))) ? "0" : prsObj
                                    .parseXmlTag(result_grid,
                                            "Other_Benifits" + i + ""))))
                    + "";
*/
            String OtherBenefitsifAny = commonForAllProd.getStringWithout_E(Double
                    .valueOf(((prsObj.parseXmlTag(result_grid,
                            "Other_Benifits" + i + "") == null) || (prsObj
                            .parseXmlTag(result_grid,
                                    "Other_Benifits" + i + "")
                            .equals(""))) ? "0" : prsObj
                            .parseXmlTag(result_grid,
                                    "Other_Benifits" + i + "")))
                    + "";




								/*String guaranteed_maturity_benefit = ((int) Double
                                        .parseDouble(prsObj.parseXmlTag(result_grid,
												"Maturity_Benifits" + i + "")))
										+ "";*/
            String guaranteed_maturity_benefit = prsObj.parseXmlTag(result_grid, "Maturity_Benifits" + i + "");


								/*String guaranteed_death_benefit = ((int) Double.parseDouble(prsObj
                                        .parseXmlTag(result_grid, "Death_Benifits" + i + "")))
										+ "";*/

            String guaranteed_death_benefit = prsObj
                    .parseXmlTag(result_grid, "Death_Benifits" + i + "") + "";

								/*String guaranteed_surrender_value = ((int) Double
                                        .parseDouble(prsObj.parseXmlTag(result_grid,
												"Min_Guaranteed_Surr_Value" + i + "")))
										+ "";*/

            String guaranteed_surrender_value = prsObj.parseXmlTag(result_grid, "Min_Guaranteed_Surr_Value" + i + "");

								/*String nonGuaranSurrenderValue = ((int) Double
										.parseDouble(prsObj.parseXmlTag(result_grid,
												"Special_Surr_Value" + i + "")))
										+ "";*/

            String nonGuaranSurrenderValue = prsObj.parseXmlTag(result_grid,
                    "Special_Surr_Value" + i + "");


            list_data.add(new M_BI_Annuity_Plus_Adapter(end_of_year,
                    yearly_basic_premium, SurvivalBenefits, OtherBenefitsifAny,
                    guaranteed_maturity_benefit, guaranteed_death_benefit,
                    guaranteed_surrender_value, nonGuaranSurrenderValue));

			/*list_data.add(new M_BI_Annuity_Plus_Adapter(end_of_year,
					"", "", "",
					"", "",
					"", ""));
		*/
        }


        Adapter_BI_AnnuityPlusGrid adapter = new Adapter_BI_AnnuityPlusGrid(
                this, list_data);
        gv_userinfo.setAdapter(adapter);

        GridHeight gh = new GridHeight();
        gh.getheight(gv_userinfo, "25");

        d.show();

    }


    private void windowmessagesgin() {

        d = new Dialog(BI_AnnuityPlusActivity.this);
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);
        d.getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.BLACK));

        d.setContentView(R.layout.window_message_signature);
        final Button btn_save = d.findViewById(R.id.save);
        final Button btn_cancel = d.findViewById(R.id.cancel);

        Button btn_takeSign = d.findViewById(R.id.takesignature);

        btn_takeSign.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_save.setVisibility(View.INVISIBLE);
                Intent intent = new Intent(BI_AnnuityPlusActivity.this,
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


    private void createPdf(AnnuityPlusBean annuityPlusBean) {
        try {
            ParseXML prsObj = new ParseXML();
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
            //			needAnalysis_Tabular_path = new File(folder, "NA_Tabular.pdf");
            newFile = mStorageUtils.createFileToAppSpecificDir(context, QuatationNumber + "P01.pdf");

            Rectangle rect = new Rectangle(594f, 792f);

            Document document = new Document(rect, 50, 50, 50, 50);
            // Document document = new Document(PageSize.A4, 50, 50, 50, 50);
            PdfWriter pdf_writer = null;
            pdf_writer = PdfWriter.getInstance(document, new FileOutputStream(
                    mypath.getAbsolutePath()));

            document.open();


            // For SBI- Life Logo starts
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            Bitmap bitmap = BitmapFactory.decodeResource(getBaseContext().getResources(), R.drawable.sbi_life_logo);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
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
                            "Some benefits are guaranteed and some benefits are variable with returns based on the future performance of your insurer carrying on life insurance business. If your policy offers guaranteed benefits then these will be clearly marked “guaranteed” in the illustration table on this page. If your policy offers variable benefits then the illustrations on this page will show two different rates of assumed future investment returns. These assumed rates of return are not guaranteed and they are not the upper or lower limits of what you might get back, as the value of your policy is dependent on a number of factors including future investment performance.",
                            small_normal));

            PdfPTable headertable = new PdfPTable(1);
            headertable.setWidthPercentage(100);
            PdfPCell c1 = new PdfPCell(new Phrase(Para_Header));
            //c1.setBackgroundColor(BaseColor.DARK_GRAY);
            c1.setPadding(5);
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            headertable.addCell(c1);
            headertable.setHorizontalAlignment(Element.ALIGN_CENTER);
            Paragraph para_address = new Paragraph(
                    "SBI Life Insurance Co. Ltd",
                    small_bold_for_name);
            para_address.setAlignment(Element.ALIGN_CENTER);
            Paragraph para_address1 = new Paragraph(
                    "Registered & Corporate Office: SBI Life Insurance Co. Ltd, “Natraj”, M.V. Road & Western Express Highway Junction, Andheri (East),",
                    small_bold);
            para_address1.setAlignment(Element.ALIGN_CENTER);
            Paragraph para_address2 = new Paragraph(
                    "Mumbai - 400 069 | IRDAI Registration No. 111 | Website: www.sbilife.co.in | Email: info@sbilife.co.in | CIN: L99999MH2000PLC129113",
                    small_bold);
            para_address2.setAlignment(Element.ALIGN_CENTER);
            Paragraph para_address3 = new Paragraph(
                    "Toll Free: 1800 267 9090 (Between 9.00 am & 9.00 pm)",
                    small_bold);
            para_address3.setAlignment(Element.ALIGN_CENTER);
            Paragraph para_address4 = new Paragraph(
                    "Benefit Illustration for SBI Life - Annuity Plus(UIN: 111N083V11)",
                    small_bold);
            para_address4.setAlignment(Element.ALIGN_CENTER);
            Paragraph para_address5 = new Paragraph(
                    "An Individual, Non-Linked, Non-Participating, General Annuity Product",
                    small_bold);
            para_address5.setAlignment(Element.ALIGN_CENTER);
            document.add(para_address);
            document.add(para_address1);
            document.add(para_address2);
            document.add(para_address3);
            document.add(para_address4);
            document.add(para_address5);

            document.add(para_img_logo_after_space_1);
            document.add(headertable);
            document.add(para_img_logo_after_space_1);

            //	document.add(para_img_logo_after_space_1);
            if (Proposer_is_same_as_Annuitant.equalsIgnoreCase("Y")) {
                name_of_proposer = name_of_life_assured;
            }

            PdfPTable table_proposer_name = new PdfPTable(4);
            // float[] columnWidths_table_proposer_name = { 2f, 2f, 1f, 2f };
            // table_proposer_name.setWidths(columnWidths_table_proposer_name);
            table_proposer_name.setWidthPercentage(100);

            PdfPCell ProposalNumber_cell_1 = new PdfPCell(new Paragraph(
                    "Name of the Prospect/ Policyholder:", small_normal));
            PdfPCell ProposalNumber_cell_2 = new PdfPCell(new Paragraph(
                    name_of_proposer, small_bold1));
            ProposalNumber_cell_2.setHorizontalAlignment(Element.ALIGN_CENTER);
            PdfPCell NameofProposal_cell_3 = new PdfPCell(new Paragraph(
                    "Quotation Number: ", small_normal));
            PdfPCell NameofProposal_cell_4 = new PdfPCell(new Paragraph(
                    QuatationNumber, small_bold1));
            NameofProposal_cell_4.setHorizontalAlignment(Element.ALIGN_CENTER);

            NameofProposal_cell_4.setVerticalAlignment(Element.ALIGN_CENTER);

            PdfPCell ProposalNumber_cell_5 = new PdfPCell(new Paragraph(
                    "Age: (in Years)", small_normal));
            PdfPCell ProposalNumber_cell_6 = new PdfPCell(new Paragraph(
                    proposer_age, small_bold1));
            ProposalNumber_cell_6.setHorizontalAlignment(Element.ALIGN_CENTER);
            PdfPCell NameofProposal_cell_7 = new PdfPCell(new Paragraph(
                    "Name of the Product: ", small_normal));
            PdfPCell NameofProposal_cell_8 = new PdfPCell(new Paragraph(
                    "SBI Life - Annuity Plus", small_bold1));
            NameofProposal_cell_8.setHorizontalAlignment(Element.ALIGN_CENTER);

            NameofProposal_cell_8.setVerticalAlignment(Element.ALIGN_CENTER);

            PdfPCell ProposalNumber_cell_9 = new PdfPCell(new Paragraph(
                    "Name of the First Annuitant:", small_normal));
            PdfPCell ProposalNumber_cell_10 = new PdfPCell(new Paragraph(
                    name_of_life_assured, small_bold1));
            ProposalNumber_cell_10.setHorizontalAlignment(Element.ALIGN_CENTER);
            PdfPCell NameofProposal_cell_11 = new PdfPCell(new Paragraph(
                    "Tag line:  ", small_normal));
            PdfPCell NameofProposal_cell_12 = new PdfPCell(new Paragraph(
                    "Individual, Non linked, Non Participating, General Annuity Product", small_bold1));
            NameofProposal_cell_12.setHorizontalAlignment(Element.ALIGN_CENTER);

            NameofProposal_cell_12.setVerticalAlignment(Element.ALIGN_CENTER);

            PdfPCell ProposalNumber_cell_13 = new PdfPCell(new Paragraph(
                    "Age: (in Years)", small_normal));
            PdfPCell ProposalNumber_cell_14 = new PdfPCell(new Paragraph(
                    first_annuitant_age, small_bold1));
            ProposalNumber_cell_14.setHorizontalAlignment(Element.ALIGN_CENTER);
            PdfPCell NameofProposal_cell_15 = new PdfPCell(new Paragraph(
                    "Unique Identification No.: ", small_normal));
            PdfPCell NameofProposal_cell_16 = new PdfPCell(new Paragraph(
                    "111N083V11", small_bold1));
            NameofProposal_cell_16.setHorizontalAlignment(Element.ALIGN_CENTER);

            NameofProposal_cell_16.setVerticalAlignment(Element.ALIGN_CENTER);


            String str_second_annuitant_age;
            String str_name_of_second_annuitant;
            if (annuity_option.equalsIgnoreCase("Life and Last Survivor - 50% Income") || annuity_option.equalsIgnoreCase("Life and Last Survivor - 100% Income") ||
                    annuity_option.equalsIgnoreCase("Life and Last Survivor with Capital Refund - 50% Income") || annuity_option.equalsIgnoreCase("Life and Last Survivor with Capital Refund - 100% Income")) {
                str_second_annuitant_age = second_annuitant_age;
                str_name_of_second_annuitant = name_of_second_annuitant;
            } else {
                str_second_annuitant_age = "NA";
                str_name_of_second_annuitant = "NA";
            }


            PdfPCell ProposalNumber_cell_17 = new PdfPCell(new Paragraph(
                    "Name of Second Annuitant:", small_normal));
            PdfPCell ProposalNumber_cell_18 = new PdfPCell(new Paragraph(
                    str_name_of_second_annuitant, small_bold1));
            ProposalNumber_cell_18.setHorizontalAlignment(Element.ALIGN_CENTER);
            PdfPCell NameofProposal_cell_19 = new PdfPCell(new Paragraph(
                    "GST Rate:  ", small_normal));
            PdfPCell NameofProposal_cell_20;
            if (cb_kerladisc.isChecked()) {
                NameofProposal_cell_20 = new PdfPCell(new Paragraph(
                        "1.9 %", small_bold1));
            } else {
                NameofProposal_cell_20 = new PdfPCell(new Paragraph(
                        "1.8 %", small_bold1));
            }
            NameofProposal_cell_20.setHorizontalAlignment(Element.ALIGN_CENTER);

            NameofProposal_cell_20.setVerticalAlignment(Element.ALIGN_CENTER);


            PdfPCell ProposalNumber_cell_21 = new PdfPCell(new Paragraph(
                    "Age: (in Years)", small_normal));
            PdfPCell ProposalNumber_cell_22 = new PdfPCell(new Paragraph(
                    str_second_annuitant_age, small_bold1));
            ProposalNumber_cell_22.setHorizontalAlignment(Element.ALIGN_CENTER);
            PdfPCell NameofProposal_cell_23 = new PdfPCell(new Paragraph(
                    "Vesting Age: ", small_normal));
            PdfPCell NameofProposal_cell_24 = new PdfPCell(new Paragraph(
                    "Not Applicable", small_bold1));
            NameofProposal_cell_24.setHorizontalAlignment(Element.ALIGN_CENTER);

            NameofProposal_cell_24.setVerticalAlignment(Element.ALIGN_CENTER);

            PdfPCell ProposalNumber_cell_25 = new PdfPCell(new Paragraph(
                    "Policy Term:", small_normal));
            PdfPCell ProposalNumber_cell_26 = new PdfPCell(new Paragraph(
                    "Not Applicable", small_bold1));
            ProposalNumber_cell_26.setHorizontalAlignment(Element.ALIGN_CENTER);
            PdfPCell NameofProposal_cell_27 = new PdfPCell(new Paragraph(
                    "Premium Payment Term:  ", small_normal));
            PdfPCell NameofProposal_cell_28 = new PdfPCell(new Paragraph(
                    "Not Applicable", small_bold1));
            NameofProposal_cell_28.setHorizontalAlignment(Element.ALIGN_CENTER);

            NameofProposal_cell_28.setVerticalAlignment(Element.ALIGN_CENTER);

            PdfPCell ProposalNumber_cell_29 = new PdfPCell(new Paragraph(
                    "Amount of Instalment Premium: ", small_normal));
            PdfPCell ProposalNumber_cell_30 = new PdfPCell(new Paragraph(
                    commonForAllProd.getRound(commonForAllProd.getStringWithout_E(Double.parseDouble(total_premium))), small_bold1));
            ProposalNumber_cell_30.setHorizontalAlignment(Element.ALIGN_CENTER);
            PdfPCell NameofProposal_cell_31 = new PdfPCell(new Paragraph(
                    "Mode of Premium Payment:  ", small_normal));
            PdfPCell NameofProposal_cell_32 = new PdfPCell(new Paragraph(
                    "Single", small_bold1));
            NameofProposal_cell_32.setHorizontalAlignment(Element.ALIGN_CENTER);

            NameofProposal_cell_32.setVerticalAlignment(Element.ALIGN_CENTER);

            ProposalNumber_cell_1.setPadding(5);
            ProposalNumber_cell_2.setPadding(5);
            NameofProposal_cell_3.setPadding(5);
            NameofProposal_cell_4.setPadding(5);
            ProposalNumber_cell_5.setPadding(5);
            ProposalNumber_cell_6.setPadding(5);
            NameofProposal_cell_7.setPadding(5);
            NameofProposal_cell_8.setPadding(5);
            ProposalNumber_cell_9.setPadding(5);
            ProposalNumber_cell_10.setPadding(5);
            NameofProposal_cell_11.setPadding(5);
            NameofProposal_cell_12.setPadding(5);
            ProposalNumber_cell_13.setPadding(5);
            ProposalNumber_cell_14.setPadding(5);
            NameofProposal_cell_15.setPadding(5);
            NameofProposal_cell_16.setPadding(5);
            ProposalNumber_cell_17.setPadding(5);
            ProposalNumber_cell_18.setPadding(5);
            NameofProposal_cell_19.setPadding(5);
            NameofProposal_cell_20.setPadding(5);
            ProposalNumber_cell_21.setPadding(5);
            ProposalNumber_cell_22.setPadding(5);
            NameofProposal_cell_23.setPadding(5);
            NameofProposal_cell_24.setPadding(5);
            ProposalNumber_cell_25.setPadding(5);
            ProposalNumber_cell_26.setPadding(5);
            NameofProposal_cell_27.setPadding(5);
            NameofProposal_cell_28.setPadding(5);
            ProposalNumber_cell_29.setPadding(5);
            ProposalNumber_cell_30.setPadding(5);
            NameofProposal_cell_31.setPadding(5);
            NameofProposal_cell_32.setPadding(5);

            table_proposer_name.addCell(ProposalNumber_cell_1);
            table_proposer_name.addCell(ProposalNumber_cell_2);
            table_proposer_name.addCell(NameofProposal_cell_3);
            table_proposer_name.addCell(NameofProposal_cell_4);
            table_proposer_name.addCell(ProposalNumber_cell_5);
            table_proposer_name.addCell(ProposalNumber_cell_6);
            table_proposer_name.addCell(NameofProposal_cell_7);
            table_proposer_name.addCell(NameofProposal_cell_8);
            table_proposer_name.addCell(ProposalNumber_cell_9);
            table_proposer_name.addCell(ProposalNumber_cell_10);
            table_proposer_name.addCell(NameofProposal_cell_11);
            table_proposer_name.addCell(NameofProposal_cell_12);
            table_proposer_name.addCell(ProposalNumber_cell_13);
            table_proposer_name.addCell(ProposalNumber_cell_14);
            table_proposer_name.addCell(NameofProposal_cell_15);
            table_proposer_name.addCell(NameofProposal_cell_16);
            table_proposer_name.addCell(ProposalNumber_cell_17);
            table_proposer_name.addCell(ProposalNumber_cell_18);
            table_proposer_name.addCell(NameofProposal_cell_19);
            table_proposer_name.addCell(NameofProposal_cell_20);
            table_proposer_name.addCell(ProposalNumber_cell_21);
            table_proposer_name.addCell(ProposalNumber_cell_22);
            table_proposer_name.addCell(NameofProposal_cell_23);
            table_proposer_name.addCell(NameofProposal_cell_24);
            table_proposer_name.addCell(ProposalNumber_cell_25);
            table_proposer_name.addCell(ProposalNumber_cell_26);
            table_proposer_name.addCell(NameofProposal_cell_27);
            table_proposer_name.addCell(NameofProposal_cell_28);
            table_proposer_name.addCell(ProposalNumber_cell_29);
            table_proposer_name.addCell(ProposalNumber_cell_30);
            table_proposer_name.addCell(NameofProposal_cell_31);
            table_proposer_name.addCell(NameofProposal_cell_32);
            document.add(table_proposer_name);
            document.add(para_img_logo_after_space_1);

            LineSeparator ls = new LineSeparator();

            Paragraph para_address41 = new Paragraph(
                    "This benefit illustration is intended to show year-wise premiums payable and benefits under the policy. ",
                    small_bold);
            para_address4.setAlignment(Element.ALIGN_CENTER);

            //inputTable here -1

            PdfPTable personalDetail_table = new PdfPTable(4);
            //personalDetail_table.setWidths(new float []{4f,5f,4f,5f});
            personalDetail_table.setWidthPercentage(100f);
            personalDetail_table.setHorizontalAlignment(Element.ALIGN_CENTER);


            PdfPCell cell;
            //1 row
            cell = new PdfPCell(new Phrase("Policy Details", small_normal));
            //cell.setBorder(Rectangle.BOTTOM|Rectangle.LEFT|Rectangle.TOP);
            cell.setColspan(4);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            personalDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase("Policy Option", small_normal));
            cell.setBorder(Rectangle.BOTTOM | Rectangle.LEFT | Rectangle.TOP);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            personalDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase(annuity_option, small_normal));
            cell.setBorder(Rectangle.BOTTOM | Rectangle.LEFT | Rectangle.TOP);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            personalDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase("Sum Assured Rs.", small_normal));
            cell.setBorder(Rectangle.BOTTOM | Rectangle.LEFT | Rectangle.TOP);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            personalDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase(commonForAllProd.getRound(commonForAllProd.getStringWithout_E(Double.parseDouble(SumAssured))), small_normal));
            cell.setBorder(Rectangle.BOTTOM | Rectangle.LEFT | Rectangle.TOP | Rectangle.RIGHT);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            personalDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase("Sum Assured on Death (at inception of the policy) Rs.", small_normal));
            cell.setBorder(Rectangle.BOTTOM | Rectangle.LEFT | Rectangle.TOP);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            personalDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase(commonForAllProd.getRound(commonForAllProd.getStringWithout_E(Double.parseDouble(SumAssuredonDeath))), small_normal));
            cell.setBorder(Rectangle.BOTTOM | Rectangle.LEFT | Rectangle.TOP);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            personalDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase("Source of Funds", small_normal));
            cell.setBorder(Rectangle.BOTTOM | Rectangle.LEFT | Rectangle.TOP);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            personalDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase(source_of_business, small_normal));
            cell.setBorder(Rectangle.BOTTOM | Rectangle.LEFT | Rectangle.TOP | Rectangle.RIGHT);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            personalDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase("Annuity Payout frequency", small_normal));
            cell.setBorder(Rectangle.BOTTOM | Rectangle.LEFT | Rectangle.TOP);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            personalDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase(mode_of_annuity_payout, small_normal));
            cell.setBorder(Rectangle.BOTTOM | Rectangle.LEFT | Rectangle.TOP);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            personalDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase("Annuity Payout Start Date", small_normal));
            cell.setBorder(Rectangle.BOTTOM | Rectangle.LEFT | Rectangle.TOP | Rectangle.RIGHT);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            personalDetail_table.addCell(cell);

            String str_annuity_payoutDate;
            if (!annuity_payoutDate.equalsIgnoreCase("")) {
                str_annuity_payoutDate = commonMethods.getDDMMYYYYDate(annuity_payoutDate);
            } else {
                str_annuity_payoutDate = "NA";
            }

            cell = new PdfPCell(new Phrase(str_annuity_payoutDate, small_normal));
            cell.setBorder(Rectangle.BOTTOM | Rectangle.LEFT | Rectangle.TOP);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            personalDetail_table.addCell(cell);


            cell = new PdfPCell(new Phrase(" " + source_of_business, small_normal));
            cell.setBorder(Rectangle.BOTTOM | Rectangle.TOP);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            //personalDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase("", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.BOTTOM | Rectangle.TOP);
            //personalDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase("", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.BOTTOM | Rectangle.RIGHT | Rectangle.TOP);
            //personalDetail_table.addCell(cell);


            //2 row
            cell = new PdfPCell(new Phrase(" First Annuitant", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.BOTTOM | Rectangle.LEFT | Rectangle.TOP);
            //personalDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase(name_of_life_assured, small_normal));
            cell.setBorder(Rectangle.BOTTOM | Rectangle.TOP);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            //personalDetail_table.addCell(cell);


            cell = new PdfPCell(new Phrase(" Age", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.BOTTOM | Rectangle.TOP);
            //personalDetail_table.addCell(cell);


            cell = new PdfPCell(new Phrase(" " + first_annuitant_age, small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.BOTTOM | Rectangle.RIGHT | Rectangle.TOP);
            //personalDetail_table.addCell(cell);

            //3 row
            if (annuity_option.equalsIgnoreCase("Life and Last Survivor - 50% Income") || annuity_option.equalsIgnoreCase("Life and Last Survivor - 100% Income") ||
                    annuity_option.equalsIgnoreCase("Life and Last Survivor with Capital Refund - 50% Income") || annuity_option.equalsIgnoreCase("Life and Last Survivor with Capital Refund - 100% Income")) {
                cell = new PdfPCell(new Phrase(" Second Annuitant", small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setBorder(Rectangle.BOTTOM | Rectangle.LEFT | Rectangle.TOP);
                //personalDetail_table.addCell(cell);

                cell = new PdfPCell(new Phrase("" + TitleSecondAnnuitant + " " + nameof_secondannuitant, small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setBorder(Rectangle.BOTTOM | Rectangle.TOP);
                //personalDetail_table.addCell(cell);

                cell = new PdfPCell(new Phrase(" Age", small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setBorder(Rectangle.BOTTOM | Rectangle.TOP);
                //personalDetail_table.addCell(cell);


                cell = new PdfPCell(new Phrase(" " + second_annuitant_age, small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setBorder(Rectangle.BOTTOM | Rectangle.RIGHT | Rectangle.TOP);
                //personalDetail_table.addCell(cell);
            }
            //4 row
            cell = new PdfPCell(new Phrase(" Annuity Option Opted", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.BOTTOM | Rectangle.LEFT | Rectangle.TOP);
            //personalDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase("" + annuity_option, small_normal));
            cell.setColspan(3);
            cell.setBorder(Rectangle.BOTTOM | Rectangle.RIGHT | Rectangle.TOP);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            //personalDetail_table.addCell(cell);

            //5 row
            cell = new PdfPCell(new Phrase(" Mode of Annuity payout", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.BOTTOM | Rectangle.LEFT | Rectangle.TOP);
            personalDetail_table.addCell(cell);


            cell = new PdfPCell(new Phrase("" + mode_of_annuity_payout, small_normal));
            cell.setColspan(3);
            cell.setBorder(Rectangle.BOTTOM | Rectangle.RIGHT | Rectangle.TOP);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            //personalDetail_table.addCell(cell);

            //6 row

            cell = new PdfPCell(new Phrase(" Annuity Amount Payable", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.BOTTOM | Rectangle.LEFT | Rectangle.TOP);
            //personalDetail_table.addCell(cell);

            if (!annuity_amount_payable.equalsIgnoreCase("") && !annuity_amount_payable.equalsIgnoreCase("0")) {
                cell = new PdfPCell(new Phrase("Rs." + currencyFormat.format(Double.parseDouble(annuity_amount_payable)), small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setBorder(Rectangle.BOTTOM | Rectangle.TOP);
                //personalDetail_table.addCell(cell);
            } else {
                cell = new PdfPCell(new Phrase("NA", small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setBorder(Rectangle.BOTTOM | Rectangle.TOP);
                //personalDetail_table.addCell(cell);
            }


            cell = new PdfPCell(new Phrase(" Annuity Payout Start Date", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.BOTTOM | Rectangle.TOP);
            //personalDetail_table.addCell(cell);

            if (isadvance_annuityPayout.equalsIgnoreCase("false")) {
                cell = new PdfPCell(new Phrase("", small_normal));
            } else {
                cell = new PdfPCell(new Phrase("" + commonMethods.getDDMMYYYYDate(annuity_payoutDate), small_normal));
            }
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.BOTTOM | Rectangle.RIGHT | Rectangle.TOP);
            //personalDetail_table.addCell(cell);

            document.add(para_address41);
            document.add(para_img_logo_after_space_1);
            document.add(personalDetail_table);
            document.add(para_img_logo_after_space_1);

            PdfPTable premDetail_table = new PdfPTable(5);
            premDetail_table.setWidths(new float[]{4f, 5f, 4f, 5f, 4f});
            premDetail_table.setWidthPercentage(100f);
            premDetail_table.setHorizontalAlignment(Element.ALIGN_CENTER);
            //7 row
            cell = new PdfPCell(new Phrase("Premium Summary", small_bold1));
            cell.setColspan(5);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            premDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase("", small_bold1));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            premDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase("Base Plan", small_bold1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            premDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase("Riders", small_bold1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            premDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase("Interest for Advancement of annuity ", small_bold1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            premDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase("Total Installment premium", small_bold1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            premDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase("Instalment Premium without applicable taxes", small_bold1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            premDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase(commonForAllProd.getRound(commonForAllProd.getStringWithout_E(Double.parseDouble(purchase_price))), small_bold1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            premDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase("Not Applicable", small_bold1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            premDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase(commonForAllProd.getRound(commonForAllProd.getStringWithout_E(Double.parseDouble(advance_premium_payable))), small_bold1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            premDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase(commonForAllProd.getRound(commonForAllProd.getStringWithout_E((Double.parseDouble(purchase_price) + Double.parseDouble(advance_premium_payable)))), small_bold1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            premDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase("Instalment Premium with First year applicable taxes", small_bold1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            premDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase(commonForAllProd.getRound(commonForAllProd.getStringWithout_E(Double.parseDouble(totalPurchasePrice))), small_bold1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            premDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase("Not Applicable", small_bold1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            premDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase(commonForAllProd.getRound(commonForAllProd.getStringWithout_E(Double.parseDouble(advance_premium_payable))), small_bold1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            premDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase(commonForAllProd.getRound(commonForAllProd.getStringWithout_E((Double.parseDouble(totalPurchasePrice) + Double.parseDouble(advance_premium_payable)))), small_bold1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            premDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase("Instalment Premium with applicable taxes 2nd Year onwards", small_bold1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            premDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase("Not Applicable", small_bold1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            premDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase("Not Applicable", small_bold1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            premDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase("Not Applicable", small_bold1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            premDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase("Not Applicable", small_bold1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            premDetail_table.addCell(cell);

            //8 row
            cell = new PdfPCell(new Phrase(" Purchase Price (Capital):", small_normal));
            cell.setBorder(Rectangle.LEFT | Rectangle.TOP);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            //premDetail_table.addCell(cell);

            String output = retVal.toString();
            cell = new PdfPCell(new Phrase("Rs." + currencyFormat.format(Double.parseDouble(prsObj.parseXmlTag(output, "purchasePrice"))), small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.RIGHT | Rectangle.TOP);
            //premDetail_table.addCell(cell);

			/*cell = new PdfPCell(new Phrase(" Total Premium Required",small_normal));
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell.setBorder(Rectangle.LEFT|Rectangle.TOP);
			premDetail_table.addCell(cell);

			cell = new PdfPCell(new Phrase(currencyFormat.format(Double.parseDouble(prsObj.parseXmlTag(output, "totalPremReq"))),small_normal));
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell.setBorder(Rectangle.RIGHT|Rectangle.TOP);
			premDetail_table.addCell(cell);*/

            //9 row
			/*cell = new PdfPCell(new Phrase(" Rider Sum Assured",small_normal));
			cell.setBorder(Rectangle.LEFT);
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			premDetail_table.addCell(cell);*/

			/*if(adbRiderStatus.equalsIgnoreCase("true"))
				cell = new PdfPCell(new Phrase("Rs."+currencyFormat.format(Double.parseDouble(prsObj.parseXmlTag(output, "riderSumAssured"))),small_normal));
			else
				cell = new PdfPCell(new Phrase("-",small_normal));
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell.setBorder(Rectangle.RIGHT);
			premDetail_table.addCell(cell);*/

            cell = new PdfPCell(new Phrase(" Vesting Amount", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.LEFT);
            //premDetail_table.addCell(cell);

            if (!vesting_amount.equalsIgnoreCase("") && !vesting_amount.equalsIgnoreCase("0")) {
                cell = new PdfPCell(new Phrase("Rs." + "" + currencyFormat.format(annuityPlusBean.getVestingAmount()), small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setBorder(Rectangle.RIGHT);
                //premDetail_table.addCell(cell);
            } else {
                cell = new PdfPCell(new Phrase("NA", small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setBorder(Rectangle.RIGHT);
                //premDetail_table.addCell(cell);
            }


            //10 row
			/*cell = new PdfPCell(new Phrase(" Rider Premium",small_normal));
			cell.setBorder(Rectangle.LEFT);
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			premDetail_table.addCell(cell);*/

			/*if(adbRiderStatus.equalsIgnoreCase("true"))
				cell = new PdfPCell(new Phrase("Rs."+currencyFormat.format(Double.parseDouble(prsObj.parseXmlTag(output, "riderPrem"))),small_normal));
			else
				cell = new PdfPCell(new Phrase("-",small_normal));
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell.setBorder(Rectangle.RIGHT);
			premDetail_table.addCell(cell);*/

            cell = new PdfPCell(new Phrase("", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
            cell.setColspan(2);
            //premDetail_table.addCell(cell);

            //11 row
            cell = new PdfPCell(new Phrase(" Applicable Tax", small_normal));
            cell.setBorder(Rectangle.LEFT);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            //premDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase("Rs." + currencyFormat.format(Double.parseDouble(prsObj.parseXmlTag(output, "totServiceTax"))), small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.RIGHT);
            //premDetail_table.addCell(cell);

			/*cell = new PdfPCell(new Phrase(" Additional premium Payable",small_normal));
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell.setBorder(Rectangle.LEFT);
			premDetail_table.addCell(cell);

			cell = new PdfPCell(new Phrase(currencyFormat.format(Double.parseDouble(prsObj.parseXmlTag(output, "addPremPayable"))),small_normal));
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell.setBorder(Rectangle.RIGHT);
			premDetail_table.addCell(cell);*/

            //12 row
            cell = new PdfPCell(new Phrase(" Advance premium Payable", small_normal));
            cell.setBorder(Rectangle.LEFT);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            //premDetail_table.addCell(cell);

            if (annuityPlusBean.getIsAdvanceAnnuityPayout())
                cell = new PdfPCell(new Phrase("Rs." + currencyFormat.format(Double.parseDouble(prsObj.parseXmlTag(output, "advancePremPayable"))), small_normal));
            else
                cell = new PdfPCell(new Phrase("-", small_normal));

            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.RIGHT);
            //premDetail_table.addCell(cell);


            cell = new PdfPCell(new Phrase("(All the above amount are inclusive of service tax,applicable for Vesting and Open market option)", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT | Rectangle.BOTTOM);
            cell.setColspan(2);
            cell.setRowspan(2);
            //premDetail_table.addCell(cell);

            //13 row
            cell = new PdfPCell(new Phrase(" Total premium", small_normal));
            cell.setBorder(Rectangle.LEFT | Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            //premDetail_table.addCell(cell);


            cell = new PdfPCell(new Phrase("Rs." + currencyFormat.format(Double.parseDouble(prsObj.parseXmlTag(output, "totalPrem"))), small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.RIGHT | Rectangle.BOTTOM);
            //premDetail_table.addCell(cell);

            document.add(premDetail_table);

            document.add(para_img_logo_after_space_1);


            PdfPTable BI_Pdftable19 = new PdfPTable(1);
            BI_Pdftable19.setWidthPercentage(100);
            PdfPCell BI_Pdftable19_cell1 = new PdfPCell(new Paragraph(
                    "(Amount in Rs.)",
                    small_bold));
            BI_Pdftable19_cell1.setHorizontalAlignment(Element.ALIGN_RIGHT);
            BI_Pdftable19_cell1.setPadding(5);

            BI_Pdftable19.addCell(BI_Pdftable19_cell1);
            document.add(BI_Pdftable19);

            PdfPTable Table_BI_Header = new PdfPTable(8);
            Table_BI_Header.setWidthPercentage(100);
            PdfPCell cell_EndOfYear = new PdfPCell(new Paragraph("policy Year",
                    small_bold2));
            cell_EndOfYear.setPadding(5);
            cell_EndOfYear.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell_EndOfYear.setRowspan(3);

            PdfPCell cell_YearlyPremiumPaid = new PdfPCell(new Paragraph(
                    "Single / Annualized premium", small_bold2));
            cell_YearlyPremiumPaid.setPadding(5);
            cell_YearlyPremiumPaid.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell_YearlyPremiumPaid.setRowspan(3);

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
                    new Paragraph("Maturity/Vesting Benefit",
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
            Table_BI_Header.addCell(cell_EndOfYear);
            Table_BI_Header.addCell(cell_YearlyPremiumPaid);
            Table_BI_Header.addCell(cell_YearlyPremiumPaid2);
            Table_BI_Header.addCell(cell_YearlyPremiumPaid3);
            Table_BI_Header.addCell(cell_CummulativePremiumPaid);
            Table_BI_Header.addCell(cell_CummulativePremiumPaid2);
            Table_BI_Header.addCell(cell_GuarantedMaturityBenefit);
            Table_BI_Header.addCell(cell_GuarantedDeathBenefit);
            Table_BI_Header.addCell(cell_GuarantedSurrenderValue);
            Table_BI_Header.addCell(cell_GuarantedSurrenderValue2);
            document.add(Table_BI_Header);

            float[] columnWidthsBI_Header1 = {1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f};
            // for (int i = 0; i < list_data.size(); i++) {
            for (int i = 1; i <= 25; i++) {

                PdfPTable Table_BI_Header2 = new PdfPTable(8);

                Table_BI_Header2.setWidthPercentage(100);
                Table_BI_Header2.setWidths(columnWidthsBI_Header1);
                // PdfPCell cell_EndOfYear3 = new PdfPCell(new
                // Paragraph(list_data
                // .get(i).getPolicy_year(), small_bold2));

                String end_of_year;
                end_of_year = prsObj.parseXmlTag(result_grid, "policyYr" + i);
                if (i == 25) {
                    end_of_year = "Till Death";
                }
                PdfPCell cell_EndOfYear3 = new PdfPCell(new Phrase(
                        end_of_year,
                        small_bold2));
                cell_EndOfYear3.setPadding(5);
                cell_EndOfYear3.setHorizontalAlignment(Element.ALIGN_CENTER);

                PdfPCell cell_AnnPrem = new PdfPCell(new Phrase(
                        currencyFormat.format(Double.parseDouble(prsObj
                                .parseXmlTag(result_grid, "Annualized_premium" + i))),
                        small_bold2));
                cell_AnnPrem.setPadding(5);
                cell_AnnPrem.setHorizontalAlignment(Element.ALIGN_CENTER);

                PdfPCell cell_cummulativePremiumPaid = new PdfPCell(new Phrase(
                        currencyFormat.format(Double.parseDouble(((prsObj.parseXmlTag(result_grid,
                                "Survival_Benefit" + i + "") == null) || (prsObj
                                .parseXmlTag(result_grid,
                                        "Survival_Benefit" + i + "")
                                .equals(""))) ? "0" : prsObj
                                .parseXmlTag(result_grid,
                                        "Survival_Benefit" + i + ""))), small_bold2));
                cell_cummulativePremiumPaid.setPadding(5);
                cell_cummulativePremiumPaid
                        .setHorizontalAlignment(Element.ALIGN_CENTER);

                PdfPCell cell_cummulativePremiumPaid2 = new PdfPCell(new Phrase(
                        currencyFormat.format(Double.parseDouble(((prsObj.parseXmlTag(result_grid,
                                "Other_Benifits" + i + "") == null) || (prsObj
                                .parseXmlTag(result_grid,
                                        "Other_Benifits" + i + "")
                                .equals(""))) ? "0" : prsObj
                                .parseXmlTag(result_grid,
                                        "Other_Benifits" + i + ""))), small_bold2));
                cell_cummulativePremiumPaid2.setPadding(5);
                cell_cummulativePremiumPaid2
                        .setHorizontalAlignment(Element.ALIGN_CENTER);

               /* PdfPCell cell_guarantedDeathBenefit = new PdfPCell(new Phrase(
                        currencyFormat.format(Double.parseDouble(prsObj
                                .parseXmlTag(result_grid, "Death_Benifits"
                                        + (i + 1)))), small_bold2));
                cell_guarantedDeathBenefit.setPadding(5);
                cell_guarantedDeathBenefit
                        .setHorizontalAlignment(Element.ALIGN_CENTER);*/


                PdfPCell cell_guarantedDeathBenefit = new PdfPCell(new Phrase(
                        prsObj
                                .parseXmlTag(result_grid, "Death_Benifits"
                                        + i + ""), small_bold2));
                cell_guarantedDeathBenefit.setPadding(5);
                cell_guarantedDeathBenefit
                        .setHorizontalAlignment(Element.ALIGN_CENTER);

                PdfPCell cell_BenefitPayableAtMaturity = new PdfPCell(
                        new Phrase(prsObj.parseXmlTag(result_grid, "Maturity_Benifits" + i + ""),
                                small_bold2));
                cell_BenefitPayableAtMaturity.setPadding(5);
                cell_BenefitPayableAtMaturity
                        .setHorizontalAlignment(Element.ALIGN_CENTER);

                PdfPCell cell_guarantedSurrenderValue = new PdfPCell(
                        new Phrase(prsObj.parseXmlTag(result_grid, "Min_Guaranteed_Surr_Value" + i + ""),
                                small_bold2));
                cell_guarantedSurrenderValue.setPadding(5);
                cell_guarantedSurrenderValue
                        .setHorizontalAlignment(Element.ALIGN_CENTER);

                PdfPCell cell_guarantedSurrenderValue2 = new PdfPCell(
                        new Phrase(prsObj.parseXmlTag(result_grid,
                                "Special_Surr_Value" + i + ""),
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
            document.add(para_img_logo_after_space_1);
            String annuity_plus_annuity_amount_payable = "";
            if (opt_for.equalsIgnoreCase("Annuity Payout Amount")) {
                if (!annuity_amount.equalsIgnoreCase("") && !annuity_amount.equalsIgnoreCase("0")) {
                } else {
                }

            } else if (opt_for.equalsIgnoreCase("Premium Amount")) {
                if (!annuity_amount_payable.equalsIgnoreCase("") && !annuity_amount_payable.equalsIgnoreCase("0")) {
                } else {
                }

            }
            PdfPTable BI_Pdf_annuity_payable1 = new PdfPTable(2);
            BI_Pdf_annuity_payable1.setWidthPercentage(100);
            // CR_table6.setWidths(columnWidths_26);
            PdfPCell BI_Pdf_annuity_payable1_cell11 = new PdfPCell(
                    new Paragraph(
                            "Annuity payable p.a. based on prevailing annuity rates (Rs)",
                            small_normal));

            BI_Pdf_annuity_payable1_cell11.setPadding(5);


            PdfPCell BI_Pdf_annuity_payable1_cell12 = new PdfPCell(
                    new Paragraph(
                            SurvivalBenefits_annuity_plus_annuity_amount_payable,
                            small_bold));

            BI_Pdf_annuity_payable1_cell12.setPadding(5);
            BI_Pdf_annuity_payable1_cell12.setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_Pdf_annuity_payable1.addCell(BI_Pdf_annuity_payable1_cell11);
            BI_Pdf_annuity_payable1.addCell(BI_Pdf_annuity_payable1_cell12);
            document.add(BI_Pdf_annuity_payable1);


            PdfPTable BI_Pdf_annuity_payable2 = new PdfPTable(2);
            BI_Pdf_annuity_payable2.setWidthPercentage(100);
            // CR_table6.setWidths(columnWidths_26);
            PdfPCell BI_Pdf_annuity_payable2_cell11 = new PdfPCell(
                    new Paragraph(
                            "Annuity Option Selected (the Option can be changed any time before vesting)",
                            small_normal));

            BI_Pdf_annuity_payable2_cell11.setPadding(5);


            PdfPCell BI_Pdf_annuity_payable2_cell12 = new PdfPCell(
                    new Paragraph(
                            "Not Applicable",
                            small_bold));

            BI_Pdf_annuity_payable2_cell12.setPadding(5);
            BI_Pdf_annuity_payable2_cell12.setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_Pdf_annuity_payable2.addCell(BI_Pdf_annuity_payable2_cell11);
            BI_Pdf_annuity_payable2.addCell(BI_Pdf_annuity_payable2_cell12);
            document.add(BI_Pdf_annuity_payable2);


            document.add(para_img_logo_after_space_1);

            PdfPTable table2 = new PdfPTable(1);
            table2.setWidths(new float[]{1f});
            table2.setWidthPercentage(100);
            table2.setHorizontalAlignment(Element.ALIGN_LEFT);

            cell = new PdfPCell(new Phrase("Notes: ", small_bold));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.RIGHT | Rectangle.BOTTOM | Rectangle.LEFT | Rectangle.TOP);
            table2.addCell(cell);
            //1st Disclaimers

            cell = new PdfPCell(new Phrase("1. The values shown above are for illustration purpose only. The actual annuity amount receivable depends on the prevailing annuity rates at the time of purchase of annuity. ", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT | Element.ALIGN_JUSTIFIED);
            cell.setBorder(Rectangle.RIGHT | Rectangle.BOTTOM | Rectangle.LEFT | Rectangle.TOP);
            table2.addCell(cell);

            //2 Disclaimers

            cell = new PdfPCell(new Phrase(" 2. Annualized premium excludes underwriting extra premium, the premiums paid towards the riders, if any, and Goods & Services Tax. ", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.RIGHT | Rectangle.BOTTOM | Rectangle.LEFT | Rectangle.TOP);
            table2.addCell(cell);

            //3 Disclaimers


            cell = new PdfPCell(new Phrase(" 3. Refer sales literature for explanation of terms used in this illustration.", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.RIGHT | Rectangle.BOTTOM | Rectangle.LEFT | Rectangle.TOP);
            table2.addCell(cell);

            cell = new PdfPCell(new Phrase(" 4. For Capital Refund options, after death of the annuitant, the Purchase Price (Capital) will be refunded to the nominee.", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.RIGHT | Rectangle.BOTTOM | Rectangle.LEFT | Rectangle.TOP);
            //table2.addCell(cell);

            Paragraph para_address42 = new Paragraph(
                    "You may receive a Welcome Call from our representative to confirm your proposal details like Date of Birth, Nominee Name, Address, Email ID, Premium Amount, Payout frequency, etc. ",
                    small_bold);
            para_address42.setAlignment(Element.ALIGN_CENTER);

            document.add(table2);
            document.add(para_img_logo_after_space_1);
            document.add(para_address42);
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
                                    + "     having received the information with respect to the above, have understood the above statement before entering into the contract.",
                            small_bold));

            BI_Pdftable26_cell1.setPadding(5);

            BI_Pdftable26.addCell(BI_Pdftable26_cell1);
            //document.add(BI_Pdftable26);
            //document.add(BI_Pdftable26_cell1);

            /*PdfPTable BI_Pdftabe_Customer_signature_signature = new PdfPTable(2);
            BI_Pdftabe_Customer_signature_signature.setWidthPercentage(100);

            if (place2.equals("")) {
                String place2_pdf = "";
                place2 = place2_pdf;
            }

            PdfPCell BI_Pdftabe_Customer_signature_signature_1 = new PdfPCell(
                    new Paragraph("Place :" + place2, small_normal));
            PdfPCell BI_Pdftabe_Customer_signature_signature_2 = new PdfPCell(
                    new Paragraph("Date  :" + CurrentDate, small_normal));


            BI_Pdftabe_Customer_signature_signature_1.setPadding(5);
            BI_Pdftabe_Customer_signature_signature_2.setPadding(5);

            BI_Pdftabe_Customer_signature_signature
                    .addCell(BI_Pdftabe_Customer_signature_signature_1);
            BI_Pdftabe_Customer_signature_signature
                    .addCell(BI_Pdftabe_Customer_signature_signature_2);

            document.add(BI_Pdftabe_Customer_signature_signature);
            document.add(para_img_logo_after_space_1);*/


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

            PdfPTable BI_Pdftable261 = new PdfPTable(1);
            BI_Pdftable261.setWidthPercentage(100);
            // CR_table6.setWidths(columnWidths_26);

            PdfPCell BI_Pdftable26_cell11 = new PdfPCell(
                    new Paragraph(
                            "I, "
                                    + commonMethods.getUserName(context)
                                    + " have explained the premiums and benefits under the product fully to the prospect/policyholder.",
                            small_bold));

            BI_Pdftable26_cell11.setPadding(5);

            BI_Pdftable261.addCell(BI_Pdftable26_cell11);
            document.add(BI_Pdftable261);

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

        } catch (Exception e) {
            // TODO: handle exception
            //Log.e(getLocalClassName(), e.toString() + "Error in creating Pdf");
            System.out.println("error " + e.getMessage());
            e.printStackTrace();

            //	return false;

        }

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
//						ProductHomePageActivity.customer_Signature = CaptureSignature.scaled;
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
                        imageButtonShubhNiveshProposerPhotograph.setImageBitmap(scaled);
                    } else {
                        photoBitmap = null;
                    }
                }
            }
        }
    }


    private void addListenerOnSubmit() {

        annuityPlusBean = new AnnuityPlusBean();

        if (cb_staffdisc.isChecked()) {
            annuityPlusBean.setIsStaff(true);
        } else {
            annuityPlusBean.setIsStaff(false);
        }

        if (cb_kerladisc.isChecked()) {
            annuityPlusBean.setKerlaDisc(true);
            annuityPlusBean.setServiceTax(true);

        } else {
            annuityPlusBean.setKerlaDisc(false);
            annuityPlusBean.setServiceTax(false);
        }


        annuityPlusBean.setIsJKresident(false);

		/*annuityPlusBean.setStr_annuity_plus_purchase_annuity_for(spnr_bi_annuity_plus_purchase_annuity_for.getSelectedItem().toString());

		annuityPlusBean.setStr_annuity_plus_immediate_annuity_plan_for(spnr_bi_annuity_plus_immediate_annuity_plan_for.getSelectedItem().toString());*/

        annuityPlusBean.setAnnuityOption(spnr_bi_annuity_plus_annuity_option.getSelectedItem().toString());

        if (spnr_bi_annuity_plus_annuity_option.getSelectedItem().toString().equals("Life and Last Survivor - 50% Income") || spnr_bi_annuity_plus_annuity_option.getSelectedItem().toString().equals("Life and Last Survivor - 100% Income") || spnr_bi_annuity_plus_annuity_option.getSelectedItem().toString().equals("Life and Last Survivor with Capital Refund - 50% Income") || spnr_bi_annuity_plus_annuity_option.getSelectedItem().toString().equals("Life and Last Survivor with Capital Refund - 100% Income")) {
            annuityPlusBean.setFirstNameOfSecondAnnuitant(edt_bi_annuity_plus_life_assured_first_name_second_annuitant
                    .getText().toString());
            annuityPlusBean.setMiddleNameOfSecondAnnuitant(edt_bi_annuity_plus_life_assured_middle_name_second_annuitant
                    .getText().toString());
            annuityPlusBean.setLastNameOfSecondAnnuitant(edt_bi_annuity_plus_life_assured_last_name_second_annuitant
                    .getText().toString());
            annuityPlusBean.setAgeOfSecondAnnuitant(Integer.parseInt(edt_bi_annuity_plus_life_assured_age_second_annuitant
                    .getText().toString()));
            annuityPlusBean.setDobOfSecondAnnuitant(second_Annuitant_date_of_birth);
            annuityPlusBean.setGenderOfSecondAnnuitant(gender_2);
        }


        if (spnr_bi_annuity_plus_source_of_business.getSelectedItem().toString().equals("Vesting/Death/Surrender of existing SBI Life's pension policy")) {
            if (spnr_bi_annuity_plus_opt_for.getSelectedItem().toString().equals("Annuity Payout Amount")) {
                annuityPlusBean.setAnnuityAmount(Double.parseDouble(edt_annuity_plus_annuity_amount.getText().toString()));
                annuityPlusBean.setVestingAmount(Double.parseDouble(edt_annuity_plus_vesting_amount.getText().toString()));
            } else if (spnr_bi_annuity_plus_opt_for.getSelectedItem().toString().equals("Premium Amount")) {
                annuityPlusBean.setVestingAmount(Double.parseDouble(edt_annuity_plus_vesting_amount.getText().toString()));
                annuityPlusBean.setAdditionalAmountIfAny(Double.parseDouble(edt_annuity_plus_additional_amount_if_any.getText().toString()));
            }
        }
        //Other than Vesting of SBI Life's Pension Policy
        else {
            if (spnr_bi_annuity_plus_opt_for.getSelectedItem().toString().equals("Annuity Payout Amount")) {
                annuityPlusBean.setAnnuityAmount(Double.parseDouble(edt_annuity_plus_annuity_amount.getText().toString()));
                annuityPlusBean.setVestingAmount(0);
            } else if (spnr_bi_annuity_plus_opt_for.getSelectedItem().toString().equals("Premium Amount")) {
                annuityPlusBean.setAnnuityAmount(0);
                annuityPlusBean.setVestingAmount(Double.parseDouble(edt_annuity_plus_vesting_amount.getText().toString()));
            }
        }


        annuityPlusBean.setAgeOfFirstAnnuitant(Integer.parseInt(edt_bi_annuity_plus_life_assured_age
                .getText().toString()));
        annuityPlusBean.setGenderOfFirstAnnuitant(gender);

        annuityPlusBean.setSourceOfBusiness(spnr_bi_annuity_plus_source_of_business.getSelectedItem().toString());

        annuityPlusBean.setChannelDetails(spnr_bi_annuity_plus_channel_details.getSelectedItem().toString());

        annuityPlusBean.setModeOfAnnuityPayout(spnr_bi_annuity_plus_mode_of_annuity_payouts.getSelectedItem().toString());

        annuityPlusBean.setAnnuityOption(spnr_bi_annuity_plus_annuity_option.getSelectedItem().toString());

        annuityPlusBean.setOptFor(spnr_bi_annuity_plus_opt_for.getSelectedItem().toString());

        if ((ModeOfAnnuityPayout.equals("Half Yearly") || ModeOfAnnuityPayout.equals("Yearly")) && cb_bi_annuity_plus_advance_annuity_payout.isChecked()) {
            annuityPlusBean.setIsAdvanceAnnuityPayout(true);
        } else if ((ModeOfAnnuityPayout.equals("Half Yearly") || ModeOfAnnuityPayout.equals("Yearly")) && !cb_bi_annuity_plus_advance_annuity_payout.isChecked()) {
            annuityPlusBean.setIsAdvanceAnnuityPayout(false);
        }


        try {
            annuityPlusBean.setProposalDate(new SimpleDateFormat("MM-dd-yyyy", Locale.ENGLISH).parse(proposal_date));
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if (cb_bi_annuity_plus_advance_annuity_payout.isChecked()) {
            try {
                annuityPlusBean.setAnnuityPayoutDate(new SimpleDateFormat("MM-dd-yyyy", Locale.ENGLISH).parse(annuity_plus_advance_annuity_payout_from_which_date));
                annuityPlusBean.setIsAdvanceAnnuityPayout(cb_bi_annuity_plus_advance_annuity_payout.isChecked());
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }


        //Adb
        if (cb_bi_annuity_plus_adb_rider.isChecked()) {
            annuityPlusBean.setApplicableFor(spnr_bi_annuity_plus_applicable_for.getSelectedItem().toString());
            annuityPlusBean.setIsADB(cb_bi_annuity_plus_adb_rider.isChecked());
        }


        showAnnuityPlusBeanOutputPg(annuityPlusBean); //commented for service based

    }


    /*public void showAnnuityPlusBeanOutputPg(AnnuityPlusBean annuityPlusBean)
	{
		AnnuityPlusBusinessLogic bl = new AnnuityPlusBusinessLogic(annuityPlusBean);

		retVal=new StringBuilder();
		val_annuity_payout_date=valAnnuityPayoutDate(annuityPlusBean);
		val_annuity_amount=validateMinAnnuityAmount(annuityPlusBean);

//			System.out.println("valAnnuityPayoutDate "+valAnnuityPayoutDate);
//			System.out.println("validateMinAnnuityAmount "+validateMinAnnuityAmount);
		//Validate annuity payout date and minimum Annuity Amount

		retVal.append("<?xml version='1.0' encoding='utf-8' ?><annuityPlus>");

		if(val_annuity_payout_date && val_annuity_amount)
		{


			double riderSumAssured=0,riderPremium=0,totServiceTax=0,advancePremPayable=0,riderPrem=0;;

			String netPremium="0",staffStatus="";
			//vrushali

			String purchasePrice=commonForAllProd.roundUp_Level2_Ceil(""+commonForAllProd.getStringWithout_E(bl.getPurchasePrice()));

//				System.out.println("purchase price "+purchasePrice);
			String purchasePriceRoundUp=commonForAllProd.getRoundUp(purchasePrice);
			if(annuityPlusBean.getIsADB())
			{
				try {
					riderSumAssured = bl.getRiderSumAssured();
					riderPremium = bl.getRiderPremiumFinal(riderSumAssured);
					riderPrem = bl.getRiderPremium(riderSumAssured);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
//vrushali
			netPremium=commonForAllProd.getRoundOffLevel2New(commonForAllProd.getStringWithout_E(Double.parseDouble(purchasePrice)+riderPremium));
			String basicServiceTax,SBC_serviceTax,KKC_serviceTax,serviceTaxOnPurchasePrice;

			*//*if(annuityPlusBean.getOptFor().equals("Annuity Payout Amount"))
			{
				basicServiceTax=commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(bl.getServiceTaxOnPurchasePrice("basic")+(bl.getServiceTaxOnRider(riderSumAssured,"basic")*bl.getRiderFactor())));
				SBC_serviceTax=commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(bl.getServiceTaxOnPurchasePrice("SBC")+(bl.getServiceTaxOnRider(riderSumAssured,"SBC")*bl.getRiderFactor())));
				KKC_serviceTax=commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(bl.getServiceTaxOnPurchasePrice("KKC")+(bl.getServiceTaxOnRider(riderSumAssured,"KKC")*bl.getRiderFactor())));
			}
			else
			{
				basicServiceTax=commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(bl.getServiceTaxOnPurchasePrice("basic")+(bl.getServiceTaxOnRider(riderSumAssured,"basic")*bl.getRiderFactor())));
				SBC_serviceTax=commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(bl.getServiceTaxOnPurchasePrice("SBC")+(bl.getServiceTaxOnRider(riderSumAssured,"SBC")*bl.getRiderFactor())));
				KKC_serviceTax=commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(bl.getServiceTaxOnPurchasePrice("KKC")+(bl.getServiceTaxOnRider(riderSumAssured,"KKC")*bl.getRiderFactor())));

//					 basicServiceTax=commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(bl.getServiceTaxForPremAmtOption("basic")+(bl.getServiceTaxOnRider(riderSumAssured,"basic")*bl.getRiderFactor())));
//					 SBC_serviceTax=commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(bl.getServiceTaxForPremAmtOption("SBC")+(bl.getServiceTaxOnRider(riderSumAssured,"SBC")*bl.getRiderFactor())));
//					 KKC_serviceTax=commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(bl.getServiceTaxForPremAmtOption("KKC")+(bl.getServiceTaxOnRider(riderSumAssured,"KKC")*bl.getRiderFactor())));
			}*//*

			if(annuityPlusBean.getOptFor().equals("Annuity Payout Amount"))
			{
				basicServiceTax=commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(bl.getServiceTaxOnPurchasePrice("basic",annuityPlusBean.getServiceTax())+(bl.getServiceTaxOnRider(riderSumAssured,"basic",annuityPlusBean.getServiceTax())*bl.getRiderFactor())));
				SBC_serviceTax=commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(bl.getServiceTaxOnPurchasePrice("SBC",annuityPlusBean.getServiceTax())+(bl.getServiceTaxOnRider(riderSumAssured,"SBC",annuityPlusBean.getServiceTax())*bl.getRiderFactor())));
				KKC_serviceTax=commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(bl.getServiceTaxOnPurchasePrice("KKC",annuityPlusBean.getServiceTax())+(bl.getServiceTaxOnRider(riderSumAssured,"KKC",annuityPlusBean.getServiceTax())*bl.getRiderFactor())));
				serviceTaxOnPurchasePrice= commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(bl.getServiceTaxOnPurchasePrice("basic",annuityPlusBean.getServiceTax())+bl.getServiceTaxOnPurchasePrice("SBC",annuityPlusBean.getServiceTax())+bl.getServiceTaxOnPurchasePrice("KKC",annuityPlusBean.getServiceTax())));

			}
			else
			{
				basicServiceTax=commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(bl.getServiceTax1()+(bl.getServiceTaxOnRider(riderSumAssured,"basic",annuityPlusBean.getServiceTax())*bl.getRiderFactor())));
				SBC_serviceTax=commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(0+(bl.getServiceTaxOnRider(riderSumAssured,"SBC",annuityPlusBean.getServiceTax())*bl.getRiderFactor())));
				KKC_serviceTax=commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(0+(bl.getServiceTaxOnRider(riderSumAssured,"KKC",annuityPlusBean.getServiceTax())*bl.getRiderFactor())));
				serviceTaxOnPurchasePrice= commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(bl.getServiceTaxOnPurchasePrice1("basic",annuityPlusBean.getServiceTax())+bl.getServiceTaxOnPurchasePrice1("SBC",annuityPlusBean.getServiceTax())+bl.getServiceTaxOnPurchasePrice1("KKC",annuityPlusBean.getServiceTax())));
//					 basicServiceTax=commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(bl.getServiceTaxForPremAmtOption("basic")+(bl.getServiceTaxOnRider(riderSumAssured,"basic")*bl.getRiderFactor())));
//					 SBC_serviceTax=commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(bl.getServiceTaxForPremAmtOption("SBC")+(bl.getServiceTaxOnRider(riderSumAssured,"SBC")*bl.getRiderFactor())));
//					 KKC_serviceTax=commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(bl.getServiceTaxForPremAmtOption("KKC")+(bl.getServiceTaxOnRider(riderSumAssured,"KKC")*bl.getRiderFactor())));
			}

//				System.out.println(commonForAllProd.getStringWithout_E(bl.getServiceTaxForPremAmtOption("basic")+(bl.getServiceTaxOnRider(riderSumAssured,"basic")*bl.getRiderFactor())));
//				System.out.println(commonForAllProd.getStringWithout_E(bl.getServiceTaxForPremAmtOption("SBC")+(bl.getServiceTaxOnRider(riderSumAssured,"SBC")*bl.getRiderFactor())));


			double totalPurchasePrice=Double.parseDouble(purchasePriceRoundUp)+Double.parseDouble(serviceTaxOnPurchasePrice);
			totServiceTax=Double.parseDouble(basicServiceTax)+Double.parseDouble(SBC_serviceTax)+Double.parseDouble(KKC_serviceTax);

			bl.setTotalServiceTax(totServiceTax);

			long factor=0;


			if((annuityPlusBean.getModeOfAnnuityPayout().equals("Yearly") || annuityPlusBean.getModeOfAnnuityPayout().equals("Half Yearly")) && annuityPlusBean.getIsAdvanceAnnuityPayout())
			{
//					System.out.println("Annuity Payout Date -> "+annuityPlusBean.getAnnuityPayoutDate().toString());
				if(annuityPlusBean.getModeOfAnnuityPayout().equals("Yearly"))
				{

					factor=annuityPlusBean.getAnnuityPayoutDate().getTime()-31536000000L;
//						System.out.println("factor Y "+factor);
				}
				else
				{
					factor=annuityPlusBean.getAnnuityPayoutDate().getTime()-15768000000L;
//						System.out.println("factor H "+factor);
				}
			}
			//System.out.println("@2");
			idealDate = new Date(factor);
//				System.out.println("idealDate "+idealDate);
			//System.out.println("@3");
			int intAnnuityPayoutDate=0,intIdealDate=0;
			long noOfDaysForIntCaln=0;

			if((annuityPlusBean.getModeOfAnnuityPayout().equals("Yearly")  || annuityPlusBean.getModeOfAnnuityPayout().equals("Half Yearly")) && annuityPlusBean.getIsAdvanceAnnuityPayout())
			{
				intAnnuityPayoutDate=Integer.parseInt(annuityPlusBean.getAnnuityPayoutDate().toString().substring(8,10));
				intIdealDate=Integer.parseInt(idealDate.toString().substring(8,10));

//					System.out.println(" Initial intAnnuityPayoutDate -> "+ intAnnuityPayoutDate);
//					System.out.println("Initial intIdealDate -> "+ intIdealDate);

				//To match with escel date comparison
				for(int i=1;i<10;i++)
				{
					//System.out.println("** i -> "+i);
					if(intAnnuityPayoutDate > intIdealDate)
					{
						idealDate = new Date(factor+86400000*i);
						intIdealDate=Integer.parseInt(idealDate.toString().substring(8,10));
						//System.out.println("In for loop -> Initial intAnnuityPayoutDate -> "+ intAnnuityPayoutDate);
						//System.out.println("In for loop -> Initial intIdealDate -> "+ intIdealDate);
					}
					else if(intAnnuityPayoutDate < intIdealDate)
					{
						idealDate = new Date(factor-86400000*i);
						intIdealDate=Integer.parseInt(idealDate.toString().substring(8,10));
						//System.out.println("In for loop -> Initial intAnnuityPayoutDate -> "+ intAnnuityPayoutDate);
						//System.out.println("In for loop -> Initial intIdealDate -> "+ intIdealDate);
					}
					else if(intAnnuityPayoutDate == intIdealDate)
					{break;}
				}

//					System.out.println("*********   IdealDate -> "+idealDate.toString());
//					System.out.println("@4");
//					System.out.println("@5");
//					System.out.println("Annuity Payout Dates Part => "+3);
//					System.out.println("Ideal Dates Part => "+intIdealDate);
//					System.out.println("@6");
//					System.out.println("proposal date " + annuityPlusBean.getProposalDate().toString());
//					System.out.println("days diff "+(commonForAllProd.getDaysDiff(annuityPlusBean.getProposalDate(), idealDate)));

				if(annuityPlusBean.getModeOfAnnuityPayout().equals("Yearly"))
				{
					noOfDaysForIntCaln=(commonForAllProd.getDaysDiff(annuityPlusBean.getProposalDate(), idealDate))+1;
				}
				else if(annuityPlusBean.getModeOfAnnuityPayout().equals("Half Yearly"))
				{
					noOfDaysForIntCaln=(commonForAllProd.getDaysDiff(annuityPlusBean.getProposalDate(), idealDate))+2;
				}
				//System.out.println("@7");
//					System.out.println("noOfDaysForIntCaln -> "+noOfDaysForIntCaln);
			}

//				System.out.println("*** noOfDaysForIntCaln [BT96]-> "+noOfDaysForIntCaln);



			noOfDaysForIntCalculation=noOfDaysForIntCaln;
			//System.out.println("@8");
			advancePremPayable=bl.getAdvancePremiumPayabale(noOfDaysForIntCaln);
			String totalPremium=commonForAllProd.getRound(commonForAllProd.getStringWithout_E(bl.getTotalPremium(Double.parseDouble(purchasePrice),advancePremPayable)));
			//System.out.println("Total Premium  ==>>  "+totalPremium);
			String totalPremiumRequired=commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(bl.getTotalPremiumRequired(Double.parseDouble(totalPremium))));
			//System.out.println("Total Premium Payable   ==>>    "+totalPremiumRequired);

			String additionalPremiumPayable=commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(bl.additionalPremiumPayableFinal(Double.parseDouble(totalPremiumRequired),Double.parseDouble(totalPremium),Double.parseDouble(purchasePrice),advancePremPayable)));
			//System.out.println("@9");
			//For Premium Amount Option
			//System.out.println("E18  ->  getTotalAmt()     -> "+bl.getTotalAmt());
			//System.out.println("E18  ->  getServiceTax()     -> "+bl.getServiceTax());
			//System.out.println("E34  ->  getAmtAfterServiceTax()    -> "+bl.getAmtAfterServiceTax());
			//System.out.println("@10");




			String annuityAmt=commonForAllProd.getRound(commonForAllProd.getStringWithout_E(bl.getAnnuityAmtForSelectedFreq()));
			*//*

			if(annuityPlusBean.getIsStaff())
			{
				staffStatus = "sbi";
				//disc_Basic_SelFreq
			}
			else
				staffStatus = "none";


			discountPercentage = bl.getStaffRebate(cb_staffdisc.isChecked()) + "";



			retVal.append("<errCode>0</errCode>");
			retVal.append("<staffStatus>" + staffStatus + "</staffStatus>");
			retVal.append("<staffRebate>" + discountPercentage + "</staffRebate>");

			retVal.append("<modeOfAnnuityPayout>"+annuityPlusBean.getModeOfAnnuityPayout()+"</modeOfAnnuityPayout>");
			//vrushali

			retVal.append("<annuityAmtPayable>"+commonForAllProd.getStringWithout_E(Double.parseDouble(annuityAmt))+"</annuityAmtPayable>");

			retVal.append("<purchasePrice>"+commonForAllProd.getStringWithout_E(Double.parseDouble(purchasePrice)) +"</purchasePrice>");

			retVal.append("<totalPurchasePrice>"+commonForAllProd.getStringWithout_E(totalPurchasePrice)+"</totalPurchasePrice>");

			if(annuityPlusBean.getIsADB())
			{
				retVal.append("<riderSumAssured>"+commonForAllProd.getStringWithout_E(riderSumAssured)+"</riderSumAssured>");
				retVal.append("<riderPrem>"+commonForAllProd.getStringWithout_E(riderPremium)+"</riderPrem>");
				//vrushali
				retVal.append("<riderPrem1>"+commonForAllProd.getStringWithout_E(riderPrem)+"</riderPrem1>");
			}
			else
			{
				retVal.append("<riderSumAssured>0</riderSumAssured>");
				retVal.append("<riderPrem>0</riderPrem>");
				retVal.append("<riderPrem1>0</riderPrem1>");
			}
			//vrushali
			retVal.append("<netPremium>"+netPremium+"</netPremium>");
			retVal.append("<basicServiceTax>"+basicServiceTax+"</basicServiceTax>");

			retVal.append("<SBC_serviceTax>"+SBC_serviceTax+"</SBC_serviceTax>");

			retVal.append("<KKC_serviceTax>"+KKC_serviceTax+"</KKC_serviceTax>");

			retVal.append("<totServiceTax>"+commonForAllProd.getStringWithout_E(totServiceTax)+"</totServiceTax>");

			if(annuityPlusBean.getIsAdvanceAnnuityPayout())
			{
				retVal.append("<advancePremPayable>"+commonForAllProd.getStringWithout_E(advancePremPayable)+"</advancePremPayable>");
			}
			else
			{
				retVal.append("<advancePremPayable>0</advancePremPayable>");
			}

			retVal.append("<totalPrem>"+ commonForAllProd.getStringWithout_E(Double.parseDouble(totalPremium))+"</totalPrem>");

			if(annuityPlusBean.getSourceOfBusiness().equals("Vesting/Death/Surrender of existing SBI Life's pension policy") || annuityPlusBean.getSourceOfBusiness().equals("Open Market Option (Any Other Life Insurance Company Pension Policy)"))
			{
				retVal.append("<totalPremReq>"+commonForAllProd.getStringWithout_E(Double.parseDouble(totalPremiumRequired))+"</totalPremReq>");
				retVal.append("<addPremPayable>"+commonForAllProd.getStringWithout_E(Double.parseDouble(additionalPremiumPayable))+"</addPremPayable>");
			}
			else
			{
				retVal.append("<totalPremReq>0</totalPremReq>");
				retVal.append("<addPremPayable>0</addPremPayable>");
			}



		}
		else
		{
			if(val_annuity_payout_date)
			{
				retVal.append("<errCode>1</errCode>");
				retVal.append("<annuityPayoutDateError>"+val_annuity_payout_date+"</annuityPayoutDateError>");
			}
			if(val_annuity_amount)
			{
				retVal.append("<errCode>1</errCode>");
				retVal.append("<minAnnuityAmountError>"+val_annuity_amount+"</minAnnuityAmountError>");
			}
		}
		retVal.append("</annuityPlus>");
//			System.out.println("Final output in xml" + retVal.toString());
		//return retVal.toString();


		*/

    /****************************** Output ends here *****************************************************//*


	}*/
    private void showAnnuityPlusBeanOutputPg(AnnuityPlusBean annuityPlusBean) {


        retVal = new StringBuilder();
        val_annuity_payout_date = valAnnuityPayoutDate(annuityPlusBean);
        val_annuity_amount = validateMinAnnuityAmount(annuityPlusBean);

//			System.out.println("valAnnuityPayoutDate "+valAnnuityPayoutDate);
//			System.out.println("validateMinAnnuityAmount "+validateMinAnnuityAmount);
        //Validate annuity payout date and minimum Annuity Amount

        retVal.append("<?xml version='1.0' encoding='utf-8' ?><annuityPlus>");

        if (val_annuity_payout_date && val_annuity_amount) {


            //double riderSumAssured=0,riderPremium=0,totServiceTax=0,advancePremPayable=0,riderPrem=0;;

            //String netPremium="0",staffStatus="";
            //vrushali

            //String purchasePrice=commonForAllProd.roundUp_Level2_Ceil(""+commonForAllProd.getStringWithout_E((Double.parseDouble(purchasePrice))));

//				System.out.println("purchase price "+purchasePrice);
			/*String purchasePriceRoundUp=commonForAllProd.getRoundUp(commonForAllProd.roundUp_Level2_Ceil(""+commonForAllProd.getStringWithout_E((Double.parseDouble(purchasePrice)))));
			if(annuityPlusBean.getIsADB())
			{
				try {
					//riderSumAssured = bl.getRiderSumAssured();
					//riderPremium = bl.getRiderPremiumFinal(riderSumAssured);
					//riderPrem = bl.getRiderPremium(riderSumAssured);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}*/
//vrushali
            //netPremium=commonForAllProd.getRoundOffLevel2New(commonForAllProd.getStringWithout_E(Double.parseDouble(purchasePrice)+(Double.parseDouble(riderPrem))));
            //String basicServiceTax,SBC_serviceTax,KKC_serviceTax,serviceTaxOnPurchasePrice;

			/*if(annuityPlusBean.getOptFor().equals("Annuity Payout Amount"))
			{
				basicServiceTax=commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(bl.getServiceTaxOnPurchasePrice("basic")+(bl.getServiceTaxOnRider(riderSumAssured,"basic")*bl.getRiderFactor())));
				SBC_serviceTax=commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(bl.getServiceTaxOnPurchasePrice("SBC")+(bl.getServiceTaxOnRider(riderSumAssured,"SBC")*bl.getRiderFactor())));
				KKC_serviceTax=commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(bl.getServiceTaxOnPurchasePrice("KKC")+(bl.getServiceTaxOnRider(riderSumAssured,"KKC")*bl.getRiderFactor())));
			}
			else
			{
				basicServiceTax=commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(bl.getServiceTaxOnPurchasePrice("basic")+(bl.getServiceTaxOnRider(riderSumAssured,"basic")*bl.getRiderFactor())));
				SBC_serviceTax=commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(bl.getServiceTaxOnPurchasePrice("SBC")+(bl.getServiceTaxOnRider(riderSumAssured,"SBC")*bl.getRiderFactor())));
				KKC_serviceTax=commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(bl.getServiceTaxOnPurchasePrice("KKC")+(bl.getServiceTaxOnRider(riderSumAssured,"KKC")*bl.getRiderFactor())));

//					 basicServiceTax=commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(bl.getServiceTaxForPremAmtOption("basic")+(bl.getServiceTaxOnRider(riderSumAssured,"basic")*bl.getRiderFactor())));
//					 SBC_serviceTax=commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(bl.getServiceTaxForPremAmtOption("SBC")+(bl.getServiceTaxOnRider(riderSumAssured,"SBC")*bl.getRiderFactor())));
//					 KKC_serviceTax=commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(bl.getServiceTaxForPremAmtOption("KKC")+(bl.getServiceTaxOnRider(riderSumAssured,"KKC")*bl.getRiderFactor())));
			}*/

			/*if(annuityPlusBean.getOptFor().equals("Annuity Payout Amount"))
			{
				basicServiceTax=commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(bl.getServiceTaxOnPurchasePrice("basic",annuityPlusBean.getServiceTax())+(bl.getServiceTaxOnRider(riderSumAssured,"basic",annuityPlusBean.getServiceTax())*bl.getRiderFactor())));
				SBC_serviceTax=commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(bl.getServiceTaxOnPurchasePrice("SBC",annuityPlusBean.getServiceTax())+(bl.getServiceTaxOnRider(riderSumAssured,"SBC",annuityPlusBean.getServiceTax())*bl.getRiderFactor())));
				KKC_serviceTax=commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(bl.getServiceTaxOnPurchasePrice("KKC",annuityPlusBean.getServiceTax())+(bl.getServiceTaxOnRider(riderSumAssured,"KKC",annuityPlusBean.getServiceTax())*bl.getRiderFactor())));
				serviceTaxOnPurchasePrice= commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(bl.getServiceTaxOnPurchasePrice("basic",annuityPlusBean.getServiceTax())+bl.getServiceTaxOnPurchasePrice("SBC",annuityPlusBean.getServiceTax())+bl.getServiceTaxOnPurchasePrice("KKC",annuityPlusBean.getServiceTax())));

			}
			else
			{
				basicServiceTax=commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(bl.getServiceTax1()+(bl.getServiceTaxOnRider(riderSumAssured,"basic",annuityPlusBean.getServiceTax())*bl.getRiderFactor())));
				SBC_serviceTax=commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(0+(bl.getServiceTaxOnRider(riderSumAssured,"SBC",annuityPlusBean.getServiceTax())*bl.getRiderFactor())));
				KKC_serviceTax=commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(0+(bl.getServiceTaxOnRider(riderSumAssured,"KKC",annuityPlusBean.getServiceTax())*bl.getRiderFactor())));
				serviceTaxOnPurchasePrice= commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(bl.getServiceTaxOnPurchasePrice1("basic",annuityPlusBean.getServiceTax())+bl.getServiceTaxOnPurchasePrice1("SBC",annuityPlusBean.getServiceTax())+bl.getServiceTaxOnPurchasePrice1("KKC",annuityPlusBean.getServiceTax())));
//					 basicServiceTax=commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(bl.getServiceTaxForPremAmtOption("basic")+(bl.getServiceTaxOnRider(riderSumAssured,"basic")*bl.getRiderFactor())));
//					 SBC_serviceTax=commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(bl.getServiceTaxForPremAmtOption("SBC")+(bl.getServiceTaxOnRider(riderSumAssured,"SBC")*bl.getRiderFactor())));
//					 KKC_serviceTax=commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(bl.getServiceTaxForPremAmtOption("KKC")+(bl.getServiceTaxOnRider(riderSumAssured,"KKC")*bl.getRiderFactor())));
			}*/

//				System.out.println(commonForAllProd.getStringWithout_E(bl.getServiceTaxForPremAmtOption("basic")+(bl.getServiceTaxOnRider(riderSumAssured,"basic")*bl.getRiderFactor())));
//				System.out.println(commonForAllProd.getStringWithout_E(bl.getServiceTaxForPremAmtOption("SBC")+(bl.getServiceTaxOnRider(riderSumAssured,"SBC")*bl.getRiderFactor())));


            //double totalPurchasePrice=Double.parseDouble(purchasePriceRoundUp)+Double.parseDouble(serviceTaxOnPurchasePrice);
            //totServiceTax=Double.parseDouble(basicServiceTax)+Double.parseDouble(SBC_serviceTax)+Double.parseDouble(KKC_serviceTax);

            //bl.setTotalServiceTax(totServiceTax);

            long factor = 0;


            if ((annuityPlusBean.getModeOfAnnuityPayout().equals("Yearly") || annuityPlusBean.getModeOfAnnuityPayout().equals("Half Yearly")) && annuityPlusBean.getIsAdvanceAnnuityPayout()) {
//					System.out.println("Annuity Payout Date -> "+annuityPlusBean.getAnnuityPayoutDate().toString());
                if (annuityPlusBean.getModeOfAnnuityPayout().equals("Yearly")) {

                    factor = annuityPlusBean.getAnnuityPayoutDate().getTime() - 31536000000L;
//						System.out.println("factor Y "+factor);
                } else {
                    factor = annuityPlusBean.getAnnuityPayoutDate().getTime() - 15768000000L;
//						System.out.println("factor H "+factor);
                }
            }
            //System.out.println("@2");
            idealDate = new Date(factor);
//				System.out.println("idealDate "+idealDate);
            //System.out.println("@3");
            int intAnnuityPayoutDate = 0, intIdealDate = 0;
            long noOfDaysForIntCaln = 0;

            if ((annuityPlusBean.getModeOfAnnuityPayout().equals("Yearly") || annuityPlusBean.getModeOfAnnuityPayout().equals("Half Yearly")) && annuityPlusBean.getIsAdvanceAnnuityPayout()) {
                intAnnuityPayoutDate = Integer.parseInt(annuityPlusBean.getAnnuityPayoutDate().toString().substring(8, 10));
                intIdealDate = Integer.parseInt(idealDate.toString().substring(8, 10));

//					System.out.println(" Initial intAnnuityPayoutDate -> "+ intAnnuityPayoutDate);
//					System.out.println("Initial intIdealDate -> "+ intIdealDate);

                //To match with escel date comparison
                for (int i = 1; i < 10; i++) {
                    //System.out.println("** i -> "+i);
                    if (intAnnuityPayoutDate > intIdealDate) {
                        idealDate = new Date(factor + 86400000 * i);
                        intIdealDate = Integer.parseInt(idealDate.toString().substring(8, 10));
                        //System.out.println("In for loop -> Initial intAnnuityPayoutDate -> "+ intAnnuityPayoutDate);
                        //System.out.println("In for loop -> Initial intIdealDate -> "+ intIdealDate);
                    } else if (intAnnuityPayoutDate < intIdealDate) {
                        idealDate = new Date(factor - 86400000 * i);
                        intIdealDate = Integer.parseInt(idealDate.toString().substring(8, 10));
                        //System.out.println("In for loop -> Initial intAnnuityPayoutDate -> "+ intAnnuityPayoutDate);
                        //System.out.println("In for loop -> Initial intIdealDate -> "+ intIdealDate);
                    } else if (intAnnuityPayoutDate == intIdealDate) {
                        break;
                    }
                }

//					System.out.println("*********   IdealDate -> "+idealDate.toString());
//					System.out.println("@4");
//					System.out.println("@5");
//					System.out.println("Annuity Payout Dates Part => "+3);
//					System.out.println("Ideal Dates Part => "+intIdealDate);
//					System.out.println("@6");
//					System.out.println("proposal date " + annuityPlusBean.getProposalDate().toString());
//					System.out.println("days diff "+(commonForAllProd.getDaysDiff(annuityPlusBean.getProposalDate(), idealDate)));

                if (annuityPlusBean.getModeOfAnnuityPayout().equals("Yearly")) {
                    noOfDaysForIntCaln = (commonForAllProd.getDaysDiff(annuityPlusBean.getProposalDate(), idealDate)) + 1;
                } else if (annuityPlusBean.getModeOfAnnuityPayout().equals("Half Yearly")) {
                    noOfDaysForIntCaln = (commonForAllProd.getDaysDiff(annuityPlusBean.getProposalDate(), idealDate)) + 2;
                }
                //System.out.println("@7");
//					System.out.println("noOfDaysForIntCaln -> "+noOfDaysForIntCaln);
            }

//				System.out.println("*** noOfDaysForIntCaln [BT96]-> "+noOfDaysForIntCaln);


            noOfDaysForIntCalculation = noOfDaysForIntCaln;
            //System.out.println("@8");
            //advancePremPayable=bl.getAdvancePremiumPayabale(noOfDaysForIntCaln);
            //String totalPremium=commonForAllProd.getRound(commonForAllProd.getStringWithout_E(bl.getTotalPremium(Double.parseDouble(purchasePrice),advancePremPayable)));
            //System.out.println("Total Premium  ==>>  "+totalPremium);
            //String totalPremiumRequired=commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(bl.getTotalPremiumRequired(Double.parseDouble(totalPremium))));
            //System.out.println("Total Premium Payable   ==>>    "+totalPremiumRequired);

            //String additionalPremiumPayable=commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(bl.additionalPremiumPayableFinal(Double.parseDouble(totalPremiumRequired),Double.parseDouble(totalPremium),Double.parseDouble(purchasePrice),advancePremPayable)));
            //System.out.println("@9");
            //For Premium Amount Option
            //System.out.println("E18  ->  getTotalAmt()     -> "+bl.getTotalAmt());
            //System.out.println("E18  ->  getServiceTax()     -> "+bl.getServiceTax());
            //System.out.println("E34  ->  getAmtAfterServiceTax()    -> "+bl.getAmtAfterServiceTax());
            //System.out.println("@10");


            //String annuityAmt=commonForAllProd.getRound(commonForAllProd.getStringWithout_E(bl.getAnnuityAmtForSelectedFreq()));

			/*if(annuityPlusBean.getIsStaff())
			{
				staffStatus = "sbi";
				//disc_Basic_SelFreq
			}
			else
				staffStatus = "none";*/


            StaffRebate = getStaffRebate(cb_staffdisc.isChecked());
            String discountPercentage = StaffRebate + "";


            retVal.append("<errCode>0</errCode>");
            retVal.append("<staffStatus>").append(staffStatus).append("</staffStatus>");
            retVal.append("<staffRebate>").append(discountPercentage).append("</staffRebate>");

            retVal.append("<modeOfAnnuityPayout>").append(modeOfAnnuityPayout).append("</modeOfAnnuityPayout>");
            //vrushali
            retVal.append("<policyTerm>").append(policyTerm).append("</policyTerm>");

            retVal.append("<annuityAmtPayable>").append(commonForAllProd.getStringWithout_E(Double.parseDouble(annuityAmtPayable))).append("</annuityAmtPayable>");

            retVal.append("<purchasePrice>").append(commonForAllProd.getStringWithout_E(Double.parseDouble(purchasePrice))).append("</purchasePrice>");

            retVal.append("<totalPurchasePrice>").append(commonForAllProd.getStringWithout_E(Double.parseDouble(totalPurchasePrice))).append("</totalPurchasePrice>");

            if (annuityPlusBean.getIsADB()) {
                retVal.append("<riderSumAssured>").append(commonForAllProd.getStringWithout_E(Double.parseDouble(riderSumAssured))).append("</riderSumAssured>");
                retVal.append("<riderPrem>").append(commonForAllProd.getStringWithout_E(Double.parseDouble(riderPrem))).append("</riderPrem>");
                //vrushali
                retVal.append("<riderPrem1>").append(commonForAllProd.getStringWithout_E(Double.parseDouble(riderPrem1))).append("</riderPrem1>");
            } else {
                retVal.append("<riderSumAssured>0</riderSumAssured>");
                retVal.append("<riderPrem>0</riderPrem>");
                retVal.append("<riderPrem1>0</riderPrem1>");
            }
            //vrushali
            retVal.append("<netPremium>").append(commonForAllProd.getRoundOffLevel2(commonForAllProd.getStringWithout_E(Double.parseDouble(netPremium)))).append("</netPremium>");
            retVal.append("<basicServiceTax>").append(commonForAllProd.getStringWithout_E(Double.parseDouble(basicServiceTax))).append("</basicServiceTax>");

            retVal.append("<SBC_serviceTax>").append(commonForAllProd.getStringWithout_E(Double.parseDouble(SBC_serviceTax))).append("</SBC_serviceTax>");

            retVal.append("<KKC_serviceTax>").append(commonForAllProd.getStringWithout_E(Double.parseDouble(KKC_serviceTax))).append("</KKC_serviceTax>");

            retVal.append("<totServiceTax>").append(commonForAllProd.getStringWithout_E(Double.parseDouble(totServiceTax))).append("</totServiceTax>");

            if (annuityPlusBean.getIsAdvanceAnnuityPayout()) {
                retVal.append("<advancePremPayable>").append(commonForAllProd.getStringWithout_E(Double.parseDouble(advancePremPayable))).append("</advancePremPayable>");
            } else {
                retVal.append("<advancePremPayable>0</advancePremPayable>");
            }

            retVal.append("<totalPrem>").append(commonForAllProd.getStringWithout_E(Double.parseDouble(totalPrem))).append("</totalPrem>");

            if (annuityPlusBean.getSourceOfBusiness().equals("Vesting/Death/Surrender of existing SBI Life's pension policy") || annuityPlusBean.getSourceOfBusiness().equals("Open Market Option (Any Other Life Insurance Company Pension Policy)")) {
                retVal.append("<totalPremReq>").append(commonForAllProd.getStringWithout_E(Double.parseDouble(totalPremReq))).append("</totalPremReq>");
                retVal.append("<addPremPayable>").append(commonForAllProd.getStringWithout_E(Double.parseDouble(addPremPayable))).append("</addPremPayable>");
            } else {
                retVal.append("<totalPremReq>0</totalPremReq>");
                retVal.append("<addPremPayable>0</addPremPayable>");
            }

        } else {
            if (val_annuity_payout_date) {
                retVal.append("<errCode>1</errCode>");
                retVal.append("<annuityPayoutDateError>").append(val_annuity_payout_date).append("</annuityPayoutDateError>");
            }
            if (val_annuity_amount) {
                retVal.append("<errCode>1</errCode>");
                retVal.append("<minAnnuityAmountError>").append(val_annuity_amount).append("</minAnnuityAmountError>");
            }
        }
        retVal.append("</annuityPlus>");
//			System.out.println("Final output in xml" + retVal.toString());
        //return retVal.toString();


    }


    private void getInput(AnnuityPlusBean annuityPlusBean) {

        inputVal = new StringBuilder();

        boolean isJKresident = annuityPlusBean.getIsJKresident();
        boolean isStaffOrNot = annuityPlusBean.getIsStaff();
        boolean isAdvanceAnnuityPayout = annuityPlusBean.getIsAdvanceAnnuityPayout();
        boolean isADBRider = annuityPlusBean.getIsADB();
        String DobOfSecondAnnuitant = annuityPlusBean.getDobOfSecondAnnuitant();

        inputVal.append("<?xml version='1.0' encoding='utf-8' ?><annuityPlusBean>");
        inputVal.append("<Proposer_is_same_as_Annuitant>").append(Proposer_is_same_as_Annuitant).append("</Proposer_is_same_as_Annuitant>");


        inputVal.append("<proposer_title>").append(proposer_Title).append("</proposer_title>");
        inputVal.append("<proposer_firstName>").append(proposer_First_Name).append("</proposer_firstName>");
        inputVal.append("<proposer_middleName>").append(proposer_Middle_Name).append("</proposer_middleName>");
        inputVal.append("<proposer_lastName>").append(proposer_Last_Name).append("</proposer_lastName>");
        inputVal.append("<proposer_name>").append(name_of_proposer).append("</proposer_name>");
        inputVal.append("<proposer_DOB>").append(proposer_date_of_birth).append("</proposer_DOB>");
        inputVal.append("<proposer_age>").append(proposer_age).append("</proposer_age>");
        inputVal.append("<propsoser_gender>").append(propsoser_gender).append("</propsoser_gender>");

        inputVal.append("<TitleFirstAnnuitant>").append(lifeAssured_Title).append("</TitleFirstAnnuitant>");
        inputVal.append("<FirstNameFirstAnnuitant>").append(lifeAssured_First_Name).append("</FirstNameFirstAnnuitant>");
        inputVal.append("<MiddleNameFirstAnnuitant>").append(lifeAssured_Middle_Name).append("</MiddleNameFirstAnnuitant>");
        inputVal.append("<LastNameFirstAnnuitant>").append(lifeAssured_Last_Name).append("</LastNameFirstAnnuitant>");
        inputVal.append("<NameOfFirstAnnuitant>").append(name_of_life_assured).append("</NameOfFirstAnnuitant>");
        inputVal.append("<DobFirstAnnuitant>").append(lifeAssured_date_of_birth).append("</DobFirstAnnuitant>");
        inputVal.append("<AgeFirstAnnuitant>").append(edt_bi_annuity_plus_life_assured_age.getText().toString()).append("</AgeFirstAnnuitant>");
        inputVal.append("<GenderFirstAnnuitant>").append(gender).append("</GenderFirstAnnuitant>");

        inputVal.append("<is_Second_Annuitant>").append(is_Second_Annuitant).append("</is_Second_Annuitant>");
        inputVal.append("<TitleSecondAnnuitant>").append(spnr_bi_annuity_plus_life_assured_title_second_annuitant.getSelectedItem().toString()).append("</TitleSecondAnnuitant>");
        inputVal.append("<FirstNameSecondAnnuitant>").append(edt_bi_annuity_plus_life_assured_first_name_second_annuitant.getText().toString()).append("</FirstNameSecondAnnuitant>");
        inputVal.append("<MiddleNameSecondAnnuitant>").append(edt_bi_annuity_plus_life_assured_middle_name_second_annuitant.getText().toString()).append("</MiddleNameSecondAnnuitant>");
        inputVal.append("<LastNameSecondAnnuitant>").append(edt_bi_annuity_plus_life_assured_last_name_second_annuitant.getText().toString()).append("</LastNameSecondAnnuitant>");
        inputVal.append("<NameOfSecondAnnuitant>").append(name_of_second_annuitant).append("</NameOfSecondAnnuitant>");
        inputVal.append("<DobSecondAnnuitant>").append(DobOfSecondAnnuitant).append("</DobSecondAnnuitant>");
        inputVal.append("<AgeSecondAnnuitant>").append(edt_bi_annuity_plus_life_assured_age_second_annuitant.getText().toString()).append("</AgeSecondAnnuitant>");
        inputVal.append("<GenderSecondtAnnuitant>").append(gender_2).append("</GenderSecondtAnnuitant>");

        inputVal.append("<isJKResident>").append(isJKresident).append("</isJKResident>");
        inputVal.append("<isStaff>").append(isStaffOrNot).append("</isStaff>");
        //Added Tushar Kadam Kerla Applicable Tax 7-Jun-2019
        String str_kerla_discount = "N";
        if (cb_kerladisc.isChecked()) {
            str_kerla_discount = "Y";
        }

        inputVal.append("<KFC>" + str_kerla_discount + "</KFC>");
        inputVal.append("<SourceOfBusiness>").append(spnr_bi_annuity_plus_source_of_business.getSelectedItem().toString()).append("</SourceOfBusiness>");
        inputVal.append("<ChannelDetails>").append(spnr_bi_annuity_plus_channel_details.getSelectedItem().toString()).append("</ChannelDetails>");
        inputVal.append("<ModeOfAnnuityPayout>").append(ModeOfAnnuityPayout).append("</ModeOfAnnuityPayout>");

        inputVal.append("<AnnuityOption>").append(spnr_bi_annuity_plus_annuity_option.getSelectedItem().toString()).append("</AnnuityOption>");
        inputVal.append("<ProposalDate>").append(proposal_date).append("</ProposalDate>");
        inputVal.append("<OptFor>").append(spnr_bi_annuity_plus_opt_for.getSelectedItem().toString()).append("</OptFor>");

        inputVal.append("<AnnuityAmount>").append(edt_annuity_plus_annuity_amount.getText().toString()).append("</AnnuityAmount>");
        inputVal.append("<VestingAmount>").append(edt_annuity_plus_vesting_amount.getText().toString()).append("</VestingAmount>");
        inputVal.append("<AdditionalAmountIfAny>").append(edt_annuity_plus_additional_amount_if_any.getText().toString()).append("</AdditionalAmountIfAny>");

        inputVal.append("<isAdvanceAnnuityPayout>").append(isAdvanceAnnuityPayout).append("</isAdvanceAnnuityPayout>");
        inputVal.append("<AnnuityPayoutDate>").append(annuity_plus_advance_annuity_payout_from_which_date).append("</AnnuityPayoutDate>");

        inputVal.append("<isADBRider>").append(isADBRider).append("</isADBRider>");
        inputVal.append("<ApplicableFor>").append(spnr_bi_annuity_plus_applicable_for.getSelectedItem().toString()).append("</ApplicableFor>");
        inputVal.append("<BIVERSION>" + "7" + "</BIVERSION>");

        inputVal.append("<product_name>").append(planName).append("</product_name>");
        /*parivartan changes*/
        inputVal.append("<product_Code>").append(product_Code).append("</product_Code>");
        inputVal.append("<product_UIN>").append(product_UIN).append("</product_UIN>");
        inputVal.append("<product_cateogory>").append(product_cateogory).append("</product_cateogory>");
        inputVal.append("<product_type>").append(product_type).append("</product_type>");
        /*end*/

        inputVal.append("</annuityPlusBean>");

    }


    public void onClickProposerDate(View v) {
        initialiseDateParameter(proposal_date, 0);
        flag = false;
        DIALOG_ID = 4;
        showDateDialog();
    }

    public void onClickLADob(View v) {
        initialiseDateParameter(lifeAssured_date_of_birth, 40);
        flag = false;
        DIALOG_ID = 5;
        showDateDialog();
    }

    public void onClickLADobSecond(View v) {
        initialiseDateParameter(second_Annuitant_date_of_birth, 40);
        flag = false;
        DIALOG_ID = 7;
        showDateDialog();
    }

    public void onClickAdvanceAnnuityPayoutDate(View v) {
        initialiseDateParameter(annuity_plus_advance_annuity_payout_from_which_date, 0);
        flag = true;
        DIALOG_ID = 8;
        showDateDialog();
    }

    public void onClickProposerDob(View v) {
        initialiseDateParameter(proposer_date_of_birth, 40);
        flag = false;
        DIALOG_ID = 9;
        showDateDialog();
    }

    private void showDateDialog() {
        DatePickerDialog dialog = new DatePickerDialog(
                context,
                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                mDateSetListener,
                mYear, mMonth, mDay);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }


    private final DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {

            mYear = year;
            mMonth = month;
            mDay = day;
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

        String final_age = Integer.toString(age);

        if (final_age.contains("-") && !flag) {
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
                    String proposalDate = final_age;
                    final_age = Integer.toString(age);
                    if (final_age.contains("-")) {
                        commonMethods.BICommonDialog(context, "Please fill Valid Birth Date");
                    } else {

                        btn_bi_annuity_plus_proposal_date.setText(date);
                        // edt_bi_annuity_plus_life_assured_age
                        // .setText(final_age);
                        proposal_date = commonMethods.getMMDDYYYYDatabaseDate(date + "");


                    }
                    break;

                case 5:

                    final_age = Integer.toString(age);
                    if (final_age.contains("-")) {
                        commonMethods.BICommonDialog(context, "Please fill Valid Bith Date");
                    } else {

                        if (40 <= age && 80 >= age) {

                            btn_bi_annuity_plus_life_assured_date.setText(date);

                            edt_bi_annuity_plus_life_assured_age.setText(final_age);

                            lifeAssured_date_of_birth = commonMethods.getMMDDYYYYDatabaseDate(date + "");

                            if (cb_bi_annuity_plus_adb_rider.isChecked()) {
                                cb_bi_annuity_plus_adb_rider.setChecked(false);
                                tr_bi_annuity_plus_adb_rider.setVisibility(View.GONE);
                            }

                        } else {

                            commonMethods.BICommonDialog(context, "Minimum Age should be 40 yrs and Maximum Age should be 80 yrs For First Annuitant");
                            btn_bi_annuity_plus_life_assured_date
                                    .setText("Select Date");
                            lifeAssured_date_of_birth = "";
                            commonMethods.clearFocusable(btn_bi_annuity_plus_life_assured_date);
                            commonMethods.setFocusable(btn_bi_annuity_plus_life_assured_date);
                            btn_bi_annuity_plus_life_assured_date.requestFocus();
                        }
                    }
                    break;
                case 6:
                case 7:
                    String secondAnnuitantdateofbirth = final_age;
                    final_age = Integer.toString(age);
                    if (final_age.contains("-")) {
                        commonMethods.BICommonDialog(context, "Please fill Valid Date");
                    } else {
                        if (40 <= age && 80 >= age) {

                            btn_bi_annuity_plus_life_assured_date_second_annuitant.setText(date);
                            edt_bi_annuity_plus_life_assured_age_second_annuitant.setText(final_age);
                            second_Annuitant_date_of_birth = commonMethods.getMMDDYYYYDatabaseDate(date + "");
                            commonMethods.clearFocusable(btn_bi_annuity_plus_life_assured_date_second_annuitant);
                            commonMethods.setFocusable(btn_bi_annuity_plus_advance_annuity_payout_from_which_date);
                            btn_bi_annuity_plus_advance_annuity_payout_from_which_date.requestFocus();

                            if (cb_bi_annuity_plus_adb_rider.isChecked()) {
                                cb_bi_annuity_plus_adb_rider.setChecked(false);
                                tr_bi_annuity_plus_adb_rider.setVisibility(View.GONE);
                            }

                        } else {
                            commonMethods.BICommonDialog(context, "Minimum Age should be 40 yrs and Maximum Age should be 80 yrs For Second Annuitant");
                            btn_bi_annuity_plus_life_assured_date_second_annuitant.setText("Select Date");
                            edt_bi_annuity_plus_life_assured_age_second_annuitant
                                    .setText("");
                            second_Annuitant_date_of_birth = "";
                            commonMethods.clearFocusable(btn_bi_annuity_plus_life_assured_date_second_annuitant);
                            commonMethods.setFocusable(btn_bi_annuity_plus_life_assured_date_second_annuitant);
                            btn_bi_annuity_plus_life_assured_date_second_annuitant.requestFocus();
                        }
                    }
                    break;

                case 8:
                    String annuityPlusAdvanceAnnuityPayoutFromWhichDate = final_age;
                    final_age = Integer.toString(age);
//				if (final_age.contains("-")) {
//					commonMethods.BICommonDialog("Please fill Valid Date");
//				} else {
                    //if (18 <= age) {

                    btn_bi_annuity_plus_advance_annuity_payout_from_which_date.setText(date);
                    // edt_bi_annuity_plus_life_assured_age
                    // .setText(final_age);
                    annuity_plus_advance_annuity_payout_from_which_date = commonMethods.getMMDDYYYYDatabaseDate(date + "");
                    commonMethods.clearFocusable(btn_bi_annuity_plus_advance_annuity_payout_from_which_date);
                    commonMethods.setFocusable(btn_bi_annuity_plus_advance_annuity_payout_from_which_date);
                    btn_bi_annuity_plus_advance_annuity_payout_from_which_date.requestFocus();

//					} else {
//					}
//				}
                    break;

                case 9:

                    proposer_age = final_age;
                    final_age = Integer.toString(age);
                    if (final_age.contains("-")) {
                        commonMethods.BICommonDialog(context, "Please fill Valid Birth Date");
                    } else {

                        if (18 <= age && 100 >= age) {

                            btn_bi_annuity_plus_proposer_date.setText(date);

                            tvProposerAge.setText(final_age);

                            proposer_date_of_birth = commonMethods.getMMDDYYYYDatabaseDate(date + "");
                            commonMethods.clearFocusable(btn_bi_annuity_plus_proposer_date);
                            commonMethods.setFocusable(edt_annuity_plus_contact_no);
                            edt_annuity_plus_contact_no.requestFocus();

                            if (cb_bi_annuity_plus_adb_rider.isChecked()) {
                                cb_bi_annuity_plus_adb_rider.setChecked(false);
                                tr_bi_annuity_plus_adb_rider.setVisibility(View.GONE);
                            }

                            if (cb_different_from_proposer.isChecked()) {
                                cb_different_from_proposer.setChecked(false);
                                Proposer_is_same_as_Annuitant = "N";
                                edt_bi_annuity_plus_life_assured_first_name.setText("");
                                edt_bi_annuity_plus_life_assured_middle_name.setText("");
                                edt_bi_annuity_plus_life_assured_last_name.setText("");
                                lifeAssured_First_Name = "";
                                lifeAssured_Middle_Name = "";
                                lifeAssured_Last_Name = "";


                                spnr_bi_annuity_plus_life_assured_title.setSelection(
                                        getIndex(spnr_bi_annuity_plus_life_assured_title,
                                                "Select Title"), false);
                                lifeAssured_Title = "";
                                gender = "";

                                btn_bi_annuity_plus_life_assured_date.setText("Select Date");
                                lifeAssured_date_of_birth = "";
                                edt_bi_annuity_plus_life_assured_age.setText("");


                                spnr_bi_annuity_plus_life_assured_title.setClickable(true);
                                edt_bi_annuity_plus_life_assured_first_name.setClickable(true);
                                edt_bi_annuity_plus_life_assured_middle_name.setClickable(true);
                                edt_bi_annuity_plus_life_assured_last_name.setClickable(true);
                                btn_bi_annuity_plus_life_assured_date.setClickable(true);

                                spnr_bi_annuity_plus_life_assured_title.setEnabled(true);
                                edt_bi_annuity_plus_life_assured_first_name.setEnabled(true);
                                edt_bi_annuity_plus_life_assured_middle_name.setEnabled(true);
                                edt_bi_annuity_plus_life_assured_last_name.setEnabled(true);
                                btn_bi_annuity_plus_life_assured_date.setEnabled(true);
                            }


                        } else {

                            commonMethods.BICommonDialog(context, "Minimum Age should be 18 yrs and Maximum Age should be 100 yrs For Proposer");
                            btn_bi_annuity_plus_proposer_date
                                    .setText("Select Date");
                            proposer_date_of_birth = "";
                            commonMethods.clearFocusable(btn_bi_annuity_plus_proposer_date);
                            commonMethods.setFocusable(btn_bi_annuity_plus_proposer_date);
                            btn_bi_annuity_plus_proposer_date.requestFocus();
                        }
                    }
                    break;


                default:
                    break;

            }

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

    private void confirming_email_id(String email_id) {

        boolean validationFlag2 = false;
        if (!(email_id.equals(ProposerEmailId))) {
            edt_annuity_plus_ConfirmEmail_id
                    .setError("Email id does not match");
            validationFlag2 = false;
        } else if ((email_id.equals(ProposerEmailId))) {
            validationFlag2 = true;
        }

    }


    private void mobile_validation(String number) {
        boolean validationFlag3 = false;
        if ((number.length() != 10)) {
            edt_annuity_plus_contact_no
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
            edt_annuity_plus_Email_id
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
    /*private String getDate(String OldDate) {
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
    }*/
    public String getDate2(String OldDate) {
        String NewDate = "";
        try {
            DateFormat userDateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
            DateFormat dateFormatNeeded = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
            Date date = userDateFormat.parse(OldDate);

            NewDate = dateFormatNeeded.format(date);

        } catch (Exception e) {
            // TODO: handle exception
            Log.e("Integrated Service", "Error in Getting Date");
        }

        return NewDate;
    }



   /* public void dialog(String msg) {

        final Dialog d = new Dialog(BI_AnnuityPlusActivity.this);
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);
        d.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        d.setContentView(R.layout.window_agreement);
        TextView textMessage = (TextView) d.findViewById(R.id.textMessage);
        textMessage.setText(msg);
        Button ok = (Button) d.findViewById(R.id.idbtnagreement);
        ok.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                d.dismiss();
            }

        });
        d.setCancelable(true);
        d.setCanceledOnTouchOutside(true);
        d.show();

    }*/

    // Validation


    private boolean valProposerDetail() {
        if (proposer_Title.equals("")
                || proposer_First_Name.equals("")
                || proposer_Last_Name.equals("")) {

            commonMethods.showMessageDialog(context, "Please Fill Name Detail For Proposer");
            if (proposer_Title.equals("")) {
                // apply focusable method
                commonMethods.setFocusable(spnr_bi_annuity_plus_proposer_title);
                spnr_bi_annuity_plus_life_assured_title
                        .requestFocus();
            } else if (proposer_First_Name.equals("")) {
                commonMethods.setFocusable(edt_bi_annuity_plus_proposer_first_name);
                edt_bi_annuity_plus_proposer_first_name
                        .requestFocus();
            } else {
                commonMethods.setFocusable(edt_bi_annuity_plus_proposer_last_name);
                edt_bi_annuity_plus_proposer_last_name
                        .requestFocus();
            }


            return false;
        } else if (propsoser_gender.equals("")) {

            commonMethods.showMessageDialog(context, "Please select Proposer Gender");
            commonMethods.setFocusable(selGender);
            selGender.requestFocus();

            return false;
        } else if (proposer_Title.equalsIgnoreCase("Mr.")
                && propsoser_gender.equalsIgnoreCase("Female")) {

            commonMethods.showMessageDialog(context, "Proposer Title and Gender is not Valid");
            commonMethods.setFocusable(spnr_bi_annuity_plus_proposer_title);
            spnr_bi_annuity_plus_proposer_title.requestFocus();

            return false;

        } else if (proposer_Title.equalsIgnoreCase("Ms.")
                && propsoser_gender.equalsIgnoreCase("Male")) {

            commonMethods.showMessageDialog(context, "Proposer Title and Gender is not Valid");
            commonMethods.setFocusable(spnr_bi_annuity_plus_proposer_title);
            spnr_bi_annuity_plus_proposer_title
                    .requestFocus();

            return false;

        } else if (proposer_Title.equalsIgnoreCase("Mrs.")
                && propsoser_gender.equalsIgnoreCase("Male")) {

            commonMethods.showMessageDialog(context, "Proposer Title and Gender is not Valid");
            commonMethods.setFocusable(spnr_bi_annuity_plus_proposer_title);
            spnr_bi_annuity_plus_proposer_title
                    .requestFocus();

            return false;
        } else {
            return true;
        }

//		}
//		else
//			return true;
    }


    private boolean valLifeAssuredProposerDetail() {
        if (lifeAssured_Title.equals("")
                || lifeAssured_First_Name.equals("")
                || lifeAssured_Last_Name.equals("")) {

            commonMethods.showMessageDialog(context, "Please Fill Name Detail For LifeAssured");
            if (lifeAssured_Title.equals("")) {
                // apply focusable method
                commonMethods.setFocusable(spnr_bi_annuity_plus_life_assured_title);
                spnr_bi_annuity_plus_life_assured_title
                        .requestFocus();
            } else if (lifeAssured_First_Name.equals("")) {
                commonMethods.setFocusable(edt_bi_annuity_plus_life_assured_first_name);
                edt_bi_annuity_plus_life_assured_first_name
                        .requestFocus();
            } else {
                commonMethods.setFocusable(edt_bi_annuity_plus_life_assured_last_name);
                edt_bi_annuity_plus_life_assured_last_name
                        .requestFocus();
            }

            return false;
        } else if (gender.equals("")
        ) {

            commonMethods.showMessageDialog(context, "Please select First Annuitant Gender");

            commonMethods.setFocusable(selFirstAnnutantGender);
            selFirstAnnutantGender.requestFocus();

            return false;
        } else if (lifeAssured_Title.equalsIgnoreCase("Mr.")
                && gender.equalsIgnoreCase("Female")) {

            commonMethods.showMessageDialog(context, "First Annuitant Title and Gender is not Valid");
            commonMethods.setFocusable(spnr_bi_annuity_plus_life_assured_title);
            spnr_bi_annuity_plus_life_assured_title
                    .requestFocus();

            return false;

        } else if (lifeAssured_Title.equalsIgnoreCase("Ms.")
                && gender.equalsIgnoreCase("Male")) {
            commonMethods.showMessageDialog(context, "First Annuitant Title and Gender is not Valid");
            commonMethods.setFocusable(spnr_bi_annuity_plus_life_assured_title);
            spnr_bi_annuity_plus_life_assured_title
                    .requestFocus();

            return false;

        } else if (lifeAssured_Title.equalsIgnoreCase("Mrs.")
                && gender.equalsIgnoreCase("Male")) {

            commonMethods.showMessageDialog(context, "First Annuitant Title and Gender is not Valid");
            // apply focusable method
            commonMethods.setFocusable(spnr_bi_annuity_plus_life_assured_title);
            spnr_bi_annuity_plus_life_assured_title
                    .requestFocus();

            return false;
        } else {
            return true;
        }

//			}
//				else
//				return true;
    }

    private boolean valSecondAnnuitantDetails() {

        if (is_Second_Annuitant.equalsIgnoreCase("Y")) {
            if (second_Annuitant_Proposer_Title.equals("")
                    || second_Annuitant_First_Name.equals("")
                    || second_Annuitant_Last_Name.equals("")) {

                commonMethods.showMessageDialog(context, "Please Fill Name Detail For Second Annuitant");
                if (second_Annuitant_Proposer_Title.equals("")) {
                    // apply focusable method
                    commonMethods.setFocusable(spnr_bi_annuity_plus_life_assured_title_second_annuitant);
                    spnr_bi_annuity_plus_life_assured_title_second_annuitant
                            .requestFocus();
                } else if (second_Annuitant_First_Name.equals("")) {
                    commonMethods.setFocusable(edt_bi_annuity_plus_life_assured_first_name_second_annuitant);
                    edt_bi_annuity_plus_life_assured_first_name_second_annuitant
                            .requestFocus();
                } else {
                    commonMethods.setFocusable(edt_bi_annuity_plus_life_assured_last_name_second_annuitant);
                    edt_bi_annuity_plus_life_assured_last_name_second_annuitant
                            .requestFocus();
                }

                return false;
            }


            if (gender_2.equals("")) {
                commonMethods.showMessageDialog(context, "Please select gender for the second annuitant");

                commonMethods.setFocusable(selSecondAnnutantGender);
                selSecondAnnutantGender.requestFocus();

                return false;
            } else if (second_Annuitant_Proposer_Title.equalsIgnoreCase("Mr.")
                    && gender_2.equalsIgnoreCase("Female")) {

                commonMethods.showMessageDialog(context, "Second Annuitant Title and Gender is not Valid");
                commonMethods.setFocusable(spnr_bi_annuity_plus_life_assured_title_second_annuitant);
                spnr_bi_annuity_plus_life_assured_title_second_annuitant
                        .requestFocus();

                return false;

            } else if (second_Annuitant_Proposer_Title.equalsIgnoreCase("Ms.")
                    && gender_2.equalsIgnoreCase("Male")) {

                commonMethods.showMessageDialog(context, "Second Annuitant Title and Gender is not Valid");
                commonMethods.setFocusable(spnr_bi_annuity_plus_life_assured_title_second_annuitant);
                spnr_bi_annuity_plus_life_assured_title_second_annuitant
                        .requestFocus();

                return false;

            } else if (second_Annuitant_Proposer_Title.equalsIgnoreCase("Mrs.")
                    && gender_2.equalsIgnoreCase("Male")) {

                commonMethods.showMessageDialog(context, "Second Annuitant Title and Gender is not Valid");
                commonMethods.setFocusable(spnr_bi_annuity_plus_life_assured_title_second_annuitant);
                spnr_bi_annuity_plus_life_assured_title_second_annuitant
                        .requestFocus();

                return false;
            } else {
                return true;
            }

        } else
            return true;
    }

    private boolean val_proposer_dob() {


        if (proposer_date_of_birth.equals("")
                || proposer_date_of_birth
                .equalsIgnoreCase("Select Date")) {
            commonMethods.showMessageDialog(context, "Please Select Valid Date Of Birth For First Annuitant");
            commonMethods.setFocusable(btn_bi_annuity_plus_proposer_date);
            btn_bi_annuity_plus_proposer_date
                    .requestFocus();

            return false;
        } else {
            return true;
        }
//		}else
//			return true;
    }

    private boolean val_first_Annuitant() {


        if (lifeAssured_date_of_birth.equals("")
                || lifeAssured_date_of_birth
                .equalsIgnoreCase("Select Date")) {
            commonMethods.showMessageDialog(context, "Please Select Valid Date Of Birth For First Annuitant");
            commonMethods.setFocusable(btn_bi_annuity_plus_life_assured_date);
            btn_bi_annuity_plus_life_assured_date
                    .requestFocus();

            return false;
        } else {
            return true;
        }
//			}else
//					return true;
    }


    private boolean valSpinner() {


        if (ModeOfAnnuityPayout.equals("")
                || ModeOfAnnuityPayout
                .equalsIgnoreCase("Select")) {
            commonMethods.showMessageDialog(context, "Please Select Annuity Payouts Mode");
            commonMethods.setFocusable(spnr_bi_annuity_plus_mode_of_annuity_payouts);
            spnr_bi_annuity_plus_mode_of_annuity_payouts
                    .requestFocus();

            return false;
        } else if (AnnuityOption.equals("")
                || AnnuityOption
                .equalsIgnoreCase("Select")) {
            commonMethods.showMessageDialog(context, "Please Select Annuity Option");
            commonMethods.setFocusable(spnr_bi_annuity_plus_annuity_option);
            spnr_bi_annuity_plus_annuity_option
                    .requestFocus();

            return false;
        } else if (OptFor.equals("")
                || OptFor
                .equalsIgnoreCase("Select")) {
            commonMethods.showMessageDialog(context, "Please Select opt for");
            commonMethods.setFocusable(spnr_bi_annuity_plus_opt_for);
            spnr_bi_annuity_plus_opt_for
                    .requestFocus();

            return false;
        } else {
            return true;
        }
//			}else
//					return true;
    }


    private boolean val_second_annuitant() {
        if (is_Second_Annuitant.equalsIgnoreCase("Y")) {

            if (second_Annuitant_date_of_birth.equals("")
                    || second_Annuitant_date_of_birth
                    .equalsIgnoreCase("Select Date")) {
                commonMethods.showMessageDialog(context, "Please Select Valid Date Of Birth For Second Annuitant");
                commonMethods.setFocusable(btn_bi_annuity_plus_life_assured_date_second_annuitant);
                btn_bi_annuity_plus_life_assured_date_second_annuitant.requestFocus();
                return false;
            } else {
                return true;
            }
        } else
            return true;
    }


    private boolean val_advance_annuity_payout() {
        if (cb_bi_annuity_plus_advance_annuity_payout.isChecked()) {

            if (annuity_plus_advance_annuity_payout_from_which_date.equals("")
                    || annuity_plus_advance_annuity_payout_from_which_date
                    .equalsIgnoreCase("Select Date")) {
                commonMethods.showMessageDialog(context, "Please Select Valid Date For Advance Annuity Payout");
                commonMethods.setFocusable(btn_bi_annuity_plus_advance_annuity_payout_from_which_date);
                btn_bi_annuity_plus_advance_annuity_payout_from_which_date
                        .requestFocus();

                return false;
            } else {
                return true;
            }
        } else
            return true;
    }


    private boolean val_proposal_date() {
        if (proposal_date.equals("")
                || proposal_date
                .equalsIgnoreCase("Select Date")) {
            commonMethods.showMessageDialog(context, "Please Select Valid Date For Proposer Date");
            commonMethods.setFocusable(btn_bi_annuity_plus_proposal_date);
            btn_bi_annuity_plus_proposal_date
                    .requestFocus();

            return false;
        } else {
            return true;
        }
    }

    private boolean valBasicDetail() {

        if (edt_annuity_plus_contact_no.getText()
                .toString().equals("")) {
            commonMethods.dialogWarning(context, "Please Fill  Mobile No", true);
            edt_annuity_plus_contact_no.requestFocus();
            return false;
        } else if (edt_annuity_plus_contact_no.getText()
                .toString().length() != 10) {
            commonMethods.dialogWarning(context, "Please Fill 10 Digit Mobile No", true);
            edt_annuity_plus_contact_no.requestFocus();
            return false;
        } else if (!emailId.equals("")) {

            email_id_validation(emailId);
            if (validationFla1) {

                if (ConfirmEmailId.equals("")) {

                    commonMethods.dialogWarning(context, "Please Fill Confirm Email Id", true);
                    edt_annuity_plus_ConfirmEmail_id.requestFocus();
                    return false;
                } else if (!ConfirmEmailId.equals(emailId)) {
                    commonMethods.dialogWarning(context, "Email Id Does Not Match", true);
                    return false;
                }

                return true;
            } else {
                commonMethods.dialogWarning(context, "Please Fill Valid Email Id", true);
                edt_annuity_plus_Email_id.requestFocus();
                return false;
            }
        } else if (!ConfirmEmailId.equals("")) {

            email_id_validation(ConfirmEmailId);
            if (validationFla1) {

                if (emailId.equals("")) {
                    commonMethods.dialogWarning(context, "Please Fill Email Id", true);
                    edt_annuity_plus_Email_id.requestFocus();
                    return false;

                }

                return true;
            } else {
                commonMethods.dialogWarning(context, "Please Fill Valid Confirm Email Id", true);
                edt_annuity_plus_ConfirmEmail_id.requestFocus();
                return false;
            }
        } else {
            return true;
        }
    }

    //validate Eligibility for ADB Rider
    private boolean valRider() {
        StringBuilder error = new StringBuilder();


        if (cb_bi_annuity_plus_adb_rider.isChecked()) {
            if (!(edt_bi_annuity_plus_life_assured_age.getText().toString().equals(""))) {

                if (Integer.parseInt(edt_bi_annuity_plus_life_assured_age.getText().toString()) > Prop.maxAgeOfAnnuitantWhenRider) {
                    error.append("Maximum Age limit of Accidental Death Benefit Rider for First Annuitant is ").append(Prop.maxAgeOfAnnuitantWhenRider).append("\n");
                }
				/*if((spnr_bi_annuity_plus_annuity_option.getSelectedItem().toString().equals("Life and Last Survivor - 50% Income")
                        || spnr_bi_annuity_plus_annuity_option.getSelectedItem().toString().equals("Life and Last Survivor - 100% Income")
                        || spnr_bi_annuity_plus_annuity_option.getSelectedItem().toString().equals("Life and Last Survivor with Capital Refund - 50% Income")
                        || spnr_bi_annuity_plus_annuity_option.getSelectedItem().toString().equals("Life and Last Survivor with Capital Refund - 100% Income"))
                        && Integer.parseInt(edt_bi_annuity_plus_life_assured_age_second_annuitant.getText().toString()) > Prop.maxAgeOfAnnuitantWhenRider)
				{
					error.append("Maximum Age limit of Accidental Death Benefit Rider for Second Annuitant is "+ Prop.maxAgeOfAnnuitantWhenRider +"\n");
				}*/


            }


            if (!error.toString().equals("")) {
                cb_bi_annuity_plus_adb_rider.setChecked(false);
                if (tr_bi_annuity_plus_adb_rider.getVisibility() == View.VISIBLE) {
                    tr_bi_annuity_plus_adb_rider.setVisibility(View.GONE);
                }

                commonMethods.showMessageDialog(context, error.toString());
                return false;
            }

        }
        return true;
    }

    //Used in Annuity Option Item Listener
    private void updateField_FirstOrBothAnnuitant() {
        if (spnr_bi_annuity_plus_annuity_option.getSelectedItem().toString().equals("Life and Last Survivor - 50% Income") || spnr_bi_annuity_plus_annuity_option.getSelectedItem().toString().equals("Life and Last Survivor - 100% Income") || spnr_bi_annuity_plus_annuity_option.getSelectedItem().toString().equals("Life and Last Survivor with Capital Refund - 50% Income") || spnr_bi_annuity_plus_annuity_option.getSelectedItem().toString().equals("Life and Last Survivor with Capital Refund - 100% Income")) {
            String[] applicableForList = {"First Annuitant", "Both Annuitant"};
            ArrayAdapter<String> applicableForAdapter = new ArrayAdapter<String>(
                    getApplicationContext(), R.layout.spinner_item, applicableForList);
            applicableForAdapter.setDropDownViewResource(R.layout.spinner_item1);
            spnr_bi_annuity_plus_applicable_for.setAdapter(applicableForAdapter);
            applicableForAdapter.notifyDataSetChanged();

        } else {
            String[] applicableForList = {"First Annuitant"};
            ArrayAdapter<String> applicableForAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, applicableForList);
            applicableForAdapter.setDropDownViewResource(R.layout.spinner_item1);
            spnr_bi_annuity_plus_applicable_for.setAdapter(applicableForAdapter);
            applicableForAdapter.notifyDataSetChanged();

        }
    }

    //Used in Annuity Option Item Listener
    private void addOrRemoveSecAnnuitantFields() {

        if (spnr_bi_annuity_plus_annuity_option.getSelectedItem().toString().equals("Life and Last Survivor - 50% Income") || spnr_bi_annuity_plus_annuity_option.getSelectedItem().toString().equals("Life and Last Survivor - 100% Income") || spnr_bi_annuity_plus_annuity_option.getSelectedItem().toString().equals("Life and Last Survivor with Capital Refund - 50% Income") || spnr_bi_annuity_plus_annuity_option.getSelectedItem().toString().equals("Life and Last Survivor with Capital Refund - 100% Income")) {
            linearlayoutSecondAnnuitant.setVisibility(View.VISIBLE);
            is_Second_Annuitant = "Y";

        } else {
            linearlayoutSecondAnnuitant.setVisibility(View.GONE);
            btn_bi_annuity_plus_life_assured_date_second_annuitant.setText("Select Date");
            second_Annuitant_date_of_birth = "";
            is_Second_Annuitant = "N";
        }

    }

    //Used in source of business and opt for item listener
    private void deleteAndAddFieldsUnderOptForField() {
        tr_annuity_amount.setVisibility(View.GONE);
        tr_vesting_amount.setVisibility(View.GONE);
        tr_additional_amount_if_any.setVisibility(View.GONE);

        if (OptFor.equals("Annuity Payout Amount")) {
            tr_annuity_amount.setVisibility(View.VISIBLE);

            if (spnr_bi_annuity_plus_source_of_business.getSelectedItem().toString().equals("Vesting of SBI Life's Pension Policy")) {
                tr_vesting_amount.setVisibility(View.VISIBLE);
            }

        } else if (OptFor.equals("Premium Amount")) {
            tr_vesting_amount.setVisibility(View.VISIBLE);

            if (spnr_bi_annuity_plus_source_of_business.getSelectedItem().toString().equals("Vesting of SBI Life's Pension Policy")) {
                tr_additional_amount_if_any.setVisibility(View.VISIBLE);
            }
        }

    }

    private boolean valInputScreen() {

        //validate Annuity Amount
        if (tr_annuity_amount.getVisibility() == View.VISIBLE) {
            if (!valAnnuityAmt()) {
                return false;
            }
        }


        //validate Vesting Amount
        if (tr_vesting_amount.getVisibility() == View.VISIBLE) {
            if (!valVestingAmt()) {
                return false;
            }
        }

        //validate additional amount
        if (tr_additional_amount_if_any.getVisibility() == View.VISIBLE) {
            return valAdditionalAmt();
        }

        return true;
    }

    private boolean valAnnuityAmt() {
        String error = "";
        if (edt_annuity_plus_annuity_amount.getText().toString().equals("")) {
            error = "Please enter Annuity Amount in Rs.";
        } else if (!edt_annuity_plus_annuity_amount.getText().toString().equals("")) {
            if (ModeOfAnnuityPayout.equals("Yearly")
                    && Double.parseDouble(edt_annuity_plus_annuity_amount.getText().toString()) < 12000) {
                error = "Annuity Amount should not be less than Rs.12000";
            } else if (ModeOfAnnuityPayout.equals("Half Yearly")
                    && Double.parseDouble(edt_annuity_plus_annuity_amount.getText().toString()) < 6000) {
                error = "Annuity Amount should not be less than Rs.6000";
            } else if (ModeOfAnnuityPayout.equals("Quarterly")
                    && Double.parseDouble(edt_annuity_plus_annuity_amount.getText().toString()) < 3000) {
                error = "Annuity Amount should not be less than Rs.3000";
            } else if (ModeOfAnnuityPayout.equals("Monthly")
                    && Double.parseDouble(edt_annuity_plus_annuity_amount.getText().toString()) < 1000) {
                error = "Annuity Amount should not be less than Rs.1000";
            }
        }

        if (!error.equals("")) {
            commonMethods.showMessageDialog(context, error);
            return false;
        }

        return true;
    }

    //Validate Vesting Amount [called from valInputScreen()]
    private boolean valVestingAmt() {
        if (edt_annuity_plus_vesting_amount.getText().toString().equals("")) {
            commonMethods.showMessageDialog(context, "Please enter Vesting Amount in Rs.");
            return false;
        }

        return true;
    }

    //Validate Additional Amount [called from valInputScreen()]
    private boolean valAdditionalAmt() {
        if (edt_annuity_plus_additional_amount_if_any.getText().toString().equals("")) {
            commonMethods.showMessageDialog(context, "Please enter Additional Amount in Rs.");
            return false;
        }

        return true;
    }

    /*public boolean validateMinAnnuityAmount(AnnuityPlusBean annuityPlusBean)
		{
			AnnuityPlusBusinessLogic bl=new AnnuityPlusBusinessLogic(annuityPlusBean);
			String error="";

			if(annuityPlusBean.getModeOfAnnuityPayout().equals("Yearly") && bl.getAnnuityAmtForSelectedFreq() < 12000)
			{
				error = "Minimum Annuity Amount for Yearly mode is Rs.12000";
			}
			else if(annuityPlusBean.getModeOfAnnuityPayout().equals("Half Yearly") && bl.getAnnuityAmtForSelectedFreq() < 6000)
			{
				error = "Minimum Annuity Amount for Half Yearly mode is Rs.6000";
			}
			else if(annuityPlusBean.getModeOfAnnuityPayout().equals("Quarterly") && bl.getAnnuityAmtForSelectedFreq() < 3000)
			{
				error = "Minimum Annuity Amount for Quarterly mode is Rs.3000";
			}
			//Monthly
			else if(annuityPlusBean.getModeOfAnnuityPayout().equals("Monthly") && bl.getAnnuityAmtForSelectedFreq() < 1000)
			{ error = "Minimum Annuity Amount for Monthly mode is Rs.1000";}
			else
			{
				if((cb_bi_annuity_plus_adb_rider.isChecked()) && (bl.getRiderSumAssured() < 25000 || bl.getRiderSumAssured() > 5000000))
				{ error = "Rider Sum Assured should be between 25000 and 5000000";}
			}

			if(!error.equals(""))
			{
			}
			else
				return true;

		}*/
    private boolean validateMinAnnuityAmount(AnnuityPlusBean annuityPlusBean) {
        //AnnuityPlusBusinessLogic bl=new AnnuityPlusBusinessLogic(annuityPlusBean);
        String error = "";

        if (annuityPlusBean.getModeOfAnnuityPayout().equals("Yearly") && (Double.parseDouble(annuityAmtPayable)) < 12000) {
            error = "Minimum Annuity Amount for Yearly mode is Rs.12000";
        } else if (annuityPlusBean.getModeOfAnnuityPayout().equals("Half Yearly") && (Double.parseDouble(annuityAmtPayable)) < 6000) {
            error = "Minimum Annuity Amount for Half Yearly mode is Rs.6000";
        } else if (annuityPlusBean.getModeOfAnnuityPayout().equals("Quarterly") && (Double.parseDouble(annuityAmtPayable)) < 3000) {
            error = "Minimum Annuity Amount for Quarterly mode is Rs.3000";
        }
        //Monthly
        else if (annuityPlusBean.getModeOfAnnuityPayout().equals("Monthly") && (Double.parseDouble(annuityAmtPayable)) < 1000) {
            error = "Minimum Annuity Amount for Monthly mode is Rs.1000";
        } else {
            if ((cb_bi_annuity_plus_adb_rider.isChecked()) && ((Double.parseDouble(riderSumAssured)) < 25000 || (Double.parseDouble(riderSumAssured)) > 5000000)) {
                error = "Rider Sum Assured should be between 25000 and 5000000";
            }
        }

        if (!error.equals("")) {
            commonMethods.showMessageDialog(context, error);
            return false;
        } else
            return true;

    }


    private boolean valAnnuityPayoutDate(AnnuityPlusBean annuityPlusBean) {
        String error = "";

        if (cb_bi_annuity_plus_advance_annuity_payout.isChecked()) {
            noOfDaysForIntCalculation = getNoOfDaysForIntCaln(annuityPlusBean);

            //System.out.println("=} In valAnnuityPayoutDate()");
            if ((annuityPlusBean.getModeOfAnnuityPayout().equals("Yearly") || annuityPlusBean.getModeOfAnnuityPayout().equals("Half Yearly")) && annuityPlusBean.getIsAdvanceAnnuityPayout()) {
                if (annuityPlusBean.getAnnuityPayoutDate().getTime() < annuityPlusBean.getProposalDate().getTime()) {
                    if (annuityPlusBean.getModeOfAnnuityPayout().equals("Yearly")) {
                        error = "For Yearly mode Advancement Date should be between 3 to 12 months from the Proposed Annuity Purchase Date";

                    } else if (annuityPlusBean.getModeOfAnnuityPayout().equals("Half Yearly")) {
                        error = "For Half Yearly Mode Advancement Date should be between 3 to 6 months from the Proposed Annuity Purchase Date";

                    }

                } else {
                    if (annuityPlusBean.getModeOfAnnuityPayout().equals("Yearly")) {
                        if (commonForAllProd.getMessage(annuityPlusBean.getProposalDate(), annuityPlusBean.getAnnuityPayoutDate(), "Yearly", noOfDaysForIntCalculation).equals("displayMsg")) {
                            error = "For Yearly Mode Advancement Date should be between 3 to 12 months from the Proposed Annuity Purchase Date";

                        }

                    } else if (annuityPlusBean.getModeOfAnnuityPayout().equals("Half Yearly")) {
                        if (commonForAllProd.getMessage(annuityPlusBean.getProposalDate(), annuityPlusBean.getAnnuityPayoutDate(), "Half Yearly", noOfDaysForIntCalculation).equals("displayMsg")) {
                            error = "For Half Yearly Mode Advancement Date should be between 3 to 6 months from the Proposed Annuity Purchase Date";

                        }

                    }

                }
            }


            if (!error.equals("")) {
                commonMethods.showMessageDialog(context, error);
                return false;
            }

        }

        return true;
    }


    //returns days difference between proposal date and ideal date [called from valAnnuityPayoutDate(AnnuityPlusBean annuityPlusBean)]
/*		public long getNoOfDaysForIntCaln(AnnuityPlusBean annuityPlusBean)
		{

			double riderSumAssured=0,riderPremium=0,totServiceTax=0,advancePremPayable=0;
			AnnuityPlusBusinessLogic bl=new AnnuityPlusBusinessLogic(annuityPlusBean);

			String purchasePrice=commonForAllProd.roundUp_Level2_Ceil(""+commonForAllProd.getStringWithout_E(bl.getPurchasePrice()));
			if(annuityPlusBean.getIsADB())
			{
				riderSumAssured=bl.getRiderSumAssured();
				riderPremium=bl.getRiderPremiumFinal(riderSumAssured);
			}
			totServiceTax=bl.getTotalServiceTax();

			//System.out.println("@1");
			long factor=0;


			if((annuityPlusBean.getModeOfAnnuityPayout().equals("Yearly") || annuityPlusBean.getModeOfAnnuityPayout().equals("Half Yearly")) && annuityPlusBean.getIsAdvanceAnnuityPayout())
			{
				if(annuityPlusBean.getModeOfAnnuityPayout().toString().equals("Yearly"))
				{factor=annuityPlusBean.getAnnuityPayoutDate().getTime()-31536000000L;}
				else
				{factor=annuityPlusBean.getAnnuityPayoutDate().getTime()-15768000000L;}
			}
			//System.out.println("@2");
			idealDate = new Date(factor);
			//System.out.println("@3");
			int intAnnuityPayoutDate=0,intIdealDate=0;
			long noOfDaysForIntCaln=0;

			if((annuityPlusBean.getModeOfAnnuityPayout().equals("Yearly")  || annuityPlusBean.getModeOfAnnuityPayout().equals("Half Yearly")) && annuityPlusBean.getIsAdvanceAnnuityPayout())
			{
				intAnnuityPayoutDate=Integer.parseInt(annuityPlusBean.getAnnuityPayoutDate().toString().substring(8,10));
				intIdealDate=Integer.parseInt(idealDate.toString().substring(8,10));


				//To match with escel date comparison
				for(int i=1;i<10;i++)
				{
					if(intAnnuityPayoutDate > intIdealDate)
					{
						idealDate = new Date(factor+86400000*i);
						intIdealDate=Integer.parseInt(idealDate.toString().substring(8,10));

					}
					else if(intAnnuityPayoutDate < intIdealDate)
					{
						idealDate = new Date(factor-86400000*i);
						intIdealDate=Integer.parseInt(idealDate.toString().substring(8,10));

					}
					else if(intAnnuityPayoutDate == intIdealDate)
					{break;}
				}

				if(ModeOfAnnuityPayout.equals("Yearly"))
				{
					noOfDaysForIntCaln=(commonForAllProd.getDaysDiff(annuityPlusBean.getProposalDate(), idealDate))+1;
				}
				else if(ModeOfAnnuityPayout.equals("Half Yearly"))
				{
					noOfDaysForIntCaln=(commonForAllProd.getDaysDiff(annuityPlusBean.getProposalDate(), idealDate))+2;
				}
			}
			return noOfDaysForIntCaln;
		}*/

    private long getNoOfDaysForIntCaln(AnnuityPlusBean annuityPlusBean) {

        //double riderSumAssured=0,riderPremium=0,totServiceTax=0,advancePremPayable=0;
        //AnnuityPlusBusinessLogic bl=new AnnuityPlusBusinessLogic(annuityPlusBean);

        //String purchasePrice=commonForAllProd.roundUp_Level2_Ceil(""+commonForAllProd.getStringWithout_E((Double.parseDouble(purchasePrice))));
        if (annuityPlusBean.getIsADB()) {
            //riderSumAssured=bl.getRiderSumAssured();
            //riderPremium=bl.getRiderPremiumFinal(riderSumAssured);
        }
        //totServiceTax=bl.getTotalServiceTax();

        //System.out.println("@1");
        long factor = 0;


        if ((annuityPlusBean.getModeOfAnnuityPayout().equals("Yearly") || annuityPlusBean.getModeOfAnnuityPayout().equals("Half Yearly")) && annuityPlusBean.getIsAdvanceAnnuityPayout()) {
            if (annuityPlusBean.getModeOfAnnuityPayout().equals("Yearly")) {
                factor = annuityPlusBean.getAnnuityPayoutDate().getTime() - 31536000000L;
            } else {
                factor = annuityPlusBean.getAnnuityPayoutDate().getTime() - 15768000000L;
            }
        }
        //System.out.println("@2");
        idealDate = new Date(factor);
        //System.out.println("@3");
        int intAnnuityPayoutDate, intIdealDate;
        long noOfDaysForIntCaln = 0;

        if ((annuityPlusBean.getModeOfAnnuityPayout().equals("Yearly") || annuityPlusBean.getModeOfAnnuityPayout().equals("Half Yearly")) && annuityPlusBean.getIsAdvanceAnnuityPayout()) {
            intAnnuityPayoutDate = Integer.parseInt(annuityPlusBean.getAnnuityPayoutDate().toString().substring(8, 10));
            intIdealDate = Integer.parseInt(idealDate.toString().substring(8, 10));


            //To match with escel date comparison
            for (int i = 1; i < 10; i++) {
                if (intAnnuityPayoutDate > intIdealDate) {
                    idealDate = new Date(factor + 86400000 * i);
                    intIdealDate = Integer.parseInt(idealDate.toString().substring(8, 10));

                } else if (intAnnuityPayoutDate < intIdealDate) {
                    idealDate = new Date(factor - 86400000 * i);
                    intIdealDate = Integer.parseInt(idealDate.toString().substring(8, 10));

                } else if (intAnnuityPayoutDate == intIdealDate) {
                    break;
                }
            }

            if (ModeOfAnnuityPayout.equals("Yearly")) {
                noOfDaysForIntCaln = (commonForAllProd.getDaysDiff(annuityPlusBean.getProposalDate(), idealDate)) + 1;
            } else if (ModeOfAnnuityPayout.equals("Half Yearly")) {
                noOfDaysForIntCaln = (commonForAllProd.getDaysDiff(annuityPlusBean.getProposalDate(), idealDate)) + 2;
            }
        }
        return noOfDaysForIntCaln;
    }


    private void Date() {
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        String formattedDate = df.format(c.getTime());
        // formattedDate have current date/time
        // Toast.makeText(this, formattedDate, Toast.LENGTH_SHORT).show();
        c.add(Calendar.DATE, 30);
        String expiredDate = df.format(c.getTime());
    }


    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        // TODO Auto-generated method stub
        if (v.getId() == edt_bi_annuity_plus_life_assured_first_name
                .getId()) {
            commonMethods.setFocusable(edt_bi_annuity_plus_life_assured_middle_name);
            edt_bi_annuity_plus_life_assured_middle_name.requestFocus();
        } else if (v.getId() == edt_bi_annuity_plus_life_assured_middle_name
                .getId()) {
            commonMethods.setFocusable(edt_bi_annuity_plus_life_assured_last_name);
            edt_bi_annuity_plus_life_assured_last_name.requestFocus();
        } else if (v.getId() == edt_bi_annuity_plus_life_assured_last_name
                .getId()) {

            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(
                    edt_bi_annuity_plus_life_assured_last_name
                            .getWindowToken(), 0);
            commonMethods.setFocusable(btn_bi_annuity_plus_life_assured_date);
            btn_bi_annuity_plus_life_assured_date.requestFocus();
        } else if (v.getId() == edt_bi_annuity_plus_life_assured_first_name_second_annuitant
                .getId()) {
            commonMethods.setFocusable(edt_bi_annuity_plus_life_assured_middle_name_second_annuitant);
            edt_bi_annuity_plus_life_assured_middle_name_second_annuitant.requestFocus();
        } else if (v.getId() == edt_bi_annuity_plus_life_assured_middle_name_second_annuitant
                .getId()) {
            commonMethods.setFocusable(edt_bi_annuity_plus_life_assured_last_name_second_annuitant);
            edt_bi_annuity_plus_life_assured_last_name_second_annuitant.requestFocus();
        } else if (v.getId() == edt_bi_annuity_plus_life_assured_last_name_second_annuitant
                .getId()) {

            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(edt_bi_annuity_plus_life_assured_last_name_second_annuitant.getWindowToken(), 0);
            commonMethods.setFocusable(btn_bi_annuity_plus_life_assured_date_second_annuitant);
            btn_bi_annuity_plus_life_assured_date_second_annuitant.requestFocus();
        } else if (v.getId() == edt_annuity_plus_contact_no.getId()) {
            commonMethods.setFocusable(edt_annuity_plus_Email_id);
            edt_annuity_plus_Email_id.requestFocus();
        } else if (v.getId() == edt_annuity_plus_Email_id.getId()) {
            commonMethods.setFocusable(edt_annuity_plus_ConfirmEmail_id);
            edt_annuity_plus_ConfirmEmail_id.requestFocus();
        } else if (v.getId() == edt_annuity_plus_ConfirmEmail_id
                .getId()) {
            commonMethods.setFocusable(spnr_bi_annuity_plus_source_of_business);
            spnr_bi_annuity_plus_source_of_business.requestFocus();
        } else if (v.getId() == edt_annuity_plus_annuity_amount.getId()) {
            commonMethods.setFocusable(edt_annuity_plus_vesting_amount);
            edt_annuity_plus_vesting_amount.requestFocus();

        } else if (v.getId() == edt_annuity_plus_vesting_amount.getId()) {
            commonMethods.setFocusable(edt_annuity_plus_additional_amount_if_any);
            edt_annuity_plus_additional_amount_if_any.requestFocus();

        } else if (v.getId() == edt_annuity_plus_additional_amount_if_any.getId()) {
            commonMethods.hideKeyboard(edt_annuity_plus_additional_amount_if_any, context);
            commonMethods.clearFocusable(cb_bi_annuity_plus_adb_rider);
            commonMethods.setFocusable(cb_bi_annuity_plus_adb_rider);
            cb_bi_annuity_plus_adb_rider.requestFocus();

        } else if (v.getId() == spnr_bi_annuity_plus_applicable_for.getId()) {
            commonMethods.hideKeyboard(edt_annuity_plus_additional_amount_if_any, context);
        }

        return true;
    }

    public boolean valPurchaseAnnuity() {
        if (str_annuity_plus_purchase_annuity_for.equalsIgnoreCase("")) {
            commonMethods.dialogWarning(context, "Please select purchase annuity for", false);
            commonMethods.clearFocusable(spnr_bi_annuity_plus_purchase_annuity_for);
            commonMethods.setFocusable(spnr_bi_annuity_plus_purchase_annuity_for);
            spnr_bi_annuity_plus_purchase_annuity_for.requestFocus();
            return false;
        } else if (str_annuity_plus_immediate_annuity_plan_for.equalsIgnoreCase("")) {
            commonMethods.dialogWarning(context, "Please select immediate annuity plan for", false);
            commonMethods.clearFocusable(spnr_bi_annuity_plus_immediate_annuity_plan_for);
            commonMethods.setFocusable(spnr_bi_annuity_plus_immediate_annuity_plan_for);
            spnr_bi_annuity_plus_immediate_annuity_plan_for.requestFocus();
            return false;
        } else {
            return true;
        }
    }


    private boolean valJointLifeAnnityOptionDob() {
        if ((spnr_bi_annuity_plus_annuity_option.getSelectedItem().toString().equals("Life and Last Survivor - 50% Income")
                || spnr_bi_annuity_plus_annuity_option.getSelectedItem().toString().equals("Life and Last Survivor - 100% Income")
                || spnr_bi_annuity_plus_annuity_option.getSelectedItem().toString().equals("Life and Last Survivor with Capital Refund - 50% Income")
                || spnr_bi_annuity_plus_annuity_option.getSelectedItem().toString().equals("Life and Last Survivor with Capital Refund - 100% Income"))
                && (((Integer.parseInt(edt_bi_annuity_plus_life_assured_age.getText().toString())) - (Integer.parseInt(edt_bi_annuity_plus_life_assured_age_second_annuitant.getText().toString()))) > 30)) {
            commonMethods.showMessageDialog(context, "Maximum Age difference between First Annitant and Second Annuitant should be 30");
            commonMethods.setFocusable(btn_bi_annuity_plus_life_assured_date);
            btn_bi_annuity_plus_life_assured_date
                    .requestFocus();
            return false;
        } else if ((spnr_bi_annuity_plus_annuity_option.getSelectedItem().toString().equals("Life and Last Survivor - 50% Income")
                || spnr_bi_annuity_plus_annuity_option.getSelectedItem().toString().equals("Life and Last Survivor - 100% Income")
                || spnr_bi_annuity_plus_annuity_option.getSelectedItem().toString().equals("Life and Last Survivor with Capital Refund - 50% Income")
                || spnr_bi_annuity_plus_annuity_option.getSelectedItem().toString().equals("Life and Last Survivor with Capital Refund - 100% Income"))
                && (((Integer.parseInt(edt_bi_annuity_plus_life_assured_age_second_annuitant.getText().toString())) - (Integer.parseInt(edt_bi_annuity_plus_life_assured_age.getText().toString()))) > 30)) {
            commonMethods.showMessageDialog(context, "Maximum Age difference between First Annitant and Second Annuitant should be 30");
            commonMethods.setFocusable(btn_bi_annuity_plus_life_assured_date);
            btn_bi_annuity_plus_life_assured_date
                    .requestFocus();
            return false;
        } else {
            return true;
        }
    }

    private void addListenerOnSubmit_New() {
        String METHOD_NAME_CALC = "getPremiumAnnuityPlusNew";
        String SOAP_ACTION_CALC = "http://tempuri.org/getPremiumAnnuityPlusNew";
        String URL_CALC = ServiceURL.SERVICE_URL;//server variables
        String NAMESPACE_CALC = "http://tempuri.org/";
        AsyncAnnuityPlus service = new AsyncAnnuityPlus(BI_AnnuityPlusActivity.this, NAMESPACE_CALC, URL_CALC, SOAP_ACTION_CALC, METHOD_NAME_CALC);
        service.execute();
    }

    class AsyncAnnuityPlus extends AsyncTask<String, Void, String> {
        final Context mContext;
        ProgressDialog progressDialog = null;
        String NAMESPACE;
        String URL;
        String SOAP_ACTION;
        String METHOD_NAME;
        //String result,modeOfAnnuityPayout,annuityAmtPayable,purchasePrice,riderSumAssured,riderPrem,totServiceTax,advancePremPayable,totalPrem,totalPremReq,addPremPayable;
        final DecimalFormat currencyFormat;
        String selApplicableForText, selSourceOfBusinessText, selChannelDetailsText,
                selAnnuityOptionText, selFirstAnnutantGenderText, selOptForText, edAdditionalAmtText,
                selSecondAnnutantAgeText, selFirstAnnutantAgeText, selSecondAnnutantGenderText, edVestingAmtText, edAnnuityAmtText,
                AdvAnnPayoutDateText, isStaff;
        boolean selJKResidentBool, selAdvAnnPayoutBool, selADBRiderBool, KFC;


        AsyncAnnuityPlus(Context mContext, String NAMESPACE, String URL, String SOAP_ACTION, String METHOD_NAME) {
            // TODO Auto-generated constructor stub

            this.NAMESPACE = NAMESPACE;
            this.URL = URL;
            this.SOAP_ACTION = SOAP_ACTION;
            this.METHOD_NAME = METHOD_NAME;
            this.mContext = mContext;
            currencyFormat = new DecimalFormat("##,##,##,###");
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(context, ProgressDialog.THEME_HOLO_LIGHT);
            String Message = "Loading Please wait...";
            progressDialog.setMessage(Html.fromHtml("<font color='#00a1e3'><b>" + Message + "<b></font>"));
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setCancelable(true);
            progressDialog.setMax(100);
            progressDialog.show();

            isStaff = String.valueOf(cb_staffdisc.isChecked());
            selApplicableForText = spnr_bi_annuity_plus_applicable_for.getSelectedItem().toString();
            selSourceOfBusinessText = spnr_bi_annuity_plus_source_of_business.getSelectedItem().toString();
            selChannelDetailsText = spnr_bi_annuity_plus_channel_details.getSelectedItem().toString();
            selAnnuityOptionText = spnr_bi_annuity_plus_annuity_option.getSelectedItem().toString();
            selFirstAnnutantGenderText = selFirstAnnutantGender.getSelectedItem().toString();
            selOptForText = spnr_bi_annuity_plus_opt_for.getSelectedItem().toString();
            edAdditionalAmtText = edt_annuity_plus_additional_amount_if_any.getText().toString();
            selSecondAnnutantAgeText = edt_bi_annuity_plus_life_assured_age_second_annuitant.getText().toString();
            selFirstAnnutantAgeText = edt_bi_annuity_plus_life_assured_age.getText().toString();
            selSecondAnnutantGenderText = selSecondAnnutantGender.getSelectedItem().toString();
            edVestingAmtText = edt_annuity_plus_vesting_amount.getText().toString();
            edAnnuityAmtText = edt_annuity_plus_annuity_amount.getText().toString();
            selAdvAnnPayoutBool = cb_bi_annuity_plus_advance_annuity_payout.isChecked();
            KFC = cb_kerladisc.isChecked();

        }

        @Override
        protected String doInBackground(String... param) {
            // TODO Auto-generated method stub

            if (commonMethods.isNetworkConnected(context)) {

                try {
                    SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);


//					if(selStaffNonStaff.getSelectedItem().toString().equals("Staff"))
//					{request.addProperty("isStaff","true");}

//					request.addProperty("isStaff","Staff");

                    //request.addProperty("isStaff","false");
                    request.addProperty("isStaff", isStaff);
                    request.addProperty("isJkResident", "false");


                    if ((ModeOfAnnuityPayout.equals("Half Yearly")
                            || ModeOfAnnuityPayout.equals("Yearly"))
                            && selAdvAnnPayoutBool) {
                        request.addProperty("isAdvanceAnnuityPayout", "true");
                    } else if ((ModeOfAnnuityPayout.equals("Half Yearly")
                            || ModeOfAnnuityPayout.equals("Yearly"))
                            && !selAdvAnnPayoutBool) {
                        request.addProperty("isAdvanceAnnuityPayout", "false");
                    } else {
                        request.addProperty("isAdvanceAnnuityPayout", "false");
                    }

					/*if(cb_bi_annuity_plus_adb_rider.isChecked())
					{
						request.addProperty("isADB","true");
						request.addProperty("applicableFor",spnr_bi_annuity_plus_applicable_for.getSelectedItem().toString());

					}
					else
					{
						request.addProperty("isADB","false");
						request.addProperty("applicableFor","");
					}*/


                    request.addProperty("sourceOfBusiness", selSourceOfBusinessText);
                    request.addProperty("channelDetails", selChannelDetailsText);
                    request.addProperty("modeOfAnnuityPayout", ModeOfAnnuityPayout);
                    request.addProperty("annuityOption", selAnnuityOptionText);
                    //request.addProperty("genderOfFirstAnnuitant",selFirstAnnutantGender.getSelectedItem().toString());
                    request.addProperty("genderOfFirstAnnuitant", gender);
                    request.addProperty("optFor", selOptForText);

                    try {
//					request.addProperty("proposalDate",new SimpleDateFormat("MM/dd/yyyy",Locale.ENGLISH).parse(proposalDate.getText().toString()));
                        //request.addProperty("proposalDate",proposalDate.getText().toString());
                        request.addProperty("proposalDate", proposal_date.replace("-", "/"));

                    } catch (Exception ignored) {

                    }


                    if (selAdvAnnPayoutBool) {
                        try {
                            request.addProperty("annuityPayoutDate", annuity_plus_advance_annuity_payout_from_which_date.replace("-", "/"));

                        } catch (Exception e) {
                            //        	  System.out.println("In exception " + e.getMessage());
                        }
                    } else {
                        request.addProperty("annuityPayoutDate", "");
                    }

                    request.addProperty("ageOfFirstAnnuitant", selFirstAnnutantAgeText);


                    if ((selAnnuityOptionText.equals("Life and Last Survivor - 50% Income") || selAnnuityOptionText.equals("Life and Last Survivor - 100% Income") || selAnnuityOptionText.equals("Life and Last Survivor with Capital Refund - 50% Income") || selAnnuityOptionText.equals("Life and Last Survivor with Capital Refund - 100% Income"))) {

                        request.addProperty("ageOfSecondAnnuitant", selSecondAnnutantAgeText);
                        request.addProperty("genderOfSecondAnnuitant", gender_2);

                    } else {
                        request.addProperty("ageOfSecondAnnuitant", "");
                        request.addProperty("genderOfSecondAnnuitant", "");
                    }

                    if (selSourceOfBusinessText.equals("Vesting/Death/Surrender of existing SBI Life's pension policy")) {

                        if (selOptForText.equals("Annuity Payout Amount")) {

                            request.addProperty("additionalAmountIfAny", "0");
                            request.addProperty("vestingAmount", edVestingAmtText);
                            request.addProperty("annuityAmount", edAnnuityAmtText);

                        } else if (selOptForText.equals("Premium Amount")) {
                            request.addProperty("additionalAmountIfAny", edAdditionalAmtText);
                            request.addProperty("vestingAmount", edVestingAmtText);
                            request.addProperty("annuityAmount", "0");

                        }
                    }
                    //Other than Vesting/Death/Surrender of existing SBI Life's pension policy
                    else {
                        if (selOptForText.equals("Annuity Payout Amount")) {
                            request.addProperty("additionalAmountIfAny", "0");
                            request.addProperty("vestingAmount", "0");
                            request.addProperty("annuityAmount", edAnnuityAmtText);

                        } else if (selOptForText.equals("Premium Amount")) {
                            request.addProperty("additionalAmountIfAny", "0");
                            request.addProperty("vestingAmount", edVestingAmtText);
                            request.addProperty("annuityAmount", "0");

                        }
                    }

                    request.addProperty("KFC", KFC);

                    Log.e("Request : ", " -- " + request.toString());


                    SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                    //Enable this envelope if service is written in dot net
                    envelope.dotNet = true;
                    envelope.setOutputSoapObject(request);
                    HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

                    commonMethods.TLSv12Enable();

                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);

                    androidHttpTransport.call(SOAP_ACTION, envelope);
                    SoapPrimitive response = (SoapPrimitive) envelope.getResponse();

                    String result = response.toString();
                    result_grid = result;
//					System.out.println("result " + result);

                    if (!result.contains("<errorMessage>")) {

                        ParseXML prsObj = new ParseXML();

                        if (result.contains("<annuityPayoutDateError>")) {
                            result = prsObj.parseXmlTag(result, "annuityPayoutDateError");
                            return result;
                        } else if (result.contains("<minAnnuityAmountError>")) {
                            result = prsObj.parseXmlTag(result, "minAnnuityAmountError");
                            return result;
                        } else {

                            result = prsObj.parseXmlTag(result, "annuityPlus");
                            modeOfAnnuityPayout = prsObj.parseXmlTag(result, "modeOfAnnuityPayout");
                            annuityAmtPayable = prsObj.parseXmlTag(result, "annuityAmtPayable");
                            purchasePrice = prsObj.parseXmlTag(result, "purchasePrice");
                            totalPurchasePrice = prsObj.parseXmlTag(result, "totalPurchasePrice");
                            riderSumAssured = prsObj.parseXmlTag(result, "riderSumAssured");
                            if (riderSumAssured == null) {
                                riderSumAssured = "0";
                            }
                            policyTerm = prsObj.parseXmlTag(result, "policyTerm");
                            policyTerm = policyTerm == null ? "0" : policyTerm;
                            riderPrem = prsObj.parseXmlTag(result, "riderPrem");
                            if (riderPrem == null) {
                                riderPrem = "0";
                            }
                            riderPrem1 = prsObj.parseXmlTag(result, "riderPrem1");
                            if (riderPrem1 == null) {
                                riderPrem1 = "0";
                            }
                            basicServiceTax = prsObj.parseXmlTag(result, "basicServiceTax");
                            SBC_serviceTax = prsObj.parseXmlTag(result, "SBC_serviceTax");
                            KKC_serviceTax = prsObj.parseXmlTag(result, "KKC_serviceTax");
                            totServiceTax = prsObj.parseXmlTag(result, "totServiceTax");
                            advancePremPayable = prsObj.parseXmlTag(result, "advancePremPayable");
                            totalPrem = prsObj.parseXmlTag(result, "totalPrem");
                            totalPremReq = prsObj.parseXmlTag(result, "totalPremReq");
                            addPremPayable = prsObj.parseXmlTag(result, "addPremPayable");
                            staffStatus = prsObj.parseXmlTag(result, "staffStatus");
                            netPremium = prsObj.parseXmlTag(result, "netPremium");


                        }
                        return "Success";

                    } else {
                        return "Server not Found. Please try after some time.";
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    return "Server not Found. Please try after some time.";
                }

            } else
                return "Please Activate Internet connection";

        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

            try {
                if (progressDialog.isShowing())
                    progressDialog.dismiss();

            } catch (Exception e) {
                e.getMessage();
            }

            if (result.equals("Success")) {

                addListenerOnSubmit();
                if (val_annuity_payout_date && val_annuity_amount) {
                    getInput(annuityPlusBean);
                    Dialog();
                }

				/*Intent i = new Intent(mContext,success.class);

				i.putExtra("op","Mode of Annuity Payout is " +modeOfAnnuityPayout);
				i.putExtra("op1","Annuity Amount Payable is Rs." + currencyFormat.format(Double.parseDouble(annuityAmtPayable)));
				i.putExtra("op2","Purchase Price is Rs. " + currencyFormat.format(Double.parseDouble(purchasePrice)));

				if(cb_bi_annuity_plus_adb_rider.isChecked())
				{
					i.putExtra("op3","Rider Sum Assured is Rs. " + currencyFormat.format(Double.parseDouble(riderSumAssured)));
					i.putExtra("op4","Rider Premium is Rs. " + currencyFormat.format(Double.parseDouble(riderPrem)));
				}

				i.putExtra("op5","Total Applicable Taxes is Rs. " + currencyFormat.format(Double.parseDouble(totServiceTax)));

				if(cb_bi_annuity_plus_advance_annuity_payout.isChecked())
				{
					i.putExtra("op6","Advance Premium Payable is Rs. " + currencyFormat.format(Double.parseDouble(advancePremPayable)));
				}

				i.putExtra("op7","Total Premium is Rs. " + currencyFormat.format(Double.parseDouble(totalPrem)));

				*//*
				if(spnr_bi_annuity_plus_source_of_business.getSelectedItem().toString().equals("Vesting/Death/Surrender of existing SBI Life's pension policy") || spnr_bi_annuity_plus_source_of_business.getSelectedItem().toString().equals("Open Market Option (Any Other Life Insurance Company Pension Policy)"))
				{
					i.putExtra("op8","Total Premium Required is Rs. " + currencyFormat.format(Double.parseDouble(totalPremReq)));
					i.putExtra("op9","Additional Premium Payable is Rs. " + currencyFormat.format(Double.parseDouble(addPremPayable)));
				}
				*//*

				i.putExtra("ProductDescName","SBI Life - Annuity Plus");

				mContext.startActivity(i);*/


            } else {
                Toast.makeText(mContext, result, Toast.LENGTH_LONG).show();
            }
        }


    }


    private double getStaffRebate(boolean IsStaff) {

        if (IsStaff) {
            StaffRebate = 0.02;
        } else {
            StaffRebate = 0.00;
        }
        return StaffRebate;

    }

}

