package sbilife.com.pointofsale_bancaagency.ekyc.request;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

@Root(name = "Param")
public class Param {
    @Attribute(name = "name", required = false)
    private String name;

    @Attribute(name = "value", required = false)
    private String value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
