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
import android.widget.BaseAdapter;
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
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;

/**
 * Created by O0110 on 15/09/2017.
 */

public class GroupFundCliamActivity extends AppCompatActivity implements View.OnClickListener, ServiceHits.DownLoadData {

    private  final int DATE_DIALOG_ID = 1;
    private  final int DIALOG_DOWNLOAD_PROGRESS = 0;
    private  final String NAMESPACE = "http://tempuri.org/";
    private  final String URl = ServiceURL.SERVICE_URL;

    private  final String METHOD_NAME_GROUP_FUND_CLAIM = "ClaimPayment_Fund";

    private EditText editTextdt, editTextdtto,editTextGroupFundPolicyNumber;

    private TextView txterrordescGroupFundCliam,txtGroupFundCliamlistcount;
    private ListView listviewGroupFundCliamList;

    private Context context;
    private CommonMethods commonMethods;

    private int mYear, mMonth, mDay, datecheck = 0;

    private ProgressDialog mProgressDialog;

    private SelectedAdapterGroupFundClaim selectedAdapterGroupFundClaim;

    private long lstGroupFundClaimListCount = 0;

    private DownloadFileAsyncGroupFundClaim taskAsyncGroupFundClaim;

    private String strtodate = "",strfromdate = "",policyNumber = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.group_fund_claim_layout);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);

        context = this;
        commonMethods = new CommonMethods();
        commonMethods.setApplicationToolbarMenu(this,"Fund Claim Payment");

        editTextdt = findViewById(R.id.editTextdt);
        editTextdtto  = findViewById(R.id.editTextdtto);
        editTextGroupFundPolicyNumber  = findViewById(R.id.editTextGroupFundPolicyNumber);

        ImageButton btndate = findViewById(R.id.btndate);
        ImageButton btnbtndateto = findViewById(R.id.btnbtndateto);

        Button btn_save = findViewById(R.id.btn_save);
        Button btn_reset = findViewById(R.id.btn_reset);

        btn_save.setOnClickListener(this);
        btn_reset.setOnClickListener(this);

        btndate.setOnClickListener(this);
        btnbtndateto.setOnClickListener(this);

        txterrordescGroupFundCliam  = findViewById(R.id.txterrordescGroupFundCliam);
        txtGroupFundCliamlistcount  = findViewById(R.id.txtGroupFundCliamlistcount);
        listviewGroupFundCliamList   = findViewById(R.id.listviewGroupFundCliamList);

        setDates();
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

        m = commonMethods.getFullMonthName(m);
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

        String todate = daeCPT + "-" + commonMethods.getFullMonthName(meCPT) + "-" + yeCPT;
        editTextdt.setText(todate);

    }
    private void updateDisplay(int year, int month, int day) {

        String y = String.valueOf(year);
        String m = String.valueOf(month + 1);
        String da = String.valueOf(day);

        m = commonMethods.getFullMonthName(m);
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

            case DATE_DIALOG_ID:
                return new DatePickerDialog(this, R.style.datepickerstyle,
                        mDateSetListener, mYear, mMonth, mDay);

            default:
                return null;
        }
    }
    @SuppressWarnings("deprecation")
    private void showDateProgressDialog() {
        showDialog(DATE_DIALOG_ID);
    }

    @SuppressWarnings("deprecation")
    private void dismissProgressDialog() {
        dismissDialog(DIALOG_DOWNLOAD_PROGRESS);
    }

    @SuppressWarnings("deprecation")
    private void showProgressDialog() {
        showDialog(DIALOG_DOWNLOAD_PROGRESS);
    }


    class DownloadFileAsyncGroupFundClaim extends AsyncTask<String, String, String> {

        private volatile boolean running = true;
        private String inputpolicylist = "",strGroupNewBusinessListErrorCOde = "",
                strfromdate = "",strtodate = "";
        @Override
        protected void onPreExecute() {
            showProgressDialog();
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

                request = new SoapObject(NAMESPACE,METHOD_NAME_GROUP_FUND_CLAIM);
                request.addProperty("strFromDate", strfromdate);
                request.addProperty("strToDate", strtodate);
                request.addProperty("strPolicyNo", policyNumber);
                request.addProperty("strCode", commonMethods.setUserDetails(context).getStrCIFBDMUserId());
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);
                envelope.dotNet = true;

                envelope.setOutputSoapObject(request);

                commonMethods.TLSv12Enable();

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);

                HttpTransportSE androidHttpTranport = new HttpTransportSE(URl);
                try {

                    String SOAP_ACTION_GROUP_FUND_CLAIM = "http://tempuri.org/ClaimPayment_Fund";
                    androidHttpTranport.call(
                            SOAP_ACTION_GROUP_FUND_CLAIM, envelope);
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

                                List<ParseXML.XMLHolderGroupFundClaimList> nodeData = prsObj
                                        .parseNodeGroupFundClaim(Node);

                                // final List<XMLHolderMaturity> lst;
                                List<ParseXML.XMLHolderGroupFundClaimList> lstGroupTermRenewal = new ArrayList<ParseXML.XMLHolderGroupFundClaimList>();
                                lstGroupTermRenewal.clear();

                                for (ParseXML.XMLHolderGroupFundClaimList node : nodeData) {

                                    lstGroupTermRenewal.add(node);
                                }

                                lstGroupFundClaimListCount = lstGroupTermRenewal.size();
                                selectedAdapterGroupFundClaim = new SelectedAdapterGroupFundClaim(
                                        context, lstGroupTermRenewal);
                                selectedAdapterGroupFundClaim.notifyDataSetChanged();

                                //registerForContextMenu(listviewGroupFundCliamList);

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
                dismissProgressDialog();
            }
            //lvMaturityList.setVisibility(View.GONE);
            if (running) {

                //txterrordescmaturity.setVisibility(View.VISIBLE);
                //txtmaturitylistcount.setVisibility(View.VISIBLE);
                if (strGroupNewBusinessListErrorCOde == null) {
                   // ll_search_GroupTermRenewal.setVisibility(View.VISIBLE);
                    listviewGroupFundCliamList.setVisibility(View.VISIBLE);

                    txterrordescGroupFundCliam.setText("");
                    txtGroupFundCliamlistcount.setText("Total Policy : "
                            + lstGroupFundClaimListCount);
                    listviewGroupFundCliamList.setAdapter(selectedAdapterGroupFundClaim);

                    //Utility.setListViewHeightBasedOnChildren(listviewGroupFundCliamList);

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

                    txterrordescGroupFundCliam.setText("No Record Found");
                    txtGroupFundCliamlistcount.setText("Total Policy : " + 0);
                    List<ParseXML.XMLHolderGroupFundClaimList> lst;
                    ParseXML.XMLHolderGroupFundClaimList node = null;
                    lst = new ArrayList<ParseXML.XMLHolderGroupFundClaimList>();
                    lst.clear();
                    lst.add(node);
                    selectedAdapterGroupFundClaim = new SelectedAdapterGroupFundClaim(
                            context, lst);
                    listviewGroupFundCliamList.setAdapter(selectedAdapterGroupFundClaim);

                    //Utility.setListViewHeightBasedOnChildren(listviewGroupFundCliamList);

                }
            } else {
                commonMethods.showMessageDialog(context, commonMethods.SERVER_ERROR);
            }
        }
    }

    class SelectedAdapterGroupFundClaim extends BaseAdapter {

        // used to keep selected position in ListView
        private int selectedPos = -1; // init value for not-selected

        private Context adapterContext;
        List<ParseXML.XMLHolderGroupFundClaimList> lst;

        SelectedAdapterGroupFundClaim(Context adapterContext, List<ParseXML.XMLHolderGroupFundClaimList> lst) {
            this.adapterContext = adapterContext;
            this.lst = lst;
        }

        public void setSelectedPosition(int pos) {
            selectedPos = pos;
            // inform the view of this change
            notifyDataSetChanged();
        }

        @Override
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
        }

        public int getSelectedPosition() {
            return selectedPos;
        }

        @Override
        public int getCount() {
            return lst.size();
        }

        @Override
        public Object getItem(int i) {
            return lst.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        class Holder{
            TextView textviewMemberId,textviewMemberName,textviewClaimSanctionDate,textviewClaimAmount;
        }

        @SuppressWarnings("deprecation")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            Holder mHolder;

            // only inflate the view if it's null
            if (convertView == null) {
                /*LayoutInflater vi = (LayoutInflater) this.getContext()
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.group_fund_claim_list_item, null);*/

                convertView=LayoutInflater.from(adapterContext).inflate(R.layout.group_fund_claim_list_item, parent, false);
                mHolder = new Holder();

                mHolder.textviewMemberId = convertView.findViewById(R.id.textviewMemberId);
                mHolder.textviewMemberName = convertView.findViewById(R.id.textviewMemberName);
                mHolder.textviewClaimSanctionDate = convertView.findViewById(R.id.textviewClaimSanctionDate);
                mHolder.textviewClaimAmount = convertView.findViewById(R.id.textviewClaimAmount);

                convertView.setTag(mHolder);
            }else{
                mHolder = (Holder) convertView.getTag();
            }


            mHolder.textviewMemberId.setText(lst.get(position).getEmployee_id() == null ? ""
                        : lst.get(position).getEmployee_id());

                /*WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
                Display display = wm.getDefaultDisplay();
                int screenWidth = display.getWidth(); // Get full screen width

                int eightyPercent = (screenWidth * 50) / 100;*/

                String memberName = lst.get(position).getMEMBER_NAME() == null ? ""
                        : lst.get(position).getMEMBER_NAME();

            mHolder.textviewMemberName.setText(memberName);

                /*float textWidthLifeAssuredName = textviewMemberName.getPaint().measureText(memberName);

                float linesFloat = (textWidthLifeAssuredName/eightyPercent) + 0.7f;
                int linesInt = Math.round(linesFloat);

                int numberOfLineslifeAssuredName =  linesInt ;

                textviewMemberName.setLines(numberOfLineslifeAssuredName);*/

            mHolder.textviewClaimSanctionDate.setText(lst.get(position).getCLAIM_SANCTION_DATE() == null ? ""
                        : lst.get(position).getCLAIM_SANCTION_DATE());

            mHolder.textviewClaimAmount.setText(lst.get(position).getCLAIM_AMOUNT() == null ? ""
                        : lst.get(position).getCLAIM_AMOUNT());

            return convertView;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_save:

                listviewGroupFundCliamList.setVisibility(View.GONE);
                txterrordescGroupFundCliam.setVisibility(View.GONE);
                txtGroupFundCliamlistcount.setVisibility(View.GONE);
                txterrordescGroupFundCliam.setText("");
                txterrordescGroupFundCliam.setText("");
                if(selectedAdapterGroupFundClaim!=null)
                {
                    selectedAdapterGroupFundClaim.notifyDataSetChanged();
                }
                getGroupFundClailmList();
                break;
            case R.id.btn_reset:
                txterrordescGroupFundCliam.setText("");
                txterrordescGroupFundCliam.setText("");
                resetFields();
                break;

            /*case R.id.btn_search_GroupTermRenewal_by_name:
                searchByName();
                break;
            case R.id.btn_search_GroupTermRenewal_by_policy:
                searchByPolicyNumber();
                break;
            case R.id.btn_search_GroupTermRenewal_by_end_date:
                searchByEndDate();
                break;*/
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

    private void resetFields() {

        listviewGroupFundCliamList.setVisibility(View.GONE);
        txterrordescGroupFundCliam.setVisibility(View.GONE);
        txtGroupFundCliamlistcount.setVisibility(View.GONE);
        //ll_search_GroupTermRenewal.setVisibility(View.GONE);
        if(selectedAdapterGroupFundClaim!=null)
        {
            selectedAdapterGroupFundClaim.notifyDataSetChanged();
        }

        editTextdt.setText("");
        editTextdtto.setText("");

        txterrordescGroupFundCliam.setText("");
        txtGroupFundCliamlistcount.setText("");

        Calendar cal = Calendar.getInstance();
        mYear = cal.get(Calendar.YEAR);
        mMonth = cal.get(Calendar.MONTH);
        mDay = cal.get(Calendar.DAY_OF_MONTH);
    }

    private void getGroupFundClailmList() {
        taskAsyncGroupFundClaim = new DownloadFileAsyncGroupFundClaim();
        txtGroupFundCliamlistcount.setVisibility(View.VISIBLE);

        strfromdate = editTextdt.getText().toString();
        strtodate = editTextdtto.getText().toString();

        policyNumber = editTextGroupFundPolicyNumber.getText().toString();

        if (strfromdate.equalsIgnoreCase("")
                || strtodate.equalsIgnoreCase("")||policyNumber.equalsIgnoreCase("")) {
            commonMethods.showMessageDialog(context, "All Fields Required..");
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

                if (commonMethods.isNetworkConnected(context)) {
                   // edt_search_GroupTermRenewal_by_policy.setText("");
                   // edt_search_GroupTermRenewal_by_name.setText("");
                   // edt_search_GroupTermRenewal_by_end_date.setText("");
                    service_hits(METHOD_NAME_GROUP_FUND_CLAIM);

                } else {
                    commonMethods.showMessageDialog(context,
                            "Internet Connection Not Present,Try again..");
                }
            } else {
                commonMethods.showMessageDialog(context,
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

        CommonMethods.UserDetailsValuesModel userDetailsValuesModel = commonMethods.setUserDetails(context);
        String strCIFBDMUserId = userDetailsValuesModel.getStrCIFBDMUserId();
        String strCIFBDMEmailId = userDetailsValuesModel.getStrCIFBDMEmailId();
        String strCIFBDMPassword = userDetailsValuesModel.getStrCIFBDMPassword();
        String  strCIFBDMMObileNo = userDetailsValuesModel.getStrCIFBDMMObileNo();

        ServiceHits service = new ServiceHits(context,strServiceName, str.toString(),
                strCIFBDMUserId, strCIFBDMEmailId, strCIFBDMMObileNo,
                strCIFBDMPassword, this);
        service.execute();
    }
    @Override
    public void downLoadData() {
        taskAsyncGroupFundClaim = new DownloadFileAsyncGroupFundClaim();
        taskAsyncGroupFundClaim.execute();
    }
}
