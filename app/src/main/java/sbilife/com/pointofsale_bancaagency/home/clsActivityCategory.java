package sbilife.com.pointofsale_bancaagency.home;

public class clsActivityCategory {

    private int _id;
    private String _name;
    private String _discription;
    private String _parentid;
    private String _createdby;
    private String _createddate;
    private String _modifiedby;
    private String _modifieddate;

    public clsActivityCategory(String name, String dis, String parentid, String createdby, String createddate, String modifiedby, String modifieddate) {
        this._name = name;
        this._discription = dis;
        this._parentid = parentid;
        this._createdby = createdby;
        this._createddate = createddate;
        this._modifiedby = modifiedby;
        this._modifieddate = modifieddate;
    }

    public int getID() {
        return this._id;
    }

    public void SetID(int ID) {
        this._id = ID;
    }

    public String getName() {
        return this._name;
    }

    public void setName(String name) {
        this._name = name;
    }

    public String getDiscription() {
        return this._discription;
    }

    public void setDiscription(String Discription) {
        this._discription = Discription;
    }

    public String getParentId() {
        return this._parentid;
    }

    public void setParentId(String ParentId) {
        this._parentid = ParentId;
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
}
