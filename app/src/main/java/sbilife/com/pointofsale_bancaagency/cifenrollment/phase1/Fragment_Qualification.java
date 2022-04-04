package sbilife.com.pointofsale_bancaagency.cifenrollment.phase1;

import android.os.Bundle;
import android.text.Html;
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
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.util.List;

import sbilife.com.pointofsale_bancaagency.DatabaseHelper;
import sbilife.com.pointofsale_bancaagency.ParseXML;
import sbilife.com.pointofsale_bancaagency.R;

public class Fragment_Qualification extends Fragment {

	private EditText edt_basic_qualification, edt_board_name_for_basic_qualification,
			edt_roll_number_for_basic_qualification;
	private Spinner spnr_educational_qualification, spnr_other_qualification,
			spnr_year_of_passing_for_basic_qualification;
    private Button btn_back;

    private TableRow tr_other_qualification;

    public static String str_basic_qualification = "",
			str_board_name_for_basic_qualification = "",
			str_roll_number_for_basic_qualification = "",
			str_year_of_passing_for_basic_qualification = "",
			str_other_qualification = "", str_educational_qualification = "";

	private DatabaseHelper db;
	private StringBuilder inputVal;

	private String str_pf_numbers = "";
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
    private String str_candidate_corporate_name = "";
    private String str_father_name = "";
    private String str_category = "";
    private String str_aadhar_card_no = "";
    private String str_mobile_no = "";
    private String str_phone_number_2 = "";
    private String str_email_id = "";
    private String str_contact_person_eamil_id = "";
    private String quotation_no = "";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(
				R.layout.cifenrollment_layout_qualification, container, false);

		db = new DatabaseHelper(getActivity());

		edt_basic_qualification = rootView
				.findViewById(R.id.edt_basic_qualification);
		//edt_basic_qualification.setText("Class XII");

		edt_basic_qualification.setText("Class XII or Equivalent Class");

		edt_board_name_for_basic_qualification = rootView
				.findViewById(R.id.edt_board_name_for_basic_qualification);
		edt_roll_number_for_basic_qualification = rootView
				.findViewById(R.id.edt_roll_number_for_basic_qualification);
		spnr_year_of_passing_for_basic_qualification = rootView
				.findViewById(R.id.spnr_year_of_passing_for_basic_qualification);
		spnr_other_qualification = rootView
				.findViewById(R.id.spnr_other_qualification);
		spnr_educational_qualification = rootView
				.findViewById(R.id.spnr_educational_qualification);

        Button btn_next = rootView.findViewById(R.id.btn_next);
		// btn_back = (Button) rootView.findViewById(R.id.btn_back);
        TableRow tr_button = rootView
                .findViewById(R.id.tr_qualification_button);
		tr_button.setVisibility(View.VISIBLE);

		tr_other_qualification = rootView
				.findViewById(R.id.tr_other_qualification);

        TextView tv_basic_qualification = rootView
                .findViewById(R.id.tv_basic_qualification);

        TextView tv_board_name_for_basic_qualification = rootView
                .findViewById(R.id.tv_board_name_for_basic_qualification);

        TextView tv_roll_number_for_basic_qualification = rootView
                .findViewById(R.id.tv_roll_number_for_basic_qualification);

        TextView tv_year_of_passing_for_basic_qualification = rootView
                .findViewById(R.id.tv_year_of_passing_for_basic_qualification);

        TextView tv_educational_qualification = rootView
                .findViewById(R.id.tv_educational_qualification);

        TextView tv_other_qualification = rootView
                .findViewById(R.id.tv_other_qualification);

		tv_basic_qualification.setText(Html
				.fromHtml("<font color='#000000'>Basic Qualification </font>"
						+ "<font color ='#FF0000'><small>*</small></font>"));

		tv_board_name_for_basic_qualification
				.setText(Html
						.fromHtml("<font color='#000000'>Board Name for Basic Qualification </font>"
								+ "<font color ='#FF0000'><small>*</small></font>"));

		tv_roll_number_for_basic_qualification
				.setText(Html
						.fromHtml("<font color='#000000'>Roll Number for Basic Qualification </font>"
								+ "<font color ='#FF0000'><small>*</small></font>"));

		tv_year_of_passing_for_basic_qualification
				.setText(Html
						.fromHtml("<font color='#000000'>Year of Passing for Basic Qualification </font>"
								+ "<font color ='#FF0000'><small>*</small></font>"));

		tv_educational_qualification
				.setText(Html
						.fromHtml("<font color='#000000'>Educational Qualification </font>"
								+ "<font color ='#FF0000'><small>*</small></font>"));

		tv_other_qualification.setText(Html
				.fromHtml("<font color='#000000'>Other Qualification </font>"
						+ "<font color ='#FF0000'><small>*</small></font>"));

