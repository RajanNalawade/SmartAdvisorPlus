package sbilife.com.pointofsale_bancaagency.smartpower;

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
import sbilife.com.pointofsale_bancaagency.common.BIPdfMail;
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
public class BI_SmartPowerInsuranceActivity extends AppCompatActivity implements
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

	private String premiumAmount;

	private CheckBox cb_staffdisc;
	private CheckBox cb_bi_smart_power_insurance_JKResident;
	private CheckBox cb_selViewSFIN;

	private EditText edt_bi_smart_power_insurance_life_assured_first_name;
	private EditText edt_bi_smart_power_insurance_life_assured_middle_name;
	private EditText edt_bi_smart_power_insurance_life_assured_last_name;
	private EditText edt_bi_smart_power_insurance_life_assured_age;
	private EditText edt_smart_power_insurance_contact_no;
	private EditText edt_smart_power_insurance_Email_id;
	private EditText edt_smart_power_insurance_ConfirmEmail_id;
	private EditText edt_bi_smart_power_insurance_sum_assured_amount;
	private EditText edt_smart_power_insurance_samf;
	//    private EditText edt_smart_power_insurance_noOfYearsElapsedSinceInception;
	private EditText edt_smart_power_insurance_percent_EquityFund;
	private EditText edt_smart_power_insurance_percent_BalancedFund;
	private EditText edt_smart_power_insurance_percent_BondFund;
	private EditText edt_smart_power_insurance_percent_equityOptFund;
	private EditText edt_smart_power_insurance_percent_growthFund;
	private EditText edt_smart_power_insurance_percent_moneyMktFund;
	private EditText edt_smart_power_insurance_percent_top300Fund, percent_BondOptimiserFund, percent_CorporateBondFund, percent_PureFund;

	private Spinner spnr_bi_smart_power_insurance_life_assured_title;
	private Spinner spnr_bi_smart_power_insurance_selGender;
	private Spinner spnr_bi_smart_power_insurance_policyterm;
	private Spinner spnr_bi_smart_power_insurance_premium_frequency;
	private Spinner spnr_select_fund;
	private Spinner spnr_increasing_cover_option;

	private TextView help_premAmt;
	private TextView help_SAMF;
	private TextView help_noOfYearsElapsedSinceInception;

	private Button btn_bi_smart_power_insurance_life_assured_date;
	private Button btnBack;
	private Button btnSubmit;
	private LinearLayout ll_select_fund;

	private TableRow tbSFINEquityFund;
	private TableRow tbSFINBondFund;
	private TableRow tbSFINTop300Fund;
	private TableRow tbSFINEquityOptFund;
	private TableRow tbSFINGrowthFund;
	private TableRow tbSFINBalancedFund;
	private TableRow tbSFINMoneyMktFund;

	private String QuatationNumber = "";
	private String planName = "";
	private String sr_Code = "";
	List<M_BI_SmartPowerInsuranceAdapterCommon> list_data;
	List<M_BI_SmartPowerInsuranceAdapterCommon2> list_data1;
	List<M_BI_SmartPowerInsuranceAdapterCommon2> list_data2;
    /*newDBHelper db;
    ParseXML prsObj;
    String emailId = "", mobileNo = "", ConfirmEmailId = "",
            ProposerEmailId = "", lifeAssured_Title = "";
    String EMAIL_PATTERN;
    Dialog d;
    Pattern pattern;
    Matcher matcher;*/

	// newDBHelper db;
	private ParseXML prsObj;
	private String emailId = "";
	private String mobileNo = "";
	private String ConfirmEmailId = "";
	private String ProposerEmailId = "";
	private String lifeAssured_Title = "";
	private Dialog d;

	private Bitmap photoBitmap;
	String propsoser_gender = "";
	private  final int DATE_DIALOG_ID = 1;
	private  int DIALOG_ID;
	private int mYear;
	private int mMonth;
	private int mDay;

	private boolean validationFla1 = false;
	private AlertDialog.Builder showAlert;
	private DecimalFormat currencyFormat;
	private SmartPowerInsuranceProperties prop;
	private SmartPowerInsuranceBean smartPowerInsuranceBean;
	private CommonForAllProd commonForAllProd;
	private CommonForAllProd obj;
	private StringBuilder bussIll = null;
	private StringBuilder retVal;
	private StringBuilder inputVal;
	private String output = "";
	private String input = "";
	private String lifeAssured_First_Name = "";
	private String lifeAssured_Middle_Name = "";
	private String lifeAssured_Last_Name = "";
	private String name_of_life_assured = "";
	private String lifeAssured_date_of_birth = "";
	private String proposer_date_of_birth = "";
	private String name_of_proposer = "";
	private final String proposer_Is_Same_As_Life_Assured = "Y";
	private int PF = 0;

	private String name_of_person = "";
	private String place2 = "";
	private String date1 = "";
	private String date2 = "";
	private String agent_sign = "";
	private String proposer_sign = "";
	private Button btn_MarketingOfficalDate;
	private Button btn_PolicyholderDate;
	private ImageButton Ibtn_signatureofMarketing;
	private ImageButton Ibtn_signatureofPolicyHolders;
	private  final int SIGNATURE_ACTIVITY = 1;
	private String latestImage = "";
	private String policyTerm = "";
	private String perInvEquityFund = "";
	private String perInvEquityOptFund = "";
	private String perInvGrowthFund = "";
	private String perInvBalancedFund = "";
	private String perInvBondFund = "";
	private String perInvMoneyMktFund = "";
	private String perInvTop300Fund = "";
	private String noOfYrElapsed = "", perInvBondOptimiserFund = "", perInvCorporateBondFund = "",perInvPureFund = "";
	private String annPrem = "";
	private String redInYieldNoYr = "";
	private String redInYieldMat = "";
	private String policy_Frequency = "";
	private String sumAssured = "";
	private String netYield4pa = "";
	private String netYield8pa = "";
	private String gender = "";
	private String maturityAge = "";
	private String age_entry = "";
	private String increaseCoverOption = "",select_fund = "";
	private String premPayingMode = "";
	private File mypath;
	private String[] outputArr;
	private LinearLayout ll_bi_smart_power_insurance_main;

	private String product_Code, product_UIN, product_cateogory, product_type;
	private String bankUserType = "",mode = "";
	private String sumAssuredAmount = "";
	private String str_kerla_discount = "No";

	private Context context;

	private String Check = "";
	private CommonMethods commonMethods;
	private StorageUtils mStorageUtils;
	private ImageButton imageButtonSmartPowerInsuranceProposerPhotograph;

	private LinearLayout linearlayoutThirdpartySignature,
			linearlayoutAppointeeSignature;
	private ImageButton Ibtn_signatureofThirdParty, Ibtn_signatureofAppointee;
	private String thirdPartySign = "", appointeeSign = "",Company_policy_surrender_dec =  "";

	private CheckBox cb_kerladisc;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.bi_smart_power_insurancemain);
		//getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);

		dbHelper = new DatabaseHelper(getApplicationContext());

		context = this;
		commonMethods = new CommonMethods();
		mStorageUtils = new StorageUtils();
		commonMethods.setActionbarLayout(this);

		NABIObj = new NeedAnalysisBIService(this);
		BIPdfMail objBIPdfMail = new BIPdfMail();
		prsObj = new ParseXML();
		Intent intent = getIntent();

		//Added Tushar Kadam Kerla Applicable Tax 7-Jun-2019
		TableRow tablerowKerlaDiscount = findViewById(R.id.tablerowKerlaDiscount);
		cb_kerladisc =  findViewById(R.id.cb_kerladisc);
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
					planName = "Smart Power Insurance";
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

		Date();
		prsObj = new ParseXML();
		setSpinner_Value();

		list_data = new ArrayList<M_BI_SmartPowerInsuranceAdapterCommon>();
		list_data1 = new ArrayList<M_BI_SmartPowerInsuranceAdapterCommon2>();
		list_data2 = new ArrayList<M_BI_SmartPowerInsuranceAdapterCommon2>();
		// setBIInputGui();

		edt_bi_smart_power_insurance_life_assured_first_name
				.setOnEditorActionListener(this);
		edt_bi_smart_power_insurance_life_assured_middle_name
				.setOnEditorActionListener(this);
		edt_bi_smart_power_insurance_life_assured_last_name
				.setOnEditorActionListener(this);
		edt_bi_smart_power_insurance_sum_assured_amount
				.setOnEditorActionListener(this);
		edt_smart_power_insurance_contact_no.setOnEditorActionListener(this);
		edt_smart_power_insurance_Email_id.setOnEditorActionListener(this);
		edt_smart_power_insurance_ConfirmEmail_id
				.setOnEditorActionListener(this);
		edt_smart_power_insurance_samf.setOnEditorActionListener(this);
