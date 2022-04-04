package sbilife.com.pointofsale_bancaagency.reports;

import androidx.appcompat.app.AppCompatActivity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import sbilife.com.pointofsale_bancaagency.ParseXML;
import sbilife.com.pointofsale_bancaagency.R;
import sbilife.com.pointofsale_bancaagency.ServiceURL;
import sbilife.com.pointofsale_bancaagency.Utility;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;

public class GroupFundContributionReceivedActivity extends AppCompatActivity implements View.OnClickListener, ServiceHits.DownLoadData{

    private  final String NAMESPACE = "http://tempuri.org/";
    private  final String URl = ServiceURL.SERVICE_URL;

    private  final String METHOD_NAME_GROUP_FUND_CONTRIBUTION_RECEIVED = "Contribution_Fund";

    private Context mContext;
    private CommonMethods mCommonMethods;

    private int mYear, mMonth, mDay, datecheck = 0;

    private ProgressDialog mProgressDialog;

    private SelectedAdapterGroupFundContribution selectedAdapterGroupFundContribution;

    private List<ParseXML.XMLHolderGroupFundContribution> lstGroupFundContribution;

    private AsyncGroupFundContribution taskAsyncGroupFundContribution;

    private String strtodate = "",strfromdate = "",policyNumber = "";

    private EditText editTextdt, editTextdtto,edtGroupFundContributionPolicyNo;

    private TextView txtGroupFundContributionMsg;
    private ListView lvGroupFundContributionList;

