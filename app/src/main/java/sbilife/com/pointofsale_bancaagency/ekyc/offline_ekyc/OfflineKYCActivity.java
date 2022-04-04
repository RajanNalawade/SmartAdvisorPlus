package sbilife.com.pointofsale_bancaagency.ekyc.offline_ekyc;

import android.app.Dialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.webkit.DownloadListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import net.lingala.zip4j.core.ZipFile;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;

import sbilife.com.pointofsale_bancaagency.R;
import sbilife.com.pointofsale_bancaagency.ServiceURL;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;
import sbilife.com.pointofsale_bancaagency.common.StorageUtils;
import sbilife.com.pointofsale_bancaagency.utility.Tls12SocketFactory;


public class OfflineKYCActivity extends AppCompatActivity {


    private static final String SOAP_ACTION_VALIDATE_XML = "http://tempuri.org/validatexml";
    private static final String METHOD_NAME_VALIDATE_XML = "validatexml";
    String URL = ServiceURL.SERVICE_URL;
    private static final String NAMESPACE = "http://tempuri.org/";


    RadioButton rb_scanOfflineXML, rb_scanQRCode;
    LinearLayout ll_ScanOfflineXML;
    Button btn_UploadXML, btn_DownLoadXML;
    WebView webview_UIDAI_Site;
    CheckBox checkBox;
    final AppCompatActivity activityForButton = this;
    private final int REQUEST_CODE_PICK_FILE = 2;
    EditText edt_path;
    String QuatationNumber = "";
    String proposer_name = "";
    String planName = "";
    String PathHolder = "";
    ProgressDialog mProgressDialog;
    String str_Validate_XML_Status = "";
    String signedXml = "";
    File XMLFilePath;

