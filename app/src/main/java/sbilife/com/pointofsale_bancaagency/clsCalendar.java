package sbilife.com.pointofsale_bancaagency;

/*
 * while adding event in calendar it is hold by this class.
 */

class clsCalendar {

	private int _id;
	private String _datename;
	private String _eventname;
	private String _month;
	private String _year;
	private String _time;
	private String _userid;
	private String _notes;

	public clsCalendar(String DateName, String EventName, String Month,String Year,String Time,String Userid, String Notes) {	
		this._datename = DateName;
		this._eventname = EventName;
		this._month = Month;
		this._year = Year;
		this._time = Time;
		this._userid = Userid;
		this._notes = Notes;
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
	
	public String getUserId() {
		return this._userid;
	}

	public void setUserId(String userid) {
		this._userid = userid;
	}
	
	public String getNotes() {
		return this._notes;
	}

	public void setNotes(String notes) {
		this._notes = notes;
	}
}
