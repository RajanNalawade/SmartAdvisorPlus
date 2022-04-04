package sbilife.com.pointofsale_bancaagency.cifenrollment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import org.ksoap2.serialization.SoapObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import sbilife.com.pointofsale_bancaagency.DatabaseHelper;
import sbilife.com.pointofsale_bancaagency.R;
import sbilife.com.pointofsale_bancaagency.cifenrollment.phase1.AsyncUploadFile_CIF;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;
import sbilife.com.pointofsale_bancaagency.common.CompressImage;
import sbilife.com.pointofsale_bancaagency.common.FileLoader;
import sbilife.com.pointofsale_bancaagency.common.StorageUtils;

public class ScoreCardEducationalActivity extends AppCompatActivity implements View.OnClickListener, AsyncUploadFile_CIF.Interface_Upload_CIF_Files {

    private final int REQUEST_CODE_PICK_FILE = 2, REQUEST_CODE_PICK_PHOTO_FILE = 3;

    private final String NAMESPACE = "http://tempuri.org/";
    private final String METHOD_NAME_UPLOAD_ALL_DOC = "UploadFile_CIFEnroll_URN";

    private Context mContext;
    private CommonMethods mCommonMethods;
    private StorageUtils mStorageUtils;
    private String str_urn_no = "", str_extension = "", str_doc = "";
    private Spinner spnr_tcc_education_doc_ssc, spnr_tcc_education_doc_hsc, spnr_tcc_education_doc_degree;
    private ImageButton imgbtn_tcc_education_doc_ssc_camera,
            imgbtn_tcc_education_doc_ssc_browse, imgbtn_tcc_education_doc_ssc_upload;
    private ImageButton imgbtn_tcc_education_doc_hsc_camera,
            imgbtn_tcc_education_doc_hsc_browse, imgbtn_tcc_education_doc_hsc_upload, ib_score_card_hsc_doc_view;
    private ImageButton imgbtn_tcc_education_doc_degree_camera,
            imgbtn_tcc_education_doc_degree_browse, imgbtn_tcc_education_doc_degree_upload, ib_score_card_degree_doc_view;
    private List<String> lstDocs = new ArrayList<>();
    private File mSSCFile, mHSCFile, mDegreeFile;