    private File file;
    private CommonMethods mCommonMethods;
    private StorageUtils mStorageUtils;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline_kyc);

        mContext = this;
        mCommonMethods = new CommonMethods();
        mStorageUtils = new StorageUtils();


        Intent i = getIntent();
        QuatationNumber = i.getStringExtra("QuatationNumber");
        proposer_name = i.getStringExtra("proposer_name");
        // proposer_AadharNumber = i.getStringExtra("proposer_AadharNumber");
        planName = i.getStringExtra("planName");

        rb_scanOfflineXML = (RadioButton) findViewById(R.id.rb_scanOfflineXML);
        rb_scanQRCode = (RadioButton) findViewById(R.id.rb_scanQRCode);
        ll_ScanOfflineXML = (LinearLayout) findViewById(R.id.ll_ScanOfflineXML);
        btn_UploadXML = (Button) findViewById(R.id.btn_UploadXML);
        btn_DownLoadXML = (Button) findViewById(R.id.btn_DownLoadXML);
        webview_UIDAI_Site = (WebView) findViewById(R.id.webview_UIDAI_Site);
        webview_UIDAI_Site.getSettings().setJavaScriptEnabled(true);
        webview_UIDAI_Site.getSettings().setSaveFormData(true);
        webview_UIDAI_Site.getSettings().setAllowContentAccess(true);
        webview_UIDAI_Site.getSettings().setAllowFileAccess(true);
        webview_UIDAI_Site.getSettings().setAllowFileAccessFromFileURLs(true);
        webview_UIDAI_Site.getSettings().setAllowUniversalAccessFromFileURLs(true);
        webview_UIDAI_Site.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webview_UIDAI_Site.loadUrl("https://resident.uidai.gov.in/offline-kyc");
        webview_UIDAI_Site.setDownloadListener(new DownloadListener() {

            @Override
            public void onDownloadStart(String url, String userAgent,
                                        String contentDisposition, String mimetype,
                                        long contentLength) {
                DownloadManager.Request request = new DownloadManager.Request(
                        Uri.parse(url));

                request.allowScanningByMediaScanner();
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED); //Notify client once download is completed!
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "Download");
                DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                dm.enqueue(request);
                Toast.makeText(getApplicationContext(), "Downloading File", //To notify the Client that the file is being downloaded
                        Toast.LENGTH_LONG).show();

            }
        });
       /* webview_UIDAI_Site.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDescription,
                                        String mimetype, long contentLength) {
                *//*
                    DownloadManager.Request
                        This class contains all the information necessary to request a new download.
                        The URI is the only required parameter. Note that the default download
                        destination is a shared volume where the system might delete your file
                        if it needs to reclaim space for system use. If this is a problem,
                        use a location on external storage (see setDestinationUri(Uri).
                *//*
                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));

                *//*
                    void allowScanningByMediaScanner ()
                        If the file to be downloaded is to be scanned by MediaScanner, this method
                        should be called before enqueue(Request) is called.
                *//*
                request.allowScanningByMediaScanner();

                *//*
                    DownloadManager.Request setNotificationVisibility (int visibility)
                        Control whether a system notification is posted by the download manager
                        while this download is running or when it is completed. If enabled, the
                        download manager posts notifications about downloads through the system
                        NotificationManager. By default, a notification is shown only
                        when the download is in progress.

                        It can take the following values: VISIBILITY_HIDDEN, VISIBILITY_VISIBLE,
                        VISIBILITY_VISIBLE_NOTIFY_COMPLETED.

                        If set to VISIBILITY_HIDDEN, this requires the permission
                        android.permission.DOWNLOAD_WITHOUT_NOTIFICATION.

                    Parameters
                        visibility int : the visibility setting value
                    Returns
                        DownloadManager.Request this object
                *//*
                request.setNotificationVisibility(
                        DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

                *//*
                    DownloadManager
                        The download manager is a system service that handles long-running HTTP
                        downloads. Clients may request that a URI be downloaded to a particular
                        destination file. The download manager will conduct the download in the
                        background, taking care of HTTP interactions and retrying downloads
                        after failures or across connectivity changes and system reboots.
                *//*

                *//*
                    String guessFileName (String url, String contentDisposition, String mimeType)
                        Guesses canonical filename that a download would have, using the URL
                        and contentDisposition. File extension, if not defined,
                        is added based on the mimetype

                    Parameters
                        url String : Url to the content
                        contentDisposition String : Content-Disposition HTTP header or null
                        mimeType String : Mime-type of the content or null

                    Returns
                        String : suggested filename
                *//*
                String fileName = URLUtil.guessFileName(url, contentDescription, mimetype);

                *//*
                    DownloadManager.Request setDestinationInExternalPublicDir
                    (String dirType, String subPath)

                        Set the local destination for the downloaded file to a path within
                        the public external storage directory (as returned by
                        getExternalStoragePublicDirectory(String)).

                        The downloaded file is not scanned by MediaScanner. But it can be made
                        scannable by calling allowScanningByMediaScanner().

                    Parameters
                        dirType String : the directory type to pass to
                                         getExternalStoragePublicDirectory(String)
                        subPath String : the path within the external directory, including
                                         the destination filename

                    Returns
                        DownloadManager.Request this object

                    Throws
                        IllegalStateException : If the external storage directory cannot be
                                                found or created.

                *//*
                String extStorageDirectory = Environment
                        .getExternalStorageDirectory().toString();
                File f = new File(extStorageDirectory + direct + "/");

                request.setDestinationInExternalPublicDir(f.getAbsolutePath(), fileName);

                DownloadManager dManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);

                *//*
                    long enqueue (DownloadManager.Request request)
                        Enqueue a new download. The download will start automatically once the
                        download manager is ready to execute it and connectivity is available.

                    Parameters
                        request DownloadManager.Request : the parameters specifying this download

                    Returns
                        long : an ID for the download, unique across the system. This ID is used
                               to make future calls related to this download.
                *//*
                dManager.enqueue(request);
            }
        });*/
        checkBox = (CheckBox) findViewById(R.id.checkBox);
        TextView tv_declaration_text = (TextView) findViewById(R.id.tv_declaration_text);

        /*String str_declaration = "I, "
                + "<font color=\"blue\">"
                + proposer_name
                + "</font>"
                + ", holder of Aadhar Number "
                + "<font color=\"blue\">"
                + "</font>"
                + " ,  hereby give my consent to SBI Life Insurance Co. Ltd. (SBI Life) to obtain my Aadhaar number issued by UIDAI and PAN issued by the Income Tax department and provide voluntary consent to link my Aadhaar and PAN with all my SBI Life policies. I give my consent to obtain and use my Aadhaar Number/Virtual ID, Name, Date of Birth, Fingerprint/Iris and my Aadhaar details to authenticate me with UIDAI as per the Aadhaar (Targeted Delivery of Financial and Other Subsidies, Benefits and Services) Act, 2016 and all other applicable laws. SBI Life has informed me that my Aadhaar details/Virtual ID and identity information would only be used for authentication either through Yes/No authentication facility or e-KYC facility in accordance with applicable regulations. SBI Life has also informed me that my Aadhaar details/Virtual ID and identity information will be used only for KYC purpose and for all service aspects related to SBI Life and my biometrics will not be stored /shared by SBI Life. I will not hold SBI Life or any of its authorised officials responsible in case of any incorrect information provided by me. I further authorize SBI Life to use my mobile number for sending SMS alerts to me regarding this purpose."
                + "\n"
                + "<font color=\"blue\">"
                + "Note:- Please provide your consent by ticking the checkbox above to proceed further"
                + "" + "</font>";*/

        String str_declaration = "I, "
                + "<font color=\"blue\">"
                + proposer_name
                + "</font>"
                + ", hereby give my voluntary consent to SBI Life Insurance Company Limited (SBI Life) and authorise the Company to obtain necessary details like Name, DOB, Address, Mobile Number, Email, Photograph through the QR code available on my Aadhaar card / XML File shared using the offline verification process of UIDAI. "
                + "\n"
                + "I understand and agree that this information will be exclusively used by SBI Life only for the KYC purpose and for all service aspects related to my policy/ies. \n" +
                "I have duly been made aware that I can also use alternative KYC documents like Passport, Voter's ID Card, Driving licence, NREGA job card, letter from National Population Register, in lieu of Aadhaar for the purpose of completing my KYC formalities. I understand and agree that the details so obtained shall be stored with SBI Life and be shared solely for the purpose of issuing insurance policy to me and for servicing them. I will not hold SBI Life or any of its authorized officials responsible in case of any incorrect information provided by me. I further authorize SBI Life that it may use my mobile number for sending SMS alerts to me regarding various servicing and other matters related to my policy/ies."
                + "\n"
                + "<font color=\"blue\">"
                + "Note:- Please provide your consent by ticking the checkbox above to proceed further."
                + "" + "</font>";

        tv_declaration_text.setText(Html.fromHtml(str_declaration),
                TextView.BufferType.SPANNABLE);

        btn_UploadXML.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View arg0) {
                if (checkBox.isChecked()) {
                    webview_UIDAI_Site.setVisibility(View.GONE);
                    Dialog_UploadXML();
                } else {
                    checkBox.requestFocus();
                    Toast.makeText(OfflineKYCActivity.this,
                            "Please accept the consent first",
                            Toast.LENGTH_LONG).show();

                }

            }
        });

        btn_DownLoadXML.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View arg0) {


                if (checkBox.isChecked()) {
                   // webview_UIDAI_Site.setVisibility(View.VISIBLE);
                   // webview_UIDAI_Site.loadUrl("https://resident.uidai.gov.in/offline-kyc");
                    Intent myWebLink = new Intent(Intent.ACTION_VIEW);
                    myWebLink.setData(Uri
                            .parse("https://resident.uidai.gov.in/offline-kyc"));
                    startActivity(myWebLink);

                } else {
                    checkBox.requestFocus();
                    Toast.makeText(OfflineKYCActivity.this,
                            "Please accept the consent first",
                            Toast.LENGTH_LONG).show();

                }

            }
        });
        rb_scanOfflineXML.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                // TODO Auto-generated method stub
                if (isChecked) {
                    ll_ScanOfflineXML.setVisibility(View.VISIBLE);

                }
            }
        });
        rb_scanQRCode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                // TODO Auto-generated method stub
                if (isChecked) {
                    ll_ScanOfflineXML.setVisibility(View.GONE);

                }
            }
        });
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

                            File originalFile = null;
                            try {
                                originalFile = new File(FileUtils.getRealPath(mContext, mSelectedUri));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            PathHolder = getRealPathFromURI(mContext, mSelectedUri);
                            //    PathHolder = data.getData().getPath();
                            String[] filePathColumn = {MediaStore.MediaColumns.DATA};

                            Cursor cursor = getContentResolver().query(mSelectedUri,
                                    filePathColumn, null, null, null);
                            cursor.moveToFirst();

                            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                            String strPathOfFile = cursor.getString(columnIndex);
                            cursor.close();

                            XMLFilePath = new File(originalFile.getAbsolutePath());
                            PathHolder = XMLFilePath.getAbsolutePath();
                            edt_path.setText(XMLFilePath.getAbsolutePath());
                            mCommonMethods.showToast(mContext, PathHolder);
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

    public static String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;//ww  w.j  a  va2  s. c om
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj,
                    null, null, null);
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public boolean JavaMethod(String zipFile_path, String ShareCode) {
        boolean verificationResult = false;
        String extStorageDirectory = Environment
                .getExternalStorageDirectory().toString();

        File unzipLocation = new File(extStorageDirectory + new CommonMethods().DIRECT_DIRECTORY);

        if (!unzipLocation.exists()) {
            unzipLocation.mkdirs();
        }

        File filezipFile = new File(zipFile_path);
        String strFileName = filezipFile.getName();
        String unzipLocation_path = unzipLocation + "/" + strFileName.replaceAll(".zip", "") + ".xml";
        try {
            File src = new File(zipFile_path);
            ZipFile zipFile = new ZipFile(src);
            if (zipFile.isEncrypted()) {
                zipFile.setPassword(ShareCode);
            }
            String dest = new String(unzipLocation.getAbsolutePath());
            zipFile.extractAll(dest);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(OfflineKYCActivity.this, e.toString(), Toast.LENGTH_LONG).show();
        }

        // Get the text file
        file = new File(unzipLocation_path);

        // Read text from file
        StringBuilder text = new StringBuilder();

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
            br.close();

            String str_output = text + "";

            signedXml = str_output;
            if (!signedXml.equals("")) {
                ValidateXML();
            }

/*            AssetManager assManager = getApplicationContext().getAssets();
            InputStream is = null;
            try {
                is = assManager.open("uidai_offline_publickey_19062019.cer");
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            InputStream caInput = new BufferedInputStream(is);

            // String certficate_path = Environment.getExternalStorageDirectory() + "/uidai_offline_publickey_19062019.cer";
         *//*   InputStream is = new FileInputStream(
                    certficate_path);*//*
            SignatureVerifier SignatureVerifier1 = new SignatureVerifier(caInput);
             verificationResult = SignatureVerifier1.verify(signedXml);*/


        } catch (Exception e) {
            // You'll need to add proper error handling here
            Toast.makeText(OfflineKYCActivity.this, e.toString(), Toast.LENGTH_LONG).show();
        }
        return verificationResult;

    }

    public void Dialog_UploadXML() {
        final Dialog d = new Dialog(this);
        d.setCancelable(false);
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);
        d.getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.BLACK));
        d.setContentView(R.layout.layout_dialog_upload_offline_xml);
        edt_path = (EditText) d
                .findViewById(R.id.edt_path);
        final EditText edt_Share_code = (EditText) d
                .findViewById(R.id.edt_Share_code);


        Button btn_browse_xml = (Button) d
                .findViewById(R.id.btn_browse_xml);
        Button btn_Validate = (Button) d
                .findViewById(R.id.btn_Validate);


        Button btn_submit = (Button) d.findViewById(R.id.btn_submit);
        Button btn_cancel = (Button) d.findViewById(R.id.btn_cancel);

        btn_browse_xml.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                //Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                //intent.setType("*/*");
                // intent.setType("application/zip");
                // intent.setType("application/x-wav");
                //startActivityForResult(intent, REQUEST_CODE_PICK_FILE);

                Intent mIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                mIntent.addCategory(Intent.CATEGORY_OPENABLE);
                mIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION
                        | Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
                //mIntent.setType("application/pdf");
                mIntent.setType("*/*");
                /*String[] mimeType = new String[]{"application/x-binary,application/octet-stream"};
                if(mimeType.length > 0) {
                mIntent.putExtra(Intent.EXTRA_MIME_TYPES, mimeType);
                }*/
                mBrowseDocLauncher.launch(mIntent);
            }
        });
        btn_Validate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                String ShareCode = edt_Share_code.getText().toString();
                if (PathHolder == null || PathHolder.equals("")) {
                    Toast.makeText(OfflineKYCActivity.this, "Please Browse the XML File", Toast.LENGTH_LONG).show();
                } else if (ShareCode.equalsIgnoreCase("") && ShareCode.length() != 4) {
                    Toast.makeText(OfflineKYCActivity.this, "Please enter valid Pass Code", Toast.LENGTH_LONG).show();
                } else {
                    JavaMethod(PathHolder, ShareCode);
                }
            }
        });


        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                d.dismiss();

            }
        });

        d.show();
    }

    public void ValidateXML() {
        AsyncValidateXML asyncValidateXML = new AsyncValidateXML();
        mProgressDialog = new ProgressDialog(this);
        // String Message =
        //
        // "This is a one time registration process, please wait till gets complete.Please Do not Touch Anywhere";
        String Message = "Please wait ,Loading...";

        mProgressDialog.setMessage(Message);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(false);

        mProgressDialog.setButton("Cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        mProgressDialog.dismiss();
                    }
                });

        mProgressDialog.setMax(100);
        mProgressDialog.show();
        asyncValidateXML.execute();

    }

    public class AsyncValidateXML extends AsyncTask<String, Void, String> {
        private volatile boolean running = true;

        @Override
        protected String doInBackground(String... param) {
            try {
                running = true;
                SoapObject request = new SoapObject(NAMESPACE,
                        METHOD_NAME_VALIDATE_XML);
                request.addProperty("XMLFilePath", signedXml);

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);
                envelope.dotNet = true;

                envelope.setOutputSoapObject(request);

                allowAllSSL();

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);

                HttpTransportSE androidHttpTranport = new HttpTransportSE(
                        ServiceURL.SERVICE_URL);
                androidHttpTranport.call(SOAP_ACTION_VALIDATE_XML,
                        envelope);
                Object response = envelope.getResponse();

                SoapPrimitive sa = null;
                sa = (SoapPrimitive) envelope.getResponse();

                str_Validate_XML_Status = sa.toString();


            } catch (Exception e) {

                mProgressDialog.dismiss();
                running = false;
            }
            return null;

        }

        @Override
        protected void onPostExecute(String result) {
            if (mProgressDialog.isShowing())
                mProgressDialog.dismiss();
            if (running != false) {
                if (str_Validate_XML_Status.equalsIgnoreCase("True")) {
                    Toast.makeText(OfflineKYCActivity.this,
                            "Signature  Validated Successfully",
                            Toast.LENGTH_LONG).show();
                    try {

                        StringBuilder str_kycResXML = new StringBuilder();
                        str_kycResXML.append(signedXml);

                        str_kycResXML
                                .append("<ConsentAccepted>" + "Y" + "</ConsentAccepted>");
                        str_kycResXML
                                .append("<BIOMETRICMODE>"
                                        + "XML"
                                        + "</BIOMETRICMODE>");

                        //delete extracted file
                        if (file != null)
                            file.delete();

                        Intent intent = new Intent();
                        intent.putExtra("KYC_XML", str_kycResXML.toString());
                        // setResult(10, intent);
                        setResult(RESULT_OK, intent);
                        finish();
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                        Toast.makeText(OfflineKYCActivity.this, "Error in KYC XML", Toast.LENGTH_LONG).show();
                    }

                } else if (str_Validate_XML_Status.equalsIgnoreCase("False")) {
                    Toast.makeText(OfflineKYCActivity.this,
                            "Signature not Validated",
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(OfflineKYCActivity.this,
                            "Error during Signing the XML",
                            Toast.LENGTH_LONG).show();
                }
            } else {
                syncerror();
            }
        }
    }

    public void syncerror() {

        Toast.makeText(OfflineKYCActivity.this, "Server not Responding",
                Toast.LENGTH_SHORT).show();

    }

    public void allowAllSSL() {
        try {
            SSLContext sslcontext = SSLContext.getInstance("TLSv1.2");

            sslcontext.init(null, null, null);
            Tls12SocketFactory tls12SocketFactory = new Tls12SocketFactory(
                    sslcontext.getSocketFactory());
            // SSLSocketFactory NoSSLv3Factory = new
            // NoSSLv3SocketFactory(sslcontext.getSocketFactory());

            HttpsURLConnection.setDefaultSSLSocketFactory(tls12SocketFactory);

        } catch (Exception e) {
            Log.e("allowAllSSL", e.toString());
        }
    }
}
