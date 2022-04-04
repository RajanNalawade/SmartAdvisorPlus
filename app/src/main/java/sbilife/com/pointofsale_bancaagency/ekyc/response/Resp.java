package sbilife.com.pointofsale_bancaagency.ekyc.response;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

@Root(name="Resp")
public class Resp {

    @Attribute(name = "errCode", required = false)
    private String errCode;

    @Attribute(name = "errInfo", required = false)
    private String errInfo;

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

    @Attribute(name = "nmPoints", required = false)
    private String nmPoints;

    @Attribute(name = "qScore", required = false)
    private String qScore;

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

    public String getErrInfo() {
        return errInfo;
    }

    public void setErrInfo(String errInfo) {
        this.errInfo = errInfo;
    }

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

    public String getNmPoints() {
        return nmPoints;
    }

    public void setNmPoints(String nmPoints) {
        this.nmPoints = nmPoints;
    }

    public String getqScore() {
        return qScore;
    }

    public void setqScore(String qScore) {
        this.qScore = qScore;
    }
}
