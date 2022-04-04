package sbilife.com.pointofsale_bancaagency.smartelite;

public class M_BI_EliteGrid_AdapterCommon2 {
    String policy_year = "";
    String premium = "";
    String premium_allocation_charge = "";
    String annulized_premium_allocation_charge = "";
    String mortality_charge1 = "";
    String service_tax_on_mortality_charge1 = "";
    String policy_administration_charge = "";
    String ATPDChrg = "";
    String total_charge1 = "";
    String str_AddToTheFund4Pr = "";
    String fund_before_fmc1 = "";
    String fund_management_charge1 = "";
    String fund_value_at_end1 = "";
    String surrender_value1 = "";
    String death_benefit1 = "";

    public M_BI_EliteGrid_AdapterCommon2(String policy_year, String premium, String premium_allocation_charge, String annulized_premium_allocation_charge,
                                         String mortality_charge1, String service_tax_on_mortality_charge1, String policy_administration_charge,
                                         String ATPDChrg, String total_charge1,
                                         String str_AddToTheFund4Pr, String fund_before_fmc1, String fund_management_charge1, String fund_value_at_end1,
                                         String surrender_value1, String death_benefit1) {
        super();
        this.policy_year = policy_year;
        this.premium = premium;
        this.premium_allocation_charge = premium_allocation_charge;
        this.annulized_premium_allocation_charge = annulized_premium_allocation_charge;
        this.mortality_charge1 = mortality_charge1;
        this.service_tax_on_mortality_charge1 = service_tax_on_mortality_charge1;
        this.policy_administration_charge = policy_administration_charge;
        this.ATPDChrg = ATPDChrg;
        this.total_charge1 = total_charge1;
        this.str_AddToTheFund4Pr = str_AddToTheFund4Pr;
        this.fund_before_fmc1 = fund_before_fmc1;
        this.fund_management_charge1 = fund_management_charge1;
        this.fund_value_at_end1 = fund_value_at_end1;
        this.surrender_value1 = surrender_value1;
        this.death_benefit1 = death_benefit1;

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

    public String getPolicy_administration_charge() {
        return policy_administration_charge;
    }

    public String getATPDChrg() {
        return ATPDChrg;
    }

    public void setATPDChrg(String ATPDChrg) {
        this.ATPDChrg = ATPDChrg;
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


    public String getService_tax_on_mortality_charge1() {
        return service_tax_on_mortality_charge1;
    }


    public void setService_tax_on_mortality_charge1(
            String service_tax_on_mortality_charge1) {
        this.service_tax_on_mortality_charge1 = service_tax_on_mortality_charge1;
    }

    public String getStr_AddToTheFund4Pr() {
        return str_AddToTheFund4Pr;
    }

    public void setStr_AddToTheFund4Pr(String str_AddToTheFund4Pr) {
        this.str_AddToTheFund4Pr = str_AddToTheFund4Pr;
    }

    public String getFund_before_fmc1() {
        return fund_before_fmc1;
    }


    public void setFund_before_fmc1(String fund_before_fmc1) {
        this.fund_before_fmc1 = fund_before_fmc1;
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

    public String getAnnulized_premium_allocation_charge() {
        return annulized_premium_allocation_charge;
    }

    public void setAnnulized_premium_allocation_charge(String annulized_premium_allocation_charge) {
        this.annulized_premium_allocation_charge = annulized_premium_allocation_charge;
    }
}