    private byte[] mAllBytes;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_score_card_educational);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.cifenrollment_pf_activity_title);

        str_urn_no = getIntent().getStringExtra("URN");

        initialisation();
    }

    private void initialisation() {

        mContext = this;
        mCommonMethods = new CommonMethods();
        mStorageUtils = new StorageUtils();
        DatabaseHelper db = new DatabaseHelper(mContext);

        mCommonMethods.setApplicationToolbarMenu1(this,"CIF on Boarding");

        lstDocs.add("Select Document");
        lstDocs.add("Mark Sheet");
        lstDocs.add("Passing Certificate");

        spnr_tcc_education_doc_ssc = findViewById(R.id.spnr_tcc_education_doc_ssc);
        mCommonMethods.fillSpinnerValue(mContext, spnr_tcc_education_doc_ssc, lstDocs);

        spnr_tcc_education_doc_hsc = findViewById(R.id.spnr_tcc_education_doc_hsc);
        mCommonMethods.fillSpinnerValue(mContext, spnr_tcc_education_doc_hsc, lstDocs);

        spnr_tcc_education_doc_degree = findViewById(R.id.spnr_tcc_education_doc_degree);
        mCommonMethods.fillSpinnerValue(mContext, spnr_tcc_education_doc_degree, lstDocs);

        imgbtn_tcc_education_doc_ssc_camera = findViewById(R.id.imgbtn_tcc_education_doc_ssc_camera);
        imgbtn_tcc_education_doc_ssc_browse = findViewById(R.id.imgbtn_tcc_education_doc_ssc_browse);
        imgbtn_tcc_education_doc_ssc_upload = findViewById(R.id.imgbtn_tcc_education_doc_ssc_upload);

        ib_score_card_hsc_doc_view = findViewById(R.id.ib_score_card_hsc_doc_view);
        imgbtn_tcc_education_doc_hsc_camera = findViewById(R.id.imgbtn_tcc_education_doc_hsc_camera);
        imgbtn_tcc_education_doc_hsc_browse = findViewById(R.id.imgbtn_tcc_education_doc_hsc_browse);
        imgbtn_tcc_education_doc_hsc_upload = findViewById(R.id.imgbtn_tcc_education_doc_hsc_upload);

        ib_score_card_degree_doc_view = findViewById(R.id.ib_score_card_degree_doc_view);
        imgbtn_tcc_education_doc_degree_camera = findViewById(R.id.imgbtn_tcc_education_doc_degree_camera);
        imgbtn_tcc_education_doc_degree_browse = findViewById(R.id.imgbtn_tcc_education_doc_degree_browse);
        imgbtn_tcc_education_doc_degree_upload = findViewById(R.id.imgbtn_tcc_education_doc_degree_upload);

        Button btn_tcc_education_next = findViewById(R.id.btn_tcc_education_next);

        imgbtn_tcc_education_doc_ssc_camera.setEnabled(true);
        imgbtn_tcc_education_doc_ssc_browse.setEnabled(true);
        imgbtn_tcc_education_doc_ssc_upload.setEnabled(true);

        imgbtn_tcc_education_doc_hsc_camera.setEnabled(true);
        imgbtn_tcc_education_doc_hsc_browse.setEnabled(true);
        imgbtn_tcc_education_doc_hsc_upload.setEnabled(true);

        imgbtn_tcc_education_doc_degree_camera.setEnabled(true);
        imgbtn_tcc_education_doc_degree_browse.setEnabled(true);
        imgbtn_tcc_education_doc_degree_upload.setEnabled(true);

        imgbtn_tcc_education_doc_ssc_camera.setOnClickListener(this);
        imgbtn_tcc_education_doc_ssc_browse.setOnClickListener(this);
        imgbtn_tcc_education_doc_ssc_upload.setOnClickListener(this);

        ib_score_card_hsc_doc_view.setOnClickListener(this);
        imgbtn_tcc_education_doc_hsc_camera.setOnClickListener(this);
        imgbtn_tcc_education_doc_hsc_browse.setOnClickListener(this);
        imgbtn_tcc_education_doc_hsc_upload.setOnClickListener(this);

        ib_score_card_degree_doc_view.setOnClickListener(this);
        imgbtn_tcc_education_doc_degree_camera.setOnClickListener(this);
        imgbtn_tcc_education_doc_degree_browse.setOnClickListener(this);
        imgbtn_tcc_education_doc_degree_upload.setOnClickListener(this);

        btn_tcc_education_next.setOnClickListener(this);
    }

    private void onEducationNext() {
        if (mHSCFile != null || mDegreeFile != null) {

            /*Fragment_TCC_Quationaries quariesFragment = new Fragment_TCC_Quationaries();
            Bundle args = new Bundle();
            args.putString("URN", str_urn_no);
            quariesFragment.setArguments(args);

            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.frag_tcc_exam_details_container, quariesFragment);
            transaction.commit();*/

            Intent mIntent = new Intent(ScoreCardEducationalActivity.this, ScoreCardQuationariesActivity.class);
            mIntent.putExtra("URN", str_urn_no);
            startActivity(mIntent);
        } else {

            mHSCFile = null;
            mDegreeFile = null;

            mCommonMethods.showMessageDialog(mContext, "Please Uplaod Minimum HSC Document");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_tcc_education_next:

                onEducationNext();

                break;

            case R.id.imgbtn_tcc_education_doc_ssc_camera:

                if (spnr_tcc_education_doc_ssc.getSelectedItem().toString().equalsIgnoreCase("Select Document")) {
                    mCommonMethods.showToast(mContext, "Please Select Document");
                } else {
                    clear_varibles();
                    str_doc = "SSC";
                    capture_docs(str_doc);
                }
                break;

            case R.id.imgbtn_tcc_education_doc_ssc_browse:
                if (spnr_tcc_education_doc_ssc.getSelectedItem().toString().equalsIgnoreCase("Select Document")) {
                    mCommonMethods.showToast(mContext, "Please Select Document");
                } else {
                    clear_varibles();
                    str_doc = "SSC";
                    browse_docs();
                }
                break;

            case R.id.imgbtn_tcc_education_doc_ssc_upload:
                if (spnr_tcc_education_doc_ssc.getSelectedItem().toString().equalsIgnoreCase("Select Document")) {
                    mCommonMethods.showToast(mContext, "Please Select Document");
                } else {
                    upload_docs();
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
                if (spnr_tcc_education_doc_hsc.getSelectedItem().toString().equalsIgnoreCase("Select Document")) {
                    mCommonMethods.showToast(mContext, "Please Select Document");
                } else {
                    clear_varibles();
                    str_doc = "HSC";
                    capture_docs(str_doc);
                }
                break;

            case R.id.imgbtn_tcc_education_doc_hsc_browse:
                if (spnr_tcc_education_doc_hsc.getSelectedItem().toString().equalsIgnoreCase("Select Document")) {
                    mCommonMethods.showToast(mContext, "Please Select Document");
                } else {
                    clear_varibles();
                    str_doc = "HSC";
                    browse_docs();
                }
                break;

            case R.id.imgbtn_tcc_education_doc_hsc_upload:
                if (spnr_tcc_education_doc_hsc.getSelectedItem().toString().equalsIgnoreCase("Select Document")) {
                    mCommonMethods.showToast(mContext, "Please Select Document");
                } else {
                    upload_docs();
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
                    clear_varibles();
                    str_doc = "Degree";
                    capture_docs(str_doc);
                }
                break;

            case R.id.imgbtn_tcc_education_doc_degree_browse:
                if (spnr_tcc_education_doc_degree.getSelectedItem().toString().equalsIgnoreCase("Select Document")) {
                    mCommonMethods.showToast(mContext, "Please Select Document");
                } else {
                    clear_varibles();
                    str_doc = "Degree";
                    browse_docs();
                }
                break;

            case R.id.imgbtn_tcc_education_doc_degree_upload:
                if (spnr_tcc_education_doc_degree.getSelectedItem().toString().equalsIgnoreCase("Select Document")) {
                    mCommonMethods.showToast(mContext, "Please Select Document");
                } else {
                    upload_docs();
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

                        str_extension = mObject[0].toString();
                        double kilobyte = (double) mObject[2];

                        if (!strMimeType.equals("application/octet-stream")
                                && !strMimeType.equals("application/vnd.android.package-archive")) {

                            String imageFileName = str_urn_no + "_" + str_doc + str_extension;

                            new FileLoader(mContext, StorageUtils.DIRECT_DIRECTORY,
                                    imageFileName,
                                    new FileLoader.FileLoaderResponce() {
                                        @Override
                                        public void fileLoadFinished(File fileOutput) {
                                            if (fileOutput != null){
                                                if (str_extension.equals(".jpg") || str_extension.equals(".jpeg")) {
                                                    //compress image
                                                    CompressImage.compressImage(fileOutput.getPath());
                                                }
                                                long size = fileOutput.length();
                                                double kilobyte = size / 1024;

                                                if (str_doc.equals("SSC")) {
                                                    mSSCFile = fileOutput;
                                                    //2 MB valiadation
                                                    if (kilobyte < mCommonMethods.FILE_UPLOAD_RESTRICT_SIZE) {
                                                        imgbtn_tcc_education_doc_ssc_browse
                                                                .setImageDrawable(getResources().getDrawable(R.drawable.checkedbrowse));
                                                        imgbtn_tcc_education_doc_ssc_camera.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_camera));
                                                    } else {
                                                        mSSCFile = null;
                                                        mCommonMethods.showMessageDialog(mContext, mCommonMethods.FILE_UPLOAD_RESTRICT_SIZE_MSG);
                                                    }
                                                } else if (str_doc.equals("HSC")) {
                                                    mHSCFile = fileOutput;
                                                    //2 MB valiadation
                                                    if (kilobyte < mCommonMethods.FILE_UPLOAD_RESTRICT_SIZE) {
                                                        imgbtn_tcc_education_doc_hsc_browse
                                                                .setImageDrawable(getResources().getDrawable(R.drawable.checkedbrowse));
                                                        imgbtn_tcc_education_doc_hsc_camera.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_camera));
                                                    } else {
                                                        mHSCFile = null;
                                                        mCommonMethods.showMessageDialog(mContext, mCommonMethods.FILE_UPLOAD_RESTRICT_SIZE_MSG);
                                                    }
                                                } else if (str_doc.equals("Degree")) {
                                                    mDegreeFile = fileOutput;
                                                    //2 MB valiadation
                                                    if (kilobyte < mCommonMethods.FILE_UPLOAD_RESTRICT_SIZE) {
                                                        imgbtn_tcc_education_doc_degree_browse
                                                                .setImageDrawable(getResources().getDrawable(R.drawable.checkedbrowse));
                                                        imgbtn_tcc_education_doc_degree_camera.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_camera));
                                                    } else {
                                                        mSSCFile = null;
                                                        mCommonMethods.showMessageDialog(mContext, mCommonMethods.FILE_UPLOAD_RESTRICT_SIZE_MSG);
                                                    }
                                                }
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

        if (requestCode == REQUEST_CODE_PICK_PHOTO_FILE) {
            if (resultCode == RESULT_OK) {

                if (str_doc.equals("SSC")) {

                    CompressImage.compressImage(mSSCFile.getPath());
                    //mSSCFile = mCommonMethods.compressImageCIF2(mSSCFile, str_urn_no, "_" + str_doc + ".jpg");

                    long size = mSSCFile.length();
                    double kilobyte = size / 1024;

                    //2 MB valiadation
                    if (kilobyte < mCommonMethods.FILE_UPLOAD_RESTRICT_SIZE) {
                        //str_extension = mSSCFile.getPath().substring(mSSCFile.getPath().lastIndexOf("."));

                        imgbtn_tcc_education_doc_ssc_camera.setImageDrawable(getResources().getDrawable(R.drawable.checkedcamera));
                        imgbtn_tcc_education_doc_ssc_browse.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_browsedoc));

                    } else {
                        mSSCFile = null;
                        mCommonMethods.showMessageDialog(mContext, mCommonMethods.FILE_UPLOAD_RESTRICT_SIZE_MSG);
                    }

                } else if (str_doc.equals("HSC")) {

                    CompressImage.compressImage(mHSCFile.getPath());

                    //mHSCFile = mCommonMethods.compressImageCIF2(mHSCFile, str_urn_no, "_" + str_doc + ".jpg");

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

                } else if (str_doc.equals("Degree")) {

                    CompressImage.compressImage(mDegreeFile.getPath());

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

    private void capture_docs(String str_doc) {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        String imageFileName = str_urn_no + "_" + str_doc + ".jpg";

        if (str_doc.equals("SSC")) {
            mSSCFile = mStorageUtils.createFileToAppSpecificDirCIF(mContext, imageFileName);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mCommonMethods.getContentUri(
                        mContext, mSSCFile));
            }else{
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mSSCFile));
            }
        } else if (str_doc.equals("HSC")) {

            mHSCFile = mStorageUtils.createFileToAppSpecificDirCIF(mContext, imageFileName);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mCommonMethods.getContentUri(
                        mContext, mHSCFile));
            }else{
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mHSCFile));
            }
        } else if (str_doc.equals("Degree")) {

            mDegreeFile = mStorageUtils.createFileToAppSpecificDirCIF(mContext, imageFileName);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mCommonMethods.getContentUri(
                        mContext, mDegreeFile));
            }else{
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mDegreeFile));
            }
        }
        startActivityForResult(takePictureIntent, REQUEST_CODE_PICK_PHOTO_FILE);
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

    private void upload_docs() {

        FileInputStream fin = null;

        try {

            if (mSSCFile != null) {
                fin = new FileInputStream(mSSCFile);
            } else if (mHSCFile != null) {
                fin = new FileInputStream(mHSCFile);
            } else if (mDegreeFile != null) {
                fin = new FileInputStream(mDegreeFile);
            }

            if (fin != null) {
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

    private void clear_varibles() {
        mSSCFile = null;
        mHSCFile = null;
        mDegreeFile = null;
        mAllBytes = null;
        str_extension = "";
        str_doc = "";
    }

    public void delete_file(final File dltFile) {

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext, AlertDialog.THEME_HOLO_LIGHT);
        builder.setTitle("Confirmation");
        builder.setMessage("do you want to delete this document ?");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (dltFile.exists()) {
                    if (dltFile.delete()) {
                        if (str_doc.equals("SSC")) {
                            imgbtn_tcc_education_doc_ssc_camera.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_camera));
                            imgbtn_tcc_education_doc_ssc_browse.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_browsedoc));
                        } else if (str_doc.equals("HSC")) {
                            imgbtn_tcc_education_doc_hsc_camera.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_camera));
                            imgbtn_tcc_education_doc_hsc_browse.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_browsedoc));
                        } else if (str_doc.equals("Degree")) {
                            imgbtn_tcc_education_doc_degree_camera.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_camera));
                            imgbtn_tcc_education_doc_degree_browse.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_browsedoc));
                        }
                        clear_varibles();
                        mCommonMethods.showToast(mContext, "file deleted successfully!!");
                    } else {
                        mCommonMethods.showToast(mContext, "error while deleting file");
                    }
                } else {
                    mCommonMethods.showToast(mContext, "file does not exists");
                }
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void createSoapRequestToUploadDoc(){
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME_UPLOAD_ALL_DOC);

        request.addProperty("f", mAllBytes);
        if (str_doc.equals("SSC")) {
            request.addProperty("fileName", mSSCFile.getName());
        } else if (str_doc.equals("HSC")) {
            request.addProperty("fileName", mHSCFile.getName());
        } else if (str_doc.equals("Degree")) {
            request.addProperty("fileName", mDegreeFile.getName());
        }
        request.addProperty("URN", str_urn_no);
        //for UAT
        //request.addProperty("Authkey", mCommonMethods.getStr_cif_auth_key());

        new AsyncUploadFile_CIF(mContext, this, request, METHOD_NAME_UPLOAD_ALL_DOC).execute();
    }

    @Override
    public void onUploadComplete(Boolean result) {
        if (result) {
            if (str_doc.equals("SSC")) {

                        /*imgbtn_tcc_education_doc_ssc_preview.setEnabled(false);
                        imgbtn_tcc_education_doc_ssc_dlt.setEnabled(false);*/
                imgbtn_tcc_education_doc_ssc_camera.setEnabled(false);
                imgbtn_tcc_education_doc_ssc_browse.setEnabled(false);
                imgbtn_tcc_education_doc_ssc_upload.setEnabled(false);
                imgbtn_tcc_education_doc_ssc_upload.setImageDrawable(getResources().getDrawable(R.drawable.checkedupload));

            } else if (str_doc.equals("HSC")) {

                        /*imgbtn_tcc_education_doc_hsc_preview.setEnabled(false);
                        imgbtn_tcc_education_doc_hsc_dlt.setEnabled(false);*/
                imgbtn_tcc_education_doc_hsc_camera.setEnabled(false);
                imgbtn_tcc_education_doc_hsc_browse.setEnabled(false);
                imgbtn_tcc_education_doc_hsc_upload.setEnabled(false);
                imgbtn_tcc_education_doc_hsc_upload.setImageDrawable(getResources().getDrawable(R.drawable.checkedupload));
            } else if (str_doc.equals("Degree")) {

                        /*imgbtn_tcc_education_doc_degree_preview.setEnabled(false);
                        imgbtn_tcc_education_doc_degree_dlt.setEnabled(false);*/
                imgbtn_tcc_education_doc_degree_camera.setEnabled(false);
                imgbtn_tcc_education_doc_degree_browse.setEnabled(false);
                imgbtn_tcc_education_doc_degree_upload.setEnabled(false);
                imgbtn_tcc_education_doc_degree_upload.setImageDrawable(getResources().getDrawable(R.drawable.checkedupload));
            }

            //clear_varibles();

            Toast.makeText(mContext, "Document Upload Successfully...",
                    Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(mContext, "PLease try agian later..",
                    Toast.LENGTH_SHORT).show();
        }
    }
}
