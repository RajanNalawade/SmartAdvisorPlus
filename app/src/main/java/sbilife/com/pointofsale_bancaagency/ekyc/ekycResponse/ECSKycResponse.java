package sbilife.com.pointofsale_bancaagency.ekyc.ekycResponse;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.io.Serializable;

@Root(name="ECSKycResponse")
public class ECSKycResponse implements Serializable {

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

	@Element(name = "Poi", required=false)
    protected Poi poi;

	@Element(name = "Poa", required=false)
    protected Poa poa;

	@Element(name = "RegionalData", required=false)
    protected RegionalData regionalData;

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

	public Poi getPoi() {
		return poi;
	}

	public void setPoi(Poi poi) {
		this.poi = poi;
	}

	public Poa getPoa() {
		return poa;
	}

	public void setPoa(Poa poa) {
		this.poa = poa;
	}

	public RegionalData getRegionalData() {
		return regionalData;
	}

	public void setRegionalData(RegionalData regionalData) {
		this.regionalData = regionalData;
	}
}

