package sbilife.com.pointofsale_bancaagency.rinnraksha;

public class RinRakshaBean {

    private String str_source_branch_code = "", str_pf_sourcing_staff_cif = "",
            str_pf_hlst_mpst_staff = "", str_pf_hlst_mpst_head = "",
            str_life_assured_tittle = "", str_life_assured_first_name = "",
            str_life_assured_middle_name = "", str_life_assured_last_name = "",
            str_life_assured_dob = "", str_email = "", str_mobile = "",
            str_gender = "", str_dob_borrower1 = "", str_dob_borrower2 = "",
            str_dob_borrower3 = "", str_membership_date = "", str_co_borrower_option = "",
            str_loan_type = "", str_loan_sub_catagory = "", str_intrest_rate_range = "",
            str_premium_payment_option = "", str_premium_freq_mode = "", str_is_marato_checked = "",
            str_premium_paid_by = "", str_is_int_pay_during_marato = "",
            str_is_staff = "", str_is_jk_resident = "", str_is_co_borrower = "";

    private double marato_period = 0.0, cover_intrest_rate = 0.0;
    private int loan_amount = 0, loan_term = 0, premium_payment_term = 0, option = 0,
            loan_share_borrower1 = 0, loan_share_borrower2 = 0, loan_share_borrower3 = 0;
    private String strQuotationNo = "", strURNBorrower1 = "", strURNBorrower2 = "",
            strURNBorrower3 = "";
    private String strSourceBy = "", strPlanType = "";

    public String getStrPlanType() {
        return strPlanType;
    }

    public void setStrPlanType(String strPlanType) {
        this.strPlanType = strPlanType;
    }

    public String getStrSourceBy() {
        return strSourceBy;
    }

    public void setStrSourceBy(String strSourceBy) {
        this.strSourceBy = strSourceBy;
    }

    public String getStr_source_branch_code() {
        return str_source_branch_code;
    }

    public void setStr_source_branch_code(String str_source_branch_code) {
        this.str_source_branch_code = str_source_branch_code;
    }

    public String getStr_pf_sourcing_staff_cif() {
        return str_pf_sourcing_staff_cif;
    }

    public void setStr_pf_sourcing_staff_cif(String str_pf_sourcing_staff_cif) {
        this.str_pf_sourcing_staff_cif = str_pf_sourcing_staff_cif;
    }

    public String getStr_pf_hlst_mpst_staff() {
        return str_pf_hlst_mpst_staff;
    }

    public void setStr_pf_hlst_mpst_staff(String str_pf_hlst_mpst_staff) {
        this.str_pf_hlst_mpst_staff = str_pf_hlst_mpst_staff;
    }

    public String getStr_pf_hlst_mpst_head() {
        return str_pf_hlst_mpst_head;
    }

    public void setStr_pf_hlst_mpst_head(String str_pf_hlst_mpst_head) {
        this.str_pf_hlst_mpst_head = str_pf_hlst_mpst_head;
    }

    public String getStr_life_assured_tittle() {
        return str_life_assured_tittle;
    }

    public void setStr_life_assured_tittle(String str_life_assured_tittle) {
        this.str_life_assured_tittle = str_life_assured_tittle;
    }

    public String getStr_life_assured_first_name() {
        return str_life_assured_first_name;
    }

    public void setStr_life_assured_first_name(String str_life_assured_first_name) {
        this.str_life_assured_first_name = str_life_assured_first_name;
    }

    public String getStr_life_assured_middle_name() {
        return str_life_assured_middle_name;
    }

    public void setStr_life_assured_middle_name(String str_life_assured_middle_name) {
        this.str_life_assured_middle_name = str_life_assured_middle_name;
    }

    public String getStr_life_assured_last_name() {
        return str_life_assured_last_name;
    }

    public void setStr_life_assured_last_name(String str_life_assured_last_name) {
        this.str_life_assured_last_name = str_life_assured_last_name;
    }

    public String getStr_life_assured_dob() {
        return str_life_assured_dob;
    }

    public void setStr_life_assured_dob(String str_life_assured_dob) {
        this.str_life_assured_dob = str_life_assured_dob;
    }

    public String getStr_email() {
        return str_email;
    }

    public void setStr_email(String str_email) {
        this.str_email = str_email;
    }

    public String getStr_mobile() {
        return str_mobile;
    }

    public void setStr_mobile(String str_mobile) {
        this.str_mobile = str_mobile;
    }

    public String getStr_gender() {
        return str_gender;
    }

    public void setStr_gender(String str_gender) {
        this.str_gender = str_gender;
    }

    public String getStr_dob_borrower1() {
        return str_dob_borrower1;
    }

    public void setStr_dob_borrower1(String str_dob_borrower1) {
        this.str_dob_borrower1 = str_dob_borrower1;
    }

    public String getStr_dob_borrower2() {
        return str_dob_borrower2;
    }

    public void setStr_dob_borrower2(String str_dob_borrower2) {
        this.str_dob_borrower2 = str_dob_borrower2;
    }

    public String getStr_dob_borrower3() {
        return str_dob_borrower3;
    }

    public void setStr_dob_borrower3(String str_dob_borrower3) {
        this.str_dob_borrower3 = str_dob_borrower3;
    }

    public String getStr_membership_date() {
        return str_membership_date;
    }

