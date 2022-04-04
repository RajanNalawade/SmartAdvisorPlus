package sbilife.com.pointofsale_bancaagency.cifenrollment.phase1;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.util.List;

import sbilife.com.pointofsale_bancaagency.DatabaseHelper;
import sbilife.com.pointofsale_bancaagency.ParseXML;
import sbilife.com.pointofsale_bancaagency.R;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;
import sbilife.com.pointofsale_bancaagency.common.Verhoeff;

public class Fragment_PersonalDetails extends Fragment {

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		System.out.println("Onresume");
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		System.out.println("onStart");
	}

	private EditText edt_candidate_corporate_name, edt_father_name, edt_pf_number, edt_aadhar_card_no;
	private Spinner spnr_category;
	private Button btn_back;
    public static String str_candidate_corporate_name = "", str_father_name = "",
			str_category = "";
	private String str_mobile_no = "", str_email_id = "", str_city = "",
			str_state = "", str_exam_center_location = "", str_dob = "",
			str_sex = "", str_pan_no = "", str_branch_name = "",
			str_current_district = "", str_permanent_district = "",
			str_address1 = "", str_address2 = "", str_pincode = "",
			str_state_id = "", str_area = "", str_aadhar_card_no = "", str_contact_person_eamil_id = "",
			str_pf_numbers = "", quotation_no = "";
	private DatabaseHelper db;
	private StringBuilder inputVal;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		// outState.putString(key, value);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.cifenrollment_layout_personal_details, container, false);

		db = new DatabaseHelper(getActivity());

		edt_candidate_corporate_name = rootView
				.findViewById(R.id.edt_candidate_corporate_name);
		edt_father_name = rootView
				.findViewById(R.id.edt_father_name);

		edt_pf_number = rootView.findViewById(R.id.edt_pf_number);

		spnr_category = rootView.findViewById(R.id.spnr_category);
        Button btn_next = rootView.findViewById(R.id.btn_next);
		// btn_back = (Button) rootView.findViewById(R.id.btn_back);
		edt_aadhar_card_no = rootView.findViewById(R.id.edt_aadhar_card_no);

        TableRow tr_button = rootView.findViewById(R.id.tr_personal_button);
		tr_button.setVisibility(View.VISIBLE);

		String[] category_Arr = new String[]{"GEN", "SC", "ST", "OBC"};
		ArrayAdapter<String> category_adapter = new ArrayAdapter<String>(
				getActivity(), R.layout.spinner_item, category_Arr);
		category_adapter.setDropDownViewResource(R.layout.spinner_item1);
		spnr_category.setAdapter(category_adapter);
		category_adapter.notifyDataSetChanged();

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
			//	setCIFInputGui();
		} else {
			quotation_no = CIFEnrollmentPFActivity.quotation_Number;
		}

		setCIFInputGui();

		spnr_category.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
									   int arg2, long arg3) {
				// TODO Auto-generated method stub
				str_category = spnr_category.getSelectedItem().toString();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});

		edt_aadhar_card_no.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

			}

			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

			}

			@Override
			public void afterTextChanged(Editable editable) {
				String str_faadhar = edt_aadhar_card_no
						.getText().toString().trim();

				if (!Verhoeff.validateVerhoeff(str_faadhar)) {
					edt_aadhar_card_no
							.setError("Incorrect UID number");
				} else {
					edt_aadhar_card_no.setError(null);
				}
			}
		});

		btn_next.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				str_candidate_corporate_name = edt_candidate_corporate_name
						.getText().toString();
				str_father_name = edt_father_name.getText().toString();

				str_aadhar_card_no = edt_aadhar_card_no.getText().toString().trim();
				str_aadhar_card_no = str_aadhar_card_no == null ? "" : str_aadhar_card_no;

				String str_valid_error = validation();

				if (str_valid_error.equals("")) {

					getInput();

					// M_MainActivity_Data data = new M_MainActivity_Data(
					// quotation_no, LoginActivity.str_pf_number,
					// str_candidate_corporate_name, str_father_name,
					// str_category);
					M_MainActivity_Data data = new M_MainActivity_Data(
							quotation_no, str_pf_numbers,
							new String(inputVal));
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
							quotation_no, str_pf_numbers,
							str_candidate_corporate_name);
					DashboardDetail_data.setStr_aadhar_card_no(str_aadhar_card_no);
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
							.setCurrentItem(1);

				}else{
					new CommonMethods().showMessageDialog(getActivity(), str_valid_error);
				}

			}
		});
//		str_candidate_corporate_name = LoginActivity.str_candidate_corporate_name;
//		if (str_candidate_corporate_name != null
//				&& !str_candidate_corporate_name.equalsIgnoreCase("")) {
//			edt_candidate_corporate_name.setText(str_candidate_corporate_name);
//		}
//
//		String str_pf_number = LoginActivity.str_pf_number;
//		if (str_pf_number != null && !str_pf_number.equalsIgnoreCase("")) {
//			edt_pf_number.setText(str_pf_number);
//		}

		return rootView;
	}

	private void setCIFInputGui() {
		if (getValueFromDatabase()) {

			// personal

			edt_pf_number.setText(str_pf_numbers);

			if (str_candidate_corporate_name != null) {

				edt_candidate_corporate_name.setText(str_candidate_corporate_name);

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

				String[] category_Arr = new String[]{"GEN", "SC", "ST", "OBC"};
				ArrayAdapter<String> category_adapter = new ArrayAdapter<String>(
						getActivity(), R.layout.spinner_item, category_Arr);
				category_adapter
						.setDropDownViewResource(R.layout.spinner_item1);
				spnr_category.setAdapter(category_adapter);
				category_adapter.notifyDataSetChanged();

				str_category = "";
			}

			if (!str_aadhar_card_no.equals("")) {
				edt_aadhar_card_no.setText(str_aadhar_card_no.trim());
			} else {
				str_aadhar_card_no = "";
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

			// str_pf_numbers = data.get(i).getStr_pf_number();
			//
			// str_candidate_corporate_name = data.get(i)
			// .getStr_candidate_corporate_name();
			// str_father_name = data.get(i).getStr_father_name();
			//
			// str_category = data.get(i).getStr_category();

			String input = data.get(i).getInput();
			str_pf_numbers = prsObj.parseXmlTag(input, "pf_number");
			str_candidate_corporate_name = prsObj.parseXmlTag(input,
					"candidate_corporate_name");
			str_father_name = prsObj.parseXmlTag(input, "father_name");
			str_category = prsObj.parseXmlTag(input, "category");

			str_mobile_no = prsObj.parseXmlTag(input, "mobile_no");
			str_email_id = prsObj.parseXmlTag(input, "email_id");
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
			str_aadhar_card_no = prsObj.parseXmlTag(input, "AADHAR_NO");
			str_aadhar_card_no = str_aadhar_card_no == null ? "" : str_aadhar_card_no;

			str_contact_person_eamil_id = prsObj.parseXmlTag(input, "CIF_CONTACTPERSON_EMAIL_ID");

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

	private String validation() {

		String str_error = "";

		if (str_candidate_corporate_name.trim().equals("")) {

			str_error = "Please Enter Your Name\n";

		} else if (str_father_name.trim().equals("")) {

			str_error = "Please Enter Father's Name\n";

		} else if (str_category.trim().equals("")) {

			str_error = "Please Select Category\n";
		}

		//ekyc hide
		/*if (!Verhoeff.validateVerhoeff(str_aadhar_card_no)) {
				str_error = "Please Enter proper Aadhar Card No.\n";
		}*/

		return str_error;
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
}