    private final int DATE_DIALOG_ID = 1;
    private final int DIALOG_DOWNLOAD_PROGRESS = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_group_fund_contribution_received);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);

        initialization();

        setDates();
    }

    private void initialization(){
        mContext = this;
        mCommonMethods = new CommonMethods();
        mCommonMethods.setApplicationToolbarMenu(this,"Fund Contribution Received");

        editTextdt = findViewById(R.id.editTextdt);
        editTextdtto  = findViewById(R.id.editTextdtto);
        edtGroupFundContributionPolicyNo  = findViewById(R.id.edtGroupFundContributionPolicyNo);

        ImageButton btndate = findViewById(R.id.btndate);
        ImageButton btnbtndateto = findViewById(R.id.btnbtndateto);

        Button btnGroupFundContributionOK = findViewById(R.id.btnGroupFundContributionOK);
        Button btnGroupFundContributionReset = findViewById(R.id.btnGroupFundContributionReset);

        btnGroupFundContributionOK.setOnClickListener(this);
        btnGroupFundContributionReset.setOnClickListener(this);

        btndate.setOnClickListener(this);
        btnbtndateto.setOnClickListener(this);

        txtGroupFundContributionMsg  = findViewById(R.id.txtGroupFundContributionMsg);
        lvGroupFundContributionList   = findViewById(R.id.lvGroupFundContributionList);
    }

    private void setDates() {
        Calendar cal = Calendar.getInstance();
        mYear = cal.get(Calendar.YEAR);
        mMonth = cal.get(Calendar.MONTH);
        mDay = cal.get(Calendar.DAY_OF_MONTH);

        int mYear = cal.get(Calendar.YEAR);
        int mMonth = cal.get(Calendar.MONTH);
        int mDay = cal.get(Calendar.DAY_OF_MONTH);

        String y = String.valueOf(mYear);
        String m = String.valueOf(mMonth + 1);
        String da = String.valueOf(mDay);

        m = mCommonMethods.getFullMonthName(m);
        String totaldate = da + "-" + m + "-" + y;
        editTextdtto.setText(totaldate);

        Calendar calenderToDate = Calendar.getInstance();
        calenderToDate.add(Calendar.MONTH, -1);
        int mYeareCPT = calenderToDate.get(Calendar.YEAR);
        int mMontheCPT = calenderToDate.get(Calendar.MONTH);
        int mDayeCPT = calenderToDate.get(Calendar.DAY_OF_MONTH);

        String yeCPT = String.valueOf(mYeareCPT);
        String meCPT = String.valueOf(mMontheCPT + 1);
        String daeCPT = String.valueOf(mDayeCPT);

        String todate = daeCPT + "-" + mCommonMethods.getFullMonthName(meCPT) + "-" + yeCPT;
        editTextdt.setText(todate);

    }

    private void updateDisplay(int year, int month, int day) {

        String y = String.valueOf(year);
        String m = String.valueOf(month + 1);
        String da = String.valueOf(day);

        m = mCommonMethods.getFullMonthName(m);
        String totaldate = da + "-" + m + "-" + y;

        if (datecheck == 1) {
            editTextdt.setText(totaldate);
        } else {
            editTextdtto.setText(totaldate);
        }
    }

    private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;
            updateDisplay(mYear, mMonth, mDay);

        }
    };

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

            case DATE_DIALOG_ID:
                return new DatePickerDialog(this, R.style.datepickerstyle,
                        mDateSetListener, mYear, mMonth, mDay);

            default:
                return null;
        }
    }

    class AsyncGroupFundContribution extends AsyncTask<String, String, String> {

        private volatile boolean running = true;
        private String inputpolicylist = "",strGroupNewBusinessListErrorCOde = "",
                strfromdate = "",strtodate = "";
        @Override
        protected void onPreExecute() {
            showDialog(DIALOG_DOWNLOAD_PROGRESS);
            strfromdate = editTextdt.getText().toString();
            strtodate = editTextdtto.getText().toString();

            final SimpleDateFormat formatter = new SimpleDateFormat(
                    "dd-MMMM-yyyy");
            final SimpleDateFormat formatter1 = new SimpleDateFormat(
                    "MM-dd-yyyy");
            try {
                Date dt = formatter.parse(strfromdate);
                strfromdate = formatter1.format(dt);

                Date dt1 = formatter.parse(strtodate);
                strtodate = formatter1.format(dt1);

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        //98001001103
        @Override
        protected String doInBackground(String... params) {
            try {

                running = true;
                SoapObject request = null;

                request = new SoapObject(NAMESPACE,METHOD_NAME_GROUP_FUND_CONTRIBUTION_RECEIVED);
                request.addProperty("strFromDate", strfromdate);
                request.addProperty("strToDate", strtodate);
                request.addProperty("strPolicyNo", policyNumber);
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

                    String SOAP_ACTION_GROUP_FUND_CONTRIBUTION_RECEIVED = "http://tempuri.org/Contribution_Fund";
                    androidHttpTranport.call(
                            SOAP_ACTION_GROUP_FUND_CONTRIBUTION_RECEIVED, envelope);
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

                                List<ParseXML.XMLHolderGroupFundContribution> nodeData = prsObj
                                        .parseNodeGroupFundContribution(Node);

                                // final List<XMLHolderMaturity> lst;
                                lstGroupFundContribution = new ArrayList<ParseXML.XMLHolderGroupFundContribution>();

                                for (ParseXML.XMLHolderGroupFundContribution node : nodeData) {

                                    lstGroupFundContribution.add(node);
                                }

                                selectedAdapterGroupFundContribution = new SelectedAdapterGroupFundContribution(
                                        mContext, 0, lstGroupFundContribution);
                                selectedAdapterGroupFundContribution.setNotifyOnChange(true);

                                registerForContextMenu(lvGroupFundContributionList);

                            } else {
                                // txterrordesc.setText("No Data");
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
                    lvGroupFundContributionList.setVisibility(View.VISIBLE);

                    txtGroupFundContributionMsg.setText("Total Policy : "
                            + lstGroupFundContribution.size());
                    lvGroupFundContributionList.setAdapter(selectedAdapterGroupFundContribution);

                    Utility.setListViewHeightBasedOnChildren(lvGroupFundContributionList);

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

                    txtGroupFundContributionMsg.setText("No Record Found"+"\n"+"Total Policy : " + 0);
                    List<ParseXML.XMLHolderGroupFundContribution> lst;
                    ParseXML.XMLHolderGroupFundContribution node = null;
                    lst = new ArrayList<ParseXML.XMLHolderGroupFundContribution>();
                    lst.clear();
                    lst.add(node);
                    selectedAdapterGroupFundContribution = new SelectedAdapterGroupFundContribution(
                            mContext, 0, lst);
                    selectedAdapterGroupFundContribution.setNotifyOnChange(true);
                    lvGroupFundContributionList.setAdapter(selectedAdapterGroupFundContribution);

                    Utility.setListViewHeightBasedOnChildren(lvGroupFundContributionList);

                }
            } else {
                mCommonMethods.showMessageDialog(mContext, mCommonMethods.SERVER_ERROR);
            }
        }
    }

    class SelectedAdapterGroupFundContribution extends ArrayAdapter<ParseXML.XMLHolderGroupFundContribution> {

        // used to keep selected position in ListView
        private int selectedPos = -1; // init value for not-selected

        List<ParseXML.XMLHolderGroupFundContribution> lst;

        SelectedAdapterGroupFundContribution(Context context, int textViewResourceId,
                                             List<ParseXML.XMLHolderGroupFundContribution> objects) {
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

            txtTitleID.setText("Premium Billed Date");
            txtTitleName.setText("Amount");

            // get text view
            TextView textviewMemberId = v.findViewById(R.id.textviewMemberId);
            TextView textviewMemberName = v.findViewById(R.id.textviewMemberName);
            TextView textviewClaimSanctionDate = v.findViewById(R.id.textviewClaimSanctionDate);
            TextView textviewClaimAmount = v.findViewById(R.id.textviewClaimAmount);

            txtTitleThird.setVisibility(View.GONE);
            txtTitleForth.setVisibility(View.GONE);

            textviewClaimSanctionDate.setVisibility(View.GONE);
            textviewClaimAmount.setVisibility(View.GONE);

            Object obj = null;
            boolean i = lst.contains(obj);

            if (!i) {

                textviewMemberId.setText(lst.get(position).getPremium_billed_date() == null ? ""
                        : lst.get(position).getPremium_billed_date());

                textviewMemberName.setText(lst.get(position).getAmount() == null ? ""
                        : lst.get(position).getAmount());
            }

            return (v);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnGroupFundContributionOK:

                lvGroupFundContributionList.setVisibility(View.GONE);
                txtGroupFundContributionMsg.setVisibility(View.GONE);
                txtGroupFundContributionMsg.setText("");
                if(selectedAdapterGroupFundContribution!=null)
                {
                    selectedAdapterGroupFundContribution.clear();
                }
                getGroupFundContributionList();
                break;
            case R.id.btnGroupFundContributionReset:
                txtGroupFundContributionMsg.setText("");
                resetFields();
                break;

            case R.id.btndate:
                datecheck = 1;
                showDateProgressDialog();
                break;

            case R.id.btnbtndateto:
                datecheck = 0;
                showDateProgressDialog();
                break;
            default:
                break;
        }
    }

    private void showDateProgressDialog() {
        showDialog(DATE_DIALOG_ID);
    }

    private void resetFields() {

        lvGroupFundContributionList.setVisibility(View.GONE);
        txtGroupFundContributionMsg.setVisibility(View.GONE);
        //ll_search_GroupTermRenewal.setVisibility(View.GONE);
        if(selectedAdapterGroupFundContribution!=null)
        {
            selectedAdapterGroupFundContribution.clear();
        }

        editTextdt.setText("");
        editTextdtto.setText("");

        txtGroupFundContributionMsg.setText("");

        Calendar cal = Calendar.getInstance();
        mYear = cal.get(Calendar.YEAR);
        mMonth = cal.get(Calendar.MONTH);
        mDay = cal.get(Calendar.DAY_OF_MONTH);
    }

    private void getGroupFundContributionList() {
        taskAsyncGroupFundContribution = new AsyncGroupFundContribution();
        txtGroupFundContributionMsg.setVisibility(View.VISIBLE);

        strfromdate = editTextdt.getText().toString();
        strtodate = editTextdtto.getText().toString();

        policyNumber = edtGroupFundContributionPolicyNo.getText().toString();

        if (strfromdate.equalsIgnoreCase("")
                || strtodate.equalsIgnoreCase("")||policyNumber.equalsIgnoreCase("")) {
            mCommonMethods.showMessageDialog(mContext, "All Fields Required..");
        } else {
            final SimpleDateFormat formatter = new SimpleDateFormat(
                    "dd-MMMM-yyyy");

            String strfromdate = editTextdt.getText().toString();
            String strtodate = editTextdtto.getText().toString();

            Date d1 = null;
            try {
                d1 = formatter.parse(strfromdate);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            Date d2 = null;
            try {
                d2 = formatter.parse(strtodate);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            if ((d2.after(d1)) || (d2.equals(d1))) {

                if (mCommonMethods.isNetworkConnected(mContext)) {
                    // edt_search_GroupTermRenewal_by_policy.setText("");
                    // edt_search_GroupTermRenewal_by_name.setText("");
                    // edt_search_GroupTermRenewal_by_end_date.setText("");
                    service_hits(METHOD_NAME_GROUP_FUND_CONTRIBUTION_RECEIVED);

                } else {
                    mCommonMethods.showMessageDialog(mContext,
                            "Internet Connection Not Present,Try again..");
                }
            } else {
                mCommonMethods.showMessageDialog(mContext,
                        "To date should be greater than From date");
            }
        }
    }

    private void service_hits(String strServiceName) {

        strfromdate = editTextdt.getText().toString();
        strtodate = editTextdtto.getText().toString();

        final SimpleDateFormat formatter = new SimpleDateFormat("dd-MMMM-yyyy");
        final SimpleDateFormat formatter1 = new SimpleDateFormat("MM-dd-yyyy");
        try {
            Date dt = formatter.parse(strfromdate);
            strfromdate = formatter1.format(dt);

            Date dt1 = formatter.parse(strtodate);
            strtodate = formatter1.format(dt1);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        StringBuilder str = new StringBuilder();
        str.append(strfromdate);
        str.append(",");
        str.append(strtodate);

        CommonMethods.UserDetailsValuesModel userDetailsValuesModel = mCommonMethods.setUserDetails(mContext);
        String strCIFBDMUserId = userDetailsValuesModel.getStrCIFBDMUserId();
        String strCIFBDMEmailId = userDetailsValuesModel.getStrCIFBDMEmailId();
        String strCIFBDMPassword = userDetailsValuesModel.getStrCIFBDMPassword();
        String  strCIFBDMMObileNo = userDetailsValuesModel.getStrCIFBDMMObileNo();

        ServiceHits service = new ServiceHits(mContext,  strServiceName, str.toString(),
                strCIFBDMUserId, strCIFBDMEmailId, strCIFBDMMObileNo,
                strCIFBDMPassword, this);
        service.execute();
    }


    @Override
    public void downLoadData() {
        taskAsyncGroupFundContribution = new AsyncGroupFundContribution();
        taskAsyncGroupFundContribution.execute();
    }
}
