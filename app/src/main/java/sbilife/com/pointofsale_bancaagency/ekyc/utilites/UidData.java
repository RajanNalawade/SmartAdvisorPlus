package sbilife.com.pointofsale_bancaagency.ekyc.utilites;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class UidData {
	String ts;
	String uid;
	String name = null;
	String dob = null;
	String gender = null;
	String co = null;
	String house = null;
	String street = null;
	String po = null;
	String vtc = null;
	String subdist = null;
	String dist = null;
	String state = null;
	String pc = null;
	String phone = null;
	String email = null;
	String loc = null;
	String lm = null;
	Bitmap photo;
	private static final String TAG = "UidData";

	public UidData(String uidDataXML) {
		DocumentBuilder db;
		try {
			
			db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document kycResDoc = db.parse(new ByteArrayInputStream(uidDataXML
					.getBytes(StandardCharsets.UTF_8)));
			NamedNodeMap kycResAttrs = kycResDoc.getDocumentElement()
					.getAttributes();
			this.ts = kycResAttrs.getNamedItem("ts").getNodeValue();
			Node uidDataNode = kycResDoc.getElementsByTagName("UidData")
					.item(0);
			if (uidDataNode == null) {
				return;
			}
			Node uidAttr = uidDataNode.getAttributes().item(0);
			this.uid = uidAttr.getNodeValue();
			NodeList uidDataNodes = uidDataNode.getChildNodes();
			for (int i = 0; i < uidDataNodes.getLength(); i++) {
				Node n = uidDataNodes.item(i);
				if (n.getNodeName().equalsIgnoreCase("poi")) {
					NamedNodeMap poiAttrs = n.getAttributes();
					this.name = poiAttrs.getNamedItem("name").getNodeValue();
					this.dob = poiAttrs.getNamedItem("dob").getNodeValue();
					this.gender = poiAttrs.getNamedItem("gender")
							.getNodeValue();
					// ADD PHONE AND EMAIL PARSE CODE HERE
				}
				if (n.getNodeName().equalsIgnoreCase("poa")) {
					NamedNodeMap poiAttrs = n.getAttributes();
					try {
						this.dist = poiAttrs.getNamedItem("dist")
								.getNodeValue();
					} catch (Exception e) {
						e.printStackTrace();
					}
					try {
						this.state = poiAttrs.getNamedItem("state")
								.getNodeValue();
					} catch (Exception e) {
						e.printStackTrace();
					}
					try {
						this.pc = poiAttrs.getNamedItem("pc").getNodeValue();
					} catch (Exception e) {
						e.printStackTrace();
					}
					try {
						this.co = poiAttrs.getNamedItem("co").getNodeValue();
					} catch (Exception e) {
						e.printStackTrace();
					}
					try {
						this.house = poiAttrs.getNamedItem("house")
								.getNodeValue();
					} catch (Exception e) {
						e.printStackTrace();
					}
					try {
						this.street = poiAttrs.getNamedItem("street")
								.getNodeValue();
					} catch (Exception e) {
						e.printStackTrace();
					}
					try {
						this.vtc = poiAttrs.getNamedItem("vtc").getNodeValue();
					} catch (Exception e) {
						e.printStackTrace();
					}
					try {
						this.subdist = poiAttrs.getNamedItem("subdist")
								.getNodeValue();
					} catch (Exception e) {
						e.printStackTrace();
					}
					try {
						this.po = poiAttrs.getNamedItem("po").getNodeValue();
					} catch (Exception e) {
						e.printStackTrace();
					}
					try {
						this.email = poiAttrs.getNamedItem("email")
								.getNodeValue();
					} catch (Exception e) {
						e.printStackTrace();
					}

					try {
						this.phone = poiAttrs.getNamedItem("phone")
								.getNodeValue();
					} catch (Exception e) {
						e.printStackTrace();
					}
					try {
						this.loc = poiAttrs.getNamedItem("loc").getNodeValue();
					} catch (Exception e) {
						e.printStackTrace();
					}

					try {
						this.lm = poiAttrs.getNamedItem("lm").getNodeValue();
					} catch (Exception e) {
						e.printStackTrace();
					}

					// ADD PHONE AND EMAIL PARSE CODE HERE
				}
				if (n.getNodeName().equalsIgnoreCase("pht")) {
					byte[] imgBytes = Base64.decode(n.getTextContent(),
							Base64.DEFAULT);
					this.photo = BitmapFactory.decodeByteArray(imgBytes, 0,
							imgBytes.length);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public UidData(JSONObject jKycRes) {
		try {
			this.uid = jKycRes.getString("aadhaar-id");
			JSONObject info = jKycRes.getJSONObject("kyc");
			if (info != null) {
				JSONObject poi = info.getJSONObject("poi");
				if (poi != null) {
					this.name = poi.getString("name");
					this.dob = poi.getString("dob");
					this.gender = poi.getString("gender");
				}
				JSONObject poa = info.getJSONObject("poa");
				if (poa != null) {
					this.dist = poa.getString("dist");
					this.state = poa.getString("state");
					this.pc = poa.getString("pc");
				}
				String b64Pht = info.getString("photo");
				if (b64Pht != null) {
					byte[] imgBytes = Base64.decode(b64Pht, Base64.DEFAULT);
					this.photo = BitmapFactory.decodeByteArray(imgBytes, 0,
							imgBytes.length);
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*
	 * public UidData(ResponseData kycResData) { this.ts = kycResData.getTs();
	 * this.uid = kycResData.getUid(); this.name = kycResData.getName();
	 * this.dob = kycResData.getDob(); this.gender = kycResData.getGender();
	 * this.dist = kycResData.getDist(); this.state = kycResData.getState();
	 * this.pc = kycResData.getPc(); byte [] imgBytes =
	 * kycResData.getPhoto().toByteArray(); this.photo =
	 * BitmapFactory.decodeByteArray(imgBytes,0,imgBytes.length); }
	 */
	public String getTs() {
		return ts;
	}

	public String getUid() {
		return uid;
	}

	public String getName() {
		return name;
	}

	public String getDob() {
		return dob;
	}

	public String getPhone() {
		return phone;
	}

	public String getEmail() {
		return email;
	}

	public String getGender() {
		return gender;
	}

	public String getCo() {
		return co;
	}

	public String getHouse() {
		return house;
	}

	public String getStreet() {
		return street;
	}

	public String getPo() {
		return po;
	}

	public String getVtc() {
		return vtc;
	}

	public String getSubdist() {
		return subdist;
	}

	public String getDist() {
		return dist;
	}

	public String getState() {
		return state;
	}

	public String getPc() {
		return pc;
	}

	public Bitmap getPhoto() {
		return photo;
	}

	public String getloc() {
		return loc;
	}

	public String getlm() {
		return lm;
	}
}
