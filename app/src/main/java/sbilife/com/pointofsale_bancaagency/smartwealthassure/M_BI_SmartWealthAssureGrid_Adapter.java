package sbilife.com.pointofsale_bancaagency.smartwealthassure;

class M_BI_SmartWealthAssureGrid_Adapter {


	private String policy_year="";
	private String premium= "";
	private String premium_allocation_charge= "";
	private String amount_available_for_investment= "";
	private String option_charge;
	private String policy_administration_charge= "";
	private String mortality_charge1= "";
	private String total_charge1= "";
	private String total_service_tax1= "";
	private String addition_to_fund1= "";
	private String fund_management_charge1= "";
	private String fund_value_at_end1= "";
	private String surrender_value1= "";
	private String death_benefit1= "";
	private String mortality_charge2= "";
	private String total_charge2= "";
	private String total_service_tax2= "";
	private String addition_to_fund2= "";
	private String fund_management_charge2= "";
	private String fund_value_at_end2= "";
	private String surrender_value2= "";
	private String death_benefit2= "";
	private String commission= "";
	
	
	
	
	public M_BI_SmartWealthAssureGrid_Adapter(String policy_year, String premium,
			String premium_allocation_charge,
			String amount_available_for_investment,String option_charge,
			String policy_administration_charge, String mortality_charge1,
			String total_charge1, String total_service_tax1,
			String addition_to_fund1, String fund_management_charge1,
            String fund_value_at_end1,
			String surrender_value1, String death_benefit1,
			String mortality_charge2, String total_charge2,
			String total_service_tax2, String addition_to_fund2,
			String fund_management_charge2,
			String fund_value_at_end2, String surrender_value2,
			String death_benefit2, String commission) {
		super();
		this.policy_year = policy_year;
		this.premium = premium;
		this.premium_allocation_charge = premium_allocation_charge;
		this.amount_available_for_investment = amount_available_for_investment;
		this.option_charge=option_charge;
		this.policy_administration_charge = policy_administration_charge;
		this.mortality_charge1 = mortality_charge1;
		this.total_charge1 = total_charge1;
		this.total_service_tax1 = total_service_tax1;
		this.addition_to_fund1 = addition_to_fund1;
		this.fund_management_charge1 = fund_management_charge1;
		this.fund_value_at_end1 = fund_value_at_end1;
		this.surrender_value1 = surrender_value1;
		this.death_benefit1 = death_benefit1;
		this.mortality_charge2 = mortality_charge2;
		this.total_charge2 = total_charge2;
		this.total_service_tax2 = total_service_tax2;
		this.addition_to_fund2 = addition_to_fund2;
		this.fund_management_charge2 = fund_management_charge2;
		this.fund_value_at_end2 = fund_value_at_end2;
		this.surrender_value2 = surrender_value2;
		this.death_benefit2 = death_benefit2;
		this.commission = commission;
	}
	public String getPolicy_year() {
		return policy_year;
	}
	public void setPolicy_year(String policy_year) {
		this.policy_year = policy_year;
	}
	public String getPremium() {
		return premium;
	}
	public void setPremium(String premium) {
		this.premium = premium;
	}
	public String getPremium_allocation_charge() {
		return premium_allocation_charge;
	}
	public void setPremium_allocation_charge(String premium_allocation_charge) {
		this.premium_allocation_charge = premium_allocation_charge;
	}
	public String getAmount_available_for_investment() {
		return amount_available_for_investment;
	}
	public void setAmount_available_for_investment(
			String amount_available_for_investment) {
		this.amount_available_for_investment = amount_available_for_investment;
	}
	public String getPolicy_administration_charge() {
		return policy_administration_charge;
	}
	public void setPolicy_administration_charge(String policy_administration_charge) {
		this.policy_administration_charge = policy_administration_charge;
	}
	public String getMortality_charge1() {
		return mortality_charge1;
	}
	public void setMortality_charge1(String mortality_charge1) {
		this.mortality_charge1 = mortality_charge1;
	}
	public String getTotal_charge1() {
		return total_charge1;
	}
	public void setTotal_charge1(String total_charge1) {
		this.total_charge1 = total_charge1;
	}
	public String getTotal_service_tax1() {
		return total_service_tax1;
	}
	public void setTotal_service_tax1(String total_service_tax1) {
		this.total_service_tax1 = total_service_tax1;
	}
	public String getAddition_to_fund1() {
		return addition_to_fund1;
	}
	public void setAddition_to_fund1(String addition_to_fund1) {
		this.addition_to_fund1 = addition_to_fund1;
	}
	public String getFund_management_charge1() {
		return fund_management_charge1;
	}
	public void setFund_management_charge1(String fund_management_charge1) {
		this.fund_management_charge1 = fund_management_charge1;
	}

	public String getFund_value_at_end1() {
		return fund_value_at_end1;
	}
	public void setFund_value_at_end1(String fund_value_at_end1) {
		this.fund_value_at_end1 = fund_value_at_end1;
	}
	public String getSurrender_value1() {
		return surrender_value1;
	}
	public void setSurrender_value1(String surrender_value1) {
		this.surrender_value1 = surrender_value1;
	}
	public String getDeath_benefit1() {
		return death_benefit1;
	}
	public void setDeath_benefit1(String death_benefit1) {
		this.death_benefit1 = death_benefit1;
	}
	public String getMortality_charge2() {
		return mortality_charge2;
	}
	public void setMortality_charge2(String mortality_charge2) {
		this.mortality_charge2 = mortality_charge2;
	}
	public String getTotal_charge2() {
		return total_charge2;
	}
	public void setTotal_charge2(String total_charge2) {
		this.total_charge2 = total_charge2;
	}
	public String getTotal_service_tax2() {
		return total_service_tax2;
	}
	public void setTotal_service_tax2(String total_service_tax2) {
		this.total_service_tax2 = total_service_tax2;
	}
	public String getAddition_to_fund2() {
		return addition_to_fund2;
	}
	public void setAddition_to_fund2(String addition_to_fund2) {
		this.addition_to_fund2 = addition_to_fund2;
	}
	public String getFund_management_charge2() {
		return fund_management_charge2;
	}
	public void setFund_management_charge2(String fund_management_charge2) {
		this.fund_management_charge2 = fund_management_charge2;
	}
	public String getFund_value_at_end2() {
		return fund_value_at_end2;
	}
	public void setFund_value_at_end2(String fund_value_at_end2) {
		this.fund_value_at_end2 = fund_value_at_end2;
	}
	public String getSurrender_value2() {
		return surrender_value2;
	}
	public void setSurrender_value2(String surrender_value2) {
		this.surrender_value2 = surrender_value2;
	}
	public String getDeath_benefit2() {
		return death_benefit2;
	}
	public void setDeath_benefit2(String death_benefit2) {
		this.death_benefit2 = death_benefit2;
	}
	public String getCommission() {
		return commission;
	}
	public void setCommission(String commission) {
		this.commission = commission;
	}
	public String getOption_charge() {
		return option_charge;
	}
	public void setOption_charge(String option_charge) {
		this.option_charge = option_charge;
	}
	


}
