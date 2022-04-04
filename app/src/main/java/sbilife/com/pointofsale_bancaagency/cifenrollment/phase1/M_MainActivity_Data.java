package sbilife.com.pointofsale_bancaagency.cifenrollment.phase1;

public class M_MainActivity_Data {
	private String str_telemarketer_name = "", str_candidate_corporate_name = "",
			str_father_name = "", str_current_house_number = "",
			str_current_street = "", str_current_town = "",
			str_current_pincode = "", str_permanent_house_number = "",
			str_permanent_street = "", str_permanent_town = "",
			str_permanent_pincode = "", str_basic_qualification = "",
			str_board_name_for_basic_qualification = "",
			str_roll_number_for_basic_qualification = "",
			str_other_qualification = "", str_primary_profession = "",
			str_phone_no = "", str_mobile_no = "", str_branch_name = "",
			str_ati_center_accreditation_no = "", str_exam_mode = "",
			str_exam_body_name = "", str_exam_center_location = "",
			str_exam_language = "", str_email_id = "", str_pan = "",
			str_internal_ref_no = "", str_voter_id = "",
			str_driving_license_no = "", str_passport_no = "",
			str_central_govt_id = "", str_nationality = "",
			//added by rajan 24-10-2017
			str_contact_mail_id = "", str_aadhar_no = "";

	public String getStr_aadhar_no() {
		return str_aadhar_no;
	}

	public void setStr_aadhar_no(String str_aadhar_no) {
		this.str_aadhar_no = str_aadhar_no;
	}

	public String getStr_contact_mail_id() {
		return str_contact_mail_id;
	}

	public void setStr_contact_mail_id(String str_contact_mail_id) {
		this.str_contact_mail_id = str_contact_mail_id;
	}

	public String getStr_current_district() {
		return str_current_district;
	}

	public void setStr_current_district(String str_current_district) {
		this.str_current_district = str_current_district;
	}

	public String getStr_permanent_district() {
		return str_permanent_district;
	}

	public void setStr_permanent_district(String str_permanent_district) {
		this.str_permanent_district = str_permanent_district;
	}

	private String str_cor_type = "";
    private String str_insurance_category = "";
    private String str_name_initial = "";
    private String str_category = "";
    private String str_area = "";
    private String str_educational_qualification = "";
    private String str_gender = "";
	private String str_current_address_as_same_as_permanent = "";

	private String str_sponsorship_date = "";
    private String str_year_of_passing = "";
    private String str_date_of_birth = "";

	private String str_current_state_code = "";
    private String str_permanent_state_code = "";
    private String str_current_city_code = "";
    private String str_permanent_city_code = "";
	private String str_photo = "";
	private String str_signature = "";

	private Boolean isSync;
	private Boolean isFlag1;
	private Boolean isFlag2;
	private String createdBy = "";
	private String createdDate = "";
	private String modifiedBy = "";
	private String modifiedDate = "";

	private String str_quotation = "";
	private String str_pf_number = "";
    private String str_current_district = "";
    private String str_permanent_district;
	private String input="";

	public String getInput() {
		return input;
	}

	public void setInput(String input) {
		this.input = input;
	}

	public M_MainActivity_Data(String str_quotation) {
		super();
		// TODO Auto-generated constructor stub
		this.str_quotation = str_quotation;
	}

	public String getStr_pf_number() {
		return str_pf_number;
	}

	public void setStr_pf_number(String str_pf_number) {
		this.str_pf_number = str_pf_number;
	}

	public M_MainActivity_Data(String str_quotation, String str_pf_number) {
		super();

		this.str_quotation = str_quotation;
		this.str_pf_number = str_pf_number;
	}

	public M_MainActivity_Data(String str_quotation,
			String str_pf_number, String input) {
		super();

		this.str_quotation = str_quotation;
		this.str_pf_number = str_pf_number;
		this.input = input;
	}
	
	public M_MainActivity_Data(String str_quotation,
			String str_pf_number, String input,Boolean isFlag1) {
		super();

		this.str_quotation = str_quotation;
		this.str_pf_number = str_pf_number;
		this.input = input;
		this.isFlag1=isFlag1;
	}

