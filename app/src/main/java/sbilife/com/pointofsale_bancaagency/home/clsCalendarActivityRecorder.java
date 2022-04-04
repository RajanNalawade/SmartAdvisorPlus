package sbilife.com.pointofsale_bancaagency.home;

public class clsCalendarActivityRecorder {

    private int _id;
    private String _datename;
    private String _eventname;
    private String _month;
    private String _year;
    private String _time;
    private String _remark;
    private String _activity;
    private String _subactivity;
    private String _timeto;
    private String _userid;
    private String _sync;
    private String _createddate;
    private String _modifieddate;
    private String _status;
    private String _lead;

    public clsCalendarActivityRecorder(String DateName, String EventName, String Month, String Year, String Time, String Remark, String Activity,
                                       String strSubActivity, String strTimeTo, String strUserId, String sync,
                                       String createddate, String modifieddate, String status, String lead) {
        this._datename = DateName;
        this._eventname = EventName;
        this._month = Month;
        this._year = Year;
        this._time = Time;
        this._remark = Remark;
        this._activity = Activity;
        this._subactivity = strSubActivity;
        this._timeto = strTimeTo;
        this._userid = strUserId;
        this._sync = sync;
        this._createddate = createddate;
        this._modifieddate = modifieddate;
        this._status = status;
        this._lead = lead;
    }

    public int getID() {
        return this._id;
    }

    public void SetID(int ID) {
        this._id = ID;
    }

    public String getDateName() {
        return this._datename;
    }

    public void setDateName(String datename) {
        this._datename = datename;
    }

    public String getEventName() {
        return this._eventname;
    }

    public void setEventName(String eventname) {
        this._eventname = eventname;
    }

    public String getMonth() {
        return this._month;
    }

    public void setMonth(String month) {
        this._month = month;
    }

    public String getYear() {
        return this._year;
    }

    public void setYear(String Year) {
        this._year = Year;
    }

    public String getTime() {
        return this._time;
    }

    public void setTime(String Time) {
        this._time = Time;
    }

    public String getRemark() {
        return this._remark;
    }

    public void setRemark(String Remark) {
        this._remark = Remark;
    }

    public String getActivity() {
        return this._activity;
    }

    public void setActivity(String Activity) {
        this._activity = Activity;
    }

    public String getSubActivity() {
        return this._subactivity;
    }

    public void setSubActivity(String SubActivity) {
        this._subactivity = SubActivity;
    }

    public String getTimeTo() {
        return this._timeto;
    }

    public void setTimeTo(String timeto) {
        this._timeto = timeto;
    }

    public String getUserId() {
        return this._userid;
    }

    public void setUserId(String userid) {
        this._userid = userid;
    }

    public String getSync() {
        return this._sync;
    }

    public void setSync(String sync) {
        this._sync = sync;
    }

    public String getCreatedDate() {
        return this._createddate;
    }

    public void setCreatedDate(String createddate) {
        this._createddate = createddate;
    }

    public String getModifiedDate() {
        return this._modifieddate;
    }

    public void setModifiedDate(String modifieddate) {
        this._modifieddate = modifieddate;
    }

    public String getStatus() {
        return this._status;
    }

    public void setStatus(String status) {
        this._status = status;
    }

    public String getLead() {
        return this._lead;
    }

    public void setLead(String lead) {
        this._lead = lead;
    }
}
