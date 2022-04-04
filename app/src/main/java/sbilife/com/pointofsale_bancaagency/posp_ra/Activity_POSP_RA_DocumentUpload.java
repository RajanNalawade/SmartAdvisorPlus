package sbilife.com.pointofsale_bancaagency.posp_ra;

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
import sbilife.com.pointofsale_bancaagency.utility.SelfAttestedDocumentActivity;

public class Activity_POSP_RA_DocumentUpload extends AppCompatActivity implements View.OnClickListener, AsyncUploadFile_Common.Interface_Upload_File_Common {

    private final String NAMESPACE = "http://tempuri.org/";
    private final String METHOD_NAME_UPLOAD_ALL_POSP_RA_DOC = "UploadFile_AgentEnroll";

    //to handle rejection
    private final String METHOD_NAME_UPLOAD_ALL_POSP_RA_DOC_REJECTION = "UploadFile_AgentEnroll_other";
    private final String METHOD_NAME_SAVE_POSP_RA_DETAILS = "saveAgentOnboardingDetail_other";
    //private final int REQUEST_OCR = 100;
    private final int REQUEST_CODE_CAPTURE_DOCUMENT = 200;
    private final String BANK_PASSBOOK_DOC = "bank_passbook";
    private final String TCC_PROOF_DOC = "posp_ra_tcc_proof";
    private final String EDUCATION_PROOF_DOC = "education_proof";
    private final String COMM_PROOF_DOC = "communication_proof";
    private final String SCORE_CARD_DOC = "posp_ra_score_card_proof";
    private final String OTHERS_DOC = "posp_ra_others";
    private final String MOBILE_DECLARATION_DOC = "posp_ra_mob_declare_proof";
    private final String NOMINATION_FORM_DOC = "nomination_form";
    private final String PAN_DOC = "pan";
    private Context mContext;
    private CommonMethods mCommonMethods;
    private StorageUtils mStorageUtils;
    private DatabaseHelper db;
    private Button btn_aob_auth_pan_submit, btn_aob_auth_pan_back;
    private EditText edt_aob_auth_others;
    private ImageButton imgbtn_aob_auth_bank_pass_book_view, imgbtn_aob_auth_bank_pass_book_camera, imgbtn_aob_auth_bank_pass_book_browse, imgbtn_aob_auth_bank_pass_book_upload,
            imgbtn_posp_tcc_view, imgbtn_posp_tcc_camera, imgbtn_posp_tcc_browse, imgbtn_posp_tcc_upload,
            imgbtn_aob_auth_basic_qualification_view, imgbtn_aob_auth_basic_qualification_camera, imgbtn_aob_auth_basic_qualification_browse, imgbtn_aob_auth_basic_qualification_upload,
            imgbtn_aob_auth_comm_add_view, imgbtn_aob_auth_comm_add_camera, imgbtn_aob_auth_comm_add_browse, imgbtn_aob_auth_comm_add_upload,
            imgbtn_posp_score_card_view, imgbtn_posp_score_card_camera, imgbtn_posp_score_card_browse, imgbtn_posp_score_cardupload,
            imgbtn_aob_auth_others_view, imgbtn_aob_auth_others_camera, imgbtn_aob_auth_others_browse, imgbtn_aob_auth_others_upload,
            imgbtn_posp_declaration_view, imgbtn_posp_declaration_camera, imgbtn_posp_declaration_browse, imgbtn_posp_declaration_upload,
            imgbtn_posp_nomination_view, imgbtn_posp_nomination_camera, imgbtn_posp_nomination_browse, imgbtn_posp_nomination_upload,
            imgbtn_posp_pan_view, imgbtn_posp_pan_camera, imgbtn_posp_pan_browse, imgbtn_posp_pan_upload;
    private TextView txt_aob_auth_bank_pass_book_remark, txt_aob_auth_basic_qualification_remark, txt_aob_auth_comm_add_remark,
            txt_posp_tcc_remark, txt_posp_score_card_remark, txt_posp_declaration_remark, txt_aob_auth_others_remark,
            txt_posp_nomination_remark, txt_posp_pan_remark;

    private String str_pan_no = "", str_um_code = "";
    private String str_doc = "";
    private String str_bank_passbook_type = "";
    private String str_tcc_proof_type = "";
    private String str_education_proof_type = "";
    private String str_communication_proof_type = "";
    private String str_score_card_proof_type = "";
    private String str_others_doc_type = "";
    private String str_nomination_doc_type = "", str_pan_doc_type = "";
    private String OCR_TYPE = "";
    private String str_declaration_type = "";
    private String strCIFBDMUserId = "";
    private String strCIFBDMEmailId = "";
    private String strCIFBDMMObileNo = "";
    private boolean is_bank_passbook_uploaded = false;
    private boolean is_tcc_proof_uploaded = false;
    private boolean is_education_proof_uploaded = false;
    private boolean is_communication_proof_uploaded = false;
    private boolean is_score_card_proof_uploaded = false;
    private boolean is_nomination_doc_uploaded = false, is_pan_doc_uploaded = false;
    private boolean is_others_doc_uploaded = false;
    private boolean is_back_preesed = false;
    private boolean is_dashboard = false;
    private boolean is_rejection = false, is_from_ho = false;
    private boolean is_declaration_uploaded = false;
    private boolean is_bsm_questions = false;

    private LinearLayout ll_posp_requirement_upload;

    private ProgressDialog mProgressDialog;

    private File mBankPassbookFile, mTCCFile, mEducationFile, mCommunicationFile,
            mScoreCardFile, mOthersFile, mNominationFile, mPanFile,
            mMobDeclarationFile, mCapturedImgFile;

    private AsyncUploadFile_Common mAsyncUploadFileCommon;

    private StringBuilder str_doc_upload, str_final_posp_info;

    private Calendar mCalendar;

    private AsyncAllAOB mAsyncAllAOB;

    private ParseXML mParseXML;
    private ActivityResultLauncher<Intent> mBrowseDocLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            try {
                if (result.getResultCode() == RESULT_OK) {
                    if (result != null && result.getData().getData() != null) {

                        Uri mSelectedUri = result.getData().getData();

                        String strMimeType = mStorageUtils.getMimeType(mContext, mSelectedUri);
                        Object[] mObject = mCommonMethods.getContentURIDetails(mContext, mSelectedUri);

                        String str_extension = mObject[0].toString();
                        double kilobyte = (double) mObject[2];

                        if (!strMimeType.equals("application/octet-stream")
                                && !strMimeType.equals("application/vnd.android.package-archive")) {
                            if (kilobyte < mCommonMethods.FILE_UPLOAD_RESTRICT_SIZE) {

                                if (str_extension.equals(".pdf")) {

                                    String imageFileName = "";
                                    if (str_doc.equals(PAN_DOC))
                                        imageFileName = str_pan_no + ".pdf";
                                    else
                                        imageFileName = str_pan_no + "_" + str_doc + ".pdf";

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
                                                        } else if (str_doc.equals(TCC_PROOF_DOC)) {
                                                            mTCCFile = fileOutput;

                                                            imgbtn_posp_tcc_camera.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_camera));
                                                            imgbtn_posp_tcc_browse.setImageDrawable(getResources().getDrawable(R.drawable.checkedbrowse));

                                                            str_tcc_proof_type = "BROWSE";

                                                            //remove common source file from directory
                                                            enableUploadButton(TCC_PROOF_DOC);
                                                        } else if (str_doc.equals(EDUCATION_PROOF_DOC)) {
                                                            mEducationFile = fileOutput;

                                                            imgbtn_aob_auth_basic_qualification_camera.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_camera));
                                                            imgbtn_aob_auth_basic_qualification_browse.setImageDrawable(getResources().getDrawable(R.drawable.checkedbrowse));

                                                            str_education_proof_type = "BROWSE";

                                                            //remove common source file from directory
                                                            enableUploadButton(EDUCATION_PROOF_DOC);
                                                        } else if (str_doc.equals(COMM_PROOF_DOC)) {
                                                            mCommunicationFile = fileOutput;

                                                            imgbtn_aob_auth_comm_add_camera.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_camera));
                                                            imgbtn_aob_auth_comm_add_browse.setImageDrawable(getResources().getDrawable(R.drawable.checkedbrowse));

