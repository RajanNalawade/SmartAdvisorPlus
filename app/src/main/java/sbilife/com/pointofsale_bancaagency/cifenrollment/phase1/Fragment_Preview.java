package sbilife.com.pointofsale_bancaagency.cifenrollment.phase1;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TableRow;

import androidx.fragment.app.Fragment;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import sbilife.com.pointofsale_bancaagency.DatabaseHelper;
import sbilife.com.pointofsale_bancaagency.ParseXML;
import sbilife.com.pointofsale_bancaagency.R;
import sbilife.com.pointofsale_bancaagency.ServiceURL;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;

public class Fragment_Preview extends Fragment implements AsyncUploadFile_CIF.Interface_Upload_CIF_Files{

	private DatabaseHelper db;
	private Context context;
	// Banca
	private EditText edt_pf_number,
	// personal
	edt_candidate_corporate_name, edt_father_name, edt_aadhar_card_no,
	// Contact
	edt_mobile_no, edt_phone_number_2, edt_email_id, edt_re_enter_email_id, edt_contact_person_email_id,
	// Qualification
	edt_basic_qualification, edt_board_name_for_basic_qualification, edt_roll_number_for_basic_qualification,
	//exam
	edt_state, edt_city,edt_exam_center_location;

	//personal
	private Spinner spnr_category,
			// Qualification
			spnr_educational_qualification, spnr_other_qualification, spnr_year_of_passing_for_basic_qualification,
			// Exam
			spnr_insurance_category, spnr_exam_center_location, spnr_exam_language;

	private TableRow tr_other_qualification, tr_edt_exam_center, tr_spnr_exam_center;
	private View view_edt_exam_center, view_spnr_exam_center;

	private boolean is_annexure_uploaded = false;
    private boolean is_id_card_uploaded = false;
    private boolean is_pan_card_uploaded = false;

	// Identification
	private ImageButton img_photo, img_signature;
    private String quotation_no = "";
    private String str_PF_NO = "";
    private String str_candidate_corporate_name = "";
    private String str_father_name = "";
    private String str_category = "";
    private String str_mobile_no = "";
    private String str_phone_no = "";
    private String str_current_pincode = "";
    private String str_email_id = "";
    private String str_permanent_pincode = "";
    private String str_basic_qualification = "";
    private String str_board_name_for_basic_qualification = "";
    private String str_roll_number_for_basic_qualification = "";
    private String str_year_of_passing = "";
    private String str_educational_qualification = "";
    private String str_other_qualification = "";
    private String str_insurance_category = "";
    private String str_current_state_code = "";
    private String str_current_city_code = "";
    private String str_exam_center_location = "";
    private String str_exam_language = "";
    private String str_photo = "";
    private String str_signature = "";
    private String str_aadhar_card_no = "";
    private String str_contact_person_eamil_id = "";
    private String str_branch_name = "";
    private String str_pan = "";
    private String str_nationality = "";
    private String str_sex = "";
    private String str_dob = "";
    private String str_created_dates = "";
    private String str_cor_type = "";
    private String str_current_district = "";
    private String str_permanent_district = "";
    private String inputciflist = "";
    private String inputciflist_photo = "";
    private String inputciflist_sign = "";
    private String Str_cif_data = "";
    private String str_salutation = "";
    private String photoByteArrayAsString = "";
    private String proposer_sign = "";
    private String str_state = "";
    private String str_city = "";
    private String str_telemarketer_name = "";
    private String str_current_house_number = "";
    private String str_current_street = "";
    private String str_current_town = "";
    private String str_current_pin_code = "";
    private String str_permanent_house_number = "";
    private String str_permanent_street = "";
    private String str_permanent_town = "";
    private String str_permanent_pin_code = "";
    private String str_primary_profession = "";
    private String str_ati_center = "";
    private String str_exam_mode = "";
    private String str_exam_body_name = "";
    private String str_internal_pf = "";
    private String str_voter_id = "";
    private String str_driving_license = "";
    private String str_passport_no = "";
    private String str_central_govt = "";
    private String str_name_initial = "";
    private String str_area = "";
    private String str_curr_same_as_permanent = "";
    private String str_created_by = "";
    private String str_modified_by = "";
    private String SyncStatus = "";
    private String str_exam_center_id = "";
    private String str_phone_number_2 = "", str_bdm_email = "", str_bdm_mobile = "",
			str_sales_support_email = "", str_sales_support_mobile = "";

	private AlertDialog.Builder showAlert;

	private ProgressDialog mProgressDialog;
	private AsyncResponse resp;

	private final String NAMESPACE = "http://tempuri.org/";
	private final String METHOD_NAME_UPLOADFILE_CIF = "UploadFile_CIFEnroll";
	private final String METHOD_NAME_SAVECIF = "saveCIFEnrollmentDetail";

	private StringBuilder inputVal;
	private byte[] photoByteArray;
	private CommonMethods mCommonMethods;

