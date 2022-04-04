package sbilife.com.pointofsale_bancaagency.agent_on_boarding;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by O0411 on 24/04/2018.
 */

public class PojoAOB implements Parcelable {

    public static final Creator<PojoAOB> CREATOR = new Creator<PojoAOB>() {
        @Override
        public PojoAOB createFromParcel(Parcel in) {
            return new PojoAOB(in);
        }

        @Override
        public PojoAOB[] newArray(int size) {
            return new PojoAOB[size];
        }
    };
    private String str_id = "", str_aadhaar_no = "", str_aadhaar_details = "", str_pan_no = "", str_pan_details = "",
            str_basic_details = "", str_personal_info = "", str_occupation_info = "", str_nomination_info = "",
            str_bank_details = "", str_form_1_a = "", str_exam_training_details = "", str_bsm_interview_questions = "",
            str_declarations_conditions = "", str_doc_upload = "", str_created_by = "",
            str_updated_by = "", str_created_date = "", str_updated_date = "",
            str_synch_status = "", strEnrollType = "";

    public PojoAOB() {
    }

    public PojoAOB(String str_id, String str_aadhaar_no, String str_aadhaar_details, String str_pan_no, String str_pan_details, String str_basic_details,
                   String str_personal_info, String str_occupation_info, String str_nomination_info, String str_bank_details,
                   String str_form_1_a, String str_exam_training_details, String str_bsm_interview_questions, String str_declarations_conditions,
                   String str_doc_upload, String str_created_by, String str_updated_by, String str_created_date, String str_updated_date,
                   String str_synch_status, String strEnrollType) {
        this.str_id = str_id;
        this.str_aadhaar_no = str_aadhaar_no;
        this.str_aadhaar_details = str_aadhaar_details;
        this.str_pan_no = str_pan_no;
        this.str_pan_details = str_pan_details;
        this.str_basic_details = str_basic_details;
        this.str_personal_info = str_personal_info;
        this.str_occupation_info = str_occupation_info;
        this.str_nomination_info = str_nomination_info;
        this.str_bank_details = str_bank_details;
        this.str_form_1_a = str_form_1_a;
        this.str_exam_training_details = str_exam_training_details;
        this.str_bsm_interview_questions = str_bsm_interview_questions;
        this.str_declarations_conditions = str_declarations_conditions;
        this.str_doc_upload = str_doc_upload;
        this.str_created_by = str_created_by;
        this.str_updated_by = str_updated_by;
        this.str_created_date = str_created_date;
        this.str_updated_date = str_updated_date;
        this.str_synch_status = str_synch_status;
        this.strEnrollType = strEnrollType;
    }

    protected PojoAOB(Parcel in) {
        str_id = in.readString();
        str_aadhaar_no = in.readString();
        str_aadhaar_details = in.readString();
        str_pan_no = in.readString();
        str_pan_details = in.readString();
        str_basic_details = in.readString();
        str_personal_info = in.readString();
        str_occupation_info = in.readString();
        str_nomination_info = in.readString();
        str_bank_details = in.readString();
        str_form_1_a = in.readString();
        str_exam_training_details = in.readString();
        str_bsm_interview_questions = in.readString();
        str_declarations_conditions = in.readString();
        str_doc_upload = in.readString();
        str_created_by = in.readString();
        str_updated_by = in.readString();
        str_created_date = in.readString();
        str_updated_date = in.readString();
        str_synch_status = in.readString();
        strEnrollType = in.readString();
    }

    public String getStrEnrollType() {
        return strEnrollType;
    }

    public void setStrEnrollType(String strEnrollType) {
        this.strEnrollType = strEnrollType;
    }

    public String getStr_basic_details() {
        return str_basic_details;
    }

    public void setStr_basic_details(String str_basic_details) {
        this.str_basic_details = str_basic_details;
    }

    public String getStr_declarations_conditions() {
        return str_declarations_conditions;
    }

    public void setStr_declarations_conditions(String str_declarations_conditions) {
        this.str_declarations_conditions = str_declarations_conditions;
    }

    public String getStr_doc_upload() {
        return str_doc_upload;
    }

