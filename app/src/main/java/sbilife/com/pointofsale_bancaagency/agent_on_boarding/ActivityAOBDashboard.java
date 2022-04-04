package sbilife.com.pointofsale_bancaagency.agent_on_boarding;

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
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

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

import sbilife.com.pointofsale_bancaagency.DatabaseHelper;
import sbilife.com.pointofsale_bancaagency.ParseXML;
import sbilife.com.pointofsale_bancaagency.R;
import sbilife.com.pointofsale_bancaagency.ServiceURL;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;
import sbilife.com.pointofsale_bancaagency.common.StorageUtils;
import sbilife.com.pointofsale_bancaagency.ekyc.utilites.Base64;

public class ActivityAOBDashboard extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemLongClickListener {

    private final String NAMESPACE = "http://tempuri.org/";
    private final String METHOD_NAME_SAVE_AOB_DETAILS = "saveAgentOnboardingDetail";
    private final String METHOD_NAME_DOWNLOAD_AGENT_FORM = "getAgentForm";
    private Context mContext;
    private CommonMethods mCommonMethods;
    private DatabaseHelper db;
    private EditText edt_agent_form_pan;
    private ListView lv_aob_dashboard;
    private TextView txt_aob_dashboard_no_data;
    private ImageButton btn_agent_form_download;
    private ArrayList<PojoAOB> lstAOBDashboardDetails = new ArrayList<>();
    private AdapterAOBDashboard mAdapterAOBDashboard;
    private ProgressDialog mProgressDialog;
    private StringBuilder str_final_aob_info;
    private AsyncAllAOB mAsyncAllAOB;
    private File fileAgentForm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_aob_dashboard);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.cifenrollment_pf_activity_title);

        initialisation();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(ActivityAOBDashboard.this, Activity_AOB_Authentication.class));
    }

    public void initialisation() {

        mContext = this;
        mCommonMethods = new CommonMethods();
        mCommonMethods.setApplicationToolbarMenu1(this, "Agent on Boarding");

        db = new DatabaseHelper(mContext);

        lv_aob_dashboard = (ListView) findViewById(R.id.lv_aob_dashboard);
        lv_aob_dashboard.setOnItemLongClickListener(this);

        txt_aob_dashboard_no_data = (TextView) findViewById(R.id.txt_aob_dashboard_no_data);

        edt_agent_form_pan = findViewById(R.id.edt_agent_form_pan);
        btn_agent_form_download = findViewById(R.id.btn_agent_form_download);
        btn_agent_form_download.setOnClickListener(this);

        //set Data from DB
        lstAOBDashboardDetails = db.get_AOB_dashboard_details_by_ID();

        if (lstAOBDashboardDetails.size() > 0) {

            txt_aob_dashboard_no_data.setVisibility(View.GONE);
            lv_aob_dashboard.setVisibility(View.VISIBLE);

            mAdapterAOBDashboard = new AdapterAOBDashboard(mContext, lstAOBDashboardDetails);
            lv_aob_dashboard.setAdapter(mAdapterAOBDashboard);

        } else {
            txt_aob_dashboard_no_data.setVisibility(View.VISIBLE);
            lv_aob_dashboard.setVisibility(View.GONE);
        }

        str_final_aob_info = new StringBuilder();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_agent_form_download:
                String strPan = edt_agent_form_pan.getText().toString();
                strPan = strPan == null ? "" : strPan;

                if (strPan.length() == 10) {

                    try {
                        fileAgentForm = new StorageUtils().createFileToAppSpecificDir(mContext, strPan + "_AGENT_FORM.pdf");
                        if (!fileAgentForm.exists()) {
                            new AsyncDownloadAgentForm().execute(strPan);
                        } else {
                            mCommonMethods.openAllDocs(mContext, fileAgentForm);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    mCommonMethods.showToast(mContext, "Please Enter 10 Digit PAN Number !");
                }
                break;

            default:
                break;
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

        if (lstAOBDashboardDetails.get(position).getStr_id().equals(""))
            Activity_AOB_Authentication.row_details = 0;
        else {
            Activity_AOB_Authentication.row_details = Integer.parseInt(lstAOBDashboardDetails.get(position).getStr_id());
        }

        //for synch failure then synch it only for data not for pdf doc
        if (lstAOBDashboardDetails.get(position).getStr_synch_status().equals("15")) {

            //get all data from db
            ArrayList<PojoAOB> lstRes = db.get_agent_on_boarding_details_by_ID(Activity_AOB_Authentication.row_details);
            if (lstRes.size() > 0) {

                str_final_aob_info = new StringBuilder();

                //create final Agent on boarding string to synch
                str_final_aob_info.append("<?xml version='1.0' encoding='utf-8' ?><agent_on_boarding>");
                //str_final_aob_info.append(lstRes.get(0).getStr_basic_details().toString());//basic details
                str_final_aob_info.append(lstRes.get(0).getStr_personal_info().toString());//personal info
                str_final_aob_info.append(lstRes.get(0).getStr_occupation_info().toString());//occupational info
                str_final_aob_info.append(lstRes.get(0).getStr_nomination_info().toString());//nominational info
                str_final_aob_info.append(lstRes.get(0).getStr_bank_details().toString());//bank details info
                str_final_aob_info.append(lstRes.get(0).getStr_form_1_a().toString());//form 1-a info
                str_final_aob_info.append(lstRes.get(0).getStr_exam_training_details().toString());//Exam and Training details
                str_final_aob_info.append(lstRes.get(0).getStr_bsm_interview_questions().toString());//BSM interview questions

                str_final_aob_info.append("<um_code>" + mCommonMethods.GetUserCode(mContext) + "</um_code>");

                Calendar cal = Calendar.getInstance();
                SimpleDateFormat sdp = new SimpleDateFormat("MM-dd-yyyy");
                String str_created_date = sdp.format(new Date(cal.getTimeInMillis()));
                str_final_aob_info.append("<created_date>" + str_created_date + "</created_date>");//created_date

                str_final_aob_info.append("</agent_on_boarding>");

                mAsyncAllAOB = new AsyncAllAOB();
                mAsyncAllAOB.execute();

            } else {
                mCommonMethods.showMessageDialog(mContext, "Data Synch Failed");
            }

        } else {
            /*Intent mIntent = new Intent(ActivityAOBDashboard.this, ActivityAOBBasicDetails.class);*/
            Intent mIntent = new Intent(ActivityAOBDashboard.this, ActivityAOBPersonalInfo.class);
            mIntent.putExtra("is_dashboard", true);
            startActivity(mIntent);
        }

        return false;
    }

    public class AdapterAOBDashboard extends BaseAdapter {

        private Context mAdptrContext;
        private ArrayList<PojoAOB> lstAdapterAOBDetails = new ArrayList<>();

        public AdapterAOBDashboard(Context mAdptrContext, ArrayList<PojoAOB> lstAdapterAOBDetails) {
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

                //v.tv_aob_dashboard_count = (TextView) view.findViewById(R.id.tv_aob_dashboard_count);
                v.tv_aob_dashboard_name = (TextView) view.findViewById(R.id.tv_aob_dashboard_name);
                v.tv_aob_dashboard_pan = (TextView) view.findViewById(R.id.tv_aob_dashboard_pan);
                v.tv_aob_dashboard_synch = (TextView) view.findViewById(R.id.tv_aob_dashboard_synch);

                view.setTag(v);

            } else {
                v = (ViewHolder) view.getTag();
            }

            //v.tv_aob_dashboard_count.setText((position+1)+".");

            String str_personal = lstAdapterAOBDetails.get(position).getStr_personal_info();
            str_personal = str_personal == null ? "" : str_personal;

            ParseXML mParseXML = new ParseXML();

            if (!str_personal.equals("")) {
                v.tv_aob_dashboard_name.setText(mParseXML.parseXmlTag(str_personal, "personal_info_full_name"));
            } else {

                //get PAN details
                String str_pan_details = lstAdapterAOBDetails.get(position).getStr_pan_details().toString();

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

            String str_status = lstAdapterAOBDetails.get(position).getStr_synch_status();

            switch (str_status) {

                /*case "1":
                    //call personal info page with editable
                    mIntent = new Intent(Activity_AOB_Authentication.this, ActivityAOBBasicDetails.class);
                    mIntent.putExtra("is_dashboard", false);
                    startActivity(mIntent);
                    break;*/

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
                    v.tv_aob_dashboard_synch.setText("Form 1A");
                    break;

                case "6":
                    v.tv_aob_dashboard_synch.setText("Exam And Trainig Details");
                    break;

                case "7":
                    v.tv_aob_dashboard_synch.setText("Terms And Conditions");
                    break;

                case "8":
                    v.tv_aob_dashboard_synch.setText("Agent Form Upload Pending");
                    break;

                case "9":
                    v.tv_aob_dashboard_synch.setText("Agent Declaration Upload Pending");
                    break;
                case "10":
                    v.tv_aob_dashboard_synch.setText("Agent Photo Upload Pending");
                    break;
                case "11":
                    v.tv_aob_dashboard_synch.setText("Agent Signature Upload Pending");
                    break;

                case "12":
                    v.tv_aob_dashboard_synch.setText("Other Document Upload Pending");
                    break;

                case "13":

                case "14":
                    v.tv_aob_dashboard_synch.setText("Success");
                    break;

                case "15":
                    v.tv_aob_dashboard_synch.setText("Not Synched");
                    break;

                case "16":
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

                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME_SAVE_AOB_DETAILS);
                request.addProperty("xmlStr", str_final_aob_info.toString());
                mCommonMethods.TLSv12Enable();

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;

                envelope.setOutputSoapObject(request);

                HttpTransportSE androidHttpTranport = new HttpTransportSE(ServiceURL.SERVICE_URL);
                try {
                    androidHttpTranport.call(NAMESPACE + METHOD_NAME_SAVE_AOB_DETAILS, envelope);
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
                cv.put(db.AGENT_ON_BOARDING_UPDATED_BY, mCommonMethods.GetUserCode(mContext));

                Calendar c = Calendar.getInstance();
                //save date in long
                cv.put(db.AGENT_ON_BOARDING_UPDATED_DATE, new Date(c.getTimeInMillis()).getTime() + "");

                String str_msg = "";
                if (result.equals("1")) {
                    cv.put(db.AGENT_ON_BOARDING_SYNCH_STATUS, "14");//for successfull synch
                    str_msg = "Data synch Successfully..";
                } else if (result.equals("0")) {
                    cv.put(db.AGENT_ON_BOARDING_SYNCH_STATUS, "15");//for synch failure
                    str_msg = "Data synch Failure..\ntry after some time by dashboard menu.";
                } else if (result.equals("2")) {
                    cv.put(db.AGENT_ON_BOARDING_SYNCH_STATUS, "16");//for data already exist in server
                    str_msg = "Applicant data already exists in server";
                }

                int i = db.update_agent_on_boarding_details(cv, db.AGENT_ON_BOARDING_ID + " =? ",
                        new String[]{Activity_AOB_Authentication.row_details + ""});

                Activity_AOB_Authentication.row_details = 0;

                str_final_aob_info = new StringBuilder();

                mCommonMethods.showMessageDialog(mContext, str_msg);

            } else {
                mCommonMethods.showMessageDialog(mContext, mCommonMethods.WEEK_INTERNET_MESSAGE);
            }

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
                request.addProperty("strPAN", aurl[0]);

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

                if (response != null) {
                    pdfResponce = Base64.decode(response.toString());

                    FileOutputStream fileOuputStream = null;

                    try {
                        fileOuputStream = new FileOutputStream(fileAgentForm);
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
                } else {
                    return "No Data Found";
                }
            } catch (Exception e) {
                running = false;
                return e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String unused) {
            if (mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }

            if (running) {
                running = false;
                if (unused.equalsIgnoreCase("No Data Found")) {
                    mCommonMethods.showMessageDialog(mContext, "No Data Found");
                } else {
                    if (pdfResponce == null) {
                        mCommonMethods.showMessageDialog(mContext, "No Data Found");
                    } else {
                        try {
                            mCommonMethods.openAllDocs(mContext, fileAgentForm);
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
