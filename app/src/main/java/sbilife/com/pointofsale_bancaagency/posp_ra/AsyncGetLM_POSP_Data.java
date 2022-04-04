package sbilife.com.pointofsale_bancaagency.posp_ra;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.text.Html;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.text.SimpleDateFormat;
import java.util.Date;

import sbilife.com.pointofsale_bancaagency.DatabaseHelper;
import sbilife.com.pointofsale_bancaagency.ParseXML;
import sbilife.com.pointofsale_bancaagency.ServiceURL;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;

public class AsyncGetLM_POSP_Data extends AsyncTask<String, String, String> {

    private final String NAMESPACE = "http://tempuri.org/";
    private final String METHOD_NAME_GET_LM_POSP_DATA = "getPOSPdetail";

    private final ProgressDialog mProgressDialog;
    private final InterfaceAsyncGetLM_POSP_Data listener;
    private final CommonMethods mCommonMethods;
    private String strPan, strIRN, strUM, strAgencyType, strFrom, strCreatedDate, strEnrollmentType;
    private Context mContext;
    private volatile boolean running;
    private StringBuilder str_personal_info, str_occupational_info, str_nominational_info, str_bank_info,
            str_exam_training_details, str_terms_conditions;

    public AsyncGetLM_POSP_Data(InterfaceAsyncGetLM_POSP_Data listener, String strPan, Context mContext,
                                String strFrom) {
        this.listener = listener;
        this.strPan = strPan;
        this.mContext = mContext;
        this.strFrom = strFrom;

        mCommonMethods = new CommonMethods();
        mProgressDialog = new ProgressDialog(mContext, ProgressDialog.THEME_HOLO_LIGHT);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setTitle(Html.fromHtml("<font color='#00a1e3'><b>Please Wait...<b></font>"));
    }

    @Override
    protected void onPreExecute() {
        mProgressDialog.show();
    }

    @Override
    protected String doInBackground(String... strings) {
        try {
            running = true;
            SoapObject request;

            request = new SoapObject(NAMESPACE, METHOD_NAME_GET_LM_POSP_DATA);
            request.addProperty("strPOSPCode", strPan);//PAN no
            request.addProperty("strType", strFrom);


            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);
            envelope.dotNet = true;

            envelope.setOutputSoapObject(request);

            // 	allowAllSSL();
            mCommonMethods.TLSv12Enable();

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);

            HttpTransportSE androidHttpTranport = new HttpTransportSE(ServiceURL.SERVICE_URL);

            androidHttpTranport.call(NAMESPACE + METHOD_NAME_GET_LM_POSP_DATA, envelope);
            SoapPrimitive sa = (SoapPrimitive) envelope.getResponse();

            String inputpolicylist = sa.toString();

            ParseXML prsObj = new ParseXML();

            inputpolicylist = prsObj.parseXmlTag(inputpolicylist, "NewDataSet");

