package sbilife.com.pointofsale_bancaagency.reports;

import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import sbilife.com.pointofsale_bancaagency.DatabaseHelper;
import sbilife.com.pointofsale_bancaagency.Element_TextView_BaseAdapter;
import sbilife.com.pointofsale_bancaagency.R;
import sbilife.com.pointofsale_bancaagency.ServiceURL;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;

public class GroupFeedbackActivity extends AppCompatActivity {

    private Context mContext;
    private CommonMethods mCommonMethods;

    private Spinner spnr_feedback_main_type , spnr_feedback_sub_type;
    private EditText edt_feedback_policy_no, edt_main_feedback;

    private String strFeedbackMainType = "", strFeedbackSubType = "", strPolicyNo = "",
        strMainFeedback = "", strUserId = "";

    private DatabaseHelper db;

    private long row = 0;

    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_group_feedback);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);

        mContext = this;
        mCommonMethods = new CommonMethods();
        mCommonMethods.setApplicationToolbarMenu(this,"Group Feedback");
        db = new DatabaseHelper(mContext);

        spnr_feedback_main_type = findViewById(R.id.spnr_feedback_main_type);
        spnr_feedback_sub_type = findViewById(R.id.spnr_feedback_sub_type);
        edt_feedback_policy_no = findViewById(R.id.edt_feedback_policy_no);
        edt_main_feedback = findViewById(R.id.edt_main_feedback);
        Button btn_feedback_ok = findViewById(R.id.btn_feedback_ok);

        strUserId = mCommonMethods.GetUserID(mContext);

        ArrayList<String> lst_main_types = new ArrayList<String>();
        lst_main_types.add("Grievances");
        lst_main_types.add("Feedback");
        lst_main_types.add("Queries");
        fillSpinnerValue(spnr_feedback_main_type, lst_main_types);

        ArrayList<String> lst_sub_types = new ArrayList<String>();
        lst_sub_types.add("Fund");
        lst_sub_types.add("GTI");
        lst_sub_types.add("Others");
        fillSpinnerValue(spnr_feedback_sub_type, lst_sub_types);


        btn_feedback_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                strFeedbackMainType = spnr_feedback_main_type.getSelectedItem().toString();
                strFeedbackSubType = spnr_feedback_sub_type.getSelectedItem().toString();
                strPolicyNo = edt_feedback_policy_no.getText().toString();
                strMainFeedback = edt_main_feedback.getText().toString();

                if (!strMainFeedback.equals("")){

                    //save data locally and synch
                    String TABLE_GROUP_FEEDBACK = "T_GROUP_FEEDBACK";
                    String GROUP_FEEDBACK_ID = "id";
                    String GROUP_FEEDBACK_USER_ID = "user_id";
                    String GROUP_FEEDBACK_MAIN_TYPE = "main_type";
                    String GROUP_FEEDBACK_SUB_TYPE = "sub_type";
                    String GROUP_FEEDBACK_POLICY_NUMBER = "policy_number";
                    String GROUP_FEEDBACK_MAIN_FEEDBACK = "feedback";
                    String GROUP_FEEDBACK_DATE = "date";
                    String GROUP_FEEDBACK_IS_DELETE = "is_delete";
                    String GROUP_FEEDBACK_IS_SYNCH = "is_synch";

                    ContentValues cv = new ContentValues();
                    cv.put(GROUP_FEEDBACK_USER_ID, strUserId);
                    cv.put(GROUP_FEEDBACK_MAIN_TYPE, strFeedbackMainType);
                    cv.put(GROUP_FEEDBACK_SUB_TYPE, strFeedbackSubType);
                    cv.put(GROUP_FEEDBACK_POLICY_NUMBER, strPolicyNo);
                    cv.put(GROUP_FEEDBACK_MAIN_FEEDBACK, strMainFeedback);
                    cv.put(GROUP_FEEDBACK_DATE, DateFormat.getDateTimeInstance().format(new Date()));
                    cv.put(GROUP_FEEDBACK_IS_DELETE, 0);
                    cv.put(GROUP_FEEDBACK_IS_SYNCH, 0);
                    row = db.insertGroupFeedbackDetails(cv);

                    mCommonMethods.showToast(mContext, "Data Saved Successfully!");

                    new AsynchSaveFeedbackDetails().execute();

                }else{
                    mCommonMethods.showToast(mContext, "Please Enter Your Grievances / Feedback / Queries");
                }

            }
        });
    }

    private void fillSpinnerValue(Spinner spinner, List<String> value_list) {

        Element_TextView_BaseAdapter retd_adapter = new Element_TextView_BaseAdapter(
                GroupFeedbackActivity.this, value_list);
        spinner.setAdapter(retd_adapter);
    }

    class AsynchSaveFeedbackDetails extends AsyncTask<String, String, String>{

        private volatile boolean running = true;

        @Override
        protected void onPreExecute() {
            mProgressDialog = new ProgressDialog(mContext,
                    ProgressDialog.THEME_HOLO_LIGHT);
            String Message = "Loading Please wait...";
            mProgressDialog.setMessage(Html
                    .fromHtml("<font color='#00a1e3'><b>" + Message
                            + "<b></font>"));
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {

            try {

                running = true;
                SoapObject request = null;

                String METHOD_NAME_GROUP_SAVE_FEEDBACK = "saveGroupsFeedback";
                String NAMESPACE = "http://tempuri.org/";
                request = new SoapObject(NAMESPACE, METHOD_NAME_GROUP_SAVE_FEEDBACK);
                request.addProperty("strType", strFeedbackMainType);
                request.addProperty("strSubType", strFeedbackSubType);
                request.addProperty("strPolNo", strPolicyNo);
                request.addProperty("strDesc", strMainFeedback);
                request.addProperty("strUserID", strUserId);

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);
                envelope.dotNet = true;

                envelope.setOutputSoapObject(request);

                mCommonMethods.TLSv12Enable();

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);

                String URl = ServiceURL.SERVICE_URL;
                HttpTransportSE androidHttpTranport = new HttpTransportSE(URl);
                try {

                    String SOAP_ACTION_GROUP_SAVE_FEEDBACK = "http://tempuri.org/saveGroupsFeedback";
                    androidHttpTranport.call(
                            SOAP_ACTION_GROUP_SAVE_FEEDBACK, envelope);
                    return envelope.getResponse().toString();
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

            ContentValues cv = new ContentValues();

            String GROUP_FEEDBACK_IS_SYNCH = "is_synch";
            if (result.equals("1")){
                cv.put(GROUP_FEEDBACK_IS_SYNCH, 1);
                db.updateGroupFeedbackDetails(cv, new String[]{String.valueOf(row)});
            }else{
                cv.put(GROUP_FEEDBACK_IS_SYNCH, 0);
                db.updateGroupFeedbackDetails(cv, new String[]{String.valueOf(row)});
            }

        }
    }
}
