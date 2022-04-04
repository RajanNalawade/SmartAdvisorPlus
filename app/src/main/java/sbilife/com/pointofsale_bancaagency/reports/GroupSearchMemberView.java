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

public class GroupSearchMemberView extends AppCompatActivity implements View.OnClickListener, ServiceHits.DownLoadData{

    private  final String NAMESPACE = "http://tempuri.org/";
    private  final String URl = ServiceURL.SERVICE_URL;

    private  final String METHOD_NAME_GROUP_MEMBER_VIEW = "MemberView_Groups";

    private Context mContext;
    private CommonMethods mCommonMethods;

    private EditText edtGroupMemberViewPolicyNo, edtGroupMemberViewMemberID;
    private TextView txtGroupMemberViewMsg;
    private ListView lvGroupMemberViewList;

    private final int DIALOG_DOWNLOAD_PROGRESS = 0;
    private ProgressDialog mProgressDialog;

    private List<ParseXML.XMLHolderGroupMemberView> lstGroupMemberView;
    private SelectedAdapterGroupMemberView selectedAdapterGroupMemberView;

    private AsyncGroupMemberView taskAsyncGroupMemberView;

    private String strPolicyNumber = "", strMemberID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_group_search_member_view);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);

        initialization();
    }

    private void initialization(){

        mCommonMethods = new CommonMethods();
        mContext = this;
        mCommonMethods.setApplicationToolbarMenu(this,"Group Member View Search");

        edtGroupMemberViewPolicyNo = findViewById(R.id.edtGroupMemberViewPolicyNo);
        edtGroupMemberViewMemberID = findViewById(R.id.edtGroupMemberViewMemberID);
        Button btnGroupMemberViewOk = findViewById(R.id.btnGroupMemberViewOk);
        Button btnGroupMemberViewReset = findViewById(R.id.btnGroupMemberViewReset);
        txtGroupMemberViewMsg  = findViewById(R.id.txtGroupMemberViewMsg);
        lvGroupMemberViewList   = findViewById(R.id.lvGroupMemberViewList);

        btnGroupMemberViewOk.setOnClickListener(this);
        btnGroupMemberViewReset.setOnClickListener(this);
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

    class AsyncGroupMemberView extends AsyncTask<String, String, String> {

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

                request = new SoapObject(NAMESPACE,METHOD_NAME_GROUP_MEMBER_VIEW);
                request.addProperty("strPolicyNo", strPolicyNumber);
                request.addProperty("strCode", mCommonMethods.setUserDetails(mContext).getStrCIFBDMUserId());
                request.addProperty("strMemberID", strMemberID);
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

                    String SOAP_ACTION_GROUP_MEMBER_VIEW = "http://tempuri.org/MemberView_Groups";
                    androidHttpTranport.call(
                            SOAP_ACTION_GROUP_MEMBER_VIEW, envelope);
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

                                List<ParseXML.XMLHolderGroupMemberView> nodeData = prsObj
                                        .parseNodeGroupMemberView(Node);

                                // final List<XMLHolderMaturity> lst;
                                lstGroupMemberView = new ArrayList<ParseXML.XMLHolderGroupMemberView>();
                                lstGroupMemberView.clear();

                                for (ParseXML.XMLHolderGroupMemberView node : nodeData) {

                                    lstGroupMemberView.add(node);
                                }

                                selectedAdapterGroupMemberView = new SelectedAdapterGroupMemberView(
                                        mContext, 0, lstGroupMemberView);
                                selectedAdapterGroupMemberView.setNotifyOnChange(true);

                                registerForContextMenu(lvGroupMemberViewList);

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
                    lvGroupMemberViewList.setVisibility(View.VISIBLE);

                    txtGroupMemberViewMsg.setText("Total Policy : "
                            + lstGroupMemberView.size());
                    lvGroupMemberViewList.setAdapter(selectedAdapterGroupMemberView);

                    Utility.setListViewHeightBasedOnChildren(lvGroupMemberViewList);

                } else {
                    //ll_search_GroupTermRenewal.setVisibility(View.GONE);

                    txtGroupMemberViewMsg.setText("No Record Found" +"\n"+"Total Policy : " + 0);
                    List<ParseXML.XMLHolderGroupMemberView> lst = new ArrayList<ParseXML.XMLHolderGroupMemberView>();
                    ParseXML.XMLHolderGroupMemberView node = null;
                    lst.add(node);
                    selectedAdapterGroupMemberView = new SelectedAdapterGroupMemberView(
                            mContext, 0, lst);
                    selectedAdapterGroupMemberView.setNotifyOnChange(true);
                    lvGroupMemberViewList.setAdapter(selectedAdapterGroupMemberView);

                    Utility.setListViewHeightBasedOnChildren(lvGroupMemberViewList);
                }
            } else {
                mCommonMethods.showMessageDialog(mContext, mCommonMethods.WEEK_INTERNET_MESSAGE);
            }
        }
    }

    class SelectedAdapterGroupMemberView extends ArrayAdapter<ParseXML.XMLHolderGroupMemberView> {

        // used to keep selected position in ListView
        private int selectedPos = -1; // init value for not-selected

        List<ParseXML.XMLHolderGroupMemberView> lst;

        SelectedAdapterGroupMemberView(Context context, int textViewResourceId,
                                       List<ParseXML.XMLHolderGroupMemberView> objects) {
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
            txtTitleForth.setText("Employee ID:");
            txtTitleFifth.setText("Member Name:");
            txtTitleSixth.setText("DOJS:");
            txtTitleSeven.setText("Basic Sum Assured:");
            txtTitleEighth.setText("Rider PPD:");
            txtTitleEighth1.setText("Rider AD");
            txtTitleNinth.setText("Rider CI:");
            txtTitleTenth.setText("Rider TPD:");
            txtTitleEleven.setText("Rider Sum Assured:");
            txtTitleTwelth.setText("Nominee Name:");
            txtTitleThirteen.setText("Nominee Relationship:");

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

                textviewARD.setText(lst.get(position).getMember_emp_id() == null ? ""
                        : lst.get(position).getMember_emp_id());

                textviewTotalPremium.setText(lst.get(position).getMember_name() == null ? ""
                        : lst.get(position).getMember_name());

                textviewLives.setText(lst.get(position).getMember_rcd() == null ? ""
                        : lst.get(position).getMember_rcd());

                textviewUnadjustedDeposit.setText(lst.get(position).getMember_sum_assured() == null ? ""
                        : lst.get(position).getMember_sum_assured());

                textviewRiderOption.setText(lst.get(position).getRider_ppd() == null ? ""
                        : lst.get(position).getRider_ppd());

                textviewFCL.setText(lst.get(position).getRider_AD() == null ? ""
                        : lst.get(position).getRider_AD());

                textviewPolicyStatus.setText(lst.get(position).getRider_cl() == null ? ""
                        : lst.get(position).getRider_cl());

                textviewBasicPremiumRate.setText(lst.get(position).getRider_tpd() == null ? ""
                        : lst.get(position).getRider_tpd());

                textviewRiderPremiumRate.setText(lst.get(position).getRider_sum_assured() == null ? ""
                        : lst.get(position).getRider_sum_assured());

                textviewTotalSumAssured.setText(lst.get(position).getNominee_name() == null ? ""
                        : lst.get(position).getNominee_name());

                textviewTypeScheme.setText(lst.get(position).getNominee_relationship() == null ? ""
                        : lst.get(position).getNominee_relationship());
            }

            return (v);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnGroupMemberViewOk:
                lvGroupMemberViewList.setVisibility(View.GONE);
                txtGroupMemberViewMsg.setVisibility(View.GONE);
                if(selectedAdapterGroupMemberView!=null)
                {
                    selectedAdapterGroupMemberView.clear();
                }
                getGroupPolicySearchList();
                break;

            case R.id.btnGroupMemberViewReset:
                txtGroupMemberViewMsg.setText("");
                resetFields();
                break;


            default:
                break;
        }
    }

    private void resetFields() {

        lvGroupMemberViewList.setVisibility(View.GONE);
        txtGroupMemberViewMsg.setVisibility(View.GONE);
        //ll_search_GroupTermRenewal.setVisibility(View.GONE);
        if(selectedAdapterGroupMemberView!=null)
        {
            selectedAdapterGroupMemberView.clear();
        }

        txtGroupMemberViewMsg.setText("");
    }

    private void getGroupPolicySearchList() {
        taskAsyncGroupMemberView = new AsyncGroupMemberView();
        txtGroupMemberViewMsg.setVisibility(View.VISIBLE);

        strPolicyNumber = edtGroupMemberViewPolicyNo.getText().toString();
        strMemberID = edtGroupMemberViewMemberID.getText().toString();

        if (!strPolicyNumber.equals("") && !strMemberID.equals("")){
            if (mCommonMethods.isNetworkConnected(mContext)) {

                CommonMethods.UserDetailsValuesModel userDetailsValuesModel = mCommonMethods.setUserDetails(mContext);
                String strCIFBDMUserId = userDetailsValuesModel.getStrCIFBDMUserId();
                String strCIFBDMEmailId = userDetailsValuesModel.getStrCIFBDMEmailId();
                String strCIFBDMPassword = userDetailsValuesModel.getStrCIFBDMPassword();
                String  strCIFBDMMObileNo = userDetailsValuesModel.getStrCIFBDMMObileNo();

                ServiceHits service = new ServiceHits(mContext,  METHOD_NAME_GROUP_MEMBER_VIEW, strPolicyNumber,
                        strCIFBDMUserId, strCIFBDMEmailId, strCIFBDMMObileNo,
                        strCIFBDMPassword, this);
                service.execute();

            } else {
                mCommonMethods.showMessageDialog(mContext,
                        "Internet Connection Not Present,Try again..");
            }
        }else
            mCommonMethods.showToast(mContext, "Please enter all details.");
    }


    @Override
    public void downLoadData() {
        taskAsyncGroupMemberView = new AsyncGroupMemberView();
        taskAsyncGroupMemberView.execute();
    }
}