	public M_MainActivity_Data(String str_quotation, String str_pf_number,
			String str_candidate_corporate_name, String str_father_name,
			String str_category) {
		super();
		this.str_quotation = str_quotation;
		this.str_pf_number = str_pf_number;
		this.str_candidate_corporate_name = str_candidate_corporate_name;
		this.str_father_name = str_father_name;
		this.str_category = str_category;
	}

	public M_MainActivity_Data(String str_quotation, String str_pf_number,
			String str_candidate_corporate_name, String str_father_name,
			String str_category, String str_mobile_no, String str_phone_no, String str_email_id) {
		super();
		this.str_quotation = str_quotation;
		this.str_pf_number = str_pf_number;
		this.str_candidate_corporate_name = str_candidate_corporate_name;
		this.str_father_name = str_father_name;
		this.str_category = str_category;
		this.str_mobile_no = str_mobile_no;
		this.str_phone_no = str_phone_no;
		this.str_email_id = str_email_id;
	}

	public M_MainActivity_Data(String str_quotation, String str_pf_number,
			String str_candidate_corporate_name, String str_father_name,
			String str_category, String str_mobile_no, String str_phone_no, String str_email_id,
			String str_basic_qualification,
			String str_board_name_for_basic_qualification,
			String str_roll_number_for_basic_qualification,
			String str_year_of_passing, String str_educational_qualification,
			String str_other_qualification) {
		super();
		this.str_quotation = str_quotation;
		this.str_pf_number = str_pf_number;
		this.str_candidate_corporate_name = str_candidate_corporate_name;
		this.str_father_name = str_father_name;
		this.str_category = str_category;
		this.str_mobile_no = str_mobile_no;
		this.str_phone_no = str_phone_no;
		this.str_email_id = str_email_id;
		this.str_basic_qualification = str_basic_qualification;
		this.str_board_name_for_basic_qualification = str_board_name_for_basic_qualification;
		this.str_roll_number_for_basic_qualification = str_roll_number_for_basic_qualification;
		this.str_year_of_passing = str_year_of_passing;
		this.str_educational_qualification = str_educational_qualification;
		this.str_other_qualification = str_other_qualification;
	}

	public M_MainActivity_Data(String str_quotation, String str_pf_number,
			String str_candidate_corporate_name, String str_father_name,
			String str_category, String str_mobile_no, String str_phone_no,
			 String str_email_id, String str_basic_qualification,
			String str_board_name_for_basic_qualification,
			String str_roll_number_for_basic_qualification,
			String str_year_of_passing, String str_educational_qualification,
			String str_other_qualification, String str_insurance_category,
			String str_current_state_code, String str_current_city_code,
			String str_exam_center_location, String str_exam_language) {
		super();
		this.str_quotation = str_quotation;
		this.str_pf_number = str_pf_number;
		this.str_candidate_corporate_name = str_candidate_corporate_name;
		this.str_father_name = str_father_name;
		this.str_category = str_category;
		this.str_mobile_no = str_mobile_no;
		this.str_phone_no = str_phone_no;
		this.str_email_id = str_email_id;
		this.str_basic_qualification = str_basic_qualification;
		this.str_board_name_for_basic_qualification = str_board_name_for_basic_qualification;
		this.str_roll_number_for_basic_qualification = str_roll_number_for_basic_qualification;
		this.str_year_of_passing = str_year_of_passing;
		this.str_educational_qualification = str_educational_qualification;
		this.str_other_qualification = str_other_qualification;
		this.str_insurance_category = str_insurance_category;
		this.str_current_state_code = str_current_state_code;
		this.str_current_city_code = str_current_city_code;
		this.str_exam_center_location = str_exam_center_location;
		this.str_exam_language = str_exam_language;
	}

