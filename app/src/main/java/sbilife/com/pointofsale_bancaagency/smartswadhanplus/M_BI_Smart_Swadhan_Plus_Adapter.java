package sbilife.com.pointofsale_bancaagency.smartswadhanplus;

public class M_BI_Smart_Swadhan_Plus_Adapter {

    public String end_of_year = "";
    public String yearly_basic_premium = "";
    //	private String cumulative_premium= "";
    public String SurvivalBenefits = "";
    public String OtherBenefitsifAny = "";
    public String guaranteed_death_benefit = "";
    public String guaranteed_maturity_benefit = "";
    public String guaranteed_surrender_value = "";
    public String nonGuaranSurrenderValue = "";

    public M_BI_Smart_Swadhan_Plus_Adapter(String end_of_year,
                                           String yearly_basic_premium, String SurvivalBenefits, String OtherBenefitsifAny,
                                           String guaranteed_maturity_benefit, String guaranteed_death_benefit, String guaranteed_surrender_value,
                                           String nonGuaranSurrenderValue) {
        super();
        this.end_of_year = end_of_year;
        this.yearly_basic_premium = yearly_basic_premium;
        this.SurvivalBenefits = SurvivalBenefits;
        this.OtherBenefitsifAny = OtherBenefitsifAny;
        this.guaranteed_maturity_benefit = guaranteed_maturity_benefit;
        this.guaranteed_death_benefit = guaranteed_death_benefit;
        this.guaranteed_surrender_value = guaranteed_surrender_value;
        this.nonGuaranSurrenderValue = nonGuaranSurrenderValue;
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

    /*public String getCumulative_premium() {
        return cumulative_premium;
    }
    public void setCumulative_premium(String cumulative_premium) {
        this.cumulative_premium = cumulative_premium;
    }*/
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

    public String getSurvivalBenefits() {
        return SurvivalBenefits;
    }

    public void setSurvivalBenefits(String survivalBenefits) {
        SurvivalBenefits = survivalBenefits;
    }

    public String getOtherBenefitsifAny() {
        return OtherBenefitsifAny;
    }

    public void setOtherBenefitsifAny(String otherBenefitsifAny) {
        OtherBenefitsifAny = otherBenefitsifAny;
    }

    public String getNonGuaranSurrenderValue() {
        return nonGuaranSurrenderValue;
    }

    public void setNonGuaranSurrenderValue(String nonGuaranSurrenderValue) {
        this.nonGuaranSurrenderValue = nonGuaranSurrenderValue;
    }
}
