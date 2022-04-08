package sbilife.com.pointofsale_bancaagency.posp_ra;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.MarshalBase64;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import org.simpleframework.xml.stream.Format;
import org.simpleframework.xml.stream.Verbosity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import cropper.CropImage;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import sbilife.com.pointofsale_bancaagency.DatabaseHelper;
import sbilife.com.pointofsale_bancaagency.ParseXML;
import sbilife.com.pointofsale_bancaagency.R;
import sbilife.com.pointofsale_bancaagency.ServiceURL;
import sbilife.com.pointofsale_bancaagency.agent_on_boarding.ActivityAOB_Menu;
import sbilife.com.pointofsale_bancaagency.ckyc.AsyncGetcKYCSearchDetail;
import sbilife.com.pointofsale_bancaagency.ckyc.InterfaceCKYCProcessCompletion;
import sbilife.com.pointofsale_bancaagency.common.AsyncUploadFile_Common;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;
import sbilife.com.pointofsale_bancaagency.common.CompressImage;
import sbilife.com.pointofsale_bancaagency.common.FileLoader;
import sbilife.com.pointofsale_bancaagency.common.SimpleCrypto;
import sbilife.com.pointofsale_bancaagency.common.StorageUtils;
import sbilife.com.pointofsale_bancaagency.ekyc.ekycRequest.ECSOtpKycRequest;
import sbilife.com.pointofsale_bancaagency.ekyc.ekycRequest.ECSOtpRequest;
import sbilife.com.pointofsale_bancaagency.ekyc.ekycRequest.ECSRDKYCRequest;
import sbilife.com.pointofsale_bancaagency.ekyc.ekycResponse.ECSKycResponse;
import sbilife.com.pointofsale_bancaagency.ekyc.ekycResponse.ECSOtpResponse;
import sbilife.com.pointofsale_bancaagency.ekyc.offline_ekyc.OfflineKYCActivity;
import sbilife.com.pointofsale_bancaagency.ekyc.offline_ekyc.OfflinePaperlessKycResponse;
import sbilife.com.pointofsale_bancaagency.ekyc.offline_ekyc.UidData;
import sbilife.com.pointofsale_bancaagency.ekyc.request.CustOpts;
import sbilife.com.pointofsale_bancaagency.ekyc.request.Demo;
import sbilife.com.pointofsale_bancaagency.ekyc.request.Opts;
import sbilife.com.pointofsale_bancaagency.ekyc.request.Param;
import sbilife.com.pointofsale_bancaagency.ekyc.request.PidOptions;
import sbilife.com.pointofsale_bancaagency.ekyc.response.PidData;
import sbilife.com.pointofsale_bancaagency.ekyc.utilites.HttpConnector;
import sbilife.com.pointofsale_bancaagency.ekyc.utilites.XMLUtilities;
import sbilife.com.pointofsale_bancaagency.utility.SelfAttestedDocumentActivity;

