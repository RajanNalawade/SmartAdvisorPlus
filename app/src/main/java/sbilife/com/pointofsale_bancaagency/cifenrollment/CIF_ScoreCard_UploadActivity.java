package sbilife.com.pointofsale_bancaagency.cifenrollment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import sbilife.com.pointofsale_bancaagency.DatabaseHelper;
import sbilife.com.pointofsale_bancaagency.ParseXML;
import sbilife.com.pointofsale_bancaagency.R;
import sbilife.com.pointofsale_bancaagency.ServiceURL;
import sbilife.com.pointofsale_bancaagency.cifenrollment.phase1.AsyncUploadFile_CIF;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;
import sbilife.com.pointofsale_bancaagency.common.CompressImage;
import sbilife.com.pointofsale_bancaagency.common.FileLoader;
import sbilife.com.pointofsale_bancaagency.common.StorageUtils;

public class CIF_ScoreCard_UploadActivity extends AppCompatActivity implements View.OnClickListener, AsyncUploadFile_CIF.Interface_Upload_CIF_Files {

    private CommonMethods mCommonMethods;
    private StorageUtils mStorageUtils;
    private Context mContext;

    private EditText edt_cif_score_card_upload;

    private ImageButton imgbtn_score_card_camera, imgbtn_score_card_browse, imgbtn_score_card_upload,
            ib_score_card_docs_view;

    private ProgressDialog mProgressDialog;

    private static final int REQUEST_CODE_PICK_FILE = 2;
    private final int REQUEST_CODE_PICK_PHOTO_FILE = 3;

    private File mScoreCardFile;
    private byte[] mScoreCardBytes;

    private String str_urn_no = "";

    private AsyncUploadFile_CIF mAsyncUploadFile_cif;

    private  final String FILE_NAME = "_SCORE_CARD";

    private final String NAMESPACE = "http://tempuri.org/";

    private final String METHOD_NAME_UPLOAD_ALL_DOC = "UploadFile_CIFEnroll_URN";
    private final String METHOD_NAME_SCORE_CARD_VALIDATE_URN = "validateURN";