	public M_MainActivity_Data(String str_quotation, String str_pf_number,
			String str_candidate_corporate_name, String str_father_name,
			String str_category, String str_mobile_no, String str_phone_no,String str_email_id,
			String str_basic_qualification,
			String str_board_name_for_basic_qualification,
			String str_roll_number_for_basic_qualification,
			String str_year_of_passing, String str_educational_qualification,
			String str_other_qualification, String str_insurance_category,
			String str_current_state_code, String str_current_city_code,
			String str_exam_center_location, String str_exam_language,
			String str_photo, String str_signature) {
		super();
		this.str_quotation = str_quotation;
		this.str_pf_number = str_pf_number;
		this.str_candidate_corporate_name = str_candidate_corporate_name;
		this.str_father_name = str_father_name;
		this.str_category = str_category;
		this.str_mobile_no = str_mobile_no;
		this.str_phone_no = str_phone_no;
		this.str_email_id = str_email_id;
		this.str_basic_qualification = str_basic_qualification;
		this.str_board_name_for_basic_qualification = str_board_name_for_basic_qualification;
		this.str_roll_number_for_basic_qualification = str_roll_number_for_basic_qualification;
		this.str_year_of_passing = str_year_of_passing;
		this.str_educational_qualification = str_educational_qualification;
		this.str_other_qualification = str_other_qualification;
		this.str_insurance_category = str_insurance_category;
		this.str_current_state_code = str_current_state_code;
		this.str_current_city_code = str_current_city_code;
		this.str_exam_center_location = str_exam_center_location;
		this.str_exam_language = str_exam_language;
		this.str_photo = str_photo;
		this.str_signature = str_signature;
	}

	public M_MainActivity_Data(String str_quotation, String str_pf_number,
			String str_candidate_corporate_name, String str_father_name,
			String str_category, String str_mobile_no, String str_phone_no,
			String str_email_id, String str_basic_qualification,
			String str_board_name_for_basic_qualification,
			String str_roll_number_for_basic_qualification,
			String str_year_of_passing, String str_educational_qualification,
			String str_other_qualification, String str_insurance_category,
			String str_current_state_code, String str_current_city_code,
			String str_exam_center_location, String str_exam_language,
			String str_photo, String str_signature, Boolean isFlag1,
			String str_date_of_birth, String str_gender, String str_pan,
			String str_branch_name, String str_nationality, String createdDate,
			String str_primary_profession, String str_exam_mode,
			String str_exam_body_name, String str_area,
			String str_current_house_number, String str_current_street,
			String str_current_town, String str_current_pincode,
			String str_current_district, String str_permanent_house_number,
			String str_permanent_street, String str_permanent_town,
			String str_permanent_pincode, String str_permanent_district,
			String str_cor_type) {

		super();
		this.str_quotation = str_quotation;
		this.str_pf_number = str_pf_number;
		this.str_candidate_corporate_name = str_candidate_corporate_name;
		this.str_father_name = str_father_name;
		this.str_category = str_category;
		this.str_mobile_no = str_mobile_no;
		this.str_phone_no = str_phone_no;
		this.str_email_id = str_email_id;
		this.str_basic_qualification = str_basic_qualification;
		this.str_board_name_for_basic_qualification = str_board_name_for_basic_qualification;
		this.str_roll_number_for_basic_qualification = str_roll_number_for_basic_qualification;
		this.str_year_of_passing = str_year_of_passing;
		this.str_educational_qualification = str_educational_qualification;
		this.str_other_qualification = str_other_qualification;
		this.str_insurance_category = str_insurance_category;
		this.str_current_state_code = str_current_state_code;
		this.str_current_city_code = str_current_city_code;
		this.str_exam_center_location = str_exam_center_location;
		this.str_exam_language = str_exam_language;
		this.str_photo = str_photo;
		this.str_signature = str_signature;
		this.isFlag1 = isFlag1;
		this.str_date_of_birth = str_date_of_birth;
		this.str_gender = str_gender;
		this.str_pan = str_pan;
		this.str_branch_name = str_branch_name;
		this.str_nationality = str_nationality;
		this.createdDate = createdDate;
		this.str_primary_profession = str_primary_profession;
		this.str_exam_mode = str_exam_mode;
		this.str_exam_body_name = str_exam_body_name;
		this.str_area = str_area;
		this.str_current_house_number = str_current_house_number;
		this.str_current_street = str_current_street;
		this.str_current_town = str_current_town;
		this.str_current_pincode = str_current_pincode;
		this.str_current_district = str_current_district;
		this.str_permanent_house_number = str_permanent_house_number;
		this.str_permanent_street = str_permanent_street;
		this.str_permanent_town = str_permanent_town;
		this.str_permanent_district = str_permanent_district;
		this.str_permanent_pincode = str_permanent_pincode;
		this.str_cor_type = str_cor_type;

	}

