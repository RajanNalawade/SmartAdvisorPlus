package sbilife.com.pointofsale_bancaagency.smartshield;

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
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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

@SuppressWarnings("deprecation")
public class BI_SmartShieldActivity extends AppCompatActivity implements
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

	private String totalPremWithoutDisc;

	/* For DashBoard */

	private boolean valPremiumError = false;
	private boolean valRiderPremiumError = false;
	private  boolean loandetails = false;

	// To Store User Info and sync info

	// UI Elements
	private CheckBox CbJkResident, cbADBRider, cbATPDBRider, cb_bi_smart_shield_criti_care_13_non_linked_rider, cb_staffdisc;
	private Button btnSubmit, btnBack;
	private Spinner spnr_bi_smart_shield_life_assured_age;
	private TableRow tr_rate_of_interest, tr_ADB_rider, tr_ATPDB_Rider,tr_ADB_rider2,
			tr_ATPDB_Rider2, tr_ACI, tr_ACI2, tr_rate_of_interest1,
			tr_bi_smart_shield_criti_care_13_non_linked_rider;

	private String isJkResident = "", aciRiderStatus = "", adbRiderStatus = "",
			atpdbRiderStatus = "";
	// For BI
	private StringBuilder retVal;
	private StringBuilder bussIll = null;

	private String QuatationNumber = "";
	private String planName = "";

	// class declaration
	private CommonForAllProd obj;
	private SmartShieldBean smartShieldBean;

	// variable declaration
	private DecimalFormat currencyFormat;
	private AlertDialog.Builder showAlert;

	private RadioButton rb_smart_shield_proposer_smoker_yes,
			rb_smart_shield_proposer_smoker_no;

	private Spinner spnr_bi_smart_shield_life_assured_title;
	private EditText edt_bi_smart_shield_life_assured_first_name,
			edt_bi_smart_shield_life_assured_middle_name,
			edt_bi_smart_shield_life_assured_last_name;

	private Spinner spnrPremFrequency, spnrPolicyTerm, spnrPlan, spnrGender,
			spnr_AdbTerm, spnr_AtpdbTerm, spnr_bi_smart_shield_criti_care_13_non_linked_rider_term, spnrLoanRateInterest;
	private Button btn_bi_smart_shield_life_assured_date;

	private Button btn_MarketingOfficalDate;
	private Button btn_PolicyholderDate;

	private ImageButton Ibtn_signatureofMarketing,Ibtn_signatureofPolicyHolders;

	private String product_name = "";
	private String lifeAssured_Title = "";
	private String lifeAssured_First_Name = "";
	private String lifeAssured_Middle_Name = "";
	private String lifeAssured_Last_Name = "";
    private String lifeAssuredAge = "";
	private String name_of_life_assured = "";
	private String lifeAssured_date_of_birth = "";
	private String Is_Smoker_or_Not = "";
	private String lifeAssured_loan_rate_interest = "";
	private String loanAccountNo = "";
	private String financingInstituteName = "";
	private String loanCategory = "";
	private String sumAssuredOutStanding = "";
	private String balanceLoanTenure = "";
	private String firstEmiDate = "";
	private String lastEmiDate = "";
	// For Bi Dialog
	private ParseXML prsObj;
	private String name_of_proposer = "";
	private String name_of_person = "";
	private String place2 = "";
	private String date1 = "";
	private String date2 = "";
	private String agent_sign = "";
	private String proposer_sign = "";
	private final String proposer_Is_Same_As_Life_Assured = "Y";
	private String output = "";

	private Dialog d;
	private final int SIGNATURE_ACTIVITY = 1;
	private String latestImage = "";
	// Variable Used For Date Purpose
	private static final int DATE_DIALOG_ID = 1;
	private static int DIALOG_ID;
	private int mYear;
	private int mMonth;
	private int mDay;

	// for BI
	private StringBuilder inputVal;

	private String premPayingMode = "", aciTerm = "", adbRiderTerm = "",
			atpdbRiderTerm = "",  policyTerm = "",
			ageAtEntry = "", gender = "", sumAssured = "", aciSA = "",
			adbSA = "", atpdbSA = "", premiumFreq = "",
			adbRiderYearly = "", atpdbRiderYearly = "",ptRiderYearly = "";


	private String basicCoverSumAssured = "";
	private String basicCoverYearlyPremium = "";
	private String planProposedName = "";
	private String totalPremiumYearlyInstallmentPremWithServiceTaxYearly = "";


	private EditText edtAdbSA, edtAtpdbSA, edtBasicSA, et_bi_smart_shield_criti_care_13_non_linked_rider_sum_assured,
			et_loan_account_no, et_financing_institute_name, et_loan_category,
			et_sum_assured_outstanding_loan_amount, et_balance_loan_tenure;

	private LinearLayout ll_loan_protection;

	// Variable declaration
	private  int age = 0;


	private boolean flagFirstFocus = true;
	private EditText edt_proposerdetail_basicdetail_contact_no,
			edt_proposerdetail_basicdetail_Email_id,
			edt_proposerdetail_basicdetail_ConfirmEmail_id;

	private String mobileNo = "";
	private String emailId = "";
	private String ConfirmEmailId = "";
	private boolean validationFla1 = false;
	private String ProposerEmailId = "";
	private Bitmap photoBitmap;
	private File mypath;
	private String smoker_or_not = "";

	private int b;
	private Button btn_plandetail_loan_1st_emi_date, btn_plandetail_loan_last_emi_date;

	/* Added by vrushali on 09/04/2015 end */

    private String emi_first_date = "", emi_second_date = "";
    private int emiage_1st = 0, emiage_2nd = 0;
	// ,servcTax="";basicServiceTax="",

	/*** Added by Akshaya on 31-Mar-15 start ***/
	private String product_Code, product_UIN, product_cateogory, product_type;

	private String bankUserType = "",mode = "",basicServiceTax = "";

	private Context context;
	private String Check = "";
	private CommonMethods commonMethods;
	private StorageUtils mStorageUtils;
	private ImageButton imageButtonSmartShieldProposerPhotograph;

	private LinearLayout linearlayoutThirdpartySignature,
			linearlayoutAppointeeSignature;
	private ImageButton Ibtn_signatureofThirdParty, Ibtn_signatureofAppointee;
	private String thirdPartySign = "", appointeeSign = "", Company_policy_surrender_dec = "";

	private CheckBox cb_kerladisc;

    private String str_kerla_discount = "No";
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.bi_smart_shieldmain);
		//getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);


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
					product_name = "Smart Shield";
					planName = "Smart Shield";
					product_Code = prodInfoObj.getProductCode(product_name);
					product_UIN = prodInfoObj.getProductUIN(product_name);
					product_cateogory = prodInfoObj
							.getProductCategory(product_name);
					product_type = prodInfoObj.getProductType(product_name);
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

		initialiseDate();
		obj = new CommonForAllProd();
		obj = new CommonForAllProd();
		smartShieldBean = new SmartShieldBean();
		showAlert = new AlertDialog.Builder(this);
		prsObj = new ParseXML();


		btn_bi_smart_shield_life_assured_date = findViewById(R.id.btn_bi_smart_shield_life_assured_date);
		spnr_bi_smart_shield_life_assured_age = findViewById(R.id.spnr_bi_smart_shield_age);
		spnr_bi_smart_shield_life_assured_age.setEnabled(false);

		spnr_bi_smart_shield_life_assured_title = findViewById(R.id.spnr_bi_smart_shield_life_assured_title);
		edt_bi_smart_shield_life_assured_first_name = findViewById(R.id.edt_bi_smart_shield_life_assured_first_name);
		edt_bi_smart_shield_life_assured_middle_name = findViewById(R.id.edt_bi_smart_shield_life_assured_middle_name);
		edt_bi_smart_shield_life_assured_last_name = findViewById(R.id.edt_bi_smart_shield_life_assured_last_name);
		edtBasicSA = findViewById(R.id.et_bi_smart_shield_sum_assured);
		edtAdbSA = findViewById(R.id.et_bi_smart_shield_adb_rider_sum_assured);
		edtAtpdbSA = findViewById(R.id.et_bi_smart_shield_atpd_rider_sum_assured);

		et_bi_smart_shield_criti_care_13_non_linked_rider_sum_assured = findViewById(R.id.et_bi_smart_shield_criti_care_13_non_linked_rider_sum_assured);

		rb_smart_shield_proposer_smoker_yes = findViewById(R.id.rb_smart_shield_proposer_smoker_yes);
		rb_smart_shield_proposer_smoker_no = findViewById(R.id.rb_smart_shield_proposer_smoker_no);

		spnr_AdbTerm = findViewById(R.id.spnr_bi_smart_shield_adb_rider_term);
		spnr_AtpdbTerm = findViewById(R.id.spnr_bi_smart_shield_atpdb_rider_term);

		spnr_bi_smart_shield_criti_care_13_non_linked_rider_term = findViewById(R.id.spnr_bi_smart_shield_criti_care_13_non_linked_rider_term);

		spnrPlan = findViewById(R.id.spnr_bi_smart_shield_plan);
		spnrGender = findViewById(R.id.spnr_bi_smart_shield_selGender);
//        spnrGender.setClickable(false);
//        spnrGender.setEnabled(false);

		spnrPremFrequency = findViewById(R.id.spnr_bi_smart_shield_premium_paying_mode);
		spnrPolicyTerm = findViewById(R.id.spnr_bi_smart_shield_policyterm);
		spnrLoanRateInterest = findViewById(R.id.spnr_bi_smart_shield_loan_rate_of_interest);

		//spnr_bi_smart_shield_criti_care_13_non_linked_rider_term.setEnabled(false);

		CbJkResident = findViewById(R.id.cb_bi_smart_shield_JKResident);
		cb_staffdisc = findViewById(R.id.cb_staffdisc);
		cbADBRider = findViewById(R.id.cb_bi_smart_shield_adb_rider);

		cb_bi_smart_shield_criti_care_13_non_linked_rider = findViewById(R.id.cb_bi_smart_shield_criti_care_13_non_linked_rider);

		cbATPDBRider = findViewById(R.id.cb_bi_smart_shield_atpdb_rider);

		tr_rate_of_interest = findViewById(R.id.tr_bi_smart_shield_rate_of_interest);
		tr_rate_of_interest1 = findViewById(R.id.tr_bi_smart_shield_rate_of_interest1);
		tr_ACI2 = findViewById(R.id.tr_bi_smart_shield_criti_care_13_non_linked_rider2);
		tr_bi_smart_shield_criti_care_13_non_linked_rider = findViewById(R.id.tr_bi_smart_shield_criti_care_13_non_linked_rider);

		tr_ADB_rider = findViewById(R.id.tr_bi_smart_shield_adb_rider);
		tr_ADB_rider2 = findViewById(R.id.tr_bi_smart_shield_adb_rider2);
		tr_ATPDB_Rider = findViewById(R.id.tr_bi_smart_shield_atpd_rider);
		tr_ATPDB_Rider2 = findViewById(R.id.tr_bi_smart_shield_atpd_rider2);

		// Go home Button
		btnBack = findViewById(R.id.btn_bi_smart_shield_btnback);
		// Submit Button
		btnSubmit = findViewById(R.id.btn_bi_smart_shield_btnSubmit);

		edt_proposerdetail_basicdetail_contact_no = findViewById(R.id.edt_smart_shield_contact_no);
		edt_proposerdetail_basicdetail_Email_id = findViewById(R.id.edt_smart_shield_Email_id);
		edt_proposerdetail_basicdetail_ConfirmEmail_id = findViewById(R.id.edt_smart_shield_ConfirmEmail_id);

		// Loan Details
		et_loan_account_no = findViewById(R.id.edt_plandetail_loan_loan_account_no);
		et_financing_institute_name = findViewById(R.id.edt_plandetail_loan_name_of_institude);
		et_loan_category = findViewById(R.id.edt_plandetail_loan_loan_category);
		et_sum_assured_outstanding_loan_amount = findViewById(R.id.edt_plandetail_loan_loan_protection_sum_assured);
		et_balance_loan_tenure = findViewById(R.id.edt_plandetail_loan_loan_tenure);

		btn_plandetail_loan_1st_emi_date = findViewById(R.id.btn_plandetail_loan_1st_emi_date);
		btn_plandetail_loan_last_emi_date = findViewById(R.id.btn_plandetail_loan_last_emi_date);

		ll_loan_protection = findViewById(R.id.ll_loan_protection);

		// Plan
