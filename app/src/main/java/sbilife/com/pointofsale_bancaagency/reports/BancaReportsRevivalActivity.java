package sbilife.com.pointofsale_bancaagency.reports;

import android.annotation.SuppressLint;
import androidx.appcompat.app.AppCompatActivity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
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

import sbilife.com.pointofsale_bancaagency.CustomerDetailActivity;
import sbilife.com.pointofsale_bancaagency.ParseXML;
import sbilife.com.pointofsale_bancaagency.ParseXML.XMLHolderRevival;
import sbilife.com.pointofsale_bancaagency.R;
import sbilife.com.pointofsale_bancaagency.ServiceURL;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;
import sbilife.com.pointofsale_bancaagency.common.SimpleCrypto;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods.UserDetailsValuesModel;
import sbilife.com.pointofsale_bancaagency.reports.ServiceHits.DownLoadData;
import sbilife.com.pointofsale_bancaagency.reports.commonreports.RevivalQuotationActivity;

@SuppressLint("InflateParams")
public class BancaReportsRevivalActivity extends AppCompatActivity implements DownLoadData {

    private final String METHOD_NAME_REVIVAL_LIST = "getCiFPoliciesRevivalList";

    private final String METHOD_NAME_REVIVAL_LIST_AGENCY = "getAgentPoliciesRevivalList";

    private DownloadFileAsyncRevival taskRevival;

    private CommonMethods mCommonMethods;
    private Context context;
    private final int DIALOG_DOWNLOAD_PROGRESS = 0;
    private ProgressDialog mProgressDialog;

    private String strCIFBDMUserId = "", strCIFBDMEmailId = "",
            strCIFBDMPassword = "", strCIFBDMMObileNo = "";

    private final ArrayList<XMLHolderRevival> lstRevival = new ArrayList<>();
    private ArrayList<XMLHolderRevival> mAdapaterList = new ArrayList<>();
    // revival
    private ListView RevivallistView1;

    private AdapterRevivalList mAdapterRevivalList;

    private TextView txtrevivallistcount;

