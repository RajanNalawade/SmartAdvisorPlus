package sbilife.com.pointofsale_bancaagency;

public class NA_CBI_bean {
	private String unique_ref_no=null;
	private String sr_code=null;
	private String sr_sr_code=null;
	private String sr_type=null;
	private String sr_sr_type=null;
	private String cust_title=null;
	private String cust_first_name=null;
	private String cust_mid_name=null;
	private String cust_last_name=null;
	private String planName=null;
	private String sumassured=null;
	private String premium=null;
	private String cust_email=null;
	private String cust_mobile=null;
	private String sr_email=null;
	private String sr_mobile=null;
	private String na_input=null;
	private String na_output=null;
	private String frequency="";
	private int policyTerm=0;
	private int prem_paying_term=0;
	private String plan_code="";
	private String LA_dob="";
	private String proposer_dob="";
	
	private String bi_inputVal="";
	private String bi_outputVal="";

	public NA_CBI_bean()
	{

	}
	public NA_CBI_bean(String unique_ref_no, String sr_code, String sr_sr_code,
			String sr_type, String sr_sr_type, String cust_title,
			String cust_first_name, String cust_mid_name,
			String cust_last_name, String planName, String sumassured,
			String premium, String cust_email, String cust_mobile,
			String sr_email, String sr_mobile, String na_input,
			String na_output, String frequency, int policyTerm,
			int prem_paying_term, String plan_code, String lA_dob,
			String proposer_dob,String bi_inputVal,String bi_outputVal) {
		super();
		this.unique_ref_no = unique_ref_no;
		this.sr_code = sr_code;
		this.sr_sr_code = sr_sr_code;
		this.sr_type = sr_type;
		this.sr_sr_type = sr_sr_type;
		this.cust_title = cust_title;
		this.cust_first_name = cust_first_name;
		this.cust_mid_name = cust_mid_name;
		this.cust_last_name = cust_last_name;
		this.planName = planName;
		this.sumassured = sumassured;
		this.premium = premium;
		this.cust_email = cust_email;
		this.cust_mobile = cust_mobile;
		this.sr_email = sr_email;
		this.sr_mobile = sr_mobile;
		this.na_input = na_input;
		this.na_output = na_output;
		this.frequency = frequency;
		this.policyTerm = policyTerm;
		this.prem_paying_term = prem_paying_term;
		this.plan_code = plan_code;
		this.LA_dob = lA_dob;
		this.proposer_dob = proposer_dob;
		this.bi_inputVal=bi_inputVal;
		this.bi_outputVal=bi_outputVal;
	}
	public String getFrequency() {
		return frequency;
	}

	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}
	public int getPolicyTerm() {
		return policyTerm;
	}
	public void setPolicyTerm(int policyTerm) {
		this.policyTerm = policyTerm;
	}
	public int getPrem_paying_term() {
		return prem_paying_term;
	}
	public void setPrem_paying_term(int prem_paying_term) {
		this.prem_paying_term = prem_paying_term;
	}
	public String getPlan_code() {
		return plan_code;
	}
	public void setPlan_code(String plan_code) {
		this.plan_code = plan_code;
	}
	public String getLA_dob() {
		return LA_dob;
	}
	public void setLA_dob(String lA_dob) {
		LA_dob = lA_dob;
	}
	public String getProposer_dob() {
		return proposer_dob;
	}
	public void setProposer_dob(String proposer_dob) {
		this.proposer_dob = proposer_dob;
	}
	public String getSr_sr_type() {
		return sr_sr_type;
	}
	public void setSr_sr_type(String sr_sr_type) {
		this.sr_sr_type = sr_sr_type;
	}
	public String getUnique_ref_no() {
		return unique_ref_no;
	}
	public void setUnique_ref_no(String unique_ref_no) {
		this.unique_ref_no = unique_ref_no;
	}
	public String getSr_code() {
		return sr_code;
	}
	public void setSr_code(String sr_code) {
		this.sr_code = sr_code;
	}
	public String getSr_sr_code() {
		return sr_sr_code;
	}
	public void setSr_sr_code(String sr_sr_code) {
		this.sr_sr_code = sr_sr_code;
	}
	public String getSr_type() {
		return sr_type;
	}
	public void setSr_type(String sr_type) {
		this.sr_type = sr_type;
	}
	public String getCust_title() {
		return cust_title;
	}
	public void setCust_title(String cust_title) {
		this.cust_title = cust_title;
	}
	public String getCust_first_name() {
		return cust_first_name;
	}
	public void setCust_first_name(String cust_first_name) {
		this.cust_first_name = cust_first_name;
	}
	public String getCust_mid_name() {
		return cust_mid_name;
	}
	public void setCust_mid_name(String cust_mid_name) {
		this.cust_mid_name = cust_mid_name;
	}
	public String getCust_last_name() {
		return cust_last_name;
	}
	public void setCust_last_name(String cust_last_name) {
		this.cust_last_name = cust_last_name;
	}
	public String getPlanName() {
		return planName;
	}
	public void setPlanName(String planName) {
		this.planName = planName;
	}
	public String getSumassured() {
		return sumassured;
	}
	public void setSumassured(String sumassured) {
		this.sumassured = sumassured;
	}
	public String getPremium() {
		return premium;
	}
	public void setPremium(String premium) {
		this.premium = premium;
	}
	public String getCust_email() {
		return cust_email;
	}
	public void setCust_email(String cust_email) {
		this.cust_email = cust_email;
	}
	public String getCust_mobile() {
		return cust_mobile;
	}
	public void setCust_mobile(String cust_mobile) {
		this.cust_mobile = cust_mobile;
	}
	public String getSr_email() {
		return sr_email;
	}
	public void setSr_email(String sr_email) {
		this.sr_email = sr_email;
	}
	public String getSr_mobile() {
		return sr_mobile;
	}
	public void setSr_mobile(String sr_mobile) {
		this.sr_mobile = sr_mobile;
	}
	public String getNa_input() {
		return na_input;
	}
	public void setNa_input(String na_input) {
		this.na_input = na_input;
	}
	public String getNa_output() {
		return na_output;
	}
	public void setNa_output(String na_output) {
		this.na_output = na_output;
	}
	public String getBi_inputVal() {
		return bi_inputVal;
	}

	public void setBi_inputVal(String bi_inputVal) {
		this.bi_inputVal = bi_inputVal;
	}

	public String getBi_outputVal() {
		return bi_outputVal;
	}

	public void setBi_outputVal(String bi_outputVal) {
		this.bi_outputVal = bi_outputVal;
	}

	
}
