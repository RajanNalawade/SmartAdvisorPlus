package sbilife.com.pointofsale_bancaagency.futureplanner;

import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import sbilife.com.pointofsale_bancaagency.ChildEducationPlannerBean;
import sbilife.com.pointofsale_bancaagency.DatabaseHelper;
import sbilife.com.pointofsale_bancaagency.ParseXML;
import sbilife.com.pointofsale_bancaagency.R;
import sbilife.com.pointofsale_bancaagency.ServiceURL;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;

public class ChildPlannerListviewActivity extends AppCompatActivity {

    private ProgressDialog pd;
    private String child_planner_response = "";
    private String CIFcode;
    private DatabaseHelper dbhelper;
    private List<HashMap<String, String>> hm_rp_details = null;
    List<ChildEducationPlannerBean> beans = null;
    private TextView txt_Error;
    private CommonMethods mCommonMethods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        // requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.child_planner_response_details);
        // getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,

        txt_Error = findViewById(R.id.txt_Error);
        mCommonMethods = new CommonMethods();

        /*
         * ImageView img_header =
         * (ImageView)getWindow().findViewById(R.id.header);
         * img_header.setOnClickListener(new OnClickListener() {
         *
         * //@Override public void onClick(View arg0) { // TODO Auto-generated
         * method stub Intent i=new Intent(ChildPlannerListviewActivity.this,
         * CarouselHomeActivity.class); startActivity(i); } });
         *
         * ImageButton img = (ImageButton)getWindow().findViewById(R.id.btn);
         * img.setOnClickListener(new OnClickListener() {
         *
         * //@Override public void onClick(View arg0) { // TODO Auto-generated
         * method stub showpopup_alert(arg0); } });
         */
        dbhelper = new DatabaseHelper(this);
        Intent i = getIntent();
        CIFcode = i.getStringExtra("CIF_Code");

        new ChildPlannListDetails().execute();

        // hm_rp_details = new ArrayList<HashMap<String, String>>();
        //
        // beans = new ArrayList<ChildEducationPlannerBean>();
        //
        //
        // //if (beans != null) {
        //
        // // for (ChildEducationPlannerBean bean : beans) {
        // HashMap<String, String> hm_rp_detail = new HashMap<String, String>();
        //
        //
        //
        // try {
        // hm_rp_detail.put("prospect_name", "Priyanka");
        // hm_rp_detail.put("prospect_email", "priyankawarekar@gmail.com");
        // hm_rp_detail.put("prospect_mobile", "8655356870");
        // hm_rp_detail.put("product_type", "Child Plan");
        //
        // }
        // catch (Exception e) {
        //
        // }
        //
        // hm_rp_details.add(hm_rp_detail);
        // // }
        //
        // String[] str_from = { "prospect_name","prospect_email",
        // "prospect_mobile", "product_type"};
        // int[] in_to = {
        // R.id.tv_child_prospect_name,R.id.tv_child_prospect_email,
        // R.id.tv_child_prospect_mobile, R.id.tv_child_product_Type
        // };
        //
        // SimpleAdapter adapter = new
        // SimpleAdapter(ChildPlannerListviewActivity.this,
        // hm_rp_details, R.layout.child_education_planner_items, str_from,
        // in_to);
        // ListView listView =
        // (ListView)findViewById(R.id.lv_child_education_planner_list);
        // listView.setAdapter(adapter);

        // }

    }

    // Saving NB details using Async task
    private class ChildPlannListDetails extends AsyncTask<Void, Void, Void> {

        List<ChildEducationPlannerBean> beans;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(ChildPlannerListviewActivity.this);
            pd.setMessage("Loading");
            pd.setCancelable(false);
            pd.show();
        }

        @SuppressWarnings("unused")
        @Override
        protected Void doInBackground(Void... params) {

            beans = new ArrayList<ChildEducationPlannerBean>();
            String METHOD_NAME_CHILD_PLANNER_ACK_DTLS = "getChLeads";
            String NAMESPACE = "http://tempuri.org/";
            SoapObject request = new SoapObject(NAMESPACE,
                    METHOD_NAME_CHILD_PLANNER_ACK_DTLS);

            request.addProperty("agentCode", CIFcode);
            request.addProperty("mobileNo", "");
            request.addProperty("strEmail", "");
            request.addProperty("strPwd", "");

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
                String SOAP_ACTION_CHILD_PLANNER_ACK_DTLS = "http://tempuri.org/getChLeads";
                androidHttpTranport.call(SOAP_ACTION_CHILD_PLANNER_ACK_DTLS,
                        envelope);
                Object response = envelope.getResponse();

                SoapPrimitive sa = null;

                sa = (SoapPrimitive) envelope.getResponse();

                if (sa != null) {
                    child_planner_response = sa.toString();
                }

                ParseXML prsObj = new ParseXML();
                String input = prsObj.parseXmlTag(child_planner_response,
                        "NewDataSet");
                List<String> Node = prsObj.parseParentNode(input, "Table");

                beans = prsObj.parseNodeElementChildEducationList(Node);

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if (pd.isShowing()) {
                pd.dismiss();
            }

            System.out.println("Response in post: " + child_planner_response);

            if (child_planner_response != null) {
                // Checking response
                if (child_planner_response.equals("1")) {
                    // Toast.makeText(ChildPlannerListviewActivity.this,
                    // "Details has been synced successfully.",
                    // Toast.LENGTH_SHORT).show();

                } else {

                    hm_rp_details = new ArrayList<HashMap<String, String>>();
                    if (beans != null) {

                        for (ChildEducationPlannerBean bean : beans) {
                            HashMap<String, String> hm_rp_detail = new HashMap<String, String>();

                            try {
                                hm_rp_detail.put("prospect_name",
                                        bean.getProspectName());
                                hm_rp_detail.put("prospect_email",
                                        bean.getProspectEmail());
                                hm_rp_detail.put("prospect_mobile",
                                        bean.getProspectMobile());
                                hm_rp_detail.put("product_type", "Child Plan");

                            } catch (Exception e) {

                            }

                            hm_rp_details.add(hm_rp_detail);
                        }

                        String[] str_from = {"prospect_name",
                                "prospect_email", "prospect_mobile",
                                "product_type"};
                        int[] in_to = {R.id.tv_child_prospect_name,
                                R.id.tv_child_prospect_email,
                                R.id.tv_child_prospect_mobile,
                                R.id.tv_child_product_Type};

                        SimpleAdapter adapter = new SimpleAdapter(
                                ChildPlannerListviewActivity.this,
                                hm_rp_details,
                                R.layout.child_education_planner_items,
                                str_from, in_to);
                        ListView listView = findViewById(R.id.lv_child_education_planner_list);
                        listView.setAdapter(adapter);
                        txt_Error.setVisibility(View.GONE);
                    } else
                        txt_Error.setVisibility(View.VISIBLE);
                    txt_Error.setVisibility(View.GONE);
                }
                // else
                // txt_Error.setVisibility(View.VISIBLE);
            } else {
                Toast.makeText(ChildPlannerListviewActivity.this,
                        "No Data Found", Toast.LENGTH_SHORT).show();
            }

        }

    }


}
