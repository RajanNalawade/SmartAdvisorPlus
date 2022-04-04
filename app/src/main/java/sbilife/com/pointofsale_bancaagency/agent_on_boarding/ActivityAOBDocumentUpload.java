package sbilife.com.pointofsale_bancaagency.agent_on_boarding;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import sbilife.com.pointofsale_bancaagency.DatabaseHelper;
import sbilife.com.pointofsale_bancaagency.ParseXML;
import sbilife.com.pointofsale_bancaagency.R;
import sbilife.com.pointofsale_bancaagency.ServiceURL;
import sbilife.com.pointofsale_bancaagency.common.AsyncUploadFile_Common;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;
import sbilife.com.pointofsale_bancaagency.common.CompressImage;
import sbilife.com.pointofsale_bancaagency.common.FileLoader;
import sbilife.com.pointofsale_bancaagency.common.StorageUtils;
import sbilife.com.pointofsale_bancaagency.posp_ra.Activity_POSP_RA_Rejection_Remarks;
import sbilife.com.pointofsale_bancaagency.posp_ra.POJO_POSP_RA_Rejection;
import sbilife.com.pointofsale_bancaagency.utility.SelfAttestedDocumentActivity;

public class ActivityAOBDocumentUpload extends AppCompatActivity implements View.OnClickListener, AsyncUploadFile_Common.Interface_Upload_File_Common {

    private final String NAMESPACE = "http://tempuri.org/";

    private final String METHOD_NAME_UPLOAD_ALL_AOB_DOC = "UploadFile_AgentEnroll";

    //to handle rejection
    private final String METHOD_NAME_UPLOAD_ALL_AOB_DOC_REJECTION = "UploadFile_AgentEnroll_other";
    private final String METHOD_NAME_SAVE_AOB_DETAILS = "saveAgentOnboardingDetail";

    private final int /*REQUEST_OCR = 100,*/ REQUEST_CODE_CAPTURE_DOCUMENT = 200, REQUEST_CODE_BROWSE_DOCUMENT = 300;

    private final String BANK_PASSBOOK_DOC = "bank_passbook";
    private final String AGE_PROOF_DOC = "age_proof";
    private final String BASIC_QUALIFICATION_DOC = "basic_qualification";
    private final String HIGH_QUALIFICATION_DOC = "higher_qualification";
    private final String COMM_ADDRESS_DOC = "communication_address_proof";
    private final String PERMANENT_ADDRESS_DOC = "permanent_address_proof";
    private final String FORM_15G_15H_DOC = "form_15g_15h_proof";
    private final String OTHERS_DOC = "others";
    private final String FORM_1B_DOC = "form_1b";
    private final String FORM_1C_DOC = "form_1c";
    private final String APPLICANT_SIGN = "applicant_signature";
    private final String DECLARATION = "declaration";

    //required for IA upgrade
    private final String IA_UPGRADE_TCC_DOC = "ia_upgrade_tcc_doc";
    private final String IA_UPGRADE_SCORE_CARD_DOC = "ia_upgrade_score_card";
    private final String IA_UPGRADE_ULIP_CARD_DOC = "ia_upgrade_ulip_card";
    private final String IA_UPGRADE_MOBILE_DECLARATION = "ia_upgrade_mob_declare";
    //private final String IA_UPGRADE_NOMINATION_FORM = "ia_upgrade_nomination_form";
    private final String IA_UPGRADE_FORM_IA = "ia_upgrade_form_ia";
    //private final String IA_UPGRADE_OTHERS = "ia_upgrade_others";

    private Context mContext;
    private CommonMethods mCommonMethods;
    private DatabaseHelper db;
    private Button btn_aob_auth_pan_submit, btn_aob_auth_pan_back;
    private EditText edt_aob_auth_others;
    private ImageButton imgbtn_aob_auth_bank_pass_book_view, imgbtn_aob_auth_bank_pass_book_camera, imgbtn_aob_auth_bank_pass_book_browse, imgbtn_aob_auth_bank_pass_book_upload,
            imgbtn_aob_auth_age_proof_view, imgbtn_aob_auth_age_proof_camera, imgbtn_aob_auth_age_proof_browse, imgbtn_aob_auth_age_proof_upload,
            imgbtn_aob_auth_basic_qualification_view, imgbtn_aob_auth_basic_qualification_camera, imgbtn_aob_auth_basic_qualification_browse, imgbtn_aob_auth_basic_qualification_upload,
            imgbtn_aob_auth_high_qualification_view, imgbtn_aob_auth_high_qualification_camera, imgbtn_aob_auth_high_qualification_browse, imgbtn_aob_auth_high_qualification_upload,
            imgbtn_aob_auth_comm_add_view, imgbtn_aob_auth_comm_add_camera, imgbtn_aob_auth_comm_add_browse, imgbtn_aob_auth_comm_add_upload,
            imgbtn_aob_auth_permanent_add_view, imgbtn_aob_auth_permanent_add_camera, imgbtn_aob_auth_permanent_add_browse, imgbtn_aob_auth_permanent_add_upload,
            imgbtn_aob_auth_form15g_15h_view, imgbtn_aob_auth_form15g_15h_camera, imgbtn_aob_auth_form15g_15h_browse, imgbtn_aob_auth_form15g_15h_upload,
            imgbtn_aob_auth_others_view, imgbtn_aob_auth_others_camera, imgbtn_aob_auth_others_browse, imgbtn_aob_auth_others_upload,
            imgbtn_aob_auth_form1b_view, imgbtn_aob_auth_form1b_camera, imgbtn_aob_auth_form1b_browse, imgbtn_aob_auth_form1b_upload,
            imgbtn_aob_auth_form1c_view, imgbtn_aob_auth_form1c_camera, imgbtn_aob_auth_form1c_browse, imgbtn_aob_auth_form1c_upload,
            imgbtn_aob_applicant_sign_camera, imgbtn_aob_applicant_sign_browse, imgbtn_aob_applicant_sign_upload,
            imgbtn_aob_declaration_camera, imgbtn_aob_declaration_browse, imgbtn_aob_declaration_upload,
            imgbtn_ia_upgrade_tcc_view, imgbtn_ia_upgrade_tcc_camera, imgbtn_ia_upgrade_tcc_browse, imgbtn_ia_upgrade_tcc_upload,
            imgbtn_ia_upgrade_score_card_view, imgbtn_ia_upgrade_score_card_camera, imgbtn_ia_upgrade_score_card_browse, imgbtn_ia_upgrade_score_card_upload,
            imgbtn_ia_upgrade_ulip_view, imgbtn_ia_upgrade_ulip_camera, imgbtn_ia_upgrade_ulip_browse, imgbtn_ia_upgrade_ulip_upload,
            imgbtn_ia_upgrade_form_ia_view, imgbtn_ia_upgrade_form_ia_camera, imgbtn_ia_upgrade_form_ia_browse, imgbtn_ia_upgrade_form_ia_upload,
            imgbtn_ia_upgrade_mob_declaration_view, imgbtn_ia_upgrade_mob_declaration_camera, imgbtn_ia_upgrade_mob_declaration_browse, imgbtn_ia_upgrade_mob_declaration_upload;

    private String str_pan_no = "", str_doc = "", str_extension = "", str_bank_passbook_type = "", str_age_proof_type = "",
            str_basic_qualification_type = "", str_high_qualification_type = "", str_comm_address_type = "",
            str_permanent_address_type = "", str_form15g_15h_type = "", str_others_doc_type = "", str_form1B_type = "",
            str_form1C_type = "", str_applicant_sign_type = "", str_declaration_type = "", strCIFBDMUserId = "",
            strCIFBDMEmailId = "", strCIFBDMMObileNo = "", str_ia_upgrade_tcc_type = "", str_ia_upgrade_score_card_type = "",
            str_ia_upgrade_ulip_type = "", str_ia_upgrade_form_ia_type = "", str_ia_upgrade_mob_declaration_type = "",
            OCR_TYPE = "", str_ia_upgrade_pan_no = "";

    private boolean is_bank_passbook_uploaded = false, is_age_proof_uploaded = false,
            is_basic_qualification_uploaded = false, is_high_qualification_uploaded = false,
            is_comm_address_uploaded = false, is_permanent_address_uploaded = false, is_form15g_15h_uploaded = false,
            is_others_doc_uploaded = false, is_form1B_uploaded = false, is_form1C_uploaded = false,
            is_back_preesed = false, is_dashboard = false, is_applicant_sign_uploaded = false,
            is_declaration_uploaded = false, is_ia_upgrade = false, is_ia_upgrade_tcc_uploaded = false,
            is_ia_upgrade_score_card_uploaded = false, is_ia_upgrade_ulip_uploaded = false,
            is_ia_upgrade_form_ia_uploaded = false, is_ia_upgrade_mob_declaration_uploaded = false,
            is_rejection = false;

    private ProgressDialog mProgressDialog;
    private byte[] mAllBytes;

    private File mBankPassbookFile, mAgeProofFile, mBasicQualificationFile, mHighQualificationFile, mCommAddressFile,
            mPermanentAddFile, mForm15G_15HFile, mOthersFile, mForm1BFile, mForm1CFile, mApplicantSignFile, mDeclarationFile,
            mIAUpgradeTCC, mIAUpgradeScoreCard, mIAUpgradeUlip, mIAUpgradeFormIA, mIAUpgradeMobDeclaration, mCapturedImgFile;

    private AsyncUploadFile_Common mAsyncUploadFileCommon;

    private StringBuilder str_doc_upload, str_final_aob_info;

    private Calendar mCalendar;

    private AsyncAllAOB mAsyncAllAOB;

    private ParseXML mParseXML;

    private LinearLayout ll_aob_doc_upload_ia, ll_aob_doc_upload_ia_upgrade;

    private TextView txt_ia_upgrade_tcc_proof_remark, txt_ia_upgrade_score_card_proof_remark,
            txt_ia_upgrade_ulip_proof_remark, txt_ia_upgrade_form_ia_proof_remark,
            txt_ia_upgrade_mob_declaration_proof_remark, txt_aob_auth_others_remark;

