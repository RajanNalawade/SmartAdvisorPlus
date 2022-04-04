package sbilife.com.pointofsale_bancaagency.home.dgh;

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
import android.location.LocationManager;
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
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.model.LatLng;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.xbizventures.ocrlib.OcrActivity;

import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import sbilife.com.pointofsale_bancaagency.ParseXML;
import sbilife.com.pointofsale_bancaagency.R;
import sbilife.com.pointofsale_bancaagency.ServiceURL;
import sbilife.com.pointofsale_bancaagency.branchlocator.GPSTracker;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;
import sbilife.com.pointofsale_bancaagency.common.CompressImage;
import sbilife.com.pointofsale_bancaagency.common.GenerateOTPGeneralAsyncTask;
import sbilife.com.pointofsale_bancaagency.common.StorageUtils;
import sbilife.com.pointofsale_bancaagency.common.ValidateOTPGeneralAsyncTask;
import sbilife.com.pointofsale_bancaagency.reports.ServiceHits;
import sbilife.com.pointofsale_bancaagency.utility.AlertDialogMessage;
import sbilife.com.pointofsale_bancaagency.utility.SelfAttestedDocumentActivity;

public class DGHActivity extends AppCompatActivity implements View.OnClickListener,
        AdapterView.OnItemSelectedListener, ServiceHits.DownLoadData, RadioGroup.OnCheckedChangeListener,
        TextWatcher, GenerateOTPGeneralAsyncTask.GenerateOTPAsyncResultInterface, ValidateOTPGeneralAsyncTask.ValidateOTPAsyncResultInterface {

    private final int REQUEST_CODE_PICK_PHOTO_FILE = 3;
    private final int REQUEST_CODE_PICK_FILE = 2;
    private final String JPEG_FILE_PREFIX = "IMG_";
    private ProgressDialog mProgressDialog;
    private Context context;
    private CommonMethods commonMethods;
    private StorageUtils mStorageUtils;
    private RecyclerView recyclerview;
    private ArrayList<QuestionsValuesModel> globalDataList;
    private SelectedAdapter selectedAdapter;
    private Spinner spnr_document_upload_document_identity, spnr_document_upload_document_address;
    private LinearLayout llIdentityAadhaarScan, llAddressAadhaarScan;
    private boolean is1WProduct = false;

    private int increment;
    private final int REQUEST_OCR = 1;
    private int isBrowseCapture = 0;


    private Button buttonValidateOTP, buttonGenerateOTP;

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

    private int deleteAddressDocument, deleteIdentityDocument, uploadFlag, deleteOtherDocument;
    private File addressProofFileName, identityProofFileName, userPhotoFileName, agentPhotoFileName, otherProofFileName;
    private final String str_selected_urn_no = "";
    private String Check = "";
    private String strUploadDocument = "";
    private static File f;

    String strOCRDocumentNames = "", strAadhaarQRScannedValue = "",
            strSelectedProof = "", str_QR_code_Name = "", str_QR_code_DOB = "", str_QR_code_Gender = "",
            str_qr_code_address = "", str1WFirstQR = "", str_QR_code_mailID = "", str_QR_code_mobile = "",
            str_ekyc_code_Name = "", str_ekyc_code_DOB = "", str_ekyc_code_Gender = "", str_ekyc_code_address = "",
            str_ekyc_code_mailID = "", str_ekyc_code_mobile = "", str1WFirstEKYC = "", str_QR_code_Photo = "",
            str_ekyc_code_Name_1W_spouse = "", str_ekyc_code_DOB_1W_spouse = "", str_ekyc_code_Gender_1W_spouse = "",
            str_ekyc_code_address_1W_spouse = "", str_QR_code_Photo_1W_spouse = "";
    private CommonMethods.UserDetailsValuesModel mUserDetailsValuesModel;
    private GPSTracker gpsTracker;
    private LatLng mLatLng;
    private File DGHFilePath, customerPDFFilePath;

    private EditText etPolicyNumber, etLifeAssuredName, etCountryCode,
            etMobileNumber, etEmailId, etHeights, etWeight, etHealthStatus, etDateOfProposalDetailsOptionAYes,
            etDateOfProposalDetailsOptionBYes, etDateOfProposalDetailsOptionCYes, etQuestion5New, etQuestion6New,
            etQuestion7New, etQuestion8NewA, etQuestion8NewB, etQuestion8NewC, etQuestion9New, etSticksPackets10A1, etNoOfYearsUsed10A1, etQuantity10A2AlcoholType,
            etQuantity10A2Ml, etNoOfYearsUsed10A2, etTypeOfDrug10A3, etNoOfYearsUsed10A3, etConsumptionQuit10B,
            etPregnancyMonth11A, etGynecologicalProblem11B, etWeightLoss, etMultipleDiseaseDetails, edt_document_upload_document_others;
    private TextView tvDateOfBirth;
    private TextView tvAge;
    private TextView tvImageIDProofDetails;
    private TextView TVImageAddressProofDetails;
    private TextView TVImageOtherProofDetails;
    private Spinner spinnerGender, spinnerOccupation, spinnerCountryResidence;
    private AutoCompleteTextView autoCompleteTextViewCountryResidence;

    private RadioGroup radioGroupResidentStatus, radioGroupHealthStatus, rgDateOfProposalDetailsOptionA,
            rgDateOfProposalDetailsOptionB, rgDateOfProposalDetailsOptionC, radioGroupQuestion5New,
            radioGroupQuestion6New, radioGroupQuestion7New, radioGroupQuestion8NewA, radioGroupQuestion8NewB,
            radioGroupQuestion8NewC,
            radioGroupQuestion9New, radioGroupQuestion10A1, radioGroupQuestion10A2, radioGroupQuestion10A3,
            radioGroupQuestion11A, radioGroupQuestion11B, radioGroupWeightLoss, rgMultipleDiseaseDetails;
    //radioGroupFormFiller;

    private RadioGroup radioGroupPastForeignVisit, radioGroupScreenedAtAirport, radioGroupTestedForCovid, radioGroupHomeQuarantine, radioGroupSelfIsolation,
            radioGroupunderObservation, radioGroupFutureForeignVisit, radioGroupSymptoms, radioGroupRespiratorySymptoms,
            radioGroupContactWithCorona;

    private EditText etPastCountryName, etFutureCountryname, etOTP, etPlace;

    private TextView tvPastFromDuration, tvPastToDuration, tvPastReturnDate, tvFutureFromDuration, tvFutureToDuration, tvFutureReturnDate;

    private String residentRBString = "", healthRBString = "", DateOfProposalDetailsOptionARBString = "",
            dateOfProposalOptionBRBString = "", dateOfProposalOptionCRBString = "", question5NewRBString = "",
            question6NewRBString = "", question7NewRBString = "", question8NewARBString = "",
            question8NewBRBString = "", question8NewCRBString = "", question9NewRBString = "", question10A1RBString = "",
            question10A2RBString = "",
            question10A3RBString = "", question11ARBString = "", question11BRBString = "",
            weightLossRBString = "", multipleDiseaseRBString = "";//, otherFormFillerRBString;
    private LinearLayout llHealthStatusQ1, llDateOfProposalDetailsOptionAYes, llDateOfProposalDetailsOptionBYes,
            llDateOfProposalDetailsOptionCYes, llQuestion5New, llQuestion6New, llQuestion7New, llQuestion8NewA,
            llQuestion8NewB, llQuestion8NewC, llQuestion9New, ll10A1, ll10A2, ll10A3, llWeightLoss,
            llMultipleDiseaseDetails;// llFillerOtherThanProp;

    private ImageView imagePhotoGraphGPSCord, ibAgentTypeUploadPhoto;
    private int mYear, mMonth, mDay, datecheck = 0;
    private String policyNumber, lifeAssuredName, dobString, occupation, countryResidence, countryCode,
            heights, weight, healthStatus, dateOfProposalOptionAYes,
            dateOfProposalOptionBYes, dateOfProposalOptionCYes, question5New, question6New, question7New,
            question8NewA, question8NewB, question8NewC, question9New, sticksPackets10A1, noOfYearsUsed10A1,
            quantity10A2AlcoholType, quantity10A2Ml, noOfYearsUsed10A2, typeOfDrug10A3, noOfYearsUsed10A3,
            consumptionQuit10B, pregnancyMonth11A, gynecologicalProblem11B, weightLossDetails,
            multipleDiseaseDetails, strCIFBDMUserId = "", strCIFBDMEmailId = "", strCIFBDMMObileNo = "", GENDER,
            OTP, NAME, MOBILE, EMAILID, AGE, place, languageSelected;

    private String pastForeignVisit, pastCountryName, pastFromDuration, pastToDuration, pastReturnDate, screeneAtAirport,
            testedForCovid, homeQuarantine, underObservation, selfIsolation, futureForeignVisit, futureCountryName,
            futureFromDuration, futureToDuration, futureReturnDate, familyMemberSymptoms, respiratorySymptoms, contactWithCorona;

    private Bitmap identityProofPhotoBitmap, addressProofPhotoBitmap, userPhotoBitmap, agentPhotoBitmap, otherPhotoBitmap;
    private String IDProofDOCDetails = "", addressProofDOCDetails = "", otherDocDetailsString = "";
    private DownloadPolicyDetailsAsyncTask downloadPolicyDetailsAsyncTask;
    private LinearLayout ll_dgh, llFemaleLivesOnly, llOTP;

    private ValidateOTPGeneralAsyncTask validateOTPGeneralAsyncTask;
    private AuthenticatePDFAsync authenticatePDFAsync;
    private GenerateOTPGeneralAsyncTask generateOTPGeneralAsyncTask;
    private SaveDGHDataAsyncTask saveDGHDataAsyncTask;
    private UploadPDFService uploadPDFService;
    private String str_Doc_Abbreviation_identity = "";
    private String str_Doc_Abbreviation = "";
    private String declarationOne;
    private String declarationSecond;
    private CheckBox checkBoxOtherUserDeclaration, checkBoxOtherUserDeclarationSecond;

    private Spinner spinnerLanguages;
    private String userType, userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_dgh);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);
        context = this;
        commonMethods = new CommonMethods();
        mStorageUtils = new StorageUtils();
        commonMethods.setApplicationToolbarMenu(this, "DGH");


        Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        getUserDetails();
        recyclerview = findViewById(R.id.recyclerview);
        Button buttonAddRow = findViewById(R.id.buttonAddRow);

        llIdentityAadhaarScan = findViewById(R.id.llIdentityAadhaarScan);

        llAddressAadhaarScan = findViewById(R.id.llAddressAadhaarScan);

        ll_dgh = findViewById(R.id.ll_dgh);
        ll_dgh.setVisibility(View.GONE);

        llOTP = findViewById(R.id.llOTP);
        llOTP.setVisibility(View.GONE);

        llFemaleLivesOnly = findViewById(R.id.llFemaleLivesOnly);
        llFemaleLivesOnly.setVisibility(View.GONE);
        spnr_document_upload_document_identity = (Spinner) findViewById(R.id.spnr_document_upload_document_identity);
        spnr_document_upload_document_identity.setOnItemSelectedListener(this);
        img_btn_document_upload_preview_image_identity = (ImageButton) findViewById(R.id.img_btn_document_upload_preview_image_identity);
        img_btn_document_delete_identity = (ImageButton) findViewById(R.id.img_btn_document_delete_identity);
        img_btn_document_upload_click_image_identity = (ImageButton) findViewById(R.id.img_btn_document_upload_click_image_identity);
        ImageButton img_btn_document_upload_identity_upload = (ImageButton) findViewById(R.id.img_btn_document_upload_identity_upload);
        img_btn_document_upload_click_browse_identity = (ImageButton) findViewById(R.id.img_btn_document_upload_click_browse_identity);
        llIdentityAadhaarScan = (LinearLayout) findViewById(R.id.llIdentityAadhaarScan);


        img_btn_document_upload_click_image_identity = findViewById(R.id.img_btn_document_upload_click_image_identity);
        img_btn_document_upload_click_image_address = findViewById(R.id.img_btn_document_upload_click_image_address);

        setBtnListenerOrDisable(img_btn_document_upload_click_image_identity,
                IdentityProofOnClickListener, MediaStore.ACTION_IMAGE_CAPTURE);
        setBtnListenerOrDisable(img_btn_document_upload_click_image_address,
                AddressProofOnClickListener, MediaStore.ACTION_IMAGE_CAPTURE);
        img_btn_document_upload_click_image_others = findViewById(R.id.img_btn_document_upload_click_image_others);
        setBtnListenerOrDisable(img_btn_document_upload_click_image_others,
                OthersProofOnClickListener, MediaStore.ACTION_IMAGE_CAPTURE);
        spnr_document_upload_document_address = (Spinner) findViewById(R.id.spnr_document_upload_document_address);
        spnr_document_upload_document_address.setOnItemSelectedListener(this);
        img_btn_document_upload_click_preview_image_address = (ImageButton) findViewById(R.id.img_btn_document_upload_click_preview_image_address);
        img_btn_document_delete_address = (ImageButton) findViewById(R.id.img_btn_document_delete_address);
        img_btn_document_upload_click_image_address = (ImageButton) findViewById(R.id.img_btn_document_upload_click_image_address);
        img_btn_document_upload_click_browse_address = (ImageButton) findViewById(R.id.img_btn_document_upload_click_browse_address);
        ImageButton img_btn_document_upload_address_upload = (ImageButton) findViewById(R.id.img_btn_document_upload_address_upload);

        img_btn_document_upload_click_browse_Others = findViewById(R.id.img_btn_document_upload_click_browse_Others);

        img_btn_document_delete_Others = findViewById(R.id.img_btn_document_delete_Others);
        img_btn_document_upload_click_preview_image_others = findViewById(R.id.img_btn_document_upload_click_preview_image_others);

        llAddressAadhaarScan = (LinearLayout) findViewById(R.id.llAddressAadhaarScan);


        buttonValidateOTP = findViewById(R.id.buttonValidateOTP);
        buttonGenerateOTP = findViewById(R.id.buttonGenerateOTP);

        buttonGenerateOTP.setOnClickListener(this);
        buttonValidateOTP.setOnClickListener(this);

        etPolicyNumber = findViewById(R.id.etPolicyNumber);
        etLifeAssuredName = findViewById(R.id.etLifeAssuredName);

        autoCompleteTextViewCountryResidence = findViewById(R.id.autoCompleteTextViewCountryResidence);
        etCountryCode = findViewById(R.id.etCountryCode);
        etMobileNumber = findViewById(R.id.etMobileNumber);
        etEmailId = findViewById(R.id.etEmailId);
        etHeights = findViewById(R.id.etHeights);
        etHeights.addTextChangedListener(this);

        etWeight = findViewById(R.id.etWeight);
        etHealthStatus = findViewById(R.id.etHealthStatus);
        etHealthStatus.addTextChangedListener(this);

        etDateOfProposalDetailsOptionAYes = findViewById(R.id.etDateOfProposalDetailsOptionAYes);
        etDateOfProposalDetailsOptionAYes.addTextChangedListener(this);

        etDateOfProposalDetailsOptionBYes = findViewById(R.id.etDateOfProposalDetailsOptionBYes);
        etDateOfProposalDetailsOptionBYes.addTextChangedListener(this);

        etDateOfProposalDetailsOptionCYes = findViewById(R.id.etDateOfProposalDetailsOptionCYes);
        etDateOfProposalDetailsOptionCYes.addTextChangedListener(this);

        etQuestion5New = findViewById(R.id.etQuestion5New);
        etQuestion5New.addTextChangedListener(this);

        etQuestion6New = findViewById(R.id.etQuestion6New);
        etQuestion6New.addTextChangedListener(this);

        etQuestion7New = findViewById(R.id.etQuestion7New);
        etQuestion7New.addTextChangedListener(this);

        etQuestion8NewA = findViewById(R.id.etQuestion8NewA);
        etQuestion8NewA.addTextChangedListener(this);

        etQuestion8NewB = findViewById(R.id.etQuestion8NewB);
        etQuestion8NewB.addTextChangedListener(this);

        etQuestion8NewC = findViewById(R.id.etQuestion8NewC);
        etQuestion8NewC.addTextChangedListener(this);

        etQuestion9New = findViewById(R.id.etQuestion9New);
        etQuestion9New.addTextChangedListener(this);

        etSticksPackets10A1 = findViewById(R.id.etSticksPackets10A1);
        etNoOfYearsUsed10A1 = findViewById(R.id.etNoOfYearsUsed10A1);
        etQuantity10A2AlcoholType = findViewById(R.id.etQuantity10A2AlcoholType);
        etQuantity10A2Ml = findViewById(R.id.etQuantity10A2Ml);
        etNoOfYearsUsed10A2 = findViewById(R.id.etNoOfYearsUsed10A2);
        etTypeOfDrug10A3 = findViewById(R.id.etTypeOfDrug10A3);
        etNoOfYearsUsed10A3 = findViewById(R.id.etNoOfYearsUsed10A3);

        etConsumptionQuit10B = findViewById(R.id.etConsumptionQuit10B);
        etConsumptionQuit10B.addTextChangedListener(this);

        etPregnancyMonth11A = findViewById(R.id.etPregnancyMonth11A);
        etPregnancyMonth11A.addTextChangedListener(this);

        etGynecologicalProblem11B = findViewById(R.id.etGynecologicalProblem11B);
        etGynecologicalProblem11B.addTextChangedListener(this);

        etWeightLoss = findViewById(R.id.etWeightLoss);
        etWeightLoss.addTextChangedListener(this);

        etMultipleDiseaseDetails = findViewById(R.id.etMultipleDiseaseDetails);
        etMultipleDiseaseDetails.addTextChangedListener(this);

        edt_document_upload_document_others = findViewById(R.id.edt_document_upload_document_others);
        tvDateOfBirth = findViewById(R.id.tvDateOfBirth);
        tvAge = findViewById(R.id.tvAge);
        tvImageIDProofDetails = findViewById(R.id.tvImageIDProofDetails);
        TVImageAddressProofDetails = findViewById(R.id.TVImageAddressProofDetails);
        TVImageOtherProofDetails = findViewById(R.id.TVImageOtherProofDetails);

        TextView tvUserTypeDescritionTitle = findViewById(R.id.tvUserTypeDescritionTitle);
        TextView TVLMCIFBDMUMGPSDetails = findViewById(R.id.TVLMCIFBDMUMGPSDetails);
        TextView tvUserTypePhotoTitle = findViewById(R.id.tvUserTypePhotoTitle);

        userType = commonMethods.GetUserType(context);
        tvUserTypeDescritionTitle.setText(userType);
        userName = commonMethods.getUserName(context);
        String userDetails = "User Name : " + userName + " \nUser Code : " + strCIFBDMUserId;
        TVLMCIFBDMUMGPSDetails.setText(userDetails);

        tvUserTypePhotoTitle.setText(userType + " Photo");

        ibAgentTypeUploadPhoto = findViewById(R.id.ibAgentTypeUploadPhoto);
        spinnerGender = findViewById(R.id.spinnerGender);
        spinnerOccupation = findViewById(R.id.spinnerOccupation);
        mUserDetailsValuesModel = commonMethods.setUserDetails(context);


        radioGroupResidentStatus = findViewById(R.id.radioGroupResidentStatus);
        radioGroupHealthStatus = findViewById(R.id.radioGroupHealthStatus);
        rgDateOfProposalDetailsOptionA = findViewById(R.id.rgDateOfProposalDetailsOptionA);
        rgDateOfProposalDetailsOptionB = findViewById(R.id.rgDateOfProposalDetailsOptionB);
        rgDateOfProposalDetailsOptionC = findViewById(R.id.rgDateOfProposalDetailsOptionC);
        radioGroupQuestion5New = findViewById(R.id.radioGroupQuestion5New);
        radioGroupQuestion6New = findViewById(R.id.radioGroupQuestion6New);
        radioGroupQuestion7New = findViewById(R.id.radioGroupQuestion7New);
        radioGroupQuestion8NewA = findViewById(R.id.radioGroupQuestion8NewA);
        radioGroupQuestion8NewB = findViewById(R.id.radioGroupQuestion8NewB);
        radioGroupQuestion8NewC = findViewById(R.id.radioGroupQuestion8NewC);
        radioGroupQuestion9New = findViewById(R.id.radioGroupQuestion9New);
        radioGroupQuestion10A1 = findViewById(R.id.radioGroupQuestion10A1);
        radioGroupQuestion10A2 = findViewById(R.id.radioGroupQuestion10A2);
        radioGroupQuestion10A3 = findViewById(R.id.radioGroupQuestion10A3);
        radioGroupQuestion11A = findViewById(R.id.radioGroupQuestion11A);
        radioGroupQuestion11B = findViewById(R.id.radioGroupQuestion11B);
        radioGroupWeightLoss = findViewById(R.id.radioGroupWeightLoss);
        rgMultipleDiseaseDetails = findViewById(R.id.rgMultipleDiseaseDetails);
        //radioGroupFormFiller = findViewById(R.id.radioGroupFormFiller);
        checkBoxOtherUserDeclaration = findViewById(R.id.checkBoxOtherUserDeclaration);
        checkBoxOtherUserDeclarationSecond = findViewById(R.id.checkBoxOtherUserDeclarationSecond);
        checkBoxOtherUserDeclarationSecond.setVisibility(View.GONE);
        TextView tvUserName, tvUserCode;
        tvUserName = findViewById(R.id.tvUserName);
        tvUserCode = findViewById(R.id.tvUserCode);

        spinnerLanguages = findViewById(R.id.spinnerLanguages);
        String[] languageList = {"Select Language", "English", "Marathi", "Bengali", "Hindi",
                "Gujarati", "Tamil", "Telugu", "Oriya", "Malayalam", "Kannada",
                "Punjabi", "Naga", "Manipuri", "Mizo", "Marwadi", "Assamese"};
        ArrayAdapter<String> languageAdapter = new ArrayAdapter<>(
                getApplicationContext(), R.layout.spinner_item, languageList);
        languageAdapter.setDropDownViewResource(R.layout.spinner_item1);
        spinnerLanguages.setAdapter(languageAdapter);
        spinnerLanguages.setAdapter(languageAdapter);
        languageAdapter.notifyDataSetChanged();

        tvUserName.setText("Name : " + userName);
        tvUserCode.setText("User Code : " + strCIFBDMUserId);

        radioGroupPastForeignVisit = findViewById(R.id.radioGroupPastForeignVisit);
        radioGroupScreenedAtAirport = findViewById(R.id.radioGroupScreenedAtAirport);
        radioGroupTestedForCovid = findViewById(R.id.radioGroupTestedForCovid19);
        radioGroupHomeQuarantine = findViewById(R.id.radioGroupHomeQuarantine);
        radioGroupSelfIsolation = findViewById(R.id.radioGroupSelfIsolation);
        radioGroupunderObservation = findViewById(R.id.radioGroupUnderObservation);
        radioGroupFutureForeignVisit = findViewById(R.id.radioGroupFutureCountryVisit);
        radioGroupSymptoms = findViewById(R.id.radioGroupSymptoms);
        radioGroupRespiratorySymptoms = findViewById(R.id.radioGroupRespiratorySymptoms);
        radioGroupContactWithCorona = findViewById(R.id.radioGroupContactWithCorona);

        etPastCountryName = findViewById(R.id.etPastCountryVisited);
        etFutureCountryname = findViewById(R.id.etFutureCountryVisited);

        etOTP = findViewById(R.id.etOTP);
        etPlace = findViewById(R.id.etPlace);
        tvPastFromDuration = findViewById(R.id.tvPastFromDuration);
        tvPastToDuration = findViewById(R.id.tvPastToDuration);
        tvPastReturnDate = findViewById(R.id.tvPastReturnDate);
        tvFutureFromDuration = findViewById(R.id.tvFutureFromDuration);
        tvFutureToDuration = findViewById(R.id.tvFutureToDuration);
        tvFutureReturnDate = findViewById(R.id.tvFutureReturnDate);


        imagePhotoGraphGPSCord = findViewById(R.id.imagePhotoGraphGPSCord);

        llHealthStatusQ1 = findViewById(R.id.llHealthStatusQ1);
        llDateOfProposalDetailsOptionAYes = findViewById(R.id.llDateOfProposalDetailsOptionAYes);
        llDateOfProposalDetailsOptionBYes = findViewById(R.id.llDateOfProposalDetailsOptionBYes);
        llDateOfProposalDetailsOptionCYes = findViewById(R.id.llDateOfProposalDetailsOptionCYes);
        llQuestion5New = findViewById(R.id.llQuestion5New);
        llQuestion6New = findViewById(R.id.llQuestion6New);
        llQuestion7New = findViewById(R.id.llQuestion7New);
        llQuestion8NewA = findViewById(R.id.llQuestion8NewA);
        llQuestion8NewB = findViewById(R.id.llQuestion8NewB);
        llQuestion8NewC = findViewById(R.id.llQuestion8NewC);
        llQuestion9New = findViewById(R.id.llQuestion9New);
        ll10A1 = findViewById(R.id.ll10A1);
        ll10A2 = findViewById(R.id.ll10A2);
        ll10A3 = findViewById(R.id.ll10A3);
        llWeightLoss = findViewById(R.id.llWeightLoss);
        llMultipleDiseaseDetails = findViewById(R.id.llMultipleDiseaseDetails);
        //llFillerOtherThanProp = findViewById(R.id.llFillerOtherThanProp);

        llHealthStatusQ1.setVisibility(View.GONE);
        llDateOfProposalDetailsOptionAYes.setVisibility(View.GONE);
        llDateOfProposalDetailsOptionBYes.setVisibility(View.GONE);
        llDateOfProposalDetailsOptionCYes.setVisibility(View.GONE);
        llQuestion5New.setVisibility(View.GONE);
        llQuestion6New.setVisibility(View.GONE);
        llQuestion7New.setVisibility(View.GONE);
        llQuestion8NewA.setVisibility(View.GONE);
        llQuestion8NewB.setVisibility(View.GONE);
        llQuestion8NewC.setVisibility(View.GONE);
        llQuestion9New.setVisibility(View.GONE);
        ll10A1.setVisibility(View.GONE);
        ll10A2.setVisibility(View.GONE);
        ll10A3.setVisibility(View.GONE);
        llWeightLoss.setVisibility(View.GONE);
        llMultipleDiseaseDetails.setVisibility(View.GONE);
        //llFillerOtherThanProp.setVisibility(View.GONE);

        etPregnancyMonth11A.setVisibility(View.GONE);
        etGynecologicalProblem11B.setVisibility(View.GONE);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        // set LayoutManager to RecyclerView
        recyclerview.setLayoutManager(linearLayoutManager);
        // call the constructor of CustomAdapter to send the reference and data to Adapter
        globalDataList = new ArrayList<>();

        selectedAdapter = new SelectedAdapter(globalDataList);
        recyclerview.setAdapter(selectedAdapter);
        recyclerview.setItemAnimator(new DefaultItemAnimator());
        buttonAddRow.setOnClickListener(this);
        spnr_document_upload_document_identity.setOnItemSelectedListener(this);

        String[] genderList = {"Male", "Female", "Third Gender"};
        ArrayAdapter<String> genderAdapter = new ArrayAdapter<>(
                getApplicationContext(), R.layout.spinner_item, genderList);
        genderAdapter.setDropDownViewResource(R.layout.spinner_item1);
        spinnerGender.setAdapter(genderAdapter);
        genderAdapter.notifyDataSetChanged();
        spinnerGender.setOnItemSelectedListener(this);
        spinnerGender.setEnabled(false);
        spinnerGender.setClickable(false);

        String[] countryList = {"Select Country",
                "India", "United States (USA)",
                "United Kingdom (UK)", "United Arab Emirates(UAE)",
                "Turkey", "Taiwan",
                "Switzerland",
                "Sweden",
                "Spain",
                "South Korea",
                "Singapore",
                "Qatar",
                "Portugal",
                "Oman",
                "New Zealand",
                "Norway",
                "Malaysia",
                "Luxembourg",
                "Kuwait",
                "Japan",
                "Italy",
                "Ireland",
                "Hungary",
                "Netherlands (Holland)",
                "Hong Kong",
                "Greece",
                "Germany",
                "France",
                "Finland",
                "Egypt",
                "Denmark",
                "Czech Republic",
                "Cyprus",
                "Canada",
                "Belgium",
                "Bahrain",
                "Austria",
                "Australia",
                "Russia",
                "Indonesia",
                "Kenya",
                "Abidjan (West Africa)",
                "Nepal",
                "Zimbabwe",
                "Zambia",
                "Zaire",
                "Yugoslavia",
                "Yemen",
                "Western Sahara",
                "Uganda",
                "Tokelau",
                "Tonga",
                "Togo",
                "Tajikistan",
                "Tanzania",
                "Swaziland",
                "Suriname",
                "Sudan",
                "Somalia",
                "Solomon Islands",
                "Sierra Leone",
                "Serbia",
                "Senegal",
                "Rwanda",
                "Panama",
                "Palestine",
                "Palau",
                "Nigeria",
                "Niger",
                "Nicaragua",
                "Namibia",
                "Mozambique",
                "Morocco",
                "Montserrat",
                "Mongolia",
                "Moldova",
                "Mali",
                "Malawi",
                "Madagascar",
                "Macedonia",
                "Libya",
                "Liberia",
                "Lesotho",
                "Lebanon",
                "Ivory Coast",
                "Honduras",
                "Haiti",
                "Guyana",
                "Guinea-Bissau",
                "Guatemala",
                "Ghana",
                "Georgia",
                "Gambia",
                "Gabon",
                "Federated States of Micronesia",
                "Ethiopia",
                "Estonia",
                "Eritrea",
                "Equatorial Guinea",
                "El Salvador",
                "Djibouti",
                "Curacao",
                "Cuba",
                "Congo",
                "Comoros",
                "Chad",
                "Central African Republic",
                "Cayman Islands",
                "Cape Verde",
                "Canary Islands",
                "Cameroon",
                "Cambodia (Kampuchea)",
                "Burundi",
                "Burkina Faso",
                "Botswana",
                "Bosnia-Herzegovina",
                "Benin",
                "Antigua, Barbuda",
                "Anguilla",
                "Angola",
                "Albania",
                "Papua New Guinea",
                "Marshall Islands",
                "Kiribati",
                "Vietnam - Danag City",
                "Bhutan - Shemgag",
                "Phillipines - Manila",
                "Phillipines - Lucena City",
                "Russia - Valadivostok-Nerchinskay",
                "Russia - St. Petersbury",
                "Bangladesh - Wardha",
                "Bangladesh - Dhaka",
                "Indonesia - Tangerang",
                "Indonesia - Jakarta",
                "Indonesia - Purwakarta",
                "KSA - Al Alsa",
                "KSA - Jeddah",
                "KSA - Riyadh",
                "Kingdom Of Saudi Arabia",
                "Brunei",
                "Jordon",
                "Walmer South Africa",
                "Belarus",
                "Uzbekistan",
                "Turkmenistan",
                "Tajikstan",
                "Sao Tome Principe",
                "Pakistan",
                "Northern Parts of Cyprus",
                "Myanmar",
                "Laos",
                "Kazakhstan",
                "Iraq",
                "Iran",
                // India",
                "All Central and South American countries",
                "Cambodia",
                "Azerbaijan",
                "Armenia",
                "Algeria",
                "Afghanistan",
                "North Korea",
                "Krygystan",
                "Phillipines",
                "American Samoa",
                "Andorra",
                "Antarctica",
                "Antigua and Barbuda",
                "Argentina",
                "Aruba",
                "Ashmore and Cartier Islands",
                "Bahamas, The",
                "Barbados",
                "Bassas da India",
                "Belize",
                "Bermuda",
                "Bolivia",
                "Bosnia and Herzegovina",
                "Bouvet Island",
                "Brazil",
                "British Indian Ocean Territory",
                "British Virgin Islands",
                "Bulgaria",
                "Burma",
                "Chile",
                "China",
                "Christmas Island",
                "Clipperton Island",
                "Cocos (Keeling) Islands",
                "Colombia",
                "Congo, Democratic Republic of the",
                "Congo, Republic of the",
                "Cook Islands",
                "Coral Sea Islands",
                "Costa Rica",
                "Cote dIvoire",
                "Croatia",
                "Dhekelia",
                "Dominica",
                "Dominican Republic",
                "Ecuador",
                "Europa Island",
                "Falkland Islands (Islas Malvinas)",
                "Faroe Islands",
                "Fiji",
                "French Guiana",
                "French Polynesia",
                "French Southern and Antarctic Lands",
                "Gambia,The",
                "Gaza Strip",
                "Gibraltar",
                "Glorioso Islands",
                "Greenland",
                "Grenada",
                "Guadeloupe",
                "Guam",
                "Guernsey",
                "Guinea",
                "Heard Island and McDonald Islands",
                "Holy See (Vatican City)",
                "Iceland",
                "Isle of Man",
                "Israel",
                "Jamaica",
                "Jan Mayen",
                "Jersey",
                "Jordan",
                "Juan de Nova Island",
                "Korea, North",
                "Kyrgyzstan",
                "Latvia",
                "Liechtenstein",
                "Lithuania",
                "Macau",
                "Maldives",
                "Malta",
                "Martinique",
                "Mauritania",
                "Mauritius",
                "Mayotte",
                "Mexico",
                "Micronesia Federated States of",
                "Monaco",
                "Nauru",
                "Navassa Island",
                "Netherlands Antilles",
                "New Caledonia",
                "Niue",
                "Norfolk Island",
                "Northern Mariana Islands",
                "Paracel Islands",
                "Paraguay",
                "Peru",
                "Philippines",
                "Pitcairn Islands",
                "Poland",
                "Puerto Rico",
                "Reunion",
                "Romania",
                "Saint Helena",
                "Saint Kitts and Nevis",
                "Saint Lucia",
                "Saint Pierre and Miquelon",
                "Saint Vincent and the Grenadines",
                "Samoa",
                "San Marino",
                "Sao Tome and Principe",
                "Saudi Arabia",
                "Serbia and Montenegro",
                "Seychelles",
                "Slovakia",
                "Slovenia",
                "South Africa",
                "South Georgia and the South Sandwich Islands",
                "Spratly Islands",
                "Sri Lanka",
                "Svalbard",
                "Syria",
                "Thailand",
                "Tibet",
                "Timor-Leste",
                "Trinidad and Tobago",
                "Tromelin Island",
                "Tunisia",
                "Turks and Caicos Islands",
                "Tuvalu",
                "Ukraine",
                "Uruguay",
                "Vanuatu",
                "Venezuela",
                "Virgin Islands",
                "Wake Island",
                "Wallis and Futuna",
                "West Bank",
                "Akrotiri"};


        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this, android.R.layout.select_dialog_item, countryList);
        //Getting the instance of AutoCompleteTextView
        autoCompleteTextViewCountryResidence.setThreshold(1);//will start working from first character
        autoCompleteTextViewCountryResidence.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView

        String[] ls_Occupation = {"Select Occupation", "Service", "Professional", "Business", "Self employed",
                "Agriculturist", "Housewife", "Retired", "Defence Retired", "Student", "construction workers",
                "agriculture labor", "Family Pension", "Army", "Navy", "Air Force", "Indian Coast Guard",
                "Rashtriya Rifles", "Border Roads/GREF", "Assam Rifles", "BSF", "CISF", "CRPF", "ITBP", "NSG",
                "SSB", "Other Arm Forces except police"};
        ArrayAdapter<String> occupationAdapter = new ArrayAdapter<>(
                getApplicationContext(), R.layout.spinner_item, ls_Occupation);
        occupationAdapter.setDropDownViewResource(R.layout.spinner_item1);
        spinnerOccupation.setAdapter(occupationAdapter);
        occupationAdapter.notifyDataSetChanged();

        radioGroupResidentStatus.setOnCheckedChangeListener(this);
        radioGroupHealthStatus.setOnCheckedChangeListener(this);
        rgDateOfProposalDetailsOptionA.setOnCheckedChangeListener(this);
        rgDateOfProposalDetailsOptionB.setOnCheckedChangeListener(this);
        rgDateOfProposalDetailsOptionC.setOnCheckedChangeListener(this);
        radioGroupQuestion5New.setOnCheckedChangeListener(this);
        radioGroupQuestion6New.setOnCheckedChangeListener(this);
        radioGroupQuestion7New.setOnCheckedChangeListener(this);
        radioGroupQuestion8NewA.setOnCheckedChangeListener(this);
        radioGroupQuestion8NewB.setOnCheckedChangeListener(this);
        radioGroupQuestion8NewC.setOnCheckedChangeListener(this);
        radioGroupQuestion9New.setOnCheckedChangeListener(this);
        radioGroupQuestion10A1.setOnCheckedChangeListener(this);
        radioGroupQuestion10A2.setOnCheckedChangeListener(this);
        radioGroupQuestion10A3.setOnCheckedChangeListener(this);
        radioGroupQuestion11A.setOnCheckedChangeListener(this);
        radioGroupQuestion11B.setOnCheckedChangeListener(this);
        radioGroupWeightLoss.setOnCheckedChangeListener(this);

        radioGroupPastForeignVisit.setOnCheckedChangeListener(this);
        radioGroupScreenedAtAirport.setOnCheckedChangeListener(this);
        radioGroupTestedForCovid.setOnCheckedChangeListener(this);
        radioGroupHomeQuarantine.setOnCheckedChangeListener(this);
        radioGroupSelfIsolation.setOnCheckedChangeListener(this);
        radioGroupunderObservation.setOnCheckedChangeListener(this);
        radioGroupFutureForeignVisit.setOnCheckedChangeListener(this);
        radioGroupSymptoms.setOnCheckedChangeListener(this);
        radioGroupRespiratorySymptoms.setOnCheckedChangeListener(this);
        radioGroupContactWithCorona.setOnCheckedChangeListener(this);


        rgMultipleDiseaseDetails.setOnCheckedChangeListener(this);
        //radioGroupFormFiller.setOnCheckedChangeListener(this);


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

        Button buttonGetDetails = findViewById(R.id.buttonGetDetails);

        buttonGetDetails.setOnClickListener(this);

        tvDateOfBirth.setOnClickListener(this);
        tvPastFromDuration.setOnClickListener(this);
        tvPastToDuration.setOnClickListener(this);
        tvPastReturnDate.setOnClickListener(this);
        tvFutureFromDuration.setOnClickListener(this);
        tvFutureToDuration.setOnClickListener(this);
        tvFutureReturnDate.setOnClickListener(this);

        ibAgentTypeUploadPhoto.setOnClickListener(this);
        imagePhotoGraphGPSCord.setOnClickListener(this);
        etEmailId.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence arg0, int arg1,
                                      int arg2, int arg3) {

            }

            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
            }

            public void afterTextChanged(Editable arg0) {
                EMAILID = etEmailId.getText().toString();
                commonMethods.emailPatternValidation(etEmailId, context);
            }
        });


        gpsTracker = new GPSTracker(context);
        mLatLng = new LatLng(0.0, 0.0);

        getLocationPromt();

        mProgressDialog = new ProgressDialog(context, ProgressDialog.THEME_HOLO_LIGHT);
        String Message = "Loading Please wait...";
        mProgressDialog.setMessage(Html.fromHtml("<font color='#00a1e3'><b>" + Message + "<b></font>"));
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(true);

        mProgressDialog.setButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {


                        if (downloadPolicyDetailsAsyncTask != null) {
                            downloadPolicyDetailsAsyncTask.cancel(true);
                        }

                        if (mProgressDialog != null) {
                            if (mProgressDialog.isShowing()) {
                                mProgressDialog.dismiss();
                            }
                        }
                    }
                });

        mProgressDialog.setMax(100);


        spinnerLanguages.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    languageSelected = spinnerLanguages.getSelectedItem().toString();
                    declarationSecond = "I hereby declare that I have explained the contents of this form " +
                            "to the Proposer /policyholder/member insured in " + languageSelected + " Language." +
                            "\n\nI also declare that I have truly and correctly recorded the answers given by " +
                            "the Proposer /policyholder/member insured.";
                    checkBoxOtherUserDeclarationSecond.setText(declarationSecond);
                    checkBoxOtherUserDeclarationSecond.setVisibility(View.VISIBLE);
                } else {
                    checkBoxOtherUserDeclarationSecond.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

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

        Intent intent = getIntent();
        policyNumber = intent.getStringExtra("policyNumber");
        if (policyNumber != null) {
            etPolicyNumber.setText(policyNumber);
            onClick(buttonGetDetails);
        }

    }

    private void getUserDetails() {
        CommonMethods.UserDetailsValuesModel userDetailsValuesModel = commonMethods.setUserDetails(context);
        strCIFBDMUserId = userDetailsValuesModel.getStrCIFBDMUserId();
        strCIFBDMEmailId = userDetailsValuesModel.getStrCIFBDMEmailId();
        strCIFBDMMObileNo = userDetailsValuesModel.getStrCIFBDMMObileNo();
    }

    private void getLocationPromt() {
        LocationManager locationmanager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        if (locationmanager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

            try {
                mLatLng = commonMethods.getCurrentLocation(context, gpsTracker);

                if (mLatLng.latitude == 0.0 && mLatLng.longitude == 0.0) {
                    commonMethods.showToast(context, "Please check your gps connection and try again");
                    mLatLng = new LatLng(0.0, 0.0);
                    mLatLng = commonMethods.getCurrentLocation(context, gpsTracker);
                }

            } catch (Exception ex) {
                ex.printStackTrace();
                mLatLng = commonMethods.getCurrentLocation(context, gpsTracker);
            }
        } else {
            commonMethods.showGPSDisabledAlertToUser(context);
        }
    }

    private final DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {

            mYear = year;
            mMonth = month;
            mDay = day;
            updateDisplay(mYear, mMonth, mDay);
        }
    };

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

    private void showFutureDateDialog() {
        DatePickerDialog dialog = new DatePickerDialog(
                context,
                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                mDateSetListener,
                mYear, mMonth, mDay);

        dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    private void updateDisplay(int year, int month, int day) {

        String y = String.valueOf(year);
        String m = String.valueOf(month + 1);
        String da = String.valueOf(day);
        // String totaldate = m + "-" + da + "-" + y;

        /*if (m.contentEquals("1")) {
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

        }*/
        m = commonMethods.getFullMonthName(m);
        String totaldate = da + "-" + m + "-" + y;

        Calendar present_date = Calendar.getInstance();
        int tDay = present_date.get(Calendar.DAY_OF_MONTH);
        int tMonth = present_date.get(Calendar.MONTH);
        int tYear = present_date.get(Calendar.YEAR);

        int age;

        if (tMonth == mMonth) {
            if (tDay >= mDay) {
                age = tYear - mYear;
            } else
                age = tYear - mYear - 1;
        } else if (tMonth > mMonth) {
            age = tYear - mYear;
        } else
            age = tYear - mYear - 1;

        String final_age = Integer.toString(age);
        tvAge.setText(final_age);

        if (datecheck == 1) {
            tvDateOfBirth.setText(totaldate);
        }
        if (datecheck == 2) {
            tvPastFromDuration.setText(totaldate);
            pastFromDuration = totaldate;
        }
        if (datecheck == 3) {
            tvPastToDuration.setText(totaldate);
            pastToDuration = totaldate;
        }
        if (datecheck == 4) {
            tvPastReturnDate.setText(totaldate);
            pastReturnDate = totaldate;
        }
        if (datecheck == 5) {
            tvFutureFromDuration.setText(totaldate);
            futureFromDuration = totaldate;
        }
        if (datecheck == 6) {
            tvFutureToDuration.setText(totaldate);
            futureToDuration = totaldate;
        }
        if (datecheck == 7) {
            tvFutureReturnDate.setText(totaldate);
            futureReturnDate = totaldate;
        }
    }

    private final View.OnClickListener IdentityProofOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

//            if (!str_selected_urn_no.equals("")) {
            // ClearDocumentsPDF();
            if (!(spnr_document_upload_document_identity.getSelectedItem().toString()).equalsIgnoreCase("Select Document")) {
                uploadFlag = 1;
                img_btn_document_upload_click_browse_identity
                        .setImageDrawable(getResources().getDrawable(
                                R.drawable.ibtn_browsedoc));
                Check = "IdentityProof";
                increment = 2;

                isBrowseCapture = REQUEST_CODE_PICK_PHOTO_FILE;

                Intent intent = new Intent(DGHActivity.this, OcrActivity.class);
                startActivityForResult(intent, REQUEST_OCR);
            } else
                commonMethods.showMessageDialog(context, "Please select ID Proof");

        }
    };

    private final View.OnClickListener AddressProofOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!(spnr_document_upload_document_address.getSelectedItem().toString()).equalsIgnoreCase("Select Document")) {
                uploadFlag = 1;
                img_btn_document_upload_click_browse_address
                        .setImageDrawable(getResources().getDrawable(
                                R.drawable.ibtn_browsedoc));
                Check = "AddressProof";
                increment = 3;
                isBrowseCapture = REQUEST_CODE_PICK_PHOTO_FILE;
                Intent intent = new Intent(DGHActivity.this, OcrActivity.class);
                startActivityForResult(intent, REQUEST_OCR);
            } else
                commonMethods.showMessageDialog(context, "Please select Address Proof");

        }
    };

    private final View.OnClickListener OthersProofOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            //ClearDocumentsPDF();
            if (!TextUtils.isEmpty(edt_document_upload_document_others.getText().toString())) {
                uploadFlag = 1;
                img_btn_document_upload_click_browse_Others
                        .setImageDrawable(getResources().getDrawable(
                                R.drawable.ibtn_browsedoc));

                Check = "OtherProof";
                isBrowseCapture = REQUEST_CODE_PICK_PHOTO_FILE;
                increment = 5;
                //dispatchTakePictureIntent("0");

                Intent intent = new Intent(DGHActivity.this, OcrActivity.class);
                startActivityForResult(intent, REQUEST_OCR);
            } else
                commonMethods.showMessageDialog(context, "Please enter Other Proof Name");

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

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.ibAgentTypeUploadPhoto:
                Check = "AgentPhoto";
                capture_all_docs("2");
                /*Intent intent = new Intent(DGHActivity.this, OcrActivity.class);
                startActivityForResult(intent, REQUEST_OCR);*/
                break;
            case R.id.imagePhotoGraphGPSCord:
                Check = "UserPhoto";
                capture_all_docs("1");
                /*Intent intentPhoto = new Intent(DGHActivity.this, OcrActivity.class);
                startActivityForResult(intentPhoto, REQUEST_OCR);*/
                break;
            case R.id.buttonAddRow:
                globalDataList.add(new QuestionsValuesModel("", ""));
                selectedAdapter.notifyItemInserted(globalDataList.size() - 1);
                break;


            case R.id.buttonGetDetails:
                commonMethods.hideKeyboard(etPolicyNumber, context);
                policyNumber = etPolicyNumber.getText().toString();
                ll_dgh.setVisibility(View.GONE);
                if (TextUtils.isEmpty(policyNumber)) {
                    policyNumber = "";
                    commonMethods.showMessageDialog(context, "Please enter Policy Number");
                    commonMethods.setFocusable(etPolicyNumber);
                    etPolicyNumber.requestFocus();
                    return;
                } else if (!commonMethods.isNetworkConnected(context)) {
                    commonMethods.showMessageDialog(context, commonMethods.NO_INTERNET_MESSAGE);
                    return;
                }
                downloadPolicyDetailsAsyncTask = new DownloadPolicyDetailsAsyncTask();
                downloadPolicyDetailsAsyncTask.execute();
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
            case R.id.tvPastFromDuration:
                datecheck = 2;
                showDateDialog();
                break;
            case R.id.tvPastToDuration:
                datecheck = 3;
                showDateDialog();
                break;
            case R.id.tvPastReturnDate:
                datecheck = 4;
                showDateDialog();
                break;
            case R.id.tvFutureFromDuration:
                datecheck = 5;
                showFutureDateDialog();
                break;
            case R.id.tvFutureToDuration:
                datecheck = 6;
                showFutureDateDialog();
                break;
            case R.id.tvFutureReturnDate:
                datecheck = 7;
                showFutureDateDialog();
                break;

        }
    }

    private void validation(String flag) {

        commonMethods.hideKeyboard(etPolicyNumber, context);
        policyNumber = etPolicyNumber.getText().toString().trim();
        if (TextUtils.isEmpty(policyNumber)) {
            policyNumber = "";
            commonMethods.showMessageDialog(context, "Please enter Policy Number");
            commonMethods.setFocusable(etPolicyNumber);
            etPolicyNumber.requestFocus();
            return;
        }
        lifeAssuredName = etLifeAssuredName.getText().toString().trim();
        if (TextUtils.isEmpty(lifeAssuredName)) {
            lifeAssuredName = "";
            commonMethods.showMessageDialog(context, "Please enter Life Assured Name");
            commonMethods.setFocusable(etLifeAssuredName);
            etLifeAssuredName.requestFocus();
            return;
        }

        AGE = tvAge.getText().toString();
        if (TextUtils.isEmpty(AGE)) {
            AGE = "";
            commonMethods.showMessageDialog(context, "Please select DOB");
            commonMethods.setFocusable(tvAge);
            tvAge.requestFocus();
            return;
        }

        dobString = tvDateOfBirth.getText().toString();
        if (TextUtils.isEmpty(dobString)) {
            dobString = "";
            commonMethods.showMessageDialog(context, "Please select DOB");
            commonMethods.setFocusable(tvDateOfBirth);
            tvDateOfBirth.requestFocus();
            return;
        }


        occupation = spinnerOccupation.getSelectedItem().toString();
        if (!TextUtils.isEmpty(occupation) && occupation.equalsIgnoreCase("Select Occupation")) {
            occupation = "";
            commonMethods.showMessageDialog(context, "Please Select Occupation");
            commonMethods.setFocusable(spinnerOccupation);
            spinnerOccupation.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(residentRBString)) {

            commonMethods.showMessageDialog(context, "Please check Residence Status");
            commonMethods.setFocusable(radioGroupResidentStatus);
            radioGroupResidentStatus.requestFocus();
            return;
        }

        countryResidence = autoCompleteTextViewCountryResidence.getEditableText().toString().trim();
        if (!TextUtils.isEmpty(countryResidence) && countryResidence.equalsIgnoreCase("Select Country")) {
            commonMethods.showMessageDialog(context, "Please enter Country of Residence");
            commonMethods.setFocusable(autoCompleteTextViewCountryResidence);
            autoCompleteTextViewCountryResidence.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(countryResidence)) {
            commonMethods.showMessageDialog(context, "Please enter Country of Residence");
            commonMethods.setFocusable(autoCompleteTextViewCountryResidence);
            autoCompleteTextViewCountryResidence.requestFocus();
            return;
        }

        countryCode = etCountryCode.getText().toString().trim();
        if (TextUtils.isEmpty(countryCode)) {
            commonMethods.showMessageDialog(context, "Please enter Country Code");
            commonMethods.setFocusable(etCountryCode);
            etCountryCode.requestFocus();
            return;
        }
        if (!commonMethods.mobileNumberPatternValidation(etMobileNumber, context)) {
            MOBILE = "";
            commonMethods.showMessageDialog(context, "Please enter valid Mobile Number");
            commonMethods.setFocusable(etMobileNumber);
            etMobileNumber.requestFocus();
            return;
        } else {
            MOBILE = etMobileNumber.getText().toString().trim();
        }

        if (!commonMethods.emailPatternValidation(etEmailId, context)) {
            EMAILID = "";
            commonMethods.showMessageDialog(context, "Please enter valid Email-ID");
            commonMethods.setFocusable(etEmailId);
            etEmailId.requestFocus();
            return;
        } else {
            EMAILID = etEmailId.getText().toString().trim();
        }
        heights = etHeights.getText().toString().trim();
        if (TextUtils.isEmpty(heights)) {
            commonMethods.showMessageDialog(context, "Please enter Height(in Cms)");
            commonMethods.setFocusable(etHeights);
            etHeights.requestFocus();
            return;
        } else if (!TextUtils.isEmpty(heights) && Double.parseDouble(heights) < 132.0) {
            commonMethods.showMessageDialog(context, "Minimum height is 132(in Cms)");
            commonMethods.setFocusable(etHeights);
            etHeights.requestFocus();
            return;
        }

        weight = etWeight.getText().toString().trim();
        if (TextUtils.isEmpty(weight)) {
            commonMethods.showMessageDialog(context, "Please enter Weight(in Kgs)");
            commonMethods.setFocusable(etWeight);
            etWeight.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(healthRBString)) {
            commonMethods.showMessageDialog(context, "Please check Health Status");
            commonMethods.setFocusable(radioGroupHealthStatus);
            radioGroupHealthStatus.requestFocus();
            return;
        } else {

            if (healthRBString.equalsIgnoreCase("no")) {
                healthStatus = etHealthStatus.getText().toString().trim();
                if (TextUtils.isEmpty(healthStatus)) {
                    commonMethods.showMessageDialog(context, "Please enter Health Status");
                    commonMethods.setFocusable(etHealthStatus);
                    etHealthStatus.requestFocus();
                    return;
                }
            } else {
                healthStatus = "";
                etHealthStatus.setText("");
            }
        }

        if (TextUtils.isEmpty(weightLossRBString)) {
            commonMethods.showMessageDialog(context, "Please check Weight Loss Details");
            commonMethods.setFocusable(radioGroupWeightLoss);
            radioGroupWeightLoss.requestFocus();
            return;
        } else {

            if (weightLossRBString.equalsIgnoreCase("yes")) {
                weightLossDetails = etWeightLoss.getText().toString().trim();
                if (TextUtils.isEmpty(weightLossDetails)) {
                    commonMethods.showMessageDialog(context, "Please enter Weight Loss Details");
                    commonMethods.setFocusable(etWeightLoss);
                    etWeightLoss.requestFocus();
                    return;
                }
            } else {
                weightLossDetails = "";
                etWeightLoss.setText("");
            }
        }
        if (TextUtils.isEmpty(DateOfProposalDetailsOptionARBString)) {
            commonMethods.showMessageDialog(context, "Please check Question 3 Option (a)");
            commonMethods.setFocusable(rgDateOfProposalDetailsOptionA);
            rgDateOfProposalDetailsOptionA.requestFocus();
            return;
        } else {

            if (DateOfProposalDetailsOptionARBString.equalsIgnoreCase("yes")) {
                dateOfProposalOptionAYes = etDateOfProposalDetailsOptionAYes.getText().toString().trim();
                if (TextUtils.isEmpty(dateOfProposalOptionAYes)) {
                    commonMethods.showMessageDialog(context, "Please enter Illness / Disease Deatils");
                    commonMethods.setFocusable(etDateOfProposalDetailsOptionAYes);
                    etDateOfProposalDetailsOptionAYes.requestFocus();
                    return;
                }
            } else {
                dateOfProposalOptionAYes = "";
                etDateOfProposalDetailsOptionAYes.setText("");
            }
        }

        if (TextUtils.isEmpty(dateOfProposalOptionBRBString)) {
            commonMethods.showMessageDialog(context, "Please check Question 3 Option (b)");
            commonMethods.setFocusable(rgDateOfProposalDetailsOptionB);
            rgDateOfProposalDetailsOptionB.requestFocus();
            return;
        } else {

            if (dateOfProposalOptionBRBString.equalsIgnoreCase("yes")) {
                dateOfProposalOptionBYes = etDateOfProposalDetailsOptionBYes.getText().toString().trim();
                if (TextUtils.isEmpty(dateOfProposalOptionBYes)) {
                    commonMethods.showMessageDialog(context, "Please enter Operation, Accident or Injury Details");
                    commonMethods.setFocusable(etDateOfProposalDetailsOptionBYes);
                    etDateOfProposalDetailsOptionBYes.requestFocus();
                    return;
                }
            } else {
                dateOfProposalOptionBYes = "";
                etDateOfProposalDetailsOptionBYes.setText("");
            }
        }

        if (TextUtils.isEmpty(dateOfProposalOptionCRBString)) {
            commonMethods.showMessageDialog(context, "Please check Question 3 Option (c)");
            commonMethods.setFocusable(rgDateOfProposalDetailsOptionC);
            rgDateOfProposalDetailsOptionC.requestFocus();
            return;
        } else {

            if (dateOfProposalOptionCRBString.equalsIgnoreCase("yes")) {
                dateOfProposalOptionCYes = etDateOfProposalDetailsOptionCYes.getText().toString().trim();
                if (TextUtils.isEmpty(dateOfProposalOptionCYes)) {
                    commonMethods.showMessageDialog(context, "Please enter Examinations Deatils");
                    commonMethods.setFocusable(etDateOfProposalDetailsOptionCYes);
                    etDateOfProposalDetailsOptionCYes.requestFocus();
                    return;
                }
            } else {
                dateOfProposalOptionCYes = "";
                etDateOfProposalDetailsOptionCYes.setText("");
            }
        }

        if (TextUtils.isEmpty(multipleDiseaseRBString)) {
            commonMethods.showMessageDialog(context, "Please Answer Question 4");
            commonMethods.setFocusable(rgMultipleDiseaseDetails);
            rgMultipleDiseaseDetails.requestFocus();
            return;
        } else {

            if (multipleDiseaseRBString.equalsIgnoreCase("yes")) {
                multipleDiseaseDetails = etMultipleDiseaseDetails.getText().toString().trim();
                if (TextUtils.isEmpty(multipleDiseaseDetails)) {
                    commonMethods.showMessageDialog(context, "Please enter Disease Deatils");
                    commonMethods.setFocusable(etMultipleDiseaseDetails);
                    etMultipleDiseaseDetails.requestFocus();
                    return;
                }
            } else {
                multipleDiseaseDetails = "";
                etMultipleDiseaseDetails.setText("");
            }
        }

        if (TextUtils.isEmpty(question5NewRBString)) {
            commonMethods.showMessageDialog(context, "Please check Question 5");
            commonMethods.setFocusable(radioGroupQuestion5New);
            radioGroupQuestion5New.requestFocus();
            return;
        } else {

            if (question5NewRBString.equalsIgnoreCase("yes")) {
                question5New = etQuestion5New.getText().toString().trim();
                if (TextUtils.isEmpty(question5New)) {
                    commonMethods.showMessageDialog(context, "Please enter Proposal Rejection Details");
                    commonMethods.setFocusable(etQuestion5New);
                    etQuestion5New.requestFocus();
                    return;
                }
            } else {
                question5New = "";
                etQuestion5New.setText("");
            }
        }

        if (TextUtils.isEmpty(question6NewRBString)) {
            commonMethods.showMessageDialog(context, "Please check Question 6");
            commonMethods.setFocusable(radioGroupQuestion6New);
            radioGroupQuestion6New.requestFocus();
            return;
        } else {

            if (question6NewRBString.equalsIgnoreCase("yes")) {
                question6New = etQuestion6New.getText().toString().trim();
                if (TextUtils.isEmpty(question6New)) {
                    commonMethods.showMessageDialog(context, "Please enter Hazardous Occupation Deatils");
                    commonMethods.setFocusable(etQuestion6New);
                    etQuestion6New.requestFocus();
                    return;
                }
            } else {
                question6New = "";
                etQuestion6New.setText("");
            }
        }

        if (TextUtils.isEmpty(question7NewRBString)) {
            commonMethods.showMessageDialog(context, "Please check Question 7");
            commonMethods.setFocusable(radioGroupQuestion7New);
            radioGroupQuestion7New.requestFocus();
            return;
        } else {

            if (question7NewRBString.equalsIgnoreCase("yes")) {
                question7New = etQuestion7New.getText().toString().trim();
                if (TextUtils.isEmpty(question7New)) {
                    commonMethods.showMessageDialog(context, "Please enter PEP Deatils");
                    commonMethods.setFocusable(etQuestion7New);
                    etQuestion7New.requestFocus();
                    return;
                }
            } else {
                question7New = "";
                etQuestion7New.setText("");
            }
        }

        if (TextUtils.isEmpty(question8NewARBString)) {
            commonMethods.showMessageDialog(context, "Please check Question 8 (a)");
            commonMethods.setFocusable(radioGroupQuestion8NewA);
            radioGroupQuestion8NewA.requestFocus();
            return;
        } else {

            if (question8NewARBString.equalsIgnoreCase("yes")) {
                question8NewA = etQuestion8NewA.getText().toString().trim();
                if (TextUtils.isEmpty(question8NewA)) {
                    commonMethods.showMessageDialog(context, "Please enter Crime Details");
                    commonMethods.setFocusable(etQuestion8NewA);
                    etQuestion8NewA.requestFocus();
                    return;
                }
            } else {
                question8NewA = "";
                etQuestion8NewA.setText("");
            }
        }

        if (TextUtils.isEmpty(question8NewBRBString)) {
            commonMethods.showMessageDialog(context, "Please check Question 8 (b)");
            commonMethods.setFocusable(radioGroupQuestion8NewB);
            radioGroupQuestion8NewB.requestFocus();
            return;
        } else {

            if (question8NewBRBString.equalsIgnoreCase("yes")) {
                question8NewB = etQuestion8NewB.getText().toString().trim();
                if (TextUtils.isEmpty(question8NewB)) {
                    commonMethods.showMessageDialog(context, "Please enter Criminal Proceedings Deatils");
                    commonMethods.setFocusable(etQuestion8NewB);
                    etQuestion8NewB.requestFocus();
                    return;
                }
            } else {
                question8NewB = "";
                etQuestion8NewB.setText("");
            }
        }
        if (TextUtils.isEmpty(question8NewCRBString)) {
            commonMethods.showMessageDialog(context, "Please check Question 8 (c)");
            commonMethods.setFocusable(radioGroupQuestion8NewC);
            radioGroupQuestion8NewC.requestFocus();
            return;
        } else {

            if (question8NewCRBString.equalsIgnoreCase("yes")) {
                question8NewC = etQuestion8NewC.getText().toString().trim();
                if (TextUtils.isEmpty(question8NewC)) {
                    commonMethods.showMessageDialog(context, "Please enter Criminal Conviction Deatils");
                    commonMethods.setFocusable(etQuestion8NewC);
                    etQuestion8NewC.requestFocus();
                    return;
                }
            } else {
                question8NewC = "";
                etQuestion8NewC.setText("");
            }
        }

        if (TextUtils.isEmpty(question9NewRBString)) {
            commonMethods.showMessageDialog(context, "Please check Question 9");
            commonMethods.setFocusable(radioGroupQuestion9New);
            radioGroupQuestion9New.requestFocus();
            return;
        } else {

            if (question9NewRBString.equalsIgnoreCase("yes")) {
                question9New = etQuestion9New.getText().toString().trim();
                if (TextUtils.isEmpty(question9New)) {
                    commonMethods.showMessageDialog(context, "Please enter Hazardous Sports Deatils");
                    commonMethods.setFocusable(etQuestion9New);
                    etQuestion9New.requestFocus();
                    return;
                }
            } else {
                question9New = "";
                etQuestion9New.setText("");
            }
        }

        if (TextUtils.isEmpty(question10A1RBString)) {
            commonMethods.showMessageDialog(context, "Please check Question 10 (a) Tobaco Details");
            commonMethods.setFocusable(radioGroupQuestion10A1);
            radioGroupQuestion10A1.requestFocus();
            return;
        } else {

            if (question10A1RBString.equalsIgnoreCase("yes")) {
                sticksPackets10A1 = etSticksPackets10A1.getText().toString().trim();
                noOfYearsUsed10A1 = etNoOfYearsUsed10A1.getText().toString().trim();
                if (TextUtils.isEmpty(sticksPackets10A1)) {
                    commonMethods.showMessageDialog(context, "Please enter Sticks/Packets Per day");
                    commonMethods.setFocusable(etSticksPackets10A1);
                    etSticksPackets10A1.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(noOfYearsUsed10A1)) {
                    commonMethods.showMessageDialog(context, "Please enter number of years used");
                    commonMethods.setFocusable(etNoOfYearsUsed10A1);
                    etNoOfYearsUsed10A1.requestFocus();
                    return;
                }
            } else {
                sticksPackets10A1 = "";
                noOfYearsUsed10A1 = "";
                etSticksPackets10A1.setText("");
                etNoOfYearsUsed10A1.setText("");
            }
        }

        if (TextUtils.isEmpty(question10A2RBString)) {
            commonMethods.showMessageDialog(context, "Please check Question 10 (a) Alcohol Details");
            commonMethods.setFocusable(radioGroupQuestion10A2);
            radioGroupQuestion10A2.requestFocus();
            return;
        } else {

            if (question10A2RBString.equalsIgnoreCase("yes")) {
                quantity10A2AlcoholType = etQuantity10A2AlcoholType.getText().toString().trim();
                quantity10A2Ml = etQuantity10A2Ml.getText().toString().trim();
                noOfYearsUsed10A2 = etNoOfYearsUsed10A2.getText().toString().trim();
                if (TextUtils.isEmpty(quantity10A2AlcoholType)) {
                    commonMethods.showMessageDialog(context, "Please enter Kind of Alcohol");
                    commonMethods.setFocusable(etQuantity10A2AlcoholType);
                    etQuantity10A2AlcoholType.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(quantity10A2Ml)) {
                    commonMethods.showMessageDialog(context, "Please enter Quantity Per Week in ml");
                    commonMethods.setFocusable(etQuantity10A2Ml);
                    etQuantity10A2Ml.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(noOfYearsUsed10A2)) {
                    commonMethods.showMessageDialog(context, "Please enter number of years used");
                    commonMethods.setFocusable(etNoOfYearsUsed10A2);
                    etNoOfYearsUsed10A2.requestFocus();
                    return;
                }
            } else {
                quantity10A2AlcoholType = "";
                quantity10A2Ml = "";
                noOfYearsUsed10A2 = "";
                etQuantity10A2AlcoholType.setText("");
                etQuantity10A2Ml.setText("");
                etNoOfYearsUsed10A2.setText("");
            }
        }

        if (TextUtils.isEmpty(question10A3RBString)) {
            commonMethods.showMessageDialog(context, "Please check Question 10 (a) Narcotics Details");
            commonMethods.setFocusable(radioGroupQuestion10A3);
            radioGroupQuestion10A3.requestFocus();
            return;
        } else {

            if (question10A3RBString.equalsIgnoreCase("yes")) {
                typeOfDrug10A3 = etTypeOfDrug10A3.getText().toString().trim();
                noOfYearsUsed10A3 = etNoOfYearsUsed10A3.getText().toString().trim();

                if (TextUtils.isEmpty(typeOfDrug10A3)) {
                    commonMethods.showMessageDialog(context, "Please enter type of drug");
                    commonMethods.setFocusable(etTypeOfDrug10A3);
                    etTypeOfDrug10A3.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(noOfYearsUsed10A3)) {
                    commonMethods.showMessageDialog(context, "Please enter Number of years used");
                    commonMethods.setFocusable(etNoOfYearsUsed10A3);
                    etNoOfYearsUsed10A3.requestFocus();
                    return;
                }


            } else {
                typeOfDrug10A3 = "";
                noOfYearsUsed10A3 = "";
                etTypeOfDrug10A3.setText("");
                etNoOfYearsUsed10A3.setText("");
            }
        }


        consumptionQuit10B = etConsumptionQuit10B.getText().toString().trim();
        if (TextUtils.isEmpty(consumptionQuit10B)) {
            commonMethods.showMessageDialog(context, "Please enter above Consumption quit details if any");
            commonMethods.setFocusable(etConsumptionQuit10B);
            etConsumptionQuit10B.requestFocus();
            return;
        }

        GENDER = spinnerGender.getSelectedItem().toString();

        if (GENDER.equalsIgnoreCase("female")) {
            if (TextUtils.isEmpty(question11ARBString)) {
                commonMethods.showMessageDialog(context, "Please check Question 11 (a)");
                commonMethods.setFocusable(radioGroupQuestion11A);
                radioGroupQuestion11A.requestFocus();
                return;
            } else {

                if (question11ARBString.equalsIgnoreCase("yes")) {
                    pregnancyMonth11A = etPregnancyMonth11A.getText().toString().trim();

                    if (TextUtils.isEmpty(pregnancyMonth11A)) {
                        commonMethods.showMessageDialog(context, "Please enter Pregnancy Month");
                        commonMethods.setFocusable(etPregnancyMonth11A);
                        etPregnancyMonth11A.requestFocus();
                        return;
                    }

                } else {
                    pregnancyMonth11A = "";
                    etPregnancyMonth11A.setText("");
                }
            }

            if (TextUtils.isEmpty(question11BRBString)) {
                commonMethods.showMessageDialog(context, "Please check Question 11 (b)");
                commonMethods.setFocusable(radioGroupQuestion11B);
                radioGroupQuestion11B.requestFocus();
                return;
            } else {

                if (question11BRBString.equalsIgnoreCase("yes")) {
                    gynecologicalProblem11B = etGynecologicalProblem11B.getText().toString().trim();

                    if (TextUtils.isEmpty(gynecologicalProblem11B)) {
                        commonMethods.showMessageDialog(context, "Please enter gynecological problems details");
                        commonMethods.setFocusable(etGynecologicalProblem11B);
                        etGynecologicalProblem11B.requestFocus();
                        return;
                    }

                } else {
                    gynecologicalProblem11B = "";
                    etGynecologicalProblem11B.setText("");
                }
            }
        }

        if (TextUtils.isEmpty(pastForeignVisit)) {
            commonMethods.showMessageDialog(context, "Please check Question 1 of COVID Questionnaire");
            commonMethods.setFocusable(radioGroupPastForeignVisit);
            radioGroupPastForeignVisit.requestFocus();
            return;
        } else {

            if (pastForeignVisit.equalsIgnoreCase("yes")) {
                pastCountryName = etPastCountryName.getText().toString().trim();
                pastFromDuration = tvPastFromDuration.getText().toString();
                pastToDuration = tvPastToDuration.getText().toString();
                pastReturnDate = tvPastReturnDate.getText().toString();

                Date past_from_dt = null, past_to_dt = null, past_returnDate = null;

                try {
                    final SimpleDateFormat formatter = new SimpleDateFormat(
                            "dd-MMMM-yyyy");
                    past_from_dt = formatter.parse(pastFromDuration);
                    past_to_dt = formatter.parse(pastToDuration);
                    past_returnDate = formatter.parse(pastReturnDate);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (TextUtils.isEmpty(pastCountryName)) {
                    commonMethods.showMessageDialog(context, "Please enter name of Country visited after 1.1.2020");
                    commonMethods.setFocusable(etPastCountryName);
                    etPastCountryName.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(pastFromDuration)) {
                    commonMethods.showMessageDialog(context, "Please enter Duration");
                    commonMethods.setFocusable(tvPastFromDuration);
                    tvPastFromDuration.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(pastToDuration)) {
                    commonMethods.showMessageDialog(context, "Please enter Duration");
                    commonMethods.setFocusable(tvPastToDuration);
                    tvPastToDuration.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(pastReturnDate)) {
                    commonMethods.showMessageDialog(context, "Please enter return Date");
                    commonMethods.setFocusable(tvPastReturnDate);
                    tvPastReturnDate.requestFocus();
                    return;
                }
                if ((past_from_dt != null && past_from_dt.after(past_to_dt)) || (past_from_dt != null && past_from_dt.equals(past_to_dt))) {
                    commonMethods.showMessageDialog(context, "To Date cannot be less than or equal to From Date");
                    commonMethods.setFocusable(tvPastFromDuration);
                    tvPastFromDuration.requestFocus();
                }
                if ((past_returnDate != null && past_to_dt.after(past_returnDate))) {
                    commonMethods.showMessageDialog(context, "Return Date cannot be less than To Date");
                    tvPastReturnDate.setFocusable(true);
                    tvPastReturnDate.setFocusableInTouchMode(true);
                    tvPastReturnDate.requestFocus();
                }

                if (TextUtils.isEmpty(screeneAtAirport)) {
                    commonMethods.showMessageDialog(context, "Please check Question 1.4 of COVID Questionnaire");
                    commonMethods.setFocusable(radioGroupScreenedAtAirport);
                    radioGroupScreenedAtAirport.requestFocus();
                    return;
                }

            } else {
                pastCountryName = "";
                pastFromDuration = "";
                pastToDuration = "";
                pastReturnDate = "";
                screeneAtAirport = "";
                etPastCountryName.setText("");
                tvPastFromDuration.setText("");
                tvPastToDuration.setText("");
                tvPastReturnDate.setText("");
            }
        }

        if (TextUtils.isEmpty(testedForCovid)) {
            commonMethods.showMessageDialog(context, "Please check Question 2 of COVID Questionnaire");
            commonMethods.setFocusable(radioGroupTestedForCovid);
            radioGroupTestedForCovid.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(homeQuarantine)) {
            commonMethods.showMessageDialog(context, "Please check Question 3.1 of COVID Questionnaire");
            commonMethods.setFocusable(radioGroupHomeQuarantine);
            radioGroupHomeQuarantine.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(underObservation)) {
            commonMethods.showMessageDialog(context, "Please check Question 3.2 of COVID Questionnaire");
            radioGroupunderObservation.setFocusable(true);
            radioGroupunderObservation.setFocusableInTouchMode(true);
            radioGroupunderObservation.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(selfIsolation)) {
            commonMethods.showMessageDialog(context, "Please check Question 3.3 of COVID Questionnaire");
            commonMethods.setFocusable(radioGroupSelfIsolation);
            radioGroupSelfIsolation.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(futureForeignVisit)) {
            commonMethods.showMessageDialog(context, "Please check Question 4 of COVID Questionnaire");
            commonMethods.setFocusable(radioGroupFutureForeignVisit);
            radioGroupFutureForeignVisit.requestFocus();
            return;
        } else {

            if (futureForeignVisit.equalsIgnoreCase("yes")) {
                futureCountryName = etFutureCountryname.getText().toString();
                futureFromDuration = tvFutureFromDuration.getText().toString();
                futureToDuration = tvFutureToDuration.getText().toString();
                futureReturnDate = tvFutureReturnDate.getText().toString();

                Date future_from_dt = null, future_to_dt = null, future_returnDate = null;

                try {
                    final SimpleDateFormat formatter = new SimpleDateFormat(
                            "dd-MMMM-yyyy");
                    future_from_dt = formatter.parse(futureFromDuration);
                    future_to_dt = formatter.parse(futureToDuration);
                    future_returnDate = formatter.parse(futureReturnDate);

                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (TextUtils.isEmpty(futureCountryName)) {
                    commonMethods.showMessageDialog(context, "Please enter name of Country");
                    commonMethods.setFocusable(etFutureCountryname);
                    etFutureCountryname.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(futureFromDuration)) {
                    commonMethods.showMessageDialog(context, "Please enter Duration");
                    commonMethods.setFocusable(tvFutureFromDuration);
                    tvFutureFromDuration.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(futureToDuration)) {
                    commonMethods.showMessageDialog(context, "Please enter Duration");
                    commonMethods.setFocusable(tvFutureToDuration);
                    tvFutureToDuration.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(futureReturnDate)) {
                    commonMethods.showMessageDialog(context, "Please enter return Date");
                    commonMethods.setFocusable(tvFutureReturnDate);
                    tvFutureReturnDate.requestFocus();
                    return;
                }

                if ((future_from_dt != null && future_from_dt.after(future_to_dt)) || (future_from_dt != null && future_from_dt.equals(future_to_dt))) {
                    commonMethods.showMessageDialog(context, "To Date cannot be less than or equal to From Date");
                    commonMethods.setFocusable(tvFutureFromDuration);
                    tvFutureFromDuration.requestFocus();
                }
                if ((future_returnDate != null && future_to_dt.after(future_returnDate))) {
                    commonMethods.showMessageDialog(context, "Return Date cannot be less than To Date");
                    commonMethods.setFocusable(tvFutureReturnDate);
                    tvFutureReturnDate.requestFocus();
                }

            } else {
                futureCountryName = "";
                futureFromDuration = "";
                futureToDuration = "";
                futureReturnDate = "";
                etFutureCountryname.setText("");
                tvFutureFromDuration.setText("");
                tvFutureToDuration.setText("");
                tvFutureReturnDate.setText("");
            }
        }


        if (TextUtils.isEmpty(familyMemberSymptoms)) {
            commonMethods.showMessageDialog(context, "Please check Question 5 of COVID Questionnaire");
            commonMethods.setFocusable(radioGroupSymptoms);
            radioGroupSymptoms.requestFocus();
            return;
        }


        if (TextUtils.isEmpty(respiratorySymptoms)) {
            commonMethods.showMessageDialog(context, "Please check Question 6 of COVID Questionnaire");
            commonMethods.setFocusable(radioGroupRespiratorySymptoms);
            radioGroupRespiratorySymptoms.requestFocus();
            return;
        }


        if (TextUtils.isEmpty(contactWithCorona)) {
            commonMethods.showMessageDialog(context, "Please check Question 7 of COVID Questionnaire");
            commonMethods.setFocusable(radioGroupContactWithCorona);
            radioGroupContactWithCorona.requestFocus();
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
            commonMethods.showMessageDialog(context, "Please Capture Life Assured Photo");
            commonMethods.setFocusable(imagePhotoGraphGPSCord);
            imagePhotoGraphGPSCord.requestFocus();
            return;
        }

        if (agentPhotoBitmap == null) {
            commonMethods.showMessageDialog(context, "Please Capture Agent Photo");
            commonMethods.setFocusable(ibAgentTypeUploadPhoto);
            ibAgentTypeUploadPhoto.requestFocus();
            return;
        }

        //if (otherFormFillerRBString.equalsIgnoreCase("yes")) {
        if (!checkBoxOtherUserDeclaration.isChecked()) {
            commonMethods.showMessageDialog(context, "Please accept the declaration");
            commonMethods.setFocusable(checkBoxOtherUserDeclaration);
            checkBoxOtherUserDeclaration.requestFocus();
            return;
        }

        languageSelected = spinnerLanguages.getSelectedItem().toString();
        if (languageSelected.equalsIgnoreCase("Select Language")) {
            commonMethods.showMessageDialog(context, "Please Select Language");
            commonMethods.setFocusable(spinnerLanguages);
            spinnerLanguages.requestFocus();
            return;
        }

        if (!checkBoxOtherUserDeclarationSecond.isChecked()) {
            commonMethods.showMessageDialog(context, "Please accept the declaration");
            commonMethods.setFocusable(checkBoxOtherUserDeclarationSecond);
            checkBoxOtherUserDeclarationSecond.requestFocus();
            return;
        }

        place = etPlace.getText().toString().trim();
        if (TextUtils.isEmpty(place)) {
            commonMethods.showMessageDialog(context, "Please enter the Place");
            commonMethods.setFocusable(etPlace);
            etPlace.requestFocus();
            return;
        }
        if (flag.equalsIgnoreCase("Generate")) {
            generateOTPGeneralAsyncTask = new GenerateOTPGeneralAsyncTask(MOBILE, this, context);
            generateOTPGeneralAsyncTask.execute();
        } else {
            validateOTPGeneralAsyncTask = new ValidateOTPGeneralAsyncTask(MOBILE,
                    OTP, this, context);
            validateOTPGeneralAsyncTask.execute();
        }

    }

    private String generateXmlString() {

        /*HD_COVID_NAME_COUNTRY_VISITED
                HD_COVID_VISIT_DURA_FRm_Dt
        HD_COVID_VISIT_DURA_TO_Dt
                HD_COVID_Dt_OF_REt_TO_INDIA
        HD_COVID_SCREEN_AT_AIRPORT
                HD_COVID_TEST_FOR_COVID19
        HD_COVID_KEPT_HOME_QUARTINE
                HD_COVID_KEPT_UNDER_OBSERV
        HD_COVID_KEPT_HOME_ISOLAT
                HD_COVID_PLN_TRA_FORE_COUNRTY
        HD_COVID_NM_COUN_VIS_NXT_6MON
                HD_COVID_VIS_DUR_FR_DT_NX_6MN
        HD_COVID_VIS_DUR_TO_DT_NX_6MN
                HD_COVID_DT_RE_TO_IND_NXT_6MN
        HD_COVID_FMLY_SUFF_ANY_SYMP
                HD_COVID_FMLY_UND_ANY_TEst
        HD_COVID_FMLY_CASE_CORONA
                HD_COVID_VISIT_ANYFORE_CNTRY*/
        String xmlTags = "<?xml version='1.0' encoding='utf-8' ?><DGH>" +
                "<policyNumber>" + policyNumber + "</policyNumber>" +
                "<lifeAssuredName>" + lifeAssuredName + "</lifeAssuredName>" +
                "<DOB>" + commonMethods.formatDateForerver(dobString) + "</DOB>" +
                "<age>" + AGE + "</age>" +
                "<gender>" + GENDER + "</gender>" +
                "<occupation>" + occupation + "</occupation>" +
                "<residenceStatus>" + residentRBString + "</residenceStatus>" +
                "<countryOfResidence>" + countryResidence + "</countryOfResidence>" +
                "<countryCode>" + countryCode + "</countryCode>" +
                "<mobileNumber>" + MOBILE + "</mobileNumber>" +
                "<emailId>" + EMAILID + "</emailId>" +
                "<height>" + heights + "</height>" +
                "<weight>" + weight + "</weight>" +

                "<healthQ1YesNo>" + healthRBString + "</healthQ1YesNo>" +
                "<healthQ1NoAnswer>" + healthStatus + "</healthQ1NoAnswer>" +
                "<weightLossGainQ2YesNo>" + weightLossRBString + "</weightLossGainQ2YesNo>" +
                "<weightLossGainQ2YesAnswer>" + weightLossDetails + "</weightLossGainQ2YesAnswer>" +

                "<postPolicyQ3AYesNo>" + DateOfProposalDetailsOptionARBString + "</postPolicyQ3AYesNo>" +
                "<postPolicyQ3AYesAnswer>" + dateOfProposalOptionAYes + "</postPolicyQ3AYesAnswer>" +
                "<postPolicyQ3BYesNo>" + dateOfProposalOptionBRBString + "</postPolicyQ3BYesNo>" +
                "<postPolicyQ3BYesAnswer>" + dateOfProposalOptionBYes + "</postPolicyQ3BYesAnswer>" +
                "<postPolicyQ3CYesNo>" + dateOfProposalOptionCRBString + "</postPolicyQ3CYesNo>" +
                "<postPolicyQ3CYesAnswer>" + dateOfProposalOptionCYes + "</postPolicyQ3CYesAnswer>" +

                "<multipleDiseaseQ4YesNo>" + multipleDiseaseRBString + "</multipleDiseaseQ4YesNo>" +
                "<multipleDiseaseQ4YesAnswer>" + multipleDiseaseDetails + "</multipleDiseaseQ4YesAnswer>" +

                "<proposalRejAcceptQ5YesNo>" + question5NewRBString + "</proposalRejAcceptQ5YesNo>" +
                "<proposalRejAcceptQ5YesAnswer>" + question5New + "</proposalRejAcceptQ5YesAnswer>" +

                "<hazardousOccupationQ6YesNO>" + question6NewRBString + "</hazardousOccupationQ6YesNO>" +
                "<hazardousOccupationQ6YesAnswer>" + question6New + "</hazardousOccupationQ6YesAnswer>" +

                "<PEPDetailsQ7YesNo>" + question7NewRBString + "</PEPDetailsQ7YesNo>" +
                "<PEPDetailsQ7YesAnswer>" + question7New + "</PEPDetailsQ7YesAnswer>" +

                "<crimeDetailsQ8AYesNO>" + question8NewARBString + "</crimeDetailsQ8AYesNO>" +
                "<crimeDetailsQ8AYesAnswer>" + question8NewA + "</crimeDetailsQ8AYesAnswer>" +
                "<crimeDetailsQ8BYesNO>" + question8NewBRBString + "</crimeDetailsQ8BYesNO>" +
                "<crimeDetailsQ8BYesAnswer>" + question8NewB + "</crimeDetailsQ8BYesAnswer>" +
                "<crimeDetailsQ8CYesNO>" + question8NewCRBString + "</crimeDetailsQ8CYesNO>" +
                "<crimeDetailsQ8CYesAnswer>" + question8NewC + "</crimeDetailsQ8CYesAnswer>" +

                "<hazardousSportsQ9YesNO>" + question9NewRBString + "</hazardousSportsQ9YesNO>" +
                "<hazardousSportsQ9YesAnswer>" + question9New + "</hazardousSportsQ9YesAnswer>" +

                "<CDQ10ATobaccoYesNO>" + question10A1RBString + "</CDQ10ATobaccoYesNO>" +
                "<CDQ10ATobaccoSticksPacketsPerDay>" + sticksPackets10A1 + "</CDQ10ATobaccoSticksPacketsPerDay>" +
                "<CDQ10ATobaccoNoYears>" + noOfYearsUsed10A1 + "</CDQ10ATobaccoNoYears>" +

                "<CDQ10AAlcoholYesNO>" + question10A2RBString + "</CDQ10AAlcoholYesNO>" +
                "<CDQ10AAlcoholKind>" + quantity10A2AlcoholType + "</CDQ10AAlcoholKind>" +
                "<CDQ10AAlcoholNoYears>" + noOfYearsUsed10A2 + "</CDQ10AAlcoholNoYears>" +
                "<CDQ10AAlcoholQuantity>" + quantity10A2Ml + "</CDQ10AAlcoholQuantity>" +

                "<CDQ10ANarcoticsYesNO>" + question10A3RBString + "</CDQ10ANarcoticsYesNO>" +
                "<CDQ10ANarcoticsType>" + typeOfDrug10A3 + "</CDQ10ANarcoticsType>" +
                "<CDQ10ANarcoticsNoYears>" + noOfYearsUsed10A3 + "</CDQ10ANarcoticsNoYears>" +

                "<CDQuitQ10BAnswer>" + consumptionQuit10B + "</CDQuitQ10BAnswer>";


        xmlTags += "<addressProofDetails>" + addressProofDOCDetails + "</addressProofDetails>" +
                "<idProofDetails>" + IDProofDOCDetails + "</idProofDetails>" +
                "<otherProofDetails>" + otherDocDetailsString + "</otherProofDetails>";


        xmlTags += "<HD_COVID_NAME_COUNTRY_VISITED>" + pastCountryName + "</HD_COVID_NAME_COUNTRY_VISITED>" +
                "<HD_COVID_VISIT_DURA_FRm_Dt>" + (TextUtils.isEmpty(pastFromDuration) ? "" : commonMethods.formatDateForerver(pastFromDuration)) + "</HD_COVID_VISIT_DURA_FRm_Dt>" +
                "<HD_COVID_VISIT_DURA_TO_Dt>" + (TextUtils.isEmpty(pastToDuration) ? "" : commonMethods.formatDateForerver(pastToDuration)) + "</HD_COVID_VISIT_DURA_TO_Dt>" +
                "<HD_COVID_Dt_OF_REt_TO_INDIA>" + (TextUtils.isEmpty(pastReturnDate) ? "" : commonMethods.formatDateForerver(pastReturnDate)) + "</HD_COVID_Dt_OF_REt_TO_INDIA>" +
                "<HD_COVID_SCREEN_AT_AIRPORT>" + screeneAtAirport + "</HD_COVID_SCREEN_AT_AIRPORT>" +
                "<HD_COVID_TEST_FOR_COVID19>" + testedForCovid + "</HD_COVID_TEST_FOR_COVID19>" +
                "<HD_COVID_KEPT_HOME_QUARTINE>" + homeQuarantine + "</HD_COVID_KEPT_HOME_QUARTINE>" +
                "<HD_COVID_KEPT_UNDER_OBSERV>" + underObservation + "</HD_COVID_KEPT_UNDER_OBSERV>" +
                "<HD_COVID_KEPT_HOME_ISOLAT>" + selfIsolation + "</HD_COVID_KEPT_HOME_ISOLAT>" +
                "<HD_COVID_PLN_TRA_FORE_COUNRTY>" + futureForeignVisit + "</HD_COVID_PLN_TRA_FORE_COUNRTY>" +
                "<HD_COVID_NM_COUN_VIS_NXT_6MON>" + futureCountryName + "</HD_COVID_NM_COUN_VIS_NXT_6MON>" +
                "<HD_COVID_VIS_DUR_FR_DT_NX_6MN>" + (TextUtils.isEmpty(futureFromDuration) ? "" : commonMethods.formatDateForerver(futureFromDuration)) + "</HD_COVID_VIS_DUR_FR_DT_NX_6MN>" +
                "<HD_COVID_VIS_DUR_TO_DT_NX_6MN>" + (TextUtils.isEmpty(futureToDuration) ? "" : commonMethods.formatDateForerver(futureToDuration)) + "</HD_COVID_VIS_DUR_TO_DT_NX_6MN>" +
                "<HD_COVID_DT_RE_TO_IND_NXT_6MN>" + (TextUtils.isEmpty(futureReturnDate) ? "" : commonMethods.formatDateForerver(futureReturnDate)) + "</HD_COVID_DT_RE_TO_IND_NXT_6MN>" +
                "<HD_COVID_FMLY_SUFF_ANY_SYMP>" + familyMemberSymptoms + "</HD_COVID_FMLY_SUFF_ANY_SYMP>" +
                "<HD_COVID_FMLY_UND_ANY_TEst>" + respiratorySymptoms + "</HD_COVID_FMLY_UND_ANY_TEst>" +
                "<HD_COVID_FMLY_CASE_CORONA>" + contactWithCorona + "</HD_COVID_FMLY_CASE_CORONA>" +
                "<HD_COVID_VISIT_ANYFORE_CNTRY>" + pastForeignVisit + "</HD_COVID_VISIT_ANYFORE_CNTRY>";
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdp = new SimpleDateFormat("MM-dd-yyyy");
        String str_created_date = sdp.format(new Date(cal.getTimeInMillis()));
        xmlTags += "<createddate>" + str_created_date + "</createddate>";

        if (GENDER.equalsIgnoreCase("female")) {
            xmlTags += "<pregnancyStatusQ11AYesNo>" + question11ARBString + "</pregnancyStatusQ11AYesNo>" +
                    "<pregnancyStatusQ11AYesAnswer>" + pregnancyMonth11A + "</pregnancyStatusQ11AYesAnswer>" +
                    "<gynecologicalQ11BYesNo>" + question11BRBString + "</gynecologicalQ11BYesNo>" +
                    "<gynecologicalQ11BYesAnswer>" + gynecologicalProblem11B + "</gynecologicalQ11BYesAnswer>";
        }
        xmlTags += "</DGH>";
        System.out.println("xmlTags:" + xmlTags);
        return xmlTags;
    }


   /* class GenerateOTPAsyncTask extends AsyncTask<String, String, String> {
        private final String SOAP_ACTION_GENERATE_PASSCODE = "http://tempuri.org/GenerateOTP_SMRT";
        private final String METHOD_NAME_GENERATE_PASSCODE = "GenerateOTP_SMRT";//GenerateOTP_SBIL
        int flag = 0;
        private volatile boolean running = true;

        //,mergedNACBIFile;
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
                SoapObject request = new SoapObject(NAMESPACE,
                        METHOD_NAME_GENERATE_PASSCODE);
                //string ,string , string , string , string , string
                request.addProperty("strQuot", policyNumber);
                request.addProperty("strProposalNo", policyNumber);
                request.addProperty("strMobile", strCIFBDMMObileNo);
                request.addProperty("strEmailId", strCIFBDMEmailId);
                request.addProperty("strMobileNo", strCIFBDMMObileNo);
                request.addProperty("strAuthKey", commonMethods.getStrAuth());

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);
                envelope.dotNet = true;

                envelope.setOutputSoapObject(request);

                commonMethods.TLSv12Enable();

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);

                HttpTransportSE androidHttpTranport = new HttpTransportSE(URl);

                androidHttpTranport.call(SOAP_ACTION_GENERATE_PASSCODE, envelope);

                SoapPrimitive sa;

                sa = (SoapPrimitive) envelope.getResponse();
                String inputpolicylist = sa.toString();

                if (inputpolicylist.equalsIgnoreCase("0") || inputpolicylist.equalsIgnoreCase("2")) {
                    flag = 0;
                } else {
                    flag = 1;
                }

            } catch (Exception e) {
                e.printStackTrace();
                running = false;
            }
            return null;

        }

        @Override
        protected void onPostExecute(String unused) {


            if (running) {
                if (flag == 1) {
                    buttonValidateOTP.setEnabled(true);
                    buttonValidateOTP.setBackgroundColor(Color.parseColor("#00a1e3"));

                    buttonGenerateOTP.setEnabled(false);
                    buttonGenerateOTP.setBackgroundColor(Color.parseColor("#A9A9A9"));
                    etOTP.setVisibility(View.VISIBLE);
                    etOTP.setFocusable(true);
                    etOTP.requestFocus();
                    commonMethods.showMessageDialog(context, "OTP sent succesfully to your Mobile Number.");

                } else {
                    commonMethods.showMessageDialog(context, "Please try after sometime");
                }
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();

            } else {
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
                commonMethods.showMessageDialog(context, "Please try after sometime");
            }
        }
    }*/


    public void generateOTPAsynResultMethod(int flag) {
        if (flag == 1) {
            buttonValidateOTP.setEnabled(true);
            buttonValidateOTP.setBackgroundColor(Color.parseColor("#00a1e3"));

            buttonGenerateOTP.setEnabled(false);
            buttonGenerateOTP.setBackgroundColor(Color.parseColor("#A9A9A9"));
            etOTP.setVisibility(View.VISIBLE);
            etOTP.setFocusable(true);
            etOTP.requestFocus();
            commonMethods.showMessageDialog(context, "OTP sent succesfully to your Mobile Number.");

        } else {
            commonMethods.showMessageDialog(context, "Please try after sometime");
        }
    }


    public void validateOTPAsynResultMethod(int flag) {
        if (flag == 1) {
            authenticatePDFAsync = new AuthenticatePDFAsync("PDF");
            authenticatePDFAsync.execute();
        } else {
            buttonGenerateOTP.setEnabled(true);
            buttonGenerateOTP.setBackgroundColor(Color.parseColor("#00a1e3"));
            commonMethods.showMessageDialog(context, "Invalid Passcode. Please re-enter Passcode");
        }
    }

    class AuthenticatePDFAsync extends AsyncTask<String, String, String> {
        private final String passCode;
        int flag = 0;
        private volatile boolean running = true;
        private File newFilePath;

        private AuthenticatePDFAsync(String passCode) {
            this.passCode = passCode;
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
                if (passCode.equalsIgnoreCase("PDF")) {
                    // createCOVIDPDF();
                    fetchQuestionData();
                    createDGHPDF();
                }
                flag = 1;

            } catch (Exception e) {
                flag = 0;
                e.printStackTrace();
            }

            return null;

        }

        @Override
        protected void onPostExecute(String unused) {
            if (mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }

            if (running) {
                if (flag == 1) {
                    if (passCode.equalsIgnoreCase("PDF")) {
                        saveDGHDataAsyncTask = new SaveDGHDataAsyncTask();
                        saveDGHDataAsyncTask.execute();
                    }
                } else {
                    buttonGenerateOTP.setEnabled(true);
                    buttonGenerateOTP.setBackgroundColor(Color.parseColor("#00a1e3"));
                    commonMethods.showMessageDialog(context, "Please try after sometime");
                }
            } else {
                buttonGenerateOTP.setEnabled(true);
                buttonGenerateOTP.setBackgroundColor(Color.parseColor("#00a1e3"));
                commonMethods.showMessageDialog(context, "Please try after sometime");
            }
        }
    }

    class SaveDGHDataAsyncTask extends AsyncTask<String, String, String> {

        private volatile boolean running = true;
        private String result = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            if (mProgressDialog != null && !mProgressDialog.isShowing()) {
                mProgressDialog.show();
            }

        }

        @SuppressLint("SimpleDateFormat")
        @Override
        protected String doInBackground(String... aurl) {
            try {

                running = true;
                SoapObject request;
                // saveeDGH_smrt(string xmlStr, string strEmailId, string strMobileNo, string strAuthKey)

                String NAMESPACE = "http://tempuri.org/";
                String METHOD_NAME = "saveeDGH_smrt";
                request = new SoapObject(NAMESPACE, METHOD_NAME);
                request.addProperty("xmlStr", generateXmlString());
                commonMethods.appendSecurityParams(context, request, strCIFBDMEmailId, strCIFBDMMObileNo);
                commonMethods.TLSv12Enable();

                System.out.println("request:" + request.toString());
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);

                commonMethods.TLSv12Enable();

                String URl = ServiceURL.SERVICE_URL;
                HttpTransportSE androidHttpTranport = new HttpTransportSE(URl);
                String SOAP_ACTION = "http://tempuri.org/" + METHOD_NAME;
                androidHttpTranport.call(SOAP_ACTION, envelope);
                Object response = envelope.getResponse();
                result = response.toString();

            } catch (Exception e) {
                mProgressDialog.dismiss();
                running = false;
            }
            return null;

        }


        @Override
        protected void onPostExecute(String unused) {
            if (mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }

            if (running) {
                if (result.equalsIgnoreCase("1")) {
                    uploadPDFService = new UploadPDFService("DGHPDF");
                    uploadPDFService.execute();
                } else if (result.equalsIgnoreCase("2")) {
                    commonMethods.showMessageDialog(context, "Data Already Exist");
                } else if (result.equalsIgnoreCase("0")) {
                    commonMethods.showMessageDialog(context, "Data Saving Failed");
                } else {
                    commonMethods.showMessageDialog(context, "Data Saving Failed");
                }
            } else {
                commonMethods.showMessageDialog(context, "Data Saving Failed");
            }
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
                //UploadFilePayout(byte[] f, string fileName, string policyno, string filetype)
                //fileName, string policyno, string filetype)
                String METHOD_NAME = "UploadFilePayout";
                String NAMESPACE = "http://tempuri.org/";
                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
                byte[] BI_bytes;
                if (fileTag.equalsIgnoreCase("DGHPDF")) {
                    BI_bytes = new CommonMethods().read(DGHFilePath);
                    request.addProperty("filetype", "DGH");
                    request.addProperty("fileName", DGHFilePath.getName());
                } else if (fileTag.equalsIgnoreCase("IDProof")) {
                    BI_bytes = new CommonMethods().read(identityProofFileName);
                    request.addProperty("filetype", str_Doc_Abbreviation_identity);
                    request.addProperty("fileName", identityProofFileName.getName());
                } else if (fileTag.equalsIgnoreCase("custPhoto")) {
                    BI_bytes = new CommonMethods().read(userPhotoFileName);
                    request.addProperty("filetype", "CustPhoto");
                    request.addProperty("fileName", userPhotoFileName.getName());
                } else if (fileTag.equalsIgnoreCase("Other")) {
                    BI_bytes = new CommonMethods().read(otherProofFileName);
                    request.addProperty("filetype", "Other");
                    request.addProperty("fileName", otherProofFileName.getName());
                } else {
                    BI_bytes = new CommonMethods().read(addressProofFileName);
                    request.addProperty("filetype", str_Doc_Abbreviation);
                    request.addProperty("fileName", addressProofFileName.getName());
                }
                request.addProperty("f", org.kobjects.base64.Base64.encode(BI_bytes));
                request.addProperty("policyno", policyNumber);


                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);
                // allowAllSSL();
                commonMethods.TLSv12Enable();

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);

                String URl = ServiceURL.SERVICE_URL;
                HttpTransportSE androidHttpTranport = new HttpTransportSE(URl);

                String SOAP_ACTION = "http://tempuri.org/" + METHOD_NAME;
                androidHttpTranport.call(SOAP_ACTION,
                        envelope);

                SoapPrimitive sa;

                sa = (SoapPrimitive) envelope.getResponse();
                String inputpolicylist = sa.toString();

                if (inputpolicylist.equalsIgnoreCase("1"))
                    flag = 1;
                else
                    flag = 0;

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
                    if (fileTag.equalsIgnoreCase("DGHPDF")) {
                        uploadPDFService = new UploadPDFService("IDProof");
                        uploadPDFService.execute();
                    } else if (fileTag.equalsIgnoreCase("IDProof")) {
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
                        ll_dgh.setVisibility(View.GONE);
                        llOTP.setVisibility(View.GONE);
                        etPolicyNumber.setText("");
                        commonMethods.showMessageDialog(context, "Data Saved successfully");

                        if (!TextUtils.isEmpty(EMAILID)) {
                            sendMail(EMAILID, "DGH Customer Copy ", "Dear " + NAME + ",\n" +
                                            "Please find attached DGH PDF. ",
                                    customerPDFFilePath, customerPDFFilePath);
                        }

                    }

                } else {
                    commonMethods.showMessageDialog(context, "DGH PDF Upload Failed");
                }

            } else {
                commonMethods.showMessageDialog(context, "DGH PDF Upload Failed");
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        boolean is1WProductEKYC = false;
        switch (parent.getId()) {


            case R.id.spnr_document_upload_document_identity:

                if (spnr_document_upload_document_identity.getSelectedItem().toString().toLowerCase().contains("aadhar card")) {
                    is1WProduct = false;
                    is1WProductEKYC = false;
                    llIdentityAadhaarScan.setVisibility(View.VISIBLE);
                } else {
                    llIdentityAadhaarScan.setVisibility(View.VISIBLE);
                }

                break;

            case R.id.spnr_document_upload_document_address:

                if (spnr_document_upload_document_address.getSelectedItem().toString().toLowerCase().contains("aadhar card")) {
                    is1WProduct = false;
                    is1WProductEKYC = false;
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

    public void onPreviewUserPhoto(View v) {

        if (userPhotoFileName.exists()) {
            try {
                commonMethods.openAllDocs(context, userPhotoFileName);
            } catch (IOException ex) {
                commonMethods.showToast(context, ex.getMessage());
            }
        } else {
            commonMethods.showToast(context, "File does not exists");
        }
    }

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
        // ClearDocumentsPDF();
        if (!(spnr_document_upload_document_identity.getSelectedItem().toString()).equalsIgnoreCase("Select Document")) {
            uploadFlag = 0;
            img_btn_document_upload_click_image_identity
                    .setImageDrawable(getResources().getDrawable(
                            R.drawable.ibtn_camera));
            Check = "IdentityProof";
            isBrowseCapture = REQUEST_CODE_PICK_FILE;

            Intent intent = new Intent(DGHActivity.this, OcrActivity.class);
            startActivityForResult(intent, REQUEST_OCR);
        } else {
            commonMethods.showMessageDialog(context, "Please select IDonc Proof");
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
        //ClearDocumentsPDF();
        if (!(spnr_document_upload_document_address.getSelectedItem().toString()).equalsIgnoreCase("Select Document")) {
            uploadFlag = 0;
            img_btn_document_upload_click_image_address
                    .setImageDrawable(getResources().getDrawable(
                            R.drawable.ibtn_camera));
            Check = "AddressProof";
            isBrowseCapture = REQUEST_CODE_PICK_FILE;

            Intent intent = new Intent(DGHActivity.this, OcrActivity.class);
            startActivityForResult(intent, REQUEST_OCR);
        } else
            commonMethods.showMessageDialog(context, "Please select Address Proof");

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
            } /*else {

                ClearDocumentsPDF();
                Toast.makeText(
                        context,
                        "Please Capture Or Browse The Other Proof Document First",
                        Toast.LENGTH_SHORT).show();
            }*/

        } /*else {

            ClearDocumentsPDF();
        }*/

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
                String textPrint = " Latitude: " + mLatLng.latitude + ", Longitude: " + mLatLng.longitude + "\n";
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

                    identityProofFileName = mStorageUtils.saveFileToAppSpecificDir(context, StorageUtils.DIRECT_DIRECTORY,
                            FileName, imagePath);
                    CompressImage.compressImage(identityProofFileName.getAbsolutePath());
                    //copy  FILE
                    //commonMethods.copyFile(new FileInputStream(imagePath), new FileOutputStream(destinationFile));
                    //identityProofFileName = destinationFile;

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

                    addressProofFileName = mStorageUtils.saveFileToAppSpecificDir(context, StorageUtils.DIRECT_DIRECTORY,
                            fileName, imagePath);
                    CompressImage.compressImage(addressProofFileName.getAbsolutePath());

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

                    otherProofFileName = mStorageUtils.saveFileToAppSpecificDir(context, StorageUtils.DIRECT_DIRECTORY,
                            fileName, imagePath);
                    CompressImage.compressImage(otherProofFileName.getAbsolutePath());

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

                    String fileName = str_selected_urn_no + "_cust2Photo.jpg";
                    destinationFile = mStorageUtils.saveFileToAppSpecificDir(context, StorageUtils.DIRECT_DIRECTORY,
                            fileName, imagePath);

                    //image compression by bhalla
                    CompressImage.compressImage(destinationFile.getAbsolutePath());
                    //copy  FILE
                    //commonMethods.copyFile(new FileInputStream(imagePath), new FileOutputStream(destinationFile));
                    agentPhotoBitmap = commonMethods.mergeBitmap(commonMethods.
                                    getBitmap(imagePath.getAbsolutePath()),
                            commonMethods.convertStringToBitMap(context, textPrint));

                    commonMethods.storeImage(context, agentPhotoBitmap, destinationFile.getAbsolutePath());

                    ibAgentTypeUploadPhoto.setImageBitmap(agentPhotoBitmap);
                    commonMethods.setFocusable(ibAgentTypeUploadPhoto);
                    ibAgentTypeUploadPhoto.requestFocus();

                    agentPhotoFileName = destinationFile;
                    commonMethods.setFocusable(ibAgentTypeUploadPhoto);
                    ibAgentTypeUploadPhoto.requestFocus();

                }
                else if (Check.equals("UserPhoto")) {
                    String fileName = str_selected_urn_no + "_cust1Photo.jpg";
                    destinationFile = mStorageUtils.saveFileToAppSpecificDir(context, StorageUtils.DIRECT_DIRECTORY,
                            fileName, imagePath);

                    //image compression by bhalla
                    CompressImage.compressImage(destinationFile.getAbsolutePath());
                    //copy  FILE
                    //commonMethods.copyFile(new FileInputStream(imagePath), new FileOutputStream(destinationFile));

                    userPhotoBitmap = commonMethods.mergeBitmap(commonMethods.
                                    getBitmap(imagePath.getAbsolutePath()),
                            commonMethods.convertStringToBitMap(context, textPrint));

                    commonMethods.storeImage(context, userPhotoBitmap, destinationFile.getAbsolutePath());

                    imagePhotoGraphGPSCord.setImageBitmap(userPhotoBitmap);
                    commonMethods.setFocusable(imagePhotoGraphGPSCord);
                    imagePhotoGraphGPSCord.requestFocus();

                    userPhotoFileName = destinationFile;
                    commonMethods.setFocusable(imagePhotoGraphGPSCord);
                    imagePhotoGraphGPSCord.requestFocus();
                    // String stringToParse = "Address: " + commonMethods.getCompleteAddressString(context, mLatLng.latitude, mLatLng.longitude);

                           /* StringBuffer sb = new StringBuffer(stringToParse);

                            int i = 0;
                            while ((i = sb.indexOf(" ", i + 25)) != -1) {
                                sb.replace(i, i + 1, "\n");
                            }

                                textPrint += "\n" + commonMethods.GetUserType(context) + " Name: " + commonMethods.getUserName(context)
                                        + ", " + commonMethods.GetUserType(context) + " ID: " + commonMethods.GetUserCode(context)
                                        + "\n" + sb.toString()
                                        + "Date: " + commonMethods.getCurrentTimeStamp();*/

                }
            } else {
                Toast.makeText(context, "Data not receive", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == REQUEST_CODE_PICK_FILE) {
            if (resultCode == AppCompatActivity.RESULT_OK) {

                try {

                    if (Check.equals("UserPhoto")) {
                        //File destinationFile = commonMethods.createCaptureImg( "_cust1Photo.jpg");
                        String textPrint = " Latitude: " + mLatLng.latitude + ", Longitude: " + mLatLng.longitude + "\n"
                                + "Date : " + commonMethods.getCurrentMonthDate() + "\n";
                        //image compression by bhalla
                        CompressImage.compressImage(userPhotoFileName.getAbsolutePath());
                        userPhotoBitmap = commonMethods.mergeBitmap(commonMethods.
                                        getBitmap(userPhotoFileName.getAbsolutePath()),
                                commonMethods.convertStringToBitMap(context, textPrint));

                        commonMethods.storeImage(context, userPhotoBitmap, userPhotoFileName.getAbsolutePath());

                        imagePhotoGraphGPSCord.setImageBitmap(userPhotoBitmap);
                        commonMethods.setFocusable(imagePhotoGraphGPSCord);
                        imagePhotoGraphGPSCord.requestFocus();

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


    public class SelectedAdapter extends RecyclerView.Adapter<SelectedAdapter.
            ViewHolderAdapter> implements Filterable {

        private ArrayList<QuestionsValuesModel> lstAdapterList, lstSearch;

        SelectedAdapter(ArrayList<QuestionsValuesModel> lstAdapterList) {
            this.lstAdapterList = lstAdapterList;
        }

        @Override
        public Filter getFilter() {

            return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence charSequence) {

                    final FilterResults oReturn = new FilterResults();
                    final ArrayList<QuestionsValuesModel> results = new ArrayList<>();

                    if (lstSearch == null)
                        lstSearch = lstAdapterList;

                    String strSearch = charSequence == null ? "" : charSequence.toString();

                   /* if (!strSearch.equals("")) {
                        if (lstSearch != null && lstSearch.size() > 0) {
                            for (final QuestionsValuesModel model : lstSearch) {
                                if (model.getREPORT_TYPE().toLowerCase().contains(charSequence.toString().toLowerCase())
                                        || model.getPROPOSALNO().toLowerCase().contains(charSequence.toString().toLowerCase())
                                        || model.getDOC_TYPE().toLowerCase().contains(charSequence.toString().toLowerCase())
                                        || model.getDESP_DATE().toLowerCase().contains(charSequence.toString().toLowerCase())
                                        || model.getCHEQUENO().toLowerCase().contains(charSequence.toString().toLowerCase())
                                        || model.getPOLICYNO().toLowerCase().contains(charSequence.toString().toLowerCase())

                                        || model.getCHEQUEAMOUNT().toLowerCase().contains(charSequence.toString().toLowerCase())
                                        || model.getCHEQUEDATE().toLowerCase().contains(charSequence.toString().toLowerCase())
                                ) {
                                    results.add(model);
                                }
                            }
                        }
                        oReturn.values = results;
                    } else {
                        oReturn.values = globalDataList;
                    }*/
                    return oReturn;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {

                    lstAdapterList = (ArrayList<QuestionsValuesModel>) results.values;
                    selectedAdapter = new SelectedAdapter(lstAdapterList);
                    recyclerview.setAdapter(selectedAdapter);

                    notifyDataSetChanged();
                }
            };
        }


        @Override
        public int getItemCount() {
            return lstAdapterList.size();
        }

        @Override
        public ViewHolderAdapter onCreateViewHolder(ViewGroup parent, int viewType) {
            View mView = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.list_item_dgh, parent, false);

            return new ViewHolderAdapter(mView);
        }

        @Override
        public void onBindViewHolder(final ViewHolderAdapter holder, final int position) {

            holder.editetxQuestionNumber.setText(lstAdapterList.get(position).getQuestionNumber());
            holder.editetxQuestionExplained.setText(lstAdapterList.get(position).getQuestionDescription());
        }

        public List<QuestionsValuesModel> getTotalItemList() {
            List<QuestionsValuesModel> finalList = new ArrayList<>();
            finalList.addAll(lstAdapterList);

            return finalList;
        }

        public class ViewHolderAdapter extends RecyclerView.ViewHolder {


            private final EditText editetxQuestionNumber, editetxQuestionExplained;

            ViewHolderAdapter(View v) {
                super(v);
                editetxQuestionExplained = v.findViewById(R.id.editetxQuestionExplained);
                editetxQuestionNumber = v.findViewById(R.id.editetxQuestionNumber);
            }

        }

    }

    class QuestionsValuesModel {
        private final String questionNumber;
        private final String questionDescription;

        public QuestionsValuesModel(String questionNumber, String questionDescription) {
            this.questionNumber = questionNumber;
            this.questionDescription = questionDescription;
        }

        public String getQuestionNumber() {
            return questionNumber;
        }

        public String getQuestionDescription() {
            return questionDescription;
        }
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

    private static boolean isIntentAvailable(Context context, String action) {
        final PackageManager packageManager = context.getPackageManager();
        final Intent intent = new Intent(action);
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent,
                PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }


    public boolean isThisDateValid(String dateToValidate) {

        if (dateToValidate == null) {
            return false;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        sdf.setLenient(false);

        try {
            // if not valid, it will throw ParseException
            Date date = sdf.parse(dateToValidate);
            System.out.println(date);

        } catch (ParseException e) {

            e.printStackTrace();
            return false;
        }

        return true;
    }

    public String[] Split(String text, int chunkSize, int maxLength) {
        char[] data = text.toCharArray();
        int len = Math.min(data.length, maxLength);
        String[] result = new String[(len + chunkSize - 1) / chunkSize];
        int linha = 0;
        for (int i = 0; i < len; i += chunkSize) {
            result[linha] = new String(data, i, Math.min(chunkSize, len - i));
            linha++;
        }
        return result;
    }

    public String AllAddress(String query) {

        final Map<String, String> query_pairs = new LinkedHashMap<String, String>();
        String[] pairs = query.replace("\" ", "&").replace("\"", "").split("&");
        for (String pair : pairs) {
            int idx = pair.indexOf("=");
            query_pairs.put(pair.substring(0, idx).trim(),
                    pair.substring(idx + 1).trim());
        }

        String str_Address_title = query_pairs.get("co") == null ? ""
                : query_pairs.get("co");

        String str_Address1 = (query_pairs.get("house") == null ? ""
                : query_pairs.get("house") + ", ")
                + (query_pairs.get("street") == null ? "" : query_pairs
                .get("street") + ", ")
                + (query_pairs.get("lm") == null ? "" : query_pairs
                .get("lm"));

        String str_Address2 = query_pairs.get("loc") == null ? "" : query_pairs
                .get("loc");
        String str_Address3 = query_pairs.get("vtc") == null ? "" : query_pairs
                .get("vtc");
        String str_PinCode = query_pairs.get("pc") == null ? "" : query_pairs
                .get("pc");
        String str_City = query_pairs.get("dist") == null ? "" : query_pairs
                .get("dist");
        String str_State = query_pairs.get("state") == null ? "" : query_pairs
                .get("state");

        str_QR_code_Gender = query_pairs.get("gender") == null ? "" : query_pairs
                .get("gender");

        str_QR_code_Name = query_pairs.get("name") == null ? "" : query_pairs
                .get("name");

        String str_QR_code_YOB = query_pairs.get("yob") == null ? "" : query_pairs
                .get("yob");

        str_QR_code_DOB = query_pairs.get("dob") == null ? "" : query_pairs
                .get("dob");

        if (str_QR_code_Gender.equals("M")) {
            str_QR_code_Gender = "Male";
        } else if (str_QR_code_Gender.equals("F")) {
            str_QR_code_Gender = "Female";
        }

        if (str_QR_code_DOB.equals("")) {
            str_QR_code_DOB = str_QR_code_YOB;
        }

        if (!(str_Address_title.equals(""))) {
            str_Address_title = str_Address_title.substring(0, 3);
        }

        String[] lines = Split((str_Address1 + str_Address2 + str_Address3), 45, 135);
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

        str_qr_code_address = str_Address_title + str_Address1 + str_Address2 + str_Address3
                + str_City + str_PinCode + str_State;

        strSelectedProof = "";

        int qrCodeClick = 0;
        switch (qrCodeClick) {

            case 2:
                strSelectedProof = spnr_document_upload_document_identity.getSelectedItem().toString();
                break;

            case 3:
                strSelectedProof = spnr_document_upload_document_address.getSelectedItem().toString();
                break;

            default:
                strSelectedProof = "";
                break;
        }

        //xml
        String strQRXml =
                "<urn_NO>" + str_selected_urn_no + "</urn_NO>" +
                        "<FACTCA_ID_PROOF>" + strSelectedProof + "</FACTCA_ID_PROOF>" +
                        "<ADD_FACTCA_ID_PROOF></ADD_FACTCA_ID_PROOF>" +
                        "<Aadhar_qr_prop_name>" + str_QR_code_Name + "</Aadhar_qr_prop_name>" +
                        "<Aadhar_qr_prop_dob>" + str_QR_code_DOB + "</Aadhar_qr_prop_dob>" +
                        "<Aadhar_qr_prop_gender>" + str_QR_code_Gender + "</Aadhar_qr_prop_gender>" +
                        "<Aadhar_qr_prop_address>" + str_qr_code_address + "</Aadhar_qr_prop_address>" +
                        "<Aadhar_qr_prop_email_id></Aadhar_qr_prop_email_id>" +
                        "<Aadhar_qr_prop_mobile_no></Aadhar_qr_prop_mobile_no>" +
                        "<CREATED_BY>" + mUserDetailsValuesModel.getStrCIFBDMUserId() + "</CREATED_BY>" +
                        "<CREATED_DATE>" + commonMethods.getCurrentDateMM_DD_YYYY() + "</CREATED_DATE>" +
                        "<MODIFIED_BY></MODIFIED_BY>" +
                        "<MODIFIED_DATE></MODIFIED_DATE>" +
                        "<FLAG1></FLAG1>" +
                        "<FLAG2></FLAG2>" +
                        "<FLAG3></FLAG3>" +
                        "<FLAG4></FLAG4>";

        return strQRXml;
    }


    @Override
    public void downLoadData() {

        if (!strAadhaarQRScannedValue.equals("")) {

            //check 1w
            final String query = parseXmlTag(strAadhaarQRScannedValue,
                    "PrintLetterBarcodeData");

            if (is1WProduct) {
                AllAddress(query);

                String strQRData = "<QRCODE>" + str1WFirstQR +
                        "<add_Aadhar_qr_prop_name>" + str_QR_code_Name + "</add_Aadhar_qr_prop_name>" +
                        "<add_Aadhar_qr_prop_dob>" + str_QR_code_DOB + "</add_Aadhar_qr_prop_dob>" +
                        "<add_Aadhar_qr_prop_gender>" + str_QR_code_Gender + "</add_Aadhar_qr_prop_gender>" +
                        "<add_Aadhar_qr_prop_addres>" + str_qr_code_address + "</add_Aadhar_qr_prop_addres>" +
                        "<add_Aadhar_qr_prop_email></add_Aadhar_qr_prop_email>" +
                        "<add_Aadhar_qr_prop_mobile></add_Aadhar_qr_prop_mobile>" +
                        "<add_Aadhar_is_ekyc>N</add_Aadhar_is_ekyc>" +
                        "</QRCODE>";

                is1WProduct = false;
                //new UploadScannedQRDetails().execute(strQRData);
            } else {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setMessage("URN is Smart Humsafar Product?");
                alertDialogBuilder.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                dialog.dismiss();
                                if (query != null) {

                                    is1WProduct = true;
                                    str1WFirstQR = AllAddress(query);

                                    commonMethods.showMessageDialog(context, "Please Scan aadhar QR for spouse");
                                } else {
                                    Toast.makeText(DGHActivity.this, "Invalid QR Code!", Toast.LENGTH_LONG).show();
                                }
                            }
                        });

                alertDialogBuilder.setNegativeButton("No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                is1WProduct = false;
                                dialog.dismiss();

                                if (query != null) {

                                    String strQRData = "<QRCODE>" + AllAddress(query) +
                                            "<add_Aadhar_qr_prop_name></add_Aadhar_qr_prop_name>" +
                                            "<add_Aadhar_qr_prop_dob></add_Aadhar_qr_prop_dob>" +
                                            "<add_Aadhar_qr_prop_gender></add_Aadhar_qr_prop_gender>" +
                                            "<add_Aadhar_qr_prop_addres></add_Aadhar_qr_prop_addres>" +
                                            "<add_Aadhar_qr_prop_email></add_Aadhar_qr_prop_email>" +
                                            "<add_Aadhar_qr_prop_mobile></add_Aadhar_qr_prop_mobile>" +
                                            "<add_Aadhar_is_ekyc>N</add_Aadhar_is_ekyc>" +
                                            "</QRCODE>";

                                    // new UploadScannedQRDetails().execute(strQRData);
                                } else {
                                    Toast.makeText(DGHActivity.this, "Invalid QR Code!", Toast.LENGTH_LONG).show();
                                }
                            }
                        });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        } else {
            commonMethods.showToast(context, "Please Try Again Later..");
            strAadhaarQRScannedValue = "";
        }
    }

    public String parseXmlTag(String parentNode, String tag) {
        int start_indx = parentNode.indexOf("<" + tag);
        int end_indx = parentNode.indexOf("/>", start_indx + tag.length());

        String content = null;
        if (start_indx >= 0 && end_indx > 0) {
            content = parentNode.substring(start_indx + tag.length() + 1,
                    end_indx);
        }
        return content;
    }


    private void createCOVIDPDF() {
        try {


            Font headerBold = new Font(Font.FontFamily.TIMES_ROMAN, 12,
                    Font.BOLD);
            Font small_bold = new Font(Font.FontFamily.TIMES_ROMAN, 8,
                    Font.BOLD);
            Font small_normal = new Font(Font.FontFamily.TIMES_ROMAN, 8,
                    Font.NORMAL);

            File COVIDFilePath = mStorageUtils.createFileToAppSpecificDir(context,
                    "COVID19_" + policyNumber + ".pdf");

            Rectangle rect = new Rectangle(594f, 792f);

            Document document = new Document(rect, 50, 50, 50, 50);
            @SuppressWarnings("unused")
            PdfWriter pdf_writer = null;
            pdf_writer = PdfWriter.getInstance(document, new FileOutputStream(
                    COVIDFilePath.getAbsolutePath()));

            HeaderFooter footer = new HeaderFooter();
            pdf_writer.setPageEvent(footer);

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

            PdfPCell cell;

            Paragraph new_line = new Paragraph("\n");

            //Policy Number Table
            PdfPTable policyNumberTable = new PdfPTable(2);
            policyNumberTable.setWidths(new float[]{5f, 5f});
            policyNumberTable.setWidthPercentage(100f);
            policyNumberTable.setHorizontalAlignment(Element.ALIGN_LEFT);

            cell = new PdfPCell(new Phrase("COVID Questionnaire (UWM029)",
                    headerBold));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
//            cell.setBorder(Rectangle.NO_BORDER);
            cell.setColspan(2);
            policyNumberTable.addCell(cell);


            cell = new PdfPCell(new Phrase(
                    "Proposal Number : ", small_bold));

            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            policyNumberTable.addCell(cell);

            cell = new PdfPCell(new Phrase(policyNumber, small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            policyNumberTable.addCell(cell);

            cell = new PdfPCell(new Phrase(
                    "Name of the Proposer/ Life to be assured : ", small_bold));

            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            policyNumberTable.addCell(cell);

            cell = new PdfPCell(new Phrase(lifeAssuredName, small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            policyNumberTable.addCell(cell);

            document.add(policyNumberTable);
            //End Policy Number Table


            document.add(new_line);

            //Question Table
            PdfPTable question1Table = new PdfPTable(2);
            question1Table.setWidths(new float[]{5f, 5f});
            question1Table.setWidthPercentage(100f);
            question1Table.setHorizontalAlignment(Element.ALIGN_LEFT);

            //Question 1 cell
            cell = new PdfPCell(new Phrase(
                    "1.Have you visited any foreign country after 1.1.2020,then please answer the following", small_normal));

            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            question1Table.addCell(cell);


            //Column 2
            cell = new PdfPCell(new Phrase(
                    pastForeignVisit, small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            question1Table.addCell(cell);


            if (pastForeignVisit.equalsIgnoreCase("yes")) {

                cell = new PdfPCell(new Phrase(
                        "1.1 Name of country visited", small_normal));

                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                question1Table.addCell(cell);

                cell = new PdfPCell(new Phrase(
                        pastCountryName, small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                question1Table.addCell(cell);

                cell = new PdfPCell(new Phrase(
                        "1.2 Duration", small_normal));

                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                question1Table.addCell(cell);

                cell = new PdfPCell(new Phrase(
                        "From: " + pastFromDuration + "\n" + "To: " + pastToDuration, small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                question1Table.addCell(cell);

                cell = new PdfPCell(new Phrase(
                        "1.3 Date of return to India", small_normal));

                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                question1Table.addCell(cell);

                cell = new PdfPCell(new Phrase(
                        pastReturnDate, small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                question1Table.addCell(cell);


                cell = new PdfPCell(new Phrase(
                        "1.4 If you have been screened at the airport please providecopy of report provided", small_normal));

                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                question1Table.addCell(cell);

                cell = new PdfPCell(new Phrase(
                        screeneAtAirport, small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                question1Table.addCell(cell);
            } else {

                cell = new PdfPCell(new Phrase(
                        "1.1 Name of country visited", small_normal));

                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                question1Table.addCell(cell);

                cell = new PdfPCell(new Phrase(
                        "NA", small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                question1Table.addCell(cell);

                cell = new PdfPCell(new Phrase(
                        "1.2 Duration", small_normal));

                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                question1Table.addCell(cell);

                cell = new PdfPCell(new Phrase(
                        "From: NA\n" + "To: NA", small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                question1Table.addCell(cell);

                cell = new PdfPCell(new Phrase(
                        "1.3 Date of return to India", small_normal));

                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                question1Table.addCell(cell);

                cell = new PdfPCell(new Phrase(
                        "NA", small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                question1Table.addCell(cell);


                cell = new PdfPCell(new Phrase(
                        "1.4 If you have been screened at the airport please providecopy of report provided", small_normal));

                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                question1Table.addCell(cell);

                cell = new PdfPCell(new Phrase(
                        "NA", small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                question1Table.addCell(cell);
            }


            cell = new PdfPCell(new Phrase(
                    "2 Have you been tested for COVID-19. If yes, all thereports of same till date", small_normal));

            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            question1Table.addCell(cell);

            cell = new PdfPCell(new Phrase(
                    testedForCovid, small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            question1Table.addCell(cell);


            cell = new PdfPCell(new Phrase(
                    "3 Please confirm if either of the following is applicable to you", small_normal));
            cell.setColspan(2);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            question1Table.addCell(cell);

            cell = new PdfPCell(new Phrase(
                    "3.1 Kept in home quarantine anytime (till date) since1.1.2020", small_normal));

            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            question1Table.addCell(cell);

            cell = new PdfPCell(new Phrase(
                    homeQuarantine, small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            question1Table.addCell(cell);


            cell = new PdfPCell(new Phrase(
                    "3.2 Kept under observation anytime (till date) since1.1.2020", small_normal));

            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            question1Table.addCell(cell);

            cell = new PdfPCell(new Phrase(
                    underObservation, small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            question1Table.addCell(cell);


            cell = new PdfPCell(new Phrase(
                    "3.3 Kept in home isolation/self isolation anytime (till date)since 1.1.2020", small_normal));

            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            question1Table.addCell(cell);

            cell = new PdfPCell(new Phrase(
                    selfIsolation, small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            question1Table.addCell(cell);

            cell = new PdfPCell(new Phrase(
                    "4 Do you plan to travel to any foreign countries in next 6months", small_normal));

            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            question1Table.addCell(cell);

            cell = new PdfPCell(new Phrase(
                    futureForeignVisit, small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            question1Table.addCell(cell);

            if (futureForeignVisit.equalsIgnoreCase("yes")) {

                cell = new PdfPCell(new Phrase(
                        "4.1 Name of country visited", small_normal));

                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                question1Table.addCell(cell);

                cell = new PdfPCell(new Phrase(
                        futureCountryName, small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                question1Table.addCell(cell);

                cell = new PdfPCell(new Phrase(
                        "4.2 Duration", small_normal));

                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                question1Table.addCell(cell);

                cell = new PdfPCell(new Phrase(
                        "From: " + futureFromDuration + "\n" + "To: " + futureToDuration, small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                question1Table.addCell(cell);

                cell = new PdfPCell(new Phrase(
                        "4.3 Date of return to India", small_normal));

                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                question1Table.addCell(cell);

                cell = new PdfPCell(new Phrase(
                        futureReturnDate, small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                question1Table.addCell(cell);

            } else {

                cell = new PdfPCell(new Phrase(
                        "4.1 Name of country visited", small_normal));

                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                question1Table.addCell(cell);

                cell = new PdfPCell(new Phrase(
                        "NA", small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                question1Table.addCell(cell);

                cell = new PdfPCell(new Phrase(
                        "4.2 Duration", small_normal));

                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                question1Table.addCell(cell);

                cell = new PdfPCell(new Phrase(
                        "From: NA\n" + "To: NA", small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                question1Table.addCell(cell);

                cell = new PdfPCell(new Phrase(
                        "4.3 Date of return to India", small_normal));

                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                question1Table.addCell(cell);

                cell = new PdfPCell(new Phrase(
                        "NA", small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                question1Table.addCell(cell);

            }


            cell = new PdfPCell(new Phrase(
                    "5 Have you or your immediate family members/co-habitants suffered from any signs & symptoms of flu(cough, cold, fever more than 05 days) since 1.1.2020(whether any medical consultation taken or not)?", small_normal));

            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            question1Table.addCell(cell);

            cell = new PdfPCell(new Phrase(
                    familyMemberSymptoms, small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            question1Table.addCell(cell);

            cell = new PdfPCell(new Phrase(
                    "6 Have you or your immediate family members undergoneor been advised to undergo any test/investigations orhospitalized for observation or treatment in past 2 monthsfor respiratory symptoms?", small_normal));

            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            question1Table.addCell(cell);

            cell = new PdfPCell(new Phrase(
                    respiratorySymptoms, small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            question1Table.addCell(cell);

            cell = new PdfPCell(new Phrase(
                    "7 Have you or any of your immediate familymembers/cohabitants come in contact with suspected or confirmed cases of coronavirus", small_normal));

            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            question1Table.addCell(cell);

            cell = new PdfPCell(new Phrase(
                    contactWithCorona, small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            question1Table.addCell(cell);

            document.add(question1Table);


            PdfPTable declarationTable = new PdfPTable(1);
            declarationTable.setWidthPercentage(100f);
            declarationTable.setHorizontalAlignment(Element.ALIGN_LEFT);

            cell = new PdfPCell((new Phrase("Declaration to be given by the Proposer/Life to be assured", headerBold)));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            declarationTable.addCell(cell);

            cell = new PdfPCell((new Phrase("I declare that the answers given above are true and to the best of my knowledge and that I have not withheld any materialinformation that may influence the assessment or acceptance of this application.", small_bold)));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            declarationTable.addCell(cell);

            cell = new PdfPCell((new Phrase("I agree that this form will constitute part of my application for life assurance and that failure to disclose any material factknown to me may invalidate the contract.", small_bold)));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            declarationTable.addCell(cell);

            document.add(declarationTable);

            PdfPTable signatureTable = new PdfPTable(2);
            signatureTable.setWidths(new float[]{5f, 5f});
            signatureTable.setWidthPercentage(100f);
            signatureTable.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell = new PdfPCell((new Phrase("Signature of Proposer/ Life to be assured\n" +
                    "This document Authenticated by " + lifeAssuredName, small_bold)));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            signatureTable.addCell(cell);

            cell = new PdfPCell((new Phrase("", small_bold)));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);

            signatureTable.addCell(cell);

            document.add(signatureTable);

            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void createDGHPDF() {

        try {


            Font headerBold = new Font(Font.FontFamily.TIMES_ROMAN, 12,
                    Font.BOLD);
            Font small_bold = new Font(Font.FontFamily.TIMES_ROMAN, 8,
                    Font.BOLD);
            Font small_normal = new Font(Font.FontFamily.TIMES_ROMAN, 8,
                    Font.NORMAL);
            //18001545008_DGH_01.pdf
            // DGHFilePath = new File(folder, "DGH_" + policyNumber + ".pdf");
            DGHFilePath = mStorageUtils.createFileToAppSpecificDir(context, policyNumber + "_DGH_01" + ".pdf");
            customerPDFFilePath = mStorageUtils.createFileToAppSpecificDir(context, "DGH_" + policyNumber + ".pdf");
            Rectangle rect = new Rectangle(594f, 792f);

            Document document = new Document(rect, 50, 50, 50, 50);
            Document documentCustomerPDF = new Document(rect, 50, 50, 50, 50);
            @SuppressWarnings("unused")
            PdfWriter pdf_writer = null, pdfWriterCustomerPDF;
            pdf_writer = PdfWriter.getInstance(document, new FileOutputStream(
                    DGHFilePath.getAbsolutePath()));

            pdfWriterCustomerPDF = PdfWriter.getInstance(documentCustomerPDF, new FileOutputStream(
                    customerPDFFilePath.getAbsolutePath()));

            HeaderFooter footer = new HeaderFooter();
            pdf_writer.setPageEvent(footer);

            pdfWriterCustomerPDF.setPageEvent(footer);

            Bitmap bitmap = BitmapFactory.decodeFile(userPhotoFileName.getAbsolutePath());

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 50, stream);
            byte[] bitMapData = stream.toByteArray();
            Image image = Image.getInstance(bitMapData);
            image.setAlignment(Image.LEFT);
            image.scaleToFit(90, 90);

            document.open();
            documentCustomerPDF.open();

            PdfPTable table = new PdfPTable(3);
            table.setWidths(new float[]{2.5f, 8.5f, 2f});
            table.setWidthPercentage(100);
            table.getDefaultCell().setPadding(15);

            PdfPCell cell;

            /*cell = new PdfPCell(new Phrase("\n", headerBold));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);*/

            cell = new PdfPCell(new Phrase("Electronic Request for Revival and Declaration of Good Health (Assisted)",
                    headerBold));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setColspan(2);
            table.addCell(cell);

            cell = new PdfPCell(getImageForPDF(userPhotoBitmap));
            cell.setRowspan(3);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("", headerBold));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setColspan(2);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("\n", headerBold));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            document.add(table);
            documentCustomerPDF.add(table);

            Paragraph new_line = new Paragraph("\n");
            document.add(new_line);
            documentCustomerPDF.add(new_line);

            //Policy Number Table
            PdfPTable policyNumberTableDGH = new PdfPTable(4);
            policyNumberTableDGH.setWidths(new float[]{5f, 5f, 5f, 5f});
            policyNumberTableDGH.setWidthPercentage(100f);
            policyNumberTableDGH.setHorizontalAlignment(Element.ALIGN_LEFT);


            cell = new PdfPCell(new Phrase(
                    "Policy Number : ", small_bold));

            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            policyNumberTableDGH.addCell(cell);

            cell = new PdfPCell(new Phrase(policyNumber, small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            policyNumberTableDGH.addCell(cell);

            cell = new PdfPCell(new Phrase(
                    "Life Assured Name : ", small_bold));

            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            policyNumberTableDGH.addCell(cell);

            cell = new PdfPCell(new Phrase(lifeAssuredName, small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            policyNumberTableDGH.addCell(cell);

            document.add(policyNumberTableDGH);
            documentCustomerPDF.add(policyNumberTableDGH);
            //End Policy Number Table

            //Date of Birth Gedner
            PdfPTable dobGenderTable = new PdfPTable(4);
            dobGenderTable.setWidths(new float[]{5f, 5f, 5f, 5f});
            dobGenderTable.setWidthPercentage(100f);
            dobGenderTable.setHorizontalAlignment(Element.ALIGN_LEFT);

            //DOB cell
            cell = new PdfPCell(new Phrase(
                    "Date of Birth : ", small_bold));

            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            dobGenderTable.addCell(cell);

            cell = new PdfPCell(new Phrase(dobString, small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            dobGenderTable.addCell(cell);

            //Gender Cell
            cell = new PdfPCell(new Phrase(
                    "Gender : ", small_bold));

            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            dobGenderTable.addCell(cell);

            cell = new PdfPCell(new Phrase(GENDER, small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            dobGenderTable.addCell(cell);

            document.add(dobGenderTable);
            documentCustomerPDF.add(dobGenderTable);
            //End Date of Birth Gender

            //Age Occupation
            PdfPTable ageOccupationTable = new PdfPTable(4);
            ageOccupationTable.setWidths(new float[]{5f, 5f, 5f, 5f});
            ageOccupationTable.setWidthPercentage(100f);
            ageOccupationTable.setHorizontalAlignment(Element.ALIGN_LEFT);

            //Age cell
            cell = new PdfPCell(new Phrase(
                    "Age : ", small_bold));

            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            ageOccupationTable.addCell(cell);

            cell = new PdfPCell(new Phrase(AGE, small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            ageOccupationTable.addCell(cell);

            //Occupation Cell
            cell = new PdfPCell(new Phrase(
                    "Occupation : ", small_bold));

            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            ageOccupationTable.addCell(cell);

            cell = new PdfPCell(new Phrase(occupation, small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            ageOccupationTable.addCell(cell);

            document.add(ageOccupationTable);
            documentCustomerPDF.add(ageOccupationTable);
            //End Age Occupation

            //Residence Status & Country Of Residence
            PdfPTable residenceStatusCountryTable = new PdfPTable(4);
            residenceStatusCountryTable.setWidths(new float[]{5f, 5f, 5f, 5f});
            residenceStatusCountryTable.setWidthPercentage(100f);
            residenceStatusCountryTable.setHorizontalAlignment(Element.ALIGN_LEFT);

            //Residence Status cell
            cell = new PdfPCell(new Phrase(
                    "Residence Status : ", small_bold));

            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            residenceStatusCountryTable.addCell(cell);

            cell = new PdfPCell(new Phrase(residentRBString, small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            residenceStatusCountryTable.addCell(cell);

            //Residence Country Cell
            cell = new PdfPCell(new Phrase(
                    "Country of Residence  : ", small_bold));

            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            residenceStatusCountryTable.addCell(cell);

            cell = new PdfPCell(new Phrase(countryResidence, small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            residenceStatusCountryTable.addCell(cell);

            document.add(residenceStatusCountryTable);
            documentCustomerPDF.add(residenceStatusCountryTable);
            //End Residence Status & Country Of Residence

            //Mobile Number & Email -Id
            PdfPTable mobileEmailTable = new PdfPTable(4);
            mobileEmailTable.setWidths(new float[]{5f, 5f, 5f, 5f});
            mobileEmailTable.setWidthPercentage(100f);
            mobileEmailTable.setHorizontalAlignment(Element.ALIGN_LEFT);

            //Mobile Number cell
            cell = new PdfPCell(new Phrase(
                    "Mobile Number : ", small_bold));

            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            mobileEmailTable.addCell(cell);

            cell = new PdfPCell(new Phrase("+" + countryCode + " " + MOBILE, small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            mobileEmailTable.addCell(cell);

            //Email-Id
            cell = new PdfPCell(new Phrase(
                    "Email- Id  : ", small_bold));

            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            mobileEmailTable.addCell(cell);

            cell = new PdfPCell(new Phrase(EMAILID, small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            mobileEmailTable.addCell(cell);

            document.add(mobileEmailTable);
            documentCustomerPDF.add(mobileEmailTable);
            //End Mobile Number & Email -Id


            //Height & Weight
            PdfPTable heightWeightTable = new PdfPTable(4);
            heightWeightTable.setWidths(new float[]{5f, 5f, 5f, 5f});
            heightWeightTable.setWidthPercentage(100f);
            heightWeightTable.setHorizontalAlignment(Element.ALIGN_LEFT);

            //Height cell
            cell = new PdfPCell(new Phrase(
                    "Height (in Cms) : ", small_bold));

            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            heightWeightTable.addCell(cell);

            cell = new PdfPCell(new Phrase(heights, small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            heightWeightTable.addCell(cell);

            //Weight Cell
            cell = new PdfPCell(new Phrase(
                    "Weight (in Kgs)  : ", small_bold));

            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            heightWeightTable.addCell(cell);

            cell = new PdfPCell(new Phrase(weight, small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            heightWeightTable.addCell(cell);

            document.add(heightWeightTable);
            documentCustomerPDF.add(heightWeightTable);
            //End Height & WeightId

            document.add(new_line);
            documentCustomerPDF.add(new_line);

            //Question Table
            PdfPTable question1TableDGH = new PdfPTable(2);
            question1TableDGH.setWidths(new float[]{7f, 3f});
            question1TableDGH.setWidthPercentage(100f);
            question1TableDGH.setHorizontalAlignment(Element.ALIGN_LEFT);

            //Question 1 cell
            if (healthRBString.equalsIgnoreCase("no")) {
                cell = new PdfPCell(new Phrase(
                        "1) Are you at present in GOOD health? \n\nDetails : " + healthStatus, small_normal));
            } else {
                cell = new PdfPCell(new Phrase(
                        "1) Are you at present in GOOD health?  ", small_normal));
            }
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            question1TableDGH.addCell(cell);


            //Column 2
            cell = new PdfPCell(new Phrase(
                    healthRBString, small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            question1TableDGH.addCell(cell);
            document.add(question1TableDGH);
            documentCustomerPDF.add(question1TableDGH);
            //End Question 1 cell


            //Question Table
            PdfPTable weightLossTable = new PdfPTable(2);
            weightLossTable.setWidths(new float[]{7f, 3f});
            weightLossTable.setWidthPercentage(100f);
            weightLossTable.setHorizontalAlignment(Element.ALIGN_LEFT);

            //Question 1 cell
            if (weightLossRBString.equalsIgnoreCase("yes")) {
                cell = new PdfPCell(new Phrase(
                        "2) During the last one year, has there been any increase/decrease in your weight over 5 Kilograms. \n" +
                                "If yes, please provide full details in the space below \n \n\nDetails : " + weightLossDetails, small_normal));
            } else {
                cell = new PdfPCell(new Phrase(
                        "2) During the last one year, has there been any increase/decrease in your weight over 5 Kilograms."
                        , small_normal));
            }
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            weightLossTable.addCell(cell);


            //Column 2
            cell = new PdfPCell(new Phrase(
                    weightLossRBString, small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            weightLossTable.addCell(cell);
            document.add(weightLossTable);
            documentCustomerPDF.add(weightLossTable);
            //End Question 1 cell

            //Question 3 cell
            PdfPTable question2Table = new PdfPTable(2);
            question2Table.setWidths(new float[]{7f, 3f});
            question2Table.setWidthPercentage(100f);
            question2Table.setHorizontalAlignment(Element.ALIGN_LEFT);

            cell = new PdfPCell(new Phrase(
                    "3) Since the Date of Proposal of this policy ?", small_normal));
            cell.setColspan(2);
            question2Table.addCell(cell);
            document.add(question2Table);
            documentCustomerPDF.add(question2Table);

            PdfPTable question2ATable = new PdfPTable(2);
            question2ATable.setWidths(new float[]{7f, 3f});
            question2ATable.setWidthPercentage(100f);
            question2ATable.setHorizontalAlignment(Element.ALIGN_LEFT);

            if (DateOfProposalDetailsOptionARBString.equalsIgnoreCase("Yes")) {
                cell = new PdfPCell(new Phrase(
                        "\ta) Have you suffered from any illness / disease requiring treatment for 3 or more days? \n\nDetails : "
                                + dateOfProposalOptionAYes, small_normal));
            } else {
                cell = new PdfPCell(new Phrase(
                        "\ta) Have you suffered from any illness / disease requiring treatment for 3 or more days? ", small_normal));
            }
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            question2ATable.addCell(cell);


            //Column 2
            cell = new PdfPCell(new Phrase(
                    DateOfProposalDetailsOptionARBString, small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            question2ATable.addCell(cell);
            document.add(question2ATable);
            documentCustomerPDF.add(question2ATable);

            PdfPTable question2BTable = new PdfPTable(2);
            question2BTable.setWidths(new float[]{7f, 3f});
            question2BTable.setWidthPercentage(100f);
            question2BTable.setHorizontalAlignment(Element.ALIGN_LEFT);

            if (dateOfProposalOptionBRBString.equalsIgnoreCase("Yes")) {
                cell = new PdfPCell(new Phrase(
                        "\tb) Do you plan or have been advised to undergo any surgery or hospitalization or visit to doctor or practitioner for any physical, mental or emotional condition, injury, or sickness in near future. If yes, please provide details in the space below   \n\nDetails : "
                                + dateOfProposalOptionBYes, small_normal));
            } else {
                cell = new PdfPCell(new Phrase(
                        "\tb) Do you plan or have been advised to undergo any surgery or hospitalization or visit to doctor or practitioner for any physical, mental or emotional condition, injury, or sickness in near future. ", small_normal));
            }
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            question2BTable.addCell(cell);


            //Column 2
            cell = new PdfPCell(new Phrase(
                    dateOfProposalOptionBRBString, small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            question2BTable.addCell(cell);
            document.add(question2BTable);
            documentCustomerPDF.add(question2BTable);

            PdfPTable question2CTable = new PdfPTable(2);
            question2CTable.setWidths(new float[]{7f, 3f});
            question2CTable.setWidthPercentage(100f);
            question2CTable.setHorizontalAlignment(Element.ALIGN_LEFT);

            if (dateOfProposalOptionCRBString.equalsIgnoreCase("Yes")) {
                cell = new PdfPCell(new Phrase(
                        "\tc)  In the last 5 year, apart from minor ailments like cough/cold etc, have you received any treatment under doctor’s consultation and /or undergone any major surgery and/or been hospitalized and/or undergone major investigations like CT/MRI scan, Angiogram, Endoscopy, Biopsy etc?  If yes, please provide full details in the space below   \n\nDetails : "
                                + dateOfProposalOptionCYes, small_normal));
            } else {
                cell = new PdfPCell(new Phrase(
                        "\tc)  In the last 5 year, apart from minor ailments like cough/cold etc, have you received any treatment under doctor’s consultation and /or undergone any major surgery and/or been hospitalized and/or undergone major investigations like CT/MRI scan, Angiogram, Endoscopy, Biopsy etc? ", small_normal));
            }
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            question2CTable.addCell(cell);


            //Column 2
            cell = new PdfPCell(new Phrase(
                    dateOfProposalOptionCRBString, small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            question2CTable.addCell(cell);
            document.add(question2CTable);
            documentCustomerPDF.add(question2CTable);
            //End Question 3 cell


            //Question Table
            PdfPTable multipleDiseaseTable = new PdfPTable(2);
            multipleDiseaseTable.setWidths(new float[]{7f, 3f});
            multipleDiseaseTable.setWidthPercentage(100f);
            multipleDiseaseTable.setHorizontalAlignment(Element.ALIGN_LEFT);

            //Question 1 cell
            if (multipleDiseaseRBString.equalsIgnoreCase("yes")) {
                cell = new PdfPCell(new Phrase(
                        "4)\tDo you suffer from or have ever suffered from: Raised Blood sugar, Diabetes mellitus, high blood pressure, Disease of heart, lung, kidney, Liver, thyroid, brain/nervous system, bone/joint/spine, Genitourinary tract, any part of the body or blood disorder, digestive disorder, Gynaecological disorder, Psychiatric disorder, HIV/AIDS and/or cancer/tumour of any part of the body. " +
                                "If yes, please provide full details in the space below \n \n\nDetails : " + multipleDiseaseDetails, small_normal));
            } else {
                cell = new PdfPCell(new Phrase(
                        "4)\tDo you suffer from or have ever suffered from: Raised Blood sugar, Diabetes mellitus, high blood pressure, Disease of heart, lung, kidney, Liver, thyroid, brain/nervous system, bone/joint/spine, Genitourinary tract, any part of the body or blood disorder, digestive disorder, Gynaecological disorder, Psychiatric disorder, HIV/AIDS and/or cancer/tumour of any part of the body."
                        , small_normal));
            }
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            multipleDiseaseTable.addCell(cell);


            //Column 2
            cell = new PdfPCell(new Phrase(
                    multipleDiseaseRBString, small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            multipleDiseaseTable.addCell(cell);
            document.add(multipleDiseaseTable);
            documentCustomerPDF.add(multipleDiseaseTable);

            PdfPTable question5NewTable = new PdfPTable(2);
            question5NewTable.setWidths(new float[]{7f, 3f});
            question5NewTable.setWidthPercentage(100f);
            question5NewTable.setHorizontalAlignment(Element.ALIGN_LEFT);
            //Question 3 cell
            if (question5NewRBString.equalsIgnoreCase("yes")) {
                cell = new PdfPCell(new Phrase(
                        "5) Has any of your proposals for life insurance, critical illness or health insurance, ever been denied, declined, rejected, postponed or accepted with extra premium due to health or lifestyle reason If yes, please provide full details in the space below \n\nDetails : " + question5New, small_normal));
            } else {
                cell = new PdfPCell(new Phrase(
                        "5) Has any of your proposals for life insurance, critical illness or health insurance, ever been denied, declined, rejected, postponed or accepted with extra premium due to health or lifestyle reason.", small_normal));
            }
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            question5NewTable.addCell(cell);


            //Column 2
            cell = new PdfPCell(new Phrase(
                    question5NewRBString, small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            question5NewTable.addCell(cell);
            document.add(question5NewTable);
            documentCustomerPDF.add(question5NewTable);
            //End Question 3 cell


            PdfPTable question6NewTable = new PdfPTable(2);
            question6NewTable.setWidths(new float[]{7f, 3f});
            question6NewTable.setWidthPercentage(100f);
            question6NewTable.setHorizontalAlignment(Element.ALIGN_LEFT);

            //Question 4 cell
            if (question6NewRBString.equalsIgnoreCase("yes")) {
                cell = new PdfPCell(new Phrase(
                        "6) Are you exposed to any special hazard associated with your occupation (e.g. chemical factory, mines, explosives, corrosives, combative duties, oil exploration, high sea voyage etc.) which may render you susceptible to injuries or illnesses? \n" +
                                "If Yes, please provide details in the occupation questionnaire and submit along with this declaration.\n\n\nDetails : " + question6New, small_normal));
            } else {
                cell = new PdfPCell(new Phrase(
                        "6) Are you exposed to any special hazard associated with your occupation (e.g. chemical factory, mines, explosives, corrosives, combative duties, oil exploration, high sea voyage etc.) which may render you susceptible to injuries or illnesses? "
                        , small_normal));
            }
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            question6NewTable.addCell(cell);


            //Column 2
            cell = new PdfPCell(new Phrase(
                    question6NewRBString, small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            question6NewTable.addCell(cell);
            document.add(question6NewTable);
            documentCustomerPDF.add(question6NewTable);
            //End Question 4 cell


            PdfPTable question7NewTable = new PdfPTable(2);
            question7NewTable.setWidths(new float[]{7f, 3f});
            question7NewTable.setWidthPercentage(100f);
            question7NewTable.setHorizontalAlignment(Element.ALIGN_LEFT);
            //Question 5 cell
            if (question7NewRBString.equalsIgnoreCase("yes")) {
                cell = new PdfPCell(new Phrase(
                        "7) Are you a “Politically Exposed Person” (PEP) or a close relative of PEP?"
                                + "PEPs are individuals who are or have been entrusted with prominent public functions, i.e. heads / ministers of central / state govt., senior politicians, senior govt, judicial or military officials, senior executives of govt. companies, important political party officials, immediate family member of above persons (would include spouse, parents, siblings, children, spouses parents or siblings and close associates of PEPs"
                                + "\nIn case of change in your PEP status in future, you shall inform SBI Life of such change"
                                + " \nDetails : " + question7New, small_normal));
            } else {
                cell = new PdfPCell(new Phrase(
                        "7) Are you a “Politically Exposed Person” (PEP) or a close relative of PEP?"
                                + "\nIn case of change in your PEP status in future, you shall inform SBI Life of such change"
                                + "PEPs are individuals who are or have been entrusted with prominent public functions, i.e. heads / ministers of central / state govt., senior politicians, senior govt, judicial or military officials, senior executives of govt. companies, important political party officials, immediate family member of above persons (would include spouse, parents, siblings, children, spouses parents or siblings and close associates of PEPs",
                        small_normal));
            }
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            question7NewTable.addCell(cell);


            //Column 2
            cell = new PdfPCell(new Phrase(
                    question7NewRBString, small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            question7NewTable.addCell(cell);
            document.add(question7NewTable);
            documentCustomerPDF.add(question7NewTable);
            //End Question 5 cell

            PdfPTable question8NewTable = new PdfPTable(2);
            question8NewTable.setWidths(new float[]{7f, 3f});
            question8NewTable.setWidthPercentage(100f);
            question8NewTable.setHorizontalAlignment(Element.ALIGN_LEFT);

            cell = new PdfPCell(new Phrase(
                    "8) History of criminal proceedings, FIR or conviction", small_normal));
            cell.setColspan(2);
            question8NewTable.addCell(cell);
            document.add(question8NewTable);
            documentCustomerPDF.add(question8NewTable);


            PdfPTable question8NewATable = new PdfPTable(2);
            question8NewATable.setWidths(new float[]{7f, 3f});
            question8NewATable.setWidthPercentage(100f);
            question8NewATable.setHorizontalAlignment(Element.ALIGN_LEFT);

            //Question 6 cell
            if (question8NewARBString.equalsIgnoreCase("yes")) {
                cell = new PdfPCell(new Phrase(
                        "\ta) Has any FIR or Criminal Complaint ever been registered or lodged against you? "
                                + " \n\nDetails : " + question8NewA, small_normal));
            } else {
                cell = new PdfPCell(new Phrase(
                        "\ta) Has any FIR or Criminal Complaint ever been registered or lodged against you?",
                        small_normal));
            }
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            question8NewATable.addCell(cell);


            //Column 2
            cell = new PdfPCell(new Phrase(
                    question8NewARBString, small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            question8NewATable.addCell(cell);
            document.add(question8NewATable);
            documentCustomerPDF.add(question8NewATable);


            PdfPTable question8NewBTable = new PdfPTable(2);
            question8NewBTable.setWidths(new float[]{7f, 3f});
            question8NewBTable.setWidthPercentage(100f);
            question8NewBTable.setHorizontalAlignment(Element.ALIGN_LEFT);

            //Question 6 cell
            if (question8NewBRBString.equalsIgnoreCase("yes")) {
                cell = new PdfPCell(new Phrase(
                        "\tb) Have there been any Criminal proceedings initiated against you either in the past or in the present? "
                                + " \n\nDetails : " + question8NewB, small_normal));
            } else {
                cell = new PdfPCell(new Phrase(
                        "\tb) Have there been any Criminal proceedings initiated against you either in the past or in the present? ",
                        small_normal));
            }
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            question8NewBTable.addCell(cell);


            //Column 2
            cell = new PdfPCell(new Phrase(
                    question8NewBRBString, small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            question8NewBTable.addCell(cell);
            document.add(question8NewBTable);
            documentCustomerPDF.add(question8NewBTable);


            PdfPTable question8NewCTable = new PdfPTable(2);
            question8NewCTable.setWidths(new float[]{7f, 3f});
            question8NewCTable.setWidthPercentage(100f);
            question8NewCTable.setHorizontalAlignment(Element.ALIGN_LEFT);

            //Question 6 cell
            if (question8NewCRBString.equalsIgnoreCase("yes")) {
                cell = new PdfPCell(new Phrase(
                        "\tc) Do you have any history of conviction under any criminal proceedings in India or abroad?"
                                + " \n\nDetails : " + question8NewC, small_normal));
            } else {
                cell = new PdfPCell(new Phrase(
                        "\tc) Do you have any history of conviction under any criminal proceedings in India or abroad? ",
                        small_normal));
            }
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            question8NewCTable.addCell(cell);


            //Column 2
            cell = new PdfPCell(new Phrase(
                    question8NewCRBString, small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            question8NewCTable.addCell(cell);
            document.add(question8NewCTable);
            documentCustomerPDF.add(question8NewCTable);
            //End Question 6 cell


            //Question 7 cell
            PdfPTable question9NewTable = new PdfPTable(2);
            question9NewTable.setWidths(new float[]{7f, 3f});
            question9NewTable.setWidthPercentage(100f);
            question9NewTable.setHorizontalAlignment(Element.ALIGN_LEFT);
            //Question 5 cell
            if (question9NewRBString.equalsIgnoreCase("yes")) {
                cell = new PdfPCell(new Phrase(
                        "9) Do you take part in or have you any intention of taking part in any hazardous sports, hobbies, activities or pursuits, for example mountaineering, Diving, racing or aviation other than as a fare paying passenger, that could be dangerous in any way?"
                                + " \n\nDetails : " + question9New, small_normal));
            } else {
                cell = new PdfPCell(new Phrase(
                        "9) Do you take part in or have you any intention of taking part in any hazardous sports, hobbies, activities or pursuits, for example mountaineering, Diving, racing or aviation other than as a fare paying passenger, that could be dangerous in any way?",
                        small_normal));
            }
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            question9NewTable.addCell(cell);


            //Column 2
            cell = new PdfPCell(new Phrase(
                    question9NewRBString, small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            question9NewTable.addCell(cell);
            document.add(question9NewTable);
            documentCustomerPDF.add(question9NewTable);
            //End Question 7 cell

            //Question 8 cell
            PdfPTable question10Table = new PdfPTable(1);
            question10Table.setWidthPercentage(100f);
            question10Table.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell = new PdfPCell(new Phrase(
                    "10) ", small_normal));

            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            question10Table.addCell(cell);

            cell = new PdfPCell(new Phrase(
                    "\ta) Do you consume any of the following? ", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            question10Table.addCell(cell);

            document.add(question10Table);
            documentCustomerPDF.add(question10Table);


            PdfPTable question10ATitleTable = new PdfPTable(3);
            question10ATitleTable.setWidths(new float[]{3f, 2f, 5f});
            question10ATitleTable.setWidthPercentage(100f);
            question10ATitleTable.setHorizontalAlignment(Element.ALIGN_LEFT);

            cell = new PdfPCell(new Phrase(
                    "Substance consumed", small_bold));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            question10ATitleTable.addCell(cell);

            cell = new PdfPCell(new Phrase(
                    "YES/NO", small_bold));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            question10ATitleTable.addCell(cell);

            cell = new PdfPCell(new Phrase(
                    "If YES, please give below details", small_bold));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            question10ATitleTable.addCell(cell);

            document.add(question10ATitleTable);
            documentCustomerPDF.add(question10ATitleTable);


            //1st row 8A
            PdfPTable question10A1stTable = new PdfPTable(4);
            question10A1stTable.setWidths(new float[]{3f, 2f, 3f, 2f});
            question10A1stTable.setWidthPercentage(100f);
            question10A1stTable.setHorizontalAlignment(Element.ALIGN_LEFT);

            cell = new PdfPCell(new Phrase(
                    "", small_bold));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            question10A1stTable.addCell(cell);

            cell = new PdfPCell(new Phrase(
                    "", small_bold));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            question10A1stTable.addCell(cell);

            cell = new PdfPCell(new Phrase(
                    "Quantity", small_bold));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            question10A1stTable.addCell(cell);

            cell = new PdfPCell(new Phrase(
                    "No of years used", small_bold));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            question10A1stTable.addCell(cell);

            document.add(question10A1stTable);
            documentCustomerPDF.add(question10A1stTable);
            // End 1st row 8A

            //2nd row 8A
            PdfPTable question10ATobaccoTable = new PdfPTable(4);
            question10ATobaccoTable.setWidths(new float[]{3f, 2f, 3f, 2f});
            question10ATobaccoTable.setWidthPercentage(100f);
            question10ATobaccoTable.setHorizontalAlignment(Element.ALIGN_LEFT);

            cell = new PdfPCell(new Phrase(
                    "Tobacco (cigs, cigar, gutkha, etc)", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            question10ATobaccoTable.addCell(cell);

            cell = new PdfPCell(new Phrase(
                    question10A1RBString, small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            question10ATobaccoTable.addCell(cell);


            if (question10A1RBString.equalsIgnoreCase("Yes")) {

                cell = new PdfPCell(new Phrase(
                        "Sticks/packets Per day : " + sticksPackets10A1, small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                question10ATobaccoTable.addCell(cell);

                cell = new PdfPCell(new Phrase(noOfYearsUsed10A1, small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                question10ATobaccoTable.addCell(cell);
            } else {
                cell = new PdfPCell(new Phrase(
                        "", small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                question10ATobaccoTable.addCell(cell);

                cell = new PdfPCell(new Phrase("", small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                question10ATobaccoTable.addCell(cell);
            }

            document.add(question10ATobaccoTable);
            documentCustomerPDF.add(question10ATobaccoTable);
            // End 2nd row 8A


            //3rd row 8A
            PdfPTable question10AAlcoholTable = new PdfPTable(4);
            question10AAlcoholTable.setWidths(new float[]{3f, 2f, 3f, 2f});
            question10AAlcoholTable.setWidthPercentage(100f);
            question10AAlcoholTable.setHorizontalAlignment(Element.ALIGN_LEFT);

            cell = new PdfPCell(new Phrase(
                    "Alcohol", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            question10AAlcoholTable.addCell(cell);

            cell = new PdfPCell(new Phrase(
                    question10A2RBString, small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            question10AAlcoholTable.addCell(cell);


            if (question10A2RBString.equalsIgnoreCase("Yes")) {

                cell = new PdfPCell(new Phrase(
                        "Kind of alcohol : " + quantity10A2AlcoholType + "\n" +
                                "Quantity per week " + quantity10A2Ml + "ml", small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                question10AAlcoholTable.addCell(cell);

                cell = new PdfPCell(new Phrase(
                        "No of years used : " + noOfYearsUsed10A2, small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                question10AAlcoholTable.addCell(cell);
            } else {
                cell = new PdfPCell(new Phrase(
                        "", small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                question10AAlcoholTable.addCell(cell);

                cell = new PdfPCell(new Phrase(
                        "", small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                question10AAlcoholTable.addCell(cell);
            }

            document.add(question10AAlcoholTable);
            documentCustomerPDF.add(question10AAlcoholTable);
            // End 3rd row 8A

            //4th row 8A
            PdfPTable question10ANarcoticsTable = new PdfPTable(4);
            question10ANarcoticsTable.setWidths(new float[]{3f, 2f, 3f, 2f});
            question10ANarcoticsTable.setWidthPercentage(100f);
            question10ANarcoticsTable.setHorizontalAlignment(Element.ALIGN_LEFT);

            cell = new PdfPCell(new Phrase(
                    "Any Narcotics", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            question10ANarcoticsTable.addCell(cell);

            cell = new PdfPCell(new Phrase(
                    question10A3RBString, small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            question10ANarcoticsTable.addCell(cell);


            if (question10A3RBString.equalsIgnoreCase("Yes")) {

                cell = new PdfPCell(new Phrase(
                        "Type of Drug  : " + typeOfDrug10A3, small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                question10ANarcoticsTable.addCell(cell);

                cell = new PdfPCell(new Phrase(
                        "No of years used : " + noOfYearsUsed10A3, small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                question10ANarcoticsTable.addCell(cell);
            } else {
                cell = new PdfPCell(new Phrase("", small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                question10ANarcoticsTable.addCell(cell);

                cell = new PdfPCell(new Phrase("", small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                question10ANarcoticsTable.addCell(cell);
            }

            document.add(question10ANarcoticsTable);
            documentCustomerPDF.add(question10ANarcoticsTable);

            // End 4th row 8A

            // Question 8 Optio B
            PdfPTable question10BTable = new PdfPTable(1);
            question10BTable.setWidthPercentage(100f);
            question10BTable.setHorizontalAlignment(Element.ALIGN_LEFT);
            //Question 5 cell
            cell = new PdfPCell(new Phrase(
                    "\tb) Quitting details in last 1 year \n\n Details : " +
                            consumptionQuit10B, small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            question10BTable.addCell(cell);
            document.add(question10BTable);
            documentCustomerPDF.add(question10BTable);
            // End Question 8 Optio B

            //End Question 8


            if (GENDER.equalsIgnoreCase("female")) {
                // Question 9
                PdfPTable question11Table = new PdfPTable(2);
                question11Table.setWidths(new float[]{7f, 3f});
                question11Table.setWidthPercentage(100f);
                question11Table.setHorizontalAlignment(Element.ALIGN_LEFT);

                cell = new PdfPCell(new Phrase(
                        "11) For Female Lives Only: ", small_normal));
                cell.setColspan(2);
                question11Table.addCell(cell);
                document.add(question11Table);
                documentCustomerPDF.add(question11Table);

                PdfPTable question11ATable = new PdfPTable(2);
                question11ATable.setWidths(new float[]{7f, 3f});
                question11ATable.setWidthPercentage(100f);
                question11ATable.setHorizontalAlignment(Element.ALIGN_LEFT);

                //Question 6 cell
                if (question11ARBString.equalsIgnoreCase("yes")) {
                    cell = new PdfPCell(new Phrase(
                            "\ta) Are you presently pregnant?  "
                                    + " \n\nDetails : " + pregnancyMonth11A, small_normal));
                } else {
                    cell = new PdfPCell(new Phrase(
                            "\ta) Are you presently pregnant?",
                            small_normal));
                }
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                question11ATable.addCell(cell);


                //Column 2
                cell = new PdfPCell(new Phrase(
                        question11ARBString, small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                question11ATable.addCell(cell);
                document.add(question11ATable);
                documentCustomerPDF.add(question11ATable);


                PdfPTable question11BTable = new PdfPTable(2);
                question11BTable.setWidths(new float[]{7f, 3f});
                question11BTable.setWidthPercentage(100f);
                question11BTable.setHorizontalAlignment(Element.ALIGN_LEFT);

                if (question11BRBString.equalsIgnoreCase("yes")) {
                    cell = new PdfPCell(new Phrase(
                            "\tb) Have you ever suffered from or have undergone any investigations or treatment for any gynecological problems related to cervix, uterus, ovaries, breast, breast lump/cyst etc or undergone surgical procedure like hysterectomy.  "
                                    + " \n\nDetails : " + gynecologicalProblem11B, small_normal));
                } else {
                    cell = new PdfPCell(new Phrase(
                            "\tb) Have you ever suffered from or have undergone any investigations or treatment for any gynecological problems related to cervix, uterus, ovaries, breast, breast lump/cyst etc or undergone surgical procedure like hysterectomy.",
                            small_normal));
                }
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                question11BTable.addCell(cell);


                //Column 2
                cell = new PdfPCell(new Phrase(
                        question11BRBString, small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                question11BTable.addCell(cell);
                document.add(question11BTable);
                documentCustomerPDF.add(question11BTable);
                //End Question 9
            }


            //Question Loop cell

            if (globalDataList.size() > 0) {
                PdfPTable questionDetailsTable = new PdfPTable(1);
                questionDetailsTable.setWidthPercentage(100f);
                questionDetailsTable.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell = new PdfPCell(new Phrase(
                        "Space for providing details pertaining to the above questions. If required, please use an additional sheet of paper and attach", small_bold));

                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                questionDetailsTable.addCell(cell);
                document.add(questionDetailsTable);
                documentCustomerPDF.add(questionDetailsTable);

                PdfPTable questionDetailsLoopTable = new PdfPTable(2);
                questionDetailsLoopTable.setWidths(new float[]{2f, 4f});
                questionDetailsLoopTable.setWidthPercentage(100f);
                questionDetailsLoopTable.setHorizontalAlignment(Element.ALIGN_LEFT);

                cell = new PdfPCell(new Phrase(
                        "Q No", small_bold));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                questionDetailsLoopTable.addCell(cell);

                cell = new PdfPCell(new Phrase(
                        "Details for questions answered as Yes above or any other information as required ", small_bold));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                questionDetailsLoopTable.addCell(cell);

                for (int i = 0; i < globalDataList.size(); i++) {
                    cell = new PdfPCell(new Phrase(
                            globalDataList.get(i).getQuestionNumber(), small_normal));
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    questionDetailsLoopTable.addCell(cell);

                    cell = new PdfPCell(new Phrase(
                            globalDataList.get(i).getQuestionDescription(), small_normal));
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    questionDetailsLoopTable.addCell(cell);
                }

                document.add(questionDetailsLoopTable);
                documentCustomerPDF.add(questionDetailsLoopTable);
                //End Question Loop
            }

            document.add(new_line);
            document.add(new_line);
            documentCustomerPDF.add(new_line);
            documentCustomerPDF.add(new_line);

            //COVID Questionnaire
            //Policy Number Table
            PdfPTable policyNumberTable = new PdfPTable(2);
            policyNumberTable.setWidths(new float[]{5f, 5f});
            policyNumberTable.setWidthPercentage(100f);
            policyNumberTable.setHorizontalAlignment(Element.ALIGN_LEFT);

            cell = new PdfPCell(new Phrase("COVID Questionnaire (UWM029)",
                    headerBold));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
//            cell.setBorder(Rectangle.NO_BORDER);
            cell.setColspan(2);
            policyNumberTable.addCell(cell);


            cell = new PdfPCell(new Phrase(
                    "Proposal Number : ", small_bold));

            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            policyNumberTable.addCell(cell);

            cell = new PdfPCell(new Phrase(policyNumber, small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            policyNumberTable.addCell(cell);

            cell = new PdfPCell(new Phrase(
                    "Name of the Proposer/ Life to be assured : ", small_bold));

            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            policyNumberTable.addCell(cell);

            cell = new PdfPCell(new Phrase(lifeAssuredName, small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            policyNumberTable.addCell(cell);

            document.add(policyNumberTable);
            documentCustomerPDF.add(policyNumberTable);
            //End Policy Number Table


            document.add(new_line);
            documentCustomerPDF.add(new_line);

            //Question Table
            PdfPTable question1Table = new PdfPTable(2);
            question1Table.setWidths(new float[]{5f, 5f});
            question1Table.setWidthPercentage(100f);
            question1Table.setHorizontalAlignment(Element.ALIGN_LEFT);

            //Question 1 cell
            cell = new PdfPCell(new Phrase(
                    "1.Have you visited any foreign country after 1.1.2020,then please answer the following", small_normal));

            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            question1Table.addCell(cell);


            //Column 2
            cell = new PdfPCell(new Phrase(
                    pastForeignVisit, small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            question1Table.addCell(cell);


            if (pastForeignVisit.equalsIgnoreCase("yes")) {

                cell = new PdfPCell(new Phrase(
                        "1.1 Name of country visited", small_normal));

                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                question1Table.addCell(cell);

                cell = new PdfPCell(new Phrase(
                        pastCountryName, small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                question1Table.addCell(cell);

                cell = new PdfPCell(new Phrase(
                        "1.2 Duration", small_normal));

                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                question1Table.addCell(cell);

                cell = new PdfPCell(new Phrase(
                        "From: " + pastFromDuration + "\n" + "To: " + pastToDuration, small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                question1Table.addCell(cell);

                cell = new PdfPCell(new Phrase(
                        "1.3 Date of return to India", small_normal));

                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                question1Table.addCell(cell);

                cell = new PdfPCell(new Phrase(
                        pastReturnDate, small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                question1Table.addCell(cell);


                cell = new PdfPCell(new Phrase(
                        "1.4 If you have been screened at the airport please providecopy of report provided", small_normal));

                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                question1Table.addCell(cell);

                cell = new PdfPCell(new Phrase(
                        screeneAtAirport, small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                question1Table.addCell(cell);
            } else {

                cell = new PdfPCell(new Phrase(
                        "1.1 Name of country visited", small_normal));

                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                question1Table.addCell(cell);

                cell = new PdfPCell(new Phrase(
                        "NA", small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                question1Table.addCell(cell);

                cell = new PdfPCell(new Phrase(
                        "1.2 Duration", small_normal));

                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                question1Table.addCell(cell);

                cell = new PdfPCell(new Phrase(
                        "From: NA\n" + "To: NA", small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                question1Table.addCell(cell);

                cell = new PdfPCell(new Phrase(
                        "1.3 Date of return to India", small_normal));

                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                question1Table.addCell(cell);

                cell = new PdfPCell(new Phrase(
                        "NA", small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                question1Table.addCell(cell);


                cell = new PdfPCell(new Phrase(
                        "1.4 If you have been screened at the airport please providecopy of report provided", small_normal));

                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                question1Table.addCell(cell);

                cell = new PdfPCell(new Phrase(
                        "NA", small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                question1Table.addCell(cell);
            }


            cell = new PdfPCell(new Phrase(
                    "2 Have you been tested for COVID-19. If yes, all thereports of same till date", small_normal));

            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            question1Table.addCell(cell);

            cell = new PdfPCell(new Phrase(
                    testedForCovid, small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            question1Table.addCell(cell);


            cell = new PdfPCell(new Phrase(
                    "3 Please confirm if either of the following is applicable to you", small_normal));
            cell.setColspan(2);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            question1Table.addCell(cell);

            cell = new PdfPCell(new Phrase(
                    "3.1 Kept in home quarantine anytime (till date) since1.1.2020", small_normal));

            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            question1Table.addCell(cell);

            cell = new PdfPCell(new Phrase(
                    homeQuarantine, small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            question1Table.addCell(cell);


            cell = new PdfPCell(new Phrase(
                    "3.2 Kept under observation anytime (till date) since1.1.2020", small_normal));

            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            question1Table.addCell(cell);

            cell = new PdfPCell(new Phrase(
                    underObservation, small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            question1Table.addCell(cell);


            cell = new PdfPCell(new Phrase(
                    "3.3 Kept in home isolation/self isolation anytime (till date)since 1.1.2020", small_normal));

            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            question1Table.addCell(cell);

            cell = new PdfPCell(new Phrase(
                    selfIsolation, small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            question1Table.addCell(cell);

            cell = new PdfPCell(new Phrase(
                    "4 Do you plan to travel to any foreign countries in next 6months", small_normal));

            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            question1Table.addCell(cell);

            cell = new PdfPCell(new Phrase(
                    futureForeignVisit, small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            question1Table.addCell(cell);

            if (futureForeignVisit.equalsIgnoreCase("yes")) {

                cell = new PdfPCell(new Phrase(
                        "4.1 Name of country visited", small_normal));

                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                question1Table.addCell(cell);

                cell = new PdfPCell(new Phrase(
                        futureCountryName, small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                question1Table.addCell(cell);

                cell = new PdfPCell(new Phrase(
                        "4.2 Duration", small_normal));

                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                question1Table.addCell(cell);

                cell = new PdfPCell(new Phrase(
                        "From: " + futureFromDuration + "\n" + "To: " + futureToDuration, small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                question1Table.addCell(cell);

                cell = new PdfPCell(new Phrase(
                        "4.3 Date of return to India", small_normal));

                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                question1Table.addCell(cell);

                cell = new PdfPCell(new Phrase(
                        futureReturnDate, small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                question1Table.addCell(cell);

            } else {

                cell = new PdfPCell(new Phrase(
                        "4.1 Name of country visited", small_normal));

                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                question1Table.addCell(cell);

                cell = new PdfPCell(new Phrase(
                        "NA", small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                question1Table.addCell(cell);

                cell = new PdfPCell(new Phrase(
                        "4.2 Duration", small_normal));

                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                question1Table.addCell(cell);

                cell = new PdfPCell(new Phrase(
                        "From: NA\n" + "To: NA", small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                question1Table.addCell(cell);

                cell = new PdfPCell(new Phrase(
                        "4.3 Date of return to India", small_normal));

                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                question1Table.addCell(cell);

                cell = new PdfPCell(new Phrase(
                        "NA", small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                question1Table.addCell(cell);

            }


            cell = new PdfPCell(new Phrase(
                    "5 Have you or your immediate family members/co-habitants suffered from any signs & symptoms of flu(cough, cold, fever more than 05 days) since 1.1.2020(whether any medical consultation taken or not)?", small_normal));

            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            question1Table.addCell(cell);

            cell = new PdfPCell(new Phrase(
                    familyMemberSymptoms, small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            question1Table.addCell(cell);

            cell = new PdfPCell(new Phrase(
                    "6 Have you or your immediate family members undergoneor been advised to undergo any test/investigations orhospitalized for observation or treatment in past 2 monthsfor respiratory symptoms?", small_normal));

            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            question1Table.addCell(cell);

            cell = new PdfPCell(new Phrase(
                    respiratorySymptoms, small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            question1Table.addCell(cell);

            cell = new PdfPCell(new Phrase(
                    "7 Have you or any of your immediate familymembers/cohabitants come in contact with suspected orconfirmed cases of coronavirus", small_normal));

            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            question1Table.addCell(cell);

            cell = new PdfPCell(new Phrase(
                    contactWithCorona, small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            question1Table.addCell(cell);

            document.add(question1Table);
            documentCustomerPDF.add(question1Table);
            // End COvid Questionnaire

            PdfPTable LADeclarationTitleTable = new PdfPTable(2);
            LADeclarationTitleTable.setWidths(new float[]{2f, 4f});
            LADeclarationTitleTable.setWidthPercentage(100f);
            LADeclarationTitleTable.setHorizontalAlignment(Element.ALIGN_LEFT);

            Paragraph paraLADeclarationTitle = new Paragraph("\nDeclaration by Life Assured:", headerBold);
            paraLADeclarationTitle.setAlignment(Element.ALIGN_LEFT);
            // LADeclarationTitleTable.add(paraLADeclarationTitle);
            document.add(LADeclarationTitleTable);
            documentCustomerPDF.add(LADeclarationTitleTable);

            Paragraph paraLADeclaration = new Paragraph("I hereby declare that the above statements, answers " +
                    "and/or particulars given by me are true and complete in all respects to the best of my knowledge." +
                    "  I understand that the information provided by me will form the basis for the revival of the " +
                    "insurance policy and that the statements in this declaration constitute warranties. " +
                    "If there is any mis-statement or suppression of material information or if any untrue statements " +
                    "are contained therein or in case of fraud, the said contract shall be treated as per the provisions" +
                    " of section 45 of the Insurance Act, 1938, as amended from time to time. For complete details of " +
                    "the section and the definition of ‘date of policy’, " +
                    "please refer to section 45 of the Insurance Act, 1938, as amended from time to time." +

                    "\nI understand that, the REVIVAL WILL NOT BE CONSIDERED UNTIL THE FULL PREMIUM" +
                    " INCLUDING TAXES AND LATE FEE, IFANY, IS PAID BY ME. I agree that the amount held" +
                    " in policy deposit shall not earn any interest except as may be provided in the " +
                    "relevant regulations.\n" +
                    "\n" +
                    "I understand and agree that risk cover and other benefits will not recommence until a written acceptance of this revival request is issued by the company and THAT THE BENEFITS UNDER THE POLICY shall be strictly as per the terms and conditions of the policy. \n" +
                    "\n" +
                    "I understand that the insurance contract will be governed by the provisions of all the applicable Statutes, as amended from time to time. \n" +
                    "\n" +
                    "I undertake to undergo all medical tests as may be required by the Company for the revival of the policy at my cost. \n" +
                    "\n" +
                    "I agree that by submitting this request and declaration, I will be bound by all" +
                    " the statements/disclosures of material facts made through the electronic process " +
                    "in the same manner and to the same extent, as if I have signed and submitted the " +
                    "written request and declaration for revival of my insurance policy to the Company. " +
                    "I accept and agree to affix my signature (in electronic mode/tablet/mobile) here\n", small_normal);
            document.add(paraLADeclaration);
            documentCustomerPDF.add(paraLADeclaration);


            Paragraph paraLAAadharConsentTitle = new Paragraph("\nAadhaar Consent – Life Assured:", headerBold);
            paraLAAadharConsentTitle.setAlignment(Element.ALIGN_LEFT);
            document.add(paraLAAadharConsentTitle);
            documentCustomerPDF.add(paraLAAadharConsentTitle);

            Paragraph paraLAAadharConsent = new Paragraph("I, " + lifeAssuredName + ", hereby give my " +
                    "voluntary consent to SBI Life Insurance Company Limited (SBI Life) and authorise " +
                    "the Company to obtain necessary details like Name, DOB, Address, Mobile Number, " +
                    "Email, Photograph through the QR code available on my Aadhaar card / XML File " +
                    "shared using the offline verification process of UIDAI. \n" +
                    "\n" +

                    "I understand and agree that this information will be exclusively used by SBI Life only for the KYC purpose and for all service aspects related to my policy/ies." +
                    "wherever KYC requirements have to be complied with, right from issue of policies after acceptance of " +
                    "risk under my proposals for life insurance, various payments that may have to be made under the policies," +
                    " various contingencies where the KYC information is mandatory, till the contract is terminated." +
                    "I have duly been made aware that I can also use alternative KYC documents like " +
                    "Passport, Voter's ID Card, Driving licence, NREGA job card, letter from National " +
                    "Population Register, in lieu of Aadhaar for the purpose of completing my KYC " +
                    "formalities. I understand and agree that the details so obtained shall be stored " +
                    "with SBI Life and be shared solely for the purpose of issuing insurance policy to " +
                    "me and for servicing them. I will not hold SBI Life or any of its authorized " +
                    "officials responsible in case of any incorrect information provided by me. I " +
                    "further authorize SBI Life that it may use my mobile number for sending SMS alerts " +
                    "to me regarding various servicing and other matters related to my policy/ies.\n",
                    small_normal);
            document.add(paraLAAadharConsent);
            documentCustomerPDF.add(paraLAAadharConsent);

            document.add(new_line);
            documentCustomerPDF.add(new_line);

            PdfPTable declarationTable = new PdfPTable(1);
            declarationTable.setWidthPercentage(100f);
            declarationTable.setHorizontalAlignment(Element.ALIGN_LEFT);

            cell = new PdfPCell((new Phrase("Declaration to be given by the Proposer/Life to be assured", headerBold)));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            declarationTable.addCell(cell);

            cell = new PdfPCell((new Phrase("I declare that the answers given above are true and to the best of my knowledge and that I have not withheld any materialinformation that may influence the assessment or acceptance of this application.", small_bold)));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            declarationTable.addCell(cell);

            cell = new PdfPCell((new Phrase("I agree that this form will constitute part of my application for life assurance and that failure to disclose any material factknown to me may invalidate the contract.", small_bold)));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            declarationTable.addCell(cell);

            document.add(declarationTable);
            documentCustomerPDF.add(declarationTable);

            PdfPTable signatureTable = new PdfPTable(2);
            signatureTable.setWidths(new float[]{5f, 5f});
            signatureTable.setWidthPercentage(100f);
            signatureTable.setHorizontalAlignment(Element.ALIGN_LEFT);

            String locationCord = " Latitude: " + mLatLng.latitude + ", Longitude: " + mLatLng.longitude;
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.ENGLISH);//"yyyy-MM-dd HH:mm:ss");
            String currentDateTime = dateFormat.format(new Date());

            /*e signed by verification code sent on mobile number 9687203172 at 04-06-2020
            08:39:06, GPS Coordinates ( Latitude: 17.5172684, Longitude: 73.9548709)*/

           /* Paragraph paraSignedDeclaration = new Paragraph("Declaration is signed by "
                    + userName + ", User Code - " + commonMethods.GetUserID(context) +
                    " with verification code sent on mobile number " + MOBILE + " at " +
                    currentDateTime + ", GPS Coordinates (" + locationCord + ") and with user ID and password authentication",
                    small_normal);*/
            cell = new PdfPCell((new Phrase("Signature of Proposer/ Life to be assured\nThis document is eSigned " +
                    "by " + lifeAssuredName, small_bold)));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            signatureTable.addCell(cell);

            cell = new PdfPCell((new Phrase("e signed by verification code sent on mobile number " +
                    MOBILE + " at " + currentDateTime + ", GPS Coordinates (" + locationCord + ")", small_bold)));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);

            signatureTable.addCell(cell);

            document.add(signatureTable);
            documentCustomerPDF.add(signatureTable);


            PdfPTable placeTable = new PdfPTable(2);
            placeTable.setWidths(new float[]{5f, 5f});
            placeTable.setWidthPercentage(100f);
            placeTable.setHorizontalAlignment(Element.ALIGN_LEFT);

            cell = new PdfPCell((new Phrase("Place : ", small_bold)));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            placeTable.addCell(cell);

            Calendar present_date = Calendar.getInstance();
            int mDay = present_date.get(Calendar.DAY_OF_MONTH);
            int mMonth = present_date.get(Calendar.MONTH);
            int mYear = present_date.get(Calendar.YEAR);

            String CurrentDate = mDay + "-" + (mMonth + 1) + "-" + mYear;


            cell = new PdfPCell((new Phrase("Date: " + CurrentDate, small_bold)));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);

            placeTable.addCell(cell);

            document.add(placeTable);
            documentCustomerPDF.add(placeTable);

            Paragraph paraAgentDeclarationTitle = new Paragraph("\nDeclaration by Intermediary", headerBold);
            paraAgentDeclarationTitle.setAlignment(Element.ALIGN_LEFT);
            document.add(paraAgentDeclarationTitle);
            documentCustomerPDF.add(paraAgentDeclarationTitle);

            Paragraph paraAgentDeclaration = new Paragraph("I hereby declare that I have read out and explained the " +
                    "contents of this declaration of good health form for requesting revival of the insurance " +
                    "policy from SBI Life Insurance Company Ltd. to the Life Assured and that he/she declared " +
                    "that he/she has understood the same completely and that he/she agrees to abide by all the " +
                    "terms and conditions of the same.\n" +

                    "I hereby declare that I have fully explained to the Life Assured the answers to the " +
                    "questions that form the basis of the revival of the insurance policy and  I also explained " +
                    "to the Life Assured that if there is any mis-statement or suppression of material " +
                    "information or if any untrue statements are contained therein or in case of fraud, the said" +
                    " contract shall be treated as per the provisions of Section 45 of the Insurance Act 1938 as " +
                    "amended from time to time and the Life Asured has completely understood the importance of " +
                    "giving complete and accurate information to every question in the declaration of good " +
                    "health form and the importance of each declaration in the form..\n" +
                    "I hereby declare that I have explained the contents of this form to the Proposer in English" +
                    " Language. \n" +
                    "I also declare that I have truly and correctly recorded the answers given by the Life " +
                    "Assured and that the Life Assured has shared his verification code in my presence, after " +
                    "fully understanding the contents thereof.\n",
                    small_normal);
            document.add(paraAgentDeclaration);
            documentCustomerPDF.add(paraAgentDeclaration);
            /*Declaration is signed by SUJIT KUMAR GHOSH, User Code - 990134795 with user ID and password authentication*/
            Paragraph paraSignedDeclaration = new Paragraph("Declaration is signed by "
                    + userName + ", User Code - " + commonMethods.GetUserID(context) +
                    " with user ID and password authentication",
                    small_normal);

            document.add(paraSignedDeclaration);
            documentCustomerPDF.add(paraSignedDeclaration);
            document.add(new_line);
            documentCustomerPDF.add(new_line);


            PdfPTable tableImageProof = new PdfPTable(1);
            tableImageProof.setWidthPercentage(100);

            PdfPCell Nocell = new PdfPCell(new Paragraph("Image of ID Proof Uploaded", small_bold));
            tableImageProof.addCell(Nocell);


            PdfPCell imageCell = new PdfPCell(getImageForPDF(identityProofPhotoBitmap));
            imageCell.setHorizontalAlignment(Element.ALIGN_TOP);
            imageCell.setPadding(5);
            tableImageProof.addCell(imageCell);
            document.add(tableImageProof);
            documentCustomerPDF.add(tableImageProof);
            document.add(new_line);
            documentCustomerPDF.add(new_line);

            PdfPTable tableAddressProof = new PdfPTable(1);
            tableAddressProof.setWidthPercentage(100);

            Nocell = new PdfPCell(new Paragraph("Image of Address Proof Uploaded", small_bold));
            tableAddressProof.addCell(Nocell);

            imageCell = new PdfPCell(getImageForPDF(addressProofPhotoBitmap));
            imageCell.setHorizontalAlignment(Element.ALIGN_TOP);
            imageCell.setPadding(5);
            tableAddressProof.addCell(imageCell);
            document.add(tableAddressProof);
            documentCustomerPDF.add(tableAddressProof);
            document.add(new_line);
            documentCustomerPDF.add(new_line);

            if (!TextUtils.isEmpty(edt_document_upload_document_others.getText().toString())) {
                PdfPTable tableOtherProof = new PdfPTable(1);
                tableOtherProof.setWidthPercentage(100);

                Nocell = new PdfPCell(new Paragraph("Image of ID OtherProof Uploaded", small_bold));
                tableOtherProof.addCell(Nocell);

                imageCell = new PdfPCell(getImageForPDF(otherPhotoBitmap));
                imageCell.setHorizontalAlignment(Element.ALIGN_TOP);
                imageCell.setPadding(5);
                tableOtherProof.addCell(imageCell);
                document.add(tableOtherProof);
                documentCustomerPDF.add(tableOtherProof);
                document.add(new_line);
                documentCustomerPDF.add(new_line);
            }

            //Customer PDF End

            PdfPTable officeTable = new PdfPTable(1);
            officeTable.setWidthPercentage(100f);
            officeTable.setHorizontalAlignment(Element.ALIGN_LEFT);


            cell = new PdfPCell(new Phrase("For Office Use", headerBold));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            officeTable.addCell(cell);

            cell = new PdfPCell(new Phrase(
                    "KYC Details", small_bold));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            officeTable.addCell(cell);

            document.add(officeTable);

            PdfPTable userPhotoTable = new PdfPTable(1);
            userPhotoTable.setWidthPercentage(100);

            cell = new PdfPCell(new Paragraph("Life Assured Photo", small_bold));
            userPhotoTable.addCell(cell);

            imageCell = new PdfPCell(getImageForPDF(userPhotoBitmap));
            imageCell.setHorizontalAlignment(Element.ALIGN_TOP);
            imageCell.setPadding(5);
            userPhotoTable.addCell(imageCell);
            document.add(userPhotoTable);

            PdfPTable addressProofDetailsTable = new PdfPTable(2);
            addressProofDetailsTable.setWidths(new float[]{7f, 3f});
            addressProofDetailsTable.setWidthPercentage(100f);
            addressProofDetailsTable.setHorizontalAlignment(Element.ALIGN_LEFT);

            cell = new PdfPCell(new Phrase("Address Proof", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            addressProofDetailsTable.addCell(cell);


            //Column 2
            cell = new PdfPCell(new Phrase(
                    addressProofDOCDetails, small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            addressProofDetailsTable.addCell(cell);
            document.add(addressProofDetailsTable);

            PdfPTable idProofDetailsTable = new PdfPTable(2);
            idProofDetailsTable.setWidths(new float[]{7f, 3f});
            idProofDetailsTable.setWidthPercentage(100f);
            idProofDetailsTable.setHorizontalAlignment(Element.ALIGN_LEFT);

            cell = new PdfPCell(new Phrase("ID Proof", small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            idProofDetailsTable.addCell(cell);


            //Column 2
            cell = new PdfPCell(new Phrase(
                    IDProofDOCDetails, small_normal));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            idProofDetailsTable.addCell(cell);
            document.add(idProofDetailsTable);


            PdfPTable agentPhotoTable = new PdfPTable(1);
            agentPhotoTable.setWidthPercentage(100);

            cell = new PdfPCell(new Paragraph(userType + " Photo", small_bold));
            agentPhotoTable.addCell(cell);

            imageCell = new PdfPCell(getImageForPDF(agentPhotoBitmap));
            imageCell.setHorizontalAlignment(Element.ALIGN_TOP);
            imageCell.setPadding(5);
            agentPhotoTable.addCell(imageCell);
            document.add(agentPhotoTable);

            //if (otherFormFillerRBString.equalsIgnoreCase("yes")) {
            PdfPTable declarOtherFormFillerTable = new PdfPTable(1);
            declarOtherFormFillerTable.setWidthPercentage(100f);
            declarOtherFormFillerTable.setHorizontalAlignment(Element.ALIGN_LEFT);

            cell = new PdfPCell((new Phrase("DECLARATION WHEN THE DGH IS FILLED BY A PERSON OTHER THAN THE PROPOSER /POLICYHOLDER / MEMBER INSURED IN VERNACULAR\n" +
                    "LANGUAGE/ PROPOSER /POLICYHOLDER / MEMBER INSURED IS ILLITERATE", headerBold)));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            declarOtherFormFillerTable.addCell(cell);

            cell = new PdfPCell((new Phrase(declarationOne, small_normal)));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            declarOtherFormFillerTable.addCell(cell);

            cell = new PdfPCell((new Phrase(declarationSecond, small_normal)));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            declarOtherFormFillerTable.addCell(cell);
            document.add(declarOtherFormFillerTable);
            documentCustomerPDF.add(declarOtherFormFillerTable);
            //AgentDetails
            PdfPTable agentDetailsTable = new PdfPTable(2);
            agentDetailsTable.setWidths(new float[]{5f, 5f});
            agentDetailsTable.setWidthPercentage(100f);
            agentDetailsTable.setHorizontalAlignment(Element.ALIGN_LEFT);

            cell = new PdfPCell((new Phrase("Authenticated by " + strCIFBDMUserId, small_normal)));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            agentDetailsTable.addCell(cell);

            /*cell = new PdfPCell((new Phrase("e signed by verification code sent on mobile number " +
                    MOBILE + " at " + currentDateTime + ", GPS Coordinates (" + locationCord + ")", small_normal)));

           *//* cell = new PdfPCell((new Phrase("Authenticated via OTP shared for Policy No." + policyNumber +
                    " on " + currentDateTime + ", GPS Coordinates (" + locationCord + ")", small_bold)));*//*
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            agentDetailsTable.addCell(cell);

            cell = new PdfPCell((new Phrase("Signature of Proposer/ Life to be assured\nThis document is authenticated by " + lifeAssuredName, small_bold)));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            agentDetailsTable.addCell(cell);*/

           /* cell = new PdfPCell((new Phrase("e signed by verification code sent on mobile number " +
                    MOBILE + " at " + currentDateTime + ", GPS Coordinates (" + locationCord + ")", small_bold)));*/

            cell = new PdfPCell((new Phrase("(" + userType + " Code - " + strCIFBDMUserId +
                    ")\nName of " + userType + " - " + userName +
                    "\nAuthenticated by Id & Password"
                    , small_bold)));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);

            agentDetailsTable.addCell(cell);

            cell = new PdfPCell((new Phrase("Place : " + place, small_normal)));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            agentDetailsTable.addCell(cell);

            cell = new PdfPCell((new Phrase("Date: " + CurrentDate, small_normal)));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            agentDetailsTable.addCell(cell);

            cell = new PdfPCell((new Phrase("Name : " + userName, small_normal)));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            agentDetailsTable.addCell(cell);

            cell = new PdfPCell((new Phrase("User Code: " + strCIFBDMUserId, small_normal)));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            agentDetailsTable.addCell(cell);
            document.add(agentDetailsTable);
            documentCustomerPDF.add(agentDetailsTable);
            documentCustomerPDF.close();
            //}


            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {


        int selectedId = radioGroup.getCheckedRadioButtonId();
        RadioButton radioButton = findViewById(selectedId);
        String text = radioButton.getText() + "";
        switch (radioGroup.getId()) {
            case R.id.radioGroupResidentStatus:
                residentRBString = text;

                break;
            case R.id.radioGroupHealthStatus:
                healthRBString = text;

                if (healthRBString.equalsIgnoreCase("Yes")) {
                    llHealthStatusQ1.setVisibility(View.GONE);
                } else {
                    llHealthStatusQ1.setVisibility(View.VISIBLE);
                }

                break;
            case R.id.rgDateOfProposalDetailsOptionA:
                DateOfProposalDetailsOptionARBString = text;
                if (DateOfProposalDetailsOptionARBString.equalsIgnoreCase("Yes")) {
                    llDateOfProposalDetailsOptionAYes.setVisibility(View.VISIBLE);
                } else {
                    llDateOfProposalDetailsOptionAYes.setVisibility(View.GONE);
                }
                break;
            case R.id.rgDateOfProposalDetailsOptionB:
                dateOfProposalOptionBRBString = text;

                if (dateOfProposalOptionBRBString.equalsIgnoreCase("Yes")) {
                    llDateOfProposalDetailsOptionBYes.setVisibility(View.VISIBLE);
                } else {
                    llDateOfProposalDetailsOptionBYes.setVisibility(View.GONE);
                }
                break;
            case R.id.rgDateOfProposalDetailsOptionC:
                dateOfProposalOptionCRBString = text;
                if (dateOfProposalOptionCRBString.equalsIgnoreCase("Yes")) {
                    llDateOfProposalDetailsOptionCYes.setVisibility(View.VISIBLE);
                } else {
                    llDateOfProposalDetailsOptionCYes.setVisibility(View.GONE);
                }
                break;
            case R.id.radioGroupQuestion5New:
                question5NewRBString = text;
                if (question5NewRBString.equalsIgnoreCase("Yes")) {
                    llQuestion5New.setVisibility(View.VISIBLE);
                } else {
                    llQuestion5New.setVisibility(View.GONE);
                }
                break;
            case R.id.radioGroupQuestion6New:
                question6NewRBString = text;

                if (question6NewRBString.equalsIgnoreCase("Yes")) {
                    llQuestion6New.setVisibility(View.VISIBLE);
                } else {
                    llQuestion6New.setVisibility(View.GONE);
                }
                break;
            case R.id.radioGroupQuestion7New:
                question7NewRBString = text;
                if (question7NewRBString.equalsIgnoreCase("Yes")) {
                    llQuestion7New.setVisibility(View.VISIBLE);
                } else {
                    llQuestion7New.setVisibility(View.GONE);
                }
                break;
            case R.id.radioGroupQuestion8NewA:
                question8NewARBString = text;
                if (question8NewARBString.equalsIgnoreCase("Yes")) {
                    llQuestion8NewA.setVisibility(View.VISIBLE);
                } else {
                    llQuestion8NewA.setVisibility(View.GONE);
                }
                break;
            case R.id.radioGroupQuestion8NewB:
                question8NewBRBString = text;
                if (question8NewBRBString.equalsIgnoreCase("Yes")) {
                    llQuestion8NewB.setVisibility(View.VISIBLE);
                } else {
                    llQuestion8NewB.setVisibility(View.GONE);
                }
                break;
            case R.id.radioGroupQuestion8NewC:
                question8NewCRBString = text;
                if (question8NewCRBString.equalsIgnoreCase("Yes")) {
                    llQuestion8NewC.setVisibility(View.VISIBLE);
                } else {
                    llQuestion8NewC.setVisibility(View.GONE);
                }
                break;

            case R.id.radioGroupQuestion9New:
                question9NewRBString = text;
                if (question9NewRBString.equalsIgnoreCase("Yes")) {
                    llQuestion9New.setVisibility(View.VISIBLE);
                } else {
                    llQuestion9New.setVisibility(View.GONE);
                }
                break;
            case R.id.radioGroupQuestion10A1:
                question10A1RBString = text;
                if (question10A1RBString.equalsIgnoreCase("Yes")) {
                    ll10A1.setVisibility(View.VISIBLE);
                } else {
                    ll10A1.setVisibility(View.GONE);
                }
                break;
            case R.id.radioGroupQuestion10A2:
                question10A2RBString = text;
                if (question10A2RBString.equalsIgnoreCase("Yes")) {
                    ll10A2.setVisibility(View.VISIBLE);
                } else {
                    ll10A2.setVisibility(View.GONE);
                }
                break;
            case R.id.radioGroupQuestion10A3:
                question10A3RBString = text;
                if (question10A3RBString.equalsIgnoreCase("Yes")) {
                    ll10A3.setVisibility(View.VISIBLE);
                } else {
                    ll10A3.setVisibility(View.GONE);
                }
                break;
            case R.id.radioGroupQuestion11A:
                question11ARBString = text;
                if (question11ARBString.equalsIgnoreCase("Yes")) {
                    etPregnancyMonth11A.setVisibility(View.VISIBLE);
                } else {
                    etPregnancyMonth11A.setVisibility(View.GONE);
                }
                break;
            case R.id.radioGroupQuestion11B:
                question11BRBString = text;
                if (question11BRBString.equalsIgnoreCase("Yes")) {
                    etGynecologicalProblem11B.setVisibility(View.VISIBLE);
                } else {
                    etGynecologicalProblem11B.setVisibility(View.GONE);
                }
                break;

            case R.id.radioGroupWeightLoss:
                weightLossRBString = text;

                if (weightLossRBString.equalsIgnoreCase("Yes")) {
                    llWeightLoss.setVisibility(View.VISIBLE);
                } else {
                    llWeightLoss.setVisibility(View.GONE);
                }

                break;

            case R.id.rgMultipleDiseaseDetails:
                multipleDiseaseRBString = text;

                if (multipleDiseaseRBString.equalsIgnoreCase("Yes")) {
                    llMultipleDiseaseDetails.setVisibility(View.VISIBLE);
                } else {
                    llMultipleDiseaseDetails.setVisibility(View.GONE);
                }

                break;

            case R.id.radioGroupPastForeignVisit:
                pastForeignVisit = text;

                if (pastForeignVisit.equalsIgnoreCase("Yes")) {
                    etPastCountryName.setEnabled(true);
                    tvPastFromDuration.setEnabled(true);
                    tvPastToDuration.setEnabled(true);
                    tvPastReturnDate.setEnabled(true);
                } else {
                    etPastCountryName.setEnabled(false);
                    tvPastFromDuration.setEnabled(false);
                    tvPastToDuration.setEnabled(false);
                    tvPastReturnDate.setEnabled(false);
                }
                break;

            case R.id.radioGroupScreenedAtAirport:
                screeneAtAirport = text;

                break;

            case R.id.radioGroupTestedForCovid19:
                testedForCovid = text;

                break;

            case R.id.radioGroupHomeQuarantine:
                homeQuarantine = text;

                break;

            case R.id.radioGroupUnderObservation:
                underObservation = text;

                break;

            case R.id.radioGroupSelfIsolation:
                selfIsolation = text;

                break;

            case R.id.radioGroupFutureCountryVisit:
                futureForeignVisit = text;

                if (futureForeignVisit.equalsIgnoreCase("Yes")) {
                    etFutureCountryname.setEnabled(true);
                    tvFutureFromDuration.setEnabled(true);
                    tvFutureToDuration.setEnabled(true);
                    tvFutureReturnDate.setEnabled(true);
                } else {
                    etFutureCountryname.setEnabled(false);
                    tvFutureFromDuration.setEnabled(false);
                    tvFutureToDuration.setEnabled(false);
                    tvFutureReturnDate.setEnabled(false);
                }
                break;

            case R.id.radioGroupSymptoms:
                familyMemberSymptoms = text;

                break;

            case R.id.radioGroupRespiratorySymptoms:
                respiratorySymptoms = text;

                break;

            case R.id.radioGroupContactWithCorona:
                contactWithCorona = text;

                break;
        }

        fetchQuestionData();

    }

    void fetchQuestionData() {
        globalDataList = new ArrayList<>();

        if (healthRBString.equalsIgnoreCase("Yes")) {

        } else {
            globalDataList.add(new QuestionsValuesModel("1",
                    etHealthStatus.getText().toString()));
        }

        if (weightLossRBString.equalsIgnoreCase("Yes")) {
            globalDataList.add(new QuestionsValuesModel("2",
                    etWeightLoss.getText().toString()));
        } else {
        }

        if (DateOfProposalDetailsOptionARBString.equalsIgnoreCase("Yes")) {
            globalDataList.add(new QuestionsValuesModel("3 (a)",
                    etDateOfProposalDetailsOptionAYes.getText().toString()));
        } else {

        }

        if (dateOfProposalOptionBRBString.equalsIgnoreCase("Yes")) {
            globalDataList.add(new QuestionsValuesModel("3 (b)",
                    etDateOfProposalDetailsOptionBYes.getText().toString()));
        } else {

        }

        if (dateOfProposalOptionCRBString.equalsIgnoreCase("Yes")) {
            globalDataList.add(new QuestionsValuesModel("3 (c)",
                    etDateOfProposalDetailsOptionCYes.getText().toString()));
        } else {

        }

        if (multipleDiseaseRBString.equalsIgnoreCase("Yes")) {
            globalDataList.add(new QuestionsValuesModel("4",
                    etMultipleDiseaseDetails.getText().toString()));
        } else {
        }

        if (question5NewRBString.equalsIgnoreCase("Yes")) {
            globalDataList.add(new QuestionsValuesModel("5",
                    etQuestion5New.getText().toString()));
        } else {

        }

        if (question6NewRBString.equalsIgnoreCase("Yes")) {
            globalDataList.add(new QuestionsValuesModel("6",
                    etQuestion6New.getText().toString()));
        } else {
        }

        if (question7NewRBString.equalsIgnoreCase("Yes")) {
            globalDataList.add(new QuestionsValuesModel("7",
                    etQuestion7New.getText().toString()));
        } else {
        }

        if (question8NewARBString.equalsIgnoreCase("Yes")) {
            globalDataList.add(new QuestionsValuesModel("8(a)",
                    etQuestion8NewA.getText().toString()));
        } else {

        }

        if (question8NewBRBString.equalsIgnoreCase("Yes")) {
            globalDataList.add(new QuestionsValuesModel("8(b)",
                    etQuestion8NewB.getText().toString()));
        } else {

        }

        if (question8NewCRBString.equalsIgnoreCase("Yes")) {
            globalDataList.add(new QuestionsValuesModel("8(c)",
                    etQuestion8NewC.getText().toString()));
        } else {

        }

        if (question9NewRBString.equalsIgnoreCase("Yes")) {
            globalDataList.add(new QuestionsValuesModel("9",
                    etQuestion9New.getText().toString()));
        } else {
        }

        if (question10A1RBString.equalsIgnoreCase("Yes")) {
            globalDataList.add(new QuestionsValuesModel("10(a)Tobacco",
                    "Sticks/packets Per day : " + etSticksPackets10A1.getText().toString()
                            + " No. of years used : " + etNoOfYearsUsed10A1.getText().toString()));
        } else {
        }

        if (question10A2RBString.equalsIgnoreCase("Yes")) {
            globalDataList.add(new QuestionsValuesModel("10(a)Alcohol",
                    "Kind of alcohol i.e whisky, beer, etc : " + etQuantity10A2AlcoholType.getText().toString()
                            + " Quantity per week ml : " + etQuantity10A2Ml.getText().toString()
                            + " No. of years used : " + etNoOfYearsUsed10A2.getText().toString()));
        } else {

        }

        if (question10A3RBString.equalsIgnoreCase("Yes")) {
            globalDataList.add(new QuestionsValuesModel("10(a)Any Narcotics",
                    "Type Of Drug : " + etTypeOfDrug10A3.getText().toString()
                            + " No. of years used : " + etNoOfYearsUsed10A3.getText().toString()));
        } else {

        }


        if (question11ARBString.equalsIgnoreCase("Yes")) {
            globalDataList.add(new QuestionsValuesModel("11(a)",
                    "Pregnancy Month : " + etPregnancyMonth11A.getText().toString()));
        } else {
        }

        if (question11BRBString.equalsIgnoreCase("Yes")) {

            globalDataList.add(new QuestionsValuesModel("11(b)",
                    "Gynecological Problems Details : " + etGynecologicalProblem11B.getText().toString()));

        } else {
        }

        selectedAdapter = new SelectedAdapter(globalDataList);
        recyclerview.setAdapter(selectedAdapter);
        selectedAdapter.notifyDataSetChanged();
    }


    private void capture_all_docs(String flag) {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (flag.equalsIgnoreCase("1")) {
            userPhotoFileName = mStorageUtils.createFileToAppSpecificDir(context, "custPhoto.jpg");

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, commonMethods.getContentUri(context,
                        userPhotoFileName));
            } else {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(userPhotoFileName));
            }
        } else if (flag.equalsIgnoreCase("2")) {
            agentPhotoFileName = mStorageUtils.createFileToAppSpecificDir(context, "agentPhoto.jpg");

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

    private Image getImageForPDF(Bitmap mainBitmap) {
        Image finalImagePdf = null;
        try {
            mainBitmap = mainBitmap != null ? mainBitmap.copy(Bitmap.Config.RGB_565, true) : null;
            String bitmapPhotoStr = "";
            if (mainBitmap != null) {

                Bitmap scaled = Bitmap.createScaledBitmap(mainBitmap, 230, 200, true);
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                scaled.compress(Bitmap.CompressFormat.PNG, 100, out);
                byte[] signByteArray = out.toByteArray();
                bitmapPhotoStr = Base64.encodeToString(signByteArray, Base64.DEFAULT);
            }

            byte[] photoBytes = Base64.decode(bitmapPhotoStr, 0);
            Bitmap processedBitmap = BitmapFactory.decodeByteArray(photoBytes, 0,
                    photoBytes.length);

            ByteArrayOutputStream photoOutputStream = new ByteArrayOutputStream();

            (processedBitmap).compress(Bitmap.CompressFormat.PNG, 100, photoOutputStream);

            finalImagePdf = Image.getInstance(photoOutputStream.toByteArray());
            finalImagePdf.scaleToFit(90, 90);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return finalImagePdf;
    }


    class DownloadPolicyDetailsAsyncTask extends AsyncTask<String, String, String> {

        private volatile boolean running = true;
        private String error = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog.show();

            if (mProgressDialog != null && !mProgressDialog.isShowing()) {
                mProgressDialog.show();
            }


        }

        @SuppressLint("SimpleDateFormat")
        @Override
        protected String doInBackground(String... aurl) {
            try {

                running = true;
                SoapObject request;


                //getPolicyDetail_smrt(string strCode, string strPolicyNumber,
                // string strEmailId, string strMobileNo, string strAuthKey)
                String METHOD_NAME = "getPolicyDetail_smrt";
                String NAMESPACE = "http://tempuri.org/";
                request = new SoapObject(NAMESPACE, METHOD_NAME);
                request.addProperty("strCode", strCIFBDMUserId);
                request.addProperty("strPolicyNumber", policyNumber);
                commonMethods.appendSecurityParams(context, request, strCIFBDMEmailId, strCIFBDMMObileNo);
                commonMethods.TLSv12Enable();

                System.out.println("request:" + request.toString());
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);

                commonMethods.TLSv12Enable();

                String URl = ServiceURL.SERVICE_URL;
                HttpTransportSE androidHttpTranport = new HttpTransportSE(URl);
                String SOAP_ACTION = "http://tempuri.org/" + METHOD_NAME;
                androidHttpTranport.call(SOAP_ACTION, envelope);
                Object response = envelope.getResponse();

                if (!response.toString().contentEquals("<NewDataSet />")) {

                      /*<CIFPolicyList> <Table> <NAME>Vinodbhai V Pargi</NAME> <MOBILE>9687203172</MOBILE>
                <EMAILID>tinapargi215@gmail.com</EMAILID> <AGE>33</AGE> <GENDER>Male</GENDER> </Table> </CIFPolicyList>*/
                    ParseXML prsObj = new ParseXML();
                    String inputpolicylist = response.toString();
                    inputpolicylist = prsObj.parseXmlTag(inputpolicylist, "NewDataSet");
                    error = prsObj.parseXmlTag(
                            inputpolicylist, "ScreenData");

                         /*<NewDataSet >
                          <Table >
                            <NAME > Sirisha Vipparthi</NAME >
                            <MOBILE > 9949584358 </MOBILE >
                            <EMAILID > ramjikumar.kusuma @gmail.com</EMAILID >
                            <AGE > 40 </AGE >
                            <GENDER > Female </GENDER >
                          </Table >
                        </NewDataSet >*/
                    if (error == null) {
                        // for agent policy list
                        List<String> Node = prsObj.parseParentNode(inputpolicylist, "Table");

                        NAME = prsObj.parseXmlTag(inputpolicylist, "NAME");
                        NAME = NAME == null ? "" : NAME;

                        MOBILE = prsObj.parseXmlTag(inputpolicylist, "MOBILE");
                        MOBILE = MOBILE == null ? "" : MOBILE;

                        EMAILID = prsObj.parseXmlTag(inputpolicylist, "EMAILID");
                        EMAILID = EMAILID == null ? "" : EMAILID;

                        AGE = prsObj.parseXmlTag(inputpolicylist, "AGE");
                        AGE = AGE == null ? "" : AGE;

                        GENDER = prsObj.parseXmlTag(inputpolicylist, "GENDER");
                        GENDER = GENDER == null ? "" : GENDER;

                        error = "success";
                    } else {
                        error = "1";
                    }

                } else {
                    error = "1";
                }

            } catch (Exception e) {
                mProgressDialog.dismiss();
                running = false;
            }
            return null;

        }


        @Override
        protected void onPostExecute(String unused) {
            if (mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
            if (running) {
                if (error.equalsIgnoreCase("success")) {
                    ll_dgh.setVisibility(View.VISIBLE);
                    llOTP.setVisibility(View.VISIBLE);
                    if (!TextUtils.isEmpty(NAME)) {
                        etLifeAssuredName.setText(NAME);
                    }

                    if (!TextUtils.isEmpty(MOBILE)) {
                        etMobileNumber.setText(MOBILE);
                    }

                    if (!TextUtils.isEmpty(EMAILID)) {
                        etEmailId.setText(EMAILID);
                    }

                    if (!TextUtils.isEmpty(AGE)) {
                        tvAge.setText(AGE);
                    }

                    if (!TextUtils.isEmpty(GENDER)) {
                        llFemaleLivesOnly.setVisibility(View.GONE);
                        if (GENDER.equalsIgnoreCase("male")) {
                            spinnerGender.setSelection(0);
                        } else if (GENDER.equalsIgnoreCase("female")) {
                            spinnerGender.setSelection(1);
                            llFemaleLivesOnly.setVisibility(View.VISIBLE);
                        } else {
                            spinnerGender.setSelection(2);
                        }
                    }
                } else {
                    commonMethods.showMessageDialog(context, "No record found");
                }
            }
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

        EditText editText = null;
        if (s.hashCode() == etHealthStatus.getText().hashCode() && healthRBString.equalsIgnoreCase("no")) {
            editText = etHealthStatus;
        } else if (s.hashCode() == etWeightLoss.getText().hashCode() && weightLossRBString.equalsIgnoreCase("Yes")) {
            editText = etWeightLoss;
        } else if (s.hashCode() == etDateOfProposalDetailsOptionAYes.getText().hashCode() && DateOfProposalDetailsOptionARBString.equalsIgnoreCase("Yes")) {
            editText = etDateOfProposalDetailsOptionAYes;
        } else if (s.hashCode() == etDateOfProposalDetailsOptionBYes.getText().hashCode() && dateOfProposalOptionBRBString.equalsIgnoreCase("Yes")) {
            editText = etDateOfProposalDetailsOptionBYes;
        } else if (s.hashCode() == etDateOfProposalDetailsOptionCYes.getText().hashCode() && dateOfProposalOptionCRBString.equalsIgnoreCase("Yes")) {
            editText = etDateOfProposalDetailsOptionCYes;
        } else if (s.hashCode() == etMultipleDiseaseDetails.getText().hashCode() && multipleDiseaseRBString.equalsIgnoreCase("Yes")) {
            editText = etMultipleDiseaseDetails;
        } else if (s.hashCode() == etQuestion5New.getText().hashCode() && question5NewRBString.equalsIgnoreCase("Yes")) {
            editText = etQuestion5New;
        } else if (s.hashCode() == etQuestion6New.getText().hashCode() && question6NewRBString.equalsIgnoreCase("Yes")) {
            editText = etQuestion6New;
        } else if (s.hashCode() == etQuestion7New.getText().hashCode() && question7NewRBString.equalsIgnoreCase("Yes")) {
            editText = etQuestion7New;
        } else if (s.hashCode() == etQuestion8NewA.getText().hashCode() && question8NewARBString.equalsIgnoreCase("Yes")) {
            editText = etQuestion8NewA;
        } else if (s.hashCode() == etQuestion8NewB.getText().hashCode() && question8NewBRBString.equalsIgnoreCase("Yes")) {
            editText = etQuestion8NewB;
        } else if (s.hashCode() == etQuestion8NewC.getText().hashCode() && question8NewCRBString.equalsIgnoreCase("Yes")) {
            editText = etQuestion8NewC;
        } else if (s.hashCode() == etQuestion9New.getText().hashCode() && question9NewRBString.equalsIgnoreCase("Yes")) {
            editText = etQuestion9New;
        } else if (s.hashCode() == etConsumptionQuit10B.getText().hashCode()) {
            commonMethods.edittextLengthValidation(etConsumptionQuit10B, "Min Length 5", 5, 0);
        } else if (s.hashCode() == etPregnancyMonth11A.getText().hashCode() && question11ARBString.equalsIgnoreCase("Yes")) {
            editText = etPregnancyMonth11A;
        } else if (s.hashCode() == etGynecologicalProblem11B.getText().hashCode() && question11BRBString.equalsIgnoreCase("Yes")) {
            editText = etGynecologicalProblem11B;
        } else if (s.hashCode() == etHeights.getText().hashCode()) {
            String weight = s.toString();
            if (!TextUtils.isEmpty(weight)) {
                if (Double.parseDouble(weight) < 132) {
                    etHeights.setError("Weight >= 132");
                    etHeights.requestFocus();
                }
            }

        }
        if (s.toString().length() > 500 && editText != null) {
            commonMethods.edittextLengthValidation(editText, "Max Length 500", 0, 500);
        } else if (s.toString().length() < 30 && editText != null) {
            commonMethods.edittextLengthValidation(editText, "Min Length 30", 30, 0);
        }

    }

    @Override
    protected void onDestroy() {
        killTasks();
        super.onDestroy();
    }


    private void killTasks() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }

        if (saveDGHDataAsyncTask != null) {
            saveDGHDataAsyncTask.cancel(true);
        }

        if (generateOTPGeneralAsyncTask != null) {
            generateOTPGeneralAsyncTask.cancel(true);
        }
        if (validateOTPGeneralAsyncTask != null) {
            validateOTPGeneralAsyncTask.cancel(true);
        }
        if (authenticatePDFAsync != null) {
            authenticatePDFAsync.cancel(true);
        }

        if (uploadPDFService != null) {
            uploadPDFService.cancel(true);
        }

    }

    private void sendMail(String email, String subject, String messageBody,
                          File FileName, File filelocation_Graphical) {
        Session session = new CommonMethods().createSessionObject();

        try {
            Message message = createMessage(email, subject, messageBody,
                    session, FileName, filelocation_Graphical);
            new SendMailTask().execute(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Message createMessage(String email, String subject,
                                  String messageBody, Session session, File FileName, File filelocation_Graphical)
            throws MessagingException {
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress("sbilconnectlife@sbi-life.com"));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(
                email));
        message.setSubject(subject);
        message.setText(messageBody);
        Multipart mp = new MimeMultipart();
        if (FileName != null) {
            MimeBodyPart mbp1 = new MimeBodyPart();
            mbp1.setText(messageBody);
            MimeBodyPart mbp2 = new MimeBodyPart();
            FileDataSource fds = new FileDataSource(FileName);
            mbp2.setDataHandler(new DataHandler(fds));
            mbp2.setFileName(fds.getName());
            mp.addBodyPart(mbp1);
            mp.addBodyPart(mbp2);
        }
       /* if (filelocation_Graphical != null) {

            MimeBodyPart mbp3 = new MimeBodyPart();
            FileDataSource fds1 = new FileDataSource(filelocation_Graphical);
            mbp3.setDataHandler(new DataHandler(fds1));
            mbp3.setFileName(fds1.getName());
            mp.addBodyPart(mbp3);
        }*/
        message.setContent(mp);
        return message;
    }


    private class SendMailTask extends AsyncTask<Message, Void, Void> {
        ProgressDialog mProgressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(context,
                    ProgressDialog.THEME_HOLO_LIGHT);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog
                    .setTitle(Html
                            .fromHtml("<font color='#00a1e3'><b>Please Wait...<b></font>"));
            mProgressDialog.setMax(100);
            mProgressDialog.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if (mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
            if (new CommonMethods().isNetworkConnected(context)) {
                Toast.makeText(
                        context,
                        "DGH Customer Copy sent to Email.",
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(
                        context,
                        new CommonMethods().NO_INTERNET_MESSAGE,
                        Toast.LENGTH_SHORT).show();

            }

        }

        @Override
        protected Void doInBackground(Message... messages) {
            try {
                Transport.send(messages[0]);
            } catch (MessagingException e) {
                e.printStackTrace();
            }
            return null;
        }
    }


}

//<policyNumber>1K458164608</policyNumber>
//<lifeAssuredName>Vinodbhai V Pargi</lifeAssuredName>
//<DOB>27-May-2019</DOB>
//<age>-1</age>
//<gender>Male</gender>
//<countryCode>91</countryCode>
//<mobileNumber>9687203172</mobileNumber>
//<emailId>tinapargi215@gmail.com</emailId>
//
//<addressProofDetails>Banker's certificate -(Ann III)</addressProofDetails>
//<idProofDetails>Photo ID Card issued by PSUs,Schld Com Banks&Public Financial Inst</idProofDetails>
//<otherProofDetails></otherProofDetails>





