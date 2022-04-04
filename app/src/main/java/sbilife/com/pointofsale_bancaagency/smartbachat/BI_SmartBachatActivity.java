package sbilife.com.pointofsale_bancaagency.smartbachat;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
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
import java.io.UnsupportedEncodingException;
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
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

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
public class BI_SmartBachatActivity extends AppCompatActivity implements
		OnEditorActionListener {

	private NeedAnalysisBIService NABIObj;
	private NA_CBI_bean na_cbi_bean;
	private File needAnalysispath, newFile;
	private String agentcode, agentMobile, agentEmail, userType;
	private int needAnalysis_flag = 0;
	private String na_input = null;
	private String na_output = null;
	private DatabaseHelper dbHelper;
	private File mypath;

	private String QuatationNumber;
	private String planName = "";
	private String sr_Code = "";

	private Spinner spinner_select_plan, spinner_age, spinner_selGender,spinner_proposer_selGender,
			spinner_policyterm, spinner_premium_payment_term,
			spinner_premiumfreq;
	private EditText et_sumassured, et_adtpd_benifit;
	private TableRow tableRow_adtpd;
	private CheckBox cb_staffdisc, cb_JKResident;
	private final SmartBachatBean smartbachatbean = new SmartBachatBean();
	private final CommonForAllProd commonforall = new CommonForAllProd();
	private final SmartBachatBusinessLogic smartbachatbusinesslogic = new SmartBachatBusinessLogic();

	private RadioButton rb_smart_bachat_backdating_yes,
			rb_smart_bachat_backdating_no;
	private LinearLayout ll_backdating1;
	private Button btn_smart_bachat_backdatingdate;

	private Dialog d;
	/* Basic Details */
	private String mobileNo = "";
	private String emailId = "";
	private String ConfirmEmailId = "";
	private boolean validationFla1 = false;
	private EditText edt_proposerdetail_basicdetail_contact_no,
			edt_proposerdetail_basicdetail_Email_id,
			edt_proposerdetail_basicdetail_ConfirmEmail_id;
	private Button btn_bi_smart_bachat_life_assured_date;
	private Spinner spnr_bi_smart_bachat_life_assured_title;
	private EditText edt_bi_smart_bachat_life_assured_first_name,
			edt_bi_smart_bachat_life_assured_middle_name,
			edt_bi_smart_bachat_life_assured_last_name;
	private  final int SIGNATURE_ACTIVITY = 1;
	private  final int DATE_DIALOG_ID = 1;
	private  int DIALOG_ID;
	private int mYear;
	private int mMonth;
	private int mDay;

	// newDBHelper db;
	private CommonForAllProd obj;
	private ParseXML prsObj;
	private String ProposerEmailId = "";
	private String latestImage = "";
	private String output = "";

	private final String premiumPaymentMode = "";

	private String plan = "";

	private String policy_term = "";

	private String sum_assured = "";

	private String lifeAssured_Title = "", lifeAssured_First_Name = "",
			lifeAssured_Middle_Name = "", lifeAssured_Last_Name = "",
			name_of_life_assured = "";
	private String lifeAssured_date_of_birth = "";

	private String proposer_Title = "", proposer_First_Name = "",
			proposer_Middle_Name = "", proposer_Last_Name = "",
			name_of_proposer = "", proposer_date_of_birth = "",
			proposer_Is_Same_As_Life_Assured = "Y",lifeAssuredAge = "";// product_name = "",,

	private String proposer_Backdating_WishToBackDate_Policy = "";
	private String proposer_Backdating_BackDate = "";

	private String name_of_person = "";
	private String place2 = "";
	private String date1 = "";
	private String date2 = "";
	private String agent_sign = "";
	private String proposer_sign = "";
	private StringBuilder inputVal, retVal, bussIll;
	private AlertDialog.Builder showAlert;

	private ImageButton Ibtn_signatureofMarketing;
	private ImageButton Ibtn_signatureofPolicyHolders;

	private Button btn_MarketingOfficalDate;
	private Button btn_PolicyholderDate;
	private List<M_BI_SmartBachatNew_Adapter> list_data;

	private String basicplustax = "";
	private DecimalFormat currencyFormat;// , decimalcurrencyFormat;

	private TableRow
			tr_smart_bachat_proposer_detail2;
	private Spinner spnr_bi_smart_bachat_proposer_title;
	private EditText edt_bi_smart_bachat_proposer_last_name,
			edt_bi_smart_bachat_proposer_middle_name,
			edt_bi_smart_bachat_proposer_first_name;
	private Button btn_bi_smart_bachat_proposer_date;
	private String BackdatingInt = "";// ProposerAge = "",

	private String bankUserType = "",mode = "";
	/* parivartan changes */
	private String Check = "";
	private CommonMethods commonMethods;
	private StorageUtils mStorageUtils;
	private Context mContext;
	private ImageButton imageButtonSmartBachatProposerPhotograph;

	private LinearLayout linearlayoutThirdpartySignature,tr_smart_bachat_proposer_detail1;
	private LinearLayout linearlayoutAppointeeSignature;
	private ImageButton Ibtn_signatureofThirdParty, Ibtn_signatureofAppointee,
			Ibtn_signatureofLifeAssured;
	private String thirdPartySign = "", appointeeSign = "",
			proposerAsLifeAssuredSign = "";
	private String product_Code, product_UIN, product_cateogory, product_type;
	private Bitmap photoBitmap;

	/* end */
	private CheckBox cb_kerladisc;
	private String AD_TPD_sum_assured;
	private String staffdiscount;
	private String ProposerAge;
	private String age_entry;
	private String basicPremWithoutDisc,IntsallmentPremATPDB_FirstYear,BasicInstallmentPremSecondYear,modalprem3,IntsallmentPremATPDB_SecondYear;
	private String Smart_bachat_sum_assured_on_death,basicprem,modalprem1,BasicInstallmentPremFirstYear,modalprem2,gender;
	private DecimalFormat decimalcurrencyFormat;
	private String gender_proposer,Total_installment_premium_with_tax_second_year,adtpdRiderprem,Total_installment_premium_with_tax_first_year,
			Total_installment_premium_without_tax,Base_premium_with_tax_first_year,modalprem,premium_paying_frequency,adtpdbenfits,premPayTerm,
			isJkResident;
	private String BackDateinterest;
	private String staffRebate;
	private String MinesOccuInterest;
	private String servicetax_MinesOccuInterest;
	private String BackDateinterestwithGST;
	private int sumassured;
	private String formattedDate;
	private String ExpiredDate;
	private String str_kerla_discount = "No";
	private SmartBachatProperties prop = new SmartBachatProperties();
	private String staffStatus;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.bi_smartbachatmain);
		//getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);

		commonMethods = new CommonMethods();
		mStorageUtils = new StorageUtils();
		mContext = this;
		list_data = new ArrayList<M_BI_SmartBachatNew_Adapter>();

		commonMethods.setApplicationToolbarMenu(this,
				getString(R.string.app_name));

		dbHelper = new DatabaseHelper(getApplicationContext());
		NABIObj = new NeedAnalysisBIService(this);
		prsObj = new ParseXML();
		currencyFormat = new DecimalFormat("##,##,##,###");
		Intent intent = getIntent();
		obj = new CommonForAllProd();

		//Added Tushar Kadam Kerla Applicable Tax 7-Jun-2019
		TableRow tablerowKerlaDiscount = findViewById(R.id.tablerowKerlaDiscount);
		cb_kerladisc =  findViewById(R.id.cb_kerladisc);
		commonMethods.setKerlaDiscount(mContext, tablerowKerlaDiscount, cb_kerladisc);
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
					planName = "Smart Bachat";
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
		list_data = new ArrayList<>();
		showAlert = new AlertDialog.Builder(this);
		initialiseDate();

		prsObj = new ParseXML();

		spinner_select_plan = findViewById(R.id.spinner_select_plan);
		spinner_age = findViewById(R.id.spinner_age);

		spinner_age.setClickable(false);
		spinner_age.setEnabled(false);

		spinner_selGender = findViewById(R.id.spinner_selGender);
		spinner_proposer_selGender = findViewById(R.id.spinner_proposer_selGender);