public class Activity_POSP_RA_Authentication extends AppCompatActivity implements View.OnClickListener,
        AsyncUploadFile_Common.Interface_Upload_File_Common, DatePickerDialog.OnDateSetListener,
        InterfaceCKYCProcessCompletion {

    public static int row_details = 0;
    private final String NAMESPACE = "http://tempuri.org/";
    private final String METHOD_NAME_UPLOAD_ALL_AOB_DOC = "UploadFile_AgentEnroll";
    private final String METHOD_NAME_CHECK_PAN_NUMBER = "checkPanCardNo_smrt";
    private final String METHOD_NAME_CHECK_PAN_EXISTS = "checkpan_aob";
    private final int REQUEST_OCR = 100;
    private final int REQUEST_CODE_CAPTURE_DOCUMENT = 200;
    private final int REQUEST_CODE_BROWSE_DOCUMENT = 300;
    private final int OFFLINE_EKYC_ACTIVITY = 11;
    private final String DATE_APPLICANT_DOB = "ApplicantDOB";
    private final String PAN_DOC = "pan";
    private final String AGE_DOC = "age_proof";
    private final String COMM_ADDRESS_DOC = "communication_proof";
    //private HashMap<String, Boolean> mSupportedDevicelist;
    private final int DEVICE_CAPTURE_REQUEST_CODE = 1;
    private final int IRIS_CAPTURE_REQUEST_CODE = 2;
    /*private String base64FingerTemplate = "", deviceCode = "";*/
    private final String str_aashaar_no = "";
    private final String str_generated_otp = "";
    private final boolean validate_aadhar = false;
    private final boolean aob_iris_single_checked = false;
    private CommonMethods mCommonMethods;
    private StorageUtils mStorageUtils;
    private Context mContext;
    private DatabaseHelper db;
    private Button btn_aob_aadhaar_ok, btn_aob_aadhaar_GenerateOTP, btn_aob_aadhaar_cancel;
    /*private Button btn_aob_aadhaar_ekyc_capture;*/
    private EditText edt_aob_auth_aadhaar, edt_aob_aadhaar_otp, edt_aob_auth_pan;
    private TextView tv_aob_aadhaar_textViewMessage, tv_aob_aadhaar_Status;
    private String str_pan_no = "", str_aob_dob = "";
    private String eSignAuth = "";
    private String OCR_TYPE = "";
    private String resGenerateOTPString = "";
    private String str_otp_txn = "";
    private String pidDataXMLbase64 = "";
    private String str_doc = "";
    private String strCIFBDMUserId = "";
    private String strCIFBDMEmailId = "";
    private String strCIFBDMMObileNo = "";
    private String str_ekyc_code_DOB = "";
    private String str_ekyc_code_Name = "";
    private String str_ekyc_code_Gender = "";
    private String str_QR_code_Photo = "";
    private String str_ekyc_code_address = "";
    private boolean validate_pan_card = false;
    private boolean is_pan_file_uploaded = false, is_browse = false;
    private Date date1;
    private int panLookupClickedCount = 0;
    /*private ProgressBar vertical_aob_aadhaar_progressbar;*/
    private ProgressDialog mProgressDialog;
    //private SecBiometricLicenseManager mLicenseMgr;
    private ImageButton imgbtn_aob_auth_pan_view, imgbtn_aob_auth_pan_camera, imgbtn_aob_auth_pan_browse, imgbtn_aob_auth_pan_upload
            /*,imageButton_rejecti_remark*/;
    private RadioGroup rg_aob_ekyc_type;
    private RadioButton rb_aob_aadhaar_OTP, rb_aob_aadhaar_finger_print, rb_aob_aadhaar_IRIS, rb_aob_aadhaar_single_iris, rb_aob_aadhaar_dual_iris;
    private ImageView iv_aob_aadhaar_ekyc_fingerprintcapture, imageButtonAOBEkyc, imageButtonAOBManualKYC, imageButton_aob_dashboard;
    private LinearLayout ll_aob_aadhaar_OTP, ll_aob_aadhaar_Fingerprint, ll_aob_aadhaar_IRIS/*, ll_aob_auth_aadhhar*/,
            ll_aob_ekyc, ll_aob_manual_kyc, ll_manual_kyc_details, ll_aob_dashboard, ll_aob_aadhaar_details/*, ll_psop_rejecti_remark*/;
    private TextView textviewAOBEkyc, textviewAOBManualKYC, textview_aob_dashboard, txtPanLookUpLink, txt_aob_auth_dob
            /*, textview_rejecti_remark*/;

    private AsyncGenerateOTP mAsyncGenerateOTP;
    private AsyncGetGeneratedOTP mAsyncGetGeneratedOTP;
    private AsyncCaptureRDIRISSuccess mAsyncCaptureRDIRISSuccess;
    private AsyncFingerPrintRDServiceCaptureSuccess mAsyncFingerPrintRDServiceCaptureSuccess;

    private AsyncGetcKYCSearchDetail mAsyncGetcKYCSearchDetail;

    private File mPanFile, mCapturedImgFile;
    private AsyncUploadFile_Common mAsyncUploadFileCommon;
    private AsyncCheckPAN mAsyncCheckPAN;
    private ContentValues cv;
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

                        if (str_extension.equals(".pdf")) {

                            if (kilobyte < mCommonMethods.FILE_UPLOAD_RESTRICT_SIZE) {

                                String imageFileName = str_pan_no + "_" + str_doc + ".pdf";
                                new FileLoader(mContext, StorageUtils.DIRECT_DIRECTORY,
                                        imageFileName,
                                        new FileLoader.FileLoaderResponce() {
                                            @Override
                                            public void fileLoadFinished(File fileOutput) {
                                                if (fileOutput != null) {
                                                    mPanFile = fileOutput;
                                                    imgbtn_aob_auth_pan_camera.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_camera));
                                                    imgbtn_aob_auth_pan_browse.setImageDrawable(getResources().getDrawable(R.drawable.checkedbrowse));
                                                } else {
                                                    mPanFile = null;
                                                    imgbtn_aob_auth_pan_camera.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_camera));
                                                    imgbtn_aob_auth_pan_browse.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_browsedoc));
                                                }

                                            }
                                        }).execute(mSelectedUri);
                            } else {
                                mCommonMethods.showMessageDialog(mContext, mCommonMethods.FILE_UPLOAD_RESTRICT_SIZE_MSG);
                            }
                        } else if (strMimeType.equals("image/jpeg") || strMimeType.equals("image/jpg")
                                || strMimeType.equals("image/png")) {
                            // start cropping activity for pre-acquired image saved on the device
                            is_browse = true;
                            CropImage.activity(mSelectedUri)
                                    .start(Activity_POSP_RA_Authentication.this);
                        } else {
                            mCommonMethods.showMessageDialog(mContext, "Please Select Proper Document format!");
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

    private Calendar mCalender;
    private DatePickerDialog datePickerDialog;
    private int mDOBDay = 0, mDOBYear = 0, mDOBMonth = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_posp_ra_aunthentication);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.cifenrollment_pf_activity_title);

        initialisation();
    }

    private void getUserDetails() {
        CommonMethods.UserDetailsValuesModel userDetailsValuesModel = mCommonMethods.setUserDetails(mContext);
        strCIFBDMUserId = userDetailsValuesModel.getStrCIFBDMUserId();
        strCIFBDMEmailId = userDetailsValuesModel.getStrCIFBDMEmailId();
        strCIFBDMMObileNo = userDetailsValuesModel.getStrCIFBDMMObileNo();
    }

    public void initialisation() {
        mContext = this;
        mCommonMethods = new CommonMethods();
        mStorageUtils = new StorageUtils();
        cv = new ContentValues();

        mCommonMethods.setApplicationToolbarMenu1(this, "POSP-RA");

        db = new DatabaseHelper(mContext);

        /*mSupportedDevicelist = new HashMap<>();
        mSupportedDevicelist.put("SM-T116IR", true);*/

        mCalender = Calendar.getInstance();
        mDOBYear = mCalender.get(Calendar.YEAR);
        mDOBMonth = mCalender.get(Calendar.MONTH);
        mDOBDay = mCalender.get(Calendar.DAY_OF_MONTH);

        date1 = new Date();

        getUserDetails();

        txtPanLookUpLink = findViewById(R.id.txtPanLookUpLink);
        txtPanLookUpLink.setOnClickListener(this);
        txtPanLookUpLink.setText(Html.fromHtml("<font color='#00a1e3'>"
                + "<a href='https://agencyportal.irdai.gov.in/PublicAccess/LookUpPAN.aspx'>"
                + "PAN Look Up Link *</a></font>"), TextView.BufferType.SPANNABLE);

        edt_aob_auth_pan = findViewById(R.id.edt_aob_auth_pan);

        imgbtn_aob_auth_pan_view = findViewById(R.id.imgbtn_aob_auth_pan_view);
        imgbtn_aob_auth_pan_view.setOnClickListener(this);

        imgbtn_aob_auth_pan_camera = findViewById(R.id.imgbtn_aob_auth_pan_camera);
        imgbtn_aob_auth_pan_camera.setOnClickListener(this);
        imgbtn_aob_auth_pan_camera.setEnabled(false);

        imgbtn_aob_auth_pan_browse = findViewById(R.id.imgbtn_aob_auth_pan_browse);
        imgbtn_aob_auth_pan_browse.setOnClickListener(this);
        imgbtn_aob_auth_pan_browse.setEnabled(false);

        imgbtn_aob_auth_pan_upload = findViewById(R.id.imgbtn_aob_auth_pan_upload);
        imgbtn_aob_auth_pan_upload.setOnClickListener(this);
        imgbtn_aob_auth_pan_upload.setEnabled(false);

        btn_aob_aadhaar_ok = findViewById(R.id.btn_aob_aadhaar_ok);
        btn_aob_aadhaar_ok.setOnClickListener(this);

        txt_aob_auth_dob = findViewById(R.id.txt_aob_auth_dob);
        txt_aob_auth_dob.setOnClickListener(this);

        ll_aob_dashboard = findViewById(R.id.ll_aob_dashboard);
        ll_aob_dashboard.setOnClickListener(this);

        /*ll_psop_rejecti_remark = findViewById(R.id.ll_psop_rejecti_remark);
        ll_psop_rejecti_remark.setOnClickListener(this);

        imageButton_rejecti_remark = findViewById(R.id.imageButton_rejecti_remark);
        imageButton_rejecti_remark.setOnClickListener(this);

        textview_rejecti_remark = findViewById(R.id.textview_rejecti_remark);
        textview_rejecti_remark.setOnClickListener(this);*/

        imageButton_aob_dashboard = findViewById(R.id.imageButton_aob_dashboard);
        imageButton_aob_dashboard.setOnClickListener(this);

        textview_aob_dashboard = findViewById(R.id.textview_aob_dashboard);
        textview_aob_dashboard.setOnClickListener(this);

        edt_aob_auth_aadhaar = findViewById(R.id.edt_aob_auth_aadhaar);
        //edt_aob_auth_aadhaar.setTransformationMethod(new ChangeTransformationMethod());

        tv_aob_aadhaar_textViewMessage = findViewById(R.id.tv_aob_aadhaar_textViewMessage);
        tv_aob_aadhaar_Status = findViewById(R.id.tv_aob_aadhaar_Status);

        btn_aob_aadhaar_GenerateOTP = findViewById(R.id.btn_aob_aadhaar_GenerateOTP);
        btn_aob_aadhaar_cancel = findViewById(R.id.btn_aob_aadhaar_cancel);
        //btn_aob_aadhaar_ekyc_capture = (Button) findViewById(R.id.btn_aob_aadhaar_ekyc_capture);

        edt_aob_aadhaar_otp = findViewById(R.id.edt_aob_aadhaar_otp);

        rg_aob_ekyc_type = findViewById(R.id.rg_aob_ekyc_type);

        rb_aob_aadhaar_OTP = findViewById(R.id.rb_aob_aadhaar_OTP);
        rb_aob_aadhaar_finger_print = findViewById(R.id.rb_aob_aadhaar_finger_print);
        rb_aob_aadhaar_IRIS = findViewById(R.id.rb_aob_aadhaar_IRIS);

        rb_aob_aadhaar_single_iris = findViewById(R.id.rb_aob_aadhaar_single_iris);
        rb_aob_aadhaar_dual_iris = findViewById(R.id.rb_aob_aadhaar_dual_iris);

        iv_aob_aadhaar_ekyc_fingerprintcapture = findViewById(R.id.iv_aob_aadhaar_ekyc_fingerprintcapture);

        btn_aob_aadhaar_GenerateOTP = findViewById(R.id.btn_aob_aadhaar_GenerateOTP);
        btn_aob_aadhaar_GenerateOTP.setOnClickListener(this);

        /*ll_aob_auth_aadhhar = (LinearLayout) findViewById(R.id.ll_aob_auth_aadhhar);*/
        ll_aob_aadhaar_OTP = findViewById(R.id.ll_aob_aadhaar_OTP);
        ll_aob_aadhaar_Fingerprint = findViewById(R.id.ll_aob_aadhaar_Fingerprint);
        ll_aob_aadhaar_IRIS = findViewById(R.id.ll_aob_aadhaar_IRIS);

        //vertical_aob_aadhaar_progressbar = (ProgressBar) findViewById(R.id.vertical_aob_aadhaar_progressbar);

        ll_aob_ekyc = findViewById(R.id.ll_aob_ekyc);
        imageButtonAOBEkyc = findViewById(R.id.imageButtonAOBEkyc);
        textviewAOBEkyc = findViewById(R.id.textviewAOBEkyc);

        ll_aob_ekyc.setOnClickListener(this);
        imageButtonAOBEkyc.setOnClickListener(this);
        textviewAOBEkyc.setOnClickListener(this);

        ll_aob_manual_kyc = findViewById(R.id.ll_aob_manual_kyc);
        imageButtonAOBManualKYC = findViewById(R.id.imageButtonAOBManualKYC);
        textviewAOBManualKYC = findViewById(R.id.textviewAOBManualKYC);

        ll_aob_manual_kyc.setOnClickListener(this);
        imageButtonAOBManualKYC.setOnClickListener(this);
        textviewAOBManualKYC.setOnClickListener(this);

        ll_manual_kyc_details = findViewById(R.id.ll_manual_kyc_details);

        ll_aob_aadhaar_details = findViewById(R.id.ll_aob_aadhaar_details);
        ll_aob_aadhaar_details.setVisibility(View.GONE);

        rb_aob_aadhaar_finger_print.setChecked(false);
        rb_aob_aadhaar_IRIS.setChecked(false);
        rb_aob_aadhaar_OTP.setChecked(false);

        rb_aob_aadhaar_finger_print.setClickable(false);
        rb_aob_aadhaar_finger_print.setEnabled(false);

        rb_aob_aadhaar_IRIS.setClickable(false);
        rb_aob_aadhaar_IRIS.setEnabled(false);

        rb_aob_aadhaar_OTP.setClickable(false);
        rb_aob_aadhaar_OTP.setEnabled(false);

        iv_aob_aadhaar_ekyc_fingerprintcapture.setImageBitmap(null);
        tv_aob_aadhaar_textViewMessage.setText("");
        //vertical_aob_aadhaar_progressbar.setProgress(0);

        eSignAuth = "";
        /*base64FingerTemplate = "";
        deviceCode = null;*/

        /*try {
            String devicename = Build.MODEL;
            if (mSupportedDevicelist.get(devicename) == null) {
                rb_aob_aadhaar_IRIS.setVisibility(View.INVISIBLE);
            } else {
                mLicenseMgr = SecBiometricLicenseManager.getInstance(this);
                if (!hasPermission()) {
                    activateIrisLicense();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            rb_aob_aadhaar_IRIS.setVisibility(View.INVISIBLE);
        }*/


        edt_aob_auth_pan.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                str_aob_dob = txt_aob_auth_dob.getText().toString();
                str_aob_dob = str_aob_dob == null ? "" : str_aob_dob;
                if (!str_aob_dob.equals("")) {
                    str_pan_no = edt_aob_auth_pan.getText().toString().replaceAll("\\s+", "").trim();
                    str_pan_no = str_pan_no == null ? "" : str_pan_no;

                    if (str_pan_no.length() == 10) {
                        validate_pan_card = mCommonMethods.valPancard(str_pan_no, edt_aob_auth_pan);

                        if (validate_pan_card) {

                            //check wheter pan is available or not
                            ArrayList<String> lstRslt = db.get_POS_RA_ID(str_pan_no);

                            if (lstRslt.size() > 0) {

                                //depending upon status navigate

                                row_details = Integer.parseInt(lstRslt.get(0));

                                String str_status = lstRslt.get(1);

                                //get training end date
                            /*String str_training_end_date = lstRslt.get(2).toString() == null ? "" : lstRslt.get(2).toString();

                            if (!str_training_end_date.equals("")){
                                str_training_end_date = new ParseXML().parseXmlTag(lstRslt.get(2).toString(),
                                        "training_details_end_date");
                                String[] arrEndDate = str_training_end_date.split("-");
                                str_training_end_date = arrEndDate[1] + "-" + arrEndDate[0] + "-" + arrEndDate[2];
                            }*/

                                Intent mIntent;
                                switch (str_status) {

                                    case "1":

                                    case "13":
                                        //mCommonMethods.showToast(mContext, "Synch failure... try dashboard menu");
                                        //call personal info page with editable
                                        mIntent = new Intent(Activity_POSP_RA_Authentication.this, Activity_POSP_RA_PersonalInfo.class);
                                        mIntent.putExtra("is_dashboard", false);
                                        startActivity(mIntent);
                                        break;

                                    case "2":
                                        //call occupational depatails page with editable
                                        mIntent = new Intent(Activity_POSP_RA_Authentication.this, Activity_POSP_RA_Occupation.class);
                                        mIntent.putExtra("is_dashboard", false);
                                        startActivity(mIntent);
                                        break;

                                    case "3":
                                        //call nominee info page with editable
                                        mIntent = new Intent(Activity_POSP_RA_Authentication.this, Activity_POSP_RA_Nomination.class);
                                        mIntent.putExtra("is_dashboard", false);
                                        startActivity(mIntent);
                                        break;

                                    case "4":
                                        //call bank info page with editable
                                        mIntent = new Intent(Activity_POSP_RA_Authentication.this, Activity_POSP_RA_BankDetails.class);
                                        mIntent.putExtra("is_dashboard", false);
                                        startActivity(mIntent);
                                        break;

                                    case "5":
                                        //call exam and training info page with editable
                                        mIntent = new Intent(Activity_POSP_RA_Authentication.this, Activity_POSP_RA_ExamTraining.class);
                                        mIntent.putExtra("is_dashboard", false);
                                        startActivity(mIntent);
                                        break;

                                    //call declarations and conditions page with editable
                                    case "6":

                                        //for declarations and conditions data saved
                                    case "7":


                                        //for agent form complet sync
                                    case "8":

                                        //for customer photo complet sync
                                    case "9":
                                        mIntent = new Intent(Activity_POSP_RA_Authentication.this, Activity_POSP_RA_TermsConditionsDeclaration.class);
                                        mIntent.putExtra("is_dashboard", false);
                                        startActivity(mIntent);
                                        break;

                                    case "10":

                                    case "11":

                                    case "12":
                                        //compare current date with training end date

                                        //current date
                                    /*Calendar cal = Calendar.getInstance();
                                    String mont = ((cal.get(Calendar.MONTH) + 1) < 10 ? "0" : "") + (cal.get(Calendar.MONTH) + 1);
                                    String day = (cal.get(Calendar.DAY_OF_MONTH) < 10 ? "0" : "") + cal.get(Calendar.DAY_OF_MONTH);

                                    String str_current_date = day + "-" + mont + "-" + cal.get(Calendar.YEAR);
                                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

                                    Date currentDate = null, trainingEndDate = null;

                                    try {
                                        currentDate = dateFormat.parse(str_current_date);
                                        trainingEndDate = dateFormat.parse(str_training_end_date);

                                        if (trainingEndDate.before(currentDate) || trainingEndDate.equals(currentDate)) {*/
                                        //call doc upload page and conditions page with editable
                                        mIntent = new Intent(Activity_POSP_RA_Authentication.this, Activity_POSP_RA_DocumentUpload.class);
                                        mIntent.putExtra("is_dashboard", false);
                                        startActivity(mIntent);
                                        /*}else{
                                            mCommonMethods.showMessageDialog(mContext,
                                                    "The application can be submitted only upon completion of the training");
                                        }

                                    } catch (ParseException ex) {
                                        mCommonMethods.printLog("parse Error : ", ex.getMessage());
                                    }*/
                                        break;


                                    case "14":

                                        mCommonMethods.showMessageDialog(mContext, "This PAN Number Has Already Synched Details \n"
                                                + "You Can view Details by Dashboard");

                                        break;

                                    default:
                                        imgbtn_aob_auth_pan_camera.setEnabled(true);

                                        imgbtn_aob_auth_pan_browse.setEnabled(true);

                                        imgbtn_aob_auth_pan_upload.setEnabled(true);
                                        break;
                                }
                            } else {
                                //insert data in to table
                                try {

                                    //save date in long
                                    Calendar c = Calendar.getInstance();

                                    //insert first to status and remark table
                                    cv.clear();
                                    cv.put(db.POSP_RA_PAN_NO, str_pan_no);
                                    cv.put(db.POSP_RA_PAN_DETAILS, "");
                                    cv.put(db.POSP_RA_CREATED_BY, strCIFBDMUserId);
                                    cv.put(db.POSP_RA_CREATED_DATE, new Date(c.getTimeInMillis()).getTime() + "");

                                    cv.put(db.POSP_RA_IN_APP_STATUS, "0");
                                    cv.put(db.POSP_RA_IN_APP_STATUS_REMARK, "New Candidate PAN inserted");
                                    cv.put(db.POSP_RA_AGENCY_TYPE, mCommonMethods.str_posp_ra_customer_type);

                                    row_details = db.insert_POSP_RA(cv);

                                    imgbtn_aob_auth_pan_camera.setEnabled(true);

                                    imgbtn_aob_auth_pan_browse.setEnabled(true);

                                    imgbtn_aob_auth_pan_upload.setEnabled(true);

                                } catch (Exception e) {
                                    e.printStackTrace();
                                    mCommonMethods.showToast(mContext, "KYC Details Saving Failed");
                                }
                            }
                        } else {
                            edt_aob_auth_pan.setError("Invalid PAN Number");

                            validate_pan_card = false;

                            imgbtn_aob_auth_pan_camera.setEnabled(false);

                            imgbtn_aob_auth_pan_browse.setEnabled(false);

                            imgbtn_aob_auth_pan_upload.setEnabled(false);
                        }

                    } else {

                        validate_pan_card = false;

                        imgbtn_aob_auth_pan_camera.setEnabled(false);

                        imgbtn_aob_auth_pan_browse.setEnabled(false);

                        imgbtn_aob_auth_pan_upload.setEnabled(false);
                    }
                } else {
                    mCommonMethods.showToast(mContext,
                            "Please select Advisor's(Applicant) Date of Birth..");
                }
            }
        });

        //ekyc removed
        /*edt_aob_auth_aadhaar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                str_aashaar_no = edt_aob_auth_aadhaar.getText().toString().replaceAll("\\s+", "").trim();
                if (str_aashaar_no.length() == 12 || str_aashaar_no.length() == 16) {
                    if (!Verhoeff.validateVerhoeff(str_aashaar_no)) {

                        if (str_aashaar_no.length() == 12)
                            edt_aob_auth_aadhaar.setError("Incorrect Aadhaar Number");
                        else
                            edt_aob_auth_aadhaar.setError("Incorrect Virtual ID");

                        validate_aadhar = false;

                        rb_aob_aadhaar_finger_print.setClickable(false);
                        rb_aob_aadhaar_finger_print.setEnabled(false);

                        rb_aob_aadhaar_IRIS.setClickable(false);
                        rb_aob_aadhaar_IRIS.setEnabled(false);

                        rb_aob_aadhaar_OTP.setClickable(false);
                        rb_aob_aadhaar_OTP.setEnabled(false);

                        iv_aob_aadhaar_ekyc_fingerprintcapture.setImageBitmap(null);
                        tv_aob_aadhaar_textViewMessage.setText("");
                        //vertical_aob_aadhaar_progressbar.setProgress(0);

                        eSignAuth = "";
                        base64FingerTemplate = "";
                        deviceCode = null;

                    } else {
                        //edt_ekyc_aadhar_no.setError(null);
                        validate_aadhar = true;

                            rg_aob_ekyc_type.setVisibility(View.VISIBLE);

                            btn_aob_aadhaar_ok.setText("Submit");

                            rb_aob_aadhaar_finger_print.setClickable(true);
                            rb_aob_aadhaar_finger_print.setEnabled(true);

                            rb_aob_aadhaar_IRIS.setClickable(true);
                            rb_aob_aadhaar_IRIS.setEnabled(true);

                            rb_aob_aadhaar_OTP.setClickable(true);
                            rb_aob_aadhaar_OTP.setEnabled(true);
                    }
                } else if (str_aashaar_no.length() > 12){
                    edt_aob_auth_aadhaar.setError("Incorrect Virtual ID");
                    validate_aadhar = false;

                    rg_aob_ekyc_type.setVisibility(View.GONE);

                    btn_aob_aadhaar_ok.setText("Submit");

                    rb_aob_aadhaar_finger_print.setClickable(false);
                    rb_aob_aadhaar_finger_print.setEnabled(false);

                    rb_aob_aadhaar_IRIS.setClickable(false);
                    rb_aob_aadhaar_IRIS.setEnabled(false);

                    rb_aob_aadhaar_OTP.setClickable(false);
                    rb_aob_aadhaar_OTP.setEnabled(false);

                    iv_aob_aadhaar_ekyc_fingerprintcapture.setImageBitmap(null);
                    tv_aob_aadhaar_textViewMessage.setText("");
                    //vertical_aob_aadhaar_progressbar.setProgress(0);

                    eSignAuth = "";
                    base64FingerTemplate = "";
                    deviceCode = null;
                }else {
                    edt_aob_auth_aadhaar.setError("Incorrect Aadhaar Number");
                    validate_aadhar = false;

                    rg_aob_ekyc_type.setVisibility(View.GONE);

                    btn_aob_aadhaar_ok.setText("Submit");

                    rb_aob_aadhaar_finger_print.setClickable(false);
                    rb_aob_aadhaar_finger_print.setEnabled(false);

                    rb_aob_aadhaar_IRIS.setClickable(false);
                    rb_aob_aadhaar_IRIS.setEnabled(false);

                    rb_aob_aadhaar_OTP.setClickable(false);
                    rb_aob_aadhaar_OTP.setEnabled(false);

                    iv_aob_aadhaar_ekyc_fingerprintcapture.setImageBitmap(null);
                    tv_aob_aadhaar_textViewMessage.setText("");
                    //vertical_aob_aadhaar_progressbar.setProgress(0);

                    eSignAuth = "";
                    base64FingerTemplate = "";
                    deviceCode = null;
                }
            }
        });*/

        rg_aob_ekyc_type.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId) {
                    case R.id.rb_aob_aadhaar_OTP:

                        if (validate_aadhar) {
                            // rb_OTP.setChecked(true);
                            iv_aob_aadhaar_ekyc_fingerprintcapture.setImageBitmap(null);
                            tv_aob_aadhaar_textViewMessage.setText("");
                            //vertical_aob_aadhaar_progressbar.setProgress(0);

                            eSignAuth = "OTP";
                            /*base64FingerTemplate = "";
                            deviceCode = null;*/
                            ll_aob_aadhaar_OTP.setVisibility(View.VISIBLE);
                            ll_aob_aadhaar_IRIS.setVisibility(View.GONE);
                            ll_aob_aadhaar_Fingerprint.setVisibility(View.GONE);

                        } else {
                            mCommonMethods.showToast(mContext, "Please enter valid aadhaar card number");
                        }

                        break;

                    case R.id.rb_aob_aadhaar_finger_print:

                        if (validate_aadhar) {
                            eSignAuth = "BIOMETRIC";
                            //base64FingerTemplate = "";
                            ll_aob_aadhaar_OTP.setVisibility(View.GONE);
                            ll_aob_aadhaar_IRIS.setVisibility(View.GONE);
                            ll_aob_aadhaar_Fingerprint.setVisibility(View.VISIBLE);

                        } else {
                            mCommonMethods.showToast(mContext, "Please enter valid aadhaar card number");
                        }

                        break;

                    case R.id.rb_aob_aadhaar_IRIS:

                        if (validate_aadhar) {
                            // rb_IRIS.setChecked(true);
                            iv_aob_aadhaar_ekyc_fingerprintcapture.setImageBitmap(null);
                            tv_aob_aadhaar_textViewMessage.setText("");
                            //vertical_aob_aadhaar_progressbar.setProgress(0);

                            //base64FingerTemplate = "";
                            eSignAuth = "IRIS";

                            // secIrisManager = SecIrisManager.getInstance();
                            // mSensorManager = (SensorManager)
                            // getSystemService(SENSOR_SERVICE);

                            ll_aob_aadhaar_OTP.setVisibility(View.GONE);
                            ll_aob_aadhaar_Fingerprint.setVisibility(View.GONE);
                            ll_aob_aadhaar_IRIS.setVisibility(View.VISIBLE);

                        } else {
                            mCommonMethods.showToast(mContext, "Please enter valid aadhaar card number");
                        }

                        break;

                    default:
                        break;
                }

            }
        });

    }

    /*private void activateIrisLicense() {
        try {

            String key = "5B481893587214DD076EE91D706FE2DE704F8EEE351C8A6C6E81BAE63C6BE8BE201167A9B97A4421864D7C03685957016B474B066AC9C58FCB27B831DC084524";

            String packageName = getApplicationContext().getPackageName();
            Log.i("Activation", "packageName: " + packageName);
            mLicenseMgr.activateLicense(key, packageName);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }*/

    private boolean hasPermission() {
        try {
            String BIOMETRIC_LICENSE_PERMISSION = "com.sec.enterprise.biometric.permission.IRIS_RECOGNITION";
            PackageManager packageManager = this.getApplicationContext()
                    .getPackageManager();

            if (packageManager.checkPermission(BIOMETRIC_LICENSE_PERMISSION,
                    this.getApplicationContext().getPackageName()) == PackageManager.PERMISSION_GRANTED) {
                return true;
            }
        } catch (Exception ignored) {
        }
        return false;
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (mAsyncGenerateOTP != null)
            mAsyncGenerateOTP.cancel(true);

        if (mAsyncGetGeneratedOTP != null)
            mAsyncGetGeneratedOTP.cancel(true);

        if (mAsyncCaptureRDIRISSuccess != null)
            mAsyncCaptureRDIRISSuccess.cancel(true);

        if (mAsyncFingerPrintRDServiceCaptureSuccess != null)
            mAsyncFingerPrintRDServiceCaptureSuccess.cancel(true);

        if (mAsyncGetcKYCSearchDetail != null)
            mAsyncGetcKYCSearchDetail.cancel(true);
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        switch (view.getTag()) {
            case DATE_APPLICANT_DOB:
                String strSelectedDate = (dayOfMonth < 10 ? "0" : "") + dayOfMonth + "-"
                        + ((monthOfYear + 1) < 10 ? "0" : "") + (monthOfYear + 1) + "-"
                        + year;
                txt_aob_auth_dob.setText(strSelectedDate);
                break;
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.txtPanLookUpLink:
                panLookupClickedCount++;
                mCommonMethods.openWebLink(mContext, "https://agencyportal.irdai.gov.in/PublicAccess/LookUpPAN.aspx");
                break;

            case R.id.ll_aob_dashboard:

            case R.id.imageButton_aob_dashboard:

            case R.id.textview_aob_dashboard:
                startActivity(new Intent(Activity_POSP_RA_Authentication.this, Activity_POSP_RA_Dashboard.class));
                break;

            /*case R.id.imageButton_rejecti_remark:

            case R.id.textview_rejecti_remark:

            case R.id.ll_psop_rejecti_remark:
                Intent mIntent = new Intent(new Intent(Activity_POSP_RA_Authentication.this, Activity_POSP_RA_Rejection_Remarks.class));
                mIntent.putExtra("enrollment_type", mCommonMethods.str_posp_ra_customer_type);
                startActivity(mIntent);
                break;*/

            case R.id.imgbtn_aob_auth_pan_view:
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

            case R.id.imgbtn_aob_auth_pan_camera:

                if (validate_pan_card) {
                    capture_document();
                    //capture_docs(PAN_DOC, "CAMERA");
                } else {
                    mCommonMethods.showMessageDialog(mContext, "Please Enter PAN Number Proper");
                }

                break;

            case R.id.imgbtn_aob_auth_pan_browse:

                if (validate_pan_card) {
                    browse_docs();
                    //capture_docs(PAN_DOC, "BROWSE");
                } else {
                    mCommonMethods.showMessageDialog(mContext, "Please Enter PAN Number Proper");
                }

                break;

            case R.id.imgbtn_aob_auth_pan_upload:

                if (panLookupClickedCount >= 1) {
                    is_pan_file_uploaded = false;

                    //Check PAN already exists
                    str_doc = METHOD_NAME_CHECK_PAN_EXISTS;
                    SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME_CHECK_PAN_EXISTS);

                    request.addProperty("strPAN", str_pan_no);
                    request.addProperty("strSource", "SB!L!F@");
                    request.addProperty("strAuthKey", "SBI@@L!");

                    mAsyncUploadFileCommon = new AsyncUploadFile_Common(mContext,
                            Activity_POSP_RA_Authentication.this, request,
                            METHOD_NAME_CHECK_PAN_EXISTS);
                    mAsyncUploadFileCommon.execute();

                } else {
                    mCommonMethods.showMessageDialog(mContext, "Please check your PAN with Lookup Link first!");
                }
                break;

            case R.id.btn_aob_aadhaar_ok:

                //ekyc removed
                /*//commented 08-04-2018 for non mendate aadhar purpose
                if (rb_aob_aadhaar_OTP.isChecked()) {

                    str_generated_otp = edt_aob_aadhaar_otp.getText().toString();

                    if (str_generated_otp.equals("")) {
                        mCommonMethods.showToast(mContext, "Please enter valid OTP");
                    } else {
                        mAsyncGetGeneratedOTP = new AsyncGetGeneratedOTP();
                        mAsyncGetGeneratedOTP.execute();
                    }
                    //save mode of ekyc to xml responce
                } else if (rb_aob_aadhaar_IRIS.isChecked()) {
                    if (rb_aob_aadhaar_single_iris.isChecked() || rb_aob_aadhaar_dual_iris.isChecked()) {

                        aob_iris_single_checked = rb_aob_aadhaar_single_iris.isChecked();
                        StartRDIRISCapture();

                    } else {
                        clearFocusable(rb_aob_aadhaar_single_iris);
                        clearFocusable(rb_aob_aadhaar_dual_iris);
                        mCommonMethods.showToast(mContext, "Please select capture type");
                        // uploadHashPdf();
                    }
                } else if (rb_aob_aadhaar_finger_print.isChecked()) {
                    Intent intentCapture = new Intent(Activity_AOB_Authentication.this,
                            ECSBioCaptureActivity.class);
                    intentCapture.putExtra("OPERATION", "K");
                    intentCapture.putExtra("DEVICETYPE", "F");
                    // Environment values are S for Staging,
                    // PP for pre-production and P for Production
                    intentCapture.putExtra("UIDAIENVIRONMENT", "P");
                    intentCapture.putExtra("PFR", true);
                    //eKYC 2.5
                    intentCapture.putExtra("DE", false);
                    intentCapture.putExtra("LR", true);
                    intentCapture.putExtra("KYCVER", "2.5");
                    startActivityForResult(intentCapture, DEVICE_CAPTURE_REQUEST_CODE);
                } else {
                    mCommonMethods.showMessageDialog(mContext, "Please select mode of EKYC");
                }*/

                break;

            case R.id.btn_aob_aadhaar_GenerateOTP:

                if (validate_aadhar) {

                    if (mCommonMethods.isNetworkConnected(mContext)) {
                        mAsyncGenerateOTP = new AsyncGenerateOTP();
                        mAsyncGenerateOTP.execute();
                    } else {
                        mCommonMethods.showToast(mContext, mCommonMethods.NO_INTERNET_MESSAGE);
                    }
                } else {
                    edt_aob_auth_aadhaar.requestFocus();
                    mCommonMethods.showToast(mContext, "Please enter valid aadhaar card number");
                }

                break;

            case R.id.btn_aob_aadhaar_cancel:
                onBackPressed();
                break;

            case R.id.ll_aob_ekyc:

            case R.id.imageButtonAOBEkyc:

            case R.id.textviewAOBEkyc:

                if (validate_pan_card) {

                    final Dialog dialog = new Dialog(mContext);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.loading_window_twobutton);
                    TextView dialog_title = dialog.findViewById(R.id.dialog_title);
                    dialog_title.setText("Communication Address");
                    TextView text = dialog.findViewById(R.id.txtalertheader);
                    text.setText("Is the latest Communication Address updated on the AADHAR Card?\nIf NOT please follow the Manual KYC process.\nIf YES please follow the Offline-eKYC");
                    Button dialogButton = dialog.findViewById(R.id.btnalert);
                    dialogButton.setText("Manual KYC");
                    dialogButton.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            dialog.dismiss();

                            /*Intent mIntent = new Intent(Activity_AOB_Authentication.this, ActivityAOBBasicDetails.class);*/
                            Intent mIntent = new Intent(Activity_POSP_RA_Authentication.this, Activity_POSP_RA_PersonalInfo.class);
                            mIntent.putExtra("is_dashboard", false);
                            startActivity(mIntent);
                        }
                    });
                    Button dialogButtoncancel = dialog.findViewById(R.id.btnalertcancel);
                    dialogButtoncancel.setText("Offline-eKYC");
                    dialogButtoncancel.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            dialog.dismiss();

                            if (mCommonMethods.isNetworkConnected(mContext)) {
                                Intent intent = new Intent(Activity_POSP_RA_Authentication.this,
                                        OfflineKYCActivity.class);
                                intent.putExtra("proposer_name", "");
                                intent.putExtra("planName", "");
                                intent.putExtra("QuatationNumber", str_pan_no);
                                startActivityForResult(intent, OFFLINE_EKYC_ACTIVITY);
                            } else {
                                mCommonMethods.showToast(mContext, "Please Check the Internet Connection");
                            }
                        }
                    });

                    dialog.show();

                } else {
                    mCommonMethods.showMessageDialog(mContext, "Please Enter PAN Number Proper");
                }

                break;

            case R.id.ll_aob_manual_kyc:

            case R.id.imageButtonAOBManualKYC:

            case R.id.textviewAOBManualKYC:

                if (validate_pan_card) {
                    /*Intent mIntent = new Intent(Activity_AOB_Authentication.this, ActivityAOBBasicDetails.class);*/
                    Intent mIntent1 = new Intent(Activity_POSP_RA_Authentication.this, Activity_POSP_RA_PersonalInfo.class);
                    mIntent1.putExtra("is_dashboard", false);
                    startActivity(mIntent1);
                } else {
                    mCommonMethods.showMessageDialog(mContext, "Please Enter PAN Number Proper");
                }

                break;

            case R.id.txt_aob_auth_dob:
                datePickerDialog = DatePickerDialog.newInstance(Activity_POSP_RA_Authentication.this,
                        mDOBYear, mDOBMonth, mDOBDay);
                datePickerDialog.setThemeDark(false);
                datePickerDialog.showYearPickerFirst(false);
                /*//future date not allowed
                datePickerDialog.setMinDate(mCalender);*/

                //Applicant should be above 18 years.
                Calendar max_date_c = Calendar.getInstance();
                max_date_c.set(Calendar.YEAR, mDOBYear - 18);
                datePickerDialog.setMaxDate(max_date_c);

                datePickerDialog.dismissOnPause(true); //dismiss dialog when onPause() called?
                datePickerDialog.setAccentColor(getResources().getColor(R.color.Common_blue));
                datePickerDialog.setTitle("Applicant DOB");

                datePickerDialog.show(getFragmentManager(), DATE_APPLICANT_DOB);
                break;

            default:
                break;
        }

    }

    @Override
    public void onBackPressed() {
        Bundle mBundle = new Bundle();
        mBundle.putString("STRING_DATA", mCommonMethods.str_posp_ra_customer_type);

        Intent mIntent = new Intent(Activity_POSP_RA_Authentication.this, ActivityAOB_Menu.class);
        mIntent.putExtras(mBundle);
        startActivity(mIntent);
    }

    private String getErrorMessage(String error) {
        int startIndex = "<Error>".length();
        int endIndex = error.indexOf("</Error>");

        if (endIndex != -1) {
            return error.substring(startIndex, endIndex);
        }

        return "";
    }

    private void StartRDIRISCapture() {
        Intent intentCapture = new Intent("in.gov.uidai.rdservice.iris.CAPTURE");

        PackageManager packageManager = getPackageManager();
        List activities = packageManager.queryIntentActivities(intentCapture,
                PackageManager.MATCH_DEFAULT_ONLY);
        boolean isIntentSafe = activities.size() > 0;

        if (!isIntentSafe) {

            mCommonMethods.showMessageDialog(mContext, "Error:\nIRIS Scanner Support not available. Please update the latest software in your device");
            return;
        }

        int numeye = 2;
        if (aob_iris_single_checked)
            numeye = 1;

        Opts opts = new Opts();
        opts.setiCount(String.valueOf(numeye));
        opts.setiType("0");
        opts.setFormat("0");
        opts.setPidVer("2.0");
        opts.setTimeout("10000");
        opts.setPosh("UNKNOWN");
        // Environment values are S for Staging,
        // PP for pre-production and P for Production
        opts.setEnv("P");

        String wadh = null;

        // ***************************************************************************************************************
        // Do this for KYC ONLY
        try {

            /*wadh = Base64.encodeToString(
                    getKycWadh("2.1", false, true, false, true, true, true,
                            true), Base64.NO_WRAP);*/

            //eKYC 2.5
            wadh = Base64.encodeToString(
                    getKycWadh("2.5", false, true, false, true, true, false,
                            true), Base64.NO_WRAP);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        opts.setWadh(wadh);
        // ***************************************************************************************************************

        CustOpts custOpts = new CustOpts();
        custOpts.setParams(new ArrayList<Param>());

        Demo demo = null;

        PidOptions pidOptions = new PidOptions();
        pidOptions.setVer("1.0");
        pidOptions.setOpts(opts);
        pidOptions.setCustOpts(custOpts);
        pidOptions.setDemo(new Demo());
        String pidOptXml = "";

        try {
            pidOptXml = getXMLNoFormatting(pidOptions);
        } catch (Exception ex) {
            mCommonMethods.showMessageDialog(mContext, "Error:\n" + ex.getMessage());
            return;
        }

        pidOptXml = pidOptXml.replace("<CustOpts/>", "<CustOpts></CustOpts>");
        pidOptXml = pidOptXml.replace("<Demo/>", "<Demo></Demo>");

        // Log.d("PIDOptions", pidOptXml);

        intentCapture.putExtra("PID_OPTIONS", pidOptXml);
        startActivityForResult(intentCapture, IRIS_CAPTURE_REQUEST_CODE);
    }

    private String getXMLNoFormatting(Object input) throws Exception {
        StringWriter xml = new StringWriter();
        Format format = new Format(0, Verbosity.LOW);

        Serializer serializer = new Persister(format);
        try {
            serializer.write(input, xml);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }
        return xml.toString();
    }

    private Object parseXML(Class clazz, String xmlToParse) throws Exception {
        Serializer serializer = new Persister();
        Reader reader = new StringReader(xmlToParse);
        return serializer.read(clazz, reader, false);
    }

    private byte[] getKycWadh(String kycVersion, boolean fingerprint,
                              boolean iris, boolean otp, boolean rc, boolean lr, boolean de,
                              boolean pfr) throws Exception {
        String ra = "";

        if (fingerprint)
            ra += "F";
        if (iris)
            ra += "I";
        if (otp)
            ra += "O";

        if (ra.isEmpty())
            throw new Exception("Invalid value for Ra for generating Wadh");

        if (!rc)
            throw new Exception("Invalid Resident consent");

        String data = kycVersion + ra + (rc ? "Y" : "N") + (lr ? "Y" : "N")
                + (de ? "Y" : "N") + (pfr ? "Y" : "N");

        byte[] hash = null;

        MessageDigest digest;
        digest = MessageDigest.getInstance("SHA-256");
        digest.reset();
        hash = digest.digest(data.getBytes());

        return hash;
    }

    // method to set a clearing a element
    private void clearFocusable(View v) {
        // TODO Auto-generated method stub
        v.setFocusable(false);
        v.setFocusableInTouchMode(false);
        // v.clearFocus();
    }

    private void showDialogGogoglePayStore(String title, String text) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(title);
        builder.setMessage(text);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                Intent myWebLink = new Intent(Intent.ACTION_VIEW);
                myWebLink.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.scl.rdservice&hl=en"));
                startActivity(myWebLink);
            }
        });
        Dialog dlg = builder.create();
        dlg.show();
    }

    private void registeredIfRequired() {
        Intent intent = new Intent("android.intent.action.SCL_RDSERVICE_OTP_RECIEVER");
        intent.putExtra("OTP", "SCL-9999-8888-777");
        intent.setPackage("com.scl.rdservice");
        // sendBroadcast(intent);
        try {
            sendBroadcast(intent);
        } catch (Exception e) {
            e.printStackTrace();
            mCommonMethods.showToast(mContext, "RD Service not installed");
        }
    }

    public void capture_document() {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        String imageFileName = str_pan_no + str_doc + ".jpg";

        //mCapturedImgFile = mCommonMethods.createCaptureImg(str_pan_no + str_doc + ".jpg");
        mCapturedImgFile = mStorageUtils.createFileToAppSpecificDir(mContext, imageFileName);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mCommonMethods.getContentUri(mContext,
                    mCapturedImgFile));
        } else {
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mCapturedImgFile));
        }

        startActivityForResult(takePictureIntent, REQUEST_CODE_CAPTURE_DOCUMENT);
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
        //mIntent.setType("application/pdf");
        mIntent.setType("*/*");
            /*String[] mimeType = new String[]{"application/x-binary,application/octet-stream"};
            if(mimeType.length > 0) {
                mIntent.putExtra(Intent.EXTRA_MIME_TYPES, mimeType);
            }*/
        mBrowseDocLauncher.launch(mIntent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                Uri resultUri = result.getUri();
                mCapturedImgFile = new File(resultUri.getPath());
                if (mCapturedImgFile != null) {
                    CompressImage.compressImage(mCapturedImgFile.getPath());

                    long size = mCapturedImgFile.length();
                    double kilobyte = size / 1024;

                    //2 MB valiadation
                    if (kilobyte < mCommonMethods.FILE_UPLOAD_RESTRICT_SIZE) {

                        String imageFileName = str_pan_no + "_" + str_doc + ".pdf";
                        SelfAttestedDocumentActivity obj = new SelfAttestedDocumentActivity();

                        mPanFile = mStorageUtils.createFileToAppSpecificDir(mContext, imageFileName);

                        if (obj.createAOBDOcumentPdf(mPanFile, mCapturedImgFile, "PAN Document")) {

                            if (is_browse) {
                                imgbtn_aob_auth_pan_camera.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_camera));
                                imgbtn_aob_auth_pan_browse.setImageDrawable(getResources().getDrawable(R.drawable.checkedbrowse));
                            } else {
                                imgbtn_aob_auth_pan_camera.setImageDrawable(getResources().getDrawable(R.drawable.checkedcamera));
                                imgbtn_aob_auth_pan_browse.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_browsedoc));
                            }

                            //remove common source file from directory
                            mCapturedImgFile.delete();
                        } else {
                            mCommonMethods.showMessageDialog(mContext, "File Not Found..");
                        }

                    } else {
                        mCommonMethods.showMessageDialog(mContext, mCommonMethods.FILE_UPLOAD_RESTRICT_SIZE_MSG);
                    }
                }
                is_browse = false;
            } else if (requestCode == OFFLINE_EKYC_ACTIVITY) {
                try {
                    /*String resString = db.getKYCDetails(QuatationNumber);

                    resString = SimpleCrypto.decrypt("SBIL", resString);*/

                    final String resXMLRes = data.getStringExtra("KYC_XML") == null ? "" : data.getStringExtra("KYC_XML");

                    //3. update data against global row id
                    cv.clear();
                    cv.put(db.POSP_RA_AADHAAR_NO, "");
                    cv.put(db.POSP_RA_AADHAAR_DETAILS, resXMLRes);
                    cv.put(db.POSP_RA_UPDATED_BY, strCIFBDMUserId);

                    Calendar c = Calendar.getInstance();
                    //save date in long
                    cv.put(db.POSP_RA_UPDATED_DATE, new Date(c.getTimeInMillis()).getTime() + "");
                    cv.put(db.POSP_RA_IN_APP_STATUS, "1");
                    cv.put(db.POSP_RA_IN_APP_STATUS_REMARK, "Off-line EKyc Done");

                    db.update_POSP_RA_details(cv, db.POSP_RA_ID + " =? ",
                            new String[]{Activity_POSP_RA_Authentication.row_details + ""});

                    final OfflinePaperlessKycResponse res = (OfflinePaperlessKycResponse) XMLUtilities
                            .parseXML(OfflinePaperlessKycResponse.class, resXMLRes);

                    final UidData res_UidData = res.getUidData();

                    String str_Address_title = res_UidData.getPoa().getCo() == null ? ""
                            : res_UidData.getPoa().getCo();

                    String str_aad_Address1 = (res_UidData.getPoa().getLoc() == null ? ""
                            : res_UidData.getPoa().getLoc() + " ")
                            + (res_UidData.getPoa().getHouse() == null ? "" : res_UidData
                            .getPoa().getHouse() + " ")
                            + (res_UidData.getPoa().getStreet() == null ? "" : res_UidData
                            .getPoa().getStreet() + " ")
                            + (res_UidData.getPoa().getLm() == null ? "" : res_UidData.getPoa()
                            .getLm());
                    String str_aad_Address2 = (res_UidData.getPoa().getSubdist() == null ? ""
                            : res_UidData.getPoa().getSubdist() + " ")

                            + (res_UidData.getPoa().getPo() == null ? "" : res_UidData.getPoa()
                            .getPo() + " ");

                    String str_aad_Address3 = (res_UidData.getPoa().getVtc() == null ? ""
                            : res_UidData.getPoa().getVtc());
                    String str_PinCode = (res_UidData.getPoa().getPc() == null ? ""
                            : res_UidData.getPoa().getPc());
                    String str_City = (res_UidData.getPoa().getDist() == null ? ""
                            : res_UidData.getPoa().getDist());
                    String str_State = (res_UidData.getPoa().getState() == null ? ""
                            : res_UidData.getPoa().getState());

                    /*str_QR_code_mailID = res_UidData.getPoi().getEmail() == null ? ""
                            : res_UidData.getPoi().getEmail();
                    str_QR_code_mobile = res_UidData.getPoi().getPhone() == null ? ""
                            : res_UidData.getPoi().getPhone();*/


                    str_ekyc_code_DOB = res_UidData.getPoi().getDob() == null ? ""
                            : res_UidData.getPoi().getDob();

                    boolean isValidDate = mCommonMethods.isThisDateValid(str_ekyc_code_DOB);

                    if (isValidDate == false) {
                        str_ekyc_code_DOB = "01-01-1990";
                    }

                    str_ekyc_code_Name = res_UidData.getPoi().getName() == null ? ""
                            : res_UidData.getPoi().getName();
                    str_ekyc_code_Gender = res_UidData.getPoi().getGender() == null ? ""
                            : res_UidData.getPoi().getGender();
                    str_QR_code_Photo = res.getUidData().getPht() == null ? ""
                            : res.getUidData().getPht();


                    if (!(str_Address_title.equals(""))) {
                        str_Address_title = str_Address_title.substring(0, 3);
                    } else {
                        str_Address_title = "C/O";
                    }

                    String str_Address1 = "", str_Address2 = "", str_Address3 = "";

                    String[] lines = mCommonMethods.Split_EKYC((str_aad_Address1 + " "
                            + str_aad_Address2 + " " + str_aad_Address3), 45, 135);

                    for (int i = 0; i < lines.length; i++) {

                        if (i == 0) {
                            str_Address1 = lines[i];
                        } else if (i == 1) {
                            str_Address2 = lines[i];

                        } else if (i == 2) {
                            str_Address3 = lines[i];
                        }

                    }

                    if (!str_Address_title.equalsIgnoreCase("")) {
                        str_Address_title = str_Address_title + ", ";
                    }

                    if (!str_Address1.equalsIgnoreCase("")) {
                        str_Address1 = str_Address1 + ", ";
                    }
                    if (!str_Address2.equalsIgnoreCase("")) {
                        str_Address2 = str_Address2 + ", ";
                    }
                    if (!str_Address3.equalsIgnoreCase("")) {
                        str_Address3 = str_Address3 + "-";
                    }
                    if (!str_PinCode.equalsIgnoreCase("")) {
                        str_PinCode = str_PinCode + ", ";
                    }
                    if (!str_City.equalsIgnoreCase("")) {
                        str_City = str_City + ", ";
                    }

                    str_ekyc_code_address = str_Address_title + str_Address1
                            + str_Address2 + str_Address3 + str_City + str_PinCode
                            + str_State;

                    //String strHTML = toOfflineeKYCAadharCardhtml();

                    /***** XML Parsing Start ******/
                    if (resXMLRes != null) {

                        str_doc = AGE_DOC;

                        File aadhaarAgeProofFileUpload = CreateAdhharPDF(str_pan_no, AGE_DOC);

                        if (aadhaarAgeProofFileUpload != null) {

                            createSoapRequestToUploadDoc(aadhaarAgeProofFileUpload);

                        } else {
                            mCommonMethods.showMessageDialog(mContext, "Error While Creating Aadhaar pdf !!");
                        }

                    } else {
                        Toast.makeText(Activity_POSP_RA_Authentication.this, "Invalid QR Code!", Toast.LENGTH_LONG).show();
                    }


                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } else if (requestCode == REQUEST_CODE_CAPTURE_DOCUMENT) {
                // start cropping activity for pre-acquired image saved on the device
                CropImage.activity(Uri.fromFile(mCapturedImgFile)).start(this);
            } else if (requestCode == DEVICE_CAPTURE_REQUEST_CODE) {

                try {
                    String pidDataXML = data.getStringExtra("PID_DATA");

                    if (pidDataXML == null) {
                        mCommonMethods.showMessageDialog(mContext, "Error:\n" + "Unable to capture fingerprint. pidData is Null");
                        return;
                    }

                    PidData pidData = (PidData) parseXML(PidData.class, pidDataXML);

                    if (pidData.getResp() == null) {
                        mCommonMethods.showMessageDialog(mContext, "Error:\n" + "Capture fingerprint failed. Resp is NULL");
                        return;
                    }

                    if (!pidData.getResp().getErrCode().equals("0")) {
                        mCommonMethods.showMessageDialog(mContext, "Error:\n" + "Capture fingerprint failed. Reason: "
                                + pidData.getResp().getErrInfo());
                        return;
                    }

                    byte[] data_pidDataXML = pidDataXML.getBytes(StandardCharsets.UTF_8);

                    pidDataXMLbase64 = Base64.encodeToString(data_pidDataXML,
                            Base64.DEFAULT);

                    //ll_header_part.setVisibility(View.GONE);
                    //ll_progress_layout.setVisibility(View.VISIBLE);

                    mAsyncFingerPrintRDServiceCaptureSuccess = new AsyncFingerPrintRDServiceCaptureSuccess();
                    mAsyncFingerPrintRDServiceCaptureSuccess.execute();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    mCommonMethods.showMessageDialog(mContext, "Error\n" + ex.getMessage());
                }
            } else if (requestCode == IRIS_CAPTURE_REQUEST_CODE) {

                try {
                    String pidDataXML = data.getStringExtra("PID_DATA");

                    if (pidDataXML == null) {

                        mCommonMethods.showMessageDialog(mContext, "Error:\n" + "Unable to capture IRIS. pidData is Null");
                        return;
                    }

                    PidData pidData = (PidData) parseXML(PidData.class, pidDataXML);

                    if (pidData.getResp() == null) {

                        mCommonMethods.showMessageDialog(mContext, "Error:\n" + "Capture IRIS failed. Resp is NULL");
                        return;
                    }

                    if (!pidData.getResp().getErrCode().equals("0")) {

                        mCommonMethods.showMessageDialog(mContext, "Error:\n" + "Capture IRIS failed. Reason: "
                                + pidData.getResp().getErrInfo());
                        return;
                    }

                    byte[] data_pidDataXML = pidDataXML.getBytes(StandardCharsets.UTF_8);

                    pidDataXMLbase64 = Base64.encodeToString(data_pidDataXML, Base64.DEFAULT);
                    //ll_header_part.setVisibility(View.GONE);
                    //ll_progress_layout.setVisibility(View.VISIBLE);

                    mAsyncCaptureRDIRISSuccess = new AsyncCaptureRDIRISSuccess();
                    mAsyncCaptureRDIRISSuccess.execute();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    mCommonMethods.showMessageDialog(mContext, "Error\n" + ex.getMessage());
                }
            }

        } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            Exception error = result.getError();
            mCommonMethods.showMessageDialog(mContext, error.getMessage());
        } else {
            try {
                if (requestCode == DEVICE_CAPTURE_REQUEST_CODE) {
                    String errorMessage = data.getStringExtra("ERROR_MESSAGE");
                    if (errorMessage.contains("Device not registered")) {

                        mCommonMethods.showToast(mContext, "wait Registeration Process will Start");

                        registeredIfRequired();

                        // showTokenDialog();
                        return;
                    } else if (errorMessage
                            .contains("device from google playstore")) {

                        showDialogGogoglePayStore("Error",
                                "Please install the Morpho RD service for your device from google play store");

                        // showTokenDialog();
                        return;
                    } else {
                        mCommonMethods.showMessageDialog(mContext, "Error:\n" + errorMessage);
                        return;
                    }
                } else if (requestCode == IRIS_CAPTURE_REQUEST_CODE) {
                    mCommonMethods.showMessageDialog(mContext, "Error:\n" + "RD Service Error. Code : "
                            + resultCode);
                    return;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                mCommonMethods.showMessageDialog(mContext, "Error\n" + ex.getMessage());
                return;
            }
        }
    }

    private void parse_show_ekyc_details(Context con, final String strData) {

        //parse ekyc responce

        if (strData != null) {

            try {
                final ECSKycResponse res = (ECSKycResponse) XMLUtilities
                        .parseXML(ECSKycResponse.class, strData);

                // ECSKycResponse res = (ECSKycResponse) data
                // .getSerializableExtra("KYC_XML");

                // UidData uidData;

                String str_ekyc_pic = res.getPoi().getPhoto() == null ? "" : res.getPoi().getPhoto();

                String str_Address_title = res.getPoa().getCo() == null ? ""
                        : res.getPoa().getCo();
                String str_aad_Address1 = (res.getPoa().getLoc() == null ? ""
                        : res.getPoa().getLoc() + " ")
                        + (res.getPoa().getHouse() == null ? "" : res
                        .getPoa().getHouse() + " ")
                        + (res.getPoa().getStreet() == null ? "" : res
                        .getPoa().getStreet() + " ")
                        + (res.getPoa().getLm() == null ? "" : res.getPoa()
                        .getLm());
                String str_aad_Address2 = (res.getPoa().getSubdist() == null ? ""
                        : res.getPoa().getSubdist() + " ")
                        + (res.getPoa().getPo() == null ? "" : res.getPoa()
                        .getPo() + " ");
                String str_aad_Address3 = (res.getPoa().getVtc() == null ? ""
                        : res.getPoa().getVtc());
                String str_PinCode = (res.getPoa().getPc() == null ? ""
                        : res.getPoa().getPc());
                String str_City = (res.getPoa().getDist() == null ? ""
                        : res.getPoa().getDist());
                String str_State = (res.getPoa().getState() == null ? ""
                        : res.getPoa().getState());
                String str_prop_aadhar_email_id = res.getPoi().getEmail() == null ? ""
                        : res.getPoi().getEmail();
                String str_prop_aadhar_mobile_no = res.getPoi().getPhone() == null ? ""
                        : res.getPoi().getPhone();
                String str_prop_aadhar_dob = res.getPoi().getDob() == null ? ""
                        : res.getPoi().getDob();
                String str_prop_aadhar_name = res.getPoi().getDob() == null ? ""
                        : res.getPoi().getName();
                String str_prop_aadhar_number = res.getPoi().getAadhaarNumber() == null ? ""
                        : res.getPoi().getAadhaarNumber();

                //ekyc 2.5 changes
                String str_uid_token = res.getPoi().getUidtoken() == null ? ""
                        : res.getPoi().getUidtoken();

                String str_prop_aadhar_gender = res.getPoi().getGender() == null ? ""
                        : res.getPoi().getGender();

                boolean isValidDate = mCommonMethods.isThisDateValid(str_prop_aadhar_dob);
                if (!isValidDate) {
                    str_prop_aadhar_dob = "01-01-1990";
                }
                //map_result.put("aadhar_dob", str_prop_aadhar_dob);
                String str_ekyc_address = str_Address_title + "\n"
                        + str_aad_Address1 + "\n" + str_aad_Address2
                        + str_aad_Address3 + "\n" + str_PinCode + "\n"
                        + str_City + "\n" + str_State;

                final Dialog dialog = new Dialog(con);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_ekyc_details);
                dialog.setCancelable(false);

                // Get screen width and height in pixels
                DisplayMetrics displayMetrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                // The absolute width of the available display size in pixels.
                int displayWidth = displayMetrics.widthPixels;
                // The absolute height of the available display size in pixels.
                //int displayHeight = displayMetrics.heightPixels;

                // Initialize a new window manager layout parameters
                WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();

                // Copy the alert dialog window attributes to new layout parameter instance
                layoutParams.copyFrom(dialog.getWindow().getAttributes());

                // Set alert dialog width equal to screen width 70%
                // Set alert dialog height equal to screen height 70%
                //int dialogWindowHeight = (int) (displayHeight * 0.7f);

                // Set the width and height for the layout parameters
                // This will bet the width and height of alert dialog
                layoutParams.width = (int) (displayWidth * 0.7f);
                //layoutParams.height = dialogWindowHeight;

                // Apply the newly created layout parameters to the alert dialog window
                dialog.getWindow().setAttributes(layoutParams);

                //image
                ImageView iv_ekyc_pic = dialog.findViewById(R.id.iv_ekyc_pic);
                if (str_ekyc_pic.equals("")) {
                    iv_ekyc_pic.setVisibility(View.GONE);
                } else {
                    iv_ekyc_pic.setVisibility(View.VISIBLE);
                    //decode base64 string to image
                    byte[] imageBytes = Base64.decode(str_ekyc_pic, Base64.DEFAULT);
                    Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                    iv_ekyc_pic.setImageBitmap(decodedImage);
                }

                //name
                LinearLayout ll_ekyc_name = dialog.findViewById(R.id.ll_ekyc_name);
                TextView txt_ekyc_name = dialog.findViewById(R.id.txt_ekyc_name);
                if (str_prop_aadhar_name.equals("")) {
                    ll_ekyc_name.setVisibility(View.GONE);
                } else {
                    ll_ekyc_name.setVisibility(View.VISIBLE);
                    txt_ekyc_name.setText(str_prop_aadhar_name);
                }

                //DOB
                LinearLayout ll_ekyc_dob = dialog.findViewById(R.id.ll_ekyc_dob);
                TextView txt_ekyc_dob = dialog.findViewById(R.id.txt_ekyc_dob);
                if (str_prop_aadhar_dob.equals("")) {
                    ll_ekyc_dob.setVisibility(View.GONE);
                } else {
                    ll_ekyc_dob.setVisibility(View.VISIBLE);
                    txt_ekyc_dob.setText(str_prop_aadhar_dob);
                }

                //Gender
                LinearLayout ll_ekyc_gender = dialog.findViewById(R.id.ll_ekyc_gender);
                TextView txt_ekyc_gender = dialog.findViewById(R.id.txt_ekyc_gender);
                if (str_prop_aadhar_gender.equals("")) {
                    ll_ekyc_gender.setVisibility(View.GONE);
                } else {
                    ll_ekyc_gender.setVisibility(View.VISIBLE);
                    txt_ekyc_gender.setText(str_prop_aadhar_gender);
                }

                //Mobile
                LinearLayout ll_ekyc_mobile = dialog.findViewById(R.id.ll_ekyc_mobile);
                TextView txt_ekyc_mobile = dialog.findViewById(R.id.txt_ekyc_mobile);
                if (str_prop_aadhar_mobile_no.equals("")) {
                    ll_ekyc_mobile.setVisibility(View.GONE);
                } else {
                    ll_ekyc_mobile.setVisibility(View.VISIBLE);
                    txt_ekyc_mobile.setText(str_prop_aadhar_mobile_no);
                }

                //email
                LinearLayout ll_ekyc_email = dialog.findViewById(R.id.ll_ekyc_email);
                TextView txt_ekyc_email = dialog.findViewById(R.id.txt_ekyc_email);
                if (str_prop_aadhar_email_id.equals("")) {
                    ll_ekyc_email.setVisibility(View.GONE);
                } else {
                    ll_ekyc_email.setVisibility(View.VISIBLE);
                    txt_ekyc_email.setText(str_prop_aadhar_email_id);
                }

                //Address
                LinearLayout ll_ekyc_address = dialog.findViewById(R.id.ll_ekyc_address);
                TextView txt_ekyc_address = dialog.findViewById(R.id.txt_ekyc_address);
                if (str_ekyc_address.equals("")) {
                    ll_ekyc_address.setVisibility(View.GONE);
                } else {
                    ll_ekyc_address.setVisibility(View.VISIBLE);
                    txt_ekyc_address.setText(str_ekyc_address);
                }

                Button btn_save_ekyc_details = dialog.findViewById(R.id.btn_save_ekyc_details);
                btn_save_ekyc_details.setText("NEXT");
                btn_save_ekyc_details.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {

                        //save data to local database
                        try {
                            /* KYC Encrypted response */
                            String str_encyptedRes = SimpleCrypto.encrypt("SBIL", strData);

                            //3. update data against global row id
                            cv.clear();
                            cv.put(db.POSP_RA_AADHAAR_NO, str_aashaar_no);
                            cv.put(db.POSP_RA_AADHAAR_DETAILS, str_encyptedRes);
                            cv.put(db.POSP_RA_UPDATED_BY, strCIFBDMUserId);

                            Calendar c = Calendar.getInstance();
                            //save date in long
                            cv.put(db.POSP_RA_UPDATED_DATE, new Date(c.getTimeInMillis()).getTime() + "");
                            cv.put(db.POSP_RA_IN_APP_STATUS, "1");
                            cv.put(db.POSP_RA_IN_APP_STATUS_REMARK, "Off-line EKyc Done");

                            int i = db.update_POSP_RA_details(cv, db.POSP_RA_ID + " =? ",
                                    new String[]{Activity_POSP_RA_Authentication.row_details + ""});

                            /*Intent mIntent = new Intent(Activity_AOB_Authentication.this, ActivityAOBBasicDetails.class);*/
                            Intent mIntent = new Intent(Activity_POSP_RA_Authentication.this, Activity_POSP_RA_PersonalInfo.class);
                            mIntent.putExtra("is_dashboard", false);
                            startActivity(mIntent);

                        } catch (Exception e) {
                            e.printStackTrace();
                            mCommonMethods.showToast(mContext, "KYC Details Saving Failed");
                        }

                        dialog.dismiss();
                    }
                });
                dialog.show();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void ekycShowDetails(Context context, String str_reslt, String str_ekyc_type) {

        if (str_reslt != null) {
            if (str_reslt.startsWith("<Error>")) {
                mCommonMethods.showMessageDialog(mContext, getErrorMessage(str_reslt));
            }

            try {

                if (str_reslt.contains("<ECSKycResponse") == true) {
                    final ECSKycResponse res = (ECSKycResponse) XMLUtilities.parseXML(ECSKycResponse.class, str_reslt);

                    if (res.getErr()) {
                        mCommonMethods.showMessageDialog(mContext, res.getErrCode() + "-" + res.getErrMsg());
                        return;
                    }

                    try {
                        StringBuilder str_kycResXML = new StringBuilder();
                        str_kycResXML.append(str_reslt);

                        str_kycResXML.append("<ConsentAccepted>"
                                + "Y"
                                + "</ConsentAccepted>");

                        str_kycResXML.append("<BIOMETRICMODE>" + str_ekyc_type + "</BIOMETRICMODE>");

                        //ll_progress_layout.setVisibility(View.GONE);
                        //ll_header_part.setVisibility(View.VISIBLE);

                        //show data on dialog
                        parse_show_ekyc_details(mContext, str_kycResXML.toString());

                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();

                        mCommonMethods.showMessageDialog(mContext, "Error in KYC Response!\n"
                                + e.getMessage());
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                mCommonMethods.showMessageDialog(mContext, "Error:\n" + ex.getMessage());
            }
        } else {
            mCommonMethods.showToast(mContext, "Error in KYC Response!");
        }
    }

    private String changeURL(String strXML) throws Exception {
        /* str_kycXML is generated request XML */
        //for producction
        return HttpConnector.getInstance().postData(strXML);
        /*//for stagging
        return HttpConnector.getInstance().eKYCWrapper(str_kycXML.toString());*/
    }

    public File CreateAdhharPDF(String strPAN, String strDoc) {

        File aadhaarQRFileName = null;

        try {
            Font headerBold = new Font(Font.FontFamily.TIMES_ROMAN, 11,
                    Font.BOLD, BaseColor.WHITE);
            Font small_bold = new Font(Font.FontFamily.TIMES_ROMAN, 8,
                    Font.BOLD);
            Font small_bold_for_name = new Font(Font.FontFamily.TIMES_ROMAN,
                    10, Font.BOLD);
            Font small_normal = new Font(Font.FontFamily.TIMES_ROMAN, 8,
                    Font.NORMAL);
            Font sub_headerBold = new Font(Font.FontFamily.TIMES_ROMAN, 7,
                    Font.BOLD);

            aadhaarQRFileName = mStorageUtils.createFileToAppSpecificDir(mContext, strPAN + "_" + strDoc + ".pdf");
            //File mypath = new File(folder, str_selected_urn_no + "_X1.pdf");

            Rectangle rect = new Rectangle(594f, 792f);

            Document document = new Document(rect, 50, 50, 50, 50);
            // Document document = new Document(PageSize.A4, 50, 50, 50, 50);
            PdfWriter pdf_writer = PdfWriter.getInstance(document, new FileOutputStream(aadhaarQRFileName.getPath()));

            document.open();
            // For SBI- Life Logo starts
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            Bitmap bitmap = BitmapFactory.decodeResource(getBaseContext()
                    .getResources(), R.drawable.sbi_life_logo);
            bitmap.compress(Bitmap.CompressFormat.PNG, 50, stream);
            Image img_sbi_logo = Image.getInstance(stream.toByteArray());
            img_sbi_logo.setAlignment(Image.LEFT);
            img_sbi_logo.getSpacingAfter();
            img_sbi_logo.scaleToFit(80, 50);

            Paragraph para_img_logo = new Paragraph("");
            para_img_logo.add(img_sbi_logo);

            Paragraph para_img_logo_after_space_1 = new Paragraph(" ");

            document.add(para_img_logo);
            // For SBI- Life Logo ends

            // To draw line after the sbi logo image
            document.add(new LineSeparator());
            document.add(para_img_logo_after_space_1);

            PdfPTable headertable = new PdfPTable(1);
            headertable.setWidthPercentage(100);
            headertable.setHorizontalAlignment(Element.ALIGN_CENTER);
            Paragraph para_address = new Paragraph("Aadhaar Offline KYC Details", small_bold_for_name);
            para_address.setAlignment(Element.ALIGN_CENTER);
            document.add(headertable);
            // document.add(new LineSeparator());
            document.add(para_img_logo_after_space_1);
            document.add(para_address);
            document.add(para_img_logo_after_space_1);

            if (str_QR_code_Photo != null && !str_QR_code_Photo.equals("")) {
                ByteArrayOutputStream stream_photo = new ByteArrayOutputStream();
                byte[] fbyt_photo = Base64.decode(
                        str_QR_code_Photo, 0);
                Bitmap bitmap_photo = BitmapFactory
                        .decodeByteArray(fbyt_photo, 0,
                                fbyt_photo.length);
                bitmap_photo.compress(Bitmap.CompressFormat.PNG, 100, stream_photo);
                Image img_photo = Image.getInstance(stream_photo.toByteArray());
                img_photo.setAlignment(Image.RIGHT);
                img_photo.getSpacingAfter();
                img_photo.scaleToFit(200, 100);
                Paragraph para_photo = new Paragraph("");
                para_photo.add(img_photo);
                document.add(para_photo);
            }
            PdfPTable table_Name = new PdfPTable(2);
            table_Name.setWidthPercentage(100);

            PdfPCell Name_cell_1 = new PdfPCell(new Paragraph("Name", small_normal));
            PdfPCell Name_cell_2 = new PdfPCell(new Paragraph(str_ekyc_code_Name, small_bold));
            Name_cell_1.setPadding(5);
            Name_cell_2.setPadding(5);
            Name_cell_2.setHorizontalAlignment(Element.ALIGN_CENTER);
            table_Name.addCell(Name_cell_1);
            table_Name.addCell(Name_cell_2);
            document.add(table_Name);

            PdfPTable table_Gender = new PdfPTable(2);
            table_Gender.setWidthPercentage(100);

            PdfPCell Gender_cell_1 = new PdfPCell(new Paragraph("Gender", small_normal));
            PdfPCell Gender_cell_2 = new PdfPCell(new Paragraph(str_ekyc_code_Gender, small_bold));
            Gender_cell_1.setPadding(5);
            Gender_cell_2.setPadding(5);
            Gender_cell_2.setHorizontalAlignment(Element.ALIGN_CENTER);
            table_Gender.addCell(Gender_cell_1);
            table_Gender.addCell(Gender_cell_2);
            document.add(table_Gender);

            PdfPTable table_DOB = new PdfPTable(2);
            table_DOB.setWidthPercentage(100);

            PdfPCell DOB_cell_1 = new PdfPCell(new Paragraph("DOB/YOB", small_normal));
            PdfPCell DOB_cell_2 = new PdfPCell(new Paragraph(str_ekyc_code_DOB, small_bold));
            DOB_cell_1.setPadding(5);
            DOB_cell_2.setPadding(5);
            DOB_cell_2.setHorizontalAlignment(Element.ALIGN_CENTER);
            table_DOB.addCell(DOB_cell_1);
            table_DOB.addCell(DOB_cell_2);
            document.add(table_DOB);

            PdfPTable table_Address = new PdfPTable(2);
            table_Address.setWidthPercentage(100);

            PdfPCell Address_cell_1 = new PdfPCell(new Paragraph("Address", small_normal));
            PdfPCell Address_cell_2 = new PdfPCell(new Paragraph(str_ekyc_code_address, small_bold));
            Address_cell_1.setPadding(5);
            Address_cell_2.setPadding(5);
            Address_cell_2.setHorizontalAlignment(Element.ALIGN_CENTER);
            table_Address.addCell(Address_cell_1);
            table_Address.addCell(Address_cell_2);
            document.add(table_Address);

            document.close();

            return aadhaarQRFileName;

        } catch (Exception e) {
            Log.i("Error", e.toString() + "Error in creating pdf");
            return null;
        }
    }

    private void createSoapRequestToUploadDoc(final File mFiles) {

        Single.fromCallable(() -> mCommonMethods.read(mFiles))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<byte[]>() {
                    @Override
                    public void accept(@NonNull byte[] result) throws Exception {
                        if (result != null) {
                            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME_UPLOAD_ALL_AOB_DOC);

                            request.addProperty("f", result);

                            /*if (str_doc.equals(PAN_DOC)) {*/
                            request.addProperty("fileName", mFiles.getName());
                            //}

                            request.addProperty("PAN", str_pan_no);
                            //request.addProperty("Type", mCommonMethods.str_posp_ra_customer_type);

                            mCommonMethods.appendSecurityParams(mContext, request, strCIFBDMEmailId, strCIFBDMMObileNo);

                            mAsyncUploadFileCommon = new AsyncUploadFile_Common(mContext, Activity_POSP_RA_Authentication.this,
                                    request, METHOD_NAME_UPLOAD_ALL_AOB_DOC);
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
    public void onCKYCProcessComppletion(int processCount, boolean isProcessComplete, String Result) {
        if (isProcessComplete) {
            if (processCount == InterfaceCKYCProcessCompletion.CKYC_DOWNLOAD_DETAILS_PROCESS) {
                try {
                    Single.fromCallable(() -> mAsyncGetcKYCSearchDetail.GetCKYCDetails())
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Consumer<Boolean>() {
                                @Override
                                public void accept(@androidx.annotation.NonNull Boolean aBoolean) throws Exception {
                                    if (aBoolean) {
                                        //show deatils in dialog with Yes and No
                                        final Dialog dialog = new Dialog(mContext);
                                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                        dialog.setContentView(R.layout.dailog_ckyc_result);
                                        dialog.setCancelable(false);
                                        TextView tv_dialog_msg = dialog.findViewById(R.id.tv_dialog_msg);
                                        tv_dialog_msg.setText(Html.fromHtml(mAsyncGetcKYCSearchDetail.createFormatedCKYCDetails()));
                                        tv_dialog_msg.setMovementMethod(new ScrollingMovementMethod());
                                        Button bt_dialog_ok = dialog.findViewById(R.id.bt_dialog_ok);
                                        bt_dialog_ok.setOnClickListener(new View.OnClickListener() {
                                            public void onClick(View v) {

                                                dialog.dismiss();
                                                mAsyncGetcKYCSearchDetail.setStr_CKYC_increment(1);
                                                mAsyncGetcKYCSearchDetail.UploadCKYCDocuments();
                                            }
                                        });
                                        Button bt_dialog_reject = dialog.findViewById(R.id.bt_dialog_reject);
                                        bt_dialog_reject.setOnClickListener(new View.OnClickListener() {
                                            public void onClick(View v) {
                                                dialog.dismiss();

                                                //for offline eKYC
                                                ll_aob_aadhaar_details.setVisibility(View.VISIBLE);
                                            }
                                        });
                                        dialog.show();
                                    } else {
                                        mCommonMethods.showMessageDialog(mContext, "Error Parsing CKYC Deatils");
                                    }
                                }
                            }, throwable -> {
                                mCommonMethods.showMessageDialog(mContext, "Error Parsing CKYC Deatils");
                            });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            if (processCount == InterfaceCKYCProcessCompletion.CKYC_SEARCH_PROCESS) {
                mCommonMethods.showToast(mContext, "CKYC Search Details Error : " + Result);
            } else if (processCount == InterfaceCKYCProcessCompletion.CKYC_DOWNLOAD_DETAILS_PROCESS) {
                mCommonMethods.showToast(mContext, "CKYC Download Details Error : " + Result);
            } else if (processCount == InterfaceCKYCProcessCompletion.CKYC_UPLOAD_CKYC_DOC_PROCESS) {
                mCommonMethods.showToast(mContext, "CKYC Upload Details Error : " + Result);
            }

            ll_aob_aadhaar_details.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onUploadComplete(Boolean result) {

        if (result) {

            if (str_doc.equals(METHOD_NAME_CHECK_PAN_EXISTS)) {
                mCommonMethods.showMessageDialog(mContext, "PAN no. - " + str_pan_no + " already exists..");
            } else if (str_doc.equals(PAN_DOC)) {

                imgbtn_aob_auth_pan_camera.setEnabled(false);
                imgbtn_aob_auth_pan_browse.setEnabled(false);
                imgbtn_aob_auth_pan_upload.setEnabled(false);
                imgbtn_aob_auth_pan_upload.setImageDrawable(getResources().getDrawable(R.drawable.checkedupload));

                is_pan_file_uploaded = true;
                mPanFile = null;

                /*ll_aob_auth_aadhhar.setVisibility(View.VISIBLE);*/

                //str_aashaar_no = edt_aob_auth_aadhaar.getText().toString().replaceAll("\\s+", "").trim();
                //save data to local database
                try {

                    //3. update data against global row id
                    cv.clear();
                    cv.put(db.POSP_RA_AADHAAR_NO, "");
                    cv.put(db.POSP_RA_AADHAAR_DETAILS, "");
                    cv.put(db.POSP_RA_UPDATED_BY, strCIFBDMUserId);

                    Calendar c = Calendar.getInstance();
                    //save date in long
                    cv.put(db.POSP_RA_UPDATED_DATE, new Date(c.getTimeInMillis()).getTime() + "");
                    cv.put(db.POSP_RA_IN_APP_STATUS, "1");
                    cv.put(db.POSP_RA_IN_APP_STATUS_REMARK, "Pan Card Uploaded");

                    int i = db.update_POSP_RA_details(cv, db.POSP_RA_ID + " =? ",
                            new String[]{Activity_POSP_RA_Authentication.row_details + ""});

                    /*Intent mIntent = new Intent(Activity_AOB_Authentication.this, ActivityAOBBasicDetails.class);*/

                            /*Intent mIntent = new Intent(Activity_AOB_Authentication.this, Activity_POSP_RA_PersonalInfo.class);
                            mIntent.putExtra("is_dashboard", false);
                            startActivity(mIntent);*/

                    //CKYC

                    mAsyncGetcKYCSearchDetail = new AsyncGetcKYCSearchDetail(mContext, str_pan_no, str_aob_dob,
                            mCommonMethods.str_posp_ra_customer_type, this);
                    mAsyncGetcKYCSearchDetail.execute();

                } catch (Exception e) {

                    ll_aob_aadhaar_details.setVisibility(View.GONE);

                    e.printStackTrace();
                    mCommonMethods.showToast(mContext, "KYC Details Saving Failed");
                }

                Toast.makeText(mContext, "Document Upload Successfully...",
                        Toast.LENGTH_SHORT).show();
            } else if (str_doc.equals(AGE_DOC)) {

                str_doc = COMM_ADDRESS_DOC;

                //update status of Age doc
                cv.clear();

                String str_doc_upload = "<doc_upload_age_proof_type>OFF-LINE EKYC</doc_upload_age_proof_type>" +
                        "<doc_upload_age_proof_is_upload>true</doc_upload_age_proof_is_upload>";

                cv.put(db.POSP_RA_DOCUMENTS_UPLOAD, str_doc_upload);
                cv.put(db.POSP_RA_UPDATED_BY, strCIFBDMUserId);

                Calendar c = Calendar.getInstance();
                //save date in long
                cv.put(db.POSP_RA_UPDATED_DATE, new Date(c.getTimeInMillis()).getTime() + "");
                cv.put(db.POSP_RA_IN_APP_STATUS, "1");
                cv.put(db.POSP_RA_IN_APP_STATUS_REMARK, "Off-line EKyc Done");

                int i = db.update_POSP_RA_details(cv, db.POSP_RA_ID + " =? ",
                        new String[]{Activity_POSP_RA_Authentication.row_details + ""});

                File aadhaarCommAddressFileUpload = CreateAdhharPDF(str_pan_no, COMM_ADDRESS_DOC);

                if (aadhaarCommAddressFileUpload != null) {

                    createSoapRequestToUploadDoc(aadhaarCommAddressFileUpload);

                } else {
                    mCommonMethods.showMessageDialog(mContext, "Error While Creating Aadhaar pdf !!");
                }

            } else if (str_doc.equals(COMM_ADDRESS_DOC)) {

                //update status of communication address

                String str_doc_upload = "<doc_upload_age_proof_type>OFF-LINE EKYC</doc_upload_age_proof_type>"
                        + "<doc_upload_age_proof_is_upload>true</doc_upload_age_proof_is_upload>"
                        + "<doc_upload_comm_address_type>OFF-LINE EKYC</doc_upload_comm_address_type>"
                        + "<doc_upload_comm_address_is_upload>true</doc_upload_comm_address_is_upload>";

                cv.clear();
                cv.put(db.POSP_RA_DOCUMENTS_UPLOAD, str_doc_upload);
                cv.put(db.POSP_RA_UPDATED_BY, strCIFBDMUserId);

                Calendar c = Calendar.getInstance();
                //save date in long
                cv.put(db.POSP_RA_UPDATED_DATE, new Date(c.getTimeInMillis()).getTime() + "");
                cv.put(db.POSP_RA_IN_APP_STATUS, "1");
                cv.put(db.POSP_RA_IN_APP_STATUS_REMARK, "Off-line EKyc Done");

                int i = db.update_POSP_RA_details(cv, db.POSP_RA_ID + " =? ",
                        new String[]{Activity_POSP_RA_Authentication.row_details + ""});

                Intent mIntent = new Intent(Activity_POSP_RA_Authentication.this, Activity_POSP_RA_PersonalInfo.class);
                mIntent.putExtra("is_dashboard", false);
                startActivity(mIntent);
            }

        } else {

            if (str_doc.equals(METHOD_NAME_CHECK_PAN_EXISTS)) {
                str_doc = PAN_DOC;

                Random r = new Random(System.currentTimeMillis());

                //call PAN service to get Name of PAN card holder
                String strPANInput = "<PANDETAILS>"
                        + "<MODULENAME>" + "CMS" + "</MODULENAME>"
                        + "<USERID>" + "14830" + "</USERID>"//hard coded user ID
                        + "<PANINPUT>" + "<PANNO>" + str_pan_no + "</PANNO>"
                        + "<LASTNAME>" + "" + "</LASTNAME>"
                        + "<FIRSTNAME>" + "ABC" + "</FIRSTNAME>"
                        + "<MIDDLENAME>" + "" + "</MIDDLENAME>"
                        + "<TRANSACTIONID>" + "CMS_" + r.nextInt(999999) + "</TRANSACTIONID>"
                        + "<PURPOSE>POSP-RA</PURPOSE>"
                        + "</PANINPUT>"
                        + "</PANDETAILS>";

                mAsyncCheckPAN = new AsyncCheckPAN();
                mAsyncCheckPAN.execute(strPANInput);

                //for testing
                    /*if (mPanFile != null) {

                        createSoapRequestToUploadDoc(mPanFile);

                    } else {
                        mCommonMethods.showMessageDialog(mContext, "Error While creating PAN card doc!!");
                    }*/
            } else {
                Toast.makeText(mContext, "PLease try agian later..", Toast.LENGTH_SHORT).show();

                is_pan_file_uploaded = false;
                /*ll_aob_auth_aadhhar.setVisibility(View.GONE);*/
            }
        }

    }

    public class AsyncFingerPrintRDServiceCaptureSuccess extends AsyncTask<String, Void, String> {

        private volatile boolean running = true;

        @Override
        protected void onPreExecute() {
            mProgressDialog = new ProgressDialog(Activity_POSP_RA_Authentication.this, ProgressDialog.THEME_HOLO_LIGHT);
            mProgressDialog.setMessage(Html.fromHtml("<font color='#00a1e3'><b>Loading Please wait...<b></font>"));
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            if (mCommonMethods.isNetworkConnected(mContext)) {

                try {
                    /*final TelephonyManager tm = (TelephonyManager) getBaseContext()
                            .getSystemService(Context.TELEPHONY_SERVICE);

                    String str_IMEI_No = "" + tm.getDeviceId();*/

                    ECSRDKYCRequest request = new ECSRDKYCRequest();
                    request.setAadhaarNumber(str_aashaar_no);
                    request.setPidData(pidDataXMLbase64);
                    request.setTid("registered");
                    //ekyc 2.5
                    /*request.setUdc(str_IMEI_No);*/
                    request.setUsesBio(true);
                    request.setUsesBt("FMR");
                    request.setUsesOtp(false);
                    request.setLr("true");
                    request.setPfr("true");

                    String dataAsXml = XMLUtilities.getXML(request);
                    String str_kycXML = dataAsXml +
                            "<AppName>" + "Smart Advisor"
                            + "</AppName>" +
                            "<ReqID>" + str_aashaar_no + "-" + date1.getTime() + "</ReqID>" +
                            "<KYCType>" + "FMR"
                            + "</KYCType>";

                    return changeURL(str_kycXML);

                } catch (final Exception e) {
                    System.out.println("Inside Exception");
                    e.printStackTrace();
                    running = false;
                    return "Error sending Kyc request!\n"
                            + e.getMessage();
                }

            } else {
                running = false;
                return mCommonMethods.NO_INTERNET_MESSAGE;
            }
        }

        @Override
        protected void onPostExecute(String str_reslt) {
            if (mProgressDialog.isShowing())
                mProgressDialog.dismiss();

            //save string str_reslt to database agaist aadahr card no
            if (running) {

                ekycShowDetails(mContext, str_reslt, "FMR");

            } else {
                mCommonMethods.showMessageDialog(mContext, str_reslt);
            }
        }
    }

    public class AsyncCaptureRDIRISSuccess extends AsyncTask<String, Void, String> {

        private volatile boolean running = true;

        @Override
        protected void onPreExecute() {
            mProgressDialog = new ProgressDialog(Activity_POSP_RA_Authentication.this, ProgressDialog.THEME_HOLO_LIGHT);
            mProgressDialog.setMessage(Html.fromHtml("<font color='#00a1e3'><b>Loading Please wait...<b></font>"));
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            if (mCommonMethods.isNetworkConnected(mContext)) {
                try {
                    String androidId = Settings.Secure.getString(
                            getContentResolver(),
                            Settings.Secure.ANDROID_ID);

                    /*final TelephonyManager tm = (TelephonyManager) getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);

                    String str_IMEI_No = "" + tm.getDeviceId();*/
                    ECSRDKYCRequest request = new ECSRDKYCRequest();
                    request.setAadhaarNumber(str_aashaar_no);
                    request.setPidData(pidDataXMLbase64);
                    request.setTid("registered");
                    //ekyc 2.5
                    /*request.setUdc(str_IMEI_No);*/
                    request.setUsesBio(true);
                    request.setUsesBt("IIR");
                    request.setUsesOtp(false);
                    request.setLr("true");
                    request.setPfr("true");

                    String dataAsXml = XMLUtilities.getXML(request);
                    String str_kycXML = dataAsXml +
                            "<AppName>" + "Smart Advisor"
                            + "</AppName>" +
                            "<ReqID>" + str_aashaar_no + "-" + date1.getTime() + "</ReqID>" +
                            "<KYCType>" + "IIR"
                            + "</KYCType>";

                    return changeURL(str_kycXML);

                } catch (final Exception e) {
                    System.out.println("Inside Exception");
                    e.printStackTrace();
                    running = false;
                    return "Error sending Kyc request!\n" + e.getMessage();
                }
            } else
                return mCommonMethods.NO_INTERNET_MESSAGE;
        }

        @Override
        protected void onPostExecute(String str_reslt) {
            if (mProgressDialog != null)
                mProgressDialog.dismiss();

            //save String str_reslt against aadhaar card no
            if (running) {

                ekycShowDetails(mContext, str_reslt, "IIR");

            } else {
                mCommonMethods.showMessageDialog(mContext, str_reslt);
            }

        }
    }

    public class AsyncGetGeneratedOTP extends AsyncTask<String, Void, String> {

        private volatile boolean running = true;

        @Override
        protected void onPreExecute() {
            mProgressDialog = new ProgressDialog(Activity_POSP_RA_Authentication.this, ProgressDialog.THEME_HOLO_LIGHT);
            mProgressDialog.setMessage(Html.fromHtml("<font color='#00a1e3'><b>Loading Please wait...<b></font>"));
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            if (mCommonMethods.isNetworkConnected(mContext)) {
                try {
                    /*final TelephonyManager tm = (TelephonyManager) getBaseContext()
                            .getSystemService(Context.TELEPHONY_SERVICE);

                    String str_IMEI_No = "" + tm.getDeviceId();*/

                    ECSOtpKycRequest request = new ECSOtpKycRequest();
                    request.setAadhaarNumber(str_aashaar_no);

                    request.setOtp(str_generated_otp);
                    request.setOtpReqTxnId(str_otp_txn);
                    /*request.setUdc(str_IMEI_No);*/
                    request.setLr(true);
                    request.setPfr(true);

                    String dataAsXml = XMLUtilities.getXML(request);
                    String str_kycXML = dataAsXml +
                            "<AppName>" + "Smart Advisor"
                            + "</AppName>" +
                            "<ReqID>" + str_aashaar_no + "-" + date1.getTime() + "</ReqID>" +
                            "<KYCType>" + "OTP"
                            + "</KYCType>";

                    return changeURL(str_kycXML);

                } catch (final Exception e) {
                    System.out.println("Inside Exception");
                    e.printStackTrace();
                    running = false;
                    return "Error sending Kyc request!\n" + e.getMessage();
                }
            } else {
                return mCommonMethods.NO_INTERNET_MESSAGE;
            }
        }

        @Override
        protected void onPostExecute(String str_reslt) {
            if (mProgressDialog.isShowing())
                mProgressDialog.dismiss();

            //save String str_reslt against aadhaar card no
            if (running) {

                ekycShowDetails(mContext, str_reslt, "OTP");

            } else {
                mCommonMethods.showMessageDialog(mContext, str_reslt);
            }

        }
    }

    public class AsyncGenerateOTP extends AsyncTask<String, Void, String> {

        private volatile boolean running = true;

        @Override
        protected void onPreExecute() {
            mProgressDialog = new ProgressDialog(Activity_POSP_RA_Authentication.this, ProgressDialog.THEME_HOLO_LIGHT);
            mProgressDialog.setMessage(Html.fromHtml("<font color='#00a1e3'><b>Loading Please wait...<b></font>"));
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            if (mCommonMethods.isNetworkConnected(mContext)) {
                try {
                    running = true;
                    /*final TelephonyManager tm = (TelephonyManager) getBaseContext()
                            .getSystemService(Context.TELEPHONY_SERVICE);

                    String str_IMEI_No = "" + tm.getDeviceId();*/
                    ECSOtpRequest request = new ECSOtpRequest();
                    request.setAadhaarNumber(str_aashaar_no);
                    request.setSms(true);
                    request.setEmail(true);
                    String dataAsXml = XMLUtilities.getXML(request);
                    String str_kycXML = dataAsXml +
                            "<AppName>" + "Smart Advisor"
                            + "</AppName>" +
                            "<ReqID>" + str_aashaar_no + "-" + date1.getTime() + "</ReqID>" +
                            "<KYCType>" + "OTP"
                            + "</KYCType>";

                    resGenerateOTPString = changeURL(str_kycXML);

                    return resGenerateOTPString;

                } catch (Exception e) {
                    e.printStackTrace();
                    running = false;
                    return mCommonMethods.WEEK_INTERNET_MESSAGE;
                }
            } else {
                running = false;
                return mCommonMethods.NO_INTERNET_MESSAGE;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            if (mProgressDialog.isShowing())
                mProgressDialog.dismiss();

            if (running) {
                ECSOtpResponse ores = null;
                if (resGenerateOTPString.startsWith("<Error>")) {

                    String errorMessage = getErrorMessage(resGenerateOTPString);
                    mCommonMethods.showMessageDialog(mContext, errorMessage);

                } else if (resGenerateOTPString.contains("<ECSOtpResponse")) {

                    try {

                        // ores = (ECSOtpResponse) XMLUtilities.deserialize(
                        // ECSOtpResponse.class, resGenerateOTPString);
                        ores = (ECSOtpResponse) XMLUtilities.parseXML(ECSOtpResponse.class, resGenerateOTPString);
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    if (ores.getErr()) {
                        String errorMessage = ores.getErrCode() + "-"
                                + ores.getErrMsg();
                        //showErrorActivity(errorMessage);
                        mCommonMethods.showMessageDialog(mContext, errorMessage);
                    } else {
                        str_otp_txn = ores.getTxn();
                        if (ores.getMaskedEmail() == null
                                && ores.getMaskedMobile() == null)
                            mCommonMethods.showToast(mContext, "OTP Generated successfully");
                        else if (ores.getMaskedEmail() == null
                                && ores.getMaskedMobile() != null)

                            mCommonMethods.showToast(mContext, String.format(
                                    "OTP sent to Mobile Number %s",
                                    ores.getMaskedMobile()));

                        else if (ores.getMaskedEmail() != null
                                && ores.getMaskedMobile() == null)

                            mCommonMethods.showToast(mContext, String.format("OTP sent to Email Id %s", ores.getMaskedEmail()));

                        else
                            mCommonMethods.showToast(mContext, String.format(
                                    "OTP sent to Mobile Number %s Email Id %s", ores.getMaskedMobile(),
                                    ores.getMaskedEmail()));
                    }

                }

            } else {
                mCommonMethods.showToast(mContext, "Server not responding,Please try again");
            }

        }
    }

    public class AsyncCheckPAN extends AsyncTask<String, Void, String> {
        private volatile boolean running = true;
        private String str_output_result = "";

        @Override
        protected void onPreExecute() {
            mProgressDialog = new ProgressDialog(mContext,
                    ProgressDialog.THEME_HOLO_LIGHT);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog
                    .setTitle(Html
                            .fromHtml("<font color='#00a1e3'><b>Please Wait...<b></font>"));
            mProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {

            if (mCommonMethods.isNetworkConnected(mContext)) {
                try {
                    running = true;

                    SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME_CHECK_PAN_NUMBER);

                    request.addProperty("strInput", strings[0]);

                    mCommonMethods.TLSv12Enable();

                    SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                            SoapEnvelope.VER11);
                    envelope.dotNet = true;

                    new MarshalBase64().register(envelope); // serialization

                    envelope.setOutputSoapObject(request);

                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                            .permitAll().build();
                    StrictMode.setThreadPolicy(policy);

                    HttpTransportSE androidHttpTranport = new HttpTransportSE(ServiceURL.SERVICE_URL, 50000);
                    try {
                        androidHttpTranport.call(NAMESPACE + METHOD_NAME_CHECK_PAN_NUMBER, envelope);
                        Object response = envelope.getResponse();
                        str_output_result = response.toString();

                        // SoapPrimitive sa = (SoapPrimitive)
                        // envelope.getResponse();
                    } catch (Exception e) {
                        e.printStackTrace();
                        running = false;
                        return mCommonMethods.WEEK_INTERNET_MESSAGE;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    running = false;
                    return mCommonMethods.WEEK_INTERNET_MESSAGE;
                }
            } else {
                return mCommonMethods.NO_INTERNET_MESSAGE;
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            if (mProgressDialog.isShowing())
                mProgressDialog.dismiss();

            if (running) {

                ParseXML prsObj = new ParseXML();

                if (str_output_result == null
                        || str_output_result.equalsIgnoreCase("")) {

                    db.delete_POSP_RA_row(db.POSP_RA_ID + " = " + row_details);
                    row_details = 0;

                    validate_pan_card = false;

                    imgbtn_aob_auth_pan_camera.setEnabled(false);

                    imgbtn_aob_auth_pan_browse.setEnabled(false);

                    imgbtn_aob_auth_pan_upload.setEnabled(false);

                    edt_aob_auth_pan.setText("");
                    str_pan_no = "";
                    mPanFile = null;

                    mCommonMethods.showMessageDialog(mContext, "E-PAN DOES NOT EXSIST and INVALID");

                } else if (str_output_result.equalsIgnoreCase("Service Not Availiable Please try after some time")
                        || str_output_result.equalsIgnoreCase("anyType{}")) {

                    db.delete_POSP_RA_row(db.POSP_RA_ID + " = " + row_details);
                    row_details = 0;

                    validate_pan_card = false;

                    imgbtn_aob_auth_pan_camera.setEnabled(false);

                    imgbtn_aob_auth_pan_browse.setEnabled(false);

                    imgbtn_aob_auth_pan_upload.setEnabled(false);

                    edt_aob_auth_pan.setText("");
                    str_pan_no = "";
                    mPanFile = null;
                    mCommonMethods.showMessageDialog(mContext, "Service Not Availiable Please try after some time");
                } else {
                    String strOut = prsObj.parseXmlTag(str_output_result, "PANDETAILS");
                    strOut = strOut == null ? "" : strOut;

                    if (strOut.equals("")) {

                        db.delete_POSP_RA_row(db.POSP_RA_ID + " = " + row_details);
                        row_details = 0;

                        validate_pan_card = false;

                        imgbtn_aob_auth_pan_camera.setEnabled(false);
                        imgbtn_aob_auth_pan_camera.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_camera));

                        imgbtn_aob_auth_pan_browse.setEnabled(false);
                        imgbtn_aob_auth_pan_browse.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_browsedoc));

                        imgbtn_aob_auth_pan_upload.setEnabled(false);

                        edt_aob_auth_pan.setText("");
                        str_pan_no = "";
                        mPanFile = null;
                        mCommonMethods.showMessageDialog(mContext, "E-PAN DOES NOT EXSIST and INVALID");

                    } else {

                        String str_ReturnCode = prsObj.parseXmlTag(strOut, "RETURNCODE");
                        str_ReturnCode = str_ReturnCode == null ? "" : str_ReturnCode;

                        String str_pan_status = prsObj.parseXmlTag(strOut, "PANSTATUS");
                        str_pan_status = str_pan_status == null ? "" : str_pan_status;

                        String str_PANNO = prsObj.parseXmlTag(strOut, "PANNO");
                        str_PANNO = str_PANNO == null ? "" : str_PANNO;

                        if (str_ReturnCode.equals("1") && str_pan_status.equals("E")) {

                            if (str_pan_no.equals(str_PANNO)) {

                                running = false;
                                //3. update data against global row id
                                cv.clear();
                                cv.put(db.POSP_RA_PAN_NO, str_pan_no);
                                cv.put(db.POSP_RA_PAN_DETAILS, str_output_result);
                                cv.put(db.POSP_RA_UPDATED_BY, strCIFBDMUserId);

                                Calendar c = Calendar.getInstance();
                                //save date in long
                                cv.put(db.POSP_RA_UPDATED_DATE, new Date(c.getTimeInMillis()).getTime() + "");
                                cv.put(db.POSP_RA_IN_APP_STATUS, "0");
                                cv.put(db.POSP_RA_IN_APP_STATUS_REMARK, "PAN details updated");

                                int i = db.update_POSP_RA_details(cv, db.POSP_RA_ID + " =? ",
                                        new String[]{Activity_POSP_RA_Authentication.row_details + ""});


                                if (mPanFile != null) {
                                    str_doc = PAN_DOC;

                                    createSoapRequestToUploadDoc(mPanFile);

                                } else {
                                    mCommonMethods.showMessageDialog(mContext, "Error While creating PAN card doc!!");
                                }

                            } else {
                                mCommonMethods.showMessageDialog(mContext, "Uploaded PAN And Entered PAN Mismatch!");
                            }

                        } else {

                            db.delete_POSP_RA_row(db.POSP_RA_ID + " = " + row_details);
                            row_details = 0;

                            validate_pan_card = false;

                            imgbtn_aob_auth_pan_camera.setEnabled(false);
                            imgbtn_aob_auth_pan_camera.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_camera));

                            imgbtn_aob_auth_pan_browse.setEnabled(false);
                            imgbtn_aob_auth_pan_browse.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_browsedoc));

                            imgbtn_aob_auth_pan_upload.setEnabled(false);

                            edt_aob_auth_pan.setText("");
                            str_pan_no = "";
                            mPanFile = null;
                            mCommonMethods.showMessageDialog(mContext, "E-PAN DOES NOT EXSIST and INVALID");
                        }
                    }
                }

            } else {

                db.delete_POSP_RA_row(db.POSP_RA_ID + " = " + row_details);
                row_details = 0;

                validate_pan_card = false;

                imgbtn_aob_auth_pan_camera.setEnabled(false);

                imgbtn_aob_auth_pan_browse.setEnabled(false);

                imgbtn_aob_auth_pan_upload.setEnabled(false);

                edt_aob_auth_pan.setText("");
                str_pan_no = "";

                mPanFile = null;

                Toast.makeText(mContext, s.toString(),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }
}
