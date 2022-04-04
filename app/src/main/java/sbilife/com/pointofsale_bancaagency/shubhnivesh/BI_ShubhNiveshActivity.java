package sbilife.com.pointofsale_bancaagency.shubhnivesh;

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
public class BI_ShubhNiveshActivity extends AppCompatActivity implements
		OnEditorActionListener {
	private final int SIGNATURE_ACTIVITY = 1;
	private final int DATE_DIALOG_ID = 1;


    public double totInstPrem_exclST = 0, premPTA = 0, premBasic = 0, premBasicRoundUp = 0,
            premADB = 0, premATPDB = 0, premFY = 0;
    String premiumSingleInstBasic, premInstBasicFirstYear, premInstBasicSecondYear, premInstFYRider, premInstSYRider, premiumSingleInstBasicRider, nonGuaranMatBen_4Percent, nonGuaranMatBen_8Percent;
	private NeedAnalysisBIService NABIObj;
	private NA_CBI_bean na_cbi_bean;
	private File needAnalysispath,newFile;
	private String agentcode,agentMobile,agentEmail,userType;
	private int needAnalysis_flag=0;
	private String na_input=null;
	private String na_output=null, backDateinterest = "", BackDateinterestwithGST = "",
			minesOccuInterest = "";
	private DatabaseHelper dbHelper;
	private String na_dob="";
	private int flag=0;
	private String QuatationNumber="";
	private String planName = "";

	// UI Elements
	private CheckBox selStaffDisc, selBancAssuranceDisc, selADBRider, selATPDBRider,
			selPTARider, selJkResident;
	private Button btnSubmit, back;
	private Spinner selBasicTerm, selPremFreq, ageInYears, selPlan, selGender,
			selADBTerm, selATPDBTerm, selPTATerm;
	private TextView  adb_txt1, adb_txt2, atpdb_txt2, atpdb_txt1,
			selPTATerm_txt2, selPTATerm_txt1;
	private EditText adbSA, atpdbSA, ptaSA, basicSA;
	private String Premium_pdf;
	private String BackdatingInt;
	private String premiumPTAWithoutAnyDisc;
	private String premiumADBWithoutAnyDisc;
	private String premiumATPDBWithoutAnyDisc;

    String BackDateinterest, MinesOccuInterest;
	// Variable declaration
	private  int  age = 0;
    String premiumBasicWithoutAnyDisc;
//    private double premBasic = 0;

	private AlertDialog.Builder showAlert;
	private DecimalFormat currencyFormat;
	private String proposer_Title = "", proposer_First_Name = "",
			proposer_Middle_Name = "", proposer_Last_Name = "",
			product_name = "", proposer_Is_Same_As_Life_Assured = "",
			lifeAssuredAge = "";

	private Spinner spnr_bi_shubh_nivesh_proposer_title;
	private EditText edt_bi_shubh_nivesh_proposer_first_name, edt_bi_shubh_nivesh_proposer_middle_name,edt_bi_shubh_nivesh_proposer_last_name,edt_bi_shubh_nivesh_age;

	private EditText edt_bi_shubh_nivesh_life_assured_first_name,
			edt_bi_shubh_nivesh_life_assured_middle_name,
			edt_bi_shubh_nivesh_life_assured_last_name,
			edt_bi_shubh_nivesh_life_assured_age;

	private TableRow tv_monthly_mode;

	private Spinner spnr_bi_shubh_nivesh_life_assured_title;

	private Button btn_bi_shubh_nivesh_life_assured_date,
			btn_proposerdetail_personaldetail_backdatingdate;

	private String lifeAssured_Title = "", lifeAssured_First_Name = "",
			lifeAssured_Middle_Name = "", lifeAssured_Last_Name = "",
			name_of_life_assured = "";
	private String lifeAssured_date_of_birth = "";

	private TableRow tr_shubh_nivesh_proposer_detail2,
			tr_shubh_nivesh_proposer_detail1;
	// For xml Output
	private StringBuilder retVal, bussIll;
//    private String nonGuaranMatBen_4Percent, nonGuaranMatBen_8Percent;
	private ShubhNiveshBean shubhNiveshBean;
    private List<M_BI_ShubhNivesh_AdapterCommon> list_data;
	private RadioButton rb_shubh_nivesh_proposer_same_as_life_assured_yes,
			rb_shubh_nivesh_proposer_same_as_life_assured_no,
			rb_proposerdetail_personaldetail_backdating_yes,
			rb_proposerdetail_personaldetail_backdating_no;
	private LinearLayout ll_backdating1;

    String output = "";
    String input = "";
    String premiumPaymentMode = "";
	private String age_entry = "";
	private String gender = "";

	private String premium_paying_frequency = "";

	private String plan = "";
	private String policy_term = "";
	private String sum_assured = "";


	private String adb_rider_status = "";
	private String atpdb_rider_status = "";
	private String ptr_rider_status = "";

	private String term_adb = "";
	private String sa_adb = "";
	private String prem_adb = "";

	private String term_atpdb = "";
	private String sa_atpdb = "";
	private String prem_atpdb = "";

	private String term_pta = "";
	private String sa_pta = "";
	private String prem_pta = "";

	private String basicprem = "";
	private String servicetax = "";
	private String basicplustax = "",servcTaxSecondYear="",premWthSTSecondYear="";

	// For Bi Dialog
	private ParseXML prsObj;
	private String name_of_proposer = "";
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

	private File mypath;
	private Dialog d;
	private String latestImage = "";
	private  int DIALOG_ID;
	private int mYear;
	private int mMonth;
	private int mDay;

	// for BI
	private StringBuilder inputVal;

	private Button btn_bi_shubh_nivesh_proposer_date;
	private  String proposer_date_of_birth = "";
	private String proposer_Backdating_WishToBackDate_Policy = "";
	private String proposer_Backdating_BackDate = "";
	private CommonForAllProd obj;

	private ScrollView svShubhNiveshMain;
	private boolean flagFirstFocus = true;
    String staffdiscount = "";

	/* Basic Details */

	private EditText edt_proposerdetail_basicdetail_contact_no,
			edt_proposerdetail_basicdetail_Email_id,
			edt_proposerdetail_basicdetail_ConfirmEmail_id;

	private String mobileNo = "";
	private String emailId = "";
	private String ConfirmEmailId = "";
	private boolean validationFla1 = false;
	private String ProposerEmailId = "";
	private Bitmap photoBitmap;

	private String basicServiceTax="",SBCServiceTax="",KKCServiceTax="",basicServiceTaxSecondYear="",SBCServiceTaxSecondYear="",KKCServiceTaxSecondYear="";

	private String bankUserType = "",mode = "";

	/*parivartan changes*/
	private String Check = "";
	private Context context;
	private CommonMethods commonMethods;
	private StorageUtils mStorageUtils;
	private ImageButton imageButtonShubhNiveshProposerPhotograph;

	private LinearLayout linearlayoutThirdpartySignature,linearlayoutAppointeeSignature;
	private ImageButton Ibtn_signatureofThirdParty,Ibtn_signatureofAppointee;
	private String thirdPartySign = "",appointeeSign = "";
	private String product_Code,product_UIN,product_cateogory,product_type;
	private Double premBasicRoundedOff2;

	private String Company_policy_surrender_dec = "";
	/*end*/

	private CheckBox cb_kerladisc;
    String str_kerla_discount = "No";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.bi_shubhniveshmain);
		//getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);

		dbHelper = new DatabaseHelper(getApplicationContext());

		context = this;
		commonMethods = new CommonMethods();
		mStorageUtils = new StorageUtils();

		commonMethods.setApplicationToolbarMenu(this,getString(R.string.app_name));

		NABIObj=new NeedAnalysisBIService(this);
		prsObj = new ParseXML();
		Intent intent=getIntent();

		//Added Tushar Kadam Kerla Applicable Tax 7-Jun-2019
		TableRow tablerowKerlaDiscount = findViewById(R.id.tablerowKerlaDiscount);
		cb_kerladisc =  findViewById(R.id.cb_kerladisc);
		commonMethods.setKerlaDiscount(context, tablerowKerlaDiscount, cb_kerladisc);
		//End Added Tushar Kadam Kerla Applicable Tax 7-Jun-2019

		String na_flag = intent.getStringExtra("NAFlag");

		if(na_flag!=null)
		{
			if(na_flag.equalsIgnoreCase("1"))
			{
				needAnalysis_flag=1;
				na_input=intent.getStringExtra("NaInput");
				na_output=intent.getStringExtra("NaOutput");
				na_dob=intent.getStringExtra("custDOB");
				gender=intent.getStringExtra("custGender");

				bankUserType = intent.getStringExtra("Other");

				if (bankUserType != null) {

				} else {
					bankUserType = "";
				}
				//URNNumber = intent.getStringExtra("URNNumber");
				try {
					agentcode = SimpleCrypto.decrypt("SBIL", dbHelper.GetUserCode());

					agentMobile= SimpleCrypto.decrypt("SBIL", dbHelper.GetMobileNo());
					agentEmail= SimpleCrypto.decrypt("SBIL", dbHelper.GetEmailId());
					userType= SimpleCrypto.decrypt("SBIL", dbHelper.GetUserType());

					/*parivartan changes*/
					ProductInfo prodInfoObj=new ProductInfo();
					planName = "Shubh Nivesh";
					product_Code=prodInfoObj.getProductCode(planName);
					product_UIN=prodInfoObj.getProductUIN(planName);
					product_cateogory=prodInfoObj.getProductCategory(planName);
					product_type=prodInfoObj.getProductType(planName);
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
				QuatationNumber = CommonForAllProd.getquotationNumber30("35",agentcode ,
						zero + "");



			}
		}
		else
			needAnalysis_flag=0;
		//		ProductHomePageActivity.path.setText("Benefit Illustrator");


		initialiseDate();
		obj = new CommonForAllProd();


		prsObj = new ParseXML();
		svShubhNiveshMain = findViewById(R.id.sv_bi_shubh_nivesh_main);
		btn_bi_shubh_nivesh_proposer_date = findViewById(R.id.btn_bi_shubh_nivesh_proposer_date);
		rb_shubh_nivesh_proposer_same_as_life_assured_yes = findViewById(R.id.rb_shubh_nivesh_proposer_same_as_life_assured_yes);
		rb_shubh_nivesh_proposer_same_as_life_assured_no = findViewById(R.id.rb_shubh_nivesh_proposer_same_as_life_assured_no);

		rb_proposerdetail_personaldetail_backdating_yes = findViewById(R.id.rb_shubh_nivesh_backdating_yes);
		rb_proposerdetail_personaldetail_backdating_no = findViewById(R.id.rb_shubh_nivesh_backdating_no);

		ll_backdating1 = findViewById(R.id.ll_backdating1);
		edt_bi_shubh_nivesh_age = findViewById(R.id.edt_bi_shubh_nivesh_proposer_age);
		list_data = new ArrayList<>();

		// ProductHomePageActivity.tv_QuatationNo2
		// .setText(ProductHomePageActivity.quotation_Number);
		spnr_bi_shubh_nivesh_proposer_title = findViewById(R.id.spnr_bi_shubh_nivesh_proposer_title);
		edt_bi_shubh_nivesh_proposer_first_name = findViewById(R.id.edt_bi_shubh_nivesh_proposer_first_name);
		edt_bi_shubh_nivesh_proposer_middle_name = findViewById(R.id.edt_bi_shubh_nivesh_proposer_middle_name);
		edt_bi_shubh_nivesh_proposer_last_name = findViewById(R.id.edt_bi_shubh_nivesh_proposer_last_name);

		spnr_bi_shubh_nivesh_life_assured_title = findViewById(R.id.spnr_bi_shubh_nivesh_life_assured_title);
		edt_bi_shubh_nivesh_life_assured_age = findViewById(R.id.edt_bi_shubh_nivesh_life_assured_age);
		edt_bi_shubh_nivesh_life_assured_first_name = findViewById(R.id.edt_bi_shubh_nivesh_life_assured_first_name);
		edt_bi_shubh_nivesh_life_assured_middle_name = findViewById(R.id.edt_bi_shubh_nivesh_life_assured_middle_name);
		edt_bi_shubh_nivesh_life_assured_last_name = findViewById(R.id.edt_bi_shubh_nivesh_life_assured_last_name);
		btn_bi_shubh_nivesh_life_assured_date = findViewById(R.id.btn_bi_shubh_nivesh_life_assured_date);
		btn_proposerdetail_personaldetail_backdatingdate = findViewById(R.id.btn_shubh_nivesh_backdatingdate);
		tv_monthly_mode = findViewById(R.id.tv_monthly_mode);
		tr_shubh_nivesh_proposer_detail2 = findViewById(R.id.tr_shubh_nivesh_proposer_detail2);
		tr_shubh_nivesh_proposer_detail1 = findViewById(R.id.tr_shubh_nivesh_proposer_detail1);

		edt_proposerdetail_basicdetail_contact_no = findViewById(R.id.edt_shubh_nivesh_contact_no);
		edt_proposerdetail_basicdetail_Email_id = findViewById(R.id.edt_shubh_nivesh_Email_id);
		edt_proposerdetail_basicdetail_ConfirmEmail_id = findViewById(R.id.edt_shubh_nivesh_ConfirmEmail_id);

		selPTARider = findViewById(R.id.selPTA);
		selADBRider = findViewById(R.id.ADB);
		selATPDBRider = findViewById(R.id.ATPDB);

		adbSA = findViewById(R.id.sumAssuredADB);
		ptaSA = findViewById(R.id.sumAssuredPTA);
		atpdbSA = findViewById(R.id.sumAssuredATPDB);

		// Policy Term
		selBasicTerm = findViewById(R.id.policyterm);

		// adb Term
		selADBTerm = findViewById(R.id.adb_term);

		// Gender
		selGender = findViewById(R.id.selGender);
//        selGender.setClickable(false);
//        selGender.setEnabled(false);

		basicSA = findViewById(R.id.premium_amt);

		// Age
		ageInYears = findViewById(R.id.age);
		ageInYears.setEnabled(false);

		// Premium Frequency
		selPremFreq = findViewById(R.id.premiumfreq);
		// UI elements
		selStaffDisc = findViewById(R.id.cb_staffdisc);
		selBancAssuranceDisc = findViewById(R.id.cb_BankAssuranceDisc);
		selADBTerm = findViewById(R.id.adb_term);

		/*
		  JK Resident added in xml and activity as per 1,Jan,2014 by Akshaya
		  Mirajkar
		 */

		selJkResident = findViewById(R.id.cb_shubh_nivesh_jk);

		// Plan
		selPlan = findViewById(R.id.selplan);

		retVal = new StringBuilder();

		commonMethods.fillSpinnerValue(context, spnr_bi_shubh_nivesh_proposer_title,
				Arrays.asList(getResources().getStringArray(R.array.arr_select_title)));

		commonMethods.fillSpinnerValue(context, spnr_bi_shubh_nivesh_life_assured_title,
				Arrays.asList(getResources().getStringArray(R.array.arr_select_title)));

//		String frmDashboard = ProductHomePageActivity.frmDashboard;
//		if (frmDashboard.equals("FALSE")) {
//			if (getValueFromDatabase()) {
//				// Dialog();
//			}
//		}

		/*
		  Change as per 1,Jan,2014 by Akshaya Mirajkar
		 */
		// "Endowment Assurance","Endowment Assurance with Whole Life Option"
		String[] planList = { "Endowment Option",
				"Endowment with Whole Life Option" };
		ArrayAdapter<String> planAdapter = new ArrayAdapter<>(
				getApplicationContext(), R.layout.spinner_item, planList);
		planAdapter.setDropDownViewResource(R.layout.spinner_item1);
		selPlan.setAdapter(planAdapter);
		planAdapter.notifyDataSetChanged();

		String[] premFreqList = { "Yearly", "Half Yearly", "Quarterly",
				"Monthly", "Single" };
		ArrayAdapter<String> premFreqAdapter = new ArrayAdapter<>(
				getApplicationContext(), R.layout.spinner_item, premFreqList);
		premFreqAdapter.setDropDownViewResource(R.layout.spinner_item1);
		selPremFreq.setAdapter(premFreqAdapter);
		premFreqAdapter.notifyDataSetChanged();

		String[] ageList = new String[43];
		for (int i = 18; i <= 60; i++) {
			ageList[i - 18] = i + "";
		}
		ArrayAdapter<String> ageAdapter = new ArrayAdapter<>(
				getApplicationContext(), R.layout.spinner_item, ageList);
		ageAdapter.setDropDownViewResource(R.layout.spinner_item1);
		ageInYears.setAdapter(ageAdapter);
		ageAdapter.notifyDataSetChanged();

        String[] genderList = {"Male", "Female", "Third Gender"};
		ArrayAdapter<String> genderAdapter = new ArrayAdapter<>(
				getApplicationContext(), R.layout.spinner_item, genderList);
		genderAdapter.setDropDownViewResource(R.layout.spinner_item1);
		selGender.setAdapter(genderAdapter);
		genderAdapter.notifyDataSetChanged();

		showAlert = new AlertDialog.Builder(this);

		String[] policyTermList = new String[26];
		for (int i = 5; i <= 30; i++) {
			policyTermList[i - 5] = i + "";
		}
		ArrayAdapter<String> policyTermAdapter = new ArrayAdapter<>(
				getApplicationContext(), R.layout.spinner_item, policyTermList);
		policyTermAdapter.setDropDownViewResource(R.layout.spinner_item1);
		selBasicTerm.setAdapter(policyTermAdapter);
		policyTermAdapter.notifyDataSetChanged();

		// pta Term
		selPTATerm = findViewById(R.id.selPTATerm);
		String[] ptaTermList = new String[26];
		for (int i = 5; i <= 30; i++) {
			ptaTermList[i - 5] = i + "";
		}
		ArrayAdapter<String> ptaTermAdapter = new ArrayAdapter<>(
				getApplicationContext(), R.layout.spinner_item, ptaTermList);
		ptaTermAdapter.setDropDownViewResource(R.layout.spinner_item1);
		selPTATerm.setAdapter(ptaTermAdapter);
		ptaTermAdapter.notifyDataSetChanged();

		String[] adbTermList = new String[26];
		for (int i = 5; i <= 30; i++) {
			adbTermList[i - 5] = i + "";
		}
		ArrayAdapter<String> adbTermAdapter = new ArrayAdapter<>(
				getApplicationContext(), R.layout.spinner_item, adbTermList);
		adbTermAdapter.setDropDownViewResource(R.layout.spinner_item1);
		selADBTerm.setAdapter(adbTermAdapter);
		adbTermAdapter.notifyDataSetChanged();

		// atpdb Term
		selATPDBTerm = findViewById(R.id.atpdb_term);
		String[] atpdbTermList = new String[26];
		for (int i = 5; i <= 30; i++) {
			atpdbTermList[i - 5] = i + "";
		}
		ArrayAdapter<String> atpdbTermAdapter = new ArrayAdapter<>(
				getApplicationContext(), R.layout.spinner_item, atpdbTermList);
		atpdbTermAdapter.setDropDownViewResource(R.layout.spinner_item1);
		selATPDBTerm.setAdapter(atpdbTermAdapter);
		atpdbTermAdapter.notifyDataSetChanged();

		currencyFormat = new DecimalFormat("##,##,##,###");
		back = findViewById(R.id.back);
		btnSubmit = findViewById(R.id.btnSubmit);

		/*
		  Removed as per IRDA Guidelines to be effective from on 1,Jan,2014 by
		  Akshaya Mirajkar
		 */

		selBancAssuranceDisc.setVisibility(View.GONE);
		selPremFreq.setSelection(4, false);

		/*********************** Item Listener starts here ********************************************/


		// //BAsic Term
		// selBasicTerm.setOnItemSelectedListener(new OnItemSelectedListener() {
		//
		// 
		// public void onItemSelected(AdapterView<?> arg0, View arg1,int pos,
		// long id) {
		// valTermRider();
		// }
		//
		// 
		// public void onNothingSelected(AdapterView<?> arg0) {
		// // TODO Auto-generated method stub
		//
		// }
		// });

		/*
		  Change as per 1,Jan,2014 by Akshaya Mirajkar valTerm() and
		  valTermRider() added On submit event
		 */

		// //PTA term
		// selPTATermo(new OnItemSelectedListener() {
		//
		// 
		// public void onItemSelected(AdapterView<?> arg0, View arg1,int pos,
		// long id) {
		// valTermRider();
		// }
		//
		// 
		// public void onNothingSelected(AdapterView<?> arg0) {
		// // TODO Auto-generated method stub
		//
		// }
		// });
		//
		// //ADB Term
		// selADBTerm.setOnItemSelectedListener(new OnItemSelectedListener() {
		//
		// 
		// public void onItemSelected(AdapterView<?> arg0, View arg1,int pos,
		// long id) {
		// valTermRider();
		// }
		//
		// 
		// public void onNothingSelected(AdapterView<?> arg0) {
		// // TODO Auto-generated method stub
		//
		// }
		// });
		//
		// //ATPDB Term
		// selATPDBTerm.setOnItemSelectedListener(new OnItemSelectedListener() {
		//
		// 
		// public void onItemSelected(AdapterView<?> arg0, View arg1,int pos,
		// long id) {
		// valTermRider();
		// }
		//
		// 
		// public void onNothingSelected(AdapterView<?> arg0) {
		// // TODO Auto-generated method stub
		//
		// }
		// });

//		setBIInputGui();

		if(needAnalysis_flag==1 && !TextUtils.isEmpty(gender))
		{
			selGender.setSelection(genderAdapter.getPosition(gender));
			onClickLADob(btn_bi_shubh_nivesh_life_assured_date);
		}
		proposer_Is_Same_As_Life_Assured = "y";

		edt_bi_shubh_nivesh_life_assured_first_name
				.setOnEditorActionListener(this);
		edt_bi_shubh_nivesh_life_assured_middle_name
				.setOnEditorActionListener(this);
		edt_bi_shubh_nivesh_life_assured_last_name
				.setOnEditorActionListener(this);
		edt_bi_shubh_nivesh_proposer_first_name.setOnEditorActionListener(this);
		edt_bi_shubh_nivesh_proposer_middle_name
				.setOnEditorActionListener(this);
		edt_bi_shubh_nivesh_proposer_last_name.setOnEditorActionListener(this);
		basicSA.setOnEditorActionListener(this);
		ptaSA.setOnEditorActionListener(this);
		adbSA.setOnEditorActionListener(this);
		atpdbSA.setOnEditorActionListener(this);

		setFocusable(spnr_bi_shubh_nivesh_life_assured_title);
		spnr_bi_shubh_nivesh_life_assured_title.requestFocus();

		setSpinnerAndOtherListner();
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

		TableRow tr_staff_disc = findViewById(R.id.tr_shubh_nivesh_staff_disc);
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


	@Override
	protected void onResume() {
		super.onResume();

		boolean flagFocus = true;
		if (!flagFocus) {
			svShubhNiveshMain.requestFocus();
		}

	}

	private void setSpinnerAndOtherListner() {

        cb_kerladisc.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cb_kerladisc.isChecked()) {
                    str_kerla_discount = "Yes";
                    clearFocusable(cb_kerladisc);
                    setFocusable(spnr_bi_shubh_nivesh_life_assured_title);
                    spnr_bi_shubh_nivesh_life_assured_title.requestFocus();
                } else {
                    str_kerla_discount = "No";
                    clearFocusable(cb_kerladisc);
                    setFocusable(spnr_bi_shubh_nivesh_life_assured_title);
                    spnr_bi_shubh_nivesh_life_assured_title.requestFocus();
                }
            }
        });

		rb_shubh_nivesh_proposer_same_as_life_assured_yes
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {


					public void onCheckedChanged(CompoundButton arg0,
												 boolean checked) {
						if (checked) {
							proposer_Is_Same_As_Life_Assured = "y";
							tr_shubh_nivesh_proposer_detail1
									.setVisibility(View.GONE);
							tr_shubh_nivesh_proposer_detail2
									.setVisibility(View.GONE);
						}

					}
				});
		rb_shubh_nivesh_proposer_same_as_life_assured_no
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {


					public void onCheckedChanged(CompoundButton arg0,
												 boolean checked) {
						if (checked) {
							proposer_Is_Same_As_Life_Assured = "n";
							tr_shubh_nivesh_proposer_detail1
									.setVisibility(View.VISIBLE);
							tr_shubh_nivesh_proposer_detail2
									.setVisibility(View.VISIBLE);
						}

					}
				});

		rb_proposerdetail_personaldetail_backdating_yes
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					public void onCheckedChanged(CompoundButton buttonView,
												 boolean isChecked) {
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
						if (isChecked) {

							proposer_Backdating_WishToBackDate_Policy = "n";
							proposer_Backdating_BackDate = "";
							// setDefaultDate();
							ll_backdating1.setVisibility(View.GONE);

							ageInYears
									.setSelection(
											getIndex(ageInYears, lifeAssuredAge),
											false);
							valAge();
							rb_proposerdetail_personaldetail_backdating_yes
									.setFocusable(false);

							clearFocusable(rb_proposerdetail_personaldetail_backdating_no);
							clearFocusable(rb_proposerdetail_personaldetail_backdating_yes);
							setFocusable(selPTARider);
							selPTARider.requestFocus();

						}
					}
				});

		spnr_bi_shubh_nivesh_proposer_title
				.setOnItemSelectedListener(new OnItemSelectedListener() {


					public void onItemSelected(AdapterView<?> arg0, View arg1,
											   int position, long id) {
                        // TODO Auto-generated method stub
                        if (position > 0) {
							proposer_Title = spnr_bi_shubh_nivesh_proposer_title
									.getSelectedItem().toString();
                           /* if (proposer_Title.equalsIgnoreCase("Mr.")) {
                                propsoser_gender = "Male";
                                iv_men.setImageDrawable(getResources().getDrawable(
                                        R.drawable.male_selected));
                                iv_women.setImageDrawable(getResources().getDrawable(
                                        R.drawable.female_nonselected));
							} else if (proposer_Title.equalsIgnoreCase("Ms.")
									|| proposer_Title.equalsIgnoreCase("Mrs.")) {
                                propsoser_gender = "Female";
                                iv_women.setImageDrawable(getResources().getDrawable(
                                        R.drawable.female_selected));
                                iv_men.setImageDrawable(getResources().getDrawable(
                                        R.drawable.male_nonselected));
                            }*/
						}
					}


					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub

					}
				});

		// Age
		ageInYears.setOnItemSelectedListener(new OnItemSelectedListener() {


			public void onItemSelected(AdapterView<?> arg0, View arg1, int pos,
									   long id) {
				valAge();

			}


			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});

		// Plan
		selPlan.setOnItemSelectedListener(new OnItemSelectedListener() {


			public void onItemSelected(AdapterView<?> arg0, View arg1, int pos,
									   long id) {

				valAge();
				clearFocusable(selPlan);
				setFocusable(selBasicTerm);
				selBasicTerm.requestFocus();
			}


			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});

		// Prem Frequency
		selPremFreq.setOnItemSelectedListener(new OnItemSelectedListener() {


			public void onItemSelected(AdapterView<?> arg0, View arg1, int pos,
									   long id) {
				valAge();
				if (selPremFreq.getSelectedItem().toString().equals("Monthly")) {
					tv_monthly_mode.setVisibility(View.VISIBLE);
				} else {
					tv_monthly_mode.setVisibility(View.GONE);
				}
				clearFocusable(selPremFreq);
				setFocusable(basicSA);
				basicSA.requestFocus();

			}


			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});

		selPTATerm.setOnItemSelectedListener(new OnItemSelectedListener() {


			public void onItemSelected(AdapterView<?> arg0, View arg1,
									   int arg2, long arg3) {
				// TODO Auto-generated method stub
				clearFocusable(selPTATerm);
				setFocusable(ptaSA);
				ptaSA.requestFocus();

			}


			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});

		selADBTerm.setOnItemSelectedListener(new OnItemSelectedListener() {


			public void onItemSelected(AdapterView<?> arg0, View arg1,
									   int arg2, long arg3) {
				// TODO Auto-generated method stub
				clearFocusable(selADBTerm);
				setFocusable(adbSA);
				adbSA.requestFocus();

			}


			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});

		selATPDBTerm.setOnItemSelectedListener(new OnItemSelectedListener() {


			public void onItemSelected(AdapterView<?> arg0, View arg1,
									   int arg2, long arg3) {
				// TODO Auto-generated method stub
				clearFocusable(selATPDBTerm);
				setFocusable(atpdbSA);
				atpdbSA.requestFocus();

			}


			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});

		selBasicTerm.setOnItemSelectedListener(new OnItemSelectedListener() {


			public void onItemSelected(AdapterView<?> arg0, View arg1,
									   int position, long id) {
				// TODO Auto-generated method stub
				valMaturityAge();
				selPTATerm.setSelection(
						getIndex(selPTATerm, selBasicTerm.getSelectedItem()
								.toString()), false);
				selADBTerm.setSelection(
						getIndex(selADBTerm, selBasicTerm.getSelectedItem()
								.toString()), false);
				selATPDBTerm.setSelection(
						getIndex(selATPDBTerm, selBasicTerm.getSelectedItem()
								.toString()), false);

				clearFocusable(selBasicTerm);

				if (flagFirstFocus) {
					setFocusable(edt_bi_shubh_nivesh_life_assured_first_name);
					edt_bi_shubh_nivesh_life_assured_first_name.requestFocus();
					flagFirstFocus = false;
				} else {
					setFocusable(selPremFreq);
					selPremFreq.requestFocus();
				}

			}


			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});


		selJkResident.setOnClickListener(new OnClickListener() {


			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (selJkResident.isChecked()) {
					selJkResident.setChecked(true);
				}else{
					selJkResident.setChecked(false);
				}
			}
		});


		// Staff Discount
		selStaffDisc.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				if ((selStaffDisc.isChecked())
						&& selBancAssuranceDisc.isChecked()) {
					selStaffDisc.setChecked(true);
					selBancAssuranceDisc.setChecked(false);

				} else if (selStaffDisc.isChecked()) {
					clearFocusable(spnr_bi_shubh_nivesh_life_assured_title);
					setFocusable(spnr_bi_shubh_nivesh_life_assured_title);
					spnr_bi_shubh_nivesh_life_assured_title.requestFocus();
				}

			}
		});

		// For Bancassurance Discount
		selBancAssuranceDisc.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				if (selStaffDisc.isChecked()
						&& selBancAssuranceDisc.isChecked()) {
					selBancAssuranceDisc.setChecked(true);
					selStaffDisc.setChecked(false);
					// alertDialog.show();
				}
			}
		});

		// PTA Rider
		selPTARider
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {


					public void onCheckedChanged(CompoundButton buttonView,
												 boolean isChecked) {
						if (isChecked) {

							selPTATerm_txt1 = findViewById(R.id.selPTATerm_txt1);
							selPTATerm_txt2 = findViewById(R.id.selPTATerm_txt2);

							selPTATerm_txt1.setVisibility(View.VISIBLE);
							selPTATerm_txt2.setVisibility(View.VISIBLE);
							selPTATerm.setVisibility(View.VISIBLE);

							ptaSA.setVisibility(View.VISIBLE);

							clearFocusable(selPTARider);

							setFocusable(selPTATerm);
							selPTATerm.requestFocus();

						} else {
							selPTATerm_txt1 = findViewById(R.id.selPTATerm_txt1);
							selPTATerm_txt2 = findViewById(R.id.selPTATerm_txt2);
							ptaSA = findViewById(R.id.sumAssuredPTA);
							selPTATerm_txt1.setVisibility(View.GONE);
							selPTATerm_txt2.setVisibility(View.GONE);
							selPTATerm.setVisibility(View.GONE);
							ptaSA.setVisibility(View.GONE);

							clearFocusable(selPTARider);

							setFocusable(selADBRider);
							selADBRider.requestFocus();
						}
					}
				});

		// ADB Rider
		selADBRider
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {


					public void onCheckedChanged(CompoundButton buttonView,
												 boolean isChecked) {
						if (isChecked) {
							adb_txt1 = findViewById(R.id.adb_txt1);
							adb_txt2 = findViewById(R.id.adb_txt2);

							adb_txt1.setVisibility(View.VISIBLE);
							adb_txt2.setVisibility(View.VISIBLE);
							selADBTerm.setVisibility(View.VISIBLE);
							adbSA.setVisibility(View.VISIBLE);
							// setFocusable(selADBTerm);
							// selADBTerm.requestFocus();
							// System.out.println("* 5 *");

							clearFocusable(selADBRider);

							setFocusable(selADBTerm);
							selADBTerm.requestFocus();
						} else {
							adb_txt1 = findViewById(R.id.adb_txt1);
							adb_txt2 = findViewById(R.id.adb_txt2);
							adbSA = findViewById(R.id.sumAssuredADB);
							adb_txt1.setVisibility(View.GONE);
							adb_txt2.setVisibility(View.GONE);
							selADBTerm.setVisibility(View.GONE);
							adbSA.setVisibility(View.GONE);
							// System.out.println("* 6 *");

							clearFocusable(selADBRider);

							setFocusable(selATPDBRider);
							selATPDBRider.requestFocus();
						}
					}
				});

		// ATPDB Rider
		selATPDBRider
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					public void onCheckedChanged(CompoundButton buttonView,
												 boolean isChecked) {
						if (isChecked) {
							atpdb_txt1 = findViewById(R.id.atpdb_txt1);
							atpdb_txt2 = findViewById(R.id.atpdb_txt2);

							atpdb_txt1.setVisibility(View.VISIBLE);
							atpdb_txt2.setVisibility(View.VISIBLE);
							selATPDBTerm.setVisibility(View.VISIBLE);
							atpdbSA.setVisibility(View.VISIBLE);
							clearFocusable(selATPDBRider);
							setFocusable(selATPDBTerm);
							selATPDBTerm.requestFocus();
							// System.out.println("* 5 *");
						} else {
							atpdb_txt1 = findViewById(R.id.atpdb_txt1);
							atpdb_txt2 = findViewById(R.id.atpdb_txt2);
							atpdbSA = findViewById(R.id.sumAssuredATPDB);
							atpdb_txt1.setVisibility(View.GONE);
							atpdb_txt2.setVisibility(View.GONE);
							selATPDBTerm.setVisibility(View.GONE);
							atpdbSA.setVisibility(View.GONE);
							// System.out.println("* 6 *");

							clearFocusable(selATPDBRider);
						}
					}
				});

		spnr_bi_shubh_nivesh_life_assured_title
				.setOnItemSelectedListener(new OnItemSelectedListener() {


					public void onItemSelected(AdapterView<?> arg0, View arg1,
											   int position, long id) {
						// TODO Auto-generated method stub
						if (position == 0){
							lifeAssured_Title = "";
						}else {
							lifeAssured_Title = spnr_bi_shubh_nivesh_life_assured_title
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

							clearFocusable(spnr_bi_shubh_nivesh_life_assured_title);
							setFocusable(edt_bi_shubh_nivesh_life_assured_first_name);

							edt_bi_shubh_nivesh_life_assured_first_name
									.requestFocus();
						}
					}


					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub

					}
				});

		// Go Home Button
		back.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				setResult(RESULT_OK);
				finish();
			}
		});

		// Submit Button
		btnSubmit.setOnClickListener(new OnClickListener() {


			public void onClick(View arg0) {
				// TODO Auto-generated method stu

				inputVal = new StringBuilder();
				retVal = new StringBuilder();
				bussIll = new StringBuilder();

                gender = selGender.getSelectedItem().toString();

				proposer_First_Name = edt_bi_shubh_nivesh_proposer_first_name
						.getText().toString();
				proposer_Middle_Name = edt_bi_shubh_nivesh_proposer_middle_name
						.getText().toString();
				proposer_Last_Name = edt_bi_shubh_nivesh_proposer_last_name
						.getText().toString();

				name_of_proposer = proposer_Title + " " + proposer_First_Name
						+ " " + proposer_Middle_Name + " " + proposer_Last_Name;

				lifeAssured_First_Name = edt_bi_shubh_nivesh_life_assured_first_name
						.getText().toString();
				lifeAssured_Middle_Name = edt_bi_shubh_nivesh_life_assured_middle_name
						.getText().toString();
				lifeAssured_Last_Name = edt_bi_shubh_nivesh_life_assured_last_name
						.getText().toString();

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
				/*
				  Change as per 1,Jan,2014 by Akshaya Mirajkar valTerm() and
				  valTermRider() added On submit event
				 */
				if (valProposerSameAsLifeAssured()
						&& valLifeAssuredProposerDetail() && valDob()
						&& valBasicDetail() && valTerm() && valTermRider()
						&& valSA() && valDoYouBackdate() && valBackdate()
						&& TrueBackdate() && valminPremiumValueAndRider()) {
					addListenerOnSubmit();
					getInput(shubhNiveshBean);

					if (proposer_Is_Same_As_Life_Assured.equalsIgnoreCase("y")) {
						proposer_Title = "";
						proposer_First_Name = "";
						proposer_Middle_Name = "";
						proposer_Last_Name = "";
						name_of_proposer = "";
						proposer_date_of_birth = "";
					}


					if(needAnalysis_flag==0)
					{
						Intent i = new Intent(BI_ShubhNiveshActivity.this, success.class);

						String strInstallPreWithoutTax = prsObj.parseXmlTag(retVal.toString(), "basePremExcludngST");
						strInstallPreWithoutTax = strInstallPreWithoutTax == null ? "0.0" : strInstallPreWithoutTax;

						i.putExtra("op","Basic Premium is Rs. "+ currencyFormat.format(Double.parseDouble(prsObj.parseXmlTag(retVal.toString(), "basicPrem"))));
                        i.putExtra("op1", "Installment Premium without Applicable Taxes is Rs. " + currencyFormat.format(Double.parseDouble(strInstallPreWithoutTax)));
						i.putExtra("op2","Applicable Taxes is Rs. "+ currencyFormat.format(Double.parseDouble(prsObj.parseXmlTag(retVal.toString(), "servTax"))));
						i.putExtra("op3","Installment Premium with Applicable Taxes is Rs. "+currencyFormat.format(Double.parseDouble(prsObj.parseXmlTag(retVal.toString(), "installmntPremWithSerTx"))));
						i.putExtra("op4","Guaranteed Maturity Benefit is Rs. " + currencyFormat.format(Double.parseDouble(prsObj.parseXmlTag(retVal.toString(), "guarMatBenefit"))));
						i.putExtra("op5","Non-guaranteed Maturity Benefit With 4%pa is Rs. " + currencyFormat.format(Double.parseDouble(prsObj.parseXmlTag(retVal.toString(), "nonGuarMatBenefit4"))));
						i.putExtra("op6","Non-guaranteed Maturity Benefit With 8%pa is Rs. " + currencyFormat.format(Double.parseDouble(prsObj.parseXmlTag(retVal.toString(), "nonGuarMatBenefit8"))));

						if(selADBRider.isChecked())
						{
							i.putExtra("op8","Accidental Death Benefit Rider Premium is Rs. "+ currencyFormat.format(Double.parseDouble(prsObj.parseXmlTag(retVal.toString(), "adbRiderPrem"))));
						}

						if(selPTARider.isChecked())
						{
							i.putExtra("op7","Preferred Term Assurance Rider Premium is Rs. "+ currencyFormat.format(Double.parseDouble(prsObj.parseXmlTag(retVal.toString(), "ptaRiderPrem"))));
						}

						if(selATPDBRider.isChecked())
						{
							i.putExtra("op9","Accidental Total & Permanent Disability Benefit Rider Premium is Rs. "+ currencyFormat.format(Double.parseDouble(prsObj.parseXmlTag(retVal.toString(), "atpdbRiderPrem"))));
						}
						startActivity(i);
					}
					else
						Dialog();
//					}
				}

			}
		});

	}


	private void Date() {
		Calendar c = Calendar.getInstance();
		System.out.println("Current time => " + c.getTime());

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	}

	private void initialiseDate() {
		Calendar calender = Calendar.getInstance();
		mYear = calender.get(Calendar.YEAR);
		mMonth = calender.get(Calendar.MONTH);
		mDay = calender.get(Calendar.DAY_OF_MONTH);

	}



	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		/*parivartan changes*/
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
					else if (latestImage.equalsIgnoreCase("thirdParty")) {
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
		}
		else if (requestCode == 3) {
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
		/*end*/
	}

	public void onClickDob(View v) {
		// setDefaultDate();
		DIALOG_ID = 4;
		showDialog(DATE_DIALOG_ID);
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
		} else {
			age = tYear - mYear - 1;
		}

		String final_age = age + " yrs";

		if (final_age.contains("-")) {
			commonMethods.dialogWarning(context,"Please fill Valid Date", true);
		} else {
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
						commonMethods.BICommonDialog(context,"Please fill Valid Birth Date");
					} else {
						if (18 <= age) {

							btn_bi_shubh_nivesh_proposer_date.setText(date);
							edt_bi_shubh_nivesh_age.setText(final_age);
							proposer_date_of_birth = getDate1(date + "");
							// ageInYears
							// .setSelection(getIndex(ageInYears, final_age));
							// valAge();
						} else {
							commonMethods.BICommonDialog(context,"Minimum age should be 18 yrs for proposer");
							btn_bi_shubh_nivesh_proposer_date
									.setText("Select Date");
							edt_bi_shubh_nivesh_age.setText("");
							proposer_date_of_birth = "";
						}
					}
					break;

				case 5:

					final_age = Integer.toString(age);
					if (final_age.contains("-")) {
						commonMethods.BICommonDialog(context,"Please fill Valid Birth Date");
					} else {
						int maxLimit;
						if (selPlan.getSelectedItem().toString()
								.equals("Endowment Option")) {
							if (selPremFreq.getSelectedItem().toString()
									.equals("Single"))
								maxLimit = 60;
							else
                                maxLimit = 55;
						} else
							maxLimit = 50;

						if (18 <= age && age <= maxLimit) {
							lifeAssuredAge = final_age;
							btn_bi_shubh_nivesh_life_assured_date.setText(date);
							edt_bi_shubh_nivesh_life_assured_age.setText(final_age);

							ageInYears.setSelection(
									getIndex(ageInYears, final_age), false);
							valAge();
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

							else {

								clearFocusable(btn_bi_shubh_nivesh_life_assured_date);

								setFocusable(edt_proposerdetail_basicdetail_contact_no);
								edt_proposerdetail_basicdetail_contact_no
										.requestFocus();
								/*
								 * setFocusable(selPlan); selPlan.requestFocus();
								 */
							}

						} else {
							commonMethods.BICommonDialog(context,"Minimum Age should be 18 yrs and Maximum Age should be "
									+ maxLimit + " yrs For LifeAssured");
							btn_bi_shubh_nivesh_life_assured_date
									.setText("Select Date");
							edt_bi_shubh_nivesh_life_assured_age.setText("");
							lifeAssured_date_of_birth = "";

							clearFocusable(btn_bi_shubh_nivesh_life_assured_date);
							setFocusable(btn_bi_shubh_nivesh_life_assured_date);
							btn_bi_shubh_nivesh_life_assured_date.requestFocus();
						}
					}
					break;

				case 6:
					if (age >= 0) {
						proposer_Backdating_BackDate = date + "";
						btn_proposerdetail_personaldetail_backdatingdate
								.setText(proposer_Backdating_BackDate);
						clearFocusable(btn_proposerdetail_personaldetail_backdatingdate);

						setFocusable(selPTARider);
						selPTARider.requestFocus();

					} else {
						commonMethods.dialogWarning(context,"Please Select Valid BackDating Date", true);
						btn_proposerdetail_personaldetail_backdatingdate
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

			int Proposerage = calculateMyAge(mYear, Integer.parseInt(mont),
					Integer.parseInt(day));
			String str_final_Age = Integer.toString(Proposerage);
            if (Integer.parseInt(str_final_Age) < 18) {
                btn_proposerdetail_personaldetail_backdatingdate
                        .setText("Select Date");
                proposer_Backdating_BackDate = "";
                commonMethods.dialogWarning(context,"Please select valid date", true);

            } else {
			ageInYears.setSelection(getIndex(ageInYears, str_final_Age), false);
			valAge();
            }
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

	private void windowmessagesgin() {

		d = new Dialog(BI_ShubhNiveshActivity.this);
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
				Intent intent = new Intent(BI_ShubhNiveshActivity.this, CaptureSignature.class);
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


	private int getIndex(Spinner s1, String value) {

		int index = 0;

		for (int i = 0; i < s1.getCount(); i++) {
			if (s1.getItemAtPosition(i).equals(value)) {
				index = i;
			}
		}
		return index;
	}

	// Dialog USed For Displaying BI

	private void Dialog() {
		final Dialog d = new Dialog(this);
		d.requestWindowFeature(Window.FEATURE_NO_TITLE);
		d.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.BLACK));

		d.setContentView(R.layout.layout_shubh_nivesh_bi_grid);

		TextView tv_proposername = d
				.findViewById(R.id.tv_proposername);
		TextView tv_proposal_number = d
				.findViewById(R.id.tv_proposal_number);

		TextView tv_bi_shubh_nivesh_life_assured_name = d
				.findViewById(R.id.tv_bi_shubh_nivesh_life_assured_name);
        TextView tv_bi_shubh_nivesh_life_assured_name2 = (TextView) d
                .findViewById(R.id.tv_bi_shubh_nivesh_life_assured_name2);
        TextView tv_bi_shubh_nivesh_life_assured_age2 = (TextView) d
                .findViewById(R.id.tv_bi_shubh_nivesh_life_assured_age2);
		TextView tv_bi_shubh_nivesh_life_assured_age = d
				.findViewById(R.id.tv_bi_shubh_nivesh_life_assured_age);
        TextView tv_bi_shubh_nivesh_life_assured_gender2 = (TextView) d
                .findViewById(R.id.tv_bi_shubh_nivesh_life_assured_gender2);
		TextView tv_bi_shubh_nivesh_life_assured_gender = d
				.findViewById(R.id.tv_bi_shubh_nivesh_life_assured_gender);
		TextView tv_bi_shubh_nivesh_life_assured_premium_frequency = d
				.findViewById(R.id.tv_bi_shubh_nivesh_life_assured_premium_frequency);

		TextView tv_bi_shubh_nivesh_plan_proposed = d
				.findViewById(R.id.tv_bi_shubh_nivesh_plan_proposed);
		TextView tv_bi_shubh_nivesh_term = d
				.findViewById(R.id.tv_bi_shubh_nivesh_term);
		TextView tv_bi_shubh_nivesh_premium_paying_term = d
				.findViewById(R.id.tv_bi_shubh_nivesh_premium_paying_term);
		TextView tv_bi_shubh_nivesh_sum_assured = d
				.findViewById(R.id.tv_bi_shubh_nivesh_sum_assured);
        TextView tv_bi_shubh_nivesh_sum_assured2 = (TextView) d
                .findViewById(R.id.tv_bi_shubh_nivesh_sum_assured2);
		TextView tv_bi_shubh_nivesh_yearly_premium = d

				.findViewById(R.id.tv_bi_shubh_nivesh_yearly_premium);

		TextView tv_basic_premium_tag = d
				.findViewById(R.id.tv_basic_premium_tag);
        TextView tv_subh_nivesh_ppo = (TextView) d.findViewById(R.id.tv_subh_nivesh_ppo);
        TextView tv_premium_tag = (TextView) d.findViewById(R.id.tv_premium_tag);

		TextView tv_bi_shubh_nivesh_basic_premium = d
				.findViewById(R.id.tv_bi_shubh_nivesh_basic_premium);
		TextView tv_bi_shubh_nivesh_service_tax = d
				.findViewById(R.id.tv_bi_shubh_nivesh_service_tax);
		TextView tv_bi_shubh_nivesh_yearly_premium_with_tax = d
				.findViewById(R.id.tv_bi_shubh_nivesh_yearly_premium_with_tax);



		//Second year tables

		final TextView tv_premium_install_rider_type1 = d
				.findViewById(R.id.tv_premium_install_rider_type1);
		final TextView tv_mandatory_bi_shubh_nivesh_yearly_premium_with_tax1 = d
				.findViewById(R.id.tv_mandatory_bi_shubh_nivesh_yearly_premium_with_tax1);

		// First year policy
		TextView tv_bi_shubh_nivesh_basic_premium_first_year = d
				.findViewById(R.id.tv_bi_shubh_nivesh_basic_premium_first_year);

		TextView tv_bi_shubh_nivesh_service_tax_first_year = d
				.findViewById(R.id.tv_bi_shubh_nivesh_service_tax_first_year);

		TextView tv_bi_shubh_nivesh_yearly_premium_with_tax_first_year = d
				.findViewById(R.id.tv_bi_shubh_nivesh_yearly_premium_with_tax_first_year);

//        TextView tv_bi_shubh_nivesh_swachh_bharat_tax_first_year = d
//                .findViewById(R.id.tv_bi_shubh_nivesh_swachh_bharat_tax_first_year);
//
//        TextView tv_bi_shubh_nivesh_krishi_kalyan_tax_first_year = d
//                .findViewById(R.id.tv_bi_shubh_nivesh_krishi_kalyan_tax_first_year);
//
//        TextView tv_bi_shubh_nivesh_total_service_tax_first_year = d
//                .findViewById(R.id.tv_bi_shubh_nivesh_total_service_tax_first_year);
//
//        // Seconf year policy onwards
//        TextView tv_bi_shubh_nivesh_second_year_heading = d
//                .findViewById(R.id.tv_bi_shubh_nivesh_second_year_heading);

		TextView tv_bi_shubh_nivesh_basic_premium_second_year = d
				.findViewById(R.id.tv_bi_shubh_nivesh_basic_premium_second_year);
		TextView tv_bi_shubh_nivesh_service_tax_second_year = d
				.findViewById(R.id.tv_bi_shubh_nivesh_service_tax_second_year);
		TextView tv_bi_shubh_nivesh_yearly_premium_with_tax_second_year = d
				.findViewById(R.id.tv_bi_shubh_nivesh_yearly_premium_with_tax_second_year);

//        TextView tv_bi_shubh_nivesh_swachh_bharat_tax_second_year = d
//                .findViewById(R.id.tv_bi_shubh_nivesh_swachh_bharat_tax_second_year);
//        TextView tv_bi_shubh_nivesh_krishi_kalyan_tax_second_year = d
//                .findViewById(R.id.tv_bi_shubh_nivesh_krishi_kalyan_tax_second_year);
//        TextView tv_bi_shubh_nivesh_total_service_tax_second_year = d
//                .findViewById(R.id.tv_bi_shubh_nivesh_total_service_tax_second_year);

		//TableRow tr_second_year = (TableRow) d .findViewById(R.id.tr_second_year);

        TextView tv_bi_subh_nivesh_yearly_premium2rider = (TextView) d
                .findViewById(R.id.tv_bi_subh_nivesh_yearly_premium2rider);

        TextView tv_bi_subh_nivesh_yearly_premium2 = (TextView) d
                .findViewById(R.id.tv_bi_subh_nivesh_yearly_premium2);

        TextView tv_bi_subh_nivesh_yearly_premium = (TextView) d
                .findViewById(R.id.tv_bi_subh_nivesh_yearly_premium);

        TableRow tr_second_year = (TableRow) d
                .findViewById(R.id.tr_second_year);

        TextView tv_backdatingint = (TextView) d
				.findViewById(R.id.tv_backdatingint);

		TableRow tr_shubh_nivesh_surrender_value = d
				.findViewById(R.id.tr_shubh_nivesh_surrender_value);
		final TextView edt_proposer_name = d
				.findViewById(R.id.edt_ProposalName);
		GridView gv_userinfo = d.findViewById(R.id.gv_userinfo);
		final EditText edt_Policyholderplace = d
				.findViewById(R.id.edt_Policyholderplace);

        final TextView tv_subh_nivesh_distribution_channel = (TextView) d
                .findViewById(R.id.tv_subh_nivesh_distribution_channel);
        tv_subh_nivesh_distribution_channel.setText(userType);

		final TextView tv_premium_type = d
				.findViewById(R.id.tv_premium_type);

		final TextView tv_premium_type1 = d
				.findViewById(R.id.tv_premium_type1);
		final TextView tv_premium_type2 = d
				.findViewById(R.id.tv_premium_type2);
		final TextView tv_premium_type3 = d
				.findViewById(R.id.tv_premium_type3);
		LinearLayout ll_basic_rider = d
				.findViewById(R.id.ll_basic_rider);
		LinearLayout ll_basic_adb_rider = d
				.findViewById(R.id.ll_basic_adb_rider);
		LinearLayout ll_basic_atpdb_rider = d
				.findViewById(R.id.ll_basic_atpdb_rider);
		LinearLayout ll_basic_pta_rider = d
				.findViewById(R.id.ll_basic_pta_rider);
		TextView tv_premium_type_rider = d
				.findViewById(R.id.tv_premium_type_rider);

		TextView tv_bi_shubh_nivesh_premium_paying_term_pta = d
				.findViewById(R.id.tv_bi_shubh_nivesh_premium_paying_term_pta);
        TextView tv_bi_shubh_nivesh_rider_premium_paying_term_pta = (TextView) d
                .findViewById(R.id.tv_bi_shubh_nivesh_rider_premium_paying_term_pta);
		TextView tv_bi_shubh_nivesh_sum_assured_pta = d
				.findViewById(R.id.tv_bi_shubh_nivesh_sum_assured_pta);
        TextView tv_bi_shubh_nivesh_yearly_premium_term_assurance_rider = (TextView) d
                .findViewById(R.id.tv_bi_shubh_nivesh_yearly_premium_term_assurance_rider);

		TextView tv_bi_shubh_nivesh_premium_paying_term_adb = d
				.findViewById(R.id.tv_bi_shubh_nivesh_premium_paying_term_adb);
        TextView tv_bi_shubh_nivesh_rider_premium_paying_term_adb = (TextView) d
                .findViewById(R.id.tv_bi_shubh_nivesh_rider_premium_paying_term_adb);
		TextView tv_bi_shubh_nivesh_sum_assured_adb = d
				.findViewById(R.id.tv_bi_shubh_nivesh_sum_assured_adb);
		TextView tv_bi_shubh_nivesh_yearly_premium_adb = d
				.findViewById(R.id.tv_bi_shubh_nivesh_yearly_premium_adb);

		TextView tv_bi_shubh_nivesh_premium_paying_term_adptb = d
				.findViewById(R.id.tv_bi_shubh_nivesh_premium_paying_term_adptb);
        TextView tv_bi_shubh_nivesh_rider_premium_paying_term_adptb = (TextView) d
                .findViewById(R.id.tv_bi_shubh_nivesh_rider_premium_paying_term_adptb);
		TextView tv_bi_shubh_nivesh_sum_assured_adptb = d
				.findViewById(R.id.tv_bi_shubh_nivesh_sum_assured_adptb);
		TextView tv_bi_shubh_nivesh_yearly_premium_adptb = d
				.findViewById(R.id.tv_bi_shubh_nivesh_yearly_premium_adptb);

		TextView tv_bi_is_Staff = d
				.findViewById(R.id.tv_bi_is_Staff);
        TextView tv_bi_subh_nivesh_state = (TextView) d.findViewById(R.id.tv_bi_subh_nivesh_state);
        TextView tv_subh_nivesh_rate_of_appl_taxes = (TextView) d.findViewById(R.id.tv_subh_nivesh_rate_of_appl_taxes);

		TextView tv_bi_is_jk = d
				.findViewById(R.id.tv_bi_is_jk);

        TextView tv_bi_shubh_nivesh_basic_service_tax_first_year = (TextView) d
                .findViewById(R.id.tv_bi_shubh_nivesh_basic_service_tax_first_year);
        TextView tv_bi_shubh_nivesh_swachh_bharat_cess_first_year = (TextView) d
                .findViewById(R.id.tv_bi_shubh_nivesh_swachh_bharat_cess_first_year);
        TextView tv_bi_shubh_nivesh_krishi_kalyan_cess_first_year = (TextView) d
                .findViewById(R.id.tv_bi_shubh_nivesh_krishi_kalyan_cess_first_year);

        TextView tv_bi_shubh_nivesh_basic_service_tax_second_year = (TextView) d
                .findViewById(R.id.tv_bi_shubh_nivesh_basic_service_tax_second_year);
        TextView tv_bi_shubh_nivesh_swachh_bharat_cess_second_year = (TextView) d
                .findViewById(R.id.tv_bi_shubh_nivesh_swachh_bharat_cess_second_year);
        TextView tv_bi_shubh_nivesh_krishi_kalyan_cess_second_year = (TextView) d
                .findViewById(R.id.tv_bi_shubh_nivesh_krishi_kalyan_cess_second_year);

        TextView tv_uin_shubh_nivesh = (TextView) d
                .findViewById(R.id.tv_uin_shubh_nivesh);

		final CheckBox cb_statement = d
				.findViewById(R.id.cb_statement);
		cb_statement.setChecked(false);

		/*parivartan changes*/
		imageButtonShubhNiveshProposerPhotograph = d.findViewById(R.id.imageButtonShubhNiveshProposerPhotograph);
		imageButtonShubhNiveshProposerPhotograph.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {

				Check = "Photo";
				commonMethods.windowmessage(context,"_cust1Photo.jpg");
			}
		});
		/*end*/

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

		LinearLayout ll_endowment = d
				.findViewById(R.id.ll_endowment);
		LinearLayout ll_endowment_with_option = d
				.findViewById(R.id.ll_endowment_with_option);

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