	private final String STATUS_SIGN = "1", STATUS_PHOTO = "2", STATUS_DATA = "3";
	private String checkFlag = "";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.cifenrollment_layout_preview,
				container, false);

		mCommonMethods = new CommonMethods();
		context = getActivity();
		db = new DatabaseHelper(context);

		quotation_no = CIFEnrollmentPFActivity.quotation_Number;
		showAlert = new AlertDialog.Builder(getActivity());

		// Banca
		edt_pf_number = rootView.findViewById(R.id.edt_pf_number);
		edt_pf_number.setClickable(false);
		edt_pf_number.setEnabled(false);

		// personal
		edt_candidate_corporate_name = rootView
				.findViewById(R.id.edt_candidate_corporate_name);
		edt_candidate_corporate_name.setClickable(false);
		edt_candidate_corporate_name.setEnabled(false);

		edt_father_name = rootView
				.findViewById(R.id.edt_father_name);
		edt_father_name.setClickable(false);
		edt_father_name.setEnabled(false);

		spnr_category = rootView.findViewById(R.id.spnr_category);
		spnr_category.setClickable(false);
		spnr_category.setEnabled(false);

		edt_aadhar_card_no = rootView.findViewById(R.id.edt_aadhar_card_no);
		edt_aadhar_card_no.setClickable(false);
		edt_aadhar_card_no.setEnabled(false);

		// Contact
		edt_mobile_no = rootView.findViewById(R.id.edt_mobile_no);
		edt_mobile_no.setClickable(false);
		edt_mobile_no.setEnabled(false);

		/*
		 * edt_phone_number_1 = (EditText) rootView
		 * .findViewById(R.id.edt_phone_number_1);
		 * edt_phone_number_1.setClickable(false);
		 * edt_phone_number_1.setEnabled(false);
		 */

		edt_phone_number_2 = rootView
				.findViewById(R.id.edt_phone_number_2);
		edt_phone_number_2.setClickable(false);
		edt_phone_number_2.setEnabled(false);

		edt_email_id = rootView.findViewById(R.id.edt_email_id);
		edt_email_id.setClickable(false);
		edt_email_id.setEnabled(false);

		edt_re_enter_email_id = rootView
				.findViewById(R.id.edt_re_enter_email_id);
		edt_re_enter_email_id.setClickable(false);
		edt_re_enter_email_id.setEnabled(false);

		edt_contact_person_email_id = rootView
				.findViewById(R.id.edt_contact_person_email_id);
		edt_contact_person_email_id.setClickable(false);
		edt_contact_person_email_id.setEnabled(false);

		// Qualification
		edt_basic_qualification = rootView
				.findViewById(R.id.edt_basic_qualification);
		edt_basic_qualification.setClickable(false);
		edt_basic_qualification.setEnabled(false);

		edt_board_name_for_basic_qualification = rootView
				.findViewById(R.id.edt_board_name_for_basic_qualification);
		edt_board_name_for_basic_qualification.setClickable(false);
		edt_board_name_for_basic_qualification.setEnabled(false);

		edt_roll_number_for_basic_qualification = rootView
				.findViewById(R.id.edt_roll_number_for_basic_qualification);
		edt_roll_number_for_basic_qualification.setClickable(false);
		edt_roll_number_for_basic_qualification.setEnabled(false);

		spnr_year_of_passing_for_basic_qualification = rootView
				.findViewById(R.id.spnr_year_of_passing_for_basic_qualification);
		spnr_year_of_passing_for_basic_qualification.setClickable(false);
		spnr_year_of_passing_for_basic_qualification.setEnabled(false);

		spnr_educational_qualification = rootView
				.findViewById(R.id.spnr_educational_qualification);
		spnr_educational_qualification.setClickable(false);
		spnr_educational_qualification.setEnabled(false);

		spnr_other_qualification = rootView
				.findViewById(R.id.spnr_other_qualification);
		spnr_other_qualification.setClickable(false);
		spnr_other_qualification.setEnabled(false);

		tr_other_qualification = rootView
				.findViewById(R.id.tr_other_qualification);

		// Exam
		edt_state = rootView.findViewById(R.id.edt_state);
		edt_state.setClickable(false);
		edt_state.setEnabled(false);

		edt_city = rootView.findViewById(R.id.edt_city);
		edt_city.setClickable(false);
		edt_city.setEnabled(false);

		edt_exam_center_location = rootView
				.findViewById(R.id.edt_exam_center_location);
		edt_exam_center_location.setClickable(false);
		edt_exam_center_location.setEnabled(false);

		tr_edt_exam_center = rootView
				.findViewById(R.id.tr_edt_exam_center);
		tr_spnr_exam_center = rootView
				.findViewById(R.id.tr_spnr_exam_center);
		view_edt_exam_center = rootView
				.findViewById(R.id.view_edt_exam_center);
		view_spnr_exam_center = rootView
				.findViewById(R.id.view_spnr_exam_center);

		spnr_exam_center_location = rootView
				.findViewById(R.id.spnr_exam_center_location);
		spnr_exam_center_location.setClickable(false);
		spnr_exam_center_location.setEnabled(false);

		spnr_exam_language = rootView
				.findViewById(R.id.spnr_exam_language);
		spnr_exam_language.setClickable(false);
		spnr_exam_language.setEnabled(false);

		spnr_insurance_category = rootView
				.findViewById(R.id.spnr_insurance_category);
		spnr_insurance_category.setClickable(false);
		spnr_insurance_category.setEnabled(false);

		// Identification
		// img_photo = (ImageButton)
		// rootView.findViewById(R.id.img_preview_photo);
		// img_photo.setClickable(false);
		// img_photo.setEnabled(false);
		//
		// img_signature = (ImageButton) rootView
		// .findViewById(R.id.img_preview_signature);
		// img_signature.setClickable(false);
		// img_signature.setEnabled(false);

        Button btn_proceed = rootView.findViewById(R.id.btn_proceed);

		try{

			String dashboard = "";
			dashboard = CIFEnrollmentMainActivity.dashboard;
			if (dashboard != null && dashboard.equalsIgnoreCase("true")) {
				quotation_no = CIFEnrollmentMainActivity.quotation_dashboard;
				str_PF_NO = CIFEnrollmentMainActivity.pf_dasboard;
				String isFlag1 = CIFEnrollmentMainActivity.isFlag1;

			} else {
				quotation_no = CIFEnrollmentPFActivity.quotation_Number;
				str_PF_NO = CIFEnrollmentPFActivity.str_pf_number;
			}

			SyncStatus();

			if (SyncStatus.equals(STATUS_DATA)) {
				// btn_proceed.setClickable(false);
				// btn_proceed.setEnabled(false);
			} else {
				btn_proceed.setClickable(true);
				btn_proceed.setEnabled(true);
			}

			String[] category_Arr = new String[] { "GEN", "SC", "ST", "OBC" };
			ArrayAdapter<String> category_adapter = new ArrayAdapter<String>(
					context, R.layout.spinner_item, category_Arr);
			category_adapter.setDropDownViewResource(R.layout.spinner_item1);
			spnr_category.setAdapter(category_adapter);
			category_adapter.notifyDataSetChanged();

			String[] educational_qualification_Arr = new String[] {
					/*"Others",*/"Not Applicable",
					"Associate / Fellow of Institute of Actuaries of India",
					"Associate / Fellow of CII London",
					"Associate / Fellow of Insurance Institute of India",
					"Post Graduate Qualification of Institute of Insurance and Risk Management Hyderabad Others" };
			ArrayAdapter<String> educational_qualification_adapter = new ArrayAdapter<String>(
					context, R.layout.spinner_item,
					educational_qualification_Arr);
			educational_qualification_adapter
					.setDropDownViewResource(R.layout.spinner_item1);
			spnr_educational_qualification
					.setAdapter(educational_qualification_adapter);
			educational_qualification_adapter.notifyDataSetChanged();

			String[] other_qualification_Arr = new String[] { "Graduate",
					"Post Graduate" };
			ArrayAdapter<String> other_qualification_adapter = new ArrayAdapter<String>(
					context, R.layout.spinner_item, other_qualification_Arr);
			other_qualification_adapter
					.setDropDownViewResource(R.layout.spinner_item1);
			spnr_other_qualification.setAdapter(other_qualification_adapter);
			other_qualification_adapter.notifyDataSetChanged();

			String[] insurance_categoryArr = new String[] { "Life", "Composite" };
			ArrayAdapter<String> insurance_category_adapter = new ArrayAdapter<String>(
					context, R.layout.spinner_item, insurance_categoryArr);
			insurance_category_adapter
					.setDropDownViewResource(R.layout.spinner_item1);
			spnr_insurance_category.setAdapter(insurance_category_adapter);
			insurance_category_adapter.notifyDataSetChanged();

			getValueFromDatabase_for_exam_center();
			setSpinner_ExamDetail();

			String[] exam_languageArr = new String[] { "English", "Hindi",
					"Marathi", "Bengali", "Tamil", "Telegu", "Assamese",
					"Gujarati", "malayalam", "Kannada", "Punjabi", "Urdu", "Oriya" };
			ArrayAdapter<String> exam_language_adapter = new ArrayAdapter<String>(
					context, R.layout.spinner_item, exam_languageArr);
			exam_language_adapter.setDropDownViewResource(R.layout.spinner_item1);
			spnr_exam_language.setAdapter(exam_language_adapter);
			exam_language_adapter.notifyDataSetChanged();

			//dob date
			getValueFromDatabaseForDob();

			int str_year = Integer.parseInt(str_dob.substring(6, 10)) + 15;
			String[] year_List = new String[11];
			for (int i = 0; i <= 10; i++) {
				year_List[i] = str_year + i + "";
			}
			ArrayAdapter<String> year_passing_adapter = new ArrayAdapter<String>(
					context, R.layout.spinner_item, year_List);
			year_passing_adapter.setDropDownViewResource(R.layout.spinner_item1);
			spnr_year_of_passing_for_basic_qualification
					.setAdapter(year_passing_adapter);
			year_passing_adapter.notifyDataSetChanged();
		}catch (Exception ex){
			ex.printStackTrace();
		}

		btn_proceed.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				SyncStatus();

				//check sign upload status
				if (!SyncStatus.equals(STATUS_DATA) && validation()) {
					getValueFromDatabase_New();
					getInput();

					try {

						M_MainActivity_Data data = new M_MainActivity_Data(
								quotation_no, str_PF_NO, new String(inputVal),
								true);

						long count = db.insertCIFDetail_New(data, quotation_no);
						if (count > 0) {
							mCommonMethods.showToast(context, "Data Inserted Successfully");
						}

						M_UserInformation DashboardDetail_data = new M_UserInformation(
								quotation_no,
								str_PF_NO,
								Fragment_PersonalDetails.str_candidate_corporate_name,
								"Document Upload Pending",
								Fragment_ContactDetails.str_mobile_no,
								Fragment_ContactDetails.str_email_id,
								Fragment_Identification.createdDate, true);

						DashboardDetail_data.setStr_aadhar_card_no(str_aadhar_card_no);
						DashboardDetail_data.setStr_contact_person_email_id(str_contact_person_eamil_id);

						long rowId2 = db.insertDashBoardDetail(DashboardDetail_data);
						if (rowId2 > 0) {
							mCommonMethods.showToast(context, "Data Inserted Successfully");
						}

					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					if (mCommonMethods.isNetworkConnected(context) && getValueFromDatabase()) {

						if (str_sex.equalsIgnoreCase("Male")) {
							str_salutation = "Mr.";
						} else if (str_sex.equalsIgnoreCase("Female")) {
							str_salutation = "Ms.";
						}

						Str_cif_data = "<?xml version='1.0' encoding='utf-8'?>"
								+ "<T_channel_details>" + "<CIF_TELEMARKETER_NAME>" + str_telemarketer_name + "</CIF_TELEMARKETER_NAME>"
								+ "<CIF_CANDIDATE_CORPORATE_NAME>" /*+ str_salutation*/ + str_candidate_corporate_name + "</CIF_CANDIDATE_CORPORATE_NAME>"
								+ "<CIF_FATHER_NAME>" + str_father_name + "</CIF_FATHER_NAME>"
								+ "<CIF_CURRENT_HOUSE_NUMBER>" + str_current_house_number + "</CIF_CURRENT_HOUSE_NUMBER>"
								+ "<CIF_CURRENT_STREET>" + str_current_street + "</CIF_CURRENT_STREET>"
								+ "<CIF_CURRENT_TOWN>" + str_current_town + "</CIF_CURRENT_TOWN>"
								+ "<CIF_CURRENT_PINCODE>" + str_current_pincode + "</CIF_CURRENT_PINCODE>"
								+ "<CIF_PERMANENT_HOUSE_NUMBER>" + str_permanent_house_number + "</CIF_PERMANENT_HOUSE_NUMBER>"
								+ "<CIF_PERMANENT_STREET>" + str_permanent_street + "</CIF_PERMANENT_STREET>"
								+ "<CIF_PERMANENT_TOWN>" + str_permanent_town + "</CIF_PERMANENT_TOWN>"
								+ "<CIF_PERMANENT_PINCODE>" + str_permanent_pincode + "</CIF_PERMANENT_PINCODE>"
								+ "<CIF_BASIC_QUALIFICATION>" + str_basic_qualification + "</CIF_BASIC_QUALIFICATION>"
								+ "<CIF_BOARD_NAME_QUALIFICATION>" + str_board_name_for_basic_qualification + "</CIF_BOARD_NAME_QUALIFICATION>"
								+ "<CIF_ROLL_NO_QUALIFICATION>" + str_roll_number_for_basic_qualification + "</CIF_ROLL_NO_QUALIFICATION>"
								+ "<CIF_OTHER_QUALIFICATION>" + str_other_qualification + "</CIF_OTHER_QUALIFICATION>"
								+ "<CIF_PRIMARY_PROFESSION>" + str_primary_profession + "</CIF_PRIMARY_PROFESSION>"
								+ "<CIF_PHONE_NO>" /*+ str_phone_number_1*/ + str_phone_number_2 + "</CIF_PHONE_NO>"
								+ "<CIF_MOBILE_NO>" + str_mobile_no + "</CIF_MOBILE_NO>"
								+ "<CIF_BRANCH_NAME>" + str_branch_name + "</CIF_BRANCH_NAME>"
								+ "<CIF_ATI_CENTER_ACCREDITATN_NO>" + str_ati_center + "</CIF_ATI_CENTER_ACCREDITATN_NO>"
								+ "<CIF_EXAM_MODE>" + str_exam_mode + "</CIF_EXAM_MODE>"
								+ "<CIF_EXAM_BODY_NAME>" + str_exam_body_name + "</CIF_EXAM_BODY_NAME>"
								+ "<CIF_EXAM_CENTER_LOCATION>" + "NSEiT Limited - " + str_exam_center_location + "</CIF_EXAM_CENTER_LOCATION>"
								+ "<CIF_EXAM_LANGUAGE>" + str_exam_language + "</CIF_EXAM_LANGUAGE>"
								+ "<CIF_EMAIL_ID>" + str_email_id + "</CIF_EMAIL_ID>"
								+ "<CIF_PAN>" + str_pan + "</CIF_PAN>"
								+ "<CIF_INTERNAL_REF_NO>" + str_PF_NO + "</CIF_INTERNAL_REF_NO>"
								+ "<CIF_VOTER_ID>" + str_voter_id + "</CIF_VOTER_ID>"
								+ "<CIF_DRIVING_LICENSE_NO>" + str_driving_license + "</CIF_DRIVING_LICENSE_NO>"
								+ "<CIF_PASSPORT_NO>" + str_passport_no + "</CIF_PASSPORT_NO>"
								+ "<CIF_CENTRAL_GOVT_ID>" + str_central_govt + "</CIF_CENTRAL_GOVT_ID>"
								+ "<CIF_NATIONALITY>" + str_nationality + "</CIF_NATIONALITY>"
								+ "<CIF_PF_NO>" + str_PF_NO + "</CIF_PF_NO>"
								+ "<CIF_INSURANCE_CATEGORY>" + str_insurance_category + "</CIF_INSURANCE_CATEGORY>"
								+ "<CIF_NAME_INITIAL>" + str_salutation + "</CIF_NAME_INITIAL>"
								+ "<CIF_CATEGORY>" + str_category + "</CIF_CATEGORY>"
								+ "<CIF_AREA>" + str_area + "</CIF_AREA>"
								+ "<CIF_EDUCATIONAL_QUALIFICATION>" + str_educational_qualification + "</CIF_EDUCATIONAL_QUALIFICATION>"
								+ "<CIF_GENDER>" + str_sex + "</CIF_GENDER>"
								+ "<CIF_CURR_ADD_SAMEAS_PERMANENT>" + str_curr_same_as_permanent + "</CIF_CURR_ADD_SAMEAS_PERMANENT>"
								+ "<CIF_SPONSORSHIP_DATE>" + Date_dd_mmm_yyyy(str_created_dates) + "</CIF_SPONSORSHIP_DATE>"
								+ "<CIF_YEAR_OF_PASSING>" + str_year_of_passing + "</CIF_YEAR_OF_PASSING>"
								+ "<CIF_DATE_OF_BIRTH>" + str_dob + "</CIF_DATE_OF_BIRTH>"
								+ "<CIF_CURRENT_STATE_NAME>" + str_current_state_code + "</CIF_CURRENT_STATE_NAME>"
								+ "<CIF_PERMANENT_STATE_NAME>" + str_current_state_code + "</CIF_PERMANENT_STATE_NAME>"
								+ "<CIF_CURRENT_CITY_NAME>" + str_current_city_code + "</CIF_CURRENT_CITY_NAME>"
								+ "<CIF_PERMANENT_CITY_NAME>" + str_current_city_code + "</CIF_PERMANENT_CITY_NAME>"
								+ "<CIF_PHOTO>" /*+ str_photo*/ + str_PF_NO + "Photo.jpg" + "</CIF_PHOTO>"
								+ "<CIF_SIGNATURE>" /*+ str_signature*/ + str_PF_NO + "Sign.jpg" + "</CIF_SIGNATURE>"
								+ "<ISSYNC>" + 0 + "</ISSYNC>"
								+ "<ISFLAG1>" + 0 + "</ISFLAG1>"
								+ "<ISFLAG2>" + 0 + "</ISFLAG2>"
								+ "<ISFLAG3>" + 0 + "</ISFLAG3>"
								+ "<ISFLAG4>" + 0 + "</ISFLAG4>"
								+ "<DELFLAG>" + 0 + "</DELFLAG>"
								+ "<CREATEDDATE>" + str_created_dates + "</CREATEDDATE>"
								+ "<CREATEDBY>" + 0 + "</CREATEDBY>"
								+ "<MODIFIEDDATE>" + str_created_dates + "</MODIFIEDDATE>"
								+ "<MODIFIEDBY>" + 0 + "</MODIFIEDBY>"
								+ "<CIF_COR_TYPE>" + str_cor_type + "</CIF_COR_TYPE>"
								+ "<CIF_CURRENT_DISTRICT>" + str_current_district + "</CIF_CURRENT_DISTRICT>"
								+ "<CIF_PERMANENT_DISTRICT>" + str_permanent_district + "</CIF_PERMANENT_DISTRICT>"
								//added by rajan 24-10-2017
								+ "<AADHAR_NO>" + str_aadhar_card_no + "</AADHAR_NO>"
								+ "<CIF_CONTACTPERSON_EMAIL_ID>" + str_contact_person_eamil_id + "</CIF_CONTACTPERSON_EMAIL_ID>"

								+ "<BDM_email>" + str_bdm_email + "</BDM_email>"
								+ "<BDM_mobile>" + str_bdm_mobile + "</BDM_mobile>"
								+ "<Sales_Support_email>" + str_sales_support_email + "</Sales_Support_email>"
								+ "<Sales_Support_mobile>" + str_sales_support_mobile + "</Sales_Support_mobile>"
								+ "</T_channel_details>";

						if (SyncStatus.equalsIgnoreCase("")
								|| SyncStatus.equalsIgnoreCase("0")) {
							checkFlag = STATUS_SIGN;
							createSoapRequestToUploadDoc();
							//SyncData();
						} else if (SyncStatus.equals(STATUS_SIGN)) {
							checkFlag = STATUS_PHOTO;
							createSoapRequestToUploadDoc();
						} else if (SyncStatus.equals(STATUS_PHOTO)) {
							//SyncData_image_sign();
							SyncData();
						}

					} else {
						mCommonMethods.showToast(context, "No internet Connection");
					}

				} else if (SyncStatus.equals(STATUS_DATA)) {
					mCommonMethods.showToast(context, "Data already exist");
				}
			}
		});

		setCIFInputGui();

		return rootView;
	}

	private void setCIFInputGui() {
		if (getValueFromDatabase()) {

			// Banca
			edt_pf_number.setText(str_PF_NO);

			// personal
			if (str_candidate_corporate_name != null) {

				edt_candidate_corporate_name
						.setText(str_candidate_corporate_name);

			} else {
				str_candidate_corporate_name = "";
			}
			if (str_father_name != null) {

				edt_father_name.setText(str_father_name);

			} else {
				str_father_name = "";
			}
			if (str_category != null) {

				spnr_category.setSelection(
						getIndex(spnr_category, str_category), false);

			} else {

				String[] category_Arr = new String[] { "GEN", "SC", "ST", "OBC" };
				ArrayAdapter<String> category_adapter = new ArrayAdapter<String>(
						context, R.layout.spinner_item, category_Arr);
				category_adapter
						.setDropDownViewResource(R.layout.spinner_item1);
				spnr_category.setAdapter(category_adapter);
				category_adapter.notifyDataSetChanged();

				str_category = "";
			}

			if(!str_aadhar_card_no.equals("")){
				edt_aadhar_card_no.setText(str_aadhar_card_no);
			}else{
				edt_aadhar_card_no.setText("");
			}

			// Contact
			if (str_mobile_no != null) {

				edt_mobile_no.setText(str_mobile_no);

			} else {
				str_mobile_no = "";
			}

			if (str_phone_number_2 != null) {

				edt_phone_number_2.setText(str_phone_number_2);

			} else {
				str_phone_number_2 = "";
			}
			if (str_email_id != null) {

				edt_email_id.setText(str_email_id);
				edt_re_enter_email_id.setText(str_email_id);

			} else {
				str_email_id = "";
			}

			if(str_contact_person_eamil_id != null){
				edt_contact_person_email_id.setText(str_contact_person_eamil_id);
			}else{
				edt_contact_person_email_id.setText("");
			}

			// Qualification
			if (str_basic_qualification != null) {

				edt_basic_qualification.setText(str_basic_qualification);

			} else {
				str_basic_qualification = "";
			}
			if (str_board_name_for_basic_qualification != null) {

				edt_board_name_for_basic_qualification
						.setText(str_board_name_for_basic_qualification);

			} else {
				str_board_name_for_basic_qualification = "";
			}
			if (str_roll_number_for_basic_qualification != null) {

				edt_roll_number_for_basic_qualification
						.setText(str_roll_number_for_basic_qualification);

			} else {
				str_roll_number_for_basic_qualification = "";
			}
			if (str_year_of_passing != null) {

				spnr_year_of_passing_for_basic_qualification.setSelection(
						getIndex(spnr_year_of_passing_for_basic_qualification,
								str_year_of_passing), false);

			} else {
				str_year_of_passing = "";
			}

			if (str_educational_qualification != null) {

				spnr_educational_qualification.setSelection(
						getIndex(spnr_educational_qualification,
								str_educational_qualification), false);

				if (str_educational_qualification.equals("Not Applicable")) {
					tr_other_qualification.setVisibility(View.GONE);
				} else {
					tr_other_qualification.setVisibility(View.GONE);
				}

			} else {
				String[] educational_qualification_Arr = new String[] {
						/*"Others",*/"Not Applicable",
						"Associate / Fellow of Institute of Actuaries of India",
						"Associate / Fellow of CII London",
						"Associate / Fellow of Insurance Institute of India",
						"Post Graduate Qualification of Institute of Insurance and Risk Management Hyderabad Others" };
				ArrayAdapter<String> educational_qualification_adapter = new ArrayAdapter<String>(
						context, R.layout.spinner_item,
						educational_qualification_Arr);
				educational_qualification_adapter
						.setDropDownViewResource(R.layout.spinner_item1);
				spnr_educational_qualification
						.setAdapter(educational_qualification_adapter);
				educational_qualification_adapter.notifyDataSetChanged();
				str_educational_qualification = "Not Applicable";
			}

			if (str_other_qualification != null) {

				spnr_other_qualification.setSelection(
						getIndex(spnr_other_qualification,
								str_other_qualification), false);

			} else {

				String[] other_qualification_Arr = new String[] { "Graduate",
						"Post Graduate" };
				ArrayAdapter<String> other_qualification_adapter = new ArrayAdapter<String>(
						context, R.layout.spinner_item,
						other_qualification_Arr);
				other_qualification_adapter
						.setDropDownViewResource(R.layout.spinner_item1);
				spnr_other_qualification
						.setAdapter(other_qualification_adapter);
				other_qualification_adapter.notifyDataSetChanged();
				str_other_qualification = "Graduate";
			}

			// Exam
			if (str_current_state_code != null) {

				edt_state.setText(str_current_state_code);

			} else {
				str_current_state_code = "";
			}
			if (str_current_city_code != null) {

				edt_city.setText(str_current_city_code);

			} else {
				str_current_city_code = "";
			}

			if (str_insurance_category != null) {

				spnr_insurance_category.setSelection(
						getIndex(spnr_insurance_category,
								str_insurance_category), false);

			} else {
				String[] insurance_categoryArr = new String[] { "Life",
						"Composite" };
				ArrayAdapter<String> insurance_category_adapter = new ArrayAdapter<String>(
						context, R.layout.spinner_item,
						insurance_categoryArr);
				insurance_category_adapter
						.setDropDownViewResource(R.layout.spinner_item1);
				spnr_insurance_category.setAdapter(insurance_category_adapter);
				insurance_category_adapter.notifyDataSetChanged();

				str_insurance_category = "";
			}

			if (str_exam_center_location != null) {

				if (str_exam_center_id.equalsIgnoreCase("0")) {
					edt_exam_center_location.setText(str_exam_center_location);
				} else {
					spnr_exam_center_location.setSelection(
							getIndex(spnr_exam_center_location,
									str_exam_center_location), false);
				}

			} else {
				getValueFromDatabase_for_exam_center();
				setSpinner_ExamDetail();
				str_exam_center_location = "";
			}

			if (!str_exam_language.equals("")) {

				spnr_exam_language.setSelection(
						getIndex(spnr_exam_language, str_exam_language), false);

			} else {
				String[] exam_languageArr = new String[] { "English", "Hindi",
						"Marathi", "Bengali", "Tamil", "Telegu", "Assamese",
						"Gujarati", "malayalam", "Kannada", "Punjabi", "Urdu",
						"Oriya" };
				ArrayAdapter<String> exam_language_adapter = new ArrayAdapter<String>(
						context, R.layout.spinner_item, exam_languageArr);
				exam_language_adapter
						.setDropDownViewResource(R.layout.spinner_item1);
				spnr_exam_language.setAdapter(exam_language_adapter);
				exam_language_adapter.notifyDataSetChanged();
				str_exam_language = "";
			}

		}
	}

	private boolean getValueFromDatabase() {
		// retrieving data from database
		boolean flag = false;
		List<M_MainActivity_Data> data = db.getCIFDetail_New(quotation_no);
		if (data.size() > 0) {
			int i = 0;

			ParseXML prsObj = new ParseXML();

			String input = data.get(i).getInput();

            String str_quotation = prsObj.parseXmlTag(input, "quotation_number");
			str_PF_NO = prsObj.parseXmlTag(input, "pf_number");
			str_candidate_corporate_name = prsObj.parseXmlTag(input,
					"candidate_corporate_name");
			str_father_name = prsObj.parseXmlTag(input, "father_name");
			str_category = prsObj.parseXmlTag(input, "category");
			str_mobile_no = prsObj.parseXmlTag(input, "mobile_no");
			String str_phone_number = prsObj.parseXmlTag(input, "phone_number");
			str_phone_number_2 = str_phone_number;
			if (str_phone_number != null
					&& !str_phone_number.equalsIgnoreCase("")) {
				str_phone_number_2 = str_phone_number;
			}

			str_email_id = prsObj.parseXmlTag(input, "email_id");

			str_basic_qualification = prsObj.parseXmlTag(input,
					"basic_qualification");
			str_board_name_for_basic_qualification = prsObj.parseXmlTag(input,
					"board_name_for_basic_qualification");

			str_roll_number_for_basic_qualification = prsObj.parseXmlTag(input,
					"roll_number_for_basic_qualification");
			str_year_of_passing = prsObj.parseXmlTag(input,
					"year_of_passing_for_basic_qualification");
			str_educational_qualification = prsObj.parseXmlTag(input,
					"educational_qualification");
			str_other_qualification = prsObj.parseXmlTag(input,
					"other_qualification");

			str_insurance_category = prsObj.parseXmlTag(input,
					"insurance_category");
			str_current_state_code = prsObj.parseXmlTag(input, "state");
			str_current_city_code = prsObj.parseXmlTag(input, "city");
			str_exam_center_location = prsObj.parseXmlTag(input,
					"exam_center_location");
			str_exam_language = prsObj.parseXmlTag(input, "exam_language");
			str_exam_language = str_exam_language == null ? "" : str_exam_language;

			str_photo = prsObj.parseXmlTag(input, "photoByteArrayAsString");
			str_signature = prsObj.parseXmlTag(input, "proposer_sign");

			String strAnnexure = prsObj.parseXmlTag(input, "CIF_IS_ANNEXURE_UPLOADED");
			strAnnexure = strAnnexure == null ? "" : strAnnexure;
            is_annexure_uploaded = strAnnexure.equals("true");

            String strIDCardUploaded = prsObj.parseXmlTag(input, "CIF_IS_ID_CARD_UPLOADED");
            strIDCardUploaded = strIDCardUploaded == null ? "" : strIDCardUploaded;
            is_id_card_uploaded = strIDCardUploaded.equals("true");

            String strPanCardUploaded = prsObj.parseXmlTag(input, "CIF_IS_PAN_CARD_UPLOADED");
            strPanCardUploaded = strPanCardUploaded == null ? "" : strPanCardUploaded;
            is_pan_card_uploaded = strPanCardUploaded.equals("true");

			//added by rajan 14-05-2021
			str_bdm_email = prsObj.parseXmlTag(input, "BDM_email");
			str_bdm_mobile = prsObj.parseXmlTag(input, "BDM_mobile");
			str_sales_support_email = prsObj.parseXmlTag(input, "Sales_Support_email");
			str_sales_support_mobile = prsObj.parseXmlTag(input, "Sales_Support_mobile");
			//end by rajan 14-05-2021

			str_branch_name = prsObj.parseXmlTag(input, "branch_name");
			str_pan = prsObj.parseXmlTag(input, "pan_no");
			str_nationality = prsObj.parseXmlTag(input, "nationality");
			str_sex = prsObj.parseXmlTag(input, "sex");
			str_dob = prsObj.parseXmlTag(input, "dob");
			str_created_dates = prsObj.parseXmlTag(input, "createdDate");
			str_primary_profession = prsObj.parseXmlTag(input,
					"primary_profession");
			str_exam_mode = prsObj.parseXmlTag(input, "exam_mode");
			str_exam_body_name = prsObj.parseXmlTag(input, "exam_body_name");
			str_area = prsObj.parseXmlTag(input, "area");

			str_current_house_number = prsObj.parseXmlTag(input,
					"current_house_number");
			str_current_street = prsObj.parseXmlTag(input, "current_street");
			str_current_town = prsObj.parseXmlTag(input, "city");
			str_current_pincode = prsObj.parseXmlTag(input, "current_pincode");
			str_current_district = prsObj
					.parseXmlTag(input, "current_district");

			str_permanent_house_number = prsObj.parseXmlTag(input,
					"current_house_number");
			str_permanent_street = prsObj.parseXmlTag(input, "current_street");
			str_permanent_town = prsObj.parseXmlTag(input, "city");
			str_permanent_pincode = prsObj
					.parseXmlTag(input, "current_pincode");
			str_permanent_district = prsObj.parseXmlTag(input,
					"current_district");
			str_cor_type = prsObj.parseXmlTag(input, "cor_type");

			//added by rajan 24-10-2017
			str_aadhar_card_no = prsObj.parseXmlTag(input, "AADHAR_NO");
			str_aadhar_card_no = str_aadhar_card_no == null ? "" : str_aadhar_card_no;

			str_contact_person_eamil_id = prsObj
					.parseXmlTag(input, "CIF_CONTACTPERSON_EMAIL_ID");

			flag = true;
		}
		return flag;
	}

	private boolean getValueFromDatabaseForDob() {
		// retrieving data from database
		boolean flag = false;
		List<M_MainActivity_Data> data = db.getCIFDetail_New(quotation_no);
		if (data.size() > 0) {
			int i = 0;

			ParseXML prsObj = new ParseXML();

			String input = data.get(i).getInput();

			str_dob = prsObj.parseXmlTag(input, "dob");

		}
		return flag;
	}

	private boolean getValueFromDatabase_New() {
		// retrieving data from database
		boolean flag = false;
		List<M_MainActivity_Data> data = db.getCIFDetail_New(quotation_no);
		if (data.size() > 0) {
			int i = 0;
			ParseXML prsObj = new ParseXML();

			String input = data.get(i).getInput();
			str_candidate_corporate_name = prsObj.parseXmlTag(input,
					"candidate_corporate_name");
			str_father_name = prsObj.parseXmlTag(input, "father_name");
			str_category = prsObj.parseXmlTag(input, "category");

			// Contact details

			str_mobile_no = prsObj.parseXmlTag(input, "mobile_no");
			String str_phone_number = prsObj.parseXmlTag(input, "phone_number");

			if (str_phone_number != null
					&& !str_phone_number.equalsIgnoreCase("")) {
				str_phone_number_2 = str_phone_number;
				/*
				 * String[] array = str_phone_number.split("-");
				 * str_phone_number_1 = array[0]; str_phone_number_2 = array[1];
				 */
			}

			str_email_id = prsObj.parseXmlTag(input, "email_id");
			// str_re_enter_email_id = str_email_id;

			str_basic_qualification = prsObj.parseXmlTag(input,
					"basic_qualification");
			str_board_name_for_basic_qualification = prsObj.parseXmlTag(input,
					"board_name_for_basic_qualification");

			str_roll_number_for_basic_qualification = prsObj.parseXmlTag(input,
					"roll_number_for_basic_qualification");
			str_year_of_passing = prsObj.parseXmlTag(input,
					"year_of_passing_for_basic_qualification");
			str_educational_qualification = prsObj.parseXmlTag(input,
					"educational_qualification");
			str_other_qualification = prsObj.parseXmlTag(input,
					"other_qualification");

			str_insurance_category = prsObj.parseXmlTag(input,
					"insurance_category");
			str_state = prsObj.parseXmlTag(input, "state");
			str_city = prsObj.parseXmlTag(input, "city");
			str_exam_center_location = prsObj.parseXmlTag(input,
					"exam_center_location");
			str_exam_language = prsObj.parseXmlTag(input, "exam_language");
			str_exam_language = str_exam_language == null ? "" : str_exam_language;

			photoByteArrayAsString = prsObj.parseXmlTag(input,
					"photoByteArrayAsString");
			proposer_sign = prsObj.parseXmlTag(input, "proposer_sign");

            is_annexure_uploaded = prsObj.parseXmlTag(input, "CIF_IS_ANNEXURE_UPLOADED").equals("true");
            is_id_card_uploaded = prsObj.parseXmlTag(input, "CIF_IS_ID_CARD_UPLOADED").equals("true");
            is_pan_card_uploaded = prsObj.parseXmlTag(input, "CIF_IS_PAN_CARD_UPLOADED").equals("true");

			//added by rajan 14-05-2021
			str_bdm_email = prsObj.parseXmlTag(input, "BDM_email");
			str_bdm_mobile = prsObj.parseXmlTag(input, "BDM_mobile");
			str_sales_support_email = prsObj.parseXmlTag(input, "Sales_Support_email");
			str_sales_support_mobile = prsObj.parseXmlTag(input, "Sales_Support_mobile");
			//end by rajan 14-05-2021

			str_created_dates = prsObj.parseXmlTag(input, "createdDate");

			//added by rajan 24-10-2017
			str_aadhar_card_no = prsObj.parseXmlTag(input, "AADHAR_NO");
			str_aadhar_card_no = str_aadhar_card_no == null ? "": str_aadhar_card_no;

			str_contact_person_eamil_id = prsObj
					.parseXmlTag(input, "CIF_CONTACTPERSON_EMAIL_ID");

			flag = true;

		}
		return flag;
	}

	private void setSpinner_ExamDetail() {
		String[] exam_center_locationArr;
		if (str_exam_center_id.equalsIgnoreCase("0")) {
			tr_edt_exam_center.setVisibility(View.VISIBLE);
			view_edt_exam_center.setVisibility(View.VISIBLE);
			tr_spnr_exam_center.setVisibility(View.GONE);
			view_spnr_exam_center.setVisibility(View.GONE);
			exam_center_locationArr = new String[] { "Mumbai-Andheri" };
		} else {
			tr_spnr_exam_center.setVisibility(View.VISIBLE);
			view_spnr_exam_center.setVisibility(View.VISIBLE);
			tr_edt_exam_center.setVisibility(View.GONE);
			view_edt_exam_center.setVisibility(View.GONE);

			ParseXML prsObj = new ParseXML();
			// String inputciflist_exam = LoginActivity.inputciflist_exam;

			ArrayList<String> list_data = null;
			if (str_exam_center_id != null) {
				List<String> Node_table = prsObj.parseParentNode(
						str_exam_center_id, "Table");

				List<String> Node_data = prsObj
						.parseNodeElementpolicy_issu(Node_table);

				list_data = new ArrayList<String>();
				list_data.clear();

				for (String node : Node_data) {

					list_data.add(node);
				}

			}

			exam_center_locationArr = new String[list_data.size()];
			Collections.sort(list_data);
			exam_center_locationArr = list_data
					.toArray(exam_center_locationArr);
		}

		ArrayAdapter<String> exam_center_location_adapter = new ArrayAdapter<String>(
				context, R.layout.spinner_item, exam_center_locationArr);
		exam_center_location_adapter
				.setDropDownViewResource(R.layout.spinner_item1);
		spnr_exam_center_location.setAdapter(exam_center_location_adapter);
		exam_center_location_adapter.notifyDataSetChanged();
	}

	private boolean getValueFromDatabase_for_exam_center() {
		// retrieving data from database
		boolean flag = false;
		List<M_ExamDetails> data = db.getExamDetails(quotation_no);
		if (data.size() > 0) {
			int i = 0;

			str_exam_center_id = data.get(i).getStr_exam_center();

			flag = true;

		}
		return flag;
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

	private boolean validation() {

		if (Fragment_PersonalDetails.str_candidate_corporate_name.trim()
				.equals("")
				|| Fragment_PersonalDetails.str_father_name.trim().equals("")
				|| Fragment_PersonalDetails.str_category.trim().equals("")) {

			mCommonMethods.showToast(context, "Please Fill Personal Details Form First");

			((CIFEnrollmentMainActivity) getActivity()).getViewpager()
					.setCurrentItem(0);
			return false;
		} else if (Fragment_ContactDetails.str_mobile_no.trim().equals("")
				|| Fragment_ContactDetails.str_mobile_no.length() < 10
				|| Fragment_ContactDetails.str_phone_number_2.equals("")
				|| Fragment_ContactDetails.str_phone_number_2.length() < 10
				|| Fragment_ContactDetails.str_email_id.trim().equals("")
				|| Fragment_ContactDetails.str_re_enter_email_id.equals("")) {

			mCommonMethods.showToast(context, "Please Fill Contact Details Form First");

			((CIFEnrollmentMainActivity) getActivity()).getViewpager()
					.setCurrentItem(1);
			return false;
		} else if (Fragment_Qualification.str_basic_qualification.trim()
				.equals("")
				|| Fragment_Qualification.str_board_name_for_basic_qualification
						.trim().equals("")
				|| Fragment_Qualification.str_roll_number_for_basic_qualification
						.equals("")
				|| Fragment_Qualification.str_year_of_passing_for_basic_qualification
						.trim().equals("")
				|| Fragment_Qualification.str_educational_qualification
						.equals("")
				|| Fragment_Qualification.str_other_qualification.equals("")) {

			mCommonMethods.showToast(context, "Please Fill Qualification Details Form First");

			((CIFEnrollmentMainActivity) getActivity()).getViewpager()
					.setCurrentItem(2);
			return false;
		} else if (Fragment_ExamDetails.str_insurance_category.trim()
				.equals("")
				|| Fragment_ExamDetails.str_state.trim().equals("")
				|| Fragment_ExamDetails.str_city.equals("")
				|| Fragment_ExamDetails.str_exam_center_location.trim().equals(
						"")
				|| Fragment_ExamDetails.str_exam_language.trim().equals("")) {

			mCommonMethods.showToast(context, "Please Fill Exam Details Form First");

			((CIFEnrollmentMainActivity) getActivity()).getViewpager().setCurrentItem(3);
			return false;
		} else if (Fragment_Identification.photoByteArrayAsString.trim()
				.equals("")
				|| Fragment_Identification.proposer_sign.trim().equals("")
				|| !Fragment_Identification.is_annexure_uploaded
                || !Fragment_Identification.is_id_card_uploaded
        || !Fragment_Identification.is_pan_card_uploaded) {

			mCommonMethods.showToast(context, "Please Fill Identification Form First");

			((CIFEnrollmentMainActivity) getActivity()).getViewpager()
					.setCurrentItem(4);
			return false;
		}

		return true;
	}

	private void SyncData() {

		resp = new AsyncResponse();
		resp.execute();

	}

	class AsyncResponse extends AsyncTask<String, Void, String> {
		private volatile boolean running = true;

		@Override
		protected void onPreExecute() {
			mProgressDialog = new ProgressDialog(getActivity());
			String Message = "Please wait ,Loading...";

			mProgressDialog.setMessage(Message);
			mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			mProgressDialog.setCancelable(false);

			mProgressDialog.setButton("Cancel",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {

							resp.cancel(true);
							mProgressDialog.dismiss();
						}
					});
			mProgressDialog.show();
		}

		@Override
		protected String doInBackground(String... param) {
			try {
				String CIFBirthdate = "";
				running = true;

                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME_SAVECIF);

				request.addProperty("xmlStr", Str_cif_data);

				SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
						SoapEnvelope.VER11);
				envelope.dotNet = true;

				envelope.setOutputSoapObject(request);

				//allowAllSSL();
				mCommonMethods.TLSv12Enable();

				StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
						.permitAll().build();
				StrictMode.setThreadPolicy(policy);

				HttpTransportSE androidHttpTranport = new HttpTransportSE(ServiceURL.SERVICE_URL);
				try {

                    androidHttpTranport.call(NAMESPACE + METHOD_NAME_SAVECIF, envelope);
					Object response = envelope.getResponse();

					SoapPrimitive sa = (SoapPrimitive) envelope.getResponse();

					inputciflist = sa.toString();

				} catch (IOException e) {
					try {
						throw (e);
					} catch (Exception e1) {
						e1.printStackTrace();
					}
					mProgressDialog.dismiss();
					running = false;
				} catch (XmlPullParserException e) {
					try {
						throw (e);
					} catch (Exception e1) {
						e1.printStackTrace();
					}
					mProgressDialog.dismiss();
					running = false;
				}
			} catch (Exception e) {
				try {
					throw (e);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				mProgressDialog.dismiss();
				running = false;
			}
			return null;

		}

		@Override
		protected void onPostExecute(String result) {
			if (mProgressDialog.isShowing())
				mProgressDialog.dismiss();
			if (running) {

				if (inputciflist.equalsIgnoreCase("1")) {

					try {

						M_UserInformation DashboardDetail_data = new M_UserInformation(
								quotation_no,
								str_PF_NO,
								Fragment_PersonalDetails.str_candidate_corporate_name,
								"Completed",
								Fragment_ContactDetails.str_mobile_no,
								Fragment_ContactDetails.str_email_id,
								Fragment_Identification.createdDate, true);

						DashboardDetail_data.setStr_aadhar_card_no(str_aadhar_card_no);
						DashboardDetail_data.setStr_contact_person_email_id(str_contact_person_eamil_id);

						db.insertDashBoardDetail(DashboardDetail_data);

						M_Sync_Status Sync_data = new M_Sync_Status(
								quotation_no, STATUS_DATA);
						db.insertSyncStatus(Sync_data);

						showAlert
								.setMessage("Your details have been successfully submitted. The �Unique Reference Number (URN)� will be communicated as soon as generated at Corporate Centre.");
						showAlert.setNeutralButton("OK",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
														int which) {
										Intent i = new Intent(
												context,
												CIFEnrollmentDashboardActivity.class);
										startActivity(i);
									}
								});
						showAlert.show();

					} catch (Exception ex) {
						ex.printStackTrace();
					}

				} else if (inputciflist.equalsIgnoreCase("2")) {

					mCommonMethods.showToast(context, "Data already exist ");

					try {
						db.DeleteProposalFromDashboard_New(str_PF_NO);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					mCommonMethods.showToast(context, "Server not responding,Please try again from Dashboard");
				}

			} else {
				mCommonMethods.showToast(context, "Server not responding,Please try again from Dashboard");
			}
		}

	}

	private void getInput() {
		inputVal = new StringBuilder();
		inputVal.append("<?xml version='1.0' encoding='utf-8' ?><cif>");
		inputVal.append("<quotation_number>" + quotation_no.trim()
				+ "</quotation_number>");
		inputVal.append("<pf_number>" + str_PF_NO.trim() + "</pf_number>");
		inputVal.append("<candidate_corporate_name>"
				+ str_candidate_corporate_name.trim()
				+ "</candidate_corporate_name>");
		inputVal.append("<father_name>" + str_father_name.trim()
				+ "</father_name>");
		inputVal.append("<category>" + str_category.trim() + "</category>");
		inputVal.append("<mobile_no>" + str_mobile_no.trim() + "</mobile_no>");
		inputVal.append("<phone_number>" /* + str_phone_number_1.trim() + "-" */
				+ str_phone_number_2.trim() + "</phone_number>");
		inputVal.append("<email_id>" + str_email_id.trim() + "</email_id>");

		inputVal.append("<basic_qualification>"
				+ str_basic_qualification.trim() + "</basic_qualification>");
		inputVal.append("<board_name_for_basic_qualification>"
				+ str_board_name_for_basic_qualification.trim()
				+ "</board_name_for_basic_qualification>");
		inputVal.append("<roll_number_for_basic_qualification>"
				+ str_roll_number_for_basic_qualification.trim()
				+ "</roll_number_for_basic_qualification>");
		inputVal.append("<year_of_passing_for_basic_qualification>"
				+ str_year_of_passing.trim()
				+ "</year_of_passing_for_basic_qualification>");
		inputVal.append("<educational_qualification>"
				+ str_educational_qualification.trim()
				+ "</educational_qualification>");
		inputVal.append("<other_qualification>"
				+ str_other_qualification.trim() + "</other_qualification>");

		inputVal.append("<insurance_category>" + str_insurance_category.trim()
				+ "</insurance_category>");
		inputVal.append("<state>" + str_state.trim() + "</state>");
		inputVal.append("<city>" + str_city.trim() + "</city>");
		inputVal.append("<exam_center_location>"
				+ str_exam_center_location.trim() + "</exam_center_location>");
		inputVal.append("<exam_language>" + str_exam_language.trim()
				+ "</exam_language>");

		inputVal.append("<photoByteArrayAsString>"
				+ photoByteArrayAsString.trim() + "</photoByteArrayAsString>");
		inputVal.append("<proposer_sign>" + proposer_sign.trim()
				+ "</proposer_sign>");

		inputVal.append("<dob>" + str_dob.trim() + "</dob>");
		inputVal.append("<sex>" + str_sex.trim() + "</sex>");

		inputVal.append("<pan_no>" + str_pan.trim() + "</pan_no>");
		inputVal.append("<branch_name>" + str_branch_name.trim()
				+ "</branch_name>");
		inputVal.append("<nationality>" + "INDIA" + "</nationality>");

		inputVal.append("<createdDate>" + str_created_dates.trim()
				+ "</createdDate>");
		inputVal.append("<primary_profession>" + "Banker"
				+ "</primary_profession>");
		inputVal.append("<exam_mode>" + "ONLINE" + "</exam_mode>");
		inputVal.append("<exam_body_name>" + "NSE.IT" + "</exam_body_name>");
		inputVal.append("<area>" + str_area.trim() + "</area>");

		inputVal.append("<current_house_number>"
				+ str_current_house_number.trim() + "</current_house_number>");
		inputVal.append("<current_street>" + str_current_street.trim()
				+ "</current_street>");
		// inputVal.append("<current_town>" + str_current_town +
		// "</current_town>");
		inputVal.append("<current_district>" + str_current_district.trim()
				+ "</current_district>");
		inputVal.append("<current_pincode>" + str_current_pincode.trim()
				+ "</current_pincode>");

		// inputVal.append("<permanent_house_number>" + str_current_house_number
		// + "</permanent_house_number>");
		// inputVal.append("<permanent_street>" + str_current_street
		// + "</permanent_street>");
		// inputVal.append("<permanent_town>" + str_current_town
		// + "</permanent_town>");
		//
		// inputVal.append("<permanent_pincode>" + str_current_pincode
		// + "</permanent_pincode>");

		inputVal.append("<permanent_district>" + str_current_district.trim()
				+ "</permanent_district>");

		inputVal.append("<cor_type>" + "Specified Person" + "</cor_type>");

		//added by rajan 24-10-2017
		inputVal.append("<AADHAR_NO>" + str_aadhar_card_no
				+ "</AADHAR_NO>");
		inputVal.append("<CIF_CONTACTPERSON_EMAIL_ID>" + str_contact_person_eamil_id
				+ "</CIF_CONTACTPERSON_EMAIL_ID>");

		//added by rajan 25-09-2018
		inputVal.append("<CIF_IS_ANNEXURE_UPLOADED>" + is_annexure_uploaded
				+ "</CIF_IS_ANNEXURE_UPLOADED>");

        //added by rajan 10-01-2019
        inputVal.append("<CIF_IS_ID_CARD_UPLOADED>" + is_id_card_uploaded
                + "</CIF_IS_ID_CARD_UPLOADED>");

		//added by rajan 10-01-2019
		inputVal.append("<CIF_IS_PAN_CARD_UPLOADED>" + is_pan_card_uploaded
				+ "</CIF_IS_PAN_CARD_UPLOADED>");

		//added by rajan 14-05-2021
		inputVal.append("<BDM_email>" + str_bdm_email
				+ "</BDM_email>");

		inputVal.append("<BDM_mobile>" + str_bdm_mobile
				+ "</BDM_mobile>");

		inputVal.append("<Sales_Support_email>" + str_sales_support_email
				+ "</Sales_Support_email>");

		inputVal.append("<Sales_Support_mobile>" + str_sales_support_mobile
				+ "</Sales_Support_mobile>");
		//end by rajan 14-05-2021

		inputVal.append("</cif>");

	}

	private void SyncStatus() {
		try {
			List<M_Sync_Status> list_sync_Status = db.getSyncStatus(quotation_no);
			int i = 0;
			if (list_sync_Status.size() > 0) {

				SyncStatus = list_sync_Status.get(i).getSyncStatus();
			}

		} catch (Exception e) {
			Log.e("Dbconnection", e.toString()
					+ "Error in getting Sync status from database");
			e.printStackTrace();
		}
	}

	private void SyncAgain() {

		SyncStatus();

		if (SyncStatus.equalsIgnoreCase("") || SyncStatus.equalsIgnoreCase("0")) {
			//SyncData();
			checkFlag = STATUS_SIGN;
			createSoapRequestToUploadDoc();
		} else if (SyncStatus.equalsIgnoreCase(STATUS_SIGN)) {
			checkFlag = STATUS_PHOTO;
			createSoapRequestToUploadDoc();
		} else if (SyncStatus.equalsIgnoreCase(STATUS_PHOTO)) {
			//SyncData_image_sign();
			SyncData();
		}
	}

	private String Date_dd_mmm_yyyy(String createdDate) {

		String NewDate = "";
		SimpleDateFormat format1 = new SimpleDateFormat("MM-dd-yyyy");
		SimpleDateFormat format2 = new SimpleDateFormat("dd-MMM-yyyy");
		Date date;
		try {
			date = format1.parse(createdDate);
			NewDate = format2.format(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return NewDate;
	}

	private void createSoapRequestToUploadDoc(){

		SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME_UPLOADFILE_CIF);

		if (checkFlag.equalsIgnoreCase(STATUS_SIGN)) {

			request.addProperty("f", str_signature);
			request.addProperty("fileName", str_PF_NO + "Sign.jpg");
			request.addProperty("strType", "S");

		}else if(checkFlag.equals(STATUS_PHOTO)){
			request.addProperty("f", str_photo);
			request.addProperty("fileName", str_PF_NO + "Photo.jpg");
			request.addProperty("strType", "P");
		}
		request.addProperty("PFNo", str_PF_NO);

		//for UAT
		//request.addProperty("Authkey", mCommonMethods.getStr_cif_auth_key());

		new AsyncUploadFile_CIF(getContext(), this, request, METHOD_NAME_UPLOADFILE_CIF).execute();
	}

	@Override
	public void onUploadComplete(Boolean result) {
		if (result) {

			try {
				if (checkFlag.equals(STATUS_SIGN)) {


					long rowId2 = db.insertDashBoardDetail_Status(
							quotation_no, "Document Photo Pending");

					if (rowId2 > 0)
						mCommonMethods.showToast(context, "Data Inserted Successfully");

					db.insertDashBoardDetail_Status_New(quotation_no, STATUS_SIGN);

					if (mCommonMethods.isNetworkConnected(context)) {
						//SyncAgain();
						checkFlag = STATUS_PHOTO;
						createSoapRequestToUploadDoc();
					} else {
						mCommonMethods.showToast(context, "Document Signature Uploaded Successfully!!");

						Intent i = new Intent(context,
								CIFEnrollmentDashboardActivity.class);
						startActivity(i);
					}


				} else if (checkFlag.equals(STATUS_PHOTO)){


					db.insertDashBoardDetail_Status(
							quotation_no, "Data Synch Pending");

					db.insertDashBoardDetail_Status_New(quotation_no, STATUS_PHOTO);

					if (mCommonMethods.isNetworkConnected(context)) {
						//SyncData_image_sign();
						//SyncAgain();
						SyncData();
					} else {
						mCommonMethods.showToast(context, "Photo Document Synced Successfully");
						Intent i = new Intent(context, CIFEnrollmentDashboardActivity.class);
						startActivity(i);
					}
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		} else {
			mCommonMethods.showToast(context, "Server not responding,Please try again from Dashboard");
		}
	}
}
