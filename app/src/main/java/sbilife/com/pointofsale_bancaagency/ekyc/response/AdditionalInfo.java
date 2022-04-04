package sbilife.com.pointofsale_bancaagency.ekyc.response;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

import sbilife.com.pointofsale_bancaagency.ekyc.request.Param;

/**
 * Created by vinoth on 18/07/2017.
 */

@Root(name = "additional_info")
public class AdditionalInfo {

    @ElementList(name = "Param", required = false, inline = true)
    private List<Param> params;

    public List<Param> getParams() {
        return params;
    }

    public void setParams(List<Param> params) {
        this.params = params;
    }
}
