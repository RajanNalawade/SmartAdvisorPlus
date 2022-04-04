package sbilife.com.pointofsale_bancaagency.utility;

public class M_DocumentUploadStatus {
	
	
	public M_DocumentUploadStatus(String proofOf, String documentName,
			String fileName, String syncDate) {
		super();
		ProofOf = proofOf;
		DocumentName = documentName;
		FileName = fileName;
		SyncDate = syncDate;
	}
	private String ProofOf="";
	public String getProofOf() {
		return ProofOf;
	}
	public void setProofOf(String proofOf) {
		ProofOf = proofOf;
	}
	public String getDocumentName() {
		return DocumentName;
	}
	public void setDocumentName(String documentName) {
		DocumentName = documentName;
	}
	public String getFileName() {
		return FileName;
	}
	public void setFileName(String fileName) {
		FileName = fileName;
	}
	public String getSyncDate() {
		return SyncDate;
	}
	public void setSyncDate(String syncDate) {
		SyncDate = syncDate;
	}
	private String DocumentName="";
	private String FileName="";
	private String SyncDate="";

}
