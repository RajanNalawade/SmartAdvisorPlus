package sbilife.com.pointofsale_bancaagency.utility;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.google.android.gms.maps.model.LatLng;
import com.xbizventures.ocrlib.OcrActivity;

import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import sbilife.com.pointofsale_bancaagency.R;
import sbilife.com.pointofsale_bancaagency.ServiceURL;
import sbilife.com.pointofsale_bancaagency.branchlocator.GPSTracker;
import sbilife.com.pointofsale_bancaagency.common.AsyncUploadFile_Common;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;
import sbilife.com.pointofsale_bancaagency.common.CompressImage;
import sbilife.com.pointofsale_bancaagency.common.FileLoader;
import sbilife.com.pointofsale_bancaagency.common.StorageUtils;
import sbilife.com.pointofsale_bancaagency.reports.ServiceHits;

public class AllDocumentsUploadActivity extends AppCompatActivity implements View.OnClickListener,
        ServiceHits.DownLoadData, AsyncUploadFile_Common.Interface_Upload_File_Common {

    private static final int REQUEST_CODE_PICK_FILE = 2;
    private final String METHOD_NAME_UPLOAD_ALL_DOC = "UploadFile_SMRT";
    private final String METHOD_NAME_VALIDATE_PROPOSAL = "validateProp_smrt";
    private final String NAMESPACE = "http://tempuri.org/";
    private final int REQUEST_OCR = 1, PERMS_RUNTIME_REQ_CODE = 100;
    private final int REQUEST_CODE_PICK_PHOTO_FILE = 3;
    private final String JPEG_FILE_SUFFIX = ".jpg";
    private Context mContext;
    private CommonMethods mCommonMethods;
    private EditText edt_upload_all_docs;
    private ImageButton ib_upload_all_docs_capture, ib_upload_all_docs_browse, ib_upload_all_docs_view;
    private File mAllFile;
    private Bitmap mAllBitmap;
    private byte[] mAllBytes;
    private String strfileName = "", str_extension = "", OCR_TYPE = "";
    private int increment_val = 1;
    private ProgressDialog mProgressDialog;
    private AsyncUploadFile_Common mAsyncUploadFileCommon;
    private boolean val_proposer_no = false;
    private LatLng mLatLng;
    private GPSTracker mGPGpsTracker;

    private StorageUtils mStorageUtils;

    private ActivityResultLauncher<Intent> mBrowseDocLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            try {
                if (result.getResultCode() == RESULT_OK){
                    if (result != null && result.getData().getData() != null){

                        Uri mSelectedUri = result.getData().getData();

                        String strMimeType = mStorageUtils.getMimeType(mContext, mSelectedUri);
                        Object[] mObject = mCommonMethods.getContentURIDetails(mContext, mSelectedUri);

                        str_extension = mObject[0].toString();
                        double kilobyte = (double) mObject[2];

                        if (!strMimeType.equals("application/octet-stream")
                                && !strMimeType.equals("application/vnd.android.package-archive")) {

                            if (strMimeType.equals("image/jpeg")
                                    || strMimeType.equals("image/gif")) {

                                mCommonMethods.showToast(mContext, ".jpg/.jpeg/.gif file format please select another option.");
                                mAllFile = null;
                            } else {

                                strfileName = edt_upload_all_docs.getText().toString().replaceAll("\\s", "");

                                new FileLoader(mContext, StorageUtils.DIRECT_DIRECTORY,
                                        strfileName + "_R" + increment_val + str_extension,
                                        new FileLoader.FileLoaderResponce() {
                                            @Override
                                            public void fileLoadFinished(File fileOutput) {
                                                if (fileOutput != null){
                                                    mAllFile = fileOutput;
                                                    ib_upload_all_docs_browse.setImageDrawable(getResources().getDrawable(R.drawable.checkedbrowse));
                                                }else {
                                                    mAllFile = null;
                                                    ib_upload_all_docs_browse.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_browsedoc));
                                                }

                                            }
                                        }).execute(mSelectedUri);
                            }

                        } else {
                            mCommonMethods.showToast(mContext, ".exe/.apk file format not acceptable");
                            mAllFile = null;
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

    private ActivityResultLauncher<Intent> mOCRActivityLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == RESULT_OK){

                strfileName = edt_upload_all_docs.getText().toString().replaceAll("\\s", "");

                File imagePath = null;
                String DocumentType = "";
                Bundle bundle = result.getData().getExtras();

                if (bundle != null) {
                    String jsonData = (String) bundle.get("jsonData");

                    try {
                        JSONObject object = new JSONObject(jsonData);

                        DocumentType = object.get("DocumentType").toString();

                        imagePath = new File(bundle.get("BitmapImageUri").toString());
                        //Bitmap edgeBitmap = BitmapFactory.decodeFile(imagePath.getAbsolutePath());

                        mAllFile = null;

                        if (imagePath != null) {

                            String str_Doc_Abbreviation = "";

                            if (DocumentType.toLowerCase().equalsIgnoreCase("Pancard".toLowerCase())
                                    || DocumentType.toLowerCase().equalsIgnoreCase("PAN".toLowerCase())) {
                                str_Doc_Abbreviation = "PAN";
                            } else if (DocumentType.toLowerCase().equalsIgnoreCase("Aadhaar Card".toLowerCase())
                                    || DocumentType.toLowerCase().equalsIgnoreCase("Aadhaar card with complete DOB".toLowerCase())
                                    || DocumentType.toLowerCase().equalsIgnoreCase("Aadhaar card with incomplete DOB".toLowerCase())
                                    || DocumentType.toLowerCase().equalsIgnoreCase("Aadhar Card".toLowerCase())
                                    || DocumentType.toLowerCase().contains("AADHAAR".toLowerCase())
                                    || DocumentType.toLowerCase().equalsIgnoreCase("Aadhar card with complete DOB".toLowerCase())
                                    || DocumentType.toLowerCase().equalsIgnoreCase("Aadhar card with incomplete DOB".toLowerCase())) {
                                str_Doc_Abbreviation = "AADHAR";
                            } else if (DocumentType.toLowerCase().equalsIgnoreCase("Driving Licence".toLowerCase())) {
                                str_Doc_Abbreviation = "DL";
                            } else if (DocumentType.toLowerCase().equalsIgnoreCase("Voter s Identity Card".toLowerCase())) {
                                str_Doc_Abbreviation = "VID";
                            } else if (DocumentType.toLowerCase().equalsIgnoreCase("Passport".toLowerCase())) {
                                str_Doc_Abbreviation = "PASSPORT";
                            } else if (DocumentType.toLowerCase().equalsIgnoreCase("Job Card issued by NREGA duly signed by an officer of State Govt".toLowerCase())) {
                                str_Doc_Abbreviation = "NJC";
                            } else if (DocumentType.toLowerCase().equalsIgnoreCase("Copy of Bank Statement".toLowerCase())
                                    || DocumentType.toLowerCase().equalsIgnoreCase("Cancelled Cheque".toLowerCase())) {
                                str_Doc_Abbreviation = "BKST";
                            } else if (DocumentType.toLowerCase().equalsIgnoreCase("Cheque".toLowerCase())) {
                                str_Doc_Abbreviation = "CHQ";
                            } else {
                                str_Doc_Abbreviation = "OTHER";
                            }

                            String imageFileName = strfileName + "_" + str_Doc_Abbreviation + "_R" + increment_val + ".jpg";

                            //image compression by bhalla
                            CompressImage.compressImage(imagePath.getAbsolutePath());

                            mAllFile = mStorageUtils.saveFileToAppSpecificDir(mContext,
                                    StorageUtils.DIRECT_DIRECTORY,imageFileName, imagePath);

                            if (OCR_TYPE.equals("browse")) {
                                ib_upload_all_docs_browse.setImageDrawable(getResources().getDrawable(R.drawable.checkedbrowse));
                                ib_upload_all_docs_capture.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_camera));
                            } else if (OCR_TYPE.equals("capture")) {
                                ib_upload_all_docs_browse.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_browsedoc));
                                ib_upload_all_docs_capture.setImageDrawable(getResources().getDrawable(R.drawable.checkedcamera));
                            }
                        } else {
                            mCommonMethods.showToast(mContext, "Blanck file Path");
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        mCommonMethods.showToast(mContext, e.getMessage());
                    }
                }
            } else {
                Toast.makeText(mContext, "Data not receive", Toast.LENGTH_SHORT).show();
            }
        }
    });

    @SuppressWarnings("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_all_documents_upload);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);

        mContext = this;
        mCommonMethods = new CommonMethods();
        mStorageUtils = new StorageUtils();
        mCommonMethods.setApplicationToolbarMenu(this, "All Documents Upload");

        edt_upload_all_docs = findViewById(R.id.edt_upload_all_docs);
        ib_upload_all_docs_capture = findViewById(R.id.ib_upload_all_docs_capture);
        ib_upload_all_docs_capture.setOnClickListener(this);
        ib_upload_all_docs_browse = findViewById(R.id.ib_upload_all_docs_browse);
        ib_upload_all_docs_browse.setOnClickListener(this);
        ImageButton ib_upload_all_docs_ok = findViewById(R.id.ib_upload_all_docs_ok);
        ib_upload_all_docs_ok.setOnClickListener(this);
        ib_upload_all_docs_view = findViewById(R.id.ib_upload_all_docs_view);
        ib_upload_all_docs_view.setOnClickListener(this);

        String str_proposal = getIntent().getStringExtra("PROPOSAL_NO");

        if (!str_proposal.equals("")) {

            val_proposer_no = true;

            edt_upload_all_docs.setText(str_proposal);
            edt_upload_all_docs.setEnabled(false);
        }

        //txt_upload_all_note.setText(Html.fromHtml("<b>Note : </b>"+ "You can upload requirement documents through capture or browse against proposal no. Document size ideally less than 1 MB"));

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
                    edt_upload_all_docs.setError(null);
                } else {
                    val_proposer_no = false;
                    edt_upload_all_docs.setError("Enter Valid Proposal No.");
                }

            }
        });

        mGPGpsTracker = new GPSTracker(mContext);
        mLatLng = new LatLng(0.0, 0.0);

        getLocationPromt();

    }

    private void getLocationPromt() {
        LocationManager locationmanager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        if (locationmanager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

            try {
                mLatLng = mCommonMethods.getCurrentLocation(mContext, mGPGpsTracker);

                if (mLatLng.latitude == 0.0 && mLatLng.longitude == 0.0) {
                    mCommonMethods.showToast(mContext, "Please check your gps connection and try again");
                    mLatLng = new LatLng(0.0, 0.0);
                    mLatLng = mCommonMethods.getCurrentLocation(mContext, mGPGpsTracker);
                }

            } catch (Exception ex) {
                ex.printStackTrace();
                mLatLng = mCommonMethods.getCurrentLocation(mContext, mGPGpsTracker);
            }
        } else {
            mCommonMethods.showGPSDisabledAlertToUser(mContext);
        }
    }

    @Override
    public void onClick(View v) {

        Intent intent;

        switch (v.getId()) {

            case R.id.ib_upload_all_docs_browse:

                final Dialog dialog = new Dialog(mContext);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.loading_window_twobutton);
                TextView text = dialog.findViewById(R.id.txtalertheader);
                text.setText("Do you want browse image (JPG/JPEG) format document?");
                Button dialogButton = dialog.findViewById(R.id.btnalert);
                dialogButton.setText(getResources().getText(R.string.yes));
                dialogButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        dialog.dismiss();

                        OCR_TYPE = "browse";

                        Intent intent = new Intent(AllDocumentsUploadActivity.this, OcrActivity.class);
                        //startActivityForResult(intent, REQUEST_OCR);
                        mOCRActivityLauncher.launch(intent);

                    }
                });
                Button dialogButtoncancel = dialog.findViewById(R.id.btnalertcancel);
                dialogButtoncancel.setText(getResources().getText(R.string.no));
                dialogButtoncancel.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        dialog.dismiss();

                        browse_all_docs();
                    }
                });

                dialog.show();

                break;

            case R.id.ib_upload_all_docs_capture:

                OCR_TYPE = "capture";

                intent = new Intent(AllDocumentsUploadActivity.this, OcrActivity.class);
                //startActivityForResult(intent, REQUEST_OCR);
                mOCRActivityLauncher.launch(intent);

                //capture_all_docs();
                break;

            case R.id.ib_upload_all_docs_ok:

                service_hits(METHOD_NAME_UPLOAD_ALL_DOC);

                break;

            case R.id.ib_upload_all_docs_view:

                if (mAllFile != null) {
                    try {
                        mCommonMethods.openAllDocs(mContext, mAllFile);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                } else {
                    mCommonMethods.showToast(mContext, "Please Browse or Capture Document First!");
                }

                break;

            default:
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (mAsyncUploadFileCommon != null)
            mAsyncUploadFileCommon.cancel(true);
    }

    private void capture_all_docs() {
        if (val_proposer_no) {

            try {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                strfileName = edt_upload_all_docs.getText().toString().replaceAll("\\s", "");

                String imageFileName = strfileName + "_R" + increment_val + ".jpg";

                mAllFile = mStorageUtils.createFileToAppSpecificDir(mContext, imageFileName);

                // Continue only if the File was successfully created
                if (mAllFile != null) {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mCommonMethods.getContentUri(mContext,
                                mAllFile));
                    } else {
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mAllFile));
                    }
                    startActivityForResult(takePictureIntent, REQUEST_CODE_PICK_PHOTO_FILE);
                }
            } catch (Exception exp) {
                exp.printStackTrace();
                mCommonMethods.printLog("Capture : ", exp.getMessage());
                mAllFile = null;
            }
            //Nought changes new ends

        } else {
            Toast.makeText(mContext, "Please Enter Proposal No.", Toast.LENGTH_LONG).show();
        }
    }

    private void upload_all_docs() {


        if (val_proposer_no) {

            strfileName = edt_upload_all_docs.getText().toString().replaceAll("\\s", "");

            if (mAllFile != null) {

                if (str_extension.equals(".png") || str_extension.equals(".jpeg") ||
                        str_extension.equals(".jpg")) {

                    String textPrint = "Lat: " + mLatLng.latitude + ", Long: " + mLatLng.longitude;

                    String stringToParse = "";
                    StringBuffer sb;
                    if (mLatLng.latitude == 0.0 && mLatLng.longitude == 0.0) {
                        stringToParse = "Address: \n";
                    } else {
                        stringToParse = "Address: " + mCommonMethods.getCompleteAddressString(mContext, mLatLng.latitude, mLatLng.longitude);
                    }
                    sb = new StringBuffer(stringToParse);

                    int i = 0;
                    while ((i = sb.indexOf(" ", i + 25)) != -1) {
                        sb.replace(i, i + 1, "\n");
                    }

                    textPrint += "\n" + mCommonMethods.GetUserType(mContext) + " Name: " + mCommonMethods.getUserName(mContext)
                            + ", " + mCommonMethods.GetUserType(mContext) + " ID: " + mCommonMethods.GetUserCode(mContext)
                            + "\n" + sb.toString()
                            + "Date: " + mCommonMethods.getCurrentTimeStamp();

                    Bitmap newBitmap = mCommonMethods.mergeBitmap(mCommonMethods.getBitmap(mAllFile.getAbsolutePath()),
                            mCommonMethods.convertStringToBitMap(mContext, textPrint));

                    //save bitmap to file location
                    //mCommonMethods.storeImage(mContext, newBitmap, mAllFile.getAbsolutePath());

                    //convert bitmap to byte[]
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    newBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    mAllBytes = stream.toByteArray();

                } else {
                    mAllBytes = mCommonMethods.read(mAllFile);
                }

                if(mAllBytes != null){
                    //upload document
                    createSoapRequestToUploadDoc();
                }else{
                    mCommonMethods.showMessageDialog(mContext, "File Null Error..");
                }

            } else {
                Toast.makeText(mContext, "Please Browse/Capture Document", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(mContext, "Please Enter Proposal No.", Toast.LENGTH_LONG).show();
        }
    }

    private void browse_all_docs() {

        if (val_proposer_no) {

            Intent mIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            mIntent.addCategory(Intent.CATEGORY_OPENABLE);
            mIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION
                    | Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
            mIntent.setType("application/pdf");
            //mIntent.setType("*/*");
            /*String[] mimeType = new String[]{"application/x-binary,application/octet-stream"};
            if(mimeType.length > 0) {
                mIntent.putExtra(Intent.EXTRA_MIME_TYPES, mimeType);
            }*/
            mBrowseDocLauncher.launch(mIntent);

        } else {
            Toast.makeText(mContext, "Please Enter Proposal No.", Toast.LENGTH_LONG).show();
        }
    }

    private void service_hits(String strServiceName) {
        CommonMethods.UserDetailsValuesModel userDetails = mCommonMethods
                .setUserDetails(mContext);

        ServiceHits service = new ServiceHits(this,
                strServiceName, "", userDetails.getStrCIFBDMUserId(),
                userDetails.getStrCIFBDMEmailId(), userDetails.getStrCIFBDMMObileNo(),
                mCommonMethods.GetUserPassword(mContext), this);
        service.execute();
    }

    @Override
    public void downLoadData() {

        //first validate proposal number
        new AsycValidateProposalNo().execute(
                edt_upload_all_docs.getText().toString().replaceAll("\\s", "")
        );
    }

    private void createSoapRequestToUploadDoc() {
        SoapObject request = new SoapObject(NAMESPACE,
                METHOD_NAME_UPLOAD_ALL_DOC);

        request.addProperty("f", mAllBytes);

        /*request.addProperty("fileName", strfileName+"_R"+increment_val+".tif");*/
        //request.addProperty("fileName", strfileName + "_R" + increment_val + str_extension);
        request.addProperty("fileName", mAllFile.getName());
        request.addProperty("qNo", "");
        request.addProperty("agentCode", mCommonMethods.GetUserID(mContext));
        request.addProperty("strEmailId", mCommonMethods.GetUserEmail(mContext));
        request.addProperty("strMobileNo", mCommonMethods.GetUserMobile(mContext));
        request.addProperty("strAuthKey", "");

        mAsyncUploadFileCommon = new AsyncUploadFile_Common(mContext, this, request, METHOD_NAME_UPLOAD_ALL_DOC);
        mAsyncUploadFileCommon.execute();
    }

    @Override
    public void onUploadComplete(Boolean result) {
        if (result) {

            if (mAllFile != null) {
                mAllFile.delete();
            }

            mAllFile = null;
            mAllBytes = null;
            mAllBitmap = null;
            str_extension = "";

            strfileName = "";

            increment_val++;

            ib_upload_all_docs_browse
                    .setImageDrawable(getResources().getDrawable(R.drawable.ibtn_browsedoc));

            ib_upload_all_docs_capture.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_camera));

            Toast.makeText(mContext, "Document Upload Successfully...", Toast.LENGTH_SHORT).show();
        } else {
            mCommonMethods.showToast(mContext, "PLease try agian later..");
        }
    }

    class AsycValidateProposalNo extends AsyncTask<String, String, String> {

        private volatile boolean running = true;
        private String str_error = "";

        @Override
        protected void onPreExecute() {
            mProgressDialog = new ProgressDialog(mContext, ProgressDialog.THEME_HOLO_LIGHT);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.setTitle(Html.fromHtml("<font color='#00a1e3'><b>Please Wait...<b></font>"));
            mProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... aurl) {

            if (mCommonMethods.isNetworkConnected(mContext)) {
                try {

                    running = true;
                    SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME_VALIDATE_PROPOSAL);

                    request.addProperty("strCode", mCommonMethods.GetUserCode(mContext));
                    request.addProperty("strProposalNo", aurl[0]);
                    request.addProperty("Type", mCommonMethods.GetUserType(mContext));
                    mCommonMethods.appendSecurityParams(mContext, request,
                            mCommonMethods.GetUserEmail(mContext), mCommonMethods.GetUserMobile(mContext));

                    mCommonMethods.TLSv12Enable();

                    SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                            SoapEnvelope.VER11);
                    envelope.dotNet = true;

                    envelope.setOutputSoapObject(request);

                    HttpTransportSE androidHttpTranport = new HttpTransportSE(ServiceURL.SERVICE_URL);

                    androidHttpTranport.call(NAMESPACE + METHOD_NAME_VALIDATE_PROPOSAL, envelope);

                    SoapPrimitive sa;
                    try {

                        sa = (SoapPrimitive) envelope.getResponse();
                        String inputpolicylist = sa.toString();

                        if (inputpolicylist.equals("0")) {
                            str_error = "Invalid Proposal Number";
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        running = false;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    running = false;
                }
            } else {
                running = true;
                str_error = mCommonMethods.NO_INTERNET_MESSAGE;
            }
            return null;
        }


        @Override
        protected void onPostExecute(String unused) {
            if (mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }

            if (running) {

                if (str_error.equals("")) {
                    upload_all_docs();
                } else {
                    mCommonMethods.showMessageDialog(mContext, str_error);
                }
            } else {
                mCommonMethods.showMessageDialog(mContext, mCommonMethods.SERVER_ERROR);
            }
        }
    }
}
