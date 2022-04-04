package sbilife.com.pointofsale_bancaagency.ekyc.response;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "PidData")
public class PidData {
    @Element(name = "Resp", required = false)
    private Resp resp;

    @Element(name = "DeviceInfo", required = false)
    private DeviceInfo deviceInfo;

    @Element(name = "Hmac", required = false)
    private String hmac;

    public Resp getResp() {
        return resp;
    }

    public void setResp(Resp resp) {
        this.resp = resp;
    }

    public DeviceInfo getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(DeviceInfo deviceInfo) {
        this.deviceInfo = deviceInfo;
    }

    public String getHmac() {
        return hmac;
    }

    public void setHmac(String hmac) {
        this.hmac = hmac;
    }
}
