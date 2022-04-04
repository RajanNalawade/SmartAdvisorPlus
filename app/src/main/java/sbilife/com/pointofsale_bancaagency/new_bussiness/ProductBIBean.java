package sbilife.com.pointofsale_bancaagency.new_bussiness;

public class ProductBIBean {
    private String ProductBIID = null;
    private String QuotationNo = null;
    private String PlanSelected = null;
    private String ProposalDate = null;
    private String MobileNo = null;
    private String CreatedDate = null;
    private String CreatedBy = null;
    private String email = null;
    private String syncStatus = null;
    String uinNo = null;

    private String sr_code = null;
    private String sr_sr_code = null;
    private String sr_type = null;
    private String sr_sr_type = null;
    private String cust_title = null;
    String cust_first_name = null;
    String cust_mid_name = null;

    String cust_last_name = null;

    private String bi_inputVal = "";
    private String bi_outputVal = "";


    @Override
    public String toString() {
        return "ProductBIBean{" +
                "ProductBIID='" + ProductBIID + '\'' +
                ", QuotationNo='" + QuotationNo + '\'' +
                ", PlanSelected='" + PlanSelected + '\'' +
                ", ProposalDate='" + ProposalDate + '\'' +
                ", MobileNo='" + MobileNo + '\'' +
                ", CreatedDate='" + CreatedDate + '\'' +
                ", CreatedBy='" + CreatedBy + '\'' +
                ", email='" + email + '\'' +
                ", syncStatus='" + syncStatus + '\'' +
                ", uinNo='" + uinNo + '\'' +
                ", sr_code='" + sr_code + '\'' +
                ", sr_sr_code='" + sr_sr_code + '\'' +
                ", sr_type='" + sr_type + '\'' +
                ", sr_sr_type='" + sr_sr_type + '\'' +
                ", cust_title='" + cust_title + '\'' +
                ", cust_first_name='" + cust_first_name + '\'' +
                ", cust_mid_name='" + cust_mid_name + '\'' +
                ", cust_last_name='" + cust_last_name + '\'' +
                ", sumassured='" + sumassured + '\'' +
                ", premium='" + premium + '\'' +
                ", sr_email='" + sr_email + '\'' +
                ", sr_mobile='" + sr_mobile + '\'' +
                ", na_input='" + na_input + '\'' +
                ", na_output='" + na_output + '\'' +
                ", frequency='" + frequency + '\'' +
                ", policyTerm=" + policyTerm +
                ", prem_paying_term=" + prem_paying_term +
                ", plan_code='" + plan_code + '\'' +
                ", LA_dob='" + LA_dob + '\'' +
                ", proposer_dob='" + proposer_dob + '\'' +
                ", na_group='" + na_group + '\'' +
                ", transactionMode='" + transactionMode + '\'' +
                '}';
    }

    private String sumassured = null;
    private String premium = null;
    private String sr_email = null;
    private String sr_mobile = null;
    private String na_input = null;
    private String na_output = null;
    private String frequency = "";
    private int policyTerm = 0;
    private int prem_paying_term = 0;
    private String plan_code = "";
    private String LA_dob = "";
    private String proposer_dob = "";

    /* Group changes */
    private String na_group = "";
    private String transactionMode = "";

	/*public ProductBIBean(String productBIID, String quotationNo,
			String cust_title, String cust_first_name, String cust_mid_name,
			String cust_last_name, String planSelected, String proposalDate,
			String mobileNo, String createdDate, String createdBy,
			String email, String syncStatus, String uinNo) {
		super();
		ProductBIID = productBIID;
		QuotationNo = quotationNo;
		this.cust_title = cust_title;
		this.cust_first_name = cust_first_name;
		this.cust_mid_name = cust_mid_name;
		this.cust_last_name = cust_last_name;
		PlanSelected = planSelected;
		ProposalDate = proposalDate;
		MobileNo = mobileNo;
		CreatedDate = createdDate;
		CreatedBy = createdBy;
		this.email = email;
		this.syncStatus = syncStatus;
		this.uinNo = uinNo;
		//this.transactionMode = applicationMode;
	}*/

