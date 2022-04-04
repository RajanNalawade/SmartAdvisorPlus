package sbilife.com.pointofsale_bancaagency.smartchamp;

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

@SuppressWarnings("deprecation")
public class BI_SmartChampActivity extends AppCompatActivity implements
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
	private int flag = 0;

	// UI Elements
	private CheckBox isStaffDisc, isjkResident;
	private Spinner spnr_Gender;
	private Spinner spnr_Age;
	private Spinner spinner_selGender;
	private EditText edt_SumAssured;

	private Button btnSubmit;
	private Button btnback;
	private double staffRebate;
	// for BI
	private StringBuilder inputVal;
	private String premPayingTerm = "";
	private String premPayingMode = "";
	private String policyTermStr = "";
	private String ageAtEntry = "";
	private String gender = "";
	String premium = "";

	private String basicCoverTerm = "", basicCoverPremiumTerm = "";
	private String basicCoverSumAssured = "";
	private String basicCoverYearlyPremium = "";
	private String totalPremium_YearlyPrem_Prem = "";
	private String totalPremium_ServiceTax_Prem = "";
	private String totalPremium_YearlyPrem_With_ServiceTax_Prem = "";

	private String BackDateinterest, BackDateinterestwithGST;
	private String BackdatingInt;
	private String servcTaxSecondYear = "";
	private String premWthSTSecondYear = "";

	private SmartChampBean smartChampBean;

	// EditText edt_premiumAmt;

	private Spinner spnrPremPayingTerm;
	private Spinner spnrPremPayingMode;
	private Spinner spnrPolicyTerm;

	private RadioButton rb_proposerdetail_personaldetail_backdating_yes;
	private RadioButton rb_proposerdetail_personaldetail_backdating_no;

	private LinearLayout ll_backdating1;

	private Button btn_proposerdetail_personaldetail_backdatingdate;
	private Button btn_MarketingOfficalDate;
	private Button btn_PolicyholderDate;

	private ImageButton Ibtn_signatureofMarketing;
	private ImageButton Ibtn_signatureofPolicyHolders;


	private Dialog d;
	private  final int SIGNATURE_ACTIVITY = 1;
	private String latestImage = "";

	// List used for The Policy Detail Depend on The policy Term
	//List<M_BI_SmartChampInsurance_Adapter> list_data;

	List<M_BI_SmartChamp_Adapter> list_data;


	// Variable Used For Date Purpose
	private  final int DATE_DIALOG_ID = 1;
	private  int DIALOG_ID;
	private int mYear;
	private int mMonth;
	private int mDay;

	private Button btn_bi_smart_champ_insurance_life_assured_date;
	private Button btn_bi_smart_champ_insurance_proposer_date;

	private String QuatationNumber = "";
	private String planName = "";

	private TableRow tr_premium_paying_term;

	private String proposer_Title = "";
	private String proposer_First_Name = "";
	private String proposer_Middle_Name = "";
	private String proposer_Last_Name = "";

	private String lifeAssured_Title = "";
	private String lifeAssured_First_Name = "";
	private String lifeAssured_Middle_Name = "";
	private String lifeAssured_Last_Name = "";
	private String name_of_life_assured = "";
	private String lifeAssured_date_of_birth = "";
	private String lifeAssuredAge = "";

	private String proposer_Backdating_WishToBackDate_Policy = "";
	private String proposer_Backdating_BackDate = "";

	// Spinner USed
	private Spinner spnr_bi_smart_champ_insurance_life_assured_title;

	// edit Text Used
	private EditText edt_bi_smart_champ_insurance_life_assured_first_name;
	private EditText edt_bi_smart_champ_insurance_life_assured_middle_name;
	private EditText edt_bi_smart_champ_insurance_life_assured_last_name;

	private EditText edt_proposer_age;

	// Spinner USed
	private Spinner spnr_bi_smart_champ_insurance_proposer_title, spnr_bi_smart_champ_insurance_selGender;

	// edit Text Used
	private EditText edt_bi_smart_champ_insurance_proposer_first_name;
	private EditText edt_bi_smart_champ_insurance_proposer_middle_name;
	private EditText edt_bi_smart_champ_insurance_proposer_last_name;

	private String proposerAge = "";
	private String proposer_Age = "";

	private String proposer_date_of_birth = "";

	// Retrieving value from database and storing
	private String output = "";
	private String input = "";

	// For Bi Dialog
	private ParseXML prsObj;
	String proposal_no = "";
	private String name_of_proposer = "";
	private String name_of_person = "";
	String place1 = "";
	private String place2 = "";
	private String date1 = "";
	private String date2 = "";
	private String agent_sign = "";
	private String proposer_sign = "";
	private final String proposer_Is_Same_As_Life_Assured = "Y";

	// Class Declaration
	private CommonForAllProd commonForAllProd;
	private SmartChampProperties prop;

	// Variable Declaration
	private DecimalFormat currencyFormat;
	private AlertDialog.Builder showAlert;

	private StringBuilder bussIll = null;
	private StringBuilder retVal = null;

	private String PremFreqMode = "";
	private String staffdiscount = "";
	private File mypath;

	private ScrollView svSmartChmapMain;

	private boolean flagFirstFocus = true;

	String end_of_year = "";
	String total_base_premium = "";
	String guaranteed_death_benefit = "";
	String not_guaranteed_death_benefit_4per = "";
	String not_guaranteed_death_benefit_8per = "";
	String guaranteed_survival_benefit = "";
	String not_guaranteed_survival_benefit_4per = "";
	String not_guaranteed_survival_benefit_8per = "";
	String total_survival_benefit_4per = "";
	String total_survival_benefit_8per = "";

	String guaranteed_surrender_value = "";
	String not_guaranteed_surrender_value_4per = "";
	String not_guaranteed_surrender_value_8per = "";

	/* Basic Details */

	private EditText edt_proposerdetail_basicdetail_contact_no;
	private EditText edt_proposerdetail_basicdetail_Email_id;
	private EditText edt_proposerdetail_basicdetail_ConfirmEmail_id;

	private String mobileNo = "";
	private String emailId = "";
	private String ConfirmEmailId = "";
	private boolean validationFla1 = false;
	private String ProposerEmailId = "";
	private Bitmap photoBitmap;

	private String child_gender;

	/*********************** Added by Akshaya on 03-Mar-2015 start ****************/


	/*********************** Added by Akshaya on 03-Mar-2015 end ****************/


	private String bankUserType = "",mode = "";

	private String basicServiceTax = "", basicServiceTaxSecondYear = "";

	/* parivartan changes */
	private String Check = "";
	private CommonMethods commonMethods;
	private StorageUtils mStorageUtils;
	private ImageButton imageButtonSmartChampInsuranceProposerPhotograph;

	private LinearLayout linearlayoutThirdpartySignature,
			linearlayoutAppointeeSignature;
	private ImageButton Ibtn_signatureofThirdParty, Ibtn_signatureofAppointee;
	private String thirdPartySign = "", appointeeSign = "";
	private String product_Code, product_UIN, product_cateogory, product_type, Company_policy_surrender_dec = "";
	private Context context;

	String str_kerla_discount = "No";

	private String sum_assured_on_death = "";

	/* end */

	private CheckBox cb_kerladisc;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.bi_smartchamp_insurancemain);
		//getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);

		commonMethods = new CommonMethods();
		mStorageUtils = new StorageUtils();
		context = this;

		dbHelper = new DatabaseHelper(getApplicationContext());
		commonMethods.setApplicationToolbarMenu(this, getString(R.string.app_name));

		NABIObj = new NeedAnalysisBIService(this);
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
				// na_dob=intent.getStringExtra("custDOB");
				// gender=intent.getStringExtra("custGender");

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
					planName = "Smart Champ Insurance";
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
						product_Code, agentcode, zero + "");

			}
		} else
			needAnalysis_flag = 0;
		// ProductHomePageActivity.path.setText("Benefit Illustrator");

		// db = new newDBHelper(this);

		initialiseDate();
		prsObj = new ParseXML();

		commonForAllProd = new CommonForAllProd();
		prop = new SmartChampProperties();

		//list_data = new ArrayList<M_BI_SmartChampInsurance_Adapter>();

		list_data = new ArrayList<M_BI_SmartChamp_Adapter>();

		svSmartChmapMain = findViewById(R.id.sv_bi_smart_champ_main);

		rb_proposerdetail_personaldetail_backdating_yes = findViewById(R.id.rb_smart_champ_backdating_yes);
		rb_proposerdetail_personaldetail_backdating_no = findViewById(R.id.rb_smart_champ_backdating_no);

		ll_backdating1 = findViewById(R.id.ll_backdating1);
		btn_proposerdetail_personaldetail_backdatingdate = findViewById(R.id.btn_smart_champ_backdatingdate);
		btn_bi_smart_champ_insurance_life_assured_date = findViewById(R.id.btn_bi_smart_champ_insurance_life_assured_date);
		btn_bi_smart_champ_insurance_proposer_date = findViewById(R.id.btn_bi_smart_champ_insurance_proposer_date);

		spnr_bi_smart_champ_insurance_life_assured_title = findViewById(R.id.spnr_bi_smart_champ_insurance_life_assured_title);
		spnr_bi_smart_champ_insurance_proposer_title = findViewById(R.id.spnr_bi_smart_champ_insurance_proposer_title);
		spnr_bi_smart_champ_insurance_selGender = findViewById(R.id.spnr_bi_smart_champ_insurance_selGender);

		edt_bi_smart_champ_insurance_life_assured_first_name = findViewById(R.id.edt_bi_smart_champ_insurance_life_assured_first_name);
		edt_bi_smart_champ_insurance_life_assured_middle_name = findViewById(R.id.edt_bi_smart_champ_insurance_life_assured_middle_name);
		edt_bi_smart_champ_insurance_life_assured_last_name = findViewById(R.id.edt_bi_smart_champ_insurance_life_assured_last_name);

		edt_bi_smart_champ_insurance_proposer_first_name = findViewById(R.id.edt_bi_smart_champ_insurance_proposer_first_name);
		edt_bi_smart_champ_insurance_proposer_middle_name = findViewById(R.id.edt_bi_smart_champ_insurance_proposer_middle_name);
		edt_bi_smart_champ_insurance_proposer_last_name = findViewById(R.id.edt_bi_smart_champ_insurance_proposer_last_name);

		spnrPremPayingTerm = findViewById(R.id.spnr_bi_smart_champ_insurance_premium_paying_term);
		spnrPremPayingMode = findViewById(R.id.spnr_bi_smart_champ_insurance_premium_paying_mode);
		spnrPolicyTerm = findViewById(R.id.spnr_bi_smart_champ_insurance_policyterm);
		spnr_Gender = findViewById(R.id.spnr_bi_smart_champ_insurance_selGender);
//        spnr_Gender.setClickable(false);
//        spnr_Gender.setEnabled(false);
		spinner_selGender = findViewById(R.id.spinner_selGender);