//        edt_smart_power_insurance_noOfYearsElapsedSinceInception
//                .setOnEditorActionListener(this);
		edt_smart_power_insurance_percent_EquityFund
				.setOnEditorActionListener(this);
		edt_smart_power_insurance_percent_equityOptFund
				.setOnEditorActionListener(this);
		edt_smart_power_insurance_percent_growthFund
				.setOnEditorActionListener(this);
		edt_smart_power_insurance_percent_BalancedFund
				.setOnEditorActionListener(this);
		edt_smart_power_insurance_percent_BondFund
				.setOnEditorActionListener(this);
		edt_smart_power_insurance_percent_moneyMktFund
				.setOnEditorActionListener(this);
		edt_smart_power_insurance_percent_top300Fund
				.setOnEditorActionListener(this);

		percent_BondOptimiserFund.setOnEditorActionListener(this);
		percent_CorporateBondFund.setOnEditorActionListener(this);
		percent_PureFund.setOnEditorActionListener(this);

		setFocusable(spnr_bi_smart_power_insurance_life_assured_title);
		spnr_bi_smart_power_insurance_life_assured_title.requestFocus();

		validationOfMoile_EmailId();
		setSpinnerAndOtherListner();
		obj = new CommonForAllProd();
		commonForAllProd = new CommonForAllProd();
		prop = new SmartPowerInsuranceProperties();
		smartPowerInsuranceBean = new SmartPowerInsuranceBean();
		showAlert = new AlertDialog.Builder(this);
		currencyFormat = new DecimalFormat("##,##,##,###");
		// getBasicDetail();

		String str_usertype = "";
		TableRow tr_staff_disc;


		tr_staff_disc = findViewById(R.id.tr_smart_power_insurance_staffdisc);
		try {
			str_usertype = SimpleCrypto.decrypt("SBIL",
					dbHelper.GetUserType());

		} catch (Exception e) {
			e.printStackTrace();
		}
       /* if (str_usertype.equalsIgnoreCase("BAP")
				|| str_usertype.equalsIgnoreCase("CAG")) {
			tr_staff_disc.setVisibility(View.GONE);
        }*/
		if (needAnalysis_flag == 1 && !TextUtils.isEmpty(gender)) {
			spnr_bi_smart_power_insurance_selGender.setSelection(genderAdapter
					.getPosition(gender));
			onClickLADob(btn_bi_smart_power_insurance_life_assured_date);
		}
	}

	private void UI_Declaration() {

		cb_staffdisc = findViewById(R.id.cb_staffdisc);
		cb_bi_smart_power_insurance_JKResident = findViewById(R.id.cb_jk);

		edt_bi_smart_power_insurance_life_assured_first_name = findViewById(R.id.edt_bi_smart_power_insurance_life_assured_first_name);
		edt_bi_smart_power_insurance_life_assured_middle_name = findViewById(R.id.edt_bi_smart_power_insurance_life_assured_middle_name);
		edt_bi_smart_power_insurance_life_assured_last_name = findViewById(R.id.edt_bi_smart_power_insurance_life_assured_last_name);
		edt_bi_smart_power_insurance_life_assured_age = findViewById(R.id.edt_bi_smart_power_insurance_life_assured_age);
		edt_smart_power_insurance_contact_no = findViewById(R.id.edt_smart_power_insurance_contact_no);
		edt_smart_power_insurance_Email_id = findViewById(R.id.edt_smart_power_insurance_Email_id);
		edt_smart_power_insurance_ConfirmEmail_id = findViewById(R.id.edt_smart_power_insurance_ConfirmEmail_id);
		edt_bi_smart_power_insurance_sum_assured_amount = findViewById(R.id.edt_bi_smart_power_insurance_sum_assured_amount);

		spnr_bi_smart_power_insurance_life_assured_title = findViewById(R.id.spnr_bi_smart_power_insurance_life_assured_title);
		spnr_bi_smart_power_insurance_selGender = findViewById(R.id.spnr_bi_smart_power_insurance_selGender);
//        spnr_bi_smart_power_insurance_selGender.setClickable(false);
//        spnr_bi_smart_power_insurance_selGender.setEnabled(false);

		spnr_bi_smart_power_insurance_policyterm = findViewById(R.id.spnr_bi_smart_power_insurance_policyterm);
		spnr_bi_smart_power_insurance_premium_frequency = findViewById(R.id.spnr_bi_smart_power_insurance_premium_frequency);
		spnr_select_fund = findViewById(R.id.spnr_select_fund);
		spnr_increasing_cover_option = findViewById(R.id.spnr_increasing_cover_option);

		edt_smart_power_insurance_samf = findViewById(R.id.samf);
//        edt_smart_power_insurance_noOfYearsElapsedSinceInception = findViewById(R.id.years_elapsed_since_inception);
		edt_smart_power_insurance_percent_EquityFund = findViewById(R.id.equityFund);
		edt_smart_power_insurance_percent_equityOptFund = findViewById(R.id.equityOptFund);
		edt_smart_power_insurance_percent_growthFund = findViewById(R.id.growthFund);
		edt_smart_power_insurance_percent_BalancedFund = findViewById(R.id.balancedFund);
		edt_smart_power_insurance_percent_BondFund = findViewById(R.id.bondFund);
		edt_smart_power_insurance_percent_moneyMktFund = findViewById(R.id.moneyMktFund);
		edt_smart_power_insurance_percent_top300Fund = findViewById(R.id.top300Fund);

		percent_BondOptimiserFund = (EditText) findViewById(R.id.bondOptimiserFund);
		percent_CorporateBondFund = (EditText) findViewById(R.id.corporateBondFund);
		percent_PureFund = (EditText) findViewById(R.id.pureFund);

		help_SAMF = findViewById(R.id.help_samf);
		help_premAmt = findViewById(R.id.help_premAmt);
		TextView help_policyTerm = findViewById(R.id.help_policyterm);
		help_noOfYearsElapsedSinceInception = findViewById(R.id.help_years_elapsed_since_inception);

		btn_bi_smart_power_insurance_life_assured_date = findViewById(R.id.btn_bi_smart_power_insurance_life_assured_date);
		btnBack = findViewById(R.id.back);
		btnSubmit = findViewById(R.id.btnSubmit);

		ll_select_fund = findViewById(R.id.ll_select_fund);

		cb_selViewSFIN = findViewById(R.id.selViewSFIN);
		tbSFINEquityFund = findViewById(R.id.tbSFINEquityFund);
		tbSFINBondFund = findViewById(R.id.tbSFINBondFund);
		tbSFINTop300Fund = findViewById(R.id.tbSFINTop300Fund);
		tbSFINEquityOptFund = findViewById(R.id.tbSFINEquityOptFund);
		tbSFINGrowthFund = findViewById(R.id.tbSFINGrowthFund);
		tbSFINBalancedFund = findViewById(R.id.tbSFINBalancedFund);
		tbSFINMoneyMktFund = findViewById(R.id.tbSFINMoneyMktFund);
		ll_bi_smart_power_insurance_main = findViewById(R.id.ll_bi_smart_power_insurance_main);
	}

	private void setSpinner_Value() {

		// Gender
		String[] genderList = {"Male", "Female", "Third Gender"};
		genderAdapter = new ArrayAdapter<>(getApplicationContext(),
				R.layout.spinner_item, genderList);
		genderAdapter.setDropDownViewResource(R.layout.spinner_item1);
		spnr_bi_smart_power_insurance_selGender.setAdapter(genderAdapter);
		genderAdapter.notifyDataSetChanged();

		commonMethods.fillSpinnerValue(context, spnr_bi_smart_power_insurance_life_assured_title,
				Arrays.asList(getResources().getStringArray(R.array.arr_select_title)));

		// String[] policyTermList = new String[21];
		// for (int i = 10; i <= 30; i++) {
		// policyTermList[i - 10] = i + "";
		// }

		// Policy Term
		ArrayList<String> policyTermList = new ArrayList<String>();
		//	policyTermList.add("10");
		for (int i = 10; i <= 30; i++) {
			policyTermList.add(i + "");
		}
		ArrayAdapter<String> policyTermAdapter = new ArrayAdapter<>(
				getApplicationContext(), R.layout.spinner_item, policyTermList);
		policyTermAdapter.setDropDownViewResource(R.layout.spinner_item1);
		spnr_bi_smart_power_insurance_policyterm.setAdapter(policyTermAdapter);
		policyTermAdapter.notifyDataSetChanged();

		// Premium Frequency
		String[] premFreqList = { "Yearly", "Half Yearly", "Quarterly",
				"Monthly" };
		ArrayAdapter<String> premFreqAdapter = new ArrayAdapter<>(
				getApplicationContext(), R.layout.spinner_item, premFreqList);
		premFreqAdapter.setDropDownViewResource(R.layout.spinner_item1);
		spnr_bi_smart_power_insurance_premium_frequency
				.setAdapter(premFreqAdapter);
		premFreqAdapter.notifyDataSetChanged();

		// Select Fund
		String[] selectFundList = { "Trigger Fund", "Smart Funds" };
		ArrayAdapter<String> selectFundAdapter = new ArrayAdapter<>(
				getApplicationContext(), R.layout.spinner_item, selectFundList);
		selectFundAdapter.setDropDownViewResource(R.layout.spinner_item1);
		spnr_select_fund.setAdapter(selectFundAdapter);
		selectFundAdapter.notifyDataSetChanged();

		// Increasing cover option
		String[] IncreasingCoverOptionList = { "Yes", "No" };
		ArrayAdapter<String> IncreasingCoverOptionAdapter = new ArrayAdapter<>(
				getApplicationContext(), R.layout.spinner_item,
				IncreasingCoverOptionList);
		IncreasingCoverOptionAdapter
				.setDropDownViewResource(R.layout.spinner_item1);
		spnr_increasing_cover_option.setAdapter(IncreasingCoverOptionAdapter);
		IncreasingCoverOptionAdapter.notifyDataSetChanged();

	}

	private void setSpinnerAndOtherListner() {

		cb_staffdisc.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (cb_staffdisc.isChecked()) {
					cb_staffdisc.setChecked(true);
				}

				clearFocusable(cb_staffdisc);
				clearFocusable(spnr_bi_smart_power_insurance_life_assured_title);
				setFocusable(spnr_bi_smart_power_insurance_life_assured_title);
				spnr_bi_smart_power_insurance_life_assured_title.requestFocus();
			}
		});

		cb_kerladisc.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (cb_kerladisc.isChecked()) {
					str_kerla_discount = "Yes";
					clearFocusable(cb_kerladisc);
					setFocusable(spnr_bi_smart_power_insurance_life_assured_title);
					spnr_bi_smart_power_insurance_life_assured_title.requestFocus();
				} else {
					str_kerla_discount = "No";
					clearFocusable(cb_kerladisc);
					setFocusable(spnr_bi_smart_power_insurance_life_assured_title);
					spnr_bi_smart_power_insurance_life_assured_title.requestFocus();
				}
			}
		});

		cb_bi_smart_power_insurance_JKResident
				.setOnClickListener(new OnClickListener() {

					public void onClick(View v) {
						// TODO Auto-generated method stub
						if (cb_bi_smart_power_insurance_JKResident.isChecked()) {
							cb_bi_smart_power_insurance_JKResident
									.setChecked(true);
						} else {
							cb_bi_smart_power_insurance_JKResident
									.setChecked(false);
						}
					}
				});

		cb_selViewSFIN.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (cb_selViewSFIN.isChecked()) {
					cb_selViewSFIN.setChecked(true);
					tbSFINEquityFund.setVisibility(View.VISIBLE);
					tbSFINBondFund.setVisibility(View.VISIBLE);
					tbSFINTop300Fund.setVisibility(View.VISIBLE);
					tbSFINEquityOptFund.setVisibility(View.VISIBLE);
					tbSFINGrowthFund.setVisibility(View.VISIBLE);
					tbSFINBalancedFund.setVisibility(View.VISIBLE);
					tbSFINMoneyMktFund.setVisibility(View.VISIBLE);
				} else {
					cb_selViewSFIN.setChecked(false);
					tbSFINEquityFund.setVisibility(View.GONE);
					tbSFINBondFund.setVisibility(View.GONE);
					tbSFINTop300Fund.setVisibility(View.GONE);
					tbSFINEquityOptFund.setVisibility(View.GONE);
					tbSFINGrowthFund.setVisibility(View.GONE);
					tbSFINBalancedFund.setVisibility(View.GONE);
					tbSFINMoneyMktFund.setVisibility(View.GONE);
				}

			}
		});

		// Spinner

		spnr_select_fund
				.setOnItemSelectedListener(new OnItemSelectedListener() {
					public void onItemSelected(AdapterView<?> arg0, View arg1,
											   int pos, long id) {
						if (spnr_select_fund.getSelectedItem().toString()
								.equals("Trigger Fund")) {
							ll_select_fund.setVisibility(View.GONE);
						} else {
							ll_select_fund.setVisibility(View.VISIBLE);
						}

					}

					public void onNothingSelected(AdapterView<?> arg0) {
					}
				});

		spnr_bi_smart_power_insurance_life_assured_title
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					public void onItemSelected(AdapterView<?> arg0, View arg1,
											   int position, long id) {
						// TODO Auto-generated method stub
						if (position == 0){
							lifeAssured_Title = "";
						}else {
							lifeAssured_Title = spnr_bi_smart_power_insurance_life_assured_title
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
							clearFocusable(spnr_bi_smart_power_insurance_life_assured_title);
							setFocusable(edt_bi_smart_power_insurance_life_assured_first_name);
							edt_bi_smart_power_insurance_life_assured_first_name
									.requestFocus();
						}
					}

					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub

					}
				});

		spnr_bi_smart_power_insurance_policyterm
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					public void onItemSelected(AdapterView<?> arg0, View arg1,
											   int position, long id) {

						if (!(edt_bi_smart_power_insurance_life_assured_age
								.getText().toString().equals(""))) {

							if (!valPolicyTermPlusAge()) {
								spnr_bi_smart_power_insurance_policyterm
										.setSelection(0);

							}
						}
						updateSAMFlabel();
						updatenoOfYearsElapsedSinceInception();
						if (edt_bi_smart_power_insurance_life_assured_first_name
								.getText().toString().equals("")) {
							clearFocusable(spnr_bi_smart_power_insurance_policyterm);
							setFocusable(spnr_bi_smart_power_insurance_life_assured_title);
							spnr_bi_smart_power_insurance_life_assured_title
									.requestFocus();
						} else {
							clearFocusable(spnr_bi_smart_power_insurance_policyterm);
							setFocusable(edt_bi_smart_power_insurance_sum_assured_amount);
							edt_bi_smart_power_insurance_sum_assured_amount
									.requestFocus();
						}
					}

					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub

					}
				});

		spnr_bi_smart_power_insurance_premium_frequency
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					public void onItemSelected(AdapterView<?> adapter,
											   View arg1, int position, long id) {
						// TODO Auto-generated method stub

						updatePremiumAmtLabel();

						clearFocusable(spnr_bi_smart_power_insurance_premium_frequency);
						setFocusable(spnr_bi_smart_power_insurance_policyterm);
						spnr_bi_smart_power_insurance_policyterm.requestFocus();

					}

					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub

					}
				});

		spnr_increasing_cover_option
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					public void onItemSelected(AdapterView<?> adapter,
											   View arg1, int position, long id) {
						// TODO Auto-generated method stub

						clearFocusable(spnr_increasing_cover_option);
						setFocusable(spnr_bi_smart_power_insurance_life_assured_title);
						spnr_bi_smart_power_insurance_life_assured_title
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

				gender = spnr_bi_smart_power_insurance_selGender.getSelectedItem().toString();

				inputVal = new StringBuilder();
				retVal = new StringBuilder();

				lifeAssured_First_Name = edt_bi_smart_power_insurance_life_assured_first_name
						.getText().toString();
				lifeAssured_Middle_Name = edt_bi_smart_power_insurance_life_assured_middle_name
						.getText().toString();
				lifeAssured_Last_Name = edt_bi_smart_power_insurance_life_assured_last_name
						.getText().toString();

				name_of_life_assured = lifeAssured_Title + " "
						+ lifeAssured_First_Name + " "
						+ lifeAssured_Middle_Name + " " + lifeAssured_Last_Name;

				mobileNo = edt_smart_power_insurance_contact_no.getText()
						.toString();
				emailId = edt_smart_power_insurance_Email_id.getText()
						.toString();
				ConfirmEmailId = edt_smart_power_insurance_ConfirmEmail_id
						.getText().toString();

				if (valLifeAssuredProposerDetail() && valDob()
						&& valBasicDetail() && valPremiumAmt() && valSAMF()
						&& valTotalAllocation()
						&& valPolicyTermPlusAge()) {

					if (proposer_Is_Same_As_Life_Assured.equalsIgnoreCase("y")) {
						name_of_proposer = "";
						proposer_date_of_birth = "";
					}

					addListenerOnSubmit();
					getInput(smartPowerInsuranceBean);
					// insertDataIntoDatabase();

					double sumAssured1 = Double.parseDouble(outputArr[0]);
					if (valSumAssured(sumAssured1)) {

						if (needAnalysis_flag == 0) {
							Intent i = new Intent(getApplicationContext(),
									Success.class);

							i.putExtra("ProductName",
									"Product : SBI Life - Smart Power Insurance (UIN:111L090V02)");

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
													"fundVal4"))));
							i.putExtra(
									"op2",
									"Fund Value @ 8% is Rs. "
											+ currencyFormat.format(Double.parseDouble(prsObj
											.parseXmlTag(
													retVal.toString(),
													"fundVal8"))));
							i.putExtra("header",
									"SBI Life - Smart Power Insurance");
							i.putExtra("header1", "(UIN:111L090V02)");
							startActivity(i);
						} else
							Dialog();
					}
				}

			}
		});

	}


	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		// TODO Auto-generated method stub
		if (v.getId() == edt_bi_smart_power_insurance_life_assured_first_name
				.getId()) {
			setFocusable(edt_bi_smart_power_insurance_life_assured_middle_name);
			edt_bi_smart_power_insurance_life_assured_middle_name
					.requestFocus();
		} else if (v.getId() == edt_bi_smart_power_insurance_life_assured_middle_name
				.getId()) {
			setFocusable(edt_bi_smart_power_insurance_life_assured_last_name);
			edt_bi_smart_power_insurance_life_assured_last_name.requestFocus();
		} else if (v.getId() == edt_bi_smart_power_insurance_life_assured_last_name
				.getId()) {

			// InputMethodManager imm = (InputMethodManager)
			// getSystemService(INPUT_METHOD_SERVICE);
			// imm.hideSoftInputFromWindow(
			// edt_bi_smart_power_insurance_life_assured_last_name
			// .getWindowToken(), 0);
			// clearFocusable(btn_bi_smart_power_insurance_life_assured_date);
			setFocusable(btn_bi_smart_power_insurance_life_assured_date);
			btn_bi_smart_power_insurance_life_assured_date.requestFocus();
		}

		else if (v.getId() == edt_smart_power_insurance_contact_no.getId()) {
			// clearFocusable(premiumAmt);
			setFocusable(edt_smart_power_insurance_Email_id);
			edt_smart_power_insurance_Email_id.requestFocus();
		}

		else if (v.getId() == edt_smart_power_insurance_Email_id.getId()) {
			// clearFocusable(premiumAmt);
			setFocusable(edt_smart_power_insurance_ConfirmEmail_id);
			edt_smart_power_insurance_ConfirmEmail_id.requestFocus();
		} else if (v.getId() == edt_smart_power_insurance_ConfirmEmail_id
				.getId()) {
			setFocusable(spnr_bi_smart_power_insurance_premium_frequency);
			spnr_bi_smart_power_insurance_premium_frequency.requestFocus();
		} else if (v.getId() == edt_bi_smart_power_insurance_sum_assured_amount
				.getId()) {
			setFocusable(edt_smart_power_insurance_samf);
			edt_smart_power_insurance_samf.requestFocus();

		} else if (v.getId() == edt_smart_power_insurance_samf.getId()) {
           /* setFocusable(edt_smart_power_insurance_noOfYearsElapsedSinceInception);
			edt_smart_power_insurance_noOfYearsElapsedSinceInception
                    .requestFocus()*/;

		} /*else if (v.getId() == edt_smart_power_insurance_noOfYearsElapsedSinceInception
				.getId()) {
			setFocusable(spnr_select_fund);
			spnr_select_fund.requestFocus();

		} else if (v.getId() == edt_smart_power_insurance_noOfYearsElapsedSinceInception
				.getId()) {

			if (spnr_select_fund.getSelectedItem().toString()
					.equalsIgnoreCase("Smart Funds")) {
				setFocusable(edt_smart_power_insurance_percent_EquityFund);
				edt_smart_power_insurance_percent_EquityFund.requestFocus();
			} else {
				setFocusable(spnr_increasing_cover_option);
				spnr_increasing_cover_option.requestFocus();
			}

        }*/ else if (v.getId() == edt_smart_power_insurance_percent_EquityFund
				.getId()) {
			setFocusable(edt_smart_power_insurance_percent_equityOptFund);
			edt_smart_power_insurance_percent_equityOptFund.requestFocus();

		} else if (v.getId() == edt_smart_power_insurance_percent_equityOptFund
				.getId()) {
			setFocusable(edt_smart_power_insurance_percent_growthFund);
			edt_smart_power_insurance_percent_growthFund.requestFocus();

		} else if (v.getId() == edt_smart_power_insurance_percent_growthFund
				.getId()) {
			setFocusable(edt_smart_power_insurance_percent_BalancedFund);
			edt_smart_power_insurance_percent_BalancedFund.requestFocus();

		} else if (v.getId() == edt_smart_power_insurance_percent_BalancedFund
				.getId()) {
			setFocusable(edt_smart_power_insurance_percent_BondFund);
			edt_smart_power_insurance_percent_BondFund.requestFocus();
		} else if (v.getId() == edt_smart_power_insurance_percent_BondFund
				.getId()) {
			setFocusable(edt_smart_power_insurance_percent_moneyMktFund);
			edt_smart_power_insurance_percent_moneyMktFund.requestFocus();
		} else if (v.getId() == edt_smart_power_insurance_percent_moneyMktFund
				.getId()) {
			setFocusable(edt_smart_power_insurance_percent_top300Fund);
			edt_smart_power_insurance_percent_top300Fund.requestFocus();
		} else if (v.getId() == edt_smart_power_insurance_percent_top300Fund
				.getId()) {

			setFocusable(percent_BondOptimiserFund);
			percent_BondOptimiserFund.requestFocus();
		} else if (v.getId() == percent_BondOptimiserFund.getId()) {
			// clearFocusable(percent_BondOptimiserFund);
			setFocusable(percent_CorporateBondFund);
			percent_CorporateBondFund.requestFocus();
		} else if (v.getId() == percent_CorporateBondFund.getId()) {
			// clearFocusable(percent_CorporateBondFund);

			setFocusable(percent_PureFund);
			percent_PureFund.requestFocus();
		} else if (v.getId() == percent_PureFund.getId()) {

			InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(
					percent_PureFund
							.getWindowToken(), 0);
		}
		return true;
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		boolean flagFocus = true;
		if (!flagFocus) {
			ll_bi_smart_power_insurance_main.requestFocus();
		} else {
			spnr_bi_smart_power_insurance_life_assured_title.requestFocus();
		}

	}

	// private void getBasicDetail() {
	// List<M_basicDetail> list_BasicDetail = db
	// .getBasicDetails(QuatationNumber);
	//
	// int i = 0;
	// list_BasicDetail = db.getBasicDetails(QuatationNumber);
	// if (list_BasicDetail.size() > 0) {
	// flagFocus = false;
	//
	// edt_smart_power_insurance_contact_no.setText(list_BasicDetail
	// .get(i).getMobileNo());
	// edt_smart_power_insurance_Email_id.setText(list_BasicDetail.get(i)
	// .getEmailId());
	// edt_smart_power_insurance_ConfirmEmail_id.setText(list_BasicDetail
	// .get(i).getEmailId());
	//
	// photoByteArrayAsString = list_BasicDetail.get(i).getPhoto();
	// if (!photoByteArrayAsString.equals("")
	// && photoByteArrayAsString != null) {
	// byte[] photoByteArray = Base64
	// .decode(photoByteArrayAsString, 0);
	// Bitmap bitmap = BitmapFactory.decodeByteArray(photoByteArray,
	// 0, photoByteArray.length);
	// photoBitmap = bitmap;
	// }
	// flg_personalDetails = list_BasicDetail.get(i).getCreatedDate();
	// }
	//
	// }

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
		}

		else {
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

							btn_bi_smart_power_insurance_life_assured_date
									.setText(date);
							edt_bi_smart_power_insurance_life_assured_age
									.setText(final_age);
							proposer_date_of_birth = getDate1(date + "");
							// ageInYears
							// .setSelection(getIndex(ageInYears, final_age));
							// valAge();
						} else {
							commonMethods.BICommonDialog(context, "Minimum age should be 18 yrs for proposer");
							btn_bi_smart_power_insurance_life_assured_date
									.setText("Select Date");
							edt_bi_smart_power_insurance_life_assured_age
									.setText("");
							proposer_date_of_birth = "";
						}
					}
					break;

				case 5:

					final_age = Integer.toString(age);
					if (final_age.contains("-")) {
						commonMethods.BICommonDialog(context, "Please fill Valid Birth Date");
					} else {
						int maxLimit;
						maxLimit = 45;

						if (18 <= age && age <= maxLimit) {
							String lifeAssuredAge = final_age;
							btn_bi_smart_power_insurance_life_assured_date
									.setText(date);
							edt_bi_smart_power_insurance_life_assured_age
									.setText(final_age);

							edt_bi_smart_power_insurance_life_assured_age
									.setText(final_age);
							lifeAssured_date_of_birth = getDate1(date + "");
							// clearFocusable(btn_bi_smart_power_insurance_life_assured_date);
							clearFocusable(btn_bi_smart_power_insurance_life_assured_date);
							setFocusable(edt_smart_power_insurance_contact_no);
							edt_smart_power_insurance_contact_no.requestFocus();

						} else {
							commonMethods.BICommonDialog(context, "Minimum Age should be 18 yrs and Maximum Age should be "
									+ maxLimit + " yrs For LifeAssured");
							btn_bi_smart_power_insurance_life_assured_date
									.setText("Select Date");
							edt_bi_smart_power_insurance_life_assured_age
									.setText("");
							lifeAssured_date_of_birth = "";

							clearFocusable(btn_bi_smart_power_insurance_life_assured_date);
							setFocusable(btn_bi_smart_power_insurance_life_assured_date);
							btn_bi_smart_power_insurance_life_assured_date
									.requestFocus();
						}
					}
					break;


				default:
					break;

			}

		}
	}

	private void validationOfMoile_EmailId() {

		edt_smart_power_insurance_contact_no
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
						String abc = edt_smart_power_insurance_contact_no
								.getText().toString();
						mobile_validation(abc);

					}
				});

		edt_smart_power_insurance_Email_id
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
						ProposerEmailId = edt_smart_power_insurance_Email_id
								.getText().toString();
						//email_id_validation(ProposerEmailId);

					}
				});

		edt_smart_power_insurance_ConfirmEmail_id
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
						String proposer_confirm_emailId = edt_smart_power_insurance_ConfirmEmail_id
								.getText().toString();
						//confirming_email_id(proposer_confirm_emailId);

					}
				});

	}

	private void addListenerOnSubmit() {
		smartPowerInsuranceBean = new SmartPowerInsuranceBean();

		if (cb_staffdisc.isChecked()) {
			smartPowerInsuranceBean.setIsStaffDiscOrNot(true);
		} else {
			smartPowerInsuranceBean.setIsStaffDiscOrNot(false);
		}
		if(cb_kerladisc.isChecked())
		{
			smartPowerInsuranceBean.setKerlaDisc(true);
			//smartPowerInsuranceBean.setServiceTax(true);
		}
		else
		{
			//smartPowerInsuranceBean.setServiceTax(false);
			smartPowerInsuranceBean.setKerlaDisc(false);
		}
		if (cb_bi_smart_power_insurance_JKResident.isChecked()) {
			smartPowerInsuranceBean.setJKResident(true);
		} else {
			smartPowerInsuranceBean.setJKResident(false);
		}

		smartPowerInsuranceBean.setAge(Integer
				.parseInt(edt_bi_smart_power_insurance_life_assured_age
						.getText().toString()));
		smartPowerInsuranceBean
				.setGender(spnr_bi_smart_power_insurance_selGender
						.getSelectedItem().toString());

		smartPowerInsuranceBean.setPolicy_Term(Integer
				.parseInt(spnr_bi_smart_power_insurance_policyterm
						.getSelectedItem().toString()));

		smartPowerInsuranceBean
				.setPremFreqMode(spnr_bi_smart_power_insurance_premium_frequency
						.getSelectedItem().toString());

		smartPowerInsuranceBean.setPremiumAmt(Double
				.parseDouble(edt_bi_smart_power_insurance_sum_assured_amount
						.getText().toString()));

		smartPowerInsuranceBean.setFundOption(spnr_select_fund
				.getSelectedItem().toString());
		smartPowerInsuranceBean
				.setIncreasingCoverOption(spnr_increasing_cover_option
						.getSelectedItem().toString());

		smartPowerInsuranceBean.setSAMF(Double
				.parseDouble(edt_smart_power_insurance_samf.getText()
						.toString()));
/*
		smartPowerInsuranceBean
				.setNoOfYearsElapsedSinceInception(Integer
						.parseInt(edt_smart_power_insurance_noOfYearsElapsedSinceInception
                                .getText().toString()));*/

		if (!edt_smart_power_insurance_percent_EquityFund.getText().toString()
				.equals(""))
			smartPowerInsuranceBean.setPercentToBeInvested_EquityFund(Double
					.parseDouble(edt_smart_power_insurance_percent_EquityFund
							.getText().toString()));
		else
			smartPowerInsuranceBean.setPercentToBeInvested_EquityFund(0);

		if (!edt_smart_power_insurance_percent_equityOptFund.getText()
				.toString().equals(""))
			smartPowerInsuranceBean
					.setPercentToBeInvested_EquityOptFund(Double
							.parseDouble(edt_smart_power_insurance_percent_equityOptFund
									.getText().toString()));
		else
			smartPowerInsuranceBean.setPercentToBeInvested_EquityOptFund(0);

		if (!edt_smart_power_insurance_percent_growthFund.getText().toString()
				.equals(""))
			smartPowerInsuranceBean.setPercentToBeInvested_GrowthFund(Double
					.parseDouble(edt_smart_power_insurance_percent_growthFund
							.getText().toString()));
		else
			smartPowerInsuranceBean.setPercentToBeInvested_GrowthFund(0);

		if (!edt_smart_power_insurance_percent_BalancedFund.getText()
				.toString().equals(""))
			smartPowerInsuranceBean.setPercentToBeInvested_BalancedFund(Double
					.parseDouble(edt_smart_power_insurance_percent_BalancedFund
							.getText().toString()));
		else
			smartPowerInsuranceBean.setPercentToBeInvested_BalancedFund(0);

		if (!edt_smart_power_insurance_percent_BondFund.getText().toString()
				.equals(""))
			smartPowerInsuranceBean.setPercentToBeInvested_BondFund(Double
					.parseDouble(edt_smart_power_insurance_percent_BondFund
							.getText().toString()));
		else
			smartPowerInsuranceBean.setPercentToBeInvested_BondFund(0);

		if (!edt_smart_power_insurance_percent_moneyMktFund.getText()
				.toString().equals(""))
			smartPowerInsuranceBean
					.setPercentToBeInvested_MoneyMarketFund(Double
							.parseDouble(edt_smart_power_insurance_percent_moneyMktFund
									.getText().toString()));
		else
			smartPowerInsuranceBean.setPercentToBeInvested_MoneyMarketFund(0);

		if (!edt_smart_power_insurance_percent_top300Fund.getText().toString()
				.equals(""))
			smartPowerInsuranceBean.setPercentToBeInvested_Top300Fund(Double
					.parseDouble(edt_smart_power_insurance_percent_top300Fund
							.getText().toString()));
		else
			smartPowerInsuranceBean.setPercentToBeInvested_Top300Fund(0);

		if (!percent_BondOptimiserFund.getText().toString().equals(""))
			smartPowerInsuranceBean.setPercentToBeInvested_BondOptimiserFund(Double
					.parseDouble(percent_BondOptimiserFund.getText().toString()));
		else
			smartPowerInsuranceBean.setPercentToBeInvested_BondOptimiserFund(0);

		if (!percent_CorporateBondFund.getText().toString().equals(""))
			smartPowerInsuranceBean.setPercentToBeInvested_CorpBondFund(Double
					.parseDouble(percent_CorporateBondFund.getText().toString()));
		else
			smartPowerInsuranceBean.setPercentToBeInvested_CorpBondFund(0);


		if (!percent_PureFund.getText().toString().equals(""))
			smartPowerInsuranceBean.setPercentToBeInvested_PureFund(Double
					.parseDouble(percent_PureFund.getText().toString()));
		else
			smartPowerInsuranceBean.setPercentToBeInvested_PureFund(0);

		if (spnr_bi_smart_power_insurance_premium_frequency.getSelectedItem()
				.toString().equals("Yearly")) {
			smartPowerInsuranceBean.setPF(1);
		} else if (spnr_bi_smart_power_insurance_premium_frequency
				.getSelectedItem().toString().equals("Half Yearly")) {
			smartPowerInsuranceBean.setPF(2);
		} else if (spnr_bi_smart_power_insurance_premium_frequency
				.getSelectedItem().toString().equals("Quarterly")) {
			smartPowerInsuranceBean.setPF(4);
		} else if (spnr_bi_smart_power_insurance_premium_frequency
				.getSelectedItem().toString().equals("Monthly")) {
			smartPowerInsuranceBean.setPF(12);
		} else {
			smartPowerInsuranceBean.setPF(1);
		}

		smartPowerInsuranceBean.setEffectivePremium(getEffectivePremium());
		showSmartPowerInsuranceBeanOutputPg(smartPowerInsuranceBean);

	}

	private ArrayList<String[]> showSmartPowerInsuranceBeanOutputPg(
			SmartPowerInsuranceBean smartPowerBean) {
		// For BI

		/***** Added by Akshaya on 3-AUG-15 start */
		String staffStatus;
		if (smartPowerBean.getIsStaffDiscOrNot())
			staffStatus = "sbi";
		else
			staffStatus = "none";

		/***** Added by Akshaya on 3-AUG-15 end */

		retVal = new StringBuilder();

		ArrayList<String[]> arrList = new ArrayList<>();

		outputArr = getOutput("BI_Incl_Mort & Ser Tax", smartPowerBean);
		String[] outputReductionYield = getOutputReductionInYield(
				"Benefit Illustrator_CAP", smartPowerBean);
		arrList.add(outputArr);
		arrList.add(outputReductionYield);
		try {
			retVal.append("<?xml version='1.0' encoding='utf-8' ?><SmartPowerInsurance>");
			retVal.append("<errCode>0</errCode>");
			/***** Added by Akshaya on 3-AUG-15 start */
			retVal.append("<staffStatus>").append(staffStatus).append("</staffStatus>");
			/***** Added by Akshaya on 3-AUG-15 end */

			retVal.append("<maturityAge>").append(smartPowerBean.getAge() + smartPowerBean
					.getPolicy_Term()).append("</maturityAge>");
			retVal.append("<annPrem>").append(smartPowerBean.getEffectivePremium()).append("</annPrem>").append("<redInYieldMat>").append(outputReductionYield[0]).append("</redInYieldMat>").append("<redInYieldNoYr>").append(outputReductionYield[1]).append("</redInYieldNoYr>").append("<netYield4Pr>").append(outputReductionYield[2]).append("</netYield4Pr>").append("<netYield8Pr>").append(outputReductionYield[3]).append("</netYield8Pr>").append("<sumAssured>").append(Double.parseDouble(outputArr[0])).append("</sumAssured>").append("<fundVal4>").append(Double.parseDouble(outputArr[1])).append("</fundVal4>").append("<fundVal8>").append(Double.parseDouble(outputArr[2])).append("</fundVal8>").append("<totFYPrem>").append(Double.parseDouble(outputArr[3])).append("</totFYPrem>");

			int index = smartPowerBean.getPolicy_Term();
			String FundValAtEnd4Pr  = prsObj.parseXmlTag(bussIll.toString(), "FundValAtEnd4Pr" + index + "");
			String FundValAtEnd8Pr = prsObj.parseXmlTag(bussIll.toString(),"FundValAtEnd8Pr" + index + "");

			retVal.append("<FundValAtEnd4Pr"+index+">"+FundValAtEnd4Pr+"</FundValAtEnd4Pr"+index+">");
			retVal.append("<FundValAtEnd8Pr"+index+">"+FundValAtEnd8Pr+"</FundValAtEnd8Pr"+index+">");

			retVal.append(bussIll.toString());
			retVal.append("</SmartPowerInsurance>");

		} catch (Exception e) {
			retVal.append("<?xml version='1.0' encoding='utf-8' ?><SmartPowerInsurance>" + "<errCode>1</errCode>" + "<errorMessage>").append(e.getMessage()).append("</errorMessage></SmartPowerInsurance>");
		}

		/******************************************** xml Output *************************************/

		System.out.println("Final output in xml" + retVal.toString());

		/******************************************** xml Output *************************************/
		// progressDialog.dismiss();

		return arrList;

	}

	private String[] getOutput(String sheetName,
							   SmartPowerInsuranceBean smartPowerBean) {
		bussIll = new StringBuilder();

		// output variable declaration
		int _month_E = 0, _year_F = 0, _age_H = 0;

		// temp variable declaration
		int month_E = 0, year_F = 0, age_H = 0;

		String policyInforce_G = "Y";

		double premium_I = 0, topUpPremium_J = 0, premiumAllocationCharge_K = 0, topUpCharges_L = 0, ServiceTaxOnAllocation_M = 0, amountAvailableForInvestment_N = 0, amountAvailableForInvestment_N1 = 0, sumAssuredRelatedCharges_O = 0, riderCharges_P = 0, policyAdministrationCharges_Q = 0, mortalityCharges_R = 0, accTPDCharges_S = 0, totalCharges_T = 0, serviceTaxExclOfSTOnAllocAndSurr_U = 0, totalServiceTax_V = 0, additionToFundIfAny_W = 0, fundBeforeFMC_X = 0, fundManagementCharge_Y = 0, serviceTaxOnFMC_Z = 0, fundValueAfterFMCBerforeGA_AA = 0, guaranteedAddition_AB = 0, fundValueAtEnd_AC = 0, surrenderCharges_AD = 0, serviceTaxOnSurrenderCharges_AE = 0, surrenderValue_AF = 0, deathBenefit_AG = 0, mortalityCharges_AH = 0, accTPDCharges_AI = 0, totalCharges_AJ = 0, serviceTaxExclOfSTOnAllocAndSurr_AK = 0, totalServiceTax_AL = 0, additionToFundIfAny_AM = 0, fundBeforeFMC_AN = 0, fundManagementCharge_AO = 0, serviceTaxOnFMC_AP = 0, fundValueAfterFMCBerforeGA_AQ = 0, guaranteedAddition_AR = 0, fundValueAtEnd_AS = 0, surrenderCharges_AT = 0, serviceTaxOnSurrenderCharges_AU = 0, surrenderValue_AV = 0, deathBenefit_AW = 0, oneHundredPercentOfCummulativePremium_AY = 0, surrenderCap_AX = 0, commission_BL = 0, totalFYPremium = 0;

		// For BI
		double sum_I = 0, sum_K = 0, sum_N = 0, sum_N1 = 0, sum_Q = 0, sum_R = 0, sum_S = 0, sum_V = 0, sum_Y = 0, sum_AA = 0, sum_AG = 0, sum_AH = 0, sum_AJ = 0, sum_AM = 0, sum_AP = 0, Commission_BL = 0, sum_J = 0, sum_T = 0, sum_AB = 0, sum_AC = 0, sum_AI = 0, sum_AL = 0, sum_AO = 0, sum_AS = 0, sum_FY_Premium = 0, sum_C = 0, sum_E = 0;

		double _sum_Y = 0, _sum_AM = 0, _sum_J = 0, _sum_I = 0, sum_Y1 = 0, _sum_Y1 = 0, _sum_Z = 0, sum_Z = 0;

		double otherCharges_PartB = 0, otherCharges1_PartB = 0, otherCharges_PartA = 0, otherCharges1_PartA = 0, sum_01 = 0, sum_02 = 0;

		// from GUI input
		boolean staffDisc = smartPowerBean.getIsStaffDiscOrNot();
		//double serviceTax=smartPowerBean.getServiceTax();
		double serviceTax=0;
		boolean isKerlaDisc =smartPowerBean.isKerlaDisc();
		int ageAtEntry = smartPowerBean.getAge();
		int policyTerm = smartPowerBean.getPolicy_Term();
		String premFreqMode = smartPowerBean.getPremFreqMode();
		double SAMF = smartPowerBean.getSAMF();
		double effectivePremium = smartPowerBean.getEffectivePremium();
		int PF = smartPowerBean.getPF();
		// System.out.println("getPF "+smartPowerBean.getPF());
		// System.out.println("SAMF "+SAMF);
		double sumAssured = (effectivePremium * SAMF);
		// System.out.println("sumassured "+sumAssured);
		String increasingCoverOption = smartPowerBean
				.getIncreasingCoverOption();
		String addTopUp = "No";
		smartPowerBean.setEffectiveTopUpPrem(addTopUp, prop.topUpPremiumAmt);
		double effectiveTopUpPrem = smartPowerBean.getEffectiveTopUpPrem();

		SmartPowerInsuranceBusinessLogic BIMAST = new SmartPowerInsuranceBusinessLogic();
		String[] forBIArray = BIMAST
				.getForBIArr(prop.mortalityCharges_AsPercentOfofLICtable);
		int rowNumber = 0;
		try {
			// calcultion of fields
			// for(int i=0; i < 1; i++)
			for (int i = 0; i < (policyTerm * 12); i++) {
				rowNumber++;

				BIMAST.setMonth_E(rowNumber);
				month_E = Integer.parseInt(BIMAST.getMonth_E());
				_month_E = month_E;
//				System.out.println("1. month_E " + BIMAST.getMonth_E());

				BIMAST.setYear_F();
				year_F = Integer.parseInt(BIMAST.getYear_F());
				_year_F = year_F;
//				System.out.println("2. year_F " + BIMAST.getYear_F());

				if ((_month_E % 12) == 0) {
					bussIll.append("<policyYr").append(_year_F).append(">").append(_year_F).append("</policyYr").append(_year_F).append(">");

				}
				if(isKerlaDisc == true && _year_F <=2){
					serviceTax =0.19;
				}else{
					serviceTax =0.18;
				}
				policyInforce_G = BIMAST.getPolicyInForce_G();
				// _policyInforce_G=BIMAST.getPolicyInForce_G();
//				System.out.println("3. policyInforce_G "
//						+ BIMAST.getPolicyInForce_G());

				BIMAST.setAge_H(ageAtEntry);
				age_H = Integer.parseInt(BIMAST.getAge_H());
				_age_H = age_H;
				// System.out.println("4. age_H "+BIMAST.getAge_H());

				BIMAST.setPremium_I(policyTerm, PF, effectivePremium);
				premium_I = Double.parseDouble(BIMAST.getPremium_I());
				// _premium_I=premium_I;
//				System.out.println("5. premium_I " + BIMAST.getPremium_I());

				sum_I += premium_I;
				if ((_month_E % 12) == 0) {

					bussIll.append("<AnnPrem").append(_year_F).append(">").append(sum_I).append("</AnnPrem").append(_year_F).append(">");
					//_sum_I = sum_I;
					sum_I = 0;
				}

				BIMAST.setTopUpPremium_J(prop.topUpStatus, effectiveTopUpPrem,
						addTopUp);
				topUpPremium_J = Double.parseDouble(BIMAST.getTopUpPremium_J());
				// _topUpPremium_J=topUpPremium_J;
//				System.out.println("6. Top up premium charges "
//						+ BIMAST.getTopUpPremium_J());

				BIMAST.setPremiumAllocationCharge_K(staffDisc, policyTerm);
				premiumAllocationCharge_K = Double.parseDouble(BIMAST
						.getPremiumAllocationCharge_K());
				// _premiumAllocationCharge_K=premiumAllocationCharge_K;
//				System.out.println("7. premiumAllocationCharge_K "
//						+ premiumAllocationCharge_K);
				sum_K += premiumAllocationCharge_K;

				sum_01 = sum_K;

				if ((_month_E % 12) == 0) {

					bussIll.append("<PremAllCharge"
							+ _year_F
							+ ">"
							+ commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(sum_K))
							+ "</PremAllCharge" + _year_F + ">");
					sum_K = 0;
				}

				BIMAST.setTopUpCharges_L(prop.topUp);
				topUpCharges_L = Double.parseDouble(BIMAST.getTopUpCharges_L());
				// _topUpCharges_L=topUpCharges_L;
//				System.out.println("8. topUpCharges_L " + topUpCharges_L);

				BIMAST.setServiceTaxOnAllocation_M(prop.allocationCharges,
						serviceTax);
				ServiceTaxOnAllocation_M = Double.parseDouble(BIMAST
						.getServiceTaxOnAllocation_M());
				// _ServiceTaxOnAllocation_M=ServiceTaxOnAllocation_M;
//				System.out.println("9. ServiceTaxOnAllocation_M "
//						+ ServiceTaxOnAllocation_M);

				BIMAST.setAmountAvailableForInvestment_N(policyTerm);
				amountAvailableForInvestment_N = Double.parseDouble(BIMAST
						.getAmountAvailableForInvestment_N());
				// _amountAvailableForInvestment_N=amountAvailableForInvestment_N;
				/*System.out.println("_month_E"+_month_E);
				System.out.println("10. amountAvailableForInvestment_N "
						+ amountAvailableForInvestment_N);*/
				sum_N += amountAvailableForInvestment_N;

				BIMAST.setAmountAvailableForInvestment_N1(policyTerm);
				amountAvailableForInvestment_N1 = Double.parseDouble(BIMAST
						.getAmountAvailableForInvestment_N1());
				// _amountAvailableForInvestment_N=amountAvailableForInvestment_N;
//				System.out.println("10. amountAvailableForInvestment_N "
//						+ amountAvailableForInvestment_N);
				sum_N1 += amountAvailableForInvestment_N1;

				if ((_month_E % 12) == 0) {

					bussIll.append("<AmtAviFrInv"
							+ _year_F
							+ ">"
							+ commonForAllProd.getRound(commonForAllProd.getStringWithout_E(sum_N1))
							+ "</AmtAviFrInv" + _year_F + ">");
					sum_N1 = 0;
				}

				BIMAST.setSumAssuredRelatedCharges_O(policyTerm, sumAssured,
						prop.charge_SumAssuredBase);
				sumAssuredRelatedCharges_O = Double.parseDouble(BIMAST
						.getSumAssuredRelatedCharges_O());
				// _sumAssuredRelatedCharges_O=sumAssuredRelatedCharges_O;
//				System.out.println("11. sumAssuredRelatedCharges_O "
//						+ sumAssuredRelatedCharges_O);

//				System.out.println("12. RiderCharges_P " + riderCharges_P);

				if (year_F == 1) {
					sum_C += riderCharges_P;
					sum_E += topUpPremium_J;
					sum_FY_Premium += premium_I;
				}
				BIMAST.setPolicyAdministrationCharge_Q(
						policyAdministrationCharges_Q, prop.charge_Inflation,
						prop.fixedMonthlyExp_RP, prop.inflation_pa_RP);
				policyAdministrationCharges_Q = Double.parseDouble(BIMAST
						.getPolicyAdministrationCharge_Q());
				// _policyAdministrationCharges_Q=policyAdministrationCharges_Q;
//				System.out.println("13. policyAdministrationCharges_Q "
//						+ policyAdministrationCharges_Q);
				sum_Q += policyAdministrationCharges_Q;

				sum_02 = sum_Q;

				if ((_month_E % 12) == 0) {

					bussIll.append("<PolAdminChrg"
							+ _year_F
							+ ">"
							+ commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(sum_Q))
							+ "</PolAdminChrg" + _year_F + ">");
					sum_Q = 0;
				}

				BIMAST.setOneHundredPercentOfCummulativePremium_AY(oneHundredPercentOfCummulativePremium_AY);
				oneHundredPercentOfCummulativePremium_AY = Double
						.parseDouble(BIMAST
								.getOneHundredPercentOfCummulativePremium_AY());
				// _oneHundredPercentOfCummulativePremium_AY=oneHundredPercentOfCummulativePremium_AY;
//				System.out
//						.println("46. oneHundredPercentOfCummulativePremium_AY "
//								+ oneHundredPercentOfCummulativePremium_AY);

				BIMAST.setMortalityCharges_R(ageAtEntry, sumAssured,
						forBIArray, policyTerm, prop.mortalityCharges,
						fundValueAtEnd_AC, increasingCoverOption);
				mortalityCharges_R = Double.parseDouble(BIMAST
						.getMortalityCharges_R());
				// _mortalityCharges_R=mortalityCharges_R;
//				System.out.println("14. mortalityCharges_R "
//						+ mortalityCharges_R);
				sum_R += mortalityCharges_R;
				if ((_month_E % 12) == 0) {
					bussIll.append("<MortChrg4Pr"
							+ _year_F
							+ ">"
//							+ commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(sum_R))
							+ commonForAllProd.getRound(commonForAllProd.getStringWithout_E(sum_R))
							+ "</MortChrg4Pr" + _year_F + ">");
					sum_R = 0;
				}

				BIMAST.setAccTPDCharge_S(policyTerm, prop.accTPD_Charge,
						sumAssured, prop.mortalityCharges, fundValueAtEnd_AC,
						increasingCoverOption);
				accTPDCharges_S = Double
						.parseDouble(BIMAST.getAccTPDCharge_S());
				// _accTPDCharges_S=accTPDCharges_S;
//				System.out.println("15. accTPDCharges_S " + accTPDCharges_S);
				sum_S += accTPDCharges_S;
				if ((_month_E % 12) == 0) {

					bussIll.append("<AccTPDCharg4Pr"
							+ _year_F
							+ ">"
//							+ commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(sum_S))
							+ commonForAllProd.getRound(commonForAllProd.getStringWithout_E(sum_S))
							+ "</AccTPDCharg4Pr" + _year_F + ">");
//					sum_S = 0;
				}

				BIMAST.setTotalCharges_T(policyTerm, riderCharges_P);
				totalCharges_T = Double.parseDouble(BIMAST.getTotalCharges_T());
				// _totalCharges_T=totalCharges_T;
//				System.out.println("16. totalCharges_T " + totalCharges_T);
				sum_T += totalCharges_T;
				if ((_month_E % 12) == 0) {
					bussIll.append("<TotCharg4Pr").append(_year_F).append(">").append(Math.round(sum_T)).append("</TotCharg4Pr").append(_year_F).append(">");
					sum_T = 0;
				}

				BIMAST.setServiceTaxExclOfSTOnAllocAndSurr_U(
						prop.mortalityAndRiderCharges,
						prop.administrationCharges, serviceTax);
				serviceTaxExclOfSTOnAllocAndSurr_U = Double.parseDouble(BIMAST
						.getServiceTaxExclOfSTOnAllocAndSurr_U());
				// _serviceTaxExclOfSTOnAllocAndSurr_U=serviceTaxExclOfSTOnAllocAndSurr_U;
//				System.out.println("17. serviceTaxExclOfSTOnAllocAndSurr_U "
//						+ serviceTaxExclOfSTOnAllocAndSurr_U);

				BIMAST.setAdditionToFundIfAny_W(fundValueAtEnd_AC, policyTerm,
						prop.int1, riderCharges_P);
				additionToFundIfAny_W = Double.parseDouble(BIMAST
						.getAdditionToFundIfAny_W());
				// _additionToFundIfAny_W=additionToFundIfAny_W;
//				System.out.println("19. additionToFundIfAny_W "
//						+ additionToFundIfAny_W);

				sum_Y1 += additionToFundIfAny_W;
				if ((_month_E % 12) == 0) {
					_sum_Y1 = Double.parseDouble(commonForAllProd.getRound(commonForAllProd.getStringWithout_E((sum_Y1))));
					bussIll.append("<AddToTheFund4Pr" + _year_F + ">" + _sum_Y1 + "</AddToTheFund4Pr" + _year_F + ">");
					sum_Y1 = 0;
				}

				BIMAST.setFundBeforeFMC_X(fundValueAtEnd_AC, policyTerm,
						riderCharges_P);
				fundBeforeFMC_X = Double.parseDouble(BIMAST
						.getFundBeforeFMC_X());
				// _fundBeforeFMC_X=fundBeforeFMC_X;
//				System.out.println("20. fundBeforeFMC_X " + fundBeforeFMC_X);

				BIMAST.setFundManagementCharge_Y(policyTerm,
						smartPowerBean.getFundOption(),
						smartPowerBean.getPercentToBeInvested_EquityFund(),
						smartPowerBean.getPercentToBeInvested_BondFund(),
						smartPowerBean.getPercentToBeInvested_Top300Fund(),
						smartPowerBean.getPercentToBeInvested_EquityOptFund(),
						smartPowerBean.getPercentToBeInvested_GrowthFund(),
						smartPowerBean.getPercentToBeInvested_Balanced(),
						smartPowerBean.getPercentToBeInvested_MoneyMarketFund(),
						smartPowerBean.getPercentToBeInvested_BondOptimiserFund(),
						smartPowerBean.getPercentToBeInvested_PureFund(),
						smartPowerBean.getPercentToBeInvested_CorpBondFund());
				fundManagementCharge_Y = Double.parseDouble(BIMAST
						.getFundManagementCharge_Y());
				// _fundManagementCharge_Y=fundManagementCharge_Y;
//				System.out.println("21. fundManagementCharge_Y "
//						+ fundManagementCharge_Y);
				sum_Y += fundManagementCharge_Y;
				if ((_month_E % 12) == 0) {

					bussIll.append("<FundMgmtCharg4Pr"
							+ _year_F
							+ ">"
//							+ commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(sum_Y))
							+ commonForAllProd.getRound(commonForAllProd.getStringWithout_E(sum_Y))
							+ "</FundMgmtCharg4Pr" + _year_F + ">");

				}


				otherCharges_PartA = sum_01 + sum_02 + sum_Y + sum_S;

				if ((_month_E % 12) == 0) {

					bussIll.append("<OtherCharges4Pr_PartA" + _year_F + ">"
							+ commonForAllProd.getRound(String
							.valueOf(otherCharges_PartA)) + "</OtherCharges4Pr_PartA" + _year_F
							+ ">");
					otherCharges_PartA = 0;
					sum_S = 0;
				}

				if ((_month_E % 12) == 0) {

					bussIll.append("<OtherCharges4Pr_PartB" + _year_F + ">"
							+ commonForAllProd.getRound(String
							.valueOf(otherCharges_PartB)) + "</OtherCharges4Pr_PartB" + _year_F
							+ ">");
					otherCharges_PartB = 0;
				}

				BIMAST.setServiceTaxOnFMC_Z(prop.fundManagementCharges,
						serviceTax, smartPowerBean.getFundOption(),
						smartPowerBean.getPercentToBeInvested_EquityFund(),
						smartPowerBean.getPercentToBeInvested_BondFund(),
						smartPowerBean.getPercentToBeInvested_Top300Fund(),
						smartPowerBean.getPercentToBeInvested_EquityOptFund(),
						smartPowerBean.getPercentToBeInvested_GrowthFund(),
						smartPowerBean.getPercentToBeInvested_Balanced(),
						smartPowerBean.getPercentToBeInvested_MoneyMarketFund(),
						smartPowerBean.getPercentToBeInvested_BondOptimiserFund(),
						smartPowerBean.getPercentToBeInvested_PureFund(),
						smartPowerBean.getPercentToBeInvested_CorpBondFund());
				serviceTaxOnFMC_Z = Double.parseDouble(BIMAST
						.getServiceTaxOnFMC_Z());
				// _serviceTaxOnFMC_Z=serviceTaxOnFMC_Z;
//				System.out
//						.println("22. serviceTaxOnFMC_Z " + serviceTaxOnFMC_Z);

				BIMAST.setTotalServiceTax_V(prop.riderCharges, riderCharges_P,serviceTax);
				totalServiceTax_V = Double.parseDouble(BIMAST
						.getTotalServiceTax_V());
				// _totalServiceTax_V=totalServiceTax_V;
//				System.out
//						.println("18. totalServiceTax_V " + totalServiceTax_V);
				sum_V += totalServiceTax_V;
				if ((_month_E % 12) == 0) {
					bussIll.append("<TotServTax4Pr"
							+ _year_F
							+ ">"
//							+ commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(sum_V))
							+ commonForAllProd.getRound(commonForAllProd.getStringWithout_E(sum_V))
							+ "</TotServTax4Pr" + _year_F + ">");
					sum_V = 0;
				}

				BIMAST.setFundValueAfterFMCandBeforeGA_AA(policyTerm);
				fundValueAfterFMCBerforeGA_AA = Double.parseDouble(BIMAST
						.getFundValueAfterFMCandBeforeGA_AA());
				// _fundValueAfterFMCBerforeGA_AA=fundValueAfterFMCBerforeGA_AA;
//				System.out.println("23. fundValueAfterFMCBerforeGA_AA "
//						+ fundValueAfterFMCBerforeGA_AA);
				sum_AA = fundValueAfterFMCBerforeGA_AA;

				BIMAST.setGuaranteedAddition_AB(effectivePremium, policyTerm,
						prop.guaranteedAddition);
				guaranteedAddition_AB = Double.parseDouble(BIMAST
						.getGuaranteedAddition_AB());
				// _guaranteedAddition_AB=guaranteedAddition_AB;
//				System.out.println("24.guaranteedAddition_AB "
//						+ guaranteedAddition_AB);
				sum_AB += guaranteedAddition_AB;

				BIMAST.setFundValueAtEnd_AC();
				fundValueAtEnd_AC = Double.parseDouble(BIMAST
						.getFundValueAtEnd_AC());
				// _fundValueAtEnd_AC=fundValueAtEnd_AC;
//				System.out
//						.println("25. fundValueAtEnd_AC " + fundValueAtEnd_AC);
				sum_AC = sum_AA + sum_AB;
				if ((_month_E % 12) == 0) {

					bussIll.append("<FundValAtEnd4Pr"
							+ _year_F
							+ ">"
							+ commonForAllProd.getRoundUp(commonForAllProd
							.getStringWithout_E(sum_AC))
							+ "</FundValAtEnd4Pr" + _year_F + ">");
					sum_AB = 0;
					sum_AA = 0;
				}

				if ((_month_E % 12) == 0) {
					bussIll.append("<FundBefFMC4Pr"
							+ _year_F
							+ ">"
							+ Math.round(Double.parseDouble(commonForAllProd
							.getRoundUp(commonForAllProd
									.getStringWithout_E(sum_AC)))
							+ Double.parseDouble(commonForAllProd
							.getRoundUp(String.valueOf(sum_Y))))
							+ "</FundBefFMC4Pr" + _year_F + ">");
					sum_Y = 0;
					sum_AC = 0;
				}
				BIMAST.setSurrenderCap_AX(effectivePremium);
				surrenderCap_AX = Double.parseDouble(BIMAST
						.getSurrenderCap_AX());
				// _surrenderCap_AX=surrenderCap_AX;
//				System.out.println("46. surrenderCap_AX " + surrenderCap_AX);

				BIMAST.setSurrenderCharges_AD(effectivePremium, policyTerm);
				surrenderCharges_AD = Double.parseDouble(BIMAST
						.getSurrenderCharges_AD());
				// _surrenderCharges_AD=surrenderCharges_AD;
//				System.out.println("26. surrenderCharges_AD "
//						+ surrenderCharges_AD);

				BIMAST.setServiceTaxOnSurrenderCharges_AE(
						prop.surrenderCharges, serviceTax);
				serviceTaxOnSurrenderCharges_AE = Double.parseDouble(BIMAST
						.getServiceTaxOnSurrenderCharges_AE());
				// _serviceTaxOnSurrenderCharges_AE=serviceTaxOnSurrenderCharges_AE;
//				System.out.println("27.  serviceTaxOnSurrenderCharges_AE "
//						+ serviceTaxOnSurrenderCharges_AE);

				BIMAST.setSurrenderValue_AF();
				surrenderValue_AF = Double.parseDouble(BIMAST
						.getSurrenderValue_AF());
				// _surrenderValue_AF=surrenderValue_AF;
//				System.out
//						.println("28. surrenderValue_AF " + surrenderValue_AF);
				if ((_month_E % 12) == 0) {
					bussIll.append("<SurrVal4Pr"
							+ _year_F
							+ ">"
							/*+ commonForAllProd.getRoundUp(commonForAllProd*/
							+ commonForAllProd.getRound(commonForAllProd
							.getStringWithout_E(surrenderValue_AF))
							+ "</SurrVal4Pr" + _year_F + ">");

				}

				BIMAST.setDeathBenefit_AG(policyTerm, sumAssured,
						increasingCoverOption);
				deathBenefit_AG = Double.parseDouble(BIMAST
						.getDeathBenefit_AG());
				// _deathBenefit_AG=deathBenefit_AG;
//				System.out.println("29. deathBenefit_AG " + deathBenefit_AG);

				if ((_month_E % 12) == 0) {

					bussIll.append("<DeathBen4Pr"
							+ _year_F
							+ ">"
							+ commonForAllProd.getRoundUp(commonForAllProd
							.getStringWithout_E(BIMAST.getDeathBenefit_AB(policyTerm)))
							+ "</DeathBen4Pr" + _year_F + ">");

				}

				// System.out.println("@@@@@@@@@@@@@@@@@@@@@ 8% interest rate @@@@@@@@@@@@@@@@@@@@@ ");

				BIMAST.setMortalityCharges_AH(ageAtEntry, sumAssured,
						forBIArray, policyTerm, prop.mortalityCharges,
						fundValueAtEnd_AS, increasingCoverOption);
				mortalityCharges_AH = Double.parseDouble(BIMAST
						.getMortalityCharges_AH());
				// _mortalityCharges_AH=mortalityCharges_AH;
//				System.out.println("30. mortalityCharges_AH "
//						+ BIMAST.getMortalityCharges_AH());
				sum_AH += mortalityCharges_AH;
				if ((_month_E % 12) == 0) {

					bussIll.append("<MortChrg8Pr"
							+ _year_F
							+ ">"
//							+ commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(sum_AH)) + "</MortChrg8Pr"
							+ commonForAllProd.getRound(commonForAllProd.getStringWithout_E(sum_AH)) + "</MortChrg8Pr"
							+ _year_F + ">");
					sum_AH = 0;
				}

				BIMAST.setAccTPDCharge_AI(policyTerm, prop.accTPD_Charge,
						sumAssured, prop.mortalityCharges, fundValueAtEnd_AS,
						increasingCoverOption);
				accTPDCharges_AI = Double.parseDouble(BIMAST
						.getAccTPDCharge_AI());
				// _accTPDCharges_AI=accTPDCharges_AI;
//				System.out.println("31. accTPDCharges_AI " + accTPDCharges_AI);

				sum_AI += accTPDCharges_AI;
				if ((_month_E % 12) == 0) {

					bussIll.append("<AccTPDCharg8Pr"
							+ _year_F
							+ ">"
//							+ commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(sum_AI)) + "</AccTPDCharg8Pr"
							+ commonForAllProd.getRound(commonForAllProd.getStringWithout_E(sum_AI)) + "</AccTPDCharg8Pr"
							+ _year_F + ">");
//					sum_AI = 0;
				}
				BIMAST.setTotalCharges_AJ(policyTerm, riderCharges_P);
				totalCharges_AJ = Double.parseDouble(BIMAST
						.getTotalCharges_AJ());
				// _totalCharges_AJ=totalCharges_AJ;
//				System.out.println("32. totalCharges_AJ " + totalCharges_AJ);
				sum_AJ += totalCharges_AJ;
				if ((_month_E % 12) == 0) {
					bussIll.append("<TotCharg8Pr" + _year_F + ">"
							+ Math.round(sum_AJ) + "</TotCharg8Pr" + _year_F
							+ ">");
					sum_AJ = 0;
				}

				BIMAST.setServiceTaxExclOfSTOnAllocAndSurr_AK(
						prop.mortalityAndRiderCharges,
						prop.administrationCharges, serviceTax);
				serviceTaxExclOfSTOnAllocAndSurr_AK = Double.parseDouble(BIMAST
						.getServiceTaxExclOfSTOnAllocAndSurr_AK());
				// _serviceTaxExclOfSTOnAllocAndSurr_AK=serviceTaxExclOfSTOnAllocAndSurr_AK;
//				System.out.println("33. serviceTaxExclOfSTOnAllocAndSurr_AK "
//						+ serviceTaxExclOfSTOnAllocAndSurr_AK);

				BIMAST.setAdditionToFundIfAny_AM(fundValueAtEnd_AS, policyTerm,
						prop.int2, riderCharges_P);
				additionToFundIfAny_AM = Double.parseDouble(BIMAST
						.getAdditionToFundIfAny_AM());
				// _additionToFundIfAny_AM=additionToFundIfAny_AM;
//				System.out.println("35. addtitionToFunIfAny "
//						+ additionToFundIfAny_AM);

				sum_Z += additionToFundIfAny_AM;
				if ((_month_E % 12) == 0) {
					_sum_Z = Math.round(sum_Z);
					bussIll.append("<AddToTheFund8Pr" + _year_F + ">" + _sum_Z + "</AddToTheFund8Pr" + _year_F + ">");
					sum_Z = 0;
				}

				BIMAST.setFundBeforeFMC_AN(fundValueAtEnd_AS, policyTerm,
						riderCharges_P);
				fundBeforeFMC_AN = Double.parseDouble(BIMAST
						.getFundBeforeFMC_AN());
				// _fundBeforeFMC_AN=fundBeforeFMC_AN;
//				System.out.println("36. fundBeforeFMC_AN " + fundBeforeFMC_AN);

				BIMAST.setFundManagementCharge_AO(policyTerm,
						smartPowerBean.getFundOption(),
						smartPowerBean.getPercentToBeInvested_EquityFund(),
						smartPowerBean.getPercentToBeInvested_BondFund(),
						smartPowerBean.getPercentToBeInvested_Top300Fund(),
						smartPowerBean.getPercentToBeInvested_EquityOptFund(),
						smartPowerBean.getPercentToBeInvested_GrowthFund(),
						smartPowerBean.getPercentToBeInvested_Balanced(),
						smartPowerBean.getPercentToBeInvested_MoneyMarketFund(),
						smartPowerBean.getPercentToBeInvested_BondOptimiserFund(),
						smartPowerBean.getPercentToBeInvested_PureFund(),
						smartPowerBean.getPercentToBeInvested_CorpBondFund());
				fundManagementCharge_AO = Double.parseDouble(BIMAST
						.getFundManagementCharge_AO());
				// _fundManagementCharge_AO=fundManagementCharge_AO;
//				System.out.println("37. fundManagementCharge_AO "
//						+ fundManagementCharge_AO);

				sum_AO += fundManagementCharge_AO;
				if ((_month_E % 12) == 0) {

					bussIll.append("<FundMgmtCharg8Pr"
							+ _year_F
							+ ">"
							+ commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(sum_AO)) + "</FundMgmtCharg8Pr"
							+ _year_F + ">");

				}


				otherCharges1_PartA = sum_01 + sum_02 + sum_AO + sum_AI;

				if ((_month_E % 12) == 0) {

					bussIll.append("<OtherCharges8Pr_PartA" + _year_F + ">"
							+ commonForAllProd.getRound(String
							.valueOf(otherCharges1_PartA)) + "</OtherCharges8Pr_PartA" + _year_F
							+ ">");
					otherCharges1_PartA = 0;
					sum_AI = 0;
				}

				if ((_month_E % 12) == 0) {

					bussIll.append("<OtherCharges8Pr_PartB" + _year_F + ">"
							+ commonForAllProd.getRound(String
							.valueOf(otherCharges1_PartB)) + "</OtherCharges8Pr_PartB" + _year_F
							+ ">");
					otherCharges1_PartB = 0;
				}

				BIMAST.setServiceTaxOnFMC_AP(prop.fundManagementCharges,
						serviceTax, smartPowerBean.getFundOption(),
						smartPowerBean.getPercentToBeInvested_EquityFund(),
						smartPowerBean.getPercentToBeInvested_BondFund(),
						smartPowerBean.getPercentToBeInvested_Top300Fund(),
						smartPowerBean.getPercentToBeInvested_EquityOptFund(),
						smartPowerBean.getPercentToBeInvested_GrowthFund(),
						smartPowerBean.getPercentToBeInvested_Balanced(),
						smartPowerBean.getPercentToBeInvested_MoneyMarketFund(),
						smartPowerBean.getPercentToBeInvested_BondOptimiserFund(),
						smartPowerBean.getPercentToBeInvested_PureFund(),
						smartPowerBean.getPercentToBeInvested_CorpBondFund());
				serviceTaxOnFMC_AP = Double.parseDouble(BIMAST
						.getServiceTaxOnFMC_AP());
				// _serviceTaxOnFMC_AP=serviceTaxOnFMC_AP;
