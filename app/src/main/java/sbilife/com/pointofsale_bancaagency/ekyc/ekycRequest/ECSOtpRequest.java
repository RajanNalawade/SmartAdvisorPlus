package sbilife.com.pointofsale_bancaagency.ekyc.ekycRequest;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name="ECSOtpRequest")
public class ECSOtpRequest {

	@Attribute(name="aadhaarNumber", required=true)
	protected String aadhaarNumber;
	
    @Element(name="Sms", required = true)
    private boolean sms;

    @Element(name="Email", required = true)
    private boolean email;

	public String getAadhaarNumber() {
		return aadhaarNumber;
	}

	public void setAadhaarNumber(String aadhaarNumber) {
		this.aadhaarNumber = aadhaarNumber;
	}

	public boolean isSms() {
		return sms;
	}

	public void setSms(boolean sms) {
		this.sms = sms;
	}

	public boolean isEmail() {
		return email;
	}

	public void setEmail(boolean email) {
		this.email = email;
	}
}
