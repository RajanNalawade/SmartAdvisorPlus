package sbilife.com.pointofsale_bancaagency.ekyc.offline_ekyc;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.io.Serializable;

@Root(name="OfflinePaperlessKyc")
public class OfflinePaperlessKycResponse implements Serializable {

    public String getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }

    public UidData getUidData() {
        return uidData;
    }

    public void setUidData(UidData uidData) {
        this.uidData = uidData;
    }

   /* public Signature getSignature() {
        return Signature;
    }

    public void setSignature(Signature signature) {
        Signature = signature;
    }
*/

   /* public offline_Poi getPoi() {
        return poi;
    }
    public void setPoi(offline_Poi poi) {
        this.poi = poi;
    }
    public offline_Poa getPoa() {
        return poa;
    }
    public void setPoa(Poa offline_Poa) {
        this.poa = poa;
    }*/
    @Attribute(name = "referenceId", required=true)
    protected String referenceId;


    @Element(name = "UidData", required=true)
    protected UidData uidData;


   /* @Element(name = "Signature", required=true)
    protected Signature Signature;*/
   /* @Element(name = "Poi", required = true)
    protected offline_Poi poi;

    @Element(name = "Poa", required = true)
    protected offline_Poa poa;*/
    @Attribute(name = "Pht", required = false)
    protected String Pht;

}