    public ProductBIBean(String productBIID, String quotationNo,
                         String planSelected, String proposalDate, String mobileNo,
                         String createdDate, String createdBy, String email,
                         String syncStatus, String uinNo, String sr_code, String sr_sr_code,
                         String sr_type, String sr_sr_type, String cust_title,
                         String cust_first_name, String cust_mid_name,
                         String cust_last_name, String sumassured, String premium,
                         String sr_email, String sr_mobile, String na_input,
                         String na_output, String frequency, int policyTerm,
                         int prem_paying_term, String plan_code, String lA_dob,
                         String proposer_dob, String na_group,
                         String transactionMode, String bi_inputVal, String bi_outputVal) {
        super();
        ProductBIID = productBIID;
        QuotationNo = quotationNo;
        PlanSelected = planSelected;
        ProposalDate = proposalDate;
        MobileNo = mobileNo;
        CreatedDate = createdDate;
        CreatedBy = createdBy;
        this.email = email;
        this.syncStatus = syncStatus;
        this.uinNo = uinNo;
        this.sr_code = sr_code;
        this.sr_sr_code = sr_sr_code;
        this.sr_type = sr_type;
        this.sr_sr_type = sr_sr_type;
        this.cust_title = cust_title;
        this.cust_first_name = cust_first_name;
        this.cust_mid_name = cust_mid_name;
        this.cust_last_name = cust_last_name;
        this.sumassured = sumassured;
        this.premium = premium;
        this.sr_email = sr_email;
        this.sr_mobile = sr_mobile;
        this.na_input = na_input;
        this.na_output = na_output;
        this.frequency = frequency;
        this.policyTerm = policyTerm;
        this.prem_paying_term = prem_paying_term;
        this.plan_code = plan_code;
        LA_dob = lA_dob;
        this.proposer_dob = proposer_dob;
        this.na_group = na_group;
        this.transactionMode = transactionMode;
        this.bi_inputVal = bi_inputVal;
        this.bi_outputVal = bi_outputVal;
    }

    public ProductBIBean(String uinNo, String na_group, String na_input,
                         String na_output) {
        super();
        this.uinNo = uinNo;
        this.na_group = na_group;
        this.na_input = na_input;
        this.na_output = na_output;
    }

    public String getNa_group() {
        return na_group;
    }


    public String getSyncStatus() {
        return syncStatus;
    }


    public String getUinNo() {
        return uinNo;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProductBIID() {
        return ProductBIID;
    }


    public String getQuotationNo() {
        return QuotationNo;
    }


    public String getPlanSelected() {
        return PlanSelected;
    }


    public String getProposalDate() {
        return ProposalDate;
    }


    public String getMobileNo() {
        return MobileNo;
    }

    public void setMobileNo(String mobileNo) {
        MobileNo = mobileNo;
    }

    public String getCreatedDate() {
        return CreatedDate;
    }

    public void setCreatedDate(String createdDate) {
        CreatedDate = createdDate;
    }

    public String getCreatedBy() {
        return CreatedBy;
    }

    public void setCreatedBy(String createdBy) {
        CreatedBy = createdBy;
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


    public String getSr_type() {
        return sr_type;
    }


    public String getSr_sr_type() {
        return sr_sr_type;
    }


    public String getCust_title() {
        return cust_title;
    }


    public String getCust_first_name() {
        return cust_first_name;
    }


    public String getCust_mid_name() {
        return cust_mid_name;
    }


    public String getCust_last_name() {
        return cust_last_name;
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

    public String getSr_email() {
        return sr_email;
    }


    public String getSr_mobile() {
        return sr_mobile;
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


    public String getPlan_code() {
        return plan_code;
    }


    public String getLA_dob() {
        return LA_dob;
    }


    public String getProposer_dob() {
        return proposer_dob;
    }

    public void setProposer_dob(String proposer_dob) {
        this.proposer_dob = proposer_dob;
    }

    public String getTransactionMode() {
        return transactionMode;
    }

    public String getBi_inputVal() {
        return bi_inputVal;
    }

    public String getBi_outputVal() {
        return bi_outputVal;
    }
}
