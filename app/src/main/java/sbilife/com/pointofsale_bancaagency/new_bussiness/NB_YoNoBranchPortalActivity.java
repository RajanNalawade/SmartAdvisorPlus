package sbilife.com.pointofsale_bancaagency.new_bussiness;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.helper.widget.Flow;

import com.xbizventures.ocrlib.OcrActivity;

import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import sbilife.com.pointofsale_bancaagency.ParseXML;
import sbilife.com.pointofsale_bancaagency.R;
import sbilife.com.pointofsale_bancaagency.ServiceURL;
import sbilife.com.pointofsale_bancaagency.common.AsyncUploadFile_Common;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;
import sbilife.com.pointofsale_bancaagency.common.CompressImage;
import sbilife.com.pointofsale_bancaagency.common.FileLoader;
import sbilife.com.pointofsale_bancaagency.common.StorageUtils;
import sbilife.com.pointofsale_bancaagency.reports.ServiceHits;

public class NB_YoNoBranchPortalActivity extends AppCompatActivity implements View.OnClickListener,
        ServiceHits.DownLoadData, AsyncUploadFile_Common.Interface_Upload_File_Common {

    private CommonMethods mCommonMethods;
    private StorageUtils mStorageUtils;
    private Context mContext;

    private final String NAMESPACE = "http://tempuri.org/";
    private final String METHOD_NAME_GET_YoNo_Pending_KYC = "getLoggedPropCout_notifyKYC_yono";
    private final String METHOD_NAME_UPLOAD_DOC = "UploadFile";

    private List<ParseXML.XMLHolderYoNoPendingKYC> lstYoNoProposal = new ArrayList<>();

    private TextView txt_yono_pending_error;
    private Spinner spnr_yono_pending_proposals;
    private Flow flow_yono_upload;
    private ImageButton ib_yono_pending_doc_view, ib_yono_pending_doc_capture, ib_yono_pending_doc_browse,
            ib_yono_pending_doc_upload;

    private ProgressDialog mProgressDialog;

    private AsynchGetYoNoPendingKYC mAsynchGetYoNoPendingKYC;
    private AsyncUploadFile_Common mAsyncUploadFileCommon;

    private File mAllFile;
    private byte[] mAllBytes;
    private String strfileName = "", str_extension = "", OCR_TYPE = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nb_yo_no_branch_portal);

        initialise();

    }

    private void initialise(){

        mContext = this;
        mCommonMethods = new CommonMethods();
        mStorageUtils = new StorageUtils();

        mCommonMethods.setApplicationToolbarMenu(this, "YoNo Branch Portal");

        txt_yono_pending_error = findViewById(R.id.txt_yono_pending_error);
        spnr_yono_pending_proposals = findViewById(R.id.spnr_yono_pending_proposals);
        flow_yono_upload = findViewById(R.id.flow_yono_upload);

        ib_yono_pending_doc_view = findViewById(R.id.ib_yono_pending_doc_view);
        ib_yono_pending_doc_view.setOnClickListener(this);
        ib_yono_pending_doc_capture = findViewById(R.id.ib_yono_pending_doc_capture);
        ib_yono_pending_doc_capture.setOnClickListener(this);
        ib_yono_pending_doc_browse = findViewById(R.id.ib_yono_pending_doc_browse);
        ib_yono_pending_doc_browse.setOnClickListener(this);
        ib_yono_pending_doc_upload = findViewById(R.id.ib_yono_pending_doc_upload);
        ib_yono_pending_doc_upload.setOnClickListener(this);

        spnr_yono_pending_proposals.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mAllFile = null;
                mAllBytes = null;
                str_extension = "";

                strfileName = "";

                ib_yono_pending_doc_browse.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_browsedoc));
                ib_yono_pending_doc_capture.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_camera));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mAsynchGetYoNoPendingKYC = new AsynchGetYoNoPendingKYC();
        mAsynchGetYoNoPendingKYC.execute();
    }

    @Override
    protected void onDestroy() {

        if (mAsynchGetYoNoPendingKYC != null)
            mAsynchGetYoNoPendingKYC.cancel(true);

        if (mAsyncUploadFileCommon != null)
            mAsyncUploadFileCommon.cancel(true);

        super.onDestroy();
    }

    @Override
    public void onClick(View v) {

        if (spnr_yono_pending_proposals.getSelectedItemPosition() == 0){
            mCommonMethods.showToast(mContext, "Please Select Proposal Number");
        }else{
            Intent intent;
            switch (v.getId()){
                case R.id.ib_yono_pending_doc_view:
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

                case R.id.ib_yono_pending_doc_capture:
                    OCR_TYPE = "capture";

                    intent = new Intent(NB_YoNoBranchPortalActivity.this, OcrActivity.class);
                    //startActivityForResult(intent, REQUEST_OCR);
                    mOCRActivityLauncher.launch(intent);
                    break;

                case R.id.ib_yono_pending_doc_browse:
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

                            Intent intent = new Intent(NB_YoNoBranchPortalActivity.this,
                                    OcrActivity.class);
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

                case R.id.ib_yono_pending_doc_upload:
                    if (mAllFile != null) {
                        service_hits(METHOD_NAME_UPLOAD_DOC);
                    } else {
                        mCommonMethods.showToast(mContext, "Please Browse or Capture Document First!");
                    }
                    break;

                default:
                    break;
            }
        }
    }

    private ActivityResultLauncher<Intent> mOCRActivityLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == RESULT_OK){

                strfileName = spnr_yono_pending_proposals.getSelectedItem().toString();

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

                            String imageFileName = strfileName + "_" + str_Doc_Abbreviation + ".jpg";

                            //image compression by bhalla
                            CompressImage.compressImage(imagePath.getAbsolutePath());

                            mAllFile = mStorageUtils.saveFileToAppSpecificDir(mContext,
                                    StorageUtils.DIRECT_DIRECTORY,imageFileName, imagePath);

                            if (OCR_TYPE.equals("browse")) {
                                ib_yono_pending_doc_browse.setImageDrawable(getResources().getDrawable(R.drawable.checkedbrowse));
                                ib_yono_pending_doc_capture.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_camera));
                            } else if (OCR_TYPE.equals("capture")) {
                                ib_yono_pending_doc_browse.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_browsedoc));
                                ib_yono_pending_doc_capture.setImageDrawable(getResources().getDrawable(R.drawable.checkedcamera));
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

    private void browse_all_docs() {
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

                        str_extension = mObject[0].toString();
                        double kilobyte = (double) mObject[2];

                        if (!strMimeType.equals("application/octet-stream")
                                && !strMimeType.equals("application/vnd.android.package-archive")) {

                            if (strMimeType.equals("image/jpeg")
                                    || strMimeType.equals("image/gif")) {

                                mCommonMethods.showToast(mContext, ".jpg/.jpeg/.gif file format please select another option.");
                                mAllFile = null;
                            } else {

                                strfileName = spnr_yono_pending_proposals.getSelectedItem().toString();

                                new FileLoader(mContext, StorageUtils.DIRECT_DIRECTORY,
                                        strfileName + str_extension,
                                        new FileLoader.FileLoaderResponce() {
                                            @Override
                                            public void fileLoadFinished(File fileOutput) {
                                                if (fileOutput != null){
                                                    mAllFile = fileOutput;
                                                    ib_yono_pending_doc_browse.setImageDrawable(getResources().getDrawable(R.drawable.checkedbrowse));
                                                }else {
                                                    mAllFile = null;
                                                    ib_yono_pending_doc_browse.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_browsedoc));
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

        mAllBytes = mCommonMethods.read(mAllFile);

        if(mAllBytes != null){
            //upload document
            createSoapRequestToUploadDoc();
        }else{
            mCommonMethods.showMessageDialog(mContext, "File Null Error..");
        }
    }

    private void createSoapRequestToUploadDoc() {
        SoapObject request = new SoapObject(NAMESPACE,
                METHOD_NAME_UPLOAD_DOC);

        request.addProperty("f", mAllBytes);

        /*request.addProperty("fileName", strfileName+"_R"+increment_val+".tif");*/
        //request.addProperty("fileName", strfileName + "_R" + increment_val + str_extension);
        request.addProperty("fileName", mAllFile.getName());
        request.addProperty("qNo", "");
        request.addProperty("agentCode", mCommonMethods.GetUserID(mContext));
        request.addProperty("strEmailId", mCommonMethods.GetUserEmail(mContext));
        request.addProperty("strMobileNo", mCommonMethods.GetUserMobile(mContext));
        request.addProperty("strAuthKey", mCommonMethods.getStrAuth());

        mAsyncUploadFileCommon = new AsyncUploadFile_Common(mContext, this, request, METHOD_NAME_UPLOAD_DOC);
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
            str_extension = "";

            strfileName = "";

            ib_yono_pending_doc_browse.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_browsedoc));
            ib_yono_pending_doc_capture.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_camera));

            Toast.makeText(mContext, "Document Upload Successfully...", Toast.LENGTH_SHORT).show();
        } else {
            mCommonMethods.showToast(mContext, "PLease try agian later..");
        }
    }

    public class AsynchGetYoNoPendingKYC extends AsyncTask<String, String, String> {

        private volatile boolean running = true;

        @Override
        protected void onPreExecute() {
            mProgressDialog = new ProgressDialog(mContext, ProgressDialog.THEME_HOLO_LIGHT);
            mProgressDialog.setMessage(Html.fromHtml("<font color='#00a1e3'><b>Please wait...<b></font>"));
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            if (mCommonMethods.isNetworkConnected(mContext)) {

                try {
                    running = true;

                    SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME_GET_YoNo_Pending_KYC);
                    request.addProperty("strCode", mCommonMethods.GetUserCode(mContext));
                    request.addProperty("strEmailId", mCommonMethods.GetUserEmail(mContext));
                    request.addProperty("strMobileNo", mCommonMethods.GetUserMobile(mContext));
                    request.addProperty("strAuthKey", mCommonMethods.getStrAuth());

                    mCommonMethods.TLSv12Enable();

                    SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                    envelope.dotNet = true;

                    envelope.setOutputSoapObject(request);

                    HttpTransportSE androidHttpTranport = new HttpTransportSE(ServiceURL.SERVICE_URL);

                    androidHttpTranport.call(NAMESPACE + METHOD_NAME_GET_YoNo_Pending_KYC, envelope);
                    //Object response = envelope.getResponse();

                    SoapPrimitive sa = (SoapPrimitive) envelope.getResponse();

                    if (sa.toString().equals("0")) {
                        return "0";
                    }else{
                        ParseXML mParse = new ParseXML();

                        List<String> lstParse = mParse.parseParentNode(mParse.parseXmlTag(sa.toString(), "Data"), "Table");

                        lstYoNoProposal = mParse.parseNodeElementYoNoPendingKYC(lstParse);

                        return "";
                    }

                } catch (Exception e) {
                    try {
                        throw (e);
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                    running = false;
                }

            } else {
                mCommonMethods.showMessageDialog(mContext, mCommonMethods.NO_INTERNET_MESSAGE);
            }

            return null;
        }

        @Override
        protected void onPostExecute(String strResult) {
            if (mProgressDialog.isShowing())
                mProgressDialog.dismiss();

            if (running) {

                if (strResult.equals("0")){
                    txt_yono_pending_error.setText("No data found..");
                    txt_yono_pending_error.setVisibility(View.VISIBLE);
                    spnr_yono_pending_proposals.setVisibility(View.GONE);
                    flow_yono_upload.setVisibility(View.GONE);
                }else{
                    if (lstYoNoProposal.size() > 0){

                        List<String> spnrList = new ArrayList<>();
                        for(ParseXML.XMLHolderYoNoPendingKYC val : lstYoNoProposal){
                            spnrList.add(val.getStrProposalNo());
                        }
                        spnrList.add(0, "Select Proposal Number");

                        ArrayAdapter<String> adapter_yono_pending = new ArrayAdapter<String>(mContext, R.layout.spinner_aob, spnrList);
                        spnr_yono_pending_proposals.setAdapter(adapter_yono_pending);
                        adapter_yono_pending.notifyDataSetChanged();

                        txt_yono_pending_error.setVisibility(View.GONE);
                        spnr_yono_pending_proposals.setVisibility(View.VISIBLE);
                        flow_yono_upload.setVisibility(View.VISIBLE);

                    }else{
                        txt_yono_pending_error.setText("No data found..");
                        txt_yono_pending_error.setVisibility(View.VISIBLE);
                        spnr_yono_pending_proposals.setVisibility(View.GONE);
                        flow_yono_upload.setVisibility(View.GONE);
                    }
                }

            } else {
                mCommonMethods.showMessageDialog(mContext, mCommonMethods.WEEK_INTERNET_MESSAGE);
            }
        }
    }
}