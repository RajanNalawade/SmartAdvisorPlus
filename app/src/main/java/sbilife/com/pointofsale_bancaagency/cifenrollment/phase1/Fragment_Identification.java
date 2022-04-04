package sbilife.com.pointofsale_bancaagency.cifenrollment.phase1;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

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
import org.ksoap2.serialization.SoapObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import sbilife.com.pointofsale_bancaagency.DatabaseHelper;
import sbilife.com.pointofsale_bancaagency.ParseXML;
import sbilife.com.pointofsale_bancaagency.R;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;
import sbilife.com.pointofsale_bancaagency.common.CompressImage;
import sbilife.com.pointofsale_bancaagency.common.FileLoader;
import sbilife.com.pointofsale_bancaagency.common.StorageUtils;

import static android.app.Activity.RESULT_OK;

public class Fragment_Identification extends Fragment implements OnClickListener, AsyncUploadFile_CIF.Interface_Upload_CIF_Files {

    public static String photoByteArrayAsString = "", proposer_sign = "", createdDate = "";
    public static boolean is_annexure_uploaded, is_id_card_uploaded, is_pan_card_uploaded;
    private static File f;
    private final int REQUEST_CODE_PICK_PHOTO_FILE = 1;
    private final int REQUEST_OCR = 4;
    private final String CHECK_TYPE_ID_CARD_CAPTURE = "SBI_ID_CARD";
    private final String CHECK_TYPE_PAN_CARD_CAPTURE = "PAN_CARD";
    private final String CHECK_TYPE_ANNEXURE_CAPTURE = "Annexure";
    private final String CHECK_TYPE_PHOTO = "Photo";
    private final String CHECK_TYPE_HSC = "HSC";
    private final String CHECK_TYPE_DEGREE = "Degree";

    /*private final int SIGNATURE_ACTIVITY = 2;*/
    private final String CHECK_TYPE_SIGN = "Sign";
    private final String NAMESPACE = "http://tempuri.org/";
    private final String METHOD_NAME_UPLOAD_ALL_DOC = "UploadFile_CIFEnroll";
    private final String QUESTIONS_DOC = "questions_";
    private final String SCHEDULE3_DOC = "_SCHEDULE3";
    private ImageButton img_photo, imgbtn_annexure_capture, imgbtn_annexure_browse, imgbtn_annexure_upload,
            imgbtn_sign_capture, imgbtn_sign_browse, img_sign, imgbtn_id_card_capture, imgbtn_id_card_browse,
            imgbtn_id_card_upload, imgbtn_pan_card_capture, imgbtn_pan_card_browse, imgbtn_pan_card_upload;
    private CommonMethods mCommonMethods;
    private StorageUtils mStorageUtils;
    private File fileAnnexure, fileSign, fileIDCard, filePanCard;
    private Bitmap scaledBitmap = null;
    private Dialog d;
    private TextView txt_download_annexure;
    private DatabaseHelper db;
    private StringBuilder inputVal;
    private String str_pf_numbers = "", str_dob = "", str_sex = "", str_pan_no = "", str_branch_name = "", str_current_district = "", str_permanent_district = "",
            str_address1 = "", str_address2 = "", str_pincode = "", str_state_id = "", str_area = "",
            str_aadhar_card_no = "", str_candidate_corporate_name = "", str_father_name = "",
            str_category = "", str_mobile_no = "", str_phone_number_2 = "", str_email_id = "",
            str_contact_person_eamil_id = "", str_basic_qualification = "", str_board_name_for_basic_qualification = "",
            str_roll_number_for_basic_qualification = "", str_year_of_passing_for_basic_qualification = "",
            str_other_qualification = "", str_educational_qualification = "", str_state = "", str_city = "",
            str_exam_center_location = "", str_exam_language = "", str_insurance_category = "", Check = "",
            latestImage = "", quotation_no = "", str_extension = "", OCR_TYPE = "", strBDMEmail = "",
            strBDMMobile = "", strSaleSupportEmail = "", strSaleSupportMobile = "";

    private byte[] mAllBytes;
    //private AsyncUploadAnnexure mAsyncUploadAnnexure;
    private LinearLayout ll_identification_main, ll_educational_details, ll_annexure1_fit_proper,
            ll_schedule3;
    //education
    private Spinner spnr_tcc_education_doc_hsc, spnr_tcc_education_doc_degree;
    private ImageButton imgbtn_tcc_education_doc_hsc_camera,
            imgbtn_tcc_education_doc_hsc_browse, imgbtn_tcc_education_doc_hsc_upload, ib_score_card_hsc_doc_view;
    private ImageButton imgbtn_tcc_education_doc_degree_camera,
            imgbtn_tcc_education_doc_degree_browse, imgbtn_tcc_education_doc_degree_upload, ib_score_card_degree_doc_view;
    private List<String> lstDocs = new ArrayList<>();
    private File mHSCFile, mDegreeFile;
    private Context mContext;
    private Button btn_tcc_education_next;
    //Annexure 1 fit and proper
    private Button btn_tcc_queries_submit;
    private CheckBox chk_tcc_que_a, chk_tcc_que_b, chk_tcc_que_c, chk_tcc_que_d,
            chk_tcc_que_e, chk_tcc_que_f, chk_tcc_que_g, chk_tcc_que_h, chk_tcc_que_i,
            chk_tcc_que_j, chk_tcc_que_k, chk_tcc_que_l, chk_tcc_que_m, chk_tcc_que_n, chk_tcc_que_o;
    private EditText edt_tcc_query_place;
    private TextView txt_tcc_query_date;
    //Declaration
    private CheckBox chk_tcc_declaration;
    private Button btn_tcc_declaration_submit;
    private File mSchedule3File;
    private TextView txt_tcc_declaration_place;

    private AsyncUploadFile_CIF mAsyncUploadFileCifEnroll;

