package sbilife.com.pointofsale_bancaagency.smarthumsafar;

class M_BI_SmartHumsafarGrid_Adpter {


    private String policy_year = "";
    private String total_base_premium_without_tax = "";
    private String benefit_payable_on_first_death = "";
    private String death_gurantee = "";
    private String benefit_payable_at_death_4_percentage = "";
    private String benefit_payable_at_death_8_percentage = "";
    private String maturity_benefit_gurantee = "";
    private String maturity_benefit_non_gurantee_4_percentage = "";
    private String maturity_benefit_non_gurantee_8_percentage = "";
    private String surrender_value_gurantee = "";
    private String surrender_value_ssv_4_percentage = "";
    private String surrender_value_ssv_8_percentage = "";


    public M_BI_SmartHumsafarGrid_Adpter(String policy_year,
                                         String total_base_premium_without_tax, String benefit_payable_on_first_death, String death_gurantee,
                                         String benefit_payable_at_death_4_percentage,
                                         String benefit_payable_at_death_8_percentage,
                                         String maturity_benefit_gurantee,
                                         String maturity_benefit_non_gurantee_4_percentage,
                                         String maturity_benefit_non_gurantee_8_percentage,
                                         String surrender_value_gurantee,
                                         String surrender_value_ssv_4_percentage,
                                         String surrender_value_ssv_8_percentage) {
        super();
        this.policy_year = policy_year;
        this.total_base_premium_without_tax = total_base_premium_without_tax;
        this.benefit_payable_on_first_death = benefit_payable_on_first_death;
        this.death_gurantee = death_gurantee;
        this.benefit_payable_at_death_4_percentage = benefit_payable_at_death_4_percentage;
        this.benefit_payable_at_death_8_percentage = benefit_payable_at_death_8_percentage;
        this.maturity_benefit_gurantee = maturity_benefit_gurantee;
        this.maturity_benefit_non_gurantee_4_percentage = maturity_benefit_non_gurantee_4_percentage;
        this.maturity_benefit_non_gurantee_8_percentage = maturity_benefit_non_gurantee_8_percentage;
        this.surrender_value_gurantee = surrender_value_gurantee;
        this.surrender_value_ssv_4_percentage = surrender_value_ssv_4_percentage;
        this.surrender_value_ssv_8_percentage = surrender_value_ssv_8_percentage;
    }

    public String getBenefit_payable_at_death_4_percentage() {
        return benefit_payable_at_death_4_percentage;
    }

    public void setBenefit_payable_at_death_4_percentage(
            String benefit_payable_at_death_4_percentage) {
        this.benefit_payable_at_death_4_percentage = benefit_payable_at_death_4_percentage;
    }

    public String getBenefit_payable_at_death_8_percentage() {
        return benefit_payable_at_death_8_percentage;
    }

    public void setBenefit_payable_at_death_8_percentage(
            String benefit_payable_at_death_8_percentage) {
        this.benefit_payable_at_death_8_percentage = benefit_payable_at_death_8_percentage;
    }

    public String getMaturity_benefit_gurantee() {
        return maturity_benefit_gurantee;
    }

    public void setMaturity_benefit_gurantee(String maturity_benefit_gurantee) {
        this.maturity_benefit_gurantee = maturity_benefit_gurantee;
    }

    public String getMaturity_benefit_non_gurantee_4_percentage() {
        return maturity_benefit_non_gurantee_4_percentage;
    }

    public void setMaturity_benefit_non_gurantee_4_percentage(
            String maturity_benefit_non_gurantee_4_percentage) {
        this.maturity_benefit_non_gurantee_4_percentage = maturity_benefit_non_gurantee_4_percentage;
    }

    public String getMaturity_benefit_non_gurantee_8_percentage() {
        return maturity_benefit_non_gurantee_8_percentage;
    }

    public void setMaturity_benefit_non_gurantee_8_percentage(
            String maturity_benefit_non_gurantee_8_percentage) {
        this.maturity_benefit_non_gurantee_8_percentage = maturity_benefit_non_gurantee_8_percentage;
    }

    public String getSurrender_value_gurantee() {
        return surrender_value_gurantee;
    }

    public void setSurrender_value_gurantee(String surrender_value_gurantee) {
        this.surrender_value_gurantee = surrender_value_gurantee;
    }

    public String getSurrender_value_ssv_4_percentage() {
        return surrender_value_ssv_4_percentage;
    }

    public void setSurrender_value_ssv_4_percentage(
            String surrender_value_ssv_4_percentage) {
        this.surrender_value_ssv_4_percentage = surrender_value_ssv_4_percentage;
    }

    public String getSurrender_value_ssv_8_percentage() {
        return surrender_value_ssv_8_percentage;
    }

    public void setSurrender_value_ssv_8_percentage(
            String surrender_value_ssv_8_percentage) {
        this.surrender_value_ssv_8_percentage = surrender_value_ssv_8_percentage;
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

    public String getDeath_gurantee() {
        return death_gurantee;
    }

    public void setDeath_gurantee(String death_gurantee) {
        this.death_gurantee = death_gurantee;
    }

    public void setBenefit_payable_on_first_death(
            String benefit_payable_on_first_death) {
        this.benefit_payable_on_first_death = benefit_payable_on_first_death;
    }

    public String getBenefit_payable_on_first_death() {
        return benefit_payable_on_first_death;
    }
}
