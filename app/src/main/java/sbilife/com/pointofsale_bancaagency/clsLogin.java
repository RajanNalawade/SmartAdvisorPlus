package sbilife.com.pointofsale_bancaagency;

/*
 * while adding information in login it is hold by this class.
 */

public class clsLogin {

	private int _id;
	private String _title;
	private String _fname;
	private String _lname;
	private String _address;
	private String _status;
	private String _cifno;
	private String _patename;
	private String _email;
	private String _Pass;
	private String _confirmPass;
	private String _question;
	private String _mobileno;
	private String _dob;
	private String _type;
	private String PIN;

	public clsLogin(String title,String Fname,String Lname,String Address,String Status,
			String CIFNo,String Patename, String Email,String Pass,String ConfirmPass,
			String Question,String mobileno,String dob,String type,String PIN) {
		this._title = title;
		this._fname = Fname;
		this._lname = Lname;
		this._address = Address;
		this._status = Status;
		this._cifno = CIFNo;
		this._patename = Patename;
		this._email = Email;
		this._Pass = Pass;
		this._confirmPass = ConfirmPass;
		this._question = Question;
		this._mobileno = mobileno;
		this._dob = dob;
		this._type = type;
		this.PIN = PIN; 
	}

	public int getID() {
		return this._id;
	}

	public void SetID(int ID) {
		this._id = ID;
	}

	public String getTitle() {
		return this._title;
	}

	public void setTitle(String Title) {
		this._title = Title;
	}
	
	public String getFname() {
		return this._fname;
	}

	public void setFname(String fname) {
		this._fname = fname;
	}
		
	public String getLname() {
		return this._lname;
	}

	public void setLname(String lname) {
		this._lname = lname;
	}
	
	public String getAddress(){
		return this._address;
	}

	public void setAddress(String Address) {
		this._address = Address;
	}
	
	public String getStatus(){
		return this._status;
	}

	public void setStatus(String Status) {
		this._status = Status;
	}
	
	public String getCIFNo(){
		return this._cifno;
	}

	public void setCIFNo(String CIFNo) {
		this._cifno = CIFNo;
	}
	public String getPateName(){
		return this._patename;
	}

	public void setPateName(String PateName) {
		this._patename = PateName;
	}
	public String getEmail() {
		return this._email;
	}

	public void setEmail(String Email) {
		this._email = Email;
	}
	
	public String getPassword() {
		return this._Pass;
	}

	public void setPassword(String Pass) {
		this._Pass = Pass;
	}

	public String getConfirmPassword() {
		return this._confirmPass;
	}

	public void setConfirmPassword(String ConfirmPass) {
		this._confirmPass = ConfirmPass;
	}
	
	public String getQuestion() {
		return this._question;
	}

	public void setQuestion(String strQuestion) {
		this._question = strQuestion;
	}
	
	public String getMobileNo() {
		return this._mobileno;
	}

	public void setMobileNo(String strmobileno) {
		this._mobileno = strmobileno;
	}
	
	public String getDob() {
		return this._dob;
	}

	public void setDob(String strdob) {
		this._dob = strdob;
	}
	
	public String getType() {
		return this._type;
	}

	public void setType(String strtype) {
		this._type = strtype;
	}

	public int get_id() {
		return _id;
	}

	public String get_title() {
		return _title;
	}

	public String get_fname() {
		return _fname;
	}

	public String get_lname() {
		return _lname;
	}

	public String get_address() {
		return _address;
	}

	public String get_status() {
		return _status;
	}

	public String get_cifno() {
		return _cifno;
	}

	public String get_patename() {
		return _patename;
	}

	public String get_email() {
		return _email;
	}

	public String get_Pass() {
		return _Pass;
	}

	public String get_confirmPass() {
		return _confirmPass;
	}

	public String get_question() {
		return _question;
	}

	public String get_mobileno() {
		return _mobileno;
	}

	public String get_dob() {
		return _dob;
	}

	public String get_type() {
		return _type;
	}

	public String getPIN() {
		return PIN;
	}
}
