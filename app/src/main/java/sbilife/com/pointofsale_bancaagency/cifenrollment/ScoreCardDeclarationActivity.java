package sbilife.com.pointofsale_bancaagency.cifenrollment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.text.Html;
import android.util.Base64;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

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
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import sbilife.com.pointofsale_bancaagency.DatabaseHelper;
import sbilife.com.pointofsale_bancaagency.ParseXML;
import sbilife.com.pointofsale_bancaagency.R;
import sbilife.com.pointofsale_bancaagency.ServiceURL;
import sbilife.com.pointofsale_bancaagency.cifenrollment.phase1.AsyncUploadFile_CIF;
import sbilife.com.pointofsale_bancaagency.cifenrollment.phase1.CIFEnrollmentPFActivity;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;
import sbilife.com.pointofsale_bancaagency.common.CompressImage;
import sbilife.com.pointofsale_bancaagency.common.FileLoader;
import sbilife.com.pointofsale_bancaagency.common.StorageUtils;

public class ScoreCardDeclarationActivity extends AppCompatActivity implements View.OnClickListener, AsyncUploadFile_CIF.Interface_Upload_CIF_Files {

    private final String NAMESPACE = "http://tempuri.org/";
    private final String METHOD_NAME_SCORE_CARD_VALIDATE_URN = "validateURN";
    private final String METHOD_NAME_DOWNLOAD_SIGN = "getSignCIFenroll_smrt";
    private final String METHOD_NAME_UPLOAD_TIME_URN = "saveTCC_ScorecardDetail_CIFenroll";
    private final String METHOD_NAME_UPLOAD_QUESTIONS_DOC = "UploadFile_CIFEnroll_URN";
    private final String METHOD_NAME_GET_NAME_BY_URN = "getName_CIFEnroll";
    private final String FILE_HSC_DOC_NAME = "_HSC";
    private final String FILE_DEGREE_DOC_NAME = "_Degree";
    private final String FILE_SCORE_CARD_NAME = "_SCORE_CARD";
    private final String QUESTIONS_DOC = "questions_";
    private final String SCHEDULE3_DOC = "_SCHEDULE3";
    private final int REQUEST_CODE_PICK_PHOTO_FILE = 3;
    private Context mContext;
    private CommonMethods mCommonMethods;
    private StorageUtils mStorageUtils;
    private String str_urn_no = "", str_place = "", str_customer_sign = "";
    private CheckBox chk_tcc_declaration;
    private Button btn_tcc_declaration_submit, btn_validate_urn;
    private ProgressDialog mProgressDialog;
    private AsyncUploadFile_CIF mAsyncUploadFile_cif;
    private UploadTimeURN mUploadTimeURN;
    private File mScoreCardFile, mHSCFile, mDegreeFile, mSchedule3File;
    private byte[] mScoreCardBytes, docBytes;
    private String UPLOAD_TYPE = "";
    private ImageButton ib_score_card_docs_view, imgbtn_score_card_camera, imgbtn_score_card_browse, imgbtn_score_card_upload,
            ib_score_card_hsc_doc_view, imgbtn_tcc_education_doc_hsc_camera, imgbtn_tcc_education_doc_hsc_browse,
            imgbtn_tcc_education_doc_hsc_upload, ib_score_card_degree_doc_view, imgbtn_tcc_education_doc_degree_camera,
            imgbtn_tcc_education_doc_degree_browse, imgbtn_tcc_education_doc_degree_upload, imgbtn_tcc_declaration_cust_sign;

    private Spinner spnr_tcc_education_doc_hsc, spnr_tcc_education_doc_degree;
    private EditText edt_score_card_urn, edt_tcc_declaration_place;
    private LinearLayout ll_score_card_declaration;
    private AsynchValidateURN mAsynchValidateURN;

    private boolean is_score_card_upload = false;
    private ActivityResultLauncher<Intent> mBrowseDocLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
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

