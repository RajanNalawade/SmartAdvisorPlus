package sbilife.com.pointofsale_bancaagency.ekyc.response;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "DeviceInfo")
public class DeviceInfo {

    @Attribute(name = "srno", required = false)
    private String srno;

    @Attribute(name = "dpId", required = false)
    private String dpId;

    @Attribute(name = "rdsId", required = false)
    private String rdsId;

    @Attribute(name = "rdsVer", required = false)
    private String rdsVer;

    @Attribute(name = "dc", required = false)
    private String dc;

    @Attribute(name = "mi", required = false)
    private String mi;

    @Attribute(name = "mc", required = false)
    private String mc;

    @Element(name = "additional_info", required = false)
    private AdditionalInfo additionalInfo;

    public String getSrno() {
        return srno;
    }

    public void setSrno(String srno) {
        this.srno = srno;
    }

    public String getDpId() {
        return dpId;
    }

    public void setDpId(String dpId) {
        this.dpId = dpId;
    }

    public String getRdsId() {
        return rdsId;
    }

    public void setRdsId(String rdsId) {
        this.rdsId = rdsId;
    }

    public String getRdsVer() {
        return rdsVer;
    }

    public void setRdsVer(String rdsVer) {
        this.rdsVer = rdsVer;
    }

    public String getDc() {
        return dc;
    }

    public void setDc(String dc) {
        this.dc = dc;
    }

    public String getMi() {
        return mi;
    }

    public void setMi(String mi) {
        this.mi = mi;
    }

    public String getMc() {
        return mc;
    }

    public void setMc(String mc) {
        this.mc = mc;
    }

    public AdditionalInfo getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(AdditionalInfo additionalInfo) {
        this.additionalInfo = additionalInfo;
    }
}
