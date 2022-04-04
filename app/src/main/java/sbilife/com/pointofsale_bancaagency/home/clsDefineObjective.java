package sbilife.com.pointofsale_bancaagency.home;

public class clsDefineObjective {

    private int _id;
    private String _code;
    private String _name;
    private String _newbuscash;
    private String _homeloan;
    private String _newbuspre;
    private String _share;
    private String _remark;
    private String _userid;

    public clsDefineObjective(String code, String name, String newbuscash, String homeloan, String bewbuspre, String share, String remark, String userid) {
        this._code = code;
        this._name = name;
        this._newbuscash = newbuscash;
        this._homeloan = homeloan;
        this._newbuspre = bewbuspre;
        this._share = share;
        this._remark = remark;
        this._userid = userid;
    }

    public int getID() {
        return this._id;
    }

    public void SetID(int ID) {
        this._id = ID;
    }

    public String getCode() {
        return this._code;
    }

    public void setCode(String code) {
        this._code = code;
    }

    public String getName() {
        return this._name;
    }

    public void setName(String name) {
        this._name = name;
    }

    public String getNewBusCash() {
        return this._newbuscash;
    }

    public void setNewBusCash(String newbuscash) {
        this._newbuscash = newbuscash;
    }

    public String getHomeLoan() {
        return this._homeloan;
    }

    public void setHomeLoan(String homeloan) {
        this._homeloan = homeloan;
    }

    public String getNewBusPre() {
        return this._newbuspre;
    }

    public void setNewBusPre(String newbuspre) {
        this._newbuspre = newbuspre;
    }

    public String getShare() {
        return this._share;
    }

    public void setShare(String share) {
        this._share = share;
    }

    public String getRemark() {
        return this._remark;
    }

    public void setRemark(String remark) {
        this._remark = remark;
    }

    public String getUserId() {
        return this._userid;
    }

    public void setUserId(String userid) {
        this._userid = userid;
    }
}
