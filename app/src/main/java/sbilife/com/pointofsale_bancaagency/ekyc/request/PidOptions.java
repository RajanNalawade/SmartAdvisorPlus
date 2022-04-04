package sbilife.com.pointofsale_bancaagency.ekyc.request;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "PidOptions", strict = false)
public class PidOptions {

    @Attribute(name = "ver", required = true)
    private String ver;

    @Element(name = "Opts", required = true)
    private Opts opts;

    @Element(name = "CustOpts", required = true)
    private CustOpts custOpts;

    @Element(name = "Demo", required = true)
    private Demo demo;

    public String getVer() {
        return ver;
    }

    public void setVer(String ver) {
        this.ver = ver;
    }

    public Opts getOpts() {
        return opts;
    }

    public void setOpts(Opts opts) {
        this.opts = opts;
    }

    public CustOpts getCustOpts() {
        return custOpts;
    }

    public void setCustOpts(CustOpts custOpts) {
        this.custOpts = custOpts;
    }

    public Demo getDemo() {
        return demo;
    }

    public void setDemo(Demo demo) {
        this.demo = demo;
    }
}