//				System.out.println("38. serviceTaxOnFMC_AP "
//						+ serviceTaxOnFMC_AP);

				BIMAST.setTotalServiceTax_AL(prop.riderCharges, riderCharges_P,serviceTax);
				totalServiceTax_AL = Double.parseDouble(BIMAST
						.getTotalServiceTax_AL());
				// _totalServiceTax_AL=totalServiceTax_AL;
//				System.out.println("34. totalServiceTax_AL "
//						+ totalServiceTax_AL);

				sum_AL += totalServiceTax_AL;
				if ((_month_E % 12) == 0) {

					bussIll.append("<TotServTxOnCharg8Pr"
							+ _year_F
							+ ">"
							+ commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(sum_AL)) + "</TotServTxOnCharg8Pr"
							+ _year_F + ">");
					sum_AL = 0;
				}

				BIMAST.setFundValueAfterFMCandBeforeGA_AQ(policyTerm);
				fundValueAfterFMCBerforeGA_AQ = Double.parseDouble(BIMAST
						.getFundValueAfterFMCandBeforeGA_AQ());
				// _fundValueAfterFMCBerforeGA_AQ=fundValueAfterFMCBerforeGA_AQ;
//				System.out.println("39. fundValueAfterFMCBerforeGA_AQ "
//						+ fundValueAfterFMCBerforeGA_AQ);

				BIMAST.setGuaranteedAddition_AR(effectivePremium, policyTerm,
						prop.guaranteedAddition);
				guaranteedAddition_AR = Double.parseDouble(BIMAST
						.getGuaranteedAddition_AR());
				// _guaranteedAddition_AR=guaranteedAddition_AR;
//				System.out.println("40. guaranteedAddition_AR "
//						+ guaranteedAddition_AR);

				BIMAST.setFundValueAtEnd_AS();
				fundValueAtEnd_AS = Double.parseDouble(BIMAST
						.getFundValueAtEnd_AS());
				// _fundValueAtEnd_AS=fundValueAtEnd_AS;
//				System.out
//						.println("41. fundValueAtEnd_AS " + fundValueAtEnd_AS);

				if ((_month_E % 12) == 0) {
					bussIll.append("<FundValAtEnd8Pr"
							+ _year_F
							+ ">"
							+ commonForAllProd.getRoundUp(commonForAllProd
							.getStringWithout_E(fundValueAtEnd_AS))
							+ "</FundValAtEnd8Pr" + _year_F + ">");
				}

				if ((_month_E % 12) == 0) {
					// double temp =
					// Math.round(Double.parseDouble(commonForAllProd.getRoundUp(String.valueOf(_fundValueAtEnd_AC)))
					// + _sum_Y);

					bussIll.append("<FundBefFMC8Pr"
							+ _year_F
							+ ">"
							+ Math.round(Double.parseDouble(commonForAllProd.getRoundUp(commonForAllProd
							.getStringWithout_E(fundValueAtEnd_AS)))
							+ Double.parseDouble(commonForAllProd
							.getRoundUp(commonForAllProd.getStringWithout_E(sum_AO))))
							+ "</FundBefFMC8Pr" + _year_F + ">");
					sum_AO = 0;
				}

				BIMAST.setSurrenderCharges_AT(effectivePremium, policyTerm);
				surrenderCharges_AT = Double.parseDouble(BIMAST
						.getSurrenderCharges_AT());
				// _surrenderCharges_AT=surrenderCharges_AT;
//				System.out.println("42. surrenderCharges_AT "
//						+ surrenderCharges_AT);

				BIMAST.setServiceTaxOnSurrenderCharges_AU(
						prop.surrenderCharges, serviceTax);
				serviceTaxOnSurrenderCharges_AU = Double.parseDouble(BIMAST
						.getServiceTaxOnSurrenderCharges_AU());
				// _serviceTaxOnSurrenderCharges_AU=serviceTaxOnSurrenderCharges_AU;
//				System.out.println("43.  serviceTaxOnSurrenderCharges_AU "
//						+ serviceTaxOnSurrenderCharges_AU);

				BIMAST.setSurrenderValue_AV();
				surrenderValue_AV = Double.parseDouble(BIMAST
						.getSurrenderValue_AV());
				// _surrenderValue_AV= surrenderValue_AV;
//				System.out.println("44.  surrenderValue_AV "
//						+ surrenderValue_AV);
				if ((_month_E % 12) == 0) {
					bussIll.append("<SurrVal8Pr"
							+ _year_F
							+ ">"
//							+ commonForAllProd.getRoundUp(commonForAllProd
							+ commonForAllProd.getRound(commonForAllProd
							.getStringWithout_E(surrenderValue_AV))
							+ "</SurrVal8Pr" + _year_F + ">");
				}

				BIMAST.setDeathBenefit_AW(policyTerm, sumAssured,
						increasingCoverOption);
				deathBenefit_AW = Double.parseDouble(BIMAST
						.getDeathBenefit_AW());
				// _deathBenefit_AW= deathBenefit_AW;
//				System.out.println("45. deathBenefit_AW " + deathBenefit_AW);

				if ((_month_E % 12) == 0) {

					bussIll.append("<DeathBen8Pr"
							+ _year_F
							+ ">"
							+ commonForAllProd.getRoundUp(commonForAllProd
							.getStringWithout_E(BIMAST.getDeathBenefit_AR(policyTerm)))
							+ "</DeathBen8Pr" + _year_F + ">");

				}

				// BIMAST.getCommission_BL(policyTerm);
				commission_BL = BIMAST.getCommission(policyTerm,
						smartPowerBean.getIsStaffDiscOrNot());
				// _commission_BL= commission_BL;
