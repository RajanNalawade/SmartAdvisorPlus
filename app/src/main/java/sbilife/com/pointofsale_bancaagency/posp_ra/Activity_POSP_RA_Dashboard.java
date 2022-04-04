package sbilife.com.pointofsale_bancaagency.posp_ra;

import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import sbilife.com.pointofsale_bancaagency.DatabaseHelper;
import sbilife.com.pointofsale_bancaagency.ParseXML;
import sbilife.com.pointofsale_bancaagency.R;
import sbilife.com.pointofsale_bancaagency.ServiceURL;
import sbilife.com.pointofsale_bancaagency.agent_on_boarding.ActivityAOBDashboard;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;
import sbilife.com.pointofsale_bancaagency.common.StorageUtils;
import sbilife.com.pointofsale_bancaagency.ekyc.utilites.Base64;

public class Activity_POSP_RA_Dashboard extends AppCompatActivity implements
        AdapterView.OnItemLongClickListener, View.OnClickListener {

    private final String NAMESPACE = "http://tempuri.org/";
    private final String METHOD_NAME_SAVE_POSP_RA_DETAILS = "saveAgentOnboardingDetail_other";
    private final String METHOD_NAME_GET_PAN_REJECTION_STATUS = "getAgentPanStatus_smrt";
    private final String METHOD_NAME_DOWNLOAD_AGENT_FORM = "getAgentForm";
    private Context mContext;
    private CommonMethods mCommonMethods;
    private DatabaseHelper db;
    private ListView lv_aob_dashboard;
    private TextView txt_aob_dashboard_no_data;
    private EditText edt_agent_posp_form_pan;
    private ImageButton btn_agent_posp_form_download;

    private ArrayList<Pojo_POSP_RA> lstAOBDashboardDetails = new ArrayList<>();
    private AdapterAOBDashboard mAdapterAOBDashboard;
    private ProgressDialog mProgressDialog;
    private StringBuilder str_final_posp_info;
    private AsyncAllAOB mAsyncAllAOB;
    private File filePOSP_RAForm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_posp_ra_dashboard);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.cifenrollment_pf_activity_title);

        initialisation();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(Activity_POSP_RA_Dashboard.this, Activity_POSP_RA_Authentication.class));
    }

    public void initialisation() {

        mContext = this;
        mCommonMethods = new CommonMethods();
        mCommonMethods.setApplicationToolbarMenu1(this, "POSP-RA");

        db = new DatabaseHelper(mContext);

        lv_aob_dashboard = findViewById(R.id.lv_aob_dashboard);
        lv_aob_dashboard.setOnItemLongClickListener(this);

        txt_aob_dashboard_no_data = findViewById(R.id.txt_aob_dashboard_no_data);

        edt_agent_posp_form_pan = findViewById(R.id.edt_agent_posp_form_pan);
        btn_agent_posp_form_download = findViewById(R.id.btn_agent_posp_form_download);
        btn_agent_posp_form_download.setOnClickListener(this);

        //set Data from DB
        lstAOBDashboardDetails = db.get_posp_ra_dashboard_details_by_ID();

        if (lstAOBDashboardDetails.size() > 0) {

            txt_aob_dashboard_no_data.setVisibility(View.GONE);
            lv_aob_dashboard.setVisibility(View.VISIBLE);

            mAdapterAOBDashboard = new AdapterAOBDashboard(mContext, lstAOBDashboardDetails);
            lv_aob_dashboard.setAdapter(mAdapterAOBDashboard);

        } else {
            txt_aob_dashboard_no_data.setVisibility(View.VISIBLE);
            lv_aob_dashboard.setVisibility(View.GONE);
        }

        str_final_posp_info = new StringBuilder();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

        if (lstAOBDashboardDetails.get(position).getStr_id().equals(""))
            Activity_POSP_RA_Authentication.row_details = 0;
        else {
            Activity_POSP_RA_Authentication.row_details = Integer.parseInt(lstAOBDashboardDetails.get(position).getStr_id());
        }

        //for synch failure then synch it only for data not for pdf doc
        if (lstAOBDashboardDetails.get(position).getStr_in_app_status().equals("13")) {

            //get all data from db
            ArrayList<Pojo_POSP_RA> lstRes = db.get_posp_ra_details_by_ID(Activity_POSP_RA_Authentication.row_details);
            if (lstRes.size() > 0) {

                str_final_posp_info = new StringBuilder();

                //create final POSP-RA string to synch
                //create final POSP-RA string to synch
                str_final_posp_info.append("<?xml version='1.0' encoding='utf-8' ?><POSP_RA>");
                //str_final_aob_info.append(lstRes.get(0).getStr_basic_details().toString());//basic details
                //personal info
                str_final_posp_info.append(lstRes.get(0).getStr_personal_info());
                //occupational info
                str_final_posp_info.append(lstRes.get(0).getStr_occupation_info());

                //nominational info
                //added 19-01-2021
                String str_nominee_info = lstRes.get(0).getStr_nomination_info();
                String str_nominee_info_nom_address2 = mCommonMethods.getSubStringByString(str_nominee_info, "nominee_info_nom_address2");
                str_nominee_info = str_nominee_info.replace(str_nominee_info_nom_address2, "");
                String str_nominee_info_nom_address3 = mCommonMethods.getSubStringByString(str_nominee_info, "nominee_info_nom_address3");
                str_nominee_info = str_nominee_info.replace(str_nominee_info_nom_address3, "");
                String str_nominee_info_appointee_address2 = mCommonMethods.getSubStringByString(str_nominee_info, "nominee_info_appointee_address2");
                str_nominee_info = str_nominee_info.replace(str_nominee_info_appointee_address2, "");
                String str_nominee_info_appointee_address3 = mCommonMethods.getSubStringByString(str_nominee_info, "nominee_info_appointee_address3");
                str_nominee_info = str_nominee_info.replace(str_nominee_info_appointee_address3, "");
                str_final_posp_info.append(str_nominee_info);
                //ended 19-01-2021

                //bank details info
                str_final_posp_info.append(lstRes.get(0).getStr_bank_details());

                //form 1-a info
                str_final_posp_info.append(new Pojo_POSP_RA().get_form1a_xml());

                //Exam and Training details
                String str_exam_training_info = lstRes.get(0).getStr_exam_training_details();
                String str_training_details_language = mCommonMethods.getSubStringByString(str_exam_training_info, "training_details_language");
                str_exam_training_info = str_exam_training_info.replace(str_training_details_language, "");
                String str_exam_details_exam_date = mCommonMethods.getSubStringByString(str_exam_training_info, "exam_details_exam_date");
                str_exam_training_info = str_exam_training_info.replace(str_exam_details_exam_date, "");
                String str_training_details_training_mode = mCommonMethods.getSubStringByString(str_exam_training_info, "training_details_training_mode");
                str_exam_training_info = str_exam_training_info.replace(str_training_details_training_mode, "");
                String str_training_details_required_hrs = mCommonMethods.getSubStringByString(str_exam_training_info, "training_details_required_hrs");
                str_exam_training_info = str_exam_training_info.replace(str_training_details_required_hrs, "");
                String str_training_details_institute_name = mCommonMethods.getSubStringByString(str_exam_training_info, "training_details_institute_name");
                str_exam_training_info = str_exam_training_info.replace(str_training_details_institute_name, "");
                String str_exam_details_exam_mode = mCommonMethods.getSubStringByString(str_exam_training_info, "exam_details_exam_mode");
                str_exam_training_info = str_exam_training_info.replace(str_exam_details_exam_mode, "");
                String str_exam_details_exam_body = mCommonMethods.getSubStringByString(str_exam_training_info, "exam_details_exam_body");
                str_exam_training_info = str_exam_training_info.replace(str_exam_details_exam_body, "");
                String str_exam_details_marks_obtained = mCommonMethods.getSubStringByString(str_exam_training_info, "exam_details_marks_obtained");
                str_exam_training_info = str_exam_training_info.replace(str_exam_details_marks_obtained, "");
                String str_exam_details_exam_status = mCommonMethods.getSubStringByString(str_exam_training_info, "exam_details_exam_status");
                str_exam_training_info = str_exam_training_info.replace(str_exam_details_exam_status, "");
                str_final_posp_info.append(str_exam_training_info);

                //BSM interview questions
                str_final_posp_info.append(new Pojo_POSP_RA().getBSMQuestions());
                //str_final_aob_info.append(lstRes.get(0).getStr_declarations_conditions().toString());//terms and conditions

                str_final_posp_info.append("<um_code>" + mCommonMethods.GetUserCode(mContext) + "</um_code>");

                SimpleDateFormat sdp = new SimpleDateFormat("MM-dd-yyyy");

                String str_created_date = "";
                try {
                    str_created_date = sdp.format(new Date(Long.valueOf(lstRes.get(0).getStr_created_date())));
                } catch (Exception ex) {
                    str_created_date = lstRes.get(0).getStr_created_date();
                }
                str_final_posp_info.append("<created_date>" + str_created_date + "</created_date>");//created_date

                //added 19-01-2021 nominational info extra fields
                str_final_posp_info.append(str_nominee_info_nom_address2 + str_nominee_info_nom_address3
                        + str_nominee_info_appointee_address2 + str_nominee_info_appointee_address3);
                //ended 19-01-2021

                //added 25-05-2021 agency type
                str_final_posp_info.append("<agency_type>").append(mCommonMethods.str_posp_ra_customer_type).append("</agency_type>");
                //ended 25-05-2021

                // exam and training details extrafields
                str_final_posp_info.append(
                        str_training_details_language + str_exam_details_exam_date
                                + str_training_details_training_mode + str_training_details_required_hrs
                                + str_training_details_institute_name + str_exam_details_exam_mode
                                + str_exam_details_exam_body + str_exam_details_marks_obtained
                                + str_exam_details_exam_status);

                String str_irn = lstRes.get(0).getStr_irn();
                str_irn = str_irn == null ? "" : str_irn;

                str_final_posp_info.append("<posp_ra_irn>").append(str_irn).append("</posp_ra_irn>");
                str_final_posp_info.append("<sub_type>").append("New").append("</sub_type>");

                str_final_posp_info.append("</POSP_RA>");

                mAsyncAllAOB = new AsyncAllAOB();
                mAsyncAllAOB.execute();

            } else {
                mCommonMethods.showMessageDialog(mContext, "Data Synch Failed");
            }

        } else {
            Intent mIntent = new Intent(Activity_POSP_RA_Dashboard.this, Activity_POSP_RA_PersonalInfo.class);
            mIntent.putExtra("is_dashboard", true);
            startActivity(mIntent);

            //new Async_Get_PAN_Rejection_Status().execute(lstAOBDashboardDetails.get(position).getStr_pan_no());
        }

        return false;
    }

    public class AdapterAOBDashboard extends BaseAdapter {

        private final Context mAdptrContext;
        private ArrayList<Pojo_POSP_RA> lstAdapterAOBDetails = new ArrayList<>();

        public AdapterAOBDashboard(Context mAdptrContext, ArrayList<Pojo_POSP_RA> lstAdapterAOBDetails) {
            this.mAdptrContext = mAdptrContext;
            this.lstAdapterAOBDetails = lstAdapterAOBDetails;
        }

        @Override
        public Object getItem(int position) {
            return lstAdapterAOBDetails.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getCount() {
            return lstAdapterAOBDetails.size();
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {

            ViewHolder v;
            if (view == null) {
                LayoutInflater mInflater = LayoutInflater.from(mAdptrContext);
                view = mInflater.inflate(R.layout.row_aob_dashboard, null);

                v = new ViewHolder();

                //v.tv_aob_dashboard_count = view.findViewById(R.id.tv_aob_dashboard_count);
                v.tv_aob_dashboard_name = view.findViewById(R.id.tv_aob_dashboard_name);
                v.tv_aob_dashboard_pan = view.findViewById(R.id.tv_aob_dashboard_pan);
                v.tv_aob_dashboard_synch = view.findViewById(R.id.tv_aob_dashboard_synch);

                view.setTag(v);

            } else {
                v = (ViewHolder) view.getTag();
            }

            //v.tv_aob_dashboard_count.setText((position + 1) + ".");

            String str_personal = lstAdapterAOBDetails.get(position).getStr_personal_info();
            str_personal = str_personal == null ? "" : str_personal;

            ParseXML mParseXML = new ParseXML();

            if (!str_personal.equals("")) {
                v.tv_aob_dashboard_name.setText(mParseXML.parseXmlTag(str_personal, "personal_info_full_name"));
            } else {

                //get PAN details
                String str_pan_details = lstAdapterAOBDetails.get(position).getStr_pan_details();

                str_pan_details = str_pan_details == null ? "" : str_pan_details;

                if (!str_pan_details.equals("")) {

                    str_pan_details = mParseXML.parseXmlTag(str_pan_details,
                            "PANDETAILS");

                    String str_ReturnCode = mParseXML.parseXmlTag(str_pan_details, "RETURNCODE");
                    if (str_ReturnCode.equals("1")) {

                        String strPANTitle = mParseXML.parseXmlTag(str_pan_details, "TITLE");
                        if (strPANTitle == null || strPANTitle.equalsIgnoreCase("NA")) {
                            strPANTitle = "";
                        }

                        String strPANFirstName = mParseXML.parseXmlTag(str_pan_details, "FIRSTNAME");
                        if (strPANFirstName == null || strPANFirstName.equalsIgnoreCase("NA")) {
                            strPANFirstName = "";
                        }

                        String strPANMiddleName = mParseXML.parseXmlTag(str_pan_details, "MIDDLENAME");
                        if (strPANMiddleName == null || strPANMiddleName.equalsIgnoreCase("NA")) {
                            strPANMiddleName = "";
                        }

                        String strPANLastName = mParseXML.parseXmlTag(str_pan_details, "LASTNAME");
                        if (strPANLastName == null || strPANLastName.equalsIgnoreCase("NA")) {
                            strPANLastName = "";
                        }

                        v.tv_aob_dashboard_name.setText(strPANFirstName + " " + strPANMiddleName + " " + strPANLastName);
                    }
                }

            }
            v.tv_aob_dashboard_pan.setText(lstAdapterAOBDetails.get(position).getStr_pan_no());

            String str_status = lstAdapterAOBDetails.get(position).getStr_in_app_status();

            switch (str_status) {

                case "1":
                    v.tv_aob_dashboard_synch.setText("Personal Details");
                    break;

                case "2":
                    v.tv_aob_dashboard_synch.setText("Occupation Details");
                    break;

                case "3":
                    v.tv_aob_dashboard_synch.setText("Nomination Details");
                    break;

                case "4":
                    v.tv_aob_dashboard_synch.setText("Bank Details");
                    break;

                case "5":
                    v.tv_aob_dashboard_synch.setText("Exam And Trainig Details");
                    break;

                case "6":
                    v.tv_aob_dashboard_synch.setText("Terms And Conditions");
                    break;

                case "7":
                    v.tv_aob_dashboard_synch.setText("Agent Form Upload Pending");
                    break;

                case "8":
                    v.tv_aob_dashboard_synch.setText("Agent Photo Upload Pending");
                    break;

                case "9":
                    v.tv_aob_dashboard_synch.setText("Agent Signature Upload Pending");
                    break;
                case "10":
                    v.tv_aob_dashboard_synch.setText("Document Upload Pending");
                    break;
                case "11":
                    v.tv_aob_dashboard_synch.setText("Data sync pending");
                    break;

                case "12":
                    v.tv_aob_dashboard_synch.setText("Success");
                    break;

                case "13":
                    v.tv_aob_dashboard_synch.setText("Not Synched");
                    break;

                case "14":
                    v.tv_aob_dashboard_synch.setText("Already done");
                    break;
            }

            return view;
        }

        class ViewHolder {
            TextView tv_aob_dashboard_count, tv_aob_dashboard_name, tv_aob_dashboard_pan, tv_aob_dashboard_synch;
        }
    }

    public class AsyncAllAOB extends AsyncTask<String, String, String> {

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

            try {
                running = true;

                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME_SAVE_POSP_RA_DETAILS);
                request.addProperty("xmlStr", str_final_posp_info.toString());
                request.addProperty("Type", mCommonMethods.str_posp_ra_customer_type);
                mCommonMethods.TLSv12Enable();

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;

                envelope.setOutputSoapObject(request);

                HttpTransportSE androidHttpTranport = new HttpTransportSE(ServiceURL.SERVICE_URL);
                try {
                    androidHttpTranport.call(NAMESPACE + METHOD_NAME_SAVE_POSP_RA_DETAILS, envelope);
                    //Object response = envelope.getResponse();

                    SoapPrimitive sa = null;
                    try {
                        sa = (SoapPrimitive) envelope.getResponse();

                        return sa.toString();

                    } catch (Exception e) {
                        try {
                            throw (e);
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                        running = false;
                    }

                } catch (IOException e) {
                    try {
                        throw (e);
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                    running = false;
                } catch (XmlPullParserException e) {
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
        protected void onPostExecute(String result) {

            if (mProgressDialog.isShowing())
                mProgressDialog.dismiss();

            if (running) {

                //3. update data against global row id
                ContentValues cv = new ContentValues();
                cv.put(db.POSP_RA_UPDATED_BY, mCommonMethods.GetUserCode(mContext));

                Calendar c = Calendar.getInstance();
                //save date in long
                cv.put(db.POSP_RA_UPDATED_DATE, new Date(c.getTimeInMillis()).getTime() + "");

                String str_msg = "";
                if (result.equals("1")) {
                    cv.put(db.POSP_RA_IN_APP_STATUS, "14");//for successfull synch
                    cv.put(db.POSP_RA_IN_APP_STATUS_REMARK, "Data Sync Completed");
                    str_msg = "Data synch Successfully..";
                } else if (result.equals("0")) {
                    cv.put(db.POSP_RA_IN_APP_STATUS, "15");//for synch failure
                    cv.put(db.POSP_RA_IN_APP_STATUS_REMARK, "Data Sync Failure, Sync from Dashboard menu");
                } else if (result.equals("2")) {
                    cv.put(db.POSP_RA_IN_APP_STATUS, "16");//for data already exist in server
                    cv.put(db.POSP_RA_IN_APP_STATUS_REMARK, "Data already exists in server");
                    str_msg = "Applicant data already exists in server";
                }

                int i = db.update_POSP_RA_details(cv, db.POSP_RA_ID + " =? ",
                        new String[]{Activity_POSP_RA_Authentication.row_details + ""});

                Activity_POSP_RA_Authentication.row_details = 0;

                str_final_posp_info = new StringBuilder();

                mCommonMethods.showMessageDialog(mContext, str_msg);

            } else {
                mCommonMethods.showMessageDialog(mContext, mCommonMethods.WEEK_INTERNET_MESSAGE);
            }

        }
    }

    class Async_Get_PAN_Rejection_Status extends AsyncTask<String, String, String> {

        private volatile boolean running = true;
        private List<ParseXML.Pojo_POSP_RA_Rejection> lstRejectionData = new ArrayList<>();
        private String strPan = "";

        @Override
        protected void onPreExecute() {
            mProgressDialog = new ProgressDialog(mContext, ProgressDialog.THEME_HOLO_LIGHT);
            mProgressDialog.setMessage(Html.fromHtml("<font color='#00a1e3'><b>Please wait...<b></font>"));
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... input) {

            if (mCommonMethods.isNetworkConnected(mContext)) {

                try {
                    running = true;
                    strPan = input[0];

                    SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME_GET_PAN_REJECTION_STATUS);
                    request.addProperty("strPAN", strPan);
                    mCommonMethods.appendSecurityParams(mContext, request, "", "");

                    SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                    envelope.dotNet = true;
                    envelope.setOutputSoapObject(request);

                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);

                    HttpTransportSE androidHttpTranport = new HttpTransportSE(ServiceURL.SERVICE_URL);
                    androidHttpTranport.call(NAMESPACE + METHOD_NAME_GET_PAN_REJECTION_STATUS, envelope);

                    SoapPrimitive sa = (SoapPrimitive) envelope.getResponse();
                    String inputpolicylist = sa.toString();

                    if (inputpolicylist.equals("0")) {
                        return inputpolicylist;
                    } else {
                        ParseXML prsObj = new ParseXML();

                        inputpolicylist = prsObj.parseXmlTag(inputpolicylist, "NewDataSet");
                        List<String> nodeData = prsObj.parseParentNode(inputpolicylist, "Table");
                        lstRejectionData = prsObj.parseNodeElementPOSP_RA_Rejection(nodeData,
                                mCommonMethods.str_posp_ra_customer_type);

                        return "Success";
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return e.getMessage();
                }

            } else {
                running = false;
            }
            return null;

        }

        @Override
        protected void onPostExecute(String unused) {

            if (mProgressDialog.isShowing())
                mProgressDialog.dismiss();

            if (running) {

                if (unused.equals("0")) {
                    Intent mIntent = new Intent(Activity_POSP_RA_Dashboard.this, Activity_POSP_RA_PersonalInfo.class);
                    mIntent.putExtra("is_dashboard", true);
                    startActivity(mIntent);
                } else {
                    if (lstRejectionData.size() == 0) {
                        mCommonMethods.showMessageDialog(mContext, "NO Data Found..");
                    } else {
                        //check pan has data available
                        ArrayList<POJO_POSP_RA_Rejection> lstResult = db.getPOSP_RA_Rejection_By_PAN(strPan,
                                mCommonMethods.str_posp_ra_customer_type);
                        if (lstResult.size() != 0) {
                            db.delete_POSP_RA_Rejection_row(db.POSP_RA_REQ_ID + " = " + lstResult.get(0).getPrimaryID());
                        }

                        for (ParseXML.Pojo_POSP_RA_Rejection obj : lstRejectionData) {
                            ContentValues cv = new ContentValues();
                            cv.put(db.POSP_RA_DATA_ID, Activity_POSP_RA_Authentication.row_details);
                            cv.put(db.POSP_RA_REQ_PAN, strPan);
                            cv.put(db.POSP_RA_REQ_IA_CODE, obj.getStr_req_ia_code());
                            cv.put(db.POSP_RA_REQ_RAISED, obj.getStr_req_raised());
                            cv.put(db.POSP_RA_REQ_RAISED_REMARK, obj.getStr_req_raised_remark());
                            cv.put(db.POSP_RA_REQ_RAISED_DOC_STATUS, obj.getStr_req_raised_document_status());
                            cv.put(db.POSP_RA_REQ_RAISED_STATUS, obj.getStr_req_raised_status());
                            cv.put(db.POSP_RA_REQ_RAISED_DOC_NAME, obj.getStr_req_raised_document_name());

                            db.insert_POSP_RA_Rejection(cv);
                        }

                        Intent mIntent = new Intent(Activity_POSP_RA_Dashboard.this, Activity_POSP_RA_PersonalInfo.class);
                        mIntent.putExtra("is_rejection", true);
                        startActivity(mIntent);
                    }
                }
            } else {
                mCommonMethods.showToast(mContext, mCommonMethods.WEEK_INTERNET_MESSAGE);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_agent_posp_form_download:
                String strPan = edt_agent_posp_form_pan.getText().toString();
                strPan = strPan == null ? "" : strPan;

                if (strPan.length() == 10){

                    try {
                        filePOSP_RAForm = new StorageUtils().createFileToAppSpecificDir(mContext,strPan + "_POSP_RA_AGENT_FORM.pdf");
                        if (!filePOSP_RAForm.exists()){
                            new AsyncDownloadAgentForm().execute(strPan);
                        }else {
                            mCommonMethods.openAllDocs(mContext, filePOSP_RAForm);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else{
                    mCommonMethods.showToast(mContext, "Please Enter 10 Digit PAN Number !");
                }
                break;

            default:
                break;
        }
    }

    class AsyncDownloadAgentForm extends AsyncTask<String, String, String> {

        private volatile boolean running = true;
        private byte[] pdfResponce;

        @Override
        protected void onPreExecute() {
            mProgressDialog = new ProgressDialog(mContext, ProgressDialog.THEME_HOLO_LIGHT);
            mProgressDialog.setMessage(Html.fromHtml("<font color='#00a1e3'><b>Loading Please wait...<b></font>"));
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setCancelable(true);
            mProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... aurl) {
            try {

                running = true;

                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME_DOWNLOAD_AGENT_FORM);

                //for posp ra append '|' with PAN else pass only PAN
                request.addProperty("strPAN", aurl[0] + "|");

                mCommonMethods.appendSecurityParams(mContext, request, "", "");

                mCommonMethods.TLSv12Enable();

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);
                envelope.dotNet = true;

                envelope.setOutputSoapObject(request);

                // 	allowAllSSL();

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);

                HttpTransportSE androidHttpTranport = new HttpTransportSE(ServiceURL.SERVICE_URL);

                androidHttpTranport.call(NAMESPACE + METHOD_NAME_DOWNLOAD_AGENT_FORM, envelope);

                Object response = envelope.getResponse();

                if (response != null){
                    pdfResponce = Base64.decode(response.toString());

                    FileOutputStream fileOuputStream = null;

                    try {
                        fileOuputStream = new FileOutputStream(filePOSP_RAForm);
                        fileOuputStream.write(pdfResponce);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        if (fileOuputStream != null) {
                            try {
                                fileOuputStream.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    return "Success";
                }else{
                    return "No Data Found";
                }
            } catch (Exception e) {
                running = false;
                return  e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String unused) {
            if (mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }

            if (running) {
                running = false;
                if (unused.equalsIgnoreCase("No Data Found")){
                    mCommonMethods.showMessageDialog(mContext, "No Data Found");
                }else{
                    if (pdfResponce == null){
                        mCommonMethods.showMessageDialog(mContext, "No Data Found");
                    }else{
                        try {
                            mCommonMethods.openAllDocs(mContext, filePOSP_RAForm);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } else {
                mCommonMethods.showMessageDialog(mContext, unused);
            }
        }
    }
}
