package sbilife.com.pointofsale_bancaagency.products.smartplatinaplus;

public class M_BI_SmartPlatinaPlus_Adapter {

    String end_of_year = "";
    String yearly_basic_premium = "";
    String SurvivalBenefit = "";
    String OtherBenefit = "";
    String guaranteed_death_benefit = "";
    String guaranteed_maturity_benefit = "";
    String guaranteed_surrender_value = "";
    String SpecialSurrenderValue = "";

    public M_BI_SmartPlatinaPlus_Adapter(String end_of_year,
                                         String yearly_basic_premium,
                                         String SurvivalBenefit,
                                         String OtherBenefit,
                                         String guaranteed_maturity_benefit, String guaranteed_death_benefit,
                                         String guaranteed_surrender_value, String SpecialSurrenderValue) {
        super();
        this.end_of_year = end_of_year;
        this.yearly_basic_premium = yearly_basic_premium;
        this.SurvivalBenefit = SurvivalBenefit;
        this.OtherBenefit = OtherBenefit;
        this.guaranteed_maturity_benefit = guaranteed_maturity_benefit;
        this.guaranteed_death_benefit = guaranteed_death_benefit;
        this.guaranteed_surrender_value = guaranteed_surrender_value;
        this.SpecialSurrenderValue = SpecialSurrenderValue;
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

    public String getSurvivalBenefit() {
        return SurvivalBenefit;
    }

    public void setSurvivalBenefit(String survivalBenefit) {
        SurvivalBenefit = survivalBenefit;
    }

    public String getOtherBenefit() {
        return OtherBenefit;
    }

    public void setOtherBenefit(String otherBenefit) {
        OtherBenefit = otherBenefit;
    }

    public String getSpecialSurrenderValue() {
        return SpecialSurrenderValue;
    }

    public void setSpecialSurrenderValue(String specialSurrenderValue) {
        SpecialSurrenderValue = specialSurrenderValue;
    }
}
