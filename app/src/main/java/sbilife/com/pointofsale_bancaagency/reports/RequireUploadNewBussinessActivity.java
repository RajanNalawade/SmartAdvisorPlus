package sbilife.com.pointofsale_bancaagency.reports;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.text.Html;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.MarshalBase64;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import sbilife.com.pointofsale_bancaagency.Element_TextView_BaseAdapter;
import sbilife.com.pointofsale_bancaagency.ParseXML;
import sbilife.com.pointofsale_bancaagency.R;
import sbilife.com.pointofsale_bancaagency.ServiceURL;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;
import sbilife.com.pointofsale_bancaagency.common.StorageUtils;
import sbilife.com.pointofsale_bancaagency.utility.AlertDialogMessage;
import sbilife.com.pointofsale_bancaagency.utility.SelfAttestedDocumentActivity;

public class RequireUploadNewBussinessActivity extends AppCompatActivity {

    private Spinner spnr_document_upload_document_age, spnr_document_upload_document_identity, spnr_document_upload_document_address;

    private ImageButton img_btn_document_upload_preview_image_age, img_btn_document_delete_age,
            img_btn_document_upload_click_image_age, img_btn_document_upload_click_browse_age,
            img_btn_document_upload_age_upload,
            img_btn_document_upload_preview_image_identity, img_btn_document_delete_identity , img_btn_document_upload_click_image_identity,
            img_btn_document_upload_click_browse_identity, img_btn_document_upload_identity_upload,
            img_btn_document_upload_click_preview_image_address, img_btn_document_delete_address, img_btn_document_upload_click_image_address,
            img_btn_document_upload_click_browse_address, img_btn_document_upload_address_upload;

    private Context mContext;
    private CommonMethods mCommonMethods;
    private StorageUtils mStorageUtils;

    private int Flag;
    private int increment;
    private int deleteAgeDocument;
    private int deleteAddressDocument;
    private int deleteIdentityDocument;
    private int flag_cancel_age = 0;
    private int flag_cancel_address = 0;
    private int flag_cancel_identity = 0;
    private int uploadFlag;

    private static File f;
    private File imageF;
    private File AgeProofFileName;
    private File IdentityProofFileName;
    private File AddressProofFileName;
    private Bitmap ageProofBitmap, identityProofBitmap, addressProofBitmap;

    private final String NAMESPACE = "http://tempuri.org/";
    private final String URL = ServiceURL.SERVICE_URL;

    private static final String SOAP_ACTION_VALIDATE_QUATATION_SERVICE_GROUPS = "http://tempuri.org/validateQuotID";
    private static final String METHOD_NAME_VALIDATE_QUATATION_SERVICE_GROUPS = "validateQuotID";

    private static final String SOAP_ACTION_UPLOAD_FILE_SERVICE_GROUPS = "http://tempuri.org/UploadFile_Groups";
    private static final String METHOD_NAME_UPLOAD_FILE_SERVICE_GROUPS = "UploadFile_Groups";

    private static final String JPEG_FILE_PREFIX = "IMG_";
    private static final String JPEG_FILE_SUFFIX = ".jpg";

    private final int REQUEST_CODE_CAPTURE_FILE = 3;
    private final int REQUEST_CODE_BROWSE_FILE = 2;

    private static final int DIALOG_ALERT = 10;
    private static final int DIALOG_ALERT_ANOTHER_DOCS = 20;

    private String mCurrentPhotoPath = "", Check = "", strUploadDocument = "", str_quotation_no = "";

    private static String strPathOfFile = "";

    private byte[] bytes = null;

    private static Bitmap ReducedAgeBitmap;
    private static Bitmap ReducedIdentityBitmap;
    private static Bitmap ReducedAddressBitmap;
    private ProgressDialog mProgressDialog;

    private Button btn_require_upload_quote_no_ok;
    private EditText edt_require_upload_quote_no;
    private LinearLayout linearLayoutParivartanDocumentsUpload;

    //add multiple mail ids seperated by ';'
    private static final String EMAIL_REQUIRE_UPLOAD_NEW_BUSINESS = "NB_Sampoornsuraksha@sbilife.co.in;";
    private static final String EMAIL_FUND_NEW_BUSINESS = "Milind.Ahire@sbilife.co.in;Sunil.Narayanan@sbilife.co.in;";