//				System.out.println("Commission " + commission_BL);
				sum_AS += commission_BL;
				if ((_month_E % 12) == 0) {

					bussIll.append("<Commission"
							+ _year_F
							+ ">"
							+ commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(sum_AS)) + "</Commission"
							+ _year_F + ">");
					sum_AS = 0;
				}

			}

			totalFYPremium = BIMAST.getTotalFirstYearPremium(sum_C, sum_E,
					sum_FY_Premium);
		} catch (Exception e) {
			e.printStackTrace();
			// System.out.println("** error **" + e.getMessage());
		}

		// System.out.println("Fund Value At the end AC "+commonForAllProd.getStringWithout_E(Double.parseDouble(commonForAllProd.getRoundUp(fundValueAtEnd_AC))));
		// System.out.println("Fund Value At the end AS "+commonForAllProd.getStringWithout_E(Double.parseDouble(commonForAllProd.getRoundUp(fundValueAtEnd_AS))));
		// return new
		// String[]{(commonForAllProd.getStringWithout_E(sumAssured)),(commonForAllProd.getStringWithout_E(Double.parseDouble(commonForAllProd.getRoundUp(fundValueAtEnd_AC)))),(commonForAllProd.getStringWithout_E(Double.parseDouble(commonForAllProd.getRoundUp(fundValueAtEnd_AS))))};
		return new String[] {
				(commonForAllProd.getStringWithout_E(sumAssured)),
				commonForAllProd.getRoundUp(commonForAllProd
						.getStringWithout_E(fundValueAtEnd_AC)),
				commonForAllProd.getRoundUp(commonForAllProd
						.getStringWithout_E(fundValueAtEnd_AS)),
				commonForAllProd.getRoundUp(commonForAllProd
						.getStringWithout_E(totalFYPremium)) };
	}

	/******************************************************* Start of Reduction in Yield **********************/

	private String[] getOutputReductionInYield(String sheetName,
											   SmartPowerInsuranceBean smartPowerBean) {
		// System.out.println("inside getoutputreductionyield");
		commonForAllProd = new CommonForAllProd();
		SmartPowerInsuranceProperties prop = new SmartPowerInsuranceProperties();
		ArrayList<String> List_BU = new ArrayList<>();
		ArrayList<String> List_BQ = new ArrayList<>();
		ArrayList<String> List_BP = new ArrayList<>();
		// output variable declaration
		int _month_E = 0, _year_F = 0, _age_H = 0, _month_BO = 0;

		// String _policyInforce_G="Y";
		//
		// double _premium_I=0,
		// _topUpPremium_J=0,
		// _premiumAllocationCharge_K=0,
		// _topUpCharges_L=0,
		// _ServiceTaxOnAllocation_M=0,
		// _amountAvailableForInvestment_N=0,
		// _sumAssuredRelatedCharges_O=0,
		// _riderCharges_P=0,
		// _policyAdministrationCharges_Q=0,
		// _mortalityCharges_R=0,
		// _accTPDCharges_S=0,
		// _totalCharges_T=0,
		// _serviceTaxExclOfSTOnAllocAndSurr_U=0,
		// _totalServiceTax_V=0,
		// _additionToFundIfAny_W=0,
		// _fundBeforeFMC_X=0,
		// _fundManagementCharge_Y=0,
		// _serviceTaxOnFMC_Z=0,
		// _fundValueAfterFMCBerforeGA_AA=0,
		// _guaranteedAddition_AB=0,
		// _fundValueAtEnd_AC=0,
		// _surrenderCharges_AD=0,
		// _serviceTaxOnSurrenderCharges_AE=0,
		// _surrenderValue_AF=0,
		// _deathBenefit_AG=0,
		// _mortalityCharges_AH=0,
		// _accTPDCharges_AI=0,
		// _totalCharges_AJ=0,
		// _serviceTaxExclOfSTOnAllocAndSurr_AK=0,
		// _totalServiceTax_AL=0,
		// _additionToFundIfAny_AM=0,
		// _fundBeforeFMC_AN=0,
		// _fundManagementCharge_AO=0,
		// _serviceTaxOnFMC_AP=0,
		// _fundValueAfterFMCBerforeGA_AQ=0,
		// _guaranteedAddition_AR=0,
		// _fundValueAtEnd_AS=0,
		// _surrenderCharges_AT=0,
		// _serviceTaxOnSurrenderCharges_AU=0,
		// _surrenderValue_AV=0,
		// _deathBenefit_AW=0,
		// _oneHundredPercentOfCummulativePremium_AY=0,
		// _surrenderCap_AX=0,
		// _reductionYield_BU=0,
		// _reductionYield_BQ=0,
		// _irrAnnual_BQ=0,
		// _irrAnnual_BU=0,
		// _reductionInYieldMaturityAt=0,
		// _reductionInYieldNumberOfYearsElapsedSinceInception=0,
		//
		// reductionYield_BP=0,
		// _reductionYield_BP=0,

		// _irrAnnual_BP=0;

		// temp variable declaration
		int month_E = 0, year_F = 0, age_H = 0, month_BO = 0;
		String policyInforce_G = "Y";

		double premium_I = 0, topUpPremium_J = 0, premiumAllocationCharge_K = 0, topUpCharges_L = 0, ServiceTaxOnAllocation_M = 0, amountAvailableForInvestment_N = 0, sumAssuredRelatedCharges_O = 0, riderCharges_P = 0, policyAdministrationCharges_Q = 0, mortalityCharges_R = 0, accTPDCharges_S = 0, totalCharges_T = 0, serviceTaxExclOfSTOnAllocAndSurr_U = 0, totalServiceTax_V = 0, additionToFundIfAny_W = 0, fundBeforeFMC_X = 0, fundManagementCharge_Y = 0, serviceTaxOnFMC_Z = 0, fundValueAfterFMCBerforeGA_AA = 0, guaranteedAddition_AB = 0, fundValueAtEnd_AC = 0, surrenderCharges_AD = 0, serviceTaxOnSurrenderCharges_AE = 0, surrenderValue_AF = 0, deathBenefit_AG = 0, mortalityCharges_AH = 0, accTPDCharges_AI = 0, totalCharges_AJ = 0, serviceTaxExclOfSTOnAllocAndSurr_AK = 0, totalServiceTax_AL = 0, additionToFundIfAny_AM = 0, fundBeforeFMC_AN = 0, fundManagementCharge_AO = 0, serviceTaxOnFMC_AP = 0, fundValueAfterFMCBerforeGA_AQ = 0, guaranteedAddition_AR = 0, fundValueAtEnd_AS = 0, surrenderCharges_AT = 0, serviceTaxOnSurrenderCharges_AU = 0, surrenderValue_AV = 0, deathBenefit_AW = 0, oneHundredPercentOfCummulativePremium_AY = 0, surrenderCap_AX = 0, reductionYield_BU = 0, reductionYield_BQ = 0, irrAnnual_BQ = 0, irrAnnual_BU = 0, reductionYield_BP = 0, reductionInYieldMaturityAt = 0, reductionInYieldNumberOfYearsElapsedSinceInception = 0, netYield4pa = 0, netYield8pa = 0, irrAnnual_BP = 0;

		// from GUI input
		boolean staffDisc = smartPowerBean.getIsStaffDiscOrNot();
		//double serviceTax=smartPowerBean.getServiceTax();
		double serviceTax=0;
		boolean isKerlaDisc =smartPowerBean.isKerlaDisc();
		int ageAtEntry = smartPowerBean.getAge();
		int policyTerm = smartPowerBean.getPolicy_Term();
		String premFreqMode = smartPowerBean.getPremFreqMode();
		double SAMF = smartPowerBean.getSAMF();
		double effectivePremium = smartPowerBean.getEffectivePremium();
		int PF = smartPowerBean.getPF();
		int noOfYearsElapsedSinceInception = smartPowerBean
				.getNoOfYearsElapsedSinceInception();
		// System.out.println("noOfYearsElapsedSinceInception "+noOfYearsElapsedSinceInception);
		// System.out.println("getPF "+smartPowerBean.getPF());
		// System.out.println("SAMF "+SAMF);

		double sumAssured = (effectivePremium * SAMF);
		// System.out.println("sumassured "+sumAssured);
		String increasingCoverOption = smartPowerBean
				.getIncreasingCoverOption();
		String addTopUp = "No";
		smartPowerBean.setEffectiveTopUpPrem(addTopUp, prop.topUpPremiumAmt);
		double effectiveTopUpPrem = smartPowerBean.getEffectiveTopUpPrem();

		SmartPowerInsuranceBusinessLogic BIMAST = new SmartPowerInsuranceBusinessLogic();
		String[] forBIArray = BIMAST
				.getForBIArr(prop.mortalityCharges_AsPercentOfofLICtable);
		int rowNumber = 0, monthNumber = 0;

		try {
			// calcultion of fields
			// for(int i=0; i < 1; i++)
			for (int i = 0; i <= (policyTerm * 12); i++) {
				rowNumber++;

				BIMAST.setMonth_E(rowNumber);
				month_E = Integer.parseInt(BIMAST.getMonth_E());
				_month_E = month_E;
				// System.out.println("1. month_E " +BIMAST.getMonth_E());

				BIMAST.setYear_F();
				year_F = Integer.parseInt(BIMAST.getYear_F());
				_year_F = year_F;
				// System.out.println("2. year_F "+BIMAST.getYear_F());
				if(isKerlaDisc == true && _year_F <=2){
					serviceTax =0.19;
				}else{
					serviceTax =0.18;
				}
				policyInforce_G = BIMAST.getPolicyInForce_G();
				// _policyInforce_G=BIMAST.getPolicyInForce_G();
				// System.out.println("3. policyInforce_G "+BIMAST.getPolicyInForce_G());

				BIMAST.setAge_H(ageAtEntry);
				age_H = Integer.parseInt(BIMAST.getAge_H());
				_age_H = age_H;
				// System.out.println("4. age_H "+BIMAST.getAge_H());

				BIMAST.setPremium_I(policyTerm, PF, effectivePremium);
				premium_I = Double.parseDouble(BIMAST.getPremium_I());
				// _premium_I=premium_I;
				// System.out.println("5. BIMAST.getPremium_I() "+BIMAST.getPremium_I());

				BIMAST.setTopUpPremium_J(prop.topUpStatusYield,
						effectiveTopUpPrem, addTopUp);
				topUpPremium_J = Double.parseDouble(BIMAST.getTopUpPremium_J());
				// _topUpPremium_J=topUpPremium_J;
				// System.out.println("6. Top up premium charges "+topUpPremium_J);

				BIMAST.setPremiumAllocationCharge_K(staffDisc, policyTerm);
				premiumAllocationCharge_K = Double.parseDouble(BIMAST
						.getPremiumAllocationCharge_K());
				// _premiumAllocationCharge_K=premiumAllocationCharge_K;
				// System.out.println("7. premiumAllocationCharge_K "+premiumAllocationCharge_K);

				BIMAST.setTopUpCharges_L(prop.topUp);
				topUpCharges_L = Double.parseDouble(BIMAST.getTopUpCharges_L());
				// _topUpCharges_L=topUpCharges_L;
				// System.out.println("8. topUpCharges_L "+topUpCharges_L);

				BIMAST.setServiceTaxOnAllocation_M(prop.allocationChargesYield,
						serviceTax);
				ServiceTaxOnAllocation_M = Double.parseDouble(BIMAST
						.getServiceTaxOnAllocation_M());
				// _ServiceTaxOnAllocation_M=ServiceTaxOnAllocation_M;

				// System.out.println("9. ServiceTaxOnAllocation_M "+ServiceTaxOnAllocation_M);

				BIMAST.setAmountAvailableForInvestment_N(policyTerm);
				amountAvailableForInvestment_N = Double.parseDouble(BIMAST
						.getAmountAvailableForInvestment_N());
				// _amountAvailableForInvestment_N=amountAvailableForInvestment_N;
				// System.out.println("10. amountAvailableForInvestment_N "+amountAvailableForInvestment_N);

				BIMAST.setSumAssuredRelatedCharges_O(policyTerm, sumAssured,
						prop.charge_SumAssuredBase);
				sumAssuredRelatedCharges_O = Double.parseDouble(BIMAST
						.getSumAssuredRelatedCharges_O());
				// _sumAssuredRelatedCharges_O=sumAssuredRelatedCharges_O;
				// System.out.println("11. sumAssuredRelatedCharges_O "+sumAssuredRelatedCharges_O);

				// System.out.println("12. RiderCharges_P "+riderCharges_P);

				BIMAST.setPolicyAdministrationCharge_Q(
						policyAdministrationCharges_Q, prop.charge_Inflation,
						prop.fixedMonthlyExp_RP, prop.inflation_pa_RP);
				policyAdministrationCharges_Q = Double.parseDouble(BIMAST
						.getPolicyAdministrationCharge_Q());
				// _policyAdministrationCharges_Q=policyAdministrationCharges_Q;
				// System.out.println("13. policyAdministrationCharges_Q "+policyAdministrationCharges_Q);

				BIMAST.setOneHundredPercentOfCummulativePremium_AY(oneHundredPercentOfCummulativePremium_AY);
				oneHundredPercentOfCummulativePremium_AY = Double
						.parseDouble(BIMAST
								.getOneHundredPercentOfCummulativePremium_AY());
				// _oneHundredPercentOfCummulativePremium_AY=oneHundredPercentOfCummulativePremium_AY;
				// System.out.println("46. oneHundredPercentOfCummulativePremium_AY "+oneHundredPercentOfCummulativePremium_AY);

				BIMAST.setMortalityCharges_R(ageAtEntry, sumAssured,
						forBIArray, policyTerm, prop.mortalityChargesYield,
						fundValueAtEnd_AC, increasingCoverOption);
				mortalityCharges_R = Double.parseDouble(BIMAST
						.getMortalityCharges_R());
				// _mortalityCharges_R=mortalityCharges_R;
				// System.out.println("14. mortalityCharges_R "+mortalityCharges_R);

				BIMAST.setAccTPDCharge_S(policyTerm, prop.accTPD_Charge,
						sumAssured, prop.mortalityChargesYield,
						fundValueAtEnd_AC, increasingCoverOption);
				accTPDCharges_S = Double
						.parseDouble(BIMAST.getAccTPDCharge_S());
				// _accTPDCharges_S=accTPDCharges_S;
				// System.out.println("15. accTPDCharges_S "+accTPDCharges_S);

				BIMAST.setTotalCharges_T(policyTerm, riderCharges_P);
				totalCharges_T = Double.parseDouble(BIMAST.getTotalCharges_T());
				// _totalCharges_T=totalCharges_T;
				// System.out.println("16. totalCharges_T "+totalCharges_T);

				BIMAST.setServiceTaxExclOfSTOnAllocAndSurrReductionYield_U(
						prop.mortalityAndRiderChargesYield,
						prop.administrationChargesYield, serviceTax);
				serviceTaxExclOfSTOnAllocAndSurr_U = Double.parseDouble(BIMAST
						.getServiceTaxExclOfSTOnAllocAndSurrReductionYield_U());
				// _serviceTaxExclOfSTOnAllocAndSurr_U=serviceTaxExclOfSTOnAllocAndSurr_U;
				// System.out.println("17. serviceTaxExclOfSTOnAllocAndSurr_U "+serviceTaxExclOfSTOnAllocAndSurr_U);

				BIMAST.setAdditionToFundIfAnyReductionYield_W(
						fundValueAtEnd_AC, policyTerm, prop.int1,
						riderCharges_P);
				additionToFundIfAny_W = Double.parseDouble(BIMAST
						.getAdditionToFundIfAnyReductionYield_W());
				// _additionToFundIfAny_W=additionToFundIfAny_W;
				// System.out.println("19. additionToFundIfAny_W "+additionToFundIfAny_W);

				BIMAST.setFundBeforeFMCReductionYield_X(fundValueAtEnd_AC,
						policyTerm, riderCharges_P);
				fundBeforeFMC_X = Double.parseDouble(BIMAST
						.getFundBeforeFMCReductionYield_X());
				// _fundBeforeFMC_X=fundBeforeFMC_X;
				// System.out.println("20. fundBeforeFMC_X "+fundBeforeFMC_X);

				BIMAST.setFundManagementChargeReductionYield_Y(policyTerm,
						smartPowerBean.getFundOption(),
						smartPowerBean.getPercentToBeInvested_EquityFund(),
						smartPowerBean.getPercentToBeInvested_BondFund(),
						smartPowerBean.getPercentToBeInvested_Top300Fund(),
						smartPowerBean.getPercentToBeInvested_EquityOptFund(),
						smartPowerBean.getPercentToBeInvested_GrowthFund(),
						smartPowerBean.getPercentToBeInvested_Balanced(),
						smartPowerBean.getPercentToBeInvested_MoneyMarketFund(),
						smartPowerBean.getPercentToBeInvested_BondOptimiserFund(),
						smartPowerBean.getPercentToBeInvested_PureFund(),
						smartPowerBean.getPercentToBeInvested_CorpBondFund());
				fundManagementCharge_Y = Double.parseDouble(BIMAST
						.getFundManagementChargeReductionYield_Y());
				// _fundManagementCharge_Y=fundManagementCharge_Y;
				// System.out.println("21. fundManagementCharge_Y "+fundManagementCharge_Y);

				BIMAST.setServiceTaxOnFMCReductionYield_Z(
						prop.fundManagementChargesYield, serviceTax);
				serviceTaxOnFMC_Z = Double.parseDouble(BIMAST
						.getServiceTaxOnFMCReductionYield_Z());
				// _serviceTaxOnFMC_Z=serviceTaxOnFMC_Z;
				// System.out.println("22. serviceTaxOnFMC_Z "+serviceTaxOnFMC_Z);

				BIMAST.setTotalServiceTaxReductionYield_V(
						prop.riderChargesYield, riderCharges_P,serviceTax);
				totalServiceTax_V = Double.parseDouble(BIMAST
						.getTotalServiceTaxReductionYield_V());
				// _totalServiceTax_V=totalServiceTax_V;
				// System.out.println("18. totalServiceTax_V "+totalServiceTax_V);

				BIMAST.setFundValueAfterFMCandBeforeGAReductionYield_AA(policyTerm);
				fundValueAfterFMCBerforeGA_AA = Double.parseDouble(BIMAST
						.getFundValueAfterFMCandBeforeGAReductionYield_AA());
				// _fundValueAfterFMCBerforeGA_AA=fundValueAfterFMCBerforeGA_AA;
				// System.out.println("23. fundValueAfterFMCBerforeGA_AA "+fundValueAfterFMCBerforeGA_AA);
				// System.out.println("23. BIMAST.getFundValueAfterFMCandBeforeGA_AA() "+BIMAST.getFundValueAfterFMCandBeforeGAReductionYield_AA());

				BIMAST.setGuaranteedAddition_AB(effectivePremium, policyTerm,
						prop.guaranteedAddition);
				guaranteedAddition_AB = Double.parseDouble(BIMAST
						.getGuaranteedAddition_AB());
				// _guaranteedAddition_AB=guaranteedAddition_AB;
				// System.out.println("24.guaranteedAddition_AB "+guaranteedAddition_AB);

				// //////////
				BIMAST.setMonth_BO(monthNumber);
				month_BO = Integer.parseInt(BIMAST.getMonth_BO());
				_month_BO = month_BO;
				// System.out.println("month_bo "+month_BO);

				BIMAST.setReductionYield_BP(policyTerm, fundValueAtEnd_AC);
				reductionYield_BP = Double.parseDouble(BIMAST
						.getReductionYield_BP());
				// _reductionYield_BP=reductionYield_BP;
//				System.out.println("reductionYield_BP " + reductionYield_BP);

				BIMAST.setReductionYield_BU(noOfYearsElapsedSinceInception,
						fundValueAtEnd_AS);
				reductionYield_BU = Double.parseDouble(BIMAST
						.getReductionYield_BU());
				// _reductionYield_BU=reductionYield_BU;
				// System.out.println("reductionYield_BU "+reductionYield_BU);

				BIMAST.setReductionYield_BQ(policyTerm, fundValueAtEnd_AS);
				reductionYield_BQ = Double.parseDouble(BIMAST
						.getReductionYield_BQ());
				// _reductionYield_BQ=reductionYield_BQ;
				// System.out.println("reductionYield_BQ "+reductionYield_BQ);

				// /////////////

				BIMAST.setFundValueAtEndReductionYield_AC();
				fundValueAtEnd_AC = Double.parseDouble(BIMAST
						.getFundValueAtEndReductionYield_AC());
				// _fundValueAtEnd_AC=fundValueAtEnd_AC;
				// System.out.println("25. fundValueAtEnd_AC "+fundValueAtEnd_AC);

				BIMAST.setSurrenderCap_AX(effectivePremium);
				surrenderCap_AX = Double.parseDouble(BIMAST
						.getSurrenderCap_AX());
				// _surrenderCap_AX=surrenderCap_AX;
				// System.out.println("46. surrenderCap_AX "+surrenderCap_AX);

				BIMAST.setSurrenderChargesReductionYield_AD(effectivePremium,
						policyTerm);
				surrenderCharges_AD = Double.parseDouble(BIMAST
						.getSurrenderChargesReductionYield_AD());
				// _surrenderCharges_AD=surrenderCharges_AD;
				// System.out.println("26. surrenderCharges_AD "+surrenderCharges_AD);

				BIMAST.setServiceTaxOnSurrenderChargesReductionYield_AE(
						prop.surrenderCharges, serviceTax);
				serviceTaxOnSurrenderCharges_AE = Double.parseDouble(BIMAST
						.getServiceTaxOnSurrenderChargesReductionYield_AE());
				// _serviceTaxOnSurrenderCharges_AE=serviceTaxOnSurrenderCharges_AE;
				// System.out.println("27.  serviceTaxOnSurrenderCharges_AE "+
				// serviceTaxOnSurrenderCharges_AE);

				BIMAST.setSurrenderValueReductionYield_AF();
				surrenderValue_AF = Double.parseDouble(BIMAST
						.getSurrenderValueReductionYield_AF());
				// _surrenderValue_AF=surrenderValue_AF;
				// System.out.println("28. surrenderValue_AF "+surrenderValue_AF);

				BIMAST.setDeathBenefitReductionYield_AG(policyTerm, sumAssured,
						increasingCoverOption);
				deathBenefit_AG = Double.parseDouble(BIMAST
						.getDeathBenefitReductionYield_AG());
				// _deathBenefit_AG=deathBenefit_AG;
				// System.out.println("29. deathBenefit_AG "+deathBenefit_AG);

				// System.out.println("\n @@@@@@@@@@@@@@@@@@@@@ 8% interest rate @@@@@@@@@@@@@@@@@@@@@ ");

				BIMAST.setMortalityCharges_AH(ageAtEntry, sumAssured,
						forBIArray, policyTerm, prop.mortalityChargesYield,
						fundValueAtEnd_AS, increasingCoverOption);
				mortalityCharges_AH = Double.parseDouble(BIMAST
						.getMortalityCharges_AH());
				// _mortalityCharges_AH=mortalityCharges_AH;
				// System.out.println("30. mortalityCharges_AH "+BIMAST.getMortalityCharges_AH());

				BIMAST.setAccTPDCharge_AI(policyTerm, prop.accTPD_Charge,
						sumAssured, prop.mortalityChargesYield,
						fundValueAtEnd_AS, increasingCoverOption);
				accTPDCharges_AI = Double.parseDouble(BIMAST
						.getAccTPDCharge_AI());
				// _accTPDCharges_AI=accTPDCharges_AI;
				// System.out.println("31. accTPDCharges_AI "+
				// accTPDCharges_AI);

				BIMAST.setTotalCharges_AJ(policyTerm, riderCharges_P);
				totalCharges_AJ = Double.parseDouble(BIMAST
						.getTotalCharges_AJ());
				// _totalCharges_AJ=totalCharges_AJ;
				// System.out.println("32. totalCharges_AJ "+totalCharges_AJ);

				BIMAST.setServiceTaxExclOfSTOnAllocAndSurrReductionYield_AK(
						prop.mortalityAndRiderChargesYield,
						prop.administrationChargesYield, serviceTax);
				serviceTaxExclOfSTOnAllocAndSurr_AK = Double
						.parseDouble(BIMAST
								.getServiceTaxExclOfSTOnAllocAndSurrReductionYield_AK());
				// _serviceTaxExclOfSTOnAllocAndSurr_AK=serviceTaxExclOfSTOnAllocAndSurr_AK;
				// System.out.println("33. serviceTaxExclOfSTOnAllocAndSurr_AK "+serviceTaxExclOfSTOnAllocAndSurr_AK);

				BIMAST.setAdditionToFundIfAnyReductionYield_AM(
						fundValueAtEnd_AS, policyTerm, prop.int2,
						riderCharges_P);
				additionToFundIfAny_AM = Double.parseDouble(BIMAST
						.getAdditionToFundIfAnyReductionYield_AM());
				// _additionToFundIfAny_AM=additionToFundIfAny_AM;
				// System.out.println("35. addtitionToFunIfAny "+additionToFundIfAny_AM);

				BIMAST.setFundBeforeFMCReductionYield_AN(fundValueAtEnd_AS,
						policyTerm, riderCharges_P);
				fundBeforeFMC_AN = Double.parseDouble(BIMAST
						.getFundBeforeFMCReductionYield_AN());
				// _fundBeforeFMC_AN=fundBeforeFMC_AN;
				// System.out.println("36. fundBeforeFMC_AN "+fundBeforeFMC_AN);

				BIMAST.setFundManagementChargeReductionYield_AO(policyTerm,
						smartPowerBean.getFundOption(),
						smartPowerBean.getPercentToBeInvested_EquityFund(),
						smartPowerBean.getPercentToBeInvested_BondFund(),
						smartPowerBean.getPercentToBeInvested_Top300Fund(),
						smartPowerBean.getPercentToBeInvested_EquityOptFund(),
						smartPowerBean.getPercentToBeInvested_GrowthFund(),
						smartPowerBean.getPercentToBeInvested_Balanced(),
						smartPowerBean.getPercentToBeInvested_MoneyMarketFund(),
						smartPowerBean.getPercentToBeInvested_BondOptimiserFund(),
						smartPowerBean.getPercentToBeInvested_PureFund(),
						smartPowerBean.getPercentToBeInvested_CorpBondFund());
				fundManagementCharge_AO = Double.parseDouble(BIMAST
						.getFundManagementChargeReductionYield_AO());
				// _fundManagementCharge_AO=fundManagementCharge_AO;
				// System.out.println("37. fundManagementCharge_AO "+fundManagementCharge_AO);

				BIMAST.setServiceTaxOnFMCReductionYield_AP(
						prop.fundManagementChargesYield, serviceTax);
				serviceTaxOnFMC_AP = Double.parseDouble(BIMAST
						.getServiceTaxOnFMCReductionYield_AP());
				// _serviceTaxOnFMC_AP=serviceTaxOnFMC_AP;
				// System.out.println("38. serviceTaxOnFMC_AP "+serviceTaxOnFMC_AP);

				BIMAST.setTotalServiceTaxReductionYield_AL(
						prop.riderChargesYield, riderCharges_P,serviceTax);
				totalServiceTax_AL = Double.parseDouble(BIMAST
						.getTotalServiceTaxReductionYield_AL());
				// _totalServiceTax_AL=totalServiceTax_AL;
				// System.out.println("34. totalServiceTax_AL "+totalServiceTax_AL);

				BIMAST.setFundValueAfterFMCandBeforeGAReductionYield_AQ(policyTerm);
				fundValueAfterFMCBerforeGA_AQ = Double.parseDouble(BIMAST
						.getFundValueAfterFMCandBeforeGAReductionYield_AQ());
				// _fundValueAfterFMCBerforeGA_AQ=fundValueAfterFMCBerforeGA_AQ;
				// System.out.println("39. fundValueAfterFMCBerforeGA_AQ "+fundValueAfterFMCBerforeGA_AQ);

				BIMAST.setGuaranteedAddition_AR(effectivePremium, policyTerm,
						prop.guaranteedAddition);
				guaranteedAddition_AR = Double.parseDouble(BIMAST
						.getGuaranteedAddition_AR());
				// _guaranteedAddition_AR=guaranteedAddition_AR;
				// System.out.println("40. guaranteedAddition_AR "+guaranteedAddition_AR);

				// ////////////////////////////////
				BIMAST.setFundValueAtEndReductionYield_AS();
				fundValueAtEnd_AS = Double.parseDouble(BIMAST
						.getFundValueAtEndReductionYield_AS());
				// _fundValueAtEnd_AS=fundValueAtEnd_AS;
				// System.out.println("41. fundValueAtEnd_AS "+fundValueAtEnd_AS);

				BIMAST.setSurrenderChargesReductionYield_AT(effectivePremium,
						policyTerm);
				surrenderCharges_AT = Double.parseDouble(BIMAST
						.getSurrenderChargesReductionYield_AT());
				// _surrenderCharges_AT=surrenderCharges_AT;
				// System.out.println("42. surrenderCharges_AT "+surrenderCharges_AT);

				BIMAST.setServiceTaxOnSurrenderChargesReductionYield_AU(
						prop.surrenderCharges, serviceTax);
				serviceTaxOnSurrenderCharges_AU = Double.parseDouble(BIMAST
						.getServiceTaxOnSurrenderChargesReductionYield_AU());
				// _serviceTaxOnSurrenderCharges_AU=serviceTaxOnSurrenderCharges_AU;
				// System.out.println("43.  serviceTaxOnSurrenderCharges_AU "+
				// serviceTaxOnSurrenderCharges_AU);

				BIMAST.setSurrenderValueReductionYield_AV();
				surrenderValue_AV = Double.parseDouble(BIMAST
						.getSurrenderValueReductionYield_AV());
				// _surrenderValue_AV= surrenderValue_AV;
				// System.out.println("44.  surrenderValue_AV "+
				// surrenderValue_AV);

				BIMAST.setDeathBenefitReductionYield_AW(policyTerm, sumAssured,
						increasingCoverOption);
				deathBenefit_AW = Double.parseDouble(BIMAST
						.getDeathBenefitReductionYield_AW());
				// _deathBenefit_AW= deathBenefit_AW;
				// System.out.println("45. deathBenefit_AW "+ deathBenefit_AW);

				monthNumber++;

				List_BQ.add(commonForAllProd.roundUp_Level2(commonForAllProd
						.getStringWithout_E(reductionYield_BQ)));
				List_BU.add(commonForAllProd.roundUp_Level2(commonForAllProd
						.getStringWithout_E(reductionYield_BU)));
				List_BP.add(commonForAllProd.roundUp_Level2(commonForAllProd
						.getStringWithout_E(reductionYield_BP)));

			}
			// System.out.println(_month_BO+". fundValueAtEnd_AS "+fundValueAtEnd_AS);
			// System.out.println("List_BQ.size "+List_BQ.size());
			// System.out.println("List_BQ "+List_BQ);
			// System.out.println("List_BU.size "+List_BU.size());
			// System.out.println("List_BU "+List_BU);
			double ans = BIMAST.irr(List_BQ, 0.01);
			double ans1 = BIMAST.irr(List_BU, 0.001);
			double ans2 = BIMAST.irr(List_BP, 0.001);
//			System.out.println("ans_BQ " + ans);
			// System.out.println("ans1_BU "+ans1);
//			System.out.println("ans2_BP " + ans2);

			BIMAST.setIRRAnnual_BP(ans2);
			irrAnnual_BP = Double.parseDouble(BIMAST.getIRRAnnual_BP());
			// _irrAnnual_BP=irrAnnual_BP;
//			System.out.println("irrAnnual_BP " + irrAnnual_BP);

			BIMAST.setIRRAnnual_BQ(ans);
			irrAnnual_BQ = Double.parseDouble(BIMAST.getIRRAnnual_BQ());
			// _irrAnnual_BQ=irrAnnual_BQ;
//			System.out.println("irrAnnual_BQ " + irrAnnual_BQ);

			BIMAST.setIRRAnnual_BU(ans1);
			irrAnnual_BU = Double.parseDouble(BIMAST.getIRRAnnual_BU());
			// _irrAnnual_BU=irrAnnual_BU;
			// System.out.println("irrAnnual_BU "+irrAnnual_BU);

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
//			System.out.println("netYield4pa " + netYield4pa);
			BIMAST.setnetYieldAt8Percent();
			netYield8pa = Double.parseDouble(BIMAST.getnetYieldAt8Percent());
//			System.out.println("netYield8pa " + netYield8pa);
		} catch (Exception e) {
			e.printStackTrace();
			// System.out.println("** error **" + e.getMessage());
		}

		// System.out.println("reduction yield maturity at "+commonForAllProd.getRoundOffLevel2(commonForAllProd.getStringWithout_E(reductionInYieldMaturityAt)));
		// System.out.println("reduction yield number of years elapsed since inception  "+commonForAllProd.getRoundOffLevel2(commonForAllProd.getStringWithout_E(reductionInYieldNumberOfYearsElapsedSinceInception)));
		// return new
		// String[]{(commonForAllProd.getStringWithout_E(sumAssured)),(commonForAllProd.getStringWithout_E(Double.parseDouble(commonForAllProd.getRoundUp(fundValueAtEnd_AC)))),(commonForAllProd.getStringWithout_E(Double.parseDouble(commonForAllProd.getRoundUp(fundValueAtEnd_AS))))};
		return new String[] {
				commonForAllProd.getRoundOffLevel2(commonForAllProd
						.getStringWithout_E(reductionInYieldMaturityAt)),
				commonForAllProd
						.getRoundOffLevel2(commonForAllProd
						.getStringWithout_E(reductionInYieldNumberOfYearsElapsedSinceInception)),
				commonForAllProd.getRoundOffLevel2(commonForAllProd
						.getStringWithout_E(netYield4pa)),
				commonForAllProd.getRoundOffLevel2(commonForAllProd
						.getStringWithout_E(netYield8pa)) };
	}

	private void getInput(SmartPowerInsuranceBean smartPowerInsuranceBean) {

		inputVal = new StringBuilder();
		// From GUI Input
		String LifeAssured_title = spnr_bi_smart_power_insurance_life_assured_title
				.getSelectedItem().toString();
		String LifeAssured_firstName = edt_bi_smart_power_insurance_life_assured_first_name
				.getText().toString();
		String LifeAssured_middleName = edt_bi_smart_power_insurance_life_assured_middle_name
				.getText().toString();
		String LifeAssured_lastName = edt_bi_smart_power_insurance_life_assured_last_name
				.getText().toString();
		String LifeAssured_DOB = btn_bi_smart_power_insurance_life_assured_date
				.getText().toString();
		String LifeAssured_age = edt_bi_smart_power_insurance_life_assured_age
				.getText().toString();

		String proposer_title = "";
		String proposer_firstName = "";
		String proposer_middleName = "";
		String proposer_lastName = "";
		String proposer_DOB = "";
		String proposer_age = "";
		String proposer_gender = "";

		boolean staffDisc = smartPowerInsuranceBean.getIsStaffDiscOrNot();
		smartPowerInsuranceBean.getPolicy_Term();

		boolean isJKresident = smartPowerInsuranceBean.isJKResident();

		inputVal.append("<?xml version='1.0' encoding='utf-8' ?><smartPowerInsuranceBean>");
		inputVal.append("<LifeAssured_title>").append(LifeAssured_title).append("</LifeAssured_title>");
		inputVal.append("<LifeAssured_firstName>").append(LifeAssured_firstName).append("</LifeAssured_firstName>");
		inputVal.append("<LifeAssured_middleName>").append(LifeAssured_middleName).append("</LifeAssured_middleName>");
		inputVal.append("<LifeAssured_lastName>").append(LifeAssured_lastName).append("</LifeAssured_lastName>");
		inputVal.append("<LifeAssured_DOB>").append(LifeAssured_DOB).append("</LifeAssured_DOB>");
		inputVal.append("<LifeAssured_age>").append(LifeAssured_age).append("</LifeAssured_age>");
		// inputVal.append("<gender>" + gender + "</gender>");

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

		inputVal.append("<proposer_Is_Same_As_Life_Assured>"
				+ proposer_Is_Same_As_Life_Assured
				+ "</proposer_Is_Same_As_Life_Assured>");
		inputVal.append("<isStaff>").append(staffDisc).append("</isStaff>");
		inputVal.append("<str_kerla_discount>" + str_kerla_discount + "</str_kerla_discount>");
		inputVal.append("<age>").append(edt_bi_smart_power_insurance_life_assured_age.getText()
				.toString()).append("</age>");
		inputVal.append("<isJKResident>").append(isJKresident).append("</isJKResident>");
		inputVal.append("<gender>").append(spnr_bi_smart_power_insurance_selGender.getSelectedItem()
				.toString()).append("</gender>");

		inputVal.append("<premFreqMode>").append(spnr_bi_smart_power_insurance_premium_frequency
				.getSelectedItem().toString()).append("</premFreqMode>");

		inputVal.append("<policyTerm>").append(spnr_bi_smart_power_insurance_policyterm.getSelectedItem()
				.toString()).append("</policyTerm>");

		inputVal.append("<premiumAmount>").append(edt_bi_smart_power_insurance_sum_assured_amount.getText()
				.toString()).append("</premiumAmount>");

		inputVal.append("<selectFund>").append(spnr_select_fund.getSelectedItem().toString()).append("</selectFund>");

		inputVal.append("<increaseCoverOption>").append(spnr_increasing_cover_option.getSelectedItem().toString()).append("</increaseCoverOption>");

		inputVal.append("<SAMF>").append(edt_smart_power_insurance_samf.getText().toString()).append("</SAMF>");

//        inputVal.append("<noOfYrElapsed>").append(edt_smart_power_insurance_noOfYearsElapsedSinceInception
//                .getText().toString()).append("</noOfYrElapsed>");
		inputVal.append("<noOfYrElapsed>").append("").append("</noOfYrElapsed>");
		inputVal.append("<perInvEquityFund>").append(edt_smart_power_insurance_percent_EquityFund.getText()
				.toString()).append("</perInvEquityFund>");

		inputVal.append("<perInvEquityOptFund>").append(edt_smart_power_insurance_percent_equityOptFund.getText()
				.toString()).append("</perInvEquityOptFund>");

		inputVal.append("<perInvGrowthFund>").append(edt_smart_power_insurance_percent_growthFund.getText()
				.toString()).append("</perInvGrowthFund>");

		inputVal.append("<perInvBalancedFund>").append(edt_smart_power_insurance_percent_BalancedFund.getText()
				.toString()).append("</perInvBalancedFund>");
		inputVal.append("<perInvBondFund>").append(edt_smart_power_insurance_percent_BondFund.getText()
				.toString()).append("</perInvBondFund>");

		inputVal.append("<perInvMoneyMktFund>").append(edt_smart_power_insurance_percent_moneyMktFund.getText()
				.toString()).append("</perInvMoneyMktFund>");
		inputVal.append("<perInvTop300Fund>").append(edt_smart_power_insurance_percent_top300Fund.getText()
				.toString()).append("</perInvTop300Fund>");
		inputVal.append("<perInvBondOptimiserFund>"
				+ percent_BondOptimiserFund.getText().toString()
				+ "</perInvBondOptimiserFund>");

		inputVal.append("<perInvCorporateBondFund>"
				+ percent_CorporateBondFund.getText().toString()
				+ "</perInvCorporateBondFund>");

		inputVal.append("<perInvPureFund>"
				+ percent_PureFund.getText().toString()
				+ "</perInvPureFund>");
		//Added Tushar Kadam Kerla Applicable Tax 7-Jun-2019
		String str_kerla_discount = "N";
		if (cb_kerladisc.isChecked()) {
			str_kerla_discount = "Y";
		}

		inputVal.append("<KFC>" + str_kerla_discount + "</KFC>");
		//End Added Tushar Kadam Kerla Applicable Tax 7-Jun-2019

		inputVal.append("</smartPowerInsuranceBean>");

	}

	private void Dialog() {

		final Dialog d = new Dialog(this);
		d.requestWindowFeature(Window.FEATURE_NO_TITLE);
		d.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.BLACK));

		d.setContentView(R.layout.layout_smart_power_insurance_bi_grid);

		TextView tv_proposername = d
				.findViewById(R.id.tv_proposername);
		TextView tv_proposal_number = d
				.findViewById(R.id.tv_proposal_number);
		TextView tv_bi_smart_power_insurance_age_entry = d
				.findViewById(R.id.tv_bi_smart_power_insurance_age_entry);
		TextView tv_bi_smart_power_insurance_age_entry_1 = (TextView) d
				.findViewById(R.id.tv_bi_smart_power_insurance_age_entry_1);
		TextView tv_bi_smart_power_insurance_maturity_age = (TextView) d
				.findViewById(R.id.tv_bi_smart_power_insurance_maturity_age);
		TextView tv_bi_smart_power_insurance_life_assured_gender = d
				.findViewById(R.id.tv_bi_smart_power_insurance_life_assured_gender);
		TextView tv_bi_smart_power_insurance_policy_term = d
				.findViewById(R.id.tv_bi_smart_power_insurance_policy_term);
		TextView tv_bi_smart_power_insurance_annualised_premium = d
				.findViewById(R.id.tv_bi_smart_power_insurance_annualised_premium);
		TextView tv_bi_smart_power_insurance_sum_assured_main = d
				.findViewById(R.id.tv_bi_smart_power_insurance_sum_assured_main);
		TextView tv_bi_smart_power_insurance_mode = d
				.findViewById(R.id.tv_bi_smart_power_insurance_mode);
		TextView tv_bi_smart_power_insurance_increasing_cover_option = d
				.findViewById(R.id.tv_bi_smart_power_insurance_increasing_cover_option);

//        TextView tv_bi_total_first_year_premium = d
//                .findViewById(R.id.tv_bi_total_first_year_premium);


		TextView tv_smart_power_insurance_equity_fund_allocation = d
				.findViewById(R.id.tv_smart_power_insurance_equity_fund_allocation);
		TextView tv_smart_power_insurance_equity_optimiser_fund_allocation = d
				.findViewById(R.id.tv_smart_power_insurance_equity_optimiser_fund_allocation);
		TextView tv_smart_power_insurance_growth_fund_allocation = d
				.findViewById(R.id.tv_smart_power_insurance_growth_fund_allocation);

		TextView tv_smart_power_insurance_balanced_fund_allocation = d
				.findViewById(R.id.tv_smart_power_insurance_balanced_fund_allocation);
		TextView tv_smart_power_insurance_bond_fund_allocation = d
				.findViewById(R.id.tv_smart_power_insurance_bond_fund_allocation);
		TextView tv_smart_power_insurance_market_fund_allocation = d
				.findViewById(R.id.tv_smart_power_insurance_market_fund_allocation);
		TextView tv_smart_power_insurance_top_300_fund_allocation = d
				.findViewById(R.id.tv_smart_power_insurance_top_300_fund_allocation);

		TextView tv_smart_power_insurance_equity_fund_fmc = (TextView) d
				.findViewById(R.id.tv_smart_power_insurance_equity_fund_fmc);

		TextView tv_smart_power_insurance_balanced_fund_fmc = (TextView) d
				.findViewById(R.id.tv_smart_power_insurance_balanced_fund_fmc);
		TextView tv_smart_power_insurance_bond_fund_fmc = (TextView) d
				.findViewById(R.id.tv_smart_power_insurance_bond_fund_fmc);

		TextView tv_smart_power_insurance_no_of_years_elapsed = d
				.findViewById(R.id.tv_smart_power_insurance_no_of_years_elapsed);
		TextView tv_smart_power_insurance_reduction_yield = d
				.findViewById(R.id.tv_smart_power_insurance_reduction_yield);
		TextView tv_smart_power_insurance_maturity_at = d
				.findViewById(R.id.tv_smart_power_insurance_maturity_at);
		TextView tv_smart_power_insurance_reduction_yeild2 = d
				.findViewById(R.id.tv_smart_power_insurance_reduction_yeild2);
		final TextView edt_proposer_name = d
				.findViewById(R.id.edt_ProposalName);
		final EditText edt_Policyholderplace = d
				.findViewById(R.id.edt_Policyholderplace);

		TextView tv_smart_power_insurance_death_benefit = d
				.findViewById(R.id.tv_smart_power_insurance_death_benefit);

