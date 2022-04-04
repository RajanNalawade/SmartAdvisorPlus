package sbilife.com.pointofsale_bancaagency.reports.policyservicing.servicingreqmodule;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.model.LatLng;
import com.xbizventures.ocrlib.OcrActivity;

import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.MarshalBase64;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import sbilife.com.pointofsale_bancaagency.ParseXML;
import sbilife.com.pointofsale_bancaagency.R;
import sbilife.com.pointofsale_bancaagency.ServiceURL;
import sbilife.com.pointofsale_bancaagency.branchlocator.GPSTracker;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;
import sbilife.com.pointofsale_bancaagency.common.CompressImage;
import sbilife.com.pointofsale_bancaagency.common.GenerateOTPGeneralAsyncTask;
import sbilife.com.pointofsale_bancaagency.common.StorageUtils;
import sbilife.com.pointofsale_bancaagency.common.ValidateOTPGeneralAsyncTask;
import sbilife.com.pointofsale_bancaagency.ekyc.utilites.HttpConnector;
import sbilife.com.pointofsale_bancaagency.utility.AlertDialogMessage;
import sbilife.com.pointofsale_bancaagency.utility.SelfAttestedDocumentActivity;

public class ServicingRequestModuleActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener,
        View.OnClickListener, GenerateOTPGeneralAsyncTask.GenerateOTPAsyncResultInterface,
        ValidateOTPGeneralAsyncTask.ValidateOTPAsyncResultInterface {
    private final String NAMESPACE = "http://tempuri.org/";
    private final String URl = ServiceURL.SERVICE_URL;
    private final int REQUEST_CODE_PICK_FILE = 2;

    private Context context;
    private CommonMethods commonMethods;
    private StorageUtils mStorageUtils;
    private Spinner spinnerTransNatureAllowed;
    private Spinner spinnerNonPayoutRequest, spinnerPayoutRequest, spinnerDocumentsStatements;
    private LinearLayout llNonPayoutRequest, llPayoutRequest, llDocumentsStatements, llDateOfBirth, llOTP,
            llPolicyCustomerDetails, llCoeDetails, llKYCUploadDoc, llButtonOK, llAnnuityNumberCoe;
    private String transactionNature, nonPayoutRequest, payoutRequest, documentsStatements, policyNumber, dob,
            newEmaild, confirmEmailId, newMobileNo, confirmMobileNo, newPANCard, annuityNo;
    private ImageView ivCustomerPhotoGraphGPSCord, ibAgentTypeUploadPhoto;
    private String userType, userName, strCIFBDMUserId, strCIFBDMEmailId, strCIFBDMMObileNo, Check, OTP,
            strPrType = "";
    private File customerPhotoFileName, agentPhotoFileName;
    private GPSTracker gpsTracker;
    private LatLng mLatLng;
    private Bitmap userPhotoBitmap, agentPhotoBitmap;
    private Button buttonValidateOTP, buttonGenerateOTP, buttonGenerateEmailOTP, buttonGenerateMobileOTP, buttonValidateMobileOTP,
            buttonValidateEmailOTP, buttonValidateCOEOTP, buttonGeneratePANOTP, buttonValidatePANOTP;
    private GenerateOTPGeneralAsyncTask generateOTPGeneralAsyncTask;
    private ValidateOTPGeneralAsyncTask validateOTPGeneralAsyncTask;
    private EditText edt_upload_all_docs, etOTP, etEmailId, etConfirmEmailId, etMobileNo,
            etConfirmMobileNo, etNewPANCard, etEmailOTP, etMobileOTP, etCOEOTP, etPANOTP, etAnnuityNumber;

    private boolean val_proposer_no = false;
    private ProgressDialog mProgressDialog;
    private int mYear, mMonth, mDay, datecheck = 0;
    private TextView tvDateOfBirth, tvMobileNo, txt_annutant_name, txt_annutant_dob;
    private String PL_PERSON_ID, PR_MOBILE, PR_FULL_NM, PR_GENDER, PR_EMAIL, HOLDERPANNUMBER;
    private String parivartanMobile, fullName, email, ADDRESS, BRCOD;

    private GetCustomerDetailsAsync getCustomerDetailsAsync;
    private GenerateOTP_EasyAccess_EMAILAsync generateOTP_easyAccess_emailAsync;
    //private GenerateOTP_EasyAccessAsync generateOTP_EasyAccessAsync;
    private SaveCustProfileChreqAsync saveCustProfileChreqAsync;
    private AsyncCheckPAN mAsyncCheckPAN;
    private boolean validate_pan_card = false;

    private final String METHOD_GET_COE_DETAILS = "getCOEDetail";

    private final DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
            mYear = year;
            mMonth = month;
            mDay = day;
            updateDisplay(mYear, mMonth, mDay);
        }
    };

    //KYC Declaration
    private String IDProofDOCDetails = "", addressProofDOCDetails = "", otherDocDetailsString = "";
    private int increment, uploadFlag;
    private final int REQUEST_OCR = 1;
    private int isBrowseCapture = 0;

    private Spinner spnr_document_upload_document_identity, spnr_document_upload_document_address;
    private LinearLayout llIdentityAadhaarScan, llAddressAadhaarScan;

    private ImageButton img_btn_document_upload_preview_image_identity;
    private ImageButton img_btn_document_delete_identity;
    private ImageButton img_btn_document_upload_click_image_identity;
    private ImageButton img_btn_document_upload_click_browse_identity;
    private ImageButton img_btn_document_upload_click_preview_image_address;
    private ImageButton img_btn_document_delete_address;
    private ImageButton img_btn_document_upload_click_image_address;
    private ImageButton img_btn_document_upload_click_browse_address;
    private ImageButton img_btn_document_upload_click_browse_Others;
    private ImageButton img_btn_document_upload_click_image_others;
    private ImageButton img_btn_document_delete_Others;
    private ImageButton img_btn_document_upload_click_preview_image_others;

    private int deleteAddressDocument, deleteIdentityDocument, deleteOtherDocument;
    private File addressProofFileName, identityProofFileName, otherProofFileName;
    private EditText edt_document_upload_document_others;
    private String str_Doc_Abbreviation_identity = "";
    private String str_Doc_Abbreviation = "";
    private Bitmap identityProofPhotoBitmap, addressProofPhotoBitmap, otherPhotoBitmap;
    private File f;
    private final int REQUEST_CODE_PICK_PHOTO_FILE = 3;
    private final String str_selected_urn_no = "";
    private TextView tvImageIDProofDetails;
    private TextView TVImageAddressProofDetails;
    private TextView TVImageOtherProofDetails;
    private String strUploadDocument = "", declarationOne, requestTypeParivartanService = "";
    private boolean is1WProduct = false;
    private CheckBox checkBoxOtherUserDeclaration;
    private UploadPDFService uploadPDFService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_servicing_request_module);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);

        context = this;
        commonMethods = new CommonMethods();
        mStorageUtils = new StorageUtils();
        commonMethods.setApplicationToolbarMenu(this, "Servicing Request Module");
        getUserDetails();

        edt_upload_all_docs = findViewById(R.id.edt_upload_all_docs);

        transactionNature = "Select Nature Of Transaction Allowed";
        nonPayoutRequest = "Select Non Payout Request";
        payoutRequest = "Select Payout Request";
        documentsStatements = "Select Documents and Statements";
        spinnerTransNatureAllowed = findViewById(R.id.spinnerTransNatureAllowed);

        final ArrayAdapter<CharSequence> transNatureAllowedAdapter = new ArrayAdapter<CharSequence>(context, R.layout.spinner_text,
                getResources().getStringArray(R.array.transactionNatureAllowArray));
        transNatureAllowedAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown);
        spinnerTransNatureAllowed.setAdapter(transNatureAllowedAdapter);

        spinnerNonPayoutRequest = findViewById(R.id.spinnerNonPayoutRequest);
        final ArrayAdapter<CharSequence> nonPayoutRequestAdapter = new ArrayAdapter<CharSequence>(context, R.layout.spinner_text,
                getResources().getStringArray(R.array.nonPayoutRequestArray));
        nonPayoutRequestAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown);
        spinnerNonPayoutRequest.setAdapter(nonPayoutRequestAdapter);

        spinnerPayoutRequest = findViewById(R.id.spinnerPayoutRequest);
        final ArrayAdapter<CharSequence> payoutRequestAdapter = new ArrayAdapter<CharSequence>(context, R.layout.spinner_text,
                getResources().getStringArray(R.array.payoutRequestArray));
        payoutRequestAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown);
        spinnerPayoutRequest.setAdapter(payoutRequestAdapter);

        spinnerDocumentsStatements = findViewById(R.id.spinnerDocumentsStatements);
        final ArrayAdapter<CharSequence> documentsStatementsAdapter = new ArrayAdapter<CharSequence>(context, R.layout.spinner_text,
                getResources().getStringArray(R.array.documentsNStatementsArray));
        documentsStatementsAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown);
        spinnerDocumentsStatements.setAdapter(documentsStatementsAdapter);

        llNonPayoutRequest = findViewById(R.id.llNonPayoutRequest);
        llPayoutRequest = findViewById(R.id.llPayoutRequest);
        llDocumentsStatements = findViewById(R.id.llDocumentsStatements);
        llButtonOK = findViewById(R.id.llButtonOK);
        llDateOfBirth = findViewById(R.id.llDateOfBirth);
        llAnnuityNumberCoe = findViewById(R.id.llAnnuityNumberCoe);
        llPolicyCustomerDetails = findViewById(R.id.llPolicyCustomerDetails);
        tvDateOfBirth = findViewById(R.id.tvDateOfBirth);

        tvDateOfBirth.setOnClickListener(this);
        llOTP = findViewById(R.id.llOTP);
        llCoeDetails = findViewById(R.id.llCoeDetails);
        llKYCUploadDoc = findViewById(R.id.llKYCUploadDoc);
        llKYCUploadDoc.setVisibility(View.VISIBLE);
        etOTP = findViewById(R.id.etOTP);
        etEmailOTP = findViewById(R.id.etEmailOTP);
        etEmailOTP.setVisibility(View.GONE);
        etMobileOTP = findViewById(R.id.etMobileOTP);
        etMobileOTP.setVisibility(View.GONE);
        etCOEOTP = findViewById(R.id.etCOEOTP);
        //etMobileOTP.setVisibility(View.GONE);


        etEmailId = findViewById(R.id.etEmailId);
        etConfirmEmailId = findViewById(R.id.etConfirmEmailId);
        etMobileNo = findViewById(R.id.etMobileNo);
        etConfirmMobileNo = findViewById(R.id.etConfirmMobileNo);


        tvMobileNo = findViewById(R.id.tvMobileNo);

        txt_annutant_name = findViewById(R.id.txt_annutant_name);
        txt_annutant_dob = findViewById(R.id.txt_annutant_dob);

        OTP = "";
        llNonPayoutRequest.setVisibility(View.GONE);
        llPayoutRequest.setVisibility(View.GONE);
        llDocumentsStatements.setVisibility(View.GONE);
        llDateOfBirth.setVisibility(View.GONE);
        llAnnuityNumberCoe.setVisibility(View.GONE);
        llPolicyCustomerDetails.setVisibility(View.GONE);
        llCoeDetails.setVisibility(View.GONE);

        spinnerTransNatureAllowed.setOnItemSelectedListener(this);
        spinnerNonPayoutRequest.setOnItemSelectedListener(this);
        spinnerPayoutRequest.setOnItemSelectedListener(this);
        spinnerDocumentsStatements.setOnItemSelectedListener(this);

        Button buttonOk = findViewById(R.id.buttonOk);
        buttonOk.setOnClickListener(this);
        /*spinnerTransNatureAllowed.setAdapter(ArrayAdapter.createFromResource(this,
                R.array.transactionNatureAllowArray,
                R.layout.spinner_large_words));*/


        userType = commonMethods.GetUserType(context);
        userName = commonMethods.getUserName(context);

        ivCustomerPhotoGraphGPSCord = findViewById(R.id.ivCustomerPhotoGraphGPSCord);
        ibAgentTypeUploadPhoto = findViewById(R.id.ibAgentTypeUploadPhoto);

        ivCustomerPhotoGraphGPSCord.setOnClickListener(this);
        ibAgentTypeUploadPhoto.setOnClickListener(this);

        buttonValidateOTP = findViewById(R.id.buttonValidateOTP);
        buttonGenerateOTP = findViewById(R.id.buttonGenerateOTP);
        buttonGenerateEmailOTP = findViewById(R.id.buttonGenerateEmailOTP);
        buttonValidateEmailOTP = findViewById(R.id.buttonValidateEmailOTP);
        buttonValidateEmailOTP.setVisibility(View.GONE);
        buttonGenerateMobileOTP = findViewById(R.id.buttonGenerateMobileOTP);
        buttonValidateMobileOTP = findViewById(R.id.buttonValidateMobileOTP);
        buttonValidateMobileOTP.setVisibility(View.GONE);
        buttonValidateCOEOTP = findViewById(R.id.buttonValidateCOEOTP);

        etNewPANCard = findViewById(R.id.etNewPANCard);
        etPANOTP = findViewById(R.id.etPANOTP);
        etAnnuityNumber = findViewById(R.id.etAnnuityNumber);
        buttonGeneratePANOTP = findViewById(R.id.buttonGeneratePANOTP);
        buttonValidatePANOTP = findViewById(R.id.buttonValidatePANOTP);
        buttonValidatePANOTP.setVisibility(View.GONE);

        buttonGenerateEmailOTP.setOnClickListener(this);
        buttonValidateEmailOTP.setOnClickListener(this);
        buttonGenerateMobileOTP.setOnClickListener(this);
        buttonValidateMobileOTP.setOnClickListener(this);

        buttonGenerateOTP.setOnClickListener(this);
        buttonValidateOTP.setOnClickListener(this);
        buttonValidateCOEOTP.setOnClickListener(this);
        buttonGeneratePANOTP.setOnClickListener(this);
        buttonValidatePANOTP.setOnClickListener(this);

        gpsTracker = new GPSTracker(context);
        mLatLng = new LatLng(0.0, 0.0);

        mLatLng = commonMethods.getLocationPromt(context, gpsTracker);


        edt_upload_all_docs.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if ((edt_upload_all_docs.getText().toString().replaceAll("\\s", "")).length() == 10) {
                    val_proposer_no = true;
                    //edt_upload_all_docs.setError("");

                } else {
                    val_proposer_no = false;
                    //edt_upload_all_docs.setError("Enter Valid Policy No.");
                }

            }
        });

        mProgressDialog = new ProgressDialog(context, ProgressDialog.THEME_HOLO_LIGHT);
        String Message = "Loading Please wait...";
        mProgressDialog.setMessage(Html.fromHtml("<font color='#00a1e3'><b>" + Message + "<b></font>"));
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(true);

        Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        mProgressDialog.setButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        if (mProgressDialog != null) {
                            if (mProgressDialog.isShowing()) {
                                mProgressDialog.dismiss();
                            }
                        }

                        if (uploadPDFService != null) {
                            uploadPDFService.cancel(true);
                        }

                    }
                });

        mProgressDialog.setMax(100);

        etEmailId.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence arg0, int arg1,
                                      int arg2, int arg3) {

            }

            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
            }

            public void afterTextChanged(Editable arg0) {
                newEmaild = etEmailId.getText().toString();
            }
        });
        etMobileNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() < 10) {

                    buttonGenerateMobileOTP.setVisibility(View.VISIBLE);
                    buttonValidateMobileOTP.setVisibility(View.GONE);
                }
            }
        });

        etNewPANCard.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                newPANCard = etNewPANCard.getText().toString().replaceAll("\\s+", "").trim();
                newPANCard = newPANCard == null ? "" : newPANCard;

                if (newPANCard.length() == 10) {
                    validate_pan_card = commonMethods.valPancard(newPANCard, etNewPANCard);

                    if (validate_pan_card) {

                    } else {
                        etNewPANCard.setError("Invalid PAN Number");
                        validate_pan_card = false;
                    }

                } else {
                    validate_pan_card = false;
                }
            }
        });

        spnr_document_upload_document_identity = findViewById(R.id.spnr_document_upload_document_identity);
        spnr_document_upload_document_identity.setOnItemSelectedListener(this);


        spnr_document_upload_document_identity = findViewById(R.id.spnr_document_upload_document_identity);
        spnr_document_upload_document_identity.setOnItemSelectedListener(this);
        img_btn_document_upload_preview_image_identity = findViewById(R.id.img_btn_document_upload_preview_image_identity);
        img_btn_document_delete_identity = findViewById(R.id.img_btn_document_delete_identity);
        img_btn_document_upload_click_image_identity = findViewById(R.id.img_btn_document_upload_click_image_identity);
        ImageButton img_btn_document_upload_identity_upload = findViewById(R.id.img_btn_document_upload_identity_upload);
        img_btn_document_upload_click_browse_identity = findViewById(R.id.img_btn_document_upload_click_browse_identity);
        llIdentityAadhaarScan = findViewById(R.id.llIdentityAadhaarScan);


        img_btn_document_upload_click_image_identity = findViewById(R.id.img_btn_document_upload_click_image_identity);
        img_btn_document_upload_click_image_address = findViewById(R.id.img_btn_document_upload_click_image_address);

        setBtnListenerOrDisable(img_btn_document_upload_click_image_identity,
                IdentityProofOnClickListener, MediaStore.ACTION_IMAGE_CAPTURE);
        setBtnListenerOrDisable(img_btn_document_upload_click_image_address,
                AddressProofOnClickListener, MediaStore.ACTION_IMAGE_CAPTURE);
        img_btn_document_upload_click_image_others = findViewById(R.id.img_btn_document_upload_click_image_others);
        setBtnListenerOrDisable(img_btn_document_upload_click_image_others,
                OthersProofOnClickListener, MediaStore.ACTION_IMAGE_CAPTURE);
        spnr_document_upload_document_address = findViewById(R.id.spnr_document_upload_document_address);
        spnr_document_upload_document_address.setOnItemSelectedListener(this);
        img_btn_document_upload_click_preview_image_address = findViewById(R.id.img_btn_document_upload_click_preview_image_address);
        img_btn_document_delete_address = findViewById(R.id.img_btn_document_delete_address);
        img_btn_document_upload_click_browse_address = findViewById(R.id.img_btn_document_upload_click_browse_address);
        ImageButton img_btn_document_upload_address_upload = findViewById(R.id.img_btn_document_upload_address_upload);

        img_btn_document_upload_click_browse_Others = findViewById(R.id.img_btn_document_upload_click_browse_Others);

        img_btn_document_delete_Others = findViewById(R.id.img_btn_document_delete_Others);
        img_btn_document_upload_click_preview_image_others = findViewById(R.id.img_btn_document_upload_click_preview_image_others);

        llAddressAadhaarScan = findViewById(R.id.llAddressAadhaarScan);
        edt_document_upload_document_others = findViewById(R.id.edt_document_upload_document_others);
        tvImageIDProofDetails = findViewById(R.id.tvImageIDProofDetails);
        TVImageAddressProofDetails = findViewById(R.id.TVImageAddressProofDetails);
        TVImageOtherProofDetails = findViewById(R.id.TVImageOtherProofDetails);

        List<String> upload_address_doc_name = new ArrayList<>();

        upload_address_doc_name.add("Select Document");
        upload_address_doc_name.add("Passport");
        upload_address_doc_name.add("Driving License");
        upload_address_doc_name.add("Voter s Identity Card");
        upload_address_doc_name.add("Simultaneous proposal");
        upload_address_doc_name.add("COI /AOA-KM");
        upload_address_doc_name.add("Board Resolution -KM");
        upload_address_doc_name.add("Power of Attorney -KM");
        upload_address_doc_name.add("Registration certificate-PF");
        upload_address_doc_name.add("Partnership deed-PF");
        upload_address_doc_name.add("Power of Attorney-PF");
        upload_address_doc_name.add("Aadhar Card");
        // upload_address_doc_name.add("AADHAR Letter");
        upload_address_doc_name.add("Bank A/c stmnt (upto Current month)");
        upload_address_doc_name.add("PostOffice A/c stmnt(upto Current month)");
        upload_address_doc_name.add("Letter-Foreign Embassy/Mission in India");
        upload_address_doc_name.add("Not Applicable");
        upload_address_doc_name.add("Not Submitted");
        upload_address_doc_name.add("Banker's certificate -(Ann III)");
        upload_address_doc_name.add("Docu-Govt Dept of foreign jurisdiction");
        upload_address_doc_name.add("Electricity Bills (max 2 months old)");
        upload_address_doc_name.add("Letter of accomd-State/Central Govt Dept");
        upload_address_doc_name.add("Letter of accomd-Regula/Statutory bodies");
        upload_address_doc_name.add("Letter of accomd-PSU/Scheduled Bank/FIs");
        upload_address_doc_name.add("Post paid mobile bills (max 2 month old)");
        upload_address_doc_name.add("Piped gas & water bill (max 2 month old)");
        upload_address_doc_name.add("PPO to retiree of Govt/PSU with address");
        upload_address_doc_name.add("Power of Attorney – Trusts");
        upload_address_doc_name.add("Property / Municipal Tax receipt");
        upload_address_doc_name.add("Telephone Bill (max 2 months old)");
        upload_address_doc_name
                .add("Valid L&L Agreement-Govt(official accomd)");
        upload_address_doc_name
                .add("Valid L&LAgmtRegulatorybodies-official accomd");
        upload_address_doc_name
                .add("Valid L&LAgmtStatutoryBodies(official accomd)");
        upload_address_doc_name.add("Valid L&L agreement-PSU(official accomd)");
        upload_address_doc_name
                .add("Valid L&L agmt Scheduled Bank-official accomd");
        upload_address_doc_name
                .add("Valid L&L agmt Fina Insti (official accomd)");

        upload_address_doc_name.add("Trust Deed");
        commonMethods.fillSpinnerValue(context, spnr_document_upload_document_address,
                upload_address_doc_name);

        // identity
        List<String> upload_identity_doc_name = new ArrayList<>();

        upload_identity_doc_name.add("Select Document");
        upload_identity_doc_name.add("Passport");
        upload_identity_doc_name.add("Driving License");
        upload_identity_doc_name.add("Pancard");
        upload_identity_doc_name.add("Aadhar Card");
        upload_identity_doc_name.add("Voter s Identity Card");
        upload_identity_doc_name.add("Simultaneous proposal");
        upload_identity_doc_name.add("COI /AOA-KM");
        upload_identity_doc_name.add("Board Resolution -KM");
        upload_identity_doc_name.add("Power of Attorney -KM");
        upload_identity_doc_name.add("Registration certificate-PF");
        upload_identity_doc_name.add("Partnership deed-PF");
        upload_identity_doc_name.add("Power of Attorney-PF");

        upload_identity_doc_name.add("Not Submitted");
        upload_identity_doc_name.add("Annexure I & III");

        upload_identity_doc_name
                .add("Photo ID Card issued by PSUs, Schld Com Banks & Public Financial Inst");
        upload_identity_doc_name
                .add("ID Card with photo issued by Statutory / Regulatory Authorities");
        upload_identity_doc_name
                .add("ID Card with photo issued by Central / State Govt. Dept.");
        upload_identity_doc_name
                .add("Job Card issued by NREGA duly signed by an officer of State Govt");
        upload_identity_doc_name
                .add("Letter issued by UIDAI or National Population Register (name, address, AADHAR no.)");
        upload_identity_doc_name
                .add("Letter issued by a Gazetted Officer with a duly attested photo of the person.");
        upload_identity_doc_name.add("Trust Deed");
        commonMethods.fillSpinnerValue(context, spnr_document_upload_document_identity,
                upload_identity_doc_name);

        TextView tvUserTypeDescritionTitle = findViewById(R.id.tvUserTypeDescritionTitle);
        TextView TVLMCIFBDMUMGPSDetails = findViewById(R.id.TVLMCIFBDMUMGPSDetails);
        TextView tvUserTypePhotoTitle = findViewById(R.id.tvUserTypePhotoTitle);
        String userDetails = "User Name : " + userName + " \nUser Code : " + strCIFBDMUserId;

        tvUserTypeDescritionTitle.setText(userType);
        TVLMCIFBDMUMGPSDetails.setText(userDetails);
        tvUserTypePhotoTitle.setText(userType + " Photo");

        checkBoxOtherUserDeclaration = findViewById(R.id.checkBoxOtherUserDeclaration);

        declarationOne = "I hereby declare that I have read out and explained the contents of this proposal " +
                "form and all other documents incidental to availing the insurance\n" +
                "policy from SBI Life Insurance Company Ltd. to the Proposer /policyholder/member insured " +
                "and that he/she declared that he/she has understood the same completely." +
                "I hereby declare that I have fully explained to the Proposer /policyholder/member insured  the answers " +
                "to the questions that form the basis of the contract of insurance and I also\n" +
                "explained to the Proposer /policyholder/member insured  that if there is any mis-statement " +
                "or suppression of material information or if any untrue statements are contained therein or in " +
                "case of fraud, the said contract shall be treated as per the provisions of Section 45 of the " +
                "Insurance Act 1938 as amended from time to time.";
        checkBoxOtherUserDeclaration.setText(declarationOne);
    }

    private void getUserDetails() {
        CommonMethods.UserDetailsValuesModel userDetailsValuesModel = commonMethods.setUserDetails(context);
        strCIFBDMUserId = userDetailsValuesModel.getStrCIFBDMUserId();
        strCIFBDMEmailId = userDetailsValuesModel.getStrCIFBDMEmailId();
        strCIFBDMMObileNo = userDetailsValuesModel.getStrCIFBDMMObileNo();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonOk:
                if (commonMethods.isNetworkConnected(context)) {

                    commonMethods.hideKeyboard(edt_upload_all_docs, context);
                    llPolicyCustomerDetails.setVisibility(View.GONE);
                    etEmailOTP.setVisibility(View.GONE);
                    buttonValidateEmailOTP.setVisibility(View.GONE);
                    buttonGenerateEmailOTP.setVisibility(View.VISIBLE);

                    buttonValidatePANOTP.setVisibility(View.GONE);
                    buttonGeneratePANOTP.setVisibility(View.VISIBLE);

                    etMobileOTP.setVisibility(View.GONE);
                    etEmailOTP.setText("");

                    etEmailId.setText("");
                    etEmailId.setError("");
                    etConfirmEmailId.setText("");
                    etMobileNo.setText("");
                    etMobileOTP.setText("");
                    etConfirmMobileNo.setText("");
                    etNewPANCard.setText("");
                    if (transactionNature.equalsIgnoreCase("Select Nature Of Transaction Allowed")) {
                        commonMethods.showMessageDialog(context, "Please Select Nature Of Transaction Allowed");
                        return;
                    } else if (transactionNature.equalsIgnoreCase("Non Payout Request")) {
                        if (nonPayoutRequest.equalsIgnoreCase("Select Non Payout Request")) {
                            commonMethods.showMessageDialog(context, "Please Select Non Payout Request");
                            return;
                        }
                        payoutRequest = "Select Payout Request";
                        documentsStatements = "Select Documents and Statements";
                    } else if (transactionNature.equalsIgnoreCase("Payout Request")) {
                        if (payoutRequest.equalsIgnoreCase("Select Payout Request")) {
                            commonMethods.showMessageDialog(context, "Please Select Payout Request");
                            return;
                        }
                        nonPayoutRequest = "Select Non Payout Request";
                        documentsStatements = "Select Documents and Statements";
                    } else if (transactionNature.equalsIgnoreCase("Documents and Statements")) {
                        if (documentsStatements.equalsIgnoreCase("Select Documents and Statements")) {
                            commonMethods.showMessageDialog(context, "Please Select Documents and Statements");
                            return;
                        }
                        nonPayoutRequest = "Select Non Payout Request";
                        payoutRequest = "Select Payout Request";
                    }

                    policyNumber = edt_upload_all_docs.getText().toString().trim();
                    if (TextUtils.isEmpty(policyNumber)) {
                        policyNumber = "";
                        commonMethods.showMessageDialog(context, "Please enter Policy Number");
                        commonMethods.setFocusable(edt_upload_all_docs);
                        edt_upload_all_docs.requestFocus();
                        return;
                    }

                    if (identityProofFileName == null) {
                        commonMethods.showMessageDialog(context, "Please Upload Identity Proof");
                        commonMethods.setFocusable(spnr_document_upload_document_identity);
                        spnr_document_upload_document_identity.requestFocus();
                        return;
                    }

                    if (addressProofFileName == null) {
                        commonMethods.showMessageDialog(context, "Please Upload Address Proof");
                        commonMethods.setFocusable(spnr_document_upload_document_address);
                        spnr_document_upload_document_address.requestFocus();
                        return;
                    }


                    if (userPhotoBitmap == null) {
                        commonMethods.showMessageDialog(context, "Please Capture User Photo");
                        commonMethods.setFocusable(ivCustomerPhotoGraphGPSCord);
                        ivCustomerPhotoGraphGPSCord.requestFocus();
                        return;
                    }

                    if (agentPhotoBitmap == null) {
                        commonMethods.showMessageDialog(context, "Please Capture Agent Photo");
                        commonMethods.setFocusable(ibAgentTypeUploadPhoto);
                        ibAgentTypeUploadPhoto.requestFocus();
                        return;
                    }

                    if (!checkBoxOtherUserDeclaration.isChecked()) {
                        commonMethods.showMessageDialog(context, "Please accept the declaration");
                        commonMethods.setFocusable(checkBoxOtherUserDeclaration);
                        checkBoxOtherUserDeclaration.requestFocus();
                        return;
                    }

                    if (nonPayoutRequest.equalsIgnoreCase("Email id update") ||
                            nonPayoutRequest.equalsIgnoreCase("PAN number update") ||
                            nonPayoutRequest.equalsIgnoreCase("Mobile number update")) {
                        dob = tvDateOfBirth.getText().toString();
                        if (TextUtils.isEmpty(dob)) {
                            commonMethods.showMessageDialog(context, commonMethods.DOB_ALERT);
                            return;
                        }

                        final SimpleDateFormat formatter = new SimpleDateFormat(
                                "dd-MMMM-yyyy", Locale.ENGLISH);
                        SimpleDateFormat finalDateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);

                        Date d1 = null, d2 = null;
                        try {
                            d1 = formatter.parse(dob);
                            dob = finalDateFormat.format(d1);
                            //dob = dob.toUpperCase();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        commonMethods.hideKeyboard(edt_upload_all_docs, context);
                        getCustomerDetailsAsync = new GetCustomerDetailsAsync();
                        getCustomerDetailsAsync.execute();
                    } else if (nonPayoutRequest.equalsIgnoreCase("Certificate of Existence for annuity holders")) {
                        llCoeDetails.setVisibility(View.GONE);
                        etCOEOTP.setText("");
                        txt_annutant_name.setText("");
                        txt_annutant_dob.setText("");
                        annuityNo = etAnnuityNumber.getText().toString();
                        if ((policyNumber.substring(0, 2).equals("65") || policyNumber.substring(0, 2).equals("91"))
                                && (annuityNo == null || annuityNo.equalsIgnoreCase(""))) {
                            llAnnuityNumberCoe.setVisibility(View.VISIBLE);
                            etAnnuityNumber.setFocusable(true);
                            etAnnuityNumber.requestFocus();
                            commonMethods.showMessageDialog(context, "Please Enter Annuity Number.");
                            return;
                        }
                        if (TextUtils.isEmpty(annuityNo)) {
                            annuityNo = "";
                        }
                        GetCOEDetails getCOEDetails = new GetCOEDetails(policyNumber, annuityNo);
                        getCOEDetails.execute();
                    }
                } else {
                    commonMethods.showMessageDialog(context, commonMethods.NO_INTERNET_MESSAGE);
                }
                break;

            case R.id.buttonGeneratePANOTP:
            case R.id.buttonGenerateMobileOTP:
            case R.id.buttonGenerateEmailOTP:
                if (commonMethods.isNetworkConnected(context)) {
                    validation("PANMobEmailUpdate");
                } else {
                    commonMethods.showMessageDialog(context, commonMethods.NO_INTERNET_MESSAGE);
                }
                break;

            case R.id.buttonValidateEmailOTP:
            case R.id.buttonValidateMobileOTP:
            case R.id.buttonValidatePANOTP:
                if (commonMethods.isNetworkConnected(context)) {
                    validation("PANMobEmailUpdateValidation");
                } else {
                    commonMethods.showMessageDialog(context, commonMethods.NO_INTERNET_MESSAGE);
                }
                break;

            case R.id.buttonValidateCOEOTP:
                if (commonMethods.isNetworkConnected(context)) {
                    if (transactionNature.equalsIgnoreCase("Select Nature Of Transaction Allowed")) {
                        commonMethods.showMessageDialog(context, "Please Select Nature Of Transaction Allowed");
                        return;
                    } else if (transactionNature.equalsIgnoreCase("Non Payout Request")) {
                        if (nonPayoutRequest.equalsIgnoreCase("Select Non Payout Request")) {
                            commonMethods.showMessageDialog(context, "Please Select Non Payout Request");
                            return;
                        }
                        payoutRequest = "Select Payout Request";
                        documentsStatements = "Select Documents and Statements";
                    } else if (transactionNature.equalsIgnoreCase("Payout Request")) {
                        if (payoutRequest.equalsIgnoreCase("Select Payout Request")) {
                            commonMethods.showMessageDialog(context, "Please Select Payout Request");
                            return;
                        }
                        nonPayoutRequest = "Select Non Payout Request";
                        documentsStatements = "Select Documents and Statements";
                    } else if (transactionNature.equalsIgnoreCase("Documents and Statements")) {
                        if (documentsStatements.equalsIgnoreCase("Select Documents and Statements")) {
                            commonMethods.showMessageDialog(context, "Please Select Documents and Statements");
                            return;
                        }
                        nonPayoutRequest = "Select Non Payout Request";
                        payoutRequest = "Select Payout Request";
                    }

                    String mobileOTP = etCOEOTP.getText().toString();
                    if (TextUtils.isEmpty(mobileOTP)) {
                        commonMethods.showMessageDialog(context, "Please Enter valid OTP.");
                        return;
                    }

                    /*validateOTPGeneralAsyncTask = new ValidateOTPGeneralAsyncTask(AnnuityPersonDetails.getCONTACTMOBILE(),
                            mobileOTP, this, context);
                    validateOTPGeneralAsyncTask.execute();*/
                } else {
                    commonMethods.showMessageDialog(context, commonMethods.NO_INTERNET_MESSAGE);
                }
                break;


            case R.id.ivCustomerPhotoGraphGPSCord:
                Check = "UserPhoto";
                capture_all_docs("1");

                break;
            case R.id.ibAgentTypeUploadPhoto:

                Check = "AgentPhoto";
                capture_all_docs("2");
                break;

            case R.id.buttonValidateOTP:
                commonMethods.hideKeyboard(etOTP, context);

                if (commonMethods.isNetworkConnected(context)) {
                    OTP = etOTP.getText().toString();
                    if (TextUtils.isEmpty(OTP)) {
                        commonMethods.showMessageDialog(context, "Please Enter valid OTP.");
                        return;
                    } else {
                        validation("validate");
                    }

                } else {
                    commonMethods.showMessageDialog(context, commonMethods.NO_INTERNET_MESSAGE);
                }
                break;
            case R.id.buttonGenerateOTP:
                commonMethods.hideKeyboard(etOTP, context);

                if (commonMethods.isNetworkConnected(context)) {
                    validation("Generate");
                } else {
                    commonMethods.showMessageDialog(context, commonMethods.NO_INTERNET_MESSAGE);
                }
                break;

            case R.id.tvDateOfBirth:
                datecheck = 1;
                showDateDialog();
                break;
            default:
                break;

        }
    }

    private void showDateDialog() {
        DatePickerDialog dialog = new DatePickerDialog(
                context,
                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                mDateSetListener,
                mYear, mMonth, mDay);
        dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    private void validation(String flag) {

        commonMethods.hideKeyboard(edt_upload_all_docs, context);

        if (transactionNature.equalsIgnoreCase("Select Nature Of Transaction Allowed")) {
            commonMethods.showMessageDialog(context, "Please Select Nature Of Transaction Allowed");
            return;
        } else if (transactionNature.equalsIgnoreCase("Non Payout Request")) {
            if (nonPayoutRequest.equalsIgnoreCase("Select Non Payout Request")) {
                commonMethods.showMessageDialog(context, "Please Select Non Payout Request");
                return;
            }
            payoutRequest = "Select Payout Request";
            documentsStatements = "Select Documents and Statements";
        } else if (transactionNature.equalsIgnoreCase("Payout Request")) {
            if (payoutRequest.equalsIgnoreCase("Select Payout Request")) {
                commonMethods.showMessageDialog(context, "Please Select Payout Request");
                return;
            }
            nonPayoutRequest = "Select Non Payout Request";
            documentsStatements = "Select Documents and Statements";
        } else if (transactionNature.equalsIgnoreCase("Documents and Statements")) {
            if (documentsStatements.equalsIgnoreCase("Select Documents and Statements")) {
                commonMethods.showMessageDialog(context, "Please Select Documents and Statements");
                return;
            }
            nonPayoutRequest = "Select Non Payout Request";
            payoutRequest = "Select Payout Request";
        }

        policyNumber = edt_upload_all_docs.getText().toString().trim();
        if (TextUtils.isEmpty(policyNumber)) {
            policyNumber = "";
            commonMethods.showMessageDialog(context, "Please enter Policy Number");
            commonMethods.setFocusable(edt_upload_all_docs);
            edt_upload_all_docs.requestFocus();
            return;
        }

        if (identityProofFileName == null) {
            commonMethods.showMessageDialog(context, "Please Upload Identity Proof");
            commonMethods.setFocusable(spnr_document_upload_document_identity);
            spnr_document_upload_document_identity.requestFocus();
            return;
        }

        if (addressProofFileName == null) {
            commonMethods.showMessageDialog(context, "Please Upload Address Proof");
            commonMethods.setFocusable(spnr_document_upload_document_address);
            spnr_document_upload_document_address.requestFocus();
            return;
        }


        if (userPhotoBitmap == null) {
            commonMethods.showMessageDialog(context, "Please Capture User Photo");
            commonMethods.setFocusable(ivCustomerPhotoGraphGPSCord);
            ivCustomerPhotoGraphGPSCord.requestFocus();
            return;
        }

        if (agentPhotoBitmap == null) {
            commonMethods.showMessageDialog(context, "Please Capture Agent Photo");
            commonMethods.setFocusable(ibAgentTypeUploadPhoto);
            ibAgentTypeUploadPhoto.requestFocus();
            return;
        }

        if (!checkBoxOtherUserDeclaration.isChecked()) {
            commonMethods.showMessageDialog(context, "Please accept the declaration");
            commonMethods.setFocusable(checkBoxOtherUserDeclaration);
            checkBoxOtherUserDeclaration.requestFocus();
            return;
        }

        if (nonPayoutRequest.equalsIgnoreCase("Premium Holiday")) {
            if (!policyNumber.substring(0, 2).equals("56")) {
                commonMethods.showMessageDialog(context, "This Policy is not Allowed for Premium Holiday");
                commonMethods.setFocusable(edt_upload_all_docs);
                edt_upload_all_docs.requestFocus();
                return;
            }
        }


        if (nonPayoutRequest.equalsIgnoreCase("Fund Switch")
                || nonPayoutRequest.equalsIgnoreCase("Bank Account update")
                || nonPayoutRequest.equalsIgnoreCase("Address Change")
                || payoutRequest.equalsIgnoreCase("Partial Withdraw")) {
            dob = tvDateOfBirth.getText().toString();
            if (TextUtils.isEmpty(dob)) {
                commonMethods.showMessageDialog(context, commonMethods.DOB_ALERT);
                return;
            } else {
                final SimpleDateFormat formatter = new SimpleDateFormat(
                        "dd-MMMM-yyyy", Locale.ENGLISH);
                SimpleDateFormat finalDateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);

                Date d1 = null, d2 = null;
                try {
                    d1 = formatter.parse(dob);
                    dob = finalDateFormat.format(d1);
                    //dob = dob.toUpperCase();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }

        if (flag.equalsIgnoreCase("PANMobEmailUpdate")) {
            if (nonPayoutRequest.equalsIgnoreCase("Email id update") ||
                    nonPayoutRequest.equalsIgnoreCase("PAN number update") ||
                    nonPayoutRequest.equalsIgnoreCase("Mobile number update")) {
                dob = tvDateOfBirth.getText().toString();
                if (TextUtils.isEmpty(dob)) {
                    commonMethods.showMessageDialog(context, commonMethods.DOB_ALERT);
                    return;
                }

                final SimpleDateFormat formatter = new SimpleDateFormat(
                        "dd-MMMM-yyyy", Locale.ENGLISH);
                SimpleDateFormat finalDateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);

                Date d1 = null, d2 = null;
                try {
                    d1 = formatter.parse(dob);
                    dob = finalDateFormat.format(d1);
                    //dob = dob.toUpperCase();
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }

            if (nonPayoutRequest.equalsIgnoreCase("PAN number update")) {
                newPANCard = etNewPANCard.getText().toString();
                if (validate_pan_card) {
                    generateOTPGeneralAsyncTask = new GenerateOTPGeneralAsyncTask(PR_MOBILE,
                            this, context);
                    generateOTPGeneralAsyncTask.execute();
                } else {
                    commonMethods.showMessageDialog(context, "Please enter valid PAN");
                    commonMethods.setFocusable(etNewPANCard);
                    etNewPANCard.requestFocus();
                }

            } else if (nonPayoutRequest.equalsIgnoreCase("Mobile number update")) {
                newMobileNo = etMobileNo.getText().toString();
                confirmMobileNo = etConfirmMobileNo.getText().toString();

                if (!commonMethods.mobileNumberPatternValidation(etMobileNo, context)) {
                    newMobileNo = "";
                    commonMethods.showMessageDialog(context, "Please enter valid Mobile No");
                    commonMethods.setFocusable(etEmailId);
                    etMobileNo.requestFocus();
                    return;
                } else if (!newMobileNo.equalsIgnoreCase(confirmMobileNo)) {
                    commonMethods.showMessageDialog(context, "Please Confirm Mobile No");
                    return;
                } else {
                    newMobileNo = etMobileNo.getText().toString().trim();
                }
                commonMethods.hideKeyboard(etMobileNo, context);
                buttonValidateMobileOTP.setVisibility(View.GONE);
                buttonGenerateMobileOTP.setVisibility(View.VISIBLE);
                    /*generateOTP_EasyAccessAsync = new GenerateOTP_EasyAccessAsync(newMobileNo);
                    generateOTP_EasyAccessAsync.execute();*/
                generateOTPGeneralAsyncTask = new GenerateOTPGeneralAsyncTask(newMobileNo,
                        this, context);
                generateOTPGeneralAsyncTask.execute();
            } else if (nonPayoutRequest.equalsIgnoreCase("Email id update")) {
                newEmaild = etEmailId.getText().toString();
                confirmEmailId = etConfirmEmailId.getText().toString();

                if (!commonMethods.emailPatternValidation(etEmailId, context)) {
                    newEmaild = "";
                    commonMethods.showMessageDialog(context, "Please enter valid Email-ID");
                    commonMethods.setFocusable(etEmailId);
                    etEmailId.requestFocus();
                    return;
                }

                if (!newEmaild.equalsIgnoreCase(confirmEmailId)) {
                    commonMethods.showMessageDialog(context, "Please Confirm Emaild Id");
                    return;
                }

                newEmaild = etEmailId.getText().toString().trim();
                commonMethods.hideKeyboard(etEmailId, context);
                buttonValidateEmailOTP.setVisibility(View.GONE);
                buttonGenerateEmailOTP.setVisibility(View.VISIBLE);

                generateOTP_easyAccess_emailAsync = new GenerateOTP_EasyAccess_EMAILAsync();
                generateOTP_easyAccess_emailAsync.execute();
            }

        } else if (flag.equalsIgnoreCase("PANMobEmailUpdateValidation")) {
            if (nonPayoutRequest.equalsIgnoreCase("PAN number update")) {
                newPANCard = etNewPANCard.getText().toString();
                if (validate_pan_card) {
                    commonMethods.hideKeyboard(etPANOTP, context);
                    String PANOTP = etPANOTP.getText().toString();
                    if (TextUtils.isEmpty(PANOTP)) {
                        commonMethods.showMessageDialog(context, "Please Enter valid OTP.");
                        return;
                    }

                    validateOTPGeneralAsyncTask = new ValidateOTPGeneralAsyncTask(PR_MOBILE,
                            PANOTP, this, context);
                    validateOTPGeneralAsyncTask.execute();

                } else {
                    commonMethods.showMessageDialog(context, "Please enter valid PAN");
                    commonMethods.setFocusable(etNewPANCard);
                    etNewPANCard.requestFocus();
                }


            } else if (nonPayoutRequest.equalsIgnoreCase("Mobile number update")) {
                newMobileNo = etMobileNo.getText().toString();
                confirmMobileNo = etConfirmMobileNo.getText().toString();

                if (!commonMethods.mobileNumberPatternValidation(etMobileNo, context)) {
                    newMobileNo = "";
                    commonMethods.showMessageDialog(context, "Please enter valid Mobile No");
                    commonMethods.setFocusable(etMobileNo);
                    etMobileNo.requestFocus();
                    return;
                } else if (!newMobileNo.equalsIgnoreCase(confirmMobileNo)) {
                    commonMethods.showMessageDialog(context, "Please Confirm Mobile No");
                    return;
                } else {
                    newMobileNo = etMobileNo.getText().toString().trim();
                }
                commonMethods.hideKeyboard(etMobileNo, context);
                String mobileOTP = etMobileOTP.getText().toString();
                if (TextUtils.isEmpty(mobileOTP)) {
                    commonMethods.showMessageDialog(context, "Please Enter valid OTP.");
                    return;
                }

                validateOTPGeneralAsyncTask = new ValidateOTPGeneralAsyncTask(newMobileNo,
                        mobileOTP, this, context);
                validateOTPGeneralAsyncTask.execute();
            } else if (nonPayoutRequest.equalsIgnoreCase("Email id update")) {
                newEmaild = etEmailId.getText().toString();
                confirmEmailId = etConfirmEmailId.getText().toString();

                if (!commonMethods.emailPatternValidation(etEmailId, context)) {
                    newEmaild = "";
                    commonMethods.showMessageDialog(context, "Please enter valid Email-ID");
                    commonMethods.setFocusable(etEmailId);
                    etEmailId.requestFocus();
                    return;
                } else if (!newEmaild.equalsIgnoreCase(confirmEmailId)) {
                    commonMethods.showMessageDialog(context, "Please Confirm Emaild Id");
                } else {
                    newEmaild = etEmailId.getText().toString().trim();
                }
                commonMethods.hideKeyboard(etEmailId, context);
                String emailOTP = etEmailOTP.getText().toString();
                if (TextUtils.isEmpty(emailOTP)) {
                    commonMethods.showMessageDialog(context, "Please Enter valid OTP.");
                    return;
                }

                validateOTPGeneralAsyncTask = new ValidateOTPGeneralAsyncTask(PR_MOBILE,
                        emailOTP, this, context);
                validateOTPGeneralAsyncTask.execute();
            }

        }

    }

    public void generateOTPAsynResultMethod(int flag) {
        if (flag == 1) {

            commonMethods.showMessageDialog(context, "OTP sent succesfully to your Mobile Number.");

            if (nonPayoutRequest.equalsIgnoreCase("Mobile number update")) {
                llPolicyCustomerDetails.setVisibility(View.VISIBLE);
                etMobileOTP.setVisibility(View.VISIBLE);
                buttonValidateMobileOTP.setVisibility(View.VISIBLE);
                buttonGenerateMobileOTP.setVisibility(View.GONE);
            } else if (nonPayoutRequest.equalsIgnoreCase("PAN number update")) {
                llPolicyCustomerDetails.setVisibility(View.VISIBLE);
                etPANOTP.setVisibility(View.VISIBLE);
                buttonValidatePANOTP.setVisibility(View.VISIBLE);
                buttonGeneratePANOTP.setVisibility(View.GONE);
            } else if (nonPayoutRequest.equalsIgnoreCase("Certificate of Existence for annuity holders")) {
                /*llCoeDetails.setVisibility(View.VISIBLE);
                txt_annutant_name.setText(annuityPersonDetails.getNAMEOFANNUITANT());
                txt_annutant_dob.setText(annuityPersonDetails.getDATEOFBIRTHOFANNUITANT());*/
            } else {
                buttonValidateOTP.setEnabled(true);
                buttonValidateOTP.setBackgroundColor(Color.parseColor("#00a1e3"));

                buttonGenerateOTP.setEnabled(false);
                buttonGenerateOTP.setBackgroundColor(Color.parseColor("#A9A9A9"));
                etOTP.setVisibility(View.VISIBLE);
                etOTP.setFocusable(true);
                etOTP.requestFocus();
            }

        } else {
            commonMethods.showMessageDialog(context, "Please try after sometime");
        }
    }


    public void validateOTPAsynResultMethod(int flag) {
        if (flag == 1) {
            uploadPDFService = new UploadPDFService("IDProof");
            uploadPDFService.execute();
        } else {
            buttonGenerateOTP.setEnabled(true);
            buttonGenerateOTP.setBackgroundColor(Color.parseColor("#00a1e3"));
            commonMethods.showMessageDialog(context, "Invalid Passcode. Please re-enter Passcode");
        }

        /*uploadPDFService = new UploadPDFService("IDProof");
        uploadPDFService.execute();*/
    }

    private void capture_all_docs(String flag) {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        String imageFileName = "";
        if (flag.equalsIgnoreCase("1")) {
            imageFileName = "custPhoto.jpg";
            customerPhotoFileName = mStorageUtils.createFileToAppSpecificDir(context,imageFileName);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, commonMethods.getContentUri(context,
                        customerPhotoFileName));
            } else {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(customerPhotoFileName));
            }
        }else if (flag.equalsIgnoreCase("2")) {
            imageFileName = "agentPhoto.jpg";

            agentPhotoFileName = mStorageUtils.createFileToAppSpecificDir(context,imageFileName);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, commonMethods.getContentUri(context,
                        agentPhotoFileName));
            } else {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(agentPhotoFileName));
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                takePictureIntent.putExtra("android.intent.extras.LENS_FACING_FRONT", 1);
            } else {
                takePictureIntent.putExtra("android.intent.extras.CAMERA_FACING", 1);
            }
        }
        startActivityForResult(takePictureIntent, REQUEST_CODE_PICK_FILE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_OCR) {
            if (resultCode == RESULT_OK) {
                File imagePath = null;
                String DocumentType = "";
                String proofDetails = "";
                Bundle bundle = data.getExtras();
                if (bundle != null) {
                    String jsonData = (String) bundle.get("jsonData");

                    try {
                        JSONObject object = new JSONObject(jsonData);
                        proofDetails = jsonData;
                        DocumentType = object.get("DocumentType").toString();

                        imagePath = new File(bundle.get("BitmapImageUri").toString());
                        //Bitmap edgeBitmap = BitmapFactory.decodeFile(imagePath.getAbsolutePath());

                    } catch (Exception e) {
                        e.printStackTrace();
                        /*imagePath = (File) bundle.get("BitmapImageUri");
                        Bitmap edgeBitmap = BitmapFactory.decodeFile(imagePath.getAbsolutePath());*/
                    }
                }
                //
                File destinationFile = null;
                if (Check.equals("IdentityProof")) {

                    increment = 2;
                    boolean is_Doc_Detected = true;
                    IDProofDOCDetails = spnr_document_upload_document_identity.getSelectedItem().toString();

                    if (IDProofDOCDetails.toLowerCase().contains("aadhar card")
                            || IDProofDOCDetails.toLowerCase().contains("pancard")
                            || IDProofDOCDetails.toLowerCase().contains("passport")
                            || IDProofDOCDetails.toLowerCase().contains("voter s identity card")
                            || IDProofDOCDetails.toLowerCase().contains("driving license")) {
                        if (IDProofDOCDetails.toLowerCase().contains("aadhar") && (IDProofDOCDetails.toLowerCase().contains("AADHAAR CARD")
                                || DocumentType.equalsIgnoreCase("BACK AADHAAR CARD") || DocumentType.equalsIgnoreCase("FRONT BACK AADHAAR"))) {
                            is_Doc_Detected = true;
                            str_Doc_Abbreviation_identity = "AADHAR";
                        } else if (IDProofDOCDetails.toLowerCase().contains("pancard")
                                && DocumentType.equalsIgnoreCase("PAN")) {
                            is_Doc_Detected = true;
                            str_Doc_Abbreviation_identity = "PAN";
                        } else if (IDProofDOCDetails.toLowerCase().contains("passport")
                                && (DocumentType.equalsIgnoreCase("PASSPORT") || DocumentType.equalsIgnoreCase("PASSPORT BACK") || DocumentType.equalsIgnoreCase("FRONT BACK PASSPORT"))) {
                            is_Doc_Detected = true;
                            str_Doc_Abbreviation_identity = "PASSPORT";
                        } else if (IDProofDOCDetails.toLowerCase().contains("voter s identity card")
                                && (DocumentType.equalsIgnoreCase("VOTER ID") || DocumentType.equalsIgnoreCase("BACK VOTER ID") || DocumentType.equalsIgnoreCase("FRONT BACK VOTER"))) {
                            is_Doc_Detected = true;
                            str_Doc_Abbreviation_identity = "VID";
                        } else if (IDProofDOCDetails.toLowerCase().contains("driving license") && (DocumentType.equalsIgnoreCase("DRIVING LICENCE") || DocumentType.equalsIgnoreCase("BACK DRIVING LICENCE") || DocumentType.equalsIgnoreCase("FRONT BACK DRIVING LICENCE"))) {
                            is_Doc_Detected = true;
                            str_Doc_Abbreviation_identity = "DL";
                        } else {
                            is_Doc_Detected = false;
                        }
                        IDProofDOCDetails = proofDetails;
                    } else if (IDProofDOCDetails.equalsIgnoreCase("Job Card issued by NREGA duly signed by an officer of State Govt")) {
                        is_Doc_Detected = true;
                        str_Doc_Abbreviation_identity = "NJC";
                    } else {
                        is_Doc_Detected = true;
                        str_Doc_Abbreviation_identity = "XID";
                    }

                    String FileName = policyNumber + "_" + str_Doc_Abbreviation_identity + "_" + "0"
                            + increment + "." + "png";
                    //image compression by bhalla
                    CompressImage.compressImage(imagePath.getAbsolutePath());
                    identityProofFileName = mStorageUtils.saveFileToAppSpecificDir(context, StorageUtils.DIRECT_DIRECTORY,
                            FileName, imagePath);

                    if (identityProofFileName.exists()) {
                        identityProofPhotoBitmap = BitmapFactory.decodeFile(identityProofFileName.getAbsolutePath());
                    }
                    if (is_Doc_Detected) {

                        tvImageIDProofDetails.setText(IDProofDOCDetails);
                        img_btn_document_upload_preview_image_identity
                                .setVisibility(View.VISIBLE);
                        img_btn_document_delete_identity
                                .setVisibility(View.VISIBLE);

                        if (isBrowseCapture == REQUEST_CODE_PICK_FILE) {
                            img_btn_document_upload_click_image_identity
                                    .setImageDrawable(getResources().getDrawable(
                                            R.drawable.ibtn_camera));

                            img_btn_document_upload_click_browse_identity.setImageDrawable(getResources().getDrawable(
                                    R.drawable.checkedbrowse));

                        } else {
                            img_btn_document_upload_click_image_identity.setImageDrawable(
                                    getResources().getDrawable(R.drawable.checkedcamera));

                            img_btn_document_upload_click_browse_identity.setImageDrawable(getResources().getDrawable(
                                    R.drawable.ibtn_browsedoc));
                        }

                    } else {
                        img_btn_document_upload_preview_image_identity
                                .setVisibility(View.INVISIBLE);
                        img_btn_document_delete_identity
                                .setVisibility(View.INVISIBLE);

                        img_btn_document_upload_click_image_identity
                                .setImageDrawable(getResources().getDrawable(
                                        R.drawable.ibtn_camera));

                        img_btn_document_upload_click_browse_identity.setImageDrawable(getResources().getDrawable(
                                R.drawable.ibtn_browsedoc));

                        if (identityProofFileName.exists()) {
                            identityProofFileName.delete();
                            identityProofFileName = null;
                        }

                        Toast.makeText(this, "Document is not properly detected,Kindly capture the actual document. ", Toast.LENGTH_LONG)
                                .show();

                    }

                    commonMethods.setFocusable(spnr_document_upload_document_identity);
                    spnr_document_upload_document_identity.requestFocus();

                }
                else if (Check.equals("AddressProof")) {

                    increment = 3;
                    boolean is_Doc_Detected = true;
                    addressProofDOCDetails = spnr_document_upload_document_address.getSelectedItem().toString();

                    if (addressProofDOCDetails.toLowerCase().contains("aadhar card")
                            || addressProofDOCDetails.toLowerCase().contains("pancard")
                            || addressProofDOCDetails.toLowerCase().contains("passport")
                            || addressProofDOCDetails.toLowerCase().contains("voter s identity card")
                            || addressProofDOCDetails.toLowerCase().contains("driving license")) {
                        if (addressProofDOCDetails.toLowerCase().contains("aadhar") && (DocumentType.equalsIgnoreCase("AADHAAR CARD") || DocumentType.equalsIgnoreCase("BACK AADHAAR CARD") || DocumentType.equalsIgnoreCase("FRONT BACK AADHAAR"))) {
                            is_Doc_Detected = true;
                            str_Doc_Abbreviation = "AADHAR";
                        } else if (addressProofDOCDetails.toLowerCase().contains("pancard") && DocumentType.equalsIgnoreCase("PAN")) {
                            is_Doc_Detected = true;
                            str_Doc_Abbreviation = "PAN";
                        } else if (addressProofDOCDetails.toLowerCase().contains("passport") && (DocumentType.equalsIgnoreCase("PASSPORT") || DocumentType.equalsIgnoreCase("PASSPORT BACK") || DocumentType.equalsIgnoreCase("FRONT BACK PASSPORT"))) {
                            is_Doc_Detected = true;
                            str_Doc_Abbreviation = "PASSPORT";
                        } else if (addressProofDOCDetails.toLowerCase().contains("voter s identity card") && (DocumentType.equalsIgnoreCase("VOTER ID") || DocumentType.equalsIgnoreCase("BACK VOTER ID") || DocumentType.equalsIgnoreCase("FRONT BACK VOTER"))) {
                            is_Doc_Detected = true;
                            str_Doc_Abbreviation = "VID";
                        } else if (addressProofDOCDetails.toLowerCase().contains("driving license") && (DocumentType.equalsIgnoreCase("DRIVING LICENCE") || DocumentType.equalsIgnoreCase("BACK DRIVING LICENCE") || DocumentType.equalsIgnoreCase("FRONT BACK DRIVING LICENCE"))) {
                            is_Doc_Detected = true;
                            str_Doc_Abbreviation = "DL";
                        } else if (addressProofDOCDetails.equalsIgnoreCase("Job Card issued by NREGA duly signed by an officer of State Govt")) {
                            is_Doc_Detected = false;
                            str_Doc_Abbreviation = "NJC";
                        } else {
                            is_Doc_Detected = false;
                            str_Doc_Abbreviation = "XADD";
                        }
                        addressProofDOCDetails = proofDetails;

                    } else {
                        is_Doc_Detected = true;
                    }

                    commonMethods.setFocusable(spnr_document_upload_document_address);
                    spnr_document_upload_document_address.requestFocus();

                    String fileName = policyNumber + "_" + str_Doc_Abbreviation + "_" + "0"
                            + increment + "." + "png";
                    //image compression by bhalla
                    CompressImage.compressImage(imagePath.getAbsolutePath());

                    addressProofFileName = mStorageUtils.saveFileToAppSpecificDir(context, StorageUtils.DIRECT_DIRECTORY,
                            fileName, imagePath);

                    if (addressProofFileName.exists()) {
                        addressProofPhotoBitmap = BitmapFactory.decodeFile(addressProofFileName.getAbsolutePath());
                    }
                    //  boolean isBlurr = Blurr(addressProofBitmap);


                    if (is_Doc_Detected == true) {

                        TVImageAddressProofDetails.setText(addressProofDOCDetails);
                        img_btn_document_upload_click_preview_image_address
                                .setVisibility(View.VISIBLE);
                        img_btn_document_delete_address
                                .setVisibility(View.VISIBLE);

                        if (isBrowseCapture == REQUEST_CODE_PICK_FILE) {
                            img_btn_document_upload_click_image_address
                                    .setImageDrawable(getResources().getDrawable(
                                            R.drawable.ibtn_camera));

                            img_btn_document_upload_click_browse_address.setImageDrawable(getResources().getDrawable(
                                    R.drawable.checkedbrowse));

                        } else {
                            img_btn_document_upload_click_image_address.setImageDrawable(
                                    getResources().getDrawable(R.drawable.checkedcamera));

                            img_btn_document_upload_click_browse_address.setImageDrawable(getResources().getDrawable(
                                    R.drawable.ibtn_browsedoc));
                        }

                    } else {
                        img_btn_document_upload_click_preview_image_address
                                .setVisibility(View.INVISIBLE);
                        img_btn_document_delete_address
                                .setVisibility(View.INVISIBLE);
                        img_btn_document_upload_click_image_address
                                .setImageDrawable(getResources().getDrawable(
                                        R.drawable.ibtn_camera));
                        img_btn_document_upload_click_browse_address.setImageDrawable(getResources().getDrawable(
                                R.drawable.ibtn_browsedoc));

                        if (addressProofFileName.exists()) {
                            addressProofFileName.delete();
                            addressProofFileName = null;
                        }
                        Toast.makeText(this, "Document is not properly detected,Kindly capture the actual document. ", Toast.LENGTH_LONG)
                                .show();

                    }

                }
                else if (Check.equalsIgnoreCase("OtherProof")) {

                    // OtherProofFileName = galleryAddPic();
                    increment = 5;
                    String str_Doc_Abbreviation_other = "OTHER";
                    String fileName = policyNumber + "_" + str_Doc_Abbreviation_other + "_" + "0"
                            + increment + "." + "png";
                    //image compression by bhalla
                    CompressImage.compressImage(imagePath.getAbsolutePath());
                    otherProofFileName = mStorageUtils.saveFileToAppSpecificDir(context,
                            StorageUtils.DIRECT_DIRECTORY, fileName, imagePath);
                    if (otherProofFileName.exists()) {
                        otherPhotoBitmap = BitmapFactory.decodeFile(otherProofFileName.getAbsolutePath());
                    }
                    //copy  FILE
                    boolean is_Doc_Detected;
                    otherDocDetailsString = proofDetails;
                    if (edt_document_upload_document_others.toString().toLowerCase().contains("aadhar card")
                            || edt_document_upload_document_others.toString().toLowerCase().contains("pancard")
                            || edt_document_upload_document_others.toString().toLowerCase().contains("passport")
                            || edt_document_upload_document_others.toString().toLowerCase().contains("voter s identity card")
                            || edt_document_upload_document_others.toString().toLowerCase().contains("driving license")) {
                        if (edt_document_upload_document_others.toString().toLowerCase().contains("aadhar") && (DocumentType.equalsIgnoreCase("AADHAAR CARD") || DocumentType.equalsIgnoreCase("BACK AADHAAR CARD") || DocumentType.equalsIgnoreCase("FRONT BACK AADHAAR"))) {
                            is_Doc_Detected = true;
                        } else if (edt_document_upload_document_others.toString().toLowerCase().contains("pancard") && DocumentType.equalsIgnoreCase("PAN")) {
                            is_Doc_Detected = true;
                        } else if (edt_document_upload_document_others.toString().toLowerCase().contains("passport") && (DocumentType.equalsIgnoreCase("PASSPORT") || DocumentType.equalsIgnoreCase("PASSPORT BACK") || DocumentType.equalsIgnoreCase("FRONT BACK PASSPORT"))) {
                            is_Doc_Detected = true;
                        } else if (edt_document_upload_document_others.toString().toLowerCase().contains("voter s identity card") && (DocumentType.equalsIgnoreCase("VOTER ID") || DocumentType.equalsIgnoreCase("BACK VOTER ID") || DocumentType.equalsIgnoreCase("FRONT BACK VOTER"))) {
                            is_Doc_Detected = true;
                        } else if (edt_document_upload_document_others.toString().toLowerCase().contains("driving license") && (DocumentType.equalsIgnoreCase("DRIVING LICENCE") || DocumentType.equalsIgnoreCase("BACK DRIVING LICENCE") || DocumentType.equalsIgnoreCase("FRONT BACK DRIVING LICENCE"))) {
                            is_Doc_Detected = true;
                        } else {
                            is_Doc_Detected = false;
                        }
                    } else {
                        otherDocDetailsString = edt_document_upload_document_others.getText().toString();
                        is_Doc_Detected = true;
                    }


                    if (is_Doc_Detected == true) {

                        TVImageOtherProofDetails.setText(otherDocDetailsString);
                        img_btn_document_upload_click_preview_image_others
                                .setVisibility(View.VISIBLE);
                        img_btn_document_delete_Others
                                .setVisibility(View.VISIBLE);

                        if (isBrowseCapture == REQUEST_CODE_PICK_FILE) {
                            img_btn_document_upload_click_image_others
                                    .setImageDrawable(getResources().getDrawable(
                                            R.drawable.ibtn_camera));

                            img_btn_document_upload_click_browse_Others.setImageDrawable(getResources().getDrawable(
                                    R.drawable.checkedbrowse));

                        } else {
                            img_btn_document_upload_click_image_others.setImageDrawable(
                                    getResources().getDrawable(R.drawable.checkedcamera));

                            img_btn_document_upload_click_browse_Others.setImageDrawable(getResources().getDrawable(
                                    R.drawable.ibtn_browsedoc));
                        }

                    } else {
                        img_btn_document_upload_click_preview_image_others
                                .setVisibility(View.INVISIBLE);
                        img_btn_document_delete_Others
                                .setVisibility(View.INVISIBLE);
                        img_btn_document_upload_click_image_others
                                .setImageDrawable(getResources().getDrawable(
                                        R.drawable.ibtn_camera));
                        img_btn_document_upload_click_browse_Others.setImageDrawable(getResources().getDrawable(
                                R.drawable.ibtn_browsedoc));

                        if (otherProofFileName.exists()) {
                            otherProofFileName.delete();
                            otherProofFileName = null;
                        }
                        Toast.makeText(this, "Document is not properly detected,Kindly capture the actual document. ", Toast.LENGTH_LONG)
                                .show();

                    }

                    commonMethods.setFocusable(edt_document_upload_document_others);
                    edt_document_upload_document_others.requestFocus();
                }
                else if (Check.equals("AgentPhoto")) {
                    String textPrint = " Latitude: " + mLatLng.latitude + ", Longitude: " + mLatLng.longitude + "\n";
                    //image compression by bhalla
                    CompressImage.compressImage(imagePath.getAbsolutePath());
                    destinationFile = mStorageUtils.saveFileToAppSpecificDir(context, StorageUtils.DIRECT_DIRECTORY,
                            str_selected_urn_no + "_cust2Photo.jpg", imagePath);
                    agentPhotoBitmap = commonMethods.mergeBitmap(commonMethods.
                                    getBitmap(imagePath.getAbsolutePath()),
                            commonMethods.convertStringToBitMap(context, textPrint));

                    commonMethods.storeImage(context, agentPhotoBitmap, imagePath.getAbsolutePath());

                    ibAgentTypeUploadPhoto.setImageBitmap(agentPhotoBitmap);
                    commonMethods.setFocusable(ibAgentTypeUploadPhoto);
                    ibAgentTypeUploadPhoto.requestFocus();

                    agentPhotoFileName = imagePath;
                    commonMethods.setFocusable(ibAgentTypeUploadPhoto);
                    ibAgentTypeUploadPhoto.requestFocus();

                }
                else if (Check.equals("UserPhoto")) {
                    String textPrint = " Latitude: " + mLatLng.latitude + ", Longitude: " + mLatLng.longitude + "\n";
                    //image compression by bhalla
                    CompressImage.compressImage(imagePath.getAbsolutePath());
                    destinationFile = mStorageUtils.saveFileToAppSpecificDir(context, StorageUtils.DIRECT_DIRECTORY,
                            str_selected_urn_no + "_cust1Photo.jpg", imagePath);
                    userPhotoBitmap = commonMethods.mergeBitmap(commonMethods.
                                    getBitmap(imagePath.getAbsolutePath()),
                            commonMethods.convertStringToBitMap(context, textPrint));

                    commonMethods.storeImage(context, userPhotoBitmap, imagePath.getAbsolutePath());

                        /*imagePhotoGraphGPSCord.setImageBitmap(userPhotoBitmap);
                        commonMethods.setFocusable(imagePhotoGraphGPSCord);
                        imagePhotoGraphGPSCord.requestFocus();

                        userPhotoFileName = imagePath;
                        commonMethods.setFocusable(imagePhotoGraphGPSCord);
                        imagePhotoGraphGPSCord.requestFocus();*/
                    // String stringToParse = "Address: " + commonMethods.getCompleteAddressString(context, mLatLng.latitude, mLatLng.longitude);

                           /* StringBuffer sb = new StringBuffer(stringToParse);

                            int i = 0;
                            while ((i = sb.indexOf(" ", i + 25)) != -1) {
                                sb.replace(i, i + 1, "\n");
                            }

                                textPrint += "\n" + commonMethods.GetUserType(context) + " Name: " + commonMethods.getUserName(context)
                                        + ", " + commonMethods.GetUserType(context) + " ID: " + strCIFBDMUserId
                                        + "\n" + sb.toString()
                                        + "Date: " + commonMethods.getCurrentTimeStamp();*/

                }

            } else {
                Toast.makeText(context, "Data not receive", Toast.LENGTH_SHORT).show();
            }
        }
        else if (requestCode == REQUEST_CODE_PICK_FILE) {
            if (resultCode == AppCompatActivity.RESULT_OK) {

                try {

                    if (Check.equals("UserPhoto")) {
                        //File destinationFile = commonMethods.createCaptureImg( "_cust1Photo.jpg");
                        String textPrint = " Latitude: " + mLatLng.latitude + ", Longitude: " + mLatLng.longitude + "\n"
                                + "Date : " + commonMethods.getCurrentMonthDate() + "\n";
                        //image compression by bhalla
                        CompressImage.compressImage(customerPhotoFileName.getAbsolutePath());
                        userPhotoBitmap = commonMethods.mergeBitmap(commonMethods.
                                        getBitmap(customerPhotoFileName.getAbsolutePath()),
                                commonMethods.convertStringToBitMap(context, textPrint));

                        commonMethods.storeImage(context, userPhotoBitmap, customerPhotoFileName.getAbsolutePath());

                        ivCustomerPhotoGraphGPSCord.setImageBitmap(userPhotoBitmap);
                        commonMethods.setFocusable(ivCustomerPhotoGraphGPSCord);
                        ivCustomerPhotoGraphGPSCord.requestFocus();

                    } else if (Check.equals("AgentPhoto")) {
                        String textPrint = " Latitude: " + mLatLng.latitude + ", Longitude: " + mLatLng.longitude + "\n"
                                + "Date : " + commonMethods.getCurrentMonthDate() + "\n" +
                                "Name : " + userName + ",\n Code - " + strCIFBDMUserId + "\n";
                        CompressImage.compressImage(agentPhotoFileName.getAbsolutePath());

                        agentPhotoBitmap = commonMethods.mergeBitmap(commonMethods.
                                        getBitmap(agentPhotoFileName.getAbsolutePath()),
                                commonMethods.convertStringToBitMap(context, textPrint));

                        commonMethods.storeImage(context, agentPhotoBitmap, agentPhotoFileName.getAbsolutePath());

                        ibAgentTypeUploadPhoto.setImageBitmap(agentPhotoBitmap);
                        commonMethods.setFocusable(ibAgentTypeUploadPhoto);
                        ibAgentTypeUploadPhoto.requestFocus();

                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                    //commonMethods.showMessageDialog(context,ex.getMessage());
                }
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.spinnerTransNatureAllowed:
                transactionNature = spinnerTransNatureAllowed.getSelectedItem().toString();
                if (position > 0) {
                    if (transactionNature.equalsIgnoreCase("Non Payout Request")) {
                        llNonPayoutRequest.setVisibility(View.VISIBLE);
                        llPayoutRequest.setVisibility(View.GONE);
                        llDocumentsStatements.setVisibility(View.GONE);
                    } else if (transactionNature.equalsIgnoreCase("Payout Request")) {
                        llNonPayoutRequest.setVisibility(View.GONE);
                        llPayoutRequest.setVisibility(View.VISIBLE);
                        llDocumentsStatements.setVisibility(View.GONE);
                    } else if (transactionNature.equalsIgnoreCase("Documents and Statements")) {
                        llNonPayoutRequest.setVisibility(View.GONE);
                        llPayoutRequest.setVisibility(View.GONE);
                        llDocumentsStatements.setVisibility(View.VISIBLE);
                    }
                } else {
                    llNonPayoutRequest.setVisibility(View.GONE);
                    llPayoutRequest.setVisibility(View.GONE);
                    llDocumentsStatements.setVisibility(View.GONE);
                }
                break;
            case R.id.spinnerNonPayoutRequest:
                nonPayoutRequest = spinnerNonPayoutRequest.getSelectedItem().toString();
                if (position > 0) {
                    llDateOfBirth.setVisibility(View.GONE);
                    llAnnuityNumberCoe.setVisibility(View.GONE);
                    llButtonOK.setVisibility(View.GONE);
                    llOTP.setVisibility(View.GONE);
                    llPolicyCustomerDetails.setVisibility(View.GONE);
                    llKYCUploadDoc.setVisibility(View.VISIBLE);

                    if (nonPayoutRequest.equalsIgnoreCase("Declaration of Good Health (DGH)")) {
                        llOTP.setVisibility(View.VISIBLE);
                    } else if (nonPayoutRequest.equalsIgnoreCase("Email id update") ||
                            nonPayoutRequest.equalsIgnoreCase("PAN number update") ||
                            nonPayoutRequest.equalsIgnoreCase("Mobile number update")) {
                        llButtonOK.setVisibility(View.VISIBLE);
                        llDateOfBirth.setVisibility(View.VISIBLE);
                    } else if (nonPayoutRequest.equalsIgnoreCase("Certificate of Existence for annuity holders")) {
                        llButtonOK.setVisibility(View.VISIBLE);
                    } else if (nonPayoutRequest.equalsIgnoreCase("Hit Date Change")
                            || nonPayoutRequest.equalsIgnoreCase("Fund Redirection")
                            || nonPayoutRequest.equalsIgnoreCase("Name Correction")
                            || nonPayoutRequest.equalsIgnoreCase("Premium Holiday")
                            || nonPayoutRequest.equalsIgnoreCase("Frequency Change")) {
                        llOTP.setVisibility(View.VISIBLE);
                    } else if (nonPayoutRequest.equalsIgnoreCase("Fund Switch")
                            || nonPayoutRequest.equalsIgnoreCase("Bank Account update")
                            || nonPayoutRequest.equalsIgnoreCase("Address Change")) {
                        llOTP.setVisibility(View.VISIBLE);
                        llDateOfBirth.setVisibility(View.VISIBLE);
                    }
                }
                break;
            case R.id.spinnerPayoutRequest:
                payoutRequest = spinnerPayoutRequest.getSelectedItem().toString();
                if (position > 0) {
                    llDateOfBirth.setVisibility(View.GONE);
                    llAnnuityNumberCoe.setVisibility(View.GONE);
                    llButtonOK.setVisibility(View.GONE);
                    llOTP.setVisibility(View.GONE);
                    llPolicyCustomerDetails.setVisibility(View.GONE);
                    llKYCUploadDoc.setVisibility(View.VISIBLE);
                    if (payoutRequest.equalsIgnoreCase("Free Look Cancellation")
                            || payoutRequest.equalsIgnoreCase("LTR Refund")) {
                        llOTP.setVisibility(View.VISIBLE);
                    } else if (payoutRequest.equalsIgnoreCase("Partial Withdraw")) {
                        llOTP.setVisibility(View.VISIBLE);
                        llDateOfBirth.setVisibility(View.VISIBLE);
                    }
                }

                break;
            case R.id.spinnerDocumentsStatements:
                llDateOfBirth.setVisibility(View.GONE);
                llAnnuityNumberCoe.setVisibility(View.GONE);
                llButtonOK.setVisibility(View.GONE);
                llPolicyCustomerDetails.setVisibility(View.GONE);

                documentsStatements = spinnerDocumentsStatements.getSelectedItem().toString();
                llOTP.setVisibility(View.VISIBLE);
                llKYCUploadDoc.setVisibility(View.VISIBLE);
                break;

            case R.id.spnr_document_upload_document_identity:
                if (spnr_document_upload_document_identity.getSelectedItem().toString().toLowerCase().contains("aadhar card")) {
                    is1WProduct = false;
                    // is1WProductEKYC = false;
                    llIdentityAadhaarScan.setVisibility(View.VISIBLE);
                } else {
                    llIdentityAadhaarScan.setVisibility(View.VISIBLE);
                }
                break;

            case R.id.spnr_document_upload_document_address:

                if (spnr_document_upload_document_address.getSelectedItem().toString().toLowerCase().contains("aadhar card")) {
                    is1WProduct = false;
                    //is1WProductEKYC = false;
                    llAddressAadhaarScan.setVisibility(View.VISIBLE);
                } else {
                    llAddressAadhaarScan.setVisibility(View.VISIBLE);
                }

                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }


    class GetCustomerDetailsAsync extends AsyncTask<String, String, String> {
        private final String METHOD_NAME_CUST_DETAILS = "getCustomer_det";
        private volatile boolean running = true;
        private String status = "", proposalNumber;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog.show();
        }

        @SuppressLint("SimpleDateFormat")
        @Override
        protected String doInBackground(String... aurl) {
            try {

                running = true;
                SoapObject request;

                request = new SoapObject(NAMESPACE, METHOD_NAME_CUST_DETAILS);

                //getCustomer_det(string strPolicyNo, string strDOB)
                request.addProperty("strPolicyNo", policyNumber);
                request.addProperty("strDOB", dob);

                System.out.println("request:" + request.toString());
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);
                commonMethods.TLSv12Enable();

                String URlProd = "https://sbilposservices.sbilife.co.in/service.asmx?wsdl";
                HttpTransportSE androidHttpTranport = new HttpTransportSE(URlProd);
                String SOAP_ACTION = "http://tempuri.org/" + METHOD_NAME_CUST_DETAILS;
                androidHttpTranport.call(SOAP_ACTION, envelope);
                Object response = envelope.getResponse();

                if (!response.toString().contentEquals("<NewDataSet />")) {
                    SoapPrimitive sa;
                    sa = (SoapPrimitive) envelope.getResponse();

                    String inputpolicylist = sa.toString();

                    ParseXML prsObj = new ParseXML();

                    inputpolicylist = prsObj.parseXmlTag(
                            inputpolicylist, "PolicyDetails");
                    status = prsObj.parseXmlTag(
                            inputpolicylist, "ScreenData");

                    if (status == null) {
                        List<String> Node = prsObj.parseParentNode(
                                inputpolicylist, "Table");
                        PL_PERSON_ID = prsObj.parseXmlTag(inputpolicylist, "PL_PERSON_ID");
                        PL_PERSON_ID = PL_PERSON_ID == null ? "" : PL_PERSON_ID;

                        PR_MOBILE = prsObj.parseXmlTag(inputpolicylist, "PR_MOBILE");
                        PR_MOBILE = PR_MOBILE == null ? "" : PR_MOBILE;

                        PR_FULL_NM = prsObj.parseXmlTag(inputpolicylist, "PR_FULL_NM");
                        PR_FULL_NM = PR_FULL_NM == null ? "" : PR_FULL_NM;

                        PR_GENDER = prsObj.parseXmlTag(inputpolicylist, "PR_GENDER");
                        PR_GENDER = PR_GENDER == null ? "" : PR_GENDER;

                        PR_EMAIL = prsObj.parseXmlTag(inputpolicylist, "PR_EMAIL");
                        PR_EMAIL = PR_EMAIL == null ? "" : PR_EMAIL;

                        HOLDERPANNUMBER = prsObj.parseXmlTag(inputpolicylist, "HOLDERPANNUMBER");
                        HOLDERPANNUMBER = HOLDERPANNUMBER == null ? "" : HOLDERPANNUMBER;


                        status = "Success";
                    }

                } else {
                    running = false;
                }
            } catch (Exception e) {
                running = false;
            }
            return null;

        }


        @Override
        protected void onPostExecute(String unused) {
            if (mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
            llPolicyCustomerDetails.setVisibility(View.GONE);
            if (running) {
                if (status.equalsIgnoreCase("Success")) {
                    LinearLayout llEmailIdCustDetails = findViewById(R.id.llEmailIdCustDetails);
                    LinearLayout llPANNoCustDetails = findViewById(R.id.llPANNoCustDetails);
                    LinearLayout llMobileCustDetails = findViewById(R.id.llMobileCustDetails);
                    llPolicyCustomerDetails.setVisibility(View.VISIBLE);
                    llPANNoCustDetails.setVisibility(View.GONE);
                    llEmailIdCustDetails.setVisibility(View.GONE);
                    llMobileCustDetails.setVisibility(View.GONE);
                    llOTP.setVisibility(View.GONE);
                    llDateOfBirth.setVisibility(View.GONE);

                    if (nonPayoutRequest.equalsIgnoreCase("Email id update")) {
                        llDateOfBirth.setVisibility(View.VISIBLE);
                        llEmailIdCustDetails.setVisibility(View.VISIBLE);

                        TextView tvEmailId = findViewById(R.id.tvEmailId);
                        tvEmailId.setText(PR_EMAIL);

                        commonMethods.setFocusable(tvEmailId);
                        tvEmailId.requestFocus();
                    } else if (nonPayoutRequest.equalsIgnoreCase("PAN number update")) {
                        llDateOfBirth.setVisibility(View.VISIBLE);
                        TextView tvPANCustDetails = findViewById(R.id.tvPANCustDetails);
                        tvPANCustDetails.setText(HOLDERPANNUMBER);

                        llPANNoCustDetails.setVisibility(View.VISIBLE);
                        commonMethods.setFocusable(tvPANCustDetails);
                        tvPANCustDetails.requestFocus();
                    } else if (nonPayoutRequest.equalsIgnoreCase("Mobile number update")) {
                        llDateOfBirth.setVisibility(View.VISIBLE);
                        llMobileCustDetails.setVisibility(View.VISIBLE);
                        tvMobileNo.setText(PR_MOBILE);
                    }

                } else {
                    commonMethods.showMessageDialog(context, "No record Found");
                }
            } else {
                commonMethods.showMessageDialog(context, "No record Found");
            }
        }
    }


    class GenerateOTP_EasyAccess_EMAILAsync extends AsyncTask<String, String, String> {
        private final String METHOD_NAME_GENRATE_EASY_Acess_EMAIL = "GenerateOTP_EasyAccess_EMAIL";
        private volatile boolean running = true;
        private String status = "", proposalNumber;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog.show();
        }

        @SuppressLint("SimpleDateFormat")
        @Override
        protected String doInBackground(String... aurl) {
            try {

                running = true;
                SoapObject request;

                request = new SoapObject(NAMESPACE, METHOD_NAME_GENRATE_EASY_Acess_EMAIL);

                //GenerateOTP_EasyAccess_EMAIL(string MOBILE_NO, string EMAIL)
                // request.addProperty("MOBILE_NO", PR_MOBILE);
                request.addProperty("MOBILE_NO", PR_MOBILE);
                request.addProperty("EMAIL", newEmaild);

                System.out.println("request:" + request.toString());
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);
                commonMethods.TLSv12Enable();

                String URlProd = "https://sbilposservices.sbilife.co.in/service.asmx?wsdl";
                //String URlProd = ServiceURL.SERVICE_URL;
                HttpTransportSE androidHttpTranport = new HttpTransportSE(URlProd);
                String SOAP_ACTION = "http://tempuri.org/" + METHOD_NAME_GENRATE_EASY_Acess_EMAIL;
                androidHttpTranport.call(SOAP_ACTION, envelope);
                Object response = envelope.getResponse();

                if (!response.toString().contentEquals("<NewDataSet />")) {
                    SoapPrimitive sa;
                    sa = (SoapPrimitive) envelope.getResponse();

                    String inputpolicylist = sa.toString();
                    if (inputpolicylist.equalsIgnoreCase("1")) {
                        status = "Success";
                    } else {
                        status = "0";
                    }


                } else {
                    running = false;
                }
            } catch (Exception e) {
                running = false;
            }
            return null;

        }


        @Override
        protected void onPostExecute(String unused) {
            if (mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
            llPolicyCustomerDetails.setVisibility(View.GONE);
            if (running) {
                if (status.equalsIgnoreCase("Success")) {
                    commonMethods.showMessageDialog(context, "OTP Sent Successfully on Email");
                    llPolicyCustomerDetails.setVisibility(View.VISIBLE);
                    etEmailOTP.setVisibility(View.VISIBLE);
                    buttonValidateEmailOTP.setVisibility(View.VISIBLE);
                    buttonGenerateEmailOTP.setVisibility(View.GONE);
                    buttonValidateEmailOTP.setFocusable(true);
                } else {
                    commonMethods.showMessageDialog(context, "OTP Sending on Email Failed");
                }
            } else {
                commonMethods.showMessageDialog(context, "OTP Sending on Email Failed");
            }
        }
    }

    class SaveCustProfileChreqAsync extends AsyncTask<String, String, String> {
        private final String METHOD_NAME_SAVE_CUST_PROFILE = "saveCustProfileChreq";
        private volatile boolean running = true;
        private String status = "", strPrType, emailPANValue;

        SaveCustProfileChreqAsync(String strPrType, String emailPANValue) {
            this.strPrType = strPrType;
            this.emailPANValue = emailPANValue;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog.show();
        }

        @SuppressLint("SimpleDateFormat")
        @Override
        protected String doInBackground(String... aurl) {
            try {

                running = true;
                SoapObject request;

                request = new SoapObject(NAMESPACE, METHOD_NAME_SAVE_CUST_PROFILE);

                //<strPrType>${strPrType}</strPrType>` +
                // `<PR_VALUE>${emailId}</PR_VALUE><strPolicynum>${policyNumber}</strPolicynum>`
                request.addProperty("strPrType", strPrType);
                request.addProperty("PR_VALUE", emailPANValue);
                request.addProperty("strPolicynum", policyNumber);
                request.addProperty("strAuthKey", commonMethods.getEncryptedAuthKey());

                System.out.println("request:" + request.toString());
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);
                commonMethods.TLSv12Enable();


                HttpTransportSE androidHttpTranport = new HttpTransportSE(URl);
                String SOAP_ACTION = "http://tempuri.org/" + METHOD_NAME_SAVE_CUST_PROFILE;
                androidHttpTranport.call(SOAP_ACTION, envelope);
                Object response = envelope.getResponse();

                if (!response.toString().contentEquals("<NewDataSet />")) {
                    SoapPrimitive sa;
                    sa = (SoapPrimitive) envelope.getResponse();

                    String inputpolicylist = sa.toString();
                    System.out.println("request:" + inputpolicylist);
                    if (inputpolicylist.equalsIgnoreCase("1")) {
                        status = "Success";
                    } else {
                        status = "0";
                    }


                } else {
                    running = false;
                }
            } catch (Exception e) {
                running = false;
            }
            return null;

        }


        @Override
        protected void onPostExecute(String unused) {
            if (mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
            llPolicyCustomerDetails.setVisibility(View.GONE);
            if (running) {
                if (status.equalsIgnoreCase("Success")) {
                    tvMobileNo.setText("");
                    etMobileNo.setText("");
                    etConfirmMobileNo.setText("");
                    etConfirmMobileNo.setText("");
                    etMobileOTP.setVisibility(View.GONE);
                    buttonGenerateMobileOTP.setVisibility(View.VISIBLE);
                    buttonValidateMobileOTP.setVisibility(View.GONE);
                    llPolicyCustomerDetails.setVisibility(View.GONE);

                    requestTypeParivartanService = strPrType;
                    commonMethods.showMessageDialog(context, "Your request for " + requestTypeParivartanService + " change has been accepted,the updation will reflect after 2 hours ");
                   /* getCustomerDetailsParivartanAsync = new GetCustomerDetailsParivartanAsync();
                    getCustomerDetailsParivartanAsync.execute();*/
                } else {
                    commonMethods.showMessageDialog(context, "Failed to save your changes");
                }
            }
        }
    }

    class NameMatchingAsynTask extends AsyncTask<String, String, String> {
        private final String METHOD_NAME_NAME_MATCH = "NameMatching";
        private volatile boolean running = true;
        private String status = "", strPrType, emailPANValue;
        private String nameOne, nameTwo;
        private int matchedPer = 0;

        NameMatchingAsynTask(String strPrType, String emailPANValue, String nameOne, String nameTwo) {
            this.strPrType = strPrType;
            this.emailPANValue = emailPANValue;
            this.nameOne = nameOne;
            this.nameTwo = nameTwo;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog.show();
        }

        @SuppressLint("SimpleDateFormat")
        @Override
        protected String doInBackground(String... aurl) {
            try {

                running = true;
                SoapObject request;

                request = new SoapObject(NAMESPACE, METHOD_NAME_NAME_MATCH);

                //<strPrType>${strPrType}</strPrType>` +
                // `<PR_VALUE>${emailId}</PR_VALUE><strPolicynum>${policyNumber}</strPolicynum>`
                request.addProperty("strName1", nameOne);
                request.addProperty("strName2", nameTwo);
                System.out.println("request = " + request.toString());
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);
                commonMethods.TLSv12Enable();

                String URLStr = "https://sbilposservices.sbilife.co.in/service.asmx?wsdl";
                HttpTransportSE androidHttpTranport = new HttpTransportSE(URLStr);
                String SOAP_ACTION = "http://tempuri.org/" + METHOD_NAME_NAME_MATCH;
                androidHttpTranport.call(SOAP_ACTION, envelope);
                Object response = envelope.getResponse();

                if (!response.toString().contentEquals("<NewDataSet />")) {
                    SoapPrimitive sa;
                    sa = (SoapPrimitive) envelope.getResponse();

                    String inputpolicylist = sa.toString();
                    System.out.println("request:" + inputpolicylist);
                    status = "Success";
                    //{"Output":"90","Error":""}
                    JSONObject jsonObject = new JSONObject(inputpolicylist);
                    String Output = jsonObject.getString("Output");
                    matchedPer = Integer.parseInt(Output);
                } else {
                    running = false;
                }
            } catch (Exception e) {
                e.printStackTrace();
                running = false;
            }
            return null;

        }


        @Override
        protected void onPostExecute(String unused) {
            if (mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
            llPolicyCustomerDetails.setVisibility(View.GONE);
            if (running) {
                if (status.equalsIgnoreCase("Success")) {
                    if (matchedPer >= 85) {
                        saveCustProfileChreqAsync = new SaveCustProfileChreqAsync("Pan", newPANCard);
                        saveCustProfileChreqAsync.execute();
                    } else {
                        commonMethods.showMessageDialog(context, "Name is not matching.");
                    }

                } else {
                    commonMethods.showMessageDialog(context, "Failed to save your changes");
                }
            }
        }
    }

    private void updateDisplay(int year, int month, int day) {

        String y = String.valueOf(year);
        String m = String.valueOf(month + 1);
        String da = String.valueOf(day);
        // String totaldate = m + "-" + da + "-" + y;

        if (m.contentEquals("1")) {
            m = "January";

        } else if (m.contentEquals("2")) {
            m = "February";

        } else if (m.contentEquals("3")) {
            m = "March";

        } else if (m.contentEquals("4")) {
            m = "April";

        } else if (m.contentEquals("5")) {
            m = "May";

        } else if (m.contentEquals("6")) {
            m = "June";

        } else if (m.contentEquals("7")) {
            m = "July";

        } else if (m.contentEquals("8")) {
            m = "August";

        } else if (m.contentEquals("9")) {
            m = "September";

        } else if (m.contentEquals("10")) {
            m = "October";

        } else if (m.contentEquals("11")) {
            m = "November";

        } else if (m.contentEquals("12")) {
            m = "December";

        }

        String totaldate = da + "-" + m + "-" + y;

        if (datecheck == 1) {
            tvDateOfBirth.setText(totaldate);
        }
    }

    public class AsyncCheckPAN extends AsyncTask<String, Void, String> {
        private volatile boolean running = true;
        private String str_output_result = "";
        private final String SOAP_ACTION_CHECK_PAN_NUMBER = "http://tempuri.org/checkPanCardNo_smrt";
        private final String METHOD_NAME_CHECK_PAN_NUMBER = "checkPanCardNo_smrt";

        @Override
        protected void onPreExecute() {
            mProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {

            if (commonMethods.isNetworkConnected(context)) {
                try {
                    running = true;

                    SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME_CHECK_PAN_NUMBER);

                    request.addProperty("strInput", strings[0].toString());
                    System.out.println("request:" + request.toString());
                    commonMethods.TLSv12Enable();

                    SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                            SoapEnvelope.VER11);
                    envelope.dotNet = true;

                    new MarshalBase64().register(envelope); // serialization

                    envelope.setOutputSoapObject(request);
                    String URLStr = "https://sbilposservices.sbilife.co.in/service.asmx?wsdl";
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                            .permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                    HttpTransportSE androidHttpTranport = new HttpTransportSE(URLStr, 50000);
                    try {
                        androidHttpTranport.call(SOAP_ACTION_CHECK_PAN_NUMBER, envelope);
                        Object response = envelope.getResponse();
                        str_output_result = response.toString();
                        System.out.println("request:" + str_output_result);
                        // SoapPrimitive sa = (SoapPrimitive)
                        // envelope.getResponse();
                    } catch (Exception e) {
                        e.printStackTrace();
                        running = false;
                        return commonMethods.WEEK_INTERNET_MESSAGE;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    running = false;
                    return commonMethods.WEEK_INTERNET_MESSAGE;
                }
            } else {
                return commonMethods.NO_INTERNET_MESSAGE;
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

                    etNewPANCard.setText("");
                    newPANCard = "";
                    commonMethods.showMessageDialog(context, "E-PAN DOES NOT EXSIST and INVALID");

                } else if (str_output_result.equalsIgnoreCase("Service Not Availiable Please try after some time")
                        || str_output_result.equalsIgnoreCase("anyType{}")) {
                    etNewPANCard.setText("");
                    newPANCard = "";
                    commonMethods.showMessageDialog(context, "Service Not Availiable Please try after some time");
                } else {
                    String strOut = prsObj.parseXmlTag(str_output_result, "PANDETAILS");
                    strOut = strOut == null ? "" : strOut;

                    if (strOut.equals("")) {
                        etNewPANCard.setText("");
                        newPANCard = "";
                        commonMethods.showMessageDialog(context, "E-PAN DOES NOT EXSIST and INVALID");

                    } else {

                        String str_ReturnCode = prsObj.parseXmlTag(strOut, "RETURNCODE");
                        str_ReturnCode = str_ReturnCode == null ? "" : str_ReturnCode;

                        String str_pan_status = prsObj.parseXmlTag(strOut, "PANSTATUS");
                        str_pan_status = str_pan_status == null ? "" : str_pan_status;

                        if (str_ReturnCode.equals("1") && str_pan_status.equals("E")) {
                            System.out.println("str_ReturnCode = " + str_ReturnCode);
                            running = false;

                            String namePAN = "";

                            namePAN = prsObj.parseXmlTag(strOut, "TITLE");
                            namePAN = namePAN == null ? "" : namePAN;

                            String FIRSTNAME = prsObj.parseXmlTag(strOut, "FIRSTNAME");
                            FIRSTNAME = FIRSTNAME == null ? "" : FIRSTNAME;
                            namePAN += " " + FIRSTNAME;

                            String MIDDLENAME = prsObj.parseXmlTag(strOut, "MIDDLENAME");
                            MIDDLENAME = MIDDLENAME == null ? "" : MIDDLENAME;
                            namePAN += " " + MIDDLENAME;

                            String LASTNAME = prsObj.parseXmlTag(strOut, "LASTNAME");
                            LASTNAME = LASTNAME == null ? "" : LASTNAME;
                            namePAN += " " + LASTNAME;

                            System.out.println("namePAN = " + namePAN);
                            NameMatchingAsynTask nameMatchingAsynTask = new NameMatchingAsynTask("Pan", newPANCard,
                                    PR_FULL_NM, namePAN);
                            nameMatchingAsynTask.execute();
                            /*if (mPanFile != null) {


                            } else {
                                commonMethods.showMessageDialog(context, "Error While creating PAN card doc!!");
                            }*/

                        } else {
                            etNewPANCard.setText("");
                            newPANCard = "";
                            commonMethods.showMessageDialog(context, "E-PAN DOES NOT EXSIST and INVALID");
                        }
                    }
                }

            } else {
                etNewPANCard.setText("");
                newPANCard = "";
                etPANOTP.setText("");
                Toast.makeText(context, s.toString(),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    class GetCOEDetails extends AsyncTask<String, String, String> {

        private volatile boolean running = true;
        private String policyNo;
        private String annuityNo;
        private String isSuccess = null;

        GetCOEDetails(String policyNo, String annuityNo) {
            this.policyNo = policyNo;
            this.annuityNo = annuityNo;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... aurl) {
            if (commonMethods.isNetworkConnected(context)) {
                try {

                    running = true;
                    policyNo = policyNo.toUpperCase();
                    if (annuityNo != null) {
                        annuityNo = annuityNo.toUpperCase();
                    }
                    SoapObject request = new SoapObject(NAMESPACE, METHOD_GET_COE_DETAILS);
                    request.addProperty("PolicyNo", policyNo);
                    request.addProperty("AnnuityNo", annuityNo);
                    SoapSerializationEnvelope envelope =
                            new SoapSerializationEnvelope(SoapEnvelope.VER11);
                    envelope.dotNet = true;

                    envelope.setOutputSoapObject(request);

                    HttpConnector.getInstance().allowAllSSL();
//
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);

                    HttpTransportSE androidHttpTranport = new HttpTransportSE(URl);

                    androidHttpTranport.call(NAMESPACE + METHOD_GET_COE_DETAILS, envelope);
                    //Object response = envelope.getResponse();

                    SoapPrimitive sa = null;

                    sa = (SoapPrimitive) envelope.getResponse();
                    String result = sa.toString();
                    //System.out.println("Output = " + result);
                    if (result.contains("<NewDataSet>")) {
                        ParseXML prsObj = new ParseXML();
                        result = prsObj.parseXmlTag(result, "NewDataSet");
                        result = prsObj.parseXmlTag(result, "Table");
                        /*annuityPersonDetails = prsObj.parseCOEData(result);
                        annuityPersonDetails.setPolicyNo(policyNo);
                        annuityPersonDetails.setANNUITYNO(annuityNo);*/
                        isSuccess = "1";
                    } else {
                        isSuccess = "0";
                        return result;
                    }


                } catch (Exception e) {
                    mProgressDialog.dismiss();
                    running = false;
                }

            } else
                return "Please Activate Internet connection";

            return null;

        }

        @Override
        protected void onPostExecute(String result) {

            try {
                mProgressDialog.dismiss();
            } catch (Exception e) {
                e.getMessage();
            }
            if (running != false) {
                if (isSuccess == "1") {/*
                    String date = getDifferenceDays(annuityPersonDetails.getCoeEndDate());
                    if (date == null) {
                        if (annuityPersonDetails.getCONTACTMOBILE().equalsIgnoreCase("")) {
                            commonMethods.showMessageDialog(context, "Your Mobile Number is not registered " +
                                    "with SBI-Life.Please visit your nearest branch");
                        } else {
                            annuityPersonDetails.setIsLoggedIn(false);
                            requestTypeParivartanService = "coe";
                            getCustomerDetailsParivartanAsync = new GetCustomerDetailsParivartanAsync();
                            getCustomerDetailsParivartanAsync.execute();
                        }
                    } else {
                        commonMethods.showMessageDialog(context, "Please submit the Certificate after " + date);
                    }*/
                } else {
                    commonMethods.showMessageDialog(context, result);
                }
            } else {
                commonMethods.showMessageDialog(context, "Server Not Responding,Try again..");
            }
        }
    }

    private String getDifferenceDays(String coeEndDate) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
        SimpleDateFormat dateFormat1 = new SimpleDateFormat("dd-MMMM-yyyy");

        try {
            Date date1 = dateFormat.parse(coeEndDate);
            Date date2 = dateFormat.parse(dateFormat.format(new Date()));
            long diff = date1.getTime() - date2.getTime();
            int days = (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
            if (days > 90) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(date1);
                cal.add(Calendar.DATE, -90);
                Date before90Days = cal.getTime();
                return dateFormat1.format(before90Days);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }


    private void setBtnListenerOrDisable(ImageButton btn,
                                         View.OnClickListener mTakePicOnClickListener, String intentName) {
        if (isIntentAvailable(this, intentName)) {
            btn.setOnClickListener(mTakePicOnClickListener);
        } else {
            btn.setTag(getText(R.string.cannot).toString() + " "
                    + btn.getContext());
            btn.setClickable(false);
        }
    }

    private boolean isIntentAvailable(Context context, String action) {
        final PackageManager packageManager = context.getPackageManager();
        final Intent intent = new Intent(action);
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent,
                PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    private final View.OnClickListener IdentityProofOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            policyNumber = edt_upload_all_docs.getText().toString().trim();

            if (!TextUtils.isEmpty(policyNumber)) {
                if (!(spnr_document_upload_document_identity.getSelectedItem().toString()).equalsIgnoreCase("Select Document")) {
                    img_btn_document_upload_click_browse_identity
                            .setImageDrawable(getResources().getDrawable(
                                    R.drawable.ibtn_browsedoc));
                    Check = "IdentityProof";
                    increment = 2;

                    isBrowseCapture = REQUEST_CODE_PICK_PHOTO_FILE;

                    Intent intent = new Intent(context, OcrActivity.class);
                    startActivityForResult(intent, REQUEST_OCR);
                } else
                    commonMethods.showMessageDialog(context, "Please select ID Proof");
            } else {
                commonMethods.showMessageDialog(context, "Please enter Policy Number.");
            }

        }
    };

    private final View.OnClickListener AddressProofOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            policyNumber = edt_upload_all_docs.getText().toString().trim();

            if (!TextUtils.isEmpty(policyNumber)) {
                if (!(spnr_document_upload_document_address.getSelectedItem().toString()).equalsIgnoreCase("Select Document")) {
                    img_btn_document_upload_click_browse_address
                            .setImageDrawable(getResources().getDrawable(
                                    R.drawable.ibtn_browsedoc));
                    Check = "AddressProof";
                    increment = 3;
                    isBrowseCapture = REQUEST_CODE_PICK_PHOTO_FILE;
                    Intent intent = new Intent(context, OcrActivity.class);
                    startActivityForResult(intent, REQUEST_OCR);
                } else {
                    commonMethods.showMessageDialog(context, "Please enter Policy Number.");
                }
            } else
                commonMethods.showMessageDialog(context, "Please enter Policy Number");
        }
    };

    private final View.OnClickListener OthersProofOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            policyNumber = edt_upload_all_docs.getText().toString().trim();

            if (!TextUtils.isEmpty(policyNumber)) {
                if (!TextUtils.isEmpty(edt_document_upload_document_others.getText().toString())) {
                    img_btn_document_upload_click_browse_Others
                            .setImageDrawable(getResources().getDrawable(
                                    R.drawable.ibtn_browsedoc));

                    Check = "OtherProof";
                    isBrowseCapture = REQUEST_CODE_PICK_PHOTO_FILE;
                    increment = 5;
                    //dispatchTakePictureIntent("0");

                    Intent intent = new Intent(context, OcrActivity.class);
                    startActivityForResult(intent, REQUEST_OCR);
                } else
                    commonMethods.showMessageDialog(context, "Please enter Other Proof Name");
            } else {
                commonMethods.showMessageDialog(context, "Please enter Policy Number.");
            }
        }
    };

    private final class OkOnClickListener implements
            DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialog, int which) {


            if (deleteAddressDocument == 1) {

                if (addressProofFileName != null) {
                    if (addressProofFileName.exists()) {

                        addressProofFileName.delete();

                        img_btn_document_upload_click_preview_image_address
                                .setVisibility(View.INVISIBLE);
                        img_btn_document_delete_address
                                .setVisibility(View.INVISIBLE);

                        img_btn_document_upload_click_image_address
                                .setImageDrawable(getResources().getDrawable(
                                        R.drawable.ibtn_camera));
                        img_btn_document_upload_click_browse_address
                                .setImageDrawable(getResources().getDrawable(
                                        R.drawable.ibtn_browsedoc));
                        SelfAttestedDocumentActivity.lst_AddressBitmap.clear();
                        deleteAddressDocument = 0;
                        addressProofFileName = null;
                        TVImageAddressProofDetails.setText("");
                    } else
                        commonMethods.showToast(context, "File Not Found..");
                } else {
                    commonMethods.showToast(context, "please capture or browse document");
                }

            } else if (deleteIdentityDocument == 1) {

                if (identityProofFileName != null) {
                    if (identityProofFileName.exists()) {

                        identityProofFileName.delete();

                        img_btn_document_upload_preview_image_identity
                                .setVisibility(View.INVISIBLE);
                        img_btn_document_delete_identity
                                .setVisibility(View.INVISIBLE);

                        img_btn_document_upload_click_image_identity
                                .setImageDrawable(getResources().getDrawable(
                                        R.drawable.ibtn_camera));
                        img_btn_document_upload_click_browse_identity
                                .setImageDrawable(getResources().getDrawable(
                                        R.drawable.ibtn_browsedoc));
                        deleteIdentityDocument = 0;
                        identityProofFileName = null;
                        tvImageIDProofDetails.setText("");

                    } else
                        commonMethods.showToast(context, "File Not Found..");
                } else {
                    commonMethods.showToast(context, "please capture or browse document");
                }

            } else if (deleteOtherDocument == 1) {

                if (otherProofFileName != null) {
                    if (otherProofFileName.exists()) {

                        otherProofFileName.delete();

                        img_btn_document_upload_click_preview_image_others
                                .setVisibility(View.INVISIBLE);
                        img_btn_document_delete_Others
                                .setVisibility(View.INVISIBLE);
                        img_btn_document_upload_click_image_others
                                .setImageDrawable(getResources().getDrawable(
                                        R.drawable.ibtn_camera));
                        img_btn_document_upload_click_browse_Others
                                .setImageDrawable(getResources().getDrawable(
                                        R.drawable.ibtn_browsedoc));
                        //SelfAttestedDocumentActivity.lst_OtherBitmap.clear();
                        deleteOtherDocument = 0;
                        otherProofFileName = null;
                        TVImageOtherProofDetails.setText("");
                    } else
                        commonMethods.showToast(context, "File Not Found..");
                } else {
                    commonMethods.showToast(context, "please capture or browse document");
                }
            }

        }
    }

    private final class CancelOnClickListener implements
            DialogInterface.OnClickListener {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            if (!(!(deleteAddressDocument == 1)
                    || !(deleteIdentityDocument == 1) || !(deleteOtherDocument == 1))) {
                if (SelfAttestedDocumentActivity.lst_IdentityBitmap.size() >= 1) {
                    int flag_cancel_identity = 1;
                    increment = 2;
                    SelfAttestedDocumentActivity obj = new SelfAttestedDocumentActivity();
                    obj.createDocumentPdf(increment, " IdentityProof",
                            spnr_document_upload_document_identity
                                    .getSelectedItem().toString(),
                            SelfAttestedDocumentActivity.lst_IdentityBitmap, "");
                }

                if (SelfAttestedDocumentActivity.lst_AddressBitmap.size() >= 1) {
                    int flag_cancel_address = 1;
                    increment = 3;
                    SelfAttestedDocumentActivity obj = new SelfAttestedDocumentActivity();
                    obj.createDocumentPdf(increment, " AddressProof",
                            spnr_document_upload_document_address
                                    .getSelectedItem().toString(),
                            SelfAttestedDocumentActivity.lst_AddressBitmap, "");
                }
                if (SelfAttestedDocumentActivity.lst_OtherBitmap.size() >= 1) {
                    int flag_cancel_others = 1;
                    increment = 5;
                    SelfAttestedDocumentActivity obj = new SelfAttestedDocumentActivity();
                    obj.createDocumentPdf(increment, " OtherProof", "",
                            SelfAttestedDocumentActivity.lst_OtherBitmap, "");
                }


            }
            dialog.dismiss();
        }
    }


    public void onPreview_IdentityProof(View v) {

        if (identityProofFileName.exists()) {
            try {
                commonMethods.openAllDocs(context, identityProofFileName);
            } catch (IOException ex) {
                commonMethods.showToast(context, ex.getMessage());
            }
        } else {
            commonMethods.showToast(context, "File does not exists");
        }
    }

    /*public void onPreviewUserPhoto(View v) {

        if (userPhotoFileName.exists()) {
            try {
                commonMethods.openAllDocs(context, userPhotoFileName);
            } catch (IOException ex) {
                commonMethods.showToast(context, ex.getMessage());
            }
        } else {
            commonMethods.showToast(context, "File does not exists");
        }
    }*/

    public void onDelete_identityProof(View v) {
        deleteIdentityDocument = 1;
        AlertDialog.Builder builder = new AlertDialog.Builder(this,
                AlertDialog.THEME_HOLO_LIGHT);
        builder.setTitle("Confirmation");
        builder.setMessage("do you want to delete this document ?");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", new OkOnClickListener());
        builder.setNegativeButton("No", new CancelOnClickListener());
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    public void onBrowse_IdentityProof(View v) {
        policyNumber = edt_upload_all_docs.getText().toString().trim();

        if (!TextUtils.isEmpty(policyNumber)) {
            if (!(spnr_document_upload_document_identity.getSelectedItem().toString()).equalsIgnoreCase("Select Document")) {
                uploadFlag = 0;
                img_btn_document_upload_click_image_identity
                        .setImageDrawable(getResources().getDrawable(
                                R.drawable.ibtn_camera));
                Check = "IdentityProof";
                isBrowseCapture = REQUEST_CODE_PICK_FILE;

                Intent intent = new Intent(this, OcrActivity.class);
                startActivityForResult(intent, REQUEST_OCR);
            } else {
                commonMethods.showMessageDialog(context, "Please select IDonc Proof");
            }
        } else {
            commonMethods.showMessageDialog(context, "Please enter Policy Number.");
        }

    }

    public void onUpload_IdentityProof1(View v) {
        if (identityProofFileName != null) {
            if (identityProofFileName.exists()) {

                if (!(spnr_document_upload_document_identity.getSelectedItem()
                        .toString()).equalsIgnoreCase("Select Document")) {

                    strUploadDocument = spnr_document_upload_document_identity
                            .getSelectedItem().toString();
                    increment = 2;

                    uploaddoc(identityProofFileName);
                    // DeleteFiles();

                } else {
                    String message = "";
                    if (spnr_document_upload_document_identity
                            .getSelectedItem().toString()
                            .equalsIgnoreCase("Select Document")) {
                        message = "Please Select Identity Document First";
                    } else if (identityProofFileName == null) {
                        message = "Please Capture Or Browse The Identity Proof Document First";
                    }
                    AlertDialogMessage obj = new AlertDialogMessage();
                    obj.dialog(this, message, true);

                }
                // }
            } else {

                // ClearDocumentsPDF();

                Toast.makeText(
                        context,
                        "Please Capture Or Browse The Identity Proof Document First",
                        Toast.LENGTH_SHORT).show();
            }
        } else {

            //ClearDocumentsPDF();

        }
    }


    public void onPreview_AddressProof(View v) {

        if (addressProofFileName.exists()) {
            try {
                commonMethods.openAllDocs(context, addressProofFileName);
            } catch (IOException ex) {
                commonMethods.showToast(context, ex.getMessage());
            }
        } else {
            commonMethods.showToast(context, "File does not exists");
        }
    }

    public void onDelete_addressProof(View v) {
        deleteAddressDocument = 1;
        AlertDialog.Builder builder = new AlertDialog.Builder(this,
                AlertDialog.THEME_HOLO_LIGHT);
        builder.setTitle("Confirmation");
        builder.setMessage("do you want to delete this document ?");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", new OkOnClickListener());
        builder.setNegativeButton("No", new CancelOnClickListener());
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    public void onBrowse_AddressProof(View v) {
        policyNumber = edt_upload_all_docs.getText().toString().trim();

        if (!TextUtils.isEmpty(policyNumber)) {
            if (!(spnr_document_upload_document_address.getSelectedItem().toString()).equalsIgnoreCase("Select Document")) {
                uploadFlag = 0;
                img_btn_document_upload_click_image_address
                        .setImageDrawable(getResources().getDrawable(
                                R.drawable.ibtn_camera));
                Check = "AddressProof";
                isBrowseCapture = REQUEST_CODE_PICK_FILE;

                Intent intent = new Intent(this, OcrActivity.class);
                startActivityForResult(intent, REQUEST_OCR);
            } else
                commonMethods.showMessageDialog(context, "Please select Address Proof");
        } else {
            commonMethods.showMessageDialog(context, "Please enter Policy Number.");
        }
    }


    public void onUpload_AddressProof1(View v) {
        if (addressProofFileName != null) {
            if (addressProofFileName.exists()) {

                if (!(spnr_document_upload_document_address.getSelectedItem()
                        .toString()).equalsIgnoreCase("Select Document")) {

                    strUploadDocument = spnr_document_upload_document_address
                            .getSelectedItem().toString();
                    increment = 3;

                    uploaddoc(addressProofFileName);
                    // DeleteFiles();

                } else {
                    String message = "";
                    if (spnr_document_upload_document_address.getSelectedItem()
                            .toString().equalsIgnoreCase("Select Document")) {
                        message = "Please Select Address Document First";
                    } else if (addressProofFileName == null) {
                        message = "Please Capture Or Browse The Address Proof Document First";
                    }
                    AlertDialogMessage obj = new AlertDialogMessage();
                    obj.dialog(this, message, true);
                }
            } else {
                // ClearDocumentsPDF();
                Toast.makeText(context, "Please Capture Or Browse The Address Proof Document First", Toast.LENGTH_SHORT).show();
            }
        } else {

            // ClearDocumentsPDF();
        }
    }


    public void onPreview_OthersProof(View v) {

        if (otherProofFileName.exists()) {
            try {
                commonMethods.openAllDocs(context, otherProofFileName);
            } catch (IOException ex) {
                commonMethods.showToast(context, ex.getMessage());
            }
        } else {
            commonMethods.showToast(context, "File does not exists");
        }
    }

    public void onDelete_othersProof(View v) {
        deleteOtherDocument = 1;
        AlertDialog.Builder builder = new AlertDialog.Builder(this,
                AlertDialog.THEME_HOLO_LIGHT);
        builder.setTitle("Confirmation");
        builder.setMessage("do you want to delete this document ?");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", new OkOnClickListener());
        builder.setNegativeButton("No", new CancelOnClickListener());
        AlertDialog dialog = builder.create();
        dialog.show();

    }


    public void onBrowse_OtherProof(View v) {
        policyNumber = edt_upload_all_docs.getText().toString().trim();

        if (!TextUtils.isEmpty(policyNumber)) {
            if (!TextUtils.isEmpty(edt_document_upload_document_others.getText().toString())) {
                uploadFlag = 0;
                img_btn_document_upload_click_image_others
                        .setImageDrawable(getResources().getDrawable(
                                R.drawable.ibtn_camera));
                Check = "OtherProof";
                isBrowseCapture = REQUEST_CODE_PICK_FILE;
                increment = 5;

                Intent intent = new Intent(this, OcrActivity.class);
                startActivityForResult(intent, REQUEST_OCR);
            } else
                commonMethods.showMessageDialog(context, "Please enter Other Proof Name");
        } else {
            commonMethods.showMessageDialog(context, "Please enter Policy Number.");
        }
        //onBrowse();
    }

    public void onUpload_OthersProof1(View v) {
        if (otherProofFileName != null) {

            if (otherProofFileName.exists()) {

                if (!(edt_document_upload_document_others.getText().toString()
                        .equalsIgnoreCase(""))) {

                    strUploadDocument = edt_document_upload_document_others
                            .getText().toString();
                    increment = 5;
                    uploaddoc(otherProofFileName); // DeleteFiles();
                } else {
                    String message = "";
                    if (edt_document_upload_document_others.getText()
                            .toString().equalsIgnoreCase("")) {
                        message = "Please enter Other Document First";
                    } else if (otherProofFileName == null) {
                        message = "Please Capture Or Browse The Other Proof Document First";
                    }
                    AlertDialogMessage obj = new AlertDialogMessage();
                    obj.dialog(this, message, true);
                }
            }

        }

    }

    private void uploaddoc(File fileName) {

        FileInputStream fin = null;
        try {
            String str_extension = fileName.getAbsolutePath().substring(fileName.getAbsolutePath().lastIndexOf("."));

            fin = new FileInputStream(fileName);

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            int bytesRead = 0;
            try {
                assert fin != null;
                while ((bytesRead = fin.read(b)) != -1) {
                    bos.write(b, 0, bytesRead);
                }

                byte[] bytes = bos.toByteArray();
                bos.flush();
                bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }
    }

    class UploadPDFService extends AsyncTask<String, String, String> {
        int flag = 0;
        private volatile boolean running = true;
        private final String fileTag;

        public UploadPDFService(String fileTag) {
            this.fileTag = fileTag;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            if (mProgressDialog != null) {
                mProgressDialog.show();
            }
        }

        @Override
        protected String doInBackground(String... aurl) {

            try {

                running = true;
                //UploadFilePayout(byte[] f, string fileName, string policyno, string filetype,string strAuthkey)
                //fileName, string policyno, string filetype)
                String METHOD_NAME = "UploadFilePayout";
                String NAMESPACE = "http://tempuri.org/";
                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
                byte[] BI_bytes;

                if (fileTag.equalsIgnoreCase("IDProof")) {
                    BI_bytes = commonMethods.read(identityProofFileName);

                    //BI_bytes = commonMethods.convertFileToByteArray(context, identityProofFileName);
                    request.addProperty("filetype", str_Doc_Abbreviation_identity);
                    request.addProperty("fileName", identityProofFileName.getName());
                } else if (fileTag.equalsIgnoreCase("custPhoto")) {
                    BI_bytes = commonMethods.read(customerPhotoFileName);
                    //BI_bytes = commonMethods.convertFileToByteArray(context, customerPhotoFileName);
                    request.addProperty("filetype", "CustPhoto");
                    request.addProperty("fileName", customerPhotoFileName.getName());
                } else if (fileTag.equalsIgnoreCase("Other")) {
                    BI_bytes = commonMethods.read(otherProofFileName);
                    //BI_bytes = commonMethods.convertFileToByteArray(context, otherProofFileName);
                    request.addProperty("filetype", "Other");
                    request.addProperty("fileName", otherProofFileName.getName());
                } else {
                    BI_bytes = commonMethods.read(addressProofFileName);
                    //BI_bytes = commonMethods.convertFileToByteArray(context, addressProofFileName);
                    request.addProperty("filetype", str_Doc_Abbreviation);
                    request.addProperty("fileName", addressProofFileName.getName());
                }
                request.addProperty("f", org.kobjects.base64.Base64.encode(BI_bytes));
                request.addProperty("policyno", policyNumber);
                request.addProperty("strAuthkey", commonMethods.getEncryptedAuthKey());

                System.out.println("request = " + request.toString());
                //request.addProperty("f", BI_bytes);

                commonMethods.TLSv12Enable();
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);
                envelope.dotNet = true;
                new MarshalBase64().register(envelope); // serialization
                envelope.setOutputSoapObject(request);


                //Log.d("Upload", "doInBackground: " + request.toString());
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);

                HttpTransportSE androidHttpTranport = new HttpTransportSE(URl);
                androidHttpTranport.call(NAMESPACE + METHOD_NAME, envelope);
                SoapPrimitive sa = null;

                sa = (SoapPrimitive) envelope.getResponse();

                String inputpolicylist = sa.toString();
                System.out.println("inputpolicylist = " + inputpolicylist);
                if (inputpolicylist.equalsIgnoreCase("1"))
                    flag = 1;
                else
                    flag = 0;
                //////////////////////////////////////////////////////////

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;

        }

        @Override
        protected void onPostExecute(String unused) {
            if (mProgressDialog.isShowing())
                mProgressDialog.dismiss();

            if (running) {
                if (flag == 1) {
                    if (fileTag.equalsIgnoreCase("IDProof")) {
                        uploadPDFService = new UploadPDFService("custPhoto");
                        uploadPDFService.execute();
                    } else if (fileTag.equalsIgnoreCase("custPhoto")) {
                        if (otherProofFileName != null) {
                            uploadPDFService = new UploadPDFService("Other");
                            uploadPDFService.execute();
                        } else {
                            uploadPDFService = new UploadPDFService("AddressProof");
                            uploadPDFService.execute();
                        }
                    } else if (fileTag.equalsIgnoreCase("Other")) {
                        uploadPDFService = new UploadPDFService("AddressProof");
                        uploadPDFService.execute();
                    } else {

                        if (nonPayoutRequest.equalsIgnoreCase("Email id update")) {
                            //Done
                            saveCustProfileChreqAsync = new SaveCustProfileChreqAsync("Email", newEmaild);
                            saveCustProfileChreqAsync.execute();
                        } else if (nonPayoutRequest.equalsIgnoreCase("Mobile number update")) {
                            //Done
                            saveCustProfileChreqAsync = new SaveCustProfileChreqAsync("Mobile", newMobileNo);
                            saveCustProfileChreqAsync.execute();
                        } else if (nonPayoutRequest.equalsIgnoreCase("PAN number update")) {
                            //Done
                            if (validate_pan_card) {
                                newPANCard = etNewPANCard.getText().toString();
                                Random r = new Random(System.currentTimeMillis());

                                //call PAN service to get Name of PAN card holder
                                String strPANInput = "<PANDETAILS>"
                                        + "<MODULENAME>" + "CMS" + "</MODULENAME>"
                                        + "<USERID>" + "14830" + "</USERID>"//hard coded user ID
                                        + "<PANINPUT>" + "<PANNO>" + newPANCard + "</PANNO>"
                                        + "<LASTNAME>" + "" + "</LASTNAME>"
                                        + "<FIRSTNAME>" + "ABC" + "</FIRSTNAME>"
                                        + "<MIDDLENAME>" + "" + "</MIDDLENAME>"
                                        + "<TRANSACTIONID>" + "CMS_" + r.nextInt(999999) + "</TRANSACTIONID>"
                                        + "<PURPOSE>Agent On Boarding</PURPOSE>"
                                        + "</PANINPUT>"
                                        + "</PANDETAILS>";
                                System.out.println("strPANInput = " + strPANInput);
                                mAsyncCheckPAN = new AsyncCheckPAN();
                                mAsyncCheckPAN.execute(strPANInput);
                            }
                        }  /*else if (nonPayoutRequest.equalsIgnoreCase("Certificate of Existence for annuity holders")) {
                            //
                            Gson gson = new Gson();
                            String annuityDetails = gson.toJson(annuityPersonDetails);
                            Intent captureImageIntent = new Intent(context, COECaptureImageActivity.class);
                            captureImageIntent.putExtra("annuityNo", "");
                            captureImageIntent.putExtra("annuityPolicyNo", policyNumber);
                            captureImageIntent.putExtra("branchCode", BRCOD);
                            captureImageIntent.putExtra("mobileNumber", parivartanMobile);
                            captureImageIntent.putExtra("annuityDetails", annuityDetails);
                            startActivity(captureImageIntent);

                        } else if (nonPayoutRequest.equalsIgnoreCase("Declaration of Good Health (DGH)")) {
                            //Done
                            Intent intent = new Intent(context, DGHActivity.class);
                            intent.putExtra("policyNumber", policyNumber);
                            intent.putExtra("branchCode", BRCOD);
                            intent.putExtra("mobileNumber", parivartanMobile);
                            startActivity(intent);
                        } else if (nonPayoutRequest.equalsIgnoreCase("Hit Date Change")) {
                            //Done
                            Intent intent = new Intent(context, NonPayoutHitDateChangeActivity.class);
                            intent.putExtra("policyNumber", policyNumber);
                            intent.putExtra("branchCode", BRCOD);
                            intent.putExtra("mobileNumber", parivartanMobile);
                            startActivity(intent);
                        } else if (nonPayoutRequest.equalsIgnoreCase("Fund Redirection")) {
                            //Done
                            Intent intent = new Intent(context, NonPayoutFundRedirectionActivity.class);
                            intent.putExtra("policyNumber", policyNumber);
                            intent.putExtra("branchCode", BRCOD);
                            intent.putExtra("mobileNumber", parivartanMobile);
                            startActivity(intent);
                        } else if (nonPayoutRequest.equalsIgnoreCase("Name Correction")) {
                            //Done
                            Intent intent = new Intent(context, ServiceMultipleRequestActivity.class);
                            intent.putExtra("policyNumber", policyNumber);
                            intent.putExtra("payoutOption", nonPayoutRequest);
                            intent.putExtra("branchCode", BRCOD);
                            intent.putExtra("mobileNumber", parivartanMobile);
                            startActivity(intent);
                        }else if (nonPayoutRequest.equalsIgnoreCase("Frequency Change")) {
                            //Done
                            Intent intent = new Intent(context, NonPayoutFrequencyChangeActivity.class);
                            intent.putExtra("policyNumber", policyNumber);
                            intent.putExtra("branchCode", BRCOD);
                            intent.putExtra("mobileNumber", parivartanMobile);
                            startActivity(intent);
                        } else if (nonPayoutRequest.equalsIgnoreCase("Premium Holiday")) {
                            //Done
                            Intent intent = new Intent(context, NonPayoutPremiumHolidayActivity.class);
                            intent.putExtra("policyNumber", policyNumber);
                            intent.putExtra("branchCode", BRCOD);
                            intent.putExtra("mobileNumber", parivartanMobile);
                            startActivity(intent);
                        } else if (nonPayoutRequest.equalsIgnoreCase("Bank Account update")) {
                            //Done
                            Intent intent = new Intent(context, NonPayoutBankAccountUpdateActivity.class);
                            intent.putExtra("policyNumber", policyNumber);
                            intent.putExtra("branchCode", BRCOD);
                            intent.putExtra("mobileNumber", parivartanMobile);
                            intent.putExtra("dob", dob);
                            startActivity(intent);
                        } else if (nonPayoutRequest.equalsIgnoreCase("Address Change")) {
                            //Done
                            Intent intent = new Intent(context, NonPayoutAddressChangeActivity.class);
                            intent.putExtra("policyNumber", policyNumber);
                            intent.putExtra("branchCode", BRCOD);
                            intent.putExtra("mobileNumber", parivartanMobile);
                            intent.putExtra("dob", dob);
                            startActivity(intent);
                        } else if (nonPayoutRequest.equalsIgnoreCase("Fund Switch")) {
                            //Done
                            Intent intent = new Intent(context, FundSwitchActivity.class);
                            intent.putExtra("policyNumber", policyNumber);
                            intent.putExtra("branchCode", BRCOD);
                            intent.putExtra("mobileNumber", parivartanMobile);
                            intent.putExtra("dob", dob);
                            startActivity(intent);
                        } */

                    }

                } else {
                    commonMethods.showMessageDialog(context, "Document Upload Failed");
                }

            } else {
                commonMethods.showMessageDialog(context, "Document Upload Failed");
            }
        }
    }

}
