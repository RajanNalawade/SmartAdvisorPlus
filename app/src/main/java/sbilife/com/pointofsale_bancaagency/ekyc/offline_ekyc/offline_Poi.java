package sbilife.com.pointofsale_bancaagency.ekyc.offline_ekyc;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

import java.io.Serializable;

@Root(name="Poi")
public class offline_Poi implements Serializable {


	@Attribute(name = "name")
    protected String name;
    @Attribute(name = "dob")
    protected String dob;
    @Attribute(name = "gender")
    protected String gender;
    @Attribute(name = "m", required=false)
    protected String phone;
    @Attribute(name = "e",required=false)
    protected String email;

    
    public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDob() {
		return dob;
	}
	public void setDob(String dob) {
		this.dob = dob;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}






}