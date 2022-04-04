package sbilife.com.pointofsale_bancaagency.poornsuraksha;

class M_BI_PoornSurakshaGrid_Adpter {

    private String policy_year = "", total_base_premium_without_tax = "", death_gurantee = "",
            critical_illness_benefit_gurantee = "";
    //	String benefit_payable_at_death_4_percentage ="";
//	String benefit_payable_at_death_8_percentage ="";
//	String maturity_benefit_non_gurantee_4_percentage ="";
//	String maturity_benefit_non_gurantee_8_percentage ="";
//	String surrender_value_gurantee ="";
//	String surrender_value_ssv_4_percentage ="";
//	String surrender_value_ssv_8_percentage ="";


    public M_BI_PoornSurakshaGrid_Adpter(String policy_year,
                                         String total_base_premium_without_tax, String death_gurantee,
                                         String critical_illness_benefit_gurantee) {
        super();
        this.policy_year = policy_year;
        this.total_base_premium_without_tax = total_base_premium_without_tax;
        this.death_gurantee = death_gurantee;
        this.critical_illness_benefit_gurantee = critical_illness_benefit_gurantee;
    }

    public String getcritical_illness_benefit_gurantee() {
        return critical_illness_benefit_gurantee;
    }

    public void setcritical_illness_benefit_gurantee(String critical_illness_benefit_gurantee) {
        this.critical_illness_benefit_gurantee = critical_illness_benefit_gurantee;
    }

    public String getPolicy_year() {
        return policy_year;
    }

    public void setPolicy_year(String policy_year) {
        this.policy_year = policy_year;
    }

    public String getTotal_base_premium_without_tax() {
        return total_base_premium_without_tax;
    }

    public void setTotal_base_premium_without_tax(
            String total_base_premium_withoout_tax) {
        this.total_base_premium_without_tax = total_base_premium_without_tax;
    }

    public String getDeath_gurantee() {
        return death_gurantee;
    }

    public void setDeath_gurantee(String death_gurantee) {
        this.death_gurantee = death_gurantee;
    }

}
