package sbilife.com.pointofsale_bancaagency.ekyc.offline_ekyc;

import com.ecs.rdlibrary.response.Poa;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.io.Serializable;

/**
 * Created by Connectlife on 5/26/2019.
 */
@Root(name = "UidData")
public class UidData implements Serializable {

    @Element(name = "Poi", required = true)
    protected offline_Poi poi;

    @Element(name = "Poa", required = true)
    protected offline_Poa poa;

    public String getPht() {
        return Pht;
    }

    public void setPht(String pht) {
        Pht = pht;
    }

    @Element(name = "Pht", required = true)
    protected String Pht;
    public offline_Poi getPoi() {
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
    }


}