//		spinner_selGender.setClickable(false);
//		spinner_selGender.setEnabled(false);

		spinner_policyterm = findViewById(R.id.spinner_policyterm);
		spinner_premium_payment_term = findViewById(R.id.spinner_premium_payment_term);
		spinner_premiumfreq = findViewById(R.id.spinner_premiumfreq);

		et_sumassured = findViewById(R.id.et_sumassured);
		et_adtpd_benifit = findViewById(R.id.et_adtpd_benifit);
		et_adtpd_benifit.setClickable(false);
		et_adtpd_benifit.setEnabled(false);
		Button btnSubmit = findViewById(R.id.btnSubmit);

		tableRow_adtpd = findViewById(R.id.tableRow_adtpd);
		cb_staffdisc = findViewById(R.id.cb_staffdisc);
		cb_JKResident = findViewById(R.id.cb_JKResident);
		Button btngohome = findViewById(R.id.back);

		spnr_bi_smart_bachat_life_assured_title = findViewById(R.id.spnr_bi_smart_bachat_life_assured_title);
		edt_bi_smart_bachat_life_assured_first_name = findViewById(R.id.edt_bi_smart_bachat_life_assured_first_name);
		edt_bi_smart_bachat_life_assured_middle_name = findViewById(R.id.edt_bi_smart_bachat_life_assured_middle_name);
		edt_bi_smart_bachat_life_assured_last_name = findViewById(R.id.edt_bi_smart_bachat_life_assured_last_name);

		btn_bi_smart_bachat_life_assured_date = findViewById(R.id.btn_bi_smart_bachat_life_assured_date);

		edt_proposerdetail_basicdetail_contact_no = findViewById(R.id.edt_smart_bachat_contact_no);
		edt_proposerdetail_basicdetail_Email_id = findViewById(R.id.edt_smart_bachat_Email_id);
		edt_proposerdetail_basicdetail_ConfirmEmail_id = findViewById(R.id.edt_smart_bachat_ConfirmEmail_id);

		rb_smart_bachat_backdating_yes = findViewById(R.id.rb_smart_bachat_backdating_yes);
		rb_smart_bachat_backdating_no = findViewById(R.id.rb_smart_bachat_backdating_no);
		ll_backdating1 = findViewById(R.id.ll_backdating1);
		btn_smart_bachat_backdatingdate = findViewById(R.id.btn_smart_bachat_backdatingdate);

		// proposer
		edt_bi_smart_bachat_proposer_last_name = findViewById(R.id.edt_bi_smart_bachat_proposer_last_name);
		edt_bi_smart_bachat_proposer_middle_name = findViewById(R.id.edt_bi_smart_bachat_proposer_middle_name);
		edt_bi_smart_bachat_proposer_first_name = findViewById(R.id.edt_bi_smart_bachat_proposer_first_name);
		spnr_bi_smart_bachat_proposer_title = findViewById(R.id.spnr_bi_smart_bachat_proposer_title);
		tr_smart_bachat_proposer_detail1 = findViewById(R.id.tr_smart_bachat_proposer_detail1);
		tr_smart_bachat_proposer_detail2 = findViewById(R.id.tr_smart_bachat_proposer_detail2);
		btn_bi_smart_bachat_proposer_date = findViewById(R.id.btn_bi_smart_bachat_proposer_date);

		// String frmDashboard = ProductHomePageActivity.frmDashboard;
		// if (frmDashboard.equals("FALSE")) {
		// if (getValueFromDatabase()) {
		// // Dialog();
		// }
		// }

		commonMethods.fillSpinnerValue(mContext, spnr_bi_smart_bachat_life_assured_title,
				Arrays.asList(getResources().getStringArray(R.array.arr_select_title)));

		commonMethods.fillSpinnerValue(mContext, spnr_bi_smart_bachat_proposer_title,
				Arrays.asList(getResources().getStringArray(R.array.arr_select_title)));

		String[] ageList = new String[55];
		for (int i = 6; i <= 60; i++) {
			ageList[i - 6] = i + "";
		}
		ArrayAdapter<String> ageAdapter = new ArrayAdapter<>(
				getApplicationContext(), R.layout.spinner_item, ageList);
		ageAdapter.setDropDownViewResource(R.layout.spinner_item1);
		spinner_age.setAdapter(ageAdapter);
		ageAdapter.notifyDataSetChanged();

		String[] planList = {
				"Option A (Endowment Option)",
				"Option B (Endowment Option with in-built Accidental Death and Total Permanent Disability (AD&TPD) Benefit)" };
		ArrayAdapter<String> planAdapter = new ArrayAdapter<>(
				getApplicationContext(), R.layout.spinner_item, planList);
		planAdapter.setDropDownViewResource(R.layout.spinner_item1);
		spinner_select_plan.setAdapter(planAdapter);
		planAdapter.notifyDataSetChanged();

		String[] genderList = { "Male", "Female" ,"Third Gender"};
		ArrayAdapter<String> genderAdapter = new ArrayAdapter<>(
				getApplicationContext(), R.layout.spinner_item, genderList);
		genderAdapter.setDropDownViewResource(R.layout.spinner_item1);
		spinner_selGender.setAdapter(genderAdapter);
		spinner_proposer_selGender.setAdapter(genderAdapter);
		genderAdapter.notifyDataSetChanged();

		String[] policyterm = { "12", "13", "14", "15", "16", "17",
				"18", "19", "20", "21", "22", "23", "24", "25" };

		ArrayAdapter<String> policytermadapter = new ArrayAdapter<>(
				getApplicationContext(), R.layout.spinner_item, policyterm);
		policytermadapter.setDropDownViewResource(R.layout.spinner_item1);
		spinner_policyterm.setAdapter(policytermadapter);
		policytermadapter.notifyDataSetChanged();

		String[] premFreqList = { "Yearly", "Half-Yearly", "Quarterly",
				"Monthly" };
		ArrayAdapter<String> premFreqAdapter = new ArrayAdapter<>(
				getApplicationContext(), R.layout.spinner_item, premFreqList);
		premFreqAdapter.setDropDownViewResource(R.layout.spinner_item1);
		spinner_premiumfreq.setAdapter(premFreqAdapter);
		premFreqAdapter.notifyDataSetChanged();

		String[] premiumpayingterm = { "6", "7", "10", "15" };

		ArrayAdapter<String> premiumpayingtermadapter = new ArrayAdapter<>(
				getApplicationContext(), R.layout.spinner_item,
				premiumpayingterm);
		premiumpayingtermadapter
				.setDropDownViewResource(R.layout.spinner_item1);
		spinner_premium_payment_term.setAdapter(premiumpayingtermadapter);
		premiumpayingtermadapter.notifyDataSetChanged();

		// setBIInputGui();

		spinner_policyterm
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
											   int pos, long arg3) {

						if (pos == 0 || pos == 1 || pos == 2 ) {
							String[] premiumpayingterm = {"6", "7"};

							ArrayAdapter<String> premiumpayingtermadapter = new ArrayAdapter<String>(
									getApplicationContext(),
									R.layout.spinner_item, premiumpayingterm);
							premiumpayingtermadapter
									.setDropDownViewResource(R.layout.spinner_item1);
							spinner_premium_payment_term
									.setAdapter(premiumpayingtermadapter);
							premiumpayingtermadapter.notifyDataSetChanged();
						} else if (pos == 3 || pos == 4 || pos == 5 || pos == 6
								|| pos == 7) {

							String[] premiumpayingterm = {"6", "7", "10"};

							ArrayAdapter<String> premiumpayingtermadapter = new ArrayAdapter<String>(
									getApplicationContext(),
									R.layout.spinner_item, premiumpayingterm);
							premiumpayingtermadapter
									.setDropDownViewResource(R.layout.spinner_item1);
							spinner_premium_payment_term
									.setAdapter(premiumpayingtermadapter);
							premiumpayingtermadapter.notifyDataSetChanged();

						} else if (pos == 8 || pos == 9 || pos == 10
								|| pos == 11 || pos == 12 || pos == 13) {

							String[] premiumpayingterm = {"6", "7", "10", "15"};

							ArrayAdapter<String> premiumpayingtermadapter = new ArrayAdapter<String>(
									getApplicationContext(),
									R.layout.spinner_item, premiumpayingterm);
							premiumpayingtermadapter
									.setDropDownViewResource(R.layout.spinner_item1);
							spinner_premium_payment_term
									.setAdapter(premiumpayingtermadapter);
							premiumpayingtermadapter.notifyDataSetChanged();

						}

						spinner_policyterm.setFocusable(false);
						clearFocusable(spinner_policyterm);
						setFocusable(spinner_premium_payment_term);
						spinner_premium_payment_term.requestFocus();

					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub

					}
				});

		spinner_select_plan
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
											   int pos, long arg3) {
						// TODO Auto-generated method stub

						if (pos == 0) {

							tableRow_adtpd.setVisibility(View.GONE);

						}

						else if (pos == 1) {

							tableRow_adtpd.setVisibility(View.VISIBLE);
							tr_smart_bachat_proposer_detail1
									.setVisibility(View.GONE);
							tr_smart_bachat_proposer_detail2
									.setVisibility(View.GONE);
							String str_sumassured = et_sumassured.getText()
									.toString();

							if (!str_sumassured.equalsIgnoreCase("")) {
								et_adtpd_benifit.setText(str_sumassured);
							}

							String proposer_Title = spnr_bi_smart_bachat_proposer_title
									.getSelectedItem().toString();
							String proposer_First_Name = edt_bi_smart_bachat_proposer_first_name
									.getText().toString();
							String proposer_Middle_Name = edt_bi_smart_bachat_proposer_middle_name
									.getText().toString();
							String proposer_Last_Name = edt_bi_smart_bachat_proposer_last_name
									.getText().toString();

							if (!proposer_Title
									.equalsIgnoreCase("Select Title")
									|| !proposer_First_Name.equals("")
									|| !proposer_Middle_Name.equals("")
									|| !proposer_Last_Name.equals("")
									|| !proposer_date_of_birth.equals("")) {

								proposer_Title = "";
								proposer_First_Name = "";
								proposer_Middle_Name = "";
								proposer_Last_Name = "";
								name_of_proposer = "";
								proposer_date_of_birth = "";

								spnr_bi_smart_bachat_proposer_title
										.setSelection(0);
								edt_bi_smart_bachat_proposer_first_name
										.setText("");
								edt_bi_smart_bachat_proposer_middle_name
										.setText("");
								edt_bi_smart_bachat_proposer_last_name
										.setText("");
								btn_bi_smart_bachat_proposer_date
										.setText("Select Date");
								btn_bi_smart_bachat_life_assured_date
										.setText("Select Date");
								lifeAssured_date_of_birth = "";
								tr_smart_bachat_proposer_detail1
										.setVisibility(View.GONE);
								tr_smart_bachat_proposer_detail2
										.setVisibility(View.GONE);
								spinner_age.setSelection(
										getIndex(spinner_age, "18"), false);

							}

						}
						spinner_select_plan.setFocusable(false);
						clearFocusable(spinner_select_plan);
						setFocusable(spinner_policyterm);
						spinner_policyterm.requestFocus();

					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub

					}
				});

		spinner_premium_payment_term
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
											   int pos, long arg3) {

						spinner_premium_payment_term.setFocusable(false);
						clearFocusable(spinner_premium_payment_term);
						setFocusable(spinner_premiumfreq);
						spinner_premiumfreq.requestFocus();

					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub

					}
				});

		spinner_premiumfreq
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
											   int pos, long arg3) {

						spinner_premiumfreq.setFocusable(false);
						clearFocusable(spinner_premiumfreq);

						if (spnr_bi_smart_bachat_life_assured_title
								.getSelectedItem().toString()
								.equalsIgnoreCase("Select Title")) {
							clearFocusable(spinner_premiumfreq);
							setFocusable(spnr_bi_smart_bachat_life_assured_title);
							spnr_bi_smart_bachat_life_assured_title
									.requestFocus();
						} else {
							clearFocusable(spinner_premiumfreq);
							setFocusable(et_sumassured);
							et_sumassured.requestFocus();
						}
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub

					}
				});
		spnr_bi_smart_bachat_life_assured_title
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
											   int position, long id) {
						// TODO Auto-generated method stub
						if (position == 0){
							lifeAssured_Title = "";
						}else {
							lifeAssured_Title = spnr_bi_smart_bachat_life_assured_title
									.getSelectedItem().toString();
                          /*  if (lifeAssured_Title.equalsIgnoreCase("Mr.")) {
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
							clearFocusable(spnr_bi_smart_bachat_life_assured_title);

							setFocusable(edt_bi_smart_bachat_life_assured_first_name);

							edt_bi_smart_bachat_life_assured_first_name
									.requestFocus();

						}
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub

					}
				});

		spnr_bi_smart_bachat_proposer_title
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
											   int position, long id) {
						// TODO Auto-generated method stub
						if (position == 0){
							proposer_Title = "";
						}else {
							proposer_Title = spnr_bi_smart_bachat_proposer_title
									.getSelectedItem().toString();

							clearFocusable(spnr_bi_smart_bachat_proposer_title);

							setFocusable(edt_bi_smart_bachat_proposer_first_name);

							edt_bi_smart_bachat_proposer_first_name
									.requestFocus();

						}
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub

					}
				});

		rb_smart_bachat_backdating_yes
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView,
												 boolean isChecked) {
						// TODO Auto-generated method stub
						if (isChecked) {
							proposer_Backdating_WishToBackDate_Policy = "y";
							ll_backdating1.setVisibility(View.VISIBLE);
							btn_smart_bachat_backdatingdate
									.setText("Select Date");
							proposer_Backdating_BackDate = "";

							rb_smart_bachat_backdating_yes.setFocusable(false);
							clearFocusable(rb_smart_bachat_backdating_yes);
							setFocusable(btn_smart_bachat_backdatingdate);
							btn_smart_bachat_backdatingdate.requestFocus();

						}
					}
				});

		rb_smart_bachat_backdating_no
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView,
												 boolean isChecked) {
						if (isChecked) {
							proposer_Backdating_WishToBackDate_Policy = "n";
							proposer_Backdating_BackDate = "";
							btn_smart_bachat_backdatingdate
									.setText("Select Date");
							// setDefaultDate();
							ll_backdating1.setVisibility(View.GONE);

							rb_smart_bachat_backdating_no.setFocusable(false);
							clearFocusable(rb_smart_bachat_backdating_no);
							setFocusable(btn_smart_bachat_backdatingdate);
							btn_smart_bachat_backdatingdate.requestFocus();
						}
					}
				});

		btngohome.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				setResult(RESULT_OK);
				finish();
			}
		});

		btnSubmit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				inputVal = new StringBuilder();
				retVal = new StringBuilder();
				bussIll = new StringBuilder();

				proposer_First_Name = edt_bi_smart_bachat_proposer_first_name
						.getText().toString().trim();
				proposer_Middle_Name = edt_bi_smart_bachat_proposer_middle_name
						.getText().toString().trim();
				proposer_Last_Name = edt_bi_smart_bachat_proposer_last_name
						.getText().toString().trim();
				gender_proposer = spinner_proposer_selGender.getSelectedItem().toString();

				name_of_proposer = proposer_Title + " " + proposer_First_Name
						+ " " + proposer_Middle_Name + " " + proposer_Last_Name;

				lifeAssured_First_Name = edt_bi_smart_bachat_life_assured_first_name
						.getText().toString().trim();
				lifeAssured_Middle_Name = edt_bi_smart_bachat_life_assured_middle_name
						.getText().toString().trim();
				lifeAssured_Last_Name = edt_bi_smart_bachat_life_assured_last_name
						.getText().toString().trim();
				gender = spinner_selGender.getSelectedItem().toString();

				name_of_life_assured = lifeAssured_Title + " "
						+ lifeAssured_First_Name + " "
						+ lifeAssured_Middle_Name + " " + lifeAssured_Last_Name;

				mobileNo = edt_proposerdetail_basicdetail_contact_no.getText()
						.toString();
				emailId = edt_proposerdetail_basicdetail_Email_id.getText()
						.toString();
				ConfirmEmailId = edt_proposerdetail_basicdetail_ConfirmEmail_id
						.getText().toString();

				Date();

				if (valLifeAssuredProposerDetail() && valDob()
						&& valProposerDob() && sumassuredval()
						&& valBasicDetail() && matval() && valDoYouBackdate()
						&& valBackdate() && TrueBackdate()
						&& addListenerOnSubmit()) {

					getInput(smartbachatbean);

					if (proposer_Is_Same_As_Life_Assured.equalsIgnoreCase("y")) {
						proposer_Title = "";
						proposer_First_Name = "";
						proposer_Middle_Name = "";
						proposer_Last_Name = "";
						name_of_proposer = "";
						proposer_date_of_birth = "";
					}

					if (needAnalysis_flag == 0) {
						Intent i = new Intent(BI_SmartBachatActivity.this,
								success.class);

						i.putExtra(
								"op",
								"Premium Before Applicable Tax : Rs. "
										+ currencyFormat.format(Double
										.parseDouble(prsObj
												.parseXmlTag(retVal
																.toString(),
														"basicPrem"))));
						i.putExtra(
								"op1",
								"Total Taxes 1st Year : Rs. "
										+ currencyFormat.format(Double
										.parseDouble(prsObj
												.parseXmlTag(retVal
																.toString(),
														"servTax"))));
						i.putExtra(
								"op2",
								"Total Taxes 2nd Year Onwards : Rs. "
										+ currencyFormat.format(Double.parseDouble(prsObj
										.parseXmlTag(retVal.toString(),
												"servTaxSecondYear"))));
						i.putExtra(
								"op3",
								"Premium with taxes 1st year : Rs. "
										+ currencyFormat.format(Double.parseDouble(prsObj.parseXmlTag(
										retVal.toString(),
										"installmntPremWithSerTx"))));
						i.putExtra(
								"op4",
								"Premium with taxes 2nd year onwards : Rs. "
										+ currencyFormat.format(Double.parseDouble(prsObj.parseXmlTag(
										retVal.toString(),
										"installmntPremWithSerTxSecondYear"))));
						i.putExtra(
								"op5",
								"Matuarity Benifit Guaranteed : Rs. "
										+ currencyFormat.format(Double.parseDouble(prsObj.parseXmlTag(
										retVal.toString(),
										"matGuar"
												+ spinner_policyterm
												.getSelectedItem()
												.toString()))));
						i.putExtra(
								"op6",
								"Matuarity Benifit Non-Guaranteed 4% : Rs. "
										+ currencyFormat.format(Double.parseDouble(prsObj.parseXmlTag(
										retVal.toString(),
										"matNonGuar4pa"
												+ spinner_policyterm
												.getSelectedItem()
												.toString()))));
						i.putExtra(
								"op7",
								"Matuarity Benifit Non-Guaranteed 8% : Rs. "
										+ currencyFormat.format(Double.parseDouble(prsObj.parseXmlTag(
										retVal.toString(),
										"matNonGuar8pa"
												+ spinner_policyterm
												.getSelectedItem()
												.toString()))));
						if (spinner_select_plan
								.getSelectedItem()
								.toString()
								.equalsIgnoreCase(
										"Option B (Endowment Option with in-built Accidental Death and Total Permanent Disability (AD&TPD) Benefit)"))
							i.putExtra(
									"op8",
									"AD&TPD Premium : Rs. "
											+ currencyFormat.format(Double.parseDouble(prsObj.parseXmlTag(
											retVal.toString(),
											"AdTpdPremWithoutDisc"))));

						i.putExtra("ProductDescName", "SBI Life - Smart Bachat");
						startActivity(i);


					} else
						Dialog();
				}

			}

		});

		edt_bi_smart_bachat_life_assured_first_name
				.setOnEditorActionListener(this);
		edt_bi_smart_bachat_life_assured_middle_name
				.setOnEditorActionListener(this);
		edt_bi_smart_bachat_life_assured_last_name
				.setOnEditorActionListener(this);

		edt_bi_smart_bachat_proposer_first_name.setOnEditorActionListener(this);
		edt_bi_smart_bachat_proposer_middle_name
				.setOnEditorActionListener(this);
		edt_bi_smart_bachat_proposer_last_name.setOnEditorActionListener(this);

		edt_proposerdetail_basicdetail_contact_no
				.setOnEditorActionListener(this);
		edt_proposerdetail_basicdetail_Email_id.setOnEditorActionListener(this);
		edt_proposerdetail_basicdetail_ConfirmEmail_id
				.setOnEditorActionListener(this);
		et_sumassured.setOnEditorActionListener(this);
		et_adtpd_benifit.setOnEditorActionListener(this);

		edt_proposerdetail_basicdetail_contact_no
				.addTextChangedListener(new TextWatcher() {

					@Override
					public void onTextChanged(CharSequence arg0, int arg1,
											  int arg2, int arg3) {
						// TODO Auto-generated method stub

					}

					@Override
					public void beforeTextChanged(CharSequence arg0, int arg1,
												  int arg2, int arg3) {
						// TODO Auto-generated method stub

					}

					@Override
					public void afterTextChanged(Editable arg0) {
						// TODO Auto-generated method stub
						String abc = edt_proposerdetail_basicdetail_contact_no
								.getText().toString();
						mobile_validation(abc);

					}
				});

		edt_proposerdetail_basicdetail_Email_id
				.addTextChangedListener(new TextWatcher() {

					@Override
					public void onTextChanged(CharSequence arg0, int arg1,
											  int arg2, int arg3) {
						// TODO Auto-generated method stub

					}

					@Override
					public void beforeTextChanged(CharSequence arg0, int arg1,
												  int arg2, int arg3) {
						// TODO Auto-generated method stub

					}

					@Override
					public void afterTextChanged(Editable arg0) {
						// TODO Auto-generated method stub
						ProposerEmailId = edt_proposerdetail_basicdetail_Email_id
								.getText().toString();
						//email_id_validation(ProposerEmailId);

					}
				});

		edt_proposerdetail_basicdetail_ConfirmEmail_id
				.addTextChangedListener(new TextWatcher() {

					@Override
					public void onTextChanged(CharSequence arg0, int arg1,
											  int arg2, int arg3) {
						// TODO Auto-generated method stub

					}

					@Override
					public void beforeTextChanged(CharSequence arg0, int arg1,
												  int arg2, int arg3) {
						// TODO Auto-generated method stub

					}

					@Override
					public void afterTextChanged(Editable arg0) {
						// TODO Auto-generated method stub
						String proposer_confirm_emailId = edt_proposerdetail_basicdetail_ConfirmEmail_id
								.getText().toString();
						//confirming_email_id(proposer_confirm_emailId);

					}
				});
		et_sumassured.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
									  int arg3) {

			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
										  int arg2, int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable arg0) {

				try {
					if (!et_sumassured.getText().toString().equals("")) {
						int sumAssured = Integer.parseInt(et_sumassured
								.getText().toString());
						if (sumAssured < 5000000)
							et_adtpd_benifit.setText(sumAssured + "");
						else {
							et_adtpd_benifit.setText(5000000 + "");
						}
					} else
						et_adtpd_benifit.setText(0 + "");
				} catch (Exception ignored) {
				}

			}

		});

		String str_usertype = "";
		try {
			str_usertype = SimpleCrypto.decrypt("SBIL",
					dbHelper.GetUserType());

		} catch (Exception e) {
			e.printStackTrace();
		}
		TableRow tr_staff_disc = findViewById(R.id.tr_smart_bachat_staff_disc);

		/*if (str_usertype.equalsIgnoreCase("BAP")
				|| str_usertype.equalsIgnoreCase("CAG")) {
			tr_staff_disc.setVisibility(View.GONE);
		}*/

	}

	private boolean addListenerOnSubmit() {

		if (cb_staffdisc.isChecked()) {
			smartbachatbean.setIsForStaffOrNot(true);
		} else {
			smartbachatbean.setIsForStaffOrNot(false);
		}

		if(cb_kerladisc.isChecked())
		{
			smartbachatbean.setKerlaDisc(true);
		}
		else
		{
			smartbachatbean.setKerlaDisc(false);
		}

		if (cb_JKResident.isChecked()) {
			smartbachatbean.setIsJammuResident(true);
		} else {
			smartbachatbean.setIsJammuResident(false);
		}

		String temp = et_adtpd_benifit.getText().toString();
		int adtpdval;
		if (!(temp.equals(""))) {
			adtpdval = Integer.parseInt(et_adtpd_benifit.getText().toString());

		} else {
			adtpdval = 0;
		}

		smartbachatbean.setPlanType(spinner_select_plan.getSelectedItem()
				.toString());
		smartbachatbean.setAge(Integer.parseInt(spinner_age.getSelectedItem()
				.toString()));
		smartbachatbean.setGender(spinner_selGender.getSelectedItem()
				.toString());
		smartbachatbean.setPolicyterm(Integer.parseInt(spinner_policyterm
				.getSelectedItem().toString()));
		smartbachatbean.setPremiumpayingterm(Integer
				.parseInt(spinner_premium_payment_term.getSelectedItem()
						.toString()));
		smartbachatbean.setPremiumFrequency(spinner_premiumfreq
				.getSelectedItem().toString());
		smartbachatbean.setSumAssured(Integer.parseInt(et_sumassured.getText()
				.toString()));
		smartbachatbean.setAdtpdbenifit(adtpdval);

		return showSmartBachatOutputPg(smartbachatbean);
	}

	/************************** Output STarts Here ****************/
	// Display Smart Scholar Output Screen

	private boolean showSmartBachatOutputPg(SmartBachatBean smartbachatbean) {
		retVal = new StringBuilder();

		String[] outputArr = getOutput("BI_of_Smart_Bachat", smartbachatbean);
		/*** Added by Akshaya on 31-Mar-15 start ***/
		if (cb_staffdisc.isChecked()) {
			staffStatus = "sbi";
			// disc_Basic_SelFreq
		} else
			staffStatus = "none";

		if (valPremAmount(Double.parseDouble(outputArr[0]))) {
			try {

				retVal.append("<?xml version='1.0' encoding='utf-8' ?><SmartBachat>");
				retVal.append("<errCode>0</errCode>");
				retVal.append("<staffStatus>" + staffStatus + "</staffStatus>");
				retVal.append("<staffRebate>" + outputArr[16]
						+ "</staffRebate>");
				retVal.append("<FYServiceTax>" + outputArr[7]
						+ "</FYServiceTax>");
				retVal.append("<InstmntPrem>" + outputArr[17] + "</InstmntPrem>");
				retVal.append("<basicPremWithoutDisc>" + outputArr[17]
						+ "</basicPremWithoutDisc>");
				retVal.append("<basicPremWithoutDiscSA>" + outputArr[20]
						+ "</basicPremWithoutDiscSA>");
				retVal.append("<AdTpdPremWithoutDisc>" + outputArr[21]
						+ "</AdTpdPremWithoutDisc>");
				retVal.append("<AdTpdPremWithoutDiscSA>" + outputArr[22]
						+ "</AdTpdPremWithoutDiscSA>");
				retVal.append("<OccuInt>" + outputArr[19] + "</OccuInt>");
				retVal.append("<backdateInt>" + outputArr[18]
						+ "</backdateInt>");

				retVal.append("<basicPrem>" + outputArr[0] + "</basicPrem>" +
						"<basicServiceTax>" + outputArr[1] + "</basicServiceTax>" +
						"<basicServiceTaxSecondYear>" + outputArr[2] + "</basicServiceTaxSecondYear>" +
						"<SBCServiceTax>" + outputArr[3] + "</SBCServiceTax>" +
						"<SBCServiceTaxSecondYear>" + outputArr[4] + "</SBCServiceTaxSecondYear>" +
						"<KKCServiceTax>" + outputArr[5] + "</KKCServiceTax>" +
						"<KKCServiceTaxSecondYear>" + outputArr[6] + "</KKCServiceTaxSecondYear>" +
						"<servTax>" + outputArr[7] + "</servTax>" +
						"<servTaxSecondYear>" + outputArr[8] + "</servTaxSecondYear>" +
						"<installmntPremWithSerTx>" + outputArr[9] + "</installmntPremWithSerTx>" +
						"<installmntPremWithSerTxSecondYear>" + outputArr[10] + "</installmntPremWithSerTxSecondYear>" +
						"<installmntPrem>" + outputArr[0] + "</installmntPrem>" +
						"<adtpdRiderprem>" + outputArr[14] + "</adtpdRiderprem>" +
						"<modalprem>" + outputArr[15] + "</modalprem>" +
						"<KeralaCessServiceTax>" + outputArr[23] + "</KeralaCessServiceTax>" +
						"<KeralaCessServiceTaxSecondYear>" + outputArr[24] + "</KeralaCessServiceTaxSecondYear>" +
						"<BasicInstallmentPremFirstYear>" + outputArr[25] + "</BasicInstallmentPremFirstYear>" +
						"<BasicInstallmentPremSecondYear>" + outputArr[26] + "</BasicInstallmentPremSecondYear>" +
						"<IntsallmentPremATPDB_FirstYear>" + outputArr[27] + "</IntsallmentPremATPDB_FirstYear>" +
						"<IntsallmentPremATPDB_SecondYear>" + outputArr[28] + "</IntsallmentPremATPDB_SecondYear>" +
						"<modalprem1>"+outputArr[29]  + "</modalprem1>" +
						"<modalprem2>"+outputArr[30]  + "</modalprem2>" +
						"<modalprem3>"+outputArr[31]  + "</modalprem3>" );
				retVal.append("<OccuIntServiceTax>" + outputArr[29] + "</OccuIntServiceTax>");
				int index = smartbachatbean.getPolicyterm();
				String matNonGuar4pa  = prsObj.parseXmlTag(bussIll.toString(), "matNonGuar4pa" + index + "");
				String matNonGuar8pa = prsObj.parseXmlTag(bussIll.toString(),"matNonGuar8pa" + index + "");

				retVal.append("<matNonGuar4pa"+index+">"+matNonGuar4pa+"</matNonGuar4pa"+index+">");
				retVal.append("<matNonGuar8pa"+index+">"+matNonGuar8pa+"</matNonGuar8pa"+index+">");
				retVal.append(bussIll.toString());


				retVal.append("</SmartBachat>");
				System.out.println("output " + retVal.toString());
			} catch (Exception e) {
				retVal.append("<?xml version='1.0' encoding='utf-8' ?><SmartBachat>" + "<errCode>1</errCode>" + "<errorMessage>").append(e.getMessage()).append("</errorMessage></SmartBachat>");
			}
			return true;
		}
		return false;
	}

	private void Alert(String msg) {

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				BI_SmartBachatActivity.this);
		alertDialogBuilder.setMessage(msg);
		alertDialogBuilder.setPositiveButton("Ok",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();

					}
				});

		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();

	}

	/********************** Calculation Starts Here ***********************************************/

	private String[] getOutput(String string, SmartBachatBean smartbachatbean) {

		bussIll = new StringBuilder();

		int limit = smartbachatbean.getPolicyterm();
		double x=0, y=0,sumAnnualizedPrem=0,annualizedPrem=0,FirstYearOfAnnulized = 0;;
		double premBasic=0;
		String totalbasepremium =null,AnnulizedPremium=null;
		int year_F = 0;
		int rowNumber=0;
		int _year_F=0;

		//11-12-2019 added by sujata
		String premiumBasic_NotRounded=""+smartbachatbusinesslogic.getTotalBasePremium();
		String premiumBasic_Rounded=commonforall.getRound(commonforall.getStringWithout_E(Double.parseDouble(premiumBasic_NotRounded)));
		premBasic=Double.parseDouble((premiumBasic_Rounded));
		//System.out.println("premBasicvalue: "+premBasic);

		double totbasefactor = smartbachatbusinesslogic.getPremiumMultiplicationFactor();
		// For BaseRate
		smartbachatbusinesslogic.setBaseRate(smartbachatbean.getPlantype(),
				smartbachatbean.getPolicyterm(),
				smartbachatbean.getPremiumpayingterm(),
				smartbachatbean.getAge());

		// for base rate adtpd

		smartbachatbusinesslogic.setBaseRateFor_ADTPD(
				smartbachatbean.getPlantype(), smartbachatbean.getPolicyterm(),
				smartbachatbean.getPremiumpayingterm(),
				smartbachatbean.getAge());

		// For Staff Rebate
		smartbachatbusinesslogic.setStaffRebate(smartbachatbean
				.getIsForStaffOrNot());

		// For Staff Rebate ADTPD
		smartbachatbusinesslogic.setStaffRebatefor_ADTPD(smartbachatbean
				.getIsForStaffOrNot());

		// For SA Rebate
		smartbachatbusinesslogic.setSARebate(smartbachatbean.getSumAssured());

		// For Loading for Frequencies of Premium
		smartbachatbusinesslogic.setLoadingFrequencyPremium(smartbachatbean
				.getPremiumFrequency());

		// For Loading prem factor
		smartbachatbusinesslogic
				.setLoadingFrequencyPremiumfor_ADTPD(smartbachatbean
						.getPremiumFrequency());

		// yearlymodalloading
		smartbachatbusinesslogic
				.setYearlyPremiumBeforeModalLoading(smartbachatbean
						.getSumAssured());

		// for PremiumBeforeST
		smartbachatbusinesslogic.setPremiumBeforeST(smartbachatbean
				.getSumAssured());
//		System.out.println("getPremiumBeforeST()"+smartbachatbusinesslogic.getPremiumBeforeST());

		// Modal premiumADTPD
		smartbachatbusinesslogic.setYearlyPremiumBeforeModalLoading_ADTPD(smartbachatbean.getAdtpdbenifit());


		// For PremiumBeforeST for ADTPD
		smartbachatbusinesslogic.setPremiumBeforeST_ADTPD(smartbachatbean
				.getAdtpdbenifit());
//		System.out.println("smartbachatbusinesslogic.setPremiumBeforeST_ADTPD"+smartbachatbusinesslogic.getPremiumBeforeST_ADTPD());

		// For Total Premium
		smartbachatbusinesslogic.setTotalPremium(smartbachatbean
				.getPremiumFrequency());

		smartbachatbusinesslogic.setAnnualPremiumWithoutModal();

		// for premium multiplication factor
		smartbachatbusinesslogic.setPremiumMultiplicationFactor(smartbachatbean
				.getPremiumFrequency());

		smartbachatbusinesslogic.setModalPremium(smartbachatbean.getPlantype());

		// for total base premium
		//11-12-2019 added by sujata
		smartbachatbusinesslogic.setTotalBasePremium(smartbachatbusinesslogic.getYear(),smartbachatbean.getPremiumpayingterm());


//		//for bonus rate 4
//		smartbachatbusinesslogic.setBonusRate_4(smartbachatbean.getPolicyterm(), smartbachatbean.getPremiumpayingterm());
//
//		//for bonus rate 8
//		smartbachatbusinesslogic.setBonusRate_8(smartbachatbean.getPolicyterm(), smartbachatbean.getPremiumpayingterm());

		// for terminal bonus 4
		smartbachatbusinesslogic.setTerminalBonus_4(
				smartbachatbean.getPremiumpayingterm(),
				smartbachatbean.getPolicyterm(),
				smartbachatbean.getSumAssured());

		// for terminal bonus 8
		smartbachatbusinesslogic.setTerminalBonus_8(smartbachatbean.getPremiumpayingterm(), smartbachatbean.getPolicyterm(), smartbachatbean.getSumAssured());

		// premium with ST
		smartbachatbusinesslogic.setPremium_with_ST(smartbachatbean.getIsJammuResident(),smartbachatbean.isKerlaDisc());


		// ST for 1st year
		smartbachatbusinesslogic.setST_firstYear();

		// premium with ST 2nd year
		//11-12-2019
		smartbachatbusinesslogic.setPremium_with_ST_secondyear(smartbachatbean.getIsJammuResident(),smartbachatbean.isKerlaDisc());



		// ST for 2nd year
		smartbachatbusinesslogic.setST_SecondYear();

		/*smartbachatbusinesslogic.setModalPremium1(smartbachatbean.getPlantype());

		smartbachatbusinesslogic.setModalPremium2(smartbachatbean.getPlantype());*/

		//Basic Service Tax 1st Year
		smartbachatbusinesslogic.setBasicServiceTax_firstYear(smartbachatbean.getIsJammuResident());
		//bussIll.append("<BasicServiceTax_1styear"+ i +">" + smartbachatbusinesslogic.getBasicServiceTax_firstYear() + "</BasicServiceTax_1styear"+ i +">");

		// Swach Bharat 1st Year
		smartbachatbusinesslogic.setSwachBharat_1styear(smartbachatbean
				.getIsJammuResident());
		//bussIll.append("<SwachBharatCess_1stYear"+ i +">" + smartbachatbusinesslogic.getSwachBharat_1styear() + "</SwachBharatCess_1stYear"+ i +">");

		//  Added By Saurabh Jain on 16/05/2019 Start

		smartbachatbusinesslogic.setKeralaServiceTax_1styear(smartbachatbean.isKerlaDisc());

		//  Added By Saurabh Jain on 16/05/2019 End

		// Krishi Kalyan 1st year
		smartbachatbusinesslogic
				.setKrishiKalyan_1stYear(smartbachatbean
						.getIsJammuResident());
		//	bussIll.append("<KrishiKalyancess_1stYear"+ i +">" + smartbachatbusinesslogic.getKrishiKalyan_1stYear() + "</KrishiKalyancess_1stYear"+ i +">");

		// Total Taxes !st Year
		smartbachatbusinesslogic.setTotalTaxes1st_year(smartbachatbean.isKerlaDisc());
		//	bussIll.append("<TotalTaxes_1stYear"+ i +">" + smartbachatbusinesslogic.getTotalTaxes1st_year() + "</TotalTaxes_1stYear"+ i +">");


		//Basic service tax 2nd year
		smartbachatbusinesslogic.setBasicServiceTax_secondYear(smartbachatbean.getIsJammuResident());
		//	bussIll.append("<BasicServiceTax_2ndyear"+ i +">" + smartbachatbusinesslogic.getBasicServiceTax_secondYear() + "</BasicServiceTax_2ndyear"+ i +">");

		// Swach Bharat 2nd year
		smartbachatbusinesslogic.setSwachBharat_2ndyear(smartbachatbean
				.getIsJammuResident());
		//bussIll.append("<SwachBharatCess_2ndYear"+ i +">" + smartbachatbusinesslogic.getSwachBharat_2ndyear() + "</SwachBharatCess_2ndYear"+ i +">");

		//  Added By Saurabh Jain on 16/05/2019 Start

		smartbachatbusinesslogic.setKeralaServiceTax_2ndyear(smartbachatbean.isKerlaDisc());

		//  Added By Saurabh Jain on 16/05/2019 End

		// Krishi 2nd
		smartbachatbusinesslogic
				.setKrishiKalyan_2ndYear(smartbachatbean
						.getIsJammuResident());
		//	bussIll.append("<KrishiKalyancess_2ndYear"+ i +">" + smartbachatbusinesslogic.getKrishiKalyan_2ndYear() + "</KrishiKalyancess_2ndYear"+ i +">");

		// total taxes 2nd year
		smartbachatbusinesslogic.setTotalTaxes2nd_year(smartbachatbean.isKerlaDisc());
		//bussIll.append("<TotalTaxes_2ndYear"+ i +">" + smartbachatbusinesslogic.getTotalTaxes2nd_year() + "</TotalTaxes_2ndYear"+ i +">");

		// Final Premium With taxes
		smartbachatbusinesslogic.setFinalPremium_WithTaxes_1stYear();
		//	bussIll.append("<FinalPremiumWithTaxes_1stYear"+ i +">" + smartbachatbusinesslogic.getFinalPremium_WithTaxes_1stYear() + "</FinalPremiumWithTaxes_1stYear"+ i +">");


		// Final Premium with taxes 2nd year onwards
		smartbachatbusinesslogic.setFinalPremium_WithTaxes_2ndYear();
		//	bussIll.append("<FinalPremiumWithTaxes_2ndYear"+ i +">" + smartbachatbusinesslogic.getFinalPremium_WithTaxes_2ndYear() + "</FinalPremiumWithTaxes_2ndYear"+ i +">");

		//11-12-2019 added by sujata
		smartbachatbusinesslogic.setPremium_with_ATPDB_FirstYear(smartbachatbean.getPlantype(), smartbachatbean.getIsJammuResident(),smartbachatbean.isKerlaDisc());

		smartbachatbusinesslogic.setPremium_with_ATPDB_SecondYear(smartbachatbean.getPlantype(), smartbachatbean.getIsJammuResident(),smartbachatbean.isKerlaDisc());

		smartbachatbusinesslogic.setAdtpd(smartbachatbean.getPlantype(), smartbachatbean.getIsJammuResident(),smartbachatbean.isKerlaDisc(), smartbachatbean.getSumAssured());


		/*double totalInstallmentFirstYear =0,totalInstallmentFirstYear1 =0;
		if(smartbachatbean.getPlantype().equals("Option A (Endowment Option)")){

			totalInstallmentFirstYear = smartbachatbusinesslogic.getST_firstYear();
		}else{

			totalInstallmentFirstYear =smartbachatbusinesslogic.getPremium_with_ATPDB_FirstYear() -smartbachatbusinesslogic.getST_firstYear();
		}


		if(smartbachatbean.getPlantype().equals("Option A (Endowment Option)")){

			totalInstallmentFirstYear1 = smartbachatbusinesslogic.getST_SecondYear();
		}else{

			totalInstallmentFirstYear1 =smartbachatbusinesslogic.getPremium_with_ATPDB_SecondYear() -smartbachatbusinesslogic.getST_SecondYear();
		}*/

		smartbachatbusinesslogic.setAdtpd1(smartbachatbean.getPlantype(), smartbachatbean.getIsJammuResident(),smartbachatbean.isKerlaDisc());

		smartbachatbusinesslogic.setAdtpd2(smartbachatbean.getPlantype(), smartbachatbean.getIsJammuResident(),smartbachatbean.isKerlaDisc());
		try
		{
			for(int i=1;i<=limit;i++)
			{
				//11-12-2019 added by sujata
				year_F = rowNumber;
				_year_F = year_F;

				//for Year
				smartbachatbusinesslogic.setYear(i);
				bussIll.append("<policyYr" + i + ">" + smartbachatbusinesslogic.getYear() + "</policyYr" + i + ">");

				//11-12-2019 added by sujata
				//for setTotalPremiumPaid_withoutST
				totalbasepremium= (commonforall
						.getStringWithout_E(smartbachatbusinesslogic
								.getBasePremiumuptoPpt(
										totbasefactor,
										smartbachatbean.getPolicyterm()
										,smartbachatbean.getPremiumFrequency()
										,smartbachatbean.getPremiumpayingterm())
						));

				//24-12-2019
				AnnulizedPremium =commonforall.getRoundUp(commonforall
						.getStringWithout_E(smartbachatbusinesslogic
								.get_Annualized_Premium(
										smartbachatbusinesslogic.getYear(),smartbachatbean.getPremiumpayingterm()
										,smartbachatbean.getAdtpdbenifit(),smartbachatbean.getSumAssured()
								)));

				bussIll.append("<Annualized_Premium"+ i +">" + (AnnulizedPremium) + "</Annualized_Premium"+ i +">");


				smartbachatbusinesslogic.setTotalPremiumPaid_withoutST(smartbachatbean.getPremiumpayingterm(), smartbachatbean.getPolicyterm());
				bussIll.append("<TotBasePremWithTx" + i + ">" + commonforall.getStringWithout_E(smartbachatbusinesslogic.getTotalPremiumPaid_withoutST()) + "</TotBasePremWithTx" + i + ">");

				//for Benifit payable on death gauranteed
				//11-12-2019 added by sujata

				//	smartbachatbusinesslogic.get_Annualized_Premium(smartbachatbusinesslogic.getYear(),smartbachatbean.getPremiumpayingterm(),smartbachatbean.getAdtpdbenifit(),smartbachatbean.getSumAssured());
				//	bussIll.append("<Annualized_Premium"+ i +">" + commonforall.getStringWithout_E(smartbachatbusinesslogic.get_Annualized_Premium(smartbachatbusinesslogic.getYear(),smartbachatbean.getPremiumpayingterm(),smartbachatbean.getAdtpdbenifit(),smartbachatbean.getSumAssured()))
				//			+ "</Annualized_Premium"+ i +">");


				//24-12-2019
				if(smartbachatbusinesslogic.getYear() == 1)
				{

					FirstYearOfAnnulized = Double.parseDouble(AnnulizedPremium);
				}

				smartbachatbusinesslogic.setBenifitPayableOnDeath_Gauranteed(smartbachatbean.getSumAssured(),FirstYearOfAnnulized,smartbachatbean.getPolicyterm(),smartbachatbusinesslogic.getYear());
				bussIll.append("<deathGuar"+ i +">" + commonforall.getRound(commonforall.getStringWithout_E(smartbachatbusinesslogic.getBenifitPayableOnDeath_Gauranteed())) + "</deathGuar"+ i +">");


				//for Benifit payable on death non guaranteed 4%
				//11-12-2019 added by sujata
				smartbachatbusinesslogic.setBenifitPayableOnDeath_NonGauranteed_4(smartbachatbean.getPremiumpayingterm(), smartbachatbean.getPolicyterm(), smartbachatbean.getSumAssured());
				bussIll.append("<deathNonGuar4pa" + i + ">" + commonforall.getStringWithout_E(smartbachatbusinesslogic.getBenifitPayableOnDeath_NonGauranteed_4()) + "</deathNonGuar4pa" + i + ">");

				// for Benifit payable on death non guaranteed 8%
				//11-12-2019 added by sujata
				smartbachatbusinesslogic.setBenifitPayableOnDeath_NonGauranteed_8(smartbachatbean.getPremiumpayingterm(), smartbachatbean.getPolicyterm(), smartbachatbean.getSumAssured());
				bussIll.append("<deathNonGuar8pa" + i + ">" + commonforall.getStringWithout_E(smartbachatbusinesslogic.getBenifitPayableOnDeath_NonGauranteed_8()) + "</deathNonGuar8pa" + i + ">");

				//for Benifit payable on Accidental Death
				//11-12-2019 added by sujata
				smartbachatbusinesslogic.setBenifitPayableOn_AccidentalDeath(smartbachatbean.getAdtpdbenifit(), smartbachatbean.getPolicyterm());
				bussIll.append("<Accidentaldeathbenifit" + i + ">" + commonforall.getStringWithout_E(smartbachatbusinesslogic.getBenifitPayableOn_AccidentalDeath()) + "</Accidentaldeathbenifit" + i + ">");


				//for survival bnifit guaranteed
				smartbachatbusinesslogic.setSurvivalBenifit_Guaranteed(smartbachatbean.getPolicyterm(), smartbachatbean.getSumAssured());
				bussIll.append("<matGuar"+ i +">" + commonforall.getRound(commonforall.getStringWithout_E(smartbachatbusinesslogic.getSurvivalBenifit_Guaranteed())) + "</matGuar"+ i +">");

				//for survival benifit guaranteed 4
				smartbachatbusinesslogic.setSurvivalBenifit_Guaranteed4(smartbachatbean.getPolicyterm(), smartbachatbean.getSumAssured());
				bussIll.append("<matNonGuar4pa" + i + ">" + commonforall.getStringWithout_E(smartbachatbusinesslogic.getSurvivalBenifit_Guaranteed4()) + "</matNonGuar4pa" + i + ">");


				//for survival benifit guaranteed 8
				smartbachatbusinesslogic.setSurvivalBenifit_Guaranteed8(smartbachatbean.getPolicyterm(), smartbachatbean.getSumAssured());
				bussIll.append("<matNonGuar8pa" + i + ">" + commonforall.getStringWithout_E(smartbachatbusinesslogic.getSurvivalBenifit_Guaranteed8()) + "</matNonGuar8pa" + i + ">");

				//for surrender value guaranteed
				//24-12-2019
				smartbachatbusinesslogic.setSurrenederValue_Guaranteed(smartbachatbean.getPolicyterm(), smartbachatbean.getPremiumpayingterm(),smartbachatbean.getAge(),smartbachatbusinesslogic.getYear());
				bussIll.append("<surGuar"+ i +">" + commonforall.getRound(commonforall.getStringWithout_E(smartbachatbusinesslogic.getSurrenederValue_Guaranteed())) + "</surGuar"+ i +">");




				//for surrender value 4%
				smartbachatbusinesslogic.setSurrenederValue_Guaranteed_4(smartbachatbean.getPolicyterm(), smartbachatbean.getPremiumpayingterm(), smartbachatbean.getSumAssured());
				bussIll.append("<surNonGuar4pa"+ i +">" + commonforall.getRound(commonforall.getStringWithout_E(smartbachatbusinesslogic.getSurrenederValue_Guaranteed_4())) + "</surNonGuar4pa"+ i +">");

				//for surrender value 8%
				smartbachatbusinesslogic.setSurrenederValue_Guaranteed_8(smartbachatbean.getPolicyterm(), smartbachatbean.getPremiumpayingterm(), smartbachatbean.getSumAssured());
				bussIll.append("<surNonGuar8pa"+ i +">" + commonforall.getRound(commonforall.getStringWithout_E(smartbachatbusinesslogic.getSurrenederValue_Guaranteed_8())) + "</surNonGuar8pa"+ i +">");



				///added by sujata on 22-11-2019////

				bussIll.append("<GauranteedAddition"+ i +">" + x + "</GauranteedAddition"+ i +">");

				bussIll.append("<CashBonus"+ i +">" + y + "</CashBonus"+ i +">");



				smartbachatbusinesslogic.getMaturity_Benefit4(smartbachatbusinesslogic.getYear(),smartbachatbean.getPolicyterm());
				bussIll.append("<Maturity_Benefit4"+ i +">" + commonforall.getRound(commonforall.getStringWithout_E(smartbachatbusinesslogic.getMaturity_Benefit4(smartbachatbusinesslogic.getYear(),smartbachatbean.getPolicyterm())))
						+ "</Maturity_Benefit4"+ i +">");

				smartbachatbusinesslogic.getMaturity_Benefit8(smartbachatbusinesslogic.getYear(),smartbachatbean.getPolicyterm());
				bussIll.append("<Maturity_Benefit8"+ i +">" + commonforall.getRound(commonforall.getStringWithout_E(smartbachatbusinesslogic.getMaturity_Benefit8(smartbachatbusinesslogic.getYear(),smartbachatbean.getPolicyterm())))
						+ "</Maturity_Benefit8"+ i +">");


				//11-12-2019 added by sujata
				sumAnnualizedPrem=sumAnnualizedPrem+Double.parseDouble(totalbasepremium);



				//11-12-2019 added by sujata
				smartbachatbusinesslogic.getTotalDeathBenefit4per(smartbachatbusinesslogic.getYear(),smartbachatbean.getPolicyterm(),smartbachatbean.getSumAssured(),sumAnnualizedPrem);
				bussIll.append("<getTotalDeathBenefit4per"+ i +">" + commonforall.getRound(commonforall.getStringWithout_E(smartbachatbusinesslogic.getTotalDeathBenefit4per(smartbachatbusinesslogic.getYear(),smartbachatbean.getPolicyterm(),smartbachatbean.getSumAssured(),sumAnnualizedPrem)))
						+ "</getTotalDeathBenefit4per"+ i +">");
				//System.out.println("TotalDeath4 " +commonforall.getStringWithout_E(smartbachatbusinesslogic.getTotalDeathBenefit4per(smartbachatbusinesslogic.getYear(),smartbachatbean.getPremiumpayingterm(),smartbachatbean.getSumAssured(),sumAnnualizedPrem)));
				smartbachatbusinesslogic.getTotalDeathBenefit8per(smartbachatbusinesslogic.getYear(),smartbachatbean.getPolicyterm(),smartbachatbean.getSumAssured(),sumAnnualizedPrem);
				bussIll.append("<getTotalDeathBenefit8per"+ i +">" + commonforall.getRound(commonforall.getStringWithout_E(smartbachatbusinesslogic.getTotalDeathBenefit8per(smartbachatbusinesslogic.getYear(),smartbachatbean.getPolicyterm(),smartbachatbean.getSumAssured(),sumAnnualizedPrem)))
						+ "</getTotalDeathBenefit8per"+ i +">");

			}

			double a = smartbachatbusinesslogic.getPremiumBeforeST_ADTPD();
			//Toast.makeText(SmartBachatActivity.this, "PremiumBeforeST_ADTPD"+a, Toast.LENGTH_LONG).show();
		} catch (Exception e) {
			e.printStackTrace();
		}
		//  Added By Saurabh Jain on 16/05/2019 Start

		double KeralaCessServiceTax=0;
		double KeralaCessServiceTaxSecondYear =0;

		if(smartbachatbean.isKerlaDisc()){
			KeralaCessServiceTax=smartbachatbusinesslogic.getKeralaServiceTax_1styear()-smartbachatbusinesslogic.getBasicServiceTax_firstYear();
		}else{
			KeralaCessServiceTax=0;
		}

		if(smartbachatbean.isKerlaDisc()){
			KeralaCessServiceTaxSecondYear =smartbachatbusinesslogic.getKeralaServiceTax_2ndyear()-smartbachatbusinesslogic.getBasicServiceTax_secondYear();
		}else{
			KeralaCessServiceTaxSecondYear =0;
		}

		//  Added By Saurabh Jain on 16/05/2019 End

		String premiumbefore_st= commonforall.getRoundUp(commonforall.getRoundOffLevel2New(commonforall.getStringWithout_E(smartbachatbusinesslogic.getPremiumBeforeST())));
//		String premiumbefore_st= commonforall.getRoundUp_Level2(commonforall.getRoundOffLevel2New(commonforall.getStringWithout_E(smartbachatbusinesslogic.getPremiumBeforeST())));
		String BasicServiceTax_firstYear = commonforall.getStringWithout_E(smartbachatbusinesslogic.getBasicServiceTax_firstYear());
		String BasicServiceTax_secondYear = commonforall.getStringWithout_E(smartbachatbusinesslogic.getBasicServiceTax_secondYear());
		String SwachBharat_1styear = commonforall.getStringWithout_E(smartbachatbusinesslogic.getSwachBharat_1styear());
		String SwachBharat_2ndyear = commonforall.getStringWithout_E(smartbachatbusinesslogic.getSwachBharat_2ndyear());
		String KrishiKalyan_1stYear = commonforall.getStringWithout_E(smartbachatbusinesslogic.getKrishiKalyan_1stYear());
		String KrishiKalyan_2ndYear = commonforall.getStringWithout_E(smartbachatbusinesslogic.getKrishiKalyan_2ndYear());
		String TotalTaxes1st_year = commonforall.getStringWithout_E(smartbachatbusinesslogic.getTotalTaxes1st_year());
		String TotalTaxes2nd_year = commonforall.getStringWithout_E(smartbachatbusinesslogic.getTotalTaxes2nd_year());
//		String FinalPremium_WithTaxes_1stYear= commonforall.getStringWithout_E(smartbachatbusinesslogic.getFinalPremium_WithTaxes_1stYear());
//		String FinalPremium_WithTaxes_2ndYear= commonforall.getStringWithout_E(smartbachatbusinesslogic.getFinalPremium_WithTaxes_2ndYear());
		String FinalPremium_WithTaxes_1stYear= commonforall.getStringWithout_E(smartbachatbusinesslogic.getST_firstYear() + smartbachatbusinesslogic.getPremium_with_ATPDB_FirstYear());
		String FinalPremium_WithTaxes_2ndYear= commonforall.getStringWithout_E(smartbachatbusinesslogic.getST_SecondYear() + smartbachatbusinesslogic.getPremium_with_ATPDB_SecondYear());

//		System.out.println("Fiest "+FinalPremium_WithTaxes_1stYear);
//		System.out.println("Second "+FinalPremium_WithTaxes_2ndYear);

		String SurvivalBenifit_Guaranteed = commonforall.getStringWithout_E(smartbachatbusinesslogic.getSurvivalBenifit_Guaranteed());
		String SurvivalBenifit_Guaranteed4 = commonforall.getStringWithout_E(smartbachatbusinesslogic.getSurvivalBenifit_Guaranteed4());
		String SurvivalBenifit_Guaranteed8 = commonforall.getStringWithout_E(smartbachatbusinesslogic.getSurvivalBenifit_Guaranteed8());
		String PremiumBeforeST_ADTPD = commonforall.getRound(commonforall.getRoundOffLevel2New(commonforall.getStringWithout_E(smartbachatbusinesslogic.getPremiumBeforeST_ADTPD())));
		String ModalPremium = commonforall.getRoundUp(commonforall.getStringWithout_E(smartbachatbusinesslogic.getModalPremium()));
		//11-12-2019
		//String BasicInstallmentPrem =commonforall.getStringWithout_E(smartbachatbusinesslogic.get
		String BasicInstallmentPremFirstYear = commonforall.getRoundUp(commonforall.getStringWithout_E(smartbachatbusinesslogic.getST_firstYear()));
		String BasicInstallmentPremSecondYear =commonforall.getRoundUp( commonforall.getStringWithout_E(smartbachatbusinesslogic.getST_SecondYear()));
		String IntsallmentPremATPDB_FirstYear =  commonforall.getRoundUp(commonforall.getStringWithout_E(smartbachatbusinesslogic.getPremium_with_ATPDB_FirstYear()));
		String IntsallmentPremATPDB_SecondYear =  commonforall.getRoundUp(commonforall.getStringWithout_E(smartbachatbusinesslogic.getPremium_with_ATPDB_SecondYear()));
		//System.out.println("IntsallmentPremATPDB_SecondYear "+IntsallmentPremATPDB_SecondYear);



		/**
		 * Added by vrushali on 22 Dec 2016
		 */

		staffRebate = String.valueOf(smartbachatbusinesslogic.getStaffRebate());
		String totInstPrem_exclST_exclDisc = commonforall.getRoundUp(commonforall.getStringWithout_E(smartbachatbusinesslogic.getPremiumWithoutSTWithoutDisc(smartbachatbean.getSumAssured())));
		String totInstPrem_exclST_exclDisc_exclFreqLoading = commonforall
				.getStringWithout_E(smartbachatbusinesslogic
						.getPremiumWithoutSTWithoutDiscWithoutFreqLoading(smartbachatbean
								.getSumAssured()));
		String premiumBeforeSTWithoutDisc_ADTPD = commonforall
				.getStringWithout_E(smartbachatbusinesslogic
						.getPremiumBeforeSTWithoutDisc_ADTPD(smartbachatbean
								.getAdtpdbenifit()));
		String PremiumBeforeSTWithoutDiscWithoutLoading_ADTPD= commonforall.getStringWithout_E(smartbachatbusinesslogic.getPremiumBeforeSTWithoutDiscWithoutLoading_ADTPD(smartbachatbean.getAdtpdbenifit()));


		String ModalPremium1= commonforall.getRoundUp(commonforall.getStringWithout_E(smartbachatbusinesslogic.getAdtpd()));

		String ModalPremium2= commonforall.getRoundUp(commonforall.getStringWithout_E(smartbachatbusinesslogic.getAdtpd1()));

		String ModalPremium3= commonforall.getRoundUp(commonforall.getStringWithout_E(smartbachatbusinesslogic.getAdtpd2()));
		String ab =commonforall.getRoundUp(commonforall.getStringWithout_E(smartbachatbusinesslogic.getST_firstYear() + smartbachatbusinesslogic.getAdtpd1()));

		String ab1 =commonforall.getRoundUp(commonforall.getStringWithout_E(smartbachatbusinesslogic.getST_SecondYear() + smartbachatbusinesslogic.getAdtpd2()));

		MinesOccuInterest = commonforall
				.getStringWithout_E(smartbachatbusinesslogic
						.getMinesOccuInterest(smartbachatbean.getSumAssured()));

		servicetax_MinesOccuInterest = ""
				+ smartbachatbusinesslogic.getServiceTaxMines(Double.parseDouble(MinesOccuInterest));

		MinesOccuInterest = "" + (Double.parseDouble(MinesOccuInterest) + Double.parseDouble(servicetax_MinesOccuInterest));

		/** for testing **/
		totInstPrem_exclST_exclDisc = commonforall.getStringWithout_E(Double
				.parseDouble(totInstPrem_exclST_exclDisc));
		/** End **/

      /*  MinesOccuInterest = commonforall
				.getStringWithout_E(smartbachatbusinesslogic
						.getMinesOccuInterest(smartbachatbean.getSumAssured()));

        servicetax_MinesOccuInterest = ""
				+ smartbachatbusinesslogic
				.getServiceTaxMines(Double.parseDouble(MinesOccuInterest));

		MinesOccuInterest = "" + (Double.parseDouble(MinesOccuInterest) + Double.parseDouble(servicetax_MinesOccuInterest));


        *//** for testing **//*
		totInstPrem_exclST_exclDisc = commonforall.getStringWithout_E(Double
				.parseDouble(totInstPrem_exclST_exclDisc));
        *//** End **/

		if (rb_smart_bachat_backdating_yes.isChecked()) {
			// "16-jan-2014")));
			BackDateinterest = commonforall
					.getRoundUp(commonforall.getStringWithout_E(smartbachatbusinesslogic.getBackDateInterest(
							Double.parseDouble(FinalPremium_WithTaxes_1stYear),
							btn_smart_bachat_backdatingdate.getText()
									.toString())));

			BackDateinterestwithGST = commonforall.getRoundUp(commonforall.getStringWithout_E(Double.parseDouble(BackDateinterest) + (Double.parseDouble(BackDateinterest) * prop.GSTforbackdate)));
		} else {
			BackDateinterestwithGST = "0";
		}

		FinalPremium_WithTaxes_1stYear = commonforall.getRoundUp(commonforall
				.getStringWithout_E(Double
						.parseDouble(FinalPremium_WithTaxes_1stYear)
						+ Double.parseDouble(BackDateinterestwithGST)));

		/**************** End ******************/


//addede by sujata 11-12-2019
		return new String[]
				{premiumbefore_st, BasicServiceTax_firstYear, BasicServiceTax_secondYear,
						SwachBharat_1styear, SwachBharat_2ndyear, KrishiKalyan_1stYear, KrishiKalyan_2ndYear,
						TotalTaxes1st_year, TotalTaxes2nd_year, FinalPremium_WithTaxes_1stYear,
						FinalPremium_WithTaxes_2ndYear, SurvivalBenifit_Guaranteed, SurvivalBenifit_Guaranteed4,
						SurvivalBenifit_Guaranteed8, PremiumBeforeST_ADTPD,
						ModalPremium, staffRebate, totInstPrem_exclST_exclDisc,
						BackDateinterestwithGST, MinesOccuInterest,
						totInstPrem_exclST_exclDisc_exclFreqLoading,
						premiumBeforeSTWithoutDisc_ADTPD,
						PremiumBeforeSTWithoutDiscWithoutLoading_ADTPD,
						commonforall.getStringWithout_E(KeralaCessServiceTax),
						commonforall.getStringWithout_E(KeralaCessServiceTaxSecondYear), BasicInstallmentPremFirstYear, BasicInstallmentPremSecondYear
						,ab,ab1,ModalPremium1,ModalPremium2,ModalPremium3
				};

	}

	private void getInput(SmartBachatBean smartbachatbean) {

		inputVal = new StringBuilder();
		// From GUI Input
		boolean staffDisc = smartbachatbean.getIsForStaffOrNot();
		boolean isJKResident = smartbachatbean.getIsJammuResident();
		int policyTerm = smartbachatbean.getPolicyterm();
		String premFreq = smartbachatbean.getPremiumFrequency();
		double sumAssured = smartbachatbean.getSumAssured();
		String plantype = smartbachatbean.getPlantype();
		int premPayingterm = smartbachatbean.getPremiumpayingterm();

		String LifeAssured_title = spnr_bi_smart_bachat_life_assured_title
				.getSelectedItem().toString();
		String LifeAssured_firstName = edt_bi_smart_bachat_life_assured_first_name
				.getText().toString();
		String LifeAssured_middleName = edt_bi_smart_bachat_life_assured_middle_name
				.getText().toString();
		String LifeAssured_lastName = edt_bi_smart_bachat_life_assured_last_name
				.getText().toString();
		String LifeAssured_DOB = btn_bi_smart_bachat_life_assured_date
				.getText().toString();
		String LifeAssured_age = spinner_age.getSelectedItem().toString();
		String LifeAssured_gender = spinner_selGender.getSelectedItem()
				.toString();

		String proposer_title = "";
		String proposer_firstName = "";
		String proposer_middleName = "";
		String proposer_lastName = "";
		String proposer_DOB = "";
		String proposer_age = "";
		String proposer_gender = "";

		String wish_to_backdate_flag = "";
		if (rb_smart_bachat_backdating_yes.isChecked())
			wish_to_backdate_flag = "y";
		else
			wish_to_backdate_flag = "n";
		String backdate = "";
		if (wish_to_backdate_flag.equals("y"))
			backdate = btn_smart_bachat_backdatingdate.getText().toString();
		else
			backdate = "";

		if (!spnr_bi_smart_bachat_proposer_title.getSelectedItem().toString()
				.equals("")
				&& !spnr_bi_smart_bachat_proposer_title.getSelectedItem()
				.toString().equals("Select Title")) {
			proposer_title = spnr_bi_smart_bachat_proposer_title
					.getSelectedItem().toString();
			if (proposer_title.equals("Mr."))
				proposer_gender = "Male";
			else
				proposer_gender = "Female";
		}

		if (!edt_bi_smart_bachat_proposer_first_name.getText().toString()
				.equals(""))
			proposer_firstName = edt_bi_smart_bachat_proposer_first_name
					.getText().toString();

		if (!edt_bi_smart_bachat_proposer_middle_name.getText().toString()
				.equals(""))
			proposer_middleName = edt_bi_smart_bachat_proposer_middle_name
					.getText().toString();
		if (!edt_bi_smart_bachat_proposer_last_name.getText().toString()
				.equals(""))
			proposer_lastName = edt_bi_smart_bachat_proposer_last_name
					.getText().toString();

		if (!btn_bi_smart_bachat_proposer_date.getText().toString()
				.equals("Select Date")) {
			Calendar present_date = Calendar.getInstance();
			int tDay = present_date.get(Calendar.DAY_OF_MONTH);
			int tMonth = present_date.get(Calendar.MONTH);
			int tYear = present_date.get(Calendar.YEAR);
			proposer_DOB = btn_bi_smart_bachat_proposer_date.getText()
					.toString();
			proposer_age = ProposerAge;/*calculateMyAge(tYear, tMonth + 1, tDay,
					getDate1(proposer_DOB)) + "";*/
		}

		inputVal.append("<?xml version='1.0' encoding='utf-8' ?><smartbachat>");
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
		/* parivartan changes */
		inputVal.append("<product_Code>").append(product_Code).append("</product_Code>");
		inputVal.append("<product_UIN>").append(product_UIN).append("</product_UIN>");
		inputVal.append("<product_cateogory>").append(product_cateogory).append("</product_cateogory>");
		inputVal.append("<product_type>").append(product_type).append("</product_type>");
		/* end */
		inputVal.append("<proposer_Is_Same_As_Life_Assured>").append(proposer_Is_Same_As_Life_Assured).append("</proposer_Is_Same_As_Life_Assured>");
		inputVal.append("<isStaff>").append(staffDisc).append("</isStaff>");
		inputVal.append("<isJKResident>").append(isJKResident).append("</isJKResident>");
		inputVal.append("<age>").append(spinner_age.getSelectedItem().toString()).append("</age>");

		inputVal.append("<policyTerm>").append(policyTerm).append("</policyTerm>");
		inputVal.append("<premPayTerm>").append(premPayingterm).append("</premPayTerm>");
		inputVal.append("<premFreq>").append(premFreq).append("</premFreq>");
		inputVal.append("<sumAssured>").append(sumAssured).append("</sumAssured>");
		inputVal.append("<plan>").append(plantype).append("</plan>");
		inputVal.append("<adtpdbenfits>").append(et_adtpd_benifit.getText().toString()).append("</adtpdbenfits>");

		inputVal.append("<Product_Cat>").append(product_cateogory).append("</Product_Cat>");
		inputVal.append("<Wish_to_backdate_policy>").append(wish_to_backdate_flag).append("</Wish_to_backdate_policy>");
		inputVal.append("<backdating_Date>").append(backdate).append("</backdating_Date>");

		//Added Tushar Kadam Kerla Applicable Tax 7-Jun-2019
		String str_kerla_discount = "N";
		if (cb_kerladisc.isChecked()) {
			str_kerla_discount = "Y";
		}

		inputVal.append("<KFC>" + str_kerla_discount + "</KFC>");
		//End Added Tushar Kadam Kerla Applicable Tax 7-Jun-2019

		inputVal.append("</smartbachat>");

	}

	private int calculateMyAge(int year, int month, int day, String date) {
		Calendar nowCal = new GregorianCalendar(year, month, day);

		String[] ProposerDob = date.split("-");
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

	private boolean valLifeAssuredProposerDetail() {
		if (proposer_Is_Same_As_Life_Assured.equalsIgnoreCase("Y")) {
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
									setFocusable(spnr_bi_smart_bachat_life_assured_title);
									spnr_bi_smart_bachat_life_assured_title
											.requestFocus();
								} else if (lifeAssured_First_Name.equals("")) {
									setFocusable(edt_bi_smart_bachat_life_assured_first_name);
									edt_bi_smart_bachat_life_assured_first_name
											.requestFocus();
								} else {
									setFocusable(edt_bi_smart_bachat_life_assured_last_name);
									edt_bi_smart_bachat_life_assured_last_name
											.requestFocus();
								}
							}
						});
				showAlert.show();

				return false;
			} else if (gender.equalsIgnoreCase("")) {

				showAlert
						.setMessage("Please select Gender");
				showAlert.setNeutralButton("OK",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
												int which) {
								setFocusable(spnr_bi_smart_bachat_life_assured_title);
								spnr_bi_smart_bachat_life_assured_title
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
								setFocusable(spnr_bi_smart_bachat_life_assured_title);
								spnr_bi_smart_bachat_life_assured_title
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
								setFocusable(spnr_bi_smart_bachat_life_assured_title);
								spnr_bi_smart_bachat_life_assured_title
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
								setFocusable(spnr_bi_smart_bachat_life_assured_title);
								spnr_bi_smart_bachat_life_assured_title
										.requestFocus();
							}
						});
				showAlert.show();

				return false;
			} else {
				return true;
			}

		}else if (proposer_Is_Same_As_Life_Assured.equalsIgnoreCase("N")) {

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
									setFocusable(spnr_bi_smart_bachat_life_assured_title);
									spnr_bi_smart_bachat_life_assured_title
											.requestFocus();
								} else if (lifeAssured_First_Name.equals("")) {

									edt_bi_smart_bachat_life_assured_first_name
											.requestFocus();
								} else {
									edt_bi_smart_bachat_life_assured_last_name
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
								setFocusable(spnr_bi_smart_bachat_life_assured_title);
								spnr_bi_smart_bachat_life_assured_title
										.requestFocus();
							}
						});
				showAlert.show();

				return false;

			}else if (lifeAssured_Title.equalsIgnoreCase("Ms.")
					&& gender.equalsIgnoreCase("Male")) {

				showAlert
						.setMessage("Life Assured Title and Gender is not Valid");
				showAlert.setNeutralButton("OK",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
												int which) {
								// apply focusable method
								setFocusable(spnr_bi_smart_bachat_life_assured_title);
								spnr_bi_smart_bachat_life_assured_title
										.requestFocus();
							}
						});
				showAlert.show();

				return false;

			}else if (lifeAssured_Title.equalsIgnoreCase("Mrs.")
					&& gender.equalsIgnoreCase("Male")) {

				showAlert
						.setMessage("Life Assured Title and Gender is not Valid");
				showAlert.setNeutralButton("OK",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
												int which) {
								// apply focusable method
								setFocusable(spnr_bi_smart_bachat_life_assured_title);
								spnr_bi_smart_bachat_life_assured_title
										.requestFocus();
							}
						});
				showAlert.show();

				return false;
			}else if (proposer_Title.equals("")
					|| proposer_First_Name.equals("")
					|| proposer_Last_Name.equals("")) {

				showAlert.setMessage("Please Fill Name Detail For Proposer");
				showAlert.setNeutralButton("OK",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
												int which) {
								if (proposer_Title.equals("")) {
									// apply focusable method
									setFocusable(spnr_bi_smart_bachat_proposer_title);
									spnr_bi_smart_bachat_proposer_title
											.requestFocus();
								} else if (proposer_First_Name.equals("")) {
									edt_bi_smart_bachat_proposer_first_name
											.requestFocus();
								} else {
									edt_bi_smart_bachat_proposer_last_name
											.requestFocus();
								}
							}
						});
				showAlert.show();

				return false;

			}else if (gender_proposer.equalsIgnoreCase("")) {

				showAlert
						.setMessage("Please select Proposer Gender");
				showAlert.setNeutralButton("OK",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
												int which) {
								// apply focusable method
								setFocusable(spinner_selGender);
								spinner_selGender
										.requestFocus();
							}
						});
				showAlert.show();

				return false;

			}else if (proposer_Title.equalsIgnoreCase("Mr.")
					&& gender_proposer.equalsIgnoreCase("Female")) {

				showAlert.setMessage("Proposar Title and Gender is not Valid");
				showAlert.setNeutralButton("OK",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
												int which) {
								// apply focusable method
								setFocusable(spnr_bi_smart_bachat_proposer_title);
								spnr_bi_smart_bachat_proposer_title
										.requestFocus();
							}
						});
				showAlert.show();

				return false;

			} else if (proposer_Title.equalsIgnoreCase("Ms.")
					&& gender_proposer.equalsIgnoreCase("Male")) {

				showAlert.setMessage("Proposar Title and Gender is not Valid");
				showAlert.setNeutralButton("OK",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
												int which) {
								// apply focusable method
								setFocusable(spnr_bi_smart_bachat_proposer_title);
								spnr_bi_smart_bachat_proposer_title
										.requestFocus();
							}
						});
				showAlert.show();

				return false;

			} else if (proposer_Title.equalsIgnoreCase("Mrs.")
					&& gender_proposer.equalsIgnoreCase("Male")) {

				showAlert.setMessage("Proposar Title and Gender is not Valid");
				showAlert.setNeutralButton("OK",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
												int which) {
								// apply focusable method
								setFocusable(spnr_bi_smart_bachat_proposer_title);
								spnr_bi_smart_bachat_proposer_title
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
							@Override
							public void onClick(DialogInterface dialog,
												int which) {
								// apply focusable method
								setFocusable(btn_bi_smart_bachat_life_assured_date);
								btn_bi_smart_bachat_life_assured_date
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
	}

	private boolean sumassuredval() {
		StringBuilder error = new StringBuilder();
		if (et_sumassured.getText().toString().equals("")) {
			error.append("Please enter Sum Assured in Rs. ");

		} else if (Double.parseDouble(et_sumassured.getText().toString()) % 1000 != 0) {
			error.append("Sum assured should be multiple of 1,000");
		} else if (Double.parseDouble(et_sumassured.getText().toString()) < 100000) {
			error.append("Minimum Sum Assured Amount Should be more than Rs. 100000");

		}

		if (!error.toString().equals("")) {

			showAlert.setMessage(error.toString());
			showAlert.setNeutralButton("OK",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
						}
					});
			showAlert.show();
			return false;
		} else
			return true;
	}

	public boolean blankval() {

		String temp = et_sumassured.getText().toString();
		Boolean blankerror = false;
		if (!(temp.equals(""))) {
			sumassured = Integer.parseInt(et_sumassured.getText().toString());

			blankerror = false;
		}

		else {

			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
					BI_SmartBachatActivity.this);
			alertDialogBuilder.setMessage("Sum Assured field cannot be blank");
			alertDialogBuilder.setPositiveButton("Ok",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();

						}
					});

			AlertDialog alertDialog = alertDialogBuilder.create();
			alertDialog.show();

			blankerror = true;

		}

		return blankerror;

	}


	private boolean valBasicDetail() {

		if (edt_proposerdetail_basicdetail_contact_no.getText().toString()
				.equals("")) {
			commonMethods.dialogWarning(mContext, "Please Fill  Mobile No", true);
			edt_proposerdetail_basicdetail_contact_no.requestFocus();
			return false;
		} else if (edt_proposerdetail_basicdetail_contact_no.getText()
				.toString().length() != 10) {
			commonMethods.dialogWarning(mContext,"Please Fill 10 Digit Mobile No", true);
			edt_proposerdetail_basicdetail_contact_no.requestFocus();
			return false;
		}

		/*else if (emailId.equals("")) {
			commonMethods.dialogWarning(mContext,"Please Fill Email Id", true);
			edt_proposerdetail_basicdetail_Email_id.requestFocus();
			return false;

		}

		else if (ConfirmEmailId.equals("")) {

			commonMethods.dialogWarning(mContext,"Please Fill Confirm Email Id", true);
			edt_proposerdetail_basicdetail_ConfirmEmail_id.requestFocus();
			return false;
		} else if (!ConfirmEmailId.equals(emailId)) {
			commonMethods.dialogWarning(mContext,"Email Id Does Not Match", true);
			return false;
		}*/

		else if (!emailId.equals("")) {

			email_id_validation(emailId);
			if (validationFla1) {

				if (ConfirmEmailId.equals("")) {

					commonMethods.dialogWarning(mContext,"Please Fill Confirm Email Id", true);
					edt_proposerdetail_basicdetail_ConfirmEmail_id.requestFocus();
					return false;
				} else if (!ConfirmEmailId.equals(emailId)) {
					commonMethods.dialogWarning(mContext,"Email Id Does Not Match", true);
					return false;
				}

				return true;
			} else {
				commonMethods.dialogWarning(mContext,"Please Fill Valid Email Id", true);
				edt_proposerdetail_basicdetail_Email_id.requestFocus();
				return false;
			}
		}

		else if (!ConfirmEmailId.equals("")) {
			email_id_validation(ConfirmEmailId);
			if (validationFla1) {

				if (emailId.equals("")) {
					commonMethods.dialogWarning(mContext,"Please Fill Email Id", true);
					edt_proposerdetail_basicdetail_Email_id.requestFocus();
					return false;

				}

				return true;
			} else {
				commonMethods.dialogWarning(mContext,"Please Fill Valid Confirm Email Id", true);
				edt_proposerdetail_basicdetail_ConfirmEmail_id.requestFocus();
				return false;
			}
		}
		else {
			return true;
		}

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
		}

		else if (!cb_staffdisc.isChecked()
				&& (email_id.contains("@sbi.co.in")
				|| email_id.contains("@sbm.co.in")
				|| email_id.contains("@sbbj.co.in")
				|| email_id.contains("@sbhyd.co.in")
				|| email_id.contains("@sbp.co.in")
				|| email_id.contains("@sbt.co.in")
				|| email_id.contains("@sbi-life.com") || email_id
				.contains("@sbilife.co.in"))) {
			edt_proposerdetail_basicdetail_Email_id
					.setError("Please provide the Personal email address");
			validationFla1 = false;
		}

		else if ((matcher.matches())) {
			validationFla1 = true;
		}
	}


	private void initialiseDate() {
		Calendar calender = Calendar.getInstance();
		mYear = calender.get(Calendar.YEAR);
		mMonth = calender.get(Calendar.MONTH);
		mDay = calender.get(Calendar.DAY_OF_MONTH);

	}

	/** Used To Change date From mm-dd-yyyy to dd-mm-yyyy */
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

	private void Date() {
		Calendar c = Calendar.getInstance();
		System.out.println("Current time => " + c.getTime());

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		formattedDate = df.format(c.getTime());
		// formattedDate have current date/time
		// Toast.makeText(this, formattedDate, Toast.LENGTH_SHORT).show();
		c.add(Calendar.DATE, 30);
		ExpiredDate = df.format(c.getTime());
	}

	/** Used To Change date From dd-mm-yyyy to mm-dd-yyyy */
	private String getDate1(String OldDate) {
		String NewDate = "";
		try {
			String dateReceivedFromUser = OldDate;
			DateFormat userDateFormat = new SimpleDateFormat("dd-MM-yyyy");
			DateFormat dateFormatNeeded = new SimpleDateFormat("MM-dd-yyyy");
			Date date = userDateFormat.parse(dateReceivedFromUser);

			NewDate = dateFormatNeeded.format(date);

		} catch (Exception e) {
			// TODO: handle exception
			Log.e("Integrated Service", "Error in Getting Date");
		}

		return NewDate;
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

		@Override
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
		} else {
			age = tYear - mYear - 1;
		}

		String final_age = age + " yrs";

		if (final_age.contains("-")) {
			commonMethods.dialogWarning(mContext,"Please fill Valid Date", true);
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
					ProposerAge = final_age;
					final_age = Integer.toString(age);
					if (final_age.contains("-")) {
						commonMethods.BICommonDialog(mContext, "Please fill Valid Birth Date");
					} else {
						if (18 <= age) {

							btn_bi_smart_bachat_proposer_date.setText(date);
							// edt_bi_smart_bachat_life_assured_age
							// .setText(final_age);
							proposer_date_of_birth = getDate1(date + "");

							clearFocusable(btn_bi_smart_bachat_proposer_date);
							setFocusable(edt_proposerdetail_basicdetail_contact_no);
							edt_proposerdetail_basicdetail_contact_no
									.requestFocus();

						} else {
							commonMethods.BICommonDialog(mContext, "Minimum age should be 18 yrs for proposer");
							btn_bi_smart_bachat_proposer_date
									.setText("Select Date");
							// edt_bi_smart_bachat_life_assured_age
							// .setText("");
							proposer_date_of_birth = "";
							clearFocusable(btn_bi_smart_bachat_proposer_date);
							setFocusable(btn_bi_smart_bachat_proposer_date);
							btn_bi_smart_bachat_proposer_date.requestFocus();
						}
					}
					break;

				case 5:

					final_age = Integer.toString(age);
					if (final_age.contains("-")) {
						commonMethods.BICommonDialog(mContext, "Please fill Valid Birth Date");
					} else {
						int minLimit = 0;
						if (spinner_select_plan.getSelectedItem().toString()
								.equals("Option A (Endowment Option)")) {
							minLimit = 6;
						} else if (spinner_select_plan
								.getSelectedItem()
								.toString()
								.equals("Option B (Endowment Option with in-built Accidental Death and Total Permanent Disability (AD&TPD) Benefit)")) {
							minLimit = 18;
						}

						if (minLimit <= age && age <= 50) {
							lifeAssuredAge = final_age;
							btn_bi_smart_bachat_life_assured_date.setText(date);

							String[] ageList = new String[55];
							for (int i = 6; i <= 60; i++) {
								ageList[i - 6] = i + "";
							}
							ArrayAdapter<String> ageAdapter = new ArrayAdapter<String>(
									getApplicationContext(), R.layout.spinner_item,
									ageList);
							ageAdapter
									.setDropDownViewResource(R.layout.spinner_item1);
							spinner_age.setAdapter(ageAdapter);
							ageAdapter.notifyDataSetChanged();

							spinner_age.setSelection(
									getIndex(spinner_age, final_age), false);

							lifeAssured_date_of_birth = getDate1(date + "");

							if (Integer.parseInt(final_age) < 18) {
								tr_smart_bachat_proposer_detail1
										.setVisibility(View.VISIBLE);
								tr_smart_bachat_proposer_detail2
										.setVisibility(View.VISIBLE);
								proposer_Is_Same_As_Life_Assured = "n";

								clearFocusable(btn_bi_smart_bachat_life_assured_date);
								setFocusable(spnr_bi_smart_bachat_proposer_title);
								spnr_bi_smart_bachat_proposer_title.requestFocus();
							} else {

								tr_smart_bachat_proposer_detail1
										.setVisibility(View.GONE);
								tr_smart_bachat_proposer_detail2
										.setVisibility(View.GONE);
								proposer_Is_Same_As_Life_Assured = "y";

								clearFocusable(btn_bi_smart_bachat_life_assured_date);
								setFocusable(edt_proposerdetail_basicdetail_contact_no);
								edt_proposerdetail_basicdetail_contact_no
										.requestFocus();
							}

							btn_smart_bachat_backdatingdate.setText("Select Date");
							proposer_Backdating_BackDate = "";

						} else {
							commonMethods.BICommonDialog(mContext, "Minimum Age should be "
									+ minLimit
									+ " yrs and Maximum Age should be  50 yrs For LifeAssured");
							btn_bi_smart_bachat_life_assured_date
									.setText("Select Date");
							lifeAssured_date_of_birth = "";

							String[] ageList = new String[55];
							for (int i = minLimit; i <= 60; i++) {
								ageList[i - minLimit] = i + "";
							}
							ArrayAdapter<String> ageAdapter = new ArrayAdapter<String>(
									getApplicationContext(), R.layout.spinner_item,
									ageList);
							ageAdapter
									.setDropDownViewResource(R.layout.spinner_item1);
							spinner_age.setAdapter(ageAdapter);
							ageAdapter.notifyDataSetChanged();

							btn_smart_bachat_backdatingdate.setText("Select Date");
							proposer_Backdating_BackDate = "";

							clearFocusable(btn_bi_smart_bachat_life_assured_date);
							setFocusable(btn_bi_smart_bachat_life_assured_date);
							btn_bi_smart_bachat_life_assured_date.requestFocus();
						}

					}
					break;

				case 6:
					if (age >= 0) {
						proposer_Backdating_BackDate = date + "";

                       /* if(TrueBackdateLaunch())
                        {*/
						btn_smart_bachat_backdatingdate
								.setText(proposer_Backdating_BackDate);
						clearFocusable(btn_smart_bachat_backdatingdate);

						setFocusable(spinner_age);
						spinner_age.requestFocus();

					} else {
						commonMethods.dialogWarning(mContext,"Please Select Valid BackDating Date", true);
						btn_smart_bachat_backdatingdate.setText("Select Date");
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
			spinner_age.setSelection(getIndex(spinner_age, str_final_Age),
					false);
			// valAge();

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

	public void onClickProposerDob(View v) {
		initialiseDateParameter(proposer_date_of_birth, 35);
		DIALOG_ID = 4;
		showDialog(DATE_DIALOG_ID);
	}

	public void onClickLADob(View v) {
		initialiseDateParameter(lifeAssured_date_of_birth, 35);
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
			commonMethods.dialogWarning(mContext,"Please select a LifeAssured DOB First", true);
		}
	}

	private void Dialog() {

		final Dialog d = new Dialog(this);
		d.requestWindowFeature(Window.FEATURE_NO_TITLE);
		d.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.BLACK));

		d.setContentView(R.layout.layout_smart_bachat_bi_grid);


		TextView tv_channel_intermediary = (TextView) d
				.findViewById(R.id.tv_channel_intermediary);
		tv_channel_intermediary.setText(userType);


		TextView tv_proposername = (TextView) d
				.findViewById(R.id.tv_proposername);
		TextView tv_proposal_number = (TextView) d
				.findViewById(R.id.tv_proposal_number);

		TextView tv_bi_smart_bachat_life_assured_name = d
				.findViewById(R.id.tv_bi_smart_bachat_life_assured_name);
		TextView tv_bi_smart_bachat_life_assured_age = d
				.findViewById(R.id.tv_bi_smart_bachat_life_assured_age);
		TextView tv_bi_smart_bachat_life_assured_gender = d
				.findViewById(R.id.tv_bi_smart_bachat_life_assured_gender);
		TextView tv_bi_smart_bachat_life_assured_premium_frequency = d
				.findViewById(R.id.tv_bi_smart_bachat_life_assured_premium_frequency);

		TextView tv_bi_smart_bachat_plan_proposed = d
				.findViewById(R.id.tv_bi_smart_bachat_plan_proposed);
		TextView tv_bi_smart_bachat_term = d
				.findViewById(R.id.tv_bi_smart_bachat_term);
		TextView tv_bi_smart_bachat_premium_paying_term = d
				.findViewById(R.id.tv_bi_smart_bachat_premium_paying_term);
		TextView tv_bi_smart_bachat_sum_assured = d
				.findViewById(R.id.tv_bi_smart_bachat_sum_assured);
		TextView tv_premium_text_for_option_heading = d
				.findViewById(R.id.tv_premium_text_for_option_heading);

		TextView tv_bi_smart_bachat_rate_of_applicable_taxes = (TextView)d.
				findViewById(R.id.tv_bi_smart_bachat_rate_of_applicable_taxes);


		TextView tv_bi_smart_bachat_accidental_death_disability_benefit_sum_assured = (TextView) d
				.findViewById(R.id.tv_bi_smart_bachat_accidental_death_disability_benefit_sum_assured);


		TextView tv_bi_smart_bachat_yearly_premium_with_tax = (TextView) d
				.findViewById(R.id.tv_bi_smart_bachat_yearly_premium_with_tax);

		// Second year tables

		final TextView tv_premium_install_rider_type1 = d
				.findViewById(R.id.tv_premium_install_rider_type1);

		final TextView tv_mandatory_bi_smart_bachat_yearly_premium_with_tax1 = d
				.findViewById(R.id.tv_mandatory_bi_smart_bachat_yearly_premium_with_tax1);

		// First year policy
		TextView tv_bi_smart_bachat_basic_premium_first_year = d
				.findViewById(R.id.tv_bi_smart_bachat_basic_premium_first_year);
		TextView tv_bi_smart_bachat_service_tax_first_year = d
				.findViewById(R.id.tv_bi_smart_bachat_service_tax_first_year);
		TextView tv_bi_smart_bachat_yearly_premium_with_tax_first_year = d
				.findViewById(R.id.tv_bi_smart_bachat_yearly_premium_with_tax_first_year);

		// Seconf year policy onwards
		TextView tv_bi_smart_bachat_basic_premium_second_year = d
				.findViewById(R.id.tv_bi_smart_bachat_basic_premium_second_year);
		TextView tv_bi_smart_bachat_service_tax_second_year = d
				.findViewById(R.id.tv_bi_smart_bachat_service_tax_second_year);
		TextView tv_bi_smart_bachat_yearly_premium_with_tax_second_year = d
				.findViewById(R.id.tv_bi_smart_bachat_yearly_premium_with_tax_second_year);

		final TextView edt_proposer_name = d
				.findViewById(R.id.edt_ProposalName);

		// final EditText edt_proposer_name_e_sign = (EditText) d
		// .findViewById(R.id.edt_proposer_name_e_sign);

		GridView gv_userinfo = d.findViewById(R.id.gv_userinfo);

		final EditText edt_Policyholderplace = d
				.findViewById(R.id.edt_Policyholderplace);
		final EditText edt_MarketingOfficalPlace = d
				.findViewById(R.id.edt_MarketingOfficalPlace);

		TextView tv_bi_is_Staff = d
				.findViewById(R.id.tv_bi_is_Staff);

		TextView tv_bi_is_jk = d.findViewById(R.id.tv_bi_is_jk);

		TextView tv_bi_smart_bachat_basic_service_tax_first_year = d
				.findViewById(R.id.tv_bi_smart_bachat_basic_service_tax_first_year);
		TextView tv_bi_smart_bachat_swachh_bharat_cess_first_year = d
				.findViewById(R.id.tv_bi_smart_bachat_swachh_bharat_cess_first_year);
		TextView tv_bi_smart_bachat_krishi_kalyan_cess_first_year = d
				.findViewById(R.id.tv_bi_smart_bachat_krishi_kalyan_cess_first_year);

		TextView tv_bi_smart_bachat_basic_service_tax_second_year = d
				.findViewById(R.id.tv_bi_smart_bachat_basic_service_tax_second_year);
		TextView tv_bi_smart_bachat_swachh_bharat_cess_second_year = d
				.findViewById(R.id.tv_bi_smart_bachat_swachh_bharat_cess_second_year);
		TextView tv_bi_smart_bachat_krishi_kalyan_cess_second_year = d
				.findViewById(R.id.tv_bi_smart_bachat_krishi_kalyan_cess_second_year);

		TextView tv_uin_smart_bachat = (TextView) d
				.findViewById(R.id.tv_uin_smart_bachat);

      /*  LinearLayout ll_accidental_death = (LinearLayout) d
				.findViewById(R.id.ll_accidental_death);

        View view_accidental_death = (View) d
                .findViewById(R.id.view_accidental_death);*/

		LinearLayout tr_bi_smart_bachat_for_option_b =  d
				.findViewById(R.id.tr_bi_smart_bachat_for_option_b);

		TextView tv_bi_smart_bachat_premium_accidental_death_disability_name = d
				.findViewById(R.id.tv_bi_smart_bachat_premium_accidental_death_disability_name);

		TextView tv_bi_smart_bachat_premium_accidental_death_disability_value = d
				.findViewById(R.id.tv_bi_smart_bachat_premium_accidental_death_disability_value);

		TextView tv_bi_smart_bachat_premium_excluding_service_tax_name = d
				.findViewById(R.id.tv_bi_smart_bachat_premium_excluding_service_tax_name);

		TextView tv_bi_smart_bachat_premium_excluding_service_tax_value = d
				.findViewById(R.id.tv_bi_smart_bachat_premium_excluding_service_tax_value);


		TextView tv_bi_smart_bachat_premium_excluding_service_tax_value_2 = d
				.findViewById(R.id.tv_bi_smart_bachat_premium_excluding_service_tax_value_2);
		TextView tv_bi_smart_bachat_premium_accidental_death_disability_value_2 = d
				.findViewById(R.id.tv_bi_smart_bachat_premium_accidental_death_disability_value_2);




		TextView tv_bi_smart_bachat_backdating_interest = d
				.findViewById(R.id.tv_bi_smart_bachat_backdating_interest);

		TableRow tr_bi_smart_bachat_backdating = d
				.findViewById(R.id.tr_bi_smart_bachat_backdating);

		View view_backdating = d.findViewById(R.id.view_backdating);

		TextView tv_smart_bachat_data = d
				.findViewById(R.id.tv_smart_bachat_data);

		TextView tv_proposerage = (TextView) d
				.findViewById(R.id.tv_proposerage);
		TextView tv_proposergender = (TextView)d.findViewById(R.id.tv_proposergender);



		TextView tv_bi_smart_bachat_installment_premium = (TextView) d
				.findViewById(R.id.tv_bi_smart_bachat_installment_premium);
		TextView tv_bi_smart_bachat_sum_assured_on_death = (TextView) d
				.findViewById(R.id.tv_bi_smart_bachat_sum_assured_on_death);
		TextView tv_bi_smart_bachat_AD_TPD_sum_assured = (TextView) d
				.findViewById(R.id.tv_bi_smart_bachat_AD_TPD_sum_assured);
		TextView tv_bi_smart_bachat_base_premium_without_tax = (TextView) d
				.findViewById(R.id.tv_bi_smart_bachat_base_premium_without_tax);
		TextView tv_bi_smart_bachat_AD_TPD_premium_without_tax = (TextView) d
				.findViewById(R.id.tv_bi_smart_bachat_AD_TPD_premium_without_tax);
		TextView tv_bi_smart_bachat_Rider_premium_without_tax = (TextView) d
				.findViewById(R.id.tv_bi_smart_bachat_Rider_premium_without_tax);
		TextView tv_bi_smart_bachat_total_installment_premium_without_tax = (TextView) d
				.findViewById(R.id.tv_bi_smart_bachat_total_installment_premium_without_tax);
		TextView tv_bi_smart_bachat_base_premium_with_tax_first_year = (TextView) d
				.findViewById(R.id.tv_bi_smart_bachat_base_premium_with_tax_first_year);
		TextView tv_bi_smart_bachat_AD_TPD_premium_with_tax_first_year = (TextView) d
				.findViewById(R.id.tv_bi_smart_bachat_AD_TPD_premium_with_tax_first_year);
		TextView tv_bi_smart_bachat_Rider_premium_with_tax_first_year = (TextView) d
				.findViewById(R.id.tv_bi_smart_bachat_Rider_premium_with_tax_first_year);
		TextView tv_bi_smart_bachat_total_installment_premium_with_tax_first_year = (TextView) d
				.findViewById(R.id.tv_bi_smart_bachat_total_installment_premium_with_tax_first_year);
		TextView tv_bi_smart_bachat_base_premium_with_tax_second_year = (TextView) d
				.findViewById(R.id.tv_bi_smart_bachat_base_premium_with_tax_second_year);
		TextView tv_bi_smart_bachat_AD_TPD_premium_with_tax_second_year = (TextView) d
				.findViewById(R.id.tv_bi_smart_bachat_AD_TPD_premium_with_tax_second_year);
		TextView tv_bi_smart_bachat_Rider_premium_with_tax_second_year = (TextView) d
				.findViewById(R.id.tv_bi_smart_bachat_Rider_premium_with_tax_second_year);
		TextView tv_bi_smart_bachat_total_installment_premium_with_tax_second_year = (TextView) d
				.findViewById(R.id.tv_bi_smart_bachat_total_installment_premium_with_tax_second_year);

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
		/* parivartan changes */
		final Context context = this;
		imageButtonSmartBachatProposerPhotograph = d
				.findViewById(R.id.imageButtonSmartBachatProposerPhotograph);
		imageButtonSmartBachatProposerPhotograph
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View view) {

						Check = "Photo";
						commonMethods.windowmessage(context, "_cust1Photo.jpg");
					}
				});
		/* end */

		TextView tv_state = (TextView)d.findViewById(R.id.tv_state);
		if (cb_kerladisc.isChecked()) {
			tv_state.setText("Kerala");
			tv_bi_smart_bachat_rate_of_applicable_taxes.setText("4.75% in the 1st policy year and 2.375% from 2nd policy year onwards");
		} else {
			tv_state.setText("Non Kerala");
			tv_bi_smart_bachat_rate_of_applicable_taxes.setText("4.5% in the 1st policy year and 2.25% from 2nd policy year onwards");
		}

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
							+ ", have undergone the Need Analysis  after having reviewed the SBI Life Product options, I have opted for SBI LIFE - Smart Bachat.");

			age_entry = prsObj.parseXmlTag(inputVal.toString(), "age");

			tv_proposerage.setText(age_entry);
			tv_proposergender.setText(gender);

			//ProposerAge = lifeAssuredAge;
			tv_bi_smart_bachat_life_assured_name.setText(name_of_life_assured);
			tv_proposername.setText(name_of_life_assured);
		} else if (proposer_Is_Same_As_Life_Assured.equalsIgnoreCase("n")) {
			edt_proposer_name
					.setText("I, "
							+ name_of_proposer
							+ ", having received the information with respect to the above, have understood the above statement before entering into the contract.");
			edt_proposer_name_need_analysis
					.setText("I, "
							+ name_of_proposer
							+ ", have undergone the Need Analysis  after having reviewed the SBI Life Product options, I have opted for SBI LIFE - Smart Bachat.");

			tv_proposername.setText(name_of_proposer);
			tv_bi_smart_bachat_life_assured_name.setText(name_of_proposer);
			ProposerAge = prsObj.parseXmlTag(inputVal.toString(), "proposer_age");
			gender_proposer = prsObj.parseXmlTag(inputVal.toString(), "proposer_gender");
			tv_proposergender.setText(gender_proposer);
			System.out.println("ProposerAge = "+ProposerAge);
			tv_proposerage.setText(ProposerAge + " Years");

			/* parivartan changes */
			LinearLayout linearlayoutLifeAssuredSignature = d
					.findViewById(R.id.linearlayoutLifeAssuredSignature);
			Ibtn_signatureofLifeAssured = d
					.findViewById(R.id.Ibtn_signatureofLifeAssured);

			linearlayoutLifeAssuredSignature.setVisibility(View.GONE);
			if (proposerAsLifeAssuredSign != null
					&& !proposerAsLifeAssuredSign.equals("")) {
				byte[] signByteArray = Base64.decode(proposerAsLifeAssuredSign,
						0);
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
								commonMethods.windowmessageProposersgin(
										context, NeedAnalysisActivity.URN_NO
												+ "_cust2sign");
							} else {
								commonMethods.dialogWarning(mContext,"Please Tick on I Agree Clause ", true);
								setFocusable(cb_statement);
								cb_statement.requestFocus();
							}

						}
					});
			/* end */
		}

		TextView textviewAGentStatement = d.findViewById(R.id.textviewAGentStatement);
		textviewAGentStatement.setText(commonMethods.getAgentDeclaration(context));
		tv_proposal_number.setText(QuatationNumber);

		String place1 = "";
		edt_MarketingOfficalPlace.setText(place1);

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
							commonMethods.dialogWarning(mContext,"Please Tick on I Agree Clause ", true);
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
							/* parivartan changes */
							commonMethods.windowmessageProposersgin(context,
									NeedAnalysisActivity.URN_NO + "_cust1sign");
							/* end */
						} else {
							commonMethods.dialogWarning(mContext,"Please Tick on I Agree Clause ", true);
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
							commonMethods.dialogWarning(mContext,"Please Tick on I Agree Clause ", true);
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
			imageButtonSmartBachatProposerPhotograph
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
							commonMethods.dialogWarning(mContext,"Please Tick on I Agree Clause ", true);
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
							imageButtonSmartBachatProposerPhotograph
									.setImageDrawable(getResources()
											.getDrawable(
													R.drawable.focus_imagebutton_photo));

							RadioGroup radioGroupDepositPayment = d
									.findViewById(R.id.radioGroupDepositPayment);
							radioGroupDepositPayment.clearCheck();

							RadioGroup radioGroupAppointee = d
									.findViewById(R.id.radioGroupAppointee);
							radioGroupAppointee.clearCheck();

							if (proposer_Is_Same_As_Life_Assured
									.equalsIgnoreCase("n")) {

								String lifeassuredSignName = NeedAnalysisActivity.URN_NO
										+ "_cust2sign.png";
								File lifeassuredSignFile = mStorageUtils.createFileToAppSpecificDir(context,
										lifeassuredSignName);

								if (lifeassuredSignFile.exists()) {
									lifeassuredSignFile.delete();
								}

								proposerAsLifeAssuredSign = "";
								Ibtn_signatureofLifeAssured
										.setImageBitmap(null);
							}
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

			@Override
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
								/*&& ((radioButtonDepositPaymentNo.isChecked() == true && !thirdPartySign
										.equals("")) || radioButtonDepositPaymentYes
										.isChecked() == true)
								&& ((radioButtonAppointeeYes.isChecked() == true && !appointeeSign
										.equals("")) || radioButtonAppointeeNo
										.isChecked() == true) && ((!proposerAsLifeAssuredSign
								.equals("") && proposer_Is_Same_As_Life_Assured
								.equalsIgnoreCase("n")) || proposer_Is_Same_As_Life_Assured
								.equalsIgnoreCase("y"))*/
				) && radioButtonTrasactionModeParivartan
						.isChecked()) || radioButtonTrasactionModeManual
						.isChecked())) {
					NeedAnalysisActivity.str_need_analysis = "";

					// String isActive = "0";
					String productCode = "";

					if (plan.equalsIgnoreCase("Option A (Endowment Option)")) {
						productCode = "2DSBPA";
					} else if (plan
							.equalsIgnoreCase("Option B (Endowment Option with in-built Accidental Death and Total Permanent Disability (AD&TPD) Benefit)")) {
						productCode = "2DSBPB";
					}

					na_cbi_bean = new NA_CBI_bean(QuatationNumber, agentcode,
							"", userType, "", lifeAssured_Title,
							lifeAssured_First_Name, lifeAssured_Middle_Name,
							lifeAssured_Last_Name, planName,
							commonforall.getRound(obj.getStringWithout_E(Double
									.valueOf(sum_assured.equals("") ? "0"
											: sum_assured))), commonforall
							.getRound(basicplustax), emailId, mobileNo,
							agentEmail, agentMobile, na_input, na_output,
							premiumPaymentMode, Integer.parseInt(policy_term),
							0, productCode, getDate(lifeAssured_date_of_birth),
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
							lifeAssured_Middle_Name, lifeAssured_Last_Name,
							commonforall.getRound(obj.getStringWithout_E(Double
									.valueOf(sum_assured.equals("") ? "0"
											: sum_assured))), commonforall
							.getRound(basicplustax), agentEmail,
							agentMobile, na_input, na_output,
							premiumPaymentMode, Integer.parseInt(policy_term),
							0, productCode, getDate(lifeAssured_date_of_birth),
							getDate(proposer_date_of_birth), "",mode,inputVal
							.toString(), retVal.toString().replace(
							bussIll.toString(), "")));

					CreateSmartBachat_BIPdf();


					NABIObj.serviceHit(BI_SmartBachatActivity.this,
							na_cbi_bean, newFile, needAnalysispath.getPath(),
							mypath.getPath(), name_of_person, QuatationNumber,mode);
					d.dismiss();

				} else {

					if (proposer_sign.equals("")
							&& !bankUserType.equalsIgnoreCase("Y")) {
						commonMethods.dialogWarning(mContext,"Please Make Signature for Proposer ", true);
						setFocusable(Ibtn_signatureofPolicyHolders);
						Ibtn_signatureofPolicyHolders.requestFocus();
					}
					if (place2.equals("")) {
						commonMethods.dialogWarning(mContext,"Please Fill Place Detail", true);
						setFocusable(edt_Policyholderplace);
						edt_Policyholderplace.requestFocus();

					} else if (agent_sign.equals("")
							&& !bankUserType.equalsIgnoreCase("Y")) {
						commonMethods.dialogWarning(mContext,"Please Make Signature for Sales Representative",
								true);
						setFocusable(Ibtn_signatureofMarketing);
						Ibtn_signatureofMarketing.requestFocus();
					}

					else if (!cb_statement.isChecked()) {
						commonMethods.dialogWarning(mContext,"Please Tick on I Agree Clause ", true);
						setFocusable(cb_statement);
						cb_statement.requestFocus();
					} else if (!checkboxAgentStatement.isChecked()) {
						commonMethods.dialogWarning(context, "Please Tick on I Agree Clause ", true);
						commonMethods.setFocusable(checkboxAgentStatement);
						checkboxAgentStatement.requestFocus();
					} else if (!radioButtonTrasactionModeParivartan.isChecked()
							&& !radioButtonTrasactionModeManual.isChecked()) {
						commonMethods.dialogWarning(mContext,"Please Select Transaction Mode", true);
						setFocusable(linearlayoutTrasactionModeParivartan);
						linearlayoutTrasactionModeParivartan.requestFocus();
					} else if (photoBitmap == null) {
						commonMethods.dialogWarning(mContext,"Please Capture the Photo", true);
						setFocusable(imageButtonSmartBachatProposerPhotograph);
						imageButtonSmartBachatProposerPhotograph.requestFocus();
					} else if (!radioButtonDepositPaymentYes.isChecked()
							&& !radioButtonDepositPaymentNo.isChecked()) {
						commonMethods.dialogWarning(mContext,"Please Select Third Party Payment", true);
						setFocusable(linearlayoutThirdpartySignature);
						linearlayoutThirdpartySignature.requestFocus();
					} else if (radioButtonDepositPaymentNo.isChecked()
							&& thirdPartySign.equals("")) {
						commonMethods.dialogWarning(mContext,"Please Make Signature for Third party ", true);
						setFocusable(Ibtn_signatureofThirdParty);
						Ibtn_signatureofThirdParty.requestFocus();
					} else if (!radioButtonAppointeeYes.isChecked()
							&& !radioButtonAppointeeNo.isChecked()) {
						commonMethods.dialogWarning(mContext,"Please Select Appointee Payment", true);
						setFocusable(linearlayoutAppointeeSignature);
						linearlayoutAppointeeSignature.requestFocus();
					} else if (radioButtonAppointeeYes.isChecked()
							&& appointeeSign.equals("")) {
						commonMethods.dialogWarning(mContext,"Please Make Signature for Appointee ", true);
						setFocusable(Ibtn_signatureofAppointee);
						Ibtn_signatureofAppointee.requestFocus();
					} else if (proposer_Is_Same_As_Life_Assured
							.equalsIgnoreCase("n")
							&& proposerAsLifeAssuredSign.equals("")) {
						commonMethods.dialogWarning(mContext,"Please Make Signature for Life Assured ", true);
						setFocusable(Ibtn_signatureofLifeAssured);
						Ibtn_signatureofLifeAssured.requestFocus();
					}
					/* end */
				}

			}

		});

		btn_PolicyholderDate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				DIALOG_ID = 2;
				showDialog(DATE_DIALOG_ID);
			}
		});

		btn_MarketingOfficalDate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				DIALOG_ID = 3;
				showDialog(DATE_DIALOG_ID);

			}
		});

		String input = inputVal.toString();
		output = retVal.toString();

		tv_uin_smart_bachat
				.setText("Benefit Illustration(BI): SBI Life - Smart Bachat (UIN No -"
						+ "111N108V03" + ")" + "\n" + "An Individual, Non-Linked, Participating, Life Insurance Savings Product");
		tv_bi_smart_bachat_life_assured_name.setText(name_of_life_assured);
		age_entry = prsObj.parseXmlTag(input, "age");
		tv_bi_smart_bachat_life_assured_age.setText(age_entry + " Years");

		gender = prsObj.parseXmlTag(input, "gender");

		tv_bi_smart_bachat_life_assured_gender.setText(gender);

		premium_paying_frequency = prsObj.parseXmlTag(input, "premFreq");
		tv_bi_smart_bachat_life_assured_premium_frequency
				.setText(premium_paying_frequency);

		staffdiscount = prsObj.parseXmlTag(input, "isStaff");

		if (staffdiscount.equalsIgnoreCase("true")) {
			tv_bi_is_Staff.setText("Yes");
		} else {
			tv_bi_is_Staff.setText("No");
		}

		isJkResident = prsObj.parseXmlTag(input, "isJKResident");

		if (isJkResident.equalsIgnoreCase("true")) {
			tv_bi_is_jk.setText("Yes");
		} else {
			tv_bi_is_jk.setText("No");
		}

		plan = prsObj.parseXmlTag(input, "plan");
		tv_bi_smart_bachat_plan_proposed.setText(plan);

		policy_term = prsObj.parseXmlTag(input, "policyTerm");
		tv_bi_smart_bachat_term.setText(policy_term + " Years");

		premPayTerm = prsObj.parseXmlTag(input, "premPayTerm");
		String payingTerm = "";

		payingTerm = premPayTerm + " Years";

		tv_bi_smart_bachat_premium_paying_term.setText(payingTerm);

		sum_assured = prsObj.parseXmlTag(input, "sumAssured");

		tv_bi_smart_bachat_sum_assured.setText("Rs. "
				+ getformatedThousandString(Integer.parseInt(obj.getRound(obj
				.getStringWithout_E(Double.valueOf((sum_assured
						.equals("") || sum_assured == null) ? "0"
						: sum_assured))))));

		adtpdbenfits = prsObj.parseXmlTag(input, "adtpdbenfits");
		AD_TPD_sum_assured = getformatedThousandString(Integer.parseInt(obj.getRound(obj
				.getStringWithout_E(Double.valueOf((adtpdbenfits
						.equals("") || adtpdbenfits == null) ? "0"
						: adtpdbenfits)))));

		if (plan.equalsIgnoreCase("Option A (Endowment Option)"))
		{
			tv_bi_smart_bachat_accidental_death_disability_benefit_sum_assured.setText("Rs.0");
			tv_bi_smart_bachat_AD_TPD_sum_assured.setText("Rs.0");
			AD_TPD_sum_assured = "0";

			tv_premium_text_for_option_heading.setText("Premium for Option A (Endowment Option)");

		}
		else
		{
			tv_premium_text_for_option_heading.setText("Option B (Endowment Option with in-built Accidental Death and Total Permanent Disability (AD&TPD) Benefit)");
			tv_bi_smart_bachat_accidental_death_disability_benefit_sum_assured.setText("Rs."
					+ getformatedThousandString(Integer.parseInt(obj.getRound(obj
					.getStringWithout_E(Double.valueOf((adtpdbenfits
							.equals("") || adtpdbenfits == null) ? "0"
							: adtpdbenfits))))));


			tv_bi_smart_bachat_AD_TPD_sum_assured.setText("Rs."
					+ AD_TPD_sum_assured);

		}

		IntsallmentPremATPDB_SecondYear = prsObj.parseXmlTag(output, "IntsallmentPremATPDB_SecondYear");

		BasicInstallmentPremFirstYear = prsObj.parseXmlTag(output, "BasicInstallmentPremFirstYear");

		BasicInstallmentPremSecondYear = prsObj.parseXmlTag(output, "BasicInstallmentPremSecondYear");


		modalprem = prsObj.parseXmlTag(output, "modalprem");

		basicprem = prsObj.parseXmlTag(output, "basicPrem");

		adtpdRiderprem = prsObj.parseXmlTag(output, "adtpdRiderprem");
		IntsallmentPremATPDB_FirstYear = prsObj.parseXmlTag(output, "IntsallmentPremATPDB_FirstYear");

		modalprem1 = prsObj.parseXmlTag(output, "modalprem1");
		modalprem2 = prsObj.parseXmlTag(output, "modalprem2");
		modalprem3 = prsObj.parseXmlTag(output, "modalprem3");

		if (plan.equalsIgnoreCase("Option A (Endowment Option)"))
		{
			basicPremWithoutDisc = prsObj.parseXmlTag(output, "basicPremWithoutDisc");
			tv_bi_smart_bachat_total_installment_premium_with_tax_first_year.setText((int) (Double.parseDouble
					(BasicInstallmentPremFirstYear.equals("") || (BasicInstallmentPremFirstYear.equals(null)) ? "0" : BasicInstallmentPremFirstYear)) + "");

			tv_bi_smart_bachat_total_installment_premium_with_tax_second_year.setText((int) (Double.parseDouble
					(BasicInstallmentPremSecondYear.equals("") || (BasicInstallmentPremSecondYear.equals(null)) ? "0" : BasicInstallmentPremSecondYear)) + "");
		}
		else
		{
			basicPremWithoutDisc = String.valueOf(Double.parseDouble(prsObj.parseXmlTag(output, "basicPremWithoutDisc")) +
					Double.parseDouble(prsObj.parseXmlTag(output, "modalprem1")) );
			//basicPremWithoutDisc = prsObj.parseXmlTag(output, "AdTpdPremWithoutDiscSA");
			tv_bi_smart_bachat_total_installment_premium_with_tax_first_year.setText((int) (Double.parseDouble
					(IntsallmentPremATPDB_FirstYear.equals("") || (IntsallmentPremATPDB_FirstYear.equals(null)) ? "0" : IntsallmentPremATPDB_FirstYear)) + "");

			tv_bi_smart_bachat_total_installment_premium_with_tax_second_year.setText((int) (Double.parseDouble
					(IntsallmentPremATPDB_SecondYear.equals("") || (IntsallmentPremATPDB_SecondYear.equals(null)) ? "0" : IntsallmentPremATPDB_SecondYear)) + "");
		}


		tv_bi_smart_bachat_premium_accidental_death_disability_value
				.setText(" Rs. "
						+ obj.getRound(obj.getStringWithout_E(Double
						.valueOf(adtpdRiderprem.equals("") ? "0"
								: adtpdRiderprem))));

		tv_bi_smart_bachat_premium_accidental_death_disability_value_2
				.setText(" Rs."
						+ obj.getRound(obj.getStringWithout_E(Double
						.valueOf(adtpdRiderprem.equals("") ? "0"
								: adtpdRiderprem))));

		tv_bi_smart_bachat_premium_excluding_service_tax_value.setText(" Rs. "
				+ obj.getRound(obj.getStringWithout_E(Double.valueOf(basicprem
				.equals("") ? "0" : basicprem))));

		tv_bi_smart_bachat_premium_excluding_service_tax_value_2.setText(" Rs."
				+ obj.getRound(obj.getStringWithout_E(Double.valueOf(basicprem
				.equals("") ? "0" : basicprem))));

		switch (premium_paying_frequency) {
			case "Yearly":

				tv_mandatory_bi_smart_bachat_yearly_premium_with_tax1
						.setText("Yearly Premium with Applicable taxes(Rs.)");

				tv_bi_smart_bachat_premium_accidental_death_disability_name
						.setText("Yearly premium for Accidental Death and Disability Benefit(in Rs.)");

				tv_bi_smart_bachat_premium_excluding_service_tax_name
						.setText("Yearly Premium for base cover(Rs.)");

				tv_premium_install_rider_type1
						.setText("Yearly Premium excluding Applicable taxes(Rs.)");

				break;
			case "Half-Yearly":

				tv_mandatory_bi_smart_bachat_yearly_premium_with_tax1
						.setText("Half Yearly Premium with Applicable taxes(Rs.)");

				tv_premium_install_rider_type1
						.setText("Half Yearly Premium excluding Applicable taxes(Rs.)");

				tv_bi_smart_bachat_premium_accidental_death_disability_name
						.setText("Half Yearly premium for Accidental Death and Disability Benefit(in Rs.)");

				tv_bi_smart_bachat_premium_excluding_service_tax_name
						.setText("Half Yearly Premium for base cover(Rs.)");

				break;
			case "Quarterly":

				tv_mandatory_bi_smart_bachat_yearly_premium_with_tax1
						.setText("Quarterly Premium with Applicable taxes(Rs.)");

				tv_premium_install_rider_type1
						.setText("Quarterly Premium excluding Applicable taxes(Rs.)");

				tv_bi_smart_bachat_premium_accidental_death_disability_name
						.setText("Quarterly premium for Accidental Death and Disability Benefit(in Rs.)");

				tv_bi_smart_bachat_premium_excluding_service_tax_name
						.setText("Quarterly Premium for base cover(Rs.)");

				break;
			case "Monthly":

				tv_mandatory_bi_smart_bachat_yearly_premium_with_tax1
						.setText("Monthly Premium with Applicable taxes(Rs.)");

				tv_premium_install_rider_type1
						.setText("Monthly Premium excluding Applicable taxes(Rs.)");

				tv_bi_smart_bachat_premium_accidental_death_disability_name
						.setText("Monthly premium for Accidental Death and Disability Benefit(in Rs.)");

				tv_bi_smart_bachat_premium_excluding_service_tax_name
						.setText("Monthly Premium for base cover(Rs.)");
		}

		//
		String premium = prsObj.parseXmlTag(output, "basicPrem");
		String servicetax = prsObj.parseXmlTag(output, "servTax");

		basicplustax = prsObj.parseXmlTag(output, "installmntPremWithSerTx");

		tv_bi_smart_bachat_yearly_premium_with_tax
				.setText(" Rs. "
						+ obj.getRound(obj.getStringWithout_E(Double
						.valueOf(basicplustax.equals("") ? "0"
								: basicplustax))));

		// Second year onwards

		String servcTaxSecondYear = prsObj.parseXmlTag(output, "servTaxSecondYear");
		String premWthSTSecondYear = prsObj.parseXmlTag(output,
				"installmntPremWithSerTxSecondYear");

		tv_bi_smart_bachat_basic_premium_first_year.setText(""
				+ obj.getRound(obj.getStringWithout_E(Double.valueOf(modalprem
				.equals("") ? "0" : modalprem))));

		tv_bi_smart_bachat_service_tax_first_year.setText(""
				+ obj.getRound(obj.getStringWithout_E(Double.valueOf(servicetax
				.equals("") ? "0" : servicetax))));

		tv_bi_smart_bachat_yearly_premium_with_tax_first_year
				.setText(""
						+ obj.getRound(obj.getStringWithout_E(Double
						.valueOf(basicplustax.equals("") ? "0"
								: basicplustax))));

		// Amit changes start- 23-5-2016
		/* basicServiceTax = prsObj.parseXmlTag(output, "basicServiceTax"); */
		String basicServiceTax = prsObj.parseXmlTag(output, "servTax");

		tv_bi_smart_bachat_basic_service_tax_first_year.setText(""
				+ obj.getRound(obj.getStringWithout_E(Double
				.valueOf(basicServiceTax.equals("") ? "0"
						: basicServiceTax))));

		String SBCServiceTax = prsObj.parseXmlTag(output, "SBCServiceTax");

		tv_bi_smart_bachat_swachh_bharat_cess_first_year
				.setText(""
						+ obj.getRound(obj.getStringWithout_E(Double
						.valueOf(SBCServiceTax.equals("") ? "0"
								: SBCServiceTax))));

		String KKCServiceTax = prsObj.parseXmlTag(output, "KKCServiceTax");

		tv_bi_smart_bachat_krishi_kalyan_cess_first_year
				.setText(""
						+ obj.getRound(obj.getStringWithout_E(Double
						.valueOf(KKCServiceTax.equals("") ? "0"
								: KKCServiceTax))));
		// Amit changes end- 23-5-2016

		// tr_second_year.setVisibility(View.VISIBLE);
		tv_bi_smart_bachat_basic_premium_second_year.setText(""
				+ obj.getRound(obj.getStringWithout_E(Double.valueOf(modalprem
				.equals("") ? "0" : modalprem))));

		tv_bi_smart_bachat_service_tax_second_year.setText(""
				+ obj.getRound(obj.getStringWithout_E(Double
				.valueOf(servcTaxSecondYear.equals("") ? "0"
						: servcTaxSecondYear))));

		tv_bi_smart_bachat_yearly_premium_with_tax_second_year.setText(""
				+ obj.getRound(obj.getStringWithout_E(Double
				.valueOf(premWthSTSecondYear.equals("") ? "0"
						: premWthSTSecondYear))));

		// Amit changes start- 23-5-2016
		/*
		 * basicServiceTaxSecondYear = prsObj.parseXmlTag(output,
		 * "basicServiceTaxSecondYear");
		 */
		String basicServiceTaxSecondYear = prsObj.parseXmlTag(output,
				"servTaxSecondYear");

		tv_bi_smart_bachat_basic_service_tax_second_year.setText(""
				+ obj.getRound(obj.getStringWithout_E(Double
				.valueOf(basicServiceTaxSecondYear.equals("") ? "0"
						: basicServiceTaxSecondYear))));

		String SBCServiceTaxSecondYear = prsObj.parseXmlTag(output,
				"SBCServiceTaxSecondYear");

		tv_bi_smart_bachat_swachh_bharat_cess_second_year.setText(""
				+ obj.getRound(obj.getStringWithout_E(Double
				.valueOf(SBCServiceTaxSecondYear.equals("") ? "0"
						: SBCServiceTaxSecondYear))));

		String KKCServiceTaxSecondYear = prsObj.parseXmlTag(output,
				"KKCServiceTaxSecondYear");

		tv_bi_smart_bachat_krishi_kalyan_cess_second_year.setText(""
				+ obj.getRound(obj.getStringWithout_E(Double
				.valueOf(KKCServiceTaxSecondYear.equals("") ? "0"
						: KKCServiceTaxSecondYear))));

		if (plan.equalsIgnoreCase("Option B (Endowment Option with in-built Accidental Death and Total Permanent Disability (AD&TPD) Benefit)")) {
			//ll_accidental_death.setVisibility(View.VISIBLE);
			//view_accidental_death.setVisibility(View.VISIBLE);
			tr_bi_smart_bachat_for_option_b.setVisibility(View.VISIBLE);
			tv_bi_smart_bachat_premium_excluding_service_tax_name.setVisibility(View.VISIBLE);
			tv_bi_smart_bachat_premium_excluding_service_tax_value.setVisibility(View.VISIBLE);
			tv_bi_smart_bachat_premium_excluding_service_tax_value_2.setVisibility(View.VISIBLE);
			tv_bi_smart_bachat_premium_accidental_death_disability_value_2.setVisibility(View.VISIBLE);
			tv_bi_smart_bachat_premium_accidental_death_disability_name.setVisibility(View.VISIBLE);
			tv_bi_smart_bachat_premium_accidental_death_disability_value.setVisibility(View.VISIBLE);

			tv_bi_smart_bachat_AD_TPD_premium_without_tax.setText(
					obj.getRound(obj.getStringWithout_E(Double
							.valueOf(modalprem1.equals("") ? "0"
									: modalprem1))));

			tv_bi_smart_bachat_AD_TPD_premium_with_tax_first_year.setText(
					obj.getRound(obj.getStringWithout_E(Double
							.valueOf(modalprem2.equals("") ? "0"
									: modalprem2))));

			tv_bi_smart_bachat_AD_TPD_premium_with_tax_second_year.setText(
					obj.getRound(obj.getStringWithout_E(Double
							.valueOf(modalprem3.equals("") ? "0"
									: modalprem3))));
		} else {
			// ll_accidental_death.setVisibility(View.GONE);
			// view_accidental_death.setVisibility(View.GONE);
			tr_bi_smart_bachat_for_option_b.setVisibility(View.GONE);
			tv_bi_smart_bachat_premium_excluding_service_tax_name.setVisibility(View.GONE);
			tv_bi_smart_bachat_premium_excluding_service_tax_value.setVisibility(View.GONE);
			tv_bi_smart_bachat_premium_excluding_service_tax_value_2.setVisibility(View.GONE);
			tv_bi_smart_bachat_premium_accidental_death_disability_value_2.setVisibility(View.GONE);
			tv_bi_smart_bachat_premium_accidental_death_disability_name.setVisibility(View.GONE);
			tv_bi_smart_bachat_premium_accidental_death_disability_value.setVisibility(View.GONE);
			tv_bi_smart_bachat_AD_TPD_premium_without_tax.setText(
					"0");

			tv_bi_smart_bachat_AD_TPD_premium_with_tax_first_year.setText(
					"0");

			tv_bi_smart_bachat_AD_TPD_premium_with_tax_second_year.setText(
					"0");

		}
		String death_gurantee_sumAssured = prsObj.parseXmlTag(output, "deathGuar" + policy_term
				+ "");




		Smart_bachat_sum_assured_on_death = getformatedThousandString(Integer.parseInt(obj.getRound(obj
				.getStringWithout_E(Double.valueOf((death_gurantee_sumAssured
						.equals("") || death_gurantee_sumAssured == null) ? "0"
						: death_gurantee_sumAssured)))));

		tv_bi_smart_bachat_sum_assured_on_death.setText("Rs."
				+ Smart_bachat_sum_assured_on_death);

		tv_bi_smart_bachat_base_premium_without_tax.setText(
				obj.getRound(obj.getStringWithout_E(Double.valueOf(basicprem
						.equals("") ? "0" : basicprem))));


		Base_premium_with_tax_first_year = ((int) (Double
				.parseDouble(basicprem.equals("")
						|| (basicprem.equals(null)) ? "0"
						: basicprem) + Double
				.parseDouble(servicetax.equals("")
						|| (servicetax.equals(null)) ? "0"
						: servicetax)))
				+ "";

		tv_bi_smart_bachat_base_premium_with_tax_first_year.setText(obj.getRound(obj.getStringWithout_E(Double.valueOf(BasicInstallmentPremFirstYear
				.equals("") ? "0" : BasicInstallmentPremFirstYear))));


		String Base_premium_with_tax_second_year = ((int) (Double
				.parseDouble(basicprem.equals("")
						|| (basicprem.equals(null)) ? "0"
						: basicprem) + Double
				.parseDouble(KKCServiceTaxSecondYear.equals("")
						|| (KKCServiceTaxSecondYear.equals(null)) ? "0"
						: KKCServiceTaxSecondYear)))
				+ "";


		tv_bi_smart_bachat_base_premium_with_tax_second_year.setText(obj.getRound(obj.getStringWithout_E(Double.valueOf(
				BasicInstallmentPremSecondYear.equals("") ? "0" : BasicInstallmentPremSecondYear))));




		Total_installment_premium_without_tax = ((int) (Double
				.parseDouble(basicprem.equals("")
						|| (basicprem.equals(null)) ? "0"
						: basicprem) + Double
				.parseDouble(adtpdRiderprem.equals("")
						|| (adtpdRiderprem.equals(null)) ? "0"
						: adtpdRiderprem)))
				+ "";

		if (plan.equalsIgnoreCase("Option A (Endowment Option)"))
		{
			tv_bi_smart_bachat_total_installment_premium_without_tax.setText(
					obj.getRound(obj.getStringWithout_E(Double.valueOf(basicprem
							.equals("") ? "0" : basicprem))));

			tv_bi_smart_bachat_installment_premium.setText("Rs."
					+ obj.getRound(obj.getStringWithout_E(Double
					.valueOf(basicprem.equals("") ? "0"
							: basicprem))));
		}
		else
		{
			tv_bi_smart_bachat_total_installment_premium_without_tax.setText(basicPremWithoutDisc);

			tv_bi_smart_bachat_installment_premium.setText("Rs."
					+ obj.getRound(obj.getStringWithout_E(Double
					.valueOf(basicPremWithoutDisc.equals("") ? "0"
							: basicPremWithoutDisc))));
		}



		Total_installment_premium_with_tax_first_year = ((int) (Double
				.parseDouble(basicprem.equals("")
						|| (basicprem.equals(null)) ? "0"
						: basicprem) + Double
				.parseDouble(servicetax.equals("")
						|| (servicetax.equals(null)) ? "0"
						: servicetax) + Double
				.parseDouble(adtpdRiderprem.equals("")
						|| (adtpdRiderprem.equals(null)) ? "0"
						: adtpdRiderprem)))
				+ "";



		Total_installment_premium_with_tax_second_year = ((int) (Double
				.parseDouble(basicprem.equals("") || (basicprem.equals(null)) ? "0" : basicprem) + Double
				.parseDouble(KKCServiceTaxSecondYear.equals("")
						|| (KKCServiceTaxSecondYear.equals(null)) ? "0"
						: KKCServiceTaxSecondYear) + Double
				.parseDouble(adtpdRiderprem.equals("")
						|| (adtpdRiderprem.equals(null)) ? "0"
						: adtpdRiderprem)))
				+ "";




		tv_bi_smart_bachat_Rider_premium_without_tax.setText("NA");
		tv_bi_smart_bachat_Rider_premium_with_tax_first_year.setText("NA");
		tv_bi_smart_bachat_Rider_premium_with_tax_second_year.setText("NA");


		/*tv_smart_bachat_data
                .setText("Your SBI Life Smart Bachat (UIN: 111N108V03) is a Limited premium policy, for which your first year "
						+ premium_paying_frequency
						+ " premium is Rs. "
						+ obj.getRound(obj.getStringWithout_E(Double
						.valueOf(basicplustax.equals("") ? "0"
								: basicplustax)))
						+ ". Your Policy Term is "
						+ policy_term
						+ " years, Premium Payment Term is "
						+ premPayTerm
						+ " years and Basic Sum Assured is Rs. "
						+ getformatedThousandString(Integer.parseInt(obj.getRound(obj.getStringWithout_E(Double
						.valueOf((sum_assured.equals("") || sum_assured == null) ? "0"
								: sum_assured))))));
                                */

		if (proposer_Backdating_WishToBackDate_Policy.equalsIgnoreCase("y")) {
			tr_bi_smart_bachat_backdating.setVisibility(View.VISIBLE);
			view_backdating.setVisibility(View.VISIBLE);

			BackdatingInt = prsObj.parseXmlTag(output, "backdateInt");

			tv_bi_smart_bachat_backdating_interest.setText(" Rs "
					+ obj.getRound(obj.getStringWithout_E(Double
					.valueOf(BackdatingInt.equals("") ? "0"
							: BackdatingInt))));
		}


		for (int i = 1; i <= Integer.parseInt(policy_term); i++) {

			String end_of_year = prsObj
					.parseXmlTag(output, "policyYr" + i + "");
			String total_base_premium = ((int) Double.parseDouble(prsObj
					.parseXmlTag(output, "Annualized_Premium" + i + ""))) + "";


           /* String GuarateedAddition = ((int) Double.parseDouble(prsObj
                    .parseXmlTag(output, "GuarateedAddition" + i + ""))) + "";*/


            /*String guaranteed_survival_benefit = ((int) Double
                    .parseDouble(prsObj.parseXmlTag(output, "guaranMatBen" + i
                            + "")))
                    + "";*/

          /*  String guaranteed_survival_benefit = ((int) Double
                    .parseDouble(prsObj.parseXmlTag(output, "surGuar" + i
                            + "")))
                    + "";*/


			String guaranteed_surrender_value = ((int) Double
					.parseDouble(prsObj.parseXmlTag(output, "surGuar"
							+ i + "")))
					+ "";


			String guarntdDeathBenft = ((int) Double.parseDouble(prsObj
					.parseXmlTag(output, "deathGuar" + i + ""))) + "";


			String MaturityBenefit = ((int) Double
					.parseDouble(prsObj.parseXmlTag(output, "matGuar" + i
							+ "")))
					+ "";


			String ReversionaryBonus4Per = ((int) Double
					.parseDouble(prsObj.parseXmlTag(output,
							"deathNonGuar4pa" + i + "")))
					+ "";

			String not_guaranteed_surrender_value_4per = ((int) Double
					.parseDouble(prsObj.parseXmlTag(output,
							"surNonGuar4pa" + i + "")))
					+ "";

			String ReversionaryBonus8Per = ((int) Double
					.parseDouble(prsObj.parseXmlTag(output,
							"deathNonGuar8pa" + i + "")))
					+ "";

			String not_guaranteed_surrender_value_8per = ((int) Double
					.parseDouble(prsObj.parseXmlTag(output,
							"surNonGuar8pa" + i + "")))
					+ "";


			String TotalMaturityBenefit4per = ((int) Double
					.parseDouble(prsObj.parseXmlTag(output,
							"Maturity_Benefit4" + i + "")))
					+ "";

			String TotalMaturityBenefit8per = ((int) Double
					.parseDouble(prsObj.parseXmlTag(output,
							"Maturity_Benefit8" + i + "")))
					+ "";

			String TotalDeathBenefit4per = ((int) Double
					.parseDouble(prsObj.parseXmlTag(output,
							"getTotalDeathBenefit4per" + i + "")))
					+ "";

			String TotalDeathBenefit8per = ((int) Double
					.parseDouble(prsObj.parseXmlTag(output,
							"getTotalDeathBenefit8per" + i + "")))
					+ "";


           /* String cashBonus4per = prsObj.parseXmlTag(output,
                            "cashBonus" + i + "");


            String cashBonus8per = prsObj.parseXmlTag(output,
                    "cashBonus" + i + "");*/


			list_data.add(new M_BI_SmartBachatNew_Adapter(end_of_year,
					total_base_premium,
					"0",
					"0",
					guaranteed_surrender_value,
					guarntdDeathBenft,
					MaturityBenefit,
					ReversionaryBonus4Per,
					"0",
					not_guaranteed_surrender_value_4per,
					ReversionaryBonus8Per,
					"0",
					not_guaranteed_surrender_value_8per,
					TotalMaturityBenefit4per,
					TotalMaturityBenefit8per,
					TotalDeathBenefit4per,
					TotalDeathBenefit8per));
		}

		Adapter_BI_SmartBachatGridNew adapter = new Adapter_BI_SmartBachatGridNew(this, list_data);
		gv_userinfo.setAdapter(adapter);

		GridHeight gh = new GridHeight();
		gh.getheight(gv_userinfo, policy_term);


		d.show();

	}

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

					File Photo = commonMethods.galleryAddPic(BI_SmartBachatActivity.this);
					Bitmap bmp = BitmapFactory.decodeFile(Photo
							.getAbsolutePath());

					Bitmap b = null;
					Uri imageUri;
					try {

						if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
							imageUri = commonMethods.getContentUri(BI_SmartBachatActivity.this, new File(Photo.toString()));
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
						imageButtonSmartBachatProposerPhotograph
								.setImageBitmap(scaled);
					} else {
						photoBitmap = null;
					}
				}
			}
		}
		/* end */
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

	private int getIndex(Spinner s1, String value) {

		int index = 0;

		for (int i = 0; i < s1.getCount(); i++) {
			if (s1.getItemAtPosition(i).equals(value)) {
				index = i;
			}
		}
		return index;
	}

