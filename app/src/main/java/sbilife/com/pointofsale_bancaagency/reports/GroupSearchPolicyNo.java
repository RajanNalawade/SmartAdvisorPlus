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

public class GroupSearchPolicyNo extends AppCompatActivity implements View.OnClickListener, ServiceHits.DownLoadData{

    private  final String NAMESPACE = "http://tempuri.org/";
    private  final String URl = ServiceURL.SERVICE_URL;

    private  final String METHOD_NAME_GROUP_POLICY_SEARCH = "MasterPolicySearch_Groups";

    private Context mContext;
    private CommonMethods mCommonMethods;

    private EditText edtGroupPolicySearch;
    private TextView txtGroupPolicySearchMsg;
    private ListView lvGroupPolicySearchList;

    private final int DIALOG_DOWNLOAD_PROGRESS = 0;
    private ProgressDialog mProgressDialog;

    private List<ParseXML.XMLHolderGroupPolicySearch> lstGroupPolicySearch;
    private SelectedAdapterGroupPolicySearch selectedAdapterGroupPolicySearch;

    private AsyncGroupPolicySearch taskAsyncGroupPolicySearch;

    private String strPolicyNumber = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_group_search_policy_no);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);

        initialization();
    }

    private void initialization(){

        mCommonMethods = new CommonMethods();
        mContext = this;

        mCommonMethods.setApplicationToolbarMenu(this,"Group Master Policy Search"); ;

        edtGroupPolicySearch = findViewById(R.id.edtGroupPolicySearch);
        Button btnGroupPolicySearchOk = findViewById(R.id.btnGroupPolicySearchOk);
        Button btnGroupPolicySearchReset = findViewById(R.id.btnGroupPolicySearchReset);
        txtGroupPolicySearchMsg  = findViewById(R.id.txtGroupPolicySearchMsg);
        lvGroupPolicySearchList   = findViewById(R.id.lvGroupPolicySearchList);

        btnGroupPolicySearchOk.setOnClickListener(this);
        btnGroupPolicySearchReset.setOnClickListener(this);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
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

    class AsyncGroupPolicySearch extends AsyncTask<String, String, String> {

        private volatile boolean running = true;
        private String inputpolicylist = "",strGroupNewBusinessListErrorCOde = "";
        @Override
        protected void onPreExecute() {
            showDialog(DIALOG_DOWNLOAD_PROGRESS);
        }

        //98001001103
        @Override
        protected String doInBackground(String... params) {
            try {

                running = true;
                SoapObject request = null;

                request = new SoapObject(NAMESPACE,METHOD_NAME_GROUP_POLICY_SEARCH);
                request.addProperty("strPolicyNo", strPolicyNumber);
                request.addProperty("strCode", mCommonMethods.setUserDetails(mContext).getStrCIFBDMUserId());
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

                    String SOAP_ACTION_GROUP_POLICY_SEARCH = "http://tempuri.org/MasterPolicySearch_Groups";
                    androidHttpTranport.call(
                            SOAP_ACTION_GROUP_POLICY_SEARCH, envelope);
                    Object response = envelope.getResponse();

                    if (!response.toString().contentEquals("anyType{}")) {

                        SoapPrimitive sa = null;
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

                                List<ParseXML.XMLHolderGroupPolicySearch> nodeData = prsObj
                                        .parseNodeGroupPolicySearch(Node);

                                // final List<XMLHolderMaturity> lst;
                                lstGroupPolicySearch = new ArrayList<ParseXML.XMLHolderGroupPolicySearch>();
                                lstGroupPolicySearch.clear();

                                for (ParseXML.XMLHolderGroupPolicySearch node : nodeData) {

                                    lstGroupPolicySearch.add(node);
                                }

                                selectedAdapterGroupPolicySearch = new SelectedAdapterGroupPolicySearch(
                                        mContext, 0, lstGroupPolicySearch);
                                selectedAdapterGroupPolicySearch.setNotifyOnChange(true);

                                registerForContextMenu(lvGroupPolicySearchList);

                            } else {
                                // txterrordesc.setText("No Data");
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
                dismissDialog(DIALOG_DOWNLOAD_PROGRESS);
            }
            //lvMaturityList.setVisibility(View.GONE);
            if (running) {

                //txterrordescmaturity.setVisibility(View.VISIBLE);
                //txtmaturitylistcount.setVisibility(View.VISIBLE);
                if (strGroupNewBusinessListErrorCOde == null) {
                    // ll_search_GroupTermRenewal.setVisibility(View.VISIBLE);
                    lvGroupPolicySearchList.setVisibility(View.VISIBLE);

                    txtGroupPolicySearchMsg.setText("Total Policy : "
                            + lstGroupPolicySearch.size());
                    lvGroupPolicySearchList.setAdapter(selectedAdapterGroupPolicySearch);

                    Utility.setListViewHeightBasedOnChildren(lvGroupPolicySearchList);

                } else {
                    //ll_search_GroupTermRenewal.setVisibility(View.GONE);

                    txtGroupPolicySearchMsg.setText("No Record Found" +"\n"+"Total Policy : " + 0);
                    List<ParseXML.XMLHolderGroupPolicySearch> lst = new ArrayList<ParseXML.XMLHolderGroupPolicySearch>();
                    ParseXML.XMLHolderGroupPolicySearch node = null;
                    lst.add(node);
                    selectedAdapterGroupPolicySearch = new SelectedAdapterGroupPolicySearch(
                            mContext, 0, lst);
                    selectedAdapterGroupPolicySearch.setNotifyOnChange(true);
                    lvGroupPolicySearchList.setAdapter(selectedAdapterGroupPolicySearch);

                    Utility.setListViewHeightBasedOnChildren(lvGroupPolicySearchList);
                }
            } else {
                mCommonMethods.showMessageDialog(mContext, mCommonMethods.WEEK_INTERNET_MESSAGE);
            }
        }
    }

    class SelectedAdapterGroupPolicySearch extends ArrayAdapter<ParseXML.XMLHolderGroupPolicySearch> {

        // used to keep selected position in ListView
        private int selectedPos = -1; // init value for not-selected

        List<ParseXML.XMLHolderGroupPolicySearch> lst;

        SelectedAdapterGroupPolicySearch(Context context, int textViewResourceId,
                                         List<ParseXML.XMLHolderGroupPolicySearch> objects) {
            super(context, textViewResourceId, objects);
            lst = objects;
        }

        public void setSelectedPosition(int pos) {
            selectedPos = pos;
            // inform the view of this change
            notifyDataSetChanged();
        }

        public int getSelectedPosition() {
            return selectedPos;
        }

        @SuppressWarnings("deprecation")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            // only inflate the view if it's null
            if (v == null) {
                LayoutInflater vi = (LayoutInflater) this.getContext()
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.group_search_common_row, null);
            }

            TextView txtTitleFirst = v.findViewById(R.id.txtTitleFirst);
            TextView txtTitleSecond = v.findViewById(R.id.txtTitleSecond);
            TextView txtTitleThird = v.findViewById(R.id.txtTitleThird);
            TextView txtTitleForth = v.findViewById(R.id.txtTitleForth);
            TextView txtTitleFifth = v.findViewById(R.id.txtTitleFifth);
            TextView txtTitleSixth = v.findViewById(R.id.txtTitleSixth);
            TextView txtTitleSeven = v.findViewById(R.id.txtTitleSeven);
            TextView txtTitleEighth = v.findViewById(R.id.txtTitleEighth);
            TextView txtTitleEighth1 = v.findViewById(R.id.txtTitleEighth1);
            TextView txtTitleNinth = v.findViewById(R.id.txtTitleNinth);
            TextView txtTitleTenth = v.findViewById(R.id.txtTitleTenth);
            TextView txtTitleEleven = v.findViewById(R.id.txtTitleEleven);
            TextView txtTitleTwelth = v.findViewById(R.id.txtTitleTwelth);
            TextView txtTitleThirteen = v.findViewById(R.id.txtTitleThirteen);

            txtTitleFirst.setText("Policy No.:");
            txtTitleSecond.setText("Policy Holder Name.:");
            txtTitleThird.setText("DOC:");
            txtTitleForth.setText("ARD:");
            txtTitleFifth.setText("Total Premium:");
            txtTitleSixth.setText("Active Lives:");
            txtTitleSeven.setText("Unadjusted Deposit:");
            txtTitleEighth.setText("Rider/Option Opted:");
            txtTitleEighth1.setText("Free Cover Limit");
            txtTitleNinth.setText("Policy Status:");
            txtTitleTenth.setText("Basic Premium Rate:");
            txtTitleEleven.setText("Rider Premium Rate:");
            txtTitleTwelth.setText("Total Sum Assured:");
            txtTitleThirteen.setText("Type Of Scheme:");

            // get text view
            TextView textviewPolicyNo = v.findViewById(R.id.textviewPolicyNo);
            TextView textviewHolderName = v.findViewById(R.id.textviewHolderName);
            TextView textviewDOC = v.findViewById(R.id.textviewDOC);
            TextView textviewARD = v.findViewById(R.id.textviewARD);
            TextView textviewTotalPremium = v.findViewById(R.id.textviewTotalPremium);
            TextView textviewLives = v.findViewById(R.id.textviewLives);
            TextView textviewUnadjustedDeposit = v.findViewById(R.id.textviewUnadjustedDeposit);
            TextView textviewRiderOption = v.findViewById(R.id.textviewRiderOption);
            TextView textviewFCL = v.findViewById(R.id.textviewFCL);
            TextView textviewPolicyStatus = v.findViewById(R.id.textviewPolicyStatus);
            TextView textviewBasicPremiumRate = v.findViewById(R.id.textviewBasicPremiumRate);
            TextView textviewRiderPremiumRate = v.findViewById(R.id.textviewRiderPremiumRate);
            TextView textviewTotalSumAssured = v.findViewById(R.id.textviewTotalSumAssured);
            TextView textviewTypeScheme = v.findViewById(R.id.textviewTypeScheme);

            Object obj = null;
            boolean i = lst.contains(obj);

            if (!i) {

                textviewPolicyNo.setText(lst.get(position).getPolicy_no() == null ? ""
                        : lst.get(position).getPolicy_no());

                WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
                Display display = wm.getDefaultDisplay();
                int screenWidth = display.getWidth(); // Get full screen width

                int eightyPercent = (screenWidth * 50) / 100;

                String name = lst.get(position).getPolicy_holder() == null ? ""
                        : lst.get(position).getPolicy_holder();

                textviewHolderName.setText(name);

                float textWidthLifeAssuredName = textviewHolderName.getPaint().measureText(name);

                float linesFloat = (textWidthLifeAssuredName/eightyPercent) + 0.7f;

                textviewHolderName.setLines(Math.round(linesFloat));

                textviewDOC.setText(lst.get(position).getStr_doc() == null ? ""
                        : lst.get(position).getStr_doc());

                textviewARD.setText(lst.get(position).getStrARD() == null ? ""
                        : lst.get(position).getStrARD());

                textviewTotalPremium.setText(lst.get(position).getTotal_premium() == null ? ""
                        : lst.get(position).getTotal_premium());

                textviewLives.setText(lst.get(position).getLives() == null ? ""
                        : lst.get(position).getLives());

                textviewUnadjustedDeposit.setText(lst.get(position).getUnadjusted_deposite() == null ? ""
                        : lst.get(position).getUnadjusted_deposite());

                textviewRiderOption.setText(lst.get(position).getRideropted() == null ? ""
                        : lst.get(position).getRideropted());

                textviewFCL.setText(lst.get(position).getStr_fcl() == null ? ""
                        : lst.get(position).getStr_fcl());

                textviewPolicyStatus.setText(lst.get(position).getPolicy_status() == null ? ""
                        : lst.get(position).getPolicy_status());

                textviewBasicPremiumRate.setText(lst.get(position).getBasic_premium_rate() == null ? ""
                        : lst.get(position).getBasic_premium_rate());

                textviewRiderPremiumRate.setText(lst.get(position).getRider_premium_rate() == null ? ""
                        : lst.get(position).getRider_premium_rate());

                textviewTotalSumAssured.setText(lst.get(position).getTotal_sum_assured() == null ? ""
                        : lst.get(position).getTotal_sum_assured());

                textviewTypeScheme.setText(lst.get(position).getType_scheme() == null ? ""
                        : lst.get(position).getType_scheme());
            }

            return (v);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnGroupPolicySearchOk:

                lvGroupPolicySearchList.setVisibility(View.GONE);
                txtGroupPolicySearchMsg.setVisibility(View.GONE);
                if(selectedAdapterGroupPolicySearch!=null)
                {
                    selectedAdapterGroupPolicySearch.clear();
                }
                getGroupPolicySearchList();
                break;
            case R.id.btnGroupPolicySearchReset:
                txtGroupPolicySearchMsg.setText("");
                resetFields();
                break;

            default:
                break;
        }
    }

    private void resetFields() {

        lvGroupPolicySearchList.setVisibility(View.GONE);
        txtGroupPolicySearchMsg.setVisibility(View.GONE);
        //ll_search_GroupTermRenewal.setVisibility(View.GONE);
        if(selectedAdapterGroupPolicySearch!=null)
        {
            selectedAdapterGroupPolicySearch.clear();
        }

        txtGroupPolicySearchMsg.setText("");
    }

    private void getGroupPolicySearchList() {
        taskAsyncGroupPolicySearch = new AsyncGroupPolicySearch();
        txtGroupPolicySearchMsg.setVisibility(View.VISIBLE);

        strPolicyNumber = edtGroupPolicySearch.getText().toString();

        if (!strPolicyNumber.equals("")){
            if (mCommonMethods.isNetworkConnected(mContext)) {

                CommonMethods.UserDetailsValuesModel userDetailsValuesModel = mCommonMethods.setUserDetails(mContext);
                String strCIFBDMUserId = userDetailsValuesModel.getStrCIFBDMUserId();
                String strCIFBDMEmailId = userDetailsValuesModel.getStrCIFBDMEmailId();
                String strCIFBDMPassword = userDetailsValuesModel.getStrCIFBDMPassword();
                String  strCIFBDMMObileNo = userDetailsValuesModel.getStrCIFBDMMObileNo();

                ServiceHits service = new ServiceHits(mContext, METHOD_NAME_GROUP_POLICY_SEARCH, strPolicyNumber,
                        strCIFBDMUserId, strCIFBDMEmailId, strCIFBDMMObileNo,
                        strCIFBDMPassword, this);
                service.execute();

            } else {
                mCommonMethods.showMessageDialog(mContext,
                        "Internet Connection Not Present,Try again..");
            }
        }else
            mCommonMethods.showToast(mContext, "Please enter policy no.");
    }

    @Override
    public void downLoadData() {
        taskAsyncGroupPolicySearch = new AsyncGroupPolicySearch();
        taskAsyncGroupPolicySearch.execute();
    }
}
