package sbilife.com.pointofsale_bancaagency;

public class ChildEducationPlannerBean {

	private String prospectName="";
	private String prospectEmail="";
	private String prospectMobile="";
	private String productType="";
	
	
	public ChildEducationPlannerBean(String prospectName2,
			String prospectEmail2, String prospectMobile2, String productType2) {
		// TODO Auto-generated constructor stub
		prospectName=prospectName2;
		prospectMobile=prospectMobile2;
		prospectEmail=prospectEmail2;
		productType=productType2;
		
	}
	public String getProspectName() {
		return prospectName;
	}

	public String getProspectEmail() {
		return prospectEmail;
	}
	public void setProspectEmail(String prospectEmail) {
		this.prospectEmail = prospectEmail;
	}
	public String getProspectMobile() {
		return prospectMobile;
	}
	public void setProspectMobile(String prospectMobile) {
		this.prospectMobile = prospectMobile;
	}
	public String getProductType() {
		return productType;
	}
	public void setProductType(String productType) {
		this.productType = productType;
	}
	
}