    private EditText edt_bdm_email, edt_bdm_mobile_no, edt_sales_support_email_id, edt_sales_support_mobile;
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
                                && !strMimeType.equals("application/vnd.android.package-archive")) {

                            String strFileName = str_pf_numbers + "_" + Check + str_extension;

                            new FileLoader(mContext, StorageUtils.DIRECT_DIRECTORY_CIF,
                                    strFileName,
                                    new FileLoader.FileLoaderResponce() {
                                        @Override
                                        public void fileLoadFinished(File fileOutput) {
                                            if (fileOutput != null) {

                                                boolean b1 = str_extension.equals(".jpg") || str_extension.equals(".jpeg") || str_extension.equals(".png");
                                                if (Check.equals(CHECK_TYPE_SIGN)) {
                                                    fileSign = fileOutput;

                                                    if (fileSign != null) {
                                                        if (b1) {

                                                            //Bitmap bmp = mCommonMethods.compressImage(fileSign, 1);
                                                            Object[] mObj = mCommonMethods.compressImageCIF_Photo_Sign(mContext, fileSign, 1);
                                                            Bitmap bmp = (Bitmap) mObj[0];
                                                            fileSign = (File) mObj[1];

                                                            double kilobyte = fileSign.length() / 1024;
                                                            if (kilobyte <= mCommonMethods.FILE_UPLOAD_CIF_PHOTO_SIGN_RESTRICT_SIZE) {
                                                                Bitmap b = null;
                                                                //nought changes
                                                                Uri imageUri;
                                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                                                    imageUri = mCommonMethods.getContentUri(getActivity(), new File(fileSign.toString()));
                                                                } else {
                                                                    imageUri = Uri.fromFile(new File(fileSign.toString()));
                                                                }

                                                                try {
                                                                    b = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
                                                                    Bitmap mFaceBitmap = b.copy(Bitmap.Config.RGB_565, true);

                                                                    if (mFaceBitmap != null) {

                                                                        imgbtn_sign_capture.setVisibility(View.GONE);
                                                                        imgbtn_sign_browse.setVisibility(View.GONE);
                                                                        img_sign.setVisibility(View.VISIBLE);

                                                                        Bitmap scaled = Bitmap.createScaledBitmap(bmp, 230, 200, true);
                                                                        img_sign.setImageBitmap(scaled);

                                                                        if (scaled != null) {
                                                                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                                                            scaled.compress(Bitmap.CompressFormat.JPEG, 50, stream);

                                                                            byte[] photoByteArray = stream.toByteArray();

                                                                            proposer_sign = Base64.encodeToString(photoByteArray, Base64.DEFAULT);

                                                                        } else {
                                                                            mCommonMethods.showToast(getActivity(), "file error");
                                                                            proposer_sign = "";
                                                                        }

                                                                    } else {
                                                                        mCommonMethods.showToast(getActivity(), "file error");
                                                                        proposer_sign = "";
                                                                    }

                                                                } catch (Exception e) {
                                                                    // TODO Auto-generated catch block
                                                                    e.printStackTrace();
                                                                }
                                                            } else {
                                                                fileSign = null;
                                                                mCommonMethods.showMessageDialog(mContext, mCommonMethods.FILE_UPLOAD_CIF_PHOTO_SIGN_RESTRICT_SIZE_MSG);
                                                            }
                                                        } else {
                                                            mCommonMethods.showToast(getActivity(), "please select JPG OR JPEG file");
                                                            proposer_sign = "";
                                                        }
                                                    }
                                                } else {
                                                    if (b1) {
                                                        CompressImage.compressImage(fileOutput.getPath());
                                                    }

                                                    if (Check.equals(CHECK_TYPE_ANNEXURE_CAPTURE)) {
                                                        fileAnnexure = fileOutput;
                                                        imgbtn_annexure_browse.setImageDrawable(getResources().getDrawable(
                                                                R.drawable.checkedbrowse));
                                                        imgbtn_annexure_capture.setImageDrawable(getResources().getDrawable(
                                                                R.drawable.ibtn_camera));
                                                        is_annexure_uploaded = false;
                                                    } else if (Check.equals(CHECK_TYPE_ID_CARD_CAPTURE)) {

                                                        fileIDCard = fileOutput;
                                                        imgbtn_id_card_browse.setImageDrawable(getResources().getDrawable(
                                                                R.drawable.checkedbrowse));
                                                        imgbtn_id_card_capture.setImageDrawable(getResources().getDrawable(
                                                                R.drawable.ibtn_camera));
                                                        is_id_card_uploaded = false;
                                                    } else if (Check.equals(CHECK_TYPE_PAN_CARD_CAPTURE)) {

                                                        filePanCard = fileOutput;
                                                        imgbtn_pan_card_browse.setImageDrawable(getResources().getDrawable(
                                                                R.drawable.checkedbrowse));
                                                        imgbtn_pan_card_capture.setImageDrawable(getResources().getDrawable(
                                                                R.drawable.ibtn_camera));
                                                        is_pan_card_uploaded = false;
                                                    } else if (Check.equals(CHECK_TYPE_HSC)) {
                                                        mHSCFile = fileOutput;
                                                        long size = mHSCFile.length();
                                                        double kilobyte = size / 1024;

                                                        //2 MB valiadation
                                                        if (kilobyte < mCommonMethods.FILE_UPLOAD_RESTRICT_SIZE) {
                                                            imgbtn_tcc_education_doc_hsc_browse
                                                                    .setImageDrawable(getResources().getDrawable(R.drawable.checkedbrowse));
                                                            imgbtn_tcc_education_doc_hsc_camera.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_camera));
                                                        } else {
                                                            mHSCFile = null;
                                                            mCommonMethods.showMessageDialog(mContext, mCommonMethods.FILE_UPLOAD_RESTRICT_SIZE_MSG);
                                                        }
                                                    } else if (Check.equals(CHECK_TYPE_DEGREE)) {
                                                        mDegreeFile = fileOutput;
                                                        long size = mDegreeFile.length();
                                                        double kilobyte = size / 1024;

                                                        //2 MB valiadation
                                                        if (kilobyte < mCommonMethods.FILE_UPLOAD_RESTRICT_SIZE) {
                                                            //get extension of final file created
                                                            //str_extension = mDegreeFile.getPath().substring(mDegreeFile.getPath().lastIndexOf("."));

                                                            imgbtn_tcc_education_doc_degree_browse
                                                                    .setImageDrawable(getResources().getDrawable(R.drawable.checkedbrowse));
                                                            imgbtn_tcc_education_doc_degree_camera.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_camera));
                                                        } else {
                                                            mDegreeFile = null;
                                                            mCommonMethods.showMessageDialog(mContext, mCommonMethods.FILE_UPLOAD_RESTRICT_SIZE_MSG);
                                                        }
                                                    }
                                                }
                                            } else {
                                                mCommonMethods.showMessageDialog(mContext, "File Not Found..");
                                            }

                                        }
                                    }).execute(mSelectedUri);

                        } else {
                            mCommonMethods.showToast(mContext, ".exe/.apk file format not acceptable");
                        }

                    } else {
                        mCommonMethods.showToast(mContext, "File Not Found!");
                    }
                } else {
                    mCommonMethods.showToast(mContext, "File browsing cancelled..");
                }
            } catch (Exception e) {
                e.printStackTrace();
                mCommonMethods.showMessageDialog(mContext, "Please Select Files from Your External Storage");
            }
        }
    });

    @Override
    public View onCreateView(LayoutInflater inflater,
                             final ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(
                R.layout.cifenrollment_layout_identification_details, container, false);

        mCommonMethods = new CommonMethods();
        mStorageUtils = new StorageUtils();
        db = new DatabaseHelper(getActivity());
        mContext = getContext();

        img_photo = rootView.findViewById(R.id.img_photo);

        is_annexure_uploaded = false;
        is_id_card_uploaded = false;
        is_pan_card_uploaded = false;

        txt_download_annexure = rootView.findViewById(R.id.txt_download_annexure);
        txt_download_annexure.setText(Html.fromHtml("<u>Download Annexure</u>"));

        ImageButton imgbtn_annexure_view = rootView.findViewById(R.id.imgbtn_annexure_view);
        imgbtn_annexure_capture = rootView.findViewById(R.id.imgbtn_annexure_capture);
        imgbtn_annexure_browse = rootView.findViewById(R.id.imgbtn_annexure_browse);
        imgbtn_annexure_upload = rootView.findViewById(R.id.imgbtn_annexure_upload);

        ImageButton imgbtn_id_card_view = rootView.findViewById(R.id.imgbtn_id_card_view);
        imgbtn_id_card_capture = rootView.findViewById(R.id.imgbtn_id_card_capture);
        imgbtn_id_card_browse = rootView.findViewById(R.id.imgbtn_id_card_browse);
        imgbtn_id_card_upload = rootView.findViewById(R.id.imgbtn_id_card_upload);

        ImageButton imgbtn_pan_card_view = rootView.findViewById(R.id.imgbtn_pan_card_view);
        imgbtn_pan_card_capture = rootView.findViewById(R.id.imgbtn_pan_card_capture);
        imgbtn_pan_card_browse = rootView.findViewById(R.id.imgbtn_pan_card_browse);
        imgbtn_pan_card_upload = rootView.findViewById(R.id.imgbtn_pan_card_upload);

        imgbtn_sign_capture = rootView.findViewById(R.id.imgbtn_sign_capture);
        imgbtn_sign_browse = rootView.findViewById(R.id.imgbtn_sign_browse);
        img_sign = rootView.findViewById(R.id.img_sign);

        ll_identification_main = rootView.findViewById(R.id.ll_identification_main);
        ll_educational_details = rootView.findViewById(R.id.ll_educational_details);
        ll_annexure1_fit_proper = rootView.findViewById(R.id.ll_annexure1_fit_proper);
        ll_schedule3 = rootView.findViewById(R.id.ll_schedule3);

        //initialise educational part
        initialiseEducationalPart(rootView);

        //initialise Annexure 1 (fit and proper) part
        initialiseAnnexure1Part(rootView);

        // initialise Schedule 3 part
        initialiseSchedule3Part(rootView);

        Button btn_submit = rootView.findViewById(R.id.btn_next_idn);
        // btn_back = (Button) rootView.findViewById(R.id.btn_back);
        TableRow tr_button = rootView
                .findViewById(R.id.tr_identification_button);
        tr_button.setVisibility(View.VISIBLE);

        String dashboard = "";
        dashboard = CIFEnrollmentMainActivity.dashboard;
        if (dashboard != null && dashboard.equalsIgnoreCase("true")) {
            quotation_no = CIFEnrollmentMainActivity.quotation_dashboard;
            String isFlag1 = CIFEnrollmentMainActivity.isFlag1;
            if (isFlag1.equalsIgnoreCase("true")) {
                tr_button.setVisibility(View.GONE);
            } else {
                tr_button.setVisibility(View.VISIBLE);
            }
            // setCIFInputGui();
        } else {
            quotation_no = CIFEnrollmentPFActivity.quotation_Number;
        }
        setCIFInputGui();

        createdDate = getDate1(getCurrentDate());

        //mAsyncUploadAnnexure = new AsyncUploadAnnexure();

        imgbtn_annexure_view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fileAnnexure != null) {
                    try {
                        mCommonMethods.openAllDocs(mContext, fileAnnexure);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                } else {
                    mCommonMethods.showToast(mContext, "Please Browse or Capture Document First!");
                }
            }
        });

        imgbtn_id_card_view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fileIDCard != null) {
                    try {
                        mCommonMethods.openAllDocs(mContext, fileIDCard);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                } else {
                    mCommonMethods.showToast(mContext, "Please Browse or Capture Document First!");
                }
            }
        });

        imgbtn_pan_card_view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (filePanCard != null) {
                    try {
                        mCommonMethods.openAllDocs(mContext, filePanCard);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                } else {
                    mCommonMethods.showToast(mContext, "Please Browse or Capture Document First!");
                }
            }
        });

        txt_download_annexure.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    mCommonMethods.openWebLink(getActivity(), "https://drive.google.com/open?id=1eLYdTqVjLxo2gHr8ufOHhXZtf3srxZRz");

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        imgbtn_annexure_upload.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (fileAnnexure != null) {
                    uploadDoc(fileAnnexure);
                } else
                    mCommonMethods.showToast(mContext, "Plese Select Proper File");
            }
        });

        imgbtn_id_card_upload.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (fileIDCard != null)
                    uploadDoc(fileIDCard);
                else
                    mCommonMethods.showToast(mContext, "Plese Select Proper File");
            }
        });

        imgbtn_pan_card_upload.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (filePanCard != null)
                    uploadDoc(filePanCard);
                else
                    mCommonMethods.showToast(mContext, "Plese Select Proper File");
            }
        });

        imgbtn_annexure_browse.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                fileAnnexure = null;
                // select doc alert

                Check = CHECK_TYPE_ANNEXURE_CAPTURE;

                browseDocsORImage();
            }
        });

        imgbtn_id_card_browse.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                fileIDCard = null;

                Check = CHECK_TYPE_ID_CARD_CAPTURE;
                OCR_TYPE = "browse";
                Intent intent = new Intent(getActivity(), OcrActivity.class);
                startActivityForResult(intent, REQUEST_OCR);

            }
        });

        imgbtn_pan_card_browse.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                filePanCard = null;
                Check = CHECK_TYPE_PAN_CARD_CAPTURE;

                browseDocsORImage();
            }
        });


        imgbtn_annexure_capture.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                fileAnnexure = null;
                Check = CHECK_TYPE_ANNEXURE_CAPTURE;
                dispatchTakePictureIntent(CHECK_TYPE_ANNEXURE_CAPTURE);
            }
        });

        imgbtn_id_card_capture.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                fileIDCard = null;
                Check = CHECK_TYPE_ID_CARD_CAPTURE;
                OCR_TYPE = "capture";
                Intent intent = new Intent(getActivity(), OcrActivity.class);
                startActivityForResult(intent, REQUEST_OCR);
            }
        });

        imgbtn_pan_card_capture.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                filePanCard = null;
                Check = CHECK_TYPE_PAN_CARD_CAPTURE;
                dispatchTakePictureIntent(CHECK_TYPE_PAN_CARD_CAPTURE);
            }
        });

        imgbtn_sign_browse.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Check = CHECK_TYPE_SIGN;

                browseDocsORImage();
            }
        });

        imgbtn_sign_capture.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Check = CHECK_TYPE_SIGN;
                dispatchTakePictureIntent(CHECK_TYPE_SIGN);
            }
        });

        img_sign.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                imgbtn_sign_browse.setVisibility(View.VISIBLE);
                imgbtn_sign_capture.setVisibility(View.VISIBLE);
                img_sign.setVisibility(View.GONE);

                proposer_sign = "";
            }
        });

        img_photo.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Check = CHECK_TYPE_PHOTO;
                dispatchTakePictureIntent(CHECK_TYPE_PHOTO);
            }
        });

		/*img_signature.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {

				latestImage = "cif";
				windowmessagesgin();

			}
		});*/

        btn_submit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                if (validation()) {
                    getValueFromDatabase_New();
                    getInput();

                    try {

                        M_MainActivity_Data data = new M_MainActivity_Data(
                                quotation_no, str_pf_numbers,
                                new String(inputVal));

                        long count = 0;
                        count = db.insertCIFDetail_New(data, quotation_no);
                        if (count > 0) {
                            Toast toast = Toast.makeText(getActivity(),
                                    "Data Inserted Successfully",
                                    Toast.LENGTH_SHORT);
                            toast.show();
                        }
                        M_UserInformation DashboardDetail_data = new M_UserInformation(
                                quotation_no,
                                str_pf_numbers,
                                Fragment_PersonalDetails.str_candidate_corporate_name,
                                Fragment_ContactDetails.str_mobile_no,
                                Fragment_ContactDetails.str_email_id,
                                createdDate);

                        DashboardDetail_data.setStr_aadhar_card_no(str_aadhar_card_no);
                        DashboardDetail_data.setStr_contact_person_email_id(str_contact_person_eamil_id);

                        long rowId2 = db
                                .insertDashBoardDetail(DashboardDetail_data);
                        if (rowId2 > 0) {
                            mCommonMethods.showToast(mContext, "Data Inserted Successfully");
                        }

                        //visible educational layout and gone other layouts
                        ll_identification_main.setVisibility(View.GONE);
                        ll_educational_details.setVisibility(View.VISIBLE);
                        ll_annexure1_fit_proper.setVisibility(View.GONE);
                        ll_schedule3.setVisibility(View.GONE);

                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        });

        return rootView;
    }

    private void dispatchTakePictureIntent(String strCheckType) {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        try {

            //nought changes
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

                if (strCheckType.equalsIgnoreCase(CHECK_TYPE_ANNEXURE_CAPTURE)) {
                    fileAnnexure = createImageFile(strCheckType);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mCommonMethods.getContentUri(getActivity(), fileAnnexure));
                } else if (strCheckType.equals(CHECK_TYPE_SIGN)) {
                    fileSign = createImageFile(strCheckType);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mCommonMethods.getContentUri(getActivity(), fileSign));
                } else if (strCheckType.equals(CHECK_TYPE_ID_CARD_CAPTURE)) {
                    fileIDCard = createImageFile(strCheckType);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mCommonMethods.getContentUri(getActivity(), fileIDCard));
                } else if (strCheckType.equals(CHECK_TYPE_PAN_CARD_CAPTURE)) {
                    filePanCard = createImageFile(strCheckType);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mCommonMethods.getContentUri(getActivity(), filePanCard));
                } else {
                    f = createImageFile(strCheckType);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mCommonMethods.getContentUri(getActivity(), f));
                }
            } else {
                if (strCheckType.equalsIgnoreCase(CHECK_TYPE_ANNEXURE_CAPTURE)) {
                    fileAnnexure = createImageFile(strCheckType);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(fileAnnexure));
                } else if (strCheckType.equals(CHECK_TYPE_SIGN)) {
                    fileSign = createImageFile(strCheckType);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(fileSign));
                } else if (strCheckType.equals(CHECK_TYPE_ID_CARD_CAPTURE)) {
                    fileIDCard = createImageFile(strCheckType);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(fileIDCard));
                } else if (strCheckType.equals(CHECK_TYPE_PAN_CARD_CAPTURE)) {
                    filePanCard = createImageFile(strCheckType);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(filePanCard));
                } else {
                    f = createImageFile(strCheckType);
                    //mCurrentPhotoPath = f.getPath();
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                }
            }
            startActivityForResult(takePictureIntent, REQUEST_CODE_PICK_PHOTO_FILE);
        } catch (SecurityException e) {
            mCommonMethods.showMessageDialog(getActivity(), "Problem with the Permission");
        } catch (IOException e) {
            mCommonMethods.showMessageDialog(getActivity(), "Something went wrong");
        } catch (Exception e) {
            e.printStackTrace();
            mCommonMethods.showMessageDialog(getActivity(), "Something went wrong");
        }
    }

    private File createImageFile(String strCheckType) throws IOException {

        String imageFileName = str_pf_numbers + strCheckType + ".jpg";
        /*if (strCheckType.equalsIgnoreCase(CHECK_TYPE_ANNEXURE_CAPTURE))
            imageFileName = str_pf_numbers + CHECK_TYPE_ANNEXURE_CAPTURE;
        else if (strCheckType.equalsIgnoreCase(CHECK_TYPE_SIGN)) {
            imageFileName = str_pf_numbers + CHECK_TYPE_SIGN;
        } else if (strCheckType.equalsIgnoreCase(CHECK_TYPE_ID_CARD_CAPTURE))
            imageFileName = str_pf_numbers + CHECK_TYPE_ID_CARD_CAPTURE;
        else if (strCheckType.equalsIgnoreCase(CHECK_TYPE_PAN_CARD_CAPTURE))
            imageFileName = str_pf_numbers + CHECK_TYPE_PAN_CARD_CAPTURE;
        else
            imageFileName = str_pf_numbers + CHECK_TYPE_PHOTO;*/

        return mStorageUtils.createFileToAppSpecificDirCIF(mContext, imageFileName);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_OCR) {
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
                        //Bitmap edgeBitmap = BitmapFactory.decodeFile(imagePath.getPath());

                    } catch (Exception e) {
                        e.printStackTrace();

                        imagePath = null;

                        /*imagePath = (File) bundle.get("BitmapImageUri");
                        Bitmap edgeBitmap = BitmapFactory.decodeFile(imagePath.getPath());*/
                    }
                }

                fileIDCard = null;
                if (imagePath != null) {

                    //fileIDCard = mCommonMethods.createCaptureImg(str_pf_numbers + CHECK_TYPE_ID_CARD_CAPTURE + ".jpg");

                    //image compression by bhalla
                    CompressImage.compressImage(imagePath.getPath());
                    //copy  FILE
                    //mCommonMethods.copyFile(new FileInputStream(imagePath), new FileOutputStream(fileIDCard));
                    fileIDCard = mStorageUtils.saveFileToAppSpecificDir(mContext, StorageUtils.DIRECT_DIRECTORY_CIF,
                            str_pf_numbers + CHECK_TYPE_ID_CARD_CAPTURE + ".jpg", imagePath);

                    str_extension = fileIDCard.getPath().substring(fileIDCard.getPath().lastIndexOf("."))
                            == null ? "" : fileIDCard.getPath().substring(fileIDCard.getPath().lastIndexOf("."));

                    if (OCR_TYPE.equals("browse")) {

                        imgbtn_id_card_browse.setImageDrawable(getResources().getDrawable(R.drawable.checkedbrowse));

                        imgbtn_id_card_capture.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_camera));

                    } else if (OCR_TYPE.equals("capture")) {

                        imgbtn_id_card_browse.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_browsedoc));
                        imgbtn_id_card_capture.setImageDrawable(getResources().getDrawable(R.drawable.checkedcamera));
                    }

                    is_id_card_uploaded = false;

                    if (imagePath.exists()) {
                        imagePath.delete();
                    }
                } else {
                    mCommonMethods.showToast(getActivity(), "Error while generating file..");
                }

            } else {
                mCommonMethods.showToast(getActivity(), "Data not receive");
            }
        } else if (requestCode == REQUEST_CODE_PICK_PHOTO_FILE) {
            if (resultCode == RESULT_OK) {

                if (Check.equals(CHECK_TYPE_SIGN)) {

                    //Bitmap bmp = compressImage(fileSign, 1);
                    Object[] mObj = mCommonMethods.compressImageCIF_Photo_Sign(mContext, fileSign, 1);
                    Bitmap bmp = (Bitmap) mObj[0];
                    fileSign = (File) mObj[1];

                    double kilobyte = fileSign.length() / 1024;
                    if (kilobyte < mCommonMethods.FILE_UPLOAD_CIF_PHOTO_SIGN_RESTRICT_SIZE) {
                        Bitmap b = null;
                        //nought changes
                        Uri imageUri;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            imageUri = mCommonMethods.getContentUri(getActivity(), new File(fileSign.toString()));
                        } else {
                            imageUri = Uri.fromFile(new File(fileSign.toString()));
                        }

                        try {
                            b = MediaStore.Images.Media.getBitmap(
                                    getActivity().getContentResolver(), imageUri);
                        } catch (FileNotFoundException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        Bitmap mFaceBitmap = b.copy(Bitmap.Config.RGB_565, true);

                        if (mFaceBitmap != null) {

                            Bitmap scaled = Bitmap.createScaledBitmap(bmp, 230,
                                    200, true);

                            imgbtn_sign_capture.setVisibility(View.GONE);
                            imgbtn_sign_browse.setVisibility(View.GONE);
                            img_sign.setVisibility(View.VISIBLE);

                            img_sign.setImageBitmap(scaled);

                            if (scaled != null) {
                                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                scaled.compress(Bitmap.CompressFormat.JPEG,
                                        50, stream);

                                byte[] photoByteArray = stream.toByteArray();

                                proposer_sign = Base64.encodeToString(photoByteArray, Base64.DEFAULT);

                            } else {
                                mCommonMethods.showToast(getActivity(), "file error");
                                proposer_sign = "";
                            }
                        } else {
                            mCommonMethods.showToast(getActivity(), "file error");
                            proposer_sign = "";
                        }
                    } else {
                        fileSign = null;
                        mCommonMethods.showMessageDialog(mContext, mCommonMethods.FILE_UPLOAD_CIF_PHOTO_SIGN_RESTRICT_SIZE_MSG);
                    }
                } else if (Check.equals(CHECK_TYPE_PHOTO)) {

                    //str_extension = f.getPath().substring(f.getPath().lastIndexOf("."));

                    //Bitmap bmp = compressImage(f, 1);
                    Object[] mObj = mCommonMethods.compressImageCIF_Photo_Sign(mContext, f, 1);
                    Bitmap bmp = (Bitmap) mObj[0];
                    f = (File) mObj[1];

                    double kilobyte = f.length() / 1024;
                    if (kilobyte < mCommonMethods.FILE_UPLOAD_CIF_PHOTO_SIGN_RESTRICT_SIZE) {
                        Bitmap b = null;
                        //nought changes
                        Uri imageUri;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            imageUri = mCommonMethods.getContentUri(getActivity(), new File(f.toString()));
                        } else {
                            imageUri = Uri.fromFile(new File(f.toString()));
                        }

                        try {
                            b = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);

                        } catch (FileNotFoundException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        Bitmap mFaceBitmap = b.copy(Bitmap.Config.RGB_565, true);

                        if (mFaceBitmap != null) {

                            // ImageButton button1 = (ImageButton)
                            // findViewById(R.id.img_photo);
                            Bitmap scaled = Bitmap.createScaledBitmap(bmp, 230,
                                    200, true);

                            img_photo.setImageBitmap(scaled);

                            if (scaled != null) {
                                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                scaled.compress(Bitmap.CompressFormat.JPEG, 80, stream);
                                byte[] photoByteArray = stream.toByteArray();
                                photoByteArrayAsString = Base64.encodeToString(
                                        photoByteArray, Base64.DEFAULT);
                            }
                        }
                    } else {
                        f = null;
                        mCommonMethods.showMessageDialog(mContext, mCommonMethods.FILE_UPLOAD_CIF_PHOTO_SIGN_RESTRICT_SIZE_MSG);
                    }
                } else if (Check.equalsIgnoreCase(CHECK_TYPE_ANNEXURE_CAPTURE)) {

                    str_extension = fileAnnexure.getPath().substring(fileAnnexure.getPath().lastIndexOf("."));

                    if (!str_extension.equalsIgnoreCase(".exe")) {
                        //image compression by bhalla
                        CompressImage.compressImage(fileAnnexure.getPath());

                        imgbtn_annexure_capture.setImageDrawable(getResources().getDrawable(R.drawable.checkedcamera));
                        imgbtn_annexure_browse.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_browsedoc));

                        is_annexure_uploaded = false;
                    } else {
                        //.exe not allowed
                        mCommonMethods.showMessageDialog(getActivity(), "Blank file error");
                        fileAnnexure = null;
                    }

                } else if (Check.equalsIgnoreCase(CHECK_TYPE_ID_CARD_CAPTURE)) {

                    str_extension = fileIDCard.getPath().substring(fileIDCard.getPath().lastIndexOf("."));

                    if (!str_extension.equalsIgnoreCase(".exe")) {
                        //image compression by bhalla
                        CompressImage.compressImage(fileIDCard.getAbsolutePath());

                        imgbtn_id_card_capture.setImageDrawable(getResources().getDrawable(R.drawable.checkedcamera));
                        imgbtn_id_card_browse.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_browsedoc));

                        is_id_card_uploaded = false;
                    } else {
                        //.exe not allowed
                        mCommonMethods.showMessageDialog(getActivity(), "Blank file error");
                        fileIDCard = null;
                    }

                } else if (Check.equalsIgnoreCase(CHECK_TYPE_PAN_CARD_CAPTURE)) {

                    str_extension = filePanCard.getAbsolutePath().substring(filePanCard.getAbsolutePath().lastIndexOf("."));

                    if (!str_extension.equalsIgnoreCase(".exe")) {
                        //image compression by bhalla
                        CompressImage.compressImage(filePanCard.getAbsolutePath());

                        imgbtn_pan_card_capture.setImageDrawable(getResources().getDrawable(R.drawable.checkedcamera));
                        imgbtn_pan_card_browse.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_browsedoc));

                        is_pan_card_uploaded = false;
                    } else {
                        //.exe not allowed
                        mCommonMethods.showMessageDialog(getActivity(), "Blank file error");
                        filePanCard = null;
                    }

                } else if (Check.equals(CHECK_TYPE_HSC)) {

                    CompressImage.compressImage(mHSCFile.getAbsolutePath());

                    //mHSCFile = mCommonMethods.compressImageCIF2(mHSCFile, str_urn_no, "_" + str_doc + ".jpg");

                    long size = mHSCFile.length();
                    double kilobyte = size / 1024;

                    //2 MB valiadation
                    if (kilobyte < mCommonMethods.FILE_UPLOAD_RESTRICT_SIZE) {
                        //str_extension = mHSCFile.getAbsolutePath().substring(mHSCFile.getAbsolutePath().lastIndexOf("."));

                        imgbtn_tcc_education_doc_hsc_camera.setImageDrawable(getResources().getDrawable(R.drawable.checkedcamera));
                        imgbtn_tcc_education_doc_hsc_browse.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_browsedoc));
                    } else {
                        mHSCFile = null;
                        mCommonMethods.showMessageDialog(mContext, mCommonMethods.FILE_UPLOAD_RESTRICT_SIZE_MSG);
                    }

                } else if (Check.equals(CHECK_TYPE_DEGREE)) {

                    CompressImage.compressImage(mDegreeFile.getAbsolutePath());

                    //mDegreeFile = mCommonMethods.compressImageCIF2(mDegreeFile, str_urn_no, "_" + str_doc + ".jpg");

                    long size = mDegreeFile.length();
                    double kilobyte = size / 1024;

                    //2 MB valiadation
                    if (kilobyte < mCommonMethods.FILE_UPLOAD_RESTRICT_SIZE) {
                        //str_extension = mDegreeFile.getAbsolutePath().substring(mDegreeFile.getAbsolutePath().lastIndexOf("."));

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

    private String getCurrentDate() {
        Calendar present_date = Calendar.getInstance();
        int mDay = present_date.get(Calendar.DAY_OF_MONTH);
        int mMonth = present_date.get(Calendar.MONTH) + 1;
        int mYear = present_date.get(Calendar.YEAR);

        return mDay + "-" + mMonth + "-" + mYear;

    }

    private String getDate1(String OldDate) {
        String NewDate = "";
        try {
            DateFormat userDateFormat = new SimpleDateFormat("dd-MM-yyyy");
            DateFormat dateFormatNeeded = new SimpleDateFormat("MM-dd-yyyy");
            Date date = userDateFormat.parse(OldDate);

            NewDate = dateFormatNeeded.format(date);

        } catch (Exception e) {
            // TODO: handle exception
            Log.e("Integrated Service", "Error in Getting Date");
        }

        return NewDate;
    }

    private void setCIFInputGui() {
        if (getValueFromDatabase()) {

            // Identification

            if (photoByteArrayAsString != null) {
                byte[] photoByteArray = Base64
                        .decode(photoByteArrayAsString, 0);
                Bitmap bitmap = BitmapFactory.decodeByteArray(photoByteArray,
                        0, photoByteArray.length);
                img_photo.setImageBitmap(bitmap);
                // photoBitmap = bitmap;
            } else {
                photoByteArrayAsString = "";
            }

            if (proposer_sign != null) {
                byte[] signByteArray = Base64.decode(proposer_sign, 0);
                Bitmap bitmap = BitmapFactory.decodeByteArray(signByteArray, 0,
                        signByteArray.length);
                img_sign.setImageBitmap(bitmap);

                imgbtn_sign_browse.setVisibility(View.GONE);
                imgbtn_sign_capture.setVisibility(View.GONE);
                img_sign.setVisibility(View.VISIBLE);

            } else {
                proposer_sign = "";
            }

            //Education Support
            edt_bdm_email.setText(strBDMEmail);
            edt_bdm_mobile_no.setText(strBDMMobile);
            edt_sales_support_email_id.setText(strSaleSupportEmail);
            edt_sales_support_mobile.setText(strSaleSupportMobile);
        }
    }

    private boolean getValueFromDatabase() {
        // retrieving data from database
        boolean flag = false;
        List<M_MainActivity_Data> data = db.getCIFDetail_New(quotation_no);
        if (data.size() > 0) {
            int i = 0;
            ParseXML prsObj = new ParseXML();

            String input = data.get(i).getInput();

            str_pf_numbers = prsObj.parseXmlTag(input, "pf_number");

            photoByteArrayAsString = prsObj.parseXmlTag(input,
                    "photoByteArrayAsString");
            proposer_sign = prsObj.parseXmlTag(input, "proposer_sign");

            String strAnnexure = prsObj.parseXmlTag(input, "CIF_IS_ANNEXURE_UPLOADED");
            strAnnexure = strAnnexure == null ? "" : strAnnexure;
            is_annexure_uploaded = strAnnexure.equals("true");

            String strIDCardUploaded = prsObj.parseXmlTag(input, "CIF_IS_ID_CARD_UPLOADED");
            strIDCardUploaded = strIDCardUploaded == null ? "" : strIDCardUploaded;
            is_id_card_uploaded = strIDCardUploaded.equals("true");

            String strPanCardUploaded = prsObj.parseXmlTag(input, "CIF_IS_PAN_CARD_UPLOADED");
            strPanCardUploaded = strPanCardUploaded == null ? "" : strPanCardUploaded;
            is_pan_card_uploaded = strPanCardUploaded.equals("true");

            //added by rajan 14-05-2021
            strBDMEmail = prsObj.parseXmlTag(input, "BDM_email");
            strBDMEmail = strBDMEmail == null ? "" : strBDMEmail;

            strBDMMobile = prsObj.parseXmlTag(input, "BDM_mobile");
            strBDMMobile = strBDMMobile == null ? "" : strBDMMobile;

            strSaleSupportEmail = prsObj.parseXmlTag(input, "Sales_Support_email");
            strSaleSupportEmail = strSaleSupportEmail == null ? "" : strSaleSupportEmail;

            strSaleSupportMobile = prsObj.parseXmlTag(input, "Sales_Support_mobile");
            strSaleSupportMobile = strSaleSupportMobile == null ? "" : strSaleSupportMobile;
            //end by rajan 14-05-2021

            str_branch_name = prsObj.parseXmlTag(input, "branch_name");
            str_pan_no = prsObj.parseXmlTag(input, "pan_no");
            str_sex = prsObj.parseXmlTag(input, "sex");
            str_dob = prsObj.parseXmlTag(input, "dob");
            str_area = prsObj.parseXmlTag(input, "area");

            str_address1 = prsObj.parseXmlTag(input, "current_house_number");
            str_address2 = prsObj.parseXmlTag(input, "current_street");
            str_pincode = prsObj.parseXmlTag(input, "current_pincode");
            str_current_district = prsObj
                    .parseXmlTag(input, "current_district");
            str_permanent_district = prsObj.parseXmlTag(input,
                    "permanent_district");

            //added by rajan 24-10-2017
            str_aadhar_card_no = prsObj.parseXmlTag(input, "AADHAR_NO");
            str_aadhar_card_no = str_aadhar_card_no == null ? "" : str_aadhar_card_no;

            str_contact_person_eamil_id = prsObj.parseXmlTag(input, "CIF_CONTACTPERSON_EMAIL_ID");

            flag = true;

        }
        return flag;
    }

    private boolean getValueFromDatabase_New() {
        // retrieving data from database
        boolean flag = false;
        List<M_MainActivity_Data> data = db.getCIFDetail_New(quotation_no);
        if (data.size() > 0) {
            int i = 0;
            ParseXML prsObj = new ParseXML();

            String input = data.get(i).getInput();
            str_candidate_corporate_name = prsObj.parseXmlTag(input,
                    "candidate_corporate_name");
            str_father_name = prsObj.parseXmlTag(input, "father_name");
            str_category = prsObj.parseXmlTag(input, "category");

            // Contact details

            str_mobile_no = prsObj.parseXmlTag(input, "mobile_no");
            String str_phone_number = prsObj.parseXmlTag(input, "phone_number");

            if (str_phone_number != null
                    && !str_phone_number.equalsIgnoreCase("")) {
                str_phone_number_2 = str_phone_number;
				/*String[] array = str_phone_number.split("-");
				str_phone_number_1 = array[0];
				str_phone_number_2 = array[1];*/
            }

            str_email_id = prsObj.parseXmlTag(input, "email_id");
            String str_re_enter_email_id = str_email_id;

            str_basic_qualification = prsObj.parseXmlTag(input,
                    "basic_qualification");
            str_board_name_for_basic_qualification = prsObj.parseXmlTag(input,
                    "board_name_for_basic_qualification");

            str_roll_number_for_basic_qualification = prsObj.parseXmlTag(input,
                    "roll_number_for_basic_qualification");
            str_year_of_passing_for_basic_qualification = prsObj.parseXmlTag(
                    input, "year_of_passing_for_basic_qualification");
            str_educational_qualification = prsObj.parseXmlTag(input,
                    "educational_qualification");
            str_other_qualification = prsObj.parseXmlTag(input,
                    "other_qualification");

            str_insurance_category = prsObj.parseXmlTag(input,
                    "insurance_category");
            str_state = prsObj.parseXmlTag(input, "state");
            str_city = prsObj.parseXmlTag(input, "city");
            str_exam_center_location = prsObj.parseXmlTag(input,
                    "exam_center_location");
            str_exam_language = prsObj.parseXmlTag(input, "exam_language");

            //added by rajan 24-10-2017
            str_aadhar_card_no = prsObj.parseXmlTag(input, "AADHAR_NO");
            str_aadhar_card_no = str_aadhar_card_no == null ? "" : str_aadhar_card_no;

            str_contact_person_eamil_id = prsObj
                    .parseXmlTag(input, "CIF_CONTACTPERSON_EMAIL_ID");

            flag = true;

        }
        return flag;
    }

    private boolean validation() {
        // if (LoginActivity.str_pf_number.trim().equals("")) {
        //
        // Toast.makeText(getActivity(), "Please Fill Banca-Cif Form First",
        // Toast.LENGTH_LONG).show();
        //
        // ((NewMainActivity) getActivity()).getViewpager().setCurrentItem(0);
        // return false;
        // } else
        if (Fragment_PersonalDetails.str_candidate_corporate_name.trim()
                .equals("")
                && Fragment_PersonalDetails.str_father_name.trim().equals("")
                && Fragment_PersonalDetails.str_category.trim().equals("")) {

            Toast.makeText(getActivity(),
                    "Please Fill Personal Details Form First",
                    Toast.LENGTH_LONG).show();

            ((CIFEnrollmentMainActivity) getActivity()).getViewpager().setCurrentItem(0);
            return false;
        } else if (Fragment_ContactDetails.str_mobile_no.trim().equals("")
                || Fragment_ContactDetails.str_mobile_no.length() < 10
                || Fragment_ContactDetails.str_phone_number_2.equals("")
                || Fragment_ContactDetails.str_phone_number_2.length() < 10
                || Fragment_ContactDetails.str_email_id.trim().equals("")
                || Fragment_ContactDetails.str_re_enter_email_id.equals("")) {

            Toast.makeText(getActivity(),
                    "Please Fill Contact Details Form First", Toast.LENGTH_LONG)
                    .show();

            ((CIFEnrollmentMainActivity) getActivity()).getViewpager()
                    .setCurrentItem(1);
            return false;
        } else if (Fragment_Qualification.str_basic_qualification.trim()
                .equals("")
                || Fragment_Qualification.str_board_name_for_basic_qualification
                .trim().equals("")
                || Fragment_Qualification.str_roll_number_for_basic_qualification
                .equals("")
                || Fragment_Qualification.str_year_of_passing_for_basic_qualification
                .trim().equals("")
                || Fragment_Qualification.str_educational_qualification
                .equals("")
                || Fragment_Qualification.str_other_qualification.equals("")) {

            Toast.makeText(getActivity(),
                    "Please Fill Qualification Details Form First",
                    Toast.LENGTH_LONG).show();

            ((CIFEnrollmentMainActivity) getActivity()).getViewpager().setCurrentItem(2);
            return false;
        } else if (Fragment_ExamDetails.str_insurance_category.trim()
                .equals("")
                || Fragment_ExamDetails.str_state.trim().equals("")
                || Fragment_ExamDetails.str_city.equals("")
                || Fragment_ExamDetails.str_exam_center_location.trim().equals(
                "")
                || Fragment_ExamDetails.str_exam_language.trim().equals("")) {

            Toast.makeText(getActivity(),
                    "Please Fill Exam Details Form First", Toast.LENGTH_LONG)
                    .show();

            ((CIFEnrollmentMainActivity) getActivity()).getViewpager().setCurrentItem(3);
            return false;
        } else if (photoByteArrayAsString.trim().equals("")) {

            Toast.makeText(getActivity(), "Please Capture Your Photo",
                    Toast.LENGTH_LONG).show();

            return false;
        } else if (proposer_sign.trim().equals("")) {

            Toast.makeText(getActivity(), "Please Make Your Signature",
                    Toast.LENGTH_LONG).show();

            return false;
        } else if (!is_annexure_uploaded) {
            mCommonMethods.showToast(getActivity(), "Please upload Annexure Document");
            return false;
        } else if (!is_id_card_uploaded) {
            mCommonMethods.showToast(getActivity(), "Please upload ID Card");
            return false;
        } else if (!is_pan_card_uploaded) {
            mCommonMethods.showToast(getActivity(), "Please upload PAN Card");
            return false;
        }

        return true;

    }

    private void getInput() {
        inputVal = new StringBuilder();
        inputVal.append("<?xml version='1.0' encoding='utf-8' ?><cif>");
        inputVal.append("<quotation_number>" + quotation_no
                + "</quotation_number>");
        inputVal.append("<pf_number>" + str_pf_numbers + "</pf_number>");
        inputVal.append("<candidate_corporate_name>"
                + str_candidate_corporate_name + "</candidate_corporate_name>");
        inputVal.append("<father_name>" + str_father_name + "</father_name>");
        inputVal.append("<category>" + str_category + "</category>");
        inputVal.append("<mobile_no>" + str_mobile_no + "</mobile_no>");
        inputVal.append("<phone_number>" /*+ str_phone_number_1 + "-"*/
                + str_phone_number_2 + "</phone_number>");
        inputVal.append("<email_id>" + str_email_id + "</email_id>");

        inputVal.append("<basic_qualification>" + str_basic_qualification
                + "</basic_qualification>");
        inputVal.append("<board_name_for_basic_qualification>"
                + str_board_name_for_basic_qualification
                + "</board_name_for_basic_qualification>");
        inputVal.append("<roll_number_for_basic_qualification>"
                + str_roll_number_for_basic_qualification
                + "</roll_number_for_basic_qualification>");
        inputVal.append("<year_of_passing_for_basic_qualification>"
                + str_year_of_passing_for_basic_qualification
                + "</year_of_passing_for_basic_qualification>");
        inputVal.append("<educational_qualification>"
                + str_educational_qualification
                + "</educational_qualification>");
        inputVal.append("<other_qualification>" + str_other_qualification
                + "</other_qualification>");

        inputVal.append("<insurance_category>" + str_insurance_category
                + "</insurance_category>");
        inputVal.append("<state>" + str_state + "</state>");
        inputVal.append("<city>" + str_city + "</city>");
        inputVal.append("<exam_center_location>" + str_exam_center_location
                + "</exam_center_location>");
        inputVal.append("<exam_language>" + str_exam_language
                + "</exam_language>");

        inputVal.append("<photoByteArrayAsString>" + photoByteArrayAsString
                + "</photoByteArrayAsString>");
        inputVal.append("<proposer_sign>" + proposer_sign + "</proposer_sign>");
        inputVal.append("<createdDate>" + createdDate + "</createdDate>");
        inputVal.append("<dob>" + str_dob + "</dob>");
        inputVal.append("<sex>" + str_sex + "</sex>");
        inputVal.append("<pan_no>" + str_pan_no + "</pan_no>");
        inputVal.append("<branch_name>" + str_branch_name + "</branch_name>");
        inputVal.append("<current_house_number>" + str_address1
                + "</current_house_number>");
        inputVal.append("<current_street>" + str_address2 + "</current_street>");
        inputVal.append("<current_district>" + str_current_district
                + "</current_district>");
        inputVal.append("<current_pincode>" + str_pincode
                + "</current_pincode>");
        inputVal.append("<area>" + str_area + "</area>");
        inputVal.append("<permanent_district>" + str_permanent_district
                + "</permanent_district>");
        //added by rajan 24-10-2017
        inputVal.append("<AADHAR_NO>" + str_aadhar_card_no
                + "</AADHAR_NO>");
        inputVal.append("<CIF_CONTACTPERSON_EMAIL_ID>" + str_contact_person_eamil_id
                + "</CIF_CONTACTPERSON_EMAIL_ID>");

        //added by rajan 25-09-2018
        inputVal.append("<CIF_IS_ANNEXURE_UPLOADED>" + is_annexure_uploaded
                + "</CIF_IS_ANNEXURE_UPLOADED>");

        //added by rajan 10-01-2019
        inputVal.append("<CIF_IS_ID_CARD_UPLOADED>" + is_id_card_uploaded
                + "</CIF_IS_ID_CARD_UPLOADED>");

        //added by rajan 10-01-2019
        inputVal.append("<CIF_IS_PAN_CARD_UPLOADED>" + is_pan_card_uploaded
                + "</CIF_IS_PAN_CARD_UPLOADED>");

        inputVal.append("</cif>");

    }

    private String getInputEducation() {
        StringBuilder inputEducation = new StringBuilder();
        inputEducation.append("<?xml version='1.0' encoding='utf-8' ?><cif>");
        inputEducation.append("<quotation_number>" + quotation_no
                + "</quotation_number>");
        inputEducation.append("<pf_number>" + str_pf_numbers + "</pf_number>");
        inputEducation.append("<candidate_corporate_name>"
                + str_candidate_corporate_name + "</candidate_corporate_name>");
        inputEducation.append("<father_name>" + str_father_name + "</father_name>");
        inputEducation.append("<category>" + str_category + "</category>");
        inputEducation.append("<mobile_no>" + str_mobile_no + "</mobile_no>");
        inputEducation.append("<phone_number>" /*+ str_phone_number_1 + "-"*/
                + str_phone_number_2 + "</phone_number>");
        inputEducation.append("<email_id>" + str_email_id + "</email_id>");

        inputEducation.append("<basic_qualification>" + str_basic_qualification
                + "</basic_qualification>");
        inputEducation.append("<board_name_for_basic_qualification>"
                + str_board_name_for_basic_qualification
                + "</board_name_for_basic_qualification>");
        inputEducation.append("<roll_number_for_basic_qualification>"
                + str_roll_number_for_basic_qualification
                + "</roll_number_for_basic_qualification>");
        inputEducation.append("<year_of_passing_for_basic_qualification>"
                + str_year_of_passing_for_basic_qualification
                + "</year_of_passing_for_basic_qualification>");
        inputEducation.append("<educational_qualification>"
                + str_educational_qualification
                + "</educational_qualification>");
        inputEducation.append("<other_qualification>" + str_other_qualification
                + "</other_qualification>");

        inputEducation.append("<insurance_category>" + str_insurance_category
                + "</insurance_category>");
        inputEducation.append("<state>" + str_state + "</state>");
        inputEducation.append("<city>" + str_city + "</city>");
        inputEducation.append("<exam_center_location>" + str_exam_center_location
                + "</exam_center_location>");
        inputEducation.append("<exam_language>" + str_exam_language
                + "</exam_language>");

        inputEducation.append("<photoByteArrayAsString>" + photoByteArrayAsString
                + "</photoByteArrayAsString>");
        inputEducation.append("<proposer_sign>" + proposer_sign + "</proposer_sign>");
        inputEducation.append("<createdDate>" + createdDate + "</createdDate>");
        inputEducation.append("<dob>" + str_dob + "</dob>");
        inputEducation.append("<sex>" + str_sex + "</sex>");
        inputEducation.append("<pan_no>" + str_pan_no + "</pan_no>");
        inputEducation.append("<branch_name>" + str_branch_name + "</branch_name>");
        inputEducation.append("<current_house_number>" + str_address1
                + "</current_house_number>");
        inputEducation.append("<current_street>" + str_address2 + "</current_street>");
        inputEducation.append("<current_district>" + str_current_district
                + "</current_district>");
        inputEducation.append("<current_pincode>" + str_pincode
                + "</current_pincode>");
        inputEducation.append("<area>" + str_area + "</area>");
        inputEducation.append("<permanent_district>" + str_permanent_district
                + "</permanent_district>");
        //added by rajan 24-10-2017
        inputEducation.append("<AADHAR_NO>" + str_aadhar_card_no
                + "</AADHAR_NO>");
        inputEducation.append("<CIF_CONTACTPERSON_EMAIL_ID>" + str_contact_person_eamil_id
                + "</CIF_CONTACTPERSON_EMAIL_ID>");

        //added by rajan 25-09-2018
        inputEducation.append("<CIF_IS_ANNEXURE_UPLOADED>" + is_annexure_uploaded
                + "</CIF_IS_ANNEXURE_UPLOADED>");

        //added by rajan 10-01-2019
        inputEducation.append("<CIF_IS_ID_CARD_UPLOADED>" + is_id_card_uploaded
                + "</CIF_IS_ID_CARD_UPLOADED>");

        //added by rajan 10-01-2019
        inputEducation.append("<CIF_IS_PAN_CARD_UPLOADED>" + is_pan_card_uploaded
                + "</CIF_IS_PAN_CARD_UPLOADED>");

        //added by rajan 14-05-2021
        inputEducation.append("<BDM_email>" + edt_bdm_email.getText().toString()
                + "</BDM_email>");

        inputEducation.append("<BDM_mobile>" + edt_bdm_mobile_no.getText().toString()
                + "</BDM_mobile>");

        inputEducation.append("<Sales_Support_email>" + edt_sales_support_email_id.getText().toString()
                + "</Sales_Support_email>");

        inputEducation.append("<Sales_Support_mobile>" + edt_sales_support_mobile.getText().toString()
                + "</Sales_Support_mobile>");
        //end by rajan 14-05-2021

        inputEducation.append("</cif>");


        return inputEducation.toString();
    }

    /*private Bitmap compressImage(File filePath, int imgRate) {

        // String filePath = getRealPathFromURI(imageUri);
        try {

            BitmapFactory.Options options = new BitmapFactory.Options();

            // by setting this field as true, the actual bitmap pixels are not
            // loaded in the memory. Just the bounds are loaded. If
            // you try the use the bitmap here, you will get null.
            options.inJustDecodeBounds = true;
            Bitmap bmp = BitmapFactory.decodeFile(filePath.getAbsolutePath(),
                    options);

            int actualHeight = options.outHeight;
            int actualWidth = options.outWidth;

            // max Height and width values of the compressed image is taken as
            // 816x612
            float maxHeight = 816.0f;
            float maxWidth = 612.0f;

            if (imgRate == 1) {
                maxHeight = 816.0f;
                maxWidth = 612.0f;
            } else if (imgRate == 2) {
                maxHeight = 516.0f;
                maxWidth = 312.0f;
            } else if (imgRate == 3) {
                maxHeight = 616.0f;
                maxWidth = 412.0f;
            } else if (imgRate == 4) {
                maxHeight = 716.0f;
                maxWidth = 512.0f;
            }

            float imgRatio = actualWidth / actualHeight;
            float maxRatio = maxWidth / maxHeight;

            // width and height values are set maintaining the aspect ratio of the
            // image

            if (actualHeight > maxHeight || actualWidth > maxWidth) {
                if (imgRatio < maxRatio) {
                    imgRatio = maxHeight / actualHeight;
                    actualWidth = (int) (imgRatio * actualWidth);
                    actualHeight = (int) maxHeight;
                } else if (imgRatio > maxRatio) {
                    imgRatio = maxWidth / actualWidth;
                    actualHeight = (int) (imgRatio * actualHeight);
                    actualWidth = (int) maxWidth;
                } else {
                    actualHeight = (int) maxHeight;
                    actualWidth = (int) maxWidth;
                }
            }

            // setting inSampleSize value allows to load a scaled down version of
            // the original image

            options.inSampleSize = calculateInSampleSize(options, actualWidth,
                    actualHeight);

            // inJustDecodeBounds set to false to load the actual bitmap
            options.inJustDecodeBounds = false;

            // this options allow android to claim the bitmap memory if it runs low
            // on memory
            options.inPurgeable = true;
            options.inInputShareable = true;
            options.inTempStorage = new byte[16 * 1024];

            try {
                // load the bitmap from its path
                bmp = BitmapFactory.decodeFile(filePath.getAbsolutePath(), options);
            } catch (OutOfMemoryError exception) {
                exception.printStackTrace();

            }
            try {
                scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight,
                        Bitmap.Config.ARGB_8888);
            } catch (Error exception) {
                exception.printStackTrace();
            }

            float ratioX = actualWidth / (float) options.outWidth;
            float ratioY = actualHeight / (float) options.outHeight;
            float middleX = actualWidth / 2.0f;
            float middleY = actualHeight / 2.0f;

            Matrix scaleMatrix = new Matrix();
            scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

            Canvas canvas = new Canvas(scaledBitmap);
            canvas.setMatrix(scaleMatrix);
            canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2,
                    middleY - bmp.getHeight() / 2, new Paint(
                            Paint.FILTER_BITMAP_FLAG));

            // check the rotation of the image and display it properly
            ExifInterface exif;
            try {
                exif = new ExifInterface(filePath.getAbsolutePath());

                int orientation = exif.getAttributeInt(
                        ExifInterface.TAG_ORIENTATION, 0);
                Log.d("EXIF", "Exif: " + orientation);
                Matrix matrix = new Matrix();
                if (orientation == 6) {
                    matrix.postRotate(90);
                    Log.d("EXIF", "Exif: " + orientation);
                } else if (orientation == 3) {
                    matrix.postRotate(180);
                    Log.d("EXIF", "Exif: " + orientation);
                } else if (orientation == 8) {
                    matrix.postRotate(270);
                    Log.d("EXIF", "Exif: " + orientation);
                }
                scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                        scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
                        true);
            } catch (Exception e) {
                e.printStackTrace();
            }

            FileOutputStream out = null;
            String filename = getFilename();
            try {
                out = new FileOutputStream(filename);

                // write the compressed bitmap at the destination specified by
                // filename.
                scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);

            } catch (Exception e) {
                e.printStackTrace();
            }

            File file = new File(filename);
            int file_size = Integer.parseInt(String.valueOf(file.length() / 1024));

            if (file_size > 150) {
                compressImage(filePath, 2);//2
            } else if (file_size > 100) {
                compressImage(filePath, 3);//3
            } else if (file_size > 50) {
                compressImage(filePath, 4);//4
            }
        } catch (Error | ArithmeticException er) {
            er.printStackTrace();
        }
        return scaledBitmap;
    }

    private int calculateInSampleSize(BitmapFactory.Options options,
                                      int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height
                    / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }

    private String getFilename() {
        File file = mStorageUtils.createFileToAppSpecificDirCIF(mContext,
                System.currentTimeMillis() + ".jpg");
        return file.getAbsolutePath();
    }*/

    private void uploadDoc(File mFile) {

        str_extension = mFile.getPath().substring(mFile.getPath().lastIndexOf("."));

        FileInputStream fin = null;

        try {

            if (mFile != null)
                fin = new FileInputStream(mFile);

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
                    /*mAsyncUploadAnnexure = new AsyncUploadAnnexure();
                    mAsyncUploadAnnexure.execute();*/

                    createSoapRequestToUploadDoc();

                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(getActivity(), "Please Browse/Capture Document", Toast.LENGTH_LONG).show();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void browseDocsORImage() {
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_tcc_education_next:
                onEducationNext();
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
                    Check = CHECK_TYPE_HSC;
                    capture_docs(Check);
                }
                break;

            case R.id.imgbtn_tcc_education_doc_hsc_browse:
                if (spnr_tcc_education_doc_hsc.getSelectedItem().toString().equalsIgnoreCase("Select Document")) {
                    mCommonMethods.showToast(mContext, "Please Select Document");
                } else {
                    Check = CHECK_TYPE_HSC;
                    browseDocsORImage();
                }
                break;

            case R.id.imgbtn_tcc_education_doc_hsc_upload:
                if (spnr_tcc_education_doc_hsc.getSelectedItem().toString().equalsIgnoreCase("Select Document")) {
                    mCommonMethods.showToast(mContext, "Please Select Document");
                } else {
                    if (mHSCFile != null)
                        uploadDoc(mHSCFile);
                    else
                        mCommonMethods.showToast(mContext, "Plese Select Proper File");
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
                    Check = CHECK_TYPE_DEGREE;
                    capture_docs(Check);
                }
                break;

            case R.id.imgbtn_tcc_education_doc_degree_browse:
                if (spnr_tcc_education_doc_degree.getSelectedItem().toString().equalsIgnoreCase("Select Document")) {
                    mCommonMethods.showToast(mContext, "Please Select Document");
                } else {
                    Check = CHECK_TYPE_DEGREE;
                    browseDocsORImage();
                }
                break;

            case R.id.imgbtn_tcc_education_doc_degree_upload:
                if (spnr_tcc_education_doc_degree.getSelectedItem().toString().equalsIgnoreCase("Select Document")) {
                    mCommonMethods.showToast(mContext, "Please Select Document");
                } else {
                    if (mDegreeFile != null)
                        uploadDoc(mDegreeFile);
                    else
                        mCommonMethods.showToast(mContext, "Plese Select Proper File");
                }
                break;

            case R.id.btn_tcc_queries_submit:

                if (!edt_tcc_query_place.getText().toString().replaceAll("\\s+", "").trim().equals("")) {

                    if (mCommonMethods.isNetworkConnected(mContext)) {

                        Check = QUESTIONS_DOC;
                        //1. Create pdf
                        //new AsyncCreatePDF().execute(Check);

                        Single.fromCallable(() -> create_tcc_questions_Pdf())
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Consumer<Boolean>() {
                                    @Override
                                    public void accept(@NonNull Boolean result) throws Exception {
                                        if (result) {
                                            File mPdfFile = mStorageUtils.createFileToAppSpecificDirCIF(mContext, QUESTIONS_DOC + str_pf_numbers + ".pdf");

                                            if (mPdfFile != null) {
                                                uploadDoc(mPdfFile);
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

                    } else {
                        mCommonMethods.showToast(mContext, mCommonMethods.NO_INTERNET_MESSAGE);
                    }
                } else {
                    mCommonMethods.showMessageDialog(mContext, "Please enter place.");
                }
                break;

            case R.id.btn_tcc_declaration_submit:

                if (chk_tcc_declaration.isChecked()) {

                    //1st synch all data then upload queries pdf

                    Check = SCHEDULE3_DOC;
                    File mPdfFile = mStorageUtils.createFileToAppSpecificDirCIF(mContext,
                            str_pf_numbers + SCHEDULE3_DOC + ".pdf");
                    uploadDoc(mPdfFile);

                } else {
                    mCommonMethods.showMessageDialog(mContext, "Please Agree terms and conditons.");
                }

                break;

            default:
                break;
        }
    }

    private void initialiseEducationalPart(View rootView) {

        lstDocs.add("Select Document");
        lstDocs.add("Mark Sheet");
        lstDocs.add("Passing Certificate");

        spnr_tcc_education_doc_hsc = rootView.findViewById(R.id.spnr_tcc_education_doc_hsc);
        mCommonMethods.fillSpinnerValue(mContext, spnr_tcc_education_doc_hsc, lstDocs);

        spnr_tcc_education_doc_degree = rootView.findViewById(R.id.spnr_tcc_education_doc_degree);
        mCommonMethods.fillSpinnerValue(mContext, spnr_tcc_education_doc_degree, lstDocs);

        ib_score_card_hsc_doc_view = rootView.findViewById(R.id.ib_score_card_hsc_doc_view);
        imgbtn_tcc_education_doc_hsc_camera = rootView.findViewById(R.id.imgbtn_tcc_education_doc_hsc_camera);
        imgbtn_tcc_education_doc_hsc_browse = rootView.findViewById(R.id.imgbtn_tcc_education_doc_hsc_browse);
        imgbtn_tcc_education_doc_hsc_upload = rootView.findViewById(R.id.imgbtn_tcc_education_doc_hsc_upload);

        ib_score_card_degree_doc_view = rootView.findViewById(R.id.ib_score_card_degree_doc_view);
        imgbtn_tcc_education_doc_degree_camera = rootView.findViewById(R.id.imgbtn_tcc_education_doc_degree_camera);
        imgbtn_tcc_education_doc_degree_browse = rootView.findViewById(R.id.imgbtn_tcc_education_doc_degree_browse);
        imgbtn_tcc_education_doc_degree_upload = rootView.findViewById(R.id.imgbtn_tcc_education_doc_degree_upload);

        imgbtn_tcc_education_doc_hsc_camera.setEnabled(true);
        imgbtn_tcc_education_doc_hsc_browse.setEnabled(true);
        imgbtn_tcc_education_doc_hsc_upload.setEnabled(true);

        imgbtn_tcc_education_doc_degree_camera.setEnabled(true);
        imgbtn_tcc_education_doc_degree_browse.setEnabled(true);
        imgbtn_tcc_education_doc_degree_upload.setEnabled(true);

        ib_score_card_hsc_doc_view.setOnClickListener(this);
        imgbtn_tcc_education_doc_hsc_camera.setOnClickListener(this);
        imgbtn_tcc_education_doc_hsc_browse.setOnClickListener(this);
        imgbtn_tcc_education_doc_hsc_upload.setOnClickListener(this);

        ib_score_card_degree_doc_view.setOnClickListener(this);
        imgbtn_tcc_education_doc_degree_camera.setOnClickListener(this);
        imgbtn_tcc_education_doc_degree_browse.setOnClickListener(this);
        imgbtn_tcc_education_doc_degree_upload.setOnClickListener(this);

        btn_tcc_education_next = rootView.findViewById(R.id.btn_tcc_education_next);
        btn_tcc_education_next.setOnClickListener(this);

        edt_bdm_email = rootView.findViewById(R.id.edt_bdm_email);
        edt_bdm_mobile_no = rootView.findViewById(R.id.edt_bdm_mobile_no);
        edt_sales_support_email_id = rootView.findViewById(R.id.edt_sales_support_email_id);
        edt_sales_support_mobile = rootView.findViewById(R.id.edt_sales_support_mobile);
    }

    private void capture_docs(String str_doc) {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        String imageFileName = str_pf_numbers + "_" + str_doc + ".jpg";

        if (str_doc.equals(CHECK_TYPE_HSC)) {
            mHSCFile = mStorageUtils.createFileToAppSpecificDirCIF(mContext, imageFileName);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mCommonMethods.getContentUri(
                        mContext, mHSCFile));
            } else {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mHSCFile));
            }
        } else if (str_doc.equals(CHECK_TYPE_DEGREE)) {

            mDegreeFile = mStorageUtils.createFileToAppSpecificDirCIF(mContext, imageFileName);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mCommonMethods.getContentUri(
                        mContext, mDegreeFile));
            } else {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mDegreeFile));
            }
        }
        startActivityForResult(takePictureIntent, REQUEST_CODE_PICK_PHOTO_FILE);
    }

    private void onEducationNext() {

        String strError = validateEducationDetails();

        if (strError.equals("")) {

            getValueFromDatabase_New();
            String strInput = getInputEducation();

            try {
                M_MainActivity_Data data = new M_MainActivity_Data(quotation_no, str_pf_numbers, strInput);

                long count = db.insertCIFDetail_New(data, quotation_no);
                if (count > 0) {
                    Toast toast = Toast.makeText(getActivity(),
                            "Data Inserted Successfully",
                            Toast.LENGTH_SHORT);
                    toast.show();
                }

                M_UserInformation DashboardDetail_data = new M_UserInformation(
                        quotation_no,
                        str_pf_numbers,
                        Fragment_PersonalDetails.str_candidate_corporate_name,
                        Fragment_ContactDetails.str_mobile_no,
                        Fragment_ContactDetails.str_email_id,
                        createdDate);

                DashboardDetail_data.setStr_aadhar_card_no(str_aadhar_card_no);
                DashboardDetail_data.setStr_contact_person_email_id(str_contact_person_eamil_id);

                long rowId2 = db.insertDashBoardDetail(DashboardDetail_data);
                if (rowId2 > 0) {
                    mCommonMethods.showToast(mContext, "Data Inserted Successfully");
                }

                //visible Annexure 1 layout and gone other layouts
                ll_identification_main.setVisibility(View.GONE);
                ll_educational_details.setVisibility(View.GONE);
                ll_annexure1_fit_proper.setVisibility(View.VISIBLE);
                ll_schedule3.setVisibility(View.GONE);

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            mCommonMethods.showMessageDialog(mContext, strError);
        }
    }

    private String validateEducationDetails() {

        if (mHSCFile == null && mDegreeFile == null) {
            mHSCFile = null;
            mDegreeFile = null;
            return "Please Uplaod Minimum HSC Document";
        }

        strBDMEmail = edt_bdm_email.getText().toString();
        strSaleSupportEmail = edt_sales_support_email_id.getText().toString();

        if (!mCommonMethods.emailPatternValidation(edt_bdm_email, mContext)) {
            edt_bdm_email.requestFocus();
            return "Please Enter Proper BDM email ID";
        }

        if (!strBDMEmail.contains("sbilife.co.in")) {
            edt_bdm_email.requestFocus();
            return "BDM Email ID is not belongs to SBI Life Insurance.";
        }

        if (!mCommonMethods.mobileNumberPatternValidation(edt_bdm_mobile_no, mContext)) {
            return "Inavalid BDM Mobile Number";
        }

        if (!mCommonMethods.emailPatternValidation(edt_sales_support_email_id, mContext)) {
            edt_sales_support_email_id.requestFocus();
            return "Please Enter Proper Sales Support email ID";
        }

        if (!strSaleSupportEmail.contains("sbilife.co.in")) {
            edt_sales_support_email_id.requestFocus();
            return "Sales Support Email ID is not belongs to SBI Life Insurance.";
        }

        if (!mCommonMethods.mobileNumberPatternValidation(edt_sales_support_mobile, mContext)) {
            return "Inavalid Sales Support Mobile Number";
        }

        return "";
    }

    private void initialiseAnnexure1Part(View rootView) {

        btn_tcc_queries_submit = rootView.findViewById(R.id.btn_tcc_queries_submit);

        chk_tcc_que_a = rootView.findViewById(R.id.chk_tcc_que_a);
        chk_tcc_que_b = rootView.findViewById(R.id.chk_tcc_que_b);
        chk_tcc_que_c = rootView.findViewById(R.id.chk_tcc_que_c);
        chk_tcc_que_d = rootView.findViewById(R.id.chk_tcc_que_d);
        chk_tcc_que_e = rootView.findViewById(R.id.chk_tcc_que_e);
        chk_tcc_que_f = rootView.findViewById(R.id.chk_tcc_que_f);
        chk_tcc_que_g = rootView.findViewById(R.id.chk_tcc_que_g);
        chk_tcc_que_h = rootView.findViewById(R.id.chk_tcc_que_h);
        chk_tcc_que_i = rootView.findViewById(R.id.chk_tcc_que_i);
        chk_tcc_que_j = rootView.findViewById(R.id.chk_tcc_que_j);
        chk_tcc_que_k = rootView.findViewById(R.id.chk_tcc_que_k);
        chk_tcc_que_l = rootView.findViewById(R.id.chk_tcc_que_l);
        chk_tcc_que_m = rootView.findViewById(R.id.chk_tcc_que_m);
        chk_tcc_que_n = rootView.findViewById(R.id.chk_tcc_que_n);
        chk_tcc_que_o = rootView.findViewById(R.id.chk_tcc_que_o);

        /*edt_tcc_query_name = (EditText) findViewById(R.id.edt_tcc_query_name);*/
        edt_tcc_query_place = rootView.findViewById(R.id.edt_tcc_query_place);
        txt_tcc_query_date = rootView.findViewById(R.id.txt_tcc_query_date);
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
                    QUESTIONS_DOC + str_pf_numbers + ".pdf");

            Rectangle rect = new Rectangle(594f, 792f);

            Document document = new Document(rect, 50, 50, 50, 50);
            // Document document = new Document(PageSize.A4, 50, 50, 50, 50);
            PdfWriter pdf_writer = null;
            pdf_writer = PdfWriter.getInstance(document, new FileOutputStream(quesFile.getAbsolutePath()));

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
            byte[] fbyt_applicant = Base64.decode(proposer_sign, 0);
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

    private void initialiseSchedule3Part(View rootView) {

        TextView txt_tcc_declaration = rootView.findViewById(R.id.txt_tcc_declaration);
        chk_tcc_declaration = rootView.findViewById(R.id.chk_tcc_declaration);
        btn_tcc_declaration_submit = rootView.findViewById(R.id.btn_tcc_declaration_submit);
        btn_tcc_declaration_submit.setOnClickListener(this);

        txt_tcc_declaration_place = rootView.findViewById(R.id.txt_tcc_declaration_place);
        TextView txt_tcc_declaration_date = rootView.findViewById(R.id.txt_tcc_declaration_date);

        txt_tcc_declaration_date.setText(txt_tcc_query_date.getText().toString());

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

            mSchedule3File = mStorageUtils.createFileToAppSpecificDirCIF(mContext, str_pf_numbers + SCHEDULE3_DOC + ".pdf");

            Rectangle rect = new Rectangle(594f, 792f);

            //Document document = new Document(rect, 50, 50, 50, 50);
            Document document = new Document(PageSize.A4, 50, 50, 50, 50);
            PdfWriter pdf_writer = PdfWriter.getInstance(document, new FileOutputStream(mSchedule3File.getAbsolutePath()));

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
            byte[] fbyt_applicant = Base64.decode(proposer_sign, 0);
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

    private void createSoapRequestToUploadDoc() {
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME_UPLOAD_ALL_DOC);

        request.addProperty("f", mAllBytes);

        if (Check.equals(CHECK_TYPE_ANNEXURE_CAPTURE)) {
            //request.addProperty("fileName", fileAnnexure.getName().toString());
            mCommonMethods.printLog("File Name Annexture : ", fileAnnexure.getName().toString());
            request.addProperty("fileName", str_pf_numbers + CHECK_TYPE_ANNEXURE_CAPTURE + str_extension);
        } else if (Check.equals(CHECK_TYPE_ID_CARD_CAPTURE)) {
            //request.addProperty("fileName", fileIDCard.getName().toString()name = "UploadFile_CIFEnroll");
            request.addProperty("fileName", str_pf_numbers + CHECK_TYPE_ID_CARD_CAPTURE + str_extension);
        } else if (Check.equals(CHECK_TYPE_PAN_CARD_CAPTURE)) {
            //request.addProperty("fileName", filePanCard.getName().toString());
            request.addProperty("fileName", str_pf_numbers + CHECK_TYPE_PAN_CARD_CAPTURE + str_extension);
        } else if (Check.equals(CHECK_TYPE_HSC)) {
            request.addProperty("fileName", mHSCFile.getName());
        } else if (Check.equals(CHECK_TYPE_DEGREE)) {
            request.addProperty("fileName", mDegreeFile.getName());
        } else if (Check.equals(QUESTIONS_DOC)) {
            request.addProperty("fileName", QUESTIONS_DOC + str_pf_numbers + ".pdf");
        } else if (Check.equals(SCHEDULE3_DOC)) {
            request.addProperty("fileName", str_pf_numbers + SCHEDULE3_DOC + ".pdf");
        }

        request.addProperty("PFNo", str_pf_numbers);
        request.addProperty("strType", "I");

        //for UAT
        //request.addProperty("Authkey", mCommonMethods.getStr_cif_auth_key());

        mAsyncUploadFileCifEnroll = new AsyncUploadFile_CIF(mContext, this, request, METHOD_NAME_UPLOAD_ALL_DOC);
        mAsyncUploadFileCifEnroll.execute();
    }

    @Override
    public void onUploadComplete(Boolean result) {
        if (result) {
            mAllBytes = null;

            if (Check.equals(CHECK_TYPE_ANNEXURE_CAPTURE)) {

                imgbtn_annexure_upload.setImageDrawable(getResources().getDrawable(R.drawable.checkedupload));
                mCommonMethods.showToast(getActivity(), "Document Upload Successfully...");

                is_annexure_uploaded = true;

            } else if (Check.equals(CHECK_TYPE_ID_CARD_CAPTURE)) {

                imgbtn_id_card_upload.setImageDrawable(getResources().getDrawable(R.drawable.checkedupload));

                mCommonMethods.showToast(getActivity(), "Document Upload Successfully...");

                is_id_card_uploaded = true;

            } else if (Check.equals(CHECK_TYPE_PAN_CARD_CAPTURE)) {

                imgbtn_pan_card_upload.setImageDrawable(getResources().getDrawable(R.drawable.checkedupload));

                mCommonMethods.showToast(getActivity(), "Document Upload Successfully...");

                is_pan_card_uploaded = true;
            } else if (Check.equals(CHECK_TYPE_HSC)) {

                        /*imgbtn_tcc_education_doc_hsc_preview.setEnabled(false);
                        imgbtn_tcc_education_doc_hsc_dlt.setEnabled(false);*/
                imgbtn_tcc_education_doc_hsc_camera.setEnabled(false);
                imgbtn_tcc_education_doc_hsc_browse.setEnabled(false);
                imgbtn_tcc_education_doc_hsc_upload.setEnabled(false);
                imgbtn_tcc_education_doc_hsc_upload.setImageDrawable(getResources().getDrawable(R.drawable.checkedupload));
            } else if (Check.equals(CHECK_TYPE_DEGREE)) {

                        /*imgbtn_tcc_education_doc_degree_preview.setEnabled(false);
                        imgbtn_tcc_education_doc_degree_dlt.setEnabled(false);*/
                imgbtn_tcc_education_doc_degree_camera.setEnabled(false);
                imgbtn_tcc_education_doc_degree_browse.setEnabled(false);
                imgbtn_tcc_education_doc_degree_upload.setEnabled(false);
                imgbtn_tcc_education_doc_degree_upload.setImageDrawable(getResources().getDrawable(R.drawable.checkedupload));
            } else if (Check.equals(QUESTIONS_DOC)) {
                //create Schedule3 pdf and set place from questionary
                //new AsyncCreatePDF().execute(SCHEDULE3_DOC);

                Single.fromCallable(() -> createSchedule3PDF())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<Boolean>() {
                            @Override
                            public void accept(@NonNull Boolean out) throws Exception {
                                if (out) {

                                    txt_tcc_declaration_place.setText(edt_tcc_query_place.getText().toString());

                                    String str_declare = "I " + "<font color=\"blue\">" + str_candidate_corporate_name + "</font>, confirm that the above "
                                            + "information is, to the best of my knowledge and belief, true and complete. I undertake to keep the Authority "
                                            + "fully informed, as soon as possible, of all events, which take place subsequent to my appointment,"
                                            + " which are relevant to the information provided above.";

                                    chk_tcc_declaration.setText(Html.fromHtml(str_declare), TextView.BufferType.SPANNABLE);

                                    //visible Schedule 3 layout and gone other layouts
                                    ll_identification_main.setVisibility(View.GONE);
                                    ll_educational_details.setVisibility(View.GONE);
                                    ll_annexure1_fit_proper.setVisibility(View.GONE);
                                    ll_schedule3.setVisibility(View.VISIBLE);

                                } else {
                                    mCommonMethods.printLog("Error", "While Creating Schedule-3 PDF");
                                }
                            }
                        }, throwable -> {
                            mCommonMethods.showToast(mContext, "File Not Found");
                        });

            } else if (Check.equals(SCHEDULE3_DOC)) {
                ((CIFEnrollmentMainActivity) getActivity()).getViewpager().setCurrentItem(5);
            }

        } else {

            if (Check.equals(CHECK_TYPE_ANNEXURE_CAPTURE)) {
                is_annexure_uploaded = false;
            } else if (Check.equals(CHECK_TYPE_ID_CARD_CAPTURE)) {
                is_id_card_uploaded = false;
            } else if (Check.equals(CHECK_TYPE_PAN_CARD_CAPTURE)) {
                is_pan_card_uploaded = false;
            }

            mCommonMethods.showToast(getActivity(), "PLease try agian later..");
        }
    }
}
