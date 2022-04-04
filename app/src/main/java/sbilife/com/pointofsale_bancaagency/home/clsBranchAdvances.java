package sbilife.com.pointofsale_bancaagency.home;

public class clsBranchAdvances {

    private int _id;
    private String advancesid;

    private String branchcode;

    private String tot_no_of_acc;

    private String tot_out;

    private String no_of_acc_b1l;

    private String tot_out_b1l;

    private String no_of_acc_1lto5l;

    private String tot_out_1lto5l;

    private String no_of_acc_a5l;

    private String tot_out_a5l;

    private String category;

    public clsBranchAdvances(String advancesid, String branchcode, String tot_no_of_acc,
                             String tot_out, String no_of_acc_b1l, String tot_out_b1l,
                             String no_of_acc_1lto5l, String tot_out_1lto5l, String no_of_acc_a5l,
                             String tot_out_a5l, String category) {

        this.advancesid = advancesid;

        this.branchcode = branchcode;

        this.tot_no_of_acc = tot_no_of_acc;

        this.tot_out = tot_out;

        this.no_of_acc_b1l = no_of_acc_b1l;

        this.tot_out_b1l = tot_out_b1l;

        this.no_of_acc_1lto5l = no_of_acc_1lto5l;

        this.tot_out_1lto5l = tot_out_1lto5l;

        this.no_of_acc_a5l = no_of_acc_a5l;

        this.tot_out_a5l = tot_out_a5l;

        this.category = category;
    }

    public int getID() {
        return this._id;
    }

    public void SetID(int ID) {
        this._id = ID;
    }

    /**
     * @return the advancesid
     */
    public String getAdvancesid() {
        return advancesid;
    }

    /**
     * @param advancesid the advancesid to set
     */
    public void setAdvancesid(String advancesid) {
        this.advancesid = advancesid;
    }

    /**
     * @return the branchcode
     */
    public String getBranchcode() {
        return branchcode;
    }

    /**
     * @param branchcode the branchcode to set
     */
    public void setBranchcode(String branchcode) {
        this.branchcode = branchcode;
    }

    /**
     * @return the tot_no_of_acc
     */
    public String getTot_no_of_acc() {
        return tot_no_of_acc;
    }

    /**
     * @param tot_no_of_acc the tot_no_of_acc to set
     */
    public void setTot_no_of_acc(String tot_no_of_acc) {
        this.tot_no_of_acc = tot_no_of_acc;
    }

    /**
     * @return the tot_out
     */
    public String getTot_out() {
        return tot_out;
    }

    /**
     * @param tot_out the tot_out to set
     */
    public void setTot_out(String tot_out) {
        this.tot_out = tot_out;
    }

    /**
     * @return the no_of_acc_b1l
     */
    public String getNo_of_acc_b1l() {
        return no_of_acc_b1l;
    }

    /**
     * @param no_of_acc_b1l the no_of_acc_b1l to set
     */
    public void setNo_of_acc_b1l(String no_of_acc_b1l) {
        this.no_of_acc_b1l = no_of_acc_b1l;
    }

    /**
     * @return the tot_out_b1l
     */
    public String getTot_out_b1l() {
        return tot_out_b1l;
    }

    /**
     * @param tot_out_b1l the tot_out_b1l to set
     */
    public void setTot_out_b1l(String tot_out_b1l) {
        this.tot_out_b1l = tot_out_b1l;
    }

    /**
     * @return the no_of_acc_1lto5l
     */
    public String getNo_of_acc_1lto5l() {
        return no_of_acc_1lto5l;
    }

    /**
     * @param no_of_acc_1lto5l the no_of_acc_1lto5l to set
     */
    public void setNo_of_acc_1lto5l(String no_of_acc_1lto5l) {
        this.no_of_acc_1lto5l = no_of_acc_1lto5l;
    }

    /**
     * @return the tot_out_1lto5l
     */
    public String getTot_out_1lto5l() {
        return tot_out_1lto5l;
    }

    /**
     * @param tot_out_1lto5l the tot_out_1lto5l to set
     */
    public void setTot_out_1lto5l(String tot_out_1lto5l) {
        this.tot_out_1lto5l = tot_out_1lto5l;
    }

    /**
     * @return the no_of_acc_a5l
     */
    public String getNo_of_acc_a5l() {
        return no_of_acc_a5l;
    }

    /**
     * @param no_of_acc_a5l the no_of_acc_a5l to set
     */
    public void setNo_of_acc_a5l(String no_of_acc_a5l) {
        this.no_of_acc_a5l = no_of_acc_a5l;
    }

    /**
     * @return the tot_out_a5l
     */
    public String getTot_out_a5l() {
        return tot_out_a5l;
    }

    /**
     * @param tot_out_a5l the tot_out_a5l to set
     */
    public void setTot_out_a5l(String tot_out_a5l) {
        this.tot_out_a5l = tot_out_a5l;
    }

    /**
     * @return the category
     */
    public String getCategory() {
        return category;
    }

    /**
     * @param category the category to set
     */
    public void setCategory(String category) {
        this.category = category;
    }
}
