package sbilife.com.pointofsale_bancaagency.futureplanner;

import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Html;
import android.view.View;
import android.view.Window;
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

import sbilife.com.pointofsale_bancaagency.DatabaseHelper;
import sbilife.com.pointofsale_bancaagency.ParseXML;
import sbilife.com.pointofsale_bancaagency.R;
import sbilife.com.pointofsale_bancaagency.ServiceURL;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods.UserDetailsValuesModel;

public class HLVCalculatorListViewActivity extends AppCompatActivity {

    private ProgressDialog pd;
    private String hlvPlannerResponse = "";
    private String CIFcode;
    private DatabaseHelper dbhelper;
    //private List<ChildEducationPlannerBean> beans = null;
    private TextView txt_Error;//,textviewTopHeadingPlanner;

    private CommonMethods mCommonMethods;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.child_planner_response_details);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);

        new CommonMethods().setApplicationToolbarMenu(this, "HLV Calculator Leads");

        context = this;

        mCommonMethods = new CommonMethods();

        dbhelper = new DatabaseHelper(this);

        txt_Error = findViewById(R.id.txt_Error);
        getUserDetails();
        new HLVCalculatorListDetails().execute();
    }

    private void getUserDetails() {
        UserDetailsValuesModel userDetailsValuesModel = mCommonMethods
                .setUserDetails(context);
        CIFcode = userDetailsValuesModel.getStrCIFBDMUserId();
        System.out.println("strCIFBDMUserId:" + CIFcode);
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(HLVCalculatorListViewActivity.this,
                FuturePlannerActivity.class);
        startActivity(i);
        finish();
    }

    private class HLVCalculatorListDetails extends AsyncTask<Void, Void, Void> {

        List<HLVCalculatorLeadsBean> beans = new ArrayList<HLVCalculatorLeadsBean>();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(HLVCalculatorListViewActivity.this, ProgressDialog.THEME_HOLO_LIGHT);
            String Message = "Loading";
            pd.setMessage(Html.fromHtml("<font color='#00a1e3'><b>" + Message + "<b></font>"));
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            String METHOD_NAME_HLV_PLANNER_LEADS = "getHLVLeads";
            String NAMESPACE = "http://tempuri.org/";
            SoapObject request = new SoapObject(NAMESPACE,
                    METHOD_NAME_HLV_PLANNER_LEADS);
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
                String SOAP_ACTION_HLV_PLANNER_LEADS = "http://tempuri.org/getHLVLeads";
                androidHttpTranport.call(SOAP_ACTION_HLV_PLANNER_LEADS,
                        envelope);
                Object response = envelope.getResponse();
                System.out.println("response:" + response.toString());
                SoapPrimitive sa = null;

                sa = (SoapPrimitive) envelope.getResponse();

                if (sa != null) {
                    hlvPlannerResponse = sa.toString();
                }

				/*<NewDataSet>
				<Table>
				<PERSON_NAME>Tushar</PERSON_NAME> <PERSON_DOB>21/01/1982</PERSON_DOB>
				<PERSON_AGE>35</PERSON_AGE> </Table> <Table> <PERSON_NAME>Rajan</PERSON_NAME>
				<PERSON_DOB>20/01/1982</PERSON_DOB> <PERSON_AGE>35</PERSON_AGE>
				</Table>
				</NewDataSet> */

                ParseXML prsObj = new ParseXML();
                String input = prsObj.parseXmlTag(hlvPlannerResponse, "NewDataSet");
                List<String> Node = prsObj.parseParentNode(input, "Table");

                for (String lsNode : Node) {

                    String PERSON_NAME = prsObj.parseXmlTag(lsNode, "PERSON_NAME");
                    String PERSON_DOB = prsObj.parseXmlTag(lsNode, "PERSON_DOB");
                    String PERSON_AGE = prsObj.parseXmlTag(lsNode, "PERSON_AGE");
                    HLVCalculatorLeadsBean nodeVal = new HLVCalculatorLeadsBean(PERSON_NAME, PERSON_DOB, PERSON_AGE);

                    beans.add(nodeVal);
                }

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

            if (hlvPlannerResponse != null) {
                // Checking response
                if (hlvPlannerResponse.equals("1")) {

                } else {

                    List<HashMap<String, String>> hlv_calculator_details = new ArrayList<HashMap<String, String>>();
                    if (beans != null) {
                        for (HLVCalculatorLeadsBean bean : beans) {
                            HashMap<String, String> hm_rp_detail = new HashMap<String, String>();

                            try {
                                hm_rp_detail.put("PERSON_NAME",
                                        bean.getPERSON_NAME());

                                hm_rp_detail.put("PERSON_DOB",
                                        bean.getPERSON_DOB());

                                hm_rp_detail.put("PERSON_AGE",
                                        bean.getPERSON_AGE());

                                hm_rp_detail.put("product_type",
                                        "HLV Planner");

                            } catch (Exception e) {

                            }

                            hlv_calculator_details.add(hm_rp_detail);
                        }

                        String[] str_from = {"PERSON_NAME", "PERSON_DOB", "PERSON_AGE", "product_type"};
                        int[] in_to = {R.id.textviewHLVCalculatorprospectName, R.id.textviewHLVCalculatorDOB,
                                R.id.textviewHLVCalculatorAge, R.id.textviewHLVCalculatorProductType
                        };

                        SimpleAdapter adapter = new SimpleAdapter(HLVCalculatorListViewActivity.this,
                                hlv_calculator_details, R.layout.hlv_calculator_items, str_from,
                                in_to);
                        ListView listView = findViewById(R.id.lv_child_education_planner_list);
                        listView.setAdapter(adapter);
                        txt_Error.setVisibility(View.GONE);
                    } else
                        txt_Error.setVisibility(View.VISIBLE);
                    txt_Error.setVisibility(View.GONE);
                }
                //				else
                //					txt_Error.setVisibility(View.VISIBLE);
            } else {
                Toast.makeText(HLVCalculatorListViewActivity.this,
                        "No Data Found",
                        Toast.LENGTH_SHORT).show();
            }

        }

    }


    class HLVCalculatorLeadsBean {
        private String PERSON_NAME = "", PERSON_DOB = "", PERSON_AGE = "";

        HLVCalculatorLeadsBean(String pERSON_NAME, String pERSON_DOB,
                               String pERSON_AGE) {
            super();
            PERSON_NAME = pERSON_NAME;
            PERSON_DOB = pERSON_DOB;
            PERSON_AGE = pERSON_AGE;
        }

        String getPERSON_NAME() {
            return PERSON_NAME;
        }

        String getPERSON_DOB() {
            return PERSON_DOB;
        }

        String getPERSON_AGE() {
            return PERSON_AGE;
        }
    }
}

