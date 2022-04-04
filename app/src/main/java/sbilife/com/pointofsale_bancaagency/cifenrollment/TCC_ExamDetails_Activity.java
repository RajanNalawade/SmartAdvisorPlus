package sbilife.com.pointofsale_bancaagency.cifenrollment;

import androidx.appcompat.app.AppCompatActivity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.MarshalBase64;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
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
import sbilife.com.pointofsale_bancaagency.cifenrollment.phase1.CIFEnrollmentPFActivity;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;

public class TCC_ExamDetails_Activity extends AppCompatActivity implements View.OnClickListener{

    private Context mContext;
    private CommonMethods mCommonMethods;
    private DatabaseHelper db;

    private final String FILE_NAME = "SCORE_CARD";

    private final String NAMESPACE = "http://tempuri.org/";
    private final String METHOD_NAME_CIF_EXAM = "getCIFExamDetail";
    private final String METHOD_NAME_UPLOAD_ALL_DETAILS = "saveExamDetail_URN_CIFenroll";

    private  final int DIALOG_EXAM_PREFFERED_DATE = 100;

    private String str_exam_location = "";
    private String str_exam_center = "";
    private String str_urn_no = "";
    private String str_exam_date = "";

    private int mDOBDay = 0, mDOBYear = 0, mDOBMonth = 0;
    private static Date currentDate;

    private ArrayList<String> lstLocation = new ArrayList<>();
    private ArrayList<String> lstCenter = new ArrayList<>();

    private ArrayList<String> lstLocationID = new ArrayList<>();

    private Spinner spnr_tcc_exam_center, spnr_tcc_exam_location;
    private static TextView txt_tcc_exam_existing_center;
    private static TextView txt_tcc_exam_preffered_date;
    private LinearLayout ll_exam_location;
    private ParseXML mParse;

    private ProgressDialog mProgressDialog;

    private AsyncGetExamCenter mAsyncGetExamCenter;

    private boolean is_dashboard = false;

    private ArrayAdapter<String> exam_center_adapter;

    private static Calendar cal;

