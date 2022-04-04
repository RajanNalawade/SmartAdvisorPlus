package sbilife.com.pointofsale_bancaagency.home;

public class clsBranchProfile {

    private int _id;
    private String branchcode;
    private String branch_name;
    private String branch_open_date;
    private String branch_net_result;
    private String branch_created_date;

    public clsBranchProfile(String branchcode, String branch_name,
                            String branch_open_date, String branch_net_result,
                            String branch_created_date) {

        this.branchcode = branchcode;
        this.branch_name = branch_name;
        this.branch_open_date = branch_open_date;
        this.branch_net_result = branch_net_result;
        this.branch_created_date = branch_created_date;
    }

    public int getID() {
        return this._id;
    }

    public void SetID(int ID) {
        this._id = ID;
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
     * @return the branch_name
     */
    public String getBranch_name() {
        return branch_name;
    }

    /**
     * @param branch_name the branch_name to set
     */
    public void setBranch_name(String branch_name) {
        this.branch_name = branch_name;
    }

    /**
     * @return the branch_open_date
     */
    public String getBranch_open_date() {
        return branch_open_date;
    }

    /**
     * @param branch_open_date the branch_open_date to set
     */
    public void setBranch_open_date(String branch_open_date) {
        this.branch_open_date = branch_open_date;
    }

    /**
     * @return the branch_net_result
     */
    public String getBranch_net_result() {
        return branch_net_result;
    }

    /**
     * @param branch_net_result the branch_net_result to set
     */
    public void setBranch_net_result(String branch_net_result) {
        this.branch_net_result = branch_net_result;
    }

    /**
     * @return the branch_created_date
     */
    public String getBranch_created_date() {
        return branch_created_date;
    }

    /**
     * @param branch_created_date the branch_created_date to set
     */
    public void setBranch_created_date(String branch_created_date) {
        this.branch_created_date = branch_created_date;
    }
}
