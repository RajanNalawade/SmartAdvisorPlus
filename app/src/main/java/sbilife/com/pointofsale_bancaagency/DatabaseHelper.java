package sbilife.com.pointofsale_bancaagency;

import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import sbilife.com.pointofsale_bancaagency.agent_on_boarding.PojoAOB;
import sbilife.com.pointofsale_bancaagency.cifenrollment.phase1.M_ExamDetails;
import sbilife.com.pointofsale_bancaagency.cifenrollment.phase1.M_MainActivity_Data;
import sbilife.com.pointofsale_bancaagency.cifenrollment.phase1.M_Sync_Status;
import sbilife.com.pointofsale_bancaagency.cifenrollment.phase1.M_UserInformation;
import sbilife.com.pointofsale_bancaagency.cifenrollment.TCC_ExamDetails_Activity;
import sbilife.com.pointofsale_bancaagency.common.SimpleCrypto;
import sbilife.com.pointofsale_bancaagency.home.clsActivityCategory;
import sbilife.com.pointofsale_bancaagency.home.clsActivitySubCategory;
import sbilife.com.pointofsale_bancaagency.home.clsBDMTrackerCalendar;
import sbilife.com.pointofsale_bancaagency.home.clsBranch;
import sbilife.com.pointofsale_bancaagency.home.clsBranchAdvances;
import sbilife.com.pointofsale_bancaagency.home.clsBranchDeposits;
import sbilife.com.pointofsale_bancaagency.home.clsBranchProfile;
import sbilife.com.pointofsale_bancaagency.home.clsCalendarActivityRecorder;
import sbilife.com.pointofsale_bancaagency.home.clsDefineObjective;
import sbilife.com.pointofsale_bancaagency.home.clsDefineObjectiveSync;
import sbilife.com.pointofsale_bancaagency.home.clsEmail;
import sbilife.com.pointofsale_bancaagency.home.clsHOLead;
import sbilife.com.pointofsale_bancaagency.home.clsParamList;
import sbilife.com.pointofsale_bancaagency.home.clsSyncBranchProfile;
import sbilife.com.pointofsale_bancaagency.home.lmcorner.CovidSelfDeclarationDashboardActivity;
import sbilife.com.pointofsale_bancaagency.new_bussiness.ProductBIBean;
import sbilife.com.pointofsale_bancaagency.new_bussiness.RenewalPremiumNBBean;
import sbilife.com.pointofsale_bancaagency.posp_ra.POJO_POSP_RA_Rejection;
import sbilife.com.pointofsale_bancaagency.posp_ra.Pojo_POSP_RA;
import sbilife.com.pointofsale_bancaagency.rinnraksha.RinRakshaBean;
import sbilife.com.pointofsale_bancaagency.utility.M_DocumentUpload;
import sbilife.com.pointofsale_bancaagency.utility.M_DocumentUploadStatus;

class ValueNotInsertedException extends Exception {
    private static final long serialVersionUID = 1L;
    private final String s;


    ValueNotInsertedException(String exc) {
        s = exc;
    }

    @Override
    public String toString() {
        return (s);
    }

}