    public void setStr_membership_date(String str_membership_date) {
        this.str_membership_date = str_membership_date;
    }

    public String getStr_co_borrower_option() {
        return str_co_borrower_option;
    }

    public void setStr_co_borrower_option(String str_co_borrower_option) {
        this.str_co_borrower_option = str_co_borrower_option;
    }

    public String getStr_loan_type() {
        return str_loan_type;
    }

    public void setStr_loan_type(String str_loan_type) {
        this.str_loan_type = str_loan_type;
    }

    public String getStr_loan_sub_catagory() {
        return str_loan_sub_catagory;
    }

    public void setStr_loan_sub_catagory(String str_loan_sub_catagory) {
        this.str_loan_sub_catagory = str_loan_sub_catagory;
    }

    public String getStr_intrest_rate_range() {
        return str_intrest_rate_range;
    }

    public void setStr_intrest_rate_range(String str_intrest_rate_range) {
        this.str_intrest_rate_range = str_intrest_rate_range;
    }

    public String getStr_premium_payment_option() {
        return str_premium_payment_option;
    }

    public void setStr_premium_payment_option(String str_premium_payment_option) {
        this.str_premium_payment_option = str_premium_payment_option;
    }

    public String getStr_premium_freq_mode() {
        return str_premium_freq_mode;
    }

    public void setStr_premium_freq_mode(String str_premium_freq_mode) {
        this.str_premium_freq_mode = str_premium_freq_mode;
    }

    public String getStr_is_marato_checked() {
        return str_is_marato_checked;
    }

    public void setStr_is_marato_checked(String str_is_marato_checked) {
        this.str_is_marato_checked = str_is_marato_checked;
    }

    public String getStr_premium_paid_by() {
        return str_premium_paid_by;
    }

    public void setStr_premium_paid_by(String str_premium_paid_by) {
        this.str_premium_paid_by = str_premium_paid_by;
    }

    public String getStr_is_int_pay_during_marato() {
        return str_is_int_pay_during_marato;
    }

    public void setStr_is_int_pay_during_marato(String str_is_int_pay_during_marato) {
        this.str_is_int_pay_during_marato = str_is_int_pay_during_marato;
    }

    public String getStr_is_staff() {
        return str_is_staff;
    }

    public void setStr_is_staff(String str_is_staff) {
        this.str_is_staff = str_is_staff;
    }

    public String getStr_is_jk_resident() {
        return str_is_jk_resident;
    }

    public void setStr_is_jk_resident(String str_is_jk_resident) {
        this.str_is_jk_resident = str_is_jk_resident;
    }

    public String getStr_is_co_borrower() {
        return str_is_co_borrower;
    }

    public void setStr_is_co_borrower(String str_is_co_borrower) {
        this.str_is_co_borrower = str_is_co_borrower;
    }

    public double getMarato_period() {
        return marato_period;
    }

    public void setMarato_period(double marato_period) {
        this.marato_period = marato_period;
    }

    public double getCover_intrest_rate() {
        return cover_intrest_rate;
    }

    public void setCover_intrest_rate(double cover_intrest_rate) {
        this.cover_intrest_rate = cover_intrest_rate;
    }

    public int getLoan_amount() {
        return loan_amount;
    }

    public void setLoan_amount(int loan_amount) {
        this.loan_amount = loan_amount;
    }

    public int getLoan_term() {
        return loan_term;
    }

    public void setLoan_term(int loan_term) {
        this.loan_term = loan_term;
    }

    public int getPremium_payment_term() {
        return premium_payment_term;
    }

    public void setPremium_payment_term(int premium_payment_term) {
        this.premium_payment_term = premium_payment_term;
    }

    public int getOption() {
        return option;
    }

    public void setOption(int option) {
        this.option = option;
    }

    public int getLoan_share_borrower1() {
        return loan_share_borrower1;
    }

    public void setLoan_share_borrower1(int loan_share_borrower1) {
        this.loan_share_borrower1 = loan_share_borrower1;
    }

    public int getLoan_share_borrower2() {
        return loan_share_borrower2;
    }

    public void setLoan_share_borrower2(int loan_share_borrower2) {
        this.loan_share_borrower2 = loan_share_borrower2;
    }

    public int getLoan_share_borrower3() {
        return loan_share_borrower3;
    }

    public void setLoan_share_borrower3(int loan_share_borrower3) {
        this.loan_share_borrower3 = loan_share_borrower3;
    }

    public String getStrQuotationNo() {
        return strQuotationNo;
    }

    public void setStrQuotationNo(String strQuotationNo) {
        this.strQuotationNo = strQuotationNo;
    }

    public String getStrURNBorrower1() {
        return strURNBorrower1;
    }

    public void setStrURNBorrower1(String strURNBorrower1) {
        this.strURNBorrower1 = strURNBorrower1;
    }

    public String getStrURNBorrower2() {
        return strURNBorrower2;
    }

    public void setStrURNBorrower2(String strURNBorrower2) {
        this.strURNBorrower2 = strURNBorrower2;
    }

    public String getStrURNBorrower3() {
        return strURNBorrower3;
    }

    public void setStrURNBorrower3(String strURNBorrower3) {
        this.strURNBorrower3 = strURNBorrower3;
    }


}
