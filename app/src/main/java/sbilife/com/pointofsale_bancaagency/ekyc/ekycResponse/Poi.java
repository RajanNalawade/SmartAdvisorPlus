package sbilife.com.pointofsale_bancaagency.ekyc.ekycResponse;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.io.Serializable;

@Root(name="Poi")
public class Poi  implements Serializable {

	@Attribute(name = "aadhaar")
    protected String aadhaarNumber;
	@Attribute(name = "name")
    protected String name;
    @Attribute(name = "dob")
    protected String dob;
    @Attribute(name = "gender")
    protected String gender;
    @Attribute(name = "phone", required=false)
    protected String phone;
    @Attribute(name = "email",required=false)
    protected String email;
    @Element(name = "photo",required=false)
    protected String photo;
    
	@Attribute(name = "uidtoken")
	protected String uidtoken;

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
	public String getPhoto() {
		return photo;
	}
	public void setPhoto(String photo) {
		this.photo = photo;
	}
	public String getAadhaarNumber() {
		return aadhaarNumber;
	}
	public void setAadhaarNumber(String aadhaarNumber) {
		this.aadhaarNumber = aadhaarNumber;
	}
	public String getUidtoken() {
		return uidtoken;
	}
	public void setUidtoken(String uidtoken) {
		this.uidtoken = uidtoken;
	}







}