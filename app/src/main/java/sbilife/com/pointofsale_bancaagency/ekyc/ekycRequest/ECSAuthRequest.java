package sbilife.com.pointofsale_bancaagency.ekyc.ekycRequest;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

@Root(name="ECSAuthRequest")
public class ECSAuthRequest {

    @Attribute(name="aadhaarNumber", required=true)
    protected String aadhaarNumber;

    @Attribute(name="subType", required=true) // OTP, IIR, FMR
    protected String subType;

    @Attribute(name="fdc", required=true)
    protected String fdc;

    @Attribute(name="idc", required=true)
    protected String idc;

    @Attribute(name="ci", required=true)
    protected String ci;

    @Attribute(name="key", required=true)
    protected String key;

    @Attribute(name="hmac", required=true)
    protected String  hmac;

    @Attribute(name="pid", required=true)
    protected String  pid;

    @Attribute(name="pidTs", required=true)
    protected String pidTs;

    @Attribute(name="terminalId", required=true)
    protected String  terminalId;

    @Attribute(name="udc", required=false)
    protected String  udc;

    public String getAadhaarNumber() {
        return aadhaarNumber;
    }

    public void setAadhaarNumber(String aadhaarNumber) {
        this.aadhaarNumber = aadhaarNumber;
    }

    public String getSubType() {
        return subType;
    }

    public void setSubType(String subType) {
        this.subType = subType;
    }

    public String getFdc() {
        return fdc;
    }

    public void setFdc(String fdc) {
        this.fdc = fdc;
    }

    public String getIdc() {
        return idc;
    }

    public void setIdc(String idc) {
        this.idc = idc;
    }

    public String getCi() {
        return ci;
    }

    public void setCi(String ci) {
        this.ci = ci;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getHmac() {
        return hmac;
    }

    public void setHmac(String hmac) {
        this.hmac = hmac;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getPidTs() {
        return pidTs;
    }

    public void setPidTs(String pidTs) {
        this.pidTs = pidTs;
    }

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }


    public String getUdc() {
        return udc;
    }

    public void setUdc(String udc) {
        this.udc = udc;
    }
}
