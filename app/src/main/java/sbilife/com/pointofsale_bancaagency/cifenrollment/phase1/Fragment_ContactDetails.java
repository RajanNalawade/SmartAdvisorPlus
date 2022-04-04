package sbilife.com.pointofsale_bancaagency.cifenrollment.phase1;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import sbilife.com.pointofsale_bancaagency.DatabaseHelper;
import sbilife.com.pointofsale_bancaagency.ParseXML;
import sbilife.com.pointofsale_bancaagency.R;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;

public class Fragment_ContactDetails extends Fragment {

	private EditText edt_mobile_no, edt_phone_number_2, edt_email_id,
			edt_re_enter_email_id, edt_contact_person_email_id;
    private Button btn_back;
    public static String str_mobile_no = "";
    public static String str_phone_number_2 = "";
    public static String str_email_id = "";
    public static String str_re_enter_email_id = "";
    private static String str_aadhar_card_no = "";
    private static String str_contact_person_eamil_id = "";

	private DatabaseHelper db;

    private StringBuilder inputVal;
	private String str_city = "";
    private String str_state = "";
    private String str_exam_center_location = "";
    private String str_dob = "";
    private String str_sex = "";
    private String str_pan_no = "";
    private String str_branch_name = "";
    private String str_current_district = "";
    private String str_permanent_district = "";
    private String str_address1 = "";
    private String str_address2 = "";
    private String str_pincode = "";
    private String str_state_id = "";
    private String str_area = "";
    private String str_pf_numbers = "";
    private String str_candidate_corporate_name = "";
    private String str_father_name = "";
    private String str_category = "";
    private String quotation_no = "";
    private String emailId = "";
    private String ConfirmEmailId = "";
    private CommonMethods mCommonMethods;
    private Context mContext;

    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater
				.inflate(R.layout.cifenrollment_layout_contact_details,
						container, false);

		mContext = getActivity();
		mCommonMethods = new CommonMethods();
		db = new DatabaseHelper(mContext);

		edt_mobile_no = rootView.findViewById(R.id.edt_mobile_no);
		// edt_phone_number_1 = (EditText)
		// rootView.findViewById(R.id.edt_phone_number_1);
		edt_phone_number_2 = rootView
				.findViewById(R.id.edt_phone_number_2);
		edt_email_id = rootView.findViewById(R.id.edt_email_id);
		edt_re_enter_email_id = rootView
				.findViewById(R.id.edt_re_enter_email_id);
		edt_contact_person_email_id = rootView
				.findViewById(R.id.edt_contact_person_email_id);

        Button btn_next = rootView.findViewById(R.id.btn_next);
		// btn_back = (Button) rootView.findViewById(R.id.btn_back);
        TableRow tr_button = rootView.findViewById(R.id.tr_contact_button);
		tr_button.setVisibility(View.VISIBLE);

		String dashboard = "";

		dashboard = CIFEnrollmentMainActivity.dashboard;
		if (dashboard != null && dashboard.equalsIgnoreCase("true")) {
			quotation_no = CIFEnrollmentMainActivity.quotation_dashboard;
			String isFlag1 = CIFEnrollmentMainActivity.isFlag1;
			if (isFlag1.equalsIgnoreCase("true")) {
				tr_button.setVisibility(View.GONE);
			} else {
				tr_button.setVisibility(View.VISIBLE);
			}
			// setCIFInputGui();
		} else {
			quotation_no = CIFEnrollmentPFActivity.quotation_Number;
		}

		setCIFInputGui();