//    public void fillSpinnerValue(Spinner spinner, List<String> value_list) {
//
//        Element_TextView_BaseAdapter retd_adapter = new Element_TextView_BaseAdapter(
//                BI_SmartBachatActivity.this, value_list);
//        spinner.setAdapter(retd_adapter);
//
//    }

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

	private void CreateSmartBachat_BIPdf() {

		currencyFormat = new DecimalFormat("##,##,##,###");
		decimalcurrencyFormat = new DecimalFormat("##,##,##,###.##");

		try {
			ParseXML prsObj = new ParseXML();
			System.out.println("Final output in BIPDF" + output);
			String basicPrem = prsObj.parseXmlTag(output, "basicPrem");
			String basicServiceTax = prsObj.parseXmlTag(output,
					"basicServiceTax");
			String basicServiceTaxSecondYear = prsObj.parseXmlTag(output,
					"basicServiceTaxSecondYear");
			String SBCServiceTax = prsObj.parseXmlTag(output, "SBCServiceTax");
			String SBCServiceTaxSecondYear = prsObj.parseXmlTag(output,
					"SBCServiceTaxSecondYear");
			String KKCServiceTax = prsObj.parseXmlTag(output, "KKCServiceTax");
			String KKCServiceTaxSecondYear = prsObj.parseXmlTag(output,
					"KKCServiceTaxSecondYear");
			String servTax = prsObj.parseXmlTag(output, "servTax");
			String servTaxSeondYear = prsObj.parseXmlTag(output,
					"servTaxSecondYear");
			String installmntPremWithSerTx = prsObj.parseXmlTag(output,
					"installmntPremWithSerTx");
			String installmntPremWithSerTxSecondYear = prsObj.parseXmlTag(
					output, "installmntPremWithSerTxSecondYear");

			String installmntPrem = prsObj
					.parseXmlTag(output, "installmntPrem");
			String modalprem = prsObj.parseXmlTag(output, "modalprem");
			String adtpdRiderprem = prsObj
					.parseXmlTag(output, "adtpdRiderprem");

			// System.out.println("  "+maturityAge+
			// "  "+annPrem+" "+sumAssured);
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
			Font normal = new Font(Font.FontFamily.TIMES_ROMAN, 5, Font.NORMAL);
			Font normal_bold = new Font(Font.FontFamily.TIMES_ROMAN, 5,
					Font.BOLD);
			Font normal1_bold = new Font(Font.FontFamily.TIMES_ROMAN, 6,
					Font.BOLD);
			mypath = mStorageUtils.createFileToAppSpecificDir(mContext, QuatationNumber + "BI.pdf");
			needAnalysispath = mStorageUtils.createFileToAppSpecificDir(mContext, "NA.pdf");
			// needAnalysis_Tabular_path = new File(folder, "NA_Tabular.pdf");
			newFile = mStorageUtils.createFileToAppSpecificDir(mContext, QuatationNumber + "P01.pdf");

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
							"Benefit Illustration(BI): SBI Life - Smart Bachat (UIN: 111N108V03)" + "\n" + "An Individual, Non-Linked, Participating, Life Insurance Savings Product",
							headerBold));

			PdfPTable headertable = new PdfPTable(1);
			headertable.setWidthPercentage(100);
			PdfPCell c1 = new PdfPCell(new Phrase(Para_Header));
			c1.setPadding(5);
			c1.setHorizontalAlignment(Element.ALIGN_CENTER);
			headertable.addCell(c1);
			headertable.setHorizontalAlignment(Element.ALIGN_CENTER);
			Paragraph para_address = new Paragraph(
					"SBI Life Insurance Co. Ltd" + "\n" + "Registered & Corporate Office: 'Natraj', M.V.Road and Western Express Highway Junction, Andheri (East), Mumbai - 400069",
					small_bold_for_name);
			para_address.setAlignment(Element.ALIGN_CENTER);
			Paragraph para_address1 = new Paragraph(
					"IRDAI Registration No. 111 | Website: www.sbilife.co.in | Email: info@sbilife.co.in | CIN: L99999MH2000PLC129113" + "\n" + "Toll Free: 1800 267 9090 (Between 9.00 am & 9.00 pm)" + "\n" + "Benefit Illustration(BI): SBI Life - Smart Bachat (UIN: 111N108V03)" + "\n" + "An Individual, Non-Linked, Participating, Life Insurance Savings Product",
					small_bold);
			para_address1.setAlignment(Element.ALIGN_CENTER);
			document.add(para_address);
			document.add(para_address1);
			//document.add(Para_Header);
			document.add(para_img_logo_after_space_1);

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
			PdfPCell ProposalNumber_cell_2 = new PdfPCell(new Paragraph(""
					+ QuatationNumber, small_bold1));
			ProposalNumber_cell_2.setHorizontalAlignment(Element.ALIGN_CENTER);
			PdfPCell NameofProposal_cell_3 = new PdfPCell(new Paragraph(
					"Channel / Intermediary ", small_normal));
			PdfPCell NameofProposal_cell_4 = new PdfPCell(new Paragraph(""
					+ userType, small_bold1));
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
							"Insurance Regulatory & Development Authority of India (IRDAI) requires all life insurance companies operating in India to provide official illustrations to their customers. The illustrations are based on the investment rates of return set by the Life Insurance Council (constituted under Section 64C(a) of the Insurance Act 1938) and is not intended to reflect the actual investment returns achieved or which may be achieved in future by SBI life Insurance Company Limited.  All life insurance companies use the same rates in their benefit illustrations."
							,
							small_normal));
			BI_Pdftable2_cell1.setPadding(5);
			BI_Pdftable2.addCell(BI_Pdftable2_cell1);
			document.add(BI_Pdftable2);

			PdfPTable BI_Pdftable3 = new PdfPTable(1);
			BI_Pdftable3.setWidthPercentage(100);
			// CR_table6.setWidths(columnWidths_3);
			PdfPCell BI_Pdftable3_cell1 = new PdfPCell(
					new Paragraph(
							"The main objective of the illustration is that the client is able to appreciate the features of the product and the flow of benefits in different circumstances with some level of quantification. For further information on the product, its benefits and applicable charges please refer to the sales brochure and/or policy document.",
							small_normal));
			BI_Pdftable3_cell1.setPadding(5);

			BI_Pdftable3.addCell(BI_Pdftable3_cell1);
			document.add(BI_Pdftable3);


			document.add(para_img_logo_after_space_1);

			// inputTable here -1

			PdfPTable personalDetail_table = new PdfPTable(2);
			personalDetail_table.setWidths(new float[]{5f, 5f,});
			personalDetail_table.setWidthPercentage(100f);

			personalDetail_table.setHorizontalAlignment(Element.ALIGN_LEFT);


			// 1st row
			cell = new PdfPCell(new Phrase(
					"Proposer & Life Assured Details ", small_bold));
			cell.setColspan(2);
			cell.setPadding(5);
			cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
			cell.setBorder(Rectangle.BOTTOM | Rectangle.LEFT | Rectangle.RIGHT
					| Rectangle.TOP);
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			personalDetail_table.addCell(cell);


			cell = new PdfPCell(new Phrase("Name of the Prospect/Policyholder", small_normal));
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell.setPadding(5);
			personalDetail_table.addCell(cell);

			if (proposer_Is_Same_As_Life_Assured.equalsIgnoreCase("y")) {
				cell = new PdfPCell(new Phrase(
						name_of_life_assured, small_normal));
				cell.setHorizontalAlignment(Element.ALIGN_LEFT);
				cell.setPadding(5);
				personalDetail_table.addCell(cell);
			}
			else if (proposer_Is_Same_As_Life_Assured.equalsIgnoreCase("n")) {
				cell = new PdfPCell(new Phrase(
						name_of_proposer, small_normal));
				cell.setHorizontalAlignment(Element.ALIGN_LEFT);
				cell.setPadding(5);
				personalDetail_table.addCell(cell);
			}

			cell = new PdfPCell(new Phrase("Age(Years)", small_normal));
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell.setPadding(5);
			personalDetail_table.addCell(cell);
			if (proposer_Is_Same_As_Life_Assured.equalsIgnoreCase("y")) {

				cell = new PdfPCell(new Phrase(age_entry + "" + "",
						small_normal));
				cell.setHorizontalAlignment(Element.ALIGN_LEFT);
				cell.setPadding(5);
				personalDetail_table.addCell(cell);
			}
			else if (proposer_Is_Same_As_Life_Assured.equalsIgnoreCase("n")) {
				cell = new PdfPCell(new Phrase(ProposerAge + "" + "",
						small_normal));
				cell.setHorizontalAlignment(Element.ALIGN_LEFT);
				cell.setPadding(5);
				personalDetail_table.addCell(cell);
			}
			cell = new PdfPCell(new Phrase("Gender", small_normal));
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell.setPadding(5);
			personalDetail_table.addCell(cell);
			if (proposer_Is_Same_As_Life_Assured.equalsIgnoreCase("y")) {

				cell = new PdfPCell(new Phrase(gender + "" + "",
						small_normal));
				cell.setHorizontalAlignment(Element.ALIGN_LEFT);
				cell.setPadding(5);
				personalDetail_table.addCell(cell);
			}
			else if (proposer_Is_Same_As_Life_Assured.equalsIgnoreCase("n")) {
				cell = new PdfPCell(new Phrase(gender_proposer + "" + "",
						small_normal));
				cell.setHorizontalAlignment(Element.ALIGN_LEFT);
				cell.setPadding(5);
				personalDetail_table.addCell(cell);
			}
			cell = new PdfPCell(new Phrase("State", small_normal));
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell.setPadding(5);
			//personalDetail_table.addCell(cell);

			if (cb_kerladisc.isChecked()) {
				cell = new PdfPCell(new Phrase("Kerala" + "" + "",
						small_normal));
				cell.setHorizontalAlignment(Element.ALIGN_LEFT);
				cell.setPadding(5);
				//personalDetail_table.addCell(cell);
			} else {
				cell = new PdfPCell(new Phrase("Non Kerala" + "" + "",
						small_normal));
				cell.setHorizontalAlignment(Element.ALIGN_LEFT);
				cell.setPadding(5);
				//personalDetail_table.addCell(cell);
			}

			cell = new PdfPCell(new Phrase("Name of the Life Assured", small_normal));
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell.setPadding(5);
			personalDetail_table.addCell(cell);

			cell = new PdfPCell(new Phrase(
					name_of_life_assured, small_normal));
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell.setPadding(5);
			personalDetail_table.addCell(cell);


			cell = new PdfPCell(new Phrase("Age(Years)", small_normal));
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell.setPadding(5);
			personalDetail_table.addCell(cell);

			cell = new PdfPCell(new Phrase(smartbachatbean.getAge() + "" + " Years",
					small_normal));
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell.setPadding(5);
			personalDetail_table.addCell(cell);

			cell = new PdfPCell(new Phrase("Gender", small_normal));
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell.setPadding(5);
			personalDetail_table.addCell(cell);

			cell = new PdfPCell(new Phrase(gender + "" + "",
					small_normal));
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell.setPadding(5);
			personalDetail_table.addCell(cell);

			cell = new PdfPCell(new Phrase("Staff", small_normal));
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell.setPadding(5);
			personalDetail_table.addCell(cell);

			staffdiscount = prsObj.parseXmlTag(inputVal.toString(), "isStaff");

			if (staffdiscount.equalsIgnoreCase("true")) {
				cell = new PdfPCell(new Phrase("Yes", small_normal));
				cell.setHorizontalAlignment(Element.ALIGN_LEFT);
				cell.setPadding(5);
				personalDetail_table.addCell(cell);
			} else {
				cell = new PdfPCell(new Phrase("No", small_normal));
				cell.setHorizontalAlignment(Element.ALIGN_LEFT);
				cell.setPadding(5);
				personalDetail_table.addCell(cell);
			}





           /* cell = new PdfPCell(new Phrase("Gender", small_normal));
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell.setPadding(5);
			personalDetail_table.addCell(cell);

			cell = new PdfPCell(new Phrase("Plan", small_normal));
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell.setPadding(5);
			personalDetail_table.addCell(cell);

			cell = new PdfPCell(new Phrase(smartbachatbean.getPlantype() + "",
					small_normal));
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell.setPadding(5);
			personalDetail_table.addCell(cell);
*/
           /* cell = new PdfPCell(new Phrase("Base Sum Assured (in Rs.)",
					small_normal));
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell.setPadding(5);
			personalDetail_table.addCell(cell);

			cell = new PdfPCell(
					new Phrase(currencyFormat.format(smartbachatbean
							.getSumAssured()) + "", small_normal));
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell.setPadding(5);
            personalDetail_table.addCell(cell);*/


          /*  String adtpdbenfits_str = "";

			if (plan.equalsIgnoreCase("Option A (Endowment Option)"))
			{
				adtpdbenfits_str = "0";
			}
			else
			{
				adtpdbenfits_str = et_adtpd_benifit.getText().toString();
			}

			cell = new PdfPCell(new Phrase("Accidental Death and Disability Benefit  Sum Assured (in Rs.)",
					small_normal));
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell.setPadding(5);
			personalDetail_table.addCell(cell);

			cell = new PdfPCell(
					new Phrase(currencyFormat.format(Double.parseDouble(adtpdbenfits_str)) + "", small_normal));
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell.setPadding(5);
            personalDetail_table.addCell(cell);*/


         /*   cell = new PdfPCell(new Phrase("Staff/Non-Staff ", small_normal));
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell.setPadding(5);
			personalDetail_table.addCell(cell);

			if (smartbachatbean.getIsForStaffOrNot())
				cell = new PdfPCell(new Phrase("Staff", small_normal));
			else
				cell = new PdfPCell(new Phrase("Non-Staff", small_normal));
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell.setPadding(5);
			personalDetail_table.addCell(cell);
*/

			cell = new PdfPCell(new Phrase("", small_normal));
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell.setPadding(5);
			personalDetail_table.addCell(cell);

			cell = new PdfPCell(new Phrase("", small_normal));
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell.setPadding(5);
			personalDetail_table.addCell(cell);

			document.add(personalDetail_table);
			document.add(para_img_logo_after_space_1);

			PdfPTable BI_Pdftable_BI_details = new PdfPTable(1);
			BI_Pdftable_BI_details.setWidthPercentage(100);
			PdfPCell BI_Pdftable_BI_details_cell1 = new PdfPCell(new Paragraph(
					"How to read and understand this benefit illustration?", small_bold));
			BI_Pdftable_BI_details_cell1
					.setHorizontalAlignment(Element.ALIGN_LEFT);
			BI_Pdftable_BI_details_cell1.setBackgroundColor(BaseColor.LIGHT_GRAY);
			BI_Pdftable_BI_details_cell1.setPadding(5);

			BI_Pdftable_BI_details.addCell(BI_Pdftable_BI_details_cell1);
			document.add(BI_Pdftable_BI_details);

			PdfPTable BI_Pdftable_BI_details1 = new PdfPTable(1);
			BI_Pdftable_BI_details1.setWidthPercentage(100);
			PdfPCell BI_Pdftable_BI_details_cell2 = new PdfPCell(
					new Paragraph(
							"This benefit illustration is intended to show year-wise premiums payable and benefits under the policy, at two assumed rates of interest i.e, 8% p.a and 4% p.a.",
							small_normal));
			BI_Pdftable_BI_details_cell2.setPadding(5);
			BI_Pdftable_BI_details1.addCell(BI_Pdftable_BI_details_cell2);
			document.add(BI_Pdftable_BI_details1);


			PdfPTable BI_Pdftable_BI_details2 = new PdfPTable(1);
			BI_Pdftable_BI_details2.setWidthPercentage(100);
			PdfPCell BI_Pdftable_BI_details2_cell2 = new PdfPCell(
					new Paragraph(
							"Some benefits are guaranteed and some benefits are variable with returns based on the future performance of your insurer carrying on life insurance business. If your policy offers guaranteed benefits then these will be clearly marked “guaranteed” in the illustration table on this page. If your policy offers variable benefits then the illustrations on this page will show two different rates of assumed future investment returns, of 8% p.a. and 4% p.a. These assumed rates of return are not guaranteed and they are not the upper or lower limits of what you might get back, as the value of your policy is dependent on a number of factors including future investment performance.",
							small_normal));
			BI_Pdftable_BI_details2_cell2.setPadding(5);
			BI_Pdftable_BI_details2.addCell(BI_Pdftable_BI_details2_cell2);
			document.add(BI_Pdftable_BI_details2);


			document.add(para_img_logo_after_space_1);

			PdfPTable BI_Pdftable_Policy_details = new PdfPTable(1);
			BI_Pdftable_Policy_details.setWidthPercentage(100);
			PdfPCell BI_Pdftable_Policy_details_cell1 = new PdfPCell(new Paragraph(
					"Policy Details", small_bold));
			BI_Pdftable_Policy_details_cell1
					.setHorizontalAlignment(Element.ALIGN_LEFT);
			BI_Pdftable_Policy_details_cell1.setBackgroundColor(BaseColor.LIGHT_GRAY);
			BI_Pdftable_Policy_details_cell1.setPadding(5);

			BI_Pdftable_Policy_details.addCell(BI_Pdftable_Policy_details_cell1);
			document.add(BI_Pdftable_Policy_details);

			PdfPTable table_Policy_options = new PdfPTable(2);
			// float[] columnWidths_table_Policy_options = { 2f, 2f, 1f, 2f };
			// table_Policy_options.setWidths(columnWidths_table_Policy_options);
			table_Policy_options.setWidthPercentage(100);
			String str_policy_option = "";
			if (smartbachatbean.getPlantype().contains("Option A")) {
				str_policy_option = "Option A";
			}
			if (smartbachatbean.getPlantype().contains("Option B")) {
				str_policy_option = "Option B";
			}

			PdfPCell Policy_options_cell_1 = new PdfPCell(new Paragraph(
					"Policy Option", small_normal));
			PdfPCell Policy_options_cell_2 = new PdfPCell(new Paragraph(
					str_policy_option, small_bold1));
			Policy_options_cell_2.setHorizontalAlignment(Element.ALIGN_CENTER);

			Policy_options_cell_1.setPadding(5);
			Policy_options_cell_2.setPadding(5);

			table_Policy_options.addCell(Policy_options_cell_1);
			table_Policy_options.addCell(Policy_options_cell_2);
			document.add(table_Policy_options);


			PdfPTable personalDetail_tablesecond = new PdfPTable(2);
			personalDetail_tablesecond.setWidths(new float[]{5f, 5f,});
			personalDetail_tablesecond.setWidthPercentage(100f);

			personalDetail_tablesecond.setHorizontalAlignment(Element.ALIGN_LEFT);

			cell = new PdfPCell(new Phrase("Premium Payment Option", small_normal));
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell.setPadding(5);
			personalDetail_tablesecond.addCell(cell);

			cell = new PdfPCell(new Phrase(
					"Limited"+ "", small_normal));
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell.setPadding(5);
			personalDetail_tablesecond.addCell(cell);

			cell = new PdfPCell(new Phrase("Policy Term (Years)", small_normal));
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell.setPadding(5);
			personalDetail_tablesecond.addCell(cell);

			cell = new PdfPCell(new Phrase(
					smartbachatbean.getPolicyterm() + "", small_normal));
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell.setPadding(5);
			personalDetail_tablesecond.addCell(cell);

			cell = new PdfPCell(new Phrase("Premium Payment Term (years)",
					small_normal));
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell.setPadding(5);
			personalDetail_tablesecond.addCell(cell);

			cell = new PdfPCell(new Phrase(
					smartbachatbean.getPremiumpayingterm() + "", small_normal));
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell.setPadding(5);
			personalDetail_tablesecond.addCell(cell);

			cell = new PdfPCell(new Phrase("Mode of Payment of Premium",
					small_normal));
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell.setPadding(5);
			personalDetail_tablesecond.addCell(cell);

			cell = new PdfPCell(new Phrase(
					smartbachatbean.getPremiumFrequency() + "", small_normal));
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell.setPadding(5);
			personalDetail_tablesecond.addCell(cell);


			cell = new PdfPCell(new Phrase("Amount of Installment Premium",
					small_normal));
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell.setPadding(5);
			personalDetail_tablesecond.addCell(cell);

			if (plan.equalsIgnoreCase("Option A (Endowment Option)"))
			{
				cell = new PdfPCell(new Phrase(
						" Rs."
								+ obj.getRound(obj.getStringWithout_E(Double.valueOf(basicprem
								.equals("") ? "0" : basicprem))), small_normal));
				cell.setHorizontalAlignment(Element.ALIGN_LEFT);
				cell.setPadding(5);
				personalDetail_tablesecond.addCell(cell);
			}
			else
			{
				cell = new PdfPCell(new Phrase(
						" Rs."
								+ obj.getRound(obj.getStringWithout_E(Double.valueOf(basicPremWithoutDisc
								.equals("") ? "0" : basicPremWithoutDisc))), small_normal));
				cell.setHorizontalAlignment(Element.ALIGN_LEFT);
				cell.setPadding(5);
				personalDetail_tablesecond.addCell(cell);
			}


			document.add(personalDetail_tablesecond);

			PdfPTable table_bonus_type = new PdfPTable(2);
			// float[] columnWidths_table_bonus_type = { 2f, 2f, 1f, 2f };
			// table_bonus_type.setWidths(columnWidths_table_bonus_type);
			table_bonus_type.setWidthPercentage(100);

			PdfPCell bonus_type_cell_1 = new PdfPCell(new Paragraph(
					"Bonus Type", small_normal));
			PdfPCell bonus_type_cell_2 = new PdfPCell(new Paragraph(
					"Simple Reversionary Bonus", small_bold1));
			bonus_type_cell_2.setHorizontalAlignment(Element.ALIGN_CENTER);

			bonus_type_cell_1.setPadding(5);
			bonus_type_cell_2.setPadding(5);

			table_bonus_type.addCell(bonus_type_cell_1);
			table_bonus_type.addCell(bonus_type_cell_2);
			document.add(table_bonus_type);

			PdfPTable table_Sum_assured = new PdfPTable(2);
			// float[] columnWidths_table_Sum_assured = { 2f, 2f, 1f, 2f };
			// table_Sum_assured.setWidths(columnWidths_table_Sum_assured);
			table_Sum_assured.setWidthPercentage(100);

			PdfPCell Sum_assured_cell_1 = new PdfPCell(new Paragraph(
					"Sum Assured (Rs.)", small_normal));
			PdfPCell Sum_assured_cell_2 = new PdfPCell(new Paragraph(
					currencyFormat.format(smartbachatbean
							.getSumAssured()) + "", small_bold1));
			Sum_assured_cell_2.setHorizontalAlignment(Element.ALIGN_CENTER);

			Sum_assured_cell_1.setPadding(5);
			Sum_assured_cell_2.setPadding(5);

			table_Sum_assured.addCell(Sum_assured_cell_1);
			table_Sum_assured.addCell(Sum_assured_cell_2);
			document.add(table_Sum_assured);


			PdfPTable table_Sum_assured_death = new PdfPTable(2);
			// float[] columnWidths_table_Sum_assured_death = { 2f, 2f, 1f, 2f };
			// table_Sum_assured_death.setWidths(columnWidths_table_Sum_assured_death);
			table_Sum_assured_death.setWidthPercentage(100);

			PdfPCell Sum_assured_death_cell_1 = new PdfPCell(new Paragraph(
					"Sum Assured on Death(Rs.) (at inception of the policy)", small_normal));
			PdfPCell Sum_assured_death_cell_2 = new PdfPCell(new Paragraph(
					Smart_bachat_sum_assured_on_death + "", small_bold1));
			Sum_assured_death_cell_2.setHorizontalAlignment(Element.ALIGN_CENTER);

			Sum_assured_death_cell_1.setPadding(5);
			Sum_assured_death_cell_2.setPadding(5);

			table_Sum_assured_death.addCell(Sum_assured_death_cell_1);
			table_Sum_assured_death.addCell(Sum_assured_death_cell_2);
			document.add(table_Sum_assured_death);


			PdfPTable table_Sum_assured_ad_tpd = new PdfPTable(2);
			// float[] columnWidths_table_Sum_assured_ad_tpd = { 2f, 2f, 1f, 2f };
			// table_Sum_assured_ad_tpd.setWidths(columnWidths_table_Sum_assured_ad_tpd);
			table_Sum_assured_ad_tpd.setWidthPercentage(100);

			PdfPCell Sum_assured_ad_tpd_cell_1 = new PdfPCell(new Paragraph(
					"AD&TPD* Sum Assured (Rs.)", small_normal));
			PdfPCell Sum_assured_ad_tpd_cell_2 = new PdfPCell(new Paragraph(
					AD_TPD_sum_assured + "", small_bold1));
			Sum_assured_ad_tpd_cell_2.setHorizontalAlignment(Element.ALIGN_CENTER);

			PdfPCell Sum_assured_ad_tpd_cell_11 = new PdfPCell(new Paragraph(
					"Rate of Applicable Taxes", small_normal));
			PdfPCell Sum_assured_ad_tpd_cell_22;
			if (cb_kerladisc.isChecked()) {

				Sum_assured_ad_tpd_cell_22 = new PdfPCell(new Paragraph(
						"4.75% in the 1st policy year and 2.375% from 2nd policy year onwards", small_bold1));
				Sum_assured_ad_tpd_cell_22.setHorizontalAlignment(Element.ALIGN_CENTER);


			} else {
				Sum_assured_ad_tpd_cell_22 = new PdfPCell(new Paragraph(
						"4.5% in the 1st policy year and 2.25% from 2nd policy year onwards", small_bold1));
				Sum_assured_ad_tpd_cell_22.setHorizontalAlignment(Element.ALIGN_CENTER);
			}


			Sum_assured_ad_tpd_cell_1.setPadding(5);
			Sum_assured_ad_tpd_cell_2.setPadding(5);

			table_Sum_assured_ad_tpd.addCell(Sum_assured_ad_tpd_cell_1);
			table_Sum_assured_ad_tpd.addCell(Sum_assured_ad_tpd_cell_2);
			table_Sum_assured_ad_tpd.addCell(Sum_assured_ad_tpd_cell_11);
			table_Sum_assured_ad_tpd.addCell(Sum_assured_ad_tpd_cell_22);
			document.add(table_Sum_assured_ad_tpd);

			document.add(para_img_logo_after_space_1);

			PdfPTable BI_Pdftable_Premium_summary = new PdfPTable(1);
			BI_Pdftable_Premium_summary.setWidthPercentage(100);
			PdfPCell BI_Pdftable_Premium_summary_cell1 = new PdfPCell(new Paragraph(
					"Premium Summary", small_bold));
			BI_Pdftable_Premium_summary_cell1
					.setHorizontalAlignment(Element.ALIGN_CENTER);
			BI_Pdftable_Premium_summary_cell1.setBackgroundColor(BaseColor.LIGHT_GRAY);
			BI_Pdftable_Premium_summary_cell1.setPadding(5);

			BI_Pdftable_Premium_summary.addCell(BI_Pdftable_Premium_summary_cell1);
			document.add(BI_Pdftable_Premium_summary);


			PdfPTable BI_Pdftable_Premium_summary_table = new PdfPTable(5);
			BI_Pdftable_Premium_summary_table.setWidthPercentage(100);
			PdfPCell BI_Pdftable_Premium_summary_table_cell1 = new PdfPCell(new Paragraph(
					"", small_bold));

			PdfPCell BI_Pdftable_Premium_summary_table_cell2 = new PdfPCell(new Paragraph(
					"Base Plan (Rs.)", small_bold));
			PdfPCell BI_Pdftable_Premium_summary_table_cell3 = new PdfPCell(new Paragraph(
					"AD&TPD* Benefit", small_bold));
			PdfPCell BI_Pdftable_Premium_summary_table_cell4 = new PdfPCell(new Paragraph(
					"Riders (Rs.)", small_bold));
			PdfPCell BI_Pdftable_Premium_summary_table_cell5 = new PdfPCell(new Paragraph(
					"Total Installment Premium (Rs.)", small_bold));
			BI_Pdftable_Premium_summary_table_cell1
					.setHorizontalAlignment(Element.ALIGN_CENTER);
			BI_Pdftable_Premium_summary_table_cell2
					.setHorizontalAlignment(Element.ALIGN_CENTER);
			BI_Pdftable_Premium_summary_table_cell3
					.setHorizontalAlignment(Element.ALIGN_CENTER);
			BI_Pdftable_Premium_summary_table_cell4
					.setHorizontalAlignment(Element.ALIGN_CENTER);
			BI_Pdftable_Premium_summary_table_cell5
					.setHorizontalAlignment(Element.ALIGN_CENTER);
			BI_Pdftable_Premium_summary_table_cell1.setPadding(5);
			BI_Pdftable_Premium_summary_table_cell2.setPadding(5);
			BI_Pdftable_Premium_summary_table_cell3.setPadding(5);
			BI_Pdftable_Premium_summary_table_cell4.setPadding(5);
			BI_Pdftable_Premium_summary_table_cell5.setPadding(5);

			BI_Pdftable_Premium_summary_table.addCell(BI_Pdftable_Premium_summary_table_cell1);
			BI_Pdftable_Premium_summary_table.addCell(BI_Pdftable_Premium_summary_table_cell2);
			BI_Pdftable_Premium_summary_table.addCell(BI_Pdftable_Premium_summary_table_cell3);
			BI_Pdftable_Premium_summary_table.addCell(BI_Pdftable_Premium_summary_table_cell4);
			BI_Pdftable_Premium_summary_table.addCell(BI_Pdftable_Premium_summary_table_cell5);
			document.add(BI_Pdftable_Premium_summary_table);


			PdfPTable BI_Pdftable_Premium_summary_table1 = new PdfPTable(5);
			BI_Pdftable_Premium_summary_table1.setWidthPercentage(100);
			PdfPCell BI_Pdftable_Premium_summary_table1_cell1 = new PdfPCell(new Paragraph(
					"Installment Premium without Applicable Taxes (Rs.)", small_bold));

			PdfPCell BI_Pdftable_Premium_summary_table1_cell2 = new PdfPCell(new Paragraph(
					currencyFormat.format(Double
							.parseDouble(basicprem)), small_normal));
			PdfPCell BI_Pdftable_Premium_summary_table1_cell3;

			if (plan.equalsIgnoreCase("Option A (Endowment Option)"))
			{
				BI_Pdftable_Premium_summary_table1_cell3 = new PdfPCell(new Paragraph(
						"0", small_normal));

			}else {
				BI_Pdftable_Premium_summary_table1_cell3 = new PdfPCell(new Paragraph(
						currencyFormat.format(Double
								.parseDouble(modalprem1)), small_normal));

			}
			PdfPCell BI_Pdftable_Premium_summary_table1_cell4 = new PdfPCell(new Paragraph(
					"NA", small_normal));

			String str_base_prem = "";
			if (plan.equalsIgnoreCase("Option A (Endowment Option)"))
			{
				str_base_prem = currencyFormat.format(Double
						.parseDouble(basicprem));
			}
			else
			{
				str_base_prem = currencyFormat.format(Double
						.parseDouble(basicPremWithoutDisc));
			}

			PdfPCell BI_Pdftable_Premium_summary_table1_cell5 = new PdfPCell(new Paragraph(
					str_base_prem , small_normal));
			BI_Pdftable_Premium_summary_table1_cell1
					.setHorizontalAlignment(Element.ALIGN_CENTER);
			BI_Pdftable_Premium_summary_table1_cell2
					.setHorizontalAlignment(Element.ALIGN_CENTER);
			BI_Pdftable_Premium_summary_table1_cell3
					.setHorizontalAlignment(Element.ALIGN_CENTER);
			BI_Pdftable_Premium_summary_table1_cell4
					.setHorizontalAlignment(Element.ALIGN_CENTER);
			BI_Pdftable_Premium_summary_table1_cell5
					.setHorizontalAlignment(Element.ALIGN_CENTER);
			BI_Pdftable_Premium_summary_table1_cell1.setPadding(5);
			BI_Pdftable_Premium_summary_table1_cell2.setPadding(5);
			BI_Pdftable_Premium_summary_table1_cell3.setPadding(5);
			BI_Pdftable_Premium_summary_table1_cell4.setPadding(5);
			BI_Pdftable_Premium_summary_table1_cell5.setPadding(5);

			BI_Pdftable_Premium_summary_table1.addCell(BI_Pdftable_Premium_summary_table1_cell1);
			BI_Pdftable_Premium_summary_table1.addCell(BI_Pdftable_Premium_summary_table1_cell2);
			BI_Pdftable_Premium_summary_table1.addCell(BI_Pdftable_Premium_summary_table1_cell3);
			BI_Pdftable_Premium_summary_table1.addCell(BI_Pdftable_Premium_summary_table1_cell4);
			BI_Pdftable_Premium_summary_table1.addCell(BI_Pdftable_Premium_summary_table1_cell5);
			document.add(BI_Pdftable_Premium_summary_table1);


			PdfPTable BI_Pdftable_Premium_summary_table2 = new PdfPTable(5);
			BI_Pdftable_Premium_summary_table2.setWidthPercentage(100);
			PdfPCell BI_Pdftable_Premium_summary_table2_cell1 = new PdfPCell(new Paragraph(
					"Installment Premium with Applicable Taxes: for 1st Year (Rs.)", small_bold));

			PdfPCell BI_Pdftable_Premium_summary_table2_cell2 = new PdfPCell(new Paragraph(
					currencyFormat.format(Double
							.parseDouble(obj.getRound(obj.getStringWithout_E(Double.valueOf(BasicInstallmentPremFirstYear
									.equals("") ? "0" : BasicInstallmentPremFirstYear))))), small_normal));

			PdfPCell BI_Pdftable_Premium_summary_table2_cell3;
			if (plan.equalsIgnoreCase("Option A (Endowment Option)"))
			{
				BI_Pdftable_Premium_summary_table2_cell3 = new PdfPCell(new Paragraph(
						"0", small_normal));
			}else{
				BI_Pdftable_Premium_summary_table2_cell3 = new PdfPCell(new Paragraph(
						currencyFormat.format(Double
								.parseDouble(obj.getRound(obj.getStringWithout_E(Double
										.valueOf(modalprem2.equals("") ? "0"
												: modalprem2))))), small_normal));
			}


			PdfPCell BI_Pdftable_Premium_summary_table2_cell4 = new PdfPCell(new Paragraph(
					"NA", small_normal));
			String str_total_prem_1st_year = "";
			if (plan.equalsIgnoreCase("Option A (Endowment Option)"))
			{
				str_total_prem_1st_year = currencyFormat.format(Double
						.parseDouble((int) (Double.parseDouble
								(BasicInstallmentPremFirstYear.equals("") || (BasicInstallmentPremFirstYear.equals(null)) ? "0" : BasicInstallmentPremFirstYear)) + ""));
			}
			else
			{
				str_total_prem_1st_year = currencyFormat.format(Double
						.parseDouble((int) (Double.parseDouble(IntsallmentPremATPDB_FirstYear.equals("") || (IntsallmentPremATPDB_FirstYear.equals(null)) ? "0" : IntsallmentPremATPDB_FirstYear)) + ""));

			}
			PdfPCell BI_Pdftable_Premium_summary_table2_cell5 = new PdfPCell(new Paragraph(
					str_total_prem_1st_year, small_normal));
			BI_Pdftable_Premium_summary_table2_cell1
					.setHorizontalAlignment(Element.ALIGN_CENTER);
			BI_Pdftable_Premium_summary_table2_cell2
					.setHorizontalAlignment(Element.ALIGN_CENTER);
			BI_Pdftable_Premium_summary_table2_cell3
					.setHorizontalAlignment(Element.ALIGN_CENTER);
			BI_Pdftable_Premium_summary_table2_cell4
					.setHorizontalAlignment(Element.ALIGN_CENTER);
			BI_Pdftable_Premium_summary_table2_cell5
					.setHorizontalAlignment(Element.ALIGN_CENTER);
			BI_Pdftable_Premium_summary_table2_cell1.setPadding(5);
			BI_Pdftable_Premium_summary_table2_cell2.setPadding(5);
			BI_Pdftable_Premium_summary_table2_cell3.setPadding(5);
			BI_Pdftable_Premium_summary_table2_cell4.setPadding(5);
			BI_Pdftable_Premium_summary_table2_cell5.setPadding(5);

			BI_Pdftable_Premium_summary_table2.addCell(BI_Pdftable_Premium_summary_table2_cell1);
			BI_Pdftable_Premium_summary_table2.addCell(BI_Pdftable_Premium_summary_table2_cell2);
			BI_Pdftable_Premium_summary_table2.addCell(BI_Pdftable_Premium_summary_table2_cell3);
			BI_Pdftable_Premium_summary_table2.addCell(BI_Pdftable_Premium_summary_table2_cell4);
			BI_Pdftable_Premium_summary_table2.addCell(BI_Pdftable_Premium_summary_table2_cell5);
			document.add(BI_Pdftable_Premium_summary_table2);


			PdfPTable BI_Pdftable_Premium_summary_table3 = new PdfPTable(5);
			BI_Pdftable_Premium_summary_table3.setWidthPercentage(100);
			PdfPCell BI_Pdftable_Premium_summary_table3_cell1 = new PdfPCell(new Paragraph(
					"Installment Premium with Applicable Taxes: for 2nd Year Onwards (Rs.)", small_bold));

			PdfPCell BI_Pdftable_Premium_summary_table3_cell2 = new PdfPCell(new Paragraph(
					currencyFormat.format(Double
							.parseDouble(obj.getRound(obj.getStringWithout_E(Double.valueOf(BasicInstallmentPremSecondYear
									.equals("") ? "0" : BasicInstallmentPremSecondYear))))), small_normal));
			PdfPCell BI_Pdftable_Premium_summary_table3_cell3;

			if (plan.equalsIgnoreCase("Option A (Endowment Option)"))
			{
				BI_Pdftable_Premium_summary_table3_cell3 = new PdfPCell(new Paragraph(
						"0", small_normal));
			}else {
				BI_Pdftable_Premium_summary_table3_cell3 = new PdfPCell(new Paragraph(
						currencyFormat.format(Double
								.parseDouble(obj.getRound(obj.getStringWithout_E(Double
										.valueOf(modalprem3.equals("") ? "0"
												: modalprem3))))), small_normal));
			}



			PdfPCell BI_Pdftable_Premium_summary_table3_cell4 = new PdfPCell(new Paragraph(
					"NA", small_normal));

			String str_total_prem_2nd_year = "";
			if (plan.equalsIgnoreCase("Option A (Endowment Option)"))
			{
				str_total_prem_2nd_year = currencyFormat.format(Double
						.parseDouble((int) (Double.parseDouble
								(BasicInstallmentPremSecondYear.equals("") || (BasicInstallmentPremSecondYear.equals(null)) ? "0" : BasicInstallmentPremSecondYear)) + ""));
			}
			else
			{
				str_total_prem_2nd_year = currencyFormat.format(Double
						.parseDouble((int) (Double.parseDouble(IntsallmentPremATPDB_SecondYear.equals("") || (IntsallmentPremATPDB_SecondYear.equals(null)) ? "0" : IntsallmentPremATPDB_SecondYear)) + ""));

			}

			PdfPCell BI_Pdftable_Premium_summary_table3_cell5 = new PdfPCell(new Paragraph(
					str_total_prem_2nd_year, small_normal));
			BI_Pdftable_Premium_summary_table3_cell1
					.setHorizontalAlignment(Element.ALIGN_CENTER);
			BI_Pdftable_Premium_summary_table3_cell2
					.setHorizontalAlignment(Element.ALIGN_CENTER);
			BI_Pdftable_Premium_summary_table3_cell3
					.setHorizontalAlignment(Element.ALIGN_CENTER);
			BI_Pdftable_Premium_summary_table3_cell4
					.setHorizontalAlignment(Element.ALIGN_CENTER);
			BI_Pdftable_Premium_summary_table3_cell5
					.setHorizontalAlignment(Element.ALIGN_CENTER);
			BI_Pdftable_Premium_summary_table3_cell1.setPadding(5);
			BI_Pdftable_Premium_summary_table3_cell2.setPadding(5);
			BI_Pdftable_Premium_summary_table3_cell3.setPadding(5);
			BI_Pdftable_Premium_summary_table3_cell4.setPadding(5);
			BI_Pdftable_Premium_summary_table3_cell5.setPadding(5);

			BI_Pdftable_Premium_summary_table3.addCell(BI_Pdftable_Premium_summary_table3_cell1);
			BI_Pdftable_Premium_summary_table3.addCell(BI_Pdftable_Premium_summary_table3_cell2);
			BI_Pdftable_Premium_summary_table3.addCell(BI_Pdftable_Premium_summary_table3_cell3);
			BI_Pdftable_Premium_summary_table3.addCell(BI_Pdftable_Premium_summary_table3_cell4);
			BI_Pdftable_Premium_summary_table3.addCell(BI_Pdftable_Premium_summary_table3_cell5);
			document.add(BI_Pdftable_Premium_summary_table3);

			Paragraph para_adtp_note = new Paragraph(
					"*Accidental Death & Total Permanent Disability",
					small_bold);

			document.add(para_adtp_note);

			document.add(para_img_logo_after_space_1);
			PdfPTable BI_Pdftable_Notes = new PdfPTable(1);
			BI_Pdftable_Notes.setWidthPercentage(100);
			PdfPCell BI_Pdftable_Notes_cell1 = new PdfPCell(new Paragraph(
					"Please Note:", small_bold));
			BI_Pdftable_Notes_cell1
					.setHorizontalAlignment(Element.ALIGN_LEFT);
			BI_Pdftable_Notes_cell1.setBackgroundColor(BaseColor.LIGHT_GRAY);
			BI_Pdftable_Notes_cell1.setPadding(5);

			BI_Pdftable_Notes.addCell(BI_Pdftable_Notes_cell1);
			document.add(BI_Pdftable_Notes);

			PdfPTable BI_Pdftable_Notes1 = new PdfPTable(1);
			BI_Pdftable_Notes1.setWidthPercentage(100);
			PdfPCell BI_Pdftable_Notes_cell2 = new PdfPCell(
					new Paragraph(
							"1. The premiums can be paid by giving standing instruction to your bank or you can pay through your credit card.",
							small_normal));
			BI_Pdftable_Notes_cell2.setPadding(5);
			BI_Pdftable_Notes1.addCell(BI_Pdftable_Notes_cell2);
			document.add(BI_Pdftable_Notes1);

			PdfPTable BI_Pdftable_Notes2 = new PdfPTable(1);
			BI_Pdftable_Notes2.setWidthPercentage(100);
			PdfPCell BI_Pdftable_Notes2_cell2 = new PdfPCell(
					new Paragraph(
							"2. Applicable Taxes (including surcharge/cess etc), at the rate notified by the Central Government/ State Government / Union Territories of India from time to time and as per the provisions of the prevalent tax laws will be payable on premium as per the product features.",
							small_normal));
			BI_Pdftable_Notes2_cell2.setPadding(5);
			BI_Pdftable_Notes2.addCell(BI_Pdftable_Notes2_cell2);
			document.add(BI_Pdftable_Notes2);

			PdfPTable BI_Pdftable_Notes3 = new PdfPTable(1);
			BI_Pdftable_Notes3.setWidthPercentage(100);
			PdfPCell BI_Pdftable_Notes3_cell2 = new PdfPCell(
					new Paragraph(
							"3. Tax deduction under Section 80 C is available. However in case the premium paid during the financial year, exceeds 10% of the sum assured, the benefit will be limited up to 10% of the sum assured. Tax exemption under Section 10(10D) is available at the time of maturity/surrender, subject to the premium not exceeding 10% of the sum assured in any of the years during the term of the policy. However, death proceeds are completely exempt."
							,
							small_normal));
			BI_Pdftable_Notes3_cell2.setPadding(5);
			BI_Pdftable_Notes3.addCell(BI_Pdftable_Notes3_cell2);
			// document.add(BI_Pdftable_Notes3);

			PdfPTable BI_Pdftable_Notes4 = new PdfPTable(1);
			BI_Pdftable_Notes4.setWidthPercentage(100);
			PdfPCell BI_Pdftable_Notes4_cell2 = new PdfPCell(
					new Paragraph(
							"3. In case of Option B - If accidental total and Permanent Disablity claim is paid, then future premiums would be waived off for the rest of premium payment term."
							,
							small_normal));
			BI_Pdftable_Notes4_cell2.setPadding(5);
			BI_Pdftable_Notes4.addCell(BI_Pdftable_Notes4_cell2);
			document.add(BI_Pdftable_Notes4);
			document.add(para_img_logo_after_space_1);