	public M_MainActivity_Data(String str_quotation, String str_pf_number,
			String str_candidate_corporate_name, String str_father_name,
			String str_category, String str_mobile_no, String str_phone_no,
			String str_current_pincode, String str_email_id,
			String str_permanent_pincode, String str_basic_qualification,
			String str_board_name_for_basic_qualification,
			String str_roll_number_for_basic_qualification,
			String str_year_of_passing, String str_educational_qualification,
			String str_other_qualification, String str_insurance_category,
			String str_current_state_code, String str_current_city_code,
			String str_exam_center_location, String str_exam_language,
			String str_photo, String str_signature, Boolean isFlag1,
			String str_date_of_birth, String str_gender, String str_pan,
			String str_branch_name, String str_nationality, String createdDate,
			String str_current_district, String str_permanent_district,
			String str_cor_type) {
		super();
		this.str_quotation = str_quotation;
		this.str_pf_number = str_pf_number;
		this.str_candidate_corporate_name = str_candidate_corporate_name;
		this.str_father_name = str_father_name;
		this.str_category = str_category;
		this.str_mobile_no = str_mobile_no;
		this.str_phone_no = str_phone_no;
		this.str_current_pincode = str_current_pincode;
		this.str_email_id = str_email_id;
		this.str_permanent_pincode = str_permanent_pincode;
		this.str_basic_qualification = str_basic_qualification;
		this.str_board_name_for_basic_qualification = str_board_name_for_basic_qualification;
		this.str_roll_number_for_basic_qualification = str_roll_number_for_basic_qualification;
		this.str_year_of_passing = str_year_of_passing;
		this.str_educational_qualification = str_educational_qualification;
		this.str_other_qualification = str_other_qualification;
		this.str_insurance_category = str_insurance_category;
		this.str_current_state_code = str_current_state_code;
		this.str_current_city_code = str_current_city_code;
		this.str_exam_center_location = str_exam_center_location;
		this.str_exam_language = str_exam_language;
		this.str_photo = str_photo;
		this.str_signature = str_signature;
		this.isFlag1 = isFlag1;
		this.str_date_of_birth = str_date_of_birth;
		this.str_gender = str_gender;
		this.str_pan = str_pan;
		this.str_branch_name = str_branch_name;
		this.str_nationality = str_nationality;
		this.createdDate = createdDate;
		this.str_current_district = str_current_district;
		this.str_permanent_district = str_permanent_district;
		this.str_cor_type = str_cor_type;

	}

