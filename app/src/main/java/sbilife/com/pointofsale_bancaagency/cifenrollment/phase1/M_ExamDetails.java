package sbilife.com.pointofsale_bancaagency.cifenrollment.phase1;

public class M_ExamDetails {

	private String str_quotation_no = "", str_exam_center = "";

	public String getStr_quotation_no() {
		return str_quotation_no;
	}

	public void setStr_quotation_no(String str_quotation_no) {
		this.str_quotation_no = str_quotation_no;
	}

	public String getStr_exam_center() {
		return str_exam_center;
	}

	public void setStr_exam_center(String str_exam_center) {
		this.str_exam_center = str_exam_center;
	}

	public M_ExamDetails(String str_quotation_no, String str_exam_center) {
		super();
		this.str_quotation_no = str_quotation_no;
		this.str_exam_center = str_exam_center;
	}

}
