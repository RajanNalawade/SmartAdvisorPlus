package sbilife.com.pointofsale_bancaagency.ekyc.ekycRequest;


import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name="ECSOtpKycRequest")
public class ECSOtpKycRequest {
	

	@Attribute(name="aadhaarNumber", required=true)
	private String aadhaarNumber;
	
    @Attribute(name="pfr", required = true)
    private boolean pfr;

    @Attribute(name="lr",required=true)
    private boolean lr;

    @Attribute(name="otpReqTxnId", required = true)
    private String otpReqTxnId;

  /*  @Attribute(name="udc", required = true)
    private String udc;*/

    @Element(name="otp", required = true)
    private String otp;

	public String getAadhaarNumber() {
		return aadhaarNumber;
	}

	public void setAadhaarNumber(String aadhaarNumber) {
		this.aadhaarNumber = aadhaarNumber;
	}

	public boolean isPfr() {
		return pfr;
	}

	public void setPfr(boolean pfr) {
		this.pfr = pfr;
	}

	public boolean isLr() {
		return lr;
	}

	public void setLr(boolean lr) {
		this.lr = lr;
	}

	public String getOtpReqTxnId() {
		return otpReqTxnId;
	}

	public void setOtpReqTxnId(String otpReqTxnId) {
		this.otpReqTxnId = otpReqTxnId;
	}

	/*public String getUdc() {
		return udc;
	}

	public void setUdc(String udc) {
		this.udc = udc;
	}*/

	public String getOtp() {
		return otp;
	}

	public void setOtp(String otp) {
		this.otp = otp;
	}


}