    private boolean is_score_card_upload = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_cif_score_card_upload);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.cifenrollment_pf_activity_title);

        initialisation();
    }

    private void initialisation(){

        mContext = this;

        mCommonMethods = new CommonMethods();
        mStorageUtils = new StorageUtils();
        mCommonMethods.setApplicationToolbarMenu1(this, "CIF on Boarding");

        DatabaseHelper db = new DatabaseHelper(mContext);

        edt_cif_score_card_upload = findViewById(R.id.edt_cif_score_card_upload);

        ib_score_card_docs_view = findViewById(R.id.ib_score_card_docs_view);
        imgbtn_score_card_camera = findViewById(R.id.imgbtn_score_card_camera);
        imgbtn_score_card_browse = findViewById(R.id.imgbtn_score_card_browse);
        imgbtn_score_card_upload = findViewById(R.id.imgbtn_score_card_upload);

        Button btn_score_card_next = findViewById(R.id.btn_score_card_next);

        ib_score_card_docs_view.setOnClickListener(this);
        imgbtn_score_card_camera.setOnClickListener(this);
        imgbtn_score_card_browse.setOnClickListener(this);
        imgbtn_score_card_upload.setOnClickListener(this);
        btn_score_card_next.setOnClickListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (mAsyncUploadFile_cif != null)
            mAsyncUploadFile_cif.cancel(true);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

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

                if (edt_cif_score_card_upload.getText().toString().replaceAll("\\s+", "").trim().equals("")){
                    mCommonMethods.showToast(mContext, "Please Enter URN");
                }else{

                    str_urn_no = edt_cif_score_card_upload.getText().toString().replaceAll("\\s+", "").trim();
                    capture_docs();
                }

                break;

            case R.id.imgbtn_score_card_browse:
                is_score_card_upload = true;
                if (edt_cif_score_card_upload.getText().toString().replaceAll("\\s+", "").trim().equals("")){
                    mCommonMethods.showToast(mContext, "Please Enter URN");
                }else{
                    str_urn_no = edt_cif_score_card_upload.getText().toString().replaceAll("\\s+", "").trim();
                    browse_docs();
                }
                break;

            case R.id.imgbtn_score_card_upload:

                str_urn_no = edt_cif_score_card_upload.getText().toString().replaceAll("\\s+", "").trim();
                str_urn_no = str_urn_no == null ? "" : str_urn_no;

                if (!str_urn_no.equals("")){
                    //1st get exam details from server and save it to locally



                    //check whether urn had already used??
                    //if (db.getExamLocation_CenterByURN(str_urn_no) == null){

                    //validate urn 1st
                    //call upload OCR docs
                    is_score_card_upload = false;

                    new AsynchValidateURN().execute();


                    /*}else{

                        //call upload tcc docs
                        upload_docs();
                    }*/
                }else{
                    mCommonMethods.showToast(mContext, "Please Enter URN Number");
                }
                break;

            case R.id.btn_score_card_next:

                if (is_score_card_upload){
                    Intent mIntent = new Intent(CIF_ScoreCard_UploadActivity.this, ScoreCardEducationalActivity.class);
                    mIntent.putExtra("URN", str_urn_no);
                    startActivity(mIntent);
                }else {
                    mCommonMethods.showMessageDialog(mContext, "Please upload Score card");
                }

                break;

            default:
                break;
        }
    }

    private ActivityResultLauncher<Intent> mBrowseDocLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            try {
                if (result.getResultCode() == RESULT_OK){
                    if (result != null && result.getData().getData() != null){

                        Uri mSelectedUri = result.getData().getData();

                        String strMimeType = mStorageUtils.getMimeType(mContext, mSelectedUri);
                        Object[] mObject = mCommonMethods.getContentURIDetails(mContext, mSelectedUri);

                        String str_extension = mObject[0].toString();
                        double kilobyte = (double) mObject[2];

                        if (!strMimeType.equals("application/octet-stream")
                                && !strMimeType.equals("application/vnd.android.package-archive")) {
                            String imageFileName =  str_urn_no+ FILE_NAME + str_extension;

                            new FileLoader(mContext, StorageUtils.DIRECT_DIRECTORY_CIF,
                                    imageFileName,
                                    new FileLoader.FileLoaderResponce() {
                                        @Override
                                        public void fileLoadFinished(File fileOutput) {
                                            if (fileOutput != null){
                                                if (str_extension.equals(".jpg") || str_extension.equals(".jpeg") || str_extension.equals(".png")){
                                                    //compress image
                                                    CompressImage.compressImage(fileOutput.getPath());
                                                }
                                                mScoreCardFile = fileOutput;
                                                long size = mScoreCardFile.length();
                                                double kilobyte = size/1024;

                                                //2 MB valiadation
                                                if (kilobyte < mCommonMethods.FILE_UPLOAD_RESTRICT_SIZE) {
                                                    imgbtn_score_card_browse.setImageDrawable(getResources().getDrawable(R.drawable.checkedbrowse));
                                                    imgbtn_score_card_camera.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_camera));
                                                }else{
                                                    mScoreCardFile = null;
                                                    mCommonMethods.showMessageDialog(mContext, mCommonMethods.FILE_UPLOAD_RESTRICT_SIZE_MSG);
                                                }
                                            }else {
                                                mScoreCardFile = null;
                                                mCommonMethods.showMessageDialog(mContext, "File Not found..");
                                            }

                                        }
                                    }).execute(mSelectedUri);

                        } else {
                            mCommonMethods.showToast(mContext, ".exe/.apk file format not acceptable");
                        }
                    }else{
                        mCommonMethods.showToast(mContext, "File Not Found!");
                    }
                }else{
                    mCommonMethods.showToast(mContext, "File browsing cancelled..");
                }
            } catch (Exception e) {
                e.printStackTrace();
                mCommonMethods.showMessageDialog(mContext, e.getMessage());
            }
        }
    });

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        String str_extension = "";
        if (requestCode == REQUEST_CODE_PICK_PHOTO_FILE){
            if (resultCode == RESULT_OK){

                CompressImage.compressImage(mScoreCardFile.getPath());

                //mScoreCardFile = mCommonMethods.compressImageCIF2(mScoreCardFile, str_urn_no, FILE_NAME+".jpg");

                if (mScoreCardFile != null){

                    long size = mScoreCardFile.length();
                    double kilobyte = size/1024;

                    // 2MB valiadation
                    if (kilobyte < mCommonMethods.FILE_UPLOAD_RESTRICT_SIZE) {
                        //mAllBitmap = mCommonMethods.ShrinkBitmap(mAllFile.getPath(), 600, 600);

                        //str_extension = mScoreCardFile.getPath().substring(mScoreCardFile.getPath().lastIndexOf("."));

                        imgbtn_score_card_camera.setImageDrawable(getResources().getDrawable(R.drawable.checkedcamera));
                        imgbtn_score_card_browse.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_browsedoc));

                /*ByteArrayOutputStream baos = new ByteArrayOutputStream();
                mAllBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);*/
                    }else{
                        mScoreCardFile = null;
                        mCommonMethods.showMessageDialog(mContext, mCommonMethods.FILE_UPLOAD_RESTRICT_SIZE_MSG);
                    }

                }else {
                    mCommonMethods.showMessageDialog(mContext, "File Size is too small");
                }
            }
        }
    }

    private void upload_docs() {

        try{

            if (mScoreCardFile != null){
                FileInputStream fin = new FileInputStream(mScoreCardFile);

                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                byte[] b = new byte[1024];
                int bytesRead = 0;
                try {
                    while ((bytesRead = fin.read(b)) != -1) {
                        bos.write(b, 0, bytesRead);
                    }

                    mScoreCardBytes = bos.toByteArray();
                    bos.flush();
                    bos.close();

                    //upload document
                    createSoapRequestToUploadDoc();

                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }else{
                Toast.makeText(mContext, "Please Browse/Capture Document", Toast.LENGTH_LONG).show();
            }

        }catch (FileNotFoundException e){
            e.printStackTrace();
        }
    }

    private void browse_docs(){
        //Intent mediaIntent = new Intent(Intent.ACTION_GET_CONTENT);
        //mediaIntent.setType("image/*"); // Set MIME type  to access only images
        //mediaIntent.setType("*/*"); // Set MIME type  to access whole storage
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

    private void capture_docs(){

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        String imageFileName = str_urn_no + FILE_NAME + ".jpg";

        mScoreCardFile = mStorageUtils.createFileToAppSpecificDirCIF(mContext, imageFileName);
        // Continue only if the File was successfully created
        if (mScoreCardFile != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mCommonMethods.getContentUri(mContext,
                        mScoreCardFile));
            } else {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mScoreCardFile));
            }
            startActivityForResult(takePictureIntent, REQUEST_CODE_PICK_PHOTO_FILE);
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
            try{

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


                    androidHttpTranport.call(NAMESPACE + METHOD_NAME_SCORE_CARD_VALIDATE_URN, envelope);
                    //Object response = envelope.getResponse();

                    SoapPrimitive sa = (SoapPrimitive) envelope.getResponse();

                    String str_res = sa.toString();
                    mParse = new ParseXML();

                    str_res = mParse.parseXmlTag(str_res, "NewDataSet");
                    if (str_res != null){

                        List<String> mData = mParse.parseParentNode(str_res, "Table");

                        if (mData.size() > 0){
                            /*for (String strXMl: mData){
                                if (mData.indexOf(strXMl) == 0){
                                    str_exam_location = mParse.parseXmlTag(strXMl, "EXAM_CENTER_LOCATION");
                                }
                            }*/
                            return "1";
                        }else {
                            return "0";
                        }
                    }else{
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
            return null;
        }

        @Override
        protected void onPostExecute(String s) {

            if (mProgressDialog.isShowing())
                mProgressDialog.dismiss();

            if (running){
                if (!s.equals("0")){
                    upload_docs();
                }else{
                    mCommonMethods.showMessageDialog(mContext, "Invalid URN");
                }
            }else{
                mCommonMethods.showMessageDialog(mContext, mCommonMethods.WEEK_INTERNET_MESSAGE);
            }
        }
    }

    private void createSoapRequestToUploadDoc(){

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME_UPLOAD_ALL_DOC);

        request.addProperty("f", mScoreCardBytes);
        request.addProperty("fileName", mScoreCardFile.getName());
        request.addProperty("URN", str_urn_no);

        //for UAT
        //request.addProperty("Authkey", mCommonMethods.getStr_cif_auth_key());

        mAsyncUploadFile_cif = new AsyncUploadFile_CIF(mContext, this, request, METHOD_NAME_UPLOAD_ALL_DOC);
        mAsyncUploadFile_cif.execute();
    }

    @Override
    public void onUploadComplete(Boolean result) {
        if (result){
            is_score_card_upload = true;

            edt_cif_score_card_upload.setEnabled(false);
            imgbtn_score_card_camera.setEnabled(false);
            imgbtn_score_card_browse.setEnabled(false);
            imgbtn_score_card_upload.setEnabled(false);

            imgbtn_score_card_upload.setImageDrawable(getResources().getDrawable(R.drawable.checkedupload));

            Toast.makeText(mContext, "Document Upload Successfully...", Toast.LENGTH_SHORT).show();

        }else{
            is_score_card_upload = false;
            Toast.makeText(mContext, "PLease try agian later..", Toast.LENGTH_SHORT).show();
        }
    }
}