                                    String imageFileName = str_urn_no + UPLOAD_TYPE + str_extension;
                                    new FileLoader(mContext, StorageUtils.DIRECT_DIRECTORY_CIF, imageFileName,
                                            new FileLoader.FileLoaderResponce() {
                                                @Override
                                                public void fileLoadFinished(File fileOutput) {
                                                    if (fileOutput != null) {
                                                        if (UPLOAD_TYPE.equals(FILE_SCORE_CARD_NAME)) {
                                                            mScoreCardFile = fileOutput;
                                                            CompressImage.compressImage(mScoreCardFile.getPath());
                                                            long size = mScoreCardFile.length();
                                                            double kilobyte = size / 1024;

                                                            //2 MB valiadation
                                                            if (kilobyte < mCommonMethods.FILE_UPLOAD_RESTRICT_SIZE) {
                                                                //get extension of final file created
                                                                //str_extension = mScoreCardFile.getPath().substring(mScoreCardFile.getPath().lastIndexOf("."));

                                                                imgbtn_score_card_browse.setImageDrawable(getResources().getDrawable(R.drawable.checkedbrowse));
                                                                imgbtn_score_card_camera.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_camera));
                                                            } else {
                                                                mScoreCardFile = null;
                                                                mCommonMethods.showMessageDialog(mContext, mCommonMethods.FILE_UPLOAD_RESTRICT_SIZE_MSG);
                                                            }
                                                        } else if (UPLOAD_TYPE.equals(FILE_HSC_DOC_NAME)) {
                                                            mHSCFile = fileOutput;
                                                            CompressImage.compressImage(mHSCFile.getPath());
                                                            long size = mHSCFile.length();
                                                            double kilobyte = size / 1024;

                                                            //2 MB valiadation
                                                            if (kilobyte < mCommonMethods.FILE_UPLOAD_RESTRICT_SIZE) {
                                                                imgbtn_tcc_education_doc_hsc_browse
                                                                        .setImageDrawable(getResources().getDrawable(R.drawable.checkedbrowse));
                                                                imgbtn_tcc_education_doc_hsc_camera.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_camera));
                                                            } else {
                                                                mHSCFile = null;
                                                                mCommonMethods.showMessageDialog(mContext, mCommonMethods.FILE_UPLOAD_RESTRICT_SIZE_MSG);
                                                            }
                                                        } else if (UPLOAD_TYPE.equals(FILE_DEGREE_DOC_NAME)) {
                                                            mDegreeFile = fileOutput;
                                                            CompressImage.compressImage(mDegreeFile.getPath());
                                                            long size = mDegreeFile.length();
                                                            double kilobyte = size / 1024;

                                                            //2 MB valiadation
                                                            if (kilobyte < mCommonMethods.FILE_UPLOAD_RESTRICT_SIZE) {
                                                                imgbtn_tcc_education_doc_degree_browse
                                                                        .setImageDrawable(getResources().getDrawable(R.drawable.checkedbrowse));
                                                                imgbtn_tcc_education_doc_degree_camera.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_camera));
                                                            } else {
                                                                mDegreeFile = null;
                                                                mCommonMethods.showMessageDialog(mContext, mCommonMethods.FILE_UPLOAD_RESTRICT_SIZE_MSG);
                                                            }
                                                        }

                                                    } else {
                                                        mCommonMethods.showToast(mContext, "File Not found..");
                                                    }
                                                }
                                            }).execute(mSelectedUri);
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
        setContentView(R.layout.fragment_tcc_declartion);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.cifenrollment_pf_activity_title);

        /*str_urn_no = getIntent().getStringExtra("URN");
        str_place = getIntent().getStringExtra("PLACE");*/

        initialisation();
    }

    private void initialisation() {

        mContext = this;
        mCommonMethods = new CommonMethods();
        mStorageUtils = new StorageUtils();
        DatabaseHelper db = new DatabaseHelper(mContext);

        edt_score_card_urn = findViewById(R.id.edt_score_card_urn);

        btn_validate_urn = findViewById(R.id.btn_validate_urn);
        btn_validate_urn.setOnClickListener(this);

        ll_score_card_declaration = findViewById(R.id.ll_score_card_declaration);

        mCommonMethods.setApplicationToolbarMenu1(this, "CIF on Boarding");

        TextView txt_tcc_declaration = findViewById(R.id.txt_tcc_declaration);
        chk_tcc_declaration = findViewById(R.id.chk_tcc_declaration);
        btn_tcc_declaration_submit = findViewById(R.id.btn_tcc_declaration_submit);
        btn_tcc_declaration_submit.setOnClickListener(this);

        edt_tcc_declaration_place = findViewById(R.id.edt_tcc_declaration_place);
        TextView txt_tcc_declaration_date = findViewById(R.id.txt_tcc_declaration_date);

        ib_score_card_docs_view = findViewById(R.id.ib_score_card_docs_view);
        imgbtn_score_card_camera = findViewById(R.id.imgbtn_score_card_camera);
        imgbtn_score_card_browse = findViewById(R.id.imgbtn_score_card_browse);
        imgbtn_score_card_upload = findViewById(R.id.imgbtn_score_card_upload);

        ib_score_card_docs_view.setOnClickListener(this);
        imgbtn_score_card_camera.setOnClickListener(this);
        imgbtn_score_card_browse.setOnClickListener(this);
        imgbtn_score_card_upload.setOnClickListener(this);

        ArrayList<String> lstDocs = new ArrayList<>();
        lstDocs.add("Select Document");
        lstDocs.add("Mark Sheet");
        lstDocs.add("Passing Certificate");

        spnr_tcc_education_doc_hsc = findViewById(R.id.spnr_tcc_education_doc_hsc);
        mCommonMethods.fillSpinnerValue(mContext, spnr_tcc_education_doc_hsc, lstDocs);

        ib_score_card_hsc_doc_view = findViewById(R.id.ib_score_card_hsc_doc_view);
        imgbtn_tcc_education_doc_hsc_camera = findViewById(R.id.imgbtn_tcc_education_doc_hsc_camera);
        imgbtn_tcc_education_doc_hsc_browse = findViewById(R.id.imgbtn_tcc_education_doc_hsc_browse);
        imgbtn_tcc_education_doc_hsc_upload = findViewById(R.id.imgbtn_tcc_education_doc_hsc_upload);

        ib_score_card_hsc_doc_view.setOnClickListener(this);
        imgbtn_tcc_education_doc_hsc_camera.setOnClickListener(this);
        imgbtn_tcc_education_doc_hsc_browse.setOnClickListener(this);
        imgbtn_tcc_education_doc_hsc_upload.setOnClickListener(this);

        spnr_tcc_education_doc_degree = findViewById(R.id.spnr_tcc_education_doc_degree);
        mCommonMethods.fillSpinnerValue(mContext, spnr_tcc_education_doc_degree, lstDocs);

        ib_score_card_degree_doc_view = findViewById(R.id.ib_score_card_degree_doc_view);
        imgbtn_tcc_education_doc_degree_camera = findViewById(R.id.imgbtn_tcc_education_doc_degree_camera);
        imgbtn_tcc_education_doc_degree_browse = findViewById(R.id.imgbtn_tcc_education_doc_degree_browse);
        imgbtn_tcc_education_doc_degree_upload = findViewById(R.id.imgbtn_tcc_education_doc_degree_upload);

        ib_score_card_degree_doc_view.setOnClickListener(this);
        imgbtn_tcc_education_doc_degree_camera.setOnClickListener(this);
        imgbtn_tcc_education_doc_degree_browse.setOnClickListener(this);
        imgbtn_tcc_education_doc_degree_upload.setOnClickListener(this);

        //current date
        Calendar cal = Calendar.getInstance();
        String mont = ((cal.get(Calendar.MONTH) + 1) < 10 ? "0" : "") + (cal.get(Calendar.MONTH) + 1);
        String day = (cal.get(Calendar.DAY_OF_MONTH) < 10 ? "0" : "") + cal.get(Calendar.DAY_OF_MONTH);

        txt_tcc_declaration_date.setText(day + "-" + mont + "-" + cal.get(Calendar.YEAR));

        imgbtn_tcc_declaration_cust_sign = findViewById(R.id.imgbtn_tcc_declaration_cust_sign);

        String str_declaration = "<b>A. General Code Of Conduct</b><br/>"
                + "<pre><b>1. Every corporate agent shall follow recognised standards of professional conduct and discharge their duties in the interest of the policyholders. While doing so -</b></pre><br/>"
                + "<pre>&nbsp &nbsp &nbsp(a) Conduct its dealings with clients with utmost good faith and integrity at all times.</pre><br/>"
                + "<pre>&nbsp &nbsp &nbsp(b) Act with care and diligence.</pre><br/>"
                + "<pre>&nbsp &nbsp &nbsp(c) Ensure that the client understands his relationship with the corporate agent and on whose behalf the corporate agent is acting.</pre><br/>"
                + "<pre>&nbsp &nbsp &nbsp(d) Treat all information supplied by the prospective clients as completely confidential to themselves and to the insurer(s) to which the business is being offered.</pre><br/>"
                + "<pre>&nbsp &nbsp &nbsp(e) Take  appropriate  steps  to  maintain  the  security  of  confidential  documents  in  their possession.</pre><br/>"
                + "<pre>&nbsp &nbsp &nbsp(f) No director of a company or a partner of a firm or the chief executive or a principal officer or a specified person shall hold similar position with another corporate agent.</pre><br/>"
                + "<pre><b>2. Every Corporate Agent shall</b></pre><br/>"
                + "<pre>&nbsp &nbsp &nbsp(a) Be responsible for all acts of omission and commission of its principal officer and every specified person.</pre><br/>"
                + "<pre>&nbsp &nbsp &nbsp(b) Ensure that the principal officer and all specified persons are properly trained, skilled and knowledgeable in the insurance products they market.</pre><br/>"
                + "<pre>&nbsp &nbsp &nbsp(c) Ensure that the principal officer and the specified person do not make to the prospect any misrepresentation on policy benefits and returns available under the policy.</pre><br/>"
                + "<pre>&nbsp &nbsp &nbsp(d) Ensure that no prospect is forced to buy an insurance product.</pre><br/>"
                + "<pre>&nbsp &nbsp &nbsp(e) Give adequate pre-sales and post-sales advice to the insured in respect of the insurance product.</pre><br/>"
                + "<pre>&nbsp &nbsp &nbsp(f) Extend all possible help and cooperation to an insured in completion of all formalities and documentation in the event of a claim.</pre><br/>"
                + "<pre>&nbsp &nbsp &nbsp(g) Give due publicity to the fact that the corporate agent does not underwrite the risk or act as an insurer.</pre><br/>"
                + "<pre>&nbsp &nbsp &nbsp(h) Enter into agreements with the insurers in which the duties and responsibilities of both are defined.</pre><br/>"
                + "<b>B. Pre-sale Code Of Conduct</b><br/>"
                + "<pre><b>1. Every corporate agent or principal officer or a specified person shall also follow the code of conduct specified below :</b></pre><br/>"
                + "<pre>&nbsp &nbsp &nbsp<b>Every corporate agent/ principal officer / specified person shall, </b></pre><br/>"
                + "<pre>&nbsp &nbsp &nbsp(a) Identify himself and disclose his registration/ certificate to the prospect on demand.</pre><br/>"
                + "<pre>&nbsp &nbsp &nbsp(b) Disseminate the requisite information in respect of insurance products offered for sale by the insurers with whom they have arrangement and take into account  the needs of the prospect while recommending a specific insurance plan.</pre><br/>"
                + "<pre>&nbsp &nbsp &nbsp(c) Disclose the scales of commission in respect of the insurance product offered for sale, if asked by the prospect.</pre><br/>"
                + "<pre>&nbsp &nbsp &nbsp(d) Indicate the premium to be charged by the insurer for the insurance product offered for sale.</pre><br/>"
                + "<pre>&nbsp &nbsp &nbsp(e) Explain to the prospect the nature of information required in the proposal form by the insurer, and also the importance of disclosure of material information in the purchase of an insurance contract.</pre><br/>"
                + "<pre>&nbsp &nbsp &nbsp(f) Bring to the notice of the insurer any adverse habits or income inconsistency of the prospect, in the form of a Confidential Report along with every proposal submitted to the insurer, and any material fact that may adversely affect the underwriting decision of the insurer as regards acceptance of the proposal, by making all reasonable enquiries about the prospect.</pre><br/>"
                + "<pre>&nbsp &nbsp &nbsp(g) Inform promptly the prospect about the acceptance or rejection of the proposal by the insurer.</pre><br/>"
                + "<pre>&nbsp &nbsp &nbsp(h) Obtain the requisite documents at the time of filing the proposal form with the insurer and other documents subsequently asked for by the insurer for completion of the proposal.</pre><br/>"
                + "<pre>&nbsp &nbsp &nbsp<b>No corporate agent/ principal officer / specified person shall, </b></pre><br/>"
                + "<pre>&nbsp &nbsp &nbsp(a) Solicit or procure insurance business without holding a valid registration/ certificate.</pre><br/>"
                + "<pre>&nbsp &nbsp &nbsp(b) Induce the prospect to omit any material information in the proposal form.</pre><br/>"
                + "<pre>&nbsp &nbsp &nbsp(c) Induce the prospect to submit wrong information in the proposal form or documents submitted to the insurer for acceptance of the proposal.</pre><br/>"
                + "<pre>&nbsp &nbsp &nbsp(d) Behave in a discourteous manner with the prospect.</pre><br/>"
                + "<pre>&nbsp &nbsp &nbsp(e) Interfere with any proposal introduced by any other specified person or any insurance intermediary.</pre><br/>"
                + "<pre>&nbsp &nbsp &nbsp(f) Offer different rates, advantages, terms and conditions other than those offered by the insurer.</pre><br/>"
                + "<pre>&nbsp &nbsp &nbsp(g) Force a policyholder to terminate the existing policy and to effect a new proposal from him within three years from the date of such termination.</pre><br/>"
                + "<pre>&nbsp &nbsp &nbsp(h) No corporate agent shall have a portfolio of insurance business from one person or one organization or one group of organizations under which the premium is in excess of fifty percent of total premium procured in any year.</pre><br/>"
                + "<pre>&nbsp &nbsp &nbsp(i) Become or remain a director of any insurance company, except with the prior approval of the Authority.</pre><br/>"
                + "<pre>&nbsp &nbsp &nbsp(j) Indulge in any sort of money laundering activities.</pre><br/>"
                + "<pre>&nbsp &nbsp &nbsp(k) Indulge in sourcing of business by themselves or through call centers by way of misleading calls or spurious calls.</pre><br/>"
                + "<pre>&nbsp &nbsp &nbsp(l) Undertake multi-level marketing for soliciting and procuring of insurance products.</pre><br/>"
                + "<pre>&nbsp &nbsp &nbsp(m) Engage untrained and unauthorised persons to bring in business.</pre><br/>"
                + "<pre>&nbsp &nbsp &nbsp(n) Provide insurance consultancy or claims consultancy or any other insurance related services except soliciting and servicing of insurance products as per the certificate of registration.</pre><br/>"
                + "<pre>&nbsp &nbsp &nbsp(o) Engage, encourage, enter into a contract with or have any sort of arrangement with any person other than a specified person, to refer, solicit, generate lead, advise, introduce, find or provide contact details of prospective policyholders in furtherance of the distribution of the insurance product.</pre><br/>"
                + "<pre>&nbsp &nbsp &nbsp(p) Pay or allow the payment of any fee, commission, incentive by any other name whatsoever for the purpose of sale, introduction, lead generation, referring or finding to any person or entity.</pre><br/>"
                + "<b>C. Post-sale Code Of Conduct</b><br/>"
                + "<pre>&nbsp &nbsp &nbsp<b>Every Corporate Agent shall </b></pre><br/>"
                + "<pre>&nbsp &nbsp &nbsp(a) Advise every individual policyholder to effect nomination or assignment or change of address or exercise of options, as the case may be, and offer necessary assistance in this behalf, wherever necessary.</pre><br/>"
                + "<pre>&nbsp &nbsp &nbsp(b) With a view to conserve the insurance business already procured through him, make every attempt to ensure remittance of the premiums by the policyholders within the stipulated time, by giving notice to the policyholder orally and in writing.</pre><br/>"
                + "<pre>&nbsp &nbsp &nbsp(c) Ensure that its client is aware of the expiry date of the insurance even if it chooses not to offer further cover to the client.</pre><br/>"
                + "<pre>&nbsp &nbsp &nbsp(d) Ensure that renewal notices contain a warning about the duty of disclosure including the necessity to advise changes affecting the policy, which have occurred since the policy inception or the last renewal date.</pre><br/>"
                + "<pre>&nbsp &nbsp &nbsp(e) Ensure that renewal notices contain a requirement for keeping a record (including copies of letters) of all information supplied to the insurer for the purpose of renewal of the contract.</pre><br/>"
                + "<pre>&nbsp &nbsp &nbsp(f) Ensure that the client receives the insurer's renewal invitation well in time before the expiry date.</pre><br/>"
                + "<pre>&nbsp &nbsp &nbsp(g) Render necessary assistance to the policyholders or claimants or beneficiaries in complying with the requirements for settlement of claims by the insurer.</pre><br/>"
                + "<pre>&nbsp &nbsp &nbsp(h) Explain to its clients their obligation to notify claims promptly and to disclose all material facts and advise subsequent developments as soon as possible.</pre><br/>"
                + "<pre>&nbsp &nbsp &nbsp(i) Advise  the client to make true, fair and complete disclosure where it  believes that the client has not done so. If further disclosure is not forthcoming it shall consider declining to act further for the client.</pre><br/>"
                + "<pre>&nbsp &nbsp &nbsp(j) Give prompt advice to the client of any requirements concerning the claim.</pre><br/>"
                + "<pre>&nbsp &nbsp &nbsp(k) Forward any information received from the client regarding a claim or an incident that may give rise to a claim without delay, and in any event within three working days.</pre><br/>"
                + "<pre>&nbsp &nbsp &nbsp(l) Advise the client without delay of the insurer's decision or otherwise of a claim; and give all reasonable assistance to the client in pursuing his claim.</pre><br/>"
                + "<pre>&nbsp &nbsp &nbsp(m) Shall not demand or receive a share of proceeds from the beneficiary under an insurance contract.</pre><br/>"
                + "<pre>&nbsp &nbsp &nbsp(n) Ensure that letters of instruction, policies and renewal documents contain details of complaints handling procedures.</pre><br/>"
                + "<pre>&nbsp &nbsp &nbsp(o) Accept complaints either by phone or in writing.</pre><br/>"
                + "<pre>&nbsp &nbsp &nbsp(p) Acknowledge a complaint within fourteen days from the receipt of correspondence, advise the member of staff who will be dealing with the complaint and the timetable for dealing with it.</pre><br/>"
                + "<pre>&nbsp &nbsp &nbsp(q) Ensure that response letters are sent and inform the complainant of what he may do if he is unhappy with the response.</pre><br/>"
                + "<pre>&nbsp &nbsp &nbsp(r) Ensure that complaints are dealt with at a suitably senior level.</pre><br/>"
                + "<pre>&nbsp &nbsp &nbsp(s) Have in place a system for recording and monitoring complaints.</pre><br/>";

        txt_tcc_declaration.setText(Html.fromHtml(str_declaration));
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.btn_validate_urn:

                if (!edt_score_card_urn.getText().toString().replaceAll("\\s+", "").trim().equals("")) {

                    str_urn_no = edt_score_card_urn.getText().toString().replaceAll("\\s+", "").trim();

                    //validate urn
                    mAsynchValidateURN = new AsynchValidateURN();
                    mAsynchValidateURN.execute();

                } else {
                    mCommonMethods.showToast(mContext, "Please Enter URN Number");
                }

                break;

            case R.id.btn_tcc_declaration_submit:

                str_place = edt_tcc_declaration_place.getText().toString().replaceAll("\\s+", "").trim();

                if (!str_place.equals("") && is_score_card_upload && chk_tcc_declaration.isChecked() && mHSCFile != null & mDegreeFile != null) {

                    //1st synch all data then upload queries pdf

                    UPLOAD_TYPE = SCHEDULE3_DOC;
                    upload_docs(null);

                    /*mUploadQuestionPDF = new UploadQuestionPDF();
                    mUploadQuestionPDF.execute();*/

                } else {
                    if (str_place.equals("")) {
                        mCommonMethods.showMessageDialog(mContext, "Please enter place.");
                    } else if (!is_score_card_upload) {
                        mCommonMethods.showMessageDialog(mContext, "Please upload score card document.");
                    } else if (mHSCFile == null) {
                        mCommonMethods.showMessageDialog(mContext, "Please Upload HSC Document");
                    } else if (mDegreeFile == null) {
                        mCommonMethods.showMessageDialog(mContext, "Please Upload Degree Document");
                    } else if (chk_tcc_declaration.isChecked()) {
                        mCommonMethods.showMessageDialog(mContext, "Please Agree terms and conditons.");
                    }
                }
                break;

            case R.id.ib_score_card_hsc_doc_view:

                if (mHSCFile != null) {
                    try {
                        mCommonMethods.openAllDocs(mContext, mHSCFile);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                } else {
                    mCommonMethods.showToast(mContext, "Please Browse or Capture Document First!");
                }

                break;

            case R.id.imgbtn_tcc_education_doc_hsc_camera:

                try {
                    if (spnr_tcc_education_doc_hsc.getSelectedItem().toString().equalsIgnoreCase("Select Document")) {
                        mCommonMethods.showToast(mContext, "Please Select Document");
                    } else {
                        UPLOAD_TYPE = FILE_HSC_DOC_NAME;

                        String imageFileName = str_urn_no + FILE_HSC_DOC_NAME + ".jpg";
                        mHSCFile = mStorageUtils.createFileToAppSpecificDirCIF(mContext, imageFileName);
                        capture_docs(mHSCFile);
                    }
                } catch (Exception io) {
                    mCommonMethods.printLog("Capture : ", io.getMessage());
                    mHSCFile = null;
                }

                break;

            case R.id.imgbtn_tcc_education_doc_hsc_browse:

                if (spnr_tcc_education_doc_hsc.getSelectedItem().toString().equalsIgnoreCase("Select Document")) {
                    mCommonMethods.showToast(mContext, "Please Select Document");
                } else {
                    UPLOAD_TYPE = FILE_HSC_DOC_NAME;
                    browse_docs();
                }

                break;

            case R.id.imgbtn_tcc_education_doc_hsc_upload:

                if (spnr_tcc_education_doc_hsc.getSelectedItem().toString().equalsIgnoreCase("Select Document")) {
                    mCommonMethods.showToast(mContext, "Please Select Document");
                } else {
                    UPLOAD_TYPE = FILE_HSC_DOC_NAME;
                    upload_docs(mHSCFile);
                }

                break;

            case R.id.ib_score_card_degree_doc_view:

                if (mDegreeFile != null) {
                    try {
                        mCommonMethods.openAllDocs(mContext, mDegreeFile);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                } else {
                    mCommonMethods.showToast(mContext, "Please Browse or Capture Document First!");
                }

                break;

            case R.id.imgbtn_tcc_education_doc_degree_camera:

                if (spnr_tcc_education_doc_degree.getSelectedItem().toString().equalsIgnoreCase("Select Document")) {
                    mCommonMethods.showToast(mContext, "Please Select Document");
                } else {
                    try {
                        UPLOAD_TYPE = FILE_DEGREE_DOC_NAME;

                        String imageFileName = str_urn_no + FILE_DEGREE_DOC_NAME + ".jpg";
                        mDegreeFile = mStorageUtils.createFileToAppSpecificDirCIF(mContext, imageFileName);
                        capture_docs(mDegreeFile);
                    } catch (Exception io) {
                        mCommonMethods.printLog("Capture : ", io.getMessage());
                        mDegreeFile = null;
                    }
                }

                break;

            case R.id.imgbtn_tcc_education_doc_degree_browse:

                if (spnr_tcc_education_doc_degree.getSelectedItem().toString().equalsIgnoreCase("Select Document")) {
                    mCommonMethods.showToast(mContext, "Please Select Document");
                } else {
                    UPLOAD_TYPE = FILE_DEGREE_DOC_NAME;
                    browse_docs();
                }

                break;

            case R.id.imgbtn_tcc_education_doc_degree_upload:

                if (spnr_tcc_education_doc_degree.getSelectedItem().toString().equalsIgnoreCase("Select Document")) {
                    mCommonMethods.showToast(mContext, "Please Select Document");
                } else {
                    UPLOAD_TYPE = FILE_DEGREE_DOC_NAME;
                    upload_docs(mDegreeFile);
                }

                break;

            case R.id.ib_score_card_docs_view:

                if (mScoreCardFile != null) {
                    try {
                        mCommonMethods.openAllDocs(mContext, mScoreCardFile);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                } else {
                    mCommonMethods.showToast(mContext, "Please Browse or Capture Document First!");
                }

                break;

            case R.id.imgbtn_score_card_camera:
                try {
                    UPLOAD_TYPE = FILE_SCORE_CARD_NAME;
                    is_score_card_upload = false;
                    String imageFileName = str_urn_no + FILE_SCORE_CARD_NAME + ".jpg";
                    mScoreCardFile = mStorageUtils.createFileToAppSpecificDirCIF(mContext, imageFileName);
                    capture_docs(mScoreCardFile);
                } catch (Exception io) {
                    mCommonMethods.printLog("Capture : ", io.getMessage());
                    mScoreCardFile = null;
                }
                break;

            case R.id.imgbtn_score_card_browse:
                UPLOAD_TYPE = FILE_SCORE_CARD_NAME;
                is_score_card_upload = false;
                browse_docs();
                break;

            case R.id.imgbtn_score_card_upload:
                UPLOAD_TYPE = FILE_SCORE_CARD_NAME;
                is_score_card_upload = false;

                upload_docs(mScoreCardFile);

                break;

            default:
                break;

        }
    }

    private void upload_docs(File mFile) {

        if (UPLOAD_TYPE.equals(SCHEDULE3_DOC)) {

            try {

                File mPdfFile = mStorageUtils.createFileToAppSpecificDirCIF(mContext,
                        str_urn_no + SCHEDULE3_DOC + ".pdf");

                docBytes = mCommonMethods.read(mPdfFile);

                //upload document
                createSoapRequestToUploadDoc();

            } catch (Exception ex) {
                docBytes = null;
                mCommonMethods.showMessageDialog(mContext, ex.getMessage());
            }

        } else if (UPLOAD_TYPE.equals(QUESTIONS_DOC)) {

            try {

                File mPdfFile = mStorageUtils.createFileToAppSpecificDirCIF(mContext,
                        QUESTIONS_DOC + str_urn_no + ".pdf");

                docBytes = mCommonMethods.read(mPdfFile);

                //upload document
                createSoapRequestToUploadDoc();

            } catch (Exception ex) {
                docBytes = null;
                mCommonMethods.showMessageDialog(mContext, ex.getMessage());
            }
        } else {
            try {

                if (mFile != null) {
                    FileInputStream fin = new FileInputStream(mFile);

                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    byte[] b = new byte[1024];
                    int bytesRead = 0;
                    try {
                        while ((bytesRead = fin.read(b)) != -1) {
                            bos.write(b, 0, bytesRead);
                        }

                        docBytes = bos.toByteArray();
                        bos.flush();
                        bos.close();

                        //upload document
                        createSoapRequestToUploadDoc();

                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(mContext, "Please Browse/Capture Document", Toast.LENGTH_LONG).show();
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void showMessageDialog(Context context, String message) {
        try {
            final Dialog dialog = new Dialog(context);
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

                    /*call TCC main Activity activity*/
                    Intent intent = new Intent(mContext, CIFEnrollmentPFActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            });
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void browse_docs() {
        Intent mIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        mIntent.addCategory(Intent.CATEGORY_OPENABLE);
        mIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION
                | Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
        mIntent.setType("image/*");
        //mIntent.setType("*/*");
            /*String[] mimeType = new String[]{"application/x-binary,application/octet-stream"};
            if(mimeType.length > 0) {
                mIntent.putExtra(Intent.EXTRA_MIME_TYPES, mimeType);
            }*/
        mBrowseDocLauncher.launch(mIntent);
    }

    private void capture_docs(File mFile) {

        try {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            // Continue only if the File was successfully created
            if (mFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "sbilife.com.pointofsale_bancaagency",
                        mFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_CODE_PICK_PHOTO_FILE);
            }
        } catch (Exception exp) {
            exp.printStackTrace();
            mCommonMethods.printLog("Capture : ", exp.getMessage());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String str_extension = "";
        if (requestCode == REQUEST_CODE_PICK_PHOTO_FILE) {
            if (resultCode == RESULT_OK) {

                if (UPLOAD_TYPE.equals(FILE_SCORE_CARD_NAME)) {
                    CompressImage.compressImage(mScoreCardFile.getPath());

                    //mScoreCardFile = mCommonMethods.compressImageCIF2(mScoreCardFile, str_urn_no, FILE_SCORE_CARD_NAME+".jpg");

                    if (mScoreCardFile != null) {

                        long size = mScoreCardFile.length();
                        double kilobyte = size / 1024;

                        // 2MB valiadation
                        if (kilobyte < mCommonMethods.FILE_UPLOAD_RESTRICT_SIZE) {
                            //str_extension = mScoreCardFile.getPath().substring(mScoreCardFile.getPath().lastIndexOf("."));

                            imgbtn_score_card_camera.setImageDrawable(getResources().getDrawable(R.drawable.checkedcamera));
                            imgbtn_score_card_browse.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_browsedoc));

                        } else {
                            mScoreCardFile = null;
                            mCommonMethods.showMessageDialog(mContext, mCommonMethods.FILE_UPLOAD_RESTRICT_SIZE_MSG);
                        }

                    } else {
                        mCommonMethods.showMessageDialog(mContext, "File Size is too small");
                    }
                } else if (UPLOAD_TYPE.equals(FILE_HSC_DOC_NAME)) {

                    CompressImage.compressImage(mHSCFile.getPath());

                    //mHSCFile = mCommonMethods.compressImageCIF2(mHSCFile, str_urn_no, FILE_HSC_DOC_NAME + ".jpg");

                    long size = mHSCFile.length();
                    double kilobyte = size / 1024;

                    //2 MB valiadation
                    if (kilobyte < mCommonMethods.FILE_UPLOAD_RESTRICT_SIZE) {
                        //str_extension = mHSCFile.getPath().substring(mHSCFile.getPath().lastIndexOf("."));

                        imgbtn_tcc_education_doc_hsc_camera.setImageDrawable(getResources().getDrawable(R.drawable.checkedcamera));
                        imgbtn_tcc_education_doc_hsc_browse.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_browsedoc));
                    } else {
                        mHSCFile = null;
                        mCommonMethods.showMessageDialog(mContext, mCommonMethods.FILE_UPLOAD_RESTRICT_SIZE_MSG);
                    }
                } else if (UPLOAD_TYPE.equals(FILE_DEGREE_DOC_NAME)) {

                    CompressImage.compressImage(mDegreeFile.getPath());

                    //mDegreeFile = mCommonMethods.compressImageCIF2(mDegreeFile, str_urn_no, FILE_DEGREE_DOC_NAME + ".jpg");

                    long size = mDegreeFile.length();
                    double kilobyte = size / 1024;

                    //2 MB valiadation
                    if (kilobyte < mCommonMethods.FILE_UPLOAD_RESTRICT_SIZE) {
                        //str_extension = mDegreeFile.getPath().substring(mDegreeFile.getPath().lastIndexOf("."));

                        imgbtn_tcc_education_doc_degree_camera.setImageDrawable(getResources().getDrawable(R.drawable.checkedcamera));
                        imgbtn_tcc_education_doc_degree_browse.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_browsedoc));
                    } else {
                        mDegreeFile = null;
                        mCommonMethods.showMessageDialog(mContext, mCommonMethods.FILE_UPLOAD_RESTRICT_SIZE_MSG);
                    }
                }
            }
        }
    }

    private String createSchedule3PDF() {

        try {

            Font headerBold = new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.BOLD);
            Font small_bold = new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.BOLD);
            Font small_bold1 = new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.BOLD);
            Font small_bold2 = new Font(Font.FontFamily.TIMES_ROMAN, 4, Font.BOLD);
            Font small_normal = new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.NORMAL);

            mSchedule3File = mStorageUtils.createFileToAppSpecificDirCIF(mContext, str_urn_no + SCHEDULE3_DOC + ".pdf");

            Rectangle rect = new Rectangle(594f, 792f);

            //Document document = new Document(rect, 50, 50, 50, 50);
            Document document = new Document(PageSize.A4, 50, 50, 50, 50);
            PdfWriter pdf_writer = PdfWriter.getInstance(document, new FileOutputStream(mSchedule3File.getPath()));

            document.open();

            Paragraph para_tittle = new Paragraph("SCHEDULE III", headerBold);
            para_tittle.setAlignment(Element.ALIGN_CENTER);
            document.add(para_tittle);

            para_tittle = new Paragraph("[See regulation 26]", headerBold);
            para_tittle.setAlignment(Element.ALIGN_CENTER);
            document.add(para_tittle);

            para_tittle = new Paragraph("Insurance Regulatory and Development Authority of India (Registration of Corporate Agents)\n" +
                    "Regulations, 2015", headerBold);
            para_tittle.setAlignment(Element.ALIGN_CENTER);
            document.add(para_tittle);

            para_tittle = new Paragraph("Code of Conduct", headerBold);
            para_tittle.setAlignment(Element.ALIGN_CENTER);
            document.add(para_tittle);

            para_tittle = new Paragraph("I. General Code of Conduct:", small_bold);
            para_tittle.setAlignment(Element.ALIGN_LEFT);
            document.add(para_tittle);

            Paragraph para_point_1 = new Paragraph("\n1. Every corporate agent shall follow recognised standards of professional conduct and discharge their duties in the interest of the policyholders. While doing so -", small_bold1);
            para_point_1.setAlignment(Element.ALIGN_LEFT);
            document.add(para_point_1);

            Paragraph para_point_1_a = new Paragraph("(a) conduct its dealings with clients with utmost good faith and integrity at all times;", small_normal);
            para_point_1_a.setIndentationLeft(36);
            document.add(para_point_1_a);

            para_point_1_a = new Paragraph("(b) act with care and diligence;", small_normal);
            para_point_1_a.setIndentationLeft(36);
            document.add(para_point_1_a);

            para_point_1_a = new Paragraph("(c) ensure that the client understands his relationship with the corporate agent and on whose behalf the corporate agent is acting;", small_normal);
            para_point_1_a.setIndentationLeft(36);
            document.add(para_point_1_a);

            para_point_1_a = new Paragraph("(d) treat all information supplied by the prospective clients as completely confidential to themselves and to the insurer(s) to which the business is being offered;", small_normal);
            para_point_1_a.setIndentationLeft(36);
            document.add(para_point_1_a);

            para_point_1_a = new Paragraph("(f) No director of a company or a partner of a firm or the chief executive or a principal officer or a specified person shall hold similar position with another corporate agent;", small_normal);
            para_point_1_a.setIndentationLeft(36);
            document.add(para_point_1_a);

            para_point_1_a = new Paragraph("(e) take appropriate steps to maintain the security of confidential documents in their possession;", small_normal);
            para_point_1_a.setIndentationLeft(36);
            document.add(para_point_1_a);

            Paragraph para_point_2 = new Paragraph("\n2. Every Corporate Agent shall", small_bold1);
            para_point_2.setAlignment(Element.ALIGN_LEFT);
            document.add(para_point_2);

            Paragraph para_point_2_a = new Paragraph("a) be responsible for all acts of omission and commission of its principal officer and every specified person;", small_normal);
            para_point_2_a.setIndentationLeft(36);
            document.add(para_point_2_a);

            para_point_2_a = new Paragraph("b) ensure that the principal officer and all specified persons are properly trained, skilled and knowledgeable in the insurance products they market;", small_normal);
            para_point_2_a.setIndentationLeft(36);
            document.add(para_point_2_a);

            para_point_2_a = new Paragraph("c) ensure that the principal officer and the specified person do not make to the prospect any misrepresentation on policy benefits and returns available under the policy;", small_normal);
            para_point_2_a.setIndentationLeft(36);
            document.add(para_point_2_a);

            para_point_2_a = new Paragraph("d) ensure that no prospect is forced to buy an insurance product;", small_normal);
            para_point_2_a.setIndentationLeft(36);
            document.add(para_point_2_a);

            para_point_2_a = new Paragraph("e) give adequate pre-sales and post-sales advice to the insured in respect of the insurance product;", small_normal);
            para_point_2_a.setIndentationLeft(36);
            document.add(para_point_2_a);

            para_point_2_a = new Paragraph("f) extend all possible help and cooperation to an insured in completion of all formalities and documentation in the event of a claim;", small_normal);
            para_point_2_a.setIndentationLeft(36);
            document.add(para_point_2_a);

            para_point_2_a = new Paragraph("g) give due publicity to the fact that the corporate agent does not underwrite the risk or act as an insurer;", small_normal);
            para_point_2_a.setIndentationLeft(36);
            document.add(para_point_2_a);

            para_point_2_a = new Paragraph("h) enter into agreements with the insurers in which the duties and responsibilities of both are defined.", small_normal);
            para_point_2_a.setIndentationLeft(36);
            document.add(para_point_2_a);

            para_tittle = new Paragraph("\nII. Pre-sale Code of Conduct", small_bold);
            para_tittle.setAlignment(Element.ALIGN_LEFT);
            document.add(para_tittle);

            Paragraph para_point_3 = new Paragraph("\n3. Every corporate agent or principal officer or a specified person shall also follow the code of conduct specified below:", small_bold1);
            para_point_3.setAlignment(Element.ALIGN_LEFT);
            document.add(para_point_3);

            Paragraph para_point_3_1 = new Paragraph("\n(i) Every corporate agent/ principal officer / specified person shall,--", small_bold1);
            para_point_3_1.setIndentationLeft(18);
            document.add(para_point_3_1);

            Paragraph para_point_3_1_a = new Paragraph("(a) identify himself and disclose his registration/ certificate to the prospect on demand;", small_normal);
            para_point_3_1_a.setIndentationLeft(36);
            document.add(para_point_3_1_a);

            para_point_3_1_a = new Paragraph("(b) disseminate the requisite information in respect of insurance products offered for sale by the insurers with whom they have arrangement and take into account the needs of the prospect while recommending a specific insurance plan;", small_normal);
            para_point_3_1_a.setIndentationLeft(36);
            document.add(para_point_3_1_a);

            para_point_3_1_a = new Paragraph("(c) disclose the scales of commission in respect of the insurance product offered for sale, if asked by the prospect;", small_normal);
            para_point_3_1_a.setIndentationLeft(36);
            document.add(para_point_3_1_a);

            para_point_3_1_a = new Paragraph("(d) indicate the premium to be charged by the insurer for the insurance product offered for sale;", small_normal);
            para_point_3_1_a.setIndentationLeft(36);
            document.add(para_point_3_1_a);

            para_point_3_1_a = new Paragraph("(e) explain to the prospect the nature of information required in the proposal form by the insurer,and also the importance of disclosure of material information in the purchase of an insurance contract;", small_normal);
            para_point_3_1_a.setIndentationLeft(36);
            document.add(para_point_3_1_a);

            para_point_3_1_a = new Paragraph("(f) bring to the notice of the insurer any adverse habits or income inconsistency of the prospect,in the form of a Confidential Report along with every proposal submitted to the insurer, and any material fact that may adversely affect the underwriting decision of the insurer as regards acceptance of the proposal, by making all reasonable enquiries about the prospect;", small_normal);
            para_point_3_1_a.setIndentationLeft(36);
            document.add(para_point_3_1_a);

            para_point_3_1_a = new Paragraph("(g) inform promptly the prospect about the acceptance or rejection of the proposal by the insurer;", small_normal);
            para_point_3_1_a.setIndentationLeft(36);
            document.add(para_point_3_1_a);

            para_point_3_1_a = new Paragraph("(h) obtain the requisite documents at the time of filing the proposal form with the insurer; and other documents subsequently asked for by the insurer for completion of the proposal;", small_normal);
            para_point_3_1_a.setIndentationLeft(36);
            document.add(para_point_3_1_a);

            Paragraph para_point_3_2 = new Paragraph("\n(ii) No corporate agent/ principal officer / specified person shall,----", small_bold1);
            para_point_3_2.setIndentationLeft(18);
            document.add(para_point_3_2);

            Paragraph para_point_3_2_a = new Paragraph("a. solicit or procure insurance business without holding a valid registration/ certificate;", small_normal);
            para_point_3_2_a.setIndentationLeft(36);
            document.add(para_point_3_2_a);

            //sign at bottom
            byte[] fbyt_applicant = Base64.decode(str_customer_sign, 0);
            Bitmap applicantBitmap = BitmapFactory.decodeByteArray(fbyt_applicant, 0, fbyt_applicant.length);
            //Bitmap applicantBitmap = CaptureSignature.scaled;

            ByteArrayOutputStream customer_signature_stream = new ByteArrayOutputStream();

            (applicantBitmap).compress(Bitmap.CompressFormat.PNG, 50, customer_signature_stream);
            Image applicant_signature = Image.getInstance(customer_signature_stream.toByteArray());

            applicant_signature.scaleToFit(90, 90);

            PdfPTable BI_PdftableApplicant_sign = new PdfPTable(3);
            BI_PdftableApplicant_sign.setWidths(new float[]{5f, 5f, 5f});
            BI_PdftableApplicant_sign.setWidthPercentage(100);

            PdfPCell Nocell = new PdfPCell(new Paragraph("", small_bold));
            Nocell.setBorder(Rectangle.NO_BORDER);

            BI_PdftableApplicant_sign.addCell(Nocell);

            BI_PdftableApplicant_sign.addCell(Nocell);

            PdfPCell sign_cell = new PdfPCell(applicant_signature);
            sign_cell.setHorizontalAlignment(Element.ALIGN_TOP);
            sign_cell.setPadding(5);
            sign_cell.setBorder(Rectangle.NO_BORDER);
            BI_PdftableApplicant_sign.addCell(sign_cell);

            /*BI_PdftableApplicant_sign.addCell(Nocell);

            BI_PdftableApplicant_sign.addCell(Nocell);

            sign_cell = new PdfPCell(new Paragraph(
                    "Applicant Signature", small_bold));
            sign_cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            sign_cell.setBorder(Rectangle.TOP);
            sign_cell.setPadding(5);
            BI_PdftableApplicant_sign.addCell(sign_cell);*/

            document.add(BI_PdftableApplicant_sign);

            para_point_3_2_a = new Paragraph("\nb. induce the prospect to omit any material information in the proposal form;", small_normal);
            para_point_3_2_a.setIndentationLeft(36);
            document.add(para_point_3_2_a);

            para_point_3_2_a = new Paragraph("c. induce the prospect to submit wrong information in the proposal form or documents submitted to the insurer for acceptance of the proposal;", small_normal);
            para_point_3_2_a.setIndentationLeft(36);
            document.add(para_point_3_2_a);

            para_point_3_2_a = new Paragraph("d. behave in a discourteous manner with the prospect;", small_normal);
            para_point_3_2_a.setIndentationLeft(36);
            document.add(para_point_3_2_a);

            para_point_3_2_a = new Paragraph("e. interfere with any proposal introduced by any other specified person or any insurance intermediary;", small_normal);
            para_point_3_2_a.setIndentationLeft(36);
            document.add(para_point_3_2_a);


            para_point_3_2_a = new Paragraph("f. offer different rates, advantages, terms and conditions other than those offered by the insurer;", small_normal);
            para_point_3_2_a.setIndentationLeft(36);
            document.add(para_point_3_2_a);

            para_point_3_2_a = new Paragraph("g. force a policyholder to terminate the existing policy and to effect a new proposal from him within three years from the date of such termination;", small_normal);
            para_point_3_2_a.setIndentationLeft(36);
            document.add(para_point_3_2_a);

            para_point_3_2_a = new Paragraph("h. No corporate agent shall have a portfolio of insurance business from one person or one organization or one group of organizations under which the premium is in excess of fifty percent of total premium procured in any year;", small_normal);
            para_point_3_2_a.setIndentationLeft(36);
            document.add(para_point_3_2_a);

            para_point_3_2_a = new Paragraph("i. become or remain a director of any insurance company, except with the prior approval of the Authority;", small_normal);
            para_point_3_2_a.setIndentationLeft(36);
            document.add(para_point_3_2_a);

            para_point_3_2_a = new Paragraph("j. indulge in any sort of money laundering activities;", small_normal);
            para_point_3_2_a.setIndentationLeft(36);
            document.add(para_point_3_2_a);

            para_point_3_2_a = new Paragraph("k. indulge in sourcing of business by themselves or through call centers by way of misleading calls or spurious calls;", small_normal);
            para_point_3_2_a.setIndentationLeft(36);
            document.add(para_point_3_2_a);

            para_point_3_2_a = new Paragraph("l. undertake multi-level marketing for soliciting and procuring of insurance products;", small_normal);
            para_point_3_2_a.setIndentationLeft(36);
            document.add(para_point_3_2_a);

            para_point_3_2_a = new Paragraph("m. engage untrained and unauthorised persons to bring in business;", small_normal);
            para_point_3_2_a.setIndentationLeft(36);
            document.add(para_point_3_2_a);

            para_point_3_2_a = new Paragraph("n. provide insurance consultancy or claims consultancy or any other insurance related services except soliciting and servicing of insurance products as per the certificate of registration.", small_normal);
            para_point_3_2_a.setIndentationLeft(36);
            document.add(para_point_3_2_a);

            para_point_3_2_a = new Paragraph("o. Engage, encourage, enter into a contract with or have any sort of arrangement with any person other than a specified person, to refer, solicit, generate lead, advise, introduce,find or provide contact details of prospective policyholders in furtherance of the distribution of the insurance product;", small_normal);
            para_point_3_2_a.setIndentationLeft(36);
            document.add(para_point_3_2_a);

            para_point_3_2_a = new Paragraph("p. Pay or allow the payment of any fee, commission, incentive by any other name whatsoever for the purpose of sale, introduction, lead generation, referring or finding to any person or entity", small_normal);
            para_point_3_2_a.setIndentationLeft(36);
            document.add(para_point_3_2_a);

            para_tittle = new Paragraph("\nIII. Post-Sale Code of Conduct", small_bold);
            para_tittle.setAlignment(Element.ALIGN_LEFT);
            document.add(para_tittle);

            Paragraph para_point_4 = new Paragraph("\n4. Every Corporate Agent shall –", small_bold1);
            para_point_4.setAlignment(Element.ALIGN_LEFT);
            document.add(para_point_4);

            Paragraph para_point_4_a = new Paragraph("a. advise every individual policyholder to effect nomination or assignment or change of address or exercise of options, as the case may be, and offer necessary assistance in this behalf, wherever necessary;", small_bold1);
            para_point_4_a.setIndentationLeft(36);
            document.add(para_point_4_a);

            para_point_4_a = new Paragraph("b. with a view to conserve the insurance business already procured through him, make every attempt to ensure remittance of the premiums by the policyholders within the stipulated time, by giving notice to the policyholder orally and in writing.", small_bold1);
            para_point_4_a.setIndentationLeft(36);
            document.add(para_point_4_a);

            para_point_4_a = new Paragraph("c. ensure that its client is aware of the expiry date of the insurance even if it chooses not to offer further cover to the client;", small_bold1);
            para_point_4_a.setIndentationLeft(36);
            document.add(para_point_4_a);

            para_point_4_a = new Paragraph("d. ensure that renewal notices contain a warning about the duty of disclosure including the necessity to advise changes affecting the policy, which have occurred since the policy inception or the last renewal date;", small_bold1);
            para_point_4_a.setIndentationLeft(36);
            document.add(para_point_4_a);

            para_point_4_a = new Paragraph("e. ensure that renewal notices contain a requirement for keeping a record (including copies of letters) of all information supplied to the insurer for the purpose of renewal of the contract;", small_bold1);
            para_point_4_a.setIndentationLeft(36);
            document.add(para_point_4_a);

            para_point_4_a = new Paragraph("f. ensure that the client receives the insurer's renewal invitation well in time before the expiry date.", small_bold1);
            para_point_4_a.setIndentationLeft(36);
            document.add(para_point_4_a);

            para_point_4_a = new Paragraph("g. render necessary assistance to the policyholders or claimants or beneficiaries in complying with the requirements for settlement of claims by the insurer;", small_bold1);
            para_point_4_a.setIndentationLeft(36);
            document.add(para_point_4_a);

            para_point_4_a = new Paragraph("h. explain to its clients their obligation to notify claims promptly and to disclose all material facts and advise subsequent developments as soon as possible;", small_bold1);
            para_point_4_a.setIndentationLeft(36);
            document.add(para_point_4_a);

            para_point_4_a = new Paragraph("i. advise the client to make true, fair and complete disclosure where it believes that the client has not done so. If further disclosure is not forthcoming it shall consider declining to act further for the client;", small_bold1);
            para_point_4_a.setIndentationLeft(36);
            document.add(para_point_4_a);

            para_point_4_a = new Paragraph("j. give prompt advice to the client of any requirements concerning the claim;", small_bold1);
            para_point_4_a.setIndentationLeft(36);
            document.add(para_point_4_a);

            para_point_4_a = new Paragraph("k. forward any information received from the client regarding a claim or an incident that may give rise to a claim without delay, and in any event within three working days;", small_bold1);
            para_point_4_a.setIndentationLeft(36);
            document.add(para_point_4_a);

            para_point_4_a = new Paragraph("l. advise the client without delay of the insurer's decision or otherwise of a claim; and give all reasonable assistance to the client in pursuing his claim.", small_bold1);
            para_point_4_a.setIndentationLeft(36);
            document.add(para_point_4_a);

            para_point_4_a = new Paragraph("m. shall not demand or receive a share of proceeds from the beneficiary under an insurance contract;", small_bold1);
            para_point_4_a.setIndentationLeft(36);
            document.add(para_point_4_a);

            para_point_4_a = new Paragraph("n. ensure that letters of instruction, policies and renewal documents contain details of complaints handling procedures;", small_bold1);
            para_point_4_a.setIndentationLeft(36);
            document.add(para_point_4_a);

            para_point_4_a = new Paragraph("o. accept complaints either by phone or in writing;", small_bold1);
            para_point_4_a.setIndentationLeft(36);
            document.add(para_point_4_a);

            para_point_4_a = new Paragraph("p. acknowledge a complaint within fourteen days from the receipt of correspondence, advise the member of staff who will be dealing with the complaint and the timetable for dealing with it;", small_bold1);
            para_point_4_a.setIndentationLeft(36);
            document.add(para_point_4_a);

            para_point_4_a = new Paragraph("q. ensure that response letters are sent and inform the complainant of what he may do if he is unhappy with the response;", small_bold1);
            para_point_4_a.setIndentationLeft(36);
            document.add(para_point_4_a);

            para_point_4_a = new Paragraph("r. ensure that complaints are dealt with at a suitably senior level;", small_bold1);
            para_point_4_a.setIndentationLeft(36);
            document.add(para_point_4_a);

            para_point_4_a = new Paragraph("s. have in place a system for recording and monitoring complaints.", small_bold1);
            para_point_4_a.setIndentationLeft(36);
            document.add(para_point_4_a);

            document.add(BI_PdftableApplicant_sign);

            document.close();

            return "";

        } catch (Exception ex) {
            ex.printStackTrace();
            return ex.getMessage();
        }
    }

    private Bitmap base64ToBitmap(String b64) {
        byte[] imageAsBytes = Base64.decode(b64.getBytes(), Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
    }

    private void createSoapRequestToUploadDoc() {
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME_UPLOAD_QUESTIONS_DOC);

        request.addProperty("f", docBytes);

        if (UPLOAD_TYPE.equals(FILE_SCORE_CARD_NAME)) {
            request.addProperty("fileName", mScoreCardFile.getName());
        } else if (UPLOAD_TYPE.equals(QUESTIONS_DOC)) {
            request.addProperty("fileName", QUESTIONS_DOC + str_urn_no + ".pdf");
        } else if (UPLOAD_TYPE.equals(FILE_HSC_DOC_NAME)) {
            request.addProperty("fileName", mHSCFile.getName());
        } else if (UPLOAD_TYPE.equals(FILE_DEGREE_DOC_NAME)) {
            request.addProperty("fileName", mDegreeFile.getName());
        } else if (UPLOAD_TYPE.equals(SCHEDULE3_DOC)) {
            request.addProperty("fileName", str_urn_no + SCHEDULE3_DOC + ".pdf");
        }

        request.addProperty("URN", str_urn_no);

        //for UAT
        //request.addProperty("Authkey", mCommonMethods.getStr_cif_auth_key());

        mAsyncUploadFile_cif = new AsyncUploadFile_CIF(mContext, this, request, METHOD_NAME_UPLOAD_QUESTIONS_DOC);
        mAsyncUploadFile_cif.execute();
    }

    @Override
    public void onUploadComplete(Boolean result) {
        if (result) {
            if (UPLOAD_TYPE.equals(SCHEDULE3_DOC)) {

                UPLOAD_TYPE = QUESTIONS_DOC;
                upload_docs(null);

            } else if (UPLOAD_TYPE.equals(QUESTIONS_DOC)) {

                str_customer_sign = null;

                mUploadTimeURN = new UploadTimeURN();
                mUploadTimeURN.execute();

                //showMessageDialog(mContext, "Documents  have been  submitted  successfully & C.o.R. can be  downloaded upon the checklist  approved  by IRDAI.");
            } else if (UPLOAD_TYPE.equals(FILE_SCORE_CARD_NAME)) {
                is_score_card_upload = true;

                imgbtn_score_card_camera.setEnabled(false);
                imgbtn_score_card_browse.setEnabled(false);
                imgbtn_score_card_upload.setEnabled(false);

                imgbtn_score_card_upload.setImageDrawable(getResources().getDrawable(R.drawable.checkedupload));

                Toast.makeText(mContext, "Document Upload Successfully...", Toast.LENGTH_SHORT).show();
            } else if (UPLOAD_TYPE.equals(FILE_HSC_DOC_NAME)) {
                imgbtn_tcc_education_doc_hsc_camera.setEnabled(false);
                imgbtn_tcc_education_doc_hsc_browse.setEnabled(false);
                imgbtn_tcc_education_doc_hsc_upload.setEnabled(false);
                imgbtn_tcc_education_doc_hsc_upload.setImageDrawable(getResources().getDrawable(R.drawable.checkedupload));
            } else if (UPLOAD_TYPE.equals(FILE_DEGREE_DOC_NAME)) {
                imgbtn_tcc_education_doc_degree_camera.setEnabled(false);
                imgbtn_tcc_education_doc_degree_browse.setEnabled(false);
                imgbtn_tcc_education_doc_degree_upload.setEnabled(false);
                imgbtn_tcc_education_doc_degree_upload.setImageDrawable(getResources().getDrawable(R.drawable.checkedupload));
            }

        } else {

            if (UPLOAD_TYPE.equals(FILE_SCORE_CARD_NAME)) {
                is_score_card_upload = false;
            }

            Toast.makeText(mContext, "PLease try agian later..", Toast.LENGTH_SHORT).show();
        }
    }

    class AsynchGetCIFName extends AsyncTask<String, String, String> {

        private volatile boolean running = true;
        private String str_output_result = "";

        @Override
        protected void onPreExecute() {
            mProgressDialog = new ProgressDialog(mContext, ProgressDialog.THEME_HOLO_LIGHT);
            if (mProgressDialog != null && !mProgressDialog.isShowing()) {
                mProgressDialog.show();
            }
        }

        @Override
        protected String doInBackground(String... params) {

            if (mCommonMethods.isNetworkConnected(mContext)) {

                try {

                    running = true;

                    SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME_GET_NAME_BY_URN);

                    request.addProperty("strURN", str_urn_no);

                    mCommonMethods.TLSv12Enable();

                    SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                    envelope.dotNet = true;

                    new MarshalBase64().register(envelope); // serialization

                    envelope.setOutputSoapObject(request);

                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);

                    HttpTransportSE androidHttpTranport = new HttpTransportSE(ServiceURL.SERVICE_URL, 50000);
                    try {
                        androidHttpTranport.call(NAMESPACE + METHOD_NAME_GET_NAME_BY_URN, envelope);
                        Object response = envelope.getResponse();
                        str_output_result = response.toString();

                        return str_output_result;

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
        }

        @Override
        protected void onPostExecute(String s) {
            if (mProgressDialog.isShowing())
                mProgressDialog.dismiss();

            if (running) {

                ParseXML mParseXML = new ParseXML();

                str_output_result = mParseXML.parseXmlTag(str_output_result, "NewDataSet");

                str_output_result = str_output_result == null ? "" : str_output_result;

                if (!str_output_result.equals("")) {
                    str_output_result = mParseXML.parseXmlTag(str_output_result, "Table");
                    str_output_result = mParseXML.parseXmlTag(str_output_result, "CHD_EMPLOYEENAME");

                    String str_declare = "I "
                            + "<font color=\"blue\">" + str_output_result + "</font>, confirm that the above information is, to the best of my knowledge and belief, true and complete. I undertake to keep the Authority fully informed, as soon as possible, of all events, which take place subsequent to my appointment, which are relevant to the information provided above.";

                    chk_tcc_declaration.setText(Html.fromHtml(str_declare), TextView.BufferType.SPANNABLE);

                    running = false;
                    str_output_result = "";

                    ll_score_card_declaration.setVisibility(View.VISIBLE);
                } else {

                    ll_score_card_declaration.setVisibility(View.GONE);
                    Toast.makeText(mContext, "PLease try agian later..",
                            Toast.LENGTH_SHORT).show();
                }

            } else {
                ll_score_card_declaration.setVisibility(View.GONE);
                Toast.makeText(mContext, s, Toast.LENGTH_SHORT).show();
            }
        }
    }

    class UploadTimeURN extends AsyncTask<String, String, String> {

        private volatile boolean running = true;
        private String str_output_result = "";

        @Override
        protected void onPreExecute() {

            if (!mProgressDialog.isShowing()) {
                mProgressDialog = new ProgressDialog(mContext,
                        ProgressDialog.THEME_HOLO_LIGHT);
                mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                mProgressDialog.setCanceledOnTouchOutside(false);
                mProgressDialog
                        .setTitle(Html
                                .fromHtml("<font color='#00a1e3'><b>Please Wait...<b></font>"));
                mProgressDialog.show();
            }
        }

        @Override
        protected String doInBackground(String... params) {

            if (mCommonMethods.isNetworkConnected(mContext)) {
                try {

                    running = true;

                    SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME_UPLOAD_TIME_URN);

                    request.addProperty("URN", str_urn_no);

                    //for UAT
                    //request.addProperty("Authkey", mCommonMethods.getStr_cif_auth_key());

                    //current date
                    Calendar cal = Calendar.getInstance();
                    String mont = ((cal.get(Calendar.MONTH) + 1) < 10 ? "0" : "") + (cal.get(Calendar.MONTH) + 1);
                    String day = (cal.get(Calendar.DAY_OF_MONTH) < 10 ? "0" : "") + cal.get(Calendar.DAY_OF_MONTH);

                    request.addProperty("submit_Date", mont + "-" + day + "-" + cal.get(Calendar.YEAR));

                    mCommonMethods.TLSv12Enable();

                    SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                    envelope.dotNet = true;

                    new MarshalBase64().register(envelope); // serialization

                    envelope.setOutputSoapObject(request);

                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);

                    HttpTransportSE androidHttpTranport = new HttpTransportSE(ServiceURL.SERVICE_URL, 50000);
                    try {

                        androidHttpTranport.call(NAMESPACE + METHOD_NAME_UPLOAD_TIME_URN, envelope);
                        Object response = envelope.getResponse();
                        str_output_result = response.toString();
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

                if (str_output_result.equals("1")) {
                    running = false;
                    showMessageDialog(mContext, "Documents  have been  submitted  successfully & C.o.R. can be  downloaded upon the checklist  approved  by IRDAI.");

                } else {
                    Toast.makeText(mContext, "PLease try agian later..",
                            Toast.LENGTH_SHORT).show();
                }

            } else {
                Toast.makeText(mContext, s,
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    class AsynchValidateURN extends AsyncTask<String, String, String> {

        private volatile boolean running = true;
        private ParseXML mParse;

        @Override
        protected void onPreExecute() {

            mProgressDialog = new ProgressDialog(mContext, ProgressDialog.THEME_HOLO_LIGHT);
            String Message = "Loading Please wait...";
            mProgressDialog.setMessage(Html.fromHtml("<font color='#00a1e3'><b>" + Message + "<b></font>"));
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {

                running = true;

                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME_SCORE_CARD_VALIDATE_URN);
                request.addProperty("strURN", str_urn_no);

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);
                envelope.dotNet = true;

                envelope.setOutputSoapObject(request);

                mCommonMethods.TLSv12Enable();

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);

                HttpTransportSE androidHttpTranport = new HttpTransportSE(ServiceURL.SERVICE_URL);
                try {

                    androidHttpTranport.call(NAMESPACE + METHOD_NAME_SCORE_CARD_VALIDATE_URN, envelope);
                    //Object response = envelope.getResponse();

                    SoapPrimitive sa = (SoapPrimitive) envelope.getResponse();

                    String str_res = sa.toString();
                    mParse = new ParseXML();

                    str_res = mParse.parseXmlTag(str_res, "NewDataSet");
                    if (str_res != null) {

                        List<String> mData = mParse.parseParentNode(str_res, "Table");

                        if (mData.size() > 0) {
                            /*for (String strXMl: mData){
                                if (mData.indexOf(strXMl) == 0){
                                    str_exam_location = mParse.parseXmlTag(strXMl, "EXAM_CENTER_LOCATION");
                                }
                            }*/
                            return "1";
                        } else {
                            return "0";
                        }
                    } else {
                        return "0";
                    }
                } catch (Exception e) {
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
        protected void onPostExecute(String s) {

            if (mProgressDialog.isShowing())
                mProgressDialog.dismiss();

            if (running) {
                if (!s.equals("0")) {
                    new DownloadSignature().execute();
                } else {
                    ll_score_card_declaration.setVisibility(View.GONE);
                    mCommonMethods.showMessageDialog(mContext, "Invalid URN");
                }
            } else {
                ll_score_card_declaration.setVisibility(View.GONE);
                mCommonMethods.showMessageDialog(mContext, mCommonMethods.WEEK_INTERNET_MESSAGE);
            }
        }
    }

    class DownloadSignature extends AsyncTask<String, String, String> {

        private volatile boolean running = true;
        private String strRevivalListErrorCOde1 = "";
        private SoapObject soapObject;
        private SoapPrimitive soapPrimitive;
        private String inputpolicylist;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(mContext, ProgressDialog.THEME_HOLO_LIGHT);
            if (mProgressDialog != null && !mProgressDialog.isShowing()) {
                mProgressDialog.show();
            }
        }


        @Override
        protected String doInBackground(String... aurl) {
            try {

                running = true;
                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME_DOWNLOAD_SIGN);
                request.addProperty("strURN", str_urn_no);

                //commonMethods.appendSecurityParams(context,request,strCIFBDMEmailId,strCIFBDMMObileNo);

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);
                envelope.dotNet = true;

                envelope.setOutputSoapObject(request);

                mCommonMethods.TLSv12Enable();

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);

                HttpTransportSE androidHttpTranport = new HttpTransportSE(ServiceURL.SERVICE_URL);
                androidHttpTranport.call(NAMESPACE + METHOD_NAME_DOWNLOAD_SIGN, envelope);

                if (envelope.getResponse() instanceof SoapObject) {
                    soapObject = (SoapObject) envelope.getResponse();
                    inputpolicylist = soapObject.toString();
                } else {
                    soapPrimitive = (SoapPrimitive) envelope.getResponse();
                    inputpolicylist = soapPrimitive.toString();
                }

//                SoapPrimitive sa = (SoapPrimitive) envelope.getResponse();
//                String inputpolicylist = sa.toString();

                if (inputpolicylist != null && !inputpolicylist.equalsIgnoreCase("")) {
                    str_customer_sign = inputpolicylist;
                    //CaptureSignature.scaled = base64ToBitmap(str_customer_sign);
                    strRevivalListErrorCOde1 = "success";
                } else {
                    strRevivalListErrorCOde1 = "0";
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
                if (strRevivalListErrorCOde1 != null && strRevivalListErrorCOde1.equalsIgnoreCase("success")) {

                    imgbtn_tcc_declaration_cust_sign.setImageBitmap(base64ToBitmap(str_customer_sign));

                    new AsyncSchedule3PDF().execute();
                } else {
                    ll_score_card_declaration.setVisibility(View.GONE);
                    mCommonMethods.showMessageDialog(mContext, "Signature is not uploaded, Please upload through Document reupload tab");
                }
            } else {
                ll_score_card_declaration.setVisibility(View.GONE);
                mCommonMethods.showMessageDialog(mContext, "Signature is not uploaded, Please upload through Document reupload tab");
            }
        }
    }

    class AsyncSchedule3PDF extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(mContext, ProgressDialog.THEME_HOLO_LIGHT);
            if (mProgressDialog != null && !mProgressDialog.isShowing()) {
                mProgressDialog.show();
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            return createSchedule3PDF();
        }

        @Override
        protected void onPostExecute(String s) {
            if (mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }

            if (s.equals("")) {
                //get name from URN
                new AsynchGetCIFName().execute();
            } else {
                ll_score_card_declaration.setVisibility(View.GONE);
                mCommonMethods.showMessageDialog(mContext, s);
            }
        }
    }
}
