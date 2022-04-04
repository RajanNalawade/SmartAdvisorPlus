package sbilife.com.pointofsale_bancaagency.saralshield;

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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
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
public class BI_SaralShieldActivity extends AppCompatActivity implements
		OnEditorActionListener {

	private NeedAnalysisBIService NABIObj;
	private NA_CBI_bean na_cbi_bean;
	private File needAnalysispath, newFile;
	private String agentcode, agentMobile, agentEmail, userType;
	private int needAnalysis_flag = 0;
	private String na_input = null;
	private String na_output = null;
	private String UIN_NO;
	// private Utility utilityObj;
	private DatabaseHelper dbHelper;
	private ArrayAdapter<String> genderAdapter;
	private String na_dob = "";
	private int flag = 0;

	private CheckBox cb_staffdisc, cb_bi_saral_shield_JKResident,
			cb_bi_saral_shield_adb_rider, cb_bi_saral_shield_atpdb_rider;

	private EditText edt_bi_saral_shield_life_assured_first_name,
			edt_bi_saral_shield_life_assured_middle_name,
			edt_bi_saral_shield_life_assured_last_name,
			edt_bi_saral_shield_life_assured_age, edt_saral_shield_contact_no,
			edt_saral_shield_Email_id, edt_saral_shield_ConfirmEmail_id,
			edt_bi_saral_shield_sum_assured_amount,
			edt_bi_saral_shield_adb_rider_sum_assured,
			edt_bi_saral_shield_atpbd_rider_sum_assured;

	// Loan
	private EditText edt_plandetail_loan_loan_account_no,
			edt_plandetail_loan_name_of_institute,
			edt_plandetail_loan_loan_category,
			edt_plandetail_loan_loan_protection_sum_assured,
			edt_plandetail_loan_loan_tenure;
	// edt_plandetail_loan_rate_of_interest;

	private Spinner spnr_bi_saral_shield_life_assured_title,
			spnr_bi_saral_shield_selGender, spnr_bi_saral_shield_plan,
			spnr_bi_saral_shield_policyterm,
			spnr_bi_saral_shield_premium_paying_mode,
			spnr_bi_saral_shield_loan_rate_of_interest,
			spnr_bi_saral_shield_adb_rider_term,
			spnr_bi_saral_shield_atpdb_rider_term;

    private TableRow tr_bi_saral_shield_rate_of_interest, tr_ADB_rider,/* tr_ATPDB_Rider2, tr_ADB_rider2, tr_bi_saral_shield_rate_of_interest1,*/
			tr_ATPDB_Rider;
	private LinearLayout ll_loan_protection;
	private Button btn_bi_saral_shield_life_assured_date,
			btn_plandetail_loan_1st_emi_date,
			btn_plandetail_loan_last_emi_date;
	private Button btnBack, btnSubmit;

	private Button btn_MarketingOfficalDate;
	private Button btn_PolicyholderDate;
	private ImageButton Ibtn_signatureofMarketing;
	private ImageButton Ibtn_signatureofPolicyHolders;

	private String lifeAssured_Title = "";
	private String lifeAssured_First_Name = "";
	private String lifeAssured_Middle_Name = "";
	private String lifeAssured_Last_Name = "";
	private String name_of_life_assured = "";
	private String lifeAssured_date_of_birth = "";
	private String lifeAssured_loan_rate_interest = "";// lifeAssuredAge
	// = "",

	private  boolean loandetails = false;
	private AlertDialog.Builder showAlert;
	private  final int DATE_DIALOG_ID = 1;
	private  int DIALOG_ID;
	private int mYear;
	private int mMonth;
	private int mDay;

	private String loanAccountNo = "", financingInstituteName = "",
			loanCategory = "", sumAssuredOutStanding = "",
			balanceLoanTenure = "", firstEmiDate = "", lastEmiDate = "";

	private String emailId = "", mobileNo = "", ConfirmEmailId = "",
			ProposerEmailId = "";
	private boolean validationFla1 = false;
	private boolean validationFlag2 = false;
	private boolean validationFlag3 = false;
	private SaralShieldBean saralShieldBean;
	private CommonForAllProd obj;
	private StringBuilder retVal;
	private StringBuilder inputVal;
	private DecimalFormat currencyFormat;
	private int basePremWithDisc_exclST = 0;
	private double premBasic = 0;
	private double premADB = 0;
	private double premATPDB = 0;

	private String proposer_Is_Same_As_Life_Assured = "Y";// proposer_Middle_Name

	// For Bi Dialog
	private ParseXML prsObj;
	private String name_of_proposer = "", name_of_person = "", place2 = "",
			date1 = "", date2 = "";
	private String agent_sign = "", proposer_sign = "";// ,
	private String flg_needAnalyis = "";// proposer_Backdating_BackDate = "",
	// To Store User Info and sync info

	// newDBHelper db;

	private File mypath;
	private boolean valPremiumError = false, valRiderPremiumError = false;

	private String QuatationNumber = "";
	// private String currentRecordId = "";
	private String productName = "";
	// private String sr_Code = "";

	private Dialog d;
	private final int SIGNATURE_ACTIVITY = 1;
	private String latestImage = "";

	// Retrieving value from database and storing
	private String output = "";
    private String input = "";


	private String premPayingMode = "";
	private String adbRiderTerm = "";
	private String atpdbRiderTerm = "";
	private String policyTerm = "";
	private String ageAtEntry = "";
	private String gender = "";
	private String sumAssured = "";
	private String adbSA = "";
	private String atpdbSA = "";

	private String adbRiderStatus = "";
	private String atpdbRiderStatus = "";

	private String basicCoverSumAssured = "";
	private String basicCoverYearlyPremium = "";
	private String planProposedName = "";
	private String totalPremiumYearlyInstallmentPremWithServiceTaxYearly = "";// basicCoverTerm

	/*** Added by Akshaya on 04-AUG-15 end ***/

	private int emiage_1st = 0;

	private LinearLayout ll_bi_saral_shield_main;
	private String basicServiceTax = "";

	/*** Added by Akshaya on 05-AUG-15 end ***/
	private String bankUserType = "",mode = "";

	/* parivartan changes */
	private String Check = "";
	private Context context;
	private CommonMethods commonMethods;
	private StorageUtils mStorageUtils;
	private ImageButton imageButtonSaralShieldProposerPhotograph;

	private LinearLayout linearlayoutThirdpartySignature,
			linearlayoutAppointeeSignature;
	private ImageButton Ibtn_signatureofThirdParty, Ibtn_signatureofAppointee;
	private String thirdPartySign = "", appointeeSign = "";
	private String product_Code, product_UIN, product_cateogory, product_type, Company_policy_surrender_dec ="";
	private Bitmap photoBitmap;
    String str_kerla_discount = "No";

	/* end */

	private CheckBox cb_kerladisc;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.bi_saralshieldmain);
		//getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);

		// utilityObj=new Utility();

		dbHelper = new DatabaseHelper(getApplicationContext());

		context = this;
		commonMethods = new CommonMethods();
		mStorageUtils = new StorageUtils();
		commonMethods.setApplicationToolbarMenu(this, getString(R.string.app_name));

		NABIObj = new NeedAnalysisBIService(this);
		prsObj = new ParseXML();
		Intent intent = getIntent();

		//Added Tushar Kadam Kerla Applicable Taxes 7-Jun-2019
		TableRow tablerowKerlaDiscount = findViewById(R.id.tablerowKerlaDiscount);
		cb_kerladisc =  findViewById(R.id.cb_kerladisc);
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

					productName = "Saral Shield";
					/* parivartan changes */
					ProductInfo prodInfoObj = new ProductInfo();
					product_Code = prodInfoObj.getProductCode(productName);
					product_UIN = prodInfoObj.getProductUIN(productName);
					product_cateogory = prodInfoObj
							.getProductCategory(productName);
					product_type = prodInfoObj.getProductType(productName);
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
						product_Code/* "47" */, agentcode, zero + "");

			}
		} else
			needAnalysis_flag = 0;
		// ProductHomePageActivity.path.setText("Benefit Illustrator");

		Date();
		UI_Declaration();
		prsObj = new ParseXML();
		setSpinner_Value();
		// setBIInputGui();

		edt_bi_saral_shield_life_assured_first_name
				.setOnEditorActionListener(this);
		edt_bi_saral_shield_life_assured_middle_name
				.setOnEditorActionListener(this);
		edt_bi_saral_shield_life_assured_last_name
				.setOnEditorActionListener(this);
		edt_bi_saral_shield_adb_rider_sum_assured
				.setOnEditorActionListener(this);
		edt_bi_saral_shield_atpbd_rider_sum_assured
				.setOnEditorActionListener(this);
		edt_bi_saral_shield_sum_assured_amount.setOnEditorActionListener(this);
		edt_saral_shield_contact_no.setOnEditorActionListener(this);
		edt_saral_shield_Email_id.setOnEditorActionListener(this);
		edt_saral_shield_ConfirmEmail_id.setOnEditorActionListener(this);
		edt_plandetail_loan_loan_account_no.setOnEditorActionListener(this);
		edt_plandetail_loan_name_of_institute.setOnEditorActionListener(this);
		edt_plandetail_loan_loan_category.setOnEditorActionListener(this);
		edt_plandetail_loan_loan_protection_sum_assured
				.setOnEditorActionListener(this);
		edt_plandetail_loan_loan_tenure.setOnEditorActionListener(this);

		setFocusable(spnr_bi_saral_shield_life_assured_title);
		spnr_bi_saral_shield_life_assured_title.requestFocus();

		validationOfMoile_EmailId();
		setSpinnerAndOtherListner();
		showAlert = new AlertDialog.Builder(this);
		obj = new CommonForAllProd();
		saralShieldBean = new SaralShieldBean();
		currencyFormat = new DecimalFormat("##,##,##,###");

		// String str_usertype = "";
		// TableRow tr_staff_disc;

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

		TableRow tr_staff_disc = findViewById(R.id.tr_saral_shield_staff_disc);
		try {
			userType = SimpleCrypto.decrypt("SBIL",
					dbHelper.GetUserType());

		} catch (Exception e) {
			e.printStackTrace();
		}
		/*if (userType.equalsIgnoreCase("BAP")
				|| userType.equalsIgnoreCase("CAG")) {
			tr_staff_disc.setVisibility(View.GONE);
		}*/

		if (needAnalysis_flag == 1 && !TextUtils.isEmpty(gender)) {
			spnr_bi_saral_shield_selGender.setSelection(genderAdapter
					.getPosition(gender));
			onClickLADob(btn_bi_saral_shield_life_assured_date);
		}

	}

	private void UI_Declaration() {

		cb_staffdisc = findViewById(R.id.cb_staffdisc);
		cb_bi_saral_shield_JKResident = findViewById(R.id.cb_bi_saral_shield_JKResident);
		cb_bi_saral_shield_adb_rider = findViewById(R.id.cb_bi_saral_shield_adb_rider);
		cb_bi_saral_shield_atpdb_rider = findViewById(R.id.cb_bi_saral_shield_atpdb_rider);

		edt_bi_saral_shield_life_assured_first_name = findViewById(R.id.edt_bi_saral_shield_life_assured_first_name);
		edt_bi_saral_shield_life_assured_middle_name = findViewById(R.id.edt_bi_saral_shield_life_assured_middle_name);
		edt_bi_saral_shield_life_assured_last_name = findViewById(R.id.edt_bi_saral_shield_life_assured_last_name);
		edt_bi_saral_shield_life_assured_age = findViewById(R.id.edt_bi_saral_shield_life_assured_age);
		edt_saral_shield_contact_no = findViewById(R.id.edt_saral_shield_contact_no);
		edt_saral_shield_Email_id = findViewById(R.id.edt_saral_shield_Email_id);
		edt_saral_shield_ConfirmEmail_id = findViewById(R.id.edt_saral_shield_ConfirmEmail_id);
		edt_bi_saral_shield_sum_assured_amount = findViewById(R.id.edt_bi_saral_shield_sum_assured_amount);
		edt_bi_saral_shield_adb_rider_sum_assured = findViewById(R.id.edt_bi_saral_shield_adb_rider_sum_assured);
		edt_bi_saral_shield_atpbd_rider_sum_assured = findViewById(R.id.edt_bi_saral_shield_atpbd_rider_sum_assured);

		// Loan
		edt_plandetail_loan_loan_account_no = findViewById(R.id.edt_plandetail_loan_loan_account_no);
		edt_plandetail_loan_name_of_institute = findViewById(R.id.edt_plandetail_loan_name_of_institute);
		edt_plandetail_loan_loan_category = findViewById(R.id.edt_plandetail_loan_loan_category);
		edt_plandetail_loan_loan_protection_sum_assured = findViewById(R.id.edt_plandetail_loan_loan_protection_sum_assured);
		edt_plandetail_loan_loan_tenure = findViewById(R.id.edt_plandetail_loan_loan_tenure);

		spnr_bi_saral_shield_life_assured_title = findViewById(R.id.spnr_bi_saral_shield_life_assured_title);
		spnr_bi_saral_shield_selGender = findViewById(R.id.spnr_bi_saral_shield_selGender);
//        spnr_bi_saral_shield_selGender.setClickable(false);
//        spnr_bi_saral_shield_selGender.setEnabled(false);

		spnr_bi_saral_shield_plan = findViewById(R.id.spnr_bi_saral_shield_plan);
		spnr_bi_saral_shield_policyterm = findViewById(R.id.spnr_bi_saral_shield_policyterm);
		spnr_bi_saral_shield_premium_paying_mode = findViewById(R.id.spnr_bi_saral_shield_premium_paying_mode);
		spnr_bi_saral_shield_loan_rate_of_interest = findViewById(R.id.spnr_bi_saral_shield_loan_rate_of_interest);
		spnr_bi_saral_shield_adb_rider_term = findViewById(R.id.spnr_bi_saral_shield_adb_rider_term);
		spnr_bi_saral_shield_atpdb_rider_term = findViewById(R.id.spnr_bi_saral_shield_atpdb_rider_term);

		tr_bi_saral_shield_rate_of_interest = findViewById(R.id.tr_bi_saral_shield_rate_of_interest);
//        tr_bi_saral_shield_rate_of_interest1 = (TableRow) findViewById(R.id.tr_bi_saral_shield_rate_of_interest1);
		tr_ADB_rider = findViewById(R.id.tr_bi_saral_shield_adb_rider);
//        tr_ADB_rider2 = (TableRow) findViewById(R.id.tr_bi_saral_shield_adb_rider2);
		tr_ATPDB_Rider = findViewById(R.id.tr_bi_saral_shield_atpd_rider);
//        tr_ATPDB_Rider2 = (TableRow) findViewById(R.id.tr_bi_saral_shield_atpd_rider2);

		ll_loan_protection = findViewById(R.id.ll_loan_protection);

		btn_bi_saral_shield_life_assured_date = findViewById(R.id.btn_bi_saral_shield_life_assured_date);
		btn_plandetail_loan_1st_emi_date = findViewById(R.id.btn_plandetail_loan_1st_emi_date);
		btn_plandetail_loan_last_emi_date = findViewById(R.id.btn_plandetail_loan_last_emi_date);

		btnBack = findViewById(R.id.btn_bi_saral_shield_btnback);
		btnSubmit = findViewById(R.id.btn_bi_saral_shield_btnSubmit);
		ll_bi_saral_shield_main = findViewById(R.id.ll_bi_saral_shield_main);

	}

	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		// TODO Auto-generated method stub
		if (v.getId() == edt_bi_saral_shield_life_assured_first_name.getId()) {
			setFocusable(edt_bi_saral_shield_life_assured_middle_name);
			edt_bi_saral_shield_life_assured_middle_name.requestFocus();
		}

		else if (v.getId() == edt_bi_saral_shield_life_assured_middle_name
				.getId()) {
			setFocusable(edt_bi_saral_shield_life_assured_last_name);
			edt_bi_saral_shield_life_assured_last_name.requestFocus();
		} else if (v.getId() == edt_bi_saral_shield_life_assured_last_name
				.getId()) {
			setFocusable(btn_bi_saral_shield_life_assured_date);
			btn_bi_saral_shield_life_assured_date.requestFocus();
		} else if (v.getId() == edt_saral_shield_contact_no.getId()) {
			// clearFocusable(premiumAmt);
			setFocusable(edt_saral_shield_Email_id);
			edt_saral_shield_Email_id.requestFocus();
		}

		else if (v.getId() == edt_saral_shield_Email_id.getId()) {
			// clearFocusable(premiumAmt);
			setFocusable(edt_saral_shield_ConfirmEmail_id);
			edt_saral_shield_ConfirmEmail_id.requestFocus();
		}

		else if (v.getId() == edt_saral_shield_ConfirmEmail_id.getId()) {
			// clearFocusable(premiumAmt);
			clearFocusable(spnr_bi_saral_shield_plan);
			setFocusable(spnr_bi_saral_shield_plan);
			spnr_bi_saral_shield_plan.requestFocus();
		} else if (v.getId() == edt_bi_saral_shield_sum_assured_amount.getId()) {

			// if(spnr_bi_saral_shield_plan.getSelectedItem().toString().equalsIgnoreCase("Decreasing Term Assurance[Loan Protection]")){
			// if (v.getId() == edt_bi_saral_shield_sum_assured_amount.getId())
			// {
			// setFocusable(edt_plandetail_loan_loan_account_no);
			// edt_plandetail_loan_loan_account_no.requestFocus();
			// }else if (v.getId() ==
			// edt_plandetail_loan_loan_account_no.getId()) {
			// setFocusable(edt_plandetail_loan_name_of_institute);
			// edt_plandetail_loan_name_of_institute.requestFocus();
			// }
			// else if (v.getId() ==
			// edt_plandetail_loan_name_of_institute.getId()) {
			// setFocusable(edt_plandetail_loan_loan_category);
			// edt_plandetail_loan_loan_category.requestFocus();
			// }
			// else if (v.getId() == edt_plandetail_loan_loan_category.getId())
			// {
			// // clearFocusable(premiumAmt);
			// setFocusable(edt_plandetail_loan_loan_protection_sum_assured);
			// edt_plandetail_loan_loan_protection_sum_assured.requestFocus();
			// }
			// else if (v.getId() ==
			// edt_plandetail_loan_loan_protection_sum_assured.getId()) {
			// // clearFocusable(premiumAmt);
			// setFocusable(edt_plandetail_loan_loan_tenure);
			// edt_plandetail_loan_loan_tenure.requestFocus();
			// }else if (v.getId() == edt_plandetail_loan_loan_tenure
			// .getId()) {
			// setFocusable(btn_plandetail_loan_1st_emi_date);
			// btn_plandetail_loan_1st_emi_date.requestFocus();
			// }
			// }
			commonMethods.hideKeyboard(edt_bi_saral_shield_sum_assured_amount,context);

		} else if (v.getId() == edt_bi_saral_shield_adb_rider_sum_assured
				.getId()) {
			clearFocusable(cb_bi_saral_shield_adb_rider);
			setFocusable(cb_bi_saral_shield_adb_rider);
			cb_bi_saral_shield_adb_rider.requestFocus();
		} else if (v.getId() == edt_bi_saral_shield_atpbd_rider_sum_assured
				.getId()) {
			clearFocusable(cb_bi_saral_shield_atpdb_rider);
			setFocusable(cb_bi_saral_shield_atpdb_rider);
			cb_bi_saral_shield_atpdb_rider.requestFocus();
		} else if (v.getId() == edt_bi_saral_shield_atpbd_rider_sum_assured
				.getId()) {
			commonMethods.hideKeyboard(edt_bi_saral_shield_atpbd_rider_sum_assured,context);
		}

		return true;
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		boolean flagFocus = true;
		if (!flagFocus) {
			ll_bi_saral_shield_main.requestFocus();
		} else {
			spnr_bi_saral_shield_life_assured_title.requestFocus();
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
	// edt_saral_shield_contact_no.setText(list_BasicDetail.get(i)
	// .getMobileNo());
	// edt_saral_shield_Email_id.setText(list_BasicDetail.get(i)
	// .getEmailId());
	// edt_saral_shield_ConfirmEmail_id.setText(list_BasicDetail.get(i)
	// .getEmailId());
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
	// spnr_bi_saral_shield_life_assured_title.setSelection(
	// getIndex(spnr_bi_saral_shield_life_assured_title,
	// lifeAssured_Title), false);
	// edt_bi_saral_shield_life_assured_first_name
	// .setText(lifeAssuredName[1]);
	// edt_bi_saral_shield_life_assured_middle_name
	// .setText(lifeAssuredName[2]);
	// edt_bi_saral_shield_life_assured_last_name
	// .setText(lifeAssuredName[3]);
	//
	// proposer_Is_Same_As_Life_Assured = data.get(i)
	// .getProposer_Same_As_Life_Assured();
	//
	// lifeAssured_date_of_birth = data.get(i)
	// .getLife_assured_date_of_birth();
	// btn_bi_saral_shield_life_assured_date
	// .setText(getDate(lifeAssured_date_of_birth));
	//
	// String input = data.get(i).getInput();
	//
	// ageAtEntry = prsObj.parseXmlTag(input, "age");
	//
	// edt_bi_saral_shield_life_assured_age.setText(ageAtEntry);
	//
	// gender = prsObj.parseXmlTag(input, "gender");
	// spnr_bi_saral_shield_selGender.setSelection(
	// getIndex(spnr_bi_saral_shield_selGender, gender), false);
	//
	// policyTerm = prsObj.parseXmlTag(input, "policyTerm");
	// spnr_bi_saral_shield_policyterm.setSelection(
	// getIndex(spnr_bi_saral_shield_policyterm, policyTerm),
	// false);
	//
	// premPayingMode = prsObj.parseXmlTag(input, "premFreq");
	// spnr_bi_saral_shield_premium_paying_mode.setSelection(
	// getIndex(spnr_bi_saral_shield_premium_paying_mode,
	// premPayingMode), false);
	//
	// isJkResident = prsObj.parseXmlTag(input, "isJKResident");
	// if (isJkResident.equalsIgnoreCase("true")) {
	// cb_bi_saral_shield_JKResident.setChecked(true);
	// }
	// else{
	// cb_bi_saral_shield_JKResident.setChecked(false);
	// }
	// // aciRiderStatus = prsObj.parseXmlTag(input, "isAccCIRider");
	// adbRiderStatus = prsObj.parseXmlTag(input, "isADBRider");
	// atpdbRiderStatus = prsObj.parseXmlTag(input, "isATPDBRider");
	// // ccnlRiderStatus = prsObj.parseXmlTag(input, "isCCNLRider");
	// planProposedName = prsObj.parseXmlTag(input, "plan");
	//
	// spnr_bi_saral_shield_plan.setSelection(
	// getIndex(spnr_bi_saral_shield_plan, planProposedName),
	// false);
	// // int pos = spnr_bi_saral_shield_plan.getSelectedItemPosition();
	//
	// sumAssured = ((int) Double.parseDouble(prsObj.parseXmlTag(input,
	// "sumAssured"))) + "";
	// edt_bi_saral_shield_sum_assured_amount.setText(sumAssured);
	//
	// if (adbRiderStatus.equalsIgnoreCase("true")) {
	// tr_ADB_rider.setVisibility(View.VISIBLE);
	// cb_bi_saral_shield_adb_rider.setChecked(true);
	// adbRiderTerm = prsObj.parseXmlTag(input, "adbTerm");
	// adbSA = ((int) Double.parseDouble(prsObj.parseXmlTag(input,
	// "adbSA"))) + "";
	// edt_bi_saral_shield_adb_rider_sum_assured.setText(adbSA);
	// spnr_bi_saral_shield_adb_rider_term.setSelection(
	// getIndex(spnr_bi_saral_shield_adb_rider_term,
	// adbRiderTerm), false);
	//
	// }
	//
	// if (atpdbRiderStatus.equalsIgnoreCase("true")) {
	// tr_ATPDB_Rider.setVisibility(View.VISIBLE);
	// cb_bi_saral_shield_atpdb_rider.setChecked(true);
	// atpdbRiderTerm = prsObj.parseXmlTag(input, "atpdbTerm");
	// atpdbSA = ((int) Double.parseDouble(prsObj.parseXmlTag(input,
	// "atpdbSA"))) + "";
	// edt_bi_saral_shield_atpbd_rider_sum_assured.setText(atpdbSA);
	// spnr_bi_saral_shield_atpdb_rider_term.setSelection(
	// getIndex(spnr_bi_saral_shield_atpdb_rider_term,
	// atpdbRiderTerm), false);
	//
	// }
	//
	// staffdiscount = prsObj.parseXmlTag(input, "isStaff");
	// if (staffdiscount.equalsIgnoreCase("true")) {
	// cb_staffdisc.setChecked(true);
	// } else {
	// cb_staffdisc.setChecked(false);
	// }
	// // smoker_or_not = prsObj.parseXmlTag(input, "isSmoker");
	// // if (smoker_or_not.equalsIgnoreCase("true")) {
	// // rb_saral_shield_proposer_smoker_yes.setChecked(true);
	// // Is_Smoker_or_Not = "Smoker";
	// // } else {
	// // rb_saral_shield_proposer_smoker_no.setChecked(true);
	// // Is_Smoker_or_Not = "Non Smoker";
	// // }
	//
	// proposer_Backdating_BackDate = data.get(i).getBackdate();
	//
	// if (planProposedName
	// .equalsIgnoreCase("Decreasing Term Assurance[Loan Protection]")) {
	// tr_bi_saral_shield_rate_of_interest.setVisibility(View.VISIBLE);
	// ll_loan_protection.setVisibility(View.VISIBLE);
	// tr_ADB_rider.setVisibility(View.GONE);
	// cb_bi_saral_shield_adb_rider.setChecked(false);
	// tr_ATPDB_Rider.setVisibility(View.GONE);
	// cb_bi_saral_shield_atpdb_rider.setChecked(false);
	// cb_bi_saral_shield_adb_rider.setClickable(false);
	// cb_bi_saral_shield_atpdb_rider.setClickable(false);
	//
	// strloanIntrate = prsObj.parseXmlTag(input, "nbd_loan_INT");
	//
	// loanAccountNo = prsObj.parseXmlTag(input, "nbd_loanAccNo");
	// financingInstituteName = prsObj.parseXmlTag(input,
	// "nbd_loanFinanceInst");
	// loanCategory = prsObj.parseXmlTag(input, "nbd_loanCatgy");
	// sumAssuredOutStanding = prsObj.parseXmlTag(input,
	// "nbd_loanSumAssOutStanding");
	// balanceLoanTenure = prsObj.parseXmlTag(input,
	// "nbd_loanBalTenure");
	// firstEmiDate = prsObj.parseXmlTag(input, "nbd_loanFstEmiDate");
	// lastEmiDate = prsObj.parseXmlTag(input, "nbd_loanLstEmiDate");
	//
	// spnr_bi_saral_shield_loan_rate_of_interest.setSelection(
	// getIndex(spnr_bi_saral_shield_loan_rate_of_interest,
	// strloanIntrate), false);
	//
	// edt_plandetail_loan_loan_account_no.setText(loanAccountNo);
	// edt_plandetail_loan_name_of_institute
	// .setText(financingInstituteName);
	// edt_plandetail_loan_loan_category.setText(loanCategory);
	// edt_plandetail_loan_loan_protection_sum_assured
	// .setText(sumAssuredOutStanding);
	// edt_plandetail_loan_loan_tenure.setText(balanceLoanTenure);
	// btn_plandetail_loan_1st_emi_date.setText(firstEmiDate);
	// btn_plandetail_loan_last_emi_date.setText(lastEmiDate);
	//
	// } else if (planProposedName
	// .equalsIgnoreCase("Decreasing Term Assurance[Family Income Protection]")){
	// tr_bi_saral_shield_rate_of_interest.setVisibility(View.GONE);
	// ll_loan_protection.setVisibility(View.GONE);
	// tr_ADB_rider.setVisibility(View.GONE);
	// cb_bi_saral_shield_adb_rider.setChecked(false);
	// cb_bi_saral_shield_adb_rider.setClickable(false);
	// tr_ATPDB_Rider.setVisibility(View.GONE);
	// cb_bi_saral_shield_atpdb_rider.setChecked(false);
	// cb_bi_saral_shield_adb_rider.setClickable(false);
	// cb_bi_saral_shield_atpdb_rider.setClickable(false);
	// }
	// else {
	// tr_bi_saral_shield_rate_of_interest.setVisibility(View.GONE);
	// ll_loan_protection.setVisibility(View.GONE);
	// cb_bi_saral_shield_adb_rider.setClickable(true);
	// cb_bi_saral_shield_atpdb_rider.setClickable(true);
	//
	// }
	//
	// }
	//
	// }

	private void loan_details() {

		loanAccountNo = edt_plandetail_loan_loan_account_no.getText()
				.toString();
		financingInstituteName = edt_plandetail_loan_name_of_institute
				.getText().toString();
		loanCategory = edt_plandetail_loan_loan_category.getText().toString();
		sumAssuredOutStanding = edt_plandetail_loan_loan_protection_sum_assured
				.getText().toString();
		balanceLoanTenure = edt_plandetail_loan_loan_tenure.getText()
				.toString();
		lifeAssured_loan_rate_interest = spnr_bi_saral_shield_loan_rate_of_interest
				.getSelectedItem().toString();

	}

	private void setSpinner_Value() {

		// Gender
        String[] genderList = {"Male", "Female", "Third Gender"};
		genderAdapter = new ArrayAdapter<String>(getApplicationContext(),
				R.layout.spinner_item, genderList);
		genderAdapter.setDropDownViewResource(R.layout.spinner_item1);
		spnr_bi_saral_shield_selGender.setAdapter(genderAdapter);
		genderAdapter.notifyDataSetChanged();

		commonMethods.fillSpinnerValue(context, spnr_bi_saral_shield_life_assured_title,
				Arrays.asList(getResources().getStringArray(R.array.arr_select_title)));

		// Plan

		String[] planList = { "Level Term Assurance",
				"Decreasing Term Assurance[Loan Protection]",
				"Decreasing Term Assurance[Family Income Protection]" };
		ArrayAdapter<String> planAdapter = new ArrayAdapter<String>(
				getApplicationContext(), R.layout.spinner_item, planList);
		planAdapter.setDropDownViewResource(R.layout.spinner_item1);
		spnr_bi_saral_shield_plan.setAdapter(planAdapter);
		planAdapter.notifyDataSetChanged();
//        } else {
//            String[] planList = {"Level Term Assurance"};
//            ArrayAdapter<String> planAdapter = new ArrayAdapter<String>(
//                    getApplicationContext(), R.layout.spinner_item, planList);
//            planAdapter.setDropDownViewResource(R.layout.spinner_item1);
//            spnr_bi_saral_shield_plan.setAdapter(planAdapter);
//            planAdapter.notifyDataSetChanged();
//        }

		// Policy Term
		String[] policyTermList = new String[26];
		for (int i = 5; i <= 30; i++) {
			policyTermList[i - 5] = i + "";
		}
		ArrayAdapter<String> policyTermAdapter = new ArrayAdapter<String>(
				getApplicationContext(), R.layout.spinner_item, policyTermList);
		policyTermAdapter.setDropDownViewResource(R.layout.spinner_item1);
		spnr_bi_saral_shield_policyterm.setAdapter(policyTermAdapter);
		policyTermAdapter.notifyDataSetChanged();

		// Loan Rate of interest
		String[] lriList = { "6%", "8%", "10%", "12%", "14%", "16%", "18%",
				"20%" };
		ArrayAdapter<String> lriAdapter = new ArrayAdapter<String>(
				getApplicationContext(), R.layout.spinner_item, lriList);
		lriAdapter.setDropDownViewResource(R.layout.spinner_item1);
		spnr_bi_saral_shield_loan_rate_of_interest.setAdapter(lriAdapter);
		lriAdapter.notifyDataSetChanged();

		// Premium Frequency
		String[] premFreqList = { "Yearly", "Half Yearly", "Quarterly",
				"Monthly", "Single" };
		ArrayAdapter<String> premFreqAdapter = new ArrayAdapter<String>(
				getApplicationContext(), R.layout.spinner_item, premFreqList);
		premFreqAdapter.setDropDownViewResource(R.layout.spinner_item1);
		spnr_bi_saral_shield_premium_paying_mode.setAdapter(premFreqAdapter);
		premFreqAdapter.notifyDataSetChanged();

		// adb Term
		String[] adbTermList = new String[26];
		for (int i = 5; i <= 30; i++) {
			adbTermList[i - 5] = i + "";
		}
		ArrayAdapter<String> adbTermAdapter = new ArrayAdapter<String>(
				getApplicationContext(), R.layout.spinner_item, adbTermList);
		adbTermAdapter.setDropDownViewResource(R.layout.spinner_item1);
		spnr_bi_saral_shield_adb_rider_term.setAdapter(adbTermAdapter);
		adbTermAdapter.notifyDataSetChanged();

		// atpdb Term
		String[] atpdbTermList = new String[26];
		for (int i = 5; i <= 30; i++) {
			atpdbTermList[i - 5] = i + "";
		}
		ArrayAdapter<String> atpdbTermAdapter = new ArrayAdapter<String>(
				getApplicationContext(), R.layout.spinner_item, atpdbTermList);
		atpdbTermAdapter.setDropDownViewResource(R.layout.spinner_item1);
		spnr_bi_saral_shield_atpdb_rider_term.setAdapter(atpdbTermAdapter);
		atpdbTermAdapter.notifyDataSetChanged();

	}

	private void setSpinnerAndOtherListner() {

        cb_kerladisc.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cb_kerladisc.isChecked()) {
                    str_kerla_discount = "Yes";
                    clearFocusable(cb_kerladisc);
                    setFocusable(spnr_bi_saral_shield_life_assured_title);
                    spnr_bi_saral_shield_life_assured_title.requestFocus();
                } else {
                    str_kerla_discount = "No";
                    clearFocusable(cb_kerladisc);
                    setFocusable(spnr_bi_saral_shield_life_assured_title);
                    spnr_bi_saral_shield_life_assured_title.requestFocus();
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
				clearFocusable(spnr_bi_saral_shield_life_assured_title);
				setFocusable(spnr_bi_saral_shield_life_assured_title);
				spnr_bi_saral_shield_life_assured_title.requestFocus();
			}
		});

		cb_bi_saral_shield_JKResident.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (cb_bi_saral_shield_JKResident.isChecked()) {
					cb_bi_saral_shield_JKResident.setChecked(true);
				} else {
					cb_bi_saral_shield_JKResident.setChecked(false);
				}
			}
		});

		// Spinner
		spnr_bi_saral_shield_life_assured_title
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					public void onItemSelected(AdapterView<?> arg0, View arg1,
											   int position, long id) {
						// TODO Auto-generated method stub
						if (position == 0){
							lifeAssured_Title = "";
						}else {
							lifeAssured_Title = spnr_bi_saral_shield_life_assured_title
									.getSelectedItem().toString();
							/*if (lifeAssured_Title.equalsIgnoreCase("Mr.")) {
								gender="Male";
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
							clearFocusable(spnr_bi_saral_shield_life_assured_title);
							setFocusable(edt_bi_saral_shield_life_assured_first_name);
							edt_bi_saral_shield_life_assured_first_name
									.requestFocus();
						}
					}

					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub

					}
				});

		spnr_bi_saral_shield_plan
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					public void onItemSelected(AdapterView<?> arg0, View arg1,
											   int position, long id) {
						// TODO Auto-generated method stub
						if (position == 0) {
							spnr_bi_saral_shield_premium_paying_mode
									.setEnabled(true);

							tr_bi_saral_shield_rate_of_interest
									.setVisibility(View.GONE);
//                            tr_bi_saral_shield_rate_of_interest1
//                                    .setVisibility(View.GONE);
							cb_bi_saral_shield_adb_rider.setChecked(false);
							cb_bi_saral_shield_atpdb_rider.setChecked(false);
							cb_bi_saral_shield_adb_rider.setClickable(true);
							cb_bi_saral_shield_atpdb_rider.setClickable(true);

							tr_ADB_rider.setVisibility(View.GONE);
//                            tr_ADB_rider2.setVisibility(View.GONE);
							tr_ATPDB_Rider.setVisibility(View.GONE);
//                            tr_ATPDB_Rider2.setVisibility(View.GONE);

							spnr_bi_saral_shield_premium_paying_mode
									.setEnabled(true);
							// spnr_bi_saral_shield_premium_paying_mode
							// .setSelection(4, false);

							tr_bi_saral_shield_rate_of_interest
									.setVisibility(View.GONE);
//                            tr_bi_saral_shield_rate_of_interest1
//                                    .setVisibility(View.GONE);
							ll_loan_protection.setVisibility(View.GONE);
							loandetails = false;
						} else if (position == 1) {
							tr_bi_saral_shield_rate_of_interest
									.setVisibility(View.VISIBLE);
//                            tr_bi_saral_shield_rate_of_interest1
//                                    .setVisibility(View.VISIBLE);
							ll_loan_protection.setVisibility(View.VISIBLE);
							loandetails = true;
							spnr_bi_saral_shield_premium_paying_mode
									.setSelection(4, false);
							spnr_bi_saral_shield_premium_paying_mode
									.setEnabled(false);
							cb_bi_saral_shield_adb_rider.setChecked(false);
							cb_bi_saral_shield_atpdb_rider.setChecked(false);
							cb_bi_saral_shield_adb_rider.setClickable(false);
							cb_bi_saral_shield_atpdb_rider.setClickable(false);
							tr_ADB_rider.setVisibility(View.GONE);
//                            tr_ADB_rider2.setVisibility(View.GONE);
							tr_ATPDB_Rider.setVisibility(View.GONE);
//                            tr_ATPDB_Rider2.setVisibility(View.GONE);

						} else {
							cb_bi_saral_shield_adb_rider.setChecked(false);
							cb_bi_saral_shield_atpdb_rider.setChecked(false);
							cb_bi_saral_shield_adb_rider.setClickable(false);
							cb_bi_saral_shield_atpdb_rider.setClickable(false);
							tr_ADB_rider.setVisibility(View.GONE);
//                            tr_ADB_rider2.setVisibility(View.GONE);
							tr_ATPDB_Rider.setVisibility(View.GONE);
//                            tr_ATPDB_Rider2.setVisibility(View.GONE);

							spnr_bi_saral_shield_premium_paying_mode
									.setEnabled(false);
							spnr_bi_saral_shield_premium_paying_mode
									.setSelection(4, false);

							tr_bi_saral_shield_rate_of_interest
									.setVisibility(View.GONE);
//                            tr_bi_saral_shield_rate_of_interest1
//                                    .setVisibility(View.GONE);
							ll_loan_protection.setVisibility(View.GONE);
							loandetails = false;
						}

						clearFocusable(spnr_bi_saral_shield_plan);
						// clearFocusable(spnr_bi_saral_shield_policyterm);
						setFocusable(spnr_bi_saral_shield_policyterm);
						spnr_bi_saral_shield_policyterm.requestFocus();
					}

					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub

					}
				});

		spnr_bi_saral_shield_policyterm
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					public void onItemSelected(AdapterView<?> arg0, View arg1,
											   int pos, long id) {
						// int position = spnr_bi_saral_shield_plan
						// .getSelectedItemPosition();
						// if (position == 0 || position == 2) {
						//
						// clearFocusable(spnr_bi_saral_shield_premium_paying_mode);
						// } else {
						// clearFocusable(spnr_bi_saral_shield_policyterm);
						// setFocusable(spnr_bi_saral_shield_loan_rate_of_interest);
						// spnr_bi_saral_shield_loan_rate_of_interest
						// .requestFocus();
						// }

					}

					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub

					}
				});

		spnr_bi_saral_shield_premium_paying_mode
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					public void onItemSelected(AdapterView<?> arg0, View arg1,
											   int pos, long id) {
						int position = spnr_bi_saral_shield_plan
								.getSelectedItemPosition();
						clearFocusable(spnr_bi_saral_shield_premium_paying_mode);
						if (position == 1) {
							clearFocusable(spnr_bi_saral_shield_premium_paying_mode);
							clearFocusable(spnr_bi_saral_shield_loan_rate_of_interest);
							setFocusable(spnr_bi_saral_shield_loan_rate_of_interest);
							spnr_bi_saral_shield_loan_rate_of_interest
									.requestFocus();
						} else if (edt_bi_saral_shield_life_assured_first_name
								.getText().toString().equals("")) {
							clearFocusable(spnr_bi_saral_shield_premium_paying_mode);
							setFocusable(spnr_bi_saral_shield_life_assured_title);
							spnr_bi_saral_shield_life_assured_title
									.requestFocus();
						} else {
							clearFocusable(spnr_bi_saral_shield_premium_paying_mode);
							setFocusable(edt_bi_saral_shield_sum_assured_amount);
							edt_bi_saral_shield_sum_assured_amount
									.requestFocus();
						}

					}

					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub

					}
				});

		spnr_bi_saral_shield_loan_rate_of_interest
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					public void onItemSelected(AdapterView<?> adapter,
											   View arg1, int position, long id) {
						// TODO Auto-generated method stub
						adapter.getItemAtPosition(position).toString();

						clearFocusable(spnr_bi_saral_shield_loan_rate_of_interest);
						setFocusable(edt_bi_saral_shield_sum_assured_amount);
						edt_bi_saral_shield_sum_assured_amount.requestFocus();
					}

					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub

					}
				});

		// ADB Term
		spnr_bi_saral_shield_adb_rider_term
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					public void onItemSelected(AdapterView<?> arg0, View arg1,
											   int pos, long id) {
						if (valTermRider()) {
							clearFocusable(edt_bi_saral_shield_adb_rider_sum_assured);
							setFocusable(edt_bi_saral_shield_adb_rider_sum_assured);
							edt_bi_saral_shield_adb_rider_sum_assured
									.requestFocus();
						}
					}

					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub

					}
				});

		// ATPDB Term
		spnr_bi_saral_shield_atpdb_rider_term
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					public void onItemSelected(AdapterView<?> arg0, View arg1,
											   int pos, long id) {
						if (valTermRider()) {
							clearFocusable(spnr_bi_saral_shield_atpdb_rider_term);
							setFocusable(edt_bi_saral_shield_atpbd_rider_sum_assured);
							edt_bi_saral_shield_atpbd_rider_sum_assured
									.requestFocus();
						}
					}

					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub

					}
				});

		// ADB Rider
		cb_bi_saral_shield_adb_rider
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					public void onCheckedChanged(CompoundButton buttonView,
												 boolean isChecked) {
						if (isChecked) {
							tr_ADB_rider.setVisibility(View.VISIBLE);
//                            tr_ADB_rider2.setVisibility(View.VISIBLE);
							clearFocusable(cb_bi_saral_shield_adb_rider);
							clearFocusable(spnr_bi_saral_shield_adb_rider_term);
							setFocusable(spnr_bi_saral_shield_adb_rider_term);
							spnr_bi_saral_shield_adb_rider_term.requestFocus();
						} else {

							tr_ADB_rider.setVisibility(View.GONE);
//                            tr_ADB_rider2.setVisibility(View.GONE);
						}
					}
				});

		// ATPDB Rider
		cb_bi_saral_shield_atpdb_rider
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					public void onCheckedChanged(CompoundButton buttonView,
												 boolean isChecked) {
						if (isChecked) {
							tr_ATPDB_Rider.setVisibility(View.VISIBLE);
//                            tr_ATPDB_Rider2.setVisibility(View.VISIBLE);
							clearFocusable(cb_bi_saral_shield_atpdb_rider);
							clearFocusable(spnr_bi_saral_shield_atpdb_rider_term);
							setFocusable(spnr_bi_saral_shield_atpdb_rider_term);
							spnr_bi_saral_shield_atpdb_rider_term
									.requestFocus();
						} else {
							tr_ATPDB_Rider.setVisibility(View.GONE);
//                            tr_ATPDB_Rider2.setVisibility(View.GONE);
						}
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

                gender = spnr_bi_saral_shield_selGender
                        .getSelectedItem().toString();

				inputVal = new StringBuilder();
				retVal = new StringBuilder();

				lifeAssured_First_Name = edt_bi_saral_shield_life_assured_first_name
						.getText().toString();
				lifeAssured_Middle_Name = edt_bi_saral_shield_life_assured_middle_name
						.getText().toString();
				lifeAssured_Last_Name = edt_bi_saral_shield_life_assured_last_name
						.getText().toString();

				name_of_life_assured = lifeAssured_Title + " "
						+ lifeAssured_First_Name + " "
						+ lifeAssured_Middle_Name + " " + lifeAssured_Last_Name;

				mobileNo = edt_saral_shield_contact_no.getText().toString();
				emailId = edt_saral_shield_Email_id.getText().toString();
				ConfirmEmailId = edt_saral_shield_ConfirmEmail_id.getText()
						.toString();
				loan_details();
				if (loandetails) {

					if (valLoanDetails()) {
						if (valLifeAssuredProposerDetail() && valDob()
								&& valBasicDetail() && valSA() && valTerm()
								&& valTermRider()) {

							if (proposer_Is_Same_As_Life_Assured
									.equalsIgnoreCase("y")) {
								name_of_proposer = "";
							}

							System.out.println("Output:" + output);
							addListenerOnSubmit();
							System.out.println("Output:" + output);
							if (valPremiumError && valRiderPremiumError) {
								getInput(saralShieldBean);
								// insertDataIntoDatabase();

								if (needAnalysis_flag == 0) {
									Intent i = new Intent(
											BI_SaralShieldActivity.this,
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
													+ currencyFormat.format(Double.parseDouble(prsObj.parseXmlTag(
													retVal.toString(),
													"InstmntPrem"))));
									if (cb_staffdisc.isChecked())
										i.putExtra(
												"op2",
												"Installment Premium with SBG Discount is Rs. "
														+ currencyFormat.format(Double
														.parseDouble(prsObj
																.parseXmlTag(
																		retVal.toString(),
																		"InstmntPremWithDisc"))));

									i.putExtra(
											"op3",
											"Installment Premium Inclusive Applicable Taxes is Rs. "
													+ currencyFormat.format(Double.parseDouble(prsObj.parseXmlTag(
													retVal.toString(),
													"InstmntPremWthST"))));

									if (cb_bi_saral_shield_adb_rider
											.isChecked()) {
										i.putExtra(
												"op5",
												"Accidental Death Benefit Rider Premium is Rs. "
														+ currencyFormat.format(Double
														.parseDouble(prsObj
																.parseXmlTag(
																		retVal.toString(),
																		"ADBPrem"))));
									}

									if (cb_bi_saral_shield_atpdb_rider
											.isChecked()) {
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
							&& valBasicDetail() && valSA() && valTerm()
							&& valTermRider()) {

						if (proposer_Is_Same_As_Life_Assured
								.equalsIgnoreCase("y")) {
							name_of_proposer = "";
						}

						addListenerOnSubmit();
						if (valPremiumError && valRiderPremiumError) {
							getInput(saralShieldBean);
							// insertDataIntoDatabase();
							if (needAnalysis_flag == 0) {
								Intent i = new Intent(
										BI_SaralShieldActivity.this,
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
												+ currencyFormat.format(Double.parseDouble(prsObj
												.parseXmlTag(retVal
																.toString(),
														"InstmntPrem"))));
								if (cb_staffdisc.isChecked())
									i.putExtra(
											"op2",
											"Installment Premium with SBG Discount is Rs. "
													+ currencyFormat.format(Double.parseDouble(prsObj.parseXmlTag(
													retVal.toString(),
													"InstmntPremWithDisc"))));

								i.putExtra(
										"op3",
										"Installment Premium Inclusive Applicable Taxes is Rs. "
												+ currencyFormat.format(Double.parseDouble(prsObj.parseXmlTag(
												retVal.toString(),
												"InstmntPremWthST"))));

								if (cb_bi_saral_shield_adb_rider.isChecked()) {
									i.putExtra(
											"op5",
											"Accidental Death Benefit Rider Premium is Rs. "
													+ currencyFormat.format(Double.parseDouble(prsObj.parseXmlTag(
													retVal.toString(),
													"ADBPrem"))));
								}

								if (cb_bi_saral_shield_atpdb_rider.isChecked()) {
									i.putExtra(
											"op6",
											"Accidental Total & Permanent Disability Benefit Rider Premium is Rs. "
													+ currencyFormat.format(Double.parseDouble(prsObj.parseXmlTag(
													retVal.toString(),
													"ATPDPrem"))));
								}

								startActivity(i);
							} else
								Dialog();
						}

					}
				}
			}
		});

	}

	private void addListenerOnSubmit() {

		saralShieldBean = new SaralShieldBean();

		if (cb_staffdisc.isChecked()) {
			saralShieldBean.setStaffDisc(true);
		} else {
			saralShieldBean.setStaffDisc(false);
		}

		if(cb_kerladisc.isChecked())
		{
			saralShieldBean.setKerlaDisc(true);
		}
		else
		{
			saralShieldBean.setKerlaDisc(false);
		}

		saralShieldBean
				.setJKResident(cb_bi_saral_shield_JKResident.isChecked());

		if (cb_bi_saral_shield_adb_rider.isChecked()) {
			saralShieldBean.setADB_SA(Integer
					.parseInt(edt_bi_saral_shield_adb_rider_sum_assured
							.getText().toString()));
		} else {
			saralShieldBean.setADB_SA(0);
		}

		if (cb_bi_saral_shield_atpdb_rider.isChecked()) {
			saralShieldBean.setATPDB_SA(Integer
					.parseInt(edt_bi_saral_shield_atpbd_rider_sum_assured
							.getText().toString()));
		} else {
			saralShieldBean.setATPDB_SA(0);
		}

		saralShieldBean.setADB_Term(Integer
				.parseInt(spnr_bi_saral_shield_adb_rider_term.getSelectedItem()
						.toString()));
		saralShieldBean.setADB_Status(cb_bi_saral_shield_adb_rider.isChecked());

		saralShieldBean.setATPDB_Term(Integer
				.parseInt(spnr_bi_saral_shield_atpdb_rider_term
						.getSelectedItem().toString()));
		saralShieldBean.setATPDB_Status(cb_bi_saral_shield_atpdb_rider
				.isChecked());

		saralShieldBean.setAge(Integer
				.parseInt(edt_bi_saral_shield_life_assured_age.getText()
						.toString()));

		saralShieldBean.setBasicSA(Integer
				.parseInt(edt_bi_saral_shield_sum_assured_amount.getText()
						.toString()));
		saralShieldBean.setBasicTerm(Integer
				.parseInt(spnr_bi_saral_shield_policyterm.getSelectedItem()
						.toString()));
		saralShieldBean.setGender(spnr_bi_saral_shield_selGender
				.getSelectedItem().toString());
		saralShieldBean.setPlanName(spnr_bi_saral_shield_plan.getSelectedItem()
				.toString());
		saralShieldBean.setPremFreq(spnr_bi_saral_shield_premium_paying_mode
				.getSelectedItem().toString());
		saralShieldBean.setLRI(lifeAssured_loan_rate_interest);
		// premiumFreq =
		// spnr_bi_saral_shield_premium_paying_mode.getSelectedItem().toString();
		// saralShieldBean.setType(Is_Smoker_or_Not);

		saralShieldBean.setLoanAccountNumber(loanAccountNo);
		saralShieldBean.setLoanFinancialInstitue(financingInstituteName);
		saralShieldBean.setLoanCategory(loanCategory);
		saralShieldBean.setLoanSumAssuredOutstanding(sumAssuredOutStanding);
		saralShieldBean.setLoanBalanceLoanTenure(balanceLoanTenure);
		saralShieldBean.setLoanFirstEmiDate(firstEmiDate);
		saralShieldBean.setLoanLastEmiDate(lastEmiDate);

		showsaralShieldOutputPg(saralShieldBean);
	}

	private void showsaralShieldOutputPg(SaralShieldBean saralShieldBean) {
		retVal = new StringBuilder();

		// Intent Coding
		Intent i = new Intent(getApplicationContext(), success.class);
		TextView op = new TextView(this);
		TextView op1 = new TextView(this);
		TextView op2 = new TextView(this);
		TextView op3 = new TextView(this);
		TextView op5 = new TextView(this);
		TextView op6 = new TextView(this);

		/*** modified by Akshaya on 20-MAY-16 start **/

		String totalInstPremiumWithST = "", serviceTax = "";
		double basicServiceTax, SBCServiceTax = 0, KKCServiceTax = 0;
		/*** modified by Akshaya on 20-MAY-16 end **/
		// Create object of SaralShieldBussinesLogic class
		SaralShieldBusinessLogic saralShieldBusinessLogic = new SaralShieldBusinessLogic(
				saralShieldBean);
		CommonForAllProd commonForAllProd = new CommonForAllProd();
		// Display Result Data common to all plans

		/* added by vrushali on 09/04/2015 start */
		double discountPercentage = saralShieldBusinessLogic.getDiscPercentage();
		/* End */
		double premium_Basic_WithoutDisc_Yearly = saralShieldBusinessLogic
				.getBasicPremium_Yearly();
		double disc_Basic_SelFreq = saralShieldBusinessLogic
				.getDisc_BasicPremium_SelFreq();
		double premium_ADB_WithoutDisc_Yearly = saralShieldBusinessLogic
				.getADBPremium_Yearly();
		double disc_ADB_SelFreq = saralShieldBusinessLogic
				.getDisc_ADBPremium_SelFreq();
		double premium_ATPDB_WithoutDisc_Yearly = saralShieldBusinessLogic
				.getATPDBPremium_Yearly();
		double disc_ATPDB_SelFreq = saralShieldBusinessLogic
				.getDisc_ATPDBPremium_SelFreq();
		// System.out.println("premium_Basic_WithoutDisc_Yearly [AK6]: "+premium_Basic_WithoutDisc_Yearly);
		// System.out.println("disc_Basic_SelFreq [AN6]: "+disc_Basic_SelFreq);
		// System.out.println("premium_ADB_WithoutDisc_Yearly [AK7]: "+premium_ADB_WithoutDisc_Yearly);
		// System.out.println("disc_ADB_SelFreq [AN7]: "+disc_ADB_SelFreq);
		// System.out.println("premium_ATPDB_WithoutDisc_Yearly [AK8]: "+premium_ATPDB_WithoutDisc_Yearly);
		// System.out.println("disc_ATPDB_SelFreq [AN8]: "+disc_ATPDB_SelFreq);
		String premiumBasicWithoutDisc_ForSelFreq = commonForAllProd
				.getRoundUp(saralShieldBusinessLogic
						.getPremWithoutStaffDisc_Basic());
		String premiumADB_WithoutDisc = commonForAllProd.getRoundUp(""
				+ saralShieldBusinessLogic.getPremWithoutStaffDisc_ADB());
		String premiumATPDB_WithoutDisc = commonForAllProd.getRoundUp(""
				+ saralShieldBusinessLogic.getPremWithoutStaffDisc_ATPDB());
		String totalPremWithoutDisc = commonForAllProd.getRoundUp(""
				+ (Double.parseDouble(premiumBasicWithoutDisc_ForSelFreq)
				+ Double.parseDouble(premiumADB_WithoutDisc) + Double
				.parseDouble(premiumATPDB_WithoutDisc)));
		String annualBasePremium = ""
				+ (saralShieldBusinessLogic.getPremiumYearly_Basic() - disc_Basic_SelFreq);
		// System.out.println("annualBasePremium  [AN13]"+ annualBasePremium);
		String annualRiderPremium = ""
				+ ((premium_ADB_WithoutDisc_Yearly + premium_ATPDB_WithoutDisc_Yearly) - (disc_ADB_SelFreq + disc_ATPDB_SelFreq));
		// System.out.println("annualRiderPremium  [AN12]"+ annualRiderPremium);
		double totalAnnualizedPrem = Double.parseDouble(annualBasePremium)
				+ Double.parseDouble(annualRiderPremium);

		String basicPremWithDiscWithoutServTx = commonForAllProd
				.getStringWithout_E(saralShieldBusinessLogic
						.getPremWithStaffDisc_Basic(disc_Basic_SelFreq,
								premiumBasicWithoutDisc_ForSelFreq));
		System.out.println("basicPremWithDiscWithoutServTx "
				+ basicPremWithDiscWithoutServTx);

		/** Added by Akshaya on 05-AUG-15 start */
		String minesOccuInterest = ""
				+ saralShieldBusinessLogic.getMinesOccuInterest(saralShieldBean
				.getBasicSA());

		String servicetax_MinesOccuInterest = ""
				+ saralShieldBusinessLogic
				.getServiceTaxMines(Double.parseDouble(minesOccuInterest));

		minesOccuInterest=""+(Double.parseDouble(minesOccuInterest)+Double.parseDouble(servicetax_MinesOccuInterest));

		// totalAnnualizedPrem =totalAnnualizedPrem +
		// Double.parseDouble(MinesOccuInterest);

		/****************************** Added by Akshaya on 05-AUG-15 end **********************/

		String totalInstPremiumWithDiscWithoutST = commonForAllProd.getRoundUp(""
				+ saralShieldBusinessLogic
				.getTotalInstallPremWithDisc(totalAnnualizedPrem));

		double totalInstPremiumWithDiscWithoutSTNotRounded = saralShieldBusinessLogic
				.getTotalInstallPremWithDisc(totalAnnualizedPrem);

		double premiumBasicWithoutDisc_ForSelFreqNotRounded = saralShieldBusinessLogic
				.getPremWithoutStaffDisc_Basic();
		double premiumADB_WithoutDiscNotRounded = saralShieldBusinessLogic
				.getPremWithoutStaffDisc_ADB();
		double premiumATPDB_WithoutDiscNotRouned = saralShieldBusinessLogic
				.getPremWithoutStaffDisc_ATPDB();

		double totalPremWithoutDiscNotRounded = premiumBasicWithoutDisc_ForSelFreqNotRounded
				+ premiumADB_WithoutDiscNotRounded
				+ premiumATPDB_WithoutDiscNotRouned;

		/**
		 * Change as per 1,Jan,2014 by Akshaya Mirajkar JkResident GST
		 */

		/*** modified by Akshaya on 20-MAY-16 start **/
		double KerlaServiceTax =0;
		double KeralaCessServiceTax =0;
		boolean isKerlaDiscount = saralShieldBean.isKerlaDisc();

/*		if (saralShieldBean.getStaffDisc()) {
			basicServiceTax = saralShieldBusinessLogic.getServiceTax(
					totalInstPremiumWithDiscWithoutSTNotRounded,
					saralShieldBean.isJKResident(), "basic");
			SBCServiceTax = saralShieldBusinessLogic.getServiceTax(
					totalInstPremiumWithDiscWithoutSTNotRounded,
					saralShieldBean.isJKResident(), "SBC");
			KKCServiceTax = saralShieldBusinessLogic.getServiceTax(
					totalInstPremiumWithDiscWithoutSTNotRounded,
					saralShieldBean.isJKResident(), "KKC");

			serviceTax = commonForAllProd.getStringWithout_E(basicServiceTax
					+ SBCServiceTax + KKCServiceTax);

			totalInstPremiumWithST = commonForAllProd.getRoundUp(""
					+ (totalInstPremiumWithDiscWithoutSTNotRounded + Double
							.parseDouble(serviceTax)));
		}
		else if(isKerlaDiscount)
		{
			basicServiceTax = saralShieldBusinessLogic.getServiceTax(Double.parseDouble(commonForAllProd.getRoundOffLevel2(commonForAllProd.getStringWithout_E(totalInstPremiumWithDiscWithoutSTNotRounded))),saralShieldBean.isJKResident(),"basic");
			KerlaServiceTax = saralShieldBusinessLogic.getServiceTax(totalPremWithoutDiscNotRounded,saralShieldBean.isJKResident(),"KERALA");
			KeralaCessServiceTax =KerlaServiceTax-basicServiceTax;
			serviceTax = commonForAllProd.getStringWithout_E(basicServiceTax+KerlaServiceTax);
			totalInstPremiumWithST = commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(totalPremWithoutDiscNotRounded +Double.parseDouble(serviceTax)));
		} else {

			basicServiceTax = saralShieldBusinessLogic.getServiceTax(
					totalPremWithoutDiscNotRounded,
					saralShieldBean.isJKResident(), "basic");
			SBCServiceTax = saralShieldBusinessLogic.getServiceTax(
					totalPremWithoutDiscNotRounded,
					saralShieldBean.isJKResident(), "SBC");
			KKCServiceTax = saralShieldBusinessLogic.getServiceTax(
					totalPremWithoutDiscNotRounded,
					saralShieldBean.isJKResident(), "KKC");

			serviceTax = commonForAllProd.getStringWithout_E(basicServiceTax
					+ SBCServiceTax + KKCServiceTax);

			totalInstPremiumWithST = commonForAllProd.getRoundUp(""
					+ (totalPremWithoutDiscNotRounded + Double
							.parseDouble(serviceTax)));
		}*/


		if (saralShieldBean.getStaffDisc())
		{
			if(isKerlaDiscount){
				basicServiceTax = saralShieldBusinessLogic.getServiceTax(Double.parseDouble(commonForAllProd.getRoundOffLevel2(commonForAllProd.getStringWithout_E(totalInstPremiumWithDiscWithoutSTNotRounded))),saralShieldBean.isJKResident(),"basic");
				KerlaServiceTax = saralShieldBusinessLogic.getServiceTax(totalInstPremiumWithDiscWithoutSTNotRounded,saralShieldBean.isJKResident(),"KERALA");
				KeralaCessServiceTax =KerlaServiceTax-basicServiceTax;
				serviceTax = commonForAllProd.getStringWithout_E(basicServiceTax+KerlaServiceTax);
				totalInstPremiumWithST = commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(totalInstPremiumWithDiscWithoutSTNotRounded +Double.parseDouble(serviceTax)));
			}else{
				basicServiceTax = saralShieldBusinessLogic.getServiceTax(Double.parseDouble(commonForAllProd.getRoundOffLevel2(commonForAllProd.getStringWithout_E(totalInstPremiumWithDiscWithoutSTNotRounded))),saralShieldBean.isJKResident(),"basic");
				SBCServiceTax = saralShieldBusinessLogic.getServiceTax(totalInstPremiumWithDiscWithoutSTNotRounded,saralShieldBean.isJKResident(),"SBC");
				KKCServiceTax = saralShieldBusinessLogic.getServiceTax(totalInstPremiumWithDiscWithoutSTNotRounded,saralShieldBean.isJKResident(),"KKC");
				serviceTax = commonForAllProd.getStringWithout_E(basicServiceTax+SBCServiceTax+KKCServiceTax);
				totalInstPremiumWithST = commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(totalInstPremiumWithDiscWithoutSTNotRounded +Double.parseDouble(serviceTax)));
			}
		}
		else if(isKerlaDiscount)
		{
			basicServiceTax = saralShieldBusinessLogic.getServiceTax(Double.parseDouble(commonForAllProd.getRoundOffLevel2(commonForAllProd.getStringWithout_E(totalInstPremiumWithDiscWithoutSTNotRounded))),saralShieldBean.isJKResident(),"basic");
			KerlaServiceTax = saralShieldBusinessLogic.getServiceTax(totalPremWithoutDiscNotRounded,saralShieldBean.isJKResident(),"KERALA");
			KeralaCessServiceTax =KerlaServiceTax-basicServiceTax;
			serviceTax = commonForAllProd.getStringWithout_E(basicServiceTax+KerlaServiceTax);
			totalInstPremiumWithST = commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(totalPremWithoutDiscNotRounded +Double.parseDouble(serviceTax)));
		}
		else{
			basicServiceTax = saralShieldBusinessLogic.getServiceTax(Double.parseDouble(commonForAllProd.getRoundOffLevel2(commonForAllProd.getStringWithout_E(totalInstPremiumWithDiscWithoutSTNotRounded))),saralShieldBean.isJKResident(),"basic");
			SBCServiceTax = saralShieldBusinessLogic.getServiceTax(totalPremWithoutDiscNotRounded,saralShieldBean.isJKResident(),"SBC");
			KKCServiceTax = saralShieldBusinessLogic.getServiceTax(totalPremWithoutDiscNotRounded,saralShieldBean.isJKResident(),"KKC");
			serviceTax = commonForAllProd.getStringWithout_E(basicServiceTax+SBCServiceTax+KKCServiceTax);
			totalInstPremiumWithST = commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(totalPremWithoutDiscNotRounded +Double.parseDouble(serviceTax)));
		}

		// String serviceTax = commonForAllProd
		// .getRoundUp(""
		// + (Double
		// .parseDouble(totalInstPremiumWithDiscWithoutST) *
		// (saralShieldBusinessLogic
		// .getServiceTax())));

		/*** modified by Akshaya on 20-MAY-16 end **/

		/************************* Output starts from here ******************************************/
		// if (saralShieldBean.getStaffDisc()) {
		// op3.setText("Installment Premium Inclusive Applicable Taxes is Rs."
		// + currencyFormat.format(Double
		// .parseDouble(totalInstPremiumWithDiscWithST)));
		// i.putExtra("op3", op3.getText().toString());
		// } else {
		// op3.setText("Installment Premium Inclusive Applicable Taxes is Rs."
		// + currencyFormat.format(Double
		// .parseDouble(totalInstPremiumWithoutDiscWithST)));
		// i.putExtra("op3", op3.getText().toString());
		// }
		// ADB
		if (cb_bi_saral_shield_adb_rider.isChecked()) {
			try {
				// premADB=Double.parseDouble(premiumADB.substring(0,
				// (indexADB+3)));
				op5.setText("Accidental Death Benefit Rider Premium is Rs. "
						+ currencyFormat.format(Double
						.parseDouble(premiumADB_WithoutDisc)));
				i.putExtra("op5", op5.getText().toString());
			} catch (StringIndexOutOfBoundsException e) {
				// premADB=Double.parseDouble(premiumADB);
				op5.setText("Accidental Death Benefit Rider Premium is Rs. "
						+ currencyFormat.format(Double
						.parseDouble(premiumADB_WithoutDisc)));
				i.putExtra("op5", op5.getText().toString());
			}
		}

		// ATPDB
		if (cb_bi_saral_shield_atpdb_rider.isChecked()) {
			try {
				// premATPDB=Double.parseDouble(premiumATPDB.substring(0,
				// (indexATPDB+3)));
				op6.setText("Accidental Total & Permanent Disability Benefit Rider Premium is Rs. "
						+ currencyFormat.format(Double
						.parseDouble(premiumATPDB_WithoutDisc)));
				i.putExtra("op6", op6.getText().toString());
			} catch (Exception e) {
				// premATPDB=Double.parseDouble(premiumATPDB);
				op6.setText("Accidental Total & Permanent Disability Benefit Rider Premium is Rs. "
						+ currencyFormat.format(Double
						.parseDouble(premiumATPDB_WithoutDisc)));
				i.putExtra("op6", op6.getText().toString());

			}
		}

		// Store Values for Rider Validation
		premBasic = Integer.parseInt(premiumBasicWithoutDisc_ForSelFreq);
		premADB = Integer.parseInt(premiumADB_WithoutDisc);
		premATPDB = Integer.parseInt(premiumATPDB_WithoutDisc);

		// Store value required to do Minimum Premium Validation
		if (saralShieldBean.getStaffDisc()) {
			basePremWithDisc_exclST = Integer
					.parseInt(""
							+ commonForAllProd.getRoundUp(""
							+ (saralShieldBusinessLogic
							.getPremWithoutStaffDisc_Basic() - saralShieldBusinessLogic
							.getDiscAfterFreqLoading_Basic_SelFreq())));
		} else {
			basePremWithDisc_exclST = Integer.parseInt(""
					+ commonForAllProd.getRoundUp(""
					+ saralShieldBusinessLogic
					.getPremWithoutStaffDisc_Basic()));
		}

		// System.out.println("Value [AH23]: "+basePremWithDisc_exclST);
		// System.out.println("basic premium saral shield" +
		// premiumBasicWithoutDisc_ForSelFreq);
		double sumOfRiders = premADB + premATPDB;

		/**
		 * Change as per 1,Jan,2014 by Akshaya Mirajkar.
		 */
		// valPremiumError = valInstPremium(totalInstPremiumWithDiscWithoutST);
		valPremiumError = valInstPremium(commonForAllProd
				.getRoundUp(basicPremWithDiscWithoutServTx));

		valRiderPremiumError = valRiderPremium(premBasic, sumOfRiders);

		// Validate Minimum premium and Rider premium
		if (valPremiumError && valRiderPremiumError) {
			op.setText("Basic Premium is Rs. "
					+ currencyFormat.format(Double
					.parseDouble(premiumBasicWithoutDisc_ForSelFreq)));
			op1.setText("Installment Premium is Rs. "
					+ currencyFormat.format(Double
					.parseDouble(totalPremWithoutDisc)));

			// show discount only if staff discount is checked
			if (saralShieldBean.getStaffDisc()) {
				op2.setText("Installment Premium with SBG Discount is Rs. "
						+ currencyFormat.format(Double
						.parseDouble(totalInstPremiumWithDiscWithoutST)));
				i.putExtra("op2", op2.getText().toString());
			}

			// op3.setText("Installment Premium Inclusive GST is Rs."
			// + currencyFormat.format(Double
			// .parseDouble(totalInstPremiumWithDiscWithST)));
			i.putExtra("op", op.getText().toString());
			i.putExtra("op1", op1.getText().toString());
			i.putExtra("op3", op3.getText().toString());

			/*** Added by Akshaya on 04-AUG-15 start ***/
			/** Added by Akshaya on 04-AUG-15 start */String staffStatus;
			if (cb_staffdisc.isChecked()) {
				staffStatus = "sbi";
				// disc_Basic_SelFreq
			} else
				staffStatus = "none";
			/*** Added by Akshaya on 04-AUG-15 end ***/

			try {
				retVal.append("<?xml version='1.0' encoding='utf-8' ?><SaralShield>");
				retVal.append("<errCode>0</errCode>");
				/*** Added by Akshaya on 04-AUG-15 start ***/
				retVal.append("<staffStatus>" + staffStatus + "</staffStatus>");
				retVal.append("<staffRebate>" + discountPercentage
						+ "</staffRebate>");
				retVal.append("<servcTax>" + serviceTax + "</servcTax>");
				retVal.append("<basicPremWithoutDisc>"
						+ premiumBasicWithoutDisc_ForSelFreq
						+ "</basicPremWithoutDisc>");
				retVal.append("<premADBWithoutDisc>" + premiumADB_WithoutDisc
						+ "</premADBWithoutDisc>");
				retVal.append("<premATPDBWithoutDisc>"
						+ premiumATPDB_WithoutDisc + "</premATPDBWithoutDisc>");
				retVal.append("<basicPremWithoutDiscSA>"
						+ premium_Basic_WithoutDisc_Yearly
						+ "</basicPremWithoutDiscSA>");
				retVal.append("<premADBWithoutDiscSA>"
						+ premium_ADB_WithoutDisc_Yearly
						+ "</premADBWithoutDiscSA>");
				retVal.append("<premATPDBWithoutDiscSA>"
						+ premium_ATPDB_WithoutDisc_Yearly
						+ "</premATPDBWithoutDiscSA>");

				/*** Added by Akshaya on 04-AUG-15 end ***/

				/*** Added by Akshaya on 05-AUG-15 start ***/

				/**
				 * <OccuInt> tag value will be stored in nbd_occu_extra of
				 * T_new_business_detail
				 *
				 */
				/*** Added by Akshaya on 05-AUG-15 end ***/

				retVal.append("<OccuInt>" + minesOccuInterest + "</OccuInt>");
				retVal.append("<OccuIntServiceTax>" +servicetax_MinesOccuInterest+ "</OccuIntServiceTax>");
				retVal.append("<basicPremWthOutDisc>"
						+ Double.parseDouble(premiumBasicWithoutDisc_ForSelFreq)
						+ "</basicPremWthOutDisc>" + "<InstmntPrem>"
						+ Double.parseDouble(totalPremWithoutDisc)
						+ "</InstmntPrem>" + "<InstmntPremWithDisc>"
						+ Double.parseDouble(totalInstPremiumWithDiscWithoutST)
						+ "</InstmntPremWithDisc>" + "<ADBPrem>"
						+ Double.parseDouble(premiumADB_WithoutDisc)
						+ "</ADBPrem>" + "<ATPDPrem>"
						+ Double.parseDouble(premiumATPDB_WithoutDisc)
						+ "</ATPDPrem>");

				/*** modified by Akshaya on 20-MAY-16 start **/
				// if (saralShieldBean.getStaffDisc())
				// retVal.append("<InstmntPremWthST>"
				// + Double.parseDouble(totalInstPremiumWithDiscWithST)
				// + "</InstmntPremWthST>");
				// else
				// retVal.append("<InstmntPremWthST>"
				// + Double.parseDouble(totalInstPremiumWithoutDiscWithST)
				// + "</InstmntPremWthST>");

				retVal.append("<InstmntPremWthST>"
						+ Double.parseDouble(totalInstPremiumWithST)
						+ "</InstmntPremWthST>");
				/*** modified by Akshaya on 20-MAY-16 end **/

				/*** modified by Akshaya on 20-MAY-16 start **/

				retVal.append("<basicServiceTax>"
						+ commonForAllProd.getStringWithout_E(basicServiceTax)
						+ "</basicServiceTax>" + "<SBCServiceTax>"
						+ commonForAllProd.getStringWithout_E(SBCServiceTax)
						+ "</SBCServiceTax>" + "<KKCServiceTax>"
						+ commonForAllProd.getStringWithout_E(KKCServiceTax)
						+ "</KKCServiceTax>"
						+ "<KeralaCessServiceTax>"  + commonForAllProd.getStringWithout_E(KeralaCessServiceTax)  + "</KeralaCessServiceTax>");

				/*** modified by Akshaya on 20-MAY-16 end **/

				retVal.append("</SaralShield>");
			} catch (Exception e) {
				retVal.append("<?xml version='1.0' encoding='utf-8' ?><SaralShield>"
						+ "<errCode>1</errCode>"
						+ "<errorMessage>"
						+ e.getMessage() + "</errorMessage></SaralShield>");
			}

			/*********************************************** xml Output *************************************/

			System.out.println("Final output in xml" + retVal.toString());
			// i.putExtra("pdf", retVal.toString());
			// startActivity(i);
		}
		/************************* output ends here ********************************************/
	}

	private boolean valInstPremium(String premiumBasicWithoutDisc_ForSelFreq) {
		if (spnr_bi_saral_shield_premium_paying_mode.getSelectedItem()
				.toString().equals("Yearly")
				&& (Integer.parseInt(premiumBasicWithoutDisc_ForSelFreq) < 2000)) {
			showAlert
					.setMessage("Minimum premium for Yearly Mode under this product is Rs. 2000");
			showAlert.setNeutralButton("OK",
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {
						}
					});
			showAlert.show();

			return false;

		} else if (spnr_bi_saral_shield_premium_paying_mode.getSelectedItem()
				.toString().equals("Half Yearly")
				&& (Integer.parseInt(premiumBasicWithoutDisc_ForSelFreq) < 1100)) {
			showAlert
					.setMessage("Minimum premium for Half Yearly Mode under this product is Rs. 1100");
			showAlert.setNeutralButton("OK",
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {
						}
					});
			showAlert.show();

			return false;
		}
		if (spnr_bi_saral_shield_premium_paying_mode.getSelectedItem()
				.toString().equals("Quarterly")
				&& (Integer.parseInt(premiumBasicWithoutDisc_ForSelFreq) < 600)) {
			showAlert
					.setMessage("Minimum premium for Quarterly Mode under this product is Rs. 600");
			showAlert.setNeutralButton("OK",
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {
							// Toast.makeText(getApplicationContext(),
							// "Ok button Clicked ", Toast.LENGTH_LONG).show();
						}
					});
			showAlert.show();
			return false;
		} else if (spnr_bi_saral_shield_premium_paying_mode.getSelectedItem()
				.toString().equals("Monthly")
				&& (Integer.parseInt(premiumBasicWithoutDisc_ForSelFreq) < 250)) {
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
		} else if (spnr_bi_saral_shield_premium_paying_mode.getSelectedItem()
				.toString().equals("Single")
				&& (Integer.parseInt(premiumBasicWithoutDisc_ForSelFreq) < 10000)) {
			showAlert
					.setMessage("Minimum premium for Single Mode under this product is Rs. 10000");
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

	// Validate minimum rider premium
	private boolean valRiderPremium(double premBasic, double sumOfRiders) {
		if ((premBasic * 0.3) < sumOfRiders) {
			showAlert
					.setMessage("Total of Rider Premium should not be greater than 30% of the Base Premium");
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

	private void getInput(SaralShieldBean saralShieldBean) {
		inputVal = new StringBuilder();

		String LifeAssured_title = spnr_bi_saral_shield_life_assured_title
				.getSelectedItem().toString();
		String LifeAssured_firstName = edt_bi_saral_shield_life_assured_first_name
				.getText().toString();
		String LifeAssured_middleName = edt_bi_saral_shield_life_assured_middle_name
				.getText().toString();
		String LifeAssured_lastName = edt_bi_saral_shield_life_assured_last_name
				.getText().toString();
		String LifeAssured_DOB = btn_bi_saral_shield_life_assured_date
				.getText().toString();
		String LifeAssured_age = edt_bi_saral_shield_life_assured_age.getText()
				.toString();

		String proposer_title = "";
		String proposer_firstName = "";
		String proposer_middleName = "";
		String proposer_lastName = "";
		String proposer_DOB = "";
		String proposer_age = "";
		String proposer_gender = "";

		int age = saralShieldBean.getAge();
		String planName = saralShieldBean.getPlanName();
		String gender = saralShieldBean.getGender();
		int basicPolicyTerm = saralShieldBean.getBasicTerm();
		int adbTerm = saralShieldBean.getADB_Term();
		int atpdbTerm = saralShieldBean.getATPDB_Term();

		double basicSumAssured = saralShieldBean.getBasicSA();
		double adbSumAssured = saralShieldBean.getADB_SA();
		double atpdbSumAssured = saralShieldBean.getATPDB_SA();
		String PremPayingMode = saralShieldBean.getPremFreq();

		boolean isJKresident = saralShieldBean.isJKResident();
		// boolean isJKresident = false;
		boolean isStaffOrNot = saralShieldBean.getStaffDisc();
		boolean statusADB = saralShieldBean.getADB_Status();
		boolean statusATPDB = saralShieldBean.getATPDB_Status();
		boolean smokerOrNot = false;

		String loanAccNo = saralShieldBean.getLoanAccountNumber();
		String loanFinanceInst = saralShieldBean.getLoanFinancialInstitue();
		String loanCatgy = saralShieldBean.getLoanCategory();
		String loanSumAssOutStanding = saralShieldBean
				.getLoanSumAssuredOutstanding();
		String loanBalTenure = saralShieldBean.getLoanBalanceLoanTenure();
		String loanFstEmiDate = saralShieldBean.getLoanFirstEmiDate();
		String loanLstEmiDate = saralShieldBean.getLoanLastEmiDate();
		String loanIntRate = saralShieldBean.getLRI();

		String is_Smoker_or_Not = "";
		smokerOrNot = is_Smoker_or_Not.equalsIgnoreCase("Smoker");

		inputVal.append("<?xml version='1.0' encoding='utf-8' ?><saralshield>");

		inputVal.append("<LifeAssured_title>" + LifeAssured_title
				+ "</LifeAssured_title>");
		inputVal.append("<LifeAssured_firstName>" + LifeAssured_firstName
				+ "</LifeAssured_firstName>");
		inputVal.append("<LifeAssured_middleName>" + LifeAssured_middleName
				+ "</LifeAssured_middleName>");
		inputVal.append("<LifeAssured_lastName>" + LifeAssured_lastName
				+ "</LifeAssured_lastName>");
		inputVal.append("<LifeAssured_DOB>" + LifeAssured_DOB
				+ "</LifeAssured_DOB>");
		inputVal.append("<LifeAssured_age>" + LifeAssured_age
				+ "</LifeAssured_age>");
		inputVal.append("<gender>" + gender + "</gender>");

		inputVal.append("<proposer_title>" + proposer_title
				+ "</proposer_title>");
		inputVal.append("<proposer_firstName>" + proposer_firstName
				+ "</proposer_firstName>");
		inputVal.append("<proposer_middleName>" + proposer_middleName
				+ "</proposer_middleName>");
		inputVal.append("<proposer_lastName>" + proposer_lastName
				+ "</proposer_lastName>");
		inputVal.append("<proposer_DOB>" + proposer_DOB + "</proposer_DOB>");
		inputVal.append("<proposer_age>" + proposer_age + "</proposer_age>");
		inputVal.append("<proposer_gender>" + proposer_gender
				+ "</proposer_gender>");

		/* parivartan changes */
		inputVal.append("<product_name>" + productName + "</product_name>");
		inputVal.append("<product_Code>" + product_Code + "</product_Code>");
		inputVal.append("<product_UIN>" + product_UIN + "</product_UIN>");
		inputVal.append("<product_cateogory>" + product_cateogory
				+ "</product_cateogory>");
		inputVal.append("<product_type>" + product_type + "</product_type>");
		/* end */

		inputVal.append("<proposer_Is_Same_As_Life_Assured>"
				+ proposer_Is_Same_As_Life_Assured
				+ "</proposer_Is_Same_As_Life_Assured>");

		inputVal.append("<isJKResident>" + isJKresident
				+ "</isJKResident>");
		inputVal.append("<isStaff>" + isStaffOrNot
				+ "</isStaff>");
        inputVal.append("<str_kerla_discount>" + str_kerla_discount + "</str_kerla_discount>");
		inputVal.append("<isSmoker>" + smokerOrNot
				+ "</isSmoker>");
		inputVal.append("<age>" + age + "</age>");
		// inputVal.append("<gender>" + gender + "</gender>");
		inputVal.append("<plan>" + planName + "</plan>");

		inputVal.append("<policyTerm>" + basicPolicyTerm + "</policyTerm>");

		inputVal.append("<isADBRider>" + statusADB
				+ "</isADBRider>");
		inputVal.append("<isATPDBRider>" + statusATPDB
				+ "</isATPDBRider>");

		inputVal.append("<adbTerm>" + adbTerm + "</adbTerm>");
		inputVal.append("<atpdbTerm>" + atpdbTerm + "</atpdbTerm>");

		inputVal.append("<premFreq>" + PremPayingMode + "</premFreq>");
		inputVal.append("<sumAssured>" + basicSumAssured + "</sumAssured>");
		inputVal.append("<adbSA>" + adbSumAssured + "</adbSA>");
		inputVal.append("<atpdbSA>" + atpdbSumAssured + "</atpdbSA>");
		inputVal.append("<nbd_loan_INT>" + loanIntRate + "</nbd_loan_INT>");
		inputVal.append("<nbd_loanAccNo>" + loanAccNo + "</nbd_loanAccNo>");
		inputVal.append("<nbd_loanFinanceInst>" + loanFinanceInst
				+ "</nbd_loanFinanceInst>");
		inputVal.append("<nbd_loanCatgy>" + loanCatgy + "</nbd_loanCatgy>");
		inputVal.append("<nbd_loanSumAssOutStanding>" + loanSumAssOutStanding
				+ "</nbd_loanSumAssOutStanding>");
		inputVal.append("<nbd_loanBalTenure>" + loanBalTenure
				+ "</nbd_loanBalTenure>");
		inputVal.append("<nbd_loanFstEmiDate>" + loanFstEmiDate
				+ "</nbd_loanFstEmiDate>");
		inputVal.append("<nbd_loanLstEmiDate>" + loanLstEmiDate
				+ "</nbd_loanLstEmiDate>");

		//Added Tushar Kadam Kerla Applicable Taxes 7-Jun-2019
		String str_kerla_discount = "N";
		if (cb_kerladisc.isChecked()) {
			str_kerla_discount = "Y";
		}

		inputVal.append("<KFC>" + str_kerla_discount + "</KFC>");
		//End Added Tushar Kadam Kerla Applicable Taxes 7-Jun-2019

		inputVal.append("</saralshield>");

	}

	// public long insertDataIntoDatabase() {
	// String str_need_analysis_output = "";
	// String str_need_analysis_input = "";
	// if (NeedAnalysisActivity.outputlist != null) {
	// str_need_analysis_output = NeedAnalysisActivity.outputlist;
	// str_need_analysis_input = NeedAnalysisActivity.strValue;
	// } else {
	// str_need_analysis_output = "";
	// str_need_analysis_input = "";
	// }
	//
	// M_Benefit_Illustration_Detail data = new M_Benefit_Illustration_Detail(
	// new String(inputVal), new String(retVal), proposal_no,
	// proposer_Is_Same_As_Life_Assured, proposer_Title,
	// proposer_First_Name, proposer_Middle_Name, proposer_Last_Name,
	// name_of_proposer, proposer_date_of_birth, lifeAssured_Title,
	// lifeAssured_First_Name, lifeAssured_Middle_Name,
	// lifeAssured_Last_Name, name_of_life_assured,
	// lifeAssured_date_of_birth, product_name, place1, place2, date1,
	// date2, agent_sign, proposer_sign,
	// proposer_Backdating_WishToBackDate_Policy,
	// proposer_Backdating_BackDate, flg_needAnalyis, createdBy,
	// createdDate, modifiedBy, modifiedDate, isSync, isFlag1,
	// isFlag2, isFlag3, isFlag4);
	// long count = 0;
	// count = db.insertBIDetail(data, QuatationNumber);
	//
	// List<M_basicDetail> dataBasicDetail = new ArrayList<M_basicDetail>();
	//
	// mobileNo = edt_saral_shield_contact_no.getText().toString();
	// emailId = edt_saral_shield_Email_id.getText().toString();
	// ConfirmEmailId = edt_saral_shield_ConfirmEmail_id.getText().toString();
	//
	// String photoByteArrayAsString = "";
	// if (photoBitmap != null) {
	// ByteArrayOutputStream stream = new ByteArrayOutputStream();
	// photoBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
	// byte[] photoByteArray = stream.toByteArray();
	// photoByteArrayAsString = Base64.encodeToString(photoByteArray,
	// Base64.DEFAULT);
	//
	// }
	//
	// Boolean isSync = false;
	// Boolean isFlag1 = true;
	// Boolean isFlag2 = true;
	// Boolean isFlag3 = true;
	// Boolean isFlag4 = true;
	//
	// if (proposer_Is_Same_As_Life_Assured.equalsIgnoreCase("y")) {
	// if (lifeAssured_Title.equalsIgnoreCase("Mr.")) {
	// propsoser_gender = "Male";
	//
	// } else if (lifeAssured_Title.equalsIgnoreCase("Ms.")) {
	// propsoser_gender = "Female";
	//
	// } else if (lifeAssured_Title.equalsIgnoreCase("Mrs.")) {
	// propsoser_gender = "Female";
	// }
	//
	// M_basicDetail model = new M_basicDetail(lifeAssured_Title,
	// lifeAssured_First_Name, lifeAssured_Last_Name,
	// lifeAssured_Middle_Name, name_of_life_assured,
	// propsoser_gender, lifeAssured_date_of_birth, mobileNo,
	// emailId, photoByteArrayAsString, str_need_analysis_output,
	// flg_personalDetails, str_need_analysis_input, modifiedDate,
	// isSync, isFlag1, isFlag2, isFlag3, isFlag4);
	// dataBasicDetail.add(model);
	// }
	//
	// try {
	// long rowId1 = db.insertBasicDetails(dataBasicDetail,
	// QuatationNumber);
	// } catch (Exception e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	//
	// return count;
	//
	// }

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		/* parivartan changes */
		if (requestCode == SIGNATURE_ACTIVITY) {
			if (resultCode == RESULT_OK) {

				Bundle bundle = data.getExtras();
				String status = bundle.getString("status");
				if (status.equalsIgnoreCase("done")) {
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
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
					Bitmap mFaceBitmap = b.copy(Bitmap.Config.RGB_565, true);
					b.recycle();
					if (mFaceBitmap != null) {
						Bitmap scaled = Bitmap.createScaledBitmap(bmp, 230,
								200, true);
						photoBitmap = scaled;
						imageButtonSaralShieldProposerPhotograph
								.setImageBitmap(scaled);
					} else {
						photoBitmap = null;
					}
				}
			}
		}
		/* end */
	}

	private void windowmessagesgin() {

		d = new Dialog(BI_SaralShieldActivity.this);
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
				Intent intent = new Intent(BI_SaralShieldActivity.this,
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

	public void windowmessageProposersgin() {

		d = new Dialog(BI_SaralShieldActivity.this);
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
				Intent intent = new Intent(BI_SaralShieldActivity.this,
						ProposerCaptureSignature.class);
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

	private void Dialog() {

		final Dialog d = new Dialog(this);
		d.requestWindowFeature(Window.FEATURE_NO_TITLE);
		d.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.BLACK));
		d.setContentView(R.layout.layout_saral_shield_bi_grid);


		TextView tv_proposername = d
				.findViewById(R.id.tv_saral_shield_proposername);
		TextView tv_proposal_number = d
				.findViewById(R.id.tv_saral_shield_proposal_number);
		TextView tv_bi_is_Staff = d
				.findViewById(R.id.tv_bi_is_Staff);
		TextView tv_bi_is_jk = d.findViewById(R.id.tv_bi_is_jk);

		TextView tv_bi_saral_shield_plan = d
				.findViewById(R.id.tv_bi_saral_shield_plan);
		TextView tv_life_age_at_entry = d
				.findViewById(R.id.tv_bi_saral_shield_life_assured_age);
		TextView tv_life_assured_gender = d
				.findViewById(R.id.tv_bi_saral_shield_life_assured_gender);
		TextView tv_premPaymentfrequency = d
				.findViewById(R.id.tv_bi_saral_shield_frequency);

		TextView tv_basic_cover_term = d
				.findViewById(R.id.tv_saral_shield_term_basic_cover);
		TextView tv_basic_cover_sum_assured = d
				.findViewById(R.id.tv_saral_shield_sum_assured_basic_cover);
		TextView tv_basic_cover_yearly_premium = d
				.findViewById(R.id.tv_saral_shield_premium_basic_cover);
		TextView tv_adb_term = d
				.findViewById(R.id.tv_saral_shield_term_adb_rider);
		TextView tv_adb_sum_assured = d
				.findViewById(R.id.tv_saral_shield_sum_assured_adb_rider);
		TextView tv_adb_yearly_premium = d
				.findViewById(R.id.tv_saral_shield_premium_adb_rider);
		TextView tv_atpdb_term = d
				.findViewById(R.id.tv_saral_shield_term_atpdb_rider);
		TextView tv_atpdb_sum_assured = d
				.findViewById(R.id.tv_saral_shield_sum_assured_atpdb_rider);
		TextView tv_atpdb_yearly_premium = d
				.findViewById(R.id.tv_saral_shield_premium_atpdb_rider);


        TextView tv_for_frequency_premium_with_staff = (TextView) d
                .findViewById(R.id.tv_for_frequency_premium_with_staff);

        TextView tv_bi_saral_shield_installment_premium_with_discount = (TextView) d
                .findViewById(R.id.tv_bi_saral_shield_installment_premium_with_discount);


		TextView tv_bi_saral_shield_frequency_premium = d
				.findViewById(R.id.tv_for_frequency_premium);
		TextView tv_bi_saral_shield_frequency_premium_for_service_tax = d
				.findViewById(R.id.tv_for_frequency_premium_for_service_Tax);
		TextView tv_bi_saral_shield_installment_premium = d
				.findViewById(R.id.tv_bi_saral_shield_installment_premium);

		TextView tv_bi_saral_shield_installment_premium_with_tax = d
				.findViewById(R.id.tv_bi_saral_shield_installment_premium_with_tax);

		TextView tv_bi_saral_shield_loan_account_no = d
				.findViewById(R.id.tv_bi_saral_shield_loan_account_no);
		TextView tv_bi_saral_shield_loan_financing_institute_name = d
				.findViewById(R.id.tv_bi_saral_shield_loan_financing_institute_name);
		TextView tv_bi_saral_shield_loan_category = d
				.findViewById(R.id.tv_bi_saral_shield_loan_category);
		TextView tv_bi_saral_shield_loan_outstanding_loan_amount = d
				.findViewById(R.id.tv_bi_saral_shield_loan_outstanding_loan_amount);
		TextView tv_bi_saral_shield_loan_balance_tenure = d
				.findViewById(R.id.tv_bi_saral_shield_loan_balance_tenure);
		TextView tv_bi_saral_shield_loan_rate_of_interest = d
				.findViewById(R.id.tv_bi_saral_shield_loan_rate_of_interest);
		TextView tv_bi_saral_shield_loan_date_of_first_emi = d
				.findViewById(R.id.tv_bi_saral_shield_loan_date_of_first_emi);
		TextView tv_bi_saral_shield_loan_date_of_last_emi = d
				.findViewById(R.id.tv_bi_saral_shield_loan_date_of_last_emi);

		// staff discount
		TableRow tr_installment_premium_with_staffdiscount_table = d
				.findViewById(R.id.tr_installment_premium_with_staffdiscount_table);
		TextView tv_installment_premium_with_staffdiscount = d
				.findViewById(R.id.tv_installment_premium_with_staffdiscount);
		TextView tv_installment_premium_with_staffdiscount_value = d
				.findViewById(R.id.tv_installment_premium_with_staffdiscount_value);

		LinearLayout ll_bi_saral_shield_life_assured_loan_details = d
				.findViewById(R.id.ll_bi_saral_shield_life_assured_loan_details);
		LinearLayout ll_saral_shield_adb_rider = d
				.findViewById(R.id.ll_saral_shield_adb_rider);
		LinearLayout ll_saral_shield_atpdb_rider = d
				.findViewById(R.id.ll_saral_shield_atpdb_rider);

		final EditText edt_Policyholderplace = d
				.findViewById(R.id.edt_Policyholderplace);
		final TextView edt_proposer_name = d
				.findViewById(R.id.edt_ProposalName);
		final CheckBox cb_statement = d
				.findViewById(R.id.cb_statement);

        TextView tv_bi_saral_shield_basic_service_tax_first_year = (TextView) d
                .findViewById(R.id.tv_bi_saral_shield_basic_service_tax_first_year);

		/* parivartan changes */
		imageButtonSaralShieldProposerPhotograph = d
				.findViewById(R.id.imageButtonSaralShieldProposerPhotograph);
		imageButtonSaralShieldProposerPhotograph
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View view) {

						Check = "Photo";
						commonMethods.windowmessage(context, "_cust1Photo.jpg");
					}
				});
		/* end */

		/* Need Analysis */
		final TextView edt_proposer_name_need_analysis = d
				.findViewById(R.id.edt_proposer_name_need_analysis);

		final CheckBox cb_statement_need_analysis = d
				.findViewById(R.id.cb_statement_need_analysis);
		cb_statement_need_analysis.setChecked(true);
		TableRow tr_need_analysis = d
				.findViewById(R.id.tr_need_analysis);

		if (NeedAnalysisActivity.str_need_analysis != null) {
			if (NeedAnalysisActivity.str_need_analysis.equals("1")) {

				File mypath_old = mStorageUtils.createFileToAppSpecificDir(context, "NA" + ".pdf");
				File mypath_new = mStorageUtils.createFileToAppSpecificDir(context, QuatationNumber + "_NA"
						+ ".pdf");
				if (mypath_old.exists()) {
					mypath_old.renameTo(mypath_new);
				}
				flg_needAnalyis = "1";
				tr_need_analysis.setVisibility(View.VISIBLE);
			} else {
				cb_statement_need_analysis.setChecked(true);
				tr_need_analysis.setVisibility(View.GONE);
			}

		}

		Button btn_proceed = d.findViewById(R.id.btn_proceed);

		btn_MarketingOfficalDate = d
				.findViewById(R.id.btn_MarketingOfficalDate);
		btn_PolicyholderDate = d
				.findViewById(R.id.btn_PolicyholderDate);

		Ibtn_signatureofMarketing = d
				.findViewById(R.id.Ibtn_signatureofMarketing);
		Ibtn_signatureofPolicyHolders = d
				.findViewById(R.id.Ibtn_signatureofPolicyHolders);

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
							+ " have undergone the Need Analysis after having reviewed the SBI Life Product options, I have opted for SBI LIFE - Saral Shield.");

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
							+ " have undergone the Need Analysis after having reviewed the SBI Life Product options, I have opted for SBI LIFE - Saral Shield.");

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
			imageButtonSaralShieldProposerPhotograph
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
							imageButtonSaralShieldProposerPhotograph
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
					String productCode = "";

					if (saralShieldBean.getPlanName().equals(
							"Level Term Assurance"))
						productCode = "SHIELD3A";
					else if (saralShieldBean.getPlanName().equals(
							"Decreasing Term Assurance[Loan Protection]"))
						productCode = "SHIELD3B";
					else if (saralShieldBean
							.getPlanName()
							.equals("Decreasing Term Assurance[Family Income Protection]"))
						productCode = "SHIELD3C";

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
							productName,
							obj.getRound(sumAssured),
							obj.getRound(totalPremiumYearlyInstallmentPremWithServiceTaxYearly),
							emailId, mobileNo, agentEmail, agentMobile,
							na_input, na_output, premPayingMode, Integer
							.parseInt(policyTerm), 0, productCode,
							getDate(lifeAssured_date_of_birth), "", inputVal
							.toString(), retVal.toString());

					name_of_person = name_of_life_assured;
					System.out.println("in main : " + UIN_NO);


					if (radioButtonTrasactionModeParivartan.isChecked()) {
						mode = "Parivartan";
					} else if(radioButtonTrasactionModeManual.isChecked()){
						mode = "Manual";
					}
					dbHelper.AddNeedAnalysisDashboardDetails(new ProductBIBean(
							"",
							QuatationNumber,
							productName,
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
							obj.getRound(sumAssured),
							obj.getRound(totalPremiumYearlyInstallmentPremWithServiceTaxYearly),
							agentEmail, agentMobile, na_input, na_output,
							premPayingMode, Integer.parseInt(policyTerm), 0,
							productCode, getDate(lifeAssured_date_of_birth),
							"", "",mode,inputVal
							.toString(), retVal.toString()));

					createPdf();


					NABIObj.serviceHit(BI_SaralShieldActivity.this,
							na_cbi_bean, newFile, needAnalysispath.getPath(),
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
					}
					/* parivartan changes */
					else if (!radioButtonTrasactionModeParivartan.isChecked()
							&& !radioButtonTrasactionModeManual.isChecked()) {
						commonMethods.dialogWarning(context,"Please Select Transaction Mode", true);
						setFocusable(linearlayoutTrasactionModeParivartan);
						linearlayoutTrasactionModeParivartan.requestFocus();
					} else if (photoBitmap == null) {
						commonMethods.dialogWarning(context,"Please Capture the Photo", true);
						setFocusable(imageButtonSaralShieldProposerPhotograph);
						imageButtonSaralShieldProposerPhotograph.requestFocus();
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

		String isJkResident = prsObj.parseXmlTag(input, "isJKResident");
		adbRiderStatus = prsObj.parseXmlTag(input, "isADBRider");
		atpdbRiderStatus = prsObj.parseXmlTag(input, "isATPDBRider");

		String staffdiscount = prsObj.parseXmlTag(input, "isStaff");

		String staffDiscountInstallmentPremWithServiceTaxYearly = ((int) Double
				.parseDouble(prsObj.parseXmlTag(output, "InstmntPremWithDisc")))
				+ "";
        tv_bi_saral_shield_installment_premium_with_discount.setText(staffDiscountInstallmentPremWithServiceTaxYearly);
        if (staffdiscount.equalsIgnoreCase("true")) {
            tv_bi_is_Staff.setText("Staff");
            tr_installment_premium_with_staffdiscount_table
                    .setVisibility(View.VISIBLE);
		tv_installment_premium_with_staffdiscount_value
                    .setText(staffDiscountInstallmentPremWithServiceTaxYearly);
		tv_installment_premium_with_staffdiscount
				.setText((spnr_bi_saral_shield_premium_paying_mode
                            .getSelectedItem().toString() + " Installment Premium with Staff Discount"));
		} else {
            tv_bi_is_Staff.setText("Non Staff");
            tr_installment_premium_with_staffdiscount_table
                    .setVisibility(View.GONE);

		}

		if (isJkResident.equalsIgnoreCase("true")) {
			tv_bi_is_jk.setText("Yes");
		} else {
			tv_bi_is_jk.setText("No");
		}

		policyTerm = prsObj.parseXmlTag(input, "policyTerm");
		tv_basic_cover_term.setText(policyTerm + " Years");

		sumAssured = prsObj.parseXmlTag(input, "sumAssured");

		basicCoverYearlyPremium = ((int) Double.parseDouble(prsObj.parseXmlTag(
				output, "basicPremWthOutDisc"))) + "";
		tv_basic_cover_yearly_premium.setText("" + basicCoverYearlyPremium);

		basicCoverSumAssured = (int) Double.parseDouble(sumAssured) + "";
		tv_basic_cover_sum_assured.setText(""
				+ getformatedThousandString(Integer
				.parseInt(basicCoverSumAssured)));

		// Changes done by amit
		tv_bi_saral_shield_frequency_premium
				.setText((spnr_bi_saral_shield_premium_paying_mode
                        .getSelectedItem().toString() + " Installment Premium (Rs.)"));


        tv_for_frequency_premium_with_staff.setText((spnr_bi_saral_shield_premium_paying_mode
                .getSelectedItem().toString() + " Installment Premium with Discount(Rs.)"));

		tv_bi_saral_shield_frequency_premium_for_service_tax
				.setText((spnr_bi_saral_shield_premium_paying_mode
                        .getSelectedItem().toString() + " Installment Premium with Applicable Taxes(Rs.)"));

		String totalPremiumYearlyInstallmentPremYearly = ((int) Double
				.parseDouble(prsObj.parseXmlTag(output, "InstmntPrem"))) + "";
		tv_bi_saral_shield_installment_premium.setText("Rs. "
				+ totalPremiumYearlyInstallmentPremYearly);

		totalPremiumYearlyInstallmentPremWithServiceTaxYearly = ((int) Double
				.parseDouble(prsObj.parseXmlTag(output, "InstmntPremWthST")))
				+ "";
        tv_bi_saral_shield_installment_premium_with_tax
                .setText(totalPremiumYearlyInstallmentPremWithServiceTaxYearly);

		// Amit changes start- 23-5-2016
		/* basicServiceTax = prsObj.parseXmlTag(output, "basicServiceTax"); */
		basicServiceTax = prsObj.parseXmlTag(output, "servcTax");

        tv_bi_saral_shield_basic_service_tax_first_year.setText(""
                + obj.getRound(obj.getStringWithout_E(Double
                .valueOf(basicServiceTax.equals("") ? "0"
                        : basicServiceTax))));

		String servcTax = prsObj.parseXmlTag(output, "servcTax");
		double basicServiceTaxDouble = Double
				.valueOf((basicServiceTax == null || basicServiceTax.equals("")) ? "0"
						: basicServiceTax);

//        TextView tv_bi_saral_shield_service_tax_first_year = d
//                .findViewById(R.id.tv_bi_saral_shield_service_tax_first_year);
//
//        TextView tv_bi_saral_shield_saral_pension_swachh_bharat_tax_first_year = d
//                .findViewById(R.id.tv_bi_saral_shield_saral_pension_swachh_bharat_tax_first_year);
//
//        TextView tv_bi_saral_shield_krishi_kalyan_tax_first_year = d
//                .findViewById(R.id.tv_bi_saral_shield_krishi_kalyan_tax_first_year);
//        TextView tv_bi_saral_shield_total_service_tax_first_year = d
//                .findViewById(R.id.tv_bi_saral_shield_total_service_tax_first_year);

//        tv_bi_saral_shield_service_tax_first_year.setText("Rs. "
//                + obj.getRound(obj.getStringWithout_E(basicServiceTaxDouble)));
//
//        String SBCServiceTax = prsObj.parseXmlTag(output, "SBCServiceTax");
//        double sbcServiceTaxDouble = Double
//                .valueOf((SBCServiceTax == null || SBCServiceTax.equals("")) ? "0"
//                        : SBCServiceTax);
//
//        tv_bi_saral_shield_saral_pension_swachh_bharat_tax_first_year
//                .setText("Rs. "
//                        + obj.getRound(obj
//                        .getStringWithout_E(sbcServiceTaxDouble)));
//
//        String KKCServiceTax = prsObj.parseXmlTag(output, "KKCServiceTax");
//        double kkcserviceTaxDouble = Double
//                .valueOf((KKCServiceTax == null || KKCServiceTax.equals("")) ? "0"
//                        : KKCServiceTax);
//        tv_bi_saral_shield_krishi_kalyan_tax_first_year.setText("Rs. "
//                + obj.getRound(obj.getStringWithout_E(kkcserviceTaxDouble)));
//
//        double totalServiceTaxFirstYear = Double
//                .valueOf((servcTax == null || servcTax.equals("")) ? "0"
//                        : servcTax);
//
//        tv_bi_saral_shield_total_service_tax_first_year
//                .setText("Rs. "
//                        + obj.getRound(obj
//                        .getStringWithout_E(totalServiceTaxFirstYear)));

		String totalPremiumServiceTaxYearly = prsObj.parseXmlTag(output,
				"InstmntPremWthST");

		double totalPremiumWithAllTaxes = Double
				.valueOf((totalPremiumServiceTaxYearly == null || totalPremiumServiceTaxYearly
						.equals("")) ? "0" : totalPremiumServiceTaxYearly);

		tv_bi_saral_shield_installment_premium_with_tax
				.setText("Rs. "
						+ obj.getRound(obj
						.getStringWithout_E(totalPremiumWithAllTaxes)));
		// Amit changes end- 23-5-2016

		TextView tv_saral_shield_sbi_life_details = d
                .findViewById(R.id.tv_Company_policy_surrender_dec);

		String str_prem_freq = "";
		if (premPayingMode.equalsIgnoreCase("Single"))
		{
			str_prem_freq = "Single";
			Company_policy_surrender_dec = "Your SBI Life -Saral Shield (UIN: 111N066V03) is a "
					+ str_prem_freq
					+ " premium policy and you are required to pay One Time Premium of Rs. "
					+ totalPremiumYearlyInstallmentPremWithServiceTaxYearly
					+ " .Your Policy Term is "
					+ policyTerm
					+ " years"
					+ " and Basic Sum Assured is Rs. "
					+  getformatedThousandString(Integer.parseInt(obj.getRound(obj
					.getStringWithout_E(Double.valueOf(sumAssured
							.equals("") ? "0" : sumAssured)))));

		}
		else
		{
			str_prem_freq = "Regular/Limited";

			Company_policy_surrender_dec = "Your SBI Life -Saral Shield (UIN: 111N066V03) is a "
					+ str_prem_freq
					+ " premium policy, for which your first year "
					+ premPayingMode
					+ " Premium is Rs. "
					+ totalPremiumYearlyInstallmentPremWithServiceTaxYearly
					+ " .Your Policy Term is "
					+ policyTerm
					+ " years"
					+ " , Premium Payment Term is "
					+ policyTerm
					+ " years"
					+ " and Basic Sum Assured is Rs. "
					+

					getformatedThousandString(Integer.parseInt(obj.getRound(obj
							.getStringWithout_E(Double.valueOf(sumAssured
									.equals("") ? "0" : sumAssured)))));
		}



		tv_saral_shield_sbi_life_details.setText(Company_policy_surrender_dec);

		planProposedName = prsObj.parseXmlTag(input, "plan");
		tv_bi_saral_shield_plan.setText(planProposedName);
		// tv_basic_cover_plan.setText(planProposedName);
		if (planProposedName.equalsIgnoreCase("Level Term Assurance")) {
			ll_bi_saral_shield_life_assured_loan_details
					.setVisibility(View.GONE);

		} else if (planProposedName
				.equalsIgnoreCase("Increasing Term Assurance")) {
			ll_bi_saral_shield_life_assured_loan_details
					.setVisibility(View.GONE);
		} else if (planProposedName
				.equalsIgnoreCase("Decreasing Term Assurance[Loan Protection]")) {
			ll_bi_saral_shield_life_assured_loan_details
					.setVisibility(View.VISIBLE);

			tv_bi_saral_shield_loan_account_no.setText(loanAccountNo);
			tv_bi_saral_shield_loan_financing_institute_name
					.setText(financingInstituteName);
			tv_bi_saral_shield_loan_category.setText(loanCategory);
			tv_bi_saral_shield_loan_outstanding_loan_amount
					.setText(sumAssuredOutStanding);
			tv_bi_saral_shield_loan_balance_tenure.setText(balanceLoanTenure);
			tv_bi_saral_shield_loan_rate_of_interest
					.setText(lifeAssured_loan_rate_interest);
			tv_bi_saral_shield_loan_date_of_first_emi.setText(firstEmiDate);
			tv_bi_saral_shield_loan_date_of_last_emi.setText(lastEmiDate);

		} else if (planProposedName
				.equalsIgnoreCase("Decreasing Term Assurance[Family Income Protection]")) {
			ll_bi_saral_shield_life_assured_loan_details
					.setVisibility(View.GONE);
		}

		tv_basic_cover_term.setText(policyTerm);

		if (adbRiderStatus.equalsIgnoreCase("true")) {
			ll_saral_shield_adb_rider.setVisibility(View.VISIBLE);
			String adbRiderYearly = ((int) Double.parseDouble(prsObj.parseXmlTag(
					output, "ADBPrem"))) + "";
			adbRiderTerm = prsObj.parseXmlTag(input, "adbTerm");
			adbSA = ((int) Double.parseDouble(prsObj
					.parseXmlTag(input, "adbSA"))) + "";
			tv_adb_term.setText(adbRiderTerm);
			tv_adb_sum_assured.setText(adbSA);
			tv_adb_yearly_premium.setText(adbRiderYearly);

		}else {
			ll_saral_shield_adb_rider.setVisibility(View.VISIBLE);
			tv_adb_term.setText("0");
			tv_adb_sum_assured.setText("0");
			tv_adb_yearly_premium.setText("0");

		}

		if (atpdbRiderStatus.equalsIgnoreCase("true")) {
			ll_saral_shield_atpdb_rider.setVisibility(View.VISIBLE);
			String atpdbRiderYearly = ((int) Double.parseDouble(prsObj.parseXmlTag(
					output, "ATPDPrem"))) + "";
			atpdbRiderTerm = prsObj.parseXmlTag(input, "atpdbTerm");
			atpdbSA = ((int) Double.parseDouble(prsObj.parseXmlTag(input,
					"atpdbSA"))) + "";

			tv_atpdb_term.setText(atpdbRiderTerm);
			tv_atpdb_sum_assured.setText(atpdbSA);
			tv_atpdb_yearly_premium.setText(atpdbRiderYearly);

        }else {
            ll_saral_shield_atpdb_rider.setVisibility(View.VISIBLE);
            tv_atpdb_term.setText("0");
            tv_atpdb_sum_assured.setText("0");
            tv_atpdb_yearly_premium.setText("0");
		}

      /*  LinearLayout ll_signature = d
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

	private void validationOfMoile_EmailId() {

		edt_saral_shield_contact_no.addTextChangedListener(new TextWatcher() {

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
				String abc = edt_saral_shield_contact_no.getText().toString();
				mobile_validation(abc);

			}
		});

		edt_saral_shield_Email_id.addTextChangedListener(new TextWatcher() {

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
				ProposerEmailId = edt_saral_shield_Email_id.getText()
						.toString();
				//email_id_validation(ProposerEmailId);

			}
		});

		edt_saral_shield_ConfirmEmail_id
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
						String proposer_confirm_emailId = edt_saral_shield_ConfirmEmail_id
								.getText().toString();
						//confirming_email_id(proposer_confirm_emailId);

					}
				});

	}

	public void onClickLADob(View v) {
		if (!na_dob.equals("") && flag == 0) {
			System.out.println(" na_dob : " + na_dob);
			initialiseDateParameter(na_dob, 35);
			DIALOG_ID = 4;
			updateDisplay(DIALOG_ID);
			flag = 1;
		} else {
			initialiseDateParameter(lifeAssured_date_of_birth, 35);
			DIALOG_ID = 4;
			showDialog(DATE_DIALOG_ID);
		}
	}

	public void emiDate1(View v) {
		initialiseDateParameter(firstEmiDate, 0);
		DIALOG_ID = 5;
		showDialog(DATE_DIALOG_ID);
	}

	public void emiDate2(View v) {
		initialiseDateParameter(lastEmiDate, 0);
		DIALOG_ID = 6;
		showDialog(DATE_DIALOG_ID);
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

	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

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
		} else
			age = tYear - mYear - 1;

		String final_age = Integer.toString(age);

		if ((id != 5) && (id != 6) && final_age.contains("-")) {
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

				case 4:
					final_age = Integer.toString(age);
					if (final_age.contains("-")) {
						commonMethods.BICommonDialog(context, "Please fill Valid Bith Date");
					} else {

						if (18 <= age && age <= 60) {

							btn_bi_saral_shield_life_assured_date.setText(date);

							edt_bi_saral_shield_life_assured_age.setText(final_age);

							lifeAssured_date_of_birth = getDate1(date + "");
							clearFocusable(btn_bi_saral_shield_life_assured_date);
							setFocusable(edt_saral_shield_contact_no);
							edt_saral_shield_contact_no.requestFocus();

						} else {

							commonMethods.BICommonDialog(context, "The Age of Life Assured should be between 18 & 60 yrs");
							btn_bi_saral_shield_life_assured_date
									.setText("Select Date");
							lifeAssured_date_of_birth = "";
							clearFocusable(btn_bi_saral_shield_life_assured_date);
							setFocusable(btn_bi_saral_shield_life_assured_date);
							btn_bi_saral_shield_life_assured_date.requestFocus();
						}
					}
					break;

				case 5:
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

				case 6:

					if (!(firstEmiDate.equals(""))) {
						lastEmiDate = getDate1(date + "");
						if (age < emiage_1st) {
							btn_plandetail_loan_last_emi_date.setText(date);

							clearFocusable(btn_plandetail_loan_last_emi_date);
							setFocusable(cb_bi_saral_shield_adb_rider);
							cb_bi_saral_shield_adb_rider.requestFocus();
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
			image.setAlignment(Element.ALIGN_LEFT);
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

			cell = new PdfPCell(
					new Phrase(
							"Website: www.sbilife.co.in | Email: info@sbilife.co.in | CIN: L99999MH2000PLC129113. Toll Free: 1800 267 9090 (Between 9.00 am & 9.00 pm)",
							small_bold));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setBorder(Rectangle.NO_BORDER);

			table.addCell(cell);

			// cell = new PdfPCell(new


			cell = new PdfPCell(new Phrase("\n", normal));
			cell.setColspan(3);
			cell.setBorder(Rectangle.BOTTOM);
			cell.setBorderWidth(1.2f);
			table.addCell(cell);
			Paragraph Para_Header = new Paragraph();
			Para_Header
					.add(new Paragraph(
                            "Premium Quotation  for SBI Life - Saral Shield (UIN No : 111N066V03)",
							headerBold));

			PdfPTable headertable = new PdfPTable(1);
			headertable.setWidthPercentage(100);
			PdfPCell c1 = new PdfPCell(new Phrase(Para_Header));
			c1.setBackgroundColor(BaseColor.GRAY);
			c1.setPadding(5);
			c1.setHorizontalAlignment(Element.ALIGN_CENTER);
			headertable.addCell(c1);
			headertable.setHorizontalAlignment(Element.ALIGN_CENTER);
			document.add(headertable);

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
					"SBI Life - Saral Shield (UIN: 111N066V03)", normal1));
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
			// cell = new PdfPCell(new Phrase(" Smoker/Non Smoker", normal1));
			// cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			// personalDetail_table.addCell(cell);
			//
			// // Changes done by amit -24-3-2015
			// if (smoker_or_not.equalsIgnoreCase("true")) {
			// cell = new PdfPCell(new Phrase("Smoker", normal1));
			// cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			// personalDetail_table.addCell(cell);
			// } else {
			// cell = new PdfPCell(new Phrase("Non Smoker", normal1));
			// cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			// personalDetail_table.addCell(cell);
			// }

			// 6 row
			cell = new PdfPCell(new Phrase(" Frequency", normal1));
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			personalDetail_table.addCell(cell);

			cell = new PdfPCell(new Phrase(" " + premPayingMode, normal1));
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			personalDetail_table.addCell(cell);

			String isStaff = "";
			if (cb_staffdisc.isChecked()) {
                isStaff = "Staff";

                cell = new PdfPCell(new Phrase("Staff Discount", normal1));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                personalDetail_table.addCell(cell);

                cell = new PdfPCell(new Phrase(isStaff, normal1));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                personalDetail_table.addCell(cell);
            } else {
                isStaff = "Non Staff";

				cell = new PdfPCell(new Phrase("Staff Discount", normal1));
				cell.setHorizontalAlignment(Element.ALIGN_LEFT);
				personalDetail_table.addCell(cell);

				cell = new PdfPCell(new Phrase(isStaff, normal1));
				cell.setHorizontalAlignment(Element.ALIGN_LEFT);
				personalDetail_table.addCell(cell);
			}

			String isJk = "";
			if (cb_bi_saral_shield_JKResident.isChecked()) {
				isJk = "yes";

				cell = new PdfPCell(new Phrase("J&k", normal1));
				cell.setHorizontalAlignment(Element.ALIGN_LEFT);
				personalDetail_table.addCell(cell);

				cell = new PdfPCell(new Phrase(isJk, normal1));
				cell.setHorizontalAlignment(Element.ALIGN_LEFT);
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

			cell = new PdfPCell(new Phrase(currencyFormat.format(Double
					.parseDouble(obj.getStringWithout_E(Double
							.valueOf(basicCoverYearlyPremium)))), normal1));
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			basicCover_table.addCell(cell);

			// if (aciRiderStatus.equalsIgnoreCase("true")) {
			//
			// // 2 row
			// cell = new PdfPCell(new Phrase(
			// " Accelerated Critical Illness Cover Option ", normal1));
			// cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			// basicCover_table.addCell(cell);
			//
			// cell = new PdfPCell(new Phrase("" + aciTerm, normal1));
			// cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			// basicCover_table.addCell(cell);
			//
			// // changes done by amit - 25-3-2015
			// cell = new PdfPCell(new Phrase(currencyFormat.format(Integer
			// .parseInt(aciSA)), normal1));
			// cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			// basicCover_table.addCell(cell);
			//
			// cell = new PdfPCell(new Phrase(currencyFormat.format(Double
			// .parseDouble(prsObj.parseXmlTag(output, "AccCIPrem"))),
			// normal1));
			// cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			// basicCover_table.addCell(cell);
			//
			// }

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

				cell = new PdfPCell(new Phrase(currencyFormat.format(Double
						.parseDouble(prsObj.parseXmlTag(output, "ADBPrem"))),
						normal1));
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				basicCover_table.addCell(cell);

            } else {
                // 2 row
                cell = new PdfPCell(new Phrase(
                        " Accidental Death Benefit Rider (UIN: 111B015V03)",
                        normal1));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                basicCover_table.addCell(cell);

                cell = new PdfPCell(new Phrase("0", normal1));
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                basicCover_table.addCell(cell);

                // changes done by amit - 25-3-2015
                cell = new PdfPCell(new Phrase("0", normal1));
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                basicCover_table.addCell(cell);

                cell = new PdfPCell(new Phrase("0",
                        normal1));
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

				cell = new PdfPCell(new Phrase(currencyFormat.format(Double
						.parseDouble(prsObj.parseXmlTag(output, "ATPDPrem"))),
						normal1));
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				basicCover_table.addCell(cell);

            } else {
                // 2 row
                cell = new PdfPCell(
                        new Phrase(
                                " Accidental Total & Permanent Disability Benefit Rider (UIN: 111B016V03)",
                                normal1));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                basicCover_table.addCell(cell);

                cell = new PdfPCell(new Phrase("0", normal1));
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                basicCover_table.addCell(cell);

                // changes done by amit - 25-3-2015
                cell = new PdfPCell(new Phrase("0", normal1));
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                basicCover_table.addCell(cell);

                cell = new PdfPCell(new Phrase("0", normal1));
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                basicCover_table.addCell(cell);

			}

			// Table here

            // PdfPTable FY_SY_premDetail_table = new PdfPTable(6);
            // FY_SY_premDetail_table.setWidths(new float[] { 5f, 5f, 5f, 5f,
            // 5f,
            // 5f });
            PdfPTable FY_SY_premDetail_table = new PdfPTable(4);
            FY_SY_premDetail_table.setWidths(new float[]{5f, 5f, 5f, 5f});
			FY_SY_premDetail_table.setWidthPercentage(100f);
			FY_SY_premDetail_table.setHorizontalAlignment(Element.ALIGN_LEFT);

			// 1st row
			/*cell = new PdfPCell(new Phrase("Total Premium for Base Product",
					small_bold));
			cell.setColspan(7);
			cell.setBorder(Rectangle.BOTTOM);
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			FY_SY_premDetail_table.addCell(cell);*/

			// 2 row
//			cell = new PdfPCell(new Phrase("Policy Year", small_normal));
//			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
//			FY_SY_premDetail_table.addCell(cell);

			cell = new PdfPCell(new Phrase(premPayingMode + " Installment  Premium  (Rs.)",
					small_normal));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			FY_SY_premDetail_table.addCell(cell);

            cell = new PdfPCell(new Phrase(premPayingMode + " Installment Premium with discount (Rs )", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            FY_SY_premDetail_table.addCell(cell);

			cell = new PdfPCell(new Phrase("Applicable Taxes(Rs.)", small_normal));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			FY_SY_premDetail_table.addCell(cell);



			cell = new PdfPCell(new Phrase(premPayingMode
					+ " Installment Premium with Applicable Taxes (Rs.)", small_normal));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			FY_SY_premDetail_table.addCell(cell);

			/*cell = new PdfPCell(new Phrase("First year", small_normal));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			FY_SY_premDetail_table.addCell(cell);*/

			cell = new PdfPCell(new Phrase(currencyFormat.format(Double
					.parseDouble(prsObj.parseXmlTag(output, "InstmntPrem"))),
					small_normal));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			FY_SY_premDetail_table.addCell(cell);


            cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                    .parseDouble(prsObj.parseXmlTag(output, "InstmntPremWithDisc"))),
                    small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            FY_SY_premDetail_table.addCell(cell);

			cell = new PdfPCell(new Phrase(obj.getRound(obj
					.getStringWithout_E(Double.valueOf(basicServiceTax
							.equals("") ? "0" : basicServiceTax))),
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
							.parseXmlTag(output, "InstmntPremWthST"))),
					small_normal));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			FY_SY_premDetail_table.addCell(cell);

			// ///////////////

			// Table here
			PdfPTable table1 = new PdfPTable(2);
			table1.setWidths(new float[] { 4f, 4f });
			table1.setHorizontalAlignment(Element.ALIGN_LEFT);
			table1.setWidthPercentage(100);

			/*** Added by Priyanka Warekar - 24-08-2015 -start ****/
			/*if (prsObj.parseXmlTag(output, "staffStatus").equalsIgnoreCase(
					"sbi")) {
			cell = new PdfPCell(new Phrase(premPayingMode
					+ " Installment Premium with discount", normal1));
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			table1.addCell(cell);

			cell = new PdfPCell(new Phrase(currencyFormat.format(Double
					.parseDouble(prsObj.parseXmlTag(output,
							"InstmntPremWithDisc"))), normal1));
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			table1.addCell(cell);
			}*/

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
                            "The premium calculation is after large sum assured discount, wherever applicable. ",
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
			// 1st note
			/*cell = new PdfPCell(new Phrase("*", normal1));
			cell.setBorder(Rectangle.NO_BORDER);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table2.addCell(cell);
			cell = new PdfPCell(
					new Phrase(
							"The Premium calculation shown may change if there are changes in the levies like Applicable Taxes, education or any other cess or any other statutory levies required to be collected from the policyholder.",
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
							"Tax deduction under Section 80C is available. However in case the premium exceeds 10% of the sum assured, the benefit will be limited upto 10% of the sum assured.",
							normal1));
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell.setBorder(Rectangle.NO_BORDER);
			table2.addCell(cell);
			// // 1st note
			// cell = new PdfPCell(new Phrase("*", normal1));
			// cell.setBorder(Rectangle.NO_BORDER);
			// cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			// table2.addCell(cell);
			// cell = new PdfPCell(
			// new Phrase(
			// "Tax deduction under Section 80 (D) is available for premiums paid towards Accelerated Critical Illness Benefit.",
			// normal1));
			// cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			// cell.setBorder(Rectangle.NO_BORDER);
			// table2.addCell(cell);

			// 1st note
			cell = new PdfPCell(new Phrase("*", normal1));
			cell.setBorder(Rectangle.NO_BORDER);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table2.addCell(cell);
			cell = new PdfPCell(
					new Phrase(
                            "Tax exemption under Section 10(10D) is available, subject to the premium not exceeding 10% of the sum assured in any of the years during the term of the policy. However, death proceeds are completely exempt",
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
							"Tax benefits, are as per the provisions of the Income Tax laws & are subject to change from time to time. Please consult your tax advisor for details.",
							normal1));
			cell.setHorizontalAlignment(Element.ALIGN_LEFT
					| Element.ALIGN_JUSTIFIED);
			cell.setBorder(Rectangle.NO_BORDER);
			table2.addCell(cell);



		/*	cell = new PdfPCell(new Phrase("*", normal1));
			cell.setBorder(Rectangle.NO_BORDER);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table2.addCell(cell);
			cell = new PdfPCell(
					new Phrase(
							"The above BI is subject to payment of stipulated premiums on due date",
							normal1));
			cell.setHorizontalAlignment(Element.ALIGN_LEFT
					| Element.ALIGN_JUSTIFIED);
			cell.setBorder(Rectangle.NO_BORDER);
            table2.addCell(cell);*/

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
					new Phrase("IA/CIF/SP/Signature of Marketing official & Company Seal Signature : ",
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
			document.add(table1);

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
			/*PdfPTable BI_Pdftable26 = new PdfPTable(1);
			BI_Pdftable26.setWidthPercentage(100);*/
			// CR_table6.setWidths(columnWidths_26);
			/*PdfPCell BI_Pdftable26_cell1 = new PdfPCell(
					new Paragraph(
							"I, "
									+ name_of_person
									+ "     having received the information with respect to the above, have understood the above statement before entering into the contract.",
							small_bold));

			BI_Pdftable26_cell1.setPadding(5);

			BI_Pdftable26.addCell(BI_Pdftable26_cell1);
			document.add(BI_Pdftable26);
			document.add(BI_Pdftable26_cell1);*/
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
			PdfPTable BI_PdftableMarketing = new PdfPTable(1);
			BI_PdftableMarketing.setWidthPercentage(100);

			if (!bankUserType.equalsIgnoreCase("Y")) {
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
			// document.add(table4);

			document.close();

			// return true;
		} catch (Exception e) {
			e.printStackTrace();

		}

	}

	// public void MailPDF() {
	//
	// /* to send pdf to customer */
	// File mypath;
	// Calendar present_date = Calendar.getInstance();
	// mDay = present_date.get(Calendar.DAY_OF_MONTH);
	// mMonth = present_date.get(Calendar.MONTH);
	// mYear = present_date.get(Calendar.YEAR);
	//
	// String CurrentDate = mDay + "-" + (mMonth + 1) + "-" + mYear;
	//
	// ProposerEmailId = edt_saral_shield_Email_id.getText().toString();
	// String proposer_name = "";
	//
	// if (proposer_Is_Same_As_Life_Assured.equalsIgnoreCase("y")) {
	// proposer_name = name_of_life_assured;
	// } else {
	// proposer_name = name_of_proposer;
	// }
	//
	// M_ChannelDetails list_channel_detail = db.getChannelDetail(sr_Code);
	//
	// // Email - 1: Quotation No & BI
	//
	// // below value come from database
	// String strProposerEmailId = ProposerEmailId;
	// String strPlanName = planName;
	// String strFullName = proposer_name;
	// String strDate = CurrentDate;
	// // String strQuatationNo = ProductHomePageActivity.quotation_Number;
	// String strAgentName = list_channel_detail.getName();
	// String strAgentCode = list_channel_detail.getCode();
	// String strAgentMobileNo = list_channel_detail.getMobile_No();
	// String strBranchName = list_channel_detail.getBranchName();
	// String LicenceNumber = list_channel_detail.getLicense_no();
	// String strBranchAddress = "";
	// String strAgentEmailId = sr_Code;
	// String strLicense_no = LicenceNumber;
	//
	// String emailBody = "";
	// if (sr_Code.contains("IA")) {
	//
	// emailBody = "Dear "
	// + strFullName
	// + ","
	// + "\n"
	// + "\n"
	// + "We thank you for your interest in the SBI Life - "
	// + Html.fromHtml("<b>" + strPlanName + "</b>")
	// + " plan."
	// + "A e Quote has been generated as per the information provided by you."
	// + "\n"
	// + "Quotation reference number is "
	// + QuatationNumber
	// + "."
	// + "Dated "
	// + strDate
	// + "\n"
	// +
	// "Based on the inputs submitted by you, please find attached a Benefit Illustrator - "
	// + "\n"
	// + "Your  Sales Representative Details"
	// + "\n"
	// + "Name: "
	// + Html.fromHtml("<b>" + strAgentName + "</b>")
	// + "\n"
	// + "License No: "
	// + strLicense_no
	// + "\n"
	// + "Sales Representative Code: "
	// + Html.fromHtml("<b>" + strAgentCode + "</b>")
	// + "\n"
	// + "Mobile No: "
	// + Html.fromHtml("<b>" + strAgentMobileNo + "</b>")
	// + "\n"
	// + "Branch Name "
	// + strBranchName
	// + ""
	// + strBranchAddress
	// + "\n"
	// + "\n"
	// +
	// "You are requested to get in touch with your Insurance Advisor to complete the policy purchase at the earliest."
	// + "\n"
	// +
	// "Please note that this Quotation No. is valid for a period of 30 days the date of generation."
	// + "\n"
	// + "\n"
	// + "For any assistance:"
	// + "\n"
	// + "Toll Free No.1800 267 9090 (Between 9:00 am to 9:00 pm)"
	// + "\n"
	// + "Email :info@sbilife.co.in"
	// + "\n"
	// + "\n"
	// + "Regards,"
	// + "\n"
	// + "SBI Life Insurance Company Ltd"
	// + "\n"
	// +
	// "1) Most Trusted Private Life Insurance Brand 2013' thrice in a row, by ET Brand Equity"
	// + "\n"
	// + "2) SBI Life wins at Global Performance Excellence Award 2013"
	// + "\n"
	// +
	// "3) Claim Settlement Ratio of 94.41%, as per SBI Life Annual Report for FY 2012 - 13"
	// + "\n"
	// +
	// "4) 'The Most Admired Life Insurance Company in the Private Sector' by ABP BFSI Awards 2014"
	// + "\n"
	// + "\n"
	// +
	// "Document Checklist: Keep the following self attested scanned copy ready"
	// + "\n"
	// + "Identity Proof : PAN Card, Driving Licence, Aadhar Card, Passport"
	// + "\n"
	// +
	// "Age Proof (any one) : PAN Card, Birth Certificate, School / College Certificate, Passport, Driving License, Others"
	// + "\n"
	// +
	// "Address Proof (any one) : Bank Account Statement, Electricity Bill, Letter from Recognized Public Authority, Ration Card, Telephone Bill"
	// + "\n"
	// +
	// "Income Proof (any one) : Employer Certificate, Income Tax Returns, Assessment Order, Others"
	// + "\n"
	// +
	// "Please Keep Your Internet Banking / Credit Card / Debit Card details for making the Premium Payment."
	// + "\n"
	// + "\n"
	// + "Insurance is the subject matter of solicitation."
	//
	// + "\n"
	// + "\n"
	// +
	// "This is a system generated mail. Please do not reply to this message.";
	// } else {
	//
	// String strHeader =
	// "Your Certified Insurance Facilitator (CIF) details are:";
	// String Hname = "Name:";
	// String Hcode = "CIF Code:";
	// String Hmobile = "Mobile No:";
	// String Haddres = "SBI Branch Name";
	// String strCall = "Call us on our ";
	// String strEmail = "Email us at ";
	// String strDOC_Check_List = "Document Checklist:";
	// String strIdenty = "Identity Proof (any one) : ";
	// String strAge = "Age Proof (any one) : ";
	// String strAddress = "Address Proof (any one) : ";
	// String strIncome = "Income Proof (any one) : ";
	// String strInternet =
	// "Keep Your Internet Banking / Credit Card / Debit Card /Bank details ";
	// String strSystem =
	// "This is a system generated mail. Please do not reply to this message.";
	//
	// emailBody = "Dear "
	// + strFullName
	// + ","
	// + "\n"
	// + "\n"
	// + "We thank you for your interest in the SBI Life - "
	// + Html.fromHtml("<b>" + strPlanName + "</b>")
	// + "."
	// + "A Quote has been generated as per the information provided by you."
	// + "\n"
	// + "Please note that your Quotation reference number is "
	// + QuatationNumber
	// + ","
	// + "dated "
	// + Html.fromHtml("<b>" + strDate + "</b>")
	// + "."
	// + "\n"
	// +
	// "Based upon the inputs submitted by you, please find attached a Benefit Illustrator for your reference. "
	// + "\n"
	// + Html.fromHtml("<b><u>" + strHeader + "<u></b>")
	// + "\n"
	// + Html.fromHtml("<b>" + Hname + "</b>")
	// + strAgentName
	// + "\n"
	// + Html.fromHtml("<b>" + Hcode + "</b>")
	// + sr_Code
	// + "\n"
	// + Html.fromHtml("<b>" + Hmobile + "</b>")
	// + strAgentMobileNo
	// + "\n"
	// + Html.fromHtml("<b>" + Haddres + "</b>")
	// + strBranchName
	// + "\n"
	// + "\n"
	// +
	// "You are requested to get in touch with your CIF to complete the policy purchase at the earliest."
	// + "\n"
	// +
	// "Please note that this quotation is valid for a period of 30 days the date of generation,subject to no changes in the input values/Age or Regulations."
	// + "\n"
	// + "\n"
	// + "For any other assistance:"
	// + "\n"
	// + Html.fromHtml("<b>" + strCall + "</b>")
	// + "Toll Free No.1800 267 9090 (Between 9:00 am to 9:00 pm)"
	// + "\n"
	// + Html.fromHtml("<b>" + strEmail + "</b>")
	// + "info@sbilife.co.in"
	// + "\n"
	// + "\n"
	// + "Regards,"
	// + "\n"
	// + "SBI Life Insurance Company ltd"
	// + "\n"
	// +
	// "Registered & Corporate Office: SBI Life Insurance Co. Ltd, Natraj, M.V. Road & Western Express Highway Junction, Andheri (East), Mumbai - 400 069."
	// +
	// "IRDAI Registration no. 111 issued on 29th March 2001.CIN: L99999MH2000PLC129113, Website : www.sbilife.co.in , Insurance is the subject matter of solicitation."
	// + "\n"
	// + "\n"
	// + Html.fromHtml("<b><u>" + strDOC_Check_List + "<u></b>")
	// + "\n"
	// + "\n"
	// + "Please keep the self attested copies of the following ready:-"
	// + "\n"
	// + "\n"
	// + Html.fromHtml("<b>" + strIdenty + "</b>")
	// + "PAN Card/ Driving Licence/ Aadhar Card/ Passport"
	// + "\n"
	// + Html.fromHtml("<b>" + strAge + "</b>")
	// +
	// " PAN Card/ Birth Certificate, School / College Certificate/ Passport/ Driving License/ Others"
	// + "\n"
	// + Html.fromHtml("<b>" + strAddress + "</b>")
	// +
	// " Bank Account Statement/ Electricity Bill/ Letter from Recognized Public Authority/ Ration Card/ Telephone Bill"
	// + "\n"
	// + Html.fromHtml("<b>" + strIncome + "</b>")
	// + " Employer Certificate/ Income Tax Returns/ Assessment Order/ Others"
	// + "\n" + Html.fromHtml("<b>" + strInternet + "</b>")
	// + "ready for making the Premium Payment." + "\n" + "\n"
	// + "Insurance is the subject matter of solicitation." + "\n"
	// + "\n" + strSystem;
	//
	// }
	// String extStorageDirectory = Environment.getExternalStorageDirectory()
	// .toString();
	// File folder = new File(extStorageDirectory + direct + "/");
	//
	// mypath = new File(folder, QuatationNumber + "P01.pdf");
	//
	// String filelocation = mypath.toString();
	//
	// // try {
	// //
	// // Mail m = new Mail("sbilconnectlife@sbi-life.com", "sky@12345");
	// //
	// // String[] toArr = { ProposerEmailId };
	// // m.setTo(toArr);
	// // m.setFrom("sbilconnectlife@sbi-life.com");
	// // m.setSubject(strPlanName + " Benefit Illustrator");
	// // m.setBody(emailBody);
	// //
	// // try {
	// // m.addAttachment(filelocation);
	// //
	// // if (m.send()) {
	// // Toast.makeText(this, "Email was sent successfully.",
	// // Toast.LENGTH_LONG).show();
	// // } else {
	// // Toast.makeText(this, "Email was not sent.",
	// // Toast.LENGTH_LONG).show();
	// // }
	// // } catch (Exception e) {
	// // // Toast.makeText(MailApp.this,
	// // // "There was a problem sending the email.",
	// // // Toast.LENGTH_LONG).show();
	// // Log.e("MailApp", "Could not send email", e);
	// // }
	// try {
	// sendMail(strProposerEmailId, "SBI Life Quotation No "
	// + QuatationNumber + " for your " + strPlanName, emailBody,
	// mypath);
	//
	// Toast.makeText(this,
	// "Benefit Illustrator PDF Has Been Sent to Your Email Id",
	// Toast.LENGTH_SHORT).show();
	// } catch (Exception e) {
	// Toast.makeText(BI_SaralShieldActivity.this,
	// "There was a problem sending the email.", Toast.LENGTH_LONG)
	// .show();
	// Log.e("MailApp", "Could not send email", e);
	// }
	//
	// }

	// public boolean getValueFromDatabase() {
	// // retrieving data from database
	// boolean flag = false;
	// List<M_Benefit_Illustration_Detail> data = db
	// .getBIDetail(QuatationNumber);
	// if (data.size() > 0) {
	// int i = 0;
	// output = data.get(i).getOutput();
	// input = data.get(i).getInput();
	// proposal_no = data.get(i).getProposal_no();
	// name_of_life_assured = data.get(i).getName_of_lifeAssured();
	// lifeAssured_date_of_birth = data.get(i)
	// .getLife_assured_date_of_birth();
	// proposer_date_of_birth = data.get(i).getProposer_dob();
	//
	// proposer_Is_Same_As_Life_Assured = data.get(i)
	// .getProposer_Same_As_Life_Assured();
	// proposer_Title = data.get(i).getProposer_Title();
	//
	// proposer_First_Name = data.get(i).getProposer_FirstName();
	// proposer_Middle_Name = data.get(i).getProposer_MiddleName();
	// proposer_Last_Name = data.get(i).getProposer_LastName();
	// lifeAssured_Title = data.get(i).getLifeAssured_Title();
	// lifeAssured_First_Name = data.get(i).getLifeAssured_FirstName();
	// lifeAssured_Middle_Name = data.get(i).getLifeAssured_MiddleName();
	// lifeAssured_Last_Name = data.get(i).getLifeAssured_LastName();
	// product_name = data.get(i).getProduct_name();
	// proposer_Backdating_WishToBackDate_Policy = data.get(i)
	// .getWish_to_backdate_policy();
	// proposer_Backdating_BackDate = data.get(i).getBackdate();
	//
	// name_of_proposer = data.get(i).getName_of_proposer();
	// place1 = data.get(i).getPlace1();
	// place2 = data.get(i).getPlace2();
	// date1 = data.get(i).getDate1();
	// date2 = data.get(i).getDate2();
	// agent_sign = data.get(i).getAgent_sign();
	// proposer_sign = data.get(i).getProposer_sign();
	// flg_needAnalyis = data.get(i).getneedAnalyisDone();
	// // name_of_person = data.get(i).getName_of_person();
	//
	// flag = true;
	//
	// }
	// return flag;
	// }

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

	// Validation

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
								setFocusable(edt_plandetail_loan_loan_account_no);
								edt_plandetail_loan_loan_account_no
										.requestFocus();

							} else if (financingInstituteName.equals("")) {
								setFocusable(edt_plandetail_loan_name_of_institute);
								edt_plandetail_loan_name_of_institute
										.requestFocus();

							} else if (loanCategory.equals("")) {
								setFocusable(edt_plandetail_loan_loan_category);
								edt_plandetail_loan_loan_category
										.requestFocus();

							} else if (sumAssuredOutStanding.equals("")) {
								setFocusable(edt_plandetail_loan_loan_protection_sum_assured);
								edt_plandetail_loan_loan_protection_sum_assured
										.requestFocus();

							} else if (balanceLoanTenure.equals("")) {
								setFocusable(edt_plandetail_loan_loan_tenure);
								edt_plandetail_loan_loan_tenure.requestFocus();

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
									setFocusable(spnr_bi_saral_shield_life_assured_title);
									spnr_bi_saral_shield_life_assured_title
											.requestFocus();
								} else if (lifeAssured_First_Name.equals("")) {
									setFocusable(edt_bi_saral_shield_life_assured_first_name);
									edt_bi_saral_shield_life_assured_first_name
											.requestFocus();
								} else {
									setFocusable(edt_bi_saral_shield_life_assured_last_name);
									edt_bi_saral_shield_life_assured_last_name
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
                                setFocusable(spnr_bi_saral_shield_life_assured_title);
                                spnr_bi_saral_shield_life_assured_title
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
                                setFocusable(spnr_bi_saral_shield_life_assured_title);
                                spnr_bi_saral_shield_life_assured_title
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
                                setFocusable(spnr_bi_saral_shield_life_assured_title);
                                spnr_bi_saral_shield_life_assured_title
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
								setFocusable(btn_bi_saral_shield_life_assured_date);
								btn_bi_saral_shield_life_assured_date
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

		if (edt_saral_shield_contact_no.getText().toString().equals("")) {
			commonMethods.dialogWarning(context,"Please Fill  Mobile No", true);
			edt_saral_shield_contact_no.requestFocus();
			return false;
		} else if (edt_saral_shield_contact_no.getText().toString().length() != 10) {
			commonMethods.dialogWarning(context,"Please Fill 10 Digit Mobile No", true);
			edt_saral_shield_contact_no.requestFocus();
			return false;
		}

		/*else if (emailId.equals("")) {
			commonMethods.dialogWarning(context,"Please Fill Email Id", true);
			edt_saral_shield_Email_id.requestFocus();
			return false;

		}

		else if (ConfirmEmailId.equals("")) {

			commonMethods.dialogWarning(context,"Please Fill Confirm Email Id", true);
			edt_saral_shield_ConfirmEmail_id.requestFocus();
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
					edt_saral_shield_ConfirmEmail_id.requestFocus();
					return false;
				} else if (!ConfirmEmailId.equals(emailId)) {
					commonMethods.dialogWarning(context,"Email Id Does Not Match", true);
					return false;
				}

				return true;
			} else {
				commonMethods.dialogWarning(context,"Please Fill Valid Email Id", true);
				edt_saral_shield_Email_id.requestFocus();
				return false;
			}
		}

		else if (!ConfirmEmailId.equals("")) {

			email_id_validation(ConfirmEmailId);
			if (validationFla1) {

				if (emailId.equals("")) {
					commonMethods.dialogWarning(context,"Please Fill Email Id", true);
					edt_saral_shield_Email_id.requestFocus();
					return false;

				}

				return true;
			} else {
				commonMethods.dialogWarning(context,"Please Fill Valid Confirm Email Id", true);
				edt_saral_shield_ConfirmEmail_id.requestFocus();
				return false;
			}
		}

		else {
			return true;
		}
	}

	// Validate Sum Assured
	private boolean valSA() {

		StringBuilder error = new StringBuilder();
		double minRiderLimit, maxRiderLimit;
		if (edt_bi_saral_shield_sum_assured_amount.getText().toString()
				.equals("")
				|| Double.parseDouble(edt_bi_saral_shield_sum_assured_amount
				.getText().toString()) > 2400000
				|| Double.parseDouble(edt_bi_saral_shield_sum_assured_amount
				.getText().toString()) < 750000) {
			error.append("Enter Basic Sum Assured between Rs. 7,50,000 and Rs. 24,00,000");

		} else if (Double.parseDouble(edt_bi_saral_shield_sum_assured_amount
				.getText().toString()) % 50000 != 0) {
			error.append("Sum assured should be multiple of 50,000");
        } else if (!(edt_plandetail_loan_loan_protection_sum_assured
                .getText().toString().equalsIgnoreCase("")) && Double.parseDouble(edt_plandetail_loan_loan_protection_sum_assured
                .getText().toString()) % 50000 != 0) {
            error.append("Sum assured/Outstanding loan amount should be multiple of 50,000");
		} else if (loandetails) {

			if (Double.parseDouble(edt_bi_saral_shield_sum_assured_amount
					.getText().toString()) > Double
					.parseDouble(edt_plandetail_loan_loan_protection_sum_assured
							.getText().toString())) {
				error.append("Sum assured should not be greater than outstanding loan amount");
			} else if ((Integer.parseInt(edt_plandetail_loan_loan_tenure
					.getText().toString()) / 12) < Integer
					.parseInt(spnr_bi_saral_shield_policyterm.getSelectedItem()
							.toString())) {
				error.append("Policy term should not be greater then outstanding term");
			}

		} else {

			if (cb_bi_saral_shield_adb_rider.isChecked()) {
				minRiderLimit = 25000;
				maxRiderLimit = Math.min(2400000, Double
						.parseDouble(edt_bi_saral_shield_sum_assured_amount
								.getText().toString()));
				if (edt_bi_saral_shield_adb_rider_sum_assured.getText()
						.toString().equals("")) {
					error.append("\nPlease Enter Sum assured for Accidental Death Benefit Rider");
				} else if (Double
						.parseDouble(edt_bi_saral_shield_adb_rider_sum_assured
								.getText().toString()) % 1000 != 0) {
					error.append("\nEnter Sum assured for Accidental Death Benefit Rider in multiples of 1,000");
				} else if (Double
						.parseDouble(edt_bi_saral_shield_adb_rider_sum_assured
								.getText().toString()) < minRiderLimit
						|| Double
						.parseDouble(edt_bi_saral_shield_adb_rider_sum_assured
								.getText().toString()) > maxRiderLimit)
					error.append("\nEnter Sum assured for Accidental Death Benefit Rider between "
							+ currencyFormat.format(minRiderLimit)
							+ " and "
							+ currencyFormat.format(maxRiderLimit));
			}
			if (cb_bi_saral_shield_atpdb_rider.isChecked()) {
				minRiderLimit = 25000;
				maxRiderLimit = Math.min(2400000, Double
						.parseDouble(edt_bi_saral_shield_sum_assured_amount
								.getText().toString()));
				if (edt_bi_saral_shield_atpbd_rider_sum_assured.getText()
						.toString().equals("")) {
					error.append("\nPlease Enter Sum assured for Accidental Total and Permanent Disability Benefit Rider");
				} else if (Double
						.parseDouble(edt_bi_saral_shield_atpbd_rider_sum_assured
								.getText().toString()) % 1000 != 0) {
					error.append("\nEnter Sum assured for Accidental Total and Permanent Disability Benefit Rider in multiples of 1,000");
				} else if (Double
						.parseDouble(edt_bi_saral_shield_atpbd_rider_sum_assured
								.getText().toString()) < minRiderLimit
						|| Double
						.parseDouble(edt_bi_saral_shield_atpbd_rider_sum_assured
								.getText().toString()) > maxRiderLimit)
					error.append("\nEnter Sum assured for Accidental Total and Permenent Disability Benefit Rider between "
							+ currencyFormat.format(minRiderLimit)
							+ " and "
							+ currencyFormat.format(maxRiderLimit));
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

	// Validate Policy term
	private boolean valTerm() {
		int maxLimit = Math.min(30, 65 - Integer
				.parseInt(edt_bi_saral_shield_life_assured_age.getText()
						.toString()));
		if (Integer.parseInt(spnr_bi_saral_shield_policyterm.getSelectedItem()
				.toString()) > maxLimit) {
			showAlert
					.setMessage("Minimum Term limit is 5 and maximum limit is "
							+ maxLimit);
			showAlert.setNeutralButton("OK",
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {
							spnr_bi_saral_shield_policyterm.setSelection(0);
						}
					});
			showAlert.show();
			return false;
		}
		return true;
	}

	// Validate rider term
	private boolean valTermRider() {

		int maxLimit = Integer.parseInt(spnr_bi_saral_shield_policyterm
				.getSelectedItem().toString());
		StringBuilder error = new StringBuilder();

		if (cb_bi_saral_shield_adb_rider.isChecked()
				&& Integer.parseInt(spnr_bi_saral_shield_adb_rider_term
				.getSelectedItem().toString()) > maxLimit) {
			spnr_bi_saral_shield_adb_rider_term.setSelection(0);
			error.append("Maximum term for Accidental Death Benefit Rider is "
					+ maxLimit + " years");
		}

		if (cb_bi_saral_shield_atpdb_rider.isChecked()
				&& Integer.parseInt(spnr_bi_saral_shield_atpdb_rider_term
				.getSelectedItem().toString()) > maxLimit) {
			spnr_bi_saral_shield_atpdb_rider_term.setSelection(0);
			error.append("Maximum term for Accidental Total and Permenent Disability Benefit Rider is "
					+ maxLimit + " years");
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
		}
		return true;
	}

	private String getformatedThousandString(int number) {
		return NumberFormat.getNumberInstance(Locale.US)
				.format(number);
	}

	private void email_id_validation(String email_id) {

		String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
		Pattern pattern = Pattern.compile(EMAIL_PATTERN);
		Matcher matcher = pattern.matcher(email_id);
		if (!(matcher.matches())) {
			edt_saral_shield_Email_id
					.setError("Please provide the correct email address");
			validationFla1 = false;
		} else if ((matcher.matches())) {
			validationFla1 = true;
		}
	}

	public int Datecomparison() {
		final Calendar c = Calendar.getInstance();
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH);
		mDay = c.get(Calendar.DAY_OF_MONTH);

		String mont = (mMonth + 1 < 10 ? "0" : "") + (mMonth + 1);
		String day = (mDay < 10 ? "0" : "") + mDay;

		String todaysDate = mont + "-" + day + "-" + mYear;

		return firstEmiDate.compareTo(todaysDate);
	}



	private void mobile_validation(String number) {
		if ((number.length() != 10)) {
			edt_saral_shield_contact_no
					.setError("Please provide correct 10-digit mobile number");
			validationFlag3 = false;
		} else if ((number.length() == 10)) {
			validationFlag3 = true;
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

	private void Date() {
		Calendar c = Calendar.getInstance();
		System.out.println("Current time => " + c.getTime());

		c.add(Calendar.DATE, 30);
	}
}
