package sbilife.com.pointofsale_bancaagency.utility;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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
import java.util.ArrayList;
import java.util.List;

import sbilife.com.pointofsale_bancaagency.R;
import sbilife.com.pointofsale_bancaagency.ServiceURL;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;
import sbilife.com.pointofsale_bancaagency.common.CompressImage;
import sbilife.com.pointofsale_bancaagency.common.StorageUtils;

public class RinrakshaParivartanDocumentsUploadActivity extends AppCompatActivity implements View.OnClickListener{

    private Context mContext;
    private CommonMethods mCommonMethods;
    private StorageUtils mStorageUtils;

    private final String NAMESPACE = "http://tempuri.org/";
    private final String URL = ServiceURL.SERVICE_URL;

    private static final String SOAP_ACTION_VERIFY_PROPOSAL_NO = "http://tempuri.org/getFormNo_Rinn_SMRT";
    private static final String METHOD_NAME_VERIFY_PROPOSAL_NO = "getFormNo_Rinn_SMRT";

    private static final String SOAP_ACTION_UPLOAD_FILE_SERVICE_RINRAKSHA = "http://tempuri.org/UploadFileRinn_SMRT";
    private static final String METHOD_NAME_UPLOAD_FILE_SERVICE_RINRAKSHA= "UploadFileRinn_SMRT";

    private static final String JPEG_FILE_PREFIX = "IMG_";
    private static final String JPEG_FILE_SUFFIX = ".jpg";
    private static final int REQUEST_CODE_CAPTURE_FILE = 3;
    private static final int REQUEST_CODE_BROWSE_FILE = 2;
    private static final int DIALOG_ALERT = 10;

    private EditText edtRinPariDocUploadProposalNo, edtRinPariOthersProof;

    private AsynchVarifyProposalNo mAsynchVarifyProposalNo;
    private AsyncUploadDoc mAsyncUploadDoc;

    private ProgressDialog mProgressDialog;
    private LinearLayout llRinPariDocUpload;

    private String strProposalNo = "", mCurrentPhotoPath = "", strUploadDocument = "", Check = "";
    private int increment = 0, uploadFlag = 0,
    //age
    deleteAgeDocument = 0, flag_cancel_age = 0,
    //income
    deleteIncomeDocument=0, flag_cancel_income = 0,
    //other
    deleteOtherDocument = 0, flag_cancel_others = 0,
    //one Pager
    deleteOnePagerDocument = 0, flag_cancel_OnePager = 0;


    private File f;

    private Bitmap ageProofBitmap, otherProofBitmap, incomeProofBitmap, onePagerProofBitmap;

    private byte[] bytes = null;

    private Spinner spnrRinPariAgeProof, spnrRinPariIncomeProof;