//		getValueFromDatabase();

//        premiumPaymentMode = prsObj.parseXmlTag(input, "premFreq");
//        if (premiumPaymentMode.equalsIgnoreCase("Single")) {
//            tv_subh_nivesh_ppo.setText("Single");
//            tv_premium_tag.setText("Single");
//        } else {
//            tv_subh_nivesh_ppo.setText("Regular");
//            tv_premium_tag.setText("Annualized premium");
//        }

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
			edt_proposer_name.setText("I, "+name_of_life_assured+ ", having received the information with respect to the above, have understood the above statement before entering into the contract.");
			edt_proposer_name_need_analysis.setText("I, "+name_of_life_assured+", have undergone the Need Analysis after having reviewed the SBI Life Product options, I have opted for SBI LIFE - Shubh Nivesh.");

			// tv_life_assured_name.setText(name_of_life_assured);
			tv_proposername.setText(name_of_life_assured);
		} else if (proposer_Is_Same_As_Life_Assured.equalsIgnoreCase("n")) {
			edt_proposer_name.setText("I, "+name_of_proposer+ ", having received the information with respect to the above, have understood the above statement before entering into the contract.");
			edt_proposer_name_need_analysis.setText("I, "+name_of_proposer+", have undergone the Need Analysis after having reviewed the SBI Life Product options, I have opted for SBI LIFE - Shubh Nivesh.");

			tv_proposername.setText(name_of_proposer);
		}
		tv_proposal_number.setText(QuatationNumber);

		if (!date2.equals("")) {
			btn_PolicyholderDate.setText(getDate(date2));
		} else {
			date2 = getDate1(getCurrentDate());
			btn_PolicyholderDate.setText(getCurrentDate());
		}
		TextView textviewAGentStatement = d.findViewById(R.id.textviewAGentStatement);
		textviewAGentStatement.setText(commonMethods.getAgentDeclaration(context));
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
                                && cb_statement_need_analysis.isChecked()&& checkboxAgentStatement.isChecked()) {
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
                                && cb_statement_need_analysis.isChecked()  && checkboxAgentStatement.isChecked()) {
							latestImage = "proposer";
							// windowmessagesgin();
							//windowmessageProposersgin();
							/*parivartan changes*/
							commonMethods.windowmessageProposersgin(context,
									NeedAnalysisActivity.URN_NO + "_cust1sign");
							/*end*/
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

		/*parivartan changes*/
		linearlayoutThirdpartySignature = d.findViewById(R.id.linearlayoutThirdpartySignature);
		Ibtn_signatureofThirdParty = d.findViewById(R.id.Ibtn_signatureofThirdParty);

		if (thirdPartySign != null && !thirdPartySign.equals("")) {
			byte[] signByteArray = Base64.decode(thirdPartySign, 0);
			Bitmap bitmap = BitmapFactory.decodeByteArray(signByteArray, 0,
					signByteArray.length);
			Ibtn_signatureofThirdParty.setImageBitmap(bitmap);
		}

		final RadioButton radioButtonDepositPaymentYes = d.findViewById(R.id.radioButtonDepositPaymentYes);
		final RadioButton radioButtonDepositPaymentNo = d.findViewById(R.id.radioButtonDepositPaymentNo);


		radioButtonDepositPaymentNo.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
										 boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					linearlayoutThirdpartySignature.setVisibility(View.VISIBLE);
				}
				else
				{
					linearlayoutThirdpartySignature.setVisibility(View.GONE);

					String thirdyPartySignName = NeedAnalysisActivity.URN_NO +"_thirdParty.png";

					File thirdyPartySignFile = mStorageUtils.createFileToAppSpecificDir(context,
							thirdyPartySignName);

					if(thirdyPartySignFile.exists())
					{
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
							commonMethods.windowmessageProposersgin(context, NeedAnalysisActivity.URN_NO+"_thirdParty");
						} else {
							commonMethods.dialogWarning(context, "Please Tick on I Agree Clause ", true);
							setFocusable(cb_statement);
							cb_statement.requestFocus();
						}

					}
				});

		//////

		linearlayoutAppointeeSignature = d.findViewById(R.id.linearlayoutAppointeeSignature);
		Ibtn_signatureofAppointee = d.findViewById(R.id.Ibtn_signatureofAppointee);

		if (appointeeSign != null && !appointeeSign.equals("")) {
			byte[] signByteArray = Base64.decode(appointeeSign, 0);
			Bitmap bitmap = BitmapFactory.decodeByteArray(signByteArray, 0,
					signByteArray.length);
			Ibtn_signatureofAppointee.setImageBitmap(bitmap);
		}

		final RadioButton radioButtonAppointeeYes = d.findViewById(R.id.radioButtonAppointeeYes);
		final RadioButton radioButtonAppointeeNo = d.findViewById(R.id.radioButtonAppointeeNo);

		radioButtonAppointeeYes.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
										 boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					linearlayoutAppointeeSignature.setVisibility(View.VISIBLE);
				}
				else
				{
					linearlayoutAppointeeSignature.setVisibility(View.GONE);
					String appointeeSignName =  NeedAnalysisActivity.URN_NO  +"_appointee.png";
					File appointeeSignFile = mStorageUtils.createFileToAppSpecificDir(context,
							appointeeSignName);
					if(appointeeSignFile.exists())
					{
						appointeeSignFile.delete();
					}

					appointeeSign = "";
					Ibtn_signatureofAppointee.setImageBitmap(null);
				}
			}
		});



		if(photoBitmap!=null)
		{
			imageButtonShubhNiveshProposerPhotograph.setImageBitmap(photoBitmap);
		}

		Ibtn_signatureofAppointee
				.setOnClickListener(new OnClickListener() {

					public void onClick(View view) {

						if (cb_statement.isChecked()
								&& cb_statement_need_analysis.isChecked()) {
							latestImage = "Appointee";
							commonMethods.windowmessageProposersgin(context, NeedAnalysisActivity.URN_NO+"_appointee");
						} else {
							commonMethods.dialogWarning(context, "Please Tick on I Agree Clause ", true);
							setFocusable(cb_statement);
							cb_statement.requestFocus();
						}

					}
				});

		final RadioButton radioButtonTrasactionModeManual = d.findViewById(R.id.radioButtonTrasactionModeManual);
		final RadioButton radioButtonTrasactionModeParivartan = d.findViewById(R.id.radioButtonTrasactionModeParivartan);
		final LinearLayout linearlayoutTrasactionModeParivartan = d.findViewById(R.id.linearlayoutTrasactionModeParivartan);

		radioButtonTrasactionModeParivartan.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
										 boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					linearlayoutTrasactionModeParivartan.setVisibility(View.VISIBLE);
				}
				else
				{
					linearlayoutTrasactionModeParivartan.setVisibility(View.GONE);

					String appointeeSignName =  NeedAnalysisActivity.URN_NO  +"_appointee.png";
					File appointeeSignFile = mStorageUtils.createFileToAppSpecificDir(context,
							appointeeSignName);
					if(appointeeSignFile.exists())
					{
						appointeeSignFile.delete();
					}

					appointeeSign = "";
					Ibtn_signatureofAppointee.setImageBitmap(null);

					String thirdyPartySignName = NeedAnalysisActivity.URN_NO +"_thirdParty.png";

					File thirdyPartySignFile = mStorageUtils.createFileToAppSpecificDir(context,
							thirdyPartySignName);

					if(thirdyPartySignFile.exists())
					{
						thirdyPartySignFile.delete();
					}

					thirdPartySign = "";
					Ibtn_signatureofThirdParty.setImageBitmap(null);


					String customerPhotoName = NeedAnalysisActivity.URN_NO +"_cust1Photo.jpg";
					File customerPhotoFile = mStorageUtils.createFileToAppSpecificDir(context,
							customerPhotoName);
					if(customerPhotoFile.exists())
					{
						customerPhotoFile.delete();
					}

					photoBitmap = null;
					imageButtonShubhNiveshProposerPhotograph.setImageDrawable
							(getResources().getDrawable(R.drawable.focus_imagebutton_photo));

					RadioGroup radioGroupDepositPayment = d.findViewById(R.id.radioGroupDepositPayment);
					radioGroupDepositPayment.clearCheck();

					RadioGroup radioGroupAppointee = d.findViewById(R.id.radioGroupAppointee);
					radioGroupAppointee.clearCheck();
				}
			}
		});
		/*end*/

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
						&& (((photoBitmap!=null
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
					String	productCode;

					if(plan.equals("Endowment Option"))
						productCode="SSNEND";
					else
						productCode="SSNWHLF";

					na_cbi_bean=new NA_CBI_bean(QuatationNumber, agentcode, "", userType, "",
							lifeAssured_Title, lifeAssured_First_Name, lifeAssured_Middle_Name,
							lifeAssured_Last_Name, planName,obj.getRound(sum_assured),
							obj.getRound(basicplustax)
							, emailId, mobileNo, agentEmail, agentMobile, na_input, na_output,
							premium_paying_frequency,Integer.parseInt(policy_term),0,productCode,
							getDate(lifeAssured_date_of_birth),"",inputVal
							.toString(), retVal.toString().replace(
							bussIll.toString(), ""));

					name_of_person = name_of_life_assured;

					if (radioButtonTrasactionModeParivartan.isChecked()) {
						mode = "Parivartan";
					} else if(radioButtonTrasactionModeManual.isChecked()){
						mode = "Manual";
					}
					dbHelper.AddNeedAnalysisDashboardDetails(new ProductBIBean("",  QuatationNumber, planName, getCurrentDate(), mobileNo, getCurrentDate(),
							dbHelper.GetUserCode(), emailId,"","",
							agentcode, "", userType, "",
							lifeAssured_Title, lifeAssured_First_Name, lifeAssured_Middle_Name, lifeAssured_Last_Name, obj.getRound(sum_assured), obj.getRound(basicplustax),
							agentEmail, agentMobile, na_input, na_output,
							premium_paying_frequency,Integer.parseInt(policy_term),0,productCode,
							getDate(lifeAssured_date_of_birth),"","",mode,inputVal
							.toString(), retVal.toString().replace(
							bussIll.toString(), "")));

					CreateShubhNiveshPlusBIPdf();


					NABIObj.serviceHit(BI_ShubhNiveshActivity.this,na_cbi_bean,newFile, needAnalysispath.getPath(),
							mypath.getPath(),name_of_person,QuatationNumber,mode);
					d.dismiss();

				}else {

					if (proposer_sign.equals("")&& !bankUserType
							.equalsIgnoreCase("Y")) {
						commonMethods.dialogWarning(context,"Please Make Signature for Proposer ", true);
						setFocusable(Ibtn_signatureofPolicyHolders);
						Ibtn_signatureofPolicyHolders.requestFocus();
					} else if (place2.equals("")) {
						commonMethods.dialogWarning(context,"Please Fill Place Detail", true);
						setFocusable(edt_Policyholderplace);
						edt_Policyholderplace.requestFocus();

					} else if (agent_sign.equals("")&& !bankUserType
							.equalsIgnoreCase("Y")) {
						commonMethods.dialogWarning(context,"Please Make Signature for Sales Representative",
								true);
						setFocusable(Ibtn_signatureofMarketing);
						Ibtn_signatureofMarketing.requestFocus();
					}

					else if (!cb_statement.isChecked()) {
						commonMethods.dialogWarning(context, "Please Tick on I Agree Clause ", true);
						setFocusable(cb_statement);
						cb_statement.requestFocus();
					}
					else if (!checkboxAgentStatement.isChecked()) {
						commonMethods.dialogWarning(context, "Please Tick on I Agree Clause ", true);
						commonMethods.setFocusable(checkboxAgentStatement);
						checkboxAgentStatement.requestFocus();
					}
					else if (!radioButtonTrasactionModeParivartan.isChecked() && !radioButtonTrasactionModeManual.isChecked()) {
						commonMethods.dialogWarning(context,"Please Select Transaction Mode", true);
						setFocusable(linearlayoutTrasactionModeParivartan);
						linearlayoutTrasactionModeParivartan.requestFocus();
					}
					else if (photoBitmap == null) {
						commonMethods.dialogWarning(context, "Please Capture the Photo", true);
						setFocusable(imageButtonShubhNiveshProposerPhotograph);
						imageButtonShubhNiveshProposerPhotograph.requestFocus();
					}
					else if (!radioButtonDepositPaymentYes.isChecked() && !radioButtonDepositPaymentNo.isChecked()) {
						commonMethods.dialogWarning(context, "Please Select Third Party Payment", true);
						setFocusable(linearlayoutThirdpartySignature);
						linearlayoutThirdpartySignature.requestFocus();
					} else if (radioButtonDepositPaymentNo.isChecked() && thirdPartySign.equals("")) {
						commonMethods.dialogWarning(context, "Please Make Signature for Third party ", true);
						setFocusable(Ibtn_signatureofThirdParty);
						Ibtn_signatureofThirdParty.requestFocus();
					}
					else if (!radioButtonAppointeeYes.isChecked() && !radioButtonAppointeeNo.isChecked()) {
						commonMethods.dialogWarning(context, "Please Select Appointee Payment", true);
						setFocusable(linearlayoutAppointeeSignature);
						linearlayoutAppointeeSignature.requestFocus();
					} else if (radioButtonAppointeeYes.isChecked() && appointeeSign.equals("")) {
						commonMethods.dialogWarning(context, "Please Make Signature for Appointee ", true);
						setFocusable(Ibtn_signatureofAppointee);
						Ibtn_signatureofAppointee.requestFocus();
					}
					/*end*/
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
        tv_uin_shubh_nivesh
                .setText("Benefit Illustration (BI) : SBI Life - Shubh Nivesh  (UIN :  111N055V04) | An Individual, Non-linked, Participating, Life Insurance Savings Product");

		tv_bi_shubh_nivesh_life_assured_name.setText(name_of_life_assured);
        tv_bi_shubh_nivesh_life_assured_name2.setText(name_of_life_assured);
		age_entry = prsObj.parseXmlTag(input, "age");
        tv_bi_shubh_nivesh_life_assured_age.setText(age_entry + "");
        tv_bi_shubh_nivesh_life_assured_age2.setText(age_entry);

		gender = prsObj.parseXmlTag(input, "gender");
		tv_bi_shubh_nivesh_life_assured_gender.setText(gender);
        tv_bi_shubh_nivesh_life_assured_gender2.setText(gender);

		premium_paying_frequency = prsObj.parseXmlTag(input, "premFreq");
		tv_bi_shubh_nivesh_life_assured_premium_frequency
				.setText(premium_paying_frequency);

		// if (premium_paying_frequency.equals("Single")) {
		// tr_shubh_nivesh_surrender_value.setVisibility(View.GONE);
		// }

        staffdiscount = prsObj.parseXmlTag(input, "isStaff");

		if (staffdiscount.equalsIgnoreCase("true")) {
            tv_bi_is_Staff.setText("Yes");
		} else {
            tv_bi_is_Staff.setText("No");
        }

        str_kerla_discount = prsObj.parseXmlTag(input, "str_kerla_discount");
        if (str_kerla_discount.equalsIgnoreCase("Yes")) {
            tv_bi_subh_nivesh_state.setText("Kerala");
            if (premium_paying_frequency.equals("Single")) {
                tv_subh_nivesh_rate_of_appl_taxes.setText("4.75%");
            } else {
                tv_subh_nivesh_rate_of_appl_taxes.setText(" 4.75% in the 1st policy year and 2.375% from 2nd policy year onwards");
            }
        } else {
            tv_bi_subh_nivesh_state.setText("Non Kerala");
            if (premium_paying_frequency.equals("Single")) {
                tv_subh_nivesh_rate_of_appl_taxes.setText("4.50%");
            } else {
                tv_subh_nivesh_rate_of_appl_taxes.setText("4.5% in the 1st policy year and 2.25% from 2nd policy year onwards");
            }
		}


		String isJkResident = prsObj.parseXmlTag(input, "isJKResident");

		if (isJkResident.equalsIgnoreCase("true")) {
			tv_bi_is_jk.setText("Yes");
		}
		else{
			tv_bi_is_jk.setText("No");
		}



		plan = prsObj.parseXmlTag(input, "plan");
		tv_bi_shubh_nivesh_plan_proposed.setText(plan);
       /* if (plan.equals("Endowment Option")) {
			ll_endowment.setVisibility(View.VISIBLE);
		} else if (plan.equals("Endowment with Whole Life Option")) {
            // ll_endowment_with_option.setVisibility(View.VISIBLE);
        }*/

		policy_term = prsObj.parseXmlTag(input, "policyTerm");
		tv_bi_shubh_nivesh_term.setText(policy_term + " Years");

		String payingTerm;
		if (premium_paying_frequency.equalsIgnoreCase("Single")) {
			payingTerm = "1 Year";
		} else {
			payingTerm = policy_term + " Years";
		}

		tv_bi_shubh_nivesh_premium_paying_term.setText(payingTerm);

		sum_assured = prsObj.parseXmlTag(input, "sumAssured");

		tv_bi_shubh_nivesh_sum_assured.setText("Rs. "
				+ getformatedThousandString(Integer.parseInt(obj.getRound(obj
				.getStringWithout_E(Double.valueOf((sum_assured
						.equals("") || sum_assured == null) ? "0"
						: sum_assured))))));

        String guarntdDeathBenft_suAssured = ((int) Double.parseDouble(prsObj
                .parseXmlTag(output, "deathGuar" + 1 + ""))) + "";


        tv_bi_shubh_nivesh_sum_assured2.setText(""
                + getformatedThousandString(Integer.parseInt(obj.getRound(obj
                .getStringWithout_E(Double.valueOf((guarntdDeathBenft_suAssured
                        .equals("") || guarntdDeathBenft_suAssured == null) ? "0"
                        : guarntdDeathBenft_suAssured))))));

		switch (premium_paying_frequency) {
			case "Yearly":
				tv_premium_type.setText("Yearly " + "premium ");
				tv_premium_type_rider.setText("Yearly "
						+ "premium with Applicable Taxes");
            // tv_premium_type1.setText("Yearly " + "premium ");
            //  tv_premium_type2.setText("Yearly " + "premium ");
            //  tv_premium_type3.setText("Yearly " + "premium ");
				tv_basic_premium_tag.setText("Yearly " + "premium ");
//                tv_premium_install_rider_type1.setText("Yearly Premium (Rs.)");
				tv_mandatory_bi_shubh_nivesh_yearly_premium_with_tax1.setText("Yearly Premium with Applicable Taxes(Rs.)");

				break;
			case "Half Yearly":
				tv_premium_type.setText("Half Yearly " + "premium ");
				tv_premium_type_rider.setText("Half-Yearly "
						+ "premium with Applicable Taxes");
            // tv_premium_type1.setText("Half-Yearly " + "premium ");
            // tv_premium_type2.setText("Half-Yearly " + "premium ");
            //  tv_premium_type3.setText("Half-Yearly " + "premium ");
				tv_basic_premium_tag.setText("Half-Yearly " + "premium ");
//                tv_premium_install_rider_type1.setText("Half Yearly Premium (Rs.)");
				tv_mandatory_bi_shubh_nivesh_yearly_premium_with_tax1.setText("Half Yearly Premium with Applicable Taxes(Rs.)");
				break;
			case "Quarterly":
				tv_premium_type.setText("Quarterly " + "premium ");
				tv_premium_type_rider.setText("Quarterly "
						+ "premium with Applicable Taxes");
//                tv_premium_type1.setText("Quarterly " + "premium ");
//                tv_premium_type2.setText("Quarterly " + "premium ");
//                tv_premium_type3.setText("Quarterly " + "premium ");
				tv_basic_premium_tag.setText("Quarterly " + "premium ");
//                tv_premium_install_rider_type1.setText("Quarterly Premium (Rs.)");
				tv_mandatory_bi_shubh_nivesh_yearly_premium_with_tax1.setText("Quarterly Premium with Applicable Taxes(Rs.)");
				break;
			case "Monthly":
				tv_premium_type.setText("Monthly " + "premium ");
				tv_premium_type_rider.setText("Monthly "
						+ "premium with Applicable Taxes");
//                tv_premium_type1.setText("Monthly " + "premium ");
//                tv_premium_type2.setText("Monthly " + "premium ");
//                tv_premium_type3.setText("Monthly " + "premium ");
				tv_basic_premium_tag.setText("Monthly " + "premium ");
//                tv_premium_install_rider_type1.setText("Monthly Premium (Rs.)");
				tv_mandatory_bi_shubh_nivesh_yearly_premium_with_tax1.setText("Monthly Premium with Applicable Taxes(Rs.)");
				break;
			case "Monthly ECS":
				tv_premium_type.setText("Monthly-ECS " + "premium ");
				tv_premium_type_rider.setText("Monthly-ECS "
						+ "premium with Applicable Taxes");
//                tv_premium_type1.setText("Monthly-ECS " + "premium ");
//                tv_premium_type2.setText("Monthly-ECS " + "premium ");
//                tv_premium_type3.setText("Monthly-ECS " + "premium ");
				break;
			case "Monthly-SI/CC":
				tv_premium_type.setText("Monthly-SI/CC " + "premium ");
				tv_premium_type_rider.setText("Monthly-SI/CC "
						+ "premium with Applicable Taxes");
//                tv_premium_type1.setText("Monthly-SI/CC " + "premium ");
//                tv_premium_type2.setText("Monthly-SI/CC " + "premium ");
//                tv_premium_type3.setText("Monthly-SI/CC " + "premium ");
				break;
			case "Single":
				tv_premium_type.setText("Single " + "premium ");
				tv_premium_type_rider.setText("Single "
						+ "premium with Applicable Taxes");
//                tv_premium_type1.setText("Single " + "premium ");
//                tv_premium_type2.setText("Single " + "premium ");
//                tv_premium_type3.setText("Single " + "premium ");
				tv_basic_premium_tag.setText("Single " + "premium ");
//                tv_premium_install_rider_type1.setText("Single Premium (Rs.)");
				tv_mandatory_bi_shubh_nivesh_yearly_premium_with_tax1.setText("Single Premium with Applicable Taxes(Rs.)");
				break;
		}
		String premium = prsObj.parseXmlTag(output, "basicPrem");
		tv_bi_shubh_nivesh_yearly_premium.setText("Rs. "
				+ commonMethods.getformatedThousandString(Integer.parseInt(obj.getRound(obj.getStringWithout_E(Double.valueOf((premium
				.equals("") || premium == null) ? "0" : premium))))));

		Premium_pdf = premium;

		adb_rider_status = prsObj.parseXmlTag(input, "isADBRider");
		atpdb_rider_status = prsObj.parseXmlTag(input, "isATPDBRider");
		ptr_rider_status = prsObj.parseXmlTag(input, "isPTARider");

		if (adb_rider_status.equals("false")
				&& atpdb_rider_status.equals("false")
				&& ptr_rider_status.equals("false")) {
			ll_basic_rider.setVisibility(View.GONE);

		} else {
			ll_basic_rider.setVisibility(View.VISIBLE);
			if (adb_rider_status.equals("true")) {
				ll_basic_adb_rider.setVisibility(View.VISIBLE);
				term_adb = prsObj.parseXmlTag(input, "adbTerm");
				sa_adb = prsObj.parseXmlTag(input, "adbSA");
				prem_adb = prsObj.parseXmlTag(output, "adbRiderPrem");

				tv_bi_shubh_nivesh_premium_paying_term_adb.setText(term_adb
						+ " Years");

				tv_bi_shubh_nivesh_sum_assured_adb.setText("Rs. "
						+ commonMethods.getformatedThousandString(Integer.parseInt(obj.getRound(obj.getStringWithout_E(Double
						.valueOf(sa_adb.equals("") ? "0" : sa_adb))))));

				tv_bi_shubh_nivesh_yearly_premium_adb
						.setText("Rs. "
								+ commonMethods.getformatedThousandString(Integer.parseInt(obj.getRound(obj.getStringWithout_E(Double
								.valueOf(prem_adb.equals("") ? "0"
										: prem_adb))))));
			} else {
				ll_basic_adb_rider.setVisibility(View.GONE);
			}
			if (atpdb_rider_status.equals("true")) {
				ll_basic_atpdb_rider.setVisibility(View.VISIBLE);
				term_atpdb = prsObj.parseXmlTag(input, "atpdbTerm");

				sa_atpdb = prsObj.parseXmlTag(input, "atpdbSA");
				prem_atpdb = prsObj.parseXmlTag(output, "atpdbRiderPrem");

				tv_bi_shubh_nivesh_premium_paying_term_adptb.setText(term_atpdb
						+ " Years");

				tv_bi_shubh_nivesh_sum_assured_adptb
						.setText("Rs. "
								+ commonMethods.getformatedThousandString(Integer.parseInt(obj.getRound(obj.getStringWithout_E(Double
								.valueOf(sa_atpdb.equals("") ? "0"
										: sa_atpdb))))));

				tv_bi_shubh_nivesh_yearly_premium_adptb.setText("Rs. "
						+ commonMethods.getformatedThousandString(Integer.parseInt(obj.getRound(obj.getStringWithout_E(Double
						.valueOf(prem_atpdb.equals("") ? "0"
								: prem_atpdb))))));
			} else {
				ll_basic_atpdb_rider.setVisibility(View.GONE);
			}
			if (ptr_rider_status.equals("true")) {
				ll_basic_pta_rider.setVisibility(View.VISIBLE);
				term_pta = prsObj.parseXmlTag(input, "ptaTerm");
				sa_pta = prsObj.parseXmlTag(input, "ptaSA");
				prem_pta = prsObj.parseXmlTag(output, "ptaRiderPrem");

				tv_bi_shubh_nivesh_premium_paying_term_pta.setText(term_pta
                        + "");
                premiumPaymentMode = prsObj.parseXmlTag(input, "premFreq");

//                premiumPaymentMode = prsObj.parseXmlTag(input, "premFreq");
        if (premiumPaymentMode.equalsIgnoreCase("Single")) {
            tv_subh_nivesh_ppo.setText("Single");
            tv_premium_tag.setText("Single");
        } else {
            tv_subh_nivesh_ppo.setText("Regular");
            tv_premium_tag.setText("Annualized premium");
        }
                if (premiumPaymentMode.equalsIgnoreCase("Single")) {
                    tv_bi_shubh_nivesh_rider_premium_paying_term_pta.setText("1");
                    tv_bi_shubh_nivesh_rider_premium_paying_term_adb.setText("1");
                    tv_bi_shubh_nivesh_rider_premium_paying_term_adptb.setText("1");
                } else {
                    tv_bi_shubh_nivesh_rider_premium_paying_term_pta.setText(term_pta
                            + "");
                }

				tv_bi_shubh_nivesh_sum_assured_pta.setText("Rs. "
						+ commonMethods.getformatedThousandString(Integer.parseInt(obj.getRound(obj.getStringWithout_E(Double
						.valueOf(sa_pta.equals("") ? "0" : sa_pta))))));

                tv_bi_shubh_nivesh_yearly_premium_term_assurance_rider
                        .setText(""
                                + getformatedThousandString(Integer.parseInt(obj.getRound(obj.getStringWithout_E(Double
								.valueOf(prem_pta.equals("") ? "0"
										: prem_pta))))));
			} else {
				ll_basic_pta_rider.setVisibility(View.GONE);
			}
		}

		basicprem = prsObj.parseXmlTag(output, "installmntPrem");

		tv_bi_shubh_nivesh_basic_premium.setText("Rs. "
				+ commonMethods.getformatedThousandString(Integer.parseInt(obj.getRound(obj.getStringWithout_E(Double.valueOf(basicprem
				.equals("") ? "0" : basicprem))))));

		servicetax = prsObj.parseXmlTag(output, "servTax");

		tv_bi_shubh_nivesh_service_tax.setText("Rs. "
				+ commonMethods.getformatedThousandString(Integer.parseInt(obj.getRound(obj.getStringWithout_E(Double.valueOf(servicetax
				.equals("") ? "0" : servicetax))))));

		basicplustax = prsObj.parseXmlTag(output, "installmntPremWithSerTx");

		tv_bi_shubh_nivesh_yearly_premium_with_tax
				.setText("Rs. "
						+ commonMethods.getformatedThousandString(Integer.parseInt(obj.getRound(obj.getStringWithout_E(Double
						.valueOf(basicplustax.equals("") ? "0"
								: basicplustax))))));

		//Second year onwards

		servcTaxSecondYear= prsObj.parseXmlTag(output, "servTaxSeondYear");
		premWthSTSecondYear= prsObj.parseXmlTag(output, "installmntPremWithSerTxSecondYear");


        tv_bi_subh_nivesh_yearly_premium.setText(""
                + getformatedThousandString(Integer.parseInt(obj.getRound(obj.getStringWithout_E(Double.valueOf((premium
                .equals("") || premium == null) ? "0" : premium))))));


        premiumSingleInstBasicRider = prsObj.parseXmlTag(output, "premiumSingleInstBasicRider");
        tv_bi_subh_nivesh_yearly_premium2rider.setText(""
                + getformatedThousandString(Integer.parseInt(obj.getRound(obj.getStringWithout_E(Double.valueOf((premiumSingleInstBasicRider
                .equals("") || premiumSingleInstBasicRider == null) ? "0" : premiumSingleInstBasicRider))))));


        tv_bi_subh_nivesh_yearly_premium2.setText(""
                + getformatedThousandString(Integer.parseInt(obj.getRound(obj.getStringWithout_E(Double.valueOf((basicprem
                .equals("") || basicprem == null) ? "0" : basicprem))))));


        tv_bi_shubh_nivesh_basic_premium_first_year.setText(""
                + getformatedThousandString(Integer.parseInt(obj.getRound(obj.getStringWithout_E(Double.valueOf(premInstBasicFirstYear
                .equals("") ? "0" : premInstBasicFirstYear))))));


        premInstFYRider = prsObj.parseXmlTag(output, "premInstFYRider");
        tv_bi_shubh_nivesh_service_tax_first_year.setText(""
                + getformatedThousandString(Integer.parseInt(obj.getRound(obj.getStringWithout_E(Double.valueOf((premInstFYRider
                .equals("") || premInstFYRider == null) ? "0" : premInstFYRider))))));


        premInstSYRider = prsObj.parseXmlTag(output, "premInstSYRider");
       /* tv_bi_shubh_nivesh_service_tax_second_year.setText(""
                + getformatedThousandString(Integer.parseInt(obj.getRound(obj.getStringWithout_E(Double.valueOf((premInstSYRider
                .equals("") || premInstSYRider == null) ? "0" : premInstSYRider))))));*/

        tv_bi_shubh_nivesh_yearly_premium_with_tax_first_year
                .setText(""
                        + getformatedThousandString(Integer.parseInt(obj.getRound(obj.getStringWithout_E(Double
                        .valueOf(basicplustax.equals("") ? "0"
                                : basicplustax))))));

		//Amit changes start- 23-5-2016
		/*basicServiceTax = prsObj.parseXmlTag(output, "basicServiceTax");*/
		basicServiceTax = prsObj.parseXmlTag(output, "servTax");

        tv_bi_shubh_nivesh_basic_service_tax_first_year.setText(""
						+ obj.getRound(obj.getStringWithout_E(Double
								.valueOf(basicServiceTax.equals("") ? "0"
                        : basicServiceTax))));

		SBCServiceTax = prsObj.parseXmlTag(output, "SBCServiceTax");

        tv_bi_shubh_nivesh_swachh_bharat_cess_first_year
                .setText(""
                        + obj.getRound(obj.getStringWithout_E(Double
                        .valueOf(SBCServiceTax.equals("") ? "0"
                                : SBCServiceTax))));

		KKCServiceTax = prsObj.parseXmlTag(output, "KKCServiceTax");

        tv_bi_shubh_nivesh_krishi_kalyan_cess_first_year
                .setText(""
                        + obj.getRound(obj.getStringWithout_E(Double
                        .valueOf(KKCServiceTax.equals("") ? "0"
                                : KKCServiceTax))));
		//Amit changes end- 23-5-2016

        tr_second_year.setVisibility(View.GONE);

		if(!shubhNiveshBean.getPremiumFreq().equalsIgnoreCase("Single"))
		{
            tr_second_year.setVisibility(View.VISIBLE);
            tv_bi_shubh_nivesh_basic_premium_second_year.setText(""
                    + getformatedThousandString(Integer.parseInt(obj.getRound(obj.getStringWithout_E(Double
                    .valueOf(premInstBasicSecondYear.equals("") ? "0" : premInstBasicSecondYear))))));

            tv_bi_shubh_nivesh_service_tax_second_year.setText(""
                    + getformatedThousandString(Integer.parseInt(obj.getRound(obj.getStringWithout_E(Double.valueOf((premInstSYRider
                    .equals("") || premInstSYRider == null) ? "0" : premInstSYRider))))));

            tv_bi_shubh_nivesh_yearly_premium_with_tax_second_year.setText(""
                    + getformatedThousandString(Integer.parseInt(obj.getRound(obj.getStringWithout_E(Double
                    .valueOf(premWthSTSecondYear.equals("") ? "0"
                            : premWthSTSecondYear))))));

			//Amit changes start- 23-5-2016
            // basicServiceTaxSecondYear = prsObj.parseXmlTag(output,
            // "basicServiceTaxSecondYear");
            basicServiceTaxSecondYear = prsObj.parseXmlTag(output,
                    "servTaxSeondYear");

            tv_bi_shubh_nivesh_basic_service_tax_second_year.setText(""
							+ obj.getRound(obj.getStringWithout_E(Double
									.valueOf(basicServiceTaxSecondYear.equals("") ? "0"
                            : basicServiceTaxSecondYear))));

            SBCServiceTaxSecondYear = prsObj.parseXmlTag(output,
                    "SBCServiceTaxSecondYear");

            tv_bi_shubh_nivesh_swachh_bharat_cess_second_year.setText(""
                    + obj.getRound(obj.getStringWithout_E(Double
                    .valueOf(SBCServiceTaxSecondYear.equals("") ? "0"
                            : SBCServiceTaxSecondYear))));

            KKCServiceTaxSecondYear = prsObj.parseXmlTag(output,
                    "KKCServiceTaxSecondYear");

            tv_bi_shubh_nivesh_krishi_kalyan_cess_second_year.setText(""
                    + obj.getRound(obj.getStringWithout_E(Double
                    .valueOf(KKCServiceTaxSecondYear.equals("") ? "0"
                            : KKCServiceTaxSecondYear))));
			//Amit changes end- 23-5-2016
        } else {
            tv_bi_shubh_nivesh_basic_premium_second_year.setText("NA");
            tv_bi_shubh_nivesh_service_tax_second_year.setText("NA");
            tv_bi_shubh_nivesh_yearly_premium_with_tax_second_year.setText("NA");
		}


		BackdatingInt = prsObj.parseXmlTag(output, "backdateInt");

		tv_backdatingint
				.setText("Rs. "
                        + getformatedThousandString(Integer.parseInt(obj.getRound(obj.getStringWithout_E(Double
						.valueOf(BackdatingInt.equals("") ? "0"
                                : BackdatingInt))))));

		String str_prem_freq;

		if (premium_paying_frequency.equalsIgnoreCase("Single")) {
			str_prem_freq = "Single";
            // tr_source_of_fund.setVisibility(View.VISIBLE);
		} else {
			str_prem_freq = "Regular/Limited";
		}

		TextView tv_Company_policy_surrender_dec = d
				.findViewById(R.id.tv_Company_policy_surrender_dec);

       /* if(str_prem_freq.equalsIgnoreCase("Single")) {


			Company_policy_surrender_dec = "Your SBI Life Shubh Nivesh (UIN: 111N055V04) is a "
					+ str_prem_freq
					+ " premium policy and you are required to pay One Time Premium of Rs. "
					+ commonMethods.getformatedThousandString(Integer.parseInt(obj.getRound(obj.getStringWithout_E(Double
					.valueOf(basicplustax.equals("") ? "0" : basicplustax)))))
					+ " .Your Policy Term is "
					+ policy_term
					+ " years"
					+ " and Basic Sum Assured is Rs. "
					+

					commonMethods.getformatedThousandString(Integer.parseInt(obj.getRound(obj
							.getStringWithout_E(Double.valueOf((sum_assured
									.equals("") || sum_assured == null) ? "0"
									: sum_assured)))));

		}
		else {

			Company_policy_surrender_dec = "Your SBI Life Shubh Nivesh (UIN: 111N055V04) is a "
					+ str_prem_freq
					+ " premium policy, for which your first year " + premium_paying_frequency + " Premium is Rs "
					+ commonMethods.getformatedThousandString(Integer.parseInt(obj.getRound(obj.getStringWithout_E(Double
					.valueOf(basicplustax.equals("") ? "0" : basicplustax)))))
					+ " .Your Policy Term is "
					+ policy_term
					+ " years"
					+ " , Premium Payment Term is "
					+ payingTerm
					+ " and Basic Sum Assured is Rs. "
					+

					commonMethods.getformatedThousandString(Integer.parseInt(obj.getRound(obj
							.getStringWithout_E(Double.valueOf((sum_assured
									.equals("") || sum_assured == null) ? "0"
									: sum_assured)))));

		}
		tv_Company_policy_surrender_dec.setText(Company_policy_surrender_dec);
*/
		for (int i = 1; i <= Integer.parseInt(policy_term); i++) {

			String policy_year = prsObj
					.parseXmlTag(output, "policyYr" + i + "");
			String total_base_premium_without_tax = prsObj.parseXmlTag(output,
					"TotBasePremWithTx" + i + "");
			String benefit_payable_at_death_4_percentage = prsObj.parseXmlTag(
					output, "deathNonGuar4pa" + i + "");
            String death_gurantee = prsObj.parseXmlTag(output, "deathGuar" + i
                    + "");
			String benefit_payable_at_death_8_percentage = prsObj.parseXmlTag(
					output, "deathNonGuar8pa" + i + "");
			String maturity_benefit_gurantee = prsObj.parseXmlTag(output,
					"matGuar" + i + "");
			String maturity_benefit_non_gurantee_4_percentage = prsObj
					.parseXmlTag(output, "matNonGuar4pa" + i + "");
			String maturity_benefit_non_gurantee_8_percentage = prsObj
					.parseXmlTag(output, "matNonGuar8pa" + i + "");
			String surrender_value_gurantee = prsObj.parseXmlTag(output,
					"surGuar" + i + "");
			String surrender_value_ssv_4_percentage = prsObj.parseXmlTag(
					output, "surNonGuar4pa" + i + "");
			String surrender_value_ssv_8_percentage = prsObj.parseXmlTag(
					output, "surNonGuar8pa" + i + "");


            String end_of_year = prsObj
                    .parseXmlTag(output, "policyYr" + i + "");
            String total_base_premium = ((int) Double.parseDouble(prsObj
                    .parseXmlTag(output, "AnnulizedPremium" + i + ""))) + "";


            String GuarateedAddition = ((int) Double.parseDouble(prsObj
                    .parseXmlTag(output, "GuarnAddition" + i + ""))) + "";


            String guaranteed_survival_benefit = ((int) Double
                    .parseDouble(prsObj.parseXmlTag(output, "matGuar" + i
                            + "")))
                    + "";

           /* String guaranteed_survival_benefit = ((int) Double
                    .parseDouble(prsObj.parseXmlTag(output, "getSurvivalBenefit" + i
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
            //  list_data.add(new M_BI_ShubhNivesh_AdapterCommon("","","","","","","","","","","","","","","","",""));

			list_data
                    .add(new M_BI_ShubhNivesh_AdapterCommon(
                            end_of_year,
                            (obj.getRound(obj.getStringWithout_E(Double
                                    .valueOf(total_base_premium))))
                                    + "",
							(obj.getRound(obj.getStringWithout_E(Double
                                    .valueOf(GuarateedAddition))))
                                    + "",
                            "0",
                            (obj.getRound(obj.getStringWithout_E(Double
                                    .valueOf(guaranteed_surrender_value))))
                                    + "",
							(obj.getRound(obj.getStringWithout_E(Double
                                    .valueOf(guarntdDeathBenft))))
									+ "",
							(obj.getRound(obj.getStringWithout_E(Double
                                    .valueOf(MaturityBenefit))))
                                    + "",
							(obj.getRound(obj.getStringWithout_E(Double
                                    .valueOf(ReversionaryBonus4Per))))
									+ "",
                            "0",
							(obj.getRound(obj.getStringWithout_E(Double
                                    .valueOf(not_guaranteed_surrender_value_4per))))
									+ "",
							(obj.getRound(obj.getStringWithout_E(Double
                                    .valueOf(ReversionaryBonus8Per))))
                                    + "",
                            "0",
							(obj.getRound(obj.getStringWithout_E(Double
                                    .valueOf(not_guaranteed_surrender_value_8per))))
									+ "",
							(obj.getRound(obj.getStringWithout_E(Double
                                    .valueOf(TotalMaturityBenefit4per))))
									+ "",
							(obj.getRound(obj.getStringWithout_E(Double
                                    .valueOf(TotalMaturityBenefit8per))))
                                    + "",
							(obj.getRound(obj.getStringWithout_E(Double
                                    .valueOf(TotalDeathBenefit4per))))
									+ "",
							(obj.getRound(obj.getStringWithout_E(Double
                                    .valueOf(TotalDeathBenefit8per))))
									+ ""));

		}
        Adapter_BI_ShubhNiveshGridCommon adapter = new Adapter_BI_ShubhNiveshGridCommon(
				BI_ShubhNiveshActivity.this, list_data);
		gv_userinfo.setAdapter(adapter);

		GridHeight gh =new GridHeight();
		gh.getheight(gv_userinfo, policy_term);

		d.show();

	}

	private void CreateShubhNiveshPlusBIPdf() {

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
			Font small_bold = new Font(Font.FontFamily.TIMES_ROMAN, 8,
					Font.BOLD);
            Font normal1_bold = new Font(Font.FontFamily.TIMES_ROMAN, 6,
                    Font.BOLD);
			Font small_bold1 = new Font(Font.FontFamily.TIMES_ROMAN, 8,
					Font.BOLD);
            Font normal_bold = new Font(Font.FontFamily.TIMES_ROMAN, 5,
                    Font.BOLD);
			Font small_bold2 = new Font(Font.FontFamily.TIMES_ROMAN, 4,
					Font.BOLD);
            Font normal = new Font(Font.FontFamily.TIMES_ROMAN, 5, Font.NORMAL);
			Font small_normal = new Font(Font.FontFamily.TIMES_ROMAN, 8,
					Font.NORMAL);
			// File mypath = new File(folder, PropserNumber +
			// "Proposalno_p02.pdf");
			mypath = mStorageUtils.createFileToAppSpecificDir(context, QuatationNumber + "BI.pdf");
			needAnalysispath = mStorageUtils.createFileToAppSpecificDir(context, "NA.pdf");
			//			needAnalysis_Tabular_path = new File(folder, "NA_Tabular.pdf");
			newFile = mStorageUtils.createFileToAppSpecificDir(context, QuatationNumber+"P01.pdf");

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
							"Benefit Illustration for SBI LIFE - Shubh Nivesh (UIN No -"
									+ "111N055V04" + ")", headerBold));

			PdfPTable headertable = new PdfPTable(1);
			headertable.setWidthPercentage(100);
			PdfPCell c1 = new PdfPCell(new Phrase(Para_Header));
			c1.setBackgroundColor(BaseColor.DARK_GRAY);
			c1.setPadding(5);
			c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            // headertable.addCell(c1);
			headertable.setHorizontalAlignment(Element.ALIGN_CENTER);
			Paragraph para_address = new Paragraph(
                    "SBI Life Insurance Co. Ltd",
					small_bold);
			para_address.setAlignment(Element.ALIGN_CENTER);
            Paragraph para_address1 = new Paragraph(
                    "Registered & Corporate Office: SBI Life Insuarnce Co. Ltd, 'Natraj', M.V.Road and Western Express Highway Junction, Andheri (East), Mumbai - 400069",
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
                    "Benefit Illustration (BI) : SBI Life - Shubh Nivesh  (UIN :  111N055V04) | An Individual, Non-linked, Participating, Life Insurance Savings Product",
                    small_bold);
            para_address4.setAlignment(Element.ALIGN_CENTER);
			document.add(para_address);
            document.add(para_address1);
            document.add(para_address2);
            document.add(para_address3);
            document.add(para_address4);
			document.add(para_img_logo_after_space_1);
            //document.add(headertable);
			document.add(para_img_logo_after_space_1);

			// document.add(para_img_logo_after_space_1);
			//
			// Paragraph para_channalDetails = new Paragraph("Channel Details",
			// small_bold);
			// document.add(para_channalDetails);
			// document.add(para_img_logo_after_space_1);
			// // Get Channel Detail
			// M_ChannelDetails channelDetail = db
			// .getChannelDetail(LoginActivity.confirmemailid);
			//
			// String code = channelDetail.getCode();
			// code = code.substring(2, code.length()).trim();
			// String name = channelDetail.getName();
			// PdfPTable table_code_name = new PdfPTable(4);
			// table_code_name.setWidthPercentage(100);
			//
			// PdfPCell table_code_name_cell_1 = new PdfPCell(new Paragraph(
			// "IA Code", small_normal));
			// PdfPCell table_code_name_cell_2 = new PdfPCell(new
			// Paragraph(code,
			// small_bold));
			// table_code_name_cell_2.setHorizontalAlignment(Element.ALIGN_CENTER);
			// PdfPCell table_code_name_cell_3 = new PdfPCell(new Paragraph(
			// "IA Name", small_normal));
			// PdfPCell table_code_name_cell_4 = new PdfPCell(new
			// Paragraph(name,
			// small_bold));
			//
			// table_code_name_cell_4.setVerticalAlignment(Element.ALIGN_CENTER);
			//
			// table_code_name_cell_1.setPadding(5);
			// table_code_name_cell_2.setPadding(5);
			// table_code_name_cell_3.setPadding(5);
			// table_code_name_cell_4.setPadding(5);
			//
			// table_code_name.addCell(table_code_name_cell_1);
			// table_code_name.addCell(table_code_name_cell_2);
			// table_code_name.addCell(table_code_name_cell_3);
			// table_code_name.addCell(table_code_name_cell_4);
			//
			// document.add(table_code_name);

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
                    "Channel / Intermediary :", small_normal));
			PdfPCell NameofProposal_cell_4 = new PdfPCell(new Paragraph(
                    userType, small_bold1));
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

			PdfPTable BI_Pdftable21 = new PdfPTable(1);
			BI_Pdftable21.setWidthPercentage(100);
			PdfPCell BI_Pdftable2_cell11 = new PdfPCell(
					new Paragraph(
                            "The main objective of the illustration is that the client is able to appreciate the features of the product and the flow of benefits in different circumstances with some level of quantification. For further information on the product, its benefits  please refer to the sales brochure and/or policy document.",
							small_normal));

			BI_Pdftable2_cell11.setPadding(5);

			BI_Pdftable21.addCell(BI_Pdftable2_cell11);
			document.add(BI_Pdftable21);

			PdfPTable BI_Pdftable3 = new PdfPTable(1);
			BI_Pdftable3.setWidthPercentage(100);
			// CR_table6.setWidths(columnWidths_3);
			PdfPCell BI_Pdftable3_cell1 = new PdfPCell(
					new Paragraph(
							"The main objective of the illustration is that the client is able to appreciate the features of the product and the flow of benefits in different circumstances with some level of quantification. For further information on the product, its benefits and applicable charges please refer to the sales brochure and/or policy document. Further information will also be available on request.",
							small_normal));

			BI_Pdftable3_cell1.setPadding(5);

			BI_Pdftable3.addCell(BI_Pdftable3_cell1);
            //document.add(BI_Pdftable3);

			PdfPTable BI_Pdftable4 = new PdfPTable(1);
			BI_Pdftable4.setWidthPercentage(100);
			// CR_table6.setWidths(columnWidths_4);
			PdfPCell BI_Pdftable4_cell1 = new PdfPCell(
					new Paragraph(
							"Statutory Warning-"
									+ "Some benefits are guaranteed and some benefits are variable with returns based on the future performance of your Insurer carrying on life insurance business. If your policy offers guaranteed returns then these will be clearly marked guaranteed in the illustration table on this page. If your policy offers variable returns then the illustrations on this page will show two different rates of assumed future investment returns. These assumed rates of return are not guaranteed and they are not the upper or lower limits of what you might get back, as the value of your policy is dependent on a number of factors including future investment performance.",
							small_normal));

			BI_Pdftable4_cell1.setPadding(5);

			BI_Pdftable4.addCell(BI_Pdftable4_cell1);
            // document.add(BI_Pdftable4);
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

            PdfPCell cell_LifeAssuredName3 = new PdfPCell(new Paragraph(
                    "Name of the Life Assured", small_normal));
            cell_LifeAssuredName3.setPadding(5);
            PdfPCell cell_lLifeAssuredName4 = new PdfPCell(new Paragraph(
                    name_of_proposer, small_bold));
            cell_lLifeAssuredName4.setPadding(5);
            cell_lLifeAssuredName4.setHorizontalAlignment(Element.ALIGN_CENTER);

			PdfPCell cell_lifeAssuredAge1 = new PdfPCell(new Paragraph(
                    "Age(Years)", small_normal));
			cell_lifeAssuredAge1.setPadding(5);
			PdfPCell cell_lifeAssuredAge2 = new PdfPCell(new Paragraph(
                    age_entry + "", small_bold));
			cell_lifeAssuredAge2.setPadding(5);
			cell_lifeAssuredAge2.setHorizontalAlignment(Element.ALIGN_CENTER);

            PdfPCell cell_lifeAssuredAge3 = new PdfPCell(new Paragraph(
                    "Age(Years)", small_normal));
            cell_lifeAssuredAge3.setPadding(5);
            PdfPCell cell_lifeAssuredAge4 = new PdfPCell(new Paragraph(
                    age_entry + "", small_bold));
            cell_lifeAssuredAge4.setPadding(5);
            cell_lifeAssuredAge4.setHorizontalAlignment(Element.ALIGN_CENTER);

			table_lifeAssuredName.addCell(cell_LifeAssuredName1);
			table_lifeAssuredName.addCell(cell_lLifeAssuredName2);
            table_lifeAssuredName.addCell(cell_LifeAssuredName3);
            table_lifeAssuredName.addCell(cell_lLifeAssuredName4);
			table_lifeAssuredName.addCell(cell_lifeAssuredAge1);
			table_lifeAssuredName.addCell(cell_lifeAssuredAge2);
            table_lifeAssuredName.addCell(cell_lifeAssuredAge3);
            table_lifeAssuredName.addCell(cell_lifeAssuredAge4);
			document.add(table_lifeAssuredName);

			PdfPTable table_lifeAssuredDetails = new PdfPTable(4);
			table_lifeAssuredDetails.setWidthPercentage(100);

			PdfPCell cell_lifeAssuredAmaturityGender1 = new PdfPCell(
					new Paragraph("Gender", small_normal));
			cell_lifeAssuredAmaturityGender1.setPadding(5);
			PdfPCell cell_lifeAssuredAmaturityGender2 = new PdfPCell(
					new Paragraph(gender, small_bold));
			cell_lifeAssuredAmaturityGender2.setPadding(5);
			cell_lifeAssuredAmaturityGender2
					.setHorizontalAlignment(Element.ALIGN_CENTER);

            PdfPCell cell_lifeAssuredAmaturityGender3 = new PdfPCell(
                    new Paragraph("Gender", small_normal));
            cell_lifeAssuredAmaturityGender3.setPadding(5);
            PdfPCell cell_lifeAssuredAmaturityGender4 = new PdfPCell(
                    new Paragraph(gender, small_bold));
            cell_lifeAssuredAmaturityGender4.setPadding(5);
            cell_lifeAssuredAmaturityGender4
                    .setHorizontalAlignment(Element.ALIGN_CENTER);


            PdfPCell cell_lifeAssuredAState3 = new PdfPCell(
                    new Paragraph("", small_normal));
            cell_lifeAssuredAState3.setPadding(5);
            PdfPCell cell_lifeAssuredAState4 = new PdfPCell(
                    new Paragraph("", small_bold));
            cell_lifeAssuredAState4.setPadding(5);
            cell_lifeAssuredAState4
					.setHorizontalAlignment(Element.ALIGN_CENTER);

			table_lifeAssuredDetails.addCell(cell_lifeAssuredAmaturityGender1);
			table_lifeAssuredDetails.addCell(cell_lifeAssuredAmaturityGender2);
            table_lifeAssuredDetails.addCell(cell_lifeAssuredAmaturityGender3);
            table_lifeAssuredDetails.addCell(cell_lifeAssuredAmaturityGender4);


            // table_lifeAssuredDetails
            //         .addCell(cell_lifeAssured_Premium_frequeny1);
            // table_lifeAssuredDetails
            //         .addCell(cell_lifeAssured_Premium_frequeny2);
			document.add(table_lifeAssuredDetails);
            PdfPTable table_lifeAssuredDetails2;
            if (cb_kerladisc.isChecked()) {
                table_lifeAssuredDetails2 = new PdfPTable(2);
                table_lifeAssuredDetails2.setWidthPercentage(100);

                PdfPCell cell_lifeAssuredAState = new PdfPCell(
                        new Paragraph("State", small_normal));
                cell_lifeAssuredAState.setPadding(5);
                PdfPCell cell_lifeAssuredASate2 = new PdfPCell(
                        new Paragraph("Kerala", small_bold));
                cell_lifeAssuredASate2.setPadding(5);
                cell_lifeAssuredASate2
                        .setHorizontalAlignment(Element.ALIGN_CENTER);

                table_lifeAssuredDetails2.addCell(cell_lifeAssuredAState);
                //document.add(table_lifeAssuredDetails2);

            } else {
                table_lifeAssuredDetails2 = new PdfPTable(2);
                table_lifeAssuredDetails2.setWidthPercentage(100);

                PdfPCell cell_lifeAssuredAState = new PdfPCell(
                        new Paragraph("State", small_normal));
                cell_lifeAssuredAState.setPadding(5);
                PdfPCell cell_lifeAssuredASate2 = new PdfPCell(
                        new Paragraph("Non Kerala", small_bold));
                cell_lifeAssuredASate2.setPadding(5);
                cell_lifeAssuredASate2
                        .setHorizontalAlignment(Element.ALIGN_CENTER);

                table_lifeAssuredDetails2.addCell(cell_lifeAssuredAState);
                //document.add(table_lifeAssuredDetails2);
            }

			String isStaff = "";
			if (selStaffDisc.isChecked()) {
                isStaff = "Yes";
				PdfPTable table_staff_NonStaff = new PdfPTable(2);
				table_staff_NonStaff.setWidthPercentage(100);

				PdfPCell cell_staff_NonStaff1 = new PdfPCell(new Paragraph(
                        "Staff", small_normal));
				cell_staff_NonStaff1.setPadding(5);

				PdfPCell cell_staff_NonStaff2 = new PdfPCell(new Paragraph(
						isStaff, small_bold));
				cell_staff_NonStaff2.setPadding(5);
				cell_staff_NonStaff2
						.setHorizontalAlignment(Element.ALIGN_CENTER);

				table_staff_NonStaff.addCell(cell_staff_NonStaff1);
				table_staff_NonStaff.addCell(cell_staff_NonStaff2);
				document.add(table_staff_NonStaff);
			}
			else
			{
                isStaff = "No";
				PdfPTable table_staff_NonStaff = new PdfPTable(2);
				table_staff_NonStaff.setWidthPercentage(100);

				PdfPCell cell_staff_NonStaff1 = new PdfPCell(new Paragraph(
                        "Staff", small_normal));
				cell_staff_NonStaff1.setPadding(5);

				PdfPCell cell_staff_NonStaff2 = new PdfPCell(new Paragraph(
						isStaff, small_bold));
				cell_staff_NonStaff2.setPadding(5);
				cell_staff_NonStaff2
						.setHorizontalAlignment(Element.ALIGN_CENTER);

				table_staff_NonStaff.addCell(cell_staff_NonStaff1);
				table_staff_NonStaff.addCell(cell_staff_NonStaff2);
                //document.add(table_lifeAssuredDetails2);
				document.add(table_staff_NonStaff);

			}

			String isJK = "";
			if (selJkResident.isChecked()) {
				isJK = "yes";

				PdfPTable table_is_JK = new PdfPTable(2);
				table_is_JK.setWidthPercentage(100);

				PdfPCell cell_is_JK1 = new PdfPCell(new Paragraph("J&k",
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

			PdfPCell cell_PlanProposed1 = new PdfPCell(new Paragraph(
                    "Policy Option", small_normal));
			cell_PlanProposed1.setPadding(5);
			PdfPCell cell_tPlanProposed2 = new PdfPCell(new Paragraph(plan,
					small_bold));
			cell_tPlanProposed2.setPadding(5);
			cell_tPlanProposed2.setHorizontalAlignment(Element.ALIGN_CENTER);

            String premium = prsObj.parseXmlTag(output, "basicPrem");

            PdfPCell amount_of_installment = new PdfPCell(new Paragraph(
                    "Amount of Installment Premium (Rs.)", small_normal));
            amount_of_installment.setPadding(5);
            PdfPCell amount_of_installment2 = new PdfPCell(new Paragraph((""
                    + getformatedThousandString(Integer.parseInt(obj.getRound(obj.getStringWithout_E(Double.valueOf((premium
                    .equals("") || premium == null) ? "0" : premium)))))),
                    small_bold));
            amount_of_installment2.setPadding(5);
            amount_of_installment2.setHorizontalAlignment(Element.ALIGN_CENTER);

            PdfPCell cell_lifeAssured_Premium_frequeny1 = new PdfPCell(
                    new Paragraph("Premium Payment Option", small_normal));
            cell_lifeAssured_Premium_frequeny1.setPadding(5);

            premiumPaymentMode = prsObj.parseXmlTag(input, "premFreq");
            PdfPCell cell_lifeAssured_Premium_frequeny2;
            if (premiumPaymentMode.equalsIgnoreCase("Single")) {
                cell_lifeAssured_Premium_frequeny2 = new PdfPCell(
                        new Paragraph("Single", small_bold));
                cell_lifeAssured_Premium_frequeny2.setPadding(5);
                cell_lifeAssured_Premium_frequeny2
                        .setHorizontalAlignment(Element.ALIGN_CENTER);
                //tv_subh_nivesh_ppo.setText("Single");
            } else {
                cell_lifeAssured_Premium_frequeny2 = new PdfPCell(
                        new Paragraph("Regular", small_bold));
                cell_lifeAssured_Premium_frequeny2.setPadding(5);
                cell_lifeAssured_Premium_frequeny2
                        .setHorizontalAlignment(Element.ALIGN_CENTER);
                //tv_subh_nivesh_ppo.setText("Regular");

            }


            PdfPCell bonus_type = new PdfPCell(
                    new Paragraph("Bonus Type", small_normal));
            bonus_type.setPadding(5);
            PdfPCell bonus_type2 = new PdfPCell(
                    new Paragraph("Simple Reversionary Bonus", small_bold));
            bonus_type2.setPadding(5);
            bonus_type2
                    .setHorizontalAlignment(Element.ALIGN_CENTER);


            PdfPCell cell_Term1 = new PdfPCell(new Paragraph("Policy Term (Endowment Term) (Years)",
					small_normal));
			PdfPCell cell_Term2 = new PdfPCell(new Paragraph(policy_term
                    + "", small_bold));
			cell_Term1.setPadding(5);
			cell_Term2.setPadding(5);
			cell_Term2.setHorizontalAlignment(Element.ALIGN_CENTER);


            PdfPCell cell_plan1 = new PdfPCell(new Paragraph("Sum Assured(Rs.)",
                    small_normal));
            cell_plan1.setPadding(5);
            PdfPCell cell_plan2 = new PdfPCell(new Paragraph(""
                    + getformatedThousandString((Double.valueOf(sum_assured)).intValue()), small_bold));
            cell_plan2.setPadding(5);
            cell_plan2.setHorizontalAlignment(Element.ALIGN_CENTER);

            PdfPCell ppt = new PdfPCell(new Paragraph("Premium Payment Term (Years)",
                    small_normal));

            premium_paying_frequency = prsObj.parseXmlTag(input, "premFreq");
            policy_term = prsObj.parseXmlTag(input, "policyTerm");
            PdfPCell ppt2;
            String payingTerm = "";
            if (premium_paying_frequency.equalsIgnoreCase("Single")) {
                ppt2 = new PdfPCell(new Paragraph("1", small_bold));
                ppt.setPadding(5);
                ppt2.setPadding(5);
                ppt2.setHorizontalAlignment(Element.ALIGN_CENTER);

            } else {
                payingTerm = policy_term + "";
                ppt2 = new PdfPCell(new Paragraph(payingTerm
                        + "", small_bold));
                ppt.setPadding(5);
                ppt2.setPadding(5);
                ppt2.setHorizontalAlignment(Element.ALIGN_CENTER);

            }


            PdfPCell sum_death = new PdfPCell(new Paragraph("Sum Assured on Death (at inception of the policy) (Rs.) ",
                    small_normal));
            sum_death.setPadding(5);
            PdfPCell sum_death2 = new PdfPCell(new Paragraph(""
                    + getformatedThousandString((Double.valueOf(sum_assured)).intValue()), small_bold));
            sum_death2.setPadding(5);
            sum_death2.setHorizontalAlignment(Element.ALIGN_CENTER);
            premium_paying_frequency = prsObj.parseXmlTag(input, "premFreq");
            PdfPCell mode = new PdfPCell(new Paragraph("Mode / Frequency of Premium Payment  ",
                    small_normal));
            mode.setPadding(5);
            PdfPCell mode2 = new PdfPCell(new Paragraph(premium_paying_frequency, small_bold));
            mode2.setPadding(5);
            mode2.setHorizontalAlignment(Element.ALIGN_CENTER);

            PdfPCell rate = new PdfPCell(new Paragraph("Rate of Applicable Taxes",
                    small_normal));
            rate.setPadding(5);

            staffdiscount = prsObj.parseXmlTag(input, "isStaff");
            PdfPCell rate2;
            /*if (staffdiscount.equalsIgnoreCase("true")) {
                rate2 = new PdfPCell(new Paragraph("4.5% in the 1st policy year and 2.25% from 2nd policy year onwards", small_bold));
                rate2.setPadding(5);
                rate2.setHorizontalAlignment(Element.ALIGN_CENTER);

            } else {
               rate2 = new PdfPCell(new Paragraph("4.75% in the 1st policy year and 2.375% from 2nd policy year onwards", small_bold));
                rate2.setPadding(5);
                rate2.setHorizontalAlignment(Element.ALIGN_CENTER);
            }*/


            str_kerla_discount = prsObj.parseXmlTag(input, "str_kerla_discount");
            if (str_kerla_discount.equalsIgnoreCase("Yes")) {
                if (premium_paying_frequency.equals("Single")) {
                    rate2 = new PdfPCell(new Paragraph("4.75% ", small_bold));
                    rate2.setPadding(5);
                    rate2.setHorizontalAlignment(Element.ALIGN_CENTER);
                } else {
                    rate2 = new PdfPCell(new Paragraph("4.75% in the 1st policy year and 2.375% from 2nd policy year onwards ", small_bold));
                    rate2.setPadding(5);
                    rate2.setHorizontalAlignment(Element.ALIGN_CENTER);
                }
            } else {
                if (premium_paying_frequency.equals("Single")) {
                    rate2 = new PdfPCell(new Paragraph("4.50% ", small_bold));
                    rate2.setPadding(5);
                    rate2.setHorizontalAlignment(Element.ALIGN_CENTER);
                } else {
                    rate2 = new PdfPCell(new Paragraph("4.5% in the 1st policy year and 2.25% from 2nd policy year onwards ", small_bold));
                    rate2.setPadding(5);
                    rate2.setHorizontalAlignment(Element.ALIGN_CENTER);
                }
            }

           /* String payingTerm = "";
			if (premium_paying_frequency.equalsIgnoreCase("Single")) {
				payingTerm = "1 Year";
			} else {
				payingTerm = policy_term + " Years";
            }*/

			PdfPCell cell_PremiumPayingTerm1 = new PdfPCell(new Paragraph(
					"Premium Payment Term", small_normal));
			PdfPCell cell_PremiumPayingTerm2 = new PdfPCell(new Paragraph(
					payingTerm, small_bold));
			cell_PremiumPayingTerm1.setPadding(5);
			cell_PremiumPayingTerm2.setPadding(5);
			cell_PremiumPayingTerm2
					.setHorizontalAlignment(Element.ALIGN_CENTER);

			Table_policyTerm_annualisedPremium_sumAssured
					.addCell(cell_PlanProposed1);
			Table_policyTerm_annualisedPremium_sumAssured
					.addCell(cell_tPlanProposed2);
            Table_policyTerm_annualisedPremium_sumAssured.addCell(amount_of_installment);
            Table_policyTerm_annualisedPremium_sumAssured.addCell(amount_of_installment2);
            Table_policyTerm_annualisedPremium_sumAssured.addCell(cell_lifeAssured_Premium_frequeny1);
            Table_policyTerm_annualisedPremium_sumAssured.addCell(cell_lifeAssured_Premium_frequeny2);
            Table_policyTerm_annualisedPremium_sumAssured.addCell(bonus_type);
            Table_policyTerm_annualisedPremium_sumAssured.addCell(bonus_type2);
			Table_policyTerm_annualisedPremium_sumAssured.addCell(cell_Term1);
			Table_policyTerm_annualisedPremium_sumAssured.addCell(cell_Term2);
            Table_policyTerm_annualisedPremium_sumAssured.addCell(cell_plan1);
            Table_policyTerm_annualisedPremium_sumAssured.addCell(cell_plan2);
            Table_policyTerm_annualisedPremium_sumAssured.addCell(ppt);
            Table_policyTerm_annualisedPremium_sumAssured.addCell(ppt2);
            Table_policyTerm_annualisedPremium_sumAssured.addCell(sum_death);
            Table_policyTerm_annualisedPremium_sumAssured.addCell(sum_death2);
            Table_policyTerm_annualisedPremium_sumAssured.addCell(mode);
            Table_policyTerm_annualisedPremium_sumAssured.addCell(mode2);
            Table_policyTerm_annualisedPremium_sumAssured.addCell(rate);
            Table_policyTerm_annualisedPremium_sumAssured.addCell(rate2);


            // Table_policyTerm_annualisedPremium_sumAssured
            //        .addCell(cell_PremiumPayingTerm1);
            // Table_policyTerm_annualisedPremium_sumAssured
            //       .addCell(cell_PremiumPayingTerm2);

			document.add(Table_policyTerm_annualisedPremium_sumAssured);

			PdfPTable table_plan_premium_payingTerm = new PdfPTable(4);
			table_plan_premium_payingTerm.setWidthPercentage(100);


			PdfPCell cell_premium_paying_term1 = new PdfPCell(new Paragraph(
					premium_paying_frequency + " Premium", small_normal));
			cell_premium_paying_term1.setPadding(5);
			PdfPCell cell_premium_paying_term2 = new PdfPCell(new Paragraph(
					"Rs. "
                            + getformatedThousandString(Integer.parseInt(obj.getRound(obj.getStringWithout_E(Double
							.valueOf(Premium_pdf.equals("") ? "0"
									: Premium_pdf))))), small_bold));

			cell_premium_paying_term2.setPadding(5);
			cell_premium_paying_term2
					.setHorizontalAlignment(Element.ALIGN_CENTER);

            //table_plan_premium_payingTerm.addCell(cell_plan1);
            //table_plan_premium_payingTerm.addCell(cell_plan2);

            // table_plan_premium_payingTerm.addCell(cell_premium_paying_term1);
            // table_plan_premium_payingTerm.addCell(cell_premium_paying_term2);
            //document.add(table_plan_premium_payingTerm);

            //document.newPage();

            if (ptr_rider_status.equals("true") || adb_rider_status.equals("true") || atpdb_rider_status.equals("true")) {
				PdfPTable BI_pdftable_prefferedTermAssuredRider = new PdfPTable(
						1);
				BI_pdftable_prefferedTermAssuredRider.setWidthPercentage(100);
				PdfPCell BI_pdftable_prefferedTermAssuredRider_cell = new PdfPCell(
						new Paragraph(
                                "Rider Details",
								small_bold));

				BI_pdftable_prefferedTermAssuredRider_cell
						.setBackgroundColor(BaseColor.LIGHT_GRAY);

				BI_pdftable_prefferedTermAssuredRider_cell
						.setHorizontalAlignment(Element.ALIGN_CENTER);
				BI_pdftable_prefferedTermAssuredRider_cell.setPadding(5);

				BI_pdftable_prefferedTermAssuredRider
						.addCell(BI_pdftable_prefferedTermAssuredRider_cell);
				document.add(BI_pdftable_prefferedTermAssuredRider);

				PdfPTable Table_Preferred_term_assured_policyTerm_annualisedPremium_sumAssured = new PdfPTable(
                        5);
				Table_Preferred_term_assured_policyTerm_annualisedPremium_sumAssured
						.setWidthPercentage(100);

				PdfPCell cell_Preferred_term_assured_PlanProposed1 = new PdfPCell(
                        new Paragraph("Rider Name", small_normal));
				cell_Preferred_term_assured_PlanProposed1.setPadding(5);
                PdfPCell rider2 = new PdfPCell(
                        new Paragraph("Rider Policy Term (Years)", small_normal));
                rider2.setPadding(5);
                PdfPCell rider3 = new PdfPCell(
                        new Paragraph("Rider Sum Assured (Rs.)", small_normal));
                rider3.setPadding(5);
                PdfPCell rider4 = new PdfPCell(
                        new Paragraph("Rider Premium Payment Term (Years)", small_normal));
                rider4.setPadding(5);
                PdfPCell rider5 = new PdfPCell(
                        new Paragraph("Rider Premiums (Rs.)", small_normal));
                rider5.setPadding(5);


                Table_Preferred_term_assured_policyTerm_annualisedPremium_sumAssured
                        .addCell(cell_Preferred_term_assured_PlanProposed1);
                Table_Preferred_term_assured_policyTerm_annualisedPremium_sumAssured
                        .addCell(rider2);
                Table_Preferred_term_assured_policyTerm_annualisedPremium_sumAssured
                        .addCell(rider3);
                Table_Preferred_term_assured_policyTerm_annualisedPremium_sumAssured
                        .addCell(rider4);
                Table_Preferred_term_assured_policyTerm_annualisedPremium_sumAssured
                        .addCell(rider5);

                document.add(Table_Preferred_term_assured_policyTerm_annualisedPremium_sumAssured);

            }

            if (ptr_rider_status.equals("true")) {

                PdfPTable Table_Preferred_term_assured_policyTerm_annualisedPremium_sumAssured = new PdfPTable(
                        5);
                Table_Preferred_term_assured_policyTerm_annualisedPremium_sumAssured
                        .setWidthPercentage(100);

                PdfPCell rider6 = new PdfPCell(
                        new Paragraph("SBI Life - Preferred Term Rider (UIN:111B014V02)", small_normal));
                rider6.setPadding(5);
				PdfPCell cell_Preferred_term_assured_tPlanProposed2 = new PdfPCell(
                        new Paragraph(term_pta + "", small_bold));
				cell_Preferred_term_assured_tPlanProposed2.setPadding(5);
				cell_Preferred_term_assured_tPlanProposed2
						.setHorizontalAlignment(Element.ALIGN_CENTER);


				PdfPCell cell_Preferred_term_assured_Term2 = new PdfPCell(
						new Paragraph("Rs. "
                                + getformatedThousandString(Integer.parseInt(obj.getRound(obj.getStringWithout_E(Double
								.valueOf(sa_pta.equals("") ? "0"
										: sa_pta))))), small_bold));
				cell_Preferred_term_assured_Term2.setPadding(5);
				cell_Preferred_term_assured_Term2
						.setHorizontalAlignment(Element.ALIGN_CENTER);
                PdfPCell rider_years;
                if (premium_paying_frequency.equalsIgnoreCase("Single")) {
                    rider_years = new PdfPCell(new Paragraph("1", small_bold));
                    rider_years.setPadding(5);
                    rider_years.setHorizontalAlignment(Element.ALIGN_CENTER);
                } else {
                    rider_years = new PdfPCell(new Paragraph(term_pta + "", small_bold));
                    rider_years.setPadding(5);
                    rider_years.setHorizontalAlignment(Element.ALIGN_CENTER);
                }


                PdfPCell rider_years2 = new PdfPCell(
                        new Paragraph(("Rs."
                                + getformatedThousandString(Integer.parseInt(obj.getRound(obj.getStringWithout_E(Double
                                .valueOf(prem_pta.equals("") ? "0"
                                        : prem_pta)))))), small_bold));
                rider_years2.setPadding(5);
                rider_years2
                        .setHorizontalAlignment(Element.ALIGN_CENTER);

				PdfPCell cell_Preferred_term_assured_PremiumPayingTerm1 = new PdfPCell(
						new Paragraph(premium_paying_frequency + " Premium",
								small_normal));
				PdfPCell cell_Preferred_term_assured_PremiumPayingTerm2 = new PdfPCell(
						new Paragraph("Rs. "
                                + getformatedThousandString(Integer.parseInt(obj.getRound(obj.getStringWithout_E(Double
								.valueOf(prem_pta.equals("") ? "0"
										: prem_pta))))), small_bold));
				cell_Preferred_term_assured_PremiumPayingTerm1.setPadding(5);
				cell_Preferred_term_assured_PremiumPayingTerm2.setPadding(5);
				cell_Preferred_term_assured_PremiumPayingTerm2
						.setHorizontalAlignment(Element.ALIGN_CENTER);

				Table_Preferred_term_assured_policyTerm_annualisedPremium_sumAssured
                        .addCell(rider6);
				Table_Preferred_term_assured_policyTerm_annualisedPremium_sumAssured
						.addCell(cell_Preferred_term_assured_tPlanProposed2);

				Table_Preferred_term_assured_policyTerm_annualisedPremium_sumAssured
						.addCell(cell_Preferred_term_assured_Term2);
				Table_Preferred_term_assured_policyTerm_annualisedPremium_sumAssured
                        .addCell(rider_years);
                Table_Preferred_term_assured_policyTerm_annualisedPremium_sumAssured.
                        addCell(rider_years2);


				document.add(Table_Preferred_term_assured_policyTerm_annualisedPremium_sumAssured);
			}
			if (adb_rider_status.equals("true")) {
                PdfPTable Table_AccidetnalDeathBenefitRider_policyTerm_annualisedPremium_sumAssured = new PdfPTable(5);
				Table_AccidetnalDeathBenefitRider_policyTerm_annualisedPremium_sumAssured
						.setWidthPercentage(100);

				PdfPCell cell_AccidetnalDeathBenefitRider_PlanProposed1 = new PdfPCell(
                        new Paragraph("SBI Life - Accidental Death Benefit Rider (UIN:111B015V03)", small_normal));
				cell_AccidetnalDeathBenefitRider_PlanProposed1.setPadding(5);

                PdfPCell cell_AccidetnalDeathBenefitRider_tPlanProposed2 = new PdfPCell(new Paragraph(term_adb + "", small_bold));
				cell_AccidetnalDeathBenefitRider_tPlanProposed2.setPadding(5);
				cell_AccidetnalDeathBenefitRider_tPlanProposed2
						.setHorizontalAlignment(Element.ALIGN_CENTER);

                PdfPCell cell_AccidetnalDeathBenefitRider_Term2 = new PdfPCell(new Paragraph("" + getformatedThousandString(Integer.parseInt(obj.getRound(obj.getStringWithout_E(Double.valueOf(sa_adb.equals("") ? "0" : sa_adb))))), small_bold));
				cell_AccidetnalDeathBenefitRider_Term2.setPadding(5);
                cell_AccidetnalDeathBenefitRider_Term2.setHorizontalAlignment(Element.ALIGN_CENTER);

                PdfPCell cell_AccidetnalDeathBenefitRider_tPlanProposed23;
                if (premiumPaymentMode.equalsIgnoreCase("Single")) {
                    cell_AccidetnalDeathBenefitRider_tPlanProposed23 = new PdfPCell(new Paragraph("1", small_bold));
                    cell_AccidetnalDeathBenefitRider_tPlanProposed23.setPadding(5);
                    cell_AccidetnalDeathBenefitRider_tPlanProposed23.setHorizontalAlignment(Element.ALIGN_CENTER);
                } else {
                    cell_AccidetnalDeathBenefitRider_tPlanProposed23 = new PdfPCell(new Paragraph(term_adb + "", small_bold));
                    cell_AccidetnalDeathBenefitRider_tPlanProposed23.setPadding(5);
                    cell_AccidetnalDeathBenefitRider_tPlanProposed23.setHorizontalAlignment(Element.ALIGN_CENTER);
                }


                PdfPCell rider_years2 = new PdfPCell(new Paragraph(("" + getformatedThousandString(Integer.parseInt(obj.getRound(obj.getStringWithout_E(Double.valueOf(prem_adb.equals("") ? "0" : prem_adb)))))), small_bold));
                rider_years2.setPadding(5);
                rider_years2.setHorizontalAlignment(Element.ALIGN_CENTER);

                PdfPCell cell_AccidetnalDeathBenefitRider_PremiumPayingTerm1 = new PdfPCell(new Paragraph(premium_paying_frequency + " Premium", small_normal));
                PdfPCell cell_AccidetnalDeathBenefitRider_PremiumPayingTerm2 = new PdfPCell(new Paragraph("" + getformatedThousandString(Integer.parseInt(obj.getRound(obj.getStringWithout_E(Double.valueOf(prem_adb.equals("") ? "0" : prem_adb))))), small_bold));
                cell_AccidetnalDeathBenefitRider_PremiumPayingTerm1.setPadding(5);
                cell_AccidetnalDeathBenefitRider_PremiumPayingTerm2.setPadding(5);
                cell_AccidetnalDeathBenefitRider_PremiumPayingTerm2.setHorizontalAlignment(Element.ALIGN_CENTER);

                Table_AccidetnalDeathBenefitRider_policyTerm_annualisedPremium_sumAssured.addCell(cell_AccidetnalDeathBenefitRider_PlanProposed1);
                Table_AccidetnalDeathBenefitRider_policyTerm_annualisedPremium_sumAssured.addCell(cell_AccidetnalDeathBenefitRider_tPlanProposed2);

                Table_AccidetnalDeathBenefitRider_policyTerm_annualisedPremium_sumAssured.addCell(cell_AccidetnalDeathBenefitRider_Term2);
                Table_AccidetnalDeathBenefitRider_policyTerm_annualisedPremium_sumAssured.addCell(cell_AccidetnalDeathBenefitRider_tPlanProposed23);
                Table_AccidetnalDeathBenefitRider_policyTerm_annualisedPremium_sumAssured.addCell(rider_years2);

				document.add(Table_AccidetnalDeathBenefitRider_policyTerm_annualisedPremium_sumAssured);
			}
			if (atpdb_rider_status.equals("true")) {
                PdfPTable Table_AccidetnalAndParmantRider_policyTerm_annualisedPremium_sumAssured = new PdfPTable(5);
                Table_AccidetnalAndParmantRider_policyTerm_annualisedPremium_sumAssured.setWidthPercentage(100);

                PdfPCell cell_AccidetnalAndParmantRider_PlanProposed1 = new PdfPCell(new Paragraph("SBI Life - Accidental Total & Permanent Disability Benefit Rider (UIN:111B016V03)", small_normal));
				cell_AccidetnalAndParmantRider_PlanProposed1.setPadding(5);
                PdfPCell cell_AccidetnalAndParmantRider_tPlanProposed2 = new PdfPCell(new Paragraph(term_atpdb + "", small_bold));
				cell_AccidetnalAndParmantRider_tPlanProposed2.setPadding(5);
				cell_AccidetnalAndParmantRider_tPlanProposed2
						.setHorizontalAlignment(Element.ALIGN_CENTER);

                PdfPCell cell_AccidetnalAndParmantRider_Term2 = new PdfPCell(new Paragraph("" + getformatedThousandString(Integer.parseInt(obj.getRound(obj.getStringWithout_E(Double.valueOf(sa_atpdb.equals("") ? "0" : sa_atpdb))))), small_bold));
				cell_AccidetnalAndParmantRider_Term2.setPadding(5);
                cell_AccidetnalAndParmantRider_Term2.setHorizontalAlignment(Element.ALIGN_CENTER);

                PdfPCell cell_AccidetnalAndParmantRider_tPlanProposed23;
                if (premiumPaymentMode.equalsIgnoreCase("Single")) {
                    cell_AccidetnalAndParmantRider_tPlanProposed23 = new PdfPCell(new Paragraph("1", small_bold));
                    cell_AccidetnalAndParmantRider_tPlanProposed23.setPadding(5);
                    cell_AccidetnalAndParmantRider_tPlanProposed23.setHorizontalAlignment(Element.ALIGN_CENTER);
                } else {
                    cell_AccidetnalAndParmantRider_tPlanProposed23 = new PdfPCell(new Paragraph(term_atpdb + "", small_bold));
                    cell_AccidetnalAndParmantRider_tPlanProposed23.setPadding(5);
                    cell_AccidetnalAndParmantRider_tPlanProposed23.setHorizontalAlignment(Element.ALIGN_CENTER);
                }


                PdfPCell rider_years2 = new PdfPCell(new Paragraph(("" + getformatedThousandString(Integer.parseInt(obj.getRound(obj.getStringWithout_E(Double.valueOf(prem_atpdb.equals("") ? "0" : prem_atpdb)))))), small_bold));
                rider_years2.setPadding(5);
                rider_years2.setHorizontalAlignment(Element.ALIGN_CENTER);

                PdfPCell cell_AccidetnalAndParmantRider_PremiumPayingTerm1 = new PdfPCell(new Paragraph(premium_paying_frequency + " Premium", small_normal));
                PdfPCell cell_AccidetnalAndParmantRider_PremiumPayingTerm2 = new PdfPCell(new Paragraph("" + getformatedThousandString(Integer.parseInt(obj.getRound(obj.getStringWithout_E(Double.valueOf(prem_atpdb.equals("") ? "0" : prem_atpdb))))), small_bold));
				cell_AccidetnalAndParmantRider_PremiumPayingTerm1.setPadding(5);
				cell_AccidetnalAndParmantRider_PremiumPayingTerm2.setPadding(5);
				cell_AccidetnalAndParmantRider_PremiumPayingTerm2
						.setHorizontalAlignment(Element.ALIGN_CENTER);

				Table_AccidetnalAndParmantRider_policyTerm_annualisedPremium_sumAssured
						.addCell(cell_AccidetnalAndParmantRider_PlanProposed1);
				Table_AccidetnalAndParmantRider_policyTerm_annualisedPremium_sumAssured
						.addCell(cell_AccidetnalAndParmantRider_tPlanProposed2);

                Table_AccidetnalAndParmantRider_policyTerm_annualisedPremium_sumAssured.addCell(cell_AccidetnalAndParmantRider_Term2);
                Table_AccidetnalAndParmantRider_policyTerm_annualisedPremium_sumAssured.addCell(cell_AccidetnalAndParmantRider_tPlanProposed23);
                Table_AccidetnalAndParmantRider_policyTerm_annualisedPremium_sumAssured.addCell(rider_years2);

				document.add(Table_AccidetnalAndParmantRider_policyTerm_annualisedPremium_sumAssured);
			}
			document.add(para_img_logo_after_space_1);

			PdfPTable BI_Pdftable_totalPremiumforBaseProduct = new PdfPTable(1);
			BI_Pdftable_totalPremiumforBaseProduct.setWidthPercentage(100);
			PdfPCell BI_Pdftable_totalPremiumforBaseProductcell = new PdfPCell(
					new Paragraph(
							"Total Premium for Base Product & Rider (if any) (in Rs)",
							small_bold));

			BI_Pdftable_totalPremiumforBaseProductcell
					.setHorizontalAlignment(Element.ALIGN_CENTER);
			BI_Pdftable_totalPremiumforBaseProductcell.setPadding(5);

			BI_Pdftable_totalPremiumforBaseProduct
					.addCell(BI_Pdftable_totalPremiumforBaseProductcell);

			PdfPTable FY_SY_premDetail_table = new PdfPTable(4);
			FY_SY_premDetail_table.setWidths(new float[] { 5f, 5f, 5f, 5f});
			FY_SY_premDetail_table.setWidthPercentage(100f);
			FY_SY_premDetail_table.setHorizontalAlignment(Element.ALIGN_LEFT);

			PdfPCell cell;
			cell = new PdfPCell(
					new Phrase(
							"Total Premium for Base Product and Rider (if opted) (in Rs.)",
							small_bold));
			cell.setColspan(7);
			cell.setBorder(Rectangle.BOTTOM);
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);


			cell = new PdfPCell(new Phrase("Policy Year", small_normal));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			FY_SY_premDetail_table.addCell(cell);

			cell = new PdfPCell(new Phrase(shubhNiveshBean.getPremiumFreq()
					+ " Premium  (Rs.)", small_normal));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			FY_SY_premDetail_table.addCell(cell);


			cell = new PdfPCell(new Phrase("Applicable Taxes (Rs.)", small_normal));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			FY_SY_premDetail_table.addCell(cell);

			cell = new PdfPCell(new Phrase(shubhNiveshBean.getPremiumFreq()
					+ " Premium with Applicable Taxes (Rs.)", small_normal));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			FY_SY_premDetail_table.addCell(cell);


			cell = new PdfPCell(new Phrase("First year", small_normal));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			FY_SY_premDetail_table.addCell(cell);

			cell = new PdfPCell(new Phrase(""
					+ commonMethods.getformatedThousandString(Integer.parseInt(obj.getRound(obj.getStringWithout_E(Double
					.valueOf(basicprem.equals("") ? "0" : basicprem))))),
					small_normal));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			FY_SY_premDetail_table.addCell(cell);


			cell = new PdfPCell(
					new Phrase(""
							+ commonMethods.getformatedThousandString(Integer.parseInt(obj.getRound(obj.getStringWithout_E(Double
							.valueOf(servicetax.equals("") ? "0"
									: servicetax))))), small_normal));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			FY_SY_premDetail_table.addCell(cell);

			cell = new PdfPCell(new Phrase(""
					+ commonMethods.getformatedThousandString(Integer.parseInt(obj.getRound(obj.getStringWithout_E(Double
					.valueOf(basicplustax.equals("") ? "0"
							: basicplustax))))), small_normal));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			FY_SY_premDetail_table.addCell(cell);

			// 4 row
			if (!shubhNiveshBean.getPremiumFreq().equalsIgnoreCase("Single")) {
				cell = new PdfPCell(new Phrase("Second year onwards",
						small_normal));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				FY_SY_premDetail_table.addCell(cell);

				cell = new PdfPCell(
						new Phrase(""
								+ commonMethods.getformatedThousandString(Integer.parseInt(obj.getRound(obj.getStringWithout_E(Double
								.valueOf(basicprem.equals("") ? "0"
										: basicprem))))), small_normal));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				FY_SY_premDetail_table.addCell(cell);


				// cell = new PdfPCell(
				// new Phrase(
				// obj.getRound(obj.getStringWithout_E(Double
				// .valueOf(SBCServiceTaxSecondYear
				// .equals("") ? "0"
				// : SBCServiceTaxSecondYear))),
				// small_normal));
				// cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				// FY_SY_premDetail_table.addCell(cell);
				//
				// cell = new PdfPCell(
				// new Phrase(
				// obj.getRound(obj.getStringWithout_E(Double
				// .valueOf(KKCServiceTaxSecondYear
				// .equals("") ? "0"
				// : KKCServiceTaxSecondYear))),
				// small_normal));
				// cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				// FY_SY_premDetail_table.addCell(cell);

				cell = new PdfPCell(new Phrase(""
						+ commonMethods.getformatedThousandString(Integer.parseInt(obj.getRound(obj
						.getStringWithout_E(Double.valueOf(servcTaxSecondYear
								.equals("") ? "0" : servcTaxSecondYear))))),
						small_normal));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				FY_SY_premDetail_table.addCell(cell);

				cell = new PdfPCell(new Phrase(""
						+ commonMethods.getformatedThousandString(Integer.parseInt(obj.getRound(obj
						.getStringWithout_E(Double.valueOf(premWthSTSecondYear
								.equals("") ? "0" : premWthSTSecondYear))))),
						small_normal));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				FY_SY_premDetail_table.addCell(cell);
			}

            //document.add(FY_SY_premDetail_table);

            PdfPTable FY_SY_premDetail_table2 = new PdfPTable(4);
            FY_SY_premDetail_table2.setWidths(new float[]{5f, 5f, 5f, 5f});
            FY_SY_premDetail_table2.setWidthPercentage(100f);
            FY_SY_premDetail_table2.setHorizontalAlignment(Element.ALIGN_LEFT);

            PdfPCell mycell;

            mycell = new PdfPCell(
                    new Phrase(
                            "Premium Summary",
                            small_bold));
            mycell.setColspan(7);
            mycell.setBorder(Rectangle.BOTTOM);
            mycell.setHorizontalAlignment(Element.ALIGN_LEFT);
            FY_SY_premDetail_table2.addCell(mycell);


            mycell = new PdfPCell(new Phrase("", small_normal));
            mycell.setHorizontalAlignment(Element.ALIGN_CENTER);
            FY_SY_premDetail_table2.addCell(mycell);

            mycell = new PdfPCell(new Phrase("Base Plan (Rs.)", small_normal));
            mycell.setHorizontalAlignment(Element.ALIGN_CENTER);
            FY_SY_premDetail_table2.addCell(mycell);

            mycell = new PdfPCell(new Phrase("Riders (Rs.)", small_normal));
            mycell.setHorizontalAlignment(Element.ALIGN_CENTER);
            FY_SY_premDetail_table2.addCell(mycell);

            mycell = new PdfPCell(new Phrase("Total Installment Premium (Rs.)", small_normal));
            mycell.setHorizontalAlignment(Element.ALIGN_CENTER);
            FY_SY_premDetail_table2.addCell(mycell);

            basicprem = prsObj.parseXmlTag(output, "installmntPrem");
            premInstFYRider = prsObj.parseXmlTag(output, "premInstFYRider");
            basicplustax = prsObj.parseXmlTag(output, "installmntPremWithSerTx");
            premInstSYRider = prsObj.parseXmlTag(output, "premInstSYRider");
            premWthSTSecondYear = prsObj.parseXmlTag(output, "installmntPremWithSerTxSecondYear");


            mycell = new PdfPCell(new Phrase("Installment Premium without Applicable Taxes (Rs.)", small_normal));
            mycell.setHorizontalAlignment(Element.ALIGN_CENTER);
            FY_SY_premDetail_table2.addCell(mycell);

            mycell = new PdfPCell(new Phrase((getformatedThousandString(Integer.parseInt(obj.getRound(obj.getStringWithout_E(Double.valueOf((premium
                    .equals("") || premium == null) ? "0" : premium)))))), small_normal));
            mycell.setHorizontalAlignment(Element.ALIGN_CENTER);
            FY_SY_premDetail_table2.addCell(mycell);

            mycell = new PdfPCell(new Phrase((getformatedThousandString(Integer.parseInt(obj.getRound(obj.getStringWithout_E(Double.valueOf((premiumSingleInstBasicRider
                    .equals("") || premiumSingleInstBasicRider == null) ? "0" : premiumSingleInstBasicRider)))))), small_normal));
            mycell.setHorizontalAlignment(Element.ALIGN_CENTER);
            FY_SY_premDetail_table2.addCell(mycell);

            mycell = new PdfPCell(new Phrase((getformatedThousandString(Integer.parseInt(obj.getRound(obj.getStringWithout_E(Double.valueOf((basicprem
                    .equals("") || basicprem == null) ? "0" : basicprem)))))), small_normal));
            mycell.setHorizontalAlignment(Element.ALIGN_CENTER);
            FY_SY_premDetail_table2.addCell(mycell);

            mycell = new PdfPCell(new Phrase("Installment Premium with Applicable Taxes : 1st Year (Rs.)", small_normal));
            mycell.setHorizontalAlignment(Element.ALIGN_CENTER);
            FY_SY_premDetail_table2.addCell(mycell);

            mycell = new PdfPCell(new Phrase((""
                    + getformatedThousandString(Integer.parseInt(obj.getRound(obj.getStringWithout_E(Double.valueOf(premInstBasicFirstYear
                    .equals("") ? "0" : premInstBasicFirstYear)))))), small_normal));
            mycell.setHorizontalAlignment(Element.ALIGN_CENTER);
            FY_SY_premDetail_table2.addCell(mycell);

            mycell = new PdfPCell(new Phrase((
                    getformatedThousandString(Integer.parseInt(obj.getRound(obj.getStringWithout_E(Double.valueOf((premInstFYRider
                            .equals("") || premInstFYRider == null) ? "0" : premInstFYRider)))))), small_normal));
            mycell.setHorizontalAlignment(Element.ALIGN_CENTER);
            FY_SY_premDetail_table2.addCell(mycell);

            mycell = new PdfPCell(new Phrase((""
                    + getformatedThousandString(Integer.parseInt(obj.getRound(obj.getStringWithout_E(Double
                    .valueOf(basicplustax.equals("") ? "0"
                            : basicplustax)))))), small_normal));
            mycell.setHorizontalAlignment(Element.ALIGN_CENTER);
            FY_SY_premDetail_table2.addCell(mycell);

            mycell = new PdfPCell(new Phrase("Installment Premium with Applicable Taxes : 2nd Year onwards (Rs.)", small_normal));
            mycell.setHorizontalAlignment(Element.ALIGN_CENTER);
            FY_SY_premDetail_table2.addCell(mycell);


            if (!shubhNiveshBean.getPremiumFreq().equalsIgnoreCase("Single")) {

                mycell = new PdfPCell(new Phrase((""
                        + getformatedThousandString(Integer.parseInt(obj.getRound(obj.getStringWithout_E(Double
                        .valueOf(premInstBasicSecondYear.equals("") ? "0" : premInstBasicSecondYear)))))), small_normal));
                mycell.setHorizontalAlignment(Element.ALIGN_CENTER);
                FY_SY_premDetail_table2.addCell(mycell);

                mycell = new PdfPCell(new Phrase((
                        getformatedThousandString(Integer.parseInt(obj.getRound(obj.getStringWithout_E(Double.valueOf((premInstSYRider
                                .equals("") || premInstSYRider == null) ? "0" : premInstSYRider)))))), small_normal));
                mycell.setHorizontalAlignment(Element.ALIGN_CENTER);
                FY_SY_premDetail_table2.addCell(mycell);

                mycell = new PdfPCell(new Phrase((""
                        + getformatedThousandString(Integer.parseInt(obj.getRound(obj.getStringWithout_E(Double
                        .valueOf(premWthSTSecondYear.equals("") ? "0"
                                : premWthSTSecondYear)))))), small_normal));
                mycell.setHorizontalAlignment(Element.ALIGN_CENTER);
                FY_SY_premDetail_table2.addCell(mycell);
            } else {
                mycell = new PdfPCell(new Phrase("NA", small_normal));
                mycell.setHorizontalAlignment(Element.ALIGN_CENTER);
                FY_SY_premDetail_table2.addCell(mycell);

                mycell = new PdfPCell(new Phrase("NA", small_normal));
                mycell.setHorizontalAlignment(Element.ALIGN_CENTER);
                FY_SY_premDetail_table2.addCell(mycell);

                mycell = new PdfPCell(new Phrase("NA", small_normal));
                mycell.setHorizontalAlignment(Element.ALIGN_CENTER);
                FY_SY_premDetail_table2.addCell(mycell);
            }


            mycell = new PdfPCell(new Phrase((""
                    + getformatedThousandString(Integer.parseInt(obj.getRound(obj.getStringWithout_E(Double
                    .valueOf(premWthSTSecondYear.equals("") ? "0"
                            : premWthSTSecondYear)))))), small_normal));
            mycell.setHorizontalAlignment(Element.ALIGN_CENTER);
            FY_SY_premDetail_table2.addCell(mycell);

            document.add(FY_SY_premDetail_table2);
			document.add(para_img_logo_after_space_1);
			/** Added By - Priyanka Warekar - 26-08-2015 - End ****/

			PdfPTable Table_backdating_premium_with_service_tax = new PdfPTable(
					4);
			Table_backdating_premium_with_service_tax.setWidthPercentage(100);

			PdfPCell cell_Backdate1 = new PdfPCell(new Paragraph(
					"Backdating Interest", small_normal));
			cell_Backdate1.setPadding(5);
			PdfPCell cell_Backdate2 = new PdfPCell(new Paragraph("Rs. "
					+ commonMethods.getformatedThousandString(Integer.parseInt(obj.getRound(obj.getStringWithout_E(Double
					.valueOf(BackdatingInt.equals("") ? "0"
							: BackdatingInt))))), small_bold));
			cell_Backdate2.setPadding(5);
			cell_Backdate2.setHorizontalAlignment(Element.ALIGN_CENTER);

			PdfPCell cell_AccidetnalAndParmantRider_YearlyPremium1 = new PdfPCell(
					new Paragraph(premium_paying_frequency
							+ "  Premium with Applicable Taxes", small_normal));
			PdfPCell cell_AccidetnalAndParmantRider_YearlyPremium2 = new PdfPCell(
					new Paragraph("Rs. "
							+ getformatedThousandString(Integer.parseInt(obj.getRound(obj.getStringWithout_E(Double
							.valueOf(basicplustax.equals("") ? "0"
									: basicplustax))))), small_bold));
			cell_AccidetnalAndParmantRider_YearlyPremium1.setPadding(5);
			cell_AccidetnalAndParmantRider_YearlyPremium2.setPadding(5);
			cell_AccidetnalAndParmantRider_YearlyPremium2
					.setHorizontalAlignment(Element.ALIGN_CENTER);

			Table_backdating_premium_with_service_tax.addCell(cell_Backdate1);
			Table_backdating_premium_with_service_tax.addCell(cell_Backdate2);

			Table_backdating_premium_with_service_tax
					.addCell(cell_AccidetnalAndParmantRider_YearlyPremium1);
			Table_backdating_premium_with_service_tax
					.addCell(cell_AccidetnalAndParmantRider_YearlyPremium2);
            // document.add(Table_backdating_premium_with_service_tax);

			document.add(para_img_logo_after_space_1);

			PdfPTable BI_Pdftable_note = new PdfPTable(1);
			BI_Pdftable_note.setWidthPercentage(100);
			PdfPCell BI_Pdftable_note_cell1 = new PdfPCell(new Paragraph(
                    "Please Note:", small_bold));
            BI_Pdftable_note_cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
			BI_Pdftable_note_cell1.setPadding(5);

			BI_Pdftable_note.addCell(BI_Pdftable_note_cell1);
			document.add(BI_Pdftable_note);

			PdfPTable BI_Pdftable6 = new PdfPTable(1);
			BI_Pdftable6.setWidthPercentage(100);
			PdfPCell BI_Pdftable6_cell6 = new PdfPCell(
					new Paragraph(
                            "1. The premiums can be also paid by giving standing instruction to your bank or you can pay through your credit card."

							, small_normal));

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

			PdfPTable BI_Pdftable8 = new PdfPTable(1);
			BI_Pdftable8.setWidthPercentage(100);
			PdfPCell BI_Pdftable8_cell1 = new PdfPCell(
					new Paragraph(
							"3.Tax deduction under Section 80 C is available. However in case the premium paid during the financial year, exceeds 10% of the sum assured, the benefit will be limited up to 10% of the sum assured.Tax exemption under Section 10(10D) is available at the time of maturity/surrender, subject to the premium not exceeding 10% of the sum assured in any of the years during the term of the policy. However, death proceeds are completely exempt.",
							small_normal));

			BI_Pdftable8_cell1.setPadding(5);

			BI_Pdftable8.addCell(BI_Pdftable8_cell1);
            //document.add(BI_Pdftable8);

			PdfPTable taxes_desc = new PdfPTable(1);
			taxes_desc.setWidthPercentage(100);
			PdfPCell taxes_desc_cell = new PdfPCell(
					new Paragraph(
							"4) Applicable Taxes (including surcharge/cess etc), at the rate notified by the Central Government/ State Government / Union Territories of India from time to time and as per the provisions of the prevalent tax laws will be payable on premium/ or any other charges as per the product features. ",

							small_normal));

			taxes_desc_cell.setPadding(5);

			taxes_desc.addCell(taxes_desc_cell);
            //document.add(taxes_desc);

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
							"1. The benefit calculation is based on the age herein indicated and as applicable for healthy individual."

							, small_normal));

			BI_PdftableOtherTermCondition1_cell6.setPadding(5);

			BI_PdftableOtherTermCondition1
					.addCell(BI_PdftableOtherTermCondition1_cell6);
            // document.add(BI_PdftableOtherTermCondition1);

			PdfPTable BI_PdftableOtherTermCondition2 = new PdfPTable(1);
			BI_PdftableOtherTermCondition2.setWidthPercentage(100);
			PdfPCell BI_PdftableOtherTermCondition2_cell1 = new PdfPCell(
					new Paragraph(
							"2.The above BI is subject to payment of stipulated premiums on due date.",
							small_normal));

			BI_PdftableOtherTermCondition2_cell1.setPadding(5);

			BI_PdftableOtherTermCondition2
					.addCell(BI_PdftableOtherTermCondition2_cell1);
            //document.add(BI_PdftableOtherTermCondition2);

//			PdfPTable BI_PdftableOtherTermCondition3 = new PdfPTable(1);
//			BI_PdftableOtherTermCondition3.setWidthPercentage(100);
//			PdfPCell BI_PdftableOtherTermCondition3_cell1 = new PdfPCell(
//					new Paragraph(
//							"3. Insurance is subject matter of solicitation.",
//							small_normal));
//
//			BI_PdftableOtherTermCondition3_cell1.setPadding(5);
//
//			BI_PdftableOtherTermCondition3
//					.addCell(BI_PdftableOtherTermCondition3_cell1);
//			document.add(BI_PdftableOtherTermCondition3);

			PdfPTable BI_PdftableOtherTermCondition4 = new PdfPTable(1);
			BI_PdftableOtherTermCondition4.setWidthPercentage(100);
			PdfPCell BI_PdftableOtherTermCondition4_cell1 = new PdfPCell(
					new Paragraph(
							"3. If riders are applicable, please refer to specific rider benefit.",
							small_normal));

			BI_PdftableOtherTermCondition4_cell1.setPadding(5);

			BI_PdftableOtherTermCondition4
					.addCell(BI_PdftableOtherTermCondition4_cell1);
            //document.add(BI_PdftableOtherTermCondition4);

			/*PdfPTable BI_PdftableOtherTermCondition5 = new PdfPTable(1);
			BI_PdftableOtherTermCondition5.setWidthPercentage(100);
			PdfPCell BI_PdftableOtherTermCondition5_cell1 = new PdfPCell(
					new Paragraph(
							"4. I hereby declare that the product features and the benefits have been fully and thoroughly explained to me along with benefit illustrations and I have fully understood the same.",
							small_normal));

			BI_PdftableOtherTermCondition5_cell1.setPadding(5);

			BI_PdftableOtherTermCondition5
					.addCell(BI_PdftableOtherTermCondition5_cell1);
			document.add(BI_PdftableOtherTermCondition5);*/

			document.add(para_img_logo_after_space_1);


            PdfPTable table1 = new PdfPTable(17);
            table1.setWidths(new float[]{2f, 5f, 5f, 5f, 5f, 5f, 5f, 5f, 5f,
                    5f, 5f, 5f, 5f, 5f, 5f, 5f, 5f});
            table1.setWidthPercentage(100);

            // 1st row
            cell = new PdfPCell(
                    new Phrase(
                            "BENEFIT ILLUSTRATION FOR SBI LIFE - Shubh Nivesh (Amount in Rs.)",
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
            if (shubhNiveshBean.getPremiumFreq().equalsIgnoreCase("Single")) {
                cell = new PdfPCell(new Phrase(
                        "Single", normal_bold));
            } else {
                cell = new PdfPCell(new Phrase(
                        "Annualized premium", normal_bold));
            }


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
            cell = new PdfPCell(new Phrase("Total benefits Including Guaranteed and Non-Guaranteed benefits", normal_bold));
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
            cell = new PdfPCell(new Phrase("Reversionary bonus", normal_bold));
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

            cell = new PdfPCell(new Phrase("Reversionary bonus", normal_bold));
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


            cell = new PdfPCell(new Phrase("1", normal));
            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                    | Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table1.addCell(cell);

            cell = new PdfPCell(new Phrase(
                    "2", normal));
            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                    | Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table1.addCell(cell);


            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell = new PdfPCell(new Phrase("3", normal));
            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                    | Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table1.addCell(cell);


            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell = new PdfPCell(new Phrase("4", normal));
            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                    | Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table1.addCell(cell);


            cell = new PdfPCell(new Phrase("5", normal));
            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                    | Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table1.addCell(cell);


            cell = new PdfPCell(new Phrase("6", normal));
            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                    | Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table1.addCell(cell);


            cell = new PdfPCell(new Phrase("7", normal));
            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                    | Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table1.addCell(cell);


            cell = new PdfPCell(new Phrase("8",
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
            cell = new PdfPCell(new Phrase("9", normal));
            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                    | Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table1.addCell(cell);


            cell = new PdfPCell(new Phrase("10",
                    normal));
            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                    | Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table1.addCell(cell);


            cell = new PdfPCell(new Phrase("11",
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
            cell = new PdfPCell(new Phrase("12", normal));
            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                    | Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table1.addCell(cell);


            cell = new PdfPCell(new Phrase("13",
                    normal));
            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                    | Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table1.addCell(cell);


            cell = new PdfPCell(new Phrase("14",
                    normal));
            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                    | Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table1.addCell(cell);


            cell = new PdfPCell(new Phrase("15",
                    normal));
            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                    | Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table1.addCell(cell);


            cell = new PdfPCell(new Phrase("16",
                    normal));
            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                    | Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table1.addCell(cell);
            cell = new PdfPCell(new Phrase("17",
                    normal));
            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT
                    | Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
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


			Paragraph para_note = new Paragraph(
					"**Benefit payable to the nominee on death", small_normal);
            // document.add(para_note);
			document.add(para_img_logo_after_space_1);

			if (plan.equals("Endowment Option")) {
				PdfPTable BI_Pdftable_EndowmentOption = new PdfPTable(1);
				BI_Pdftable_EndowmentOption.setWidthPercentage(100);
				PdfPCell BI_Pdftable_EndowmentOption_cell1 = new PdfPCell(
						new Paragraph("Endowment Option", small_bold));
				BI_Pdftable_EndowmentOption_cell1
						.setHorizontalAlignment(Element.ALIGN_CENTER);
				BI_Pdftable_EndowmentOption_cell1.setPadding(5);

				BI_Pdftable_EndowmentOption
						.addCell(BI_Pdftable_EndowmentOption_cell1);
                // document.add(BI_Pdftable_EndowmentOption);

				PdfPTable BI_Pdftable_EndowmentOption1 = new PdfPTable(1);
				BI_Pdftable_EndowmentOption1.setWidthPercentage(100);
				PdfPCell BI_Pdftable_EndowmentOption_cell2 = new PdfPCell(
						new Paragraph(
								"1. For Regular Premium: Higher of i) Sum Assured on death# + Vested Simple Reversionary Bonuses + Terminal bonus, if any. OR ii) 105% of all the basic premiums paid."

								, small_normal));

				BI_Pdftable_EndowmentOption_cell2.setPadding(5);

				BI_Pdftable_EndowmentOption1
						.addCell(BI_Pdftable_EndowmentOption_cell2);
                //document.add(BI_Pdftable_EndowmentOption1);

				PdfPTable BI_Pdftable_EndowmentOption2 = new PdfPTable(1);
				BI_Pdftable_EndowmentOption2.setWidthPercentage(100);
				PdfPCell BI_Pdftable_EndowmentOption2_cell2 = new PdfPCell(
						new Paragraph(
								"2. For Single Premium: Sum Assured on death# + Vested Simple Reversionary Bonuses + Terminal bonus, if any."

								, small_normal));

				BI_Pdftable_EndowmentOption2_cell2.setPadding(5);

				BI_Pdftable_EndowmentOption2
						.addCell(BI_Pdftable_EndowmentOption2_cell2);
                // document.add(BI_Pdftable_EndowmentOption2);
			} else if (plan.equals("Endowment with Whole Life Option")) {
				PdfPTable BI_Pdftable_EndowmentOption = new PdfPTable(1);
				BI_Pdftable_EndowmentOption.setWidthPercentage(100);
				PdfPCell BI_Pdftable_EndowmentOption_cell1 = new PdfPCell(
						new Paragraph("Endowment with Whole Life Option",
								small_bold));
				BI_Pdftable_EndowmentOption_cell1
						.setHorizontalAlignment(Element.ALIGN_CENTER);
				BI_Pdftable_EndowmentOption_cell1.setPadding(5);

				BI_Pdftable_EndowmentOption
						.addCell(BI_Pdftable_EndowmentOption_cell1);
                // document.add(BI_Pdftable_EndowmentOption);

				PdfPTable BI_Pdftable_EndowmentOption1 = new PdfPTable(1);
				BI_Pdftable_EndowmentOption1.setWidthPercentage(100);
				PdfPCell BI_Pdftable_EndowmentOption_cell2 = new PdfPCell(
						new Paragraph(
								"1. Death before completion of the endowment term: same as for Endowment Option for Single Premium: Sum Assured on death# + Vested Simple Reversionary Bonuses + Terminal bonus, if any. and for Regular Premium: Higher of i) Sum Assured on death# + Vested Simple Reversionary Bonuses + Terminal bonus, if any. OR ii) 105% of all the basic premiums paid.",
								small_normal));

				BI_Pdftable_EndowmentOption_cell2.setPadding(5);

				BI_Pdftable_EndowmentOption1
						.addCell(BI_Pdftable_EndowmentOption_cell2);
                // document.add(BI_Pdftable_EndowmentOption1);

				PdfPTable BI_Pdftable_EndowmentOption2 = new PdfPTable(1);
				BI_Pdftable_EndowmentOption2.setWidthPercentage(100);
				PdfPCell BI_Pdftable_EndowmentOption2_cell2 = new PdfPCell(
						new Paragraph(
								"2. Death after completion of the endowment term and up to 100 years of age: Basic Sum Assured",
								small_normal));

				BI_Pdftable_EndowmentOption2_cell2.setPadding(5);

				BI_Pdftable_EndowmentOption2
						.addCell(BI_Pdftable_EndowmentOption2_cell2);
                // document.add(BI_Pdftable_EndowmentOption2);
			}
            PdfPTable Notes = new PdfPTable(1);
            Notes.setWidthPercentage(100);
            PdfPCell notes1 = new PdfPCell(
                    new Paragraph("Notes :", small_bold));
            notes1.setHorizontalAlignment(Element.ALIGN_LEFT);
            notes1.setPadding(5);

            PdfPCell notes2 = new PdfPCell(
                    new Paragraph("1.Annualized premium shall be the premium amount payable in a year chosen by the policyholder, excluding the taxes,  rider premiums, underwriting extra premiums and loading for modal premiums, if any / Single premium shall be the premium amount payable in lump sum at  inception of the policy as chosen by the policyholder, excluding the taxes, rider premiums and underwriting extra premiums, if any. Refer sales literature for explanation of terms used in this illustration.", small_normal));
            notes2.setHorizontalAlignment(Element.ALIGN_LEFT);
            notes2.setPadding(5);

            PdfPCell notes3 = new PdfPCell(
                    new Paragraph("2. All Benefit amount are derived on the assumption that the policies are ''in-force''", small_normal));
            notes3.setHorizontalAlignment(Element.ALIGN_LEFT);
            notes3.setPadding(5);

            PdfPCell notes4 = new PdfPCell(
                    new Paragraph("3. The above BI is subject to payment of stipulated premiums on due date.", small_normal));
            notes4.setHorizontalAlignment(Element.ALIGN_LEFT);
            notes4.setPadding(5);

            PdfPCell notes5 = new PdfPCell(
                    new Paragraph("4. If riders are applicable, please refer to specific rider benefit. For more details, refer to the concerned rider document.", small_normal));
            notes5.setHorizontalAlignment(Element.ALIGN_LEFT);
            notes5.setPadding(5);

            PdfPCell notes6 = new PdfPCell(
                    new Paragraph("5. In addition to Guaranteed Surrender Benefits (column 5), Surrender value of the vested bonuses will also be paid. For the purpose of guaranteed surrender value (GSV) in this illustration the surrender value of vested bonuses are not considered at all.", small_normal));
            notes6.setHorizontalAlignment(Element.ALIGN_LEFT);
            notes6.setPadding(5);

            Notes.addCell(notes1);
            Notes.addCell(notes2);
            Notes.addCell(notes3);
            Notes.addCell(notes4);
            Notes.addCell(notes5);
            Notes.addCell(notes6);
            document.add(Notes);


			Paragraph para_EndowmentOption = new Paragraph(
					"#For details on Sum Assured on death please refer the Sales Brochure",
					small_normal);
            //document.add(para_EndowmentOption);

            //document.add(para_img_logo_after_space_1);

			PdfPTable BI_Pdftable_BonusRates = new PdfPTable(1);
			BI_Pdftable_BonusRates.setWidthPercentage(100);
			PdfPCell BI_Pdftable_BonusRates_cell1 = new PdfPCell(new Paragraph(
					"Bonus Rates", small_bold));
			BI_Pdftable_BonusRates_cell1
                    .setHorizontalAlignment(Element.ALIGN_LEFT);
			BI_Pdftable_BonusRates_cell1.setPadding(5);

			BI_Pdftable_BonusRates.addCell(BI_Pdftable_BonusRates_cell1);
			document.add(BI_Pdftable_BonusRates);

			PdfPTable BI_Pdftable_BonusRates1 = new PdfPTable(1);
			BI_Pdftable_BonusRates1.setWidthPercentage(100);
			PdfPCell BI_Pdftable_BonusRates_cell2 = new PdfPCell(
					new Paragraph(
                            "This is a with profit plan and participates in the profits of the company’s life insurance business. Simple reversionary bonuses are declared as a percentage rate, which apply to the sum assured of the basic policy. Terminal bonuses, if any, are declared as a percentage rate, which apply to the vested reversionary bonus. The bonus rates in the benefit illustration are constant. However, in practice, future bonuses are not guaranteed and will depend on future profits. Therefore, the bonuses are shown as non-guaranteed benefits and are calculated so that they are consistent with the two projected investment return assumptions of 4% and 8% per annum."

							, small_normal));

			BI_Pdftable_BonusRates_cell2.setPadding(5);

			BI_Pdftable_BonusRates1.addCell(BI_Pdftable_BonusRates_cell2);
			document.add(BI_Pdftable_BonusRates1);

			PdfPTable BI_Pdftable_BonusRates2 = new PdfPTable(1);
			BI_Pdftable_BonusRates2.setWidthPercentage(100);
			PdfPCell BI_Pdftable_BonusRates2_cell2 = new PdfPCell(
					new Paragraph(
                            "Accordingly, for the purpose of guaranteed surrender value benefits (GSV) in this illustration, the cash values of vested bonuses are not considered at all."

							, small_normal));

			BI_Pdftable_BonusRates2_cell2.setPadding(5);

			BI_Pdftable_BonusRates2.addCell(BI_Pdftable_BonusRates2_cell2);
            // document.add(BI_Pdftable_BonusRates2);

			PdfPTable BI_Pdftable_BonusRates3 = new PdfPTable(1);
			BI_Pdftable_BonusRates3.setWidthPercentage(100);
			PdfPCell BI_Pdftable_BonusRates3_cell2 = new PdfPCell(
					new Paragraph(
							"3). The bonus rates in the benefit illustration are constant. However, in practice, future bonuses are not guaranteed and will depend on future profits. Therefore, the bonuses are shown as non-guaranteed benefits and are calculated so that they are consistent with the two projected investment return assumptions of 4% and 8% per annum."

							, small_normal));

			BI_Pdftable_BonusRates3_cell2.setPadding(5);

			BI_Pdftable_BonusRates3.addCell(BI_Pdftable_BonusRates3_cell2);
            // document.add(BI_Pdftable_BonusRates3);

			PdfPTable BI_Pdftable_BonusRates4 = new PdfPTable(1);
			BI_Pdftable_BonusRates4.setWidthPercentage(100);
			PdfPCell BI_Pdftable_BonusRates4_cell2 = new PdfPCell(
					new Paragraph(
                            "4). Accordingly, for the purpose of guaranteed gurrender value (GSV) in this illustration, the cash value of vested bonuses are not considered at all."

							, small_normal));

			BI_Pdftable_BonusRates4_cell2.setPadding(5);

			BI_Pdftable_BonusRates4.addCell(BI_Pdftable_BonusRates4_cell2);
            //document.add(BI_Pdftable_BonusRates4);

			document.add(para_img_logo_after_space_1);

			PdfPTable BI_Pdftable_SurrenderValue = new PdfPTable(1);
			BI_Pdftable_SurrenderValue.setWidthPercentage(100);
			PdfPCell BI_Pdftable_SurrenderValue_cell = new PdfPCell(
					new Paragraph("Surrender Value:- Surrender value is available for the basic policy benefits and not for the rider benefits.", small_normal));

			BI_Pdftable_SurrenderValue_cell.setPadding(5);

			BI_Pdftable_SurrenderValue.addCell(BI_Pdftable_SurrenderValue_cell);
            //document.add(BI_Pdftable_SurrenderValue);

            //document.add(para_img_logo_after_space_1);

			PdfPTable BI_Pdftable_GuaranteedSurrenderValue = new PdfPTable(1);
			BI_Pdftable_GuaranteedSurrenderValue.setWidthPercentage(100);
			PdfPCell BI_Pdftable_GuaranteedSurrenderValue_cell1 = new PdfPCell(
					new Paragraph("Guaranteed Surrender Value", small_bold));
			BI_Pdftable_GuaranteedSurrenderValue_cell1
					.setHorizontalAlignment(Element.ALIGN_CENTER);
			BI_Pdftable_GuaranteedSurrenderValue_cell1.setPadding(5);

			BI_Pdftable_GuaranteedSurrenderValue
					.addCell(BI_Pdftable_GuaranteedSurrenderValue_cell1);
            //document.add(BI_Pdftable_GuaranteedSurrenderValue);

			PdfPTable BI_Pdftable_RegularPremiumPolicies = new PdfPTable(1);
			BI_Pdftable_RegularPremiumPolicies.setWidthPercentage(100);
			PdfPCell BI_Pdftable_RegularPremiumPolicies_cell = new PdfPCell(
					new Paragraph(
							"1) For Regular Premium Policies:-  The policy will acquire a paid-up and/or surrender value only if premiums have been paid for at least 2 full years for policy term less than 10 years and at least 3 full years for policy term 10 years or more.The Guaranteed Surrender Value (GSV) ) in case of regular premium policies will be equal to GSV factors multiplied by the basic premiums paid. Basic premium is equal to total premium less Applicable Taxes, underwriting extra premiums, extra premium due to modal factors and rider premiums, if any."

							, small_normal));

			BI_Pdftable_RegularPremiumPolicies_cell.setPadding(5);

			BI_Pdftable_RegularPremiumPolicies
					.addCell(BI_Pdftable_RegularPremiumPolicies_cell);
            //document.add(BI_Pdftable_RegularPremiumPolicies);

			PdfPTable BI_Pdftable_SinglePremiumPolicies = new PdfPTable(1);
			BI_Pdftable_SinglePremiumPolicies.setWidthPercentage(100);
			PdfPCell BI_Pdftable_SinglePremiumPolicies_cell = new PdfPCell(
					new Paragraph(
							"2) For Single Premium Policies:-  The policy acquires surrender value after date of commencement of risk.For first three policy years, the Guaranteed Surrender Value (GSV) will be 70% of Single Premium (exclusive of Applicable Taxes) paid excluding extra premiums (underwriting extra) and rider premiums, if any, plus cash value of the allocated bonuses.From fourth policy year onwards, the Guaranteed Surrender Value (GSV) will be 90% of Single Premium (exclusive of Applicable Taxes) paid excluding extra premiums (underwriting extra) and rider premiums, if any, plus cash value of the allocated bonuses."

							, small_normal));

			BI_Pdftable_SinglePremiumPolicies_cell.setPadding(5);

			BI_Pdftable_SinglePremiumPolicies
					.addCell(BI_Pdftable_SinglePremiumPolicies_cell);
            //document.add(BI_Pdftable_SinglePremiumPolicies);

            //  document.add(para_img_logo_after_space_1);

			PdfPTable BI_Pdftable_CompanysPolicySurrender = new PdfPTable(1);
			BI_Pdftable_CompanysPolicySurrender.setWidthPercentage(100);
			PdfPCell BI_Pdftable_CompanysPolicySurrender_cell1 = new PdfPCell(
                    new Paragraph("Important :", small_bold));
			BI_Pdftable_CompanysPolicySurrender_cell1
                    .setHorizontalAlignment(Element.ALIGN_LEFT);
			BI_Pdftable_CompanysPolicySurrender_cell1.setPadding(5);

			BI_Pdftable_CompanysPolicySurrender
					.addCell(BI_Pdftable_CompanysPolicySurrender_cell1);
			document.add(BI_Pdftable_CompanysPolicySurrender);

			PdfPTable BI_Pdftable_CompanysPolicySurrender1 = new PdfPTable(1);
			BI_Pdftable_CompanysPolicySurrender1.setWidthPercentage(100);
			PdfPCell BI_Pdftable_CompanysPolicySurrender1_cell = new PdfPCell(
					new Paragraph(
                            "You may receive a Welcome Call from our representative to confirm your proposal details like Date of Birth, Nominee Name, Address, Email ID, Sum Assured, Premium amount, Premium Payment Term etc."

							, small_normal));

			BI_Pdftable_CompanysPolicySurrender1_cell.setPadding(5);

			BI_Pdftable_CompanysPolicySurrender1_cell
                    .setHorizontalAlignment(Element.ALIGN_LEFT);
			BI_Pdftable_CompanysPolicySurrender1
					.addCell(BI_Pdftable_CompanysPolicySurrender1_cell);
			document.add(BI_Pdftable_CompanysPolicySurrender1);

			PdfPTable BI_Pdftable_CompanysPolicySurrender2 = new PdfPTable(1);
			BI_Pdftable_CompanysPolicySurrender2.setWidthPercentage(100);
			PdfPCell BI_Pdftable_CompanysPolicySurrender2_cell = new PdfPCell(
					new Paragraph(
                            "You may have to undergo Medical tests based on our underwriting requirements.",
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
            //document.add(BI_Pdftable_CompanysPolicySurrender3);


			if(premium_paying_frequency.equalsIgnoreCase("Single")) {

				PdfPTable BI_Pdftable_CompanysPolicySurrender4 = new PdfPTable(1);
				BI_Pdftable_CompanysPolicySurrender4.setWidthPercentage(100);
				PdfPCell BI_Pdftable_CompanysPolicySurrender4_cell = new PdfPCell(
						new Paragraph(
								"You have to submit Proof of source of Fund.",
								small_normal));

				BI_Pdftable_CompanysPolicySurrender4_cell.setPadding(5);

				BI_Pdftable_CompanysPolicySurrender4
						.addCell(BI_Pdftable_CompanysPolicySurrender4_cell);
                //document.add(BI_Pdftable_CompanysPolicySurrender4);
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
			}
			else{
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

				BI_PdftableMarketing.addCell(BI_PdftableMarketing_signature_cell);
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

				byte[] fbyt_agent = Base64.decode(agent_sign, 0);
				Bitmap Agentbitmap = BitmapFactory.decodeByteArray(fbyt_agent, 0,
						fbyt_agent.length);

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
            e.printStackTrace();
			Log.i(getLocalClassName(), e.toString() + "Error in creating pdf");
		}

	}

	// Store User input in Bean Object
	private void addListenerOnSubmit() {
		// TODO Auto-generated method stub
		shubhNiveshBean = new ShubhNiveshBean();

		// Input from GUI[Basic Cover]
		if (selStaffDisc.isChecked()) {
			shubhNiveshBean.setStaffDisc(true);
		} else {
			shubhNiveshBean.setStaffDisc(false);
		}
		if (selBancAssuranceDisc.isChecked()) {
			shubhNiveshBean.setBancAssuranceDisc(true);
		} else {
			shubhNiveshBean.setBancAssuranceDisc(false);
		}

		if(cb_kerladisc.isChecked())
		{
			shubhNiveshBean.setKerlaDisc(true);
			shubhNiveshBean.setServiceTax(true);
		}
		else
		{
			shubhNiveshBean.setKerlaDisc(false);
			shubhNiveshBean.setServiceTax(false);
		}

		/**
		 * Change as per 1,Jan,2014 by Akshaya Mirajkar JKResident Added
		 */

		if (selJkResident.isChecked()) {
			shubhNiveshBean.setJKResident(true);
		} else {
			shubhNiveshBean.setJKResident(false);
		}



		shubhNiveshBean.setPlanName(selPlan.getSelectedItem().toString());
		// System.out.println("* 1 *");
		shubhNiveshBean.setAge(Integer.parseInt(ageInYears.getSelectedItem()
				.toString()));
		// System.out.println("* 1.1 *");
		shubhNiveshBean.setGender(selGender.getSelectedItem().toString());
		// System.out.println("* 1.1 *"+selGender.getSelectedItem().toString());
		shubhNiveshBean.setPolicyTerm_Basic(Integer.parseInt(selBasicTerm
				.getSelectedItem().toString()));
		// System.out.println("* 1.2 *"+Integer.parseInt(selBasicTerm.getSelectedItem().toString()));
		shubhNiveshBean
				.setPremiumFreq(selPremFreq.getSelectedItem().toString());
		// System.out.println("* 1.3 *");
		shubhNiveshBean.setSumAssured_Basic(Double.parseDouble(basicSA.getText()
				.toString()));
		// System.out.println("* 1.3 *"+Integer.parseInt(basicSA.getText().toString()));
		// Input from GUI[PTA Rider Option]
		if (selPTARider.isChecked()) {
			shubhNiveshBean.setPTA_Status(true);
		} else {
			shubhNiveshBean.setPTA_Status(false);
		}
		if (selPTARider.isChecked()) {
			shubhNiveshBean.setSumAssured_PTA(Integer.parseInt(ptaSA.getText()
					.toString()));
			shubhNiveshBean.setPolicyTerm_PTA(Integer.parseInt(selPTATerm
					.getSelectedItem().toString()));
		}
		// Input from GUI[ADB Rider Option]
		if (selADBRider.isChecked()) {
			shubhNiveshBean.setADB_Status(true);
		} else {
			shubhNiveshBean.setADB_Status(false);
		}
		if (selADBRider.isChecked()) {
			shubhNiveshBean.setSumAssured_ADB(Integer.parseInt(adbSA.getText()
					.toString()));
			shubhNiveshBean.setPolicyTerm_ADB(Integer.parseInt(selADBTerm
					.getSelectedItem().toString()));
		}
		// Input from GUI[ATPDB Rider Option]
		if (selATPDBRider.isChecked()) {
			shubhNiveshBean.setATPDB_Status(true);
		} else {
			shubhNiveshBean.setATPDB_Status(false);
		}
		if (selATPDBRider.isChecked()) {
			shubhNiveshBean.setSumAssured_ATPDB(Integer.parseInt(atpdbSA
					.getText().toString()));
			shubhNiveshBean.setPolicyTerm_ATPDB(Integer.parseInt(selATPDBTerm
					.getSelectedItem().toString()));
		}
		// System.out.println("* 3 *");
		// Show Output Form
		showShubhNiveshOutputPg(shubhNiveshBean);

	}

	private void showShubhNiveshOutputPg(ShubhNiveshBean shubhNiveshBean) {
		// TODO Auto-generated method stub

		// Variable declaration

        String BI = "";
        int year_F = 0;

		String premiumPTA = "0", premiumADB = "0", premiumATPDB = "0";

		// Class Declaration
		ShubhNiveshProperties prop = new ShubhNiveshProperties();
		ShubhNiveshBusinessLogic shubhNiveshBussinesLogic = new ShubhNiveshBusinessLogic(
				shubhNiveshBean);
		CommonForAllProd commonForAllProd = new CommonForAllProd();

		String premiumBasic = ""
				+ shubhNiveshBussinesLogic
				.get_Premium_Basic_WithoutST_NotRounded();
		/* Modified by Akshaya on 20-MAY-2016 **/

//		premBasic = Double.parseDouble(commonForAllProd
//				.getRoundUp(commonForAllProd.getStringWithout_E(Double
//						.parseDouble(commonForAllProd
//								.getRoundOffLevel2(premiumBasic)))));
        premBasicRoundUp = Double.parseDouble(commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(Double.parseDouble(premiumBasic))));
        premBasic = Double.parseDouble(commonForAllProd
                .getRoundOffLevel2New(commonForAllProd
                        .getStringWithout_E(Double.parseDouble(premiumBasic))));
        premBasicRoundedOff2 = Double.parseDouble(commonForAllProd
                .getRoundOffLevel2(commonForAllProd.getStringWithout_E(Double
                        .parseDouble(premiumBasic))));

        /** Modified by Akshaya on 20-MAY-2016 **/
        premiumBasicWithoutAnyDisc = commonForAllProd
				.getRoundUp(commonForAllProd
						.getStringWithout_E(shubhNiveshBussinesLogic
								.get_Premium_Basic_WithoutAnyDisc_NotRounded()));

		/* Modified by Akshaya on 23-APR-2014 **/
		nonGuaranMatBen_4Percent = commonForAllProd.getStringWithout_E(Math
				.round(shubhNiveshBussinesLogic
						.getNonGuaranteedMatBenefit("4%")));
		/* Modified by Akshaya on 23-APR-2014 **/
		nonGuaranMatBen_8Percent = commonForAllProd.getStringWithout_E(Math
				.round(shubhNiveshBussinesLogic
						.getNonGuaranteedMatBenefit("8%")));

		if (shubhNiveshBean.getPTA_Status()) {
			premiumPTA = ""
					+ shubhNiveshBussinesLogic
					.get_Premium_PTA_WithoutST_NotRounded();
			/* Modified on 1-APR-2014 by Akshaya start */
			premiumPTAWithoutAnyDisc = ""
					+ shubhNiveshBussinesLogic
					.get_Premium_PTA_WithoutAnyDisc_NotRounded();
			/* Modified on 1-APR-2014 by Akshaya end */
		}

		if (shubhNiveshBean.getADB_Status()) {
            premiumADB = "" + shubhNiveshBussinesLogic.get_Premium_ADB_WithoutST_NotRounded();

            /** Modified on 1-APR-2014 by Akshaya start */
            premiumADBWithoutAnyDisc = commonForAllProd.getStringWithout_E(shubhNiveshBussinesLogic
                    .get_Premium_ADB_WithoutAnyDisc_NotRounded());
            /** Modified on 1-APR-2014 by Akshaya end */
		}
		if (shubhNiveshBean.getATPDB_Status()) {
			premiumATPDB = ""
					+ shubhNiveshBussinesLogic.get_Premium_ATPDB_WithoutST_NotRounded();
			/** Modified on 1-APR-2014 by Akshaya start */
			premiumATPDBWithoutAnyDisc = commonForAllProd.getStringWithout_E(shubhNiveshBussinesLogic
					.get_Premium_ATPDB_WithoutAnyDisc_NotRounded());
			/** Modified on 1-APR-2014 by Akshaya end */
		}
		// System.out.println("calculation" + premiumADB);

		// System.out.println("premiumATPDB - > "+ premiumATPDB);


		/* Modified by Akshaya on 23-APR-2014 **/
		/* Modified By - Priyanka Warekar - 31-08-2015 - Start */

		/* modified by Akshaya on 20-MAY-16 start **/

        premiumSingleInstBasic = commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E((Double.parseDouble(premiumBasic) + Double.parseDouble(premiumPTA) + Double.parseDouble(premiumADB) + Double.parseDouble(premiumATPDB))));

        String premiumBasicRider = commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E((Double.parseDouble(premiumPTA) + Double.parseDouble(premiumADB) + Double.parseDouble(premiumATPDB))));
        premiumSingleInstBasicRider = commonForAllProd.getRound(commonForAllProd.getStringWithout_E((Double.parseDouble(premiumPTA) + Double.parseDouble(premiumADB) + Double.parseDouble(premiumATPDB))));
        premiumSingleInstBasic = commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E((Double.parseDouble(premiumBasic) + Double.parseDouble(premiumSingleInstBasicRider))));

//		premiumInstBasicFirstYear = (premBasic + );
//		System.out.println("premiumSingleInstBasicRider " + premiumSingleInstBasicRider);

		double basicServiceTax =0;
		double SBCServiceTax =0;
		double KKCServiceTax =0;
		double kerlaServiceTax=0;
		double KeralaCessServiceTax=0;
		boolean isKerlaDiscount = shubhNiveshBean.isKerlaDisc();

		if(isKerlaDiscount)
		{
            basicServiceTax = shubhNiveshBussinesLogic.getServiceTax((premBasicRoundUp), shubhNiveshBean.getJKResident(), shubhNiveshBean.isKerlaDisc(), "basic");
            kerlaServiceTax = shubhNiveshBussinesLogic.getServiceTax(premBasicRoundUp, shubhNiveshBean.getJKResident(), shubhNiveshBean.isKerlaDisc(), "KERALA");
            KeralaCessServiceTax = shubhNiveshBussinesLogic.getServiceTax(premBasicRoundUp, shubhNiveshBean.getJKResident(), shubhNiveshBean.isKerlaDisc(), "KERALA");
		}else{
            basicServiceTax = shubhNiveshBussinesLogic.getServiceTax(premBasicRoundUp, shubhNiveshBean.getJKResident(), shubhNiveshBean.isKerlaDisc(), "basic");
            SBCServiceTax = shubhNiveshBussinesLogic.getServiceTax(premBasicRoundUp, shubhNiveshBean.getJKResident(), shubhNiveshBean.isKerlaDisc(), "SBC");
            KKCServiceTax = shubhNiveshBussinesLogic.getServiceTax(premBasicRoundUp, shubhNiveshBean.getJKResident(), shubhNiveshBean.isKerlaDisc(), "KKC");
		}


        String serviceTax = commonForAllProd.getStringWithout_E(basicServiceTax + SBCServiceTax + KKCServiceTax + kerlaServiceTax);
//		System.out.println("basicServiceTax "+basicServiceTax);
//		System.out.println("SBCServiceTax "+SBCServiceTax);
//		System.out.println("kkc "+KKCServiceTax);
        //  Added By Saurabh Jain on 13/05/2019 End

//		String premiumSingleInstBasicWithST=commonForAllProd.getStringWithout_E(Double.parseDouble(premiumSingleInstBasic)+Double.parseDouble(serviceTax));
//		System.out.println("serviceTax " + serviceTax);
//
        premInstBasicFirstYear = commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(Double.parseDouble(premiumBasic) + Double.parseDouble(serviceTax)));
//		System.out.println("premInstBasicFirstYear "+premInstBasicFirstYear);


        //	premInstBasicSecondYear


		/*** modified by Akshaya on 20-MAY-16 start **/
        //  Added By Saurabh Jain on 13/05/2019 Start
		double basicServiceTaxSecondYear = 0;
		double SBCServiceTaxSecondYear = 0;
		double KKCServiceTaxSecondYear = 0;

		double kerlaServiceTaxSecondYear=0;
		double KeralaCessServiceTaxSecondYear =0;
//		if(isKerlaDiscount){
//		 basicServiceTaxSecondYear = shubhNiveshBussinesLogic.getServiceTaxSecondYear(Double.parseDouble(premiumSingleInstBasic),shubhNiveshBean.isJkResident(),"basic");
//		 kerlaServiceTaxSecondYear = shubhNiveshBussinesLogic.getServiceTaxSecondYear(Double.parseDouble(premiumSingleInstBasic),shubhNiveshBean.isJkResident(),"KERALA");
//		 KeralaCessServiceTaxSecondYear =kerlaServiceTaxSecondYear-basicServiceTaxSecondYear;
//		}else{
//			 basicServiceTaxSecondYear = shubhNiveshBussinesLogic.getServiceTaxSecondYear(Double.parseDouble(premiumSingleInstBasic),shubhNiveshBean.isJkResident(),"basic");
//			 SBCServiceTaxSecondYear = shubhNiveshBussinesLogic.getServiceTaxSecondYear(Double.parseDouble(premiumSingleInstBasic),shubhNiveshBean.isJkResident(),"SBC");
//			 KKCServiceTaxSecondYear = shubhNiveshBussinesLogic.getServiceTaxSecondYear(Double.parseDouble(premiumSingleInstBasic),shubhNiveshBean.isJkResident(),"KKC");
//		}

		if(isKerlaDiscount){
            basicServiceTaxSecondYear = shubhNiveshBussinesLogic.getServiceTaxSecondYear(premBasicRoundUp, shubhNiveshBean.getJKResident(), shubhNiveshBean.isKerlaDisc(), "basic");
            kerlaServiceTaxSecondYear = shubhNiveshBussinesLogic.getServiceTaxSecondYear(premBasicRoundUp, shubhNiveshBean.getJKResident(), shubhNiveshBean.isKerlaDisc(), "KERALA");
            KeralaCessServiceTaxSecondYear = shubhNiveshBussinesLogic.getServiceTaxSecondYear(premBasicRoundUp, shubhNiveshBean.getJKResident(), shubhNiveshBean.isKerlaDisc(), "KKC");
        } else {
            basicServiceTaxSecondYear = shubhNiveshBussinesLogic.getServiceTaxSecondYear(premBasicRoundUp, shubhNiveshBean.getJKResident(), shubhNiveshBean.isKerlaDisc(), "basic");
            SBCServiceTaxSecondYear = shubhNiveshBussinesLogic.getServiceTaxSecondYear(premBasicRoundUp, shubhNiveshBean.getJKResident(), shubhNiveshBean.isKerlaDisc(), "SBC");
            KKCServiceTaxSecondYear = shubhNiveshBussinesLogic.getServiceTaxSecondYear(premBasicRoundUp, shubhNiveshBean.getJKResident(), shubhNiveshBean.isKerlaDisc(), "KKC");
        }
        String serviceTaxSecondYear = commonForAllProd.getStringWithout_E(basicServiceTaxSecondYear + SBCServiceTaxSecondYear + KKCServiceTaxSecondYear + kerlaServiceTaxSecondYear);
        //	System.out.println("serviceTaxSecondYear "+serviceTaxSecondYear);
        //  Added By Saurabh Jain on 13/05/2019 End


        if (shubhNiveshBean.getPremiumFreq().equals("Single")) {
            premInstBasicSecondYear = "0";
        } else {
            premInstBasicSecondYear = commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(Double.parseDouble(premiumBasic) + Double.parseDouble(serviceTaxSecondYear)));
        }

//		System.out.println("premInstBasicSecondYear "+premInstBasicSecondYear);

//		String premiumSingleInstBasicWithSTSecondYear=commonForAllProd.getStringWithout_E(Double.parseDouble(premiumSingleInstBasic)+Double.parseDouble(serviceTaxSecondYear));


//		String premiumSingleInstBasicWithST=commonForAllProd.getRoundUp(""+(Double.parseDouble(premiumSingleInstBasic)*(shubhNiveshBussinesLogic.getServiceTax()+1)));
//		//	        System.out.println("*4*");
//		String serviceTax=""+(Integer.parseInt(premiumSingleInstBasicWithST)-Integer.parseInt(premiumSingleInstBasic));

        /************Rider Service*******************/
        double basicServiceTaxRider = 0;
        double SBCServiceTaxRider = 0;
        double KKCServiceTaxRider = 0;
        double kerlaServiceTaxRider = 0;
        double KeralaCessServiceTaxRider = 0;
        if (isKerlaDiscount) {
            basicServiceTaxRider = shubhNiveshBussinesLogic.getServiceTaxRider(Double.parseDouble(premiumSingleInstBasicRider), shubhNiveshBean.getJKResident(), shubhNiveshBean.isKerlaDisc(), "basic");
            kerlaServiceTaxRider = shubhNiveshBussinesLogic.getServiceTaxRider(Double.parseDouble(premiumSingleInstBasicRider), shubhNiveshBean.getJKResident(), shubhNiveshBean.isKerlaDisc(), "KERALA");
            KeralaCessServiceTaxRider = shubhNiveshBussinesLogic.getServiceTaxRider(Double.parseDouble(premiumSingleInstBasicRider), shubhNiveshBean.getJKResident(), shubhNiveshBean.isKerlaDisc(), "KKC");
            ;
        } else {
            basicServiceTaxRider = shubhNiveshBussinesLogic.getServiceTaxRider(Double.parseDouble(premiumSingleInstBasicRider), shubhNiveshBean.getJKResident(), shubhNiveshBean.isKerlaDisc(), "basic");
            SBCServiceTaxRider = shubhNiveshBussinesLogic.getServiceTaxRider(Double.parseDouble(premiumSingleInstBasicRider), shubhNiveshBean.getJKResident(), shubhNiveshBean.isKerlaDisc(), "SBC");
            KKCServiceTaxRider = shubhNiveshBussinesLogic.getServiceTaxRider(Double.parseDouble(premiumSingleInstBasicRider), shubhNiveshBean.getJKResident(), shubhNiveshBean.isKerlaDisc(), "KKC");
        }

        String serviceTaxRider = commonForAllProd.getStringWithout_E(basicServiceTaxRider + SBCServiceTaxRider + KKCServiceTax + kerlaServiceTaxRider);
//		System.out.println("serviceTaxRider "+serviceTaxRider);


        double basicServiceTaxRiderSecondYear = 0;
        double SBCServiceTaxRiderSecondYear = 0;
        double KKCServiceTaxRiderSecondYear = 0;
        double kerlaServiceTaxRiderSecondYear = 0;
        double KeralaCessServiceTaxRiderSecondYear = 0;
        if (isKerlaDiscount) {
            basicServiceTaxRiderSecondYear = shubhNiveshBussinesLogic.getServiceTaxRiderSecondYear(Double.parseDouble(premiumSingleInstBasicRider), shubhNiveshBean.getJKResident(), shubhNiveshBean.isKerlaDisc(), "basic");
            kerlaServiceTaxRiderSecondYear = shubhNiveshBussinesLogic.getServiceTaxRiderSecondYear(Double.parseDouble(premiumSingleInstBasicRider), shubhNiveshBean.getJKResident(), shubhNiveshBean.isKerlaDisc(), "KERALA");
            KeralaCessServiceTaxRiderSecondYear = kerlaServiceTaxSecondYear - basicServiceTaxRiderSecondYear;
		}else{
            basicServiceTaxRiderSecondYear = shubhNiveshBussinesLogic.getServiceTaxRiderSecondYear(Double.parseDouble(premiumSingleInstBasicRider), shubhNiveshBean.getJKResident(), shubhNiveshBean.isKerlaDisc(), "basic");
            SBCServiceTaxRiderSecondYear = shubhNiveshBussinesLogic.getServiceTaxRiderSecondYear(Double.parseDouble(premiumSingleInstBasicRider), shubhNiveshBean.getJKResident(), shubhNiveshBean.isKerlaDisc(), "SBC");
            KKCServiceTaxRiderSecondYear = shubhNiveshBussinesLogic.getServiceTaxRiderSecondYear(Double.parseDouble(premiumSingleInstBasicRider), shubhNiveshBean.getJKResident(), shubhNiveshBean.isKerlaDisc(), "KKC");
        }

        String serviceTaxRiderSecondYear = commonForAllProd.getStringWithout_E(basicServiceTaxRiderSecondYear + SBCServiceTaxRiderSecondYear + KKCServiceTaxRiderSecondYear + kerlaServiceTaxRiderSecondYear);
        //	System.out.println("serviceTaxRiderSecondYear "+serviceTaxRiderSecondYear);

        premInstFYRider = commonForAllProd.getRound(commonForAllProd.getStringWithout_E(Double.parseDouble(premiumSingleInstBasicRider) + Double.parseDouble(serviceTaxRider)));

        //	System.out.println("premInstFYRider " + premInstFYRider);

        premInstSYRider = commonForAllProd.getRound(commonForAllProd.getStringWithout_E(Double.parseDouble(premiumSingleInstBasicRider) + Double.parseDouble(serviceTaxRiderSecondYear)));

        if (Double.parseDouble(premInstFYRider) < 0) {
            premInstFYRider = "0";
        } else {
            premInstFYRider = premInstFYRider;
		}

//		System.out.println("premInstFYRider "+premInstFYRider);

        if (Double.parseDouble(premInstSYRider) < 0) {
            premInstSYRider = "0";
        } else {
            premInstSYRider = premInstSYRider;
        }



        String premiumSingleInstBasicWithST = commonForAllProd.getStringWithout_E(Double.parseDouble(premInstBasicFirstYear) + Double.parseDouble(premInstFYRider));
        //	System.out.println("premiumSingleInstBasicWithST " + premiumSingleInstBasicWithST);
        String premiumSingleInstBasicWithSTSecondYear = commonForAllProd.getStringWithout_E(Double.parseDouble(premInstBasicSecondYear) + Double.parseDouble(premInstSYRider));


        /*** modified by Akshaya on 20-MAY-16 end **/


        premADB = Double.parseDouble(premiumADB);
        premATPDB = Double.parseDouble(premiumATPDB);
        premPTA = Double.parseDouble(premiumPTA);

		double sumOfRiders = premADB + premATPDB + premPTA;

		String valPremiumError = valInstPremium(
				commonForAllProd.getRoundUp(premiumBasic),
				shubhNiveshBean.getPremiumFreq());

		String valRiderPremiumError = valRiderPremium(premBasic, sumOfRiders);

		/* Modified on 9-APR-14 by Akshaya start **/

		if (rb_proposerdetail_personaldetail_backdating_yes.isChecked()) {
			// "16-jan-2014")));
            BackDateinterest = commonForAllProd.getRoundUp(""
					+ (shubhNiveshBussinesLogic.getBackDateInterest(
					Double.parseDouble(premiumSingleInstBasicWithST),
					proposer_Backdating_BackDate)));

            BackDateinterestwithGST = commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(Double.parseDouble(BackDateinterest) + (Double.parseDouble(BackDateinterest) * prop.GSTforbackdate)));
		} else {
			BackDateinterestwithGST = "0";
		}

		// if(selOccupationMines.isChecked())
		// {
        MinesOccuInterest = ""
				+ shubhNiveshBussinesLogic.getMinesOccuInterest(shubhNiveshBean
				.getSumAssured_Basic());
		// }
		// else
		// {
		// MinesOccuInterest="0";
		// }

		// premiumSingleInstBasicWithST = String.valueOf(Double
		// .parseDouble(premiumSingleInstBasicWithST)
		// + Double.parseDouble(BackDateinterest));

		/* Modified by Akshaya on 23-APR-2014 **/
		premiumSingleInstBasicWithST = commonForAllProd
				.getStringWithout_E(Double
						.parseDouble(premiumSingleInstBasicWithST)
						+ Double.parseDouble(BackDateinterestwithGST));

		/* Modified on 9-APR-14 by Akshaya end **/
		BI = getOutput("Illustration", shubhNiveshBean);

		/*********************** output starts here ********************************************/

		retVal.append("<?xml version='1.0' encoding='utf-8' ?><shubhNivesh>");
		if (valPremiumError.equals("") && valRiderPremiumError.equals("")) {
			retVal.append("<errCode>0</errCode>");

			/*
			 * changes to be made in connect life on 30/12/2014 Staff Discount
			 * Percentage
			 */
			retVal.append("<staffDiscCode>").append(shubhNiveshBussinesLogic.getStaffDiscCode()).append("</staffDiscCode>");

			retVal.append("<staffDiscPercentage>").append(shubhNiveshBussinesLogic.getStaffRebate()).append("</staffDiscPercentage>");

			/*
			 * Basic Premium is Rs.
			 */
			/* Modified by Akshaya on 23-APR-2014 **/
			retVal.append("<basicPrem>").append(commonForAllProd.getStringWithout_E(premBasic)).append("</basicPrem>");

			/* Modified by Akshaya on 23-APR-2014 **/
			retVal.append("<basicPremWithoutAnyDisc>").append(premiumBasicWithoutAnyDisc).append("</basicPremWithoutAnyDisc>");

			/*
			 * Installment Premium without Applicable Taxes is Rs.
			 */
			/* Modified by Akshaya on 23-APR-2014 **/
			retVal.append("<installmntPrem>").append(premiumSingleInstBasic).append("</installmntPrem>");

			/*
			 * Applicable Taxes is Rs.
			 */
			/* Modified by Akshaya on 23-APR-2014 **/
			retVal.append("<servTax>").append(serviceTax).append("</servTax>");

			/*
			 * Installment Premium with Applicable Taxes is Rs.
			 */
			/* Modified by Akshaya on 23-APR-2014 **/
			retVal.append("<installmntPremWithSerTx>").append(premiumSingleInstBasicWithST).append("</installmntPremWithSerTx>");

			/* Modified By - Priyanka Warekar - 31-08-2015 - Start */
			retVal.append("<servTaxSeondYear>").append(serviceTaxSecondYear).append("</servTaxSeondYear>");
			retVal.append("<installmntPremWithSerTxSecondYear>").append(premiumSingleInstBasicWithSTSecondYear).append("</installmntPremWithSerTxSecondYear>");

			/* Modified By - Priyanka Warekar - 31-08-2015 - End */
			/*
			 * Guaranteed Maturity Benefit is Rs.
			 */
			retVal.append("<guarMatBenefit>").append(shubhNiveshBean.getSumAssured_Basic()).append("</guarMatBenefit>");
			/*
			 * Non-guaranteed Maturity Benefit With 4%pa is Rs.
			 */

			/* Modified by Akshaya on 23-APR-2014 **/
			retVal.append("<nonGuarMatBenefit4>").append(nonGuaranMatBen_4Percent).append("</nonGuarMatBenefit4>");

			/*
			 * Non-guaranteed Maturity Benefit With 8%pa is Rs.
			 */
			/* Modified by Akshaya on 23-APR-2014 **/
			retVal.append("<nonGuarMatBenefit8>").append(nonGuaranMatBen_8Percent).append("</nonGuarMatBenefit8>");

			/*
			  For Older versions of Easy Access Applications. As it is giving
			  nullPointer Exception. But It will return correct value as per
			  product updates 10,Jan,2014 Start
			 */

			/*
			 * Non-guaranteed Maturity Benefit With 6%pa is Rs.
			 */
			/* Modified by Akshaya on 23-APR-2014 **/
			retVal.append("<nonGuarMatBenefit6>").append(nonGuaranMatBen_4Percent).append("</nonGuarMatBenefit6>");

			/*
			 * Non-guaranteed Maturity Benefit With 10%pa is Rs.
			 */
			/* Modified by Akshaya on 23-APR-2014 **/
			retVal.append("<nonGuarMatBenefit10>").append(nonGuaranMatBen_8Percent).append("</nonGuarMatBenefit10>");

			/* For Older versions of Easy Access Application.End */

			/*
			 * Accidental Death Benefit Rider Premium is Rs.
			 */
			// ADB
			// System.out.println("get ADB" + shubhNiveshBean.getADB_Status());
			// System.out.println("prem ADB" +premADB );
			if (shubhNiveshBean.getADB_Status()) {
				retVal.append("<adbRiderPrem>").append(commonForAllProd.getRoundOffLevel2(String
						.valueOf(premADB))).append("</adbRiderPrem>");
				/* Modified on 1-APR-2014 by Akshaya start */
				retVal.append("<adbRiderPremWithoutAnyDisc>").append(commonForAllProd.getRoundOffLevel2(String
						.valueOf(premiumADBWithoutAnyDisc))).append("</adbRiderPremWithoutAnyDisc>");
				/* Modified on 1-APR-2014 by Akshaya end */

			} else {
				retVal.append("<adbRiderPrem>0</adbRiderPrem>");
				/* Modified on 1-APR-2014 by Akshaya start */
				retVal.append("<adbRiderPremWithoutAnyDisc>0</adbRiderPremWithoutAnyDisc>");
				/* Modified on 1-APR-2014 by Akshaya end */
			}

			/*
			 * Accidental Total & Permanent Disability Benefit Rider Premium is
			 * Rs.
			 */
			// ATPDB
			if (shubhNiveshBean.getATPDB_Status()) {
				retVal.append("<atpdbRiderPrem>").append(commonForAllProd.getRoundOffLevel2(String
						.valueOf(premATPDB))).append("</atpdbRiderPrem>");
				/* Modified on 1-APR-2014 by Akshaya start */
				retVal.append("<atpdbRiderPremWithoutAnyDisc>").append(commonForAllProd.getRoundOffLevel2(String
						.valueOf(premiumATPDBWithoutAnyDisc))).append("</atpdbRiderPremWithoutAnyDisc>");
				/* Modified on 1-APR-2014 by Akshaya end */
			} else {
				retVal.append("<atpdbRiderPrem>0</atpdbRiderPrem>");
				/* Modified on 1-APR-2014 by Akshaya start */
				retVal.append("<atpdbRiderPremWithoutAnyDisc>0</atpdbRiderPremWithoutAnyDisc>");
				/* Modified on 1-APR-2014 by Akshaya end */
			}
			/*
			  Preferred Term Assurance Rider Premium is Rs.
			 */
			// PTA
			if (shubhNiveshBean.getPTA_Status()) {
				retVal.append("<ptaRiderPrem>").append(commonForAllProd.getRoundOffLevel2(String
						.valueOf(premPTA))).append("</ptaRiderPrem>");
				retVal.append("<ptaRiderPremWithoutAnyDisc>").append(commonForAllProd.getRoundOffLevel2(String
						.valueOf(premiumPTAWithoutAnyDisc))).append("</ptaRiderPremWithoutAnyDisc>");
			} else {
				retVal.append("<ptaRiderPrem>0</ptaRiderPrem>");
				retVal.append("<ptaRiderPremWithoutAnyDisc>0</ptaRiderPremWithoutAnyDisc>");
			}

			/* Modified on 9-APR-14 by Akshaya start **/

			/*
			  <backdateInt> tag value will be stored in nbd_backdate_int and
			  Backdating date will be stored in nbd_backdating_date of
			  T_new_business_detail. send Y or N for NBD_WISH_TO_BACKDATE.
			 */

			retVal.append("<backdateInt>" + BackDateinterestwithGST + "</backdateInt>");

			/*
			  <OccuInt> tag value will be stored in nbd_occu_extra of
			  T_new_business_detail

			 */

			retVal.append("<OccuInt>").append(minesOccuInterest).append("</OccuInt>");

			/* Modified on 9-APR-14 by Akshaya end **/

			/* modified by Akshaya on 20-MAY-16 start **/

			retVal.append("<basicServiceTax>").append(commonForAllProd.getStringWithout_E(basicServiceTax)).append("</basicServiceTax>").append("<SBCServiceTax>").append(commonForAllProd.getStringWithout_E(SBCServiceTax)).append("</SBCServiceTax>").append("<KKCServiceTax>").append(commonForAllProd.getStringWithout_E(KKCServiceTax)).append("</KKCServiceTax>");

			retVal.append("<basicServiceTaxSecondYear>").append(commonForAllProd
					.getStringWithout_E(basicServiceTaxSecondYear)).append("</basicServiceTaxSecondYear>").append("<SBCServiceTaxSecondYear>").append(commonForAllProd
					.getStringWithout_E(SBCServiceTaxSecondYear)).append("</SBCServiceTaxSecondYear>").append("<KKCServiceTaxSecondYear>").append(commonForAllProd
					.getStringWithout_E(KKCServiceTaxSecondYear)).append("</KKCServiceTaxSecondYear>");

			retVal.append("<KeralaCessServiceTax>"+commonForAllProd.getStringWithout_E(KeralaCessServiceTax)+"</KeralaCessServiceTax>"+
					"<KeralaCessServiceTaxSecondYear>"+commonForAllProd.getStringWithout_E(KeralaCessServiceTaxSecondYear)+"</KeralaCessServiceTaxSecondYear>");

			/*** modified by Akshaya on 20-MAY-16 end **/
			int index = shubhNiveshBean.getPolicyTerm_Basic();
			String matNonGuar4pa = prsObj.parseXmlTag(BI, "matNonGuar4pa" + index + "");
			String matNonGuar8pa = prsObj.parseXmlTag(BI,"matNonGuar8pa" + index + "");
            retVal.append("<premiumSingleInstBasicRider>" + premiumSingleInstBasicRider + "</premiumSingleInstBasicRider>");
            retVal.append("<premInstBasicFirstYear>" + premInstBasicFirstYear + "</premInstBasicFirstYear>");
            retVal.append("<premInstBasicSecondYear>" + premInstBasicSecondYear + "</premInstBasicSecondYear>");
            retVal.append("<premInstFYRider>" + premInstFYRider + "</premInstFYRider>");
            retVal.append("<premInstSYRider>" + premInstSYRider + "</premInstSYRider>");

			retVal.append("<matNonGuar4pa"+index+">"+matNonGuar4pa+"</matNonGuar4pa"+index+">");
			retVal.append("<matNonGuar8pa"+index+">"+matNonGuar8pa+"</matNonGuar8pa"+index+">");
			retVal.append(BI);

		}
		// Min premium or min rider error
		else {
			if (!valPremiumError.equals("")) {
				retVal.append("<errCode>1</errCode>");
				retVal.append("<minPremError>").append(valPremiumError).append("</minPremError>");
			}
			if (!valRiderPremiumError.equals("")) {
				retVal.append("<errCode>1</errCode>");
				retVal.append("<minRiderPremError>").append(valRiderPremiumError).append("</minRiderPremError>");

			}
		}
		retVal.append("</shubhNivesh>");

		/*********************** output ends here ********************************************/

		/************************************** xml output *********************************/

		System.out.println(retVal.toString());

		/******************************************* xml output **********************************/

	}

	/************************* validation starts here ********************************************/

	private void getInput(ShubhNiveshBean shubhNiveshBean) {

		inputVal = new StringBuilder();
		// From GUI Input
		boolean staffDisc = shubhNiveshBean.getStaffDisc();
		boolean isJKResident = shubhNiveshBean.getJKResident();
		boolean bancAssuranceDisc = shubhNiveshBean.getBancAssuranceDisc();
		int policyTerm = shubhNiveshBean.getPolicyTerm_Basic();
		String premFreq = shubhNiveshBean.getPremiumFreq();
		double sumAssured = shubhNiveshBean.getSumAssured_Basic();
		String planOption = shubhNiveshBean.getPlanName();
		boolean isADBRider = shubhNiveshBean.getADB_Status();
		int adbTerm = shubhNiveshBean.getPolicyTerm_ADB();
		int adbSA = shubhNiveshBean.getSumAssured_ADB();
		boolean isATPDBRider = shubhNiveshBean.getATPDB_Status();
		int atpdbTerm = shubhNiveshBean.getPolicyTerm_ATPDB();
		int atpdbSA = shubhNiveshBean.getSumAssured_ATPDB();
		boolean isPTARider = shubhNiveshBean.getPTA_Status();
		int ptaTerm = shubhNiveshBean.getPolicyTerm_PTA();
		int ptaSA = shubhNiveshBean.getSumAssured_PTA();

		String LifeAssured_title=spnr_bi_shubh_nivesh_life_assured_title.getSelectedItem().toString();
		String LifeAssured_firstName=edt_bi_shubh_nivesh_life_assured_first_name.getText().toString();
		String LifeAssured_middleName=edt_bi_shubh_nivesh_life_assured_middle_name.getText().toString();
		String LifeAssured_lastName=edt_bi_shubh_nivesh_life_assured_last_name.getText().toString();
		String LifeAssured_DOB=btn_bi_shubh_nivesh_life_assured_date.getText().toString();
		String LifeAssured_age=ageInYears.getSelectedItem().toString();

		String proposer_title="";
		String proposer_firstName="";
		String proposer_middleName="";
		String proposer_lastName="";
		String proposer_DOB="";
		String proposer_age="";
		String proposer_gender="";

		String wish_to_backdate_flag="";
		if(rb_proposerdetail_personaldetail_backdating_yes.isChecked())
			wish_to_backdate_flag="y";
		else
			wish_to_backdate_flag="n";
		String backdate="";
		if(wish_to_backdate_flag.equals("y"))
			backdate=btn_proposerdetail_personaldetail_backdatingdate.getText().toString();
		else
			backdate="";

		inputVal.append("<?xml version='1.0' encoding='utf-8' ?><shubhNivesh>");

		inputVal.append("<LifeAssured_title>").append(LifeAssured_title).append("</LifeAssured_title>");
		inputVal.append("<LifeAssured_firstName>").append(LifeAssured_firstName).append("</LifeAssured_firstName>");
		inputVal.append("<LifeAssured_middleName>").append(LifeAssured_middleName).append("</LifeAssured_middleName>");
		inputVal.append("<LifeAssured_lastName>").append(LifeAssured_lastName).append("</LifeAssured_lastName>");
		inputVal.append("<LifeAssured_DOB>").append(LifeAssured_DOB).append("</LifeAssured_DOB>");
		inputVal.append("<LifeAssured_age>").append(LifeAssured_age).append("</LifeAssured_age>");


		inputVal.append("<proposer_title>").append(proposer_title).append("</proposer_title>");
		inputVal.append("<proposer_firstName>").append(proposer_firstName).append("</proposer_firstName>");
		inputVal.append("<proposer_middleName>").append(proposer_middleName).append("</proposer_middleName>");
		inputVal.append("<proposer_lastName>").append(proposer_lastName).append("</proposer_lastName>");
		inputVal.append("<proposer_DOB>").append(proposer_DOB).append("</proposer_DOB>");
		inputVal.append("<proposer_age>").append(proposer_age).append("</proposer_age>");
		inputVal.append("<proposer_gender>").append(proposer_gender).append("</proposer_gender>");

		inputVal.append("<product_name>").append(planName).append("</product_name>");
		/*parivartan changes*/
		inputVal.append("<product_Code>").append(product_Code).append("</product_Code>");
		inputVal.append("<product_UIN>").append(product_UIN).append("</product_UIN>");
		inputVal.append("<product_cateogory>").append(product_cateogory).append("</product_cateogory>");
		inputVal.append("<product_type>").append(product_type).append("</product_type>");
		/*end*/

		inputVal.append("<proposer_Is_Same_As_Life_Assured>").append(proposer_Is_Same_As_Life_Assured).append("</proposer_Is_Same_As_Life_Assured>");
		inputVal.append("<isStaff>").append(staffDisc).append("</isStaff>");
		inputVal.append("<isJKResident>").append(isJKResident).append("</isJKResident>");
		inputVal.append("<isBancAssuranceDisc>").append(bancAssuranceDisc).append("</isBancAssuranceDisc>");
		inputVal.append("<age>").append(ageInYears.getSelectedItem().toString()).append("</age>");
		inputVal.append("<gender>").append(selGender.getSelectedItem().toString()).append("</gender>");

        inputVal.append("<str_kerla_discount>" + str_kerla_discount + "</str_kerla_discount>");
		inputVal.append("<policyTerm>").append(policyTerm).append("</policyTerm>");
		inputVal.append("<premFreq>").append(premFreq).append("</premFreq>");
		inputVal.append("<sumAssured>").append(sumAssured).append("</sumAssured>");
		inputVal.append("<plan>").append(planOption).append("</plan>");
		inputVal.append("<isADBRider>").append(isADBRider).append("</isADBRider>");
		inputVal.append("<adbTerm>").append(adbTerm).append("</adbTerm>");
		inputVal.append("<adbSA>").append(adbSA).append("</adbSA>");
		inputVal.append("<isATPDBRider>").append(isATPDBRider).append("</isATPDBRider>");
		inputVal.append("<atpdbTerm>").append(atpdbTerm).append("</atpdbTerm>");
		inputVal.append("<atpdbSA>").append(atpdbSA).append("</atpdbSA>");
		inputVal.append("<isPTARider>").append(isPTARider).append("</isPTARider>");
		inputVal.append("<ptaTerm>").append(ptaTerm).append("</ptaTerm>");
		inputVal.append("<ptaSA>").append(ptaSA).append("</ptaSA>");

		inputVal.append("<Wish_to_backdate_policy>").append(wish_to_backdate_flag).append("</Wish_to_backdate_policy>");
		inputVal.append("<backdating_Date>").append(backdate).append("</backdating_Date>");

		//Added Tushar Kadam Kerla Applicable Tax 7-Jun-2019
		String str_kerla_discount = "N";
		if (cb_kerladisc.isChecked()) {
			str_kerla_discount = "Y";
		}

		inputVal.append("<KFC>" + str_kerla_discount + "</KFC>");
		//End Added Tushar Kadam Kerla Applicable Tax 7-Jun-2019

		inputVal.append("</shubhNivesh>");

	}

	private String getOutput(String sheetName, ShubhNiveshBean shubhNiveshBean) {
		bussIll = new StringBuilder();
		ShubhNiveshBusinessLogic shubhNiveshBussinesLogic = new ShubhNiveshBusinessLogic(
				shubhNiveshBean);
        double basePremWithTx = 0, deathGuar = 0, deathNonGuar4pa = 0, deathNonGuar8pa = 0, basePremWithTx_1 = 0, sumtotalPrem = 0, annualizedPrem = 0, prem = 0;
        int year_F = 0;

        CommonForAllProd commonForAllProd = new CommonForAllProd();
		try {
			for (int i = 1; i <= shubhNiveshBean.getPolicyTerm_Basic(); i++) {
                //Added by sujata on 08-01-2020
                String AnnulizedPremium = commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(shubhNiveshBussinesLogic
                        .getannulizedPremFinal(i, shubhNiveshBean.getPolicyTerm_Basic(), shubhNiveshBean.getPremiumFreq())));

                System.out.println("AnnulizedPremium " + AnnulizedPremium);
                if (i == 1) {
                    annualizedPrem = Double.parseDouble(AnnulizedPremium);
//					annualizedPrem = (commonForAllProd.getStringWithout_E(smartMoneyBackGoldBusinessLogic
//							.getannulizedPremFinal(year_F, smartMoneyBackGoldBean.getppt())));
//					System.out.println("annualizedPrem 1 "+annualizedPrem);
                }

                bussIll.append("<policyYr" + i + ">" + i + "</policyYr" + i + ">");

                /*basePremWithTx = shubhNiveshBussinesLogic.getTotPremPaidWtServTx_B(premBasicRoundedOff2,i);
                basePremWithTx_1=Math.round(basePremWithTx);
*/
                basePremWithTx = Double.parseDouble(commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(shubhNiveshBussinesLogic.getTotPremPaidWtServTx_B(premBasicRoundedOff2, i))));
                basePremWithTx_1 = (basePremWithTx);

                if (i == 1) {
                    prem = (basePremWithTx_1);
                }

                bussIll.append("<TotBasePremWithTx" + i + ">" + commonForAllProd.getStringWithout_E(basePremWithTx_1) + "</TotBasePremWithTx" + i + ">");

                bussIll.append("<AnnulizedPremium" + i + ">" + commonForAllProd.getRound(commonForAllProd
                        .getStringWithout_E(Double.parseDouble(AnnulizedPremium)))
                        + "</AnnulizedPremium" + i + ">");
//				Annulizedpremium = shubhNiveshBussinesLogic.getannulizedPremFinal(i, shubhNiveshBean.getPolicyTerm_Basic());
//				bussIll.append("<AnnulizedPremium" + i + ">" + commonForAllProd.getRound(commonForAllProd.getStringWithout_E(Annulizedpremium)) + "</AnnulizedPremium" + i + ">");

//				bussIll.append("<deathGuar"+ i +">" + commonForAllProd.getStringWithout_E(shubhNiveshBean.getSumAssured_Basic()) + "</deathGuar"+ i +">");
                //String deathGuar = commonForAllProd.getStringWithout_E(shubhNiveshBussinesLogic.getDeathBenifitGuaranteed(shubhNiveshBean.getPremiumFreq(), shubhNiveshBean.getPolicyTerm_Basic(), shubhNiveshBean.getAge(), i, shubhNiveshBean.getSumAssured_Basic(), premBasic));


                //18-01-2020

                String getTotalPremiumforDeath = commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(shubhNiveshBussinesLogic.getTotalPremiumforDeath(i, shubhNiveshBean.getPolicyTerm_Basic(), prem)));
                Double getTotalPremiumforDeath_b = Double.parseDouble(getTotalPremiumforDeath);
                sumtotalPrem = sumtotalPrem + getTotalPremiumforDeath_b;


                String DeathGuar = commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E((shubhNiveshBussinesLogic.getDeathBenifitGuaranteed(shubhNiveshBean.getPremiumFreq(), shubhNiveshBean.getPolicyTerm_Basic(), shubhNiveshBean.getAge(), i, shubhNiveshBean.getSumAssured_Basic(), premBasic, annualizedPrem))));
                //	bussIll.append("<deathGuar"+ i +">" + commonForAllProd.getStringWithout_E(shubhNiveshBussinesLogic.getDeathBenifitGuaranteed(shubhNiveshBean.getPremiumFreq(), shubhNiveshBean.getPolicyTerm_Basic(), shubhNiveshBean.getAge(), i, shubhNiveshBean.getSumAssured_Basic(), premBasic)) + "</deathGuar"+ i +">");
