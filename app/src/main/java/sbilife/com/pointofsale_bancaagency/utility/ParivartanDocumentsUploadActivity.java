package sbilife.com.pointofsale_bancaagency.utility;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
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
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
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
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import sbilife.com.pointofsale_bancaagency.DatabaseHelper;
import sbilife.com.pointofsale_bancaagency.Element_TextView_BaseAdapter;
import sbilife.com.pointofsale_bancaagency.ParseXML;
import sbilife.com.pointofsale_bancaagency.ProposerCaptureSignature;
import sbilife.com.pointofsale_bancaagency.R;
import sbilife.com.pointofsale_bancaagency.ServiceURL;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;
import sbilife.com.pointofsale_bancaagency.common.CompressImage;
import sbilife.com.pointofsale_bancaagency.common.StorageUtils;
import sbilife.com.pointofsale_bancaagency.ekyc.offline_ekyc.OfflineKYCActivity;
import sbilife.com.pointofsale_bancaagency.ekyc.offline_ekyc.OfflinePaperlessKycResponse;
import sbilife.com.pointofsale_bancaagency.ekyc.offline_ekyc.UidData;
import sbilife.com.pointofsale_bancaagency.ekyc.utilites.XMLUtilities;
import sbilife.com.pointofsale_bancaagency.reports.ServiceHits;