    private ArrayList<TCC_ExamDetails> lstTCC = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.fragment_tcc_exam_details);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.cifenrollment_pf_activity_title);

        initialisation();
    }

    private void initialisation(){

        mContext = this;
        mCommonMethods = new CommonMethods();
        db = new DatabaseHelper(mContext);

        mParse = new ParseXML();

        mCommonMethods.setApplicationToolbarMenu1(this, "CIF on Boarding");

        str_urn_no = getIntent().getStringExtra("URN");
        is_dashboard = getIntent().getBooleanExtra("DASHBOARD", false);

        if (!is_dashboard){
            String str_urn_bundle = getIntent().getStringExtra("EXAM_LOCATION_DETAILS");
            str_exam_location = getIntent().getStringExtra("DEFAULT_EXAM_LOCATION");

            str_urn_bundle = mParse.parseXmlTag(str_urn_bundle, "NewDataSet");

            if (str_urn_bundle == null){

                mCommonMethods.showToast(mContext, "URN dont have any details");

                Intent i = new Intent(mContext, CIF_TCC_UploadActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                finish();
            }else{
                List<String> mData = mParse.parseParentNode(str_urn_bundle, "Table");

                if (mData.size() > 0){

                    lstLocation.add("Select Exam Location");
                    lstLocationID.add("Select Exam Location");

                    for (String strXMl: mData){
                        lstLocation.add(mParse.parseXmlTag(strXMl, "CEC_STATE_NAME"));
                        lstLocationID.add(mParse.parseXmlTag(strXMl, "CEC_STATE_ID"));
                    }
                }else {
                    mCommonMethods.showToast(mContext, "URN dont have any details");

                    Intent i = new Intent(mContext, CIF_TCC_UploadActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                    finish();
                }
            }
        }

        spnr_tcc_exam_center = findViewById(R.id.spnr_tcc_exam_center);
        spnr_tcc_exam_location = findViewById(R.id.spnr_tcc_exam_location);
        txt_tcc_exam_existing_center = findViewById(R.id.txt_tcc_exam_existing_center);
        txt_tcc_exam_preffered_date = findViewById(R.id.txt_tcc_exam_preffered_date);
        txt_tcc_exam_preffered_date.setOnClickListener(this);
        Button btn_tcc_exam_details_submit = findViewById(R.id.btn_tcc_exam_details_submit);
        btn_tcc_exam_details_submit.setOnClickListener(this);

        ll_exam_location = findViewById(R.id.ll_exam_location);

        ArrayAdapter<String> exam_location_adapter = new ArrayAdapter<String>(mContext, R.layout.spinner_item, lstLocation);
        exam_location_adapter.setDropDownViewResource(R.layout.spinner_item1);
        spnr_tcc_exam_location.setAdapter(exam_location_adapter);
        exam_location_adapter.notifyDataSetChanged();

        if (!str_exam_location.equals("")){
            txt_tcc_exam_existing_center.setText(str_exam_location);
        }

        spnr_tcc_exam_center.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                str_exam_center = spnr_tcc_exam_center.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        cal = Calendar.getInstance();
        mDOBYear = cal.get(Calendar.YEAR);
        mDOBMonth = cal.get(Calendar.MONTH);
        mDOBDay = cal.get(Calendar.DAY_OF_MONTH);

        String mont = ((mDOBMonth + 1) < 10 ? "0" : "") + (mDOBMonth + 1);
        String day = (mDOBDay < 10 ? "0" : "") + mDOBDay;

        currentDate = new Date(cal.getTimeInMillis());

        txt_tcc_exam_preffered_date.setText(day+"-"+mont+"-"+cal.get(Calendar.YEAR));

        spnr_tcc_exam_location.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position != 0){

                    //get exam center from server by passing ID
                    mAsyncGetExamCenter = new AsyncGetExamCenter();
                    mAsyncGetExamCenter.execute(lstLocationID.get(position));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mCommonMethods.hideKeyboard(txt_tcc_exam_preffered_date, mContext);

        //if dashboard is true the get data from database against URN and all fields are not editable
        if (is_dashboard){

            lstTCC = db.getTCCAllDetails(str_urn_no);

            //set Data
            setTCCExamData();
        }
    }

    private void setTCCExamData(){

        txt_tcc_exam_existing_center.setText(lstTCC.get(0).getStr_existing_center());
        txt_tcc_exam_existing_center.setEnabled(false);

        ll_exam_location.setVisibility(View.GONE);

        lstCenter = new ArrayList<>();
        lstCenter.add(lstTCC.get(0).getStr_centre());

        exam_center_adapter = new ArrayAdapter<String>(mContext, R.layout.spinner_item, lstCenter);
        exam_center_adapter.setDropDownViewResource(R.layout.spinner_item1);
        spnr_tcc_exam_center.setAdapter(exam_center_adapter);
        exam_center_adapter.notifyDataSetChanged();

        spnr_tcc_exam_center.setEnabled(false);

        txt_tcc_exam_preffered_date.setText(lstTCC.get(0).getStr_preferred_date());
        txt_tcc_exam_preffered_date.setEnabled(false);
    }

    @Override
    public void onPause() {
        super.onPause();

        if (mAsyncGetExamCenter != null)
            mAsyncGetExamCenter.cancel(true);
    }

    private void onButtonSubmit(){

        String strError = validateAllDetails();
        if (strError.equals("")){

            /*if (db.getTCCAllDetails(str_urn_no).size() > 0){
                ContentValues cv = new ContentValues();
                cv.put(DatabaseHelper.TCC_EXAM_DETAILS_URN_NUMBER, str_urn_no);
                cv.put(DatabaseHelper.TCC_EXAM_DETAILS_EXAM_LOCATION, spnr_tcc_exam_location.getSelectedItem().toString());
                cv.put(DatabaseHelper.TCC_EXAM_DETAILS_EXAM_CENTER, spnr_tcc_exam_center.getSelectedItem().toString());
                cv.put(DatabaseHelper.TCC_EXAM_DETAILS_PREFFERED_DATE, txt_tcc_exam_preffered_date.getText().toString());
                //update data with synch status 1 for exam details
                cv.put(DatabaseHelper.TCC_EXAM_DETAILS_SYNCH_STATUS, "1");

                db.insert_tcc_and_exam_details(cv);
            }else{*/

            ContentValues cv = new ContentValues();
            cv.put(db.TCC_EXAM_DETAILS_EXAM_EXISTING_CENTER, txt_tcc_exam_existing_center.getText().toString());
            cv.put(db.TCC_EXAM_DETAILS_EXAM_CENTER, spnr_tcc_exam_center.getSelectedItem().toString());
            cv.put(db.TCC_EXAM_DETAILS_PREFFERED_DATE, txt_tcc_exam_preffered_date.getText().toString());
            //update data with synch status 2 for exam details
            cv.put(db.TCC_EXAM_DETAILS_SYNCH_STATUS, "2");

            db.update_tcc_exam_details(cv, db.TCC_EXAM_DETAILS_URN_NUMBER + " =? ",
                    new String[]{str_urn_no});
            //}

            lstTCC = db.getTCCAllDetails(str_urn_no);

            new UploadALLDetails().execute();

        }else{
            mCommonMethods.showMessageDialog(mContext, strError);
        }
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id){

            case DIALOG_EXAM_PREFFERED_DATE:
                return new DatePickerDialog(this, R.style.datepickerstyle, mDateListenerPreffered, mDOBYear,
                        mDOBMonth, mDOBDay);

            default:
                return null;

        }
    }

    private DatePickerDialog.OnDateSetListener mDateListenerPreffered = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

            String strSelectedDate = (dayOfMonth < 10 ? "0" : "") + dayOfMonth + "-"
                    + ((monthOfYear + 1) < 10 ? "0" : "") + (monthOfYear + 1) + "-"
                    + year;
            try{
                SimpleDateFormat sdp = new SimpleDateFormat("dd-MM-yyyy");
                Date selectedDate = sdp.parse(strSelectedDate);

                if (!selectedDate.before(currentDate)){
                    txt_tcc_exam_preffered_date.setText(strSelectedDate);
                }else{
                    mCommonMethods.showMessageDialog(mContext, "back date not allowed");
                }
            }catch (ParseException e){
                e.printStackTrace();
                mCommonMethods.showToast(mContext, "parse error \n"+e.getMessage());
            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_tcc_exam_details_submit:

                if (is_dashboard){
                    //navigate to dashboard
                    startActivity(new Intent(TCC_ExamDetails_Activity.this, CIF_TCC_DashboardActivity.class));
                }else{
                    onButtonSubmit();
                }
                break;

            case R.id.txt_tcc_exam_preffered_date:
                showDialog(DIALOG_EXAM_PREFFERED_DATE);
                break;

            default:
                break;
        }
    }

    private String validateAllDetails() {

        if (spnr_tcc_exam_location.getSelectedItem().toString().equals("Select Exam Location")){
            return "Please Select Exam Location";
        }else if (spnr_tcc_exam_center.getSelectedItem().toString().equals("Select Exam Center")){
            return "Please Select Exam Center";
        }else if (txt_tcc_exam_preffered_date.equals("")){
            return "Please Select Preffered Date";
        }else
            return "";
    }

    class AsyncGetExamCenter extends AsyncTask<String, Void, String> {
        private volatile boolean running = true;
        private String strResult = "";

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
        protected String doInBackground(String... strID) {
            try {
                //String CIFBirthdate = "";
                running = true;

                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME_CIF_EXAM);

                request.addProperty("strStateID", strID[0]);

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

                        strResult = sa.toString();

                        if (!strResult.equalsIgnoreCase("")) {

                            ParseXML prsObj = new ParseXML();

                            if (prsObj.parseXmlTag(strResult, "NewDataSet") == null) {
                                strResult = "0";
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

                //update list of exam center
                if (!strResult.equals("0")){

                    strResult = mParse.parseXmlTag(strResult, "NewDataSet");

                    if (strResult != null){
                        List<String> mData = mParse.parseParentNode(strResult, "Table");

                        if (mData.size() > 0){

                            lstCenter = new ArrayList<>();

                            lstCenter.add("Select Exam Center");
                            for (String strXMl: mData){
                                lstCenter.add(mParse.parseXmlTag(strXMl, "CEC_EXAMCENTER_NAME"));
                            }

                            exam_center_adapter = new ArrayAdapter<String>(mContext, R.layout.spinner_item, lstCenter);
                            exam_center_adapter.setDropDownViewResource(R.layout.spinner_item1);
                            spnr_tcc_exam_center.setAdapter(exam_center_adapter);
                            exam_center_adapter.notifyDataSetChanged();

                        }else {
                            mCommonMethods.showMessageDialog(mContext, "Exam Center not found contact admin");
                        }
                    }else {
                        mCommonMethods.showMessageDialog(mContext, "Exam Center not found contact admin");
                    }

                }else {
                    mCommonMethods.showMessageDialog(mContext, "Exam Center not found contact admin");
                }
            } else {
                mCommonMethods.showToast(mContext, "Server not responding");
            }
        }
    }

    class UploadALLDetails extends AsyncTask<String, String, String>{

        private volatile boolean running = true;
        private String str_output_result = "";

        @Override
        protected void onPreExecute() {
            mProgressDialog = new ProgressDialog(mContext,
                    ProgressDialog.THEME_HOLO_LIGHT);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.setTitle(Html
                            .fromHtml("<font color='#00a1e3'><b>Please Wait...<b></font>"));
            mProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            if (mCommonMethods.isNetworkConnected(mContext)){
                try{

                    running = true;

                    SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME_UPLOAD_ALL_DETAILS);

                    request.addProperty("URN", str_urn_no);

                    if (lstTCC.size() > 0){
                        request.addProperty("Exame_Center_existing", "NSEiT Limited - " + lstTCC.get(0).getStr_existing_center());
                        request.addProperty("Exame_Center_new", "NSEiT Limited - " + lstTCC.get(0).getStr_centre());

                        //convert date to mm-dd-yyyy from 10-06-2018
                        String[] dateArr = lstTCC.get(0).getStr_preferred_date().split("-");
                        if (dateArr != null)
                            request.addProperty("Exame_Center_Date", dateArr[1]+"-"+dateArr[0]+"-"+dateArr[2]);
                        else
                            request.addProperty("Exame_Center_Date", "");//send default date

                        request.addProperty("TRAINING_HOURS_COMPLETED", lstTCC.get(0).getStr_no_training_hrs());

                        //convert date to mm-dd-yyyy from 10-06-2018
                        dateArr = lstTCC.get(0).getStr_start_date().split("-");
                        if (dateArr != null)
                            request.addProperty("TRAINING_START_DATE", dateArr[1]+"-"+dateArr[0]+"-"+dateArr[2]);
                        else
                            request.addProperty("TRAINING_START_DATE", "");//send default date

                        dateArr = lstTCC.get(0).getStr_end_date().split("-");
                        if (dateArr != null)
                            request.addProperty("TRAINING_END_DATE", dateArr[1]+"-"+dateArr[0]+"-"+dateArr[2]);
                        else
                            request.addProperty("TRAINING_END_DATE", "");//send default date

                    }else {
                        request.addProperty("Exame_Center_existing", "");
                        request.addProperty("Exame_Center_new", "");
                        request.addProperty("Exame_Center_Date", "");
                        request.addProperty("TRAINING_HOURS_COMPLETED", "");
                        request.addProperty("TRAINING_START_DATE", "");
                        request.addProperty("TRAINING_END_DATE", "");
                    }

                    mCommonMethods.TLSv12Enable();

                    SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                    envelope.dotNet = true;

                    new MarshalBase64().register(envelope); // serialization

                    envelope.setOutputSoapObject(request);

                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);

                    String URL = ServiceURL.SERVICE_URL;
                    HttpTransportSE androidHttpTranport = new HttpTransportSE(URL, 50000);
                    try {

                        androidHttpTranport.call(NAMESPACE + METHOD_NAME_UPLOAD_ALL_DETAILS, envelope);
                        Object response = envelope.getResponse();
                        str_output_result = response.toString();

                        // SoapPrimitive sa = (SoapPrimitive)
                        // envelope.getResponse();
                    } catch (Exception e) {
                        e.printStackTrace();
                        running = false;
                        return mCommonMethods.WEEK_INTERNET_MESSAGE;
                    }

                }catch (Exception e){
                    e.printStackTrace();
                    running = false;
                    return mCommonMethods.WEEK_INTERNET_MESSAGE;
                }
            }else{
                return mCommonMethods.NO_INTERNET_MESSAGE;
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {

            if (mProgressDialog.isShowing())
                mProgressDialog.dismiss();

            if (running) {

                if (str_output_result.equals("1")){
                    running = false;

                    //update data with synch status 3 for sunch successfully
                    ContentValues cv = new ContentValues();
                    cv.put(db.TCC_EXAM_DETAILS_SYNCH_STATUS, "3");

                    db.update_tcc_exam_details(cv, db.TCC_EXAM_DETAILS_URN_NUMBER + " =? ",
                            new String[]{str_urn_no});

                    showMessageDialog(mContext,
                            "1.Exam Details have been uploaded successfully.\n"
                                    + "2.Exam Batch ID shall be informed shortly.");

                }else{
                    Toast.makeText(mContext, "PLease try agian later..",
                            Toast.LENGTH_SHORT).show();
                }

            }else{
                Toast.makeText(mContext, s,
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    public class TCC_ExamDetails{
        private String strURN = "", str_existing_center = "", str_centre = "", str_preferred_date = "",
                str_sync_status = "", str_no_training_hrs = "", str_start_date = "", str_end_date = "";

        public TCC_ExamDetails(String strURN, String str_existing_center, String str_centre, String str_preferred_date,
                               String str_sync_status, String str_no_training_hrs, String str_start_date,
                               String str_end_date) {
            this.strURN = strURN;
            this.str_existing_center = str_existing_center;
            this.str_centre = str_centre;
            this.str_preferred_date = str_preferred_date;
            this.str_sync_status = str_sync_status;
            this.str_no_training_hrs = str_no_training_hrs;
            this.str_start_date = str_start_date;
            this.str_end_date = str_end_date;
        }

        public String getStr_existing_center() {
            return str_existing_center;
        }

        public void setStr_existing_center(String str_existing_center) {
            this.str_existing_center = str_existing_center;
        }

        public String getStrURN() {
            return strURN;
        }

        public void setStrURN(String strURN) {
            this.strURN = strURN;
        }

        public String getStr_centre() {
            return str_centre;
        }

        public void setStr_centre(String str_centre) {
            this.str_centre = str_centre;
        }

        public String getStr_preferred_date() {
            return str_preferred_date;
        }

        public void setStr_preferred_date(String str_preferred_date) {
            this.str_preferred_date = str_preferred_date;
        }

        public String getStr_sync_status() {
            return str_sync_status;
        }

        public void setStr_sync_status(String str_sync_status) {
            this.str_sync_status = str_sync_status;
        }

        public String getStr_no_training_hrs() {
            return str_no_training_hrs;
        }

        public void setStr_no_training_hrs(String str_no_training_hrs) {
            this.str_no_training_hrs = str_no_training_hrs;
        }

        public String getStr_start_date() {
            return str_start_date;
        }

        public void setStr_start_date(String str_start_date) {
            this.str_start_date = str_start_date;
        }

        public String getStr_end_date() {
            return str_end_date;
        }

        public void setStr_end_date(String str_end_date) {
            this.str_end_date = str_end_date;
        }
    }

    private void showMessageDialog(Context context, String message) {
        try {
            final Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_with_ok_button);
            dialog.setCancelable(false);
            TextView text = dialog.findViewById(R.id.tv_title);
            text.setText(message);
            Button dialogButton = dialog.findViewById(R.id.bt_ok);
            dialogButton.setText("Ok");
            dialogButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    dialog.dismiss();

                    /*call TCC main Activity activity*/
                    Intent intent = new Intent(mContext, CIFEnrollmentPFActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            });
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
