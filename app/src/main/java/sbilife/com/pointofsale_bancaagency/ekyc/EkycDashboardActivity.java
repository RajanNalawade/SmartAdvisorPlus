package sbilife.com.pointofsale_bancaagency.ekyc;

import androidx.appcompat.app.AppCompatActivity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.List;

import sbilife.com.pointofsale_bancaagency.DatabaseHelper;
import sbilife.com.pointofsale_bancaagency.ParseXML;
import sbilife.com.pointofsale_bancaagency.R;
import sbilife.com.pointofsale_bancaagency.ServiceURL;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;

public class EkycDashboardActivity extends AppCompatActivity {

    private Context mContext;
    private CommonMethods mCommonMethods;
    private DatabaseHelper db;

    private ListView list_ekyc_dashboard;
    private AdapterEkycDashboard mAdapterEkycDashboard;
    private ArrayList<ParseXML.XMLHolderEkycDetails> lstEkycDetailses = new ArrayList<>();

    public  final int DIALOG_PROGRESS = 0;
    private ProgressDialog mProgressDialog;
    private Asynch_Get_eKyc_dashboard_details mAsynch_get_eKyc_dashboard_details;

    private  final String NAMESPACE = "http://tempuri.org/";
    private  final String URl = ServiceURL.SERVICE_URL;

    private  final String SOAP_ACTION_DASHBOARD_EKYC_GET_DETAILS = "http://tempuri.org/geteKYCdetail_SMRT";
    private  final String METHOD_NAME_DASHBOARD_EKYC_GET_DETAILS = "geteKYCdetail_SMRT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_ekyc_dashboard);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);

        initialise();
    }

    public void initialise(){
        mContext = this;
        mCommonMethods = new CommonMethods();

        db = new DatabaseHelper(mContext);

        mCommonMethods.setApplicationToolbarMenu(this, "Ekyc Dashboard");

        list_ekyc_dashboard = findViewById(R.id.list_ekyc_dashboard);

        try {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //lstEkycDetailses = db.get_eKyc_dashboard_list(mCommonMethods.GetUserType(mContext), 1);

                    if (mCommonMethods.isNetworkConnected(mContext)){
                        mAsynch_get_eKyc_dashboard_details = new Asynch_Get_eKyc_dashboard_details();
                        mAsynch_get_eKyc_dashboard_details.execute();
                    }else{
                        mCommonMethods.showMessageDialog(mContext, mCommonMethods.NO_INTERNET_MESSAGE);
                    }
                }
            });

        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public class AdapterEkycDashboard extends BaseAdapter{

        private Context adapterContext;
        private ArrayList<ParseXML.XMLHolderEkycDetails> lstAdapterEkycDetailses = new ArrayList<>();

        public AdapterEkycDashboard(Context adapterContext, ArrayList<ParseXML.XMLHolderEkycDetails> lstAdapterEkycDetailses) {
            this.adapterContext = adapterContext;
            this.lstAdapterEkycDetailses = lstAdapterEkycDetailses;
        }

        class ViewHolder {
            TextView tv_ekyc_policy_no;
            TextView tv_ekyc_user_type;
            TextView tv_ekyc_link_date;
        }

        @Override
        public Object getItem(int position) {
            return lstAdapterEkycDetailses.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getCount() {
            return lstAdapterEkycDetailses.size();
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {

            ViewHolder v;
            if (view == null) {
                LayoutInflater mInflater = LayoutInflater.from(adapterContext);
                view = mInflater.inflate(R.layout.row_ekyc_dashboard, null);

                v = new ViewHolder();

                v.tv_ekyc_policy_no = view.findViewById(R.id.tv_ekyc_policy_no);
                v.tv_ekyc_user_type = view.findViewById(R.id.tv_ekyc_user_type);
                v.tv_ekyc_link_date = view.findViewById(R.id.tv_ekyc_link_date);

                view.setTag(v);

            } else {
                v = (ViewHolder) view.getTag();
            }

            v.tv_ekyc_policy_no.setText(lstAdapterEkycDetailses.get(position).getStr_policy_no());
            v.tv_ekyc_user_type.setText(lstAdapterEkycDetailses.get(position).getStr_user_type());

            //Date d1 = new Date(Long.valueOf(lstAdapterEkycDetailses.get(position).getStr_link_date()));
            v.tv_ekyc_link_date.setText(lstAdapterEkycDetailses.get(position).getStr_link_date());

            return view;
        }
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DIALOG_PROGRESS:
                mProgressDialog = new ProgressDialog(this,
                        ProgressDialog.THEME_HOLO_LIGHT);
                String Message = "Loading Please wait...";
                mProgressDialog.setMessage(Html
                        .fromHtml("<font color='#00a1e3'><b>" + Message
                                + "<b></font>"));
                mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                mProgressDialog.setCancelable(false);
                mProgressDialog.show();
                return mProgressDialog;

            default:
                return null;
        }
    }

    public class Asynch_Get_eKyc_dashboard_details extends AsyncTask<String, String, String> {

        private volatile boolean running = true;
        private String str_output = "";

        @Override
        protected void onPreExecute() {
            showDialog(DIALOG_PROGRESS);
        }

        @Override
        protected String doInBackground(String... params) {

            try {

                running = true;
                SoapObject request = null;

                request = new SoapObject(NAMESPACE, METHOD_NAME_DASHBOARD_EKYC_GET_DETAILS);
                request.addProperty("strCode", mCommonMethods.GetUserID(mContext));

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);
                envelope.dotNet = true;

                envelope.setOutputSoapObject(request);

                mCommonMethods.TLSv12Enable();

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);

                HttpTransportSE androidHttpTranport = new HttpTransportSE(URl);
                try {

                    androidHttpTranport.call(SOAP_ACTION_DASHBOARD_EKYC_GET_DETAILS, envelope);
                    //Object response = envelope.getResponse();

                    SoapPrimitive sa = (SoapPrimitive) envelope.getResponse();

                    str_output = sa.toString();

                    if (str_output.equals("0")) {
                        return str_output;
                    }else {
                        ParseXML prsObj = new ParseXML();
                        str_output = prsObj.parseXmlTag(
                                str_output, "Data");

                        List<String> Node = prsObj.parseParentNode(
                                str_output, "Table");

                        lstEkycDetailses = prsObj.parseNodeElementEkycDetails(Node, mCommonMethods.GetUserType(mContext));
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
        protected void onPostExecute(String s) {
            if (mProgressDialog.isShowing())
                mProgressDialog.dismiss();

            if (running) {

                if (str_output.equals("0")) {
                    mCommonMethods.showMessageDialog(mContext, "No Record Found!!");
                }else {

                    mAdapterEkycDashboard = new AdapterEkycDashboard(mContext, lstEkycDetailses);
                    list_ekyc_dashboard.setAdapter(mAdapterEkycDashboard);

                    running = false;
                    str_output = "";
                }
            } else {
                mCommonMethods.showMessageDialog(mContext, mCommonMethods.WEEK_INTERNET_MESSAGE);
            }
        }
    }
}
