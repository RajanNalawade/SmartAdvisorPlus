package sbilife.com.pointofsale_bancaagency;

/*
 * while registering product that time check version service fire and it
 * is hold that information.
 */

class clsCheckVersion {

	private int _id;
	private String _version;
	private String _releasedate;

	public clsCheckVersion(String version,String releasedate) {
		this._version = version;
		this._releasedate = releasedate;		
		
	}

	public int getID() {
		return this._id;
	}

	public void SetID(int ID) {
		this._id = ID;
	}

	public String getVersion() {
		return this._version;
	}

	public void setVersion(String version) {
		this._version = version;
	}
	
	public String getReleasedDate() {
		return this._releasedate;
	}

	public void setReleasedDate(String ReleasedDate) {
		this._releasedate = ReleasedDate;
	}			
}
