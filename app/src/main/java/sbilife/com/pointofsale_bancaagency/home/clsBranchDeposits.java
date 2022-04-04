package sbilife.com.pointofsale_bancaagency.home;

public class clsBranchDeposits {

    private int _id;
    private String depositid;

    private String branchcode;

    private String tot_acc;

    private String tot_amt;

    private String new_acc_b1k;

    private String new_balance_b1k;

    private String new_acc_10kto1l;

    String new_bal_10kto1l;

    private String new_acc_1lto5l;

    private String new_bal_1lto5l;

    private String new_acc_5landA;

    private String new_bal_5landA;

    private String category;

    public clsBranchDeposits(String depositid, String branchcode,
                             String tot_acc, String tot_amt, String new_acc_b1k,
                             String new_balance_b1k, String new_acc_10kto1l,
                             String new_bal_10kto1l, String new_acc_1lto5l,
                             String new_bal_1lto5l, String new_acc_5landA,
                             String new_bal_5landA, String category) {

        this.depositid = depositid;
        this.branchcode = branchcode;
        this.tot_acc = tot_acc;
        this.tot_amt = tot_amt;
        this.new_acc_b1k = new_acc_b1k;
        this.new_balance_b1k = new_balance_b1k;
        this.new_acc_10kto1l = new_acc_10kto1l;
        this.new_bal_10kto1l = new_bal_10kto1l;
        this.new_acc_1lto5l = new_acc_1lto5l;
        this.new_bal_1lto5l = new_bal_1lto5l;
        this.new_acc_5landA = new_acc_5landA;
        this.new_bal_5landA = new_bal_5landA;
        this.category = category;
    }

    public int getID() {
        return this._id;
    }

    public void SetID(int ID) {
        this._id = ID;
    }

    /**
     * @return the depositid
     */
    public String getDepositid() {
        return depositid;
    }

    /**
     * @param depositid the depositid to set
     */
    public void setDepositid(String depositid) {
        this.depositid = depositid;
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
     * @return the tot_acc
     */
    public String getTot_acc() {
        return tot_acc;
    }

    /**
     * @param tot_acc the tot_acc to set
     */
    public void setTot_acc(String tot_acc) {
        this.tot_acc = tot_acc;
    }

    /**
     * @return the tot_amt
     */
    public String getTot_amt() {
        return tot_amt;
    }

    /**
     * @param tot_amt the tot_amt to set
     */
    public void setTot_amt(String tot_amt) {
        this.tot_amt = tot_amt;
    }

    /**
     * @return the new_acc_b1k
     */
    public String getNew_acc_b1k() {
        return new_acc_b1k;
    }

    /**
     * @param new_acc_b1k the new_acc_b1k to set
     */
    public void setNew_acc_b1k(String new_acc_b1k) {
        this.new_acc_b1k = new_acc_b1k;
    }

    /**
     * @return the new_balance_b1k
     */
    public String getNew_balance_b1k() {
        return new_balance_b1k;
    }

    /**
     * @param new_balance_b1k the new_balance_b1k to set
     */
    public void setNew_balance_b1k(String new_balance_b1k) {
        this.new_balance_b1k = new_balance_b1k;
    }

    /**
     * @return the new_acc_10kto1l
     */
    public String getNew_acc_10kto1l() {
        return new_acc_10kto1l;
    }

    /**
     * @param new_acc_10kto1l the new_acc_10kto1l to set
     */
    public void setNew_acc_10kto1l(String new_acc_10kto1l) {
        this.new_acc_10kto1l = new_acc_10kto1l;
    }

    /**
     * @return the new_bal_10kto1l
     */
    public String getNew_bal_10kto1l() {
        return new_bal_10kto1l;
    }

    /**
     * @param new_bal_10kto1l the new_bal_10kto1l to set
     */
    public void setNew_bal_10kto1l(String new_bal_10kto1l) {
        this.new_bal_10kto1l = new_bal_10kto1l;
    }

    /**
     * @return the new_acc_1lto5l
     */
    public String getNew_acc_1lto5l() {
        return new_acc_1lto5l;
    }

    /**
     * @param new_acc_1lto5l the new_acc_1lto5l to set
     */
    public void setNew_acc_1lto5l(String new_acc_1lto5l) {
        this.new_acc_1lto5l = new_acc_1lto5l;
    }

    /**
     * @return the new_bal_1lto5l
     */
    public String getNew_bal_1lto5l() {
        return new_bal_1lto5l;
    }

    /**
     * @param new_bal_1lto5l the new_bal_1lto5l to set
     */
    public void setNew_bal_1lto5l(String new_bal_1lto5l) {
        this.new_bal_1lto5l = new_bal_1lto5l;
    }

    /**
     * @return the new_acc_5landA
     */
    public String getNew_acc_5landA() {
        return new_acc_5landA;
    }

    /**
     * @param new_acc_5landA the new_acc_5landA to set
     */
    public void setNew_acc_5landA(String new_acc_5landA) {
        this.new_acc_5landA = new_acc_5landA;
    }

    /**
     * @return the new_bal_5landA
     */
    public String getNew_bal_5landA() {
        return new_bal_5landA;
    }

    /**
     * @param new_bal_5landA the new_bal_5landA to set
     */
    public void setNew_bal_5landA(String new_bal_5landA) {
        this.new_bal_5landA = new_bal_5landA;
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