    public void setStr_doc_upload(String str_doc_upload) {
        this.str_doc_upload = str_doc_upload;
    }

    public String getStr_exam_training_details() {
        return str_exam_training_details;
    }

    public void setStr_exam_training_details(String str_exam_training_details) {
        this.str_exam_training_details = str_exam_training_details;
    }

    public String getStr_bsm_interview_questions() {
        return str_bsm_interview_questions;
    }

    public void setStr_bsm_interview_questions(String str_bsm_interview_questions) {
        this.str_bsm_interview_questions = str_bsm_interview_questions;
    }

    public String getStr_id() {
        return str_id;
    }

    public void setStr_id(String str_id) {
        this.str_id = str_id;
    }

    public String getStr_aadhaar_no() {
        return str_aadhaar_no;
    }

    public void setStr_aadhaar_no(String str_aadhaar_no) {
        this.str_aadhaar_no = str_aadhaar_no;
    }

    public String getStr_aadhaar_details() {
        return str_aadhaar_details;
    }

    public void setStr_aadhaar_details(String str_aadhaar_details) {
        this.str_aadhaar_details = str_aadhaar_details;
    }

    public String getStr_pan_no() {
        return str_pan_no;
    }

    public void setStr_pan_no(String str_pan_no) {
        this.str_pan_no = str_pan_no;
    }

    public String getStr_pan_details() {
        return str_pan_details;
    }

    public void setStr_pan_details(String str_pan_details) {
        this.str_pan_details = str_pan_details;
    }

    public String getStr_personal_info() {
        return str_personal_info;
    }

    public void setStr_personal_info(String str_personal_info) {
        this.str_personal_info = str_personal_info;
    }

    public String getStr_occupation_info() {
        return str_occupation_info;
    }

    public void setStr_occupation_info(String str_occupation_info) {
        this.str_occupation_info = str_occupation_info;
    }

    public String getStr_nomination_info() {
        return str_nomination_info;
    }

    public void setStr_nomination_info(String str_nomination_info) {
        this.str_nomination_info = str_nomination_info;
    }

    public String getStr_bank_details() {
        return str_bank_details;
    }

    public void setStr_bank_details(String str_bank_details) {
        this.str_bank_details = str_bank_details;
    }

    public String getStr_form_1_a() {
        return str_form_1_a;
    }

    public void setStr_form_1_a(String str_form_1_a) {
        this.str_form_1_a = str_form_1_a;
    }

    public String getStr_created_by() {
        return str_created_by;
    }

    public void setStr_created_by(String str_created_by) {
        this.str_created_by = str_created_by;
    }

    public String getStr_updated_by() {
        return str_updated_by;
    }

    public void setStr_updated_by(String str_updated_by) {
        this.str_updated_by = str_updated_by;
    }

    public String getStr_created_date() {
        return str_created_date;
    }

    public void setStr_created_date(String str_created_date) {
        this.str_created_date = str_created_date;
    }

    public String getStr_updated_date() {
        return str_updated_date;
    }

    public void setStr_updated_date(String str_updated_date) {
        this.str_updated_date = str_updated_date;
    }

    public String getStr_synch_status() {
        return str_synch_status;
    }

    public void setStr_synch_status(String str_synch_status) {
        this.str_synch_status = str_synch_status;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(str_id);
        dest.writeString(str_aadhaar_no);
        dest.writeString(str_aadhaar_details);
        dest.writeString(str_pan_no);
        dest.writeString(str_pan_details);
        dest.writeString(str_basic_details);
        dest.writeString(str_personal_info);
        dest.writeString(str_occupation_info);
        dest.writeString(str_nomination_info);
        dest.writeString(str_bank_details);
        dest.writeString(str_form_1_a);
        dest.writeString(str_exam_training_details);
        dest.writeString(str_bsm_interview_questions);
        dest.writeString(str_declarations_conditions);
        dest.writeString(str_doc_upload);
        dest.writeString(str_created_by);
        dest.writeString(str_updated_by);
        dest.writeString(str_created_date);
        dest.writeString(str_updated_date);
        dest.writeString(str_synch_status);
        dest.writeString(strEnrollType);
    }
}
