package sbilife.com.pointofsale_bancaagency.cifenrollment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.text.Html;
import android.util.Base64;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
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
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
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

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import sbilife.com.pointofsale_bancaagency.ParseXML;
import sbilife.com.pointofsale_bancaagency.R;
import sbilife.com.pointofsale_bancaagency.ServiceURL;
import sbilife.com.pointofsale_bancaagency.cifenrollment.phase1.AsyncUploadFile_CIF;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;
import sbilife.com.pointofsale_bancaagency.common.CompressImage;
import sbilife.com.pointofsale_bancaagency.common.FileLoader;
import sbilife.com.pointofsale_bancaagency.common.StorageUtils;

public class CIFDocUploadActivity extends AppCompatActivity implements View.OnClickListener, AsyncUploadFile_CIF.Interface_Upload_CIF_Files {
    private final static String TAG = CIFDocUploadActivity.class.getSimpleName();
    private final int REQUEST_CODE_PICK_PHOTO_FILE = 1;
    private final int REQUEST_CODE_PICK_FILE = 3, REQUEST_OCR = 100;
    private final String CHECK_TYPE_PHOTO = "Photo";
    private final String CHECK_TYPE_SIGN = "Sign";
    private final String CHECK_TYPE_MISMATCH_SIGN = "Mismatch_Sign";
    private final String CHECK_TYPE_PAN_CARD_CAPTURE = "PAN_CARD";
    private final String CHECK_TYPE_ID_CARD_CAPTURE = "SBI_ID_CARD";
    private final String FILE_HSC_DOC_NAME = "_HSC";
    private final String FILE_DEGREE_DOC_NAME = "_Degree";
    private final String SCHEDULE3_DOC = "_SCHEDULE3";
    private final String CHECK_TYPE_ANNEXURE_CAPTURE = "Annexure";
    private final String CHECK_TYPE_SCORE_CARD = "_SCORE_CARD";
    private final String CHECK_TYPE_TCC_CARD = "_TCC";
    private final String CHECK_TYPE_QUESTIONS_DOC = "questions_";
    private final String TYPE_URN = "URN";
    private final String TYPE_PF = "PF";
    private final String METHOD_NAME_UPLOAD_FILE_VALIDATE_PF = "getCIFEnrollmentInfo";
    private final String METHOD_NAME_DOWNLOAD_FILES = "getDoc_CIFEnroll";
    private final String METHOD_NAME_UPLOAD_FILE_VALIDATE_URN = "validateURN";
    private final String METHOD_NAME_UPLOAD_FILE_CIF = "UploadFile_CIFEnroll";
    private final String METHOD_NAME_UPLOAD_FILE_CIF_URN = "UploadFile_CIFEnroll_URN";
    private final String METHOD_NAME_UPLOAD_FILE_DOWNLOAD_SIGN = "getSignCIFenroll_smrt";
    private final String METHOD_NAME_UPLOAD_FILE_CIF_REQUIREMENT = "UploadFile_CIFEnroll_Req";
    private final String NAMESPACE = "http://tempuri.org/";
    private CommonMethods mCommonMethods;
    private StorageUtils mStorageUtils;
    private Context mContext;
    private EditText edt_cif_doc_upload_pf_number;
    private String str_pf_number = "", Check = "", OCR_TYPE = "", pf_urn_type = TYPE_PF, str_customer_sign = "";
    private File fileSign, f, fileMismatchSign, filePanCard, fileIDCard, fileHSCDoc, fileDegreeDoc,
            fileSchedule3Doc, fileAnnexure1Doc, fileScoreCard, fileTCC, fileQuestions;
    private byte[] mAllBytes;
    private Spinner spnr_cif_doc_reupload;

    private ImageButton ib_cif_doc_reupload_view, ib_cif_doc_reupload_capture, ib_cif_doc_reupload_browse,
            ib_cif_doc_reupload_submit, ib_cif_doc_reupload_download;

    private RadioGroup rg_cif_upload_downlaod_type;
    //Annexure 1 fit and proper
    private LinearLayout ll_annexure1_fit_proper, ll_schedule3;
    private Button btn_tcc_queries_submit, btn_tcc_declaration_submit;
    private CheckBox chk_tcc_que_a, chk_tcc_que_b, chk_tcc_que_c, chk_tcc_que_d,
            chk_tcc_que_e, chk_tcc_que_f, chk_tcc_que_g, chk_tcc_que_h, chk_tcc_que_i,
            chk_tcc_que_j, chk_tcc_que_k, chk_tcc_que_l, chk_tcc_que_m, chk_tcc_que_n, chk_tcc_que_o,
            chk_tcc_declaration;
    private EditText edt_tcc_query_place, edt_tcc_declaration_place;
    private TextView txt_tcc_query_date;
    private boolean isDownloadBtnClicked = false;
    private ProgressDialog mProgressDialog;

    //private WebView webviewDocument;
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