	public M_MainActivity_Data(String str_telemarketer_name,
			String str_candidate_corporate_name, String str_father_name,
			String str_current_house_number, String str_current_street,
			String str_current_town, String str_current_pincode,
			String str_permanent_house_number, String str_permanent_street,
			String str_permanent_town, String str_permanent_pincode,
			String str_basic_qualification,
			String str_board_name_for_basic_qualification,
			String str_roll_number_for_basic_qualification,
			String str_other_qualification, String str_primary_profession,
			String str_phone_no, String str_mobile_no, String str_branch_name,
			String str_ati_center_accreditation_no, String str_exam_mode,
			String str_exam_body_name, String str_exam_center_location,
			String str_exam_language, String str_email_id, String str_pan,
			String str_internal_ref_no, String str_voter_id,
			String str_driving_license_no, String str_passport_no,
			String str_central_govt_id, String str_nationality,
			String str_cor_type, String str_insurance_category,
			String str_name_initial, String str_category, String str_area,
			String str_educational_qualification, String str_gender,
			String str_current_address_as_same_as_permanent,
			String str_sponsorship_date, String str_year_of_passing,
			String str_date_of_birth, String str_current_state_code,
			String str_permanent_state_code, String str_current_city_code,
			String str_permanent_city_code, String str_photo,
			String str_signature, String createdBy, String createdDate,
			String modifiedBy, String modifiedDate, Boolean isSync,
			Boolean isFlag1, Boolean isFlag2) {
		super();

		this.str_telemarketer_name = str_telemarketer_name;
		this.str_candidate_corporate_name = str_candidate_corporate_name;
		this.str_father_name = str_father_name;
		this.str_current_house_number = str_current_house_number;
		this.str_current_street = str_current_street;
		this.str_current_town = str_current_town;
		this.str_current_pincode = str_current_pincode;
		this.str_permanent_house_number = str_permanent_house_number;
		this.str_permanent_street = str_permanent_street;
		this.str_permanent_town = str_permanent_town;
		this.str_permanent_pincode = str_permanent_pincode;
		this.str_basic_qualification = str_basic_qualification;
		this.str_board_name_for_basic_qualification = str_board_name_for_basic_qualification;
		this.str_roll_number_for_basic_qualification = str_roll_number_for_basic_qualification;
		this.str_other_qualification = str_other_qualification;
		this.str_primary_profession = str_primary_profession;
		this.str_phone_no = str_phone_no;
		this.str_mobile_no = str_mobile_no;
		this.str_branch_name = str_branch_name;
		this.str_ati_center_accreditation_no = str_ati_center_accreditation_no;
		this.str_exam_mode = str_exam_mode;
		this.str_exam_body_name = str_exam_body_name;
		this.str_exam_center_location = str_exam_center_location;
		this.str_exam_language = str_exam_language;
		this.str_email_id = str_email_id;
		this.str_pan = str_pan;
		this.str_internal_ref_no = str_internal_ref_no;
		this.str_voter_id = str_voter_id;
		this.str_driving_license_no = str_driving_license_no;
		this.str_passport_no = str_passport_no;
		this.str_central_govt_id = str_central_govt_id;
		this.str_nationality = str_nationality;
		this.str_cor_type = str_cor_type;
		this.str_insurance_category = str_insurance_category;
		this.str_name_initial = str_name_initial;
		this.str_category = str_category;
		this.str_area = str_area;
		this.str_educational_qualification = str_educational_qualification;
		this.str_gender = str_gender;
		this.str_current_address_as_same_as_permanent = str_current_address_as_same_as_permanent;
		this.str_sponsorship_date = str_sponsorship_date;
		this.str_year_of_passing = str_year_of_passing;
		this.str_date_of_birth = str_date_of_birth;
		this.str_current_state_code = str_current_state_code;
		this.str_permanent_state_code = str_permanent_state_code;
		this.str_current_city_code = str_current_city_code;
		this.str_permanent_city_code = str_permanent_city_code;
		this.str_photo = str_photo;
		this.str_signature = str_signature;
		this.isSync = isSync;
		this.isFlag1 = isFlag1;
		this.isFlag2 = isFlag2;
		this.createdBy = createdBy;
		this.createdDate = createdDate;
		this.modifiedBy = modifiedBy;
		this.modifiedDate = modifiedDate;

	}

	public String getStr_telemarketer_name() {
		return str_telemarketer_name;
	}

	public void setStr_telemarketer_name(String str_telemarketer_name) {
		this.str_telemarketer_name = str_telemarketer_name;
	}

	public String getStr_candidate_corporate_name() {
		return str_candidate_corporate_name;
	}

	public void setStr_candidate_corporate_name(
			String str_candidate_corporate_name) {
		this.str_candidate_corporate_name = str_candidate_corporate_name;
	}

	public String getStr_father_name() {
		return str_father_name;
	}

	public void setStr_father_name(String str_father_name) {
		this.str_father_name = str_father_name;
	}

	public String getStr_current_house_number() {
		return str_current_house_number;
	}

	public void setStr_current_house_number(String str_current_house_number) {
		this.str_current_house_number = str_current_house_number;
	}

	public String getStr_current_street() {
		return str_current_street;
	}

	public void setStr_current_street(String str_current_street) {
		this.str_current_street = str_current_street;
	}

	public String getStr_current_town() {
		return str_current_town;
	}

	public void setStr_current_town(String str_current_town) {
		this.str_current_town = str_current_town;
	}

	public String getStr_current_pincode() {
		return str_current_pincode;
	}

	public void setStr_current_pincode(String str_current_pincode) {
		this.str_current_pincode = str_current_pincode;
	}

	public String getStr_permanent_house_number() {
		return str_permanent_house_number;
	}

	public void setStr_permanent_house_number(String str_permanent_house_number) {
		this.str_permanent_house_number = str_permanent_house_number;
	}

	public String getStr_permanent_street() {
		return str_permanent_street;
	}

	public void setStr_permanent_street(String str_permanent_street) {
		this.str_permanent_street = str_permanent_street;
	}

	public String getStr_permanent_town() {
		return str_permanent_town;
	}