    private LinearLayout lnPolicyRevival;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.banca_reports_revival);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);
        context = this;
        mCommonMethods = new CommonMethods();
        mProgressDialog = new ProgressDialog(context);

        mCommonMethods.setApplicationToolbarMenu(this, "Revival");

        taskRevival = new DownloadFileAsyncRevival();
        RevivallistView1 = findViewById(R.id.RevivallistView1);
        txtrevivallistcount = findViewById(R.id.txtrevivallistcount);
        lnPolicyRevival = findViewById(R.id.lnPolicyRevival);
        EditText edt_revival_search = findViewById(R.id.edt_revival_search);

        edt_revival_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mAdapterRevivalList.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        Intent intent = getIntent();
        String fromHome = intent.getStringExtra("fromHome");
        if (fromHome != null && fromHome.equalsIgnoreCase("N")) {
            strCIFBDMUserId = intent.getStringExtra("strBDMCifCOde");
            strCIFBDMEmailId = intent.getStringExtra("strEmailId");
            strCIFBDMMObileNo = intent.getStringExtra("strMobileNo");

            try {
                strCIFBDMPassword = SimpleCrypto.encrypt("SBIL", "sbil");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            getUserDetails();
        }

        mAdapterRevivalList = new AdapterRevivalList(context, lstRevival);
        RevivallistView1.setAdapter(mAdapterRevivalList);
        mAdapterRevivalList.notifyDataSetChanged();

        getRevivalDetails();
    }

    private void getRevivalDetails() {

        if (mCommonMethods.isNetworkConnected(context)) {
            String strUType = mCommonMethods.GetUserType(context);
            if (strUType.contentEquals("CIF")
                    || strUType.contentEquals("BDM")
                    || strUType.contentEquals("TBDM")) {
                service_hits(METHOD_NAME_REVIVAL_LIST);
            } else {
                service_hits(METHOD_NAME_REVIVAL_LIST_AGENCY);
            }
        } else {
            //intereneterror();
            mCommonMethods.showMessageDialog(context, mCommonMethods.NO_INTERNET_MESSAGE);
        }
    }

    private void getUserDetails() {
        UserDetailsValuesModel userDetailsValuesModel = mCommonMethods.setUserDetails(context);
        strCIFBDMUserId = userDetailsValuesModel.getStrCIFBDMUserId();
        strCIFBDMEmailId = userDetailsValuesModel.getStrCIFBDMEmailId();
        strCIFBDMPassword = userDetailsValuesModel.getStrCIFBDMPassword();
        strCIFBDMMObileNo = userDetailsValuesModel.getStrCIFBDMMObileNo();
    }


    @SuppressWarnings("deprecation")
    private void dismissProgressDialog() {
        dismissDialog(DIALOG_DOWNLOAD_PROGRESS);
    }

    @SuppressWarnings("deprecation")
    private void showProgressDialog() {
        showDialog(DIALOG_DOWNLOAD_PROGRESS);
    }

    class DownloadFileAsyncRevival extends AsyncTask<String, String, String> {

        private volatile boolean running = true;
        private String inputpolicylist = "", strRevivalListErrorCOde = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
        }

        @Override
        protected String doInBackground(String... aurl) {
            try {

                running = true;
                SoapObject request;

                String UserType = mCommonMethods.GetUserType(context);

                String NAMESPACE = "http://tempuri.org/";
                if (UserType.contentEquals("MAN")
                        || UserType.contentEquals("BDM")
                        || UserType.contentEquals("TBDM")
                        || UserType.contentEquals("CIF")) {
                    request = new SoapObject(NAMESPACE,
                            METHOD_NAME_REVIVAL_LIST);
                    request.addProperty("strCifNo", strCIFBDMUserId);
                } else {
                    request = new SoapObject(NAMESPACE,
                            METHOD_NAME_REVIVAL_LIST_AGENCY);
                    request.addProperty("strAgentNo", strCIFBDMUserId);
                }
                request.addProperty("strEmailId", strCIFBDMEmailId);
                request.addProperty("strMobileNo", strCIFBDMMObileNo);
                request.addProperty("strAuthKey", strCIFBDMPassword.trim());

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);
                envelope.dotNet = true;

                envelope.setOutputSoapObject(request);

                // 	allowAllSSL();
                mCommonMethods.TLSv12Enable();

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);

                String URl = ServiceURL.SERVICE_URL;
                HttpTransportSE androidHttpTranport = new HttpTransportSE(URl);
                try {
                    if (UserType.contentEquals("MAN")
                            || UserType.contentEquals("BDM")
                            || UserType.contentEquals("TBDM")
                            || UserType.contentEquals("CIF")) {
                        String SOAP_ACTION_REVIVAL_LIST = "http://tempuri.org/getCiFPoliciesRevivalList";
                        androidHttpTranport.call(SOAP_ACTION_REVIVAL_LIST,
                                envelope);
                    } else {
                        String SOAP_ACTION_REVIVAL_LIST_AGENCY = "http://tempuri.org/getAgentPoliciesRevivalList";
                        androidHttpTranport.call(SOAP_ACTION_REVIVAL_LIST_AGENCY,
                                envelope);
                    }
                    Object response = envelope.getResponse();

                    if (!response.toString().contentEquals("anyType{}")) {

                        SoapPrimitive sa ;
                        try {
                            sa = (SoapPrimitive) envelope.getResponse();

                            inputpolicylist = sa.toString();

                            ParseXML prsObj = new ParseXML();

                            inputpolicylist = prsObj.parseXmlTag(
                                    inputpolicylist, "CIFPolicyList");
                            inputpolicylist = new ParseXML().parseXmlTag(
                                    inputpolicylist, "ScreenData");
                            strRevivalListErrorCOde = inputpolicylist;

                            if (strRevivalListErrorCOde == null) {
                                inputpolicylist = sa.toString();
                                inputpolicylist = prsObj.parseXmlTag(
                                        inputpolicylist, "CIFPolicyList");

                                List<String> Node = prsObj.parseParentNode(
                                        inputpolicylist, "Table");

                                List<XMLHolderRevival> nodeData = prsObj
                                        .parseNodeElementRevival(Node);

                                lstRevival.clear();

                                lstRevival.addAll(nodeData);

                                registerForContextMenu(RevivallistView1);

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
        protected void onProgressUpdate(String... progress) {
            Log.d("ANDRO_ASYNC", progress[0]);
            mProgressDialog.setProgress(Integer.parseInt(progress[0]));
        }

        @Override
        protected void onPostExecute(String unused) {
            if (mProgressDialog.isShowing()) {
                dismissProgressDialog();
            }
            if (running) {
                lnPolicyRevival.setVisibility(View.VISIBLE);
                if (strRevivalListErrorCOde == null) {

                    txtrevivallistcount.setText("Total Policy : " + lstRevival.size());

                    mAdapterRevivalList.notifyDataSetChanged();

                    //Utility.setListViewHeightBasedOnChildren(RevivallistView1);
                } else {

                    txtrevivallistcount.setText("No Record found");

                    mAdapterRevivalList.notifyDataSetChanged();

                    //Utility.setListViewHeightBasedOnChildren(RevivallistView1);
                }
            } else {
                //servererror();
                mCommonMethods.showMessageDialog(context, mCommonMethods.SERVER_ERROR);
            }
        }
    }

    class AdapterRevivalList extends BaseAdapter implements Filterable {

        private final Context mAdapterContext;
        private ArrayList<XMLHolderRevival> lstSearch;

        AdapterRevivalList(Context mAdapterContext, ArrayList<XMLHolderRevival> list) {
            this.mAdapterContext = mAdapterContext;
            mAdapaterList = list;
        }

        @Override
        public Filter getFilter() {
            return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence charSequence) {
                    final FilterResults oReturn = new FilterResults();
                    final ArrayList<XMLHolderRevival> results = new ArrayList<>();

                    if (lstSearch == null)
                        lstSearch = mAdapaterList;

                    if (charSequence != null) {
                        if (lstSearch != null && lstSearch.size() > 0) {
                            for (final XMLHolderRevival g : lstSearch) {
                                if (g.getFName().toLowerCase().contains(charSequence.toString().toLowerCase()))
                                    results.add(g);
                                else if (g.getNo().toLowerCase().contains(charSequence.toString().toLowerCase()))
                                    results.add(g);
                                else if (g.getPremiumUp().toLowerCase().contains(charSequence.toString().toLowerCase()))
                                    results.add(g);
                            }
                        }
                        oReturn.values = results;
                    }
                    return oReturn;
        }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    mAdapaterList = (ArrayList<XMLHolderRevival>) results.values;
                    notifyDataSetChanged();

                    txtrevivallistcount.setText("Total Policy: " + mAdapaterList.size());

                    if (mAdapaterList.size() == 0) {
                        txtrevivallistcount.setText("No Record found");
                    }
                }
            };
        }

        @Override
        public int getCount() {
            return mAdapaterList.size();
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public Object getItem(int position) {
            return mAdapaterList.get(position);
        }

        class RevivalHolder {
            private TextView txtrevivalno, txtcustomercode, txtrevivalfirstname, txtrevivalpremiumup;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            RevivalHolder mHolder;
            // only inflate the view if it's null
            if (convertView == null) {
                convertView = LayoutInflater.from(mAdapterContext).inflate(R.layout.list_item_revivallist, parent, false);
                mHolder = new RevivalHolder();

            // get text view
                mHolder.txtrevivalno = convertView.findViewById(R.id.txtrevivalno);
                mHolder.txtcustomercode = convertView.findViewById(R.id.txtcustomercode);
                mHolder.txtrevivalfirstname = convertView.findViewById(R.id.txtrevivalfirstname);
                mHolder.txtrevivalpremiumup = convertView.findViewById(R.id.txtrevivalpremiumup);
                convertView.setTag(mHolder);
            } else {
                mHolder = (RevivalHolder) convertView.getTag();
            }

            mHolder.txtrevivalno.setText(mAdapaterList.get(position).getNo() == null ? "" : mAdapaterList.get(
                        position).getNo());
            mHolder.txtcustomercode.setText(mAdapaterList.get(position).getHolderId() == null ? ""
                    : mAdapaterList.get(position).getHolderId());

            String fnm = mAdapaterList.get(position).getFName() == null ? "" : mAdapaterList
                        .get(position).getFName();
            String lnm = mAdapaterList.get(position).getLName() == null ? "" : mAdapaterList
                        .get(position).getLName();

            mHolder.txtrevivalfirstname.setText(fnm + " " + lnm);

            mHolder.txtrevivalpremiumup.setText(mAdapaterList.get(position).getPremiumUp() == null ? ""
                    : mAdapaterList.get(position).getPremiumUp());

            return convertView;
        }
    }

    private void service_hits(String strServiceName) {


        ServiceHits service = new ServiceHits(context,
                strServiceName, "", strCIFBDMUserId,
                strCIFBDMEmailId, strCIFBDMMObileNo,
                strCIFBDMPassword, this);
        service.execute();
    }

    private void startDownloadRevivalList() {
        taskRevival = new DownloadFileAsyncRevival();
        taskRevival.execute("demo");
    }

    @Override
    public void downLoadData() {
        startDownloadRevivalList();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            if (taskRevival != null) {
                taskRevival.cancel(true);
            }
            if (mProgressDialog != null) {
                if (mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                }
            }

            finish();

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @SuppressWarnings("deprecation")
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DIALOG_DOWNLOAD_PROGRESS:
                mProgressDialog = new ProgressDialog(this, ProgressDialog.THEME_HOLO_LIGHT);
                String Message = "Loading Please wait...";
                mProgressDialog.setMessage(Html.fromHtml("<font color='#00a1e3'><b>" + Message + "<b></font>"));
                mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                mProgressDialog.setCancelable(true);

                mProgressDialog.setButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                if (taskRevival != null) {
                                    taskRevival.cancel(true);
                                }
                                if (mProgressDialog != null) {
                                    if (mProgressDialog.isShowing()) {
                                        mProgressDialog.dismiss();
                                    }
                                }
                            }
                        });

                mProgressDialog.setMax(100);
                mProgressDialog.show();
                return mProgressDialog;

            default:
                return null;
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        menu.setHeaderTitle("Services");

        int id = v.getId();
        if (id == R.id.RevivallistView1) {
            menu.add(0, v.getId(), 1, "Customer Details");
            menu.add(0, v.getId(), 0, "Revival Quotation");
        }


    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
                .getMenuInfo();

        if (item.getTitle().toString().contentEquals("Customer Details")) {
            String strrevCustomerId = mAdapaterList.get(info.position)
                        .getHolderId();
                if (!TextUtils.isEmpty(strrevCustomerId)) {
                    Intent intent = new Intent(context,
                            CustomerDetailActivity.class);
                    intent.putExtra("CustomerId", strrevCustomerId);
                    intent.putExtra("strUserType", "");
                    intent.putExtra("strAgentCode", strCIFBDMUserId);
                    intent.putExtra("strEmail", strCIFBDMEmailId);
                    intent.putExtra("strMobileNo", strCIFBDMMObileNo);
                    intent.putExtra("strPassword", strCIFBDMPassword.trim());
                    startActivity(intent);
                }
                return true;
        }else if (item.getTitle().toString().contentEquals("Revival Quotation")) {
            String strPolicyNo = mAdapaterList.get(info.position).getNo();
            String mobileNumber = "";
            String emailId = "";

            Intent intent = new Intent(context, RevivalQuotationActivity.class);
            intent.putExtra("policyNumber", strPolicyNo);
            intent.putExtra("mobileNumber", mobileNumber);
            intent.putExtra("emailId", emailId);
            startActivity(intent);
        }

        return true;

    }


}
