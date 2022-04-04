package sbilife.com.pointofsale_bancaagency.products.saralpensionnew;

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
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import java.text.NumberFormat;
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
import sbilife.com.pointofsale_bancaagency.annuityplus.success;
import sbilife.com.pointofsale_bancaagency.common.CommonForAllProd;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;
import sbilife.com.pointofsale_bancaagency.common.ProductInfo;
import sbilife.com.pointofsale_bancaagency.common.SimpleCrypto;
import sbilife.com.pointofsale_bancaagency.common.StorageUtils;
import sbilife.com.pointofsale_bancaagency.needanalysis.NeedAnalysisActivity;
import sbilife.com.pointofsale_bancaagency.new_bussiness.ProductBIBean;
import sbilife.com.pointofsale_bancaagency.utility.GridHeight;


public class BI_SaralPensionNewActivity extends AppCompatActivity implements OnEditorActionListener {

    private CheckBox cb_staffdisc, cb_bi_annuity_plus_advance_annuity_payout,
            cb_bi_annuity_plus_adb_rider;

    private EditText edt_bi_annuity_plus_life_assured_first_name, edt_bi_annuity_plus_life_assured_middle_name,
            edt_bi_annuity_plus_life_assured_last_name, edt_bi_annuity_plus_life_assured_age,
            edt_bi_annuity_plus_life_assured_first_name_second_annuitant, edt_bi_annuity_plus_life_assured_middle_name_second_annuitant,
            edt_bi_annuity_plus_life_assured_last_name_second_annuitant, edt_bi_annuity_plus_life_assured_age_second_annuitant,
            edt_annuity_plus_contact_no,
            edt_annuity_plus_Email_id, edt_annuity_plus_ConfirmEmail_id, edt_annuity_plus_annuity_amount, edt_annuity_plus_vesting_amount,
            edt_annuity_plus_additional_amount_if_any;

    private Spinner spnr_bi_annuity_plus_life_assured_title, spnr_bi_annuity_plus_life_assured_title_second_annuitant,
            spnr_bi_annuity_plus_source_of_business, spnr_bi_annuity_plus_channel_details,
            spnr_bi_annuity_plus_mode_of_annuity_payouts, spnr_bi_annuity_plus_annuity_option;

    private TextView btn_bi_annuity_plus_life_assured_date, btn_bi_annuity_plus_life_assured_date_second_annuitant,
            btn_bi_annuity_plus_proposer_date, btn_bi_annuity_plus_proposal_date, btn_bi_annuity_plus_advance_annuity_payout_from_which_date;
    private Button btnBack, btnSubmit;
    private TableRow tr_bi_annuity_plus_advance_annuity_payout, tr_bi_annuity_plus_adb_rider;
    private LinearLayout linearlayoutSecondAnnuitant;

    public String frmProductHome = "";
    private String QuatationNumber;
    private final String planName = "";
    private DatabaseHelper db;
    private ParseXML prsObj;

    private String emailId = "", mobileNo = "", ConfirmEmailId = "", ProposerEmailId = "";
    private boolean validationFla1 = false;
    private int DIALOG_ID;
    private int mYear;
    private int mMonth;
    private int mDay;
    private String name_of_second_annuitant = "", name_of_person = "",
            place1 = "", place2 = "", date1 = "", date2 = "";
    private String agent_sign = "", proposer_sign = "";
    private String flg_needAnalyis = "";

    private String second_Annuitant_First_Name = "",
            second_Annuitant_Middle_Name = "", second_Annuitant_Last_Name = "",
            productName = "", is_Second_Annuitant = "N", second_Annuitant_date_of_birth = "";
    private String lifeAssured_Title = "", lifeAssured_First_Name = "",
            lifeAssured_Middle_Name = "", lifeAssured_Last_Name = "",
            name_of_life_assured = "", lifeAssured_date_of_birth = "",
            annuity_plus_advance_annuity_payout_from_which_date = "", proposal_date = "";

    private String lifeAssured_Title_Second_Annuitant = "";
    private CommonForAllProd commonForAllProd, obj;
    private StringBuilder retVal, inputVal;
    private SaralPensionNewBean saralPensionNewBean;
    private String proposer_Title = "", proposer_First_Name = "",
            proposer_Middle_Name = "", proposer_Last_Name = "",
            proposer_Is_Same_As_Life_Assured = "N",
            name_of_proposer = "", proposer_date_of_birth = "";

    private Boolean flag = false;
    private Bitmap photoBitmap;
    private String propsoser_gender = "";
    private String latestImage = "";
    private Button btn_MarketingOfficalDate;
    private Button btn_PolicyholderDate;
    private ImageButton Ibtn_signatureofMarketing, Ibtn_signatureofPolicyHolders;
    private String first_annuitant_age = "";
    private String second_annuitant_age = "";
    private String source_of_business = "";
    private String annuity_option = "";
    private String mode_of_annuity_payout = "";
    private String annuity_amount = "";
    private final String purchase_price = "";
    private String AnnuityOption = "";
    private String gender = "";
    private String gender_2 = "";
    private String ModeOfAnnuityPayout = "";
    private DecimalFormat currencyFormat;
    private File mypath;

    private Spinner spnr_bi_annuity_plus_purchase_annuity_for, spnr_bi_annuity_plus_immediate_annuity_plan_for;
    private String str_annuity_plus_purchase_annuity_for = "", str_annuity_plus_immediate_annuity_plan_for = "";

    private EditText edt_bi_annuity_plus_proposer_first_name,
            edt_bi_annuity_plus_proposer_middle_name,
            edt_bi_annuity_plus_proposer_last_name, edt_bi_annuity_plus_proposer_age;
    private CheckBox cb_different_from_proposer;
    private Spinner spnr_bi_annuity_plus_proposer_title;
    private String proposer_age = "", Proposer_is_same_as_Annuitant = "N";

    private CheckBox cb_kerladisc;
    private String str_kerla_discount = "No";
    private ProgressDialog progressDialog;
    //server variables
    final String NAMESPACE_CALC = "http://tempuri.org/";
    final String URL_CALC = ServiceURL.SERVICE_URL;
    final String SOAP_ACTION_CALC = "http://tempuri.org/getPremiumSaralPensionNext";
    final String METHOD_NAME_CALC = "getPremiumSaralPensionNext";

    final String str_agent_name = "";
    private List<M_BI_Annuity_Plus_Adapter> list_data;
    private String result_grid = "";
    private String SumAssured = "";
    private String SumAssuredonDeath = "";
    private String Annuitypayable = "";
    private String ServiceTax = "";
    private String annuity_amt_payable = "";
    private String BasicPrem = "";
    private String TotalInstallmentPrem = "";
    private String FYInstalmentPremium = "";
    private String FYTotalInstallmentPrem = "";
    private String str_header_annuity_amt_payable = "";