//            if (smartbachatbean.getPlantype().equals(
//                    "Option A (Endowment Option)")) {
                /*PdfPTable Table_life_assured_name = new PdfPTable(2);
				Table_life_assured_name.setWidthPercentage(100);

				PdfPCell cell_life_assured1 = new PdfPCell(new Paragraph(
                        "Name", small_normal));
				cell_life_assured1.setPadding(5);
				// cell_life_assured1.setHorizontalAlignment(Element.ALIGN_CENTER);

				PdfPCell cell_life_assured2 = new PdfPCell(new Paragraph(
						name_of_life_assured, small_bold));
				cell_life_assured2.setPadding(5);
				// cell_life_assured2.setHorizontalAlignment(Element.ALIGN_CENTER);
				Table_life_assured_name.addCell(cell_life_assured1);
				Table_life_assured_name.addCell(cell_life_assured2);
				document.add(Table_life_assured_name);
                document.add(para_img_logo_after_space_1);*/
			//}

			// Basic Cover


           /* if (smartbachatbean.getIsJammuResident() == true) {
				PdfPTable Table_jk = new PdfPTable(2);
				Table_jk.setWidthPercentage(100);

				PdfPCell cell_jk1 = new PdfPCell(new Paragraph(
						"IsJ&K Resident", small_normal));
				cell_jk1.setPadding(5);
				// cell_jk1.setHorizontalAlignment(Element.ALIGN_CENTER);

				PdfPCell cell_jk2 = new PdfPCell(new Paragraph("Yes",
						small_normal));
				cell_jk2.setPadding(5);
				// cell_jk2.setHorizontalAlignment(Element.ALIGN_CENTER);
				Table_jk.addCell(cell_jk1);
				Table_jk.addCell(cell_jk2);
				document.add(Table_jk);

			}
*/
       /*     if (proposer_Backdating_WishToBackDate_Policy.equalsIgnoreCase("y")) {
				PdfPTable Table_backdating_premium_with_service_tax = new PdfPTable(
						2);
				Table_backdating_premium_with_service_tax
						.setWidthPercentage(100);

				PdfPCell cell_Backdate1 = new PdfPCell(new Paragraph(
						"Backdating Interest", small_normal));
				cell_Backdate1.setPadding(5);
				// cell_Backdate1.setHorizontalAlignment(Element.ALIGN_CENTER);

				PdfPCell cell_Backdate2 = new PdfPCell(new Paragraph("Rs. "
						+ obj.getRound(obj.getStringWithout_E(Double
						.valueOf(BackdatingInt.equals("") ? "0"
								: BackdatingInt))), small_normal));

				cell_Backdate2.setPadding(5);
				// cell_Backdate2.setHorizontalAlignment(Element.ALIGN_CENTER);
				Table_backdating_premium_with_service_tax
						.addCell(cell_Backdate1);
				Table_backdating_premium_with_service_tax
						.addCell(cell_Backdate2);
				document.add(Table_backdating_premium_with_service_tax);

            }*/

          /*  document.add(para_img_logo_after_space_1);
			PdfPTable premiumDetail_table = new PdfPTable(4);
			premiumDetail_table.setWidths(new float[] { 5f, 4f, 5f, 4f });
			premiumDetail_table.setWidthPercentage(100f);
			premiumDetail_table.setHorizontalAlignment(Element.ALIGN_LEFT);

            document.add(premiumDetail_table);*/

        /*    document.add(para_img_logo_after_space_1);

			if (smartbachatbean.getPlantype().equals(
					"Option A (Endowment Option)")) {

				PdfPTable basicCover_table = new PdfPTable(3);
				basicCover_table.setWidths(new float[] { 10f, 6f, 6f });
				basicCover_table.setWidthPercentage(100f);
				basicCover_table.setHorizontalAlignment(Element.ALIGN_LEFT);

				// 1st row
				cell = new PdfPCell(new Phrase("Premium for "
						+ smartbachatbean.getPlantype(), small_bold));
				cell.setColspan(3);
				cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
				cell.setBorder(Rectangle.BOTTOM | Rectangle.TOP
						| Rectangle.LEFT | Rectangle.RIGHT);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell.setPadding(5);
				basicCover_table.addCell(cell);

				cell = new PdfPCell(new Phrase(
						"Policy Years", small_bold));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell.setPadding(5);
				basicCover_table.addCell(cell);

				cell = new PdfPCell(new Phrase("First Year", small_bold));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell.setPadding(5);
				basicCover_table.addCell(cell);

				cell = new PdfPCell(new Phrase("Second Year Onwards",
						small_bold));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell.setPadding(5);
				basicCover_table.addCell(cell);

				cell = new PdfPCell(new Phrase(""
						+ smartbachatbean.getPremiumFrequency() + " Premium (Rs.)",
						small_normal));
				cell.setHorizontalAlignment(Element.ALIGN_LEFT);
				cell.setPadding(5);
				basicCover_table.addCell(cell);

				// 2 row
				cell = new PdfPCell(new Phrase(currencyFormat.format(Double
						.parseDouble(modalprem)) + "", small_normal));
				cell.setHorizontalAlignment(Element.ALIGN_LEFT);
				cell.setPadding(5);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				basicCover_table.addCell(cell);

				cell = new PdfPCell(new Phrase(currencyFormat.format(Double
						.parseDouble(modalprem)) + "", small_normal));
				cell.setHorizontalAlignment(Element.ALIGN_LEFT);
				cell.setPadding(5);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				basicCover_table.addCell(cell);

				// cell = new PdfPCell(new Phrase("(a) Basic GST",
				// small_normal));
				// cell.setHorizontalAlignment(Element.ALIGN_LEFT);
				// cell.setPadding(5);
				// basicCover_table.addCell(cell);
				//
				// // 2 row
				// cell = new PdfPCell(new Phrase(currencyFormat.format(Double
				// .parseDouble(basicServiceTax)) + "", small_normal));
				// cell.setHorizontalAlignment(Element.ALIGN_LEFT);
				// cell.setPadding(5);
				// cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				// basicCover_table.addCell(cell);
				//
				// cell = new PdfPCell(new Phrase(currencyFormat.format(Double
				// .parseDouble(basicServiceTaxSecondYear)) + "",
				// small_normal));
				// cell.setHorizontalAlignment(Element.ALIGN_LEFT);
				// cell.setPadding(5);
				// cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				// basicCover_table.addCell(cell);

				// cell = new PdfPCell(new Phrase(
				// "(b) Swachh Bharat Cess (SBC) (Rs.)", small_normal));
				// cell.setHorizontalAlignment(Element.ALIGN_LEFT);
				// cell.setPadding(5);
				// basicCover_table.addCell(cell);
				//
				// // 2 row
				// cell = new PdfPCell(new Phrase(currencyFormat.format(Double
				// .parseDouble(SBCServiceTax)) + "", small_normal));
				// cell.setHorizontalAlignment(Element.ALIGN_LEFT);
				// cell.setPadding(5);
				// cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				// basicCover_table.addCell(cell);
				//
				// cell = new PdfPCell(new Phrase(currencyFormat.format(Double
				// .parseDouble(SBCServiceTaxSecondYear)) + "",
				// small_normal));
				// cell.setHorizontalAlignment(Element.ALIGN_LEFT);
				// cell.setPadding(5);
				// cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				// basicCover_table.addCell(cell);
				//
				// cell = new PdfPCell(new Phrase(
				// "(c) Krishi Kalyan Cess (KKC) (Rs.)", small_normal));
				// cell.setHorizontalAlignment(Element.ALIGN_LEFT);
				// cell.setPadding(5);
				// basicCover_table.addCell(cell);
				//
				// // 2 row
				//
				// cell = new PdfPCell(new Phrase(currencyFormat.format(Double
				// .parseDouble(KKCServiceTax)) + "", small_normal));
				//
				// // else
				// // cell = new PdfPCell(new Phrase(0+"",small_normal));
				// cell.setHorizontalAlignment(Element.ALIGN_LEFT);
				// cell.setPadding(5);
				// cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				// basicCover_table.addCell(cell);
				//
				// // if(cessFirstYear!=null)
				// cell = new PdfPCell(new Phrase(currencyFormat.format(Double
				// .parseDouble(KKCServiceTaxSecondYear)) + "",
				// small_normal));
				// // else
				// // cell = new PdfPCell(new Phrase(0+"",small_normal));
				// cell.setHorizontalAlignment(Element.ALIGN_LEFT);
				// cell.setPadding(5);
				// cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				// basicCover_table.addCell(cell);

				cell = new PdfPCell(new Phrase("Applicable taxes (Rs.)", small_normal));
				cell.setHorizontalAlignment(Element.ALIGN_LEFT);
				cell.setPadding(5);
				basicCover_table.addCell(cell);

				cell = new PdfPCell(new Phrase(currencyFormat.format(Double
						.parseDouble(servTax)) + "", small_normal));
				cell.setHorizontalAlignment(Element.ALIGN_LEFT);
				cell.setPadding(5);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				basicCover_table.addCell(cell);

				cell = new PdfPCell(new Phrase(currencyFormat.format(Double
						.parseDouble(servTaxSeondYear)) + "", small_normal));
				cell.setHorizontalAlignment(Element.ALIGN_LEFT);
				cell.setPadding(5);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				basicCover_table.addCell(cell);

				cell = new PdfPCell(new Phrase(""
						+ smartbachatbean.getPremiumFrequency()
						+ " premium with Applicable taxes (Rs.)", small_normal));
				cell.setHorizontalAlignment(Element.ALIGN_LEFT);
				cell.setPadding(5);
				basicCover_table.addCell(cell);

				// 2 row
				cell = new PdfPCell(new Phrase(currencyFormat.format(Double
						.parseDouble(installmntPremWithSerTx)) + "",
						small_normal));
				cell.setHorizontalAlignment(Element.ALIGN_LEFT);
				cell.setPadding(5);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				basicCover_table.addCell(cell);

				cell = new PdfPCell(new Phrase(currencyFormat.format(Double
						.parseDouble(installmntPremWithSerTxSecondYear)) + "",
						small_normal));
				cell.setHorizontalAlignment(Element.ALIGN_LEFT);
				cell.setPadding(5);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				basicCover_table.addCell(cell);

				document.add(basicCover_table);

				document.add(para_img_logo_after_space_1);

			} else {

				PdfPTable basicCover_table = new PdfPTable(3);
				basicCover_table.setWidths(new float[] { 10f, 6f, 6f });
				basicCover_table.setWidthPercentage(100f);
				basicCover_table.setHorizontalAlignment(Element.ALIGN_LEFT);

				// 1st row
				cell = new PdfPCell(new Phrase("Premium for "
						+ smartbachatbean.getPlantype(), small_bold));
				cell.setColspan(3);
				cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
				cell.setBorder(Rectangle.BOTTOM | Rectangle.TOP
						| Rectangle.LEFT | Rectangle.RIGHT);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell.setPadding(5);
				basicCover_table.addCell(cell);

				cell = new PdfPCell(new Phrase(
						"Policy Years", small_bold));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell.setPadding(5);
				basicCover_table.addCell(cell);

				cell = new PdfPCell(new Phrase("First Year", small_bold));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell.setPadding(5);
				basicCover_table.addCell(cell);

				cell = new PdfPCell(new Phrase("Second Year Onwards",
						small_bold));
				cell.setHorizontalAlignment(Element.ALIGN_LEFT);
				cell.setPadding(5);
				basicCover_table.addCell(cell);

				cell = new PdfPCell(new Phrase(""
						+ smartbachatbean.getPremiumFrequency()
						+ " premium for base cover(in Rs.)", small_normal));
				cell.setHorizontalAlignment(Element.ALIGN_LEFT);
				cell.setPadding(5);
				basicCover_table.addCell(cell);

				// 2 row
				cell = new PdfPCell(new Phrase(currencyFormat.format(Double
						.parseDouble(basicPrem)) + "", small_normal));
				cell.setHorizontalAlignment(Element.ALIGN_LEFT);
				cell.setPadding(5);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				basicCover_table.addCell(cell);

				cell = new PdfPCell(new Phrase(currencyFormat.format(Double
						.parseDouble(basicPrem)) + "", small_normal));
				cell.setHorizontalAlignment(Element.ALIGN_LEFT);
				cell.setPadding(5);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				basicCover_table.addCell(cell);

				cell = new PdfPCell(
						new Phrase(
								""
										+ smartbachatbean.getPremiumFrequency()
										+ " premium for Accidental Death and Disability benifit(in Rs.)",
								small_normal));
				cell.setHorizontalAlignment(Element.ALIGN_LEFT);
				cell.setPadding(5);
				basicCover_table.addCell(cell);

				// 2 row
				cell = new PdfPCell(new Phrase(currencyFormat.format(Double
						.parseDouble(adtpdRiderprem)) + "", small_normal));
				cell.setHorizontalAlignment(Element.ALIGN_LEFT);
				cell.setPadding(5);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				basicCover_table.addCell(cell);

				cell = new PdfPCell(new Phrase(currencyFormat.format(Double
						.parseDouble(adtpdRiderprem)) + "", small_normal));
				cell.setHorizontalAlignment(Element.ALIGN_LEFT);
				cell.setPadding(5);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				basicCover_table.addCell(cell);

				cell = new PdfPCell(new Phrase("Total "
						+ smartbachatbean.getPremiumFrequency()
						+ " premium excluding Applicable taxes (in Rs.)", small_normal));
				cell.setHorizontalAlignment(Element.ALIGN_LEFT);
				cell.setPadding(5);
				basicCover_table.addCell(cell);

				// 2 row
				cell = new PdfPCell(new Phrase(currencyFormat.format(Double
						.parseDouble(modalprem)) + "", small_normal));
				cell.setHorizontalAlignment(Element.ALIGN_LEFT);
				cell.setPadding(5);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				basicCover_table.addCell(cell);

				cell = new PdfPCell(new Phrase(currencyFormat.format(Double
						.parseDouble(modalprem)) + "", small_normal));
				cell.setHorizontalAlignment(Element.ALIGN_LEFT);
				cell.setPadding(5);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				basicCover_table.addCell(cell);

                // cell = new PdfPCell(new Phrase("(a) Basic GST",
                // small_normal));
                // cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                // cell.setPadding(5);
                // basicCover_table.addCell(cell);
                //
                // // 2 row
                // cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                // .parseDouble(basicServiceTax)) + "", small_normal));
                // cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                // cell.setPadding(5);
                // cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                // basicCover_table.addCell(cell);
                //
                // cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                // .parseDouble(basicServiceTaxSecondYear)) + "",
                // small_normal));
                // cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                // cell.setPadding(5);
                // cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                // basicCover_table.addCell(cell);
                //
                // cell = new PdfPCell(new Phrase(
                // "(b) Swachh Bharat Cess (SBC) (Rs.)", small_normal));
                // cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                // cell.setPadding(5);
                // basicCover_table.addCell(cell);
                //
                // // 2 row
                // cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                // .parseDouble(SBCServiceTax)) + "", small_normal));
                // cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                // cell.setPadding(5);
                // cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                // basicCover_table.addCell(cell);
                //
                // cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                // .parseDouble(SBCServiceTaxSecondYear)) + "",
                // small_normal));
                // cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                // cell.setPadding(5);
                // cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                // basicCover_table.addCell(cell);
                //
                // cell = new PdfPCell(new Phrase(
                // "(c) Krishi Kalyan Cess (KKC) (Rs.)", small_normal));
                // cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                // cell.setPadding(5);
                // basicCover_table.addCell(cell);
                //
                // // 2 row
                //
                // cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                // .parseDouble(KKCServiceTax)) + "", small_normal));
                //
                // // else
                // // cell = new PdfPCell(new Phrase(0+"",small_normal));
                // cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                // cell.setPadding(5);
                // cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                // basicCover_table.addCell(cell);
                //
                // // if(cessFirstYear!=null)
                // cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                // .parseDouble(KKCServiceTaxSecondYear)) + "",
                // small_normal));
                // // else
                // // cell = new PdfPCell(new Phrase(0+"",small_normal));
                // cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                // cell.setPadding(5);
                // cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                // basicCover_table.addCell(cell);

				cell = new PdfPCell(new Phrase("Applicable taxes (Rs.)", small_normal));
				cell.setHorizontalAlignment(Element.ALIGN_LEFT);
				cell.setPadding(5);
				basicCover_table.addCell(cell);

				cell = new PdfPCell(new Phrase(currencyFormat.format(Double
						.parseDouble(servTax)) + "", small_normal));
				cell.setHorizontalAlignment(Element.ALIGN_LEFT);
				cell.setPadding(5);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				basicCover_table.addCell(cell);

				cell = new PdfPCell(new Phrase(currencyFormat.format(Double
						.parseDouble(servTaxSeondYear)) + "", small_normal));
				cell.setHorizontalAlignment(Element.ALIGN_LEFT);
				cell.setPadding(5);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				basicCover_table.addCell(cell);

				cell = new PdfPCell(new Phrase(""
						+ smartbachatbean.getPremiumFrequency()
						+ " premium with Applicable taxes (in Rs.)", small_normal));
				cell.setHorizontalAlignment(Element.ALIGN_LEFT);
				cell.setPadding(5);
				basicCover_table.addCell(cell);

				// 2 row
				cell = new PdfPCell(new Phrase(currencyFormat.format(Double
						.parseDouble(installmntPremWithSerTx)) + "",
						small_normal));
				cell.setHorizontalAlignment(Element.ALIGN_LEFT);
				cell.setPadding(5);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				basicCover_table.addCell(cell);

				cell = new PdfPCell(new Phrase(currencyFormat.format(Double
						.parseDouble(installmntPremWithSerTxSecondYear)) + "",
						small_normal));
				cell.setHorizontalAlignment(Element.ALIGN_LEFT);
				cell.setPadding(5);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				basicCover_table.addCell(cell);

				document.add(basicCover_table);

				document.add(para_img_logo_after_space_1);

            }*/


			document.add(para_img_logo_after_space_1);


			PdfPTable table1 = new PdfPTable(17);
			table1.setWidths(new float[]{2f, 5f, 5f, 5f, 5f, 5f, 5f, 5f, 5f,
					5f, 5f, 5f, 5f, 5f, 5f, 5f, 5f});
			table1.setWidthPercentage(100);

			// 1st row
			cell = new PdfPCell(
					new Phrase(
							"BENEFIT ILLUSTRATION FOR SBI LIFE - Smart Bachat (Amount in Rs.)",
							normal1_bold));
			cell.setColspan(17);
			cell.setBorder(Rectangle.BOTTOM);
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			table1.addCell(cell);

			// 2nd row
			cell = new PdfPCell(new Phrase("Policy Year", normal_bold));
			cell.setRowspan(3);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table1.addCell(cell);

			cell = new PdfPCell(new Phrase(
					"Annualized premium", normal_bold));
			cell.setRowspan(3);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table1.addCell(cell);

			cell = new PdfPCell(new Phrase("Guaranteed Benefits",
					normal_bold));
			cell.setColspan(5);
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
			cell.setColspan(4);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table1.addCell(cell);

			cell = new PdfPCell(new Phrase(
					"Guaranteed additions",
					normal_bold));
			cell.setRowspan(2);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table1.addCell(cell);

			cell = new PdfPCell(new Phrase("Survival benefit", normal_bold));
			cell.setRowspan(2);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table1.addCell(cell);

			cell = new PdfPCell(new Phrase(
					"Surrender Benefit",
					normal_bold));
			cell.setRowspan(2);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table1.addCell(cell);

			cell = new PdfPCell(new Phrase("Death Benefit", normal_bold));
			cell.setRowspan(2);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table1.addCell(cell);

			cell = new PdfPCell(new Phrase("Maturity Benefit", normal_bold));
			cell.setRowspan(2);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table1.addCell(cell);

			// 4 row
			cell = new PdfPCell(new Phrase("Revisionary bonus", normal_bold));
			cell.setRowspan(2);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table1.addCell(cell);

			cell = new PdfPCell(new Phrase("Cash bonus", normal_bold));
			cell.setRowspan(2);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table1.addCell(cell);

			cell = new PdfPCell(new Phrase("Surrender benefit", normal_bold));
			cell.setRowspan(2);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table1.addCell(cell);

			cell = new PdfPCell(new Phrase("Revisionary bonus", normal_bold));
			cell.setRowspan(2);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table1.addCell(cell);

			cell = new PdfPCell(new Phrase("Cash bonus", normal_bold));
			cell.setRowspan(2);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table1.addCell(cell);

			cell = new PdfPCell(new Phrase("Surrender benefit", normal_bold));
			cell.setRowspan(2);
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

			cell = new PdfPCell(new Phrase("Total Maturity benefit, incl. Terminal bonus, if any, @ 4% (7+8+9)", normal_bold));
			cell.setRowspan(1);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table1.addCell(cell);
			cell = new PdfPCell(new Phrase("Total Maturity benefit, incl. Terminal bonus, if any, @ 8% (7+11+12)", normal_bold));
			cell.setRowspan(1);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table1.addCell(cell);
			cell = new PdfPCell(new Phrase("Total Death benefit, incl. Terminal bonus, if any, @ 4% (6+8+9)", normal_bold));
			cell.setRowspan(1);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table1.addCell(cell);

			cell = new PdfPCell(new Phrase("Total Death benefit, incl. Terminal bonus, if any, @ 8% (6+11+12)", normal_bold));
			cell.setRowspan(1);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table1.addCell(cell);

			cell = new PdfPCell(new Phrase("1", normal_bold));
			cell.setRowspan(1);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table1.addCell(cell);

			cell = new PdfPCell(new Phrase("2", normal_bold));
			cell.setRowspan(1);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table1.addCell(cell);

			cell = new PdfPCell(new Phrase("3", normal_bold));
			cell.setRowspan(1);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table1.addCell(cell);

			cell = new PdfPCell(new Phrase("4", normal_bold));
			cell.setRowspan(1);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table1.addCell(cell);

			cell = new PdfPCell(new Phrase("5", normal_bold));
			cell.setRowspan(1);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table1.addCell(cell);

			cell = new PdfPCell(new Phrase("6", normal_bold));
			cell.setRowspan(1);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table1.addCell(cell);

			cell = new PdfPCell(new Phrase("7", normal_bold));
			cell.setRowspan(1);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table1.addCell(cell);

			cell = new PdfPCell(new Phrase("8", normal_bold));
			cell.setRowspan(1);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table1.addCell(cell);

			cell = new PdfPCell(new Phrase("9", normal_bold));
			cell.setRowspan(1);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table1.addCell(cell);

			cell = new PdfPCell(new Phrase("10", normal_bold));
			cell.setRowspan(1);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table1.addCell(cell);

			cell = new PdfPCell(new Phrase("11", normal_bold));
			cell.setRowspan(1);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table1.addCell(cell);

			cell = new PdfPCell(new Phrase("12", normal_bold));
			cell.setRowspan(1);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table1.addCell(cell);

			cell = new PdfPCell(new Phrase("13", normal_bold));
			cell.setRowspan(1);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table1.addCell(cell);

			cell = new PdfPCell(new Phrase("14", normal_bold));
			cell.setRowspan(1);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table1.addCell(cell);

			cell = new PdfPCell(new Phrase("15", normal_bold));
			cell.setRowspan(1);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table1.addCell(cell);

			cell = new PdfPCell(new Phrase("16", normal_bold));
			cell.setRowspan(1);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table1.addCell(cell);

			cell = new PdfPCell(new Phrase("17", normal_bold));
			cell.setRowspan(1);
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


              /*  cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                        .parseDouble(list_data.get(i)
                                .getCash_bonus_4per())),
                        normal));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                        | Rectangle.BOTTOM);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table1.addCell(cell);*/

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


              /*  cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                        .parseDouble(list_data.get(i)
                                .getCash_bonus_8per())),
                        normal));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                        | Rectangle.BOTTOM);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table1.addCell(cell);*/

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

			// Table here
           /* if (smartbachatbean
					.getPlantype()
					.equals("Option B (Endowment Option with in-built Accidental Death and Total Permanent Disability (AD&TPD) Benefit)")) {
                PdfPTable table1 = new PdfPTable(17);
                table1.setWidths(new float[]{5f, 5f, 5f, 5f, 5f, 5f, 5f, 5f,
                        5f, 5f, 5f,5f,5f,5f,5f,5f,5f});
				table1.setWidthPercentage(100);

				// 1st row
				cell = new PdfPCell(new Phrase(
						"BENEFIT ILLUSTRATION FOR SBI LIFE  Smart Bachat",
						small_bold));
                cell.setColspan(19);
				cell.setPadding(5);
				cell.setBorder(Rectangle.BOTTOM | Rectangle.LEFT
						| Rectangle.RIGHT | Rectangle.TOP);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				table1.addCell(cell);

				// 2nd row
                cell = new PdfPCell(new Phrase("Policy Year", small_bold1));
				cell.setRowspan(3);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				table1.addCell(cell);

                cell = new PdfPCell(new Phrase("Annualized premium",
						small_bold1));
				cell.setRowspan(3);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				table1.addCell(cell);

				cell = new PdfPCell(new Phrase(
                        "Guaranteed Benefits", small_bold1));
                cell.setColspan(5);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				table1.addCell(cell);

				cell = new PdfPCell(new Phrase(
                        "Non-Guaranteed Benefits @ 4%p.a." , small_bold1));
				cell.setColspan(3);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				table1.addCell(cell);

				cell = new PdfPCell(new Phrase(
                        "Non-Guaranteed Benefits @ 8%p.a." , small_bold1));
				cell.setColspan(3);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				table1.addCell(cell);

				// 3rd
                cell = new PdfPCell(new Phrase("Total benefits (including Guaranteed and Non-Guaranteed benefits)", small_bold1));
                cell.setColspan(4);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				table1.addCell(cell);

                cell = new PdfPCell(new Phrase("Guaranteed additions", small_bold1));
                cell.setRowspan(2);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				table1.addCell(cell);

                cell = new PdfPCell(new Phrase("Survival benefit" , small_bold1));
				cell.setRowspan(2);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				table1.addCell(cell);

                cell = new PdfPCell(new Phrase("Surrender Benefit", small_bold1));
				cell.setRowspan(2);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				table1.addCell(cell);

				cell = new PdfPCell(new Phrase("Non-guaranteed", small_bold1));
				cell.setColspan(2);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				table1.addCell(cell);

				cell = new PdfPCell(new Phrase("Guaranteed (GSV)", small_bold1));
				cell.setRowspan(2);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				table1.addCell(cell);

				cell = new PdfPCell(new Phrase("Non-guaranteed (SSV)",
						small_bold1));
				cell.setColspan(2);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				table1.addCell(cell);

				// 4 row
                cell = new PdfPCell(new Phrase("Cash bonus", small_bold1));
                cell.setRowspan(2);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table1.addCell(cell);

                cell = new PdfPCell(new Phrase("Surrender benefit", small_bold1));
                cell.setRowspan(2);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table1.addCell(cell);

                cell = new PdfPCell(new Phrase("Revisionary bonus", small_bold1));
                cell.setRowspan(2);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table1.addCell(cell);

                cell = new PdfPCell(new Phrase("Cash bonus", small_bold1));
                cell.setRowspan(2);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table1.addCell(cell);

                cell = new PdfPCell(new Phrase("Surrender benefit", small_bold1));
                cell.setRowspan(2);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table1.addCell(cell);

                cell = new PdfPCell(new Phrase("Maturity Benefit", small_bold1));
                cell.setColspan(2);
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

				cell = new PdfPCell(new Phrase("4% pa", small_bold1));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				table1.addCell(cell);

				cell = new PdfPCell(new Phrase("8% pa", small_bold1));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				table1.addCell(cell);

				for (int i = 0; i < smartbachatbean.getPolicyterm(); i++) {
					cell = new PdfPCell(new Phrase(prsObj.parseXmlTag(output,
							"policyYr" + (i + 1)), small_normal1));
					cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
							| Rectangle.BOTTOM);
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					table1.addCell(cell);

					cell = new PdfPCell(new Phrase(currencyFormat.format(Double
							.parseDouble(prsObj.parseXmlTag(output,
									"TotBasePremWithTx" + (i + 1)))),
                            small_normal1));*//*
                    cell = new PdfPCell(new Phrase(currencyFormat.format(Double
                            .parseDouble(prsObj.parseXmlTag(output,
                                    "Annualized_Premium" + (i + 1)))),
							small_normal1));
					cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
							| Rectangle.BOTTOM);
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					table1.addCell(cell);

					cell = new PdfPCell(new Phrase(currencyFormat.format(Double
							.parseDouble(prsObj.parseXmlTag(output, "deathGuar"
									+ (i + 1)))), small_normal1));
					cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
							| Rectangle.BOTTOM);
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					table1.addCell(cell);

					cell = new PdfPCell(new Phrase(currencyFormat.format(Double
							.parseDouble(prsObj.parseXmlTag(output,
									"deathNonGuar4pa" + (i + 1)))),
							small_normal1));
					cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
							| Rectangle.BOTTOM);
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					table1.addCell(cell);

					cell = new PdfPCell(new Phrase(currencyFormat.format(Double
							.parseDouble(prsObj.parseXmlTag(output,
									"deathNonGuar8pa" + (i + 1)))),
							small_normal1));
					cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
							| Rectangle.BOTTOM);
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					table1.addCell(cell);

					cell = new PdfPCell(new Phrase(currencyFormat.format(Double
							.parseDouble(prsObj.parseXmlTag(output,
									"Accidentaldeathbenifit" + (i + 1)))),
							small_normal1));
					cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
							| Rectangle.BOTTOM);
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					table1.addCell(cell);

					cell = new PdfPCell(new Phrase(currencyFormat.format(Double
							.parseDouble(prsObj.parseXmlTag(output, "matGuar"
									+ (i + 1)))), small_normal1));
					cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
							| Rectangle.BOTTOM);
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					table1.addCell(cell);

					cell = new PdfPCell(
							new Phrase(currencyFormat.format(Double
									.parseDouble(prsObj.parseXmlTag(output,
											"matNonGuar4pa" + (i + 1)))),
									small_normal1));
					cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
							| Rectangle.BOTTOM);
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					table1.addCell(cell);

					cell = new PdfPCell(
							new Phrase(currencyFormat.format(Double
									.parseDouble(prsObj.parseXmlTag(output,
											"matNonGuar8pa" + (i + 1)))),
									small_normal1));
					cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
							| Rectangle.BOTTOM);
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					table1.addCell(cell);

					cell = new PdfPCell(new Phrase(currencyFormat.format(Double
							.parseDouble(prsObj.parseXmlTag(output, "surGuar"
									+ (i + 1)))), small_normal1));
					cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
							| Rectangle.BOTTOM);
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					table1.addCell(cell);

					cell = new PdfPCell(
							new Phrase(currencyFormat.format(Double
									.parseDouble(prsObj.parseXmlTag(output,
											"surNonGuar4pa" + (i + 1)))),
									small_normal1));
					cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
							| Rectangle.BOTTOM);
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					table1.addCell(cell);

					cell = new PdfPCell(
							new Phrase(currencyFormat.format(Double
									.parseDouble(prsObj.parseXmlTag(output,
											"surNonGuar8pa" + (i + 1)))),
									small_normal1));
					cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
							| Rectangle.BOTTOM);
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					table1.addCell(cell);
				}

				document.add(table1);
				document.add(para_img_logo_after_space_1);
			} else {
				PdfPTable table1 = new PdfPTable(11);
				table1.setWidths(new float[] { 2f, 5f, 5f, 5f, 5f, 5f, 5f, 5f,
						5f, 5f, 5f });
				table1.setWidthPercentage(100);

				// 1st row
				cell = new PdfPCell(new Phrase(
						"BENEFIT ILLUSTRATION FOR SBI LIFE  Smart Bachat",
						small_bold));
				cell.setColspan(11);
				cell.setPadding(5);
				cell.setBorder(Rectangle.BOTTOM | Rectangle.LEFT
						| Rectangle.RIGHT | Rectangle.TOP);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				table1.addCell(cell);

				// 2nd row
				cell = new PdfPCell(new Phrase("End of Policy Year", small_bold1));
				cell.setRowspan(3);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				table1.addCell(cell);

				cell = new PdfPCell(new Phrase("Total Basic Premium paid (without taxes)",
						small_bold1));
				cell.setRowspan(3);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				table1.addCell(cell);

				cell = new PdfPCell(new Phrase(
						"Benefit payable on death (in Rs.)", small_bold1));
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

				cell = new PdfPCell(new Phrase("Non-guaranteed", small_bold1));
				cell.setColspan(2);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				table1.addCell(cell);

				cell = new PdfPCell(new Phrase("Guaranteed", small_bold1));
				cell.setRowspan(2);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				table1.addCell(cell);

				cell = new PdfPCell(new Phrase("Non-guaranteed", small_bold1));
				cell.setColspan(2);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				table1.addCell(cell);

				cell = new PdfPCell(new Phrase("Guaranteed (GSV)", small_bold1));
				cell.setRowspan(2);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				table1.addCell(cell);

				cell = new PdfPCell(new Phrase("Non-guaranteed (SSV)",
						small_bold1));
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

				for (int i = 0; i < smartbachatbean.getPolicyterm(); i++) {
					cell = new PdfPCell(new Phrase(prsObj.parseXmlTag(output,
							"policyYr" + (i + 1)), small_normal1));
					cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
							| Rectangle.BOTTOM);
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					table1.addCell(cell);

					cell = new PdfPCell(new Phrase(currencyFormat.format(Double
							.parseDouble(prsObj.parseXmlTag(output,
									"TotBasePremWithTx" + (i + 1)))),
							small_normal1));
					cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
							| Rectangle.BOTTOM);
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					table1.addCell(cell);

					cell = new PdfPCell(new Phrase(currencyFormat.format(Double
							.parseDouble(prsObj.parseXmlTag(output, "deathGuar"
									+ (i + 1)))), small_normal1));
					cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
							| Rectangle.BOTTOM);
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					table1.addCell(cell);

					cell = new PdfPCell(new Phrase(currencyFormat.format(Double
							.parseDouble(prsObj.parseXmlTag(output,
									"deathNonGuar4pa" + (i + 1)))),
							small_normal1));
					cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
							| Rectangle.BOTTOM);
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					table1.addCell(cell);

					cell = new PdfPCell(new Phrase(currencyFormat.format(Double
							.parseDouble(prsObj.parseXmlTag(output,
									"deathNonGuar8pa" + (i + 1)))),
							small_normal1));
					cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
							| Rectangle.BOTTOM);
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					table1.addCell(cell);

					cell = new PdfPCell(new Phrase(currencyFormat.format(Double
							.parseDouble(prsObj.parseXmlTag(output, "matGuar"
									+ (i + 1)))), small_normal1));
					cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
							| Rectangle.BOTTOM);
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					table1.addCell(cell);

					cell = new PdfPCell(
							new Phrase(currencyFormat.format(Double
									.parseDouble(prsObj.parseXmlTag(output,
											"matNonGuar4pa" + (i + 1)))),
									small_normal1));
					cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
							| Rectangle.BOTTOM);
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					table1.addCell(cell);

					cell = new PdfPCell(
							new Phrase(currencyFormat.format(Double
									.parseDouble(prsObj.parseXmlTag(output,
											"matNonGuar8pa" + (i + 1)))),
									small_normal1));
					cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
							| Rectangle.BOTTOM);
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					table1.addCell(cell);

					cell = new PdfPCell(new Phrase(currencyFormat.format(Double
							.parseDouble(prsObj.parseXmlTag(output, "surGuar"
									+ (i + 1)))), small_normal1));
					cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
							| Rectangle.BOTTOM);
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					table1.addCell(cell);

					cell = new PdfPCell(
							new Phrase(currencyFormat.format(Double
									.parseDouble(prsObj.parseXmlTag(output,
											"surNonGuar4pa" + (i + 1)))),
									small_normal1));
					cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
							| Rectangle.BOTTOM);
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					table1.addCell(cell);

					cell = new PdfPCell(
							new Phrase(currencyFormat.format(Double
									.parseDouble(prsObj.parseXmlTag(output,
											"surNonGuar8pa" + (i + 1)))),
									small_normal1));
					cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
							| Rectangle.BOTTOM);
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					table1.addCell(cell);
				}

				document.add(table1);
				document.add(para_img_logo_after_space_1);
            }*/

			PdfPTable BI_Pdftable_benefit_payable = new PdfPTable(1);
			BI_Pdftable_benefit_payable.setWidthPercentage(100);
			PdfPCell BI_Pdftable_benefit_payable_cell1 = new PdfPCell(
					new Paragraph(
							"Notes:",
							small_bold));
			BI_Pdftable_benefit_payable_cell1.setPadding(5);
			BI_Pdftable_benefit_payable
					.addCell(BI_Pdftable_benefit_payable_cell1);

			PdfPCell BI_Pdftable_benefit_payable_cell12 = new PdfPCell(
					new Paragraph(
							"1. Annualized premium shall be the premium amount payable in a year chosen by the policyholder, excluding the taxes,  rider premiums,underwriting extra premiums and loading for modal premiums, if any. Refer sales literature for explanation of terms used in this illustration.",
							small_normal));
			BI_Pdftable_benefit_payable_cell12.setPadding(5);
			BI_Pdftable_benefit_payable
					.addCell(BI_Pdftable_benefit_payable_cell12);

			PdfPCell BI_Pdftable_benefit_payable_cell13 = new PdfPCell(
					new Paragraph(
							"2. All Benefit amount are derived on the assumption that the policies are ”in-force”",
							small_normal));
			BI_Pdftable_benefit_payable_cell13.setPadding(5);
			BI_Pdftable_benefit_payable
					.addCell(BI_Pdftable_benefit_payable_cell13);

			PdfPCell BI_Pdftable_benefit_payable_cell14 = new PdfPCell(
					new Paragraph(
							"3. The above BI is subject to payment of stipulated premiums on due date.",
							small_normal));
			BI_Pdftable_benefit_payable_cell14.setPadding(5);
			BI_Pdftable_benefit_payable
					.addCell(BI_Pdftable_benefit_payable_cell14);

			PdfPCell BI_Pdftable_benefit_payable_cell15 = new PdfPCell(
					new Paragraph(
							"4. In addition to Guaranteed Surrender Benefits (column 5), Surrender value of the vested bonuses will also be paid. For the purpose of guaranteed surrender value (GSV) in this illustration the surrender value of vested bonuses are not considered at all.",
							small_normal));
			BI_Pdftable_benefit_payable_cell15.setPadding(5);
			BI_Pdftable_benefit_payable
					.addCell(BI_Pdftable_benefit_payable_cell15);

			document.add(BI_Pdftable_benefit_payable);


			document.add(para_img_logo_after_space_1);


			PdfPTable BI_Pdftable_GuaranteedSurrenderValue = new PdfPTable(1);
			BI_Pdftable_GuaranteedSurrenderValue.setWidthPercentage(100);
			PdfPCell BI_Pdftable_GuaranteedSurrenderValue_cell1 = new PdfPCell(
					new Paragraph("Bonus Rates", small_bold));
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
							"This is a with profit plan and participates in the profits of the company’s life insurance business. Simple reversionary bonuses are declared as a percentage rate, which apply to the sum assured of the basic policy. Terminal bonuses, if any, are declared as a percentage rate, which apply to the vested reversionary bonus.\n" +
									"The bonus rates in the benefit illustration are constant. However, in practice, future bonuses are not guaranteed and will depend on future profits. Therefore, the bonuses are shown as non-guaranteed benefits and are calculated so that they are consistent with the two projected investment return assumptions of 4% and 8% per annum.",
							small_normal));

			BI_Pdftable_RegularPremiumPolicies_cell.setPadding(5);

			BI_Pdftable_RegularPremiumPolicies
					.addCell(BI_Pdftable_RegularPremiumPolicies_cell);
			document.add(BI_Pdftable_RegularPremiumPolicies);

			document.add(para_img_logo_after_space_1);

			PdfPTable BI_Pdftable_CompanysPolicySurrender = new PdfPTable(1);
			BI_Pdftable_CompanysPolicySurrender.setWidthPercentage(100);
			PdfPCell BI_Pdftable_CompanysPolicySurrender_cell1 = new PdfPCell(
					new Paragraph("Important : ", small_bold));
			BI_Pdftable_CompanysPolicySurrender_cell1
					.setHorizontalAlignment(Element.ALIGN_LEFT);
			BI_Pdftable_CompanysPolicySurrender_cell1.setPadding(5);

           /* BI_Pdftable_CompanysPolicySurrender
					.addCell(BI_Pdftable_CompanysPolicySurrender_cell1);
			document.add(BI_Pdftable_CompanysPolicySurrender);

			PdfPTable BI_Pdftable_CompanysPolicySurrender1 = new PdfPTable(1);
			BI_Pdftable_CompanysPolicySurrender1.setWidthPercentage(100);
			PdfPCell BI_Pdftable_CompanysPolicySurrender1_cell = new PdfPCell(
					new Paragraph(
							"In practice, the company may pay a surrender value, Special Surrender Value (SSV) which could be higher than the guaranteed surrender value. The Special Surrender Value will be non-guaranteed  which is assessed based on the past financial/demographic experience of the company with regard to your policy/group of similar policies, as well as the likely future experience. The surrender value payable may be reviewed from time to time depending on company's experience of the various factors which impact the surrender values that may be paid. The surrender value payable would be higher of GSV or SSV.",
							small_normal));

			BI_Pdftable_CompanysPolicySurrender1_cell.setPadding(5);

			BI_Pdftable_CompanysPolicySurrender1
					.addCell(BI_Pdftable_CompanysPolicySurrender1_cell);
            document.add(BI_Pdftable_CompanysPolicySurrender1);*/
			document.add(para_img_logo_after_space_1);



			PdfPTable BI_Pdftable_CompanysPolicySurrender2 = new PdfPTable(1);
			BI_Pdftable_CompanysPolicySurrender2.setWidthPercentage(100);
			PdfPCell BI_Pdftable_CompanysPolicySurrender2_cell = new PdfPCell(
					new Paragraph(
							"You may receive a Welcome Call from our representative to confirm your proposal details like Date of Birth, Nominee Name, Address, Email ID, Sum Assured, Premium amount, Premium Payment Term etc. You may have to undergo Medical Test based on our Underwriting Requirements.",
							small_normal));
			PdfPCell you_may = new PdfPCell(
					new Paragraph(
							"You may have to undergo Medical Test based on our Underwriting Requirements.",
							small_normal));

			BI_Pdftable_CompanysPolicySurrender2_cell.setPadding(5);

			BI_Pdftable_CompanysPolicySurrender2.addCell(BI_Pdftable_CompanysPolicySurrender_cell1);
			BI_Pdftable_CompanysPolicySurrender2
					.addCell(BI_Pdftable_CompanysPolicySurrender2_cell);
			BI_Pdftable_CompanysPolicySurrender2
					.addCell(you_may);
			document.add(BI_Pdftable_CompanysPolicySurrender2);


			PdfPTable BI_Pdftable_CompanysPolicySurrender3 = new PdfPTable(1);
			BI_Pdftable_CompanysPolicySurrender3.setWidthPercentage(100);
			PdfPCell BI_Pdftable_CompanysPolicySurrender3_cell = new PdfPCell(
					new Paragraph(
							"Your SBI Life Smart Bachat (UIN: 111N108V03) is a Limited premium policy, for which your first year "
									+ smartbachatbean.getPremiumFrequency()
									+ " premium is Rs. "
									+ currencyFormat.format(Double
									.parseDouble(installmntPremWithSerTx))
									+ ". Your Policy Term is "
									+ smartbachatbean.getPolicyterm()
									+ " years, Premium Payment Term is "
									+ smartbachatbean.getPremiumpayingterm()
									+ " years and Basic Sum Assured is Rs. "
									+ currencyFormat.format(smartbachatbean
									.getSumAssured()), small_normal));

			BI_Pdftable_CompanysPolicySurrender3_cell.setPadding(5);

			BI_Pdftable_CompanysPolicySurrender3
					.addCell(BI_Pdftable_CompanysPolicySurrender3_cell);
			//  document.add(BI_Pdftable_CompanysPolicySurrender3);

			document.add(para_img_logo_after_space_1);

			Calendar present_date = Calendar.getInstance();
			int mDay = present_date.get(Calendar.DAY_OF_MONTH);
			int mMonth = present_date.get(Calendar.MONTH);
			int mYear = present_date.get(Calendar.YEAR);

			String CurrentDate = mDay + "-" + (mMonth + 1) + "-" + mYear;
			PdfPTable BI_Pdftable26 = new PdfPTable(1);
			BI_Pdftable26.setWidthPercentage(100);
			// CR_table6.setWidths(columnWidths_26);
			if(proposer_Is_Same_As_Life_Assured.equalsIgnoreCase("y")){
				name_of_person = name_of_life_assured;
			}else {
				name_of_person = name_of_proposer;
			}
			PdfPCell BI_Pdftable26_cell1 = new PdfPCell(
					new Paragraph(
							"I, "
									+ name_of_person
									+ "  having received the information with respect to the above, have understood the above statement before entering into a contract.",
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


			PdfPTable agentDeclarationTable = new PdfPTable(1);
			agentDeclarationTable.setWidthPercentage(100);
			PdfPCell agentDeclaration = new PdfPCell(
					new Paragraph(commonMethods.getAgentDeclaration(mContext), small_bold));

			agentDeclaration.setPadding(5);
			agentDeclarationTable.addCell(agentDeclaration);
			document.add(agentDeclarationTable);
			document.add(para_img_logo_after_space_1);

			if (!bankUserType.equalsIgnoreCase("Y")) {
				PdfPTable BI_PdftableMarketing = new PdfPTable(1);
				BI_PdftableMarketing.setWidthPercentage(100);
				PdfPCell BI_PdftableMarketing_signature_cell = new PdfPCell(
						new Paragraph("Marketing official's Signature & Company Seal",
								small_bold));
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
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	private void windowmessagesgin() {

		d = new Dialog(BI_SmartBachatActivity.this);
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
				Intent intent = new Intent(BI_SmartBachatActivity.this,
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

	private void setDefaultDate(int id) {
		Calendar present_date = Calendar.getInstance();
		present_date.add(Calendar.YEAR, -id);
		mDay = present_date.get(Calendar.DAY_OF_MONTH);
		mMonth = present_date.get(Calendar.MONTH);
		mYear = present_date.get(Calendar.YEAR);
	}

	private boolean matval() {

		int age = Integer.parseInt(spinner_age.getSelectedItem().toString());
		int policyterm = Integer.parseInt(spinner_policyterm.getSelectedItem()
				.toString());

		int val = age + policyterm;

		if (val > 65) {
			Alert("Maximum maturity age of Life Assured is 65 Years.");

			return false;
		}

		return true;
	}

	private boolean valDoYouBackdate() {
		if (!proposer_Backdating_WishToBackDate_Policy.equals("")) {
			return true;
		} else {
			showAlert.setMessage("Please Select Do you wish to Backdate ");
			showAlert.setNeutralButton("OK",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// apply focusable method
							setFocusable(rb_smart_bachat_backdating_yes);
							rb_smart_bachat_backdating_yes.requestFocus();
						}
					});
			showAlert.show();

			return false;

		}
	}

	private boolean valProposerDob() {

		if (proposer_Is_Same_As_Life_Assured.equalsIgnoreCase("N")) {

			if (proposer_date_of_birth.equals("")
					|| proposer_date_of_birth.equalsIgnoreCase("Select Date")) {
				showAlert
						.setMessage("Please Select Valid Date Of Birth For Proposer");
				showAlert.setNeutralButton("OK",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
												int which) {
								// apply focusable method
								setFocusable(btn_bi_smart_bachat_proposer_date);
								btn_bi_smart_bachat_proposer_date
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
								setFocusable(btn_smart_bachat_backdatingdate);
								btn_smart_bachat_backdatingdate.requestFocus();

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

	private boolean TrueBackdate() {

		try {

			String error = "", error1 = "";

			if (rb_smart_bachat_backdating_yes.isChecked()) {

				final Calendar c = Calendar.getInstance();
				final int currYear = c.get(Calendar.YEAR);
				final int currMonth = c.get(Calendar.MONTH) + 1;

				SimpleDateFormat dateformat1 = new SimpleDateFormat(
						"dd-MM-yyyy");
				Date dtBackDate = dateformat1
						.parse(btn_smart_bachat_backdatingdate.getText()
								.toString());
				Date currentDate = c.getTime();

				Date finYerEndDate = null;

				String back_date_fix = "01-10-2019";

				Date date1 = dateformat1.parse(back_date_fix);
				// if(date1.compareTo(dtBackDate) >= 0){
				// error1 = "Please enter backdation date after 9-1-2017";
				// }

				if (currMonth >= 4) {
					finYerEndDate = dateformat1.parse("1-4-" + currYear);
				} else {
					finYerEndDate = dateformat1.parse("1-4-" + (currYear - 1));
				}
				if (date1.compareTo(dtBackDate) > 0) {
					error = "Please enter backdation date after 03-06-2019";
				} else if (currentDate.before(dtBackDate)) {
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
									// apply focusable method

									btn_smart_bachat_backdatingdate
											.setText("Select Date");
									proposer_Backdating_BackDate = "";
									setFocusable(btn_smart_bachat_backdatingdate);
									btn_smart_bachat_backdatingdate
											.requestFocus();
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

	/**
	 * Added by vrushali on 27 Dec 2016
	 *
	 * @param premiumwithRoundUP
	 * @return
	 */

	private boolean valPremAmount(double premiumwithRoundUP) {
		String error = "";

		if ((premiumwithRoundUP < 5100)
				&& spinner_premiumfreq.getSelectedItem().toString()
				.equals("Yearly")) {
			error = "Minimum Premium for Yearly Frequency mode must be greater than 5100";
		} else if ((premiumwithRoundUP < 2600)
				&& spinner_premiumfreq.getSelectedItem().toString()
				.equals("Half-Yearly")) {
			error = "Minimum Premium for Half - Yearly Frequency mode must be greater than 2600";
		} else if ((premiumwithRoundUP < 1350)
				&& spinner_premiumfreq.getSelectedItem().toString()
				.equals("Quarterly")) {
			error = "Minimum Premium for Quarterly Frequency mode must be greater than 1350";
		} else if ((premiumwithRoundUP < 450)
				&& spinner_premiumfreq.getSelectedItem().toString()
				.equals("Monthly")) {
			error = "Minimum Premium for Monthly Frequency mode must be greater than 450";
		}

		if (!error.equals("")) {
			Alert(error);
			return false;
		} else
			return true;
	}

	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

		// TODO Auto-generated method stub
		if (v.getId() == edt_bi_smart_bachat_life_assured_first_name.getId()) {
			// clearFocusable(edt_bi_smart_bachat_life_assured_first_name);
			setFocusable(edt_bi_smart_bachat_life_assured_middle_name);
			edt_bi_smart_bachat_life_assured_middle_name.requestFocus();
		} else if (v.getId() == edt_bi_smart_bachat_life_assured_middle_name
				.getId()) {
			// clearFocusable(edt_bi_smart_bachat_life_assured_middle_name);
			setFocusable(edt_bi_smart_bachat_life_assured_last_name);
			edt_bi_smart_bachat_life_assured_last_name.requestFocus();
		} else if (v.getId() == edt_bi_smart_bachat_life_assured_last_name
				.getId()) {

			InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(
					edt_bi_smart_bachat_life_assured_last_name.getWindowToken(),
					0);
			// clearFocusable(edt_bi_smart_bachat_life_assured_last_name);
			setFocusable(btn_bi_smart_bachat_life_assured_date);
			btn_bi_smart_bachat_life_assured_date.requestFocus();
		}

		else if (v.getId() == edt_bi_smart_bachat_proposer_first_name.getId()) {
			// clearFocusable(edt_bi_smart_bachat_proposer_first_name);
			setFocusable(edt_bi_smart_bachat_proposer_middle_name);
			edt_bi_smart_bachat_proposer_middle_name.requestFocus();
		} else if (v.getId() == edt_bi_smart_bachat_proposer_middle_name
				.getId()) {
			// clearFocusable(edt_bi_smart_bachat_proposer_middle_name);
			setFocusable(edt_bi_smart_bachat_proposer_last_name);
			edt_bi_smart_bachat_proposer_last_name.requestFocus();
		} else if (v.getId() == edt_bi_smart_bachat_proposer_last_name.getId()) {

			InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(
					edt_bi_smart_bachat_proposer_last_name.getWindowToken(), 0);
			// clearFocusable(edt_bi_smart_bachat_proposer_last_name);
			setFocusable(btn_bi_smart_bachat_proposer_date);
			btn_bi_smart_bachat_proposer_date.requestFocus();
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
			clearFocusable(spinner_select_plan);
			setFocusable(spinner_select_plan);
			spinner_select_plan.requestFocus();
		}

		else if (v.getId() == spinner_select_plan.getId()) {
			// clearFocusable(premiumAmt);
			clearFocusable(spinner_policyterm);
			setFocusable(spinner_policyterm);
			spinner_policyterm.requestFocus();
		} else if (v.getId() == spinner_policyterm.getId()) {
			// clearFocusable(premiumAmt);
			clearFocusable(spinner_premium_payment_term);
			setFocusable(spinner_premium_payment_term);
			spinner_premium_payment_term.requestFocus();
		}

		else if (v.getId() == spinner_premium_payment_term.getId()) {
			// clearFocusable(premiumAmt);
			clearFocusable(spinner_premiumfreq);
			setFocusable(spinner_premiumfreq);
			spinner_premiumfreq.requestFocus();
		} else if (v.getId() == spinner_premiumfreq.getId()) {
			// clearFocusable(premiumAmt);
			clearFocusable(et_sumassured);
			setFocusable(et_sumassured);
			et_sumassured.requestFocus();
		} else if (v.getId() == et_sumassured.getId()) {
			// clearFocusable(premiumAmt);
			setFocusable(et_adtpd_benifit);
			et_adtpd_benifit.requestFocus();
		} else if (v.getId() == et_adtpd_benifit.getId()) {
			// clearFocusable(premiumAmt);
			setFocusable(rb_smart_bachat_backdating_no);
			rb_smart_bachat_backdating_no.requestFocus();
		} else if (v.getId() == rb_smart_bachat_backdating_no.getId()) {

			InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(
					rb_smart_bachat_backdating_no.getWindowToken(), 0);
		}
		return true;

	}

//    public void SaveEndTime() {
//        app.endtime();
//        try {
//            long rowId = app.insertAppAnalysisdata();
//            if (rowId > 0) {
//                Toast.makeText(getApplicationContext(), "All Data saved", Toast.LENGTH_SHORT).show();
//            } else {
//                Toast.makeText(getApplicationContext(), " Data  not saved", Toast.LENGTH_SHORT).show();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

//    public void SetComboBIDetails() {
//
//
//        String str_Combo_Id = "";
//        String str_Quot_No1 = "";
//        String str_Quot_No2 = "";
//        String str_Quot_No3 = "";
//        String str_Combo_flag = "";
//        String str_Combo_Primary_product = "";
//        String str_Combo_input_data = "";
//        String str_Combo_output_data = "";
//        try {
//
//            String ComboProduct = getString(R.string.sbi_life_smart_bachat);
//            List<M_Combo_Details> list_ComboDetails = db
//                    .getComboDetailsBasedquotNumber(QuatationNumber, ComboProduct);
//            List<M_Benefit_Illustration_Detail> data = db
//                    .getBIDetail(QuatationNumber);
//            if (list_ComboDetails.size() > 0) {
//                spinner_select_plan.setClickable(false);
//                spinner_select_plan.setEnabled(false);
//
//                spinner_age.setClickable(false);
//                spinner_age.setEnabled(false);
//
////            spinner_selGender = (Spinner) findViewById(R.id.spinner_selGender);
////            spinner_selGender.setClickable(false);
////            spinner_selGender.setEnabled(false);
////
////            spinner_selGender.setClickable(false);
////            spinner_selGender.setClickable(false);
////            spinner_selGender.setEnabled(false);
////
////            spinner_selGender.setEnabled(false);
////            spinner_selGender.setClickable(false);
////            spinner_selGender.setEnabled(false);
//
//                iv_men.setClickable(false);
//                iv_men.setEnabled(false);
//
//                iv_women.setClickable(false);
//                iv_women.setEnabled(false);
//
//                iv_transgender.setClickable(false);
//                iv_transgender.setEnabled(false);
//
//
//                spinner_policyterm.setClickable(false);
//                spinner_policyterm.setEnabled(false);
//
//                spinner_premium_payment_term.setClickable(false);
//                spinner_premium_payment_term.setEnabled(false);
//
//                spinner_premiumfreq.setClickable(false);
//                spinner_premiumfreq.setEnabled(false);
//
//                et_sumassured.setClickable(false);
//                et_sumassured.setEnabled(false);
//
//                et_adtpd_benifit.setClickable(false);
//                et_adtpd_benifit.setEnabled(false);
//
//
//                cb_staffdisc.setClickable(false);
//                cb_staffdisc.setEnabled(false);
//
//                cb_JKResident.setClickable(false);
//                cb_JKResident.setEnabled(false);
//
//                spnr_bi_smart_bachat_life_assured_title.setClickable(false);
//                spnr_bi_smart_bachat_life_assured_title.setEnabled(false);
//
//                edt_bi_smart_bachat_life_assured_first_name.setClickable(false);
//                edt_bi_smart_bachat_life_assured_first_name.setEnabled(false);
//
//                edt_bi_smart_bachat_life_assured_middle_name.setClickable(false);
//                edt_bi_smart_bachat_life_assured_middle_name.setEnabled(false);
//
//                edt_bi_smart_bachat_life_assured_last_name.setClickable(false);
//                edt_bi_smart_bachat_life_assured_last_name.setEnabled(false);
//
//                btn_bi_smart_bachat_life_assured_date.setClickable(false);
//                btn_bi_smart_bachat_life_assured_date.setEnabled(false);
//
//                spnr_bi_smart_bachat_proposer_title.setClickable(false);
//                spnr_bi_smart_bachat_proposer_title.setEnabled(false);
//
//                edt_bi_smart_bachat_proposer_first_name.setClickable(false);
//                edt_bi_smart_bachat_proposer_first_name.setEnabled(false);
//                edt_bi_smart_bachat_proposer_middle_name.setClickable(false);
//                edt_bi_smart_bachat_proposer_middle_name.setEnabled(false);
//                edt_bi_smart_bachat_proposer_last_name.setClickable(false);
//                edt_bi_smart_bachat_proposer_last_name.setEnabled(false);
//                btn_bi_smart_bachat_proposer_date.setClickable(false);
//                btn_bi_smart_bachat_proposer_date.setEnabled(false);
//                edt_proposerdetail_basicdetail_contact_no.setClickable(false);
//                edt_proposerdetail_basicdetail_contact_no.setEnabled(false);
//
//                edt_proposerdetail_basicdetail_Email_id.setClickable(false);
//                edt_proposerdetail_basicdetail_Email_id.setEnabled(false);
//
//                edt_proposerdetail_basicdetail_ConfirmEmail_id.setClickable(false);
//                edt_proposerdetail_basicdetail_ConfirmEmail_id.setEnabled(false);
//
//                rb_smart_bachat_backdating_yes.setClickable(false);
//                rb_smart_bachat_backdating_yes.setEnabled(false);
//
//                rb_smart_bachat_backdating_no.setClickable(false);
//                rb_smart_bachat_backdating_no.setEnabled(false);
//
//                btn_smart_bachat_backdatingdate.setClickable(false);
//                btn_smart_bachat_backdatingdate.setEnabled(false);
//
//
//                cb_kerladisc.setClickable(false);
//                cb_kerladisc.setEnabled(false);
//
//
//            }
//
//            if (list_ComboDetails.size() > 0 && data.size() == 0) {
//                str_Combo_input_data = list_ComboDetails.get(0).getStr_Combo_input_data();
//                str_Combo_output_data = list_ComboDetails.get(0).getStr_Combo_output_data();
//
//                JSONObject json_input = new JSONObject(str_Combo_input_data);
//
//                JSONObject json_leadinfo = json_input.getJSONObject("leadinfo");
//
//                String MOBILE_NO = (String) json_leadinfo.get("MOBILE_NO").toString();
//                String EMAIL_ID = (String) json_leadinfo.get("EMAIL_ID").toString();
//
//
//                JSONObject json_planInputInfoModel = json_input.getJSONObject("planInputInfoModel");
//
//                String PropSalutation = (String) json_planInputInfoModel.get("PropSalutation").toString();
//
//                String SMARTBACHAT_OPTION = (String) json_planInputInfoModel.get("SMARTBACHAT_OPTION").toString();
//                String PropFName = (String) json_planInputInfoModel.get("PropFName").toString();
//                String PropMName = (String) json_planInputInfoModel.get("PropMName").toString();
//                String PropLName = (String) json_planInputInfoModel.get("PropLName").toString();
//                String PropDOB = (String) json_planInputInfoModel.get("PropDOB").toString();
//                String PropAge = (String) json_planInputInfoModel.get("PropAge").toString();
//                String PropGender = (String) json_planInputInfoModel.get("PropGender").toString();
//                String PropIsSmoker = (String) json_planInputInfoModel.get("PropIsSmoker").toString();
//                String IsPropLISame = (String) json_planInputInfoModel.get("IsPropLISame").toString();
//
//
//                name_of_life_assured
//                        = PropSalutation + " "
//                        + PropFName + " "
//                        + PropMName + " " + PropLName;
//                lifeAssured_Title = PropSalutation;
//
//
//                String[] LifeAssuredname = name_of_life_assured.split(" ");
//                spnr_bi_smart_bachat_life_assured_title.setSelection(
//                        getIndex(spnr_bi_smart_bachat_life_assured_title,
//                                lifeAssured_Title), false);
//
//                edt_bi_smart_bachat_life_assured_first_name
//                        .setText(PropFName);
//                edt_bi_smart_bachat_life_assured_middle_name
//                        .setText(PropMName);
//                edt_bi_smart_bachat_life_assured_last_name
//                        .setText(PropLName);
//
//
//                lifeAssured_date_of_birth = getDate_MM_DD_YYYY(PropDOB);
//                btn_bi_smart_bachat_life_assured_date
//                        .setText(getDate(lifeAssured_date_of_birth));
//
//
//                proposer_Backdating_WishToBackDate_Policy = "n";
//
//                proposer_Backdating_BackDate = "";
//
//                if (proposer_Backdating_WishToBackDate_Policy.equalsIgnoreCase("y")) {
//                    rb_smart_bachat_backdating_yes.setChecked(true);
//                    ll_backdating1.setVisibility(View.VISIBLE);
//                    btn_smart_bachat_backdatingdate
//                            .setText(proposer_Backdating_BackDate);
//
//                } else {
//                    rb_smart_bachat_backdating_no.setChecked(true);
//                    ll_backdating1.setVisibility(View.GONE);
//
//                }
//
//
//                edt_proposerdetail_basicdetail_contact_no.setText(MOBILE_NO);
//                edt_proposerdetail_basicdetail_Email_id.setText(EMAIL_ID);
//                edt_proposerdetail_basicdetail_ConfirmEmail_id
//                        .setText(EMAIL_ID);
//
//
//                proposer_Is_Same_As_Life_Assured = "y";
//
//
//                age_entry = PropAge;
//                lifeAssuredAge = age_entry;
//                spinner_age.setSelection(getIndex(spinner_age, age_entry), false);
//
//
//                if (PropGender.equals("M")) {
//                    gender = "Male";
//                } else if (PropGender.equals("F")) {
//                    gender = "Female";
//                }
//                if (gender.equalsIgnoreCase("Male")) {
//                    iv_men.setImageDrawable(getResources().getDrawable(
//                            R.drawable.male_selected));
//                    iv_women.setImageDrawable(getResources().getDrawable(
//                            R.drawable.female_nonselected));
//                } else if (gender.equalsIgnoreCase("Female")) {
//                    iv_women.setImageDrawable(getResources().getDrawable(
//                            R.drawable.female_selected));
//                    iv_men.setImageDrawable(getResources().getDrawable(
//                            R.drawable.male_nonselected));
//                }
//
//
//                JSONObject json_main = new JSONObject(str_Combo_output_data);
//
//                JSONObject json_PlanComboTable = json_main.getJSONObject("presentationDataInfo");
//
//
//                JSONArray ja_PlanComboTable_aaray = (JSONArray) json_PlanComboTable.get("PlanComboTable");
//
//                String additionalOutput4 = (String) json_main.get("additionalOutput4").toString();
//
//
//                if (additionalOutput4 != null) {
//                    if (additionalOutput4.equalsIgnoreCase("Y")) {
//                        str_kerla_discount = "Yes";
//                        cb_kerladisc.setChecked(true);
//                    }
//                }
//
//                String Premium_SW = "";
//                String Premium_PS = "";
//                String Term_PS = "";
//                String premPaying_Term = "";
//                String premiumAmount = "";
//                String samf = "";
//                String premFreq = "";
//                for (int i = 0; i < ja_PlanComboTable_aaray.length(); i++) {
//                    String ProdName = ja_PlanComboTable_aaray.getJSONObject(i).getString("ProdName").toString();
//                    if (ProdName != null && ProdName.contains("Smart Bachat")) {
//                        String ProdCode_SB = ja_PlanComboTable_aaray.getJSONObject(i).getString("ProdCode").toString();
//                        String Term_SB = ja_PlanComboTable_aaray.getJSONObject(i).getString("Term").toString();
//                        String TermDisplay_SB = ja_PlanComboTable_aaray.getJSONObject(i).getString("TermDisplay").toString();
//                        String PPT_SB = ja_PlanComboTable_aaray.getJSONObject(i).getString("PPT").toString();
//                        String PayMode_SB = ja_PlanComboTable_aaray.getJSONObject(i).getString("PayMode").toString();
//                        String SumAssu_SB = ja_PlanComboTable_aaray.getJSONObject(i).getString("SumAssu").toString();
//                        String Premium_SB = ja_PlanComboTable_aaray.getJSONObject(i).getString("Premium").toString();
//                        String AnnuPremium_SB = ja_PlanComboTable_aaray.getJSONObject(i).getString("AnnuPremium").toString();
//
//                        String PremiumWithFYST_SB = ja_PlanComboTable_aaray.getJSONObject(i).getString("PremiumWithFYST").toString();
//                        String RenewalPremium_SB = ja_PlanComboTable_aaray.getJSONObject(i).getString("RenewalPremium").toString();
//                        String FundDetailTable_SB = ja_PlanComboTable_aaray.getJSONObject(i).getString("FundDetailTable").toString();
//                        String PlanOption_SB = ja_PlanComboTable_aaray.getJSONObject(i).getString("PlanOption").toString();
//                        String RetiAge_SB = ja_PlanComboTable_aaray.getJSONObject(i).getString("RetiAge").toString();
//                        String DesiredMaturity_SB = ja_PlanComboTable_aaray.getJSONObject(i).getString("DesiredMaturity").toString();
//                        String DesiredLifeCover_SB = ja_PlanComboTable_aaray.getJSONObject(i).getString("DesiredLifeCover").toString();
//                        String PremiumBudget_SB = ja_PlanComboTable_aaray.getJSONObject(i).getString("PremiumBudget").toString();
//                        String IsRider_SB = ja_PlanComboTable_aaray.getJSONObject(i).getString("IsRider").toString();
//
//
//                        String RiderSumAssu_SB = ja_PlanComboTable_aaray.getJSONObject(i).getString("RiderSumAssu").toString();
//                        String AnnuityOption_SB = ja_PlanComboTable_aaray.getJSONObject(i).getString("AnnuityOption").toString();
//                        String Annuity_SB = ja_PlanComboTable_aaray.getJSONObject(i).getString("Annuity").toString();
//                        String DesiredAnnualIncome_SB = ja_PlanComboTable_aaray.getJSONObject(i).getString("DesiredAnnualIncome").toString();
//                        String SWB_SAMF_SB = ja_PlanComboTable_aaray.getJSONObject(i).getString("SWB_SAMF").toString();
//                        String SMARTBACHAT_PAY_FREQUENCY_SB = ja_PlanComboTable_aaray.getJSONObject(i).getString("SMARTBACHAT_PAY_FREQUENCY").toString();
//                        String SMARTBACHAT_OPTION_SB = ja_PlanComboTable_aaray.getJSONObject(i).getString("SMARTBACHAT_OPTION").toString();
//                        String ANNUITY_OPTION_SB = ja_PlanComboTable_aaray.getJSONObject(i).getString("ANNUITY_OPTION").toString();
//                        String UIN_SB = ja_PlanComboTable_aaray.getJSONObject(i).getString("UIN").toString();
//
//
//                        if (SMARTBACHAT_OPTION.equals("A")) {
//                            plan = "Option A (Endowment Option)";
//                        } else {
//                            plan = "Option B (Endowment Option with in-built Accidental Death and Total Permanent Disability (AD&TPD) Benefit)";
//                        }
//
//
//                        spinner_select_plan.setSelection(
//                                getIndex(spinner_select_plan, plan), false);
//
//					/*	staffdiscount = prsObj.parseXmlTag(input, "isStaff");
//                        if (staffdiscount.equalsIgnoreCase("true")) {
//							cb_staffdisc.setChecked(true);
//						} else {
//							cb_staffdisc.setChecked(false);
//						}*/
//                    /*	isJkResident = prsObj.parseXmlTag(input, "isJKResident");
//                        if (isJkResident.equalsIgnoreCase("true")) {
//							cb_JKResident.setChecked(true);
//						} else {
//							cb_JKResident.setChecked(false);
//
//						}*/
//                        policy_term = Term_SB;
//                        premPayTerm = PPT_SB;
//                        premiumPaymentMode = "Yearly";
//                        sum_assured = obj.getRound(obj.getStringWithout_E(Double.valueOf(SumAssu_SB)));
//                        spinner_policyterm.setSelection(
//                                getIndex(spinner_policyterm, policy_term), false);
//
//
//                        spinner_premium_payment_term.setSelection(
//                                getIndex(spinner_premium_payment_term, premPayTerm), false);
//
//                        spinner_premiumfreq.setSelection(
//                                getIndex(spinner_premiumfreq, premiumPaymentMode), false);
//
//
//                        et_sumassured.setText(sum_assured);
//                        // et_adtpd_benifit.setText(adtpdbenfits);
//
//                        try {
//                            if (!sum_assured.equals("")) {
//                                int sumAssured = Integer.parseInt(sum_assured);
//                                if (sumAssured < 5000000)
//                                    et_adtpd_benifit.setText(sumAssured + "");
//                                else {
//                                    et_adtpd_benifit.setText(5000000 + "");
//                                }
//                            } else
//                                et_adtpd_benifit.setText(0 + "");
//                        } catch (Exception e) {
//                        }
//
//
//                        if (plan.equalsIgnoreCase("Option A (Endowment Option)")) {
//                            tableRow_adtpd.setVisibility(View.GONE);
//
//                        } else {
//                            // adtpdbenfits = sum_assured;
//                            tableRow_adtpd.setVisibility(View.VISIBLE);
//
//                        }
//
//
//                    }
//
//
//                }
//
//
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

	public String getDate_MM_DD_YYYY(String OldDate) {
		String NewDate = "";
		try {

			String dateReceivedFromUser = OldDate;
			DateFormat userDateFormat = new SimpleDateFormat("dd/MM/yyyy");
			DateFormat dateFormatNeeded = new SimpleDateFormat("MM-dd-yyyy");
			Date date = userDateFormat.parse(dateReceivedFromUser);

			NewDate = dateFormatNeeded.format(date);

		} catch (Exception e) {
			// TODO: handle exception
			Log.e("Integrated Service", "Error in Getting Date");
		} finally {
		}

		return NewDate;
	}





	private class SendMailTask extends AsyncTask<Message, Void, Void> {
		private ProgressDialog progressDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog = ProgressDialog.show(
					BI_SmartBachatActivity.this, "Please wait... ",
					"Processing", true, true);
		}

		@Override
		protected void onPostExecute(Void aVoid) {
			super.onPostExecute(aVoid);
			progressDialog.dismiss();
			ConnectivityManager mgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo netInfo = mgr.getActiveNetworkInfo();

			if (netInfo != null && netInfo.isConnected()) {

				Toast.makeText(
						BI_SmartBachatActivity.this,
						"Benefit Illustrator PDF Has Been Sent to Your Email Id",
						Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(
						BI_SmartBachatActivity.this,
						"Benefit Illustrator PDF will be emailed when user is online",
						Toast.LENGTH_SHORT).show();

			}

		}

		@Override
		protected Void doInBackground(Message... messages) {
			try {
				Transport.send(messages[0]);
			} catch (MessagingException e) {
				e.printStackTrace();
			}
			return null;
		}
	}

	private Message createMessage(String email, String subject,
								  String messageBody, Session session, File FileName)
			throws MessagingException, UnsupportedEncodingException {
		Message message = new MimeMessage(session);
		message.setFrom(new InternetAddress("sbilconnectlife@sbi-life.com"));
		message.addRecipient(Message.RecipientType.TO, new InternetAddress(
				email));
		message.setSubject(subject);
		message.setText(messageBody);
		// message.setFileName(FileName);
		if (FileName != null) {
			MimeBodyPart mbp1 = new MimeBodyPart();
			mbp1.setText(messageBody);
			MimeBodyPart mbp2 = new MimeBodyPart();
			FileDataSource fds = new FileDataSource(FileName);
			mbp2.setDataHandler(new DataHandler(fds));
			mbp2.setFileName(fds.getName());
			Multipart mp = new MimeMultipart();
			mp.addBodyPart(mbp1);
			mp.addBodyPart(mbp2);
			message.setContent(mp);
		}

		return message;
	}

	private void sendMail(String email, String subject, String messageBody,
						  File FileName) {
		Session session = createSessionObject();

		try {
			Message message = createMessage(email, subject, messageBody,
					session, FileName);
			new SendMailTask().execute(message);
		} catch (AddressException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	private Session createSessionObject() {
		Properties properties = new Properties();
		// properties.put("mail.smtp.auth", "true");
		// properties.put("mail.smtp.starttls.enable", "true");
		// properties.put("mail.smtp.host", "smtp.sbi-life.com");
		// properties.put("mail.smtp.port", "25");

		properties.put("mail.smtp.host", "webmail.sbi-life.com");
		properties.put("mail.smtp.socketFactory.port", "465");
		properties.put("mail.smtp.socketFactory.class",
				"javax.net.ssl.SSLSocketFactory");
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.port", "465");

		return Session.getInstance(properties, new javax.mail.Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(
						"sbilconnectlife@sbi-life.com", "sky@12345");
			}
		});
	}

	boolean TrueBackdateLaunch() {
		try {
			String error = "";

			if (rb_smart_bachat_backdating_yes.isChecked()) {

				final Calendar c = Calendar.getInstance();
				final int currYear = c.get(Calendar.YEAR);
				final int currMonth = c.get(Calendar.MONTH) + 1;

				SimpleDateFormat dateformat1 = new SimpleDateFormat(
						"dd-MM-yyyy");
				Date dtBackDate = dateformat1.parse(proposer_Backdating_BackDate);
				Date currentDate = c.getTime();

				Date finYerEndDate = null;

				/** Added by Vrushali on19-11-2015 start **/
				Date launchDate = dateformat1.parse("03-06-2019");
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



}