//        spinner_selGender.setClickable(false);
//        spinner_selGender.setEnabled(false);

		spnr_Age = findViewById(R.id.spnr_bi_smart_champ_insurance_age);
		edt_proposer_age = findViewById(R.id.edt_bi_smart_champ_insurance_proposer_age);

		edt_proposerdetail_basicdetail_contact_no = findViewById(R.id.edt_smart_champ_contact_no);
		edt_proposerdetail_basicdetail_Email_id = findViewById(R.id.edt_smart_champ_Email_id);
		edt_proposerdetail_basicdetail_ConfirmEmail_id = findViewById(R.id.edt_smart_champ_ConfirmEmail_id);
		tr_premium_paying_term = findViewById(R.id.tr_premium_paying_term);

		// Calculate premium
		btnSubmit = findViewById(R.id.btn_bi_smart_champ_insurance_btnSubmit);
		btnback = findViewById(R.id.btn_bi_smart_champ_insurance_btnback);

		// Sum Assured
		edt_SumAssured = findViewById(R.id.et_bi_smart_champ_insurance_sum_assured);

		// tb_prempayingterm = (TableRow)
		// findViewById(R.id.tr_bi_smart_champ_insurance_premium_paying_term);

		isStaffDisc = findViewById(R.id.cb_staffdisc);
		isjkResident = findViewById(R.id.cb_bi_smart_champ_insurance_JkResident);

		// Variable Declaration
		currencyFormat = new DecimalFormat("##,##,##,###");
		showAlert = new AlertDialog.Builder(this);

		Context mContext = this;
		// Life Assured Age
		String[] LAageList = new String[prop.maxAgeLimitForProposer - 20 + 1];
		for (int i = 20; i <= prop.maxAgeLimitForProposer; i++) {
			LAageList[i - 20] = i + "";
		}

		ArrayAdapter<String> LifeAssuredAgeAdapter = new ArrayAdapter<>(
				getApplicationContext(), R.layout.spinner_item, LAageList);
		LifeAssuredAgeAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spnr_Age.setAdapter(LifeAssuredAgeAdapter);
		spnr_Age.setEnabled(false);

		LifeAssuredAgeAdapter.notifyDataSetChanged();

		String[] policyTermArray = new String[21];
		for (int i = 0; i <= 20; i++) {
			policyTermArray[i] = (i + 1) + "";

		}

		ArrayAdapter<String> policyTermAdapter = new ArrayAdapter<>(
				getApplicationContext(), R.layout.spinner_item, policyTermArray);
		policyTermAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spnrPolicyTerm.setAdapter(policyTermAdapter);
		policyTermAdapter.notifyDataSetChanged();
		spnrPolicyTerm.setEnabled(false);

		String[] premiumTermArray = new String[19];
		for (int i = 0; i <= 18; i++) {
			premiumTermArray[i] = (i + 1) + "";

		}

		ArrayAdapter<String> premiumTermAdapter = new ArrayAdapter<>(
				getApplicationContext(), R.layout.spinner_item,
				premiumTermArray);
		premiumTermAdapter
		.setDropDownViewResource(R.layout.spinner_item1);
		spnrPremPayingTerm.setAdapter(premiumTermAdapter);
		premiumTermAdapter.notifyDataSetChanged();
		spnrPremPayingTerm.setEnabled(false);

		// Gender
		String[] genderList = {"Male", "Female", "Third Gender"};
		ArrayAdapter<String> genderAdapter = new ArrayAdapter<>(getApplicationContext(),
				R.layout.spinner_item, genderList);
		genderAdapter.setDropDownViewResource(R.layout.spinner_item1);
		spnr_Gender.setAdapter(genderAdapter);
		spinner_selGender.setAdapter(genderAdapter);

		genderAdapter.notifyDataSetChanged();
		/*
		 * title_list.add("Gen."); title_list.add("Lt.Gen.");
		 * title_list.add("Maj.Gen."); title_list.add("Brig.");
		 * title_list.add("Col."); title_list.add("Lt.Col.");
		 * title_list.add("Major"); title_list.add("Capt.");
		 * title_list.add("Lt."); title_list.add("Gr.Capt.");
		 * title_list.add("Fr."); title_list.add("Dr.");
		 */

		commonMethods.fillSpinnerValue(context, spnr_bi_smart_champ_insurance_life_assured_title,
				Arrays.asList(getResources().getStringArray(R.array.arr_select_title)));

		commonMethods.fillSpinnerValue(context, spnr_bi_smart_champ_insurance_proposer_title,
				Arrays.asList(getResources().getStringArray(R.array.arr_select_title)));

		// Child Age
		String[] childAgeList = new String[prop.maxAgeLimitForChild
				- prop.minAgeLimitForChild + 1];
		for (int i = prop.minAgeLimitForChild; i <= prop.maxAgeLimitForChild; i++) {
			childAgeList[i] = i + "";
		}
		/*
		 * ArrayAdapter<String> proposerAgeAdapter = new ArrayAdapter<String>(
		 * getApplicationContext(), R.layout.spinner_item, childAgeList);
		 * proposerAgeAdapter
		 * .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item
		 * ); spnr_Age.setAdapter(proposerAgeAdapter);
		 * proposerAgeAdapter.notifyDataSetChanged();
		 */

		// premium Frequency Mode
		String[] premiumFrequencyList = { "Yearly", "Half Yearly", "Quarterly",
				"Monthly", "Single" };
		ArrayAdapter<String> premiumFrequencyAdapter = new ArrayAdapter<>(
				getApplicationContext(), R.layout.spinner_item,
				premiumFrequencyList);
		premiumFrequencyAdapter.setDropDownViewResource(R.layout.spinner_item1);
		spnrPremPayingMode.setAdapter(premiumFrequencyAdapter);
		premiumFrequencyAdapter.notifyDataSetChanged();

		/*********************** Item Listener starts from here ******************************************/

		// setBIInputGui();

		edt_bi_smart_champ_insurance_life_assured_first_name
				.setOnEditorActionListener(this);
		edt_bi_smart_champ_insurance_life_assured_middle_name
				.setOnEditorActionListener(this);
		edt_bi_smart_champ_insurance_life_assured_last_name
				.setOnEditorActionListener(this);
		edt_bi_smart_champ_insurance_proposer_first_name
				.setOnEditorActionListener(this);
		edt_bi_smart_champ_insurance_proposer_middle_name
				.setOnEditorActionListener(this);
		edt_bi_smart_champ_insurance_proposer_last_name
				.setOnEditorActionListener(this);
		edt_SumAssured.setOnEditorActionListener(this);

		setSpinnerAndOterListner();


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

		String str_usertype = "";
		TableRow tr_staff_disc;

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

		tr_staff_disc = findViewById(R.id.tr_smart_champ_staff_disc);
		try {
			str_usertype = SimpleCrypto.decrypt("SBIL",
					dbHelper.GetUserType());

		} catch (Exception e) {
			e.printStackTrace();
		}
		/*if (str_usertype.equalsIgnoreCase("BAP")
				|| str_usertype.equalsIgnoreCase("CAG")) {
			tr_staff_disc.setVisibility(View.GONE);
		}*/
		/********************** Item Listener ends here ******************************************/

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		boolean flagFocus = true;
		if (!flagFocus) {
			svSmartChmapMain.requestFocus();
		}

	}

	private void setSpinnerAndOterListner() {
		// TODO Auto-generated method stub


		cb_kerladisc.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (cb_kerladisc.isChecked()) {
					str_kerla_discount = "Yes";
					clearFocusable(cb_kerladisc);
					setFocusable(spnr_bi_smart_champ_insurance_life_assured_title);
					spnr_bi_smart_champ_insurance_life_assured_title.requestFocus();
				} else {
					str_kerla_discount = "No";
					clearFocusable(cb_kerladisc);
					setFocusable(spnr_bi_smart_champ_insurance_life_assured_title);
					spnr_bi_smart_champ_insurance_life_assured_title.requestFocus();
				}
			}
		});


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

							edt_proposer_age.setText(proposerAge);

							rb_proposerdetail_personaldetail_backdating_yes
									.setFocusable(false);

							clearFocusable(rb_proposerdetail_personaldetail_backdating_no);
							clearFocusable(rb_proposerdetail_personaldetail_backdating_yes);

						}
					}
				});

		spnr_bi_smart_champ_insurance_life_assured_title
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					public void onItemSelected(AdapterView<?> arg0, View arg1,
											   int position, long id) {
						// TODO Auto-generated method stub
						if (position == 0){
							lifeAssured_Title = "";
						}else {
							lifeAssured_Title = spnr_bi_smart_champ_insurance_life_assured_title
									.getSelectedItem().toString();
                        /*    if (lifeAssured_Title.equalsIgnoreCase("Mr.")) {
                                gender = "Male";
                                iv_men.setImageDrawable(getResources().getDrawable(
                                        R.drawable.male_selected));
                                iv_women.setImageDrawable(getResources().getDrawable(
                                        R.drawable.female_nonselected));
							} else if (lifeAssured_Title
									.equalsIgnoreCase("Ms.")) {
								spnr_Gender.setSelection(
										getIndex(spnr_Gender, "Female"), false);
							} else if (lifeAssured_Title
									.equalsIgnoreCase("Mrs.")) {
                                gender = "Female";
                                iv_women.setImageDrawable(getResources().getDrawable(
                                        R.drawable.female_selected));
                                iv_men.setImageDrawable(getResources().getDrawable(
                                        R.drawable.male_nonselected));
                            }*/
							clearFocusable(spnr_bi_smart_champ_insurance_life_assured_title);
							setFocusable(edt_bi_smart_champ_insurance_life_assured_first_name);

							edt_bi_smart_champ_insurance_life_assured_first_name
									.requestFocus();
						}

					}

					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub

					}
				});

		spnr_bi_smart_champ_insurance_proposer_title
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					public void onItemSelected(AdapterView<?> arg0, View arg1,
											   int position, long id) {
						// TODO Auto-generated method stub
						if (position == 0){
							proposer_Title = "";
						}else {
							proposer_Title = spnr_bi_smart_champ_insurance_proposer_title
									.getSelectedItem().toString();

							clearFocusable(spnr_bi_smart_champ_insurance_proposer_title);

							setFocusable(edt_bi_smart_champ_insurance_proposer_first_name);

							edt_bi_smart_champ_insurance_proposer_first_name
									.requestFocus();
						}

					}

					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub

					}
				});

		// Premium frequency mode
		spnrPremPayingMode
				.setOnItemSelectedListener(new OnItemSelectedListener() {
					public void onItemSelected(AdapterView<?> arg0, View arg1,
											   int pos, long id) {

						if (pos == 4) {
							tr_premium_paying_term.setVisibility(View.GONE);
							updatePremPayingTermLabel();

						} else {
							tr_premium_paying_term.setVisibility(View.VISIBLE);
							updatePremPayingTermLabel();

						}
						clearFocusable(spnrPremPayingMode);

						if (flagFirstFocus) {
							setFocusable(edt_bi_smart_champ_insurance_life_assured_first_name);
							edt_bi_smart_champ_insurance_life_assured_first_name
									.requestFocus();
							flagFirstFocus = false;
						} else {
							setFocusable(edt_SumAssured);
							edt_SumAssured.requestFocus();
						}

					}

					public void onNothingSelected(AdapterView<?> arg0) {
					}
				});

		// Staff Discount
		isStaffDisc.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				if (isStaffDisc.isChecked()) {
					isStaffDisc.setChecked(true);
					clearFocusable(spnr_bi_smart_champ_insurance_life_assured_title);
					setFocusable(spnr_bi_smart_champ_insurance_life_assured_title);
					spnr_bi_smart_champ_insurance_life_assured_title
							.requestFocus();

				}
			}
		});

		isjkResident.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (isjkResident.isChecked()) {
					isjkResident.setChecked(true);
				} else {
					isjkResident.setChecked(false);
				}
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

				gender = spnr_bi_smart_champ_insurance_selGender.getSelectedItem().toString();
				child_gender = spinner_selGender.getSelectedItem().toString();


				proposer_First_Name = edt_bi_smart_champ_insurance_proposer_first_name
                        .getText().toString().trim();
				proposer_Middle_Name = edt_bi_smart_champ_insurance_proposer_middle_name
                        .getText().toString().trim();
				proposer_Last_Name = edt_bi_smart_champ_insurance_proposer_last_name
                        .getText().toString().trim();

				name_of_proposer = proposer_Title + " " + proposer_First_Name
						+ " " + proposer_Middle_Name + " " + proposer_Last_Name;

				lifeAssured_First_Name = edt_bi_smart_champ_insurance_life_assured_first_name
                        .getText().toString().trim();
				lifeAssured_Middle_Name = edt_bi_smart_champ_insurance_life_assured_middle_name
                        .getText().toString().trim();
				lifeAssured_Last_Name = edt_bi_smart_champ_insurance_life_assured_last_name
                        .getText().toString().trim();

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
						&& valSumAssured() && valDoYouBackdate() && valBackdate() && TrueBackdate()) {
					Date();
					Log.e("ouput1:", output + "");
					boolean flag = addListenerOnSubmit();
					if (flag) {
						getInput(smartChampBean);
						// insertDataIntoDatabase();

						String policyTerm = smartChampBean
								.getPolicyTerm_Basic() + "";
						System.out.println("policyterm:" + policyTerm);
						if (needAnalysis_flag == 0) {
							System.out.println("guaranted:"
									+ prsObj.parseXmlTag(retVal.toString(),
									"guarntedDeathBenft" + policyTerm));
							Intent i = new Intent(getApplicationContext(),
									SmartChampSuccess.class);

							i.putExtra(
									"op",
									PremFreqMode
											+ " Premium is Rs. "
											+ currencyFormat.format(Double.parseDouble(prsObj
											.parseXmlTag(
													retVal.toString(),
													"yearlyPrem"))));
							i.putExtra(
									"op1",
									"Applicable Tax is Rs. "
											+ currencyFormat.format(Double.parseDouble(prsObj
											.parseXmlTag(
													retVal.toString(),
													"servcTax"))));
							i.putExtra(
									"op2",
									PremFreqMode
											+ " Premium with Applicable Tax is Rs. "
											+ currencyFormat.format(Double.parseDouble(prsObj
											.parseXmlTag(
													retVal.toString(),
													"premWthST"))));
							i.putExtra(
									"op3",
									""
											+ currencyFormat.format(Double.parseDouble(prsObj.parseXmlTag(
											retVal.toString(),
											"guarntedSurvivalBenft"
													+ policyTerm))));
							i.putExtra(
									"op4",
									""
											+ currencyFormat.format(Double.parseDouble(prsObj.parseXmlTag(
											retVal.toString(),
											"SurvivalBenefits-TotalSurvivalBenefit4per"
													+ policyTerm))));
							i.putExtra(
									"op5",
									""
											+ currencyFormat.format(Double.parseDouble(prsObj.parseXmlTag(
											retVal.toString(),
											"SurvivalBenefits-TotalSurvivalBenefit8per"
													+ policyTerm))));
							i.putExtra(
									"op6",
									""
											+ currencyFormat.format(Double.parseDouble(prsObj.parseXmlTag(
											retVal.toString(),
											"SurvivalBenefits-TotalSurvivalBenefit4per"
													+ (Integer
													.parseInt(policyTerm) - 1)))));// previous
							i.putExtra(
									"op7",
									""
											+ currencyFormat.format(Double.parseDouble(prsObj.parseXmlTag(
											retVal.toString(),
											"SurvivalBenefits-TotalSurvivalBenefit8per"
													+ (Integer
													.parseInt(policyTerm) - 1)))));// previous
							i.putExtra("policyTerm", ""
									+ spnrPolicyTerm.getSelectedItem()
									.toString());
							startActivity(i);
						} else
							Dialog();

					}
					// else {
					// showAlert(error);
					// }
				}

			}
		});

	}


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
						}else{
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
						imageButtonSmartChampInsuranceProposerPhotograph
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

		d.setContentView(R.layout.layout_smart_champ_insurance_bi_grid);

		TextView tv_channel_intermediary = (TextView) d
				.findViewById(R.id.tv_channel_intermediary);
		tv_channel_intermediary.setText(userType);

		TextView tv_proposername = d
				.findViewById(R.id.tv_smart_champ_insurance_proposername);
		TextView tv_proposal_number = d
				.findViewById(R.id.tv_smart_champ_insurance_proposal_number);

		TextView tv_life_assured_name = d
				.findViewById(R.id.tv_bi_smart_champ_insurance_life_assured_name);

		TextView tv_life_age_at_entry = d
				.findViewById(R.id.tv_bi_smart_champ_insurance_life_assured_age);

		TextView tv_proposer_age = d
				.findViewById(R.id.tv_bi_smart_champ_insurance_proposer_age);

		TextView tv_life_assured_gender = d
				.findViewById(R.id.tv_bi_smart_champ_insurance_life_assured_gender);

		TextView tv_premPaymentfrequency = d
				.findViewById(R.id.tv_bi_smart_champ_insurance_premium_paying_frequency);

		TextView tv_basic_cover_term = d
				.findViewById(R.id.tv_bi_smart_champ_insurance_term_basic_cover);

		TextView tv_basic_cover_premPayment_term = d
				.findViewById(R.id.tv_bi_smart_champ_insurance_premium_paying_term_basic_cover);

		TextView tv_basic_cover_sum_assured = d
				.findViewById(R.id.tv_bi_smart_champ_insurance_sum_assured_basic_cover);

		TextView tv_basic_cover_yearly_premium = d
				.findViewById(R.id.tv_bi_smart_champ_insurance_yearly_premium_basic_cover);

		TextView tv_total_premium_yearly_premium_term = d
				.findViewById(R.id.tv_smart_champ_term_insurance_yearly_premium_term_total_premium);

		TextView tv_total_premium_yearly_premium_premium = d
				.findViewById(R.id.tv_smart_champ_term_insurance_yearly_premium_premium_yearly);

		TextView tv_total_premium_service_tax_term = d
				.findViewById(R.id.tv_smart_champ_term_insurance_service_tax_term_total_premium);

		TextView tv_total_premium_service_tax_premium = d
				.findViewById(R.id.tv_smart_champ_term_insurance_service_tax_premium_yearly);

		TextView tv_total_premium_yearly_premium_with_service_tax_term = d
				.findViewById(R.id.tv_smart_champ_term_insurance_yealy_premium_with_service_tax_term_total_premium);

		TextView tv_total_premium_yearly_premium_with_service_tax_premium = d
				.findViewById(R.id.tv_smart_champ_term_insurance_yealy_premium_with_service_tax_premium_yearly);

		TextView tv_bi_smart_champ_premium_tag_basic_cover = d
				.findViewById(R.id.tv_bi_smart_champ_premium_tag_basic_cover);

		TextView tv_bi_smart_champ_year_tag_total_premium = d
				.findViewById(R.id.tv_bi_smart_champ_year_tag_total_premium);

		TextView tv_bi_smart_champ_pemium_with_service_tag_total_premium = d
				.findViewById(R.id.tv_bi_smart_champ_pemium_with_service_tag_total_premium);

		TextView tv_bi_smart_champ_pemium_tag_total_premium = d
				.findViewById(R.id.tv_bi_smart_champ_pemium_tag_total_premium);

		TextView tv_bi_smart_champ_gv_premium_tag = d
				.findViewById(R.id.tv_bi_smart_champ_gv_premium_tag);

		TextView tv_staff_or_not = d
				.findViewById(R.id.tv_bi_smart_champ_insurance_staff_or_not);

		TextView tv_bi_is_JK = d.findViewById(R.id.tv_jk);

		TextView tv_backdatingint = d
				.findViewById(R.id.tv_backdatingint);
		TextView txt_annulized_premium = (TextView) d
				.findViewById(R.id.txt_annulized_premium);
		TableRow txt_submit_proof = (TableRow) d
				.findViewById(R.id.txt_submit_proof);

		TextView tv_bi_smart_champ_life_assured_state = (TextView)
				d.findViewById(R.id.tv_bi_smart_champ_life_assured_state);

		TextView tv_bi_smart_champ_insurance_rate_of_applicable_tax = (TextView) d
				.findViewById(R.id.tv_bi_smart_champ_insurance_rate_of_applicable_tax);

		if (cb_kerladisc.isChecked()) {
			tv_bi_smart_champ_life_assured_state.setText("Kerala");
			tv_bi_smart_champ_insurance_rate_of_applicable_tax.setText("4.75% in the 1st policy year and 2.375% from 2nd policy year onwards");
		} else {
			tv_bi_smart_champ_life_assured_state.setText("Non Kerala");
			tv_bi_smart_champ_insurance_rate_of_applicable_tax.setText("4.5% in the 1st policy year and 2.25% from 2nd policy year onwards");
		}


		// Second year tables

		final TextView tv_premium_install_rider_type1 = d
				.findViewById(R.id.tv_premium_install_rider_type1);
		final TextView tv_mandatory_bi_smart_champ_insurance_yearly_premium_with_tax1 = d
				.findViewById(R.id.tv_mandatory_bi_smart_champ_insurance_yearly_premium_with_tax1);

		// First year policy
		TextView tv_bi_smart_champ_insurance_basic_premium_first_year = d
				.findViewById(R.id.tv_bi_smart_champ_insurance_basic_premium_first_year);
		TextView tv_bi_smart_champ_insurance_service_tax_first_year = d
				.findViewById(R.id.tv_bi_smart_champ_insurance_service_tax_first_year);
		TextView tv_bi_smart_champ_insurance_yearly_premium_with_tax_first_year = d
				.findViewById(R.id.tv_bi_smart_champ_insurance_yearly_premium_with_tax_first_year);

       /* TextView tv_bi_smart_champ_insurance_swachh_bharat_tax_first_year = d
				.findViewById(R.id.tv_bi_smart_champ_insurance_swachh_bharat_tax_first_year);

		TextView tv_bi_smart_champ_insurance_krishi_kalyan_tax_first_year = d
				.findViewById(R.id.tv_bi_smart_champ_insurance_krishi_kalyan_tax_first_year);

		TextView tv_bi_smart_champ_insurance_total_service_tax_first_year = d
				.findViewById(R.id.tv_bi_smart_champ_insurance_total_service_tax_first_year);
*/
		// Seconf year policy onwards
		TextView tv_bi_smart_champ_insurance_basic_premium_second_year = (TextView) d
				.findViewById(R.id.tv_bi_smart_champ_insurance_basic_premium_second_year);
		TextView tv_bi_smart_champ_insurance_service_tax_second_year = (TextView) d
				.findViewById(R.id.tv_bi_smart_champ_insurance_service_tax_second_year);
		TextView tv_bi_smart_champ_insurance_yearly_premium_with_tax_second_year = (TextView) d
				.findViewById(R.id.tv_bi_smart_champ_insurance_yearly_premium_with_tax_second_year);

		TableRow tr_second_year = (TableRow) d
				.findViewById(R.id.tr_second_year);


		TextView tv_bi_smart_bachat_child_name = (TextView) d
				.findViewById(R.id.tv_bi_smart_bachat_child_name);
		tv_bi_smart_bachat_child_name.setText(name_of_proposer);
		TextView tv_child_gender = (TextView) d
				.findViewById(R.id.tv_child_gender);

		tv_child_gender.setText(child_gender);

		TextView tv_bi_smart_champ_installment_premium = (TextView) d
				.findViewById(R.id.tv_bi_smart_champ_installment_premium);
		TextView tv_bi_smart_champ_insurance_sum_assured_on_death = (TextView) d
				.findViewById(R.id.tv_bi_smart_champ_insurance_sum_assured_on_death);

		TextView tv_bi_installment_premium_without_tax = (TextView) d
				.findViewById(R.id.tv_bi_installment_premium_without_tax);
		TextView tv_bi_total_installment_premium_without_tax = (TextView) d
				.findViewById(R.id.tv_bi_total_installment_premium_without_tax);
		TextView tv_bi_installment_premium_with_tax_first_year = (TextView) d
				.findViewById(R.id.tv_bi_installment_premium_with_tax_first_year);
		TextView tv_bi_total_installment_premium_with_tax_first_year = (TextView) d
				.findViewById(R.id.tv_bi_total_installment_premium_with_tax_first_year);
		TextView tv_bi_installment_premium_with_tax_second_year = (TextView) d
				.findViewById(R.id.tv_bi_installment_premium_with_tax_second_year);
		TextView tv_bi_total_installment_premium_with_tax_second_year = (TextView) d
				.findViewById(R.id.tv_bi_total_installment_premium_with_tax_second_year);


     /*   TextView tv_bi_smart_champ_insurance_swachh_bharat_tax_second_year = d
				.findViewById(R.id.tv_bi_smart_champ_insurance_swachh_bharat_tax_second_year);

		TextView tv_bi_smart_champ_insurance_basic_service_tax_second_year = (TextView) d
				.findViewById(R.id.tv_bi_smart_champ_insurance_basic_service_tax_second_year);
		TextView tv_bi_smart_champ_insurance_swachh_bharat_cess_second_year = (TextView) d
				.findViewById(R.id.tv_bi_smart_champ_insurance_swachh_bharat_cess_second_year);
		TextView tv_bi_smart_champ_insurance_krishi_kalyan_cess_second_year = (TextView) d
				.findViewById(R.id.tv_bi_smart_champ_insurance_krishi_kalyan_cess_second_year);*/

		TextView tv_uin_smart_champ = (TextView) d
				.findViewById(R.id.tv_uin_smart_champ);

		GridView gv_userinfo = d
				.findViewById(R.id.gv_smart_champ_insurance_userinfo);

		gv_userinfo.setVerticalScrollBarEnabled(true);
		gv_userinfo.setSmoothScrollbarEnabled(true);

		final EditText edt_Policyholderplace = d
				.findViewById(R.id.edt_Policyholderplace);

		final TextView edt_proposer_name = d
				.findViewById(R.id.edt_ProposalName);
		final CheckBox cb_statement = d
				.findViewById(R.id.cb_statement);
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
		TableRow tr_source_of_fund = d
				.findViewById(R.id.tr_source_of_fund);

		/* parivartan changes */
		imageButtonSmartChampInsuranceProposerPhotograph = d
				.findViewById(R.id.imageButtonSmartChampInsuranceProposerPhotograph);
		imageButtonSmartChampInsuranceProposerPhotograph
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
							+ ", have undergone the Need Analysis after having reviewed the SBI Life Product options, I have opted for SBI LIFE - Smart Champ Insurance.");

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
							+ ", have undergone the Need Analysis after having reviewed the SBI Life Product options, I have opted for SBI LIFE - Smart Champ Insurance.");

			tv_proposername.setText(name_of_proposer);
		}

		TextView textviewAGentStatement = d.findViewById(R.id.textviewAGentStatement);
		textviewAGentStatement.setText(commonMethods.getAgentDeclaration(context));

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
							commonMethods.dialogWarning(context,"Please Tick on I Agree Clause ", true);
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
							commonMethods.dialogWarning(context,"Please Tick on I Agree Clause ", true);
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
							commonMethods.dialogWarning(context,"Please Tick on I Agree Clause ", true);
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
			imageButtonSmartChampInsuranceProposerPhotograph
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
							commonMethods.dialogWarning(context,"Please Tick on I Agree Clause ", true);
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
							imageButtonSmartChampInsuranceProposerPhotograph
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

		if(!TextUtils.isEmpty(place2)){
			edt_Policyholderplace.setText(place2);
		}

		btn_proceed.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// if (frmProductHome.equals("FALSE")) {
				name_of_person = edt_proposer_name.getText().toString();
				// place1 = edt_MarketingOfficalPlace.getText().toString();
				place2 = edt_Policyholderplace.getText().toString();
				premPayingMode = prsObj.parseXmlTag(inputVal.toString(), "premFreq");

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
					String productCode = "SCI";

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
									.getRound(totalPremium_YearlyPrem_With_ServiceTax_Prem),
							emailId, mobileNo, agentEmail, agentMobile,
							na_input, na_output, premPayingMode, Integer
							.parseInt(policyTermStr), 0, productCode,
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
									.getRound(totalPremium_YearlyPrem_With_ServiceTax_Prem),
							agentEmail, agentMobile, na_input, na_output,
							premPayingMode, Integer.parseInt(policyTermStr), 0,
							productCode, getDate(lifeAssured_date_of_birth),
							getDate(proposer_date_of_birth), "",mode,inputVal
							.toString(), retVal.toString().replace(
							bussIll.toString(), "")));

					createPdf();
					NABIObj.serviceHit(BI_SmartChampActivity.this, na_cbi_bean,
							newFile, needAnalysispath.getPath(),
							mypath.getPath(), name_of_person, QuatationNumber,mode);
					d.dismiss();

				} else {

					if (proposer_sign.equals("")
							&& !bankUserType.equalsIgnoreCase("Y")) {
						commonMethods.dialogWarning(context,"Please Make Signature for Proposer ", true);
						setFocusable(Ibtn_signatureofPolicyHolders);
						Ibtn_signatureofPolicyHolders.requestFocus();
					} else if (place2.equals("")) {
						commonMethods.dialogWarning(context,"Please Fill Place Detail", true);
						setFocusable(edt_Policyholderplace);
						edt_Policyholderplace.requestFocus();

					} else if (agent_sign.equals("")
							&& !bankUserType.equalsIgnoreCase("Y")) {
						commonMethods.dialogWarning(context,"Please Make Signature for Sales Representative",
								true);
						setFocusable(Ibtn_signatureofMarketing);
						Ibtn_signatureofMarketing.requestFocus();
					}

					else if (!cb_statement.isChecked()) {
						commonMethods.dialogWarning(context,"Please Tick on I Agree Clause ", true);
						setFocusable(cb_statement);
						cb_statement.requestFocus();
					}else if (!checkboxAgentStatement.isChecked()) {
						commonMethods.dialogWarning(context, "Please Tick on I Agree Clause ", true);
						commonMethods.setFocusable(checkboxAgentStatement);
						checkboxAgentStatement.requestFocus();
					}else if (!radioButtonTrasactionModeParivartan.isChecked()
							&& !radioButtonTrasactionModeManual.isChecked()) {
						commonMethods.dialogWarning(context,"Please Select Transaction Mode", true);
						setFocusable(linearlayoutTrasactionModeParivartan);
						linearlayoutTrasactionModeParivartan.requestFocus();
					} else if (photoBitmap == null) {
						commonMethods.dialogWarning(context,"Please Capture the Photo", true);
						setFocusable(imageButtonSmartChampInsuranceProposerPhotograph);
						imageButtonSmartChampInsuranceProposerPhotograph
								.requestFocus();
					} else if (!radioButtonDepositPaymentYes.isChecked()
							&& !radioButtonDepositPaymentNo.isChecked()) {
						commonMethods.dialogWarning(context,"Please Select Third Party Payment", true);
						setFocusable(linearlayoutThirdpartySignature);
						linearlayoutThirdpartySignature.requestFocus();
					} else if (radioButtonDepositPaymentNo.isChecked()
							&& thirdPartySign.equals("")) {
						commonMethods.dialogWarning(context,"Please Make Signature for Third party ", true);
						setFocusable(Ibtn_signatureofThirdParty);
						Ibtn_signatureofThirdParty.requestFocus();
					} else if (!radioButtonAppointeeYes.isChecked()
							&& !radioButtonAppointeeNo.isChecked()) {
						commonMethods.dialogWarning(context,"Please Select Appointee Payment", true);
						setFocusable(linearlayoutAppointeeSignature);
						linearlayoutAppointeeSignature.requestFocus();
					} else if (radioButtonAppointeeYes.isChecked()
							&& appointeeSign.equals("")) {
						commonMethods.dialogWarning(context,"Please Make Signature for Appointee ", true);
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

		input = inputVal.toString();
		output = retVal.toString();

		staffdiscount = prsObj.parseXmlTag(input, "isStaff");
		if (staffdiscount.equals("true")) {
			//tv_staff_or_not.setText("Staff");
			tv_staff_or_not.setText("Yes");
		} else {
			//tv_staff_or_not.setText("Non - Staff");
			tv_staff_or_not.setText("No");
		}

		str_kerla_discount = prsObj.parseXmlTag(input, "str_kerla_discount");
		premPayingMode = prsObj.parseXmlTag(input, "premFreq");
		if (str_kerla_discount.equalsIgnoreCase("Yes")) {
			if (premPayingMode.equals("Single")) {
				tv_bi_smart_champ_insurance_rate_of_applicable_tax.setText("4.75%");
			} else {
				tv_bi_smart_champ_insurance_rate_of_applicable_tax.setText(" 4.75% in the 1st policy year and 2.375% from 2nd policy year onwards");
			}
		} else {
			if (premPayingMode.equals("Single")) {
				tv_bi_smart_champ_insurance_rate_of_applicable_tax.setText("4.50%");
			} else {
				tv_bi_smart_champ_insurance_rate_of_applicable_tax.setText("4.5% in the 1st policy year and 2.25% from 2nd policy year onwards");
			}
		}

		String isJkResident = prsObj.parseXmlTag(input, "isJKResident");

		if (isJkResident.equalsIgnoreCase("true")) {
			tv_bi_is_JK.setText("Yes");
		} else {
			tv_bi_is_JK.setText("No");
		}

		tv_uin_smart_champ
				.setText("Benefit Illustration(BI): SBI Life -Smart Champ Insurance (UIN : 111N098V03)" + "\n" + "An Individual, Non-Linked, Participating, Life Insurance Savings Product ");

		ageAtEntry = prsObj.parseXmlTag(input, "age");
		tv_life_age_at_entry.setText(ageAtEntry + " Years");

		proposer_Age = prsObj.parseXmlTag(input, "ageProposer");
		tv_proposer_age.setText(proposer_Age + " Years");

		gender = prsObj.parseXmlTag(input, "gender");
		tv_life_assured_gender.setText(gender);

		premPayingMode = prsObj.parseXmlTag(input, "premFreq");
		tv_premPaymentfrequency.setText(premPayingMode);

		if (premPayingMode.equalsIgnoreCase("Half Yearly")) {
			tv_bi_smart_champ_premium_tag_basic_cover
					.setText("Premium Half Yearly (Rs)");
			tv_bi_smart_champ_year_tag_total_premium
					.setText("Premium Half Yearly  (Rs)");
			tv_bi_smart_champ_pemium_with_service_tag_total_premium
					.setText("Half Yearly Premium with Applicable Taxes");
			tv_bi_smart_champ_pemium_tag_total_premium
					.setText("Half Yearly Premium");
			tv_premium_install_rider_type1.setText("Half Yearly Premium (Rs.)");
			tv_mandatory_bi_smart_champ_insurance_yearly_premium_with_tax1
					.setText("Half Yearly Premium with Applicable Taxes(Rs.)");
			txt_annulized_premium.setText("Annualized Premium");
		} else if (premPayingMode.equalsIgnoreCase("Quarterly")) {
			tv_bi_smart_champ_premium_tag_basic_cover
					.setText("Premium Quaterly (Rs)");
			tv_bi_smart_champ_year_tag_total_premium
					.setText("Premium Quaterly (Rs)");
			tv_bi_smart_champ_pemium_with_service_tag_total_premium
					.setText("Quaterly Premium with Applicable Taxes");
			tv_bi_smart_champ_pemium_tag_total_premium
					.setText("Quaterly Premium");
			tv_premium_install_rider_type1.setText("Quaterly Premium (Rs.)");
			tv_mandatory_bi_smart_champ_insurance_yearly_premium_with_tax1
					.setText("Quaterly Premium with Applicable Taxes(Rs.)");
			txt_annulized_premium.setText("Annualized Premium");
		} else if (premPayingMode.equalsIgnoreCase("Monthly")) {
			tv_bi_smart_champ_premium_tag_basic_cover
					.setText("Premium Monthly (Rs)");
			tv_bi_smart_champ_year_tag_total_premium.setText("Premium Monthly (Rs)");
			tv_bi_smart_champ_pemium_with_service_tag_total_premium
					.setText("Monthly Premium with Applicable Taxes");
			tv_bi_smart_champ_pemium_tag_total_premium
					.setText("Monthly Premium");
			tv_premium_install_rider_type1.setText("Monthly Premium (Rs.)");
			tv_mandatory_bi_smart_champ_insurance_yearly_premium_with_tax1
					.setText("Monthly Premium with Applicable Taxes(Rs.)");
			txt_annulized_premium.setText("Annualized Premium");
		} else if (premPayingMode.equalsIgnoreCase("Single")) {
			tv_bi_smart_champ_premium_tag_basic_cover.setText("Premium Single (Rs)");
			tv_bi_smart_champ_year_tag_total_premium.setText("Premium Single (Rs)");
			tv_bi_smart_champ_pemium_with_service_tag_total_premium
					.setText("Single Premium with Applicable Taxes");
			tv_bi_smart_champ_pemium_tag_total_premium
					.setText("Single Premium");
			tv_bi_smart_champ_gv_premium_tag.setText("Single Premium");
			tv_premium_install_rider_type1.setText("Single Premium (Rs.)");
			tv_mandatory_bi_smart_champ_insurance_yearly_premium_with_tax1
					.setText("Single Premium with Applicable Taxes(Rs.)");

			txt_annulized_premium.setText("Single Premium");

		}

		premPayingTerm = prsObj.parseXmlTag(input, "premPayingTerm");

		if (!(premPayingMode.equalsIgnoreCase("Single"))) {
			tv_basic_cover_premPayment_term.setText(premPayingTerm + " Years");
		} else {
			tv_basic_cover_premPayment_term.setText("1 Year");
		}

		policyTermStr = prsObj.parseXmlTag(input, "policyTerm");
		tv_basic_cover_term.setText(policyTermStr + " Years");

		basicCoverYearlyPremium = ((int) Double.parseDouble(prsObj.parseXmlTag(
				output, "yearlyPrem"))) + "";
		tv_basic_cover_yearly_premium.setText("Rs. " + getformatedThousandString(Integer.parseInt(basicCoverYearlyPremium)));

		tv_total_premium_yearly_premium_premium.setText("Rs. "
				+ getformatedThousandString(Integer.parseInt(basicCoverYearlyPremium)));

		basicCoverSumAssured = ((int) Double.parseDouble(prsObj.parseXmlTag(
				input, "sumAssured"))) + "";
		tv_basic_cover_sum_assured.setText("Rs. "
				+ getformatedThousandString(Integer
				.parseInt(basicCoverSumAssured)));

		tv_total_premium_yearly_premium_term.setText(policyTermStr + " Years");
		tv_total_premium_service_tax_term.setText(policyTermStr + " Years");
		tv_total_premium_yearly_premium_with_service_tax_term
				.setText(policyTermStr + " Years");

		totalPremium_YearlyPrem_Prem = ((int) Double.parseDouble(prsObj
				.parseXmlTag(output, "yearlyPrem"))) + "";
		totalPremium_ServiceTax_Prem = ((int) Double.parseDouble(prsObj
				.parseXmlTag(output, "servcTax"))) + "";
		totalPremium_YearlyPrem_With_ServiceTax_Prem = ((int) Double
				.parseDouble(prsObj.parseXmlTag(output, "premWthST"))) + "";

		tv_total_premium_yearly_premium_premium.setText("Rs. "
				+ getformatedThousandString(Integer.parseInt( totalPremium_YearlyPrem_Prem)));
		tv_total_premium_service_tax_premium.setText("Rs. "
				+ getformatedThousandString(Integer.parseInt( totalPremium_ServiceTax_Prem)));
		tv_total_premium_yearly_premium_with_service_tax_premium.setText("Rs. "
				+ getformatedThousandString(Integer.parseInt(totalPremium_YearlyPrem_With_ServiceTax_Prem)));


		tv_bi_smart_champ_installment_premium.setText("Rs. "
				+ getformatedThousandString(Integer.parseInt(basicCoverYearlyPremium)));


		sum_assured_on_death = ((int) Double.parseDouble(prsObj
				.parseXmlTag(output, "guarntedDeathBenft" + 1 + ""))) + "";


		tv_bi_smart_champ_insurance_sum_assured_on_death.setText("Rs. "
				+ getformatedThousandString(Integer.parseInt(sum_assured_on_death)));
		tv_bi_installment_premium_without_tax.setText("Rs. "
				+ getformatedThousandString(Integer.parseInt(basicCoverYearlyPremium)));

		tv_bi_total_installment_premium_without_tax.setText("Rs. "
				+ getformatedThousandString(Integer.parseInt(basicCoverYearlyPremium)));

		tv_bi_installment_premium_with_tax_first_year.setText("Rs. "
				+ getformatedThousandString(Integer.parseInt(totalPremium_YearlyPrem_With_ServiceTax_Prem)));

		tv_bi_total_installment_premium_with_tax_first_year.setText("Rs. "
				+ getformatedThousandString(Integer.parseInt(totalPremium_YearlyPrem_With_ServiceTax_Prem)));


		// Second year onwards
		servcTaxSecondYear = (int) Double.parseDouble(prsObj.parseXmlTag(output, "servcTaxSeondYear")) + "";
		premWthSTSecondYear = (int) Double.parseDouble(prsObj.parseXmlTag(output, "premWthSTSecondYear")) + "";


		tv_bi_installment_premium_with_tax_second_year.setText("Rs. "
				+ getformatedThousandString(Integer.parseInt(premWthSTSecondYear)));


		tv_bi_total_installment_premium_with_tax_second_year.setText("Rs. "
				+ getformatedThousandString(Integer.parseInt(premWthSTSecondYear)));

		if (smartChampBean.getPremFreqMode().equalsIgnoreCase("Single")) {
			tv_bi_installment_premium_with_tax_second_year.setText("Not Applicable");


			tv_bi_total_installment_premium_with_tax_second_year.setText("Not Applicable");
		}


		tv_bi_smart_champ_insurance_basic_premium_first_year.setText(""
				+ getformatedThousandString(Integer.parseInt(totalPremium_YearlyPrem_Prem)));

		tv_bi_smart_champ_insurance_service_tax_first_year.setText(""
				+ getformatedThousandString(Integer.parseInt( totalPremium_ServiceTax_Prem)));

		tv_bi_smart_champ_insurance_yearly_premium_with_tax_first_year
				.setText("" + getformatedThousandString(Integer.parseInt( totalPremium_YearlyPrem_With_ServiceTax_Prem)));

		// Amit changes start- 23-5-2016
		// basicServiceTax = prsObj.parseXmlTag(output, "basicServiceTax");
		basicServiceTax = prsObj.parseXmlTag(output, "servcTax");

		/*tv_bi_smart_champ_insurance_basic_service_tax_first_year.setText(""
				+ commonForAllProd.getRound(commonForAllProd
						.getStringWithout_E(Double.valueOf(basicServiceTax
								.equals("") ? "0" : basicServiceTax))));

		SBCServiceTax = prsObj.parseXmlTag(output, "SBCServiceTax");

		tv_bi_smart_champ_insurance_swachh_bharat_cess_first_year.setText(""
				+ commonForAllProd.getRound(commonForAllProd
						.getStringWithout_E(Double.valueOf(SBCServiceTax
								.equals("") ? "0" : SBCServiceTax))));

		KKCServiceTax = prsObj.parseXmlTag(output, "KKCServiceTax");

		tv_bi_smart_champ_insurance_krishi_kalyan_cess_first_year.setText(""
				+ commonForAllProd.getRound(commonForAllProd
						.getStringWithout_E(Double.valueOf(KKCServiceTax
								.equals("") ? "0" : KKCServiceTax))));*/
		// Amit changes end- 23-5-2016

		tr_second_year.setVisibility(View.GONE);

		if (!smartChampBean.getPremFreqMode().equalsIgnoreCase("Single")) {
			tr_second_year.setVisibility(View.VISIBLE);
			tv_bi_smart_champ_insurance_basic_premium_second_year.setText(""
					+ getformatedThousandString(Integer.parseInt( totalPremium_YearlyPrem_Prem)));


			tv_bi_smart_champ_insurance_service_tax_second_year
					.setText("" + getformatedThousandString(Integer.parseInt(servcTaxSecondYear)));

			tv_bi_smart_champ_insurance_yearly_premium_with_tax_second_year
					.setText("" +  getformatedThousandString(Integer.parseInt(premWthSTSecondYear)));

			// Amit changes start- 23-5-2016
			// basicServiceTaxSecondYear = prsObj.parseXmlTag(output,
			// "basicServiceTaxSecondYear");
			basicServiceTaxSecondYear = prsObj.parseXmlTag(output,
					"servcTaxSeondYear");

			/*tv_bi_smart_champ_insurance_basic_service_tax_second_year
					.setText(""
							+ commonForAllProd.getRound(commonForAllProd.getStringWithout_E(Double
									.valueOf(basicServiceTaxSecondYear
											.equals("") ? "0"
											: basicServiceTaxSecondYear))));

			SBCServiceTaxSecondYear = prsObj.parseXmlTag(output,
					"SBCServiceTaxSecondYear");

			tv_bi_smart_champ_insurance_swachh_bharat_cess_second_year
					.setText(""
							+ commonForAllProd.getRound(commonForAllProd
									.getStringWithout_E(Double
											.valueOf(SBCServiceTaxSecondYear
													.equals("") ? "0"
													: SBCServiceTaxSecondYear))));

			KKCServiceTaxSecondYear = prsObj.parseXmlTag(output,
					"KKCServiceTaxSecondYear");

			tv_bi_smart_champ_insurance_krishi_kalyan_cess_second_year
					.setText(""
							+ commonForAllProd.getRound(commonForAllProd
									.getStringWithout_E(Double
											.valueOf(KKCServiceTaxSecondYear
													.equals("") ? "0"
													: KKCServiceTaxSecondYear))));*/
			// Amit changes end- 23-5-2016

		}

		BackdatingInt = prsObj.parseXmlTag(output, "backdateInt");

		tv_backdatingint.setText("Rs. "
				+ commonForAllProd.getRound(commonForAllProd
				.getStringWithout_E(Double.valueOf(BackdatingInt
						.equals("") ? "0" : BackdatingInt))));

		TextView tv_Company_policy_surrender_dec = d
				.findViewById(R.id.tv_Company_policy_surrender_dec);

		String str_prem_paying = "";

		if (premPayingMode.equalsIgnoreCase("Single")) {
			str_prem_paying = "Single";
		} else {
			str_prem_paying = "Limited";

		}


		/*if(str_prem_paying.equalsIgnoreCase("Single")) {

			Company_policy_surrender_dec = "Your SBI Life - Smart Champ Insurance (UIN No: 111N098V03) is a "
					+ str_prem_paying
					+ " premium policy, for which your first year "
					+ premPayingMode
					+ " premium is Rs "
					+ getformatedThousandString(Integer.parseInt(totalPremium_YearlyPrem_With_ServiceTax_Prem))
					+" .Your Policy Term is "
					+ policyTermStr
					+ " years"
					+ " and Basic Sum Assured is Rs. "
					+ getformatedThousandString(Integer
					.parseInt(basicCoverSumAssured));
		}

		else {
			Company_policy_surrender_dec = "Your SBI Life - Smart Champ Insurance (UIN No: 111N098V03) is a "
					+ str_prem_paying
					+ " premium policy, for which your first year "
					+ premPayingMode
					+ " premium is Rs "
					+ getformatedThousandString(Integer.parseInt(totalPremium_YearlyPrem_With_ServiceTax_Prem))
					+" .Your Policy Term is "
					+ policyTermStr
					+ " years"
					+ " ,your Premium Payment Term is "
					+ premPayingTerm
					+ " years"
					+ " and Basic Sum Assured is Rs. "
					+ getformatedThousandString(Integer
					.parseInt(basicCoverSumAssured));
		}*/

       /* if (str_prem_paying.equalsIgnoreCase("Single")) {

            Company_policy_surrender_dec = "Your SBI Life - Smart Champ Insurance (UIN No: 111N098V03) is a "
					+ str_prem_paying
					+ " premium policy and you are required to pay One Time Premium of Rs. "
					+ getformatedThousandString(Integer.parseInt(totalPremium_YearlyPrem_With_ServiceTax_Prem))
					+ " .Your Policy Term is "
					+ policyTermStr
					+ " years"
					+ " and Basic Sum Assured is Rs. "
					+ getformatedThousandString(Integer
					.parseInt(basicCoverSumAssured));
		}

		else {
            Company_policy_surrender_dec = "Your SBI Life - Smart Champ Insurance (UIN No: 111N098V03) is a "
					+ str_prem_paying
					+ " premium policy, for which your first year "
					+ premPayingMode
					+ " premium is Rs "
					+ getformatedThousandString(Integer.parseInt(totalPremium_YearlyPrem_With_ServiceTax_Prem))
					+ " .Your Policy Term is "
					+ policyTermStr
					+ " years"
					+ " , Premium Payment Term is "
					+ premPayingTerm
					+ " years"
					+ " and Basic Sum Assured is Rs. "
					+

					getformatedThousandString(Integer
							.parseInt(basicCoverSumAssured));
		}
        tv_Company_policy_surrender_dec.setText(Company_policy_surrender_dec);

		/*if (Integer.parseInt(policyTermStr) <= 5) {
			gv_userinfo.getLayoutParams().height = 270;

		} else if (Integer.parseInt(policyTermStr) <= 10) {

			gv_userinfo.getLayoutParams().height = 270 * 2;

		}

		else if (Integer.parseInt(policyTermStr) <= 15) {

			gv_userinfo.getLayoutParams().height = 270 * 3;

		}

		else if (Integer.parseInt(policyTermStr) <= 20) {

			gv_userinfo.getLayoutParams().height = 270 * 4;

		}

		else if (Integer.parseInt(policyTermStr) <= 25) {

			gv_userinfo.getLayoutParams().height = 270 * 5;

		}

		else if (Integer.parseInt(policyTermStr) <= 30) {

			gv_userinfo.getLayoutParams().height = 270 * 6;

		} else if (Integer.parseInt(policyTermStr) <= 35) {

			gv_userinfo.getLayoutParams().height = 270 * 7;

		}*/

       /*  for (int i = 1; i <= Integer.parseInt(policyTermStr); i++) {

			String end_of_year = prsObj.parseXmlTag(output, "policyYr" + i + "");
			String total_base_premium = ((int) Double.parseDouble(prsObj.parseXmlTag(
					output, "totBasePremPaid" + i + ""))) + "";
			String guaranteed_death_benefit = ((int) Double.parseDouble(prsObj
					.parseXmlTag(output, "guarntedDeathBenft" + i + ""))) + "";
			String not_guaranteed_death_benefit_4per = ((int) Double
					.parseDouble(prsObj.parseXmlTag(output,
							"nonGuarntedDeathBenft_4_pr" + i + "")))
					+ "";
			String not_guaranteed_death_benefit_8per = ((int) Double
					.parseDouble(prsObj.parseXmlTag(output,
							"nonGuarntedDeathBenft_8_pr" + i + "")))
					+ "";
			String guaranteed_survival_benefit = ((int) Double.parseDouble(prsObj
					.parseXmlTag(output, "guarntedSurvivalBenft" + i + "")))
					+ "";
			String not_guaranteed_survival_benefit_4per = ((int) Double
					.parseDouble(prsObj.parseXmlTag(output,
							"nonGuarntedSurvivalBenft_4_pr" + i + "")))
					+ "";
			String not_guaranteed_survival_benefit_8per = ((int) Double
					.parseDouble(prsObj.parseXmlTag(output,
							"nonGuarntedSurvivalBenft_8_pr" + i + "")))
					+ "";

			String total_survival_benefit_4per = ((int) Double.parseDouble(prsObj
					.parseXmlTag(output, "totalSurvivalBenft_4_pr" + i + "")))
					+ "";
			String total_survival_benefit_8per = ((int) Double.parseDouble(prsObj
					.parseXmlTag(output, "totalSurvivalBenft_8_pr" + i + "")))
					+ "";

			String guaranteed_surrender_value = ((int) Double.parseDouble(prsObj
					.parseXmlTag(output, "gurntedSSV" + i + ""))) + "";
			String not_guaranteed_surrender_value_4per = ((int) Double
					.parseDouble(prsObj.parseXmlTag(output,
							"nonGurntedSSV_4_pr" + i + "")))
					+ "";
			String not_guaranteed_surrender_value_8per = ((int) Double
					.parseDouble(prsObj.parseXmlTag(output,
							"nonGurntedSSV_8_pr" + i + "")))
					+ "";

			list_data.add(new M_BI_SmartChampInsurance_Adapter(end_of_year,
					total_base_premium, guaranteed_death_benefit,
					not_guaranteed_death_benefit_4per,
					not_guaranteed_death_benefit_8per,
					guaranteed_survival_benefit,
					not_guaranteed_survival_benefit_4per,
					not_guaranteed_survival_benefit_8per,
					total_survival_benefit_4per, total_survival_benefit_8per,
					guaranteed_surrender_value,
					not_guaranteed_surrender_value_4per,
					not_guaranteed_surrender_value_8per));
		}

		*/
		if (Double.parseDouble(basicCoverYearlyPremium) > 100000) {
			txt_submit_proof.setVisibility(View.VISIBLE);
		} else {
			txt_submit_proof.setVisibility(View.GONE);
		}

		String GuarateedAddition = "";
		String MaturityBenefit = "";
		String ReversionaryBonus4Per = "";

		for (int i = 1; i <= Integer.parseInt(policyTermStr); i++) {

			end_of_year = prsObj.parseXmlTag(output, "policyYr" + i + "");
			total_base_premium = ((int) Double.parseDouble(prsObj.parseXmlTag(
					output, "annulizedPremium" + i + ""))) + "";

			GuarateedAddition = ((int) Double.parseDouble(prsObj
					.parseXmlTag(output, "GuarateedAddition" + i + ""))) + "";


			guaranteed_survival_benefit = ((int) Double.parseDouble(prsObj
					.parseXmlTag(output, "guarntedSurvivalBenft" + i + "")))
					+ "";

			guaranteed_surrender_value = ((int) Double.parseDouble(prsObj
					.parseXmlTag(output, "GuarateedSSV" + i + ""))) + "";


			guaranteed_death_benefit = ((int) Double.parseDouble(prsObj
					.parseXmlTag(output, "guarntedDeathBenft" + i + ""))) + "";
			MaturityBenefit = ((int) Double
					.parseDouble(prsObj.parseXmlTag(output, "MaturityBenefit" + i
							+ "")))
					+ "";


			ReversionaryBonus4Per = ((int) Double
					.parseDouble(prsObj.parseXmlTag(output,
							"ReversionaryBonus4Per" + i + "")))
					+ "";

			not_guaranteed_survival_benefit_4per = ((int) Double
					.parseDouble(prsObj.parseXmlTag(output,
							"nonGuarntedSurvivalBenft_4_pr" + i + "")))
					+ "";

			not_guaranteed_surrender_value_4per = ((int) Double
					.parseDouble(prsObj.parseXmlTag(output,
							"nonGurntedSSV_4_pr" + i + "")))
					+ "";


			String ReversionaryBonus8Per = ((int) Double
					.parseDouble(prsObj.parseXmlTag(output,
							"ReversionaryBonus8Per" + i + "")))
					+ "";

			not_guaranteed_survival_benefit_8per = ((int) Double
					.parseDouble(prsObj.parseXmlTag(output,
							"nonGuarntedSurvivalBenft_8_pr" + i + "")))
					+ "";


			String not_guaranteed_surrender_value_8per = ((int) Double
					.parseDouble(prsObj.parseXmlTag(output,
							"nonGurntedSSV_8_pr" + i + "")))
					+ "";


			String total_survival_benefit_4per = ((int) Double
					.parseDouble(prsObj.parseXmlTag(output,
							"SurvivalBenefits-TotalSurvivalBenefit4per" + i + "")))
					+ "";

			String total_survival_benefit_8per = ((int) Double
					.parseDouble(prsObj.parseXmlTag(output,
							"SurvivalBenefits-TotalSurvivalBenefit8per" + i + "")))
					+ "";

			String TotalMaturityBenefit4per = ((int) Double
					.parseDouble(prsObj.parseXmlTag(output,
							"TotalMaturityBenefit4per" + i + "")))
					+ "";

			String TotalMaturityBenefit8per = ((int) Double
					.parseDouble(prsObj.parseXmlTag(output,
							"TotalMaturityBenefit8per" + i + "")))
					+ "";

			String TotalDeathBenefit4per = ((int) Double
					.parseDouble(prsObj.parseXmlTag(output,
							"TotalDeathBenefit4per" + i + "")))
					+ "";

			String TotalDeathBenefit8per = ((int) Double
					.parseDouble(prsObj.parseXmlTag(output,
							"TotalDeathBenefit8per" + i + "")))
					+ "";


			list_data.add(new M_BI_SmartChamp_Adapter(end_of_year,
					total_base_premium,
					GuarateedAddition,
					guaranteed_survival_benefit,
					guaranteed_surrender_value,
					guaranteed_death_benefit,
					MaturityBenefit,
					ReversionaryBonus4Per,
					"0",
					not_guaranteed_survival_benefit_4per,
					not_guaranteed_surrender_value_4per,
					ReversionaryBonus8Per,
					"0",
					not_guaranteed_survival_benefit_8per,
					not_guaranteed_surrender_value_8per,
					total_survival_benefit_4per,
					total_survival_benefit_8per,
					TotalMaturityBenefit4per,
					TotalMaturityBenefit8per,
					TotalDeathBenefit4per,
					TotalDeathBenefit8per));
		}

		Adapter_BI_SmartChampGrid adapter = new Adapter_BI_SmartChampGrid(
				this, list_data);
		gv_userinfo.setAdapter(adapter);

		GridHeight gh = new GridHeight();
		gh.getheight(gv_userinfo, policyTermStr);
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
		String formattedDate = df.format(c.getTime());
		// formattedDate have current date/time
		// Toast.makeText(this, formattedDate, Toast.LENGTH_SHORT).show();
		c.add(Calendar.DATE, 30);
		String expiredDate = df.format(c.getTime());
	}


	// Store user input in Bean object
	private boolean addListenerOnSubmit() {
		// Insert data entered by user in an object
		smartChampBean = new SmartChampBean();
		if (isStaffDisc.isChecked()) {
			smartChampBean.setIsForStaffOrNot(true);
		} else {
			smartChampBean.setIsForStaffOrNot(false);
		}

		if(cb_kerladisc.isChecked())
		{
			smartChampBean.setKerlaDisc(true);
		}
		else
		{
			smartChampBean.setKerlaDisc(false);
		}

		if (isjkResident.isChecked()) {
			smartChampBean.setIsJKResidentDiscOrNot(true);
		} else {
			smartChampBean.setIsJKResidentDiscOrNot(false);
		}

		smartChampBean.setAgeOfChild(Integer.parseInt(edt_proposer_age
				.getText().toString()));

		smartChampBean.setGender(spnr_Gender.getSelectedItem().toString());

		smartChampBean.setAgeOfProposer(Integer.parseInt(spnr_Age
				.getSelectedItem().toString()));

		smartChampBean.setPremFreqMode(spnrPremPayingMode.getSelectedItem()
				.toString());
		smartChampBean.setPolicyTerm_Basic(Integer.parseInt(spnrPolicyTerm
				.getSelectedItem().toString()));

		smartChampBean.setPremiumPayingTerm(18 - Integer
				.parseInt(edt_proposer_age.getText().toString()));
		// smartChampBean.setPremiumPayingTerm(Integer.parseInt(spnrPremPayingTerm.getSelectedItem().toString()));

		smartChampBean.setsumAssured(Double.parseDouble(edt_SumAssured
				.getText().toString()));

		// PremFreqMode = smartchampbean.getPremFreqMode();
		// policyTerm = smartchampbean.getPolicyTerm_Basic();
		// Show Smart Scholar Output Screen
		return showSmartChampOutputPg(smartChampBean);

	}

	private void getInput(SmartChampBean smartChampBean) {
		inputVal = new StringBuilder();
		String proposer_title = spnr_bi_smart_champ_insurance_life_assured_title
				.getSelectedItem().toString();
		String proposer_firstName = edt_bi_smart_champ_insurance_life_assured_first_name
				.getText().toString();
		String proposer_middleName = edt_bi_smart_champ_insurance_life_assured_middle_name
				.getText().toString();
		String proposer_lastName = edt_bi_smart_champ_insurance_life_assured_last_name
				.getText().toString();
		String proposer_DOB = btn_bi_smart_champ_insurance_life_assured_date
				.getText().toString();
		String proposer_age = spnr_Age.getSelectedItem().toString();
		String proposer_gender = child_gender;

//        String LifeAssured_title = "";
		String LifeAssured_firstName = "";
		String LifeAssured_middleName = "";
		String LifeAssured_lastName = "";
		String LifeAssured_DOB = "";
		String LifeAssured_age = "";

		String LifeAssured_gender = spnr_Gender.getSelectedItem()
				.toString();

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

       /* if (!spnr_bi_smart_champ_insurance_proposer_title.getSelectedItem()
				.toString().equals("")
				&& !spnr_bi_smart_champ_insurance_proposer_title
				.getSelectedItem().toString().equals("Select Title")) {
			LifeAssured_title = spnr_bi_smart_champ_insurance_proposer_title
					.getSelectedItem().toString();
			if (LifeAssured_title.equals("Mr."))
				LifeAssured_gender = "Male";
			else
				LifeAssured_gender = "Female";
        }*/
		if (!edt_bi_smart_champ_insurance_proposer_first_name.getText()
				.toString().equals(""))
			LifeAssured_firstName = edt_bi_smart_champ_insurance_proposer_first_name
					.getText().toString();

		if (!edt_bi_smart_champ_insurance_proposer_middle_name.getText()
				.toString().equals(""))
			LifeAssured_middleName = edt_bi_smart_champ_insurance_proposer_middle_name
					.getText().toString();

		if (!edt_bi_smart_champ_insurance_proposer_last_name.getText()
				.toString().equals(""))
			LifeAssured_lastName = edt_bi_smart_champ_insurance_proposer_last_name
					.getText().toString();

		if (!btn_bi_smart_champ_insurance_proposer_date.getText().toString()
				.equals("Select Date")) {
			Calendar present_date = Calendar.getInstance();
			int tDay = present_date.get(Calendar.DAY_OF_MONTH);
			int tMonth = present_date.get(Calendar.MONTH);
			int tYear = present_date.get(Calendar.YEAR);
			LifeAssured_DOB = btn_bi_smart_champ_insurance_proposer_date
					.getText().toString();
			LifeAssured_age = commonForAllProd.calculateMyAge(tYear,
					tMonth + 1, tDay, getDate1(LifeAssured_DOB)) + "";
		}

		int age_of_child = smartChampBean.getAgeOfChild();
		int age_of_parent = smartChampBean.getAgeOfProposer();
		String gender = smartChampBean.getGender();
		int policyTerm = smartChampBean.getPolicyTerm_Basic();
		// double premAmount = smartChampBean.getPremiumAmount();
		String PremPayingMode = smartChampBean.getPremFreqMode();
		int premPayingTerm = smartChampBean.getPremiumPayingTerm();
		double sumAssured = smartChampBean.getsumAssured();
		boolean JandKResident = smartChampBean.getIsJKResidentDiscOrNot();
		boolean isStaffOrNot = smartChampBean.getIsForStaffOrNot();

		inputVal.append("<?xml version='1.0' encoding='utf-8' ?><smartchampinsurance>");

		inputVal.append("<LifeAssured_title>").append(lifeAssured_Title).append("</LifeAssured_title>");
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

		inputVal.append("<ageProposer>").append(age_of_child).append("</ageProposer>");
		inputVal.append("<age>").append(age_of_parent).append("</age>");
		inputVal.append("<gender>").append(gender).append("</gender>");

		inputVal.append("<policyTerm>").append(policyTerm).append("</policyTerm>");
		inputVal.append("<isJKResident>").append(JandKResident).append("</isJKResident>");
		inputVal.append("<isStaff>").append(isStaffOrNot).append("</isStaff>");
		inputVal.append("<str_kerla_discount>" + str_kerla_discount + "</str_kerla_discount>");
		inputVal.append("<premFreq>").append(PremPayingMode).append("</premFreq>");
		inputVal.append("<sumAssured>").append(sumAssured).append("</sumAssured>");
		inputVal.append("<premPayingTerm>").append(premPayingTerm).append("</premPayingTerm>");
		inputVal.append("<ChildGender>" + child_gender
				+ "</ChildGender>");
		inputVal.append("<Wish_to_backdate_policy>").append(wish_to_backdate_flag).append("</Wish_to_backdate_policy>");
		inputVal.append("<backdating_Date>").append(backdate).append("</backdating_Date>");


		//Added Tushar Kadam Kerla Applicable Tax 7-Jun-2019
//        String str_kerla_discount = "N";
		if (cb_kerladisc.isChecked()) {
			str_kerla_discount = "Y";
		}

		inputVal.append("<KFC>" + str_kerla_discount + "</KFC>");
		//End Added Tushar Kadam Kerla Applicable Tax 7-Jun-2019
		inputVal.append("</smartchampinsurance>");

	}

	/********************************** Output starts here **********************************************************/
	// Display Smart Scholar Output Screen
	private boolean showSmartChampOutputPg(SmartChampBean smartchampbean1) {

		retVal = new StringBuilder();
		// ArrayList<String[]> arrList=new ArrayList<String[]>();

		String premiumBasic = "" + smartchampbean1.getPremiumAmount();


		String[] outputArr = getOutput("BI_of_Smart_Champ", smartchampbean1);
		if (valPremAmount(Double.parseDouble(outputArr[0]))) {

			try {

				retVal.append("<?xml version='1.0' encoding='utf-8' ?><Smartchampinsurance>");
				retVal.append("<errCode>0</errCode>");

				if (isStaffDisc.isChecked()) {
					retVal.append("<staffStatus>sbi</staffStatus>");
				} else {
					retVal.append("<staffStatus>none</staffStatus>");
				}
				retVal.append("<staffRebate>").append(staffRebate).append("</staffRebate>");
				/************* mofified by Akshaya on 18-Feb-15 start **************/
				retVal.append("<yearlyPrem>").append(Double.parseDouble(outputArr[0])).append("</yearlyPrem>")
						.append("<servcTax>").append(Double.parseDouble(outputArr[1])).append("</servcTax>")
						.append("<premWthST>").append(Double.parseDouble(outputArr[2])).append("</premWthST>")
						.append("<installmntPrem>").append(outputArr[8]).append("</installmntPrem>")
						.append("<backdateInt>").append(outputArr[9]).append("</backdateInt>")
						.append("<servcTaxSeondYear>").append(outputArr[10]).append("</servcTaxSeondYear>")
						.append("<premWthSTSecondYear>").append(outputArr[11]).append("</premWthSTSecondYear>")
						.append("<basicServiceTax>").append(outputArr[12]).append("</basicServiceTax>")
						.append("<SBCServiceTax>").append(outputArr[13]).append("</SBCServiceTax>")
						.append("<KKCServiceTax>").append(outputArr[14]).append("</KKCServiceTax>")
						.append("<basicServiceTaxSecondYear>").append(outputArr[15]).append("</basicServiceTaxSecondYear>")
						.append("<SBCServiceTaxSecondYear>").append(outputArr[16]).append("</SBCServiceTaxSecondYear>")
						.append("<KKCServiceTaxSecondYear>").append(outputArr[17]).append("</KKCServiceTaxSecondYear>")

						.append("<KeralaCessServiceTax>"+ outputArr[18]+"</KeralaCessServiceTax>")
						.append("<KeralaCessServiceTaxSecondYear>"+ outputArr[19]+"</KeralaCessServiceTaxSecondYear>");

				int index = smartchampbean1.getPolicyTerm_Basic();
				String nonGuarntedSurvivalBenft_4_pr  = prsObj.parseXmlTag(bussIll.toString(), "nonGuarntedSurvivalBenft_4_pr" + index + "");
				String nonGuarntedSurvivalBenft_8_pr = prsObj.parseXmlTag(bussIll.toString(),"nonGuarntedSurvivalBenft_8_pr" + index + "");

				retVal.append("<nonGuarntedSurvivalBenft_4_pr"+index+">"+nonGuarntedSurvivalBenft_4_pr+"</nonGuarntedSurvivalBenft_4_pr"+index+">");
				retVal.append("<nonGuarntedSurvivalBenft_8_pr"+index+">"+nonGuarntedSurvivalBenft_8_pr+"</nonGuarntedSurvivalBenft_8_pr"+index+">");

				retVal.append(bussIll.toString());
				retVal.append("</Smartchampinsurance>");
				/************* mofified by Akshaya on 18-Feb-15 end **************/

			} catch (Exception e) {

				retVal.append("<?xml version='1.0' encoding='utf-8' ?><Smartchampinsurance>" + "<errCode>1</errCode>" + "<errorMessage>").append(e.getMessage()).append("</errorMessage></Smartchampinsurance>");
			}
			return true;
		} else {
			return false;
		}

	}

	/******************************** Output ends here **********************************************************/

	/********************************** Calculations starts from here **********************************************************/

	private String[] getOutput(String sheetName, SmartChampBean smartchampbean1) {

		/*************** mofified by Akshaya on 18-Feb-15 start **************/
		String premiumWithoutSTDisc = "0", BackDateinterest = "0" ,MinesOccuInterest = "0", servicetax_MinesOccuInterest = "0" ;
		double staffRebate = 0;
		/*************** mofified by Akshaya on 18-Feb-15 end **************/

		bussIll = new StringBuilder();
		int _year_F = 0;

		int year_F = 0;

		double totalBasePremiumPaid = 0, TotalBasePremiumPaidWithoutFreqLoading = 0,/*GuaranteedSSV=0*/GuaranteedDeathBenefit = 0, SAMultipleForDeathBenifitToBeConsider = 0, APWithoutModalLoading = 0, NonGuarateedDeathBenefitAt_4_Percent = 0, NonGuarateedDeathBenefitAt_8_Percent = 0, GuaranteedSurvivalBenefit = 0, NonGuarateedSurvivalBenefitAt_4_Percent = 0, NonGuarateedSurvivalBenefitAt_8_Percent = 0, TotalSurvivalBenefitAt_4_Percent = 0, TotalSurvivalBenefitAt_8_Percent = 0, GuarateedSSV = 0, NonGuarateedSSV_4_Pr = 0, NonGuarateedSSV_8_Pr = 0, _NonGuarateedDeathBenefitAt_4_Percent = 0, _NonGuarateedDeathBenefitAt_8_Percent = 0;
		double premiumwithRoundUP = 0, PremiumRoundUpWithoutLoading = 0, premiumWithSTFirstYear = 0, premiumWithSTSecondYear = 0, FirstYear_ST = 0, SecondYear_ST = 0, premiumWithoutST = 0;

		double sumAnnualizedPrem = 0, FirstYearOfTotBasePrem = 0, FirstYearOfAnnulizedPrem = 0, sumSurvivalBenefit = 0, GuarateedAddition = 0, sumsurvivalbenyerr20 = 0, sumsurvivalben8perYear20 = 0;

//added by sujata on 27-11-2019
		String GuaranteedSSV = null, nonGuarateedSSV_4_Pr = null, nonGuarateedSSV_8_Pr = null, MaturityBenefit = null, PremiumBasic = null, setPremiumPaidFactor = null, TotalSurvivalBenefit4per = null, TotalSurvivalBenefit8per = null, ReversionaryBonus4Per = null, ReversionaryBonus8Per = null, TotalDeathBenefit4per = null, TotalDeathBenefit8per = null, TotalMaturityBenefit4per = null, TotalMaturityBenefit8per = null, TotalBasePremiumPaid = null, totalBasePremiumPaidWithoutFreqLoading = null, AnnulizedPremium = null;

		SmartChampBusinessLogic smartchampBusinessLogic = new SmartChampBusinessLogic();
		// From GUI Input
		boolean staffDisc = smartchampbean1.getIsForStaffOrNot();
		boolean JKResidentDisc = smartchampbean1.getIsJKResidentDiscOrNot();
		String premFreqMode = smartchampbean1.getPremFreqMode();
		int premiumPayingTerm = smartchampbean1.getPremiumPayingTerm();
		int ageOfChild = smartchampbean1.getAgeOfChild();
		int ageOfProposer = smartchampbean1.getAgeOfProposer();
		int policyTerm = smartchampbean1.getPolicyTerm_Basic();
		double sumAssured = smartchampbean1.getsumAssured();
//End


		staffRebate = smartchampBusinessLogic.getStaffRebate(premFreqMode,
				staffDisc);
		staffRebate = staffRebate * 100;

		double[] deathbenefitArr = new double[policyTerm * 5];

		premiumWithoutST = Double.valueOf(smartchampBusinessLogic
				.setPremiumWithoutSTwithoutRoundUP(ageOfProposer, policyTerm,
						sumAssured, staffDisc, premFreqMode));

		/*************** mofified by Akshaya on 18-Feb-15 start **************/
		premiumWithoutSTDisc = commonForAllProd.getRoundUp(commonForAllProd
				.getRoundOffLevel2(commonForAllProd
						.getStringWithout_E(smartchampBusinessLogic
								.setPremiumWithoutSTAndDiscwithoutRoundUP(
										ageOfProposer, policyTerm, sumAssured,
										staffDisc, premFreqMode))));
		/*************** mofified by Akshaya on 18-Feb-15 end **************/

		/*************** mofified by Akshaya on 18-Feb-15 start **************/
		staffRebate = smartchampBusinessLogic.getStaffRebate(premFreqMode,
				staffDisc);
		TotalBasePremiumPaidWithoutFreqLoading = smartchampBusinessLogic.set_Premium_Without_ST_WithoutLoadingFreq(ageOfProposer, policyTerm, premFreqMode, "Basic", sumAssured, staffRebate);
		// staffRebate=staffRebate*100;
		/*************** mofified by Akshaya on 18-Feb-15 end **************/

		bussIll.append("<staffRebate>" + staffRebate + "</staffRebate>");

		premiumwithRoundUP = smartchampBusinessLogic
				.setPremiumWithoutSTwithRoundUP(premiumWithoutST);

		PremiumRoundUpWithoutLoading = smartchampBusinessLogic.setTotalPremWithoutFreqRoundUp(TotalBasePremiumPaidWithoutFreqLoading);

		System.out.println("PremiumRoundUpWithoutLoading Testing "+PremiumRoundUpWithoutLoading);


		// premiumWithST = smartchampBusinessLogic.setPremiumWithST(
		// premiumwithRoundUP, JKResidentDisc);
		// ST = smartchampBusinessLogic.setServiceTax(premiumWithST,
		// premiumwithRoundUP);

		/*** modified by Akshaya on 20-MAY-16 start **/
		/*double basicServiceTax = smartchampBusinessLogic.getServiceTax(
				premiumwithRoundUP, JKResidentDisc, "basic");
		double SBCServiceTax = smartchampBusinessLogic.getServiceTax(
				premiumwithRoundUP, JKResidentDisc, "SBC");
		double KKCServiceTax = smartchampBusinessLogic.getServiceTax(
				premiumwithRoundUP, JKResidentDisc, "KKC");*/

		double basicServiceTax = 0;
		double SBCServiceTax = 0;
		double KKCServiceTax = 0;

		double kerlaServiceTax=0;
		double KeralaCessServiceTax=0;
		boolean isKerlaDiscount = smartchampbean1.isKerlaDisc();
		if(isKerlaDiscount){
			basicServiceTax = smartchampBusinessLogic.getServiceTax(
					premiumwithRoundUP, JKResidentDisc, "basic");
			kerlaServiceTax = smartchampBusinessLogic.getServiceTax(
					premiumwithRoundUP, JKResidentDisc, "KERALA");
			KeralaCessServiceTax =kerlaServiceTax-basicServiceTax;
		}else{
			basicServiceTax = smartchampBusinessLogic.getServiceTax(
					premiumwithRoundUP, JKResidentDisc, "basic");
			SBCServiceTax = smartchampBusinessLogic.getServiceTax(
					premiumwithRoundUP, JKResidentDisc, "SBC");
			KKCServiceTax = smartchampBusinessLogic.getServiceTax(
					premiumwithRoundUP, JKResidentDisc, "KKC");
		}




		FirstYear_ST = basicServiceTax + SBCServiceTax + KKCServiceTax + kerlaServiceTax;


		/* Added By Tushar Kotian on 1/8/2017 */



		premiumWithSTFirstYear = premiumwithRoundUP + FirstYear_ST;

		// premiumWithSTFirstYear = smartchampBusinessLogic
		// .setPremiumWithSTFirstYear(premiumwithRoundUP, JKResidentDisc);
		// FirstYear_ST = smartchampBusinessLogic.setServiceTax(
		// premiumWithSTFirstYear, premiumwithRoundUP);

		/*double basicServiceTaxSecondYear = smartchampBusinessLogic
				.getServiceTaxSecondYear(premiumwithRoundUP, JKResidentDisc,
						"basic");
		double SBCServiceTaxSecondYear = smartchampBusinessLogic
				.getServiceTaxSecondYear(premiumwithRoundUP, JKResidentDisc,
						"SBC");
		double KKCServiceTaxSecondYear = smartchampBusinessLogic
				.getServiceTaxSecondYear(premiumwithRoundUP, JKResidentDisc,
						"KKC");*/

		double basicServiceTaxSecondYear = 0;
		double SBCServiceTaxSecondYear = 0;
		double KKCServiceTaxSecondYear = 0;

		double kerlaServiceTaxSecondYear=0;
		double KeralaCessServiceTaxSecondYear =0;

		if(isKerlaDiscount){
			basicServiceTaxSecondYear = smartchampBusinessLogic
					.getServiceTaxSecondYear(premiumwithRoundUP, JKResidentDisc,
							"basic");
			kerlaServiceTaxSecondYear = smartchampBusinessLogic
					.getServiceTaxSecondYear(premiumwithRoundUP, JKResidentDisc,
							"KERALA");
			KeralaCessServiceTaxSecondYear =kerlaServiceTaxSecondYear-basicServiceTaxSecondYear;
		}else{
			basicServiceTaxSecondYear = smartchampBusinessLogic
					.getServiceTaxSecondYear(premiumwithRoundUP, JKResidentDisc,
							"basic");
			SBCServiceTaxSecondYear = smartchampBusinessLogic
					.getServiceTaxSecondYear(premiumwithRoundUP, JKResidentDisc,
							"SBC");
			KKCServiceTaxSecondYear = smartchampBusinessLogic
					.getServiceTaxSecondYear(premiumwithRoundUP, JKResidentDisc,
							"KKC");
		}




		SecondYear_ST = basicServiceTaxSecondYear + SBCServiceTaxSecondYear
				+ KKCServiceTaxSecondYear + kerlaServiceTaxSecondYear;

		premiumWithSTSecondYear = premiumwithRoundUP + SecondYear_ST;

		// premiumWithSTSecondYear = smartchampBusinessLogic
		// .setPremiumWithSTSecondYear(premiumwithRoundUP, JKResidentDisc);
		// SecondYear_ST = smartchampBusinessLogic.setServiceTax(
		// premiumWithSTSecondYear, premiumwithRoundUP);
		/*** modified by Akshaya on 20-MAY-16 start **/

		double prevTotalSurvivalBenefitAt_4_Percent = 0, prevTotalSurvivalBenefitAt_8_Percent = 0;
		try {

			// System.out.println(premiumWithoutST+" "+premiumWithST+" "+ST+" "+premiumwithRoundUP);
			int premiumMultiplication = smartchampBusinessLogic
					.setPremiumMultiplication(premFreqMode);
			double BasePremiumPaid = premiumWithoutST * premiumMultiplication;
			double yearlyPremiumWithST = smartchampBusinessLogic
					.setYearlyPremiumWithST(premiumWithSTFirstYear,
							premFreqMode);
			int rowNumber = 0, j = 0;
			double maxNonGuarateedDeathBenefitAt_4_Percent = 0, maxNonGuarateedDeathBenefitAt_8_Percent = 0, fyTotPremPaid = 0, maxTotBasePremPaid = 0, annualizedPrem = 0, setTotalBasePPaidFactor = 0;
			;

			for (int i = 0; i < policyTerm; i++)
			// for(int i=0;i<2;i++)
			{
				rowNumber++;

				year_F = rowNumber;
				_year_F = year_F;
				// System.out.println("1. year_F "+year_F);
				bussIll.append("<policyYr" + _year_F + ">" + _year_F
						+ "</policyYr" + _year_F + ">");


				//added by sujata 18-11-2019

               /* GuarateedAddition = commonForAllProd.getRoundOffLevel2(commonForAllProd
                        .getStringWithout_E(smartchampBusinessLogic
                                .get_Guarateed_add(year_F,
                                        sumAssured, policyTerm
                                        , totalBasePremiumPaid,
                                        SAMultipleForDeathBenifitToBeConsider
                                        , APWithoutModalLoading)));*/

				MaturityBenefit = commonForAllProd.getRoundOffLevel2(commonForAllProd
						.getStringWithout_E(smartchampBusinessLogic
								.Maturity_Benefit(sumAssured
										, premiumPayingTerm
										, policyTerm, year_F)));

				ReversionaryBonus4Per = commonForAllProd.getRoundOffLevel2(commonForAllProd
						.getStringWithout_E(smartchampBusinessLogic
								.Reversionary_Bonus4pr(premFreqMode,
										sumAssured, year_F, premiumPayingTerm, policyTerm)));

				ReversionaryBonus8Per = commonForAllProd.getRoundOffLevel2(commonForAllProd
						.getStringWithout_E(smartchampBusinessLogic
								.Reversionary_Bonus8pr(premFreqMode,
										sumAssured, year_F, premiumPayingTerm, policyTerm)));
//added by sujata on 27-11-2019

				//System.out.println("TotalDeathBenefit8per "+TotalDeathBenefit4per);

				TotalMaturityBenefit4per = commonForAllProd.getRoundOffLevel2(commonForAllProd
						.getStringWithout_E(smartchampBusinessLogic
								.setTotalMaturity_Ben4per(sumAssured,
										premiumPayingTerm, policyTerm, _year_F, premFreqMode, year_F, sumsurvivalbenyerr20)));


				TotalMaturityBenefit8per = commonForAllProd.getRoundOffLevel2(commonForAllProd
						.getStringWithout_E(smartchampBusinessLogic
								.setTotalMaturity_Ben8per(sumAssured,
										premiumPayingTerm, policyTerm, _year_F, premFreqMode, year_F, sumsurvivalben8perYear20)));

//added by sujata on 27-11-2019
				//10-01-2020
				TotalBasePremiumPaid = commonForAllProd.getRoundUp(commonForAllProd.getRoundOffLevel2New(commonForAllProd
						.getStringWithout_E(smartchampBusinessLogic
								.setTotalBasePremiumPaid(ageOfProposer
										, policyTerm
										, premFreqMode, "Basic"
										, sumAssured, staffRebate
								))));
				//System.out.println("TotalBasePremiumPaid "+TotalBasePremiumPaid);


				totalBasePremiumPaidWithoutFreqLoading = commonForAllProd.getRoundUp(commonForAllProd
						.getStringWithout_E(smartchampBusinessLogic
								.set_Premium_Without_ST_WithoutLoadingFreq(ageOfProposer
										, policyTerm
										, premFreqMode, "Basic"
										, sumAssured, staffRebate
								)));

				//added by sujata on 23-12-2019
				AnnulizedPremium = commonForAllProd.getRound(commonForAllProd
						.getStringWithout_E(smartchampBusinessLogic
								.setAnnulized_Premium(year_F, premiumPayingTerm, premiumwithRoundUP, premFreqMode)));

				//added by sujata on 16-12-2019

				if (_year_F == 1) {
					FirstYearOfAnnulizedPrem = Double.parseDouble(AnnulizedPremium);
				}

				//System.out.println("AnnulizedPremium "+AnnulizedPremium);
				/*PremiumBasic = commonForAllProd.getRoundUp(commonForAllProd
						.getStringWithout_E(Double.parseDouble(TotalBasePremiumPaid)
										));*/

				PremiumBasic = commonForAllProd.getRoundUp(commonForAllProd
						.getStringWithout_E(smartchampBusinessLogic
								.setTotalBasePPaid(totalBasePremiumPaid, year_F, premiumPayingTerm, premFreqMode, ageOfProposer, policyTerm, "Basic", sumAssured, staffRebate
										, Double.parseDouble(TotalBasePremiumPaid))
						));
				if (_year_F == 1) {
					FirstYearOfTotBasePrem = Double.parseDouble(PremiumBasic);
				}

				//16-12-2019
				TotalDeathBenefit4per = commonForAllProd.getRound(commonForAllProd
						.getStringWithout_E(smartchampBusinessLogic
								.TotalDeathBenefit_4percent(sumAssured,
										year_F, policyTerm, ageOfProposer, totalBasePremiumPaid, SAMultipleForDeathBenifitToBeConsider, APWithoutModalLoading,
										premFreqMode, premiumPayingTerm, PremiumRoundUpWithoutLoading, FirstYearOfTotBasePrem, Double.parseDouble(PremiumBasic), FirstYearOfAnnulizedPrem)));
				//System.out.println("TotalDeathBenefit4per "+TotalDeathBenefit4per);
				//16-12-2019
				TotalDeathBenefit8per = commonForAllProd.getRound(commonForAllProd
						.getStringWithout_E(smartchampBusinessLogic
								.TotalDeathBenefit_8percent(sumAssured,
										year_F, policyTerm, ageOfProposer, totalBasePremiumPaid, SAMultipleForDeathBenifitToBeConsider, APWithoutModalLoading,
										premFreqMode, premiumPayingTerm, PremiumRoundUpWithoutLoading, FirstYearOfTotBasePrem, Double.parseDouble(PremiumBasic), FirstYearOfAnnulizedPrem
								)));
				//System.out.println("TotalDeathBenefit8per "+TotalDeathBenefit4per);


				//Added by sujata on 10-12-2019
				TotalSurvivalBenefit4per = commonForAllProd.getRound(commonForAllProd
						.getStringWithout_E(smartchampBusinessLogic
								.setTotalSurvivalBenefit4per(sumAssured, premiumPayingTerm, policyTerm, year_F
										, premFreqMode, NonGuarateedDeathBenefitAt_4_Percent, sumsurvivalbenyerr20
								)));


				TotalSurvivalBenefit8per = commonForAllProd.getRound(commonForAllProd
						.getStringWithout_E(smartchampBusinessLogic
								.setTotalSurvivalBenefit8per(sumAssured, premiumPayingTerm, policyTerm, year_F
										, premFreqMode, NonGuarateedDeathBenefitAt_8_Percent
								)));

//AADED BY SUJATA ON 21-11-2019
				bussIll.append("<cashbonus" + _year_F + ">"
						+ 0 + "</cashbonus" + _year_F
						+ ">");

				//26-11-2019
				bussIll.append("<totBasePremPaid" + _year_F + ">"
						+ TotalBasePremiumPaid + "</totBasePremPaid" + _year_F
						+ ">");

				bussIll.append("<TotalBasePremiumPaidWithoutFreqLoading" + _year_F + ">"
						+ TotalBasePremiumPaidWithoutFreqLoading + "</TotalBasePremiumPaidWithoutFreqLoading" + _year_F
						+ ">");

				//added by sujata on 06-12-2019
				bussIll.append("<annulizedPremium" + _year_F + ">"
						+ AnnulizedPremium + "</annulizedPremium" + _year_F
						+ ">");

//End

				//26-11-2019
				bussIll.append("<GuarateedAddition" + _year_F + ">"
						+ 0 + "</GuarateedAddition" + _year_F + ">");

				bussIll.append("<MaturityBenefit" + _year_F + ">"
						+ MaturityBenefit + "</MaturityBenefit" + _year_F + ">");

				bussIll.append("<ReversionaryBonus4Per" + _year_F + ">"
						+ ReversionaryBonus4Per + "</ReversionaryBonus4Per" + _year_F + ">");

				bussIll.append("<ReversionaryBonus8Per" + _year_F + ">"
						+ ReversionaryBonus8Per + "</ReversionaryBonus8Per" + _year_F + ">");

				bussIll.append("<TotalDeathBenefit4per" + _year_F + ">"
						+ TotalDeathBenefit4per + "</TotalDeathBenefit4per" + _year_F + ">");

				bussIll.append("<TotalDeathBenefit8per" + _year_F + ">"
						+ TotalDeathBenefit8per + "</TotalDeathBenefit8per" + _year_F + ">");
//added by sujata on 27-11-2019
				bussIll.append("<TotalMaturityBenefit4per" + _year_F + ">"
						+ TotalMaturityBenefit4per + "</TotalMaturityBenefit4per" + _year_F + ">");

				bussIll.append("<TotalMaturityBenefit8per" + _year_F + ">"
						+ TotalMaturityBenefit8per + "</TotalMaturityBenefit8per" + _year_F + ">");

				bussIll.append("<SurvivalBenefits-TotalSurvivalBenefit4per" + _year_F + ">"
						+ TotalSurvivalBenefit4per + "</SurvivalBenefits-TotalSurvivalBenefit4per" + _year_F + ">");

				bussIll.append("<SurvivalBenefits-TotalSurvivalBenefit8per" + _year_F + ">"
						+ TotalSurvivalBenefit8per + "</SurvivalBenefits-TotalSurvivalBenefit8per" + _year_F + ">");

				//End
				SAMultipleForDeathBenifitToBeConsider =smartchampBusinessLogic
						.setSAMultipleForDeathBenifitToBeConsider(premFreqMode,ageOfProposer,policyTerm);

				APWithoutModalLoading =smartchampBusinessLogic
						.setAPWithoutModalLoading(ageOfProposer, premFreqMode, policyTerm, staffDisc, sumAssured);

				//added by sujata on 06-12-2019
				GuaranteedDeathBenefit = smartchampBusinessLogic
						.setGuaranteedDeathBenefit(sumAssured, year_F,
								policyTerm, ageOfProposer, totalBasePremiumPaid, SAMultipleForDeathBenifitToBeConsider, APWithoutModalLoading
								, premiumPayingTerm, premFreqMode, PremiumRoundUpWithoutLoading, FirstYearOfTotBasePrem, Double.parseDouble(PremiumBasic),
								FirstYearOfAnnulizedPrem);
				//System.out.println(" Year_F "+_year_F);
				// System.out.println("3.Guaranteed Death Benefit "+GuaranteedDeathBenefit);
				bussIll.append("<guarntedDeathBenft" + _year_F + ">"
						+ commonForAllProd.getRound(commonForAllProd.getStringWithout_E(GuaranteedDeathBenefit)) + "</guarntedDeathBenft"
						+ _year_F + ">");


				NonGuarateedDeathBenefitAt_4_Percent = smartchampBusinessLogic
						.setNonGuarateedDeathBenefitAt_4_Percent(premFreqMode,
								sumAssured, year_F,
								premiumPayingTerm, policyTerm);

				_NonGuarateedDeathBenefitAt_4_Percent = NonGuarateedDeathBenefitAt_4_Percent;

				NonGuarateedDeathBenefitAt_8_Percent = smartchampBusinessLogic
						.setNonGuarateedDeathBenefitAt_8_Percent(premFreqMode,
								sumAssured, year_F,
								premiumPayingTerm, policyTerm);
				_NonGuarateedDeathBenefitAt_8_Percent = NonGuarateedDeathBenefitAt_8_Percent;
				//End//
				sumSurvivalBenefit = (GuaranteedSurvivalBenefit) + sumSurvivalBenefit;
				GuaranteedSurvivalBenefit = smartchampBusinessLogic
						.setGuaranteedSurvivalBenefit(sumAssured,
								premiumPayingTerm, policyTerm, year_F);
				// System.out.println("6.Guaranteed Survival Benefit "+GuaranteedSurvivalBenefit);
				bussIll.append("<guarntedSurvivalBenft" + _year_F + ">"
						+ GuaranteedSurvivalBenefit + "</guarntedSurvivalBenft"
						+ _year_F + ">");


				NonGuarateedSurvivalBenefitAt_4_Percent = smartchampBusinessLogic
						.setNonGuarateedSurvivalBenefitAt_4_Percent(
								premFreqMode,
								NonGuarateedDeathBenefitAt_4_Percent,
								premiumPayingTerm, policyTerm, year_F,
								sumAssured);
				// System.out.println("7.Non Guarateed Survival Benefit At_4_Percent "+NonGuarateedSurvivalBenefitAt_4_Percent);
				bussIll.append("<nonGuarntedSurvivalBenft_4_pr" + _year_F + ">"
						+ NonGuarateedSurvivalBenefitAt_4_Percent
						+ "</nonGuarntedSurvivalBenft_4_pr" + _year_F + ">");

				//Added by sujata on 10-12-2019

				NonGuarateedSurvivalBenefitAt_8_Percent = smartchampBusinessLogic
						.setNonGuarateedSurvivalBenefitAt_8_Percent(
								premFreqMode,
								NonGuarateedDeathBenefitAt_8_Percent,
								premiumPayingTerm, policyTerm, year_F,
								sumAssured);

				if (year_F >= 2) {
					sumsurvivalbenyerr20 = (NonGuarateedSurvivalBenefitAt_4_Percent);
					sumsurvivalben8perYear20 = (NonGuarateedSurvivalBenefitAt_8_Percent);
				}
				//System.out.println("sumsurvivalbenyerr20 "+sumsurvivalbenyerr20);
				//System.out.println("sumsurvivalben8perYear20 "+sumsurvivalben8perYear20);

				// System.out.println("8.Non Guarateed Survival Benefit At_8_Percent "+NonGuarateedSurvivalBenefitAt_8_Percent);
				bussIll.append("<nonGuarntedSurvivalBenft_8_pr" + _year_F + ">"
						+ NonGuarateedSurvivalBenefitAt_8_Percent
						+ "</nonGuarntedSurvivalBenft_8_pr" + _year_F + ">");

				TotalSurvivalBenefitAt_4_Percent = smartchampBusinessLogic
						.setTotalSurvivalBenefitAt_4_Percent(
								GuaranteedSurvivalBenefit,
								NonGuarateedSurvivalBenefitAt_4_Percent);
				// System.out.println("9.Total Survival Benefit At_4_Percent "+TotalSurvivalBenefitAt_4_Percent);


				TotalSurvivalBenefitAt_8_Percent = smartchampBusinessLogic
						.setTotalSurvivalBenefitAt_8_Percent(
								GuaranteedSurvivalBenefit,
								NonGuarateedSurvivalBenefitAt_8_Percent);

				//added by sujata 27-11-2019
				if (_year_F == 1) {
					annualizedPrem = Double.parseDouble(PremiumBasic);
				}
//End
				//added by sujata on 09-12-2019
//				if((_year_F==1))
//				{
//					setTotalBasePPaidFactor = Double.parseDouble(setPremiumPaidFactor);
//				}
				//added by sujata on 27-11-2019
				//added by sujata on 06-12-2019 parameter added as TotalBasePremiumPaidWithoutFreqLoading
				GuaranteedSSV = commonForAllProd.getRound(commonForAllProd.getStringWithout_E((smartchampBusinessLogic.setGuarateedSSV(
						fyTotPremPaid, premiumPayingTerm,
						policyTerm, year_F, ageOfProposer, BasePremiumPaid,
						premFreqMode, "Basic", sumAssured, staffRebate, sumSurvivalBenefit, annualizedPrem, TotalBasePremiumPaidWithoutFreqLoading, setTotalBasePPaidFactor))));


				bussIll.append("<GuarateedSSV" + _year_F + ">"
						+ GuaranteedSSV
						+ "</GuarateedSSV" + _year_F + ">");


				nonGuarateedSSV_4_Pr = commonForAllProd.getRound(commonForAllProd.getStringWithout_E(smartchampBusinessLogic
						.setNonGuarateedSSV_4_Pr(sumAssured, premiumPayingTerm,
								policyTerm, year_F, premFreqMode,
								_NonGuarateedDeathBenefitAt_4_Percent)));
				// System.out.println("12.Non-guaranteed (SSV) At_4_Percent "+NonGuarateedSSV_4_Pr);
				bussIll.append("<nonGurntedSSV_4_pr" + _year_F + ">"
						+ nonGuarateedSSV_4_Pr + "</nonGurntedSSV_4_pr"
						+ _year_F + ">");

				nonGuarateedSSV_8_Pr = commonForAllProd.getRound(commonForAllProd.getStringWithout_E(smartchampBusinessLogic
						.setNonGuarateedSSV_8_Pr(sumAssured, premiumPayingTerm,
								policyTerm, year_F, premFreqMode,
								_NonGuarateedDeathBenefitAt_8_Percent)));
				// System.out.println("13.Non-guaranteed (SSV) At_8_Percent "+nonGuarateedSSV_8_Pr);
				bussIll.append("<nonGurntedSSV_8_pr" + _year_F + ">"
						+ nonGuarateedSSV_8_Pr + "</nonGurntedSSV_8_pr"
						+ _year_F + ">");

				if (i == 0) {
					fyTotPremPaid = totalBasePremiumPaid;
				}

				if (totalBasePremiumPaid > maxTotBasePremPaid) {
					maxTotBasePremPaid = totalBasePremiumPaid;
				}
				if (i == (policyTerm - 2)) {
					prevTotalSurvivalBenefitAt_4_Percent = TotalSurvivalBenefitAt_4_Percent;
					prevTotalSurvivalBenefitAt_8_Percent = TotalSurvivalBenefitAt_8_Percent;
				}
			}

     /*       rowNumber = 0;
			for (int i = 0; i < policyTerm; i++)
			// for(int i=0;i<1;i++)
			{
				rowNumber++;

				year_F = rowNumber;
				_year_F = year_F;
				// System.out.println("1. year_F "+year_F);

				totalBasePremiumPaid = smartchampBusinessLogic
						.setTotalBasePremiumPaid(premiumwithRoundUP, year_F,
								premiumPayingTerm, premFreqMode);
				// System.out.println("2.Total Base Premium Paid "+totalBasePremiumPaid);

				GuarateedSSV = smartchampBusinessLogic.setGuarateedSSV(
						fyTotPremPaid, premiumPayingTerm, policyTerm, year_F,
						totalBasePremiumPaid, premFreqMode, maxTotBasePremPaid);
				// System.out.println("11.Guaranteed (GSV) "+ GuarateedSSV);
				bussIll.append("<gurntedSSV" + _year_F + ">" + GuarateedSSV
						+ "</gurntedSSV" + _year_F + ">");

            }*/

			/****************************** Added by Akshaya on 03-MAR-15 start **********************/

			if (rb_proposerdetail_personaldetail_backdating_yes.isChecked()) {
				// "16-jan-2014")));
				BackDateinterest = commonForAllProd.getRoundUp(""
						+ (smartchampBusinessLogic.getBackDateInterest(
						premiumWithSTFirstYear,
						btn_proposerdetail_personaldetail_backdatingdate
								.getText().toString())));

				BackDateinterestwithGST = commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(Double.parseDouble(BackDateinterest) + (Double.parseDouble(BackDateinterest) *  prop.GSTforbackdate)));
			} else {
				BackDateinterestwithGST = "0";
			}

			premiumWithSTFirstYear = premiumWithSTFirstYear
					+ Double.parseDouble(BackDateinterestwithGST);
			/****************************** Added by Akshaya on 03-MAR-15 end **********************/

		} catch (Exception e) {
			e.printStackTrace();
		}
		premiumWithSTFirstYear = premiumWithSTFirstYear
				+ Double.parseDouble(BackDateinterest);

		MinesOccuInterest = smartchampBusinessLogic.getMinesOccuInterest(sumAssured) + "";

		/*MinesOccuInterest = commonForAllProd
				.getStringWithout_E(smartchampBusinessLogic
						.getMinesOccuInterest(smartchampbean1.getSumAssured()));

		servicetax_MinesOccuInterest = ""
				+ smartchampBusinessLogic
				.getServiceTaxMines(Double.parseDouble(MinesOccuInterest));

		MinesOccuInterest=""+(Double.parseDouble(MinesOccuInterest)+Double.parseDouble(servicetax_MinesOccuInterest));
*/


		/****** Added By Tushar Kotian on 1/8/2017 end *****/

		/* Modified By - Priyanka Warekar - 31-08-2015 - Start */

		/* modified by Akshaya on 20-MAY-16 start **/

		/*** modified by Akshaya on 20-MAY-16 start **/

        /*return new String[]{
				commonForAllProd.getStringWithout_E(premiumwithRoundUP),
				commonForAllProd.getStringWithout_E(FirstYear_ST),
				commonForAllProd.getStringWithout_E(premiumWithSTFirstYear),
				commonForAllProd.getStringWithout_E(GuaranteedSurvivalBenefit),
				commonForAllProd.getStringWithout_E(TotalSurvivalBenefitAt_4_Percent),
                commonForAllProd.getStringWithout_E(TotalSurvivalBenefitAt_8_Percent),
                commonForAllProd.getStringWithout_E(prevTotalSurvivalBenefitAt_4_Percent),
                commonForAllProd.getStringWithout_E(prevTotalSurvivalBenefitAt_8_Percent),
                premiumWithoutSTDisc, BackDateinterestwithGST,
                commonForAllProd.getStringWithout_E(SecondYear_ST),
                commonForAllProd.getStringWithout_E(premiumWithSTSecondYear),
                commonForAllProd.getStringWithout_E(basicServiceTax),
                commonForAllProd.getStringWithout_E(SBCServiceTax),
                commonForAllProd.getStringWithout_E(KKCServiceTax),
                commonForAllProd.getStringWithout_E(basicServiceTaxSecondYear),
                commonForAllProd.getStringWithout_E(SBCServiceTaxSecondYear),
                commonForAllProd.getStringWithout_E(KKCServiceTaxSecondYear),
                commonForAllProd.getStringWithout_E(KeralaCessServiceTax),
                commonForAllProd.getStringWithout_E(KeralaCessServiceTaxSecondYear)};*/

        /*return new String[]{
                (commonForAllProd.getStringWithout_E(premiumwithRoundUP)),
                commonForAllProd.getStringWithout_E(FirstYear_ST),
                commonForAllProd.getStringWithout_E(premiumWithSTFirstYear),
                commonForAllProd.getStringWithout_E(GuaranteedSurvivalBenefit),
                (commonForAllProd
                        .getStringWithout_E(TotalSurvivalBenefitAt_4_Percent)),
				commonForAllProd
						.getStringWithout_E(TotalSurvivalBenefitAt_8_Percent),
				commonForAllProd
						.getStringWithout_E(prevTotalSurvivalBenefitAt_4_Percent),
				commonForAllProd
						.getStringWithout_E(prevTotalSurvivalBenefitAt_8_Percent),
				premiumWithoutSTDisc, BackDateinterestwithGST,
				commonForAllProd.getStringWithout_E(SecondYear_ST),
				commonForAllProd.getStringWithout_E(premiumWithSTSecondYear),
				commonForAllProd.getStringWithout_E(basicServiceTax),
				commonForAllProd.getStringWithout_E(SBCServiceTax),
				commonForAllProd.getStringWithout_E(KKCServiceTax),
				commonForAllProd.getStringWithout_E(basicServiceTaxSecondYear),
				commonForAllProd.getStringWithout_E(SBCServiceTaxSecondYear),
				commonForAllProd.getStringWithout_E(KKCServiceTaxSecondYear),
				commonForAllProd.getStringWithout_E(KeralaCessServiceTax),
                commonForAllProd.getStringWithout_E(KeralaCessServiceTaxSecondYear),
                //added by sujata
                commonForAllProd.getStringWithout_E(Double.parseDouble(GuarateedAddition)),
                commonForAllProd.getStringWithout_E(Double.parseDouble(MaturityBenefit)),
                commonForAllProd.getStringWithout_E(Double.parseDouble(ReversionaryBonus4Per)),
                commonForAllProd.getStringWithout_E(Double.parseDouble(ReversionaryBonus8Per)),
                commonForAllProd.getStringWithout_E(Double.parseDouble(TotalDeathBenefit4per)),
                commonForAllProd.getStringWithout_E(Double.parseDouble(TotalDeathBenefit8per)),
                commonForAllProd.getStringWithout_E(Double.parseDouble(TotalMaturityBenefit4per)),
                commonForAllProd.getStringWithout_E(Double.parseDouble(TotalMaturityBenefit8per))

        };
    */

		return new String[]{
				(commonForAllProd.getStringWithout_E(premiumwithRoundUP)),
				commonForAllProd.getStringWithout_E(FirstYear_ST),
				commonForAllProd.getStringWithout_E(premiumWithSTFirstYear),
				commonForAllProd.getStringWithout_E(GuaranteedSurvivalBenefit),


				//Commented by sujata//
				commonForAllProd
						.getStringWithout_E(TotalSurvivalBenefitAt_4_Percent),
				commonForAllProd
						.getStringWithout_E(TotalSurvivalBenefitAt_8_Percent),
				commonForAllProd
						.getStringWithout_E(prevTotalSurvivalBenefitAt_4_Percent),
				commonForAllProd
						.getStringWithout_E(prevTotalSurvivalBenefitAt_8_Percent),
				premiumWithoutSTDisc, BackDateinterest,
				commonForAllProd.getStringWithout_E(SecondYear_ST),
				commonForAllProd.getStringWithout_E(premiumWithSTSecondYear),
				commonForAllProd.getStringWithout_E(basicServiceTax),
				commonForAllProd.getStringWithout_E(SBCServiceTax),
				commonForAllProd.getStringWithout_E(KKCServiceTax),
				commonForAllProd.getStringWithout_E(basicServiceTaxSecondYear),
				commonForAllProd.getStringWithout_E(SBCServiceTaxSecondYear),
				commonForAllProd.getStringWithout_E(KKCServiceTaxSecondYear), MinesOccuInterest,
				BackDateinterest,
				commonForAllProd.getStringWithout_E(KeralaCessServiceTax),
				commonForAllProd.getStringWithout_E(KeralaCessServiceTaxSecondYear),
				//added by sujata
				commonForAllProd.getStringWithout_E((GuarateedAddition)),
				commonForAllProd.getStringWithout_E(Double.parseDouble(MaturityBenefit)),
				commonForAllProd.getStringWithout_E(Double.parseDouble(ReversionaryBonus4Per)),
				commonForAllProd.getStringWithout_E(Double.parseDouble(ReversionaryBonus8Per)),
				commonForAllProd.getStringWithout_E(Double.parseDouble(TotalDeathBenefit4per)),
				commonForAllProd.getStringWithout_E(Double.parseDouble(TotalDeathBenefit8per)),
				commonForAllProd.getStringWithout_E(Double.parseDouble(TotalMaturityBenefit4per)),
				commonForAllProd.getStringWithout_E(Double.parseDouble(TotalMaturityBenefit8per))

		};
		/*** modified by Akshaya on 20-MAY-16 end **/

	}

	private boolean valSumAssured() {

		String error = "";
		if (edt_SumAssured.getText().toString().equals("")) {
			error = "Please enter Sum Assured in Rs. ";

		}

		else if (Double.parseDouble(edt_SumAssured.getText().toString()) > prop.maxSumAssured) {
			error = "Sum Assured should not be greater than Rs. "
					+ currencyFormat.format(prop.maxSumAssured);

		} else if (Double.parseDouble(edt_SumAssured.getText().toString()) < prop.minSumAssured) {
			error = "Sum Assured should not be less than Rs. "
					+ currencyFormat.format(prop.minSumAssured);

		} else {
			if (!(Double.parseDouble(edt_SumAssured.getText().toString()) % 1000 == 0)) {
				error = "Sum Assured should be multiple of 1000";

			}
		}

		if (!error.equals("")) {
			showAlert(error);
			setFocusable(edt_SumAssured);
			edt_SumAssured.requestFocus();
			return false;
		} else
			return true;

	}

	private boolean valMaturityAge() {
		String error = "";
		int i = Integer.parseInt(spnr_Age.getSelectedItem().toString())
				+ Integer.parseInt(spnrPolicyTerm.getSelectedItem().toString());
		if (i > 70) {
			error = "Maturity Age (Life assured age + Policy Term) should not be greater than 70 years.";

		}
		if (i < 42) {
			error = "Maturity age ( Life Assured + Policy Term ) should not be less than 42 years.";

		}
		if (!error.equals("")) {
			showAlert(error);
			setFocusable(btn_bi_smart_champ_insurance_life_assured_date);
			btn_bi_smart_champ_insurance_life_assured_date.requestFocus();
			return false;
		} else
			return true;

	}

	private boolean valPremAmount(double premiumwithRoundUP) {

		String error = "";
		if (spnrPremPayingMode.getSelectedItem().toString().equals("Yearly")) {
			if (premiumwithRoundUP < prop.minYearlyPrem) {
				error = "Minimum Premium for Yearly mode under this product is Rs. "
						+ currencyFormat.format(prop.minYearlyPrem);
				showAlert(error);
				// setFocusable(edt_SumAssured);
				edt_SumAssured.requestFocus();
				return false;
			} else {
				return true;
			}
		} else if (spnrPremPayingMode.getSelectedItem().toString()
				.equals("Half Yearly")) {
			if (premiumwithRoundUP < prop.minHalfYearlyPrem) {
				error = "Minimum Premium for Half-Yearly mode under this product is Rs. "
						+ currencyFormat.format(prop.minHalfYearlyPrem);
				showAlert(error);
				// setFocusable(edt_SumAssured);
				edt_SumAssured.requestFocus();
				return false;
			} else {
				return true;
			}
		} else if (spnrPremPayingMode.getSelectedItem().toString()
				.equals("Quarterly")) {
			if (premiumwithRoundUP < prop.minQuarterleyPrem) {
				error = "Minimum Premium for Quarterly mode under this product is Rs. "
						+ currencyFormat.format(prop.minQuarterleyPrem);
				showAlert(error);
				// setFocusable(edt_SumAssured);
				edt_SumAssured.requestFocus();
				return false;
			} else {
				return true;
			}
		} else if (spnrPremPayingMode.getSelectedItem().toString()
				.equals("Monthly")) {
			if (premiumwithRoundUP < prop.minMonthleyPrem) {
				error = "Minimum Premium for "
						+ spnrPremPayingMode.getSelectedItem().toString()
						+ " mode under this product is Rs. "
						+ currencyFormat.format(prop.minMonthleyPrem);
				showAlert(error);
				// setFocusable(edt_SumAssured);
				edt_SumAssured.requestFocus();
				return false;
			} else {
				return true;
			}
		} else {
			if (premiumwithRoundUP < prop.minSinglePrem) {
				error = "Minimum Premium for Single mode under this product is Rs. "
						+ currencyFormat.format(prop.minSinglePrem);
				showAlert(error);
				// setFocusable(edt_SumAssured);
				edt_SumAssured.requestFocus();
				return false;
			} else {
				return true;
			}
		}
		// if (!error.equals("")) {
		// setFocusable(edt_SumAssured);
		// edt_SumAssured.requestFocus();
		// showAlert(error);
		// return false;
		// } else {
		// return true;
		// }

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
			}

			else {
				return true;
			}
		}
		return true;
	}

	// boolean TrueBackdate() {
	//
	// try {
	//
	// String error = "";
	//
	// if (rb_proposerdetail_personaldetail_backdating_yes.isChecked()) {
	//
	// final Calendar c = Calendar.getInstance();
	// final int currYear = c.get(Calendar.YEAR);
	// final int currMonth = c.get(Calendar.MONTH) + 1;
	//
	// SimpleDateFormat dateformat1 = new SimpleDateFormat(
	// "dd-MM-yyyy");
	// Date dtBackDate = dateformat1
	// .parse(btn_proposerdetail_personaldetail_backdatingdate
	// .getText().toString());
	// Date currentDate = c.getTime();
	//
	// Date finYerEndDate = null;
	//
	// if (currMonth >= 4) {
	// finYerEndDate = dateformat1.parse("1-4-" + currYear);
	// } else {
	// finYerEndDate = dateformat1.parse("1-4-" + (currYear - 1));
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
	// // apply focusable method
	// setFocusable(btn_proposerdetail_personaldetail_backdatingdate);
	// btn_proposerdetail_personaldetail_backdatingdate
	// .requestFocus();
	// }
	// });
	// showAlert.show();
	//
	// return false;
	// }
	//
	// }
	//
	// } catch (Exception e) {
	// return false;
	// }
	//
	// return true;
	//
	// }

	private boolean valLifeAssuredProposerDetail() {
		if (proposer_Is_Same_As_Life_Assured.equalsIgnoreCase("Y")) {
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
									setFocusable(spnr_bi_smart_champ_insurance_life_assured_title);
									spnr_bi_smart_champ_insurance_life_assured_title
											.requestFocus();
								} else if (lifeAssured_First_Name.equals("")) {
									setFocusable(edt_bi_smart_champ_insurance_life_assured_first_name);
									edt_bi_smart_champ_insurance_life_assured_first_name
											.requestFocus();
								} else {
									setFocusable(edt_bi_smart_champ_insurance_life_assured_last_name);
									edt_bi_smart_champ_insurance_life_assured_last_name
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
								setFocusable(spnr_bi_smart_champ_insurance_life_assured_title);
								spnr_bi_smart_champ_insurance_life_assured_title
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
								setFocusable(spnr_bi_smart_champ_insurance_life_assured_title);
								spnr_bi_smart_champ_insurance_life_assured_title
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
								setFocusable(spnr_bi_smart_champ_insurance_life_assured_title);
								spnr_bi_smart_champ_insurance_life_assured_title
										.requestFocus();
							}
						});
				showAlert.show();

				return false;
			} else if (proposer_Title.equals("")
					|| proposer_First_Name.equals("")
					|| proposer_Last_Name.equals("")) {

				showAlert.setMessage("Please Fill Name Detail For Child");
				showAlert.setNeutralButton("OK",
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
												int which) {
								if (proposer_Title.equals("")) {
									// apply focusable method
									setFocusable(spnr_bi_smart_champ_insurance_proposer_title);
									spnr_bi_smart_champ_insurance_proposer_title
											.requestFocus();
								} else if (proposer_First_Name.equals("")) {
									setFocusable(edt_bi_smart_champ_insurance_proposer_first_name);
									edt_bi_smart_champ_insurance_proposer_first_name
											.requestFocus();
								} else {
									setFocusable(edt_bi_smart_champ_insurance_proposer_last_name);
									edt_bi_smart_champ_insurance_proposer_last_name
											.requestFocus();
								}
							}
						});
				showAlert.show();

				return false;
			} else if (child_gender.equalsIgnoreCase("")) {
				showAlert
						.setMessage("Please Select Child Gender");
				showAlert.setNeutralButton("OK",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
												int which) {
								// apply focusable method
								setFocusable(spnr_bi_smart_champ_insurance_proposer_title);
								spnr_bi_smart_champ_insurance_proposer_title
										.requestFocus();
							}
						});
				showAlert.show();