public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "posbancaagency008";
    private static final int DB_VERSION = 44;
    public final String EKYC_PS_CLAIMS_USER_ID = "user_id";
    public final String EKYC_PS_CLAIMS_USER_TYPE = "user_type";
    public final String EKYC_PS_CLAIMS_PROPOSAL_NUMBER = "proposal_number";
    public final String EKYC_PS_CLAIMS_CUSTOMER_NAME = "customer_name";
    public final String EKYC_PS_CLAIMS_CLIENT_ID = "client_id";
    public final String EKYC_PS_CLAIMS_PLAN_NAME = "plan_name";
    public final String EKYC_PS_CLAIMS_POLICY_STATUS = "policy_status";
    public final String EKYC_PS_CLAIMS_AADHAAR_NUMBER = "aadhaar_number";
    public final String EKYC_PS_CLAIMS_UID_TOKEN_ID = "uid_token_id";
    public final String EKYC_PS_CLAIMS_PAN = "pan_number";
    public final String EKYC_PS_CLAIMS_AP_FLAG = "ap_flag";
    public final String EKYC_PS_CLAIMS_MODE = "mode";
    public final String EKYC_PS_CLAIMS_EKYC_MODE = "ekyc_mode";
    public final String EKYC_PS_CLAIMS_EKYC_RESULT = "ekyc_result";
    public final String EKYC_PS_CLAIMS_APP_VERSION = "app_version";
    public final String EKYC_PS_CLAIMS_IS_DELETE = "is_delete";
    public final String EKYC_PS_CLAIMS_DATE_TIME = "date_time";
    public final String EKYC_PS_CLAIMS_EKYC_STATUS = "ekyc_status";
    public final String EKYC_PS_CLAIMS_EKYC_FLOW_TYPE = "ekyc_flow_type";
    public final String EKYC_PS_CLAIMS_EKYC_NA_URN = "ekyc_na_urn";
    // Table creation for agent on boarding by rajan 17-04-2018
    public final String TABLE_AGENT_ON_BOARDING = "T_AGENT_ON_BOARDING";
    public final String AGENT_ON_BOARDING_ID = "id";
    public final String AGENT_ON_BOARDING_AADHAAR_NO = "aadhaar_no";
    public final String AGENT_ON_BOARDING_AADHAAR_DETAILS = "aadhaar_details";
    public final String AGENT_ON_BOARDING_PAN_NO = "pan_no";
    public final String AGENT_ON_BOARDING_PAN_DETAILS = "pan_details";
    public final String AGENT_ON_BOARDING_BASIC_DETAILS = "basic_details";
    public final String AGENT_ON_BOARDING_PERSONAL_INFO = "personal_info";
    public final String AGENT_ON_BOARDING_OCCUPATION_INFO = "occupation_info";
    public final String AGENT_ON_BOARDING_NOMINATION_INFO = "nomination_info";
    public final String AGENT_ON_BOARDING_BANK_DETAILS = "bank_details";
    public final String AGENT_ON_BOARDING_FORM_1_A = "form_1_a";
    public final String AGENT_ON_BOARDING_EXAM_TRAINING_DETAILS = "exam_training_details";
    public final String AGENT_ON_BOARDING_BSM_INTERVIEW_QUE = "bsm_interview_que";
    public final String AGENT_ON_BOARDING_DECLARATION_CONDITIONS = "nominee_conditions_declarations";
    public final String AGENT_ON_BOARDING_DOCUMENTS_UPLOAD = "doc_upload";
    public final String AGENT_ON_BOARDING_CREATED_BY = "created_by";
    public final String AGENT_ON_BOARDING_UPDATED_BY = "updated_by";
    public final String AGENT_ON_BOARDING_CREATED_DATE = "created_date";//time in milisec
    public final String AGENT_ON_BOARDING_UPDATED_DATE = "updated_date";//time in milisec
    public final String AGENT_ON_BOARDING_SYNCH_STATUS = "synch_status";
    public final String AGENT_ON_BOARDING_ENROLLMENT_TYPE = "enrollment_type";
    public final String TCC_EXAM_DETAILS_URN_NUMBER = "urn_number";//1
    public final String TCC_EXAM_DETAILS_START_DATE = "start_date";//2
    public final String TCC_EXAM_DETAILS_END_DATE = "end_date";//3
    public final String TCC_EXAM_DETAILS_NO_HRS = "no_hrs";//4
    public final String TCC_EXAM_DETAILS_EXAM_EXISTING_CENTER = "exam_existing_center";//5
    public final String TCC_EXAM_DETAILS_EXAM_CENTER = "exam_center";//6
    public final String TCC_EXAM_DETAILS_PREFFERED_DATE = "preffered_date";//7
    public final String TCC_EXAM_DETAILS_SYNCH_STATUS = "synch_status";//8
    //Point of sale person - retail agency by rajan 08-05-2021
    public final String TABLE_POSP_RA = "T_POSP_RA";
    public final String POSP_RA_ID = "posp_ra_id";
    public final String POSP_RA_AADHAAR_NO = "posp_ra_aadhaar_no";
    public final String POSP_RA_AADHAAR_DETAILS = "posp_ra_aadhaar_details";
    public final String POSP_RA_PAN_NO = "posp_ra_pan_no";
    public final String POSP_RA_PAN_DETAILS = "posp_ra_pan_details";
    public final String POSP_RA_PERSONAL_INFO = "posp_ra_personal_info";
    public final String POSP_RA_OCCUPATION_INFO = "posp_ra_occupation_info";
    public final String POSP_RA_NOMINATION_INFO = "posp_ra_nomination_info";
    public final String POSP_RA_BANK_DETAILS = "posp_ra_bank_details";
    public final String POSP_RA_EXAM_TRAINING_DETAILS = "posp_ra_exam_training_details";
    public final String POSP_RA_TERMS_CONDITIONS_DECLARATION = "posp_ra_terms_conditions_declarations";
    public final String POSP_RA_DOCUMENTS_UPLOAD = "posp_ra_doc_upload";
    public final String POSP_RA_IRN = "posp_ra_irn";
    public final String POSP_RA_IN_APP_STATUS = "posp_ra_in_app_status";
    public final String POSP_RA_IN_APP_STATUS_REMARK = "posp_ra_in_app_status_remark";
    public final String POSP_RA_CREATED_BY = "posp_ra_created_by";
    public final String POSP_RA_UPDATED_BY = "posp_ra_updated_by";
    public final String POSP_RA_CREATED_DATE = "posp_ra_created_date";//time in milisec
    public final String POSP_RA_UPDATED_DATE = "posp_ra_updated_date";//time in milisec
    public final String POSP_RA_AGENCY_TYPE = "posp_ra_agency_type";
    public final String POSP_RA_ENROLLMENT_TYPE = "posp_ra_enrollment_type";
    public final String POSP_RA_FLAG_1 = "posp_ra_flag_1";
    public final String POSP_RA_FLAG_2 = "posp_ra_flag_2";
    public final String POSP_RA_FLAG_3 = "posp_ra_flag_3";
    public final String TABLE_POSP_RA_REJECTION = "T_POSP_RA_REJECTION";
    public final String POSP_RA_REQ_ID = "posp_ra_req_id";
    public final String POSP_RA_DATA_ID = "posp_ra_data_id";// table T_POSP_RA id
    public final String POSP_RA_REQ_PAN = "posp_ra_req_pan";//PAN
    public final String POSP_RA_REQ_IA_CODE = "posp_ra_req_ia_code";//IACODE
    public final String POSP_RA_REQ_RAISED = "posp_ra_requirement_raised";//REQUIREMENT_RAISED
    public final String POSP_RA_REQ_RAISED_REMARK = "posp_ra_requirement_raised_remark";// REMARKS

    // Sub Category Table
    public final String POSP_RA_REQ_RAISED_DOC_STATUS = "posp_ra_requirement_raised_document_status";// DOCUMENT_STATUS
    public final String POSP_RA_REQ_RAISED_STATUS = "posp_ra_requirement_raised_status";//REQUIREMENT_STATUS
    public final String POSP_RA_REQ_RAISED_DOC_NAME = "posp_ra_requirement_raised_document_name";//DOCUMENT_NAME
    public final String POSP_RA_REQ_RAISED_DOC_OPTION_VAL = "posp_ra_requirement_raised_document_opt_val";//DOCUMENT_OPTION_VALUE
    public final String POSP_RA_REQ_RAISED_UM_CODE = "posp_ra_requirement_raised_um_code";//DOCUMENT_OPTION_VALUE
    public final String POSP_RA_REQ_RAISED_ENROLLMENT_TYPE = "posp_ra_requirement_raised_enrollment_type";
    private final String LoginTable = "Login";
    private final String colLoginID = "LoginID";
    private final String colLoginTitle = "LoginTitle";
    private final String colLoginFirstName = "LoginFirstName";

    // Branch Table
    private final String colLoginLastName = "LoginLastName";
    private final String colLoginAddress = "LoginAddress";
    private final String colLoginStatus = "LoginStatus";
    private final String colLoginCIFNo = "LoginCIFNo";
    private final String colLoginPateName = "LoginPateName";
    private final String colLoginEmail = "LoginEmail";
    private final String colLoginPassword = "LoginPassword";
    private final String colLoginConfirmPassword = "LoginConfirmPassword";
    private final String colLoginQuestion = "LoginQuestion";
    private final String colLoginMobileNo = "LoginMobileNo";
    private final String colLoginDOB = "LoginDOB";
    private final String colLoginType = "LoginType";
    private final String colLoginPIN = "LoginPIN";
    private final String DateTable = "DateTable";
    private final String colDateID = "DateID";
    private final String colDateName = "DateName";
    private final String colEventName = "EventName";
    private final String colMonth = "Month";
    private final String colYear = "Year";
    private final String colTime = "Time";
    private final String colCalendarUserID = "CalendarUserID";
    private final String colNotes = "Notes";
    private final String VersionTable = "VersionTable";
    // bdm_tracker
    private final String DateTableBDMT = "DateTableBDMT";
    private final String colDateIDBDMT = "DateIDBDMT";
    private final String colDateNameBDMT = "DateNameBDMT";
    private final String colEventNameBDMT = "EventNameBDMT";
    private final String colMonthBDMT = "MonthBDMT";

    // for branch profile tables
    private final String colYearBDMT = "YearBDMT";
    private final String colTimeBDMT = "TimeBDMT";
    private final String colRemark = "Remark";
    private final String colActivity = "Activity";
    private final String colSubActivity = "SubActivity";
    private final String colTimeTo = "TimeTo";
    private final String colUserID = "UserID";
    private final String colActivityStatus = "ActivityStatus";
    private final String colActivitySync = "ActivitySync";
    private final String colActivityCreatedDate = "ActivityCreatedDate";
    private final String colActivityModifiedDate = "ActivityModifiedDate";
    private final String colActivityServerMasterId = "ActivityServerMasterId";
    private final String colActivityLead = "ActivityLead";
    private final String ActivityCategorySubCategoryTable = "ActivityCategorySubCategoryTable";
    private final String EmailTable = "EmailTable";
    private final String colEmailID = "EmailID";
    private final String colEmailType = "EmailType";
    private final String colEmailName = "EmailName";
    private final String colEmailCreatedDate = "EmailCreatedDate";
    private final String colEmailCreatedBy = "EmailCreatedBy";
    private final String colEmailModifiedDate = "EmailModifiedDate";
    private final String colEmailModifiedBy = "EmailModifiedBy";
    private final String colEmailUserId = "EmailUserId";
    private final String DateTableAR = "DateTableAR";
    private final String colDateIDAR = "DateIDAR";
    private final String colDateNameAR = "DateNameAR";
    private final String colEventNameAR = "EventNameAR";
    private final String colMonthAR = "MonthAR";
    private final String colYearAR = "YearAR";
    private final String colTimeAR = "TimeAR";
    private final String colRemarkAR = "RemarkAR";
    private final String colActivityAR = "ActivityAR";
    private final String colSubActivityAR = "SubActivityAR";
    private final String colTimeToAR = "TimeToAR";
    private final String colUserIDAR = "UserIDAR";
    private final String colActivitySyncAR = "ActivitySyncAR";
    private final String colActivityCreatedDateAR = "ActivityCreatedDateAR";
    private final String colActivityModifiedDateAR = "ActivityModifiedDateAR";
    private final String colActivityStatusAR = "ActivityStatusAR";
    private final String colActivityLeadAR = "ActivityLeadAR";
    // Category Table
    private final String ActivityCategoryTable = "ActivityCategoryTable";
    private final String colActivityCategoryID = "ActivityCategoryID";
    private final String colActivityCategoryName = "ActivityCategoryName";
    private final String colActivityCategoryDiscription = "ActivityCategoryDiscription";
    private final String colActivityCategoryParentId = "ActivityCategoryParentId";
    private final String colActivityCategoryCreatedDate = "colActivityCategoryCreatedDate";
    private final String colActivityCategoryCreatedBy = "colActivityCategoryCreatedBy";
    private final String colActivityCategoryModifiedDate = "colActivityCategoryModifiedDate";
    private final String colActivityCategoryModifiedBy = "colActivityCategoryModifiedBy";
    private final String ActivitySubCategoryTable = "ActivitySubCategoryTable";
    private final String colActivitySubCategoryID = "ActivitySubCategoryID";
    private final String colActivitySubCategoryName = "ActivitySubCategoryName";
    private final String colActivitySubCategoryDiscription = "ActivitySubCategoryDiscription";
    private final String colActivitySubCategoryParentId = "ActivitySubCategoryParentId";
    private final String colActivitySubCategoryCreatedDate = "colActivitySubCategoryCreatedDate";

    // Need Analysis Table
    private final String colActivitySubCategoryCreatedBy = "colActivitySubCategoryCreatedBy";
    private final String colActivitySubCategoryModifiedDate = "colActivitySubCategoryModifiedDate";
    private final String colActivitySubCategoryModifiedBy = "colActivitySubCategoryModifiedBy";
    private final String colActivitySubCategoryMasterId = "colActivitySubCategoryMasterId";
    private final String BranchTable = "BranchTable";
    private final String colBranchID = "BranchID";
    private final String colBranchName = "BranchName";
    private final String colBranchDiscription = "BranchDiscription";
    private final String colBranchParentId = "BranchParentId";
    private final String colBranchCreatedDate = "BranchCreatedDate";
    private final String colABranchCreatedBy = "BranchCreatedBy";
    private final String colBranchModifiedDate = "BranchModifiedDate";
    private final String colBranchModifiedBy = "BranchModifiedBy";
    private final String colBranchUserId = "BranchUserId";
    private final String ParamListTable = "ParamListTable";
    private final String colParamName = "ParamName";
    private final String colParamDiscription = "ParamDiscription";
    private final String colParamParentId = "ParamParentId";
    private final String colParamCreatedDate = "ParamCreatedDate";
    private final String colParamCreatedBy = "ParamCreatedBy";
    private final String colParamModifiedDate = "ParamModifiedDate";
    private final String colParamModifiedBy = "ParamModifiedBy";
    private final String DefineObjectiveTable = "DefineObjectiveTable";
    private final String colDefineObjectiveID = "DefineObjectiveID";
    private final String colDefineObjectiveBranchCode = "DefineObjectiveBranchCode";
    private final String colDefineObjectiveBranchName = "DefineObjectiveBranchName";
    private final String colDefineObjectiveNewBusCash = "DefineObjectiveNewBusCash";
    private final String colDefineObjectiveHomeLoan = "DefineObjectiveHomeLoan";
    private final String colDefineObjectiveNewBusPre = "DefineObjectiveNewBusPre";
    private final String colDefineObjectiveShare = "DefineObjectiveShare";
    private final String colDefineObjectiveRemark = "DefineObjectiveRemark";
    private final String colDefineObjectiveUserId = "DefineObjectiveUserId";
    private final String BranchProfileTable = "BranchProfileTable";
    private final String colBranchCode = "BranchCode";


    // HO Lead
    private final String colBranchProfileName = "BranchProfileName";
    private final String colBranchOpenDate = "BranchOpenDate";
    private final String colBranchNetResult = "BranchNetResult";
    private final String colBranchProfileCreatedDate = "BranchProfileCreatedDate";
    private final String BranchAdvancesTable = "BranchAdvancesTable";
    private final String colBranch_Advances_ID = "Branch_Advances_ID";
    private final String colBranch_Advances_BranchCode = "Branch_Advances_BranchCode";
    private final String colTot_no_of_Acc = "Tot_no_of_Acc";
    private final String colTot_outstanding = "Tot_outstanding";
    private final String colNo_of_acc_b1l = "No_of_acc_b1l";
    private final String colTot_outstanding_b1l = "Tot_outstanding_b1l";
    private final String colNo_of_acc_1lto5l = "No_of_acc_1lto5l";
    private final String colTot_outstanding_1lto5l = "Tot_outstanding_1lto5l";
    private final String colNo_of_acc_a5l = "No_of_acc_a5l";
    private final String colTot_outstanding_a5l = "Tot_outstanding_a5l";
    private final String colBranchAdvances_Category = "BranchAdvances_Category";
    private final String BranchDepositsTable = "BranchDepositsTable";
    private final String colBranch_Deposits_ID = "Branch_Deposits_ID";
    private final String colBranch_Deposits_BranchCode = "Branch_Deposits_BranchCode";
    private final String colTot_amount = "Tot_amount";

    // Renewal Premium table fields
    private final String colNew_acc_b1k = "New_acc_b1k";
    private final String colNew_bal_b1k = "New_bal_b1k";
    private final String colNew_acc_10kto1l = "New_acc_10kto1l";
    private final String colNew_bal_10kto1l = "New_bal_10kto1l";
    private final String colNew_acc_1lto5l = "New_acc_1lto5l";
    private final String colNew_bal_1lto5l = "New_bal_1lto5l";
    private final String colNew_acc_5landA = "New_acc_5landA";
    private final String colNew_bal_5landA = "New_bal_5landA";
    private final String colBranchDeposits_Category = "BranchDeposits_Category";
    private final String SBranchProfileTable = "SBranchProfileTable";
    private final String SyncBranchProfileTable = "SyncBranchProfileTable";
    private final String colSyncBranchCode = "SyncBranchCode";
    private final String colSyncBranchPerticular = "SyncBranchPerticular";
    private final String colSyncBranchCategory = "SyncBranchCategory";
    private final String colSyncBranchCrossSalingProduct = "SyncBranchCrossSalingProduct";
    private final String colSyncBranchCrossSalingPotenNops = "SyncBranchCrossSalingPotenNops";
    private final String colSyncBranchCrossSalingPotenPre = "SyncBranchCrossSalingPotenPre";
    private final String colSyncBranchUserId = "SyncBranchUserId";
    private final String DefineObjectiveSyncTable = "DefineObjectiveSyncTable";
    private final String colDefineObjectiveSyncBRCode = "DefineObjectiveSyncBRCode";
    private final String colDefineObjectiveSyncUserId = "DefineObjectiveSyncUserId";
    private final String colDefineObjectiveSyncFlag = "DefineObjectiveSyncFlag";
    // for agent professional profile
    private final String AgentProfileTable = "AgentProfileTable";
    private final String colAgentName = "AgentName";
    private final String colAgentLicenceNo = "AgentLicenceNo";
    private final String colAgentLicenceIssuDate = "AgentLicenceIssuDate";
    private final String colAgentLicenceExpiryDate = "AgentLicenceExpiryDate";
    private final String colAgentActivationStartDate = "AgentActivationStartDate";
    private final String colAgentProfileBranchName = "AgentProfileBranchName";
    private final String colAgentProfileRegion = "AgentProfileRegion";
    private final String colAgentProfileUMName = "AgentProfileUMName";
    private final String colAgentProfileStatus = "AgentProfileStatus";
    private final String colAgentProfileULIPCertified = "AgentProfileULIPCertified";
    private final String NeedAnalysisTable = "NeedAnalysisTable";
    private final String colQuotationNo = "QuotationNo";
    private final String colPlanSelected = "PlanSelected";
    private final String colProposalDate = "ProposalDate";
    private final String colMobileNo = "MobileNo";
    private final String colUinNo = "uinNo";
    private final String colSyncStatus = "syncStatus";
    private final String colCreatedDate = "CreatedDate";
    private final String colCreatedBy = "CreatedBy";

    /* tables of CIFEnrollment by rajan on 19-05-2017 */
    private final String colemail = "email";
    private final String colsr_code = "sr_code";
    private final String colsr_sr_code = "sr_sr_code";
    private final String colsr_type = "sr_type";
    private final String colsr_sr_type = "sr_sr_type";
    private final String colcust_title = "cust_title";
    private final String colcust_first_name = "cust_first_name";
    private final String colcust_mid_name = "cust_mid_name";
    private final String colcust_last_name = "cust_last_name";
    private final String colsumassured = "sumassured";
    private final String colpremium = "premium";

    /* Table Structure for DashBord Display */
    private final String colsr_email = "sr_email";
    private final String colsr_mobile = "sr_mobile";
    private final String colna_input = "na_input";
    private final String colna_output = "na_output";
    private final String colfrequency = "frequency";
    private final String colpolicyTerm = "policyTerm";
    private final String colprem_paying_term = "prem_paying_term";
    private final String colplan_code = "plan_code";
    private final String colLA_dob = "LA_dob";
    private final String colproposer_dob = "proposer_dob";
    private final String colna_group = "na_group";
    private final String col_transaction_mode = "col_transaction_mode";
    private final String col_bi_inputVal = "col_bi_inputVal";
    private final String col_bi_outputVal = "col_bi_outputVal";
    private final String HOLeadTable = "HOLeadTable";
    private final String colHOLeadID = "HOLeadID";
    private final String colHOLeadDate = "HOLeadDate";
    private final String colHOLeadCustomerId = "HOLeadCustomerId";
    private final String colHOLeadCustomerName = "HOLeadCustomerName";
    private final String colHOLeadPriority = "HOLeadPriority";
    private final String colHOLeadStatus = "HOLeadStatus";
    private final String colHOLeadSubStatus = "HOLeadSubStatus";
    private final String colHOLeadProposalNo = "HOLeadProposalNo";
    private final String colHOLeadFollowUpDate = "HOLeadFollowUpDate";
    private final String colHOLeadComments = "HOLeadComments";
    private final String colHOLeadAge = "HOLeadAge";
    private final String colHOLeadTotalAcc = "HOLeadTotalAcc";
    private final String colHOLeadBalance = "HOLeadBalance";
    private final String colHOLeadBranchCode = "HOLeadBranchCode";
    private final String colHOLeadUserID = "HOLeadUserID";
    private final String colHOLeadSync = "HOLeadSync";
    private final String colHOLeadServerID = "HOLeadServerID";
    private final String colHOLeadBDMName = "HOLeadBDMName";
    private final String colHOLeadSource = "HOLeadSource";
    private final String RenewalPremiumNewBusinessLogTable = "T_RP_NB_CHEQUE_DD_LOG";
    private final String rp_nb_id = "rp_nb_id";
    private final String rp_nb_policy_no = "rp_nb_policy_no";
    private final String rp_nb_proposal_no = "rp_nb_proposal_no";
    private final String rp_nb_cust_dob = "rp_nb_cust_dob";
    private final String rp_nb_cust_mob = "rp_nb_cust_mob";
    private final String rp_nb_cust_name = "rp_nb_cust_name";
    private final String rp_nb_micr = "rp_nb_micr";
    private final String rp_nb_accnt_no = "rp_nb_accnt_no";
    private final String rp_nb_cheque_no = "rp_nb_cheque_no";
    private final String rp_nb_cheque_date = "rp_nb_cheque_date";
    private final String rp_nb_cheque_amt = "rp_nb_cheque_amt";
    private final String rp_nb_bank_name = "rp_nb_bank_name";
    private final String rp_nb_branch_name = "rp_nb_branch_name";
    private final String rp_nb_pay_mode = "rp_nb_pay_mode";
    private final String rp_nb_pay_type = "rp_nb_pay_type";
    private final String rp_nb_payment_type = "rp_nb_payment_type";
    private final String rp_nb_advisor_code = "rp_nb_advisor_code";
    private final String rp_nb_advisor_type = "rp_nb_advisor_type";
    private final String rp_nb_isSync = "rp_nb_isSync";
    private final String rp_nb_isDeleted = "rp_nb_isDeleted";
    private final String rp_nb_created_date = "rp_nb_created_date";
    private final String rp_nb_created_by = "rp_nb_created_by";
    private final String rp_nb_is_rp = "rp_nb_is_rp";
    private final String C_CREATED_DATE = "CreatedDate";
    private final String C_CREATED_BY = "CreatedBy";
    private final String C_MODIFIED_DATE = "ModifiedDate";
    private final String C_MODIFIED_BY = "ModifiedBy";
    private final String cisSync = "isSync";
    private final String cisFlag1 = "isFlag1";
    private final String cisFlag2 = "isFlag2";
    private final String cisFlag3 = "isFlag3";
    private final String cisFlag4 = "isFlag4";
    private final String C_USER_ID = "user_id";
    /* Table for Document Upload Activity */
    private final String TABLE_DOCUMENT_UPLOAD = "T_document_upload";
    private final String C_DU_QUOTATION_NO = "du_quotation_no";
    private final String C_DU_PROPOSAL_NO = "du_proposal_no";
    private final String C_DU_PROOF_OF = "du_proof_of";
    private final String C_DU_DOCUMENT_NAME = "du_document_name";
    private final String C_DU_FILE_NAME = "du_File_name";
    private final String C_DU_DOCUMENT_CONTENT = "du_document_content";
    private final String C_DU_DOCUMENT_UPLOADDATE = "du_document_uploadDate";
    /**
     * Table Creation For State
     */
    private final String TABLE_STATE = "T_state";
    /**
     * Table Creation For City
     */
    private final String TABLE_CITY = "T_city";
    // Table creation for CIF
    private final String TABLE_CIF_ENROLLMENT = "T_cif_enrollment";
    private final String C_CIF_PF_NUMBER = "cif_pf_number";
    private final String C_CIF_QUOTATION_NO = "cif_quotation_no";
    private final String C_CIF_CANDIDATE_NAME = "cif_candidate_corporate_name";
    private final String C_CIF_MOBILE_NO = "cif_mobile_no";
    private final String C_CIF_EMAIL_ID = "cif_email_id";
    private final String C_CIF_PAN = "cif_pan";
    //added by rajan 24-10-2017
    private final String C_CIF_CONTACT_PERSON_EMAIL_ID = "cif_contact_email_id";
    private final String C_CIF_AADHAR_CARD_NO = "cif_aadhar_card_no";
    private final String TABLE_DASHBOARDDETAIL = "T_Dashboard_details";
    private final String C_DD_PENDING_STATUS = "dd_Pending_status";
    /**
     * Table Creation Exam center location
     */
    private final String TABLE_EXAM_CENTER_LOCATION = "T_exam_center_location";
    private final String C_CIF_EXAM_CENTER = "cif_exam_center";
    private final String TABLE_SYNC_STATUS = "T_Sync_status";
    private final String C_SS_SYNC_STATUS = "ps_Sync_status";
    // Table creation for CIF
    private final String TABLE_RIN_RAKSHA_BI = "T_RIN_RAKSHA_BI";
    private final String RIN_RAKSHA_BI_QUOTATION_NO = "rinraksha_quotation_no";
    private final String RIN_RAKSHA_BI_INPUT = "rinraksha_input";
    private final String RIN_RAKSHA_BI_OUTPUT = "rinraksha_output";
    private final String RIN_RAKSHA_BI_DEL_FLAG = "rinraksha_del_flag";
    private final String RIN_RAKSHA_BI_CODE = "rinraksha_code";
    private final String RIN_RAKSHA_BI_TYPE = "rinraksha_type";
    private final String RIN_RAKSHA_BI_URN = "rinraksha_urn";
    private final String RIN_RAKSHA_BI_BORROWER1_URN = "rinraksha_borrower1_urn";
    private final String RIN_RAKSHA_BI_BORROWER2_URN = "rinraksha_borrower2_urn";
    private final String RIN_RAKSHA_BI_BORROWER3_URN = "rinraksha_borrower3_urn";
    // Table creation for Group Feedback
    private final String TABLE_GROUP_FEEDBACK = "T_GROUP_FEEDBACK";
    private final String GROUP_FEEDBACK_ID = "id";
    private final String GROUP_FEEDBACK_USER_ID = "user_id";
    private final String GROUP_FEEDBACK_MAIN_TYPE = "main_type";
    private final String GROUP_FEEDBACK_SUB_TYPE = "sub_type";
    private final String GROUP_FEEDBACK_POLICY_NUMBER = "policy_number";
    private final String GROUP_FEEDBACK_MAIN_FEEDBACK = "feedback";
    private final String GROUP_FEEDBACK_DATE = "date";
    private final String GROUP_FEEDBACK_IS_DELETE = "is_delete";
    private final String GROUP_FEEDBACK_IS_SYNCH = "is_synch";
    private final String CREATE_TABLE_GROUP_FEEDBACK = " CREATE TABLE "
            + TABLE_GROUP_FEEDBACK + " ( "
            + GROUP_FEEDBACK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + GROUP_FEEDBACK_USER_ID + " TEXT not null, "
            + GROUP_FEEDBACK_MAIN_TYPE + " TEXT, "
            + GROUP_FEEDBACK_SUB_TYPE + " TEXT, "
            + GROUP_FEEDBACK_POLICY_NUMBER + " TEXT, "
            + GROUP_FEEDBACK_MAIN_FEEDBACK + " TEXT, "
            + GROUP_FEEDBACK_DATE + " TEXT, "
            + GROUP_FEEDBACK_IS_DELETE + " INTEGER, "
            + GROUP_FEEDBACK_IS_SYNCH + " INTEGER )";
    // Table creation for eKYC for PS and Claims
    private final String TABLE_EKYC_PS_CLAIMS = "T_EKYC_PS_CLAIMS";
    private final String EKYC_PS_CLAIMS_ID = "id";
    private final String CREATE_TABLE_EKYC_PS_CLAIMS = " CREATE TABLE "
            + TABLE_EKYC_PS_CLAIMS + " ( "
            + EKYC_PS_CLAIMS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "//0
            + EKYC_PS_CLAIMS_USER_ID + " INTEGER, "//1
            + EKYC_PS_CLAIMS_USER_TYPE + " TEXT, "//2
            + EKYC_PS_CLAIMS_PROPOSAL_NUMBER + " TEXT, "//3
            + EKYC_PS_CLAIMS_CUSTOMER_NAME + " TEXT, "//4
            + EKYC_PS_CLAIMS_CLIENT_ID + " TEXT, "//5
            + EKYC_PS_CLAIMS_PLAN_NAME + " TEXT, "//6
            + EKYC_PS_CLAIMS_POLICY_STATUS + " TEXT, "//7
            + EKYC_PS_CLAIMS_AADHAAR_NUMBER + " TEXT, "//8
            + EKYC_PS_CLAIMS_UID_TOKEN_ID + " TEXT, "//9
            + EKYC_PS_CLAIMS_PAN + " TEXT, "//10
            + EKYC_PS_CLAIMS_AP_FLAG + " TEXT, "//11
            + EKYC_PS_CLAIMS_MODE + " TEXT, "//12
            + EKYC_PS_CLAIMS_EKYC_MODE + " TEXT, "//13
            + EKYC_PS_CLAIMS_EKYC_RESULT + " TEXT, "//14
            + EKYC_PS_CLAIMS_APP_VERSION + " TEXT, "//15
            + EKYC_PS_CLAIMS_IS_DELETE + " INTEGER, "//16
            + EKYC_PS_CLAIMS_DATE_TIME + " TEXT, " //17
            + EKYC_PS_CLAIMS_EKYC_STATUS + " INTEGER, " //18
            + EKYC_PS_CLAIMS_EKYC_NA_URN + " TEXT, " //19
            + EKYC_PS_CLAIMS_EKYC_FLOW_TYPE + " TEXT )";//20
    private final String CREATE_TABLE_AGENT_ON_BOARDING = " CREATE TABLE "
            + TABLE_AGENT_ON_BOARDING + " ( "
            + AGENT_ON_BOARDING_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "//0
            + AGENT_ON_BOARDING_AADHAAR_NO + " TEXT , "//1
            + AGENT_ON_BOARDING_AADHAAR_DETAILS + " TEXT, "//2
            + AGENT_ON_BOARDING_PAN_NO + " TEXT, "//3
            + AGENT_ON_BOARDING_PAN_DETAILS + " TEXT, "//4
            + AGENT_ON_BOARDING_BASIC_DETAILS + " TEXT, "//5
            + AGENT_ON_BOARDING_PERSONAL_INFO + " TEXT, "//6
            + AGENT_ON_BOARDING_OCCUPATION_INFO + " TEXT, "//7
            + AGENT_ON_BOARDING_NOMINATION_INFO + " TEXT, "//8
            + AGENT_ON_BOARDING_BANK_DETAILS + " TEXT, "//9
            + AGENT_ON_BOARDING_FORM_1_A + " TEXT, "//10
            + AGENT_ON_BOARDING_EXAM_TRAINING_DETAILS + " TEXT, "//11
            + AGENT_ON_BOARDING_BSM_INTERVIEW_QUE + " TEXT, "//12
            + AGENT_ON_BOARDING_DECLARATION_CONDITIONS + " TEXT, "//13
            + AGENT_ON_BOARDING_DOCUMENTS_UPLOAD + " TEXT, "//14
            + AGENT_ON_BOARDING_CREATED_BY + " TEXT, "//15
            + AGENT_ON_BOARDING_UPDATED_BY + " TEXT, "//16
            + AGENT_ON_BOARDING_CREATED_DATE + " TEXT, "//17 //time in milisec
            + AGENT_ON_BOARDING_UPDATED_DATE + " TEXT, "//18 //time in milisec
            + AGENT_ON_BOARDING_SYNCH_STATUS + " TEXT, "//19
            + AGENT_ON_BOARDING_ENROLLMENT_TYPE + " TEXT ) ";//20
    //Table Creation for TCC and Exam details
    /*table TCC and exam details 27-03-2018 by rajan*/
    private final String TABLE_TCC_EXAM_DETAILS = "T_TCC_EXAM_DETAILS";
    private final String TCC_EXAM_DETAILS_ID = "id";//0
    private final String TABLE_SHORT_MHR_PROPOSAL_DETAILS = "T_SHORT_MHR_PROPOSAL_DETAILS";
    private final String SHORT_MHR_PROPOSAL_DETAILS_ID = "id";//0
    private final String SHORT_MHR_PROPOSAL_NUMBER = "short_mhr_prop_number";
    private final String SHORT_MHR_FROM_DATE = "short_mhr_from_date";
    private final String SHORT_MHR_TO_DATE = "short_mhr_to_date";
    private final String CREATE_TABLE_TCC_EXAM_DETAILS = " CREATE TABLE " + TABLE_TCC_EXAM_DETAILS
            + " ( " + TCC_EXAM_DETAILS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + TCC_EXAM_DETAILS_URN_NUMBER + " TEXT, "
            + TCC_EXAM_DETAILS_START_DATE + " TEXT, "
            + TCC_EXAM_DETAILS_END_DATE + " TEXT, "
            + TCC_EXAM_DETAILS_NO_HRS + " TEXT, "
            + TCC_EXAM_DETAILS_EXAM_EXISTING_CENTER + " TEXT, "
            + TCC_EXAM_DETAILS_EXAM_CENTER + " TEXT, "
            + TCC_EXAM_DETAILS_PREFFERED_DATE + " TEXT, "
            + TCC_EXAM_DETAILS_SYNCH_STATUS + " TEXT ) ";
    private final String CREATE_TABLE_SHORT_MHR_PROPOSAL_DETAILS = " CREATE TABLE " + TABLE_SHORT_MHR_PROPOSAL_DETAILS
            + " ( " + SHORT_MHR_PROPOSAL_DETAILS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + SHORT_MHR_PROPOSAL_NUMBER + " TEXT NOT NULL UNIQUE, "
            + SHORT_MHR_FROM_DATE + " TEXT, "
            + SHORT_MHR_TO_DATE + " TEXT) ";
    private final String TABLE_COVID_SELF_DECLARATION = "T_COVID_SELF_DECLARATION";
    private final String COVID_SELF_DECLARATION_ID = "id";//0
    private final String COVID_SELF_DECLARATION_IA_CODE = "covid_self_declaration_ia_code";//0
    private final String COVID_SELF_DECLARATION_XML_STRING = "covid_self_declaration_xml_string";//0
    private final String COVID_SELF_DECLARATION_DATE = "covid_self_declaration_date";//0
    private final String COVID_SELF_DECLARATION_TIME = "covid_self_declaration_time";//0
    private final String CREATE_TABLE_COVID_SELF_DECLARATION = " CREATE TABLE " + TABLE_COVID_SELF_DECLARATION
            + " ( " + COVID_SELF_DECLARATION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COVID_SELF_DECLARATION_IA_CODE + " TEXT, "
            + COVID_SELF_DECLARATION_DATE + " TEXT, "
            + COVID_SELF_DECLARATION_XML_STRING + " TEXT, "
            + COVID_SELF_DECLARATION_TIME + " TEXT) ";
    private final String CREATE_TABLE_POSP_RA = " CREATE TABLE "
            + TABLE_POSP_RA + " ( "
            + POSP_RA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "//0
            + POSP_RA_AADHAAR_NO + " TEXT , "//1
            + POSP_RA_AADHAAR_DETAILS + " TEXT, "//2
            + POSP_RA_PAN_NO + " TEXT, "//3
            + POSP_RA_PAN_DETAILS + " TEXT, "//4
            + POSP_RA_PERSONAL_INFO + " TEXT, "//5
            + POSP_RA_OCCUPATION_INFO + " TEXT, "//6
            + POSP_RA_NOMINATION_INFO + " TEXT, "//7
            + POSP_RA_BANK_DETAILS + " TEXT, "//8
            + POSP_RA_EXAM_TRAINING_DETAILS + " TEXT, "//9
            + POSP_RA_TERMS_CONDITIONS_DECLARATION + " TEXT, "//10
            + POSP_RA_DOCUMENTS_UPLOAD + " TEXT, "//11
            + POSP_RA_IRN + " TEXT, " //12
            + POSP_RA_IN_APP_STATUS + " TEXT, " //13
            + POSP_RA_IN_APP_STATUS_REMARK + " TEXT, " //14
            + POSP_RA_CREATED_BY + " TEXT, "//15
            + POSP_RA_UPDATED_BY + " TEXT, "//16
            + POSP_RA_CREATED_DATE + " TEXT, "//17 //time in milisec
            + POSP_RA_UPDATED_DATE + " TEXT, "//18 //time in milisec
            + POSP_RA_AGENCY_TYPE + " TEXT, "//19 to check customer type e.g - LM Or POSP-RA or IA-Upgrade
            + POSP_RA_ENROLLMENT_TYPE + " TEXT, " // 20 enrollment type "New" and IAUpgrade
            + POSP_RA_FLAG_1 + " TEXT, " //21
            + POSP_RA_FLAG_2 + " TEXT, " //22
            + POSP_RA_FLAG_3 + "TEXT );"; //23
    private final String CREATE_TABLE_POSP_RA_REJECTION = " CREATE TABLE "
            + TABLE_POSP_RA_REJECTION + " ( "
            + POSP_RA_REQ_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "//0
            + POSP_RA_DATA_ID + " INTEGER, "//1
            + POSP_RA_REQ_PAN + " TEXT , "//2
            + POSP_RA_REQ_IA_CODE + " TEXT, "//3
            + POSP_RA_REQ_RAISED + " TEXT, "//4
            + POSP_RA_REQ_RAISED_REMARK + " TEXT, "//5
            + POSP_RA_REQ_RAISED_DOC_STATUS + " TEXT, "//6
            + POSP_RA_REQ_RAISED_STATUS + " TEXT, "//7
            + POSP_RA_REQ_RAISED_DOC_NAME + " TEXT, "//8
            + POSP_RA_REQ_RAISED_DOC_OPTION_VAL + " TEXT, "//9
            + POSP_RA_REQ_RAISED_UM_CODE + " TEXT, "//10
            + POSP_RA_REQ_RAISED_ENROLLMENT_TYPE + " TEXT );"; //11
    /*
     * these are all table related static string column name
     */
    private SQLiteDatabase db;

    /*
     * these are all table related static string column name
     */
    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    /*
     * create table based on sql
     */

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {

            db.execSQL("CREATE TABLE " + LoginTable + " (" + colLoginID
                    + " INTEGER PRIMARY KEY AUTOINCREMENT, " + colLoginTitle
                    + " TEXT, " + colLoginFirstName + " TEXT, "
                    + colLoginLastName + " TEXT , " + colLoginAddress
                    + " TEXT , " + colLoginStatus + " TEXT," + colLoginCIFNo
                    + " TEXT," + colLoginPateName + " TEXT," + colLoginEmail
                    + " TEXT," + colLoginPassword + " TEXT,"
                    + colLoginConfirmPassword + " TEXT," + colLoginQuestion
                    + " TEXT," + colLoginMobileNo + " TEXT," + colLoginDOB
                    + " TEXT," + colLoginType + " TEXT," + colLoginPIN
                    + " TEXT)");

            String colProposerName = "ProposerName";
            db.execSQL("CREATE TABLE " + NeedAnalysisTable + " ("
                    + "ProductBIID" + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + colProposerName + " TEXT, " + colQuotationNo + " TEXT, "
                    + colPlanSelected + " TEXT, " + colProposalDate + " TEXT, "
                    + colMobileNo + " TEXT, " + colUinNo + " TEXT, "
                    + colSyncStatus + " TEXT, " + colCreatedDate + " TEXT, "
                    + colCreatedBy + " TEXT, " + colemail

                    + " TEXT, " + colsr_code + " TEXT, " + colsr_sr_code
                    + " TEXT, " + colsr_type + " TEXT, " + colsr_sr_type
                    + " TEXT, " + colcust_title + " TEXT, "
                    + colcust_first_name + " TEXT, " + colcust_mid_name
                    + " TEXT, " + colcust_last_name + " TEXT, " + colsumassured
                    + " TEXT, " + colpremium + " TEXT, " + colsr_email
                    + " TEXT, " + colsr_mobile + " TEXT, " + colna_input
                    + " TEXT, " + colna_output + " TEXT, " + colfrequency
                    + " TEXT, " + colpolicyTerm + " TEXT, "
                    + colprem_paying_term + " TEXT, " + colplan_code
                    + " TEXT, " + colLA_dob + " TEXT, "
                    + colproposer_dob + " TEXT, "
                    + colna_group + " TEXT, "
                    + col_transaction_mode + " TEXT, "
                    + col_bi_inputVal + " TEXT, "
                    + col_bi_outputVal + " TEXT)");

            db.execSQL("CREATE TABLE " + DateTable + " (" + colDateID
                    + " INTEGER PRIMARY KEY AUTOINCREMENT, " + colDateName
                    + " TEXT, " + colEventName + " TEXT, " + colMonth
                    + " TEXT," + colYear + " TEXT," + colTime + " TEXT,"
                    + colCalendarUserID + " TEXT," + colNotes + " TEXT)");

            String c_DU_FOREIGN_KEY = "fk_nbm_master_key";
            String c_DU_MASTER_KEY = "du_master_key";
            String SEPERATOR = " , ";
            String CLOSE = " )";
            String OPEN = " ( ";
            String UNIQUE = " UNIQUE ";
            String ON_CONFLICT = "ON CONFLICT REPLACE";
            String CREATE_TABLE_DOCUMENT_UPLOAD = " create table "
                    + TABLE_DOCUMENT_UPLOAD + OPEN + c_DU_MASTER_KEY
                    + " INTEGER PRIMARY KEY AUTOINCREMENT, " + c_DU_FOREIGN_KEY
                    + " TEXT, " + C_DU_QUOTATION_NO + " TEXT, " + C_USER_ID + " TEXT, "
                    + C_DU_PROPOSAL_NO + " TEXT, " + C_DU_PROOF_OF + " TEXT, "
                    + C_DU_DOCUMENT_NAME + " TEXT, " + C_DU_FILE_NAME + " TEXT, "
                    + C_DU_DOCUMENT_CONTENT + " TEXT, " + C_DU_DOCUMENT_UPLOADDATE
                    + " TEXT, " + C_CREATED_BY + " TEXT, " + C_CREATED_DATE + " TEXT, "
                    + C_MODIFIED_BY + " TEXT, " + C_MODIFIED_DATE + " TEXT, " + cisSync
                    + " BOOLEAN, " + cisFlag1 + " BOOLEAN, " + cisFlag2 + " BOOLEAN, "
                    + cisFlag3 + " BOOLEAN, " + cisFlag4 + " BOOLEAN, " + UNIQUE + OPEN
                    + C_DU_PROPOSAL_NO + SEPERATOR + C_DU_PROOF_OF + SEPERATOR
                    + C_DU_DOCUMENT_NAME + SEPERATOR + C_DU_FILE_NAME + CLOSE
                    + ON_CONFLICT + " )";
            db.execSQL(CREATE_TABLE_DOCUMENT_UPLOAD);

            String colReleasedDate = "ReleasedDate";
            String colVersionName = "VersionName";
            String colVersionID = "VersionID";
            db.execSQL("CREATE TABLE " + VersionTable + " (" + colVersionID
                    + " INTEGER PRIMARY KEY AUTOINCREMENT, " + colVersionName
                    + " TEXT, " + colReleasedDate + " TEXT)");

            // bdm_tracker

            db.execSQL("CREATE TABLE " + DateTableBDMT + " (" + colDateIDBDMT
                    + " INTEGER PRIMARY KEY AUTOINCREMENT, " + colDateNameBDMT
                    + " TEXT, " + colEventNameBDMT + " TEXT, " + colMonthBDMT
                    + " TEXT," + colYearBDMT + " TEXT," + colTimeBDMT
                    + " TEXT," + colRemark + " TEXT," + colActivity + " TEXT,"
                    + colSubActivity + " TEXT," + colTimeTo + " TEXT,"
                    + colUserID + " TEXT," + colActivityStatus + " TEXT,"
                    + colActivitySync + " TEXT," + colActivityCreatedDate
                    + " TEXT," + colActivityModifiedDate + " TEXT,"
                    + colActivityServerMasterId + " TEXT," + colActivityLead
                    + " TEXT)");

            String colActivityCategorySubCategoryeModifiedBy = "colActivityCategorySubCategoryeModifiedBy";
            String colActivityCategorySubCategoryeModifiedDate = "colActivityCategorySubCategoryeModifiedDate";
            String colActivityCategorySubCategoryeCreatedBy = "colActivityCategorySubCategoryeCreatedBy";
            String colActivityCategorySubCategoryeCreatedDate = "colActivityCategorySubCategoryeCreatedDate";
            String colActivityCategorySubCategoryeDiscription = "ActivityCategorySubCategoryeDiscription";
            String colActivityCategorySubCategoryID = "ActivityCategorySubCategoryID";
            String colActivityCategorySubCategoryeParentId = "ActivityCategorySubCategoryeParentId";
            String colActivityCategorySubCategoryName = "ActivityCategorySubCategoryName";
            db.execSQL("CREATE TABLE " + ActivityCategorySubCategoryTable
                    + " (" + colActivityCategorySubCategoryID
                    + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + colActivityCategorySubCategoryName + " TEXT, "
                    + colActivityCategorySubCategoryeDiscription + " TEXT, "
                    + colActivityCategorySubCategoryeParentId + " TEXT,"
                    + colActivityCategorySubCategoryeCreatedDate + " TEXT,"
                    + colActivityCategorySubCategoryeCreatedBy + " TEXT,"
                    + colActivityCategorySubCategoryeModifiedDate + " TEXT,"
                    + colActivityCategorySubCategoryeModifiedBy + " TEXT)");

            db.execSQL("CREATE TABLE " + EmailTable + " (" + colEmailID
                    + " INTEGER PRIMARY KEY AUTOINCREMENT, " + colEmailType
                    + " TEXT, " + colEmailName + " TEXT, "
                    + colEmailCreatedDate + " TEXT," + colEmailCreatedBy
                    + " TEXT," + colEmailModifiedDate + " TEXT,"
                    + colEmailModifiedBy + " TEXT," + colEmailUserId + " TEXT)");

            db.execSQL("CREATE TABLE " + DateTableAR + " (" + colDateIDAR
                    + " INTEGER PRIMARY KEY AUTOINCREMENT, " + colDateNameAR
                    + " TEXT, " + colEventNameAR + " TEXT, " + colMonthAR
                    + " TEXT," + colYearAR + " TEXT," + colTimeAR + " TEXT,"
                    + colRemarkAR + " TEXT," + colActivityAR + " TEXT,"
                    + colSubActivityAR + " TEXT," + colTimeToAR + " TEXT,"
                    + colUserIDAR + " TEXT," + colActivitySyncAR + " TEXT,"
                    + colActivityCreatedDateAR + " TEXT,"
                    + colActivityModifiedDateAR + " TEXT,"
                    + colActivityStatusAR + " TEXT," + colActivityLeadAR
                    + " TEXT)");

            // Create Category Table

            db.execSQL("CREATE TABLE " + ActivityCategoryTable + " ("
                    + colActivityCategoryID
                    + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + colActivityCategoryName + " TEXT, "
                    + colActivityCategoryDiscription + " TEXT, "
                    + colActivityCategoryParentId + " TEXT,"
                    + colActivityCategoryCreatedDate + " TEXT,"
                    + colActivityCategoryCreatedBy + " TEXT,"
                    + colActivityCategoryModifiedDate + " TEXT,"
                    + colActivityCategoryModifiedBy + " TEXT)");

            // Create Sub Category Table

            db.execSQL("CREATE TABLE " + ActivitySubCategoryTable + " ("
                    + colActivitySubCategoryID
                    + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + colActivitySubCategoryName + " TEXT, "
                    + colActivitySubCategoryDiscription + " TEXT, "
                    + colActivitySubCategoryParentId + " TEXT,"
                    + colActivitySubCategoryCreatedDate + " TEXT,"
                    + colActivitySubCategoryCreatedBy + " TEXT,"
                    + colActivitySubCategoryModifiedDate + " TEXT,"
                    + colActivitySubCategoryModifiedBy + " TEXT,"
                    + colActivitySubCategoryMasterId + " TEXT)");

            // Create Branch Table

            db.execSQL("CREATE TABLE " + BranchTable + " (" + colBranchID
                    + " INTEGER PRIMARY KEY AUTOINCREMENT, " + colBranchName
                    + " TEXT, " + colBranchDiscription + " TEXT, "
                    + colBranchParentId + " TEXT," + colBranchCreatedDate
                    + " TEXT," + colABranchCreatedBy + " TEXT,"
                    + colBranchModifiedDate + " TEXT," + colBranchModifiedBy
                    + " TEXT," + colBranchUserId + " TEXT)");

            String colParamID = "ParamID";
            db.execSQL("CREATE TABLE " + ParamListTable + " (" + colParamID
                    + " INTEGER PRIMARY KEY AUTOINCREMENT, " + colParamName
                    + " TEXT, " + colParamDiscription + " TEXT, "
                    + colParamParentId + " TEXT," + colParamCreatedDate
                    + " TEXT," + colParamCreatedBy + " TEXT,"
                    + colParamModifiedDate + " TEXT," + colParamModifiedBy
                    + " TEXT)");

            db.execSQL("CREATE TABLE " + DefineObjectiveTable + " ("
                    + colDefineObjectiveID
                    + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + colDefineObjectiveBranchCode + " TEXT, "
                    + colDefineObjectiveBranchName + " TEXT, "
                    + colDefineObjectiveNewBusCash + " TEXT,"
                    + colDefineObjectiveHomeLoan + " TEXT,"
                    + colDefineObjectiveNewBusPre + " TEXT,"
                    + colDefineObjectiveShare + " TEXT,"
                    + colDefineObjectiveRemark + " TEXT,"
                    + colDefineObjectiveUserId + " TEXT)");

            // for branch profile menu

            String colBranchProfileID = "BranchProfileID";
            db.execSQL("CREATE TABLE " + BranchProfileTable + " ("
                    + colBranchProfileID
                    + " INTEGER PRIMARY KEY AUTOINCREMENT, " + colBranchCode
                    + " TEXT, " + colBranchProfileName + " TEXT, "
                    + colBranchOpenDate + " TEXT," + colBranchNetResult
                    + " TEXT," + colBranchProfileCreatedDate + " TEXT)");

            String colBranchAdvancesID = "BranchAdvancesID";
            db.execSQL("CREATE TABLE " + BranchAdvancesTable + " ("
                    + colBranchAdvancesID
                    + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + colBranch_Advances_ID + " TEXT, "
                    + colBranch_Advances_BranchCode + " TEXT, "
                    + colTot_no_of_Acc + " TEXT," + colTot_outstanding
                    + " TEXT," + colNo_of_acc_b1l + " TEXT,"
                    + colTot_outstanding_b1l + " TEXT," + colNo_of_acc_1lto5l
                    + " TEXT," + colTot_outstanding_1lto5l + " TEXT,"
                    + colNo_of_acc_a5l + " TEXT," + colTot_outstanding_a5l
                    + " TEXT," + colBranchAdvances_Category + " TEXT)");

            String colBranchDepositsID = "BranchDepositsID";
            db.execSQL("CREATE TABLE " + BranchDepositsTable + " ("
                    + colBranchDepositsID
                    + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + colBranch_Deposits_ID + " TEXT, "
                    + colBranch_Deposits_BranchCode + " TEXT, " + colTot_amount
                    + " TEXT," + colNew_acc_b1k + " TEXT," + colNew_bal_b1k
                    + " TEXT," + colNew_acc_10kto1l + " TEXT,"
                    + colNew_bal_10kto1l + " TEXT," + colNew_acc_1lto5l
                    + " TEXT," + colNew_bal_1lto5l + " TEXT,"
                    + colNew_acc_5landA + " TEXT," + colNew_bal_5landA
                    + " TEXT ," + colBranchDeposits_Category + " TEXT)");

            String colSBranchProfileID = "SBranchProfileID";
            String colSBranchUserId = "SBranchUserId";
            String colSBranchCrossSalingPotenPre = "SBranchCrossSalingPotenPre";
            String colSBranchCrossSalingPotenNops = "SBranchCrossSalingPotenNops";
            String colSBranchCrossSalingProduct = "SBranchCrossSalingProduct";
            String colSBranchNonRetailAccount = "SBranchNonRetailAccount";
            String colSBranchRetailAccount = "SBranchRetailAccount";
            String colSBranchCategory = "SBranchCategory";
            String colSBranchPerticular = "SBranchPerticular";
            String colSBranchCode = "SBranchCode";
            db.execSQL("CREATE TABLE " + SBranchProfileTable + " ("
                    + colSBranchProfileID
                    + " INTEGER PRIMARY KEY AUTOINCREMENT, " + colSBranchCode
                    + " TEXT, " + colSBranchPerticular + " TEXT, "
                    + colSBranchCategory + " TEXT," + colSBranchRetailAccount
                    + " TEXT," + colSBranchNonRetailAccount + " TEXT,"
                    + colSBranchCrossSalingProduct + " TEXT,"
                    + colSBranchCrossSalingPotenNops + " TEXT,"
                    + colSBranchCrossSalingPotenPre + " TEXT,"
                    + colSBranchUserId + " TEXT)");

            String colSyncBranchProfileID = "SyncBranchProfileID";
            db.execSQL("CREATE TABLE " + SyncBranchProfileTable + " ("
                    + colSyncBranchProfileID
                    + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + colSyncBranchCode + " TEXT, " + colSyncBranchPerticular
                    + " TEXT, " + colSyncBranchCategory + " TEXT,"
                    + colSyncBranchCrossSalingProduct + " TEXT,"
                    + colSyncBranchCrossSalingPotenNops + " TEXT,"
                    + colSyncBranchCrossSalingPotenPre + " TEXT,"
                    + colSyncBranchUserId + " TEXT)");

            String colDefineObjectiveSyncID = "DefineObjectiveSyncID";
            db.execSQL("CREATE TABLE " + DefineObjectiveSyncTable + " ("
                    + colDefineObjectiveSyncID
                    + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + colDefineObjectiveSyncBRCode + " TEXT, "
                    + colDefineObjectiveSyncUserId + " TEXT, "
                    + colDefineObjectiveSyncFlag + " TEXT)");

            // for agent profile

            String colAgenthProfileID = "AgentProfileID";
            db.execSQL("CREATE TABLE " + AgentProfileTable + " ("
                    + colAgenthProfileID
                    + " INTEGER PRIMARY KEY AUTOINCREMENT, " + colAgentName
                    + " TEXT, " + colAgentLicenceNo + " TEXT, "
                    + colAgentLicenceIssuDate + " TEXT,"
                    + colAgentLicenceExpiryDate + " TEXT,"
                    + colAgentActivationStartDate + " TEXT,"
                    + colAgentProfileBranchName + " TEXT,"
                    + colAgentProfileRegion + " TEXT," + colAgentProfileUMName
                    + " TEXT," + colAgentProfileStatus + " TEXT,"
                    + colAgentProfileULIPCertified + " TEXT)");

            // HO Lead

            db.execSQL("CREATE TABLE " + HOLeadTable + " (" + colHOLeadID
                    + " INTEGER PRIMARY KEY AUTOINCREMENT, " + colHOLeadDate
                    + " TEXT, " + colHOLeadCustomerId + " TEXT, "
                    + colHOLeadCustomerName + " TEXT," + colHOLeadPriority
                    + " TEXT," + colHOLeadStatus + " TEXT,"
                    + colHOLeadSubStatus + " TEXT," + colHOLeadProposalNo
                    + " TEXT," + colHOLeadFollowUpDate + " TEXT,"
                    + colHOLeadComments + " TEXT," + colHOLeadAge + " TEXT,"
                    + colHOLeadTotalAcc + " TEXT," + colHOLeadBalance
                    + " TEXT," + colHOLeadBranchCode + " TEXT,"
                    + colHOLeadUserID + " TEXT," + colHOLeadSync + " TEXT,"
                    + colHOLeadServerID + " TEXT," + colHOLeadBDMName
                    + " TEXT," + colHOLeadSource + " TEXT)");

            // Renewal Premium NB Table

            db.execSQL("CREATE TABLE " + RenewalPremiumNewBusinessLogTable
                    + "(" + rp_nb_id + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + rp_nb_policy_no + " TEXT, " + rp_nb_proposal_no
                    + " TEXT, " + rp_nb_cust_dob + " TEXT, " + rp_nb_cust_mob
                    + " TEXT, " + rp_nb_micr + " TEXT, " + rp_nb_cust_name
                    + " TEXT, " + rp_nb_accnt_no + " TEXT, " + rp_nb_cheque_no
                    + " TEXT, " + rp_nb_cheque_date + " TEXT, "
                    + rp_nb_cheque_amt + " TEXT, " + rp_nb_bank_name
                    + " TEXT, " + rp_nb_branch_name + " TEXT, "
                    + rp_nb_pay_mode + " TEXT, " + rp_nb_pay_type + " TEXT, "
                    + rp_nb_payment_type + " TEXT, " + rp_nb_advisor_code
                    + " TEXT, " + rp_nb_advisor_type + " TEXT, " + rp_nb_isSync
                    + " TEXT NOT NULL DEFAULT '0', " + rp_nb_isDeleted
                    + " TEXT NOT NULL DEFAULT '0', " + rp_nb_created_date
                    + " TEXT, " + rp_nb_created_by + " TEXT, " + rp_nb_is_rp
                    + " TEXT)");

            /* CIFEnrollment tables */

            String c_STATE_NAME = "state_name";
            String c_STATE_ID = "state_id";
            String c_STATE_AUTO_ID = "auto_id";
            String CREATE_TABLE_STATE = " create table "

                    + TABLE_STATE + " ( " + c_STATE_AUTO_ID
                    + " INTEGER PRIMARY KEY AUTOINCREMENT, "

                    + c_STATE_ID + " TEXT, " + c_STATE_NAME + " TEXT, "

                    + UNIQUE + OPEN + c_STATE_AUTO_ID + CLOSE + ON_CONFLICT + " )";
            db.execSQL(CREATE_TABLE_STATE);
            String c_CITY_NAME = "city_name";
            String c_CITY_ID = "city_id";
            String c_CITY_AUTO_ID = "auto_id";
            String CREATE_TABLE_CITY = " create table "

                    + TABLE_CITY + " ( " + c_CITY_AUTO_ID
                    + " INTEGER PRIMARY KEY AUTOINCREMENT, "

                    + c_CITY_ID + " TEXT, " + c_CITY_NAME + " TEXT, " + c_STATE_ID
                    + " TEXT, " + UNIQUE + OPEN + c_CITY_AUTO_ID + CLOSE + ON_CONFLICT
                    + " )";
            db.execSQL(CREATE_TABLE_CITY);
            String c_CIF_SIGNATURE = "cif_signature";
            String c_CIF_PHOTO = "cif_photo";
            String c_CIF_PERMANENT_CITY_CODE = "cif_permanent_city_code";
            String c_CIF_CURRENT_CITY_CODE = "cif_current_city_code";
            String c_CIF_PERMANENT_STATE_CODE = "cif_permanent_state_code";
            String c_CIF_CURRENT_STATE_CODE = "cif_current_state_code";
            String c_CIF_DATE_OF_BIRTH = "cif_date_of_birth";
            String c_CIF_YEAR_OF_PASSING = "cif_year_of_passing";
            String c_CIF_SPONSORSHIP_DATE = "cif_sponsorship_date";
            String c_CIF_CURRENT_ADDRESS_SAME_AS_PERMANENT = "cif_current_address_as_same_as_permanent";
            String c_CIF_GENDER = "cif_gender";
            String c_CIF_EDUCATIONAL_QUALIFICATION = "cif_educational_qualification";
            String c_CIF_AREA = "cif_area";
            String c_CIF_CATEGORY = "cif_category";
            String c_CIF_NAME_INITIAL = "cif_name_initial";
            String c_CIF_INSURANCE_CATEGORY = "cif_insurance_category";
            String c_CIF_COR_TYPE = "cif_cor_type";
            String c_CIF_NATIONALITY = "cif_nationality";
            String c_CIF_CENTRAL_GOVT_ID = "cif_central_govt_id";
            String c_CIF_PASSPORT_NO = "cif_passport_no";
            String c_CIF_DRIVING_LICENSE_NO = "cif_driving_license_no";
            String c_CIF_VOTER_ID = "cif_voter_id";
            String c_CIF_INTERNAL_REF_NO = "cif_internal_ref_no";
            String c_CIF_EXAM_LANGUAGE = "cif_exam_language";
            String c_CIF_EXAM_CENTER_LOCATION = "cif_exam_center_location";
            String c_CIF_EXAM_BODY_NAME = "cif_exam_body_name";
            String c_CIF_EXAM_MODE = "cif_exam_mode";
            String c_CIF_ATI_CENTER_ACCREDITATION_NO = "cif_ati_center_accreditation_no";
            String c_CIF_BRANCH_NAME = "cif_branch_name";
            String c_CIF_PHONE_NO = "cif_phone_no";
            String c_CIF_PRIMARY_PROFESSION = "cif_primary_profession";
            String c_CIF_OTHER_QUALIFICATION = "cif_other_qualification";
            String c_CIF_ROLL_NUM_BASIC_QUALIFICATION = "cif_roll_number_for_basic_qualification";
            String c_CIF_BOARD_NAME_BASIC_QUALIFICATION = "cif_board_name_for_basic_qualification";
            String c_CIF_BASIC_QUALIFICATION = "cif_basic_qualification";
            String c_CIF_PERMANENT_DISTRICT = "cif_permanent_district";
            String c_CIF_PERMANENT_PINCODE = "cif_permanent_pincode";
            String c_CIF_PERMANENT_TOWN = "cif_permanent_town";
            String c_CIF_PERMANENT_STREET = "cif_permanent_street";
            String c_CIF_PERMANENT_HOUSE_NUM = "cif_permanent_house_number";
            String c_CIF_CURRENT_DISTRICT = "cif_current_district";
            String c_CIF_CURRENT_PINCODE = "cif_current_pincode";
            String c_CIF_CURRENT_TOWN = "cif_current_town";
            String c_CIF_CURRENT_STREET = "cif_current_street";
            String c_CIF_CURRENT_HOUSE_NUM = "cif_current_house_number";
            String c_CIF_FATHER_NAME = "cif_father_name";
            String c_CIF_TELEMARKETER_NAME = "cif_telemarketer_name";
            String c_CIF_MASTER_KEY = "cif_master_key";
            String CREATE_TABLE_CIF_ENROLLMENT = " Create Table "
                    + TABLE_CIF_ENROLLMENT + OPEN + c_CIF_MASTER_KEY
                    + " INTEGER PRIMARY KEY AUTOINCREMENT, " + C_CIF_PF_NUMBER
                    + " TEXT, " + C_CIF_QUOTATION_NO + " TEXT, " + C_CIF_PAN
                    + " TEXT, " + C_USER_ID + " TEXT, " + c_CIF_NAME_INITIAL
                    + " TEXT, " + C_CIF_CANDIDATE_NAME + " TEXT, " + c_CIF_FATHER_NAME
                    + " TEXT, " + c_CIF_GENDER + " TEXT, " + c_CIF_DATE_OF_BIRTH
                    + " TEXT, " + C_CIF_EMAIL_ID + " TEXT, " + C_CIF_MOBILE_NO
                    + " TEXT, " + c_CIF_PHONE_NO + " TEXT, " + c_CIF_COR_TYPE
                    + " TEXT, " + c_CIF_INSURANCE_CATEGORY + " TEXT, "
                    + c_CIF_TELEMARKETER_NAME + " TEXT, " + c_CIF_CURRENT_HOUSE_NUM
                    + " TEXT, " + c_CIF_CURRENT_STREET + " TEXT, " + c_CIF_CURRENT_TOWN
                    + " TEXT, " + c_CIF_CURRENT_STATE_CODE + " TEXT, "
                    + c_CIF_CURRENT_CITY_CODE + " TEXT, " + c_CIF_CURRENT_PINCODE
                    + " TEXT, " + c_CIF_CURRENT_DISTRICT + " TEXT, "
                    + c_CIF_CURRENT_ADDRESS_SAME_AS_PERMANENT + " TEXT, "
                    + c_CIF_PERMANENT_HOUSE_NUM + " TEXT, " + c_CIF_PERMANENT_STREET
                    + " TEXT, " + c_CIF_PERMANENT_TOWN + " TEXT, "
                    + c_CIF_PERMANENT_STATE_CODE + " TEXT, "
                    + c_CIF_PERMANENT_CITY_CODE + " TEXT, " + c_CIF_PERMANENT_PINCODE
                    + " TEXT, " + c_CIF_PERMANENT_DISTRICT + " TEXT, "
                    + c_CIF_NATIONALITY + " TEXT, " + c_CIF_AREA + " TEXT, "
                    + c_CIF_BRANCH_NAME + " TEXT, " + c_CIF_EDUCATIONAL_QUALIFICATION
                    + " TEXT, " + c_CIF_BASIC_QUALIFICATION + " TEXT, "
                    + c_CIF_BOARD_NAME_BASIC_QUALIFICATION + " TEXT, "
                    + c_CIF_ROLL_NUM_BASIC_QUALIFICATION + " TEXT, "
                    + c_CIF_OTHER_QUALIFICATION + " TEXT, " + c_CIF_PRIMARY_PROFESSION
                    + " TEXT, " + c_CIF_EXAM_MODE + " TEXT, " + c_CIF_EXAM_BODY_NAME
                    + " TEXT, " + c_CIF_EXAM_CENTER_LOCATION + " TEXT, "
                    + c_CIF_EXAM_LANGUAGE + " TEXT, " + c_CIF_YEAR_OF_PASSING
                    + " TEXT, " + c_CIF_ATI_CENTER_ACCREDITATION_NO + " TEXT, "
                    + c_CIF_CATEGORY + " TEXT, " + c_CIF_SPONSORSHIP_DATE + " TEXT, "
                    + c_CIF_INTERNAL_REF_NO + " TEXT, " + c_CIF_VOTER_ID + " TEXT, "
                    + c_CIF_DRIVING_LICENSE_NO + " TEXT, " + c_CIF_PASSPORT_NO
                    + " TEXT, " + c_CIF_CENTRAL_GOVT_ID + " TEXT, " + c_CIF_PHOTO
                    + " TEXT, " + c_CIF_SIGNATURE + " TEXT, " + C_CREATED_BY
                    + " TEXT, " + C_CREATED_DATE + " TEXT, " + C_MODIFIED_BY
                    + " TEXT, " + C_MODIFIED_DATE + " TEXT, " + cisSync + " BOOLEAN, "
                    + cisFlag1 + " BOOLEAN, " + cisFlag2 + " BOOLEAN, "
                    + C_CIF_AADHAR_CARD_NO + " TEXT, " + C_CIF_CONTACT_PERSON_EMAIL_ID + " TEXT, "
                    + UNIQUE + OPEN + C_CIF_PF_NUMBER + "," + C_CIF_QUOTATION_NO + CLOSE + ON_CONFLICT
                    + " )";
            db.execSQL(CREATE_TABLE_CIF_ENROLLMENT);
            String c_DD_MASTER_KEY = "dd_master_key";
            String CREATE_TABLE_DASHBOARDDETAIL = " create table "
                    + TABLE_DASHBOARDDETAIL + " ( " + c_DD_MASTER_KEY
                    + " INTEGER PRIMARY KEY AUTOINCREMENT, " + C_CIF_QUOTATION_NO
                    + " TEXT, " + C_USER_ID + " TEXT, " + C_CIF_PF_NUMBER + " TEXT, "
                    + C_CIF_CANDIDATE_NAME + " TEXT, " + C_CIF_EMAIL_ID + " TEXT, "
                    + C_DD_PENDING_STATUS + " TEXT, " + C_CREATED_DATE + " TEXT, "
                    + C_CIF_MOBILE_NO + " TEXT, " + C_CREATED_BY + " TEXT, "
                    + C_MODIFIED_BY + " TEXT, " + C_MODIFIED_DATE + " TEXT, " + cisSync
                    + " BOOLEAN, " + cisFlag1 + " BOOLEAN, " + cisFlag2 + " BOOLEAN, "
                    + C_CIF_AADHAR_CARD_NO + " TEXT, " + C_CIF_CONTACT_PERSON_EMAIL_ID + " TEXT, "
                    + UNIQUE + OPEN + C_CIF_QUOTATION_NO + CLOSE + ON_CONFLICT + " )";
            db.execSQL(CREATE_TABLE_DASHBOARDDETAIL);
            String c_SS_MASTER_KEY = "ss_master_key";
            String CREATE_TABLE_SYNC_STATUS = " create table "
                    + TABLE_SYNC_STATUS + " ( " + c_SS_MASTER_KEY
                    + " INTEGER PRIMARY KEY AUTOINCREMENT, " + C_CIF_QUOTATION_NO
                    + " TEXT, " + C_CIF_PF_NUMBER + " TEXT, " + C_SS_SYNC_STATUS
                    + " TEXT, " + C_CREATED_BY + " TEXT, " + C_CREATED_DATE + " TEXT, "
                    + C_MODIFIED_BY + " TEXT, " + C_MODIFIED_DATE + " TEXT, " + cisSync
                    + " BOOLEAN, " + cisFlag1 + " TEXT, " + cisFlag2 + " BOOLEAN, "
                    + UNIQUE + OPEN + C_CIF_QUOTATION_NO + "," + C_CIF_PF_NUMBER
                    + CLOSE + ON_CONFLICT + " )";
            db.execSQL(CREATE_TABLE_SYNC_STATUS);
            String CREATE_TABLE_EXAM_CENTER_LOCATION = " create table "
                    + TABLE_EXAM_CENTER_LOCATION
                    + " ( "
                    + c_DD_MASTER_KEY
                    + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + C_CIF_QUOTATION_NO
                    + " TEXT, "
                    + C_CIF_EXAM_CENTER
                    + " TEXT, "
                    + UNIQUE
                    + OPEN
                    + C_CIF_QUOTATION_NO + CLOSE + ON_CONFLICT + " )";
            db.execSQL(CREATE_TABLE_EXAM_CENTER_LOCATION);

            /*Rinraksha table*/
            String RIN_RAKSHA_BI_SOURCE = "rinraksha_source";
            String RIN_RAKSHA_BI_FLAG4 = "rinraksha_flag4";
            String RIN_RAKSHA_BI_FLAG3 = "rinraksha_flag3";
            String RIN_RAKSHA_BI_FLAG2 = "rinraksha_flag2";
            String RIN_RAKSHA_BI_FLAG1 = "rinraksha_flag1";
            String RIN_RAKSHA_BI_MODIFIED_BY = "rinraksha_modified_by";
            String RIN_RAKSHA_BI_MODIFIED_DATE = "rinraksha_modified_date";
            String RIN_RAKSHA_BI_CREATED_BY = "rinraksha_created_by";
            String RIN_RAKSHA_BI_CREATED_DATE = "rinraksha_created_date";
            String RIN_RAKSHA_BI_NEED_ANALYSIS = "rinraksha_need_analysis";
            String RIN_RAKSHA_BI_BACKDATE = "rinraksha_backdate";
            String RIN_RAKSHA_BI_WISH_TO_BACKDATE = "rinraksha_wish_to_backdate";
            String RIN_RAKSHA_BI_PROPOSER_SIGN = "rinraksha_proposer_sign";
            String RIN_RAKSHA_BI_AGENT_SIGN = "rinraksha_agent_sign";
            String RIN_RAKSHA_BI_DATE2 = "rinraksha_date2";
            String RIN_RAKSHA_BI_DATE1 = "rinraksha_date1";
            String RIN_RAKSHA_BI_PLACE2 = "rinraksha_place2";
            String RIN_RAKSHA_BI_PLACE1 = "rinraksha_place1";
            String RIN_RAKSHA_BI_NAME_OF_PERSON = "rinraksha_name_of_person";
            String RIN_RAKSHA_BI_LIFE_ASSURED_DOB = "rinraksha_life_assured_dob";
            String RIN_RAKSHA_BI_PROPOSAL_DOB = "rinraksha_proposal_dob";
            String RIN_RAKSHA_BI_NAME_OF_LIFE_ASSURED = "rinraksha_name_life_assured";
            String RIN_RAKSHA_BI_LIFE_ASSURED_LAST_NAME = "rinraksha_life_assured_last_name";
            String RIN_RAKSHA_BI_LIFE_ASSURED_MIDDLE_NAME = "rinraksha_life_assured_middle_name";
            String RIN_RAKSHA_BI_LIFE_ASSURED_FIRST_NAME = "rinraksha_life_assured_first_name";
            String RIN_RAKSHA_BI_LIFE_ASSURED_TITLE = "rinraksha_life_assured_tittle";
            String RIN_RAKSHA_BI_NAME_OF_PROPOSER = "rinraksha_name_of_proposer";
            String RIN_RAKSHA_BI_PROPOSAL_LAST_NAME = "rinraksha_proposal_last_name";
            String RIN_RAKSHA_BI_PROPOSAL_MIDDLE_NAME = "rinraksha_proposal_middle_name";
            String RIN_RAKSHA_BI_PROPOSAL_FIRST_NAME = "rinraksha_proposal_first_name";
            String RIN_RAKSHA_BI_PROPOSAL_TITTLE = "rinraksha_proposal_tittle";
            String RIN_RAKSHA_BI_PROPOSAL_SAME_AS_LIFE_ASSURED = "rinraksha_proposal_same_as_life_assured";
            String RIN_RAKSHA_BI_PROPOSAL_NO = "rinraksha_proposal_no";
            String RIN_RAKSHA_BI_PRODUCT_NAME = "rinraksha_product_name";
            String RIN_RAKSHA_BI_MASTER_KEY = "rinraksha_master_key";
            String CREATE_TABLE_RIN_RAKSHA_BI = " create table "
                    + TABLE_RIN_RAKSHA_BI + " ( "
                    + RIN_RAKSHA_BI_MASTER_KEY + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + RIN_RAKSHA_BI_QUOTATION_NO + " TEXT not null, "
                    + RIN_RAKSHA_BI_PRODUCT_NAME + " TEXT, "
                    + RIN_RAKSHA_BI_INPUT + " TEXT, "
                    + RIN_RAKSHA_BI_OUTPUT + " TEXT, "
                    + RIN_RAKSHA_BI_PROPOSAL_NO + " TEXT, "
                    + RIN_RAKSHA_BI_PROPOSAL_SAME_AS_LIFE_ASSURED + " TEXT, "
                    + RIN_RAKSHA_BI_PROPOSAL_TITTLE + " TEXT, "
                    + RIN_RAKSHA_BI_PROPOSAL_FIRST_NAME + " TEXT, "
                    + RIN_RAKSHA_BI_PROPOSAL_MIDDLE_NAME + " TEXT, "
                    + RIN_RAKSHA_BI_PROPOSAL_LAST_NAME + " TEXT, "
                    + RIN_RAKSHA_BI_NAME_OF_PROPOSER + " TEXT, "
                    + RIN_RAKSHA_BI_LIFE_ASSURED_TITLE + " TEXT, "
                    + RIN_RAKSHA_BI_LIFE_ASSURED_FIRST_NAME + " TEXT, "
                    + RIN_RAKSHA_BI_LIFE_ASSURED_MIDDLE_NAME + " TEXT, "
                    + RIN_RAKSHA_BI_LIFE_ASSURED_LAST_NAME + " TEXT, "
                    + RIN_RAKSHA_BI_NAME_OF_LIFE_ASSURED + " TEXT, "
                    + RIN_RAKSHA_BI_PROPOSAL_DOB + " TEXT, "
                    + RIN_RAKSHA_BI_LIFE_ASSURED_DOB + " TEXT, "
                    + RIN_RAKSHA_BI_NAME_OF_PERSON + " TEXT, "
                    + RIN_RAKSHA_BI_PLACE1 + " TEXT, "
                    + RIN_RAKSHA_BI_PLACE2 + " TEXT, "
                    + RIN_RAKSHA_BI_DATE1 + " TEXT, "
                    + RIN_RAKSHA_BI_DATE2 + " TEXT, "
                    + RIN_RAKSHA_BI_AGENT_SIGN + " TEXT, "
                    + RIN_RAKSHA_BI_PROPOSER_SIGN + " TEXT, "
                    + RIN_RAKSHA_BI_WISH_TO_BACKDATE + " TEXT, "
                    + RIN_RAKSHA_BI_BACKDATE + " TEXT, "
                    + RIN_RAKSHA_BI_NEED_ANALYSIS + " TEXT, "
                    + RIN_RAKSHA_BI_CREATED_DATE + " TEXT, "
                    + RIN_RAKSHA_BI_CREATED_BY + " TEXT, "
                    + RIN_RAKSHA_BI_MODIFIED_DATE + " TEXT, "
                    + RIN_RAKSHA_BI_MODIFIED_BY + " TEXT, "
                    + RIN_RAKSHA_BI_FLAG1 + " TEXT, "
                    + RIN_RAKSHA_BI_FLAG2 + " TEXT, "
                    + RIN_RAKSHA_BI_FLAG3 + " TEXT, "
                    + RIN_RAKSHA_BI_FLAG4 + " TEXT, "
                    + RIN_RAKSHA_BI_DEL_FLAG + " INTEGER, "
                    + RIN_RAKSHA_BI_CODE + " TEXT, "
                    + RIN_RAKSHA_BI_TYPE + " TEXT, "
                    + RIN_RAKSHA_BI_URN + " TEXT, "
                    + RIN_RAKSHA_BI_SOURCE + " TEXT, "
                    + RIN_RAKSHA_BI_BORROWER1_URN + " TEXT, "
                    + RIN_RAKSHA_BI_BORROWER2_URN + " TEXT, "
                    + RIN_RAKSHA_BI_BORROWER3_URN + " TEXT, "
                    + UNIQUE + OPEN + RIN_RAKSHA_BI_QUOTATION_NO + CLOSE + ON_CONFLICT + " )";
            db.execSQL(CREATE_TABLE_RIN_RAKSHA_BI);

            /*group feedback*/

            db.execSQL(CREATE_TABLE_GROUP_FEEDBACK);

            /*eKYC for PS and Claims*/

            db.execSQL(CREATE_TABLE_EKYC_PS_CLAIMS);

            /*Agent on boarding created by rajan 17-04-2018*/

            db.execSQL(CREATE_TABLE_AGENT_ON_BOARDING);

            /*table TCC and exam details 27-03-2018 by rajan*/

            db.execSQL(CREATE_TABLE_TCC_EXAM_DETAILS);

            /*table Short MHR 14-May-2020 by Tushar*/
            db.execSQL(CREATE_TABLE_SHORT_MHR_PROPOSAL_DETAILS);
            /*table Short MHR 01-November-2020 by Tushar*/
            db.execSQL(CREATE_TABLE_COVID_SELF_DECLARATION);

            /*POSP RA created by rajan 08-05-2021*/
            db.execSQL(CREATE_TABLE_POSP_RA);
            db.execSQL(CREATE_TABLE_POSP_RA_REJECTION);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /*
     * if any upgrade in table is there then those table are exist then it will
     * drop them and again created
     */

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub

        /*db.execSQL("DROP TABLE IF EXISTS " + LoginTable);
        db.execSQL("DROP TABLE IF EXISTS " + DateTable);
        db.execSQL("DROP TABLE IF EXISTS " + VersionTable);

        // bdm_tracker
        db.execSQL("DROP TABLE IF EXISTS " + DateTableBDMT);
        db.execSQL("DROP TABLE IF EXISTS " + ActivityCategorySubCategoryTable);
        db.execSQL("DROP TABLE IF EXISTS " + EmailTable);
        db.execSQL("DROP TABLE IF EXISTS " + DateTableAR);
        db.execSQL("DROP TABLE IF EXISTS " + ActivityCategoryTable);
        db.execSQL("DROP TABLE IF EXISTS " + ActivitySubCategoryTable);
        db.execSQL("DROP TABLE IF EXISTS " + BranchTable);
        db.execSQL("DROP TABLE IF EXISTS " + ParamListTable);
        db.execSQL("DROP TABLE IF EXISTS " + DefineObjectiveTable);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DOCUMENT_UPLOAD);

        // for branch profile menu
        db.execSQL("DROP TABLE IF EXISTS " + BranchProfileTable);
        db.execSQL("DROP TABLE IF EXISTS " + BranchAdvancesTable);
        db.execSQL("DROP TABLE IF EXISTS " + BranchDepositsTable);
        db.execSQL("DROP TABLE IF EXISTS " + SBranchProfileTable);
        db.execSQL("DROP TABLE IF EXISTS " + SyncBranchProfileTable);
        db.execSQL("DROP TABLE IF EXISTS " + DefineObjectiveSyncTable);

        db.execSQL("DROP TABLE IF EXISTS " + NeedAnalysisTable);
        // for agent profile
        db.execSQL("DROP TABLE IF EXISTS " + AgentProfileTable);

        // HO Lead
        db.execSQL("DROP TABLE IF EXISTS " + HOLeadTable);
        // Renewal Premium New Businedd
        db.execSQL("DROP TABLE IF EXISTS " + RenewalPremiumNewBusinessLogTable);


        //CIF enrollment tables 19-05-2017 by rajan
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STATE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CITY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CIF_ENROLLMENT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DASHBOARDDETAIL);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SYNC_STATUS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXAM_CENTER_LOCATION);

        //rinraksha tables 19-05-2017 by rajan
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RIN_RAKSHA_BI);

        //group feedback tables 05-10-2017 by rajan
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GROUP_FEEDBACK);

        //eKYC for PS and Claims table 27-12-2017 by rajan
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EKYC_PS_CLAIMS);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TCC_EXAM_DETAILS);
        *//*Agent on boarding created by rajan 17-04-2018*//*
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_AGENT_ON_BOARDING);

        *//*table Short MHR 14-May-2020 by Tushar*//*
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SHORT_MHR_PROPOSAL_DETAILS);*/

        /*table Short MHR 01-November-2020 by Tushar*/
        /*try {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_COVID_SELF_DECLARATION);
        } catch (Exception e) {
            e.printStackTrace();
        }*/

        try {

            /*table Short MHR 01-November-2020 by Tushar*/
            if (oldVersion <= 41) {
                db.execSQL(CREATE_TABLE_COVID_SELF_DECLARATION);
            }

            if (oldVersion <= 42) {
                db.execSQL("ALTER TABLE " + TABLE_AGENT_ON_BOARDING + " ADD COLUMN "
                        + AGENT_ON_BOARDING_ENROLLMENT_TYPE + " TEXT ;");
                db.execSQL(CREATE_TABLE_POSP_RA);
                db.execSQL(CREATE_TABLE_POSP_RA_REJECTION);
            }
            if (oldVersion <= 43) {
                db.execSQL("ALTER TABLE " + CREATE_TABLE_POSP_RA_REJECTION + " ADD COLUMN "
                        + POSP_RA_REQ_RAISED_ENROLLMENT_TYPE + " TEXT ;");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        //onCreate(db);
    }

    /*
     * you will pass class object of event of calendar and it will store in db.
     */

    public void AddLogin(clsLogin log) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();

        cv.put(colLoginTitle, log.getTitle());
        cv.put(colLoginFirstName, log.getFname());
        cv.put(colLoginLastName, log.getLname());
        cv.put(colLoginAddress, log.getAddress());
        cv.put(colLoginStatus, log.getStatus());
        cv.put(colLoginCIFNo, log.getCIFNo());
        cv.put(colLoginPateName, log.getPateName());
        cv.put(colLoginEmail, log.getEmail());
        cv.put(colLoginPassword, log.getPassword());
        cv.put(colLoginConfirmPassword, log.getConfirmPassword());
        cv.put(colLoginQuestion, log.getQuestion());
        cv.put(colLoginMobileNo, log.getMobileNo());
        cv.put(colLoginDOB, log.getDob());
        cv.put(colLoginType, log.getType());
        cv.put(colLoginPIN, log.getPIN());

        db.insert(LoginTable, colLoginFirstName, cv);
        System.out.println("Successfully added");
        db.close();

    }

    @TargetApi(19)
    public int deletePreviousUser() {
        int rowsDeleted = 0;
        try (SQLiteDatabase db = this.getWritableDatabase()) {
            rowsDeleted = db.delete(LoginTable, null, null);
        }
        return rowsDeleted;
    }


    // need analysis group other changes
    public void AddNeedAnalysisDashboardDetailsForOther(ProductBIBean log,
                                                        String created_by) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(colUinNo, log.getUinNo());
        cv.put(colna_group, log.getNa_group());
        cv.put(colCreatedBy, created_by);
        cv.put(colna_input, log.getNa_input());
        cv.put(colna_output, log.getNa_output());

        long rows = db.insert(NeedAnalysisTable, null, cv);
        db.close();
        System.out.println("NA Added Succesfully for other gorup:" + rows);
    }

    /*
     * you will pass class object of event of calendar and it will store in db.
     */

    public void AddNeedAnalysisDashboardDetails(ProductBIBean log) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();

        // cv.put(colProductBIID,log.getProductBIID());
        cv.put(colQuotationNo, log.getQuotationNo());
        cv.put(colPlanSelected, log.getPlanSelected());
        cv.put(colProposalDate, log.getProposalDate());
        cv.put(colMobileNo, log.getMobileNo());
        cv.put(colUinNo, log.getUinNo());
        cv.put(colSyncStatus, log.getSyncStatus());
        cv.put(colCreatedDate, log.getCreatedDate());
        cv.put(colCreatedBy, log.getCreatedBy());
        cv.put(colemail, log.getEmail());

        cv.put(colsr_code, log.getSr_code());
        cv.put(colsr_sr_code, log.getSr_sr_code());
        cv.put(colsr_type, log.getSr_type());
        cv.put(colsr_sr_type, log.getSr_sr_type());
        cv.put(colcust_title, log.getCust_title());
        cv.put(colcust_first_name, log.getCust_first_name());
        cv.put(colcust_mid_name, log.getCust_mid_name());
        cv.put(colcust_last_name, log.getCust_last_name());
        cv.put(colsumassured, log.getSumassured());
        cv.put(colpremium, log.getPremium());
        cv.put(colsr_email, log.getSr_email());
        cv.put(colsr_mobile, log.getSr_mobile());
        cv.put(colna_input, log.getNa_input());
        cv.put(colna_output, log.getNa_output());
        cv.put(colfrequency, log.getFrequency());
        cv.put(colpolicyTerm, log.getPolicyTerm());
        cv.put(colprem_paying_term, log.getPrem_paying_term());
        cv.put(colplan_code, log.getPlan_code());
        cv.put(colLA_dob, log.getLA_dob());
        cv.put(colproposer_dob, log.getProposer_dob());
        cv.put(col_transaction_mode, log.getTransactionMode());
        cv.put(col_bi_inputVal, log.getBi_inputVal());
        cv.put(col_bi_outputVal, log.getBi_outputVal());

        db.insert(NeedAnalysisTable, null, cv);
        db.close();
        System.out.println("Added Succesfully ");
    }

    public void updateNA_CBI_UINNum(String quotationNo, String uinNo,
                                    String syncStatus, String retailUserType) {

        if (retailUserType.equalsIgnoreCase("Other")) {
            deleteOtherListRow(GetUserCode(), uinNo);
        }

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();

        cv.put(colUinNo, uinNo);
        cv.put(colSyncStatus, syncStatus);

        /*if (retailUserType.equalsIgnoreCase("Other")) {
            cv.put(col_transaction_mode, trasactionMode);
        }*/
        cv.put(colna_group, retailUserType);

        db.update(NeedAnalysisTable, cv, colQuotationNo + '=' + "'"
                + quotationNo + "'", null);
        System.out.println(" rows affected : " + 1);

        System.out.println("Updated Succesfully ");
        db.close();
    }

    public void updateNA_CBI_UINNumOtherPolicyList(String quotationNo,
                                                   String uinNo, String syncStatus) {

        long row = deleteOtherListRow(GetUserCode(), uinNo);

        if (row > 0) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put(colUinNo, uinNo);
            cv.put(colSyncStatus, syncStatus);
            cv.put(colna_group, "Other");

            db.update(NeedAnalysisTable, cv, colQuotationNo + '=' + "'"
                    + quotationNo + "'", null);
            System.out.println(" rows affected : " + 1);

            System.out.println("Updated Succesfully ");
            db.close();
        }
    }

    private long deleteOtherListRow(String id, String URNNumber) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor;
        long row = 0;
        try {

            cursor = db.rawQuery("Select * from " + NeedAnalysisTable
                    + " where " + colCreatedBy + "=" + "'" + id
                    + "' AND  " + colUinNo + "='" + URNNumber + "'  AND  "
                    + colna_group + "='Other'", null);

            long count = cursor.getCount();
            if (count > 0) {
                cursor.moveToFirst();

                do {

                    row = db.delete(NeedAnalysisTable, colCreatedBy + "='" + id
                            + "'  AND " + colUinNo + "='" + URNNumber
                            + "'  AND  " + colna_group + "='Other'", null);
                } while (cursor.moveToNext());
            } else {
                row = 1;
            }
            cursor.close();
        } catch (Exception e) {
            Log.e("NewDBHelper", e.toString() + "Error in getting BI Detail");
        }

        System.out.println("row:" + row);
        db.close();
        return row;

    }

    /*
     * you will pass class object of version of apps and it will store in db.
     */


    /*
     * it is retrieving the registered user id from database.
     */

    public int GetUserId() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cur = db.rawQuery("Select * from " + LoginTable, null);
        int x = cur.getCount();
        cur.close();
        return x;
    }

    public String GetUserEmailId() {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor c = db.query(LoginTable, new String[]{"LoginEmail"}, null,
                null, null, null, null);
        c.moveToFirst();
        int index = c.getColumnIndex(colLoginEmail);
        String indexString = c.getString(index);
        c.close();
        return indexString;
    }

    // get CIF or BDM code

    public String GetUserCode() {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor c = db.query(LoginTable, new String[]{"LoginCIFNo"}, null,
                null, null, null, null);

        int i = c.getCount();

        c.moveToFirst();
        int index = c.getColumnIndex(colLoginCIFNo);

        String str;

        if (i == 0) {
            str = "";
        } else {
            str = c.getString(index);
        }
        c.close();
        return str;
    }

    /*
     * it will store event detail.
     */

    void AddEvent(clsCalendar log) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();

        cv.put(colDateName, log.getDateName());
        cv.put(colEventName, log.getEventName());
        cv.put(colMonth, log.getMonth());
        cv.put(colYear, log.getYear());
        cv.put(colTime, log.getTime());
        cv.put(colCalendarUserID, log.getUserId());
        cv.put(colNotes, log.getNotes());

        db.insert(DateTable, colDateName, cv);
        db.close();
    }

    void UpdateEvent(clsCalendar log, String id) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();

        cv.put(colDateName, log.getDateName());
        cv.put(colEventName, log.getEventName());
        cv.put(colMonth, log.getMonth());
        cv.put(colYear, log.getYear());
        cv.put(colTime, log.getTime());
        cv.put(colCalendarUserID, log.getUserId());
        cv.put(colNotes, log.getNotes());

        db.update(DateTable, cv, colDateID + "=" + id, null);
        db.close();

    }

    void DeleteEvent(String id) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(DateTable, colDateID + "=" + id, null);
        db.close();

    }

    /*
     * void DeleteRecordActivity(String id) { SQLiteDatabase db =
     * this.getWritableDatabase();
     *
     * db.delete(DateTableAR,colDateIDAR+"="+id,null); db.close();
     *
     * }
     */

    public void DeleteRecordActivity(String act, String subact, String evnt,
                                     String date, String time, String remr, String lead) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete("DateTableAR", colActivityAR + " = " + "'" + act + "'"
                + "AND " + colSubActivityAR + "=" + "'" + subact + "'" + "AND "
                + colEventNameAR + "=" + "'" + evnt + "'" + "AND "
                + colDateNameAR + "=" + "'" + date + "'" + "AND " + colTimeAR
                + "=" + "'" + time + "'" + "AND " + colRemarkAR + "=" + "'"
                + remr + "'" + "AND " + colActivityLeadAR + "=" + "'" + lead
                + "'", null);
        db.close();

    }

    /*
     * it will get date based on month and year.
     */

    Cursor getdate(String month, String year, String userid) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor c = null;
        try {

            c = db.query(DateTable, new String[]{colDateID, " DateID",
                            colMonth, colYear, colDateName, colEventName, colNotes},
                    colMonth + "=" + "'" + month + "'" + "AND " + colYear + "="
                            + "'" + year + "'" + "AND " + colCalendarUserID
                            + "=" + "'" + userid + "'", null, null, null, null,
                    null);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return c;
    }

    public String GetRowID(String date, String event, String time,
                           String userid, String notes) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor c = db.query(DateTable, new String[]{colDateID, " DateID",
                        colDateName, colEventName, colTime, colCalendarUserID},
                colDateName + "=" + "'" + date + "'" + "AND " + colEventName
                        + "=" + "'" + event + "'" + "AND " + colTime + "="
                        + "'" + time + "'" + "AND " + colCalendarUserID + "="
                        + "'" + userid + "'" + "AND " + colNotes + "=" + "'"
                        + notes + "'", null, null, null, null, null);

        int x = c.getCount();
        if (x == 0) {
            return "";
        } else {
            c.moveToFirst();
            int index = c.getColumnIndex(colDateID);
            String indexString = c.getString(index);
            c.close();
            return indexString;
        }
    }


    /*
     * based on date get event name
     */

    Cursor geteventname(String datename, String userid) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor c = null;
        try {

            c = db.query(DateTable, new String[]{colDateID, " DateID",
                            colDateName, colEventName, colTime, colCalendarUserID,
                            colNotes}, colDateName + "=" + "'" + datename + "'"
                            + "AND " + colCalendarUserID + "=" + "'" + userid + "'",
                    null, null, null, null, null);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return c;
    }

    /*
     * it will get all events in db
     */

    Cursor getAllEvents(String userid) {
        SQLiteDatabase db = this.getReadableDatabase();

        return db.query(DateTable, new String[]{"DateName", "EventName",
                        "Time", "Notes"},
                colCalendarUserID + "=" + "'" + userid + "'", null, null, null,
                null, null);
    }

    Cursor getSelectedRowEvent(int rowid, String userid) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor c = null;
        try {

            c = db.query(DateTable, new String[]{colDateID, " DateID",
                    colDateName, colEventName, colTime, colNotes}, colDateID
                    + "=" + "'" + rowid + "'" + "AND " + colCalendarUserID
                    + "=" + "'" + userid + "'", null, null, null, null, null);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return c;
    }

    Cursor UpdateCalendarEventStatus(String rowid, String userid) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor c = null;
        try {

            c = db.query(DateTable, new String[]{colDateID, " DateID",
                    colDateName, colEventName, colMonth, colYear, colTime,
                    colCalendarUserID, colNotes}, colDateID + "=" + "'"
                    + rowid + "'" + "AND " + colCalendarUserID + "=" + "'"
                    + userid + "'", null, null, null, null, null);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return c;
    }

    // for bdm_tracker

    public void AddEventBDMT(clsBDMTrackerCalendar log) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();

        cv.put(colDateNameBDMT, log.getDateName());
        cv.put(colEventNameBDMT, log.getEventName());
        cv.put(colMonthBDMT, log.getMonth());
        cv.put(colYearBDMT, log.getYear());
        cv.put(colTimeBDMT, log.getTime());
        cv.put(colRemark, log.getRemark());
        cv.put(colActivity, log.getActivity());
        cv.put(colSubActivity, log.getSubActivity());
        cv.put(colTimeTo, log.getTimeTo());
        cv.put(colUserID, log.getUserId());
        cv.put(colActivityStatus, log.getStatus());
        cv.put(colActivitySync, log.getSync());
        cv.put(colActivityCreatedDate, log.getCreatedDate());
        cv.put(colActivityModifiedDate, log.getModifiedDate());
        cv.put(colActivityServerMasterId, log.getServerMasterId());
        cv.put(colActivityLead, log.getLead());

        db.insert(DateTableBDMT, colDateNameBDMT, cv);
        db.close();
    }

    public Cursor getdateBDMT(String month, String year) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor c = null;
        try {

            c = db.query(DateTableBDMT, new String[]{colDateIDBDMT,
                    " DateIDBDMT", colMonthBDMT, colYearBDMT, colDateNameBDMT,
                    colEventNameBDMT, colActivity}, colMonthBDMT + "=" + "'"
                    + month + "'" + "AND " + colYearBDMT + "=" + "'" + year
                    + "'", null, null, null, null, null);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return c;
    }


    public Cursor geteventnameBDMT(String datename, String userid) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor c = null;
        try {

            c = db.query(DateTableBDMT, new String[]{colDateIDBDMT,
                            " DateIDBDMT", colDateNameBDMT, colEventNameBDMT,
                            colTimeBDMT, colRemark, colActivity, colSubActivity,
                            colTimeTo, colUserID, colActivityStatus, colActivityLead},
                    colDateNameBDMT + "=" + "'" + datename + "'" + "AND "
                            + colUserID + "=" + "'" + userid + "'", null, null,
                    null, null, null);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return c;
    }

    public Cursor getAllEventsBDMT(String userid) {
        SQLiteDatabase db = this.getReadableDatabase();

        return db.query(DateTableBDMT, new String[]{"DateNameBDMT",
                        "EventNameBDMT", "TimeBDMT", "Remark", "Activity",
                        "SubActivity", "TimeTo", "ActivityStatus", "ActivityLead"},
                colUserID + "=" + "'" + userid + "'", null, null, null, null,
                null);
    }

    // end bdm_tracker

    /*
     * based on security answer it will get CIF or BDM code
     */

    public String getCIFNo(String patename) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor c = db.query(LoginTable, new String[]{colLoginID, " LoginID",
                colLoginPateName, colLoginCIFNo}, colLoginPateName + "=" + "'"
                + patename + "'", null, null, null, null, null);

        int count = c.getCount();

        if (count == 0) {
            c.close();
            return "";
        } else {

            c.moveToFirst();
            int index = c.getColumnIndex(colLoginCIFNo);
            String indexString = c.getString(index);
            c.close();
            return indexString;
        }
    }

    /*
     * based on security answer it will get password for CIF or BDM.
     */

    public String getpassword(String patename) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor c = db.query(LoginTable, new String[]{colLoginID, " LoginID",
                colLoginPateName, colLoginPassword}, colLoginPateName + "="
                + "'" + patename + "'", null, null, null, null, null);

        int count = c.getCount();

        if (count == 0) {
            c.close();
            return "";
        } else {

            c.moveToFirst();
            int index = c.getColumnIndex(colLoginPassword);

            String indexString = c.getString(index);
            c.close();
            return indexString;
        }
    }

    /*
     * it will get security question
     */

    public String GetQuestion() {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor c = db.query(LoginTable, new String[]{"LoginQuestion"}, null,
                null, null, null, null);
        c.moveToFirst();
        int index = c.getColumnIndex(colLoginQuestion);

        String indexString = c.getString(index);
        c.close();
        return indexString;
    }

    /*
     * with it get all information of registered user.
     */

    public Cursor GetProfile() {
        SQLiteDatabase db = this.getWritableDatabase();

        return db.rawQuery("Select * from " + LoginTable, null);
    }

    /*
     * if any information of user is change then this method do update for it.
     */

    void UpdateRecord(clsLogin log, String id) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();

        cv.put(colLoginTitle, log.getTitle());
        cv.put(colLoginFirstName, log.getFname());
        cv.put(colLoginLastName, log.getLname());
        cv.put(colLoginAddress, log.getAddress());
        cv.put(colLoginStatus, log.getStatus());
        cv.put(colLoginCIFNo, log.getCIFNo());
        cv.put(colLoginPateName, log.getPateName());
        cv.put(colLoginEmail, log.getEmail());
        cv.put(colLoginPassword, log.getPassword());
        cv.put(colLoginConfirmPassword, log.getConfirmPassword());
        cv.put(colLoginQuestion, log.getQuestion());
        cv.put(colLoginMobileNo, log.getMobileNo());
        cv.put(colLoginDOB, log.getDob());
        cv.put(colLoginType, log.getType());

        db.update(LoginTable, cv, colLoginID + "=" + id, null);
        db.close();

    }

    /*
     * when you want to change password that time when you enter old password
     * the it will check your old password from db.
     */

    public String VarifyOldPassword(String oldpassword) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor c = db.query(LoginTable, new String[]{colLoginID, " LoginID",
                colLoginPassword}, colLoginPassword + "=" + "'" + oldpassword
                + "'", null, null, null, null, null);

        int count = c.getCount();

        if (count == 0) {
            c.close();
            return "";
        } else {

            c.moveToFirst();
            int index = c.getColumnIndex(colLoginPassword);

            String indexString = c.getString(index);
            c.close();
            return indexString;
        }
    }

    /*
     * it will check the CIF/BDM code is valid or not.
     */


    /*
     * it will check the password is valid or not
     */


    public String checkDOB(String dob) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor c = db.query(LoginTable, new String[]{colLoginID, " LoginID",
                        colLoginDOB}, colLoginDOB + "=" + "'" + dob + "'", null, null,
                null, null, null);

        int count = c.getCount();

        if (count == 0) {
            c.close();
            return "";
        } else {

            c.moveToFirst();
            int index = c.getColumnIndex(colLoginDOB);
            String indexString = c.getString(index);
            c.close();
            return indexString;
        }
    }

    /*
     * it will get CIF/BDM code of registered user
     */

    public String GetCIFNo() {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor c = db.query(LoginTable, new String[]{"LoginCIFNo"}, null,
                null, null, null, null);
        c.moveToFirst();
        int index = c.getColumnIndex(colLoginCIFNo);
        String indexString = c.getString(index);
        c.close();
        return indexString;
    }

    public String GetUserName() {
        SQLiteDatabase db = this.getReadableDatabase();
        String title = "", fname = "", lname = "";
        Cursor c = db.query(LoginTable, new String[]{"LoginTitle",
                        "LoginFirstName", "LoginLastName"}, null, null, null, null,
                null);
        c.moveToFirst();
        int count = c.getColumnCount();
        try {
            if (count != 0)

                title = SimpleCrypto.decrypt("SBIL",
                        c.getString(c.getColumnIndex(colLoginTitle)));
            fname = SimpleCrypto.decrypt("SBIL",
                    c.getString(c.getColumnIndex(colLoginFirstName)));
            lname = SimpleCrypto.decrypt("SBIL",
                    c.getString(c.getColumnIndex(colLoginLastName)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        c.close();
        return fname + " " + lname;
    }


    public String GetUserType() {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor c = db.query(LoginTable, new String[]{"LoginType"}, null,
                null, null, null, null);

        int count = c.getCount();

        c.moveToFirst();
        int index = c.getColumnIndex(colLoginType);
        String str;
        if (count == 0) {
            str = "";
        } else {
            str = c.getString(index);
        }
        c.close();
        return str;
    }

    /*
     * it will get email id of registered user
     */

    public String GetEmailId() {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor c = db.query(LoginTable, new String[]{"LoginEmail"}, null,
                null, null, null, null);
        c.moveToFirst();
        int index = c.getColumnIndex(colLoginEmail);
        String indexString = c.getString(index);
        c.close();
        return indexString;
    }

    /*
     * it will get password of registered user
     */

    public String GetPassword() {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor c = db.query(LoginTable, new String[]{"LoginPassword"}, null,
                null, null, null, null);
        c.moveToFirst();
        int index = c.getColumnIndex(colLoginPassword);
        String indexString = c.getString(index);
        c.close();
        return indexString;
    }

    /*
     * it will get mobile no of registered user
     */

    public String GetMobileNo() {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor c = db.query(LoginTable, new String[]{"LoginMobileNo"}, null,
                null, null, null, null);
        c.moveToFirst();
        int index = c.getColumnIndex(colLoginMobileNo);
        String indexString = c.getString(index);
        c.close();
        return indexString;
    }


    public Cursor getSelectedRowRecord_ar(String act, String subact, String evnt,
                                          String date, String time, String remr, String lead) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor c = null;
        try {

            c = db.rawQuery("Select * from " + DateTableAR + " where "
                    + colActivityAR + " = " + "'" + act + "'" + "AND "
                    + colSubActivityAR + "=" + "'" + subact + "'" + "AND "
                    + colEventNameAR + "=" + "'" + evnt + "'" + "AND "
                    + colDateNameAR + "=" + "'" + date + "'" + "AND "
                    + colTimeAR + "=" + "'" + time + "'" + "AND " + colRemarkAR
                    + "=" + "'" + remr + "'" + "AND " + colActivityLeadAR + "="
                    + "'" + lead + "'", null);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return c;
    }

    public int RecordExist(String act, String subact, String evnt, String date,
                           String time, String remr, String lead) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor c = null;
        try {

            c = db.rawQuery("Select * from " + DateTableAR + " where "
                    + colActivityAR + " = " + "'" + act + "'" + "AND "
                    + colSubActivityAR + "=" + "'" + subact + "'" + "AND "
                    + colEventNameAR + "=" + "'" + evnt + "'" + "AND "
                    + colDateNameAR + "=" + "'" + date + "'" + "AND "
                    + colTimeAR + "=" + "'" + time + "'" + "AND " + colRemarkAR
                    + "=" + "'" + remr + "'" + "AND " + colActivityLeadAR + "="
                    + "'" + lead + "'", null);

        } catch (Exception e) {
            e.printStackTrace();
        }

        int x = c != null ? c.getCount() : 0;
        assert c != null;
        c.close();
        if (x == 0) {
            return 0;
        } else {
            return 1;
        }
    }

    public void AddActivityCategory(clsActivityCategory ClsActCat) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();

        /*
         * cv.put(colActivityCategorySubCategoryName, ClsActCat.getName());
         * cv.put(colActivityCategorySubCategoryeDiscription,
         * ClsActCat.getDiscription());
         * cv.put(colActivityCategorySubCategoryeParentId,
         * ClsActCat.getParentId());
         * cv.put(colActivityCategorySubCategoryeCreatedDate,
         * ClsActCat.getCreatedDate());
         * cv.put(colActivityCategorySubCategoryeCreatedBy,
         * ClsActCat.getCreatedDate());
         * cv.put(colActivityCategorySubCategoryeModifiedDate,
         * ClsActCat.getModifiedBy());
         * cv.put(colActivityCategorySubCategoryeModifiedBy,
         * ClsActCat.getModifiedDate());
         *
         *
         * db.insert(ActivityCategorySubCategoryTable,
         * colActivityCategorySubCategoryName, cv);
         */

        cv.put(colActivityCategoryName, ClsActCat.getName());
        cv.put(colActivityCategoryDiscription, ClsActCat.getDiscription());
        cv.put(colActivityCategoryParentId, ClsActCat.getParentId());
        cv.put(colActivityCategoryCreatedDate, ClsActCat.getCreatedDate());
        cv.put(colActivityCategoryCreatedBy, ClsActCat.getCreatedDate());
        cv.put(colActivityCategoryModifiedDate, ClsActCat.getModifiedBy());
        cv.put(colActivityCategoryModifiedBy, ClsActCat.getModifiedDate());

        db.insert(ActivityCategoryTable, colActivityCategoryName, cv);

        db.close();
    }

    public void AddActivitySubCategory(clsActivitySubCategory ClsActCat) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();

        cv.put(colActivitySubCategoryName, ClsActCat.getName());
        cv.put(colActivitySubCategoryDiscription, ClsActCat.getDiscription());
        cv.put(colActivitySubCategoryParentId, ClsActCat.getParentId());
        cv.put(colActivitySubCategoryCreatedDate, ClsActCat.getCreatedDate());
        cv.put(colActivitySubCategoryCreatedBy, ClsActCat.getCreatedDate());
        cv.put(colActivitySubCategoryModifiedDate, ClsActCat.getModifiedBy());
        cv.put(colActivitySubCategoryModifiedBy, ClsActCat.getModifiedDate());
        cv.put(colActivitySubCategoryMasterId, ClsActCat.getMasterId());

        db.insert(ActivitySubCategoryTable, colActivitySubCategoryName, cv);
        db.close();
    }

    public void AddBranch(clsBranch ClsActCat) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();

        cv.put(colBranchName, ClsActCat.getName());
        cv.put(colBranchDiscription, ClsActCat.getDiscription());
        cv.put(colBranchParentId, ClsActCat.getParentId());
        cv.put(colBranchCreatedDate, ClsActCat.getCreatedDate());
        cv.put(colABranchCreatedBy, ClsActCat.getCreatedDate());
        cv.put(colBranchModifiedDate, ClsActCat.getModifiedBy());
        cv.put(colBranchModifiedBy, ClsActCat.getModifiedDate());
        cv.put(colBranchUserId, ClsActCat.getUserId());

        db.insert(BranchTable, colBranchName, cv);
        db.close();
    }

    public void AddParam(clsParamList ClsActCat) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();

        cv.put(colParamName, ClsActCat.getName());
        cv.put(colParamDiscription, ClsActCat.getDiscription());
        cv.put(colParamParentId, ClsActCat.getParentId());
        cv.put(colParamCreatedDate, ClsActCat.getCreatedDate());
        cv.put(colParamCreatedBy, ClsActCat.getCreatedDate());
        cv.put(colParamModifiedDate, ClsActCat.getModifiedBy());
        cv.put(colParamModifiedBy, ClsActCat.getModifiedDate());

        db.insert(ParamListTable, colParamName, cv);
        db.close();
    }

    public void AddDefineObjective(clsDefineObjective ClsActCat) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();

        cv.put(colDefineObjectiveBranchCode, ClsActCat.getCode());
        cv.put(colDefineObjectiveBranchName, ClsActCat.getName());
        cv.put(colDefineObjectiveNewBusCash, ClsActCat.getNewBusCash());
        cv.put(colDefineObjectiveHomeLoan, ClsActCat.getHomeLoan());
        cv.put(colDefineObjectiveNewBusPre, ClsActCat.getNewBusPre());
        cv.put(colDefineObjectiveShare, ClsActCat.getShare());
        cv.put(colDefineObjectiveRemark, ClsActCat.getRemark());
        cv.put(colDefineObjectiveUserId, ClsActCat.getUserId());

        db.insert(DefineObjectiveTable, colDefineObjectiveBranchCode, cv);
        db.close();
    }

    public int GetActivityCount() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cur = db
                .rawQuery("Select * from " + ActivityCategoryTable, null);
        int x = cur.getCount();
        cur.close();
        return x;
    }

    public Cursor GetAllActivityName() {
        SQLiteDatabase db = this.getWritableDatabase();

        return db
                .rawQuery("Select * from " + ActivityCategoryTable, null);
    }

    public String getActivityId(String name) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor c = db.query(ActivityCategoryTable, new String[]{
                colActivityCategoryID, " ActivityCategoryID",
                colActivityCategoryParentId}, colActivityCategoryName + "="
                + "'" + name + "'", null, null, null, null, null);

        int count = c.getCount();

        if (count == 0) {
            c.close();
            return "";
        } else {

            c.moveToFirst();
            int index = c.getColumnIndex(colActivityCategoryParentId);
            String indexString = c.getString(index);
            c.close();
            return indexString;
        }
    }

    public int GetSubActivityCount(String id) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor c = db.query(ActivitySubCategoryTable, new String[]{
                colActivitySubCategoryID, " ActivitySubCategoryID",
                colActivitySubCategoryName}, colActivitySubCategoryParentId
                + "=" + "'" + id + "'", null, null, null, null, null);

        int x = c.getCount();
        c.close();
        if (x == 0) {
            return 0;
        } else {
            return 1;
        }
    }

    public Cursor getSubActivityName(String parentid) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor c = null;
        try {

            c = db.query(
                    ActivitySubCategoryTable,
                    new String[]{colActivitySubCategoryID,
                            " ActivitySubCategoryID",
                            colActivitySubCategoryName},
                    colActivitySubCategoryParentId + "=" + "'" + parentid + "'",
                    null, null, null, null, null);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return c;
    }


    public String GetSubCategoryMasterId(String catid, String subcatname) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor c = db.query(ActivitySubCategoryTable, new String[]{
                        colActivitySubCategoryID, " ActivitySubCategoryID",
                        colActivitySubCategoryMasterId},
                colActivitySubCategoryParentId + "=" + "'" + catid + "'"
                        + "AND " + colActivitySubCategoryName + "=" + "'"
                        + subcatname + "'", null, null, null, null, null);

        c.moveToFirst();
        int index = c.getColumnIndex(colActivitySubCategoryMasterId);
        String indexString = c.getString(index);
        c.close();
        return indexString;
    }

    public int GetBranchCount(String userid) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor c = db.query(BranchTable, new String[]{colBranchID,
                " BranchID", colBranchName}, colBranchUserId + "=" + "'"
                + userid + "'", null, null, null, null, null);

        int x = c.getCount();
        c.close();
        if (x == 0) {
            return 0;
        } else {
            return 1;
        }
    }

    public int GetParamCount() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cur = db.rawQuery("Select * from " + ParamListTable, null);
        int x = cur.getCount();
        cur.close();
        return x;
    }

    public Cursor GetAllBranchName(String userid) {
        SQLiteDatabase db = this.getWritableDatabase();

        return db.rawQuery("Select * from " + BranchTable + " where "
                + colBranchUserId + " = " + "'" + userid + "'", null);
    }

    public Cursor GetAllParamName() {
        SQLiteDatabase db = this.getWritableDatabase();

        return db.rawQuery("Select * from " + ParamListTable, null);
    }

    public String getBranchName(String code) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor c = db.query(BranchTable, new String[]{colBranchID,
                " BranchID", colBranchName}, colBranchParentId + "=" + "'"
                + code + "'", null, null, null, null, null);

        int count = c.getCount();

        if (count == 0) {
            c.close();
            return "";
        } else {

            c.moveToFirst();
            int index = c.getColumnIndex(colBranchName);
            String indexString = c.getString(index);
            c.close();
            return indexString;
        }
    }

    public int DefineObjectiveExistorNot(String code, String userid) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor c = db.query(DefineObjectiveTable, new String[]{
                colDefineObjectiveID, " DefineObjectiveID",
                colDefineObjectiveBranchCode}, colDefineObjectiveBranchCode
                + "=" + "'" + code + "'" + "AND " + colDefineObjectiveUserId
                + "=" + "'" + userid + "'", null, null, null, null, null);

        int x = c.getCount();
        c.close();
        if (x == 0) {
            return 0;
        } else {
            return 1;
        }
    }

    public Cursor GetDefineObjectiveRecord(String code, String userid) {
        SQLiteDatabase db = this.getWritableDatabase();

        return db.rawQuery("Select * from " + DefineObjectiveTable
                + " where " + colDefineObjectiveBranchCode + " = " + "'" + code
                + "'" + "AND " + colDefineObjectiveUserId + "=" + "'" + userid
                + "'", null);
    }

    public Cursor GetDefineObjectiveAllRecord(String userid) {
        SQLiteDatabase db = this.getWritableDatabase();

        return db.rawQuery("Select * from " + DefineObjectiveTable
                + " where " + colDefineObjectiveUserId + " = " + "'" + userid
                + "'", null);
    }

    public void UpdateActivityRecord(clsBDMTrackerCalendar log, String id) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();

        cv.put(colDateNameBDMT, log.getDateName());
        cv.put(colEventNameBDMT, log.getEventName());
        cv.put(colMonthBDMT, log.getMonth());
        cv.put(colYearBDMT, log.getYear());
        cv.put(colTimeBDMT, log.getTime());
        cv.put(colRemark, log.getRemark());
        cv.put(colActivity, log.getActivity());
        cv.put(colSubActivity, log.getSubActivity());
        cv.put(colTimeTo, log.getTimeTo());
        cv.put(colActivityStatus, log.getStatus());
        cv.put(colActivitySync, log.getSync());
        cv.put(colActivityCreatedDate, log.getCreatedDate());
        cv.put(colActivityModifiedDate, log.getModifiedDate());
        cv.put(colActivityServerMasterId, log.getServerMasterId());
        cv.put(colActivityLead, log.getLead());

        db.update(DateTableBDMT, cv, colDateIDBDMT + "=" + id, null);
        db.close();

    }

    public void UpdateActivityRecordAR(clsCalendarActivityRecorder log, String id) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();

        cv.put(colDateNameAR, log.getDateName());
        cv.put(colEventNameAR, log.getEventName());
        cv.put(colMonthAR, log.getMonth());
        cv.put(colYearAR, log.getYear());
        cv.put(colTimeAR, log.getTime());
        cv.put(colRemarkAR, log.getRemark());
        cv.put(colActivityAR, log.getActivity());
        cv.put(colSubActivityAR, log.getSubActivity());
        cv.put(colTimeToAR, log.getTimeTo());
        cv.put(colActivitySyncAR, log.getSync());
        cv.put(colActivityCreatedDateAR, log.getCreatedDate());
        cv.put(colActivityModifiedDateAR, log.getModifiedDate());
        cv.put(colActivityStatusAR, log.getStatus());
        cv.put(colActivityLeadAR, log.getLead());

        db.update(DateTableAR, cv, colDateIDAR + "=" + id, null);
        db.close();

    }


    public void AddEmail(clsEmail objEmail) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();

        cv.put(colEmailType, objEmail.getType());
        cv.put(colEmailName, objEmail.getName());
        cv.put(colEmailCreatedBy, objEmail.getCreatedBy());
        cv.put(colEmailCreatedDate, objEmail.getCreatedDate());
        cv.put(colEmailModifiedBy, objEmail.getModifiedBy());
        cv.put(colEmailModifiedDate, objEmail.getModifiedDate());
        cv.put(colEmailUserId, objEmail.getUserId());

        db.insert(EmailTable, colEmailType, cv);
        db.close();
    }

    public Cursor getGroupListEmail(String grouptype, String userid) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor c = null;
        try {

            c = db.query(EmailTable, new String[]{colEmailID, " EmailID",
                            colEmailName}, colEmailType + "=" + "'" + grouptype + "'"
                            + "AND " + colEmailUserId + "=" + "'" + userid + "'", null,
                    null, null, null, null);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return c;
    }

    public String getEmailGroup(String emailid) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor c = db.query(EmailTable, new String[]{colEmailID, " EmailID",
                        colEmailType}, colEmailName + "=" + "'" + emailid + "'", null,
                null, null, null, null);

        int count = c.getCount();

        if (count == 0) {
            c.close();
            return "";
        } else {

            c.moveToFirst();
            int index = c.getColumnIndex(colEmailType);
            String indexString = c.getString(index);
            c.close();
            return indexString;
        }
    }

    public void UpdateEmail(clsEmail log, String id) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();

        cv.put(colEmailType, log.getType());
        cv.put(colEmailName, log.getName());
        cv.put(colEmailCreatedBy, log.getCreatedBy());
        cv.put(colEmailCreatedDate, log.getCreatedDate());
        cv.put(colEmailModifiedBy, log.getModifiedBy());
        cv.put(colEmailModifiedDate, log.getModifiedDate());

        db.update(EmailTable, cv, colEmailID + "=" + id, null);
        db.close();

    }

    public Cursor GetAllEmail() {
        SQLiteDatabase db = this.getWritableDatabase();

        return db.rawQuery("Select * from " + EmailTable, null);
    }

    public int EmailExistorNot(String type, String email) {
        SQLiteDatabase db = this.getWritableDatabase();

        // for first name and password

        /*
         * Cursor c = db.query(LoginTable, new String[] { colLoginID,
         * " LoginID", colLoginName }, colLoginName + "=" + "'" + name + "'" +
         * "AND " + colLoginPassword + "=" + "'" + pass + "'", null, null, null,
         * null, null);
         */

        // for emailid and password

        Cursor c = db.query(EmailTable, new String[]{colEmailID, " EmailID",
                        colEmailName}, colEmailType + "=" + "'" + type + "'" + "AND "
                        + colEmailName + "=" + "'" + email + "'", null, null, null,
                null, null);

        int x = c.getCount();
        c.close();
        if (x == 0) {
            return 0;
        } else {
            return 1;
        }
    }

    public String getEmailRowId(String emailid) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor c = db.query(EmailTable, new String[]{colEmailID, " EmailID",
                        colEmailID}, colEmailName + "=" + "'" + emailid + "'", null,
                null, null, null, null);

        int count = c.getCount();

        if (count == 0) {
            c.close();
            return "";
        } else {

            c.moveToFirst();
            int index = c.getColumnIndex(colEmailID);
            String indexString = c.getString(index);
            c.close();
            return indexString;
        }
    }

    public void AddActivityRecordEvent(clsCalendarActivityRecorder log) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();

        cv.put(colDateNameAR, log.getDateName());
        cv.put(colEventNameAR, log.getEventName());
        cv.put(colMonthAR, log.getMonth());
        cv.put(colYearAR, log.getYear());
        cv.put(colTimeAR, log.getTime());
        cv.put(colRemarkAR, log.getRemark());
        cv.put(colActivityAR, log.getActivity());
        cv.put(colSubActivityAR, log.getSubActivity());
        cv.put(colTimeToAR, log.getTimeTo());
        cv.put(colUserIDAR, log.getUserId());
        cv.put(colActivitySyncAR, log.getSync());
        cv.put(colActivityCreatedDateAR, log.getCreatedDate());
        cv.put(colActivityModifiedDateAR, log.getModifiedDate());
        cv.put(colActivityStatusAR, log.getStatus());
        cv.put(colActivityLeadAR, log.getLead());

        db.insert(DateTableAR, colDateNameAR, cv);
        db.close();
    }

    public Cursor GetAllActivityRecord(String userid) {
        SQLiteDatabase db = this.getWritableDatabase();

        return db.rawQuery("Select * from " + DateTableAR + " where "
                + colUserIDAR + " = " + "'" + userid + "'", null);
    }

    public Cursor GetDataBetweenTowDate(String startdate, String enddate, String userid) {

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cur = db.rawQuery("Select * from " + DateTableBDMT + " where "
                + "date(" + colDateNameBDMT + ")" + " >= " + "date('"
                + startdate + "')" + " AND " + colDateNameBDMT + " <= "
                + "date('" + enddate + "')" + "AND " + colUserID + " = " + "'"
                + userid + "'", null);

        cur.getCount();

        return cur;
    }


    public String getSync(String id) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor c = db.query(DateTableBDMT, new String[]{colDateIDBDMT,
                " DateIDBDMT", colActivitySync}, colDateIDBDMT + "=" + "'"
                + id + "'", null, null, null, null, null);

        int count = c.getCount();

        if (count == 0) {
            c.close();
            return "";
        } else {

            c.moveToFirst();
            int index = c.getColumnIndex(colActivitySync);
            String indexString = c.getString(index);
            c.close();
            return indexString;
        }
    }


    public Cursor UpdateSynsStatus(String rowid) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor c = null;
        try {

            c = db.query(DateTableBDMT, new String[]{colDateIDBDMT,
                            " DateIDBDMT", colDateNameBDMT, colEventNameBDMT,
                            colMonthBDMT, colYearBDMT, colTimeBDMT, colRemark,
                            colActivity, colSubActivity, colTimeTo, colUserID,
                            colActivityStatus, colActivitySync, colActivityCreatedDate,
                            colActivityModifiedDate, colActivityServerMasterId,
                            colActivityLead}, colDateIDBDMT + "=" + "'" + rowid + "'",
                    null, null, null, null, null);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return c;
    }

    /*
     * Cursor UpdateSynsStatusAR(String rowid) { SQLiteDatabase db =
     * this.getWritableDatabase();
     *
     * Cursor c = null; try {
     *
     * c = db.query(DateTableAR, new String[] { colDateIDAR, " DateIDAR",
     * colDateNameAR,colEventNameAR,colMonthAR,colYearAR,colTimeAR
     * ,colRemarkAR,colActivityAR
     * ,colSubActivityAR,colTimeToAR,colUserIDAR,colActivitySyncAR
     * ,colActivityCreatedDateAR
     * ,colActivityModifiedDateAR,colActivityStatusAR,colActivityLeadAR},
     * colDateIDAR + "=" + "'" + rowid + "'", null, null, null, null, null);
     *
     * } catch (Exception e) { e.printStackTrace(); }
     *
     * return c; }
     */

    public Cursor UpdateSynsStatusAR(String rowid) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor c = null;
        try {

            c = db.query(DateTableAR, new String[]{colDateIDAR, " DateIDAR",
                    colDateNameAR, colEventNameAR, colMonthAR, colYearAR,
                    colTimeAR, colRemarkAR, colActivityAR, colSubActivityAR,
                    colTimeToAR, colUserIDAR, colActivitySyncAR,
                    colActivityCreatedDateAR, colActivityModifiedDateAR,
                    colActivityStatusAR, colActivityLeadAR}, colDateIDAR + "="
                    + "'" + rowid + "'", null, null, null, null, null);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return c;
    }

    // for branch profile menu

    public void AddBranchProfile(clsBranchProfile log) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();

        cv.put(colBranchCode, log.getBranchcode());
        cv.put(colBranchProfileName, log.getBranch_name());
        cv.put(colBranchOpenDate, log.getBranch_open_date());
        cv.put(colBranchNetResult, log.getBranch_net_result());
        cv.put(colBranchProfileCreatedDate, log.getBranch_created_date());

        db.insert(BranchProfileTable, colBranchCode, cv);
        db.close();
    }

    public void AddBranchAdvances(clsBranchAdvances log) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();

        cv.put(colBranch_Advances_ID, log.getAdvancesid());
        cv.put(colBranch_Advances_BranchCode, log.getBranchcode());
        cv.put(colTot_no_of_Acc, log.getTot_no_of_acc());
        cv.put(colTot_outstanding, log.getTot_out());
        cv.put(colNo_of_acc_b1l, log.getNo_of_acc_b1l());
        cv.put(colTot_outstanding_b1l, log.getTot_out_b1l());
        cv.put(colNo_of_acc_1lto5l, log.getNo_of_acc_1lto5l());
        cv.put(colTot_outstanding_1lto5l, log.getTot_out_1lto5l());
        cv.put(colNo_of_acc_a5l, log.getNo_of_acc_a5l());
        cv.put(colTot_outstanding_a5l, log.getTot_out_a5l());
        cv.put(colBranchAdvances_Category, log.getCategory());

        db.insert(BranchAdvancesTable, colBranch_Advances_ID, cv);
        db.close();
    }

    public void AddBranchDeposits(clsBranchDeposits log) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();

        cv.put(colBranch_Deposits_ID, log.getDepositid());
        cv.put(colBranch_Deposits_BranchCode, log.getBranchcode());
        cv.put(colTot_amount, log.getTot_amt());
        cv.put(colNew_acc_b1k, log.getNew_acc_b1k());
        cv.put(colNew_bal_b1k, log.getNew_balance_b1k());
        cv.put(colNew_acc_10kto1l, log.getNew_acc_10kto1l());
        cv.put(colNew_bal_10kto1l, log.getNew_bal_10kto1l());
        cv.put(colNew_acc_1lto5l, log.getNew_acc_1lto5l());
        cv.put(colNew_bal_1lto5l, log.getNew_bal_1lto5l());
        cv.put(colNew_acc_5landA, log.getNew_acc_5landA());
        cv.put(colNew_bal_5landA, log.getNew_bal_5landA());
        cv.put(colBranchDeposits_Category, log.getCategory());

        db.insert(BranchDepositsTable, colBranch_Deposits_ID, cv);
        db.close();
    }


    public void AddSyncBranchProfile(clsSyncBranchProfile log) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();

        cv.put(colSyncBranchCode, log.getBranchcode());
        cv.put(colSyncBranchPerticular, log.getPerticular());
        cv.put(colSyncBranchCategory, log.getCategory());
        cv.put(colSyncBranchCrossSalingProduct, log.getCross_salling());
        cv.put(colSyncBranchCrossSalingPotenNops, log.getCross_salling_nops());
        cv.put(colSyncBranchCrossSalingPotenPre, log.getCross_salling_pre());
        cv.put(colSyncBranchUserId, log.getUserid());

        db.insert(SyncBranchProfileTable, colSyncBranchCode, cv);
        db.close();
    }

    public int BranchProfileExistorNot(String branchcode) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.query(BranchProfileTable,
                new String[]{"BranchCode", "BranchOpenDate",
                        "BranchNetResult", "BranchProfileCreatedDate"},
                colBranchCode + "=" + "'" + branchcode + "'", null, null, null,
                null, null);

        int x = c.getCount();
        c.close();
        if (x == 0) {
            return 0;
        } else {
            return 1;
        }
    }

    public int BranchAdvancesExistorNot(String branchcode) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.query(BranchAdvancesTable,
                new String[]{"Branch_Advances_ID"},
                colBranch_Advances_BranchCode + "=" + "'" + branchcode + "'",
                null, null, null, null, null);

        int x = c.getCount();
        c.close();
        if (x == 0) {
            return 0;
        } else {
            return 1;
        }
    }

    public int BranchDepositsExistorNot(String branchcode) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.query(BranchDepositsTable,
                new String[]{"Branch_Deposits_ID"},
                colBranch_Deposits_BranchCode + "=" + "'" + branchcode + "'",
                null, null, null, null, null);

        int x = c.getCount();
        c.close();
        if (x == 0) {
            return 0;
        } else {
            return 1;
        }
    }


    public int SyncBranchProfileExistorNot(String branchcode,
                                           String perticular, String category) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.query(SyncBranchProfileTable,
                new String[]{"SyncBranchCode"}, colSyncBranchCode + "="
                        + "'" + branchcode + "'" + "AND "
                        + colSyncBranchPerticular + "=" + "'" + perticular
                        + "'" + "AND " + colSyncBranchCategory + "=" + "'"
                        + category + "'", null, null, null, null, null);

        int x = c.getCount();
        c.close();

        if (x == 0) {
            return 0;
        } else {
            return 1;
        }
    }

    public Cursor GetBranchProfile(String branchcode) {
        SQLiteDatabase db = this.getReadableDatabase();

        return db.query(BranchProfileTable, new String[]{"BranchCode",
                "BranchProfileName", "BranchOpenDate", "BranchNetResult",
                "BranchProfileCreatedDate"}, colBranchCode + "=" + "'"
                + branchcode + "'", null, null, null, null, null);
    }

    // for deposits

    public String GetBranchDepositsB1K(String branchcode, String perticular) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.query(BranchDepositsTable,
                new String[]{"New_acc_b1k"}, colBranch_Deposits_BranchCode
                        + "=" + "'" + branchcode + "'" + "AND "
                        + colBranchDeposits_Category + "=" + "'" + perticular
                        + "'", null, null, null, null, null);

        c.moveToFirst();
        if (c.moveToFirst()) {
            int index = c.getColumnIndex(colNew_acc_b1k);
            String indexString = c.getString(index);
            c.close();
            return indexString;
        } else {
            c.close();
            return "0";
        }

    }

    public String GetBranchDeposits10Kto1L(String branchcode, String perticular) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.query(BranchDepositsTable,
                new String[]{"New_acc_10kto1l"},
                colBranch_Deposits_BranchCode + "=" + "'" + branchcode + "'"
                        + "AND " + colBranchDeposits_Category + "=" + "'"
                        + perticular + "'", null, null, null, null, null);

        c.moveToFirst();
        int index = c.getColumnIndex(colNew_acc_10kto1l);
        String indexString = c.getString(index);
        c.close();
        return indexString;

    }

    public String GetBranchDeposits1Lto5L(String branchcode, String perticular) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.query(BranchDepositsTable,
                new String[]{"New_acc_1lto5l"},
                colBranch_Deposits_BranchCode + "=" + "'" + branchcode + "'"
                        + "AND " + colBranchDeposits_Category + "=" + "'"
                        + perticular + "'", null, null, null, null, null);

        c.moveToFirst();
        int index = c.getColumnIndex(colNew_acc_1lto5l);
        String indexString = c.getString(index);
        c.close();
        return indexString;

    }

    public String GetBranchDeposits5LandA(String branchcode, String perticular) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.query(BranchDepositsTable,
                new String[]{"New_acc_5landA"},
                colBranch_Deposits_BranchCode + "=" + "'" + branchcode + "'"
                        + "AND " + colBranchDeposits_Category + "=" + "'"
                        + perticular + "'", null, null, null, null, null);

        c.moveToFirst();
        int index = c.getColumnIndex(colNew_acc_5landA);
        String indexString = c.getString(index);
        c.close();
        return indexString;

    }

    // for advances

    public String GetBranchAdvacesB1L(String branchcode, String perticular) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.query(BranchAdvancesTable,
                new String[]{"No_of_acc_b1l"}, colBranch_Advances_BranchCode
                        + "=" + "'" + branchcode + "'" + "AND "
                        + colBranchAdvances_Category + "=" + "'" + perticular
                        + "'", null, null, null, null, null);

        c.moveToFirst();
        if (c.moveToFirst()) {
            int index = c.getColumnIndex(colNo_of_acc_b1l);
            String indexString = c.getString(index);
            c.close();
            return indexString;
        } else {
            c.close();
            return "";
        }

    }

    public String GetBranchAdvaces1Lto5L(String branchcode, String perticular) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.query(BranchAdvancesTable,
                new String[]{"No_of_acc_1lto5l"},
                colBranch_Advances_BranchCode + "=" + "'" + branchcode + "'"
                        + "AND " + colBranchAdvances_Category + "=" + "'"
                        + perticular + "'", null, null, null, null, null);

        c.moveToFirst();
        int index = c.getColumnIndex(colNo_of_acc_1lto5l);
        String indexString = c.getString(index);
        c.close();
        return indexString;

    }

    public String GetBranchAdvacesA5L(String branchcode, String perticular) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.query(BranchAdvancesTable,
                new String[]{"No_of_acc_a5l"}, colBranch_Advances_BranchCode
                        + "=" + "'" + branchcode + "'" + "AND "
                        + colBranchAdvances_Category + "=" + "'" + perticular
                        + "'", null, null, null, null, null);

        c.moveToFirst();
        int index = c.getColumnIndex(colNo_of_acc_a5l);
        String indexString = c.getString(index);
        c.close();
        return indexString;

    }

    public String GetBranchDepositsId(String branchcode, String perticular) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.query(BranchDepositsTable,
                new String[]{"Branch_Deposits_ID"},
                colBranch_Deposits_BranchCode + "=" + "'" + branchcode + "'"
                        + "AND " + colBranchDeposits_Category + "=" + "'"
                        + perticular + "'", null, null, null, null, null);

        c.moveToFirst();
        int index = c.getColumnIndex(colBranch_Deposits_ID);
        String indexString = c.getString(index);
        c.close();
        return indexString;

    }

    public String GetBranchAdvacesId(String branchcode, String perticular) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.query(BranchAdvancesTable,
                new String[]{"Branch_Advances_ID"},
                colBranch_Advances_BranchCode + "=" + "'" + branchcode + "'"
                        + "AND " + colBranchAdvances_Category + "=" + "'"
                        + perticular + "'", null, null, null, null, null);

        c.moveToFirst();
        int index = c.getColumnIndex(colBranch_Advances_ID);
        String indexString = c.getString(index);
        c.close();
        return indexString;

    }

    public Cursor GetBranchAandD_local(String branchcode, String perticular,
                                       String category, String userid) {
        SQLiteDatabase db = this.getReadableDatabase();

        return db.query(SyncBranchProfileTable, new String[]{
                "SyncBranchCrossSalingProduct",
                "SyncBranchCrossSalingPotenNops",
                "SyncBranchCrossSalingPotenPre"}, colSyncBranchCode + "="
                + "'" + branchcode + "'" + "AND " + colSyncBranchPerticular
                + "=" + "'" + perticular + "'" + "AND " + colSyncBranchCategory
                + "=" + "'" + category + "'" + "AND " + colSyncBranchUserId
                + "=" + "'" + userid + "'", null, null, null, null, null);
    }

    public void AddDefineObjectiveSync(clsDefineObjectiveSync log) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();

        cv.put(colDefineObjectiveSyncBRCode, log.getCode());
        cv.put(colDefineObjectiveSyncUserId, log.getUserId());
        cv.put(colDefineObjectiveSyncFlag, log.getSyncFlag());

        db.insert(DefineObjectiveSyncTable, colDefineObjectiveSyncBRCode, cv);
        db.close();
    }

    public String GetDefineObjectiveSyncRecord(String code, String userid) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.query(DefineObjectiveSyncTable,
                new String[]{"DefineObjectiveSyncFlag"},
                colDefineObjectiveSyncBRCode + "=" + "'" + code + "'" + "AND "
                        + colDefineObjectiveSyncUserId + "=" + "'" + userid
                        + "'", null, null, null, null, null);

        c.moveToFirst();
        if (c.moveToFirst()) {
            int index = c.getColumnIndex(colDefineObjectiveSyncFlag);
            String indexString = c.getString(index);
            c.close();
            return indexString;
        } else {
            c.close();
            return "False";
        }

    }

    // agent profile

    void AddAgentProfile(clsAgentProfile log) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();

        cv.put(colAgentName, log.getName());
        cv.put(colAgentLicenceNo, log.getLicenceNo());
        cv.put(colAgentLicenceIssuDate, log.getLicenceIssuDate());
        cv.put(colAgentLicenceExpiryDate, log.getLicenceExpDate());
        cv.put(colAgentActivationStartDate, log.getActivationStartDate());
        cv.put(colAgentProfileBranchName, log.getBranchName());
        cv.put(colAgentProfileRegion, log.getRegion());
        cv.put(colAgentProfileUMName, log.getUMName());
        cv.put(colAgentProfileStatus, log.getStatus());
        cv.put(colAgentProfileULIPCertified, log.getULIPCertified());

        db.insert(AgentProfileTable, colAgentName, cv);
        db.close();
    }

    Cursor GetAgentProfile() {
        SQLiteDatabase db = this.getWritableDatabase();

        return db.rawQuery("Select * from " + AgentProfileTable, null);
    }

    public int GetAgentProfileId() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cur = db.rawQuery("Select * from " + AgentProfileTable, null);
        int x = cur.getCount();
        cur.close();
        return x;
    }

    // HO Lead

    public void AddHOLead(clsHOLead log) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();

        cv.put(colHOLeadDate, log.get_date());
        cv.put(colHOLeadCustomerId, log.get_custid());
        cv.put(colHOLeadCustomerName, log.get_custname());
        cv.put(colHOLeadPriority, log.get_priority());
        cv.put(colHOLeadStatus, log.get_status());
        cv.put(colHOLeadSubStatus, log.get_substatus());
        cv.put(colHOLeadProposalNo, log.get_proposalno());
        cv.put(colHOLeadFollowUpDate, log.get_followupdate());
        cv.put(colHOLeadComments, log.get_comments());
        cv.put(colHOLeadAge, log.get_age());
        cv.put(colHOLeadTotalAcc, log.get_totalacc());
        cv.put(colHOLeadBalance, log.get_balance());
        cv.put(colHOLeadBranchCode, log.get_branchcode());
        cv.put(colHOLeadUserID, log.get_userid());
        cv.put(colHOLeadSync, log.get_sync());
        cv.put(colHOLeadServerID, log.get_leadid());
        cv.put(colHOLeadBDMName, log.get_name());
        cv.put(colHOLeadSource, log.get_source());

        db.insert(HOLeadTable, colHOLeadDate, cv);
        db.close();
    }

    public void UpdateHOLead(clsHOLead log, String id) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();

        cv.put(colHOLeadDate, log.get_date());
        cv.put(colHOLeadCustomerId, log.get_custid());
        cv.put(colHOLeadCustomerName, log.get_custname());
        cv.put(colHOLeadPriority, log.get_priority());
        cv.put(colHOLeadStatus, log.get_status());
        cv.put(colHOLeadSubStatus, log.get_substatus());
        cv.put(colHOLeadProposalNo, log.get_proposalno());
        cv.put(colHOLeadFollowUpDate, log.get_followupdate());
        cv.put(colHOLeadComments, log.get_comments());
        cv.put(colHOLeadAge, log.get_age());
        cv.put(colHOLeadTotalAcc, log.get_totalacc());
        cv.put(colHOLeadBalance, log.get_balance());
        cv.put(colHOLeadBranchCode, log.get_branchcode());
        cv.put(colHOLeadUserID, log.get_userid());
        cv.put(colHOLeadSync, log.get_sync());
        cv.put(colHOLeadServerID, log.get_leadid());
        cv.put(colHOLeadBDMName, log.get_name());
        cv.put(colHOLeadSource, log.get_source());

        db.update(HOLeadTable, cv, colHOLeadID + "=" + id, null);
        db.close();

    }


    public Cursor GetAllLead(String userid) {
        SQLiteDatabase db = this.getWritableDatabase();

        return db.rawQuery("Select * from " + HOLeadTable + " where "
                + colHOLeadUserID + "=" + "'" + userid + "'", null);
    }


    public List<ProductBIBean> GetNeedAnalysisDashboard(String userid) {
        Cursor cursor = null;
        List<ProductBIBean> data_List = new ArrayList<>();

        SQLiteDatabase db = this.getWritableDatabase();
        try {
			/*cursor = db.rawQuery("SELECT "+colUinNo+", COUNT(*) c FROM "+NeedAnalysisTable+" GROUP BY "+colUinNo+" HAVING c > 1",null);

			String duplicateUINNo =  cursor.getString(cursor
					.getColumnIndexOrThrow(colUinNo));
			if(cursor.getCount()>0){
				System.out.println("duplicateUINNo = " + duplicateUINNo);
			}*/
            cursor = db
                    .rawQuery("Select * from " + NeedAnalysisTable + " where "
                            + colCreatedBy + "=" + "'" + userid + "'", null);

            long count = cursor.getCount();
            if (count > 0) {
                cursor.moveToFirst();
                do {
                    /*
                     * cv.put(colQuotationNo,log.getQuotationNo());
                     * cv.put(colPlanSelected ,log.getPlanSelected());
                     * cv.put(colProposalDate,log.getProposalDate());
                     * cv.put(colMobileNo ,log.getMobileNo()); cv.put(colUinNo
                     * ,log.getUinNo()); cv.put(colSyncStatus
                     * ,log.getSyncStatus()); cv.put(colCreatedDate
                     * ,log.getCreatedDate());
                     * cv.put(colCreatedBy,log.getCreatedBy());
                     * cv.put(colemail,log.getEmail());
                     *
                     * cv.put(colsr_code,log.getSr_code());
                     * cv.put(colsr_sr_code,log.getSr_sr_code());
                     * cv.put(colsr_type,log.getSr_type());
                     * cv.put(colsr_sr_type,log.getSr_sr_type());
                     * cv.put(colcust_title,log.getCust_title());
                     * cv.put(colcust_first_name,log.getCust_first_name());
                     * cv.put(colcust_mid_name,log.getCust_mid_name());
                     * cv.put(colcust_last_name,log.getCust_last_name());
                     * cv.put(colsumassured,log.getSumassured());
                     * cv.put(colpremium,log.getPremium());
                     * cv.put(colsr_email,log.getSr_email());
                     * cv.put(colsr_mobile,log.getSr_mobile());
                     * cv.put(colna_input,log.getNa_input());
                     * cv.put(colna_output,log.getNa_output());
                     * cv.put(colfrequency,log.getFrequency());
                     * cv.put(colpolicyTerm,log.getPolicyTerm());
                     * cv.put(colprem_paying_term,log.getPrem_paying_term());
                     * cv.put(colplan_code,log.getPlan_code());
                     * cv.put(colLA_dob,log.getLA_dob());
                     * cv.put(colproposer_dob,log.getProposer_dob());
                     */

                    String colProductBIID = "ProductBIID";
                    String ProductBIID = cursor.getString(cursor
                            .getColumnIndexOrThrow(colProductBIID));

                    String QuotationNo = cursor.getString(cursor
                            .getColumnIndexOrThrow(colQuotationNo));

                    String PlanSelected = cursor.getString(cursor
                            .getColumnIndexOrThrow(colPlanSelected));

                    String ProposalDate = cursor.getString(cursor
                            .getColumnIndexOrThrow(colProposalDate));

                    String MobileNo = cursor.getString(cursor
                            .getColumnIndexOrThrow(colMobileNo));

                    String CreatedDate = cursor.getString(cursor
                            .getColumnIndexOrThrow(colCreatedDate));

                    String CreatedBy = cursor.getString(cursor
                            .getColumnIndexOrThrow(colCreatedBy));

                    String email = cursor.getString(cursor
                            .getColumnIndexOrThrow(colemail));

                    String syncStatus = cursor.getString(cursor
                            .getColumnIndexOrThrow(colSyncStatus));

                    String uinNo = cursor.getString(cursor
                            .getColumnIndexOrThrow(colUinNo));

                    String sr_code = cursor.getString(cursor
                            .getColumnIndexOrThrow(colsr_code));

                    String sr_sr_code = cursor.getString(cursor
                            .getColumnIndexOrThrow(colsr_sr_code));

                    String sr_type = cursor.getString(cursor
                            .getColumnIndexOrThrow(colsr_type));

                    String sr_sr_type = cursor.getString(cursor
                            .getColumnIndexOrThrow(colsr_sr_type));

                    String cust_title = cursor.getString(cursor
                            .getColumnIndexOrThrow(colcust_title));

                    String cust_firstname = cursor.getString(cursor
                            .getColumnIndexOrThrow(colcust_first_name));
                    String cust_midname = cursor.getString(cursor
                            .getColumnIndexOrThrow(colcust_mid_name));

                    String cust_lastname = cursor.getString(cursor
                            .getColumnIndexOrThrow(colcust_last_name));

                    String sumassured = cursor.getString(cursor
                            .getColumnIndexOrThrow(colsumassured));

                    String premium = cursor.getString(cursor
                            .getColumnIndexOrThrow(colpremium));
                    String sr_email = cursor.getString(cursor
                            .getColumnIndexOrThrow(colsr_email));
                    String sr_mobile = cursor.getString(cursor
                            .getColumnIndexOrThrow(colsr_mobile));
                    String na_input = cursor.getString(cursor
                            .getColumnIndexOrThrow(colna_input));
                    String na_output = cursor.getString(cursor
                            .getColumnIndexOrThrow(colna_output));
                    String frequency = cursor.getString(cursor
                            .getColumnIndexOrThrow(colfrequency));

                    String policyTermString = cursor.getString(cursor
                            .getColumnIndexOrThrow(colpolicyTerm));
                    int policyTerm = 0;
                    if (policyTermString != null) {
                        policyTerm = Integer.parseInt(policyTermString);
                    }

                    String prem_paying_termString = cursor.getString(cursor
                            .getColumnIndexOrThrow(colprem_paying_term));
                    int prem_paying_term = 0;
                    if (prem_paying_termString != null) {
                        prem_paying_term = Integer
                                .parseInt(prem_paying_termString);
                    }

                    String plan_code = cursor.getString(cursor
                            .getColumnIndexOrThrow(colplan_code));
                    String lA_dob = cursor.getString(cursor
                            .getColumnIndexOrThrow(colLA_dob));
                    String proposer_dob = cursor.getString(cursor
                            .getColumnIndexOrThrow(colproposer_dob));

                    String na_group = cursor.getString(cursor
                            .getColumnIndexOrThrow(colna_group));

                    String transactionMode = cursor.getString(cursor
                            .getColumnIndexOrThrow(col_transaction_mode));

                    String bi_inputVal = cursor.getString(cursor
                            .getColumnIndexOrThrow(col_bi_inputVal));

                    String bi_outputVal = cursor.getString(cursor
                            .getColumnIndexOrThrow(col_bi_outputVal));


                    ProductBIBean productBIBean = new ProductBIBean(ProductBIID, QuotationNo,
                            PlanSelected, ProposalDate, MobileNo, CreatedDate,
                            CreatedBy, email, syncStatus, uinNo, sr_code,
                            sr_sr_code, sr_type, sr_sr_type, cust_title,
                            cust_firstname, cust_midname, cust_lastname,
                            sumassured, premium, sr_email, sr_mobile, na_input,
                            na_output, frequency, policyTerm, prem_paying_term,
                            plan_code, lA_dob, proposer_dob, na_group, transactionMode, bi_inputVal,
                            bi_outputVal);


                    String str = uinNo.trim();
                    for (ProductBIBean node : new ArrayList<>(data_List)) {
                        String UINNumber = node.getUinNo().trim();
                        if (UINNumber.equalsIgnoreCase(str)) {
                            data_List.remove(node);
                        }
                    }

                    data_List.add(productBIBean);
                    System.out.println("productBIBean.toString() = " + productBIBean.toString());

                } while (cursor.moveToNext());
                cursor.close();
            }
        } catch (Exception e) {
            // TODO: handle exception
            Log.e("NewDBHelper", e.toString() + "Error in getting BI Detail");
        } finally {
            if (cursor != null) {
                db.close();
            }
        }
        //removeDuplicates(data_List)
        return data_List;
    }

    public Cursor GetAllLead_BasedOnPriority(String userid, String priority) {
        SQLiteDatabase db = this.getWritableDatabase();

        return db.rawQuery("Select * from " + HOLeadTable + " where "
                + colHOLeadUserID + "=" + "'" + userid + "'" + "AND "
                + colHOLeadPriority + "=" + "'" + priority + "'", null);
    }

    public Cursor GetAllLead_BasedOnStatus(String userid, String status) {
        SQLiteDatabase db = this.getWritableDatabase();

        return db.rawQuery("Select * from " + HOLeadTable + " where "
                + colHOLeadUserID + "=" + "'" + userid + "'" + "AND "
                + colHOLeadStatus + "=" + "'" + status + "'", null);
    }

    public Cursor GetDataBetweenTowHODate(String startdate, String enddate,
                                          String userid) {

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cur = db.rawQuery("Select * from " + HOLeadTable + " where "
                        + "date(" + colHOLeadDate + ")" + " >= " + "date('" + startdate
                        + "')" + " AND " + colHOLeadDate + " <= " + "date('" + enddate
                        + "')" + "AND " + colHOLeadUserID + " = " + "'" + userid + "'",
                null);

        cur.getCount();

        return cur;
    }

    public Cursor GetDataBetweenTowFollowUpDate(String startdate, String enddate,
                                                String userid) {

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cur = db.rawQuery("Select * from " + HOLeadTable + " where "
                + "date(" + colHOLeadFollowUpDate + ")" + " >= " + "date('"
                + startdate + "')" + " AND " + colHOLeadFollowUpDate + " <= "
                + "date('" + enddate + "')" + "AND " + colHOLeadUserID + " = "
                + "'" + userid + "'", null);

        cur.getCount();

        return cur;
    }

    public Cursor GetAllLead_BasedOnLeadNo(String userid, String leadno) {
        SQLiteDatabase db = this.getWritableDatabase();

        return db.rawQuery("Select * from " + HOLeadTable + " where "
                + colHOLeadUserID + "=" + "'" + userid + "'" + "AND "
                + colHOLeadServerID + "=" + "'" + leadno + "'", null);
    }

    public int GetHOLeadServerId(String leadserverid) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor c = db.query(HOLeadTable, new String[]{colHOLeadID,
                " HOLeadID", colHOLeadCustomerId}, colHOLeadServerID + "="
                + "'" + leadserverid + "'", null, null, null, null, null);

        int x = c.getCount();
        c.close();
        if (x == 0) {
            return 0;
        } else {
            return 1;
        }
    }

    public Cursor Get_HOLeadRowId(String userid, String leadid) {
        SQLiteDatabase db = this.getWritableDatabase();

        return db.rawQuery("Select * from " + HOLeadTable + " where "
                + colHOLeadUserID + "=" + "'" + userid + "'" + "AND "
                + colHOLeadServerID + "=" + "'" + leadid + "'", null);
    }

    public Cursor Get_HOLeadUpdateRowId(String userid, String leadid) {
        SQLiteDatabase db = this.getWritableDatabase();

        return db.rawQuery("Select * from " + HOLeadTable + " where "
                + colHOLeadUserID + "=" + "'" + userid + "'" + "AND "
                + colHOLeadID + "=" + "'" + leadid + "'", null);
    }

    public Cursor GetStatus_based_on_lead_id(String userid, String leadid) {
        SQLiteDatabase db = this.getWritableDatabase();

        return db.rawQuery("Select * from " + HOLeadTable + " where "
                + colHOLeadUserID + "=" + "'" + userid + "'" + "AND "
                + colHOLeadServerID + "=" + "'" + leadid + "'", null);
    }

    // end HO Lead

    /*
     * Saving Renewal Premium details
     */
    public long saveRenewalPremiumDetails(RenewalPremiumNBBean bean) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        System.out.println("Pay details:" + bean.getRp_nb_pay_mode() + ", "
                + bean.getRp_nb_pay_type() + ", "
                + bean.getRp_nb_payment_type());

        try {
            values.put(rp_nb_policy_no, bean.getRp_nb_policy_no());
            values.put(rp_nb_cust_name, bean.getRp_nb_cust_name());
            values.put(rp_nb_cust_dob, bean.getRp_nb_cust_dob());
            values.put(rp_nb_cust_mob, bean.getRp_nb_cust_mob());
            values.put(rp_nb_micr, bean.getRp_nb_micr());

            values.put(rp_nb_bank_name, bean.getRp_nb_bank_name());
            values.put(rp_nb_branch_name, bean.getRp_nb_branch_name());

            values.put(rp_nb_accnt_no, bean.getRp_nb_accnt_no());
            values.put(rp_nb_cheque_no, bean.getRp_nb_cheque_no());
            values.put(rp_nb_cheque_date, bean.getRp_nb_cheque_date());
            values.put(rp_nb_cheque_amt, bean.getRp_nb_cheque_amt());

            values.put(rp_nb_pay_mode, bean.getRp_nb_pay_mode());
            values.put(rp_nb_pay_type, bean.getRp_nb_pay_type());

            values.put(rp_nb_payment_type, bean.getRp_nb_payment_type());

            values.put(rp_nb_advisor_code, bean.getRp_nb_advisor_code());
            values.put(rp_nb_advisor_type, bean.getRp_nb_advisor_type());

            values.put(rp_nb_created_date, bean.getRp_nb_created_date());
            values.put(rp_nb_created_by, bean.getRp_nb_created_by());

            values.put(rp_nb_is_rp, bean.getRp_nb_is_rp());

            return db.insert(RenewalPremiumNewBusinessLogTable, null, values); // -1-exception
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        } finally {
            if (db.isOpen()) {
                db.close();
            }
        }
    }

    /*
     * Save New Business FP details
     */
    public long saveNewBusinessFPDetails(RenewalPremiumNBBean bean) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        System.out
                .println("Payment type in db:" + bean.getRp_nb_payment_type());

        try {
            values.put(rp_nb_proposal_no, bean.getRp_nb_proposal_no());
            values.put(rp_nb_cust_name, bean.getRp_nb_cust_name());
            values.put(rp_nb_cust_mob, bean.getRp_nb_cust_mob());
            values.put(rp_nb_micr, bean.getRp_nb_micr());

            values.put(rp_nb_bank_name, bean.getRp_nb_bank_name());
            values.put(rp_nb_branch_name, bean.getRp_nb_branch_name());

            values.put(rp_nb_accnt_no, bean.getRp_nb_accnt_no());
            values.put(rp_nb_cheque_no, bean.getRp_nb_cheque_no());
            values.put(rp_nb_cheque_date, bean.getRp_nb_cheque_date());
            values.put(rp_nb_cheque_amt, bean.getRp_nb_cheque_amt());

            values.put(rp_nb_pay_mode, bean.getRp_nb_pay_mode());
            values.put(rp_nb_pay_type, bean.getRp_nb_pay_type());

            values.put(rp_nb_payment_type, bean.getRp_nb_payment_type());

            values.put(rp_nb_advisor_code, bean.getRp_nb_advisor_code());
            values.put(rp_nb_advisor_type, bean.getRp_nb_advisor_type());

            values.put(rp_nb_created_date, bean.getRp_nb_created_date());
            values.put(rp_nb_created_by, bean.getRp_nb_created_by());

            values.put(rp_nb_is_rp, bean.getRp_nb_is_rp());

            return db.insert(RenewalPremiumNewBusinessLogTable, null, values); // -1-exception
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        } finally {
            if (db.isOpen())
                db.close();
        }
    }

    /*
     * Updating Renewal Premium sync flag
     */
    public int updateRenewalPreSyncFlag(long id, String syncStatus) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        final String[] whereArgs = {String.valueOf(id)};

        try {
            values.put(rp_nb_isSync, syncStatus); // Sync status 0 - not syncd,
            // 1- syncd

            return db.update(RenewalPremiumNewBusinessLogTable, values,
                    rp_nb_id + "=?", whereArgs);
        } catch (Exception e) {
            e.printStackTrace();
            return -2;
        } finally {
            if (db.isOpen()) {
                db.close();
            }
        }
    }

    /*
     * Get Renewal Premium details
     */
    public List<RenewalPremiumNBBean> getRenewalPremiumNBDetails(String isRP) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = null;
        List<RenewalPremiumNBBean> beans = null;

        try {

            if (isRP.equals("RP")) {

                c = db.rawQuery("SELECT " + rp_nb_id + ", " + rp_nb_isSync
                        + ", " + rp_nb_policy_no + ", " + rp_nb_cust_name
                        + ", " + rp_nb_accnt_no + ", " + rp_nb_micr + ", "
                        + rp_nb_cheque_no + ", " + rp_nb_bank_name + ", "
                        + rp_nb_branch_name + ", " + rp_nb_pay_mode + ", "
                        + rp_nb_pay_type + ", " + rp_nb_payment_type + ", "
                        + rp_nb_cheque_date + ", " + rp_nb_cheque_amt + ", "
                        + rp_nb_cust_mob + " FROM "
                        + RenewalPremiumNewBusinessLogTable + " WHERE "
                        + rp_nb_is_rp + " = '" + isRP + "' AND "
                        + rp_nb_isDeleted + " ='0'", null);
            } else if (isRP.equals("NB")) {
                c = db.rawQuery("SELECT " + rp_nb_id + ", " + rp_nb_isSync
                        + ", " + rp_nb_proposal_no + ", " + rp_nb_cust_name
                        + ", " + rp_nb_accnt_no + ", " + rp_nb_micr + ", "
                        + rp_nb_cheque_no + ", " + rp_nb_bank_name + ", "
                        + rp_nb_branch_name + ", " + rp_nb_pay_mode + ", "
                        + rp_nb_pay_type + ", " + rp_nb_payment_type + ", "
                        + rp_nb_cheque_date + ", " + rp_nb_cheque_amt + ", "
                        + rp_nb_cust_mob + " FROM "
                        + RenewalPremiumNewBusinessLogTable + " WHERE "
                        + rp_nb_is_rp + " = '" + isRP + "' AND "
                        + rp_nb_isDeleted + " ='0'", null);
            }

            if (c != null && c.moveToFirst()) {
                beans = new ArrayList<>();
                do {
                    System.out.println("RP DB details:" + c.getInt(0) + ","
                            + c.getString(1) + "," + c.getString(2) + ","
                            + c.getString(3) + "," + c.getString(4) + ", "
                            + c.getString(5) + ", " + c.getString(6) + ", "
                            + c.getString(7) + ", " + c.getString(8) + ", "
                            + c.getString(9) + ", " + c.getString(10) + ", "
                            + c.getString(11) + ", " + c.getString(12) + ", "
                            + c.getString(13) + ", " + c.getString(14));
                    RenewalPremiumNBBean bean = new RenewalPremiumNBBean();

                    bean.setRp_rp_nb_id(c.getInt(0));
                    bean.setRp_nb_isSync(c.getString(1));

                    if (isRP.equals("RP")) {
                        bean.setRp_nb_policy_no(c.getString(2));
                    } else if (isRP.equals("NB")) {
                        bean.setRp_nb_proposal_no(c.getString(2));
                    }

                    bean.setRp_nb_cust_name(c.getString(3));
                    bean.setRp_nb_accnt_no(c.getString(4));
                    bean.setRp_nb_micr(c.getString(5));
                    bean.setRp_nb_cheque_no(c.getString(6));
                    bean.setRp_nb_bank_name(c.getString(7));
                    bean.setRp_nb_branch_name(c.getString(8));
                    bean.setRp_nb_pay_mode(c.getString(9));
                    bean.setRp_nb_pay_type(c.getString(10));
                    bean.setRp_nb_payment_type(c.getString(11));
                    bean.setRp_nb_cheque_date(c.getString(12));
                    bean.setRp_nb_cheque_amt(c.getString(13));
                    bean.setRp_nb_cust_mob(c.getString(14));

                    beans.add(bean);

                } while (c.moveToNext());
            }
            assert c != null;
            c.close();
            return beans;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (db.isOpen())
                db.close();
        }
    }

    /*
     * Update edited RenewalPremiumNB details
     */
    public int updateRenewalPremiumNB(RenewalPremiumNBBean bean) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        final String[] whereArgs = {String.valueOf(bean.getRp_rp_nb_id())};

        try {
            values.put(rp_nb_accnt_no, bean.getRp_nb_accnt_no());
            values.put(rp_nb_cheque_no, bean.getRp_nb_cheque_no());
            values.put(rp_nb_cheque_date, bean.getRp_nb_cheque_date());
            values.put(rp_nb_cust_mob, bean.getRp_nb_cust_mob());

            return db.update(RenewalPremiumNewBusinessLogTable, values,
                    rp_nb_id + " =?", whereArgs); // -2 EXCEPTION
        } catch (Exception e) {
            e.printStackTrace();
            return -2;
        } finally {
            if (db.isOpen()) {
                db.close();
            }
        }
    }

    /*
     * Setting isDelete flag in Renewal Premium NB table
     */
    public int setDeleteFlagRPNB(RenewalPremiumNBBean bean, String isDeleted) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        final String[] whereArgs = {String.valueOf(bean.getRp_rp_nb_id())};

        try {
            values.put(rp_nb_isDeleted, isDeleted);

            return db.update(RenewalPremiumNewBusinessLogTable, values,
                    rp_nb_id + " =?", whereArgs); // -2 EXCEPTION
        } catch (Exception e) {
            e.printStackTrace();
            return -2;
        } finally {
            if (db.isOpen()) {
                db.close();
            }
        }
    }

    /*
     * Checking for duplicate cheque numbers
     */
    public boolean isDuplicateChequeNo(String chequeNo) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c;
        // List<RenewalPremiumNBBean> beans = null;
        boolean b = false;

        try {
            c = db.rawQuery("SELECT " + rp_nb_id + " FROM "
                    + RenewalPremiumNewBusinessLogTable + " WHERE "
                    + rp_nb_cheque_no + " = '" + chequeNo + "' AND "
                    + rp_nb_isSync + " = '1'", null);

            b = !c.moveToFirst();
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db.isOpen()) {
                db.close();
            }
        }

        return b;
    }

    /*** Added By - Priyanka Warekar - 28-10-2015 - Start ****/
    /*
     * it will get password of registered user
     */
    public String GetCustomerDOB(String policyNo) {
        SQLiteDatabase db = this.getReadableDatabase();

        try {
            // String whereClause=rp_nb_policy_no+"='"+policyNo+"'";
            // Cursor c = db.query(RenewalPremiumNewBusinessLogTable, new
            // String[] { rp_nb_cust_dob },
            // whereClause, null, null, null, rp_nb_created_date);
            String sql = "SELECT " + rp_nb_cust_dob + " FROM "
                    + RenewalPremiumNewBusinessLogTable + " WHERE "
                    + rp_nb_policy_no + "='" + policyNo + "'" + " ORDER BY "
                    + rp_nb_created_by + " DESC LIMIT 1";
            Cursor c = db.rawQuery(sql, null);
            c.moveToFirst();
            // int x=c.getCount();
            String indexString = c.getString(c.getColumnIndex(rp_nb_cust_dob));
            c.close();
            return indexString;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }

    }

    public List<M_DocumentUploadStatus> getDocumentsDetails(
            String Proposal_Number) {

        Cursor c;
        db = getReadableDatabase();

        String where = C_DU_PROPOSAL_NO + " = " + "'" + Proposal_Number + "'";

        c = getTableValue(TABLE_DOCUMENT_UPLOAD, null, where);
        List<M_DocumentUploadStatus> cust_details = new ArrayList<>();

        int count = c.getCount();
        try {

            String ProofOf;
            String DocumentName;
            String FileName;
            String SyncDate;

            if (count > 0) {
                c.moveToFirst();
                do {

                    ProofOf = c.getString(c
                            .getColumnIndex(C_DU_PROOF_OF));
                    DocumentName = c.getString(c
                            .getColumnIndex(C_DU_DOCUMENT_NAME));
                    FileName = c.getString(c
                            .getColumnIndex(C_DU_FILE_NAME));
                    SyncDate = c
                            .getString(c
                                    .getColumnIndex(C_DU_DOCUMENT_UPLOADDATE));

                    M_DocumentUploadStatus c_deatils = new M_DocumentUploadStatus(
                            ProofOf, DocumentName, FileName, SyncDate);
                    cust_details.add(c_deatils);

                } while (c.moveToNext());
            }
        } catch (Exception e) {
            // TODO: handle exception
            Log.e("NewDBHelper", e.toString()
                    + "Error in getting Document Upload Detail");
        } finally {
            db.close();
        }

        return cust_details;

    }

    public long insertDocumentUploadDetail(String Proposal_No,
                                           String QuatationNo, List<M_DocumentUpload> data) {
        long rowId = 0;
        try {
            db = getWritableDatabase();
            ContentValues cv = new ContentValues();
            for (int i = 0; i < data.size(); i++) {

                cv.put(C_DU_PROOF_OF, data.get(i).getProof_Of());
                cv.put(C_DU_PROPOSAL_NO, Proposal_No);
                cv.put(C_DU_QUOTATION_NO, QuatationNo);
                cv.put(C_DU_DOCUMENT_NAME, data.get(i).getDocument_Name());
                cv.put(C_DU_DOCUMENT_CONTENT, data.get(i).getDocument_Content());
                cv.put(C_DU_DOCUMENT_UPLOADDATE, data.get(i)
                        .getDocument_UploadDate());
                cv.put(C_DU_FILE_NAME, data.get(i).getFileName());
                cv.put(C_CREATED_BY, data.get(i).getCreatedBy());
                cv.put(C_CREATED_DATE, data.get(i).getCreatedDate());
                cv.put(C_MODIFIED_BY, data.get(i).getModifiedBy());
                cv.put(C_MODIFIED_DATE, data.get(i).getModifiedDate());

                cv.put(cisSync, data.get(i).isSync);
                cv.put(cisFlag1, data.get(i).isFlag1);
                cv.put(cisFlag2, data.get(i).isFlag2);
                cv.put(cisFlag3, data.get(i).isFlag3);
                cv.put(cisFlag4, data.get(i).isFlag4);
                rowId = db.insert(TABLE_DOCUMENT_UPLOAD, null, cv);
                if (rowId < 0) {
                    throw new Exception("insert TABLE_DOCUMENT_UPLOAD Table");
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
            Log.e("newDBHelper", "Error in inserting in Document Table");
        } finally {
            db.close();
        }
        return rowId;

    }


    private Cursor getTableValue(String TableName, String[] columns, String Where) {
        try {
            db = this.getReadableDatabase();
            return db.query(TableName, columns, Where, null, null, null,
                    null, null);
        } catch (Exception e) {

            Log.e("mydbHelper", e.toString() + "getTableValue()");
            throw e;
        }
    }


    /*** Added By - Priyanka Warekar - 28-10-2015 - Start ****/

    /* CIFEnrollment database query */
    public long insertDashBoardDetail(M_UserInformation Userdeatils)
            throws ValueNotInsertedException {

        try {
            db = this.getWritableDatabase();

            ContentValues cv = new ContentValues();
            cv.put(C_CIF_QUOTATION_NO, Userdeatils.getStr_quotation());
            cv.put(C_USER_ID, "demouser");
            cv.put(C_CIF_PF_NUMBER, Userdeatils.getStr_pf_number());
            cv.put(C_CIF_CANDIDATE_NAME, Userdeatils.getStr_candidate_name());
            cv.put(C_DD_PENDING_STATUS, Userdeatils.getStr_status());
            cv.put(C_CIF_EMAIL_ID, Userdeatils.getStr_email_id());
            cv.put(C_CREATED_DATE, Userdeatils.getStr_created_date());
            cv.put(C_CIF_MOBILE_NO, Userdeatils.getStr_mobile_no());
            cv.put(C_CREATED_BY, "created by");
            cv.put(C_MODIFIED_BY, "modified by");
            cv.put(C_MODIFIED_DATE, "modified date");
            cv.put(cisSync, "0");

            if (Userdeatils.getIsFlag1() != null) {
                cv.put(cisFlag1, Userdeatils.getIsFlag1());
            } else {
                cv.put(cisFlag1, "0");
            }

            cv.put(cisFlag2, "0");

            //added by rajan 24-10-2017
            cv.put(C_CIF_AADHAR_CARD_NO, Userdeatils.getStr_aadhar_card_no());
            cv.put(C_CIF_CONTACT_PERSON_EMAIL_ID, Userdeatils.getStr_contact_person_email_id());

            return db.insert(TABLE_DASHBOARDDETAIL, null, cv);
        } catch (Exception e) {
            Log.e("Dbconnection", e.toString()
                    + "Error in inserting Basic Details");

            throw new ValueNotInsertedException(e.toString());

        } finally {
            db.close();
        }

    }

    public long insertExamDetail(M_ExamDetails examdeatils)
            throws ValueNotInsertedException {

        try {
            db = this.getWritableDatabase();

            ContentValues cv = new ContentValues();
            cv.put(C_CIF_QUOTATION_NO, examdeatils.getStr_quotation_no());
            // cv.put(C_USER_ID, "demouser");
            cv.put(C_CIF_EXAM_CENTER, examdeatils.getStr_exam_center());

            return db.insert(TABLE_EXAM_CENTER_LOCATION, null, cv);
        } catch (Exception e) {
            Log.e("Dbconnection", e.toString()
                    + "Error in inserting Basic Details");

            throw new ValueNotInsertedException(e.toString());

        } finally {
            db.close();
        }

    }

    public long insertCIFDetail_New(M_MainActivity_Data data,
                                    String quotation_number) {
        db = this.getWritableDatabase();
        long id = 0;
        try {
            ContentValues cv = new ContentValues();
            cv.put(C_CIF_QUOTATION_NO, data.getStr_quotation());
            cv.put(C_USER_ID, "demouser");
            cv.put(C_CIF_PF_NUMBER, data.getStr_pf_number());
            cv.put(C_CIF_PAN, data.getInput());
            cv.put(cisFlag1, data.getIsFlag1());

            id = db.insert(TABLE_CIF_ENROLLMENT, null, cv);

        } catch (Exception e) {
            Log.e("Dbconnection", "Error in inserting in Cif Enrollment ");
        } finally {
            db.close();
        }

        return id;
    }

    public List<M_MainActivity_Data> getCIFDetail_Pf_Number(String pf_number) {
        List<M_MainActivity_Data> data_List = new ArrayList<>();
        Cursor cursor = null;
        try {

            db = getReadableDatabase();

            String where = C_CIF_PF_NUMBER + " = " + "'" + pf_number + "'";
            String[] columns = null;

            cursor = db.query(TABLE_CIF_ENROLLMENT, columns, where, null, null,
                    null, null);

            long count = cursor.getCount();
            if (count > 0) {
                cursor.moveToFirst();

                do {
                    String str_quotation = cursor.getString(cursor
                            .getColumnIndexOrThrow(C_CIF_QUOTATION_NO));

                    data_List.add(new M_MainActivity_Data(str_quotation));

                } while (cursor.moveToNext());
                cursor.close();
            }
        } catch (Exception e) {
            // TODO: handle exception
            Log.e("Dbconnection", e.toString() + "Error in getting BI Detail");
        } finally {
            if (cursor != null) {
                db.close();
            }
        }

        return data_List;
    }

    private Cursor getTableValueForDashboard(String TableName, String[] columns,
                                             String Where) {
        try {
            db = this.getReadableDatabase();
            return db.query(TableName, columns, Where, null, null, null,
                    "dd_master_key DESC", null);
        } catch (Exception e) {

            Log.e("dbconnection", e.toString() + "getTableValue()");
            throw e;
        }
    }

    public List<M_UserInformation> getDashboardDetails(String agentId) {

        Cursor c;
        db = getReadableDatabase();
        List<M_UserInformation> cust_details = new ArrayList<>();
        String Where = cisFlag2 + " = " + "'" + 0 + "'" + " AND "
                + C_USER_ID + " = " + "'" + agentId + "'";

        c = getTableValueForDashboard(TABLE_DASHBOARDDETAIL, null,
                Where);
        if (c != null) {
            int count = c.getCount();
            try {

                String str_candidate_name;
                String str_quotation;
                String str_created_date;
                String str_mobile_no;

                if (count > 0) {
                    c.moveToFirst();
                    do {

                        str_candidate_name = c.getString(c
                                .getColumnIndex(C_CIF_CANDIDATE_NAME));
                        str_quotation = c.getString(c
                                .getColumnIndex(C_CIF_QUOTATION_NO));

                        str_created_date = c.getString(c
                                .getColumnIndex(C_CREATED_DATE));

                        str_mobile_no = c.getString(c
                                .getColumnIndex(C_CIF_MOBILE_NO));

                        String str_pf_number = c.getString(c
                                .getColumnIndex(C_CIF_PF_NUMBER));

                        String str_email_id = c.getString(c
                                .getColumnIndex(C_CIF_EMAIL_ID));

                        String isFlag1 = c.getString(c
                                .getColumnIndex(cisFlag1));

                        String status = c.getString(c
                                .getColumnIndex(C_DD_PENDING_STATUS));

                        String aadhar_no = c.getString(c
                                .getColumnIndex(C_CIF_AADHAR_CARD_NO));

                        String contact_person_email_id = c.getString(c
                                .getColumnIndex(C_CIF_CONTACT_PERSON_EMAIL_ID));

                        boolean hm_isFlag1;
                        hm_isFlag1 = isFlag1.equalsIgnoreCase("1");

                        M_UserInformation c_deatils = new M_UserInformation(
                                str_quotation, str_pf_number, str_candidate_name,
                                status, str_mobile_no, str_email_id,
                                str_created_date, hm_isFlag1);

                        c_deatils.setStr_aadhar_card_no(aadhar_no);
                        c_deatils.setStr_contact_person_email_id(contact_person_email_id);

                        cust_details.add(c_deatils);

                    } while (c.moveToNext());
                }
            } catch (Exception e) {
                // TODO: handle exception
                Log.e("DbConnection", e.toString()
                        + "Error in getting DashBoardDetail");
            } finally {
                db.close();
            }
        }

        return cust_details;

    }

    public long DeleteProposalFromDashboard(String quotation)
            throws ValueNotInsertedException {
        long rowid = 0;
        try {
            db = this.getWritableDatabase();

            String Where = C_CIF_QUOTATION_NO + " = " + "'" + quotation + "'";

            ContentValues cv = new ContentValues();
            cv.put(cisFlag1, 1);

            rowid = db.update(TABLE_DASHBOARDDETAIL, cv, Where, null);

        } catch (Exception e) {
            Log.e("DbConnection", e.toString()
                    + "Error in inserting Dashboard Details");

            throw new ValueNotInsertedException(e.toString());

        } finally {
            db.close();
        }
        return rowid;
    }

    public List<M_UserInformation> getDashboardDetailsByQuotNo(String like,
                                                               String AgentId) {

        Cursor c;
        db = getReadableDatabase();
        List<M_UserInformation> cust_details = new ArrayList<>();

        String where = " ( " + C_USER_ID + " = " + "'" + AgentId + "'" + " ) "
                + " AND " +

                " ( " + cisFlag2 + " = " + "'" + 0 + "'" + " ) "

                + " AND " + " ( " + C_CIF_PF_NUMBER + " like '%" + like + "%'"
                + " OR " + C_CIF_QUOTATION_NO + " like '%" + like + "%'"
                + " OR " + C_CIF_CANDIDATE_NAME + " like '%" + like + "%'"

                + " OR " + C_CIF_EMAIL_ID + " like '%" + like + "%'"

                + " OR " + C_CREATED_DATE + " like '%" + like + "%'" + " OR "
                + C_CIF_MOBILE_NO + " like '%" + like + "%'" + " ) ";

        //
        c = getTableValue(TABLE_DASHBOARDDETAIL, null, where);
        int count = c.getCount();

        try {

            String str_pf_number;
            String str_candidate;
            String str_created_date;
            String str_mobile;

            if (count > 0) {
                c.moveToFirst();
                do {

                    String str_quotation_no = c.getString(c
                            .getColumnIndex(C_CIF_QUOTATION_NO));
                    str_pf_number = c.getString(c
                            .getColumnIndex(C_CIF_PF_NUMBER));
                    str_candidate = c.getString(c
                            .getColumnIndex(C_CIF_CANDIDATE_NAME));

                    String str_email_id = c.getString(c
                            .getColumnIndex(C_CIF_EMAIL_ID));

                    str_created_date = c.getString(c
                            .getColumnIndex(C_CREATED_DATE));
                    str_mobile = c.getString(c
                            .getColumnIndex(C_CIF_MOBILE_NO));

                    M_UserInformation c_deatils = new M_UserInformation(
                            str_quotation_no, str_pf_number, str_candidate,
                            str_mobile, str_email_id, str_created_date);
                    cust_details.add(c_deatils);

                } while (c.moveToNext());
            }
        } catch (Exception e) {
            // TODO: handle exception
            Log.e("DbConnection", e.toString()
                    + "Error in getting DashBoardDetail");
        } finally {
            db.close();
        }

        return cust_details;
    }

    public List<M_MainActivity_Data> getCIFDetail_New(String quotation_no) {
        List<M_MainActivity_Data> data_List = new ArrayList<>();
        Cursor cursor = null;
        try {

            db = getReadableDatabase();

            String where = C_CIF_QUOTATION_NO + " = " + "'" + quotation_no
                    + "'";
            String[] columns = null;

            cursor = db.query(TABLE_CIF_ENROLLMENT, columns, where, null, null,
                    null, null);

            long count = cursor.getCount();
            if (count > 0) {
                cursor.moveToFirst();

                do {
                    String str_quotation = cursor.getString(cursor
                            .getColumnIndexOrThrow(C_CIF_QUOTATION_NO));
                    String str_pf_number = cursor.getString(cursor
                            .getColumnIndexOrThrow(C_CIF_PF_NUMBER));
                    String str_input = cursor.getString(cursor
                            .getColumnIndexOrThrow(C_CIF_PAN));
                    String str_isflag1 = cursor.getString(cursor
                            .getColumnIndexOrThrow(cisFlag1));

                    boolean hm_isFlag1;
                    hm_isFlag1 = str_isflag1 != null
                            && str_isflag1.equalsIgnoreCase("1");

                    data_List.add(new M_MainActivity_Data(str_quotation,
                            str_pf_number, str_input, hm_isFlag1));

                } while (cursor.moveToNext());
                cursor.close();
            }
        } catch (Exception e) {
            // TODO: handle exception
            Log.e("Dbconnection", e.toString() + "Error in getting BI Detail");
        } finally {
            if (cursor != null) {
                db.close();
            }
        }

        return data_List;
    }

    public List<M_ExamDetails> getExamDetails(String quotation_No) {
        List<M_ExamDetails> data_List = new ArrayList<>();
        Cursor cursor = null;
        try {

            db = getReadableDatabase();

            String where = C_CIF_QUOTATION_NO + " = " + "'" + quotation_No
                    + "'";
            String[] columns = null;

            cursor = db.query(TABLE_EXAM_CENTER_LOCATION, columns, where, null,
                    null, null, null);

            long count = cursor.getCount();
            if (count > 0) {
                cursor.moveToFirst();

                do {
                    String str_quotation_no = cursor.getString(cursor
                            .getColumnIndexOrThrow(C_CIF_QUOTATION_NO));
                    String str_exam_center = cursor.getString(cursor
                            .getColumnIndexOrThrow(C_CIF_EXAM_CENTER));

                    data_List.add(new M_ExamDetails(str_quotation_no,
                            str_exam_center));

                } while (cursor.moveToNext());
                cursor.close();
            }
        } catch (Exception e) {
            // TODO: handle exception
            Log.e("Dbconnection", e.toString() + "Error in getting BI Detail");
        } finally {
            if (cursor != null) {
                db.close();
            }
        }

        return data_List;
    }

    public long insertSyncStatus(M_Sync_Status sync_Status)
            throws ValueNotInsertedException {

        try {
            db = this.getWritableDatabase();

            ContentValues cv = new ContentValues();
            cv.put(C_CIF_QUOTATION_NO, sync_Status.getQuotation_no());
            cv.put(C_SS_SYNC_STATUS, sync_Status.getSyncStatus());

            return db.insert(TABLE_SYNC_STATUS, null, cv);
        } catch (Exception e) {
            Log.e("Dbconnection", e.toString()
                    + "Error in inserting sync Status Table");

            throw new ValueNotInsertedException(e.toString());

        } finally {
            db.close();
        }
    }

    public long DeleteProposalFromDashboard_New(String pf_no)
            throws ValueNotInsertedException {
        long rowid = 0;
        try {
            db = this.getWritableDatabase();

            String Where = C_CIF_PF_NUMBER + " = " + "'" + pf_no + "'";

//			ContentValues cv = new ContentValues();
//			cv.put(cisFlag1, 1);

            //	rowid = db.update(TABLE_DASHBOARDDETAIL, cv, Where, null);
            rowid = db.delete(TABLE_DASHBOARDDETAIL, Where, null);
        } catch (Exception e) {
            Log.e("DbConnection", e.toString()
                    + "Error in inserting Dashboard Details");

            throw new ValueNotInsertedException(e.toString());

        } finally {
            db.close();
        }
        return rowid;
    }

    public long insertDashBoardDetail_Status_New(String Quatno, String str_status) {

        long count = 0;
        db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        String Where = C_CIF_QUOTATION_NO + " = " + "'" + Quatno + "'";

        try {
            cv.put(C_SS_SYNC_STATUS, str_status);
            count = db.update(TABLE_SYNC_STATUS, cv, Where, null);

        } catch (Exception e) {
            // TODO: handle exception
            Log.e("Dbconnection", "Error in inserting Sync table");
        } finally {
            db.close();
        }
        return count;
    }

    public long insertDashBoardDetail_Status(String Quatno, String str_status) {
        long count = 0;
        db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        String Where = C_CIF_QUOTATION_NO + " = " + "'" + Quatno + "'";

        try {
            cv.put(C_DD_PENDING_STATUS, str_status);
            count = db.update(TABLE_DASHBOARDDETAIL, cv, Where, null);

        } catch (Exception e) {
            // TODO: handle exception
            Log.e("Dbconnection", "Error in inserting dashboard table");
        } finally {
            db.close();
        }
        return count;
    }

    public List<M_Sync_Status> getSyncStatus(String quotation_no) {

        Cursor c;
        db = getReadableDatabase();

        String where = C_CIF_QUOTATION_NO + " = " + "'" + quotation_no + "'";

        c = getTableValue(TABLE_SYNC_STATUS, null, where);
        List<M_Sync_Status> lst_sync_status = new ArrayList<>();

        int count = c.getCount();
        try {

            String sync_status;

            if (count > 0) {
                c.moveToFirst();
                do {

                    sync_status = c.getString(c
                            .getColumnIndex(C_SS_SYNC_STATUS));

                    M_Sync_Status c_deatils = new M_Sync_Status(sync_status);
                    lst_sync_status.add(c_deatils);

                } while (c.moveToNext());
            }
        } catch (Exception e) {
            // TODO: handle exception
            Log.e("Dbconnection", e.toString() + "Error in getting Sync Status");
        } finally {
            db.close();
        }

        return lst_sync_status;

    }

    public long insertRinRakshaDetails(String QuatationNumber, String inputVal, String outputVal, String agentcode,
                                       String userType, String strURN, String strBorrowerURN1, String strBorrowerURN2,
                                       String strBorrowerURN3) {

        db = getWritableDatabase();
        long insrt_id = 0;
        try {

            ContentValues cv = new ContentValues();
            cv.put(RIN_RAKSHA_BI_QUOTATION_NO, QuatationNumber);
            cv.put(RIN_RAKSHA_BI_INPUT, inputVal);
            cv.put(RIN_RAKSHA_BI_OUTPUT, outputVal);
            cv.put(RIN_RAKSHA_BI_CODE, agentcode);
            cv.put(RIN_RAKSHA_BI_TYPE, userType);
            cv.put(RIN_RAKSHA_BI_URN, strURN);
            cv.put(RIN_RAKSHA_BI_BORROWER1_URN, strBorrowerURN1);
            cv.put(RIN_RAKSHA_BI_BORROWER2_URN, strBorrowerURN2);
            cv.put(RIN_RAKSHA_BI_BORROWER3_URN, strBorrowerURN3);
            cv.put(RIN_RAKSHA_BI_DEL_FLAG, 0);
            insrt_id = db.insertWithOnConflict(TABLE_RIN_RAKSHA_BI, RIN_RAKSHA_BI_QUOTATION_NO, cv, SQLiteDatabase.CONFLICT_REPLACE);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

        return insrt_id;
    }

    public ArrayList<RinRakshaBean> getRinRakshaDashboardDetails() {
        db = getReadableDatabase();
        String selectQuery = "SELECT " + RIN_RAKSHA_BI_QUOTATION_NO + "," + RIN_RAKSHA_BI_BORROWER1_URN + ","
                + RIN_RAKSHA_BI_BORROWER2_URN + ", " + RIN_RAKSHA_BI_BORROWER3_URN + " FROM "
                + TABLE_RIN_RAKSHA_BI + " WHERE " + RIN_RAKSHA_BI_DEL_FLAG + " = 0";

        ArrayList<RinRakshaBean> lstRakshaBeans = new ArrayList<>();
        Cursor cur = db.rawQuery(selectQuery, null);
        if (cur.getCount() != 0) {
            while (cur.moveToNext()) {
                String quotNo = cur.getString(cur.getColumnIndex(RIN_RAKSHA_BI_QUOTATION_NO));
                String urn1 = cur.getString(cur.getColumnIndex(RIN_RAKSHA_BI_BORROWER1_URN));
                String urn2 = cur.getString(cur.getColumnIndex(RIN_RAKSHA_BI_BORROWER2_URN));
                String urn3 = cur.getString(cur.getColumnIndex(RIN_RAKSHA_BI_BORROWER3_URN));

                RinRakshaBean bean = new RinRakshaBean();
                bean.setStrQuotationNo(quotNo);

                bean.setStrURNBorrower1(urn1);

                if (urn2.equals("")) {
                    bean.setStrURNBorrower2("");
                } else {
                    bean.setStrURNBorrower2(urn2);
                }

                if (urn3.equals("")) {
                    bean.setStrURNBorrower3("");
                } else {
                    bean.setStrURNBorrower3(urn3);
                }

                lstRakshaBeans.add(bean);
            }
        }
        cur.close();

        return lstRakshaBeans;
    }

    public long insertGroupFeedbackDetails(ContentValues cv) {

        db = getWritableDatabase();
        long insrt_id = 0;
        try {
            insrt_id = db.insert(TABLE_GROUP_FEEDBACK, null, cv);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

        return insrt_id;
    }

    public int updateGroupFeedbackDetails(ContentValues cv, String[] whereArgs) {
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            return db.update(TABLE_GROUP_FEEDBACK, cv,
                    GROUP_FEEDBACK_ID + " =?", whereArgs); // -2 EXCEPTION
        } catch (Exception e) {
            e.printStackTrace();
            return -2;
        } finally {
            if (db.isOpen()) {
                db.close();
            }
        }
    }

    public ArrayList<String> getLinkedEkycPolicies(int ekyc_status) {

        ArrayList<String> lstPolicy = new ArrayList<>();
        try {
            db = getReadableDatabase();

            String selectQuery = "SELECT " + EKYC_PS_CLAIMS_PROPOSAL_NUMBER + " FROM " + TABLE_EKYC_PS_CLAIMS + " WHERE "
                    + EKYC_PS_CLAIMS_IS_DELETE + " = 0 AND "
                    + EKYC_PS_CLAIMS_EKYC_STATUS + " ='" + ekyc_status + "'";


            Cursor cur = db.rawQuery(selectQuery, null);
            if (cur.getCount() > 0) {
                while (cur.moveToNext()) {

                    if (!lstPolicy.contains(cur.getString(0))) {
                        lstPolicy.add(cur.getString(0));
                    }
                }
            }
            cur.close();
        } catch (Exception e) {
            e.printStackTrace();

        }

        return lstPolicy;
    }

	/*public ArrayList<EkycDashboardActivity.EkycDetails> get_eKyc_dashboard_list(String strType, int ekyc_status){
		db = getReadableDatabase();
		String selectQuery = "SELECT " + EKYC_PS_CLAIMS_PROPOSAL_NUMBER + "," + EKYC_PS_CLAIMS_DATE_TIME
				+ "," + EKYC_PS_CLAIMS_USER_TYPE + " FROM "
				+ TABLE_EKYC_PS_CLAIMS + " WHERE " + EKYC_PS_CLAIMS_IS_DELETE + " = 0 AND "
				+ EKYC_PS_CLAIMS_USER_TYPE + " = '" + strType + "' AND "
				+ EKYC_PS_CLAIMS_EKYC_STATUS + " ='" + ekyc_status+"'";

		ArrayList<EkycDashboardActivity.EkycDetails> lstEkycDetails = new ArrayList<EkycDashboardActivity.EkycDetails>();
		ArrayList<String> lstPolicy = new ArrayList<String>();
		Cursor cur = db.rawQuery(selectQuery, null);

		if (cur.getCount() > 0){
			while (cur.moveToNext()){

				if (!lstPolicy.contains(cur.getString(0).toString())){
					lstEkycDetails.add(new EkycDashboardActivity().new EkycDetails(cur.getString(0).toString(),
							cur.getString(1).toString(), cur.getString(2).toString()));
				}
			}
		}
		cur.close();

		//list sort by date
		Collections.sort(lstEkycDetails, new Comparator<EkycDashboardActivity.EkycDetails>() {
			@Override
			public int compare(EkycDashboardActivity.EkycDetails o1, EkycDashboardActivity.EkycDetails o2) {

				Date d1 = new Date(Long.valueOf(o1.getStr_link_date().toString()));
				Date d2 = new Date(Long.valueOf(o2.getStr_link_date().toString()));

				return d2.compareTo(d1);
			}
		});

		return lstEkycDetails;
	}*/

    public Cursor get_eKYC_Details(String strProposalNo) {
        db = getReadableDatabase();
        String selectQuery = "SELECT * FROM "
                + TABLE_EKYC_PS_CLAIMS + " WHERE "
                + EKYC_PS_CLAIMS_PROPOSAL_NUMBER + "='" + strProposalNo + "' AND "
                /*+ EKYC_PS_CLAIMS_AADHAAR_NUMBER +"='"+strAadhar+"' AND "*/
                + EKYC_PS_CLAIMS_IS_DELETE + " = 0 ";

        return db.rawQuery(selectQuery, null);
    }

    public long insert_eKYC_PS_Claims_Details(ContentValues cv) {

        db = getWritableDatabase();
        long insrt_id = 0;
        try {
            insrt_id = db.insert(TABLE_EKYC_PS_CLAIMS, null, cv);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return insrt_id;
    }

    public int update_eKYC_PS_Claims_Details(ContentValues cv, String[] whereArgs) {
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            /*return db.update(TABLE_EKYC_PS_CLAIMS, cv,
					EKYC_PS_CLAIMS_PROPOSAL_NUMBER + " =? and "
							+ EKYC_PS_CLAIMS_AADHAAR_NUMBER + " =?", whereArgs); // -2 EXCEPTION*/

            //eKYC 2.5 chnages aadhaar card No(12-digit) OR Virtual ID(16-digit)
            return db.update(TABLE_EKYC_PS_CLAIMS, cv,
                    EKYC_PS_CLAIMS_PROPOSAL_NUMBER + " =?", whereArgs); // -2 EXCEPTION
        } catch (Exception e) {
            e.printStackTrace();
            return -2;
        } finally {
            if (db.isOpen()) {
                db.close();
            }
        }
    }

    public int insert_POSP_RA(ContentValues cv) {

        db = getWritableDatabase();
        long insrt_id = 0;
        try {
            insrt_id = db.insert(TABLE_POSP_RA, null, cv);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            return -2;
        } finally {
            if (db.isOpen()) {
                db.close();
            }
        }

        return (int) insrt_id;
    }

    public ArrayList<String> get_POS_RA_ID(String str_pan_no) {

        db = getReadableDatabase();
        String selectQuery = "SELECT " + POSP_RA_ID + ", " + POSP_RA_IN_APP_STATUS + ", "
                + POSP_RA_EXAM_TRAINING_DETAILS + " FROM " + TABLE_POSP_RA
                + " WHERE " + POSP_RA_PAN_NO + "='" + str_pan_no + "'";

        ArrayList<String> lstRslt = new ArrayList<>();

        try {
            Cursor cur = db.rawQuery(selectQuery, null);

            if (cur.moveToLast()) {
                lstRslt.add(cur.getString(0));

                String str_status = cur.getString(1);
                str_status = str_status == null ? "0" : str_status;
                if (!str_status.equals("")) {
                    lstRslt.add(str_status);
                } else {
                    lstRslt.add("0");
                }

                String str_training_end_date = cur.getString(2);
                str_training_end_date = str_training_end_date == null ? "0" : str_training_end_date;
                if (!str_training_end_date.equals("")) {
                    lstRslt.add(str_training_end_date);
                } else {
                    lstRslt.add("");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db.isOpen()) {
                db.close();
            }
        }

        return lstRslt;
    }

    public int update_POSP_RA_details(ContentValues cv, String whereCol, String[] whereArgs) {
        db = this.getWritableDatabase();

        try {
            return db.update(TABLE_POSP_RA, cv, whereCol, whereArgs); // -2 EXCEPTION
        } catch (Exception e) {
            e.printStackTrace();
            return -2;
        } finally {
            if (db.isOpen()) {
                db.close();
            }
        }
    }

    public void delete_POSP_RA_Rejection_row(String strWhereQuery) {

        db = getWritableDatabase();
        try {

            db.delete(TABLE_POSP_RA_REJECTION, strWhereQuery, null);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db.isOpen())
                db.close();
        }
    }

    public void delete_POSP_RA_Rejection_all_row() {

        db = getWritableDatabase();
        try {

            db.execSQL("delete from " + TABLE_POSP_RA_REJECTION);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db.isOpen())
                db.close();
        }
    }

    public int insert_POSP_RA_Rejection(ContentValues cv) {

        db = getWritableDatabase();
        long insrt_id = 0;
        try {
            insrt_id = db.insert(TABLE_POSP_RA_REJECTION, null, cv);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            return -2;
        } finally {
            if (db.isOpen()) {
                db.close();
            }
        }

        return (int) insrt_id;
    }

    public ArrayList<POJO_POSP_RA_Rejection> getPOSP_RA_Rejection_By_PAN(String str_pan_no, String enrollment_type) {
        db = getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_POSP_RA_REJECTION
                + " WHERE " + POSP_RA_REQ_PAN + "='" + str_pan_no + "' AND "
                + POSP_RA_REQ_RAISED_ENROLLMENT_TYPE + " = '" + enrollment_type + "'";

        ArrayList<POJO_POSP_RA_Rejection> lstRslt = new ArrayList<>();

        try {
            Cursor cur = db.rawQuery(selectQuery, null);

            cur.moveToFirst();
            do {
                int primaryID = cur.getInt(0);
                int posp_table_id = cur.getInt(1);

                String strPAN = cur.getString(2);
                strPAN = strPAN == null ? "" : strPAN;

                String strIACode = cur.getString(3);
                strIACode = strIACode == null ? "" : strIACode;

                String strDocStatus = cur.getString(4);
                strDocStatus = strDocStatus == null ? "" : strDocStatus;

                String strReqStatus = cur.getString(5);
                strReqStatus = strReqStatus == null ? "" : strReqStatus;

                String strReqRaised = cur.getString(6);
                strReqRaised = strReqRaised == null ? "" : strReqRaised;

                String strDocName = cur.getString(7);
                strDocName = strDocName == null ? "" : strDocName;

                String strRemarks = cur.getString(8);
                strRemarks = strRemarks == null ? "" : strRemarks;

                String strReqRaisedDocOptVal = cur.getString(9);
                strReqRaisedDocOptVal = strReqRaisedDocOptVal == null ? "" : strReqRaisedDocOptVal;

                String strReqRaisedUMCode = cur.getString(10);
                strReqRaisedUMCode = strReqRaisedUMCode == null ? "" : strReqRaisedUMCode;

                lstRslt.add(new POJO_POSP_RA_Rejection(primaryID, -1, strPAN, strIACode, strDocStatus,
                        strReqStatus, strReqRaised, strDocName, strRemarks, strReqRaisedDocOptVal,
                        strReqRaisedUMCode));
            } while (cur.moveToNext());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db.isOpen()) {
                db.close();
            }
        }

        return lstRslt;
    }

    public void delete_POSP_RA_row(String strWhereQuery) {

        db = getWritableDatabase();
        try {

            db.delete(TABLE_POSP_RA, strWhereQuery, null);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db.isOpen())
                db.close();
        }
    }

    public int insert_agent_on_boarding(ContentValues cv) {

        db = getWritableDatabase();
        long insrt_id = 0;
        try {
            insrt_id = db.insert(TABLE_AGENT_ON_BOARDING, null, cv);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            return -2;
        } finally {
            if (db.isOpen()) {
                db.close();
            }
        }

        return (int) insrt_id;
    }

    public ArrayList<String> getAgentOnBoardingID(String str_pan_no) {

        db = getReadableDatabase();
        String selectQuery = "SELECT " + AGENT_ON_BOARDING_ID + ", " + AGENT_ON_BOARDING_SYNCH_STATUS + ", "
                + AGENT_ON_BOARDING_EXAM_TRAINING_DETAILS + " FROM " + TABLE_AGENT_ON_BOARDING
                + " WHERE " + AGENT_ON_BOARDING_PAN_NO + "='" + str_pan_no + "'";

        ArrayList<String> lstRslt = new ArrayList<>();

        try {
            Cursor cur = db.rawQuery(selectQuery, null);

            if (cur.moveToLast()) {
                lstRslt.add(cur.getString(0));

                String str_status = cur.getString(1);
                str_status = str_status == null ? "0" : str_status;
                if (!str_status.equals("")) {
                    lstRslt.add(str_status);
                } else {
                    lstRslt.add("0");
                }

                String str_training_end_date = cur.getString(2);
                str_training_end_date = str_training_end_date == null ? "0" : str_training_end_date;
                if (!str_training_end_date.equals("")) {
                    lstRslt.add(str_training_end_date);
                } else {
                    lstRslt.add("");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db.isOpen()) {
                db.close();
            }
        }

        return lstRslt;
    }

    public int update_agent_on_boarding_details(ContentValues cv, String whereCol, String[] whereArgs) {
        db = this.getWritableDatabase();

        try {
            return db.update(TABLE_AGENT_ON_BOARDING, cv, whereCol, whereArgs); // -2 EXCEPTION
        } catch (Exception e) {
            e.printStackTrace();
            return -2;
        } finally {
            if (db.isOpen()) {
                db.close();
            }
        }
    }

    public void delete_agent_on_boarding_row(String strWhereQuery) {

        db = getWritableDatabase();
        try {

            db.delete(TABLE_AGENT_ON_BOARDING, strWhereQuery, null);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db.isOpen())
                db.close();
        }
    }

    public ArrayList<PojoAOB> get_agent_on_boarding_details_by_ID() {

        db = getWritableDatabase();

        //get only synched data
        String selectQuery = "SELECT * FROM " + TABLE_AGENT_ON_BOARDING;

        ArrayList<PojoAOB> lstResult = new ArrayList<>();

        try {
            Cursor cur = db.rawQuery(selectQuery, null);

            if (cur.getCount() > 0) {

                while (cur.moveToNext()) {

                    String str_status = cur.getString(19);
                    str_status = str_status == null ? "0" : str_status;

                    if (str_status.equals(""))
                        str_status = "0";

                    if (!str_status.equals("0")) {
                        lstResult.add(new PojoAOB(cur.getString(0), cur.getString(1), cur.getString(2), cur.getString(3), cur.getString(4),
                                cur.getString(5), cur.getString(6), cur.getString(7), cur.getString(8), cur.getString(9), cur.getString(10),
                                cur.getString(11), cur.getString(12), cur.getString(13), cur.getString(14), cur.getString(15),
                                cur.getString(16), cur.getString(17), cur.getString(18), cur.getString(19),
                                cur.getString(20)));
                    }
                }
                cur.close();
            }

            return lstResult;

        } catch (Exception e) {
            e.printStackTrace();
            return lstResult;
        } finally {
            if (db.isOpen()) {
                db.close();
            }
        }
    }


    public ArrayList<PojoAOB> get_agent_on_boarding_details_by_ID(int row_id) {

        db = getWritableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_AGENT_ON_BOARDING
                + " WHERE " + AGENT_ON_BOARDING_ID + "=" + row_id;

        ArrayList<PojoAOB> lstResult = new ArrayList<>();

        try {
            Cursor cur = db.rawQuery(selectQuery, null);

            if (cur.moveToLast()) {

                lstResult.add(new PojoAOB(cur.getString(0), cur.getString(1), cur.getString(2), cur.getString(3), cur.getString(4),
                        cur.getString(5), cur.getString(6), cur.getString(7), cur.getString(8), cur.getString(9), cur.getString(10),
                        cur.getString(11), cur.getString(12), cur.getString(13), cur.getString(14), cur.getString(15),
                        cur.getString(16), cur.getString(17), cur.getString(18), cur.getString(19),
                        cur.getString(20)));

                cur.close();
                return lstResult;
            } else {
                cur.close();
                return lstResult;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return lstResult;
        } finally {
            if (db.isOpen()) {
                db.close();
            }
        }
    }

    public ArrayList<Pojo_POSP_RA> get_posp_ra_details_by_ID(int row_id) {

        db = getWritableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_POSP_RA
                + " WHERE " + POSP_RA_ID + "=" + row_id;

        ArrayList<Pojo_POSP_RA> lstResult = new ArrayList<>();

        try {
            Cursor cur = db.rawQuery(selectQuery, null);

            if (cur.moveToLast()) {

                lstResult.add(new Pojo_POSP_RA(cur.getString(0), cur.getString(1),
                        cur.getString(2), cur.getString(3), cur.getString(4),
                        cur.getString(5), cur.getString(6), cur.getString(7),
                        cur.getString(8), cur.getString(9), cur.getString(10),
                        cur.getString(11), cur.getString(12), cur.getString(13),
                        cur.getString(14), cur.getString(15), cur.getString(16),
                        cur.getString(17), cur.getString(18), cur.getString(19),
                        cur.getString(20)));

                cur.close();
                return lstResult;
            } else {
                cur.close();
                return lstResult;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return lstResult;
        } finally {
            if (db.isOpen()) {
                db.close();
            }
        }
    }

    public ArrayList<TCC_ExamDetails_Activity.TCC_ExamDetails> getTCCAllDetails(String str_urn) {

        ArrayList<TCC_ExamDetails_Activity.TCC_ExamDetails> rsltList = new ArrayList<>();

        try {

            db = getReadableDatabase();
            String selectQuery = "SELECT * FROM " + TABLE_TCC_EXAM_DETAILS;

            if (!str_urn.equals(""))
                selectQuery += " WHERE " + TCC_EXAM_DETAILS_URN_NUMBER + " = '" + str_urn + "'";

            Cursor cur = db.rawQuery(selectQuery, null);
            if (cur.getCount() > 0) {

                while (cur.moveToNext()) {
                    String urn = cur.getString(1);
                    String start_date = cur.getString(2);
                    String end_date = cur.getString(3);
                    String no_hrs = cur.getString(4);
                    String location = cur.getString(5);
                    String centre = cur.getString(6);
                    String exam_date = cur.getString(7);
                    String sync_status = cur.getString(8);

                    rsltList.add(new TCC_ExamDetails_Activity().new TCC_ExamDetails(urn, location, centre, exam_date,
                            sync_status, no_hrs, start_date, end_date));
                }

                cur.close();
                return rsltList;
            } else {
                cur.close();
                return rsltList;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return rsltList;
        }
    }

    public ArrayList<PojoAOB> get_AOB_dashboard_details_by_ID() {

        db = getWritableDatabase();

        //get only synched data
        String selectQuery = "SELECT * FROM " + TABLE_AGENT_ON_BOARDING + " WHERE "
                + AGENT_ON_BOARDING_ENROLLMENT_TYPE + " = 'New'";

        ArrayList<PojoAOB> lstResult = new ArrayList<>();

        try {
            Cursor cur = db.rawQuery(selectQuery, null);

            if (cur.getCount() > 0) {

                while (cur.moveToNext()) {

                    String str_status = cur.getString(19);
                    str_status = str_status == null ? "0" : str_status;

                    if (str_status.equals(""))
                        str_status = "0";

                    if (!str_status.equals("0")) {
                        lstResult.add(new PojoAOB(cur.getString(0), cur.getString(1), cur.getString(2), cur.getString(3), cur.getString(4),
                                cur.getString(5), cur.getString(6), cur.getString(7), cur.getString(8), cur.getString(9), cur.getString(10),
                                cur.getString(11), cur.getString(12), cur.getString(13), cur.getString(14), cur.getString(15),
                                cur.getString(16), cur.getString(17), cur.getString(18), cur.getString(19),
                                cur.getString(20)));
                    }
                }
            }

            return lstResult;

        } catch (Exception e) {
            e.printStackTrace();
            return lstResult;
        } finally {
            if (db.isOpen()) {
                db.close();
            }
        }
    }

    public ArrayList<Pojo_POSP_RA> get_posp_ra_dashboard_details_by_ID() {

        db = getWritableDatabase();

        //get only synched data
        String selectQuery = "SELECT * FROM " + TABLE_POSP_RA;

        ArrayList<Pojo_POSP_RA> lstResult = new ArrayList<>();

        try {
            Cursor cur = db.rawQuery(selectQuery, null);

            if (cur.getCount() > 0) {

                while (cur.moveToNext()) {

                    String str_status = cur.getString(13);
                    str_status = str_status == null ? "0" : str_status;

                    if (str_status.equals(""))
                        str_status = "0";

                    if (!str_status.equals("0")) {
                        lstResult.add(new Pojo_POSP_RA(cur.getString(0), cur.getString(1),
                                cur.getString(2), cur.getString(3), cur.getString(4),
                                cur.getString(5), cur.getString(6), cur.getString(7),
                                cur.getString(8), cur.getString(9), cur.getString(10),
                                cur.getString(11), cur.getString(12), cur.getString(13),
                                cur.getString(14), cur.getString(15), cur.getString(16),
                                cur.getString(17), cur.getString(18), cur.getString(19),
                                cur.getString(20)));
                    }
                }
            }

            return lstResult;

        } catch (Exception e) {
            e.printStackTrace();
            return lstResult;
        } finally {
            if (db.isOpen()) {
                db.close();
            }
        }
    }

    public long insert_tcc_and_exam_details(ContentValues cv) {

        db = getWritableDatabase();
        long insrt_id = 0;
        try {
            insrt_id = db.insert(TABLE_TCC_EXAM_DETAILS, null, cv);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return insrt_id;
    }

    public int update_tcc_exam_details(ContentValues cv, String whereCol, String[] whereArgs) {
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            return db.update(TABLE_TCC_EXAM_DETAILS, cv,
                    whereCol, whereArgs); // -2 EXCEPTION
        } catch (Exception e) {
            e.printStackTrace();
            return -2;
        } finally {
            if (db.isOpen()) {
                db.close();
            }
        }
    }

    public long insertShortMHRProposal(String proposalNumber, String fromDate, String toDate) {
        long result = 0;
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put(SHORT_MHR_PROPOSAL_NUMBER, proposalNumber);
            cv.put(SHORT_MHR_FROM_DATE, fromDate);
            cv.put(SHORT_MHR_TO_DATE, toDate);
            result = db.insert(TABLE_SHORT_MHR_PROPOSAL_DETAILS, SHORT_MHR_FROM_DATE, cv);

            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public ArrayList<ParseXML.MHRReportValuesModel> checkShortMHRProposalNumberExists(ArrayList<ParseXML.
            MHRReportValuesModel> mhrReportValuesModelArrayList) {

        try {
            SQLiteDatabase db = this.getReadableDatabase();
            String Query = "Select * from " + TABLE_SHORT_MHR_PROPOSAL_DETAILS;
            Cursor cursor = db.rawQuery(Query, null);
            long count = cursor.getCount();
            System.out.println(count);
            if (count > 0) {
                cursor.moveToFirst();
                do {
                    String dbProposalNumber = cursor.getString(1);
                    System.out.println("dbProposalNumber:" + dbProposalNumber + " : Position" + cursor.getPosition());
                    for (ParseXML.MHRReportValuesModel obj : mhrReportValuesModelArrayList) {
                        if (obj.getPROPOSALNUMBER().equalsIgnoreCase(dbProposalNumber)) {
                            mhrReportValuesModelArrayList.remove(obj);
                            break;
                        }
                    }
                    System.out.println("mhrReportValuesModelArrayList = " + mhrReportValuesModelArrayList.size());
                } while (cursor.moveToNext());
            }

            cursor.close();
            db.close();
        } catch (Exception e) {

        }

        return mhrReportValuesModelArrayList;
    }

    public long insertCovidSelfDeclarationDetails(String IACode, String xml, String date, String time) {
        long result = 0;

        SQLiteDatabase db = this.getWritableDatabase();
        /*try {
            String selectQuery = "SELECT * FROM " + TABLE_COVID_SELF_DECLARATION
                    + " where " + COVID_SELF_DECLARATION_IA_CODE + "=" + "'" + IACode + "'";
            Cursor cur = db.rawQuery(selectQuery, null);

            if (cur.getCount() > 0) {

                while (cur.moveToNext()) {

                    String dateCur = cur.getString(2);
                    dateCur = dateCur == null ? "0" : dateCur;

                    if (!dateCur.equalsIgnoreCase(date)) {
                        try {

                            ContentValues cv = new ContentValues();
                            cv.put(COVID_SELF_DECLARATION_IA_CODE, IACode);
                            cv.put(COVID_SELF_DECLARATION_XML_STRING, xml);
                            cv.put(COVID_SELF_DECLARATION_DATE, date);
                            cv.put(COVID_SELF_DECLARATION_TIME, time);
                            result = db.insert(TABLE_COVID_SELF_DECLARATION, COVID_SELF_DECLARATION_XML_STRING, cv);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            *//*SQLiteDatabase db = this.getWritableDatabase();
                            ContentValues cv = new ContentValues();
                            cv.put(COVID_SELF_DECLARATION_IA_CODE, IACode);
                            cv.put(COVID_SELF_DECLARATION_XML_STRING, xml);
                            cv.put(COVID_SELF_DECLARATION_DATE, date);
                            cv.put(COVID_SELF_DECLARATION_TIME, time);
                            result = db.insert(TABLE_COVID_SELF_DECLARATION, null, cv);*//*
                            String strSQL = "UPDATE " + TABLE_COVID_SELF_DECLARATION + " SET "
                                    + COVID_SELF_DECLARATION_XML_STRING + " = '" + xml
                                    + "' WHERE " + COVID_SELF_DECLARATION_DATE + " = '" + date +"'";

                            db.execSQL(strSQL);
                            result = 1;
                            db.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }*/

        try {
            ContentValues cv = new ContentValues();
            cv.put(COVID_SELF_DECLARATION_IA_CODE, IACode);
            cv.put(COVID_SELF_DECLARATION_XML_STRING, xml);
            cv.put(COVID_SELF_DECLARATION_DATE, date);
            cv.put(COVID_SELF_DECLARATION_TIME, time);
            result = db.insert(TABLE_COVID_SELF_DECLARATION, COVID_SELF_DECLARATION_DATE, cv);
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public ArrayList<CovidSelfDeclarationDashboardActivity.DBCovidSelfDeclarationValuesModel> getCovidSelfDeclarationByIACode(String IACode) {

        db = getWritableDatabase();

        //get only synched data
        String selectQuery = "SELECT * FROM " + TABLE_COVID_SELF_DECLARATION
                + " where " + COVID_SELF_DECLARATION_IA_CODE + "=" + "'" + IACode + "'";

        ArrayList<CovidSelfDeclarationDashboardActivity.DBCovidSelfDeclarationValuesModel> lstResult = new ArrayList<>();

        try {
            Cursor cur = db.rawQuery(selectQuery, null);

            if (cur.getCount() > 0) {

                while (cur.moveToNext()) {

                    String date = cur.getString(2);
                    date = date == null ? "0" : date;

                    String xmlString = cur.getString(3);
                    xmlString = xmlString == null ? "0" : xmlString;

                    String time = cur.getString(4);
                    time = time == null ? "0" : time;

                    lstResult.add(new CovidSelfDeclarationDashboardActivity().new DBCovidSelfDeclarationValuesModel(IACode, date, xmlString, time));
                }
            }

            return lstResult;

        } catch (Exception e) {
            e.printStackTrace();
            return lstResult;
        } finally {
            if (db.isOpen()) {
                db.close();
            }
        }
    }
}