	public void setStr_permanent_town(String str_permanent_town) {
		this.str_permanent_town = str_permanent_town;
	}

	public String getStr_permanent_pincode() {
		return str_permanent_pincode;
	}

	public void setStr_permanent_pincode(String str_permanent_pincode) {
		this.str_permanent_pincode = str_permanent_pincode;
	}

	public String getStr_basic_qualification() {
		return str_basic_qualification;
	}

	public void setStr_basic_qualification(String str_basic_qualification) {
		this.str_basic_qualification = str_basic_qualification;
	}

	public String getStr_board_name_for_basic_qualification() {
		return str_board_name_for_basic_qualification;
	}

	public void setStr_board_name_for_basic_qualification(
			String str_board_name_for_basic_qualification) {
		this.str_board_name_for_basic_qualification = str_board_name_for_basic_qualification;
	}

	public String getStr_roll_number_for_basic_qualification() {
		return str_roll_number_for_basic_qualification;
	}

	public void setStr_roll_number_for_basic_qualification(
			String str_roll_number_for_basic_qualification) {
		this.str_roll_number_for_basic_qualification = str_roll_number_for_basic_qualification;
	}

	public String getStr_other_qualification() {
		return str_other_qualification;
	}

	public void setStr_other_qualification(String str_other_qualification) {
		this.str_other_qualification = str_other_qualification;
	}

	public String getStr_primary_profession() {
		return str_primary_profession;
	}

	public void setStr_primary_profession(String str_primary_profession) {
		this.str_primary_profession = str_primary_profession;
	}

	public String getStr_phone_no() {
		return str_phone_no;
	}

	public void setStr_phone_no(String str_phone_no) {
		this.str_phone_no = str_phone_no;
	}

	public String getStr_mobile_no() {
		return str_mobile_no;
	}

	public void setStr_mobile_no(String str_mobile_no) {
		this.str_mobile_no = str_mobile_no;
	}

	public String getStr_branch_name() {
		return str_branch_name;
	}

	public void setStr_branch_name(String str_branch_name) {
		this.str_branch_name = str_branch_name;
	}

	public String getStr_ati_center_accreditation_no() {
		return str_ati_center_accreditation_no;
	}

	public void setStr_ati_center_accreditation_no(
			String str_ati_center_accreditation_no) {
		this.str_ati_center_accreditation_no = str_ati_center_accreditation_no;
	}

	public String getStr_exam_mode() {
		return str_exam_mode;
	}

	public void setStr_exam_mode(String str_exam_mode) {
		this.str_exam_mode = str_exam_mode;
	}

	public String getStr_exam_body_name() {
		return str_exam_body_name;
	}

	public void setStr_exam_body_name(String str_exam_body_name) {
		this.str_exam_body_name = str_exam_body_name;
	}

	public String getStr_exam_center_location() {
		return str_exam_center_location;
	}

	public void setStr_exam_center_location(String str_exam_center_location) {
		this.str_exam_center_location = str_exam_center_location;
	}

	public String getStr_exam_language() {
		return str_exam_language;
	}

	public void setStr_exam_language(String str_exam_language) {
		this.str_exam_language = str_exam_language;
	}

	public String getStr_email_id() {
		return str_email_id;
	}

	public void setStr_email_id(String str_email_id) {
		this.str_email_id = str_email_id;
	}

	public String getStr_pan() {
		return str_pan;
	}

	public void setStr_pan(String str_pan) {
		this.str_pan = str_pan;
	}

	public String getStr_internal_ref_no() {
		return str_internal_ref_no;
	}

	public void setStr_internal_ref_no(String str_internal_ref_no) {
		this.str_internal_ref_no = str_internal_ref_no;
	}

	public String getStr_voter_id() {
		return str_voter_id;
	}

	public void setStr_voter_id(String str_voter_id) {
		this.str_voter_id = str_voter_id;
	}

	public String getStr_driving_license_no() {
		return str_driving_license_no;
	}

	public void setStr_driving_license_no(String str_driving_license_no) {
		this.str_driving_license_no = str_driving_license_no;
	}

	public String getStr_passport_no() {
		return str_passport_no;
	}

	public void setStr_passport_no(String str_passport_no) {
		this.str_passport_no = str_passport_no;
	}

