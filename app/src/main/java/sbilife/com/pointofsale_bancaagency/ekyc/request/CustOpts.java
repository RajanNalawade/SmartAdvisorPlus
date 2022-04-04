package sbilife.com.pointofsale_bancaagency.ekyc.request;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

@Root(name = "CustOpts")
public class CustOpts {

    @ElementList(name = "Param", required = false, inline = true)
    private List<Param> params;

    public List<Param> getParams() {
        return params;
    }

    public void setParams(List<Param> params) {
        this.params = params;
    }
}
