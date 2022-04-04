package sbilife.com.pointofsale_bancaagency.cifenrollment.phase1;

public class M_Sync_Status {

	private String quotation_no = "", SyncStatus = "";

	public M_Sync_Status(String quotation_no, String syncStatus) {
		super();
		this.quotation_no = quotation_no;
		this.SyncStatus = syncStatus;
	}

	public M_Sync_Status(String syncStatus) {
		super();
		this.SyncStatus = syncStatus;
	}

	public String getQuotation_no() {
		return quotation_no;
	}

	public void setQuotation_no(String quotation_no) {
		this.quotation_no = quotation_no;
	}

	public String getSyncStatus() {
		return SyncStatus;
	}

	public void setSyncStatus(String syncStatus) {
		SyncStatus = syncStatus;
	}

}
