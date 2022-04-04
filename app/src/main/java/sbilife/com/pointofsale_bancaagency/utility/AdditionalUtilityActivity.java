package sbilife.com.pointofsale_bancaagency.utility;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.text.Html;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.model.LatLng;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.xbizventures.ocrlib.OcrActivity;

import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import sbilife.com.pointofsale_bancaagency.DatabaseHelper;
import sbilife.com.pointofsale_bancaagency.ParseXML;
import sbilife.com.pointofsale_bancaagency.ParseXML.XMLDOCREQList;
import sbilife.com.pointofsale_bancaagency.ParseXML.XMLProposerTrackerList;
import sbilife.com.pointofsale_bancaagency.R;
import sbilife.com.pointofsale_bancaagency.ServiceURL;
import sbilife.com.pointofsale_bancaagency.Utility;
import sbilife.com.pointofsale_bancaagency.branchlocator.GPSTracker;
import sbilife.com.pointofsale_bancaagency.common.AsyncUploadFile_Common;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;
import sbilife.com.pointofsale_bancaagency.common.CompressImage;
import sbilife.com.pointofsale_bancaagency.common.SimpleCrypto;
import sbilife.com.pointofsale_bancaagency.common.StorageUtils;

@SuppressWarnings("unused")
public class AdditionalUtilityActivity extends AppCompatActivity implements AsyncUploadFile_Common.Interface_Upload_File_Common {

    private String inputpolicylist;
    private List<XMLDOCREQList> lstPolicyList, nonMedicallstPolicyList;
    // String[] medicalReqList;
    private ArrayList<String> medicalReqList, nonMedicalReqList;
    private int flag_cancel_age = 0, flag_cancel_address = 0,
            flag_cancel_income = 0, flag_cancel_identity = 0,
            flag_cancel_others = 0, flag_cancel_bank = 0, flag_cancel_eft = 0;
    private String req1, req2, req3, req4, req5, req6, req7;// ,OtherDocumentName
    // = "";

    private ExpandableListView exp_requirement_upload;// exp_Mandatory_upload,
    private LinearLayout ll_requirement_upload, ll_medical_req_header,
            ll_non_medical_requirements_not_taken_up;
    private Spinner spnr_Questionnaires;// spnr_mandatory_quotation_no,
    // spnr_pay_shortage_quation_no,,
    // spnr_resync_document_quation_no;

    // private List<String> ls_QuatationNo = new ArrayList<String>();
    // private boolean otherProofFlag = false;
    // private String mandatory_QuotationNumber,
    // requirment_QuotationNumber,payshortage_QuotationNumber;
    private EditText edt_requirement_Proposer_no;// ,
    // edt_pay_shortage_proposer_number,edt_pay_shortage_amount,
    // edt_resync_document_proposer_number;
    private ListView listView, lv_nonmedical_not_taken_up_list;
    private DatabaseHelper db;
    static String ProposalNumber = "";
    // private int flag;
    /* From Document Upload */
    private Button btn_focus;// btn_proceed,
    private String NAMESPACE = "http://tempuri.org/";
    // uplaod doc
    private final String METHOD_NAME_UPLOAD_FILE = "UploadFile_SMRT";

    private final String SOAP_ACTION_VALID_PROPSOAL = "http://tempuri.org/checkPropNum";
    private final String METHOD_NAME_VALID_PROPSOAL = "checkPropNum";

    public final int REQUEST_OCR = 1;

    // Changes done by amit 21-3-2016
    private final String SOAP_ACTION_UPLOAD_FILE_GET_REQUIREMENT = "http://tempuri.org/getReqirementDtls";
    private final String METHOD_NAME_UPLOAD_FILE_GET_REQUIREMENT = "getReqirementDtls";

    private AsynccheakProposal ProposalCheakService;

    private String strPremium = "", str_extension = "";
    int increment;
    private Context context;
    public static String ProposerNumber;
    private int deleteAgeDocument;
    private int deleteAddressDocument;
    private int deleteIdentityDocument;
    private int deleteIncomeDocument;
    private int deleteOtherDocument;
    private int deleteBankDocument;
    private int deleteEFTDocument;
    private final int DIALOG_ALERT = 10;
    // private final String LOGTAG = "DocumetUploadActivity";
    private static final String JPEG_FILE_PREFIX = "IMG_";
    private static final String JPEG_FILE_SUFFIX = ".jpg";
    //public static final int SIGNATURE_ACTIVITY = 1;
    public static final int CAPTURE_ACTIVITY = 2;
    // private static String strPathOfFile;
    private String mCurrentPhotoPath;
    private final int REQUEST_CODE_CAPTURE_FILE = 3;
    private final int REQUEST_CODE_BROWSE_FILE = 2;
    final AppCompatActivity activityForButton = this;
    public static String Check;
    private Dialog d;
    byte[] bytes = null;
    // AlertDialog.Builder showAlert;
    // private DecimalFormat currencyFormat;
    private ProgressDialog progressDialog;
    private static File f;
    AlertDialogMessage obj;
    File imageF;
    String AgeDocument = "Birth Certificate";
    String IdentityDocument = "Aadhar Card";
    String AddressDocument = "Passport";
    String IncomeDocument = "Salary Slip";
    String strUploadDocument = "";
    // Document Upload detail

    String createdBy = "";
    String createdDate = "";
    String modifiedBy = "";
    String modifiedDate = "";

    Boolean isSync;
    Boolean isFlag1;
    Boolean isFlag2;
    Boolean isFlag3;
    Boolean isFlag4;

    /* variable used for Saving Data to Database */
    boolean ageProofFlag = false;
    boolean identityProofFlag = false;
    boolean addressProofFlag = false;
    boolean incomeProofFlag = false;

    Bitmap ageProofBitmap;
    Bitmap identityProofBitmap;
    Bitmap addressProofBitmap;
    Bitmap incomeProofBitmap;
    Bitmap otherProofBitmap;
    Bitmap bankProofBitmap;
    Bitmap eftProofBitmap;
    public static String MobileNo;
    public static String EmailId;
    String formattedDate;
    String ExpiredDate;
    String PremiumAmount = "";
    /* Reduced Size Bitmap */
    public static Bitmap ReducedAgeBitmap;
    public static Bitmap ReducedIdentityBitmap;
    public static Bitmap ReducedAddressBitmap;
    public static Bitmap ReducedIncomeBitmap;
    public static Bitmap ReducedPhotoBitmap;
    public static Bitmap ReducedOtherBitmap;
    public static Bitmap ReducedBankBitmap;
    public static Bitmap ReducedEFTBitmap;

    static byte[] byteArray = null;
    static Bitmap ageimagebitmap;
    static Bitmap Identityimagebitmap;
    static Bitmap Addressimagebitmap;
    static Bitmap Incomeimagebitmap;
    static Bitmap Otherimagebitmap;

    private File AgeProofFileName, BankProofFileName, EFTProofFileName, IdentityProofFileName, AddressProofFileName,
            IncomeProofFileName, OtherProofFileName, Photo;

    static ImageButton getSignature_Proposer;
    ImageButton getphoto;
    // Spinners
    public static Spinner spnr_document_upload_document_age;
    public static Spinner spnr_document_upload_document_identity;
    public static Spinner spnr_document_upload_document_address;
    public static Spinner spnr_document_upload_document_income;

    // Image Button for Camera
    ImageButton img_btn_document_upload_click_image_age,
            img_btn_document_upload_click_image_identity,
            img_btn_document_upload_click_image_address,
            img_btn_document_upload_click_image_income,
            img_btn_document_upload_click_image_others;

    // Image button for Browse

    ImageButton img_btn_document_upload_click_browse_age,
            img_btn_document_upload_click_browse_identity,
            img_btn_document_upload_click_browse_address,
            img_btn_document_upload_click_browse_income,
            img_btn_document_upload_click_browse_Others;

    // Image button for Preview Button

    ImageButton img_btn_document_upload_preview_image_age,
            img_btn_document_upload_preview_image_identity,
            img_btn_document_upload_click_preview_image_address,
            img_btn_document_upload_click_preview_image_income,
            img_btn_document_upload_click_preview_image_others;

    /* Image Button to delete uploaded Image */

    ImageButton img_btn_document_delete_age, img_btn_document_delete_identity,
            img_btn_document_delete_address, img_btn_document_delete_income,
            img_btn_document_delete_Others;
    EditText edt_document_upload_document_others;

    TableRow tr_income_proof;
    LinearLayout ll_documentStatus;
    File mypath;
    String path;
    List<String> lstQuatationNumber = new ArrayList<String>();
    String proposer_IsUnder = "";
    int uploadFlag;

    private String QuotationNumber = "";
    // private String currentRecordId = "";
    private String planName = "";
    // private String productCode = "";
    // private String agentId = "";
    private String proposerSign = "";

    private String agentcode, agentMobile, agentEmail, agentType, agentPassword;
    public static List<String> lst_Age = new ArrayList<String>();
    public static List<String> lst_Identity = new ArrayList<String>();
    public static List<String> lst_Address = new ArrayList<String>();
    public static List<String> lst_Income = new ArrayList<String>();
    public static List<String> lst_Others = new ArrayList<String>();
    public static List<String> lst_Bank = new ArrayList<String>();
    public static List<String> lst_EFT = new ArrayList<String>();
    int addflag = 1;
    String AgentCode = "";

    String encr_Password = "";
    String decr_Password = "";
    String strWithoutIAId = "";
    /* Changes done for banker proof */
    Spinner spnr_document_upload_document_bank;
    ImageButton img_btn_document_upload_click_preview_image_bank,
            img_btn_document_delete_bank,
            img_btn_document_upload_click_image_bank,
            img_btn_document_upload_click_browse_bank,
            img_btn_document_upload_bank_upload;
    TextView tv_proposalName_document_upload, tv_proposalName_resync_document;
    LinearLayout ll_document_upload;
    String ProposalName = "";
    Utility objUtility;

    /* Changes Done for EFT */
    Spinner spnr_document_upload_document_eft;
    ImageButton img_btn_document_upload_preview_image_eft,
            img_btn_document_delete_eft,
            img_btn_document_upload_click_image_eft,
            img_btn_document_upload_click_browse_eft,
            img_btn_document_upload_eft_upload;

    String productCategory = "";
    String currentRowId = "";
    String encryptPassword = "";
    private ProgressDialog mProgressDialog;
    String str_Valid_Success = "";
    int upload_Flag = -1;
    TableRow tr_document_upload_proof_of_age,
            tr_document_upload_proof_of_identity,
            tr_document_upload_proof_of_address,
            tr_document_upload_proof_of_bank,
            tr_document_upload_proof_of_others,
            tr_document_upload_proof_of_eft, tr_non_medical_header,
            tr_medical_requirement_header;

    TextView txt_document_upload_proof_of_age,
            txt_document_upload_proof_of_identity,
            txt_document_upload_proof_of_address,
            txt_document_upload_proof_of_income,
            txt_document_upload_proof_of_bank,
            txt_document_upload_proof_of_others,
            txt_document_upload_proof_of_eft;

    CheckBox cb_document_upload_age_upload_tick,
            cb_document_upload_identity_upload_tick,
            cb_document_upload_address_upload_tick,
            cb_document_upload_income_upload_tick,
            cb_document_upload_bank_upload_tick,
            cb_document_upload_others_upload_tick,
            cb_document_upload_eft_upload_tick;

    String des1_Age = "";
    String des2_Identity = "";
    String des3_Address = "";
    String des4_Income = "";
    String des5_bank = "";
    String des6_others = "";
    String des7_EFT = "";

    // private EditText edservicepass ;
    // private TableLayout tblcheckpass;
    // private TextView txtpasswordlbl;
    // private ImageButton btncifdobdate;
    private int mYear;
    private int mMonth;
    private int mDay;
    public static final int DATE_DIALOG_ID = 1;
    private int datecheck = 0;

    // Proposal Tracker
    DatabaseHelper dbhelper;
    String username = "";
    LinearLayout lnProposerTracker;
    EditText edProposalTrackerNo;
    Button btn_proposal_ok;
    TableLayout tblProposarTrackerStatus;
    TextView txtProposalTrackerProposalNo, txtProposalTrackerStatus,
            txtProposalTrackerReason, btn_proposal_tracker_separator,
            txtProposalTrackerPolicyHolderName,
            txtProposalTrackerLifeAssuredName;// ,txterrordescproposaltracker;

    private DownloadFileAsyncProposalTracker taskProposalTracker;
    private static final String SOAP_ACTION_PROPOSAL_TRACKER = "http://tempuri.org/getProposalStatus";
    private static final String METHOD_NAME_PROPOSAL_TRACKER = "getProposalStatus";

    String SOAP_ACTION_SH = "http://tempuri.org/saveSmartAdvisorServiceHits";
    String METHOD_NAME_SH = "saveSmartAdvisorServiceHits";
    private static final String URl = ServiceURL.SERVICE_URL;
    private String strProposalTrackerErrorCOde = "";
    // End Proposal Tracker
    public static final int DIALOG_DOWNLOAD_PROGRESS = 0;
    private ServiceHits service;
    private AsyncUploadFile_Common mAsyncUploadFileCommon;
    private AsynccheakProposalRequirement proposalCheakServiceRequirement;
    private CommonMethods mCommonMethods;
    private StorageUtils mStorageUtils;

