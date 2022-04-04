package sbilife.com.pointofsale_bancaagency.home;

/*
 * while registering product that time check version service fire and it
 * is hold that information.
 */

public class clsDefineObjectiveSync {

    private int _id;
    private String _code;
    private String _userid;
    private String _syncFlag;

    public clsDefineObjectiveSync(String code, String userid, String syncflag) {
        this._code = code;
        this._userid = userid;
        this._syncFlag = syncflag;

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

    public String getUserId() {
        return this._userid;
    }

    public void setUserId(String userid) {
        this._userid = userid;
    }

    public String getSyncFlag() {
        return this._syncFlag;
    }

    public void setSyncFlag(String syncflag) {
        this._syncFlag = syncflag;
    }
}