//                dialog("Please Select Child Gender", true);

				return false;
			} else if (proposer_Title.equalsIgnoreCase("Mr.")
					&& child_gender.equalsIgnoreCase("Female")) {

				showAlert
						.setMessage("Child Title and Gender is not Valid");
				showAlert.setNeutralButton("OK",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
												int which) {
								// apply focusable method
								setFocusable(spnr_bi_smart_champ_insurance_proposer_title);
								spnr_bi_smart_champ_insurance_proposer_title
										.requestFocus();
							}
						});
				showAlert.show();

				return false;

			} else if (proposer_Title.equalsIgnoreCase("Ms.")
					&& child_gender.equalsIgnoreCase("Male")) {

				showAlert
						.setMessage("Child Title and Gender is not Valid");
				showAlert.setNeutralButton("OK",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
												int which) {
								// apply focusable method
								setFocusable(spnr_bi_smart_champ_insurance_proposer_title);
								spnr_bi_smart_champ_insurance_proposer_title
										.requestFocus();
							}
						});
				showAlert.show();

				return false;

			} else if (proposer_Title.equalsIgnoreCase("Mrs.")
					&& child_gender.equalsIgnoreCase("Male")) {

				showAlert
						.setMessage("Child Title and Gender is not Valid");
				showAlert.setNeutralButton("OK",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
												int which) {
								// apply focusable method
								setFocusable(spnr_bi_smart_champ_insurance_proposer_title);
								spnr_bi_smart_champ_insurance_proposer_title
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

		if (proposer_Is_Same_As_Life_Assured.equalsIgnoreCase("Y")) {

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
								setFocusable(btn_bi_smart_champ_insurance_life_assured_date);
								btn_bi_smart_champ_insurance_life_assured_date
										.requestFocus();
							}
						});
				showAlert.show();

				return false;
			} else if (proposer_date_of_birth.equals("")
					|| proposer_date_of_birth.equalsIgnoreCase("select Date")) {
				showAlert
						.setMessage("Please Select Valid Date Of Birth For Child");
				showAlert.setNeutralButton("OK",
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
												int which) {
								// apply focusable method
								setFocusable(btn_bi_smart_champ_insurance_proposer_date);
								btn_bi_smart_champ_insurance_proposer_date
										.requestFocus();
							}
						});
				showAlert.show();

				return false;
			}

			else {
				return true;
			}
		} else
			return true;
	}

	/********************************** Updation of policy term n premium paying term starts here **********************************************************/

	// Policy Term Help
	private void updatePolicyTermLabel() {
		try {
			int PolicyTerm = prop.policyTerm
					- Integer.parseInt(edt_proposer_age.getText().toString());

			spnrPolicyTerm.setSelection(
					getIndex(spnrPolicyTerm, PolicyTerm + ""), false);
		} catch (Exception ignored) {
		}
	}

	// premium paying term help
	private void updatePremPayingTermLabel() {
		try {
			if (!spnrPremPayingMode.getSelectedItem().toString()
					.equals("Single")) {
				int PremiumPayingTerm = prop.premPayingTerm
						- Integer.parseInt(edt_proposer_age.getText()
						.toString());

				spnrPremPayingTerm.setSelection(
						getIndex(spnrPremPayingTerm, PremiumPayingTerm + ""),
						false);

			}
			// else if (spnrPremPayingMode.getSelectedItem().toString()
			// .equals("Single")) {
			// spnrPremPayingTerm.setSelection(
			// getIndex(spnrPremPayingTerm, "1"), false);
			// }
		} catch (Exception ignored) {
		}
	}

	/********************************** Ends here updation of policy term n premium paying term **********************************************************/

	// Alert Dialog Box
	private void showAlert(String error) {
		showAlert.setMessage(error);
		showAlert.setNeutralButton("OK", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
			}
		});
		showAlert.show();

	}

	/************************************** Async Start ********************************************/

	/*************************************** Async end **********************************************/

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

	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

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
			commonMethods.dialogWarning(context,"Please fill Valid Date", false);
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
					lifeAssuredAge = final_age;
					final_age = Integer.toString(age);
					if (final_age.contains("-")) {
						commonMethods.BICommonDialog(context, "Please fill Valid Bith Date For Life Assured");
					} else {
						if (21 <= age && age <= 50) {

							btn_bi_smart_champ_insurance_life_assured_date
									.setText(date);

							// spnr_Age.setSelection(getIndex(spnr_Age, final_age));

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

							if (!proposer_date_of_birth.equals("")) {
								edt_proposer_age.setText(String
										.valueOf(proposerAge));
							}

							spnr_Age.setSelection(getIndex(spnr_Age, final_age),
									false);
							// valMaturityAge();

							clearFocusable(btn_bi_smart_champ_insurance_life_assured_date);
							setFocusable(spnr_bi_smart_champ_insurance_proposer_title);
							spnr_bi_smart_champ_insurance_proposer_title
									.requestFocus();
						} else {
							commonMethods.BICommonDialog(context, "Minimum Age should be 21 yrs and Maximum Age should be 50 yrs For LifeAssured");
							btn_bi_smart_champ_insurance_life_assured_date
									.setText("Select Date");
							lifeAssured_date_of_birth = "";

							setFocusable(btn_bi_smart_champ_insurance_life_assured_date);
							btn_bi_smart_champ_insurance_life_assured_date
									.requestFocus();
						}
					}
					break;

				case 5:
					proposerAge = final_age;
					final_age = Integer.toString(age);
					if (final_age.contains("-")) {
						commonMethods.BICommonDialog(context, "Please fill Valid Bith Date For Proposer");
					} else {
						if (0 <= age && age <= 13) {

							btn_bi_smart_champ_insurance_proposer_date
									.setText(date);

							// spnr_Age.setSelection(getIndex(spnr_Age, final_age));

							proposer_date_of_birth = getDate1(date + "");
							edt_proposer_age.setText(final_age);
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

							// valMaturityAge();
							updatePolicyTermLabel();
							updatePremPayingTermLabel();

							clearFocusable(btn_bi_smart_champ_insurance_proposer_date);
							setFocusable(edt_proposerdetail_basicdetail_contact_no);
							edt_proposerdetail_basicdetail_contact_no
									.requestFocus();
							/*
							 * setFocusable(spnrPremPayingMode);
							 * spnrPremPayingMode.requestFocus();
							 */
						} else {
							commonMethods.BICommonDialog(context, "Minimum Age should be 0 yrs and Maximum Age should be 13 yrs For Child");
							btn_bi_smart_champ_insurance_proposer_date
									.setText("Select Date");
							proposer_date_of_birth = "";
							edt_proposer_age.setText("");

							clearFocusable(btn_bi_smart_champ_insurance_proposer_date);
							setFocusable(btn_bi_smart_champ_insurance_proposer_date);
							btn_bi_smart_champ_insurance_proposer_date
									.requestFocus();
						}
					}
					break;

				case 6:

					if (!lifeAssured_date_of_birth.equalsIgnoreCase("")
							&& !proposer_date_of_birth.equalsIgnoreCase("")) {
						if (age >= 0) {
							proposer_Backdating_BackDate = date + "";
							btn_proposerdetail_personaldetail_backdatingdate
									.setText(proposer_Backdating_BackDate);
							clearFocusable(btn_proposerdetail_personaldetail_backdatingdate);

						} else {
							commonMethods.dialogWarning(context,"Please Select Valid BackDating Date", true);
							btn_proposerdetail_personaldetail_backdatingdate
									.setText("Select Date");
							proposer_Backdating_BackDate = "";
						}
					} else {
						commonMethods.dialogWarning(context, "Please select Life Assured and Child DOB.", true);
					}

					break;

				default:
					break;
			}
		}

		if (proposer_Backdating_WishToBackDate_Policy.equalsIgnoreCase("y")
				&& !proposer_Backdating_BackDate.equals("")) {

			int Proposerage = calculateMyAge(mYear, Integer.parseInt(mont),
					Integer.parseInt(day), lifeAssured_date_of_birth);

			int childage = calculateMyAge(mYear, Integer.parseInt(mont),
					Integer.parseInt(day), proposer_date_of_birth);

			String str_final_age = Integer.toString(Proposerage);
			String str_final_child_age = Integer.toString(childage);
			spnr_Age.setSelection(getIndex(spnr_Age, str_final_age), false);

			if (Integer.parseInt(str_final_child_age) < 0) {
				commonMethods.dialogWarning(context,"Minimum Age of child should be 0", true);
				edt_proposer_age.setText("");
				proposer_date_of_birth = "";
				btn_bi_smart_champ_insurance_proposer_date
						.setText("Select Date");

			} else {
				edt_proposer_age.setText(str_final_child_age);
			}

			if (Integer.parseInt(str_final_age) < 21) {
				commonMethods.dialogWarning(context,"Minimum Age of life assured should be 21", true);
				spnr_Age.setSelection(getIndex(spnr_Age, "20"), false);
				lifeAssured_date_of_birth = "";
				btn_bi_smart_champ_insurance_life_assured_date
						.setText("Select Date");

			} else {
				edt_proposer_age.setText(str_final_child_age);
			}
			// valMaturityAge();
			updatePolicyTermLabel();
			updatePremPayingTermLabel();

		} else {
			if (!lifeAssured_date_of_birth.equals("")) {
				spnr_Age.setSelection(
						getIndex(spnr_Age, String.valueOf(lifeAssuredAge)),
						false);
			}
			if (!proposer_date_of_birth.equals("")) {
				edt_proposer_age.setText(String.valueOf(proposerAge));
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

	public void onClickLADob(View v) {
		initialiseDateParameter(lifeAssured_date_of_birth, 35);
		DIALOG_ID = 4;
		showDialog(DATE_DIALOG_ID);

	}

	public void onClickProposerDob(View v) {
		initialiseDateParameter(proposer_date_of_birth, 0);
		DIALOG_ID = 5;
		showDialog(DATE_DIALOG_ID);

	}

	public void onClickBackDating(View v) {
		String backdate = getDate1(proposer_Backdating_BackDate);
		initialiseDateParameter(backdate, 0);
		DIALOG_ID = 6;
		if (lifeAssured_date_of_birth != null
				&& !lifeAssured_date_of_birth.equals("")) {
			showDialog(DATE_DIALOG_ID);
		}

		else {
			commonMethods.dialogWarning(context, "Please select a LifeAssured DOB First", true);
		}
	}

	private int getIndex(Spinner s1, String value) {

		int index = 0;

		Log.d("TAG", "COUNT------" + s1.getCount());

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

		d = new Dialog(BI_SmartChampActivity.this);
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
				Intent intent = new Intent(BI_SmartChampActivity.this,
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
		if (v.getId() == edt_bi_smart_champ_insurance_life_assured_first_name
				.getId()) {
			setFocusable(edt_bi_smart_champ_insurance_life_assured_middle_name);
			edt_bi_smart_champ_insurance_life_assured_middle_name
					.requestFocus();
		} else if (v.getId() == edt_bi_smart_champ_insurance_life_assured_middle_name
				.getId()) {
			setFocusable(edt_bi_smart_champ_insurance_life_assured_last_name);
			edt_bi_smart_champ_insurance_life_assured_last_name.requestFocus();
		} else if (v.getId() == edt_bi_smart_champ_insurance_life_assured_last_name
				.getId()) {
			setFocusable(btn_bi_smart_champ_insurance_life_assured_date);
			btn_bi_smart_champ_insurance_life_assured_date.requestFocus();
		} else if (v.getId() == edt_bi_smart_champ_insurance_proposer_first_name
				.getId()) {
			setFocusable(edt_bi_smart_champ_insurance_proposer_middle_name);
			edt_bi_smart_champ_insurance_proposer_middle_name.requestFocus();
		} else if (v.getId() == edt_bi_smart_champ_insurance_proposer_middle_name
				.getId()) {
			setFocusable(edt_bi_smart_champ_insurance_proposer_last_name);
			edt_bi_smart_champ_insurance_proposer_last_name.requestFocus();
		} else if (v.getId() == edt_bi_smart_champ_insurance_proposer_last_name
				.getId()) {
			setFocusable(btn_bi_smart_champ_insurance_proposer_date);
			btn_bi_smart_champ_insurance_proposer_date.requestFocus();
		}

		else if (v.getId() == edt_proposerdetail_basicdetail_contact_no.getId()) {
			// clearFocusable(premiumAmt);
			setFocusable(edt_proposerdetail_basicdetail_Email_id);
			edt_proposerdetail_basicdetail_Email_id.requestFocus();
		}

		else if (v.getId() == edt_proposerdetail_basicdetail_Email_id.getId()) {
			// clearFocusable(premiumAmt);
			setFocusable(edt_proposerdetail_basicdetail_ConfirmEmail_id);
			edt_proposerdetail_basicdetail_ConfirmEmail_id.requestFocus();
		}

		else if (v.getId() == edt_proposerdetail_basicdetail_ConfirmEmail_id
				.getId()) {
			// clearFocusable(premiumAmt);
			clearFocusable(spnrPremPayingMode);
			setFocusable(spnrPremPayingMode);
			spnrPremPayingMode.requestFocus();
		}

		else if (v.getId() == edt_SumAssured.getId()) {


			commonMethods.hideKeyboard(edt_SumAssured,context);
			/*
			 * clearFocusable(edt_SumAssured);
			 * setFocusable(rb_proposerdetail_personaldetail_backdating_yes);
			 * rb_proposerdetail_personaldetail_backdating_yes.requestFocus();
			 */

		}
		return true;
	}

	private boolean createPdf() {
		try {

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
			bitmap.compress(Bitmap.CompressFormat.PNG, 50, stream);
			byte[] bitMapData = stream.toByteArray();
			Image image = Image.getInstance(bitMapData);
			image.setAlignment(Image.LEFT);
			image.scaleToFit(80, 50);

			document.open();

			PdfPTable table = new PdfPTable(3);
			table.setWidths(new float[] { 2.5f, 8.5f, 2f });
			table.setWidthPercentage(100);
			table.getDefaultCell().setPadding(15);

			PdfPCell cell;

			cell = new PdfPCell(new Phrase("\n", headerBold));
			cell.setColspan(3);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setBorder(Rectangle.NO_BORDER);
			table.addCell(cell);

			cell = new PdfPCell(new Phrase("\n", headerBold));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setBorder(Rectangle.NO_BORDER);
			table.addCell(cell);

			cell = new PdfPCell(new Phrase("SBI Life Insurance Co. Ltd",
					headerBold));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setBorder(Rectangle.NO_BORDER);
			table.addCell(cell);

			cell = new PdfPCell(new Phrase("\n", headerBold));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setRowspan(7);
			cell.setBorder(Rectangle.NO_BORDER);
			table.addCell(cell);

			cell = new PdfPCell(image);
			cell.setRowspan(7);
			cell.setBorder(Rectangle.NO_BORDER);
			table.addCell(cell);

			cell = new PdfPCell(
					new Phrase(
							"Registered & Corporate Office: 'Natraj', M.V.Road and Western Express Highway Junction, Andheri (East), Mumbai - 400069 ",
							headerBold));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setBorder(Rectangle.NO_BORDER);

			table.addCell(cell);

			cell = new PdfPCell(new Phrase("IRDAI Registration No. 111 | Website: www.sbilife.co.in | Email: info@sbilife.co.in | CIN: L99999MH2000PLC129113",
					headerBold));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setBorder(Rectangle.NO_BORDER);

			table.addCell(cell);

			cell = new PdfPCell(new Phrase("Toll Free: 1800 267 9090 (Between 9.00 am & 9.00 pm)",
					headerBold));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setBorder(Rectangle.NO_BORDER);

			table.addCell(cell);
			cell = new PdfPCell(new Phrase("Benefit Illustration(BI): SBI Life -Smart Champ Insurance (UIN : 111N098V03)",
					headerBold));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setBorder(Rectangle.NO_BORDER);

			table.addCell(cell);
			cell = new PdfPCell(new Phrase("An Individual, Non-Linked, Participating, Life Insurance Savings Product",
					headerBold));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setBorder(Rectangle.NO_BORDER);
			table.addCell(cell);


			/*cell = new PdfPCell(new Phrase("(UIN No -" + "111N098V03" + ")",
                    headerBold));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
			table.addCell(cell);*/


			cell = new PdfPCell(new Phrase("\n", normal));
			cell.setColspan(3);
			cell.setBorder(Rectangle.BOTTOM);
			cell.setBorderWidth(1.2f);
			table.addCell(cell);

			LineSeparator ls = new LineSeparator();
			//Paragraph para3 = new Paragraph("Introduction", small_bold);
			//para3.setAlignment(Element.ALIGN_LEFT);
			Paragraph para4 = new Paragraph(
					"The main objective of the illustration is that the client is able to appreciate the features of the product and the flow of benefits in different circumstances with some level of quantification. For further information on the product, its benefits and applicable charges please refer to the sales brochure and/or policy document.",
					normal1);
			para4.setAlignment(Element.ALIGN_LEFT | Element.ALIGN_JUSTIFIED);
			/*Paragraph para5 = new Paragraph(
					"Currently, the two rates of investment return declared by the Life Insurance Council are 4% and 8% per annum.",
					normal1);
			para5.setAlignment(Element.ALIGN_LEFT | Element.ALIGN_JUSTIFIED);
			Paragraph para6 = new Paragraph(
					"The main objective of the illustration is that the client is able to appreciate the features of the product and the flow of benefits in different circumstances with some level of quantification. For further information on the product and its benefits, please refer to the sales brochure and / or policy document. Further information will also be available on request.",
					normal1);
			para6.setAlignment(Element.ALIGN_LEFT | Element.ALIGN_JUSTIFIED);

			Paragraph para8 = new Paragraph(
					"Some benefits are guaranteed and some benefits are variable with returns based on the future performance of your Insurer carrying on life insurance business. If your policy offers guaranteed returns then these will be clearly marked guaranteed in the illustration table on this page. If your policy offers variable returns then the illustrations on this page will show two different rates of assumed future investment returns. These assumed rates of return are not guaranteed and they are not the upper or lower limits of what you might get back, as the value of your policy is dependent on a number of factors including future investment performance.",
					normal1);
			para4.setAlignment(Element.ALIGN_LEFT | Element.ALIGN_JUSTIFIED);
*/
			PdfPTable table_proposer_name = new PdfPTable(4);
			// float[] columnWidths_table_proposer_name = { 2f, 2f, 1f, 2f };
			// table_proposer_name.setWidths(columnWidths_table_proposer_name);
			table_proposer_name.setWidthPercentage(100);

			PdfPCell ProposalNumber_cell_1 = new PdfPCell(new Paragraph(
					"Quotation Number", small_normal));
			PdfPCell ProposalNumber_cell_2 = new PdfPCell(new Paragraph(
					QuatationNumber, normal1_bold));
			ProposalNumber_cell_2.setHorizontalAlignment(Element.ALIGN_CENTER);

			PdfPCell ProposalNumber_cell_3 = new PdfPCell(new Paragraph(
					"Channel/Intermediary", small_normal));
			PdfPCell ProposalNumber_cell_4 = new PdfPCell(new Paragraph(
					userType, normal1_bold));
			ProposalNumber_cell_4.setHorizontalAlignment(Element.ALIGN_CENTER);
			ProposalNumber_cell_1.setPadding(5);
			ProposalNumber_cell_2.setPadding(5);
			ProposalNumber_cell_3.setPadding(5);
			ProposalNumber_cell_4.setPadding(5);

			table_proposer_name.addCell(ProposalNumber_cell_1);
			table_proposer_name.addCell(ProposalNumber_cell_2);
			table_proposer_name.addCell(ProposalNumber_cell_3);
			table_proposer_name.addCell(ProposalNumber_cell_4);

			// inputTable here -1

			Paragraph intro = new Paragraph(
					"Introduction",
					normal_bold);
			intro.setAlignment(Element.ALIGN_LEFT | Element.ALIGN_JUSTIFIED);

			Paragraph intro2 = new Paragraph(
					"Insurance Regulatory & Development Authority of India (IRDAI) requires all life insurance companies operating in India to provide official illustrations to their customers.  The illustrations are based on the investment rates of return set by the Life Insurance Council (constituted under Section 64C(a) of the Insurance Act 1938) and is not intended to reflect the actual investment returns achieved or which may be achieved in future by SBI Life Insurance Company Limited. All life insurance companies use the same rates in their benefit illustrations.",
					normal1);
			intro2.setAlignment(Element.ALIGN_LEFT | Element.ALIGN_JUSTIFIED);

			Paragraph intro3 = new Paragraph(
					"The main objective of the illustration is that the client is able to appreciate the features of the product and the flow of benefits in different circumstances with some level of quantification. For further information on the product and its benefits,  please refer to the sales brochure and/or policy document.",
					normal1);
			intro3.setAlignment(Element.ALIGN_LEFT | Element.ALIGN_JUSTIFIED);

			PdfPTable personalDetail_table = new PdfPTable(8);
			personalDetail_table
					.setWidths(new float[]{3f, 3f, 3f, 3f, 3f, 3f, 3f, 3f});
			personalDetail_table.setWidthPercentage(100f);
			personalDetail_table.setHorizontalAlignment(Element.ALIGN_LEFT);

			// 1st row
			cell = new PdfPCell(new Phrase(
					"Proposer and Life Assured ", normal1_bold));
			cell.setColspan(8);
			cell.setBorder(Rectangle.BOTTOM);
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			personalDetail_table.addCell(cell);

			// 2 row
			cell = new PdfPCell(new Phrase("Name of the Prospect (Life Assured) ", normal1));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			personalDetail_table.addCell(cell);

			cell = new PdfPCell(new Phrase("Age of the Prospect (Life Assured)",
					normal1));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			personalDetail_table.addCell(cell);

			cell = new PdfPCell(new Phrase("Gender of the Prospect (Life Assured)", normal1));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			personalDetail_table.addCell(cell);

			cell = new PdfPCell(new Phrase("Name of the Child ",
					normal1));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			personalDetail_table.addCell(cell);

			cell = new PdfPCell(new Phrase("Age of the Child",
					normal1));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			personalDetail_table.addCell(cell);


			cell = new PdfPCell(new Phrase("Gender of Child", normal1));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			personalDetail_table.addCell(cell);

			cell = new PdfPCell(new Phrase("State", normal1));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			//personalDetail_table.addCell(cell);

			cell = new PdfPCell(new Phrase("Staff", normal1));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setColspan(2);
			personalDetail_table.addCell(cell);

			// 3 row

			if (proposer_Is_Same_As_Life_Assured.equalsIgnoreCase("y")) {

				cell = new PdfPCell(new Phrase(name_of_life_assured, normal1));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				personalDetail_table.addCell(cell);

			} else if (proposer_Is_Same_As_Life_Assured.equalsIgnoreCase("n")) {
				cell = new PdfPCell(new Phrase(name_of_proposer, normal1));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				personalDetail_table.addCell(cell);
			}

			cell = new PdfPCell(new Phrase(ageAtEntry + "", normal1));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			personalDetail_table.addCell(cell);

			cell = new PdfPCell(new Phrase(gender + "", normal1));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			personalDetail_table.addCell(cell);

			cell = new PdfPCell(new Phrase(name_of_proposer, normal1));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);

			personalDetail_table.addCell(cell);

			cell = new PdfPCell(new Phrase(proposer_Age, normal1));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			personalDetail_table.addCell(cell);


			cell = new PdfPCell(new Phrase(child_gender, normal1));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			personalDetail_table.addCell(cell);

			if (cb_kerladisc.isChecked()) {
				cell = new PdfPCell(new Phrase("Kerala", normal1));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				//personalDetail_table.addCell(cell);
			} else {
				cell = new PdfPCell(new Phrase("Non Kerala", normal1));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				//personalDetail_table.addCell(cell);
			}

			if (staffdiscount.equals("true")) {
				cell = new PdfPCell(new Phrase("Yes", normal1));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell.setColspan(2);
				personalDetail_table.addCell(cell);
			} else {
				cell = new PdfPCell(new Phrase("No", normal1));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell.setColspan(2);
				personalDetail_table.addCell(cell);
			}
			/*if (staffdiscount.equalsIgnoreCase("true"))
				cell = new PdfPCell(new Phrase("Yes", normal1));
			else
				cell = new PdfPCell(new Phrase("No", normal1));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			personalDetail_table.addCell(cell);
*/

			Paragraph read = new Paragraph(
					"How to read and understand this benefit illustration?", normal1_bold);
			read.setAlignment(Element.ALIGN_LEFT);
			Paragraph terms_12 = new Paragraph(
					"This benefit illustration is intended to show year-wise premiums payable and benefits under the policy, at two assumed rates of interest i.e, 8% p.a and 4% p.a.",
					normal1);
			terms_12.setAlignment(Element.ALIGN_LEFT | Element.ALIGN_JUSTIFIED);
			Paragraph terms_22 = new Paragraph(
					"Some benefits are guaranteed and some benefits are variable with returns based on the future performance of your insurer carrying on life insurance business. If your policy offers guaranteed benefits then these will be clearly marked “guaranteed” in the illustration table on this page. If your policy offers variable benefits then the illustrations on this page will show two different rates of assumed future investment returns, of 8% p.a. and 4% p.a. These assumed rates of return are not guaranteed and they are not the upper or lower limits of what you might get back, as the value of your policy is dependent on a number of factors including future investment performance.",
					normal1);
			terms_22.setAlignment(Element.ALIGN_LEFT | Element.ALIGN_JUSTIFIED);

			PdfPTable policyDetails_table = new PdfPTable(2);
			policyDetails_table
					.setWidths(new float[]{5f, 5f});
			policyDetails_table.setWidthPercentage(50f);
			policyDetails_table.setHorizontalAlignment(Element.ALIGN_LEFT);

			// 1st row
			cell = new PdfPCell(new Phrase(
					"Policy Detail ", normal1_bold));
			cell.setColspan(2);
			cell.setBorder(Rectangle.BOTTOM);
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			policyDetails_table.addCell(cell);

			//2nd row
			cell = new PdfPCell(new Phrase("Policy Option", normal1));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			policyDetails_table.addCell(cell);

			cell = new PdfPCell(new Phrase("Not Applicable", normal));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			policyDetails_table.addCell(cell);

			//3rd row
			cell = new PdfPCell(new Phrase("Policy Term (Years)", normal1));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			policyDetails_table.addCell(cell);

			cell = new PdfPCell(new Phrase(policyTermStr, normal));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			policyDetails_table.addCell(cell);

			String premPayment_term = "";

			if (!(premPayingMode.equalsIgnoreCase("Single"))) {

				premPayment_term = premPayingTerm;
			} else {
				premPayment_term = "1";
			}

			//4th row
			cell = new PdfPCell(new Phrase("Premium Payment Term (Years)", normal1));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			policyDetails_table.addCell(cell);

			cell = new PdfPCell(new Phrase(premPayment_term, normal));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			policyDetails_table.addCell(cell);

			//5th row
			cell = new PdfPCell(new Phrase("Mode / Frequency of Premium Payment", normal1));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			policyDetails_table.addCell(cell);

			cell = new PdfPCell(new Phrase(premPayingMode + "", normal));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			policyDetails_table.addCell(cell);

			cell = new PdfPCell(new Phrase("Bonus Type", normal1));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			policyDetails_table.addCell(cell);

			cell = new PdfPCell(new Phrase("Simple Reversionary Bonus", normal));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			policyDetails_table.addCell(cell);

			PdfPTable policyDetails_table2 = new PdfPTable(2);
			policyDetails_table2
					.setWidths(new float[]{5f, 5f});
			policyDetails_table2.setWidthPercentage(50f);
			policyDetails_table2.setHorizontalAlignment(Element.ALIGN_LEFT);

			//1st row
			cell = new PdfPCell(new Phrase("Amount of Installment Premium (Rs.)", normal1));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			policyDetails_table2.addCell(cell);

			cell = new PdfPCell(new Phrase(getformatedThousandString(Integer.parseInt(basicCoverYearlyPremium)) + "", normal));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			policyDetails_table2.addCell(cell);

			//2nd row
			cell = new PdfPCell(new Phrase("Sum Assured (Rs.)", normal1));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			policyDetails_table2.addCell(cell);

			cell = new PdfPCell(new Phrase(getformatedThousandString(Integer
					.parseInt(basicCoverSumAssured)) + "", normal));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			policyDetails_table2.addCell(cell);

			//3rd row
			cell = new PdfPCell(new Phrase("Sum Assured on Death (at inception of the policy) (Rs.)", normal1));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			policyDetails_table2.addCell(cell);

			cell = new PdfPCell(new Phrase(
					getformatedThousandString(Integer
							.parseInt(sum_assured_on_death)) + "", normal));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			policyDetails_table2.addCell(cell);

			//4th row
			cell = new PdfPCell(new Phrase("Rate of Applicable Taxes", normal1));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			policyDetails_table2.addCell(cell);

          /*  if (cb_kerladisc.isChecked()) {

                cell = new PdfPCell(new Phrase("4.75% in the 1st policy year and 2.375% from 2nd policy year onwards" + "", normal));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                policyDetails_table2.addCell(cell);
            } else {

                cell = new PdfPCell(new Phrase("4.5% in the 1st policy year and 2.25% from 2nd policy year onwards" + "", normal));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                policyDetails_table2.addCell(cell);
            }*/

			str_kerla_discount = prsObj.parseXmlTag(input, "str_kerla_discount");
			premPayingMode = prsObj.parseXmlTag(input, "premFreq");
			if (str_kerla_discount.equalsIgnoreCase("Yes")) {
				if (premPayingMode.equals("Single")) {
					cell = new PdfPCell(new Phrase("4.75% ", normal));
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					policyDetails_table2.addCell(cell);
				} else {
					cell = new PdfPCell(new Phrase("4.75% in the 1st policy year and 2.375% from 2nd policy year onwards" + "", normal));
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					policyDetails_table2.addCell(cell);
				}
			} else {
				if (premPayingMode.equals("Single")) {
					cell = new PdfPCell(new Phrase("4.50% ", normal));
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					policyDetails_table2.addCell(cell);
				} else {
					cell = new PdfPCell(new Phrase("4.5% in the 1st policy year and 2.25% from 2nd policy year onwards" + "", normal));
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					policyDetails_table2.addCell(cell);
				}
			}


			PdfPTable premium_table = new PdfPTable(4);
			premium_table
					.setWidths(new float[]{5f, 5f, 5f, 5f});
			premium_table.setWidthPercentage(100f);
			premium_table.setHorizontalAlignment(Element.ALIGN_LEFT);

			// 1st row
			cell = new PdfPCell(new Phrase(
					"Premium Summary ", normal1_bold));
			cell.setColspan(4);
			cell.setBorder(Rectangle.BOTTOM);
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			premium_table.addCell(cell);

			//2nd row
			cell = new PdfPCell(new Phrase("" + "", normal));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			premium_table.addCell(cell);

			cell = new PdfPCell(new Phrase("Base Plan", normal1));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			premium_table.addCell(cell);

			cell = new PdfPCell(new Phrase("Riders", normal1));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			premium_table.addCell(cell);

			cell = new PdfPCell(new Phrase("Total Installment Premium", normal1));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			premium_table.addCell(cell);

			//3rd row
			cell = new PdfPCell(new Phrase("Installment Premium without Applicable Taxes (Rs.)", normal1));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			premium_table.addCell(cell);

			cell = new PdfPCell(new Phrase(getformatedThousandString(Integer.parseInt(basicCoverYearlyPremium)) + "", normal));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			premium_table.addCell(cell);

			cell = new PdfPCell(new Phrase("Not Applicable", normal1));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			premium_table.addCell(cell);

			cell = new PdfPCell(new Phrase(getformatedThousandString(Integer.parseInt(basicCoverYearlyPremium)) + "", normal));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			premium_table.addCell(cell);

			//4th row
			cell = new PdfPCell(new Phrase("Installment Premium with Applicable Taxes: First Year (Rs.)", normal1));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			premium_table.addCell(cell);

			cell = new PdfPCell(new Phrase(getformatedThousandString(Integer.parseInt(totalPremium_YearlyPrem_With_ServiceTax_Prem)) + "", normal));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			premium_table.addCell(cell);

			cell = new PdfPCell(new Phrase("Not Applicable", normal1));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			premium_table.addCell(cell);

			cell = new PdfPCell(new Phrase(getformatedThousandString(Integer.parseInt(totalPremium_YearlyPrem_With_ServiceTax_Prem)) + "", normal));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			premium_table.addCell(cell);

			if (!premPayingMode.equalsIgnoreCase("Single")) {
				//5th row
                cell = new PdfPCell(new Phrase("Installment Premium with Applicable Taxes: 2nd Year Onwards (Rs.)", normal1));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				premium_table.addCell(cell);

				cell = new PdfPCell(new Phrase(getformatedThousandString(Integer.parseInt(premWthSTSecondYear)) + "", normal));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				premium_table.addCell(cell);

				cell = new PdfPCell(new Phrase("Not Applicable", normal1));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				premium_table.addCell(cell);

				cell = new PdfPCell(new Phrase(getformatedThousandString(Integer.parseInt(premWthSTSecondYear)) + "", normal));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				premium_table.addCell(cell);
			}
			if (premPayingMode.equalsIgnoreCase("Single")) {

				//5th row
				cell = new PdfPCell(new Phrase("Installment Premium with Applicable Taxes: 2nd Year onwards(Rs.)", normal1));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				premium_table.addCell(cell);

				cell = new PdfPCell(new Phrase("Not Applicable", normal));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				premium_table.addCell(cell);

				cell = new PdfPCell(new Phrase("Not Applicable", normal1));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				premium_table.addCell(cell);

				cell = new PdfPCell(new Phrase("Not Applicable", normal));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				premium_table.addCell(cell);

			}

			PdfPTable BI_Pdftable_note = new PdfPTable(1);
			BI_Pdftable_note.setWidthPercentage(100);
			PdfPCell BI_Pdftable_note_cell1 = new PdfPCell(new Paragraph(
					"Please Notes", small_bold));
			BI_Pdftable_note_cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
			BI_Pdftable_note_cell1.setPadding(5);

			BI_Pdftable_note.addCell(BI_Pdftable_note_cell1);


			PdfPTable BI_Pdftable6 = new PdfPTable(1);
			BI_Pdftable6.setWidthPercentage(100);
			PdfPCell BI_Pdftable6_cell6 = new PdfPCell(
					new Paragraph(
							"1. The premiums can be also paid by giving standing instruction to your bank or you can pay through your credit card.  ",
							small_normal));

			BI_Pdftable6_cell6.setPadding(5);

			BI_Pdftable6.addCell(BI_Pdftable6_cell6);


			PdfPTable BI_Pdftable7 = new PdfPTable(1);
			BI_Pdftable7.setWidthPercentage(100);
			PdfPCell BI_Pdftable7_cell1 = new PdfPCell(
					new Paragraph(
							"2. Tax deduction under Section 80 C is available. However in case the premium paid during the financial year, exceeds 10% of the sum assured, the benefit will be limited up to 10% of the sum assured. Tax exemption under Section 10(10D) is available at the time of maturity/surrender, subject to the premium not exceeding 10% of the sum assured in any of the years during the term of the policy. However, death proceeds are completely exempt.",
							small_normal));

			BI_Pdftable7_cell1.setPadding(5);

			// BI_Pdftable7.addCell(BI_Pdftable7_cell1);


			PdfPTable taxes_desc = new PdfPTable(1);
			taxes_desc.setWidthPercentage(100);
			PdfPCell taxes_desc_cell = new PdfPCell(
					new Paragraph(
							"2. Applicable Taxes (including surcharge/cess etc), at the rate notified by the Central Government/ State Government / Union Territories of India from time to time and as per the provisions of the prevalent tax laws will be payable on premium/ or any other charges as per the product features. ",

							small_normal));

			taxes_desc_cell.setPadding(5);

			taxes_desc.addCell(taxes_desc_cell);


			// J&k
			String isJk = "";
			if (isjkResident.isChecked()) {
				isJk = "yes";
			}

			PdfPTable table_is_JK = new PdfPTable(2);
			table_is_JK.setWidthPercentage(100);

			PdfPCell cell_is_JK1 = new PdfPCell(new Paragraph("J&K Resident",
					small_normal));
			cell_is_JK1.setPadding(5);

			PdfPCell cell_is_JK2 = new PdfPCell(new Paragraph(isJk, small_bold));
			cell_is_JK2.setPadding(5);
			cell_is_JK2.setHorizontalAlignment(Element.ALIGN_CENTER);

			table_is_JK.addCell(cell_is_JK1);
			table_is_JK.addCell(cell_is_JK2);

			// Basic Cover

			PdfPTable basicCover_table = new PdfPTable(4);
			basicCover_table.setWidths(new float[] { 5f, 5f, 5f, 5f });
			basicCover_table.setWidthPercentage(100f);
			basicCover_table.setHorizontalAlignment(Element.ALIGN_LEFT);

			// 1st row
			cell = new PdfPCell(new Phrase("Base Cover", normal1_bold));
			cell.setColspan(4);
			cell.setBorder(Rectangle.BOTTOM);
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			basicCover_table.addCell(cell);

			// 2 row
			cell = new PdfPCell(new Phrase("Policy Term (yrs)", normal1));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			basicCover_table.addCell(cell);

			cell = new PdfPCell(new Phrase("Premium Payment Term (yrs)",
					normal1));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			basicCover_table.addCell(cell);

			cell = new PdfPCell(new Phrase("Sum Assured (Rs)", normal1));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			basicCover_table.addCell(cell);

			cell = new PdfPCell(new Phrase("Premium " + premPayingMode
					+ " (Rs)", normal1));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			basicCover_table.addCell(cell);

			// 3 row
			cell = new PdfPCell(new Phrase(policyTermStr + "", normal1));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			basicCover_table.addCell(cell);

			if (!(premPayingMode.equals("Single"))) {
				cell = new PdfPCell(new Phrase(premPayingTerm + "", normal1));
			} else {
				cell = new PdfPCell(new Phrase("1", normal1));
			}
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

			PdfPTable premDetail_table = new PdfPTable(3);
			premDetail_table.setWidths(new float[] { 5f, 5f, 5f });
			premDetail_table.setWidthPercentage(100f);
			premDetail_table.setHorizontalAlignment(Element.ALIGN_LEFT);

			// 1st row
			cell = new PdfPCell(new Phrase(
					"Total Premium for Base Product (Rs.)", normal1_bold));
			cell.setColspan(4);
			cell.setBorder(Rectangle.BOTTOM);
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			premDetail_table.addCell(cell);

			// 2 row
			cell = new PdfPCell(new Phrase("", normal1));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			premDetail_table.addCell(cell);

			cell = new PdfPCell(new Phrase("Term (yrs)", normal1));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			premDetail_table.addCell(cell);

			cell = new PdfPCell(new Phrase("Premium " + premPayingMode
					+ " (Rs)", normal1));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			premDetail_table.addCell(cell);

			// 3 row
			cell = new PdfPCell(
					new Phrase(premPayingMode + " Premium", normal1));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			premDetail_table.addCell(cell);

			cell = new PdfPCell(new Phrase(policyTermStr + "", normal1));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			premDetail_table.addCell(cell);

			cell = new PdfPCell(new Phrase((currencyFormat.format(Double
					.parseDouble(basicCoverYearlyPremium))) + "", normal1));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			premDetail_table.addCell(cell);

			cell = new PdfPCell(new Phrase("Applicable Taxes", normal1));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			premDetail_table.addCell(cell);

			cell = new PdfPCell(new Phrase(policyTermStr + "", normal1));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			premDetail_table.addCell(cell);

			cell = new PdfPCell(new Phrase((currencyFormat.format(Double
					.parseDouble(totalPremium_ServiceTax_Prem))) + "", normal1));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			premDetail_table.addCell(cell);

			cell = new PdfPCell(new Phrase(
					premPayingMode + " Premium with Applicable Taxes", normal1));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			premDetail_table.addCell(cell);

			cell = new PdfPCell(new Phrase(policyTermStr + "", normal1));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			premDetail_table.addCell(cell);

			cell = new PdfPCell(
					new Phrase(
							(currencyFormat.format(Double
									.parseDouble(totalPremium_YearlyPrem_With_ServiceTax_Prem)))
									+ "", normal1));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			premDetail_table.addCell(cell);

			PdfPTable Table_backdating_premium_with_service_tax = new PdfPTable(
					2);
			Table_backdating_premium_with_service_tax.setWidthPercentage(100);

			PdfPCell cell_Backdate1 = new PdfPCell(new Paragraph(
					"Backdating Interest", small_normal));
			cell_Backdate1.setPadding(5);
			PdfPCell cell_Backdate2 = new PdfPCell(new Paragraph("Rs. "
					+ commonForAllProd.getRound(commonForAllProd
					.getStringWithout_E(Double.valueOf(BackdatingInt
							.equals("") ? "0" : BackdatingInt))),
					small_bold));
			cell_Backdate2.setPadding(5);
			cell_Backdate2.setHorizontalAlignment(Element.ALIGN_CENTER);

			//Table_backdating_premium_with_service_tax.addCell(cell_Backdate1);
			//Table_backdating_premium_with_service_tax.addCell(cell_Backdate2);

			/**** Added By - Priyanka Warekar 26-08-2015 - Start *****/
			// PdfPTable FY_SY_premDetail_table = new PdfPTable(7);
			// FY_SY_premDetail_table.setWidths(new float[] { 5f, 5f, 5f, 5f,
			// 5f,
			// 5f, 5f });
			PdfPTable FY_SY_premDetail_table = new PdfPTable(4);
			FY_SY_premDetail_table.setWidths(new float[] { 5f, 5f, 5f, 5f });
			FY_SY_premDetail_table.setWidthPercentage(100f);
			FY_SY_premDetail_table.setHorizontalAlignment(Element.ALIGN_LEFT);

			// 1st row
			cell = new PdfPCell(
					new Phrase(
							"Total Premium for Base Product and Rider (if opted) (in Rs.)",
							small_bold));
			cell.setColspan(7);
			cell.setBorder(Rectangle.BOTTOM);
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			FY_SY_premDetail_table.addCell(cell);

			// 2 row
			cell = new PdfPCell(new Phrase("", small_normal));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			FY_SY_premDetail_table.addCell(cell);

			cell = new PdfPCell(new Phrase(smartChampBean.getPremFreqMode()
					+ " Premium  (Rs.)", small_normal));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			FY_SY_premDetail_table.addCell(cell);

			// cell = new PdfPCell(new Phrase("(a)Basic Applicable Taxes(Rs.)",
			// small_normal));
			// cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			// FY_SY_premDetail_table.addCell(cell);

			// cell = new PdfPCell(new Phrase("(b)Swachh Bharat Cess(Rs.)",
			// small_normal));
			// cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			// FY_SY_premDetail_table.addCell(cell);
			//
			// cell = new PdfPCell(new Phrase("(c)Krishi Kalyan Cess(Rs.)",
			// small_normal));
			// cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			// FY_SY_premDetail_table.addCell(cell);

			cell = new PdfPCell(new Phrase("Applicable Taxes(Rs.)", small_normal));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			FY_SY_premDetail_table.addCell(cell);

			cell = new PdfPCell(new Phrase(smartChampBean.getPremFreqMode()
					+ " Premium with Applicable Taxes (Rs.)", small_normal));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			FY_SY_premDetail_table.addCell(cell);

			// System.out.println("Plan "+plan.substring(6, 8));
			// 3 row
			cell = new PdfPCell(new Phrase("First year", small_normal));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			FY_SY_premDetail_table.addCell(cell);

			cell = new PdfPCell(new Phrase(
					(getformatedThousandString(Integer.parseInt(totalPremium_YearlyPrem_Prem))),
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
					(getformatedThousandString(Integer.parseInt(totalPremium_ServiceTax_Prem))),
					small_normal));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			FY_SY_premDetail_table.addCell(cell);

			cell = new PdfPCell(
					new Phrase((getformatedThousandString(Integer.parseInt(totalPremium_YearlyPrem_With_ServiceTax_Prem))),
							small_normal));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			FY_SY_premDetail_table.addCell(cell);

			// 4 row
			if (!smartChampBean.getPremFreqMode().equalsIgnoreCase("Single")) {
				cell = new PdfPCell(new Phrase("Second year onwards",
						small_normal));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				FY_SY_premDetail_table.addCell(cell);

				cell = new PdfPCell(new Phrase(getformatedThousandString(Integer.parseInt(totalPremium_YearlyPrem_Prem)),
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
						new Phrase((getformatedThousandString(Integer.parseInt(servcTaxSecondYear))),
								small_normal));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				FY_SY_premDetail_table.addCell(cell);

				cell = new PdfPCell(
						new Phrase((getformatedThousandString(Integer.parseInt( premWthSTSecondYear))),
								small_normal));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				FY_SY_premDetail_table.addCell(cell);
			}

			// document.add(FY_SY_premDetail_table);
			/** Added By - Priyanka Warekar - 26-08-2015 - End ****/

			Paragraph note = new Paragraph("Please Note: ",
					normal1_BoldUnderline);
			note.setAlignment(Element.ALIGN_LEFT);
			Paragraph note_1 = new Paragraph(
					"1. *In case of monthly mode 3 months premiums have to be paid in advance.",
					normal1);
			note_1.setAlignment(Element.ALIGN_LEFT | Element.ALIGN_JUSTIFIED);
			Paragraph note_2 = new Paragraph(
					"2. The premiums can be paid by giving standing instruction to your bank or you can pay through your credit card (Visa and Master Card).",
					normal1);
			note_2.setAlignment(Element.ALIGN_LEFT | Element.ALIGN_JUSTIFIED);
			Paragraph note_3 = new Paragraph(
					"3. For Monthly Salary Saving Scheme (SSS), 2 month premium to be paid in advance and renewal premium payment is allowed only through Salary Deduction ",
					normal1);
			note_3.setAlignment(Element.ALIGN_LEFT | Element.ALIGN_JUSTIFIED);
			Paragraph note_4 = new Paragraph(
					"4. Tax deduction under Section 80 C is available. However in case the premium paid during the financial year, exceeds 10% of the sum assured, the benefit will be limited up to 10% of the sum assured. Tax exemption under Section 10(10D) is available at the time of maturity/surrender, subject to the premium not exceeding 10% of the sum assured in any of the years during the term of the policy. However, death proceeds are completely exempt. Tax benefits, are as per the provisions of the Income Tax laws & are subject to change from time to time. Please consult your tax advisor for details.",
					normal1);
			note_4.setAlignment(Element.ALIGN_LEFT | Element.ALIGN_JUSTIFIED);
			Paragraph note_5 = new Paragraph(
					"5. Applicable Taxes (including surcharge/cess etc), at the rate notified by the Central Government/ State Government / Union Territories of India from time to time and as per the provisions of the prevalent tax laws will be payable on premium/ or any other charges as per the product features. ",
					normal1);
			note_5.setAlignment(Element.ALIGN_LEFT | Element.ALIGN_JUSTIFIED);

			Paragraph termsCondition = new Paragraph(
					"Other Terms and Conditions", normal1_bold);
			termsCondition.setAlignment(Element.ALIGN_LEFT);
			Paragraph terms_1 = new Paragraph(
					"1. The benefit calculation is based on the age herein indicated and as applicable for healthy individual.",
					normal1);
			terms_1.setAlignment(Element.ALIGN_LEFT | Element.ALIGN_JUSTIFIED);
			Paragraph terms_2 = new Paragraph(
					"2. The above BI is subject to payment of stipulated premiums on due date.",
					normal1);
			terms_2.setAlignment(Element.ALIGN_LEFT | Element.ALIGN_JUSTIFIED);
			// Paragraph terms_3 = new Paragraph(
			// "3. Insurance is subject matter of solicitation.", normal1);
			// terms_3.setAlignment(Element.ALIGN_LEFT |
			// Element.ALIGN_JUSTIFIED);

			// Table here

			// Table here

			PdfPTable table1 = new PdfPTable(21);
			table1.setWidths(new float[] { 2f, 5f, 5f, 5f, 5f, 5f, 5f, 5f, 5f,
					5f, 5f, 5f, 5f, 5f, 5f, 5f, 5f, 5f, 5f, 5f, 5f});
			table1.setWidthPercentage(100);

			// 1st row
			cell = new PdfPCell(
					new Phrase(
							"BENEFIT ILLUSTRATION FOR SBI LIFE - Smart Champ Insurance",
							normal1_bold));
			cell.setColspan(23);
			cell.setBorder(Rectangle.BOTTOM);
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			table1.addCell(cell);

			cell = new PdfPCell(
					new Phrase(
							"Amount In Rs.",
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
			cell.setColspan(5);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table1.addCell(cell);
			cell = new PdfPCell(
					new Phrase("Non-Guaranteed Benefits @ 4% p.a.", normal_bold));
			cell.setColspan(4);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table1.addCell(cell);
			cell = new PdfPCell(
					new Phrase("Non-Guaranteed Benefits @ 8% p.a.", normal_bold));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setColspan(4);
			table1.addCell(cell);

			// 3rd
            cell = new PdfPCell(new Phrase("Total benefits Including Guaranteed and Non-Guaranteed benefits", normal_bold));
			cell.setColspan(6);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table1.addCell(cell);

			cell = new PdfPCell(new Phrase(
					"Guaranteed additions",
					normal_bold));
			cell.setRowspan(4);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table1.addCell(cell);

			cell = new PdfPCell(new Phrase("Survival benefit / Smart Benefits", normal_bold));
			cell.setRowspan(4);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table1.addCell(cell);

			cell = new PdfPCell(new Phrase(
					"Surrender Benefit",
					normal_bold));
			cell.setRowspan(4);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table1.addCell(cell);

			cell = new PdfPCell(new Phrase("Death Benefit", normal_bold));
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

			cell = new PdfPCell(new Phrase("Survival benefit / Smart Benefits", normal_bold));
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
			cell = new PdfPCell(new Phrase("Survival benefit / Smart Benefits", normal_bold));
			cell.setRowspan(4);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table1.addCell(cell);

			cell = new PdfPCell(new Phrase("Surrender benefit", normal_bold));
			cell.setRowspan(4);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table1.addCell(cell);

			cell = new PdfPCell(new Phrase("Survival  Benefit", normal_bold));
			cell.setColspan(2);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table1.addCell(cell);

			cell = new PdfPCell(new Phrase("Maturity Benefit", normal_bold));
			cell.setColspan(2);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table1.addCell(cell);

			cell = new PdfPCell(new Phrase("Death Benefit", normal_bold));
			cell.setColspan(2);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table1.addCell(cell);


			cell = new PdfPCell(new Phrase("Total Survival Benefit @ 4% (4+10)", normal_bold));
			cell.setRowspan(3);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table1.addCell(cell);
			cell = new PdfPCell(new Phrase("Total Survival Benefit @ 8% (4+14)", normal_bold));
			cell.setRowspan(3);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table1.addCell(cell);

			cell = new PdfPCell(new Phrase("Total Maturity benefit, incl. Terminal bonus, if any, @ 4% (7+8+9)", normal_bold));
			cell.setRowspan(3);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table1.addCell(cell);
			cell = new PdfPCell(new Phrase("Total Maturity benefit, incl. Terminal bonus, if any, @ 8% (7+12+13)", normal_bold));
			cell.setRowspan(3);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table1.addCell(cell);
			cell = new PdfPCell(new Phrase("Total Death benefit, incl. Terminal bonus, if any, @ 4% (6)", normal_bold));
			cell.setRowspan(3);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table1.addCell(cell);

			cell = new PdfPCell(new Phrase("Total Death benefit, incl. Terminal bonus, if any, @ 8% (6)", normal_bold));
			cell.setRowspan(3);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table1.addCell(cell);

			cell = new PdfPCell(new Phrase("1", normal_bold));
			cell.setRowspan(4);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table1.addCell(cell);
			cell = new PdfPCell(new Phrase("2", normal_bold));
			cell.setRowspan(4);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table1.addCell(cell);
			cell = new PdfPCell(new Phrase("3", normal_bold));
			cell.setRowspan(4);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table1.addCell(cell);
			cell = new PdfPCell(new Phrase("4", normal_bold));
			cell.setRowspan(4);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table1.addCell(cell);
			cell = new PdfPCell(new Phrase("5", normal_bold));
			cell.setRowspan(4);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table1.addCell(cell);
			cell = new PdfPCell(new Phrase("6", normal_bold));
			cell.setRowspan(4);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table1.addCell(cell);
			cell = new PdfPCell(new Phrase("7", normal_bold));
			cell.setRowspan(4);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table1.addCell(cell);
			cell = new PdfPCell(new Phrase("8", normal_bold));
			cell.setRowspan(4);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table1.addCell(cell);
			cell = new PdfPCell(new Phrase("9", normal_bold));
			cell.setRowspan(4);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table1.addCell(cell);
			cell = new PdfPCell(new Phrase("10", normal_bold));
			cell.setRowspan(4);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table1.addCell(cell);
			cell = new PdfPCell(new Phrase("11", normal_bold));
			cell.setRowspan(4);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table1.addCell(cell);
			cell = new PdfPCell(new Phrase("12", normal_bold));
			cell.setRowspan(4);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table1.addCell(cell);
			cell = new PdfPCell(new Phrase("13", normal_bold));
			cell.setRowspan(4);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table1.addCell(cell);
			cell = new PdfPCell(new Phrase("14", normal_bold));
			cell.setRowspan(4);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table1.addCell(cell);
			cell = new PdfPCell(new Phrase("15", normal_bold));
			cell.setRowspan(4);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table1.addCell(cell);
			cell = new PdfPCell(new Phrase("16", normal_bold));
			cell.setRowspan(4);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table1.addCell(cell);
			cell = new PdfPCell(new Phrase("17", normal_bold));
			cell.setRowspan(4);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table1.addCell(cell);
			cell = new PdfPCell(new Phrase("18", normal_bold));
			cell.setRowspan(4);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table1.addCell(cell);
			cell = new PdfPCell(new Phrase("19", normal_bold));
			cell.setRowspan(4);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table1.addCell(cell);
			cell = new PdfPCell(new Phrase("20", normal_bold));
			cell.setRowspan(4);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table1.addCell(cell);
			cell = new PdfPCell(new Phrase("21", normal_bold));
			cell.setRowspan(4);
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
								.getNon_guaranteed_survival_benefit_4per())),
						normal));
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
								.getNon_guaranteed_survival_benefit_8per())),
						normal));
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

			Paragraph benefitsofDeath = new Paragraph(
					"Notes:", small_bold);
			benefitsofDeath.setAlignment(Element.ALIGN_LEFT);

			Paragraph notes = new Paragraph(
					"1. Annualized premium shall be the premium amount payable in a year chosen by the policyholder, excluding the taxes,  underwriting extra premiums and loading for modal premiums, if any / Single premium shall be the premium amount payable in lumpsum at  inception of the policy as chosen by the policyholder, excluding the taxes and underwriting extra premiums, if any. Refer sales literature for explanation of terms used in this illustration.", normal1);
			notes.setAlignment(Element.ALIGN_LEFT);
			Paragraph notes2 = new Paragraph(
					"2. All Benefit amount are derived on the assumption that the policies are ''in-force''", normal1);
			notes2.setAlignment(Element.ALIGN_LEFT);
			Paragraph notes3 = new Paragraph(
					"3. In addition to Guaranteed Surrender Benefits (column 5), Surrender value of the vested bonuses will also be paid. For the purpose of guaranteed surrender value (GSV) in this illustration the surrender value of vested bonuses are not considered at all.", normal1);
			notes3.setAlignment(Element.ALIGN_LEFT);
			Paragraph notes4 = new Paragraph(
					"4. The Death Benefits  shown under column 6,20,21  are payable on death or on Accidental Total Permanent Disability(ATPD) whichever is earlier.", normal1);
			notes4.setAlignment(Element.ALIGN_LEFT);

			Paragraph bonusRate = new Paragraph("Bonus Rates", small_bold);
			bonusRate.setAlignment(Element.ALIGN_LEFT);
			Paragraph bonusRate1 = new Paragraph(
					"This is a with profit plan and participates in the profits of the company’s life insurance business. Simple reversionary bonuses are declared as a percentage rate, which apply to the sum assured of the basic policy. Terminal bonuses, if any, are declared as a percentage rate, which apply to the vested reversionary bonus.\n" +
							"The bonus rates in the benefit illustration are constant. However, in practice, future bonuses are not guaranteed and will depend on future profits. Therefore, the bonuses are shown as non-guaranteed benefits and are calculated so that they are consistent with the two projected investment return assumptions of 4% and 8% per annum. \n",
					normal1);
			bonusRate1.setAlignment(Element.ALIGN_LEFT
					| Element.ALIGN_JUSTIFIED);
			Paragraph bonusRate2 = new Paragraph(
					"Simple reversionary bonuses are declared as a percentage rate, which apply to the sum assured of the basic policy. Once declared, they form a part of the guaranteed benefits of the plan. We may also pay a terminal bonus in addition to the above reversionary bonus at the time of termination of the policy (i.e. death, maturity or surrender).",
					normal1);
			bonusRate2.setAlignment(Element.ALIGN_LEFT
					| Element.ALIGN_JUSTIFIED);
			Paragraph bonusRate3 = new Paragraph(
					"The bonus rates in the benefit illustration are shown as constant. However, in practice, future bonuses are not guaranteed and will depend on future profits. The bonuses are shown as non-guaranteed benefits and are calculated so that they are consistent with the two projected investment return assumptions of 4% and 8% per annum.",
					normal1);
			bonusRate3.setAlignment(Element.ALIGN_LEFT
					| Element.ALIGN_JUSTIFIED);

			Paragraph surrender = new Paragraph("Surrender Value For Policy :", small_bold);
			surrender.setAlignment(Element.ALIGN_LEFT);
			/*Paragraph surrender_value = new Paragraph(
					"Surrender value is available on the basic policy benefit.",
					normal1);
			surrender_value.setAlignment(Element.ALIGN_LEFT
					| Element.ALIGN_JUSTIFIED);*/

			Paragraph guarnteedSurrender = new Paragraph(
					"Guaranteed Surrender Value (GSV)", small_bold);
			guarnteedSurrender.setAlignment(Element.ALIGN_LEFT);
			Paragraph guarnteedSurrender_value1 = new Paragraph(
					"1) For LPPT Policies, the policy will acquire a paid-up and/or surrender value only if premiums have been paid for at least 2 full years for policy term less than 10 years and at least 3 full years for policy term 10 years or more.",
					normal1);
			guarnteedSurrender_value1.setAlignment(Element.ALIGN_LEFT
					| Element.ALIGN_JUSTIFIED);
			/*
			 * Paragraph guarnteedSurrender_value11 = new Paragraph(
			 * "The Guaranteed Surrender Value (GSV) ) in case of limited premium payment policies will be equal to GSV factors multiplied by the basic premiums paid. Basic premium is equal to total premium less Applicable Taxes and cess, underwriting extra premiums, extra premium due to modal factors, if any."
			 * , normal1);
			 * guarnteedSurrender_value11.setAlignment(Element.ALIGN_LEFT |
			 * Element.ALIGN_JUSTIFIED);
			 */

			Paragraph guarnteedSurrender_value2 = new Paragraph(
					"2) For Single Premium Policies:-", normal1);
			guarnteedSurrender_value2.setAlignment(Element.ALIGN_LEFT
					| Element.ALIGN_JUSTIFIED);
			Paragraph guarnteedSurrender_value21 = new Paragraph(
					"The policy acquires surrender value after date of commencement of risk.",
					normal1);
			guarnteedSurrender_value21.setAlignment(Element.ALIGN_LEFT
					| Element.ALIGN_JUSTIFIED);
			Paragraph guarnteedSurrender_value22 = new Paragraph(
					"The Guaranteed Surrender Value (GSV) is equal to GSV factors multiplied by the basic premiums paid less any sum assured part of the smart benefit instalments already paid. Basic premium is equal to total premium excluding applicable taxes, underwriting extra premiums, extra premium due to modal factors and rider premiums, if any. Surrender value of vested bonuses, if any, is also added to the GSV.",
					normal1);
			guarnteedSurrender_value22.setAlignment(Element.ALIGN_LEFT
					| Element.ALIGN_JUSTIFIED);
			/*
			 * Paragraph guarnteedSurrender_value23 = new Paragraph(
			 * "From fourth policy year onwards, the Guaranteed Surrender Value (GSV) will be 90% of Single Premium (exclusive of Applicable Taxes) paid excluding extra premiums (underwriting extra), if any, plus cash value of the allocated bonuses."
			 * , normal1);
			 * guarnteedSurrender_value23.setAlignment(Element.ALIGN_LEFT |
			 * Element.ALIGN_JUSTIFIED);
			 *
			 * Paragraph guarnteedSurrender_value3 = new Paragraph(
			 * "3) Surrender during last 3 years", normal1);
			 * guarnteedSurrender_value3.setAlignment(Element.ALIGN_LEFT |
			 * Element.ALIGN_JUSTIFIED); Paragraph guarnteedSurrender_value31 =
			 * new Paragraph(
			 * "GSV will be applicable after commencement of smart benefits. Surrender value will be equal to GSV factors multiplied by the basic premiums paid. Surrender value of the vested bonuses, is any, is also added to this GSV."
			 * , normal1);
			 * guarnteedSurrender_value31.setAlignment(Element.ALIGN_LEFT |
			 * Element.ALIGN_JUSTIFIED);
			 */

			Paragraph policySurrender = new Paragraph(
					"Company's Policy on Surrender", small_bold);
			policySurrender.setAlignment(Element.ALIGN_LEFT);
			Paragraph policySurrender_value = new Paragraph(
					"In practice, the company may pay a surrender value(Non-Guaranteed Special Surrender Value or SSV) which could be higher than the guaranteed surrender value. The benefits payable on surrender reflects the value of your policy, which is assessed based on the past financial/demographic experience of the company with regard to your policy/group of similar policies, as well as the likely future experience. The surrender value payable may be reviewed from time to time depending on company's experience of the various factors which impact the surrender values that may be paid.The surrender value would be higher of GSV or SSV.",
					normal1);
			policySurrender_value.setAlignment(Element.ALIGN_LEFT
					| Element.ALIGN_JUSTIFIED);

			PdfPTable table4 = new PdfPTable(10);
			table4.setWidths(new float[] { 0.9f, 2.1f, 0.8f, 1.7f, 4.7f, 3f,
					0.8f, 1.7f, 2.9f, 3.5f });
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
			Paragraph new_line = new Paragraph("\n");

			document.add(table);

			// document.add(ls);
			//document.add(para3);
			// document.add(para4);
			//document.add(para5);
			//document.add(para6);

			//document.add(para8);

			document.add(new_line);

			// document.add(main_table);

			document.add(table_proposer_name);
			document.add(new_line);
			document.add(intro);
			document.add(intro2);
			document.add(intro3);
			document.add(new_line);
			document.add(personalDetail_table);
			document.add(new_line);
			document.add(read);
			document.add(terms_12);
			document.add(terms_22);
			document.add(new_line);
			document.add(policyDetails_table);
			//document.add(new_line);
			document.add(policyDetails_table2);
			document.add(new_line);
			document.add(premium_table);
			document.add(new_line);
			document.add(BI_Pdftable_note);
			document.add(BI_Pdftable6);
			document.add(BI_Pdftable7);
			document.add(taxes_desc);
			document.add(new_line);
			//document.add(policyDetails_table2);

			if (isjkResident.isChecked()) {
				document.add(table_is_JK);
			}
			//document.add(basicCover_table);
			//document.add(premDetail_table);
			//document.add(new_line);
			//document.add(FY_SY_premDetail_table);
			//document.add(new_line);
			document.add(Table_backdating_premium_with_service_tax);
			document.add(new_line);
			//document.add(note);
			//document.add(note_1);
			//document.add(note_2);
			//document.add(note_3);
			//document.add(note_4);
			//document.add(note_5);

			//document.add(new_line);
			//document.add(termsCondition);
			//document.add(terms_1);
			//document.add(terms_2);
			// document.add(terms_3);

			document.add(new_line);
			document.add(table1);

			document.add(new_line);
			document.add(benefitsofDeath);
			document.add(notes);
			document.add(notes2);
			document.add(notes3);
			document.add(notes4);
			//document.add(benefitsofDeath1);
			/*document.add(benefitsofDeath2);*/
			//document.add(benefitsofDeath3);

			document.add(new_line);
			document.add(bonusRate);
			document.add(bonusRate1);
			//document.add(bonusRate2);
			//document.add(bonusRate3);

			//document.add(new_line);
			//document.add(surrender);
			//document.add(surrender_value);

			//document.add(new_line);
			//document.add(guarnteedSurrender);
			//document.add(guarnteedSurrender_value1);
			// document.add(guarnteedSurrender_value11);

			//document.add(new_line);
			//document.add(guarnteedSurrender_value2);
			//document.add(guarnteedSurrender_value21);
			//document.add(guarnteedSurrender_value22);
			// document.add(guarnteedSurrender_value23);

			//document.add(new_line);
			// document.add(guarnteedSurrender_value3);
			// document.add(guarnteedSurrender_value31);

			//document.add(new_line);
			//document.add(policySurrender);
			//document.add(policySurrender_value);

			document.add(new_line);
			PdfPTable important = new PdfPTable(1);
			important.setWidthPercentage(100);
			PdfPCell important2 = new PdfPCell(new Paragraph(
					" Important", small_bold));
			important2.setHorizontalAlignment(Element.ALIGN_LEFT);
			important2.setPadding(5);

			important.addCell(important2);

			PdfPTable BI_Pdftable_CompanysPolicySurrender2 = new PdfPTable(1);
			BI_Pdftable_CompanysPolicySurrender2.setWidthPercentage(100);
			PdfPCell BI_Pdftable_CompanysPolicySurrender2_cell = new PdfPCell(
					new Paragraph(
							"You may receive a Welcome Call from our representative to confirm your proposal details like Date of Birth, Nominee Name, Address, Email ID, Sum Assured, Premium amount, Premium Payment Term etc.",
							small_normal));

			BI_Pdftable_CompanysPolicySurrender2_cell.setPadding(5);

			BI_Pdftable_CompanysPolicySurrender2
					.addCell(BI_Pdftable_CompanysPolicySurrender2_cell);
			document.add(important);
			document.add(BI_Pdftable_CompanysPolicySurrender2);

			PdfPTable BI_Pdftable_CompanysPolicySurrender3 = new PdfPTable(1);
			BI_Pdftable_CompanysPolicySurrender3.setWidthPercentage(100);
			PdfPCell BI_Pdftable_CompanysPolicySurrender3_cell = new PdfPCell(
					new Paragraph(
							"You may have to undergo Medical Test based on our Underwriting Requirements.",
							small_normal));

			BI_Pdftable_CompanysPolicySurrender3_cell.setPadding(5);

			BI_Pdftable_CompanysPolicySurrender3
					.addCell(BI_Pdftable_CompanysPolicySurrender3_cell);
			document.add(BI_Pdftable_CompanysPolicySurrender3);

			PdfPTable BI_Pdftable_CompanysPolicySurrender4 = new PdfPTable(1);
			BI_Pdftable_CompanysPolicySurrender4.setWidthPercentage(100);
			PdfPCell BI_Pdftable_CompanysPolicySurrender4_cell = new PdfPCell(
					new Paragraph(
							"You have to submit Proof of source of Fund",
							small_normal));

			BI_Pdftable_CompanysPolicySurrender4_cell.setPadding(5);

			BI_Pdftable_CompanysPolicySurrender4
					.addCell(BI_Pdftable_CompanysPolicySurrender4_cell);

			if (Double.parseDouble(basicCoverYearlyPremium) > 100000) {
				//document.add(BI_Pdftable_CompanysPolicySurrender4);
			}

           /* if (premPayingMode.equalsIgnoreCase("Single")) {

				PdfPTable BI_Pdftable_CompanysPolicySurrender4 = new PdfPTable(1);
				BI_Pdftable_CompanysPolicySurrender4.setWidthPercentage(100);
				PdfPCell BI_Pdftable_CompanysPolicySurrender4_cell = new PdfPCell(
						new Paragraph(
                                "Your SBI Life - Smart Champ Insurance (UIN: 111N098V03) is a Single Premium Policy  and you are required to pay Limited Premium Policy for which your first year Quarterly Premmium is Rs.15825. Your Policy Term is 15 years , Premium Payment Term is 15 years with Sum Assured is Rs.700000",
								small_normal));

				BI_Pdftable_CompanysPolicySurrender4_cell.setPadding(5);

				BI_Pdftable_CompanysPolicySurrender4
						.addCell(BI_Pdftable_CompanysPolicySurrender4_cell);
				document.add(BI_Pdftable_CompanysPolicySurrender4);
            }*/

			PdfPTable BI_Pdftable_CompanysPolicySurrender5 = new PdfPTable(1);
			BI_Pdftable_CompanysPolicySurrender5.setWidthPercentage(100);
			PdfPCell BI_Pdftable_CompanysPolicySurrender5_cell = new PdfPCell(
					new Paragraph(Company_policy_surrender_dec, small_normal));

			BI_Pdftable_CompanysPolicySurrender5_cell.setPadding(5);

			BI_Pdftable_CompanysPolicySurrender5
					.addCell(BI_Pdftable_CompanysPolicySurrender5_cell);
			document.add(BI_Pdftable_CompanysPolicySurrender5);

			document.add(new_line);
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
									+ " , having received the information with respect to the above, have understood the above statement before entering into a contract",
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
//            document.add(para_img_logo_after_space_1);

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
				document.add(new_line);



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
			commonMethods.dialogWarning(context,"Please Fill  Mobile No", true);
			edt_proposerdetail_basicdetail_contact_no.requestFocus();
			return false;
		} else if (edt_proposerdetail_basicdetail_contact_no.getText()
				.toString().length() != 10) {
			commonMethods.dialogWarning(context,"Please Fill 10 Digit Mobile No", true);
			edt_proposerdetail_basicdetail_contact_no.requestFocus();
			return false;
		}

		/*else if (emailId.equals("")) {
			commonMethods.dialogWarning(context,"Please Fill Email Id", true);
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
		}

		else if (!ConfirmEmailId.equals("")) {

			email_id_validation(ConfirmEmailId);
			if (validationFla1) {

				if (emailId.equals("")) {
					commonMethods.dialogWarning(context,"Please Fill Email Id", true);
					edt_proposerdetail_basicdetail_Email_id.requestFocus();
					return false;

				}

				return true;
			} else {
				commonMethods.dialogWarning(context,"Please Fill Valid Confirm Email Id", true);
				edt_proposerdetail_basicdetail_ConfirmEmail_id.requestFocus();
				return false;
			}
		}

		else {
			return true;
		}

		// else if(Integer.parseInt(proposer_age)!=age_ByButton)
		// {
		// dialog("Birth Does NOt Match With Bi Detail", true);
		// return false;
		// }
		// else if (validationFla1 == false || validationFlag2 == false
		// || validationFlag3 == false) {
		// dialog("Please Rectify The Error Field", true);
		// return false;
		// } else {
		// return true;
		// }
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



	/********************************** Added by Akshaya on 03-Mar-2015 start ***************************/
    /*private boolean TrueBackdate() {

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


	boolean TrueBackdate() {
		try {
			String error = "";

			if (rb_proposerdetail_personaldetail_backdating_yes.isChecked()) {

				final Calendar c = Calendar.getInstance();
				final int currYear = c.get(Calendar.YEAR);
				final int currMonth = c.get(Calendar.MONTH) + 1;

				SimpleDateFormat dateformat1 = new SimpleDateFormat(
						"dd-MM-yyyy");
				Date dtBackDate = dateformat1
						.parse(btn_proposerdetail_personaldetail_backdatingdate.getText()
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
				}
				/** Added by Akshaya on 24-08-2015 start **/
				else if (dtBackDate.before(launchDate)
						&& finYerEndDate.before(launchDate)) {
					error = "Please enter Backdation date between "
							+ dateformat1.format(launchDate) + " and "
							+ dateformat1.format(currentDate);
				}
				/** Added by Akshaya on 24-08-2015 end **/
				else if (dtBackDate.before(finYerEndDate)) {
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
