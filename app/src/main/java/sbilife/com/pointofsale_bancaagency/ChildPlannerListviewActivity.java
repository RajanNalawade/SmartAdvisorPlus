package sbilife.com.pointofsale_bancaagency;

import android.annotation.SuppressLint;
import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
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

import sbilife.com.pointofsale_bancaagency.futureplanner.FuturePlannerActivity;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;

@SuppressLint("ClickableViewAccessibility")
public class ChildPlannerListviewActivity extends AppCompatActivity {

	private ProgressDialog pd;
	private String child_planner_response = "";
	private String CIFcode;
	private TextView txt_Error;


	// TextView tv_title_cif_common ;
	// ImageButton imagtbt_option ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.child_planner_response_details);
		//getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);
		Intent i =getIntent();
		String title =i.getStringExtra("currentTab");
		new CommonMethods().setApplicationToolbarMenu(this, title);

		txt_Error = findViewById(R.id.txt_Error);


		CIFcode = i.getStringExtra("CIF_Code");

		new ChildPlannListDetails().execute();

	}

	@Override
	public void onBackPressed() {
		Intent i = new Intent(ChildPlannerListviewActivity.this,
				FuturePlannerActivity.class);
		startActivity(i);
		finish();
	}

	// Saving NB details using Async task
	private class ChildPlannListDetails extends AsyncTask<Void, Void, Void> {

		List<ChildEducationPlannerBean> beans;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			
			pd = new ProgressDialog(ChildPlannerListviewActivity.this,ProgressDialog.THEME_HOLO_LIGHT);
			String Message = "Loading";			
			pd.setMessage(Html.fromHtml("<font color='#00a1e3'><b>" + Message + "<b></font>"));
			pd.setCancelable(false);
			pd.show();
		}

		@SuppressWarnings("unused")
		@Override
		protected Void doInBackground(Void... params) {

			beans = new ArrayList<>();
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

				} else {

					List<HashMap<String, String>> hm_rp_details = new ArrayList<>();

					if (beans != null) {

						for (ChildEducationPlannerBean bean : beans) {
							HashMap<String, String> hm_rp_detail = new HashMap<>();

							try {
								hm_rp_detail.put("prospect_name",
										bean.getProspectName());
								hm_rp_detail.put("prospect_email",
										bean.getProspectEmail());
								hm_rp_detail.put("prospect_mobile",
										bean.getProspectMobile());
								hm_rp_detail.put("product_type", "Child Plan");

							} catch (Exception ignored) {

							}

							hm_rp_details.add(hm_rp_detail);
						}

						String[] str_from = { "prospect_name",
								"prospect_email", "prospect_mobile",
								"product_type" };
						int[] in_to = { R.id.tv_child_prospect_name,
								R.id.tv_child_prospect_email,
								R.id.tv_child_prospect_mobile,
								R.id.tv_child_product_Type };

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
			} else {
				Toast.makeText(ChildPlannerListviewActivity.this,
						"No Data Found", Toast.LENGTH_SHORT).show();
			}

		}

	}



}