//        TextView tv_smart_power_insurance_net_yield4 = d
//                .findViewById(R.id.tv_smart_power_insurance_net_yield_4);

		TextView tv_smart_power_insurance_net_yield8 = d
				.findViewById(R.id.tv_smart_power_insurance_net_yield_8);

		TextView tv_bi_is_Staff = d
				.findViewById(R.id.tv_bi_is_Staff);

		TextView tv_bi_is_jk = d.findViewById(R.id.tv_bi_is_jk);

		GridView gv_userinfo = (GridView) d.findViewById(R.id.gv_userinfo);
		GridView gv_userinfo2 = (GridView) d.findViewById(R.id.gv_userinfo2);
		GridView gv_userinfo3 = (GridView) d.findViewById(R.id.gv_userinfo3);
		final CheckBox cb_statement = d
				.findViewById(R.id.cb_statement);
		cb_statement.setChecked(false);

		TableRow tr_submit_id_proof = (TableRow) d
				.findViewById(R.id.tr_submit_id_proof);
		if (Double.parseDouble(edt_bi_smart_power_insurance_sum_assured_amount.getText().toString()) > 100000) {
			tr_submit_id_proof.setVisibility(View.VISIBLE);
		} else {
			tr_submit_id_proof.setVisibility(View.GONE);
		}
		TextView tv_channel_intermediary = (TextView) d
				.findViewById(R.id.tv_channel_intermediary);
		tv_channel_intermediary.setText(userType);
		TextView tv_bi_smart_power_insurance_name_of_life_assured = (TextView) d.findViewById(R.id.tv_bi_smart_power_insurance_name_of_life_assured);
		tv_bi_smart_power_insurance_name_of_life_assured.setText(name_of_life_assured);


		TextView tv_bi_smart_power_insurance_name_of_life_assured_1 = (TextView) d.findViewById(R.id.tv_bi_smart_power_insurance_name_of_life_assured_1);
		tv_bi_smart_power_insurance_name_of_life_assured_1.setText(name_of_life_assured);

		/* Need Analysis */
		final TextView edt_proposer_name_need_analysis = d
				.findViewById(R.id.edt_proposer_name_need_analysis);

		final CheckBox cb_statement_need_analysis = d
				.findViewById(R.id.cb_statement_need_analysis);
		cb_statement_need_analysis.setChecked(true);
		TableRow tr_need_analysis = d
				.findViewById(R.id.tr_need_analysis);

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
		list_data1.clear();
		list_data2.clear();
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
							+ ", have undergone the Need Analysis  after having reviewed the SBI Life Product options, I have opted for SBI LIFE - Smart Power Insurance.");

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
							+ ", have undergone the Need Analysis  after having reviewed the SBI Life Product options, I have opted for SBI LIFE - Smart Power Insurance.");

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

		imageButtonSmartPowerInsuranceProposerPhotograph = d
				.findViewById(R.id.imageButtonSmartPowerInsuranceProposerPhotograph);
		imageButtonSmartPowerInsuranceProposerPhotograph
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
			imageButtonSmartPowerInsuranceProposerPhotograph
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
							imageButtonSmartPowerInsuranceProposerPhotograph
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

		if(!TextUtils.isEmpty(place2)){
			edt_Policyholderplace.setText(place2);
		}

		btn_proceed.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// if (frmProductHome.equals("FALSE")) {
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
					String productCode = "";

					if (smartPowerInsuranceBean.getIncreasingCoverOption()
							.equalsIgnoreCase("Yes")
							&& smartPowerInsuranceBean.getFundOption()
							.equalsIgnoreCase("Smart Fund"))
						productCode = "UPSPWICS";
					else if(smartPowerInsuranceBean.getIncreasingCoverOption()
							.equalsIgnoreCase("Yes")
							&& smartPowerInsuranceBean.getFundOption()
							.equalsIgnoreCase("Trigger Fund"))
						productCode = "UPSPWICT";
					else if (smartPowerInsuranceBean.getIncreasingCoverOption()
							.equalsIgnoreCase("No")
							&& smartPowerInsuranceBean.getFundOption()
							.equalsIgnoreCase("Smart Fund"))
						productCode = "UPSPWLCS";
					else if (smartPowerInsuranceBean.getIncreasingCoverOption()
							.equalsIgnoreCase("No")
							&& smartPowerInsuranceBean.getFundOption()
							.equalsIgnoreCase("Trigger Fund"))
						productCode = "UPSPWLCT";

					na_cbi_bean = new NA_CBI_bean(QuatationNumber, agentcode,
							"", userType, "", lifeAssured_Title,
							lifeAssured_First_Name, lifeAssured_Middle_Name,
							lifeAssured_Last_Name, planName, obj
							.getRound(sumAssured), obj
							.getRound(premiumAmount), emailId,
							mobileNo, agentEmail, agentMobile, na_input,
							na_output, premPayingMode, Integer
							.parseInt(policyTerm), 0, productCode,
							getDate(lifeAssured_date_of_birth),
							getDate(proposer_date_of_birth), inputVal
							.toString(), retVal.toString().replace(
							bussIll.toString(), ""));

					name_of_person = name_of_life_assured;

					if (radioButtonTrasactionModeParivartan.isChecked()) {
						mode = "Parivartan";
					} else if(radioButtonTrasactionModeManual.isChecked()){
						mode = "Manual";
					}
					dbHelper.AddNeedAnalysisDashboardDetails(new ProductBIBean(
							"", QuatationNumber, planName, getCurrentDate(),
							mobileNo, getCurrentDate(), dbHelper.GetUserCode(),
							emailId, "", "", agentcode, "", userType, "",
							lifeAssured_Title, lifeAssured_First_Name,
							lifeAssured_Middle_Name, lifeAssured_Last_Name, obj
							.getRound(sumAssured), obj
							.getRound(premiumAmount), agentEmail,
							agentMobile, na_input, na_output, premPayingMode,
							Integer.parseInt(policyTerm), 0, productCode,
							getDate(lifeAssured_date_of_birth),
							getDate(proposer_date_of_birth), "",mode,inputVal
							.toString(), retVal.toString().replace(
							bussIll.toString(), "")));

					CreateSmartPowerInsuranceBIPdf();

					NABIObj.serviceHit(BI_SmartPowerInsuranceActivity.this,
							na_cbi_bean, newFile, needAnalysispath.getPath(),
							mypath.getPath(), name_of_person, QuatationNumber,mode);
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
					}

					else if (!cb_statement.isChecked()) {
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
						setFocusable(imageButtonSmartPowerInsuranceProposerPhotograph);
						imageButtonSmartPowerInsuranceProposerPhotograph
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

		input = inputVal.toString();
		output = retVal.toString();
		String staffdiscount = prsObj.parseXmlTag(input, "isStaff");
		premPayingMode = prsObj.parseXmlTag(input, "premFreqMode");
		policyTerm = prsObj.parseXmlTag(input, "policyTerm");
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

		age_entry = prsObj.parseXmlTag(input, "age");
		tv_bi_smart_power_insurance_age_entry.setText(age_entry + " Years");
		tv_bi_smart_power_insurance_age_entry_1.setText(age_entry + " Years");

		maturityAge = prsObj.parseXmlTag(output, "maturityAge");
		tv_bi_smart_power_insurance_maturity_age
				.setText(maturityAge + " Years");

		gender = prsObj.parseXmlTag(input, "gender");
		tv_bi_smart_power_insurance_life_assured_gender.setText(gender);

		netYield4pa = prsObj.parseXmlTag(output, "netYield4Pr");
		//tv_smart_power_insurance_net_yield4.setText(netYield4pa + " %");

		netYield8pa = prsObj.parseXmlTag(output, "netYield8Pr");
		tv_smart_power_insurance_net_yield8.setText(netYield8pa + " %");

		annPrem = prsObj.parseXmlTag(output, "annPrem");
		premiumAmount = prsObj.parseXmlTag(input, "premiumAmount");

		tv_bi_smart_power_insurance_annualised_premium.setText("Rs"
				+ obj.getRound(obj.getStringWithout_E(Double.valueOf((premiumAmount
				.equals("") || premiumAmount == null) ? "0" : premiumAmount))));

		sumAssured = prsObj.parseXmlTag(output, "sumAssured");
		increaseCoverOption = prsObj.parseXmlTag(input, "increaseCoverOption");
		if (increaseCoverOption.equals("Yes")) {
			tv_smart_power_insurance_death_benefit.setText("10) This policy provides guaranteed death benefit of Rs"
					+ obj.getRound(obj.getStringWithout_E(Double.valueOf(sumAssured
					.equals("") ? "0" : sumAssured))) + "");
		} else if (increaseCoverOption.equals("No")) {
			tv_smart_power_insurance_death_benefit.setText("10) This policy provides guaranteed death benefit of Rs"
					+ obj.getRound(obj.getStringWithout_E(Double.valueOf(sumAssured
					.equals("") ? "0" : sumAssured))));
		}

//        tv_bi_total_first_year_premium.setText("Rs. "
//                + obj.getRound(obj.getStringWithout_E(Double.valueOf((annPrem
//                .equals("") || annPrem == null) ? "0" : annPrem))));


		tv_bi_smart_power_insurance_sum_assured_main.setText("Rs. "
				+ getformatedThousandString(Integer.parseInt(obj.getRound(obj
				.getStringWithout_E(Double.valueOf(sumAssured
						.equals("") ? "0" : sumAssured))))));

		policy_Frequency = prsObj.parseXmlTag(input, "premFreqMode");
		tv_bi_smart_power_insurance_mode.setText(policy_Frequency);

		policyTerm = prsObj.parseXmlTag(input, "policyTerm");
		tv_smart_power_insurance_maturity_at.setText(policyTerm + " Years");
		tv_bi_smart_power_insurance_policy_term.setText(policyTerm + " Years");


		if (increaseCoverOption.equalsIgnoreCase("Yes")) {
			tv_bi_smart_power_insurance_increasing_cover_option
					.setText("Increasing Cover Option");
		} else {
			tv_bi_smart_power_insurance_increasing_cover_option
					.setText("Level Cover Option");
		}

		LinearLayout tv_power_insurance_smart_choice = (LinearLayout) d
				.findViewById(R.id.tv_power_insurance_smart_choice);

		LinearLayout tv_power_insurance_trigger_fund = (LinearLayout) d
				.findViewById(R.id.tv_power_insurance_trigger_fund);
		TextView tv_smart_power_insurance_bond_optimiser_fund_allocation = (TextView) d.findViewById(R.id.tv_smart_power_insurance_bond_optimiser_fund_allocation);
		TextView tv_power_insurance_corporate_fund_allocation = (TextView) d.findViewById(R.id.tv_power_insurance_corporate_fund_allocation);
		TextView tv_power_insurance_pure_fund_allocation = (TextView) d.findViewById(R.id.tv_power_insurance_pure_fund_allocation);
		TextView tv_trigger_choice_equity_fund = (TextView) d.findViewById(R.id.tv_trigger_choice_equity_fund);
		TextView tv_trigger_choice_bond_fund = (TextView) d.findViewById(R.id.tv_trigger_choice_bond_fund);


		noOfYrElapsed = prsObj.parseXmlTag(input, "noOfYrElapsed");
		tv_smart_power_insurance_no_of_years_elapsed.setText(noOfYrElapsed
				+ " Years");

		select_fund = prsObj.parseXmlTag(input, "selectFund");

		if (select_fund.equals("Trigger Fund")) {
			tv_power_insurance_trigger_fund.setVisibility(View.VISIBLE);
			tv_power_insurance_smart_choice.setVisibility(View.GONE);
			tv_trigger_choice_equity_fund.setText("80%");
			tv_trigger_choice_bond_fund.setText("20%");
			/*tv_smart_power_insurance_equity_fund_allocation.setText("80%");
			tv_smart_power_insurance_equity_optimiser_fund_allocation
					.setText("0%");
			tv_smart_power_insurance_growth_fund_allocation.setText("0%");
			tv_smart_power_insurance_balanced_fund_allocation.setText("0%");
			tv_smart_power_insurance_bond_fund_allocation.setText("20%");
			tv_smart_power_insurance_market_fund_allocation.setText("0%");
			tv_smart_power_insurance_top_300_fund_allocation.setText("0%");*/

		} else {
			tv_power_insurance_smart_choice.setVisibility(View.VISIBLE);
			tv_power_insurance_trigger_fund.setVisibility(View.GONE);

			perInvEquityFund = (prsObj.parseXmlTag(input, "perInvEquityFund"));
			perInvEquityOptFund = (prsObj.parseXmlTag(input,
					"perInvEquityOptFund"));
			perInvGrowthFund = (prsObj.parseXmlTag(input, "perInvGrowthFund"));
			perInvBalancedFund = (prsObj.parseXmlTag(input,
					"perInvBalancedFund"));
			perInvBondFund = (prsObj.parseXmlTag(input, "perInvBondFund"));
			perInvMoneyMktFund = (prsObj.parseXmlTag(input,
					"perInvMoneyMktFund"));
			perInvTop300Fund = (prsObj.parseXmlTag(input, "perInvTop300Fund"));
			perInvBondOptimiserFund = (prsObj.parseXmlTag(input, "perInvBondOptimiserFund"));
			perInvCorporateBondFund = (prsObj.parseXmlTag(input, "perInvCorporateBondFund"));
			perInvPureFund = (prsObj.parseXmlTag(input, "perInvPureFund"));

			tv_smart_power_insurance_equity_fund_allocation
					.setText((perInvEquityFund.equals("") ? "0"
							: perInvEquityFund) + " %");
			tv_smart_power_insurance_equity_optimiser_fund_allocation
					.setText((perInvEquityOptFund.equals("") ? "0"
							: perInvEquityOptFund) + " %");
			tv_smart_power_insurance_growth_fund_allocation
					.setText((perInvGrowthFund.equals("") ? "0"
							: perInvGrowthFund) + " %");
			tv_smart_power_insurance_balanced_fund_allocation
					.setText((perInvBalancedFund.equals("") ? "0"
							: perInvBalancedFund) + " %");
			tv_smart_power_insurance_bond_fund_allocation
					.setText((perInvBondFund.equals("") ? "0" : perInvBondFund)
							+ " %");
			tv_smart_power_insurance_market_fund_allocation
					.setText((perInvMoneyMktFund.equals("") ? "0"
							: perInvMoneyMktFund) + " %");
			tv_smart_power_insurance_top_300_fund_allocation
					.setText((perInvTop300Fund.equals("") ? "0"
							: perInvTop300Fund) + " %");
			tv_smart_power_insurance_bond_optimiser_fund_allocation
					.setText((perInvBondOptimiserFund.equals("") ? "0"
							: perInvBondOptimiserFund) + " %");
			tv_power_insurance_corporate_fund_allocation
					.setText((perInvCorporateBondFund.equals("") ? "0"
							: perInvCorporateBondFund) + " %");
			tv_power_insurance_pure_fund_allocation
					.setText((perInvPureFund.equals("") ? "0"
							: perInvPureFund) + " %");
		}

		String str_prem_pay = "";
		if (policy_Frequency.equalsIgnoreCase("Single")) {
			str_prem_pay = "Single";

		} else {
			str_prem_pay = "Regular";
		}

		TextView tv_Company_policy_surrender_dec = d
				.findViewById(R.id.tv_Company_policy_surrender_dec);

		sumAssuredAmount = ((int) Double.parseDouble(prsObj.parseXmlTag(
				input, "premiumAmount"))) + "";


		Company_policy_surrender_dec = "Your SBI Life Smart Power Insurance (111L090V02) is a "
				+ str_prem_pay
				+ " Premium Policy and you are required to pay "
				+ policy_Frequency
				+ " Premium of Rs "
				+ obj.getRound(obj.getStringWithout_E(Double.valueOf((sumAssuredAmount
				.equals("") || sumAssuredAmount == null) ? "0" : sumAssuredAmount)))
				+ " .Your Policy Term is "
				+ policyTerm
				+ " years"
				+ " , Premium Payment Term is same as policy term "
				+ " and  Sum Assured is Rs. "
				+

				getformatedThousandString(Integer.parseInt(obj.getRound(obj
						.getStringWithout_E(Double.valueOf(sumAssured
								.equals("") ? "0" : sumAssured)))));

		tv_Company_policy_surrender_dec.setText(Company_policy_surrender_dec);

		// tv_smart_power_insurance_equity_fund_fmc.setText("1.35 %");
		// tv_smart_power_insurance_balanced_fund_fmc.setText("1.25 %");
		// tv_smart_power_insurance_bond_fund_fmc.setText("1.00 %");

		redInYieldNoYr = prsObj.parseXmlTag(output, "redInYieldNoYr");
		tv_smart_power_insurance_reduction_yield.setText(redInYieldNoYr + " %");

		redInYieldMat = prsObj.parseXmlTag(output, "redInYieldMat");
		tv_smart_power_insurance_reduction_yeild2.setText(redInYieldMat + " %");

		for (int i = 1; i <= Integer.parseInt(policyTerm); i++) {
			String policy_year = prsObj
					.parseXmlTag(output, "policyYr" + i + "");
			String premium = prsObj.parseXmlTag(output, "AnnPrem" + i + "");

			String mortality_charge1 = prsObj.parseXmlTag(output, "MortChrg4Pr"
					+ i + "");
			String OtherCharges4Pr_PartA = prsObj.parseXmlTag(output, "OtherCharges4Pr_PartA" + i
					+ "");
			String OtherCharges4Pr_PartB = prsObj.parseXmlTag(output, "OtherCharges4Pr_PartB" + i
					+ "");
			String service_tax_on_mortality_charge1 = prsObj.parseXmlTag(
					output, "TotServTax4Pr" + i + "");

			String fund_value_at_end1 = prsObj.parseXmlTag(output,
					"FundValAtEnd4Pr" + i + "");
			String surrender_value1 = prsObj.parseXmlTag(output, "SurrVal4Pr"
					+ i + "");
			String death_benefit1 = prsObj.parseXmlTag(output, "DeathBen4Pr"
					+ i + "");


			String mortality_charge2 = prsObj.parseXmlTag(output, "MortChrg8Pr"
					+ i + "");
			String OtherCharges8Pr_PartA = prsObj.parseXmlTag(output, "OtherCharges8Pr_PartA" + i
					+ "");
			String OtherCharges8Pr_PartB = prsObj.parseXmlTag(output, "OtherCharges8Pr_PartB" + i
					+ "");
			String service_tax_on_mortality_charge2 = prsObj.parseXmlTag(
					output, "TotServTxOnCharg8Pr" + i + "");

			String fund_value_at_end2 = prsObj.parseXmlTag(output,
					"FundValAtEnd8Pr" + i + "");
			String surrender_value2 = prsObj.parseXmlTag(output, "SurrVal8Pr"
					+ i + "");
			String death_benefit2 = prsObj.parseXmlTag(output, "DeathBen8Pr"
					+ i + "");

			String premium_allocation_charge = prsObj.parseXmlTag(output,
					"PremAllCharge" + i + "");

			String annulise_premium_allocation_charge = prsObj.parseXmlTag(output,
					"AmtAviFrInv" + i + "");

			String policy_administration_charge = prsObj.parseXmlTag(output,
					"PolAdminChrg" + i + "");

			String str_AddToTheFund8Pr = prsObj.parseXmlTag(
					output, "AddToTheFund8Pr" + i + "");

			String fund_before_fmc2 = prsObj.parseXmlTag(output,
					"FundBefFMC8Pr" + i + "");
			String fund_management_charge2 = prsObj.parseXmlTag(output,
					"FundMgmtCharg8Pr" + i + "");
			String guranteed_addition2 = prsObj.parseXmlTag(output,
					"AccTPDCharg8Pr" + i + "");

			String commission = prsObj.parseXmlTag(output, "Commission" + i
					+ "");

			String str_AddToTheFund4Pr = prsObj.parseXmlTag(
					output, "AddToTheFund4Pr" + i + "");

			String fund_before_fmc1 = prsObj.parseXmlTag(output,
					"FundBefFMC4Pr" + i + "");
			String fund_management_charge1 = prsObj.parseXmlTag(output,
					"FundMgmtCharg4Pr" + i + "");
			String guranteed_addition1 = prsObj.parseXmlTag(output,
					"AccTPDCharg4Pr" + i + "");

			/*list_data.add(new M_BI_SmartPowerInsuranceAdapterCommon("", "", "", "", "", "","", "", "","", "", "","", "", ""));
			list_data1.add(new M_BI_SmartPowerInsuranceAdapterCommon2("", "", "", "", "", "","", "", "","", "", "","", "", ""));
			list_data2.add(new M_BI_SmartPowerInsuranceAdapterCommon2("", "", "", "", "", "","", "", "","", "", "","", "", ""));
*/
			list_data.add(new M_BI_SmartPowerInsuranceAdapterCommon(policy_year, premium, mortality_charge1, OtherCharges4Pr_PartA,
					service_tax_on_mortality_charge1, fund_value_at_end1, surrender_value1, death_benefit1,
					mortality_charge2, OtherCharges8Pr_PartA, service_tax_on_mortality_charge2, fund_value_at_end2,
					surrender_value2, death_benefit2, commission));


			list_data1.add(new M_BI_SmartPowerInsuranceAdapterCommon2(policy_year, premium, premium_allocation_charge,
					annulise_premium_allocation_charge, mortality_charge2, service_tax_on_mortality_charge2, policy_administration_charge,
					guranteed_addition2, OtherCharges8Pr_PartB, str_AddToTheFund8Pr, fund_before_fmc2, fund_management_charge2,
					fund_value_at_end2, surrender_value2, death_benefit2));

			list_data2.add(new M_BI_SmartPowerInsuranceAdapterCommon2(policy_year, premium, premium_allocation_charge,
					annulise_premium_allocation_charge, mortality_charge1, service_tax_on_mortality_charge1, policy_administration_charge,
					guranteed_addition1, OtherCharges4Pr_PartB, str_AddToTheFund4Pr, fund_before_fmc1, fund_management_charge1,
					fund_value_at_end1, surrender_value1, death_benefit1));



		/*for (int i = 1; i <= Integer.parseInt(policyTerm); i++) {
			String policy_year = prsObj
					.parseXmlTag(output, "policyYr" + i + "");
			String premium = prsObj.parseXmlTag(output, "AnnPrem" + i + "");
			String premium_allocation_charge = prsObj.parseXmlTag(output,
					"PremAllCharge" + i + "");
			String amount_for_investment = prsObj.parseXmlTag(output,
					"AmtAviFrInv" + i + "");
			String policy_administration_charge = prsObj.parseXmlTag(output,
					"PolAdminChrg" + i + "");

			String mortality_charge1 = prsObj.parseXmlTag(output, "MortChrg4Pr"
					+ i + "");
			String accelerated_tpd_charge1 = prsObj.parseXmlTag(output,
					"AccTPDCharg4Pr" + i + "");
			String total_charge1 = prsObj.parseXmlTag(output, "TotCharg4Pr" + i
					+ "");
			String service_tax_on_mortality_charge1 = prsObj.parseXmlTag(
					output, "TotServTax4Pr" + i + "");
			String fund_before_fmc1 = prsObj.parseXmlTag(output,
					"FundBefFMC4Pr" + i + "");
			String fund_management_charge1 = prsObj.parseXmlTag(output,
					"FundMgmtCharg4Pr" + i + "");
			String fund_value_at_end1 = prsObj.parseXmlTag(output,
					"FundValAtEnd4Pr" + i + "");
			String surrender_value1 = prsObj.parseXmlTag(output, "SurrVal4Pr"
					+ i + "");
			String death_benefit1 = prsObj.parseXmlTag(output, "DeathBen4Pr"
					+ i + "");

			String mortality_charge2 = prsObj.parseXmlTag(output, "MortChrg8Pr"
					+ i + "");
			String accelerated_tpd_charge2 = prsObj.parseXmlTag(output,
					"AccTPDCharg8Pr" + i + "");
			String total_charge2 = prsObj.parseXmlTag(output, "TotCharg8Pr" + i
					+ "");
			String service_tax_on_mortality_charge2 = prsObj.parseXmlTag(
					output, "TotServTxOnCharg8Pr" + i + "");
			String fund_before_fmc2 = prsObj.parseXmlTag(output,
					"FundBefFMC8Pr" + i + "");
			String fund_management_charge2 = prsObj.parseXmlTag(output,
					"FundMgmtCharg8Pr" + i + "");
			String fund_value_at_end2 = prsObj.parseXmlTag(output,
					"FundValAtEnd8Pr" + i + "");
			String surrender_value2 = prsObj.parseXmlTag(output, "SurrVal8Pr"
					+ i + "");
			String death_benefit2 = prsObj.parseXmlTag(output, "DeathBen8Pr"
					+ i + "");

			String commission = prsObj.parseXmlTag(output, "Commission" + i
					+ "");

			*/

		}

		// tv_smart_power_insurance_death_benefit.setText("Rs" + )




		policyTerm = policyTerm.replaceAll("\\s+", "");
		Adapter_BI_SmartPowerInsuranceGridCommon adapter = new Adapter_BI_SmartPowerInsuranceGridCommon(
				BI_SmartPowerInsuranceActivity.this, list_data);
		gv_userinfo.setAdapter(adapter);
		Adapter_BI_SmartPowerInsuranceGridCommon2 adapter1 = new Adapter_BI_SmartPowerInsuranceGridCommon2(
				BI_SmartPowerInsuranceActivity.this, list_data1);
		gv_userinfo2.setAdapter(adapter1);

		Adapter_BI_SmartPowerInsuranceGridCommon2 adapter2 = new Adapter_BI_SmartPowerInsuranceGridCommon2(
				BI_SmartPowerInsuranceActivity.this, list_data2);
		gv_userinfo3.setAdapter(adapter2);


		GridHeight gh =new GridHeight();
		gh.getheight(gv_userinfo,policyTerm);
		gh.getheight(gv_userinfo2, policyTerm);
		gh.getheight(gv_userinfo3, policyTerm);


		d.show();
	}

	private void CreateSmartPowerInsuranceBIPdf() {

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
			// File mypath = new File(folder, PropserNumber +
			// "Proposalno_p02.pdf");
			mypath = mStorageUtils.createFileToAppSpecificDir(context, QuatationNumber + "BI.pdf");
			needAnalysispath = mStorageUtils.createFileToAppSpecificDir(context, "NA.pdf");
			// needAnalysis_Tabular_path = new File(folder, "NA_Tabular.pdf");
			newFile = mStorageUtils.createFileToAppSpecificDir(context, QuatationNumber + "P01.pdf");

			Rectangle rect = new Rectangle(594f, 792f);

			Document document = new Document(rect, 50, 50, 50, 50);
			// Document document = new Document(rect, 50, 50, 50, 50);
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
							"IN THIS POLICY, THE INVESTMENT RISK IN INVESTMENT PORTFOLIO IS BORNE BY THE POLICYHOLDER.",
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
					small_bold_for_name);
			para_address.setAlignment(Element.ALIGN_CENTER);
			Paragraph para_address1 = new Paragraph(
					"Corporate Office: 'Natraj', M.V.Road and Western Express Highway Junction, Andheri (East), Mumbai - 400069",
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
			Paragraph para_address4 = new Paragraph(
					"Customised Benefit Illustration (CBI)",
					small_bold);
			para_address4.setAlignment(Element.ALIGN_CENTER);
			Paragraph para_address5 = new Paragraph(
					"SBI Life - Smart Power Insurance (111L090V02)",
					small_bold);
			para_address5.setAlignment(Element.ALIGN_CENTER);
			Paragraph para_address6 = new Paragraph(
					"An Individual, Unit-linked, Non-Participating, Life Insurance Product",
					small_bold);
			para_address6.setAlignment(Element.ALIGN_CENTER);

			document.add(para_address);
			document.add(para_address1);
			document.add(para_address2);
			document.add(para_address3);
			document.add(para_address4);
			document.add(para_address5);
			document.add(para_address6);
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
					"Proposal No.:", small_normal));
			PdfPCell ProposalNumber_cell_2 = new PdfPCell(new Paragraph(
					QuatationNumber, small_bold1));
			ProposalNumber_cell_2.setHorizontalAlignment(Element.ALIGN_CENTER);
			PdfPCell NameofProposal_cell_3 = new PdfPCell(new Paragraph(
					"Proposer Name ", small_normal));
			PdfPCell NameofProposal_cell_4 = new PdfPCell(new Paragraph(
					name_of_proposer, small_bold1));
			NameofProposal_cell_4.setHorizontalAlignment(Element.ALIGN_CENTER);

			NameofProposal_cell_4.setVerticalAlignment(Element.ALIGN_CENTER);
			PdfPCell ProposalNumber_cell_5 = new PdfPCell(new Paragraph(
					"Channel / Intermediary :", small_normal));
			PdfPCell ProposalNumber_cell_6 = new PdfPCell(new Paragraph(
					userType, small_bold1));
			ProposalNumber_cell_6.setHorizontalAlignment(Element.ALIGN_CENTER);

			ProposalNumber_cell_1.setPadding(5);
			ProposalNumber_cell_2.setPadding(5);
			NameofProposal_cell_3.setPadding(5);
			NameofProposal_cell_4.setPadding(5);

			table_proposer_name.addCell(ProposalNumber_cell_1);
			table_proposer_name.addCell(ProposalNumber_cell_2);
			table_proposer_name.addCell(ProposalNumber_cell_5);
			table_proposer_name.addCell(ProposalNumber_cell_6);
			//table_proposer_name.addCell(NameofProposal_cell_3);
			//table_proposer_name.addCell(NameofProposal_cell_4);
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
			//document.add(BI_Pdftable_Introdcution);

			PdfPTable BI_Pdftable2 = new PdfPTable(1);
			BI_Pdftable2.setWidthPercentage(100);
			// CR_table6.setWidths(columnWidths_2);
			PdfPCell BI_Pdftable2_cell1 = new PdfPCell(
					new Paragraph(
							"Insurance Regulatory and Development authority of India (IRDAI) requires all life insurance companies operating in India to provide official illustrations to their customers. The illustrations are based on the investment rates of return set by the Insurance Regulatory and Development Authority of India (Unit Linked Insurance Products) Regulations, 2019 and is not intended to reflect the actual investment returns achieved or which may be achieved in future by SBI Life Insurance Company Limited.",
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
					new Paragraph("Some benefits are guaranteed and some benefits are variable with returns based on the future fund performance of SBI Life Insurance Company Limited. If your policy offers guaranteed returns then the same will be clearly marked as “guaranteed” in the illustration table. If your policy offers variable returns then the illustration will show two different rates of assumed future investment returns. These assumed rates of return are not guaranteed and they are not the upper or lower limits of what you might get back, as the value of your policy is dependent on a number of factors including future fund investment performance.",
							small_normal));

			BI_Pdftable4_cell1.setPadding(5);

			BI_Pdftable4.addCell(BI_Pdftable4_cell1);
			document.add(BI_Pdftable4);
			document.add(para_img_logo_after_space_1);

			PdfPTable BI_PdftablePlanDetails = new PdfPTable(1);
			BI_PdftablePlanDetails.setWidthPercentage(100);
			PdfPCell BI_PdftablePlanDetails_cell = new PdfPCell(new Paragraph(
					"Proposer, Life Assured and Plan Details", small_bold));

			BI_PdftablePlanDetails_cell
					.setBackgroundColor(BaseColor.LIGHT_GRAY);

			BI_PdftablePlanDetails_cell
					.setHorizontalAlignment(Element.ALIGN_CENTER);
			BI_PdftablePlanDetails_cell.setPadding(5);

			BI_PdftablePlanDetails.addCell(BI_PdftablePlanDetails_cell);
			document.add(BI_PdftablePlanDetails);

			PdfPTable table_lifeAssuredName = new PdfPTable(4);
			table_lifeAssuredName.setWidthPercentage(100);

			PdfPCell cell_LifeAssuredName12 = new PdfPCell(new Paragraph(
					"Name of Proposer", small_normal));
			cell_LifeAssuredName12.setPadding(5);
			PdfPCell cell_lLifeAssuredName22 = new PdfPCell(new Paragraph(
					name_of_life_assured, small_bold));
			cell_lLifeAssuredName22.setPadding(5);
			cell_lLifeAssuredName22.setHorizontalAlignment(Element.ALIGN_CENTER);

			PdfPCell cell_lifeAssuredAge12 = new PdfPCell(new Paragraph(
					"Age of Proposer ", small_normal));
			cell_lifeAssuredAge12.setPadding(5);
			PdfPCell cell_lifeAssuredAge22 = new PdfPCell(new Paragraph(
					age_entry + " Years", small_bold));
			cell_lifeAssuredAge22.setPadding(5);
			cell_lifeAssuredAge22.setHorizontalAlignment(Element.ALIGN_CENTER);

			PdfPCell cell_LifeAssuredName1 = new PdfPCell(new Paragraph(
					"Name of the Life Assured", small_normal));
			cell_LifeAssuredName1.setPadding(5);
			PdfPCell cell_lLifeAssuredName2 = new PdfPCell(new Paragraph(
					name_of_life_assured, small_bold));
			cell_lLifeAssuredName2.setPadding(5);
			cell_lLifeAssuredName2.setHorizontalAlignment(Element.ALIGN_CENTER);

			PdfPCell cell_lifeAssuredAge1 = new PdfPCell(new Paragraph(
					"Life Assured  Age", small_normal));
			cell_lifeAssuredAge1.setPadding(5);
			PdfPCell cell_lifeAssuredAge2 = new PdfPCell(new Paragraph(
					age_entry + " Years", small_bold));
			cell_lifeAssuredAge2.setPadding(5);
			cell_lifeAssuredAge2.setHorizontalAlignment(Element.ALIGN_CENTER);

			PdfPCell cell_termsofPolicy1 = new PdfPCell(new Paragraph(
					"Policy Term " + "  ", small_normal));
			cell_termsofPolicy1.setPadding(5);
			PdfPCell cell_termsofPolicy2 = new PdfPCell(new Paragraph("  "
					+ policyTerm + "", small_bold));
			cell_termsofPolicy2.setPadding(5);
			cell_termsofPolicy2.setHorizontalAlignment(Element.ALIGN_CENTER);


			PdfPCell premium_payment_term1 = new PdfPCell(new Paragraph(
					"Premium Payment Term ", small_normal));
			premium_payment_term1.setPadding(5);
			PdfPCell premium_payment_term2 = new PdfPCell(new Paragraph(" Same as Policy Term", small_bold));
			premium_payment_term2.setPadding(5);
			premium_payment_term2
					.setHorizontalAlignment(Element.ALIGN_CENTER);

			PdfPCell cell_monthly_prem1 = new PdfPCell(new Paragraph("Amount of Installment Premium ", small_normal));
			cell_monthly_prem1.setPadding(5);
			PdfPCell cell_monthly_prem2 = new PdfPCell(new Paragraph("" + currencyFormat.format(Double.parseDouble(premiumAmount)), small_bold));
			cell_monthly_prem2.setPadding(5);
			cell_monthly_prem2.setHorizontalAlignment(Element.ALIGN_CENTER);

			PdfPCell cell_lifeAssured_Premium_frequeny1 = new PdfPCell(
					new Paragraph("Mode / Frequency of Premium Payment", small_normal));
			cell_lifeAssured_Premium_frequeny1.setPadding(5);
			PdfPCell cell_lifeAssured_Premium_frequeny2 = new PdfPCell(
					new Paragraph(policy_Frequency, small_bold));
			cell_lifeAssured_Premium_frequeny2.setPadding(5);
			cell_lifeAssured_Premium_frequeny2
					.setHorizontalAlignment(Element.ALIGN_CENTER);


			table_lifeAssuredName.addCell(cell_LifeAssuredName12);
			table_lifeAssuredName.addCell(cell_lLifeAssuredName22);
			table_lifeAssuredName.addCell(cell_lifeAssuredAge12);
			table_lifeAssuredName.addCell(cell_lifeAssuredAge22);

			table_lifeAssuredName.addCell(cell_LifeAssuredName1);
			table_lifeAssuredName.addCell(cell_lLifeAssuredName2);
			table_lifeAssuredName.addCell(cell_lifeAssuredAge1);
			table_lifeAssuredName.addCell(cell_lifeAssuredAge2);
			table_lifeAssuredName.addCell(cell_termsofPolicy1);
			table_lifeAssuredName.addCell(cell_termsofPolicy2);

			table_lifeAssuredName.addCell(premium_payment_term1);
			table_lifeAssuredName.addCell(premium_payment_term2);

			table_lifeAssuredName.addCell(cell_monthly_prem1);
			table_lifeAssuredName.addCell(cell_monthly_prem2);
			table_lifeAssuredName.addCell(cell_lifeAssured_Premium_frequeny1);
			table_lifeAssuredName.addCell(cell_lifeAssured_Premium_frequeny2);
			document.add(table_lifeAssuredName);

			PdfPTable table_lifeAssuredDetails = new PdfPTable(2);
			table_lifeAssuredDetails.setWidthPercentage(100);

			PdfPCell sumAssured1 = new PdfPCell(
					new Paragraph("Sum Assured", small_normal));
			sumAssured1.setPadding(5);
			PdfPCell sumAssured2 = new PdfPCell(
					new Paragraph(currencyFormat.format(Double.parseDouble(sumAssured)), small_bold));
			sumAssured2.setPadding(5);
			sumAssured2
					.setHorizontalAlignment(Element.ALIGN_CENTER);

			PdfPCell rate = new PdfPCell(new Paragraph(
					"Rate of Applicable Taxes", small_bold));

			PdfPCell rate2 = new PdfPCell(new Paragraph(
					"18%", small_bold));
			rate2.setHorizontalAlignment(Element.ALIGN_CENTER);
			PdfPCell cell_lifeAssuredAmaturityGender1 = new PdfPCell(
					new Paragraph("Life Assured Gender", small_normal));
			cell_lifeAssuredAmaturityGender1.setPadding(5);
			PdfPCell cell_lifeAssuredAmaturityGender2 = new PdfPCell(
					new Paragraph(gender, small_bold));
			cell_lifeAssuredAmaturityGender2.setPadding(5);
			cell_lifeAssuredAmaturityGender2
					.setHorizontalAlignment(Element.ALIGN_CENTER);

			table_lifeAssuredDetails.addCell(sumAssured1);
			table_lifeAssuredDetails.addCell(sumAssured2);
			table_lifeAssuredDetails.addCell(rate);
			table_lifeAssuredDetails.addCell(rate2);

			//table_lifeAssuredDetails.addCell(cell_lifeAssuredAmaturityGender1);
			//table_lifeAssuredDetails.addCell(cell_lifeAssuredAmaturityGender2);
			//table_lifeAssuredDetails
			//		.addCell(cell_lifeAssured_Premium_frequeny1);
			//table_lifeAssuredDetails
			//		.addCell(cell_lifeAssured_Premium_frequeny2);
			document.add(table_lifeAssuredDetails);

			/*String isStaff = "";
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

			}*/

			String isJK = "";
			if (cb_bi_smart_power_insurance_JKResident.isChecked()) {
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
			input_table.setWidths(new float[] { 3f, 2f });
			input_table.setWidthPercentage(80f);
			input_table.setHorizontalAlignment(Element.ALIGN_LEFT);

			// 1st row
			cell = new PdfPCell(new Phrase("Life Assured Age at Entry : ",
					small_normal));
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
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
			cell = new PdfPCell(new Phrase("Regular Premium (RP)* : ",
					small_normal));
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

			// 7 row
			cell = new PdfPCell(new Phrase("Sum Assured Option : ",
					small_normal));
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			input_table.addCell(cell);
			if (increaseCoverOption.equalsIgnoreCase("Yes"))
				cell = new PdfPCell(new Phrase("Increasing Cover Option",
						small_normal));
			else
				cell = new PdfPCell(new Phrase("Level Cover Option",
						small_normal));
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			input_table.addCell(cell);

			// 8 row
			cell = new PdfPCell(new Phrase("Mode : ", small_normal));
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			input_table.addCell(cell);

			cell = new PdfPCell(new Phrase(policy_Frequency, small_normal));
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			input_table.addCell(cell);

			// fund table here -3

			PdfPTable fund_table = new PdfPTable(4);
			fund_table.setWidths(new float[]{3f, 1.9f, 1f, 2f});
			fund_table.setWidthPercentage(80f);
			fund_table.setHorizontalAlignment(Element.ALIGN_LEFT);
			if (select_fund.equals("Trigger Fund")) {


				// 1st row
				cell = new PdfPCell(new Phrase(
						"Investment Strategy Opted For ", small_bold));
				cell.setColspan(2);

				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				fund_table.addCell(cell);

				cell = new PdfPCell(new Phrase(
						"Trigger Fund Strategy", small_bold));
				cell.setColspan(2);

				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				fund_table.addCell(cell);

				// 2 row
				cell = new PdfPCell(new Phrase("Fund Name (SFIN Name)", small_normal));

				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				fund_table.addCell(cell);
				cell = new PdfPCell(new Phrase("% Allocation", small_normal));

				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				fund_table.addCell(cell);
				cell = new PdfPCell(new Phrase("FMC", small_normal));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				fund_table.addCell(cell);
				cell = new PdfPCell(new Phrase("Risk Level", small_normal));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				fund_table.addCell(cell);

				// 3 row
				cell = new PdfPCell(new Phrase("Equity Fund (SFIN: ULIF001100105EQUITY-FND111)", small_normal));
				cell.setHorizontalAlignment(Element.ALIGN_LEFT);
				fund_table.addCell(cell);

				cell = new PdfPCell(new Phrase("80%", small_normal));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				fund_table.addCell(cell);
				cell = new PdfPCell(new Phrase("1.35%", small_normal));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				fund_table.addCell(cell);
				cell = new PdfPCell(new Phrase("High", small_normal));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				fund_table.addCell(cell);

				// 4 row
				cell = new PdfPCell(new Phrase("Bond Fund (SFIN: ULIF002100105BONDULPFND111)", small_normal));
				cell.setHorizontalAlignment(Element.ALIGN_LEFT);
				fund_table.addCell(cell);
				cell = new PdfPCell(new Phrase("20%", small_normal));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				fund_table.addCell(cell);
				cell = new PdfPCell(new Phrase("1.00%", small_normal));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				fund_table.addCell(cell);
				cell = new PdfPCell(new Phrase("Low to Medium", small_normal));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				fund_table.addCell(cell);
			} else {

				cell = new PdfPCell(new Phrase(
						"Investment Strategy Opted For ", small_bold));
				cell.setColspan(2);

				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				fund_table.addCell(cell);


				cell = new PdfPCell(new Phrase(
						"Smart Funds Option", small_bold));
				cell.setColspan(2);

				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				fund_table.addCell(cell);
				// 2 row
				cell = new PdfPCell(new Phrase("Fund Name (SFIN Name)", small_normal));

				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				fund_table.addCell(cell);
				cell = new PdfPCell(new Phrase("% Allocation", small_normal));

				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				fund_table.addCell(cell);
				cell = new PdfPCell(new Phrase("FMC", small_normal));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				fund_table.addCell(cell);
				cell = new PdfPCell(new Phrase("Risk Level", small_normal));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				fund_table.addCell(cell);

				// 3 row
				cell = new PdfPCell(new Phrase("Equity Fund (SFIN: ULIF001100105EQUITY-FND111)", small_normal));
				cell.setHorizontalAlignment(Element.ALIGN_LEFT);
				fund_table.addCell(cell);

				if (perInvEquityFund.equalsIgnoreCase("")) {

					perInvEquityFund = "0";
				}

				cell = new PdfPCell(new Phrase(perInvEquityFund + "%", small_normal));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				fund_table.addCell(cell);
				cell = new PdfPCell(new Phrase("1.35%", small_normal));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				fund_table.addCell(cell);
				cell = new PdfPCell(new Phrase("High", small_normal));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				fund_table.addCell(cell);

				cell = new PdfPCell(new Phrase("Equity Optimizer Fund (SFIN: ULIF010210108EQTYOPTFND111)",
						small_normal));
				cell.setHorizontalAlignment(Element.ALIGN_LEFT);
				fund_table.addCell(cell);

				if (perInvEquityOptFund.equalsIgnoreCase("")) {

					perInvEquityOptFund = "0";
				}
				cell = new PdfPCell(new Phrase(perInvEquityOptFund + "%",
						small_normal));

				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				fund_table.addCell(cell);
				cell = new PdfPCell(new Phrase("1.35%", small_normal));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				fund_table.addCell(cell);
				cell = new PdfPCell(new Phrase("High", small_normal));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				fund_table.addCell(cell);
				if (perInvGrowthFund.equalsIgnoreCase("")) {

					perInvGrowthFund = "0";
				}
				cell = new PdfPCell(new Phrase("Growth Fund (SFIN: ULIF003241105GROWTH-FND111)", small_normal));
				cell.setHorizontalAlignment(Element.ALIGN_LEFT);
				fund_table.addCell(cell);

				cell = new PdfPCell(new Phrase(perInvGrowthFund + "%",
						small_normal));

				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				fund_table.addCell(cell);
				cell = new PdfPCell(new Phrase("1.35%", small_normal));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				fund_table.addCell(cell);
				cell = new PdfPCell(new Phrase("Medium to High", small_normal));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				fund_table.addCell(cell);

				cell = new PdfPCell(new Phrase("Balanced Fund (SFIN: ULIF004051205BALANCDFND111)", small_normal));
				cell.setHorizontalAlignment(Element.ALIGN_LEFT);
				fund_table.addCell(cell);

				if (perInvBalancedFund.equalsIgnoreCase("")) {

					perInvBalancedFund = "0";
				}
				cell = new PdfPCell(new Phrase(perInvBalancedFund + "%",
						small_normal));

				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				fund_table.addCell(cell);
				cell = new PdfPCell(new Phrase("1.25%", small_normal));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				fund_table.addCell(cell);
				cell = new PdfPCell(new Phrase("Medium", small_normal));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				fund_table.addCell(cell);

				cell = new PdfPCell(new Phrase("Bond Fund (SFIN: ULIF002100105BONDULPFND111)", small_normal));
				cell.setHorizontalAlignment(Element.ALIGN_LEFT);
				fund_table.addCell(cell);
				if (perInvBondFund.equalsIgnoreCase("")) {

					perInvBondFund = "0";
				}
				cell = new PdfPCell(new Phrase(perInvBondFund + "%", small_normal));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				fund_table.addCell(cell);
				cell = new PdfPCell(new Phrase("1.00%", small_normal));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				fund_table.addCell(cell);
				cell = new PdfPCell(new Phrase("Low to Medium", small_normal));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				fund_table.addCell(cell);

				cell = new PdfPCell(new Phrase("Money Market Fund (SFIN: ULIF005010206MONYMKTFND111)", small_normal));
				cell.setHorizontalAlignment(Element.ALIGN_LEFT);
				fund_table.addCell(cell);

				if (perInvMoneyMktFund.equalsIgnoreCase("")) {

					perInvMoneyMktFund = "0";
				}
				cell = new PdfPCell(new Phrase(perInvMoneyMktFund + "%",
						small_normal));

				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				fund_table.addCell(cell);
				cell = new PdfPCell(new Phrase("0.25%", small_normal));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				fund_table.addCell(cell);
				cell = new PdfPCell(new Phrase("Low", small_normal));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				fund_table.addCell(cell);

				// 5 row
				cell = new PdfPCell(new Phrase("Top 300 Fund (SFIN: ULIF016070110TOP300-FND111)", small_normal));
				cell.setHorizontalAlignment(Element.ALIGN_LEFT);
				fund_table.addCell(cell);
				if (perInvTop300Fund.equalsIgnoreCase("")) {

					perInvTop300Fund = "0";
				}
				cell = new PdfPCell(new Phrase(perInvTop300Fund + "%",
						small_normal));

				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				fund_table.addCell(cell);
				cell = new PdfPCell(new Phrase("1.35%", small_normal));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				fund_table.addCell(cell);
				cell = new PdfPCell(new Phrase("High", small_normal));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				fund_table.addCell(cell);

				cell = new PdfPCell(new Phrase("Bond Optimiser Fund (SFIN : ULIF032290618BONDOPTFND111)", small_normal));
				cell.setHorizontalAlignment(Element.ALIGN_LEFT);
				fund_table.addCell(cell);
				if (perInvBondOptimiserFund.equalsIgnoreCase("")) {

					perInvBondOptimiserFund = "0";
				}
				cell = new PdfPCell(new Phrase(perInvBondOptimiserFund + "%",
						small_normal));

				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				fund_table.addCell(cell);
				cell = new PdfPCell(new Phrase("1.15%", small_normal));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				fund_table.addCell(cell);
				cell = new PdfPCell(new Phrase("Low to Medium", small_normal));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				fund_table.addCell(cell);


				cell = new PdfPCell(new Phrase("Pure Fund (SFIN : ULIF030290915PUREULPFND111)", small_normal));
				cell.setHorizontalAlignment(Element.ALIGN_LEFT);
				fund_table.addCell(cell);
				if (perInvPureFund.equalsIgnoreCase("")) {

					perInvPureFund = "0";
				}
				cell = new PdfPCell(new Phrase(perInvPureFund + "%",
						small_normal));

				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				fund_table.addCell(cell);
				cell = new PdfPCell(new Phrase("1.35%", small_normal));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				fund_table.addCell(cell);
				cell = new PdfPCell(new Phrase("High", small_normal));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				fund_table.addCell(cell);


				cell = new PdfPCell(new Phrase("Corporate Bond Fund (SFIN : ULIF033290618CORBONDFND111)", small_normal));
				cell.setHorizontalAlignment(Element.ALIGN_LEFT);
				fund_table.addCell(cell);
				if (perInvCorporateBondFund.equalsIgnoreCase("")) {

					perInvCorporateBondFund = "0";
				}
				cell = new PdfPCell(new Phrase(perInvCorporateBondFund + "%",
						small_normal));

				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				fund_table.addCell(cell);
				cell = new PdfPCell(new Phrase("1.15%", small_normal));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				fund_table.addCell(cell);
				cell = new PdfPCell(new Phrase("Low to Medium", small_normal));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				fund_table.addCell(cell);

			}
			// first year premium Table here -2

			PdfPTable FyPrem_table = new PdfPTable(2);
			FyPrem_table.setWidths(new float[] { 3.2f, 2.5f });
			FyPrem_table.setWidthPercentage(80f);
			FyPrem_table.setHorizontalAlignment(Element.ALIGN_CENTER);

			// 1st row
			cell = new PdfPCell(new Phrase("Total First Year Premium (RP) : ",
					small_normal));
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			FyPrem_table.addCell(cell);
			cell = new PdfPCell(new Phrase("Rs. "
					+ currencyFormat.format(Double.parseDouble(annPrem)),
					small_normal));
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			FyPrem_table.addCell(cell);

			// reduction in yeild for elapsed year since inception Table here
			// -4a

			PdfPTable reductionInYeild_table = new PdfPTable(2);
			reductionInYeild_table.setWidths(new float[] { 3.5f, 2f });
			reductionInYeild_table.setWidthPercentage(80f);
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
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			reductionInYeild_table.addCell(cell);

			// reduction in yeild for maturity Table here -4b

			PdfPTable reductionInYeild_maturity_table = new PdfPTable(2);
			reductionInYeild_maturity_table.setWidths(new float[] { 3.5f, 2f });
			reductionInYeild_maturity_table.setWidthPercentage(80f);
			reductionInYeild_maturity_table
					.setHorizontalAlignment(Element.ALIGN_RIGHT);

			// 1st row
			cell = new PdfPCell(new Phrase("Maturity at", small_normal));
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			reductionInYeild_maturity_table.addCell(cell);
			cell = new PdfPCell(new Phrase("Reduction in Yeild @ 8%",
					small_normal));
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			reductionInYeild_maturity_table.addCell(cell);

			// 2 row
			cell = new PdfPCell(new Phrase("" + policyTerm, small_normal));
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			reductionInYeild_maturity_table.addCell(cell);
			cell = new PdfPCell(new Phrase(redInYieldMat + "%", small_normal));
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			reductionInYeild_maturity_table.addCell(cell);

			// main table of 4 tables
			PdfPTable main_table = new PdfPTable(1);
			//main_table.setWidths(new float[] { 4f, 2.8f, 5f, 3.4f });
			main_table.setWidthPercentage(100);
			main_table.getDefaultCell().setPadding(20f);

			//cell = new PdfPCell(input_table);
			//cell.setRowspan(4);
			//cell.setBorder(Rectangle.NO_BORDER);
			//cell.setVerticalAlignment(Element.ALIGN_TOP);
			//main_table.addCell(cell);

			//cell = new PdfPCell(FyPrem_table);
			//cell.setRowspan(4);
			//cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			//cell.setBorder(Rectangle.NO_BORDER);

			//main_table.addCell(cell);

			cell = new PdfPCell(fund_table);
			cell.setRowspan(4);
			cell.setBorder(Rectangle.NO_BORDER);
			cell.setVerticalAlignment(Element.ALIGN_TOP);
			main_table.addCell(cell);

			/*cell = new PdfPCell(new Phrase("\n"));
			cell.setBorder(Rectangle.NO_BORDER);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			main_table.addCell(cell);
			cell = new PdfPCell(reductionInYeild_table);
			cell.setBorder(Rectangle.NO_BORDER);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			main_table.addCell(cell);
			cell = new PdfPCell(reductionInYeild_maturity_table);
			cell.setBorder(Rectangle.NO_BORDER);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			main_table.addCell(cell);
			cell = new PdfPCell(new Phrase("\n"));
			cell.setBorder(Rectangle.NO_BORDER);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			main_table.addCell(cell);*/

			// Table here


			Paragraph base_para = new Paragraph(" *It is a base premium ",
					small_normal);
			base_para.setAlignment(Element.ALIGN_LEFT);

			Paragraph note = new Paragraph("Notes :", small_bold);
			note.setAlignment(Element.ALIGN_LEFT);
			// notes Table here

			PdfPTable table2 = new PdfPTable(2);
			table2.setWidths(new float[] { 0.3f, 9f });
			table2.setWidthPercentage(100);
			table2.setHorizontalAlignment(Element.ALIGN_LEFT);

			// 1st note
			cell = new PdfPCell(new Phrase("", small_normal));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setBorder(Rectangle.NO_BORDER);
			table2.addCell(cell);
			cell = new PdfPCell(
					new Phrase(
							"1). Refer the sales literature for explanation of terms used in this illustration",
							small_normal));
			cell.setHorizontalAlignment(Element.ALIGN_LEFT
					| Element.ALIGN_JUSTIFIED);
			cell.setBorder(Rectangle.NO_BORDER);
			table2.addCell(cell);

			// 2 note
			cell = new PdfPCell(new Phrase("", small_normal));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setBorder(Rectangle.NO_BORDER);
			table2.addCell(cell);
			cell = new PdfPCell(
					new Phrase(
							"2) Please read this benefit illustration in conjunction with Sales Brochure and the Policy Document to understand all Terms, Conditions & Exclusions carefully.",
							small_normal));
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell.setBorder(Rectangle.NO_BORDER);
			table2.addCell(cell);

			// 3 note
			cell = new PdfPCell(new Phrase("", small_normal));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setBorder(Rectangle.NO_BORDER);
			table2.addCell(cell);
			cell = new PdfPCell(
					new Phrase(
							"3) Kindly note that above is only an illustration and does not in any way create any rights and/or obligations. The actual experience on the contract may be different from what is illustrated. The non-guaranteed low and high rate mentioned above relate to assumed investment returns at different rates and may vary depending upon market conditions. For more details on risk factors, terms and conditions please read sales brochure carefully.",
							small_normal));
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell.setBorder(Rectangle.NO_BORDER);
			table2.addCell(cell);
			// 1st note
			cell = new PdfPCell(new Phrase("", small_normal));
			cell.setBorder(Rectangle.NO_BORDER);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table2.addCell(cell);
			cell = new PdfPCell(
					new Phrase(
							"4) The unit values may go up as well as down and past performance is no indication of future performance on the part of SBI Life Insurance Co. Ltd. We would request you to appreciate the associated risk under this plan vis-à-vis the likely future returns before taking your investment decision.",
							small_normal));
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell.setBorder(Rectangle.NO_BORDER);
			table2.addCell(cell);
			// 1st note
			cell = new PdfPCell(new Phrase("", small_normal));
			cell.setBorder(Rectangle.NO_BORDER);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table2.addCell(cell);
			cell = new PdfPCell(
					new Phrase(
							"5) It is assumed that the policy is in force throughout the term.",
							small_normal));
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell.setBorder(Rectangle.NO_BORDER);
			table2.addCell(cell);
			// 1st note
			cell = new PdfPCell(new Phrase("", small_normal));
			cell.setBorder(Rectangle.NO_BORDER);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table2.addCell(cell);
			cell = new PdfPCell(
					new Phrase(
							"6) Fund management charge is based on the specific  investment strategy / fund option(s) chosen.",
							small_normal));
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell.setBorder(Rectangle.NO_BORDER);
			table2.addCell(cell);

			// 1st note
			cell = new PdfPCell(new Phrase("", small_normal));
			cell.setBorder(Rectangle.NO_BORDER);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table2.addCell(cell);
			cell = new PdfPCell(
					new Phrase(
							"7) Surrender Value equals the Fund Value at the end of the year minus Discontinuance Charges. Surrender value is available on or after 5th policy anniversary.",
							small_normal));
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell.setBorder(Rectangle.NO_BORDER);
			table2.addCell(cell);

			// 1st note
			cell = new PdfPCell(new Phrase("", small_normal));
			cell.setBorder(Rectangle.NO_BORDER);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table2.addCell(cell);
			cell = new PdfPCell(
					new Phrase(
							"8) Acceptance of proposal is subject to Underwriting decision. Mortality charges are for a healthy person.",
							small_normal));
			cell.setHorizontalAlignment(Element.ALIGN_LEFT
					| Element.ALIGN_JUSTIFIED);
			cell.setBorder(Rectangle.NO_BORDER);
			table2.addCell(cell);

			// 1st note
			cell = new PdfPCell(new Phrase("", small_normal));
			cell.setBorder(Rectangle.NO_BORDER);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table2.addCell(cell);
			cell = new PdfPCell(
					new Phrase(
							"9) Applicable Taxes (including surcharge/cess etc), at the rate notified by the Central Government/ State Government / Union Territories of India from time to time and as per the provisions of the prevalent tax laws will be payable on premium/ or any other charges as per the product features.",
							small_normal));
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell.setBorder(Rectangle.NO_BORDER);
			table2.addCell(cell);

			// 1st note
			cell = new PdfPCell(new Phrase("", small_normal));
			cell.setBorder(Rectangle.NO_BORDER);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table2.addCell(cell);
			if (increaseCoverOption.equals("Yes"))
				cell = new PdfPCell(
						new Phrase(
								"10)This policy provides guaranteed death benefit of Rs "
										+ currencyFormat.format(Double
										.parseDouble(sumAssured))
										+ "",
								small_normal));
			else
				cell = new PdfPCell(new Phrase(
						"10)This policy provides guaranteed death benefit of Rs "
								+ currencyFormat.format(Double
								.parseDouble(sumAssured)) + ".",
						small_normal));
			cell.setBorder(Rectangle.NO_BORDER);
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			table2.addCell(cell);

			// 1st note
			cell = new PdfPCell(new Phrase("", small_normal));
			cell.setBorder(Rectangle.NO_BORDER);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table2.addCell(cell);
			cell = new PdfPCell(
					new Phrase(
							"11) Net Yield have been calculated after applying all the charges (except GST, mortality charges).",
							small_normal));
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell.setBorder(Rectangle.NO_BORDER);
			table2.addCell(cell);

			// 1st note
			cell = new PdfPCell(new Phrase("12)", small_normal));
			cell.setBorder(Rectangle.NO_BORDER);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			//table2.addCell(cell);
			cell = new PdfPCell(
					new Phrase(
							"This illustration has been prepared in compliance with IRDAI (Linked Insurance Products) Regulations, 2013.",
							small_normal));
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell.setBorder(Rectangle.NO_BORDER);
			//table2.addCell(cell);

			// 1st note
			cell = new PdfPCell(new Phrase("13)", small_normal));
			cell.setBorder(Rectangle.NO_BORDER);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			//table2.addCell(cell);
			cell = new PdfPCell(
					new Phrase(
							"Col (24) gives the commission payable to the agent/ broker in respect of the base policy .",
							small_normal));
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell.setBorder(Rectangle.NO_BORDER);
			//table2.addCell(cell);

			PdfPTable table3 = new PdfPTable(1);
			//table3.setWidths(new float[] { 0.5f, 3f, 9f });
			table3.setWidthPercentage(80);
			table3.setHorizontalAlignment(Element.ALIGN_LEFT);

			cell = new PdfPCell(new Phrase("Definition of Various Charges:",
					small_bold));
			cell.setColspan(1);
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell.setBorder(Rectangle.NO_BORDER);
			table3.addCell(cell);
			cell = new PdfPCell(new Phrase("", small_normal));
			cell.setRowspan(1);
			cell.setBorder(Rectangle.NO_BORDER);
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			table3.addCell(cell);

			// charges row
			cell = new PdfPCell(new Phrase("",
					small_normal));
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			//table3.addCell(cell);
			cell = new PdfPCell(
					new Phrase(
							"1)Policy Administration Charges : a charge of a fixed sum which is applied at the beginning of each policy month by cancelling units for equivalent amount, deducted for maintaining the policy.",
							small_normal));
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			table3.addCell(cell);

			// 1st charges row
			cell = new PdfPCell(new Phrase("",
					small_normal));
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			//table3.addCell(cell);
			cell = new PdfPCell(
					new Phrase(
							"2)Premium Allocation Charge : is the percentage of premium that would not be utilised to purchase units.",
							small_normal));
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			table3.addCell(cell);

			// 2 charges row
			cell = new PdfPCell(new Phrase("",
					small_normal));
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			//table3.addCell(cell);
			cell = new PdfPCell(
					new Phrase(
							"3)Mortality Charges : are the charges recovered for providing life insurance cover, deducted at the beginning of each policy month by cancelling units for equivalent amount.",
							small_normal));
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			table3.addCell(cell);

			// 3 charges row
			cell = new PdfPCell(new Phrase("",
					small_normal));
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			//table3.addCell(cell);
			cell = new PdfPCell(
					new Phrase(
							"4)Fund Management Charge (FMC) : is the deduction made from the fund at a stated percentage before the computation of the NAV of the fund.",
							small_normal));
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			table3.addCell(cell);

			// 4 charges row
			cell = new PdfPCell(new Phrase("Mortality charges", small_normal));
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			//table3.addCell(cell);
			cell = new PdfPCell(
					new Phrase(
							"are the charges recovered for providing life insurance cover.",
							small_normal));
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			//table3.addCell(cell);

			Paragraph new_line = new Paragraph("\n");
			LineSeparator ls = new LineSeparator();

			document.add(new_line);
			document.add(main_table);
			//document.add(new_line);
			//document.add(new_line);
			//document.add(new_line);
			//document.add(new_line);
			//document.add(new_line);
			document.add(new_line);

			PdfPTable BI_Pdftablereadunderstand = new PdfPTable(1);
			BI_Pdftablereadunderstand.setWidthPercentage(100);
			PdfPCell BI_Pdftablereadunderstand_cell = new PdfPCell(new Paragraph(
					"How to read and understand this benefit illustration?", small_bold));

			BI_Pdftablereadunderstand_cell
					.setBackgroundColor(BaseColor.LIGHT_GRAY);

			BI_Pdftablereadunderstand_cell
					.setHorizontalAlignment(Element.ALIGN_CENTER);
			BI_Pdftablereadunderstand_cell.setPadding(5);

			BI_Pdftablereadunderstand.addCell(BI_Pdftablereadunderstand_cell);

			PdfPTable BI_Pdftable101 = new PdfPTable(1);
			BI_Pdftable101.setWidthPercentage(100);
			PdfPCell BI_Pdftable101_cell1 = new PdfPCell(
					new Paragraph(
							"This benefit illustration is intended to show what charges are deducted from your premiums and how the unit fund, net of charges and taxes, may grow over the years of the policy term if the fund earns a gross return of 8% p.a. or 4% p.a. These rates, i.e. 8% p.a. and 4% p.a. are assumed only for the purpose of illustrating the flow of benefits if the returns are at this level. It should not be interpreted that the returns under the plan are going to be  either 8% p.a. or 4% p.a.",

							small_normal));

			BI_Pdftable101_cell1.setPadding(5);

			BI_Pdftable101.addCell(BI_Pdftable101_cell1);

			PdfPTable BI_Pdftable102 = new PdfPTable(1);
			BI_Pdftable102.setWidthPercentage(100);
			PdfPCell BI_Pdftable102_cell1 = new PdfPCell(
					new Paragraph(
							"Net yield mentioned corresponds to the gross investment return of 8% p.a., net of all charges but does not consider mortality, underwriting extra, if any, if deducted by cancellation of units. It demonstrates the impact of charges exclusive of taxes on the net yield. Please note that the mortality charges per thousand sum assured in general, increases with age.",

							small_normal));

			BI_Pdftable102_cell1.setPadding(5);

			BI_Pdftable102.addCell(BI_Pdftable102_cell1);

			PdfPTable BI_Pdftable103 = new PdfPTable(1);
			BI_Pdftable103.setWidthPercentage(100);
			PdfPCell BI_Pdftable103_cell1 = new PdfPCell(
					new Paragraph(
							"The actual returns can vary depending on the performance of the chosen fund, charges towards mortality, underwriting extra etc. The investment risk in this policy is borne by the policyholder, hence, for more details on terms and conditions please read the sales literature carefully.",

							small_normal));

			BI_Pdftable103_cell1.setPadding(5);

			BI_Pdftable103.addCell(BI_Pdftable103_cell1);

			PdfPTable BI_Pdftable104 = new PdfPTable(1);
			BI_Pdftable104.setWidthPercentage(100);
			PdfPCell BI_Pdftable104_cell1 = new PdfPCell(
					new Paragraph(
							"Part A of this statement presents a summary view of year- by- year charges deducted under the policy, fund value, surrender value and the death benefit, at two assumed rates of return. Part B of this statement presents a detailed break-up of the charges, and other values.",

							small_normal));

			BI_Pdftable104_cell1.setPadding(5);

			BI_Pdftable104.addCell(BI_Pdftable104_cell1);
			document.add(BI_Pdftablereadunderstand);
			document.add(BI_Pdftable101);
			document.add(BI_Pdftable102);
			document.add(BI_Pdftable103);
			document.add(BI_Pdftable104);


			PdfPTable t1 = new PdfPTable(1);
			t1.setWidthPercentage(100);
			PdfPCell cell1 = new PdfPCell(new Paragraph("PART A", small_bold));
			t1.addCell(cell1);
			document.add(t1);

			PdfPTable BI_PdftableOutputHeader = new PdfPTable(4);

			BI_PdftableOutputHeader.setWidthPercentage(100);
			float[] columnWidths = {2f, 6f, 6f, 1f};
			BI_PdftableOutputHeader.setWidths(columnWidths);
			PdfPCell BI_PdftableBI_PdftableOutputHeader_cell1 = new PdfPCell(
					new Paragraph("Amount in Rupees", small_bold2));
			PdfPCell BI_PdftableBI_PdftableOutputHeader_cell2 = new PdfPCell(
					new Paragraph("At 4% p.a. Gross Investment return", small_bold2));
			PdfPCell BI_PdftableBI_PdftableOutputHeader_cell4 = new PdfPCell(
					new Paragraph("At 8% p.a. Gross Investment return", small_bold2));
			PdfPCell BI_PdftableBI_PdftableOutputHeader_cell41 = new PdfPCell(
					new Paragraph("", small_bold2));

			BI_PdftableBI_PdftableOutputHeader_cell1
					.setHorizontalAlignment(Element.ALIGN_CENTER);
			BI_PdftableBI_PdftableOutputHeader_cell1.setPadding(5);

			BI_PdftableBI_PdftableOutputHeader_cell2
					.setHorizontalAlignment(Element.ALIGN_CENTER);
			BI_PdftableBI_PdftableOutputHeader_cell2.setPadding(5);


			BI_PdftableBI_PdftableOutputHeader_cell4
					.setHorizontalAlignment(Element.ALIGN_CENTER);
			BI_PdftableBI_PdftableOutputHeader_cell4.setPadding(5);
			BI_PdftableBI_PdftableOutputHeader_cell41
					.setHorizontalAlignment(Element.ALIGN_CENTER);
			BI_PdftableBI_PdftableOutputHeader_cell41.setPadding(5);

			BI_PdftableOutputHeader
					.addCell(BI_PdftableBI_PdftableOutputHeader_cell1);
			BI_PdftableOutputHeader
					.addCell(BI_PdftableBI_PdftableOutputHeader_cell2);

			BI_PdftableOutputHeader
					.addCell(BI_PdftableBI_PdftableOutputHeader_cell4);
			BI_PdftableOutputHeader
					.addCell(BI_PdftableBI_PdftableOutputHeader_cell41);

			document.add(BI_PdftableOutputHeader);


			PdfPTable BI_Pdftableoutput = new PdfPTable(15);
			BI_Pdftableoutput.setWidthPercentage(100);

			PdfPCell BI_Pdftable_output_cell1 = new PdfPCell(new Paragraph(
					"Policy year", small_bold2));
			PdfPCell BI_Pdftable_output_cell2 = new PdfPCell(new Paragraph(
					"Annualized Premium", small_bold2));

			PdfPCell BI_Pdftable_output_cell3 = new PdfPCell(new Paragraph(
					"Mortality charges", small_bold2));

			PdfPCell BI_Pdftable_output_cell4 = new PdfPCell(new Paragraph(
					"Other Charges*",
					small_bold2));
			PdfPCell BI_Pdftable_output_cell5 = new PdfPCell(new Paragraph(
					"Applicable Taxes", small_bold2));

			PdfPCell BI_Pdftable_output_cell6 = new PdfPCell(new Paragraph(
					"Fund at end of the year", small_bold2));

			PdfPCell BI_Pdftable_output_cell7 = new PdfPCell(new Paragraph(
					"Surrender Value", small_bold2));
			PdfPCell BI_Pdftable_output_cell8 = new PdfPCell(new Paragraph(
					"Death Benefit", small_bold2));
			PdfPCell BI_Pdftable_output_cell81 = new PdfPCell(new Paragraph(
					"Mortality charges", small_bold2));

			PdfPCell BI_Pdftable_output_cell9 = new PdfPCell(new Paragraph(
					"Other Charges*", small_bold2));

			PdfPCell BI_Pdftable_output_cell10 = new PdfPCell(new Paragraph(
					"Applicable Taxes", small_bold2));
			PdfPCell BI_Pdftable_output_cell11 = new PdfPCell(new Paragraph(
					"Fund at end of the year", small_bold2));


			PdfPCell BI_Pdftable_output_cell13 = new PdfPCell(new Paragraph(
					"Surrender Value", small_bold2));
			PdfPCell BI_Pdftable_output_cell14 = new PdfPCell(new Paragraph(
					"Death Benefit", small_bold2));

			PdfPCell BI_Pdftable_output_cell24 = new PdfPCell(new Paragraph(
					"Commission payable to intermediary (Rs)", small_bold2));

			BI_Pdftableoutput.addCell(BI_Pdftable_output_cell1);
			BI_Pdftableoutput.addCell(BI_Pdftable_output_cell2);
			BI_Pdftableoutput.addCell(BI_Pdftable_output_cell3);

			BI_Pdftableoutput.addCell(BI_Pdftable_output_cell4);
			BI_Pdftableoutput.addCell(BI_Pdftable_output_cell5);
			BI_Pdftableoutput.addCell(BI_Pdftable_output_cell6);

			BI_Pdftableoutput.addCell(BI_Pdftable_output_cell7);
			BI_Pdftableoutput.addCell(BI_Pdftable_output_cell8);
			BI_Pdftableoutput.addCell(BI_Pdftable_output_cell81);
			BI_Pdftableoutput.addCell(BI_Pdftable_output_cell9);

			BI_Pdftableoutput.addCell(BI_Pdftable_output_cell10);
			BI_Pdftableoutput.addCell(BI_Pdftable_output_cell11);

			BI_Pdftableoutput.addCell(BI_Pdftable_output_cell13);
			BI_Pdftableoutput.addCell(BI_Pdftable_output_cell14);

			BI_Pdftableoutput.addCell(BI_Pdftable_output_cell24);
			document.add(BI_Pdftableoutput);

			PdfPTable BI_Pdftableoutput_no = new PdfPTable(15);
			BI_Pdftableoutput_no.setWidthPercentage(100);

			PdfPCell BI_Pdftable_output_no_cell1 = new PdfPCell(new Paragraph(
					"1", small_bold2));
			PdfPCell BI_Pdftable_output_no_cell2 = new PdfPCell(new Paragraph(
					"2", small_bold2));

			PdfPCell BI_Pdftable_output_no_cell3 = new PdfPCell(new Paragraph(
					"3", small_bold2));

			PdfPCell BI_Pdftable_output_no_cell4 = new PdfPCell(new Paragraph(
					"4", small_bold2));
			PdfPCell BI_Pdftable_output_no_cell5 = new PdfPCell(new Paragraph(
					"5", small_bold2));

			PdfPCell BI_Pdftable_output_no_cell6 = new PdfPCell(new Paragraph(
					"6", small_bold2));

			PdfPCell BI_Pdftable_output_no_cell7 = new PdfPCell(new Paragraph(
					"7", small_bold2));
			PdfPCell BI_Pdftable_output_no_cell8 = new PdfPCell(new Paragraph(
					"8", small_bold2));

			PdfPCell BI_Pdftable_output_no_cell9 = new PdfPCell(new Paragraph(
					"9", small_bold2));

			PdfPCell BI_Pdftable_output_no_cell10 = new PdfPCell(new Paragraph(
					"10", small_bold2));
			PdfPCell BI_Pdftable_output_no_cell11 = new PdfPCell(new Paragraph(
					"11", small_bold2));

			PdfPCell BI_Pdftable_output_no_cell12 = new PdfPCell(new Paragraph(
					"12", small_bold2));

			PdfPCell BI_Pdftable_output_no_cell13 = new PdfPCell(new Paragraph(
					"13", small_bold2));
			PdfPCell BI_Pdftable_output_no_cell14 = new PdfPCell(new Paragraph(
					"14", small_bold2));


			PdfPCell BI_Pdftable_output_no_cell24 = new PdfPCell(new Paragraph(
					"15", small_bold2));


			BI_Pdftableoutput_no.addCell(BI_Pdftable_output_no_cell1);
			BI_Pdftableoutput_no.addCell(BI_Pdftable_output_no_cell2);
			BI_Pdftableoutput_no.addCell(BI_Pdftable_output_no_cell3);

			BI_Pdftableoutput_no.addCell(BI_Pdftable_output_no_cell4);
			BI_Pdftableoutput_no.addCell(BI_Pdftable_output_no_cell5);
			BI_Pdftableoutput_no.addCell(BI_Pdftable_output_no_cell6);

			BI_Pdftableoutput_no.addCell(BI_Pdftable_output_no_cell7);
			BI_Pdftableoutput_no.addCell(BI_Pdftable_output_no_cell8);
			BI_Pdftableoutput_no.addCell(BI_Pdftable_output_no_cell9);

			BI_Pdftableoutput_no.addCell(BI_Pdftable_output_no_cell10);
			BI_Pdftableoutput_no.addCell(BI_Pdftable_output_no_cell11);
			BI_Pdftableoutput_no.addCell(BI_Pdftable_output_no_cell12);

			BI_Pdftableoutput_no.addCell(BI_Pdftable_output_no_cell13);
			BI_Pdftableoutput_no.addCell(BI_Pdftable_output_no_cell14);

			BI_Pdftableoutput_no.addCell(BI_Pdftable_output_no_cell24);
			document.add(BI_Pdftableoutput_no);

			for (int i = 0; i < Integer.parseInt(policyTerm); i++) {

				PdfPTable BI_Pdftableoutput_row1 = new PdfPTable(15);
				BI_Pdftableoutput_row1.setWidthPercentage(100);

				PdfPCell BI_Pdftable_output_row1_cell1 = new PdfPCell(
						new Paragraph(list_data.get(i).getPolicy_year(),
								small_bold2));
				PdfPCell BI_Pdftable_output_row1_cell2 = new PdfPCell(
						new Paragraph(list_data.get(i).getPremium(),
								small_bold2));

				PdfPCell BI_Pdftable_output_row1_cell3 = new PdfPCell(
						new Paragraph(list_data.get(i)
								.getMortality_charge1(), small_bold2));

				PdfPCell BI_Pdftable_output_row1_cell5 = new PdfPCell(
						new Paragraph(list_data.get(i)
								.getTotal_charge1(), small_bold2));

				PdfPCell BI_Pdftable_output_row1_cell6 = new PdfPCell(
						new Paragraph(list_data.get(i).getService_tax_on_mortality_charge1(),
								small_bold2));

				PdfPCell BI_Pdftable_output_row1_cell7 = new PdfPCell(
						new Paragraph(list_data.get(i).getFund_value_at_end1(),
								small_bold2));
				PdfPCell BI_Pdftable_output_row1_cell8 = new PdfPCell(
						new Paragraph(list_data.get(i)
								.getSurrender_value1(),
								small_bold2));
				PdfPCell BI_Pdftable_output_row1_cell81 = new PdfPCell(
						new Paragraph(list_data.get(i).getDeath_benefit1(), small_bold2));


				PdfPCell BI_Pdftable_output_row1_cell31 = new PdfPCell(
						new Paragraph(list_data.get(i)
								.getMortality_charge2(), small_bold2));

				PdfPCell BI_Pdftable_output_row1_cell51 = new PdfPCell(
						new Paragraph(list_data.get(i)
								.getTotal_charge2(), small_bold2));

				PdfPCell BI_Pdftable_output_row1_cell61 = new PdfPCell(
						new Paragraph(list_data.get(i).getService_tax_on_mortality_charge2(),
								small_bold2));


				PdfPCell BI_Pdftable_output_row1_cell12 = new PdfPCell(
						new Paragraph(list_data.get(i).getFund_value_at_end2(),
								small_bold2));

				PdfPCell BI_Pdftable_output_row1_cell13 = new PdfPCell(
						new Paragraph(list_data.get(i).getSurrender_value2(),
								small_bold2));

				PdfPCell BI_Pdftable_output_row1_cell14 = new PdfPCell(
						new Paragraph(list_data.get(i).getDeath_benefit2(),
								small_bold2));


				PdfPCell BI_Pdftable_output_row1_cell24 = new PdfPCell(
						new Paragraph(list_data.get(i).getCommission(),
								small_bold2));

				BI_Pdftableoutput_row1.addCell(BI_Pdftable_output_row1_cell1);
				BI_Pdftableoutput_row1.addCell(BI_Pdftable_output_row1_cell2);
				BI_Pdftableoutput_row1.addCell(BI_Pdftable_output_row1_cell3);

				BI_Pdftableoutput_row1.addCell(BI_Pdftable_output_row1_cell5);
				BI_Pdftableoutput_row1.addCell(BI_Pdftable_output_row1_cell6);

				BI_Pdftableoutput_row1.addCell(BI_Pdftable_output_row1_cell7);
				BI_Pdftableoutput_row1.addCell(BI_Pdftable_output_row1_cell8);

				BI_Pdftableoutput_row1.addCell(BI_Pdftable_output_row1_cell81);
				BI_Pdftableoutput_row1.addCell(BI_Pdftable_output_row1_cell31);

				BI_Pdftableoutput_row1.addCell(BI_Pdftable_output_row1_cell51);
				BI_Pdftableoutput_row1.addCell(BI_Pdftable_output_row1_cell61);


				BI_Pdftableoutput_row1.addCell(BI_Pdftable_output_row1_cell12);

				BI_Pdftableoutput_row1.addCell(BI_Pdftable_output_row1_cell13);
				BI_Pdftableoutput_row1.addCell(BI_Pdftable_output_row1_cell14);

				BI_Pdftableoutput_row1.addCell(BI_Pdftable_output_row1_cell24);
				document.add(BI_Pdftableoutput_row1);

			}

			PdfPTable BI_PdftableOutputHeader833 = new PdfPTable(3);
			BI_PdftableOutputHeader833.setWidthPercentage(100);
			float[] columnWidths833 = {2f, 6f, 7f};
			BI_PdftableOutputHeader833.setWidths(columnWidths833);
			PdfPCell see_partB = new PdfPCell(
					new Paragraph("* See Part B for details", small_bold2));
			see_partB.setColspan(3);
			PdfPCell decl = new PdfPCell(
					new Paragraph("IN THIS POLICY, THE INVESTMENT RISK IS BORNE BY THE POLICYHOLDER AND THE ABOVE INTEREST RATES ARE ONLY FOR ILLUSTRATIVE PURPOSE", small_bold2));
			decl.setColspan(3);

			BI_PdftableOutputHeader833
					.addCell(see_partB);
			BI_PdftableOutputHeader833
					.addCell(decl);


			document.add(BI_PdftableOutputHeader833);
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
									+ "    have received the information with respect to the above and have understood the above statement before entering into a contract.",
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

			PdfPTable t2 = new PdfPTable(1);
			t2.setWidthPercentage(100);
			PdfPCell cell2 = new PdfPCell(new Paragraph("PART B", small_bold));
			t2.addCell(cell2);
			document.add(t2);


			PdfPTable BI_PdftableOutputHeader8 = new PdfPTable(3);
			BI_PdftableOutputHeader8.setWidthPercentage(100);
			float[] columnWidths8 = {2f, 6f, 7f};
			BI_PdftableOutputHeader8.setWidths(columnWidths8);
			PdfPCell BI_PdftableBI_PdftableOutputHeader_cell18 = new PdfPCell(
					new Paragraph("Amount in  Rs.", small_bold2));
			PdfPCell BI_PdftableBI_PdftableOutputHeader_cell28 = new PdfPCell(
					new Paragraph("Gross Yield 8% pa",
							small_bold2));

			PdfPCell BI_PdftableBI_PdftableOutputHeader_cell48 = new PdfPCell(
					new Paragraph("Net Yield " + netYield8pa + "%", small_bold2));


			BI_PdftableBI_PdftableOutputHeader_cell18
					.setHorizontalAlignment(Element.ALIGN_CENTER);
			BI_PdftableBI_PdftableOutputHeader_cell18.setPadding(5);

			BI_PdftableBI_PdftableOutputHeader_cell28
					.setHorizontalAlignment(Element.ALIGN_CENTER);
			BI_PdftableBI_PdftableOutputHeader_cell28.setPadding(5);
			BI_PdftableBI_PdftableOutputHeader_cell48
					.setHorizontalAlignment(Element.ALIGN_CENTER);
			BI_PdftableBI_PdftableOutputHeader_cell48.setPadding(5);


			BI_PdftableOutputHeader8
					.addCell(BI_PdftableBI_PdftableOutputHeader_cell18);
			BI_PdftableOutputHeader8
					.addCell(BI_PdftableBI_PdftableOutputHeader_cell28);

			BI_PdftableOutputHeader8
					.addCell(BI_PdftableBI_PdftableOutputHeader_cell48);


			document.add(BI_PdftableOutputHeader8);

			PdfPTable BI_Pdftableoutput8 = new PdfPTable(15);
			BI_Pdftableoutput8.setWidthPercentage(100);

			PdfPCell BI_Pdftable_output_cell18 = new PdfPCell(new Paragraph(
					"Policy Year", small_bold2));
			PdfPCell BI_Pdftable_output_cell28 = new PdfPCell(new Paragraph(
					"Annualized Premium  (AP) ", small_bold2));

			PdfPCell BI_Pdftable_output_cell38 = new PdfPCell(new Paragraph(
					"Premium Allocation Charge (PAC)", small_bold2));

			PdfPCell BI_Pdftable_output_cell48 = new PdfPCell(new Paragraph(
					"Annualized Premium - Premium Allocation Charge",
					small_bold2));
			PdfPCell BI_Pdftable_output_cell58 = new PdfPCell(new Paragraph(
					"Mortality charge", small_bold2));

			PdfPCell BI_Pdftable_output_cell68 = new PdfPCell(new Paragraph(
					"Applicable Taxes", small_bold2));

			PdfPCell BI_Pdftable_output_cell78 = new PdfPCell(new Paragraph(
					"Policy Admin charge", small_bold2));

			PdfPCell BI_Pdftable_output_cell781 = new PdfPCell(new Paragraph(
					"Acc. TPD Charges", small_bold2));
			PdfPCell BI_Pdftable_output_cell88 = new PdfPCell(new Paragraph(
					"Other charges*", small_bold2));
			PdfPCell BI_Pdftable_output_cell881 = new PdfPCell(new Paragraph(
					"Additions to the fund*", small_bold2));

			PdfPCell BI_Pdftable_output_cell98 = new PdfPCell(new Paragraph(
					"Fund before FMC", small_bold2));

			PdfPCell BI_Pdftable_output_cell108 = new PdfPCell(new Paragraph(
					"FMC", small_bold2));
			PdfPCell BI_Pdftable_output_cell118 = new PdfPCell(new Paragraph(
					"Fund at End of year", small_bold2));
			PdfPCell BI_Pdftable_output_cell128 = new PdfPCell(new Paragraph(
					"Surrender Value", small_bold2));

			PdfPCell BI_Pdftable_output_cell138 = new PdfPCell(new Paragraph(
					"Death benefit", small_bold2));


			BI_Pdftableoutput8.addCell(BI_Pdftable_output_cell18);
			BI_Pdftableoutput8.addCell(BI_Pdftable_output_cell28);
			BI_Pdftableoutput8.addCell(BI_Pdftable_output_cell38);

			BI_Pdftableoutput8.addCell(BI_Pdftable_output_cell48);
			BI_Pdftableoutput8.addCell(BI_Pdftable_output_cell58);
			BI_Pdftableoutput8.addCell(BI_Pdftable_output_cell68);

			BI_Pdftableoutput8.addCell(BI_Pdftable_output_cell78);

			BI_Pdftableoutput8.addCell(BI_Pdftable_output_cell781);
			BI_Pdftableoutput8.addCell(BI_Pdftable_output_cell88);
			BI_Pdftableoutput8.addCell(BI_Pdftable_output_cell881);
			BI_Pdftableoutput8.addCell(BI_Pdftable_output_cell98);

			BI_Pdftableoutput8.addCell(BI_Pdftable_output_cell108);
			BI_Pdftableoutput8.addCell(BI_Pdftable_output_cell118);
			BI_Pdftableoutput8.addCell(BI_Pdftable_output_cell128);

			BI_Pdftableoutput8.addCell(BI_Pdftable_output_cell138);

			document.add(BI_Pdftableoutput8);

			PdfPTable BI_Pdftableoutput_no8 = new PdfPTable(15);
			BI_Pdftableoutput_no8.setWidthPercentage(100);

			PdfPCell BI_Pdftable_output_no_cell18 = new PdfPCell(new Paragraph(
					"1", small_bold2));
			PdfPCell BI_Pdftable_output_no_cell28 = new PdfPCell(new Paragraph(
					"2", small_bold2));

			PdfPCell BI_Pdftable_output_no_cell38 = new PdfPCell(new Paragraph(
					"3", small_bold2));

			PdfPCell BI_Pdftable_output_no_cell48 = new PdfPCell(new Paragraph(
					"4", small_bold2));
			PdfPCell BI_Pdftable_output_no_cell58 = new PdfPCell(new Paragraph(
					"5", small_bold2));

			PdfPCell BI_Pdftable_output_no_cell68 = new PdfPCell(new Paragraph(
					"6", small_bold2));

			PdfPCell BI_Pdftable_output_no_cell78 = new PdfPCell(new Paragraph(
					"7", small_bold2));
			PdfPCell BI_Pdftable_output_no_cell88 = new PdfPCell(new Paragraph(
					"8", small_bold2));

			PdfPCell BI_Pdftable_output_no_cell98 = new PdfPCell(new Paragraph(
					"9", small_bold2));

			PdfPCell BI_Pdftable_output_no_cell108 = new PdfPCell(new Paragraph(
					"10", small_bold2));
			PdfPCell BI_Pdftable_output_no_cell118 = new PdfPCell(new Paragraph(
					"11", small_bold2));

			PdfPCell BI_Pdftable_output_no_cell128 = new PdfPCell(new Paragraph(
					"12", small_bold2));

			PdfPCell BI_Pdftable_output_no_cell138 = new PdfPCell(new Paragraph(
					"13", small_bold2));
			PdfPCell BI_Pdftable_output_no_cell148 = new PdfPCell(new Paragraph(
					"14", small_bold2));


			PdfPCell BI_Pdftable_output_no_cell248 = new PdfPCell(new Paragraph(
					"15", small_bold2));

			BI_Pdftableoutput_no8.addCell(BI_Pdftable_output_no_cell18);
			BI_Pdftableoutput_no8.addCell(BI_Pdftable_output_no_cell28);
			BI_Pdftableoutput_no8.addCell(BI_Pdftable_output_no_cell38);

			BI_Pdftableoutput_no8.addCell(BI_Pdftable_output_no_cell48);
			BI_Pdftableoutput_no8.addCell(BI_Pdftable_output_no_cell58);
			BI_Pdftableoutput_no8.addCell(BI_Pdftable_output_no_cell68);

			BI_Pdftableoutput_no8.addCell(BI_Pdftable_output_no_cell78);
			BI_Pdftableoutput_no8.addCell(BI_Pdftable_output_no_cell88);
			BI_Pdftableoutput_no8.addCell(BI_Pdftable_output_no_cell98);

			BI_Pdftableoutput_no8.addCell(BI_Pdftable_output_no_cell108);
			BI_Pdftableoutput_no8.addCell(BI_Pdftable_output_no_cell118);
			BI_Pdftableoutput_no8.addCell(BI_Pdftable_output_no_cell128);

			BI_Pdftableoutput_no8.addCell(BI_Pdftable_output_no_cell138);
			BI_Pdftableoutput_no8.addCell(BI_Pdftable_output_no_cell148);

			BI_Pdftableoutput_no8.addCell(BI_Pdftable_output_no_cell248);
			//document.add(BI_Pdftableoutput_no8);

			for (int i = 0; i < Integer.parseInt(policyTerm); i++) {

				PdfPTable BI_Pdftableoutput_row18 = new PdfPTable(15);
				BI_Pdftableoutput_row18.setWidthPercentage(100);

				PdfPCell BI_Pdftable_output_row1_cell18 = new PdfPCell(
						new Paragraph(list_data1.get(i).getPolicy_year(),
								small_bold2));
				PdfPCell BI_Pdftable_output_row1_cell28 = new PdfPCell(
						new Paragraph(list_data1.get(i).getPremium(),
								small_bold2));

				PdfPCell BI_Pdftable_output_row1_cell38 = new PdfPCell(
						new Paragraph(list_data1.get(i)
								.getPremium_allocation_charge(), small_bold2));

				PdfPCell BI_Pdftable_output_row1_cell48 = new PdfPCell(
						new Paragraph(list_data1.get(i)
								.getAnnulized_premium_allocation_charge(), small_bold2));


				PdfPCell BI_Pdftable_output_row1_cell68 = new PdfPCell(
						new Paragraph(list_data1.get(i).getMortality_charge1(),
								small_bold2));

				PdfPCell BI_Pdftable_output_row1_cell88 = new PdfPCell(
						new Paragraph(list_data1.get(i)
								.getService_tax_on_mortality_charge1(),
								small_bold2));

				PdfPCell BI_Pdftable_output_row1_cell58 = new PdfPCell(
						new Paragraph(list_data1.get(i)
								.getPolicy_administration_charge(), small_bold2));

				PdfPCell BI_Pdftable_output_row1_cell118 = new PdfPCell(
						new Paragraph(
								list_data1.get(i).getGuranteed_addition1(),
								small_bold2));

				PdfPCell BI_Pdftable_output_row1_cell78 = new PdfPCell(
						new Paragraph(list_data1.get(i).getTotal_charge1(),
								small_bold2));

				PdfPCell BI_Pdftable_output_row1_cell881 = new PdfPCell(
						new Paragraph(list_data1.get(i)
								.getStr_AddToTheFund4Pr(),
								small_bold2));

				PdfPCell BI_Pdftable_output_row1_cell98 = new PdfPCell(
						new Paragraph(list_data1.get(i).getFund_before_fmc1(),
								small_bold2));

				PdfPCell BI_Pdftable_output_row1_cell108 = new PdfPCell(
						new Paragraph(list_data1.get(i)
								.getFund_management_charge1(), small_bold2));


				PdfPCell BI_Pdftable_output_row1_cell128 = new PdfPCell(
						new Paragraph(list_data1.get(i).getFund_value_at_end1(),
								small_bold2));

				PdfPCell BI_Pdftable_output_row1_cell138 = new PdfPCell(
						new Paragraph(list_data1.get(i).getSurrender_value1(),
								small_bold2));

				PdfPCell BI_Pdftable_output_row1_cell148 = new PdfPCell(
						new Paragraph(list_data1.get(i).getDeath_benefit1(),
								small_bold2));


				BI_Pdftableoutput_row18.addCell(BI_Pdftable_output_row1_cell18);
				BI_Pdftableoutput_row18.addCell(BI_Pdftable_output_row1_cell28);
				BI_Pdftableoutput_row18.addCell(BI_Pdftable_output_row1_cell38);
				BI_Pdftableoutput_row18.addCell(BI_Pdftable_output_row1_cell48);
				BI_Pdftableoutput_row18.addCell(BI_Pdftable_output_row1_cell68);
				BI_Pdftableoutput_row18.addCell(BI_Pdftable_output_row1_cell88);
				BI_Pdftableoutput_row18.addCell(BI_Pdftable_output_row1_cell58);

				BI_Pdftableoutput_row18.addCell(BI_Pdftable_output_row1_cell118);
				BI_Pdftableoutput_row18.addCell(BI_Pdftable_output_row1_cell78);
				BI_Pdftableoutput_row18.addCell(BI_Pdftable_output_row1_cell881);
				BI_Pdftableoutput_row18.addCell(BI_Pdftable_output_row1_cell98);

				BI_Pdftableoutput_row18.addCell(BI_Pdftable_output_row1_cell108);
				BI_Pdftableoutput_row18.addCell(BI_Pdftable_output_row1_cell128);

				BI_Pdftableoutput_row18.addCell(BI_Pdftable_output_row1_cell138);
				BI_Pdftableoutput_row18.addCell(BI_Pdftable_output_row1_cell148);

				document.add(BI_Pdftableoutput_row18);

			}


			PdfPTable BI_PdftableNetYield = new PdfPTable(4);

			BI_PdftableNetYield.setWidthPercentage(100);
			float[] columnWidthsNetYield = {5f, 9f, 9f, 1f};
			BI_PdftableNetYield.setWidths(columnWidthsNetYield);
			PdfPCell BI_PdftableBI_PdftableNetYield_cell1 = new PdfPCell(
					new Paragraph("", small_normal));
			PdfPCell BI_PdftableBI_PdftableNetYield_cell2 = new PdfPCell(
					new Paragraph("Net Yield -  " + netYield8pa + " %", small_bold2));

			PdfPCell BI_PdftableBI_PdftableNetYield_cell3 = new PdfPCell(
					new Paragraph("Reduction in yield -  " + redInYieldMat + " %",
							small_bold2));

			PdfPCell BI_PdftableBI_PdftableNetYield_cell4 = new PdfPCell(
					new Paragraph("", small_bold2));

			BI_PdftableBI_PdftableNetYield_cell1
					.setHorizontalAlignment(Element.ALIGN_CENTER);
			BI_PdftableBI_PdftableNetYield_cell1.setPadding(5);

			BI_PdftableBI_PdftableNetYield_cell2
					.setHorizontalAlignment(Element.ALIGN_CENTER);
			BI_PdftableBI_PdftableNetYield_cell2.setPadding(5);

			BI_PdftableBI_PdftableNetYield_cell3
					.setHorizontalAlignment(Element.ALIGN_CENTER);
			BI_PdftableBI_PdftableNetYield_cell3.setPadding(5);

			BI_PdftableBI_PdftableNetYield_cell4
					.setHorizontalAlignment(Element.ALIGN_CENTER);
			BI_PdftableBI_PdftableNetYield_cell4.setPadding(5);

			BI_PdftableNetYield.addCell(BI_PdftableBI_PdftableNetYield_cell1);
			BI_PdftableNetYield.addCell(BI_PdftableBI_PdftableNetYield_cell2);
			BI_PdftableNetYield.addCell(BI_PdftableBI_PdftableNetYield_cell3);
			BI_PdftableNetYield.addCell(BI_PdftableBI_PdftableNetYield_cell4);

			// document.add(BI_PdftableNetYield);


			PdfPTable BI_PdftableOutputHeader4 = new PdfPTable(3);
			BI_PdftableOutputHeader4.setWidthPercentage(100);
			float[] columnWidths84 = {2f, 6f, 7f};
			BI_PdftableOutputHeader4.setWidths(columnWidths84);
			PdfPCell BI_PdftableBI_PdftableOutputHeader_cell14 = new PdfPCell(
					new Paragraph("Amount in  Rs.", small_bold2));
			PdfPCell BI_PdftableBI_PdftableOutputHeader_cell24 = new PdfPCell(
					new Paragraph("Gross Yield 4% pa",
							small_bold2));


			PdfPCell BI_PdftableBI_PdftableOutputHeader_cell44 = new PdfPCell(
					new Paragraph("", small_bold2));

			BI_PdftableBI_PdftableOutputHeader_cell14
					.setHorizontalAlignment(Element.ALIGN_CENTER);
			BI_PdftableBI_PdftableOutputHeader_cell14.setPadding(5);

			BI_PdftableBI_PdftableOutputHeader_cell24
					.setHorizontalAlignment(Element.ALIGN_CENTER);
			BI_PdftableBI_PdftableOutputHeader_cell24.setPadding(5);


			BI_PdftableBI_PdftableOutputHeader_cell44
					.setHorizontalAlignment(Element.ALIGN_CENTER);
			BI_PdftableBI_PdftableOutputHeader_cell44.setPadding(5);

			BI_PdftableOutputHeader4
					.addCell(BI_PdftableBI_PdftableOutputHeader_cell14);
			BI_PdftableOutputHeader4
					.addCell(BI_PdftableBI_PdftableOutputHeader_cell24);

			BI_PdftableOutputHeader4
					.addCell(BI_PdftableBI_PdftableOutputHeader_cell44);

			document.add(BI_PdftableOutputHeader4);

			PdfPTable BI_Pdftableoutput4 = new PdfPTable(15);
			BI_Pdftableoutput4.setWidthPercentage(100);

			PdfPCell BI_Pdftable_output_cell181 = new PdfPCell(new Paragraph(
					"Policy Year", small_bold2));
			PdfPCell BI_Pdftable_output_cell281 = new PdfPCell(new Paragraph(
					"Annualized Premium  (AP) ", small_bold2));

			PdfPCell BI_Pdftable_output_cell381 = new PdfPCell(new Paragraph(
					"Premium Allocation Charge (PAC)", small_bold2));

			PdfPCell BI_Pdftable_output_cell481 = new PdfPCell(new Paragraph(
					"Annualized Premium - Premium Allocation Charge",
					small_bold2));
			PdfPCell BI_Pdftable_output_cell581 = new PdfPCell(new Paragraph(
					"Mortality charge", small_bold2));

			PdfPCell BI_Pdftable_output_cell681 = new PdfPCell(new Paragraph(
					"Applicable Taxes", small_bold2));

			PdfPCell BI_Pdftable_output_cell7814 = new PdfPCell(new Paragraph(
					"Policy Admin charge", small_bold2));

			PdfPCell BI_Pdftable_output_cell7811 = new PdfPCell(new Paragraph(
					"Acc. TPD Charges", small_bold2));
			PdfPCell BI_Pdftable_output_cell8814 = new PdfPCell(new Paragraph(
					"Other charges*", small_bold2));
			PdfPCell BI_Pdftable_output_cell8811 = new PdfPCell(new Paragraph(
					"Additions to the fund*", small_bold2));

			PdfPCell BI_Pdftable_output_cell981 = new PdfPCell(new Paragraph(
					"Fund before FMC", small_bold2));

			PdfPCell BI_Pdftable_output_cell1081 = new PdfPCell(new Paragraph(
					"FMC", small_bold2));
			PdfPCell BI_Pdftable_output_cell1181 = new PdfPCell(new Paragraph(
					"Fund at End of year", small_bold2));
			PdfPCell BI_Pdftable_output_cell1281 = new PdfPCell(new Paragraph(
					"Surrender Value", small_bold2));

			PdfPCell BI_Pdftable_output_cell1381 = new PdfPCell(new Paragraph(
					"Death benefit", small_bold2));


			BI_Pdftableoutput4.addCell(BI_Pdftable_output_cell181);
			BI_Pdftableoutput4.addCell(BI_Pdftable_output_cell281);
			BI_Pdftableoutput4.addCell(BI_Pdftable_output_cell381);

			BI_Pdftableoutput4.addCell(BI_Pdftable_output_cell481);
			BI_Pdftableoutput4.addCell(BI_Pdftable_output_cell581);
			BI_Pdftableoutput4.addCell(BI_Pdftable_output_cell681);

			BI_Pdftableoutput4.addCell(BI_Pdftable_output_cell7814);

			BI_Pdftableoutput4.addCell(BI_Pdftable_output_cell7811);
			BI_Pdftableoutput4.addCell(BI_Pdftable_output_cell8814);
			BI_Pdftableoutput4.addCell(BI_Pdftable_output_cell8811);
			BI_Pdftableoutput4.addCell(BI_Pdftable_output_cell981);

			BI_Pdftableoutput4.addCell(BI_Pdftable_output_cell1081);
			BI_Pdftableoutput4.addCell(BI_Pdftable_output_cell1181);
			BI_Pdftableoutput4.addCell(BI_Pdftable_output_cell1281);

			BI_Pdftableoutput4.addCell(BI_Pdftable_output_cell1381);

			document.add(BI_Pdftableoutput4);

			PdfPTable BI_Pdftableoutput_no4 = new PdfPTable(15);
			BI_Pdftableoutput_no4.setWidthPercentage(100);

			PdfPCell BI_Pdftable_output_no_cell181 = new PdfPCell(new Paragraph(
					"1", small_bold2));
			PdfPCell BI_Pdftable_output_no_cell281 = new PdfPCell(new Paragraph(
					"2", small_bold2));

			PdfPCell BI_Pdftable_output_no_cell381 = new PdfPCell(new Paragraph(
					"3", small_bold2));

			PdfPCell BI_Pdftable_output_no_cell481 = new PdfPCell(new Paragraph(
					"4", small_bold2));
			PdfPCell BI_Pdftable_output_no_cell581 = new PdfPCell(new Paragraph(
					"5", small_bold2));

			PdfPCell BI_Pdftable_output_no_cell681 = new PdfPCell(new Paragraph(
					"6", small_bold2));

			PdfPCell BI_Pdftable_output_no_cell781 = new PdfPCell(new Paragraph(
					"7", small_bold2));
			PdfPCell BI_Pdftable_output_no_cell881 = new PdfPCell(new Paragraph(
					"8", small_bold2));

			PdfPCell BI_Pdftable_output_no_cell981 = new PdfPCell(new Paragraph(
					"9", small_bold2));

			PdfPCell BI_Pdftable_output_no_cell1081 = new PdfPCell(new Paragraph(
					"10", small_bold2));
			PdfPCell BI_Pdftable_output_no_cell1181 = new PdfPCell(new Paragraph(
					"11", small_bold2));

			PdfPCell BI_Pdftable_output_no_cell1281 = new PdfPCell(new Paragraph(
					"12", small_bold2));

			PdfPCell BI_Pdftable_output_no_cell1381 = new PdfPCell(new Paragraph(
					"13", small_bold2));
			PdfPCell BI_Pdftable_output_no_cell1481 = new PdfPCell(new Paragraph(
					"14", small_bold2));


			PdfPCell BI_Pdftable_output_no_cell2481 = new PdfPCell(new Paragraph(
					"15", small_bold2));

			BI_Pdftableoutput_no4.addCell(BI_Pdftable_output_no_cell181);
			BI_Pdftableoutput_no4.addCell(BI_Pdftable_output_no_cell281);
			BI_Pdftableoutput_no4.addCell(BI_Pdftable_output_no_cell381);

			BI_Pdftableoutput_no4.addCell(BI_Pdftable_output_no_cell481);
			BI_Pdftableoutput_no4.addCell(BI_Pdftable_output_no_cell581);
			BI_Pdftableoutput_no4.addCell(BI_Pdftable_output_no_cell681);

			BI_Pdftableoutput_no4.addCell(BI_Pdftable_output_no_cell781);
			BI_Pdftableoutput_no4.addCell(BI_Pdftable_output_no_cell881);
			BI_Pdftableoutput_no4.addCell(BI_Pdftable_output_no_cell981);

			BI_Pdftableoutput_no4.addCell(BI_Pdftable_output_no_cell1081);
			BI_Pdftableoutput_no4.addCell(BI_Pdftable_output_no_cell1181);
			BI_Pdftableoutput_no4.addCell(BI_Pdftable_output_no_cell1281);

			BI_Pdftableoutput_no4.addCell(BI_Pdftable_output_no_cell1381);
			BI_Pdftableoutput_no4.addCell(BI_Pdftable_output_no_cell1481);

			BI_Pdftableoutput_no4.addCell(BI_Pdftable_output_no_cell2481);
			//document.add(BI_Pdftableoutput_no4);

			for (int i = 0; i < Integer.parseInt(policyTerm); i++) {

				PdfPTable BI_Pdftableoutput_row184 = new PdfPTable(15);
				BI_Pdftableoutput_row184.setWidthPercentage(100);

				PdfPCell BI_Pdftable_output_row1_cell181 = new PdfPCell(
						new Paragraph(list_data2.get(i).getPolicy_year(),
								small_bold2));
				PdfPCell BI_Pdftable_output_row1_cell281 = new PdfPCell(
						new Paragraph(list_data2.get(i).getPremium(),
								small_bold2));

				PdfPCell BI_Pdftable_output_row1_cell381 = new PdfPCell(
						new Paragraph(list_data2.get(i)
								.getPremium_allocation_charge(), small_bold2));

				PdfPCell BI_Pdftable_output_row1_cell481 = new PdfPCell(
						new Paragraph(list_data2.get(i)
								.getAnnulized_premium_allocation_charge(), small_bold2));


				PdfPCell BI_Pdftable_output_row1_cell681 = new PdfPCell(
						new Paragraph(list_data2.get(i).getMortality_charge1(),
								small_bold2));

				PdfPCell BI_Pdftable_output_row1_cell881 = new PdfPCell(
						new Paragraph(list_data2.get(i)
								.getService_tax_on_mortality_charge1(),
								small_bold2));

				PdfPCell BI_Pdftable_output_row1_cell581 = new PdfPCell(
						new Paragraph(list_data2.get(i)
								.getPolicy_administration_charge(), small_bold2));

				PdfPCell BI_Pdftable_output_row1_cell1181 = new PdfPCell(
						new Paragraph(
								list_data2.get(i).getGuranteed_addition1(),
								small_bold2));

				PdfPCell BI_Pdftable_output_row1_cell781 = new PdfPCell(
						new Paragraph(list_data2.get(i).getTotal_charge1(),
								small_bold2));

				PdfPCell BI_Pdftable_output_row1_cell8811 = new PdfPCell(
						new Paragraph(list_data2.get(i)
								.getStr_AddToTheFund4Pr(),
								small_bold2));

				PdfPCell BI_Pdftable_output_row1_cell981 = new PdfPCell(
						new Paragraph(list_data2.get(i).getFund_before_fmc1(),
								small_bold2));

				PdfPCell BI_Pdftable_output_row1_cell1081 = new PdfPCell(
						new Paragraph(list_data2.get(i)
								.getFund_management_charge1(), small_bold2));


				PdfPCell BI_Pdftable_output_row1_cell1281 = new PdfPCell(
						new Paragraph(list_data2.get(i).getFund_value_at_end1(),
								small_bold2));

				PdfPCell BI_Pdftable_output_row1_cell1381 = new PdfPCell(
						new Paragraph(list_data2.get(i).getSurrender_value1(),
								small_bold2));

				PdfPCell BI_Pdftable_output_row1_cell1481 = new PdfPCell(
						new Paragraph(list_data2.get(i).getDeath_benefit1(),
								small_bold2));


				BI_Pdftableoutput_row184.addCell(BI_Pdftable_output_row1_cell181);
				BI_Pdftableoutput_row184.addCell(BI_Pdftable_output_row1_cell281);
				BI_Pdftableoutput_row184.addCell(BI_Pdftable_output_row1_cell381);
				BI_Pdftableoutput_row184.addCell(BI_Pdftable_output_row1_cell481);
				BI_Pdftableoutput_row184.addCell(BI_Pdftable_output_row1_cell681);
				BI_Pdftableoutput_row184.addCell(BI_Pdftable_output_row1_cell881);
				BI_Pdftableoutput_row184.addCell(BI_Pdftable_output_row1_cell581);

				BI_Pdftableoutput_row184.addCell(BI_Pdftable_output_row1_cell1181);
				BI_Pdftableoutput_row184.addCell(BI_Pdftable_output_row1_cell781);
				BI_Pdftableoutput_row184.addCell(BI_Pdftable_output_row1_cell8811);
				BI_Pdftableoutput_row184.addCell(BI_Pdftable_output_row1_cell981);

				BI_Pdftableoutput_row184.addCell(BI_Pdftable_output_row1_cell1081);
				BI_Pdftableoutput_row184.addCell(BI_Pdftable_output_row1_cell1281);

				BI_Pdftableoutput_row184.addCell(BI_Pdftable_output_row1_cell1381);
				BI_Pdftableoutput_row184.addCell(BI_Pdftable_output_row1_cell1481);

				document.add(BI_Pdftableoutput_row184);

			}
			document.add(new_line);
			document.add(note);
			document.add(table2);
			document.add(new_line);
			document.add(ls);
			document.add(table3);
			document.add(new_line);
			//Start 10Jan18
			PdfPTable BI_Pdftable_CompanysPolicySurrender2 = new PdfPTable(1);
			BI_Pdftable_CompanysPolicySurrender2.setWidthPercentage(100);

			PdfPCell imp = new PdfPCell(
					new Paragraph(
							"Important : ",
							small_normal));
			imp.setBackgroundColor(BaseColor.LIGHT_GRAY);

			PdfPCell BI_Pdftable_CompanysPolicySurrender2_cell = new PdfPCell(
					new Paragraph(
							"You may receive a Welcome Call from our representative to confirm your proposal details like Date of Birth, Nominee Name, Address, Email ID, Sum Assured, Premium amount, Premium Payment Term etc.",
							small_normal));

			BI_Pdftable_CompanysPolicySurrender2_cell.setPadding(5);

			BI_Pdftable_CompanysPolicySurrender2
					.addCell(imp);
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
							"If premium is greater than Rs 1Lakh. You have to submit Proof of source of Fund.",
							small_normal));

			BI_Pdftable_CompanysPolicySurrender4_cell.setPadding(5);

			BI_Pdftable_CompanysPolicySurrender4
					.addCell(BI_Pdftable_CompanysPolicySurrender4_cell);
			if (Double.parseDouble(edt_bi_smart_power_insurance_sum_assured_amount.getText().toString()) > 100000) {
				document.add(BI_Pdftable_CompanysPolicySurrender4);
			}


			PdfPTable BI_Pdftable_CompanysPolicySurrender5 = new PdfPTable(1);
			BI_Pdftable_CompanysPolicySurrender5.setWidthPercentage(100);
			PdfPCell BI_Pdftable_CompanysPolicySurrender5_cell = new PdfPCell(
					new Paragraph(Company_policy_surrender_dec, small_normal));

			BI_Pdftable_CompanysPolicySurrender5_cell.setPadding(5);

			BI_Pdftable_CompanysPolicySurrender5
					.addCell(BI_Pdftable_CompanysPolicySurrender5_cell);
			document.add(BI_Pdftable_CompanysPolicySurrender5);

			document.add(para_img_logo_after_space_1);
