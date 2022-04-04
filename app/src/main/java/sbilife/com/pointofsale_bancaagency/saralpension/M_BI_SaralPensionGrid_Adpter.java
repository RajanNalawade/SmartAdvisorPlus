package sbilife.com.pointofsale_bancaagency.saralpension;

class M_BI_SaralPensionGrid_Adpter {

    private String year_of_death;
    private String guaranteed_death_benefit;
    private String bonuses_non_guarantee_4pa;
    private String bonuses_non_guarantee_8pa;
    private String vesting_benefit_4pa;
    private String vesting_benefit_8pa;

    public M_BI_SaralPensionGrid_Adpter(String year_of_death,
                                        String guaranteed_death_benefit, String bonuses_non_guarantee_4pa,
                                        String bonuses_non_guarantee_8pa,
                                        String vesting_benefit_4pa,
                                        String vesting_benefit_8pa) {
        super();
        this.year_of_death = year_of_death;
        this.guaranteed_death_benefit = guaranteed_death_benefit;
        this.bonuses_non_guarantee_4pa = bonuses_non_guarantee_4pa;
        this.bonuses_non_guarantee_8pa = bonuses_non_guarantee_8pa;
        this.vesting_benefit_4pa = vesting_benefit_4pa;
        this.vesting_benefit_8pa = vesting_benefit_8pa;

    }

    public String getYear_of_death() {
        return year_of_death;
    }

    public void setYear_of_death(String year_of_death) {
        this.year_of_death = year_of_death;
    }

    public String getGuaranteed_death_benefit() {
        return guaranteed_death_benefit;
    }

    public void setGuaranteed_death_benefit(String guaranteed_death_benefit) {
        this.guaranteed_death_benefit = guaranteed_death_benefit;
    }

    public String getBonuses_non_guarantee_4pa() {
        return bonuses_non_guarantee_4pa;
    }

    public void setBonuses_non_guarantee_4pa(String bonuses_non_guarantee_4pa) {
        this.bonuses_non_guarantee_4pa = bonuses_non_guarantee_4pa;
    }

    public String getBonuses_non_guarantee_8pa() {
        return bonuses_non_guarantee_8pa;
    }

    public void setBonuses_non_guarantee_8pa(String bonuses_non_guarantee_8pa) {
        this.bonuses_non_guarantee_8pa = bonuses_non_guarantee_8pa;
    }

    public String getVesting_benefit_4pa() {
        return vesting_benefit_4pa;
    }

    public void setVesting_benefit_4pa(String vesting_benefit_4pa) {
        this.vesting_benefit_4pa = vesting_benefit_4pa;
    }

    public String getVesting_benefit_8pa() {
        return vesting_benefit_8pa;
    }

    public void setVesting_benefit_8pa(String vesting_benefit_8pa) {
        this.vesting_benefit_8pa = vesting_benefit_8pa;
    }
}
