package sbilife.com.pointofsale_bancaagency.ekyc.ekycResponse;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

@Root(name="ECSAuthResponse")
public class ECSAuthResponse {

    @Attribute(name = "err", required=true)
    protected boolean err;

    @Attribute(name = "errCode", required=false)
    protected String errCode;

    @Attribute(name = "errMsg", required=false)
    protected String errMsg;

    @Attribute(name = "code", required=false)
    protected String code;

    @Attribute(name = "txn", required=false)
    protected String txn;

    public boolean getErr() {
        return err;
    }

    public void setErr(boolean err) {
        this.err = err;
    }

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTxn() {
        return txn;
    }

    public void setTxn(String txn) {
        this.txn = txn;
    }
}

