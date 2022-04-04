package sbilife.com.pointofsale_bancaagency.cifenrollment.phase1;

import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import sbilife.com.pointofsale_bancaagency.DatabaseHelper;
import sbilife.com.pointofsale_bancaagency.ParseXML;
import sbilife.com.pointofsale_bancaagency.R;
import sbilife.com.pointofsale_bancaagency.ServiceURL;
import sbilife.com.pointofsale_bancaagency.cifenrollment.CIFDocUploadActivity;
import sbilife.com.pointofsale_bancaagency.cifenrollment.CIFEnrollCorDownload;
import sbilife.com.pointofsale_bancaagency.cifenrollment.CIF_ScoreCard_UploadActivity;
import sbilife.com.pointofsale_bancaagency.cifenrollment.CIF_TCC_UploadActivity;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;

public class CIFEnrollmentPFActivity extends AppCompatActivity implements View.OnClickListener {

    public static String quotation_Number = "";
    static String str_pf_number = "";
    private static String formattedDate = "";
    private final String NAMESPACE = "http://tempuri.org/";
    private final String METHOD_NAME_CIF = "getCIFEnrollmentInfo";
    private final String METHOD_NAME_CIF_EXAM = "getCIFExamDetail";
    ArrayList<ParseXML.XMLHolderIssuPolicy> data;
    private EditText edt_cif_pf_number;
    private DatabaseHelper db = new DatabaseHelper(this);
    private ProgressDialog mProgressDialog;
    private AsyncDownloadCIFProfile CIFProfile;
    private AsyncDownloadCIFExam CIFExam;
    private String str_candidate_corporate_name = "", str_mobile_no = "", str_email_id = "", str_city = "", str_state = "",
            str_exam_center_location = "", str_dob = "", str_sex = "", str_pan_no = "", str_branch_name = "",
            str_current_district = "", str_permanent_district = "", str_address1 = "", str_address2 = "", str_pincode = "",
            str_state_id = "", str_area = "", quotation_num = "", inputciflist = "", inputciflist_exam = "";
    private StringBuilder inputVal;
    private CommonMethods mCommonMethods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.cifenrollment_pf_number_popup);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.cifenrollment_pf_activity_title);

        mCommonMethods = new CommonMethods();

        edt_cif_pf_number = findViewById(R.id.edt_cif_pf_number);

        Button btn_submit = findViewById(R.id.btn_submit);
        Button btn_view_dashboard = findViewById(R.id.btn_view_dashboard);

        LinearLayout ll_tcc_upload = findViewById(R.id.ll_tcc_upload);
        ImageButton ib_tcc_upload = findViewById(R.id.ib_tcc_upload);
        TextView tv_tcc_upload = findViewById(R.id.tv_tcc_upload);

        LinearLayout ll_score_card_upload = findViewById(R.id.ll_score_card_upload);
        ImageButton ib_score_card_upload = findViewById(R.id.ib_score_card_upload);
        TextView tv_score_card_upload = findViewById(R.id.tv_score_card_upload);

        LinearLayout ll_cor_download = findViewById(R.id.ll_cor_download);
        ImageButton ib_cor_download = findViewById(R.id.ib_cor_download);
        TextView tv_cor_download = findViewById(R.id.tv_cor_download);

        LinearLayout ll_cif_doc_upload = findViewById(R.id.ll_cif_doc_upload);
        ImageButton ib_cif_doc_upload = findViewById(R.id.ib_cif_doc_upload);
        TextView tv_cif_doc_upload = findViewById(R.id.tv_cif_doc_upload);

        btn_submit.setOnClickListener(this);
        btn_view_dashboard.setOnClickListener(this);

        ll_tcc_upload.setOnClickListener(this);
        ib_tcc_upload.setOnClickListener(this);
        tv_tcc_upload.setOnClickListener(this);

        ll_score_card_upload.setOnClickListener(this);
        ib_score_card_upload.setOnClickListener(this);
        tv_score_card_upload.setOnClickListener(this);

        ll_cor_download.setOnClickListener(this);
        ib_cor_download.setOnClickListener(this);
        tv_cor_download.setOnClickListener(this);

        ll_cif_doc_upload.setOnClickListener(this);
        ib_cif_doc_upload.setOnClickListener(this);
        tv_cif_doc_upload.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        Intent mIntent;

        switch (view.getId()) {

            case R.id.btn_submit:

                str_pf_number = edt_cif_pf_number.getText().toString();

                if (str_pf_number.equals("")) {
                    mCommonMethods.showToast(getApplicationContext(), "Please Enter PF Number");
                } else {
                    if (mCommonMethods.isNetworkConnected(CIFEnrollmentPFActivity.this)) {
                        GetCIFProfile();
                    } else {
                        mCommonMethods.showToast(getApplicationContext(), mCommonMethods.NO_INTERNET_MESSAGE);
                    }
                }

                break;

            case R.id.btn_view_dashboard:
                mIntent = new Intent(CIFEnrollmentPFActivity.this, CIFEnrollmentDashboardActivity.class);
                startActivity(mIntent);
                break;

            case R.id.ll_tcc_upload:

            case R.id.ib_tcc_upload:

            case R.id.tv_tcc_upload:
                //new cif enrollment TCC and Exam Details 26-03-2018
                mIntent = new Intent(CIFEnrollmentPFActivity.this, CIF_TCC_UploadActivity.class);
                mIntent.putExtra("DASHBOARD", false);
                mIntent.putExtra("URN", "");
                startActivity(mIntent);
                break;

            case R.id.ll_score_card_upload:

            case R.id.ib_score_card_upload:

            case R.id.tv_score_card_upload:
                //mIntent = new Intent(CIFEnrollmentPFActivity.this, ScoreCardDeclarationActivity.class);
                mIntent = new Intent(CIFEnrollmentPFActivity.this, CIF_ScoreCard_UploadActivity.class);
                startActivity(mIntent);
                break;

            case R.id.ll_cor_download:

            case R.id.ib_cor_download:

            case R.id.tv_cor_download:
                //new cif enrollment COR download 10-04-2018
                mIntent = new Intent(CIFEnrollmentPFActivity.this, CIFEnrollCorDownload.class);
                startActivity(mIntent);
                break;

            case R.id.ll_cif_doc_upload:

            case R.id.ib_cif_doc_upload:

            case R.id.tv_cif_doc_upload:
                mIntent = new Intent(CIFEnrollmentPFActivity.this, CIFDocUploadActivity.class);
                startActivity(mIntent);
                break;

            default:
                break;
        }
    }

	/*@Override
	public void onBackPressed() {

	}*/

    @Override
    protected void onResume() {

        edt_cif_pf_number.setText("");
        super.onResume();

    }

    @SuppressWarnings("deprecation")
    private void GetCIFProfile() {

        // "This is a one time registration process, please wait till gets complete.Please Do not Touch Anywhere";
        mProgressDialog = new ProgressDialog(this, ProgressDialog.THEME_HOLO_LIGHT);
        String Message = "Loading Please wait...";
        mProgressDialog.setMessage(Html.fromHtml("<font color='#00a1e3'><b>" + Message + "<b></font>"));
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(false);

        mProgressDialog.setButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                if (CIFProfile != null)
                    CIFProfile.cancel(true);

                mProgressDialog.dismiss();
            }
        });

        mProgressDialog.setMax(100);
        mProgressDialog.show();

        CIFProfile = new AsyncDownloadCIFProfile();
        CIFProfile.execute();

    }

    @SuppressWarnings("deprecation")
    private void GetCIFExamDetails() {

        mProgressDialog = new ProgressDialog(this);
        // String Message =
        // "This is a one time registration process, please wait till gets complete.Please Do not Touch Anywhere";
        String Message = "Please wait ,Loading...";

        mProgressDialog.setMessage(Message);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(false);

        mProgressDialog.setButton("Cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        CIFExam.cancel(true);
                        mProgressDialog.dismiss();
                    }
                });

        mProgressDialog.setMax(100);
        mProgressDialog.show();

        CIFExam = new AsyncDownloadCIFExam();
        try {
            CIFExam.execute().get();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ExecutionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    private boolean getValueFromDatabase() {
        // retrieving data from database
        boolean flag = false;
        List<M_MainActivity_Data> data = db
                .getCIFDetail_Pf_Number(str_pf_number);
        if (data.size() > 0) {

            quotation_num = data.get(0).getStr_quotation();

            flag = true;

        }
        return flag;
    }

    public boolean validation_pf_number() {

        int pf_length = str_pf_number.length();
        Character[] array = new Character[pf_length];

        for (int i = 0; i < pf_length; i++) {

            array[i] = str_pf_number.charAt(i);

        }

        int p1 = Integer.parseInt(array[0].toString()) * 7;
        int p2 = Integer.parseInt(array[1].toString()) * 6;
        int p3 = Integer.parseInt(array[2].toString()) * 5;
        int p4 = Integer.parseInt(array[3].toString()) * 4;
        int p5 = Integer.parseInt(array[4].toString()) * 3;
        int p6 = Integer.parseInt(array[5].toString()) * 2;
        int p7 = Integer.parseInt(array[6].toString());

        int sum = p1 + p2 + p3 + p4 + p5 + p6;

        int reminder = sum % 11;

        int final_number = 11 - reminder;

        return final_number == p7;

    }

    private void getInput() {
        inputVal = new StringBuilder();
        inputVal.append("<?xml version='1.0' encoding='utf-8' ?><cif>");
        inputVal.append("<quotation_number>" + quotation_Number
                + "</quotation_number>");
        inputVal.append("<pf_number>" + str_pf_number + "</pf_number>");

        inputVal.append("<candidate_corporate_name>"
                + str_candidate_corporate_name + "</candidate_corporate_name>");

        inputVal.append("<mobile_no>" + str_mobile_no + "</mobile_no>");
        inputVal.append("<email_id>" + str_email_id + "</email_id>");

        inputVal.append("<state>" + str_state + "</state>");
        inputVal.append("<city>" + str_city + "</city>");
        inputVal.append("<exam_center_location>" + str_exam_center_location
                + "</exam_center_location>");

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

        inputVal.append("</cif>");

    }

    class AsyncDownloadCIFProfile extends
            AsyncTask<String, Void, String> {
        private volatile boolean running = true;

        @Override
        protected String doInBackground(String... param) {
            try {
                //String CIFBirthdate = "";
                running = true;

                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME_CIF);

                request.addProperty("strPFNo", str_pf_number.trim());

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);
                envelope.dotNet = true;

                envelope.setOutputSoapObject(request);

                //allowAllSSL();
                mCommonMethods.TLSv12Enable();

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);

                HttpTransportSE androidHttpTranport = new HttpTransportSE(ServiceURL.SERVICE_URL);
                try {
                    androidHttpTranport.call(NAMESPACE + METHOD_NAME_CIF, envelope);
                    //Object response = envelope.getResponse();

                    SoapPrimitive sa = null;
                    try {
                        sa = (SoapPrimitive) envelope.getResponse();

                        inputciflist = sa.toString();

                        if (inputciflist.equalsIgnoreCase("0")) {

                        } else if (inputciflist.equalsIgnoreCase("1")) {

                        } else {

                            ParseXML prsObj = new ParseXML();

                            inputciflist = prsObj.parseXmlTag(inputciflist,
                                    "NewDataSet");

                            if (inputciflist != null) {

                                inputciflist = prsObj.parseXmlTag(inputciflist,
                                        "Table");

                                str_candidate_corporate_name = new ParseXML()
                                        .parseXmlTag(inputciflist,
                                                "CHD_EMPLOYEENAME");

                                str_dob = new ParseXML().parseXmlTag(
                                        inputciflist, "CHD_DOB");
                                str_sex = new ParseXML().parseXmlTag(
                                        inputciflist, "CHD_GENDER");
                                str_mobile_no = new ParseXML().parseXmlTag(
                                        inputciflist, "CHD_MOBILENO");

                                str_email_id = new ParseXML().parseXmlTag(
                                        inputciflist, "CHD_EMAILID");
                                str_pan_no = new ParseXML().parseXmlTag(
                                        inputciflist, "CHD_PANNO");
                                str_branch_name = new ParseXML().parseXmlTag(
                                        inputciflist, "CHD_BRANCHNAME");
                                str_city = new ParseXML().parseXmlTag(
                                        inputciflist, "CHD_CITY");
                                str_state = new ParseXML().parseXmlTag(
                                        inputciflist, "CHD_STATE");
                                str_exam_center_location = new ParseXML()
                                        .parseXmlTag(inputciflist, "ExamCenter");

                                str_current_district = new ParseXML()
                                        .parseXmlTag(inputciflist,
                                                "CHD_DISTRICT");
                                str_permanent_district = new ParseXML()
                                        .parseXmlTag(inputciflist,
                                                "CHD_DISTRICT");

                                str_address1 = new ParseXML().parseXmlTag(
                                        inputciflist, "CHD_ADDRESS1");
                                str_address2 = new ParseXML().parseXmlTag(
                                        inputciflist, "ADDRESS2");

                                str_pincode = new ParseXML().parseXmlTag(
                                        inputciflist, "CHD_PINCODE");

                                str_state_id = new ParseXML().parseXmlTag(
                                        inputciflist, "CHD_STATEID");

                                str_area = new ParseXML().parseXmlTag(
                                        inputciflist, "CHD_AREA");

                                if (str_dob == null) {
                                    str_dob = "";
                                } else {
                                    Date dobdate = null;
                                    SimpleDateFormat dobextdate = new SimpleDateFormat(
                                            "MM/dd/yyyy");
                                    SimpleDateFormat dobnewdate = new SimpleDateFormat(
                                            "MM-dd-yyyy");
                                    dobdate = dobextdate.parse(str_dob);
                                    str_dob = dobnewdate.format(dobdate);

                                }

                                if (str_candidate_corporate_name == null) {
                                    str_candidate_corporate_name = "";
                                }
                                if (str_dob == null) {
                                    str_dob = "";
                                }
                                if (str_sex == null) {
                                    str_sex = "";
                                }
                                if (str_mobile_no == null) {
                                    str_mobile_no = "";
                                }
                                if (str_email_id == null) {
                                    str_email_id = "";
                                }
                                if (str_pan_no == null) {
                                    str_pan_no = "";
                                }
                                if (str_branch_name == null) {
                                    str_branch_name = "";
                                }
                                if (str_city == null) {
                                    str_city = "";
                                }
                                if (str_state == null) {
                                    str_state = "";
                                }
                                if (str_exam_center_location == null) {
                                    str_exam_center_location = "";
                                }
                                if (str_current_district == null) {
                                    str_current_district = "";
                                }
                                if (str_permanent_district == null) {
                                    str_permanent_district = "";
                                }
                                if (str_address1 == null) {
                                    str_address1 = "";
                                }
                                if (str_address2 == null) {
                                    str_address2 = "";
                                }
                                if (str_pincode == null) {
                                    str_pincode = "";
                                }
                                if (str_state_id == null) {
                                    str_state_id = "";
                                }
                                if (str_area == null) {
                                    str_area = "";
                                }

                            }
                        }
                    } catch (Exception e) {
                        try {
                            throw (e);
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                        mProgressDialog.dismiss();
                        running = false;
                    }

                } catch (IOException e) {
                    try {
                        throw (e);
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                    mProgressDialog.dismiss();
                    running = false;
                } catch (XmlPullParserException e) {
                    try {
                        throw (e);
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                    mProgressDialog.dismiss();
                    running = false;
                }
            } catch (Exception e) {
                try {
                    throw (e);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                mProgressDialog.dismiss();
                running = false;
            }
            return null;

        }

        @Override
        protected void onPostExecute(String result) {
            if (mProgressDialog != null && mProgressDialog.isShowing())
                mProgressDialog.dismiss();
            if (running) {

                if (inputciflist.equalsIgnoreCase("0")) {
                    Toast.makeText(getApplicationContext(),
                            "PF No. does not exist", Toast.LENGTH_SHORT).show();
                } else if (inputciflist.equalsIgnoreCase("1")) {
                    Toast.makeText(getApplicationContext(),
                            "PF No. already registered", Toast.LENGTH_SHORT)
                            .show();
                }

                //	else if (inputciflist != null && db.CheckIsDataAlreadyInDBorNot(str_pf_number)==0) {
                else if (inputciflist != null && !getValueFromDatabase()) {
                    if (!str_state_id.equalsIgnoreCase("0")) {

                        if (mCommonMethods.isNetworkConnected(CIFEnrollmentPFActivity.this)) {
                            GetCIFExamDetails();
                        } else {
                            Toast.makeText(getApplicationContext(),
                                    "No internet Connection",
                                    Toast.LENGTH_SHORT).show();

                        }
                    } else {
                        inputciflist_exam = "0";
                    }
                    String zero = "0000";
                    quotation_Number = getquotationNumber25("demo", zero);

                    getInput();

                    // M_MainActivity_Data data = new M_MainActivity_Data(
                    // quotation_Number, str_pf_number);

                    M_MainActivity_Data data = new M_MainActivity_Data(
                            quotation_Number, str_pf_number, new String(
                            inputVal));

                    try {
                        long count = db.insertCIFDetail_New(data,
                                quotation_Number);
                        if (count > 0) {
                            // Toast toast = Toast.makeText(
                            // getApplicationContext(),
                            // "Data Inserted Successfully",
                            // Toast.LENGTH_SHORT);
                            // toast.show();
                        }
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    M_UserInformation DashboardDetail_data = new M_UserInformation(
                            quotation_Number, str_pf_number);
                    try {
                        long rowId2 = db
                                .insertDashBoardDetail(DashboardDetail_data);
                        if (rowId2 > 0) {
                            // Toast toast = Toast.makeText(
                            // getApplicationContext(),
                            // "Data Inserted Successfully",
                            // Toast.LENGTH_SHORT);
                            // toast.show();
                        }
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    M_ExamDetails exam_center_data = new M_ExamDetails(
                            quotation_Number, inputciflist_exam);
                    try {
                        long rowId2 = db.insertExamDetail(exam_center_data);
                        if (rowId2 > 0) {
                            // Toast toast = Toast.makeText(
                            // getApplicationContext(),
                            // "Data Inserted Successfully",
                            // Toast.LENGTH_SHORT);
                            // toast.show();
                        }
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    Toast.makeText(getApplicationContext(),
                            "Login Successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(CIFEnrollmentPFActivity.this,
                            CIFEnrollmentMainActivity.class);
                    startActivity(intent);
                } else {
                    quotation_Number = quotation_num;

                    Intent intent = new Intent(CIFEnrollmentPFActivity.this,
                            CIFEnrollmentMainActivity.class);
                    startActivity(intent);
                }
//				else {
//					Toast.makeText(getApplicationContext(),
//							"Please Enter Valid PF Number", Toast.LENGTH_SHORT)
//							.show();
//				}
            } else {
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Server not responding", Toast.LENGTH_SHORT);
                toast.show();
            }
        }

    }

    class AsyncDownloadCIFExam extends AsyncTask<String, Void, String> {
        private volatile boolean running = true;

        @Override
        protected String doInBackground(String... param) {
            try {
                //String CIFBirthdate = "";
                running = true;

                SoapObject request = new SoapObject(NAMESPACE,
                        METHOD_NAME_CIF_EXAM);

                request.addProperty("strStateID", str_state_id);

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);
                envelope.dotNet = true;

                envelope.setOutputSoapObject(request);

                //allowAllSSL();
                mCommonMethods.TLSv12Enable();

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);

                HttpTransportSE androidHttpTranport = new HttpTransportSE(ServiceURL.SERVICE_URL);
                try {

                    androidHttpTranport.call(NAMESPACE + METHOD_NAME_CIF_EXAM, envelope);
                    //Object response = envelope.getResponse();

                    SoapPrimitive sa = null;
                    try {
                        sa = (SoapPrimitive) envelope.getResponse();

                        inputciflist_exam = sa.toString();

                        if (!sa.toString().equalsIgnoreCase("")) {

                            ParseXML prsObj = new ParseXML();
                            inputciflist_exam = prsObj.parseXmlTag(
                                    inputciflist_exam, "NewDataSet");

                            if (inputciflist_exam == null) {
                                inputciflist_exam = "0";
                            }

                        }

                    } catch (Exception e) {
                        try {
                            throw (e);
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                        mProgressDialog.dismiss();
                        running = false;
                    }

                } catch (IOException e) {
                    try {
                        throw (e);
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                    mProgressDialog.dismiss();
                    running = false;
                } catch (XmlPullParserException e) {
                    try {
                        throw (e);
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                    mProgressDialog.dismiss();
                    running = false;
                }
            } catch (Exception e) {
                try {
                    throw (e);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                mProgressDialog.dismiss();
                running = false;
            }
            return null;

        }

        @Override
        protected void onPostExecute(String result) {
            if (mProgressDialog.isShowing())
                mProgressDialog.dismiss();
            if (running) {

            } else {
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Server not responding", Toast.LENGTH_SHORT);
                toast.show();
            }
        }

    }

    private static String getquotationNumber25(String username, String Zero) {

        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());
        SimpleDateFormat s = new SimpleDateFormat("ddMMyyyyhhmmss");
        formattedDate = s.format(c.getTime());
        // max++;
        String quotationNumber = "";

        quotationNumber = "CIF" + Zero + username + formattedDate;

        return quotationNumber;
    }
}