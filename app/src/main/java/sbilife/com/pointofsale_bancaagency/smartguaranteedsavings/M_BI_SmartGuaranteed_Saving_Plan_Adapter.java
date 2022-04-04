package sbilife.com.pointofsale_bancaagency.smartguaranteedsavings;

class M_BI_SmartGuaranteed_Saving_Plan_Adapter {

    private String end_of_year = "";
    private String yearly_basic_premium = "";
    private String cumulative_premium = "";
    private String guaranteed_addition = "";
    private String guaranteed_death_benefit = "";
    private String guaranteed_maturity_benefit = "";
    private String guaranteed_surrender_value = "";


    public M_BI_SmartGuaranteed_Saving_Plan_Adapter(String end_of_year,
                                                    String yearly_basic_premium, String cumulative_premium,
                                                    String guaranteed_addition, String guaranteed_death_benefit,
                                                    String guaranteed_maturity_benefit,
                                                    String guaranteed_surrender_value) {
        super();
        this.end_of_year = end_of_year;
        this.yearly_basic_premium = yearly_basic_premium;
        this.cumulative_premium = cumulative_premium;
        this.guaranteed_addition = guaranteed_addition;
        this.guaranteed_death_benefit = guaranteed_death_benefit;
        this.guaranteed_maturity_benefit = guaranteed_maturity_benefit;
        this.guaranteed_surrender_value = guaranteed_surrender_value;
    }

    public String getEnd_of_year() {
        return end_of_year;
    }

    public void setEnd_of_year(String end_of_year) {
        this.end_of_year = end_of_year;
    }

    public String getYearly_basic_premium() {
        return yearly_basic_premium;
    }

    public void setYearly_basic_premium(String yearly_basic_premium) {
        this.yearly_basic_premium = yearly_basic_premium;
    }

    public String getCumulative_premium() {
        return cumulative_premium;
    }

    public void setCumulative_premium(String cumulative_premium) {
        this.cumulative_premium = cumulative_premium;
    }

    public String getGuaranteed_addition() {
        return guaranteed_addition;
    }

    public void setGuaranteed_addition(String guaranteed_addition) {
        this.guaranteed_addition = guaranteed_addition;
    }

    public String getGuaranteed_death_benefit() {
        return guaranteed_death_benefit;
    }

    public void setGuaranteed_death_benefit(String guaranteed_death_benefit) {
        this.guaranteed_death_benefit = guaranteed_death_benefit;
    }

    public String getGuaranteed_maturity_benefit() {
        return guaranteed_maturity_benefit;
    }

    public void setGuaranteed_maturity_benefit(String guaranteed_maturity_benefit) {
        this.guaranteed_maturity_benefit = guaranteed_maturity_benefit;
    }

    public String getGuaranteed_surrender_value() {
        return guaranteed_surrender_value;
    }

    public void setGuaranteed_surrender_value(String guaranteed_surrender_value) {
        this.guaranteed_surrender_value = guaranteed_surrender_value;
    }


}
