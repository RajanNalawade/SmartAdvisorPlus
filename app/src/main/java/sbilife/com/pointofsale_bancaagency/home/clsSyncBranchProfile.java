package sbilife.com.pointofsale_bancaagency.home;

public class clsSyncBranchProfile {

    private int _id;
    private String branchcode;
    private String perticular;
    private String category;
    private String cross_salling;
    private String cross_salling_nops;
    private String cross_salling_pre;
    private String userid;

    public clsSyncBranchProfile(String branchcode, String perticular,
                                String category, String cross_salling, String cross_salling_nops,
                                String cross_salling_pre, String userid) {

        this.branchcode = branchcode;
        this.perticular = perticular;
        this.category = category;
        this.cross_salling = cross_salling;
        this.cross_salling_nops = cross_salling_nops;
        this.cross_salling_pre = cross_salling_pre;
        this.userid = userid;
    }

    /**
     * @return the _id
     */
    public int get_id() {
        return _id;
    }

    /**
     * @param _id the _id to set
     */
    public void set_id(int _id) {
        this._id = _id;
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
     * @return the perticular
     */
    public String getPerticular() {
        return perticular;
    }

    /**
     * @param perticular the perticular to set
     */
    public void setPerticular(String perticular) {
        this.perticular = perticular;
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

    /**
     * @return the cross_salling
     */
    public String getCross_salling() {
        return cross_salling;
    }

    /**
     * @param cross_salling the cross_salling to set
     */
    public void setCross_salling(String cross_salling) {
        this.cross_salling = cross_salling;
    }

    /**
     * @return the cross_salling_nops
     */
    public String getCross_salling_nops() {
        return cross_salling_nops;
    }

    /**
     * @param cross_salling_nops the cross_salling_nops to set
     */
    public void setCross_salling_nops(String cross_salling_nops) {
        this.cross_salling_nops = cross_salling_nops;
    }

    /**
     * @return the cross_salling_pre
     */
    public String getCross_salling_pre() {
        return cross_salling_pre;
    }

    /**
     * @param cross_salling_pre the cross_salling_pre to set
     */
    public void setCross_salling_pre(String cross_salling_pre) {
        this.cross_salling_pre = cross_salling_pre;
    }

    /**
     * @return the userid
     */
    public String getUserid() {
        return userid;
    }

    /**
     * @param userid the userid to set
     */
    public void setUserid(String userid) {
        this.userid = userid;
    }


}
