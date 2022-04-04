package sbilife.com.pointofsale_bancaagency.posp_ra;

import android.os.Parcel;
import android.os.Parcelable;

public class POJO_POSP_RA_Rejection implements Parcelable {

    private int primaryID = -1, posp_table_id = -1;

    private String strPan = "", //PAN
            strIACode = "", //IACODE
            strDocStatus = "", //DOCUMENT_STATUS
            strReqStatus = "", //REQUIREMENT_STATUS
            strReqRaised = "", //REQUIREMENT_RAISED
            strDocName = "", //DOCUMENT_NAME
            strRemarks = "", //REMARKS
            strDocOptionalVal = "", //DOCUMENT_OPTION_VALUE
            strUMCode = ""; //UM Code

    public POJO_POSP_RA_Rejection(int primaryID, int posp_table_id, String strPan, String strIACode,
                                  String strDocStatus, String strReqStatus, String strReqRaised, String strDocName,
                                  String strRemarks, String strDocOptionalVal, String strUMCode) {
        this.primaryID = primaryID;
        this.posp_table_id = posp_table_id;
        this.strPan = strPan;
        this.strIACode = strIACode;
        this.strDocStatus = strDocStatus;
        this.strReqStatus = strReqStatus;
        this.strReqRaised = strReqRaised;
        this.strDocName = strDocName;
        this.strRemarks = strRemarks;
        this.strDocOptionalVal = strDocOptionalVal;
        this.strUMCode = strUMCode;
    }

    protected POJO_POSP_RA_Rejection(Parcel in) {
        primaryID = in.readInt();
        posp_table_id = in.readInt();
        strPan = in.readString();
        strIACode = in.readString();
        strDocStatus = in.readString();
        strReqStatus = in.readString();
        strReqRaised = in.readString();
        strDocName = in.readString();
        strRemarks = in.readString();
        strDocOptionalVal = in.readString();
        strUMCode = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(primaryID);
        dest.writeInt(posp_table_id);
        dest.writeString(strPan);
        dest.writeString(strIACode);
        dest.writeString(strDocStatus);
        dest.writeString(strReqStatus);
        dest.writeString(strReqRaised);
        dest.writeString(strDocName);
        dest.writeString(strRemarks);
        dest.writeString(strDocOptionalVal);
        dest.writeString(strUMCode);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<POJO_POSP_RA_Rejection> CREATOR = new Creator<POJO_POSP_RA_Rejection>() {
        @Override
        public POJO_POSP_RA_Rejection createFromParcel(Parcel in) {
            return new POJO_POSP_RA_Rejection(in);
        }

        @Override
        public POJO_POSP_RA_Rejection[] newArray(int size) {
            return new POJO_POSP_RA_Rejection[size];
        }
    };


    public String getStrUMCode() {
        return strUMCode;
    }

    public void setStrUMCode(String strUMCode) {
        this.strUMCode = strUMCode;
    }

    public String getStrPan() {
        return strPan;
    }

    public void setStrPan(String strPan) {
        this.strPan = strPan;
    }

    public String getStrIACode() {
        return strIACode;
    }

    public void setStrIACode(String strIACode) {
        this.strIACode = strIACode;
    }

    public String getStrDocStatus() {
        return strDocStatus;
    }

    public void setStrDocStatus(String strDocStatus) {
        this.strDocStatus = strDocStatus;
    }

    public String getStrReqStatus() {
        return strReqStatus;
    }

    public void setStrReqStatus(String strReqStatus) {
        this.strReqStatus = strReqStatus;
    }

    public String getStrReqRaised() {
        return strReqRaised;
    }

    public void setStrReqRaised(String strReqRaised) {
        this.strReqRaised = strReqRaised;
    }

    public String getStrDocName() {
        return strDocName;
    }

    public void setStrDocName(String strDocName) {
        this.strDocName = strDocName;
    }

    public String getStrRemarks() {
        return strRemarks;
    }

    public void setStrRemarks(String strRemarks) {
        this.strRemarks = strRemarks;
    }

    public int getPrimaryID() {
        return primaryID;
    }

    public void setPrimaryID(int primaryID) {
        this.primaryID = primaryID;
    }

    public int getPosp_table_id() {
        return posp_table_id;
    }

    public void setPosp_table_id(int posp_table_id) {
        this.posp_table_id = posp_table_id;
    }

    public String getStrDocOptionalVal() {
        return strDocOptionalVal;
    }

    public void setStrDocOptionalVal(String strDocOptionalVal) {
        this.strDocOptionalVal = strDocOptionalVal;
    }
}
