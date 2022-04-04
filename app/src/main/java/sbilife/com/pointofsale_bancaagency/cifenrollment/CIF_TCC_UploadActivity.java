package sbilife.com.pointofsale_bancaagency.cifenrollment;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
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
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

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
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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

public class CIF_TCC_UploadActivity extends AppCompatActivity implements View.OnClickListener, AsyncUploadFile_CIF.Interface_Upload_CIF_Files {

    private CommonMethods mCommonMethods;
    private StorageUtils mStorageUtils;
    private Context mContext;
    private DatabaseHelper db;

    private EditText edt_cif_tcc_exam_urn, edt_no_hrs_training;
    private TextView txt_tcc_training_start_date, txt_tcc_training_end_date;

    private ImageButton imgbtn_tcc_camera, imgbtn_tcc_browse, imgbtn_tcc_upload, ib_cif_tcc_docs_view;

    private ProgressDialog mProgressDialog;

    private boolean is_doc_uploaded = false, is_dashboard = false;

    private final int REQUEST_CODE_PICK_FILE = 2;
    private final int REQUEST_CODE_PICK_PHOTO_FILE = 3;

    private File mTCCFile;
    private byte[] mAllBytes;

    private AsynchValidateURN mAsynchValidateURN;
    private AsyncUploadFile_CIF mAsyncUploadFile_cif;

    private  final int DIALOG_EXAM_START_DATE = 100;
    private  final int DIALOG_EXAM_END_DATE = 200;

    private  final String NAMESPACE = "http://tempuri.org/";
    private final String METHOD_NAME_TCC_VALIDATE_URN = "validateURN";
    private final String METHOD_NAME_UPLOAD_ALL_DOC = "UploadFile_CIFEnroll_URN";
    private final String METHOD_NAME_TCC_GET_ALL_STATE = "getAllState_CIFEnroll";

    private String str_urn_no = "";
    private String str_urn_bundle = "";
    private String str_exam_location = "";

    private int mDay = 0, mYear = 0, mMonth = 0;