    private LatLng mLatLng;
    private GPSTracker mGPGpsTracker;

    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.layout_utility);

        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.layout_utility);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);
        objUtility = new Utility();
        context = this;
        mCommonMethods = new CommonMethods();
        mStorageUtils = new StorageUtils();

        dbhelper = new DatabaseHelper(this);
        username = dbhelper.GetUserName();
        mCommonMethods.setApplicationToolbarMenu(this, "Requirement Upload");

        medicalReqList = new ArrayList<String>();
        nonMedicalReqList = new ArrayList<String>();
        obj = new AlertDialogMessage();

        /**
         * <Modified By Tushar Kadam>
         */
        TextView btn_document_upload = findViewById(R.id.btn_document_upload);
        TableRow tr_default = findViewById(R.id.tr_default_proposal_number_tablerow);
        TableRow tr_default_proposal_number_tablerow_submit = findViewById(R.id.tr_default_proposal_number_tablerow_submit);

        btn_document_upload.setVisibility(View.GONE);
        tr_default.setVisibility(View.GONE);
        tr_default_proposal_number_tablerow_submit.setVisibility(View.GONE);

        Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        taskProposalTracker = new DownloadFileAsyncProposalTracker();
        // edservicepass = (EditText) findViewById(R.id.edservicepass);
        InputFilter[] filters = new InputFilter[1];
        filters[0] = new InputFilter() {
            public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int dstart, int dend) {
                if (end > start) {

                    char[] acceptedChars = new char[]{'a', 'b', 'c', 'd',
                            'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n',
                            'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x',
                            'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H',
                            'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R',
                            'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '0', '1',
                            '2', '3', '4', '5', '6', '7', '8', '9', '@', '.',
                            '_', '#', '$', '%', '&', '*', '-', '+', '(', ')',
                            '!', '"', '\'', ':', '/', '?', ',', '~', '`', '|',
                            '\\', '^', '<', '>', '{', '}', '[', ']', '=', '.'};

                    for (int index = start; index < end; index++) {
                        if (!new String(acceptedChars).contains(String
                                .valueOf(source.charAt(index)))) {
                            return "";
                        }
                    }
                }
                return null;
            }

        };
        // edservicepass.setFilters(filters);
        // tblcheckpass = (TableLayout) findViewById(R.id.tblcheckpass);
        // txtpasswordlbl = (TextView) findViewById(R.id.txtpasswordlbl);
        // btncifdobdate = (ImageButton) findViewById(R.id.btncifdobdate);
        // Button btn_check_pass = (Button) findViewById(R.id.btn_check_pass);

        lnProposerTracker = findViewById(R.id.lnProposerTracker);

        edProposalTrackerNo = findViewById(R.id.edProposalTrackerNo);
        btn_proposal_ok = findViewById(R.id.btn_proposal_ok);
        tblProposarTrackerStatus = findViewById(R.id.tblProposarTrackerStatus);

        txtProposalTrackerProposalNo = findViewById(R.id.txtProposalTrackerProposalNo);
        txtProposalTrackerStatus = findViewById(R.id.txtProposalTrackerStatus);
        txtProposalTrackerReason = findViewById(R.id.txtProposalTrackerReason);
        btn_proposal_tracker_separator = findViewById(R.id.btn_proposal_tracker_separator);

        txtProposalTrackerPolicyHolderName = findViewById(R.id.txtProposalTrackerPolicyHolderName);
        txtProposalTrackerLifeAssuredName = findViewById(R.id.txtProposalTrackerLifeAssuredName);

        db = new DatabaseHelper(this);
        btn_proposal_ok.setOnClickListener(new OnClickListener() {

            // @Override
            public void onClick(View arg0) {
                taskProposalTracker = new DownloadFileAsyncProposalTracker();

                if (edProposalTrackerNo.getText().toString()
                        .equalsIgnoreCase("")) {
                    ProposalvalidationAlert();
                } else {
                    if (mCommonMethods.isNetworkConnected(context)) {

                        service_hits(METHOD_NAME_PROPOSAL_TRACKER);
                    } else {
                        intereneterror();
                    }
                }
            }
        });
        /**
         * </Modified By Tushar kadam
         */

        // btn_proceed = (Button) findViewById(R.id.btn_proceed);
        btn_focus = findViewById(R.id.btn_focus);
        InitializeVariable();
        db = new DatabaseHelper(this);


        Intent intent = getIntent();
        String fromHome = intent.getStringExtra("fromHome");
        if (fromHome != null && fromHome.equalsIgnoreCase("N")) {
            agentcode = intent.getStringExtra("strBDMCifCOde");
            agentEmail = intent.getStringExtra("strEmailId");
            agentMobile = intent.getStringExtra("strMobileNo");
            agentType = intent.getStringExtra("strUserType");
            try {
                /*agentPassword = SimpleCrypto.encrypt("SBIL", "sbil");*/
                agentPassword = mCommonMethods.getStrAuth();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            agentcode = mCommonMethods.GetUserCode(context);
            agentType = mCommonMethods.GetUserType(context);
            agentEmail = mCommonMethods.GetUserEmail(context);
            agentMobile = mCommonMethods.GetUserMobile(context);
            agentPassword = mCommonMethods.GetUserPassword(context);
        }

        lnProposerTracker.getLayoutParams().height = LayoutParams.WRAP_CONTENT;
        lnProposerTracker.requestLayout();
        btn_proposal_tracker_separator.setVisibility(View.VISIBLE);
        if (agentType.contentEquals("MAN")
                || agentType.contentEquals("BDM")
                || agentType.contentEquals("TBDM")
                || agentType.contentEquals("UM")) {

        } else {
            btn_document_upload.setVisibility(View.VISIBLE);
            tr_default.setVisibility(View.VISIBLE);
            tr_default_proposal_number_tablerow_submit
                    .setVisibility(View.VISIBLE);
            ll_requirement_upload.setVisibility(View.VISIBLE);

        }

        ll_document_upload.setVisibility(View.GONE);

        exp_requirement_upload
                .setOnGroupClickListener(new OnGroupClickListener() {

                    public boolean onGroupClick(ExpandableListView arg0,
                                                View arg1, int arg2, long arg3) {
                        ll_requirement_upload.getLayoutParams().height = LayoutParams.WRAP_CONTENT;

                        ll_requirement_upload.requestLayout();
                        return false;
                    }
                });

        exp_requirement_upload
                .setOnGroupCollapseListener(new OnGroupCollapseListener() {

                    public void onGroupCollapse(int groupPosition) {
                        ll_requirement_upload.getLayoutParams().height = 0;
                        ll_requirement_upload.requestLayout();
                    }
                });

        /* From Document upload */

        List<String> ls_documents_direct_credit = new ArrayList<String>();

        ls_documents_direct_credit.add("Select");
        ls_documents_direct_credit.add("Copy of Bank Statement");
        ls_documents_direct_credit.add("Copy of Bank Passbook");
        ls_documents_direct_credit.add("Cancelled Cheque");
        ls_documents_direct_credit.add("Annexure III");

        fillSpinnerValue(spnr_document_upload_document_bank,
                ls_documents_direct_credit);

        List<String> ls_EFT = new ArrayList<String>();

        ls_EFT.add("Select");
        ls_EFT.add("EFT");
        ls_EFT.add("Cheque");
        ls_EFT.add("DD");

        fillSpinnerValue(spnr_document_upload_document_eft, ls_EFT);

        List<String> lst_quations = new ArrayList<String>();
        lst_quations.add("Select Questionnaires");
        lst_quations.add("1.Armed Services Q");
        lst_quations.add("2.Aviation Q");
        lst_quations.add("3. Climbing, Mountaineering Q");
        lst_quations.add("4. Diving Q");
        lst_quations.add("5. Extreme Sports Q");
        lst_quations.add("6. Fishing Q");
        lst_quations.add("7. Financial Q");
        lst_quations.add("8. Journalism Q");
        lst_quations.add("9. Merchant Marine Q");
        lst_quations.add("10. Mining Quarrying Q");
        lst_quations.add("11. NRI Q");
        lst_quations.add("12. Occupation Q");
        lst_quations.add("13. Oil and Natural Gas Q");
        lst_quations.add("14. Parachuting Q");
        lst_quations.add("15. Rafting Q");
        lst_quations.add("16. Minor Life Addendum");
        lst_quations.add("17. Natural Disaster Q");
        lst_quations.add("18. AML KYC Annexure I");
        lst_quations.add("19. AML KYC Annexure II");
        lst_quations.add("20. AML KYC Annexure III");
        lst_quations.add("21. Housewife Addendum");
        lst_quations.add("22. Residence and Travel Q");
        lst_quations.add("23. Special MHR & KYC Verification");
        lst_quations.add("24. Financial Evaluation Sheet");
        fillSpinnerValue(spnr_Questionnaires, lst_quations);

        List<String> upload_age_doc_name = new ArrayList<String>();
        // upload_age_doc_name.add("Select Document");
        // upload_age_doc_name.add("Affidavit");
        // upload_age_doc_name.add("Bank Certificate for its Customer");
        // upload_age_doc_name.add("Bank Certification of Date of Birth");
        // upload_age_doc_name.add("Baptism / Marriage Certificate");
        // upload_age_doc_name.add("Birth Certificate");
        // upload_age_doc_name.add("Certified Hospital Records");
        // upload_age_doc_name.add("Declaration certified by Gram Panchayat");
        // upload_age_doc_name.add("Domicile Certificate");
        // upload_age_doc_name.add("Driving License");
        // upload_age_doc_name.add("ESIS Card");
        // upload_age_doc_name.add("Extract from Register of Gram Panchayat");
        // upload_age_doc_name.add("Identity Card (Defence)");
        // upload_age_doc_name.add("Identity Card (PSU Employee)");
        // // ls_AgeProof.add("Others");
        // upload_age_doc_name.add("PF Statement (PSU Employees)");
        // upload_age_doc_name.add("Pancard");
        // upload_age_doc_name.add("Passport");
        // upload_age_doc_name.add("Pension Order Of Self");
        // upload_age_doc_name.add("Pension Order Of Spouse");
        // upload_age_doc_name.add("Ration Card");
        // upload_age_doc_name.add("School/College Certificate");
        // upload_age_doc_name
        // .add("School/College Certificate without registration no");
        // upload_age_doc_name.add("Service Extract (PSU)");
        // upload_age_doc_name.add("Voter s Identity Card");
        //
        // if
        // (planName.equals(getString(R.string.sbi_life_smart_champ_insurance)))
        // {
        // upload_age_doc_name.add("Aadhar Card");
        // }

        // if
        // (planName.equals(getString(R.string.sbi_life_smart_income_protect)))
        // {
        // String ptRiderStatus = "";
        //
        // .getBIDetail(QuatationNumber);
        // if (data.size() > 0) {
        // int i = 0;
        // String input = data.get(i).getInput();
        // ParseXML parObj = new ParseXML();
        // ptRiderStatus = parObj.parseXmlTag(input, "isPTRider");
        //
        // if (ptRiderStatus == null) {
        // ptRiderStatus = "";
        //
        // }
        //
        // }
        //
        // if (ptRiderStatus.equalsIgnoreCase("true")) {
        //
        // upload_age_doc_name.add("Select Document");
        // upload_age_doc_name.add("Birth Certificate");
        // upload_age_doc_name.add("School/College Certificate");
        // upload_age_doc_name.add("Passport");
        // upload_age_doc_name.add("Service Extract (PSU)");
        // upload_age_doc_name.add("Baptism / Marriage Certificate");
        // upload_age_doc_name.add("Domicile Certificate");
        // upload_age_doc_name.add("Identity Card (Defence)");
        // upload_age_doc_name.add("Driving License");
        // upload_age_doc_name.add("Pancard");
        // upload_age_doc_name.add("PF Statement (PSU Employees)");
        // upload_age_doc_name.add("Identity Card (PSU Employee)");
        // upload_age_doc_name.add("Pension Order Of Self");
        //
        // }
        //
        // else {
        //
        // upload_age_doc_name.add("Select Document");
        // upload_age_doc_name
        // .add("Extract from Register of Gram Panchayat");
        // upload_age_doc_name.add("Voter s Identity Card");
        // upload_age_doc_name.add("Ration Card");
        // upload_age_doc_name.add("ESIS Card");
        // upload_age_doc_name.add("Certified Hospital Records");
        // upload_age_doc_name
        // .add("Declaration certified by Gram Panchayat");
        // upload_age_doc_name.add("Bank Certification of Date of Birth");
        // upload_age_doc_name
        // .add("School/College Certificate without registration no");
        // upload_age_doc_name.add("Pension Order Of Spouse");
        // upload_age_doc_name.add("Bank Certificate for its Customer");
        // upload_age_doc_name.add("Affidavit");
        //
        // }
        //
        // }

        // else {

        upload_age_doc_name.add("Select Document");
        upload_age_doc_name.add("Birth Certificate");
        upload_age_doc_name.add("School/College Certificate");
        upload_age_doc_name.add("Passport");
        upload_age_doc_name.add("Service Extract (PSU)");
        upload_age_doc_name.add("Baptism / Marriage Certificate");
        upload_age_doc_name.add("Domicile Certificate");
        upload_age_doc_name.add("Identity Card (Defence)");
        upload_age_doc_name.add("Extract from Register of Gram Panchayat");
        upload_age_doc_name.add("Driving License");
        upload_age_doc_name.add("Pancard");
        upload_age_doc_name.add("PF Statement (PSU Employees)");
        upload_age_doc_name.add("Identity Card (PSU Employee)");
        upload_age_doc_name.add("Voter s Identity Card");
        upload_age_doc_name.add("Ration Card");
        upload_age_doc_name.add("ESIS Card");
        upload_age_doc_name.add("Certified Hospital Records");
        upload_age_doc_name.add("Declaration certified by Gram Panchayat");
        upload_age_doc_name.add("Bank Certification of Date of Birth");
        upload_age_doc_name
                .add("School/College Certificate without registration no");
        upload_age_doc_name.add("Pension Order Of Self");
        upload_age_doc_name.add("Pension Order Of Spouse");
        upload_age_doc_name.add("Bank Certificate for its Customer");
        upload_age_doc_name.add("Affidavit");

        /*if (planName
                .equals(getString(R.string.sbi_life_smart_guaranteed_saving_plan))) {
            upload_age_doc_name.add("Aadhar Card");
        }*/
        // }

        // upload_age_doc_name.add("Service Extract(PSU)");
        // upload_age_doc_name.add("Baptism/Marriage Certificate");
        // upload_age_doc_name.add("Domicile Certificate");
        // upload_age_doc_name.add("Identity Card(Defence)");
        // upload_age_doc_name.add("PF Statement(PSU Employee)");
        // upload_age_doc_name.add("Identity(PSU Employee)");
        fillSpinnerValue(spnr_document_upload_document_age, upload_age_doc_name);

        List<String> upload_address_doc_name = new ArrayList<String>();

        upload_address_doc_name.add("Select Document");
        upload_address_doc_name.add("Passport");
        upload_address_doc_name.add("Driving License");
        upload_address_doc_name.add("Voter s Identity Card");
        upload_address_doc_name.add("Ration Card");
        upload_address_doc_name.add("Aadhar Card");
        upload_address_doc_name.add("AADHAR Letter");
        upload_address_doc_name.add("TelePhone Bill");
        upload_address_doc_name.add("Bank Account Statement");
        upload_address_doc_name.add("Letter From Public Authority");
        upload_address_doc_name.add("Electricity Bill");
        upload_address_doc_name
                .add("Valid lease agreement with rent receipts.");
        upload_address_doc_name.add("Employer Certificate");
        upload_address_doc_name.add("Not Applicable");
        upload_address_doc_name.add("Not Submitted");
        upload_address_doc_name.add("Banker's certificate -(Ann III)");
        upload_address_doc_name.add("Simultaneous proposal");
        upload_address_doc_name.add("COI /AOA-KM");
        upload_address_doc_name.add("Board Resolution -KM");
        upload_address_doc_name.add("Power of Attorney -KM");
        upload_address_doc_name.add("Copy of PAN allotment letter-KM");
        upload_address_doc_name.add("Registration certificate-PF");
        upload_address_doc_name.add("Partnership deed-PF");
        upload_address_doc_name.add("Power of Attorney-PF");

        // upload_address_doc_name.add("Select Document");
        // upload_address_doc_name.add("Bank Account Statement");
        // /* ls_AddressProof.add("Banker's Certificate"); */
        // upload_address_doc_name.add("Banker's certificate -(Ann III)");
        // upload_address_doc_name.add("Board Resolution -KM");
        // upload_address_doc_name.add("COI /AOA-KM");
        // upload_address_doc_name.add("Copy of PAN allotment letter-KM");
        // upload_address_doc_name.add("Driving License");
        // upload_address_doc_name.add("Electricity Bill");
        // upload_address_doc_name.add("Employer Certificate");
        // upload_address_doc_name.add("Letter From Public Authority");
        // // ls_AddressProof.add("Not Applicable");
        // // ls_AddressProof.add("Not Submitted");
        // upload_address_doc_name.add("Partnership deed-PF");
        // upload_address_doc_name.add("Passport");
        // upload_address_doc_name.add("Power of Attorney -KM");
        // upload_address_doc_name.add("Power of Attorney-PF");
        // upload_address_doc_name.add("Ration Card");
        // upload_address_doc_name.add("Registration certificate-PF");
        // upload_address_doc_name.add("Simultaneous proposal");
        // upload_address_doc_name.add("TelePhone Bill");
        // upload_address_doc_name
        // .add("Valid lease agreement with rent receipts.");
        // upload_address_doc_name.add("Voter s Identity Card");
        fillSpinnerValue(spnr_document_upload_document_address,
                upload_address_doc_name);

        List<String> upload_identity_doc_name = new ArrayList<String>();

        upload_identity_doc_name.add("Select Document");
        upload_identity_doc_name.add("Passport");
        upload_identity_doc_name.add("Driving License");
        upload_identity_doc_name.add("Pancard");
        upload_identity_doc_name.add("Voter s Identity Card");
        upload_identity_doc_name.add("Letter From Public Authority");
        upload_identity_doc_name.add("Not Submitted");
        upload_identity_doc_name.add("Annexure I & III");
        upload_identity_doc_name.add("Simultaneous proposal");
        upload_identity_doc_name.add("COI /AOA-KM");
        upload_identity_doc_name.add("Board Resolution -KM");
        upload_identity_doc_name.add("Power of Attorney -KM");
        upload_identity_doc_name.add("Copy of PAN allotment letter-KM");
        upload_identity_doc_name.add("Registration certificate-PF");
        upload_identity_doc_name.add("Partnership deed-PF");
        upload_identity_doc_name.add("Power of Attorney-PF");
        upload_identity_doc_name.add("Aadhar Card");
        upload_identity_doc_name.add("Assessment Order");
        upload_identity_doc_name
                .add("Letter from Recognized Public Authority or Public Servant with photograph verifying the identity residence");
        fillSpinnerValue(spnr_document_upload_document_identity,
                upload_identity_doc_name);

        List<String> upload_income_doc_name = new ArrayList<String>();
        upload_income_doc_name.add("Select Document");

        upload_income_doc_name.add("Not Applicable");
        upload_income_doc_name.add("Income Tax returns");
        upload_income_doc_name.add("Employer Certificate");
        upload_income_doc_name.add("Not Submitted");
        upload_income_doc_name.add("Simultaneous proposal");
        upload_income_doc_name.add("Chartered Accountant's certificate");
        upload_income_doc_name.add("Agriculture Income Certificate");
        upload_income_doc_name.add("Agricultural land details Assessments");
        upload_income_doc_name.add("Bank pass book");
        upload_income_doc_name.add("Audited Company accounts -KM/PF");
        upload_income_doc_name
                .add("Audited firm accounts and Partnership Deed-KM/PF");
        upload_income_doc_name.add("Assessment Order");
        upload_income_doc_name.add("Income proof");
        upload_income_doc_name.add("Salary Slip");
        upload_income_doc_name.add("Form 16/ 16A");
        upload_income_doc_name
                .add("Audited balance sheet and Profit & loss account");
        upload_income_doc_name.add("Investment documents");
        upload_income_doc_name.add("Appointment letter");

        fillSpinnerValue(spnr_document_upload_document_income,
                upload_income_doc_name);
        Date();

        AgeProofsetBtnListenerOrDisable(
                img_btn_document_upload_click_image_age,
                AgeProofOnClickListener, MediaStore.ACTION_IMAGE_CAPTURE);
        IdentityProofsetBtnListenerOrDisable(
                img_btn_document_upload_click_image_identity,
                IdentityProofOnClickListener, MediaStore.ACTION_IMAGE_CAPTURE);
        AddressProofsetBtnListenerOrDisable(
                img_btn_document_upload_click_image_address,
                AddressProofOnClickListener, MediaStore.ACTION_IMAGE_CAPTURE);
        IncomeProofsetBtnListenerOrDisable(
                img_btn_document_upload_click_image_income,
                IncomeProofOnClickListener, MediaStore.ACTION_IMAGE_CAPTURE);

        OthersProofsetBtnListenerOrDisable(
                img_btn_document_upload_click_image_others,
                OthersProofOnClickListener, MediaStore.ACTION_IMAGE_CAPTURE);

        BankProofsetBtnListenerOrDisable(
                img_btn_document_upload_click_image_bank,
                BankProofOnClickListener, MediaStore.ACTION_IMAGE_CAPTURE);

        EFTProofsetBtnListenerOrDisable(
                img_btn_document_upload_click_image_eft,
                EFTProofOnClickListener, MediaStore.ACTION_IMAGE_CAPTURE);

        try {
            decr_Password = SimpleCrypto.decrypt("SBIL", encr_Password);
            encryptPassword = SimpleCrypto.encrypt("SBIL", "mukesh");
        } catch (Exception e) {
            e.printStackTrace();
        }

        btn_focus.requestFocus();
        setFocusable(btn_focus);
        hideKeyboard(AdditionalUtilityActivity.this);

        mGPGpsTracker = new GPSTracker(context);
        mLatLng = new LatLng(0.0, 0.0);

        getLocationPromt();
    }

    private void getLocationPromt(){
        LocationManager locationmanager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        if (locationmanager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

            try {
                mLatLng = mCommonMethods.getCurrentLocation(context, mGPGpsTracker);

                if (mLatLng.latitude == 0.0 && mLatLng.longitude == 0.0) {
                    mCommonMethods.showToast(context, "Please check your gps connection and try again");
                    mLatLng = new LatLng(0.0, 0.0);
                    mLatLng = mCommonMethods.getCurrentLocation(context, mGPGpsTracker);
                }

            } catch (Exception ex) {
                ex.printStackTrace();
                mLatLng = mCommonMethods.getCurrentLocation(context, mGPGpsTracker);
            }
        } else {
            mCommonMethods.showGPSDisabledAlertToUser(context);
        }
    }

    public void ProposalvalidationAlert() {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.loading_window);
        TextView text = dialog.findViewById(R.id.txtalertheader);
        text.setText("Please Enter Proposal No..");
        Button dialogButton = dialog.findViewById(R.id.btnalert);
        dialogButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    protected void service_hits(String strServiceName) {

        mProgressDialog = new ProgressDialog(context, ProgressDialog.THEME_HOLO_LIGHT);
        //String Message = "Please Wait";
        //mProgressDialog.setMessage(Html.fromHtml("<font color='#00a1e3'><b>" + Message + "<b></font>"));
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setTitle(Html
                .fromHtml("<font color='#00a1e3'><b>Loading<b></font>"));
        mProgressDialog.setMax(100);
        mProgressDialog.show();

        service = new ServiceHits(context, mProgressDialog,
                NAMESPACE, URl, SOAP_ACTION_SH, METHOD_NAME_SH, strServiceName);
        service.execute();

    }

    public class ServiceHits extends AsyncTask<String, Void, String> {
        Context mContext;
        ProgressDialog progressDialog = null;
        String NAMESPACE = "";
        String URL = "";
        String SOAP_ACTION = "";
        String METHOD_NAME = "";
        String strServiceName = "";

        public ServiceHits(Context mContext, ProgressDialog progressDialog,
                           String NAMESPACE, String URL, String SOAP_ACTION,
                           String METHOD_NAME, String strServiceName) {
            // TODO Auto-generated constructor stub

            this.NAMESPACE = NAMESPACE;
            this.URL = URL;
            this.SOAP_ACTION = SOAP_ACTION;
            this.METHOD_NAME = METHOD_NAME;
            this.mContext = mContext;
            this.strServiceName = strServiceName;
            this.progressDialog = progressDialog;
        }

        @Override
        protected String doInBackground(String... param) {
            // TODO Auto-generated method stub

            if (mCommonMethods.isNetworkConnected(context)) {

                try {

                    SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

                    request.addProperty("serviceName", strServiceName);
                    request.addProperty("strProdCode", "");

                    request.addProperty("serviceInput", "");
                    request.addProperty("serviceReqUserId", GetUserID());
                    request.addProperty("strEmailId", agentEmail);
                    request.addProperty("strMobileNo", agentMobile);
                    request.addProperty("strAuthKey",
                            SimpleCrypto.encrypt("SBIL", agentPassword.trim()));

                    mCommonMethods.TLSv12Enable();

                    SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                            SoapEnvelope.VER11);
                    // Enable this envelope if service is written in dot net
                    envelope.dotNet = true;
                    envelope.setOutputSoapObject(request);
                    HttpTransportSE androidHttpTransport = new HttpTransportSE(
                            URL);

                    // allowAllSSL();

                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                            .permitAll().build();
                    StrictMode.setThreadPolicy(policy);

                    androidHttpTransport.call(SOAP_ACTION, envelope);
                    SoapPrimitive response = (SoapPrimitive) envelope
                            .getResponse();

                    String result = response.toString();
                    System.out.println("Result:" + result);
                    if (result.contains("1")) {
                        return "Success";
                    } else {
                        return "Failure";
                    }

                } catch (Exception e) {
                    return "Server not Found. Please try after some time.";
                }

            } else
                return "Please Activate Internet connection";

        }

        @SuppressWarnings("deprecation")
        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

            try {
                if (progressDialog.isShowing())
                    progressDialog.dismiss();

            } catch (Exception e) {
                e.getMessage();
            }
            taskProposalTracker = new DownloadFileAsyncProposalTracker();

            if (strServiceName.contentEquals(METHOD_NAME_PROPOSAL_TRACKER)) {
                taskProposalTracker.execute("demo");
            } else if (strServiceName.contentEquals(METHOD_NAME_VALID_PROPSOAL)) {

                mProgressDialog = new ProgressDialog(
                        AdditionalUtilityActivity.this,
                        ProgressDialog.THEME_HOLO_LIGHT);
                String Message = "Please wait ,Loading...";
                mProgressDialog.setMessage(Html
                        .fromHtml("<font color='#00a1e3'><b>" + Message
                                + "<b></font>"));
                mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                mProgressDialog.setCancelable(false);

                mProgressDialog.setButton("Cancel",
                        new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog,
                                                int which) {

                                ProposalCheakService.cancel(true);
                                mProgressDialog.dismiss();
                            }
                        });

                mProgressDialog.setMax(100);
                mProgressDialog.show();
                ProposalCheakService = new AsynccheakProposal();
                ProposalCheakService.execute();
            } else if (strServiceName
                    .contentEquals(METHOD_NAME_UPLOAD_FILE_GET_REQUIREMENT)) {
                proposalCheakServiceRequirement = new AsynccheakProposalRequirement();
                proposalCheakServiceRequirement.execute();
            } else if (strServiceName.contentEquals(METHOD_NAME_UPLOAD_FILE)) {

                createSoapRequestToUploadDoc();
            }
        }
    }

    public void intereneterror() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.loading_window);
        TextView text = dialog.findViewById(R.id.txtalertheader);
        text.setText("Internet Connection Not Present,Try again..");
        Button dialogButton = dialog.findViewById(R.id.btnalert);
        dialogButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void GetDocumentDetails() {

        List<M_DocumentUploadStatus> DocumentDetails = null;

        int j = 0;
        try {
            DocumentDetails = db.getDocumentsDetails(ProposalNumber);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (DocumentDetails.size() > 0) {
            for (j = 0; j < DocumentDetails.size(); j++) {
                String ProofOf = DocumentDetails.get(j).getProofOf();
                String DocumentName = DocumentDetails.get(j).getDocumentName();
                String FileName = DocumentDetails.get(j).getFileName();

                if (ProofOf.equalsIgnoreCase("INCOME")) {
                    if (FileName.contains("R")) {

                        lst_Income.add(ProofOf);
                    }

                }
                if (ProofOf.equalsIgnoreCase("AGE")) {
                    if (FileName.contains("R")) {

                        lst_Age.add(ProofOf);
                    }

                }
                if (ProofOf.equalsIgnoreCase("IDENTITY")) {
                    if (FileName.contains("R")) {

                        lst_Identity.add(ProofOf);
                    }

                }
                if (ProofOf.equalsIgnoreCase("ADDRESS")) {
                    if (FileName.contains("R")) {

                        lst_Address.add(ProofOf);
                    }

                }
                if (ProofOf.equalsIgnoreCase("OTHERS")) {
                    if (FileName.contains("R")) {

                        lst_Others.add(ProofOf);
                    }

                }

                if (ProofOf.equalsIgnoreCase("BANK")) {
                    if (FileName.contains("R")) {

                        lst_Bank.add(ProofOf);
                    }

                }

                if (ProofOf.equalsIgnoreCase("EFT")) {
                    if (FileName.contains("R")) {
                        lst_EFT.add(ProofOf);
                    }

                }

                if (ProofOf.equalsIgnoreCase("OTHERS")) {
                    spnr_Questionnaires.setSelection(
                            getIndex(spnr_Questionnaires, DocumentName), false);
                    // otherProofFlag = true;
                    edt_document_upload_document_others.setText("");

                } else {
                    edt_document_upload_document_others.setText("");
                }
            }
            addflag = lst_Income.size() + lst_Age.size() + lst_Identity.size()
                    + lst_Address.size() + lst_Others.size() + lst_Bank.size()
                    + lst_EFT.size() + 1;
            lst_Income.clear();
            lst_Age.clear();
            lst_Identity.clear();
            lst_Address.clear();
            lst_Others.clear();
            lst_Bank.clear();
            lst_EFT.size();
        }

    }

    public void InitializeVariable() {

        cb_document_upload_age_upload_tick = findViewById(R.id.cb_document_upload_age_upload_tick);
        cb_document_upload_identity_upload_tick = findViewById(R.id.cb_document_upload_identity_upload_tick);
        cb_document_upload_address_upload_tick = findViewById(R.id.cb_document_upload_address_upload_tick);
        cb_document_upload_income_upload_tick = findViewById(R.id.cb_document_upload_income_upload_tick);
        cb_document_upload_bank_upload_tick = findViewById(R.id.cb_document_upload_bank_upload_tick);
        cb_document_upload_others_upload_tick = findViewById(R.id.cb_document_upload_others_upload_tick);
        cb_document_upload_eft_upload_tick = findViewById(R.id.cb_document_upload_eft_upload_tick);

        spnr_document_upload_document_age = findViewById(R.id.spnr_document_upload_document_age);
        spnr_document_upload_document_identity = findViewById(R.id.spnr_document_upload_document_identity);
        spnr_document_upload_document_address = findViewById(R.id.spnr_document_upload_document_address);
        spnr_document_upload_document_income = findViewById(R.id.spnr_document_upload_document_income);
        spnr_Questionnaires = findViewById(R.id.spnr_Questionnaires);
        /* Image Button for BrowseButton */

        img_btn_document_upload_click_browse_age = findViewById(R.id.img_btn_document_upload_click_browse_age);
        img_btn_document_upload_click_browse_identity = findViewById(R.id.img_btn_document_upload_click_browse_identity);
        img_btn_document_upload_click_browse_address = findViewById(R.id.img_btn_document_upload_click_browse_address);
        img_btn_document_upload_click_browse_income = findViewById(R.id.img_btn_document_upload_click_browse_income);
        img_btn_document_upload_click_browse_Others = findViewById(R.id.img_btn_document_upload_click_browse_Others);
        /* Image Button for CameraButton */

        img_btn_document_upload_click_image_age = findViewById(R.id.img_btn_document_upload_click_image_age);
        img_btn_document_upload_click_image_identity = findViewById(R.id.img_btn_document_upload_click_image_identity);
        img_btn_document_upload_click_image_address = findViewById(R.id.img_btn_document_upload_click_image_address);
        img_btn_document_upload_click_image_income = findViewById(R.id.img_btn_document_upload_click_image_income);
        img_btn_document_upload_click_image_others = findViewById(R.id.img_btn_document_upload_click_image_others);
        /* Image Button for Preview Button */

        img_btn_document_upload_preview_image_age = findViewById(R.id.img_btn_document_upload_preview_image_age);
        img_btn_document_upload_preview_image_identity = findViewById(R.id.img_btn_document_upload_preview_image_identity);
        img_btn_document_upload_click_preview_image_address = findViewById(R.id.img_btn_document_upload_click_preview_image_address);
        img_btn_document_upload_click_preview_image_income = findViewById(R.id.img_btn_document_upload_click_preview_image_income);
        img_btn_document_upload_click_preview_image_others = findViewById(R.id.img_btn_document_upload_click_preview_image_others);
        getSignature_Proposer = findViewById(R.id.Ibtn_signature_proposer);
        getphoto = findViewById(R.id.proposer_photograph);

        /* Image Button to delete Document */
        img_btn_document_delete_age = findViewById(R.id.img_btn_document_delete_age);
        img_btn_document_delete_identity = findViewById(R.id.img_btn_document_delete_identity);
        img_btn_document_delete_address = findViewById(R.id.img_btn_document_delete_address);
        img_btn_document_delete_income = findViewById(R.id.img_btn_document_delete_income);
        img_btn_document_delete_Others = findViewById(R.id.img_btn_document_delete_Others);

        edt_document_upload_document_others = findViewById(R.id.edt_document_upload_document_others);

        exp_requirement_upload = findViewById(R.id.exp_requirement_upload);

        ll_requirement_upload = findViewById(R.id.ll_requirement_upload);
        // ll_requirement_upload.setVisibility(View.GONE);
        ll_medical_req_header = findViewById(R.id.ll_medical_req_header);
        ll_non_medical_requirements_not_taken_up = findViewById(R.id.ll_non_medical_requirements_not_taken_up);
        edt_requirement_Proposer_no = findViewById(R.id.edt_requirement_Proposer_no);

        // edt_pay_shortage_proposer_number = (EditText)
        // findViewById(R.id.edt_pay_shortage_proposer_number);
        // edt_pay_shortage_amount = (EditText)
        // findViewById(R.id.edt_pay_shortage_amount);
        // spnr_pay_shortage_quation_no = (Spinner)
        // findViewById(R.id.spnr_pay_shortage_quation_no);

        // spnr_resync_document_quation_no = (Spinner)
        // findViewById(R.id.spnr_resync_document_quation_no);
        // edt_resync_document_proposer_number = (EditText)
        // findViewById(R.id.edt_resync_document_proposer_number);

        img_btn_document_upload_click_preview_image_bank = findViewById(R.id.img_btn_document_upload_click_preview_image_bank);
        img_btn_document_delete_bank = findViewById(R.id.img_btn_document_delete_bank);
        img_btn_document_upload_click_image_bank = findViewById(R.id.img_btn_document_upload_click_image_bank);
        img_btn_document_upload_click_browse_bank = findViewById(R.id.img_btn_document_upload_click_browse_bank);
        img_btn_document_upload_bank_upload = findViewById(R.id.img_btn_document_upload_bank_upload);
        spnr_document_upload_document_bank = findViewById(R.id.spnr_document_upload_document_bank);
        // tv_proposalName_document_upload = (TextView)
        // findViewById(R.id.tv_proposalName_document_upload);
        tv_proposalName_resync_document = findViewById(R.id.tv_proposalName_resync_document);

        spnr_document_upload_document_eft = findViewById(R.id.spnr_document_upload_document_eft);
        img_btn_document_upload_preview_image_eft = findViewById(R.id.img_btn_document_upload_preview_image_eft);
        img_btn_document_delete_eft = findViewById(R.id.img_btn_document_delete_eft);
        img_btn_document_upload_click_image_eft = findViewById(R.id.img_btn_document_upload_click_image_eft);
        img_btn_document_upload_click_browse_eft = findViewById(R.id.img_btn_document_upload_click_browse_eft);
        img_btn_document_upload_eft_upload = findViewById(R.id.img_btn_document_upload_eft_upload);
        ll_document_upload = findViewById(R.id.ll_document_upload);

        tr_document_upload_proof_of_age = findViewById(R.id.tr_document_upload_proof_of_age);
        tr_document_upload_proof_of_identity = findViewById(R.id.tr_document_upload_proof_of_identity);
        tr_document_upload_proof_of_address = findViewById(R.id.tr_document_upload_proof_of_address);
        tr_income_proof = findViewById(R.id.tr_document_upload_proof_of_income);
        tr_document_upload_proof_of_bank = findViewById(R.id.tr_document_upload_proof_of_bank);
        tr_document_upload_proof_of_others = findViewById(R.id.tr_document_upload_proof_of_others);
        tr_document_upload_proof_of_eft = findViewById(R.id.tr_document_upload_proof_of_eft);
        tr_non_medical_header = findViewById(R.id.tr_non_medical_header);
        tr_medical_requirement_header = findViewById(R.id.tr_medical_requirement_header);
        txt_document_upload_proof_of_age = findViewById(R.id.txt_document_upload_proof_of_age);
        txt_document_upload_proof_of_identity = findViewById(R.id.txt_document_upload_proof_of_identity);
        txt_document_upload_proof_of_address = findViewById(R.id.txt_document_upload_proof_of_address);
        txt_document_upload_proof_of_income = findViewById(R.id.txt_document_upload_proof_of_income);
        txt_document_upload_proof_of_bank = findViewById(R.id.txt_document_upload_proof_of_bank);
        txt_document_upload_proof_of_others = findViewById(R.id.txt_document_upload_proof_of_others);
        txt_document_upload_proof_of_eft = findViewById(R.id.txt_document_upload_proof_of_eft);

        listView = findViewById(R.id.lv_medical_list);
        lv_nonmedical_not_taken_up_list = findViewById(R.id.lv_nonmedical_not_taken_up_list);
    }

    /* From Document upload */

    @SuppressWarnings("deprecation")
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DIALOG_DOWNLOAD_PROGRESS:
                mProgressDialog = new ProgressDialog(this,
                        ProgressDialog.THEME_HOLO_LIGHT);
                String Message = "Loading. Please wait...";
                mProgressDialog.setMessage(Html
                        .fromHtml("<font color='#00a1e3'><b>" + Message
                                + "<b></font>"));
                mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                mProgressDialog.setCancelable(true);

                mProgressDialog.setButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                taskProposalTracker.cancel(true);
                                mProgressDialog.dismiss();
                            }
                        });

                mProgressDialog.setMax(100);
                mProgressDialog.show();
                return mProgressDialog;

            case DIALOG_ALERT:
                Builder builder = new Builder(this,
                        AlertDialog.THEME_HOLO_LIGHT);
                builder.setTitle("Confirmation");
                builder.setMessage("Do you want to capture next page of same Document ?");
                builder.setCancelable(false);
                builder.setPositiveButton("Yes", new OkOnClickListener());
                builder.setNegativeButton("No", new CancelOnClickListener());
                AlertDialog dialog = builder.create();
                dialog.show();

            case DATE_DIALOG_ID:
                /*
                 * return new DatePickerDialog(this, mDateSetListener, mYear,
                 * mMonth, mDay);
                 */

                return new DatePickerDialog(this, R.style.datepickerstyle,
                        mDateSetListener, mYear, mMonth, mDay);
        }
        return super.onCreateDialog(id);
    }

    private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {

            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;
            System.out.println("mDateSetListener myear:" + year + " month:"
                    + monthOfYear + " mday:" + dayOfMonth);
            updateDisplay(mYear, mMonth, mDay);

        }

    };

    private void updateDisplay(int year, int month, int day) {

        String y = String.valueOf(year);
        String m = String.valueOf(month + 1);
        // date = String.valueOf(day);
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

        // String totaldate = da + "-" + m + "-" + y;

        /*
         * if (datecheck == 10) { edservicepass.setText(totaldate); }
         */
    }

    public void CreateDialog() {
        Builder builder = new Builder(this,
                AlertDialog.THEME_HOLO_LIGHT);
        builder.setTitle("Confirmation");
        builder.setMessage("Do you want to capture next page of same Document ?");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", new OkOnClickListener());
        builder.setNegativeButton("No", new CancelOnClickListener());
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private final class CancelOnClickListener implements
            DialogInterface.OnClickListener {

        public void onClick(DialogInterface dialog, int which) {
            if (!(deleteAgeDocument == 1) || !(deleteAddressDocument == 1)
                    || !(deleteIncomeDocument == 1)
                    || !(deleteIdentityDocument == 1)
                    || !(deleteOtherDocument == 1)
                    || !(deleteBankDocument == 1) || !(deleteEFTDocument == 1)) {

                if (SelfAttestedDocumentActivity.lst_AgeBitmap.size() >= 1) {
                    flag_cancel_age = 1;
                    increment = 1;

                    SelfAttestedDocumentActivity obj = new SelfAttestedDocumentActivity();
                    obj.createDocumentPdf(increment, "Proof : ",
                            des1_Age.replace(":", ""),
                            SelfAttestedDocumentActivity.lst_AgeBitmap,
                            proposerSign);

                }

                if (SelfAttestedDocumentActivity.lst_AddressBitmap.size() >= 1) {
                    flag_cancel_address = 1;
                    increment = 3;
                    SelfAttestedDocumentActivity obj = new SelfAttestedDocumentActivity();
                    obj.createDocumentPdf(increment, "Proof : ",
                            des3_Address.replace(":", ""),
                            SelfAttestedDocumentActivity.lst_AddressBitmap,
                            proposerSign);

                }

                if (SelfAttestedDocumentActivity.lst_IdentityBitmap.size() >= 1) {
                    flag_cancel_identity = 1;
                    increment = 2;
                    SelfAttestedDocumentActivity obj = new SelfAttestedDocumentActivity();
                    obj.createDocumentPdf(increment, "Proof : ",
                            des2_Identity.replace(":", ""),
                            SelfAttestedDocumentActivity.lst_IdentityBitmap,
                            proposerSign);
                }

                if (SelfAttestedDocumentActivity.lst_IncomeBitmap.size() >= 1) {
                    flag_cancel_income = 1;
                    increment = 4;
                    SelfAttestedDocumentActivity obj = new SelfAttestedDocumentActivity();
                    obj.createDocumentPdf(increment, "Proof : ",
                            des4_Income.replace(":", ""),
                            SelfAttestedDocumentActivity.lst_IncomeBitmap,
                            proposerSign);
                }

                if (SelfAttestedDocumentActivity.lst_OtherBitmap.size() >= 1) {
                    flag_cancel_others = 1;
                    increment = 5;
                    SelfAttestedDocumentActivity obj = new SelfAttestedDocumentActivity();
                    obj.createDocumentPdf(increment, "Proof : ",
                            des6_others.replace(":", ""),
                            SelfAttestedDocumentActivity.lst_OtherBitmap,
                            proposerSign);
                }

                if (SelfAttestedDocumentActivity.lst_BankBitmap.size() >= 1) {
                    flag_cancel_bank = 1;
                    increment = 6;
                    SelfAttestedDocumentActivity obj = new SelfAttestedDocumentActivity();
                    obj.createDocumentPdf(increment, "Proof : ",
                            des5_bank.replace(":", ""),
                            SelfAttestedDocumentActivity.lst_BankBitmap,
                            proposerSign);
                }

                if (SelfAttestedDocumentActivity.lst_EFTBitmap.size() >= 1) {
                    flag_cancel_eft = 1;
                    increment = 7;
                    SelfAttestedDocumentActivity obj = new SelfAttestedDocumentActivity();
                    obj.createDocumentPdf(increment, "Proof : ",
                            des7_EFT.replace(":", ""),
                            SelfAttestedDocumentActivity.lst_BankBitmap,
                            proposerSign);
                }
            }
            dialog.dismiss();
        }
    }

    private final class OkOnClickListener implements
            DialogInterface.OnClickListener {

        public void onClick(DialogInterface dialog, int which) {

            if (deleteAgeDocument == 1) {

                if (AgeProofFileName != null) {

                    if (AgeProofFileName.exists()) {

                        AgeProofFileName.delete();

                        img_btn_document_upload_preview_image_age
                                .setVisibility(View.INVISIBLE);
                        img_btn_document_delete_age.setVisibility(View.INVISIBLE);

                        img_btn_document_upload_click_image_age
                                .setImageDrawable(getResources().getDrawable(
                                        R.drawable.ibtn_camera));
                        img_btn_document_upload_click_browse_age
                                .setImageDrawable(getResources().getDrawable(
                                        R.drawable.ibtn_browsedoc));
                        //SelfAttestedDocumentActivity.lst_AgeBitmap.clear();
                        deleteAgeDocument = 0;
                    } else {
                        mCommonMethods.showToast(context, "File Not Found..");
                    }

                } else {
                    mCommonMethods.showToast(context, "Please browse or capture document");
                }

            } else if (deleteAddressDocument == 1) {

                if (AddressProofFileName != null) {

                    if (AddressProofFileName.exists()) {

                        AddressProofFileName.delete();

                        img_btn_document_upload_click_preview_image_address
                                .setVisibility(View.INVISIBLE);
                        img_btn_document_delete_address.setVisibility(View.INVISIBLE);

                        img_btn_document_upload_click_image_address
                                .setImageDrawable(getResources().getDrawable(
                                        R.drawable.ibtn_camera));
                        img_btn_document_upload_click_browse_address
                                .setImageDrawable(getResources().getDrawable(
                                        R.drawable.ibtn_browsedoc));
                        //SelfAttestedDocumentActivity.lst_AddressBitmap.clear();
                        deleteAddressDocument = 0;
                    } else {
                        mCommonMethods.showToast(context, "File Not Found..");
                    }

                } else {
                    mCommonMethods.showToast(context, "Please browse or capture document");
                }
            } else if (deleteIncomeDocument == 1) {

                if (IncomeProofFileName != null) {

                    if (IncomeProofFileName.exists()) {

                        IncomeProofFileName.delete();

                        img_btn_document_upload_click_preview_image_income
                                .setVisibility(View.INVISIBLE);
                        img_btn_document_delete_income.setVisibility(View.INVISIBLE);

                        img_btn_document_upload_click_image_income
                                .setImageDrawable(getResources().getDrawable(
                                        R.drawable.ibtn_camera));
                        img_btn_document_upload_click_browse_income
                                .setImageDrawable(getResources().getDrawable(
                                        R.drawable.ibtn_browsedoc));
                        //SelfAttestedDocumentActivity.lst_IncomeBitmap.clear();
                        deleteIncomeDocument = 0;
                    } else {
                        mCommonMethods.showToast(context, "File Not Found..");
                    }

                } else {
                    mCommonMethods.showToast(context, "Please browse or capture document");
                }
            } else if (deleteIdentityDocument == 1) {

                if (IdentityProofFileName != null) {

                    if (IdentityProofFileName.exists()) {

                        IdentityProofFileName.delete();

                        img_btn_document_upload_preview_image_identity
                                .setVisibility(View.INVISIBLE);
                        img_btn_document_delete_identity.setVisibility(View.INVISIBLE);

                        img_btn_document_upload_click_image_identity
                                .setImageDrawable(getResources().getDrawable(
                                        R.drawable.ibtn_camera));
                        img_btn_document_upload_click_browse_identity
                                .setImageDrawable(getResources().getDrawable(
                                        R.drawable.ibtn_browsedoc));
                        //SelfAttestedDocumentActivity.lst_IdentityBitmap.clear();
                        deleteIdentityDocument = 0;
                    } else {
                        mCommonMethods.showToast(context, "File Not Found..");
                    }

                } else {
                    mCommonMethods.showToast(context, "Please browse or capture document");
                }
            } else if (deleteOtherDocument == 1) {

                if (OtherProofFileName != null) {

                    if (OtherProofFileName.exists()) {

                        OtherProofFileName.delete();

                        img_btn_document_upload_click_preview_image_others
                                .setVisibility(View.INVISIBLE);
                        img_btn_document_delete_Others.setVisibility(View.INVISIBLE);

                        img_btn_document_upload_click_image_others
                                .setImageDrawable(getResources().getDrawable(
                                        R.drawable.ibtn_camera));
                        img_btn_document_upload_click_browse_Others
                                .setImageDrawable(getResources().getDrawable(
                                        R.drawable.ibtn_browsedoc));
                        //SelfAttestedDocumentActivity.lst_OtherBitmap.clear();
                        deleteOtherDocument = 0;
                    } else {
                        mCommonMethods.showToast(context, "File Not Found..");
                    }

                } else {
                    mCommonMethods.showToast(context, "Please browse or capture document");
                }

            } else if (deleteBankDocument == 1) {

                if (BankProofFileName != null) {

                    if (BankProofFileName.exists()) {

                        BankProofFileName.delete();

                        img_btn_document_upload_click_preview_image_bank
                                .setVisibility(View.INVISIBLE);
                        img_btn_document_delete_bank.setVisibility(View.INVISIBLE);

                        img_btn_document_upload_click_image_bank
                                .setImageDrawable(getResources().getDrawable(
                                        R.drawable.ibtn_camera));
                        img_btn_document_upload_click_browse_bank
                                .setImageDrawable(getResources().getDrawable(
                                        R.drawable.ibtn_browsedoc));
                        //SelfAttestedDocumentActivity.lst_BankBitmap.clear();
                        deleteBankDocument = 0;
                    } else {
                        mCommonMethods.showToast(context, "File Not Found..");
                    }

                } else {
                    mCommonMethods.showToast(context, "Please browse or capture document");
                }
            } else if (deleteEFTDocument == 1) {

                if (EFTProofFileName != null) {

                    if (EFTProofFileName.exists()) {

                        EFTProofFileName.delete();

                        img_btn_document_upload_preview_image_eft
                                .setVisibility(View.INVISIBLE);
                        img_btn_document_delete_eft.setVisibility(View.INVISIBLE);

                        img_btn_document_upload_click_image_eft
                                .setImageDrawable(getResources().getDrawable(
                                        R.drawable.ibtn_camera));
                        img_btn_document_upload_click_browse_eft
                                .setImageDrawable(getResources().getDrawable(
                                        R.drawable.ibtn_browsedoc));
                        //SelfAttestedDocumentActivity.lst_EFTBitmap.clear();
                        deleteEFTDocument = 0;
                    } else {
                        mCommonMethods.showToast(context, "File Not Found..");
                    }

                } else {
                    mCommonMethods.showToast(context, "Please browse or capture document");
                }
            } else {

                if (uploadFlag == 1) {
                    dispatchTakePictureIntent();
                } else if (uploadFlag == 0) {
                    onBrowse();
                }
            }
        }
    }

    OnClickListener AgeProofOnClickListener = new OnClickListener() {

        public void onClick(View v) {

            if (!(edt_requirement_Proposer_no.getText().toString()
                    .equalsIgnoreCase(""))) {
                if (!des1_Age.equals("")) {
                    SelfAttestedDocumentActivity.lst_AgeBitmap.clear();
                    uploadFlag = 1;
                    img_btn_document_upload_click_browse_age
                            .setImageDrawable(getResources().getDrawable(
                                    R.drawable.ibtn_browsedoc));
                    Check = "AgeProof";
                    /*if (spnr_document_upload_document_age.getSelectedItem().toString().toLowerCase().contains("aadhar card")
                            || spnr_document_upload_document_age.getSelectedItem().toString().toLowerCase().contains("pancard")
                            || spnr_document_upload_document_age.getSelectedItem().toString().toLowerCase().contains("passport")
                            || spnr_document_upload_document_age.getSelectedItem().toString().toLowerCase().contains("voter s identity card")
                            || spnr_document_upload_document_age.getSelectedItem().toString().toLowerCase().contains("driving license")) {*/
                        Intent intent = new Intent(AdditionalUtilityActivity.this, OcrActivity.class);
                        startActivityForResult(intent, REQUEST_OCR);
                    /*} else {
                        dispatchTakePictureIntent();
                    }*/
                } else {
                    upload_Flag = 0;
                    validate_age();
                }
            } else {
                AlertDialogMessage message = new AlertDialogMessage();
                message.dialog(AdditionalUtilityActivity.this,
                        "Your Proposal Number is not generated !!!", true);
            }

        }
    };

    OnClickListener IdentityProofOnClickListener = new OnClickListener() {

        public void onClick(View v) {

            if (!(edt_requirement_Proposer_no.getText().toString()
                    .equalsIgnoreCase(""))) {

                if (!des2_Identity.equals("")) {
                    SelfAttestedDocumentActivity.lst_IdentityBitmap.clear();
                    uploadFlag = 1;
                    img_btn_document_upload_click_browse_identity
                            .setImageDrawable(getResources().getDrawable(
                                    R.drawable.ibtn_browsedoc));
                    Check = "IdentityProof";
                    /*if (spnr_document_upload_document_identity.getSelectedItem().toString().toLowerCase().contains("aadhar card")
                            || spnr_document_upload_document_identity.getSelectedItem().toString().toLowerCase().contains("pancard")
                            || spnr_document_upload_document_identity.getSelectedItem().toString().toLowerCase().contains("passport")
                            || spnr_document_upload_document_identity.getSelectedItem().toString().toLowerCase().contains("voter s identity card")
                            || spnr_document_upload_document_identity.getSelectedItem().toString().toLowerCase().contains("driving license")) {*/
                        Intent intent = new Intent(AdditionalUtilityActivity.this, OcrActivity.class);
                        startActivityForResult(intent, REQUEST_OCR);
                    /*} else {
                        dispatchTakePictureIntent();
                    }*/
                } else {
                    upload_Flag = 0;
                    validate_identity();
                }

            } else {
                AlertDialogMessage message = new AlertDialogMessage();
                message.dialog(AdditionalUtilityActivity.this,
                        "Your Proposal Number is not generated !!!", true);
            }

        }
    };

    OnClickListener AddressProofOnClickListener = new OnClickListener() {

        public void onClick(View v) {

            if (!(edt_requirement_Proposer_no.getText().toString()
                    .equalsIgnoreCase(""))) {

                if (!des3_Address.equals("")) {
                    uploadFlag = 1;
                    SelfAttestedDocumentActivity.lst_AddressBitmap.clear();
                    img_btn_document_upload_click_browse_address
                            .setImageDrawable(getResources().getDrawable(
                                    R.drawable.ibtn_browsedoc));
                    Check = "AddressProof";

                    /*if (spnr_document_upload_document_address.getSelectedItem().toString().toLowerCase().contains("aadhar card")
                            || spnr_document_upload_document_address.getSelectedItem().toString().toLowerCase().contains("pancard")
                            || spnr_document_upload_document_address.getSelectedItem().toString().toLowerCase().contains("passport")
                            || spnr_document_upload_document_address.getSelectedItem().toString().toLowerCase().contains("voter s identity card")
                            || spnr_document_upload_document_address.getSelectedItem().toString().toLowerCase().contains("driving license")) {*/
                        Intent intent = new Intent(AdditionalUtilityActivity.this, OcrActivity.class);
                        startActivityForResult(intent, REQUEST_OCR);
                    /*} else {
                        dispatchTakePictureIntent();
                    }*/
                } else {
                    upload_Flag = 0;
                    validate_address();
                }

            } else {
                AlertDialogMessage message = new AlertDialogMessage();
                message.dialog(AdditionalUtilityActivity.this,
                        "Your Proposal Number is not generated !!!", true);
            }

        }
    };

    OnClickListener IncomeProofOnClickListener = new OnClickListener() {

        public void onClick(View v) {

            if (!(edt_requirement_Proposer_no.getText().toString()
                    .equalsIgnoreCase(""))) {
                if (!des4_Income.equals("")) {
                    SelfAttestedDocumentActivity.lst_IncomeBitmap.clear();
                    uploadFlag = 1;
                    img_btn_document_upload_click_browse_income
                            .setImageDrawable(getResources().getDrawable(
                                    R.drawable.ibtn_browsedoc));
                    Check = "IncomeProof";

                    dispatchTakePictureIntent();
                } else {
                    upload_Flag = 0;
                    validate_income();
                }
            } else {
                AlertDialogMessage message = new AlertDialogMessage();
                message.dialog(AdditionalUtilityActivity.this,
                        "Your Proposal Number is not generated !!!", true);
            }

        }
    };
    OnClickListener OthersProofOnClickListener = new OnClickListener() {

        public void onClick(View v) {

            if (!(edt_requirement_Proposer_no.getText().toString()
                    .equalsIgnoreCase(""))) {
                if (!des6_others.equals("")) {
                    SelfAttestedDocumentActivity.lst_OtherBitmap.clear();
                    uploadFlag = 1;
                    img_btn_document_upload_click_browse_Others
                            .setImageDrawable(getResources().getDrawable(
                                    R.drawable.ibtn_browsedoc));
                    Check = "OtherProof";
                    dispatchTakePictureIntent();

                } else {
                    upload_Flag = 0;
                    validate_others();
                }
            } else {
                AlertDialogMessage message = new AlertDialogMessage();
                message.dialog(AdditionalUtilityActivity.this,
                        "Your Proposal Number is not generated !!!", true);
            }

        }
    };

    OnClickListener BankProofOnClickListener = new OnClickListener() {

        public void onClick(View v) {

            if (!(edt_requirement_Proposer_no.getText().toString()
                    .equalsIgnoreCase(""))) {
                if (!des5_bank.equals("")) {
                    SelfAttestedDocumentActivity.lst_BankBitmap.clear();
                    uploadFlag = 1;
                    img_btn_document_upload_click_browse_bank
                            .setImageDrawable(getResources().getDrawable(
                                    R.drawable.ibtn_browsedoc));
                    Check = "BankProof";

                    /*if (spnr_document_upload_document_bank.getSelectedItem().toString().contains("Cancelled Cheque")) {*/

                        //isBrowseCapture = REQUEST_CODE_PICK_PHOTO_FILE;

                        Intent intent = new Intent(AdditionalUtilityActivity.this, OcrActivity.class);
                        startActivityForResult(intent, REQUEST_OCR);
                    /*} else {
                    dispatchTakePictureIntent();
                    }*/

                } else {
                    upload_Flag = 0;
                    validate_bankproof();
                }
            } else {
                AlertDialogMessage message = new AlertDialogMessage();
                message.dialog(AdditionalUtilityActivity.this,
                        "Your Proposal Number is not generated !!!", true);
            }

        }
    };

    OnClickListener EFTProofOnClickListener = new OnClickListener() {

        public void onClick(View v) {

            if (!(edt_requirement_Proposer_no.getText().toString()
                    .equalsIgnoreCase(""))) {
                if (!des7_EFT.equals("")) {
                    SelfAttestedDocumentActivity.lst_EFTBitmap.clear();
                    uploadFlag = 1;
                    img_btn_document_upload_click_browse_eft
                            .setImageDrawable(getResources().getDrawable(
                                    R.drawable.ibtn_browsedoc));
                    Check = "EFTProof";

                    dispatchTakePictureIntent();
                } else {
                    upload_Flag = 0;
                    validate_eft();
                }
            } else {
                AlertDialogMessage message = new AlertDialogMessage();
                message.dialog(AdditionalUtilityActivity.this,
                        "Your Proposal Number is not generated !!!", true);
            }

        }
    };

    public static boolean isIntentAvailable(Context context, String action) {
        final PackageManager packageManager = context.getPackageManager();
        final Intent intent = new Intent(action);
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent,
                PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    public void dispatchTakePictureIntent() {
        try {

            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            String imageFileName = JPEG_FILE_PREFIX + ProposalNumber + "_X0"
                    + increment + "_" + JPEG_FILE_SUFFIX;
            f = mStorageUtils.createFileToAppSpecificDir(context, imageFileName);
            mCurrentPhotoPath = f.getAbsolutePath();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mCommonMethods.getContentUri(context, f));
            } else {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
            }

            startActivityForResult(takePictureIntent, REQUEST_CODE_CAPTURE_FILE);
        } catch (SecurityException e) {
            mCommonMethods.showMessageDialog(context,"There might be issue in Permissions");
        }catch(Exception e) {
            mCommonMethods.showMessageDialog(context,"Something went wrong");
        }
    }

    private void AgeProofsetBtnListenerOrDisable(ImageButton btn,
                                                 OnClickListener mTakePicOnClickListener, String intentName) {

        if (isIntentAvailable(this, intentName)) {

            btn.setOnClickListener(mTakePicOnClickListener);

        } else {
            btn.setTag(getText(R.string.cannot).toString() + " "
                    + btn.getContext());
            btn.setClickable(false);
        }

    }

    private void IdentityProofsetBtnListenerOrDisable(ImageButton btn,
                                                      OnClickListener mTakePicOnClickListener, String intentName) {
        if (isIntentAvailable(this, intentName)) {
            btn.setOnClickListener(mTakePicOnClickListener);
        } else {
            btn.setTag(getText(R.string.cannot).toString() + " "
                    + btn.getContext());
            btn.setClickable(false);
        }
    }

    private void AddressProofsetBtnListenerOrDisable(ImageButton btn,
                                                     OnClickListener mTakePicOnClickListener, String intentName) {
        if (isIntentAvailable(this, intentName)) {
            btn.setOnClickListener(mTakePicOnClickListener);
        } else {
            btn.setTag(getText(R.string.cannot).toString() + " "
                    + btn.getContext());
            btn.setClickable(false);
        }
    }

    private void IncomeProofsetBtnListenerOrDisable(ImageButton btn,
                                                    OnClickListener mTakePicOnClickListener, String intentName) {
        if (isIntentAvailable(this, intentName)) {
            btn.setOnClickListener(mTakePicOnClickListener);
        } else {
            btn.setTag(getText(R.string.cannot).toString() + " "
                    + btn.getContext());
            btn.setClickable(false);
        }
    }

    private void OthersProofsetBtnListenerOrDisable(ImageButton btn,
                                                    OnClickListener mTakePicOnClickListener, String intentName) {
        if (isIntentAvailable(this, intentName)) {
            btn.setOnClickListener(mTakePicOnClickListener);
        } else {
            btn.setTag(getText(R.string.cannot).toString() + " "
                    + btn.getContext());
            btn.setClickable(false);
        }
    }

    private void BankProofsetBtnListenerOrDisable(ImageButton btn,
                                                  OnClickListener mTakePicOnClickListener, String intentName) {
        if (isIntentAvailable(this, intentName)) {
            btn.setOnClickListener(mTakePicOnClickListener);
        } else {
            btn.setTag(getText(R.string.cannot).toString() + " "
                    + btn.getContext());
            btn.setClickable(false);
        }
    }

    private void EFTProofsetBtnListenerOrDisable(ImageButton btn,
                                                 OnClickListener mTakePicOnClickListener, String intentName) {
        if (isIntentAvailable(this, intentName)) {
            btn.setOnClickListener(mTakePicOnClickListener);
        } else {
            btn.setTag(getText(R.string.cannot).toString() + " "
                    + btn.getContext());
            btn.setClickable(false);
        }
    }

    public void fillSpinnerValue(Spinner spinner, List<String> value_list) {

        Element_TextView_BaseAdapter retd_adapter = new Element_TextView_BaseAdapter(
                AdditionalUtilityActivity.this, value_list);
        spinner.setAdapter(retd_adapter);

    }

    // public void initialiseVariable() {
    //
    // spnr_document_upload_document_age = (Spinner)
    // findViewById(R.id.spnr_document_upload_document_age);
    // spnr_document_upload_document_identity = (Spinner)
    // findViewById(R.id.spnr_document_upload_document_identity);
    // spnr_document_upload_document_address = (Spinner)
    // findViewById(R.id.spnr_document_upload_document_address);
    // spnr_document_upload_document_income = (Spinner)
    // findViewById(R.id.spnr_document_upload_document_income);
    //
    // /* Image Button for BrowseButton */
    //
    // img_btn_document_upload_click_browse_age = (ImageButton)
    // findViewById(R.id.img_btn_document_upload_click_browse_age);
    // img_btn_document_upload_click_browse_identity = (ImageButton)
    // findViewById(R.id.img_btn_document_upload_click_browse_identity);
    // img_btn_document_upload_click_browse_address = (ImageButton)
    // findViewById(R.id.img_btn_document_upload_click_browse_address);
    // img_btn_document_upload_click_browse_income = (ImageButton)
    // findViewById(R.id.img_btn_document_upload_click_browse_income);
    // img_btn_document_upload_click_browse_Others = (ImageButton)
    // findViewById(R.id.img_btn_document_upload_click_browse_Others);
    // /* Image Button for CameraButton */
    //
    // img_btn_document_upload_click_image_age = (ImageButton)
    // findViewById(R.id.img_btn_document_upload_click_image_age);
    // img_btn_document_upload_click_image_identity = (ImageButton)
    // findViewById(R.id.img_btn_document_upload_click_image_identity);
    // img_btn_document_upload_click_image_address = (ImageButton)
    // findViewById(R.id.img_btn_document_upload_click_image_address);
    // img_btn_document_upload_click_image_income = (ImageButton)
    // findViewById(R.id.img_btn_document_upload_click_image_income);
    // img_btn_document_upload_click_image_others = (ImageButton)
    // findViewById(R.id.img_btn_document_upload_click_image_others);
    // /* Image Button for Preview Button */
    //
    // img_btn_document_upload_preview_image_age = (ImageButton)
    // findViewById(R.id.img_btn_document_upload_preview_image_age);
    // img_btn_document_upload_preview_image_identity = (ImageButton)
    // findViewById(R.id.img_btn_document_upload_preview_image_identity);
    // img_btn_document_upload_click_preview_image_address = (ImageButton)
    // findViewById(R.id.img_btn_document_upload_click_preview_image_address);
    // img_btn_document_upload_click_preview_image_income = (ImageButton)
    // findViewById(R.id.img_btn_document_upload_click_preview_image_income);
    // img_btn_document_upload_click_preview_image_others = (ImageButton)
    // findViewById(R.id.img_btn_document_upload_click_preview_image_others);
    // getSignature_Proposer = (ImageButton)
    // findViewById(R.id.Ibtn_signature_proposer);
    // getphoto = (ImageButton) findViewById(R.id.proposer_photograph);
    //
    // /* Image Button to delete Document */
    // img_btn_document_delete_age = (ImageButton)
    // findViewById(R.id.img_btn_document_delete_age);
    // img_btn_document_delete_identity = (ImageButton)
    // findViewById(R.id.img_btn_document_delete_identity);
    // img_btn_document_delete_address = (ImageButton)
    // findViewById(R.id.img_btn_document_delete_address);
    // img_btn_document_delete_income = (ImageButton)
    // findViewById(R.id.img_btn_document_delete_income);
    // img_btn_document_delete_Others = (ImageButton)
    // findViewById(R.id.img_btn_document_delete_Others);
    // tr_income_proof = (TableRow) findViewById(R.id.tr_income_proof);
    //
    // edt_document_upload_document_others = (EditText)
    // findViewById(R.id.edt_document_upload_document_others);
    // edt_pay_shortage_proposer_number = (EditText)
    // findViewById(R.id.edt_pay_shortage_proposer_number);
    // edt_pay_shortage_amount = (EditText)
    // findViewById(R.id.edt_pay_shortage_amount);
    // ll_documentStatus = (LinearLayout) findViewById(R.id.ll_documentStatus);
    // spnr_pay_shortage_quation_no = (Spinner)
    // findViewById(R.id.spnr_pay_shortage_quation_no);
    //
    // }

    public void onBrowse_AgeProof(View v) {

        if (!(edt_requirement_Proposer_no.getText().toString()
                .equalsIgnoreCase(""))) {
            // if (!(spnr_document_upload_document_age.getSelectedItem()
            // .toString()).equalsIgnoreCase("Select Document")) {
            uploadFlag = 0;
            img_btn_document_upload_click_image_age
                    .setImageDrawable(getResources().getDrawable(
                            R.drawable.ibtn_camera));
            Check = "AgeProof";

            /*if (spnr_document_upload_document_age.getSelectedItem().toString().toLowerCase().contains("aadhar card")
                    || spnr_document_upload_document_age.getSelectedItem().toString().toLowerCase().contains("pancard")
                    || spnr_document_upload_document_age.getSelectedItem().toString().toLowerCase().contains("passport")
                    || spnr_document_upload_document_age.getSelectedItem().toString().toLowerCase().contains("voter s identity card")
                    || spnr_document_upload_document_age.getSelectedItem().toString().toLowerCase().contains("driving license")) {*/
                Intent intent = new Intent(AdditionalUtilityActivity.this, OcrActivity.class);
                startActivityForResult(intent, REQUEST_OCR);
            /*} else {
            onBrowse();
            }*/
            // } else {
            // upload_Flag = 0;
            // validate_age();
            // }

        } else {
            AlertDialogMessage message = new AlertDialogMessage();
            message.dialog(AdditionalUtilityActivity.this,
                    "Your Proposal Number is not generated!!!", true);

        }

    }

    public void onBrowse_BankProof(View v) {

        if (!(edt_requirement_Proposer_no.getText().toString()
                .equalsIgnoreCase(""))) {

            // if (!(spnr_document_upload_document_bank.getSelectedItem()
            // .toString()).equalsIgnoreCase("Select")) {

            uploadFlag = 0;
            img_btn_document_upload_click_image_bank
                    .setImageDrawable(getResources().getDrawable(
                            R.drawable.ibtn_camera));
            Check = "BankProof";

            /*if (spnr_document_upload_document_bank.getSelectedItem().toString().contains("Cancelled Cheque")) {*/

                //isBrowseCapture = REQUEST_CODE_PICK_FILE;

                Intent intent = new Intent(AdditionalUtilityActivity.this, OcrActivity.class);
                startActivityForResult(intent, REQUEST_OCR);
            /*} else {
            onBrowse();
            }*/

        } else {
            AlertDialogMessage message = new AlertDialogMessage();
            message.dialog(AdditionalUtilityActivity.this,
                    "Your Proposal Number is not generated!!!", true);

        }
    }

    public void onBrowse_IdentityProof(View v) {

        if (!(edt_requirement_Proposer_no.getText().toString()
                .equalsIgnoreCase(""))) {
            // if (!(spnr_document_upload_document_identity.getSelectedItem()
            // .toString()).equalsIgnoreCase("Select Document")) {

            uploadFlag = 0;
            img_btn_document_upload_click_image_identity
                    .setImageDrawable(getResources().getDrawable(
                            R.drawable.ibtn_camera));
            Check = "IdentityProof";

            /*if (spnr_document_upload_document_identity.getSelectedItem().toString().toLowerCase().contains("aadhar card")
                    || spnr_document_upload_document_identity.getSelectedItem().toString().toLowerCase().contains("pancard")
                    || spnr_document_upload_document_identity.getSelectedItem().toString().toLowerCase().contains("passport")
                    || spnr_document_upload_document_identity.getSelectedItem().toString().toLowerCase().contains("voter s identity card")
                    || spnr_document_upload_document_identity.getSelectedItem().toString().toLowerCase().contains("driving license")) {*/
                Intent intent = new Intent(AdditionalUtilityActivity.this, OcrActivity.class);
                startActivityForResult(intent, REQUEST_OCR);
            /*} else {
            onBrowse();
            }*/
            // } else {
            // upload_Flag = 0;
            // validate_identity();
            // }
        } else {
            AlertDialogMessage message = new AlertDialogMessage();
            message.dialog(AdditionalUtilityActivity.this,
                    "Your Proposal Number is not generated!!!", true);

        }

    }

    public void onBrowse_AddressProof(View v) {

        if (!(edt_requirement_Proposer_no.getText().toString()
                .equalsIgnoreCase(""))) {
            // if (!(spnr_document_upload_document_address.getSelectedItem()
            // .toString()).equalsIgnoreCase("Select Document")) {

            uploadFlag = 0;
            img_btn_document_upload_click_image_address
                    .setImageDrawable(getResources().getDrawable(
                            R.drawable.ibtn_camera));
            Check = "AddressProof";

            /*if (spnr_document_upload_document_address.getSelectedItem().toString().toLowerCase().contains("aadhar card")
                    || spnr_document_upload_document_address.getSelectedItem().toString().toLowerCase().contains("pancard")
                    || spnr_document_upload_document_address.getSelectedItem().toString().toLowerCase().contains("passport")
                    || spnr_document_upload_document_address.getSelectedItem().toString().toLowerCase().contains("voter s identity card")
                    || spnr_document_upload_document_address.getSelectedItem().toString().toLowerCase().contains("driving license")) {*/
                Intent intent = new Intent(AdditionalUtilityActivity.this, OcrActivity.class);
                startActivityForResult(intent, REQUEST_OCR);
            /*} else {
            onBrowse();
            }*/
            // } else {
            // upload_Flag = 0;
            // validate_address();
            // }
        } else {
            AlertDialogMessage message = new AlertDialogMessage();
            message.dialog(AdditionalUtilityActivity.this,
                    "Your Proposal Number is not generated!!!", true);

        }

    }

    public void onBrowse_IncomeProof(View v) {

        if (!(edt_requirement_Proposer_no.getText().toString()
                .equalsIgnoreCase(""))) {
            // if (!(spnr_document_upload_document_income.getSelectedItem()
            // .toString()).equalsIgnoreCase("Select Document")) {

            uploadFlag = 0;
            img_btn_document_upload_click_image_income
                    .setImageDrawable(getResources().getDrawable(
                            R.drawable.ibtn_camera));
            Check = "IncomeProof";
            onBrowse();
            // } else {
            // upload_Flag = 0;
            // validate_income();
            // }

        } else {
            AlertDialogMessage message = new AlertDialogMessage();
            message.dialog(AdditionalUtilityActivity.this,
                    "Your Proposal Number is not generated!!!", true);

        }

    }

    public void onBrowse_OtherProof(View v) {

        if (!(edt_requirement_Proposer_no.getText().toString()
                .equalsIgnoreCase(""))) {
            // if (!(edt_document_upload_document_others.getText().toString())
            // .equalsIgnoreCase("")) {

            uploadFlag = 0;
            img_btn_document_upload_click_image_others
                    .setImageDrawable(getResources().getDrawable(
                            R.drawable.ibtn_camera));
            Check = "OtherProof";
            onBrowse();
            // } else {
            // upload_Flag = 0;
            // validate_others();
            // }

        } else {
            AlertDialogMessage message = new AlertDialogMessage();
            message.dialog(AdditionalUtilityActivity.this,
                    "Your Proposal Number is not generated!!!", true);
        }

    }

    public void onBrowse_eftProof(View v) {

        if (!(edt_requirement_Proposer_no.getText().toString()
                .equalsIgnoreCase(""))) {
            // if (!(spnr_document_upload_document_eft.getSelectedItem()
            // .toString().equalsIgnoreCase("Select"))) {

            uploadFlag = 0;
            img_btn_document_upload_click_image_eft
                    .setImageDrawable(getResources().getDrawable(
                            R.drawable.ibtn_camera));
            Check = "EFTProof";
            onBrowse();
            // } else {
            // upload_Flag = 0;
            // validate_eft();
            // }

        } else {
            AlertDialogMessage message = new AlertDialogMessage();
            message.dialog(AdditionalUtilityActivity.this,
                    "Your Proposal Number is not generated!!!", true);

        }

    }


    public void onPreview_ageProof(View v) {

		if (AgeProofFileName.exists()){
            try{
                mCommonMethods.openAllDocs(context, AgeProofFileName);
            }catch (IOException e){
                mCommonMethods.showToast(context, e.getMessage());
		}
        }
    }

    public void onPreview_BankProof(View v) {

        if (BankProofFileName.exists()){
            try{
                mCommonMethods.openAllDocs(context, BankProofFileName);
            }catch (IOException e){
                mCommonMethods.showToast(context, e.getMessage());
		}
        }
    }

    public void onPreview_eftProof(View v) {

        if (EFTProofFileName.exists()){
            try{
                mCommonMethods.openAllDocs(context, EFTProofFileName);
            }catch (IOException e){
                mCommonMethods.showToast(context, e.getMessage());
		}
        }
    }

    public void onPreview_IdentityProof(View v) {

        if (IdentityProofFileName.exists()){
            try{
                mCommonMethods.openAllDocs(context, IdentityProofFileName);
            }catch (IOException e){
                mCommonMethods.showToast(context, e.getMessage());
		}
        }
    }

    public void onPreview_AddressProof(View v) {

        if (AddressProofFileName.exists()){
            try{
                mCommonMethods.openAllDocs(context, AddressProofFileName);
            }catch (IOException e){
                mCommonMethods.showToast(context, e.getMessage());
		}
        }
    }

    public void onPreview_IncomeProof(View v) {

        if (IncomeProofFileName.exists()){
            try{
                mCommonMethods.openAllDocs(context, IncomeProofFileName);
            }catch (IOException e){
                mCommonMethods.showToast(context, e.getMessage());
		}
        }
    }

    public void onPreview_OthersProof(View v) {

        if (OtherProofFileName.exists()){
            try{
                mCommonMethods.openAllDocs(context, OtherProofFileName);
            }catch (IOException e){
                mCommonMethods.showToast(context, e.getMessage());
		}
        }
    }

    public void onDelete_ageProof(View v) {
        deleteAgeDocument = 1;
        Builder builder = new Builder(this,
                AlertDialog.THEME_HOLO_LIGHT);
        builder.setTitle("Confirmation");
        builder.setMessage("do you want to delete this document ?");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", new OkOnClickListener());
        builder.setNegativeButton("No", new CancelOnClickListener());
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    public void onDelete_identityProof(View v) {
        deleteIdentityDocument = 1;
        Builder builder = new Builder(this,
                AlertDialog.THEME_HOLO_LIGHT);
        builder.setTitle("Confirmation");
        builder.setMessage("do you want to delete this document ?");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", new OkOnClickListener());
        builder.setNegativeButton("No", new CancelOnClickListener());
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    public void onDelete_BankProof(View v) {
        deleteBankDocument = 1;
        Builder builder = new Builder(this,
                AlertDialog.THEME_HOLO_LIGHT);
        builder.setTitle("Confirmation");
        builder.setMessage("do you want to delete this document ?");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", new OkOnClickListener());
        builder.setNegativeButton("No", new CancelOnClickListener());
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void onDelete_eftProof(View v) {
        deleteEFTDocument = 1;
        Builder builder = new Builder(this,
                AlertDialog.THEME_HOLO_LIGHT);
        builder.setTitle("Confirmation");
        builder.setMessage("do you want to delete this document ?");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", new OkOnClickListener());
        builder.setNegativeButton("No", new CancelOnClickListener());
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    public void onDelete_addressProof(View v) {
        deleteAddressDocument = 1;
        Builder builder = new Builder(this,
                AlertDialog.THEME_HOLO_LIGHT);
        builder.setTitle("Confirmation");
        builder.setMessage("do you want to delete this document ?");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", new OkOnClickListener());
        builder.setNegativeButton("No", new CancelOnClickListener());
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    public void onDelete_incomeProof(View v) {
        deleteIncomeDocument = 1;
        Builder builder = new Builder(this,
                AlertDialog.THEME_HOLO_LIGHT);
        builder.setTitle("Confirmation");
        builder.setMessage("do you want to delete this document ?");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", new OkOnClickListener());
        builder.setNegativeButton("No", new CancelOnClickListener());
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    public void onDelete_othersProof(View v) {
        deleteOtherDocument = 1;
        Builder builder = new Builder(this,
                AlertDialog.THEME_HOLO_LIGHT);
        builder.setTitle("Confirmation");
        builder.setMessage("do you want to delete this document ?");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", new OkOnClickListener());
        builder.setNegativeButton("No", new CancelOnClickListener());
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    public void onBrowse() {
        // final String LOGTAG = "DocumentsUploadActivity";
        // Log.d(LOGTAG, "StartFileBrowser4File button pressed");
        // Intent fileExploreIntent = new Intent(
        // CustomerFileBrowserActivity.INTENT_ACTION_SELECT_FILE, null,
        // activityForButton, CustomerFileBrowserActivity.class);
        //
        // startActivityForResult(fileExploreIntent, REQUEST_CODE_PICK_FILE);

        Intent i = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, REQUEST_CODE_BROWSE_FILE);

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_BROWSE_FILE) {
            if (resultCode == AppCompatActivity.RESULT_OK) {

                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaColumns.DATA};

                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String strPathOfFile = cursor.getString(columnIndex);
                cursor.close();

                if (Check.equals("AgeProof")) {
                    AgeProofFileName = new File(strPathOfFile);

					/*ageProofBitmap = mCommonMethods.ShrinkBitmap(
							AgeProofFileName.getAbsolutePath(), 600, 600);*/
                    //image compression by bhalla
                    CompressImage.compressImage(AgeProofFileName.getAbsolutePath());

                    //ReducedAgeBitmap = ageProofBitmap;

                    img_btn_document_upload_preview_image_age
                            .setVisibility(View.VISIBLE);
                    img_btn_document_delete_age.setVisibility(View.VISIBLE);

                    img_btn_document_upload_click_browse_age
                            .setImageDrawable(getResources().getDrawable(
                                    R.drawable.checkedbrowse));

					/*Intent intent = new Intent(AdditionalUtilityActivity.this,
							SelfAttestedDocumentActivity.class);
					intent.putExtra("FLAG", "FALSE");
					intent.putExtra("ProposerSign", proposerSign);
					intent.putExtra("PlanName", planName);
					startActivityForResult(intent, CAPTURE_ACTIVITY);*/
                    // showDialog(DIALOG_ALERT);

                } else if (Check.equals("EFTProof")) {
                    EFTProofFileName = new File(strPathOfFile);

                    //image compression by bhalla
                    CompressImage.compressImage(EFTProofFileName.getAbsolutePath());

					/*eftProofBitmap = mCommonMethods.ShrinkBitmap(
							EFTProofFileName.getAbsolutePath(), 600, 600);*/

                    //ReducedEFTBitmap = eftProofBitmap;

                    img_btn_document_upload_preview_image_eft
                            .setVisibility(View.VISIBLE);
                    img_btn_document_delete_eft.setVisibility(View.VISIBLE);
                    img_btn_document_upload_click_browse_eft
                            .setImageDrawable(getResources().getDrawable(
                                    R.drawable.checkedbrowse));

					/*Intent intent = new Intent(AdditionalUtilityActivity.this,
							SelfAttestedDocumentActivity.class);
					intent.putExtra("FLAG", "FALSE");
					intent.putExtra("ProposerSign", proposerSign);
					intent.putExtra("PlanName", planName);
					startActivityForResult(intent, CAPTURE_ACTIVITY);*/
                    // showDialog(DIALOG_ALERT);

                } else if (Check.equals("IdentityProof")) {
                    IdentityProofFileName = new File(strPathOfFile);

                    //image compression by bhalla
                    CompressImage.compressImage(IdentityProofFileName.getAbsolutePath());

					/*identityProofBitmap = mCommonMethods.ShrinkBitmap(
							IdentityProofFileName.getAbsolutePath(), 600, 600);*/

                    //ReducedIdentityBitmap = identityProofBitmap;

                    img_btn_document_upload_preview_image_identity
                            .setVisibility(View.VISIBLE);
                    img_btn_document_delete_identity
                            .setVisibility(View.VISIBLE);

                    img_btn_document_upload_click_browse_identity
                            .setImageDrawable(getResources().getDrawable(
                                    R.drawable.checkedbrowse));

					/*Intent intent = new Intent(AdditionalUtilityActivity.this,
							SelfAttestedDocumentActivity.class);
					intent.putExtra("FLAG", "FALSE");
					intent.putExtra("ProposerSign", proposerSign);
					intent.putExtra("PlanName", planName);
					startActivityForResult(intent, CAPTURE_ACTIVITY);*/
                    // showDialog(DIALOG_ALERT);
                } else if (Check.equals("AddressProof")) {
                    AddressProofFileName = new File(strPathOfFile);
                    //image compression by bhalla
                    CompressImage.compressImage(AddressProofFileName.getAbsolutePath());

					/*addressProofBitmap = mCommonMethods.ShrinkBitmap(
							AddressProofFileName.getAbsolutePath(), 600, 600);*/

                    //ReducedAddressBitmap = addressProofBitmap;

                    img_btn_document_upload_click_preview_image_address
                            .setVisibility(View.VISIBLE);
                    img_btn_document_delete_address.setVisibility(View.VISIBLE);
                    img_btn_document_upload_click_browse_address
                            .setImageDrawable(getResources().getDrawable(
                                    R.drawable.checkedbrowse));
					/*Intent intent = new Intent(AdditionalUtilityActivity.this,
							SelfAttestedDocumentActivity.class);
					intent.putExtra("FLAG", "FALSE");
					intent.putExtra("ProposerSign", proposerSign);
					intent.putExtra("PlanName", planName);
					startActivityForResult(intent, CAPTURE_ACTIVITY);*/
                    // showDialog(DIALOG_ALERT);

                } else if (Check.equals("IncomeProof")) {
                    IncomeProofFileName = new File(strPathOfFile);

                    //image compression by bhalla
                    CompressImage.compressImage(IncomeProofFileName.getAbsolutePath());

					/*incomeProofBitmap = mCommonMethods.ShrinkBitmap(
							IncomeProofFileName.getAbsolutePath(), 600, 600);*/

                    //ReducedIncomeBitmap = incomeProofBitmap;

                    img_btn_document_upload_click_preview_image_income
                            .setVisibility(View.VISIBLE);
                    img_btn_document_delete_income.setVisibility(View.VISIBLE);
                    img_btn_document_upload_click_browse_income
                            .setImageDrawable(getResources().getDrawable(
                                    R.drawable.checkedbrowse));
					/*Intent intent = new Intent(AdditionalUtilityActivity.this,
							SelfAttestedDocumentActivity.class);
					intent.putExtra("FLAG", "FALSE");
					intent.putExtra("ProposerSign", proposerSign);
					intent.putExtra("PlanName", planName);
					startActivityForResult(intent, CAPTURE_ACTIVITY);*/
                    // showDialog(DIALOG_ALERT);

                } else if (Check.equals("BankProof")) {
                    BankProofFileName = new File(strPathOfFile);

                    //image compression by bhalla
                    CompressImage.compressImage(BankProofFileName.getAbsolutePath());

					/*bankProofBitmap = mCommonMethods.ShrinkBitmap(
							BankProofFileName.getAbsolutePath(), 600, 600);*/

                    //ReducedBankBitmap = bankProofBitmap;

                    img_btn_document_upload_click_preview_image_bank
                            .setVisibility(View.VISIBLE);
                    img_btn_document_delete_bank.setVisibility(View.VISIBLE);
                    img_btn_document_upload_click_browse_bank
                            .setImageDrawable(getResources().getDrawable(
                                    R.drawable.checkedbrowse));
					/*Intent intent = new Intent(AdditionalUtilityActivity.this,
							SelfAttestedDocumentActivity.class);
					intent.putExtra("FLAG", "FALSE");
					intent.putExtra("ProposerSign", proposerSign);
					intent.putExtra("PlanName", planName);
					startActivityForResult(intent, CAPTURE_ACTIVITY);*/
                    // showDialog(DIALOG_ALERT);

                } else if (Check.equals("OtherProof")) {
                    OtherProofFileName = new File(strPathOfFile);

                    //image compression by bhalla
                    CompressImage.compressImage(OtherProofFileName.getAbsolutePath());

					/*otherProofBitmap = mCommonMethods.ShrinkBitmap(
							OtherProofFileName.getAbsolutePath(), 600, 600);*/

                    //ReducedOtherBitmap = otherProofBitmap;

                    img_btn_document_upload_click_preview_image_others
                            .setVisibility(View.VISIBLE);
                    img_btn_document_delete_Others.setVisibility(View.VISIBLE);
                    img_btn_document_upload_click_browse_Others
                            .setImageDrawable(getResources().getDrawable(
                                    R.drawable.checkedbrowse));
					/*Intent intent = new Intent(AdditionalUtilityActivity.this,
							SelfAttestedDocumentActivity.class);
					intent.putExtra("FLAG", "FALSE");
					intent.putExtra("ProposerSign", proposerSign);
					intent.putExtra("PlanName", planName);
					startActivityForResult(intent, CAPTURE_ACTIVITY);*/
                    // showDialog(DIALOG_ALERT);

                } else if (Check.equals("Photo")) {
                    Photo = new File(strPathOfFile);

                    //image compression by bhalla
                    Bitmap bmp = BitmapFactory.decodeFile(CompressImage.compressImage(Photo.getAbsolutePath()));

                    ImageButton button1 = findViewById(R.id.proposer_photograph);
                    Bitmap scaled = Bitmap.createScaledBitmap(bmp, 230, 200,
                            true);
                    button1.setImageBitmap(scaled);

                }
            }
            //super.onActivityResult(requestCode, resultCode, data);
        } else if (requestCode == REQUEST_CODE_CAPTURE_FILE) {
            if (resultCode == RESULT_OK) {

                if (Check.equals("AgeProof")) {

                    AgeProofFileName = f;

                    //image compression by bhalla
                    CompressImage.compressImage(AgeProofFileName.getAbsolutePath());

					/*ageProofBitmap = mCommonMethods.ShrinkBitmap(
							AgeProofFileName.getAbsolutePath(), 600, 600);*/

                    //ReducedAgeBitmap = ageProofBitmap;

                    img_btn_document_upload_preview_image_age
                            .setVisibility(View.VISIBLE);
                    img_btn_document_delete_age.setVisibility(View.VISIBLE);
                    img_btn_document_upload_click_image_age
                            .setImageDrawable(getResources().getDrawable(
                                    R.drawable.checkedcamera));
					/*Intent intent = new Intent(AdditionalUtilityActivity.this,
							SelfAttestedDocumentActivity.class);
					intent.putExtra("FLAG", "FALSE");
					intent.putExtra("ProposerSign", proposerSign);
					intent.putExtra("PlanName", planName);
					startActivityForResult(intent, CAPTURE_ACTIVITY);*/
                    // showDialog(DIALOG_ALERT);

                } else if (Check.equals("IdentityProof")) {
                    IdentityProofFileName = f;

                    //image compression by bhalla
                    CompressImage.compressImage(IdentityProofFileName.getAbsolutePath());

					/*identityProofBitmap = mCommonMethods.ShrinkBitmap(
							IdentityProofFileName.getAbsolutePath(), 600, 600);*/

                    //ReducedIdentityBitmap = identityProofBitmap;

                    img_btn_document_upload_preview_image_identity
                            .setVisibility(View.VISIBLE);
                    img_btn_document_delete_identity
                            .setVisibility(View.VISIBLE);
                    img_btn_document_upload_click_image_identity
                            .setImageDrawable(getResources().getDrawable(
                                    R.drawable.checkedcamera));
					/*Intent intent = new Intent(AdditionalUtilityActivity.this,
							SelfAttestedDocumentActivity.class);
					intent.putExtra("FLAG", "FALSE");
					intent.putExtra("ProposerSign", proposerSign);
					intent.putExtra("PlanName", planName);
					startActivityForResult(intent, CAPTURE_ACTIVITY);*/
                    // showDialog(DIALOG_ALERT);

                } else if (Check.equals("AddressProof")) {
                    AddressProofFileName = f;

                    //image compression by bhalla
                    CompressImage.compressImage(AddressProofFileName.getAbsolutePath());

					/*addressProofBitmap = mCommonMethods.ShrinkBitmap(
							AddressProofFileName.getAbsolutePath(), 600, 600);*/

                    //ReducedAddressBitmap = addressProofBitmap;

                    img_btn_document_upload_click_preview_image_address
                            .setVisibility(View.VISIBLE);
                    img_btn_document_delete_address.setVisibility(View.VISIBLE);
                    img_btn_document_upload_click_image_address
                            .setImageDrawable(getResources().getDrawable(
                                    R.drawable.checkedcamera));
					/*Intent intent = new Intent(AdditionalUtilityActivity.this,
							SelfAttestedDocumentActivity.class);
					intent.putExtra("FLAG", "FALSE");
					intent.putExtra("ProposerSign", proposerSign);
					intent.putExtra("PlanName", planName);
					startActivityForResult(intent, CAPTURE_ACTIVITY);*/
                    // showDialog(DIALOG_ALERT);

                } else if (Check.equals("IncomeProof")) {
                    IncomeProofFileName = f;

                    //image compression by bhalla
                    CompressImage.compressImage(IncomeProofFileName.getAbsolutePath());

					/*incomeProofBitmap = mCommonMethods.ShrinkBitmap(
							IncomeProofFileName.getAbsolutePath(), 600, 600);*/

                    //ReducedIncomeBitmap = incomeProofBitmap;

                    img_btn_document_upload_click_preview_image_income
                            .setVisibility(View.VISIBLE);
                    img_btn_document_delete_income.setVisibility(View.VISIBLE);
                    img_btn_document_upload_click_image_income
                            .setImageDrawable(getResources().getDrawable(
                                    R.drawable.checkedcamera));
					/*Intent intent = new Intent(AdditionalUtilityActivity.this,
							SelfAttestedDocumentActivity.class);
					intent.putExtra("FLAG", "FALSE");
					intent.putExtra("ProposerSign", proposerSign);
					intent.putExtra("PlanName", planName);
					startActivityForResult(intent, CAPTURE_ACTIVITY);*/
                    // showDialog(DIALOG_ALERT);

                } else if (Check.equals("BankProof")) {
                    BankProofFileName = f;

                    //image compression by bhalla
                    CompressImage.compressImage(BankProofFileName.getAbsolutePath());

					/*bankProofBitmap = mCommonMethods.ShrinkBitmap(
							BankProofFileName.getAbsolutePath(), 600, 600);*/

                    //ReducedBankBitmap = bankProofBitmap;

                    img_btn_document_upload_click_preview_image_bank
                            .setVisibility(View.VISIBLE);
                    img_btn_document_delete_bank.setVisibility(View.VISIBLE);
                    img_btn_document_upload_click_image_bank
                            .setImageDrawable(getResources().getDrawable(
                                    R.drawable.checkedcamera));
					/*Intent intent = new Intent(AdditionalUtilityActivity.this,
							SelfAttestedDocumentActivity.class);
					intent.putExtra("FLAG", "FALSE");
					intent.putExtra("ProposerSign", proposerSign);
					intent.putExtra("PlanName", planName);
					startActivityForResult(intent, CAPTURE_ACTIVITY);*/
                    // showDialog(DIALOG_ALERT);

                } else if (Check.equals("EFTProof")) {

                    EFTProofFileName = f;

                    //image compression by bhalla
                    CompressImage.compressImage(EFTProofFileName.getAbsolutePath());

					/*eftProofBitmap = mCommonMethods.ShrinkBitmap(
							EFTProofFileName.getAbsolutePath(), 600, 600);*/

                    //ReducedEFTBitmap = eftProofBitmap;

                    img_btn_document_upload_preview_image_eft
                            .setVisibility(View.VISIBLE);
                    img_btn_document_delete_eft.setVisibility(View.VISIBLE);
                    img_btn_document_upload_click_image_eft
                            .setImageDrawable(getResources().getDrawable(
                                    R.drawable.checkedcamera));
					/*Intent intent = new Intent(AdditionalUtilityActivity.this,
							SelfAttestedDocumentActivity.class);
					intent.putExtra("FLAG", "FALSE");
					intent.putExtra("ProposerSign", proposerSign);
					intent.putExtra("PlanName", planName);
					startActivityForResult(intent, CAPTURE_ACTIVITY);*/
                    // showDialog(DIALOG_ALERT);

                } else if (Check.equals("OtherProof")) {
                    OtherProofFileName = f;

                    //image compression by bhalla
                    CompressImage.compressImage(OtherProofFileName.getAbsolutePath());

					/*otherProofBitmap = mCommonMethods.ShrinkBitmap(
							OtherProofFileName.getAbsolutePath(), 600, 600);*/

                    //ReducedOtherBitmap = otherProofBitmap;\

                    img_btn_document_upload_click_preview_image_others
                            .setVisibility(View.VISIBLE);
                    img_btn_document_delete_Others.setVisibility(View.VISIBLE);
                    img_btn_document_upload_click_image_others
                            .setImageDrawable(getResources().getDrawable(
                                    R.drawable.checkedcamera));
					/*Intent intent = new Intent(AdditionalUtilityActivity.this,
							SelfAttestedDocumentActivity.class);
					intent.putExtra("FLAG", "FALSE");
					intent.putExtra("ProposerSign", proposerSign);
					intent.putExtra("PlanName", planName);
					startActivityForResult(intent, CAPTURE_ACTIVITY);*/
                    // showDialog(DIALOG_ALERT);

                } else if (Check.equals("Photo")) {
                    Photo = f;

                    //image compression by bhalla
                    ReducedPhotoBitmap = BitmapFactory.decodeFile(CompressImage.compressImage(Photo.getAbsolutePath()));

                    /*ReducedPhotoBitmap = decodeFile(Photo);*/
                    // Bitmap bmp = BitmapFactory.decodeFile(Photo
                    // .getAbsolutePath());
                    ImageButton button1 = findViewById(R.id.proposer_photograph);
                    Bitmap scaled = Bitmap.createScaledBitmap(
                            ReducedPhotoBitmap, 230, 200, true);
                    button1.setImageBitmap(scaled);
                    d.dismiss();
                }

            }

        } else if (requestCode == REQUEST_OCR) {
            if (resultCode == RESULT_OK) {
                File imagePath = null;
                String DocumentType = "";
                Bundle bundle = data.getExtras();
                if (bundle != null) {
                    String jsonData = (String) bundle.get("jsonData");

                    try {
                        JSONObject object = new JSONObject(jsonData);

                        DocumentType = object.get("DocumentType").toString();

                        imagePath = new File(bundle.get("BitmapImageUri").toString());
                        //Bitmap edgeBitmap = BitmapFactory.decodeFile(imagePath.getAbsolutePath());

                    } catch (Exception e) {
                        e.printStackTrace();
                        /*imagePath = (File) bundle.get("BitmapImageUri");
                        Bitmap edgeBitmap = BitmapFactory.decodeFile(imagePath.getAbsolutePath());*/
                    }
                }

                File destinationFile = null;
                try {
                    if (Check.equals("AgeProof")) {
                        increment = 1;
                        //image compression by bhalla
                        CompressImage.compressImage(imagePath.getAbsolutePath());
                        //copy  FILE
                        //mCommonMethods.copyFile(new FileInputStream(imagePath), new FileOutputStream(destinationFile));
                        String imageFileName = JPEG_FILE_PREFIX + ProposalNumber + "_X0"
                                + increment + "_" + JPEG_FILE_SUFFIX;

                        AgeProofFileName = mStorageUtils.saveFileToAppSpecificDir(context, StorageUtils.DIRECT_DIRECTORY,
                                imageFileName, imagePath);

                        boolean is_Doc_Detected = true;
                        if (spnr_document_upload_document_age.getSelectedItem().toString().toLowerCase().contains("aadhar card") ||
                                spnr_document_upload_document_age.getSelectedItem().toString().toLowerCase().contains("pancard") ||
                                spnr_document_upload_document_age.getSelectedItem().toString().toLowerCase().contains("passport") ||
                                spnr_document_upload_document_age.getSelectedItem().toString().toLowerCase().contains("voter s identity card") ||
                                spnr_document_upload_document_age.getSelectedItem().toString().toLowerCase().contains("driving license")) {
                            if (spnr_document_upload_document_age.getSelectedItem().toString().toLowerCase().contains("aadhar") && (DocumentType.equalsIgnoreCase("AADHAAR CARD") || DocumentType.equalsIgnoreCase("BACK AADHAAR CARD") || DocumentType.equalsIgnoreCase("FRONT BACK AADHAAR"))) {
                                is_Doc_Detected = true;
                            } else if (spnr_document_upload_document_age.getSelectedItem().toString().toLowerCase().contains("pancard") && DocumentType.equalsIgnoreCase("PAN")) {
                                is_Doc_Detected = true;
                            } else if (spnr_document_upload_document_age.getSelectedItem().toString().toLowerCase().contains("passport") && (DocumentType.equalsIgnoreCase("PASSPORT") || DocumentType.equalsIgnoreCase("PASSPORT BACK") || DocumentType.equalsIgnoreCase("FRONT BACK PASSPORT"))) {
                                is_Doc_Detected = true;
                            } else if (spnr_document_upload_document_age.getSelectedItem().toString().toLowerCase().contains("voter s identity card") && (DocumentType.equalsIgnoreCase("VOTER ID") || DocumentType.equalsIgnoreCase("BACK VOTER ID") || DocumentType.equalsIgnoreCase("FRONT BACK VOTER"))) {
                                is_Doc_Detected = true;
                            } else if (spnr_document_upload_document_age.getSelectedItem().toString().toLowerCase().contains("driving license") && (DocumentType.equalsIgnoreCase("DRIVING LICENCE") || DocumentType.equalsIgnoreCase("BACK DRIVING LICENCE") || DocumentType.equalsIgnoreCase("FRONT BACK DRIVING LICENCE"))) {
                                is_Doc_Detected = true;
                            } else {
                                is_Doc_Detected = false;
                            }
                        } else {
                            is_Doc_Detected = true;
//                            Toast.makeText(context, "Invalid Document", Toast.LENGTH_SHORT).show();
                        }

                        if (is_Doc_Detected) {

                            //ReducedAgeBitmap = ageProofBitmap;

                            // String ageProofPhoto =
                            // bitmapToString(ageProofBitmap);
                            // SelfAttestedDocumentActivity.lst_AgeBitmap.add(ageProofPhoto);

                            img_btn_document_upload_preview_image_age
                                    .setVisibility(View.VISIBLE);
                            img_btn_document_delete_age.setVisibility(View.VISIBLE);
                            img_btn_document_upload_click_image_age.setImageDrawable(
                                    getResources().getDrawable(R.drawable.checkedcamera));

                            // getAndSetPersonalDetail();
                            //CheckedsameDoc();

                            /*if (isoneSide == false) {
                            DeleteOldFile();
                                *//**//*Commented on 22-07-2018*//**//*
                            Intent intent = new Intent(MandDocumentActivity.this,
                                    SelfAttestedDocumentActivity.class);
                            intent.putExtra("FLAG", "MandDocumentActivity");
                            intent.putExtra("ProposerSign", proposerSign);
                            intent.putExtra("PlanName", planName);
                            startActivity(intent);
                            showDialog(DIALOG_ALERT);
                        }*/

                        } else {
                            img_btn_document_upload_preview_image_age
                                    .setVisibility(View.INVISIBLE);
                            img_btn_document_delete_age
                                    .setVisibility(View.INVISIBLE);

                            img_btn_document_upload_click_image_age
                                    .setImageDrawable(getResources().getDrawable(
                                            R.drawable.ibtn_camera));

                            if (AgeProofFileName.exists())
                                AgeProofFileName.delete();

                            Toast.makeText(this, "Document is not properly detected,Kindly capture the" + spnr_document_upload_document_age.getSelectedItem().toString() + " document. ", Toast.LENGTH_LONG)
                                    .show();
                        }

                    /*if (imagePath.exists()) {
                        imagePath.delete();
                        }*/
                    }
                    else if (Check.equals("IdentityProof")) {
                        increment = 2;

                        //image compression by bhalla
                        CompressImage.compressImage(imagePath.getAbsolutePath());

                        String imageFileName = JPEG_FILE_PREFIX + ProposalNumber + "_X0"
                                + increment + "_" + JPEG_FILE_SUFFIX;
                        IdentityProofFileName = mStorageUtils.saveFileToAppSpecificDir(context, StorageUtils.DIRECT_DIRECTORY,
                                imageFileName, imagePath);

                        // boolean isBlurr = Blurr(identityProofBitmap);
                       /* if (Proposer_IdentityProof.equalsIgnoreCase("Pancard") || Proposer_IdentityProof.contains("Aadhar card") || Proposer_IdentityProof.contains("Aadhar Card")) {
                        is_Doc_Detected = OCR(identityProofBitmap, Proposer_IdentityProof, "IDENTITY");
                    }*/

                        boolean is_Doc_Detected = true;

                        if (spnr_document_upload_document_identity.getSelectedItem().toString().toLowerCase().contains("aadhar card")
                                || spnr_document_upload_document_identity.getSelectedItem().toString().toLowerCase().contains("pancard")
                                || spnr_document_upload_document_identity.getSelectedItem().toString().toLowerCase().contains("passport")
                                || spnr_document_upload_document_identity.getSelectedItem().toString().toLowerCase().contains("voter s identity card")
                                || spnr_document_upload_document_identity.getSelectedItem().toString().toLowerCase().contains("driving license")) {
                            if (spnr_document_upload_document_identity.getSelectedItem().toString().toLowerCase().contains("aadhar") && (spnr_document_upload_document_identity.getSelectedItem().toString().toLowerCase().contains("aadhar card")
                                    || DocumentType.equalsIgnoreCase("BACK AADHAAR CARD") || DocumentType.equalsIgnoreCase("FRONT BACK AADHAAR"))) {
                                is_Doc_Detected = true;
                            } else if (spnr_document_upload_document_identity.getSelectedItem().toString().toLowerCase().contains("pancard") && DocumentType.equalsIgnoreCase("PAN")) {
                                is_Doc_Detected = true;
                            } else if (spnr_document_upload_document_identity.getSelectedItem().toString().toLowerCase().contains("passport") && (DocumentType.equalsIgnoreCase("PASSPORT") || DocumentType.equalsIgnoreCase("PASSPORT BACK") || DocumentType.equalsIgnoreCase("FRONT BACK PASSPORT"))) {
                                is_Doc_Detected = true;
                            } else if (spnr_document_upload_document_identity.getSelectedItem().toString().toLowerCase().contains("voter s identity card") && (DocumentType.equalsIgnoreCase("VOTER ID") || DocumentType.equalsIgnoreCase("BACK VOTER ID") || DocumentType.equalsIgnoreCase("FRONT BACK VOTER"))) {
                                is_Doc_Detected = true;
                            } else if (spnr_document_upload_document_identity.getSelectedItem().toString().toLowerCase().contains("driving license") && (DocumentType.equalsIgnoreCase("DRIVING LICENCE") || DocumentType.equalsIgnoreCase("BACK DRIVING LICENCE") || DocumentType.equalsIgnoreCase("FRONT BACK DRIVING LICENCE"))) {
                                is_Doc_Detected = true;
                            } else {
                                is_Doc_Detected = false;
                            }

                        } else {
                            is_Doc_Detected = true;
                        }


                   /* if (Proposer_IdentityProof.equalsIgnoreCase("Pancard") || Proposer_IdentityProof.contains("Aadhar card") || Proposer_IdentityProof.contains("Aadhar Card")) {
                        is_Doc_Detected = OCR(identityProofBitmap, Proposer_IdentityProof, "IDENTITY");
                    }*/
                        if (is_Doc_Detected) {

                            //ReducedIdentityBitmap = identityProofBitmap;

                            // String identityProofPhoto =
                            // bitmapToString(identityProofBitmap);
                            // SelfAttestedDocumentActivity.lst_IdentityBitmap.add(identityProofPhoto);

                            img_btn_document_upload_preview_image_identity
                                    .setVisibility(View.VISIBLE);
                            img_btn_document_delete_identity
                                    .setVisibility(View.VISIBLE);
                            img_btn_document_upload_click_image_identity
                                    .setImageDrawable(getResources().getDrawable(
                                            R.drawable.checkedcamera));

                            // getAndSetPersonalDetail();
                            //CheckedsameDoc();
                       /* if(IdentityProofFileName.exists()){
                            IdentityProofFileName.delete();
                        }*/

                            /*if (isoneSide == false) {
                        DeleteOldFile();
                        Intent intent = new Intent(MandDocumentActivity.this,
                                SelfAttestedDocumentActivity.class);
                        intent.putExtra("FLAG", "MandDocumentActivity");
                        intent.putExtra("ProposerSign", proposerSign);
                        intent.putExtra("PlanName", planName);
                        startActivity(intent);
                        showDialog(DIALOG_ALERT);
                    }*/
                        } else {
                            img_btn_document_upload_preview_image_identity
                                    .setVisibility(View.INVISIBLE);
                            img_btn_document_delete_identity
                                    .setVisibility(View.INVISIBLE);

                            img_btn_document_upload_click_image_identity
                                    .setImageDrawable(getResources().getDrawable(
                                            R.drawable.ibtn_camera));

                            if (IdentityProofFileName.exists())
                                IdentityProofFileName.delete();

                            Toast.makeText(this, "Document is not properly detected,Kindly capture the " + spnr_document_upload_document_identity.getSelectedItem().toString() + " document. ", Toast.LENGTH_LONG)
                                    .show();

                        }
                    }
                    else if (Check.equals("AddressProof")) {
                        increment = 3;

                        //image compression by bhalla
                        CompressImage.compressImage(imagePath.getAbsolutePath());
                        String imageFileName = JPEG_FILE_PREFIX + ProposalNumber + "_X0"
                                + increment + "_" + JPEG_FILE_SUFFIX;
                        AddressProofFileName = mStorageUtils.saveFileToAppSpecificDir(context, StorageUtils.DIRECT_DIRECTORY,
                                imageFileName, imagePath);

                            /*if (proposer_IsUnder_NRI.equalsIgnoreCase("NRI")) {
                        increment = 3;
                    } else {
                        increment = 10;
                    }*/

                        //  boolean isBlurr = Blurr(addressProofBitmap);
                        boolean is_Doc_Detected = true;
                        if (spnr_document_upload_document_address.getSelectedItem().toString().toLowerCase().contains("aadhar card")
                                || spnr_document_upload_document_address.getSelectedItem().toString().toLowerCase().contains("pancard")
                                || spnr_document_upload_document_address.getSelectedItem().toString().toLowerCase().contains("passport")
                                || spnr_document_upload_document_address.getSelectedItem().toString().toLowerCase().contains("voter s identity card")
                                || spnr_document_upload_document_address.getSelectedItem().toString().toLowerCase().contains("driving license")) {
                            if (spnr_document_upload_document_address.getSelectedItem().toString().toLowerCase().contains("aadhar") && (DocumentType.equalsIgnoreCase("AADHAAR CARD") || DocumentType.equalsIgnoreCase("BACK AADHAAR CARD") || DocumentType.equalsIgnoreCase("FRONT BACK AADHAAR"))) {
                                is_Doc_Detected = true;
                            } else if (spnr_document_upload_document_address.getSelectedItem().toString().toLowerCase().contains("pancard") && DocumentType.equalsIgnoreCase("PAN")) {
                                is_Doc_Detected = true;
                            } else if (spnr_document_upload_document_address.getSelectedItem().toString().toLowerCase().contains("passport") && (DocumentType.equalsIgnoreCase("PASSPORT") || DocumentType.equalsIgnoreCase("PASSPORT BACK") || DocumentType.equalsIgnoreCase("FRONT BACK PASSPORT"))) {
                                is_Doc_Detected = true;
                            } else if (spnr_document_upload_document_address.getSelectedItem().toString().toLowerCase().contains("voter s identity card") && (DocumentType.equalsIgnoreCase("VOTER ID") || DocumentType.equalsIgnoreCase("BACK VOTER ID") || DocumentType.equalsIgnoreCase("FRONT BACK VOTER"))) {
                                is_Doc_Detected = true;
                            } else if (spnr_document_upload_document_address.getSelectedItem().toString().toLowerCase().contains("driving license") && (DocumentType.equalsIgnoreCase("DRIVING LICENCE") || DocumentType.equalsIgnoreCase("BACK DRIVING LICENCE") || DocumentType.equalsIgnoreCase("FRONT BACK DRIVING LICENCE"))) {
                                is_Doc_Detected = true;
                            } else {
                                is_Doc_Detected = false;
                            }
                        } else {
                            is_Doc_Detected = true;
                        }

                        if (is_Doc_Detected) {

                            //ReducedAddressBitmap = addressProofBitmap;

                            // String addressProofPhoto =
                            // bitmapToString(addressProofBitmap);
                            // SelfAttestedDocumentActivity.lst_AddressBitmap.add(addressProofPhoto);

                            img_btn_document_upload_click_preview_image_address
                                    .setVisibility(View.VISIBLE);
                            img_btn_document_delete_address
                                    .setVisibility(View.VISIBLE);
                            img_btn_document_upload_click_image_address
                                    .setImageDrawable(getResources().getDrawable(
                                            R.drawable.checkedcamera));

                            //CheckedsameDoc();
                       /* if(AddressProofFileName.exists()){
                            AddressProofFileName.delete();
                        }*//*
                             *//*if (isoneSide == false) {
                        DeleteOldFile();
                        Intent intent = new Intent(MandDocumentActivity.this,
                                SelfAttestedDocumentActivity.class);
                        intent.putExtra("FLAG", "MandDocumentActivity");
                        intent.putExtra("ProposerSign", proposerSign);
                        intent.putExtra("PlanName", planName);
                        startActivity(intent);
                        showDialog(DIALOG_ALERT);

                    }*/
                        } else {
                            img_btn_document_upload_click_preview_image_address
                                    .setVisibility(View.INVISIBLE);
                            img_btn_document_delete_address
                                    .setVisibility(View.INVISIBLE);
                            img_btn_document_upload_click_image_address
                                    .setImageDrawable(getResources().getDrawable(
                                            R.drawable.ibtn_camera));

                            if (AddressProofFileName.exists()) {
                                AddressProofFileName.delete();
                            }
                            Toast.makeText(this, "Document is not properly detected,Kindly capture the" + spnr_document_upload_document_address.getSelectedItem().toString() + " document. ", Toast.LENGTH_LONG)
                                    .show();
                        }
                    }
                    else if (Check.equals("BankProof")) {
                        increment = 6;
                        //image compression by bhalla
                        CompressImage.compressImage(imagePath.getAbsolutePath());
                        String imageFileName = JPEG_FILE_PREFIX + ProposalNumber + "_X0"
                                + increment + "_" + JPEG_FILE_SUFFIX;
                        BankProofFileName = mStorageUtils.saveFileToAppSpecificDir(context, StorageUtils.DIRECT_DIRECTORY,
                                imageFileName, imagePath);

                        boolean is_Doc_Detected = true;

                        if (spnr_document_upload_document_bank
                                .getSelectedItem().toString().contains("Cancelled Cheque")) {
                            if (DocumentType.equalsIgnoreCase("CHEQUE")) {
                                is_Doc_Detected = true;
                            } else {
                                is_Doc_Detected = false;
                            }
                        } else {
                            is_Doc_Detected = true;
                        }

                        if (is_Doc_Detected) {

                            img_btn_document_upload_click_preview_image_bank
                                    .setVisibility(View.VISIBLE);
                            img_btn_document_delete_bank
                                    .setVisibility(View.VISIBLE);

                            img_btn_document_upload_click_image_bank.setImageDrawable(
                                    getResources().getDrawable(R.drawable.checkedcamera));

                            /*if (isBrowseCapture == REQUEST_CODE_PICK_FILE){
                                img_btn_document_upload_click_image_bank
                                        .setImageDrawable(getResources().getDrawable(
                                                R.drawable.ibtn_camera));

                                img_btn_document_upload_click_image_bank.setImageDrawable(getResources().getDrawable(
                                        R.drawable.checkedbrowse));

                            }else{
                                img_btn_document_upload_click_image_bank.setImageDrawable(
                                        getResources().getDrawable(R.drawable.checkedcamera));

                                img_btn_document_upload_click_image_bank.setImageDrawable(getResources().getDrawable(
                                        R.drawable.ibtn_browsedoc));
                            }*/

                        } else {
                            img_btn_document_upload_click_preview_image_bank
                                    .setVisibility(View.INVISIBLE);
                            img_btn_document_delete_bank
                                    .setVisibility(View.INVISIBLE);
                            img_btn_document_upload_click_image_bank
                                    .setImageDrawable(getResources().getDrawable(
                                            R.drawable.ibtn_camera));
                            img_btn_document_upload_click_image_bank.setImageDrawable(getResources().getDrawable(
                                    R.drawable.ibtn_browsedoc));

                            if (BankProofFileName.exists()) {
                                BankProofFileName.delete();
                            }
                            Toast.makeText(this, "Document is not properly detected,Kindly capture the" + spnr_document_upload_document_address.getSelectedItem().toString() + " document. ", Toast.LENGTH_LONG)
                                    .show();
                        }
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } else {
                Toast.makeText(context, "Data not receive", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private Bitmap decodeFile(File f) {
        try {
            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f), null, o);

            // The new size we want to scale to
            final int REQUIRED_SIZE = 70;

            // Find the correct scale value. It should be the power of 2.
            int scale = 1;
            while (o.outWidth / scale / 2 >= REQUIRED_SIZE
                    && o.outHeight / scale / 2 >= REQUIRED_SIZE)
                scale *= 2;

            // Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeStream(
                    new FileInputStream(f), null, o2);
        } catch (FileNotFoundException e) {
        }
        return null;
    }

    /*private File galleryAddPic() {
        Intent mediaScanIntent = new Intent(
                "android.intent.action.MEDIA_SCANNER_SCAN_FILE");
        f = new File(mCurrentPhotoPath);
        // strPathOfFile = mCurrentPhotoPath;

        String docName = f.getName();

        //
        // String pathMedia =
        // Environment.getExternalStorageDirectory().getAbsolutePath() +
        // "/MyImages/docName";
        // Uri uriSavedImage = Uri.fromFile(new File(pathMedia));
        // imageIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage);

        Intent imageIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File myDir = new File("/mnt/sdcard/alpha/cnsbank/images");
        myDir.mkdirs();
        File image = new File(myDir, docName);

        //nought changes
        Uri contentUri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            imageIntent.putExtra(MediaStore.EXTRA_OUTPUT, mCommonMethods.getContentUri(context, image));

            contentUri = mCommonMethods.getContentUri(context, f);
        } else {
            imageIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(image));

            contentUri = Uri.fromFile(f);
        }

        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
        return f;
    }*/

    public static byte[] generateKey(String password) throws Exception {
        byte[] keyStart = password.getBytes(StandardCharsets.UTF_8);

        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG", "Crypto");
        sr.setSeed(keyStart);
        kgen.init(128, sr);
        SecretKey skey = kgen.generateKey();
        return skey.getEncoded();
    }

    public void savefile() throws Exception {

        // File file = new File(Environment.getExternalStorageDirectory()
        // + File.separator + "SBIFolder", f.getName());
        BufferedOutputStream bos = new BufferedOutputStream(
                new FileOutputStream(f));
        byte[] yourKey = generateKey("password");
        convertFileToByteArray();
        byte[] filesBytes = encodeFile(yourKey, byteArray);
        bos.write(filesBytes);
        bos.flush();
        bos.close();

    }

    private void ClearDocumentsPDF() {

        AgeProofFileName = null;
        BankProofFileName = null;
        EFTProofFileName = null;
        IdentityProofFileName = null;
        AddressProofFileName = null;
        IncomeProofFileName = null;
        OtherProofFileName = null;
        Photo = null;
    }

    public static byte[] encodeFile(byte[] key, byte[] fileData)
            throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);

        return cipher.doFinal(fileData);
    }

    public void decryptFile() throws Exception {
        byte[] yourKey = generateKey("password");
        byte[] decodedData = decodeFile(yourKey, byteArray);
    }

    public static byte[] decodeFile(byte[] key, byte[] fileData)
            throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, skeySpec);

        return cipher.doFinal(fileData);
    }

    @SuppressWarnings("resource")
    public static byte[] convertFileToByteArray() {
        try {
            InputStream inputStream = new FileInputStream(f);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024 * 8];
            int bytesRead = 0;

            while ((bytesRead = inputStream.read(b)) != -1) {
                bos.write(b, 0, bytesRead);
            }

            byteArray = bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return byteArray;
    }

    /*
     * private void setBtnListenerOrDisable(Button btn, Button.OnClickListener
     * mTakePicOnClickListener, String intentName) { if (isIntentAvailable(this,
     * intentName)) { btn.setOnClickListener(mTakePicOnClickListener); } else {
     * btn.setTag(getText(R.string.cannot).toString() + " " + btn.getContext());
     * btn.setClickable(false); } }
     */

    public void onClickProceed(View v) throws
            Exception {

        CreateDocumentPdf();

    }

    public void Date() {
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        formattedDate = df.format(c.getTime());
        // formattedDate have current date/time
        // Toast.makeText(this, formattedDate, Toast.LENGTH_SHORT).show();
        c.add(Calendar.DATE, 30);
        ExpiredDate = df.format(c.getTime());
    }

    private long saveDataLocally(String ProofOf, String DocumentName,
                                 String Path, String FileName, String UploadDate) throws Exception {
        List<M_DocumentUpload> data = new ArrayList<M_DocumentUpload>();
        // byte[] fileByte = null;
        String document_Content = "";

        /*
         * try { fileByte = loadFile(Path); } catch (IOException e) { // TODO
         * Auto-generated catch block e.printStackTrace(); }
         *
         * if ((fileByte != null)) { document_Content =
         * Base64.encodeToString(fileByte, Base64.DEFAULT);
         *
         * data.add(new M_DocumentUpload(ProofOf, DocumentName,
         * document_Content, UploadDate, FileName, createdBy, createdDate,
         * modifiedBy, modifiedDate, isSync, isFlag1, isFlag2, isFlag3,
         * isFlag4));
         *
         * }
         */
        // if (!spnr_document_upload_document_age.getSelectedItem().toString()
        // .equals("Select Document")) {
        // Map<String, String> innerData = new HashMap<String, String>();
        // innerData.put(spnr_document_upload_document_age
        // .getSelectedItem().toString(), document_Content);
        // data.add(new M_DocumentUpload("AGE",
        // spnr_document_upload_document_age.getSelectedItem()
        // .toString(), document_Content, formattedDate,
        // FileName, createdBy, createdDate, modifiedBy,
        // modifiedDate, isSync, isFlag1, isFlag2, isFlag3,
        // isFlag4));
        //
        // } else if (!spnr_document_upload_document_address.getSelectedItem()
        // .toString().equals("Select Document")) {
        // Map<String, String> innerData = new HashMap<String, String>();
        // innerData.put(spnr_document_upload_document_address
        // .getSelectedItem().toString(), document_Content);
        //
        // data.add(new M_DocumentUpload("ADDRESS",
        // spnr_document_upload_document_address.getSelectedItem()
        // .toString(), document_Content, formattedDate,
        // FileName, createdBy, createdDate, modifiedBy,
        // modifiedDate, isSync, isFlag1, isFlag2, isFlag3,
        // isFlag4));
        //
        // }
        //
        // else if (!spnr_document_upload_document_identity.getSelectedItem()
        // .toString().equals("Select Document")) {
        // Map<String, String> innerData = new HashMap<String, String>();
        // innerData.put(spnr_document_upload_document_identity
        // .getSelectedItem().toString(), document_Content);
        //
        // data.add(new M_DocumentUpload("IDENTITY",
        // spnr_document_upload_document_identity
        // .getSelectedItem().toString(),
        // document_Content, formattedDate, FileName, createdBy,
        // createdDate, modifiedBy, modifiedDate, isSync, isFlag1,
        // isFlag2, isFlag3, isFlag4));
        //
        // } else if (!spnr_document_upload_document_income.getSelectedItem()
        // .toString().equals("Select Document")) {
        // Map<String, String> innerData = new HashMap<String, String>();
        // innerData.put(spnr_document_upload_document_income
        // .getSelectedItem().toString(), document_Content);
        //
        // data.add(new M_DocumentUpload("INCOME",
        // spnr_document_upload_document_income.getSelectedItem()
        // .toString(), document_Content, formattedDate,
        // FileName, createdBy, createdDate, modifiedBy,
        // modifiedDate, isSync, isFlag1, isFlag2, isFlag3,
        // isFlag4));
        //
        // }
        //
        // else if (!edt_document_upload_document_others.getText().toString()
        // .equals("")) {
        // Map<String, String> innerData = new HashMap<String, String>();
        // innerData.put(edt_document_upload_document_others.getText()
        // .toString(), document_Content);
        //
        // data.add(new M_DocumentUpload("OTHER",
        // edt_document_upload_document_others.getText()
        // .toString(), document_Content, formattedDate,
        // FileName, createdBy, createdDate, modifiedBy,
        // modifiedDate, isSync, isFlag1, isFlag2, isFlag3,
        // isFlag4));
        //
        //
        // }
        data.add(new M_DocumentUpload(ProofOf, DocumentName, document_Content,
                UploadDate, FileName, createdBy, createdDate, modifiedBy,
                modifiedDate, isSync, isFlag1, isFlag2, isFlag3, isFlag4));

        long rowId = db.insertDocumentUploadDetail(ProposalNumber,
                QuotationNumber, data);
        if (rowId >= 1) {

            Toast.makeText(context, "Document Saved SuccessFully..",
                    Toast.LENGTH_SHORT).show();
        }
        return rowId;
    }

    public static byte[] loadFile(String sourcePath) throws IOException {
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(sourcePath);
            return readFully(inputStream);
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }

    public static byte[] readFully(InputStream stream) throws IOException {
        byte[] buffer = new byte[8192];
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        int bytesRead;
        while ((bytesRead = stream.read(buffer)) != -1) {
            baos.write(buffer, 0, bytesRead);
        }
        return baos.toByteArray();
    }

    private int getIndex(Spinner s1, String value) {

        int index = 0;

        for (int i = 0; i < s1.getCount(); i++) {
            if (s1.getItemAtPosition(i).equals(value)) {
                index = i;
            }
        }
        return index;
    }


    public void uploaddoc(File f) {

		/*String extStorageDirectory = Environment.getExternalStorageDirectory()
				.toString();
		String direct = "/SBI-Smart Advisor";
		File f = new File(extStorageDirectory + direct + "/");

		if (!f.exists()) {
			f.mkdirs();
		}
		f = new File(f, ProposalNumber + "_" + "X" + increment + "." + "pdf");*/

        FileInputStream fin = null;
        try {

            str_extension = f.getAbsolutePath().substring(f.getAbsolutePath().lastIndexOf("."));

            if (str_extension.equals(".png") || str_extension.equals(".jpeg") ||
                    str_extension.equals(".jpg")) {

                String textPrint = "Lat: " + mLatLng.latitude + ", Long: " + mLatLng.longitude;

                String stringToParse = "";
                StringBuffer sb;
                if (mLatLng.latitude == 0.0 && mLatLng.longitude == 0.0){
                    stringToParse = "Address: \n";
                }else{
                    stringToParse = "Address: " + mCommonMethods.getCompleteAddressString(context, mLatLng.latitude, mLatLng.longitude);
                }
                sb = new StringBuffer(stringToParse);

                int i = 0;
                while ((i = sb.indexOf(" ", i + 25)) != -1) {
                    sb.replace(i, i + 1, "\n");
                }

                textPrint += "\n" + mCommonMethods.GetUserType(context) + " Name: " + mCommonMethods.getUserName(context)
                        + ", " + mCommonMethods.GetUserType(context) + " ID: " + mCommonMethods.GetUserCode(context)
                        + "\n" + sb.toString()
                        + "Date: " + mCommonMethods.getCurrentTimeStamp();

                Bitmap newBitmap = mCommonMethods.mergeBitmap(mCommonMethods.getBitmap(f.getAbsolutePath()),
                        mCommonMethods.convertStringToBitMap(context, textPrint));

                //save bitmap to file location
                //mCommonMethods.storeImage(mContext, newBitmap, mAllFile.getAbsolutePath());

                //convert bitmap to byte[]
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                newBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                bytes = stream.toByteArray();

            }else{
                fin = new FileInputStream(f);

                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                byte[] b = new byte[1024];
                int bytesRead;
                try {
                    while ((bytesRead = fin.read(b)) != -1) {
                        bos.write(b, 0, bytesRead);
                    }

                    bytes = bos.toByteArray();

                    bos.flush();
                    bos.close();

                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            progressDialog = new ProgressDialog(this, ProgressDialog.THEME_HOLO_LIGHT);
            String Message = "Please Wait";
            progressDialog.setMessage(Html.fromHtml("<font color='#00a1e3'><b>" + Message + "<b></font>"));
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setTitle(Html
                    .fromHtml("<font color='#00a1e3'><b>Uploading<b></font>"));
            progressDialog.setMax(100);
            progressDialog.show();

            service_hits(METHOD_NAME_UPLOAD_FILE);

        } catch (FileNotFoundException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }

    public void onUpload_AgeProof1(View v) {

        if (!(edt_requirement_Proposer_no.getText().toString()
                .equalsIgnoreCase(""))) {

            if (AgeProofFileName != null) {
                if (AgeProofFileName.exists()) {

                    if (req1 != null) {
                        if (req1.equals("NON-MEDICAL")
                                && cb_document_upload_age_upload_tick.isChecked()) {

                            // String DocumentName = des1_Age;

                            increment = 1;
                            GetDocumentDetails();
                            uploaddoc(AgeProofFileName);

                            // DeleteFiles();

                        } else {

                            String message = "";
                            if (req1.equals("MEDICAL")) {
                                message = "Sorry! Not allow upload Medical Requirement";
                            } else if (AgeProofFileName == null) {
                                message = "Please Capture Or Browse The Age Proof Document First";
                            } else if (!cb_document_upload_age_upload_tick
                                    .isChecked()) {
                                message = "Please Tick on verified with original clause";
                            }
                            AlertDialogMessage obj = new AlertDialogMessage();
                            if (!message.equals(""))
                                obj.dialog(this, message, true);
                            upload_Flag = 1;
                            validate_age();
                        }
                    }

                } else {

                    ClearDocumentsPDF();
                    Toast.makeText(
                            context,
                            "Please Capture Or Browse The Age Proof Document First",
                            Toast.LENGTH_SHORT).show();
                }
            } else {

                ClearDocumentsPDF();
            }
        } else {

            ClearDocumentsPDF();

            Toast.makeText(context,
                    "Your Proposal Number is not generated !!!",
                    Toast.LENGTH_LONG).show();
        }

    }

    public void onUpload_eftProof1(View v) {

        if (!(edt_requirement_Proposer_no.getText().toString()
                .equalsIgnoreCase(""))) {

            if (EFTProofFileName != null) {
                if (EFTProofFileName.exists()) {

                    if (req7.equals("NON-MEDICAL")
                            && eftProofBitmap != null
                            && cb_document_upload_eft_upload_tick.isChecked()) {

                        // String DocumentName = des7_EFT;

                        increment = 7;
                        GetDocumentDetails();
                        uploaddoc(EFTProofFileName);

                        // DeleteFiles();

                    } else {

                        String message = "";
                        if (req7.equals("MEDICAL")) {
                            message = "Sorry! Not allow upload Medical Requirement";
                        } else if (EFTProofFileName == null) {
                            message = "Please Capture Or Browse The Document First";
                        } else if (!cb_document_upload_eft_upload_tick
                                .isChecked()) {
                            message = "Please Tick on verified with original clause";
                        }
                        AlertDialogMessage obj = new AlertDialogMessage();
                        if (!message.equals(""))
                            obj.dialog(this, message, true);

                    }
                    // }

                } else {

                    ClearDocumentsPDF();
                    Toast.makeText(context,
                            "Please Capture Or Browse The Document First",
                            Toast.LENGTH_SHORT).show();
                }
            } else {

                ClearDocumentsPDF();
            }
        } else {

            ClearDocumentsPDF();

            Toast.makeText(context,
                    "Your Proposal Number is not generated !!!",
                    Toast.LENGTH_LONG).show();

        }

    }

    public void onUpload_IdentityProof1(View v) {

        if (!(edt_requirement_Proposer_no.getText().toString()
                .equalsIgnoreCase(""))) {

            if (IdentityProofFileName != null) {
                if (IdentityProofFileName.exists()) {

                    if (req2.equals("NON-MEDICAL")
                            && cb_document_upload_identity_upload_tick
                            .isChecked()) {

                        // String DocumentName = des2_Identity;
                        increment = 2;
                        GetDocumentDetails();
                        uploaddoc(IdentityProofFileName);
                        // DeleteFiles();

                    } else {
                        String message = "";
                        if (req2.equals("MEDICAL")) {
                            message = "Sorry! Not allow upload Medical Requirement";
                        } else if (IdentityProofFileName == null) {
                            message = "Please Capture Or Browse The Identity Proof Document First";
                        } else if (!cb_document_upload_identity_upload_tick
                                .isChecked()) {
                            message = "Please Tick on verified with original clause";
                        }
                        AlertDialogMessage obj = new AlertDialogMessage();
                        if (!message.equals(""))
                            obj.dialog(this, message, true);

                    }
                    // }
                } else {
                    ClearDocumentsPDF();

                    Toast.makeText(
                            context,
                            "Please Capture Or Browse The Identity Proof Document First",
                            Toast.LENGTH_SHORT).show();
                }
            } else {

                ClearDocumentsPDF();
            }
        } else {

            ClearDocumentsPDF();
            Toast.makeText(context,
                    "Your Proposal Number is not generated !!!",
                    Toast.LENGTH_LONG).show();

        }
    }

    public void onUpload_AddressProof1(View v) {
        if (!(edt_requirement_Proposer_no.getText().toString()
                .equalsIgnoreCase(""))) {
            if (AddressProofFileName != null) {
                if (AddressProofFileName.exists()) {

                    if (req3.equals("NON-MEDICAL")
                            && cb_document_upload_address_upload_tick
                            .isChecked()) {

                        // String DocumentName = des3_Address;
                        increment = 3;
                        GetDocumentDetails();
                        uploaddoc(AddressProofFileName);
                        // DeleteFiles();

                    } else {
                        String message = "";
                        if (req3.equals("MEDICAL")) {
                            message = "Sorry! Not allow upload Medical Requirement";
                        } else if (AddressProofFileName == null) {
                            message = "Please Capture Or Browse The Address Proof Document First";
                        } else if (!cb_document_upload_address_upload_tick
                                .isChecked()) {
                            message = "Please Tick on verified with original clause";
                        }
                        AlertDialogMessage obj = new AlertDialogMessage();
                        if (!message.equals(""))
                            obj.dialog(this, message, true);

                    }
                    // }
                } else {
                    ClearDocumentsPDF();

                    Toast.makeText(
                            context,
                            "Please Capture Or Browse The Address Proof Document First",
                            Toast.LENGTH_SHORT).show();
                }
            } else {

                ClearDocumentsPDF();
            }

        } else {

            ClearDocumentsPDF();

            Toast.makeText(context,
                    "Your Proposal Number is not generated !!!",
                    Toast.LENGTH_LONG).show();

        }
    }

    public void onUpload_IncomeProof1(View v) {

        if (!(edt_requirement_Proposer_no.getText().toString()
                .equalsIgnoreCase(""))) {

            if (IncomeProofFileName != null) {
                if (IncomeProofFileName.exists()) {

                    if (req4.equals("NON-MEDICAL")
                            && cb_document_upload_income_upload_tick
                            .isChecked()) {
                        // String DocumentName = des4_Income;
                        increment = 4;
                        GetDocumentDetails();
                        uploaddoc(IncomeProofFileName);
                        // DeleteFiles();

                    } else {
                        String message = "";
                        if (req4.equals("MEDICAL")) {
                            message = "Sorry! Not allow upload Medical Requirement";
                        } else if (IncomeProofFileName == null) {
                            message = "Please Capture Or Browse The Income Proof Document First";
                        } else if (!cb_document_upload_income_upload_tick
                                .isChecked()) {
                            message = "Please Tick on verified with original clause";
                        }
                        AlertDialogMessage obj = new AlertDialogMessage();
                        if (!message.equals(""))
                            obj.dialog(this, message, true);

                    }
                    // }
                } else {

                    ClearDocumentsPDF();
                    Toast.makeText(
                            context,
                            "Please Capture Or Browse The Income Proof Document First",
                            Toast.LENGTH_SHORT).show();
                }
            } else {

                ClearDocumentsPDF();
            }

        } else {

            ClearDocumentsPDF();

            Toast.makeText(context,
                    "Your Proposal Number is not generated !!!",
                    Toast.LENGTH_LONG).show();

        }
    }

    public void onUpload_BankProof1(View v) {

        if (!(edt_requirement_Proposer_no.getText().toString()
                .equalsIgnoreCase(""))) {

            if (BankProofFileName != null) {
                if (BankProofFileName.exists()) {

                    if (req5.equals("NON-MEDICAL")
                            && cb_document_upload_bank_upload_tick.isChecked()) {
                        // String DocumentName = des5_bank;
                        increment = 6;
                        GetDocumentDetails();
                        uploaddoc(BankProofFileName);
                        // DeleteFiles();

                    } else {
                        String message = "";
                        if (BankProofFileName == null) {
                            message = "Please Capture Or Browse The bank Proof Document First";
                        }
                        AlertDialogMessage obj = new AlertDialogMessage();
                        obj.dialog(this, message, true);

                    }
                    // }
                } else {

                    ClearDocumentsPDF();
                    Toast.makeText(
                            context,
                            "Please Capture Or Browse The Income Proof Document First",
                            Toast.LENGTH_SHORT).show();
                }
            } else {

                ClearDocumentsPDF();
            }

        } else {

            ClearDocumentsPDF();

            Toast.makeText(context,
                    "Your Proposal Number is not generated !!!",
                    Toast.LENGTH_LONG).show();

        }
    }

    public void onUpload_OthersProof1(View v) {

        if (!(edt_requirement_Proposer_no.getText().toString()
                .equalsIgnoreCase(""))) {
            if (OtherProofFileName != null) {
                if (OtherProofFileName.exists()) {

                    if (req6.equals("NON-MEDICAL")
                            && cb_document_upload_others_upload_tick
                            .isChecked()) {

                        increment = 5;

                        GetDocumentDetails();
                        uploaddoc(OtherProofFileName);

                    } else {
                        String message = "";
                        if (req6.equals("MEDICAL")) {
                            message = "Sorry! Not allow upload Medical Requirement";
                        } else if (OtherProofFileName == null) {
                            message = "Please Capture Or Browse The Other Proof Document First";
                        } else if (!cb_document_upload_others_upload_tick
                                .isChecked()) {
                            message = "Please Tick on verified with original clause";
                        }
                        AlertDialogMessage obj = new AlertDialogMessage();
                        if (!message.equals(""))
                            obj.dialog(this, message, true);

                    }
                    // }
                } else {

                    ClearDocumentsPDF();
                    Toast.makeText(
                            context,
                            "Please Capture Or Browse The Other Proof Document First",
                            Toast.LENGTH_SHORT).show();
                }
            } else {

                ClearDocumentsPDF();
            }

        } else {

            ClearDocumentsPDF();

            Toast.makeText(context,
                    "Your Proposal Number is not generated !!!",
                    Toast.LENGTH_LONG).show();

        }
    }

    public void CreateDocumentPdf() throws DocumentException, IOException {
        /*
         * BaseFont bf_helv = BaseFont.createFont(BaseFont.HELVETICA, "Cp1252",
         * false); BaseFont bf_times = BaseFont.createFont(BaseFont.TIMES_ROMAN,
         * "Cp1252", false); BaseFont bf_courier =
         * BaseFont.createFont(BaseFont.COURIER, "Cp1252", false); BaseFont
         * bf_symbol = BaseFont.createFont(BaseFont.SYMBOL, "Cp1252", false);
         * Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
         * Font redFont = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL,
         * BaseColor.RED); Font subFont = new Font(Font.FontFamily.TIMES_ROMAN,
         * 16, Font.BOLD);
         */
        Font sub_headerBold = new Font(Font.FontFamily.TIMES_ROMAN, 11,
                Font.BOLD);

        Font headerBold = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD,
                BaseColor.WHITE);
        /*
         * Font small_bold = new Font(Font.FontFamily.TIMES_ROMAN, 10,
         * Font.BOLD); Font small_normal = new Font(Font.FontFamily.TIMES_ROMAN,
         * 10, Font.NORMAL);
         */

        File mypath = mStorageUtils.createFileToAppSpecificDir(context, "DocumentUpload1.pdf");

        Document document = new Document(PageSize.A4, 50, 50, 50, 50);
        PdfWriter pdf_writer = null;
        pdf_writer = PdfWriter.getInstance(document, new FileOutputStream(
                mypath.getAbsolutePath()));

        document.open();

        Paragraph DocumentUpload_Para_Header = new Paragraph();
        DocumentUpload_Para_Header.add(new Paragraph("DOCUMENT UPLOAD DETAILS",
                headerBold));

        PdfPTable DocumentUpload_headertable = new PdfPTable(1);
        DocumentUpload_headertable.setWidthPercentage(100);
        PdfPCell DocumentUpload_c1 = new PdfPCell(new Phrase(
                DocumentUpload_Para_Header));
        DocumentUpload_c1.setBackgroundColor(BaseColor.DARK_GRAY);
        DocumentUpload_c1.setPadding(5);
        DocumentUpload_c1.setHorizontalAlignment(Element.ALIGN_LEFT);
        DocumentUpload_headertable.addCell(DocumentUpload_c1);
        DocumentUpload_headertable.setHorizontalAlignment(Element.ALIGN_LEFT);

        document.add(DocumentUpload_headertable);

        Paragraph DocumentUpload_Blackheader1 = new Paragraph("");
        Paragraph DocumentUpload_Blackheader2 = new Paragraph("");
        document.add(DocumentUpload_Blackheader1);
        document.add(DocumentUpload_Blackheader2);
        // ByteArrayOutputStream Photo_stream = new ByteArrayOutputStream();
        // // Bitmap Photobitmap = BitmapFactory.decodeResource(getBaseContext()
        // // .getResources(), R.drawable.devangimage);
        // ReducedPhotoBitmap
        // .compress(Bitmap.CompressFormat.PNG, 50, Photo_stream);
        // Image photoImage = Image.getInstance(Photo_stream.toByteArray());
        // photoImage.setAlignment(Image.RIGHT);
        // photoImage.scaleToFit(80, 50);
        // document.add(photoImage);
        // document.add(DocumentUpload_Blackheader1);
        // document.add(DocumentUpload_Blackheader2);

        PdfPTable DD_table2 = new PdfPTable(3);
        DD_table2.setWidthPercentage(100);
        float[] columnWidths = {0.2f, 0.2f, 0.6f};
        DD_table2.setWidths(columnWidths);
        PdfPCell DocumentUpload_row1_cell_1 = new PdfPCell(new Paragraph(
                "Proof of", sub_headerBold));
        DocumentUpload_row1_cell_1.setPadding(5);
        DocumentUpload_row1_cell_1.setHorizontalAlignment(Element.ALIGN_CENTER);
        DocumentUpload_row1_cell_1.setBackgroundColor(BaseColor.LIGHT_GRAY);

        PdfPCell DocumentUpload_row1_cell_2 = new PdfPCell(new Paragraph(
                "Document", sub_headerBold));
        DocumentUpload_row1_cell_2.setBackgroundColor(BaseColor.LIGHT_GRAY);
        DocumentUpload_row1_cell_2.setHorizontalAlignment(Element.ALIGN_CENTER);
        DocumentUpload_row1_cell_2.setPadding(5);

        PdfPCell DocumentUpload_row1_cell_3 = new PdfPCell(new Paragraph("",
                sub_headerBold));
        DocumentUpload_row1_cell_3.setBackgroundColor(BaseColor.LIGHT_GRAY);
        DocumentUpload_row1_cell_3.setHorizontalAlignment(Element.ALIGN_CENTER);
        DocumentUpload_row1_cell_3.setPadding(5);

        DD_table2.addCell(DocumentUpload_row1_cell_1);
        DD_table2.addCell(DocumentUpload_row1_cell_2);
        DD_table2.addCell(DocumentUpload_row1_cell_3);

        document.add(DD_table2);

        PdfPTable DD_table3 = new PdfPTable(3);
        DD_table3.setWidthPercentage(100);
        DD_table3.setWidths(columnWidths);
        PdfPCell DocumentUpload_row2_cell_1 = new PdfPCell(new Paragraph("",
                sub_headerBold));
        DocumentUpload_row2_cell_1.setPadding(5);
        DocumentUpload_row2_cell_1.setHorizontalAlignment(Element.ALIGN_CENTER);

        PdfPCell DocumentUpload_row2_cell_2 = new PdfPCell(new Paragraph(
                des1_Age, sub_headerBold));
        DocumentUpload_row1_cell_2.setHorizontalAlignment(Element.ALIGN_CENTER);
        DocumentUpload_row1_cell_2.setPadding(5);

        ByteArrayOutputStream Agestream = new ByteArrayOutputStream();
        // Bitmap Agebitmap = BitmapFactory.decodeResource(getBaseContext()
        // .getResources(), R.drawable.proof1);
        ReducedAddressBitmap.compress(Bitmap.CompressFormat.PNG, 40, Agestream);
        Image img_Age = Image.getInstance(Agestream.toByteArray());

        PdfPCell DocumentUpload_row2_cell_3 = new PdfPCell();
        DocumentUpload_row2_cell_3.setHorizontalAlignment(Element.ALIGN_CENTER);
        DocumentUpload_row2_cell_3.setPadding(5);
        DocumentUpload_row2_cell_3.setImage(img_Age);

        DD_table3.addCell(DocumentUpload_row2_cell_1);
        DD_table3.addCell(DocumentUpload_row2_cell_2);
        DD_table3.addCell(DocumentUpload_row2_cell_3);

        document.add(DD_table3);

        PdfPTable DD_table4 = new PdfPTable(3);
        DD_table4.setWidthPercentage(100);
        DD_table4.setWidths(columnWidths);
        PdfPCell DocumentUpload_row3_cell_1 = new PdfPCell(new Paragraph("",
                sub_headerBold));
        DocumentUpload_row3_cell_1.setPadding(5);
        DocumentUpload_row3_cell_1.setHorizontalAlignment(Element.ALIGN_CENTER);

        PdfPCell DocumentUpload_row3_cell_2 = new PdfPCell(new Paragraph(
                des2_Identity, sub_headerBold));
        DocumentUpload_row1_cell_2.setHorizontalAlignment(Element.ALIGN_CENTER);
        DocumentUpload_row1_cell_2.setPadding(5);

        ByteArrayOutputStream Identitystream = new ByteArrayOutputStream();
        // Bitmap Identitybitmap = BitmapFactory.decodeResource(getBaseContext()
        // .getResources(), R.drawable.proof1);
        ReducedIdentityBitmap.compress(Bitmap.CompressFormat.PNG, 40,
                Identitystream);
        Image img_Identity = Image.getInstance(Agestream.toByteArray());

        PdfPCell DocumentUpload_row3_cell_3 = new PdfPCell();
        DocumentUpload_row3_cell_3.setHorizontalAlignment(Element.ALIGN_CENTER);
        DocumentUpload_row3_cell_3.setPadding(5);
        DocumentUpload_row3_cell_3.setImage(img_Identity);

        DD_table4.addCell(DocumentUpload_row3_cell_1);
        DD_table4.addCell(DocumentUpload_row3_cell_2);
        DD_table4.addCell(DocumentUpload_row3_cell_3);

        document.add(DD_table4);

        PdfPTable DD_table5 = new PdfPTable(3);
        DD_table5.setWidthPercentage(100);
        DD_table5.setWidths(columnWidths);
        PdfPCell DocumentUpload_row4_cell_1 = new PdfPCell(new Paragraph(
                "Address", sub_headerBold));
        DocumentUpload_row4_cell_1.setPadding(5);
        DocumentUpload_row4_cell_1.setHorizontalAlignment(Element.ALIGN_CENTER);

        PdfPCell DocumentUpload_row4_cell_2 = new PdfPCell(new Paragraph(
                AddressDocument, sub_headerBold));
        DocumentUpload_row4_cell_2.setHorizontalAlignment(Element.ALIGN_CENTER);
        DocumentUpload_row4_cell_2.setPadding(5);

        ByteArrayOutputStream Addressstream = new ByteArrayOutputStream();
        // Bitmap Addressbitmap = BitmapFactory.decodeResource(getBaseContext()
        // .getResources(), R.drawable.proof1);
        ReducedAddressBitmap.compress(Bitmap.CompressFormat.PNG, 40,
                Addressstream);
        Image img_Address = Image.getInstance(Agestream.toByteArray());

        PdfPCell DocumentUpload_row4_cell_3 = new PdfPCell();
        DocumentUpload_row4_cell_3.setHorizontalAlignment(Element.ALIGN_CENTER);
        DocumentUpload_row4_cell_3.setPadding(5);
        DocumentUpload_row4_cell_3.setImage(img_Address);

        DD_table5.addCell(DocumentUpload_row4_cell_1);
        DD_table5.addCell(DocumentUpload_row4_cell_2);
        DD_table5.addCell(DocumentUpload_row4_cell_3);

        document.add(DD_table5);
        document.newPage();
        PdfPTable DD_table6 = new PdfPTable(3);
        DD_table6.setWidthPercentage(100);
        DD_table6.setWidths(columnWidths);
        PdfPCell DocumentUpload_row5_cell_1 = new PdfPCell(new Paragraph("",
                sub_headerBold));
        DocumentUpload_row5_cell_1.setPadding(5);
        DocumentUpload_row5_cell_1.setHorizontalAlignment(Element.ALIGN_CENTER);

        PdfPCell DocumentUpload_row5_cell_2 = new PdfPCell(new Paragraph(
                des3_Address, sub_headerBold));
        DocumentUpload_row5_cell_2.setHorizontalAlignment(Element.ALIGN_CENTER);
        DocumentUpload_row5_cell_2.setPadding(5);

        ByteArrayOutputStream Incomestream = new ByteArrayOutputStream();
        // Bitmap Incomebitmap = BitmapFactory.decodeResource(getBaseContext()
        // .getResources(), R.drawable.proof1);
        ReducedIncomeBitmap.compress(Bitmap.CompressFormat.PNG, 40,
                Incomestream);
        Image img_Income = Image.getInstance(Agestream.toByteArray());

        PdfPCell DocumentUpload_row5_cell_3 = new PdfPCell();
        DocumentUpload_row5_cell_3.setHorizontalAlignment(Element.ALIGN_CENTER);
        DocumentUpload_row5_cell_3.setPadding(5);
        DocumentUpload_row5_cell_3.setImage(img_Income);

        DD_table6.addCell(DocumentUpload_row5_cell_1);
        DD_table6.addCell(DocumentUpload_row5_cell_2);
        DD_table6.addCell(DocumentUpload_row5_cell_3);

        document.add(DD_table6);

        PdfPTable DD_table7 = new PdfPTable(1);
        DD_table6.setWidthPercentage(100);
        DD_table6.setWidths(columnWidths);
        PdfPCell DocumentUpload_row7_cell_1 = new PdfPCell(new Paragraph(
                "Verified with original.", sub_headerBold));
        DocumentUpload_row7_cell_1.setPadding(5);
        DocumentUpload_row7_cell_1.setHorizontalAlignment(Element.ALIGN_LEFT);
        DD_table7.addCell(DocumentUpload_row7_cell_1);

        document.add(DD_table7);

        // ByteArrayOutputStream PropserSign_stream = new
        // ByteArrayOutputStream();
        // // Bitmap Signbitmap = BitmapFactory.decodeResource(getBaseContext()
        // // .getResources(), R.drawable.signimage);
        // (CaptureSignature.scaled).compress(Bitmap.CompressFormat.PNG, 50,
        // PropserSign_stream);
        // Image PropserSign_sign = Image.getInstance(PropserSign_stream
        // .toByteArray());

        // PdfPTable AD_table7 = new PdfPTable(2);
        // AD_table7.setWidthPercentage(100);
        // PdfPCell PropserSign_cell1 = new PdfPCell(new Paragraph(
        // "Appointee Signature", small_normal));
        //
        // PdfPCell PropserSign_cell2 = new PdfPCell();
        //
        // PropserSign_cell2.setImage(PropserSign_sign);
        //
        // PropserSign_cell1.setPadding(5);
        // PropserSign_cell2.setPadding(5);
        // PropserSign_cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
        //
        // PropserSign_cell1.setPaddingBottom(Element.ALIGN_CENTER);
        // PropserSign_cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
        // AD_table7.addCell(PropserSign_cell1);
        // AD_table7.addCell(PropserSign_cell2);
        //
        // document.add(AD_table7);

        document.close();

    }

    public void ViewDocumentDialog() {
        GridView gv;
        final Dialog d = new Dialog(this);
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);
        d.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.BLACK));

        d.setContentView(R.layout.layout_document_upload_status);

        // Button btn = (Button) d.findViewById(R.id.btn_proceed);
        gv = d.findViewById(R.id.gv_userdocumentinfo);

        List<M_DocumentUploadStatus> DocumentDetails = null;
        DocumentUploadViewAdapter adapter;

        try {
            DocumentDetails = db.getDocumentsDetails(ProposalNumber);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        adapter = new DocumentUploadViewAdapter(DocumentDetails, this);
        gv.setAdapter(adapter);
        registerForContextMenu(gv);
        gv.setTextFilterEnabled(true);

        /* Initialization of layout id */

        // btn.setOnClickListener(new OnClickListener() {
        //
        // @Override
        // public void onClick(View arg0) {
        //
        // d.dismiss();
        //
        // }
        //
        // });
        d.show();

    }

    public String bitmapToString(Bitmap bitmap) {
        String signPhoto = "";
        if (bitmap != null) {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            byte[] signByteArray = out.toByteArray();
            signPhoto = Base64.encodeToString(signByteArray, Base64.DEFAULT);

        }

        return signPhoto;
    }

    public void DeleteFiles() {

        File mypath = mStorageUtils.createFileToAppSpecificDir(context, ProposalNumber + "_" + "X" + increment
                + "." + "pdf");

        if (mypath.exists())
            mypath.delete();

        if (imageF != null) {
            imageF.delete();
        }
    }

	/*Bitmap ShrinkBitmap(String file, int width, int height) {

		BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
		bmpFactoryOptions.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeFile(file, bmpFactoryOptions);

		int heightRatio = (int) Math.ceil(bmpFactoryOptions.outHeight
				/ (float) height);
		int widthRatio = (int) Math.ceil(bmpFactoryOptions.outWidth
				/ (float) width);

		if (heightRatio > 1 || widthRatio > 1) {
			if (heightRatio > widthRatio) {
				bmpFactoryOptions.inSampleSize = heightRatio;
			} else {
				bmpFactoryOptions.inSampleSize = widthRatio;
			}
		}

		bmpFactoryOptions.inJustDecodeBounds = false;
		bitmap = BitmapFactory.decodeFile(file, bmpFactoryOptions);
		return bitmap;
	}*/

    public void onView(View v) {

        Intent i = new Intent(this, ViewDocumentStatusActivity.class);
        startActivity(i);
        // ViewDocumentDialog();

    }

    public void onSubmit(View v) {

        if ((!edt_requirement_Proposer_no.getText().toString().equals(""))) {
            ProposalNumber = edt_requirement_Proposer_no.getText().toString();
            CheckProposalValid();

            // hideKeyboard(AdditionalUtilityActivity.this);
        } else {
            // hideKeyboard(AdditionalUtilityActivity.this);
            Toast toast = Toast.makeText(this,
                    "Please Enter Proposal number first", Toast.LENGTH_SHORT);
            toast.show();
        }

        btn_focus.requestFocus();
        setFocusable(btn_focus);
        // edt_requirement_Proposer_no.setFocusable(false);
        InputMethodManager imm = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

    }

    private void setFocusable(Button v) {
        // TODO Auto-generated method stub
        v.setFocusable(true);
        v.setFocusableInTouchMode(true);
    }

    public static void hideKeyboard(AppCompatActivity activity) {

        InputMethodManager imm = (InputMethodManager) activity
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

    }

    public void CheckProposalValid() {

        service_hits(METHOD_NAME_VALID_PROPSOAL);

    }

    public class AsynccheakProposal extends AsyncTask<String, Void, String> {
        private volatile boolean running = true;

        protected String doInBackground(String... param) {
            try {
                running = true;

                SoapObject request = new SoapObject(NAMESPACE,
                        METHOD_NAME_VALID_PROPSOAL);

                request.addProperty("strPropNum", ProposalNumber);
                request.addProperty("adType", agentType);
                request.addProperty("adCode", agentcode);
                request.addProperty("strEmailId", agentEmail);
                request.addProperty("strMobileNo", agentMobile);
                request.addProperty("strAuthKey",
                        SimpleCrypto.encrypt("SBIL", agentPassword.trim()));

                mCommonMethods.TLSv12Enable();

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);
                envelope.dotNet = true;

                envelope.setOutputSoapObject(request);

                // allowAllSSL();


                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);

                HttpTransportSE androidHttpTranport = new HttpTransportSE(ServiceURL.SERVICE_URL);
                androidHttpTranport.call(SOAP_ACTION_VALID_PROPSOAL, envelope);
                Object response = envelope.getResponse();
                str_Valid_Success = response.toString();
                // str_Valid_Success="1";
            } catch (Exception e) {

                mProgressDialog.dismiss();
                running = false;
            }
            return null;

        }

        protected void onPostExecute(String result) {
            if (mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
            if (running) {
                hideKeyboard(AdditionalUtilityActivity.this);
                if (str_Valid_Success.equals("1")) {

                    // btn_proceed.setVisibility(View.GONE);
                    // edt_requirement_Proposer_no.setVisibility(View.GONE);
                    service_hits(METHOD_NAME_UPLOAD_FILE_GET_REQUIREMENT);

                } else {
                    // btn_proceed.setVisibility(View.VISIBLE);
                    // edt_requirement_Proposer_no.setVisibility(View.VISIBLE);
                    Toast toast = Toast.makeText(
                            AdditionalUtilityActivity.this,
                            "Please Enter Valid Proposal Number",
                            Toast.LENGTH_SHORT);
                    toast.show();
                }
            }

        }
    }

    public class AsynccheakProposalRequirement extends
            AsyncTask<String, Void, String> {
        private volatile boolean running = true;

        protected String doInBackground(String... param) {
            try {
                running = true;
                // ProposalNumber = "49AH762229";
                SoapObject request = new SoapObject(NAMESPACE,
                        METHOD_NAME_UPLOAD_FILE_GET_REQUIREMENT);
                request.addProperty("strPolicyNum", ProposalNumber);
                request.addProperty("strEmailId", agentEmail);
                request.addProperty("strMobileNo", agentMobile);
                request.addProperty("strAuthKey",
                        SimpleCrypto.encrypt("SBIL", agentPassword.trim()));
                // request.addProperty("strPolicyNum", "45QK789302");

                mCommonMethods.TLSv12Enable();
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);
                envelope.dotNet = true;

                envelope.setOutputSoapObject(request);

                // allowAllSSL();

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);

                HttpTransportSE androidHttpTranport = new HttpTransportSE(ServiceURL.SERVICE_URL);
                androidHttpTranport.call(
                        SOAP_ACTION_UPLOAD_FILE_GET_REQUIREMENT, envelope);
                Object response = envelope.getResponse();
                System.out.println("response:" + response.toString());
                // str_Valid_Success = response.toString();

                // ///

                if (!response.toString().contentEquals("anyType{}")) {

                    SoapPrimitive sa = null;
                    try {
                        sa = (SoapPrimitive) envelope.getResponse();

                        // inputpolicylist="<ReqDtls><Table><DESCRIPTION>IncomeProof</DESCRIPTION><REQUIREMENTFLAG>NON-MEDICAL</REQUIREMENTFLAG></Table><Table><DESCRIPTION>IncomeProof</DESCRIPTION><REQUIREMENTFLAG>NON-MEDICAL</REQUIREMENTFLAG></Table><Table><DESCRIPTION>IncomeProof</DESCRIPTION><REQUIREMENTFLAG>NON-MEDICAL</REQUIREMENTFLAG></Table><Table><DESCRIPTION>IncomeProof</DESCRIPTION><REQUIREMENTFLAG>NON-MEDICAL</REQUIREMENTFLAG></Table><Table><DESCRIPTION>IncomeProof</DESCRIPTION><REQUIREMENTFLAG>NON-MEDICAL</REQUIREMENTFLAG></Table><Table><DESCRIPTION>IncomeProof</DESCRIPTION><REQUIREMENTFLAG>NONMEDICAL</REQUIREMENTFLAG></Table><Table><DESCRIPTION>IncomeProof</DESCRIPTION><REQUIREMENTFLAG>NONMEDICAL</REQUIREMENTFLAG></Table><Table><DESCRIPTION>ECG</DESCRIPTION><REQUIREMENTFLAG>MEDICAL</REQUIREMENTFLAG></Table><Table><DESCRIPTION>CategoryA</DESCRIPTION><REQUIREMENTFLAG>MEDICAL</REQUIREMENTFLAG></Table><Table><DESCRIPTION>CategoryB</DESCRIPTION><REQUIREMENTFLAG>MEDICAL</REQUIREMENTFLAG></Table><Table><DESCRIPTION>CategoryC</DESCRIPTION><REQUIREMENTFLAG>MEDICAL</REQUIREMENTFLAG></Table><Table><DESCRIPTION>CategoryD</DESCRIPTION><REQUIREMENTFLAG>MEDICAL</REQUIREMENTFLAG></Table><Table><DESCRIPTION>FMR</DESCRIPTION><REQUIREMENTFLAG>MEDICAL</REQUIREMENTFLAG></Table></ReqDtls>";
                        // inputpolicylist="<ReqDtls> <Table> <DESCRIPTION>Income Proof</DESCRIPTION> <REQUIREMENTFLAG>NON-MEDICAL</REQUIREMENTFLAG> <PropStatus /> </Table> <Table> <DESCRIPTION>Audited Balance Sheet and Profit &amp; Loss Account </DESCRIPTION> <REQUIREMENTFLAG>NON-MEDICAL</REQUIREMENTFLAG> <PropStatus /> </Table> <Table> <DESCRIPTION>Copy of Bank Statement </DESCRIPTION> <REQUIREMENTFLAG>NON-MEDICAL</REQUIREMENTFLAG> <PropStatus /> </Table> </ReqDtls> ";
                        if (!sa.toString().equalsIgnoreCase("1")) {

                            inputpolicylist = sa.toString();
                            System.out.println(" inputpolicylist : "
                                    + inputpolicylist);
                            ParseXML prsObj = new ParseXML();

                            inputpolicylist = prsObj.parseXmlTag(
                                    inputpolicylist, "ReqDtls");
                            inputpolicylist = new ParseXML().parseXmlTag(
                                    inputpolicylist, "ScreenData");
                            String strPolicyListErrorCOde = inputpolicylist;

                            if (strPolicyListErrorCOde == null) {
                                // inputpolicylist="<ReqDtls><Table><DESCRIPTION>ECG</DESCRIPTION><REQUIREMENTFLAG>MEDICAL</REQUIREMENTFLAG></Table><Table><DESCRIPTION>CategoryA</DESCRIPTION><REQUIREMENTFLAG>MEDICAL</REQUIREMENTFLAG></Table><Table><DESCRIPTION>CategoryB</DESCRIPTION><REQUIREMENTFLAG>MEDICAL</REQUIREMENTFLAG></Table><Table><DESCRIPTION>CategoryC</DESCRIPTION><REQUIREMENTFLAG>MEDICAL</REQUIREMENTFLAG></Table><Table><DESCRIPTION>CategoryD</DESCRIPTION><REQUIREMENTFLAG>MEDICAL</REQUIREMENTFLAG></Table><Table><DESCRIPTION>FMR</DESCRIPTION><REQUIREMENTFLAG>MEDICAL</REQUIREMENTFLAG></Table></ReqDtls>";
                                // inputpolicylist="<ReqDtls> <Table> <DESCRIPTION>Income Proof</DESCRIPTION> <REQUIREMENTFLAG>NON-MEDICAL</REQUIREMENTFLAG> <PropStatus/> </Table> <Table> <DESCRIPTION>Audited Balance Sheet and Profit &amp; Loss Account </DESCRIPTION> <REQUIREMENTFLAG>NON-MEDICAL</REQUIREMENTFLAG> <PropStatus/> </Table> <Table> <DESCRIPTION>Copy of Bank Statement </DESCRIPTION> <REQUIREMENTFLAG>MEDICAL</REQUIREMENTFLAG> <PropStatus/> </Table> </ReqDtls> ";
                                // inputpolicylist="<ReqDtls> <Table> <DESCRIPTION>Income Proof</DESCRIPTION> <REQUIREMENTFLAG>MEDICAL</REQUIREMENTFLAG> <PropStatus>Not Taken Up</PropStatus> </Table> <Table> <DESCRIPTION>Audited Balance Sheet and Profit &amp; Loss Account </DESCRIPTION> <REQUIREMENTFLAG>MEDICAL</REQUIREMENTFLAG> <PropStatus>Not Taken Up</PropStatus> </Table> <Table> <DESCRIPTION>Copy of Bank Statement </DESCRIPTION> <REQUIREMENTFLAG>MEDICAL</REQUIREMENTFLAG> <PropStatus>Not Taken Up</PropStatus> </Table> </ReqDtls> ";
                                // inputpolicylist="<ReqDtls> <Table> <DESCRIPTION>Income Proof</DESCRIPTION> <REQUIREMENTFLAG>NON-MEDICAL</REQUIREMENTFLAG> <PropStatus>Not Taken Up</PropStatus> </Table> <Table> <DESCRIPTION>Audited Balance Sheet and Profit &amp; Loss Account </DESCRIPTION> <REQUIREMENTFLAG>NON-MEDICAL</REQUIREMENTFLAG> <PropStatus>Not Taken Up</PropStatus> </Table> <Table> <DESCRIPTION>Copy of Bank Statement </DESCRIPTION> <REQUIREMENTFLAG>NON-MEDICAL</REQUIREMENTFLAG> <PropStatus>Not Taken Up</PropStatus> </Table> </ReqDtls> ";
                                inputpolicylist = sa.toString();
                                inputpolicylist = prsObj.parseXmlTag(
                                        inputpolicylist, "ReqDtls");

                                List<String> Node = prsObj.parseParentNode(
                                        inputpolicylist, "Table");

                                List<XMLDOCREQList> nodeData = prsObj
                                        .parseNodeElement_DOCREQ(Node);

                                lstPolicyList = new ArrayList<XMLDOCREQList>();
                                // lstPolicyList.clear();
                                nonMedicallstPolicyList = new ArrayList<XMLDOCREQList>();
                                nonMedicallstPolicyList.clear();
                                for (XMLDOCREQList node : nodeData) {
                                    lstPolicyList.add(node);
                                    if (node.getREQUIREMENTFLAG().equals(
                                            "NON-MEDICAL"))
                                        nonMedicallstPolicyList.add(node);
                                }

                            } else {
                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        showDialog("You are not authorised User");
                                    }
                                });

                            }

                        } else {
                            running = false;

                            runOnUiThread(new Runnable() {
                                public void run() {
                                    Toast.makeText(getApplicationContext(),
                                            "There is no Pending Requirement",
                                            Toast.LENGTH_LONG).show();
                                    mProgressDialog.dismiss();
                                }
                            });
                        }
                    } catch (Exception e) {

                        mProgressDialog.dismiss();
                        running = false;
                    }
                }

            } catch (Exception e) {

                mProgressDialog.dismiss();
                running = false;
            }
            return null;
        }

        @SuppressWarnings("rawtypes")
        protected void onPostExecute(String result) {
            if (mProgressDialog.isShowing())
                mProgressDialog.dismiss();
            // System.out.println("running======"+running+"lstPolicyList.size();:"+lstPolicyList.size());
            if (running) {
                int count = lstPolicyList.size();
                medicalReqList.clear();
                nonMedicalReqList.clear();
                String status = lstPolicyList.get(0).getPropStatus();
                System.out.println("Status======" + status);

                for (int i = 0; i < lstPolicyList.size(); i++) {
                    if (lstPolicyList.get(i).getREQUIREMENTFLAG()
                            .equals("MEDICAL")) {
                        medicalReqList.add(lstPolicyList.get(i)
                                .getDESCRIPTION());
                    } else
                        nonMedicalReqList.add(lstPolicyList.get(i)
                                .getDESCRIPTION());
                }

                if (medicalReqList.size() < 1)
                    ll_medical_req_header.setVisibility(View.GONE);
                if (medicalReqList != null) {
                    ArrayAdapter adapter = new ArrayAdapter<String>(
                            AdditionalUtilityActivity.this,
                            R.layout.medical_list_listview, medicalReqList);
                    listView.setAdapter(adapter);
                    justifyListViewHeightBasedOnChildren(listView);
                }
                count = nonMedicallstPolicyList.size();
                if ((status.equalsIgnoreCase("In Force")
                        || status.equalsIgnoreCase("Declined")
                        || status.equalsIgnoreCase("Cancelled")
                        || status.equalsIgnoreCase("Rejected") || status
                        .equalsIgnoreCase("Postponed"))) {
                    ll_document_upload.setVisibility(View.GONE);
                    showDialog("Proposal is " + status);
                } else if (status.equalsIgnoreCase("Not Taken Up")
                        || status
                        .equalsIgnoreCase("Policy Cancelled - Dishonour")) {
                    ll_document_upload.setVisibility(View.VISIBLE);
                    if (nonMedicalReqList != null) {
                        ArrayAdapter adapter = new ArrayAdapter<String>(
                                AdditionalUtilityActivity.this,
                                R.layout.medical_list_listview,
                                nonMedicalReqList);
                        lv_nonmedical_not_taken_up_list.setAdapter(adapter);
                        justifyListViewHeightBasedOnChildren(lv_nonmedical_not_taken_up_list);
                    }
                    if (medicalReqList.size() < 1) {
                        listView.setVisibility(View.GONE);
                        ll_medical_req_header.setVisibility(View.GONE);
                        tr_medical_requirement_header.setVisibility(View.GONE);
                    } else {
                        ll_medical_req_header.setVisibility(View.VISIBLE);
                        listView.setVisibility(View.VISIBLE);

                        tr_medical_requirement_header
                                .setVisibility(View.VISIBLE);
                    }
                    if (nonMedicalReqList.size() < 1) {
                        ll_medical_req_header.setVisibility(View.GONE);
                        ll_non_medical_requirements_not_taken_up
                                .setVisibility(View.GONE);
                    } else {
                        ll_medical_req_header.setVisibility(View.VISIBLE);
                        ll_non_medical_requirements_not_taken_up
                                .setVisibility(View.VISIBLE);
                    }
                    // tr_non_medical_header.setVisibility(View.VISIBLE);

                    tr_document_upload_proof_of_age.setVisibility(View.GONE);
                    tr_document_upload_proof_of_identity
                            .setVisibility(View.GONE);
                    tr_document_upload_proof_of_address
                            .setVisibility(View.GONE);
                    tr_income_proof.setVisibility(View.GONE);
                    tr_document_upload_proof_of_bank.setVisibility(View.GONE);
                    tr_document_upload_proof_of_others.setVisibility(View.GONE);
                    tr_document_upload_proof_of_eft.setVisibility(View.GONE);
                } else {
                    ll_document_upload.setVisibility(View.VISIBLE);

                    if (count < 1) {
                        // ll_document_upload.setVisibility(View.GONE);
                        tr_non_medical_header.setVisibility(View.GONE);

                        tr_document_upload_proof_of_age
                                .setVisibility(View.GONE);
                        tr_document_upload_proof_of_identity
                                .setVisibility(View.GONE);
                        tr_document_upload_proof_of_address
                                .setVisibility(View.GONE);
                        tr_income_proof.setVisibility(View.GONE);
                        tr_document_upload_proof_of_bank
                                .setVisibility(View.GONE);
                        tr_document_upload_proof_of_others
                                .setVisibility(View.GONE);
                        tr_document_upload_proof_of_eft
                                .setVisibility(View.GONE);
                    } else if (count == 1) {
                        des1_Age = nonMedicallstPolicyList.get(0)
                                .getDESCRIPTION();
                        req1 = nonMedicallstPolicyList.get(0)
                                .getREQUIREMENTFLAG();
                        if (req1.equalsIgnoreCase("Medical"))
                            tr_document_upload_proof_of_age
                                    .setVisibility(View.GONE);
                        else
                            tr_document_upload_proof_of_age
                                    .setVisibility(View.VISIBLE);
                        tr_document_upload_proof_of_identity
                                .setVisibility(View.GONE);
                        tr_document_upload_proof_of_address
                                .setVisibility(View.GONE);
                        tr_income_proof.setVisibility(View.GONE);
                        tr_document_upload_proof_of_bank
                                .setVisibility(View.GONE);
                        tr_document_upload_proof_of_others
                                .setVisibility(View.GONE);
                        tr_document_upload_proof_of_eft
                                .setVisibility(View.GONE);
                        txt_document_upload_proof_of_age.setText(des1_Age);

                    } else if (count == 2) {

                        des1_Age = nonMedicallstPolicyList.get(0)
                                .getDESCRIPTION();
                        req1 = nonMedicallstPolicyList.get(0)
                                .getREQUIREMENTFLAG();

                        des2_Identity = nonMedicallstPolicyList.get(1)
                                .getDESCRIPTION();
                        req2 = nonMedicallstPolicyList.get(1)
                                .getREQUIREMENTFLAG();
                        System.out.println(req1 + " " + req2);
                        if (req1.equalsIgnoreCase("Medical"))
                            tr_document_upload_proof_of_age
                                    .setVisibility(View.GONE);
                        else
                            tr_document_upload_proof_of_age
                                    .setVisibility(View.VISIBLE);
                        if (req2.equalsIgnoreCase("Medical"))
                            tr_document_upload_proof_of_identity
                                    .setVisibility(View.GONE);
                        else
                            tr_document_upload_proof_of_identity
                                    .setVisibility(View.VISIBLE);
                        tr_document_upload_proof_of_address
                                .setVisibility(View.GONE);
                        tr_income_proof.setVisibility(View.GONE);
                        tr_document_upload_proof_of_bank
                                .setVisibility(View.GONE);
                        tr_document_upload_proof_of_others
                                .setVisibility(View.GONE);
                        tr_document_upload_proof_of_eft
                                .setVisibility(View.GONE);
                        txt_document_upload_proof_of_age.setText(des1_Age);
                        txt_document_upload_proof_of_identity
                                .setText(des2_Identity);

                    } else if (count == 3) {

                        des1_Age = nonMedicallstPolicyList.get(0)
                                .getDESCRIPTION();
                        req1 = nonMedicallstPolicyList.get(0)
                                .getREQUIREMENTFLAG();

                        des2_Identity = nonMedicallstPolicyList.get(1)
                                .getDESCRIPTION();
                        req2 = nonMedicallstPolicyList.get(1)
                                .getREQUIREMENTFLAG();
                        des3_Address = nonMedicallstPolicyList.get(2)
                                .getDESCRIPTION();
                        req3 = nonMedicallstPolicyList.get(2)
                                .getREQUIREMENTFLAG();
                        if (req1.equalsIgnoreCase("Medical"))
                            tr_document_upload_proof_of_age
                                    .setVisibility(View.GONE);
                        else
                            tr_document_upload_proof_of_age
                                    .setVisibility(View.VISIBLE);
                        if (req2.equalsIgnoreCase("Medical"))
                            tr_document_upload_proof_of_identity
                                    .setVisibility(View.GONE);
                        else
                            tr_document_upload_proof_of_identity
                                    .setVisibility(View.VISIBLE);
                        if (req3.equalsIgnoreCase("Medical"))
                            tr_document_upload_proof_of_address
                                    .setVisibility(View.GONE);
                        else
                            tr_document_upload_proof_of_address
                                    .setVisibility(View.VISIBLE);
                        tr_income_proof.setVisibility(View.GONE);
                        tr_document_upload_proof_of_bank
                                .setVisibility(View.GONE);
                        tr_document_upload_proof_of_others
                                .setVisibility(View.GONE);
                        tr_document_upload_proof_of_eft
                                .setVisibility(View.GONE);
                        txt_document_upload_proof_of_age.setText(des1_Age);
                        txt_document_upload_proof_of_identity
                                .setText(des2_Identity);

                        txt_document_upload_proof_of_address
                                .setText(des3_Address);

                    } else if (count == 4) {
                        des1_Age = nonMedicallstPolicyList.get(0)
                                .getDESCRIPTION();
                        req1 = nonMedicallstPolicyList.get(0)
                                .getREQUIREMENTFLAG();

                        des2_Identity = nonMedicallstPolicyList.get(1)
                                .getDESCRIPTION();
                        req2 = nonMedicallstPolicyList.get(1)
                                .getREQUIREMENTFLAG();
                        des3_Address = nonMedicallstPolicyList.get(2)
                                .getDESCRIPTION();
                        req3 = nonMedicallstPolicyList.get(2)
                                .getREQUIREMENTFLAG();
                        des4_Income = nonMedicallstPolicyList.get(3)
                                .getDESCRIPTION();
                        req4 = nonMedicallstPolicyList.get(3)
                                .getREQUIREMENTFLAG();
                        if (req1.equalsIgnoreCase("Medical"))
                            tr_document_upload_proof_of_age
                                    .setVisibility(View.GONE);
                        else
                            tr_document_upload_proof_of_age
                                    .setVisibility(View.VISIBLE);
                        if (req2.equalsIgnoreCase("Medical"))
                            tr_document_upload_proof_of_identity
                                    .setVisibility(View.GONE);
                        else
                            tr_document_upload_proof_of_identity
                                    .setVisibility(View.VISIBLE);
                        if (req3.equalsIgnoreCase("Medical"))
                            tr_document_upload_proof_of_address
                                    .setVisibility(View.GONE);
                        else
                            tr_document_upload_proof_of_address
                                    .setVisibility(View.VISIBLE);
                        if (req4.equalsIgnoreCase("Medical"))
                            tr_income_proof.setVisibility(View.GONE);
                        else
                            tr_income_proof.setVisibility(View.VISIBLE);
                        tr_document_upload_proof_of_bank
                                .setVisibility(View.GONE);
                        tr_document_upload_proof_of_others
                                .setVisibility(View.GONE);
                        tr_document_upload_proof_of_eft
                                .setVisibility(View.GONE);
                        txt_document_upload_proof_of_age.setText(des1_Age);
                        txt_document_upload_proof_of_identity
                                .setText(des2_Identity);

                        txt_document_upload_proof_of_address
                                .setText(des3_Address);

                        txt_document_upload_proof_of_income
                                .setText(des4_Income);

                    } else if (count == 5) {
                        des1_Age = nonMedicallstPolicyList.get(0)
                                .getDESCRIPTION();
                        req1 = nonMedicallstPolicyList.get(0)
                                .getREQUIREMENTFLAG();

                        des2_Identity = nonMedicallstPolicyList.get(1)
                                .getDESCRIPTION();
                        req2 = nonMedicallstPolicyList.get(1)
                                .getREQUIREMENTFLAG();
                        des3_Address = nonMedicallstPolicyList.get(2)
                                .getDESCRIPTION();
                        req3 = nonMedicallstPolicyList.get(2)
                                .getREQUIREMENTFLAG();
                        des4_Income = nonMedicallstPolicyList.get(3)
                                .getDESCRIPTION();
                        req4 = nonMedicallstPolicyList.get(3)
                                .getREQUIREMENTFLAG();
                        des5_bank = nonMedicallstPolicyList.get(4)
                                .getDESCRIPTION();
                        req5 = nonMedicallstPolicyList.get(4)
                                .getREQUIREMENTFLAG();
                        if (req1.equalsIgnoreCase("Medical"))
                            tr_document_upload_proof_of_age
                                    .setVisibility(View.GONE);
                        else
                            tr_document_upload_proof_of_age
                                    .setVisibility(View.VISIBLE);
                        if (req2.equalsIgnoreCase("Medical"))
                            tr_document_upload_proof_of_identity
                                    .setVisibility(View.GONE);
                        else
                            tr_document_upload_proof_of_identity
                                    .setVisibility(View.VISIBLE);
                        if (req3.equalsIgnoreCase("Medical"))
                            tr_document_upload_proof_of_address
                                    .setVisibility(View.GONE);
                        else
                            tr_document_upload_proof_of_address
                                    .setVisibility(View.VISIBLE);
                        if (req4.equalsIgnoreCase("Medical"))
                            tr_income_proof.setVisibility(View.GONE);
                        else
                            tr_income_proof.setVisibility(View.VISIBLE);
                        if (req5.equalsIgnoreCase("Medical"))
                            tr_document_upload_proof_of_bank
                                    .setVisibility(View.GONE);
                        else
                            tr_document_upload_proof_of_bank
                                    .setVisibility(View.VISIBLE);
                        tr_document_upload_proof_of_others
                                .setVisibility(View.GONE);
                        tr_document_upload_proof_of_eft
                                .setVisibility(View.GONE);
                        txt_document_upload_proof_of_age.setText(des1_Age);
                        txt_document_upload_proof_of_identity
                                .setText(des2_Identity);

                        txt_document_upload_proof_of_address
                                .setText(des3_Address);

                        txt_document_upload_proof_of_income
                                .setText(des4_Income);

                        txt_document_upload_proof_of_bank.setText(des5_bank);

                    } else if (count == 6) {
                        des1_Age = nonMedicallstPolicyList.get(0)
                                .getDESCRIPTION();
                        req1 = nonMedicallstPolicyList.get(0)
                                .getREQUIREMENTFLAG();

                        des2_Identity = nonMedicallstPolicyList.get(1)
                                .getDESCRIPTION();
                        req2 = nonMedicallstPolicyList.get(1)
                                .getREQUIREMENTFLAG();
                        des3_Address = nonMedicallstPolicyList.get(2)
                                .getDESCRIPTION();
                        req3 = nonMedicallstPolicyList.get(2)
                                .getREQUIREMENTFLAG();
                        des4_Income = nonMedicallstPolicyList.get(3)
                                .getDESCRIPTION();
                        req4 = nonMedicallstPolicyList.get(3)
                                .getREQUIREMENTFLAG();
                        des5_bank = nonMedicallstPolicyList.get(4)
                                .getDESCRIPTION();
                        req5 = nonMedicallstPolicyList.get(4)
                                .getREQUIREMENTFLAG();
                        des6_others = nonMedicallstPolicyList.get(5)
                                .getDESCRIPTION();
                        req6 = nonMedicallstPolicyList.get(5)
                                .getREQUIREMENTFLAG();
                        if (req1.equalsIgnoreCase("Medical"))
                            tr_document_upload_proof_of_age
                                    .setVisibility(View.GONE);
                        else
                            tr_document_upload_proof_of_age
                                    .setVisibility(View.VISIBLE);
                        if (req2.equalsIgnoreCase("Medical"))
                            tr_document_upload_proof_of_identity
                                    .setVisibility(View.GONE);
                        else
                            tr_document_upload_proof_of_identity
                                    .setVisibility(View.VISIBLE);
                        if (req3.equalsIgnoreCase("Medical"))
                            tr_document_upload_proof_of_address
                                    .setVisibility(View.GONE);
                        else
                            tr_document_upload_proof_of_address
                                    .setVisibility(View.VISIBLE);
                        if (req4.equalsIgnoreCase("Medical"))
                            tr_income_proof.setVisibility(View.GONE);
                        else
                            tr_income_proof.setVisibility(View.VISIBLE);
                        if (req5.equalsIgnoreCase("Medical"))
                            tr_document_upload_proof_of_bank
                                    .setVisibility(View.GONE);
                        else
                            tr_document_upload_proof_of_bank
                                    .setVisibility(View.VISIBLE);

                        if (req6.equalsIgnoreCase("Medical"))
                            tr_document_upload_proof_of_others
                                    .setVisibility(View.GONE);
                        else
                            tr_document_upload_proof_of_others
                                    .setVisibility(View.VISIBLE);
                        tr_document_upload_proof_of_eft
                                .setVisibility(View.GONE);
                        txt_document_upload_proof_of_age.setText(des1_Age);
                        txt_document_upload_proof_of_identity
                                .setText(des2_Identity);

                        txt_document_upload_proof_of_address
                                .setText(des3_Address);

                        txt_document_upload_proof_of_income
                                .setText(des4_Income);

                        txt_document_upload_proof_of_bank.setText(des5_bank);
                        txt_document_upload_proof_of_others
                                .setText(des6_others);

                    } else if (count == 7) {
                        des1_Age = nonMedicallstPolicyList.get(0)
                                .getDESCRIPTION();
                        req1 = nonMedicallstPolicyList.get(0)
                                .getREQUIREMENTFLAG();

                        des2_Identity = nonMedicallstPolicyList.get(1)
                                .getDESCRIPTION();
                        req2 = nonMedicallstPolicyList.get(1)
                                .getREQUIREMENTFLAG();
                        des3_Address = nonMedicallstPolicyList.get(2)
                                .getDESCRIPTION();
                        req3 = nonMedicallstPolicyList.get(2)
                                .getREQUIREMENTFLAG();
                        des4_Income = nonMedicallstPolicyList.get(3)
                                .getDESCRIPTION();
                        req4 = nonMedicallstPolicyList.get(3)
                                .getREQUIREMENTFLAG();
                        des5_bank = nonMedicallstPolicyList.get(4)
                                .getDESCRIPTION();
                        req5 = nonMedicallstPolicyList.get(4)
                                .getREQUIREMENTFLAG();
                        des6_others = nonMedicallstPolicyList.get(5)
                                .getDESCRIPTION();
                        req6 = nonMedicallstPolicyList.get(5)
                                .getREQUIREMENTFLAG();
                        des7_EFT = nonMedicallstPolicyList.get(6)
                                .getDESCRIPTION();
                        req7 = nonMedicallstPolicyList.get(6)
                                .getREQUIREMENTFLAG();
                        if (req1.equalsIgnoreCase("Medical"))
                            tr_document_upload_proof_of_age
                                    .setVisibility(View.GONE);
                        else
                            tr_document_upload_proof_of_age
                                    .setVisibility(View.VISIBLE);
                        if (req2.equalsIgnoreCase("Medical"))
                            tr_document_upload_proof_of_identity
                                    .setVisibility(View.GONE);
                        else
                            tr_document_upload_proof_of_identity
                                    .setVisibility(View.VISIBLE);
                        if (req3.equalsIgnoreCase("Medical"))
                            tr_document_upload_proof_of_address
                                    .setVisibility(View.GONE);
                        else
                            tr_document_upload_proof_of_address
                                    .setVisibility(View.VISIBLE);
                        if (req4.equalsIgnoreCase("Medical"))
                            tr_income_proof.setVisibility(View.GONE);
                        else
                            tr_income_proof.setVisibility(View.VISIBLE);
                        if (req5.equalsIgnoreCase("Medical"))
                            tr_document_upload_proof_of_bank
                                    .setVisibility(View.GONE);
                        else
                            tr_document_upload_proof_of_bank
                                    .setVisibility(View.VISIBLE);

                        if (req6.equalsIgnoreCase("Medical"))
                            tr_document_upload_proof_of_others
                                    .setVisibility(View.GONE);
                        else
                            tr_document_upload_proof_of_others
                                    .setVisibility(View.VISIBLE);
                        if (req7.equalsIgnoreCase("Medical"))
                            tr_document_upload_proof_of_eft
                                    .setVisibility(View.GONE);
                        else
                            tr_document_upload_proof_of_eft
                                    .setVisibility(View.VISIBLE);
                        txt_document_upload_proof_of_age.setText(des1_Age);
                        txt_document_upload_proof_of_identity
                                .setText(des2_Identity);

                        txt_document_upload_proof_of_address
                                .setText(des3_Address);

                        txt_document_upload_proof_of_income
                                .setText(des4_Income);

                        txt_document_upload_proof_of_bank.setText(des5_bank);
                        txt_document_upload_proof_of_others
                                .setText(des6_others);
                        txt_document_upload_proof_of_eft.setText(des7_EFT);

                    }
                }
            }
        }
    }

    public void validate_age() {
        String message = "";
        if (des1_Age.equalsIgnoreCase("") && upload_Flag == 0) {
            message = "Please Select Age Proof Document First";
        } else if (ageProofBitmap == null && upload_Flag == 1) {
            message = "Please Capture Or Browse The Age Proof Document First";
        }

        AlertDialogMessage obj = new AlertDialogMessage();
        if (!message.equals(""))
            obj.dialog(AdditionalUtilityActivity.this, message, true);

    }

    public void validate_identity() {
        String message = "";
        if (des2_Identity.equalsIgnoreCase("") && upload_Flag == 0) {
            message = "Please Select Identity Proof Document First";
        } else if (identityProofBitmap == null && upload_Flag == 1) {
            message = "Please Capture Or Browse The Identity Proof Document First";
        }
        AlertDialogMessage obj = new AlertDialogMessage();
        if (!message.equals(""))
            obj.dialog(AdditionalUtilityActivity.this, message, true);

    }

    public void validate_address() {
        String message = "";
        if (des3_Address.equalsIgnoreCase("") && upload_Flag == 0) {
            message = "Please Select Address Proof Document First";
        } else if (addressProofBitmap == null && upload_Flag == 1) {
            message = "Please Capture Or Browse The Address Proof Document First";
        }
        AlertDialogMessage obj = new AlertDialogMessage();
        if (!message.equals(""))
            obj.dialog(AdditionalUtilityActivity.this, message, true);

    }

    public void validate_income() {
        String message = "";
        if (des4_Income.equalsIgnoreCase("") && upload_Flag == 0) {
            message = "Please Select Income Proof Document First";
        } else if (incomeProofBitmap == null && upload_Flag == 1) {
            message = "Please Capture Or Browse The Income Proof Document First";
        }
        AlertDialogMessage obj = new AlertDialogMessage();
        if (!message.equals(""))
            obj.dialog(AdditionalUtilityActivity.this, message, true);

    }

    public void validate_bankproof() {
        String message = "";
        if (des5_bank.equalsIgnoreCase("") && upload_Flag == 0) {
            message = "Please Select Bank Proof Document First";
        } else if (bankProofBitmap == null && upload_Flag == 1) {
            message = "Please Capture Or Browse The Bank Proof Document First";
        }
        AlertDialogMessage obj = new AlertDialogMessage();
        if (!message.equals(""))
            obj.dialog(AdditionalUtilityActivity.this, message, true);

    }

    public void servererror() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.loading_window);
        TextView text = dialog.findViewById(R.id.txtalertheader);
        text.setText("Server Not Responding,Try again..");
        Button dialogButton = dialog.findViewById(R.id.btnalert);
        dialogButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void validate_others() {
        String message = "";
        if (des6_others.equalsIgnoreCase("") && upload_Flag == 0) {
            message = "Please Enter other Proof Document name";
        } else if (ageProofBitmap == null && upload_Flag == 1) {
            message = "Please Capture Or Browse The Age Proof Document First";
        }
        AlertDialogMessage obj = new AlertDialogMessage();
        if (!message.equals(""))
            obj.dialog(AdditionalUtilityActivity.this, message, true);

    }

    public void validate_eft() {
        String message = "";
        if (des7_EFT.equalsIgnoreCase("") && upload_Flag == 0) {
            message = "Please Select EFT/Cheque/DD Proof Document First";
        } else if (eftProofBitmap == null && upload_Flag == 1) {
            message = "Please Capture Or Browse The EFT/Cheque/DD Proof Document First";
        }
        AlertDialogMessage obj = new AlertDialogMessage();
        if (!message.equals(""))
            obj.dialog(AdditionalUtilityActivity.this, message, true);

    }


    public String GetUserID() {
        String strCIFBDMUserId = "";
        try {
            strCIFBDMUserId = SimpleCrypto.decrypt("SBIL", db.GetCIFNo());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println("strCIFBDMUserId:" + strCIFBDMUserId);
        return strCIFBDMUserId;
    }

    public void justifyListViewHeightBasedOnChildren(ListView listView) {

        ListAdapter adapter = listView.getAdapter();

        if (adapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < adapter.getCount(); i++) {
            View listItem = adapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        LayoutParams par = listView.getLayoutParams();
        par.height = totalHeight
                + (listView.getDividerHeight() * (adapter.getCount() - 1));
        listView.setLayoutParams(par);
        listView.requestLayout();
    }

    public void showDialog(String msg) {

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.window_agreement);
        dialog.setCanceledOnTouchOutside(false);
        TextView text = dialog.findViewById(R.id.textMessage);
        text.setText(msg);
        Button dialogButton = dialog.findViewById(R.id.idbtnagreement);
        dialogButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {

                dialog.dismiss();
            }
        });
        dialog.show();

    }

    public void PasswordAlert() {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.loading_window);
        TextView text = dialog.findViewById(R.id.txtalertheader);
        text.setText("Enter Your Password..");
        Button dialogButton = dialog.findViewById(R.id.btnalert);
        dialogButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void DOBAlert() {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.loading_window);
        TextView text = dialog.findViewById(R.id.txtalertheader);
        text.setText("Please Select Your DOB..");
        Button dialogButton = dialog.findViewById(R.id.btnalert);
        dialogButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void Invalid_DOBAlert() {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.loading_window);
        TextView text = dialog.findViewById(R.id.txtalertheader);
        text.setText("Invalid date formate...");
        Button dialogButton = dialog.findViewById(R.id.btnalert);
        dialogButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void Alert20Digit(String str) {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.loading_window);
        TextView text = dialog.findViewById(R.id.txtalertheader);
        text.setText("You can  not enter more than 20 Character " + str);
        Button dialogButton = dialog.findViewById(R.id.btnalert);
        dialogButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void CifNoAlert() {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.loading_window);
        TextView text = dialog.findViewById(R.id.txtalertheader);
        text.setText("You are Not Authorised User..");
        Button dialogButton = dialog.findViewById(R.id.btnalert);
        dialogButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    @Override
    protected void onPrepareDialog(int id, Dialog dialog) {
        switch (id) {
            case DATE_DIALOG_ID:

                /*
                 * String str = edservicepass.getText().toString();
                 * System.out.println("str:"+str); if
                 * (!edservicepass.getText().toString().equalsIgnoreCase("")) {
                 *
                 * final SimpleDateFormat formatter = new SimpleDateFormat(
                 * "dd-MMMM-yyyy"); final Calendar cal = Calendar.getInstance();
                 * Date d1 = null; try { d1 = formatter.parse(str); } catch
                 * (ParseException e) { // TODO Auto-generated catch block
                 * e.printStackTrace(); }
                 *
                 * cal.setTime(d1);
                 *
                 * mYear = cal.get(Calendar.YEAR); mMonth = cal.get(Calendar.MONTH);
                 * mDay = cal.get(Calendar.DAY_OF_MONTH); }
                 */
                System.out.println("myear1:" + mYear + " month:" + mMonth
                        + " mday:" + mDay);

                ((DatePickerDialog) dialog).updateDate(mYear, mMonth, mDay);
                break;
        }
    }

    class DownloadFileAsyncProposalTracker extends
            AsyncTask<String, String, String> {

        private volatile boolean running = true;
        /*
         * private String proposalTrackerStatus = "", proposalTrackerReason =
         * "", proposalTrackerNO = "";
         */
        private ArrayList<XMLProposerTrackerList> lstProposerTrackerXml;
        String edProposalTrackerNo_text;

        @SuppressWarnings("deprecation")
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog(DIALOG_DOWNLOAD_PROGRESS);
            edProposalTrackerNo_text = edProposalTrackerNo.getText().toString();
        }

        @Override
        protected String doInBackground(String... aurl) {
            try {
                running = true;
                SoapObject request = null;


                // string strCode, string strEmailId, string strMobileNo, string
                // strAuthKey
                request = new SoapObject(NAMESPACE,
                        METHOD_NAME_PROPOSAL_TRACKER);
                request.addProperty("strProposalNo", edProposalTrackerNo_text);
                request.addProperty("strEmailId", agentEmail);
                request.addProperty("strMobileNo", agentMobile);
                request.addProperty("strAuthKey",
                        SimpleCrypto.encrypt("SBIL", agentPassword.trim()));

                mCommonMethods.TLSv12Enable();

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);
                envelope.dotNet = true;

                envelope.setOutputSoapObject(request);

                // allowAllSSL();

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);

                HttpTransportSE androidHttpTranport = new HttpTransportSE(URl);
                try {

                    androidHttpTranport.call(SOAP_ACTION_PROPOSAL_TRACKER,
                            envelope);
                    Object response = envelope.getResponse();

                    if (!response.toString().contentEquals("anyType{}")) {

                        SoapPrimitive sa = null;
                        try {
                            sa = (SoapPrimitive) envelope.getResponse();

                            inputpolicylist = sa.toString();
                            if (!sa.toString().equalsIgnoreCase("1")) {
                                ParseXML prsObj = new ParseXML();

                                // <ReqDtls><Table1><Status>Cancelled</Status><ProposalNo>53NA062275</ProposalNo>
                                // <Reason>Sent for
                                // Cancel/Refund</Reason></Table1></ReqDtls>

                                inputpolicylist = prsObj.parseXmlTag(
                                        inputpolicylist, "ReqDtls");

                                if (inputpolicylist != null) {

                                    /*
                                     * <ReqDtls> <Table>
                                     * <PL_PROP_NUM>45YA034636</PL_PROP_NUM>
                                     * <PR_FULL_NM>ABDUL ASAD ABDUL SALAM
                                     * SHAIKH</PR_FULL_NM>
                                     * <IR_ROLE>Proposer</IR_ROLE>
                                     * <PropStatus>Cancelled</PropStatus>
                                     * <Reason>Not Taken Up</Reason> </Table>
                                     *
                                     * <Table>
                                     * <PL_PROP_NUM>45YA034636</PL_PROP_NUM>
                                     * <PR_FULL_NM>ABDUL ASAD ABDUL SALAM
                                     * SHAIKH</PR_FULL_NM> <IR_ROLE>
                                     * LifeAssured</IR_ROLE>
                                     * <PropStatus>Cancelled</PropStatus>
                                     * <Reason>Not Taken Up</Reason> </Table>
                                     * </ReqDtls></string>
                                     */
                                    lstProposerTrackerXml = new ArrayList<XMLProposerTrackerList>();
                                    List<String> Node = prsObj.parseParentNode(
                                            inputpolicylist, "Table");

                                    List<XMLProposerTrackerList> nodeData = prsObj
                                            .parseNodeElement_Proposertracker(Node);

                                    lstProposerTrackerXml = new ArrayList<XMLProposerTrackerList>();
                                    // lstPolicyList.clear();
                                    System.out.println("node size:"
                                            + nodeData.size());
                                    for (XMLProposerTrackerList node : nodeData) {
                                        lstProposerTrackerXml.add(node);
                                        /*
                                         * if(node.getREQUIREMENTFLAG().equals(
                                         * "NON-MEDICAL"))
                                         * nonMedicallstPolicyList.add(node);
                                         */
                                    }

                                    System.out.println("inputpolicylist2:"
                                            + inputpolicylist);
                                    strProposalTrackerErrorCOde = inputpolicylist;

                                    System.out
                                            .println("strProposalTrackerErrorCOde:"
                                                    + strProposalTrackerErrorCOde);

                                    /*
                                     * if (strProposalTrackerErrorCOde != null)
                                     * { //inputpolicylist = sa.toString();
                                     *
                                     * proposalTrackerNO = new ParseXML()
                                     * .parseXmlTag(inputpolicylist,
                                     * "ProposalNo");
                                     *
                                     * proposalTrackerStatus = new ParseXML()
                                     * .parseXmlTag(inputpolicylist, "Status");
                                     * proposalTrackerReason = new ParseXML()
                                     * .parseXmlTag(inputpolicylist, "Reason");
                                     *
                                     * if(proposalTrackerReason == null) {
                                     * proposalTrackerReason = ""; }
                                     *
                                     * System.out.println("proposalTrackerReason:"
                                     * + proposalTrackerReason +
                                     * " :proposalTrackerStatus:" +
                                     * proposalTrackerStatus +
                                     * " :proposalTrackerNO:" +
                                     * proposalTrackerNO);
                                     *
                                     * } else { //
                                     * txterrordesc.setText("No Data"); }
                                     */
                                } else {
                                    strProposalTrackerErrorCOde = "1";
                                }
                            } else {
                                running = false;
                                mProgressDialog.dismiss();
                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        Toast.makeText(getApplicationContext(),
                                                "Proposal Number is not valid",
                                                Toast.LENGTH_LONG).show();

                                    }
                                });
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
                    } else {

                    }

                } catch (IOException e) {
                    try {
                        throw (e);
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                    mProgressDialog.dismiss();
                    running = false;
                } catch (XmlPullParserException e) {
                    try {
                        throw (e);
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                    mProgressDialog.dismiss();
                    running = false;
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
        protected void onProgressUpdate(String... progress) {
            Log.d("ANDRO_ASYNC", progress[0]);
            mProgressDialog.setProgress(Integer.parseInt(progress[0]));
        }

        @SuppressWarnings("deprecation")
        @Override
        protected void onPostExecute(String unused) {
            dismissDialog(DIALOG_DOWNLOAD_PROGRESS);
            if (running) {
                if (strProposalTrackerErrorCOde != null) {
                    // txterrordescpolicydetail.setText("");

                    txtProposalTrackerProposalNo.setText(lstProposerTrackerXml
                            .get(0).getProposerNumber());
                    txtProposalTrackerReason.setText(lstProposerTrackerXml.get(
                            0).getReason());
                    txtProposalTrackerStatus.setText(lstProposerTrackerXml.get(
                            0).getStatus());
                    txtProposalTrackerLifeAssuredName
                            .setText(lstProposerTrackerXml.get(0)
                                    .getLifeAssuredName());
                    txtProposalTrackerPolicyHolderName
                            .setText(lstProposerTrackerXml.get(0)
                                    .getPolicyHolderName());

                    tblProposarTrackerStatus.getLayoutParams().height = LayoutParams.WRAP_CONTENT;
                    tblProposarTrackerStatus.requestLayout();
                } else {
                    // txterrordescpolicydetail.setText("No Record Found");

                    txtProposalTrackerProposalNo.setText("");
                    txtProposalTrackerReason.setText("");
                    txtProposalTrackerStatus.setText("");

                    tblProposarTrackerStatus.getLayoutParams().height = 0;
                    tblProposarTrackerStatus.requestLayout();
                }
            } else {
                servererror();
                txtProposalTrackerProposalNo.setText("");
                txtProposalTrackerReason.setText("");
                txtProposalTrackerStatus.setText("");
                txtProposalTrackerLifeAssuredName.setText("");
                txtProposalTrackerPolicyHolderName.setText("");

                tblProposarTrackerStatus.getLayoutParams().height = 0;
                tblProposarTrackerStatus.requestLayout();
            }
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = getIntent();
        String fromHome = intent.getStringExtra("fromHome");

        if (fromHome != null && fromHome.equalsIgnoreCase("N")) {
            super.onBackPressed();
        } else {
            Intent i = new Intent(AdditionalUtilityActivity.this,
                    DocumentsUploadActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }

        if (taskProposalTracker != null) {
            taskProposalTracker.cancel(true);
        }

        if (service != null) {
            service.cancel(true);
        }

        if (mAsyncUploadFileCommon != null) {
            mAsyncUploadFileCommon.cancel(true);
        }

        if (ProposalCheakService != null) {
            ProposalCheakService.cancel(true);
        }

        if (proposalCheakServiceRequirement != null) {
            proposalCheakServiceRequirement.cancel(true);
        }
        super.onDestroy();
    }

    private void createSoapRequestToUploadDoc(){

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME_UPLOAD_FILE);
        // get CPF pdf name
        // for(int i =01;i<15 ;i++){

        // Get Channel Detail
        // M_ChannelDetails channelDetail =
        // db.getChannelDetail(agentId);
        //
        // String email_Id = channelDetail.getEmail_Id();
        // String mobile_No = channelDetail.getMobile_No();
        // String email_Id = "mukesh.kumar@sbilife.co.in";
        // String mobile_No = "9434197714";

        QuotationNumber = "OL123456789";

        strWithoutIAId = "990134795";

        request.addProperty("f", bytes);

        if (addflag < 10) {
            request.addProperty("fileName", ProposalNumber + "_R0"
                    + addflag + str_extension);
        } else if (addflag >= 10) {
            request.addProperty("fileName", ProposalNumber + "_R"
                    + addflag + str_extension);
        }


        request.addProperty("qNo", ProposalNumber);
        request.addProperty("agentCode", agentcode);
        request.addProperty("strEmailId", agentEmail);
        request.addProperty("strMobileNo", agentMobile);
        request.addProperty("strAuthKey",
                SimpleCrypto.encrypt("SBIL", agentPassword.trim()));

        mAsyncUploadFileCommon = new AsyncUploadFile_Common(context, this, request, METHOD_NAME_UPLOAD_FILE);
        mAsyncUploadFileCommon.execute();
    }

    @Override
    public void onUploadComplete(Boolean result) {

        if (result) {

            obj.dialog(context, "Document Uploaded SuccessFully!!",
                    true);

            ClearDocumentsPDF();
            DeleteFiles();

            if (increment == 1) {
                img_btn_document_upload_preview_image_age
                        .setVisibility(View.INVISIBLE);
                img_btn_document_delete_age
                        .setVisibility(View.INVISIBLE);

                img_btn_document_upload_click_image_age
                        .setImageDrawable(getResources().getDrawable(
                                R.drawable.ibtn_camera));

                img_btn_document_upload_click_browse_age
                        .setImageDrawable(getResources().getDrawable(
                                R.drawable.ibtn_browsedoc));

                /*
                 * String DocumentName =
                 * spnr_document_upload_document_age
                 * .getSelectedItem().toString();
                 */
                flag_cancel_age = 0;
                File mypath = mStorageUtils.createFileToAppSpecificDir(context, ProposalNumber + "_"
                        + "X" + increment + str_extension);
                String FileName = "";
                if (addflag < 10) {
                    FileName = ProposalNumber + "_" + "R0" + addflag + str_extension;
                } else if (addflag >= 10) {
                    FileName = ProposalNumber + "_" + "R" + addflag + str_extension;
                }

                String path = mypath.toString();

                Calendar c = Calendar.getInstance();
                System.out.println("Current time => " + c.getTime());

                SimpleDateFormat df = new SimpleDateFormat(
                        "yyyy-MM-dd HH:mm:ss");
                String UploadDate = df.format(c.getTime());

                try {
                    saveDataLocally(des1_Age.replace(":", ""), req1,
                            path, FileName, UploadDate);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } else if (increment == 2) {
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

                String DocumentName = spnr_document_upload_document_identity
                        .getSelectedItem().toString();
                flag_cancel_identity = 0;
                File mypath = mStorageUtils.createFileToAppSpecificDir(context, ProposalNumber + "_"
                        + "X" + increment + str_extension);
                String FileName = "";
                if (addflag < 10) {
                    FileName = ProposalNumber + "_" + "R0" + addflag + str_extension;
                } else if (addflag >= 10) {
                    FileName = ProposalNumber + "_" + "R" + addflag
                            + str_extension;
                }
                String path = mypath.toString();
                Calendar c = Calendar.getInstance();
                System.out.println("Current time => " + c.getTime());

                SimpleDateFormat df = new SimpleDateFormat(
                        "yyyy-MM-dd HH:mm:ss");
                String UploadDate = df.format(c.getTime());

                try {
                    saveDataLocally(des2_Identity.replace(":", ""),
                            req2, path, FileName, UploadDate);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } else if (increment == 3) {
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

                /*
                 * String DocumentName =
                 * spnr_document_upload_document_address
                 * .getSelectedItem().toString();
                 */
                flag_cancel_address = 0;
                File mypath = mStorageUtils.createFileToAppSpecificDir(context, ProposalNumber + "_"
                        + "X" + increment + str_extension);
                String FileName = "";
                if (addflag < 10) {
                    FileName = ProposalNumber + "_" + "R0" + addflag
                            + str_extension;
                } else if (addflag >= 10) {
                    FileName = ProposalNumber + "_" + "R" + addflag
                            + str_extension;
                }
                String path = mypath.toString();
                Calendar c = Calendar.getInstance();
                System.out.println("Current time => " + c.getTime());

                SimpleDateFormat df = new SimpleDateFormat(
                        "yyyy-MM-dd HH:mm:ss");
                String UploadDate = df.format(c.getTime());
                try {
                    saveDataLocally(des3_Address.replace(":", ""),
                            req3, path, FileName, UploadDate);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } else if (increment == 4) {
                img_btn_document_upload_click_preview_image_income
                        .setVisibility(View.INVISIBLE);
                img_btn_document_delete_income
                        .setVisibility(View.INVISIBLE);

                img_btn_document_upload_click_image_income
                        .setImageDrawable(getResources().getDrawable(
                                R.drawable.ibtn_camera));

                img_btn_document_upload_click_browse_income
                        .setImageDrawable(getResources().getDrawable(
                                R.drawable.ibtn_browsedoc));

                /*
                 * String DocumentName =
                 * spnr_document_upload_document_income
                 * .getSelectedItem().toString();
                 */
                flag_cancel_income = 0;
                File mypath = mStorageUtils.createFileToAppSpecificDir(context, ProposalNumber + "_"
                        + "X" + increment + str_extension);
                String FileName = "";
                if (addflag < 10) {
                    FileName = ProposalNumber + "_" + "R0" + addflag
                            + str_extension;
                } else if (addflag >= 10) {
                    FileName = ProposalNumber + "_" + "R" + addflag
                            + str_extension;
                }
                String path = mypath.toString();
                Calendar c = Calendar.getInstance();
                System.out.println("Current time => " + c.getTime());

                SimpleDateFormat df = new SimpleDateFormat(
                        "yyyy-MM-dd HH:mm:ss");
                String UploadDate = df.format(c.getTime());

                try {
                    saveDataLocally(des4_Income.replace(":", ""), req4,
                            path, FileName, UploadDate);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } else if (increment == 5) {
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

                /*
                 * String DocumentName =
                 * edt_document_upload_document_others
                 * .getText().toString();
                 */
                flag_cancel_others = 0;
                if (proposer_IsUnder.equalsIgnoreCase("nri")) {
                    // otherProofFlag = true;
                }
                File mypath = mStorageUtils.createFileToAppSpecificDir(context, ProposalNumber + "_"
                        + "X" + increment + str_extension);
                String FileName = "";
                if (addflag < 10) {
                    FileName = ProposalNumber + "_" + "R0" + addflag
                            + str_extension;
                } else if (addflag >= 10) {
                    FileName = ProposalNumber + "_" + "R" + addflag
                            + str_extension;
                }
                String path = mypath.toString();
                Calendar c = Calendar.getInstance();
                System.out.println("Current time => " + c.getTime());

                SimpleDateFormat df = new SimpleDateFormat(
                        "yyyy-MM-dd HH:mm:ss");
                String UploadDate = df.format(c.getTime());

                try {
                    saveDataLocally(des6_others.replace(":", ""), req6,
                            path, FileName, UploadDate);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } else if (increment == 6) {
                img_btn_document_upload_click_preview_image_bank
                        .setVisibility(View.INVISIBLE);
                img_btn_document_delete_bank
                        .setVisibility(View.INVISIBLE);

                img_btn_document_upload_click_image_bank
                        .setImageDrawable(getResources().getDrawable(
                                R.drawable.ibtn_camera));

                img_btn_document_upload_click_browse_bank
                        .setImageDrawable(getResources().getDrawable(
                                R.drawable.ibtn_browsedoc));

                /*
                 * String DocumentName =
                 * spnr_document_upload_document_bank
                 * .getSelectedItem().toString();
                 */
                flag_cancel_bank = 0;
                File mypath = mStorageUtils.createFileToAppSpecificDir(context, ProposalNumber + "_"
                        + "X" + increment + str_extension);
                String FileName = "";
                if (addflag < 10) {
                    FileName = ProposalNumber + "_" + "R0" + addflag
                            + str_extension;
                } else if (addflag >= 10) {
                    FileName = ProposalNumber + "_" + "R" + addflag
                            + str_extension;
                }
                String path = mypath.toString();
                Calendar c = Calendar.getInstance();
                System.out.println("Current time => " + c.getTime());

                SimpleDateFormat df = new SimpleDateFormat(
                        "yyyy-MM-dd HH:mm:ss");
                String UploadDate = df.format(c.getTime());

                try {
                    saveDataLocally(des5_bank.replace(":", ""), req5,
                            path, FileName, UploadDate);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } else if (increment == 7) {
                img_btn_document_upload_preview_image_eft
                        .setVisibility(View.INVISIBLE);
                img_btn_document_delete_eft
                        .setVisibility(View.INVISIBLE);

                img_btn_document_upload_click_image_eft
                        .setImageDrawable(getResources().getDrawable(
                                R.drawable.ibtn_camera));

                img_btn_document_upload_click_browse_eft
                        .setImageDrawable(getResources().getDrawable(
                                R.drawable.ibtn_browsedoc));

                /*
                 * String DocumentName =
                 * spnr_document_upload_document_eft
                 * .getSelectedItem().toString();
                 */
                flag_cancel_eft = 0;
                File mypath = mStorageUtils.createFileToAppSpecificDir(context, ProposalNumber + "_"
                        + "X" + increment + str_extension);
                String FileName = "";
                if (addflag < 10) {
                    FileName = ProposalNumber + "_" + "R0" + addflag
                            + str_extension;
                } else if (addflag >= 10) {
                    FileName = ProposalNumber + "_" + "R" + addflag
                            + str_extension;
                }
                String path = mypath.toString();
                Calendar c = Calendar.getInstance();
                System.out.println("Current time => " + c.getTime());

                SimpleDateFormat df = new SimpleDateFormat(
                        "yyyy-MM-dd HH:mm:ss");
                String UploadDate = df.format(c.getTime());

                try {
                    saveDataLocally(des7_EFT.replace(":", ""), req7,
                            path, FileName, UploadDate);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

        }
        else {
            mCommonMethods.showToast(context, mCommonMethods.WEEK_INTERNET_MESSAGE);
        }
    }
}
