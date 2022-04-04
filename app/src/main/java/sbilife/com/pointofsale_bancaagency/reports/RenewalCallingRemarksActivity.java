package sbilife.com.pointofsale_bancaagency.reports;

import android.annotation.SuppressLint;
import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Html;
import android.text.TextUtils;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.apache.commons.lang.StringUtils;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import sbilife.com.pointofsale_bancaagency.ParseXML;
import sbilife.com.pointofsale_bancaagency.ParseXML.XMLHolderAdvanceQueryResult;
import sbilife.com.pointofsale_bancaagency.R;
import sbilife.com.pointofsale_bancaagency.ServiceURL;
import sbilife.com.pointofsale_bancaagency.Utility;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;

public class RenewalCallingRemarksActivity extends AppCompatActivity implements ServiceHits.DownLoadData{

    private  final String NAMESPACE = "http://tempuri.org/";
    private final String URl = ServiceURL.SERVICE_URL;
    private  final String METHOD_NAME_ADVANCE_QUERY = "getCRMList";
    private CommonMethods mCommonMethods;
    private ProgressDialog progressDialog = null;
    private Context context;
    private String mobileNumber = "";
    private ListView listViewRenewalCallingScreen;
    private int positionClicked = -1;

    private List<XMLHolderAdvanceQueryResult> nodeData;
    private EditText edittextRenewalCallingMobileNumber;
    private SelectedAdapterRenewalCallingScrrens selectedAdapterRenewalCallingScrrens;
    private String dueDate,pAY_EX1_74;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*PAY_EX1_91                  Disposition
		PAY_EX1_96                  Sub Disposition
		HTMLTEXT_280             Call Centre Remarks
		PAY_EX1_95                  RAG
		PAY_EX1_94                  Alternate Contact Number 3
		Key                                    Renewal ID*/


        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.renewal_calling_screen);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);
        context = this;

        mCommonMethods = new CommonMethods();
        mCommonMethods.setApplicationToolbarMenu(this, "Renewal Calling");

        Intent intent = getIntent();


        listViewRenewalCallingScreen = findViewById(R.id.listViewRenewalCallingScreen);

        String objectType = intent.getStringExtra("objectType");
         dueDate = intent.getStringExtra("DueDate");
         pAY_EX1_74 = intent.getStringExtra("PAY_EX1_74");
        String statusCodeID = intent.getStringExtra("StatusCodeID");

        progressDialog = new ProgressDialog(context, ProgressDialog.THEME_HOLO_LIGHT);
        String Message = "Loading Please wait...";
        progressDialog.setMessage(Html.fromHtml("<font color='#00a1e3'><b>" + Message + "<b></font>"));
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.setMax(100);
        //buttonRenewalCallingOk.setOnClickListener(this);
        nodeData = new ArrayList<>();

        service_hits();

    }

    private void service_hits() {

        CommonMethods.UserDetailsValuesModel userDetails = mCommonMethods
                .setUserDetails(context);
        ServiceHits service = new ServiceHits(context, METHOD_NAME_ADVANCE_QUERY, dueDate + ","+pAY_EX1_74,
                userDetails.getStrCIFBDMUserId(), userDetails.getStrCIFBDMEmailId(),
                userDetails.getStrCIFBDMMObileNo() , userDetails.getStrCIFBDMPassword(), this);
        service.execute();

    }
    @Override
    public void downLoadData() {

        new DownloadFileAsyncAdvanceQuery(dueDate, pAY_EX1_74).execute();
    }

    class DownloadFileAsyncAdvanceQuery extends
            AsyncTask<String, String, String> {

        private volatile boolean running = true;
        private final String dueDate;
        private final String pAY_EX1_74;


        DownloadFileAsyncAdvanceQuery(String dueDate, String pAY_EX1_74) {
            this.dueDate = dueDate;
            this.pAY_EX1_74 = pAY_EX1_74;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }

        @SuppressLint("SimpleDateFormat")
        @Override
        protected String doInBackground(String... aurl) {
            try {

                running = true;
                SoapObject request;



                request = new SoapObject(NAMESPACE,
                        METHOD_NAME_ADVANCE_QUERY);
                String newDueDate = "";
                try {
                    String[] separated = dueDate.split("-");
                    String day = separated[0]; // this will contain "Fruit"
                    String month = mCommonMethods.getMonthNumber(separated[1]);
                    String year = separated[2];
                    int lastTwoDigits = Integer.parseInt(year) % 100;

                    newDueDate = day + "-" + month + "-" + lastTwoDigits;
                } catch (Exception e) {
                    e.printStackTrace();
                }


                request.addProperty("strPolicyNo", pAY_EX1_74);//"98989898");// pAY_EX1_74);
                request.addProperty("strDueDate",newDueDate);// "03-01-18");// newDueDate);


                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);
                envelope.dotNet = true;

                envelope.setOutputSoapObject(request);

                // allowAllSSL();
                mCommonMethods.TLSv12Enable();

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);

                HttpTransportSE androidHttpTranport = new HttpTransportSE(URl,300000);
                try {
                    String SOAP_ACTION_ADVANCE_QUERY = "http://tempuri.org/getCRMList";
                    androidHttpTranport.call(SOAP_ACTION_ADVANCE_QUERY,
                            envelope);

                    Object response = envelope.getResponse();

                    if (!response.toString().contentEquals("<NewDataSet /> ")) {
                            /*<NewDataSet>
                              <QueryResults>
                                <PAY_EX1_91>Move to Call Centre</PAY_EX1_91>
                                <PAY_EX1_96>Forward for structured follow up</PAY_EX1_96>
                                <HTMLTEXT_280>LastModified By: 1point1 LastModified On: 02-07-2018 7:51:24 PM&lt;br /&gt;
                                                    Customer contact no is updated&lt;br /&gt;</HTMLTEXT_280>
                                <PAY_EX1_95>AMBER</PAY_EX1_95>
                                <ROWNUMBER>1</ROWNUMBER>
                                <Key>45693</Key>
                              </QueryResults>
                            </NewDataSet>*/
                        //SoapPrimitive sa = null;
                        try {
                           // sa = (SoapPrimitive) envelope.getResponse();
                            String inputpolicylist = response.toString();

                            if (!inputpolicylist.equalsIgnoreCase("<NewDataSet /> ")) {

                                ParseXML parseXML = new ParseXML();
                                String DataResultXML = parseXML.parseXmlTag(
                                        inputpolicylist, "NewDataSet");
                                //DataResultXML = escapeXml(DataResultXML);
                                List<String> Node = parseXML.parseParentNode(
                                        DataResultXML, "QueryResults");
                                nodeData = parseXML
                                        .parseNodeAdvanceQueryResult(Node);

                            }

                        } catch (Exception e) {

                            progressDialog.dismiss();
                            running = false;
                        }
                    }

                } catch (IOException e) {

                    progressDialog.dismiss();
                    running = false;
                } catch (XmlPullParserException e) {
                    progressDialog.dismiss();
                    running = false;
                }
            } catch (Exception e) {
                progressDialog.dismiss();
                running = false;
            }
            return null;

        }

        @Override
        protected void onPostExecute(String unused) {
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }

            if (running) {

                if (nodeData.size() > 0) {
                    selectedAdapterRenewalCallingScrrens = new SelectedAdapterRenewalCallingScrrens(
                            context, nodeData);
                    selectedAdapterRenewalCallingScrrens.setNotifyOnChange(true);

                    listViewRenewalCallingScreen.setAdapter(selectedAdapterRenewalCallingScrrens);

                    Utility.setListViewHeightBasedOnChildren(listViewRenewalCallingScreen);
                } else {
                    //linearlayoutRenewalCallingScreen.setVisibility(View.GONE);
                    mCommonMethods.showMessageDialog(context, mCommonMethods.NO_RECORD_FOUND);
                }
            } else {
                mCommonMethods.showMessageDialog(context, mCommonMethods.NO_RECORD_FOUND);
            }
        }
    }

    class UpdateMobileNumberAsyncTask extends
            AsyncTask<String, String, String> {

        private volatile boolean running = true;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }

        @SuppressLint("SimpleDateFormat")
        @Override
        protected String doInBackground(String... aurl) {
            String message = "";


            try {

                running = true;
                SoapObject request;


                String METHOD_NAME_UPDATE_MOBILE_ADVANCE_QUERY = "updateCRMMobile";
                request = new SoapObject(NAMESPACE,
                        METHOD_NAME_UPDATE_MOBILE_ADVANCE_QUERY);

                request.addProperty("strMobileNo", mobileNumber);//"8776555432");// );
                request.addProperty("strPaymentKey",nodeData.get(0).getKey());//"68");
                System.out.println("nodeData.get(0).getKey() = " + nodeData.get(0).getKey());

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);
                envelope.dotNet = true;

                envelope.setOutputSoapObject(request);

                // allowAllSSL();
                mCommonMethods.TLSv12Enable();

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);

                HttpTransportSE androidHttpTranport = new HttpTransportSE(URl);
                try {
                    String SOAP_ACTION_UPDATE_MOBILE_ADVANCE_QUERY = "http://tempuri.org/updateCRMMobile";
                    androidHttpTranport.call(SOAP_ACTION_UPDATE_MOBILE_ADVANCE_QUERY,
                            envelope);

                    Object response = envelope.getResponse();

                    if (!response.toString().contentEquals("anyType{}")) {

                        SoapPrimitive sa;
                        try {
                            sa = (SoapPrimitive) envelope.getResponse();

                            String inputpolicylist = sa.toString();

                            if (inputpolicylist.equalsIgnoreCase("Success")) {
                                message = inputpolicylist;
                            }

                        } catch (Exception e) {
                            try {
                                throw (e);
                            } catch (Exception e1) {
                                e1.printStackTrace();
                            }
                            progressDialog.dismiss();
                            running = false;
                        }
                    }

                } catch (IOException e) {
                    progressDialog.dismiss();
                    running = false;
                } catch (XmlPullParserException e) {
                    progressDialog.dismiss();
                    running = false;
                }
            } catch (Exception e) {
                progressDialog.dismiss();
                running = false;
            }
            return message;

        }

        @Override
        protected void onPostExecute(String message) {
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }

            if (running) {

                if (message.equalsIgnoreCase("Success")) {
                    nodeData.get(positionClicked).setPAY_EX1_94(mobileNumber);

                    selectedAdapterRenewalCallingScrrens = new SelectedAdapterRenewalCallingScrrens(
                            context, nodeData);
                    selectedAdapterRenewalCallingScrrens.setNotifyOnChange(true);

                    listViewRenewalCallingScreen.setAdapter(selectedAdapterRenewalCallingScrrens);

                    Utility.setListViewHeightBasedOnChildren(listViewRenewalCallingScreen);

                    edittextRenewalCallingMobileNumber.setText("");
                    mobileNumber = "";
                    System.out.println("positionClicked = " + positionClicked);
                    mCommonMethods.showMessageDialog(context, "Mobile Number Updated Successfully");
                } else {
                    mCommonMethods.showMessageDialog(context, "Mobile Number Not Updated.Please Try Again.");
                }
            } else {
                mCommonMethods.showMessageDialog(context, mCommonMethods.SERVER_ERROR);
            }
        }
    }

    class SelectedAdapterRenewalCallingScrrens extends ArrayAdapter<XMLHolderAdvanceQueryResult> {

        // used to keep selected position in ListView

        final List<XMLHolderAdvanceQueryResult> lst;

        SelectedAdapterRenewalCallingScrrens(Context context,
                                             List<XMLHolderAdvanceQueryResult> objects) {
            super(context, 0, objects);

            lst = objects;
        }


        @SuppressLint("InflateParams")
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View v = convertView;
            // only inflate the view if it's null
            if (v == null) {
                LayoutInflater vi = (LayoutInflater) this.getContext()
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.list_item_renewal_calling, null);
            }


            Object obj = null;
            boolean i = lst.contains(null);

            if (!i) {
// get text view
                TextView textviewDisposition, textviewSubDisposition, textviewCallCentreRemarks, textviewAlternateContactNumberThreeTitle,textviewRAG, textviewAlternateContactNumberThree, textviewRenewalID;

                textviewDisposition = v.findViewById(R.id.textviewDisposition);
                textviewSubDisposition = v.findViewById(R.id.textviewSubDisposition);
                textviewCallCentreRemarks = v.findViewById(R.id.textviewCallCentreRemarks);
                textviewRAG = v.findViewById(R.id.textviewRAG);
                textviewAlternateContactNumberThree = v.findViewById(R.id.textviewAlternateContactNumberThree);
                textviewRenewalID = v.findViewById(R.id.textviewRenewalID);
                textviewAlternateContactNumberThreeTitle = v.findViewById(R.id.textviewAlternateContactNumberThreeTitle);

                textviewDisposition.setText(": " + nodeData.get(0).getPAY_EX1_91());
                textviewSubDisposition.setText(": " + nodeData.get(0).getPAY_EX1_96());


                String callCentreRemarks = StringUtils.substringBetween(nodeData.get(0).getHTMLTEXT_280(), "br", ";br");
                // /&gt;Customer contact no is updated&lt;br /&gt;
                callCentreRemarks = StringUtils.substringBetween(callCentreRemarks, "/&gt;", "&lt");

                WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
                Display display = wm.getDefaultDisplay();
                int screenWidth = display.getWidth(); // Get full screen width

                int eightyPercent = (screenWidth * 50) / 100;

                if(!TextUtils.isEmpty(callCentreRemarks)) {
                    textviewCallCentreRemarks.setText(": " + callCentreRemarks.trim());
                }else{
                    String remark = ": " +nodeData.get(0).getHTMLTEXT_280();
                    float textWidthPPF = textviewCallCentreRemarks.getPaint().measureText(remark);
                    float numberOflinesForPPF = (textWidthPPF / eightyPercent) + 0.7f;
                    textviewCallCentreRemarks.setLines(Math.round(numberOflinesForPPF));
                    textviewCallCentreRemarks.setText(remark);
                }

                textviewRAG.setText(": " + nodeData.get(0).getPAY_EX1_95());
                textviewAlternateContactNumberThree.setText(": " + nodeData.get(0).getPAY_EX1_94());
                textviewRenewalID.setText(": " + nodeData.get(0).getKey());

                edittextRenewalCallingMobileNumber = v.findViewById(R.id.edittextRenewalCallingMobileNumber);
                Button buttonRenewalCallingOk = v.findViewById(R.id.buttonRenewalCallingOk);


                String alterNateContact = "Alternate Contact Number 3 ";
                float  textWidthPPF = textviewAlternateContactNumberThreeTitle.getPaint().measureText(alterNateContact);
                float  numberOflinesForPPF = (textWidthPPF / eightyPercent) + 0.7f;

                textviewAlternateContactNumberThreeTitle.setLines(Math.round(numberOflinesForPPF));
                textviewAlternateContactNumberThreeTitle.setText(alterNateContact);

                buttonRenewalCallingOk.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View view) {

                        new CommonMethods().hideKeyboard(edittextRenewalCallingMobileNumber, context);
                        if (mCommonMethods.isNetworkConnected(context)) {
                            mobileNumber = edittextRenewalCallingMobileNumber.getText()
                                    .toString();
                            boolean isvalid = mCommonMethods.mobileNumberPatternValidation(
                                    edittextRenewalCallingMobileNumber, context);
                            positionClicked = position;
                            if (isvalid) {
                                new UpdateMobileNumberAsyncTask().execute();
                            } else {
                                mCommonMethods.showMessageDialog(context,
                                        "Please Enter Valid Mobile Number");
                            }
                        } else {
                            mCommonMethods.showMessageDialog(context,
                                    mCommonMethods.NO_INTERNET_MESSAGE);
                        }

                    }
                });


            }

            return (v);
        }
    }
}