    private ArrayList<TCC_ExamDetails_Activity.TCC_ExamDetails> lstTCC = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_cif_tcc_upload);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.cifenrollment_pf_activity_title);

        initialisation();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();

        Intent i = new Intent(CIF_TCC_UploadActivity.this, CIFEnrollmentPFActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        finish();
    }

    private void initialisation(){

        mContext = this;

        mCommonMethods = new CommonMethods();
        mStorageUtils = new StorageUtils();

        mCommonMethods.setApplicationToolbarMenu1(this, "CIF on Boarding");

        db = new DatabaseHelper(mContext);

        is_dashboard = getIntent().getBooleanExtra("DASHBOARD", false);
        str_urn_no = getIntent().getStringExtra("URN");

        edt_cif_tcc_exam_urn = findViewById(R.id.edt_cif_tcc_exam_urn);
        Button btn_cif_tcc_exam_dashboard = findViewById(R.id.btn_cif_tcc_exam_dashboard);
        Button btn_cif_tcc_exam_urn_ok = findViewById(R.id.btn_cif_tcc_exam_urn_ok);

        ib_cif_tcc_docs_view = findViewById(R.id.ib_cif_tcc_docs_view);
        imgbtn_tcc_camera = findViewById(R.id.imgbtn_tcc_camera);
        imgbtn_tcc_browse = findViewById(R.id.imgbtn_tcc_browse);
        imgbtn_tcc_upload = findViewById(R.id.imgbtn_tcc_upload);

        ib_cif_tcc_docs_view.setOnClickListener(this);
        imgbtn_tcc_camera.setOnClickListener(this);
        imgbtn_tcc_browse.setOnClickListener(this);
        imgbtn_tcc_upload.setOnClickListener(this);

        edt_no_hrs_training = findViewById(R.id.edt_no_hrs_training);
        txt_tcc_training_start_date = findViewById(R.id.txt_tcc_training_start_date);

        txt_tcc_training_end_date = findViewById(R.id.txt_tcc_training_end_date);
        txt_tcc_training_end_date.setEnabled(false);

        btn_cif_tcc_exam_urn_ok.setOnClickListener(this);
        btn_cif_tcc_exam_dashboard.setOnClickListener(this);
        txt_tcc_training_start_date.setOnClickListener(this);
        txt_tcc_training_end_date.setOnClickListener(this);

        Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        Date currentDate = new Date(c.getTimeInMillis());

        //if dashboard is true the get data from database against URN and all fields are not editable
        if (is_dashboard){

            lstTCC = db.getTCCAllDetails(str_urn_no);

            //set Data
            seTCCData();
        }
    }

    private void seTCCData(){

        edt_cif_tcc_exam_urn.setText(lstTCC.get(0).getStrURN());
        edt_cif_tcc_exam_urn.setEnabled(false);

        imgbtn_tcc_camera.setEnabled(false);
        imgbtn_tcc_browse.setEnabled(false);

        imgbtn_tcc_upload.setImageDrawable(getResources().getDrawable(R.drawable.checkedupload));
        imgbtn_tcc_upload.setEnabled(false);

        txt_tcc_training_start_date.setText(lstTCC.get(0).getStr_start_date());
        txt_tcc_training_start_date.setEnabled(false);

        txt_tcc_training_end_date.setText(lstTCC.get(0).getStr_end_date());
        txt_tcc_training_end_date.setEnabled(false);

        edt_no_hrs_training.setText(lstTCC.get(0).getStr_no_training_hrs());
        edt_no_hrs_training.setEnabled(false);
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (mAsynchValidateURN != null)
            mAsynchValidateURN.cancel(true);

        if (mAsyncUploadFile_cif != null)
            mAsyncUploadFile_cif.cancel(true);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.ib_cif_tcc_docs_view:

                if (mTCCFile != null) {
                    try {
                        mCommonMethods.openAllDocs(mContext, mTCCFile);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                } else {
                    mCommonMethods.showToast(mContext, "Please Browse or Capture Document First!");
                }

                break;

            case R.id.imgbtn_tcc_camera:

                if (edt_cif_tcc_exam_urn.getText().toString().replaceAll("\\s+", "").trim().equals("")){
                    mCommonMethods.showToast(mContext, "Please Enter URN");
                }else{

                    str_urn_no = edt_cif_tcc_exam_urn.getText().toString().replaceAll("\\s+", "").trim();
                    capture_docs();
                }

                break;

            case R.id.imgbtn_tcc_browse:
                if (edt_cif_tcc_exam_urn.getText().toString().replaceAll("\\s+", "").trim().equals("")){
                    mCommonMethods.showToast(mContext, "Please Enter URN");
                }else{
                    str_urn_no = edt_cif_tcc_exam_urn.getText().toString().replaceAll("\\s+", "").trim();
                    browse_docs();
                }
                break;

            case R.id.imgbtn_tcc_upload:

                String strVal = validateAllDetails();

                if (strVal.equals("")){

                    if (!edt_cif_tcc_exam_urn.getText().toString().replaceAll("\\s+", "").trim().equals("")){
                        //1st get exam details from server and save it to locally

                        str_urn_no = edt_cif_tcc_exam_urn.getText().toString().replaceAll("\\s+", "").trim();

                        //check whether urn had already used??
                        //if (db.getExamLocation_CenterByURN(str_urn_no) == null){

                        //validate urn 1st
                        mAsynchValidateURN = new AsynchValidateURN();
                        mAsynchValidateURN.execute();

                    /*}else{

                        //call upload tcc docs
                        upload_docs();
                    }*/
                    }else{
                        mCommonMethods.showToast(mContext, "Please Enter URN Number");
                    }

                }else{
                    mCommonMethods.showMessageDialog(mContext, strVal);
                }
                break;

            case R.id.btn_cif_tcc_exam_urn_ok:

                if (is_dashboard){

                    Intent intent = new Intent(mContext, TCC_ExamDetails_Activity.class);
                    intent.putExtra("URN", str_urn_no);
                    intent.putExtra("DASHBOARD", is_dashboard);
                    startActivity(intent);

                }else {
                    //check tcc doc is uploaded or not
                    if (is_doc_uploaded){

                        String str_error = validateAllDetails();

                        if (str_error.equals("")){

                            //save details first
                            if (db.getTCCAllDetails(str_urn_no).size() == 0){
                                ContentValues cv = new ContentValues();
                                cv.put(db.TCC_EXAM_DETAILS_URN_NUMBER, str_urn_no);
                                cv.put(db.TCC_EXAM_DETAILS_START_DATE, txt_tcc_training_start_date.getText().toString());
                                cv.put(db.TCC_EXAM_DETAILS_END_DATE, txt_tcc_training_end_date.getText().toString());
                                cv.put(db.TCC_EXAM_DETAILS_NO_HRS, edt_no_hrs_training.getText().toString());
                                //update data with synch status 1 for TCC upload
                                cv.put(db.TCC_EXAM_DETAILS_SYNCH_STATUS, "1");

                                db.insert_tcc_and_exam_details(cv);
                            }else{

                                ContentValues cv = new ContentValues();
                                cv.put(db.TCC_EXAM_DETAILS_START_DATE, txt_tcc_training_start_date.getText().toString());
                                cv.put(db.TCC_EXAM_DETAILS_END_DATE, txt_tcc_training_end_date.getText().toString());
                                cv.put(db.TCC_EXAM_DETAILS_NO_HRS, edt_no_hrs_training.getText().toString());
                                //update data with synch status 1 for TCC upload
                                cv.put(db.TCC_EXAM_DETAILS_SYNCH_STATUS, "1");

                                db.update_tcc_exam_details(cv, db.TCC_EXAM_DETAILS_URN_NUMBER + " =? ",
                                        new String[]{str_urn_no});
                            }

                            Intent intent = new Intent(mContext, TCC_ExamDetails_Activity.class);
                            intent.putExtra("URN", str_urn_no);
                            intent.putExtra("EXAM_LOCATION_DETAILS", str_urn_bundle);
                            intent.putExtra("DEFAULT_EXAM_LOCATION", str_exam_location);
                            intent.putExtra("DASHBOARD", is_dashboard);
                            startActivity(intent);

                        }else{
                            mCommonMethods.showMessageDialog(mContext, str_error);
                        }
                    }else{
                        mCommonMethods.showMessageDialog(mContext, "Please upload TCC document first.");
                    }
                }
                break;

            case R.id.btn_cif_tcc_exam_dashboard:
                //show dashboard
                startActivity(new Intent(CIF_TCC_UploadActivity.this, CIF_TCC_DashboardActivity.class));

                break;

            case R.id.txt_tcc_training_start_date:
                showDialog(DIALOG_EXAM_START_DATE);
                break;

            case  R.id.txt_tcc_training_end_date:
                showDialog(DIALOG_EXAM_END_DATE);
                break;

            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        String str_extension = "";
        if (requestCode == REQUEST_CODE_PICK_PHOTO_FILE){
            if (resultCode == RESULT_OK){

                CompressImage.compressImage(mTCCFile.getPath());
                //mTCCFile = mCommonMethods.compressImageCIF2(mTCCFile, str_urn_no, "_TCC.jpg");

                if (mTCCFile != null){

                    long size = mTCCFile.length();
                    double kilobyte = size/1024;

                    //1 MB valiadation
                    if (kilobyte < mCommonMethods.FILE_UPLOAD_RESTRICT_SIZE) {
                        //mAllBitmap = mCommonMethods.ShrinkBitmap(mAllFile.getPath(), 600, 600);

                        //str_extension = mTCCFile.getPath().substring(mTCCFile.getPath().lastIndexOf("."));

                        imgbtn_tcc_camera.setImageDrawable(getResources().getDrawable(R.drawable.checkedcamera));
                        imgbtn_tcc_browse.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_browsedoc));
                    }else{
                        mTCCFile = null;
                        mCommonMethods.showMessageDialog(mContext, mCommonMethods.FILE_UPLOAD_RESTRICT_SIZE_MSG);
                    }

                }else {
                    mCommonMethods.showMessageDialog(mContext, "File Size is too small");
                }

                /*ByteArrayOutputStream baos = new ByteArrayOutputStream();
                mAllBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);*/
            }
        }
    }

    class AsynchValidateURN extends AsyncTask<String, String, String>{

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

                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME_TCC_VALIDATE_URN);
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

                    androidHttpTranport.call(NAMESPACE + METHOD_NAME_TCC_VALIDATE_URN, envelope);
                    //Object response = envelope.getResponse();

                    SoapPrimitive sa = (SoapPrimitive) envelope.getResponse();

                    String str_res = sa.toString();
                    mParse = new ParseXML();

                    str_res = mParse.parseXmlTag(str_res, "NewDataSet");
                    if (str_res != null){

                        List<String> mData = mParse.parseParentNode(str_res, "Table");

                        if (mData.size() > 0){
                            for (String strXMl: mData){
                                if (mData.indexOf(strXMl) == 0){
                                    str_exam_location = mParse.parseXmlTag(strXMl, "EXAM_CENTER_LOCATION");
                                }
                            }

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

                    if (!str_exam_location.equals("")){
                        //get all state data
                        AsynchGetAllState mAsynchGetAllState = new AsynchGetAllState();
                        mAsynchGetAllState.execute();
                    }else {
                        mCommonMethods.showMessageDialog(mContext, "Invalid URN");
                    }

                }else{
                    mCommonMethods.showMessageDialog(mContext, "Invalid URN");
                }
            }else{
                mCommonMethods.showMessageDialog(mContext, mCommonMethods.WEEK_INTERNET_MESSAGE);
            }
        }
    }

    private void upload_docs() {

        is_doc_uploaded = false;

        try{

            if (mTCCFile != null){
                FileInputStream fin = new FileInputStream(mTCCFile);

                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                byte[] b = new byte[1024];
                int bytesRead = 0;
                try {
                    while ((bytesRead = fin.read(b)) != -1) {
                        bos.write(b, 0, bytesRead);
                    }

                    mAllBytes = bos.toByteArray();
                    bos.flush();
                    bos.close();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //upload document
                            createSoapRequestToUploadDoc();
                        }
                    });

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

                        if (!strMimeType.equals("application/octet-stream")
                                && !strMimeType.equals("application/vnd.android.package-archive")){
                            new FileLoader(mContext, StorageUtils.DIRECT_DIRECTORY_CIF,
                                    str_urn_no + "_TCC" + str_extension,
                                    new FileLoader.FileLoaderResponce() {
                                        @Override
                                        public void fileLoadFinished(File fileOutput) {
                                            if (fileOutput != null){
                                                if (str_extension.equals(".jpg") || str_extension.equals(".jpeg") || str_extension.equals(".png")){
                                                    CompressImage.compressImage(fileOutput.getPath());
                                                }
                                                mTCCFile = fileOutput;
                                                long size = mTCCFile.length();
                                                double kilobyte = size/1024;

                                                //2 MB valiadation
                                                if (kilobyte < mCommonMethods.FILE_UPLOAD_RESTRICT_SIZE) {
                                                    imgbtn_tcc_browse.setImageDrawable(getResources().getDrawable(R.drawable.checkedbrowse));
                                                    imgbtn_tcc_camera.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_camera));
                                                }else{
                                                    mTCCFile = null;
                                                    mCommonMethods.showMessageDialog(mContext, mCommonMethods.FILE_UPLOAD_RESTRICT_SIZE_MSG);
                                                }
                                            }else {
                                                mTCCFile = null;
                                                mCommonMethods.showMessageDialog(mContext, "File Not Found..");
                                            }

                                        }
                                    }).execute(mSelectedUri);
                        }else{
                            mTCCFile = null;
                            mCommonMethods.showToast(mContext, ".exe/.apk file format not acceptable");
                        }

                    }else {
                        mCommonMethods.showToast(mContext, "File Not Found!");
                    }
                }else {
                    mCommonMethods.showToast(mContext, "File browsing cancelled..");
                }
            } catch (Exception e) {
                e.printStackTrace();
                mCommonMethods.showMessageDialog(mContext, "Please Select Files from Your External Storage");
            }
        }
    });

    private void capture_docs(){

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        String imageFileName = str_urn_no + "_TCC.jpg";

        mTCCFile = mStorageUtils.createFileToAppSpecificDirCIF(mContext, imageFileName);
        // Continue only if the File was successfully created
        if (mTCCFile != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mCommonMethods.getContentUri(mContext, mTCCFile));
            }else{
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mTCCFile));
            }
            startActivityForResult(takePictureIntent, REQUEST_CODE_PICK_PHOTO_FILE);
        }
    }

    private String validateAllDetails(){

        if (txt_tcc_training_start_date.getText().toString().equals("")){
            return "Please Select Start Date";
        }else if (txt_tcc_training_end_date.getText().toString().equals("")){
            return "Please Select End Date";
        }else if (edt_no_hrs_training.getText().toString().equals("")){
            return "Please Enter No. of Training Hours";
        }else{
            return "";
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id){

            case DIALOG_EXAM_START_DATE:
                return new DatePickerDialog(this, R.style.datepickerstyle, mDateListenerStart, mYear,
                        mMonth, mDay);

            case DIALOG_EXAM_END_DATE:
                return new DatePickerDialog(this, R.style.datepickerstyle, mDateListenerEnd, mYear,
                        mMonth, mDay);

            default:
                return null;

        }
    }

    private DatePickerDialog.OnDateSetListener mDateListenerStart = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

            String strSelectedDate = (dayOfMonth < 10 ? "0" : "") + dayOfMonth + "-"
                    + ((monthOfYear + 1) < 10 ? "0" : "") + (monthOfYear + 1) + "-"
                    + year;
            //try{
                /*SimpleDateFormat sdp = new SimpleDateFormat("dd-MM-yyyy");
                Date selectedDate = sdp.parse(strSelectedDate);*/

                //if (!selectedDate.after(currentDate) && !strSelectedDate.equals(sdp.format(currentDate))){
                    txt_tcc_training_start_date.setText(strSelectedDate);

                    txt_tcc_training_end_date.setEnabled(true);
                /*}else{
                    mCommonMethods.showMessageDialog(mContext, "Future / current date not allowed");
                }*/
            /*}catch (ParseException e){
                e.printStackTrace();
                mCommonMethods.showToast(mContext, "parse error \n"+e.getMessage());
            }*/
        }
    };

    private DatePickerDialog.OnDateSetListener mDateListenerEnd = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

            String strSelectedDate = (dayOfMonth < 10 ? "0" : "") + dayOfMonth + "-"
                    + ((monthOfYear + 1) < 10 ? "0" : "") + (monthOfYear + 1) + "-"
                    + year;

            try{
                SimpleDateFormat sdp = new SimpleDateFormat("dd-MM-yyyy");
                Date selectedDate = sdp.parse(strSelectedDate);

                String strStartDate = txt_tcc_training_start_date.getText().toString();
                if (!strStartDate.equals("")){
                    Date startDate = sdp.parse(strStartDate);

                    if (selectedDate.after(startDate)){
                        txt_tcc_training_end_date.setText(strSelectedDate);
                    }else{
                        mCommonMethods.showMessageDialog(mContext, "End Date should be greater than Start date");
                    }
                }
            }catch (ParseException e){
                e.printStackTrace();
                mCommonMethods.showToast(mContext, "parse error \n"+e.getMessage());
            }
        }
    };

    class AsynchGetAllState extends AsyncTask<String, String, String>{

        private volatile boolean running = true;
        private ParseXML mParse;

        @Override
        protected void onPreExecute() {
            mProgressDialog = new ProgressDialog(mContext,
                    ProgressDialog.THEME_HOLO_LIGHT);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog
                    .setTitle(Html.fromHtml("<font color='#00a1e3'><b>Please Wait...<b></font>"));
            mProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            try{
                running = true;

                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME_TCC_GET_ALL_STATE);

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

                    androidHttpTranport.call(NAMESPACE + METHOD_NAME_TCC_GET_ALL_STATE, envelope);
                    //Object response = envelope.getResponse();

                    SoapPrimitive sa = (SoapPrimitive) envelope.getResponse();

                    str_urn_bundle = sa.toString();
                    mParse = new ParseXML();
                    String str_is_error = mParse.parseXmlTag(str_urn_bundle, "NewDataSet");
                    if (str_is_error != null){
                        if (mParse.parseParentNode(str_urn_bundle, "Table").size() > 0) {
                            return str_urn_bundle;
                        }else{
                            return str_urn_bundle = "0";
                        }
                    }else{
                        return str_urn_bundle = "0";
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

            if (running){
                if (!str_urn_bundle.equals("0")){
                    //upload document
                    upload_docs();
                }else {
                    mCommonMethods.showMessageDialog(mContext, "Invalid URN");
                }
            }else {
                mCommonMethods.showMessageDialog(mContext, mCommonMethods.WEEK_INTERNET_MESSAGE);
            }
        }
    }

    private void createSoapRequestToUploadDoc(){
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME_UPLOAD_ALL_DOC);

        request.addProperty("f", mAllBytes);
        request.addProperty("fileName", mTCCFile.getName());
        request.addProperty("URN", str_urn_no);

        //for UAT
        //request.addProperty("Authkey", mCommonMethods.getStr_cif_auth_key());

        mAsyncUploadFile_cif = new AsyncUploadFile_CIF(mContext, this, request, METHOD_NAME_UPLOAD_ALL_DOC);
        mAsyncUploadFile_cif.execute();
    }

    @Override
    public void onUploadComplete(Boolean result) {
        if (result){
            edt_cif_tcc_exam_urn.setEnabled(false);
            imgbtn_tcc_camera.setEnabled(false);
            imgbtn_tcc_browse.setEnabled(false);
            imgbtn_tcc_upload.setEnabled(false);

            imgbtn_tcc_upload.setImageDrawable(getResources().getDrawable(R.drawable.checkedupload));

            Toast.makeText(mContext, "Document Upload Successfully...", Toast.LENGTH_SHORT).show();

            is_doc_uploaded = true;

        }else{
            is_doc_uploaded = false;

            Toast.makeText(mContext, "PLease try agian later..", Toast.LENGTH_SHORT).show();
        }
    }
}