    private StorageUtils mStorageUtils;
    private CommonMethods commonMethods;
    private Context context;
    private NeedAnalysisBIService NABIObj;
    private NA_CBI_bean na_cbi_bean;
    private File needAnalysispath, newFile;
    private String agentcode, agentMobile, agentEmail, userType;
    private String na_input = null;
    private String na_output = null;
    private String bankUserType = "", mode = "";
    private String product_Code;
    private String product_UIN;
    private String product_cateogory;
    private String product_type;
    private Spinner selGender, selFirstAnnutantGender, selSecondAnnutantGender;
    private String Check = "";
    private ImageView imageButtonShubhNiveshProposerPhotograph;
    private int needAnalysis_flag = 0;
    private int payoutMonth = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.bi_saralpensionnew);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);


        UI_Declaration();
        db = new DatabaseHelper(this);
        context = this;
        mStorageUtils = new StorageUtils();
        commonMethods = new CommonMethods();
        commonMethods.setActionbarLayout(this);

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
                    userType = SimpleCrypto.decrypt("SBIL",
                            db.GetUserType());

                    productName = "Saral Pension";
                    /* parivartan changes */
                    ProductInfo prodInfoObj = new ProductInfo(context);
                    product_Code = prodInfoObj.getProductCode(productName);
                    product_UIN = prodInfoObj.getProductUIN(productName);
                    product_cateogory = prodInfoObj.getProductCategory(productName);
                    product_type = prodInfoObj.getProductType(productName);
                    /* end */
                    System.out.println("product_Code = " + product_Code);
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

        Date();

        //edt_annuity_plus_contact_no.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
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

        list_data = new ArrayList<>();

        validationOfMoile_EmailId();
        setSpinnerAndOtherListner();
        saralPensionNewBean = new SaralPensionNewBean();
        obj = new CommonForAllProd();
        currencyFormat = new DecimalFormat("##,##,##,###");
        commonForAllProd = new CommonForAllProd();
        obj = new CommonForAllProd();

    }


    public void UI_Declaration() {

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

        btn_bi_annuity_plus_life_assured_date = findViewById(R.id.btn_bi_annuity_plus_life_assured_date);
        btn_bi_annuity_plus_life_assured_date_second_annuitant = findViewById(R.id.btn_bi_annuity_plus_life_assured_date_second_annuitant);
        btn_bi_annuity_plus_proposal_date = findViewById(R.id.btn_bi_annuity_plus_proposal_date);
        proposal_date = getDate1(getCurrentDate());
        btn_bi_annuity_plus_proposal_date.setText(getCurrentDate());
        btn_bi_annuity_plus_advance_annuity_payout_from_which_date = findViewById(R.id.btn_bi_annuity_plus_advance_annuity_payout_from_which_date);
        btnBack = findViewById(R.id.btn_bi_annuity_plus_btnback);
        btnSubmit = findViewById(R.id.btn_bi_annuity_plus_btnSubmit);

        tr_bi_annuity_plus_advance_annuity_payout = findViewById(R.id.tr_bi_annuity_plus_advance_annuity_payout);
        tr_bi_annuity_plus_adb_rider = findViewById(R.id.tr_bi_annuity_plus_adb_rider);

        linearlayoutSecondAnnuitant = findViewById(R.id.linearlayoutSecondAnnuitant);

        selGender = findViewById(R.id.selGender);
        selFirstAnnutantGender = findViewById(R.id.selFirstAnnutantGender);
        selSecondAnnutantGender = findViewById(R.id.selSecondAnnutantGender);

        spnr_bi_annuity_plus_purchase_annuity_for = findViewById(R.id.spnr_bi_annuity_plus_purchase_annuity_for);
        spnr_bi_annuity_plus_immediate_annuity_plan_for = findViewById(R.id.spnr_bi_annuity_plus_immediate_annuity_plan_for);

        edt_bi_annuity_plus_proposer_first_name = findViewById(R.id.edt_bi_annuity_plus_proposer_first_name);
        edt_bi_annuity_plus_proposer_middle_name = findViewById(R.id.edt_bi_annuity_plus_proposer_middle_name);
        edt_bi_annuity_plus_proposer_last_name = findViewById(R.id.edt_bi_annuity_plus_proposer_last_name);

        btn_bi_annuity_plus_proposer_date = findViewById(R.id.btn_bi_annuity_plus_proposer_date);
        edt_bi_annuity_plus_proposer_age = findViewById(R.id.edt_bi_annuity_plus_proposer_age);
        cb_different_from_proposer = findViewById(R.id.cb_different_from_proposer);
        spnr_bi_annuity_plus_proposer_title = findViewById(R.id.spnr_bi_annuity_plus_proposer_title);
        cb_kerladisc = findViewById(R.id.cb_kerladisc);


    }


    public void setSpinner_Value() {

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

        //String[] SourceOfBusinessList = {"New Proposal"};
        String[] SourceOfBusinessList = {"Vesting/Death/ Surrender of existing SBI Life's pension policy",
                "Open Market Option (Any other Life Insurance Company pension policy)", "New Proposal"};
        ArrayAdapter<String> SourceOfBusinessAdapter = new ArrayAdapter<>(
                getApplicationContext(), R.layout.spinner_item2, SourceOfBusinessList);
        SourceOfBusinessAdapter.setDropDownViewResource(R.layout.spinner_item2);
        spnr_bi_annuity_plus_source_of_business.setAdapter(SourceOfBusinessAdapter);
        SourceOfBusinessAdapter.notifyDataSetChanged();

        // Channel Details
        String[] channelDetailList = {"Direct Marketing"};
        ArrayAdapter<String> channelDetailsAdapter = new ArrayAdapter<>(
                getApplicationContext(), R.layout.spinner_item, channelDetailList);
        channelDetailsAdapter.setDropDownViewResource(R.layout.spinner_item1);
        spnr_bi_annuity_plus_channel_details.setAdapter(channelDetailsAdapter);
        channelDetailsAdapter.notifyDataSetChanged();

        // Mode of annuity Payouts
        String[] modeOfAnnuityPayoutsList = {"Select", "Monthly", "Quarterly", "Half Yearly", "Annual"};
        ArrayAdapter<String> modeOfAnnuityPayoutsAdapter = new ArrayAdapter<>(
                getApplicationContext(), R.layout.spinner_item, modeOfAnnuityPayoutsList);
        modeOfAnnuityPayoutsAdapter.setDropDownViewResource(R.layout.spinner_item1);
        spnr_bi_annuity_plus_mode_of_annuity_payouts.setAdapter(modeOfAnnuityPayoutsAdapter);
        modeOfAnnuityPayoutsAdapter.notifyDataSetChanged();

        // Annuity Option
        String[] AnnuityOptionList = {"Select", "Option1", "Option2"};
        ArrayAdapter<String> AnnuityOptionAdapter = new ArrayAdapter<>(
                getApplicationContext(), R.layout.spinner_item2, AnnuityOptionList);

        AnnuityOptionAdapter.setDropDownViewResource(R.layout.spinner_item2);
        spnr_bi_annuity_plus_annuity_option.setAdapter(AnnuityOptionAdapter);
        AnnuityOptionAdapter.notifyDataSetChanged();


        List<String> title_second_annuitant_list = new ArrayList<>();
        title_second_annuitant_list.add("Select Title");
        title_second_annuitant_list.add("Mr.");
        title_second_annuitant_list.add("Ms.");
        title_second_annuitant_list.add("Mrs.");
        commonMethods.fillSpinnerValue(context, spnr_bi_annuity_plus_life_assured_title_second_annuitant, title_second_annuitant_list);

        String[] purchaseAnnuityList = {"Select", "Single Life", "Two Lives"};
        ArrayAdapter<String> purchaseAnnuityAdapter = new ArrayAdapter<>
                (getApplicationContext(), R.layout.spinner_item, purchaseAnnuityList);
        purchaseAnnuityAdapter.setDropDownViewResource(R.layout.spinner_item1);
        spnr_bi_annuity_plus_purchase_annuity_for.setAdapter(purchaseAnnuityAdapter);
        purchaseAnnuityAdapter.notifyDataSetChanged();

        String[] immediateAnnuityPlanList = {"Select", "Self", "Another Individual"};
        ArrayAdapter<String> immediateAnnuityPlanAdapter = new ArrayAdapter<>
                (getApplicationContext(), R.layout.spinner_item, immediateAnnuityPlanList);
        immediateAnnuityPlanAdapter.setDropDownViewResource(R.layout.spinner_item1);
        spnr_bi_annuity_plus_immediate_annuity_plan_for.setAdapter(immediateAnnuityPlanAdapter);
        immediateAnnuityPlanAdapter.notifyDataSetChanged();


    }

    public void validationOfMoile_EmailId() {

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

    public void setSpinnerAndOtherListner() {


        cb_kerladisc.setOnClickListener(v -> {
            if (cb_kerladisc.isChecked()) {
                str_kerla_discount = "Yes";
                commonMethods.clearFocusable(cb_kerladisc);
                commonMethods.setFocusable(spnr_bi_annuity_plus_life_assured_title);
                spnr_bi_annuity_plus_life_assured_title.requestFocus();
            } else {
                str_kerla_discount = "No";
                commonMethods.clearFocusable(cb_kerladisc);
                commonMethods.setFocusable(spnr_bi_annuity_plus_life_assured_title);
                spnr_bi_annuity_plus_life_assured_title.requestFocus();
            }
        });

        cb_different_from_proposer.setOnClickListener(v -> {
            if (cb_different_from_proposer.isChecked()) {
                String str_proposerAge = "";
                str_proposerAge = edt_bi_annuity_plus_proposer_age.getText().toString();
                propsoser_gender = selGender.getSelectedItem().toString();

                if (str_proposerAge != null && !str_proposerAge.equals("")) {
                    if ((!edt_bi_annuity_plus_proposer_first_name.getText().toString().trim().equals(""))
                            && (!edt_bi_annuity_plus_proposer_first_name.getText().toString().trim().equals(""))
                            && !proposer_Title.equals("") && !propsoser_gender.equals("") && val_proposer_dob()) {
                        if ((Integer.parseInt(str_proposerAge)) >= 40 && (Integer.parseInt(str_proposerAge)) <= 80) {
                            Proposer_is_same_as_Annuitant = "Y";
                            proposer_Is_Same_As_Life_Assured = "Y";
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

                            btn_bi_annuity_plus_life_assured_date.setText(getDate(proposer_date_of_birth));
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
                    } else {
                        commonMethods.setFocusable(edt_bi_annuity_plus_proposer_last_name);
                        edt_bi_annuity_plus_proposer_last_name
                                .requestFocus();
                    }
                }

            } else if (!cb_different_from_proposer.isChecked()) {
                Proposer_is_same_as_Annuitant = "N";
                proposer_Is_Same_As_Life_Assured = "N";
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
        });


        //Spinner


        spnr_bi_annuity_plus_proposer_title
                .setOnItemSelectedListener(new OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int position, long id) {

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
                                proposer_Is_Same_As_Life_Assured = "N";
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
                    }
                });

        spnr_bi_annuity_plus_life_assured_title
                .setOnItemSelectedListener(new OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int position, long id) {
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
                    }
                });


        spnr_bi_annuity_plus_life_assured_title_second_annuitant
                .setOnItemSelectedListener(new OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int position, long id) {
                        if (position > 0) {
                            lifeAssured_Title_Second_Annuitant = spnr_bi_annuity_plus_life_assured_title_second_annuitant
                                    .getSelectedItem().toString();
                            commonMethods.clearFocusable(spnr_bi_annuity_plus_life_assured_title_second_annuitant);
                            commonMethods.setFocusable(edt_bi_annuity_plus_life_assured_first_name_second_annuitant);
                            edt_bi_annuity_plus_life_assured_first_name_second_annuitant.requestFocus();
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                    }
                });


        // Modes of annuity payouts
        spnr_bi_annuity_plus_mode_of_annuity_payouts.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int position, long arg3) {
                if (position > 0) {
                    ModeOfAnnuityPayout = spnr_bi_annuity_plus_mode_of_annuity_payouts.getSelectedItem().toString();
                    if (spnr_bi_annuity_plus_mode_of_annuity_payouts.getSelectedItem().toString().equals("Annual") || spnr_bi_annuity_plus_mode_of_annuity_payouts.getSelectedItem().toString().equals("Half Yearly")) {

                        //tr_bi_annuity_plus_advance_annuity_payout.setVisibility(View.VISIBLE);
                    } else {
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
            }
        });

        //Advance annuity payout
        cb_bi_annuity_plus_advance_annuity_payout.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                //	txtAdvAnnPayoutDate.setVisibility(View.VISIBLE);
                tr_bi_annuity_plus_advance_annuity_payout.setVisibility(View.VISIBLE);
            } else {
                //		txtAdvAnnPayoutDate.setVisibility(View.GONE);
                tr_bi_annuity_plus_advance_annuity_payout.setVisibility(View.GONE);
                annuity_plus_advance_annuity_payout_from_which_date = "";
                btn_bi_annuity_plus_advance_annuity_payout_from_which_date.setText("Select Date");
            }
        });


        //Annuity Option
        spnr_bi_annuity_plus_annuity_option.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int position, long arg3) {
                if (position > 0) {
                    AnnuityOption = spnr_bi_annuity_plus_annuity_option.getSelectedItem().toString();
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
            }
        });

        //Source of Business
        spnr_bi_annuity_plus_source_of_business.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                commonMethods.clearFocusable(spnr_bi_annuity_plus_source_of_business);
                commonMethods.setFocusable(spnr_bi_annuity_plus_channel_details);
                spnr_bi_annuity_plus_channel_details
                        .requestFocus();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });


        //Source of Business
        spnr_bi_annuity_plus_channel_details.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {


                commonMethods.clearFocusable(spnr_bi_annuity_plus_channel_details);
                commonMethods.setFocusable(spnr_bi_annuity_plus_mode_of_annuity_payouts);
                spnr_bi_annuity_plus_mode_of_annuity_payouts
                        .requestFocus();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
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
        edt_bi_annuity_plus_life_assured_age.setOnFocusChangeListener((arg0, arg1) -> {


            if (!(edt_bi_annuity_plus_life_assured_age.getText().toString().equals(""))) {

            }

        });


        //Second Annuitant Age
        edt_bi_annuity_plus_life_assured_age_second_annuitant.setOnFocusChangeListener((arg0, arg1) -> {


            if (!(edt_bi_annuity_plus_life_assured_age.getText().toString().equals(""))) {

            }

        });

        btnBack.setOnClickListener(arg0 -> {
            setResult(RESULT_OK);
            finish();
        });


        btnSubmit.setOnClickListener(v -> {

            commonMethods.hideKeyboard(edt_bi_annuity_plus_proposer_first_name, context);
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

            name_of_second_annuitant = lifeAssured_Title_Second_Annuitant + " " + second_Annuitant_First_Name
                    + " " + second_Annuitant_Middle_Name + " " + second_Annuitant_Last_Name;

            mobileNo = edt_annuity_plus_contact_no.getText()
                    .toString();
            emailId = edt_annuity_plus_Email_id.getText()
                    .toString();
            ConfirmEmailId = edt_annuity_plus_ConfirmEmail_id
                    .getText().toString();

            if (valProposerDetail() && valLifeAssuredProposerDetail() && valSecondAnnuitantDetails() && val_proposer_dob()
                    && valSpinner() && val_first_Annuitant() && val_second_annuitant() &&
                    val_advance_annuity_payout() && val_proposal_date() && valBasicDetail() && valInputScreen()) {
                addListenerOnSubmit_New();
            }

        });

    }


    private void Dialog() {

        final Dialog d = new Dialog(this);
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);
        d.getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.BLACK));
        d.setContentView(R.layout.layout_saralpensionnew_bi_grid);

        TextView tv_bi_annuity_plus_proposer_name = d
                .findViewById(R.id.tv_bi_annuity_plus_proposer_name);

        TextView tv_gst_rate = d
                .findViewById(R.id.tv_gst_rate);
        if (cb_kerladisc.isChecked()) {
            tv_gst_rate.setText("1.9 %");
        }


        TextView tv_bi_annuity_plus_first_annuitant_name = d
                .findViewById(R.id.tv_bi_annuity_plus_first_annuitant_name);
        TextView tv_bi_annuity_plus_second_annuitant_name = d
                .findViewById(R.id.tv_bi_annuity_plus_second_annuitant_name);
        TextView
                tv_amount_installment_premium = d
                .findViewById(R.id.
                        tv_amount_installmentpremium);

        TextView
                tv_bi_annuity_plus_annuity_option_opted = d
                .findViewById(R.id.
                        tv_bi_annuity_plus_annuity_option_opted);


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

        final CheckBox checkboxAgentStatement = d.findViewById(R.id.checkboxAgentStatement);
        checkboxAgentStatement.setChecked(false);

        final TextView textviewAGentStatement = d
                .findViewById(R.id.textviewAGentStatement);

        textviewAGentStatement.setText(commonMethods.getAgentDeclaration(context));


        TextView tv_bi_annuity_plus_proposer_age = d.findViewById(R.id.tv_bi_annuity_plus_proposer_age);
        TextView tv_bi_annuity_plus_first_annuitant_age = d
                .findViewById(R.id.tv_bi_annuity_plus_first_annuitant_age);
        TextView tv_bi_annuity_plus_second_annuitant_age = d
                .findViewById(R.id.tv_bi_annuity_plus_second_annuitant_age);
        TextView tv_bi_annuity_plus_source_of_business = d
                .findViewById(R.id.tv_bi_annuity_plus_source_of_business);

        TextView tv_bi_annuity_plus_mode_of_annuity_payout = d
                .findViewById(R.id.tv_bi_annuity_plus_mode_of_annuity_payout);
        TextView tv_bi_annuity_plus_annuity_amount_payable = d
                .findViewById(R.id.tv_bi_annuity_plus_annuity_amount_payable);

        TextView tv_bi_annuity_plus_annuity_amount_start_date = d
                .findViewById(R.id.tv_bi_annuity_plus_annuity_amount_start_date);
        TextView tv_bi_annuity_plus_total_premium_required = d
                .findViewById(R.id.tv_bi_annuity_plus_total_premium_required);

        TextView tv_bi_annuity_plus_total_service_tax = d
                .findViewById(R.id.tv_bi_annuity_plus_total_service_tax);

        TextView tv_bi_annuity_plus_total_premium = d
                .findViewById(R.id.tv_bi_annuity_plus_total_premium);

        TextView tv_annuity_amt_payable = d.findViewById(R.id.tv_annuity_amt_payable);

        GridView gv_userinfo = d
                .findViewById(R.id.gv_annuity_plus_userinfo);

        final EditText edt_Policyholderplace = d
                .findViewById(R.id.edt_Policyholderplace);

        final TextView txt_annuity_plus_proposer_name = d
                .findViewById(R.id.txt_annuity_plus_proposer_name);

        final CheckBox cb_statement = d
                .findViewById(R.id.cb_annuity_plus_statement);

        final TextView txt_proposer_name_need_analysis = d
                .findViewById(R.id.txt_proposer_name_need_analysis);

        /* Need Analysis */

        final CheckBox cb_statement_need_analysis = d
                .findViewById(R.id.cb_statement_need_analysis);
        cb_statement_need_analysis.setChecked(true);
        TableRow tr_need_analysis = d
                .findViewById(R.id.tr_need_analysis);

        if (NeedAnalysisActivity.str_need_analysis != null) {
            if (NeedAnalysisActivity.str_need_analysis.equals("1")) {

                File mypath_old = mStorageUtils.createFileToAppSpecificDir(context,
                        "NA" + ".pdf");
                File mypath_new = mStorageUtils.createFileToAppSpecificDir(context,
                        QuatationNumber + "_NA" + ".pdf");
                if (mypath_old.exists()) {
                    mypath_old.renameTo(mypath_new);
                }
                File mypath_old_Annexure1 = mStorageUtils.createFileToAppSpecificDir(context,
                        "NA_Annexure1.pdf");
                File mypath_old_Annexure2 = mStorageUtils.createFileToAppSpecificDir(context,
                        "NA_Annexure2.pdf");

                File mypath_new_Annexure1 = mStorageUtils.createFileToAppSpecificDir(context,
                        QuatationNumber + "_NA1" + ".pdf");
                File mypath_new_Annexure2 = mStorageUtils.createFileToAppSpecificDir(context,
                        QuatationNumber + "_NA2" + ".pdf");

                if (mypath_old_Annexure1.exists()) {
                    mypath_old_Annexure1.renameTo(mypath_new_Annexure1);
                }
                if (mypath_old_Annexure2.exists()) {
                    mypath_old_Annexure2.renameTo(mypath_new_Annexure2);
                }
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
        imageButtonShubhNiveshProposerPhotograph.setOnClickListener(view -> {

            Check = "Photo";
            commonMethods.windowmessage(context, "_cust1Photo.jpg");
        });

        if (!proposer_sign.equals("")) {
            if (flg_needAnalyis.equals("1")) {
                tr_need_analysis.setVisibility(View.VISIBLE);
            } else {
                cb_statement_need_analysis.setChecked(true);
                tr_need_analysis.setVisibility(View.GONE);
            }
        }

        name_of_person = name_of_proposer;

        tv_proposername.setText(name_of_proposer);

        txt_annuity_plus_proposer_name.setText("I, " + name_of_proposer +
                ", having received the information with respect to the above, have understood the above statement before entering into the contract.");
        txt_proposer_name_need_analysis.setText("I, " + name_of_proposer
                + " have undergone the Need Analysis & after having reviewed the SBI Life Product options, I have opted for 'SBI LIFE-Saral Pension'.");
        tv_proposal_number.setText(QuatationNumber);

        if (place2.equals("")) {
            edt_Policyholderplace.setText(place2);
        } else {
            edt_Policyholderplace.setText(place2);
        }


        if (!date2.equals("")) {
            btn_PolicyholderDate.setText(getDate(date2));
            tv_bi_annuity_plus_annuity_amount_start_date.setText(getDate(date2));
        } else {
            date2 = getDate1(getCurrentDate());
            btn_PolicyholderDate.setText(getCurrentDate());
            tv_bi_annuity_plus_annuity_amount_start_date.setText(getCurrentDate());
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
                .setOnClickListener(view -> {
                    if (cb_statement.isChecked()
                            && cb_statement_need_analysis.isChecked()
                            && checkboxAgentStatement.isChecked()) {
                        latestImage = "agent";
                        commonMethods.windowMessageSign(context, latestImage);
                    } else {
                        commonMethods.dialogWarning(context, "Please Tick on I Agree Clause ", true);
                        commonMethods.setFocusable(cb_statement);
                        cb_statement.requestFocus();
                    }

                });

        Ibtn_signatureofPolicyHolders
                .setOnClickListener(view -> {
                    if (cb_statement.isChecked()
                            && cb_statement_need_analysis.isChecked()
                            && checkboxAgentStatement.isChecked()) {
                        latestImage = "proposer";
                        commonMethods.windowmessageProposersgin(context,
                                NeedAnalysisActivity.URN_NO + "_cust1sign");
                    } else {
                        commonMethods.dialogWarning(context, "Please Tick on I Agree Clause ", true);
                        commonMethods.setFocusable(cb_statement);
                        cb_statement.requestFocus();
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

        radioButtonTrasactionModeParivartan.setOnCheckedChangeListener((buttonView, isChecked) -> {

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
        });
        /*end*/

        if (mode.equalsIgnoreCase("Manual")) {
            radioButtonTrasactionModeManual.setChecked(true);
        } else if (mode.equalsIgnoreCase("Parivartan")) {
            radioButtonTrasactionModeParivartan.setChecked(true);
        }


        btn_proceed.setOnClickListener(v -> {


            String isActive = "0";

            // if (frmProductHome.equals("FALSE")) {

            name_of_person = txt_annuity_plus_proposer_name.getText().toString();
            place1 = "";
            place2 = edt_Policyholderplace.getText().toString();

            commonMethods.hideKeyboard(edt_annuity_plus_annuity_amount, context);
            // if (frmProductHome.equals("FALSE")) {
            //name_of_person = txt_annuity_plus_proposer_name.getText().toString();
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
                String basicPlusTax = ServiceTax;

                na_cbi_bean = new NA_CBI_bean(QuatationNumber, agentcode, "", userType, "",
                        lifeAssured_Title, lifeAssured_First_Name, lifeAssured_Middle_Name,
                        lifeAssured_Last_Name, planName, sum_assured, basicPlusTax,
                        emailId, mobileNo, agentEmail, agentMobile, na_input, na_output,
                        ModeOfAnnuityPayout, 0, 0, productCode,
                        commonMethods.getDDMMYYYYDate(lifeAssured_date_of_birth),
                        commonMethods.getDDMMYYYYDate(proposer_date_of_birth), inputVal
                        .toString(), retVal.toString());

                name_of_person = name_of_life_assured;

                if (radioButtonTrasactionModeParivartan.isChecked()) {
                    mode = "Parivartan";
                } else if (radioButtonTrasactionModeManual.isChecked()) {
                    mode = "Manual";
                }
                db.AddNeedAnalysisDashboardDetails(new ProductBIBean("", QuatationNumber, planName, getCurrentDate(), mobileNo, getCurrentDate(),
                        db.GetUserCode(), emailId, "", "",
                        agentcode, "", userType, "",
                        lifeAssured_Title, lifeAssured_First_Name, lifeAssured_Middle_Name, lifeAssured_Last_Name,
                        sum_assured, basicPlusTax,
                        agentEmail, agentMobile, na_input, na_output,
                        ModeOfAnnuityPayout, 0, 0, productCode,
                        commonMethods.getDDMMYYYYDate(lifeAssured_date_of_birth),
                        commonMethods.getDDMMYYYYDate(proposer_date_of_birth), "", mode, inputVal
                        .toString(), retVal.toString()));

                createPdf(saralPensionNewBean);


                NABIObj.serviceHit(BI_SaralPensionNewActivity.this, na_cbi_bean, newFile, needAnalysispath.getPath(),
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
                // dialog("Please Fill All The Detail", true);
            }

        });

        btn_PolicyholderDate.setOnClickListener(v -> {


            DIALOG_ID = 2;
            showDateDialog();
        });

        btn_MarketingOfficalDate.setOnClickListener(v -> {

            DIALOG_ID = 3;
            showDateDialog();

        });

        String input = inputVal.toString();
        String output = retVal.toString();

        String staffdiscount = prsObj.parseXmlTag(input, "isStaff");

        if (staffdiscount.equalsIgnoreCase("true")) {
            tv_bi_is_Staff.setText("Yes");
        } else {
            tv_bi_is_Staff.setText("No");
        }

        String nameof_secondannuitant = prsObj.parseXmlTag(input, "NameOfSecondAnnuitant");
        String titleSecondAnnuitant = prsObj.parseXmlTag(input, "TitleSecondAnnuitant");
        String isadvance_annuityPayout = prsObj.parseXmlTag(input, "isAdvanceAnnuityPayout");

        annuity_option = prsObj.parseXmlTag(input, "AnnuityOption");

        proposer_age = prsObj.parseXmlTag(input, "age");
        tv_bi_annuity_plus_proposer_age.setText(proposer_age);


        first_annuitant_age = prsObj.parseXmlTag(input, "AgeFirstAnnuitant");
        tv_bi_annuity_plus_first_annuitant_age.setText(first_annuitant_age);

        second_annuitant_age = prsObj.parseXmlTag(input, "AgeSecondAnnuitant");

        if (annuity_option.equalsIgnoreCase("Option2")) {
            tv_bi_annuity_plus_second_annuitant_age.setText(second_annuitant_age);
        } else {
            tv_bi_annuity_plus_second_annuitant_age.setText("NA");
        }

        source_of_business = prsObj.parseXmlTag(input, "SourceOfBusiness");
        tv_bi_annuity_plus_source_of_business.setText(source_of_business);


        mode_of_annuity_payout = prsObj.parseXmlTag(input, "ModeOfAnnuityPayout");
        tv_bi_annuity_plus_mode_of_annuity_payout.setText(mode_of_annuity_payout);
        annuity_amount = prsObj.parseXmlTag(input, "AnnuityAmount");

        tv_amount_installment_premium.setText(annuity_amount);


        String survivalBenefits_annuity_plus_annuity_amount_payable = obj.getStringWithout_E(Double
                .valueOf(((prsObj.parseXmlTag(result_grid,
                        "SurvivalBenefit" + 1 + "") == null) || (prsObj
                        .parseXmlTag(result_grid,
                                "SurvivalBenefit" + 1 + "")
                        .equals(""))) ? "0" : prsObj
                        .parseXmlTag(result_grid,
                                "SurvivalBenefit" + 1 + "")))
                + "";


        tv_bi_annuity_plus_proposer_name.setText(name_of_proposer);
        tv_bi_annuity_plus_first_annuitant_name.setText(
                name_of_life_assured);


        String str_name_of_second_annuitant = "";
        String str_second_annuitant_age = "";
        if (annuity_option.equalsIgnoreCase("Option2")) {
            str_second_annuitant_age = second_annuitant_age;
            str_name_of_second_annuitant = name_of_second_annuitant;
            tv_bi_annuity_plus_annuity_option_opted.setText("Option2" + "\n" + "(Joint Life Last Survivor Annuity with Return of 100% of  Purchase Price (ROP))");
        } else {
            str_second_annuitant_age = "-";
            str_name_of_second_annuitant = "-";
            tv_bi_annuity_plus_annuity_option_opted.setText("Option1" + "\n" + "(Life Annuity with Return of 100% of Purchase price (ROP))");
        }


        SumAssured = prsObj.parseXmlTag(output, "BasicPrem");
        SumAssuredonDeath = prsObj.parseXmlTag(output, "BasicPrem");
        BasicPrem = prsObj.parseXmlTag(output, "BasicPrem");
        String riderbasicPrem = prsObj.parseXmlTag(output, "RiderbasicPrem");
        TotalInstallmentPrem = prsObj.parseXmlTag(output, "TotalInstallmentPrem");
        FYInstalmentPremium = prsObj.parseXmlTag(output, "FYInstalmentPremium");
        // FYRider = prsObj.parseXmlTag(output, "FYRider");
        FYTotalInstallmentPrem = prsObj.parseXmlTag(output, "FYTotalInstallmentPrem");
        String SYInstalmentPremium = prsObj.parseXmlTag(output, "SYInstalmentPremium");
        // SYRider = prsObj.parseXmlTag(output, "SYRider");
        String SYTotalInstallmentPrem = prsObj.parseXmlTag(output, "SYTotalInstallmentPrem");
        annuity_amt_payable = prsObj.parseXmlTag(output, "AnnuityAmount");
        ServiceTax = prsObj.parseXmlTag(output, "ServiceTax");


        String[] modeOfAnnuityPayoutsList = {"Select", "Monthly", "Quarterly", "Half Yearly", "Annual"};
        if (mode_of_annuity_payout.equalsIgnoreCase("Monthly")) {
            str_header_annuity_amt_payable = "every month";
            payoutMonth = 1;
        } else if (mode_of_annuity_payout.equalsIgnoreCase("Quarterly")) {
            str_header_annuity_amt_payable = "every Quarter";
            payoutMonth = 3;
        } else if (mode_of_annuity_payout.equalsIgnoreCase("Half Yearly")) {
            str_header_annuity_amt_payable = "every half year";
            payoutMonth = 6;
        } else if (mode_of_annuity_payout.equalsIgnoreCase("Annual")) {
            str_header_annuity_amt_payable = "every year";
            payoutMonth = 12;
        }

        tv_bi_annuity_plus_annuity_amount_start_date.setText(getCurrentDate_PayoutDate(payoutMonth));


        tv_annuity_amt_payable.setText(getformatedThousandString(Integer.parseInt(obj.getRound(obj.getStringWithout_E(Double.parseDouble(annuity_amt_payable))))) + " " + str_header_annuity_amt_payable);
        Annuitypayable = prsObj.parseXmlTag(output, "Annuitypayable");
        tv_bi_annuity_plus_annuity_amount_payable.setText(getformatedThousandString(Integer.parseInt(obj.getRound(obj.getStringWithout_E(Double.parseDouble(Annuitypayable))))));


        tv_base_plan_premium_without_tax.setText(getformatedThousandString(Integer.parseInt(commonForAllProd.getRound(obj.getStringWithout_E(Double
                .valueOf(BasicPrem))))));
        tv_total_installment_premium_without_tax.setText(getformatedThousandString(Integer.parseInt(commonForAllProd.getRound(obj.getStringWithout_E(Double
                .valueOf(TotalInstallmentPrem))))));
        tv_base_plan_premium_first_year_with_tax.setText(getformatedThousandString(Integer.parseInt(commonForAllProd.getRound(obj.getStringWithout_E(Double
                .valueOf(FYInstalmentPremium))))));
        tv_total_installment_premium_first_year_with_tax.setText(getformatedThousandString(Integer.parseInt(commonForAllProd.getRound(obj.getStringWithout_E(Double
                .valueOf(FYTotalInstallmentPrem))))));
        tv_base_plan_premium_second_year_with_tax.setText(SYInstalmentPremium);
        tv_total_installment_premium_second_year_with_tax.setText(SYTotalInstallmentPrem);


        tv_bi_annuity_plus_second_annuitant_name.setText(str_name_of_second_annuitant);

        tv_sum_assured_on_death.setText(getformatedThousandString(Integer.parseInt(commonForAllProd.getRound(obj.getStringWithout_E(Double
                .valueOf(SumAssured))))));
        tv_bi_annuity_plus_loan_outstanding_loan_amount.setText(getformatedThousandString(Integer.parseInt(commonForAllProd.getRound(obj.getStringWithout_E(Double
                .valueOf(SumAssuredonDeath))))));
        tv_source_of_funds.setText(source_of_business);


        //createPdf(saralPensionNewBean);


        //list_data.clear();
        for (int i = 1; i <= 11; i++) {
            String end_of_year = String.valueOf(i);
            if (i == 11) {
                end_of_year = "Till Death";
            }


            String yearly_basic_premium = getformatedThousandString(Integer.parseInt(commonForAllProd.getRound(obj.getStringWithout_E(Double
                    .valueOf(((prsObj.parseXmlTag(result_grid,
                            "SingleOrAnnualizedPrem" + i + "") == null) || (prsObj
                            .parseXmlTag(result_grid,
                                    "SingleOrAnnualizedPrem" + i + "")
                            .equals(""))) ? "0" : prsObj
                            .parseXmlTag(result_grid,
                                    "SingleOrAnnualizedPrem" + i + "")))
                    + "")));

            String SurvivalBenefits = getformatedThousandString(Integer.parseInt(commonForAllProd.getRound(obj.getStringWithout_E(Double
                    .valueOf(((prsObj.parseXmlTag(result_grid,
                            "SurvivalBenefit" + i + "") == null) || (prsObj
                            .parseXmlTag(result_grid,
                                    "SurvivalBenefit" + i + "")
                            .equals(""))) ? "0" : prsObj
                            .parseXmlTag(result_grid,
                                    "SurvivalBenefit" + i + "")))
                    + "")));

            String OtherBenefitsifAny = obj.getStringWithout_E(Double
                    .valueOf(((prsObj.parseXmlTag(result_grid,
                            "OtherBenefits" + i + "") == null) || (prsObj
                            .parseXmlTag(result_grid,
                                    "OtherBenefits" + i + "")
                            .equals(""))) ? "0" : prsObj
                            .parseXmlTag(result_grid,
                                    "OtherBenefits" + i + "")))
                    + "";

            String guaranteed_maturity_benefit = prsObj.parseXmlTag(result_grid, "MaturityVestingBenefit" + i + "");

            String guaranteed_death_benefit = prsObj
                    .parseXmlTag(result_grid, "DeathBenefit" + i + "") + "";

            String guaranteed_surrender_value = getformatedThousandString(Integer.parseInt(commonForAllProd.getRound(prsObj.parseXmlTag(result_grid, "MinGuaranteedSurrenderValue" + i + ""))));

            String nonGuaranSurrenderValue = prsObj.parseXmlTag(result_grid,
                    "SpecialSurrenderValue" + i + "");


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


        Adapter_BI_SaralPensionNewGrid adapter = new Adapter_BI_SaralPensionNewGrid(
                this, list_data);
        gv_userinfo.setAdapter(adapter);

        GridHeight gh = new GridHeight();
        gh.getheight(gv_userinfo, "11");

        d.show();

    }


    protected void addListenerOnSubmit() {

        saralPensionNewBean = new SaralPensionNewBean();

        saralPensionNewBean.setIsStaff(cb_staffdisc.isChecked());

        if (cb_kerladisc.isChecked()) {
            saralPensionNewBean.setKerlaDisc(true);
            saralPensionNewBean.setServiceTax(true);

        } else {
            saralPensionNewBean.setKerlaDisc(false);
            saralPensionNewBean.setServiceTax(false);
        }
        saralPensionNewBean.setAnnuityOption(spnr_bi_annuity_plus_annuity_option.getSelectedItem().toString());

        if (spnr_bi_annuity_plus_annuity_option.getSelectedItem().toString().equals("Option2")) {
            saralPensionNewBean.setFirstNameOfSecondAnnuitant(edt_bi_annuity_plus_life_assured_first_name_second_annuitant
                    .getText().toString());
            saralPensionNewBean.setMiddleNameOfSecondAnnuitant(edt_bi_annuity_plus_life_assured_middle_name_second_annuitant
                    .getText().toString());
            saralPensionNewBean.setLastNameOfSecondAnnuitant(edt_bi_annuity_plus_life_assured_last_name_second_annuitant
                    .getText().toString());
            saralPensionNewBean.setAgeOfSecondAnnuitant(Integer.parseInt(edt_bi_annuity_plus_life_assured_age_second_annuitant
                    .getText().toString()));
            saralPensionNewBean.setDobOfSecondAnnuitant(second_Annuitant_date_of_birth);
            saralPensionNewBean.setGenderOfSecondAnnuitant(gender_2);
        }
        saralPensionNewBean.setAnnuityAmount(Double.parseDouble(edt_annuity_plus_annuity_amount.getText().toString()));

        saralPensionNewBean.setAgeOfFirstAnnuitant(Integer.parseInt(edt_bi_annuity_plus_life_assured_age
                .getText().toString()));
        saralPensionNewBean.setGenderOfFirstAnnuitant(gender);

        saralPensionNewBean.setSourceOfBusiness(spnr_bi_annuity_plus_source_of_business.getSelectedItem().toString());

        saralPensionNewBean.setChannelDetails(spnr_bi_annuity_plus_channel_details.getSelectedItem().toString());

        saralPensionNewBean.setModeOfAnnuityPayout(spnr_bi_annuity_plus_mode_of_annuity_payouts.getSelectedItem().toString());

        saralPensionNewBean.setAnnuityOption(spnr_bi_annuity_plus_annuity_option.getSelectedItem().toString());

        try {
            saralPensionNewBean.setProposalDate(new SimpleDateFormat("MM-dd-yyyy", Locale.ENGLISH).parse(proposal_date));
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }

    public double getStaffRebate(boolean IsStaff) {
        Double StaffRebate = 0.0;
        if (IsStaff) {
            StaffRebate = 0.02;
        } else {
            StaffRebate = 0.00;
        }
        return StaffRebate;

    }

    public void getInput(SaralPensionNewBean saralPensionNewBean) {

        Double StaffRebate = getStaffRebate(cb_staffdisc.isChecked());
        String DobOfSecondAnnuitant = saralPensionNewBean.getDobOfSecondAnnuitant();
        String discountPercentage = StaffRebate + "";

        String staffStatus = "";
        if (cb_staffdisc.isChecked()) {
            staffStatus = "sbi";
            // disc_Basic_SelFreq
        } else {
            staffStatus = "none";
        }


        inputVal = new StringBuilder();
        inputVal.append("<?xml version='1.0' encoding='utf-8' ?><saralPensionNext>");

        inputVal.append("<proposer_title>").append(proposer_Title).append("</proposer_title>");
        inputVal.append("<proposer_firstName>").append(proposer_First_Name).append("</proposer_firstName>");
        inputVal.append("<proposer_middleName>").append(proposer_Middle_Name).append("</proposer_middleName>");
        inputVal.append("<proposer_lastName>").append(proposer_Last_Name).append("</proposer_lastName>");
        inputVal.append("<proposer_name>").append(name_of_proposer).append("</proposer_name>");
        inputVal.append("<proposer_DOB>").append(proposer_date_of_birth).append("</proposer_DOB>");
        inputVal.append("<proposer_age>").append(proposer_age).append("</proposer_age>");
        inputVal.append("<propsoser_gender>").append(propsoser_gender).append("</propsoser_gender>");

        inputVal.append("<isStaff>" + saralPensionNewBean.getIsStaff() + "</isStaff>");
        inputVal.append("<str_kerla_discount>" + str_kerla_discount + "</str_kerla_discount>");
        inputVal.append("<Proposer_is_same_as_Annuitant>" + Proposer_is_same_as_Annuitant + "</Proposer_is_same_as_Annuitant>");
        inputVal.append("<age>" + edt_bi_annuity_plus_proposer_age.getText().toString() + "</age>");
        inputVal.append("<propsoser_gender>" + propsoser_gender + "</propsoser_gender>");
        inputVal.append("<AgeFirstAnnuitant>" + edt_bi_annuity_plus_life_assured_age.getText().toString() + "</AgeFirstAnnuitant>");
        inputVal.append("<gender>" + gender + "</gender>");


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

        inputVal.append("<SourceOfBusiness>" + spnr_bi_annuity_plus_source_of_business.getSelectedItem().toString() + "</SourceOfBusiness>");
        inputVal.append("<ChannelDetails>" + spnr_bi_annuity_plus_channel_details.getSelectedItem().toString() + "</ChannelDetails>");
        inputVal.append("<ModeOfAnnuityPayout>" + spnr_bi_annuity_plus_mode_of_annuity_payouts.getSelectedItem().toString() + "</ModeOfAnnuityPayout>");

        inputVal.append("<AnnuityOption>" + spnr_bi_annuity_plus_annuity_option.getSelectedItem().toString() + "</AnnuityOption>");
        inputVal.append("<ProposalDate>" + proposal_date + "</ProposalDate>");

        inputVal.append("<AnnuityAmount>" + edt_annuity_plus_annuity_amount.getText().toString() + "</AnnuityAmount>");


        inputVal.append("<staffStatus>" + staffStatus + "</staffStatus>");
        inputVal.append("<staffRebate>" + discountPercentage + "</staffRebate>");

        inputVal.append("<BIVERSION>" + "7" + "</BIVERSION>");
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

        inputVal.append("<AnnuityAmount>").append(edt_annuity_plus_annuity_amount.getText().toString()).append("</AnnuityAmount>");
        inputVal.append("<VestingAmount>").append(edt_annuity_plus_vesting_amount.getText().toString()).append("</VestingAmount>");
        inputVal.append("<AdditionalAmountIfAny>").append(edt_annuity_plus_additional_amount_if_any.getText().toString()).append("</AdditionalAmountIfAny>");

        inputVal.append("<AnnuityPayoutDate>").append(annuity_plus_advance_annuity_payout_from_which_date).append("</AnnuityPayoutDate>");

        inputVal.append("<product_name>").append(productName).append("</product_name>");
        /*parivartan changes*/
        inputVal.append("<product_Code>").append(product_Code).append("</product_Code>");
        inputVal.append("<product_UIN>").append(product_UIN).append("</product_UIN>");
        inputVal.append("<product_cateogory>").append(product_cateogory).append("</product_cateogory>");
        inputVal.append("<product_type>").append(product_type).append("</product_type>");

        inputVal.append("</saralPensionNext>");

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


    DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {

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
                        commonMethods.showMessageDialog(context, "Please fill Valid Birth Date");
                    } else {
                        //	if (18 <= age) {

                        btn_bi_annuity_plus_proposal_date.setText(date);
                        // edt_bi_annuity_plus_life_assured_age
                        // .setText(final_age);
                        proposal_date = getDate1(date + "");

                    }
                    break;

                case 5:

                    final_age = Integer.toString(age);
                    if (final_age.contains("-")) {
                        commonMethods.showMessageDialog(context, "Please fill Valid Bith Date");
                    } else {

                        if (40 <= age && 80 >= age) {

                            btn_bi_annuity_plus_life_assured_date.setText(date);

                            edt_bi_annuity_plus_life_assured_age.setText(final_age);

                            lifeAssured_date_of_birth = getDate1(date + "");

                            if (cb_bi_annuity_plus_adb_rider.isChecked()) {
                                cb_bi_annuity_plus_adb_rider.setChecked(false);
                                tr_bi_annuity_plus_adb_rider.setVisibility(View.GONE);
                            }

                        } else {

                            commonMethods.showMessageDialog(context, "Minimum Age should be 40 yrs and Maximum Age should be 80 yrs For First Annuitant");
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
                    final_age = Integer.toString(age);
                    if (final_age.contains("-")) {
                        commonMethods.showMessageDialog(context, "Please fill Valid Date");
                    } else {
                        if (40 <= age && 80 >= age) {

                            btn_bi_annuity_plus_life_assured_date_second_annuitant.setText(date);
                            edt_bi_annuity_plus_life_assured_age_second_annuitant.setText(final_age);
                            second_Annuitant_date_of_birth = getDate1(date + "");
                            commonMethods.clearFocusable(btn_bi_annuity_plus_life_assured_date_second_annuitant);
                            commonMethods.setFocusable(btn_bi_annuity_plus_advance_annuity_payout_from_which_date);
                            btn_bi_annuity_plus_advance_annuity_payout_from_which_date.requestFocus();

                            if (cb_bi_annuity_plus_adb_rider.isChecked()) {
                                cb_bi_annuity_plus_adb_rider.setChecked(false);
                                tr_bi_annuity_plus_adb_rider.setVisibility(View.GONE);
                            }

                        } else {
                            commonMethods.showMessageDialog(context, "Minimum Age should be 40 yrs and Maximum Age should be 80 yrs For Second Annuitant");
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
                    final_age = Integer.toString(age);

                    btn_bi_annuity_plus_advance_annuity_payout_from_which_date.setText(date);
                    // edt_bi_annuity_plus_life_assured_age
                    // .setText(final_age);
                    annuity_plus_advance_annuity_payout_from_which_date = getDate1(date + "");
                    commonMethods.clearFocusable(btn_bi_annuity_plus_advance_annuity_payout_from_which_date);
                    commonMethods.setFocusable(btn_bi_annuity_plus_advance_annuity_payout_from_which_date);
                    btn_bi_annuity_plus_advance_annuity_payout_from_which_date.requestFocus();

                    break;

                case 9:

                    proposer_age = final_age;
                    final_age = Integer.toString(age);
                    if (final_age.contains("-")) {
                        commonMethods.showMessageDialog(context, "Please fill Valid Birth Date");
                    } else {

                        if (18 <= age && 100 >= age) {

                            btn_bi_annuity_plus_proposer_date.setText(date);

                            edt_bi_annuity_plus_proposer_age.setText(final_age);

                            proposer_date_of_birth = getDate1(date + "");
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
                                proposer_Is_Same_As_Life_Assured = "N";
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

                            commonMethods.showMessageDialog(context, "Minimum Age should be 18 yrs and Maximum Age should be 100 yrs For Proposer");
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


    public String getformatedThousandString(int number) {
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

    public void setDefaultDate(int id) {
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

    public void confirming_email_id(String email_id) {

        if (!(email_id.equals(ProposerEmailId))) {
            edt_annuity_plus_ConfirmEmail_id
                    .setError("Email id does not match");
        } else if ((email_id.equals(ProposerEmailId))) {
        }

    }


    public void mobile_validation(String number) {
        String isYesBank = "";
        if ((number.length() != 10) && !isYesBank.equalsIgnoreCase("TRUE")) {
            edt_annuity_plus_contact_no
                    .setError("Please provide correct 10-digit mobile number");
        } else if ((number.length() == 10)) {
        }
    }

    public void email_id_validation(String email_id) {

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


    public String getCurrentDate() {
        Calendar present_date = Calendar.getInstance();
        int mDay = present_date.get(Calendar.DAY_OF_MONTH);
        int mMonth = present_date.get(Calendar.MONTH) + 1;
        int mYear = present_date.get(Calendar.YEAR);

        return mDay + "-" + mMonth + "-" + mYear;

    }

    public String getCurrentDate_PayoutDate(int Month) {
        Calendar present_date = Calendar.getInstance();
        present_date.add(Calendar.MONTH, Month);
        int mDay = present_date.get(Calendar.DAY_OF_MONTH);
        int mMonth = present_date.get(Calendar.MONTH) + 1;
        int mYear = present_date.get(Calendar.YEAR);

        return mDay + "-" + mMonth + "-" + mYear;

    }

    /**
     * Used To Change date From mm-dd-yyyy to dd-mm-yyyy
     */
    public String getDate(String OldDate) {
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


    public String getDate1(String OldDate) {
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

    public String getDate2(String OldDate) {
        String NewDate = "";
        try {
            DateFormat userDateFormat = new SimpleDateFormat("dd-MM-yyyy");
            DateFormat dateFormatNeeded = new SimpleDateFormat("MM/dd/yyyy");
            Date date = userDateFormat.parse(OldDate);

            NewDate = dateFormatNeeded.format(date);

        } catch (Exception e) {
            // TODO: handle exception
            Log.e("Integrated Service", "Error in Getting Date");
        }

        return NewDate;
    }


    public boolean valProposerDetail() {
        //if (proposer_Is_Same_As_Life_Assured.equalsIgnoreCase("y")) {
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
            return false;
        } else if (proposer_Title.equalsIgnoreCase("Mr.")
                && propsoser_gender.equalsIgnoreCase("Female")) {

            commonMethods.showMessageDialog(context, "Proposer Title and Gender is not Valid");
            commonMethods.setFocusable(spnr_bi_annuity_plus_proposer_title);
            spnr_bi_annuity_plus_proposer_title
                    .requestFocus();

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


    public boolean valLifeAssuredProposerDetail() {
        //if (proposer_Is_Same_As_Life_Assured.equalsIgnoreCase("y"))
        //{
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

    public boolean valSecondAnnuitantDetails() {

        //if (proposer_Is_Same_As_Life_Assured.equalsIgnoreCase("y") && is_Second_Annuitant.equalsIgnoreCase("N")) {
        if (is_Second_Annuitant.equalsIgnoreCase("Y")) {
            if (lifeAssured_Title_Second_Annuitant.equals("")
                    || second_Annuitant_First_Name.equals("")
                    || second_Annuitant_Last_Name.equals("")) {

                commonMethods.showMessageDialog(context, "Please Fill Name Detail For Second Annuitant");
                if (lifeAssured_Title_Second_Annuitant.equals("")) {
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
                return false;
            } else if (lifeAssured_Title_Second_Annuitant.equalsIgnoreCase("Mr.")
                    && gender_2.equalsIgnoreCase("Female")) {

                commonMethods.showMessageDialog(context, "Second Annuitant Title and Gender is not Valid");
                commonMethods.setFocusable(spnr_bi_annuity_plus_life_assured_title_second_annuitant);
                spnr_bi_annuity_plus_life_assured_title_second_annuitant
                        .requestFocus();
                return false;

            } else if (lifeAssured_Title_Second_Annuitant.equalsIgnoreCase("Ms.")
                    && gender_2.equalsIgnoreCase("Male")) {

                commonMethods.showMessageDialog(context, "Second Annuitant Title and Gender is not Valid");
                commonMethods.setFocusable(spnr_bi_annuity_plus_life_assured_title_second_annuitant);
                spnr_bi_annuity_plus_life_assured_title_second_annuitant
                        .requestFocus();

                return false;

            } else if (lifeAssured_Title_Second_Annuitant.equalsIgnoreCase("Mrs.")
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

    public boolean val_proposer_dob() {

        //if (proposer_Is_Same_As_Life_Assured.equalsIgnoreCase("y")) {

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

    public boolean val_first_Annuitant() {

        //if (proposer_Is_Same_As_Life_Assured.equalsIgnoreCase("y")) {

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


    public boolean valSpinner() {

        //if (proposer_Is_Same_As_Life_Assured.equalsIgnoreCase("y")) {

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
            spnr_bi_annuity_plus_annuity_option.requestFocus();

            return false;
        } else {
            return true;
        }
//			}else
//					return true;
    }


    public boolean val_second_annuitant() {
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


    public boolean val_advance_annuity_payout() {
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


    public boolean val_proposal_date() {
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

    public boolean valBasicDetail() {

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


    //Used in Annuity Option Item Listener
    public void addOrRemoveSecAnnuitantFields() {

        if (spnr_bi_annuity_plus_annuity_option.getSelectedItem().toString().equals("Option2")) {
            linearlayoutSecondAnnuitant.setVisibility(View.VISIBLE);
            is_Second_Annuitant = "Y";

        } else {
            linearlayoutSecondAnnuitant.setVisibility(View.GONE);
            btn_bi_annuity_plus_life_assured_date_second_annuitant.setText("Select Date");
            second_Annuitant_date_of_birth = "";
            is_Second_Annuitant = "N";
        }

    }

    public boolean valInputScreen() {

        //validate Annuity Amount
        return valAnnuityAmt();
    }

    public boolean valAnnuityAmt() {
        String error = "";
        if (edt_annuity_plus_annuity_amount.getText().toString().equals("")) {
            error = "Please enter Annuity Amount in Rs.";
        } else if (!edt_annuity_plus_annuity_amount.getText().toString().equals("")) {
            if (spnr_bi_annuity_plus_mode_of_annuity_payouts.getSelectedItem().toString().equals("Annual")
                    && Double.parseDouble(edt_annuity_plus_annuity_amount.getText().toString()) < 12000) {
                error = "Annuity Amount should not be less than Rs.12000";
            } else if (spnr_bi_annuity_plus_mode_of_annuity_payouts.getSelectedItem().toString().equals("Half Yearly")
                    && Double.parseDouble(edt_annuity_plus_annuity_amount.getText().toString()) < 6000) {
                error = "Annuity Amount should not be less than Rs.6000";
            } else if (spnr_bi_annuity_plus_mode_of_annuity_payouts.getSelectedItem().toString().equals("Quarterly")
                    && Double.parseDouble(edt_annuity_plus_annuity_amount.getText().toString()) < 3000) {
                error = "Annuity Amount should not be less than Rs.3000";
            } else if (spnr_bi_annuity_plus_mode_of_annuity_payouts.getSelectedItem().toString().equals("Monthly")
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

    public boolean valAnnuityAmt_AnnuityAmount(String Amount) {
        String error = "";
        if (Amount.equals("")) {
            error = "Please enter Annuity Amount in Rs.";
        } else if (!Amount.equals("")) {
            if (spnr_bi_annuity_plus_mode_of_annuity_payouts.getSelectedItem().toString().equals("Annual")
                    && Double.parseDouble(Amount) < 12000) {
                error = "Annuity Amount should not be less than Rs.12000";
            } else if (spnr_bi_annuity_plus_mode_of_annuity_payouts.getSelectedItem().toString().equals("Half Yearly")
                    && Double.parseDouble(Amount) < 6000) {
                error = "Annuity Amount should not be less than Rs.6000";
            } else if (spnr_bi_annuity_plus_mode_of_annuity_payouts.getSelectedItem().toString().equals("Quarterly")
                    && Double.parseDouble(Amount) < 3000) {
                error = "Annuity Amount should not be less than Rs.3000";
            } else if (spnr_bi_annuity_plus_mode_of_annuity_payouts.getSelectedItem().toString().equals("Monthly")
                    && Double.parseDouble(Amount) < 1000) {
                error = "Annuity Amount should not be less than Rs.1000";
            }
        }

        if (!error.equals("")) {
            commonMethods.showMessageDialog(context, error);
            return false;
        }

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


    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

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
            commonMethods.hideKeyboard(edt_annuity_plus_annuity_amount, context);
        }

        return true;
    }


    public boolean valJointLifeAnnityOptionDob() {
        if ((spnr_bi_annuity_plus_annuity_option.getSelectedItem().toString().equals("Option2"))
                && (((Integer.parseInt(edt_bi_annuity_plus_life_assured_age.getText().toString())) - (Integer.parseInt(edt_bi_annuity_plus_life_assured_age_second_annuitant.getText().toString()))) > 30)) {
            commonMethods.showMessageDialog(context, "Maximum Age difference between First Annitant and Second Annuitant should be 30");
            commonMethods.setFocusable(btn_bi_annuity_plus_life_assured_date);
            btn_bi_annuity_plus_life_assured_date
                    .requestFocus();
            return false;
        } else if ((spnr_bi_annuity_plus_annuity_option.getSelectedItem().toString().equals("Option2"))
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

    public void addListenerOnSubmit_New() {
        AsyncAnnuityPlus service = new AsyncAnnuityPlus();
        service.execute();
    }

    public class AsyncAnnuityPlus extends AsyncTask<String, Void, String> {
        private String annuityAmtPayable, totServiceTax,
                totalPrem;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(context, ProgressDialog.THEME_HOLO_LIGHT);
            String Message = "Loading Please wait...";
            progressDialog.setMessage(Html.fromHtml("<font color='#00a1e3'><b>" + Message + "<b></font>"));
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setCancelable(true);

            progressDialog.setButton("Cancel",
                    (dialog, which) -> {
                    });

            progressDialog.setMax(100);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... param) {
            if (commonMethods.isNetworkConnected(context)) {

                try {
                    SoapObject request = new SoapObject(NAMESPACE_CALC, METHOD_NAME_CALC);

                    request.addProperty("isStaff", String.valueOf(cb_staffdisc.isChecked()));
                    request.addProperty("SouceofBuss", spnr_bi_annuity_plus_source_of_business.getSelectedItem().toString());
                    request.addProperty("ChannelD", "");
                    request.addProperty("payoutMode", spnr_bi_annuity_plus_mode_of_annuity_payouts.getSelectedItem().toString());
                    request.addProperty("annuityOption", spnr_bi_annuity_plus_annuity_option.getSelectedItem().toString());

                    request.addProperty("fAnnuitantAge", Integer.parseInt(edt_bi_annuity_plus_life_assured_age.getText().toString()));

                    if (spnr_bi_annuity_plus_annuity_option.getSelectedItem().toString().equals("Option2")) {
                        request.addProperty("sAnnuitantAge", Integer.parseInt(edt_bi_annuity_plus_life_assured_age_second_annuitant.getText().toString()));
                    } else {
                        request.addProperty("sAnnuitantAge", "");
                    }

                    request.addProperty("fAnnuitantGender", gender);

                    if (spnr_bi_annuity_plus_annuity_option.getSelectedItem().toString().equals("Option2")) {
                        request.addProperty("sAnnuitantGender", gender_2);
                    } else {
                        request.addProperty("sAnnuitantGender", "");
                    }

                    request.addProperty("SumAssured", edt_annuity_plus_annuity_amount.getText().toString());
                    request.addProperty("AdditionalAmt", "0");
                    request.addProperty("IsBackdate", "03/04/2020");
                    request.addProperty("IsMines", "");
                    request.addProperty("KFC", String.valueOf(cb_kerladisc.isChecked()));

                   /* request.addProperty("isStaff", "true");
                    request.addProperty("SouceofBuss", "Vesting/Death/ Surrender of existing SBI Life's pension policy");
                    request.addProperty("ChannelD", "Direct Marketing");
                    request.addProperty("payoutMode", "Monthly");
                    request.addProperty("annuityOption", "Option2");

                    request.addProperty("fAnnuitantAge", "40");
                        request.addProperty("sAnnuitantAge", "56");
                    request.addProperty("fAnnuitantGender","Female");
                        request.addProperty("sAnnuitantGender", "Male");
                    request.addProperty("SumAssured", "1250000");
                    request.addProperty("AdditionalAmt", "0");
                    request.addProperty("IsBackdate", "03/04/2020");
                    request.addProperty("IsMines", "");
                    request.addProperty("KFC", "false");*/
                    Log.e("Request : ", " -- " + request.toString());


                    SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                    //Enable this envelope if service is written in dot net
                    envelope.dotNet = true;
                    envelope.setOutputSoapObject(request);
                    HttpTransportSE androidHttpTransport = new HttpTransportSE(URL_CALC);

                    commonMethods.TLSv12Enable();

                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);

                    androidHttpTransport.call(SOAP_ACTION_CALC, envelope);
                    SoapPrimitive response = (SoapPrimitive) envelope.getResponse();

                    String result = response.toString();
                    System.out.println("result " + result);
                    Log.e("Request Response: ", " -- " + result);

                    if (!result.contains("<errorMessage>")) {
                        ParseXML prsObj = new ParseXML();
                        if (result.contains("saralPensionNext")) {
                            //result.replace("saralPensionNext", "saralPensionNew");

                            annuityAmtPayable = prsObj.parseXmlTag(result,
                                    "AnnuityAmount");
                            totServiceTax = prsObj.parseXmlTag(result,
                                    "ServiceTax");
                            totalPrem = prsObj.parseXmlTag(result, "TotalInstallmentPrem");
                            retVal.append(result);
                            result_grid = result;
                            Log.e("Request Response 2: ", " -- " + result_grid);
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

            super.onPostExecute(result);

            try {
                if (progressDialog.isShowing())
                    progressDialog.dismiss();

            } catch (Exception e) {
                e.getMessage();
            }
            if (result.equals("Success")) {
                if (valAnnuityAmt_AnnuityAmount(annuityAmtPayable)) {
                    if (needAnalysis_flag == 0) {
                        Intent i = new Intent(context, success.class);

                        i.putExtra("op", "Mode of Annuity Payout is "
                                + ModeOfAnnuityPayout);
                        i.putExtra(
                                "op1",
                                "Annuity Amount is Rs."
                                        + currencyFormat.format(Double
                                        .parseDouble(annuityAmtPayable)));

                        i.putExtra("op2", "Total Applicable Taxes is Rs. " + currencyFormat.format(Double.parseDouble(totServiceTax)));
                        i.putExtra("op2", "Total Premium is Rs. " + currencyFormat.format(Double
                                .parseDouble(totalPrem)));
                        i.putExtra("ProductDescName", "SBI Life - Saral Pension");
                        i.putExtra("productUINName", "(UIN:" + getString(R.string.sbi_life_saral_pension_new_uin) + ")");

                        context.startActivity(i);
                    } else {

                        addListenerOnSubmit();
                        getInput(saralPensionNewBean);
                        Dialog();

                    }
                }

            } else {
                Toast.makeText(context, result, Toast.LENGTH_LONG).show();
            }
        }


    }


    public void createPdf(SaralPensionNewBean saralPensionNewBean) {
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

            Calendar present_date = Calendar.getInstance();
            int mDay = present_date.get(Calendar.DAY_OF_MONTH);
            int mMonth = present_date.get(Calendar.MONTH);
            int mYear = present_date.get(Calendar.YEAR);

            String CurrentDate = mDay + "-" + (mMonth + 1) + "-" + mYear;

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
            c1.setHorizontalAlignment(Element.ALIGN_LEFT);
            headertable.addCell(c1);
            headertable.setHorizontalAlignment(Element.ALIGN_LEFT);
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
                    "Benefit Illustration for “SBI Life - Saral Pension(UIN:  111N130V01)”",
                    small_bold);
            para_address4.setAlignment(Element.ALIGN_CENTER);
            Paragraph para_address5 = new Paragraph(
                    "A Single Premium, Individual, Non-Linked, Non-Participating, Immediate Annuity Product",
                    small_bold);
            para_address5.setAlignment(Element.ALIGN_CENTER);
            document.add(para_address);
            document.add(para_address1);
            document.add(para_address2);
            document.add(para_address3);
            document.add(para_address4);
            document.add(para_address5);

           /* document.add(para_img_logo_after_space_1);
            document.add(headertable);*/
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
                    "SBI Life - Saral Pension", small_bold1));
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
                    "Single Premium, Individual, Non linked, Non Participating, Immediate Annuity Product", small_bold1));
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
                    " 111N130V01", small_bold1));
            NameofProposal_cell_16.setHorizontalAlignment(Element.ALIGN_CENTER);

            NameofProposal_cell_16.setVerticalAlignment(Element.ALIGN_CENTER);


            String str_second_annuitant_age = "";
            String str_name_of_second_annuitant = "";
            if (annuity_option.equalsIgnoreCase("Option2")) {
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
                    "Amount of Instalment Premium", small_normal));
            PdfPCell ProposalNumber_cell_30 = new PdfPCell(new Paragraph(
                    getformatedThousandString(Integer.parseInt(obj.getRound(obj.getStringWithout_E(Double.parseDouble(annuity_amount))))), small_bold1));
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
            cell = new PdfPCell(new Phrase("Policy Details", small_bold));
            //cell.setBorder(Rectangle.BOTTOM|Rectangle.LEFT|Rectangle.TOP);
            cell.setColspan(4);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            personalDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase("Policy Option", small_normal));
            cell.setBorder(Rectangle.BOTTOM | Rectangle.LEFT | Rectangle.TOP);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            personalDetail_table.addCell(cell);

            if (annuity_option.equalsIgnoreCase("Option2")) {
                cell = new PdfPCell(new Phrase(annuity_option + "\n" + "(Joint Life Last Survivor Annuity with Return of 100% of  Purchase Price (ROP))", small_normal));
            } else {
                cell = new PdfPCell(new Phrase(annuity_option + "\n" + "(Life Annuity with Return of 100% of Purchase price (ROP))", small_normal));
            }


            cell.setBorder(Rectangle.BOTTOM | Rectangle.LEFT | Rectangle.TOP);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            personalDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase("Sum Assured Rs.", small_normal));
            cell.setBorder(Rectangle.BOTTOM | Rectangle.LEFT | Rectangle.TOP);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            personalDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase(getformatedThousandString(Integer.parseInt(obj.getRound(obj.getStringWithout_E(Double.parseDouble(SumAssured))))), small_normal));
            cell.setBorder(Rectangle.BOTTOM | Rectangle.LEFT | Rectangle.TOP | Rectangle.RIGHT);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            personalDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase("Sum Assured on Death (at inception of the policy) Rs.", small_normal));
            cell.setBorder(Rectangle.BOTTOM | Rectangle.LEFT | Rectangle.TOP);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            personalDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase(getformatedThousandString(Integer.parseInt(obj.getRound(obj.getStringWithout_E(Double.parseDouble(SumAssuredonDeath))))), small_normal));
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


            //6 row

            cell = new PdfPCell(new Phrase(" Annuity Payable Amount", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.BOTTOM | Rectangle.LEFT | Rectangle.TOP);
            personalDetail_table.addCell(cell);


            cell = new PdfPCell(new Phrase(getformatedThousandString(Integer.parseInt(obj.getRound(obj.getStringWithout_E(Double.parseDouble(annuity_amt_payable))))) + " " + str_header_annuity_amt_payable, small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.BOTTOM | Rectangle.TOP);
            personalDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase("Annuity Payout Start Date", small_normal));
            cell.setBorder(Rectangle.BOTTOM | Rectangle.LEFT | Rectangle.TOP);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            personalDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase(getCurrentDate_PayoutDate(payoutMonth), small_normal));
            cell.setBorder(Rectangle.BOTTOM | Rectangle.LEFT | Rectangle.TOP);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            personalDetail_table.addCell(cell);


            cell = new PdfPCell(new Phrase("", small_normal));
            cell.setBorder(Rectangle.BOTTOM | Rectangle.LEFT | Rectangle.TOP);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            personalDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase("", small_normal));
            cell.setBorder(Rectangle.BOTTOM | Rectangle.LEFT | Rectangle.TOP);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            personalDetail_table.addCell(cell);

            document.add(para_address41);
            document.add(para_img_logo_after_space_1);
            document.add(personalDetail_table);
            document.add(para_img_logo_after_space_1);

            PdfPTable premDetail_table = new PdfPTable(4);
            premDetail_table.setWidths(new float[]{4f, 5f, 4f, 4f});
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

            cell = new PdfPCell(new Phrase("Total Installment premium", small_bold1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            premDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase("Instalment Premium without applicable taxes", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            premDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase(getformatedThousandString(Integer.parseInt(obj.getRound(obj.getStringWithout_E(Double.parseDouble(BasicPrem))))), small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            premDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase("Not Applicable", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            premDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase(getformatedThousandString(Integer.parseInt(obj.getRound(obj.getStringWithout_E(Double.parseDouble(TotalInstallmentPrem))))), small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            premDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase("Instalment Premium with First year applicable taxes", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            premDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase(getformatedThousandString(Integer.parseInt(obj.getRound(obj.getStringWithout_E(Double.parseDouble(FYInstalmentPremium))))), small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            premDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase("Not Applicable", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            premDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase(getformatedThousandString(Integer.parseInt(obj.getRound(obj.getStringWithout_E(Double.parseDouble(FYTotalInstallmentPrem))))), small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            premDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase("Instalment Premium with applicable taxes 2nd Year onwards", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            premDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase("Not Applicable", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            premDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase("Not Applicable", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            premDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase("Not Applicable", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            premDetail_table.addCell(cell);

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

            PdfPTable Table_BI_Header = new PdfPTable(7);
            Table_BI_Header.setWidthPercentage(100);
            PdfPCell cell_EndOfYear = new PdfPCell(new Paragraph("Policy Year",
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
                    "Death Benefit", small_bold2));

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
                    "Min. Guaranteed Surrender Value", small_bold2));

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
            // Table_BI_Header.addCell(cell_YearlyPremiumPaid3);
            Table_BI_Header.addCell(cell_CummulativePremiumPaid);
            Table_BI_Header.addCell(cell_CummulativePremiumPaid2);
            Table_BI_Header.addCell(cell_GuarantedMaturityBenefit);
            Table_BI_Header.addCell(cell_GuarantedDeathBenefit);
            Table_BI_Header.addCell(cell_GuarantedSurrenderValue);
            // Table_BI_Header.addCell(cell_GuarantedSurrenderValue2);
            document.add(Table_BI_Header);

            float[] columnWidthsBI_Header1 = {1f, 1f, 1f, 1f, 1f, 1f, 1f};
            // for (int i = 0; i < list_data.size(); i++) {
            for (int i = 1; i <= 11; i++) {

                PdfPTable Table_BI_Header2 = new PdfPTable(7);

                Table_BI_Header2.setWidthPercentage(100);
                Table_BI_Header2.setWidths(columnWidthsBI_Header1);
                // PdfPCell cell_EndOfYear3 = new PdfPCell(new
                // Paragraph(list_data
                // .get(i).getPolicy_year(), small_bold2));

                String end_of_year = "";
                end_of_year = prsObj.parseXmlTag(result_grid, "PolicyYear" + i);
                if (i == 11) {
                    end_of_year = "Till Death";
                }
                PdfPCell cell_EndOfYear3 = new PdfPCell(new Phrase(
                        end_of_year,
                        small_bold2));
                cell_EndOfYear3.setPadding(5);
                cell_EndOfYear3.setHorizontalAlignment(Element.ALIGN_CENTER);

                PdfPCell cell_AnnPrem = new PdfPCell(new Phrase(
                        currencyFormat.format(Double.parseDouble(prsObj
                                .parseXmlTag(result_grid, "SingleOrAnnualizedPrem" + i))),
                        small_bold2));
                cell_AnnPrem.setPadding(5);
                cell_AnnPrem.setHorizontalAlignment(Element.ALIGN_CENTER);

                PdfPCell cell_cummulativePremiumPaid = new PdfPCell(new Phrase(
                        currencyFormat.format(Double.parseDouble(((prsObj.parseXmlTag(result_grid,
                                "SurvivalBenefit" + i + "") == null) || (prsObj
                                .parseXmlTag(result_grid,
                                        "SurvivalBenefit" + i + "")
                                .equals(""))) ? "0" : prsObj
                                .parseXmlTag(result_grid,
                                        "SurvivalBenefit" + i + ""))), small_bold2));
                cell_cummulativePremiumPaid.setPadding(5);
                cell_cummulativePremiumPaid
                        .setHorizontalAlignment(Element.ALIGN_CENTER);

                PdfPCell cell_cummulativePremiumPaid2 = new PdfPCell(new Phrase(
                        currencyFormat.format(Double.parseDouble(((prsObj.parseXmlTag(result_grid,
                                "OtherBenefits" + i + "") == null) || (prsObj
                                .parseXmlTag(result_grid,
                                        "OtherBenefits" + i + "")
                                .equals(""))) ? "0" : prsObj
                                .parseXmlTag(result_grid,
                                        "OtherBenefits" + i + ""))), small_bold2));
                cell_cummulativePremiumPaid2.setPadding(5);
                cell_cummulativePremiumPaid2
                        .setHorizontalAlignment(Element.ALIGN_CENTER);


                PdfPCell cell_guarantedDeathBenefit = new PdfPCell(new Phrase(
                        prsObj
                                .parseXmlTag(result_grid, "DeathBenefit"
                                        + i + ""), small_bold2));
                cell_guarantedDeathBenefit.setPadding(5);
                cell_guarantedDeathBenefit
                        .setHorizontalAlignment(Element.ALIGN_CENTER);

                PdfPCell cell_BenefitPayableAtMaturity = new PdfPCell(
                        new Phrase(prsObj.parseXmlTag(result_grid, "MaturityVestingBenefit" + i + ""),
                                small_bold2));
                cell_BenefitPayableAtMaturity.setPadding(5);
                cell_BenefitPayableAtMaturity
                        .setHorizontalAlignment(Element.ALIGN_CENTER);

                PdfPCell cell_guarantedSurrenderValue = new PdfPCell(
                        new Phrase(currencyFormat.format(Double.parseDouble(prsObj.parseXmlTag(result_grid, "MinGuaranteedSurrenderValue" + i + ""))),
                                small_bold2));
                cell_guarantedSurrenderValue.setPadding(5);
                cell_guarantedSurrenderValue
                        .setHorizontalAlignment(Element.ALIGN_CENTER);

                PdfPCell cell_guarantedSurrenderValue2 = new PdfPCell(
                        new Phrase(prsObj.parseXmlTag(result_grid,
                                "SpecialSurrenderValue" + i + ""),
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
                //Table_BI_Header2.addCell(cell_guarantedSurrenderValue2);

                document.add(Table_BI_Header2);
            }
            document.add(para_img_logo_after_space_1);

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
                            currencyFormat.format(Double.parseDouble(Annuitypayable)),
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

            cell = new PdfPCell(new Phrase(" 4. The surrender value shall be applicable only after six months from the date of commencement, subject to terms and conditions as stated in the policy document.", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.RIGHT | Rectangle.BOTTOM | Rectangle.LEFT | Rectangle.TOP);
            table2.addCell(cell);

            Paragraph para_address42 = new Paragraph(
                    "You may receive a Welcome Call from our representative to confirm your proposal details like Date of Birth, Nominee Name, Address, Email ID, Premium Amount, Payout frequency, etc. ",
                    small_bold);
            para_address42.setAlignment(Element.ALIGN_LEFT);

            document.add(table2);
            document.add(para_img_logo_after_space_1);
            document.add(para_address42);
            document.add(para_img_logo_after_space_1);


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
            /*document.add(BI_Pdftable26);
            document.add(BI_Pdftable26_cell1);

            PdfPTable BI_Pdftable_eSign = new PdfPTable(1);
            BI_Pdftable_eSign.setWidthPercentage(100);
            // CR_table6.setWidths(columnWidths_26);
            PdfPCell BI_Pdftable_eSign_cell1 = new PdfPCell(new Paragraph(

                    "This document is eSigned by " + name_of_person, small_bold));

            BI_Pdftable_eSign_cell1.setPadding(5);

            BI_Pdftable_eSign.addCell(BI_Pdftable_eSign_cell1);
            document.add(BI_Pdftable_eSign);


            PdfPTable BI_Pdftabe_Customer_signature_signature = new PdfPTable(2);

            BI_Pdftabe_Customer_signature_signature.setWidthPercentage(100);

            M_ChannelDetails list_channel_detail = db.getChannelDetail(sr_Code);


            if (place2.equals("")) {
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
*/

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
                                    + str_agent_name
                                    + "     have explained the premiums and benefits under the product fully to the prospect/policyholder.",
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
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT);

            //	return false;

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        int SIGNATURE_ACTIVITY = 1;
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

}