		edt_phone_number_2.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
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
				String abc = edt_phone_number_2.getText().toString();
				//phone_validation(abc);
				mobile_validation(edt_phone_number_2, abc);

			}
		});

		edt_mobile_no.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
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
				String abc = edt_mobile_no.getText().toString().trim();
				mobile_validation(edt_mobile_no, abc.trim());

			}
		});

		edt_email_id.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
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
				str_email_id = edt_email_id.getText().toString();
				email_id_validation(str_email_id.trim());

			}
		});

		edt_re_enter_email_id.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
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
				String proposer_confirm_emailId = edt_re_enter_email_id
						.getText().toString();
				confirming_email_id(proposer_confirm_emailId.trim());

			}
		});

		btn_next.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				str_mobile_no = edt_mobile_no.getText().toString();
				// str_phone_number_1 = edt_phone_number_1.getText().toString();
				str_phone_number_2 = edt_phone_number_2.getText().toString();
				str_email_id = edt_email_id.getText().toString();
				str_re_enter_email_id = edt_re_enter_email_id.getText()
						.toString();
				str_contact_person_eamil_id = edt_contact_person_email_id.getText()
						.toString();

				if (validation()) {
					getValueFromDatabase_New();
					getInput();

					// M_MainActivity_Data data = new M_MainActivity_Data(
					// quotation_no,
					// LoginActivity.str_pf_number,
					// Fragment_PersonalDetails.str_candidate_corporate_name,
					// Fragment_PersonalDetails.str_father_name,
					// Fragment_PersonalDetails.str_category,
					// str_mobile_no, str_phone_number_1 + "-"
					// + str_phone_number_2, str_email_id);

					M_MainActivity_Data data = new M_MainActivity_Data(
							quotation_no, str_pf_numbers, new String(inputVal));

					try {
						long count = db.insertCIFDetail_New(data, quotation_no);
						if (count > 0) {
							Toast toast = Toast.makeText(getActivity(),
									"Data Inserted Successfully",
									Toast.LENGTH_SHORT);
							toast.show();
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					M_UserInformation DashboardDetail_data = new M_UserInformation(
							quotation_no,
							str_pf_numbers,
							Fragment_PersonalDetails.str_candidate_corporate_name,
							str_mobile_no, str_email_id);
					DashboardDetail_data.setStr_aadhar_card_no(str_aadhar_card_no);
					DashboardDetail_data.setStr_contact_person_email_id(str_contact_person_eamil_id);

					try {
						long rowId2 = db
								.insertDashBoardDetail(DashboardDetail_data);
						if (rowId2 > 0) {
							Toast toast = Toast.makeText(getActivity(),
									"Data Inserted Successfully",
									Toast.LENGTH_SHORT);
							toast.show();
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					((CIFEnrollmentMainActivity) getActivity()).getViewpager()
							.setCurrentItem(2);
				}
			}
		});

		// str_mobile_no = LoginActivity.str_mobile_no;
		// if (str_mobile_no != null && !str_mobile_no.equalsIgnoreCase("")) {
		// edt_mobile_no.setText(str_mobile_no);
		// }
		//
		// str_email_id = LoginActivity.str_email_id;
		// if (str_email_id != null && !str_email_id.equalsIgnoreCase("")) {
		// edt_email_id.setText(str_email_id);
		// edt_re_enter_email_id.setText(str_email_id);
		// }

		return rootView;
	}

	private void setCIFInputGui() {
		if (getValueFromDatabase()) {

			// Contact

			if (str_mobile_no != null) {

				edt_mobile_no.setText(str_mobile_no);

			} else {
				str_mobile_no = "";
			}
			/*
			 * if (str_phone_number_1 != null) {
			 * 
			 * edt_phone_number_1.setText(str_phone_number_1);
			 * 
			 * } else { str_phone_number_1 = ""; }
			 */
			if (str_phone_number_2 != null) {

				edt_phone_number_2.setText(str_phone_number_2);

			} else {
				str_phone_number_2 = "";
			}
			if (str_email_id != null) {

				edt_email_id.setText(str_email_id.toLowerCase());

			} else {
				str_email_id = "";
			}
			if (str_re_enter_email_id != null) {

				edt_re_enter_email_id.setText(str_re_enter_email_id
						.toLowerCase());

			} else {
				str_re_enter_email_id = "";
			}

			if (str_contact_person_eamil_id != null){
				edt_contact_person_email_id.setText(str_contact_person_eamil_id
						.toLowerCase().trim());
			}else{
				str_contact_person_eamil_id = "";
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
			str_pf_numbers = prsObj.parseXmlTag(input, "pf_number");
			// str_candidate_corporate_name = prsObj.parseXmlTag(input,
			// "candidate_corporate_name");
			// str_father_name = prsObj.parseXmlTag(input, "father_name");
			// str_category = prsObj.parseXmlTag(input, "category");

			// Contact details

			str_mobile_no = prsObj.parseXmlTag(input, "mobile_no");
            str_phone_number_2 = prsObj.parseXmlTag(input, "phone_number");
			/*
			 * if (str_phone_number != null &&
			 * !str_phone_number.equalsIgnoreCase("")) {
			 * 
			 * String[] array = str_phone_number.split("-");
			 * //str_phone_number_1 = array[0]; str_phone_number_2 = array[1]; }
			 */

			str_email_id = prsObj.parseXmlTag(input, "email_id");
			str_re_enter_email_id = str_email_id;

			str_state = prsObj.parseXmlTag(input, "state");
			str_city = prsObj.parseXmlTag(input, "city");
			str_exam_center_location = prsObj.parseXmlTag(input,
					"exam_center_location");

			str_branch_name = prsObj.parseXmlTag(input, "branch_name");
			str_pan_no = prsObj.parseXmlTag(input, "pan_no");
			str_sex = prsObj.parseXmlTag(input, "sex");
			str_dob = prsObj.parseXmlTag(input, "dob");
			str_area = prsObj.parseXmlTag(input, "area");

			str_address1 = prsObj.parseXmlTag(input, "current_house_number");
			str_address2 = prsObj.parseXmlTag(input, "current_street");
			str_pincode = prsObj.parseXmlTag(input, "current_pincode");
			str_current_district = prsObj
					.parseXmlTag(input, "current_district");
			str_permanent_district = prsObj.parseXmlTag(input,
					"permanent_district");

			//added by rajan 24-10-2017
			str_aadhar_card_no = prsObj
					.parseXmlTag(input, "AADHAR_NO");
			str_aadhar_card_no = str_aadhar_card_no == null ? "": str_aadhar_card_no;
			str_contact_person_eamil_id = prsObj
					.parseXmlTag(input, "CIF_CONTACTPERSON_EMAIL_ID");

			flag = true;

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

			//added by rajan 24-10-2017
			str_aadhar_card_no = prsObj
					.parseXmlTag(input, "AADHAR_NO");
			str_aadhar_card_no = str_aadhar_card_no == null ? "": str_aadhar_card_no;

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
		// if (LoginActivity.str_pf_number.trim().equals("")) {
		//
		// Toast.makeText(getActivity(), "Please Fill Banca-Cif Form First",
		// Toast.LENGTH_LONG).show();
		//
		// ((NewMainActivity) getActivity()).getViewpager().setCurrentItem(0);
		// return false;
		// } else

		if (Fragment_PersonalDetails.str_candidate_corporate_name.trim()
				.equals("")
				|| Fragment_PersonalDetails.str_father_name.trim().equals("")
				|| Fragment_PersonalDetails.str_category.trim().equals("")) {

			Toast.makeText(getActivity(),
					"Please Fill Personal Details Form First",
					Toast.LENGTH_LONG).show();

			((CIFEnrollmentMainActivity) getActivity()).getViewpager()
					.setCurrentItem(0);
			return false;
		} else if (str_mobile_no.trim().equals("")) {

			Toast.makeText(getActivity(), "Please Enter Mobile Number",
					Toast.LENGTH_LONG).show();
			return false;
		} else if (str_mobile_no.trim().length() < 10) {

			Toast.makeText(getActivity(),
					"Please Enter 10 Digit mobile Number", Toast.LENGTH_LONG)
					.show();
			return false;
		} else if (str_phone_number_2.trim().equals("")) {

			Toast.makeText(getActivity(), "Please Enter Telephone Number",
					Toast.LENGTH_LONG).show();
			return false;
		} else if (str_phone_number_2.trim().length() < 10) {

			Toast.makeText(getActivity(), "Please Enter 10 Digit Phone Number",
					Toast.LENGTH_LONG).show();
			return false;
		} else if (str_email_id.trim().equals("")) {

			Toast.makeText(getActivity(), "Please Enter Email Id",
					Toast.LENGTH_LONG).show();
			return false;
		} else if (str_re_enter_email_id.trim().equals("")) {

			Toast.makeText(getActivity(), "Please Re-Enter Email Id",
					Toast.LENGTH_LONG).show();
			return false;
		}

		if (email_id_validation(str_contact_person_eamil_id)){
			if (str_email_id.equals(str_contact_person_eamil_id)){
				new CommonMethods().showMessageDialog(getContext(),
						"Candidate Email ID and Contact Person Email ID should not be same");
				return false;
			}
		}

		return true;
	}

	private boolean email_id_validation(String email_id) {

        String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email_id);
        boolean validationFla1 = false;
        if (!(matcher.matches())) {
			edt_email_id.setError("Please provide the correct email address");
			validationFla1 = false;
		} else if ((matcher.matches())) {
			validationFla1 = true;
		}

		return validationFla1;
	}

	private void confirming_email_id(String email_id) {

        boolean validationFlag2 = false;
        if (!(email_id.equals(str_email_id))) {
			edt_re_enter_email_id.setError("Email id does not match");
			validationFlag2 = false;
		} else if ((email_id.equals(str_email_id))) {
			validationFlag2 = true;
		}

	}

	private void getInput() {
		inputVal = new StringBuilder();
		inputVal.append("<?xml version='1.0' encoding='utf-8' ?><cif>");
		inputVal.append("<quotation_number>" + quotation_no
				+ "</quotation_number>");
		inputVal.append("<pf_number>" + str_pf_numbers + "</pf_number>");
		inputVal.append("<candidate_corporate_name>"
				+ str_candidate_corporate_name + "</candidate_corporate_name>");
		inputVal.append("<father_name>" + str_father_name + "</father_name>");
		inputVal.append("<category>" + str_category + "</category>");
		inputVal.append("<mobile_no>" + str_mobile_no + "</mobile_no>");
		inputVal.append("<phone_number>" /* + str_phone_number_1 + "-" */
				+ str_phone_number_2 + "</phone_number>");
		inputVal.append("<email_id>" + str_email_id + "</email_id>");

		inputVal.append("<state>" + str_state + "</state>");
		inputVal.append("<city>" + str_city + "</city>");
		inputVal.append("<exam_center_location>" + str_exam_center_location
				+ "</exam_center_location>");

		inputVal.append("<dob>" + str_dob + "</dob>");
		inputVal.append("<sex>" + str_sex + "</sex>");
		inputVal.append("<pan_no>" + str_pan_no + "</pan_no>");
		inputVal.append("<branch_name>" + str_branch_name + "</branch_name>");
		inputVal.append("<current_house_number>" + str_address1
				+ "</current_house_number>");
		inputVal.append("<current_street>" + str_address2 + "</current_street>");
		inputVal.append("<current_district>" + str_current_district
				+ "</current_district>");
		inputVal.append("<current_pincode>" + str_pincode
				+ "</current_pincode>");
		inputVal.append("<area>" + str_area + "</area>");
		inputVal.append("<permanent_district>" + str_permanent_district
				+ "</permanent_district>");

		//added by rajan 24-10-2017
		inputVal.append("<AADHAR_NO>" + str_aadhar_card_no
				+ "</AADHAR_NO>");
		inputVal.append("<CIF_CONTACTPERSON_EMAIL_ID>" + str_contact_person_eamil_id
				+ "</CIF_CONTACTPERSON_EMAIL_ID>");

		inputVal.append("</cif>");
	}

	private void mobile_validation(EditText editText, String number) {
        boolean validationFlag3 = false;

		number = number.trim();
        if ((number.length() != 10)) {
			editText
					.setError("Please provide correct 10-digit mobile number");
			validationFlag3 = false;
		} else if ((number.length() == 10)) {
			validationFlag3 = true;
		}
	}
}