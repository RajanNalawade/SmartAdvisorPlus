package sbilife.com.pointofsale_bancaagency;

import androidx.appcompat.app.AppCompatActivity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import sbilife.com.pointofsale_bancaagency.common.CommonMethods;

public class CustomerDetailActivity extends AppCompatActivity {
    private static final int DIALOG_DOWNLOAD_PROGRESS = 0;
    private final Context context = this;
    private final String NAMESPACE = "http://tempuri.org/";
    private final String URl = ServiceURL.SERVICE_URL;
    private final String METHOD_NAME_CUSTOMER_DETAIL_AGENCY = "getCustomerDtls";
    protected ListView lv;
    /*
     * these are all global variables.
     */
    //String username ="";
    private ImageView imgcontact_cust_r;
    private ImageView imgcontact_cust_m;
    private ImageView imgemail_cust;
    private DownloadFileAsyncCustomerDetail taskCustomerDetail;
    private ProgressDialog mProgressDialog;
    //TextView txttotalpolicy;
    private String strCustomerId;
    private String AgentCode = "";
    private String Email = "";
    private String Mobile = "";
    private String Password = "";
    private String strPolicyListErrorCOde;
    private TextView txtfname;
    private TextView txtdob;
    private TextView txtcity;
    private TextView txtaddressone;
    private TextView txtaddresstwo;
    private TextView txtpostalcode;
    private TextView txtstate;
    private TextView txtcontactr;
    private TextView txtcontactm;
    private TextView txtemailid;
    private String strFirstName;
    private String strDOB;
    private String strCity;
    private String strAddress1;
    private String strAddress2;
    private String strPostCode;
    private String strState;
    private String strContactR;
    private String strContactM;
    private String strEmailId;
    private CommonMethods mCommonMethods;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.customer_list);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);


        mCommonMethods = new CommonMethods();

        txtfname = findViewById(R.id.txtfname);
        txtdob = findViewById(R.id.txtdob);
        txtcity = findViewById(R.id.txtcity);
        txtaddressone = findViewById(R.id.txtaddressone);
        txtaddresstwo = findViewById(R.id.txtaddresstwo);
        txtpostalcode = findViewById(R.id.txtpostalcode);
        txtstate = findViewById(R.id.txtstate);
        txtcontactr = findViewById(R.id.txtcontactr);
        txtcontactm = findViewById(R.id.txtcontactm);
        txtemailid = findViewById(R.id.txtemailid);

        imgcontact_cust_r = findViewById(R.id.imgcontact_cust_r);
        imgcontact_cust_m = findViewById(R.id.imgcontact_cust_m);
        imgemail_cust = findViewById(R.id.imgemail_cust);


        mCommonMethods.setApplicationToolbarMenu(this, "Customer Details");

        Intent i = getIntent();
        strCustomerId = i.getStringExtra("CustomerId");

        AgentCode = i.getStringExtra("strAgentCode");
        Email = i.getStringExtra("strEmail");
        Mobile = i.getStringExtra("strMobileNo");
        Password = i.getStringExtra("strPassword");

        mProgressDialog = new ProgressDialog(this);
        taskCustomerDetail = new DownloadFileAsyncCustomerDetail();

        if (mCommonMethods.isNetworkConnected(context)) {
            //startDownloadCustomer();
            service_hits();
        } else {
            intereneterror();
        }

        txtcontactr.setOnClickListener(new OnClickListener() {

            //@Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                if (!txtcontactr.getText().toString().equalsIgnoreCase("")) {
                    mCommonMethods.callMobileNumber(txtcontactr.getText().toString(),context);
                }

            }
        });

        txtcontactm.setOnClickListener(new OnClickListener() {

            //@Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                if (!txtcontactm.getText().toString().equalsIgnoreCase("")) {
                    mCommonMethods.callMobileNumber(txtcontactm.getText().toString(),context);
                }

            }
        });

        txtemailid.setOnClickListener(new OnClickListener() {

            //@Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                if (!txtemailid.getText().toString().equalsIgnoreCase("")) {
                    Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                    emailIntent.setType("text/plain/email/dir");
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "");
                    emailIntent.putExtra(Intent.EXTRA_TEXT, "");
                    emailIntent.setData(Uri.parse("mailto:" + txtemailid.getText().toString()));
                    try {
                        startActivity(emailIntent);
                    } catch (android.content.ActivityNotFoundException e) {
                        Toast.makeText(context, "There are No Email Client Installed", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

        imgcontact_cust_r.setOnClickListener(new OnClickListener() {

            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if (!txtcontactr.getText().toString().equalsIgnoreCase("")) {
                    mCommonMethods.callMobileNumber(txtcontactr.getText().toString(),context);
                }
            }
        });

        imgcontact_cust_m.setOnClickListener(new OnClickListener() {

            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if (!txtcontactm.getText().toString().equalsIgnoreCase("")) {
                    mCommonMethods.callMobileNumber(txtcontactm.getText().toString(),context);
                }
            }
        });

        imgemail_cust.setOnClickListener(new OnClickListener() {

            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                if (!txtemailid.getText().toString().equalsIgnoreCase("")) {
                    Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                    emailIntent.setType("text/plain/email/dir");
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "");
                    emailIntent.putExtra(Intent.EXTRA_TEXT, "");
                    emailIntent.setData(Uri.parse("mailto:" + txtemailid.getText().toString()));
                    try {
                        startActivity(emailIntent);
                    } catch (android.content.ActivityNotFoundException e) {
                        Toast.makeText(context, "There are No Email Client Installed", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }
	
	/*
	 * it is download detail based on policy no from server.
	 */

    private void startDownloadCustomer() {
        taskCustomerDetail.execute("demo");
    }

    private void service_hits() {

        mProgressDialog = new ProgressDialog(context, ProgressDialog.THEME_HOLO_LIGHT);
        String Message = "Loading Please wait...";
        mProgressDialog.setMessage(Html.fromHtml("<font color='#00a1e3'><b>" + Message + "<b></font>"));
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setMax(100);
        mProgressDialog.show();

        String METHOD_NAME_SH = "saveSmartAdvisorServiceHits";
        String SOAP_ACTION_SH = "http://tempuri.org/saveSmartAdvisorServiceHits";
        ServiceHits service = new ServiceHits(context,
                mProgressDialog, NAMESPACE, URl, SOAP_ACTION_SH,
                METHOD_NAME_SH);
        service.execute();

    }

    @SuppressWarnings("deprecation")
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DIALOG_DOWNLOAD_PROGRESS:
                mProgressDialog = new ProgressDialog(context, ProgressDialog.THEME_HOLO_LIGHT);
                String Message = "Loading. Please wait...";
                mProgressDialog.setMessage(Html.fromHtml("<font color='#00a1e3'><b>" + Message + "<b></font>"));
                mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                mProgressDialog.setCancelable(true);


                mProgressDialog.setButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        taskCustomerDetail.cancel(true);

                        mProgressDialog.dismiss();
                    }
                });

                mProgressDialog.setMax(100);
                mProgressDialog.show();
                return mProgressDialog;

            default:
                return null;
        }
    }

    private void intereneterror() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.loading_window);
        TextView text = dialog.findViewById(R.id.txtalertheader);
        text.setText("Internet Connection Not Present,Try again..");
        Button dialogButton = dialog.findViewById(R.id.btnalert);
        dialogButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void servererror() {
        final Dialog dialog = new Dialog(CustomerDetailActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.loading_window);
        TextView text = dialog.findViewById(R.id.txtalertheader);
        text.setText("Server Not Responding,Try again..");
        Button dialogButton = dialog.findViewById(R.id.btnalert);
        dialogButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
	 	 
	 /*
		 * if Internet connection not present in device then it will show the alert.
		 */

    class DownloadFileAsyncCustomerDetail extends AsyncTask<String, String, String> {

        private volatile boolean running = true;

        @SuppressWarnings("deprecation")
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog(DIALOG_DOWNLOAD_PROGRESS);
        }

        @Override
        protected String doInBackground(String... aurl) {
            try {

                running = true;
                SoapObject request = null;


                request = new SoapObject(NAMESPACE, METHOD_NAME_CUSTOMER_DETAIL_AGENCY);

                request.addProperty("custId", strCustomerId);
                request.addProperty("strAgentNo", AgentCode);
                request.addProperty("strEmailId", Email);
                request.addProperty("strMobileNo", Mobile);
                request.addProperty("strAuthKey", Password.trim());

                SoapSerializationEnvelope envelope =
                        new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;

                envelope.setOutputSoapObject(request);

                // 	allowAllSSL();
                mCommonMethods.TLSv12Enable();

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);

                HttpTransportSE androidHttpTranport = new HttpTransportSE(URl);
                try {

                    String SOAP_ACTION_CUSTOMER_DETAIL_AGENCY = "http://tempuri.org/getCustomerDtls";
                    androidHttpTranport.call(SOAP_ACTION_CUSTOMER_DETAIL_AGENCY, envelope);

                    Object response = envelope.getResponse();

                    if (!response.toString().contentEquals("anyType{}")) {

                        SoapPrimitive sa = null;
                        try {
                            sa = (SoapPrimitive) envelope.getResponse();

                            String inputpolicylist = sa.toString();

                            ParseXML prsObj = new ParseXML();

                            inputpolicylist = prsObj.parseXmlTag(inputpolicylist, "CustomerDetails");

                            inputpolicylist = new ParseXML().parseXmlTag(inputpolicylist, "ScreenData");
                            strPolicyListErrorCOde = inputpolicylist;

                            if (strPolicyListErrorCOde == null) {
                                inputpolicylist = sa.toString();

                                inputpolicylist = prsObj.parseXmlTag(inputpolicylist, "CustomerDetails");


                                inputpolicylist = prsObj.parseXmlTag(inputpolicylist, "Table");

                                strFirstName = new ParseXML().parseXmlTag(inputpolicylist, "HOLDERPERSONFIRSTNAME");
                                strDOB = new ParseXML().parseXmlTag(inputpolicylist, "HOLDERPERSONDOB");
                                strCity = new ParseXML().parseXmlTag(inputpolicylist, "CONTACTCITY");
                                strAddress1 = new ParseXML().parseXmlTag(inputpolicylist, "CORRESPONDENCEADDRESS1");
                                strAddress2 = new ParseXML().parseXmlTag(inputpolicylist, "CORRESPONDENCEADDRESS2");
                                strPostCode = new ParseXML().parseXmlTag(inputpolicylist, "CORRESPONDENCEPOSTCODE");
                                strState = new ParseXML().parseXmlTag(inputpolicylist, "CORRESPONDENCESTATE");
                                strContactR = new ParseXML().parseXmlTag(inputpolicylist, "CONTACTRESIDENCE");
                                strContactM = new ParseXML().parseXmlTag(inputpolicylist, "CONTACTMOBILE");
                                strEmailId = new ParseXML().parseXmlTag(inputpolicylist, "EMAILID");

                                if (strDOB == null) {
                                    strDOB = "";
                                } else {
                                    strDOB = strDOB.split("T")[0];

                                    Date dt1 = null;
                                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                                    SimpleDateFormat df1 = new SimpleDateFormat("dd-MMMM-yyyy");
                                    try {
                                        dt1 = df.parse(strDOB);
                                    } catch (ParseException e) {
                                        // TODO Auto-generated catch block
                                        e.printStackTrace();
                                    }
                                    strDOB = df1.format(dt1);

                                }

                            } else {
                                //txterrordesc.setText("No Data");
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
                    } else {

                    }

                } catch (IOException | XmlPullParserException e) {
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
        protected void onProgressUpdate(String... progress) {
            Log.d("ANDRO_ASYNC", progress[0]);
            mProgressDialog.setProgress(Integer.parseInt(progress[0]));
        }

        @SuppressWarnings("deprecation")
        @Override
        protected void onPostExecute(String unused) {
            try {
                dismissDialog(DIALOG_DOWNLOAD_PROGRESS);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (running) {
                if (strPolicyListErrorCOde == null) {
                    txtfname.setText(strFirstName == null ? "" : strFirstName);
                    txtdob.setText(strDOB == null ? "" : strDOB);
                    txtcity.setText(strCity == null ? "" : strCity);
                    txtaddressone.setText(strAddress1 == null ? "" : strAddress1);
                    txtaddresstwo.setText(strAddress2 == null ? "" : strAddress2);
                    txtpostalcode.setText(strPostCode == null ? "" : strPostCode);
                    txtstate.setText(strState == null ? "" : strState);
                    txtcontactr.setText(strContactR == null ? "" : strContactR);
                    txtcontactm.setText(strContactM == null ? "" : strContactM);
                    txtemailid.setText(strEmailId == null ? "" : strEmailId);

                    if (txtcontactr.getText().toString().contentEquals("")) {
                        imgcontact_cust_r.setVisibility(View.GONE);
                    } else {
                        imgcontact_cust_r.setVisibility(View.VISIBLE);
                    }

                    if (txtcontactm.getText().toString().contentEquals("")) {
                        imgcontact_cust_m.setVisibility(View.GONE);
                    } else {
                        imgcontact_cust_m.setVisibility(View.VISIBLE);
                    }

                    if (txtemailid.getText().toString().contentEquals("")) {
                        imgemail_cust.setVisibility(View.GONE);
                    } else {
                        imgemail_cust.setVisibility(View.VISIBLE);
                    }


                } else {
                    txtfname.setText("");
                    txtdob.setText("");
                    txtcity.setText("");
                    txtaddressone.setText("");
                    txtaddresstwo.setText("");
                    txtpostalcode.setText("");
                    txtstate.setText("");
                    txtcontactr.setText("");
                    txtcontactm.setText("");
                    txtemailid.setText("");
                }
            } else {
                WeakReference<CustomerDetailActivity> mainActivityWeakRef = new WeakReference<>(CustomerDetailActivity.this);

                if (mainActivityWeakRef.get() != null && !mainActivityWeakRef.get().isFinishing()) {
                    servererror();
                }
            }
        }
    }
		
		/*
		 * while downloading data from server,if any data failed or not there
		 * then it will show alert
		 */

    class ServiceHits extends AsyncTask<String, Void, String> {
        final Context mContext;
        ProgressDialog progressDialog = null;
        String NAMESPACE = "";
        String URL = "";
        String SOAP_ACTION = "";
        String METHOD_NAME = "";

        ServiceHits(Context mContext, ProgressDialog progressDialog, String NAMESPACE, String URL, String SOAP_ACTION, String METHOD_NAME) {
            // TODO Auto-generated constructor stub

            this.NAMESPACE = NAMESPACE;
            this.URL = URL;
            this.SOAP_ACTION = SOAP_ACTION;
            this.METHOD_NAME = METHOD_NAME;
            this.mContext = mContext;
            this.progressDialog = progressDialog;
        }

        @Override
        protected String doInBackground(String... param) {
            // TODO Auto-generated method stub

            if (mCommonMethods.isNetworkConnected(context)) {

                try {

                    SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

                    request.addProperty("serviceName", METHOD_NAME_CUSTOMER_DETAIL_AGENCY);
                    request.addProperty("strProdCode", "");
                    request.addProperty("serviceInput", strCustomerId);
                    request.addProperty("serviceReqUserId", AgentCode);
                    request.addProperty("strEmailId", Email);
                    request.addProperty("strMobileNo", Mobile);
                    request.addProperty("strAuthKey", Password.trim());


                    SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                    //Enable this envelope if service is written in dot net
                    envelope.dotNet = true;
                    envelope.setOutputSoapObject(request);
                    HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

                    // 	allowAllSSL();
                    mCommonMethods.TLSv12Enable();

                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);

                    androidHttpTransport.call(SOAP_ACTION, envelope);
                    SoapPrimitive response = (SoapPrimitive) envelope.getResponse();

                    String result = response.toString();

                    if (result.contains("1")) {
                        return "Success";
                    } else {
                        return "Failure";
                    }

                } catch (Exception e) {
                    return "Server not Found. Please try after some time.";
                }

            } else
                return "Please Activate Internet connection";

        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

            try {
                if (progressDialog.isShowing())
                    progressDialog.dismiss();

            } catch (Exception e) {
                e.getMessage();
            }

            taskCustomerDetail = new DownloadFileAsyncCustomerDetail();
            startDownloadCustomer();

        }
    }
	
	/*
	 * based on https it will verify the ssl and allow data to display from server.
	 */


    //after 5 minute it will get log out
		/*private CountDownTimer mCountDown = new CountDownTimer(300000, 300000)
			{

			    @Override
			    public void onTick(long millisUntilFinished)
			    {

			    }


			    @Override
			    public void onFinish()
			    {
			        //show your dialog here
			    	mCommonMethods.logoutToLoginActivity(context);
			    }
			};  


			@Override
			public void onResume()
			{
			    super.onResume();

			    mCountDown.start();
			}  
			@Override
			public void onPause()
			{
			    super.onPause();

			    mCountDown.cancel();
			}  
			@Override
			public void onUserInteraction()
			{
			    super.onUserInteraction();

			    // user interact cancel the timer and restart to countdown to next interaction
			    mCountDown.cancel();
			    mCountDown.start();
			}*/
}
