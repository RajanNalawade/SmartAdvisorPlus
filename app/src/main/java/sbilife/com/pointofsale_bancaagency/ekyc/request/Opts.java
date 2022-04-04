package sbilife.com.pointofsale_bancaagency.ekyc.request;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

@Root(name = "Opts")
public class Opts {

    @Attribute(name = "fCount", required = false)
    private String fCount;

    @Attribute(name = "fType", required = false)
    private String fType;

    @Attribute(name = "iCount", required = false)
    private String iCount;

    @Attribute(name = "iType", required = false)
    private String iType;

    @Attribute(name = "pCount", required = false)
    private String pCount;

    @Attribute(name = "pType", required = false)
    private String pType;

    @Attribute(name = "format", required = false)
    private String format;

    @Attribute(name = "pidVer", required = false)
    private String pidVer;

    @Attribute(name = "timeout", required = false)
    private String timeout;

    @Attribute(name = "otp", required = false)
    private String otp;

    @Attribute(name = "wadh", required = false)
    private String wadh;

    @Attribute(name = "posh", required = false)
    private String posh;

    @Attribute(name = "env", required = false)
    private String env; // S == Stage, P == Prod, PP == PreProd

    public String getfCount() {
        return fCount;
    }

    public void setfCount(String fCount) {
        this.fCount = fCount;
    }

    public String getfType() {
        return fType;
    }

    public void setfType(String fType) {
        this.fType = fType;
    }

    public String getiCount() {
        return iCount;
    }

    public void setiCount(String iCount) {
        this.iCount = iCount;
    }

    public String getiType() {
        return iType;
    }

    public void setiType(String iType) {
        this.iType = iType;
    }

    public String getpCount() {
        return pCount;
    }

    public void setpCount(String pCount) {
        this.pCount = pCount;
    }

    public String getpType() {
        return pType;
    }

    public void setpType(String pType) {
        this.pType = pType;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getPidVer() {
        return pidVer;
    }

    public void setPidVer(String pidVer) {
        this.pidVer = pidVer;
    }

    public String getTimeout() {
        return timeout;
    }

    public void setTimeout(String timeout) {
        this.timeout = timeout;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getWadh() {
        return wadh;
    }

    public void setWadh(String wadh) {
        this.wadh = wadh;
    }

    public String getPosh() {
        return posh;
    }

    public void setPosh(String posh) {
        this.posh = posh;
    }

    public String getEnv() {
        return env;
    }

    public void setEnv(String env) {
        this.env = env;
    }
}