    private StorageUtils mStorageUtils;
    private ActivityResultLauncher<Intent> mBrowseDocLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            try {
                if (result.getResultCode() == RESULT_OK) {
                    if (result != null && result.getData().getData() != null) {

                        Uri mSelectedUri = result.getData().getData();

                        String strMimeType = mStorageUtils.getMimeType(mContext, mSelectedUri);
                        Object[] mObject = mCommonMethods.getContentURIDetails(mContext, mSelectedUri);

                        str_extension = mObject[0].toString();
                        double kilobyte = (double) mObject[2];

                        if (!strMimeType.equals("application/octet-stream")
                                && !strMimeType.equals("application/vnd.android.package-archive")) {

                            //2 MB valiadation
                            if (kilobyte < mCommonMethods.FILE_UPLOAD_RESTRICT_SIZE) {
                                if (strMimeType.equals("application/pdf")) {
                                    String imageFileName = str_pan_no + "_" + str_doc + ".pdf";

                                    new FileLoader(mContext, StorageUtils.DIRECT_DIRECTORY,
                                            imageFileName,
                                            new FileLoader.FileLoaderResponce() {
                                                @Override
                                                public void fileLoadFinished(File fileOutput) {
                                                    if (fileOutput != null) {
                                                        if (str_doc.equals(BANK_PASSBOOK_DOC)) {
                                                            mBankPassbookFile = fileOutput;

                                                            imgbtn_aob_auth_bank_pass_book_camera.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_camera));
                                                            imgbtn_aob_auth_bank_pass_book_browse.setImageDrawable(getResources().getDrawable(R.drawable.checkedbrowse));

                                                            str_bank_passbook_type = "BROWSE";
                                                            enableUploadButton(BANK_PASSBOOK_DOC);
                                                        } else if (str_doc.equals(AGE_PROOF_DOC)) {

                                                            mAgeProofFile = fileOutput;
                                                            imgbtn_aob_auth_age_proof_camera.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_camera));
                                                            imgbtn_aob_auth_age_proof_browse.setImageDrawable(getResources().getDrawable(R.drawable.checkedbrowse));

                                                            str_age_proof_type = "BROWSE";
                                                            enableUploadButton(AGE_PROOF_DOC);
                                                        } else if (str_doc.equals(BASIC_QUALIFICATION_DOC)) {

                                                            mBasicQualificationFile = fileOutput;
                                                            imgbtn_aob_auth_basic_qualification_camera.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_camera));
                                                            imgbtn_aob_auth_basic_qualification_browse.setImageDrawable(getResources().getDrawable(R.drawable.checkedbrowse));

                                                            str_basic_qualification_type = "BROWSE";
                                                            enableUploadButton(BASIC_QUALIFICATION_DOC);
                                                        } else if (str_doc.equals(HIGH_QUALIFICATION_DOC)) {
                                                            mHighQualificationFile = fileOutput;
                                                            imgbtn_aob_auth_high_qualification_camera.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_camera));
                                                            imgbtn_aob_auth_high_qualification_browse.setImageDrawable(getResources().getDrawable(R.drawable.checkedbrowse));

                                                            str_high_qualification_type = "BROWSE";
                                                            enableUploadButton(HIGH_QUALIFICATION_DOC);
                                                        } else if (str_doc.equals(COMM_ADDRESS_DOC)) {

                                                            mCommAddressFile = fileOutput;

                                                            imgbtn_aob_auth_comm_add_camera.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_camera));
                                                            imgbtn_aob_auth_comm_add_browse.setImageDrawable(getResources().getDrawable(R.drawable.checkedbrowse));
                                                            str_comm_address_type = "BROWSE";
                                                            //remove common source file from directory
                                                            enableUploadButton(COMM_ADDRESS_DOC);
                                                        } else if (str_doc.equals(PERMANENT_ADDRESS_DOC)) {
                                                            mPermanentAddFile = fileOutput;

                                                            imgbtn_aob_auth_permanent_add_camera.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_camera));
                                                            imgbtn_aob_auth_permanent_add_browse.setImageDrawable(getResources().getDrawable(R.drawable.checkedbrowse));

                                                            str_permanent_address_type = "BROWSE";

                                                            //remove common source file from directory
                                                            enableUploadButton(PERMANENT_ADDRESS_DOC);
                                                        } else if (str_doc.equals(FORM_15G_15H_DOC)) {

                                                            mForm15G_15HFile = fileOutput;

                                                            imgbtn_aob_auth_form15g_15h_camera.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_camera));
                                                            imgbtn_aob_auth_form15g_15h_browse.setImageDrawable(getResources().getDrawable(R.drawable.checkedbrowse));

                                                            str_form15g_15h_type = "BROWSE";
                                                            enableUploadButton(FORM_15G_15H_DOC);
                                                        } else if (str_doc.equals(OTHERS_DOC)) {
                                                            mOthersFile = fileOutput;

                                                            imgbtn_aob_auth_others_camera.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_camera));
                                                            imgbtn_aob_auth_others_browse.setImageDrawable(getResources().getDrawable(R.drawable.checkedbrowse));

                                                            str_others_doc_type = "BROWSE";
                                                            enableUploadButton(OTHERS_DOC);
                                                        } else if (str_doc.equals(FORM_1B_DOC)) {

                                                            mForm1BFile = fileOutput;

                                                            imgbtn_aob_auth_form1b_camera.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_camera));
                                                            imgbtn_aob_auth_form1b_browse.setImageDrawable(getResources().getDrawable(R.drawable.checkedbrowse));

                                                            str_form1B_type = "BROWSE";
                                                            enableUploadButton(FORM_1B_DOC);


                                                        } else if (str_doc.equals(FORM_1C_DOC)) {
                                                            mForm1CFile = fileOutput;

                                                            imgbtn_aob_auth_form1c_camera.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_camera));
                                                            imgbtn_aob_auth_form1c_browse.setImageDrawable(getResources().getDrawable(R.drawable.checkedbrowse));

                                                            str_form1C_type = "BROWSE";
                                                            enableUploadButton(FORM_1C_DOC);

                                                        } else if (str_doc.equals(DECLARATION)) {
                                                            mDeclarationFile = fileOutput;

                                                            imgbtn_aob_declaration_camera.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_camera));
                                                            imgbtn_aob_declaration_browse.setImageDrawable(getResources().getDrawable(R.drawable.checkedbrowse));

                                                            str_declaration_type = "BROWSE";
                                                            enableUploadButton(DECLARATION);

                                                        } else if (str_doc.equals(IA_UPGRADE_TCC_DOC)) {

                                                            mIAUpgradeTCC = fileOutput;

                                                            imgbtn_ia_upgrade_tcc_camera.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_camera));
                                                            imgbtn_ia_upgrade_tcc_browse.setImageDrawable(getResources().getDrawable(R.drawable.checkedbrowse));

                                                            str_ia_upgrade_tcc_type = "BROWSE";
                                                            enableUploadButton(IA_UPGRADE_TCC_DOC);

                                                        } else if (str_doc.equals(IA_UPGRADE_SCORE_CARD_DOC)) {
                                                            mIAUpgradeScoreCard = fileOutput;

                                                            imgbtn_ia_upgrade_score_card_camera.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_camera));
                                                            imgbtn_ia_upgrade_score_card_browse.setImageDrawable(getResources().getDrawable(R.drawable.checkedbrowse));

                                                            str_ia_upgrade_score_card_type = "BROWSE";
                                                            enableUploadButton(IA_UPGRADE_SCORE_CARD_DOC);
                                                        } else if (str_doc.equals(IA_UPGRADE_ULIP_CARD_DOC)) {
                                                            mIAUpgradeUlip = fileOutput;

                                                            imgbtn_ia_upgrade_ulip_camera.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_camera));
                                                            imgbtn_ia_upgrade_ulip_browse.setImageDrawable(getResources().getDrawable(R.drawable.checkedbrowse));

                                                            str_ia_upgrade_ulip_type = "BROWSE";
                                                            enableUploadButton(IA_UPGRADE_ULIP_CARD_DOC);


                                                        } else if (str_doc.equals(IA_UPGRADE_FORM_IA)) {
                                                            mIAUpgradeFormIA = fileOutput;

                                                            imgbtn_ia_upgrade_form_ia_camera.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_camera));
                                                            imgbtn_ia_upgrade_form_ia_browse.setImageDrawable(getResources().getDrawable(R.drawable.checkedbrowse));

                                                            str_ia_upgrade_form_ia_type = "BROWSE";
                                                            enableUploadButton(IA_UPGRADE_FORM_IA);

                                                        } else if (str_doc.equals(IA_UPGRADE_MOBILE_DECLARATION)) {
                                                            mIAUpgradeMobDeclaration = fileOutput;

                                                            imgbtn_ia_upgrade_mob_declaration_camera.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_camera));
                                                            imgbtn_ia_upgrade_mob_declaration_browse.setImageDrawable(getResources().getDrawable(R.drawable.checkedbrowse));

                                                            str_ia_upgrade_mob_declaration_type = "BROWSE";
                                                            enableUploadButton(IA_UPGRADE_MOBILE_DECLARATION);
                                                        }
                                                    }
                                                }
                                            }).execute(mSelectedUri);
                                } else {
                                    mCommonMethods.showMessageDialog(mContext, "Please Select PDF format Document only!");
                                }
                            } else {
                                mCommonMethods.showMessageDialog(mContext, mCommonMethods.FILE_UPLOAD_RESTRICT_SIZE_MSG);
                            }

                        } else {
                            mCommonMethods.showToast(mContext, ".exe/.apk file format not acceptable");
                        }
                    } else {
                        mCommonMethods.showToast(mContext, "File Not Found!");
                    }
                } else {
                    mCommonMethods.showToast(mContext, "File browsing cancelled..");
                }
            } catch (Exception e) {
                e.printStackTrace();
                mCommonMethods.showMessageDialog(mContext, e.getMessage());
            }
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_aob_document_upload);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.cifenrollment_pf_activity_title);

        if (getIntent().hasExtra("is_dashboard"))
            is_dashboard = getIntent().getBooleanExtra("is_dashboard", false);

        if (getIntent().hasExtra("is_ia_upgrade"))
            is_ia_upgrade = getIntent().getBooleanExtra("is_ia_upgrade", false);

        if (getIntent().hasExtra("is_rejection"))
            is_rejection = getIntent().getBooleanExtra("is_rejection", false);

        if (getIntent().hasExtra("pan_no"))
            str_ia_upgrade_pan_no = getIntent().getStringExtra("pan_no");

        mContext = this;
        mCommonMethods = new CommonMethods();
        mStorageUtils = new StorageUtils();

        mCommonMethods.setApplicationToolbarMenu1(this, "Agent on Boarding");

        db = new DatabaseHelper(mContext);

        getUserDetails();

        str_doc_upload = new StringBuilder();
        str_final_aob_info = new StringBuilder();

        mParseXML = new ParseXML();

        mCalendar = Calendar.getInstance();

        View view_aob_doc_upload_formIA = findViewById(R.id.view_aob_doc_upload_formIA);
        TextView txt_aob_doc_upload_formIA = findViewById(R.id.txt_aob_doc_upload_formIA);
        if (is_ia_upgrade) {
            view_aob_doc_upload_formIA.setVisibility(View.GONE);
            txt_aob_doc_upload_formIA.setVisibility(View.GONE);
        } else {
            view_aob_doc_upload_formIA.setVisibility(View.VISIBLE);
            txt_aob_doc_upload_formIA.setVisibility(View.VISIBLE);
        }

        ll_aob_doc_upload_ia = findViewById(R.id.ll_aob_doc_upload_ia);
        ll_aob_doc_upload_ia_upgrade = findViewById(R.id.ll_aob_doc_upload_ia_upgrade);

        edt_aob_auth_others = (EditText) findViewById(R.id.edt_aob_auth_others);
        imgbtn_aob_auth_others_view = (ImageButton) findViewById(R.id.imgbtn_aob_auth_others_view);
        imgbtn_aob_auth_others_view.setOnClickListener(this);
        imgbtn_aob_auth_others_camera = (ImageButton) findViewById(R.id.imgbtn_aob_auth_others_camera);
        imgbtn_aob_auth_others_camera.setOnClickListener(this);
        imgbtn_aob_auth_others_browse = (ImageButton) findViewById(R.id.imgbtn_aob_auth_others_browse);
        imgbtn_aob_auth_others_browse.setOnClickListener(this);
        imgbtn_aob_auth_others_upload = (ImageButton) findViewById(R.id.imgbtn_aob_auth_others_upload);
        imgbtn_aob_auth_others_upload.setOnClickListener(this);
        txt_aob_auth_others_remark = findViewById(R.id.txt_aob_auth_others_remark);

        btn_aob_auth_pan_submit = (Button) findViewById(R.id.btn_aob_auth_pan_submit);
        btn_aob_auth_pan_submit.setOnClickListener(this);

        btn_aob_auth_pan_back = (Button) findViewById(R.id.btn_aob_auth_pan_back);
        btn_aob_auth_pan_back.setOnClickListener(this);

        if (is_ia_upgrade) {
            ll_aob_doc_upload_ia_upgrade.setVisibility(View.VISIBLE);
            ll_aob_doc_upload_ia.setVisibility(View.GONE);

            initialisation_ia_upgrade();
        } else {
            ll_aob_doc_upload_ia_upgrade.setVisibility(View.GONE);
            ll_aob_doc_upload_ia.setVisibility(View.VISIBLE);

            initialisation_ia();
        }
        setDocumentDBData();
        //non editable with no saving
        //editable
        enableDisableAllFields(!is_dashboard);

        if (is_rejection)
            setRejectionRecords();
    }

    private void setRejectionRecords() {

        imgbtn_ia_upgrade_tcc_camera.setEnabled(false);
        imgbtn_ia_upgrade_tcc_browse.setEnabled(false);
        imgbtn_ia_upgrade_tcc_upload.setEnabled(false);
        imgbtn_ia_upgrade_tcc_upload.setImageDrawable(getResources().getDrawable(R.drawable.checkedupload));
        is_ia_upgrade_tcc_uploaded = true;

        imgbtn_ia_upgrade_score_card_camera.setEnabled(false);
        imgbtn_ia_upgrade_score_card_browse.setEnabled(false);
        imgbtn_ia_upgrade_score_card_upload.setEnabled(false);
        imgbtn_ia_upgrade_score_card_upload.setImageDrawable(getResources().getDrawable(R.drawable.checkedupload));
        is_ia_upgrade_score_card_uploaded = true;

        imgbtn_ia_upgrade_ulip_camera.setEnabled(false);
        imgbtn_ia_upgrade_ulip_browse.setEnabled(false);
        imgbtn_ia_upgrade_ulip_upload.setEnabled(false);
        imgbtn_ia_upgrade_ulip_upload.setImageDrawable(getResources().getDrawable(R.drawable.checkedupload));
        is_ia_upgrade_ulip_uploaded = true;

        imgbtn_ia_upgrade_form_ia_camera.setEnabled(false);
        imgbtn_ia_upgrade_form_ia_browse.setEnabled(false);
        imgbtn_ia_upgrade_form_ia_upload.setEnabled(false);
        imgbtn_ia_upgrade_form_ia_upload.setImageDrawable(getResources().getDrawable(R.drawable.checkedupload));
        is_ia_upgrade_form_ia_uploaded = true;

        imgbtn_ia_upgrade_mob_declaration_camera.setEnabled(false);
        imgbtn_ia_upgrade_mob_declaration_browse.setEnabled(false);
        imgbtn_ia_upgrade_mob_declaration_upload.setEnabled(false);
        imgbtn_ia_upgrade_mob_declaration_upload.setImageDrawable(getResources().getDrawable(R.drawable.checkedupload));
        is_ia_upgrade_mob_declaration_uploaded = true;

        edt_aob_auth_others.setEnabled(false);
        imgbtn_aob_auth_others_camera.setEnabled(false);
        imgbtn_aob_auth_others_browse.setEnabled(false);
        imgbtn_aob_auth_others_upload.setEnabled(false);
        imgbtn_aob_auth_others_upload.setImageDrawable(getResources().getDrawable(R.drawable.checkedupload));
        is_others_doc_uploaded = true;

        if (str_pan_no.equals("")) {
            str_pan_no = str_ia_upgrade_pan_no;
        }

        ArrayList<POJO_POSP_RA_Rejection> lstRejection = db.getPOSP_RA_Rejection_By_PAN(str_pan_no,
                mCommonMethods.str_ia_upgrade_customer_type);
        for (POJO_POSP_RA_Rejection obj : lstRejection) {
            String docName = obj.getStrDocName();
            docName = docName == null ? "" : docName;
            String strRemark = obj.getStrRemarks();
            strRemark = strRemark == null ? "" : strRemark;

            if (docName.equals("Training Completion Certificate")) {
                if (strRemark.equals("")) {
                    txt_ia_upgrade_tcc_proof_remark.setVisibility(View.GONE);
                } else {
                    txt_ia_upgrade_tcc_proof_remark.setVisibility(View.VISIBLE);
                    txt_ia_upgrade_tcc_proof_remark.setText(strRemark);
                }

                imgbtn_ia_upgrade_tcc_camera.setEnabled(true);
                imgbtn_ia_upgrade_tcc_browse.setEnabled(true);
                imgbtn_ia_upgrade_tcc_upload.setEnabled(true);
                imgbtn_ia_upgrade_tcc_upload.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_uploaddoc));
                is_ia_upgrade_tcc_uploaded = false;
            }

            if (docName.equals("Exam Passing Proof")) {
                if (strRemark.equals("")) {
                    txt_ia_upgrade_score_card_proof_remark.setVisibility(View.GONE);
                } else {
                    txt_ia_upgrade_score_card_proof_remark.setText(strRemark);
                    txt_ia_upgrade_score_card_proof_remark.setVisibility(View.VISIBLE);
                }

                imgbtn_ia_upgrade_score_card_camera.setEnabled(true);
                imgbtn_ia_upgrade_score_card_browse.setEnabled(true);
                imgbtn_ia_upgrade_score_card_upload.setEnabled(true);
                imgbtn_ia_upgrade_score_card_upload.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_uploaddoc));
                is_ia_upgrade_score_card_uploaded = false;
            }

            if (docName.equals("ULIP Card Copy Certificate")) {
                if (strRemark.equals("")) {
                    txt_ia_upgrade_ulip_proof_remark.setVisibility(View.GONE);
                } else {
                    txt_ia_upgrade_ulip_proof_remark.setText(strRemark);
                    txt_ia_upgrade_ulip_proof_remark.setVisibility(View.VISIBLE);
                }

                imgbtn_ia_upgrade_ulip_camera.setEnabled(true);
                imgbtn_ia_upgrade_ulip_browse.setEnabled(true);
                imgbtn_ia_upgrade_ulip_upload.setEnabled(true);
                imgbtn_ia_upgrade_ulip_upload.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_uploaddoc));
                is_ia_upgrade_ulip_uploaded = false;
            }

            if (docName.equals("Form I-A")) {
                if (strRemark.equals("")) {
                    txt_ia_upgrade_form_ia_proof_remark.setVisibility(View.GONE);
                } else {
                    txt_ia_upgrade_form_ia_proof_remark.setText(strRemark);
                    txt_ia_upgrade_form_ia_proof_remark.setVisibility(View.VISIBLE);
                }

                imgbtn_ia_upgrade_form_ia_camera.setEnabled(true);
                imgbtn_ia_upgrade_form_ia_browse.setEnabled(true);
                imgbtn_ia_upgrade_form_ia_upload.setEnabled(true);
                imgbtn_ia_upgrade_form_ia_upload.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_uploaddoc));
                is_ia_upgrade_form_ia_uploaded = false;
            }

            if (docName.equals("Agent Certification1")) {
                if (strRemark.equals("")) {
                    txt_ia_upgrade_mob_declaration_proof_remark.setVisibility(View.GONE);
                } else {
                    txt_ia_upgrade_mob_declaration_proof_remark.setText(strRemark);
                    txt_ia_upgrade_mob_declaration_proof_remark.setVisibility(View.VISIBLE);
                }

                imgbtn_ia_upgrade_mob_declaration_camera.setEnabled(true);
                imgbtn_ia_upgrade_mob_declaration_browse.setEnabled(true);
                imgbtn_ia_upgrade_mob_declaration_upload.setEnabled(true);
                imgbtn_ia_upgrade_mob_declaration_upload.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_uploaddoc));
                is_ia_upgrade_mob_declaration_uploaded = false;
            }


            if (docName.equals("Others")) {
                if (strRemark.equals("")) {
                    txt_aob_auth_others_remark.setVisibility(View.GONE);
                } else {
                    txt_aob_auth_others_remark.setText(strRemark);
                    txt_aob_auth_others_remark.setVisibility(View.VISIBLE);
                }

                edt_aob_auth_others.setEnabled(true);
                imgbtn_aob_auth_others_camera.setEnabled(true);
                imgbtn_aob_auth_others_browse.setEnabled(true);
                imgbtn_aob_auth_others_upload.setEnabled(true);
                imgbtn_aob_auth_others_upload.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_uploaddoc));
                is_others_doc_uploaded = false;
            }
        }
    }

    private void setDocumentDBData() {
        ArrayList<PojoAOB> lst = db.get_agent_on_boarding_details_by_ID(Activity_AOB_Authentication.row_details);
        if (lst.size() > 0) {

            String str_doc_upload = lst.get(0).getStr_doc_upload();
            str_doc_upload = str_doc_upload == null ? "" : str_doc_upload;
            str_pan_no = lst.get(0).getStr_pan_no();

            if (!str_doc_upload.equals("")) {

                String str_others_doc_type = mParseXML.parseXmlTag(str_doc_upload, "doc_upload_others_type");
                str_others_doc_type = str_others_doc_type == null ? "" : str_others_doc_type;
                String str_others_document = mParseXML.parseXmlTag(str_doc_upload, "doc_upload_others_document");
                str_others_document = str_others_document == null ? "" : str_others_document;
                String str_others_is_uploaded = mParseXML.parseXmlTag(str_doc_upload, "doc_upload_others_is_upload");
                str_others_is_uploaded = str_others_is_uploaded == null ? "" : str_others_is_uploaded;

                if (str_others_doc_type.equals("CAMERA")) {

                    imgbtn_aob_auth_others_camera.setImageDrawable(getResources().getDrawable(R.drawable.checkedcamera));
                    imgbtn_aob_auth_others_browse.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_browsedoc));

                } else if (str_others_doc_type.equals("BROWSE")) {
                    imgbtn_aob_auth_others_camera.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_camera));
                    imgbtn_aob_auth_others_browse.setImageDrawable(getResources().getDrawable(R.drawable.checkedbrowse));
                }

                edt_aob_auth_others.setText(str_others_document);

                if (str_others_is_uploaded.equals("true")) {
                    imgbtn_aob_auth_others_upload.setImageDrawable(getResources().getDrawable(R.drawable.checkedupload));
                } else {
                    imgbtn_aob_auth_others_upload.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_uploaddoc));
                }

                if (is_ia_upgrade) {
                    //IA upgrade
                    String str_ia_upgrade_tcc_type = mParseXML.parseXmlTag(str_doc_upload, "doc_upload_ia_upgrade_tcc_type");
                    str_ia_upgrade_tcc_type = str_ia_upgrade_tcc_type == null ? "" : str_ia_upgrade_tcc_type;
                    String str_ia_upgrade_tcc_is_uploaded = mParseXML.parseXmlTag(str_doc_upload, "doc_upload_ia_upgrade_tcc_is_upload");
                    str_ia_upgrade_tcc_is_uploaded = str_ia_upgrade_tcc_is_uploaded == null ? "" : str_ia_upgrade_tcc_is_uploaded;
                    if (str_ia_upgrade_tcc_type.equals("CAMERA")) {
                        imgbtn_ia_upgrade_tcc_camera.setImageDrawable(getResources().getDrawable(R.drawable.checkedcamera));
                        imgbtn_ia_upgrade_tcc_browse.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_browsedoc));
                    } else if (str_ia_upgrade_tcc_type.equals("BROWSE")) {
                        imgbtn_ia_upgrade_tcc_browse.setImageDrawable(getResources().getDrawable(R.drawable.checkedbrowse));
                        imgbtn_ia_upgrade_tcc_camera.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_camera));
                    }

                    if (str_ia_upgrade_tcc_is_uploaded.equals("true")) {
                        imgbtn_ia_upgrade_tcc_upload.setImageDrawable(getResources().getDrawable(R.drawable.checkedupload));
                    } else {
                        imgbtn_ia_upgrade_tcc_upload.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_uploaddoc));
                    }

                    String str_ia_upgrade_Score_card_type = mParseXML.parseXmlTag(str_doc_upload, "doc_upload_ia_upgrade_score_card_type");
                    str_ia_upgrade_Score_card_type = str_ia_upgrade_Score_card_type == null ? "" : str_ia_upgrade_Score_card_type;
                    String str_ia_upgrade_score_card_is_uploaded = mParseXML.parseXmlTag(str_doc_upload, "doc_upload_ia_upgrade_score_card_is_upload");
                    str_ia_upgrade_score_card_is_uploaded = str_ia_upgrade_score_card_is_uploaded == null ? "" : str_ia_upgrade_score_card_is_uploaded;
                    if (str_ia_upgrade_Score_card_type.equals("CAMERA")) {
                        imgbtn_ia_upgrade_score_card_camera.setImageDrawable(getResources().getDrawable(R.drawable.checkedcamera));
                        imgbtn_ia_upgrade_score_card_browse.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_browsedoc));
                    } else if (str_ia_upgrade_Score_card_type.equals("BROWSE")) {
                        imgbtn_ia_upgrade_score_card_browse.setImageDrawable(getResources().getDrawable(R.drawable.checkedbrowse));
                        imgbtn_ia_upgrade_score_card_camera.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_camera));
                    }

                    if (str_ia_upgrade_score_card_is_uploaded.equals("true")) {
                        imgbtn_ia_upgrade_score_card_upload.setImageDrawable(getResources().getDrawable(R.drawable.checkedupload));
                    } else {
                        imgbtn_ia_upgrade_score_card_upload.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_uploaddoc));
                    }

                    String str_ia_upgrade_ulip_type = mParseXML.parseXmlTag(str_doc_upload, "doc_upload_ia_upgrade_ulip_type");
                    str_ia_upgrade_ulip_type = str_ia_upgrade_ulip_type == null ? "" : str_ia_upgrade_ulip_type;
                    String str_ia_upgrade_ulip_is_uploaded = mParseXML.parseXmlTag(str_doc_upload, "doc_upload_ia_upgrade_ulip_is_upload");
                    str_ia_upgrade_ulip_is_uploaded = str_ia_upgrade_ulip_is_uploaded == null ? "" : str_ia_upgrade_ulip_is_uploaded;
                    if (str_ia_upgrade_ulip_type.equals("CAMERA")) {
                        imgbtn_ia_upgrade_ulip_camera.setImageDrawable(getResources().getDrawable(R.drawable.checkedcamera));
                        imgbtn_ia_upgrade_ulip_browse.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_browsedoc));
                    } else if (str_ia_upgrade_ulip_type.equals("BROWSE")) {
                        imgbtn_ia_upgrade_ulip_browse.setImageDrawable(getResources().getDrawable(R.drawable.checkedbrowse));
                        imgbtn_ia_upgrade_ulip_camera.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_camera));
                    }

                    if (str_ia_upgrade_ulip_is_uploaded.equals("true")) {
                        imgbtn_ia_upgrade_ulip_upload.setImageDrawable(getResources().getDrawable(R.drawable.checkedupload));
                    } else {
                        imgbtn_ia_upgrade_ulip_upload.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_uploaddoc));
                    }

                    String str_ia_upgrade_form_ia_type = mParseXML.parseXmlTag(str_doc_upload, "doc_upload_ia_upgrade_form_ia_type");
                    str_ia_upgrade_form_ia_type = str_ia_upgrade_form_ia_type == null ? "" : str_ia_upgrade_form_ia_type;
                    String str_ia_upgrade_form_ia_is_uploaded = mParseXML.parseXmlTag(str_doc_upload, "doc_upload_ia_upgrade_form_ia_is_upload");
                    str_ia_upgrade_form_ia_is_uploaded = str_ia_upgrade_form_ia_is_uploaded == null ? "" : str_ia_upgrade_form_ia_is_uploaded;
                    if (str_ia_upgrade_form_ia_type.equals("CAMERA")) {
                        imgbtn_ia_upgrade_form_ia_camera.setImageDrawable(getResources().getDrawable(R.drawable.checkedcamera));
                        imgbtn_ia_upgrade_form_ia_browse.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_browsedoc));
                    } else if (str_ia_upgrade_form_ia_type.equals("BROWSE")) {
                        imgbtn_ia_upgrade_form_ia_browse.setImageDrawable(getResources().getDrawable(R.drawable.checkedbrowse));
                        imgbtn_ia_upgrade_form_ia_camera.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_camera));
                    }

                    if (str_ia_upgrade_form_ia_is_uploaded.equals("true")) {
                        imgbtn_ia_upgrade_form_ia_upload.setImageDrawable(getResources().getDrawable(R.drawable.checkedupload));
                    } else {
                        imgbtn_ia_upgrade_form_ia_upload.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_uploaddoc));
                    }

                    String str_ia_upgrade_mob_declaration_type = mParseXML.parseXmlTag(str_doc_upload, "doc_upload_ia_upgrade_mob_declaration_type");
                    str_ia_upgrade_mob_declaration_type = str_ia_upgrade_mob_declaration_type == null ? "" : str_ia_upgrade_mob_declaration_type;
                    String str_ia_upgrade_mob_declaration_is_uploaded = mParseXML.parseXmlTag(str_doc_upload, "doc_upload_ia_upgrade_mob_declaration_is_upload");
                    str_ia_upgrade_mob_declaration_is_uploaded = str_ia_upgrade_mob_declaration_is_uploaded == null ? "" : str_ia_upgrade_mob_declaration_is_uploaded;
                    if (str_ia_upgrade_mob_declaration_type.equals("CAMERA")) {
                        imgbtn_ia_upgrade_mob_declaration_camera.setImageDrawable(getResources().getDrawable(R.drawable.checkedcamera));
                        imgbtn_ia_upgrade_mob_declaration_browse.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_browsedoc));
                    } else if (str_ia_upgrade_mob_declaration_type.equals("BROWSE")) {
                        imgbtn_ia_upgrade_mob_declaration_browse.setImageDrawable(getResources().getDrawable(R.drawable.checkedbrowse));
                        imgbtn_ia_upgrade_mob_declaration_camera.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_camera));
                    }

                    if (str_ia_upgrade_mob_declaration_is_uploaded.equals("true")) {
                        imgbtn_ia_upgrade_mob_declaration_upload.setImageDrawable(getResources().getDrawable(R.drawable.checkedupload));
                    } else {
                        imgbtn_ia_upgrade_mob_declaration_upload.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_uploaddoc));
                    }

                } else {

                    String str_bank_pass_type = mParseXML.parseXmlTag(str_doc_upload, "doc_upload_bank_pass_type");
                    str_bank_pass_type = str_bank_pass_type == null ? "" : str_bank_pass_type;
                    String str_bank_pass_is_uploaded = mParseXML.parseXmlTag(str_doc_upload, "doc_upload_bank_pass_is_upload");
                    str_bank_pass_is_uploaded = str_bank_pass_is_uploaded == null ? "" : str_bank_pass_is_uploaded;

                    str_age_proof_type = mParseXML.parseXmlTag(str_doc_upload, "doc_upload_age_proof_type");
                    str_age_proof_type = str_age_proof_type == null ? "" : str_age_proof_type;

                    String str_age_proof_is_uploaded = mParseXML.parseXmlTag(str_doc_upload, "doc_upload_age_proof_is_upload");
                    str_age_proof_is_uploaded = str_age_proof_is_uploaded == null ? "" : str_age_proof_is_uploaded;

                    String str_basic_qualification_type = mParseXML.parseXmlTag(str_doc_upload, "doc_upload_basic_qualification_type");
                    str_basic_qualification_type = str_basic_qualification_type == null ? "" : str_basic_qualification_type;
                    String str_basic_qualification_is_uploaded = mParseXML.parseXmlTag(str_doc_upload, "doc_upload_basic_qualification_is_upload");
                    str_basic_qualification_is_uploaded = str_basic_qualification_is_uploaded == null ? "" : str_basic_qualification_is_uploaded;

                    String str_high_qualification_type = mParseXML.parseXmlTag(str_doc_upload, "doc_upload_high_qualification_type");
                    str_high_qualification_type = str_high_qualification_type == null ? "" : str_high_qualification_type;
                    String str_high_qualification_is_uplaoded = mParseXML.parseXmlTag(str_doc_upload, "doc_upload_high_qualification_is_upload");
                    str_high_qualification_is_uplaoded = str_high_qualification_is_uplaoded == null ? "" : str_high_qualification_is_uplaoded;

                    str_comm_address_type = mParseXML.parseXmlTag(str_doc_upload, "doc_upload_comm_address_type");
                    str_comm_address_type = str_comm_address_type == null ? "" : str_comm_address_type;

                    String str_comm_address_is_uploaded = mParseXML.parseXmlTag(str_doc_upload, "doc_upload_comm_address_is_upload");
                    str_comm_address_is_uploaded = str_comm_address_is_uploaded == null ? "" : str_comm_address_is_uploaded;

                    String str_permanent_address_type = mParseXML.parseXmlTag(str_doc_upload, "doc_upload_permanent_address_type");
                    str_permanent_address_type = str_permanent_address_type == null ? "" : str_permanent_address_type;
                    String str_permanent_address_is_uploaded = mParseXML.parseXmlTag(str_doc_upload, "doc_upload_permanent_address_is_upload");
                    str_permanent_address_is_uploaded = str_permanent_address_is_uploaded == null ? "" : str_permanent_address_is_uploaded;

                    String str_form15g_15h_type = mParseXML.parseXmlTag(str_doc_upload, "doc_upload_form_15G_15H_type");
                    str_form15g_15h_type = str_form15g_15h_type == null ? "" : str_form15g_15h_type;
                    String str_form15g_15h_is_uploaded = mParseXML.parseXmlTag(str_doc_upload, "doc_upload_form_15G_15H_is_upload");
                    str_form15g_15h_is_uploaded = str_form15g_15h_is_uploaded == null ? "" : str_form15g_15h_is_uploaded;

                    String str_form1B_type = mParseXML.parseXmlTag(str_doc_upload, "doc_upload_form_1B_type");
                    str_form1B_type = str_form1B_type == null ? "" : str_form1B_type;
                    String str_form1B_is_uploaded = mParseXML.parseXmlTag(str_doc_upload, "doc_upload_form_1B_is_upload");
                    str_form1B_is_uploaded = str_form1B_is_uploaded == null ? "" : str_form1B_is_uploaded;

                    String str_form1C_type = mParseXML.parseXmlTag(str_doc_upload, "doc_upload_form_1C_type");
                    str_form1C_type = str_form1C_type == null ? "" : str_form1C_type;
                    String str_form1C_is_uploaded = mParseXML.parseXmlTag(str_doc_upload, "doc_upload_form_1C_is_upload");
                    str_form1C_is_uploaded = str_form1C_is_uploaded == null ? "" : str_form1C_is_uploaded;

                    String str_applicant_sign_type = mParseXML.parseXmlTag(str_doc_upload, "doc_upload_applicant_sign_type");
                    str_applicant_sign_type = str_applicant_sign_type == null ? "" : str_applicant_sign_type;
                    String str_applicant_sign_is_uploaded = mParseXML.parseXmlTag(str_doc_upload, "doc_upload_applicant_sign_is_upload");
                    str_applicant_sign_is_uploaded = str_applicant_sign_is_uploaded == null ? "" : str_applicant_sign_is_uploaded;

                    String str_declaration_type = mParseXML.parseXmlTag(str_doc_upload, "doc_upload_declaration_type");
                    str_declaration_type = str_declaration_type == null ? "" : str_declaration_type;
                    String str_declaration_is_uploaded = mParseXML.parseXmlTag(str_doc_upload, "doc_upload_declaration_is_upload");
                    str_declaration_is_uploaded = str_declaration_is_uploaded == null ? "" : str_declaration_is_uploaded;

                    if (str_declaration_type.equals("CAMERA")) {

                        imgbtn_aob_declaration_camera.setImageDrawable(getResources().getDrawable(R.drawable.checkedcamera));
                        imgbtn_aob_declaration_browse.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_browsedoc));

                    } else if (str_declaration_type.equals("BROWSE")) {
                        imgbtn_aob_declaration_browse.setImageDrawable(getResources().getDrawable(R.drawable.checkedbrowse));
                        imgbtn_aob_declaration_camera.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_camera));
                    }

                    if (str_declaration_is_uploaded.equals("true")) {
                        imgbtn_aob_declaration_upload.setImageDrawable(getResources().getDrawable(R.drawable.checkedupload));
                    } else {
                        imgbtn_aob_declaration_upload.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_uploaddoc));
                    }

                    if (str_applicant_sign_type.equals("CAMERA")) {

                        imgbtn_aob_applicant_sign_camera.setImageDrawable(getResources().getDrawable(R.drawable.checkedcamera));
                        imgbtn_aob_applicant_sign_browse.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_browsedoc));

                    } else if (str_applicant_sign_type.equals("BROWSE")) {
                        imgbtn_aob_applicant_sign_camera.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_camera));
                        imgbtn_aob_applicant_sign_browse.setImageDrawable(getResources().getDrawable(R.drawable.checkedbrowse));
                    }

                    if (str_applicant_sign_is_uploaded.equals("true")) {
                        imgbtn_aob_applicant_sign_upload.setImageDrawable(getResources().getDrawable(R.drawable.checkedupload));
                    } else {
                        imgbtn_aob_applicant_sign_upload.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_uploaddoc));
                    }

                    if (str_bank_pass_type.equals("CAMERA")) {

                        imgbtn_aob_auth_bank_pass_book_camera.setImageDrawable(getResources().getDrawable(R.drawable.checkedcamera));
                        imgbtn_aob_auth_bank_pass_book_browse.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_browsedoc));

                    } else if (str_bank_pass_type.equals("BROWSE")) {
                        imgbtn_aob_auth_bank_pass_book_camera.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_camera));
                        imgbtn_aob_auth_bank_pass_book_browse.setImageDrawable(getResources().getDrawable(R.drawable.checkedbrowse));
                    }

                    if (str_bank_pass_is_uploaded.equals("true")) {
                        imgbtn_aob_auth_bank_pass_book_upload.setImageDrawable(getResources().getDrawable(R.drawable.checkedupload));
                    } else {
                        imgbtn_aob_auth_bank_pass_book_upload.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_uploaddoc));
                    }

                    if (str_age_proof_type.equals("CAMERA")) {

                        imgbtn_aob_auth_age_proof_camera.setImageDrawable(getResources().getDrawable(R.drawable.checkedcamera));
                        imgbtn_aob_auth_age_proof_browse.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_browsedoc));

                    } else if (str_age_proof_type.equals("BROWSE")) {
                        imgbtn_aob_auth_age_proof_camera.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_camera));
                        imgbtn_aob_auth_age_proof_browse.setImageDrawable(getResources().getDrawable(R.drawable.checkedbrowse));
                    } else if (str_age_proof_type.equals("OFF-LINE EKYC")) {
                        imgbtn_aob_auth_age_proof_camera.setClickable(false);
                        imgbtn_aob_auth_age_proof_browse.setClickable(false);
                        imgbtn_aob_auth_age_proof_upload.setClickable(false);
                    }

                    if (str_age_proof_is_uploaded.equals("true")) {
                        is_age_proof_uploaded = true;
                        imgbtn_aob_auth_age_proof_upload.setImageDrawable(getResources().getDrawable(R.drawable.checkedupload));
                    } else {
                        is_age_proof_uploaded = false;
                        imgbtn_aob_auth_age_proof_upload.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_uploaddoc));
                    }

                    if (str_basic_qualification_type.equals("CAMERA")) {

                        imgbtn_aob_auth_basic_qualification_camera.setImageDrawable(getResources().getDrawable(R.drawable.checkedcamera));
                        imgbtn_aob_auth_basic_qualification_browse.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_browsedoc));

                    } else if (str_basic_qualification_type.equals("BROWSE")) {
                        imgbtn_aob_auth_basic_qualification_camera.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_camera));
                        imgbtn_aob_auth_basic_qualification_browse.setImageDrawable(getResources().getDrawable(R.drawable.checkedbrowse));
                    }

                    if (str_basic_qualification_is_uploaded.equals("true")) {
                        imgbtn_aob_auth_basic_qualification_upload.setImageDrawable(getResources().getDrawable(R.drawable.checkedupload));
                    } else {
                        imgbtn_aob_auth_basic_qualification_upload.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_uploaddoc));
                    }

                    if (str_high_qualification_type.equals("CAMERA")) {

                        imgbtn_aob_auth_high_qualification_camera.setImageDrawable(getResources().getDrawable(R.drawable.checkedcamera));
                        imgbtn_aob_auth_high_qualification_browse.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_browsedoc));

                    } else if (str_high_qualification_type.equals("BROWSE")) {
                        imgbtn_aob_auth_high_qualification_camera.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_camera));
                        imgbtn_aob_auth_high_qualification_browse.setImageDrawable(getResources().getDrawable(R.drawable.checkedbrowse));
                    }

                    if (str_high_qualification_is_uplaoded.equals("true")) {
                        imgbtn_aob_auth_high_qualification_upload.setImageDrawable(getResources().getDrawable(R.drawable.checkedupload));
                    } else {
                        imgbtn_aob_auth_high_qualification_upload.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_uploaddoc));
                    }

                    if (str_comm_address_type.equals("CAMERA")) {

                        imgbtn_aob_auth_comm_add_camera.setImageDrawable(getResources().getDrawable(R.drawable.checkedcamera));
                        imgbtn_aob_auth_comm_add_browse.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_browsedoc));

                    } else if (str_comm_address_type.equals("BROWSE")) {
                        imgbtn_aob_auth_comm_add_camera.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_camera));
                        imgbtn_aob_auth_comm_add_browse.setImageDrawable(getResources().getDrawable(R.drawable.checkedbrowse));
                    } else if (str_age_proof_type.equals("OFF-LINE EKYC")) {
                        imgbtn_aob_auth_comm_add_camera.setClickable(false);
                        imgbtn_aob_auth_comm_add_browse.setClickable(false);

                        imgbtn_aob_auth_comm_add_upload.setClickable(false);
                    }

                    if (str_comm_address_is_uploaded.equals("true")) {
                        is_comm_address_uploaded = true;

                        imgbtn_aob_auth_comm_add_upload.setImageDrawable(getResources().getDrawable(R.drawable.checkedupload));
                    } else {
                        is_comm_address_uploaded = false;

                        imgbtn_aob_auth_comm_add_upload.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_uploaddoc));
                    }

                    if (str_permanent_address_type.equals("CAMERA")) {

                        imgbtn_aob_auth_permanent_add_camera.setImageDrawable(getResources().getDrawable(R.drawable.checkedcamera));
                        imgbtn_aob_auth_permanent_add_browse.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_browsedoc));

                    } else if (str_permanent_address_type.equals("BROWSE")) {
                        imgbtn_aob_auth_permanent_add_camera.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_camera));
                        imgbtn_aob_auth_permanent_add_browse.setImageDrawable(getResources().getDrawable(R.drawable.checkedbrowse));
                    }

                    if (str_permanent_address_is_uploaded.equals("true")) {
                        imgbtn_aob_auth_permanent_add_upload.setImageDrawable(getResources().getDrawable(R.drawable.checkedupload));
                    } else {
                        imgbtn_aob_auth_permanent_add_upload.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_uploaddoc));
                    }

                    if (str_form15g_15h_type.equals("CAMERA")) {

                        imgbtn_aob_auth_form15g_15h_camera.setImageDrawable(getResources().getDrawable(R.drawable.checkedcamera));
                        imgbtn_aob_auth_form15g_15h_browse.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_browsedoc));

                    } else if (str_form15g_15h_type.equals("BROWSE")) {
                        imgbtn_aob_auth_form15g_15h_camera.setImageDrawable(getResources().getDrawable(R.drawable.checkedbrowse));
                        imgbtn_aob_auth_form15g_15h_browse.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_camera));
                    }

                    if (str_form15g_15h_is_uploaded.equals("true")) {
                        imgbtn_aob_auth_form15g_15h_upload.setImageDrawable(getResources().getDrawable(R.drawable.checkedupload));
                    } else {
                        imgbtn_aob_auth_form15g_15h_upload.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_uploaddoc));
                    }

                    if (str_form1B_type.equals("CAMERA")) {

                        imgbtn_aob_auth_form1b_camera.setImageDrawable(getResources().getDrawable(R.drawable.checkedcamera));
                        imgbtn_aob_auth_form1b_browse.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_browsedoc));

                    } else if (str_form1B_type.equals("BROWSE")) {
                        imgbtn_aob_auth_form1b_camera.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_camera));
                        imgbtn_aob_auth_form1b_browse.setImageDrawable(getResources().getDrawable(R.drawable.checkedbrowse));
                    }

                    if (str_form1B_is_uploaded.equals("true")) {
                        imgbtn_aob_auth_form1b_upload.setImageDrawable(getResources().getDrawable(R.drawable.checkedupload));
                    } else {
                        imgbtn_aob_auth_form1b_upload.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_uploaddoc));
                    }

                    if (str_form1C_type.equals("CAMERA")) {

                        imgbtn_aob_auth_form1c_camera.setImageDrawable(getResources().getDrawable(R.drawable.checkedcamera));
                        imgbtn_aob_auth_form1c_browse.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_browsedoc));

                    } else if (str_form1C_type.equals("BROWSE")) {
                        imgbtn_aob_auth_form1c_camera.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_camera));
                        imgbtn_aob_auth_form1c_browse.setImageDrawable(getResources().getDrawable(R.drawable.checkedbrowse));
                    }

                    if (str_form1C_is_uploaded.equals("true")) {
                        imgbtn_aob_auth_form1c_upload.setImageDrawable(getResources().getDrawable(R.drawable.checkedupload));
                    } else {
                        imgbtn_aob_auth_form1c_upload.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_uploaddoc));
                    }
                }
            }
        }
    }

    private void getUserDetails() {
        CommonMethods.UserDetailsValuesModel userDetailsValuesModel = mCommonMethods.setUserDetails(mContext);
        strCIFBDMUserId = userDetailsValuesModel.getStrCIFBDMUserId();
        strCIFBDMEmailId = userDetailsValuesModel.getStrCIFBDMEmailId();
        strCIFBDMMObileNo = userDetailsValuesModel.getStrCIFBDMMObileNo();
    }

    @Override
    public void onBackPressed() {

        if (is_rejection) {
            Intent i = new Intent(ActivityAOBDocumentUpload.this, Activity_POSP_RA_Rejection_Remarks.class);
            i.putExtra("enrollment_type", mCommonMethods.str_ia_upgrade_customer_type);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        } else {
            Intent intent = new Intent(ActivityAOBDocumentUpload.this, ActivityAOBTermsConditionsDeclaration.class);
            if (is_dashboard) {
                intent.putExtra("is_dashboard", is_dashboard);
            } else if (is_ia_upgrade) {
                intent.putExtra("is_ia_upgrade", is_ia_upgrade);
            }
            startActivity(intent);
        }
    }

    public void initialisation_ia_upgrade() {
        imgbtn_ia_upgrade_tcc_view = (ImageButton) findViewById(R.id.imgbtn_ia_upgrade_tcc_view);
        imgbtn_ia_upgrade_tcc_view.setOnClickListener(this);
        imgbtn_ia_upgrade_tcc_camera = (ImageButton) findViewById(R.id.imgbtn_ia_upgrade_tcc_camera);
        imgbtn_ia_upgrade_tcc_camera.setOnClickListener(this);
        imgbtn_ia_upgrade_tcc_browse = (ImageButton) findViewById(R.id.imgbtn_ia_upgrade_tcc_browse);
        imgbtn_ia_upgrade_tcc_browse.setOnClickListener(this);
        imgbtn_ia_upgrade_tcc_upload = (ImageButton) findViewById(R.id.imgbtn_ia_upgrade_tcc_upload);
        imgbtn_ia_upgrade_tcc_upload.setOnClickListener(this);
        txt_ia_upgrade_tcc_proof_remark = findViewById(R.id.txt_ia_upgrade_tcc_proof_remark);

        imgbtn_ia_upgrade_score_card_view = (ImageButton) findViewById(R.id.imgbtn_ia_upgrade_score_card_view);
        imgbtn_ia_upgrade_score_card_view.setOnClickListener(this);
        imgbtn_ia_upgrade_score_card_camera = (ImageButton) findViewById(R.id.imgbtn_ia_upgrade_score_card_camera);
        imgbtn_ia_upgrade_score_card_camera.setOnClickListener(this);
        imgbtn_ia_upgrade_score_card_browse = (ImageButton) findViewById(R.id.imgbtn_ia_upgrade_score_card_browse);
        imgbtn_ia_upgrade_score_card_browse.setOnClickListener(this);
        imgbtn_ia_upgrade_score_card_upload = (ImageButton) findViewById(R.id.imgbtn_ia_upgrade_score_card_upload);
        imgbtn_ia_upgrade_score_card_upload.setOnClickListener(this);
        txt_ia_upgrade_score_card_proof_remark = findViewById(R.id.txt_ia_upgrade_score_card_proof_remark);

        imgbtn_ia_upgrade_ulip_view = (ImageButton) findViewById(R.id.imgbtn_ia_upgrade_ulip_view);
        imgbtn_ia_upgrade_ulip_view.setOnClickListener(this);
        imgbtn_ia_upgrade_ulip_camera = (ImageButton) findViewById(R.id.imgbtn_ia_upgrade_ulip_camera);
        imgbtn_ia_upgrade_ulip_camera.setOnClickListener(this);
        imgbtn_ia_upgrade_ulip_browse = (ImageButton) findViewById(R.id.imgbtn_ia_upgrade_ulip_browse);
        imgbtn_ia_upgrade_ulip_browse.setOnClickListener(this);
        imgbtn_ia_upgrade_ulip_upload = (ImageButton) findViewById(R.id.imgbtn_ia_upgrade_ulip_upload);
        imgbtn_ia_upgrade_ulip_upload.setOnClickListener(this);
        txt_ia_upgrade_ulip_proof_remark = findViewById(R.id.txt_ia_upgrade_ulip_proof_remark);

        imgbtn_ia_upgrade_form_ia_view = (ImageButton) findViewById(R.id.imgbtn_ia_upgrade_form_ia_view);
        imgbtn_ia_upgrade_form_ia_view.setOnClickListener(this);
        imgbtn_ia_upgrade_form_ia_camera = (ImageButton) findViewById(R.id.imgbtn_ia_upgrade_form_ia_camera);
        imgbtn_ia_upgrade_form_ia_camera.setOnClickListener(this);
        imgbtn_ia_upgrade_form_ia_browse = (ImageButton) findViewById(R.id.imgbtn_ia_upgrade_form_ia_browse);
        imgbtn_ia_upgrade_form_ia_browse.setOnClickListener(this);
        imgbtn_ia_upgrade_form_ia_upload = (ImageButton) findViewById(R.id.imgbtn_ia_upgrade_form_ia_upload);
        imgbtn_ia_upgrade_form_ia_upload.setOnClickListener(this);
        txt_ia_upgrade_form_ia_proof_remark = findViewById(R.id.txt_ia_upgrade_form_ia_proof_remark);

        imgbtn_ia_upgrade_mob_declaration_view = (ImageButton) findViewById(R.id.imgbtn_ia_upgrade_mob_declaration_view);
        imgbtn_ia_upgrade_mob_declaration_view.setOnClickListener(this);
        imgbtn_ia_upgrade_mob_declaration_camera = (ImageButton) findViewById(R.id.imgbtn_ia_upgrade_mob_declaration_camera);
        imgbtn_ia_upgrade_mob_declaration_camera.setOnClickListener(this);
        imgbtn_ia_upgrade_mob_declaration_browse = (ImageButton) findViewById(R.id.imgbtn_ia_upgrade_mob_declaration_browse);
        imgbtn_ia_upgrade_mob_declaration_browse.setOnClickListener(this);
        imgbtn_ia_upgrade_mob_declaration_upload = (ImageButton) findViewById(R.id.imgbtn_ia_upgrade_mob_declaration_upload);
        imgbtn_ia_upgrade_mob_declaration_upload.setOnClickListener(this);
        txt_ia_upgrade_mob_declaration_proof_remark = findViewById(R.id.txt_ia_upgrade_mob_declaration_proof_remark);
    }

    /*private void capture_docs(String str_doc_type, String str_ocr_type) {

        str_doc = str_doc_type;
        OCR_TYPE = str_ocr_type;

        Intent intent = new Intent(ActivityAOBDocumentUpload.this, OcrActivity.class);
        startActivityForResult(intent, REQUEST_OCR);
    }*/

    public void initialisation_ia() {

        imgbtn_aob_auth_bank_pass_book_view = (ImageButton) findViewById(R.id.imgbtn_aob_auth_bank_pass_book_view);
        imgbtn_aob_auth_bank_pass_book_view.setOnClickListener(this);
        imgbtn_aob_auth_bank_pass_book_camera = (ImageButton) findViewById(R.id.imgbtn_aob_auth_bank_pass_book_camera);
        imgbtn_aob_auth_bank_pass_book_camera.setOnClickListener(this);
        imgbtn_aob_auth_bank_pass_book_browse = (ImageButton) findViewById(R.id.imgbtn_aob_auth_bank_pass_book_browse);
        imgbtn_aob_auth_bank_pass_book_browse.setOnClickListener(this);
        imgbtn_aob_auth_bank_pass_book_upload = (ImageButton) findViewById(R.id.imgbtn_aob_auth_bank_pass_book_upload);
        imgbtn_aob_auth_bank_pass_book_upload.setOnClickListener(this);

        imgbtn_aob_auth_age_proof_view = (ImageButton) findViewById(R.id.imgbtn_aob_auth_age_proof_view);
        imgbtn_aob_auth_age_proof_view.setOnClickListener(this);
        imgbtn_aob_auth_age_proof_camera = (ImageButton) findViewById(R.id.imgbtn_aob_auth_age_proof_camera);
        imgbtn_aob_auth_age_proof_camera.setOnClickListener(this);
        imgbtn_aob_auth_age_proof_browse = (ImageButton) findViewById(R.id.imgbtn_aob_auth_age_proof_browse);
        imgbtn_aob_auth_age_proof_browse.setOnClickListener(this);
        imgbtn_aob_auth_age_proof_upload = (ImageButton) findViewById(R.id.imgbtn_aob_auth_age_proof_upload);
        imgbtn_aob_auth_age_proof_upload.setOnClickListener(this);

        imgbtn_aob_auth_basic_qualification_view = (ImageButton) findViewById(R.id.imgbtn_aob_auth_basic_qualification_view);
        imgbtn_aob_auth_basic_qualification_view.setOnClickListener(this);
        imgbtn_aob_auth_basic_qualification_camera = (ImageButton) findViewById(R.id.imgbtn_aob_auth_basic_qualification_camera);
        imgbtn_aob_auth_basic_qualification_camera.setOnClickListener(this);
        imgbtn_aob_auth_basic_qualification_browse = (ImageButton) findViewById(R.id.imgbtn_aob_auth_basic_qualification_browse);
        imgbtn_aob_auth_basic_qualification_browse.setOnClickListener(this);
        imgbtn_aob_auth_basic_qualification_upload = (ImageButton) findViewById(R.id.imgbtn_aob_auth_basic_qualification_upload);
        imgbtn_aob_auth_basic_qualification_upload.setOnClickListener(this);

        imgbtn_aob_auth_high_qualification_view = (ImageButton) findViewById(R.id.imgbtn_aob_auth_high_qualification_view);
        imgbtn_aob_auth_high_qualification_view.setOnClickListener(this);
        imgbtn_aob_auth_high_qualification_camera = (ImageButton) findViewById(R.id.imgbtn_aob_auth_high_qualification_camera);
        imgbtn_aob_auth_high_qualification_camera.setOnClickListener(this);
        imgbtn_aob_auth_high_qualification_browse = (ImageButton) findViewById(R.id.imgbtn_aob_auth_high_qualification_browse);
        imgbtn_aob_auth_high_qualification_browse.setOnClickListener(this);
        imgbtn_aob_auth_high_qualification_upload = (ImageButton) findViewById(R.id.imgbtn_aob_auth_high_qualification_upload);
        imgbtn_aob_auth_high_qualification_upload.setOnClickListener(this);

        imgbtn_aob_auth_comm_add_view = (ImageButton) findViewById(R.id.imgbtn_aob_auth_comm_add_view);
        imgbtn_aob_auth_comm_add_view.setOnClickListener(this);
        imgbtn_aob_auth_comm_add_camera = (ImageButton) findViewById(R.id.imgbtn_aob_auth_comm_add_camera);
        imgbtn_aob_auth_comm_add_camera.setOnClickListener(this);
        imgbtn_aob_auth_comm_add_browse = (ImageButton) findViewById(R.id.imgbtn_aob_auth_comm_add_browse);
        imgbtn_aob_auth_comm_add_browse.setOnClickListener(this);
        imgbtn_aob_auth_comm_add_upload = (ImageButton) findViewById(R.id.imgbtn_aob_auth_comm_add_upload);
        imgbtn_aob_auth_comm_add_upload.setOnClickListener(this);

        imgbtn_aob_auth_permanent_add_view = (ImageButton) findViewById(R.id.imgbtn_aob_auth_permanent_add_view);
        imgbtn_aob_auth_permanent_add_view.setOnClickListener(this);
        imgbtn_aob_auth_permanent_add_camera = (ImageButton) findViewById(R.id.imgbtn_aob_auth_permanent_add_camera);
        imgbtn_aob_auth_permanent_add_camera.setOnClickListener(this);
        imgbtn_aob_auth_permanent_add_browse = (ImageButton) findViewById(R.id.imgbtn_aob_auth_permanent_add_browse);
        imgbtn_aob_auth_permanent_add_browse.setOnClickListener(this);
        imgbtn_aob_auth_permanent_add_upload = (ImageButton) findViewById(R.id.imgbtn_aob_auth_permanent_add_upload);
        imgbtn_aob_auth_permanent_add_upload.setOnClickListener(this);

        imgbtn_aob_auth_form15g_15h_view = (ImageButton) findViewById(R.id.imgbtn_aob_auth_form15g_15h_view);
        imgbtn_aob_auth_form15g_15h_view.setOnClickListener(this);
        imgbtn_aob_auth_form15g_15h_camera = (ImageButton) findViewById(R.id.imgbtn_aob_auth_form15g_15h_camera);
        imgbtn_aob_auth_form15g_15h_camera.setOnClickListener(this);
        imgbtn_aob_auth_form15g_15h_browse = (ImageButton) findViewById(R.id.imgbtn_aob_auth_form15g_15h_browse);
        imgbtn_aob_auth_form15g_15h_browse.setOnClickListener(this);
        imgbtn_aob_auth_form15g_15h_upload = (ImageButton) findViewById(R.id.imgbtn_aob_auth_form15g_15h_upload);
        imgbtn_aob_auth_form15g_15h_upload.setOnClickListener(this);

        imgbtn_aob_auth_form1b_view = (ImageButton) findViewById(R.id.imgbtn_aob_auth_form1b_view);
        imgbtn_aob_auth_form1b_view.setOnClickListener(this);
        imgbtn_aob_auth_form1b_camera = (ImageButton) findViewById(R.id.imgbtn_aob_auth_form1b_camera);
        imgbtn_aob_auth_form1b_camera.setOnClickListener(this);
        imgbtn_aob_auth_form1b_browse = (ImageButton) findViewById(R.id.imgbtn_aob_auth_form1b_browse);
        imgbtn_aob_auth_form1b_browse.setOnClickListener(this);
        imgbtn_aob_auth_form1b_upload = (ImageButton) findViewById(R.id.imgbtn_aob_auth_form1b_upload);
        imgbtn_aob_auth_form1b_upload.setOnClickListener(this);

        imgbtn_aob_auth_form1c_view = (ImageButton) findViewById(R.id.imgbtn_aob_auth_form1c_view);
        imgbtn_aob_auth_form1c_view.setOnClickListener(this);
        imgbtn_aob_auth_form1c_camera = (ImageButton) findViewById(R.id.imgbtn_aob_auth_form1c_camera);
        imgbtn_aob_auth_form1c_camera.setOnClickListener(this);
        imgbtn_aob_auth_form1c_browse = (ImageButton) findViewById(R.id.imgbtn_aob_auth_form1c_browse);
        imgbtn_aob_auth_form1c_browse.setOnClickListener(this);
        imgbtn_aob_auth_form1c_upload = (ImageButton) findViewById(R.id.imgbtn_aob_auth_form1c_upload);
        imgbtn_aob_auth_form1c_upload.setOnClickListener(this);

        imgbtn_aob_applicant_sign_camera = (ImageButton) findViewById(R.id.imgbtn_aob_applicant_sign_camera);
        imgbtn_aob_applicant_sign_camera.setOnClickListener(this);
        imgbtn_aob_applicant_sign_browse = (ImageButton) findViewById(R.id.imgbtn_aob_applicant_sign_browse);
        imgbtn_aob_applicant_sign_browse.setOnClickListener(this);
        imgbtn_aob_applicant_sign_upload = (ImageButton) findViewById(R.id.imgbtn_aob_applicant_sign_upload);
        imgbtn_aob_applicant_sign_upload.setOnClickListener(this);

        imgbtn_aob_declaration_camera = (ImageButton) findViewById(R.id.imgbtn_aob_declaration_camera);
        imgbtn_aob_declaration_camera.setOnClickListener(this);
        imgbtn_aob_declaration_browse = (ImageButton) findViewById(R.id.imgbtn_aob_declaration_browse);
        imgbtn_aob_declaration_browse.setOnClickListener(this);
        imgbtn_aob_declaration_upload = (ImageButton) findViewById(R.id.imgbtn_aob_declaration_upload);
        imgbtn_aob_declaration_upload.setOnClickListener(this);
    }

    public void browse_docs() {
        //Intent mediaIntent = new Intent(Intent.ACTION_GET_CONTENT);
        //mediaIntent.setType("*/*");
        //mediaIntent.setType("application/pdf"); // Set MIME type as per requirement

        //startActivityForResult(mediaIntent, REQUEST_CODE_BROWSE_DOCUMENT);

        Intent mIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        mIntent.addCategory(Intent.CATEGORY_OPENABLE);
        mIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION
                | Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
        mIntent.setType("application/pdf");
        //mIntent.setType("*/*");
            /*String[] mimeType = new String[]{"application/x-binary,application/octet-stream"};
            if(mimeType.length > 0) {
                mIntent.putExtra(Intent.EXTRA_MIME_TYPES, mimeType);
            }*/
        mBrowseDocLauncher.launch(mIntent);
    }

    public void capture_document() {

        try {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            String imageFileName = str_pan_no + str_doc + ".jpg";

            mCapturedImgFile = mStorageUtils.createFileToAppSpecificDir(mContext, imageFileName);

            // Continue only if the File was successfully created
            if (mCapturedImgFile != null) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mCommonMethods.getContentUri(mContext,
                            mCapturedImgFile));
                } else {
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mCapturedImgFile));
                }
                startActivityForResult(takePictureIntent, REQUEST_CODE_CAPTURE_DOCUMENT);
            }
        } catch (Exception exp) {
            exp.printStackTrace();
            mCommonMethods.printLog("Capture : ", exp.getMessage());
            mCapturedImgFile = null;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_CAPTURE_DOCUMENT && resultCode == RESULT_OK) {

            if (mCapturedImgFile != null) {
                CompressImage.compressImage(mCapturedImgFile.getPath());

                long size = mCapturedImgFile.length();
                double kilobyte = size / 1024;

                //2 MB valiadation
                if (kilobyte < mCommonMethods.FILE_UPLOAD_RESTRICT_SIZE) {

                    String imageFileName = str_pan_no + "_" + str_doc + ".pdf";

                    SelfAttestedDocumentActivity obj = new SelfAttestedDocumentActivity();

                    if (str_doc.equals(BANK_PASSBOOK_DOC)) {
                        /*mBankPassbookFile = mCommonMethods.createScopedStorageAllFiles(mContext,
                                imageFileName, Environment.DIRECTORY_DOCUMENTS);*/
                        mBankPassbookFile = mStorageUtils.createFileToAppSpecificDir(mContext, imageFileName);

                        if (obj.createAOBDOcumentPdf(mBankPassbookFile, mCapturedImgFile, "Bank Passbook Document")) {

                            imgbtn_aob_auth_bank_pass_book_camera.setImageDrawable(getResources().getDrawable(R.drawable.checkedcamera));
                            imgbtn_aob_auth_bank_pass_book_browse.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_browsedoc));

                            str_bank_passbook_type = "CAMERA";

                            //remove common source file from directory
                            mCapturedImgFile.delete();
                            enableUploadButton(BANK_PASSBOOK_DOC);

                        } else {
                            mCommonMethods.showMessageDialog(mContext, "File Not Found..");
                        }
                    } else if (str_doc.equals(AGE_PROOF_DOC)) {
                        /*mAgePrrofFile = mCommonMethods.createScopedStorageAllFiles(mContext,
                                imageFileName, Environment.DIRECTORY_DOCUMENTS);*/
                        mAgeProofFile = mStorageUtils.createFileToAppSpecificDir(mContext, imageFileName);

                        if (obj.createAOBDOcumentPdf(mAgeProofFile, mCapturedImgFile, "Age Proof Document")) {

                            imgbtn_aob_auth_age_proof_camera.setImageDrawable(getResources().getDrawable(R.drawable.checkedcamera));
                            imgbtn_aob_auth_age_proof_browse.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_browsedoc));

                            str_age_proof_type = "CAMERA";

                            //remove common source file from directory
                            mCapturedImgFile.delete();
                            enableUploadButton(AGE_PROOF_DOC);
                        } else {
                            mCommonMethods.showToast(mContext, "File Not Found");
                        }
                    } else if (str_doc.equals(BASIC_QUALIFICATION_DOC)) {
                        /*mBasicQualificationFile = mCommonMethods.createScopedStorageAllFiles(mContext,
                                imageFileName, Environment.DIRECTORY_DOCUMENTS);*/

                        mBasicQualificationFile = mStorageUtils.createFileToAppSpecificDir(mContext, imageFileName);

                        if (obj.createAOBDOcumentPdf(mBasicQualificationFile, mCapturedImgFile, "Basic Qualification Document")) {

                            imgbtn_aob_auth_basic_qualification_camera.setImageDrawable(getResources().getDrawable(R.drawable.checkedcamera));
                            imgbtn_aob_auth_basic_qualification_browse.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_browsedoc));

                            str_basic_qualification_type = "CAMERA";

                            //remove common source file from directory
                            mCapturedImgFile.delete();
                            enableUploadButton(BASIC_QUALIFICATION_DOC);
                        } else {
                            mCommonMethods.showToast(mContext, "File Not Found");
                        }
                    } else if (str_doc.equals(HIGH_QUALIFICATION_DOC)) {
                        /*mHighQualificationFile = mCommonMethods.createScopedStorageAllFiles(mContext,
                                imageFileName, Environment.DIRECTORY_DOCUMENTS);*/
                        mHighQualificationFile = mStorageUtils.createFileToAppSpecificDir(mContext, imageFileName);

                        if (obj.createAOBDOcumentPdf(mHighQualificationFile, mCapturedImgFile, "Higher Qualification Document")) {

                            imgbtn_aob_auth_high_qualification_camera.setImageDrawable(getResources().getDrawable(R.drawable.checkedcamera));
                            imgbtn_aob_auth_high_qualification_browse.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_browsedoc));

                            str_high_qualification_type = "CAMERA";

                            //remove common source file from directory
                            mCapturedImgFile.delete();
                            enableUploadButton(HIGH_QUALIFICATION_DOC);
                        } else {
                            mCommonMethods.showToast(mContext, "File Not Found");
                        }
                    } else if (str_doc.equals(COMM_ADDRESS_DOC)) {
                        //mCommAddressFile = new File(folder.getPath() + File.separator + imageFileName);
                        mCommAddressFile = mStorageUtils.createFileToAppSpecificDir(mContext, imageFileName);
                        if (obj.createAOBDOcumentPdf(mCommAddressFile, mCapturedImgFile, "Communication Address Document")) {

                            imgbtn_aob_auth_comm_add_camera.setImageDrawable(getResources().getDrawable(R.drawable.checkedcamera));
                            imgbtn_aob_auth_comm_add_browse.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_browsedoc));

                            str_comm_address_type = "CAMERA";

                            //remove common source file from directory
                            mCapturedImgFile.delete();
                            enableUploadButton(COMM_ADDRESS_DOC);
                        } else {
                            mCommonMethods.showToast(mContext, "File Not Found");
                        }
                    } else if (str_doc.equals(PERMANENT_ADDRESS_DOC)) {
                        //mPermanentAddFile = new File(folder.getPath() + File.separator + imageFileName);
                        mPermanentAddFile = mStorageUtils.createFileToAppSpecificDir(mContext, imageFileName);
                        if (obj.createAOBDOcumentPdf(mPermanentAddFile, mCapturedImgFile, "Permenent Address Document")) {

                            imgbtn_aob_auth_permanent_add_camera.setImageDrawable(getResources().getDrawable(R.drawable.checkedcamera));
                            imgbtn_aob_auth_permanent_add_browse.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_browsedoc));

                            str_permanent_address_type = "CAMERA";

                            //remove common source file from directory
                            mCapturedImgFile.delete();
                            enableUploadButton(PERMANENT_ADDRESS_DOC);
                        } else {
                            mCommonMethods.showToast(mContext, "File Not Found");
                        }
                    } else if (str_doc.equals(FORM_15G_15H_DOC)) {
                        /*mForm15G_15HFile = mCommonMethods.createScopedStorageAllFiles(mContext,
                                imageFileName, Environment.DIRECTORY_DOCUMENTS);*/
                        mForm15G_15HFile = mStorageUtils.createFileToAppSpecificDir(mContext, imageFileName);

                        if (obj.createAOBDOcumentPdf(mForm15G_15HFile, mCapturedImgFile, "Form 15G 15H Document")) {

                            imgbtn_aob_auth_form15g_15h_camera.setImageDrawable(getResources().getDrawable(R.drawable.checkedcamera));
                            imgbtn_aob_auth_form15g_15h_browse.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_browsedoc));

                            str_form15g_15h_type = "CAMERA";

                            //remove common source file from directory
                            mCapturedImgFile.delete();
                            enableUploadButton(FORM_15G_15H_DOC);
                        } else {
                            mCommonMethods.showToast(mContext, "File Not Found");
                        }
                    } else if (str_doc.equals(OTHERS_DOC)) {
                        /*mOthersFile = mCommonMethods.createScopedStorageAllFiles(mContext,
                                imageFileName, Environment.DIRECTORY_DOCUMENTS);*/
                        mOthersFile = mStorageUtils.createFileToAppSpecificDir(mContext, imageFileName);

                        if (obj.createAOBDOcumentPdf(mOthersFile, mCapturedImgFile, "Others Document")) {

                            imgbtn_aob_auth_others_camera.setImageDrawable(getResources().getDrawable(R.drawable.checkedcamera));
                            imgbtn_aob_auth_others_browse.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_browsedoc));

                            str_others_doc_type = "CAMERA";

                            //remove common source file from directory
                            mCapturedImgFile.delete();
                            enableUploadButton(OTHERS_DOC);
                        } else {
                            mCommonMethods.showToast(mContext, "File Not Found");
                        }
                    } else if (str_doc.equals(FORM_1B_DOC)) {
                        /*mForm1BFile = mCommonMethods.createScopedStorageAllFiles(mContext,
                                imageFileName, Environment.DIRECTORY_DOCUMENTS);*/
                        mForm1BFile = mStorageUtils.createFileToAppSpecificDir(mContext, imageFileName);

                        if (obj.createAOBDOcumentPdf(mForm1BFile, mCapturedImgFile, "Form 1B Document")) {

                            imgbtn_aob_auth_form1b_camera.setImageDrawable(getResources().getDrawable(R.drawable.checkedcamera));
                            imgbtn_aob_auth_form1b_browse.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_browsedoc));

                            str_form1B_type = "CAMERA";

                            //remove common source file from directory
                            mCapturedImgFile.delete();
                            enableUploadButton(FORM_1B_DOC);
                        } else {
                            mCommonMethods.showToast(mContext, "File Not Found");
                        }

                    } else if (str_doc.equals(FORM_1C_DOC)) {
                        /*mForm1CFile = mCommonMethods.createScopedStorageAllFiles(mContext,
                                imageFileName, Environment.DIRECTORY_DOCUMENTS);*/
                        mForm1CFile = mStorageUtils.createFileToAppSpecificDir(mContext, imageFileName);

                        if (obj.createAOBDOcumentPdf(mForm1CFile, mCapturedImgFile, "Form 1C Document")) {

                            imgbtn_aob_auth_form1c_camera.setImageDrawable(getResources().getDrawable(R.drawable.checkedcamera));
                            imgbtn_aob_auth_form1c_browse.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_browsedoc));

                            str_form1C_type = "CAMERA";

                            //remove common source file from directory
                            mCapturedImgFile.delete();
                            enableUploadButton(FORM_1C_DOC);
                        } else {
                            mCommonMethods.showToast(mContext, "File Not Found");
                        }
                    } else if (str_doc.equals(DECLARATION)) {

                        /*mDeclarationFile = mCommonMethods.createScopedStorageAllFiles(mContext,
                                imageFileName, Environment.DIRECTORY_DOCUMENTS);*/
                        mDeclarationFile = mStorageUtils.createFileToAppSpecificDir(mContext, imageFileName);

                        if (obj.createAOBDOcumentPdf(mDeclarationFile, mCapturedImgFile, "Declaration Document")) {

                            imgbtn_aob_declaration_camera.setImageDrawable(getResources().getDrawable(R.drawable.checkedcamera));
                            imgbtn_aob_declaration_browse.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_browsedoc));

                            str_declaration_type = "CAMERA";

                            //remove common source file from directory
                            mCapturedImgFile.delete();
                            enableUploadButton(DECLARATION);
                        } else {
                            mCommonMethods.showToast(mContext, "File Not Found");
                        }
                    } else if (str_doc.equals(IA_UPGRADE_TCC_DOC)) {

                        /*mIAUpgradeTCC = mCommonMethods.createScopedStorageAllFiles(mContext,
                                imageFileName, Environment.DIRECTORY_DOCUMENTS);*/
                        mIAUpgradeTCC = mStorageUtils.createFileToAppSpecificDir(mContext, imageFileName);

                        if (obj.createAOBDOcumentPdf(mIAUpgradeTCC, mCapturedImgFile, "TCC Document")) {

                            imgbtn_ia_upgrade_tcc_camera.setImageDrawable(getResources().getDrawable(R.drawable.checkedcamera));
                            imgbtn_ia_upgrade_tcc_browse.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_browsedoc));

                            str_ia_upgrade_tcc_type = "CAMERA";

                            //remove common source file from directory
                            mCapturedImgFile.delete();
                            enableUploadButton(IA_UPGRADE_TCC_DOC);
                        } else {
                            mCommonMethods.showToast(mContext, "File Not Found");
                        }
                    } else if (str_doc.equals(IA_UPGRADE_SCORE_CARD_DOC)) {

                        /*mIAUpgradeScoreCard = mCommonMethods.createScopedStorageAllFiles(mContext,
                                imageFileName, Environment.DIRECTORY_DOCUMENTS);*/
                        mIAUpgradeScoreCard = mStorageUtils.createFileToAppSpecificDir(mContext, imageFileName);

                        if (obj.createAOBDOcumentPdf(mIAUpgradeScoreCard, mCapturedImgFile, "Score Card Document")) {

                            imgbtn_ia_upgrade_score_card_camera.setImageDrawable(getResources().getDrawable(R.drawable.checkedcamera));
                            imgbtn_ia_upgrade_score_card_browse.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_browsedoc));

                            str_ia_upgrade_score_card_type = "CAMERA";

                            //remove common source file from directory
                            mCapturedImgFile.delete();
                            enableUploadButton(IA_UPGRADE_SCORE_CARD_DOC);
                        } else {
                            mCommonMethods.showToast(mContext, "File Not Found");
                        }

                    } else if (str_doc.equals(IA_UPGRADE_ULIP_CARD_DOC)) {

                        /*mIAUpgradeUlip = mCommonMethods.createScopedStorageAllFiles(mContext,
                                imageFileName, Environment.DIRECTORY_DOCUMENTS);*/

                        mIAUpgradeUlip = mStorageUtils.createFileToAppSpecificDir(mContext, imageFileName);

                        if (obj.createAOBDOcumentPdf(mIAUpgradeUlip, mCapturedImgFile, "ULIP Document")) {

                            imgbtn_ia_upgrade_ulip_camera.setImageDrawable(getResources().getDrawable(R.drawable.checkedcamera));
                            imgbtn_ia_upgrade_ulip_browse.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_browsedoc));

                            str_ia_upgrade_ulip_type = "CAMERA";

                            //remove common source file from directory
                            mCapturedImgFile.delete();
                            enableUploadButton(IA_UPGRADE_ULIP_CARD_DOC);
                        } else {
                            mCommonMethods.showToast(mContext, "File Not Found");
                        }

                    } else if (str_doc.equals(IA_UPGRADE_FORM_IA)) {

                        /*mIAUpgradeFormIA = mCommonMethods.createScopedStorageAllFiles(mContext,
                                imageFileName, Environment.DIRECTORY_DOCUMENTS);*/
                        mIAUpgradeFormIA = mStorageUtils.createFileToAppSpecificDir(mContext, imageFileName);

                        if (obj.createAOBDOcumentPdf(mIAUpgradeFormIA, mCapturedImgFile, "Form IA Document")) {

                            imgbtn_ia_upgrade_form_ia_camera.setImageDrawable(getResources().getDrawable(R.drawable.checkedcamera));
                            imgbtn_ia_upgrade_form_ia_browse.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_browsedoc));

                            str_ia_upgrade_form_ia_type = "CAMERA";

                            //remove common source file from directory
                            mCapturedImgFile.delete();
                            enableUploadButton(IA_UPGRADE_FORM_IA);
                        } else {
                            mCommonMethods.showToast(mContext, "File Not Found");
                        }

                    } else if (str_doc.equals(IA_UPGRADE_MOBILE_DECLARATION)) {

                        /*mIAUpgradeMobDeclaration = mCommonMethods.createScopedStorageAllFiles(mContext,
                                imageFileName, Environment.DIRECTORY_DOCUMENTS);*/
                        mIAUpgradeMobDeclaration = mStorageUtils.createFileToAppSpecificDir(mContext, imageFileName);

                        if (obj.createAOBDOcumentPdf(mIAUpgradeMobDeclaration, mCapturedImgFile, "Mobile Declaration Document")) {

                            imgbtn_ia_upgrade_mob_declaration_camera.setImageDrawable(getResources().getDrawable(R.drawable.checkedcamera));
                            imgbtn_ia_upgrade_mob_declaration_browse.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_browsedoc));

                            str_ia_upgrade_mob_declaration_type = "CAMERA";

                            //remove common source file from directory
                            mCapturedImgFile.delete();
                            enableUploadButton(IA_UPGRADE_MOBILE_DECLARATION);
                        } else {
                            mCommonMethods.showToast(mContext, "File Not Found");
                        }
                    }

                } else {
                    mCommonMethods.showMessageDialog(mContext, mCommonMethods.FILE_UPLOAD_RESTRICT_SIZE_MSG);
                }
            } else {
                mCommonMethods.showMessageDialog(mContext, "File Null Error..");
            }
        }
        /*else if (requestCode == REQUEST_OCR) {
            if (resultCode == RESULT_OK) {
                final File imagePath;
                final String DocumentType;
                Bundle bundle = data.getExtras();

                if (bundle != null) {
                    String jsonData = (String) bundle.get("jsonData");

                    try {
                        JSONObject object = new JSONObject(jsonData);

                        DocumentType = object.get("DocumentType").toString();

                        imagePath = new File(bundle.get("BitmapImageUri").toString());
                        //Bitmap edgeBitmap = BitmapFactory.decodeFile(imagePath.getPath());

                        if (imagePath != null) {

                            //mCommonMethods.showToast(mContext, "Document - " + DocumentType);

                            CompressImage.compressImage(imagePath.getPath());

                            long size = imagePath.length();
                            double kilobyte = size / 1024;

                            //2 MB valiadation
                            if (kilobyte < mCommonMethods.FILE_UPLOAD_RESTRICT_SIZE) {

                                File folder = new File(CommonMethods.EXTERNAL_STORAGE_DIRECTORY + CommonMethods.DIRECT_DIRECTORY);

                                if (!folder.exists()) {
                                    folder.mkdirs();
                                }

                                String imageFileName = str_pan_no + "_" + str_doc + ".pdf";

                                SelfAttestedDocumentActivity obj = new SelfAttestedDocumentActivity();

                                if (str_doc.equals(BANK_PASSBOOK_DOC)) {
                                    mBankPassbookFile = new File(folder.getPath() + File.separator + imageFileName);

                                    if (obj.createAOBDOcumentPdf(mBankPassbookFile, imagePath, "Bank Passbook Document")) {
                                        if (OCR_TYPE.equals("CAMERA")) {
                                            imgbtn_aob_auth_bank_pass_book_camera.setImageDrawable(getResources().getDrawable(R.drawable.checkedcamera));
                                            imgbtn_aob_auth_bank_pass_book_browse.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_browsedoc));
                                        } else if (OCR_TYPE.equals("BROWSE")) {
                                            imgbtn_aob_auth_bank_pass_book_camera.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_camera));
                                            imgbtn_aob_auth_bank_pass_book_browse.setImageDrawable(getResources().getDrawable(R.drawable.checkedbrowse));
                                        }

                                        str_bank_passbook_type = OCR_TYPE;

                                        //remove common source file from directory
                                        //mSourceFile.delete();
                                        enableUploadButton(BANK_PASSBOOK_DOC);

                                    } else {
                                        mCommonMethods.showMessageDialog(mContext, "File Not Found..");
                                    }
                                } else if (str_doc.equals(AGE_PROOF_DOC)) {
                                    mAgePrrofFile = new File(folder.getPath() + File.separator + imageFileName);
                                    if (obj.createAOBDOcumentPdf(mAgePrrofFile, imagePath, "Age Proof Document")) {
                                        if (OCR_TYPE.equals("CAMERA")) {
                                            imgbtn_aob_auth_age_proof_camera.setImageDrawable(getResources().getDrawable(R.drawable.checkedcamera));
                                            imgbtn_aob_auth_age_proof_browse.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_browsedoc));
                                        } else if (OCR_TYPE.equals("BROWSE")) {
                                            imgbtn_aob_auth_age_proof_camera.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_camera));
                                            imgbtn_aob_auth_age_proof_browse.setImageDrawable(getResources().getDrawable(R.drawable.checkedbrowse));
                                        }

                                        str_age_proof_type = OCR_TYPE;

                                        //remove common source file from directory
                                        //mSourceFile.delete();
                                        enableUploadButton(AGE_PROOF_DOC);
                                    } else {
                                        mCommonMethods.showToast(mContext, "File Not Found");
                                    }
                                } else if (str_doc.equals(BASIC_QUALIFICATION_DOC)) {
                                    mBasicQualificationFile = new File(folder.getPath() + File.separator + imageFileName);
                                    if (obj.createAOBDOcumentPdf(mBasicQualificationFile, imagePath, "Basic Qualification Document")) {
                                        if (OCR_TYPE.equals("CAMERA")) {
                                            imgbtn_aob_auth_basic_qualification_camera.setImageDrawable(getResources().getDrawable(R.drawable.checkedcamera));
                                            imgbtn_aob_auth_basic_qualification_browse.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_browsedoc));
                                        } else if (OCR_TYPE.equals("BROWSE")) {
                                            imgbtn_aob_auth_basic_qualification_camera.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_camera));
                                            imgbtn_aob_auth_basic_qualification_browse.setImageDrawable(getResources().getDrawable(R.drawable.checkedbrowse));
                                        }

                                        str_basic_qualification_type = OCR_TYPE;

                                        //remove common source file from directory
                                        //mSourceFile.delete();
                                        enableUploadButton(BASIC_QUALIFICATION_DOC);
                                    } else {
                                        mCommonMethods.showToast(mContext, "File Not Found");
                                    }
                                } else if (str_doc.equals(HIGH_QUALIFICATION_DOC)) {
                                    mHighQualificationFile = new File(folder.getPath() + File.separator + imageFileName);
                                    if (obj.createAOBDOcumentPdf(mHighQualificationFile, imagePath, "Higher Qualification Document")) {
                                        if (OCR_TYPE.equals("CAMERA")) {
                                            imgbtn_aob_auth_high_qualification_camera.setImageDrawable(getResources().getDrawable(R.drawable.checkedcamera));
                                            imgbtn_aob_auth_high_qualification_browse.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_browsedoc));
                                        } else if (OCR_TYPE.equals("BROWSE")) {
                                            imgbtn_aob_auth_high_qualification_camera.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_camera));
                                            imgbtn_aob_auth_high_qualification_browse.setImageDrawable(getResources().getDrawable(R.drawable.checkedbrowse));
                                        }

                                        str_high_qualification_type = OCR_TYPE;

                                        //remove common source file from directory
                                        //mSourceFile.delete();
                                        enableUploadButton(HIGH_QUALIFICATION_DOC);
                                    } else {
                                        mCommonMethods.showToast(mContext, "File Not Found");
                                    }
                                } else if (str_doc.equals(COMM_ADDRESS_DOC)) {
                                    mCommAddressFile = new File(folder.getPath() + File.separator + imageFileName);
                                    if (obj.createAOBDOcumentPdf(mCommAddressFile, imagePath, "Communication Address Document")) {
                                        if (OCR_TYPE.equals("CAMERA")) {
                                            imgbtn_aob_auth_comm_add_camera.setImageDrawable(getResources().getDrawable(R.drawable.checkedcamera));
                                            imgbtn_aob_auth_comm_add_browse.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_browsedoc));
                                        } else if (OCR_TYPE.equals("BROWSE")) {
                                            imgbtn_aob_auth_comm_add_camera.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_camera));
                                            imgbtn_aob_auth_comm_add_browse.setImageDrawable(getResources().getDrawable(R.drawable.checkedbrowse));
                                        }

                                        str_comm_address_type = OCR_TYPE;

                                        //remove common source file from directory
                                        //mSourceFile.delete();
                                        enableUploadButton(COMM_ADDRESS_DOC);
                                    } else {
                                        mCommonMethods.showToast(mContext, "File Not Found");
                                    }
                                } else if (str_doc.equals(PERMANENT_ADDRESS_DOC)) {
                                    mPermanentAddFile = new File(folder.getPath() + File.separator + imageFileName);
                                    if (obj.createAOBDOcumentPdf(mPermanentAddFile, imagePath, "Permenent Address Document")) {
                                        if (OCR_TYPE.equals("CAMERA")) {
                                            imgbtn_aob_auth_permanent_add_camera.setImageDrawable(getResources().getDrawable(R.drawable.checkedcamera));
                                            imgbtn_aob_auth_permanent_add_browse.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_browsedoc));
                                        } else if (OCR_TYPE.equals("BROWSE")) {
                                            imgbtn_aob_auth_permanent_add_camera.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_camera));
                                            imgbtn_aob_auth_permanent_add_browse.setImageDrawable(getResources().getDrawable(R.drawable.checkedbrowse));
                                        }

                                        str_permanent_address_type = OCR_TYPE;

                                        //remove common source file from directory
                                        //mSourceFile.delete();
                                        enableUploadButton(PERMANENT_ADDRESS_DOC);
                                    } else {
                                        mCommonMethods.showToast(mContext, "File Not Found");
                                    }
                                } else if (str_doc.equals(FORM_15G_15H_DOC)) {
                                    mForm15G_15HFile = new File(folder.getPath() + File.separator + imageFileName);
                                    if (obj.createAOBDOcumentPdf(mForm15G_15HFile, imagePath, "Form 15G 15H Document")) {
                                        if (OCR_TYPE.equals("CAMERA")) {
                                            imgbtn_aob_auth_form15g_15h_camera.setImageDrawable(getResources().getDrawable(R.drawable.checkedcamera));
                                            imgbtn_aob_auth_form15g_15h_browse.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_browsedoc));
                                        } else if (OCR_TYPE.equals("BROWSE")) {
                                            imgbtn_aob_auth_form15g_15h_camera.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_camera));
                                            imgbtn_aob_auth_form15g_15h_browse.setImageDrawable(getResources().getDrawable(R.drawable.checkedbrowse));
                                        }

                                        str_form15g_15h_type = OCR_TYPE;

                                        //remove common source file from directory
                                        //mSourceFile.delete();
                                        enableUploadButton(FORM_15G_15H_DOC);
                                    } else {
                                        mCommonMethods.showToast(mContext, "File Not Found");
                                    }
                                } else if (str_doc.equals(OTHERS_DOC)) {
                                    mOthersFile = new File(folder.getPath() + File.separator + imageFileName);

                                    if (obj.createAOBDOcumentPdf(mOthersFile, imagePath, "Others Document")) {

                                        if (OCR_TYPE.equals("CAMERA")) {
                                            imgbtn_aob_auth_others_camera.setImageDrawable(getResources().getDrawable(R.drawable.checkedcamera));
                                            imgbtn_aob_auth_others_browse.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_browsedoc));
                                        } else if (OCR_TYPE.equals("BROWSE")) {
                                            imgbtn_aob_auth_others_camera.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_camera));
                                            imgbtn_aob_auth_others_browse.setImageDrawable(getResources().getDrawable(R.drawable.checkedbrowse));
                                        }

                                        str_others_doc_type = OCR_TYPE;

                                        //remove common source file from directory
                                        //mSourceFile.delete();
                                        enableUploadButton(OTHERS_DOC);
                                    } else {
                                        mCommonMethods.showToast(mContext, "File Not Found");
                                    }
                                } else if (str_doc.equals(FORM_1B_DOC)) {
                                    mForm1BFile = new File(folder.getPath() + File.separator + imageFileName);
                                    if (obj.createAOBDOcumentPdf(mForm1BFile, imagePath, "Form 1B Document")) {
                                        if (OCR_TYPE.equals("CAMERA")) {
                                            imgbtn_aob_auth_form1b_camera.setImageDrawable(getResources().getDrawable(R.drawable.checkedcamera));
                                            imgbtn_aob_auth_form1b_browse.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_browsedoc));
                                        } else if (OCR_TYPE.equals("BROWSE")) {
                                            imgbtn_aob_auth_form1b_camera.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_camera));
                                            imgbtn_aob_auth_form1b_browse.setImageDrawable(getResources().getDrawable(R.drawable.checkedbrowse));
                                        }

                                        str_form1B_type = OCR_TYPE;

                                        //remove common source file from directory
                                        //mSourceFile.delete();
                                        enableUploadButton(FORM_1B_DOC);
                                    } else {
                                        mCommonMethods.showToast(mContext, "File Not Found");
                                    }

                                } else if (str_doc.equals(FORM_1C_DOC)) {
                                    mForm1CFile = new File(folder.getPath() + File.separator + imageFileName);
                                    if (obj.createAOBDOcumentPdf(mForm1CFile, imagePath, "Form 1C Document")) {
                                        if (OCR_TYPE.equals("CAMERA")) {
                                            imgbtn_aob_auth_form1c_camera.setImageDrawable(getResources().getDrawable(R.drawable.checkedcamera));
                                            imgbtn_aob_auth_form1c_browse.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_browsedoc));
                                        } else if (OCR_TYPE.equals("BROWSE")) {
                                            imgbtn_aob_auth_form1c_camera.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_camera));
                                            imgbtn_aob_auth_form1c_browse.setImageDrawable(getResources().getDrawable(R.drawable.checkedbrowse));
                                        }

                                        str_form1C_type = OCR_TYPE;

                                        //remove common source file from directory
                                        //mSourceFile.delete();
                                        enableUploadButton(FORM_1C_DOC);
                                    } else {
                                        mCommonMethods.showToast(mContext, "File Not Found");
                                    }
                                } else if (str_doc.equals(DECLARATION)) {

                                    mDeclarationFile = new File(folder.getPath() + File.separator + imageFileName);
                                    if (obj.createAOBDOcumentPdf(mDeclarationFile, imagePath, "Declaration Document")) {
                                        if (OCR_TYPE.equals("CAMERA")) {
                                            imgbtn_aob_declaration_camera.setImageDrawable(getResources().getDrawable(R.drawable.checkedcamera));
                                            imgbtn_aob_declaration_browse.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_browsedoc));
                                        } else if (OCR_TYPE.equals("BROWSE")) {
                                            imgbtn_aob_declaration_camera.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_camera));
                                            imgbtn_aob_declaration_browse.setImageDrawable(getResources().getDrawable(R.drawable.checkedbrowse));
                                        }

                                        str_declaration_type = OCR_TYPE;

                                        //remove common source file from directory
                                        //mSourceFile.delete();
                                        enableUploadButton(DECLARATION);
                                    } else {
                                        mCommonMethods.showToast(mContext, "File Not Found");
                                    }
                                } else if (str_doc.equals(IA_UPGRADE_TCC_DOC)) {

                                    mIAUpgradeTCC = new File(folder.getPath() + File.separator + imageFileName);
                                    if (obj.createAOBDOcumentPdf(mIAUpgradeTCC, imagePath, "TCC Document")) {
                                        if (OCR_TYPE.equals("CAMERA")) {
                                            imgbtn_ia_upgrade_tcc_camera.setImageDrawable(getResources().getDrawable(R.drawable.checkedcamera));
                                            imgbtn_ia_upgrade_tcc_browse.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_browsedoc));
                                        } else if (OCR_TYPE.equals("BROWSE")) {
                                            imgbtn_ia_upgrade_tcc_camera.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_camera));
                                            imgbtn_ia_upgrade_tcc_browse.setImageDrawable(getResources().getDrawable(R.drawable.checkedbrowse));
                                        }

                                        str_ia_upgrade_tcc_type = OCR_TYPE;

                                        //remove common source file from directory
                                        //mSourceFile.delete();
                                        enableUploadButton(IA_UPGRADE_TCC_DOC);
                                    } else {
                                        mCommonMethods.showToast(mContext, "File Not Found");
                                    }
                                } else if (str_doc.equals(IA_UPGRADE_SCORE_CARD_DOC)) {

                                    mIAUpgradeScoreCard = new File(folder.getPath() + File.separator + imageFileName);
                                    if (obj.createAOBDOcumentPdf(mIAUpgradeScoreCard, imagePath, "Score Card Document")) {
                                        if (OCR_TYPE.equals("CAMERA")) {
                                            imgbtn_ia_upgrade_score_card_camera.setImageDrawable(getResources().getDrawable(R.drawable.checkedcamera));
                                            imgbtn_ia_upgrade_score_card_browse.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_browsedoc));
                                        } else if (OCR_TYPE.equals("BROWSE")) {
                                            imgbtn_ia_upgrade_score_card_camera.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_camera));
                                            imgbtn_ia_upgrade_score_card_browse.setImageDrawable(getResources().getDrawable(R.drawable.checkedbrowse));
                                        }

                                        str_ia_upgrade_score_card_type = OCR_TYPE;

                                        //remove common source file from directory
                                        //mSourceFile.delete();
                                        enableUploadButton(IA_UPGRADE_SCORE_CARD_DOC);
                                    } else {
                                        mCommonMethods.showToast(mContext, "File Not Found");
                                    }

                                } else if (str_doc.equals(IA_UPGRADE_ULIP_CARD_DOC)) {

                                    mIAUpgradeUlip = new File(folder.getPath() + File.separator + imageFileName);
                                    if (obj.createAOBDOcumentPdf(mIAUpgradeUlip, imagePath, "ULIP Document")) {
                                        if (OCR_TYPE.equals("CAMERA")) {
                                            imgbtn_ia_upgrade_ulip_camera.setImageDrawable(getResources().getDrawable(R.drawable.checkedcamera));
                                            imgbtn_ia_upgrade_ulip_browse.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_browsedoc));
                                        } else if (OCR_TYPE.equals("BROWSE")) {
                                            imgbtn_ia_upgrade_ulip_camera.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_camera));
                                            imgbtn_ia_upgrade_ulip_browse.setImageDrawable(getResources().getDrawable(R.drawable.checkedbrowse));
                                        }

                                        str_ia_upgrade_ulip_type = OCR_TYPE;

                                        //remove common source file from directory
                                        //mSourceFile.delete();
                                        enableUploadButton(IA_UPGRADE_ULIP_CARD_DOC);
                                    } else {
                                        mCommonMethods.showToast(mContext, "File Not Found");
                                    }

                                } else if (str_doc.equals(IA_UPGRADE_FORM_IA)) {

                                    mIAUpgradeFormIA = new File(folder.getPath() + File.separator + imageFileName);
                                    if (obj.createAOBDOcumentPdf(mIAUpgradeFormIA, imagePath, "Form IA Document")) {
                                        if (OCR_TYPE.equals("CAMERA")) {
                                            imgbtn_ia_upgrade_form_ia_camera.setImageDrawable(getResources().getDrawable(R.drawable.checkedcamera));
                                            imgbtn_ia_upgrade_form_ia_browse.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_browsedoc));
                                        } else if (OCR_TYPE.equals("BROWSE")) {
                                            imgbtn_ia_upgrade_form_ia_camera.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_camera));
                                            imgbtn_ia_upgrade_form_ia_browse.setImageDrawable(getResources().getDrawable(R.drawable.checkedbrowse));
                                        }

                                        str_ia_upgrade_form_ia_type = OCR_TYPE;

                                        //remove common source file from directory
                                        //mSourceFile.delete();
                                        enableUploadButton(IA_UPGRADE_FORM_IA);
                                    } else {
                                        mCommonMethods.showToast(mContext, "File Not Found");
                                    }

                                } else if (str_doc.equals(IA_UPGRADE_MOBILE_DECLARATION)) {

                                    mIAUpgradeMobDeclaration = new File(folder.getPath() + File.separator + imageFileName);
                                    if (obj.createAOBDOcumentPdf(mIAUpgradeMobDeclaration, imagePath, "Mobile Declaration Document")) {
                                        if (OCR_TYPE.equals("CAMERA")) {
                                            imgbtn_ia_upgrade_mob_declaration_camera.setImageDrawable(getResources().getDrawable(R.drawable.checkedcamera));
                                            imgbtn_ia_upgrade_mob_declaration_browse.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_browsedoc));
                                        } else if (OCR_TYPE.equals("BROWSE")) {
                                            imgbtn_ia_upgrade_mob_declaration_camera.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_camera));
                                            imgbtn_ia_upgrade_mob_declaration_browse.setImageDrawable(getResources().getDrawable(R.drawable.checkedbrowse));
                                        }

                                        str_ia_upgrade_mob_declaration_type = OCR_TYPE;

                                        //remove common source file from directory
                                        //mSourceFile.delete();
                                        enableUploadButton(IA_UPGRADE_MOBILE_DECLARATION);
                                    } else {
                                        mCommonMethods.showToast(mContext, "File Not Found");
                                    }
                                }
                            } else {
                                mCommonMethods.showMessageDialog(mContext, mCommonMethods.FILE_UPLOAD_RESTRICT_SIZE_MSG);
                            }

                            if (imagePath.exists()) {
                                imagePath.delete();
                            }
                        } else {
                            mCommonMethods.showMessageDialog(mContext, "File Size is too small");
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        mCommonMethods.showToast(mContext, e.getMessage());
                    }
                }

            } else {
                Toast.makeText(mContext, "Data not receive", Toast.LENGTH_SHORT).show();
            }
        }*/
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btn_aob_auth_pan_back:
                is_back_preesed = true;
                onBackPressed();
                break;

            case R.id.imgbtn_aob_auth_bank_pass_book_view:

                if (mBankPassbookFile != null) {
                    try {
                        mCommonMethods.openAllDocs(mContext, mBankPassbookFile);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                } else {
                    mCommonMethods.showToast(mContext, "Please Browse or Capture Bank Passbook Document First!");
                }

                break;

            case R.id.imgbtn_aob_auth_bank_pass_book_camera:

                str_doc = BANK_PASSBOOK_DOC;
                capture_document();
                //capture_docs(BANK_PASSBOOK_DOC, "CAMERA");

                break;

            case R.id.imgbtn_aob_auth_bank_pass_book_browse:

                str_doc = BANK_PASSBOOK_DOC;
                browse_docs();
                //capture_docs(BANK_PASSBOOK_DOC, "BROWSE");

                break;

            case R.id.imgbtn_aob_auth_bank_pass_book_upload:

                createSoapRequestToUploadDoc(mBankPassbookFile);

                break;

            case R.id.imgbtn_aob_auth_age_proof_view:

                if (mAgeProofFile != null) {
                    try {
                        mCommonMethods.openAllDocs(mContext, mAgeProofFile);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                } else {
                    mCommonMethods.showToast(mContext, "Please Browse or Capture Age Proof Document First!");
                }

                break;

            case R.id.imgbtn_aob_auth_age_proof_camera:

                str_doc = AGE_PROOF_DOC;
                capture_document();
                //capture_docs(AGE_PROOF_DOC, "CAMERA");

                break;

            case R.id.imgbtn_aob_auth_age_proof_browse:

                str_doc = AGE_PROOF_DOC;
                browse_docs();
                //capture_docs(AGE_PROOF_DOC, "BROWSE");

                break;

            case R.id.imgbtn_aob_auth_age_proof_upload:

                createSoapRequestToUploadDoc(mAgeProofFile);

                break;

            case R.id.imgbtn_aob_auth_basic_qualification_view:

                if (mBasicQualificationFile != null) {
                    try {
                        mCommonMethods.openAllDocs(mContext, mBasicQualificationFile);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                } else {
                    mCommonMethods.showToast(mContext, "Please Browse or Capture Basic Qualification Document First!");
                }

                break;

            case R.id.imgbtn_aob_auth_basic_qualification_camera:

                str_doc = BASIC_QUALIFICATION_DOC;
                capture_document();
                //capture_docs(BASIC_QUALIFICATION_DOC, "CAMERA");

                break;
            case R.id.imgbtn_aob_auth_basic_qualification_browse:

                str_doc = BASIC_QUALIFICATION_DOC;
                browse_docs();
                //capture_docs(BASIC_QUALIFICATION_DOC, "BROWSE");

                break;

            case R.id.imgbtn_aob_auth_basic_qualification_upload:

                createSoapRequestToUploadDoc(mBasicQualificationFile);
                break;

            case R.id.imgbtn_aob_auth_high_qualification_view:

                if (mHighQualificationFile != null) {
                    try {
                        mCommonMethods.openAllDocs(mContext, mHighQualificationFile);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                } else {
                    mCommonMethods.showToast(mContext, "Please Browse or Capture Higher Qualification Document First!");
                }

                break;

            case R.id.imgbtn_aob_auth_high_qualification_camera:

                str_doc = HIGH_QUALIFICATION_DOC;
                capture_document();
                //capture_docs(HIGH_QUALIFICATION_DOC, "CAMERA");

                break;
            case R.id.imgbtn_aob_auth_high_qualification_browse:

                str_doc = HIGH_QUALIFICATION_DOC;
                browse_docs();
                //capture_docs(HIGH_QUALIFICATION_DOC, "BROWSE");

                break;

            case R.id.imgbtn_aob_auth_high_qualification_upload:

                createSoapRequestToUploadDoc(mHighQualificationFile);

                break;

            case R.id.imgbtn_aob_auth_comm_add_view:

                if (mCommAddressFile != null) {
                    try {
                        mCommonMethods.openAllDocs(mContext, mCommAddressFile);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                } else {
                    mCommonMethods.showToast(mContext, "Please Browse or Capture Communication Address Document First!");
                }

                break;

            case R.id.imgbtn_aob_auth_comm_add_camera:

                str_doc = COMM_ADDRESS_DOC;
                capture_document();
                //capture_docs(COMM_ADDRESS_DOC, "CAMERA");

                break;
            case R.id.imgbtn_aob_auth_comm_add_browse:

                str_doc = COMM_ADDRESS_DOC;
                browse_docs();
                //capture_docs(COMM_ADDRESS_DOC, "BROWSE");

                break;

            case R.id.imgbtn_aob_auth_comm_add_upload:

                createSoapRequestToUploadDoc(mCommAddressFile);

                break;

            case R.id.imgbtn_aob_auth_permanent_add_view:

                if (mPermanentAddFile != null) {
                    try {
                        mCommonMethods.openAllDocs(mContext, mPermanentAddFile);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                } else {
                    mCommonMethods.showToast(mContext, "Please Browse or Capture Permanent Address Document First!");
                }

                break;

            case R.id.imgbtn_aob_auth_permanent_add_camera:

                str_doc = PERMANENT_ADDRESS_DOC;
                capture_document();
                //capture_docs(PERMANENT_ADDRESS_DOC, "CAMERA");

                break;
            case R.id.imgbtn_aob_auth_permanent_add_browse:

                str_doc = PERMANENT_ADDRESS_DOC;
                browse_docs();
                //capture_docs(PERMANENT_ADDRESS_DOC, "BROWSE");

                break;

            case R.id.imgbtn_aob_auth_permanent_add_upload:

                createSoapRequestToUploadDoc(mPermanentAddFile);

                break;

            case R.id.imgbtn_aob_auth_form15g_15h_view:

                if (mForm15G_15HFile != null) {
                    try {
                        mCommonMethods.openAllDocs(mContext, mForm15G_15HFile);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                } else {
                    mCommonMethods.showToast(mContext, "Please Browse or Capture Form 15G-15H Document First!");
                }

                break;

            case R.id.imgbtn_aob_auth_form15g_15h_camera:

                str_doc = FORM_15G_15H_DOC;
                capture_document();
                //capture_docs(FORM_15G_15H_DOC, "CAMERA");

                break;
            case R.id.imgbtn_aob_auth_form15g_15h_browse:

                str_doc = FORM_15G_15H_DOC;
                browse_docs();
                //capture_docs(FORM_15G_15H_DOC, "BROWSE");

                break;

            case R.id.imgbtn_aob_auth_form15g_15h_upload:

                createSoapRequestToUploadDoc(mForm15G_15HFile);
                break;

            case R.id.ib_upload_all_docs_view:
                if (mOthersFile != null) {
                    try {
                        mCommonMethods.openAllDocs(mContext, mOthersFile);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                } else {
                    mCommonMethods.showToast(mContext, "Please Browse or Capture Others Document First!");
                }
                break;

            case R.id.imgbtn_aob_auth_others_camera:

                if (!edt_aob_auth_others.getText().toString().equals("")) {

                    str_doc = OTHERS_DOC;
                    capture_document();
                    //capture_docs(OTHERS_DOC, "CAMERA");
                } else {
                    mCommonMethods.showToast(mContext, "Please Enter Other Document Name");
                }

                break;
            case R.id.imgbtn_aob_auth_others_browse:

                if (!edt_aob_auth_others.getText().toString().equals("")) {
                    str_doc = OTHERS_DOC;
                    browse_docs();
                    //capture_docs(OTHERS_DOC, "BROWSE");
                } else {
                    mCommonMethods.showToast(mContext, "Please Enter Other Document Name");
                }

                break;

            case R.id.imgbtn_aob_auth_others_upload:

                createSoapRequestToUploadDoc(mOthersFile);
                break;

            case R.id.imgbtn_aob_auth_form1b_view:
                if (mForm1BFile != null) {
                    try {
                        mCommonMethods.openAllDocs(mContext, mForm1BFile);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                } else {
                    mCommonMethods.showToast(mContext, "Please Browse or Capture Form 1B Document First!");
                }
                break;

            case R.id.imgbtn_aob_auth_form1b_camera:

                str_doc = FORM_1B_DOC;
                capture_document();
                //capture_docs(FORM_1B_DOC, "CAMERA");

                break;
            case R.id.imgbtn_aob_auth_form1b_browse:

                str_doc = FORM_1B_DOC;
                browse_docs();
                //capture_docs(FORM_1B_DOC, "BROWSE");

                break;

            case R.id.imgbtn_aob_auth_form1b_upload:

                createSoapRequestToUploadDoc(mForm1BFile);
                break;

            case R.id.imgbtn_aob_auth_form1c_view:
                if (mForm1CFile != null) {
                    try {
                        mCommonMethods.openAllDocs(mContext, mForm1CFile);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                } else {
                    mCommonMethods.showToast(mContext, "Please Browse or Capture Form 1C Document First!");
                }
                break;

            case R.id.imgbtn_aob_auth_form1c_camera:

                str_doc = FORM_1C_DOC;
                capture_document();
                //capture_docs(FORM_1C_DOC, "CAMERA");

                break;
            case R.id.imgbtn_aob_auth_form1c_browse:

                str_doc = FORM_1C_DOC;
                browse_docs();
                //capture_docs(FORM_1C_DOC, "BROWSE");

                break;

            case R.id.imgbtn_aob_auth_form1c_upload:

                createSoapRequestToUploadDoc(mForm1CFile);

                break;

            case R.id.imgbtn_aob_applicant_sign_camera:

                str_doc = APPLICANT_SIGN;
                capture_document();

                //capture_docs(APPLICANT_SIGN, "CAMERA");
                break;

            case R.id.imgbtn_aob_applicant_sign_browse:
                str_doc = APPLICANT_SIGN;
                browse_docs();
                //capture_docs(APPLICANT_SIGN, "BROWSE");
                break;

            case R.id.imgbtn_aob_applicant_sign_upload:
                createSoapRequestToUploadDoc(mApplicantSignFile);
                break;

            case R.id.imgbtn_aob_declaration_upload:
                createSoapRequestToUploadDoc(mDeclarationFile);
                break;

            case R.id.imgbtn_aob_declaration_browse:
                str_doc = DECLARATION;
                browse_docs();
                //capture_docs(DECLARATION, "BROWSE");
                break;

            case R.id.imgbtn_aob_declaration_camera:
                str_doc = DECLARATION;
                capture_document();
                //capture_docs(DECLARATION, "CAMERA");
                break;

            case R.id.imgbtn_ia_upgrade_tcc_view:
                if (mIAUpgradeTCC != null) {
                    try {
                        mCommonMethods.openAllDocs(mContext, mIAUpgradeTCC);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                } else {
                    mCommonMethods.showToast(mContext, "Please Browse or Capture TCC Document First!");
                }
                break;

            case R.id.imgbtn_ia_upgrade_tcc_camera:
                str_doc = IA_UPGRADE_TCC_DOC;
                capture_document();
                //capture_docs(IA_UPGRADE_TCC_DOC, "CAMERA");
                break;

            case R.id.imgbtn_ia_upgrade_tcc_browse:
                str_doc = IA_UPGRADE_TCC_DOC;
                browse_docs();
                //capture_docs(IA_UPGRADE_TCC_DOC, "BROWSE");
                break;

            case R.id.imgbtn_ia_upgrade_tcc_upload:
                createSoapRequestToUploadDoc(mIAUpgradeTCC);
                break;

            case R.id.imgbtn_ia_upgrade_score_card_view:
                if (mIAUpgradeScoreCard != null) {
                    try {
                        mCommonMethods.openAllDocs(mContext, mIAUpgradeScoreCard);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                } else {
                    mCommonMethods.showToast(mContext, "Please Browse or Capture Score Card Document First!");
                }
                break;

            case R.id.imgbtn_ia_upgrade_score_card_camera:
                str_doc = IA_UPGRADE_SCORE_CARD_DOC;
                capture_document();
                //capture_docs(IA_UPGRADE_SCORE_CARD_DOC, "CAMERA");
                break;

            case R.id.imgbtn_ia_upgrade_score_card_browse:
                str_doc = IA_UPGRADE_SCORE_CARD_DOC;
                browse_docs();
                //capture_docs(IA_UPGRADE_SCORE_CARD_DOC, "BROWSE");
                break;

            case R.id.imgbtn_ia_upgrade_score_card_upload:
                createSoapRequestToUploadDoc(mIAUpgradeScoreCard);
                break;

            case R.id.imgbtn_ia_upgrade_ulip_view:
                if (mIAUpgradeUlip != null) {
                    try {
                        mCommonMethods.openAllDocs(mContext, mIAUpgradeUlip);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                } else {
                    mCommonMethods.showToast(mContext, "Please Browse or Capture ULIP Document First!");
                }
                break;

            case R.id.imgbtn_ia_upgrade_ulip_camera:
                str_doc = IA_UPGRADE_ULIP_CARD_DOC;
                capture_document();
                //capture_docs(IA_UPGRADE_ULIP_CARD_DOC, "CAMERA");
                break;

            case R.id.imgbtn_ia_upgrade_ulip_browse:
                str_doc = IA_UPGRADE_ULIP_CARD_DOC;
                browse_docs();
                //capture_docs(IA_UPGRADE_ULIP_CARD_DOC, "BROWSE");
                break;

            case R.id.imgbtn_ia_upgrade_ulip_upload:
                createSoapRequestToUploadDoc(mIAUpgradeUlip);
                break;

            case R.id.imgbtn_ia_upgrade_form_ia_view:
                if (mIAUpgradeFormIA != null) {
                    try {
                        mCommonMethods.openAllDocs(mContext, mIAUpgradeFormIA);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                } else {
                    mCommonMethods.showToast(mContext, "Please Browse or Capture Form IA Document First!");
                }
                break;

            case R.id.imgbtn_ia_upgrade_form_ia_camera:
                str_doc = IA_UPGRADE_FORM_IA;
                capture_document();
                //capture_docs(IA_UPGRADE_FORM_IA, "CAMERA");
                break;

            case R.id.imgbtn_ia_upgrade_form_ia_browse:
                str_doc = IA_UPGRADE_FORM_IA;
                browse_docs();
                //capture_docs(IA_UPGRADE_FORM_IA, "BROWSE");
                break;

            case R.id.imgbtn_ia_upgrade_form_ia_upload:
                createSoapRequestToUploadDoc(mIAUpgradeFormIA);
                break;

            case R.id.imgbtn_ia_upgrade_mob_declaration_view:
                if (mIAUpgradeMobDeclaration != null) {
                    try {
                        mCommonMethods.openAllDocs(mContext, mIAUpgradeMobDeclaration);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                } else {
                    mCommonMethods.showToast(mContext, "Please Browse or Capture Mobile Declaration Document First!");
                }
                break;

            case R.id.imgbtn_ia_upgrade_mob_declaration_camera:
                str_doc = IA_UPGRADE_MOBILE_DECLARATION;
                capture_document();
                //capture_docs(IA_UPGRADE_MOBILE_DECLARATION, "CAMERA");
                break;

            case R.id.imgbtn_ia_upgrade_mob_declaration_browse:
                str_doc = IA_UPGRADE_MOBILE_DECLARATION;
                browse_docs();
                //capture_docs(IA_UPGRADE_MOBILE_DECLARATION, "BROWSE");
                break;

            case R.id.imgbtn_ia_upgrade_mob_declaration_upload:
                createSoapRequestToUploadDoc(mIAUpgradeMobDeclaration);
                break;

            case R.id.btn_aob_auth_pan_submit:

                if (is_rejection) {

                    if (is_ia_upgrade_tcc_uploaded && is_ia_upgrade_score_card_uploaded && is_ia_upgrade_ulip_uploaded
                            && is_ia_upgrade_form_ia_uploaded && is_ia_upgrade_mob_declaration_uploaded
                            && is_others_doc_uploaded) {
                        showMessageDialog("Requirement uploaded Successfully!");
                    } else {
                        mCommonMethods.showMessageDialog(mContext, "Please Upload Requirement Documents..");
                    }
                } else if (is_dashboard) {
                    startActivity(new Intent(ActivityAOBDocumentUpload.this, ActivityAOBDashboard.class));
                } else if (!is_dashboard) {
                    String str_error = validateDetails();
                    if (str_error.equals("")) {

                        //create xml for saving
                        get_doc_upload_xml();

                        //3. update data against global row id
                        ContentValues cv = new ContentValues();
                        cv.put(db.AGENT_ON_BOARDING_DOCUMENTS_UPLOAD, str_doc_upload.toString());
                        cv.put(db.AGENT_ON_BOARDING_UPDATED_BY, strCIFBDMUserId);

                        //save date in long
                        cv.put(db.AGENT_ON_BOARDING_UPDATED_DATE, new Date(mCalendar.getTimeInMillis()).getTime() + "");
                        //status for database data sync
                        cv.put(db.AGENT_ON_BOARDING_SYNCH_STATUS, "13");

                        int i = db.update_agent_on_boarding_details(cv, db.AGENT_ON_BOARDING_ID + " =? ",
                                new String[]{Activity_AOB_Authentication.row_details + ""});

                        //get all data from db
                        ArrayList<PojoAOB> lstRes = db.get_agent_on_boarding_details_by_ID(Activity_AOB_Authentication.row_details);
                        if (lstRes.size() > 0) {

                            try {
                                str_final_aob_info = new StringBuilder();

                                //create final Agent on boarding string to synch
                                str_final_aob_info.append("<?xml version='1.0' encoding='utf-8' ?><agent_on_boarding>");
                                //str_final_aob_info.append(lstRes.get(0).getStr_basic_details().toString());//basic details
                                str_final_aob_info.append(lstRes.get(0).getStr_personal_info());//personal info
                                str_final_aob_info.append(lstRes.get(0).getStr_occupation_info());//occupational info

                                //added 19-01-2021
                                String str_nominee_info = lstRes.get(0).getStr_nomination_info();

                                String str_nominee_info_nom_address2 = mCommonMethods.getSubStringByString(str_nominee_info, "nominee_info_nom_address2");
                                str_nominee_info = str_nominee_info.replace(str_nominee_info_nom_address2, "");
                                String str_nominee_info_nom_address3 = mCommonMethods.getSubStringByString(str_nominee_info, "nominee_info_nom_address3");
                                str_nominee_info = str_nominee_info.replace(str_nominee_info_nom_address3, "");

                                String str_nominee_info_appointee_address2 = mCommonMethods.getSubStringByString(str_nominee_info, "nominee_info_appointee_address2");
                                str_nominee_info = str_nominee_info.replace(str_nominee_info_appointee_address2, "");
                                String str_nominee_info_appointee_address3 = mCommonMethods.getSubStringByString(str_nominee_info, "nominee_info_appointee_address3");
                                str_nominee_info = str_nominee_info.replace(str_nominee_info_appointee_address3, "");

                                str_final_aob_info.append(str_nominee_info);//nominational info
                                //ended 19-01-2021

                                str_final_aob_info.append(lstRes.get(0).getStr_bank_details());//bank details info
                                str_final_aob_info.append(lstRes.get(0).getStr_form_1_a());//form 1-a info

                                //Exam and Training details
                                String str_exam_training_details = lstRes.get(0).getStr_exam_training_details();

                                String str_ia_upgrade_exam_roll_no = mCommonMethods.getSubStringByString(str_exam_training_details, "ia_upgrade_exam_roll_no");
                                str_exam_training_details = str_exam_training_details.replace(str_ia_upgrade_exam_roll_no, "");

                                String str_ia_upgrade_exam_date = mCommonMethods.getSubStringByString(str_exam_training_details, "ia_upgrade_exam_date");
                                str_exam_training_details = str_exam_training_details.replace(str_ia_upgrade_exam_date, "");

                                String str_ia_upgrade_exam_obtained_marks = mCommonMethods.getSubStringByString(str_exam_training_details, "ia_upgrade_exam_obtained_marks");
                                str_exam_training_details = str_exam_training_details.replace(str_ia_upgrade_exam_obtained_marks, "");

                                String str_ia_upgrade_exam_place = mCommonMethods.getSubStringByString(str_exam_training_details, "ia_upgrade_exam_place");
                                str_exam_training_details = str_exam_training_details.replace(str_ia_upgrade_exam_place, "");

                                String str_ia_upgrade_exam_language = mCommonMethods.getSubStringByString(str_exam_training_details, "ia_upgrade_exam_language");
                                str_exam_training_details = str_exam_training_details.replace(str_ia_upgrade_exam_language, "");

                                String str_ia_upgrade_exam_mode = mCommonMethods.getSubStringByString(str_exam_training_details, "ia_upgrade_exam_mode");
                                str_exam_training_details = str_exam_training_details.replace(str_ia_upgrade_exam_mode, "");

                                String str_ia_upgrade_exam_body = mCommonMethods.getSubStringByString(str_exam_training_details, "ia_upgrade_exam_body");
                                str_exam_training_details = str_exam_training_details.replace(str_ia_upgrade_exam_body, "");

                                String str_ia_upgrade_exam_status = mCommonMethods.getSubStringByString(str_exam_training_details, "ia_upgrade_exam_status");
                                str_exam_training_details = str_exam_training_details.replace(str_ia_upgrade_exam_status, "");

                                String str_ia_upgrade_ulip = mCommonMethods.getSubStringByString(str_exam_training_details, "ia_upgrade_ulip");
                                str_exam_training_details = str_exam_training_details.replace(str_ia_upgrade_ulip, "");

                                String str_ia_upgrade_urn = mCommonMethods.getSubStringByString(str_exam_training_details, "ia_upgrade_urn");
                                str_exam_training_details = str_exam_training_details.replace(str_ia_upgrade_urn, "");

                                str_final_aob_info.append(str_exam_training_details);
                                //Exam and Training details

                                str_final_aob_info.append(lstRes.get(0).getStr_bsm_interview_questions());//BSM interview questions
                                //str_final_aob_info.append(lstRes.get(0).getStr_declarations_conditions().toString());//terms and conditions

                                str_final_aob_info.append("<um_code>" + strCIFBDMUserId + "</um_code>");

                                SimpleDateFormat sdp = new SimpleDateFormat("MM-dd-yyyy");

                                String str_created_date = "";
                                try {
                                    //str_created_date = sdp.format(new Date(Long.valueOf(lstRes.get(0).getStr_created_date())));
                                    str_created_date = sdp.format(new Date(mCalendar.getTimeInMillis()));
                                } catch (Exception ex) {
                                    //str_created_date = lstRes.get(0).getStr_created_date();
                                    ex.printStackTrace();
                                }
                                //String str_created_date = sdp.format(new Date(Long.valueOf(lstRes.get(0).getStr_created_date())));
                                str_final_aob_info.append("<created_date>" + str_created_date + "</created_date>");//created_date

                                //added 19-01-2021
                                str_final_aob_info.append(str_nominee_info_nom_address2 + str_nominee_info_nom_address3
                                        + str_nominee_info_appointee_address2 + str_nominee_info_appointee_address3);
                                //ended 19-01-2021

                                //added 25-05-2021
                                str_final_aob_info.append("<agency_type>IA</agency_type>");
                                //ended 25-05-2021
                                str_final_aob_info.append("<sub_type>").append(lstRes.get(0).getStrEnrollType()).append("</sub_type>");

                                //added for IA Upgrade
                                str_final_aob_info.append(str_ia_upgrade_exam_roll_no + str_ia_upgrade_exam_date
                                        + str_ia_upgrade_exam_obtained_marks + str_ia_upgrade_exam_place
                                        + str_ia_upgrade_exam_language + str_ia_upgrade_exam_mode
                                        + str_ia_upgrade_exam_body + str_ia_upgrade_exam_status + str_ia_upgrade_ulip
                                        + str_ia_upgrade_urn);

                                //for stamping 30-09-2021
                                String strRejectFlag = "", strRejectDate = "";
                                if (is_rejection) {
                                    strRejectFlag = "1";
                                    sdp = new SimpleDateFormat("MM-dd-yyyy");
                                    strRejectDate = sdp.format(new Date());
                                }
                                str_final_aob_info.append("<REQDOCFLAG>" + strRejectFlag + "</REQDOCFLAG>");
                                str_final_aob_info.append("<Reqdocsubmitdate>" + strRejectDate + "</Reqdocsubmitdate>");
                                //for stamping 30-09-2021

                                str_final_aob_info.append("</agent_on_boarding>");

                                mAsyncAllAOB = new AsyncAllAOB();
                                mAsyncAllAOB.execute();

                            } catch (Exception ex) {
                                mCommonMethods.printLog("ActivityAOBDocumentUpload Error : ", ex.getMessage());
                            }
                        } else {
                            mCommonMethods.showMessageDialog(mContext, "Data Synch Failed");
                        }

                    } else {
                        mCommonMethods.showMessageDialog(mContext, str_error);
                    }
                }

                break;

            default:
                break;
        }
    }

    public String validateDetails() {

        if (is_ia_upgrade) {
            if (!is_ia_upgrade_tcc_uploaded) {
                return "Please upload TCC Document";
            } else if (!is_ia_upgrade_score_card_uploaded) {
                return "Please upload Score Card Document";
            } else if (!is_ia_upgrade_ulip_uploaded) {
                return "Please upload ULIP Document";
            } else if (!is_ia_upgrade_form_ia_uploaded) {
                return "Please upload Form IA Document";
            } else if (!is_ia_upgrade_mob_declaration_uploaded) {
                return "Please upload Mobile Declaration Document";
            } else {
                return "";
            }
        } else {
            if (!is_bank_passbook_uploaded) {
                return "Please upload Bank Passbook Documents";
            } else if (!is_age_proof_uploaded) {
                return "Please upload Age Proof Documents";
            } else if (!is_basic_qualification_uploaded) {
                return "Please upload Basic Qualification Documents";
            } else if (!is_comm_address_uploaded) {
                return "Please upload Communication Address Documents";
            }/*else if (!is_applicant_sign_uploaded){
            return "Please upload Applicant Signature";
        }else *//*if (!is_declaration_uploaded){
            return "Please upload Declaration";
        }*/ else
                return "";
        }
    }

    private void get_doc_upload_xml() {

        str_doc_upload = new StringBuilder();

        //str_terms_conditions.append("<doc_upload>");


        str_doc_upload.append("<doc_upload_bank_pass_type>").append(str_bank_passbook_type).append("</doc_upload_bank_pass_type>");
        str_doc_upload.append("<doc_upload_bank_pass_is_upload>").append(is_bank_passbook_uploaded).append("</doc_upload_bank_pass_is_upload>");

        str_doc_upload.append("<doc_upload_age_proof_type>").append(str_age_proof_type).append("</doc_upload_age_proof_type>");
        str_doc_upload.append("<doc_upload_age_proof_is_upload>").append(is_age_proof_uploaded).append("</doc_upload_age_proof_is_upload>");

        str_doc_upload.append("<doc_upload_basic_qualification_type>").append(str_basic_qualification_type).append("</doc_upload_basic_qualification_type>");
        str_doc_upload.append("<doc_upload_basic_qualification_is_upload>").append(is_basic_qualification_uploaded).append("</doc_upload_basic_qualification_is_upload>");

        str_doc_upload.append("<doc_upload_high_qualification_type>").append(str_high_qualification_type).append("</doc_upload_high_qualification_type>");
        str_doc_upload.append("<doc_upload_high_qualification_is_upload>").append(is_high_qualification_uploaded).append("</doc_upload_high_qualification_is_upload>");

        str_doc_upload.append("<doc_upload_comm_address_type>").append(str_comm_address_type).append("</doc_upload_comm_address_type>");
        str_doc_upload.append("<doc_upload_comm_address_is_upload>").append(is_comm_address_uploaded).append("</doc_upload_comm_address_is_upload>");

        str_doc_upload.append("<doc_upload_permanent_address_type>").append(str_permanent_address_type).append("</doc_upload_permanent_address_type>");
        str_doc_upload.append("<doc_upload_permanent_address_is_upload>").append(is_permanent_address_uploaded).append("</doc_upload_permanent_address_is_upload>");

        str_doc_upload.append("<doc_upload_form_15G_15H_type>").append(str_form15g_15h_type).append("</doc_upload_form_15G_15H_type>");
        str_doc_upload.append("<doc_upload_form_15G_15H_is_upload>").append(is_form15g_15h_uploaded).append("</doc_upload_form_15G_15H_is_upload>");

        str_doc_upload.append("<doc_upload_others_type>").append(str_others_doc_type).append("</doc_upload_others_type>");
        str_doc_upload.append("<doc_upload_others_document>").append(edt_aob_auth_others.getText().toString()).append("</doc_upload_others_document>");
        str_doc_upload.append("<doc_upload_others_is_upload>").append(is_others_doc_uploaded).append("</doc_upload_others_is_upload>");

        str_doc_upload.append("<doc_upload_form_1B_type>").append(str_form1B_type).append("</doc_upload_form_1B_type>");
        str_doc_upload.append("<doc_upload_form_1B_is_upload>").append(is_form1B_uploaded).append("</doc_upload_form_1B_is_upload>");

        str_doc_upload.append("<doc_upload_form_1C_type>").append(str_form1C_type).append("</doc_upload_form_1C_type>");
        str_doc_upload.append("<doc_upload_form_1C_is_upload>").append(is_form1C_uploaded).append("</doc_upload_form_1C_is_upload>");

        str_doc_upload.append("<doc_upload_applicant_sign_type>").append(str_applicant_sign_type).append("</doc_upload_applicant_sign_type>");
        str_doc_upload.append("<doc_upload_applicant_sign_is_upload>").append(is_applicant_sign_uploaded).append("</doc_upload_applicant_sign_is_upload>");

        str_doc_upload.append("<doc_upload_declaration_type>").append(str_declaration_type).append("</doc_upload_declaration_type>");
        str_doc_upload.append("<doc_upload_declaration_is_upload>").append(is_declaration_uploaded).append("</doc_upload_declaration_is_upload>");

        //IA Upgrade
        str_doc_upload.append("<doc_upload_ia_upgrade_tcc_type>").append(str_ia_upgrade_tcc_type).append("</doc_upload_ia_upgrade_tcc_type>");
        str_doc_upload.append("<doc_upload_ia_upgrade_tcc_is_upload>").append(is_ia_upgrade_tcc_uploaded).append("</doc_upload_ia_upgrade_tcc_is_upload>");

        str_doc_upload.append("<doc_upload_ia_upgrade_score_card_type>").append(str_ia_upgrade_score_card_type).append("</doc_upload_ia_upgrade_score_card_type>");
        str_doc_upload.append("<doc_upload_ia_upgrade_score_card_is_upload>").append(is_ia_upgrade_score_card_uploaded).append("</doc_upload_ia_upgrade_score_card_is_upload>");

        str_doc_upload.append("<doc_upload_ia_upgrade_ulip_type>").append(str_ia_upgrade_ulip_type).append("</doc_upload_ia_upgrade_ulip_type>");
        str_doc_upload.append("<doc_upload_ia_upgrade_ulip_is_upload>").append(is_ia_upgrade_ulip_uploaded).append("</doc_upload_ia_upgrade_ulip_is_upload>");

        str_doc_upload.append("<doc_upload_ia_upgrade_form_ia_type>").append(str_ia_upgrade_form_ia_type).append("</doc_upload_ia_upgrade_form_ia_type>");
        str_doc_upload.append("<doc_upload_ia_upgrade_form_ia_is_upload>").append(is_ia_upgrade_form_ia_uploaded).append("</doc_upload_ia_upgrade_form_ia_is_upload>");

        str_doc_upload.append("<doc_upload_ia_upgrade_mob_declaration_type>").append(str_ia_upgrade_mob_declaration_type).append("</doc_upload_ia_upgrade_mob_declaration_type>");
        str_doc_upload.append("<doc_upload_ia_upgrade_mob_declaration_is_upload>").append(is_ia_upgrade_mob_declaration_uploaded).append("</doc_upload_ia_upgrade_mob_declaration_is_upload>");

        //str_terms_conditions.append("</doc_upload>");
    }

    public void showMessageDialog(String message) {
        try {
            final Dialog dialog = new Dialog(mContext);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_with_ok_button);
            dialog.setCancelable(false);

            TextView text = (TextView) dialog.findViewById(R.id.tv_title);
            text.setText(message);

            Button dialogButton = (Button) dialog.findViewById(R.id.bt_ok);
            dialogButton.setText("Ok");
            dialogButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    dialog.dismiss();

                    if (is_rejection) {
                        Intent i = new Intent(ActivityAOBDocumentUpload.this, Activity_POSP_RA_Rejection_Remarks.class);
                        i.putExtra("enrollment_type", mCommonMethods.str_ia_upgrade_customer_type);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                    } else if (!message.equalsIgnoreCase("Data synch Failure..\ntry after some time by dashboard menu.")) {
                        Activity_AOB_Authentication.row_details = 0;

                        //redirect to home page
                        Intent i = new Intent(ActivityAOBDocumentUpload.this, Activity_AOB_Authentication.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                    }
                }
            });
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void enableDisableAllFields(boolean is_enable) {

        edt_aob_auth_others.setEnabled(is_enable);
        imgbtn_aob_auth_others_camera.setEnabled(is_enable);
        imgbtn_aob_auth_others_browse.setEnabled(is_enable);
        imgbtn_aob_auth_others_upload.setEnabled(is_enable);

        if (is_ia_upgrade) {
            imgbtn_ia_upgrade_tcc_camera.setEnabled(is_enable);
            imgbtn_ia_upgrade_tcc_browse.setEnabled(is_enable);
            imgbtn_ia_upgrade_tcc_upload.setEnabled(is_enable);

            imgbtn_ia_upgrade_score_card_camera.setEnabled(is_enable);
            imgbtn_ia_upgrade_score_card_browse.setEnabled(is_enable);
            imgbtn_ia_upgrade_score_card_upload.setEnabled(is_enable);

            imgbtn_ia_upgrade_ulip_camera.setEnabled(is_enable);
            imgbtn_ia_upgrade_ulip_browse.setEnabled(is_enable);
            imgbtn_ia_upgrade_ulip_upload.setEnabled(is_enable);

            imgbtn_ia_upgrade_form_ia_camera.setEnabled(is_enable);
            imgbtn_ia_upgrade_form_ia_browse.setEnabled(is_enable);
            imgbtn_ia_upgrade_form_ia_upload.setEnabled(is_enable);

            imgbtn_ia_upgrade_mob_declaration_camera.setEnabled(is_enable);
            imgbtn_ia_upgrade_mob_declaration_browse.setEnabled(is_enable);
            imgbtn_ia_upgrade_mob_declaration_upload.setEnabled(is_enable);
        } else {

            imgbtn_aob_auth_bank_pass_book_camera.setEnabled(is_enable);
            imgbtn_aob_auth_bank_pass_book_browse.setEnabled(is_enable);
            imgbtn_aob_auth_bank_pass_book_upload.setEnabled(is_enable);

            imgbtn_aob_auth_age_proof_camera.setEnabled(is_enable);
            imgbtn_aob_auth_age_proof_browse.setEnabled(is_enable);
            imgbtn_aob_auth_age_proof_upload.setEnabled(is_enable);

            imgbtn_aob_auth_basic_qualification_camera.setEnabled(is_enable);
            imgbtn_aob_auth_basic_qualification_browse.setEnabled(is_enable);
            imgbtn_aob_auth_basic_qualification_upload.setEnabled(is_enable);

            imgbtn_aob_auth_high_qualification_camera.setEnabled(is_enable);
            imgbtn_aob_auth_high_qualification_browse.setEnabled(is_enable);
            imgbtn_aob_auth_high_qualification_upload.setEnabled(is_enable);

            imgbtn_aob_auth_comm_add_camera.setEnabled(is_enable);
            imgbtn_aob_auth_comm_add_browse.setEnabled(is_enable);
            imgbtn_aob_auth_comm_add_upload.setEnabled(is_enable);

            imgbtn_aob_auth_permanent_add_camera.setEnabled(is_enable);
            imgbtn_aob_auth_permanent_add_browse.setEnabled(is_enable);
            imgbtn_aob_auth_permanent_add_upload.setEnabled(is_enable);

            imgbtn_aob_auth_form15g_15h_camera.setEnabled(is_enable);
            imgbtn_aob_auth_form15g_15h_browse.setEnabled(is_enable);
            imgbtn_aob_auth_form15g_15h_upload.setEnabled(is_enable);

            imgbtn_aob_auth_form1b_camera.setEnabled(is_enable);
            imgbtn_aob_auth_form1b_browse.setEnabled(is_enable);
            imgbtn_aob_auth_form1b_upload.setEnabled(is_enable);

            imgbtn_aob_auth_form1c_camera.setEnabled(is_enable);
            imgbtn_aob_auth_form1c_browse.setEnabled(is_enable);
            imgbtn_aob_auth_form1c_upload.setEnabled(is_enable);
        }
    }

    public void enableUploadButton(String str_doc_type) {

        imgbtn_aob_auth_others_upload.setEnabled(false);
        if (str_doc_type.equals(OTHERS_DOC)) {
            imgbtn_aob_auth_others_upload.setEnabled(true);
        }

        if (is_ia_upgrade) {

            imgbtn_ia_upgrade_tcc_upload.setEnabled(false);
            imgbtn_ia_upgrade_score_card_upload.setEnabled(false);
            imgbtn_ia_upgrade_ulip_upload.setEnabled(false);
            imgbtn_ia_upgrade_form_ia_upload.setEnabled(false);
            imgbtn_ia_upgrade_mob_declaration_upload.setEnabled(false);

            if (str_doc_type.equals(IA_UPGRADE_TCC_DOC)) {
                imgbtn_ia_upgrade_tcc_upload.setEnabled(true);
            } else if (str_doc_type.equals(IA_UPGRADE_SCORE_CARD_DOC)) {
                imgbtn_ia_upgrade_score_card_upload.setEnabled(true);
            } else if (str_doc_type.equals(IA_UPGRADE_ULIP_CARD_DOC)) {
                imgbtn_ia_upgrade_ulip_upload.setEnabled(true);
            } else if (str_doc_type.equals(IA_UPGRADE_FORM_IA)) {
                imgbtn_ia_upgrade_form_ia_upload.setEnabled(true);
            } else if (str_doc_type.equals(IA_UPGRADE_MOBILE_DECLARATION)) {
                imgbtn_ia_upgrade_mob_declaration_upload.setEnabled(true);
            }

        } else {
            imgbtn_aob_auth_bank_pass_book_upload.setEnabled(false);

            imgbtn_aob_auth_age_proof_upload.setEnabled(false);

            imgbtn_aob_auth_basic_qualification_upload.setEnabled(false);

            imgbtn_aob_auth_high_qualification_upload.setEnabled(false);

            imgbtn_aob_auth_comm_add_upload.setEnabled(false);

            imgbtn_aob_auth_permanent_add_upload.setEnabled(false);

            imgbtn_aob_auth_form15g_15h_upload.setEnabled(false);

            imgbtn_aob_auth_form1b_upload.setEnabled(false);

            imgbtn_aob_auth_form1c_upload.setEnabled(false);

            imgbtn_aob_applicant_sign_upload.setEnabled(false);

            imgbtn_aob_declaration_upload.setEnabled(false);


            if (str_doc_type.equals(BANK_PASSBOOK_DOC)) {
                imgbtn_aob_auth_bank_pass_book_upload.setEnabled(true);
            } else if (str_doc_type.equals(AGE_PROOF_DOC)) {
                imgbtn_aob_auth_age_proof_upload.setEnabled(true);
            } else if (str_doc_type.equals(BASIC_QUALIFICATION_DOC)) {
                imgbtn_aob_auth_basic_qualification_upload.setEnabled(true);
            } else if (str_doc_type.equals(HIGH_QUALIFICATION_DOC)) {
                imgbtn_aob_auth_high_qualification_upload.setEnabled(true);
            } else if (str_doc_type.equals(COMM_ADDRESS_DOC)) {
                imgbtn_aob_auth_comm_add_upload.setEnabled(true);
            } else if (str_doc_type.equals(PERMANENT_ADDRESS_DOC)) {
                imgbtn_aob_auth_permanent_add_upload.setEnabled(true);
            } else if (str_doc_type.equals(FORM_15G_15H_DOC)) {
                imgbtn_aob_auth_form15g_15h_upload.setEnabled(true);
            } else if (str_doc_type.equals(FORM_1B_DOC)) {
                imgbtn_aob_auth_form1b_upload.setEnabled(true);
            } else if (str_doc_type.equals(FORM_1C_DOC)) {
                imgbtn_aob_auth_form1c_upload.setEnabled(true);
            } else if (str_doc_type.equals(APPLICANT_SIGN)) {
                imgbtn_aob_applicant_sign_upload.setEnabled(true);
            } else if (str_doc_type.equals(DECLARATION)) {
                imgbtn_aob_declaration_upload.setEnabled(true);
            }
        }
    }

    private void createSoapRequestToUploadDoc(final File mFile) {

        if (mFile != null) {

            Single.fromCallable(() -> mCommonMethods.read(mFile))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<byte[]>() {
                        @Override
                        public void accept(@NonNull byte[] result) throws Exception {
                            if (result != null) {
                                SoapObject request;
                                if (is_rejection) {
                                    request = new SoapObject(NAMESPACE, METHOD_NAME_UPLOAD_ALL_AOB_DOC_REJECTION);
                                    request.addProperty("Type", mCommonMethods.str_ia_upgrade_customer_type);
                                } else {
                                    request = new SoapObject(NAMESPACE, METHOD_NAME_UPLOAD_ALL_AOB_DOC);
                                }

                                request.addProperty("f", result);

                                request.addProperty("fileName", mFile.getName());

                                request.addProperty("PAN", str_pan_no);

                                mCommonMethods.appendSecurityParams(mContext, request, strCIFBDMEmailId, strCIFBDMMObileNo);

                                if (is_rejection) {
                                    mAsyncUploadFileCommon = new AsyncUploadFile_Common(mContext,
                                            ActivityAOBDocumentUpload.this, request, METHOD_NAME_UPLOAD_ALL_AOB_DOC_REJECTION);
                                } else {
                                    mAsyncUploadFileCommon = new AsyncUploadFile_Common(mContext,
                                            ActivityAOBDocumentUpload.this, request, METHOD_NAME_UPLOAD_ALL_AOB_DOC);
                                }
                                mAsyncUploadFileCommon.execute();

                            } else {
                                mCommonMethods.showToast(mContext, "File Not Found");
                            }
                        }
                    }, throwable -> {
                        mCommonMethods.showToast(mContext, "File Not Found");
                    });
        } else {
            mCommonMethods.showMessageDialog(mContext, "Please Capture / Browse File first!");
        }
    }

    @Override
    public void onUploadComplete(Boolean result) {

        if (result) {

            if (str_doc.equals(DECLARATION)) {

                imgbtn_aob_declaration_camera.setEnabled(false);
                imgbtn_aob_declaration_browse.setEnabled(false);
                imgbtn_aob_declaration_upload.setEnabled(false);
                imgbtn_aob_declaration_upload.setImageDrawable(getResources().getDrawable(R.drawable.checkedupload));

                is_declaration_uploaded = true;
                mDeclarationFile = null;

            } else if (str_doc.equals(APPLICANT_SIGN)) {

                imgbtn_aob_applicant_sign_camera.setEnabled(false);
                imgbtn_aob_applicant_sign_browse.setEnabled(false);
                imgbtn_aob_applicant_sign_upload.setEnabled(false);
                imgbtn_aob_applicant_sign_upload.setImageDrawable(getResources().getDrawable(R.drawable.checkedupload));

                is_applicant_sign_uploaded = true;
                mApplicantSignFile = null;

            } else if (str_doc.equals(BANK_PASSBOOK_DOC)) {

                imgbtn_aob_auth_bank_pass_book_camera.setEnabled(false);
                imgbtn_aob_auth_bank_pass_book_browse.setEnabled(false);
                imgbtn_aob_auth_bank_pass_book_upload.setEnabled(false);
                imgbtn_aob_auth_bank_pass_book_upload.setImageDrawable(getResources().getDrawable(R.drawable.checkedupload));

                is_bank_passbook_uploaded = true;
                mBankPassbookFile = null;

            } else if (str_doc.equals(AGE_PROOF_DOC)) {

                imgbtn_aob_auth_age_proof_camera.setEnabled(false);
                imgbtn_aob_auth_age_proof_browse.setEnabled(false);
                imgbtn_aob_auth_age_proof_upload.setEnabled(false);
                imgbtn_aob_auth_age_proof_upload.setImageDrawable(getResources().getDrawable(R.drawable.checkedupload));

                is_age_proof_uploaded = true;
                mAgeProofFile = null;
            } else if (str_doc.equals(BASIC_QUALIFICATION_DOC)) {

                imgbtn_aob_auth_basic_qualification_camera.setEnabled(false);
                imgbtn_aob_auth_basic_qualification_browse.setEnabled(false);
                imgbtn_aob_auth_basic_qualification_upload.setEnabled(false);
                imgbtn_aob_auth_basic_qualification_upload.setImageDrawable(getResources().getDrawable(R.drawable.checkedupload));

                is_basic_qualification_uploaded = true;
                mBasicQualificationFile = null;
            } else if (str_doc.equals(HIGH_QUALIFICATION_DOC)) {

                imgbtn_aob_auth_high_qualification_camera.setEnabled(false);
                imgbtn_aob_auth_high_qualification_browse.setEnabled(false);
                imgbtn_aob_auth_high_qualification_upload.setEnabled(false);
                imgbtn_aob_auth_high_qualification_upload.setImageDrawable(getResources().getDrawable(R.drawable.checkedupload));

                is_high_qualification_uploaded = true;
                mHighQualificationFile = null;
            } else if (str_doc.equals(COMM_ADDRESS_DOC)) {

                imgbtn_aob_auth_comm_add_camera.setEnabled(false);
                imgbtn_aob_auth_comm_add_browse.setEnabled(false);
                imgbtn_aob_auth_comm_add_upload.setEnabled(false);
                imgbtn_aob_auth_comm_add_upload.setImageDrawable(getResources().getDrawable(R.drawable.checkedupload));

                is_comm_address_uploaded = true;
                mHighQualificationFile = null;
            } else if (str_doc.equals(PERMANENT_ADDRESS_DOC)) {

                imgbtn_aob_auth_permanent_add_camera.setEnabled(false);
                imgbtn_aob_auth_permanent_add_browse.setEnabled(false);
                imgbtn_aob_auth_permanent_add_upload.setEnabled(false);
                imgbtn_aob_auth_permanent_add_upload.setImageDrawable(getResources().getDrawable(R.drawable.checkedupload));

                is_permanent_address_uploaded = true;
                mPermanentAddFile = null;
            } else if (str_doc.equals(FORM_15G_15H_DOC)) {

                imgbtn_aob_auth_form15g_15h_camera.setEnabled(false);
                imgbtn_aob_auth_form15g_15h_browse.setEnabled(false);
                imgbtn_aob_auth_form15g_15h_upload.setEnabled(false);
                imgbtn_aob_auth_form15g_15h_upload.setImageDrawable(getResources().getDrawable(R.drawable.checkedupload));

                is_form15g_15h_uploaded = true;
                mForm15G_15HFile = null;
            } else if (str_doc.equals(OTHERS_DOC)) {

                imgbtn_aob_auth_others_camera.setEnabled(false);
                imgbtn_aob_auth_others_browse.setEnabled(false);
                imgbtn_aob_auth_others_upload.setEnabled(false);
                imgbtn_aob_auth_others_upload.setImageDrawable(getResources().getDrawable(R.drawable.checkedupload));

                is_others_doc_uploaded = true;
                mOthersFile = null;
            } else if (str_doc.equals(FORM_1B_DOC)) {

                imgbtn_aob_auth_form1b_camera.setEnabled(false);
                imgbtn_aob_auth_form1b_browse.setEnabled(false);
                imgbtn_aob_auth_form1b_upload.setEnabled(false);
                imgbtn_aob_auth_form1b_upload.setImageDrawable(getResources().getDrawable(R.drawable.checkedupload));

                is_form1B_uploaded = true;
                mForm1BFile = null;
            } else if (str_doc.equals(FORM_1C_DOC)) {

                imgbtn_aob_auth_form1c_camera.setEnabled(false);
                imgbtn_aob_auth_form1c_browse.setEnabled(false);
                imgbtn_aob_auth_form1c_upload.setEnabled(false);
                imgbtn_aob_auth_form1c_upload.setImageDrawable(getResources().getDrawable(R.drawable.checkedupload));

                is_form1C_uploaded = true;
                mForm1CFile = null;
            } else if (str_doc.equals(IA_UPGRADE_TCC_DOC)) {

                imgbtn_ia_upgrade_tcc_camera.setEnabled(false);
                imgbtn_ia_upgrade_tcc_browse.setEnabled(false);
                imgbtn_ia_upgrade_tcc_upload.setEnabled(false);
                imgbtn_ia_upgrade_tcc_upload.setImageDrawable(getResources().getDrawable(R.drawable.checkedupload));

                is_ia_upgrade_tcc_uploaded = true;
                mIAUpgradeTCC = null;
            } else if (str_doc.equals(IA_UPGRADE_SCORE_CARD_DOC)) {

                imgbtn_ia_upgrade_score_card_camera.setEnabled(false);
                imgbtn_ia_upgrade_score_card_browse.setEnabled(false);
                imgbtn_ia_upgrade_score_card_upload.setEnabled(false);
                imgbtn_ia_upgrade_score_card_upload.setImageDrawable(getResources().getDrawable(R.drawable.checkedupload));

                is_ia_upgrade_score_card_uploaded = true;
                mIAUpgradeScoreCard = null;
            } else if (str_doc.equals(IA_UPGRADE_ULIP_CARD_DOC)) {

                imgbtn_ia_upgrade_ulip_camera.setEnabled(false);
                imgbtn_ia_upgrade_ulip_browse.setEnabled(false);
                imgbtn_ia_upgrade_ulip_upload.setEnabled(false);
                imgbtn_ia_upgrade_ulip_upload.setImageDrawable(getResources().getDrawable(R.drawable.checkedupload));

                is_ia_upgrade_ulip_uploaded = true;
                mIAUpgradeUlip = null;
            } else if (str_doc.equals(IA_UPGRADE_FORM_IA)) {

                imgbtn_ia_upgrade_form_ia_camera.setEnabled(false);
                imgbtn_ia_upgrade_form_ia_browse.setEnabled(false);
                imgbtn_ia_upgrade_form_ia_upload.setEnabled(false);
                imgbtn_ia_upgrade_form_ia_upload.setImageDrawable(getResources().getDrawable(R.drawable.checkedupload));

                is_ia_upgrade_form_ia_uploaded = true;
                mIAUpgradeFormIA = null;
            } else if (str_doc.equals(IA_UPGRADE_MOBILE_DECLARATION)) {

                imgbtn_ia_upgrade_mob_declaration_camera.setEnabled(false);
                imgbtn_ia_upgrade_mob_declaration_browse.setEnabled(false);
                imgbtn_ia_upgrade_mob_declaration_upload.setEnabled(false);
                imgbtn_ia_upgrade_mob_declaration_upload.setImageDrawable(getResources().getDrawable(R.drawable.checkedupload));

                is_ia_upgrade_mob_declaration_uploaded = true;
                mIAUpgradeMobDeclaration = null;
            }

            Toast.makeText(mContext, "Document Upload Successfully...",
                    Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(mContext, "PLease try agian later..",
                    Toast.LENGTH_SHORT).show();
        }

    }

    public class AsyncAllAOB extends AsyncTask<String, String, String> {

        private volatile boolean running = true;

        @Override
        protected void onPreExecute() {
            mProgressDialog = new ProgressDialog(mContext,
                    ProgressDialog.THEME_HOLO_LIGHT);
            mProgressDialog.setMessage(Html.fromHtml("<font color='#00a1e3'><b>Please wait...<b></font>"));
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                running = true;

                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME_SAVE_AOB_DETAILS);
                request.addProperty("xmlStr", str_final_aob_info.toString());
                mCommonMethods.TLSv12Enable();

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;

                envelope.setOutputSoapObject(request);

                HttpTransportSE androidHttpTranport = new HttpTransportSE(ServiceURL.SERVICE_URL);
                try {
                    androidHttpTranport.call(NAMESPACE + METHOD_NAME_SAVE_AOB_DETAILS, envelope);
                    //Object response = envelope.getResponse();

                    SoapPrimitive sa = null;
                    try {
                        sa = (SoapPrimitive) envelope.getResponse();

                        return sa.toString();

                    } catch (Exception e) {
                        try {
                            throw (e);
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                        running = false;
                    }

                } catch (IOException e) {
                    try {
                        throw (e);
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                    running = false;
                } catch (XmlPullParserException e) {
                    try {
                        throw (e);
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                    running = false;
                }
            } catch (Exception e) {
                try {
                    throw (e);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                running = false;
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {

            if (mProgressDialog.isShowing())
                mProgressDialog.dismiss();

            if (running) {

                //3. update data against global row id
                ContentValues cv = new ContentValues();
                cv.put(db.AGENT_ON_BOARDING_UPDATED_BY, strCIFBDMUserId);

                //save date in long
                cv.put(db.AGENT_ON_BOARDING_UPDATED_DATE, new Date(mCalendar.getTimeInMillis()).getTime() + "");

                String str_data_save_err = "";
                if (result.equals("1")) {
                    cv.put(db.AGENT_ON_BOARDING_SYNCH_STATUS, "14");//for successfull synch
                    str_data_save_err = "Data synch Successfully..";
                } else if (result.equals("0")) {
                    cv.put(db.AGENT_ON_BOARDING_SYNCH_STATUS, "15");//for synch failure
                    str_data_save_err = "Data synch Failure..\ntry after some time by dashboard menu.";
                } else if (result.equals("2")) {
                    cv.put(db.AGENT_ON_BOARDING_SYNCH_STATUS, "16");//for data already exist in server
                    str_data_save_err = "Applicant data already exists in server";
                }

                int i = db.update_agent_on_boarding_details(cv, db.AGENT_ON_BOARDING_ID + " =? ",
                        new String[]{Activity_AOB_Authentication.row_details + ""});

                showMessageDialog(str_data_save_err);

            } else {
                mCommonMethods.showMessageDialog(mContext, mCommonMethods.WEEK_INTERNET_MESSAGE);
            }

        }
    }
}