//        if (product_cateogory.equals("INDIVIDUAL")) {
		String[] planList = { "Level Term Assurance",
				"Increasing Term Assurance",
				// "Decreasing Term Assurance[Loan Protection]",
				// "Decreasing Term Assurance[Family Income Protection]"
		};
		ArrayAdapter<String> planAdapter = new ArrayAdapter<>(
				getApplicationContext(), R.layout.spinner_item, planList);
		planAdapter.setDropDownViewResource(R.layout.spinner_item1);
		spnrPlan.setAdapter(planAdapter);
		planAdapter.notifyDataSetChanged();
       /* } else {
            String[] planList = {"Level Term Assurance",
                    "Increasing Term Assurance"};
            ArrayAdapter<String> planAdapter = new ArrayAdapter<String>(
                    getApplicationContext(), R.layout.spinner_item, planList);
            planAdapter.setDropDownViewResource(R.layout.spinner_item1);
            spnrPlan.setAdapter(planAdapter);
            planAdapter.notifyDataSetChanged();
        }
*/
		// Premium Frequency
		String[] premFreqList = { "Yearly", "Half Yearly", "Quarterly",
				"Monthly", "Single" };
		ArrayAdapter<String> premFreqAdapter = new ArrayAdapter<>(
				getApplicationContext(), R.layout.spinner_item, premFreqList);
		premFreqAdapter.setDropDownViewResource(R.layout.spinner_item1);
		spnrPremFrequency.setAdapter(premFreqAdapter);
		premFreqAdapter.notifyDataSetChanged();

		/*
		  Age 18 to 60 Change as per 1,Jan,2014 by Akshaya Mirajkar.
		 */
		// Age
		String[] ageList = new String[43];
		for (int i = 18; i <= 60; i++) {
			ageList[i - 18] = i + "";
		}
		ArrayAdapter<String> ageAdapter = new ArrayAdapter<>(
				getApplicationContext(), R.layout.spinner_item, ageList);
		ageAdapter.setDropDownViewResource(R.layout.spinner_item1);
		spnr_bi_smart_shield_life_assured_age.setAdapter(ageAdapter);
		ageAdapter.notifyDataSetChanged();

		// Gender
        String[] genderList = {"Male", "Female", "Third Gender"};
		ArrayAdapter<String> genderAdapter = new ArrayAdapter<>(getApplicationContext(),
				R.layout.spinner_item, genderList);
		genderAdapter.setDropDownViewResource(R.layout.spinner_item1);
		spnrGender.setAdapter(genderAdapter);
		genderAdapter.notifyDataSetChanged();

		commonMethods.fillSpinnerValue(context, spnr_bi_smart_shield_life_assured_title,
				Arrays.asList(getResources().getStringArray(R.array.arr_select_title)));

		// Policy Term
		String[] policyTermList = new String[58];
		for (int i = 5; i <= 62; i++) {
			policyTermList[i - 5] = i + "";
		}
		ArrayAdapter<String> policyTermAdapter = new ArrayAdapter<>(
				getApplicationContext(), R.layout.spinner_item, policyTermList);
		policyTermAdapter.setDropDownViewResource(R.layout.spinner_item1);
		spnrPolicyTerm.setAdapter(policyTermAdapter);
		policyTermAdapter.notifyDataSetChanged();

		// Loan Rate of interest
		String[] lriList = { "6%", "8%", "10%", "12%", "14%", "16%", "18%",
				"20%" };
		ArrayAdapter<String> lriAdapter = new ArrayAdapter<>(
				getApplicationContext(), R.layout.spinner_item, lriList);
		lriAdapter.setDropDownViewResource(R.layout.spinner_item1);
		spnrLoanRateInterest.setAdapter(lriAdapter);
		lriAdapter.notifyDataSetChanged();



		// acci Term
		String[] accCITermList = new String[58];
		for (int i = 5; i <= 62; i++) {
			accCITermList[i - 5] = i + "";
		}
		ArrayAdapter<String> accCITermAdapter = new ArrayAdapter<>(
				getApplicationContext(), R.layout.spinner_item, accCITermList);
		accCITermAdapter.setDropDownViewResource(R.layout.spinner_item1);
		spnr_bi_smart_shield_criti_care_13_non_linked_rider_term.setAdapter(accCITermAdapter);
		accCITermAdapter.notifyDataSetChanged();

		// adb Term
        String[] adbTermList = new String[53];
        for (int i = 5; i <= 57; i++) {
			adbTermList[i - 5] = i + "";
		}
		ArrayAdapter<String> adbTermAdapter = new ArrayAdapter<>(
				getApplicationContext(), R.layout.spinner_item, adbTermList);
		adbTermAdapter.setDropDownViewResource(R.layout.spinner_item1);
		spnr_AdbTerm.setAdapter(adbTermAdapter);
		adbTermAdapter.notifyDataSetChanged();

		// atpdb Term
        String[] atpdbTermList = new String[53];
        for (int i = 5; i <= 57; i++) {
			atpdbTermList[i - 5] = i + "";
		}
		ArrayAdapter<String> atpdbTermAdapter = new ArrayAdapter<>(
				getApplicationContext(), R.layout.spinner_item, atpdbTermList);
		atpdbTermAdapter.setDropDownViewResource(R.layout.spinner_item1);
		spnr_AtpdbTerm.setAdapter(atpdbTermAdapter);
		atpdbTermAdapter.notifyDataSetChanged();


		currencyFormat = new DecimalFormat("##,##,##,###");
		// setBIInputGui();
		setSpinnerAndOtherListner();

		if (needAnalysis_flag == 1&& !TextUtils.isEmpty(gender)) {
			spnrGender.setSelection(genderAdapter.getPosition(gender));
			onClickLADob(btn_bi_smart_shield_life_assured_date);
		}

		edt_proposerdetail_basicdetail_contact_no
				.addTextChangedListener(new TextWatcher() {

					public void onTextChanged(CharSequence arg0, int arg1,
											  int arg2, int arg3) {

					}

					public void beforeTextChanged(CharSequence arg0, int arg1,
												  int arg2, int arg3) {

					}

					public void afterTextChanged(Editable arg0) {
						String abc = edt_proposerdetail_basicdetail_contact_no
								.getText().toString();
						mobile_validation(abc);

					}
				});

		edt_proposerdetail_basicdetail_Email_id
				.addTextChangedListener(new TextWatcher() {

					public void onTextChanged(CharSequence arg0, int arg1,
											  int arg2, int arg3) {
					}

					public void beforeTextChanged(CharSequence arg0, int arg1,
												  int arg2, int arg3) {
					}

					public void afterTextChanged(Editable arg0) {
						ProposerEmailId = edt_proposerdetail_basicdetail_Email_id
								.getText().toString();
						//email_id_validation(ProposerEmailId);

					}
				});

		edt_proposerdetail_basicdetail_ConfirmEmail_id
				.addTextChangedListener(new TextWatcher() {

					public void onTextChanged(CharSequence arg0, int arg1,
											  int arg2, int arg3) {
					}

					public void beforeTextChanged(CharSequence arg0, int arg1,
												  int arg2, int arg3) {
					}

					public void afterTextChanged(Editable arg0) {
						String proposer_confirm_emailId = edt_proposerdetail_basicdetail_ConfirmEmail_id
								.getText().toString();
						//confirming_email_id(proposer_confirm_emailId);

					}
				});

		edt_bi_smart_shield_life_assured_first_name
				.setOnEditorActionListener(this);
		edt_bi_smart_shield_life_assured_middle_name
				.setOnEditorActionListener(this);
		edt_bi_smart_shield_life_assured_last_name
				.setOnEditorActionListener(this);
		et_bi_smart_shield_criti_care_13_non_linked_rider_sum_assured.setOnEditorActionListener(this);
		edtAdbSA.setOnEditorActionListener(this);
		edtAtpdbSA.setOnEditorActionListener(this);
		edtBasicSA.setOnEditorActionListener(this);

		edt_proposerdetail_basicdetail_contact_no
				.setOnEditorActionListener(this);
		edt_proposerdetail_basicdetail_Email_id.setOnEditorActionListener(this);
		edt_proposerdetail_basicdetail_ConfirmEmail_id
				.setOnEditorActionListener(this);

		// getBasicDetail();
		//
		// String str_usertype = "";
		// TableRow tr_staff_disc;
		//
		// try {
		// Map<String, String> data_logindetail = new HashMap<String, String>();
		// data_logindetail = db.getLoginDetailByAgentId(sr_Code);
		// str_usertype = data_logindetail.get("usertype");
		//
		// } catch (Exception e) {
		// Log.e("BIActivity", e.toString()
		// + "Error  in getting in Login Table");
		// e.printStackTrace();
		// }
		//
		// tr_staff_disc = (TableRow)
		// findViewById(R.id.tr_smart_shield_staff_disc);
		//
		// if (str_usertype.equalsIgnoreCase("BAP")
		// || str_usertype.equalsIgnoreCase("CAG")) {
		// tr_staff_disc.setVisibility(View.GONE);
		// }
		TableRow tr_staff_disc = findViewById(R.id.tr_smart_shield_staff_disc);
		String str_usertype = "";
		try {
			str_usertype = SimpleCrypto.decrypt("SBIL",
					dbHelper.GetUserType());

		} catch (Exception e) {
			e.printStackTrace();
		}
		/*if (str_usertype.equalsIgnoreCase("BAP")
				|| str_usertype.equalsIgnoreCase("CAG")
				|| str_usertype.equalsIgnoreCase("IMF")) {
			tr_staff_disc.setVisibility(View.GONE);
		}*/
	}

	private void loan_details() {

		loanAccountNo = et_loan_account_no.getText().toString();
		financingInstituteName = et_financing_institute_name.getText()
				.toString();
		loanCategory = et_loan_category.getText().toString();
		sumAssuredOutStanding = et_sum_assured_outstanding_loan_amount
				.getText().toString();
		balanceLoanTenure = et_balance_loan_tenure.getText().toString();
		lifeAssured_loan_rate_interest = spnrLoanRateInterest.getSelectedItem()
				.toString();

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		b = 1;
		boolean flagFocus = true;
		edt_bi_smart_shield_life_assured_first_name.requestFocus();

	}

	private void setSpinnerAndOtherListner() {
		// TODO Auto-generated method stub

        /************************* Item Listener starts here ********************************************/

        cb_kerladisc.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cb_kerladisc.isChecked()) {
                    str_kerla_discount = "Yes";
                    clearFocusable(cb_kerladisc);
                    setFocusable(spnr_bi_smart_shield_life_assured_title);
                    spnr_bi_smart_shield_life_assured_title.requestFocus();
                } else {
                    str_kerla_discount = "No";
                    clearFocusable(cb_kerladisc);
                    setFocusable(spnr_bi_smart_shield_life_assured_title);
                    spnr_bi_smart_shield_life_assured_title.requestFocus();
                }
            }
        });

		spnr_bi_smart_shield_life_assured_title
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					public void onItemSelected(AdapterView<?> arg0, View arg1,
											   int position, long id) {
						// TODO Auto-generated method stub
						if (position == 0){
							lifeAssured_Title = "";
						}else {
							lifeAssured_Title = spnr_bi_smart_shield_life_assured_title
									.getSelectedItem().toString();
                           /* if (lifeAssured_Title.equalsIgnoreCase("Mr.")) {
                                gender = "Male";
                                iv_men.setImageDrawable(getResources().getDrawable(
                                        R.drawable.male_selected));
                                iv_women.setImageDrawable(getResources().getDrawable(
                                        R.drawable.female_nonselected));
                            } else if (lifeAssured_Title.equalsIgnoreCase("Ms.")
                                    || lifeAssured_Title.equalsIgnoreCase("Mrs.")) {
                                gender = "Female";
                                iv_women.setImageDrawable(getResources().getDrawable(
                                        R.drawable.female_selected));
                                iv_men.setImageDrawable(getResources().getDrawable(
                                        R.drawable.male_nonselected));
                            }*/
							clearFocusable(spnr_bi_smart_shield_life_assured_title);
							setFocusable(edt_bi_smart_shield_life_assured_first_name);

							edt_bi_smart_shield_life_assured_first_name
									.requestFocus();
						}

					}

					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub

					}
				});
		//
		// spnrLoanRateInterest
		// .setOnItemSelectedListener(new OnItemSelectedListener() {
		//
		// @Override
		// public void onItemSelected(AdapterView<?> arg0, View arg1,
		// int pos, long id) {
		// // TODO Auto-generated method stub
		// {
		// clearFocusable(spnrLoanRateInterest);
		// setFocusable(edtBasicSA);
		// edtBasicSA.requestFocus();
		// }
		//
		// }
		//
		// @Override
		// public void onNothingSelected(AdapterView<?> arg0) {
		// // TODO Auto-generated method stub
		//
		// }
		// });

		spnrLoanRateInterest
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					public void onItemSelected(AdapterView<?> adapter,
											   View arg1, int position, long id) {
						// TODO Auto-generated method stub
						adapter.getItemAtPosition(position).toString();

					}

					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub

					}
				});

		// Age
		spnr_bi_smart_shield_life_assured_age
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					public void onItemSelected(AdapterView<?> arg0, View arg1,
											   int pos, long id) {
						valTerm();
					}

					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub

					}
				});

		spnr_bi_smart_shield_criti_care_13_non_linked_rider_term.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int pos,
									   long id) {
				/*if (valTermRider()) {*/
				clearFocusable(spnr_bi_smart_shield_criti_care_13_non_linked_rider_term);
				setFocusable(et_bi_smart_shield_criti_care_13_non_linked_rider_sum_assured);
				et_bi_smart_shield_criti_care_13_non_linked_rider_sum_assured.requestFocus();
				/*}*/
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});
		// ADB Term
		spnr_AdbTerm.setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> arg0, View arg1, int pos,
									   long id) {
				/*if (valTermRider()) {*/
				clearFocusable(spnr_AdbTerm);
				setFocusable(edtAdbSA);
				edtAdbSA.requestFocus();
				/*}*/
			}

			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});

		// ATPDB Term
		spnr_AtpdbTerm.setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> arg0, View arg1, int pos,
									   long id) {
				/*if (valTermRider()) {*/
				clearFocusable(spnr_AtpdbTerm);
				setFocusable(edtAtpdbSA);
				edtAtpdbSA.requestFocus();
				/*}*/
			}

			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});

		// Plan
		spnrPlan.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> arg0, View arg1, int pos,
									   long id) {
				// TODO Auto-generated method stub
				if (pos == 0 || pos == 1) {

					// premium frequency all options are available
					/*
					 * String[] premFreqList = {"Single" }; ArrayAdapter<String>
					 * premFreqAdapter = new ArrayAdapter<String>(
					 * getApplicationContext(), R.layout.spinner_item,
					 * premFreqList);
					 * spnrPremFrequency.setAdapter(premFreqAdapter);
					 */
					spnrPremFrequency.setEnabled(true);
					/*
					  Change as per 1,Jan,2014 by Akshaya Mirajkar
					 */
					tr_rate_of_interest.setVisibility(View.GONE);
					tr_rate_of_interest1.setVisibility(View.GONE);
					cbADBRider.setChecked(false);
					cbATPDBRider.setChecked(false);
					cb_bi_smart_shield_criti_care_13_non_linked_rider.setChecked(false);
					cbADBRider.setClickable(true);
					cbATPDBRider.setClickable(true);
					cb_bi_smart_shield_criti_care_13_non_linked_rider.setClickable(true);
					tr_bi_smart_shield_criti_care_13_non_linked_rider.setVisibility(View.GONE);
					tr_ACI2.setVisibility(View.GONE);
					tr_ADB_rider.setVisibility(View.GONE);
					tr_ADB_rider2.setVisibility(View.GONE);
					tr_ATPDB_Rider.setVisibility(View.GONE);
					tr_ATPDB_Rider2.setVisibility(View.GONE);
				} else {

					/*
					 * String[] premFreqList = { "Yearly", "Half Yearly",
					 * "Quarterly", "Monthly", "Single" }; ArrayAdapter<String>
					 * premFreqAdapter = new ArrayAdapter<String>(
					 * getApplicationContext(), R.layout.spinner_item,
					 * premFreqList);
					 * spnrPremFrequency.setAdapter(premFreqAdapter);
					 */

					cbADBRider.setChecked(false);
					cbATPDBRider.setChecked(false);
					cb_bi_smart_shield_criti_care_13_non_linked_rider.setChecked(false);
					cbADBRider.setClickable(false);
					cbATPDBRider.setClickable(false);
					cb_bi_smart_shield_criti_care_13_non_linked_rider.setClickable(false);
					tr_bi_smart_shield_criti_care_13_non_linked_rider.setVisibility(View.GONE);
					tr_ACI2.setVisibility(View.GONE);
					tr_ADB_rider.setVisibility(View.GONE);
					tr_ADB_rider2.setVisibility(View.GONE);
					tr_ATPDB_Rider.setVisibility(View.GONE);
					tr_ATPDB_Rider2.setVisibility(View.GONE);
					// tr_ACI.setVisibility(View.GONE)

					// premium fremium frequency mode = Single
					spnrPremFrequency.setEnabled(false);
					spnrPremFrequency.setSelection(4, false);

				}
				if (pos == 2) {

					tr_rate_of_interest.setVisibility(View.VISIBLE);
					tr_rate_of_interest1.setVisibility(View.VISIBLE);
					ll_loan_protection.setVisibility(View.VISIBLE);
					loandetails = true;
				} else

				{
					tr_rate_of_interest.setVisibility(View.GONE);
					tr_rate_of_interest1.setVisibility(View.GONE);
					ll_loan_protection.setVisibility(View.GONE);
					loandetails = false;
				}
				clearFocusable(spnrPlan);
				clearFocusable(spnrPolicyTerm);
				setFocusable(spnrPolicyTerm);
				spnrPolicyTerm.requestFocus();

			}

			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}

		});

		// CC 13 Non linked Rider
		cb_bi_smart_shield_criti_care_13_non_linked_rider.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView,
										 boolean isChecked) {
				if (isChecked) {
					tr_bi_smart_shield_criti_care_13_non_linked_rider.setVisibility(View.VISIBLE);
					clearFocusable(cb_bi_smart_shield_criti_care_13_non_linked_rider);
					tr_ACI2.setVisibility(View.VISIBLE);
					setFocusable(et_bi_smart_shield_criti_care_13_non_linked_rider_sum_assured);
					et_bi_smart_shield_criti_care_13_non_linked_rider_sum_assured.requestFocus();
				} else {

					tr_bi_smart_shield_criti_care_13_non_linked_rider.setVisibility(View.GONE);
					tr_ACI2.setVisibility(View.GONE);
				}
			}
		});




		// ADB Rider
		cbADBRider
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					public void onCheckedChanged(CompoundButton buttonView,
												 boolean isChecked) {
						if (isChecked) {
							tr_ADB_rider.setVisibility(View.VISIBLE);
							tr_ADB_rider2.setVisibility(View.VISIBLE);
							clearFocusable(cbADBRider);
							clearFocusable(spnr_AdbTerm);
							setFocusable(spnr_AdbTerm);
							spnr_AdbTerm.requestFocus();
						} else {

							tr_ADB_rider.setVisibility(View.GONE);
							tr_ADB_rider2.setVisibility(View.GONE);
						}
					}
				});

		// ATPDB Rider
		cbATPDBRider
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					public void onCheckedChanged(CompoundButton buttonView,
												 boolean isChecked) {
						if (isChecked) {
							tr_ATPDB_Rider.setVisibility(View.VISIBLE);
							tr_ATPDB_Rider2.setVisibility(View.VISIBLE);
							clearFocusable(cbATPDBRider);
							clearFocusable(spnr_AtpdbTerm);
							setFocusable(spnr_AtpdbTerm);
							spnr_AtpdbTerm.requestFocus();
						} else {
							tr_ATPDB_Rider.setVisibility(View.GONE);
							tr_ATPDB_Rider2.setVisibility(View.GONE);
						}
					}
				});

		// Staff Discount
		cb_staffdisc.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				if (cb_staffdisc.isChecked()) {
					cb_staffdisc.setChecked(true);
					clearFocusable(rb_smart_shield_proposer_smoker_yes);
					clearFocusable(rb_smart_shield_proposer_smoker_no);
					setFocusable(rb_smart_shield_proposer_smoker_yes);
					rb_smart_shield_proposer_smoker_yes.requestFocus();

				}
			}
		});

		CbJkResident.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (CbJkResident.isChecked()) {
					CbJkResident.setChecked(true);
				} else {
					CbJkResident.setChecked(false);
				}
			}
		});

		spnrPremFrequency
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					public void onItemSelected(AdapterView<?> arg0, View arg1,
											   int pos, long id) {
						int position = spnrPlan.getSelectedItemPosition();
						clearFocusable(spnrPremFrequency);

						if (b == 1) {
							edt_bi_smart_shield_life_assured_first_name
									.requestFocus();
							b = 0;
						} else {
							if (position == 2)

							{
								clearFocusable(spnrLoanRateInterest);
								setFocusable(spnrLoanRateInterest);
								spnrLoanRateInterest.requestFocus();
							} else {
								setFocusable(edtBasicSA);
								edtBasicSA.requestFocus();
							}

						}
					}

					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub

					}
				});

		// Term
		spnrPolicyTerm.setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> arg0, View arg1, int pos,
									   long id) {
				int position = spnrPlan.getSelectedItemPosition();
				if (valTerm() /*&& valTermRider()*/) {
					clearFocusable(spnrPolicyTerm);
					if (flagFirstFocus) {
						setFocusable(rb_smart_shield_proposer_smoker_yes);
						rb_smart_shield_proposer_smoker_yes.requestFocus();
						flagFirstFocus = false;
					} else {
						if (position == 0 || position == 1) {

							clearFocusable(spnrPremFrequency);
							setFocusable(spnrPremFrequency);
							spnrPremFrequency.requestFocus();
						} else {
							clearFocusable(spnrLoanRateInterest);
							setFocusable(spnrLoanRateInterest);
							spnrLoanRateInterest.requestFocus();
						}
					}
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

			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				loan_details();

				inputVal = new StringBuilder();
				retVal = new StringBuilder();
				bussIll = new StringBuilder();

				lifeAssured_First_Name = edt_bi_smart_shield_life_assured_first_name
                        .getText().toString().trim();
				lifeAssured_Middle_Name = edt_bi_smart_shield_life_assured_middle_name
                        .getText().toString().trim();
				lifeAssured_Last_Name = edt_bi_smart_shield_life_assured_last_name
						.getText().toString().trim();

                gender = spnrGender.getSelectedItem().toString();

				name_of_life_assured = lifeAssured_Title + " "
						+ lifeAssured_First_Name + " "
						+ lifeAssured_Middle_Name + " " + lifeAssured_Last_Name;

				mobileNo = edt_proposerdetail_basicdetail_contact_no.getText()
						.toString();
				emailId = edt_proposerdetail_basicdetail_Email_id.getText()
						.toString();
				ConfirmEmailId = edt_proposerdetail_basicdetail_ConfirmEmail_id
						.getText().toString();
				if (loandetails) {

					if (valLoanDetails()) {
						if (valLifeAssuredProposerDetail() && valDob()
								&& valBasicDetail() && valSA() && valTerm()
								&& valTermRider()) {


							System.out.println("Output:" + output);
							addListenerOnSubmit();
							System.out.println("Output:" + output);
							if (valPremiumError && valRiderPremiumError) {
								getInput(smartShieldBean);
								// insertDataIntoDatabase();
								if (needAnalysis_flag == 0) {
									Intent i = new Intent(
											getApplicationContext(),
											success.class);
									i.putExtra(
											"op",
											"Basic Premium is Rs. "
													+ currencyFormat.format(Double.parseDouble(prsObj.parseXmlTag(
													retVal.toString(),
													"basicPremWthOutDisc"))));
									i.putExtra(
											"op1",
											"Installment Premium is Rs. "
													+ currencyFormat.format(Double
													.parseDouble(totalPremWithoutDisc)));
									if (cb_staffdisc.isChecked())
										i.putExtra(
												"op2",
												"Installment Premium with SBG Discount is Rs. "
														+ currencyFormat.format(Double
														.parseDouble(prsObj
																.parseXmlTag(
																		retVal.toString(),
																		"InstmntPrem"))));

									i.putExtra(
											"op3",
											"Installment Premium Inclusive Applicable Taxes is Rs. "
													+ currencyFormat.format(Double.parseDouble(prsObj.parseXmlTag(
													retVal.toString(),
													"InstmntPremWthST"))));

									if (cb_bi_smart_shield_criti_care_13_non_linked_rider.isChecked()) {
										i.putExtra(
												"op4",
												"Accelerated Critical Illness Cover Premium is Rs. "
														+ currencyFormat.format(Double
														.parseDouble(prsObj
																.parseXmlTag(
																		retVal.toString(),
																		"AccCIPrem"))));
									}

									if (cbADBRider.isChecked()) {
										i.putExtra(
												"op5",
												"Accidental Death Benefit Rider Premium is Rs. "
														+ currencyFormat.format(Double
														.parseDouble(prsObj
																.parseXmlTag(
																		retVal.toString(),
																		"ADBPrem"))));
									}

									if (cbATPDBRider.isChecked()) {
										i.putExtra(
												"op6",
												"Accidental Total & Permanent Disability Benefit Rider Premium is Rs. "
														+ currencyFormat.format(Double
														.parseDouble(prsObj
																.parseXmlTag(
																		retVal.toString(),
																		"ATPDPrem"))));
									}

									startActivity(i);
								} else
									Dialog();
							}

						}

					}

				} else {
					if (valLifeAssuredProposerDetail() && valDob()
							&& valBasicDetail() && valCC13Rider() && valSA() && valTerm()
							&& valTermRider()) {

						addListenerOnSubmit();
						if (valPremiumError && valRiderPremiumError) {
							getInput(smartShieldBean);
							// insertDataIntoDatabase();
							Dialog();
						}

					}
				}
			}
		});

		rb_smart_shield_proposer_smoker_yes
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
												 boolean isChecked) {
						// TODO Auto-generated method stub
						if (isChecked) {
							Is_Smoker_or_Not = "Smoker";
							clearFocusable(rb_smart_shield_proposer_smoker_yes);
							clearFocusable(rb_smart_shield_proposer_smoker_no);
							clearFocusable(spnr_bi_smart_shield_life_assured_title);
							setFocusable(spnr_bi_smart_shield_life_assured_title);
							spnr_bi_smart_shield_life_assured_title
									.requestFocus();
						}

					}
				});

		rb_smart_shield_proposer_smoker_no
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
												 boolean isChecked) {
						// TODO Auto-generated method stub
						if (isChecked) {
							Is_Smoker_or_Not = "Non Smoker";
							clearFocusable(rb_smart_shield_proposer_smoker_yes);
							clearFocusable(rb_smart_shield_proposer_smoker_no);
							clearFocusable(spnr_bi_smart_shield_life_assured_title);
							setFocusable(spnr_bi_smart_shield_life_assured_title);
							spnr_bi_smart_shield_life_assured_title
									.requestFocus();

						}

					}
				});

	}

	private boolean valLoanDetails() {
		if (loanAccountNo.equals("") || financingInstituteName.equals("")
				|| loanCategory.equals("") || sumAssuredOutStanding.equals("")
				|| balanceLoanTenure.equals("") || firstEmiDate.equals("")
				|| lastEmiDate.equals("")) {

			showAlert
					.setMessage("Please Fill All Mandatory Details Of Loan Details");
			showAlert.setNeutralButton("OK",
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {
							if (loanAccountNo.equals("")) {
								// apply focusable method
								setFocusable(et_loan_account_no);
								et_loan_account_no.requestFocus();

							} else if (financingInstituteName.equals("")) {
								setFocusable(et_financing_institute_name);
								et_financing_institute_name.requestFocus();

							} else if (loanCategory.equals("")) {
								setFocusable(et_loan_category);
								et_loan_category.requestFocus();

							} else if (sumAssuredOutStanding.equals("")) {
								setFocusable(et_sum_assured_outstanding_loan_amount);
								et_sum_assured_outstanding_loan_amount
										.requestFocus();

							} else if (balanceLoanTenure.equals("")) {
								setFocusable(et_balance_loan_tenure);
								et_balance_loan_tenure.requestFocus();

							} else if (firstEmiDate.equals("")) {
								setFocusable(btn_plandetail_loan_1st_emi_date);
								btn_plandetail_loan_1st_emi_date.requestFocus();
							} else {
								setFocusable(btn_plandetail_loan_last_emi_date);
								btn_plandetail_loan_last_emi_date
										.requestFocus();
							}
						}
					});
			showAlert.show();

			return false;
		}

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
									setFocusable(spnr_bi_smart_shield_life_assured_title);
									spnr_bi_smart_shield_life_assured_title
											.requestFocus();
								} else if (lifeAssured_First_Name.equals("")) {
									setFocusable(edt_bi_smart_shield_life_assured_first_name);
									edt_bi_smart_shield_life_assured_first_name
											.requestFocus();
								} else {
									setFocusable(edt_bi_smart_shield_life_assured_last_name);
									edt_bi_smart_shield_life_assured_last_name
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
                                setFocusable(spnr_bi_smart_shield_life_assured_title);
                                spnr_bi_smart_shield_life_assured_title
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
                                setFocusable(spnr_bi_smart_shield_life_assured_title);
                                spnr_bi_smart_shield_life_assured_title
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
                                setFocusable(spnr_bi_smart_shield_life_assured_title);
                                spnr_bi_smart_shield_life_assured_title
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
                                    setFocusable(spnr_bi_smart_shield_life_assured_title);
                                    spnr_bi_smart_shield_life_assured_title
                                            .requestFocus();
                                } else if (lifeAssured_First_Name.equals("")) {

                                    edt_bi_smart_shield_life_assured_first_name
                                            .requestFocus();
                                } else {
                                    edt_bi_smart_shield_life_assured_last_name
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
                                setFocusable(spnr_bi_smart_shield_life_assured_title);
                                spnr_bi_smart_shield_life_assured_title
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
                                setFocusable(spnr_bi_smart_shield_life_assured_title);
                                spnr_bi_smart_shield_life_assured_title
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
                                setFocusable(spnr_bi_smart_shield_life_assured_title);
                                spnr_bi_smart_shield_life_assured_title
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
								setFocusable(btn_bi_smart_shield_life_assured_date);
								btn_bi_smart_shield_life_assured_date
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

		if (Is_Smoker_or_Not.equals("")) {
			commonMethods.dialogWarning(context, "Please select Smoker/ Non Smoker", true);
			rb_smart_shield_proposer_smoker_yes.requestFocus();
			return false;
		} else if (edt_proposerdetail_basicdetail_contact_no.getText()
				.toString().equals("")) {
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

			commonMethods.dialogWarning(context,"Please Fill Confirm Email Id", true);
			edt_proposerdetail_basicdetail_ConfirmEmail_id.requestFocus();
			return false;
		} else if (!ConfirmEmailId.equals(emailId)) {
			commonMethods.dialogWarning(context,"Email Id Does Not Match", true);
			return false;
		}*/

		else if (!emailId.equals("")) {

			email_id_validation(emailId);
			if (validationFla1) {

				if (ConfirmEmailId.equals("")) {

					commonMethods.dialogWarning(context,"Please Fill Confirm Email Id", true);
					edt_proposerdetail_basicdetail_ConfirmEmail_id.requestFocus();
					return false;
				} else if (!ConfirmEmailId.equals(emailId)) {
					commonMethods.dialogWarning(context,"Email Id Does Not Match", true);
					return false;
				}

				return true;
			} else {
				commonMethods.dialogWarning(context,"Please Fill Valid Email Id", true);
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
				commonMethods.dialogWarning(context, "Please Fill Valid Confirm Email Id", true);
				edt_proposerdetail_basicdetail_ConfirmEmail_id.requestFocus();
				return false;
			}
		}

		else {
			return true;
		}
	}

	private void initialiseDate() {
		Calendar calender = Calendar.getInstance();
		mYear = calender.get(Calendar.YEAR);
		mMonth = calender.get(Calendar.MONTH);
		mDay = calender.get(Calendar.DAY_OF_MONTH);

	}

	private void setDefaultDate(int id) {
		Calendar present_date = Calendar.getInstance();
		present_date.add(Calendar.YEAR, -id);
		mDay = present_date.get(Calendar.DAY_OF_MONTH);
		mMonth = present_date.get(Calendar.MONTH);
		mYear = present_date.get(Calendar.YEAR);
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
						imageButtonSmartShieldProposerPhotograph
								.setImageBitmap(scaled);
					} else {
						photoBitmap = null;
					}
				}
			}
		}
	}

	private void Dialog() {
		// TODO Auto-generated method stub

		final Dialog d = new Dialog(this);
		d.requestWindowFeature(Window.FEATURE_NO_TITLE);
		d.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.BLACK));

		d.setContentView(R.layout.layout_smart_shield_bi_grid);

		TextView tv_proposername = d
				.findViewById(R.id.tv_smart_shield_proposername);
		TextView tv_proposal_number = d
				.findViewById(R.id.tv_smart_shield_proposal_number);

		TextView tv_bi_smart_shield_smoker_or_not = d
				.findViewById(R.id.tv_bi_smart_shield_smoker_or_not);
		TextView tv_bi_is_Staff = d
				.findViewById(R.id.tv_bi_is_Staff);

		TextView tv_bi_is_jk = d.findViewById(R.id.tv_bi_is_jk);
		/*
		 * TextView tv_life_assured_name = (TextView) d
		 * .findViewById(R.id.tv_bi_smart_shiel);
		 */

		TextView tv_life_age_at_entry = d
				.findViewById(R.id.tv_bi_smart_shield_life_assured_age);

		TextView tv_life_assured_gender = d
				.findViewById(R.id.tv_bi_smart_shield_life_assured_gender);

		TextView tv_premPaymentfrequency = d
				.findViewById(R.id.tv_bi_smart_shield_frequency);

		TextView tv_bi_smart_shield_plan = d
				.findViewById(R.id.tv_bi_smart_shield_plan);

		TextView tv_basic_cover_term = d
				.findViewById(R.id.tv_smart_shield_term_basic_cover);

		TextView tv_basic_cover_sum_assured = d
				.findViewById(R.id.tv_smart_shield_sum_assured_basic_cover);

		TextView tv_basic_cover_yearly_premium = d
				.findViewById(R.id.tv_smart_shield_premium_basic_cover);

		TextView tv_aci_term = d
				.findViewById(R.id.tv_smart_shield_term_aci);

		TextView tv_aci_sum_assured = d
				.findViewById(R.id.tv_smart_shield_sum_assured_aci);

		TextView tv_aci_yearly_premium = d
				.findViewById(R.id.tv_smart_shield_premium_aci);

		TextView tv_adb_term = d
				.findViewById(R.id.tv_smart_shield_term_adb_rider);

		TextView tv_adb_sum_assured = d
				.findViewById(R.id.tv_smart_shield_sum_assured_adb_rider);

		TextView tv_adb_yearly_premium = d
				.findViewById(R.id.tv_smart_shield_premium_adb_rider);

		TextView tv_atpdb_term = d
				.findViewById(R.id.tv_smart_shield_term_atpdb_rider);

		TextView tv_atpdb_sum_assured = d
				.findViewById(R.id.tv_smart_shield_sum_assured_atpdb_rider);

		TextView tv_atpdb_yearly_premium = d
				.findViewById(R.id.tv_smart_shield_premium_atpdb_rider);

		// changes done by amit
		TextView tv_bi_smart_shield_frequency_premium = d
				.findViewById(R.id.tv_for_frequency_premium);

		TextView tv_bi_smart_shield_frequency_premium_for_service_tax = d
				.findViewById(R.id.tv_for_frequency_premium_for_service_Tax);

		TextView tv_bi_smart_shield_installment_premium = d
				.findViewById(R.id.tv_bi_smart_shield_installment_premium);

		TextView tv_bi_smart_shield_installment_premium_with_discount = d
				.findViewById(R.id.tv_bi_smart_shield_installment_premium_with_discount);

		TextView tv_for_frequency_premium_with_discount = d
				.findViewById(R.id.tv_for_frequency_premium_with_discount);

		TextView tv_bi_smart_shield_installment_premium_with_tax = d
				.findViewById(R.id.tv_bi_smart_shield_installment_premium_with_tax);

		TextView tv_bi_smart_shield_loan_account_no = d
				.findViewById(R.id.tv_bi_smart_shield_loan_account_no);

		TextView tv_bi_smart_shield_loan_financing_institute_name = d
				.findViewById(R.id.tv_bi_smart_shield_loan_financing_institute_name);

		TextView tv_bi_smart_shield_loan_category = d
				.findViewById(R.id.tv_bi_smart_shield_loan_category);

		TextView tv_bi_smart_shield_loan_outstanding_loan_amount = d
				.findViewById(R.id.tv_bi_smart_shield_loan_outstanding_loan_amount);

		TextView tv_bi_smart_shield_loan_balance_tenure = d
				.findViewById(R.id.tv_bi_smart_shield_loan_balance_tenure);

		TextView tv_bi_smart_shield_loan_rate_of_interest = d
				.findViewById(R.id.tv_bi_smart_shield_loan_rate_of_interest);

		TextView tv_bi_smart_shield_loan_date_of_first_emi = d
				.findViewById(R.id.tv_bi_smart_shield_loan_date_of_first_emi);

		TextView tv_bi_smart_shield_loan_date_of_last_emi = d
				.findViewById(R.id.tv_bi_smart_shield_loan_date_of_last_emi);

		LinearLayout ll_bi_smart_shield_life_assured_loan_details = d
				.findViewById(R.id.ll_bi_smart_shield_life_assured_loan_details);

		LinearLayout ll_smart_shield_acc_cover = d
				.findViewById(R.id.ll_smart_shield_acc_cover);
		LinearLayout ll_smart_shield_adb_rider = d
				.findViewById(R.id.ll_smart_shield_adb_rider);
		LinearLayout ll_smart_shield_atpdb_rider = d
				.findViewById(R.id.ll_smart_shield_atpdb_rider);

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
		TableRow tr_for_frequency_premium_with_discount = d
				.findViewById(R.id.tr_for_frequency_premium_with_discount);


		Button btn_proceed = d.findViewById(R.id.btn_proceed);

		btn_MarketingOfficalDate = d
				.findViewById(R.id.btn_MarketingOfficalDate);
		btn_PolicyholderDate = d
				.findViewById(R.id.btn_PolicyholderDate);

		Ibtn_signatureofMarketing = d
				.findViewById(R.id.Ibtn_signatureofMarketing);
		Ibtn_signatureofPolicyHolders = d
				.findViewById(R.id.Ibtn_signatureofPolicyHolders);

		String flg_needAnalyis = "0";
		if (NeedAnalysisActivity.str_need_analysis != null) {
			if (NeedAnalysisActivity.str_need_analysis.equals("1")) {


				flg_needAnalyis = "1";
				tr_need_analysis.setVisibility(View.VISIBLE);
			} else {
				cb_statement_need_analysis.setChecked(true);
				tr_need_analysis.setVisibility(View.GONE);
			}

		}
		// getValueFromDatabase();
		if (!proposer_sign.equals("")) {
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
							+ " having received the information with respect to the above, have understood the above statement before entering into the contract.");
			edt_proposer_name_need_analysis
					.setText("I, "
							+ name_of_life_assured
							+ " have undergone the Need Analysis  after having reviewed the SBI Life Product options, I have opted for SBI LIFE - Smart Shield.");

			// tv_life_assured_name.setText(name_of_life_assured);
			tv_proposername.setText(name_of_life_assured);
		} else if (proposer_Is_Same_As_Life_Assured.equalsIgnoreCase("n")) {
			edt_proposer_name
					.setText("I, "
							+ name_of_proposer
							+ " having received the information with respect to the above, have understood the above statement before entering into the contract.");
			edt_proposer_name_need_analysis
					.setText("I, "
							+ name_of_proposer
							+ " have undergone the Need Analysis  after having reviewed the SBI Life Product options, I have opted for SBI LIFE - Smart Shield.");

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

		imageButtonSmartShieldProposerPhotograph = d
				.findViewById(R.id.imageButtonSmartShieldProposerPhotograph);
		imageButtonSmartShieldProposerPhotograph
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
			imageButtonSmartShieldProposerPhotograph
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
							imageButtonSmartShieldProposerPhotograph
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

					// String isActive = "0";
					String productCode = "";

					switch (smartShieldBean.getPlanName()) {
						case "Level Term Assurance":
							productCode = "SHIELD2A";
							break;
						case "Increasing Term Assurance":
							productCode = "SHIELD2D";
							break;
						case "Decreasing Term Assurance[Loan Protection]":
							productCode = "SHIELD2B";
							break;
						case "Decreasing Term Assurance[Family Income Protection]":
							productCode = "SHIELD2C";
							break;
					}
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
									.valueOf((sumAssured.equals("") || sumAssured == null) ? "0"
											: sumAssured))),
							obj.getRound(totalPremiumYearlyInstallmentPremWithServiceTaxYearly),
							emailId, mobileNo, agentEmail, agentMobile,
							na_input, na_output, premPayingMode, Integer
							.parseInt(policyTerm), 0, productCode,
							getDate(lifeAssured_date_of_birth), "", inputVal
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
							obj.getRound(obj.getStringWithout_E(Double
									.valueOf((sumAssured.equals("") || sumAssured == null) ? "0"
											: sumAssured))),
							obj.getRound(totalPremiumYearlyInstallmentPremWithServiceTaxYearly),
							agentEmail, agentMobile, na_input, na_output,
							premPayingMode, Integer.parseInt(policyTerm), 0,
							productCode, getDate(lifeAssured_date_of_birth),
							"", "",mode,inputVal
							.toString(), retVal.toString().replace(
							bussIll.toString(), "")));

					createPdf();


					NABIObj.serviceHit(BI_SmartShieldActivity.this,
							na_cbi_bean, newFile, needAnalysispath.getPath(),
							mypath.getPath(), name_of_person, QuatationNumber,mode);
					d.dismiss();

					// dialog("Please Fill All The Detail", true);
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
						commonMethods.dialogWarning(context, "Please Tick on I Agree Clause ", true);
						setFocusable(cb_statement);
						cb_statement.requestFocus();
					} else if (!radioButtonTrasactionModeParivartan.isChecked()
							&& !radioButtonTrasactionModeManual.isChecked()) {
						commonMethods.dialogWarning(context,"Please Select Transaction Mode", true);
						setFocusable(linearlayoutTrasactionModeParivartan);
						linearlayoutTrasactionModeParivartan.requestFocus();
					} else if (photoBitmap == null) {
						commonMethods.dialogWarning(context, "Please Capture the Photo", true);
						setFocusable(imageButtonSmartShieldProposerPhotograph);
						imageButtonSmartShieldProposerPhotograph.requestFocus();
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

		ageAtEntry = prsObj.parseXmlTag(input, "age");
		tv_life_age_at_entry.setText(ageAtEntry + " Years");

		gender = prsObj.parseXmlTag(input, "gender");
		tv_life_assured_gender.setText(gender);

		premPayingMode = prsObj.parseXmlTag(input, "premFreq");
		tv_premPaymentfrequency.setText(premPayingMode);

		if (premPayingMode.equalsIgnoreCase("Half Yearly")) {

		} else if (premPayingMode.equalsIgnoreCase("Quarterly")) {

		} else if (premPayingMode.equalsIgnoreCase("Monthly")) {

		}

		isJkResident = prsObj.parseXmlTag(input, "isJKResident");
		aciRiderStatus = prsObj.parseXmlTag(input, "isAccCIRider");
		adbRiderStatus = prsObj.parseXmlTag(input, "isADBRider");
		atpdbRiderStatus = prsObj.parseXmlTag(input, "isATPDBRider");

		// staffdiscount = prsObj.parseXmlTag(input, "isStaff");

		String staffdiscount = prsObj.parseXmlTag(input, "isStaff");

		if (staffdiscount.equalsIgnoreCase("true")) {
			// tr_staff_per.setVisibility(View.VISIBLE);
			tv_bi_is_Staff.setText("Yes");
			// tv_bi_flexi_smart_plus_staff_per.setText(staffdiscount_per);
		} else {
			// .setVisibility(View.GONE);
			tv_bi_is_Staff.setText("No");

		}

		isJkResident = prsObj.parseXmlTag(input, "isJKResident");

		if (isJkResident.equalsIgnoreCase("true")) {
			tv_bi_is_jk.setText("Yes");
		} else {
			tv_bi_is_jk.setText("No");
		}

		/*
		 * if (staffdiscount.equals("true")) { tv_staff_or_not.setText("Staff");
		 * } else { tv_staff_or_not.setText("Non - Staff"); }
		 */
		smoker_or_not = prsObj.parseXmlTag(input, "isSmoker");

		if (smoker_or_not.equalsIgnoreCase("true")) {
			tv_bi_smart_shield_smoker_or_not.setText("Smoker");
		} else {
			tv_bi_smart_shield_smoker_or_not.setText("Non Smoker");
		}

		policyTerm = prsObj.parseXmlTag(input, "policyTerm");
		tv_basic_cover_term.setText(policyTerm + " Years");

		sumAssured = prsObj.parseXmlTag(input, "sumAssured");

		/*basicCoverYearlyPremium = Double.parseDouble(prsObj.parseXmlTag(output,
				"basicPremWthOutDisc")) + "";*/

		basicCoverYearlyPremium = getformatedThousandString(Integer.parseInt(obj
				.getRound(obj.getStringWithout_E(Double
						.valueOf(((prsObj.parseXmlTag(output,
								"basicPremWthOutDisc") == null) || (prsObj
								.parseXmlTag(output, "basicPremWthOutDisc")
								.equals(""))) ? "0" : prsObj
								.parseXmlTag(output, "basicPremWthOutDisc"))))))
				+ "";


		tv_basic_cover_yearly_premium.setText("" + basicCoverYearlyPremium);

		basicCoverSumAssured = (int) Double.parseDouble(sumAssured) + "";
		tv_basic_cover_sum_assured.setText(""
				+ getformatedThousandString(Integer
				.parseInt(basicCoverSumAssured)));

		// Changes done by amit
		tv_bi_smart_shield_frequency_premium.setText((spnrPremFrequency
				.getSelectedItem().toString() + " Installment Premium"));
		tv_bi_smart_shield_frequency_premium_for_service_tax
				.setText((spnrPremFrequency.getSelectedItem().toString() + " Installment Premium with Applicable Taxes"));

		String totalPremiumYearlyInstallmentPremYearlyWithoutDsc = ((int) Double
				.parseDouble(prsObj.parseXmlTag(output, "InstmntPremWithoutDisc"))) + "";
		tv_bi_smart_shield_installment_premium.setText("" + getformatedThousandString(Integer.parseInt(totalPremiumYearlyInstallmentPremYearlyWithoutDsc)));

		if (cb_staffdisc.isChecked()) {
			tr_for_frequency_premium_with_discount.setVisibility(View.VISIBLE);
		} else {
			tr_for_frequency_premium_with_discount.setVisibility(View.GONE);
		}

		tv_for_frequency_premium_with_discount.setText((spnrPremFrequency
				.getSelectedItem().toString() + " Installment Premium with Staff Discount"));
		String totalPremiumYearlyInstallmentPremYearly = ((int) Double
				.parseDouble(prsObj.parseXmlTag(output, "InstmntPrem"))) + "";
		tv_bi_smart_shield_installment_premium_with_discount
				.setText("" + getformatedThousandString(Integer.parseInt(totalPremiumYearlyInstallmentPremYearly)));

		totalPremiumYearlyInstallmentPremWithServiceTaxYearly = ((int) Double
				.parseDouble(prsObj.parseXmlTag(output, "InstmntPremWthST")))
				+ "";
        tv_bi_smart_shield_installment_premium_with_tax
                .setText("" + getformatedThousandString(Integer.parseInt(totalPremiumYearlyInstallmentPremWithServiceTaxYearly)));

		TextView tv_bi_smart_shield_total_service_tax_first_year = d
				.findViewById(R.id.tv_bi_smart_shield_total_service_tax_first_year);

		// servcTax = prsObj.parseXmlTag(output, "servcTax");
		/*
		 * double basicServiceTaxDouble = Double.parseDouble(prsObj.parseXmlTag(
		 * output, "basicServiceTax"));
		 */
		double basicServiceTaxDouble = Double.parseDouble(prsObj.parseXmlTag(
				output, "servcTax"));
		basicServiceTax = prsObj.parseXmlTag(output, "servcTax");

		tv_bi_smart_shield_total_service_tax_first_year
				.setText("Rs. "
                        + getformatedThousandString(Integer.parseInt(obj.getRound(obj
                        .getStringWithout_E(basicServiceTaxDouble)))));

		double totalPremiumWithAllTaxes = Double
				.valueOf((totalPremiumYearlyInstallmentPremWithServiceTaxYearly == null || totalPremiumYearlyInstallmentPremWithServiceTaxYearly
						.equals("")) ? "0"
						: totalPremiumYearlyInstallmentPremWithServiceTaxYearly);

		tv_bi_smart_shield_installment_premium_with_tax
				.setText("Rs. "
						+ obj.getRound(obj
						.getStringWithout_E(totalPremiumWithAllTaxes)));

		// Amit changes end- 23-5-2016

		planProposedName = prsObj.parseXmlTag(input, "plan");
		tv_bi_smart_shield_plan.setText(planProposedName);
		// tv_basic_cover_plan.setText(planProposedName);
		if (planProposedName.equalsIgnoreCase("Level Term Assurance")) {
			ll_bi_smart_shield_life_assured_loan_details
					.setVisibility(View.GONE);

		} else if (planProposedName
				.equalsIgnoreCase("Increasing Term Assurance")) {
			ll_bi_smart_shield_life_assured_loan_details
					.setVisibility(View.GONE);
		} else if (planProposedName
				.equalsIgnoreCase("Decreasing Term Assurance[Loan Protection]")) {
			ll_bi_smart_shield_life_assured_loan_details
					.setVisibility(View.VISIBLE);

			tv_bi_smart_shield_loan_account_no.setText(loanAccountNo);
			tv_bi_smart_shield_loan_financing_institute_name
					.setText(financingInstituteName);
			tv_bi_smart_shield_loan_category.setText(loanCategory);
			tv_bi_smart_shield_loan_outstanding_loan_amount
					.setText(sumAssuredOutStanding);
			tv_bi_smart_shield_loan_balance_tenure.setText(balanceLoanTenure);
			tv_bi_smart_shield_loan_rate_of_interest
					.setText(lifeAssured_loan_rate_interest);
			tv_bi_smart_shield_loan_date_of_first_emi.setText(firstEmiDate);
			tv_bi_smart_shield_loan_date_of_last_emi.setText(lastEmiDate);

		} else if (planProposedName
				.equalsIgnoreCase("Decreasing Term Assurance[Family Income Protection]")) {
			ll_bi_smart_shield_life_assured_loan_details
					.setVisibility(View.GONE);
		}

		tv_basic_cover_term.setText(policyTerm);

		TextView tv_Company_policy_surrender_dec = d
				.findViewById(R.id.tv_Company_policy_surrender_dec);

		String str_payingMode = "";
		if (premPayingMode.equalsIgnoreCase("Single")) {
			str_payingMode = "Single";

            Company_policy_surrender_dec = "Your SBI Life - Smart Shield (UIN: 111N067V07) is a "
					+ str_payingMode
					+ " premium policy and you are required to pay One Time Premium of Rs."
					+ totalPremiumYearlyInstallmentPremWithServiceTaxYearly
					+ " .Your Policy Term is "
					+ policyTerm
					+ " years"
					+ " and Basic Sum Assured is Rs. " + getformatedThousandString(Integer
					.parseInt(basicCoverSumAssured));

		} else {
			str_payingMode = "Regular";

            Company_policy_surrender_dec = "Your  SBI Life - Smart Shield (UIN: 111N067V07) is a "
					+ str_payingMode
                    + " premuim policy for which your first year "
					+ premPayingMode
                    + " premium is Rs."
					+ totalPremiumYearlyInstallmentPremWithServiceTaxYearly
					+ " .Your Policy Term is "
					+ policyTerm
                    + " years"
					+ "  Premium Payment Term is "
					+ policyTerm
					+ " years"
					+ " and Basic Sum Assured is Rs. " + getformatedThousandString(Integer
					.parseInt(basicCoverSumAssured));

		}


		tv_Company_policy_surrender_dec.setText(Company_policy_surrender_dec);

		// tv_basic_cover_sum_assured.setText(sumAssured);
		// tv_basic_cover_yearly_premium.setText(ptRiderYearly);

		if (aciRiderStatus.equalsIgnoreCase("true")) {
			ll_smart_shield_acc_cover.setVisibility(View.VISIBLE);
			ptRiderYearly = obj.getRound(Double.parseDouble(prsObj.parseXmlTag(output,
					"AccCIPrem")) + "");
			aciTerm = prsObj.parseXmlTag(input, "AccCITerm");
			aciSA = ((int) Double.parseDouble(prsObj.parseXmlTag(input,
					"AccCISA"))) + "";
			tv_aci_term.setText(aciTerm);
			tv_aci_sum_assured.setText("" + getformatedThousandString(Integer
					.parseInt(aciSA)));
           /* tv_aci_yearly_premium.setText("" + getformatedThousandString(Integer
                    .parseInt(ptRiderYearly)));*/
            tv_aci_yearly_premium.setText("" + prsObj.parseXmlTag(output,
                    "AccCIPrem"));
		}

		if (adbRiderStatus.equalsIgnoreCase("true")) {
			ll_smart_shield_adb_rider.setVisibility(View.VISIBLE);
			adbRiderYearly = obj
					.getRound(Double.parseDouble(prsObj.parseXmlTag(output,
							"ADBPrem")) + "");
			adbRiderTerm = prsObj.parseXmlTag(input, "adbTerm");
			adbSA = ((int) Double.parseDouble(prsObj
					.parseXmlTag(input, "adbSA"))) + "";
			tv_adb_term.setText(adbRiderTerm);
			tv_adb_sum_assured.setText(""
					+ getformatedThousandString(Integer
					.parseInt(adbSA)));
            /*    tv_adb_yearly_premium.setText(""
					+ getformatedThousandString(Integer
                    .parseInt(adbRiderYearly)));*/

            tv_adb_yearly_premium.setText(""
                    + prsObj.parseXmlTag(output,
                    "ADBPrem"));

		}

		if (atpdbRiderStatus.equalsIgnoreCase("true")) {
			ll_smart_shield_atpdb_rider.setVisibility(View.VISIBLE);
			atpdbRiderYearly = obj
					.getRound(Double.parseDouble(prsObj.parseXmlTag(output,
							"ATPDPrem")) + "");
			atpdbRiderTerm = prsObj.parseXmlTag(input, "atpdbTerm");
			atpdbSA = ((int) Double.parseDouble(prsObj.parseXmlTag(input,
					"atpdbSA"))) + "";

			tv_atpdb_term.setText(atpdbRiderTerm);
			tv_atpdb_sum_assured.setText(""
					+ getformatedThousandString(Integer
					.parseInt(atpdbSA)));
             /* tv_atpdb_yearly_premium.setText(""
					+ getformatedThousandString(Integer
                    .parseInt(atpdbRiderYearly)));*/
            tv_atpdb_yearly_premium.setText(prsObj.parseXmlTag(output,
                    "ATPDPrem"));

		}
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

	private void getInput(SmartShieldBean smartShieldBean) {
		inputVal = new StringBuilder();

		String LifeAssured_title = spnr_bi_smart_shield_life_assured_title
				.getSelectedItem().toString();
		String LifeAssured_firstName = edt_bi_smart_shield_life_assured_first_name
				.getText().toString();
		String LifeAssured_middleName = edt_bi_smart_shield_life_assured_middle_name
				.getText().toString();
		String LifeAssured_lastName = edt_bi_smart_shield_life_assured_last_name
				.getText().toString();
		String LifeAssured_DOB = btn_bi_smart_shield_life_assured_date
				.getText().toString();
		String LifeAssured_age = spnr_bi_smart_shield_life_assured_age
				.getSelectedItem().toString();

		String proposer_title = "";
		String proposer_firstName = "";
		String proposer_middleName = "";
		String proposer_lastName = "";
		String proposer_DOB = "";
		String proposer_age = "";
		String proposer_gender = "";

		int age = smartShieldBean.getAge();
		String planName = smartShieldBean.getPlanName();
		String gender = smartShieldBean.getGender();
		int basicPolicyTerm = smartShieldBean.getBasicTerm();
		int aciTerm = smartShieldBean.getAccCI_Term();
		int adbTerm = smartShieldBean.getADB_Term();
		int atpdbTerm = smartShieldBean.getATPDB_Term();

		double basicSumAssured = smartShieldBean.getBasicSA();
		double aciSumAssured = smartShieldBean.getAccCI_SA();
		double adbSumAssured = smartShieldBean.getADB_SA();
		double atpdbSumAssured = smartShieldBean.getATPDB_SA();
		String PremPayingMode = smartShieldBean.getPremFreq();

		boolean isJKresident = smartShieldBean.isJKResident();
		boolean isStaffOrNot = smartShieldBean.getStaffDisc();
		boolean statusACI = smartShieldBean.getAccCI_Status();
		boolean statusADB = smartShieldBean.getADB_Status();
		boolean statusATPDB = smartShieldBean.getATPDB_Status();
		boolean smokerOrNot = false;

		String loanAccNo = smartShieldBean.getLoanAccountNumber();
		String loanFinanceInst = smartShieldBean.getLoanFinancialInstitue();
		String loanCatgy = smartShieldBean.getLoanCategory();
		String loanSumAssOutStanding = smartShieldBean
				.getLoanSumAssuredOutstanding();
		String loanBalTenure = smartShieldBean.getLoanBalanceLoanTenure();
		String loanFstEmiDate = smartShieldBean.getLoanFirstEmiDate();
		String loanLstEmiDate = smartShieldBean.getLoanLastEmiDate();
		String loanIntRate = smartShieldBean.getLRI();

        if (Is_Smoker_or_Not.equalsIgnoreCase("Smoker")) {
            smokerOrNot = true;
        } else {
            smokerOrNot = false;
        }

		inputVal.append("<?xml version='1.0' encoding='utf-8' ?><smartshield>");

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

		inputVal.append("<product_name>").append(product_name).append("</product_name>");
		inputVal.append("<product_Code>").append(product_Code).append("</product_Code>");
		inputVal.append("<product_UIN>").append(product_UIN).append("</product_UIN>");
		inputVal.append("<product_cateogory>").append(product_cateogory).append("</product_cateogory>");
		inputVal.append("<product_type>").append(product_type).append("</product_type>");

		inputVal.append("<proposer_Is_Same_As_Life_Assured>").append(proposer_Is_Same_As_Life_Assured).append("</proposer_Is_Same_As_Life_Assured>");

		inputVal.append("<isJKResident>").append(isJKresident).append("</isJKResident>");
		inputVal.append("<isStaff>").append(isStaffOrNot).append("</isStaff>");
        inputVal.append("<str_kerla_discount>" + str_kerla_discount + "</str_kerla_discount>");
		inputVal.append("<isSmoker>").append(smokerOrNot).append("</isSmoker>");
		inputVal.append("<age>").append(age).append("</age>");
		inputVal.append("<plan>").append(planName).append("</plan>");

		inputVal.append("<policyTerm>").append(basicPolicyTerm).append("</policyTerm>");

		inputVal.append("<isAccCIRider>").append(statusACI).append("</isAccCIRider>");
		inputVal.append("<isADBRider>").append(statusADB).append("</isADBRider>");
		inputVal.append("<isATPDBRider>").append(statusATPDB).append("</isATPDBRider>");

		inputVal.append("<AccCITerm>").append(aciTerm).append("</AccCITerm>");
		inputVal.append("<adbTerm>").append(adbTerm).append("</adbTerm>");
		inputVal.append("<atpdbTerm>").append(atpdbTerm).append("</atpdbTerm>");

		inputVal.append("<premFreq>").append(PremPayingMode).append("</premFreq>");
		inputVal.append("<sumAssured>").append(basicSumAssured).append("</sumAssured>");
		inputVal.append("<AccCISA>").append(aciSumAssured).append("</AccCISA>");
		inputVal.append("<adbSA>").append(adbSumAssured).append("</adbSA>");
		inputVal.append("<atpdbSA>").append(atpdbSumAssured).append("</atpdbSA>");
		inputVal.append("<nbd_loan_INT>").append(loanIntRate).append("</nbd_loan_INT>");
		inputVal.append("<nbd_loanAccNo>").append(loanAccNo).append("</nbd_loanAccNo>");
		inputVal.append("<nbd_loanFinanceInst>").append(loanFinanceInst).append("</nbd_loanFinanceInst>");
		inputVal.append("<nbd_loanCatgy>").append(loanCatgy).append("</nbd_loanCatgy>");
		inputVal.append("<nbd_loanSumAssOutStanding>").append(loanSumAssOutStanding).append("</nbd_loanSumAssOutStanding>");
		inputVal.append("<nbd_loanBalTenure>").append(loanBalTenure).append("</nbd_loanBalTenure>");
		inputVal.append("<nbd_loanFstEmiDate>").append(loanFstEmiDate).append("</nbd_loanFstEmiDate>");
		inputVal.append("<nbd_loanLstEmiDate>").append(loanLstEmiDate).append("</nbd_loanLstEmiDate>");


		//Added Tushar Kadam Kerla Applicable Tax 7-Jun-2019
		String str_kerla_discount = "N";
		if (cb_kerladisc.isChecked()) {
			str_kerla_discount = "Y";
		}

		inputVal.append("<KFC>" + str_kerla_discount + "</KFC>");
		//End Added Tushar Kadam Kerla Applicable Tax 7-Jun-2019

		inputVal.append("</smartshield>");

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

	/** Used To Change date From mm-dd-yyyy to dd-mm-yyyy */
	private String getDate(String OldDate) {
		String NewDate = "";
		try {
			DateFormat userDateFormat = new SimpleDateFormat("MM-dd-yyyy",Locale.ENGLISH);
			DateFormat dateFormatNeeded = new SimpleDateFormat("dd/MM/yyyy",Locale.ENGLISH);
			Date date = userDateFormat.parse(OldDate);

			NewDate = dateFormatNeeded.format(date);

		} catch (Exception e) {
			// TODO: handle exception
			Log.e("Integrated Service", "Error in Getting Date");
		}

		return NewDate;
	}

	/** Used To Change date From dd-mm-yyyy to mm-dd-yyyy */
	private String getDate1(String OldDate) {
		String NewDate = "";
		try {
			DateFormat userDateFormat = new SimpleDateFormat("dd-MM-yyyy",Locale.ENGLISH);
			DateFormat dateFormatNeeded = new SimpleDateFormat("MM-dd-yyyy",Locale.ENGLISH);
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

		d = new Dialog(BI_SmartShieldActivity.this);
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
				Intent intent = new Intent(BI_SmartShieldActivity.this,
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

	public void emiDate1(View v) {
		initialiseDateParameter(firstEmiDate, 0);
		DIALOG_ID = 7;
		showDialog(DATE_DIALOG_ID);
	}

	public void emiDate2(View v) {
		initialiseDateParameter(lastEmiDate, 0);
		DIALOG_ID = 8;
		showDialog(DATE_DIALOG_ID);
	}

	public void onClickDob(View v) {
		// setDefaultDate();
		DIALOG_ID = 4;
		showDialog(DATE_DIALOG_ID);
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
			// if (view.isShown()) {
			mYear = year;
			mMonth = monthOfYear;
			mDay = dayOfMonth;
			updateDisplay(DIALOG_ID);
			// }
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

		String proposer_Backdating_BackDate = "";
		String proposer_Backdating_WishToBackDate_Policy = "";
		if (proposer_Backdating_WishToBackDate_Policy.equalsIgnoreCase("y")
				&& !proposer_Backdating_BackDate.equals("")) {

			int Proposerage = calculateMyAge(mYear, Integer.parseInt(mont),
					Integer.parseInt(day));

			String final_age = Integer.toString(Proposerage);
			spnr_bi_smart_shield_life_assured_age.setSelection(
					getIndex(spnr_bi_smart_shield_life_assured_age, final_age),
					false);

		}

		String final_age = Integer.toString(age);

		if ((id != 7) && (id != 8) && final_age.contains("-")) {
			commonMethods.dialogWarning(context,"Please fill Valid Birth Date", false);
		}

		else {
			switch (id) {

				case 2:
					btn_PolicyholderDate.setText(date);
					break;
				case 3:
					btn_MarketingOfficalDate.setText(date);
					break;

				case 5:

					final_age = Integer.toString(age);
                    lifeAssuredAge = Integer.toString(age);
					if (final_age.contains("-")) {
						commonMethods.BICommonDialog(context, "Please fill Valid Bith Date");
					} else {

						if (18 <= age && age <= 60) {

							btn_bi_smart_shield_life_assured_date.setText(date);

							spnr_bi_smart_shield_life_assured_age.setSelection(
									getIndex(spnr_bi_smart_shield_life_assured_age,
											final_age), false);
							lifeAssured_date_of_birth = getDate1(date + "");
							clearFocusable(btn_bi_smart_shield_life_assured_date);
							setFocusable(edt_proposerdetail_basicdetail_contact_no);
							edt_proposerdetail_basicdetail_contact_no
									.requestFocus();

							/*
							 * setFocusable(spnrPlan); spnrPlan.requestFocus();
							 */

							// setFocusable(edt_premiumAmt);
							// edt_premiumAmt.requestFocus();
						} else {
							// dialog("Minimum Age should be 18"
							// + "yrs and Maximum Age should be 60"
							// + " yrs For LifeAssured");

							commonMethods.BICommonDialog(context, "The Age of Life Assured should be between 18 & 60 yrs");
							btn_bi_smart_shield_life_assured_date
									.setText("Select Date");
							lifeAssured_date_of_birth = "";
							clearFocusable(btn_bi_smart_shield_life_assured_date);
							setFocusable(btn_bi_smart_shield_life_assured_date);
							btn_bi_smart_shield_life_assured_date.requestFocus();
						}
					}
					break;

				case 6:

					/*
					 * if (age >= 0) { proposer_Backdating_BackDate = date + "";
					 * btn_proposerdetail_personaldetail_backdatingdate
					 * .setText(proposer_Backdating_BackDate);
					 * clearFocusable(btn_proposerdetail_personaldetail_backdatingdate
					 * );
					 *
					 * setFocusable(cbPTARider); cbPTARider.requestFocus();
					 *
					 * } else { dialog("Please Select Valid BackDating Date", true);
					 * btn_proposerdetail_personaldetail_backdatingdate
					 * .setText("Select Date"); proposer_Backdating_BackDate = ""; }
					 */

					// break;

				case 7:
					final_age = Integer.toString(age);
					emiage_1st = age;
					if (final_age.contains("-")) {
						commonMethods.BICommonDialog(context, "Future date cannot be entered for 1st Emi date");
					} else {

						if (0 <= age) {

							btn_plandetail_loan_1st_emi_date.setText(date);

							firstEmiDate = getDate1(date + "");
							clearFocusable(btn_plandetail_loan_1st_emi_date);
							setFocusable(btn_plandetail_loan_last_emi_date);
							btn_plandetail_loan_last_emi_date.requestFocus();

						} else {
							commonMethods.BICommonDialog(context, "Future date cannot be entered for 1st Emi date");
							btn_plandetail_loan_1st_emi_date.setText("Select Date");
							firstEmiDate = "";
							clearFocusable(btn_plandetail_loan_1st_emi_date);
							setFocusable(btn_plandetail_loan_1st_emi_date);
							btn_plandetail_loan_1st_emi_date.requestFocus();
						}

					}

					break;

				case 8:


					if (!(firstEmiDate.equals(""))) {
						lastEmiDate = getDate1(date + "");
						if (age < emiage_1st) {
							btn_plandetail_loan_last_emi_date.setText(date);

							clearFocusable(btn_plandetail_loan_last_emi_date);
							setFocusable(cb_bi_smart_shield_criti_care_13_non_linked_rider);
							cb_bi_smart_shield_criti_care_13_non_linked_rider.requestFocus();
						} else {
							commonMethods.BICommonDialog(context, "Date of Last EMI should be greater than the Date of First EMI");
							lastEmiDate = "";
							btn_plandetail_loan_last_emi_date
									.setText("Select Date");
						}

					} else {
						commonMethods.BICommonDialog(context, "Please Select 1st Emi date First");
						lastEmiDate = "";
						btn_plandetail_loan_last_emi_date.setText("Select Date");
					}
					break;

				default:
					break;
			}
		}
	}

	private int calculateMyAge(int year, int month, int day) {
		Calendar nowCal = new GregorianCalendar(year, month, day);

		String[] ProposerDob = getDate(lifeAssured_date_of_birth).split("/");
		// int age = Integer.parseInt(ProposerDob[3]) -
		// birthCal.get(Calendar.YEAR);

		int age = nowCal.get(Calendar.YEAR) - Integer.parseInt(ProposerDob[2]);

		boolean isMonthGreater = Integer.parseInt(ProposerDob[1]) >= nowCal
				.get(Calendar.MONTH);

		boolean isMonthSameButDayGreater = Integer.parseInt(ProposerDob[1]) == nowCal
				.get(Calendar.MONTH)
				&& Integer.parseInt(ProposerDob[1]) > nowCal
				.get(Calendar.DAY_OF_MONTH);

		if (isMonthGreater || isMonthSameButDayGreater) {
			age = age - 1;
		}
		return age;
	}

	public void onClickBackDating(View v) {
		// setDefaultDate();
		DIALOG_ID = 6;
		if (lifeAssured_date_of_birth != null
				&& !lifeAssured_date_of_birth.equals("")) {
			showDialog(DATE_DIALOG_ID);
		}

		else {
			commonMethods.dialogWarning(context, "Please select a LifeAssured DOB First", true);
		}
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

	private int getIndex(Spinner s1, String value) {

		int index = 0;

		for (int i = 0; i < s1.getCount(); i++) {
			if (s1.getItemAtPosition(i).equals(value)) {
				index = i;
			}
		}
		return index;
	}

	private void addListenerOnSubmit() {
		// TODO Auto-generated method stub
		// Insert data entered by user in an object
		smartShieldBean = new SmartShieldBean();

		/**
		 * Applicable Taxes for JK Resident Change as per 1,Jan,2014 by Akshaya Mirajkar.
		 */

		if (cb_staffdisc.isChecked()) {
			smartShieldBean.setStaffDisc(true);
		} else {
			smartShieldBean.setStaffDisc(false);

		}

		if(cb_kerladisc.isChecked())
		{
			smartShieldBean.setKerlaDisc(true);
		}
		else
		{
			smartShieldBean.setKerlaDisc(false);
		}

		if (CbJkResident.isChecked()) {
			smartShieldBean.setJKResident(true);
		} else {
			smartShieldBean.setJKResident(false);
		}

		if (cb_bi_smart_shield_criti_care_13_non_linked_rider.isChecked()) {
			smartShieldBean.setAccCI_SA(Integer.parseInt(et_bi_smart_shield_criti_care_13_non_linked_rider_sum_assured.getText()
					.toString()));
		}
		if (cbADBRider.isChecked()) {
			smartShieldBean.setADB_SA(Integer.parseInt(edtAdbSA.getText()
					.toString()));
		}

		/*
		  Change as per 1,Jan,2014 by Akshaya Mirajkar.
		 */
		if (cbATPDBRider.isChecked()) {
			smartShieldBean.setATPDB_SA(Integer.parseInt(edtAtpdbSA.getText()
					.toString()));
		}

		smartShieldBean.setADB_Term(Integer.parseInt(spnr_AdbTerm
				.getSelectedItem().toString()));
		smartShieldBean.setATPDB_Term(Integer.parseInt(spnr_AtpdbTerm
				.getSelectedItem().toString()));
		smartShieldBean.setAccCI_Term(Integer.parseInt(spnr_bi_smart_shield_criti_care_13_non_linked_rider_term
				.getSelectedItem().toString()));
		smartShieldBean.setADB_Status(cbADBRider.isChecked());
		smartShieldBean.setATPDB_Status(cbATPDBRider.isChecked());
		smartShieldBean.setAccCI_Status(cb_bi_smart_shield_criti_care_13_non_linked_rider.isChecked());

		smartShieldBean.setAge(Integer
				.parseInt(spnr_bi_smart_shield_life_assured_age
						.getSelectedItem().toString()));
		smartShieldBean.setBasicSA(Integer.parseInt(edtBasicSA.getText()
				.toString()));
		smartShieldBean.setBasicTerm(Integer.parseInt(spnrPolicyTerm
				.getSelectedItem().toString()));
		smartShieldBean.setGender(spnrGender.getSelectedItem().toString());
		smartShieldBean.setPlanName(spnrPlan.getSelectedItem().toString());
		smartShieldBean.setPremFreq(spnrPremFrequency.getSelectedItem()
				.toString());
		String premiumFreq = spnrPremFrequency.getSelectedItem().toString();
		smartShieldBean.setType(Is_Smoker_or_Not);
		// smartShieldBean.setLRI(spnrLoanRateInterest.getSelectedItem()
		// .toString());
		// smartShieldBean.setStaffDisc(selStaffDisc.isChecked());
		smartShieldBean.setLRI(lifeAssured_loan_rate_interest);

		smartShieldBean.setLoanAccountNumber(loanAccountNo);
		smartShieldBean.setLoanFinancialInstitue(financingInstituteName);
		smartShieldBean.setLoanCategory(loanCategory);
		smartShieldBean.setLoanSumAssuredOutstanding(sumAssuredOutStanding);
		smartShieldBean.setLoanBalanceLoanTenure(balanceLoanTenure);
		smartShieldBean.setLoanFirstEmiDate(firstEmiDate);
		smartShieldBean.setLoanLastEmiDate(lastEmiDate);

		// Show Output Form
		showSmartShieldOutputPg(smartShieldBean);
	}

	private void showSmartShieldOutputPg(SmartShieldBean smartShieldBean) {
		retVal = new StringBuilder();
		// TODO Auto-generated method stub
		SmartShieldBusinessLogic smartShieldBusinessLogic = new SmartShieldBusinessLogic(
				smartShieldBean);
		SmartShieldProperties prop = new SmartShieldProperties();
		CommonForAllProd commonForAllProd = new CommonForAllProd();

		/* added by vrushali on 09/04/2015 start */
		/* Added by Akshaya on 31-Mar-15 start */
		double discountPercentage = smartShieldBusinessLogic
				.getDiscPercentage("basic");
		/* End */
		String minesOccuInterest = ""
				+ smartShieldBusinessLogic.getMinesOccuInterest(smartShieldBean
				.getBasicSA());

		String MinesOccuInterest = ""
				+ smartShieldBusinessLogic.getMinesOccuInterest(smartShieldBean
				.getBasicSA());

		String servicetax_MinesOccuInterest = ""
				+ smartShieldBusinessLogic
				.getServiceTaxMines(Double.parseDouble(MinesOccuInterest));

		MinesOccuInterest = "" + (Double.parseDouble(MinesOccuInterest) + Double.parseDouble(servicetax_MinesOccuInterest));

		double premium_Basic_WithoutDisc_Yearly = smartShieldBusinessLogic
				.getBasicPremium_Yearly("basic");
		double large_SA_discount = smartShieldBusinessLogic
				.getLargeSADiscount(premium_Basic_WithoutDisc_Yearly);
		double premBasic_AfterLSA_disc = premium_Basic_WithoutDisc_Yearly
				- large_SA_discount;
		double premBasic_AfterModal_N_LSA_disc = smartShieldBusinessLogic
				.getDiscAfterFreqLoading_Basic_SelFreq(premBasic_AfterLSA_disc);
		double staffDiscount = premium_Basic_WithoutDisc_Yearly
				* smartShieldBusinessLogic.getDiscPercentage("Basic");
		double premBasic_AfterStaffDisc = premium_Basic_WithoutDisc_Yearly
				- large_SA_discount - staffDiscount;
		double premBasic_AfterFreqLoading = smartShieldBusinessLogic
				.getDiscAfterFreqLoading_Basic_SelFreq(premBasic_AfterStaffDisc);

		double premium_ADB_WithoutDisc_Yearly = smartShieldBusinessLogic
				.getBasicPremium_Yearly("ADB");
		double large_SA_ADB_discount = 0;
		double premADB_AfterLSA_disc = premium_ADB_WithoutDisc_Yearly
				- large_SA_ADB_discount;
		double premADB_AfterModal_N_LSA_disc = smartShieldBusinessLogic
				.getDiscAfterFreqLoading_Rider_SelFreq(premADB_AfterLSA_disc);
		double staffDiscount_ADB = premium_ADB_WithoutDisc_Yearly
				* smartShieldBusinessLogic.getDiscPercentage("ADB");
		double premADB_AfterStaffDisc = premium_ADB_WithoutDisc_Yearly
				- large_SA_ADB_discount - staffDiscount_ADB;
		double premADB_AfterFreqLoading = smartShieldBusinessLogic
				.getDiscAfterFreqLoading_Rider_SelFreq(premADB_AfterStaffDisc);

		double premium_ATPD_WithoutDisc_Yearly = smartShieldBusinessLogic
				.getBasicPremium_Yearly("ATPD");
		double large_SA_ATPD_discount = 0;
		double premATPD_AfterLSA_disc = premium_ATPD_WithoutDisc_Yearly
				- large_SA_ATPD_discount;
		double premATPD_AfterModal_N_LSA_disc = smartShieldBusinessLogic
				.getDiscAfterFreqLoading_Rider_SelFreq(premATPD_AfterLSA_disc);
		double staffDiscount_ATPD = premium_ATPD_WithoutDisc_Yearly
				* smartShieldBusinessLogic.getDiscPercentage("ATPD");
		double premATPD_AfterStaffDisc = premium_ATPD_WithoutDisc_Yearly
				- large_SA_ATPD_discount - staffDiscount_ATPD;
		double premATPD_AfterFreqLoading = smartShieldBusinessLogic
				.getDiscAfterFreqLoading_Rider_SelFreq(premATPD_AfterStaffDisc);

		double premium_criti_WithoutDisc_Yearly = smartShieldBusinessLogic
				.getBasicPremium_Yearly("CRITI");
		double large_SA_criti_discount = 0;
		double premCRITI_AfterLSA_disc = premium_criti_WithoutDisc_Yearly
				- large_SA_criti_discount;
		double premCRITI_AfterModal_N_LSA_disc = smartShieldBusinessLogic
				.getDiscAfterFreqLoading_Rider_SelFreq(premCRITI_AfterLSA_disc);
		double staffDiscount_CRITI = premium_criti_WithoutDisc_Yearly
				* smartShieldBusinessLogic.getDiscPercentage("CRITI");
		double premCriti_AfterStaffDisc = premium_criti_WithoutDisc_Yearly
				- large_SA_criti_discount - staffDiscount_CRITI;
		double premCriti_AfterFreqLoading = smartShieldBusinessLogic
				.getDiscAfterFreqLoading_Rider_SelFreq(premCriti_AfterStaffDisc);

		/*Commented by machindra*/
       /* double totalAnnualizedPrem = premBasic_AfterModal_N_LSA_disc
				+ premADB_AfterModal_N_LSA_disc
				+ premATPD_AfterModal_N_LSA_disc
				+ premCRITI_AfterModal_N_LSA_disc;
		double totalAnnualizedPremWithDisc = premBasic_AfterFreqLoading
				+ premADB_AfterFreqLoading + premATPD_AfterFreqLoading
                + premCriti_AfterFreqLoading;*/



		//added on 19th Dec 2018 start
		double premBasic_AfterModal_N_LSA_disc_roundoff=Double.parseDouble(commonForAllProd.getRoundOffLevel2New( commonForAllProd.getStringWithout_E(premBasic_AfterModal_N_LSA_disc)));
		double premADB_AfterModal_N_LSA_disc_roundoff=Double.parseDouble(commonForAllProd.getRoundOffLevel2New( commonForAllProd.getStringWithout_E(premADB_AfterModal_N_LSA_disc)));
		double premATPD_AfterModal_N_LSA_disc_roundoff=Double.parseDouble(commonForAllProd.getRoundOffLevel2New( commonForAllProd.getStringWithout_E(premATPD_AfterModal_N_LSA_disc)));
		double premCRITI_AfterModal_N_LSA_disc_roundoff=Double.parseDouble(commonForAllProd.getRoundOffLevel2New( commonForAllProd.getStringWithout_E(premCRITI_AfterModal_N_LSA_disc)));
		//added on 19th Dec 2018 end

		//double totalAnnualizedPrem=premBasic_AfterModal_N_LSA_disc+premADB_AfterModal_N_LSA_disc+premATPD_AfterModal_N_LSA_disc+premCRITI_AfterModal_N_LSA_disc;
		double totalAnnualizedPrem=premBasic_AfterModal_N_LSA_disc_roundoff+premADB_AfterModal_N_LSA_disc_roundoff+premATPD_AfterModal_N_LSA_disc_roundoff+premCRITI_AfterModal_N_LSA_disc_roundoff;

		//added on 19th Dec 2018 start
		double premBasic_AfterFreqLoading_roundoff=Double.parseDouble(commonForAllProd.getRoundOffLevel2New( commonForAllProd.getStringWithout_E(premBasic_AfterFreqLoading)));
		double premADB_AfterFreqLoading_roundoff=Double.parseDouble(commonForAllProd.getRoundOffLevel2New( commonForAllProd.getStringWithout_E(premADB_AfterFreqLoading)));
		double premATPD_AfterFreqLoading_roundoff=Double.parseDouble(commonForAllProd.getRoundOffLevel2New( commonForAllProd.getStringWithout_E(premATPD_AfterFreqLoading)));
		double premCriti_AfterFreqLoading_roundoff=Double.parseDouble(commonForAllProd.getRoundOffLevel2New( commonForAllProd.getStringWithout_E(premCriti_AfterFreqLoading)));
		//added on 19th Dec 2018 end

		//double totalAnnualizedPremWithDisc=premBasic_AfterFreqLoading+premADB_AfterFreqLoading+premATPD_AfterFreqLoading+premCriti_AfterFreqLoading;
		double totalAnnualizedPremWithDisc=premBasic_AfterFreqLoading_roundoff+premADB_AfterFreqLoading_roundoff+premATPD_AfterFreqLoading_roundoff+premCriti_AfterFreqLoading_roundoff;


		double basicPremWithoutDisc = smartShieldBusinessLogic.getPR_Basic_yearly();

		/****************************** Added by Akshaya on 03-MAR-15 start **********************/

		// MinesOccuInterest = ""
		// + smartShieldBusinessLogic.getMinesOccuInterest(smartShieldBean
		// .getBasicSA());
		// totalAnnualizedPrem = totalAnnualizedPrem
		// + Double.parseDouble(MinesOccuInterest);

		/****************************** Added by Akshaya on 03-MAR-15 end **********************/

       /* String totalInstPremiumWithoutDiscWithoutST = commonForAllProd
				.getRoundUp(commonForAllProd
						.getStringWithout_E(totalAnnualizedPrem));
		String totalInstPremiumWithDiscWithoutST = commonForAllProd
				.getRoundUp(commonForAllProd
                        .getStringWithout_E(totalAnnualizedPremWithDisc));*/

        String totalInstPremiumWithoutDiscWithoutST=commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(totalAnnualizedPrem));
        String totalInstPremiumWithDiscWithoutST=commonForAllProd.getRoundUp(commonForAllProd.getRoundOffLevel2New(commonForAllProd.getStringWithout_E(totalAnnualizedPremWithDisc)));

        String totalInstPremiumWithDiscWithoutST_OCCU = commonForAllProd
                .getRoundUp(commonForAllProd.getRoundOffLevel2New(commonForAllProd
                        .getStringWithout_E(totalAnnualizedPremWithDisc)));


       /* totalInstPremiumWithDiscWithoutST_OCCU=commonForAllProd
                .getStringWithout_E(Double.parseDouble(totalInstPremiumWithDiscWithoutST_OCCU)   + Double.parseDouble(DefOccuInterest));
*/
		/****************************** Added by Akshaya on 20-MAY-15 start **********************/

       /* double basicServiceTax = smartShieldBusinessLogic.getServiceTax(
				Double.parseDouble(totalInstPremiumWithDiscWithoutST),
				smartShieldBean.isJKResident(), "basic");
		double SBCServiceTax = smartShieldBusinessLogic.getServiceTax(
				Double.parseDouble(totalInstPremiumWithDiscWithoutST),
				smartShieldBean.isJKResident(), "SBC");
		double KKCServiceTax = smartShieldBusinessLogic.getServiceTax(
				Double.parseDouble(totalInstPremiumWithDiscWithoutST),
                smartShieldBean.isJKResident(), "KKC");*/

		double basicServiceTax = 0;
		double SBCServiceTax = 0;
		double KKCServiceTax = 0;

		double KerlaServiceTax =0;
		double KeralaCessServiceTax =0;




        double basicServiceTax_occu = 0;
        double SBCServiceTax_occu = 0;
        double KKCServiceTax_occu = 0;

        double KerlaServiceTax_occu = 0;
        double KeralaCessServiceTax_occu = 0;

		boolean isKerlaDiscount = smartShieldBean.isKerlaDisc();
		if(isKerlaDiscount){
			basicServiceTax = smartShieldBusinessLogic.getServiceTax(Double.parseDouble(totalInstPremiumWithDiscWithoutST),smartShieldBean.isJKResident(),"basic");
			KerlaServiceTax = smartShieldBusinessLogic.getServiceTax(Double.parseDouble(totalInstPremiumWithDiscWithoutST),smartShieldBean.isJKResident(),"KERALA");
			KeralaCessServiceTax =KerlaServiceTax-basicServiceTax;


            /*basicServiceTax_occu = smartShieldBusinessLogic.getServiceTax_defOccupation(Double.parseDouble(totalInstPremiumWithDiscWithoutST_OCCU), smartShieldBean.isJKResident(), "basic");
            KerlaServiceTax_occu = smartShieldBusinessLogic.getServiceTax_defOccupation(Double.parseDouble(totalInstPremiumWithDiscWithoutST_OCCU), smartShieldBean.isJKResident(), "KERALA");
            KeralaCessServiceTax_occu = KerlaServiceTax_occu - basicServiceTax_occu;*/


		}else{
			basicServiceTax = smartShieldBusinessLogic.getServiceTax(Double.parseDouble(totalInstPremiumWithDiscWithoutST),smartShieldBean.isJKResident(),"basic");
			SBCServiceTax = smartShieldBusinessLogic.getServiceTax(Double.parseDouble(totalInstPremiumWithDiscWithoutST),smartShieldBean.isJKResident(),"SBC");
			KKCServiceTax = smartShieldBusinessLogic.getServiceTax(Double.parseDouble(totalInstPremiumWithDiscWithoutST),smartShieldBean.isJKResident(),"KKC");


            /*basicServiceTax_occu = smartShieldBusinessLogic.getServiceTax_defOccupation(Double.parseDouble(totalInstPremiumWithDiscWithoutST_OCCU), smartShieldBean.isJKResident(), "basic");
            SBCServiceTax_occu = smartShieldBusinessLogic.getServiceTax_defOccupation(Double.parseDouble(totalInstPremiumWithDiscWithoutST_OCCU), smartShieldBean.isJKResident(), "SBC");
            KKCServiceTax_occu = smartShieldBusinessLogic.getServiceTax_defOccupation(Double.parseDouble(totalInstPremiumWithDiscWithoutST_OCCU), smartShieldBean.isJKResident(), "KKC");*/


        }


		String serviceTax = commonForAllProd.getStringWithout_E(basicServiceTax
				+ SBCServiceTax + KKCServiceTax + KerlaServiceTax);


       /* String serviceTax_occu = commonForAllProd.getRound(commonForAllProd.getStringWithout_E(basicServiceTax_occu
                + SBCServiceTax_occu + KKCServiceTax_occu + KerlaServiceTax_occu));*/

        // String serviceTax = commonForAllProd
        // .getRoundUp(""
        // + (Double.parseDouble(commonForAllProd.getRoundUp(""
        // + smartShieldBusinessLogic
        // .getTotalInstallPremWithDisc(totalAnnualizedPrem))) *
        // (smartShieldBusinessLogic
        // .getServiceTax())));
        // System.out.println("serviceTax : " + serviceTax);

        // String totalInstPremiumWithDiscWithST = commonForAllProd
        // .getRoundUp(""
        // + (smartShieldBusinessLogic
        // .getTotalInstallPremWithDisc(totalAnnualizedPrem) + Double
        // .parseDouble(serviceTax)));

		String totalInstPremiumWithDiscWithST = commonForAllProd
				.getStringWithout_E(Double
						.parseDouble(totalInstPremiumWithDiscWithoutST)
						+ Double.parseDouble(serviceTax));


        /*String totalInstPremiumWithDiscWithST_occu = commonForAllProd
                .getStringWithout_E(Double
                        .parseDouble(totalInstPremiumWithDiscWithoutST_OCCU)
                        + Double.parseDouble(serviceTax_occu));*/

        // premBasic = premBasic_AfterModal_N_LSA_disc;
        // premAccCI = premCRITI_AfterModal_N_LSA_disc;
        // premADB = premADB_AfterModal_N_LSA_disc;
        // premATPDB = premATPD_AfterModal_N_LSA_disc;
        // if (smartShieldBean.getStaffDisc() == true) {
        // basePremWithDisc_exclST = Integer
        // .parseInt(""
        // + commonForAllProd.getRoundUp(""
        // + (smartShieldBusinessLogic.getPR("basic") - smartShieldBusinessLogic
        // .getDiscAfterFreqLoading_Basic_SelFreq())));
        // } else {
        // basePremWithDisc_exclST = Integer.parseInt(""
        // + commonForAllProd.getRoundUp(""
        // + smartShieldBusinessLogic.getPR("basic")));
        // }
        //
        /************************* output starts here ********************************************/

		/*
		 * Intent i = new Intent(getApplicationContext(), success.class);
		 * TextView op = new TextView(this); TextView op1 = new TextView(this);
		 * TextView op2 = new TextView(this); TextView op3 = new TextView(this);
		 * TextView op4 = new TextView(this); TextView op5 = new TextView(this);
		 * TextView op6 = new TextView(this); op.setText("Basic Premium is Rs."
		 * + currencyFormat.format(Double
		 * .parseDouble(premiumBasicWithoutDisc_ForSelFreq)));
		 * op1.setText("Installment Premium is Rs." +
		 * currencyFormat.format(Double .parseDouble(totalPremWithoutDisc)));
		 *
		 * op2.setText("Installment Premium with SBG Discount is Rs." +
		 * currencyFormat.format(Double
		 * .parseDouble(totalInstPremiumWithDiscWithoutST)));
		 * op3.setText("Installment Premium Inclusive GST is Rs." +
		 * currencyFormat.format(Double
		 * .parseDouble(totalInstPremiumWithDiscWithST)));
		 */

		// validation for minimum premium
		// System.out.println("premium " + basePremWithDisc_exclST);
		double sumOfRiders = premADB_AfterModal_N_LSA_disc
				+ premATPD_AfterModal_N_LSA_disc;
		/* Added by Akshaya on 31-Mar-15 start ***/
		valPremiumError = valInstPremium(String
				.valueOf(premBasic_AfterFreqLoading));
		// valPremiumError=true;
		valRiderPremiumError = valRiderPremium(premBasic_AfterModal_N_LSA_disc,
				sumOfRiders, premCRITI_AfterModal_N_LSA_disc);
		// valRiderPremiumError=true;
		/* Added by Akshaya on 31-Mar-15 end ***/
		if (valPremiumError && valRiderPremiumError) {
			// PTA
			if (cb_bi_smart_shield_criti_care_13_non_linked_rider.isChecked()) {
			}

			// ADB
			if (cbADBRider.isChecked()) {
			}

			// ATPDB
			if (cbATPDBRider.isChecked()) {

			}

			/* Added by Akshaya on 31-Mar-15 start ***/
			String staffStatus;
			if (cb_staffdisc.isChecked()) {
				staffStatus = "sbi";
				// disc_Basic_SelFreq
			} else {
				staffStatus = "none";
			}
			String isSmoker = "";
			if (rb_smart_shield_proposer_smoker_yes.isChecked()) {
				isSmoker = "Y";
			} else {
				isSmoker = "N";
			}

			/* Added by Akshaya on 31-Mar-15 end ***/
			try {
				retVal.append("<?xml version='1.0' encoding='utf-8' ?><SmartShield>");
				retVal.append("<errCode>0</errCode>");
                /*** Added by Akshaya on 31-Mar-15 start ***/
                retVal.append("<staffStatus>" + staffStatus + "</staffStatus>");
                retVal.append("<staffRebate>" + discountPercentage
                        + "</staffRebate>");
                retVal.append("<servcTax>" + serviceTax + "</servcTax>");
                retVal.append("<smokerOrNot>" + isSmoker + "</smokerOrNot>");
                retVal.append("<basicPremWithoutDisc>"
                        //+ premBasic_AfterModal_N_LSA_disc
                        + commonForAllProd.getRoundOffLevel2New(commonForAllProd.getStringWithout_E(premBasic_AfterModal_N_LSA_disc))
                        + "</basicPremWithoutDisc>");
                retVal.append("<basicPremWithoutDiscSA>"
                        //  + premium_Basic_WithoutDisc_Yearly
                        + commonForAllProd.getRoundOffLevel2New(commonForAllProd.getStringWithout_E(premium_Basic_WithoutDisc_Yearly))
                        + "</basicPremWithoutDiscSA>");
                retVal.append("<premADBWithoutDisc>"
                        // + premADB_AfterModal_N_LSA_disc

                        + commonForAllProd.getStringWithout_E(premADB_AfterModal_N_LSA_disc)
                        + "</premADBWithoutDisc>");
                retVal.append("<premADBWithoutDiscSA>"
                        // + premium_ADB_WithoutDisc_Yearly

                        + commonForAllProd.getStringWithout_E(premium_ADB_WithoutDisc_Yearly)
                        + "</premADBWithoutDiscSA>");
                retVal.append("<premATPDBWithoutDisc>"
                        + commonForAllProd.getStringWithout_E(premATPD_AfterModal_N_LSA_disc)
                        + "</premATPDBWithoutDisc>");
                retVal.append("<premATPDBWithoutDiscSA>"
                        + commonForAllProd.getStringWithout_E(premium_ATPD_WithoutDisc_Yearly)
                        + "</premATPDBWithoutDiscSA>");

                retVal.append("<premCC13WithoutDisc>"
                        + commonForAllProd.getRoundOffLevel2New(commonForAllProd.getStringWithout_E(premCRITI_AfterModal_N_LSA_disc))
                        + "</premCC13WithoutDisc>");
                retVal.append("<premCC13WithoutDiscSA>"
                        + commonForAllProd.getRoundOffLevel2New(commonForAllProd.getStringWithout_E(premium_criti_WithoutDisc_Yearly))
                        + "</premCC13WithoutDiscSA>");

				//
				// retVal.append("<basicPremWithoutDisc>"
				// + 0
				// + "</basicPremWithoutDisc>");
				// retVal.append("<basicPremWithoutDiscSA>"
				// + 0
				// + "</basicPremWithoutDiscSA>");
				// retVal.append("<premADBWithoutDisc>"
				// +0
				// + "</premADBWithoutDisc>");
				// retVal.append("<premADBWithoutDiscSA>"
				// + 0
				// + "</premADBWithoutDiscSA>");
				// retVal.append("<premATPDBWithoutDisc>"
				// + 0
				// + "</premATPDBWithoutDisc>");
				// retVal.append("<premATPDBWithoutDiscSA>"
				// + 0
				// + "</premATPDBWithoutDiscSA>");
				// retVal.append("<premCC13WithoutDisc>"
				// + 0
				// + "</premCC13WithoutDisc>");
				// retVal.append("<premCC13WithoutDiscSA>"
				// + 0
				// + "</premCC13WithoutDiscSA>");

				/*
				  <OccuInt> tag value will be stored in nbd_occu_extra of
				  T_new_business_detail

				 */

				//retVal.append("<OccuInt>").append(minesOccuInterest).append("</OccuInt>");
				retVal.append("<OccuInt>").append(MinesOccuInterest).append("</OccuInt>");
				retVal.append("<OccuIntServiceTax>" + servicetax_MinesOccuInterest + "</OccuIntServiceTax>");

				// <staffRebate>
                /*** Added by Akshaya on 31-Mar-15 end ***/
                retVal.append("<basicPremWthOutDisc>"
                        + commonForAllProd.getRoundOffLevel2New(commonForAllProd
                        .getStringWithout_E(premBasic_AfterModal_N_LSA_disc))
                        + "</basicPremWthOutDisc>"
                        + "<InstmntPrem>"
                        + totalInstPremiumWithDiscWithoutST
                        + "</InstmntPrem>"// totalInstPremiumWithDiscWithoutST

                        + "<InstmntPremWithoutDisc>"
                        + totalInstPremiumWithoutDiscWithoutST
                        + "</InstmntPremWithoutDisc>"
                        + "<InstmntPremWthST>"
                        + totalInstPremiumWithDiscWithST
                        + "</InstmntPremWthST>"
                        + "<AccCIPrem>"
                        + commonForAllProd.getRoundOffLevel2New(commonForAllProd
                        .getStringWithout_E(premCRITI_AfterModal_N_LSA_disc))
                        + "</AccCIPrem>"
                        + "<ADBPrem>"
                        + commonForAllProd.getRoundOffLevel2New(commonForAllProd
                        .getStringWithout_E(premADB_AfterModal_N_LSA_disc))
                        + "</ADBPrem>"
                        + "<ATPDPrem>"
                        + commonForAllProd.getRoundOffLevel2New(commonForAllProd
                        .getStringWithout_E(premATPD_AfterModal_N_LSA_disc))
                        + "</ATPDPrem>");

                /*** modified by Akshaya on 20-MAY-16 start **/

                retVal.append("<basicServiceTax>"
                        + commonForAllProd.getStringWithout_E(basicServiceTax)
                        + "</basicServiceTax>" + "<SBCServiceTax>"
                        + commonForAllProd.getStringWithout_E(SBCServiceTax)
                        + "</SBCServiceTax>" + "<KKCServiceTax>"
                        + commonForAllProd.getStringWithout_E(KKCServiceTax)
                        + "</KKCServiceTax>" +
                "<KeralaCessServiceTax>"  + commonForAllProd.getStringWithout_E(KeralaCessServiceTax)  + "</KeralaCessServiceTax>");

                /*** modified by Akshaya on 20-MAY-16 e **/

               /* retVal.append(
                        "<DefOccuInt>"
                                + DefOccuInterest
                                + "</DefOccuInt>" +
                                "<FYtotInstPrem_inclST_DEF_OCCU>"

                                + totalInstPremiumWithDiscWithST_occu
                                + "</FYtotInstPrem_inclST_DEF_OCCU>" +
                                "<FYServiceTax_DEF_OCCU>" + serviceTax_occu
                                + "</FYServiceTax_DEF_OCCU>");*/

				retVal.append("</SmartShield>");
			} catch (Exception e) {
                retVal.append("<?xml version='1.0' encoding='utf-8' ?><SmartShield>"
                        + "<errCode>1</errCode>"
                        + "<errorMessage>"
                        + e.getMessage() + "</errorMessage></SmartShield>");
			}

			/******************************************** xml Output *************************************/

			System.out.println("Final output in xml" + retVal.toString());

			/*
			 * i.putExtra("op", op.getText().toString()); i.putExtra("op1",
			 * op1.getText().toString()); if (smartShieldBean.getStaffDisc())
			 * i.putExtra("op2", op2.getText().toString()); i.putExtra("op3",
			 * op3.getText().toString()); i.putExtra("pdf", retVal.toString());
			 * smartShieldBean = smartShieldBean; // Log.e("ss", (String)
			 * op.getText()); startActivity(i);
			 */

			/********************** output ends here ********************************************/
		}
	}

	/************************* Validation starts here ********************************************/

	// Validate Minimum premium
	private boolean valInstPremium(String premiumBasicWithoutDisc_ForSelFreq) {

		if (spnrPremFrequency.getSelectedItem().toString().equals("Yearly")
				&& (Double.parseDouble(premiumBasicWithoutDisc_ForSelFreq) < 3000)) {
			showAlert
					.setMessage("Minimum premium for Yearly Mode under this product is Rs. 3000");
			showAlert.setNeutralButton("OK",
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {
						}
					});
			showAlert.show();

			return false;

		} else if (spnrPremFrequency.getSelectedItem().toString()
				.equals("Half Yearly")
				&& (Double.parseDouble(premiumBasicWithoutDisc_ForSelFreq) < 1500)) {
			showAlert
					.setMessage("Minimum premium for Half Yearly Mode under this product is Rs. 1500");
			showAlert.setNeutralButton("OK",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
						}
					});
			showAlert.show();

			return false;
		} else if (spnrPremFrequency.getSelectedItem().toString()
				.equals("Quarterly")
				&& (Double.parseDouble(premiumBasicWithoutDisc_ForSelFreq) < 750)) {
			showAlert
					.setMessage("Minimum premium for Quarterly Mode under this product is Rs. 750");
			showAlert.setNeutralButton("OK",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
						}
					});
			showAlert.show();
			return false;
		} else if (spnrPremFrequency.getSelectedItem().toString()
				.equals("Monthly")
				&& (Double.parseDouble(premiumBasicWithoutDisc_ForSelFreq) < 300)) {
			showAlert
					.setMessage("Minimum premium for Monthly Mode under this product is Rs. 300");
			showAlert.setNeutralButton("OK",
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {
						}
					});
			showAlert.show();

			return false;
		} else if (spnrPremFrequency.getSelectedItem().toString()
				.equals("Single")
				&& (Double.parseDouble(premiumBasicWithoutDisc_ForSelFreq) < 11000)) {
			showAlert
					.setMessage("Minimum premium for Single Mode under this product is Rs. 11000");
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

	// Validate Rider Premium
	private boolean valRiderPremium(double premBasic, double sumOfRiders,
									double premCRITI_AfterModal_N_LSA_disc) {
		String error = "";
		if ((premBasic * 0.3) < sumOfRiders) {
			error = "Total of Rider Premium should not be greater than 30% of the Base Premium";

		} else if (premBasic < premCRITI_AfterModal_N_LSA_disc) {
			error = "Premium for Criti Care 13 rider should not be more than the base premium.";

		} else
			return true;
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

	// Validate Sum Assured
	private boolean valSA() {
		double minRiderLimit, maxRiderLimit;

		StringBuilder error = new StringBuilder();
        boolean flagSA = false;

		if (edtBasicSA.getText().toString().equals("")
				|| Double.parseDouble(edtBasicSA.getText().toString()) < 2500000) {
			error.append("Sum assured should be greater than or equal to 25,00,000");
		} else if (loandetails) {
			@SuppressWarnings("unused")
			int datecomparison = Datecomparison();

			if (Double.parseDouble(edtBasicSA.getText().toString()) > Double
					.parseDouble(et_sum_assured_outstanding_loan_amount
							.getText().toString())) {
				error.append("Sum assured should not be greater than outstanding loan amount");
			} else if ((Integer.parseInt(et_balance_loan_tenure.getText()
					.toString()) / 12) < Integer.parseInt(spnrPolicyTerm
					.getSelectedItem().toString())) {
				error.append("Policy term should not be greater then outstanding term");
			}
			/*
			 * else if (datecomparison > 0) {
			 * error.append("Future date cannot be entered for 1st Emi date"); }
			 */

		} else if (Double.parseDouble(edtBasicSA.getText().toString()) % 100000 != 0) {
			error.append("Sum assured should be multiple of 1,00,000");
		} else {

			if (cb_bi_smart_shield_criti_care_13_non_linked_rider.isChecked()) {
				minRiderLimit = 25000;
				maxRiderLimit = Math.min(2000000,
						Double.parseDouble(edtBasicSA.getText().toString()));

				if (et_bi_smart_shield_criti_care_13_non_linked_rider_sum_assured.getText().toString().equals("")
						|| Double.parseDouble(et_bi_smart_shield_criti_care_13_non_linked_rider_sum_assured
						.getText().toString()) < minRiderLimit
						|| Double.parseDouble(et_bi_smart_shield_criti_care_13_non_linked_rider_sum_assured
						.getText().toString()) > maxRiderLimit){
					error.append("\nEnter Sum assured for Criti Care 13 Non Linked Rider between ").append(currencyFormat.format(minRiderLimit)).append(" and ").append(currencyFormat.format(maxRiderLimit));
				} else if (Double.parseDouble(et_bi_smart_shield_criti_care_13_non_linked_rider_sum_assured.getText().toString()) % 1000 != 0) {
					error.append("Sum assured for Criti Care 13 Non Linked Rider should be multiple of 1,000");
				}
			}

			if (cbADBRider.isChecked()) {
				minRiderLimit = 25000;
               /* maxRiderLimit = Math.min(10000000,
                        Double.parseDouble(edtBasicSA.getText().toString()));*/

				maxRiderLimit = Math.min(5000000,
						Double.parseDouble(edtBasicSA.getText().toString()));

				if (edtAdbSA.getText().toString().equals("")
						|| Double.parseDouble(edtAdbSA.getText().toString()) < minRiderLimit
						|| Double.parseDouble(edtAdbSA.getText().toString()) > maxRiderLimit){
					error.append("\nEnter Sum assured for Accidental Death Benefit Rider between ").append(currencyFormat.format(minRiderLimit)).append(" and ").append(currencyFormat.format(maxRiderLimit));
				} else if (Double.parseDouble(edtAdbSA.getText().toString()) % 1000 != 0) {
					error.append("Sum assured for Accidental Death Benefit Rider should be multiple of 1,000");
				}
			}
			if (cbATPDBRider.isChecked()) {
				minRiderLimit = 25000;
				maxRiderLimit = Math.min(5000000,
						Double.parseDouble(edtBasicSA.getText().toString()));
				if (edtAtpdbSA.getText().toString().equals("")
						|| Double.parseDouble(edtAtpdbSA.getText().toString()) < minRiderLimit
						|| Double.parseDouble(edtAtpdbSA.getText().toString()) > maxRiderLimit) {
					error.append("\nEnter Sum assured for Accidental Total and Permenent Disability Benefit Rider between ").append(currencyFormat.format(minRiderLimit)).append(" and ").append(currencyFormat.format(maxRiderLimit));
				} else if (Double.parseDouble(edtAtpdbSA.getText().toString()) % 1000 != 0) {
					error.append("Sum assured for Accidental Total and Permenent Disability Benefit Rider should be multiple of 1,000");
				}
			}
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

	/**
	 * Change as per 1,Jan,2014 by Akshaya Mirajkar.
	 */
	// Validate Policy term
	private boolean valTerm() {
		int maxLimit = Math.min(80, 80 - Integer
				.parseInt(spnr_bi_smart_shield_life_assured_age
						.getSelectedItem().toString()));
		if (Integer.parseInt(spnrPolicyTerm.getSelectedItem().toString()) > maxLimit) {
			showAlert.setMessage("Please enter Policy Term between 5 and "
					+ maxLimit);
			showAlert.setNeutralButton("OK",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							spnrPolicyTerm.setSelection(0, false);
						}
					});
			showAlert.show();

			return false;
		} else {
			return true;
		}
	}

	// Validate rider term
	private boolean valTermRider() {
		int minLimit = 5;
		boolean flagTermRider = false;
		int vage = Integer.parseInt(spnr_bi_smart_shield_life_assured_age.getSelectedItem().toString());
        // int maxLimit = Math.min(50, Math.min(Integer.parseInt(spnrPolicyTerm.getSelectedItem().toString()), 70 - vage));

        int maxLimit = Math.min(57, Math.min(Integer.parseInt(spnrPolicyTerm.getSelectedItem().toString()), 75 - vage));
		int maxLimit_ACI = (Integer.parseInt(spnrPolicyTerm.getSelectedItem().toString()));
		StringBuilder error = new StringBuilder();

		if (cb_bi_smart_shield_criti_care_13_non_linked_rider.isChecked()
				&& Integer.parseInt(spnr_bi_smart_shield_criti_care_13_non_linked_rider_term.getSelectedItem().toString()) > maxLimit_ACI) {
			spnr_bi_smart_shield_criti_care_13_non_linked_rider_term.setSelection(0, false);
			error.append("\nPlease enter Policy Term of Criti Care 13 Non Linked Rider between 5 and ").append(maxLimit_ACI);
		}

		if (cbADBRider.isChecked()
				&& Integer.parseInt(spnr_AdbTerm.getSelectedItem().toString()) > maxLimit) {
			spnr_AdbTerm.setSelection(0, false);
			error.append("\nPlease enter Policy Term of Accidental Death Benefit Rider between 5 and ").append(maxLimit);
		}

		if (cbATPDBRider.isChecked()
				&& Integer
				.parseInt(spnr_AtpdbTerm.getSelectedItem().toString()) > maxLimit) {
			spnr_AtpdbTerm.setSelection(0, false);
			error.append("\nPlease enter Policy Term of Accidental Total and Permanent Disability Benefit Rider between 5 and ").append(maxLimit);
		}

		if (!error.toString().equals("")) {

			showAlert.setMessage(error.toString());
			showAlert.setNeutralButton("OK",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {

						}
					});
			showAlert.show();
		}
		if (error.toString().equals("")) {
			flagTermRider = true;
		}

		return flagTermRider;
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

	private int Datecomparison() {
		final Calendar c = Calendar.getInstance();
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH);
		mDay = c.get(Calendar.DAY_OF_MONTH);

		String mont = (mMonth + 1 < 10 ? "0" : "") + (mMonth + 1);
		String day = (mDay < 10 ? "0" : "") + mDay;

		String todaysDate = mont + "-" + day + "-" + mYear;

		return firstEmiDate.compareTo(todaysDate);
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
	// edt_proposerdetail_basicdetail_contact_no.setText(list_BasicDetail
	// .get(i).getMobileNo());
	// edt_proposerdetail_basicdetail_Email_id.setText(list_BasicDetail
	// .get(i).getEmailId());
	// edt_proposerdetail_basicdetail_ConfirmEmail_id
	// .setText(list_BasicDetail.get(i).getEmailId());
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

	/************************* validation ends here ********************************************/

	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		// TODO Auto-generated method stub
		if (v.getId() == edt_bi_smart_shield_life_assured_first_name.getId()) {
			setFocusable(edt_bi_smart_shield_life_assured_middle_name);
			edt_bi_smart_shield_life_assured_middle_name.requestFocus();
		}

		else if (v.getId() == edt_bi_smart_shield_life_assured_middle_name
				.getId()) {
			setFocusable(edt_bi_smart_shield_life_assured_last_name);
			edt_bi_smart_shield_life_assured_last_name.requestFocus();
		} else if (v.getId() == edt_bi_smart_shield_life_assured_last_name
				.getId()) {
			setFocusable(btn_bi_smart_shield_life_assured_date);
			btn_bi_smart_shield_life_assured_date.requestFocus();
		} else if (v.getId() == edt_proposerdetail_basicdetail_contact_no
				.getId()) {
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
			clearFocusable(spnrPlan);
			setFocusable(spnrPlan);
			spnrPlan.requestFocus();
		} else if (v.getId() == edtBasicSA.getId()) {

			commonMethods.hideKeyboard(edtBasicSA,context);

			int pos = spnrPlan.getSelectedItemPosition();
			if (pos == 0 || pos == 1) {
				clearFocusable(cb_bi_smart_shield_criti_care_13_non_linked_rider);
				setFocusable(cb_bi_smart_shield_criti_care_13_non_linked_rider);
				cb_bi_smart_shield_criti_care_13_non_linked_rider.requestFocus();
			}

		} else if (v.getId() == et_bi_smart_shield_criti_care_13_non_linked_rider_sum_assured.getId()) {
			clearFocusable(cbADBRider);
			setFocusable(cbADBRider);
			cbADBRider.requestFocus();
		} else if (v.getId() == edtAdbSA.getId()) {
			clearFocusable(cbATPDBRider);
			setFocusable(cbATPDBRider);
			cbATPDBRider.requestFocus();
		} else if (v.getId() == edtAtpdbSA.getId()) {
			commonMethods.hideKeyboard(edtAtpdbSA,context);
		}

		return true;
	}

	private void createPdf() {
		try {


			Font headerBold = new Font(Font.FontFamily.TIMES_ROMAN, 8,
					Font.BOLD);

			Font small_bold = new Font(Font.FontFamily.TIMES_ROMAN, 6,
					Font.BOLD);
			Font small_normal = new Font(Font.FontFamily.TIMES_ROMAN, 6,
					Font.NORMAL);

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
			image.scalePercent(50f);
			image.scaleToFit(80, 50);
			image.setAlignment(Element.ALIGN_LEFT);

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
			cell.setRowspan(4);
			cell.setBorder(Rectangle.NO_BORDER);
			table.addCell(cell);

			cell = new PdfPCell(image);
			cell.setRowspan(3);
			cell.setBorder(Rectangle.NO_BORDER);
			table.addCell(cell);

			cell = new PdfPCell(
					new Phrase(
                            "Registered & Corporate Office: 'Natraj', M.V. Road and Western Express, Highway Junction, Andheri (East),",
							headerBold));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setBorder(Rectangle.NO_BORDER);

			table.addCell(cell);

            cell = new PdfPCell(new Phrase("Mumbai  400069.IRDAI Registration No. 111",
					headerBold));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setBorder(Rectangle.NO_BORDER);

			table.addCell(cell);

			cell = new PdfPCell(new Phrase(
					"SBI Life - Smart Shield (UIN: 111N067V07)", headerBold));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setBorder(Rectangle.NO_BORDER);

			table.addCell(cell);

			cell = new PdfPCell(new Phrase("\n", normal));
			cell.setColspan(3);
			cell.setBorder(Rectangle.BOTTOM);
			cell.setBorderWidth(1.2f);
			table.addCell(cell);

            LineSeparator ls = new LineSeparator();
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
					QuatationNumber, normal1_bold));
			ProposalNumber_cell_2.setHorizontalAlignment(Element.ALIGN_CENTER);
			PdfPCell NameofProposal_cell_3 = new PdfPCell(new Paragraph(
					"Proposer Name ", small_normal));
			PdfPCell NameofProposal_cell_4 = new PdfPCell(new Paragraph(
					name_of_proposer, normal1_bold));
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

			// inputTable here -1

			PdfPTable personalDetail_table = new PdfPTable(2);
			personalDetail_table.setWidths(new float[] { 5f, 5f });
			personalDetail_table.setWidthPercentage(100f);
			personalDetail_table.setHorizontalAlignment(Element.ALIGN_CENTER);

			// 1 row
			cell = new PdfPCell(new Phrase(" Product ", normal1));
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			personalDetail_table.addCell(cell);

			cell = new PdfPCell(new Phrase(
                    "SBI Life - Smart Shield (UIN: 111N067V07)", normal1));
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			personalDetail_table.addCell(cell);

			// 2 row
			cell = new PdfPCell(new Phrase(" Plan", normal1));
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			personalDetail_table.addCell(cell);

			cell = new PdfPCell(new Phrase(" " + planProposedName, normal1));
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			personalDetail_table.addCell(cell);

			// 3 row
			cell = new PdfPCell(new Phrase(" Age", normal1));
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			personalDetail_table.addCell(cell);

			cell = new PdfPCell(new Phrase(" " + ageAtEntry, normal1));
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			personalDetail_table.addCell(cell);

			// 4 row
			cell = new PdfPCell(new Phrase(" Gender", normal1));
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			personalDetail_table.addCell(cell);

			cell = new PdfPCell(new Phrase("" + gender, normal1));
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			personalDetail_table.addCell(cell);

			// 5 row
			cell = new PdfPCell(new Phrase(" Smoker/Non Smoker", normal1));
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			personalDetail_table.addCell(cell);

			// Changes done by amit -24-3-2015
			if (smoker_or_not.equalsIgnoreCase("true")) {
				cell = new PdfPCell(new Phrase("Smoker", normal1));
				cell.setHorizontalAlignment(Element.ALIGN_LEFT);
				personalDetail_table.addCell(cell);
			} else {
				cell = new PdfPCell(new Phrase("Non Smoker", normal1));
				cell.setHorizontalAlignment(Element.ALIGN_LEFT);
				personalDetail_table.addCell(cell);
			}

			// cell = new PdfPCell(new Phrase(" " + smoker_or_not, normal1));
			// cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			// personalDetail_table.addCell(cell);

			// 6 row
			cell = new PdfPCell(new Phrase(" Frequency", normal1));
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			personalDetail_table.addCell(cell);

			cell = new PdfPCell(new Phrase(" " + premPayingMode, normal1));
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			personalDetail_table.addCell(cell);

			// 7 row
			if (CbJkResident.isChecked()) {
				cell = new PdfPCell(new Phrase("J&k", normal1));
				cell.setHorizontalAlignment(Element.ALIGN_LEFT);
				personalDetail_table.addCell(cell);

				if (isJkResident.equalsIgnoreCase("true")) {
					cell = new PdfPCell(new Phrase("Yes", normal1));
				}
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				personalDetail_table.addCell(cell);
			}
			// Basic Cover

			PdfPTable basicCover_table = new PdfPTable(4);
			basicCover_table.setWidths(new float[] { 15f, 5f, 5f, 5f });
			basicCover_table.setWidthPercentage(100f);
			basicCover_table.setHorizontalAlignment(Element.ALIGN_LEFT);

			// 1st row
			cell = new PdfPCell(new Phrase("", normal1));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			basicCover_table.addCell(cell);

			cell = new PdfPCell(new Phrase("Term (Years)", normal1));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			basicCover_table.addCell(cell);

			cell = new PdfPCell(new Phrase("Sum Assured (Rs.)", normal1));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			basicCover_table.addCell(cell);

			cell = new PdfPCell(new Phrase("Premium (Rs.)", normal1));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			basicCover_table.addCell(cell);

			// 2 row
			cell = new PdfPCell(new Phrase(" Basic Cover", normal1));
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			basicCover_table.addCell(cell);

			cell = new PdfPCell(new Phrase("" + policyTerm, normal1));
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			basicCover_table.addCell(cell);

			cell = new PdfPCell(new Phrase(currencyFormat.format(Double
					.parseDouble(obj.getStringWithout_E(Double
							.valueOf(basicCoverSumAssured)))), normal1));
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			basicCover_table.addCell(cell);

         //   cell = new PdfPCell(new Phrase(basicCoverYearlyPremium, normal1));

            cell = new PdfPCell(new Phrase(  (obj.getStringWithout_E(Double
                    .valueOf(((prsObj.parseXmlTag(output,
                            "basicPremWthOutDisc") == null) || (prsObj
                            .parseXmlTag(output, "basicPremWthOutDisc")
                            .equals(""))) ? "0" : prsObj
                            .parseXmlTag(output, "basicPremWthOutDisc")))), normal1));
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			basicCover_table.addCell(cell);

			if (aciRiderStatus.equalsIgnoreCase("true")) {

				// 2 row
				cell = new PdfPCell(new Phrase(
						" Criti Care 13 Non Linked Rider (UIN: 111B025V02) ",
						normal1));
				cell.setHorizontalAlignment(Element.ALIGN_LEFT);
				basicCover_table.addCell(cell);

				cell = new PdfPCell(new Phrase("" + aciTerm, normal1));
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				basicCover_table.addCell(cell);

				// changes done by amit - 25-3-2015
				cell = new PdfPCell(new Phrase(currencyFormat.format(Integer
						.parseInt(aciSA)), normal1));
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				basicCover_table.addCell(cell);

				cell = new PdfPCell(new Phrase(currencyFormat.format(Integer
						.parseInt(ptRiderYearly)), normal1));
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				basicCover_table.addCell(cell);

			}

			if (adbRiderStatus.equalsIgnoreCase("true")) {

				// 2 row
				cell = new PdfPCell(new Phrase(
                        " Accidental Death Benefit Rider (UIN: 111B015V03)",
						normal1));
				cell.setHorizontalAlignment(Element.ALIGN_LEFT);
				basicCover_table.addCell(cell);

				cell = new PdfPCell(new Phrase("" + adbRiderTerm, normal1));
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				basicCover_table.addCell(cell);

				// changes done by amit - 25-3-2015
				cell = new PdfPCell(new Phrase(currencyFormat.format(Integer
						.parseInt(adbSA)), normal1));
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				basicCover_table.addCell(cell);

				cell = new PdfPCell(new Phrase(currencyFormat.format(Integer
						.parseInt(adbRiderYearly)), normal1));
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				basicCover_table.addCell(cell);

			}

			if (atpdbRiderStatus.equalsIgnoreCase("true")) {

				// 2 row
				cell = new PdfPCell(
						new Phrase(
                                " Accidental Total & Permanent Disability Benefit Rider (UIN: 111B016V03)",
								normal1));
				cell.setHorizontalAlignment(Element.ALIGN_LEFT);
				basicCover_table.addCell(cell);

				cell = new PdfPCell(new Phrase("" + atpdbRiderTerm, normal1));
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				basicCover_table.addCell(cell);

				// changes done by amit - 25-3-2015
				cell = new PdfPCell(new Phrase(currencyFormat.format(Integer
						.parseInt(atpdbSA)), normal1));
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				basicCover_table.addCell(cell);

				cell = new PdfPCell(new Phrase(currencyFormat.format(Integer
						.parseInt(atpdbRiderYearly)), normal1));
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				basicCover_table.addCell(cell);

			}

			// Table here

			// PdfPTable FY_SY_premDetail_table = new PdfPTable(6);
			// FY_SY_premDetail_table.setWidths(new float[] { 5f, 5f, 5f, 5f,
			// 5f,
			// 5f });

			PdfPTable FY_SY_premDetail_table;
			if (cb_staffdisc.isChecked()) {

				FY_SY_premDetail_table = new PdfPTable(4);
				FY_SY_premDetail_table.setWidths(new float[] { 5f, 5f, 5f, 5f });
				FY_SY_premDetail_table.setWidthPercentage(100f);
				FY_SY_premDetail_table.setHorizontalAlignment(Element.ALIGN_LEFT);

				// 1st row
                cell = new PdfPCell(new Phrase("Total Premium for Base Product",
						small_bold));
				cell.setColspan(7);
				cell.setBorder(Rectangle.BOTTOM);
				cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                FY_SY_premDetail_table.addCell(cell);

				// 2 row
				cell = new PdfPCell(new Phrase(premPayingMode + " Installment Premium", small_normal));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				FY_SY_premDetail_table.addCell(cell);

				cell = new PdfPCell(new Phrase(premPayingMode + " Installment Premium with Staff Discount",
						small_normal));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				FY_SY_premDetail_table.addCell(cell);

				cell = new PdfPCell(new Phrase("Applicable Taxes", small_normal));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				FY_SY_premDetail_table.addCell(cell);



				cell = new PdfPCell(new Phrase(premPayingMode
						+ "Installment Premium with Applicable Taxes", small_normal));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				FY_SY_premDetail_table.addCell(cell);

				cell = new PdfPCell(new Phrase(currencyFormat.format(Double
						.parseDouble(prsObj.parseXmlTag(output, "InstmntPremWithoutDisc"))),
						small_normal));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				FY_SY_premDetail_table.addCell(cell);

				cell = new PdfPCell(new Phrase(currencyFormat.format(Double
						.parseDouble(prsObj.parseXmlTag(output, "InstmntPrem"))),
						small_normal));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				FY_SY_premDetail_table.addCell(cell);

				cell = new PdfPCell(new Phrase(getformatedThousandString(Integer.parseInt(obj.getRound(obj
						.getStringWithout_E(Double.valueOf(basicServiceTax
								.equals("") ? "0" : basicServiceTax))))),
						small_normal));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				FY_SY_premDetail_table.addCell(cell);

				cell = new PdfPCell(new Phrase(
						currencyFormat.format(Double.parseDouble(prsObj
								.parseXmlTag(output, "InstmntPremWthST"))),
						small_normal));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				FY_SY_premDetail_table.addCell(cell);
			} else {

				FY_SY_premDetail_table = new PdfPTable(3);
				FY_SY_premDetail_table.setWidths(new float[]{5f, 5f, 5f});
				FY_SY_premDetail_table.setWidthPercentage(100f);
				FY_SY_premDetail_table.setHorizontalAlignment(Element.ALIGN_LEFT);

				// 1st row
                cell = new PdfPCell(new Phrase("Total Premium for Base Product",
						small_bold));
				cell.setColspan(7);
				cell.setBorder(Rectangle.BOTTOM);
				cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                FY_SY_premDetail_table.addCell(cell);

				// 2 row

				cell = new PdfPCell(new Phrase(premPayingMode + "Installment Premium",
						small_normal));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				FY_SY_premDetail_table.addCell(cell);

				cell = new PdfPCell(new Phrase("Applicable Taxes", small_normal));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				FY_SY_premDetail_table.addCell(cell);

				// cell = new PdfPCell(new Phrase("(b)Swachh Bharat Cess(Rs.)",
				// small_normal));
				// cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				// FY_SY_premDetail_table.addCell(cell);
				//
				// cell = new PdfPCell(new Phrase("(c)Krishi Kalyan Cess(Rs.)",
				// small_normal));
				// cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				// FY_SY_premDetail_table.addCell(cell);

				cell = new PdfPCell(new Phrase(premPayingMode
                        + " Installment Premium with Applicable Taxes", small_normal));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				FY_SY_premDetail_table.addCell(cell);

				cell = new PdfPCell(new Phrase(currencyFormat.format(Double
						.parseDouble(prsObj.parseXmlTag(output, "InstmntPrem"))),
						small_normal));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				FY_SY_premDetail_table.addCell(cell);

				cell = new PdfPCell(new Phrase(getformatedThousandString(Integer.parseInt(obj.getRound(obj
						.getStringWithout_E(Double.valueOf(basicServiceTax
								.equals("") ? "0" : basicServiceTax))))),
						small_normal));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				FY_SY_premDetail_table.addCell(cell);

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

				cell = new PdfPCell(new Phrase(
						currencyFormat.format(Double.parseDouble(prsObj
								.parseXmlTag(output, "InstmntPremWthST"))), small_normal));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				FY_SY_premDetail_table.addCell(cell);

			}

			Paragraph disclaimers = new Paragraph("     Disclaimers :-",
					normal1_bold);
			disclaimers.setAlignment(Element.ALIGN_LEFT);

			PdfPTable table2 = new PdfPTable(2);
			table2.setWidths(new float[] { 0.3f, 9f });
			table2.setWidthPercentage(100);
			table2.setHorizontalAlignment(Element.ALIGN_LEFT);

			// 1st note
			cell = new PdfPCell(new Phrase("*", normal1));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setBorder(Rectangle.NO_BORDER);
			table2.addCell(cell);
			cell = new PdfPCell(
					new Phrase(
							"The premium calculation is after large sum assured rebates, wherever applicable. ",
							normal1));
			cell.setHorizontalAlignment(Element.ALIGN_LEFT
					| Element.ALIGN_JUSTIFIED);
			cell.setBorder(Rectangle.NO_BORDER);
			table2.addCell(cell);

			// 2 note
			cell = new PdfPCell(new Phrase("*", normal1));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setBorder(Rectangle.NO_BORDER);
			table2.addCell(cell);
			cell = new PdfPCell(
					new Phrase(
							"For more details on risk factors, terms and conditions please read sales brochure carefully before concluding a sale. ",
							normal1));
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell.setBorder(Rectangle.NO_BORDER);
			table2.addCell(cell);

			// 3 note
			cell = new PdfPCell(new Phrase("*", normal1));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setBorder(Rectangle.NO_BORDER);
			table2.addCell(cell);
			cell = new PdfPCell(
					new Phrase(
							"The exact premium can be determined only at the time of acceptance of risk cover on the life to be assured after taking into consideration any extras required to be imposed. ",
							normal1));
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell.setBorder(Rectangle.NO_BORDER);
			table2.addCell(cell);
			// 1st note
			cell = new PdfPCell(new Phrase("*", normal1));
			cell.setBorder(Rectangle.NO_BORDER);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table2.addCell(cell);
			cell = new PdfPCell(
					new Phrase(
							"The Premium calculation shown may change if there are changes in the levies like Applicable Taxes, education or any other cess or any other statutory levies required to be collected from the policyholder.",
							normal1));
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell.setBorder(Rectangle.NO_BORDER);
			table2.addCell(cell);
			// 1st note
        /*    cell = new PdfPCell(new Phrase("*", normal1));
			cell.setBorder(Rectangle.NO_BORDER);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table2.addCell(cell);
			cell = new PdfPCell(
					new Phrase(
							"Tax deduction under Section 80C is available. However in case the premium paid during the financial year, exceeds 10% of the sum assured, the benefit will be limited upto 10% of the sum assured.",
							normal1));
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell.setBorder(Rectangle.NO_BORDER);
            table2.addCell(cell);*/
			// 1st note
            /*cell = new PdfPCell(new Phrase("*", normal1));
			cell.setBorder(Rectangle.NO_BORDER);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table2.addCell(cell);
			cell = new PdfPCell(
					new Phrase(
							"Tax deduction under Section 80D is available for premium paid towards SBI Life- Criti Care 13 Non - Linked rider.",
							normal1));
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell.setBorder(Rectangle.NO_BORDER);
            table2.addCell(cell);*/

          /*  // 1st note
			cell = new PdfPCell(new Phrase("*", normal1));
			cell.setBorder(Rectangle.NO_BORDER);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table2.addCell(cell);
			cell = new PdfPCell(
					new Phrase(
							"Tax exemption under Section 10(10D) is available, subject to premium not exceeding 10% of the sum assured in any of the years during the term of the policy.",
							normal1));
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell.setBorder(Rectangle.NO_BORDER);
            table2.addCell(cell);*/

			// 1st note
			cell = new PdfPCell(new Phrase("*", normal1));
			cell.setBorder(Rectangle.NO_BORDER);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table2.addCell(cell);
			cell = new PdfPCell(
					new Phrase(
							"Tax benefits, are as per the Income Tax laws & are subject to change from time to time. Please consult your tax advisor for details.",
							normal1));
			cell.setHorizontalAlignment(Element.ALIGN_LEFT
					| Element.ALIGN_JUSTIFIED);
			cell.setBorder(Rectangle.NO_BORDER);
			table2.addCell(cell);

			cell = new PdfPCell(new Phrase("*", normal1));
			cell.setBorder(Rectangle.NO_BORDER);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table2.addCell(cell);
			cell = new PdfPCell(
					new Phrase(
							"Applicable Taxes (including surcharge/cess etc), at the rate notified by the Central Government/ State Government / Union Territories of India from time to time and as per the provisions of the prevalent tax laws will be payable on premium/ or any other charges as per the product features. ",
							normal1));
			cell.setHorizontalAlignment(Element.ALIGN_LEFT
					| Element.ALIGN_JUSTIFIED);
			cell.setBorder(Rectangle.NO_BORDER);
			table2.addCell(cell);


           /* cell = new PdfPCell(new Phrase("*", normal1));
			cell.setBorder(Rectangle.NO_BORDER);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table2.addCell(cell);
			cell = new PdfPCell(
					new Phrase(
                            "The above Premium quotation is subject to payment of stipulated premiums on due date.",
							normal1));
			cell.setHorizontalAlignment(Element.ALIGN_LEFT
					| Element.ALIGN_JUSTIFIED);
			cell.setBorder(Rectangle.NO_BORDER);
            table2.addCell(cell);*/

			Paragraph para7 = new Paragraph(
					"I,_____________________________________________, having received the information with respect to the above, have understood the above statement before entering into the contract.",
					normal1_bold);

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

			document.add(new_line);

			// document.add(main_table);
			document.add(table_proposer_name);
			document.add(new_line);
			document.add(personalDetail_table);
			document.add(new_line);
			document.add(basicCover_table);
			document.add(new_line);
			document.add(FY_SY_premDetail_table);

			document.add(new_line);
			document.add(disclaimers);
			document.add(table2);
			document.add(new_line);
			document.add(new_line);

			// document.add(para7);
			document.add(new_line);

			//Start 16 Jan 2018
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
            document.add(BI_Pdftable_CompanysPolicySurrender4);

			PdfPTable BI_Pdftable_CompanysPolicySurrender5 = new PdfPTable(1);
			BI_Pdftable_CompanysPolicySurrender5.setWidthPercentage(100);
			PdfPCell BI_Pdftable_CompanysPolicySurrender5_cell = new PdfPCell(
					new Paragraph(Company_policy_surrender_dec, small_normal));

			BI_Pdftable_CompanysPolicySurrender5_cell.setPadding(5);

			BI_Pdftable_CompanysPolicySurrender5
					.addCell(BI_Pdftable_CompanysPolicySurrender5_cell);
			document.add(BI_Pdftable_CompanysPolicySurrender5);
			//End 16 Jan 2018

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
			// document.add(table4);

			document.close();

			// return true;
		} catch (Exception e) {
			// TODO: handle exception
			// Log.e(getLocalClassName(), e.toString() +
			// "Error in creating Pdf");
			// System.out.println("error " + e.getMessage());
			e.printStackTrace();
			// Toast.makeText(getApplicationContext(), e.getMessage(),
			// Toast.LENGTH_SHORT);
			// return false;

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

	private boolean valCC13Rider() {

		int sum = Integer.parseInt(spnr_bi_smart_shield_life_assured_age
				.getSelectedItem().toString())
				+ Integer.parseInt(spnr_bi_smart_shield_criti_care_13_non_linked_rider_term.getSelectedItem().toString());
		String error = "";
		if (cb_bi_smart_shield_criti_care_13_non_linked_rider.isChecked()) {
			if (Integer.parseInt(spnr_bi_smart_shield_life_assured_age
					.getSelectedItem().toString()) > 55) {
				error = "You are not eligible for Criti Care-13, Maximum Life assured age for criti care-13 is 55 years.";
				cb_bi_smart_shield_criti_care_13_non_linked_rider.setChecked(false);
				spnr_bi_smart_shield_criti_care_13_non_linked_rider_term.setSelection(0);
				aciTerm = "";
				et_bi_smart_shield_criti_care_13_non_linked_rider_sum_assured.setText("");
				aciSA = "";

			} else if (sum > 64) {
				error = "Maturity age for Criti Care 13 non Linked Rider is 64 years.";
				aciTerm = "";
			}
			if (!error.equals("")) {
				showAlert.setMessage(error);
				showAlert.setNeutralButton("OK",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
												int which) {
								spnr_bi_smart_shield_criti_care_13_non_linked_rider_term.setSelection(0, false);
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

}
