package sbilife.com.pointofsale_bancaagency.smartscholar;

public class M_BI_SmartScholarGrid_AdapterCommon {
    String policy_year = "";
    String premium = "";
    String mortality_charge1 = "";
    String total_charge1 = "";
    String service_tax_on_mortality_charge1 = "";
    String fund_value_at_end1 = "";
    String surrender_value1 = "";
    String death_benefit1 = "";
    String mortality_charge2 = "";
    String total_charge2 = "";
    String service_tax_on_mortality_charge2 = "";
    String fund_value_at_end2 = "";
    String surrender_value2 = "";
    String death_benefit2 = "";
    String commission = "";

	/*String premium_allocation_charge= "";
	String amount_for_investment = "";
	String policy_administration_charge= "";
	String str_AddToTheFund4Pr = "";
	String fund_before_fmc1 = "";
	String fund_management_charge1 ="";
	String guranteed_addition1 = "";*/


    public M_BI_SmartScholarGrid_AdapterCommon(String policy_year, String premium, String mortality_charge1,
                                               String total_charge1, String service_tax_on_mortality_charge1, String fund_value_at_end1,
                                               String surrender_value1, String death_benefit1, String mortality_charge2, String total_charge2,
                                               String service_tax_on_mortality_charge2, String fund_value_at_end2,
                                               String surrender_value2, String death_benefit2, String commission) {
        super();
        this.policy_year = policy_year;
        this.premium = premium;
        this.mortality_charge1 = mortality_charge1;
        this.total_charge1 = total_charge1;
        this.service_tax_on_mortality_charge1 = service_tax_on_mortality_charge1;
        this.fund_value_at_end1 = fund_value_at_end1;
        this.surrender_value1 = surrender_value1;
        this.death_benefit1 = death_benefit1;
        this.mortality_charge2 = mortality_charge2;
        this.total_charge2 = total_charge2;
        this.service_tax_on_mortality_charge2 = service_tax_on_mortality_charge2;
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


    public String getService_tax_on_mortality_charge1() {
        return service_tax_on_mortality_charge1;
    }


    public void setService_tax_on_mortality_charge1(
            String service_tax_on_mortality_charge1) {
        this.service_tax_on_mortality_charge1 = service_tax_on_mortality_charge1;
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

    public String getService_tax_on_mortality_charge2() {
        return service_tax_on_mortality_charge2;
    }

    public void setService_tax_on_mortality_charge2(String service_tax_on_mortality_charge2) {
        this.service_tax_on_mortality_charge2 = service_tax_on_mortality_charge2;
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


}
