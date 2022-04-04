package sbilife.com.pointofsale_bancaagency.home;

public class clsEmail {

    private int _id;
    private String _type;
    private String _name;
    private String _createdby;
    private String _createddate;
    private String _modifiedby;
    private String _modifieddate;
    private String _userid;

    public clsEmail(String type, String name, String createdby, String createddate, String modifiedby, String modifieddate, String userid) {
        this._type = type;
        this._name = name;
        this._createdby = createdby;
        this._createddate = createddate;
        this._modifiedby = modifiedby;
        this._modifieddate = modifieddate;
        this._userid = userid;
    }

    public int getID() {
        return this._id;
    }

    public void SetID(int ID) {
        this._id = ID;
    }

    public String getType() {
        return this._type;
    }

    public void setType(String type) {
        this._type = type;
    }

    public String getName() {
        return this._name;
    }

    public void setName(String name) {
        this._name = name;
    }

    public String getCreatedBy() {
        return this._createdby;
    }

    public void setCreatedBy(String CreatedBy) {
        this._createdby = CreatedBy;
    }

    public String getCreatedDate() {
        return this._createddate;
    }

    public void setCreatedDate(String CreatedDate) {
        this._createddate = CreatedDate;
    }

    public String getModifiedBy() {
        return this._modifiedby;
    }

    public void setModifiedBy(String ModifiedBy) {
        this._modifiedby = ModifiedBy;
    }

    public String getModifiedDate() {
        return this._modifieddate;
    }

    public void setModifiedDate(String ModifiedDate) {
        this._modifieddate = ModifiedDate;
    }

    public String getUserId() {
        return this._userid;
    }

    public void setUserId(String userid) {
        this._userid = userid;
    }
}