		String[] educational_qualification_Arr = new String[] {
				/*"Others",*/"Not Applicable",
				"Associate / Fellow of Institute of Actuaries of India",
				"Associate / Fellow of CII London",
				"Associate / Fellow of Insurance Institute of India",
				"Post Graduate Qualification of Institute of Insurance and Risk Management Hyderabad Others" };
		ArrayAdapter<String> educational_qualification_adapter = new ArrayAdapter<String>(
				getActivity(), R.layout.spinner_item,
				educational_qualification_Arr);
		educational_qualification_adapter
				.setDropDownViewResource(R.layout.spinner_item1);
		spnr_educational_qualification
				.setAdapter(educational_qualification_adapter);
		educational_qualification_adapter.notifyDataSetChanged();

		String[] other_qualification_Arr = new String[] { "Graduate",
				"Post Graduate" };
		ArrayAdapter<String> other_qualification_adapter = new ArrayAdapter<String>(
				getActivity(), R.layout.spinner_item, other_qualification_Arr);
		other_qualification_adapter
				.setDropDownViewResource(R.layout.spinner_item1);
		spnr_other_qualification.setAdapter(other_qualification_adapter);
		other_qualification_adapter.notifyDataSetChanged();

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

		if (!str_dob.isEmpty()){
            //birth year + 17 years
            int str_year = Integer.parseInt(str_dob.substring(6, 10)) + 17;
            String[] year_List = new String[11];
            for (int i = 0; i <= 10; i++) {
                year_List[i] = str_year + i + "";
            }
            ArrayAdapter<String> year_passing_adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, year_List);
            year_passing_adapter.setDropDownViewResource(R.layout.spinner_item1);
            spnr_year_of_passing_for_basic_qualification.setAdapter(year_passing_adapter);
            year_passing_adapter.notifyDataSetChanged();
        }

		spnr_year_of_passing_for_basic_qualification
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int position, long arg3) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub

					}
				});

		spnr_educational_qualification
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int position, long arg3) {
						tr_other_qualification.setVisibility(View.GONE);



					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub

					}
				});

		spnr_other_qualification
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int position, long arg3) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub

					}
				});

		btn_next.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				str_basic_qualification = edt_basic_qualification.getText()
						.toString();
				str_board_name_for_basic_qualification = edt_board_name_for_basic_qualification
						.getText().toString();
				str_roll_number_for_basic_qualification = edt_roll_number_for_basic_qualification
						.getText().toString();
				str_year_of_passing_for_basic_qualification = spnr_year_of_passing_for_basic_qualification
						.getSelectedItem().toString();

				str_educational_qualification = spnr_educational_qualification
						.getSelectedItem().toString();

				str_other_qualification = spnr_other_qualification
						.getSelectedItem().toString();
				// if
				// (!str_educational_qualification.equalsIgnoreCase("Others")) {
				// str_other_qualification = "NA";
				// } else {
				// str_other_qualification = edt_other_qualification.getText()
				// .toString();
				// }

				if (validation()) {
					getValueFromDatabase_New();
					getInput();

					// M_MainActivity_Data data = new M_MainActivity_Data(
					// quotation_no,
					// LoginActivity.str_pf_number,
					// Fragment_PersonalDetails.str_candidate_corporate_name,
					// Fragment_PersonalDetails.str_father_name,
					// Fragment_PersonalDetails.str_category,
					// Fragment_ContactDetails.str_mobile_no,
					// Fragment_ContactDetails.str_phone_number_1
					// + "-"
					// + Fragment_ContactDetails.str_phone_number_2,
					// Fragment_ContactDetails.str_email_id,
					// str_basic_qualification,
					// str_board_name_for_basic_qualification,
					// str_roll_number_for_basic_qualification,
					// str_year_of_passing_for_basic_qualification,
					// str_educational_qualification,
					// str_other_qualification);

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

					((CIFEnrollmentMainActivity) getActivity()).getViewpager()
							.setCurrentItem(3);
				}

			}
		});

		return rootView;
	}

	private void setCIFInputGui() {
		if (getValueFromDatabase()) {

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
			if (str_year_of_passing_for_basic_qualification != null) {

				spnr_year_of_passing_for_basic_qualification.setSelection(
						getIndex(spnr_year_of_passing_for_basic_qualification,
								str_year_of_passing_for_basic_qualification),
						false);

			} else {
				str_year_of_passing_for_basic_qualification = "";
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
						getActivity(), R.layout.spinner_item,
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
						getActivity(), R.layout.spinner_item,
						other_qualification_Arr);
				other_qualification_adapter
						.setDropDownViewResource(R.layout.spinner_item1);
				spnr_other_qualification
						.setAdapter(other_qualification_adapter);
				other_qualification_adapter.notifyDataSetChanged();
				str_other_qualification = "Graduate";
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

			str_basic_qualification = prsObj.parseXmlTag(input,
					"basic_qualification");
			str_board_name_for_basic_qualification = prsObj.parseXmlTag(input,
					"board_name_for_basic_qualification");

			str_roll_number_for_basic_qualification = prsObj.parseXmlTag(input,
					"roll_number_for_basic_qualification");
			str_year_of_passing_for_basic_qualification = prsObj.parseXmlTag(
					input, "year_of_passing_for_basic_qualification");
			str_educational_qualification = prsObj.parseXmlTag(input,
					"educational_qualification");
			str_other_qualification = prsObj.parseXmlTag(input,
					"other_qualification");

            String str_state = prsObj.parseXmlTag(input, "state");
            String str_city = prsObj.parseXmlTag(input, "city");
            String str_exam_center_location = prsObj.parseXmlTag(input,
                    "exam_center_location");

			str_branch_name = prsObj.parseXmlTag(input, "branch_name");
			str_pan_no = prsObj.parseXmlTag(input, "pan_no");
			str_sex = prsObj.parseXmlTag(input, "sex");
			str_dob = prsObj.parseXmlTag(input, "dob");
			str_dob = str_dob == null ? "" : str_dob;

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
            String str_re_enter_email_id = str_email_id;

			//added by rajan 24-10-2017
			str_aadhar_card_no = prsObj.parseXmlTag(input, "AADHAR_NO");
			str_aadhar_card_no = str_aadhar_card_no == null ? "": str_aadhar_card_no;

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
		} else if (Fragment_ContactDetails.str_mobile_no.trim().equals("")
				|| Fragment_ContactDetails.str_mobile_no.length() < 10
				|| Fragment_ContactDetails.str_phone_number_2.equals("")
				|| Fragment_ContactDetails.str_phone_number_2.length() < 10
				|| Fragment_ContactDetails.str_email_id.trim().equals("")
				|| Fragment_ContactDetails.str_re_enter_email_id.equals("")) {

			Toast.makeText(getActivity(),
					"Please Fill Contact Details Form First", Toast.LENGTH_LONG)
					.show();

			((CIFEnrollmentMainActivity) getActivity()).getViewpager()
					.setCurrentItem(1);
			return false;
		} else if (str_basic_qualification.trim().equals("")) {

			Toast.makeText(getActivity(), "Please Enter Basic Qualification",
					Toast.LENGTH_LONG).show();
			return false;
		} else if (str_board_name_for_basic_qualification.trim().equals("")) {

			Toast.makeText(getActivity(), "Please Enter Board Name",
					Toast.LENGTH_LONG).show();
			return false;
		} else if (str_roll_number_for_basic_qualification.trim().equals("")) {

			Toast.makeText(getActivity(), "Please Enter Roll Number",
					Toast.LENGTH_LONG).show();
			return false;
		} else if (str_roll_number_for_basic_qualification.trim().charAt(0) == '0') {

			Toast.makeText(getActivity(), "Roll shouldn't start with 0",
					Toast.LENGTH_LONG).show();
			return false;
		} else if (str_year_of_passing_for_basic_qualification.trim()
				.equals("")) {

			Toast.makeText(getActivity(), "Please Enter Year of Passing",
					Toast.LENGTH_LONG).show();
			return false;
		} else if (str_educational_qualification.trim().equals("")) {

			Toast.makeText(getActivity(),
					"Please Select Educational Qualification",
					Toast.LENGTH_LONG).show();
			return false;
		} else if (str_other_qualification.trim().equals("")) {

			Toast.makeText(getActivity(), "Please Select Other Qualification",
					Toast.LENGTH_LONG).show();
			return false;
		}

		return true;

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
		inputVal.append("<phone_number>" + /* str_phone_number_1 + "-" */
		str_phone_number_2 + "</phone_number>");
		inputVal.append("<email_id>" + str_email_id + "</email_id>");

		inputVal.append("<basic_qualification>" + str_basic_qualification
				+ "</basic_qualification>");
		inputVal.append("<board_name_for_basic_qualification>"
				+ str_board_name_for_basic_qualification
				+ "</board_name_for_basic_qualification>");
		inputVal.append("<roll_number_for_basic_qualification>"
				+ str_roll_number_for_basic_qualification
				+ "</roll_number_for_basic_qualification>");
		inputVal.append("<year_of_passing_for_basic_qualification>"
				+ str_year_of_passing_for_basic_qualification
				+ "</year_of_passing_for_basic_qualification>");
		inputVal.append("<educational_qualification>"
				+ str_educational_qualification
				+ "</educational_qualification>");
		inputVal.append("<other_qualification>" + str_other_qualification
				+ "</other_qualification>");

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
