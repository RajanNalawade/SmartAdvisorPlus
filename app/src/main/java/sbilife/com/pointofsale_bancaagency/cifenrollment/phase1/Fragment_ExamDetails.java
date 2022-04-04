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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import sbilife.com.pointofsale_bancaagency.DatabaseHelper;
import sbilife.com.pointofsale_bancaagency.ParseXML;
import sbilife.com.pointofsale_bancaagency.R;

public class Fragment_ExamDetails extends Fragment {

    public static String str_state = "";
    public static String str_city = "";
    public static String str_exam_center_location = "";
    public static String str_exam_language = "";
    public static String str_insurance_category = "";
    private static String str_aadhar_card_no = "";
    private EditText edt_state, edt_city;
    private Spinner spnr_insurance_category, spnr_exam_center_location,
            spnr_exam_language;
    private Button btn_back;
    private DatabaseHelper db;

    private TableRow tr_edt_exam_center, tr_spnr_exam_center;
    private View view_edt_exam_center, view_spnr_exam_center;
    private EditText edt_exam_center_location;
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
    private String str_contact_person_eamil_id = "";
    private String str_candidate_corporate_name = "";
    private String str_father_name = "";
    private String str_category = "";
    private String str_mobile_no = "";
    private String str_phone_number_2 = "";
    private String str_email_id = "";
    private String str_basic_qualification = "";
    private String str_board_name_for_basic_qualification = "";
    private String str_roll_number_for_basic_qualification = "";
    private String str_year_of_passing_for_basic_qualification = "";
    private String str_other_qualification = "";
    private String str_educational_qualification = "";
    private String quotation_no = "";
    private String str_exam_center_id = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.cifenrollment_layout_exam_details,
                container, false);

        db = new DatabaseHelper(getActivity());

        edt_state = rootView.findViewById(R.id.edt_state);
        edt_city = rootView.findViewById(R.id.edt_city);
        edt_exam_center_location = rootView
                .findViewById(R.id.edt_exam_center_location);

        spnr_exam_center_location = rootView
                .findViewById(R.id.spnr_exam_center_location);
        spnr_exam_language = rootView
                .findViewById(R.id.spnr_exam_language);
        spnr_insurance_category = rootView
                .findViewById(R.id.spnr_insurance_category);
        Button btn_next = rootView.findViewById(R.id.btn_next);
        // btn_back = (Button) rootView.findViewById(R.id.btn_back);
        TableRow tr_button = rootView.findViewById(R.id.tr_exam_button);
        tr_button.setVisibility(View.VISIBLE);

        tr_edt_exam_center = rootView
                .findViewById(R.id.tr_edt_exam_center);
        tr_spnr_exam_center = rootView
                .findViewById(R.id.tr_spnr_exam_center);
        view_edt_exam_center = rootView
                .findViewById(R.id.view_edt_exam_center);
        view_spnr_exam_center = rootView
                .findViewById(R.id.view_spnr_exam_center);

        TextView tv_exam_center_location = rootView
                .findViewById(R.id.tv_exam_center_location);

        tv_exam_center_location.setText(Html
                .fromHtml("<font color='#000000' size=30dp>Exam Center Location </font>"
                        + "<font color ='#FF0000'><small>*</small></font>"));

        String[] insurance_categoryArr = new String[]{"Life", "Composite"};
        ArrayAdapter<String> insurance_category_adapter = new ArrayAdapter<String>(
                getActivity(), R.layout.spinner_item, insurance_categoryArr);
        insurance_category_adapter
                .setDropDownViewResource(R.layout.spinner_item1);
        spnr_insurance_category.setAdapter(insurance_category_adapter);
        insurance_category_adapter.notifyDataSetChanged();

        String[] exam_languageArr = new String[]{"English", "Hindi",
                "Marathi", "Bengali", "Tamil", "Telegu", "Assamese",
                "Gujarati", "malayalam", "Kannada", "Punjabi", "Urdu", "Oriya"};
        ArrayAdapter<String> exam_language_adapter = new ArrayAdapter<String>(
                getActivity(), R.layout.spinner_item, exam_languageArr);
        exam_language_adapter.setDropDownViewResource(R.layout.spinner_item1);
        spnr_exam_language.setAdapter(exam_language_adapter);
        exam_language_adapter.notifyDataSetChanged();

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
            getValueFromDatabase_for_exam_center();
            setSpinner_ExamDetail();
            // setCIFInputGui();
        } else {
            quotation_no = CIFEnrollmentPFActivity.quotation_Number;
            getValueFromDatabase_for_exam_center();
            setSpinner_ExamDetail();
        }
        setCIFInputGui();
        spnr_insurance_category
                .setOnItemSelectedListener(new OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int arg2, long arg3) {
                        // TODO Auto-generated method stub
                        str_insurance_category = spnr_insurance_category
                                .getSelectedItem().toString();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                        // TODO Auto-generated method stub

                    }
                });

        spnr_exam_center_location
                .setOnItemSelectedListener(new OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int arg2, long arg3) {
                        // TODO Auto-generated method stub

                        str_exam_center_location = spnr_exam_center_location
                                .getSelectedItem().toString();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                        // TODO Auto-generated method stub

                    }
                });

        spnr_exam_language
                .setOnItemSelectedListener(new OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int arg2, long arg3) {
                        // TODO Auto-generated method stub
                        str_exam_language = spnr_exam_language
                                .getSelectedItem().toString();
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
                str_state = edt_state.getText().toString();
                str_city = edt_city.getText().toString();
                if (str_exam_center_id.equalsIgnoreCase("0")) {
                    str_exam_center_location = edt_exam_center_location
                            .getText().toString();
                } else {
                    if (spnr_exam_center_location != null)
                        str_exam_center_location = spnr_exam_center_location.getSelectedItem().toString();
                    else
                        str_exam_center_location = "";
                }

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
                    // Fragment_Qualification.str_basic_qualification,
                    // Fragment_Qualification.str_board_name_for_basic_qualification,
                    // Fragment_Qualification.str_roll_number_for_basic_qualification,
                    // Fragment_Qualification.str_year_of_passing_for_basic_qualification,
                    // Fragment_Qualification.str_educational_qualification,
                    // Fragment_Qualification.str_other_qualification,
                    // str_insurance_category, str_state, str_city,
                    // str_exam_center_location, str_exam_language);

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
                            ((CIFEnrollmentMainActivity) getActivity()).getViewpager()
                                    .setCurrentItem(4, true);
                        }
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }

            }
        });

        // str_state = LoginActivity.str_state;
        // if (str_state != null && !str_state.equalsIgnoreCase("")) {
        // edt_state.setText(str_state);
        // }
        //
        // str_city = LoginActivity.str_city;
        // if (str_city != null && !str_city.equalsIgnoreCase("")) {
        // edt_city.setText(str_city);
        // }

        return rootView;
    }

    private void setSpinner_ExamDetail() {
        String[] exam_center_locationArr;
        if (str_exam_center_id.equalsIgnoreCase("0")) {
            tr_edt_exam_center.setVisibility(View.VISIBLE);
            view_edt_exam_center.setVisibility(View.VISIBLE);
            tr_spnr_exam_center.setVisibility(View.GONE);
            view_spnr_exam_center.setVisibility(View.GONE);
            exam_center_locationArr = new String[]{"Mumbai-Andheri"};
        } else {
            tr_spnr_exam_center.setVisibility(View.VISIBLE);
            view_spnr_exam_center.setVisibility(View.VISIBLE);
            tr_edt_exam_center.setVisibility(View.GONE);
            view_edt_exam_center.setVisibility(View.GONE);

            ParseXML prsObj = new ParseXML();
            // String inputciflist_exam = LoginActivity.inputciflist_exam;

            List<String> list_data = null;
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
            exam_center_locationArr = list_data.toArray(exam_center_locationArr);
        }

        ArrayAdapter<String> exam_center_location_adapter = new ArrayAdapter<String>(
                getActivity(), R.layout.spinner_item, exam_center_locationArr);
        exam_center_location_adapter
                .setDropDownViewResource(R.layout.spinner_item1);
        spnr_exam_center_location.setAdapter(exam_center_location_adapter);
        exam_center_location_adapter.notifyDataSetChanged();
    }

    private void setCIFInputGui() {
        if (getValueFromDatabase()) {

            // Exam
            if (!str_state.equals("")) {
                edt_state.setText(str_state);
            }

            if (!str_city.equals("")) {
                edt_city.setText(str_city);
            }

            if (!str_insurance_category.equals("")) {

                spnr_insurance_category.setSelection(
                        getIndex(spnr_insurance_category,
                                str_insurance_category), false);

            } else {
                String[] insurance_categoryArr = new String[]{"Life",
                        "Composite"};
                ArrayAdapter<String> insurance_category_adapter = new ArrayAdapter<String>(
                        getActivity(), R.layout.spinner_item,
                        insurance_categoryArr);
                insurance_category_adapter
                        .setDropDownViewResource(R.layout.spinner_item1);
                spnr_insurance_category.setAdapter(insurance_category_adapter);
                insurance_category_adapter.notifyDataSetChanged();
            }

            if (!str_exam_center_location.equals("")) {

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
            }

            if (!str_exam_language.equals("")) {

                spnr_exam_language.setSelection(
                        getIndex(spnr_exam_language, str_exam_language), false);

            } else {
                String[] exam_languageArr = new String[]{"English", "Hindi",
                        "Marathi", "Bengali", "Tamil", "Telegu", "Assamese",
                        "Gujarati", "malayalam", "Kannada", "Punjabi", "Urdu", "Oriya"};
                ArrayAdapter<String> exam_language_adapter = new ArrayAdapter<String>(
                        getActivity(), R.layout.spinner_item, exam_languageArr);
                exam_language_adapter
                        .setDropDownViewResource(R.layout.spinner_item1);
                spnr_exam_language.setAdapter(exam_language_adapter);
                exam_language_adapter.notifyDataSetChanged();
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
            str_pf_numbers = str_pf_numbers == null ? "": str_pf_numbers;

            str_insurance_category = prsObj.parseXmlTag(input, "insurance_category");
            str_insurance_category = str_insurance_category == null ? "": str_insurance_category;

            str_state = prsObj.parseXmlTag(input, "state");
            str_state = str_state == null ? "": str_state;

            str_city = prsObj.parseXmlTag(input, "city");
            str_city = str_city == null ? "": str_city;

            str_exam_center_location = prsObj.parseXmlTag(input, "exam_center_location");
            str_exam_center_location = str_exam_center_location == null ? "": str_exam_center_location;

            str_exam_language = prsObj.parseXmlTag(input, "exam_language");
            str_exam_language = str_exam_language == null ? "": str_exam_language;

            str_branch_name = prsObj.parseXmlTag(input, "branch_name");
            str_branch_name = str_branch_name == null ? "": str_branch_name;

            str_pan_no = prsObj.parseXmlTag(input, "pan_no");
            str_pan_no = str_pan_no == null ? "": str_pan_no;

            str_sex = prsObj.parseXmlTag(input, "sex");
            str_sex = str_sex == null ? "": str_sex;

            str_dob = prsObj.parseXmlTag(input, "dob");
            str_dob = str_dob == null ? "": str_dob;

            str_area = prsObj.parseXmlTag(input, "area");
            str_area = str_area == null ? "": str_area;

            str_address1 = prsObj.parseXmlTag(input, "current_house_number");
            str_address1 = str_address1 == null ? "": str_address1;

            str_address2 = prsObj.parseXmlTag(input, "current_street");
            str_address2 = str_address2 == null ? "": str_address2;

            str_pincode = prsObj.parseXmlTag(input, "current_pincode");
            str_pincode = str_pincode == null ? "": str_pincode;

            str_current_district = prsObj.parseXmlTag(input, "current_district");
            str_current_district = str_current_district == null ? "": str_current_district;

            str_permanent_district = prsObj.parseXmlTag(input, "permanent_district");
            str_permanent_district = str_permanent_district == null ? "": str_permanent_district;

            //added by rajan 24-10-2017
            str_aadhar_card_no = prsObj.parseXmlTag(input, "AADHAR_NO");
            str_aadhar_card_no = str_aadhar_card_no == null ? "": str_aadhar_card_no;

            str_contact_person_eamil_id = prsObj.parseXmlTag(input, "CIF_CONTACTPERSON_EMAIL_ID");
            str_contact_person_eamil_id = str_contact_person_eamil_id == null ? "": str_contact_person_eamil_id;

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
                /*String[] array = str_phone_number.split("-");
				str_phone_number_1 = array[0];
				str_phone_number_2 = array[1];*/
            }

            str_email_id = prsObj.parseXmlTag(input, "email_id");
            String str_re_enter_email_id = str_email_id;

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

            //added by rajan 24-10-2017
            str_aadhar_card_no = prsObj.parseXmlTag(input, "AADHAR_NO");
            str_aadhar_card_no = str_aadhar_card_no == null ? "": str_aadhar_card_no;

            str_contact_person_eamil_id = prsObj
                    .parseXmlTag(input, "CIF_CONTACTPERSON_EMAIL_ID");

            flag = true;
        }
        return flag;
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

            ((CIFEnrollmentMainActivity) getActivity()).getViewpager().setCurrentItem(0);
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

            Toast.makeText(getActivity(),
                    "Please Fill Qualification Details Form First",
                    Toast.LENGTH_LONG).show();

            ((CIFEnrollmentMainActivity) getActivity()).getViewpager().setCurrentItem(2);
            return false;
        } else if (str_insurance_category.trim().equals("")) {

            Toast.makeText(getActivity(), "Please Select Insurance Category",
                    Toast.LENGTH_LONG).show();
            return false;
        } else if (str_state.trim().equals("")) {

            Toast.makeText(getActivity(), "Please Enter State",
                    Toast.LENGTH_LONG).show();
            return false;
        } else if (str_city.trim().equals("")) {

            Toast.makeText(getActivity(), "Please Enter City",
                    Toast.LENGTH_LONG).show();
            return false;
        } else if (str_exam_center_location.trim().equals("")) {

            Toast.makeText(getActivity(), "Please Select Exam Center Location",
                    Toast.LENGTH_LONG).show();
            return false;
        } else if (str_exam_language.trim().equals("")) {

            Toast.makeText(getActivity(), "Please Select Exam Language",
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
        inputVal.append("<phone_number>" /*+ str_phone_number_1 + "-"*/
                + str_phone_number_2 + "</phone_number>");
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

        inputVal.append("<insurance_category>" + str_insurance_category
                + "</insurance_category>");
        inputVal.append("<state>" + str_state + "</state>");
        inputVal.append("<city>" + str_city + "</city>");
        inputVal.append("<exam_center_location>" + str_exam_center_location
                + "</exam_center_location>");
        inputVal.append("<exam_language>" + str_exam_language
                + "</exam_language>");

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
        System.out.println("INputVal:" + inputVal);
    }

}
