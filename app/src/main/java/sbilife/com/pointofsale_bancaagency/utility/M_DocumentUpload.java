package sbilife.com.pointofsale_bancaagency.utility;

public class M_DocumentUpload {

	public M_DocumentUpload(String proof_Of, String document_Name,
			String document_Content, String document_UploadDate,
			String fileName, String createdBy, String createdDate,
			String modifiedBy, String modifiedDate, Boolean isSync,
			Boolean isFlag1, Boolean isFlag2, Boolean isFlag3, Boolean isFlag4) {
		super();
		this.proof_Of = proof_Of;
		this.document_Name = document_Name;
		this.document_Content = document_Content;
		this.document_UploadDate = document_UploadDate;
		this.FileName = fileName;
		this.createdBy = createdBy;
		this.createdDate = createdDate;
		this.modifiedBy = modifiedBy;
		this.modifiedDate = modifiedDate;
		this.isSync = isSync;
		this.isFlag1 = isFlag1;
		this.isFlag2 = isFlag2;
		this.isFlag3 = isFlag3;
		this.isFlag4 = isFlag4;
	}

	private String proof_Of = "";

	public String getProof_Of() {
		return proof_Of;
	}

	public void setProof_Of(String proof_Of) {
		this.proof_Of = proof_Of;
	}

	public String getDocument_Name() {
		return document_Name;
	}

	public void setDocument_Name(String document_Name) {
		this.document_Name = document_Name;
	}

	public String getDocument_Content() {
		return document_Content;
	}

	public void setDocument_Content(String document_Content) {
		this.document_Content = document_Content;
	}

	public String getDocument_UploadDate() {
		return document_UploadDate;
	}

	public void setDocument_UploadDate(String document_UploadDate) {
		this.document_UploadDate = document_UploadDate;
	}

	public String getFileName() {
		return FileName;
	}

	public void setFileName(String fileName) {
		FileName = fileName;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public String getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(String modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public Boolean getIsSync() {
		return isSync;
	}

	public void setIsSync(Boolean isSync) {
		this.isSync = isSync;
	}

	public Boolean getIsFlag1() {
		return isFlag1;
	}

	public void setIsFlag1(Boolean isFlag1) {
		this.isFlag1 = isFlag1;
	}

	public Boolean getIsFlag2() {
		return isFlag2;
	}

	public void setIsFlag2(Boolean isFlag2) {
		this.isFlag2 = isFlag2;
	}

	public Boolean getIsFlag3() {
		return isFlag3;
	}

	public void setIsFlag3(Boolean isFlag3) {
		this.isFlag3 = isFlag3;
	}

	public Boolean getIsFlag4() {
		return isFlag4;
	}

	public void setIsFlag4(Boolean isFlag4) {
		this.isFlag4 = isFlag4;
	}

	private String document_Name = "";
	private String document_Content = "";
	private String document_UploadDate = "";
	private String FileName = "";
	private String createdBy = "";
	private String createdDate = "";
	private String modifiedBy = "";
	private String modifiedDate = "";

	public Boolean isSync;
	public Boolean isFlag1;
	public Boolean isFlag2;
	public Boolean isFlag3;
	public Boolean isFlag4;

}