                                                            str_communication_proof_type = "BROWSE";
                                                            //remove common source file from directory
                                                            enableUploadButton(COMM_PROOF_DOC);
                                                        } else if (str_doc.equals(SCORE_CARD_DOC)) {
                                                            mScoreCardFile = fileOutput;

                                                            imgbtn_posp_score_card_camera.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_camera));
                                                            imgbtn_posp_score_card_browse.setImageDrawable(getResources().getDrawable(R.drawable.checkedbrowse));

                                                            str_score_card_proof_type = "BROWSE";

                                                            enableUploadButton(SCORE_CARD_DOC);
                                                        } else if (str_doc.equals(OTHERS_DOC)) {
                                                            mOthersFile = fileOutput;

                                                            imgbtn_aob_auth_others_camera.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_camera));
                                                            imgbtn_aob_auth_others_browse.setImageDrawable(getResources().getDrawable(R.drawable.checkedbrowse));

                                                            str_others_doc_type = "BROWSE";

                                                            enableUploadButton(OTHERS_DOC);
                                                        } else if (str_doc.equals(MOBILE_DECLARATION_DOC)) {
                                                            mMobDeclarationFile = fileOutput;

                                                            imgbtn_posp_declaration_camera.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_camera));
                                                            imgbtn_posp_declaration_browse.setImageDrawable(getResources().getDrawable(R.drawable.checkedbrowse));

                                                            str_declaration_type = "BROWSE";

                                                            enableUploadButton(MOBILE_DECLARATION_DOC);
                                                        } else if (str_doc.equals(NOMINATION_FORM_DOC)) {
                                                            mNominationFile = fileOutput;

                                                            imgbtn_posp_nomination_camera.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_camera));
                                                            imgbtn_posp_nomination_browse.setImageDrawable(getResources().getDrawable(R.drawable.checkedbrowse));

                                                            str_nomination_doc_type = "BROWSE";

                                                            enableUploadButton(NOMINATION_FORM_DOC);
                                                        } else if (str_doc.equals(PAN_DOC)) {
                                                            mPanFile = fileOutput;

                                                            imgbtn_posp_pan_camera.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_camera));
                                                            imgbtn_posp_pan_browse.setImageDrawable(getResources().getDrawable(R.drawable.checkedbrowse));


                                                            str_pan_doc_type = "BROWSE";

                                                            //remove common source file from directory

                                                            enableUploadButton(PAN_DOC);
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
        setContentView(R.layout.activity_posp_ra_document_upload);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.cifenrollment_pf_activity_title);

        if (getIntent().hasExtra("is_dashboard"))
            is_dashboard = getIntent().getBooleanExtra("is_dashboard", false);

        if (getIntent().hasExtra("is_bsm_questions"))
            is_bsm_questions = getIntent().getBooleanExtra("is_bsm_questions", false);

        if (getIntent().hasExtra("is_rejection"))
            is_rejection = getIntent().getBooleanExtra("is_rejection", false);

        if (getIntent().hasExtra("is_from_ho"))
            is_from_ho = getIntent().getBooleanExtra("is_from_ho", false);

        initialisation();

        //non editable with no saving
        //editable
        enableDisableAllFields(!is_dashboard && !is_bsm_questions);

        if (is_rejection)
            setRejectionRecords();
    }

    private void getUserDetails() {
        CommonMethods.UserDetailsValuesModel userDetailsValuesModel = mCommonMethods.setUserDetails(mContext);
        strCIFBDMUserId = userDetailsValuesModel.getStrCIFBDMUserId();
        strCIFBDMEmailId = userDetailsValuesModel.getStrCIFBDMEmailId();
        strCIFBDMMObileNo = userDetailsValuesModel.getStrCIFBDMMObileNo();
    }

    @Override
    public void onBackPressed() {

        if (is_from_ho) {
            Intent i = new Intent(Activity_POSP_RA_DocumentUpload.this, Activity_POSP_RA_Rejection_Remarks.class);
            i.putExtra("enrollment_type", mCommonMethods.str_posp_ra_customer_type);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
        } else {
            Intent intent = new Intent(Activity_POSP_RA_DocumentUpload.this, Activity_POSP_RA_TermsConditionsDeclaration.class);
            if (is_bsm_questions)
                intent.putExtra("is_bsm_questions", is_bsm_questions);
            else if (is_dashboard)
                intent.putExtra("is_dashboard", is_dashboard);
            else if (is_rejection)
                intent.putExtra("is_rejection", is_rejection);

            startActivity(intent);
        }
    }

    public void initialisation() {
        mContext = this;
        mCommonMethods = new CommonMethods();
        mStorageUtils = new StorageUtils();

        mCommonMethods.setApplicationToolbarMenu1(this, "POSP-RA");

        db = new DatabaseHelper(mContext);

        getUserDetails();

        str_doc_upload = new StringBuilder();
        str_final_posp_info = new StringBuilder();

        mParseXML = new ParseXML();

        mCalendar = Calendar.getInstance();

        edt_aob_auth_others = findViewById(R.id.edt_aob_auth_others);

        imgbtn_aob_auth_bank_pass_book_view = findViewById(R.id.imgbtn_aob_auth_bank_pass_book_view);
        imgbtn_aob_auth_bank_pass_book_view.setOnClickListener(this);
        imgbtn_aob_auth_bank_pass_book_camera = findViewById(R.id.imgbtn_aob_auth_bank_pass_book_camera);
        imgbtn_aob_auth_bank_pass_book_camera.setOnClickListener(this);
        imgbtn_aob_auth_bank_pass_book_browse = findViewById(R.id.imgbtn_aob_auth_bank_pass_book_browse);
        imgbtn_aob_auth_bank_pass_book_browse.setOnClickListener(this);
        imgbtn_aob_auth_bank_pass_book_upload = findViewById(R.id.imgbtn_aob_auth_bank_pass_book_upload);
        imgbtn_aob_auth_bank_pass_book_upload.setOnClickListener(this);
        txt_aob_auth_bank_pass_book_remark = findViewById(R.id.txt_aob_auth_bank_pass_book_remark);

        imgbtn_posp_tcc_view = findViewById(R.id.imgbtn_posp_tcc_view);
        imgbtn_posp_tcc_view.setOnClickListener(this);
        imgbtn_posp_tcc_camera = findViewById(R.id.imgbtn_posp_tcc_camera);
        imgbtn_posp_tcc_camera.setOnClickListener(this);
        imgbtn_posp_tcc_browse = findViewById(R.id.imgbtn_posp_tcc_browse);
        imgbtn_posp_tcc_browse.setOnClickListener(this);
        imgbtn_posp_tcc_upload = findViewById(R.id.imgbtn_posp_tcc_upload);
        imgbtn_posp_tcc_upload.setOnClickListener(this);
        txt_posp_tcc_remark = findViewById(R.id.txt_posp_tcc_remark);

        imgbtn_aob_auth_basic_qualification_view = findViewById(R.id.imgbtn_aob_auth_basic_qualification_view);
        imgbtn_aob_auth_basic_qualification_view.setOnClickListener(this);
        imgbtn_aob_auth_basic_qualification_camera = findViewById(R.id.imgbtn_aob_auth_basic_qualification_camera);
        imgbtn_aob_auth_basic_qualification_camera.setOnClickListener(this);
        imgbtn_aob_auth_basic_qualification_browse = findViewById(R.id.imgbtn_aob_auth_basic_qualification_browse);
        imgbtn_aob_auth_basic_qualification_browse.setOnClickListener(this);
        imgbtn_aob_auth_basic_qualification_upload = findViewById(R.id.imgbtn_aob_auth_basic_qualification_upload);
        imgbtn_aob_auth_basic_qualification_upload.setOnClickListener(this);
        txt_aob_auth_basic_qualification_remark = findViewById(R.id.txt_aob_auth_basic_qualification_remark);

        imgbtn_aob_auth_comm_add_view = findViewById(R.id.imgbtn_aob_auth_comm_add_view);
        imgbtn_aob_auth_comm_add_view.setOnClickListener(this);
        imgbtn_aob_auth_comm_add_camera = findViewById(R.id.imgbtn_aob_auth_comm_add_camera);
        imgbtn_aob_auth_comm_add_camera.setOnClickListener(this);
        imgbtn_aob_auth_comm_add_browse = findViewById(R.id.imgbtn_aob_auth_comm_add_browse);
        imgbtn_aob_auth_comm_add_browse.setOnClickListener(this);
        imgbtn_aob_auth_comm_add_upload = findViewById(R.id.imgbtn_aob_auth_comm_add_upload);
        imgbtn_aob_auth_comm_add_upload.setOnClickListener(this);
        txt_aob_auth_comm_add_remark = findViewById(R.id.txt_aob_auth_comm_add_remark);

        imgbtn_posp_score_card_view = findViewById(R.id.imgbtn_posp_score_card_view);
        imgbtn_posp_score_card_view.setOnClickListener(this);
        imgbtn_posp_score_card_camera = findViewById(R.id.imgbtn_posp_score_card_camera);
        imgbtn_posp_score_card_camera.setOnClickListener(this);
        imgbtn_posp_score_card_browse = findViewById(R.id.imgbtn_posp_score_card_browse);
        imgbtn_posp_score_card_browse.setOnClickListener(this);
        imgbtn_posp_score_cardupload = findViewById(R.id.imgbtn_posp_score_cardupload);
        imgbtn_posp_score_cardupload.setOnClickListener(this);
        txt_posp_score_card_remark = findViewById(R.id.txt_posp_score_card_remark);

        imgbtn_posp_nomination_view = findViewById(R.id.imgbtn_posp_nomination_view);
        imgbtn_posp_nomination_view.setOnClickListener(this);
        imgbtn_posp_nomination_camera = findViewById(R.id.imgbtn_posp_nomination_camera);
        imgbtn_posp_nomination_camera.setOnClickListener(this);
        imgbtn_posp_nomination_browse = findViewById(R.id.imgbtn_posp_nomination_browse);
        imgbtn_posp_nomination_browse.setOnClickListener(this);
        imgbtn_posp_nomination_upload = findViewById(R.id.imgbtn_posp_nomination_upload);
        imgbtn_posp_nomination_upload.setOnClickListener(this);
        txt_posp_nomination_remark = findViewById(R.id.txt_posp_nomination_remark);

        imgbtn_posp_pan_view = findViewById(R.id.imgbtn_posp_pan_view);
        imgbtn_posp_pan_view.setOnClickListener(this);
        imgbtn_posp_pan_camera = findViewById(R.id.imgbtn_posp_pan_camera);
        imgbtn_posp_pan_camera.setOnClickListener(this);
        imgbtn_posp_pan_browse = findViewById(R.id.imgbtn_posp_pan_browse);
        imgbtn_posp_pan_browse.setOnClickListener(this);
        imgbtn_posp_pan_upload = findViewById(R.id.imgbtn_posp_pan_upload);
        imgbtn_posp_pan_upload.setOnClickListener(this);
        txt_posp_pan_remark = findViewById(R.id.txt_posp_pan_remark);

        imgbtn_aob_auth_others_view = findViewById(R.id.imgbtn_aob_auth_others_view);
        imgbtn_aob_auth_others_view.setOnClickListener(this);
        imgbtn_aob_auth_others_camera = findViewById(R.id.imgbtn_aob_auth_others_camera);
        imgbtn_aob_auth_others_camera.setOnClickListener(this);
        imgbtn_aob_auth_others_browse = findViewById(R.id.imgbtn_aob_auth_others_browse);
        imgbtn_aob_auth_others_browse.setOnClickListener(this);
        imgbtn_aob_auth_others_upload = findViewById(R.id.imgbtn_aob_auth_others_upload);
        imgbtn_aob_auth_others_upload.setOnClickListener(this);
        txt_aob_auth_others_remark = findViewById(R.id.txt_aob_auth_others_remark);

        imgbtn_posp_declaration_view = findViewById(R.id.imgbtn_posp_declaration_view);
        imgbtn_posp_declaration_view.setOnClickListener(this);
        imgbtn_posp_declaration_camera = findViewById(R.id.imgbtn_posp_declaration_camera);
        imgbtn_posp_declaration_camera.setOnClickListener(this);
        imgbtn_posp_declaration_browse = findViewById(R.id.imgbtn_posp_declaration_browse);
        imgbtn_posp_declaration_browse.setOnClickListener(this);
        imgbtn_posp_declaration_upload = findViewById(R.id.imgbtn_posp_declaration_upload);
        imgbtn_posp_declaration_upload.setOnClickListener(this);
        txt_posp_declaration_remark = findViewById(R.id.txt_posp_declaration_remark);

        btn_aob_auth_pan_submit = findViewById(R.id.btn_aob_auth_pan_submit);
        btn_aob_auth_pan_submit.setOnClickListener(this);

        btn_aob_auth_pan_back = findViewById(R.id.btn_aob_auth_pan_back);
        btn_aob_auth_pan_back.setOnClickListener(this);

        ll_posp_requirement_upload = findViewById(R.id.ll_posp_requirement_upload);
        if (is_rejection) {
            ll_posp_requirement_upload.setVisibility(View.VISIBLE);
        } else {
            ll_posp_requirement_upload.setVisibility(View.GONE);
        }

        ArrayList<Pojo_POSP_RA> lst = db.get_posp_ra_details_by_ID(Activity_POSP_RA_Authentication.row_details);
        if (lst.size() > 0) {

            String str_doc_upload = lst.get(0).getStr_doc_upload();
            str_doc_upload = str_doc_upload == null ? "" : str_doc_upload;
            str_pan_no = lst.get(0).getStr_pan_no();
            str_um_code = lst.get(0).getStr_created_by();

            if (!str_doc_upload.equals("")) {

                String str_bank_pass_type = mParseXML.parseXmlTag(str_doc_upload, "doc_upload_bank_pass_type");
                str_bank_pass_type = str_bank_pass_type == null ? "" : str_bank_pass_type;
                String str_bank_pass_is_uploaded = mParseXML.parseXmlTag(str_doc_upload, "doc_upload_bank_pass_is_upload");
                str_bank_pass_is_uploaded = str_bank_pass_is_uploaded == null ? "" : str_bank_pass_is_uploaded;

                str_tcc_proof_type = mParseXML.parseXmlTag(str_doc_upload, "doc_upload_tcc_type");
                str_tcc_proof_type = str_tcc_proof_type == null ? "" : str_tcc_proof_type;
                String str_tcc_proof_is_uploaded = mParseXML.parseXmlTag(str_doc_upload, "doc_upload_tcc_is_upload");
                str_tcc_proof_is_uploaded = str_tcc_proof_is_uploaded == null ? "" : str_tcc_proof_is_uploaded;

                String str_education_proof_type = mParseXML.parseXmlTag(str_doc_upload, "doc_upload_basic_qualification_type");
                str_education_proof_type = str_education_proof_type == null ? "" : str_education_proof_type;
                String str_education_proof_is_uploaded = mParseXML.parseXmlTag(str_doc_upload, "doc_upload_basic_qualification_is_upload");
                str_education_proof_is_uploaded = str_education_proof_is_uploaded == null ? "" : str_education_proof_is_uploaded;

                str_communication_proof_type = mParseXML.parseXmlTag(str_doc_upload, "doc_upload_comm_address_type");
                str_communication_proof_type = str_communication_proof_type == null ? "" : str_communication_proof_type;
                String str_communication_proof_is_uploaded = mParseXML.parseXmlTag(str_doc_upload, "doc_upload_comm_address_is_upload");
                str_communication_proof_is_uploaded = str_communication_proof_is_uploaded == null ? "" : str_communication_proof_is_uploaded;

                String str_score_card_type = mParseXML.parseXmlTag(str_doc_upload, "doc_upload_score_card_type");
                str_score_card_type = str_score_card_type == null ? "" : str_score_card_type;
                String str_score_card_is_uploaded = mParseXML.parseXmlTag(str_doc_upload, "doc_upload_score_card_is_upload");
                str_score_card_is_uploaded = str_score_card_is_uploaded == null ? "" : str_score_card_is_uploaded;

                str_nomination_doc_type = mParseXML.parseXmlTag(str_doc_upload, "doc_upload_nomination_type");
                str_nomination_doc_type = str_nomination_doc_type == null ? "" : str_nomination_doc_type;
                String str_nomination_is_uploaded = mParseXML.parseXmlTag(str_doc_upload, "doc_upload_nomination_is_upload");
                str_nomination_is_uploaded = str_nomination_is_uploaded == null ? "" : str_nomination_is_uploaded;

                str_pan_doc_type = mParseXML.parseXmlTag(str_doc_upload, "doc_upload_pan_type");
                str_pan_doc_type = str_nomination_doc_type == null ? "" : str_nomination_doc_type;
                String str_pan_is_uploaded = mParseXML.parseXmlTag(str_doc_upload, "doc_upload_pan_is_upload");
                str_pan_is_uploaded = str_pan_is_uploaded == null ? "" : str_pan_is_uploaded;

                String str_others_doc_type = mParseXML.parseXmlTag(str_doc_upload, "doc_upload_others_type");
                str_others_doc_type = str_others_doc_type == null ? "" : str_others_doc_type;
                String str_others_document = mParseXML.parseXmlTag(str_doc_upload, "doc_upload_others_document");
                str_others_document = str_others_document == null ? "" : str_others_document;
                String str_others_is_uploaded = mParseXML.parseXmlTag(str_doc_upload, "doc_upload_others_is_upload");
                str_others_is_uploaded = str_others_is_uploaded == null ? "" : str_others_is_uploaded;

                String str_declaration_type = mParseXML.parseXmlTag(str_doc_upload, "doc_upload_declaration_type");
                str_declaration_type = str_declaration_type == null ? "" : str_declaration_type;
                String str_declaration_is_uploaded = mParseXML.parseXmlTag(str_doc_upload, "doc_upload_declaration_is_upload");
                str_declaration_is_uploaded = str_declaration_is_uploaded == null ? "" : str_declaration_is_uploaded;

                if (str_declaration_type.equals("CAMERA")) {

                    imgbtn_posp_declaration_camera.setImageDrawable(getResources().getDrawable(R.drawable.checkedcamera));
                    imgbtn_posp_declaration_browse.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_browsedoc));

                } else if (str_declaration_type.equals("BROWSE")) {
                    imgbtn_posp_declaration_browse.setImageDrawable(getResources().getDrawable(R.drawable.checkedbrowse));
                    imgbtn_posp_declaration_camera.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_camera));
                }

                if (str_declaration_is_uploaded.equals("true")) {
                    is_declaration_uploaded = true;
                    imgbtn_posp_declaration_upload.setImageDrawable(getResources().getDrawable(R.drawable.checkedupload));
                } else {
                    is_declaration_uploaded = false;
                    imgbtn_posp_declaration_upload.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_uploaddoc));
                }

                if (str_bank_pass_type.equals("CAMERA")) {

                    imgbtn_aob_auth_bank_pass_book_camera.setImageDrawable(getResources().getDrawable(R.drawable.checkedcamera));
                    imgbtn_aob_auth_bank_pass_book_browse.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_browsedoc));

                } else if (str_bank_pass_type.equals("BROWSE")) {
                    imgbtn_aob_auth_bank_pass_book_camera.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_camera));
                    imgbtn_aob_auth_bank_pass_book_browse.setImageDrawable(getResources().getDrawable(R.drawable.checkedbrowse));
                }

                if (str_bank_pass_is_uploaded.equals("true")) {
                    is_bank_passbook_uploaded = true;
                    imgbtn_aob_auth_bank_pass_book_upload.setImageDrawable(getResources().getDrawable(R.drawable.checkedupload));
                } else {
                    is_bank_passbook_uploaded = false;
                    imgbtn_aob_auth_bank_pass_book_upload.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_uploaddoc));
                }

                if (str_tcc_proof_type.equals("CAMERA")) {

                    imgbtn_posp_tcc_camera.setImageDrawable(getResources().getDrawable(R.drawable.checkedcamera));
                    imgbtn_posp_tcc_browse.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_browsedoc));

                } else if (str_tcc_proof_type.equals("BROWSE")) {
                    imgbtn_posp_tcc_camera.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_camera));
                    imgbtn_posp_tcc_browse.setImageDrawable(getResources().getDrawable(R.drawable.checkedbrowse));
                }

                if (str_tcc_proof_is_uploaded.equals("true")) {
                    is_tcc_proof_uploaded = true;
                    imgbtn_posp_tcc_upload.setImageDrawable(getResources().getDrawable(R.drawable.checkedupload));
                } else {
                    is_tcc_proof_uploaded = false;
                    imgbtn_posp_tcc_upload.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_uploaddoc));
                }

                if (str_education_proof_type.equals("CAMERA")) {

                    imgbtn_aob_auth_basic_qualification_camera.setImageDrawable(getResources().getDrawable(R.drawable.checkedcamera));
                    imgbtn_aob_auth_basic_qualification_browse.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_browsedoc));

                } else if (str_education_proof_type.equals("BROWSE")) {
                    imgbtn_aob_auth_basic_qualification_camera.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_camera));
                    imgbtn_aob_auth_basic_qualification_browse.setImageDrawable(getResources().getDrawable(R.drawable.checkedbrowse));
                }

                if (str_education_proof_is_uploaded.equals("true")) {
                    is_education_proof_uploaded = true;
                    imgbtn_aob_auth_basic_qualification_upload.setImageDrawable(getResources().getDrawable(R.drawable.checkedupload));
                } else {
                    is_education_proof_uploaded = false;
                    imgbtn_aob_auth_basic_qualification_upload.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_uploaddoc));
                }

                if (str_communication_proof_type.equals("CAMERA")) {

                    imgbtn_aob_auth_comm_add_camera.setImageDrawable(getResources().getDrawable(R.drawable.checkedcamera));
                    imgbtn_aob_auth_comm_add_browse.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_browsedoc));

                } else if (str_communication_proof_type.equals("BROWSE")) {
                    imgbtn_aob_auth_comm_add_camera.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_camera));
                    imgbtn_aob_auth_comm_add_browse.setImageDrawable(getResources().getDrawable(R.drawable.checkedbrowse));
                }

                if (str_communication_proof_is_uploaded.equals("true")) {
                    is_communication_proof_uploaded = true;

                    imgbtn_aob_auth_comm_add_upload.setImageDrawable(getResources().getDrawable(R.drawable.checkedupload));
                } else {
                    is_communication_proof_uploaded = false;

                    imgbtn_aob_auth_comm_add_upload.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_uploaddoc));
                }

                if (str_score_card_type.equals("CAMERA")) {

                    imgbtn_posp_score_card_camera.setImageDrawable(getResources().getDrawable(R.drawable.checkedcamera));
                    imgbtn_posp_score_card_browse.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_browsedoc));

                } else if (str_score_card_type.equals("BROWSE")) {
                    imgbtn_posp_score_card_camera.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_camera));
                    imgbtn_posp_score_card_browse.setImageDrawable(getResources().getDrawable(R.drawable.checkedbrowse));
                }

                if (str_score_card_is_uploaded.equals("true")) {
                    is_score_card_proof_uploaded = true;
                    imgbtn_posp_score_cardupload.setImageDrawable(getResources().getDrawable(R.drawable.checkedupload));
                } else {
                    is_score_card_proof_uploaded = false;
                    imgbtn_posp_score_cardupload.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_uploaddoc));
                }

                if (str_nomination_doc_type.equals("CAMERA")) {
                    imgbtn_posp_nomination_camera.setImageDrawable(getResources().getDrawable(R.drawable.checkedcamera));
                    imgbtn_posp_nomination_browse.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_browsedoc));
                } else if (str_nomination_doc_type.equals("BROWSE")) {
                    imgbtn_posp_nomination_camera.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_camera));
                    imgbtn_posp_nomination_browse.setImageDrawable(getResources().getDrawable(R.drawable.checkedbrowse));
                }

                if (str_nomination_is_uploaded.equals("true")) {
                    is_nomination_doc_uploaded = true;
                    imgbtn_posp_nomination_upload.setImageDrawable(getResources().getDrawable(R.drawable.checkedupload));
                } else {
                    is_nomination_doc_uploaded = false;
                    imgbtn_posp_nomination_upload.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_uploaddoc));
                }

                if (str_pan_doc_type.equals("CAMERA")) {
                    imgbtn_posp_pan_camera.setImageDrawable(getResources().getDrawable(R.drawable.checkedcamera));
                    imgbtn_posp_pan_browse.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_browsedoc));
                } else if (str_pan_doc_type.equals("BROWSE")) {
                    imgbtn_posp_pan_camera.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_camera));
                    imgbtn_posp_pan_browse.setImageDrawable(getResources().getDrawable(R.drawable.checkedbrowse));
                }

                if (str_pan_is_uploaded.equals("true")) {
                    is_pan_doc_uploaded = true;
                    imgbtn_posp_pan_upload.setImageDrawable(getResources().getDrawable(R.drawable.checkedupload));
                } else {
                    is_pan_doc_uploaded = false;
                    imgbtn_posp_pan_upload.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_uploaddoc));
                }

                if (str_others_doc_type.equals("CAMERA")) {

                    imgbtn_aob_auth_others_camera.setImageDrawable(getResources().getDrawable(R.drawable.checkedcamera));
                    imgbtn_aob_auth_others_browse.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_browsedoc));

                } else if (str_others_doc_type.equals("BROWSE")) {
                    imgbtn_aob_auth_others_camera.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_camera));
                    imgbtn_aob_auth_others_browse.setImageDrawable(getResources().getDrawable(R.drawable.checkedbrowse));
                }

                edt_aob_auth_others.setText(str_others_document);

                if (str_others_is_uploaded.equals("true")) {
                    is_others_doc_uploaded = true;
                    imgbtn_aob_auth_others_upload.setImageDrawable(getResources().getDrawable(R.drawable.checkedupload));
                } else {
                    is_others_doc_uploaded = false;
                    imgbtn_aob_auth_others_upload.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_uploaddoc));
                }
            }
        }
    }

    /*public void open_ocr_activity(String str_doc_type, String str_ocr_type) {

        str_doc = str_doc_type;
        OCR_TYPE = str_ocr_type;

        Intent intent = new Intent(Activity_POSP_RA_DocumentUpload.this, OcrActivity.class);
        startActivityForResult(intent, REQUEST_OCR);
    }*/

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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CAPTURE_DOCUMENT && resultCode == RESULT_OK) {

            CompressImage.compressImage(mCapturedImgFile.getPath());

            long size = mCapturedImgFile.length();
            double kilobyte = size / 1024;

            //2 MB valiadation
            if (kilobyte < mCommonMethods.FILE_UPLOAD_RESTRICT_SIZE) {

                String imageFileName = "";
                if (str_doc.equals(PAN_DOC))
                    imageFileName = str_pan_no + ".pdf";
                else
                    imageFileName = str_pan_no + "_" + str_doc + ".pdf";


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
                } else if (str_doc.equals(TCC_PROOF_DOC)) {
                        /*mTCCFile = mCommonMethods.createScopedStorageAllFiles(mContext,
                                imageFileName, Environment.DIRECTORY_DOCUMENTS);*/
                    mTCCFile = mStorageUtils.createFileToAppSpecificDir(mContext, imageFileName);
                    if (obj.createAOBDOcumentPdf(mTCCFile, mCapturedImgFile, "TCC Document")) {

                        imgbtn_posp_tcc_camera.setImageDrawable(getResources().getDrawable(R.drawable.checkedcamera));
                        imgbtn_posp_tcc_browse.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_browsedoc));

                        str_tcc_proof_type = "CAMERA";

                        //remove common source file from directory
                        mCapturedImgFile.delete();
                        enableUploadButton(TCC_PROOF_DOC);

                    } else {
                        mCommonMethods.showToast(mContext, "File Not Found");
                    }
                } else if (str_doc.equals(EDUCATION_PROOF_DOC)) {
                        /*mEducationFile = mCommonMethods.createScopedStorageAllFiles(mContext,
                                imageFileName, Environment.DIRECTORY_DOCUMENTS);*/
                    mEducationFile = mStorageUtils.createFileToAppSpecificDir(mContext, imageFileName);
                    if (obj.createAOBDOcumentPdf(mEducationFile, mCapturedImgFile, "Education Document")) {

                        imgbtn_aob_auth_basic_qualification_camera.setImageDrawable(getResources().getDrawable(R.drawable.checkedcamera));
                        imgbtn_aob_auth_basic_qualification_browse.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_browsedoc));

                        str_education_proof_type = "CAMERA";

                        //remove common source file from directory
                        mCapturedImgFile.delete();
                        enableUploadButton(EDUCATION_PROOF_DOC);

                    } else {
                        mCommonMethods.showToast(mContext, "File Not Found");
                    }
                } else if (str_doc.equals(COMM_PROOF_DOC)) {
                    //mCommunicationFile = new File(folder.getPath() + File.separator + imageFileName);
                    mCommunicationFile = mStorageUtils.createFileToAppSpecificDir(mContext, imageFileName);

                    if (obj.createAOBDOcumentPdf(mCommunicationFile, mCapturedImgFile, "Communication Document")) {

                        imgbtn_aob_auth_comm_add_camera.setImageDrawable(getResources().getDrawable(R.drawable.checkedcamera));
                        imgbtn_aob_auth_comm_add_browse.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_browsedoc));

                        str_communication_proof_type = "CAMERA";

                        //remove common source file from directory
                        mCapturedImgFile.delete();
                        enableUploadButton(COMM_PROOF_DOC);

                    } else {
                        mCommonMethods.showToast(mContext, "File Not Found");
                    }
                } else if (str_doc.equals(SCORE_CARD_DOC)) {
                        /*mScoreCardFile = mCommonMethods.createScopedStorageAllFiles(mContext,
                                imageFileName, Environment.DIRECTORY_DOCUMENTS);*/
                    mScoreCardFile = mStorageUtils.createFileToAppSpecificDir(mContext, imageFileName);

                    if (obj.createAOBDOcumentPdf(mScoreCardFile, mCapturedImgFile, "Score Card Document")) {

                        imgbtn_posp_score_card_camera.setImageDrawable(getResources().getDrawable(R.drawable.checkedcamera));
                        imgbtn_posp_score_card_browse.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_browsedoc));

                        str_score_card_proof_type = "CAMERA";

                        //remove common source file from directory
                        mCapturedImgFile.delete();
                        enableUploadButton(SCORE_CARD_DOC);

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
                } else if (str_doc.equals(MOBILE_DECLARATION_DOC)) {

                        /*mMobDeclarationFile = mCommonMethods.createScopedStorageAllFiles(mContext,
                                imageFileName, Environment.DIRECTORY_DOCUMENTS);*/
                    mMobDeclarationFile = mStorageUtils.createFileToAppSpecificDir(mContext, imageFileName);

                    if (obj.createAOBDOcumentPdf(mMobDeclarationFile, mCapturedImgFile, "Mobile Declaration Document")) {

                        imgbtn_posp_declaration_camera.setImageDrawable(getResources().getDrawable(R.drawable.checkedcamera));
                        imgbtn_posp_declaration_browse.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_browsedoc));

                        str_declaration_type = "CAMERA";

                        //remove common source file from directory
                        mCapturedImgFile.delete();
                        enableUploadButton(MOBILE_DECLARATION_DOC);

                    } else {
                        mCommonMethods.showToast(mContext, "File Not Found");
                    }
                } else if (str_doc.equals(NOMINATION_FORM_DOC)) {
                        /*mNominationFile = mCommonMethods.createScopedStorageAllFiles(mContext,
                                imageFileName, Environment.DIRECTORY_DOCUMENTS);*/
                    mNominationFile = mStorageUtils.createFileToAppSpecificDir(mContext, imageFileName);

                    if (obj.createAOBDOcumentPdf(mNominationFile, mCapturedImgFile, "Nomination Form Document")) {

                        imgbtn_posp_nomination_camera.setImageDrawable(getResources().getDrawable(R.drawable.checkedcamera));
                        imgbtn_posp_nomination_browse.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_browsedoc));

                        str_nomination_doc_type = "CAMERA";

                        //remove common source file from directory
                        mCapturedImgFile.delete();
                        enableUploadButton(NOMINATION_FORM_DOC);

                    } else {
                        mCommonMethods.showToast(mContext, "File Not Found");
                    }
                } else if (str_doc.equals(PAN_DOC)) {

                    //mPanFile = new File(folder, File.separator + imageFileName);
                    mPanFile = mStorageUtils.createFileToAppSpecificDir(mContext, imageFileName);

                    if (obj.createAOBDOcumentPdf(mPanFile, mCapturedImgFile, "PAN Document")) {

                        imgbtn_posp_pan_camera.setImageDrawable(getResources().getDrawable(R.drawable.checkedcamera));
                        imgbtn_posp_pan_browse.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_browsedoc));

                        str_pan_doc_type = "CAMERA";

                        //remove common source file from directory
                        mCapturedImgFile.delete();
                        enableUploadButton(PAN_DOC);

                    } else {
                        mCommonMethods.showToast(mContext, "File Not Found");
                    }

                }
            } else {
                mCommonMethods.showMessageDialog(mContext, mCommonMethods.FILE_UPLOAD_RESTRICT_SIZE_MSG);
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

                                String imageFileName = "";
                                if (str_doc.equals(PAN_DOC))
                                    imageFileName = str_pan_no + ".pdf";
                                else
                                    imageFileName = str_pan_no + "_" + str_doc + ".pdf";

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
                                } else if (str_doc.equals(TCC_PROOF_DOC)) {
                                    mTCCFile = new File(folder.getPath() + File.separator + imageFileName);
                                    if (obj.createAOBDOcumentPdf(mTCCFile, imagePath, "TCC Document")) {
                                        if (OCR_TYPE.equals("CAMERA")) {
                                            imgbtn_posp_tcc_camera.setImageDrawable(getResources().getDrawable(R.drawable.checkedcamera));
                                            imgbtn_posp_tcc_browse.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_browsedoc));
                                        } else if (OCR_TYPE.equals("BROWSE")) {
                                            imgbtn_posp_tcc_camera.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_camera));
                                            imgbtn_posp_tcc_browse.setImageDrawable(getResources().getDrawable(R.drawable.checkedbrowse));
                                        }

                                        str_tcc_proof_type = OCR_TYPE;

                                        //remove common source file from directory
                                        //mSourceFile.delete();
                                        enableUploadButton(TCC_PROOF_DOC);

                                    } else {
                                        mCommonMethods.showToast(mContext, "File Not Found");
                                    }
                                } else if (str_doc.equals(EDUCATION_PROOF_DOC)) {
                                    mEducationFile = new File(folder.getPath() + File.separator + imageFileName);

                                    if (obj.createAOBDOcumentPdf(mEducationFile, imagePath, "Education Document")) {

                                        if (OCR_TYPE.equals("CAMERA")) {
                                            imgbtn_aob_auth_basic_qualification_camera.setImageDrawable(getResources().getDrawable(R.drawable.checkedcamera));
                                            imgbtn_aob_auth_basic_qualification_browse.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_browsedoc));
                                        } else if (OCR_TYPE.equals("BROWSE")) {
                                            imgbtn_aob_auth_basic_qualification_camera.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_camera));
                                            imgbtn_aob_auth_basic_qualification_browse.setImageDrawable(getResources().getDrawable(R.drawable.checkedbrowse));
                                        }

                                        str_education_proof_type = OCR_TYPE;

                                        //remove common source file from directory
                                        //mSourceFile.delete();
                                        enableUploadButton(EDUCATION_PROOF_DOC);

                                    } else {
                                        mCommonMethods.showToast(mContext, "File Not Found");
                                    }
                                } else if (str_doc.equals(COMM_PROOF_DOC)) {
                                    mCommunicationFile = new File(folder.getPath() + File.separator + imageFileName);

                                    if (obj.createAOBDOcumentPdf(mCommunicationFile, imagePath, "Communication Document")) {

                                        if (OCR_TYPE.equals("CAMERA")) {
                                            imgbtn_aob_auth_comm_add_camera.setImageDrawable(getResources().getDrawable(R.drawable.checkedcamera));
                                            imgbtn_aob_auth_comm_add_browse.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_browsedoc));
                                        } else if (OCR_TYPE.equals("BROWSE")) {
                                            imgbtn_aob_auth_comm_add_camera.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_camera));
                                            imgbtn_aob_auth_comm_add_browse.setImageDrawable(getResources().getDrawable(R.drawable.checkedbrowse));
                                        }

                                        str_communication_proof_type = OCR_TYPE;

                                        //remove common source file from directory
                                        //mSourceFile.delete();
                                        enableUploadButton(COMM_PROOF_DOC);

                                    } else {
                                        mCommonMethods.showToast(mContext, "File Not Found");
                                    }
                                } else if (str_doc.equals(SCORE_CARD_DOC)) {
                                    mScoreCardFile = new File(folder.getPath() + File.separator + imageFileName);

                                    if (obj.createAOBDOcumentPdf(mScoreCardFile, imagePath, "Score Card Document")) {

                                        if (OCR_TYPE.equals("CAMERA")) {
                                            imgbtn_posp_score_card_camera.setImageDrawable(getResources().getDrawable(R.drawable.checkedcamera));
                                            imgbtn_posp_score_card_browse.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_browsedoc));
                                        } else if (OCR_TYPE.equals("BROWSE")) {
                                            imgbtn_posp_score_card_camera.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_camera));
                                            imgbtn_posp_score_card_browse.setImageDrawable(getResources().getDrawable(R.drawable.checkedbrowse));
                                        }

                                        str_score_card_proof_type = OCR_TYPE;

                                        //remove common source file from directory
                                        //mSourceFile.delete();
                                        enableUploadButton(SCORE_CARD_DOC);

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
                                } else if (str_doc.equals(MOBILE_DECLARATION_DOC)) {

                                    mMobDeclarationFile = new File(folder, File.separator + imageFileName);

                                    if (obj.createAOBDOcumentPdf(mMobDeclarationFile, imagePath, "Mobile Declaration Document")) {

                                        if (OCR_TYPE.equals("CAMERA")) {
                                            imgbtn_posp_declaration_camera.setImageDrawable(getResources().getDrawable(R.drawable.checkedcamera));
                                            imgbtn_posp_declaration_browse.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_browsedoc));
                                        } else if (OCR_TYPE.equals("BROWSE")) {
                                            imgbtn_posp_declaration_camera.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_camera));
                                            imgbtn_posp_declaration_browse.setImageDrawable(getResources().getDrawable(R.drawable.checkedbrowse));
                                        }

                                        str_declaration_type = OCR_TYPE;

                                        //remove common source file from directory
                                        //mSourceFile.delete();
                                        enableUploadButton(MOBILE_DECLARATION_DOC);

                                    } else {
                                        mCommonMethods.showToast(mContext, "File Not Found");
                                    }
                                } else if (str_doc.equals(NOMINATION_FORM_DOC)) {
                                    mNominationFile = new File(folder, File.separator + imageFileName);

                                    if (obj.createAOBDOcumentPdf(mNominationFile, imagePath, "Nomination Form Document")) {

                                        if (OCR_TYPE.equals("CAMERA")) {
                                            imgbtn_posp_nomination_camera.setImageDrawable(getResources().getDrawable(R.drawable.checkedcamera));
                                            imgbtn_posp_nomination_browse.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_browsedoc));
                                        } else if (OCR_TYPE.equals("BROWSE")) {
                                            imgbtn_posp_nomination_camera.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_camera));
                                            imgbtn_posp_nomination_browse.setImageDrawable(getResources().getDrawable(R.drawable.checkedbrowse));
                                        }

                                        str_nomination_doc_type = OCR_TYPE;

                                        //remove common source file from directory
                                        //mSourceFile.delete();
                                        enableUploadButton(NOMINATION_FORM_DOC);

                                    } else {
                                        mCommonMethods.showToast(mContext, "File Not Found");
                                    }
                                } else if (str_doc.equals(PAN_DOC)) {
                                    if (DocumentType.toLowerCase().equalsIgnoreCase("Pancard".toLowerCase())
                                            || DocumentType.toLowerCase().equalsIgnoreCase("PAN".toLowerCase())) {

                                        mPanFile = new File(folder, File.separator + imageFileName);

                                        if (obj.createAOBDOcumentPdf(mPanFile, imagePath, "PAN Document")) {

                                            if (OCR_TYPE.equals("CAMERA")) {
                                                imgbtn_posp_pan_camera.setImageDrawable(getResources().getDrawable(R.drawable.checkedcamera));
                                                imgbtn_posp_pan_browse.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_browsedoc));
                                            } else if (OCR_TYPE.equals("BROWSE")) {
                                                imgbtn_posp_pan_camera.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_camera));
                                                imgbtn_posp_pan_browse.setImageDrawable(getResources().getDrawable(R.drawable.checkedbrowse));
                                            }

                                            str_pan_doc_type = OCR_TYPE;

                                            //remove common source file from directory
                                            //mSourceFile.delete();
                                            enableUploadButton(PAN_DOC);

                                        } else {
                                            mCommonMethods.showToast(mContext, "File Not Found");
                                        }
                                    } else {
                                        mCommonMethods.showMessageDialog(mContext, "Please " + OCR_TYPE + " Correct Pan..");
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
                mCommonMethods.showToast(mContext, "Data not receive");
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
                    mCommonMethods.showToast(mContext, "Please Browse or Capture Bank Proof Document First!");
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
                if (mBankPassbookFile != null) {
                    createSoapRequestToUploadDoc(mBankPassbookFile);
                } else {
                    mCommonMethods.showMessageDialog(mContext, "File Not Found..");
                }
                break;

            case R.id.imgbtn_posp_tcc_view:

                if (mTCCFile != null) {
                    try {
                        mCommonMethods.openAllDocs(mContext, mTCCFile);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                } else {
                    mCommonMethods.showToast(mContext, "Please Browse or Capture TCC Document First!");
                }

                break;

            case R.id.imgbtn_posp_tcc_camera:

                str_doc = TCC_PROOF_DOC;
                capture_document();
                //capture_docs(TCC_PROOF_DOC, "CAMERA");

                break;

            case R.id.imgbtn_posp_tcc_browse:

                str_doc = TCC_PROOF_DOC;
                browse_docs();
                //capture_docs(TCC_PROOF_DOC, "BROWSE");

                break;

            case R.id.imgbtn_posp_tcc_upload:
                if (mTCCFile != null) {
                    createSoapRequestToUploadDoc(mTCCFile);
                } else {
                    mCommonMethods.showMessageDialog(mContext, "File Not Found..");
                }
                break;

            case R.id.imgbtn_aob_auth_basic_qualification_view:

                if (mEducationFile != null) {
                    try {
                        mCommonMethods.openAllDocs(mContext, mEducationFile);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                } else {
                    mCommonMethods.showToast(mContext, "Please Browse or Capture Education Proof Document First!");
                }

                break;

            case R.id.imgbtn_aob_auth_basic_qualification_camera:

                str_doc = EDUCATION_PROOF_DOC;
                capture_document();
                //capture_docs(EDUCATION_PROOF_DOC, "CAMERA");

                break;
            case R.id.imgbtn_aob_auth_basic_qualification_browse:

                str_doc = EDUCATION_PROOF_DOC;
                browse_docs();
                //capture_docs(EDUCATION_PROOF_DOC, "BROWSE");

                break;

            case R.id.imgbtn_aob_auth_basic_qualification_upload:
                if (mEducationFile != null) {
                    createSoapRequestToUploadDoc(mEducationFile);
                } else {
                    mCommonMethods.showMessageDialog(mContext, "File Not Found..");
                }
                break;

            case R.id.imgbtn_aob_auth_comm_add_view:

                if (mCommunicationFile != null) {
                    try {
                        mCommonMethods.openAllDocs(mContext, mCommunicationFile);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                } else {
                    mCommonMethods.showToast(mContext, "Please Browse or Capture Communication Address Document First!");
                }

                break;

            case R.id.imgbtn_aob_auth_comm_add_camera:

                //open_ocr_activity(COMM_PROOF_DOC, "CAMERA");
                str_doc = COMM_PROOF_DOC;
                capture_document();

                break;
            case R.id.imgbtn_aob_auth_comm_add_browse:

                //open_ocr_activity(COMM_PROOF_DOC, "BROWSE");
                str_doc = COMM_PROOF_DOC;
                browse_docs();

                break;

            case R.id.imgbtn_aob_auth_comm_add_upload:
                if (mCommunicationFile != null) {
                    createSoapRequestToUploadDoc(mCommunicationFile);
                } else {
                    mCommonMethods.showMessageDialog(mContext, "File Not Found..");
                }
                break;

            case R.id.imgbtn_posp_score_card_view:

                if (mScoreCardFile != null) {
                    try {
                        mCommonMethods.openAllDocs(mContext, mScoreCardFile);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                } else {
                    mCommonMethods.showToast(mContext, "Please Browse or Capture Score Card Document First!");
                }

                break;

            case R.id.imgbtn_posp_score_card_camera:

                str_doc = SCORE_CARD_DOC;
                capture_document();
                //capture_docs(SCORE_CARD_DOC, "CAMERA");

                break;
            case R.id.imgbtn_posp_score_card_browse:

                str_doc = SCORE_CARD_DOC;
                browse_docs();
                //open_ocr_activity(SCORE_CARD_DOC, "BROWSE");

                break;

            case R.id.imgbtn_posp_score_cardupload:
                if (mScoreCardFile != null) {
                    createSoapRequestToUploadDoc(mScoreCardFile);
                } else {
                    mCommonMethods.showMessageDialog(mContext, "File Not Found..");
                }
                break;

            case R.id.imgbtn_aob_auth_others_view:

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
                if (mOthersFile != null) {
                    createSoapRequestToUploadDoc(mOthersFile);
                } else {
                    mCommonMethods.showMessageDialog(mContext, "File Not Found..");
                }
                break;

            case R.id.imgbtn_posp_declaration_view:

                if (mMobDeclarationFile != null) {
                    try {
                        mCommonMethods.openAllDocs(mContext, mMobDeclarationFile);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                } else {
                    mCommonMethods.showToast(mContext, "Please Browse or Capture Mobile Declaration Document First!");
                }

                break;

            case R.id.imgbtn_posp_declaration_camera:

                str_doc = MOBILE_DECLARATION_DOC;
                capture_document();
                //capture_docs(MOBILE_DECLARATION_DOC, "CAMERA");
                break;

            case R.id.imgbtn_posp_declaration_browse:

                str_doc = MOBILE_DECLARATION_DOC;
                browse_docs();
                //capture_docs(MOBILE_DECLARATION_DOC, "BROWSE");
                break;

            case R.id.imgbtn_posp_declaration_upload:

                if (mMobDeclarationFile != null) {
                    createSoapRequestToUploadDoc(mMobDeclarationFile);
                } else {
                    mCommonMethods.showMessageDialog(mContext, "File Not Found..");
                }

                break;

            case R.id.imgbtn_posp_nomination_view:

                if (mNominationFile != null) {
                    try {
                        mCommonMethods.openAllDocs(mContext, mNominationFile);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                } else {
                    mCommonMethods.showToast(mContext, "Please Browse or Capture Nomination Form Document First!");
                }

                break;

            case R.id.imgbtn_posp_nomination_camera:

                str_doc = NOMINATION_FORM_DOC;
                capture_document();

                //capture_docs(NOMINATION_FORM_DOC, "CAMERA");
                break;

            case R.id.imgbtn_posp_nomination_browse:

                str_doc = NOMINATION_FORM_DOC;
                browse_docs();
                //capture_docs(NOMINATION_FORM_DOC, "BROWSE");
                break;

            case R.id.imgbtn_posp_nomination_upload:
                if (mNominationFile != null) {
                    createSoapRequestToUploadDoc(mNominationFile);
                } else {
                    mCommonMethods.showMessageDialog(mContext, "File Not Found..");
                }
                break;

            case R.id.imgbtn_posp_pan_view:

                if (mPanFile != null) {
                    try {
                        mCommonMethods.openAllDocs(mContext, mPanFile);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                } else {
                    mCommonMethods.showToast(mContext, "Please Browse or Capture PAN Document First!");
                }

                break;

            case R.id.imgbtn_posp_pan_camera:
                //open_ocr_activity(PAN_DOC, "CAMERA");
                str_doc = PAN_DOC;
                capture_document();
                break;

            case R.id.imgbtn_posp_pan_browse:
                //open_ocr_activity(PAN_DOC, "BROWSE");
                str_doc = PAN_DOC;
                browse_docs();
                break;

            case R.id.imgbtn_posp_pan_upload:
                if (mPanFile != null) {
                    createSoapRequestToUploadDoc(mPanFile);
                } else {
                    mCommonMethods.showMessageDialog(mContext, "File Not Found..");
                }
                break;

            case R.id.btn_aob_auth_pan_submit:

                if (is_from_ho) {

                    if (is_bank_passbook_uploaded && is_tcc_proof_uploaded && is_education_proof_uploaded
                            && is_communication_proof_uploaded && is_score_card_proof_uploaded
                            && is_nomination_doc_uploaded && is_declaration_uploaded && is_others_doc_uploaded
                            && is_pan_doc_uploaded) {
                        showMessageDialog("Requirement uploaded Successfully!");
                    } else {
                        mCommonMethods.showMessageDialog(mContext, "Please Upload Requirement Documents..");
                    }
                } else if (!is_dashboard && !is_bsm_questions) {
                    String str_error = validateDetails();
                    if (str_error.equals("")) {

                        //create xml for saving
                        get_doc_upload_xml();

                        //3. update data against global row id
                        ContentValues cv = new ContentValues();
                        cv.put(db.POSP_RA_DOCUMENTS_UPLOAD, str_doc_upload.toString());
                        cv.put(db.POSP_RA_UPDATED_BY, strCIFBDMUserId);

                        //save date in long
                        cv.put(db.POSP_RA_UPDATED_DATE, new Date(mCalendar.getTimeInMillis()).getTime() + "");
                        //status for database data sync
                        cv.put(db.POSP_RA_IN_APP_STATUS, "11");
                        cv.put(db.POSP_RA_IN_APP_STATUS_REMARK, "Data Sync Pending");

                        int i = db.update_POSP_RA_details(cv, db.POSP_RA_ID + " =? ",
                                new String[]{Activity_POSP_RA_Authentication.row_details + ""});

                        //get all data from db
                        ArrayList<Pojo_POSP_RA> lstRes = db.get_posp_ra_details_by_ID(Activity_POSP_RA_Authentication.row_details);
                        if (lstRes.size() > 0) {

                            try {
                                str_final_posp_info = new StringBuilder();

                                //create final POSP-RA string to synch
                                str_final_posp_info.append("<?xml version='1.0' encoding='utf-8' ?><POSP_RA>");
                                //str_final_aob_info.append(lstRes.get(0).getStr_basic_details().toString());//basic details
                                //personal info
                                str_final_posp_info.append(lstRes.get(0).getStr_personal_info());
                                //occupational info
                                str_final_posp_info.append(lstRes.get(0).getStr_occupation_info());

                                //nominational info
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
                                str_final_posp_info.append(str_nominee_info);
                                //ended 19-01-2021

                                //bank details info
                                str_final_posp_info.append(lstRes.get(0).getStr_bank_details());

                                //form 1-a info
                                str_final_posp_info.append(new Pojo_POSP_RA().get_form1a_xml());

                                //Exam and Training details
                                String str_exam_training_info = lstRes.get(0).getStr_exam_training_details();
                                String str_training_details_language = mCommonMethods.getSubStringByString(str_exam_training_info, "training_details_language");
                                str_exam_training_info = str_exam_training_info.replace(str_training_details_language, "");
                                String str_exam_details_exam_date = mCommonMethods.getSubStringByString(str_exam_training_info, "exam_details_exam_date");
                                str_exam_training_info = str_exam_training_info.replace(str_exam_details_exam_date, "");
                                String str_training_details_training_mode = mCommonMethods.getSubStringByString(str_exam_training_info, "training_details_training_mode");
                                str_exam_training_info = str_exam_training_info.replace(str_training_details_training_mode, "");
                                String str_training_details_required_hrs = mCommonMethods.getSubStringByString(str_exam_training_info, "training_details_required_hrs");
                                str_exam_training_info = str_exam_training_info.replace(str_training_details_required_hrs, "");
                                String str_training_details_institute_name = mCommonMethods.getSubStringByString(str_exam_training_info, "training_details_institute_name");
                                str_exam_training_info = str_exam_training_info.replace(str_training_details_institute_name, "");
                                String str_exam_details_exam_mode = mCommonMethods.getSubStringByString(str_exam_training_info, "exam_details_exam_mode");
                                str_exam_training_info = str_exam_training_info.replace(str_exam_details_exam_mode, "");
                                String str_exam_details_exam_body = mCommonMethods.getSubStringByString(str_exam_training_info, "exam_details_exam_body");
                                str_exam_training_info = str_exam_training_info.replace(str_exam_details_exam_body, "");
                                String str_exam_details_marks_obtained = mCommonMethods.getSubStringByString(str_exam_training_info, "exam_details_marks_obtained");
                                str_exam_training_info = str_exam_training_info.replace(str_exam_details_marks_obtained, "");
                                String str_exam_details_exam_status = mCommonMethods.getSubStringByString(str_exam_training_info, "exam_details_exam_status");
                                str_exam_training_info = str_exam_training_info.replace(str_exam_details_exam_status, "");
                                str_final_posp_info.append(str_exam_training_info);

                                //BSM interview questions
                                str_final_posp_info.append(new Pojo_POSP_RA().getBSMQuestions());
                                //str_final_aob_info.append(lstRes.get(0).getStr_declarations_conditions().toString());//terms and conditions

                                str_final_posp_info.append("<um_code>" + strCIFBDMUserId + "</um_code>");

                                SimpleDateFormat sdp = new SimpleDateFormat("MM-dd-yyyy");
                                String str_created_date = "";
                                try {
                                    //str_created_date = sdp.format(new Date(Long.valueOf(lstRes.get(0).getStr_created_date())));
                                    str_created_date = sdp.format(new Date(mCalendar.getTimeInMillis()));
                                } catch (Exception ex) {
                                    //str_created_date = lstRes.get(0).getStr_created_date();
                                    ex.printStackTrace();
                                }

                                str_final_posp_info.append("<created_date>" + str_created_date + "</created_date>");//created_date

                                //added 19-01-2021 nominational info extra fields
                                str_final_posp_info.append(str_nominee_info_nom_address2 + str_nominee_info_nom_address3
                                        + str_nominee_info_appointee_address2 + str_nominee_info_appointee_address3);
                                //ended 19-01-2021

                                //added 25-05-2021 agency type
                                str_final_posp_info.append("<agency_type>").append(mCommonMethods.str_posp_ra_customer_type).append("</agency_type>");
                                //ended 25-05-2021

                                // exam and training details extrafields
                                str_final_posp_info.append(
                                        str_training_details_language + str_exam_details_exam_date
                                                + str_training_details_training_mode + str_training_details_required_hrs
                                                + str_training_details_institute_name + str_exam_details_exam_mode
                                                + str_exam_details_exam_body + str_exam_details_marks_obtained
                                                + str_exam_details_exam_status);

                                String str_irn = lstRes.get(0).getStr_irn();
                                str_irn = str_irn == null ? "" : str_irn;

                                str_final_posp_info.append("<posp_ra_irn>").append(str_irn).append("</posp_ra_irn>");
                                str_final_posp_info.append("<sub_type>").append("New").append("</sub_type>");

                                sdp = new SimpleDateFormat("MM-dd-yyyy hh:mm:ss a");
                                String strTimeDate = sdp.format(new Date());
                                str_final_posp_info.append("<createddatetime>").append(strTimeDate).append("</createddatetime>");

                                //for stamping 30-09-2021
                                String strRejectFlag = "", strRejectDate = "";
                                if (is_rejection) {
                                    strRejectFlag = "1";
                                    strRejectDate = strTimeDate.split(" ")[0];
                                }
                                str_final_posp_info.append("<REQDOCFLAG>" + strRejectFlag + "</REQDOCFLAG>");
                                str_final_posp_info.append("<Reqdocsubmitdate>" + strRejectDate + "</Reqdocsubmitdate>");
                                //for stamping 30-09-2021

                                str_final_posp_info.append("</POSP_RA>");

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
                } else if (is_dashboard) {
                    startActivity(new Intent(Activity_POSP_RA_DocumentUpload.this, Activity_POSP_RA_Dashboard.class));
                } else if (is_bsm_questions) {
                    Intent intent = new Intent(mContext, Activity_POSP_RA_InterviewBSM.class);
                    intent.putExtra("UMCode", str_um_code);
                    intent.putExtra("PANNumber", str_pan_no);
                    startActivity(intent);
                }

                break;

            default:
                break;
        }
    }

    public String validateDetails() {

        if (!is_bank_passbook_uploaded) {
            return "Please upload Bank Passbook Documents";
        } else if (!is_education_proof_uploaded) {
            return "Please upload Basic Qualification Documents";
        } else if (!is_communication_proof_uploaded) {
            return "Please upload Communication Address Documents";
        } else if (!is_declaration_uploaded) {
            return "Please upload Mobile Declaration";
        }

        if (is_rejection) {
            if (!is_tcc_proof_uploaded)
                return "Please upload TCC Document";

            if (!is_pan_doc_uploaded)
                return "Please upload PAN Document";
        }

        return "";
    }

    private void get_doc_upload_xml() {

        str_doc_upload = new StringBuilder();

        //str_terms_conditions.append("<doc_upload>");

        str_doc_upload.append("<doc_upload_bank_pass_type>").append(str_bank_passbook_type).append("</doc_upload_bank_pass_type>");
        str_doc_upload.append("<doc_upload_bank_pass_is_upload>").append(is_bank_passbook_uploaded).append("</doc_upload_bank_pass_is_upload>");

        str_doc_upload.append("<doc_upload_age_proof_type></doc_upload_age_proof_type>");
        str_doc_upload.append("<doc_upload_age_proof_is_upload></doc_upload_age_proof_is_upload>");

        str_doc_upload.append("<doc_upload_tcc_type>").append(str_tcc_proof_type).append("</doc_upload_tcc_type>");
        str_doc_upload.append("<doc_upload_tcc_is_upload>").append(is_tcc_proof_uploaded).append("</doc_upload_tcc_is_upload>");

        str_doc_upload.append("<doc_upload_basic_qualification_type>").append(str_education_proof_type).append("</doc_upload_basic_qualification_type>");
        str_doc_upload.append("<doc_upload_basic_qualification_is_upload>").append(is_education_proof_uploaded).append("</doc_upload_basic_qualification_is_upload>");

        str_doc_upload.append("<doc_upload_comm_address_type>").append(str_communication_proof_type).append("</doc_upload_comm_address_type>");
        str_doc_upload.append("<doc_upload_comm_address_is_upload>").append(is_communication_proof_uploaded).append("</doc_upload_comm_address_is_upload>");

        str_doc_upload.append("<doc_upload_score_card_type>").append(str_score_card_proof_type).append("</doc_upload_score_card_type>");
        str_doc_upload.append("<doc_upload_score_card_is_upload>").append(is_score_card_proof_uploaded).append("</doc_upload_score_card_is_upload>");

        str_doc_upload.append("<doc_upload_others_type>").append(str_others_doc_type).append("</doc_upload_others_type>");
        str_doc_upload.append("<doc_upload_others_document>").append(edt_aob_auth_others.getText().toString()).append("</doc_upload_others_document>");
        str_doc_upload.append("<doc_upload_others_is_upload>").append(is_others_doc_uploaded).append("</doc_upload_others_is_upload>");

        str_doc_upload.append("<doc_upload_declaration_type>").append(str_declaration_type).append("</doc_upload_declaration_type>");
        str_doc_upload.append("<doc_upload_declaration_is_upload>").append(is_declaration_uploaded).append("</doc_upload_declaration_is_upload>");

        str_doc_upload.append("<doc_upload_nomination_type>").append(str_nomination_doc_type).append("</doc_upload_nomination_type>");
        str_doc_upload.append("<doc_upload_nomination_is_upload>").append(is_nomination_doc_uploaded).append("</doc_upload_nomination_is_upload>");

        str_doc_upload.append("<doc_upload_pan_type>").append(str_pan_doc_type).append("</doc_upload_pan_type>");
        str_doc_upload.append("<doc_upload_pan_is_upload>").append(is_pan_doc_uploaded).append("</doc_upload_pan_is_upload>");

        //str_terms_conditions.append("</doc_upload>");
    }

    public void showMessageDialog(String message) {
        try {
            final Dialog dialog = new Dialog(mContext);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_with_ok_button);
            dialog.setCancelable(false);

            TextView text = dialog.findViewById(R.id.tv_title);
            text.setText(message);

            Button dialogButton = dialog.findViewById(R.id.bt_ok);
            dialogButton.setText("Ok");
            dialogButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    dialog.dismiss();

                    if (is_from_ho) {
                        Intent i = new Intent(Activity_POSP_RA_DocumentUpload.this, Activity_POSP_RA_Rejection_Remarks.class);
                        i.putExtra("enrollment_type", mCommonMethods.str_posp_ra_customer_type);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                    } else if (!message.equalsIgnoreCase("Data synch Failure..\ntry after some time by dashboard menu.")) {
                        Activity_POSP_RA_Authentication.row_details = 0;

                        if (is_rejection) {
                            //redirect to dashboard page
                            Intent i = new Intent(Activity_POSP_RA_DocumentUpload.this, Activity_POSP_RA_Rejection_Remarks.class);
                            i.putExtra("enrollment_type", mCommonMethods.str_posp_ra_customer_type);
                            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(i);
                        } else {
                            //redirect to home page
                            Intent i = new Intent(Activity_POSP_RA_DocumentUpload.this, Activity_POSP_RA_Authentication.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(i);
                        }
                    }
                }
            });
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setRejectionRecords() {

        imgbtn_aob_auth_bank_pass_book_camera.setEnabled(false);
        imgbtn_aob_auth_bank_pass_book_browse.setEnabled(false);
        imgbtn_aob_auth_bank_pass_book_upload.setEnabled(false);
        imgbtn_aob_auth_bank_pass_book_upload.setImageDrawable(getResources().getDrawable(R.drawable.checkedupload));
        is_bank_passbook_uploaded = true;

        imgbtn_posp_tcc_camera.setEnabled(false);
        imgbtn_posp_tcc_browse.setEnabled(false);
        imgbtn_posp_tcc_upload.setEnabled(false);
        imgbtn_posp_tcc_upload.setImageDrawable(getResources().getDrawable(R.drawable.checkedupload));
        is_tcc_proof_uploaded = true;

        imgbtn_aob_auth_basic_qualification_camera.setEnabled(false);
        imgbtn_aob_auth_basic_qualification_browse.setEnabled(false);
        imgbtn_aob_auth_basic_qualification_upload.setEnabled(false);
        imgbtn_aob_auth_basic_qualification_upload.setImageDrawable(getResources().getDrawable(R.drawable.checkedupload));
        is_education_proof_uploaded = true;

        imgbtn_aob_auth_comm_add_camera.setEnabled(false);
        imgbtn_aob_auth_comm_add_browse.setEnabled(false);
        imgbtn_aob_auth_comm_add_upload.setEnabled(false);
        imgbtn_aob_auth_comm_add_upload.setImageDrawable(getResources().getDrawable(R.drawable.checkedupload));
        is_communication_proof_uploaded = true;

        imgbtn_posp_score_card_camera.setEnabled(false);
        imgbtn_posp_score_card_browse.setEnabled(false);
        imgbtn_posp_score_cardupload.setEnabled(false);
        imgbtn_posp_score_cardupload.setImageDrawable(getResources().getDrawable(R.drawable.checkedupload));
        is_score_card_proof_uploaded = true;

        imgbtn_posp_nomination_camera.setEnabled(false);
        imgbtn_posp_nomination_browse.setEnabled(false);
        imgbtn_posp_nomination_upload.setEnabled(false);
        imgbtn_posp_nomination_upload.setImageDrawable(getResources().getDrawable(R.drawable.checkedupload));
        is_nomination_doc_uploaded = true;

        imgbtn_posp_pan_camera.setEnabled(false);
        imgbtn_posp_pan_browse.setEnabled(false);
        imgbtn_posp_pan_upload.setEnabled(false);
        imgbtn_posp_pan_upload.setImageDrawable(getResources().getDrawable(R.drawable.checkedupload));
        is_pan_doc_uploaded = true;

        edt_aob_auth_others.setEnabled(false);
        imgbtn_aob_auth_others_camera.setEnabled(false);
        imgbtn_aob_auth_others_browse.setEnabled(false);
        imgbtn_aob_auth_others_upload.setEnabled(false);
        imgbtn_aob_auth_others_upload.setImageDrawable(getResources().getDrawable(R.drawable.checkedupload));
        is_others_doc_uploaded = true;

        imgbtn_posp_declaration_camera.setEnabled(false);
        imgbtn_posp_declaration_browse.setEnabled(false);
        imgbtn_posp_declaration_upload.setEnabled(false);
        imgbtn_posp_declaration_upload.setImageDrawable(getResources().getDrawable(R.drawable.checkedupload));
        is_declaration_uploaded = true;

        ArrayList<POJO_POSP_RA_Rejection> lstRejection = db.getPOSP_RA_Rejection_By_PAN(str_pan_no,
                mCommonMethods.str_posp_ra_customer_type);
        for (POJO_POSP_RA_Rejection obj : lstRejection) {
            String docName = obj.getStrDocName();
            docName = docName == null ? "" : docName;
            String strRemark = obj.getStrRemarks();
            strRemark = strRemark == null ? "" : strRemark;

            if (docName.equals("Bank Pass book/Cancelled Cheque leaf/Bank Direct Credit Letter for Non SBI Cases")) {
                if (strRemark.equals("")) {
                    txt_aob_auth_bank_pass_book_remark.setVisibility(View.GONE);
                } else {
                    txt_aob_auth_bank_pass_book_remark.setVisibility(View.VISIBLE);
                    txt_aob_auth_bank_pass_book_remark.setText(strRemark);
                }

                imgbtn_aob_auth_bank_pass_book_camera.setEnabled(true);
                imgbtn_aob_auth_bank_pass_book_browse.setEnabled(true);
                imgbtn_aob_auth_bank_pass_book_upload.setEnabled(true);
                imgbtn_aob_auth_bank_pass_book_upload.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_uploaddoc));
                is_bank_passbook_uploaded = false;
            }

            if (docName.equals("Basic Qualification Proof")) {
                if (strRemark.equals("")) {
                    txt_aob_auth_basic_qualification_remark.setVisibility(View.GONE);
                } else {
                    txt_aob_auth_basic_qualification_remark.setText(strRemark);
                    txt_aob_auth_basic_qualification_remark.setVisibility(View.VISIBLE);
                }

                imgbtn_aob_auth_basic_qualification_camera.setEnabled(true);
                imgbtn_aob_auth_basic_qualification_browse.setEnabled(true);
                imgbtn_aob_auth_basic_qualification_upload.setEnabled(true);
                imgbtn_aob_auth_basic_qualification_upload.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_uploaddoc));
                is_education_proof_uploaded = false;
            }

            if (docName.equals("Communication Address Proof")) {
                if (strRemark.equals("")) {
                    txt_aob_auth_comm_add_remark.setVisibility(View.GONE);
                } else {
                    txt_aob_auth_comm_add_remark.setText(strRemark);
                    txt_aob_auth_comm_add_remark.setVisibility(View.VISIBLE);
                }

                imgbtn_aob_auth_comm_add_camera.setEnabled(true);
                imgbtn_aob_auth_comm_add_browse.setEnabled(true);
                imgbtn_aob_auth_comm_add_upload.setEnabled(true);
                imgbtn_aob_auth_comm_add_upload.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_uploaddoc));
                is_communication_proof_uploaded = false;
            }

            if (docName.equals("Nomination Form")) {
                if (strRemark.equals("")) {
                    txt_posp_nomination_remark.setVisibility(View.GONE);
                } else {
                    txt_posp_nomination_remark.setText(strRemark);
                    txt_posp_nomination_remark.setVisibility(View.VISIBLE);
                }

                imgbtn_posp_nomination_camera.setEnabled(true);
                imgbtn_posp_nomination_browse.setEnabled(true);
                imgbtn_posp_nomination_upload.setEnabled(true);
                imgbtn_posp_nomination_upload.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_uploaddoc));
                is_nomination_doc_uploaded = false;
            }

            if (docName.equals("PAN Details")) {
                if (strRemark.equals("")) {
                    txt_posp_pan_remark.setVisibility(View.GONE);
                } else {
                    txt_posp_pan_remark.setText(strRemark);
                    txt_posp_pan_remark.setVisibility(View.VISIBLE);
                }

                imgbtn_posp_pan_camera.setEnabled(true);
                imgbtn_posp_pan_browse.setEnabled(true);
                imgbtn_posp_pan_upload.setEnabled(true);
                imgbtn_posp_pan_upload.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_uploaddoc));
                is_pan_doc_uploaded = false;
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

            if (docName.equals("Agent Certification1")) {
                if (strRemark.equals("")) {
                    txt_posp_declaration_remark.setVisibility(View.GONE);
                } else {
                    txt_posp_declaration_remark.setVisibility(View.VISIBLE);
                    txt_posp_declaration_remark.setText(strRemark);
                }
                imgbtn_posp_declaration_camera.setEnabled(true);
                imgbtn_posp_declaration_browse.setEnabled(true);
                imgbtn_posp_declaration_upload.setEnabled(true);
                imgbtn_posp_declaration_upload.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_uploaddoc));
                is_declaration_uploaded = false;
            }

            if (docName.equals("Training Completion Certificate")) {
                if (strRemark.equals("")) {
                    txt_posp_tcc_remark.setVisibility(View.GONE);
                } else {
                    txt_posp_tcc_remark.setText(strRemark);
                    txt_posp_tcc_remark.setVisibility(View.VISIBLE);
                }

                imgbtn_posp_tcc_camera.setEnabled(true);
                imgbtn_posp_tcc_browse.setEnabled(true);
                imgbtn_posp_tcc_upload.setEnabled(true);
                imgbtn_posp_tcc_upload.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_uploaddoc));
                is_tcc_proof_uploaded = false;
            }

            if (docName.equals("Exam passing certificate")) {
                if (strRemark.equals("")) {
                    txt_posp_score_card_remark.setVisibility(View.GONE);
                } else {
                    txt_posp_score_card_remark.setText(strRemark);
                    txt_posp_score_card_remark.setVisibility(View.VISIBLE);
                }

                imgbtn_posp_score_card_camera.setEnabled(true);
                imgbtn_posp_score_card_browse.setEnabled(true);
                imgbtn_posp_score_cardupload.setEnabled(true);
                imgbtn_posp_score_cardupload.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_uploaddoc));
                is_score_card_proof_uploaded = false;
            }
        }
    }

    public void enableDisableAllFields(boolean is_enable) {

        edt_aob_auth_others.setEnabled(is_enable);

        imgbtn_aob_auth_bank_pass_book_camera.setEnabled(is_enable);
        imgbtn_aob_auth_bank_pass_book_browse.setEnabled(is_enable);
        imgbtn_aob_auth_bank_pass_book_upload.setEnabled(is_enable);

        imgbtn_posp_tcc_camera.setEnabled(is_enable);
        imgbtn_posp_tcc_browse.setEnabled(is_enable);
        imgbtn_posp_tcc_upload.setEnabled(is_enable);

        imgbtn_aob_auth_basic_qualification_camera.setEnabled(is_enable);
        imgbtn_aob_auth_basic_qualification_browse.setEnabled(is_enable);
        imgbtn_aob_auth_basic_qualification_upload.setEnabled(is_enable);

        imgbtn_aob_auth_comm_add_camera.setEnabled(is_enable);
        imgbtn_aob_auth_comm_add_browse.setEnabled(is_enable);
        imgbtn_aob_auth_comm_add_upload.setEnabled(is_enable);

        imgbtn_posp_score_card_camera.setEnabled(is_enable);
        imgbtn_posp_score_card_browse.setEnabled(is_enable);
        imgbtn_posp_score_cardupload.setEnabled(is_enable);

        imgbtn_aob_auth_others_camera.setEnabled(is_enable);
        imgbtn_aob_auth_others_browse.setEnabled(is_enable);
        imgbtn_aob_auth_others_upload.setEnabled(is_enable);
    }

    public void enableUploadButton(String str_doc_type) {

        imgbtn_aob_auth_bank_pass_book_upload.setEnabled(false);

        imgbtn_posp_tcc_upload.setEnabled(false);

        imgbtn_aob_auth_basic_qualification_upload.setEnabled(false);

        imgbtn_aob_auth_comm_add_upload.setEnabled(false);

        imgbtn_posp_score_cardupload.setEnabled(false);

        imgbtn_aob_auth_others_upload.setEnabled(false);

        imgbtn_posp_declaration_upload.setEnabled(false);

        imgbtn_posp_nomination_upload.setEnabled(false);

        imgbtn_posp_pan_upload.setEnabled(false);

        if (str_doc_type.equals(BANK_PASSBOOK_DOC)) {
            imgbtn_aob_auth_bank_pass_book_upload.setEnabled(true);
        } else if (str_doc_type.equals(TCC_PROOF_DOC)) {
            imgbtn_posp_tcc_upload.setEnabled(true);
        } else if (str_doc_type.equals(EDUCATION_PROOF_DOC)) {
            imgbtn_aob_auth_basic_qualification_upload.setEnabled(true);
        } else if (str_doc_type.equals(COMM_PROOF_DOC)) {
            imgbtn_aob_auth_comm_add_upload.setEnabled(true);
        } else if (str_doc_type.equals(SCORE_CARD_DOC)) {
            imgbtn_posp_score_cardupload.setEnabled(true);
        } else if (str_doc_type.equals(OTHERS_DOC)) {
            imgbtn_aob_auth_others_upload.setEnabled(true);
        } else if (str_doc_type.equals(MOBILE_DECLARATION_DOC)) {
            imgbtn_posp_declaration_upload.setEnabled(true);
        } else if (str_doc_type.equals(NOMINATION_FORM_DOC)) {
            imgbtn_posp_nomination_upload.setEnabled(true);
        } else if (str_doc_type.equals(PAN_DOC)) {
            imgbtn_posp_pan_upload.setEnabled(true);
        }
    }

    private void createSoapRequestToUploadDoc(final File mFile) {

        Single.fromCallable(() -> mCommonMethods.read(mFile))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<byte[]>() {
                    @Override
                    public void accept(@NonNull byte[] result) throws Exception {
                        if (result != null) {

                            SoapObject request;
                            if (is_rejection) {
                                request = new SoapObject(NAMESPACE, METHOD_NAME_UPLOAD_ALL_POSP_RA_DOC_REJECTION);
                                request.addProperty("Type", mCommonMethods.str_posp_ra_customer_type);
                            } else {
                                request = new SoapObject(NAMESPACE, METHOD_NAME_UPLOAD_ALL_POSP_RA_DOC);
                            }

                            request.addProperty("f", result);
                            request.addProperty("fileName", mFile.getName());
                            request.addProperty("PAN", str_pan_no);

                            mCommonMethods.appendSecurityParams(mContext, request, strCIFBDMEmailId, strCIFBDMMObileNo);

                            if (is_rejection) {
                                mAsyncUploadFileCommon = new AsyncUploadFile_Common(mContext, Activity_POSP_RA_DocumentUpload.this,
                                        request, METHOD_NAME_UPLOAD_ALL_POSP_RA_DOC_REJECTION);
                            } else {
                                mAsyncUploadFileCommon = new AsyncUploadFile_Common(mContext,
                                        Activity_POSP_RA_DocumentUpload.this, request, METHOD_NAME_UPLOAD_ALL_POSP_RA_DOC);
                            }

                            mAsyncUploadFileCommon.execute();

                        } else {
                            mCommonMethods.showToast(mContext, "File Not Found");
                        }
                    }
                }, throwable -> {
                    mCommonMethods.showToast(mContext, "File Not Found");
                });
    }

    @Override
    public void onUploadComplete(Boolean result) {
        if (result) {

            if (str_doc.equals(MOBILE_DECLARATION_DOC)) {

                imgbtn_posp_declaration_camera.setEnabled(false);
                imgbtn_posp_declaration_browse.setEnabled(false);
                imgbtn_posp_declaration_upload.setEnabled(false);
                imgbtn_posp_declaration_upload.setImageDrawable(getResources().getDrawable(R.drawable.checkedupload));

                is_declaration_uploaded = true;
                mMobDeclarationFile = null;

            } else if (str_doc.equals(BANK_PASSBOOK_DOC)) {

                imgbtn_aob_auth_bank_pass_book_camera.setEnabled(false);
                imgbtn_aob_auth_bank_pass_book_browse.setEnabled(false);
                imgbtn_aob_auth_bank_pass_book_upload.setEnabled(false);
                imgbtn_aob_auth_bank_pass_book_upload.setImageDrawable(getResources().getDrawable(R.drawable.checkedupload));

                is_bank_passbook_uploaded = true;
                mBankPassbookFile = null;

            } else if (str_doc.equals(TCC_PROOF_DOC)) {

                imgbtn_posp_tcc_camera.setEnabled(false);
                imgbtn_posp_tcc_browse.setEnabled(false);
                imgbtn_posp_tcc_upload.setEnabled(false);
                imgbtn_posp_tcc_upload.setImageDrawable(getResources().getDrawable(R.drawable.checkedupload));

                is_tcc_proof_uploaded = true;
                mTCCFile = null;
            } else if (str_doc.equals(EDUCATION_PROOF_DOC)) {

                imgbtn_aob_auth_basic_qualification_camera.setEnabled(false);
                imgbtn_aob_auth_basic_qualification_browse.setEnabled(false);
                imgbtn_aob_auth_basic_qualification_upload.setEnabled(false);
                imgbtn_aob_auth_basic_qualification_upload.setImageDrawable(getResources().getDrawable(R.drawable.checkedupload));

                is_education_proof_uploaded = true;
                mEducationFile = null;
            } else if (str_doc.equals(COMM_PROOF_DOC)) {

                imgbtn_aob_auth_comm_add_camera.setEnabled(false);
                imgbtn_aob_auth_comm_add_browse.setEnabled(false);
                imgbtn_aob_auth_comm_add_upload.setEnabled(false);
                imgbtn_aob_auth_comm_add_upload.setImageDrawable(getResources().getDrawable(R.drawable.checkedupload));

                is_communication_proof_uploaded = true;
                mCommunicationFile = null;
            } else if (str_doc.equals(SCORE_CARD_DOC)) {

                imgbtn_posp_score_card_camera.setEnabled(false);
                imgbtn_posp_score_card_browse.setEnabled(false);
                imgbtn_posp_score_cardupload.setEnabled(false);
                imgbtn_posp_score_cardupload.setImageDrawable(getResources().getDrawable(R.drawable.checkedupload));

                is_score_card_proof_uploaded = true;
                mScoreCardFile = null;
            } else if (str_doc.equals(OTHERS_DOC)) {

                imgbtn_aob_auth_others_camera.setEnabled(false);
                imgbtn_aob_auth_others_browse.setEnabled(false);
                imgbtn_aob_auth_others_upload.setEnabled(false);
                imgbtn_aob_auth_others_upload.setImageDrawable(getResources().getDrawable(R.drawable.checkedupload));

                is_others_doc_uploaded = true;
                mOthersFile = null;
            } else if (str_doc.equals(NOMINATION_FORM_DOC)) {

                imgbtn_posp_nomination_camera.setEnabled(false);
                imgbtn_posp_nomination_browse.setEnabled(false);
                imgbtn_posp_nomination_upload.setEnabled(false);
                imgbtn_posp_nomination_upload.setImageDrawable(getResources().getDrawable(R.drawable.checkedupload));

                is_nomination_doc_uploaded = true;
                mNominationFile = null;
            } else if (str_doc.equals(PAN_DOC)) {

                imgbtn_posp_pan_camera.setEnabled(false);
                imgbtn_posp_pan_browse.setEnabled(false);
                imgbtn_posp_pan_upload.setEnabled(false);
                imgbtn_posp_pan_upload.setImageDrawable(getResources().getDrawable(R.drawable.checkedupload));

                is_pan_doc_uploaded = true;
                mPanFile = null;
            }

            mCommonMethods.showToast(mContext, "Document Upload Successfully...");
        } else {
            Toast.makeText(mContext, "PLease try agian later..",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public class AsyncAllAOB extends AsyncTask<String, String, String> {

        private volatile boolean running = true;

        @Override
        protected void onPreExecute() {
            mProgressDialog = new ProgressDialog(mContext, ProgressDialog.THEME_HOLO_LIGHT);
            mProgressDialog.setMessage(Html.fromHtml("<font color='#00a1e3'><b>Please wait...<b></font>"));
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                running = true;

                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME_SAVE_POSP_RA_DETAILS);
                request.addProperty("xmlStr", str_final_posp_info.toString());
                request.addProperty("Type", mCommonMethods.str_posp_ra_customer_type);
                mCommonMethods.TLSv12Enable();

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;

                envelope.setOutputSoapObject(request);

                HttpTransportSE androidHttpTranport = new HttpTransportSE(ServiceURL.SERVICE_URL);
                try {
                    androidHttpTranport.call(NAMESPACE + METHOD_NAME_SAVE_POSP_RA_DETAILS, envelope);
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
                cv.put(db.POSP_RA_UPDATED_BY, strCIFBDMUserId);

                //save date in long
                cv.put(db.POSP_RA_UPDATED_DATE, new Date(mCalendar.getTimeInMillis()).getTime() + "");

                String str_data_save_err = "";
                if (result.equals("1")) {
                    cv.put(db.POSP_RA_IN_APP_STATUS, "12");//for successfull synch
                    cv.put(db.POSP_RA_IN_APP_STATUS_REMARK, "Data Sync Completed");
                    str_data_save_err = "Data synch Successfully..";
                } else if (result.equals("0")) {
                    cv.put(db.POSP_RA_IN_APP_STATUS, "13");//for synch failure
                    cv.put(db.POSP_RA_IN_APP_STATUS_REMARK, "Data Sync Failure, Sync from Dashboard menu");
                    str_data_save_err = "Data synch Failure..\ntry after some time by dashboard menu.";
                } else if (result.equals("2")) {
                    cv.put(db.POSP_RA_IN_APP_STATUS, "14");//for data already exist in server
                    cv.put(db.POSP_RA_IN_APP_STATUS_REMARK, "Data already exists in server");
                    str_data_save_err = "Applicant data already exists in server";
                }

                int i = db.update_POSP_RA_details(cv, db.POSP_RA_ID + " =? ",
                        new String[]{Activity_POSP_RA_Authentication.row_details + ""});

                showMessageDialog(str_data_save_err);

            } else {
                mCommonMethods.showMessageDialog(mContext, mCommonMethods.WEEK_INTERNET_MESSAGE);
            }

        }
    }
}
