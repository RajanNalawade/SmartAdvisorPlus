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

public class GroupTermInsuQuoteSearchActivity extends AppCompatActivity implements View.OnClickListener, ServiceHits.DownLoadData{

    private  final String NAMESPACE = "http://tempuri.org/";
    private  final String URl = ServiceURL.SERVICE_URL;

    private  final String METHOD_NAME_GROUP_QUOTE_ID_SEARCH = "QuotIDSearch_group";

    private Context mContext;
    private CommonMethods mCommonMethods;

    private EditText edtGroupQuoteSearch;
    private TextView txtGroupQuoteSearchMsg;
    private ListView lvGroupQuoteSearchList;

    private final int DIALOG_DOWNLOAD_PROGRESS = 0;
    private ProgressDialog mProgressDialog;

    private List<ParseXML.XMLHolderGroupQuoteIDSearch> lstGroupQuoteIDSearch;
    private SelectedAdapterGroupQuoteIDSearch selectedAdapterGroupQuoteIDSearch;

    private AsyncGroupQuoteIDSearch taskAsyncGroupQuoteIDSearch;

    private String strQuoteNumber = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_group_term_insu_quote_search);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);

        initialization();
    }

    private void initialization(){

        mCommonMethods = new CommonMethods();
        mContext = this;

        mCommonMethods.setApplicationToolbarMenu(this,"Group Quote ID Search");

        edtGroupQuoteSearch = findViewById(R.id.edtGroupQuoteSearch);
        Button btnGroupQuoteSearchOk = findViewById(R.id.btnGroupQuoteSearchOk);
        Button btnGroupQuoteSearchReset = findViewById(R.id.btnGroupQuoteSearchReset);
        txtGroupQuoteSearchMsg  = findViewById(R.id.txtGroupQuoteSearchMsg);
        lvGroupQuoteSearchList   = findViewById(R.id.lvGroupQuoteSearchList);

        btnGroupQuoteSearchOk.setOnClickListener(this);
        btnGroupQuoteSearchReset.setOnClickListener(this);
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

    class AsyncGroupQuoteIDSearch extends AsyncTask<String, String, String> {

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

                request = new SoapObject(NAMESPACE,METHOD_NAME_GROUP_QUOTE_ID_SEARCH);
                request.addProperty("strQuot", strQuoteNumber);
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

                    String SOAP_ACTION_GROUP_QUOTE_ID_SEARCH = "http://tempuri.org/QuotIDSearch_group";
                    androidHttpTranport.call(
                            SOAP_ACTION_GROUP_QUOTE_ID_SEARCH, envelope);
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

                                List<ParseXML.XMLHolderGroupQuoteIDSearch> nodeData = prsObj
                                        .parseNodeGroupQuoteIDSearch(Node);

                                // final List<XMLHolderMaturity> lst;
                                lstGroupQuoteIDSearch = new ArrayList<ParseXML.XMLHolderGroupQuoteIDSearch>();
                                lstGroupQuoteIDSearch.clear();

                                for (ParseXML.XMLHolderGroupQuoteIDSearch node : nodeData) {

                                    lstGroupQuoteIDSearch.add(node);
                                }

                                selectedAdapterGroupQuoteIDSearch = new SelectedAdapterGroupQuoteIDSearch(
                                        mContext, 0, lstGroupQuoteIDSearch);
                                selectedAdapterGroupQuoteIDSearch.setNotifyOnChange(true);

                                registerForContextMenu(lvGroupQuoteSearchList);

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
                    lvGroupQuoteSearchList.setVisibility(View.VISIBLE);

                    txtGroupQuoteSearchMsg.setText("Total Policy : "
                            + lstGroupQuoteIDSearch.size());
                    lvGroupQuoteSearchList.setAdapter(selectedAdapterGroupQuoteIDSearch);

                    Utility.setListViewHeightBasedOnChildren(lvGroupQuoteSearchList);

                } else {
                    //ll_search_GroupTermRenewal.setVisibility(View.GONE);

                    txtGroupQuoteSearchMsg.setText("No Record Found" +"\n"+"Total Policy : " + 0);
                    List<ParseXML.XMLHolderGroupQuoteIDSearch> lst = new ArrayList<ParseXML.XMLHolderGroupQuoteIDSearch>();
                    ParseXML.XMLHolderGroupQuoteIDSearch node = null;
                    lst.add(node);
                    selectedAdapterGroupQuoteIDSearch = new SelectedAdapterGroupQuoteIDSearch(
                            mContext, 0, lst);
                    selectedAdapterGroupQuoteIDSearch.setNotifyOnChange(true);
                    lvGroupQuoteSearchList.setAdapter(selectedAdapterGroupQuoteIDSearch);

                    Utility.setListViewHeightBasedOnChildren(lvGroupQuoteSearchList);
                }
            } else {
                mCommonMethods.showMessageDialog(mContext, mCommonMethods.WEEK_INTERNET_MESSAGE);
            }
        }
    }

    class SelectedAdapterGroupQuoteIDSearch extends ArrayAdapter<ParseXML.XMLHolderGroupQuoteIDSearch> {

        // used to keep selected position in ListView
        private int selectedPos = -1; // init value for not-selected

        List<ParseXML.XMLHolderGroupQuoteIDSearch> lst;

        SelectedAdapterGroupQuoteIDSearch(Context context, int textViewResourceId,
                                          List<ParseXML.XMLHolderGroupQuoteIDSearch> objects) {
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
                v = vi.inflate(R.layout.group_fund_claim_list_item, null);
            }

            TextView txtTitleID = v.findViewById(R.id.txtTitleID);
            TextView txtTitleName = v.findViewById(R.id.txtTitleName);
            TextView txtTitleThird = v.findViewById(R.id.txtTitleThird);
            TextView txtTitleForth = v.findViewById(R.id.txtTitleForth);

            txtTitleID.setText("Policy No.:");
            txtTitleName.setText("Policy Holder:");
            txtTitleThird.setText("Date Of Commencement:");

            // get text view
            TextView textviewPolicyNo = v.findViewById(R.id.textviewMemberId);
            TextView textviewPolicyHolder = v.findViewById(R.id.textviewMemberName);
            TextView textviewDOC = v.findViewById(R.id.textviewClaimSanctionDate);
            TextView textviewForth = v.findViewById(R.id.textviewClaimAmount);

            txtTitleForth.setVisibility(View.GONE);
            textviewForth.setVisibility(View.GONE);

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
                textviewPolicyHolder.setText(name);

                float textWidthLifeAssuredName = textviewPolicyHolder.getPaint().measureText(name);

                float linesFloat = (textWidthLifeAssuredName/eightyPercent) + 0.7f;

                textviewPolicyHolder.setLines(Math.round(linesFloat));

                textviewDOC.setText(lst.get(position).getStr_doc() == null ? ""
                        : lst.get(position).getStr_doc());
            }

            return (v);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnGroupQuoteSearchOk:

                lvGroupQuoteSearchList.setVisibility(View.GONE);
                txtGroupQuoteSearchMsg.setVisibility(View.GONE);
                if(selectedAdapterGroupQuoteIDSearch!=null)
                {
                    selectedAdapterGroupQuoteIDSearch.clear();
                }
                getGroupQuotationIDSearchList();
                break;
            case R.id.btnGroupQuoteSearchReset:
                txtGroupQuoteSearchMsg.setText("");
                resetFields();
                break;

            default:
                break;
        }
    }

    private void resetFields() {

        lvGroupQuoteSearchList.setVisibility(View.GONE);
        txtGroupQuoteSearchMsg.setVisibility(View.GONE);
        //ll_search_GroupTermRenewal.setVisibility(View.GONE);
        if(selectedAdapterGroupQuoteIDSearch!=null)
        {
            selectedAdapterGroupQuoteIDSearch.clear();
        }

        txtGroupQuoteSearchMsg.setText("");
    }

    private void getGroupQuotationIDSearchList() {
        taskAsyncGroupQuoteIDSearch = new AsyncGroupQuoteIDSearch();
        txtGroupQuoteSearchMsg.setVisibility(View.VISIBLE);

        strQuoteNumber = edtGroupQuoteSearch.getText().toString();

        if (!strQuoteNumber.equals("")){
            if (mCommonMethods.isNetworkConnected(mContext)) {

                CommonMethods.UserDetailsValuesModel userDetailsValuesModel = mCommonMethods.setUserDetails(mContext);
                String strCIFBDMUserId = userDetailsValuesModel.getStrCIFBDMUserId();
                String strCIFBDMEmailId = userDetailsValuesModel.getStrCIFBDMEmailId();
                String strCIFBDMPassword = userDetailsValuesModel.getStrCIFBDMPassword();
                String  strCIFBDMMObileNo = userDetailsValuesModel.getStrCIFBDMMObileNo();

                ServiceHits service = new ServiceHits(mContext,  METHOD_NAME_GROUP_QUOTE_ID_SEARCH, strQuoteNumber,
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
        taskAsyncGroupQuoteIDSearch = new AsyncGroupQuoteIDSearch();
        taskAsyncGroupQuoteIDSearch.execute();
    }
}