//				System.out.println("DeathGuar "+DeathGuar);

                bussIll.append("<GuarnAddition" + i + ">" + 0 + "</GuarnAddition" + i + ">");
                bussIll.append("<SurvivalBenifit" + i + ">" + 0 + "</SurvivalBenifit" + i + ">");
                bussIll.append("<deathGuar" + i + ">" + DeathGuar + "</deathGuar" + i + ">");

                deathNonGuar4pa = shubhNiveshBussinesLogic.getDeathBenefitNonGuaranteed4per(i, shubhNiveshBean.getPolicyTerm_Basic());
                bussIll.append("<deathNonGuar4pa" + i + ">" + commonForAllProd.getStringWithout_E(deathNonGuar4pa) + "</deathNonGuar4pa" + i + ">");

                deathNonGuar8pa = shubhNiveshBussinesLogic.getDeathBenefitNonGuaranteed8per(i, shubhNiveshBean.getPolicyTerm_Basic());
                bussIll.append("<deathNonGuar8pa" + i + ">" + commonForAllProd.getStringWithout_E(deathNonGuar8pa) + "</deathNonGuar8pa" + i + ">");

				if (i == shubhNiveshBean.getPolicyTerm_Basic())
                    bussIll.append("<matGuar" + i + ">" + commonForAllProd.getStringWithout_E(shubhNiveshBean.getSumAssured_Basic()) + "</matGuar" + i + ">");
				else
                    bussIll.append("<matGuar" + i + ">" + "0" + "</matGuar" + i + ">");


				if (i == shubhNiveshBean.getPolicyTerm_Basic())
                    bussIll.append("<matNonGuar4pa" + i + ">" + nonGuaranMatBen_4Percent + "</matNonGuar4pa" + i + ">");
				else
                    bussIll.append("<matNonGuar4pa" + i + ">" + "0" + "</matNonGuar4pa" + i + ">");


				if (i == shubhNiveshBean.getPolicyTerm_Basic())
                    bussIll.append("<matNonGuar8pa" + i + ">" + nonGuaranMatBen_8Percent + "</matNonGuar8pa" + i + ">");
				else
                    bussIll.append("<matNonGuar8pa" + i + ">" + "0" + "</matNonGuar8pa" + i + ">");


                bussIll.append("<CashBonus4pa" + i + ">" + 0 + "</CashBonus4pa" + i + ">");
                bussIll.append("<CashBonus8pa" + i + ">" + 0 + "</CashBonus8pa" + i + ">");
                bussIll.append("<surGuar" + i + ">" + commonForAllProd.getRoundUp(commonForAllProd.getStringWithout_E(Double.parseDouble(shubhNiveshBussinesLogic.getSurrenderGuaranteed(premBasic, i, shubhNiveshBean.getPolicyTerm_Basic(), sumtotalPrem)))) + "</surGuar" + i + ">");

                bussIll.append("<surNonGuar4pa" + i + ">" + commonForAllProd.getRound(commonForAllProd.getStringWithout_E(shubhNiveshBussinesLogic.getSurrenderNonGuaranteed4per(i, deathNonGuar4pa, shubhNiveshBean.getSumAssured_Basic(), shubhNiveshBean.getAge(), shubhNiveshBean.getPlanName(), shubhNiveshBean.getPolicyTerm_Basic()))) + "</surNonGuar4pa" + i + ">");

                bussIll.append("<surNonGuar8pa" + i + ">" + commonForAllProd.getRound(commonForAllProd.getStringWithout_E(shubhNiveshBussinesLogic.getSurrenderNonGuaranteed8per(i, deathNonGuar8pa, shubhNiveshBean.getSumAssured_Basic(), shubhNiveshBean.getAge(), shubhNiveshBean.getPlanName(), shubhNiveshBean.getPolicyTerm_Basic()))) + "</surNonGuar8pa" + i + ">");

                //	bussIll.append("<TotalDeathBenefit4per "+ i +">" + commonForAllProd.getStringWithout_E(Double.parseDouble(TotalDeathBenefit4per)) + "</TotalDeathBenefit4per"+ i +">");


                String TotalDeathBenefit4per = commonForAllProd.getRound(commonForAllProd.getStringWithout_E(shubhNiveshBussinesLogic.TotalDeathBenefit4per(i, shubhNiveshBean.getPolicyTerm_Basic(), Double.parseDouble(DeathGuar), deathNonGuar4pa, sumtotalPrem)));

                bussIll.append("<TotalDeathBenefit4per" + i + ">" + commonForAllProd.getRound(commonForAllProd
                        .getStringWithout_E(Double.parseDouble(TotalDeathBenefit4per)))
                        + "</TotalDeathBenefit4per" + i + ">");

                String TotalDeathBenefit8per = commonForAllProd.getRound(commonForAllProd.getStringWithout_E(shubhNiveshBussinesLogic.TotalDeathBenefit8per(i, shubhNiveshBean.getPolicyTerm_Basic(), Double.parseDouble(DeathGuar), deathNonGuar8pa, sumtotalPrem)));

                bussIll.append("<TotalDeathBenefit8per" + i + ">" + commonForAllProd.getRound(commonForAllProd
                        .getStringWithout_E(Double.parseDouble(TotalDeathBenefit8per)))
                        + "</TotalDeathBenefit8per" + i + ">");

                String TotalMaturityBenefit4per = commonForAllProd.getRound(commonForAllProd.getStringWithout_E(shubhNiveshBussinesLogic.TotalMaturityBenefit4per(i, shubhNiveshBean.getPolicyTerm_Basic(), deathNonGuar4pa)));

                bussIll.append("<TotalMaturityBenefit4per" + i + ">" + commonForAllProd.getRound(commonForAllProd
                        .getStringWithout_E(Double.parseDouble(TotalMaturityBenefit4per)))
                        + "</TotalMaturityBenefit4per" + i + ">");


                String TotalMaturityBenefit8per = commonForAllProd.getRound(commonForAllProd.getStringWithout_E(shubhNiveshBussinesLogic.TotalMaturityBenefit8per(i, shubhNiveshBean.getPolicyTerm_Basic(), deathNonGuar8pa)));

                bussIll.append("<TotalMaturityBenefit8per" + i + ">" + commonForAllProd.getRound(commonForAllProd
                        .getStringWithout_E(Double.parseDouble(TotalMaturityBenefit8per)))
                        + "</TotalMaturityBenefit8per" + i + ">");


			}

		} catch (Exception e) {
			e.printStackTrace();
			// System.out.println("** error **" + e.getMessage());
		}

		// System.out.println(bussIll.toString());

		return bussIll.toString();
	}

	// Validate Minimum premium
	private String valInstPremium(String premiumSingleInstBasic, String premFreq) {
		String error = "";

		if (premFreq.equals("Yearly")
				&& (Integer.parseInt(premiumSingleInstBasic) < 6000)) {
			error = "Minimum premium for Yearly Mode under this product is Rs. 6000";

		} else if (premFreq.equals("Half Yearly")
				&& (Integer.parseInt(premiumSingleInstBasic) < 3000)) {
			error = "Minimum premium for Half Yearly Mode under this product is Rs. 3000";
		}
		if (premFreq.equals("Quarterly")
				&& (Integer.parseInt(premiumSingleInstBasic) < 1500)) {
			error = "Minimum premium for Quarterly Mode under this product is Rs. 1500";
		} else if (premFreq.contains("Monthly")
				&& (Integer.parseInt(premiumSingleInstBasic) < 500)) {
			error = "Minimum premium for Monthly Mode under this product is Rs. 500";

		}
		return error;
	}

	// Validate Rider Premium
	private String valRiderPremium(double premBasic, double sumOfRiders) {
		String error = "";
		if ((premBasic * 0.3) < sumOfRiders) {
			error = "Total of Rider Premium should not be greater than 30% of the Base Premium";
		}

		return error;
	}

	// Validate Sum Assured
	private Boolean valSA() {
		double minRiderLimit, maxRiderLimit;
		StringBuilder error = new StringBuilder();

		if (basicSA.getText().toString().equals("")) {
			error.append("Please enter Sum Assured.");
			basicSA.requestFocus();
		} else if (Double.parseDouble(basicSA.getText().toString()) % 1000 != 0) {
			error.append("Sum assured should be multiple of 1,000");
			basicSA.requestFocus();
		} else if (Double.parseDouble(basicSA.getText().toString()) < 75000) {
			error.append("Sum assured should be greater than or equal to 75,000");
			basicSA.requestFocus();

		} else {
			if (selPTARider.isChecked()) {
				minRiderLimit = 25000;
				maxRiderLimit = Math.min(5000000,
						Double.parseDouble(basicSA.getText().toString()));

				if (ptaSA.getText().toString().equals("")
						|| Double.parseDouble(ptaSA.getText().toString()) < minRiderLimit
						|| Double.parseDouble(ptaSA.getText().toString()) > maxRiderLimit) {
                    error.append("\nEnter Sum assured for Preferred Term Assurance Rider between "
                            + currencyFormat.format(minRiderLimit)
                            + " and "
                            + currencyFormat.format(maxRiderLimit));
                    ptaSA.requestFocus();
                }
                if (Double.parseDouble(ptaSA.getText().toString()) % 1000 != 0) {
                    error.append("Sum assured should be multiple of 1,000");
					ptaSA.requestFocus();
				}

			}

			if (selADBRider.isChecked()) {
				minRiderLimit = 25000;
				maxRiderLimit = Math.min(5000000,
						Double.parseDouble(basicSA.getText().toString()));

				if (adbSA.getText().toString().equals("")
						|| Double.parseDouble(adbSA.getText().toString()) < minRiderLimit
						|| Double.parseDouble(adbSA.getText().toString()) > maxRiderLimit) {
                    error.append("\nEnter Sum assured for Accidental Death Benefit Rider "
                            + currencyFormat.format(minRiderLimit)
                            + " and "
                            + currencyFormat.format(maxRiderLimit));
                    adbSA.requestFocus();
                }
                if (Double.parseDouble(adbSA.getText().toString()) % 1000 != 0) {
                    error.append("Sum assured should be multiple of 1,000");
					adbSA.requestFocus();
				}

			}
			if (selATPDBRider.isChecked()) {
				minRiderLimit = 25000;
				maxRiderLimit = Math.min(5000000,
						Double.parseDouble(basicSA.getText().toString()));
				if (atpdbSA.getText().toString().equals("")
						|| Double.parseDouble(atpdbSA.getText().toString()) < minRiderLimit
						|| Double.parseDouble(atpdbSA.getText().toString()) > maxRiderLimit) {
                    error.append("\nEnter Sum assured for Accidental Total and Permenent Disability Benefit Rider between "
                            + currencyFormat.format(minRiderLimit)
                            + " and "
                            + currencyFormat.format(maxRiderLimit));
                    atpdbSA.requestFocus();
                }
                if (Double.parseDouble(atpdbSA.getText().toString()) % 1000 != 0) {
                    error.append("Sum assured should be multiple of 1,000");
					atpdbSA.requestFocus();
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
	 * Change as per 1,Jan,2014 by Akshaya Mirajkar
	 */
	// Validate Age
	private void valAge() {
		int maxLimit;

		if (selPlan.getSelectedItem().toString().equals("Endowment Option")) {
			if (selPremFreq.getSelectedItem().toString().equals("Single"))
				maxLimit = 60;
			else
				maxLimit = 58;
		} else
			maxLimit = 50;

		if (Integer.parseInt(ageInYears.getSelectedItem().toString()) > maxLimit) {
			showAlert.setMessage("Enter age between 18 to " + maxLimit);
			showAlert.setNeutralButton("OK",
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {
							ageInYears.setSelection(0, false);
						}
					});
			btn_bi_shubh_nivesh_life_assured_date.setText("Select Date");
			btn_bi_shubh_nivesh_life_assured_date.requestFocus();
			lifeAssured_date_of_birth = "";

			showAlert.show();
		}
	}

	/**
	 * Change as per 1,Jan,2014 by Akshaya Mirajkar
	 */
	// Validate policy Term
	private boolean valTerm() {
		int minLimit = 0;
		int maxLimit;
		String message;

		if (selPlan.getSelectedItem().toString().equals("Endowment Option")) {
			if (selPremFreq.getSelectedItem().toString().equals("Single"))
				minLimit = 5;
			else
                minLimit = 10;
		} else
			minLimit = 15;

		maxLimit = Math.min(30,
				65 - Integer.parseInt(ageInYears.getSelectedItem().toString()));
		if (Integer.parseInt(selBasicTerm.getSelectedItem().toString()) < minLimit
				|| Integer.parseInt(selBasicTerm.getSelectedItem().toString()) > maxLimit) {

			if (minLimit == maxLimit)
				message = "Enter Basic Term as " + minLimit;
			else
				message = "Enter Basic Term Beween " + minLimit + " and "
						+ maxLimit;

			showAlert.setMessage(message);
			showAlert.setNeutralButton("OK",
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {

						}
					});
			showAlert.show();
			setFocusable(selBasicTerm);
			selBasicTerm.requestFocus();

			return false;
		}

		return true;
	}

	private void valMaturityAge() {
		int Age = Integer.parseInt(ageInYears.getSelectedItem().toString());
		int PolicyTerm = Integer.parseInt(selBasicTerm.getSelectedItem()
				.toString());
		if ((Age + PolicyTerm) > 65) {
			showAlert.setMessage("Maturity age is 65 years");
			showAlert.setNeutralButton("OK",
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {
							selBasicTerm.setSelection(0, false);
						}
					});
			setFocusable(btn_bi_shubh_nivesh_life_assured_date);
			btn_bi_shubh_nivesh_life_assured_date.requestFocus();
			showAlert.show();
		}

	}

	// Rider term
	private boolean valTermRider() {
        int minLimit = 5;
		int maxLimit = Integer.parseInt(selBasicTerm.getSelectedItem()
				.toString());
		StringBuilder error = new StringBuilder();

		if (selPTARider.isChecked()
				&& Integer.parseInt(selPTATerm.getSelectedItem().toString()) > maxLimit) {
			selPTATerm.setSelection(0, false);
			error.append("Maximum term for PTA is ").append(maxLimit).append(" years");
			setFocusable(selPTATerm);
			selPTATerm.requestFocus();
		}

		if (selADBRider.isChecked()
				&& Integer.parseInt(selADBTerm.getSelectedItem().toString()) > maxLimit) {
			selADBTerm.setSelection(0, false);
			error.append("Maximum term for Accidental Death Benefit Rider is ").append(maxLimit).append(" years");
			setFocusable(selADBTerm);
			selADBTerm.requestFocus();
		}

		if (selATPDBRider.isChecked()
				&& Integer.parseInt(selATPDBTerm.getSelectedItem().toString()) > maxLimit) {
			selATPDBTerm.setSelection(0, false);
			error.append("Maximum term for Accidental Total and Permenent Disability Benefit Rider is ").append(maxLimit).append(" years");
			setFocusable(selADBTerm);
			selATPDBTerm.requestFocus();
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

	// Proper Name Validation
	public boolean valName() {
		if (!proposer_Title.equals("") && !proposer_First_Name.equals("")
				&& !proposer_Last_Name.equals("")) {
			return true;
		} else {
			showAlert.setMessage("Please Fill The Detail For Name");
			showAlert.setNeutralButton("OK",
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {
						}
					});
			showAlert.show();
			return false;
		}
	}

	private boolean valProposerSameAsLifeAssured() {
		if (!proposer_Is_Same_As_Life_Assured.equals("")) {
			return true;
		} else {
			showAlert
					.setMessage("Please Select Whether Proposer Is Same As Life Assured");
			showAlert.setNeutralButton("OK",
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {
							// apply focusable method
							setFocusable(rb_shubh_nivesh_proposer_same_as_life_assured_yes);
							rb_shubh_nivesh_proposer_same_as_life_assured_yes
									.requestFocus();
						}
					});
			showAlert.show();

			return false;

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

	private boolean TrueBackdate() {

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
                Date launchDate = dateformat1.parse("20-01-2020");

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

			}

		} catch (Exception e) {
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
									setFocusable(spnr_bi_shubh_nivesh_life_assured_title);
									spnr_bi_shubh_nivesh_life_assured_title
											.requestFocus();
								} else if (lifeAssured_First_Name.equals("")) {
									edt_bi_shubh_nivesh_life_assured_first_name
											.requestFocus();
								} else {
									edt_bi_shubh_nivesh_life_assured_last_name
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
                                // apply focusable method
                                setFocusable(spnr_bi_shubh_nivesh_life_assured_title);
                                spnr_bi_shubh_nivesh_life_assured_title
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
                                setFocusable(spnr_bi_shubh_nivesh_life_assured_title);
                                spnr_bi_shubh_nivesh_life_assured_title
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
                                setFocusable(spnr_bi_shubh_nivesh_life_assured_title);
                                spnr_bi_shubh_nivesh_life_assured_title
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
                                setFocusable(spnr_bi_shubh_nivesh_life_assured_title);
                                spnr_bi_shubh_nivesh_life_assured_title
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

							public void onClick(DialogInterface dialog,
												int which) {
								if (lifeAssured_Title.equals("")) {
									// apply focusable method
									setFocusable(spnr_bi_shubh_nivesh_life_assured_title);
									spnr_bi_shubh_nivesh_life_assured_title
											.requestFocus();
								} else if (lifeAssured_First_Name.equals("")) {
									edt_bi_shubh_nivesh_life_assured_first_name
											.requestFocus();
								} else {
									edt_bi_shubh_nivesh_life_assured_last_name
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
                                setFocusable(spnr_bi_shubh_nivesh_life_assured_title);
                                spnr_bi_shubh_nivesh_life_assured_title
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
                                setFocusable(spnr_bi_shubh_nivesh_life_assured_title);
                                spnr_bi_shubh_nivesh_life_assured_title
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
                                setFocusable(spnr_bi_shubh_nivesh_life_assured_title);
                                spnr_bi_shubh_nivesh_life_assured_title
                                        .requestFocus();
			}
                        });
                showAlert.show();

                return false;
            } else if (proposer_Title.equals("")
					|| proposer_First_Name.equals("")
					|| proposer_Last_Name.equals("")) {

				showAlert.setMessage("Please Fill Name Detail For Proposer");
				showAlert.setNeutralButton("OK",
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
												int which) {
								if (proposer_Title.equals("")) {
									// apply focusable method
									setFocusable(spnr_bi_shubh_nivesh_proposer_title);
									spnr_bi_shubh_nivesh_proposer_title
											.requestFocus();
								} else if (proposer_First_Name.equals("")) {
									edt_bi_shubh_nivesh_proposer_first_name
											.requestFocus();
								} else {
									edt_bi_shubh_nivesh_proposer_last_name
											.requestFocus();
								}
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

							public void onClick(DialogInterface dialog,
												int which) {
								// apply focusable method
								setFocusable(btn_bi_shubh_nivesh_life_assured_date);
								btn_bi_shubh_nivesh_life_assured_date
										.requestFocus();
							}
						});
				showAlert.show();

				return false;
			} else {
				return true;
			}
		} else if (proposer_Is_Same_As_Life_Assured.equalsIgnoreCase("n")) {

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
								setFocusable(btn_bi_shubh_nivesh_life_assured_date);
								btn_bi_shubh_nivesh_life_assured_date
										.requestFocus();
							}
						});
				showAlert.show();

				return false;
			}

			else if (proposer_date_of_birth.equals("")
					|| proposer_date_of_birth.equalsIgnoreCase("select Date")) {
				showAlert
						.setMessage("Please Select Valid Date Of Birth For Proposer");
				showAlert.setNeutralButton("OK",
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
												int which) {
								// apply focusable method
								setFocusable(btn_bi_shubh_nivesh_proposer_date);
								btn_bi_shubh_nivesh_proposer_date
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

	private boolean valminPremiumValueAndRider() {
		ShubhNiveshBean shubhNiveshBean = new ShubhNiveshBean();
		ShubhNiveshBusinessLogic shubhNiveshBussinesLogic = new ShubhNiveshBusinessLogic(
				shubhNiveshBean);
		CommonForAllProd commonForAllProd = new CommonForAllProd();
		String premiumPTA = "0";
		String premiumADB = "0";
		String premiumATPDB = "0";
		shubhNiveshBean
				.setPremiumFreq(selPremFreq.getSelectedItem().toString());

		shubhNiveshBean.setAge(Integer.parseInt(ageInYears.getSelectedItem()
				.toString().trim()));
		shubhNiveshBean.setPolicyTerm_Basic(Integer.parseInt(selBasicTerm
				.getSelectedItem().toString()));
		shubhNiveshBean.setPlanName(selPlan.getSelectedItem().toString());
        shubhNiveshBean.setSumAssured_Basic(Double.parseDouble(basicSA.getText()
				.toString()));

		if (selStaffDisc.isChecked()) {
			shubhNiveshBean.setStaffDisc(true);
		} else {
			shubhNiveshBean.setStaffDisc(false);
		}
		if (selBancAssuranceDisc.isChecked()) {
			shubhNiveshBean.setBancAssuranceDisc(true);
		} else {
			shubhNiveshBean.setBancAssuranceDisc(false);
		}

		if (selPTARider.isChecked()) {
			shubhNiveshBean.setPTA_Status(true);
		} else {
			shubhNiveshBean.setPTA_Status(false);
		}
		if (selPTARider.isChecked()) {
			shubhNiveshBean.setSumAssured_PTA(Integer.parseInt(ptaSA.getText()
					.toString()));
			shubhNiveshBean.setPolicyTerm_PTA(Integer.parseInt(selPTATerm
					.getSelectedItem().toString()));
		}
		// Input from GUI[ADB Rider Option]
		if (selADBRider.isChecked()) {
			shubhNiveshBean.setADB_Status(true);
		} else {
			shubhNiveshBean.setADB_Status(false);
		}
		if (selADBRider.isChecked()) {
			shubhNiveshBean.setSumAssured_ADB(Integer.parseInt(adbSA.getText()
					.toString()));
			shubhNiveshBean.setPolicyTerm_ADB(Integer.parseInt(selADBTerm
					.getSelectedItem().toString()));
		}
		// Input from GUI[ATPDB Rider Option]
		if (selATPDBRider.isChecked()) {
			shubhNiveshBean.setATPDB_Status(true);
		} else {
			shubhNiveshBean.setATPDB_Status(false);
		}
		if (selATPDBRider.isChecked()) {
			shubhNiveshBean.setSumAssured_ATPDB(Integer.parseInt(atpdbSA
					.getText().toString()));
			shubhNiveshBean.setPolicyTerm_ATPDB(Integer.parseInt(selATPDBTerm
					.getSelectedItem().toString()));
		}

		if (shubhNiveshBean.getPTA_Status()) {
            premiumPTA = ""
					+ shubhNiveshBussinesLogic
                    .get_Premium_PTA_WithoutST_NotRounded();
            /** Modified on 1-APR-2014 by Akshaya start */
			premiumPTAWithoutAnyDisc = ""
					+ shubhNiveshBussinesLogic
					.get_Premium_PTA_WithoutAnyDisc_NotRounded();
			/* Modified on 1-APR-2014 by Akshaya end */
		}

		if (shubhNiveshBean.getADB_Status()) {
            premiumADB = ""
					+ shubhNiveshBussinesLogic
                    .get_Premium_ADB_WithoutST_NotRounded();
            /** Modified on 1-APR-2014 by Akshaya start */
			premiumADBWithoutAnyDisc = ""
					+ shubhNiveshBussinesLogic
					.get_Premium_ADB_WithoutAnyDisc_NotRounded();
			/* Modified on 1-APR-2014 by Akshaya end */
		}
		if (shubhNiveshBean.getATPDB_Status()) {
            premiumATPDB = ""
					+ shubhNiveshBussinesLogic
                    .get_Premium_ATPDB_WithoutST_NotRounded();
            /** Modified on 1-APR-2014 by Akshaya start */
			premiumATPDBWithoutAnyDisc = ""
					+ shubhNiveshBussinesLogic
					.get_Premium_ATPDB_WithoutAnyDisc_NotRounded();
			/* Modified on 1-APR-2014 by Akshaya end */
		}

        String premiumBasic = ""
                + shubhNiveshBussinesLogic
                .get_Premium_Basic_WithoutST_NotRounded();

        /** modified by Akshaya on 23-APR-14 **/
		double premBasic = Double.parseDouble(commonForAllProd
				.getRoundUp(commonForAllProd.getStringWithout_E(Double
						.parseDouble(premiumBasic))));

		double premADB = Double.parseDouble(premiumADB);
		double premATPDB = Double.parseDouble(premiumATPDB);
		double premPTA = Double.parseDouble(premiumPTA);

		double sumOfRiders = premADB + premATPDB + premPTA;

		String valPremiumError = valInstPremium(
				commonForAllProd.getRoundUp(premiumBasic),
				shubhNiveshBean.getPremiumFreq());

		String valRiderPremiumError = valRiderPremium(premBasic, sumOfRiders);

		if (!valPremiumError.equals("")) {
			showAlert.setMessage(valPremiumError);
			showAlert.setNeutralButton("OK",
					new DialogInterface.OnClickListener() {


						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub

						}
					});
			showAlert.show();
			return false;
		} else if (!valRiderPremiumError.equals("")) {

			showAlert.setMessage(valRiderPremiumError);
			showAlert.setNeutralButton("OK",
					new DialogInterface.OnClickListener() {


						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub

						}
					});
			showAlert.show();
			return false;
		} else {
			return true;
		}

		// String valRiderPremiumError = valRiderPremium(premBasic,
		// sumOfRiders);

	}

	public boolean email_id_validation(String email_id, EditText et) {

		Pattern pattern;
		Matcher matcher;
		boolean validationFlag = false;
		final String EMAIL_PATTERN = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
		pattern = Pattern.compile(EMAIL_PATTERN);
		matcher = pattern.matcher(email_id);
		if (!(matcher.matches())) {
			et.setError("Please provide the correct email address");
			validationFlag = false;
		} else if ((matcher.matches())) {
			validationFlag = true;
		}
		return validationFlag;
	}

	private void setDefaultDate(int id) {
		Calendar present_date = Calendar.getInstance();
		present_date.add(Calendar.YEAR, -id);
		mDay = present_date.get(Calendar.DAY_OF_MONTH);
		mMonth = present_date.get(Calendar.MONTH);
		mYear = present_date.get(Calendar.YEAR);
	}

	public String getDate_DD_MM_YY(String OldDate) {
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

	public String getDate_MM_dd_yy(String OldDate) {
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
	private  String getDate(String OldDate) {
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

	/************************* validation ends here ********************************************/

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

	//
	// public void onBackPressed() {
	// // TODO Auto-generated method stub
	// super.onBackPressed();
	// d.dismiss();
	// }


	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		// TODO Auto-generated method stub
		if (v.getId() == edt_bi_shubh_nivesh_life_assured_first_name.getId()) {
			// clearFocusable(edt_bi_shubh_nivesh_life_assured_first_name);
			setFocusable(edt_bi_shubh_nivesh_life_assured_middle_name);
			edt_bi_shubh_nivesh_life_assured_middle_name.requestFocus();
		} else if (v.getId() == edt_bi_shubh_nivesh_life_assured_middle_name
				.getId()) {
			// clearFocusable(edt_bi_shubh_nivesh_life_assured_middle_name);
			setFocusable(edt_bi_shubh_nivesh_life_assured_last_name);
			edt_bi_shubh_nivesh_life_assured_last_name.requestFocus();
		} else if (v.getId() == edt_bi_shubh_nivesh_life_assured_last_name
				.getId()) {

			InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(
					edt_bi_shubh_nivesh_life_assured_last_name.getWindowToken(),
					0);
			// clearFocusable(edt_bi_shubh_nivesh_life_assured_last_name);
			setFocusable(btn_bi_shubh_nivesh_life_assured_date);
			btn_bi_shubh_nivesh_life_assured_date.requestFocus();
		}

		else if (v.getId() == edt_bi_shubh_nivesh_proposer_first_name.getId()) {
			// clearFocusable(edt_bi_shubh_nivesh_proposer_first_name);
			setFocusable(edt_bi_shubh_nivesh_proposer_middle_name);
			edt_bi_shubh_nivesh_proposer_middle_name.requestFocus();
		} else if (v.getId() == edt_bi_shubh_nivesh_proposer_middle_name
				.getId()) {
			// clearFocusable(edt_bi_shubh_nivesh_proposer_middle_name);
			setFocusable(edt_bi_shubh_nivesh_proposer_last_name);
			edt_bi_shubh_nivesh_proposer_last_name.requestFocus();
		} else if (v.getId() == edt_bi_shubh_nivesh_proposer_last_name.getId()) {

			InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(
					edt_bi_shubh_nivesh_proposer_last_name.getWindowToken(), 0);
			// clearFocusable(edt_bi_shubh_nivesh_proposer_last_name);
			setFocusable(btn_bi_shubh_nivesh_proposer_date);
			btn_bi_shubh_nivesh_proposer_date.requestFocus();
		} else if (v.getId() == edt_proposerdetail_basicdetail_contact_no
				.getId()) {
			setFocusable(edt_proposerdetail_basicdetail_Email_id);
			edt_proposerdetail_basicdetail_Email_id.requestFocus();
		}

		else if (v.getId() == edt_proposerdetail_basicdetail_Email_id.getId()) {
			setFocusable(edt_proposerdetail_basicdetail_ConfirmEmail_id);
			edt_proposerdetail_basicdetail_ConfirmEmail_id.requestFocus();
		}

		else if (v.getId() == edt_proposerdetail_basicdetail_ConfirmEmail_id
				.getId()) {
			clearFocusable(selPlan);
			setFocusable(selPlan);
			selPlan.requestFocus();
		}

		else if (v.getId() == basicSA.getId()) {

			InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(basicSA.getWindowToken(), 0);

			// clearFocusable(basicSA);
			setFocusable(rb_proposerdetail_personaldetail_backdating_yes);
			rb_proposerdetail_personaldetail_backdating_yes.requestFocus();
		}

		else if (v.getId() == ptaSA.getId()) {

			InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(ptaSA.getWindowToken(), 0);

			// clearFocusable(basicSA);
			setFocusable(selADBRider);
			selADBRider.requestFocus();
		}

		else if (v.getId() == adbSA.getId()) {

			InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(adbSA.getWindowToken(), 0);

			// clearFocusable(basicSA);
			setFocusable(selATPDBRider);
			selATPDBRider.requestFocus();
		}

		return true;
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
			commonMethods.dialogWarning(context, "Please Fill Email Id", true);
			edt_proposerdetail_basicdetail_Email_id.requestFocus();
			return false;

		} else if (ConfirmEmailId.equals("")) {

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
					commonMethods.dialogWarning(context, "Please Fill Email Id", true);
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