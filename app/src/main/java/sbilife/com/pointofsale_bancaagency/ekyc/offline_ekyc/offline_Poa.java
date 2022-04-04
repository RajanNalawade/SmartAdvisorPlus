package sbilife.com.pointofsale_bancaagency.ekyc.offline_ekyc;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

import java.io.Serializable;

@Root(name="Poa")
public class offline_Poa implements Serializable{

	@Attribute(name = "country", required=false)
	protected String country;
	@Attribute(name = "careof", required=false)
    protected String co;
    @Attribute(name = "house", required=false)
    protected String house;
    @Attribute(name = "street", required=false)
    protected String street;
    @Attribute(name = "lm", required=false)
    protected String lm;
    @Attribute(name = "loc", required=false)
    protected String loc;
    @Attribute(name = "vtc", required=false)
    protected String vtc;
    @Attribute(name = "subdist", required=false)
    protected String subdist;
    @Attribute(name = "dist", required=false)
    protected String dist;
    @Attribute(name = "state", required=false)
    protected String state;
    @Attribute(name = "pc", required=false)
    protected String pc;
    @Attribute(name = "po", required=false)
    protected String po;
	public String getCo() {
		return co;
	}
	public void setCo(String co) {
		this.co = co;
	}
	public String getHouse() {
		return house;
	}
	public void setHouse(String house) {
		this.house = house;
	}
	public String getStreet() {
		return street;
	}
	public void setStreet(String street) {
		this.street = street;
	}
	public String getLm() {
		return lm;
	}
	public void setLm(String lm) {
		this.lm = lm;
	}
	public String getLoc() {
		return loc;
	}
	public void setLoc(String loc) {
		this.loc = loc;
	}
	public String getVtc() {
		return vtc;
	}
	public void setVtc(String vtc) {
		this.vtc = vtc;
	}
	public String getSubdist() {
		return subdist;
	}
	public void setSubdist(String subdist) {
		this.subdist = subdist;
	}
	public String getDist() {
		return dist;
	}
	public void setDist(String dist) {
		this.dist = dist;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getPc() {
		return pc;
	}
	public void setPc(String pc) {
		this.pc = pc;
	}
	public String getPo() {
		return po;
	}
	public void setPo(String po) {
		this.po = po;
	}

	public String getcountry() {
		return country;
	}
	public void setcountry(String country) {
		this.country = country;
	}
}

