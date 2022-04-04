package sbilife.com.pointofsale_bancaagency;

/*
 * while adding information in login it is hold by this class.
 */

class clsAgentProfile {

	private int _id;
	private String _name;
	private String _licenceno;
	private String _licenceissuedate;
	private String _licenceexpirydate;
	private String _activationstartdate;
	private String _branchname;
	private String _region;
	private String _umname;
	private String _status;
	private String _ulipcertified;
	

	public clsAgentProfile(String name,String licno,String issuedate,String expdate,String actdate,String brname,String region, String um,String status,String ulipcer) {
		this._name = name;
		this._licenceno = licno;
		this._licenceissuedate = issuedate;
		this._licenceexpirydate = expdate;
		this._activationstartdate = actdate;
		this._branchname = brname;
		this._region = region;
		this._umname = um;
		this._status = status;
		this._ulipcertified = ulipcer;		
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
	
	public String getLicenceNo() {
		return this._licenceno;
	}

	public void setLicenceNo(String licno) {
		this._licenceno = licno;
	}
		
	public String getLicenceIssuDate() {
		return this._licenceissuedate;
	}

	public void setLicenceIssuDate(String licissuedate) {
		this._licenceissuedate = licissuedate;
	}
	
	public String getLicenceExpDate(){
		return this._licenceexpirydate;
	}

	public void setLicenceExpDate(String licexpdate) {
		this._licenceexpirydate = licexpdate;
	}
	
	public String getActivationStartDate(){
		return this._activationstartdate;
	}

	public void setActivationStartDate(String actstartdate) {
		this._activationstartdate = actstartdate;
	}
	
	public String getBranchName(){
		return this._branchname;
	}

	public void setBranchName(String brname) {
		this._branchname = brname;
	}
	public String getRegion(){
		return this._region;
	}

	public void setRegion(String Region) {
		this._region = Region;
	}
	public String getUMName() {
		return this._umname;
	}

	public void setUMName(String um) {
		this._umname = um;
	}
	
	public String getStatus() {
		return this._status;
	}

	public void setStatus(String status) {
		this._status = status;
	}

	public String getULIPCertified() {
		return this._ulipcertified;
	}

	public void setULIPCertified(String ulipcert) {
		this._ulipcertified = ulipcert;
	}	
}