//End 10jan 18
			// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

          /*  Calendar present_date = Calendar.getInstance();
			int mDay = present_date.get(Calendar.DAY_OF_MONTH);
			int mMonth = present_date.get(Calendar.MONTH);
			int mYear = present_date.get(Calendar.YEAR);

            String CurrentDate = mDay + "-" + (mMonth + 1) + "-" + mYear;*/
			PdfPTable BI_Pdftable261 = new PdfPTable(1);
			BI_Pdftable261.setWidthPercentage(100);
			// CR_table6.setWidths(columnWidths_26);
			PdfPCell BI_Pdftable261_cell1 = new PdfPCell(
					new Paragraph(
							"I, "
									+ name_of_person
									+ "   , have received the information with respect to the above and have understood the above statement before entering into a contract.",
							small_bold));

			BI_Pdftable261_cell1.setPadding(5);

			BI_Pdftable261.addCell(BI_Pdftable261_cell1);
			document.add(BI_Pdftable261);
			document.add(BI_Pdftable261_cell1);
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
			PdfPTable BI_PdftablePolicyHolder_signature1;

			if (!bankUserType.equalsIgnoreCase("Y")) {
				BI_PdftablePolicyHolder_signature1 = new PdfPTable(3);
			} else {
				BI_PdftablePolicyHolder_signature1 = new PdfPTable(2);
			}

			BI_PdftablePolicyHolder_signature1.setWidthPercentage(100);

			PdfPCell BI_PdftablePolicyHolder_signature_11 = new PdfPCell(
					new Paragraph("Place :" + place2, small_normal));
			PdfPCell BI_PdftablePolicyHolder_signature_21 = new PdfPCell(
					new Paragraph("Date  :" + CurrentDate, small_normal));

			PdfPCell BI_PdftablePolicyHolder_signature_31 = new PdfPCell();
			if (!bankUserType.equalsIgnoreCase("Y")) {
				byte[] fbyt_Proposer = Base64.decode(proposer_sign, 0);
				Bitmap Proposerbitmap = BitmapFactory.decodeByteArray(
						fbyt_Proposer, 0, fbyt_Proposer.length);

				BI_PdftablePolicyHolder_signature_31.setFixedHeight(60f);
				ByteArrayOutputStream PolicyHolder_signature_stream = new ByteArrayOutputStream();

				(Proposerbitmap).compress(Bitmap.CompressFormat.PNG, 50,
						PolicyHolder_signature_stream);
				Image PolicyHolder_signature = Image
						.getInstance(PolicyHolder_signature_stream
								.toByteArray());

				BI_PdftablePolicyHolder_signature_31
						.setImage(PolicyHolder_signature);
			}

			BI_PdftablePolicyHolder_signature_11.setPadding(5);
			BI_PdftablePolicyHolder_signature_21.setPadding(5);

			BI_PdftablePolicyHolder_signature1
					.addCell(BI_PdftablePolicyHolder_signature_11);
			BI_PdftablePolicyHolder_signature1
					.addCell(BI_PdftablePolicyHolder_signature_21);
			if (!bankUserType.equalsIgnoreCase("Y")) {
				BI_PdftablePolicyHolder_signature1
						.addCell(BI_PdftablePolicyHolder_signature_31);
			}
			document.add(BI_PdftablePolicyHolder_signature1);
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
						}else{
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
						imageButtonSmartPowerInsuranceProposerPhotograph
								.setImageBitmap(scaled);
					} else {
						photoBitmap = null;
					}
				}
			}
		}
	}

	private void windowmessagesgin() {

		d = new Dialog(BI_SmartPowerInsuranceActivity.this);
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
				Intent intent = new Intent(BI_SmartPowerInsuranceActivity.this,
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
			edt_smart_power_insurance_contact_no
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
			edt_smart_power_insurance_Email_id
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
			if (!OldDate.equals("")) {
				DateFormat userDateFormat = new SimpleDateFormat("MM-dd-yyyy");
				DateFormat dateFormatNeeded = new SimpleDateFormat("dd/MM/yyyy");
				Date date = userDateFormat.parse(OldDate);

				NewDate = dateFormatNeeded.format(date);
			}
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

	private double getEffectivePremium() {
		if (spnr_bi_smart_power_insurance_premium_frequency.getSelectedItem()
				.toString().equals("Yearly")) {
			PF = 1;
		} else if (spnr_bi_smart_power_insurance_premium_frequency
				.getSelectedItem().toString().equals("Half Yearly")) {
			PF = 2;
		} else if (spnr_bi_smart_power_insurance_premium_frequency
				.getSelectedItem().toString().equals("Quarterly")) {
			PF = 4;
		} else if (spnr_bi_smart_power_insurance_premium_frequency
				.getSelectedItem().toString().equals("Monthly")) {
			PF = 12;
		}
		// System.out.println("PF " +PF);
		return Double
				.parseDouble(edt_bi_smart_power_insurance_sum_assured_amount
						.getText().toString())
				* PF;
	}

	// Validation
	private boolean valPolicyTermPlusAge() {
		if ((Integer.parseInt(edt_bi_smart_power_insurance_life_assured_age
				.getText().toString()) + Integer
				.parseInt(spnr_bi_smart_power_insurance_policyterm
						.getSelectedItem().toString())) > 65) {
			showAlert
					.setMessage("Addition of Policy Term & Age at Entry should be less than or equal to 65 Years");
			showAlert.setNeutralButton("OK",
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {
							spnr_bi_smart_power_insurance_policyterm
									.setSelection(0);
						}
					});
			showAlert.show();

			return false;
		} else {
			return true;
		}
	}

	// public void updateSAMFlabel() {
	// try {
	// help_SAMF.setText("(" + prop.minSAMF + " to " + prop.maxSAMF + ")");
	// } catch (Exception e) {
	// }
	// }
	//
	private void updatenoOfYearsElapsedSinceInception() {
		try {
			help_noOfYearsElapsedSinceInception.setText("(5 to "
					+ spnr_bi_smart_power_insurance_policyterm
					.getSelectedItem().toString() + " years)");
		} catch (Exception ignored) {
		}
	}

	private void updateSAMFlabel() {
		try {
			/** Modified by Akshaya on 24-08-2015 start **/
			//double minSAMF = Math.max(10, ((Double.parseDouble(spnr_bi_smart_power_insurance_policyterm.getSelectedItem().toString()) / 2)));
			/** Modified by Akshaya on 24-08-2015 end **/
			double minSAMF = 10;
			help_SAMF.setText("(" + minSAMF + " to " + prop.maxSAMF + ")");
		} catch (Exception ignored) {
		}
	}

	// public void updatePremiumAmtLabel() {
	// try {
	//
	// if (spnr_bi_smart_power_insurance_premium_frequency
	// .getSelectedItem().toString().equals("Yearly")) {
	// help_premAmt.setText("(Rs"
	// + currencyFormat.format(prop.minPremAmtYearly) + " to "
	// + prop.maxPremAmtYearly + ")");
	// } else if (spnr_bi_smart_power_insurance_premium_frequency
	// .getSelectedItem().toString().equals("Half Yearly")) {
	// help_premAmt.setText("(Rs."
	// + currencyFormat.format(prop.minPremAmtHalfYearly)
	// + " to " + prop.maxPremAmtHalfYearly + ")");
	// } else if (spnr_bi_smart_power_insurance_premium_frequency
	// .getSelectedItem().toString().equals("Quarterly")) {
	// help_premAmt.setText("(Rs."
	// + currencyFormat.format(prop.minPremAmtQuarterly)
	// + " to " + prop.maxPremAmtQuarterly + ")");
	// } else if (spnr_bi_smart_power_insurance_premium_frequency
	// .getSelectedItem().toString().equals("Monthly")) {
	// help_premAmt.setText("(Rs."
	// + currencyFormat.format(prop.minPremAmtMonthly)
	// + " to " + prop.maxPremAmtMonthly + ")");
	// }
	//
	// } catch (Exception e) {
	// }
	// }

	private void updatePremiumAmtLabel() {
		try {

			if (spnr_bi_smart_power_insurance_premium_frequency
					.getSelectedItem().toString().equals("Yearly")) {
				help_premAmt.setText("(Min. Rs. "
						+ currencyFormat.format(prop.minPremAmtYearly) + ")");
			} else if (spnr_bi_smart_power_insurance_premium_frequency
					.getSelectedItem().toString().equals("Half Yearly")) {
				help_premAmt.setText("(Min. Rs. "
						+ currencyFormat.format(prop.minPremAmtHalfYearly)
						+ ")");
			} else if (spnr_bi_smart_power_insurance_premium_frequency
					.getSelectedItem().toString().equals("Quarterly")) {
				help_premAmt
						.setText("(Min. Rs. "
								+ currencyFormat
								.format(prop.minPremAmtQuarterly) + ")");
			} else if (spnr_bi_smart_power_insurance_premium_frequency
					.getSelectedItem().toString().equals("Monthly")) {
				help_premAmt.setText("(Min. Rs."
						+ currencyFormat.format(prop.minPremAmtMonthly) + ")");
			}

		} catch (Exception ignored) {
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
									setFocusable(spnr_bi_smart_power_insurance_life_assured_title);
									spnr_bi_smart_power_insurance_life_assured_title
											.requestFocus();
								} else if (lifeAssured_First_Name.equals("")) {
									setFocusable(edt_bi_smart_power_insurance_life_assured_first_name);
									edt_bi_smart_power_insurance_life_assured_first_name
											.requestFocus();
								} else {
									setFocusable(edt_bi_smart_power_insurance_life_assured_last_name);
									edt_bi_smart_power_insurance_life_assured_last_name
											.requestFocus();
								}
							}
						});
				showAlert.show();

				return false;
			} else if (gender.equalsIgnoreCase("")) {
				showAlert.setMessage("Please Select Gender");
				showAlert.setNeutralButton("OK",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
												int which) {
								// apply focusable method
								setFocusable(spnr_bi_smart_power_insurance_life_assured_title);
								spnr_bi_smart_power_insurance_life_assured_title
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
								setFocusable(spnr_bi_smart_power_insurance_life_assured_title);
								spnr_bi_smart_power_insurance_life_assured_title
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
								setFocusable(spnr_bi_smart_power_insurance_life_assured_title);
								spnr_bi_smart_power_insurance_life_assured_title
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
								setFocusable(spnr_bi_smart_power_insurance_life_assured_title);
								spnr_bi_smart_power_insurance_life_assured_title
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
								setFocusable(btn_bi_smart_power_insurance_life_assured_date);
								btn_bi_smart_power_insurance_life_assured_date
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

		if (edt_smart_power_insurance_contact_no.getText().toString()
				.equals("")) {
			commonMethods.dialogWarning(context, "Please Fill  Mobile No", true);
			edt_smart_power_insurance_contact_no.requestFocus();
			return false;
		} else if (edt_smart_power_insurance_contact_no.getText().toString()
				.length() != 10) {
			commonMethods.dialogWarning(context, "Please Fill 10 Digit Mobile No", true);
			edt_smart_power_insurance_contact_no.requestFocus();
			return false;
		}

		/*else if (emailId.equals("")){
			commonMethods.dialogWarning(context, "Please Fill Email Id", true);
			edt_smart_power_insurance_Email_id.requestFocus();
			return false;

		}

		else if (ConfirmEmailId.equals("")) {

			commonMethods.dialogWarning(context, "Please Fill Confirm Email Id", true);
			edt_smart_power_insurance_ConfirmEmail_id.requestFocus();
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
					edt_smart_power_insurance_ConfirmEmail_id.requestFocus();
					return false;
				} else if (!ConfirmEmailId.equals(emailId)) {
					commonMethods.dialogWarning(context, "Email Id Does Not Match", true);
					return false;
				}

				return true;
			} else {
				commonMethods.dialogWarning(context, "Please Fill Valid Email Id", true);
				edt_smart_power_insurance_Email_id.requestFocus();
				return false;
			}
		}

		else if (!ConfirmEmailId.equals("")) {

			email_id_validation(ConfirmEmailId);
			if (validationFla1) {

				if (emailId.equals("")){
					commonMethods.dialogWarning(context, "Please Fill Email Id", true);
					edt_smart_power_insurance_Email_id.requestFocus();
					return false;

				}

				return true;
			} else {
				commonMethods.dialogWarning(context, "Please Fill Valid Confirm Email Id", true);
				edt_smart_power_insurance_ConfirmEmail_id.requestFocus();
				return false;
			}
		}

		else {
			return true;
		}
	}

	private boolean valPremiumAmt() {
		if (edt_bi_smart_power_insurance_sum_assured_amount.getText()
				.toString().equals("")) {
			showAlertMessage("Please enter Premium Amount in Rs. ");
			return false;
		} else if ((Double
				.parseDouble(edt_bi_smart_power_insurance_sum_assured_amount
						.getText().toString()) % 100) != 0) {
			showAlertMessage("Enter Premium Amount in multiples of 100");
			return false;
		} else if (spnr_bi_smart_power_insurance_premium_frequency
				.getSelectedItem().toString().equals("Yearly")
				&& (Double
				.parseDouble(edt_bi_smart_power_insurance_sum_assured_amount
						.getText().toString()) < prop.minPremAmtYearly)) {
			// showAlertMessage("Premium Amount should be in the range of Rs. "
			// + prop.minPremAmtYearly + " to Rs. " + prop.maxPremAmtYearly
			// + " for Yearly Mode");
			showAlertMessage("Minimum Premium Amount should be "
					+ prop.minPremAmtYearly);
			return false;
		} else if (spnr_bi_smart_power_insurance_premium_frequency
				.getSelectedItem().toString().equals("Half Yearly")
				&& ((Double
				.parseDouble(edt_bi_smart_power_insurance_sum_assured_amount
						.getText().toString()) < prop.minPremAmtHalfYearly))) {
			// showAlertMessage("Premium Amount should be in the range of Rs. "
			// + prop.minPremAmtHalfYearly + "to Rs. "
			// + prop.maxPremAmtHalfYearly + " for Half Yearly Mode");
			showAlertMessage("Minimum Premium Amount should be "
					+ prop.minPremAmtHalfYearly);
			return false;
		} else if (spnr_bi_smart_power_insurance_premium_frequency
				.getSelectedItem().toString().equals("Quarterly")
				&& ((Double
				.parseDouble(edt_bi_smart_power_insurance_sum_assured_amount
						.getText().toString()) < prop.minPremAmtQuarterly))) {
			// showAlertMessage("Premium Amount should be in the range of Rs. "
			// + prop.minPremAmtQuarterly + "to Rs. "
			// + prop.maxPremAmtQuarterly + " for Quarterly Mode");
			showAlertMessage("Minimum Premium Amount should be "
					+ prop.minPremAmtQuarterly);
			return false;
		} else if (spnr_bi_smart_power_insurance_premium_frequency
				.getSelectedItem().toString().equals("Monthly")
				&& ((Double
				.parseDouble(edt_bi_smart_power_insurance_sum_assured_amount
						.getText().toString()) < prop.minPremAmtMonthly))) {
			// showAlertMessage("Premium Amount should be in the range of Rs. "
			// + prop.minPremAmtMonthly + "to Rs. "
			// + prop.maxPremAmtMonthly + " for Monthly Mode");
			showAlertMessage("Minimum Premium Amount should be "
					+ prop.minPremAmtMonthly);
			return false;
		} else {
			return true;
		}
	}

	// // validating SAMF
	// public boolean valSAMF() {
	// if (edt_smart_power_insurance_samf.getText().toString().equals("")) {
	// showAlertMessage("Please Enter Sum Assured Multiple Factor(SAMF)");
	// return false;
	// } else if ((Double.parseDouble(edt_smart_power_insurance_samf.getText()
	// .toString()) < prop.minSAMF)
	// || (Double.parseDouble(edt_smart_power_insurance_samf.getText()
	// .toString()) > prop.maxSAMF)) {
	// showAlertMessage("Sum Assured Multiple Factor should be in the range of "
	// + prop.minSAMF + " to " + prop.maxSAMF);
	// return false;
	// } else {
	// return true;
	// }
	// }

	// validating SAMF
	private boolean valSAMF() {
		/** Modified by Akshaya on 24-08-2015 start **/
		//double minSAMF = Math.max(10, ((Double.parseDouble(spnr_bi_smart_power_insurance_policyterm.getSelectedItem().toString()) / 2)));
		/** Modified by Akshaya on 24-08-2015 end **/

		double minSAMF = 10;
		if (edt_smart_power_insurance_samf.getText().toString().equals("")) {
			showAlertMessage("Please Enter Sum Assured Multiple Factor(SAMF)");
			return false;
		} else if ((Double.parseDouble(edt_smart_power_insurance_samf.getText()
				.toString()) < minSAMF)
				|| (Double.parseDouble(edt_smart_power_insurance_samf.getText()
				.toString()) > prop.maxSAMF)) {
			showAlertMessage("Sum Assured Multiple Factor should be in the range of "
					+ minSAMF + " to " + prop.maxSAMF);
			return false;
		} else {
			return true;
		}
	}

	public boolean valTotalAllocation() {
		double equityFund, bondFund, top300Fund, equityOptimiseFund, growthFund, balancedFund, moneyMarketFund, bondOptimiserFund, corporateBondFund, pureFund;

		if (spnr_select_fund.getSelectedItem().toString().equals("Smart Funds")) {
			if (edt_smart_power_insurance_percent_EquityFund.getText()
					.toString().equals(""))
				equityFund = 0;
			else
				equityFund = Integer
						.parseInt(edt_smart_power_insurance_percent_EquityFund
								.getText().toString());

			if (edt_smart_power_insurance_percent_BondFund.getText().toString()
					.equals(""))
				bondFund = 0;
			else
				bondFund = Integer
						.parseInt(edt_smart_power_insurance_percent_BondFund
								.getText().toString());

			if (edt_smart_power_insurance_percent_top300Fund.getText()
					.toString().equals(""))
				top300Fund = 0;
			else
				top300Fund = Integer
						.parseInt(edt_smart_power_insurance_percent_top300Fund
								.getText().toString());

			if (edt_smart_power_insurance_percent_equityOptFund.getText()
					.toString().equals(""))
				equityOptimiseFund = 0;
			else
				equityOptimiseFund = Integer
						.parseInt(edt_smart_power_insurance_percent_equityOptFund
								.getText().toString());

			if (edt_smart_power_insurance_percent_growthFund.getText()
					.toString().equals(""))
				growthFund = 0;
			else
				growthFund = Integer
						.parseInt(edt_smart_power_insurance_percent_growthFund
								.getText().toString());

			if (edt_smart_power_insurance_percent_BalancedFund.getText()
					.toString().equals(""))
				balancedFund = 0;
			else
				balancedFund = Integer
						.parseInt(edt_smart_power_insurance_percent_BalancedFund
								.getText().toString());

			if (edt_smart_power_insurance_percent_moneyMktFund.getText()
					.toString().equals(""))
				moneyMarketFund = 0;
			else
				moneyMarketFund = Integer
						.parseInt(edt_smart_power_insurance_percent_moneyMktFund
								.getText().toString());

			if (!percent_BondOptimiserFund.getText().toString().equals(""))
				bondOptimiserFund = Double.parseDouble(percent_BondOptimiserFund.getText()
						.toString());
			else
				bondOptimiserFund = 0;

			if (!percent_CorporateBondFund.getText().toString().equals(""))
				corporateBondFund = Double.parseDouble(percent_CorporateBondFund.getText()
						.toString());
			else
				corporateBondFund = 0;


			if (!percent_PureFund.getText().toString().equals(""))
				pureFund = Double.parseDouble(percent_PureFund.getText()
						.toString());
			else
				pureFund = 0;

			if ((equityFund + bondFund + top300Fund + equityOptimiseFund
					+ growthFund + balancedFund + moneyMarketFund + bondOptimiserFund + corporateBondFund + pureFund) != 100) {
				showAlertMessage("Total sum of % to be invested for all fund should be equal to 100%");
				return false;
			}
		}
		return true;
	}

	public boolean valSumAssured(double SA) {
		//	if (SA > 10000000) {
		if (SA > 5000000) {
			showAlertMessage("Please choose the Premium or SAMF such that the SA is less than or equal to 50 lakh");
			return false;
		}
		return true;
	}

   /* private boolean valYearsElapsedSinceInception() {

		// System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!TERM"+term);
		if (edt_smart_power_insurance_noOfYearsElapsedSinceInception.getText()
				.toString().equals("")) {
			showAlertMessage("Enter No. of Years Elapsed Since Inception.");
			return false;
		} else if (Integer
				.parseInt(edt_smart_power_insurance_noOfYearsElapsedSinceInception
						.getText().toString()) < 5) {
			showAlertMessage("No. of Years Elapsed Since Inception should not be less than 5 Years ");
			return false;
		} else if (Integer
				.parseInt(edt_smart_power_insurance_noOfYearsElapsedSinceInception
						.getText().toString()) > Integer
				.parseInt(spnr_bi_smart_power_insurance_policyterm
						.getSelectedItem().toString())) {
			showAlertMessage("Please enter no. of Years in the range of 5 to "
					+ spnr_bi_smart_power_insurance_policyterm
					.getSelectedItem().toString() + "Years.");
			return false;
		} else {
			return true;
		}
    }*/

	private void showAlertMessage(String msg) {
		showAlert.setMessage(msg);
		showAlert.setNeutralButton("OK", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
			}
		});
		showAlert.show();
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
}