                        //2 MB valiadation
                        if (kilobyte < mCommonMethods.FILE_UPLOAD_RESTRICT_SIZE) {
                            if (!strMimeType.equals("application/octet-stream")
                                    && !strMimeType.equals("application/vnd.android.package-archive")) {

                                String imageFileName = str_pf_number + Check + str_extension;
                                new FileLoader(mContext, StorageUtils.DIRECT_DIRECTORY_CIF,
                                        imageFileName,
                                        new FileLoader.FileLoaderResponce() {
                                            @Override
                                            public void fileLoadFinished(File fileOutput) {
                                                if (fileOutput != null) {

                                                    if (Check.equals(CHECK_TYPE_PHOTO) || Check.equals(CHECK_TYPE_SIGN)) {

                                                        /*Object[] mObj = mCommonMethods.compressImageCIFbySize(mContext,
                                                                fileOutput.getAbsolutePath(), 40.0);
                                                        //Bitmap bmp = (Bitmap) mObj[0];
                                                        fileOutput = (File) mObj[1];*/

                                                        double kilobyte = fileOutput.length() / 1024;
                                                        //PHoto and Sign validation for 50K size
                                                        if (kilobyte <= mCommonMethods.FILE_UPLOAD_CIF_PHOTO_SIGN_RESTRICT_SIZE) {
                                                            if (Check.equals(CHECK_TYPE_PHOTO)) {
                                                                f = fileOutput;
                                                            } else if (Check.equals(CHECK_TYPE_SIGN)) {
                                                                fileSign = fileOutput;
                                                            }
                                                            ib_cif_doc_reupload_browse.setImageDrawable(getResources().getDrawable(R.drawable.checkedbrowse));
                                                            ib_cif_doc_reupload_capture.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_camera));
                                                        } else {
                                                            mCommonMethods.showMessageDialog(mContext, mCommonMethods.FILE_UPLOAD_CIF_PHOTO_SIGN_RESTRICT_SIZE_MSG);
                                                        }
                                                    } else {

                                                        if (str_extension.toLowerCase().equals(".jpg") || str_extension.toLowerCase().equals(".jpeg")
                                                                || str_extension.toLowerCase().equals(".png")) {
                                                            //compress image
                                                            CompressImage.compressImage(fileOutput.getAbsolutePath());
                                                        }

                                                        if (Check.equals(CHECK_TYPE_MISMATCH_SIGN)) {
                                                            fileMismatchSign = fileOutput;
                                                        } else if (Check.equals(CHECK_TYPE_PAN_CARD_CAPTURE)) {
                                                            filePanCard = fileOutput;
                                                        } else if (Check.equals(CHECK_TYPE_ID_CARD_CAPTURE)) {
                                                            fileIDCard = fileOutput;
                                                        } else if (Check.equals(FILE_HSC_DOC_NAME)) {
                                                            fileHSCDoc = fileOutput;
                                                        } else if (Check.equals(FILE_DEGREE_DOC_NAME)) {
                                                            fileDegreeDoc = fileOutput;
                                                        } else if (Check.equals(SCHEDULE3_DOC)) {
                                                            fileSchedule3Doc = fileOutput;
                                                        } else if (Check.equals(CHECK_TYPE_ANNEXURE_CAPTURE)) {
                                                            fileAnnexure1Doc = fileOutput;
                                                        } else if (Check.equals(CHECK_TYPE_SCORE_CARD)) {
                                                            fileScoreCard = fileOutput;
                                                        } else if (Check.equals(CHECK_TYPE_TCC_CARD)) {
                                                            fileTCC = fileOutput;
                                                        }

                                                        ib_cif_doc_reupload_browse.setImageDrawable(getResources().getDrawable(R.drawable.checkedbrowse));
                                                        ib_cif_doc_reupload_capture.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_camera));
                                                    }

                                                } else {
                                                    mCommonMethods.showMessageDialog(mContext, "File not found..");
                                                }
                                            }
                                        }).execute(mSelectedUri);

                            } else {
                                mCommonMethods.showToast(mContext, ".exe/.apk file format not acceptable");
                            }
                        } else {
                            mCommonMethods.showMessageDialog(mContext, mCommonMethods.FILE_UPLOAD_RESTRICT_SIZE_MSG);
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
        setContentView(R.layout.activity_cifdoc_upload);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);

        mContext = this;
        mCommonMethods = new CommonMethods();
        mStorageUtils = new StorageUtils();
        mCommonMethods.setApplicationToolbarMenu(this, "Document Re-Upload");

        edt_cif_doc_upload_pf_number = findViewById(R.id.edt_cif_doc_upload_pf_number);

        spnr_cif_doc_reupload = findViewById(R.id.spnr_cif_doc_reupload);
        spnr_cif_doc_reupload.setAdapter(ArrayAdapter.createFromResource(mContext,
                R.array.cif_reupload_doc_array, R.layout.spinner_large_words));

        ArrayAdapter<String> reupload_adapter = new ArrayAdapter<String>(
                mContext, R.layout.spinner_aob, getResources().getStringArray(R.array.cif_reupload_doc_array));
        spnr_cif_doc_reupload.setAdapter(reupload_adapter);
        reupload_adapter.notifyDataSetChanged();
        spnr_cif_doc_reupload.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                ll_annexure1_fit_proper.setVisibility(View.GONE);
                ll_schedule3.setVisibility(View.GONE);

                ib_cif_doc_reupload_view.setVisibility(View.VISIBLE);
                ib_cif_doc_reupload_capture.setVisibility(View.VISIBLE);
                ib_cif_doc_reupload_browse.setVisibility(View.VISIBLE);
                ib_cif_doc_reupload_submit.setVisibility(View.VISIBLE);

                switch (i) {
                    case 1:
                        rg_cif_upload_downlaod_type.setVisibility(View.GONE);
                        edt_cif_doc_upload_pf_number.setHint("* Please enter PF Number");
                        Check = CHECK_TYPE_PHOTO;
                        break;

                    case 2:
                        rg_cif_upload_downlaod_type.setVisibility(View.GONE);
                        edt_cif_doc_upload_pf_number.setHint("* Please enter PF Number");
                        Check = CHECK_TYPE_SIGN;
                        break;

                    case 3:
                        rg_cif_upload_downlaod_type.setVisibility(View.GONE);
                        edt_cif_doc_upload_pf_number.setHint("* Please enter PF Number");
                        Check = CHECK_TYPE_MISMATCH_SIGN;
                        break;

                    case 4:
                        rg_cif_upload_downlaod_type.setVisibility(View.GONE);
                        edt_cif_doc_upload_pf_number.setHint("* Please enter PF Number");
                        Check = CHECK_TYPE_PAN_CARD_CAPTURE;
                        break;

                    case 5:
                        rg_cif_upload_downlaod_type.setVisibility(View.GONE);
                        edt_cif_doc_upload_pf_number.setHint("* Please enter PF Number");
                        Check = CHECK_TYPE_ID_CARD_CAPTURE;
                        break;

                    case 6:
                        rg_cif_upload_downlaod_type.setVisibility(View.VISIBLE);
                        edt_cif_doc_upload_pf_number.setHint("* Please enter PF / URN Number");
                        Check = FILE_HSC_DOC_NAME;
                        break;

                    case 7:
                        rg_cif_upload_downlaod_type.setVisibility(View.VISIBLE);
                        edt_cif_doc_upload_pf_number.setHint("* Please enter PF / URN Number");
                        Check = FILE_DEGREE_DOC_NAME;
                        break;

                    case 8:
                        initialiseSchedule3Part();
                        rg_cif_upload_downlaod_type.setVisibility(View.VISIBLE);
                        edt_cif_doc_upload_pf_number.setHint("* Please enter PF / URN Number");
                        ll_schedule3.setVisibility(View.VISIBLE);
                        Check = SCHEDULE3_DOC;
                        break;

                    /*case 9:
                        rg_cif_upload_downlaod_type.setVisibility(View.GONE);
                        edt_cif_doc_upload_pf_number.setHint("* Please enter PF Number");
                        Check = CHECK_TYPE_ANNEXURE_CAPTURE;
                        break;*/

                    case 9:
                        rg_cif_upload_downlaod_type.setVisibility(View.GONE);
                        edt_cif_doc_upload_pf_number.setHint("* Please enter URN Number");
                        Check = CHECK_TYPE_SCORE_CARD;
                        pf_urn_type = TYPE_URN;
                        break;

                    case 10:
                        rg_cif_upload_downlaod_type.setVisibility(View.GONE);
                        edt_cif_doc_upload_pf_number.setHint("* Please enter URN Number");
                        Check = CHECK_TYPE_TCC_CARD;
                        pf_urn_type = TYPE_URN;
                        break;

                    case 11:
                        initialiseAnnexure1Part();
                        rg_cif_upload_downlaod_type.setVisibility(View.VISIBLE);
                        edt_cif_doc_upload_pf_number.setHint("* Please enter PF / URN Number");
                        ll_annexure1_fit_proper.setVisibility(View.VISIBLE);
                        Check = CHECK_TYPE_QUESTIONS_DOC;
                        break;

                    default:
                        rg_cif_upload_downlaod_type.setVisibility(View.GONE);
                        edt_cif_doc_upload_pf_number.setHint("* Please enter PF Number");
                        ll_annexure1_fit_proper.setVisibility(View.GONE);
                        ll_schedule3.setVisibility(View.GONE);
                        Check = "";
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ib_cif_doc_reupload_view = findViewById(R.id.ib_cif_doc_reupload_view);
        ib_cif_doc_reupload_view.setOnClickListener(this);

        ib_cif_doc_reupload_capture = findViewById(R.id.ib_cif_doc_reupload_capture);
        ib_cif_doc_reupload_capture.setOnClickListener(this);

        ib_cif_doc_reupload_browse = findViewById(R.id.ib_cif_doc_reupload_browse);
        ib_cif_doc_reupload_browse.setOnClickListener(this);

        ib_cif_doc_reupload_submit = findViewById(R.id.ib_cif_doc_reupload_submit);
        ib_cif_doc_reupload_submit.setOnClickListener(this);

        ib_cif_doc_reupload_download = findViewById(R.id.ib_cif_doc_reupload_download);
        ib_cif_doc_reupload_download.setOnClickListener(this);

        rg_cif_upload_downlaod_type = findViewById(R.id.rg_cif_upload_downlaod_type);

        rg_cif_upload_downlaod_type.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_cif_upload_downlaod_type_pf:
                        pf_urn_type = TYPE_PF;
                        break;

                    case R.id.rb_cif_upload_downlaod_type_urn:
                        pf_urn_type = TYPE_URN;
                        break;

                    default:
                        break;
                }
            }
        });

        ll_annexure1_fit_proper = findViewById(R.id.ll_annexure1_fit_proper);
        ll_annexure1_fit_proper.setVisibility(View.GONE);

        ll_schedule3 = findViewById(R.id.ll_schedule3);
        ll_schedule3.setVisibility(View.GONE);
    }

    private void initialiseAnnexure1Part() {

        ib_cif_doc_reupload_view.setVisibility(View.GONE);
        ib_cif_doc_reupload_capture.setVisibility(View.GONE);
        ib_cif_doc_reupload_browse.setVisibility(View.GONE);
        ib_cif_doc_reupload_submit.setVisibility(View.GONE);

        btn_tcc_queries_submit = findViewById(R.id.btn_tcc_queries_submit);

        chk_tcc_que_a = findViewById(R.id.chk_tcc_que_a);
        chk_tcc_que_b = findViewById(R.id.chk_tcc_que_b);
        chk_tcc_que_c = findViewById(R.id.chk_tcc_que_c);
        chk_tcc_que_d = findViewById(R.id.chk_tcc_que_d);
        chk_tcc_que_e = findViewById(R.id.chk_tcc_que_e);
        chk_tcc_que_f = findViewById(R.id.chk_tcc_que_f);
        chk_tcc_que_g = findViewById(R.id.chk_tcc_que_g);
        chk_tcc_que_h = findViewById(R.id.chk_tcc_que_h);
        chk_tcc_que_i = findViewById(R.id.chk_tcc_que_i);
        chk_tcc_que_j = findViewById(R.id.chk_tcc_que_j);
        chk_tcc_que_k = findViewById(R.id.chk_tcc_que_k);
        chk_tcc_que_l = findViewById(R.id.chk_tcc_que_l);
        chk_tcc_que_m = findViewById(R.id.chk_tcc_que_m);
        chk_tcc_que_n = findViewById(R.id.chk_tcc_que_n);
        chk_tcc_que_o = findViewById(R.id.chk_tcc_que_o);

        /*edt_tcc_query_name = (EditText) findViewById(R.id.edt_tcc_query_name);*/
        edt_tcc_query_place = findViewById(R.id.edt_tcc_query_place);
        txt_tcc_query_date = findViewById(R.id.txt_tcc_query_date);
        btn_tcc_queries_submit.setOnClickListener(this);

        Calendar cal = Calendar.getInstance();
        String mont = ((cal.get(Calendar.MONTH) + 1) < 10 ? "0" : "") + (cal.get(Calendar.MONTH) + 1);
        String day = (cal.get(Calendar.DAY_OF_MONTH) < 10 ? "0" : "") + cal.get(Calendar.DAY_OF_MONTH);

        txt_tcc_query_date.setText(day + "-" + mont + "-" + cal.get(Calendar.YEAR));
    }

    private boolean create_tcc_questions_Pdf() {

        boolean isError = true;

        try {

            Font headerBold = new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.BOLD);
            Font small_bold = new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.BOLD);
            Font small_bold1 = new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.BOLD);
            Font small_bold2 = new Font(Font.FontFamily.TIMES_ROMAN, 4, Font.BOLD);
            Font small_normal = new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.NORMAL);

            File quesFile = mStorageUtils.createFileToAppSpecificDirCIF(mContext,
                    CHECK_TYPE_QUESTIONS_DOC + str_pf_number + ".pdf");

            Rectangle rect = new Rectangle(594f, 792f);

            Document document = new Document(rect, 50, 50, 50, 50);
            // Document document = new Document(PageSize.A4, 50, 50, 50, 50);
            PdfWriter pdf_writer = null;
            pdf_writer = PdfWriter.getInstance(document, new FileOutputStream(quesFile.getPath()));

            document.open();
            /*// For SBI- Life Logo starts
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            Bitmap bitmap = BitmapFactory.decodeResource(getBaseContext().getResources(), R.drawable.sbi_life_logo);
            bitmap.compress(Bitmap.CompressFormat.PNG, 50, stream);
            Image img_sbi_logo = Image.getInstance(stream.toByteArray());
            img_sbi_logo.setAlignment(Image.LEFT);
            img_sbi_logo.getSpacingAfter();
            img_sbi_logo.scaleToFit(80, 50);

            Paragraph para_img_logo = new Paragraph("");
            para_img_logo.add(img_sbi_logo);

            document.add(para_img_logo);
            // For SBI- Life Logo ends*/

            Paragraph para_img_logo_after_space_1 = new Paragraph(" ");

            // To draw line after the sbi logo image
            /*document.add(new LineSeparator());
            document.add(para_img_logo_after_space_1);*/

            //to tittle 1
            Paragraph para_tittle_1 = new Paragraph("Annexture - 1", headerBold);
            para_tittle_1.setAlignment(Element.ALIGN_RIGHT);
            document.add(para_tittle_1);
            document.add(para_img_logo_after_space_1);

            Paragraph para_tittle_1_a = new Paragraph("[See regulafion 7(2)(g)]", headerBold);
            para_tittle_1_a.setAlignment(Element.ALIGN_CENTER);
            document.add(para_tittle_1_a);
            document.add(para_img_logo_after_space_1);

            //to tittle 2
            Paragraph para_tittle_2 = new Paragraph("INSURANCE REGULATORY AND DEVELOPMENT AUTHORITY OF INDIA (REGISTRATION OF CORPORATE AGENT) REGULATIONS, 2015", small_bold);
            para_tittle_2.setAlignment(Element.ALIGN_CENTER);
            document.add(para_tittle_2);
            document.add(para_img_logo_after_space_1);

            //to tittle 3
            Paragraph para_tittle_3 = new Paragraph("Declaration and Undertaking of Specified Persons/CIF (A separate form needs to be submitted by each individual)", small_normal);
            para_tittle_3.setAlignment(Element.ALIGN_LEFT);
            document.add(para_tittle_3);
            document.add(para_img_logo_after_space_1);

            //to Question details
            PdfPTable BI_PdfQuestionDetails = new PdfPTable(3);
            BI_PdfQuestionDetails.setWidthPercentage(100);
            BI_PdfQuestionDetails.setWidths(new int[]{1, 2, 1});

            PdfPCell BI_PdftableSrNo_cell = new PdfPCell(new Paragraph("Sr. No.", small_normal));
            BI_PdftableSrNo_cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            BI_PdftableSrNo_cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableSrNo_cell.setVerticalAlignment(Element.ALIGN_CENTER);
            BI_PdftableSrNo_cell.setPadding(5);

            PdfPCell BI_PdftableQuestion_cell = new PdfPCell(new Paragraph("Fit & Proper Certificate", small_normal));
            BI_PdftableQuestion_cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            BI_PdftableQuestion_cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableQuestion_cell.setVerticalAlignment(Element.ALIGN_CENTER);
            BI_PdftableQuestion_cell.setPadding(5);

            PdfPCell BI_PdftableAns_cell = new PdfPCell(new Paragraph("Yes/No", small_normal));
            BI_PdftableAns_cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            BI_PdftableAns_cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableAns_cell.setVerticalAlignment(Element.ALIGN_CENTER);
            BI_PdftableAns_cell.setPadding(5);

            BI_PdfQuestionDetails.addCell(BI_PdftableSrNo_cell);
            BI_PdfQuestionDetails.addCell(BI_PdftableQuestion_cell);
            BI_PdfQuestionDetails.addCell(BI_PdftableAns_cell);
            document.add(BI_PdfQuestionDetails);

            //to Question details a
            PdfPTable BI_PdfQuestionDetails_a = new PdfPTable(3);
            BI_PdfQuestionDetails_a.setWidthPercentage(100);
            BI_PdfQuestionDetails_a.setWidths(new int[]{1, 2, 1});

            PdfPCell BI_PdftableSrNo_cell_a = new PdfPCell(new Paragraph("a)", small_normal));
            //BI_PdftableSrNo_cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            BI_PdftableSrNo_cell_a.setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableSrNo_cell_a.setVerticalAlignment(Element.ALIGN_CENTER);
            BI_PdftableSrNo_cell_a.setPadding(5);

            PdfPCell BI_PdftableQuestion_cell_a = new PdfPCell(new Paragraph("Have you ever registered or obtained license from any of the regulatory authorities under any law such as SEBI, RBI, IRDA, PFRDA, FMC etc.?", small_normal));
            //BI_PdftableQuestion_cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            //BI_PdftableQuestion_cell_a.setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableQuestion_cell_a.setPadding(5);

            PdfPCell BI_PdftableAns_cell_a;
            if (chk_tcc_que_a.isChecked())
                BI_PdftableAns_cell_a = new PdfPCell(new Paragraph("Yes", small_normal));
            else
                BI_PdftableAns_cell_a = new PdfPCell(new Paragraph("No", small_normal));
            //BI_PdftableAns_cell_a.setBackgroundColor(BaseColor.LIGHT_GRAY);
            BI_PdftableAns_cell_a.setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableAns_cell_a.setVerticalAlignment(Element.ALIGN_CENTER);
            BI_PdftableAns_cell_a.setPadding(5);

            BI_PdfQuestionDetails_a.addCell(BI_PdftableSrNo_cell_a);
            BI_PdfQuestionDetails_a.addCell(BI_PdftableQuestion_cell_a);
            BI_PdfQuestionDetails_a.addCell(BI_PdftableAns_cell_a);
            document.add(BI_PdfQuestionDetails_a);

            //to Question details b
            PdfPTable BI_PdfQuestionDetails_b = new PdfPTable(3);
            BI_PdfQuestionDetails_b.setWidthPercentage(100);
            BI_PdfQuestionDetails_b.setWidths(new int[]{1, 2, 1});

            PdfPCell BI_PdftableSrNo_cell_b = new PdfPCell(new Paragraph("b)", small_normal));
            //BI_PdftableSrNo_cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            BI_PdftableSrNo_cell_b.setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableSrNo_cell_b.setVerticalAlignment(Element.ALIGN_CENTER);
            BI_PdftableSrNo_cell_b.setPadding(5);

            PdfPCell BI_PdftableQuestion_cell_b = new PdfPCell(new Paragraph("Have you carried on business under any name other than the name stated in this application?", small_normal));
            //BI_PdftableQuestion_cell_b.setBackgroundColor(BaseColor.LIGHT_GRAY);
            //BI_PdftableQuestion_cell_b.setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableQuestion_cell_b.setPadding(5);

            PdfPCell BI_PdftableAns_cell_b;
            if (chk_tcc_que_b.isChecked())
                BI_PdftableAns_cell_b = new PdfPCell(new Paragraph("Yes", small_normal));
            else
                BI_PdftableAns_cell_b = new PdfPCell(new Paragraph("No", small_normal));

            //BI_PdftableAns_cell_b.setBackgroundColor(BaseColor.LIGHT_GRAY);
            BI_PdftableAns_cell_b.setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableAns_cell_b.setVerticalAlignment(Element.ALIGN_CENTER);
            BI_PdftableAns_cell_b.setPadding(5);

            BI_PdfQuestionDetails_b.addCell(BI_PdftableSrNo_cell_b);
            BI_PdfQuestionDetails_b.addCell(BI_PdftableQuestion_cell_b);
            BI_PdfQuestionDetails_b.addCell(BI_PdftableAns_cell_b);
            document.add(BI_PdfQuestionDetails_b);

            //to Question details c
            PdfPTable BI_PdfQuestionDetails_c = new PdfPTable(3);
            BI_PdfQuestionDetails_c.setWidthPercentage(100);
            BI_PdfQuestionDetails_c.setWidths(new int[]{1, 2, 1});

            PdfPCell BI_PdftableSrNo_cell_c = new PdfPCell(new Paragraph("c)", small_normal));
            //BI_PdftableSrNo_cell_c.setBackgroundColor(BaseColor.LIGHT_GRAY);
            BI_PdftableSrNo_cell_c.setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableSrNo_cell_c.setVerticalAlignment(Element.ALIGN_CENTER);
            BI_PdftableSrNo_cell_c.setPadding(5);

            PdfPCell BI_PdftableQuestion_cell_c = new PdfPCell(new Paragraph("Have you ever been refused or restricted by any regulatory authority to carry on any business, trade or profession for which a specific license registration or other authorization is required by law?", small_normal));
            //BI_PdftableQuestion_cell_c.setBackgroundColor(BaseColor.LIGHT_GRAY);
            //BI_PdftableQuestion_cell_c.setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableQuestion_cell_c.setPadding(5);

            PdfPCell BI_PdftableAns_cell_c;
            if (chk_tcc_que_c.isChecked())
                BI_PdftableAns_cell_c = new PdfPCell(new Paragraph("Yes", small_normal));
            else
                BI_PdftableAns_cell_c = new PdfPCell(new Paragraph("No", small_normal));

            //BI_PdftableAns_cell_c.setBackgroundColor(BaseColor.LIGHT_GRAY);
            BI_PdftableAns_cell_c.setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableAns_cell_c.setVerticalAlignment(Element.ALIGN_CENTER);
            BI_PdftableAns_cell_c.setPadding(5);

            BI_PdfQuestionDetails_c.addCell(BI_PdftableSrNo_cell_c);
            BI_PdfQuestionDetails_c.addCell(BI_PdftableQuestion_cell_c);
            BI_PdfQuestionDetails_c.addCell(BI_PdftableAns_cell_c);
            document.add(BI_PdfQuestionDetails_c);

            //to Question details
            PdfPTable BI_PdfQuestionDetails_d = new PdfPTable(3);
            BI_PdfQuestionDetails_d.setWidthPercentage(100);
            BI_PdfQuestionDetails_d.setWidths(new int[]{1, 2, 1});

            PdfPCell BI_PdftableSrNo_cell_d = new PdfPCell(new Paragraph("d)", small_normal));
            //BI_PdftableSrNo_cell_d.setBackgroundColor(BaseColor.LIGHT_GRAY);
            BI_PdftableSrNo_cell_d.setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableSrNo_cell_d.setVerticalAlignment(Element.ALIGN_CENTER);
            BI_PdftableSrNo_cell_d.setPadding(5);

            PdfPCell BI_PdftableQuestion_cell_d = new PdfPCell(new Paragraph("Have you been ever censured or disciplined or suspended or refused permission or license or registration by any regulatory authority to carry on any business activity?", small_normal));
            //BI_PdftableQuestion_cell_d.setBackgroundColor(BaseColor.LIGHT_GRAY);
            //BI_PdftableQuestion_cell_d.setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableQuestion_cell_d.setPadding(5);

            PdfPCell BI_PdftableAns_cell_d;
            if (chk_tcc_que_d.isChecked())
                BI_PdftableAns_cell_d = new PdfPCell(new Paragraph("Yes", small_normal));
            else
                BI_PdftableAns_cell_d = new PdfPCell(new Paragraph("No", small_normal));
            //BI_PdftableAns_cell_d.setBackgroundColor(BaseColor.LIGHT_GRAY);
            BI_PdftableAns_cell_d.setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableAns_cell_d.setVerticalAlignment(Element.ALIGN_CENTER);
            BI_PdftableAns_cell_d.setPadding(5);

            BI_PdfQuestionDetails_d.addCell(BI_PdftableSrNo_cell_d);
            BI_PdfQuestionDetails_d.addCell(BI_PdftableQuestion_cell_d);
            BI_PdfQuestionDetails_d.addCell(BI_PdftableAns_cell_d);
            document.add(BI_PdfQuestionDetails_d);

            //to Question details e
            PdfPTable BI_PdfQuestionDetails_e = new PdfPTable(3);
            BI_PdfQuestionDetails_e.setWidthPercentage(100);
            BI_PdfQuestionDetails_e.setWidths(new int[]{1, 2, 1});

            PdfPCell BI_PdftableSrNo_cell_e = new PdfPCell(new Paragraph("e)", small_normal));
            //BI_PdftableSrNo_cell_e.setBackgroundColor(BaseColor.LIGHT_GRAY);
            BI_PdftableSrNo_cell_e.setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableSrNo_cell_e.setVerticalAlignment(Element.ALIGN_CENTER);
            BI_PdftableSrNo_cell_e.setPadding(5);

            PdfPCell BI_PdftableQuestion_cell_e = new PdfPCell(new Paragraph("Have you been subject to any investigations or disciplinary proceeding or have been issued warning or reprimand by any regulatory authority?", small_normal));
            //BI_PdftableQuestion_cell_e.setBackgroundColor(BaseColor.LIGHT_GRAY);
            //BI_PdftableQuestion_cell_e.setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableQuestion_cell_e.setPadding(5);

            PdfPCell BI_PdftableAns_cell_e;
            if (chk_tcc_que_e.isChecked())
                BI_PdftableAns_cell_e = new PdfPCell(new Paragraph("Yes", small_normal));
            else
                BI_PdftableAns_cell_e = new PdfPCell(new Paragraph("No", small_normal));

            //BI_PdftableAns_cell_e.setBackgroundColor(BaseColor.LIGHT_GRAY);
            BI_PdftableAns_cell_e.setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableAns_cell_e.setVerticalAlignment(Element.ALIGN_CENTER);
            BI_PdftableAns_cell_e.setPadding(5);

            BI_PdfQuestionDetails_e.addCell(BI_PdftableSrNo_cell_e);
            BI_PdfQuestionDetails_e.addCell(BI_PdftableQuestion_cell_e);
            BI_PdfQuestionDetails_e.addCell(BI_PdftableAns_cell_e);
            document.add(BI_PdfQuestionDetails_e);

            //to Question details f
            PdfPTable BI_PdfQuestionDetails_f = new PdfPTable(3);
            BI_PdfQuestionDetails_f.setWidthPercentage(100);
            BI_PdfQuestionDetails_f.setWidths(new int[]{1, 2, 1});

            PdfPCell BI_PdftableSrNo_cell_f = new PdfPCell(new Paragraph("f)", small_normal));
            //BI_PdftableSrNo_cell_f.setBackgroundColor(BaseColor.LIGHT_GRAY);
            BI_PdftableSrNo_cell_f.setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableSrNo_cell_f.setVerticalAlignment(Element.ALIGN_CENTER);
            BI_PdftableSrNo_cell_f.setPadding(5);

            PdfPCell BI_PdftableQuestion_cell_f = new PdfPCell(new Paragraph("Have you been convicted of any offence or subject to any pending proceedings under any law?", small_normal));
            //BI_PdftableQuestion_cell_f.setBackgroundColor(BaseColor.LIGHT_GRAY);
            //BI_PdftableQuestion_cell_f.setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableQuestion_cell_f.setPadding(5);

            PdfPCell BI_PdftableAns_cell_f;
            if (chk_tcc_que_f.isChecked())
                BI_PdftableAns_cell_f = new PdfPCell(new Paragraph("Yes", small_normal));
            else
                BI_PdftableAns_cell_f = new PdfPCell(new Paragraph("No", small_normal));

            //BI_PdftableAns_cell_f.setBackgroundColor(BaseColor.LIGHT_GRAY);
            BI_PdftableAns_cell_f.setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableAns_cell_f.setVerticalAlignment(Element.ALIGN_CENTER);
            BI_PdftableAns_cell_f.setPadding(5);

            BI_PdfQuestionDetails_f.addCell(BI_PdftableSrNo_cell_f);
            BI_PdfQuestionDetails_f.addCell(BI_PdftableQuestion_cell_f);
            BI_PdfQuestionDetails_f.addCell(BI_PdftableAns_cell_f);
            document.add(BI_PdfQuestionDetails_f);

            //to Question details g
            PdfPTable BI_PdfQuestionDetails_g = new PdfPTable(3);
            BI_PdfQuestionDetails_g.setWidthPercentage(100);
            BI_PdfQuestionDetails_g.setWidths(new int[]{1, 2, 1});

            PdfPCell BI_PdftableSrNo_cell_g = new PdfPCell(new Paragraph("g)", small_normal));
            //BI_PdftableSrNo_cell_g.setBackgroundColor(BaseColor.LIGHT_GRAY);
            BI_PdftableSrNo_cell_g.setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableSrNo_cell_g.setVerticalAlignment(Element.ALIGN_CENTER);
            BI_PdftableSrNo_cell_g.setPadding(5);

            PdfPCell BI_PdftableQuestion_cell_g = new PdfPCell(new Paragraph("Have you been banned from entry from any profession / occupation at any time?", small_normal));
            //BI_PdftableQuestion_cell_g.setBackgroundColor(BaseColor.LIGHT_GRAY);
            //BI_PdftableQuestion_cell_g.setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableQuestion_cell_g.setPadding(5);

            PdfPCell BI_PdftableAns_cell_g;
            if (chk_tcc_que_g.isChecked())
                BI_PdftableAns_cell_g = new PdfPCell(new Paragraph("Yes", small_normal));
            else
                BI_PdftableAns_cell_g = new PdfPCell(new Paragraph("No", small_normal));
            //BI_PdftableAns_cell_g.setBackgroundColor(BaseColor.LIGHT_GRAY);
            BI_PdftableAns_cell_g.setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableAns_cell_g.setVerticalAlignment(Element.ALIGN_CENTER);
            BI_PdftableAns_cell_g.setPadding(5);

            BI_PdfQuestionDetails_g.addCell(BI_PdftableSrNo_cell_g);
            BI_PdfQuestionDetails_g.addCell(BI_PdftableQuestion_cell_g);
            BI_PdfQuestionDetails_g.addCell(BI_PdftableAns_cell_g);
            document.add(BI_PdfQuestionDetails_g);

            //to Question details h
            PdfPTable BI_PdfQuestionDetails_h = new PdfPTable(3);
            BI_PdfQuestionDetails_h.setWidthPercentage(100);
            BI_PdfQuestionDetails_h.setWidths(new int[]{1, 2, 1});

            PdfPCell BI_PdftableSrNo_cell_h = new PdfPCell(new Paragraph("h)", small_normal));
            //BI_PdftableSrNo_cell_h.setBackgroundColor(BaseColor.LIGHT_GRAY);
            BI_PdftableSrNo_cell_h.setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableSrNo_cell_h.setVerticalAlignment(Element.ALIGN_CENTER);
            BI_PdftableSrNo_cell_h.setPadding(5);

            PdfPCell BI_PdftableQuestion_cell_h = new PdfPCell(new Paragraph("Details of prosecution, if any, pending or commenced or resulting in conviction in the past for violation of economic laws and regulations?", small_normal));
            //BI_PdftableQuestion_cell_h.setBackgroundColor(BaseColor.LIGHT_GRAY);
            //BI_PdftableQuestion_cell_h.setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableQuestion_cell_h.setPadding(5);

            PdfPCell BI_PdftableAns_cell_h;
            if (chk_tcc_que_h.isChecked())
                BI_PdftableAns_cell_h = new PdfPCell(new Paragraph("Yes", small_normal));
            else
                BI_PdftableAns_cell_h = new PdfPCell(new Paragraph("No", small_normal));
            //BI_PdftableAns_cell_h.setBackgroundColor(BaseColor.LIGHT_GRAY);
            BI_PdftableAns_cell_h.setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableAns_cell_h.setVerticalAlignment(Element.ALIGN_CENTER);
            BI_PdftableAns_cell_h.setPadding(5);

            BI_PdfQuestionDetails_h.addCell(BI_PdftableSrNo_cell_h);
            BI_PdfQuestionDetails_h.addCell(BI_PdftableQuestion_cell_h);
            BI_PdfQuestionDetails_h.addCell(BI_PdftableAns_cell_h);
            document.add(BI_PdfQuestionDetails_h);

            //to Question details i
            PdfPTable BI_PdfQuestionDetails_i = new PdfPTable(3);
            BI_PdfQuestionDetails_i.setWidthPercentage(100);
            BI_PdfQuestionDetails_i.setWidths(new int[]{1, 2, 1});

            PdfPCell BI_PdftableSrNo_cell_i = new PdfPCell(new Paragraph("i)", small_normal));
            //BI_PdftableSrNo_cell_i.setBackgroundColor(BaseColor.LIGHT_GRAY);
            BI_PdftableSrNo_cell_i.setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableSrNo_cell_i.setVerticalAlignment(Element.ALIGN_CENTER);
            BI_PdftableSrNo_cell_i.setPadding(5);

            PdfPCell BI_PdftableQuestion_cell_i = new PdfPCell(new Paragraph("Details of  criminal prosecution, if  any, pending or commenced or resulting in conviction in the past against the applicant?", small_normal));
            //BI_PdftableQuestion_cell_i.setBackgroundColor(BaseColor.LIGHT_GRAY);
            //BI_PdftableQuestion_cell_i.setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableQuestion_cell_i.setPadding(5);

            PdfPCell BI_PdftableAns_cell_i;
            if (chk_tcc_que_i.isChecked())
                BI_PdftableAns_cell_i = new PdfPCell(new Paragraph("Yes", small_normal));
            else
                BI_PdftableAns_cell_i = new PdfPCell(new Paragraph("No", small_normal));
            //BI_PdftableAns_cell_i.setBackgroundColor(BaseColor.LIGHT_GRAY);
            BI_PdftableAns_cell_i.setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableAns_cell_i.setVerticalAlignment(Element.ALIGN_CENTER);
            BI_PdftableAns_cell_i.setPadding(5);

            BI_PdfQuestionDetails_i.addCell(BI_PdftableSrNo_cell_i);
            BI_PdfQuestionDetails_i.addCell(BI_PdftableQuestion_cell_i);
            BI_PdfQuestionDetails_i.addCell(BI_PdftableAns_cell_i);
            document.add(BI_PdfQuestionDetails_i);

            //to Question details j
            PdfPTable BI_PdfQuestionDetails_j = new PdfPTable(3);
            BI_PdfQuestionDetails_j.setWidthPercentage(100);
            BI_PdfQuestionDetails_j.setWidths(new int[]{1, 2, 1});

            PdfPCell BI_PdftableSrNo_cell_j = new PdfPCell(new Paragraph("j)", small_normal));
            //BI_PdftableSrNo_cell_j.setBackgroundColor(BaseColor.LIGHT_GRAY);
            BI_PdftableSrNo_cell_j.setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableSrNo_cell_j.setVerticalAlignment(Element.ALIGN_CENTER);
            BI_PdftableSrNo_cell_j.setPadding(5);

            PdfPCell BI_PdftableQuestion_cell_j = new PdfPCell(new Paragraph("Do you attract any of the disqualifications envisaged under Section 164 of the Companies’ Act 2013?", small_normal));
            //BI_PdftableQuestion_cell_j.setBackgroundColor(BaseColor.LIGHT_GRAY);
            //BI_PdftableQuestion_cell_j.setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableQuestion_cell_j.setPadding(5);

            PdfPCell BI_PdftableAns_cell_j;
            if (chk_tcc_que_j.isChecked())
                BI_PdftableAns_cell_j = new PdfPCell(new Paragraph("Yes", small_normal));
            else
                BI_PdftableAns_cell_j = new PdfPCell(new Paragraph("No", small_normal));
            //BI_PdftableAns_cell_j.setBackgroundColor(BaseColor.LIGHT_GRAY);
            BI_PdftableAns_cell_j.setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableAns_cell_j.setVerticalAlignment(Element.ALIGN_CENTER);
            BI_PdftableAns_cell_j.setPadding(5);

            BI_PdfQuestionDetails_j.addCell(BI_PdftableSrNo_cell_j);
            BI_PdfQuestionDetails_j.addCell(BI_PdftableQuestion_cell_j);
            BI_PdfQuestionDetails_j.addCell(BI_PdftableAns_cell_j);
            document.add(BI_PdfQuestionDetails_j);

            //to Question details k
            PdfPTable BI_PdfQuestionDetails_k = new PdfPTable(3);
            BI_PdfQuestionDetails_k.setWidthPercentage(100);
            BI_PdfQuestionDetails_k.setWidths(new int[]{1, 2, 1});

            PdfPCell BI_PdftableSrNo_cell_k = new PdfPCell(new Paragraph("k)", small_normal));
            //BI_PdftableSrNo_cell_k.setBackgroundColor(BaseColor.LIGHT_GRAY);
            BI_PdftableSrNo_cell_k.setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableSrNo_cell_k.setVerticalAlignment(Element.ALIGN_CENTER);
            BI_PdftableSrNo_cell_k.setPadding(5);

            PdfPCell BI_PdftableQuestion_cell_k = new PdfPCell(new Paragraph("Have you been subject to any investigation at the instance of Government department or agency?", small_normal));
            //BI_PdftableQuestion_cell_k.setBackgroundColor(BaseColor.LIGHT_GRAY);
            //BI_PdftableQuestion_cell_k.setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableQuestion_cell_k.setPadding(5);

            PdfPCell BI_PdftableAns_cell_k;
            if (chk_tcc_que_k.isChecked())
                BI_PdftableAns_cell_k = new PdfPCell(new Paragraph("Yes", small_normal));
            else
                BI_PdftableAns_cell_k = new PdfPCell(new Paragraph("No", small_normal));
            //BI_PdftableAns_cell_k.setBackgroundColor(BaseColor.LIGHT_GRAY);
            BI_PdftableAns_cell_k.setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableAns_cell_k.setVerticalAlignment(Element.ALIGN_CENTER);
            BI_PdftableAns_cell_k.setPadding(5);

            BI_PdfQuestionDetails_k.addCell(BI_PdftableSrNo_cell_k);
            BI_PdfQuestionDetails_k.addCell(BI_PdftableQuestion_cell_k);
            BI_PdfQuestionDetails_k.addCell(BI_PdftableAns_cell_k);
            document.add(BI_PdfQuestionDetails_k);

            //to Question details l
            PdfPTable BI_PdfQuestionDetails_l = new PdfPTable(3);
            BI_PdfQuestionDetails_l.setWidthPercentage(100);
            BI_PdfQuestionDetails_l.setWidths(new int[]{1, 2, 1});

            PdfPCell BI_PdftableSrNo_cell_l = new PdfPCell(new Paragraph("l)", small_normal));
            //BI_PdftableSrNo_cell_l.setBackgroundColor(BaseColor.LIGHT_GRAY);
            BI_PdftableSrNo_cell_l.setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableSrNo_cell_l.setVerticalAlignment(Element.ALIGN_CENTER);
            BI_PdftableSrNo_cell_l.setPadding(5);

            PdfPCell BI_PdftableQuestion_cell_l = new PdfPCell(new Paragraph("Have you at any time been found guilty of violation of rules / regulations / legislative requirements by customs / excise / income tax / foreign exchange / other revenue authorities, if so give particulars?", small_normal));
            //BI_PdftableQuestion_cell_l.setBackgroundColor(BaseColor.LIGHT_GRAY);
            //BI_PdftableQuestion_cell_l.setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableQuestion_cell_l.setPadding(5);

            PdfPCell BI_PdftableAns_cell_l;
            if (chk_tcc_que_l.isChecked())
                BI_PdftableAns_cell_l = new PdfPCell(new Paragraph("Yes", small_normal));
            else
                BI_PdftableAns_cell_l = new PdfPCell(new Paragraph("No", small_normal));
            //BI_PdftableAns_cell_l.setBackgroundColor(BaseColor.LIGHT_GRAY);
            BI_PdftableAns_cell_l.setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableAns_cell_l.setVerticalAlignment(Element.ALIGN_CENTER);
            BI_PdftableAns_cell_l.setPadding(5);

            BI_PdfQuestionDetails_l.addCell(BI_PdftableSrNo_cell_l);
            BI_PdfQuestionDetails_l.addCell(BI_PdftableQuestion_cell_l);
            BI_PdfQuestionDetails_l.addCell(BI_PdftableAns_cell_l);
            document.add(BI_PdfQuestionDetails_l);

            //to Question details m
            PdfPTable BI_PdfQuestionDetails_m = new PdfPTable(3);
            BI_PdfQuestionDetails_m.setWidthPercentage(100);
            BI_PdfQuestionDetails_m.setWidths(new int[]{1, 2, 1});

            PdfPCell BI_PdftableSrNo_cell_m = new PdfPCell(new Paragraph("m)", small_normal));
            //BI_PdftableSrNo_cell_m.setBackgroundColor(BaseColor.LIGHT_GRAY);
            BI_PdftableSrNo_cell_m.setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableSrNo_cell_m.setVerticalAlignment(Element.ALIGN_CENTER);
            BI_PdftableSrNo_cell_m.setPadding(5);

            PdfPCell BI_PdftableQuestion_cell_m = new PdfPCell(new Paragraph("Have you at any time come to the adverse notice of a regulator such as SEBI, IRDA, MCA, PFRDA. (Though it shall not be necessary for a candidate to mention in the column about orders and findings made by regulators which have been later on reversed / set aside in toto,  it  would  be  necessary  to  make  a  mention  of  the same, in case the reversal / setting aside is on technical reasons like limitation or lack of jurisdiction, etc, and not on merit. If the order of the regulator is temporarily stayed and the appellate / court proceedings are pending, the same also should be mentioned)?", small_normal));
            //BI_PdftableQuestion_cell_m.setBackgroundColor(BaseColor.LIGHT_GRAY);
            //BI_PdftableQuestion_cell_m.setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableQuestion_cell_m.setPadding(5);

            PdfPCell BI_PdftableAns_cell_m;
            if (chk_tcc_que_m.isChecked())
                BI_PdftableAns_cell_m = new PdfPCell(new Paragraph("Yes", small_normal));
            else
                BI_PdftableAns_cell_m = new PdfPCell(new Paragraph("No", small_normal));
            //BI_PdftableAns_cell_m.setBackgroundColor(BaseColor.LIGHT_GRAY);
            BI_PdftableAns_cell_m.setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableAns_cell_m.setVerticalAlignment(Element.ALIGN_CENTER);
            BI_PdftableAns_cell_m.setPadding(5);

            BI_PdfQuestionDetails_m.addCell(BI_PdftableSrNo_cell_m);
            BI_PdfQuestionDetails_m.addCell(BI_PdftableQuestion_cell_m);
            BI_PdfQuestionDetails_m.addCell(BI_PdftableAns_cell_m);
            document.add(BI_PdfQuestionDetails_m);

            //to Question details n
            PdfPTable BI_PdfQuestionDetails_n = new PdfPTable(3);
            BI_PdfQuestionDetails_n.setWidthPercentage(100);
            BI_PdfQuestionDetails_n.setWidths(new int[]{1, 2, 1});

            PdfPCell BI_PdftableSrNo_cell_n = new PdfPCell(new Paragraph("n)", small_normal));
            //BI_PdftableSrNo_cell_n.setBackgroundColor(BaseColor.LIGHT_GRAY);
            BI_PdftableSrNo_cell_n.setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableSrNo_cell_n.setVerticalAlignment(Element.ALIGN_CENTER);
            BI_PdftableSrNo_cell_n.setPadding(5);

            PdfPCell BI_PdftableQuestion_cell_n = new PdfPCell(new Paragraph("Has any of your group company/associate company/related party been carrying any license issued by the IRDA?", small_normal));
            //BI_PdftableQuestion_cell_n.setBackgroundColor(BaseColor.LIGHT_GRAY);
            //BI_PdftableQuestion_cell_n.setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableQuestion_cell_n.setPadding(5);

            PdfPCell BI_PdftableAns_cell_n;
            if (chk_tcc_que_n.isChecked())
                BI_PdftableAns_cell_n = new PdfPCell(new Paragraph("Yes", small_normal));
            else
                BI_PdftableAns_cell_n = new PdfPCell(new Paragraph("No", small_normal));
            //BI_PdftableAns_cell_n.setBackgroundColor(BaseColor.LIGHT_GRAY);
            BI_PdftableAns_cell_n.setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableAns_cell_n.setVerticalAlignment(Element.ALIGN_CENTER);
            BI_PdftableAns_cell_n.setPadding(5);

            BI_PdfQuestionDetails_n.addCell(BI_PdftableSrNo_cell_n);
            BI_PdfQuestionDetails_n.addCell(BI_PdftableQuestion_cell_n);
            BI_PdfQuestionDetails_n.addCell(BI_PdftableAns_cell_n);
            document.add(BI_PdfQuestionDetails_n);

            //sign at bottom
            byte[] fbyt_applicant = Base64.decode(str_customer_sign, 0);
            Bitmap applicantBitmap = BitmapFactory.decodeByteArray(fbyt_applicant, 0, fbyt_applicant.length);

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

            //to Question details o
            PdfPTable BI_PdfQuestionDetails_o = new PdfPTable(3);
            BI_PdfQuestionDetails_o.setWidthPercentage(100);
            BI_PdfQuestionDetails_o.setWidths(new int[]{1, 2, 1});

            PdfPCell BI_PdftableSrNo_cell_o = new PdfPCell(new Paragraph("o)", small_normal));
            //BI_PdftableSrNo_cell_o.setBackgroundColor(BaseColor.LIGHT_GRAY);
            BI_PdftableSrNo_cell_o.setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableSrNo_cell_o.setVerticalAlignment(Element.ALIGN_CENTER);
            BI_PdftableSrNo_cell_o.setPadding(5);

            PdfPCell BI_PdftableQuestion_cell_o = new PdfPCell(new Paragraph("Any other explanation / information in regard to items I and II  and other information considered relevant for judging fit and proper criteria of the applicant?", small_normal));
            //BI_PdftableQuestion_cell_o.setBackgroundColor(BaseColor.LIGHT_GRAY);
            //BI_PdftableQuestion_cell_o.setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableQuestion_cell_o.setPadding(5);

            PdfPCell BI_PdftableAns_cell_o;
            if (chk_tcc_que_o.isChecked())
                BI_PdftableAns_cell_o = new PdfPCell(new Paragraph("Yes", small_normal));
            else
                BI_PdftableAns_cell_o = new PdfPCell(new Paragraph("No", small_normal));
            //BI_PdftableAns_cell_o.setBackgroundColor(BaseColor.LIGHT_GRAY);
            BI_PdftableAns_cell_o.setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableAns_cell_o.setVerticalAlignment(Element.ALIGN_CENTER);
            BI_PdftableAns_cell_o.setPadding(5);

            BI_PdfQuestionDetails_o.addCell(BI_PdftableSrNo_cell_o);
            BI_PdfQuestionDetails_o.addCell(BI_PdftableQuestion_cell_o);
            BI_PdfQuestionDetails_o.addCell(BI_PdftableAns_cell_o);
            document.add(BI_PdfQuestionDetails_o);

            // new page
            //document.newPage();

            //to declaration msg
            Paragraph para_declaration_msg = new Paragraph("I confirm that the above information is, to the best of my knowledge and belief, true and complete. I undertake to keep the Authority fully informed, as soon as possible, of all events, which take place subsequent to my appointment, which are relevant to the information provided above.", small_normal);
            para_declaration_msg.setAlignment(Element.ALIGN_LEFT);
            document.add(para_declaration_msg);
            document.add(para_img_logo_after_space_1);


            //place , date and signature
            PdfPTable BI_PdftableApplicant = new PdfPTable(3);
            BI_PdftableApplicant.setWidths(new float[]{5f, 5f, 5f});
            BI_PdftableApplicant.setWidthPercentage(100);

            //row 1
            sign_cell = new PdfPCell(new Paragraph(
                    "Name : " + ""/*edt_tcc_query_name.getText().toString()*/, small_bold));
            sign_cell.setHorizontalAlignment(Element.ALIGN_BOTTOM);
            sign_cell.setPadding(5);
            sign_cell.setBorder(Rectangle.NO_BORDER);
            BI_PdftableApplicant.addCell(sign_cell);

            BI_PdftableApplicant.addCell(Nocell);

            BI_PdftableApplicant.addCell(Nocell);

            //row 2
            sign_cell = new PdfPCell(new Paragraph(
                    "Designation : " + "", small_bold));
            sign_cell.setHorizontalAlignment(Element.ALIGN_BOTTOM);
            sign_cell.setPadding(5);
            sign_cell.setBorder(Rectangle.NO_BORDER);
            BI_PdftableApplicant.addCell(sign_cell);

            BI_PdftableApplicant.addCell(Nocell);

            BI_PdftableApplicant.addCell(Nocell);

            //row 3
            sign_cell = new PdfPCell(new Paragraph(
                    "Place : " + edt_tcc_query_place.getText().toString(), small_bold));
            sign_cell.setHorizontalAlignment(Element.ALIGN_BOTTOM);
            sign_cell.setPadding(5);
            sign_cell.setBorder(Rectangle.NO_BORDER);
            BI_PdftableApplicant.addCell(sign_cell);

            BI_PdftableApplicant.addCell(Nocell);

            sign_cell = new PdfPCell(applicant_signature);
            sign_cell.setHorizontalAlignment(Element.ALIGN_TOP);
            sign_cell.setPadding(5);
            sign_cell.setBorder(Rectangle.NO_BORDER);
            BI_PdftableApplicant.addCell(sign_cell);

            //row 4
            sign_cell = new PdfPCell(new Paragraph(
                    "Date : " + txt_tcc_query_date.getText().toString(), small_bold));
            sign_cell.setHorizontalAlignment(Element.ALIGN_TOP);
            sign_cell.setPadding(5);
            sign_cell.setBorder(Rectangle.NO_BORDER);
            BI_PdftableApplicant.addCell(sign_cell);

            BI_PdftableApplicant.addCell(Nocell);

            BI_PdftableApplicant.addCell(Nocell);

            /*sign_cell = new PdfPCell(new Paragraph(
                    "Applicant Signature", small_bold));
            sign_cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            sign_cell.setBorder(Rectangle.TOP);
            sign_cell.setPadding(5);
            BI_PdftableApplicant.addCell(sign_cell);*/

            document.add(BI_PdftableApplicant);

            /*//place, date and sign
            PdfPTable BI_Pdftable_tcc_sign = new PdfPTable(6);
            BI_Pdftable_tcc_sign.setWidthPercentage(100);

            PdfPCell BI_Pdftable_tcc_place = new PdfPCell(new Paragraph("Place : ", small_normal));
            BI_Pdftable_tcc_place.setVerticalAlignment(Element.ALIGN_CENTER);
            PdfPCell BI_Pdftable_tcc_place_val = new PdfPCell(new Paragraph(edt_tcc_query_place.getText().toString(), small_normal));
            BI_Pdftable_tcc_place_val.setVerticalAlignment(Element.ALIGN_CENTER);
            PdfPCell BI_Pdftable_tcc_date = new PdfPCell(new Paragraph("Date : ", small_normal));
            BI_Pdftable_tcc_date.setVerticalAlignment(Element.ALIGN_CENTER);
            PdfPCell BI_Pdftable_tcc_date_val = new PdfPCell(new Paragraph(txt_tcc_query_date.getText().toString(), small_normal));
            BI_Pdftable_tcc_date_val.setVerticalAlignment(Element.ALIGN_CENTER);


            PdfPCell BI_Pdftable_tcc_sign1 = new PdfPCell(new Paragraph("Customer Singature : ", small_normal));
            BI_Pdftable_tcc_sign1.setVerticalAlignment(Element.ALIGN_CENTER);

            byte[] fbyt_Proposer = Base64.decode(str_customer_sign, 0);
            Bitmap customerbitmap = BitmapFactory.decodeByteArray(fbyt_Proposer, 0, fbyt_Proposer.length);
            // PdfPCell BI_PdftablePolicyHolder_signature_3 = new
            // PdfPCell();
            // BI_PdftablePolicyHolder_signature_3.setFixedHeight(60f);
            ByteArrayOutputStream PolicyHolder_signature_stream = new ByteArrayOutputStream();

            (customerbitmap).compress(Bitmap.CompressFormat.PNG, 50, PolicyHolder_signature_stream);
            Image PolicyHolder_signature = Image.getInstance(PolicyHolder_signature_stream.toByteArray());
            PolicyHolder_signature.scaleToFit(90, 90);
            PolicyHolder_signature.setBorder(Rectangle.BOX);

            PdfPCell BI_Pdftable_tcc_sign1_val = new PdfPCell(PolicyHolder_signature);
            BI_Pdftable_tcc_sign1_val.setVerticalAlignment(Element.ALIGN_CENTER);

            BI_Pdftable_tcc_sign.addCell(BI_Pdftable_tcc_place);
            BI_Pdftable_tcc_sign.addCell(BI_Pdftable_tcc_place_val);
            BI_Pdftable_tcc_sign.addCell(BI_Pdftable_tcc_date);
            BI_Pdftable_tcc_sign.addCell(BI_Pdftable_tcc_date_val);
            BI_Pdftable_tcc_sign.addCell(BI_Pdftable_tcc_sign1);
            BI_Pdftable_tcc_sign.addCell(BI_Pdftable_tcc_sign1_val);
            document.add(BI_Pdftable_tcc_sign);*/

            document.add(para_img_logo_after_space_1);

            document.close();

        } catch (Exception e) {

            isError = false;

            mCommonMethods.showToast(mContext, e.toString() + "Error in creating pdf");
        }

        return isError;
    }

    private void initialiseSchedule3Part() {

        ib_cif_doc_reupload_view.setVisibility(View.GONE);
        ib_cif_doc_reupload_capture.setVisibility(View.GONE);
        ib_cif_doc_reupload_browse.setVisibility(View.GONE);
        ib_cif_doc_reupload_submit.setVisibility(View.GONE);

        TextView txt_tcc_declaration = findViewById(R.id.txt_tcc_declaration);
        chk_tcc_declaration = findViewById(R.id.chk_tcc_declaration);
        btn_tcc_declaration_submit = findViewById(R.id.btn_tcc_declaration_submit);
        btn_tcc_declaration_submit.setOnClickListener(this);

        edt_tcc_declaration_place = findViewById(R.id.edt_tcc_declaration_place);

        TextView txt_tcc_declaration_date = findViewById(R.id.txt_tcc_declaration_date);
        Calendar cal = Calendar.getInstance();
        String mont = ((cal.get(Calendar.MONTH) + 1) < 10 ? "0" : "") + (cal.get(Calendar.MONTH) + 1);
        String day = (cal.get(Calendar.DAY_OF_MONTH) < 10 ? "0" : "") + cal.get(Calendar.DAY_OF_MONTH);

        txt_tcc_declaration_date.setText(day + "-" + mont + "-" + cal.get(Calendar.YEAR));

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

    private boolean createSchedule3PDF() {

        boolean isError = true;

        try {

            Font headerBold = new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.BOLD);
            Font small_bold = new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.BOLD);
            Font small_bold1 = new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.BOLD);
            Font small_bold2 = new Font(Font.FontFamily.TIMES_ROMAN, 4, Font.BOLD);
            Font small_normal = new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.NORMAL);

            fileSchedule3Doc = mStorageUtils.createFileToAppSpecificDirCIF(mContext, str_pf_number + SCHEDULE3_DOC + ".pdf");

            Rectangle rect = new Rectangle(594f, 792f);

            //Document document = new Document(rect, 50, 50, 50, 50);
            Document document = new Document(PageSize.A4, 50, 50, 50, 50);
            PdfWriter pdf_writer = PdfWriter.getInstance(document, new FileOutputStream(fileSchedule3Doc.getPath()));

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

        } catch (Exception ex) {

            isError = false;
            ex.printStackTrace();
        }

        return isError;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.ib_cif_doc_reupload_view:
                try {

                    switch (Check) {
                        case "":
                            mCommonMethods.showMessageDialog(mContext, "Please Select Document First and then Browse or Capture Document");
                            break;

                        case CHECK_TYPE_PHOTO:
                            if (f != null) {
                                mCommonMethods.openAllDocs(mContext, f);
                            } else {
                                mCommonMethods.showToast(mContext, "Please Browse or Capture Document First!");
                            }
                            break;

                        case CHECK_TYPE_SIGN:
                            if (fileSign != null) {
                                mCommonMethods.openAllDocs(mContext, fileSign);
                            } else {
                                mCommonMethods.showToast(mContext, "Please Browse or Capture Document First!");
                            }
                            break;

                        case CHECK_TYPE_MISMATCH_SIGN:
                            if (fileMismatchSign != null) {
                                mCommonMethods.openAllDocs(mContext, fileMismatchSign);
                            } else {
                                mCommonMethods.showToast(mContext, "Please Browse or Capture Document First!");
                            }
                            break;

                        case CHECK_TYPE_PAN_CARD_CAPTURE:
                            if (filePanCard != null) {
                                mCommonMethods.openAllDocs(mContext, filePanCard);
                            } else {
                                mCommonMethods.showToast(mContext, "Please Browse or Capture Document First!");
                            }
                            break;

                        case CHECK_TYPE_ID_CARD_CAPTURE:
                            if (fileIDCard != null) {
                                mCommonMethods.openAllDocs(mContext, fileIDCard);
                            } else {
                                mCommonMethods.showToast(mContext, "Please Browse or Capture Document First!");
                            }
                            break;

                        case FILE_HSC_DOC_NAME:
                            if (fileHSCDoc != null) {
                                mCommonMethods.openAllDocs(mContext, fileHSCDoc);
                            } else {
                                mCommonMethods.showToast(mContext, "Please Browse or Capture Document First!");
                            }
                            break;

                        case FILE_DEGREE_DOC_NAME:
                            if (fileDegreeDoc != null) {
                                mCommonMethods.openAllDocs(mContext, fileDegreeDoc);
                            } else {
                                mCommonMethods.showToast(mContext, "Please Browse or Capture Document First!");
                            }
                            break;

                        case SCHEDULE3_DOC:
                            if (fileSchedule3Doc != null) {
                                mCommonMethods.openAllDocs(mContext, fileSchedule3Doc);
                            } else {
                                mCommonMethods.showToast(mContext, "Please Browse or Capture Document First!");
                            }

                            break;

                        case CHECK_TYPE_ANNEXURE_CAPTURE:
                            if (fileAnnexure1Doc != null) {
                                mCommonMethods.openAllDocs(mContext, fileAnnexure1Doc);
                            } else {
                                mCommonMethods.showToast(mContext, "Please Browse or Capture Document First!");
                            }
                            break;

                        case CHECK_TYPE_SCORE_CARD:
                            if (fileScoreCard != null) {
                                mCommonMethods.openAllDocs(mContext, fileScoreCard);
                            } else {
                                mCommonMethods.showToast(mContext, "Please Browse or Capture Document First!");
                            }
                            break;

                        case CHECK_TYPE_TCC_CARD:
                            if (fileTCC != null) {
                                mCommonMethods.openAllDocs(mContext, fileTCC);
                            } else {
                                mCommonMethods.showToast(mContext, "Please Browse or Capture Document First!");
                            }
                            break;

                        default:
                            break;
                    }

                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                break;

            case R.id.ib_cif_doc_reupload_capture:
                if (Check.equals("")) {
                    mCommonMethods.showMessageDialog(mContext, "Please Select Document First");
                } else {
                    String captureResult = validatePfNumber();
                    if (captureResult.equals("")) {

                        if (Check.equals(CHECK_TYPE_ID_CARD_CAPTURE)) {
                            OCR_TYPE = "capture";
                            Intent intent = new Intent(mContext, OcrActivity.class);
                            startActivityForResult(intent, REQUEST_OCR);
                        } else {

                            if (Check.equals(CHECK_TYPE_MISMATCH_SIGN)) {
                                fileMismatchSign = mStorageUtils.createFileToAppSpecificDirCIF(mContext,
                                        str_pf_number + CHECK_TYPE_MISMATCH_SIGN + ".jpg");
                                captureDocImages(fileMismatchSign);
                            } else if (Check.equals(CHECK_TYPE_SIGN)) {
                                fileSign = mStorageUtils.createFileToAppSpecificDirCIF(mContext,
                                        str_pf_number + CHECK_TYPE_SIGN + ".jpg");
                                captureDocImages(fileSign);
                            } else if (Check.equals(CHECK_TYPE_PAN_CARD_CAPTURE)) {
                                filePanCard = mStorageUtils.createFileToAppSpecificDirCIF(mContext,
                                        str_pf_number + CHECK_TYPE_PAN_CARD_CAPTURE + ".jpg");
                                captureDocImages(filePanCard);
                            } else if (Check.equals(CHECK_TYPE_ID_CARD_CAPTURE)) {
                                fileIDCard = mStorageUtils.createFileToAppSpecificDirCIF(mContext,
                                        str_pf_number + CHECK_TYPE_ID_CARD_CAPTURE + ".jpg");
                                captureDocImages(fileIDCard);
                            } else if (Check.equals(FILE_HSC_DOC_NAME)) {
                                fileHSCDoc = mStorageUtils.createFileToAppSpecificDirCIF(mContext,
                                        str_pf_number + FILE_HSC_DOC_NAME + ".jpg");
                                captureDocImages(fileHSCDoc);
                            } else if (Check.equals(FILE_DEGREE_DOC_NAME)) {

                                fileDegreeDoc = mStorageUtils.createFileToAppSpecificDirCIF(mContext,
                                        str_pf_number + FILE_DEGREE_DOC_NAME + ".jpg");
                                captureDocImages(fileDegreeDoc);
                            } else if (Check.equals(SCHEDULE3_DOC)) {
                                fileSchedule3Doc = mStorageUtils.createFileToAppSpecificDirCIF(mContext,
                                        str_pf_number + SCHEDULE3_DOC + ".jpg");
                                captureDocImages(fileSchedule3Doc);
                            } else if (Check.equals(CHECK_TYPE_ANNEXURE_CAPTURE)) {
                                fileAnnexure1Doc = mStorageUtils.createFileToAppSpecificDirCIF(mContext,
                                        str_pf_number + CHECK_TYPE_ANNEXURE_CAPTURE + ".jpg");
                                captureDocImages(fileAnnexure1Doc);
                            } else if (Check.equals(CHECK_TYPE_SCORE_CARD)) {
                                fileScoreCard = mStorageUtils.createFileToAppSpecificDirCIF(mContext,
                                        str_pf_number + CHECK_TYPE_SCORE_CARD + ".jpg");
                                captureDocImages(fileScoreCard);
                            } else if (Check.equals(CHECK_TYPE_TCC_CARD)) {
                                fileTCC = mStorageUtils.createFileToAppSpecificDirCIF(mContext,
                                        str_pf_number + CHECK_TYPE_TCC_CARD + ".jpg");
                                captureDocImages(fileTCC);
                            } else {
                                f = mStorageUtils.createFileToAppSpecificDirCIF(mContext,
                                        str_pf_number + CHECK_TYPE_PHOTO + ".jpg");
                                captureDocImages(f);
                            }

                        }

                    } else {
                        mCommonMethods.showToast(mContext, captureResult);
                    }
                }
                break;

            case R.id.ib_cif_doc_reupload_browse:

                if (Check.equals("")) {
                    mCommonMethods.showMessageDialog(mContext, "Please Select Document First");
                } else {
                    String browseResult = validatePfNumber();

                    if (browseResult.equals("")) {

                        if (Check.equals(CHECK_TYPE_ID_CARD_CAPTURE)) {
                            OCR_TYPE = "browse";
                            Intent mIntentIDCardBrowse = new Intent(mContext, OcrActivity.class);
                            startActivityForResult(mIntentIDCardBrowse, REQUEST_OCR);
                        } else {
                            //Intent mediaIntent = new Intent(Intent.ACTION_GET_CONTENT);
                            //mediaIntent.setType("*/*"); // Set MIME type as per requirement
                            //startActivityForResult(mediaIntent, REQUEST_CODE_PICK_FILE);

                            Intent mIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                            mIntent.addCategory(Intent.CATEGORY_OPENABLE);
                            mIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION
                                    | Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
                            mIntent.setType("*/*");
                            //mIntent.setType("*/*");
                            /*String[] mimeType = new String[]{"application/x-binary,application/octet-stream"};
                                if(mimeType.length > 0) {
                                mIntent.putExtra(Intent.EXTRA_MIME_TYPES, mimeType);
                                }*/
                            mBrowseDocLauncher.launch(mIntent);
                        }
                    } else {
                        mCommonMethods.showToast(mContext, browseResult);
                    }
                }
                break;

            case R.id.ib_cif_doc_reupload_submit:

                if (Check.equals("")) {
                    mCommonMethods.showMessageDialog(mContext, "Please Select Document First");
                } else {

                    //validate pf or urn first
                    String strValidation = validateDocumentUpload();

                    if (strValidation.equals("")) {
                        if (pf_urn_type.equals(TYPE_PF)) {
                            new AsyncValidatePF().execute();
                        } else {
                            new AsynchValidateURN().execute();
                        }
                    } else {
                        mCommonMethods.showMessageDialog(mContext, strValidation);
                    }
                }
                break;

            case R.id.btn_tcc_queries_submit:

                if (Check.equals("")) {
                    mCommonMethods.showMessageDialog(mContext, "Please Select Document First");
                } else {

                    String strError = validatePfNumber();
                    if (strError.equals("")) {
                        if (!edt_tcc_query_place.getText().toString().replaceAll("\\s+", "").trim().equals("")) {

                            if (mCommonMethods.isNetworkConnected(mContext)) {

                                //validate pf or urn first
                                Check = CHECK_TYPE_QUESTIONS_DOC;
                                if (pf_urn_type.equals(TYPE_PF)) {
                                    new AsyncValidatePF().execute();
                                } else {
                                    new AsynchValidateURN().execute();
                                }
                            } else {
                                mCommonMethods.showToast(mContext, mCommonMethods.NO_INTERNET_MESSAGE);
                            }
                        } else {
                            mCommonMethods.showMessageDialog(mContext, "Please enter place.");
                        }
                    } else {
                        mCommonMethods.showMessageDialog(mContext, strError);
                    }
                }
                break;

            case R.id.ib_cif_doc_reupload_download:
                if (Check.equals("")) {
                    mCommonMethods.showMessageDialog(mContext, "Please Select Document First");
                } else {

                    String strError = validatePfNumber();
                    if (strError.equals("")) {

                        if (mCommonMethods.isNetworkConnected(mContext)) {

                            isDownloadBtnClicked = true;

                            if (pf_urn_type.equals(TYPE_PF)) {
                                new AsyncValidatePF().execute();
                            } else {
                                new AsynchValidateURN().execute();
                            }
                        } else {
                            mCommonMethods.showToast(mContext, mCommonMethods.NO_INTERNET_MESSAGE);
                        }
                    } else {
                        mCommonMethods.showMessageDialog(mContext, strError);
                    }
                }

                break;

            case R.id.btn_tcc_declaration_submit:
                if (Check.equals("")) {
                    mCommonMethods.showMessageDialog(mContext, "Please Select Document First");
                } else {

                    String strError = validatePfNumber();
                    if (strError.equals("")) {

                        if (!edt_tcc_declaration_place.getText().toString().replaceAll("\\s+", "").trim().equals("")) {

                            if (chk_tcc_declaration.isChecked()) {

                                Check = SCHEDULE3_DOC;
                                if (pf_urn_type.equals(TYPE_PF)) {
                                    new AsyncValidatePF().execute();
                                } else {
                                    new AsynchValidateURN().execute();
                                }
                            } else {
                                mCommonMethods.showMessageDialog(mContext, "Please Agree terms and conditons.");
                            }
                        } else {
                            mCommonMethods.showMessageDialog(mContext, "Please enter place.");
                        }
                    } else {
                        mCommonMethods.showMessageDialog(mContext, strError);
                    }
                }

                break;

            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        String str_extension = "";

        if (requestCode == REQUEST_OCR) {
            if (resultCode == RESULT_OK) {
                File imagePath = null;
                String DocumentType = "", back_details = "";
                Bundle bundle = data.getExtras();

                if (bundle != null) {
                    String jsonData = (String) bundle.get("jsonData");

                    try {
                        JSONObject object = new JSONObject(jsonData);

                        DocumentType = object.get("DocumentType").toString();

                        imagePath = new File(bundle.get("BitmapImageUri").toString());
                        //Bitmap edgeBitmap = BitmapFactory.decodeFile(imagePath.getPath());

                        if (object.has("backDetails")) {
                            back_details = object.get("backDetails").toString();
                            back_details = back_details == null ? "" : back_details;
                        }

                    } catch (Exception e) {
                        e.printStackTrace();

                        imagePath = null;

                        /*imagePath = (File) bundle.get("BitmapImageUri");
                        Bitmap edgeBitmap = BitmapFactory.decodeFile(imagePath.getPath());*/
                    }
                }

                fileIDCard = null;
                try {

                    if (imagePath != null) {

                        if (imagePath.getName().contains("jpg") || imagePath.getName().contains("jpeg")
                                || imagePath.getName().contains("tif") || imagePath.getName().contains("png")) {
                            if (back_details.equals("")) {
                                mCommonMethods.showToast(mContext, "Kindly Capture second page of ID card");
                            } else {
                                //image compression by bhalla
                                CompressImage.compressImage(imagePath.getPath());

                                fileIDCard = mStorageUtils.saveFileToAppSpecificDir(mContext, StorageUtils.DIRECT_DIRECTORY_CIF,
                                        str_pf_number + CHECK_TYPE_ID_CARD_CAPTURE + ".jpg", imagePath);

                                str_extension = fileIDCard.getPath().substring(fileIDCard.getPath().lastIndexOf("."))
                                        == null ? "" : fileIDCard.getPath().substring(fileIDCard.getPath().lastIndexOf("."));

                                if (OCR_TYPE.equals("browse")) {
                                    ib_cif_doc_reupload_browse.setImageDrawable(getResources().getDrawable(R.drawable.checkedbrowse));
                                    ib_cif_doc_reupload_capture.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_camera));
                                } else if (OCR_TYPE.equals("capture")) {
                                    ib_cif_doc_reupload_browse.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_browsedoc));
                                    ib_cif_doc_reupload_capture.setImageDrawable(getResources().getDrawable(R.drawable.checkedcamera));
                                }

                                if (imagePath.exists()) {
                                    imagePath.delete();
                                }
                            }
                        } else {
                            mCommonMethods.showToast(mContext, "Please Capture / Browse image only.");
                        }

                    } else {
                        mCommonMethods.showToast(mContext, "Error while generating file..");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    mCommonMethods.showToast(mContext, "Blank file error");
                    if (fileIDCard.exists())
                        fileIDCard.delete();
                }
            } else {
                Toast.makeText(mContext, "Data not receive", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == REQUEST_CODE_PICK_PHOTO_FILE) {
            if (resultCode == RESULT_OK) {

                if (Check.equals(CHECK_TYPE_PHOTO)) {
                    if (f != null) {
                        //CompressImage.compressImage(f.getPath());
                        Object[] mObj = mCommonMethods.compressImageCIF_Photo_Sign(mContext,
                                f, 1);
                        //Bitmap bmp = (Bitmap) mObj[0];
                        f = (File) mObj[1];

                        double kilobyte = f.length() / 1024;
                        //PHoto and Sign validation for 50K size
                        if (kilobyte <= mCommonMethods.FILE_UPLOAD_CIF_PHOTO_SIGN_RESTRICT_SIZE) {
                            ib_cif_doc_reupload_capture.setImageDrawable(getResources().getDrawable(R.drawable.checkedcamera));
                            ib_cif_doc_reupload_browse.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_browsedoc));
                        } else {
                            f = null;
                            mCommonMethods.showMessageDialog(mContext, mCommonMethods.FILE_UPLOAD_CIF_PHOTO_SIGN_RESTRICT_SIZE_MSG);
                        }
                    } else {
                        mCommonMethods.showMessageDialog(mContext, "File Size is too small");
                    }
                } else if (Check.equals(CHECK_TYPE_SIGN)) {

                    //CompressImage.compressImage(fileSign.getAbsolutePath());
                    if (fileSign != null) {
                        Object[] mObj = mCommonMethods.compressImageCIF_Photo_Sign(mContext,
                                fileSign, 1);
                        //Bitmap bmp = (Bitmap) mObj[0];
                        fileSign = (File) mObj[1];

                        double kilobyte = fileSign.length() / 1024;
                        //PHoto and Sign validation for 50K size
                        if (kilobyte <= mCommonMethods.FILE_UPLOAD_CIF_PHOTO_SIGN_RESTRICT_SIZE) {
                            ib_cif_doc_reupload_capture.setImageDrawable(getResources().getDrawable(R.drawable.checkedcamera));
                            ib_cif_doc_reupload_browse.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_browsedoc));
                        } else {
                            fileSign = null;
                            mCommonMethods.showMessageDialog(mContext, mCommonMethods.FILE_UPLOAD_CIF_PHOTO_SIGN_RESTRICT_SIZE_MSG);
                        }
                    } else {
                        mCommonMethods.showMessageDialog(mContext, "File Size is too small");
                    }
                } else if (Check.equals(CHECK_TYPE_MISMATCH_SIGN)) {
                    CompressImage.compressImage(fileMismatchSign.getAbsolutePath());
                    if (fileMismatchSign != null) {
                        long size = fileMismatchSign.length();
                        double kilobyte = size / 1024;

                        //2 MB valiadation
                        if (kilobyte < mCommonMethods.FILE_UPLOAD_RESTRICT_SIZE) {
                            //str_extension = fileMismatchSign.getAbsolutePath().substring(fileMismatchSign.getAbsolutePath().lastIndexOf("."));

                            ib_cif_doc_reupload_capture.setImageDrawable(getResources().getDrawable(R.drawable.checkedcamera));
                            ib_cif_doc_reupload_browse.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_browsedoc));

                        } else {
                            fileMismatchSign = null;
                            mCommonMethods.showMessageDialog(mContext, mCommonMethods.FILE_UPLOAD_RESTRICT_SIZE_MSG);
                        }
                    } else {
                        mCommonMethods.showMessageDialog(mContext, "File Size is too small");
                    }
                } else if (Check.equals(CHECK_TYPE_PAN_CARD_CAPTURE)) {
                    CompressImage.compressImage(filePanCard.getAbsolutePath());
                    if (filePanCard != null) {
                        long size = filePanCard.length();
                        double kilobyte = size / 1024;

                        //2 MB valiadation
                        if (kilobyte < mCommonMethods.FILE_UPLOAD_RESTRICT_SIZE) {
                            //str_extension = fileMismatchSign.getAbsolutePath().substring(fileMismatchSign.getAbsolutePath().lastIndexOf("."));

                            ib_cif_doc_reupload_capture.setImageDrawable(getResources().getDrawable(R.drawable.checkedcamera));
                            ib_cif_doc_reupload_browse.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_browsedoc));

                        } else {
                            filePanCard = null;
                            mCommonMethods.showMessageDialog(mContext, mCommonMethods.FILE_UPLOAD_RESTRICT_SIZE_MSG);
                        }
                    } else {
                        mCommonMethods.showMessageDialog(mContext, "File Size is too small");
                    }
                } else if (Check.equals(CHECK_TYPE_ID_CARD_CAPTURE)) {
                    CompressImage.compressImage(fileIDCard.getAbsolutePath());
                    if (fileIDCard != null) {
                        long size = fileIDCard.length();
                        double kilobyte = size / 1024;

                        //2 MB valiadation
                        if (kilobyte < mCommonMethods.FILE_UPLOAD_RESTRICT_SIZE) {
                            //str_extension = fileMismatchSign.getAbsolutePath().substring(fileMismatchSign.getAbsolutePath().lastIndexOf("."));

                            ib_cif_doc_reupload_capture.setImageDrawable(getResources().getDrawable(R.drawable.checkedcamera));
                            ib_cif_doc_reupload_browse.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_browsedoc));

                        } else {
                            fileIDCard = null;
                            mCommonMethods.showMessageDialog(mContext, mCommonMethods.FILE_UPLOAD_RESTRICT_SIZE_MSG);
                        }
                    } else {
                        mCommonMethods.showMessageDialog(mContext, "File Size is too small");
                    }
                } else if (Check.equals(FILE_HSC_DOC_NAME)) {
                    CompressImage.compressImage(fileHSCDoc.getAbsolutePath());
                    if (fileHSCDoc != null) {
                        long size = fileHSCDoc.length();
                        double kilobyte = size / 1024;

                        //2 MB valiadation
                        if (kilobyte < mCommonMethods.FILE_UPLOAD_RESTRICT_SIZE) {
                            //str_extension = fileMismatchSign.getAbsolutePath().substring(fileMismatchSign.getAbsolutePath().lastIndexOf("."));

                            ib_cif_doc_reupload_capture.setImageDrawable(getResources().getDrawable(R.drawable.checkedcamera));
                            ib_cif_doc_reupload_browse.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_browsedoc));

                        } else {
                            fileHSCDoc = null;
                            mCommonMethods.showMessageDialog(mContext, mCommonMethods.FILE_UPLOAD_RESTRICT_SIZE_MSG);
                        }
                    } else {
                        mCommonMethods.showMessageDialog(mContext, "File Size is too small");
                    }
                } else if (Check.equals(FILE_DEGREE_DOC_NAME)) {
                    CompressImage.compressImage(fileDegreeDoc.getAbsolutePath());
                    if (fileDegreeDoc != null) {
                        long size = fileDegreeDoc.length();
                        double kilobyte = size / 1024;

                        //2 MB valiadation
                        if (kilobyte < mCommonMethods.FILE_UPLOAD_RESTRICT_SIZE) {
                            //str_extension = fileMismatchSign.getAbsolutePath().substring(fileMismatchSign.getAbsolutePath().lastIndexOf("."));

                            ib_cif_doc_reupload_capture.setImageDrawable(getResources().getDrawable(R.drawable.checkedcamera));
                            ib_cif_doc_reupload_browse.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_browsedoc));

                        } else {
                            fileDegreeDoc = null;
                            mCommonMethods.showMessageDialog(mContext, mCommonMethods.FILE_UPLOAD_RESTRICT_SIZE_MSG);
                        }
                    } else {
                        mCommonMethods.showMessageDialog(mContext, "File Size is too small");
                    }
                } else if (Check.equals(SCHEDULE3_DOC)) {
                    CompressImage.compressImage(fileDegreeDoc.getAbsolutePath());
                    if (fileSchedule3Doc != null) {
                        long size = fileSchedule3Doc.length();
                        double kilobyte = size / 1024;

                        //2 MB valiadation
                        if (kilobyte < mCommonMethods.FILE_UPLOAD_RESTRICT_SIZE) {
                            //str_extension = fileMismatchSign.getAbsolutePath().substring(fileMismatchSign.getAbsolutePath().lastIndexOf("."));

                            ib_cif_doc_reupload_capture.setImageDrawable(getResources().getDrawable(R.drawable.checkedcamera));
                            ib_cif_doc_reupload_browse.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_browsedoc));

                        } else {
                            fileSchedule3Doc = null;
                            mCommonMethods.showMessageDialog(mContext, mCommonMethods.FILE_UPLOAD_RESTRICT_SIZE_MSG);
                        }
                    } else {
                        mCommonMethods.showMessageDialog(mContext, "File Size is too small");
                    }
                } else if (Check.equals(CHECK_TYPE_ANNEXURE_CAPTURE)) {
                    CompressImage.compressImage(fileAnnexure1Doc.getAbsolutePath());
                    if (fileAnnexure1Doc != null) {
                        long size = fileAnnexure1Doc.length();
                        double kilobyte = size / 1024;

                        //2 MB valiadation
                        if (kilobyte < mCommonMethods.FILE_UPLOAD_RESTRICT_SIZE) {
                            //str_extension = fileMismatchSign.getAbsolutePath().substring(fileMismatchSign.getAbsolutePath().lastIndexOf("."));

                            ib_cif_doc_reupload_capture.setImageDrawable(getResources().getDrawable(R.drawable.checkedcamera));
                            ib_cif_doc_reupload_browse.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_browsedoc));

                        } else {
                            fileAnnexure1Doc = null;
                            mCommonMethods.showMessageDialog(mContext, mCommonMethods.FILE_UPLOAD_RESTRICT_SIZE_MSG);
                        }
                    } else {
                        mCommonMethods.showMessageDialog(mContext, "File Size is too small");
                    }
                } else if (Check.equals(CHECK_TYPE_SCORE_CARD)) {
                    CompressImage.compressImage(fileScoreCard.getAbsolutePath());
                    if (fileScoreCard != null) {
                        long size = fileScoreCard.length();
                        double kilobyte = size / 1024;

                        //2 MB valiadation
                        if (kilobyte < mCommonMethods.FILE_UPLOAD_RESTRICT_SIZE) {
                            //str_extension = fileMismatchSign.getAbsolutePath().substring(fileMismatchSign.getAbsolutePath().lastIndexOf("."));

                            ib_cif_doc_reupload_capture.setImageDrawable(getResources().getDrawable(R.drawable.checkedcamera));
                            ib_cif_doc_reupload_browse.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_browsedoc));

                        } else {
                            fileScoreCard = null;
                            mCommonMethods.showMessageDialog(mContext, mCommonMethods.FILE_UPLOAD_RESTRICT_SIZE_MSG);
                        }
                    } else {
                        mCommonMethods.showMessageDialog(mContext, "File Size is too small");
                    }
                } else if (Check.equals(CHECK_TYPE_TCC_CARD)) {
                    CompressImage.compressImage(fileTCC.getAbsolutePath());
                    if (fileTCC != null) {
                        long size = fileTCC.length();
                        double kilobyte = size / 1024;

                        //2 MB valiadation
                        if (kilobyte < mCommonMethods.FILE_UPLOAD_RESTRICT_SIZE) {
                            //str_extension = fileMismatchSign.getAbsolutePath().substring(fileMismatchSign.getAbsolutePath().lastIndexOf("."));

                            ib_cif_doc_reupload_capture.setImageDrawable(getResources().getDrawable(R.drawable.checkedcamera));
                            ib_cif_doc_reupload_browse.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_browsedoc));

                        } else {
                            fileTCC = null;
                            mCommonMethods.showMessageDialog(mContext, mCommonMethods.FILE_UPLOAD_RESTRICT_SIZE_MSG);
                        }
                    } else {
                        mCommonMethods.showMessageDialog(mContext, "File Size is too small");
                    }
                }
            }
        }
    }

    private void captureDocImages(File mFile) {

        try {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            // Continue only if the File was successfully created
            if (mFile != null) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mCommonMethods.getContentUri(mContext,
                            mFile));
                } else {
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mFile));
                }
                startActivityForResult(takePictureIntent, REQUEST_CODE_PICK_PHOTO_FILE);
            }
        } catch (Exception exp) {
            exp.printStackTrace();
            mCommonMethods.printLog("Capture : ", exp.getMessage());
        }
    }

    private String validateDocumentUpload() {
        String strError = "";

        str_pf_number = edt_cif_doc_upload_pf_number.getText().toString();

        if (str_pf_number.equals("")) {
            strError = "Please Enter PF Number";
        } else if (!mCommonMethods.validateSpecailChar(str_pf_number)) {
            strError = "Please Enter validate PF Number";
        } else if (spnr_cif_doc_reupload.getSelectedItemPosition() == 0) {
            strError = "Please Select Document First";
        } else if ((Check == CHECK_TYPE_PHOTO && f == null)
                || (Check == CHECK_TYPE_SIGN && fileSign == null)
                || (Check == CHECK_TYPE_MISMATCH_SIGN && fileMismatchSign == null)
                || (Check == CHECK_TYPE_PAN_CARD_CAPTURE && filePanCard == null)
                || (Check == CHECK_TYPE_ID_CARD_CAPTURE && fileIDCard == null)
                || (Check == FILE_HSC_DOC_NAME && fileHSCDoc == null)
                || (Check == FILE_DEGREE_DOC_NAME && fileDegreeDoc == null)
                || (Check == SCHEDULE3_DOC && fileSchedule3Doc == null)
                || (Check == CHECK_TYPE_ANNEXURE_CAPTURE && fileAnnexure1Doc == null)
                || (Check == CHECK_TYPE_SCORE_CARD && fileScoreCard == null)
                || (Check == CHECK_TYPE_TCC_CARD && fileTCC == null)) {
            strError = "Please Capture Or Browse Document";
        } else {
            strError = "";
        }

        return strError;
    }

    private String validatePfNumber() {
        String strError = "";

        str_pf_number = edt_cif_doc_upload_pf_number.getText().toString();

        if (str_pf_number.equals("")) {
            strError = "Please Enter PF / URN Number";
        } else if (!mCommonMethods.validateSpecailChar(str_pf_number)) {
            strError = "Please Enter validate PF / URN Number";
        }
        return strError;
    }

    public void showSuccessDialog(String message, final String type) {
        try {
            final Dialog dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_with_ok_button);
            TextView text = dialog.findViewById(R.id.tv_title);
            text.setText(message);
            Button dialogButton = dialog.findViewById(R.id.bt_ok);
            dialogButton.setText("Ok");
            dialogButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    ib_cif_doc_reupload_submit.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_uploaddoc));
                    ib_cif_doc_reupload_browse.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_browsedoc));
                    ib_cif_doc_reupload_capture.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_camera));
                    spnr_cif_doc_reupload.setSelection(0);
                    dialog.dismiss();
                }
            });
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
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
                            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME_UPLOAD_FILE_CIF_REQUIREMENT);
                            request.addProperty("f", result);
                            request.addProperty("fileName", mFile.getName());
                            request.addProperty("PFURNNo", str_pf_number);

                            if (pf_urn_type.equals(TYPE_URN)) {
                                request.addProperty("strType", "");
                            } else {
                                if (Check.equals(CHECK_TYPE_SIGN)) {
                                    request.addProperty("strType", "S");
                                } else if (Check.equals(CHECK_TYPE_PHOTO)) {
                                    request.addProperty("strType", "P");
                                } else if (Check.equals(CHECK_TYPE_MISMATCH_SIGN)) {
                                    request.addProperty("strType", "I");
                                } else if (Check.equals(CHECK_TYPE_PAN_CARD_CAPTURE)) {
                                    request.addProperty("strType", "I");
                                } else if (Check.equals(CHECK_TYPE_ID_CARD_CAPTURE)) {
                                    request.addProperty("strType", "I");
                                } else if (Check.equals(FILE_HSC_DOC_NAME)) {
                                    request.addProperty("strType", "I");
                                } else if (Check.equals(FILE_DEGREE_DOC_NAME)) {
                                    request.addProperty("strType", "I");
                                } else if (Check.equals(SCHEDULE3_DOC)) {
                                    request.addProperty("strType", "I");
                                } else if (Check.equals(CHECK_TYPE_ANNEXURE_CAPTURE)) {
                                    request.addProperty("strType", "I");
                                } else if (Check.equals(CHECK_TYPE_QUESTIONS_DOC)) {
                                    request.addProperty("strType", "I");
                                }
                            }

                            //request.addProperty("strType", pf_urn_type);

                            //for UAT
                            request.addProperty("Authkey", mCommonMethods.getStr_cif_auth_key());

                            new AsyncUploadFile_CIF(mContext, CIFDocUploadActivity.this,
                                    request, METHOD_NAME_UPLOAD_FILE_CIF_REQUIREMENT).execute();

                        } else {
                            mCommonMethods.showToast(mContext, "File Not Found");
                        }
                    }
                }, throwable -> {
                    mCommonMethods.showToast(mContext, "File Not Found");
                });


        /*if (pf_urn_type.equals(TYPE_PF)) {
            request = new SoapObject(NAMESPACE, METHOD_NAME_UPLOAD_FILE_CIF);
            request.addProperty("PFNo", str_pf_number);

            if (Check.equals(CHECK_TYPE_SIGN)) {
                request.addProperty("fileName", fileSign.getName());
                request.addProperty("strType", "S");
            } else if (Check.equals(CHECK_TYPE_PHOTO)) {
                request.addProperty("fileName", f.getName());
                request.addProperty("strType", "P");
            } else if (Check.equals(CHECK_TYPE_MISMATCH_SIGN)) {
                request.addProperty("fileName", fileMismatchSign.getName());
                request.addProperty("strType", "I");
            } else if (Check.equals(CHECK_TYPE_PAN_CARD_CAPTURE)) {
                request.addProperty("fileName", filePanCard.getName());
                request.addProperty("strType", "I");
            } else if (Check.equals(CHECK_TYPE_ID_CARD_CAPTURE)) {
                request.addProperty("fileName", fileIDCard.getName());
                request.addProperty("strType", "I");
            } else if (Check.equals(FILE_HSC_DOC_NAME)) {
                request.addProperty("fileName", fileHSCDoc.getName());
                request.addProperty("strType", "I");
            } else if (Check.equals(FILE_DEGREE_DOC_NAME)) {
                request.addProperty("fileName", fileDegreeDoc.getName());
                request.addProperty("strType", "I");
            } else if (Check.equals(SCHEDULE3_DOC)) {
                request.addProperty("fileName", fileSchedule3Doc.getName());
                request.addProperty("strType", "I");
            } else if (Check.equals(CHECK_TYPE_ANNEXURE_CAPTURE)) {
                request.addProperty("fileName", fileAnnexure1Doc.getName());
                request.addProperty("strType", "I");
            } else if (Check.equals(CHECK_TYPE_QUESTIONS_DOC)) {
                request.addProperty("fileName", fileQuestions.getName());
                request.addProperty("strType", "I");
            }

        }
        else {
            request = new SoapObject(NAMESPACE, METHOD_NAME_UPLOAD_FILE_CIF_URN);
            request.addProperty("URN", str_pf_number);

            if (Check.equals(CHECK_TYPE_SCORE_CARD)) {
                request.addProperty("fileName", fileScoreCard.getName());
            } else if (Check.equals(CHECK_TYPE_TCC_CARD)) {
                request.addProperty("fileName", fileTCC.getName());
            } else if (Check.equals(CHECK_TYPE_QUESTIONS_DOC)) {
                request.addProperty("fileName", fileQuestions.getName());
            } else if (Check.equals(FILE_HSC_DOC_NAME)) {
                request.addProperty("fileName", fileHSCDoc.getName());
            } else if (Check.equals(FILE_DEGREE_DOC_NAME)) {
                request.addProperty("fileName", fileDegreeDoc.getName());
            } else if (Check.equals(SCHEDULE3_DOC)) {
                request.addProperty("fileName", fileSchedule3Doc.getName());
            }
        }
        if (pf_urn_type.equals(TYPE_PF)) {
            new AsyncUploadFile_CIF(mContext, this, request, METHOD_NAME_UPLOAD_FILE_CIF).execute();
        } else {
            new AsyncUploadFile_CIF(mContext, this, request, METHOD_NAME_UPLOAD_FILE_CIF_URN).execute();
        }*/
    }

    @Override
    public void onUploadComplete(Boolean result) {
        if (result) {

            ib_cif_doc_reupload_submit.setImageDrawable(getResources().getDrawable(R.drawable.checkedupload));

            if (Check.equals(CHECK_TYPE_PHOTO)) {
                f = null;
                showSuccessDialog("Applicant Photo Uploaded Successfully...", Check);
            } else if (Check.equals(CHECK_TYPE_SIGN)) {
                fileSign = null;
                showSuccessDialog("Applicant Sign Uploaded Successfully...", Check);
            } else if (Check.equals(CHECK_TYPE_MISMATCH_SIGN)) {
                fileMismatchSign = null;
                showSuccessDialog("Signature Mismatch Declaration Uploaded Successfully...", Check);
            } else if (Check.equals(CHECK_TYPE_PAN_CARD_CAPTURE)) {
                filePanCard = null;
                showSuccessDialog("PAN Card Uploaded Successfully...", Check);
            } else if (Check.equals(CHECK_TYPE_ID_CARD_CAPTURE)) {
                fileIDCard = null;
                showSuccessDialog("ID Card Uploaded Successfully...", Check);
            } else if (Check.equals(FILE_HSC_DOC_NAME)) {
                fileHSCDoc = null;
                showSuccessDialog("HSC Document Uploaded Successfully...", Check);
            } else if (Check.equals(FILE_DEGREE_DOC_NAME)) {
                fileDegreeDoc = null;
                showSuccessDialog("Degree Document Uploaded Successfully...", Check);
            } else if (Check.equals(SCHEDULE3_DOC)) {
                showSuccessDialog("Schedule 3 Document Uploaded Successfully...", Check);
            } else if (Check.equals(CHECK_TYPE_ANNEXURE_CAPTURE)) {
                fileAnnexure1Doc = null;
                showSuccessDialog("Annexure 1 Document Uploaded Successfully...", Check);
            } else if (Check.equals(CHECK_TYPE_SCORE_CARD)) {
                fileScoreCard = null;
                showSuccessDialog("Score Card Document Uploaded Successfully...", Check);
            } else if (Check.equals(CHECK_TYPE_TCC_CARD)) {
                fileTCC = null;
                showSuccessDialog("TCC Document Uploaded Successfully...", Check);
            } else if (Check.equals(CHECK_TYPE_QUESTIONS_DOC)) {
                showSuccessDialog("Quesions(Annexure 1) Document Uploaded Successfully...", Check);
            }

            Check = "";
        } else {
            Toast.makeText(mContext, "PLease try agian later..",
                    Toast.LENGTH_SHORT).show();
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
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.setTitle(Html.fromHtml("<font color='#00a1e3'><b>Please Wait...<b></font>"));
            if (mProgressDialog != null && !mProgressDialog.isShowing()) {
                mProgressDialog.show();
            }
        }


        @Override
        protected String doInBackground(String... aurl) {
            try {

                running = true;

                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME_UPLOAD_FILE_DOWNLOAD_SIGN);
                request.addProperty("strURN", str_pf_number);

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
                androidHttpTranport.call(NAMESPACE + METHOD_NAME_UPLOAD_FILE_DOWNLOAD_SIGN, envelope);

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

                    if (Check.equals(CHECK_TYPE_QUESTIONS_DOC)) {
                        Single.fromCallable(() -> create_tcc_questions_Pdf())
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Consumer<Boolean>() {
                                    @Override
                                    public void accept(@NonNull Boolean result) throws Exception {
                                        if (result) {
                                            fileQuestions = mStorageUtils.createFileToAppSpecificDirCIF(mContext,
                                                    CHECK_TYPE_QUESTIONS_DOC + str_pf_number + ".pdf");

                                            if (fileQuestions != null) {
                                                createSoapRequestToUploadDoc(fileQuestions);
                                            } else {
                                                mCommonMethods.showToast(mContext, "File not found");
                                            }
                                        } else {
                                            mCommonMethods.showToast(mContext, "Error while creating pdf file");
                                        }
                                    }
                                }, throwable -> {
                                    mCommonMethods.showToast(mContext, "File Not Found");
                                });
                    } else if (Check.equals(SCHEDULE3_DOC)) {
                        Single.fromCallable(() -> createSchedule3PDF())
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Consumer<Boolean>() {
                                    @Override
                                    public void accept(@NonNull Boolean result) throws Exception {
                                        if (result) {
                                            fileSchedule3Doc = mStorageUtils.createFileToAppSpecificDirCIF(mContext,
                                                    str_pf_number + SCHEDULE3_DOC + ".pdf");

                                            if (fileSchedule3Doc != null) {
                                                createSoapRequestToUploadDoc(fileSchedule3Doc);
                                            } else {
                                                mCommonMethods.showToast(mContext, "File not found");
                                            }
                                        } else {
                                            mCommonMethods.showToast(mContext, "Error while creating pdf file");
                                        }
                                    }
                                }, throwable -> {
                                    mCommonMethods.showToast(mContext, "File Not Found");
                                });
                    }
                } else {
                    mCommonMethods.showMessageDialog(mContext, "Signature is not uploaded, Please upload through Document reupload tab");
                }
            } else {
                mCommonMethods.showMessageDialog(mContext, "signature is not uploaded, Please upload through Document reupload tab");
            }
        }
    }

    class AsyncValidatePF extends AsyncTask<String, Void, String> {
        private volatile boolean running = true;

        @Override
        protected void onPreExecute() {
            mProgressDialog = new ProgressDialog(mContext, ProgressDialog.THEME_HOLO_LIGHT);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.setTitle(Html.fromHtml("<font color='#00a1e3'><b>Please Wait...<b></font>"));
            if (mProgressDialog != null && !mProgressDialog.isShowing()) {
                mProgressDialog.show();
            }
        }

        @Override
        protected String doInBackground(String... param) {
            try {
                running = true;

                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME_UPLOAD_FILE_VALIDATE_PF);

                request.addProperty("strPFNo", str_pf_number.trim());

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);
                envelope.dotNet = true;

                envelope.setOutputSoapObject(request);

                //allowAllSSL();
                mCommonMethods.TLSv12Enable();

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);

                HttpTransportSE androidHttpTranport = new HttpTransportSE(ServiceURL.SERVICE_URL);

                androidHttpTranport.call(NAMESPACE + METHOD_NAME_UPLOAD_FILE_VALIDATE_PF, envelope);
                //Object response = envelope.getResponse();

                SoapPrimitive sa = (SoapPrimitive) envelope.getResponse();

                return sa.toString();

                        /*if (inputciflist.equalsIgnoreCase("0")) {

                        } else if (inputciflist.equalsIgnoreCase("1")) {

                        } else {

                            ParseXML prsObj = new ParseXML();

                            inputciflist = prsObj.parseXmlTag(inputciflist, "NewDataSet");
                        }*/

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
        protected void onPostExecute(String result) {
            if (mProgressDialog != null && mProgressDialog.isShowing())
                mProgressDialog.dismiss();
            if (running) {

                if (result.equalsIgnoreCase("0")) {
                    mCommonMethods.showMessageDialog(mContext, "PF No. does not exist");
                } else {

                    if (isDownloadBtnClicked) {
                        isDownloadBtnClicked = false;

                        new AsyncDownloadDoc().execute();
                    } else {
                        switch (Check) {
                            case "":
                                mCommonMethods.showMessageDialog(mContext, "Please Select Document First and then Browse or Capture Document");
                                break;

                            case CHECK_TYPE_PHOTO:
                                if (f != null) {
                                    createSoapRequestToUploadDoc(f);
                                } else {
                                    mCommonMethods.showToast(mContext, "Please Browse or Capture Document First!");
                                }
                                break;

                            case CHECK_TYPE_SIGN:
                                if (fileSign != null) {
                                    createSoapRequestToUploadDoc(fileSign);
                                } else {
                                    mCommonMethods.showToast(mContext, "Please Browse or Capture Document First!");
                                }

                                break;

                            case CHECK_TYPE_MISMATCH_SIGN:
                                if (fileMismatchSign != null) {
                                    createSoapRequestToUploadDoc(fileMismatchSign);
                                } else {
                                    mCommonMethods.showToast(mContext, "Please Browse or Capture Document First!");
                                }

                                break;

                            case CHECK_TYPE_PAN_CARD_CAPTURE:
                                if (filePanCard != null) {
                                    createSoapRequestToUploadDoc(filePanCard);
                                } else {
                                    mCommonMethods.showToast(mContext, "Please Browse or Capture Document First!");
                                }

                                break;

                            case CHECK_TYPE_ID_CARD_CAPTURE:
                                if (fileIDCard != null) {
                                    createSoapRequestToUploadDoc(fileIDCard);
                                } else {
                                    mCommonMethods.showToast(mContext, "Please Browse or Capture Document First!");
                                }

                                break;

                            case FILE_HSC_DOC_NAME:
                                if (fileHSCDoc != null) {
                                    createSoapRequestToUploadDoc(fileHSCDoc);
                                } else {
                                    mCommonMethods.showToast(mContext, "Please Browse or Capture Document First!");
                                }
                                break;

                            case FILE_DEGREE_DOC_NAME:
                                if (fileDegreeDoc != null) {
                                    createSoapRequestToUploadDoc(fileDegreeDoc);
                                } else {
                                    mCommonMethods.showToast(mContext, "Please Browse or Capture Document First!");
                                }
                                break;

                            case SCHEDULE3_DOC:
                                //download sign first
                                new DownloadSignature().execute();

                                break;

                            case CHECK_TYPE_ANNEXURE_CAPTURE:
                                if (fileAnnexure1Doc != null) {
                                    createSoapRequestToUploadDoc(fileAnnexure1Doc);
                                } else {
                                    mCommonMethods.showToast(mContext, "Please Browse or Capture Document First!");
                                }
                                break;

                            case CHECK_TYPE_SCORE_CARD:
                                if (fileScoreCard != null) {
                                    createSoapRequestToUploadDoc(fileScoreCard);
                                } else {
                                    mCommonMethods.showToast(mContext, "Please Browse or Capture Document First!");
                                }
                                break;

                            case CHECK_TYPE_TCC_CARD:
                                if (fileTCC != null) {
                                    createSoapRequestToUploadDoc(fileTCC);
                                } else {
                                    mCommonMethods.showToast(mContext, "Please Browse or Capture Document First!");
                                }
                                break;

                            case CHECK_TYPE_QUESTIONS_DOC:
                                //download sign first
                                new DownloadSignature().execute();
                                break;

                            default:
                                break;
                        }
                    }
                }
            } else {
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Server not responding", Toast.LENGTH_SHORT);
                toast.show();
            }
        }

    }

    class AsyncDownloadDoc extends AsyncTask<String, String, File> {

        private volatile boolean running = true;

        @Override
        protected void onPreExecute() {
            mProgressDialog = new ProgressDialog(mContext, ProgressDialog.THEME_HOLO_LIGHT);
            String Message = "Loading Please wait...";
            mProgressDialog.setMessage(Html.fromHtml("<font color='#00a1e3'><b>" + Message + "<b></font>"));
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.show();
        }

        @Override
        protected File doInBackground(String... strings) {
            try {

                running = true;

                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME_DOWNLOAD_FILES);
                //file name without extention
                String strFileName = "", strFileType = "";

                if (pf_urn_type.equals(TYPE_PF)) {

                    if (Check.equals(CHECK_TYPE_PHOTO)) {
                        strFileType = "P";
                    } else if (Check.equals(CHECK_TYPE_SIGN)) {
                        strFileType = "S";
                    } else {
                        strFileType = "I";
                    }
                }

                if (Check.equals(CHECK_TYPE_QUESTIONS_DOC)) {
                    strFileName = CHECK_TYPE_QUESTIONS_DOC + str_pf_number;
                } else {
                    strFileName = str_pf_number + Check;
                }

                request.addProperty("fileName", strFileName);
                request.addProperty("PFURNNo", str_pf_number);
                request.addProperty("strType", strFileType);

                //for UAT
                //request.addProperty("Authkey", mCommonMethods.getStr_cif_auth_key());

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;

                envelope.setOutputSoapObject(request);

                mCommonMethods.TLSv12Enable();

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);

                HttpTransportSE androidHttpTranport = new HttpTransportSE(ServiceURL.SERVICE_URL);

                androidHttpTranport.call(NAMESPACE + METHOD_NAME_DOWNLOAD_FILES, envelope);
                //Object response = envelope.getResponse();

                SoapObject soapObject;
                SoapPrimitive soapPrimitive;
                String result = "";

                if (envelope.getResponse() instanceof SoapObject) {
                    soapObject = (SoapObject) envelope.getResponse();
                    result = soapObject.toString();
                } else {
                    soapPrimitive = (SoapPrimitive) envelope.getResponse();
                    result = soapPrimitive.toString();
                }
                result = result == null ? "0" : result;

                if (!result.equals("0")) {
                    ParseXML mParseXML = new ParseXML();

                    String strDocName = mParseXML.parseXmlTag(result, "filename");
                    String strDoc = mParseXML.parseXmlTag(result, "doc");
                    byte[] fileBytes = Base64.decode(strDoc.getBytes(), Base64.DEFAULT);

                    File mFile = mStorageUtils.createFileToAppSpecificDirCIF(mContext, strDocName);

                    BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(mFile));
                    bos.write(fileBytes);
                    bos.flush();
                    bos.close();

                    return mFile;
                } else {
                    return null;
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
        protected void onPostExecute(File result) {
            if (mProgressDialog.isShowing())
                mProgressDialog.dismiss();

            if (running) {
                running = false;
                if (result != null) {
                    try {
                        mCommonMethods.openAllDocs(mContext, result);
                    } catch (IOException e) {
                        e.printStackTrace();
                        mCommonMethods.showMessageDialog(mContext, e.getMessage());
                    }
                } else {
                    mCommonMethods.showMessageDialog(mContext, "file not available");
                }
            } else {
                mCommonMethods.showMessageDialog(mContext, mCommonMethods.WEEK_INTERNET_MESSAGE);
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

                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME_UPLOAD_FILE_VALIDATE_URN);
                request.addProperty("strURN", str_pf_number);

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

                    androidHttpTranport.call(NAMESPACE + METHOD_NAME_UPLOAD_FILE_VALIDATE_URN, envelope);
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
        protected void onPostExecute(String result) {

            if (mProgressDialog.isShowing())
                mProgressDialog.dismiss();

            if (running) {
                if (!result.equals("0")) {

                    if (isDownloadBtnClicked) {
                        isDownloadBtnClicked = false;

                        new AsyncDownloadDoc().execute();
                    } else {
                        switch (Check) {
                            case "":
                                mCommonMethods.showMessageDialog(mContext, "Please Select Document First and then Browse or Capture Document");
                                break;

                            case CHECK_TYPE_PHOTO:
                                if (f != null) {
                                    createSoapRequestToUploadDoc(f);
                                } else {
                                    mCommonMethods.showToast(mContext, "Please Browse or Capture Document First!");
                                }
                                break;

                            case CHECK_TYPE_SIGN:
                                if (fileSign != null) {
                                    createSoapRequestToUploadDoc(fileSign);
                                } else {
                                    mCommonMethods.showToast(mContext, "Please Browse or Capture Document First!");
                                }

                                break;

                            case CHECK_TYPE_MISMATCH_SIGN:
                                if (fileMismatchSign != null) {
                                    createSoapRequestToUploadDoc(fileMismatchSign);
                                } else {
                                    mCommonMethods.showToast(mContext, "Please Browse or Capture Document First!");
                                }

                                break;

                            case CHECK_TYPE_PAN_CARD_CAPTURE:
                                if (filePanCard != null) {
                                    createSoapRequestToUploadDoc(filePanCard);
                                } else {
                                    mCommonMethods.showToast(mContext, "Please Browse or Capture Document First!");
                                }

                                break;

                            case CHECK_TYPE_ID_CARD_CAPTURE:
                                if (fileIDCard != null) {
                                    createSoapRequestToUploadDoc(fileIDCard);
                                } else {
                                    mCommonMethods.showToast(mContext, "Please Browse or Capture Document First!");
                                }

                                break;

                            case FILE_HSC_DOC_NAME:
                                if (fileHSCDoc != null) {
                                    createSoapRequestToUploadDoc(fileHSCDoc);
                                } else {
                                    mCommonMethods.showToast(mContext, "Please Browse or Capture Document First!");
                                }
                                break;

                            case FILE_DEGREE_DOC_NAME:
                                if (fileDegreeDoc != null) {
                                    createSoapRequestToUploadDoc(fileDegreeDoc);
                                } else {
                                    mCommonMethods.showToast(mContext, "Please Browse or Capture Document First!");
                                }
                                break;

                            case SCHEDULE3_DOC:
                                if (fileSchedule3Doc != null) {
                                    createSoapRequestToUploadDoc(fileSchedule3Doc);
                                } else {
                                    mCommonMethods.showToast(mContext, "Please Browse or Capture Document First!");
                                }

                                break;

                            case CHECK_TYPE_ANNEXURE_CAPTURE:
                                if (fileAnnexure1Doc != null) {
                                    createSoapRequestToUploadDoc(fileAnnexure1Doc);
                                } else {
                                    mCommonMethods.showToast(mContext, "Please Browse or Capture Document First!");
                                }
                                break;

                            case CHECK_TYPE_SCORE_CARD:
                                if (fileScoreCard != null) {
                                    createSoapRequestToUploadDoc(fileScoreCard);
                                } else {
                                    mCommonMethods.showToast(mContext, "Please Browse or Capture Document First!");
                                }
                                break;

                            case CHECK_TYPE_TCC_CARD:
                                if (fileTCC != null) {
                                    createSoapRequestToUploadDoc(fileTCC);
                                } else {
                                    mCommonMethods.showToast(mContext, "Please Browse or Capture Document First!");
                                }
                                break;

                            case CHECK_TYPE_QUESTIONS_DOC:

                                //download sign first
                                new DownloadSignature().execute();

                                break;

                            default:
                                break;
                        }
                    }
                } else {
                    mCommonMethods.showMessageDialog(mContext, "Invalid URN");
                }
            } else {
                mCommonMethods.showMessageDialog(mContext, mCommonMethods.WEEK_INTERNET_MESSAGE);
            }
        }
    }
}