    private int email_type = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_require_upload_new_bussiness);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);

        initialisation();
        Flag = 0;

        // age
        ArrayList<String> upload_age_doc_name = new ArrayList<>();
        switch (email_type){
            case 1:
                upload_age_doc_name.add("Select Requirement");
                upload_age_doc_name.add("Proposal Form");
                upload_age_doc_name.add("Contribution Form");
                upload_age_doc_name.add("Source of Lead Approval");
                upload_age_doc_name.add("Quote sheet duly signed by the MPH and the SBI Life Official");
                upload_age_doc_name.add("Pan Card");
                upload_age_doc_name.add("Form 60(IF PAN not applicable)");
                upload_age_doc_name.add("Bank Account Details with cancelled cheque copy");
                upload_age_doc_name.add("Premium Shortage");
                upload_age_doc_name.add("Member Data");
                upload_age_doc_name.add("Declaration of Non Employer Employee");
                upload_age_doc_name.add("Copy of Registration for Societies");
                upload_age_doc_name.add("Others");
                break;

            case 2:
                upload_age_doc_name.add("Select Requirement");
                upload_age_doc_name.add("Proposal Form");
                upload_age_doc_name.add("Contribution Form");
                upload_age_doc_name.add("Source of Lead Approval");
                upload_age_doc_name.add("Quote sheet duly signed by the MPH and the SBI Life Official");
                upload_age_doc_name.add("Pan Card");
                upload_age_doc_name.add("Bank Account Details with cancelled cheque copy");
                upload_age_doc_name.add("Member Data");
                upload_age_doc_name.add("Trust Deed / Rules (for GRATUITY & SA)");
                upload_age_doc_name.add("Leave Rules (for LE Policy)");
                upload_age_doc_name.add("Board Resolution");
                upload_age_doc_name.add("FATCA forms");
                upload_age_doc_name.add("Individual FATCA forms ( If member residing outside India)");
                upload_age_doc_name.add("Deed if variation if any");
                upload_age_doc_name.add("Others");
                break;
        }
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
        fillSpinnerValue(spnr_document_upload_document_identity, upload_identity_doc_name);

        setBtnListenerOrDisable(img_btn_document_upload_click_image_age,
                AgeProofOnClickListener, MediaStore.ACTION_IMAGE_CAPTURE);
        setBtnListenerOrDisable(img_btn_document_upload_click_image_identity,
                IdentityProofOnClickListener, MediaStore.ACTION_IMAGE_CAPTURE);
        setBtnListenerOrDisable(img_btn_document_upload_click_image_address,
                AddressProofOnClickListener, MediaStore.ACTION_IMAGE_CAPTURE);

        btn_require_upload_quote_no_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                str_quotation_no = edt_require_upload_quote_no.getText().toString().trim();

                if (!str_quotation_no.equals("")) {
                    ClearDocumentsPDF();
                    linearLayoutParivartanDocumentsUpload.setVisibility(View.GONE);

                    new AsyncValidateQuatation().execute();

                } else
                    mCommonMethods.showToast(mContext, "please enter quotation no.");
            }
        });
    }

    private View.OnClickListener AgeProofOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!str_quotation_no.equals("")) {
                ClearDocumentsPDF();
                uploadFlag = 1;
                img_btn_document_upload_click_browse_age
                        .setImageDrawable(getResources().getDrawable(
                                R.drawable.ibtn_browsedoc));
                Check = "AgeProof";
                dispatchTakePictureIntent("0");
            } else
                mCommonMethods.showToast(mContext, "please select urn");

        }
    };

    private View.OnClickListener IdentityProofOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if (!str_quotation_no.equals("")) {
                ClearDocumentsPDF();

                uploadFlag = 1;
                img_btn_document_upload_click_browse_identity
                        .setImageDrawable(getResources().getDrawable(
                                R.drawable.ibtn_browsedoc));
                Check = "IdentityProof";
                dispatchTakePictureIntent("0");
            } else
                mCommonMethods.showToast(mContext, "please select urn");

        }
    };

    private View.OnClickListener AddressProofOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!str_quotation_no.equals("")) {
                ClearDocumentsPDF();
                uploadFlag = 1;
                img_btn_document_upload_click_browse_address
                        .setImageDrawable(getResources().getDrawable(
                                R.drawable.ibtn_browsedoc));
                Check = "AddressProof";

                dispatchTakePictureIntent("0");
            } else
                mCommonMethods.showToast(mContext, "please select urn");

        }
    };

    private void ClearDocumentsPDF() {
        SelfAttestedDocumentActivity.lst_AgeBitmap.clear();
        SelfAttestedDocumentActivity.lst_IdentityBitmap.clear();
        SelfAttestedDocumentActivity.lst_AddressBitmap.clear();
    }

    private void initialisation(){

        mContext = this;
        mStorageUtils = new StorageUtils();
        mCommonMethods = new CommonMethods();
        mCommonMethods.setApplicationToolbarMenu(this,"Groups New Bussiness Requirement Upload"); ;

        email_type = getIntent().getIntExtra("EMAIL_TYPE", 0);

        switch (email_type){
            case 1:mCommonMethods.setApplicationToolbarMenu(this,"Group NB Requirement Upload");
                break;
            case 2:
                mCommonMethods.setApplicationToolbarMenu(this,"Fund NB Requirement Upload");
                break;
            default:
                break;
        }

        linearLayoutParivartanDocumentsUpload = findViewById(R.id.linearLayoutParivartanDocumentsUpload);

        edt_require_upload_quote_no = findViewById(R.id.edt_require_upload_quote_no);
        btn_require_upload_quote_no_ok = findViewById(R.id.btn_require_upload_quote_no_ok);

        //age
        spnr_document_upload_document_age = findViewById(R.id.spnr_document_upload_document_age);
        img_btn_document_upload_preview_image_age = findViewById(R.id.img_btn_document_upload_preview_image_age);
        img_btn_document_delete_age = findViewById(R.id.img_btn_document_delete_age);
        img_btn_document_upload_click_image_age = findViewById(R.id.img_btn_document_upload_click_image_age);
        img_btn_document_upload_click_browse_age = findViewById(R.id.img_btn_document_upload_click_browse_age);
        img_btn_document_upload_age_upload = findViewById(R.id.img_btn_document_upload_age_upload);

        //identity
        spnr_document_upload_document_identity = findViewById(R.id.spnr_document_upload_document_identity);
        img_btn_document_upload_preview_image_identity = findViewById(R.id.img_btn_document_upload_preview_image_identity);
        img_btn_document_delete_identity = findViewById(R.id.img_btn_document_delete_identity);
        img_btn_document_upload_click_image_identity = findViewById(R.id.img_btn_document_upload_click_image_identity);
        img_btn_document_upload_click_browse_identity = findViewById(R.id.img_btn_document_upload_click_browse_identity);
        img_btn_document_upload_identity_upload = findViewById(R.id.img_btn_document_upload_identity_upload);

        //address
        spnr_document_upload_document_address = findViewById(R.id.spnr_document_upload_document_address);
        img_btn_document_upload_click_preview_image_address = findViewById(R.id.img_btn_document_upload_click_preview_image_address);
        img_btn_document_delete_address = findViewById(R.id.img_btn_document_delete_address);
        img_btn_document_upload_click_image_address = findViewById(R.id.img_btn_document_upload_click_image_address);
        img_btn_document_upload_click_browse_address = findViewById(R.id.img_btn_document_upload_click_browse_address);
        img_btn_document_upload_address_upload = findViewById(R.id.img_btn_document_upload_address_upload);
    }

    private void fillSpinnerValue(Spinner spinner, List<String> value_list) {

        Element_TextView_BaseAdapter retd_adapter = new Element_TextView_BaseAdapter(
                RequireUploadNewBussinessActivity.this, value_list);
        spinner.setAdapter(retd_adapter);
    }

    public void onPreview_ageProof(View v) throws IOException {

        File f = mStorageUtils.createFileToAppSpecificDir(
                mContext, str_quotation_no + "_" + "X" + 1 + "." + "pdf");

        mCommonMethods.openAllDocs(mContext, f);
    }

    public void onPreview_IdentityProof(View v) throws IOException {

        File f = mStorageUtils.createFileToAppSpecificDir(
                mContext, str_quotation_no + "_" + "X" + 2 + "." + "pdf");

        mCommonMethods.openAllDocs(mContext, f);
    }

    public void onPreview_AddressProof(View v) throws IOException {

        File f = mStorageUtils.createFileToAppSpecificDir(
                mContext, str_quotation_no + "_" + "X" + 3 + "." + "pdf");

        mCommonMethods.openAllDocs(mContext, f);
    }

    /*private void openwith(File File) {

        if (File.exists()) {// Checking for the file is exist or not
            Uri path = Uri.fromFile(File);
            Intent objIntent = new Intent(Intent.ACTION_VIEW);
            objIntent.setDataAndType(path, "application/pdf");
            objIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(objIntent);// Staring the pdf viewer
        } else {
            Toast.makeText(mContext, "file does not exists", Toast.LENGTH_SHORT)
                    .show();
        }
    }*/

    public void onDelete_ageProof(View v) {
        deleteAgeDocument = 1;
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

    private final class AddYesOnClickListener implements
            DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {

            spnr_document_upload_document_age.setSelection(0);

            img_btn_document_upload_age_upload
                    .setClickable(true);
            img_btn_document_upload_age_upload
                    .setEnabled(true);

            img_btn_document_upload_click_image_age.setImageDrawable(getResources()
                    .getDrawable(
                            R.drawable.ibtn_camera));

            img_btn_document_upload_click_browse_age.setImageDrawable(getResources()
                    .getDrawable(
                            R.drawable.ibtn_browsedoc));

            img_btn_document_upload_age_upload
                    .setImageDrawable(getResources()
                            .getDrawable(
                                    R.drawable.ibtn_uploaddoc));

            img_btn_document_upload_preview_image_age
                    .setVisibility(View.INVISIBLE);
            img_btn_document_delete_age
                    .setVisibility(View.INVISIBLE);
        }
    }

    private final class AddNoOnClickListener implements
            DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            dialogInterface.dismiss();
        }
    }

    private final class OkOnClickListener implements
            DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialog, int which) {

            if (Flag == 0) {

                if (deleteAgeDocument == 1) {
                    img_btn_document_upload_preview_image_age
                            .setVisibility(View.INVISIBLE);
                    img_btn_document_delete_age.setVisibility(View.INVISIBLE);

                    img_btn_document_upload_click_image_age
                            .setImageDrawable(getResources().getDrawable(
                                    R.drawable.ibtn_camera));
                    img_btn_document_upload_click_browse_age
                            .setImageDrawable(getResources().getDrawable(
                                    R.drawable.ibtn_browsedoc));
                    SelfAttestedDocumentActivity.lst_AgeBitmap.clear();
                    deleteAgeDocument = 0;
                } else if (deleteAddressDocument == 1) {
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

                } else if (deleteIdentityDocument == 1) {
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
                    SelfAttestedDocumentActivity.lst_IdentityBitmap.clear();
                    deleteIdentityDocument = 0;

                } else {
                    if (uploadFlag == 1) {
                        dispatchTakePictureIntent("0");
                    } else if (uploadFlag == 0) {
                        onBrowse();
                    }
                }
            } else {
				/*
				 * commonMethods = new CommonMethods();
				 *
				 * Intent intent = new Intent(context, CRActivity.class);
				 * intent.putExtra("QuatationNumber", QuatationNumber);
				 * intent.putExtra("ProposerNumber", proposalNumber);
				 * startActivity(intent); Flag = 0;
				 */
                mCommonMethods.showToast(mContext, "else");
            }
        }
    }

    private final class CancelOnClickListener implements
            DialogInterface.OnClickListener {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            if (!(deleteAgeDocument == 1)
                    || !(deleteAddressDocument == 1)
                    || !(deleteIdentityDocument == 1)) {

                if (SelfAttestedDocumentActivity.lst_AgeBitmap.size() >= 1) {
                    flag_cancel_age = 1;
                    increment = 1;

                    SelfAttestedDocumentActivity obj = new SelfAttestedDocumentActivity();

                    switch (email_type){
                        case 1:
                            obj.createDocumentPdf_groups(increment, "Requirement Upload New Business ",
                                    spnr_document_upload_document_age.getSelectedItem()
                                            .toString(),
                                    SelfAttestedDocumentActivity.lst_AgeBitmap, str_quotation_no);
                            break;

                        case 2:
                            obj.createDocumentPdf_groups(increment, "Requirement Upload New Business Fund ",
                                    spnr_document_upload_document_age.getSelectedItem()
                                            .toString(),
                                    SelfAttestedDocumentActivity.lst_AgeBitmap, str_quotation_no);

                            break;
                    }
                }

                if (SelfAttestedDocumentActivity.lst_IdentityBitmap.size() >= 1) {
                    flag_cancel_identity = 1;
                    increment = 2;
                    SelfAttestedDocumentActivity obj = new SelfAttestedDocumentActivity();
                    obj.createDocumentPdf_groups(increment, "IdentityProof",
                            spnr_document_upload_document_identity
                                    .getSelectedItem().toString(),
                            SelfAttestedDocumentActivity.lst_IdentityBitmap, str_quotation_no);
                }

                if (SelfAttestedDocumentActivity.lst_AddressBitmap.size() >= 1) {
                    flag_cancel_address = 1;
                    increment = 3;
                    SelfAttestedDocumentActivity obj = new SelfAttestedDocumentActivity();

                    switch (email_type){
                        case 1:
                            obj.createDocumentPdf_groups(increment, "Requirement Upload New Business ",
                                    spnr_document_upload_document_address
                                            .getSelectedItem().toString(),
                                    SelfAttestedDocumentActivity.lst_AddressBitmap, str_quotation_no);
                            break;

                        case 2:
                            obj.createDocumentPdf_groups(increment, "Requirement Upload New Business Fund",
                                    spnr_document_upload_document_address
                                            .getSelectedItem().toString(),
                                    SelfAttestedDocumentActivity.lst_AddressBitmap, str_quotation_no);
                            break;
                    }

                }
            }
            dialog.dismiss();
        }
    }

    private void dispatchTakePictureIntent(String str) {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File f = null;

        try {
            if (str.equals("1")) {
                f = setUpPhotoFile("1");
            } else if (str.equals("2")) {
                f = setUpPhotoFile("2");
            } else if (str.equals("0")) {
                f = setUpPhotoFile("0");
            }

            mCurrentPhotoPath = f.getAbsolutePath();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mCommonMethods.getContentUri(mContext, f));
            }else{
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        startActivityForResult(takePictureIntent, REQUEST_CODE_CAPTURE_FILE);
    }

    private File setUpPhotoFile(String str) throws IOException {

        if (str.equals("1")) {
            f = createImageFile_pics("1");
            mCurrentPhotoPath = f.getAbsolutePath();

        } else if (str.equals("2")) {
            f = createImageFile_pics("2");
            mCurrentPhotoPath = f.getAbsolutePath();
        } else if (str.equals("3")) {
            f = createImageFile_pics("3");
            mCurrentPhotoPath = f.getAbsolutePath();
        } else if (str.equals("0")) {
            f = createImageFile();
            mCurrentPhotoPath = f.getAbsolutePath();
        }
        return f;
    }

    private File createImageFile() throws IOException {
        String imageFileName = JPEG_FILE_PREFIX + str_quotation_no + "_X0"
                + increment + "_" + JPEG_FILE_SUFFIX;
        imageF = mStorageUtils.createFileToAppSpecificDir(mContext, imageFileName );
        return imageF;
    }

    private File createImageFile_pics(String str) {

        String imageFileName = "";
        if (str.equals("1")) {
            imageFileName = str_quotation_no + "_cust1Photo" + JPEG_FILE_SUFFIX;
        } else if (str.equals("2")){
            imageFileName = str_quotation_no + "_cust2Photo" + JPEG_FILE_SUFFIX;
        }else{
            imageFileName = JPEG_FILE_PREFIX + str_quotation_no + "_X"
                    + increment + "_" + JPEG_FILE_SUFFIX;
        }
        imageF = mStorageUtils.createFileToAppSpecificDir(mContext, imageFileName);
        return imageF;
    }

    private void onBrowse() {

        Intent i = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, REQUEST_CODE_BROWSE_FILE);
    }

    public void onBrowse_AgeProof(View v) {
        ClearDocumentsPDF();
        uploadFlag = 0;

        img_btn_document_upload_click_image_age.setImageDrawable(getResources()
                .getDrawable(R.drawable.ibtn_camera));
        Check = "AgeProof";
        onBrowse();
    }

    public void onBrowse_IdentityProof(View v) {
        ClearDocumentsPDF();
        uploadFlag = 0;

        img_btn_document_upload_click_image_identity
                .setImageDrawable(getResources().getDrawable(
                        R.drawable.ibtn_camera));
        Check = "IdentityProof";
        onBrowse();
    }

    public void onBrowse_AddressProof(View v) {
        ClearDocumentsPDF();
        uploadFlag = 0;

        img_btn_document_upload_click_image_address
                .setImageDrawable(getResources().getDrawable(
                        R.drawable.ibtn_camera));
        Check = "AddressProof";
        onBrowse();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_BROWSE_FILE) {
            if (resultCode == AppCompatActivity.RESULT_OK) {

                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.MediaColumns.DATA};

                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String strPathOfFile = cursor.getString(columnIndex);
                cursor.close();

                if (Check.equals("AgeProof")) {
                    AgeProofFileName = new File(strPathOfFile);

                    ageProofBitmap = mCommonMethods.ShrinkBitmap(
                            AgeProofFileName.getAbsolutePath(), 600, 600);

                    boolean isBlurr = false/* Blurr(ageProofBitmap) */;

                    if (!isBlurr) {

                        ReducedAgeBitmap = ageProofBitmap;

                        img_btn_document_upload_preview_image_age
                                .setVisibility(View.VISIBLE);
                        img_btn_document_delete_age.setVisibility(View.VISIBLE);

                        img_btn_document_upload_click_browse_age
                                .setImageDrawable(getResources().getDrawable(
                                        R.drawable.checkedbrowse));

                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        ReducedAgeBitmap.compress(Bitmap.CompressFormat.PNG,
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

                    }

                }
                else if (Check.equals("IdentityProof")) {
                    IdentityProofFileName = new File(strPathOfFile);

                    identityProofBitmap = mCommonMethods.ShrinkBitmap(
                            IdentityProofFileName.getAbsolutePath(), 600, 600);

                    boolean isBlurr = false/* Blurr(identityProofBitmap) */;

                    if (!isBlurr) {

                        ReducedIdentityBitmap = identityProofBitmap;

                        img_btn_document_upload_preview_image_identity
                                .setVisibility(View.VISIBLE);
                        img_btn_document_delete_identity
                                .setVisibility(View.VISIBLE);

                        img_btn_document_upload_click_browse_identity
                                .setImageDrawable(getResources().getDrawable(
                                        R.drawable.checkedbrowse));

                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        ReducedIdentityBitmap.compress(
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

                    }

                }
                else if (Check.equals("AddressProof")) {
                    AddressProofFileName = new File(strPathOfFile);

                    addressProofBitmap = mCommonMethods.ShrinkBitmap(
                            AddressProofFileName.getAbsolutePath(), 600, 600);

                    boolean isBlurr = false/* Blurr(addressProofBitmap) */;

                    if (!isBlurr) {

                        ReducedAddressBitmap = addressProofBitmap;

                        img_btn_document_upload_click_preview_image_address
                                .setVisibility(View.VISIBLE);
                        img_btn_document_delete_address
                                .setVisibility(View.VISIBLE);
                        img_btn_document_upload_click_browse_address
                                .setImageDrawable(getResources().getDrawable(
                                        R.drawable.checkedbrowse));

                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        ReducedAddressBitmap.compress(
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
                    }
                }
            }
        }
        else if (requestCode == REQUEST_CODE_CAPTURE_FILE) {
            if (resultCode == RESULT_OK) {

                if (Check.equals("AgeProof")) {

                    AgeProofFileName = f;

                    ageProofBitmap = mCommonMethods.ShrinkBitmap(
                            AgeProofFileName.getAbsolutePath(), 600, 600);

                    boolean isBlurr = false/* Blurr(ageProofBitmap) */;

                    if (!isBlurr) {

                        ReducedAgeBitmap = ageProofBitmap;

                        img_btn_document_upload_preview_image_age
                                .setVisibility(View.VISIBLE);
                        img_btn_document_delete_age.setVisibility(View.VISIBLE);
                        img_btn_document_upload_click_image_age
                                .setImageDrawable(getResources().getDrawable(
                                        R.drawable.checkedcamera));

                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        ReducedAgeBitmap.compress(Bitmap.CompressFormat.PNG,
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

                    }

                } else if (Check.equals("IdentityProof")) {
                    IdentityProofFileName = f;

                    identityProofBitmap = mCommonMethods.ShrinkBitmap(
                            IdentityProofFileName.getAbsolutePath(), 600, 600);

                    boolean isBlurr = false/* Blurr(identityProofBitmap) */;

                    if (!isBlurr) {

                        ReducedIdentityBitmap = identityProofBitmap;

                        img_btn_document_upload_preview_image_identity
                                .setVisibility(View.VISIBLE);
                        img_btn_document_delete_identity
                                .setVisibility(View.VISIBLE);
                        img_btn_document_upload_click_image_identity
                                .setImageDrawable(getResources().getDrawable(
                                        R.drawable.checkedcamera));

                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        ReducedIdentityBitmap.compress(
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

                    }

                } else if (Check.equals("AddressProof")) {
                    AddressProofFileName = f;

                    addressProofBitmap = mCommonMethods.ShrinkBitmap(
                            AddressProofFileName.getAbsolutePath(), 600, 600);

                    boolean isBlurr = false/* Blurr(addressProofBitmap) */;

                    if (!isBlurr) {

                        ReducedAddressBitmap = addressProofBitmap;

                        img_btn_document_upload_click_preview_image_address
                                .setVisibility(View.VISIBLE);
                        img_btn_document_delete_address
                                .setVisibility(View.VISIBLE);
                        img_btn_document_upload_click_image_address
                                .setImageDrawable(getResources().getDrawable(
                                        R.drawable.checkedcamera));

                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        ReducedAddressBitmap.compress(
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
                    }
                }
            }
        }
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this,
                AlertDialog.THEME_HOLO_LIGHT);
        switch (id) {
            case DIALOG_ALERT:

                builder.setTitle("Confirmation");
                builder.setMessage("Do you want to capture next page of same Document ?");
                builder.setCancelable(false);
                builder.setPositiveButton("Yes", new OkOnClickListener());
                builder.setNegativeButton("No", new CancelOnClickListener());
                AlertDialog dialog = builder.create();
                dialog.show();
                break;

            case DIALOG_ALERT_ANOTHER_DOCS:

                builder.setTitle("Document Upload");
                builder.setMessage("Do you want to add Another Document ?");
                builder.setCancelable(false);
                builder.setPositiveButton("Yes", new AddYesOnClickListener());
                builder.setNegativeButton("No", new AddNoOnClickListener());
                final AlertDialog dialog1 = builder.create();
                dialog1.show();
                break;

            default:
                break;
        }
        return super.onCreateDialog(id);
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

    /*private File galleryAddPic() {

        Intent mediaScanIntent = new Intent(
                "android.intent.action.MEDIA_SCANNER_SCAN_FILE");
        f = new File(mCurrentPhotoPath);
        strPathOfFile = mCurrentPhotoPath;

        String docName = f.getName();

        Intent imageIntent = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE);
        File myDir = new File("/mnt/sdcard/alpha/cnsbank/images");
        myDir.mkdirs();
        File image = new File(myDir, docName);

        //nought changes
        Uri contentUri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            imageIntent.putExtra(MediaStore.EXTRA_OUTPUT, mCommonMethods.getContentUri(mContext, image));

            contentUri = mCommonMethods.getContentUri(mContext, f);
        }else{
            imageIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(image));

            contentUri = Uri.fromFile(f);
        }

        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);

        return f;
    }*/

    public void onUpload_AgeProof1(View v) {
        if (SelfAttestedDocumentActivity.lst_AgeBitmap != null) {
            if (SelfAttestedDocumentActivity.lst_AgeBitmap.size() >= 1
                    && flag_cancel_age == 1) {

                if (!(spnr_document_upload_document_age.getSelectedItem()
                        .toString()).equalsIgnoreCase("Select Requirement")
                        && ageProofBitmap != null) {

                    String DocumentName = spnr_document_upload_document_age
                            .getSelectedItem().toString();

                    increment = 1;

                    f = mStorageUtils.createFileToAppSpecificDir(mContext,str_quotation_no + "_" + "X" + increment
                            + "." + "pdf");

                    uploaddoc(f);
                } else {

                    String message = "";
                    if (spnr_document_upload_document_age.getSelectedItem()
                            .toString().equalsIgnoreCase("Select Requirement")) {
                        message = "Please Select Requirement Document First";
                    } else if (ageProofBitmap == null) {
                        message = "Please Capture Or Browse The Requirement Document First";
                    }
                    AlertDialogMessage obj = new AlertDialogMessage();
                    obj.dialog(this, message, true);

                }
            } else {
                mCommonMethods.showToast(mContext, "Please Capture Or Browse The Requirement Document First");

                SelfAttestedDocumentActivity.lst_AgeBitmap.clear();
                SelfAttestedDocumentActivity.lst_IdentityBitmap.clear();
                SelfAttestedDocumentActivity.lst_AddressBitmap.clear();
            }
        } else {
            SelfAttestedDocumentActivity.lst_AgeBitmap.clear();
            SelfAttestedDocumentActivity.lst_IdentityBitmap.clear();
            SelfAttestedDocumentActivity.lst_AddressBitmap.clear();
        }
    }

    public void onUpload_IdentityProof1(View v) {
        if (SelfAttestedDocumentActivity.lst_IdentityBitmap != null) {
            if (SelfAttestedDocumentActivity.lst_IdentityBitmap.size() >= 1
                    && flag_cancel_identity == 1) {

                if (!(spnr_document_upload_document_identity.getSelectedItem()
                        .toString()).equalsIgnoreCase("Select Document")
                        && (identityProofBitmap != null)) {

                    String DocumentName = spnr_document_upload_document_identity
                            .getSelectedItem().toString();
                    increment = 2;
                    f = mStorageUtils.createFileToAppSpecificDir(mContext, str_quotation_no + "_" + "X" + increment
                            + "." + "pdf");

                    uploaddoc(f);

                } else {
                    String message = "";
                    if (spnr_document_upload_document_identity
                            .getSelectedItem().toString()
                            .equalsIgnoreCase("Select Document")) {
                        message = "Please Select Identity Document First";
                    } else if (identityProofBitmap == null) {
                        message = "Please Capture Or Browse The Identity Proof Document First";
                    }
                    AlertDialogMessage obj = new AlertDialogMessage();
                    obj.dialog(this, message, true);
                }
            } else {
                SelfAttestedDocumentActivity.lst_AgeBitmap.clear();
                SelfAttestedDocumentActivity.lst_IdentityBitmap.clear();
                SelfAttestedDocumentActivity.lst_AddressBitmap.clear();

                mCommonMethods.showToast(mContext, "Please Capture Or Browse The Identity Proof Document First");
            }
        } else {

            SelfAttestedDocumentActivity.lst_AgeBitmap.clear();
            SelfAttestedDocumentActivity.lst_IdentityBitmap.clear();
            SelfAttestedDocumentActivity.lst_AddressBitmap.clear();
        }
    }

    public void onUpload_AddressProof1(View v) {
        if (SelfAttestedDocumentActivity.lst_AddressBitmap != null) {
            if (SelfAttestedDocumentActivity.lst_AddressBitmap.size() >= 1
                    && flag_cancel_address == 1) {

                if (!(spnr_document_upload_document_address.getSelectedItem()
                        .toString()).equalsIgnoreCase("Select Document")
                        && (addressProofBitmap != null)) {

                    String DocumentName = spnr_document_upload_document_address
                            .getSelectedItem().toString();
                    increment = 3;
                    f = mStorageUtils.createFileToAppSpecificDir(mContext, str_quotation_no + "_" + "X" + increment
                            + "." + "pdf");

                    uploaddoc(f);
                } else {
                    String message = "";
                    if (spnr_document_upload_document_address.getSelectedItem()
                            .toString().equalsIgnoreCase("Select Document")) {
                        message = "Please Select Address Document First";
                    } else if (addressProofBitmap == null) {
                        message = "Please Capture Or Browse The Address Proof Document First";
                    }
                    AlertDialogMessage obj = new AlertDialogMessage();
                    obj.dialog(this, message, true);

                }
            } else {
                SelfAttestedDocumentActivity.lst_AgeBitmap.clear();
                SelfAttestedDocumentActivity.lst_IdentityBitmap.clear();
                SelfAttestedDocumentActivity.lst_AddressBitmap.clear();

                mCommonMethods.showToast(mContext, "Please Capture Or Browse The Address Proof Document First");
            }
        } else {
            SelfAttestedDocumentActivity.lst_AgeBitmap.clear();
            SelfAttestedDocumentActivity.lst_IdentityBitmap.clear();
            SelfAttestedDocumentActivity.lst_AddressBitmap.clear();
        }
    }

    private void uploaddoc(File fileName) {

        FileInputStream fin = null;
        try {
            fin = new FileInputStream(fileName);
        } catch (FileNotFoundException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] b = new byte[1024];
        int bytesRead = 0;
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

        switch (email_type){
            case 1:
                new GroupSendMail().mailRequirement(mContext, fileName, str_quotation_no,
                        EMAIL_REQUIRE_UPLOAD_NEW_BUSINESS, "Group NB Requirement Upload of "+str_quotation_no);
                break;

            case 2:
                new GroupSendMail().mailRequirement(mContext, fileName, str_quotation_no,
                        EMAIL_FUND_NEW_BUSINESS, "Fund NB Requirement Upload of "+str_quotation_no);
                break;
        }
        new AsyncUploadDoc().execute();
    }

    class AsyncUploadDoc extends AsyncTask<String, Void, String> {
        private volatile boolean running = true;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            mProgressDialog = new ProgressDialog(mContext,
                    ProgressDialog.THEME_HOLO_LIGHT);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.setCancelable(false);
            mProgressDialog
                    .setTitle(Html
                            .fromHtml("<font color='#00a1e3'><b>Please Wait...<b></font>"));
            mProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... param) {
            // TODO Auto-generated method stub

            if (mCommonMethods.isNetworkConnected(mContext)) {

                running = true;

                SoapObject request = new SoapObject(NAMESPACE,
                        METHOD_NAME_UPLOAD_FILE_SERVICE_GROUPS);

                request.addProperty("f", bytes);
                if (Check.equals("ProposerPhoto")) {
                    request.addProperty("fileName", str_quotation_no
                            + "_cust1Photo.jpg");
                } else if (Check.equals("LifeAssuredPhoto")) {
                    request.addProperty("fileName", str_quotation_no
                            + "_cust2Photo.jpg");
                } else {
                    if (increment == 10 || increment == 25) {
                        request.addProperty("fileName", str_quotation_no
                                + "_X" + increment + "." + "pdf");
                    } else {
                        request.addProperty("fileName", str_quotation_no
                                + "_X0" + increment + "." + "pdf");
                    }
                }

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);
                envelope.dotNet = true;

                new MarshalBase64().register(envelope); // serialization

                envelope.setOutputSoapObject(request);

                mCommonMethods.TLSv12Enable();

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);

                HttpTransportSE androidHttpTranport = new HttpTransportSE(URL,
                        50000);
                try {
                    androidHttpTranport.call(
                            SOAP_ACTION_UPLOAD_FILE_SERVICE_GROUPS, envelope);
                    Object response = envelope.getResponse();
                    strUploadDocument = response.toString();

                } catch (Exception e) {
                    e.printStackTrace();
                    running = false;
                    return "server not responding..";
                }

            } else
                return "Please Activate Internet connection";

            return null;
        }

        @Override
        protected void onPostExecute(String s) {

            if (mProgressDialog.isShowing())
                mProgressDialog.dismiss();

            if (running) {
                if (strUploadDocument.contains("1")) {

                    mCommonMethods.showToast(mContext, "Document Uploaded SuccessFully!!");

                    SelfAttestedDocumentActivity.lst_AgeBitmap.clear();
                    SelfAttestedDocumentActivity.lst_IdentityBitmap.clear();
                    SelfAttestedDocumentActivity.lst_AddressBitmap.clear();

                    if (increment == 1) {

                        flag_cancel_age = 0;

                        img_btn_document_upload_age_upload
                                .setClickable(false);
                        img_btn_document_upload_age_upload
                                .setEnabled(false);

                        img_btn_document_upload_age_upload
                                .setImageDrawable(getResources()
                                        .getDrawable(
                                                R.drawable.checkedupload));

                        img_btn_document_upload_preview_image_age
                                .setVisibility(View.INVISIBLE);
                        img_btn_document_delete_age
                                .setVisibility(View.INVISIBLE);

                    } else if (increment == 2) {// Identity

                        String DocumentName = spnr_document_upload_document_identity
                                .getSelectedItem().toString();

                        flag_cancel_identity = 0;

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
                        flag_cancel_address = 0;

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
                    }

                    showDialog(DIALOG_ALERT_ANOTHER_DOCS);

                } else {
                    mCommonMethods.showToast(mContext, "Server not responding..");
                }
            }else {
                mCommonMethods.showToast(mContext, "Server not responding..");
            }
        }
    }

    class AsyncValidateQuatation extends AsyncTask<String, Void, String> {
        private volatile boolean running = true;
        private String inputpolicylist = "", strErrorCode = "", strErrorMsg = "";

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            mProgressDialog = new ProgressDialog(mContext,
                    ProgressDialog.THEME_HOLO_LIGHT);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.setCancelable(false);
            mProgressDialog
                    .setTitle(Html
                            .fromHtml("<font color='#00a1e3'><b>Please Wait...<b></font>"));
            mProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... param) {
            // TODO Auto-generated method stub

            if (mCommonMethods.isNetworkConnected(mContext)) {

                running = true;

                SoapObject request = new SoapObject(NAMESPACE,
                        METHOD_NAME_VALIDATE_QUATATION_SERVICE_GROUPS);

                request.addProperty("strQuotID", str_quotation_no);

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);
                envelope.dotNet = true;

                new MarshalBase64().register(envelope); // serialization

                envelope.setOutputSoapObject(request);

                mCommonMethods.TLSv12Enable();

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);

                HttpTransportSE androidHttpTranport = new HttpTransportSE(URL,
                        50000);
                try {
                    androidHttpTranport.call(
                            SOAP_ACTION_VALIDATE_QUATATION_SERVICE_GROUPS, envelope);
                    Object response = envelope.getResponse();
                    inputpolicylist = response.toString();
                    ParseXML prsObj = new ParseXML();

                    inputpolicylist = prsObj.parseXmlTag(
                            inputpolicylist, "CIFPolicyList");

                    inputpolicylist = prsObj.parseXmlTag(
                            inputpolicylist, "ScreenData");

                    if (inputpolicylist == null){
                        inputpolicylist = response.toString();

                        inputpolicylist = prsObj.parseXmlTag(
                                inputpolicylist, "CIFPolicyList");

                        inputpolicylist = prsObj.parseXmlTag(
                                inputpolicylist, "Table");

                        str_quotation_no = prsObj.parseXmlTag(
                                inputpolicylist, "QUOTE_ID");
                    }else{
                        strErrorCode = prsObj.parseXmlTag(
                                inputpolicylist, "ErrCode");

                        strErrorMsg = prsObj.parseXmlTag(
                                inputpolicylist, "ErrorMsg");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    running = false;
                    return "server not responding..";
                }

            } else
                return "Please Activate Internet connection";

            return null;
        }

        @Override
        protected void onPostExecute(String s) {

            if (mProgressDialog.isShowing())
                mProgressDialog.dismiss();

            if (running) {
                if (strErrorCode.equals("1")) {
                    mCommonMethods.showToast(mContext, strErrorMsg);
                    linearLayoutParivartanDocumentsUpload.setVisibility(View.GONE);
                }else
                    linearLayoutParivartanDocumentsUpload.setVisibility(View.VISIBLE);
            }else {
                mCommonMethods.showToast(mContext, "Server not responding..");
            }
        }
    }
}