public class ParivartanDocumentsUploadActivity extends AppCompatActivity implements
        OnClickListener, OnItemSelectedListener, ServiceHits.DownLoadData {
    public static Bitmap ReducedPhotoBitmap;
    private static File f;
    private static Bitmap ReducedProposerPhotoBitmap;
    private static Bitmap ReducedLifeAssuredPhotoBitmap;
    private final String NAMESPACE = "http://tempuri.org/";
    private final String SOAP_ACTION_UPLOAD_FILE_SERVICE_URN = "http://tempuri.org/UploadFile_URN";
    private final String METHOD_NAME_UPLOAD_FILE_SERVICE_URN = "UploadFile_URN";
    private final String METHOD_NAME_UPLOAD_QR_CODE_DETAILS = "saveAadhaar_QRCodedetail";
    private final String SOAP_ACTION_UPLOAD_QR_CODE_DETAILS = "http://tempuri.org/saveAadhaar_QRCodedetail";
    private final String JPEG_FILE_PREFIX = "IMG_";
    private final String JPEG_FILE_SUFFIX = ".jpg";
    private final int REQUEST_OCR = 1;
    private final int REQUEST_CODE_CAPTURE_FILE = 3;
    private final int REQUEST_CODE_BROWSE_FILE = 2;
    private final int REQUEST_QR_CODE = 49374;
    private final int DIALOG_ALERT = 10;
    private final int OFFLINE_EKYC_ACTIVITY = 11;
    private LinearLayout linearLayoutParivartanDocumentsUpload;
    private ImageButton Ibtn_signatureofPolicyHolders, Ibtn_signatureofThirdParty, Ibtn_signatureofLifeAssured,
            imageButtonParivartanDocumentsUploadLifeAssuredSignBrowse, imageButtonParivartanDocumentsUploadThirdPartySignBrowse,
            Ibtn_signatureofAppointee, imageButtonParivartanDocumentsUploadAppointeeSignBrowse,
            img_btn_document_upload_preview_image_otp_self_declaration, img_btn_document_delete_otp_self_declaration,
            img_btn_document_upload_click_image_otp_self_declaration, img_btn_document_upload_click_browse_otp_self_declaration,
            img_btn_document_upload_otp_self_declaration_upload, img_btn_document_upload_preview_image_age,
            img_btn_document_delete_age, img_btn_document_upload_click_image_age, img_btn_document_upload_click_browse_age,
            img_btn_document_upload_age_upload, img_btn_document_upload_preview_image_identity, img_btn_document_delete_identity,
            img_btn_document_upload_click_image_identity, img_btn_document_upload_click_browse_identity,
            img_btn_document_upload_identity_upload, img_btn_document_upload_click_preview_image_address, img_btn_document_delete_address,
            img_btn_document_upload_click_image_address, img_btn_document_upload_click_browse_address, img_btn_document_upload_address_upload,
            img_btn_document_upload_click_preview_image_income, img_btn_document_delete_income, img_btn_document_upload_click_image_income,
            img_btn_document_upload_click_browse_income, img_btn_document_upload_income_upload, img_btn_document_upload_click_preview_image_bank,
            img_btn_document_delete_bank, img_btn_document_upload_click_image_bank, img_btn_document_upload_click_browse_bank,
            img_btn_document_upload_bank_upload, img_btn_document_upload_preview_image_eft, img_btn_document_delete_eft, img_btn_document_upload_click_image_eft,
            img_btn_document_upload_click_browse_eft, img_btn_document_upload_eft_upload, img_btn_document_upload_click_preview_image_others,
            img_btn_document_delete_Others, img_btn_document_upload_click_image_others, img_btn_document_upload_click_browse_Others,
            img_btn_document_upload_Others_upload, img_btn_document_upload_preview_customer_photo, img_btn_document_delete_customer_photo,
            img_btn_document_upload_click_image_customer_photo, img_btn_document_upload_click_browse_customer_photo,
            img_btn_document_upload_customer_photo_upload, img_btn_document_upload_click_image_proposer_photo,
            img_btn_document_upload_click_browse_proposer_photo, img_btn_document_upload_proposer_photo_upload,
            img_btn_document_delete_proposer_photo, img_btn_document_upload_preview_proposer_photo,
            img_btn_document_upload_preview_life_assured_photo, img_btn_document_delete_life_assured_photo,
            img_btn_document_upload_click_image_life_assured_photo, img_btn_document_upload_click_browse_life_assured_photo,
            img_btn_document_upload_life_assured_photo_upload;
    private String Check = "";
    private CommonMethods commonMethods;
    private StorageUtils mStorageUtils;
    private Context context;
    private String latestImage = "", thirdPartySign = "", appointeeSign = "", lifeAssured_sign = "",
            strUploadDocument = "", strOCRDocumentNames = "", str_extension = "", strAadhaarQRScannedValue = "",
            strSelectedProof = "", str_QR_code_Name = "", str_QR_code_DOB = "", str_QR_code_Gender = "",
            str_qr_code_address = "", str1WFirstQR = "", str_QR_code_mailID = "", str_QR_code_mobile = "",
            str_ekyc_code_Name = "", str_ekyc_code_DOB = "", str_ekyc_code_Gender = "", str_ekyc_code_address = "",
            str_ekyc_code_mailID = "", str_ekyc_code_mobile = "", str1WFirstEKYC = "", str_QR_code_Photo = "",
            str_ekyc_code_Name_1W_spouse = "", str_ekyc_code_DOB_1W_spouse = "", str_ekyc_code_Gender_1W_spouse = "",
            str_ekyc_code_address_1W_spouse = "", str_QR_code_Photo_1W_spouse = "";
    private Bitmap photoBitmap, lifeAssuredBitmap;
    private EditText edt_document_upload_document_others;
    private ProgressDialog mProgressDialog;
    private Spinner spinnerUrnList, spnr_document_upload_document_age, spnr_document_upload_document_identity,
            spnr_document_upload_document_address, spnr_document_upload_document_income, spnr_document_upload_document_bank,
            spnr_document_upload_document_eft;
    private File proposerBrowsedPhotoFile, lifeAssuredBrowsedPhotoFile, lifeAssuredBrowsedSignFile, thirdPartyBrowsedSignFile,
            AppointeeBrowsedSignFile, otherProofFileName, photo, EFTProofFileName, bankProofFileName,
            incomeProofFileName, addressProofFileName, identityProofFileName, ageProofFileName, OTPSelfDeclarationFileName;
    private File imageF;
    private String str_selected_urn_no = "", mCurrentPhotoPath = "";
    private int uploadFlag;
    private int increment;
    private final OnClickListener CustomerPhotoOnClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub

            if (!str_selected_urn_no.equals("")) {
                ClearDocumentsPDF();
                uploadFlag = 1;
                img_btn_document_upload_click_browse_customer_photo
                        .setImageDrawable(getResources().getDrawable(
                                R.drawable.ibtn_browsedoc));
                Check = "CustomerPhoto";
                increment = 8;
                //dispatchTakePictureIntent("0");

                Intent intent = new Intent(ParivartanDocumentsUploadActivity.this, OcrActivity.class);
                startActivityForResult(intent, REQUEST_OCR);

            } else
                commonMethods.showToast(context, "please select urn");

        }
    };
    private int Flag;
    private int deleteOTPSelfDeclarationDocument;
    private int deleteAgeDocument;
    private int deleteAddressDocument;
    private int deleteIdentityDocument;
    private int deleteIncomeDocument;
    private int deleteOtherDocument;
    private int deleteBankDocument;
    private int deleteeftDocument;
    private int deleteCustomerPhoto;
    private int deleteProposerPhoto;
    private int deleteLifeAssuredPhoto;
    private int flag_cancel_otp_self_declaration = 0;
    private int flag_cancel_age = 0;
    private int flag_cancel_address = 0;
    private int flag_cancel_income = 0;
    private int flag_cancel_identity = 0;
    private int flag_cancel_bank = 0;
    private int flag_cancel_eft = 0;
    private int flag_cancel_customer_photo = 0;
    private int isBrowseCapture = 0;
    private final OnClickListener OtpSelfDeclarationOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!str_selected_urn_no.equals("")) {
                ClearDocumentsPDF();
                uploadFlag = 1;
                img_btn_document_upload_click_browse_otp_self_declaration
                        .setImageDrawable(getResources().getDrawable(
                                R.drawable.ibtn_browsedoc));
                Check = "OTPSelfDeclaration";
                isBrowseCapture = REQUEST_CODE_CAPTURE_FILE;
                increment = 25;

                Intent intent = new Intent(ParivartanDocumentsUploadActivity.this, OcrActivity.class);
                startActivityForResult(intent, REQUEST_OCR);

                //dispatchTakePictureIntent("0");

            } else
                commonMethods.showToast(context, "please select urn");

        }
    };
    private final OnClickListener AgeProofOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!str_selected_urn_no.equals("")) {
                ClearDocumentsPDF();
                uploadFlag = 1;
                img_btn_document_upload_click_browse_age
                        .setImageDrawable(getResources().getDrawable(
                                R.drawable.ibtn_browsedoc));
                Check = "AgeProof";
                increment = 1;

//                if (spnr_document_upload_document_age.getSelectedItem().toString().toLowerCase().contains("aadhar card")
//                        || spnr_document_upload_document_age.getSelectedItem().toString().toLowerCase().contains("pancard")
//                        || spnr_document_upload_document_age.getSelectedItem().toString().toLowerCase().contains("passport")
//                        || spnr_document_upload_document_age.getSelectedItem().toString().toLowerCase().contains("voter s identity card")
//                        || spnr_document_upload_document_age.getSelectedItem().toString().toLowerCase().contains("driving license")) {

                isBrowseCapture = REQUEST_CODE_CAPTURE_FILE;

                Intent intent = new Intent(ParivartanDocumentsUploadActivity.this, OcrActivity.class);
                    startActivityForResult(intent, REQUEST_OCR);
//                } else {
//                    dispatchTakePictureIntent("0");
//                }
            } else
                commonMethods.showToast(context, "please select urn");

        }
    };
    private final OnClickListener IdentityProofOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {

            if (!str_selected_urn_no.equals("")) {
                ClearDocumentsPDF();

                uploadFlag = 1;
                img_btn_document_upload_click_browse_identity
                        .setImageDrawable(getResources().getDrawable(
                                R.drawable.ibtn_browsedoc));
                Check = "IdentityProof";
                increment = 2;

//                if (spnr_document_upload_document_identity.getSelectedItem().toString().toLowerCase().contains("aadhar card")
//                        || spnr_document_upload_document_identity.getSelectedItem().toString().toLowerCase().contains("pancard")
//                        || spnr_document_upload_document_identity.getSelectedItem().toString().toLowerCase().contains("passport")
//                        || spnr_document_upload_document_identity.getSelectedItem().toString().toLowerCase().contains("voter s identity card")
//                        || spnr_document_upload_document_identity.getSelectedItem().toString().toLowerCase().contains("driving license")) {

                isBrowseCapture = REQUEST_CODE_CAPTURE_FILE;

                Intent intent = new Intent(ParivartanDocumentsUploadActivity.this, OcrActivity.class);
                    startActivityForResult(intent, REQUEST_OCR);
//                } else {
//                    dispatchTakePictureIntent("0");
//                }
            } else
                commonMethods.showToast(context, "please select urn");

        }
    };
    private final OnClickListener AddressProofOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!str_selected_urn_no.equals("")) {
                ClearDocumentsPDF();
                uploadFlag = 1;
                img_btn_document_upload_click_browse_address
                        .setImageDrawable(getResources().getDrawable(
                                R.drawable.ibtn_browsedoc));
                Check = "AddressProof";
                increment = 3;

//                if (spnr_document_upload_document_address.getSelectedItem().toString().toLowerCase().contains("aadhar card")
//                        || spnr_document_upload_document_address.getSelectedItem().toString().toLowerCase().contains("pancard")
//                        || spnr_document_upload_document_address.getSelectedItem().toString().toLowerCase().contains("passport")
//                        || spnr_document_upload_document_address.getSelectedItem().toString().toLowerCase().contains("voter s identity card")
//                        || spnr_document_upload_document_address.getSelectedItem().toString().toLowerCase().contains("driving license")) {

                isBrowseCapture = REQUEST_CODE_CAPTURE_FILE;

                Intent intent = new Intent(ParivartanDocumentsUploadActivity.this, OcrActivity.class);
                    startActivityForResult(intent, REQUEST_OCR);
//                } else {
//                    dispatchTakePictureIntent("0");
//                }
            } else
                commonMethods.showToast(context, "please select urn");

        }
    };
    private final OnClickListener OthersProofOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {

            if (!str_selected_urn_no.equals("")) {
                //ClearDocumentsPDF();
                uploadFlag = 1;
                img_btn_document_upload_click_browse_Others
                        .setImageDrawable(getResources().getDrawable(
                                R.drawable.ibtn_browsedoc));

                Check = "OtherProof";
                isBrowseCapture = REQUEST_CODE_CAPTURE_FILE;
                increment = 5;
                //dispatchTakePictureIntent("0");

                Intent intent = new Intent(ParivartanDocumentsUploadActivity.this, OcrActivity.class);
                startActivityForResult(intent, REQUEST_OCR);
            } else
                commonMethods.showToast(context, "please select urn");
        }
    };
    private final OnClickListener IncomeProofOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {

            if (!str_selected_urn_no.equals("")) {
                ClearDocumentsPDF();
                uploadFlag = 1;
                img_btn_document_upload_click_browse_income
                        .setImageDrawable(getResources().getDrawable(
                                R.drawable.ibtn_browsedoc));
                Check = "IncomeProof";
                isBrowseCapture = REQUEST_CODE_CAPTURE_FILE;
                increment = 4;
//                dispatchTakePictureIntent("0");
                Intent intent = new Intent(ParivartanDocumentsUploadActivity.this, OcrActivity.class);
                startActivityForResult(intent, REQUEST_OCR);

            } else
                commonMethods.showToast(context, "please select urn");
        }
    };
    private final OnClickListener BankProofOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {

            if (!str_selected_urn_no.equals("")) {
                ClearDocumentsPDF();

                uploadFlag = 1;
                img_btn_document_upload_click_browse_bank
                        .setImageDrawable(getResources().getDrawable(
                                R.drawable.ibtn_browsedoc));
                Check = "BankProof";
                isBrowseCapture = REQUEST_CODE_CAPTURE_FILE;
                increment = 6;

                Intent intent = new Intent(ParivartanDocumentsUploadActivity.this, OcrActivity.class);
                startActivityForResult(intent, REQUEST_OCR);

            } else
                commonMethods.showToast(context, "please select urn");
        }
    };
    private final OnClickListener EFTProofOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {

            if (!str_selected_urn_no.equals("")) {
                ClearDocumentsPDF();
                uploadFlag = 1;
                img_btn_document_upload_click_browse_eft
                        .setImageDrawable(getResources().getDrawable(
                                R.drawable.ibtn_browsedoc));
                Check = "EFTProof";
                isBrowseCapture = REQUEST_CODE_CAPTURE_FILE;
                increment = 7;

                Intent intent = new Intent(ParivartanDocumentsUploadActivity.this, OcrActivity.class);
                startActivityForResult(intent, REQUEST_OCR);

                //dispatchTakePictureIntent("0");
            } else
                commonMethods.showToast(context, "please select urn");
        }
    };
    private final OnClickListener ProposerPhotoOnClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub

            if (!str_selected_urn_no.equals("")) {
                ClearDocumentsPDF();
                uploadFlag = 1;
                img_btn_document_upload_click_browse_proposer_photo
                        .setImageDrawable(getResources().getDrawable(
                                R.drawable.ibtn_browsedoc));
                Check = "ProposerPhoto";
                isBrowseCapture = REQUEST_CODE_CAPTURE_FILE;

                //dispatchTakePictureIntent("1");

                Intent intent = new Intent(ParivartanDocumentsUploadActivity.this, OcrActivity.class);
                startActivityForResult(intent, REQUEST_OCR);

            } else
                commonMethods.showToast(context, "please select urn");

        }
    };
    private final OnClickListener LifeAssuredPhotoOnClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub

            if (!str_selected_urn_no.equals("")) {
                ClearDocumentsPDF();
                uploadFlag = 1;
                img_btn_document_upload_click_browse_life_assured_photo
                        .setImageDrawable(getResources().getDrawable(
                                R.drawable.ibtn_browsedoc));
                Check = "LifeAssuredPhoto";
                isBrowseCapture = REQUEST_CODE_CAPTURE_FILE;

                //dispatchTakePictureIntent("2");


                Intent intent = new Intent(ParivartanDocumentsUploadActivity.this, OcrActivity.class);
                startActivityForResult(intent, REQUEST_OCR);

            } else
                commonMethods.showToast(context, "please select urn");

        }
    };
    //QR click flag =>  1 : Age, 2 : Identity, 3 : Address;
    private int flag_cancel_others = 0, qrCodeClick = 0;
    private Bitmap otpSelfDeclarationBitmap, ageProofBitmap,
            identityProofBitmap, addressProofBitmap, incomeProofBitmap,
            customerPhotoBitmap, BankProofBitmap, EFTProofBitmap,
            otherProofBitmap;
    private byte[] bytes = null;
    private boolean flag_income_proof_submitted = false;
    private boolean flag_bank_proof_submitted = false;
    private boolean flag_eft_proof_submitted = false;
    private boolean flag_custumer_pic_proof_submitted = false, is1WProduct = false, is1WProductEKYC = false;
    private UploadLifeAssuredSignService lifeAssuredSignService;
    private UploadThirdPartySignService uploadThirdPartySignService;
    private UploadAppointeeSignService uploadAppointeeSignService;
    private AsyncUploadDoc asyncUploadDoc;
    private AsyncGetBAOnlineURN_SMRT asyncGetBAOnlineURN_smrt;
    private LinearLayout llAgeAadhaarScan, llIdentityAadhaarScan, llAddressAadhaarScan;
    private ImageView img_btn_QR_age_scan, img_btn_QR_Identity_scan, img_btn_QR_Address_scan;
    private CommonMethods.UserDetailsValuesModel mUserDetailsValuesModel;
    private Button btn_offline_ekyc_age, btn_offline_ekyc_identity, btn_offline_ekyc_address;
    private LinearLayout llAgeQR, llIdentityQR, llAddressQR;
    private String strCIFBDMEmailId, strCIFBDMMObileNo;

    private static boolean isIntentAvailable(Context context, String action) {
        final PackageManager packageManager = context.getPackageManager();
        final Intent intent = new Intent(action);
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent,
                PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.parivartan_document_upload);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);

        commonMethods = new CommonMethods();
        mStorageUtils = new StorageUtils();
        commonMethods.setApplicationToolbarMenu(this, "Upload Parivartan Documents");

        context = this;

        mUserDetailsValuesModel = commonMethods.setUserDetails(context);

        spinnerUrnList = (Spinner) findViewById(R.id.spinnerUrnList);
        spinnerUrnList.setOnItemSelectedListener(this);
        linearLayoutParivartanDocumentsUpload = (LinearLayout) findViewById(R.id.linearLayoutParivartanDocumentsUpload);

        /*
         * imageButtonParivartanDocumentsUploadProposerPhoto = (ImageButton)
         * findViewById(R.id.imageButtonParivartanDocumentsUploadProposerPhoto);
         * imageButtonParivartanDocumentsUploadProposerPhotoCapture =
         * (ImageButton) findViewById(R.id.
         * imageButtonParivartanDocumentsUploadProposerPhotoCapture);
         * imageButtonParivartanDocumentsUploadProposerPhotoBrowse =
         * (ImageButton)
         * findViewById(R.id.imageButtonParivartanDocumentsUploadProposerPhotoBrowse
         * ); imageButtonParivartanDocumentsUploadProposerPhotoUpload =
         * (ImageButton)
         * findViewById(R.id.imageButtonParivartanDocumentsUploadProposerPhotoUpload
         * );
         */

        Ibtn_signatureofPolicyHolders = (ImageButton) findViewById(R.id.Ibtn_signatureofPolicyHolders);
        /*
         * imageButtonParivartanDocumentsUploadProposerSignBrowse =
         * (ImageButton)
         * findViewById(R.id.imageButtonParivartanDocumentsUploadProposerSignBrowse
         * ); imageButtonParivartanDocumentsUploadProposerSignUpload =
         * (ImageButton)
         * findViewById(R.id.imageButtonParivartanDocumentsUploadProposerSignUpload
         * );
         */

        /*
         * imageButtonParivartanDocumentsUploadLifeAssuredPhoto = (ImageButton)
         * findViewById
         * (R.id.imageButtonParivartanDocumentsUploadLifeAssuredPhoto);
         * imageButtonParivartanDocumentsUploadLifeAssuredPhotoCapture =
         * (ImageButton) findViewById(R.id.
         * imageButtonParivartanDocumentsUploadLifeAssuredPhotoCapture);
         * imageButtonParivartanDocumentsUploadLifeAssuredPhotoBrowse =
         * (ImageButton) findViewById(R.id.
         * imageButtonParivartanDocumentsUploadLifeAssuredPhotoBrowse);
         * imageButtonParivartanDocumentsUploadLifeAssuredPhotoUpload =
         * (ImageButton) findViewById(R.id.
         * imageButtonParivartanDocumentsUploadLifeAssuredPhotoUpload);
         */

        Ibtn_signatureofLifeAssured = (ImageButton) findViewById(R.id.Ibtn_signatureofLifeAssured);
        imageButtonParivartanDocumentsUploadLifeAssuredSignBrowse = (ImageButton) findViewById(R.id.imageButtonParivartanDocumentsUploadLifeAssuredSignBrowse);
        ImageButton imageButtonParivartanDocumentsUploadLifeAssuredSignUpload = (ImageButton) findViewById(R.id.imageButtonParivartanDocumentsUploadLifeAssuredSignUpload);

        Ibtn_signatureofThirdParty = (ImageButton) findViewById(R.id.Ibtn_signatureofThirdParty);
        imageButtonParivartanDocumentsUploadThirdPartySignBrowse = (ImageButton) findViewById(R.id.imageButtonParivartanDocumentsUploadThirdPartySignBrowse);
        ImageButton imageButtonParivartanDocumentsUploadThirdPartySignUpload = (ImageButton) findViewById(R.id.imageButtonParivartanDocumentsUploadThirdPartySignUpload);

        Ibtn_signatureofAppointee = (ImageButton) findViewById(R.id.Ibtn_signatureofAppointee);
        imageButtonParivartanDocumentsUploadAppointeeSignBrowse = (ImageButton) findViewById(R.id.imageButtonParivartanDocumentsUploadAppointeeSignBrowse);
        ImageButton imageButtonParivartanDocumentsUploadAppointeeSignUpload = (ImageButton) findViewById(R.id.imageButtonParivartanDocumentsUploadAppointeeSignUpload);

        /*
         * imageButtonParivartanDocumentsUploadProposerPhoto
         * .setOnClickListener(this);
         * imageButtonParivartanDocumentsUploadProposerPhotoCapture
         * .setOnClickListener(this);
         * imageButtonParivartanDocumentsUploadProposerPhotoBrowse
         * .setOnClickListener(this);
         * imageButtonParivartanDocumentsUploadProposerPhotoUpload
         * .setOnClickListener(this);
         */

        // Ibtn_signatureofPolicyHolders.setOnClickListener(this);
        /*
         * imageButtonParivartanDocumentsUploadProposerSignBrowse
         * .setOnClickListener(this);
         * imageButtonParivartanDocumentsUploadProposerSignUpload
         * .setOnClickListener(this);
         *
         * imageButtonParivartanDocumentsUploadLifeAssuredPhoto
         * .setOnClickListener(this);
         * imageButtonParivartanDocumentsUploadLifeAssuredPhotoCapture
         * .setOnClickListener(this);
         * imageButtonParivartanDocumentsUploadLifeAssuredPhotoBrowse
         * .setOnClickListener(this);
         * imageButtonParivartanDocumentsUploadLifeAssuredPhotoUpload
         * .setOnClickListener(this);
         */

        Ibtn_signatureofLifeAssured.setOnClickListener(this);
        imageButtonParivartanDocumentsUploadLifeAssuredSignBrowse
                .setOnClickListener(this);
        imageButtonParivartanDocumentsUploadLifeAssuredSignUpload
                .setOnClickListener(this);

        Ibtn_signatureofThirdParty.setOnClickListener(this);
        imageButtonParivartanDocumentsUploadThirdPartySignBrowse
                .setOnClickListener(this);
        imageButtonParivartanDocumentsUploadThirdPartySignUpload
                .setOnClickListener(this);

        Ibtn_signatureofAppointee.setOnClickListener(this);
        imageButtonParivartanDocumentsUploadAppointeeSignBrowse
                .setOnClickListener(this);
        imageButtonParivartanDocumentsUploadAppointeeSignUpload
                .setOnClickListener(this);

        DatabaseHelper databaseHelper = new DatabaseHelper(context);

        setURNSpinner();

        initialiseDocumentUploadPart();
        Flag = 0;

        // age
        ArrayList<String> upload_age_doc_name = new ArrayList<>();
        upload_age_doc_name.add("Select Document");
        upload_age_doc_name.add("Aadhar card with complete DOB");
        upload_age_doc_name.add("Aadhar card with incomplete DOB");
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
        fillSpinnerValue(spnr_document_upload_document_age, upload_age_doc_name);

        // Address
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
        fillSpinnerValue(spnr_document_upload_document_address,
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
        fillSpinnerValue(spnr_document_upload_document_identity,
                upload_identity_doc_name);

        // income
        List<String> upload_income_doc_name = new ArrayList<>();
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

        // bank proof list
        List<String> ls_documents_direct_credit = new ArrayList<>();

        ls_documents_direct_credit.add("Select Document");
        ls_documents_direct_credit.add("Copy of Bank Statement");
        ls_documents_direct_credit.add("Copy of Bank Passbook");
        ls_documents_direct_credit.add("Cancelled Cheque");
        ls_documents_direct_credit.add("Annexure III");

        fillSpinnerValue(spnr_document_upload_document_bank,
                ls_documents_direct_credit);

        List<String> ls_EFT = new ArrayList<>();

        ls_EFT.add("Select Document");
        ls_EFT.add("EFT");
        ls_EFT.add("CHEQUE");
        ls_EFT.add("DRAFT");

        fillSpinnerValue(spnr_document_upload_document_eft, ls_EFT);

        setBtnListenerOrDisable(
                img_btn_document_upload_click_image_otp_self_declaration,
                OtpSelfDeclarationOnClickListener,
                MediaStore.ACTION_IMAGE_CAPTURE);

        setBtnListenerOrDisable(img_btn_document_upload_click_image_age,
                AgeProofOnClickListener, MediaStore.ACTION_IMAGE_CAPTURE);
        setBtnListenerOrDisable(img_btn_document_upload_click_image_identity,
                IdentityProofOnClickListener, MediaStore.ACTION_IMAGE_CAPTURE);
        setBtnListenerOrDisable(img_btn_document_upload_click_image_address,
                AddressProofOnClickListener, MediaStore.ACTION_IMAGE_CAPTURE);
        setBtnListenerOrDisable(img_btn_document_upload_click_image_income,
                IncomeProofOnClickListener, MediaStore.ACTION_IMAGE_CAPTURE);

        setBtnListenerOrDisable(img_btn_document_upload_click_image_others,
                OthersProofOnClickListener, MediaStore.ACTION_IMAGE_CAPTURE);

        setBtnListenerOrDisable(img_btn_document_upload_click_image_bank,
                BankProofOnClickListener, MediaStore.ACTION_IMAGE_CAPTURE);

        setBtnListenerOrDisable(img_btn_document_upload_click_image_eft,
                EFTProofOnClickListener, MediaStore.ACTION_IMAGE_CAPTURE);

        setBtnListenerOrDisable(
                img_btn_document_upload_click_image_proposer_photo,
                ProposerPhotoOnClickListener, MediaStore.ACTION_IMAGE_CAPTURE);

        setBtnListenerOrDisable(
                img_btn_document_upload_click_image_life_assured_photo,
                LifeAssuredPhotoOnClickListener,
                MediaStore.ACTION_IMAGE_CAPTURE);

        setBtnListenerOrDisable(
                img_btn_document_upload_click_image_customer_photo,
                CustomerPhotoOnClickListener, MediaStore.ACTION_IMAGE_CAPTURE);

        strCIFBDMEmailId = commonMethods.GetUserEmail(context);
        strCIFBDMMObileNo = commonMethods.GetUserMobile(context);
    }

    private void setURNSpinner() {

        if (commonMethods.isNetworkConnected(context)) {
            asyncGetBAOnlineURN_smrt = new AsyncGetBAOnlineURN_SMRT();
            asyncGetBAOnlineURN_smrt.execute();
        } else {
            commonMethods.showMessageDialog(context, commonMethods.NO_INTERNET_MESSAGE);
        }
    }

    private void initialiseDocumentUploadPart() {

        // self declaration
        img_btn_document_upload_preview_image_otp_self_declaration = (ImageButton) findViewById(R.id.img_btn_document_upload_preview_image_otp_self_declaration);
        img_btn_document_delete_otp_self_declaration = (ImageButton) findViewById(R.id.img_btn_document_delete_otp_self_declaration);
        img_btn_document_upload_click_image_otp_self_declaration = (ImageButton) findViewById(R.id.img_btn_document_upload_click_image_otp_self_declaration);
        img_btn_document_upload_click_browse_otp_self_declaration = (ImageButton) findViewById(R.id.img_btn_document_upload_click_browse_otp_self_declaration);
        img_btn_document_upload_otp_self_declaration_upload = (ImageButton) findViewById(R.id.img_btn_document_upload_otp_self_declaration_upload);

        // age
        //tr_ageproof = (TableRow) findViewById(R.id.tr_ageproof);
        /*
         * tr_ageproof.setEnabled(false); setChildVeiwVisibility(tr_ageproof,
         * false);
         */
        spnr_document_upload_document_age = (Spinner) findViewById(R.id.spnr_document_upload_document_age);
        spnr_document_upload_document_age.setOnItemSelectedListener(this);
        img_btn_document_upload_preview_image_age = (ImageButton) findViewById(R.id.img_btn_document_upload_preview_image_age);
        img_btn_document_delete_age = (ImageButton) findViewById(R.id.img_btn_document_delete_age);
        img_btn_document_upload_click_image_age = (ImageButton) findViewById(R.id.img_btn_document_upload_click_image_age);
        img_btn_document_upload_click_browse_age = (ImageButton) findViewById(R.id.img_btn_document_upload_click_browse_age);
        img_btn_document_upload_age_upload = (ImageButton) findViewById(R.id.img_btn_document_upload_age_upload);
        llAgeAadhaarScan = (LinearLayout) findViewById(R.id.llAgeAadhaarScan);
        img_btn_QR_age_scan = (ImageView) findViewById(R.id.img_btn_QR_age_scan);
        img_btn_QR_age_scan.setOnClickListener(this);

        llAgeQR = (LinearLayout) findViewById(R.id.llAgeQR);
        btn_offline_ekyc_age = (Button) findViewById(R.id.btn_offline_ekyc_age);
        btn_offline_ekyc_age.setOnClickListener(this);

        // identity
        //tr_identityproof = (TableRow) findViewById(R.id.tr_identityproof);
        /*
         * tr_identityproof.setEnabled(false);
         * setChildVeiwVisibility(tr_identityproof, false);
         */
        spnr_document_upload_document_identity = (Spinner) findViewById(R.id.spnr_document_upload_document_identity);
        spnr_document_upload_document_identity.setOnItemSelectedListener(this);
        img_btn_document_upload_preview_image_identity = (ImageButton) findViewById(R.id.img_btn_document_upload_preview_image_identity);
        img_btn_document_delete_identity = (ImageButton) findViewById(R.id.img_btn_document_delete_identity);
        img_btn_document_upload_click_image_identity = (ImageButton) findViewById(R.id.img_btn_document_upload_click_image_identity);
        img_btn_document_upload_identity_upload = (ImageButton) findViewById(R.id.img_btn_document_upload_identity_upload);
        img_btn_document_upload_click_browse_identity = (ImageButton) findViewById(R.id.img_btn_document_upload_click_browse_identity);
        llIdentityAadhaarScan = (LinearLayout) findViewById(R.id.llIdentityAadhaarScan);
        img_btn_QR_Identity_scan = (ImageView) findViewById(R.id.img_btn_QR_Identity_scan);
        img_btn_QR_Identity_scan.setOnClickListener(this);

        llIdentityQR = (LinearLayout) findViewById(R.id.llIdentityQR);
        btn_offline_ekyc_identity = (Button) findViewById(R.id.btn_offline_ekyc_identity);
        btn_offline_ekyc_identity.setOnClickListener(this);

        // Address
        //tr_addressproof = (TableRow) findViewById(R.id.tr_addressproof);
        /*
         * tr_addressproof.setEnabled(false);
         * setChildVeiwVisibility(tr_addressproof, false);
         */
        spnr_document_upload_document_address = (Spinner) findViewById(R.id.spnr_document_upload_document_address);
        spnr_document_upload_document_address.setOnItemSelectedListener(this);
        img_btn_document_upload_click_preview_image_address = (ImageButton) findViewById(R.id.img_btn_document_upload_click_preview_image_address);
        img_btn_document_delete_address = (ImageButton) findViewById(R.id.img_btn_document_delete_address);
        img_btn_document_upload_click_image_address = (ImageButton) findViewById(R.id.img_btn_document_upload_click_image_address);
        img_btn_document_upload_click_browse_address = (ImageButton) findViewById(R.id.img_btn_document_upload_click_browse_address);
        img_btn_document_upload_address_upload = (ImageButton) findViewById(R.id.img_btn_document_upload_address_upload);
        llAddressAadhaarScan = (LinearLayout) findViewById(R.id.llAddressAadhaarScan);
        img_btn_QR_Address_scan = (ImageView) findViewById(R.id.img_btn_QR_Address_scan);
        img_btn_QR_Address_scan.setOnClickListener(this);

        llAddressQR = (LinearLayout) findViewById(R.id.llAddressQR);
        btn_offline_ekyc_address = (Button) findViewById(R.id.btn_offline_ekyc_address);
        btn_offline_ekyc_address.setOnClickListener(this);

        // income
        //tr_income_proof = (TableRow) findViewById(R.id.tr_income_proof);
        /*
         * tr_income_proof.setEnabled(false);
         * setChildVeiwVisibility(tr_income_proof, false);
         */
        spnr_document_upload_document_income = (Spinner) findViewById(R.id.spnr_document_upload_document_income);
        img_btn_document_upload_click_preview_image_income = (ImageButton) findViewById(R.id.img_btn_document_upload_click_preview_image_income);
        img_btn_document_delete_income = (ImageButton) findViewById(R.id.img_btn_document_delete_income);
        img_btn_document_upload_click_image_income = (ImageButton) findViewById(R.id.img_btn_document_upload_click_image_income);
        img_btn_document_upload_click_browse_income = (ImageButton) findViewById(R.id.img_btn_document_upload_click_browse_income);
        img_btn_document_upload_income_upload = (ImageButton) findViewById(R.id.img_btn_document_upload_income_upload);

        // bank proof
        //tr_bank_proof = (TableRow) findViewById(R.id.tr_bank_proof);
        /*
         * tr_bank_proof.setEnabled(false);
         * setChildVeiwVisibility(tr_bank_proof, false);
         */
        spnr_document_upload_document_bank = (Spinner) findViewById(R.id.spnr_document_upload_document_bank);
        img_btn_document_upload_click_preview_image_bank = (ImageButton) findViewById(R.id.img_btn_document_upload_click_preview_image_bank);
        img_btn_document_delete_bank = (ImageButton) findViewById(R.id.img_btn_document_delete_bank);
        img_btn_document_upload_click_image_bank = (ImageButton) findViewById(R.id.img_btn_document_upload_click_image_bank);
        img_btn_document_upload_click_browse_bank = (ImageButton) findViewById(R.id.img_btn_document_upload_click_browse_bank);
        img_btn_document_upload_bank_upload = (ImageButton) findViewById(R.id.img_btn_document_upload_bank_upload);

        // eft
        //tr_eft_check_proof = (TableRow) findViewById(R.id.tr_eft_check_proof);
        /*
         * tr_eft_check_proof.setEnabled(false);
         * setChildVeiwVisibility(tr_eft_check_proof, false);
         */
        spnr_document_upload_document_eft = (Spinner) findViewById(R.id.spnr_document_upload_document_eft);
        img_btn_document_upload_preview_image_eft = (ImageButton) findViewById(R.id.img_btn_document_upload_preview_image_eft);
        img_btn_document_delete_eft = (ImageButton) findViewById(R.id.img_btn_document_delete_eft);
        img_btn_document_upload_click_image_eft = (ImageButton) findViewById(R.id.img_btn_document_upload_click_image_eft);
        img_btn_document_upload_click_browse_eft = (ImageButton) findViewById(R.id.img_btn_document_upload_click_browse_eft);
        img_btn_document_upload_eft_upload = (ImageButton) findViewById(R.id.img_btn_document_upload_eft_upload);

        // others
        edt_document_upload_document_others = (EditText) findViewById(R.id.edt_document_upload_document_others);
        img_btn_document_upload_click_preview_image_others = (ImageButton) findViewById(R.id.img_btn_document_upload_click_preview_image_others);
        img_btn_document_delete_Others = (ImageButton) findViewById(R.id.img_btn_document_delete_Others);
        img_btn_document_upload_click_image_others = (ImageButton) findViewById(R.id.img_btn_document_upload_click_image_others);
        img_btn_document_upload_click_browse_Others = (ImageButton) findViewById(R.id.img_btn_document_upload_click_browse_Others);
        img_btn_document_upload_Others_upload = (ImageButton) findViewById(R.id.img_btn_document_upload_Others_upload);

        // customer photo
        //tr_customer_photo = (TableRow) findViewById(R.id.tr_customer_photo);
        /*
         * tr_customer_photo.setEnabled(false);
         * setChildVeiwVisibility(tr_customer_photo, false);
         */
        Spinner spnr_document_upload_customer_photo = (Spinner) findViewById(R.id.spnr_document_upload_customer_photo);
        img_btn_document_upload_preview_customer_photo = (ImageButton) findViewById(R.id.img_btn_document_upload_preview_customer_photo);
        img_btn_document_delete_customer_photo = (ImageButton) findViewById(R.id.img_btn_document_delete_customer_photo);
        img_btn_document_upload_click_image_customer_photo = (ImageButton) findViewById(R.id.img_btn_document_upload_click_image_customer_photo);
        img_btn_document_upload_click_browse_customer_photo = (ImageButton) findViewById(R.id.img_btn_document_upload_click_browse_customer_photo);
        img_btn_document_upload_customer_photo_upload = (ImageButton) findViewById(R.id.img_btn_document_upload_customer_photo_upload);

        // proposer photo
        img_btn_document_upload_click_image_proposer_photo = (ImageButton) findViewById(R.id.img_btn_document_upload_click_image_proposer_photo);
        img_btn_document_upload_click_browse_proposer_photo = (ImageButton) findViewById(R.id.img_btn_document_upload_click_browse_proposer_photo);
        img_btn_document_upload_proposer_photo_upload = (ImageButton) findViewById(R.id.img_btn_document_upload_proposer_photo_upload);
        img_btn_document_delete_proposer_photo = (ImageButton) findViewById(R.id.img_btn_document_delete_proposer_photo);
        img_btn_document_upload_preview_proposer_photo = (ImageButton) findViewById(R.id.img_btn_document_upload_preview_proposer_photo);

        // life assured photo
        img_btn_document_upload_preview_life_assured_photo = (ImageButton) findViewById(R.id.img_btn_document_upload_preview_life_assured_photo);
        img_btn_document_delete_life_assured_photo = (ImageButton) findViewById(R.id.img_btn_document_delete_life_assured_photo);
        img_btn_document_upload_click_image_life_assured_photo = (ImageButton) findViewById(R.id.img_btn_document_upload_click_image_life_assured_photo);
        img_btn_document_upload_click_browse_life_assured_photo = (ImageButton) findViewById(R.id.img_btn_document_upload_click_browse_life_assured_photo);
        img_btn_document_upload_life_assured_photo_upload = (ImageButton) findViewById(R.id.img_btn_document_upload_life_assured_photo_upload);

    }

    private void fillSpinnerValue(Spinner spinner, List<String> value_list) {

        Element_TextView_BaseAdapter retd_adapter = new Element_TextView_BaseAdapter(
                ParivartanDocumentsUploadActivity.this, value_list);
        spinner.setAdapter(retd_adapter);
    }

    private void setBtnListenerOrDisable(ImageButton btn,
                                         OnClickListener mTakePicOnClickListener, String intentName) {
        if (isIntentAvailable(this, intentName)) {
            btn.setOnClickListener(mTakePicOnClickListener);
        } else {
            btn.setTag(getText(R.string.cannot).toString() + " "
                    + btn.getContext());
            btn.setClickable(false);
        }
    }

    private void ClearDocumentsPDF() {

        proposerBrowsedPhotoFile = null;
        lifeAssuredBrowsedPhotoFile = null;
        lifeAssuredBrowsedSignFile = null;
        thirdPartyBrowsedSignFile = null;
        AppointeeBrowsedSignFile = null;
        otherProofFileName = null;
        photo = null;
        EFTProofFileName = null;
        bankProofFileName = null;
        incomeProofFileName = null;
        addressProofFileName = null;
        identityProofFileName = null;
        ageProofFileName = null;
        OTPSelfDeclarationFileName = null;

        /*photoBitmap = lifeAssuredBitmap = null;

        SelfAttestedDocumentActivity.lst_OTPSelfDeclarationBitmap.clear();
        SelfAttestedDocumentActivity.lst_AgeBitmap.clear();
        SelfAttestedDocumentActivity.lst_IdentityBitmap.clear();
        SelfAttestedDocumentActivity.lst_IncomeBitmap.clear();
        SelfAttestedDocumentActivity.lst_AddressBitmap.clear();
        // SelfAttestedDocumentActivity.lst_CommAddressBitmap.clear();
        // SelfAttestedDocumentActivity.lst_OtherBitmap.clear();
        SelfAttestedDocumentActivity.lst_BankBitmap.clear();
        SelfAttestedDocumentActivity.lst_EFTBitmap.clear();
        SelfAttestedDocumentActivity.lst_CustomerPhotoBitmap.clear();
        SelfAttestedDocumentActivity.lst_OtherBitmap.clear();
        // SelfAttestedDocumentActivity.lst_ThirdPartyBitmap.clear();
        // SelfAttestedDocumentActivity.lst_SIEFTBitmap.clear();*/
    }

    private void dispatchTakePictureIntent(String str) {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        switch (str) {
            case "1":
                f = mStorageUtils.createFileToAppSpecificDir(context,str_selected_urn_no + "_cust1Photo.jpg");
                break;
            case "2":
                f = mStorageUtils.createFileToAppSpecificDir(context,str_selected_urn_no + "_cust2Photo.jpg");
                break;
            case "0":
                f = mStorageUtils.createFileToAppSpecificDir(context, JPEG_FILE_PREFIX + str_selected_urn_no + "_X"
                        + increment + "_" + JPEG_FILE_SUFFIX);
                break;
        }

        mCurrentPhotoPath = f == null ? null : f.getAbsolutePath();

        //nought changes
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, commonMethods.getContentUri(context, f));
        } else {
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
        }
        startActivityForResult(takePictureIntent, REQUEST_CODE_CAPTURE_FILE);
    }

    public void onUpload_OTPSelfDeclaration(View v) {

        if (OTPSelfDeclarationFileName != null) {
            if (OTPSelfDeclarationFileName.exists()) {

                increment = 25;

                uploaddoc(OTPSelfDeclarationFileName);

            } else {
                Toast.makeText(
                        context,
                        "Please Capture Or Browse The OTP Self Declaration Proof Document First",
                        Toast.LENGTH_SHORT).show();

                ClearDocumentsPDF();
            }
        } else {

            ClearDocumentsPDF();
        }
    }

    public void onUpload_AgeProof1(View v) {

        if (ageProofFileName != null) {
            if (ageProofFileName.exists()) {

                if (!(spnr_document_upload_document_age.getSelectedItem()
                        .toString()).equalsIgnoreCase("Select Document")) {

                    strUploadDocument = spnr_document_upload_document_age.getSelectedItem().toString();

                    increment = 1;

                    uploaddoc(ageProofFileName);
                    // DeleteFiles();
                } else {

                    String message = "";
                    if (spnr_document_upload_document_age.getSelectedItem()
                            .toString().equalsIgnoreCase("Select Document")) {
                        message = "Please Select Age Proof Document First";
                    } else if (ageProofFileName == null) {
                        message = "Please Capture Or Browse The Age Proof Document First";
                    }
                    AlertDialogMessage obj = new AlertDialogMessage();
                    obj.dialog(this, message, true);

                }
                // }
            } else {
                Toast.makeText(
                        context,
                        "Please Capture Or Browse The Age Proof Document First",
                        Toast.LENGTH_SHORT).show();

                ClearDocumentsPDF();

            }
        } else {
            ClearDocumentsPDF();
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

                ClearDocumentsPDF();

                Toast.makeText(
                        context,
                        "Please Capture Or Browse The Identity Proof Document First",
                        Toast.LENGTH_SHORT).show();
            }
        } else {

            ClearDocumentsPDF();

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
                // }
            } else {

                ClearDocumentsPDF();

                Toast.makeText(context, "Please Capture Or Browse The Address Proof Document First", Toast.LENGTH_SHORT).show();
            }
        } else {

            ClearDocumentsPDF();
        }
    }

    public void onUpload_IncomeProof1(View v) {
        if (incomeProofFileName != null) {
            if (incomeProofFileName.exists()) {

                if (!(spnr_document_upload_document_income.getSelectedItem()
                        .toString()).equalsIgnoreCase("Select Document")) {
                    strUploadDocument = spnr_document_upload_document_income
                            .getSelectedItem().toString();
                    increment = 4;

                    uploaddoc(incomeProofFileName);
                    // DeleteFiles();

                } else {
                    String message = "";
                    if (spnr_document_upload_document_income.getSelectedItem()
                            .toString().equalsIgnoreCase("Select Document")) {
                        message = "Please Select Income Document First";
                    } else if (incomeProofFileName == null) {
                        message = "Please Capture Or Browse The Income Proof Document First";
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

    }

    public void onUpload_BankProof1(View v) {
        if (bankProofFileName != null) {
            if (bankProofFileName.exists()) {

                if (!(spnr_document_upload_document_bank.getSelectedItem()
                        .toString()).equalsIgnoreCase("Select Document")) {
                    strUploadDocument = spnr_document_upload_document_bank
                            .getSelectedItem().toString();
                    increment = 6;

                    uploaddoc(bankProofFileName);
                    // DeleteFiles();

                } else {
                    String message = "";
                    if (spnr_document_upload_document_bank.getSelectedItem()
                            .toString().equalsIgnoreCase("Select Document")) {
                        message = "Please Select Bank Document First";
                    } else if (bankProofFileName == null) {
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
                        "Please Capture Or Browse The Bank Proof Document First",
                        Toast.LENGTH_SHORT).show();
            }
        } else {

            ClearDocumentsPDF();
        }

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

    }

    public void onUpload_eftProof1(View v) {
        if (EFTProofFileName != null) {
            if (EFTProofFileName.exists()) {

                if (!(spnr_document_upload_document_eft.getSelectedItem()
                        .toString()).equalsIgnoreCase("Select Document")) {
                    strUploadDocument = spnr_document_upload_document_eft
                            .getSelectedItem().toString();
                    increment = 7;

                    uploaddoc(EFTProofFileName);
                    // DeleteFiles();

                } else {
                    String message = "";
                    if (spnr_document_upload_document_eft.getSelectedItem()
                            .toString().equalsIgnoreCase("Select Document")) {
                        message = "Please Select Document First";
                    } else if (EFTProofFileName == null) {
                        message = "Please Capture Or Browse The EFT/Chqeue/DD Proof Document First";
                    }
                    AlertDialogMessage obj = new AlertDialogMessage();
                    obj.dialog(this, message, true);

                }
                // }
            } else {
                ClearDocumentsPDF();

                Toast.makeText(context,
                        "Please Capture Or Browse Document First",
                        Toast.LENGTH_SHORT).show();
            }
        } else {

            ClearDocumentsPDF();
        }

    }

    public void onUpload_CustomerPhoto(View v) {
        if (SelfAttestedDocumentActivity.lst_CustomerPhotoBitmap != null) {
            if (SelfAttestedDocumentActivity.lst_CustomerPhotoBitmap.size() >= 1
                    && flag_cancel_customer_photo == 1) {
                if ((customerPhotoBitmap != null)) {

                    String DocumentName = "Customer Photo";
                    increment = 8;

                    f = mStorageUtils.createFileToAppSpecificDir(context,str_selected_urn_no + "_" + "X" + increment + "." + "pdf");

                    uploaddoc(f);
                    // DeleteFiles();
                } else {
                    String message = "";

                    if (EFTProofBitmap == null) {
                        message = "Please Capture Or Browse The Customer photo First";
                    }
                    AlertDialogMessage obj = new AlertDialogMessage();
                    obj.dialog(this, message, true);
                }
            } else {

                SelfAttestedDocumentActivity.lst_AgeBitmap.clear();
                SelfAttestedDocumentActivity.lst_IdentityBitmap.clear();
                SelfAttestedDocumentActivity.lst_IncomeBitmap.clear();
                SelfAttestedDocumentActivity.lst_AddressBitmap.clear();
                SelfAttestedDocumentActivity.lst_OtherBitmap.clear();
                SelfAttestedDocumentActivity.lst_BankBitmap.clear();
                SelfAttestedDocumentActivity.lst_BankBitmap.clear();
                SelfAttestedDocumentActivity.lst_EFTBitmap.clear();
                SelfAttestedDocumentActivity.lst_CommAddressBitmap.clear();
                SelfAttestedDocumentActivity.lst_CustomerPhotoBitmap.clear();

                Toast.makeText(context,
                        "Please Capture Or Browse Document First",
                        Toast.LENGTH_SHORT).show();
            }
        } else {

            SelfAttestedDocumentActivity.lst_AgeBitmap.clear();
            SelfAttestedDocumentActivity.lst_IdentityBitmap.clear();
            SelfAttestedDocumentActivity.lst_IncomeBitmap.clear();
            SelfAttestedDocumentActivity.lst_AddressBitmap.clear();
            SelfAttestedDocumentActivity.lst_OtherBitmap.clear();
            SelfAttestedDocumentActivity.lst_BankBitmap.clear();
            SelfAttestedDocumentActivity.lst_BankBitmap.clear();
            SelfAttestedDocumentActivity.lst_EFTBitmap.clear();
            SelfAttestedDocumentActivity.lst_CommAddressBitmap.clear();
            SelfAttestedDocumentActivity.lst_CustomerPhotoBitmap.clear();
        }
    }

    private void uploaddoc(File fileName) {

        FileInputStream fin = null;
        try {
            str_extension = fileName.getAbsolutePath().substring(fileName.getAbsolutePath().lastIndexOf("."));

            fin = new FileInputStream(fileName);

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            int bytesRead = 0;
            try {
                assert fin != null;
                while ((bytesRead = fin.read(b)) != -1) {
                    bos.write(b, 0, bytesRead);
                }

                bytes = bos.toByteArray();
                bos.flush();
                bos.close();

                asyncUploadDoc = new AsyncUploadDoc();
                asyncUploadDoc.execute();

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        } catch (FileNotFoundException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        switch (id) {
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

                break;

            default:
                break;
        }
        return super.onCreateDialog(id);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {

            case R.id.spinnerUrnList:
                if (position == 0) {
                    str_selected_urn_no = "";
                    AdditionalUtilityActivity.ProposalNumber = "";
                    linearLayoutParivartanDocumentsUpload
                            .setVisibility(View.GONE);
                } else {

                    clearAllAfterURNChanged();
                    AdditionalUtilityActivity.ProposalNumber = str_selected_urn_no = spinnerUrnList
                            .getSelectedItem().toString();
                    linearLayoutParivartanDocumentsUpload
                            .setVisibility(View.VISIBLE);
                }
                break;

            case R.id.spnr_document_upload_document_age:

                if (spnr_document_upload_document_age.getSelectedItem().toString().toLowerCase().contains("aadhar card")) {
                    is1WProduct = false;
                    is1WProductEKYC = false;
                    llAgeAadhaarScan.setVisibility(View.VISIBLE);
                    llAgeQR.setVisibility(View.GONE);
                } else {
                    llAgeAadhaarScan.setVisibility(View.VISIBLE);
                    llAgeQR.setVisibility(View.GONE);
                }

                break;

            case R.id.spnr_document_upload_document_identity:

                if (spnr_document_upload_document_identity.getSelectedItem().toString().toLowerCase().contains("aadhar card")) {
                    is1WProduct = false;
                    is1WProductEKYC = false;
                    llIdentityAadhaarScan.setVisibility(View.VISIBLE);
                    llIdentityQR.setVisibility(View.GONE);
                } else {
                    llIdentityAadhaarScan.setVisibility(View.VISIBLE);
                    llIdentityQR.setVisibility(View.GONE);
                }

                break;

            case R.id.spnr_document_upload_document_address:

                if (spnr_document_upload_document_address.getSelectedItem().toString().toLowerCase().contains("aadhar card")) {
                    is1WProduct = false;
                    is1WProductEKYC = false;
                    llAddressAadhaarScan.setVisibility(View.VISIBLE);
                    llAddressQR.setVisibility(View.GONE);
                } else {
                    llAddressAadhaarScan.setVisibility(View.VISIBLE);
                    llAddressQR.setVisibility(View.GONE);
                }

                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.Ibtn_signatureofPolicyHolders:
                latestImage = "proposer";
                commonMethods.windowmessageProposersgin(context,
                        str_selected_urn_no + "_cust1sign");
                break;

            case R.id.Ibtn_signatureofLifeAssured:
                latestImage = "lifeAssuredSign";
                commonMethods.windowmessageProposersgin(context,
                        str_selected_urn_no + "_cust2sign");
                break;
            case R.id.imageButtonParivartanDocumentsUploadLifeAssuredSignBrowse:
                latestImage = "lifeAssuredSignBrowse";
                onBrowse();
                break;

            case R.id.Ibtn_signatureofThirdParty:
                latestImage = "thirdParty";
                commonMethods.windowmessageProposersgin(context,
                        str_selected_urn_no + "_thirdParty");
                break;
            case R.id.imageButtonParivartanDocumentsUploadThirdPartySignBrowse:
                latestImage = "thirdPartyBrowsedFile";
                onBrowse();
                break;

            case R.id.Ibtn_signatureofAppointee:
                latestImage = "Appointee";
                commonMethods.windowmessageProposersgin(context,
                        str_selected_urn_no + "_appointee");

                break;
            case R.id.imageButtonParivartanDocumentsUploadAppointeeSignBrowse:
                latestImage = "AppointeeBrowsedFile";
                onBrowse();
                break;

            case R.id.imageButtonParivartanDocumentsUploadLifeAssuredSignUpload:
                uploadLifeAssuredSign();
                break;
            case R.id.imageButtonParivartanDocumentsUploadThirdPartySignUpload:
                uploadThirdPartySign();
                break;
            case R.id.imageButtonParivartanDocumentsUploadAppointeeSignUpload:
                uploadAppointeeSign();
                break;

            case R.id.img_btn_QR_age_scan:

                if (is1WProductEKYC) {
                    commonMethods.showMessageDialog(context, "Please provide spouse ekyc");
                } else {
                    qrCodeClick = 1;

                    IntentIntegrator integrator = new IntentIntegrator(ParivartanDocumentsUploadActivity.this);
                    integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                    integrator.setPrompt("Scan Code");
                    integrator.setCameraId(0);
                    integrator.setBeepEnabled(true);
                    integrator.setBarcodeImageEnabled(false);
                    integrator.initiateScan();
                }

                break;

            case R.id.img_btn_QR_Identity_scan:

                if (is1WProductEKYC) {
                    commonMethods.showMessageDialog(context, "Please provide spouse ekyc");
                } else {

                    qrCodeClick = 2;

                    IntentIntegrator integrator1 = new IntentIntegrator(ParivartanDocumentsUploadActivity.this);
                    integrator1.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                    integrator1.setPrompt("Scan Code");
                    integrator1.setCameraId(0);
                    integrator1.setBeepEnabled(true);
                    integrator1.setBarcodeImageEnabled(false);
                    integrator1.initiateScan();
                }

                break;

            case R.id.img_btn_QR_Address_scan:

                if (is1WProductEKYC) {
                    commonMethods.showMessageDialog(context, "Please provide spouse ekyc");
                } else {
                    qrCodeClick = 3;

                    IntentIntegrator integrator2 = new IntentIntegrator(ParivartanDocumentsUploadActivity.this);
                    integrator2.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                    integrator2.setPrompt("Scan Code");
                    integrator2.setCameraId(0);
                    integrator2.setBeepEnabled(true);
                    integrator2.setBarcodeImageEnabled(false);
                    integrator2.initiateScan();
                }

                break;

            case R.id.btn_offline_ekyc_age:

                if (is1WProduct) {
                    commonMethods.showMessageDialog(context, "Please Scan aadhar QR for spouse");
                } else {
                    qrCodeClick = 1;
                    openOfflineEkyc();
                }
                break;

            case R.id.btn_offline_ekyc_identity:

                if (is1WProduct) {
                    commonMethods.showMessageDialog(context, "Please Scan aadhar QR for spouse");
                } else {
                    qrCodeClick = 2;
                    openOfflineEkyc();
                }
                break;

            case R.id.btn_offline_ekyc_address:

                if (is1WProduct) {
                    commonMethods.showMessageDialog(context, "Please Scan aadhar QR for spouse");
                } else {
                    qrCodeClick = 3;
                    openOfflineEkyc();
                }
                break;
        }

    }

    private void openOfflineEkyc() {
        if (commonMethods.isNetworkConnected(context)) {
            Intent intent = new Intent(ParivartanDocumentsUploadActivity.this,
                    OfflineKYCActivity.class);
            intent.putExtra("proposer_name", "");
            intent.putExtra("planName", "");
            intent.putExtra("QuatationNumber", str_selected_urn_no);
            startActivityForResult(intent, OFFLINE_EKYC_ACTIVITY);
        } else {
            commonMethods.showToast(context, "Please Check the Internet Connection");
        }
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
                new UploadScannedQRDetails().execute(strQRData);
            } else {
                Builder alertDialogBuilder = new Builder(context);
                alertDialogBuilder.setMessage("URN is Smart Humsafar Product?");
                alertDialogBuilder.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                /***** XML Parsing Start ******/
                                dialog.dismiss();
                                if (query != null) {

                                    is1WProduct = true;
                                    str1WFirstQR = AllAddress(query);

                                    commonMethods.showMessageDialog(context, "Please Scan aadhar QR for spouse");
                                } else {
                                    Toast.makeText(ParivartanDocumentsUploadActivity.this, "Invalid QR Code!", Toast.LENGTH_LONG).show();
                                }
                            }
                        });

                alertDialogBuilder.setNegativeButton("No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                is1WProduct = false;
                                dialog.dismiss();

                                /***** XML Parsing Start ******/
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

                                    new UploadScannedQRDetails().execute(strQRData);
                                } else {
                                    Toast.makeText(ParivartanDocumentsUploadActivity.this, "Invalid QR Code!", Toast.LENGTH_LONG).show();
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

    public String offlineEkycData() {

        strSelectedProof = "";

        switch (qrCodeClick) {
            case 1:
                strSelectedProof = spnr_document_upload_document_age.getSelectedItem().toString();
                break;

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
                        "<Aadhar_qr_prop_name>" + str_ekyc_code_Name + "</Aadhar_qr_prop_name>" +
                        "<Aadhar_qr_prop_dob>" + str_ekyc_code_DOB + "</Aadhar_qr_prop_dob>" +
                        "<Aadhar_qr_prop_gender>" + str_ekyc_code_Gender + "</Aadhar_qr_prop_gender>" +
                        "<Aadhar_qr_prop_address>" + str_ekyc_code_address + "</Aadhar_qr_prop_address>" +
                        "<Aadhar_qr_prop_email_id>" + str_ekyc_code_mailID + "</Aadhar_qr_prop_email_id>" +
                        "<Aadhar_qr_prop_mobile_no>" + str_ekyc_code_mobile + "</Aadhar_qr_prop_mobile_no>" +
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

        switch (qrCodeClick) {
            case 1:
                strSelectedProof = spnr_document_upload_document_age.getSelectedItem().toString();
                break;

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

    public void onUploadProposerPhoto(View v) {

        if (commonMethods.isNetworkConnected(context)) {
            if (proposerBrowsedPhotoFile != null) {

                uploaddoc(proposerBrowsedPhotoFile);

            } else {
                commonMethods.showMessageDialog(context, "Please Capture the Photo");
            }
        } else {
            commonMethods.showMessageDialog(context, commonMethods.NO_INTERNET_MESSAGE);
        }

    }

    public void onUploadLifeAssuredPhoto(View v) {

        if (commonMethods.isNetworkConnected(context)) {
            if (lifeAssuredBrowsedPhotoFile != null) {

                uploaddoc(lifeAssuredBrowsedPhotoFile);

            } else {
                commonMethods.showMessageDialog(context, "Please Capture the Photo");
            }
        } else {
            commonMethods.showMessageDialog(context, commonMethods.NO_INTERNET_MESSAGE);
        }

    }

    private void uploadLifeAssuredSign() {

        if (commonMethods.isNetworkConnected(context)) {
            if (lifeAssured_sign != null && !lifeAssured_sign.equals("")) {
                lifeAssuredSignService = new UploadLifeAssuredSignService();
                lifeAssuredSignService.execute();
            } else {
                commonMethods.showMessageDialog(context,
                        "Please Capture the Sign");
                setFocusable(Ibtn_signatureofLifeAssured);
                Ibtn_signatureofLifeAssured.requestFocus();
            }
        } else {
            commonMethods.showMessageDialog(context,
                    commonMethods.NO_INTERNET_MESSAGE);
        }

    }

    private void uploadThirdPartySign() {

        if (commonMethods.isNetworkConnected(context)) {
            if (thirdPartySign != null && !thirdPartySign.equals("")) {
                uploadThirdPartySignService = new UploadThirdPartySignService();
                uploadThirdPartySignService.execute();
            } else {
                commonMethods.showMessageDialog(context,
                        "Please Capture the Third Party Sign");
                setFocusable(Ibtn_signatureofThirdParty);
                Ibtn_signatureofThirdParty.requestFocus();
            }
        } else {
            commonMethods.showMessageDialog(context,
                    commonMethods.NO_INTERNET_MESSAGE);
        }

    }

    private void uploadAppointeeSign() {

        if (commonMethods.isNetworkConnected(context)) {
            if (appointeeSign != null && !appointeeSign.equals("")) {
                uploadAppointeeSignService = new UploadAppointeeSignService();
                uploadAppointeeSignService.execute();
            } else {
                commonMethods.showMessageDialog(context,
                        "Please Capture the Appointee Sign");
                setFocusable(Ibtn_signatureofThirdParty);
                Ibtn_signatureofThirdParty.requestFocus();
            }
        } else {
            commonMethods.showMessageDialog(context,
                    commonMethods.NO_INTERNET_MESSAGE);
        }

    }

    public void onBrowse_ProposerPhoto(View v) {
        ClearDocumentsPDF();
        uploadFlag = 0;

        img_btn_document_upload_click_image_proposer_photo
                .setImageDrawable(getResources().getDrawable(
                        R.drawable.ibtn_camera));
        Check = "ProposerPhoto";
        isBrowseCapture = REQUEST_CODE_BROWSE_FILE;

        Intent intent = new Intent(ParivartanDocumentsUploadActivity.this, OcrActivity.class);
        startActivityForResult(intent, REQUEST_OCR);

        //onBrowse();
    }

    /* private void uploadProposerSign() {

        if (commonMethods.isNetworkConnected(context)) {
            if (!proposer_sign.equals("")) {
                UploadProposerSignService proposerSignService = new UploadProposerSignService();
                proposerSignService.execute();
            } else {
                commonMethods.showMessageDialog(context,
                        "Please Capture the Signature");
                setFocusable(Ibtn_signatureofPolicyHolders);
                Ibtn_signatureofPolicyHolders.requestFocus();
            }
        } else {
            commonMethods.showMessageDialog(context,
                    commonMethods.NO_INTERNET_MESSAGE);
        }

    }*/

    public void onBrowse_LifeAssuredPhoto(View v) {
        ClearDocumentsPDF();
        uploadFlag = 0;

        img_btn_document_upload_click_image_life_assured_photo
                .setImageDrawable(getResources().getDrawable(
                        R.drawable.ibtn_camera));
        Check = "LifeAssuredPhoto";
        isBrowseCapture = REQUEST_CODE_BROWSE_FILE;

        Intent intent = new Intent(ParivartanDocumentsUploadActivity.this, OcrActivity.class);
        startActivityForResult(intent, REQUEST_OCR);

        //onBrowse();
    }

    public void onBrowse_CustomerPhoto(View v) {
        ClearDocumentsPDF();
        uploadFlag = 0;

        img_btn_document_upload_click_image_customer_photo
                .setImageDrawable(getResources().getDrawable(
                        R.drawable.ibtn_camera));
        Check = "CustomerPhoto";

        Intent intent = new Intent(ParivartanDocumentsUploadActivity.this, OcrActivity.class);
        startActivityForResult(intent, REQUEST_OCR);

        //onBrowse();
    }

    public void onBrowse_BankProof(View v) {
        ClearDocumentsPDF();

        uploadFlag = 0;

        img_btn_document_upload_click_image_bank
                .setImageDrawable(getResources().getDrawable(
                        R.drawable.ibtn_camera));
        Check = "BankProof";
        isBrowseCapture = REQUEST_CODE_BROWSE_FILE;
        increment = 6;

        /*if (spnr_document_upload_document_bank.getSelectedItem().toString().contains("Cancelled Cheque")) {

            isBrowseCapture = REQUEST_CODE_PICK_FILE;*/

        Intent intent = new Intent(ParivartanDocumentsUploadActivity.this, OcrActivity.class);
        startActivityForResult(intent, REQUEST_OCR);
        /*} else {
        onBrowse();
        }*/
    }

    public void onBrowse_eftProof(View v) {
        ClearDocumentsPDF();
        uploadFlag = 0;
        img_btn_document_upload_click_image_eft.setImageDrawable(getResources()
                .getDrawable(R.drawable.ibtn_camera));
        Check = "EFTProof";
        isBrowseCapture = REQUEST_CODE_BROWSE_FILE;
        increment = 7;

        Intent intent = new Intent(ParivartanDocumentsUploadActivity.this, OcrActivity.class);
        startActivityForResult(intent, REQUEST_OCR);

        //onBrowse();
    }

    public void onBrowse_OtherProof(View v) {
        ClearDocumentsPDF();
        uploadFlag = 0;
        img_btn_document_upload_click_image_others
                .setImageDrawable(getResources().getDrawable(
                        R.drawable.ibtn_camera));
        Check = "OtherProof";
        isBrowseCapture = REQUEST_CODE_BROWSE_FILE;
        increment = 5;

        Intent intent = new Intent(ParivartanDocumentsUploadActivity.this, OcrActivity.class);
        startActivityForResult(intent, REQUEST_OCR);

        //onBrowse();
    }

    public void onBrowse_OTPSelfDeclarationProof(View v) {
        ClearDocumentsPDF();
        uploadFlag = 0;

        img_btn_document_upload_click_image_otp_self_declaration
                .setImageDrawable(getResources().getDrawable(
                        R.drawable.ibtn_camera));
        Check = "OTPSelfDeclaration";
        isBrowseCapture = REQUEST_CODE_BROWSE_FILE;
        increment = 25;

        Intent intent = new Intent(ParivartanDocumentsUploadActivity.this, OcrActivity.class);
        startActivityForResult(intent, REQUEST_OCR);

        //onBrowse();
    }

    public void onBrowse_AgeProof(View v) {
        ClearDocumentsPDF();
        uploadFlag = 0;

        img_btn_document_upload_click_image_age.setImageDrawable(getResources()
                .getDrawable(R.drawable.ibtn_camera));
        Check = "AgeProof";

        /*if (spnr_document_upload_document_age.getSelectedItem().toString().toLowerCase().contains("aadhar card")
                || spnr_document_upload_document_age.getSelectedItem().toString().toLowerCase().contains("pancard")
                || spnr_document_upload_document_age.getSelectedItem().toString().toLowerCase().contains("passport")
                || spnr_document_upload_document_age.getSelectedItem().toString().toLowerCase().contains("voter s identity card")
                || spnr_document_upload_document_age.getSelectedItem().toString().toLowerCase().contains("driving license")) {*/

        isBrowseCapture = REQUEST_CODE_BROWSE_FILE;

            Intent intent = new Intent(ParivartanDocumentsUploadActivity.this, OcrActivity.class);
            startActivityForResult(intent, REQUEST_OCR);
        /*} else {
            onBrowse();
        }*/
    }

    public void onBrowse_IdentityProof(View v) {
        ClearDocumentsPDF();
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

        isBrowseCapture = REQUEST_CODE_BROWSE_FILE;

            Intent intent = new Intent(ParivartanDocumentsUploadActivity.this, OcrActivity.class);
            startActivityForResult(intent, REQUEST_OCR);
        /*} else {
            onBrowse();
        }*/
    }

    public void onBrowse_AddressProof(View v) {
        ClearDocumentsPDF();
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

        isBrowseCapture = REQUEST_CODE_BROWSE_FILE;

            Intent intent = new Intent(ParivartanDocumentsUploadActivity.this, OcrActivity.class);
            startActivityForResult(intent, REQUEST_OCR);
        /*} else {
            onBrowse();
        }*/

    }

    public void onBrowse_IncomeProof(View v) {
        ClearDocumentsPDF();
        uploadFlag = 0;
        img_btn_document_upload_click_image_income
                .setImageDrawable(getResources().getDrawable(
                        R.drawable.ibtn_camera));
        Check = "IncomeProof";
        isBrowseCapture = REQUEST_CODE_BROWSE_FILE;
        increment = 4;

        Intent intent = new Intent(ParivartanDocumentsUploadActivity.this, OcrActivity.class);
        startActivityForResult(intent, REQUEST_OCR);

        //onBrowse();
    }

    public void onPreview_ProposerPhoto(View v) {

        if (proposerBrowsedPhotoFile.exists()) {
            try {
                commonMethods.openAllDocs(context, proposerBrowsedPhotoFile);
            } catch (IOException ex) {
                commonMethods.showToast(context, ex.getMessage());
            }
        } else {
            commonMethods.showToast(context, "File does not exists");
        }
    }

    public void onPreview_LifeAssuredPhoto(View v) {

        if (lifeAssuredBrowsedPhotoFile.exists()) {
            try {
                commonMethods.openAllDocs(context, lifeAssuredBrowsedPhotoFile);
            } catch (IOException ex) {
                commonMethods.showToast(context, ex.getMessage());
            }
        } else {
            commonMethods.showToast(context, "File does not exists");
        }
    }

    public void onPreview_customerPhoto(View v) {

        if (photo.exists()) {
            try {
                commonMethods.openAllDocs(context, photo);
            } catch (IOException ex) {
                commonMethods.showToast(context, ex.getMessage());
            }
        } else {
            commonMethods.showToast(context, "File does not exists");
        }
    }

    public void onPreview_otpSelfDeclarationProof(View v) {

        if (OTPSelfDeclarationFileName.exists()) {
            try {
                commonMethods.openAllDocs(context, OTPSelfDeclarationFileName);
            } catch (IOException ex) {
                commonMethods.showToast(context, ex.getMessage());
            }
        } else {
            commonMethods.showToast(context, "File does not exists");
        }

    }

    public void onPreview_ageProof(View v) {

        if (ageProofFileName.exists()) {
            try {
                commonMethods.openAllDocs(context, ageProofFileName);
            } catch (IOException ex) {
                commonMethods.showToast(context, ex.getMessage());
            }
        } else {
            commonMethods.showToast(context, "File does not exists");
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

    public void onPreview_IncomeProof(View v) {

        if (incomeProofFileName.exists()) {
            try {
                commonMethods.openAllDocs(context, incomeProofFileName);
            } catch (IOException ex) {
                commonMethods.showToast(context, ex.getMessage());
            }
        } else {
            commonMethods.showToast(context, "File does not exists");
        }

    }

    public void onPreview_BankProof(View v) {

        if (bankProofFileName.exists()) {
            try {
                commonMethods.openAllDocs(context, bankProofFileName);
            } catch (IOException ex) {
                commonMethods.showToast(context, ex.getMessage());
            }
        } else {
            commonMethods.showToast(context, "File does not exists");
        }
    }

    public void onPreview_eftProof(View v) {

        if (EFTProofFileName.exists()) {
            try {
                commonMethods.openAllDocs(context, EFTProofFileName);
            } catch (IOException ex) {
                commonMethods.showToast(context, ex.getMessage());
            }
        } else {
            commonMethods.showToast(context, "File does not exists");
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

    public void onDelete_ProposerPhoto(View v) {
        deleteProposerPhoto = 1;
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

    public void onDelete_LifeAssuredPhoto(View v) {
        deleteLifeAssuredPhoto = 1;
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

    public void onDelete_CustomerPhoto(View v) {
        deleteCustomerPhoto = 1;
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

    public void onDelete_otpSelfDeclarationProof(View v) {
        deleteOTPSelfDeclarationDocument = 1;
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
        deleteeftDocument = 1;
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

    private void onBrowse() {

        Intent i = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, REQUEST_CODE_BROWSE_FILE);
    }

    /*private File galleryAddPic() {
        Intent mediaScanIntent = new Intent(
                "android.intent.action.MEDIA_SCANNER_SCAN_FILE");
        f = new File(mCurrentPhotoPath);
        String strPathOfFile = mCurrentPhotoPath;

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
            imageIntent.putExtra(MediaStore.EXTRA_OUTPUT, commonMethods.getContentUri(context, image));

            contentUri = commonMethods.getContentUri(context, f);
        } else {
            imageIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(image));

            contentUri = Uri.fromFile(f);
        }

        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);

        return f;
    }*/

    private void setFocusable(View v) {
        v.setFocusable(true);
        v.setFocusableInTouchMode(true);
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        String proposer_sign = "";
        Bitmap reducedCustomerPhotoBitmap;
        Bitmap reducedOtherBitmap;
        Bitmap reducedEFTBitmap;
        Bitmap reducedBankBitmap;
        Bitmap reducedIncomeBitmap;
        Bitmap reducedAddressBitmap;
        Bitmap reducedIdentityBitmap;
        Bitmap reducedAgeBitmap;
        Bitmap reducedOTPSelfDeclarationBitmap;

        if (requestCode == OFFLINE_EKYC_ACTIVITY) {
            if (resultCode == RESULT_OK) {
                //scrollViewKYC1.removeAllViews();
                try {
                    /*String resString = db.getKYCDetails(QuatationNumber);

                    resString = SimpleCrypto.decrypt("SBIL", resString);*/

                    final String resXMLRes = data.getStringExtra("KYC_XML") == null ? "" : data.getStringExtra("KYC_XML");

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

                    if (is1WProductEKYC) {
                        str_ekyc_code_DOB_1W_spouse = res_UidData.getPoi().getDob() == null ? ""
                                : res_UidData.getPoi().getDob();

                        boolean isValidDate = isThisDateValid(str_ekyc_code_DOB_1W_spouse);

                        if (isValidDate == false) {
                            str_ekyc_code_DOB_1W_spouse = "01-01-1990";
                        }

                        str_ekyc_code_Name_1W_spouse = res_UidData.getPoi().getName() == null ? ""
                                : res_UidData.getPoi().getName();
                        str_ekyc_code_Gender_1W_spouse = res_UidData.getPoi().getGender() == null ? ""
                                : res_UidData.getPoi().getGender();
                        str_QR_code_Photo_1W_spouse = res.getUidData().getPht() == null ? ""
                                : res.getUidData().getPht();

                    } else {
                        str_ekyc_code_DOB = res_UidData.getPoi().getDob() == null ? ""
                                : res_UidData.getPoi().getDob();

                        boolean isValidDate = isThisDateValid(str_ekyc_code_DOB);

                        if (isValidDate == false) {
                            str_ekyc_code_DOB = "01-01-1990";
                        }

                        str_ekyc_code_Name = res_UidData.getPoi().getName() == null ? ""
                                : res_UidData.getPoi().getName();
                        str_ekyc_code_Gender = res_UidData.getPoi().getGender() == null ? ""
                                : res_UidData.getPoi().getGender();
                        str_QR_code_Photo = res.getUidData().getPht() == null ? ""
                                : res.getUidData().getPht();
                    }

                    if (!(str_Address_title.equals(""))) {
                        str_Address_title = str_Address_title.substring(0, 3);
                    } else {
                        str_Address_title = "C/O";
                    }

                    String str_Address1 = "", str_Address2 = "", str_Address3 = "";

                    String[] lines = Split((str_aad_Address1 + " "
                                    + str_aad_Address2 + " " + str_aad_Address3), 45,
                            135);
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

                    if (is1WProductEKYC) {
                        str_ekyc_code_address_1W_spouse = str_Address_title + str_Address1
                                + str_Address2 + str_Address3 + str_City + str_PinCode
                                + str_State;
                    } else {
                        str_ekyc_code_address = str_Address_title + str_Address1
                                + str_Address2 + str_Address3 + str_City + str_PinCode
                                + str_State;
                    }

                    //String strHTML = toOfflineeKYCAadharCardhtml();
                    if (is1WProductEKYC) {

                        File aadhaarFileUpload = CreateAdhharPDF();

                        if (aadhaarFileUpload != null) {

                            Check = "OFFLINE_EKYC";

                            uploaddoc(aadhaarFileUpload);

                        } else {
                            commonMethods.showMessageDialog(context, "Error While Creating Aadhaar pdf !!");
                        }

                    } else {
                        Builder alertDialogBuilder = new Builder(context);
                        alertDialogBuilder.setMessage("URN is Smart Humsafar Product?");
                        alertDialogBuilder.setPositiveButton("Yes",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {

                                        /***** XML Parsing Start ******/
                                        dialog.dismiss();
                                        if (!resXMLRes.equals("")) {

                                            is1WProductEKYC = true;
                                            str1WFirstEKYC = offlineEkycData();

                                            commonMethods.showMessageDialog(context, "Please provide spouse ekyc");
                                        } else {
                                            Toast.makeText(ParivartanDocumentsUploadActivity.this, "Invalid ekyc data!", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });

                        alertDialogBuilder.setNegativeButton("No",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {

                                        is1WProductEKYC = false;
                                        dialog.dismiss();

                                        /***** XML Parsing Start ******/
                                        if (resXMLRes != null) {

                                            File aadhaarFileUpload = CreateAdhharPDF();

                                            if (aadhaarFileUpload != null) {

                                                Check = "OFFLINE_EKYC";
                                                uploaddoc(aadhaarFileUpload);

                                            } else {
                                                commonMethods.showMessageDialog(context, "Error While Creating Aadhaar pdf !!");
                                            }

                                        } else {
                                            Toast.makeText(ParivartanDocumentsUploadActivity.this, "Invalid QR Code!", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });

                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();
                    }

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
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

                    //if (DocumentType.equals("AADHAAR CARD") || DocumentType.equals("BACK AADHAAR CARD") || DocumentType.equalsIgnoreCase("FRONT BACK AADHAAR")) {

                    if (Check.equals("LifeAssuredPhoto")) {

                        //image compression by bhalla
                        CompressImage.compressImage(imagePath.getAbsolutePath());

                        lifeAssuredBrowsedPhotoFile = mStorageUtils.saveFileToAppSpecificDir(context, StorageUtils.DIRECT_DIRECTORY,
                                str_selected_urn_no + "_cust2Photo" + JPEG_FILE_SUFFIX, imagePath);
                        mCurrentPhotoPath = lifeAssuredBrowsedPhotoFile.getAbsolutePath();

                        /*img_btn_document_upload_preview_image_eft
                                .setVisibility(View.VISIBLE);*/
                        img_btn_document_delete_life_assured_photo.setVisibility(View.VISIBLE);


                        if (isBrowseCapture == REQUEST_CODE_BROWSE_FILE) {
                            img_btn_document_upload_click_image_life_assured_photo
                                    .setImageDrawable(getResources().getDrawable(
                                            R.drawable.ibtn_camera));

                            img_btn_document_upload_click_browse_life_assured_photo.setImageDrawable(getResources().getDrawable(
                                    R.drawable.checkedbrowse));

                        } else {
                            img_btn_document_upload_click_image_life_assured_photo.setImageDrawable(
                                    getResources().getDrawable(R.drawable.checkedcamera));

                            img_btn_document_upload_click_browse_life_assured_photo.setImageDrawable(getResources().getDrawable(
                                    R.drawable.ibtn_browsedoc));
                        }

                    }
                    else if (Check.equals("ProposerPhoto")) {

                        //image compression by bhalla
                        CompressImage.compressImage(imagePath.getAbsolutePath());
                        proposerBrowsedPhotoFile = mStorageUtils.saveFileToAppSpecificDir(context, StorageUtils.DIRECT_DIRECTORY,
                                str_selected_urn_no + "_cust1Photo" + JPEG_FILE_SUFFIX, imagePath);
                        mCurrentPhotoPath = proposerBrowsedPhotoFile.getAbsolutePath();

                        /*img_btn_document_upload_preview_image_eft
                                .setVisibility(View.VISIBLE);*/
                        img_btn_document_delete_proposer_photo.setVisibility(View.VISIBLE);


                        if (isBrowseCapture == REQUEST_CODE_BROWSE_FILE) {
                            img_btn_document_upload_click_image_proposer_photo
                                    .setImageDrawable(getResources().getDrawable(
                                            R.drawable.ibtn_camera));

                            img_btn_document_upload_click_browse_proposer_photo.setImageDrawable(getResources().getDrawable(
                                    R.drawable.checkedbrowse));

                        } else {
                            img_btn_document_upload_click_image_proposer_photo.setImageDrawable(
                                    getResources().getDrawable(R.drawable.checkedcamera));

                            img_btn_document_upload_click_browse_proposer_photo.setImageDrawable(getResources().getDrawable(
                                    R.drawable.ibtn_browsedoc));
                        }

                    }
                    else if (Check.equals("EFTProof")) {
                        increment = 7;
                        //image compression by bhalla
                        CompressImage.compressImage(imagePath.getAbsolutePath());

                        EFTProofFileName = mStorageUtils.saveFileToAppSpecificDir(context, StorageUtils.DIRECT_DIRECTORY,
                                JPEG_FILE_PREFIX + str_selected_urn_no + "_X" + increment + "_" + JPEG_FILE_SUFFIX,
                                imagePath);
                        mCurrentPhotoPath = EFTProofFileName.getAbsolutePath();

                        img_btn_document_upload_preview_image_eft
                                .setVisibility(View.VISIBLE);
                        img_btn_document_delete_eft.setVisibility(View.VISIBLE);


                        if (isBrowseCapture == REQUEST_CODE_BROWSE_FILE) {
                            img_btn_document_upload_click_image_eft
                                    .setImageDrawable(getResources().getDrawable(
                                            R.drawable.ibtn_camera));

                            img_btn_document_upload_click_browse_eft.setImageDrawable(getResources().getDrawable(
                                    R.drawable.checkedbrowse));

                        } else {
                            img_btn_document_upload_click_image_eft.setImageDrawable(
                                    getResources().getDrawable(R.drawable.checkedcamera));

                            img_btn_document_upload_click_browse_eft.setImageDrawable(getResources().getDrawable(
                                    R.drawable.ibtn_browsedoc));
                        }


                    } else if (Check.equals("OTPSelfDeclaration")) {

                        increment = 25;
                        //image compression by bhalla
                        CompressImage.compressImage(imagePath.getAbsolutePath());
                        otherProofFileName = mStorageUtils.saveFileToAppSpecificDir(context, StorageUtils.DIRECT_DIRECTORY,
                                JPEG_FILE_PREFIX + str_selected_urn_no + "_X" + increment + "_" + JPEG_FILE_SUFFIX,
                                imagePath);
                        mCurrentPhotoPath = otherProofFileName.getAbsolutePath();

                        img_btn_document_upload_preview_image_otp_self_declaration
                                .setVisibility(View.VISIBLE);
                        img_btn_document_delete_otp_self_declaration.setVisibility(View.VISIBLE);


                        if (isBrowseCapture == REQUEST_CODE_BROWSE_FILE) {
                            img_btn_document_upload_click_image_otp_self_declaration
                                    .setImageDrawable(getResources().getDrawable(
                                            R.drawable.ibtn_camera));

                            img_btn_document_upload_click_browse_otp_self_declaration.setImageDrawable(getResources().getDrawable(
                                    R.drawable.checkedbrowse));

                        } else {
                            img_btn_document_upload_click_image_otp_self_declaration.setImageDrawable(
                                    getResources().getDrawable(R.drawable.checkedcamera));

                            img_btn_document_upload_click_browse_otp_self_declaration.setImageDrawable(getResources().getDrawable(
                                    R.drawable.ibtn_browsedoc));
                        }


                    } else if (Check.equals("AgeProof")) {

                        increment = 1;
                        //image compression by bhalla
                        CompressImage.compressImage(imagePath.getAbsolutePath());
                        ageProofFileName = mStorageUtils.saveFileToAppSpecificDir(context, StorageUtils.DIRECT_DIRECTORY,
                                JPEG_FILE_PREFIX + str_selected_urn_no + "_X" + increment + "_" + JPEG_FILE_SUFFIX,
                                imagePath);
                        mCurrentPhotoPath = ageProofFileName.getAbsolutePath();

                        boolean is_Doc_Detected = true;
                        if (spnr_document_upload_document_age.getSelectedItem().toString().toLowerCase().contains("aadhar card") ||
                                spnr_document_upload_document_age.getSelectedItem().toString().toLowerCase().contains("pancard") ||
                                spnr_document_upload_document_age.getSelectedItem().toString().toLowerCase().contains("passport") ||
                                spnr_document_upload_document_age.getSelectedItem().toString().toLowerCase().contains("voter s identity card") ||
                                spnr_document_upload_document_age.getSelectedItem().toString().toLowerCase().contains("driving license")) {
                            if (spnr_document_upload_document_age.getSelectedItem().toString().toLowerCase().contains("aadhar") &&
                                    (DocumentType.equalsIgnoreCase("AADHAAR CARD") || DocumentType.equalsIgnoreCase("BACK AADHAAR CARD") || DocumentType.equalsIgnoreCase("FRONT BACK AADHAAR"))) {
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

                            img_btn_document_upload_preview_image_age
                                    .setVisibility(View.VISIBLE);
                            img_btn_document_delete_age.setVisibility(View.VISIBLE);


                            if (isBrowseCapture == REQUEST_CODE_BROWSE_FILE) {
                                img_btn_document_upload_click_image_age
                                        .setImageDrawable(getResources().getDrawable(
                                                R.drawable.ibtn_camera));

                                img_btn_document_upload_click_browse_age.setImageDrawable(getResources().getDrawable(
                                        R.drawable.checkedbrowse));

                            } else {
                                img_btn_document_upload_click_image_age.setImageDrawable(
                                        getResources().getDrawable(R.drawable.checkedcamera));

                                img_btn_document_upload_click_browse_age.setImageDrawable(getResources().getDrawable(
                                        R.drawable.ibtn_browsedoc));
                            }

                        } else {
                            img_btn_document_upload_preview_image_age
                                    .setVisibility(View.INVISIBLE);
                            img_btn_document_delete_age
                                    .setVisibility(View.INVISIBLE);

                            img_btn_document_upload_click_image_age
                                    .setImageDrawable(getResources().getDrawable(
                                            R.drawable.ibtn_camera));

                            img_btn_document_upload_click_browse_age.setImageDrawable(getResources().getDrawable(
                                    R.drawable.ibtn_browsedoc));

                            if (ageProofFileName.exists())
                                ageProofFileName.delete();

                            Toast.makeText(this, "Document is not properly detected,Kindly capture the actual document. ", Toast.LENGTH_LONG)
                                    .show();
                        }

                    } else if (Check.equals("IdentityProof")) {
                        increment = 2;
                        //image compression by bhalla
                        CompressImage.compressImage(imagePath.getAbsolutePath());
                        identityProofFileName = mStorageUtils.saveFileToAppSpecificDir(context, StorageUtils.DIRECT_DIRECTORY,
                                JPEG_FILE_PREFIX + str_selected_urn_no + "_X" + increment + "_" + JPEG_FILE_SUFFIX,
                                imagePath);
                        mCurrentPhotoPath = identityProofFileName.getAbsolutePath();

                        // boolean isBlurr = Blurr(identityProofBitmap);
                        boolean is_Doc_Detected = true;
                        if (spnr_document_upload_document_identity.getSelectedItem().toString().toLowerCase().contains("aadhar card")
                                || spnr_document_upload_document_identity.getSelectedItem().toString().toLowerCase().contains("pancard")
                                || spnr_document_upload_document_identity.getSelectedItem().toString().toLowerCase().contains("passport")
                                || spnr_document_upload_document_identity.getSelectedItem().toString().toLowerCase().contains("voter s identity card")
                                || spnr_document_upload_document_identity.getSelectedItem().toString().toLowerCase().contains("driving license")) {
                            if (spnr_document_upload_document_identity.getSelectedItem().toString().toLowerCase().contains("aadhar") && (spnr_document_upload_document_identity.getSelectedItem().toString().toLowerCase().contains("AADHAAR CARD")
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

                        if (is_Doc_Detected) {

                            img_btn_document_upload_preview_image_identity
                                    .setVisibility(View.VISIBLE);
                            img_btn_document_delete_identity
                                    .setVisibility(View.VISIBLE);

                            if (isBrowseCapture == REQUEST_CODE_BROWSE_FILE) {
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

                            if (identityProofFileName.exists())
                                identityProofFileName.delete();

                            Toast.makeText(this, "Document is not properly detected,Kindly capture the actual document. ", Toast.LENGTH_LONG)
                                    .show();

                        }

                    } else if (Check.equals("AddressProof")) {

                        increment = 3;
                        //image compression by bhalla
                        CompressImage.compressImage(imagePath.getAbsolutePath());
                        addressProofFileName = mStorageUtils.saveFileToAppSpecificDir(context, StorageUtils.DIRECT_DIRECTORY,
                                JPEG_FILE_PREFIX + str_selected_urn_no + "_X" + increment + "_" + JPEG_FILE_SUFFIX,
                                imagePath);
                        mCurrentPhotoPath = addressProofFileName.getAbsolutePath();

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

                        if (is_Doc_Detected == true) {

                            img_btn_document_upload_click_preview_image_address
                                    .setVisibility(View.VISIBLE);
                            img_btn_document_delete_address
                                    .setVisibility(View.VISIBLE);

                            if (isBrowseCapture == REQUEST_CODE_BROWSE_FILE) {
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
                            }
                            Toast.makeText(this, "Document is not properly detected,Kindly capture the actual document. ", Toast.LENGTH_LONG)
                                    .show();

                        }

                    } else if (Check.equals("BankProof")) {
                        increment = 6;
                        //image compression by bhalla
                        CompressImage.compressImage(imagePath.getAbsolutePath());
                        bankProofFileName = mStorageUtils.saveFileToAppSpecificDir(context, StorageUtils.DIRECT_DIRECTORY,
                                JPEG_FILE_PREFIX + str_selected_urn_no + "_X" + increment + "_" + JPEG_FILE_SUFFIX,
                                imagePath);
                        mCurrentPhotoPath = bankProofFileName.getAbsolutePath();

                        //boolean isBlurr = trueBlurr(BankProofBitmap);
                        boolean is_Doc_Detected = true;

                        if (spnr_document_upload_document_bank
                                .getSelectedItem().toString().contains("Cancelled Cheque")) {
                            if (DocumentType.equalsIgnoreCase("CHEQUE") ||
                                    (DocumentType.equals("AADHAAR CARD") || DocumentType.equals("BACK AADHAAR CARD") || DocumentType.equalsIgnoreCase("FRONT BACK AADHAAR"))) {
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

                            if (isBrowseCapture == REQUEST_CODE_BROWSE_FILE) {
                                img_btn_document_upload_click_image_bank
                                        .setImageDrawable(getResources().getDrawable(
                                                R.drawable.ibtn_camera));

                                img_btn_document_upload_click_browse_bank.setImageDrawable(getResources().getDrawable(
                                        R.drawable.checkedbrowse));

                            } else {
                                img_btn_document_upload_click_image_bank.setImageDrawable(
                                        getResources().getDrawable(R.drawable.checkedcamera));

                                img_btn_document_upload_click_browse_bank.setImageDrawable(getResources().getDrawable(
                                        R.drawable.ibtn_browsedoc));
                            }

                        } else {
                            img_btn_document_upload_click_preview_image_bank
                                    .setVisibility(View.INVISIBLE);
                            img_btn_document_delete_bank
                                    .setVisibility(View.INVISIBLE);
                            img_btn_document_upload_click_image_bank
                                    .setImageDrawable(getResources().getDrawable(
                                            R.drawable.ibtn_camera));
                            img_btn_document_upload_click_browse_bank.setImageDrawable(getResources().getDrawable(
                                    R.drawable.ibtn_browsedoc));

                            if (bankProofFileName.exists()) {
                                bankProofFileName.delete();
                            }
                            Toast.makeText(this, "Document is not properly detected,Kindly capture the actual document. ", Toast.LENGTH_LONG)
                                    .show();

                        }
                    } else if (Check.equalsIgnoreCase("IncomeProof")) {
                        increment = 4;

                        //image compression by bhalla
                        CompressImage.compressImage(imagePath.getAbsolutePath());
                        incomeProofFileName = mStorageUtils.saveFileToAppSpecificDir(context, StorageUtils.DIRECT_DIRECTORY,
                                JPEG_FILE_PREFIX + str_selected_urn_no + "_X" + increment + "_" + JPEG_FILE_SUFFIX,
                                imagePath);
                        mCurrentPhotoPath = incomeProofFileName.getAbsolutePath();

                        img_btn_document_upload_click_preview_image_income
                                .setVisibility(View.VISIBLE);
                        img_btn_document_delete_income
                                .setVisibility(View.VISIBLE);

                        if (isBrowseCapture == REQUEST_CODE_BROWSE_FILE) {
                            img_btn_document_upload_click_image_income
                                    .setImageDrawable(getResources().getDrawable(
                                            R.drawable.ibtn_camera));

                            img_btn_document_upload_click_browse_income.setImageDrawable(getResources().getDrawable(
                                    R.drawable.checkedbrowse));

                        } else {
                            img_btn_document_upload_click_image_income.setImageDrawable(
                                    getResources().getDrawable(R.drawable.checkedcamera));

                            img_btn_document_upload_click_browse_income.setImageDrawable(getResources().getDrawable(
                                    R.drawable.ibtn_browsedoc));
                        }


                    } else if (Check.equals("OtherProof")) {

                        increment = 5;
                        //image compression by bhalla
                        CompressImage.compressImage(imagePath.getAbsolutePath());
                        otherProofFileName = mStorageUtils.saveFileToAppSpecificDir(context, StorageUtils.DIRECT_DIRECTORY,
                                JPEG_FILE_PREFIX + str_selected_urn_no + "_X" + increment + "_" + JPEG_FILE_SUFFIX,
                                imagePath);
                        mCurrentPhotoPath = otherProofFileName.getAbsolutePath();

                        img_btn_document_upload_click_preview_image_others
                                .setVisibility(View.VISIBLE);
                        img_btn_document_delete_Others
                                .setVisibility(View.VISIBLE);

                        if (isBrowseCapture == REQUEST_CODE_BROWSE_FILE) {
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
                    } else if (Check.equalsIgnoreCase("IncomeProof")) {
                        increment = 4;
                        //image compression by bhalla
                        CompressImage.compressImage(imagePath.getAbsolutePath());
                        incomeProofFileName = mStorageUtils.saveFileToAppSpecificDir(context, StorageUtils.DIRECT_DIRECTORY,
                                JPEG_FILE_PREFIX + str_selected_urn_no + "_X" + increment + "_" + JPEG_FILE_SUFFIX,
                                imagePath);
                        mCurrentPhotoPath = incomeProofFileName.getAbsolutePath();

                        img_btn_document_upload_click_preview_image_income
                                .setVisibility(View.VISIBLE);
                        img_btn_document_delete_income
                                .setVisibility(View.VISIBLE);

                        if (isBrowseCapture == REQUEST_CODE_BROWSE_FILE) {
                            img_btn_document_upload_click_image_income
                                    .setImageDrawable(getResources().getDrawable(
                                            R.drawable.ibtn_camera));

                            img_btn_document_upload_click_browse_income.setImageDrawable(getResources().getDrawable(
                                    R.drawable.checkedbrowse));

                        } else {
                            img_btn_document_upload_click_image_income.setImageDrawable(
                                    getResources().getDrawable(R.drawable.checkedcamera));

                            img_btn_document_upload_click_browse_income.setImageDrawable(getResources().getDrawable(
                                    R.drawable.ibtn_browsedoc));
                        }
                    }

                    if (imagePath.exists()) {
                        imagePath.delete();
                    }
            } else {
                Toast.makeText(context, "Data not receive", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == commonMethods.SIGNATURE_ACTIVITY) {
            if (resultCode == RESULT_OK) {

                Bundle bundle = data.getExtras();
                String status = bundle.getString("status");
                if (status != null && status.equalsIgnoreCase("done")) {
                    Toast toast = Toast
                            .makeText(this, "Signature capture successful!",
                                    Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP, 105, 50);
                    toast.show();
                    if (latestImage.equalsIgnoreCase("proposer")) {

                        Ibtn_signatureofPolicyHolders
                                .setImageBitmap(ProposerCaptureSignature.scaled);
                        Bitmap signature = ProposerCaptureSignature.scaled;

                        if (signature != null) {
                            ByteArrayOutputStream out = new ByteArrayOutputStream();
                            signature.compress(Bitmap.CompressFormat.PNG, 100,
                                    out);
                            byte[] signByteArray = out.toByteArray();
                            proposer_sign = Base64.encodeToString(
                                    signByteArray, Base64.DEFAULT);
                        }

                    } else if (latestImage.equalsIgnoreCase("lifeAssuredSign")) {
                        Ibtn_signatureofLifeAssured
                                .setImageBitmap(ProposerCaptureSignature.scaled);

                        imageButtonParivartanDocumentsUploadLifeAssuredSignBrowse
                                .setImageDrawable(getResources().getDrawable(
                                        R.drawable.ibtn_browsedoc));

                        Bitmap signature = ProposerCaptureSignature.scaled;
                        if (signature != null) {
                            ByteArrayOutputStream out = new ByteArrayOutputStream();
                            signature.compress(Bitmap.CompressFormat.PNG, 100,
                                    out);
                            byte[] signByteArray = out.toByteArray();
                            lifeAssured_sign = Base64.encodeToString(
                                    signByteArray, Base64.DEFAULT);
                        }
                    } else if (latestImage.equalsIgnoreCase("thirdParty")) {
                        Ibtn_signatureofThirdParty
                                .setImageBitmap(ProposerCaptureSignature.scaled);
                        imageButtonParivartanDocumentsUploadThirdPartySignBrowse
                                .setImageDrawable(getResources().getDrawable(
                                        R.drawable.ibtn_browsedoc));

                        Bitmap signature = ProposerCaptureSignature.scaled;
                        if (signature != null) {
                            ByteArrayOutputStream out = new ByteArrayOutputStream();
                            signature.compress(Bitmap.CompressFormat.PNG, 100,
                                    out);
                            byte[] signByteArray = out.toByteArray();
                            thirdPartySign = Base64
                                    .encodeToString(signByteArray,
                                            Base64.DEFAULT);
                        }
                    } else if (latestImage.equalsIgnoreCase("Appointee")) {
                        Ibtn_signatureofAppointee
                                .setImageBitmap(ProposerCaptureSignature.scaled);

                        imageButtonParivartanDocumentsUploadAppointeeSignBrowse
                                .setImageDrawable(getResources().getDrawable(
                                        R.drawable.ibtn_browsedoc));

                        Bitmap signature = ProposerCaptureSignature.scaled;
                        if (signature != null) {
                            ByteArrayOutputStream out = new ByteArrayOutputStream();
                            signature.compress(Bitmap.CompressFormat.PNG, 100,
                                    out);
                            byte[] signByteArray = out.toByteArray();
                            appointeeSign = Base64.encodeToString(
                                    signByteArray, Base64.DEFAULT);
                        }

                    }

                }
            }
        } else if (requestCode == REQUEST_CODE_BROWSE_FILE) {
            if (resultCode == AppCompatActivity.RESULT_OK) {

                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaColumns.DATA};

                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                assert cursor != null;
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String strPathOfFile = cursor.getString(columnIndex);
                cursor.close();

                if (Check.equals("ProposerPhoto")) {

                    proposerBrowsedPhotoFile = new File(strPathOfFile);

                    //image compression by bhalla
                    /*photoBitmap = BitmapFactory.decodeFile(CompressImage.compressImageGetBitmap(proposerBrowsedPhotoFile.getAbsolutePath()));*/
                    CompressImage.compressImage(proposerBrowsedPhotoFile.getAbsolutePath());

                    /*photoBitmap = commonMethods.ShrinkBitmap(Photo.getAbsolutePath(), 600, 600);*/

                    img_btn_document_upload_preview_proposer_photo
                            .setVisibility(View.VISIBLE);
                    img_btn_document_delete_proposer_photo
                            .setVisibility(View.VISIBLE);
                    img_btn_document_upload_click_browse_proposer_photo
                            .setImageDrawable(getResources().getDrawable(
                                    R.drawable.checkedbrowse));

                    /*boolean isBlurr = false*//* Blurr(incomeProofBitmap) *//*;
                    if (!isBlurr) {

                        ReducedProposerPhotoBitmap = photoBitmap;

                        img_btn_document_upload_preview_proposer_photo
                                .setVisibility(View.VISIBLE);
                        img_btn_document_delete_proposer_photo
                                .setVisibility(View.VISIBLE);
                        img_btn_document_upload_click_browse_proposer_photo
                                .setImageDrawable(getResources().getDrawable(
                                        R.drawable.checkedbrowse));

                        String extStorageDirectory = Environment
                                .getExternalStorageDirectory().toString();
                        String direct = "/SBI-Smart Advisor";
                        File folder = new File(extStorageDirectory + direct
                                + "/");

                        if (!folder.exists()) {
                            folder.mkdirs();
                        }

                        Photo = new File(folder, str_selected_urn_no
                                + "_cust1Photo.jpg");

                        try {
                            FileOutputStream fileOutputStream = new FileOutputStream(
                                    Photo);
                            photoBitmap.compress(Bitmap.CompressFormat.JPEG,
                                    100, fileOutputStream);
                            fileOutputStream.flush();
                            fileOutputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    } else {
                        img_btn_document_upload_preview_proposer_photo
                                .setVisibility(View.INVISIBLE);
                        img_btn_document_delete_proposer_photo
                                .setVisibility(View.INVISIBLE);

                        img_btn_document_upload_click_browse_proposer_photo
                                .setImageDrawable(getResources().getDrawable(
                                        R.drawable.ibtn_browsedoc));
                    }*/

                } else if (Check.equals("LifeAssuredPhoto")) {

                    lifeAssuredBrowsedPhotoFile = new File(strPathOfFile);

                    //image compression by bhalla
                    /*lifeAssuredBitmap = BitmapFactory.decodeFile(CompressImage.compressImageGetBitmap(lifeAssuredBrowsedPhotoFile.getAbsolutePath()));*/
                    CompressImage.compressImage(lifeAssuredBrowsedPhotoFile.getAbsolutePath());

                    /*lifeAssuredBitmap = commonMethods.ShrinkBitmap(Photo.getAbsolutePath(), 600, 600);*/

                    img_btn_document_upload_preview_life_assured_photo
                            .setVisibility(View.VISIBLE);
                    img_btn_document_delete_life_assured_photo
                            .setVisibility(View.VISIBLE);
                    img_btn_document_upload_click_browse_life_assured_photo
                            .setImageDrawable(getResources().getDrawable(
                                    R.drawable.checkedbrowse));

                    /*boolean isBlurr = false*//* Blurr(incomeProofBitmap) *//*;
                    if (!isBlurr) {

                        ReducedLifeAssuredPhotoBitmap = lifeAssuredBitmap;

                        img_btn_document_upload_preview_life_assured_photo
                                .setVisibility(View.VISIBLE);
                        img_btn_document_delete_life_assured_photo
                                .setVisibility(View.VISIBLE);
                        img_btn_document_upload_click_browse_life_assured_photo
                                .setImageDrawable(getResources().getDrawable(
                                        R.drawable.checkedbrowse));

                        String extStorageDirectory = Environment
                                .getExternalStorageDirectory().toString();
                        String direct = "/SBI-Smart Advisor";
                        File folder = new File(extStorageDirectory + direct
                                + "/");

                        if (!folder.exists()) {
                            folder.mkdirs();
                        }

                        Photo = new File(folder, str_selected_urn_no
                                + "_cust2Photo.jpg");

                        try {
                            FileOutputStream fileOutputStream = new FileOutputStream(
                                    Photo);
                            lifeAssuredBitmap.compress(
                                    Bitmap.CompressFormat.JPEG, 100,
                                    fileOutputStream);
                            fileOutputStream.flush();
                            fileOutputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    } else {
                        img_btn_document_upload_preview_life_assured_photo
                                .setVisibility(View.INVISIBLE);
                        img_btn_document_delete_life_assured_photo
                                .setVisibility(View.INVISIBLE);

                        img_btn_document_upload_click_browse_life_assured_photo
                                .setImageDrawable(getResources().getDrawable(
                                        R.drawable.ibtn_browsedoc));
                    }*/

                } else if (latestImage.equals("ProposerSignBrowse")) {

                    File proposerBrowsedSignFile = new File(strPathOfFile);

                    Ibtn_signatureofPolicyHolders.setVisibility(View.VISIBLE);

                    Bitmap signature = BitmapFactory
                            .decodeFile(proposerBrowsedSignFile
                                    .getAbsolutePath());

                    if (signature != null) {
                        Bitmap scaled = Bitmap.createScaledBitmap(signature,
                                300, 80, true);
                        ByteArrayOutputStream out = new ByteArrayOutputStream();
                        signature.compress(Bitmap.CompressFormat.PNG, 100, out);
                        byte[] signByteArray = out.toByteArray();
                        proposer_sign = Base64.encodeToString(signByteArray,
                                Base64.DEFAULT);

                        Ibtn_signatureofPolicyHolders.setImageBitmap(scaled);
                    } else {
                        proposer_sign = null;
                    }
                } else if (latestImage.equals("lifeAssuredSignBrowse")) {

                    lifeAssuredBrowsedSignFile = new File(strPathOfFile);

                    Ibtn_signatureofLifeAssured.setVisibility(View.VISIBLE);
                    imageButtonParivartanDocumentsUploadLifeAssuredSignBrowse
                            .setImageDrawable(getResources().getDrawable(
                                    R.drawable.checkedbrowse));

                    Bitmap signature = BitmapFactory
                            .decodeFile(lifeAssuredBrowsedSignFile
                                    .getAbsolutePath());

                    if (signature != null) {
                        Bitmap scaled = Bitmap.createScaledBitmap(signature,
                                300, 80, true);
                        ByteArrayOutputStream out = new ByteArrayOutputStream();
                        signature.compress(Bitmap.CompressFormat.PNG, 100, out);
                        byte[] signByteArray = out.toByteArray();
                        lifeAssured_sign = Base64.encodeToString(signByteArray,
                                Base64.DEFAULT);

                        Ibtn_signatureofLifeAssured.setImageBitmap(scaled);
                    } else {
                        lifeAssured_sign = null;
                    }
                } else if (latestImage.equals("thirdPartyBrowsedFile")) {

                    thirdPartyBrowsedSignFile = new File(strPathOfFile);

                    Ibtn_signatureofThirdParty.setVisibility(View.VISIBLE);
                    imageButtonParivartanDocumentsUploadThirdPartySignBrowse
                            .setImageDrawable(getResources().getDrawable(
                                    R.drawable.checkedbrowse));

                    Bitmap signature = BitmapFactory
                            .decodeFile(thirdPartyBrowsedSignFile
                                    .getAbsolutePath());

                    if (signature != null) {
                        Bitmap scaled = Bitmap.createScaledBitmap(signature,
                                300, 80, true);
                        ByteArrayOutputStream out = new ByteArrayOutputStream();
                        signature.compress(Bitmap.CompressFormat.PNG, 100, out);
                        byte[] signByteArray = out.toByteArray();
                        thirdPartySign = Base64.encodeToString(signByteArray,
                                Base64.DEFAULT);

                        Ibtn_signatureofThirdParty.setImageBitmap(scaled);
                    } else {
                        thirdPartySign = null;
                    }

                } else if (latestImage.equals("AppointeeBrowsedFile")) {

                    AppointeeBrowsedSignFile = new File(strPathOfFile);

                    Ibtn_signatureofAppointee.setVisibility(View.VISIBLE);
                    imageButtonParivartanDocumentsUploadAppointeeSignBrowse
                            .setImageDrawable(getResources().getDrawable(
                                    R.drawable.checkedbrowse));

                    Bitmap signature = BitmapFactory
                            .decodeFile(AppointeeBrowsedSignFile
                                    .getAbsolutePath());

                    if (signature != null) {
                        Bitmap scaled = Bitmap.createScaledBitmap(signature,
                                300, 80, true);
                        ByteArrayOutputStream out = new ByteArrayOutputStream();
                        signature.compress(Bitmap.CompressFormat.PNG, 100, out);
                        byte[] signByteArray = out.toByteArray();
                        appointeeSign = Base64.encodeToString(signByteArray,
                                Base64.DEFAULT);

                        Ibtn_signatureofAppointee.setImageBitmap(scaled);
                    } else {
                        appointeeSign = null;
                    }
                } else if (Check.equals("BankProof")) {
                    bankProofFileName = new File(strPathOfFile);

                    //image compression by bhalla
                    /*BankProofBitmap = BitmapFactory.decodeFile(CompressImage.compressImageGetBitmap(bankProofFileName.getAbsolutePath()));*/
                    CompressImage.compressImage(bankProofFileName.getAbsolutePath());

                    /*BankProofBitmap = commonMethods.ShrinkBitmap(bankProofFileName.getAbsolutePath(), 600, 600);*/

                    img_btn_document_upload_click_preview_image_bank
                            .setVisibility(View.VISIBLE);
                    img_btn_document_delete_bank
                            .setVisibility(View.VISIBLE);

                    img_btn_document_upload_click_browse_bank
                            .setImageDrawable(getResources().getDrawable(
                                    R.drawable.checkedbrowse));

                    /*boolean isBlurr = false*//* Blurr(BankProofBitmap) *//*;

                    if (!isBlurr) {

                        reducedBankBitmap = BankProofBitmap;

                        img_btn_document_upload_click_preview_image_bank
                                .setVisibility(View.VISIBLE);
                        img_btn_document_delete_bank
                                .setVisibility(View.VISIBLE);

                        img_btn_document_upload_click_browse_bank
                                .setImageDrawable(getResources().getDrawable(
                                        R.drawable.checkedbrowse));

                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        reducedBankBitmap.compress(Bitmap.CompressFormat.PNG,
                                70, baos);
                        byte[] arr = baos.toByteArray();
                        String result = Base64.encodeToString(arr,
                                Base64.DEFAULT);
                        SelfAttestedDocumentActivity.lst_BankBitmap.add(result);

                        showDialog(DIALOG_ALERT);

                    } else {
                        img_btn_document_upload_click_preview_image_bank
                                .setVisibility(View.INVISIBLE);
                        img_btn_document_delete_bank
                                .setVisibility(View.INVISIBLE);

                        img_btn_document_upload_click_browse_bank
                                .setImageDrawable(getResources().getDrawable(
                                        R.drawable.ibtn_browsedoc));
                    }*/

                } else if (Check.equals("OtherProof")) {
                    otherProofFileName = new File(strPathOfFile);

                    //image compression by bhalla
                    /*otherProofBitmap = BitmapFactory.decodeFile(CompressImage.compressImageGetBitmap(otherProofFileName.getAbsolutePath()));*/
                    CompressImage.compressImage(otherProofFileName.getAbsolutePath());

                    /*otherProofBitmap = commonMethods.ShrinkBitmap(
                            otherProofFileName.getAbsolutePath(), 600, 600);*/

                    img_btn_document_upload_click_preview_image_others
                            .setVisibility(View.VISIBLE);
                    img_btn_document_delete_Others
                            .setVisibility(View.VISIBLE);
                    img_btn_document_upload_click_browse_Others
                            .setImageDrawable(getResources().getDrawable(
                                    R.drawable.checkedbrowse));

                    /*boolean isBlurr = false;*//* falseBlurr(otherProofBitmap); *//*

                    if (!isBlurr) {

                        reducedOtherBitmap = otherProofBitmap;

                        img_btn_document_upload_click_preview_image_others
                                .setVisibility(View.VISIBLE);
                        img_btn_document_delete_Others
                                .setVisibility(View.VISIBLE);
                        img_btn_document_upload_click_browse_Others
                                .setImageDrawable(getResources().getDrawable(
                                        R.drawable.checkedbrowse));
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        reducedOtherBitmap.compress(Bitmap.CompressFormat.PNG,
                                70, baos);
                        byte[] arr = baos.toByteArray();
                        String result = Base64.encodeToString(arr,
                                Base64.DEFAULT);
                        SelfAttestedDocumentActivity.lst_OtherBitmap
                                .add(result);

                        showDialog(DIALOG_ALERT);

                    } else {
                        img_btn_document_upload_click_preview_image_others
                                .setVisibility(View.INVISIBLE);
                        img_btn_document_delete_Others
                                .setVisibility(View.INVISIBLE);

                        img_btn_document_upload_click_image_others
                                .setImageDrawable(getResources().getDrawable(
                                        R.drawable.ibtn_browsedoc));

                    }*/

                } else if (Check.equals("EFTProof")) {
                    EFTProofFileName = new File(strPathOfFile);

                    /*EFTProofBitmap = commonMethods.ShrinkBitmap(
                            EFTProofFileName.getAbsolutePath(), 600, 600);*/

                    //image compression by bhalla
                    /*EFTProofBitmap = BitmapFactory.decodeFile(CompressImage.compressImageGetBitmap(EFTProofFileName.getAbsolutePath()));*/
                    CompressImage.compressImage(EFTProofFileName.getAbsolutePath());

                    img_btn_document_upload_preview_image_eft
                            .setVisibility(View.VISIBLE);
                    img_btn_document_delete_eft.setVisibility(View.VISIBLE);

                    img_btn_document_upload_click_browse_eft
                            .setImageDrawable(getResources().getDrawable(
                                    R.drawable.checkedbrowse));

                    /*boolean isBlurr = false*//* falseBlurr(EFTProofBitmap) *//*;

                    if (!isBlurr) {

                        reducedEFTBitmap = EFTProofBitmap;

                        img_btn_document_upload_preview_image_eft
                                .setVisibility(View.VISIBLE);
                        img_btn_document_delete_eft.setVisibility(View.VISIBLE);

                        img_btn_document_upload_click_browse_eft
                                .setImageDrawable(getResources().getDrawable(
                                        R.drawable.checkedbrowse));

                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        reducedEFTBitmap.compress(Bitmap.CompressFormat.PNG,
                                70, baos);
                        byte[] arr = baos.toByteArray();
                        String result = Base64.encodeToString(arr,
                                Base64.DEFAULT);
                        SelfAttestedDocumentActivity.lst_EFTBitmap.add(result);
                        showDialog(DIALOG_ALERT);

                    } else {
                        img_btn_document_upload_preview_image_eft
                                .setVisibility(View.INVISIBLE);
                        img_btn_document_delete_eft
                                .setVisibility(View.INVISIBLE);

                        img_btn_document_upload_click_browse_eft
                                .setImageDrawable(getResources().getDrawable(
                                        R.drawable.ibtn_browsedoc));

                    }*/

                } else if (Check.equals("OTPSelfDeclaration")) {
                    OTPSelfDeclarationFileName = new File(strPathOfFile);

                    //image compression by bhalla
                    /*otpSelfDeclarationBitmap = BitmapFactory.decodeFile(CompressImage.compressImageGetBitmap(OTPSelfDeclarationFileName.getAbsolutePath()));*/
                    CompressImage.compressImage(OTPSelfDeclarationFileName.getAbsolutePath());

                    /*otpSelfDeclarationBitmap = commonMethods.ShrinkBitmap(OTPSelfDeclarationFileName.getAbsolutePath(), 600, 600);*/

                    img_btn_document_upload_preview_image_otp_self_declaration
                            .setVisibility(View.VISIBLE);
                    img_btn_document_delete_otp_self_declaration
                            .setVisibility(View.VISIBLE);

                    img_btn_document_upload_click_browse_otp_self_declaration
                            .setImageDrawable(getResources().getDrawable(
                                    R.drawable.checkedbrowse));

                    /*boolean isBlurr = false*//* Blurr(otpSelfDeclarationBitmap) *//*;

                    if (!isBlurr) {

                        reducedOTPSelfDeclarationBitmap = otpSelfDeclarationBitmap;

                        img_btn_document_upload_preview_image_otp_self_declaration
                                .setVisibility(View.VISIBLE);
                        img_btn_document_delete_otp_self_declaration
                                .setVisibility(View.VISIBLE);

                        img_btn_document_upload_click_browse_otp_self_declaration
                                .setImageDrawable(getResources().getDrawable(
                                        R.drawable.checkedbrowse));

                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        reducedOTPSelfDeclarationBitmap.compress(
                                Bitmap.CompressFormat.PNG, 70, baos);
                        byte[] arr = baos.toByteArray();
                        String result = Base64.encodeToString(arr,
                                Base64.DEFAULT);
                        SelfAttestedDocumentActivity.lst_OTPSelfDeclarationBitmap
                                .add(result);

                        showDialog(DIALOG_ALERT);
                    } else {
                        img_btn_document_upload_preview_image_otp_self_declaration
                                .setVisibility(View.INVISIBLE);
                        img_btn_document_delete_otp_self_declaration
                                .setVisibility(View.INVISIBLE);

                        img_btn_document_upload_click_browse_otp_self_declaration
                                .setImageDrawable(getResources().getDrawable(
                                        R.drawable.ibtn_browsedoc));

                    }*/
                } else if (Check.equals("AgeProof")) {
                    ageProofFileName = new File(strPathOfFile);

                    //image compression by bhalla
                    /*ageProofBitmap = BitmapFactory.decodeFile(CompressImage.compressImageGetBitmap(ageProofFileName.getAbsolutePath()));*/

                    CompressImage.compressImage(ageProofFileName.getAbsolutePath());

                    /*ageProofBitmap = commonMethods.ShrinkBitmap(
                            ageProofFileName.getAbsolutePath(), 600, 600);*/

                    img_btn_document_upload_preview_image_age
                            .setVisibility(View.VISIBLE);
                    img_btn_document_delete_age.setVisibility(View.VISIBLE);

                    img_btn_document_upload_click_browse_age
                            .setImageDrawable(getResources().getDrawable(
                                    R.drawable.checkedbrowse));

                    /*boolean isBlurr = false*//* Blurr(ageProofBitmap) *//*;

                    if (!isBlurr) {

                        reducedAgeBitmap = ageProofBitmap;

                        img_btn_document_upload_preview_image_age
                                .setVisibility(View.VISIBLE);
                        img_btn_document_delete_age.setVisibility(View.VISIBLE);

                        img_btn_document_upload_click_browse_age
                                .setImageDrawable(getResources().getDrawable(
                                        R.drawable.checkedbrowse));

                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        reducedAgeBitmap.compress(Bitmap.CompressFormat.PNG,
                                70, baos);
                        byte[] arr = baos.toByteArray();
                        String result = Base64.encodeToString(arr,
                                Base64.DEFAULT);
                        SelfAttestedDocumentActivity.lst_AgeBitmap.add(result);

                        showDialog(DIALOG_ALERT);

                    } else {
                        img_btn_document_upload_preview_image_age
                                .setVisibility(View.INVISIBLE);
                        img_btn_document_delete_age
                                .setVisibility(View.INVISIBLE);

                        img_btn_document_upload_click_browse_age
                                .setImageDrawable(getResources().getDrawable(
                                        R.drawable.ibtn_browsedoc));

                    }*/

                } else if (Check.equals("IdentityProof")) {
                    identityProofFileName = new File(strPathOfFile);

                    //image compression by bhalla
                    /*identityProofBitmap = BitmapFactory.decodeFile(CompressImage.compressImageGetBitmap(identityProofFileName.getAbsolutePath()));*/

                    CompressImage.compressImage(identityProofFileName.getAbsolutePath());

                    /*identityProofBitmap = commonMethods.ShrinkBitmap(
                            identityProofFileName.getAbsolutePath(), 600, 600);*/

                    img_btn_document_upload_preview_image_identity
                            .setVisibility(View.VISIBLE);
                    img_btn_document_delete_identity
                            .setVisibility(View.VISIBLE);

                    img_btn_document_upload_click_browse_identity
                            .setImageDrawable(getResources().getDrawable(
                                    R.drawable.checkedbrowse));

                    /*boolean isBlurr = false*//* Blurr(identityProofBitmap) *//*;

                    if (!isBlurr) {

                        reducedIdentityBitmap = identityProofBitmap;

                        img_btn_document_upload_preview_image_identity
                                .setVisibility(View.VISIBLE);
                        img_btn_document_delete_identity
                                .setVisibility(View.VISIBLE);

                        img_btn_document_upload_click_browse_identity
                                .setImageDrawable(getResources().getDrawable(
                                        R.drawable.checkedbrowse));

                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        reducedIdentityBitmap.compress(
                                Bitmap.CompressFormat.PNG, 70, baos);
                        byte[] arr = baos.toByteArray();
                        String result = Base64.encodeToString(arr,
                                Base64.DEFAULT);
                        SelfAttestedDocumentActivity.lst_IdentityBitmap
                                .add(result);

                        showDialog(DIALOG_ALERT);

                    } else {
                        img_btn_document_upload_preview_image_identity
                                .setVisibility(View.INVISIBLE);
                        img_btn_document_delete_identity
                                .setVisibility(View.INVISIBLE);

                        img_btn_document_upload_click_browse_identity
                                .setImageDrawable(getResources().getDrawable(
                                        R.drawable.ibtn_browsedoc));

                    }*/

                } else if (Check.equals("AddressProof")) {
                    addressProofFileName = new File(strPathOfFile);

                    //image compression by bhalla
                    /*addressProofBitmap = BitmapFactory.decodeFile(CompressImage.compressImageGetBitmap(addressProofFileName.getAbsolutePath()));*/

                    CompressImage.compressImage(addressProofFileName.getAbsolutePath());

                    /*addressProofBitmap = commonMethods.ShrinkBitmap(
                            addressProofFileName.getAbsolutePath(), 600, 600);*/

                    img_btn_document_upload_click_preview_image_address
                            .setVisibility(View.VISIBLE);
                    img_btn_document_delete_address
                            .setVisibility(View.VISIBLE);
                    img_btn_document_upload_click_browse_address
                            .setImageDrawable(getResources().getDrawable(
                                    R.drawable.checkedbrowse));

                    /*boolean isBlurr = false*//* Blurr(addressProofBitmap) *//*;

                    if (!isBlurr) {

                        reducedAddressBitmap = addressProofBitmap;

                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        reducedAddressBitmap.compress(
                                Bitmap.CompressFormat.PNG, 70, baos);
                        byte[] arr = baos.toByteArray();
                        String result = Base64.encodeToString(arr,
                                Base64.DEFAULT);
                        SelfAttestedDocumentActivity.lst_AddressBitmap
                                .add(result);

                        showDialog(DIALOG_ALERT);

                    } else {
                        img_btn_document_upload_click_preview_image_address
                                .setVisibility(View.INVISIBLE);
                        img_btn_document_delete_address
                                .setVisibility(View.INVISIBLE);

                        img_btn_document_upload_click_browse_address
                                .setImageDrawable(getResources().getDrawable(
                                        R.drawable.ibtn_browsedoc));

                    }*/
                } else if (Check.equals("IncomeProof")) {
                    incomeProofFileName = new File(strPathOfFile);

                    //image compression by bhalla
                    /*incomeProofBitmap = BitmapFactory.decodeFile(CompressImage.compressImageGetBitmap(incomeProofFileName.getAbsolutePath()));*/
                    CompressImage.compressImage(incomeProofFileName.getAbsolutePath());

                    /*incomeProofBitmap = commonMethods.ShrinkBitmap(
                            incomeProofFileName.getAbsolutePath(), 600, 600);*/

                    img_btn_document_upload_click_preview_image_income
                            .setVisibility(View.VISIBLE);
                    img_btn_document_delete_income
                            .setVisibility(View.VISIBLE);
                    img_btn_document_upload_click_browse_income
                            .setImageDrawable(getResources().getDrawable(
                                    R.drawable.checkedbrowse));

                    /*boolean isBlurr = false*//* Blurr(incomeProofBitmap) *//*;
                    if (!isBlurr) {

                        reducedIncomeBitmap = incomeProofBitmap;

                        img_btn_document_upload_click_preview_image_income
                                .setVisibility(View.VISIBLE);
                        img_btn_document_delete_income
                                .setVisibility(View.VISIBLE);
                        img_btn_document_upload_click_browse_income
                                .setImageDrawable(getResources().getDrawable(
                                        R.drawable.checkedbrowse));

                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        reducedIncomeBitmap.compress(Bitmap.CompressFormat.PNG,
                                70, baos);
                        byte[] arr = baos.toByteArray();
                        String result = Base64.encodeToString(arr,
                                Base64.DEFAULT);
                        SelfAttestedDocumentActivity.lst_IncomeBitmap
                                .add(result);
                        showDialog(DIALOG_ALERT);

                    } else {
                        img_btn_document_upload_click_preview_image_income
                                .setVisibility(View.INVISIBLE);
                        img_btn_document_delete_income
                                .setVisibility(View.INVISIBLE);

                        img_btn_document_upload_click_browse_income
                                .setImageDrawable(getResources().getDrawable(
                                        R.drawable.ibtn_browsedoc));

                    }*/

                } else if (Check.equals("CustomerPhoto")) {

                    photo = new File(strPathOfFile);

                    //image compression by bhalla
                    //customerPhotoBitmap = BitmapFactory.decodeFile(CompressImage.compressImageGetBitmap(photo.getAbsolutePath()));
                    CompressImage.compressImage(photo.getAbsolutePath());

                    /*customerPhotoBitmap = commonMethods.ShrinkBitmap(photo.getAbsolutePath(), 600, 600);*/

                    img_btn_document_upload_preview_customer_photo
                            .setVisibility(View.VISIBLE);
                    img_btn_document_delete_customer_photo
                            .setVisibility(View.VISIBLE);
                    img_btn_document_upload_click_browse_customer_photo
                            .setImageDrawable(getResources().getDrawable(
                                    R.drawable.checkedbrowse));

                    /*boolean isBlurr = false*//* Blurr(incomeProofBitmap) *//*;
                    if (!isBlurr) {

                        reducedCustomerPhotoBitmap = customerPhotoBitmap;

                        img_btn_document_upload_preview_customer_photo
                                .setVisibility(View.VISIBLE);
                        img_btn_document_delete_customer_photo
                                .setVisibility(View.VISIBLE);
                        img_btn_document_upload_click_browse_customer_photo
                                .setImageDrawable(getResources().getDrawable(
                                        R.drawable.checkedbrowse));

                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        reducedCustomerPhotoBitmap.compress(
                                Bitmap.CompressFormat.PNG, 70, baos);
                        byte[] arr = baos.toByteArray();
                        String result = Base64.encodeToString(arr,
                                Base64.DEFAULT);
                        SelfAttestedDocumentActivity.lst_CustomerPhotoBitmap
                                .add(result);

                        showDialog(DIALOG_ALERT);

                    } else {
                        img_btn_document_upload_preview_customer_photo
                                .setVisibility(View.INVISIBLE);
                        img_btn_document_delete_customer_photo
                                .setVisibility(View.INVISIBLE);

                        img_btn_document_upload_click_browse_customer_photo
                                .setImageDrawable(getResources().getDrawable(
                                        R.drawable.ibtn_browsedoc));

                    }*/
                }
            }
        } else if (requestCode == REQUEST_CODE_CAPTURE_FILE) {
            if (resultCode == RESULT_OK) {
                switch (Check) {
                    case "ProposerPhoto": {

                        proposerBrowsedPhotoFile = f;

                        //image compression by bhalla
                        /*photoBitmap = BitmapFactory.decodeFile(CompressImage.compressImageGetBitmap(proposerBrowsedPhotoFile.getAbsolutePath()));*/
                        CompressImage.compressImage(proposerBrowsedPhotoFile.getAbsolutePath());

                        /*photoBitmap = commonMethods.ShrinkBitmap(Photo.getAbsolutePath(), 600, 600);*/

                        img_btn_document_upload_preview_proposer_photo
                                .setVisibility(View.VISIBLE);
                        img_btn_document_delete_proposer_photo
                                .setVisibility(View.VISIBLE);
                        img_btn_document_upload_click_image_proposer_photo
                                .setImageDrawable(getResources().getDrawable(
                                        R.drawable.checkedcamera));

                        /*boolean isBlurr = false*//* Blurr(photoBitmap) *//*;

                        if (!isBlurr) {

                            ReducedProposerPhotoBitmap = photoBitmap;

                            img_btn_document_upload_preview_proposer_photo
                                    .setVisibility(View.VISIBLE);
                            img_btn_document_delete_proposer_photo
                                    .setVisibility(View.VISIBLE);
                            img_btn_document_upload_click_image_proposer_photo
                                    .setImageDrawable(getResources().getDrawable(
                                            R.drawable.checkedcamera));

                            try {
                                FileOutputStream fileOutputStream = new FileOutputStream(
                                        Photo);
                                photoBitmap.compress(Bitmap.CompressFormat.JPEG,
                                        100, fileOutputStream);
                                fileOutputStream.flush();
                                fileOutputStream.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        } else {
                            img_btn_document_upload_preview_proposer_photo
                                    .setVisibility(View.INVISIBLE);
                            img_btn_document_delete_proposer_photo
                                    .setVisibility(View.INVISIBLE);

                            img_btn_document_upload_click_image_proposer_photo
                                    .setImageDrawable(getResources().getDrawable(
                                            R.drawable.ibtn_browsedoc));
                        }*/

                        break;
                    }
                    case "LifeAssuredPhoto": {

                        lifeAssuredBrowsedPhotoFile = f;

                        //image compression by bhalla
                        /*lifeAssuredBitmap = BitmapFactory.decodeFile(CompressImage.compressImageGetBitmap(lifeAssuredBrowsedPhotoFile.getAbsolutePath()));*/
                        CompressImage.compressImage(lifeAssuredBrowsedPhotoFile.getAbsolutePath());

                        /*lifeAssuredBitmap = commonMethods.ShrinkBitmap(Photo.getAbsolutePath(), 600, 600);*/

                        img_btn_document_upload_preview_life_assured_photo
                                .setVisibility(View.VISIBLE);
                        img_btn_document_delete_life_assured_photo
                                .setVisibility(View.VISIBLE);
                        img_btn_document_upload_click_image_life_assured_photo
                                .setImageDrawable(getResources().getDrawable(
                                        R.drawable.checkedcamera));

                        /*boolean isBlurr = false*//* Blurr(BankProofBitmap) *//*;

                        if (!isBlurr) {

                            ReducedLifeAssuredPhotoBitmap = lifeAssuredBitmap;

                            img_btn_document_upload_preview_life_assured_photo
                                    .setVisibility(View.VISIBLE);
                            img_btn_document_delete_life_assured_photo
                                    .setVisibility(View.VISIBLE);
                            img_btn_document_upload_click_image_life_assured_photo
                                    .setImageDrawable(getResources().getDrawable(
                                            R.drawable.checkedcamera));

                            try {
                                FileOutputStream fileOutputStream = new FileOutputStream(
                                        Photo);
                                lifeAssuredBitmap.compress(
                                        Bitmap.CompressFormat.JPEG, 100,
                                        fileOutputStream);
                                fileOutputStream.flush();
                                fileOutputStream.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        } else {
                            img_btn_document_upload_preview_life_assured_photo
                                    .setVisibility(View.INVISIBLE);
                            img_btn_document_delete_life_assured_photo
                                    .setVisibility(View.INVISIBLE);

                            img_btn_document_upload_click_image_life_assured_photo
                                    .setImageDrawable(getResources().getDrawable(
                                            R.drawable.ibtn_browsedoc));
                        }*/
                        break;
                    }
                    case "BankProof": {

                        bankProofFileName = f;

                        //image compression by bhalla
                        /*BankProofBitmap = BitmapFactory.decodeFile(CompressImage.compressImageGetBitmap(bankProofFileName.getAbsolutePath()));*/
                        CompressImage.compressImage(bankProofFileName.getAbsolutePath());

                        /*BankProofBitmap = commonMethods.ShrinkBitmap(bankProofFileName.getAbsolutePath(), 600, 600);*/

                        img_btn_document_upload_click_preview_image_bank
                                .setVisibility(View.VISIBLE);
                        img_btn_document_delete_bank
                                .setVisibility(View.VISIBLE);
                        img_btn_document_upload_click_image_bank
                                .setImageDrawable(getResources().getDrawable(
                                        R.drawable.checkedcamera));

                        /*boolean isBlurr = false*//* Blurr(BankProofBitmap) *//*;

                        if (!isBlurr) {

                            reducedBankBitmap = BankProofBitmap;

                            img_btn_document_upload_click_preview_image_bank
                                    .setVisibility(View.VISIBLE);
                            img_btn_document_delete_bank
                                    .setVisibility(View.VISIBLE);
                            img_btn_document_upload_click_image_bank
                                    .setImageDrawable(getResources().getDrawable(
                                            R.drawable.checkedcamera));

                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            reducedBankBitmap.compress(Bitmap.CompressFormat.PNG,
                                    70, baos);
                            byte[] arr = baos.toByteArray();
                            String result = Base64.encodeToString(arr,
                                    Base64.DEFAULT);
                            SelfAttestedDocumentActivity.lst_BankBitmap.add(result);

                            showDialog(DIALOG_ALERT);

                        } else {
                            img_btn_document_upload_click_preview_image_bank
                                    .setVisibility(View.INVISIBLE);
                            img_btn_document_delete_bank
                                    .setVisibility(View.INVISIBLE);

                            img_btn_document_upload_click_image_bank
                                    .setImageDrawable(getResources().getDrawable(
                                            R.drawable.ibtn_browsedoc));

                        }*/

                        break;
                    }
                    case "EFTProof": {

                        EFTProofFileName = f;

                        //image compression by bhalla
                        /*EFTProofBitmap = BitmapFactory.decodeFile(CompressImage.compressImageGetBitmap(EFTProofFileName.getAbsolutePath()));*/
                        CompressImage.compressImage(EFTProofFileName.getAbsolutePath());

                        /*EFTProofBitmap = commonMethods.ShrinkBitmap(EFTProofFileName.getAbsolutePath(), 600, 600);*/

                        img_btn_document_upload_preview_image_eft
                                .setVisibility(View.VISIBLE);
                        img_btn_document_delete_eft.setVisibility(View.VISIBLE);
                        img_btn_document_upload_click_image_eft
                                .setImageDrawable(getResources().getDrawable(
                                        R.drawable.checkedcamera));

                        /*boolean isBlurr = false *//* falseBlurr(EFTProofBitmap) *//*;

                        if (!isBlurr) {

                            reducedEFTBitmap = EFTProofBitmap;

                            img_btn_document_upload_preview_image_eft
                                    .setVisibility(View.VISIBLE);
                            img_btn_document_delete_eft.setVisibility(View.VISIBLE);
                            img_btn_document_upload_click_image_eft
                                    .setImageDrawable(getResources().getDrawable(
                                            R.drawable.checkedcamera));

                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            reducedEFTBitmap.compress(Bitmap.CompressFormat.PNG,
                                    70, baos);
                            byte[] arr = baos.toByteArray();
                            String result = Base64.encodeToString(arr,
                                    Base64.DEFAULT);
                            SelfAttestedDocumentActivity.lst_EFTBitmap.add(result);
                            showDialog(DIALOG_ALERT);

                        } else {
                            img_btn_document_upload_preview_image_eft
                                    .setVisibility(View.INVISIBLE);
                            img_btn_document_delete_eft
                                    .setVisibility(View.INVISIBLE);

                            img_btn_document_upload_click_image_eft
                                    .setImageDrawable(getResources().getDrawable(
                                            R.drawable.ibtn_browsedoc));

                        }*/

                        break;
                    }
                    case "OtherProof": {
                        otherProofFileName = f;

                        //image compression by bhalla
                        /*otherProofBitmap = BitmapFactory.decodeFile(CompressImage.compressImageGetBitmap(otherProofFileName.getAbsolutePath()));*/
                        CompressImage.compressImage(otherProofFileName.getAbsolutePath());

                        /*otherProofBitmap = commonMethods.ShrinkBitmap(otherProofFileName.getAbsolutePath(), 600, 600);*/

                        img_btn_document_upload_click_preview_image_others
                                .setVisibility(View.VISIBLE);

                        img_btn_document_delete_Others
                                .setVisibility(View.VISIBLE);

                        img_btn_document_upload_click_image_others
                                .setImageDrawable(getResources().getDrawable(
                                        R.drawable.checkedcamera));

                        /*boolean isBlurr = false; *//* falseBlurr(otherProofBitmap); *//*

                        if (!isBlurr) {

                            reducedOtherBitmap = otherProofBitmap;

                            img_btn_document_upload_click_preview_image_others
                                    .setVisibility(View.VISIBLE);

                            img_btn_document_delete_Others
                                    .setVisibility(View.VISIBLE);

                            img_btn_document_upload_click_image_others
                                    .setImageDrawable(getResources().getDrawable(
                                            R.drawable.checkedcamera));

                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            reducedOtherBitmap.compress(Bitmap.CompressFormat.PNG,
                                    70, baos);
                            byte[] arr = baos.toByteArray();
                            String result = Base64.encodeToString(arr,
                                    Base64.DEFAULT);
                            SelfAttestedDocumentActivity.lst_OtherBitmap
                                    .add(result);
                            showDialog(DIALOG_ALERT);

                        } else {
                            img_btn_document_upload_click_preview_image_others
                                    .setVisibility(View.INVISIBLE);
                            img_btn_document_delete_Others
                                    .setVisibility(View.INVISIBLE);

                            img_btn_document_upload_click_image_others
                                    .setImageDrawable(getResources().getDrawable(
                                            R.drawable.ibtn_browsedoc));
                        }*/

                        break;
                    }
                    case "OTPSelfDeclaration": {
                        OTPSelfDeclarationFileName = f;

                        //image compression by bhalla
                        /*otpSelfDeclarationBitmap = BitmapFactory.decodeFile(CompressImage.compressImageGetBitmap(OTPSelfDeclarationFileName.getAbsolutePath()));*/
                        CompressImage.compressImage(OTPSelfDeclarationFileName.getAbsolutePath());

                        /*otpSelfDeclarationBitmap = commonMethods.ShrinkBitmap(
                                OTPSelfDeclarationFileName.getAbsolutePath(), 600,
                                600);*/

                        img_btn_document_upload_preview_image_otp_self_declaration
                                .setVisibility(View.VISIBLE);
                        img_btn_document_delete_otp_self_declaration
                                .setVisibility(View.VISIBLE);
                        img_btn_document_upload_click_image_otp_self_declaration
                                .setImageDrawable(getResources().getDrawable(
                                        R.drawable.checkedcamera));

                        /*boolean isBlurr = false*//* Blurr(otpSelfDeclarationBitmap) *//*;
                        if (!isBlurr) {
                            reducedOTPSelfDeclarationBitmap = otpSelfDeclarationBitmap;

                            img_btn_document_upload_preview_image_otp_self_declaration
                                    .setVisibility(View.VISIBLE);
                            img_btn_document_delete_otp_self_declaration
                                    .setVisibility(View.VISIBLE);
                            img_btn_document_upload_click_image_otp_self_declaration
                                    .setImageDrawable(getResources().getDrawable(
                                            R.drawable.checkedcamera));

                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            reducedOTPSelfDeclarationBitmap.compress(
                                    Bitmap.CompressFormat.PNG, 70, baos);
                            byte[] arr = baos.toByteArray();
                            String result = Base64.encodeToString(arr,
                                    Base64.DEFAULT);
                            SelfAttestedDocumentActivity.lst_OTPSelfDeclarationBitmap
                                    .add(result);

                            showDialog(DIALOG_ALERT);
                        } else {
                            img_btn_document_upload_preview_image_otp_self_declaration
                                    .setVisibility(View.INVISIBLE);
                            img_btn_document_delete_otp_self_declaration
                                    .setVisibility(View.INVISIBLE);

                            img_btn_document_upload_click_image_otp_self_declaration
                                    .setImageDrawable(getResources().getDrawable(
                                            R.drawable.ibtn_browsedoc));
                        }*/
                        break;
                    }
                    case "AgeProof": {

                        ageProofFileName = f;

                        //image compression by bhalla
                        /*ageProofBitmap = BitmapFactory.decodeFile(CompressImage.compressImageGetBitmap(ageProofFileName.getAbsolutePath()));*/
                        CompressImage.compressImage(ageProofFileName.getAbsolutePath());

                        /*ageProofBitmap = commonMethods.ShrinkBitmap(
                                ageProofFileName.getAbsolutePath(), 600, 600);*/

                        img_btn_document_upload_preview_image_age
                                .setVisibility(View.VISIBLE);
                        img_btn_document_delete_age.setVisibility(View.VISIBLE);
                        img_btn_document_upload_click_image_age
                                .setImageDrawable(getResources().getDrawable(
                                        R.drawable.checkedcamera));

                        /*boolean isBlurr = false*//* Blurr(ageProofBitmap) *//*;

                        if (!isBlurr) {

                            reducedAgeBitmap = ageProofBitmap;

                            img_btn_document_upload_preview_image_age
                                    .setVisibility(View.VISIBLE);
                            img_btn_document_delete_age.setVisibility(View.VISIBLE);
                            img_btn_document_upload_click_image_age
                                    .setImageDrawable(getResources().getDrawable(
                                            R.drawable.checkedcamera));

                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            reducedAgeBitmap.compress(Bitmap.CompressFormat.PNG,
                                    70, baos);
                            byte[] arr = baos.toByteArray();
                            String result = Base64.encodeToString(arr,
                                    Base64.DEFAULT);
                            SelfAttestedDocumentActivity.lst_AgeBitmap.add(result);

                            showDialog(DIALOG_ALERT);

                        } else {
                            img_btn_document_upload_preview_image_age
                                    .setVisibility(View.INVISIBLE);
                            img_btn_document_delete_age
                                    .setVisibility(View.INVISIBLE);

                            img_btn_document_upload_click_image_age
                                    .setImageDrawable(getResources().getDrawable(
                                            R.drawable.ibtn_browsedoc));

                        }*/

                        break;
                    }
                    case "IdentityProof": {
                        identityProofFileName = f;

                        //image compression by bhalla
                        /*identityProofBitmap = BitmapFactory.decodeFile(CompressImage.compressImageGetBitmap(identityProofFileName.getAbsolutePath()));*/
                        CompressImage.compressImage(identityProofFileName.getAbsolutePath());

                        /*identityProofBitmap = commonMethods.ShrinkBitmap(
                                identityProofFileName.getAbsolutePath(), 600, 600);*/

                        img_btn_document_upload_preview_image_identity
                                .setVisibility(View.VISIBLE);
                        img_btn_document_delete_identity
                                .setVisibility(View.VISIBLE);
                        img_btn_document_upload_click_image_identity
                                .setImageDrawable(getResources().getDrawable(
                                        R.drawable.checkedcamera));

                        /*boolean isBlurr = false*//* Blurr(identityProofBitmap) *//*;

                        if (!isBlurr) {

                            reducedIdentityBitmap = identityProofBitmap;

                            img_btn_document_upload_preview_image_identity
                                    .setVisibility(View.VISIBLE);
                            img_btn_document_delete_identity
                                    .setVisibility(View.VISIBLE);
                            img_btn_document_upload_click_image_identity
                                    .setImageDrawable(getResources().getDrawable(
                                            R.drawable.checkedcamera));

                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            reducedIdentityBitmap.compress(
                                    Bitmap.CompressFormat.PNG, 70, baos);
                            byte[] arr = baos.toByteArray();
                            String result = Base64.encodeToString(arr,
                                    Base64.DEFAULT);
                            SelfAttestedDocumentActivity.lst_IdentityBitmap
                                    .add(result);

                            showDialog(DIALOG_ALERT);

                        } else {
                            img_btn_document_upload_preview_image_identity
                                    .setVisibility(View.INVISIBLE);
                            img_btn_document_delete_identity
                                    .setVisibility(View.INVISIBLE);

                            img_btn_document_upload_click_image_identity
                                    .setImageDrawable(getResources().getDrawable(
                                            R.drawable.ibtn_browsedoc));

                        }*/

                        break;
                    }
                    case "AddressProof": {
                        addressProofFileName = f;

                        //image compression by bhalla
                        /*addressProofBitmap = BitmapFactory.decodeFile(CompressImage.compressImageGetBitmap(addressProofFileName.getAbsolutePath()));*/
                        CompressImage.compressImage(addressProofFileName.getAbsolutePath());

                        /*addressProofBitmap = commonMethods.ShrinkBitmap(
                                addressProofFileName.getAbsolutePath(), 600, 600);*/

                        img_btn_document_upload_click_preview_image_address
                                .setVisibility(View.VISIBLE);
                        img_btn_document_delete_address
                                .setVisibility(View.VISIBLE);
                        img_btn_document_upload_click_image_address
                                .setImageDrawable(getResources().getDrawable(
                                        R.drawable.checkedcamera));

                        /*boolean isBlurr = false*//* Blurr(addressProofBitmap) *//*;

                        if (!isBlurr) {

                            reducedAddressBitmap = addressProofBitmap;

                            img_btn_document_upload_click_preview_image_address
                                    .setVisibility(View.VISIBLE);
                            img_btn_document_delete_address
                                    .setVisibility(View.VISIBLE);
                            img_btn_document_upload_click_image_address
                                    .setImageDrawable(getResources().getDrawable(
                                            R.drawable.checkedcamera));

                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            reducedAddressBitmap.compress(
                                    Bitmap.CompressFormat.PNG, 70, baos);
                            byte[] arr = baos.toByteArray();
                            String result = Base64.encodeToString(arr,
                                    Base64.DEFAULT);
                            SelfAttestedDocumentActivity.lst_AddressBitmap
                                    .add(result);
                            showDialog(DIALOG_ALERT);

                        } else {
                            img_btn_document_upload_click_preview_image_address
                                    .setVisibility(View.INVISIBLE);
                            img_btn_document_delete_address
                                    .setVisibility(View.INVISIBLE);

                            img_btn_document_upload_click_image_address
                                    .setImageDrawable(getResources().getDrawable(
                                            R.drawable.ibtn_browsedoc));

                        }*/

                        break;
                    }
                    case "IncomeProof": {
                        incomeProofFileName = f;

                        //image compression by bhalla
                        /*incomeProofBitmap = BitmapFactory.decodeFile(CompressImage.compressImageGetBitmap(incomeProofFileName.getAbsolutePath()));*/
                        CompressImage.compressImage(incomeProofFileName.getAbsolutePath());

                        /*incomeProofBitmap = commonMethods.ShrinkBitmap(
                                incomeProofFileName.getAbsolutePath(), 600, 600);*/
                        img_btn_document_upload_click_preview_image_income
                                .setVisibility(View.VISIBLE);
                        img_btn_document_delete_income
                                .setVisibility(View.VISIBLE);
                        img_btn_document_upload_click_image_income
                                .setImageDrawable(getResources().getDrawable(
                                        R.drawable.checkedcamera));

                        /*boolean isBlurr = false*//* Blurr(incomeProofBitmap) *//*;

                        if (!isBlurr) {

                            reducedIncomeBitmap = incomeProofBitmap;

                            img_btn_document_upload_click_preview_image_income
                                    .setVisibility(View.VISIBLE);
                            img_btn_document_delete_income
                                    .setVisibility(View.VISIBLE);
                            img_btn_document_upload_click_image_income
                                    .setImageDrawable(getResources().getDrawable(
                                            R.drawable.checkedcamera));

                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            reducedIncomeBitmap.compress(Bitmap.CompressFormat.PNG,
                                    70, baos);
                            byte[] arr = baos.toByteArray();
                            String result = Base64.encodeToString(arr,
                                    Base64.DEFAULT);
                            SelfAttestedDocumentActivity.lst_IncomeBitmap
                                    .add(result);
                            showDialog(DIALOG_ALERT);

                        } else {
                            img_btn_document_upload_click_preview_image_income
                                    .setVisibility(View.INVISIBLE);
                            img_btn_document_delete_income
                                    .setVisibility(View.INVISIBLE);

                            img_btn_document_upload_click_image_income
                                    .setImageDrawable(getResources().getDrawable(
                                            R.drawable.ibtn_browsedoc));

                        }*/

                        break;
                    }
                    case "CustomerPhoto": {

                        photo = f;

                        //image compression by bhalla
                        /*customerPhotoBitmap = BitmapFactory.decodeFile(CompressImage.compressImageGetBitmap(photo.getAbsolutePath()));*/
                        CompressImage.compressImage(photo.getAbsolutePath());

                        /*customerPhotoBitmap = commonMethods.ShrinkBitmap(photo.getAbsolutePath(), 600, 600);*/

                        img_btn_document_upload_preview_customer_photo
                                .setVisibility(View.VISIBLE);
                        img_btn_document_delete_customer_photo
                                .setVisibility(View.VISIBLE);
                        img_btn_document_upload_click_image_customer_photo
                                .setImageDrawable(getResources().getDrawable(
                                        R.drawable.checkedcamera));

                        /*boolean isBlurr = false*//* Blurr(customerPhotoBitmap) *//*;

                        if (!isBlurr) {

                            reducedCustomerPhotoBitmap = customerPhotoBitmap;

                            img_btn_document_upload_preview_customer_photo
                                    .setVisibility(View.VISIBLE);
                            img_btn_document_delete_customer_photo
                                    .setVisibility(View.VISIBLE);
                            img_btn_document_upload_click_image_customer_photo
                                    .setImageDrawable(getResources().getDrawable(
                                            R.drawable.checkedcamera));

                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            reducedCustomerPhotoBitmap.compress(
                                    Bitmap.CompressFormat.PNG, 70, baos);
                            byte[] arr = baos.toByteArray();
                            String result = Base64.encodeToString(arr,
                                    Base64.DEFAULT);
                            SelfAttestedDocumentActivity.lst_CustomerPhotoBitmap
                                    .add(result);
                            showDialog(DIALOG_ALERT);

                        } else {
                            img_btn_document_upload_preview_customer_photo
                                    .setVisibility(View.INVISIBLE);
                            img_btn_document_delete_customer_photo
                                    .setVisibility(View.INVISIBLE);
                            img_btn_document_upload_click_image_customer_photo
                                    .setImageDrawable(getResources().getDrawable(
                                            R.drawable.ibtn_browsedoc));

                        }*/
                        break;
                    }
                }
            }
        } else if (requestCode == REQUEST_QR_CODE) {

            if (resultCode == RESULT_OK) {
                try {
                    IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
                    if (intentResult != null) {
                        if (intentResult.getContents() == null) {
                            Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
                        } else {
                            strAadhaarQRScannedValue = intentResult.getContents();

                            new ServiceHits(context,
                                    METHOD_NAME_UPLOAD_QR_CODE_DETAILS, "", mUserDetailsValuesModel.getStrCIFBDMUserId(),
                                    mUserDetailsValuesModel.getStrCIFBDMEmailId(), mUserDetailsValuesModel.getStrCIFBDMMObileNo(),
                                    mUserDetailsValuesModel.getStrCIFBDMPassword(), this).execute();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
    }

    public void setChildVeiwVisibility(ViewGroup view_group,
                                       boolean setVisiblity) {

        for (int i = 0; i < view_group.getChildCount(); i++) {
            View view = view_group.getChildAt(i);
            view.setEnabled(setVisiblity); // Or whatever you want to do with
            // the view.
        }

    }

    private void clearAllAfterURNChanged() {

        photoBitmap = lifeAssuredBitmap = null;

        /*
         * imageButtonParivartanDocumentsUploadProposerPhoto
         * .setImageDrawable(getResources().getDrawable(
         * R.drawable.focus_imagebutton_photo));
         *
         * imageButtonParivartanDocumentsUploadLifeAssuredPhoto
         * .setImageDrawable(getResources().getDrawable(
         * R.drawable.focus_imagebutton_photo));
         */

        ClearDocumentsPDF();

        // camera proposer photo
        /*
         * imageButtonParivartanDocumentsUploadProposerPhotoCapture
         * .setClickable(true);
         * imageButtonParivartanDocumentsUploadProposerPhotoCapture
         * .setEnabled(true);
         * imageButtonParivartanDocumentsUploadProposerPhotoCapture
         * .setImageDrawable(getResources().getDrawable(
         * R.drawable.ibtn_camera));
         */

        // browse proposer photo
        /*
         * imageButtonParivartanDocumentsUploadProposerPhotoBrowse
         * .setClickable(true);
         * imageButtonParivartanDocumentsUploadProposerPhotoBrowse
         * .setEnabled(true);
         * imageButtonParivartanDocumentsUploadProposerPhotoBrowse
         * .setImageDrawable(getResources().getDrawable(
         * R.drawable.ibtn_browsedoc));
         */

        // upload proposer photo
        /*
         * imageButtonParivartanDocumentsUploadProposerPhotoUpload
         * .setClickable(true);
         * imageButtonParivartanDocumentsUploadProposerPhotoUpload
         * .setEnabled(true);
         * imageButtonParivartanDocumentsUploadProposerPhotoUpload
         * .setImageDrawable(getResources().getDrawable(
         * R.drawable.ibtn_uploaddoc));
         */

        // camera life assured photo
        /*
         * imageButtonParivartanDocumentsUploadLifeAssuredPhotoCapture
         * .setClickable(true);
         * imageButtonParivartanDocumentsUploadLifeAssuredPhotoCapture
         * .setEnabled(true);
         * imageButtonParivartanDocumentsUploadLifeAssuredPhotoCapture
         * .setImageDrawable(getResources().getDrawable(
         * R.drawable.ibtn_camera));
         */

        // browse life assured photo
        /*
         * imageButtonParivartanDocumentsUploadLifeAssuredPhotoBrowse
         * .setClickable(true);
         * imageButtonParivartanDocumentsUploadLifeAssuredPhotoBrowse
         * .setEnabled(true);
         * imageButtonParivartanDocumentsUploadLifeAssuredPhotoBrowse
         * .setImageDrawable(getResources().getDrawable(
         * R.drawable.ibtn_browsedoc));
         */

        // upload life assured photo
        /*
         * imageButtonParivartanDocumentsUploadLifeAssuredPhotoUpload
         * .setClickable(true);
         * imageButtonParivartanDocumentsUploadLifeAssuredPhotoUpload
         * .setEnabled(true);
         * imageButtonParivartanDocumentsUploadLifeAssuredPhotoUpload
         * .setImageDrawable(getResources().getDrawable(
         * R.drawable.ibtn_uploaddoc));
         */

        // camera otp self declaration
        img_btn_document_upload_click_image_otp_self_declaration
                .setClickable(true);
        img_btn_document_upload_click_image_otp_self_declaration
                .setEnabled(true);
        img_btn_document_upload_click_image_otp_self_declaration
                .setImageDrawable(getResources().getDrawable(
                        R.drawable.ibtn_camera));

        // browse otp self declaration
        img_btn_document_upload_click_browse_otp_self_declaration
                .setClickable(true);
        img_btn_document_upload_click_browse_otp_self_declaration
                .setEnabled(true);
        img_btn_document_upload_click_browse_otp_self_declaration
                .setImageDrawable(getResources().getDrawable(
                        R.drawable.ibtn_browsedoc));

        // upload otp self declaration
        img_btn_document_upload_otp_self_declaration_upload.setClickable(true);
        img_btn_document_upload_otp_self_declaration_upload.setEnabled(true);
        img_btn_document_upload_otp_self_declaration_upload
                .setImageDrawable(getResources().getDrawable(
                        R.drawable.ibtn_uploaddoc));

        // camera age
        img_btn_document_upload_click_image_age.setClickable(true);
        img_btn_document_upload_click_image_age.setEnabled(true);
        img_btn_document_upload_click_image_age.setImageDrawable(getResources()
                .getDrawable(R.drawable.ibtn_camera));

        // browse age
        img_btn_document_upload_click_browse_age.setClickable(true);
        img_btn_document_upload_click_browse_age.setEnabled(true);
        img_btn_document_upload_click_browse_age
                .setImageDrawable(getResources().getDrawable(
                        R.drawable.ibtn_browsedoc));

        // upload age
        img_btn_document_upload_age_upload.setClickable(true);
        img_btn_document_upload_age_upload.setEnabled(true);
        img_btn_document_upload_age_upload.setImageDrawable(getResources()
                .getDrawable(R.drawable.ibtn_uploaddoc));

        // spinner age
        spnr_document_upload_document_age.setClickable(true);
        spnr_document_upload_document_age.setEnabled(true);

        // camera identity
        img_btn_document_upload_click_image_identity.setClickable(true);
        img_btn_document_upload_click_image_identity.setEnabled(true);
        img_btn_document_upload_click_image_identity
                .setImageDrawable(getResources().getDrawable(
                        R.drawable.ibtn_camera));

        // browse identity
        img_btn_document_upload_click_browse_identity.setClickable(true);
        img_btn_document_upload_click_browse_identity.setEnabled(true);
        img_btn_document_upload_click_browse_identity
                .setImageDrawable(getResources().getDrawable(
                        R.drawable.ibtn_browsedoc));

        // upload identity
        img_btn_document_upload_identity_upload.setClickable(true);
        img_btn_document_upload_identity_upload.setEnabled(true);
        img_btn_document_upload_identity_upload.setImageDrawable(getResources()
                .getDrawable(R.drawable.ibtn_uploaddoc));

        // spinner identity
        spnr_document_upload_document_identity.setClickable(true);
        spnr_document_upload_document_identity.setEnabled(true);

        // camera address
        img_btn_document_upload_click_image_address.setClickable(true);
        img_btn_document_upload_click_image_address.setEnabled(true);
        img_btn_document_upload_click_image_address
                .setImageDrawable(getResources().getDrawable(
                        R.drawable.ibtn_camera));

        // browse address
        img_btn_document_upload_click_browse_address.setClickable(true);
        img_btn_document_upload_click_browse_address.setEnabled(true);
        img_btn_document_upload_click_browse_address
                .setImageDrawable(getResources().getDrawable(
                        R.drawable.ibtn_browsedoc));

        // upload address
        img_btn_document_upload_address_upload.setClickable(true);
        img_btn_document_upload_address_upload.setEnabled(true);
        img_btn_document_upload_address_upload.setImageDrawable(getResources()
                .getDrawable(R.drawable.ibtn_uploaddoc));

        // spinner address
        spnr_document_upload_document_address.setClickable(true);
        spnr_document_upload_document_address.setEnabled(true);

        // camera income
        img_btn_document_upload_click_image_income.setClickable(true);
        img_btn_document_upload_click_image_income.setEnabled(true);
        img_btn_document_upload_click_image_income
                .setImageDrawable(getResources().getDrawable(
                        R.drawable.ibtn_camera));

        // browse income
        img_btn_document_upload_click_browse_income.setClickable(true);
        img_btn_document_upload_click_browse_income.setEnabled(true);
        img_btn_document_upload_click_browse_income
                .setImageDrawable(getResources().getDrawable(
                        R.drawable.ibtn_browsedoc));

        // upload income
        img_btn_document_upload_income_upload.setClickable(true);
        img_btn_document_upload_income_upload.setEnabled(true);
        img_btn_document_upload_income_upload.setImageDrawable(getResources()
                .getDrawable(R.drawable.ibtn_uploaddoc));

        // spinner income
        spnr_document_upload_document_income.setClickable(true);
        spnr_document_upload_document_income.setEnabled(true);

        // camera bank proof
        img_btn_document_upload_click_image_bank.setClickable(true);
        img_btn_document_upload_click_image_bank.setEnabled(true);
        img_btn_document_upload_click_image_bank
                .setImageDrawable(getResources().getDrawable(
                        R.drawable.ibtn_camera));

        // browse bank proof
        img_btn_document_upload_click_browse_bank.setClickable(true);
        img_btn_document_upload_click_browse_bank.setEnabled(true);
        img_btn_document_upload_click_browse_bank
                .setImageDrawable(getResources().getDrawable(
                        R.drawable.ibtn_browsedoc));

        // upload bank proof
        img_btn_document_upload_bank_upload.setClickable(true);
        img_btn_document_upload_bank_upload.setEnabled(true);
        img_btn_document_upload_bank_upload.setImageDrawable(getResources()
                .getDrawable(R.drawable.ibtn_uploaddoc));

        // spinner bank proof
        spnr_document_upload_document_bank.setClickable(true);
        spnr_document_upload_document_bank.setEnabled(true);

        // camera eft
        img_btn_document_upload_click_image_eft.setClickable(true);
        img_btn_document_upload_click_image_eft.setEnabled(true);
        img_btn_document_upload_click_image_eft.setImageDrawable(getResources()
                .getDrawable(R.drawable.ibtn_camera));

        // browse eft
        img_btn_document_upload_click_browse_eft.setClickable(true);
        img_btn_document_upload_click_browse_eft.setEnabled(true);
        img_btn_document_upload_click_browse_eft
                .setImageDrawable(getResources().getDrawable(
                        R.drawable.ibtn_browsedoc));

        // upload eft
        img_btn_document_upload_eft_upload.setClickable(true);
        img_btn_document_upload_eft_upload.setEnabled(true);
        img_btn_document_upload_eft_upload.setImageDrawable(getResources()
                .getDrawable(R.drawable.ibtn_uploaddoc));

        // spinner eft
        spnr_document_upload_document_eft.setClickable(true);
        spnr_document_upload_document_eft.setEnabled(true);

        // camera customer pic
        img_btn_document_upload_click_image_customer_photo.setClickable(true);
        img_btn_document_upload_click_image_customer_photo.setEnabled(true);
        img_btn_document_upload_click_image_customer_photo
                .setImageDrawable(getResources().getDrawable(
                        R.drawable.ibtn_camera));

        // browse customer pic
        img_btn_document_upload_click_browse_customer_photo.setClickable(true);
        img_btn_document_upload_click_browse_customer_photo.setEnabled(true);
        img_btn_document_upload_click_browse_customer_photo
                .setImageDrawable(getResources().getDrawable(
                        R.drawable.ibtn_browsedoc));

        // upload customer pic
        img_btn_document_upload_customer_photo_upload.setClickable(true);
        img_btn_document_upload_customer_photo_upload.setEnabled(true);
        img_btn_document_upload_customer_photo_upload
                .setImageDrawable(getResources().getDrawable(
                        R.drawable.ibtn_uploaddoc));

        // camera proposer pic
        img_btn_document_upload_click_image_proposer_photo.setClickable(true);
        img_btn_document_upload_click_image_proposer_photo.setEnabled(true);
        img_btn_document_upload_click_image_proposer_photo
                .setImageDrawable(getResources().getDrawable(
                        R.drawable.ibtn_camera));

        // browse proposer pic
        img_btn_document_upload_click_browse_proposer_photo.setClickable(true);
        img_btn_document_upload_click_browse_proposer_photo.setEnabled(true);
        img_btn_document_upload_click_browse_proposer_photo
                .setImageDrawable(getResources().getDrawable(
                        R.drawable.ibtn_browsedoc));

        // upload proposer pic
        img_btn_document_upload_proposer_photo_upload.setClickable(true);
        img_btn_document_upload_proposer_photo_upload.setEnabled(true);
        img_btn_document_upload_proposer_photo_upload
                .setImageDrawable(getResources().getDrawable(
                        R.drawable.ibtn_uploaddoc));

        // camera life assured pic
        img_btn_document_upload_click_image_life_assured_photo
                .setClickable(true);
        img_btn_document_upload_click_image_life_assured_photo.setEnabled(true);
        img_btn_document_upload_click_image_life_assured_photo
                .setImageDrawable(getResources().getDrawable(
                        R.drawable.ibtn_camera));

        // browse life assured pic
        img_btn_document_upload_click_browse_life_assured_photo
                .setClickable(true);
        img_btn_document_upload_click_browse_life_assured_photo
                .setEnabled(true);
        img_btn_document_upload_click_browse_life_assured_photo
                .setImageDrawable(getResources().getDrawable(
                        R.drawable.ibtn_browsedoc));

        // upload life assured pic
        img_btn_document_upload_life_assured_photo_upload.setClickable(true);
        img_btn_document_upload_life_assured_photo_upload.setEnabled(true);
        img_btn_document_upload_life_assured_photo_upload
                .setImageDrawable(getResources().getDrawable(
                        R.drawable.ibtn_uploaddoc));
    }

    private void show_photo(File f, String name) {

        final Dialog d = new Dialog(context);
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);
        d.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.BLACK));

        d.setContentView(R.layout.dialog_show_photo);
        TextView txt_show_photo_title = (TextView) d
                .findViewById(R.id.txt_show_photo_title);
        ImageButton ib_show_photo_image = (ImageButton) d
                .findViewById(R.id.ib_show_photo_image);

        txt_show_photo_title.setText(name);

        Bitmap bmp = BitmapFactory.decodeFile(f.getAbsolutePath());
        Bitmap b = null;
        Bitmap mFaceBitmap = null;
        Uri imageUri = Uri.fromFile(new File(f.toString()));
        /*
         * try { b = MediaStore.Images.Media.getBitmap( getContentResolver(),
         * imageUri); mFaceBitmap = b.copy(Bitmap.Config.RGB_565, true);
         * b.recycle(); } catch (FileNotFoundException e) { e.printStackTrace();
         * } catch (IOException e) { e.printStackTrace(); }
         */

        /* if (mFaceBitmap != null) { */
        /*
         * Bitmap scaled = Bitmap.createScaledBitmap(bmp, 230, 200, true);
         */
        ib_show_photo_image.setImageURI(imageUri);

        d.show();
    }

    @Override
    protected void onDestroy() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }

        if (lifeAssuredSignService != null) {
            lifeAssuredSignService.cancel(true);
        }

        if (uploadThirdPartySignService != null) {
            uploadThirdPartySignService.cancel(true);
        }
        if (uploadAppointeeSignService != null) {
            uploadAppointeeSignService.cancel(true);
        }

        if (asyncUploadDoc != null) {
            asyncUploadDoc.cancel(true);
        }
        if (asyncGetBAOnlineURN_smrt != null) {
            asyncGetBAOnlineURN_smrt.cancel(true);
        }

        super.onDestroy();
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

    public File CreateAdhharPDF() {

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

            aadhaarQRFileName = mStorageUtils.createFileToAppSpecificDir(context,str_selected_urn_no + "_AADHAAR_01.pdf");

            Rectangle rect = new Rectangle(594f, 792f);

            Document document = new Document(rect, 50, 50, 50, 50);
            // Document document = new Document(PageSize.A4, 50, 50, 50, 50);
            PdfWriter pdf_writer = PdfWriter.getInstance(document, new FileOutputStream(aadhaarQRFileName.getAbsolutePath()));

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

            if (is1WProductEKYC){
                PdfPTable headertable_1W = new PdfPTable(1);
                headertable_1W.setWidthPercentage(100);
                headertable_1W.setHorizontalAlignment(Element.ALIGN_CENTER);
                Paragraph para_address_1W = new Paragraph("Spouse Deatils", small_bold_for_name);
                para_address_1W.setAlignment(Element.ALIGN_CENTER);
                document.add(headertable_1W);
                // document.add(new LineSeparator());
                document.add(para_img_logo_after_space_1);
                document.add(para_address_1W);
                document.add(para_img_logo_after_space_1);

                if (str_QR_code_Photo_1W_spouse != null && !str_QR_code_Photo_1W_spouse.equals("")) {
                    ByteArrayOutputStream stream_photo = new ByteArrayOutputStream();
                    byte[] fbyt_photo = Base64.decode(str_QR_code_Photo_1W_spouse, 0);
                    Bitmap bitmap_photo = BitmapFactory.decodeByteArray(fbyt_photo, 0,
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
                PdfPTable table_Name_1W = new PdfPTable(2);
                table_Name_1W.setWidthPercentage(100);

                PdfPCell Name_cell_1_1W = new PdfPCell(new Paragraph("Name", small_normal));
                PdfPCell Name_cell_2_1W = new PdfPCell(new Paragraph(str_ekyc_code_Name_1W_spouse, small_bold));
                Name_cell_1_1W.setPadding(5);
                Name_cell_2_1W.setPadding(5);
                Name_cell_2_1W.setHorizontalAlignment(Element.ALIGN_CENTER);
                table_Name_1W.addCell(Name_cell_1_1W);
                table_Name_1W.addCell(Name_cell_2_1W);
                document.add(table_Name_1W);

                PdfPTable table_Gender_1W = new PdfPTable(2);
                table_Gender_1W.setWidthPercentage(100);

                PdfPCell Gender_cell_1_1W = new PdfPCell(new Paragraph("Gender", small_normal));
                PdfPCell Gender_cell_2_1W = new PdfPCell(new Paragraph(str_ekyc_code_Gender_1W_spouse, small_bold));
                Gender_cell_1_1W.setPadding(5);
                Gender_cell_2_1W.setPadding(5);
                Gender_cell_2_1W.setHorizontalAlignment(Element.ALIGN_CENTER);
                table_Gender_1W.addCell(Gender_cell_1_1W);
                table_Gender_1W.addCell(Gender_cell_2_1W);
                document.add(table_Gender_1W);

                PdfPTable table_DOB_1W = new PdfPTable(2);
                table_DOB_1W.setWidthPercentage(100);

                PdfPCell DOB_cell_1_1W = new PdfPCell(new Paragraph("DOB/YOB", small_normal));
                PdfPCell DOB_cell_2_1W = new PdfPCell(new Paragraph(str_ekyc_code_DOB_1W_spouse, small_bold));
                DOB_cell_1_1W.setPadding(5);
                DOB_cell_2_1W.setPadding(5);
                DOB_cell_2_1W.setHorizontalAlignment(Element.ALIGN_CENTER);
                table_DOB_1W.addCell(DOB_cell_1_1W);
                table_DOB_1W.addCell(DOB_cell_2_1W);
                document.add(table_DOB_1W);

                PdfPTable table_Address_1W= new PdfPTable(2);
                table_Address_1W.setWidthPercentage(100);

                PdfPCell Address_cell_1_1W = new PdfPCell(new Paragraph("Address", small_normal));
                PdfPCell Address_cell_2_1W = new PdfPCell(new Paragraph(str_ekyc_code_address_1W_spouse, small_bold));
                Address_cell_1_1W.setPadding(5);
                Address_cell_2_1W.setPadding(5);
                Address_cell_2_1W.setHorizontalAlignment(Element.ALIGN_CENTER);
                table_Address_1W.addCell(Address_cell_1_1W);
                table_Address_1W.addCell(Address_cell_2_1W);
                document.add(table_Address_1W);
            }

            document.close();

            return aadhaarQRFileName;

        } catch (Exception e) {
            Log.i("Error", e.toString() + "Error in creating pdf");
            return null;
        }
    }

    private final class OkOnClickListener implements
            DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialog, int which) {

            if (Flag == 0) {

                if (deleteOTPSelfDeclarationDocument == 1) {

                    if (OTPSelfDeclarationFileName != null) {

                        if (OTPSelfDeclarationFileName.exists()) {
                            OTPSelfDeclarationFileName.delete();

                            img_btn_document_upload_preview_image_otp_self_declaration
                                    .setVisibility(View.INVISIBLE);
                            img_btn_document_delete_otp_self_declaration
                                    .setVisibility(View.INVISIBLE);

                            img_btn_document_upload_click_image_otp_self_declaration
                                    .setImageDrawable(getResources().getDrawable(
                                            R.drawable.ibtn_camera));
                            img_btn_document_upload_click_browse_otp_self_declaration
                                    .setImageDrawable(getResources().getDrawable(
                                            R.drawable.ibtn_browsedoc));
                            //SelfAttestedDocumentActivity.lst_OTPSelfDeclarationBitmap.clear();

                            deleteOTPSelfDeclarationDocument = 0;
                        } else
                            commonMethods.showToast(context, "File Not Found..");
                    } else {
                        commonMethods.showToast(context, "please capture or browse document");
                    }
                } else if (deleteAgeDocument == 1) {


                    if (ageProofFileName != null) {
                        if (ageProofFileName.exists()) {

                            ageProofFileName.delete();

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
                        } else
                            commonMethods.showToast(context, "File Not Found..");
                    } else {
                        commonMethods.showToast(context, "please capture or browse document");
                    }

                } else if (deleteAddressDocument == 1) {

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
                        } else
                            commonMethods.showToast(context, "File Not Found..");
                    } else {
                        commonMethods.showToast(context, "please capture or browse document");
                    }

                } else if (deleteIncomeDocument == 1) {

                    if (incomeProofFileName != null) {
                        if (incomeProofFileName.exists()) {

                            incomeProofFileName.delete();

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
                            //SelfAttestedDocumentActivity.lst_IncomeBitmap.clear();
                            deleteIncomeDocument = 0;
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
                            //SelfAttestedDocumentActivity.lst_IdentityBitmap.clear();
                            deleteIdentityDocument = 0;
                        } else
                            commonMethods.showToast(context, "File Not Found..");
                    } else {
                        commonMethods.showToast(context, "please capture or browse document");
                    }

                } else if (deleteBankDocument == 1) {

                    if (bankProofFileName != null) {
                        if (bankProofFileName.exists()) {

                            bankProofFileName.delete();

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
                        } else
                            commonMethods.showToast(context, "File Not Found..");
                    } else {
                        commonMethods.showToast(context, "please capture or browse document");
                    }

                } else if (deleteeftDocument == 1) {

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
                            deleteeftDocument = 0;
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
                        } else
                            commonMethods.showToast(context, "File Not Found..");
                    } else {
                        commonMethods.showToast(context, "please capture or browse document");
                    }
                } else if (deleteCustomerPhoto == 1) {

                    if (photo != null) {
                        if (photo.exists()) {

                            photo.delete();

                            img_btn_document_upload_preview_customer_photo
                                    .setVisibility(View.INVISIBLE);
                            img_btn_document_delete_customer_photo
                                    .setVisibility(View.INVISIBLE);
                            img_btn_document_upload_click_image_customer_photo
                                    .setImageDrawable(getResources().getDrawable(
                                            R.drawable.ibtn_camera));
                            img_btn_document_upload_click_browse_customer_photo
                                    .setImageDrawable(getResources().getDrawable(
                                            R.drawable.ibtn_browsedoc));
                            //SelfAttestedDocumentActivity.lst_CustomerPhotoBitmap.clear();
                            deleteCustomerPhoto = 0;
                        } else
                            commonMethods.showToast(context, "File Not Found..");
                    } else {
                        commonMethods.showToast(context, "please capture or browse document");
                    }
                } else if (deleteProposerPhoto == 1) {

                    if (proposerBrowsedPhotoFile != null) {
                        if (proposerBrowsedPhotoFile.exists()) {

                            proposerBrowsedPhotoFile.delete();

                            img_btn_document_upload_preview_proposer_photo
                                    .setVisibility(View.INVISIBLE);
                            img_btn_document_delete_proposer_photo
                                    .setVisibility(View.INVISIBLE);
                            img_btn_document_upload_click_image_proposer_photo
                                    .setImageDrawable(getResources().getDrawable(
                                            R.drawable.ibtn_camera));
                            img_btn_document_upload_click_browse_proposer_photo
                                    .setImageDrawable(getResources().getDrawable(
                                            R.drawable.ibtn_browsedoc));
                            ReducedProposerPhotoBitmap = photoBitmap = null;
                            deleteProposerPhoto = 0;
                        } else
                            commonMethods.showToast(context, "File Not Found..");
                    } else {
                        commonMethods.showToast(context, "please capture or browse document");
                    }
                } else if (deleteLifeAssuredPhoto == 1) {

                    if (lifeAssuredBrowsedPhotoFile != null) {
                        if (lifeAssuredBrowsedPhotoFile.exists()) {

                            lifeAssuredBrowsedPhotoFile.delete();

                            img_btn_document_upload_preview_life_assured_photo
                                    .setVisibility(View.INVISIBLE);
                            img_btn_document_delete_life_assured_photo
                                    .setVisibility(View.INVISIBLE);
                            img_btn_document_upload_click_image_life_assured_photo
                                    .setImageDrawable(getResources().getDrawable(
                                            R.drawable.ibtn_camera));
                            img_btn_document_upload_click_browse_life_assured_photo
                                    .setImageDrawable(getResources().getDrawable(
                                            R.drawable.ibtn_browsedoc));
                            ReducedLifeAssuredPhotoBitmap = lifeAssuredBitmap = null;
                            deleteLifeAssuredPhoto = 0;
                        } else
                            commonMethods.showToast(context, "File Not Found..");
                    } else {
                        commonMethods.showToast(context, "please capture or browse document");
                    }
                } else {
                    if (uploadFlag == 1) {
                        dispatchTakePictureIntent("0");
                    } else if (uploadFlag == 0) {
                        onBrowse();
                    }
                }
            } else {
                commonMethods.showToast(context, "else");
            }
        }
    }

    private final class CancelOnClickListener implements
            DialogInterface.OnClickListener {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            if (!(deleteOTPSelfDeclarationDocument == 1)
                    || !(deleteAgeDocument == 1)
                    || !(deleteAddressDocument == 1)
                    || !(deleteIncomeDocument == 1)
                    || !(deleteIdentityDocument == 1)
                    || !(deleteOtherDocument == 1)
                    || !(deleteBankDocument == 1) || !(deleteeftDocument == 1)
                    || !(deleteCustomerPhoto == 1)
                    || !(deleteProposerPhoto == 1)
                    || !(deleteLifeAssuredPhoto == 1)) {

                if (photoBitmap != null) {
                    int flag_cancel_proposer_photo = 1;
                }

                if (lifeAssuredBitmap != null) {
                    int flag_cancel_life_assured_photo = 1;
                }

                if (SelfAttestedDocumentActivity.lst_OTPSelfDeclarationBitmap
                        .size() >= 1) {
                    flag_cancel_otp_self_declaration = 1;
                    increment = 25;

                    SelfAttestedDocumentActivity obj = new SelfAttestedDocumentActivity();
                    obj.createDocumentPdf(
                            increment,
                            " OTPSelfDeclaration",
                            "",
                            SelfAttestedDocumentActivity.lst_OTPSelfDeclarationBitmap,
                            "");
                }

                if (SelfAttestedDocumentActivity.lst_AgeBitmap.size() >= 1) {
                    flag_cancel_age = 1;
                    increment = 1;

                    SelfAttestedDocumentActivity obj = new SelfAttestedDocumentActivity();
                    obj.createDocumentPdf(increment, " AgeProof",
                            spnr_document_upload_document_age.getSelectedItem()
                                    .toString(),
                            SelfAttestedDocumentActivity.lst_AgeBitmap, "");
                }

                if (SelfAttestedDocumentActivity.lst_IdentityBitmap.size() >= 1) {
                    flag_cancel_identity = 1;
                    increment = 2;
                    SelfAttestedDocumentActivity obj = new SelfAttestedDocumentActivity();
                    obj.createDocumentPdf(increment, " IdentityProof",
                            spnr_document_upload_document_identity
                                    .getSelectedItem().toString(),
                            SelfAttestedDocumentActivity.lst_IdentityBitmap, "");
                }

                if (SelfAttestedDocumentActivity.lst_AddressBitmap.size() >= 1) {
                    flag_cancel_address = 1;
                    increment = 3;
                    SelfAttestedDocumentActivity obj = new SelfAttestedDocumentActivity();
                    obj.createDocumentPdf(increment, " AddressProof",
                            spnr_document_upload_document_address
                                    .getSelectedItem().toString(),
                            SelfAttestedDocumentActivity.lst_AddressBitmap, "");

                }

                if (SelfAttestedDocumentActivity.lst_IncomeBitmap.size() >= 1) {
                    flag_cancel_income = 1;
                    increment = 4;
                    SelfAttestedDocumentActivity obj = new SelfAttestedDocumentActivity();
                    obj.createDocumentPdf(increment, " IncomeProof",
                            spnr_document_upload_document_income
                                    .getSelectedItem().toString(),
                            SelfAttestedDocumentActivity.lst_IncomeBitmap, "");
                }

                if (SelfAttestedDocumentActivity.lst_OtherBitmap.size() >= 1) {
                    flag_cancel_others = 1;
                    increment = 5;
                    SelfAttestedDocumentActivity obj = new SelfAttestedDocumentActivity();
                    obj.createDocumentPdf(increment, " OtherProof", "",
                            SelfAttestedDocumentActivity.lst_OtherBitmap, "");
                }

                if (SelfAttestedDocumentActivity.lst_BankBitmap.size() >= 1) {
                    flag_cancel_bank = 1;
                    increment = 6;
                    SelfAttestedDocumentActivity obj = new SelfAttestedDocumentActivity();
                    obj.createDocumentPdf(increment, " BankProof",
                            spnr_document_upload_document_bank
                                    .getSelectedItem().toString(),
                            SelfAttestedDocumentActivity.lst_BankBitmap, "");
                }

                if (SelfAttestedDocumentActivity.lst_EFTBitmap.size() >= 1) {
                    flag_cancel_eft = 1;
                    increment = 7;
                    SelfAttestedDocumentActivity obj = new SelfAttestedDocumentActivity();
                    obj.createDocumentPdf(increment, " Payment By",
                            spnr_document_upload_document_eft.getSelectedItem()
                                    .toString(),
                            SelfAttestedDocumentActivity.lst_EFTBitmap, "");
                }

                if (SelfAttestedDocumentActivity.lst_CustomerPhotoBitmap.size() >= 1) {
                    flag_cancel_customer_photo = 1;
                    increment = 8;
                    SelfAttestedDocumentActivity obj = new SelfAttestedDocumentActivity();
                    obj.createDocumentPdf(
                            increment,
                            " Customer Photo",
                            "",
                            SelfAttestedDocumentActivity.lst_CustomerPhotoBitmap,
                            "");
                }
            }
            dialog.dismiss();
        }
    }

    class UploadScannedQRDetails extends AsyncTask<String, String, String> {

        private volatile boolean running = true;

        @Override
        protected void onPreExecute() {
            mProgressDialog = new ProgressDialog(context, ProgressDialog.THEME_HOLO_LIGHT);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.setTitle(Html.fromHtml("<font color='#00a1e3'><b>Please Wait...<b></font>"));
            mProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                running = true;

                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME_UPLOAD_QR_CODE_DETAILS);

                request.addProperty("xmlStr", strings[0]);

                //System.out.println("request.toString():" + request.toString());

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;

                envelope.setOutputSoapObject(request);

                commonMethods.TLSv12Enable();

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);

                HttpTransportSE androidHttpTranport = new HttpTransportSE(ServiceURL.SERVICE_URL);

                androidHttpTranport.call(SOAP_ACTION_UPLOAD_QR_CODE_DETAILS, envelope);

                SoapPrimitive sa = null;

                sa = (SoapPrimitive) envelope.getResponse();
                return sa.toString();

            } catch (Exception e) {
                e.printStackTrace();
                running = false;
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            if (mProgressDialog.isShowing())
                mProgressDialog.dismiss();

            if (running) {

                if (s.equals("1") || s.equals("2")) {

                    if (!is1WProduct && !is1WProductEKYC) {
                        switch (qrCodeClick) {
                            case 1:
                                llAgeAadhaarScan.setVisibility(View.VISIBLE);
                                llAgeQR.setVisibility(View.GONE);
                                break;

                            case 2:
                                llIdentityAadhaarScan.setVisibility(View.VISIBLE);
                                llIdentityQR.setVisibility(View.GONE);
                                break;

                            case 3:
                                llAddressAadhaarScan.setVisibility(View.VISIBLE);
                                llAddressQR.setVisibility(View.GONE);
                                break;
                        }

                        if (s.equals("2"))
                            commonMethods.showToast(context, "Data already exists..");

                        qrCodeClick = 0;
                    }
                } else if (s.equals("0")) {
                    commonMethods.showToast(context, "QR Data insertion failed. Try agian later..");
                }

            } else {
                commonMethods.showToast(context, "Please check Your Internet Connection..");
            }
        }
    }

    class UploadLifeAssuredSignService extends
            AsyncTask<String, String, String> {
        private static final String SOAP_ACTION_UPLOAD_FILE_SERVICE_URN = "http://tempuri.org/UploadFile_URN";
        private static final String METHOD_NAME_UPLOAD_FILE_SERVICE_URN = "UploadFile_URN";
        int flag = 0;
        private volatile boolean running = true;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // showDialog(DIALOG_DOWNLOAD_PROGRESS);
            mProgressDialog = new ProgressDialog(context,
                    ProgressDialog.THEME_HOLO_LIGHT);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog
                    .setTitle(Html
                            .fromHtml("<font color='#00a1e3'><b>Please Wait...<b></font>"));

            mProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... aurl) {

            try {
                String lifeAssuredSignName = str_selected_urn_no
                        + "_cust2sign.png";

                byte[] custSign_bytes;
                if (latestImage.equals("lifeAssuredSignBrowse")) {
                    commonMethods.compressImage(
                            lifeAssuredBrowsedSignFile, str_selected_urn_no,
                            "_cust2sign.png",context);
                    File lifeAssuredSignFile = mStorageUtils.createFileToAppSpecificDir(context,
                            lifeAssuredSignName);
                    custSign_bytes = commonMethods.read(lifeAssuredSignFile);
                } else {
                    File customerSignFile = mStorageUtils.createFileToAppSpecificDir(context,
                            lifeAssuredSignName);
                    commonMethods.compressImage(customerSignFile,
                            str_selected_urn_no, "_cust2sign.png",context);

                    custSign_bytes = commonMethods.read(customerSignFile);
                }

                running = true;

                SoapObject request = new SoapObject(NAMESPACE,
                        METHOD_NAME_UPLOAD_FILE_SERVICE_URN);

                request.addProperty("f",
                        org.kobjects.base64.Base64.encode(custSign_bytes));
                request.addProperty("fileName", lifeAssuredSignName);


                System.out.println("request.toString():" + request.toString());

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);
                envelope.dotNet = true;

                envelope.setOutputSoapObject(request);

                // allowAllSSL();
                commonMethods.TLSv12Enable();

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);

                HttpTransportSE androidHttpTranport = new HttpTransportSE(ServiceURL.SERVICE_URL);

                androidHttpTranport.call(SOAP_ACTION_UPLOAD_FILE_SERVICE_URN,
                        envelope);

                SoapPrimitive sa = null;

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

                    Toast.makeText(context, "Details Sync Succesfully",
                            Toast.LENGTH_LONG).show();

                } else {

                    Toast.makeText(context, "Details not Synced",
                            Toast.LENGTH_LONG).show();
                }
                Intent i = new Intent(context, DocumentsUploadActivity.class);
                startActivity(i);
                finish();

            } else {
                Intent i = new Intent(context, DocumentsUploadActivity.class);
                startActivity(i);
                finish();
            }
        }
    }

    /*private void pan_card_submitted() {

        // camera age
        img_btn_document_upload_click_image_age.setClickable(false);
        img_btn_document_upload_click_image_age.setEnabled(false);
        img_btn_document_upload_click_image_age.setImageDrawable(getResources()
                .getDrawable(R.drawable.checkedcamera));

        // browse age
        img_btn_document_upload_click_browse_age.setClickable(false);
        img_btn_document_upload_click_browse_age.setEnabled(false);
        img_btn_document_upload_click_browse_age
                .setImageDrawable(getResources().getDrawable(
                        R.drawable.checkedbrowse));

        // upload age
        img_btn_document_upload_age_upload.setClickable(false);
        img_btn_document_upload_age_upload.setEnabled(false);
        img_btn_document_upload_age_upload.setImageDrawable(getResources()
                .getDrawable(R.drawable.checkedupload));

        // spinner age
        spnr_document_upload_document_age.setClickable(false);
        spnr_document_upload_document_age.setEnabled(false);

        img_btn_document_upload_click_image_identity.setClickable(false);
        img_btn_document_upload_click_image_identity.setEnabled(false);
        img_btn_document_upload_click_image_identity
                .setImageDrawable(getResources().getDrawable(
                        R.drawable.checkedcamera));

        img_btn_document_upload_click_browse_identity.setClickable(false);
        img_btn_document_upload_click_browse_identity.setEnabled(false);
        img_btn_document_upload_click_browse_identity
                .setImageDrawable(getResources().getDrawable(
                        R.drawable.checkedbrowse));

        img_btn_document_upload_identity_upload.setClickable(false);
        img_btn_document_upload_identity_upload.setEnabled(false);
        img_btn_document_upload_identity_upload.setImageDrawable(getResources()
                .getDrawable(R.drawable.checkedupload));

        spnr_document_upload_document_identity.setClickable(false);
        spnr_document_upload_document_identity.setEnabled(false);
    }*/

    /*private void aadhar_card_submitted() {

        // camera age
        img_btn_document_upload_click_image_age.setClickable(false);
        img_btn_document_upload_click_image_age.setEnabled(false);
        img_btn_document_upload_click_image_age.setImageDrawable(getResources()
                .getDrawable(R.drawable.checkedcamera));

        // browse age
        img_btn_document_upload_click_browse_age.setClickable(false);
        img_btn_document_upload_click_browse_age.setEnabled(false);
        img_btn_document_upload_click_browse_age
                .setImageDrawable(getResources().getDrawable(
                        R.drawable.checkedbrowse));

        // upload age
        img_btn_document_upload_age_upload.setClickable(false);
        img_btn_document_upload_age_upload.setEnabled(false);
        img_btn_document_upload_age_upload.setImageDrawable(getResources()
                .getDrawable(R.drawable.checkedupload));

        // spinner age
        spnr_document_upload_document_age.setClickable(false);
        spnr_document_upload_document_age.setEnabled(false);

        // camera identity
        img_btn_document_upload_click_image_identity.setClickable(false);
        img_btn_document_upload_click_image_identity.setEnabled(false);
        img_btn_document_upload_click_image_identity
                .setImageDrawable(getResources().getDrawable(
                        R.drawable.checkedcamera));

        // browse identity
        img_btn_document_upload_click_browse_identity.setClickable(false);
        img_btn_document_upload_click_browse_identity.setEnabled(false);
        img_btn_document_upload_click_browse_identity
                .setImageDrawable(getResources().getDrawable(
                        R.drawable.checkedbrowse));

        // upload identity
        img_btn_document_upload_identity_upload.setClickable(false);
        img_btn_document_upload_identity_upload.setEnabled(false);
        img_btn_document_upload_identity_upload.setImageDrawable(getResources()
                .getDrawable(R.drawable.checkedupload));

        // spinner identity
        spnr_document_upload_document_identity.setClickable(false);
        spnr_document_upload_document_identity.setEnabled(false);

        // camera address
        img_btn_document_upload_click_image_address.setClickable(false);
        img_btn_document_upload_click_image_address.setEnabled(false);
        img_btn_document_upload_click_image_address
                .setImageDrawable(getResources().getDrawable(
                        R.drawable.checkedcamera));

        // browse address
        img_btn_document_upload_click_browse_address.setClickable(false);
        img_btn_document_upload_click_browse_address.setEnabled(false);
        img_btn_document_upload_click_browse_address
                .setImageDrawable(getResources().getDrawable(
                        R.drawable.checkedbrowse));

        // upload address
        img_btn_document_upload_address_upload.setClickable(false);
        img_btn_document_upload_address_upload.setEnabled(false);
        img_btn_document_upload_address_upload.setImageDrawable(getResources()
                .getDrawable(R.drawable.checkedupload));

        // spinner address
        spnr_document_upload_document_address.setClickable(false);
        spnr_document_upload_document_address.setEnabled(false);
    }*/

    class UploadThirdPartySignService extends AsyncTask<String, String, String> {

        private static final String SOAP_ACTION_UPLOAD_FILE_SERVICE_URN = "http://tempuri.org/UploadFile_URN";
        private static final String METHOD_NAME_UPLOAD_FILE_SERVICE_URN = "UploadFile_URN";
        int flag = 0;
        private volatile boolean running = true;
        private String inputpolicylist = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // showDialog(DIALOG_DOWNLOAD_PROGRESS);
            mProgressDialog = new ProgressDialog(context,
                    ProgressDialog.THEME_HOLO_LIGHT);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog
                    .setTitle(Html
                            .fromHtml("<font color='#00a1e3'><b>Please Wait...<b></font>"));
            mProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... aurl) {

            try {
                String thirdyPartySignName = str_selected_urn_no
                        + "_thirdParty.png";

                byte[] custSign_bytes;
                if (latestImage.equals("thirdPartyBrowsedFile")) {
                    commonMethods.compressImage(
                            thirdPartyBrowsedSignFile, str_selected_urn_no,
                            "_thirdParty.png",context);
                    File thirdyPartySignFile = mStorageUtils.createFileToAppSpecificDir(context,
                            thirdyPartySignName);
                    custSign_bytes = commonMethods.read(thirdyPartySignFile);
                } else {
                    File thirdyPartySignFile = mStorageUtils.createFileToAppSpecificDir(context,
                            thirdyPartySignName);
                    commonMethods.compressImage(thirdyPartySignFile,
                            str_selected_urn_no, "_thirdParty.png",context);

                    custSign_bytes = commonMethods
                            .read(thirdyPartySignFile);
                }

                running = true;

                SoapObject request = new SoapObject(NAMESPACE,
                        METHOD_NAME_UPLOAD_FILE_SERVICE_URN);

                request.addProperty("f",
                        org.kobjects.base64.Base64.encode(custSign_bytes));
                request.addProperty("fileName", thirdyPartySignName);

                System.out.println("request.toString():" + request.toString());

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);
                envelope.dotNet = true;

                envelope.setOutputSoapObject(request);

                // allowAllSSL();
                commonMethods.TLSv12Enable();

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);

                HttpTransportSE androidHttpTranport = new HttpTransportSE(ServiceURL.SERVICE_URL);

                androidHttpTranport.call(SOAP_ACTION_UPLOAD_FILE_SERVICE_URN,
                        envelope);

                SoapPrimitive sa = null;

                sa = (SoapPrimitive) envelope.getResponse();
                inputpolicylist = sa.toString();

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
                    Toast.makeText(context, "Details Sync Succesfully",
                            Toast.LENGTH_LONG).show();
                    Intent i = new Intent(context,
                            DocumentsUploadActivity.class);
                    startActivity(i);
                    finish();

                } else {

                    Toast.makeText(context, "Details not Synced",
                            Toast.LENGTH_LONG).show();
                    Intent i = new Intent(context,
                            DocumentsUploadActivity.class);
                    startActivity(i);
                    finish();
                }

            } else {
                Intent i = new Intent(context, DocumentsUploadActivity.class);
                startActivity(i);
                finish();
            }
        }
    }

    class UploadAppointeeSignService extends AsyncTask<String, String, String> {

        private static final String SOAP_ACTION_UPLOAD_FILE_SERVICE_URN = "http://tempuri.org/UploadFile_URN";
        private static final String METHOD_NAME_UPLOAD_FILE_SERVICE_URN = "UploadFile_URN";
        int flag = 0;
        private volatile boolean running = true;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // showDialog(DIALOG_DOWNLOAD_PROGRESS);
            mProgressDialog = new ProgressDialog(context,
                    ProgressDialog.THEME_HOLO_LIGHT);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog
                    .setTitle(Html
                            .fromHtml("<font color='#00a1e3'><b>Please Wait...<b></font>"));
            mProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... aurl) {

            try {
                String appointeeSignName = str_selected_urn_no
                        + "_appointee.png";

                byte[] appointeeSign_bytes;
                if (latestImage.equals("AppointeeBrowsedFile")) {
                    commonMethods.compressImage(AppointeeBrowsedSignFile,
                            str_selected_urn_no, "_appointee.png",context);
                    File thirdyPartySignFile = mStorageUtils.createFileToAppSpecificDir(context,
                            appointeeSignName);
                    appointeeSign_bytes = commonMethods
                            .read(thirdyPartySignFile);
                } else {
                    File thirdyPartySignFile = mStorageUtils.createFileToAppSpecificDir(context,
                            appointeeSignName);
                    commonMethods.compressImage(thirdyPartySignFile,
                            str_selected_urn_no, "_appointee.png",context);

                    appointeeSign_bytes = commonMethods.read(thirdyPartySignFile);
                }

                running = true;

                SoapObject request = new SoapObject(NAMESPACE,
                        METHOD_NAME_UPLOAD_FILE_SERVICE_URN);

                request.addProperty("f",
                        org.kobjects.base64.Base64.encode(appointeeSign_bytes));
                request.addProperty("fileName", appointeeSignName);

                System.out.println("request.toString():" + request.toString());

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);
                envelope.dotNet = true;

                envelope.setOutputSoapObject(request);

                // allowAllSSL();
                commonMethods.TLSv12Enable();

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);

                HttpTransportSE androidHttpTranport = new HttpTransportSE(ServiceURL.SERVICE_URL);

                androidHttpTranport.call(SOAP_ACTION_UPLOAD_FILE_SERVICE_URN,
                        envelope);

                SoapPrimitive sa = null;

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
        protected void onProgressUpdate(String... progress) {
            Log.d("ANDRO_ASYNC", progress[0]);
        }

        @Override
        protected void onPostExecute(String unused) {
            if (mProgressDialog.isShowing())
                mProgressDialog.dismiss();

            if (running) {

                if (flag == 1) {
                    Toast.makeText(context, "Details Sync Succesfully",
                            Toast.LENGTH_LONG).show();
                    Intent i = new Intent(context,
                            DocumentsUploadActivity.class);
                    startActivity(i);
                    finish();

                } else {
                    Toast.makeText(context, "Details not Synced",
                            Toast.LENGTH_LONG).show();

                    Intent i = new Intent(context,
                            DocumentsUploadActivity.class);
                    startActivity(i);
                    finish();
                }

            } else {
                Intent i = new Intent(context, DocumentsUploadActivity.class);
                startActivity(i);
                finish();
            }
        }
    }

    public class AsyncUploadDoc extends AsyncTask<String, Void, String> {
        private volatile boolean running = true;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub

            mProgressDialog = new ProgressDialog(context,
                    ProgressDialog.THEME_HOLO_LIGHT);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog
                    .setTitle(Html
                            .fromHtml("<font color='#00a1e3'><b>Please Wait...<b></font>"));
            mProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... param) {
            // TODO Auto-generated method stub

            if (commonMethods.isNetworkConnected(context)) {

                running = true;

                SoapObject request = new SoapObject(NAMESPACE,
                        METHOD_NAME_UPLOAD_FILE_SERVICE_URN);

                request.addProperty("f", bytes);
                switch (Check) {

                    case "OFFLINE_EKYC": request.addProperty("fileName", str_selected_urn_no + "_AADHAAR_01.pdf");
                        break;

                    case "ProposerPhoto":
                        request.addProperty("fileName", str_selected_urn_no
                                + "_cust1Photo" + str_extension);
                        break;
                    case "LifeAssuredPhoto":
                        request.addProperty("fileName", str_selected_urn_no
                                + "_cust2Photo" + str_extension);
                        break;
                    default:
                        if (increment == 10 || increment == 25) {
                            request.addProperty("fileName", str_selected_urn_no
                                    + "_X" + increment + str_extension);
                        } else {

                            String strfileName = "";

                            //condition for parivartan age , identity, address
                            if (increment == 1 || increment == 2 || increment == 3) {

                                if (strUploadDocument
                                        .equalsIgnoreCase("Aadhar card with complete DOB")
                                        || strUploadDocument
                                        .equalsIgnoreCase("Aadhar card with incomplete DOB")
                                        || strUploadDocument.equalsIgnoreCase("Aadhar Card")) {

                                    //for parivartan requirement upload of aadhar card
                                    strfileName = str_selected_urn_no
                                            + "_X0" + 101 + str_extension;
                                } else if (strUploadDocument.equalsIgnoreCase("Pancard")) {
                                    //for parivartan requirement upload of pan card
                                    strfileName = str_selected_urn_no
                                            + "_X0" + 102 + str_extension;
                                } else
                                    strfileName = str_selected_urn_no
                                            + "_X0" + increment + str_extension;

                            } else {
                                strfileName = str_selected_urn_no
                                        + "_X0" + increment + str_extension;
                            }

                            request.addProperty("fileName", strfileName);
                        }
                        break;
                }

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);
                envelope.dotNet = true;

                new MarshalBase64().register(envelope); // serialization

                envelope.setOutputSoapObject(request);

                commonMethods.TLSv12Enable();

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);

                HttpTransportSE androidHttpTranport = new HttpTransportSE(ServiceURL.SERVICE_URL, 50000);
                try {
                    androidHttpTranport.call(
                            SOAP_ACTION_UPLOAD_FILE_SERVICE_URN, envelope);
                    Object response = envelope.getResponse();
                    strUploadDocument = response.toString();

                    // SoapPrimitive sa = (SoapPrimitive)
                    // envelope.getResponse();
                } catch (Exception e) {
                    e.printStackTrace();
                    running = false;
                    return "server not responding..";
                }

            } else
                return "Please Activate Internet connection";

            return null;
            // }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (mProgressDialog.isShowing())
                mProgressDialog.dismiss();

            if (running) {
                if (strUploadDocument.contains("1")) {

                    Toast.makeText(context, "Document Uploaded SuccessFully!!",
                            Toast.LENGTH_SHORT).show();

                    photoBitmap = lifeAssuredBitmap = null;

                    SelfAttestedDocumentActivity.lst_OTPSelfDeclarationBitmap
                            .clear();
                    SelfAttestedDocumentActivity.lst_AgeBitmap.clear();
                    SelfAttestedDocumentActivity.lst_IdentityBitmap.clear();
                    SelfAttestedDocumentActivity.lst_IncomeBitmap.clear();
                    SelfAttestedDocumentActivity.lst_AddressBitmap.clear();
                    SelfAttestedDocumentActivity.lst_OtherBitmap.clear();
                    SelfAttestedDocumentActivity.lst_BankBitmap.clear();
                    SelfAttestedDocumentActivity.lst_EFTBitmap.clear();
                    SelfAttestedDocumentActivity.lst_CustomerPhotoBitmap
                            .clear();
                    SelfAttestedDocumentActivity.lst_CommAddressBitmap.clear();

                    isBrowseCapture = 0;

                    // DeleteFiles();

                    if (Check.equals("OFFLINE_EKYC")){

                        String strQRData = "";

                        if(is1WProductEKYC){

                            offlineEkycData();

                            strQRData = "<QRCODE>" + str1WFirstEKYC +
                                    "<add_Aadhar_qr_prop_name>" + str_ekyc_code_Name + "</add_Aadhar_qr_prop_name>" +
                                    "<add_Aadhar_qr_prop_dob>" + str_ekyc_code_DOB + "</add_Aadhar_qr_prop_dob>" +
                                    "<add_Aadhar_qr_prop_gender>" + str_ekyc_code_Gender + "</add_Aadhar_qr_prop_gender>" +
                                    "<add_Aadhar_qr_prop_addres>" + str_ekyc_code_address + "</add_Aadhar_qr_prop_addres>" +
                                    "<add_Aadhar_qr_prop_email>" + str_ekyc_code_mailID + "</add_Aadhar_qr_prop_email>" +
                                    "<add_Aadhar_qr_prop_mobile>" + str_ekyc_code_mobile + "</add_Aadhar_qr_prop_mobile>" +
                                    "<add_Aadhar_is_ekyc>Y</add_Aadhar_is_ekyc>" +
                                    "</QRCODE>";

                            is1WProductEKYC = false;

                        }else{

                            strQRData = "<QRCODE>" + offlineEkycData() +
                                    "<add_Aadhar_qr_prop_name></add_Aadhar_qr_prop_name>" +
                                    "<add_Aadhar_qr_prop_dob></add_Aadhar_qr_prop_dob>" +
                                    "<add_Aadhar_qr_prop_gender></add_Aadhar_qr_prop_gender>" +
                                    "<add_Aadhar_qr_prop_addres></add_Aadhar_qr_prop_addres>" +
                                    "<add_Aadhar_qr_prop_email></add_Aadhar_qr_prop_email>" +
                                    "<add_Aadhar_qr_prop_mobile></add_Aadhar_qr_prop_mobile>" +
                                    "<add_Aadhar_is_ekyc>Y</add_Aadhar_is_ekyc>" +
                                    "</QRCODE>";
                        }

                        new UploadScannedQRDetails().execute(strQRData);

                    } else if (Check.equals("ProposerPhoto")) {

                        // preview proposer photo
                        img_btn_document_upload_preview_proposer_photo
                                .setVisibility(View.INVISIBLE);

                        // delete proposer photo
                        img_btn_document_delete_proposer_photo
                                .setVisibility(View.INVISIBLE);

						/*// camera proposer photo
						img_btn_document_upload_click_image_proposer_photo
								.setClickable(false);
						img_btn_document_upload_click_image_proposer_photo
								.setEnabled(false);
						img_btn_document_upload_click_image_proposer_photo
								.setImageDrawable(getResources().getDrawable(
										R.drawable.checkedcamera));

						// browse proposer photo
						img_btn_document_upload_click_browse_proposer_photo
								.setClickable(false);
						img_btn_document_upload_click_browse_proposer_photo
								.setEnabled(false);
						img_btn_document_upload_click_browse_proposer_photo
								.setImageDrawable(getResources().getDrawable(
										R.drawable.checkedbrowse));*/

                        // upload proposer photo
                        img_btn_document_upload_proposer_photo_upload
                                .setClickable(false);
                        img_btn_document_upload_proposer_photo_upload
                                .setEnabled(false);
                        img_btn_document_upload_proposer_photo_upload
                                .setImageDrawable(getResources().getDrawable(
                                        R.drawable.checkedupload));
                    }

                    if (Check.equals("LifeAssuredPhoto")) {

                        // preview life assured photo
                        img_btn_document_upload_preview_life_assured_photo
                                .setVisibility(View.INVISIBLE);

                        // delete life assured photo
                        img_btn_document_delete_life_assured_photo
                                .setVisibility(View.INVISIBLE);

						/*// camera life assured photo
						img_btn_document_upload_click_image_life_assured_photo
								.setClickable(false);
						img_btn_document_upload_click_image_life_assured_photo
								.setEnabled(false);
						img_btn_document_upload_click_image_life_assured_photo
								.setImageDrawable(getResources().getDrawable(
										R.drawable.checkedcamera));*/

						/*// browse life assured photo
						img_btn_document_upload_click_browse_life_assured_photo
								.setClickable(false);
						img_btn_document_upload_click_browse_life_assured_photo
								.setEnabled(false);
						img_btn_document_upload_click_browse_life_assured_photo
								.setImageDrawable(getResources().getDrawable(
										R.drawable.checkedbrowse));*/

                        // upload life assured photo
                        img_btn_document_upload_life_assured_photo_upload
                                .setClickable(false);
                        img_btn_document_upload_life_assured_photo_upload
                                .setEnabled(false);
                        img_btn_document_upload_life_assured_photo_upload
                                .setImageDrawable(getResources().getDrawable(
                                        R.drawable.checkedupload));
                    }

                    boolean flag_address_proof_submitted = false;
                    boolean flag_identity_proof_submitted = false;
                    boolean flag_age_proof_submitted = false;
                    if (increment == 25) {// otp self declaration
                        String DocumentName = "OTPSelfDeclaration";
                        strOCRDocumentNames += "OTPSelfDeclaration, ";

                        flag_cancel_otp_self_declaration = 0;
                        img_btn_document_upload_click_image_otp_self_declaration
                                .setClickable(false);
                        img_btn_document_upload_click_image_otp_self_declaration
                                .setEnabled(false);

                        img_btn_document_upload_click_browse_otp_self_declaration
                                .setClickable(false);
                        img_btn_document_upload_click_browse_otp_self_declaration
                                .setEnabled(false);

                        img_btn_document_upload_otp_self_declaration_upload
                                .setClickable(false);
                        img_btn_document_upload_otp_self_declaration_upload
                                .setEnabled(false);

                        img_btn_document_upload_preview_image_otp_self_declaration
                                .setVisibility(View.INVISIBLE);
                        img_btn_document_delete_otp_self_declaration
                                .setVisibility(View.INVISIBLE);

                        img_btn_document_upload_otp_self_declaration_upload
                                .setImageDrawable(getResources().getDrawable(
                                        R.drawable.checkedupload));
                    } else if (increment == 1) {//Age

                        String DocumentName = spnr_document_upload_document_age
                                .getSelectedItem().toString();
                        strOCRDocumentNames += spnr_document_upload_document_age
                                .getSelectedItem().toString() + ", ";

                        flag_cancel_age = 0;

                        /*if (DocumentName
                                .equalsIgnoreCase("Aadhar card with complete DOB")
                                || DocumentName
                                .equalsIgnoreCase("Aadhar card with incomplete DOB")) {

                            aadhar_card_submitted();

                            flag_age_proof_submitted = true;
                            flag_identity_proof_submitted = true;
                            flag_address_proof_submitted = true;

                        } else if (DocumentName.equalsIgnoreCase("Pancard")) {

                            pan_card_submitted();

                            flag_age_proof_submitted = true;
                            flag_identity_proof_submitted = true;
                        } else {*/
                        img_btn_document_upload_age_upload
                                .setClickable(false);
                        img_btn_document_upload_age_upload
                                .setEnabled(false);

                        img_btn_document_upload_age_upload
                                .setImageDrawable(getResources()
                                        .getDrawable(
                                                R.drawable.checkedupload));
                        //}

                        img_btn_document_upload_preview_image_age
                                .setVisibility(View.INVISIBLE);
                        img_btn_document_delete_age
                                .setVisibility(View.INVISIBLE);

                    } else if (increment == 2) {// Identity

                        String DocumentName = spnr_document_upload_document_identity
                                .getSelectedItem().toString();
                        strOCRDocumentNames += spnr_document_upload_document_identity
                                .getSelectedItem().toString() + ", ";
                        flag_cancel_identity = 0;

                        /*if (DocumentName.equalsIgnoreCase("Aadhar Card")) {
                            aadhar_card_submitted();
                            flag_age_proof_submitted = true;
                            flag_identity_proof_submitted = true;
                            flag_address_proof_submitted = true;
                        } else if (DocumentName.equalsIgnoreCase("Pancard")) {
                            pan_card_submitted();
                            flag_age_proof_submitted = true;
                            flag_identity_proof_submitted = true;
                        }*/

                        img_btn_document_upload_preview_image_identity
                                .setVisibility(View.INVISIBLE);
                        img_btn_document_delete_identity
                                .setVisibility(View.INVISIBLE);

                        img_btn_document_upload_identity_upload
                                .setClickable(false);
                        img_btn_document_upload_identity_upload
                                .setEnabled(false);
                        img_btn_document_upload_identity_upload
                                .setImageDrawable(getResources().getDrawable(
                                        R.drawable.checkedupload));

                    } else if (increment == 3) {// Address
                        String DocumentName = spnr_document_upload_document_address
                                .getSelectedItem().toString();

                        strOCRDocumentNames += spnr_document_upload_document_address
                                .getSelectedItem().toString() + ",";
                        flag_cancel_address = 0;

                        /*if (DocumentName.equalsIgnoreCase("Aadhar Card")) {
                            aadhar_card_submitted();
                            flag_age_proof_submitted = true;
                            flag_identity_proof_submitted = true;
                            flag_address_proof_submitted = true;
                        }*/

                        img_btn_document_upload_click_preview_image_address
                                .setVisibility(View.INVISIBLE);
                        img_btn_document_delete_address
                                .setVisibility(View.INVISIBLE);

                        img_btn_document_upload_address_upload
                                .setClickable(false);
                        img_btn_document_upload_address_upload
                                .setEnabled(false);
                        img_btn_document_upload_address_upload
                                .setImageDrawable(getResources().getDrawable(
                                        R.drawable.checkedupload));

                    } else if (increment == 4) {
                        String DocumentName = spnr_document_upload_document_income
                                .getSelectedItem().toString();

                        strOCRDocumentNames += spnr_document_upload_document_income
                                .getSelectedItem().toString() + ",";

                        flag_cancel_income = 0;
                        img_btn_document_upload_click_image_income
                                .setClickable(false);
                        img_btn_document_upload_click_image_income
                                .setEnabled(false);

                        img_btn_document_upload_click_browse_income
                                .setClickable(false);
                        img_btn_document_upload_click_browse_income
                                .setEnabled(false);

                        img_btn_document_upload_income_upload
                                .setClickable(false);
                        img_btn_document_upload_income_upload.setEnabled(false);

                        spnr_document_upload_document_income
                                .setClickable(false);
                        spnr_document_upload_document_income.setEnabled(false);

                        img_btn_document_upload_click_preview_image_income
                                .setVisibility(View.INVISIBLE);
                        img_btn_document_delete_income
                                .setVisibility(View.INVISIBLE);

                        img_btn_document_upload_income_upload
                                .setImageDrawable(getResources().getDrawable(
                                        R.drawable.checkedupload));

                    } else if (increment == 5) {
                        String DocumentName = edt_document_upload_document_others
                                .getText().toString();

                        strOCRDocumentNames += edt_document_upload_document_others
                                .getText().toString() + ",";

                        flag_cancel_others = 0;
                        img_btn_document_upload_click_image_others
                                .setClickable(false);
                        img_btn_document_upload_click_image_others
                                .setEnabled(false);

                        img_btn_document_upload_click_browse_Others
                                .setClickable(false);
                        img_btn_document_upload_click_browse_Others
                                .setEnabled(false);

                        img_btn_document_upload_Others_upload
                                .setClickable(false);
                        img_btn_document_upload_Others_upload.setEnabled(false);

                        edt_document_upload_document_others.setClickable(false);
                        edt_document_upload_document_others.setEnabled(false);

                        img_btn_document_upload_click_preview_image_others
                                .setVisibility(View.INVISIBLE);
                        img_btn_document_delete_Others
                                .setVisibility(View.INVISIBLE);

                        img_btn_document_upload_Others_upload
                                .setImageDrawable(getResources().getDrawable(
                                        R.drawable.checkedupload));

                    } else if (increment == 6) {
                        String DocumentName = spnr_document_upload_document_bank
                                .getSelectedItem().toString();

                        strOCRDocumentNames += spnr_document_upload_document_bank
                                .getSelectedItem().toString() + ",";

                        flag_cancel_income = 0;
                        img_btn_document_upload_click_image_bank
                                .setClickable(false);
                        img_btn_document_upload_click_image_bank
                                .setEnabled(false);

                        img_btn_document_upload_click_browse_bank
                                .setClickable(false);
                        img_btn_document_upload_click_browse_bank
                                .setEnabled(false);

                        img_btn_document_upload_bank_upload.setClickable(false);
                        img_btn_document_upload_bank_upload.setEnabled(false);

                        spnr_document_upload_document_bank.setClickable(false);
                        spnr_document_upload_document_bank.setEnabled(false);

                        img_btn_document_upload_click_preview_image_bank
                                .setVisibility(View.INVISIBLE);
                        img_btn_document_delete_bank
                                .setVisibility(View.INVISIBLE);

                        img_btn_document_upload_bank_upload
                                .setImageDrawable(getResources().getDrawable(
                                        R.drawable.checkedupload));

                    } else if (increment == 7) {
                        String DocumentName = spnr_document_upload_document_eft
                                .getSelectedItem().toString();

                        strOCRDocumentNames += spnr_document_upload_document_eft
                                .getSelectedItem().toString() + ",";

                        flag_cancel_eft = 0;
                        img_btn_document_upload_click_image_eft
                                .setClickable(false);
                        img_btn_document_upload_click_image_eft
                                .setEnabled(false);

                        img_btn_document_upload_click_browse_eft
                                .setClickable(false);
                        img_btn_document_upload_click_browse_eft
                                .setEnabled(false);

                        img_btn_document_upload_eft_upload.setClickable(false);
                        img_btn_document_upload_eft_upload.setEnabled(false);

                        spnr_document_upload_document_eft.setClickable(false);
                        spnr_document_upload_document_eft.setEnabled(false);

                        img_btn_document_upload_preview_image_eft
                                .setVisibility(View.INVISIBLE);
                        img_btn_document_delete_eft
                                .setVisibility(View.INVISIBLE);

                        img_btn_document_upload_eft_upload
                                .setImageDrawable(getResources().getDrawable(
                                        R.drawable.checkedupload));

                    } else if (increment == 8) {
                        /*
                         * String DocumentName =
                         * spnr_document_upload_document_eft
                         * .getSelectedItem().toString();
                         */
                        String DocumentName = "Customer Photo";

                        strOCRDocumentNames += "Customer Photo,";

                        flag_cancel_customer_photo = 0;
                        img_btn_document_upload_click_image_customer_photo
                                .setClickable(false);
                        img_btn_document_upload_click_image_customer_photo
                                .setEnabled(false);

                        img_btn_document_upload_click_browse_customer_photo
                                .setClickable(false);
                        img_btn_document_upload_click_browse_customer_photo
                                .setEnabled(false);

                        img_btn_document_upload_customer_photo_upload
                                .setClickable(false);
                        img_btn_document_upload_customer_photo_upload
                                .setEnabled(false);

                        img_btn_document_upload_preview_customer_photo
                                .setVisibility(View.INVISIBLE);
                        img_btn_document_delete_customer_photo
                                .setVisibility(View.INVISIBLE);

                        img_btn_document_upload_customer_photo_upload
                                .setImageDrawable(getResources().getDrawable(
                                        R.drawable.checkedupload));

                    }
                } else {
                    Toast.makeText(context, "Server not responding..",
                            Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(context, "Server not responding..",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    class AsyncGetBAOnlineURN_SMRT extends AsyncTask<String, String, String> {

        private static final String SOAP_ACTION_GET_BA_ONLINE_URN = "http://tempuri.org/getBAOnlineURN_SMRT";
        private static final String METHOD_NAME_GET_BA_ONLINE_URN = "getBAOnlineURN_SMRT";

        int flag = 0;
        ArrayList<String> URNNumberList;
        private volatile boolean running = true;
        private String inputpolicylist = "";


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // showDialog(DIALOG_DOWNLOAD_PROGRESS);

            mProgressDialog = new ProgressDialog(context,
                    ProgressDialog.THEME_HOLO_LIGHT);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog
                    .setTitle(Html
                            .fromHtml("<font color='#00a1e3'><b>Please Wait...<b></font>"));

            mProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... aurl) {

            try {
                running = true;

                SoapObject request = new SoapObject(NAMESPACE,
                        METHOD_NAME_GET_BA_ONLINE_URN);
                request.addProperty("strCode", commonMethods.setUserDetails(context).getStrCIFBDMUserId());

                System.out.println("request.toString():" + request.toString());

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);

                HttpTransportSE androidHttpTranport = new HttpTransportSE(ServiceURL.SERVICE_URL);

                androidHttpTranport.call(SOAP_ACTION_GET_BA_ONLINE_URN, envelope);

                SoapPrimitive sa = null;

                sa = (SoapPrimitive) envelope.getResponse();
                inputpolicylist = sa.toString();

                if (inputpolicylist.equalsIgnoreCase("0")) {
                    flag = 0;
                } else {
                    flag = 1;
                    ParseXML prsObj = new ParseXML();
                    inputpolicylist = prsObj.parseXmlTag(inputpolicylist, "NewDataSet");

                    if (inputpolicylist != null) {
                        List<String> Node = prsObj.parseParentNode(
                                inputpolicylist, "Table");
                        if (Node.size() > 0) {
                            URNNumberList = prsObj.parseURNNumberBAOnline(Node);
                        }
                    }
                }

            } catch (Exception e) {
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

                    fillSpinnerValue(spinnerUrnList, URNNumberList);
                } else {
                    commonMethods.showMessageDialog(context, "URN Numbers are not Available");
                    linearLayoutParivartanDocumentsUpload
                            .setVisibility(View.GONE);
                }

            } else {

                commonMethods.showMessageDialog(context, "URN Numbers are not Available");
                linearLayoutParivartanDocumentsUpload
                        .setVisibility(View.GONE);
               /* Intent i = new Intent(context, DocumentsUploadActivity.class);
                context.startActivity(i);
                str_selected_urn_no = "";
                finish();*/
            }
        }
    }
}