            if (inputpolicylist != null) {

                inputpolicylist = prsObj.parseXmlTag(inputpolicylist, "Table");

                //personal info
                str_personal_info = new StringBuilder();

                str_personal_info.append("<personal_info_title>").append(
                        prsObj.parseXmlTagNullable(inputpolicylist, "PER_TITLE")).append("</personal_info_title>");
                str_personal_info.append("<personal_info_full_name>").append(
                        prsObj.parseXmlTagNullable(inputpolicylist, "PER_FULL_NAME")).append("</personal_info_full_name>");
                str_personal_info.append("<personal_info_dob>").append(
                        getDD_MM_YYYY_date(prsObj.parseXmlTagNullable(inputpolicylist, "PER_DOB"))).append("</personal_info_dob>");
                str_personal_info.append("<personal_info_gender>").append(
                        prsObj.parseXmlTagNullable(inputpolicylist, "PER_GENDER")).append("</personal_info_gender>");
                str_personal_info.append("<personal_info_nationality>"
                        + prsObj.parseXmlTagNullable(inputpolicylist, "PER_NATIONALITY") + "</personal_info_nationality>");
                str_personal_info.append("<personal_info_aadhaar_no></personal_info_aadhaar_no>");
                str_personal_info.append("<personal_info_pan_no>"
                        + prsObj.parseXmlTagNullable(inputpolicylist, "PER_PAN_NO") + "</personal_info_pan_no>");
                str_personal_info.append("<personal_info_communication_address1>"
                        + prsObj.parseXmlTagNullable(inputpolicylist, "PER_COMMU_ADDRESS") + "</personal_info_communication_address1>");
                str_personal_info.append("<personal_info_communication_address2>"
                        + prsObj.parseXmlTagNullable(inputpolicylist, "PER_INFO_COMMU_ADDRESS2") + "</personal_info_communication_address2>");
                str_personal_info.append("<personal_info_communication_address3>"
                        + prsObj.parseXmlTagNullable(inputpolicylist, "PER_INFO_COMMU_ADDRESS3") + "</personal_info_communication_address3>");
                str_personal_info.append("<personal_info_communication_address_pin>"
                        + prsObj.parseXmlTagNullable(inputpolicylist, "PER_COMMU_ADDRESS_PIN") + "</personal_info_communication_address_pin>");

                str_personal_info.append("<personal_info_communication_add_same>"
                        + prsObj.parseXmlTagNullable(inputpolicylist, "PER_INFO_COMMU_ADD_SAME") + "</personal_info_communication_add_same>");

                str_personal_info.append("<personal_info_permanant_address1>"
                        + prsObj.parseXmlTagNullable(inputpolicylist, "PER_PERMNT_ADDRESS") + "</personal_info_permanant_address1>");
                str_personal_info.append("<personal_info_permanant_address2>"
                        + prsObj.parseXmlTagNullable(inputpolicylist, "PER_INFO_PERMA_ADDRESS2") + "</personal_info_permanant_address2>");
                str_personal_info.append("<personal_info_permanant_address3>"
                        + prsObj.parseXmlTagNullable(inputpolicylist, "PER_INFO_PERMA_ADDRESS3") + "</personal_info_permanant_address3>");
                str_personal_info.append("<personal_info_permanant_address_pin>"
                        + prsObj.parseXmlTagNullable(inputpolicylist, "PER_PERMNT_ADDRESS_PIN") + "</personal_info_permanant_address_pin>");

                str_personal_info.append("<personal_info_father_husband_name>"
                        + prsObj.parseXmlTagNullable(inputpolicylist, "PER_FATHER_HUSBAND_NAME") + "</personal_info_father_husband_name>");
                str_personal_info.append("<personal_info_relation_with_applicant>"
                        + prsObj.parseXmlTagNullable(inputpolicylist, "PER_REL_WITH_APPLICANT") + "</personal_info_relation_with_applicant>");
                str_personal_info.append("<personal_info_maiden_name>"
                        + prsObj.parseXmlTagNullable(inputpolicylist, "PER_MAIDEN_NAME") + "</personal_info_maiden_name>");
                str_personal_info.append("<personal_info_marital_status>"
                        + prsObj.parseXmlTagNullable(inputpolicylist, "PER_MARITAL_STATUS") + "</personal_info_marital_status>");
                str_personal_info.append("<personal_info_caste_category>"
                        + prsObj.parseXmlTagNullable(inputpolicylist, "PER_CASTE_CATEGORY") + "</personal_info_caste_category>");
                str_personal_info.append("<personal_info_mobile_no>"
                        + prsObj.parseXmlTagNullable(inputpolicylist, "PER_MOBILE_NO") + "</personal_info_mobile_no>");
                str_personal_info.append("<personal_info_residence_no>"
                        + prsObj.parseXmlTagNullable(inputpolicylist, "PER_RESIDENCE_NO") + "</personal_info_residence_no>");
                str_personal_info.append("<personal_info_email_id>"
                        + prsObj.parseXmlTagNullable(inputpolicylist, "PER_EMAIL_ID") + "</personal_info_email_id>");
                str_personal_info.append("<personal_info_educational_details_basic_qualification>"
                        + prsObj.parseXmlTagNullable(inputpolicylist, "PER_EDU_DET_BASIC_QUALI") + "</personal_info_educational_details_basic_qualification>");
                str_personal_info.append("<personal_info_educational_details_passing_roll_no>"
                        + prsObj.parseXmlTagNullable(inputpolicylist, "PER_EDU_DET_PASS_ROLL_NO") + "</personal_info_educational_details_passing_roll_no>");
                str_personal_info.append("<personal_info_educational_details_passing_university>"
                        + prsObj.parseXmlTagNullable(inputpolicylist, "PER_EDU_DET_PASS_UNIVE") + "</personal_info_educational_details_passing_university>");
                str_personal_info.append("<personal_info_educational_details_passing_month_year>"
                        + prsObj.parseXmlTagNullable(inputpolicylist, "PER_EDU_DET_PASS_MONTH_YR") + "</personal_info_educational_details_passing_month_year>");
                str_personal_info.append("<personal_info_educational_details_professional_qualification>"
                        + prsObj.parseXmlTagNullable(inputpolicylist, "PER_EDU_DET_PROFE_QUALI") + "</personal_info_educational_details_professional_qualification>");
                str_personal_info.append("<personal_info_educational_details_professional_qualification_others>"
                        + prsObj.parseXmlTagNullable(inputpolicylist, "PER_EDU_DET_PROFE_QUALI_OTH") + "</personal_info_educational_details_professional_qualification_others>");

                //Occupational
                str_occupational_info = new StringBuilder();
                str_occupational_info.append("<occupatinal_info_applicable>"
                        + prsObj.parseXmlTagNullable(inputpolicylist, "OCCU_APPLICABLE") + "</occupatinal_info_applicable>");
                str_occupational_info.append("<occupatinal_info_applicable_cmnt>"
                        + prsObj.parseXmlTagNullable(inputpolicylist, "OCCU_APPLICABLE_CMNT") + "</occupatinal_info_applicable_cmnt>");
                str_occupational_info.append("<occupatinal_info_self_emp>"
                        + prsObj.parseXmlTagNullable(inputpolicylist, "OCCU_SELF_EMP") + "</occupatinal_info_self_emp>");
                str_occupational_info.append("<occupatinal_info_self_emp_cmnt>"
                        + prsObj.parseXmlTagNullable(inputpolicylist, "OCCU_SELF_EMP_CMNT") + "</occupatinal_info_self_emp_cmnt>");
                str_occupational_info.append("<occupatinal_info_area_ops>"
                        + prsObj.parseXmlTagNullable(inputpolicylist, "OCCU_AREA_OPS") + "</occupatinal_info_area_ops>");
                str_occupational_info.append("<occupatinal_info_annual_income>"
                        + prsObj.parseXmlTagNullable(inputpolicylist, "OCCU_ANNUAL_INCOME") + "</occupatinal_info_annual_income>");
                str_occupational_info.append("<occupatinal_info_occu_agency>"
                        + prsObj.parseXmlTagNullable(inputpolicylist, "OCCU_OCCU_AGENCY") + "</occupatinal_info_occu_agency>");
                str_occupational_info.append("<occupatinal_info_occu_company>"
                        + prsObj.parseXmlTagNullable(inputpolicylist, "OCCU_OCCU_COM") + "</occupatinal_info_occu_company>");
                str_occupational_info.append("<occupatinal_info_occu_company_cmnt>"
                        + prsObj.parseXmlTagNullable(inputpolicylist, "OCCU_OCCU_COM_CMNT") + "</occupatinal_info_occu_company_cmnt>");
                str_occupational_info.append("<occupatinal_info_evr_surrendered>"
                        + prsObj.parseXmlTagNullable(inputpolicylist, "OCCU_EVR_SURRENDERED") + "</occupatinal_info_evr_surrendered>");
                str_occupational_info.append("<occupatinal_info_evr_surrendered_cmnt>"
                        + prsObj.parseXmlTagNullable(inputpolicylist, "OCCU_EVR_SURREN_CMNT") + "</occupatinal_info_evr_surrendered_cmnt>");
                str_occupational_info.append("<occupatinal_info_other_insurer>"
                        + prsObj.parseXmlTagNullable(inputpolicylist, "OCCU_OTHER_INSURER") + "</occupatinal_info_other_insurer>");
                str_occupational_info.append("<occupatinal_info_other_insurer_company>"
                        + prsObj.parseXmlTagNullable(inputpolicylist, "OCCU_OTHER_INSURER_COM") + "</occupatinal_info_other_insurer_company>");
                str_occupational_info.append("<occupatinal_info_other_insurer_company_cmnt>"
                        + prsObj.parseXmlTagNullable(inputpolicylist, "OCCU_OTHER_INSURER_COM_CMNT") + "</occupatinal_info_other_insurer_company_cmnt>");
                str_occupational_info.append("<occupatinal_info_r_u_promoter>"
                        + prsObj.parseXmlTagNullable(inputpolicylist, "OCCU_R_U_PROMO") + "</occupatinal_info_r_u_promoter>");
                str_occupational_info.append("<occupatinal_info_r_u_promoter_company>"
                        + prsObj.parseXmlTagNullable(inputpolicylist, "OCCU_R_U_PROMO_COM") + "</occupatinal_info_r_u_promoter_company>");
                str_occupational_info.append("<occupatinal_info_r_u_promoter_company_pan>"
                        + prsObj.parseXmlTagNullable(inputpolicylist, "OCCU_R_U_PROMO_COM_PAN") + "</occupatinal_info_r_u_promoter_company_pan>");
                str_occupational_info.append("<occupatinal_info_r_u_promoter_company_tin>"
                        + prsObj.parseXmlTagNullable(inputpolicylist, "OCCU_R_U_PROMO_COM_TIN") + "</occupatinal_info_r_u_promoter_company_tin>");
                str_occupational_info.append("<occupatinal_info_asso_with_sbil>"
                        + prsObj.parseXmlTagNullable(inputpolicylist, "OCCU_ASSO_WITH_SBIL") + "</occupatinal_info_asso_with_sbil>");
                str_occupational_info.append("<occupatinal_info_asso_with_sbil_cmnt>"
                        + prsObj.parseXmlTagNullable(inputpolicylist, "OCCU_ASSO_WITH_SBIL_CMNT") + "</occupatinal_info_asso_with_sbil_cmnt>");
                str_occupational_info.append("<occupatinal_info_related_sbil_emp>"
                        + prsObj.parseXmlTagNullable(inputpolicylist, "OCCU_RELE_SBIL_EMP") + "</occupatinal_info_related_sbil_emp>");
                str_occupational_info.append("<occupatinal_info_related_sbil_emp_name>"
                        + prsObj.parseXmlTagNullable(inputpolicylist, "OCCU_RELE_SBIL_EMP_NAME") + "</occupatinal_info_related_sbil_emp_name>");
                str_occupational_info.append("<occupatinal_info_related_sbil_emp_designation>"
                        + prsObj.parseXmlTagNullable(inputpolicylist, "OCCU_RELE_SBIL_EMP_DESIG") + "</occupatinal_info_related_sbil_emp_designation>");
                str_occupational_info.append("<occupatinal_info_related_sbil_emp_relation>"
                        + prsObj.parseXmlTagNullable(inputpolicylist, "OCCU_RELE_SBIL_EMP_REL") + "</occupatinal_info_related_sbil_emp_relation>");
                str_occupational_info.append("<occupatinal_info_related_sbil_emp_insu_off>"
                        + prsObj.parseXmlTagNullable(inputpolicylist, "OCCU_RELE_SBIL_EMP_INSU_OFF") + "</occupatinal_info_related_sbil_emp_insu_off>");
                str_occupational_info.append("<occupatinal_info_aforeside_relative>"
                        + prsObj.parseXmlTagNullable(inputpolicylist, "OCCU_AFORESIDE_RELATIVE") + "</occupatinal_info_aforeside_relative>");
                str_occupational_info.append("<occupatinal_info_related_sbil_bank_address>"
                        + prsObj.parseXmlTagNullable(inputpolicylist, "OCCU_RELE_SBIL_BANK_ADDR") + "</occupatinal_info_related_sbil_bank_address>");
                str_occupational_info.append("<occupatinal_info_sbi_x_emp>"
                        + prsObj.parseXmlTagNullable(inputpolicylist, "OCCU_SBI_X_EMP") + "</occupatinal_info_sbi_x_emp>");
                str_occupational_info.append("<occupatinal_info_sbi_x_emp_cmnt>"
                        + prsObj.parseXmlTagNullable(inputpolicylist, "OCCU_SBI_X_EMP_CMNT") + "</occupatinal_info_sbi_x_emp_cmnt>");

                str_occupational_info.append("<occupatinal_info_emp_last_date>" +
                        getDD_MM_YYYY_date(prsObj.parseXmlTagNullable(inputpolicylist, "OCCU_EMP_LAST_DATE"))
                        + "</occupatinal_info_emp_last_date>");

                str_occupational_info.append("<occupatinal_info_emp_cmnt>"
                        + prsObj.parseXmlTagNullable(inputpolicylist, "OCCU_EMP_CMNT") + "</occupatinal_info_emp_cmnt>");

                //Nominee
                str_nominational_info = new StringBuilder();
                str_nominational_info.append("<nominee_info_nom_title>"
                        + prsObj.parseXmlTagNullable(inputpolicylist, "NOMINEE_NOM_TITLE") + "</nominee_info_nom_title>");
                str_nominational_info.append("<nominee_info_nom_full_name>"
                        + prsObj.parseXmlTagNullable(inputpolicylist, "NOMINEE_NOM_FULL_NAME") + "</nominee_info_nom_full_name>");
                str_nominational_info.append("<nominee_info_nom_dob>" +
                        getDD_MM_YYYY_date(prsObj.parseXmlTagNullable(inputpolicylist, "NOMINEE_NOM_DOB"))
                        + "</nominee_info_nom_dob>");
                str_nominational_info.append("<nominee_info_nom_relation>"
                        + prsObj.parseXmlTagNullable(inputpolicylist, "NOMINEE_NOM_RELATION") + "</nominee_info_nom_relation>");
                str_nominational_info.append("<nominee_info_nom_gender>"
                        + prsObj.parseXmlTagNullable(inputpolicylist, "NOMINEE_NOM_GENDER") + "</nominee_info_nom_gender>");
                str_nominational_info.append("<nominee_info_nom_percentage>"
                        + prsObj.parseXmlTagNullable(inputpolicylist, "NOMINEE_NOM_PERCENTAGE") + "</nominee_info_nom_percentage>");
                str_nominational_info.append("<nominee_info_nom_address>"
                        + prsObj.parseXmlTagNullable(inputpolicylist, "NOMINEE_NOM_ADDRESS") + "</nominee_info_nom_address>");
                str_nominational_info.append("<nominee_info_nom_address2>"
                        + prsObj.parseXmlTagNullable(inputpolicylist, "NOMINEE_INFO_NOM_ADDRESS2") + "</nominee_info_nom_address2>");
                str_nominational_info.append("<nominee_info_nom_address3>"
                        + prsObj.parseXmlTagNullable(inputpolicylist, "NOMINEE_INFO_NOM_ADDRESS3") + "</nominee_info_nom_address3>");
                str_nominational_info.append("<nominee_info_nom_address_city>"
                        + prsObj.parseXmlTagNullable(inputpolicylist, "NOMINEE_NOM_ADDRE_CITY") + "</nominee_info_nom_address_city>");
                str_nominational_info.append("<nominee_info_nom_address_state>"
                        + prsObj.parseXmlTagNullable(inputpolicylist, "NOMINEE_NOM_ADDR_STATE") + "</nominee_info_nom_address_state>");
                str_nominational_info.append("<nominee_info_nom_address_pincode>"
                        + prsObj.parseXmlTagNullable(inputpolicylist, "NOMINEE_NOM_ADDRE_PINCODE") + "</nominee_info_nom_address_pincode>");
                str_nominational_info.append("<nominee_info_appointee_title>"
                        + prsObj.parseXmlTagNullable(inputpolicylist, "NOMINEE_APPOINTEE_TITLE") + "</nominee_info_appointee_title>");
                str_nominational_info.append("<nominee_info_appointee_full_name>"
                        + prsObj.parseXmlTagNullable(inputpolicylist, "NOMINEE_APPOINTEE_FULL_NAME") + "</nominee_info_appointee_full_name>");

                str_nominational_info.append("<nominee_info_appointee_dob>" +
                        getDD_MM_YYYY_date(prsObj.parseXmlTagNullable(inputpolicylist, "NOMINEE_APPOINTEE_DOB"))
                        + "</nominee_info_appointee_dob>");

                str_nominational_info.append("<nominee_info_appointee_relation>"
                        + prsObj.parseXmlTagNullable(inputpolicylist, "NOMINEE_APPOINTEE_RELATION") + "</nominee_info_appointee_relation>");
                str_nominational_info.append("<nominee_info_appointee_address>"
                        + prsObj.parseXmlTagNullable(inputpolicylist, "NOMINEE_APPOINTEE_ADDRESS") + "</nominee_info_appointee_address>");
                str_nominational_info.append("<nominee_info_appointee_address2>"
                        + prsObj.parseXmlTagNullable(inputpolicylist, "NOMINEE_INFO_APPO_ADDRESS2") + "</nominee_info_appointee_address2>");
                str_nominational_info.append("<nominee_info_appointee_address3>"
                        + prsObj.parseXmlTagNullable(inputpolicylist, "NOMINEE_INFO_APPO_ADDRESS3") + "</nominee_info_appointee_address3>");
                str_nominational_info.append("<nominee_info_appointee_address_city>"
                        + prsObj.parseXmlTagNullable(inputpolicylist, "NOMINEE_APPOIN_ADDRE_CITY") + "</nominee_info_appointee_address_city>");
                str_nominational_info.append("<nominee_info_appointee_address_state>"
                        + prsObj.parseXmlTagNullable(inputpolicylist, "NOMINEE_APPOIN_ADDRE_STATE") + "</nominee_info_appointee_address_state>");
                str_nominational_info.append("<nominee_info_appointee_address_pincode>"
                        + prsObj.parseXmlTagNullable(inputpolicylist, "NOMINEE_APPOIN_ADDRE_PINCODE") + "</nominee_info_appointee_address_pincode>");
                str_nominational_info.append("<nominee_info_witness_occupation>"
                        + prsObj.parseXmlTagNullable(inputpolicylist, "NOMINEE_WITNESS_OCCUPATION") + "</nominee_info_witness_occupation>");
                str_nominational_info.append("<nominee_info_witness_full_address>"
                        + prsObj.parseXmlTagNullable(inputpolicylist, "NOMINEE_WITNESS_FULL_ADDR") + "</nominee_info_witness_full_address>");
                str_nominational_info.append("<nominee_info_witness_name>"
                        + prsObj.parseXmlTagNullable(inputpolicylist, "NOMINEE_WITNESS_NAME") + "</nominee_info_witness_name>");

                //bank
                str_bank_info = new StringBuilder();
                str_bank_info.append("<bank_info_acc_type>"
                        + prsObj.parseXmlTagNullable(inputpolicylist, "BANK_INFO_ACC_TYPE") + "</bank_info_acc_type>");
                str_bank_info.append("<bank_info_acc_number>"
                        + prsObj.parseXmlTagNullable(inputpolicylist, "BANK_INFO_ACC_NUMBER") + "</bank_info_acc_number>");
                str_bank_info.append("<bank_info_branch_code>"
                        + prsObj.parseXmlTagNullable(inputpolicylist, "BANK_INFO_BRANCH_CODE") + "</bank_info_branch_code>");
                str_bank_info.append("<bank_info_ifsc_code>"
                        + prsObj.parseXmlTagNullable(inputpolicylist, "BANK_INFO_IFSC_CODE") + "</bank_info_ifsc_code>");

                //exam and training
                str_exam_training_details = new StringBuilder();
                str_exam_training_details.append("<exam_details_place>"
                        + prsObj.parseXmlTagNullable(inputpolicylist, "EXAM_DET_PLACE") + "</exam_details_place>");
                str_exam_training_details.append("<exam_details_language>"
                        + prsObj.parseXmlTagNullable(inputpolicylist, "EXAM_DET_LANGUAGE") + "</exam_details_language>");

                str_exam_training_details.append("<training_details_start_date>" +
                        getDD_MM_YYYY_date(prsObj.parseXmlTagNullable(inputpolicylist, "TRAINING_DET_START_DATE"))
                        + "</training_details_start_date>");

                str_exam_training_details.append("<training_details_end_date>" +
                        getDD_MM_YYYY_date(prsObj.parseXmlTagNullable(inputpolicylist, "TRAINING_DET_END_DATE"))
                        + "</training_details_end_date>");

                str_exam_training_details.append("<training_details_language>"
                        + prsObj.parseXmlTagNullable(inputpolicylist, "TRAINING_DETAILS_LANGUAGE") + "</training_details_language>");
                str_exam_training_details.append("<exam_details_exam_date>" +
                        getDD_MM_YYYY_date(prsObj.parseXmlTagNullable(inputpolicylist, "EXAM_DETAILS_EXAM_DATE"))
                        + "</exam_details_exam_date>");

                str_exam_training_details.append("<training_details_training_mode>"
                        + prsObj.parseXmlTagNullable(inputpolicylist, "TRAINING_DETAILS_TRAINING_MODE") + "</training_details_training_mode>");
                str_exam_training_details.append("<training_details_required_hrs>"
                        + prsObj.parseXmlTagNullable(inputpolicylist, "TRAINING_DETAILS_REQUIRED_HRS") + "</training_details_required_hrs>");
                str_exam_training_details.append("<training_details_institute_name>"
                        + prsObj.parseXmlTagNullable(inputpolicylist, "TRAINING_DETAILS_INSTITUT_NAME") + "</training_details_institute_name>");
                str_exam_training_details.append("<exam_details_exam_mode>"
                        + prsObj.parseXmlTagNullable(inputpolicylist, "EXAM_DETAILS_EXAM_MODE") + "</exam_details_exam_mode>");
                str_exam_training_details.append("<exam_details_exam_body>"
                        + prsObj.parseXmlTagNullable(inputpolicylist, "EXAM_DETAILS_EXAM_BODY") + "</exam_details_exam_body>");
                str_exam_training_details.append("<exam_details_marks_obtained>"
                        + prsObj.parseXmlTagNullable(inputpolicylist, "EXAM_DETAILS_MARKS_OBTAINED") + "</exam_details_marks_obtained>");
                str_exam_training_details.append("<exam_details_exam_status>"
                        + prsObj.parseXmlTagNullable(inputpolicylist, "EXAM_DETAILS_EXAM_STATUS") + "</exam_details_exam_status>");

                //Terms and condition
                str_terms_conditions = new StringBuilder();
                str_terms_conditions.append("<terms_conditions_place>"
                        + "" + "</terms_conditions_place>");
                str_terms_conditions.append("<terms_conditions_date>"
                        + "" + "</terms_conditions_date>");

                //irn
                strIRN = prsObj.parseXmlTagNullable(inputpolicylist, "POSP_RA_IRN");

                //created Date
                strCreatedDate = getDD_MM_YYYY_date(prsObj.parseXmlTagNullable(inputpolicylist, "CREATED_DATE"));

                //um
                strUM = prsObj.parseXmlTagNullable(inputpolicylist, "UM_CODE");

                //agency Type
                strAgencyType = prsObj.parseXmlTagNullable(inputpolicylist, "TYPE");

                //enrollment type
                strEnrollmentType = prsObj.parseXmlTagNullable(inputpolicylist, "SUB_TYPE");


                return "1";
            } else {
                return "";
            }

        } catch (Exception e) {
            try {
                throw (e);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            mProgressDialog.dismiss();
            running = false;
        }
        return null;
    }

    @Override
    protected void onPostExecute(String mResult) {

        int row_details = -1;

        try {
            if (mProgressDialog.isShowing())
                mProgressDialog.dismiss();

            if (mResult.equals("1")) {
                ContentValues cv = new ContentValues();
                DatabaseHelper db = new DatabaseHelper(mContext);

                if (strFrom.equals(mCommonMethods.str_posp_ra_customer_type)) {
                    cv.put(db.POSP_RA_PAN_NO, strPan);
                    cv.put(db.POSP_RA_CREATED_BY, strUM);
                    cv.put(db.POSP_RA_PERSONAL_INFO, str_personal_info.toString());
                    cv.put(db.POSP_RA_OCCUPATION_INFO, str_occupational_info.toString());
                    cv.put(db.POSP_RA_NOMINATION_INFO, str_nominational_info.toString());
                    cv.put(db.POSP_RA_BANK_DETAILS, str_bank_info.toString());
                    cv.put(db.POSP_RA_EXAM_TRAINING_DETAILS, str_exam_training_details.toString());
                    cv.put(db.POSP_RA_TERMS_CONDITIONS_DECLARATION, str_terms_conditions.toString());
                    cv.put(db.POSP_RA_AGENCY_TYPE, strAgencyType);
                    cv.put(db.POSP_RA_CREATED_DATE, strCreatedDate);
                    cv.put(db.POSP_RA_IRN, strIRN);
                    cv.put(db.POSP_RA_ENROLLMENT_TYPE, strEnrollmentType);

                    row_details = db.insert_POSP_RA(cv);
                } else {
                    cv.put(db.AGENT_ON_BOARDING_PAN_NO, strPan);
                    cv.put(db.AGENT_ON_BOARDING_CREATED_BY, strUM);
                    cv.put(db.AGENT_ON_BOARDING_PERSONAL_INFO, str_personal_info.toString());
                    cv.put(db.AGENT_ON_BOARDING_OCCUPATION_INFO, str_occupational_info.toString());
                    cv.put(db.AGENT_ON_BOARDING_NOMINATION_INFO, str_nominational_info.toString());
                    cv.put(db.AGENT_ON_BOARDING_BANK_DETAILS, str_bank_info.toString());
                    //cv.put(db.AGENT_ON_BOARDING_EXAM_TRAINING_DETAILS, str_exam_training_details.toString());
                    //cv.put(db.POSP_RA_TERMS_CONDITIONS_DECLARATION, str_terms_conditions.toString());
                    //cv.put(db.AGENT_ON_BOARDING_A, strAgencyType);
                    //cv.put(db.POSP_RA_IRN, strIRN);
                    cv.put(db.AGENT_ON_BOARDING_CREATED_DATE, strCreatedDate);
                    cv.put(db.AGENT_ON_BOARDING_ENROLLMENT_TYPE, strFrom);

                    row_details = db.insert_agent_on_boarding(cv);
                }
            }
        } catch (Exception e) {
            e.getMessage();
        }

        listener.onDataSuccess(row_details);
    }

    private String getDD_MM_YYYY_date(String serverDate) {

        serverDate = serverDate == null ? "" : serverDate;

        if (serverDate.equals("")) {
            return "";
        } else {

            SimpleDateFormat sdp1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
            SimpleDateFormat sdp2 = new SimpleDateFormat("MM-dd-yyyy");
            Date d1;
            try {
                d1 = sdp1.parse(serverDate);
                String str_d2 = sdp2.format(d1);
                return str_d2;
            } catch (Exception ex) {
                ex.printStackTrace();
                return "";
            }
        }

    }

    public interface InterfaceAsyncGetLM_POSP_Data {
        void onDataSuccess(int row_details);
    }
}