	public String getStr_central_govt_id() {
		return str_central_govt_id;
	}

	public void setStr_central_govt_id(String str_central_govt_id) {
		this.str_central_govt_id = str_central_govt_id;
	}

	public String getStr_nationality() {
		return str_nationality;
	}

	public void setStr_nationality(String str_nationality) {
		this.str_nationality = str_nationality;
	}

	public String getStr_cor_type() {
		return str_cor_type;
	}

	public void setStr_cor_type(String str_cor_type) {
		this.str_cor_type = str_cor_type;
	}

	public String getStr_insurance_category() {
		return str_insurance_category;
	}

	public void setStr_insurance_category(String str_insurance_category) {
		this.str_insurance_category = str_insurance_category;
	}

	public String getStr_name_initial() {
		return str_name_initial;
	}

	public void setStr_name_initial(String str_name_initial) {
		this.str_name_initial = str_name_initial;
	}

	public String getStr_category() {
		return str_category;
	}

	public void setStr_category(String str_category) {
		this.str_category = str_category;
	}

	public String getStr_area() {
		return str_area;
	}

	public void setStr_area(String str_area) {
		this.str_area = str_area;
	}

	public String getStr_educational_qualification() {
		return str_educational_qualification;
	}

	public void setStr_educational_qualification(
			String str_educational_qualification) {
		this.str_educational_qualification = str_educational_qualification;
	}

	public String getStr_gender() {
		return str_gender;
	}

	public void setStr_gender(String str_gender) {
		this.str_gender = str_gender;
	}

	public String getStr_current_address_as_same_as_permanent() {
		return str_current_address_as_same_as_permanent;
	}

	public void setStr_current_address_as_same_as_permanent(
			String str_current_address_as_same_as_permanent) {
		this.str_current_address_as_same_as_permanent = str_current_address_as_same_as_permanent;
	}

	public String getStr_sponsorship_date() {
		return str_sponsorship_date;
	}

	public void setStr_sponsorship_date(String str_sponsorship_date) {
		this.str_sponsorship_date = str_sponsorship_date;
	}

	public String getStr_year_of_passing() {
		return str_year_of_passing;
	}

	public void setStr_year_of_passing(String str_year_of_passing) {
		this.str_year_of_passing = str_year_of_passing;
	}

	public String getStr_date_of_birth() {
		return str_date_of_birth;
	}

	public void setStr_date_of_birth(String str_date_of_birth) {
		this.str_date_of_birth = str_date_of_birth;
	}

	public String getStr_current_state_code() {
		return str_current_state_code;
	}

	public void setStr_current_state_code(String str_current_state_code) {
		this.str_current_state_code = str_current_state_code;
	}

	public String getStr_permanent_state_code() {
		return str_permanent_state_code;
	}

	public void setStr_permanent_state_code(String str_permanent_state_code) {
		this.str_permanent_state_code = str_permanent_state_code;
	}

	public String getStr_current_city_code() {
		return str_current_city_code;
	}

	public void setStr_current_city_code(String str_current_city_code) {
		this.str_current_city_code = str_current_city_code;
	}

	public String getStr_permanent_city_code() {
		return str_permanent_city_code;
	}

	public void setStr_permanent_city_code(String str_permanent_city_code) {
		this.str_permanent_city_code = str_permanent_city_code;
	}

	public Boolean getIsSync() {
		return isSync;
	}

	public void setIsSync(Boolean isSync) {
		this.isSync = isSync;
	}

	public Boolean getIsFlag1() {
		return isFlag1;
	}

	public void setIsFlag1(Boolean isFlag1) {
		this.isFlag1 = isFlag1;
	}

	public Boolean getIsFlag2() {
		return isFlag2;
	}

	public void setIsFlag2(Boolean isFlag2) {
		this.isFlag2 = isFlag2;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public String getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(String modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public String getStr_photo() {
		return str_photo;
	}

	public void setStr_photo(String str_photo) {
		this.str_photo = str_photo;
	}

	public String getStr_signature() {
		return str_signature;
	}

	public void setStr_signature(String str_signature) {
		this.str_signature = str_signature;
	}

	public String getStr_quotation() {
		return str_quotation;
	}

	public void setStr_quotation(String str_quotation) {
		this.str_quotation = str_quotation;
	}

}
