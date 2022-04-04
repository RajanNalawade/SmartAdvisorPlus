package sbilife.com.pointofsale_bancaagency.reports;

import androidx.appcompat.app.AppCompatActivity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Html;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

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
import sbilife.com.pointofsale_bancaagency.R;
import sbilife.com.pointofsale_bancaagency.ServiceURL;
import sbilife.com.pointofsale_bancaagency.Utility;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;

public class UnderwrittingActivity extends AppCompatActivity implements View.OnClickListener, ServiceHits.DownLoadData{

    private final int DIALOG_DOWNLOAD_PROGRESS = 0;

    private  final String METHOD_NAME_GROUP_UNDERWRITTING = "getUnderwriting";

    private EditText edtGroupUnderwrittingPolicyNo;

    private TextView txterrordesGroupUnderwritting,txtGroupUnderwrittingListCount;
    private ListView lvGroupUnderwrittingList;

    private Context context;
    private CommonMethods commonMethods;

    private ProgressDialog mProgressDialog;

    private SelectedAdapterGroupUnderwritting selectedAdapterGroupFundClaim;

    private long lstGroupUnderwrittingListCount = 0;

    private DownloadFileAsyncGroupUnderwritting taskAsyncGroupFundClaim;

    private String policyNumber = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_underwritting);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);

        context = this;
        commonMethods = new CommonMethods();
        commonMethods.setApplicationToolbarMenu(this,"Group Under Writing");

        edtGroupUnderwrittingPolicyNo = findViewById(R.id.edtGroupUnderwrittingPolicyNo);

        Button btnGroupUnderwrittingOk = findViewById(R.id.btnGroupUnderwrittingOk);
        Button btnGroupUnderwrittingPolicyNoReset = findViewById(R.id.btnGroupUnderwrittingPolicyNoReset);

        btnGroupUnderwrittingOk.setOnClickListener(this);
        btnGroupUnderwrittingPolicyNoReset.setOnClickListener(this);

        txterrordesGroupUnderwritting  = findViewById(R.id.txterrordesGroupUnderwritting);
        txtGroupUnderwrittingListCount  = findViewById(R.id.txtGroupUnderwrittingListCount);
        lvGroupUnderwrittingList   = findViewById(R.id.lvGroupUnderwrittingList);
    }

    @Override
    @Deprecated
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        switch (id) {
            case DIALOG_DOWNLOAD_PROGRESS:
                mProgressDialog = new ProgressDialog(this,
                        ProgressDialog.THEME_HOLO_LIGHT);
                String Message = "Loading Please wait...";
                mProgressDialog.setMessage(Html
                        .fromHtml("<font color='#00a1e3'><b>" + Message
                                + "<b></font>"));
                mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                mProgressDialog.setCancelable(true);

                mProgressDialog.setButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                //taskPolicyList.cancel(true);
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

    @SuppressWarnings("deprecation")
    private void dismissProgressDialog() {
        dismissDialog(DIALOG_DOWNLOAD_PROGRESS);
    }

    @SuppressWarnings("deprecation")
    private void showProgressDialog() {
        showDialog(DIALOG_DOWNLOAD_PROGRESS);
    }


    class DownloadFileAsyncGroupUnderwritting extends AsyncTask<String, String, String> {

        private volatile boolean running = true;
        private String inputpolicylist = "",strGroupNewBusinessListErrorCOde = "";
        @Override
        protected void onPreExecute() {
            showProgressDialog();
        }

        //98001001103
        @Override
        protected String doInBackground(String... params) {
            try {

                running = true;
                SoapObject request;

                String NAMESPACE = "http://tempuri.org/";
                request = new SoapObject(NAMESPACE,METHOD_NAME_GROUP_UNDERWRITTING);
                request.addProperty("strPolNo", policyNumber);
                request.addProperty("strCode", commonMethods.setUserDetails(context).getStrCIFBDMUserId());
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);
                envelope.dotNet = true;

                envelope.setOutputSoapObject(request);

                commonMethods.TLSv12Enable();

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);

                String URl = ServiceURL.SERVICE_URL;
                HttpTransportSE androidHttpTranport = new HttpTransportSE(URl);
                try {

                    String SOAP_ACTION_GROUP_UNDERWRITTING = "http://tempuri.org/getUnderwriting";
                    androidHttpTranport.call(
                            SOAP_ACTION_GROUP_UNDERWRITTING, envelope);
                    Object response = envelope.getResponse();

                    if (!response.toString().contentEquals("anyType{}")) {

                        SoapPrimitive sa;
                        try {
                            sa = (SoapPrimitive) envelope.getResponse();

                            inputpolicylist = sa.toString();

                            System.out.println("inputpolicylist = [" + inputpolicylist + "]");
                            ParseXML prsObj = new ParseXML();

                            inputpolicylist = prsObj.parseXmlTag(
                                    inputpolicylist, "CIFPolicyList");
                            inputpolicylist = new ParseXML().parseXmlTag(
                                    inputpolicylist, "ScreenData");
                            strGroupNewBusinessListErrorCOde = inputpolicylist;

                            if (strGroupNewBusinessListErrorCOde == null) {
                                inputpolicylist = sa.toString();
                                inputpolicylist = prsObj.parseXmlTag(
                                        inputpolicylist, "CIFPolicyList");

                                List<String> Node = prsObj.parseParentNode(
                                        inputpolicylist, "Table");

                                List<ParseXML.XMLHolderGroupUnderritting> nodeData = prsObj
                                        .parseNodeGroupUnderwritting(Node);

                                // final List<XMLHolderMaturity> lst;
                                List<ParseXML.XMLHolderGroupUnderritting> lstGroupUnderwritting = new ArrayList<>();
                                lstGroupUnderwritting.clear();

                                lstGroupUnderwritting.addAll(nodeData);

                                lstGroupUnderwrittingListCount = lstGroupUnderwritting.size();
                                selectedAdapterGroupFundClaim = new SelectedAdapterGroupUnderwritting(
                                        context, lstGroupUnderwritting);
                                selectedAdapterGroupFundClaim.setNotifyOnChange(true);

                                registerForContextMenu(lvGroupUnderwrittingList);

                            }  // txterrordesc.setText("No Data");


                        } catch (Exception e) {
                            try {
                                throw (e);
                            } catch (Exception e1) {
                                e1.printStackTrace();
                            }
                            mProgressDialog.dismiss();
                            running = false;
                        }
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
            // TODO Auto-generated method stub
            if (mProgressDialog.isShowing()) {
                dismissProgressDialog();
            }
            //lvMaturityList.setVisibility(View.GONE);
            if (running) {

                //txterrordescmaturity.setVisibility(View.VISIBLE);
                //txtmaturitylistcount.setVisibility(View.VISIBLE);
                if (strGroupNewBusinessListErrorCOde == null) {
                    // ll_search_GroupTermRenewal.setVisibility(View.VISIBLE);
                    lvGroupUnderwrittingList.setVisibility(View.VISIBLE);

                    txterrordesGroupUnderwritting.setText("");
                    txtGroupUnderwrittingListCount.setText("Total Policy : "
                            + lstGroupUnderwrittingListCount);
                    lvGroupUnderwrittingList.setAdapter(selectedAdapterGroupFundClaim);

                    Utility.setListViewHeightBasedOnChildren(lvGroupUnderwrittingList);

                    /*imageButtonSearchByDate
                            .setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View arg0) {
                                    datecheck = 5;
                                    showDateProgressDialog();
                                }
                            });*/


                } else {
                    //ll_search_GroupTermRenewal.setVisibility(View.GONE);

                    txterrordesGroupUnderwritting.setText("No Record Found");
                    txtGroupUnderwrittingListCount.setText("Total Policy : " + 0);
                    List<ParseXML.XMLHolderGroupUnderritting> lst;
                    lst = new ArrayList<>();
                    lst.clear();
                    lst.add(null);
                    selectedAdapterGroupFundClaim = new SelectedAdapterGroupUnderwritting(
                            context, lst);
                    selectedAdapterGroupFundClaim.setNotifyOnChange(true);
                    lvGroupUnderwrittingList.setAdapter(selectedAdapterGroupFundClaim);

                    Utility.setListViewHeightBasedOnChildren(lvGroupUnderwrittingList);
                }
            } else {
                commonMethods.showMessageDialog(context, commonMethods.WEEK_INTERNET_MESSAGE);
            }
        }
    }

    class SelectedAdapterGroupUnderwritting extends ArrayAdapter<ParseXML.XMLHolderGroupUnderritting> {


        final List<ParseXML.XMLHolderGroupUnderritting> lst;

        SelectedAdapterGroupUnderwritting(Context context,
                                          List<ParseXML.XMLHolderGroupUnderritting> objects) {
            super(context, 0, objects);
            lst = objects;
        }


        @SuppressWarnings("deprecation")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            // only inflate the view if it's null
            if (v == null) {
                LayoutInflater vi = (LayoutInflater) this.getContext()
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.group_fund_claim_list_item, null);
            }

			/*POLICY_NO = "", POLICY_HOLDER_NAME = "", POL_START_DATE= "",
					RENEWAL_DUE_DATE = "",POLICY_STATUS;*/

            TextView txtTitleID = v.findViewById(R.id.txtTitleID);
            TextView txtTitleName = v.findViewById(R.id.txtTitleName);
            TextView txtTitleThird = v.findViewById(R.id.txtTitleThird);
            TextView txtTitleForth = v.findViewById(R.id.txtTitleForth);

            txtTitleID.setText("Member ID:");
            txtTitleName.setText("Member Name:");
            txtTitleThird.setText("Requirement Raised:");

            // get text view
            TextView textviewMemberId = v.findViewById(R.id.textviewMemberId);
            TextView textviewMemberName = v.findViewById(R.id.textviewMemberName);
            TextView textviewClaimSanctionDate = v.findViewById(R.id.textviewClaimSanctionDate);
            TextView textviewClaimAmount = v.findViewById(R.id.textviewClaimAmount);

            txtTitleForth.setVisibility(View.GONE);
            textviewClaimAmount.setVisibility(View.GONE);

            boolean i = lst.contains(null);

            if (!i) {

                textviewMemberId.setText(lst.get(position).getMember_id() == null ? ""
                        : lst.get(position).getMember_id());

                WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
                Display display = wm.getDefaultDisplay();
                int screenWidth = display.getWidth(); // Get full screen width

                int eightyPercent = (screenWidth * 50) / 100;

                String memberName = lst.get(position).getMember_name() == null ? ""
                        : lst.get(position).getMember_name();

                textviewMemberName.setText(memberName);

                float textWidthLifeAssuredName = textviewMemberName.getPaint().measureText(memberName);

                float linesFloat = (textWidthLifeAssuredName/eightyPercent) + 0.7f;

                textviewMemberName.setLines(Math.round(linesFloat));

                String raisedRequirement = lst.get(position).getRequirement_raised() == null ? ""
                        : lst.get(position).getRequirement_raised();

                textviewClaimSanctionDate.setText(raisedRequirement);

                /*float txtRaisedLines = textviewMemberName.getPaint().measureText(raisedRequirement);
                float raisedLines = (txtRaisedLines/eightyPercent) + 0.7f;

                textviewClaimSanctionDate.setLines(Math.round(raisedLines));*/
            }

            return (v);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnGroupUnderwrittingOk:

                lvGroupUnderwrittingList.setVisibility(View.GONE);
                txterrordesGroupUnderwritting.setVisibility(View.GONE);
                txtGroupUnderwrittingListCount.setVisibility(View.GONE);
                txterrordesGroupUnderwritting.setText("");
                txterrordesGroupUnderwritting.setText("");
                if(selectedAdapterGroupFundClaim!=null)
                {
                    selectedAdapterGroupFundClaim.clear();
                }
                getGroupUnderwrittingList();
                break;
            case R.id.btnGroupUnderwrittingPolicyNoReset:
                txterrordesGroupUnderwritting.setText("");
                txterrordesGroupUnderwritting.setText("");
                resetFields();
                break;

            default:
                break;
        }
    }

    private void resetFields() {

        lvGroupUnderwrittingList.setVisibility(View.GONE);
        txterrordesGroupUnderwritting.setVisibility(View.GONE);
        txtGroupUnderwrittingListCount.setVisibility(View.GONE);
        //ll_search_GroupTermRenewal.setVisibility(View.GONE);
        if(selectedAdapterGroupFundClaim!=null)
        {
            selectedAdapterGroupFundClaim.clear();
        }

        txterrordesGroupUnderwritting.setText("");
        txtGroupUnderwrittingListCount.setText("");
    }

    private void getGroupUnderwrittingList() {
        taskAsyncGroupFundClaim = new DownloadFileAsyncGroupUnderwritting();
        txtGroupUnderwrittingListCount.setVisibility(View.VISIBLE);

        policyNumber = edtGroupUnderwrittingPolicyNo.getText().toString();

        if (!policyNumber.equals("")){
            if (commonMethods.isNetworkConnected(context)) {

                CommonMethods.UserDetailsValuesModel userDetailsValuesModel = commonMethods.setUserDetails(context);
                String strCIFBDMUserId = userDetailsValuesModel.getStrCIFBDMUserId();
                String strCIFBDMEmailId = userDetailsValuesModel.getStrCIFBDMEmailId();
                String strCIFBDMPassword = userDetailsValuesModel.getStrCIFBDMPassword();
                String  strCIFBDMMObileNo = userDetailsValuesModel.getStrCIFBDMMObileNo();

                ServiceHits service = new ServiceHits(context,  METHOD_NAME_GROUP_UNDERWRITTING, policyNumber,
                        strCIFBDMUserId, strCIFBDMEmailId, strCIFBDMMObileNo,
                        strCIFBDMPassword, this);
                service.execute();

            } else {
                commonMethods.showMessageDialog(context,
                        "Internet Connection Not Present,Try again..");
            }
        }else
            commonMethods.showToast(context, "Please enter policy no.");
    }

    @Override
    public void downLoadData() {
        taskAsyncGroupFundClaim = new DownloadFileAsyncGroupUnderwritting();
        taskAsyncGroupFundClaim.execute();
    }
}
