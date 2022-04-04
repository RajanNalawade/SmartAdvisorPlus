package sbilife.com.pointofsale_bancaagency.ekyc.ekycRequest;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name="ECSRDKycRequest")
public class ECSRDKYCRequest {
	
    public String getAadhaarNumber() {
		return aadhaarNumber;
	}

	public void setAadhaarNumber(String aadhaarNumber) {
		this.aadhaarNumber = aadhaarNumber;
	}

	public String getTid() {
		return tid;
	}

	public void setTid(String tid) {
		this.tid = tid;
	}

	public boolean isUsesOtp() {
		return usesOtp;
	}

	public void setUsesOtp(boolean usesOtp) {
		this.usesOtp = usesOtp;
	}

	public boolean isUsesBio() {
		return usesBio;
	}

	public void setUsesBio(boolean usesBio) {
		this.usesBio = usesBio;
	}

	public String getUsesBt() {
		return usesBt;
	}

	public void setUsesBt(String usesBt) {
		this.usesBt = usesBt;
	}

	/*public String getUdc() {
		return udc;
	}

	public void setUdc(String udc) {
		this.udc = udc;
	}*/

	public String getPfr() {
		return pfr;
	}

	public void setPfr(String pfr) {
		this.pfr = pfr;
	}

	public String getLr() {
		return lr;
	}

	public void setLr(String lr) {
		this.lr = lr;
	}

	public String getPidData() {
		return pidData;
	}

	public void setPidData(String pidData) {
		this.pidData = pidData;
	}

	@Attribute(name="aadhaarNumber", required=true)
    protected String aadhaarNumber;

    @Attribute(name="tid", required=true)
    protected String  tid;

    @Attribute(name="usesOtp", required=true)
    protected boolean  usesOtp;
    
    @Attribute(name="usesBio", required=true)
    protected boolean usesBio;
    
    @Attribute(name="usesBt", required=false)
    protected String  usesBt;
    
  /*  @Attribute(name="udc", required=true)
    protected String  udc;*/
    
    @Attribute(name="pfr", required=true)
	protected String  pfr;

	@Attribute(name="lr", required=true)
	protected String  lr;

    @Element(name="PidData", required=true)
    protected String  pidData;
	

    
    
    
    
}