    //Age Proof
    private ImageButton ibRinPariAgeProofPreview, ibRinPariAgeProofDelete, ibRinPariAgeProofCapture, ibRinPariAgeProofBrowse,
    ibRinPariAgeProofUpload,
    //Income Proof
    ibRinPariIncomeProofPreview, ibRinPariIncomeProofDelete, ibRinPariIncomeProofCapture, ibRinPariIncomeProofBrowse,
    ibRinPariIncomeProofUpload,
    //One Pager
    ibRinPari1PagerProofPreview, ibRinPari1PagerProofDelete, ibRinPari1PagerProofCapture, ibRinPari1PagerProofBrowse,
    ibRinPari1PagerProofUpload,
    //Other Docs
    ibRinPariOthersProofPreview, ibRinPariOthersProofDelete, ibRinPariOthersProofCapture, ibRinPariOthersProofBrowse,
    ibRinPariOthersProofUpload;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_rinraksha_parivartan_documents_upload);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.layout_custom_title_need_analysis);


        initialization();

        // age
        ArrayList<String> upload_age_doc_name = new ArrayList<String>();
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
        fillSpinnerValue(spnrRinPariAgeProof, upload_age_doc_name);

        setBtnListenerOrDisable(ibRinPariAgeProofCapture, AgeProofOnClickListener ,MediaStore.ACTION_IMAGE_CAPTURE);

        // income
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
        fillSpinnerValue(spnrRinPariIncomeProof,upload_income_doc_name);

        setBtnListenerOrDisable(ibRinPariIncomeProofCapture, IncomeProofOnClickListener, MediaStore.ACTION_IMAGE_CAPTURE);

        setBtnListenerOrDisable(ibRinPariOthersProofCapture, OthersProofOnClickListener, MediaStore.ACTION_IMAGE_CAPTURE);

        setBtnListenerOrDisable(ibRinPari1PagerProofCapture, OnePagerProofOnClickListener, MediaStore.ACTION_IMAGE_CAPTURE);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mProgressDialog.isShowing())
            mProgressDialog.dismiss();

        mAsynchVarifyProposalNo.cancel(true);
        mAsyncUploadDoc.cancel(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initialization(){

        mContext = this;
        mStorageUtils = new StorageUtils();
        mCommonMethods = new CommonMethods();
        mCommonMethods.setApplicationToolbarMenu1(this, "Smart Advisor");

        edtRinPariDocUploadProposalNo = findViewById(R.id.edtRinPariDocUploadProposalNo);
        Button btnRinPariDocUploadProposalNoOk = findViewById(R.id.btnRinPariDocUploadProposalNoOk);
        btnRinPariDocUploadProposalNoOk.setOnClickListener(this);

        Button btnRinPariDocUploadProposalNoReset = findViewById(R.id.btnRinPariDocUploadProposalNoReset);
        btnRinPariDocUploadProposalNoReset.setOnClickListener(this);

        llRinPariDocUpload = findViewById(R.id.llRinPariDocUpload);
        llRinPariDocUpload.setVisibility(View.GONE);

        //Age Proof
        spnrRinPariAgeProof = findViewById(R.id.spnrRinPariAgeProof);
        ibRinPariAgeProofPreview = findViewById(R.id.ibRinPariAgeProofPreview);
        ibRinPariAgeProofDelete = findViewById(R.id.ibRinPariAgeProofDelete);
        ibRinPariAgeProofCapture = findViewById(R.id.ibRinPariAgeProofCapture);
        ibRinPariAgeProofBrowse = findViewById(R.id.ibRinPariAgeProofBrowse);
        ibRinPariAgeProofUpload = findViewById(R.id.ibRinPariAgeProofUpload);

        //Income Proof
        spnrRinPariIncomeProof = findViewById(R.id.spnrRinPariIncomeProof);
        ibRinPariIncomeProofPreview = findViewById(R.id.ibRinPariIncomeProofPreview);
        ibRinPariIncomeProofDelete = findViewById(R.id.ibRinPariIncomeProofDelete);
        ibRinPariIncomeProofCapture = findViewById(R.id.ibRinPariIncomeProofCapture);
        ibRinPariIncomeProofBrowse = findViewById(R.id.ibRinPariIncomeProofBrowse);
        ibRinPariIncomeProofUpload = findViewById(R.id.ibRinPariIncomeProofUpload);

        //one pager
        ibRinPari1PagerProofPreview = findViewById(R.id.ibRinPari1PagerProofPreview);
        ibRinPari1PagerProofDelete = findViewById(R.id.ibRinPari1PagerProofDelete);
        ibRinPari1PagerProofCapture = findViewById(R.id.ibRinPari1PagerProofCapture);
        ibRinPari1PagerProofBrowse = findViewById(R.id.ibRinPari1PagerProofBrowse);
        ibRinPari1PagerProofUpload = findViewById(R.id.ibRinPari1PagerProofUpload);

        //Other Docs
        edtRinPariOthersProof = findViewById(R.id.edtRinPariOthersProof);
        ibRinPariOthersProofPreview = findViewById(R.id.ibRinPariOthersProofPreview);
        ibRinPariOthersProofDelete = findViewById(R.id.ibRinPariOthersProofDelete);
        ibRinPariOthersProofCapture = findViewById(R.id.ibRinPariOthersProofCapture);
        ibRinPariOthersProofBrowse = findViewById(R.id.ibRinPariOthersProofBrowse);
        ibRinPariOthersProofUpload = findViewById(R.id.ibRinPariOthersProofUpload);

        mProgressDialog = new ProgressDialog(mContext, ProgressDialog.THEME_HOLO_LIGHT);

        mAsynchVarifyProposalNo = new AsynchVarifyProposalNo();
        mAsyncUploadDoc = new AsyncUploadDoc();

    }

    class AsynchVarifyProposalNo extends AsyncTask<String, String, String>{

        private volatile boolean running = true;
        private String strOutResponce = "";

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
            try {
                running = true;

                SoapObject request = new SoapObject(NAMESPACE,
                        METHOD_NAME_VERIFY_PROPOSAL_NO);
                request.addProperty("strFormNo", strProposalNo);

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);

                HttpTransportSE androidHttpTranport = new HttpTransportSE(URL);

                androidHttpTranport.call(SOAP_ACTION_VERIFY_PROPOSAL_NO, envelope);

                SoapPrimitive sa = (SoapPrimitive) envelope.getResponse();
                strOutResponce = sa.toString();

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
                if (strOutResponce.equals("1")){
                    llRinPariDocUpload.setVisibility(View.VISIBLE);

                }else{
                    mCommonMethods.showMessageDialog(mContext,"Form Number Does Not Exist.");
                    llRinPariDocUpload.setVisibility(View.GONE);
                }
            }else{
                mCommonMethods.showMessageDialog(mContext,"Something Went Wrong..");
                llRinPariDocUpload.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.btnRinPariDocUploadProposalNoOk:

                if (!edtRinPariDocUploadProposalNo.getText().toString().trim().equals("") && edtRinPariDocUploadProposalNo.getText().toString().trim().length() == 10){

                    strProposalNo = edtRinPariDocUploadProposalNo.getText().toString().trim();
                    mAsynchVarifyProposalNo = new AsynchVarifyProposalNo();
                    mAsynchVarifyProposalNo.execute();
                }else{
                    mCommonMethods.showToast(mContext, "Please Enter Valid Form Number");
                }
                break;

            case R.id.btnRinPariDocUploadProposalNoReset:
                resetAll();
                break;

            default:
                break;
        }
    }

    @Override
    protected Dialog onCreateDialog(int id) {

        switch (id) {
            case DIALOG_ALERT:
                AlertDialog.Builder builder = new AlertDialog.Builder(this,
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        File onePagerProofFileName;
        File incomeProofFileName;
        File ageProofFileName;
        File otherProofFileName;
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

                if (Check.equals("OnePagerProof")) {

                    onePagerProofFileName = new File(strPathOfFile);

                    //image compression by bhalla
                    onePagerProofBitmap = BitmapFactory.decodeFile(CompressImage.compressImage(onePagerProofFileName.getAbsolutePath()));

                    /*onePagerProofBitmap = mCommonMethods.ShrinkBitmap(
                            onePagerProofFileName.getAbsolutePath(), 600, 600);*/

                    ibRinPari1PagerProofPreview
                            .setVisibility(View.VISIBLE);
                    ibRinPari1PagerProofDelete
                            .setVisibility(View.VISIBLE);
                    ibRinPari1PagerProofBrowse
                            .setImageDrawable(getResources().getDrawable(
                                    R.drawable.checkedbrowse));
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    onePagerProofBitmap.compress(Bitmap.CompressFormat.PNG,
                            70, baos);
                    byte[] arr = baos.toByteArray();
                    String result = Base64.encodeToString(arr,
                            Base64.DEFAULT);
                    SelfAttestedDocumentActivity.lst_CustomerPhotoBitmap.add(result);
                    showDialog(DIALOG_ALERT);

                } else if (Check.equals("OtherProof")) {

                    otherProofFileName = new File(strPathOfFile);

                    //image compression by bhalla
                    otherProofBitmap = BitmapFactory.decodeFile(CompressImage.compressImage(otherProofFileName.getAbsolutePath()));

                    /*otherProofBitmap = mCommonMethods.ShrinkBitmap(
                            otherProofFileName.getAbsolutePath(), 600, 600);*/

                    ibRinPariOthersProofPreview
                            .setVisibility(View.VISIBLE);
                    ibRinPariOthersProofDelete
                            .setVisibility(View.VISIBLE);
                    ibRinPariOthersProofBrowse
                            .setImageDrawable(getResources().getDrawable(
                                    R.drawable.checkedbrowse));
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    otherProofBitmap.compress(Bitmap.CompressFormat.PNG,
                            70, baos);
                    byte[] arr = baos.toByteArray();
                    String result = Base64.encodeToString(arr,
                            Base64.DEFAULT);
                    SelfAttestedDocumentActivity.lst_OtherBitmap
                            .add(result);
                    showDialog(DIALOG_ALERT);

                } else if (Check.equals("AgeProof")) {
                    ageProofFileName = new File(strPathOfFile);

                    //image compression by bhalla
                    ageProofBitmap = BitmapFactory.decodeFile(CompressImage.compressImage(ageProofFileName.getAbsolutePath()));

                    /*ageProofBitmap = mCommonMethods.ShrinkBitmap(
                            ageProofFileName.getAbsolutePath(), 600, 600);*/

                    ibRinPariAgeProofPreview
                            .setVisibility(View.VISIBLE);
                    ibRinPariAgeProofDelete.setVisibility(View.VISIBLE);

                    ibRinPariAgeProofBrowse
                            .setImageDrawable(getResources().getDrawable(
                                    R.drawable.checkedbrowse));

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    ageProofBitmap.compress(Bitmap.CompressFormat.PNG,
                            70, baos);
                    byte[] arr = baos.toByteArray();
                    String result = Base64.encodeToString(arr,
                            Base64.DEFAULT);
                    SelfAttestedDocumentActivity.lst_AgeBitmap.add(result);

                    showDialog(DIALOG_ALERT);

                } else if (Check.equals("IncomeProof")) {
                    incomeProofFileName = new File(strPathOfFile);

                    //image compression by bhalla
                    incomeProofBitmap = BitmapFactory.decodeFile(CompressImage.compressImage(incomeProofFileName.getAbsolutePath()));

                    /*incomeProofBitmap = mCommonMethods.ShrinkBitmap(
                            incomeProofFileName.getAbsolutePath(), 600, 600);*/

                    ibRinPariIncomeProofPreview
                            .setVisibility(View.VISIBLE);
                    ibRinPariIncomeProofDelete
                            .setVisibility(View.VISIBLE);
                    ibRinPariIncomeProofBrowse
                            .setImageDrawable(getResources().getDrawable(
                                    R.drawable.checkedbrowse));

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    incomeProofBitmap.compress(Bitmap.CompressFormat.PNG,
                            70, baos);
                    byte[] arr = baos.toByteArray();
                    String result = Base64.encodeToString(arr,
                            Base64.DEFAULT);
                    SelfAttestedDocumentActivity.lst_IncomeBitmap
                            .add(result);
                    showDialog(DIALOG_ALERT);
                }
            }
        } else if (requestCode == REQUEST_CODE_CAPTURE_FILE) {
            if (resultCode == RESULT_OK) {

                if (Check.equals("OnePagerProof")) {

                    onePagerProofFileName = f;

                    //image compression by bhalla
                    onePagerProofBitmap = BitmapFactory.decodeFile(CompressImage.compressImage(onePagerProofFileName.getAbsolutePath()));

                    /*onePagerProofBitmap = mCommonMethods.ShrinkBitmap(
                            onePagerProofFileName.getAbsolutePath(), 600, 600);*/

                    ibRinPari1PagerProofPreview
                            .setVisibility(View.VISIBLE);

                    ibRinPari1PagerProofDelete
                            .setVisibility(View.VISIBLE);

                    ibRinPari1PagerProofCapture
                            .setImageDrawable(getResources().getDrawable(
                                    R.drawable.checkedcamera));

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    otherProofBitmap.compress(Bitmap.CompressFormat.PNG,
                            70, baos);
                    byte[] arr = baos.toByteArray();
                    String result = Base64.encodeToString(arr,
                            Base64.DEFAULT);
                    SelfAttestedDocumentActivity.lst_CustomerPhotoBitmap.add(result);
                    showDialog(DIALOG_ALERT);
                } else if (Check.equals("OtherProof")) {
                    otherProofFileName = f;

                    //image compression by bhalla
                    otherProofBitmap = BitmapFactory.decodeFile(CompressImage.compressImage(otherProofFileName.getAbsolutePath()));

                    /*otherProofBitmap = mCommonMethods.ShrinkBitmap(
                            otherProofFileName.getAbsolutePath(), 600, 600);*/

                    ibRinPariOthersProofPreview
                            .setVisibility(View.VISIBLE);

                    ibRinPariOthersProofDelete
                            .setVisibility(View.VISIBLE);

                    ibRinPariOthersProofCapture
                            .setImageDrawable(getResources().getDrawable(
                                    R.drawable.checkedcamera));

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    otherProofBitmap.compress(Bitmap.CompressFormat.PNG,
                            70, baos);
                    byte[] arr = baos.toByteArray();
                    String result = Base64.encodeToString(arr,
                            Base64.DEFAULT);
                    SelfAttestedDocumentActivity.lst_OtherBitmap
                            .add(result);
                    showDialog(DIALOG_ALERT);

                } else if (Check.equals("AgeProof")) {

                    ageProofFileName = f;

                    //image compression by bhalla
                    ageProofBitmap = BitmapFactory.decodeFile(CompressImage.compressImage(ageProofFileName.getAbsolutePath()));

                    /*ageProofBitmap = mCommonMethods.ShrinkBitmap(
                            ageProofFileName.getAbsolutePath(), 600, 600);*/

                    ibRinPariAgeProofPreview
                            .setVisibility(View.VISIBLE);
                    ibRinPariAgeProofDelete.setVisibility(View.VISIBLE);
                    ibRinPariAgeProofCapture
                            .setImageDrawable(getResources().getDrawable(
                                    R.drawable.checkedcamera));

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    ageProofBitmap.compress(Bitmap.CompressFormat.PNG,
                            70, baos);
                    byte[] arr = baos.toByteArray();
                    String result = Base64.encodeToString(arr,
                            Base64.DEFAULT);
                    SelfAttestedDocumentActivity.lst_AgeBitmap.add(result);

                    showDialog(DIALOG_ALERT);
                } else if (Check.equals("IncomeProof")) {
                    incomeProofFileName = f;

                    //image compression by bhalla
                    incomeProofBitmap = BitmapFactory.decodeFile(CompressImage.compressImage(incomeProofFileName.getAbsolutePath()));

                    /*incomeProofBitmap = mCommonMethods.ShrinkBitmap(
                            incomeProofFileName.getAbsolutePath(), 600, 600);*/

                    ibRinPariIncomeProofPreview
                            .setVisibility(View.VISIBLE);
                    ibRinPariIncomeProofDelete
                            .setVisibility(View.VISIBLE);
                    ibRinPariIncomeProofCapture
                            .setImageDrawable(getResources().getDrawable(
                                    R.drawable.checkedcamera));

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    incomeProofBitmap.compress(Bitmap.CompressFormat.PNG,
                            70, baos);
                    byte[] arr = baos.toByteArray();
                    String result = Base64.encodeToString(arr,
                            Base64.DEFAULT);
                    SelfAttestedDocumentActivity.lst_IncomeBitmap
                            .add(result);
                    showDialog(DIALOG_ALERT);
                }

            }
        }

    }

    private void resetAll(){
        edtRinPariDocUploadProposalNo.setText("");
        edtRinPariDocUploadProposalNo.setEnabled(true);

        llRinPariDocUpload.setVisibility(View.GONE);

        clearDocumentsPDF();

        //clear age
        spnrRinPariAgeProof.setSelection(0);
        spnrRinPariAgeProof.setEnabled(true);

        flag_cancel_age = 0;

        ibRinPariAgeProofCapture.setClickable(true);
        ibRinPariAgeProofCapture.setEnabled(true);
        ibRinPariAgeProofCapture
                .setImageDrawable(getResources()
                        .getDrawable(
                                R.drawable.ibtn_camera));

        ibRinPariAgeProofBrowse.setClickable(true);
        ibRinPariAgeProofBrowse.setEnabled(true);
        ibRinPariAgeProofBrowse.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_browsedoc));

        ibRinPariAgeProofUpload.setClickable(true);
        ibRinPariAgeProofUpload.setEnabled(true);
        ibRinPariAgeProofUpload.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_uploaddoc));

        ibRinPariAgeProofPreview.setVisibility(View.INVISIBLE);
        ibRinPariAgeProofDelete.setVisibility(View.INVISIBLE);

        //clear income
        spnrRinPariIncomeProof.setSelection(0);
        spnrRinPariIncomeProof.setEnabled(true);

        flag_cancel_income = 0;

        ibRinPariIncomeProofCapture.setClickable(true);
        ibRinPariIncomeProofCapture.setEnabled(true);
        ibRinPariIncomeProofCapture
                .setImageDrawable(getResources()
                        .getDrawable(
                                R.drawable.ibtn_camera));

        ibRinPariIncomeProofBrowse.setClickable(true);
        ibRinPariIncomeProofBrowse.setEnabled(true);
        ibRinPariIncomeProofBrowse
                .setImageDrawable(getResources()
                        .getDrawable(
                                R.drawable.ibtn_browsedoc));

        ibRinPariIncomeProofUpload.setClickable(true);
        ibRinPariIncomeProofUpload.setEnabled(true);
        ibRinPariIncomeProofUpload
                .setImageDrawable(getResources()
                        .getDrawable(
                                R.drawable.ibtn_uploaddoc));

        ibRinPariIncomeProofPreview
                .setVisibility(View.INVISIBLE);
        ibRinPariIncomeProofDelete
                .setVisibility(View.INVISIBLE);

        //clear One Pager
        flag_cancel_OnePager = 0;

        ibRinPari1PagerProofCapture.setClickable(true);
        ibRinPari1PagerProofCapture.setEnabled(true);
        ibRinPari1PagerProofCapture
                .setImageDrawable(getResources()
                        .getDrawable(
                                R.drawable.ibtn_camera));

        ibRinPari1PagerProofBrowse.setClickable(true);
        ibRinPari1PagerProofBrowse.setEnabled(true);
        ibRinPari1PagerProofBrowse
                .setImageDrawable(getResources()
                        .getDrawable(
                                R.drawable.ibtn_browsedoc));

        ibRinPari1PagerProofUpload.setClickable(true);
        ibRinPari1PagerProofUpload.setEnabled(true);
        ibRinPari1PagerProofUpload
                .setImageDrawable(getResources()
                        .getDrawable(
                                R.drawable.ibtn_uploaddoc));

        ibRinPari1PagerProofPreview
                .setVisibility(View.INVISIBLE);
        ibRinPari1PagerProofDelete
                .setVisibility(View.INVISIBLE);

        //clear others
        edtRinPariOthersProof.setText("");
        edtRinPariOthersProof.setEnabled(true);

        flag_cancel_others = 0;

        ibRinPariOthersProofCapture.setClickable(true);
        ibRinPariOthersProofCapture.setEnabled(true);
        ibRinPariOthersProofCapture
                .setImageDrawable(getResources()
                        .getDrawable(
                                R.drawable.ibtn_camera));

        ibRinPariOthersProofBrowse.setClickable(true);
        ibRinPariOthersProofBrowse.setEnabled(true);
        ibRinPariOthersProofBrowse
                .setImageDrawable(getResources()
                        .getDrawable(
                                R.drawable.ibtn_browsedoc));

        ibRinPariOthersProofUpload.setClickable(true);
        ibRinPariOthersProofUpload.setEnabled(true);
        ibRinPariOthersProofUpload
                .setImageDrawable(getResources()
                        .getDrawable(
                                R.drawable.ibtn_uploaddoc));

        ibRinPariOthersProofPreview
                .setVisibility(View.INVISIBLE);
        ibRinPariOthersProofDelete
                .setVisibility(View.INVISIBLE);
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

    private void fillSpinnerValue(Spinner spinner, List<String> value_list) {

        Element_TextView_BaseAdapter retd_adapter = new Element_TextView_BaseAdapter(
                RinrakshaParivartanDocumentsUploadActivity.this, value_list);
        spinner.setAdapter(retd_adapter);
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
        //strPathOfFile = mCurrentPhotoPath;

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

    /*private void openwith(File File) {

        // Checking for the file is exist or not
        if (File.exists()) {
            Uri path = Uri.fromFile(File);
            Intent objIntent = new Intent(Intent.ACTION_VIEW);
            objIntent.setDataAndType(path, "application/pdf");
            objIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            // Staring the pdf viewer
            startActivity(objIntent);
        } else {
            Toast.makeText(mContext, "file does not exists", Toast.LENGTH_SHORT)
                    .show();
        }
    }*/

    private void dispatchTakePictureIntent(String str) {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

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

        /*if (str.equals("1")) {
            f = createImageFile_pics("1");
            mCurrentPhotoPath = f.getAbsolutePath();

        } else if (str.equals("2")) {
            f = createImageFile_pics("2");
            mCurrentPhotoPath = f.getAbsolutePath();
        } else if (str.equals("3")) {
            f = createImageFile_pics("3");
            mCurrentPhotoPath = f.getAbsolutePath();
        } else if (str.equals("0")) {*/
            f = createImageFile();
            mCurrentPhotoPath = f.getAbsolutePath();
        //}
        return f;
    }

    private File createImageFile() throws IOException {
        String imageFileName = "";
        if (Check.equalsIgnoreCase("OnePagerProof"))
            imageFileName = JPEG_FILE_PREFIX + strProposalNo + "_S0"
                    + increment + "_" + JPEG_FILE_SUFFIX;
        else
            imageFileName = JPEG_FILE_PREFIX + strProposalNo + "_X0"
                + increment + "_" + JPEG_FILE_SUFFIX;

        File imageF = mStorageUtils.createFileToAppSpecificDir(mContext, imageFileName);
        return imageF;
    }

    private void onBrowse() {

        Intent i = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, REQUEST_CODE_BROWSE_FILE);
    }

    private void clearDocumentsPDF() {

        SelfAttestedDocumentActivity.lst_AgeBitmap.clear();
        SelfAttestedDocumentActivity.lst_IncomeBitmap.clear();

        //following used AS one pager docs
        SelfAttestedDocumentActivity.lst_CustomerPhotoBitmap.clear();

        SelfAttestedDocumentActivity.lst_OtherBitmap.clear();
    }

    private final class OkOnClickListener implements
            DialogInterface.OnClickListener{

        @Override
        public void onClick(DialogInterface dialogInterface, int i) {


                if (deleteAgeDocument == 1) {
                    ibRinPariAgeProofPreview
                            .setVisibility(View.INVISIBLE);
                    ibRinPariAgeProofDelete.setVisibility(View.INVISIBLE);

                    ibRinPariAgeProofCapture
                            .setImageDrawable(getResources().getDrawable(
                                    R.drawable.ibtn_camera));
                    ibRinPariAgeProofBrowse
                            .setImageDrawable(getResources().getDrawable(
                                    R.drawable.ibtn_browsedoc));
                    SelfAttestedDocumentActivity.lst_AgeBitmap.clear();
                    deleteAgeDocument = 0;
                } else if (deleteIncomeDocument == 1) {
                    ibRinPariIncomeProofPreview
                            .setVisibility(View.INVISIBLE);
                    ibRinPariIncomeProofDelete
                            .setVisibility(View.INVISIBLE);

                    ibRinPariIncomeProofCapture
                            .setImageDrawable(getResources().getDrawable(
                                    R.drawable.ibtn_camera));
                    ibRinPariIncomeProofBrowse
                            .setImageDrawable(getResources().getDrawable(
                                    R.drawable.ibtn_browsedoc));
                    SelfAttestedDocumentActivity.lst_IncomeBitmap.clear();
                    deleteIncomeDocument = 0;
                } else if (deleteOtherDocument == 1) {
                    ibRinPariOthersProofPreview
                            .setVisibility(View.INVISIBLE);
                    ibRinPariOthersProofDelete
                            .setVisibility(View.INVISIBLE);
                    ibRinPariOthersProofCapture
                            .setImageDrawable(getResources().getDrawable(
                                    R.drawable.ibtn_camera));
                    ibRinPariOthersProofBrowse
                            .setImageDrawable(getResources().getDrawable(
                                    R.drawable.ibtn_browsedoc));
                    SelfAttestedDocumentActivity.lst_OtherBitmap.clear();
                    deleteOtherDocument = 0;
                }else if(deleteOnePagerDocument == 1){
                    ibRinPari1PagerProofPreview
                            .setVisibility(View.INVISIBLE);
                    ibRinPari1PagerProofDelete
                            .setVisibility(View.INVISIBLE);
                    ibRinPari1PagerProofCapture
                            .setImageDrawable(getResources().getDrawable(
                                    R.drawable.ibtn_camera));
                    ibRinPari1PagerProofBrowse
                            .setImageDrawable(getResources().getDrawable(
                                    R.drawable.ibtn_browsedoc));
                    //following used AS one pager docs
                    SelfAttestedDocumentActivity.lst_CustomerPhotoBitmap.clear();
                    deleteOnePagerDocument = 0;
                }
                else {
                    if (uploadFlag == 1) {
                        dispatchTakePictureIntent("0");
                    } else if (uploadFlag == 0) {
                        onBrowse();
                    }
                }
        }
    }

    private final class CancelOnClickListener implements
            DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {

            if (!(deleteAgeDocument == 1)
                    || !(deleteIncomeDocument == 1)
                    || !(deleteOtherDocument == 1)
                    || !(deleteOnePagerDocument == 1)) {

                if (SelfAttestedDocumentActivity.lst_AgeBitmap.size() >= 1) {
                    flag_cancel_age = 1;
                    increment = 1;

                    SelfAttestedDocumentActivity obj = new SelfAttestedDocumentActivity();
                    obj.createDocumentPdf_groups(increment, "Age Proof ",
                            spnrRinPariAgeProof.getSelectedItem()
                                    .toString(),
                            SelfAttestedDocumentActivity.lst_AgeBitmap, strProposalNo);
                }

                if (SelfAttestedDocumentActivity.lst_IncomeBitmap.size() >= 1) {
                    flag_cancel_income = 1;
                    increment = 2;
                    SelfAttestedDocumentActivity obj = new SelfAttestedDocumentActivity();
                    obj.createDocumentPdf_groups(increment, "Income Proof ",
                            spnrRinPariIncomeProof
                                    .getSelectedItem().toString(),
                            SelfAttestedDocumentActivity.lst_IncomeBitmap, strProposalNo);
                }

                if (SelfAttestedDocumentActivity.lst_OtherBitmap.size() >= 1) {
                    flag_cancel_others = 1;
                    increment = 3;
                    SelfAttestedDocumentActivity obj = new SelfAttestedDocumentActivity();
                    obj.createDocumentPdf_groups(increment, "Other Proof ", edtRinPariOthersProof.getText().toString(),
                            SelfAttestedDocumentActivity.lst_OtherBitmap, strProposalNo);
                }
                if (SelfAttestedDocumentActivity.lst_CustomerPhotoBitmap.size() >= 1){
                    flag_cancel_OnePager = 1;
                    increment = 2;
                    SelfAttestedDocumentActivity obj = new SelfAttestedDocumentActivity();
                    obj.createDocumentPdf_groups(increment, "One Pager Proof ", "",
                            SelfAttestedDocumentActivity.lst_CustomerPhotoBitmap, strProposalNo);
                }
            }
            dialogInterface.dismiss();
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

        mAsyncUploadDoc = new AsyncUploadDoc();
        mAsyncUploadDoc.execute();
    }

    class AsyncUploadDoc extends AsyncTask<String, Void, String> {
        private volatile boolean running = true;

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
                try{
                    running = true;

                    SoapObject request = new SoapObject(NAMESPACE,
                            METHOD_NAME_UPLOAD_FILE_SERVICE_RINRAKSHA);

                    request.addProperty("f", bytes);
                    String strfileName = "";
                    if (Check.equalsIgnoreCase("OnePagerProof"))
                        strfileName = strProposalNo
                                + "_S0" + increment + "." + "pdf";
                    else
                        strfileName = strProposalNo
                            + "_X0" + increment + "." + "pdf";

                    request.addProperty("fileName", strfileName);

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
                                SOAP_ACTION_UPLOAD_FILE_SERVICE_RINRAKSHA, envelope);
                        Object response = envelope.getResponse();
                        strUploadDocument = response.toString();

                        // SoapPrimitive sa = (SoapPrimitive)
                        // envelope.getResponse();
                    } catch (Exception e) {
                        e.printStackTrace();
                        running = false;
                        return "server not responding..";
                    }

                }catch (Exception e){
                    e.printStackTrace();
                    running = false;
                    return "server not responding..";
                }
            }else{
                return "Please Activate Internet connection";
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            if (mProgressDialog.isShowing())
                mProgressDialog.dismiss();

            if (running) {

                if (strUploadDocument.equalsIgnoreCase("1")) {
                    Toast.makeText(mContext, "Document Uploaded SuccessFully!!",
                            Toast.LENGTH_SHORT).show();

                    clearDocumentsPDF();
                    running = false;

                    if (Check.equals("OnePagerProof")){
                        flag_cancel_OnePager = 0;

                        ibRinPari1PagerProofCapture.setClickable(false);
                        ibRinPari1PagerProofCapture.setEnabled(false);

                        ibRinPari1PagerProofBrowse.setClickable(false);
                        ibRinPari1PagerProofBrowse.setEnabled(false);

                        ibRinPari1PagerProofUpload.setClickable(false);
                        ibRinPari1PagerProofUpload.setEnabled(false);

                        ibRinPari1PagerProofUpload
                                .setImageDrawable(getResources()
                                        .getDrawable(
                                                R.drawable.checkedupload));

                        ibRinPari1PagerProofPreview
                                .setVisibility(View.INVISIBLE);
                        ibRinPari1PagerProofDelete
                                .setVisibility(View.INVISIBLE);
                    }else{
                        if (increment == 1) {//Age

                            flag_cancel_age = 0;

                            spnrRinPariAgeProof.setClickable(false);
                            spnrRinPariIncomeProof.setEnabled(false);

                            ibRinPariAgeProofCapture.setClickable(false);
                            ibRinPariAgeProofCapture.setEnabled(false);

                            ibRinPariAgeProofBrowse.setClickable(false);
                            ibRinPariAgeProofBrowse.setEnabled(false);

                            ibRinPariAgeProofUpload
                                    .setClickable(false);
                            ibRinPariAgeProofUpload
                                    .setEnabled(false);

                            ibRinPariAgeProofUpload
                                    .setImageDrawable(getResources()
                                            .getDrawable(
                                                    R.drawable.checkedupload));

                            ibRinPariAgeProofPreview
                                    .setVisibility(View.INVISIBLE);
                            ibRinPariAgeProofDelete
                                    .setVisibility(View.INVISIBLE);
                        }else if (increment == 2){//Income

                            flag_cancel_income = 0;

                            spnrRinPariIncomeProof.setClickable(false);
                            spnrRinPariIncomeProof.setEnabled(false);

                            ibRinPariIncomeProofCapture.setClickable(false);
                            ibRinPariIncomeProofCapture.setEnabled(false);

                            ibRinPariIncomeProofBrowse.setClickable(false);
                            ibRinPariIncomeProofBrowse.setEnabled(false);

                            ibRinPariIncomeProofPreview
                                    .setVisibility(View.INVISIBLE);
                            ibRinPariIncomeProofDelete
                                    .setVisibility(View.INVISIBLE);

                            ibRinPariIncomeProofUpload
                                    .setClickable(false);
                            ibRinPariIncomeProofUpload
                                    .setEnabled(false);
                            ibRinPariIncomeProofUpload
                                    .setImageDrawable(getResources().getDrawable(
                                            R.drawable.checkedupload));
                        }else if (increment == 3) {// Others

                            flag_cancel_others = 0;

                            edtRinPariOthersProof.setEnabled(false);

                            ibRinPariOthersProofCapture.setClickable(false);
                            ibRinPariOthersProofCapture.setEnabled(false);

                            ibRinPariOthersProofBrowse.setClickable(false);
                            ibRinPariOthersProofBrowse.setEnabled(false);

                            ibRinPariOthersProofPreview
                                    .setVisibility(View.INVISIBLE);
                            ibRinPariOthersProofDelete
                                    .setVisibility(View.INVISIBLE);

                            ibRinPariOthersProofUpload
                                    .setClickable(false);
                            ibRinPariOthersProofUpload
                                    .setEnabled(false);
                            ibRinPariOthersProofUpload
                                    .setImageDrawable(getResources().getDrawable(
                                            R.drawable.checkedupload));
                        }
                    }

                }else{
                    Toast.makeText(mContext, "Server not responding..",
                            Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(mContext, "Server not responding..",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    /*Age Listeners*/
    public void onPreview_ageProof(View v) {

        File f = mStorageUtils.createFileToAppSpecificDir(mContext, strProposalNo + "_" + "X" + 1 + "." + "pdf");

        mCommonMethods.openPDFAction(f, mContext);
    }

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

    private View.OnClickListener AgeProofOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!strProposalNo.equals("")) {

                if (!spnrRinPariAgeProof.getSelectedItem().toString().equals("Select Document")){

                    clearDocumentsPDF();
                    uploadFlag = 1;
                    ibRinPariAgeProofBrowse
                            .setImageDrawable(getResources().getDrawable(
                                    R.drawable.ibtn_browsedoc));
                    Check = "AgeProof";
                    increment = 1;
                    dispatchTakePictureIntent("0");

                }else
                    mCommonMethods.showToast(mContext, "Please Select Age Proof First");
            } else
                mCommonMethods.showToast(mContext, "please Enter Form Number");

        }
    };

    public void onBrowse_AgeProof(View v) {
        if (!spnrRinPariAgeProof.getSelectedItem().toString().equals("Select Document")){
            clearDocumentsPDF();
            uploadFlag = 0;

            ibRinPariAgeProofCapture.setImageDrawable(getResources()
                    .getDrawable(R.drawable.ibtn_camera));
            Check = "AgeProof";
            onBrowse();
        }else
            mCommonMethods.showToast(mContext, "Please Select Age Proof Document First");
    }

    public void onUpload_AgeProof(View v) {

        if (SelfAttestedDocumentActivity.lst_AgeBitmap != null) {
            if (SelfAttestedDocumentActivity.lst_AgeBitmap.size() >= 1
                    && flag_cancel_age == 1) {

                if (!(spnrRinPariAgeProof.getSelectedItem()
                        .toString()).equalsIgnoreCase("Select Document")
                        && ageProofBitmap != null) {

                    strUploadDocument = spnrRinPariAgeProof.getSelectedItem().toString();

                    increment = 1;

                    File f = mStorageUtils.createFileToAppSpecificDir(mContext, strProposalNo + "_" + "X" + increment
                            + "." + "pdf");

                    uploaddoc(f);

                } else {

                    String message = "";
                    if (spnrRinPariAgeProof.getSelectedItem()
                            .toString().equalsIgnoreCase("Select Document")) {
                        message = "Please Select Age Proof Document First";
                    } else if (ageProofBitmap == null) {
                        message = "Please Capture Or Browse The Age Proof Document First";
                    }
                    AlertDialogMessage obj = new AlertDialogMessage();
                    obj.dialog(this, message, true);
                }
            } else {
                Toast.makeText(
                        mContext,
                        "Please Capture Or Browse The Age Proof Document First",
                        Toast.LENGTH_SHORT).show();

                clearDocumentsPDF();
            }
        } else {
            clearDocumentsPDF();
        }
    }

    /*Income Listeners*/
    public void onPreview_incomeProof(View v) {

        File f = mStorageUtils.createFileToAppSpecificDir(mContext, strProposalNo + "_" + "X" + 2 + "." + "pdf");

        mCommonMethods.openPDFAction(f, mContext);
    }

    public void onDelete_incomeProof(View v) {
        deleteIncomeDocument = 1;
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

    private View.OnClickListener IncomeProofOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!strProposalNo.equals("")) {

                if (!spnrRinPariIncomeProof.getSelectedItem().toString().equals("Select Document")){
                    clearDocumentsPDF();
                    uploadFlag = 1;
                    ibRinPariIncomeProofBrowse
                            .setImageDrawable(getResources().getDrawable(
                                    R.drawable.ibtn_browsedoc));
                    Check = "IncomeProof";
                    increment = 2;
                    dispatchTakePictureIntent("0");
                }else
                    mCommonMethods.showToast(mContext, "Please Select Income Proof Document First");

            } else
                mCommonMethods.showToast(mContext, "please Enter Form Number");

        }
    };

    public void onBrowse_IncomeProof(View v) {
        if (!spnrRinPariIncomeProof.getSelectedItem().toString().equals("Select Document")){
            clearDocumentsPDF();
            uploadFlag = 0;

            ibRinPariIncomeProofCapture.setImageDrawable(getResources()
                    .getDrawable(R.drawable.ibtn_camera));
            Check = "IncomeProof";
            onBrowse();
        }else{
            mCommonMethods.showToast(mContext, "Please Select Income Proof Document First");
        }
    }

    public void onUpload_IncomeProof(View v) {

        if (SelfAttestedDocumentActivity.lst_IncomeBitmap != null) {
            if (SelfAttestedDocumentActivity.lst_IncomeBitmap.size() >= 1
                    && flag_cancel_income == 1) {

                if (!(spnrRinPariIncomeProof.getSelectedItem()
                        .toString()).equalsIgnoreCase("Select Document")
                        && incomeProofBitmap != null) {

                    strUploadDocument = spnrRinPariIncomeProof.getSelectedItem().toString();

                    increment = 2;

                    File f = mStorageUtils.createFileToAppSpecificDir(mContext, strProposalNo + "_" + "X" + increment
                            + "." + "pdf");

                    uploaddoc(f);

                } else {

                    String message = "";
                    if (spnrRinPariIncomeProof.getSelectedItem()
                            .toString().equalsIgnoreCase("Select Document")) {
                        message = "Please Select Income Proof Document First";
                    } else if (ageProofBitmap == null) {
                        message = "Please Capture Or Browse The Income Proof Document First";
                    }
                    AlertDialogMessage obj = new AlertDialogMessage();
                    obj.dialog(this, message, true);
                }
            } else {
                Toast.makeText(
                        mContext,
                        "Please Capture Or Browse The Income Proof Document First",
                        Toast.LENGTH_SHORT).show();

                clearDocumentsPDF();
            }
        } else {
            clearDocumentsPDF();
        }
    }

    /*Others Listeners*/
    public void onPreview_othersProof(View v) {

        File f = mStorageUtils.createFileToAppSpecificDir(mContext, strProposalNo + "_" + "X" + 3 + "." + "pdf");

        mCommonMethods.openPDFAction(f, mContext);
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

    private View.OnClickListener OthersProofOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!strProposalNo.equals("")) {
                if (!edtRinPariOthersProof.getText().toString().equals("")) {
                    clearDocumentsPDF();
                    uploadFlag = 1;
                    ibRinPariOthersProofBrowse
                            .setImageDrawable(getResources().getDrawable(
                                    R.drawable.ibtn_browsedoc));
                    Check = "OtherProof";
                    increment = 3;
                    dispatchTakePictureIntent("0");
                }else
                    mCommonMethods.showToast(mContext, "please Enter Other Document Name First");
            } else
                mCommonMethods.showToast(mContext, "please Enter Form Number");

        }
    };

    public void onBrowse_OthersProof(View v) {
        if (!edtRinPariOthersProof.getText().toString().equals("")){
            clearDocumentsPDF();
            uploadFlag = 0;

            ibRinPariOthersProofCapture.setImageDrawable(getResources()
                    .getDrawable(R.drawable.ibtn_camera));
            Check = "OtherProof";
            onBrowse();
        }else{
            mCommonMethods.showToast(mContext, "Please Enter Other Document First");
        }
    }

    public void onUpload_OthersProof(View v) {

        if (SelfAttestedDocumentActivity.lst_OtherBitmap != null) {
            if (SelfAttestedDocumentActivity.lst_OtherBitmap.size() >= 1
                    && flag_cancel_others == 1) {

                if (!edtRinPariOthersProof.getText().toString().equalsIgnoreCase("")
                        && otherProofBitmap != null) {

                    strUploadDocument = edtRinPariOthersProof.getText().toString();

                    increment = 3;

                    File f = mStorageUtils.createFileToAppSpecificDir(mContext, strProposalNo + "_" + "X" + increment
                            + "." + "pdf");

                    uploaddoc(f);

                } else {

                    String message = "";
                    if (edtRinPariOthersProof.getText().toString().equals("")) {
                        message = "Please Enter Others Proof Document First";
                    } else if (otherProofBitmap == null) {
                        message = "Please Capture Or Browse The Other Proof Document First";
                    }
                    AlertDialogMessage obj = new AlertDialogMessage();
                    obj.dialog(this, message, true);
                }
            } else {
                Toast.makeText(
                        mContext,
                        "Please Capture Or Browse The Other Proof Document First",
                        Toast.LENGTH_SHORT).show();

                clearDocumentsPDF();
            }
        } else {
            clearDocumentsPDF();
        }
    }

    /*One Pager Listener*/
    public void onPreview_onePagerProof(View v) {

        File f = mStorageUtils.createFileToAppSpecificDir(mContext, strProposalNo + "_" + "S" + 2 + "." + "pdf");

        mCommonMethods.openPDFAction(f, mContext);
    }

    public void onDelete_onePagerProof(View v) {
        deleteOnePagerDocument = 1;
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

    private View.OnClickListener OnePagerProofOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!strProposalNo.equals("")) {

                    clearDocumentsPDF();
                    uploadFlag = 1;
                    ibRinPari1PagerProofBrowse
                            .setImageDrawable(getResources().getDrawable(
                                    R.drawable.ibtn_browsedoc));
                    Check = "OnePagerProof";
                    increment = 2;
                    dispatchTakePictureIntent("0");
            } else
                mCommonMethods.showToast(mContext, "please Enter Form Number");

        }
    };

    public void onBrowse_OnePagerProof(View v) {

            clearDocumentsPDF();
            uploadFlag = 0;

            ibRinPari1PagerProofCapture.setImageDrawable(getResources()
                    .getDrawable(R.drawable.ibtn_camera));
            Check = "OnePagerProof";
            onBrowse();
    }

    public void onUpload_OnePagerProof(View v) {

        if (SelfAttestedDocumentActivity.lst_CustomerPhotoBitmap != null) {
            if (SelfAttestedDocumentActivity.lst_CustomerPhotoBitmap.size() >= 1
                    && flag_cancel_OnePager == 1) {

                if (onePagerProofBitmap != null) {

                    strUploadDocument = "";

                    increment = 2;

                    File f = mStorageUtils.createFileToAppSpecificDir(mContext, strProposalNo + "_" + "S" + increment
                            + "." + "pdf");

                    uploaddoc(f);

                } else {

                    AlertDialogMessage obj = new AlertDialogMessage();
                    obj.dialog(this, "Please Capture Or Browse The One Pager Proof Document First", true);
                }
            } else {
                Toast.makeText(
                        mContext,
                        "Please Capture Or Browse The One Pager Proof Document First",
                        Toast.LENGTH_SHORT).show();

                clearDocumentsPDF();
            }
        } else {
            clearDocumentsPDF();
        }
    }

}
