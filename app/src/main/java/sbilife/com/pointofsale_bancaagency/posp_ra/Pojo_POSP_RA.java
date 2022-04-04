package sbilife.com.pointofsale_bancaagency.posp_ra;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by O0411 on 24/04/2018.
 */

public class Pojo_POSP_RA implements Parcelable {

    public static final Creator<Pojo_POSP_RA> CREATOR = new Creator<Pojo_POSP_RA>() {
        @Override
        public Pojo_POSP_RA createFromParcel(Parcel in) {
            return new Pojo_POSP_RA(in);
        }

        @Override
        public Pojo_POSP_RA[] newArray(int size) {
            return new Pojo_POSP_RA[size];
        }
    };
    private String str_id = "", str_aadhaar_no = "", str_aadhaar_details = "",
            str_pan_no = "", str_pan_details = "", str_personal_info = "",
            str_occupation_info = "", str_nomination_info = "",
            str_bank_details = "", str_exam_training_details = "",
            str_declarations_conditions = "", str_doc_upload = "",
            str_irn = "", str_in_app_status = "", str_in_app_status_remark = "",
            str_created_by = "", str_updated_by = "",
            str_created_date = "", str_updated_date = "",
            str_applicant_type = "", str_enrollment_type = "";

    public Pojo_POSP_RA() {
    }

    public Pojo_POSP_RA(String str_id, String str_aadhaar_no, String str_aadhaar_details, String str_pan_no,
                        String str_pan_details, String str_personal_info, String str_occupation_info,
                        String str_nomination_info, String str_bank_details, String str_exam_training_details,
                        String str_declarations_conditions, String str_doc_upload, String str_irn,
                        String str_in_app_status, String str_in_app_status_remark,
                        String str_created_by, String str_updated_by,
                        String str_created_date, String str_updated_date, String str_applicant_type,
                        String str_enrollment_type) {
        this.str_id = str_id;
        this.str_aadhaar_no = str_aadhaar_no;
        this.str_aadhaar_details = str_aadhaar_details;
        this.str_pan_no = str_pan_no;
        this.str_pan_details = str_pan_details;
        this.str_personal_info = str_personal_info;
        this.str_occupation_info = str_occupation_info;
        this.str_nomination_info = str_nomination_info;
        this.str_bank_details = str_bank_details;
        this.str_exam_training_details = str_exam_training_details;
        this.str_declarations_conditions = str_declarations_conditions;
        this.str_doc_upload = str_doc_upload;
        this.str_irn = str_irn;
        this.str_in_app_status = str_in_app_status;
        this.str_in_app_status_remark = str_in_app_status_remark;
        this.str_created_by = str_created_by;
        this.str_updated_by = str_updated_by;
        this.str_created_date = str_created_date;
        this.str_updated_date = str_updated_date;
        this.str_applicant_type = str_applicant_type;
        this.str_enrollment_type = str_enrollment_type;
    }

    protected Pojo_POSP_RA(Parcel in) {
        str_id = in.readString();
        str_aadhaar_no = in.readString();
        str_aadhaar_details = in.readString();
        str_pan_no = in.readString();
        str_pan_details = in.readString();
        str_personal_info = in.readString();
        str_occupation_info = in.readString();
        str_nomination_info = in.readString();
        str_bank_details = in.readString();
        str_exam_training_details = in.readString();
        str_declarations_conditions = in.readString();
        str_doc_upload = in.readString();
        str_irn = in.readString();
        str_in_app_status = in.readString();
        str_in_app_status_remark = in.readString();
        str_created_by = in.readString();
        str_updated_by = in.readString();
        str_created_date = in.readString();
        str_updated_date = in.readString();
        str_applicant_type = in.readString();
        str_enrollment_type = in.readString();
    }

    public String getStr_enrollment_type() {
        return str_enrollment_type;
    }

    public void setStr_enrollment_type(String str_enrollment_type) {
        this.str_enrollment_type = str_enrollment_type;
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

    public String getStr_exam_training_details() {
        return str_exam_training_details;
    }

    public void setStr_exam_training_details(String str_exam_training_details) {
        this.str_exam_training_details = str_exam_training_details;
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

    public String getStr_irn() {
        return str_irn;
    }

    public void setStr_irn(String str_irn) {
        this.str_irn = str_irn;
    }

    public String getStr_in_app_status() {
        return str_in_app_status;
    }

    public void setStr_in_app_status(String str_in_app_status) {
        this.str_in_app_status = str_in_app_status;
    }

    public String getStr_in_app_status_remark() {
        return str_in_app_status_remark;
    }

    public void setStr_in_app_status_remark(String str_in_app_status_remark) {
        this.str_in_app_status_remark = str_in_app_status_remark;
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

    public String getStr_applicant_type() {
        return str_applicant_type;
    }

    public void setStr_applicant_type(String str_applicant_type) {
        this.str_applicant_type = str_applicant_type;
    }

    public String get_form1a_xml() {

        return "<form1a_info_any_insurance></form1a_info_any_insurance>"
                + "<form1a_info_insurance_name></form1a_info_insurance_name>"
                + "<form1a_info_insurance_agency_code></form1a_info_insurance_agency_code>"
                + "<form1a_info_insurance_appointment_date></form1a_info_insurance_appointment_date>"
                + "<form1a_info_insurance_cessation_date></form1a_info_insurance_cessation_date>"
                + "<form1a_info_insurance_reason_for_cess></form1a_info_insurance_reason_for_cess>";

    }

    public String getBSMQuestions() {
        return "<bsm_questions_q1_yes_no></bsm_questions_q1_yes_no>"
                + "<bsm_questions_q1_yes_no_comment></bsm_questions_q1_yes_no_comment>"
                + "<bsm_questions_q2_yes_no></bsm_questions_q2_yes_no>"
                + "<bsm_questions_q2_yes_no_comment></bsm_questions_q2_yes_no_comment>"
                + "<bsm_questions_q3_comment></bsm_questions_q3_comment>"
                + "<bsm_questions_q4_comment></bsm_questions_q4_comment>"
                + "<bsm_questions_q5_comment></bsm_questions_q5_comment>"
                + "<bsm_questions_clarify_check>true</bsm_questions_clarify_check>";
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
        dest.writeString(str_personal_info);
        dest.writeString(str_occupation_info);
        dest.writeString(str_nomination_info);
        dest.writeString(str_bank_details);
        dest.writeString(str_exam_training_details);
        dest.writeString(str_declarations_conditions);
        dest.writeString(str_doc_upload);
        dest.writeString(str_irn);
        dest.writeString(str_in_app_status);
        dest.writeString(str_in_app_status_remark);
        dest.writeString(str_created_by);
        dest.writeString(str_updated_by);
        dest.writeString(str_created_date);
        dest.writeString(str_updated_date);
        dest.writeString(str_applicant_type);
        dest.writeString(str_enrollment_type);
    }
}
