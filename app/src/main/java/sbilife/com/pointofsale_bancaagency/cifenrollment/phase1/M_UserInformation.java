package sbilife.com.pointofsale_bancaagency.cifenrollment.phase1;

public class M_UserInformation {

	private String str_candidate_name = "", str_pan = "", str_date_of_birth = "", str_created_date = "",
			str_mobile_no = "",str_quotation = "",str_email_id = "", str_contact_person_email_id = "",
			str_pf_number = "", str_aadhar_card_no = "", str_status="";

	private Boolean isFlag1;

	public String getStr_aadhar_card_no() {
		return str_aadhar_card_no;
	}

	public void setStr_aadhar_card_no(String str_aadhar_card_no) {
		this.str_aadhar_card_no = str_aadhar_card_no;
	}

	public String getStr_contact_person_email_id() {
		return str_contact_person_email_id;
	}

	public void setStr_contact_person_email_id(String str_contact_person_email_id) {
		this.str_contact_person_email_id = str_contact_person_email_id;
	}

	public String getStr_status() {
		return str_status;
	}

	public void setStr_status(String str_status) {
		this.str_status = str_status;
	}

	public M_UserInformation(String str_status) {
		super();
		this.str_status = str_status;
	}

	public M_UserInformation(String str_quotation, String str_pf_number) {
		super();
		this.str_quotation = str_quotation;
		this.str_pf_number = str_pf_number;
	}

	public M_UserInformation(String str_quotation, String str_pf_number,
			String str_candidate_name) {
		super();
		this.str_quotation = str_quotation;
		this.str_pf_number = str_pf_number;
		this.str_candidate_name = str_candidate_name;
	}

	public M_UserInformation(String str_quotation, String str_pf_number,
			String str_candidate_name, String str_mobile_no, String str_email_id) {
		super();
		this.str_quotation = str_quotation;
		this.str_pf_number = str_pf_number;
		this.str_candidate_name = str_candidate_name;
		this.str_mobile_no = str_mobile_no;
		this.str_email_id = str_email_id;
	}

	public M_UserInformation(String str_quotation, String str_pf_number,
			String str_candidate_name, String str_mobile_no,
			String str_email_id, String str_created_date) {
		super();
		this.str_quotation = str_quotation;
		this.str_pf_number = str_pf_number;
		this.str_candidate_name = str_candidate_name;
		this.str_mobile_no = str_mobile_no;
		this.str_email_id = str_email_id;
		this.str_created_date = str_created_date;

	}

	public M_UserInformation(String str_quotation, String str_pf_number,
			String str_candidate_name, String str_mobile_no,
			String str_email_id, String str_created_date, Boolean isFlag1) {
		super();
		this.str_quotation = str_quotation;
		this.str_pf_number = str_pf_number;
		this.str_candidate_name = str_candidate_name;
		this.str_mobile_no = str_mobile_no;
		this.str_email_id = str_email_id;
		this.str_created_date = str_created_date;
		this.isFlag1 = isFlag1;

	}
	
	public M_UserInformation(String str_quotation, String str_pf_number,
			String str_candidate_name, String str_status,String str_mobile_no,
			String str_email_id, String str_created_date, Boolean isFlag1) {
		super();
		this.str_quotation = str_quotation;
		this.str_pf_number = str_pf_number;
		this.str_candidate_name = str_candidate_name;
		this.str_status=str_status;
		this.str_mobile_no = str_mobile_no;
		this.str_email_id = str_email_id;
		this.str_created_date = str_created_date;
		this.isFlag1 = isFlag1;

	}

	public String getStr_candidate_name() {
		return str_candidate_name;
	}

	public void setStr_candidate_name(String str_candidate_name) {
		this.str_candidate_name = str_candidate_name;
	}

	public String getStr_pan() {
		return str_pan;
	}

	public void setStr_pan(String str_pan) {
		this.str_pan = str_pan;
	}

	public String getStr_date_of_birth() {
		return str_date_of_birth;
	}

	public void setStr_date_of_birth(String str_date_of_birth) {
		this.str_date_of_birth = str_date_of_birth;
	}

	public String getStr_created_date() {
		return str_created_date;
	}

	public void setStr_created_date(String str_created_date) {
		this.str_created_date = str_created_date;
	}

	public String getStr_mobile_no() {
		return str_mobile_no;
	}

	public void setStr_mobile_no(String str_mobile_no) {
		this.str_mobile_no = str_mobile_no;
	}

	public String getStr_quotation() {
		return str_quotation;
	}

	public void setStr_quotation(String str_quotation) {
		this.str_quotation = str_quotation;
	}

	public String getStr_email_id() {
		return str_email_id;
	}

	public void setStr_email_id(String str_email_id) {
		this.str_email_id = str_email_id;
	}

	public String getStr_pf_number() {
		return str_pf_number;
	}

	public void setStr_pf_number(String str_pf_number) {
		this.str_pf_number = str_pf_number;
	}

	public Boolean getIsFlag1() {
		return isFlag1;
	}

	public void setIsFlag1(Boolean isFlag1) {
		this.isFlag1 = isFlag1;
	}
}
