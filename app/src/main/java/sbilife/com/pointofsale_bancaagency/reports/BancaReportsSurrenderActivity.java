package sbilife.com.pointofsale_bancaagency.reports;

import android.annotation.SuppressLint;
import androidx.appcompat.app.AppCompatActivity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Html;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import sbilife.com.pointofsale_bancaagency.CustomerDetailActivity;
import sbilife.com.pointofsale_bancaagency.ParseXML;
import sbilife.com.pointofsale_bancaagency.ParseXML.XMLHolderSurrender;
import sbilife.com.pointofsale_bancaagency.R;
import sbilife.com.pointofsale_bancaagency.ServiceURL;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;
import sbilife.com.pointofsale_bancaagency.common.SimpleCrypto;
import sbilife.com.pointofsale_bancaagency.Utility;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods.UserDetailsValuesModel;
import sbilife.com.pointofsale_bancaagency.reports.ServiceHits.DownLoadData;

public class BancaReportsSurrenderActivity extends AppCompatActivity implements OnClickListener, DownLoadData {

    private final String METHOD_NAME_SURRENDER_LIST = "getCIFPoliciesSurrenderList";

    private DownloadFileAsyncSurrender taskSurrender;
    private CommonMethods mCommonMethods;
    private Context context;

    private final int DIALOG_DOWNLOAD_PROGRESS = 0;
    private ProgressDialog mProgressDialog;

    private String strCIFBDMUserId = "", strCIFBDMEmailId = "",
            strCIFBDMPassword = "", strCIFBDMMObileNo = "", strsurrenderstring = "";

    private int mYear, mMonth, mDay, datecheck = 0;
    private final int DATE_DIALOG_ID = 1;

    private Spinner spinisurrenderstring;

    private ListView SurrenderlistView1;
    private LinearLayout lnsearchsurrenderlist;
    private TextView txterrordescsurrender, txtsurrenderlistcount;

    private EditText edSearchSurrenderPN, edSearchSurrenderFN, edSearchSurrenderWD;

    private SelectedAdapterSurrender selectedAdapterSurrender;
    private ArrayList<XMLHolderSurrender> lstSurrender;
    private ArrayList<XMLHolderSurrender> array_sort;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.banca_reports_surrender);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);
        context = this;
        mCommonMethods = new CommonMethods();

        mCommonMethods.setApplicationToolbarMenu(this, "Surrender");

        taskSurrender = new DownloadFileAsyncSurrender();
        mProgressDialog = new ProgressDialog(context);

        Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        spinisurrenderstring = findViewById(R.id.spinisurrenderstring);
        Button btn_savesurrenderlist = findViewById(R.id.btn_savesurrenderlist);
        Button btn_reset_surrender_list = findViewById(R.id.btn_reset_surrender_list);
        Button btn_click_surrender_policyno = findViewById(R.id.btn_click_surrender_policyno);
        Button btn_click_surrender_fn = findViewById(R.id.btn_click_surrender_fn);
        Button btn_click_surrrender_withdrawaldate = findViewById(R.id.btn_click_surrrender_withdrawaldate);

        SurrenderlistView1 = findViewById(R.id.SurrenderlistView1);
        lnsearchsurrenderlist = findViewById(R.id.lnsearchsurrenderlist);
        txterrordescsurrender = findViewById(R.id.txterrordescsurrender);
        txtsurrenderlistcount = findViewById(R.id.txtsurrenderlistcount);
        edSearchSurrenderPN = findViewById(R.id.edSearchSurrenderPN);
        edSearchSurrenderFN = findViewById(R.id.edSearchSurrenderFN);
        edSearchSurrenderWD = findViewById(R.id.edSearchSurrenderWD);

        ImageButton imageButtonSurrenderWD = findViewById(R.id.imageButtonSurrenderWD);
        imageButtonSurrenderWD.setOnClickListener(this);

        btn_savesurrenderlist.setOnClickListener(this);
        btn_reset_surrender_list.setOnClickListener(this);
        btn_click_surrender_policyno.setOnClickListener(this);
        btn_click_surrender_fn.setOnClickListener(this);
        btn_click_surrrender_withdrawaldate.setOnClickListener(this);

        TableRow tblsursdt = findViewById(R.id.tblsursdt);
        tblsursdt.setVisibility(View.GONE);
        TableRow tblsurendt = findViewById(R.id.tblsurendt);
        tblsurendt.setVisibility(View.GONE);
        TableRow tblsurendef = findViewById(R.id.tblsurendef);
        tblsurendef.setVisibility(View.GONE);

        spinisurrenderstring
                .setOnItemSelectedListener(new OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int pos, long id) {
                        strsurrenderstring = spinisurrenderstring
                                .getSelectedItem().toString();

                        if (strsurrenderstring
                                .contentEquals("Surrender Disinvested")) {
                            strsurrenderstring = "D";
                        } else if (strsurrenderstring
                                .contentEquals("Surrender Initiated")) {
                            strsurrenderstring = "I";
                        } else if (strsurrenderstring
                                .contentEquals("Surrendered Reinvested")) {
                            strsurrenderstring = "R";
                        } else if (strsurrenderstring
                                .contentEquals("Surrender")) {
                            strsurrenderstring = "S";
                        } else if (strsurrenderstring
                                .contentEquals("Surrendered Reinvested Auto")) {
                            strsurrenderstring = "RA";
                        }
                    }

                    public void onNothingSelected(AdapterView<?> arg0) {
                        // TODO Auto-generated method stub
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
    }

    private void getUserDetails() {
        UserDetailsValuesModel userDetailsValuesModel = mCommonMethods.setUserDetails(context);
        strCIFBDMUserId = userDetailsValuesModel.getStrCIFBDMUserId();
        strCIFBDMEmailId = userDetailsValuesModel.getStrCIFBDMEmailId();
        strCIFBDMPassword = userDetailsValuesModel.getStrCIFBDMPassword();
        strCIFBDMMObileNo = userDetailsValuesModel.getStrCIFBDMMObileNo();
        System.out.println("strCIFBDMUserId:" + strCIFBDMUserId + " strCIFBDMEmailId:"
                + strCIFBDMEmailId + " strCIFBDMPassword:" + strCIFBDMPassword
                + " strCIFBDMMObileNo:" + strCIFBDMMObileNo);
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

                                if (taskSurrender != null) {
                                    taskSurrender.cancel(true);
                                }
                                if (mProgressDialog.isShowing()) {
                                    mProgressDialog.dismiss();
                                }
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

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        MenuInflater inflater = getMenuInflater();

        menu.setHeaderTitle("Services");

        int id = v.getId();
        if (id == R.id.SurrenderlistView1) {
            inflater.inflate(R.menu.menu_surrender, menu);
        }


    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
                .getMenuInfo();

        if (item.getTitle().toString().contentEquals("Customer Details")) {
            int itemId = item.getItemId();
            if (itemId == R.id.surcust) {
                String strsurCustomerId = array_sort.get(info.position)
                        .getHolderId();
                if (strsurCustomerId != "" || strsurCustomerId != null) {
                    Intent intent = new Intent(context,
                            CustomerDetailActivity.class);
                    intent.putExtra("CustomerId", strsurCustomerId);
                    intent.putExtra("strUserType", "");
                    intent.putExtra("strAgentCode", strCIFBDMUserId);
                    intent.putExtra("strEmail", strCIFBDMEmailId);
                    intent.putExtra("strMobileNo", strCIFBDMMObileNo);
                    intent.putExtra("strPassword", strCIFBDMPassword.trim());
                    startActivity(intent);
                }
                return true;
            }
        }

        return true;

    }

    private void updateDisplay(int year, int month, int day) {

        String y = String.valueOf(year);
        String m = String.valueOf(month + 1);
        String da = String.valueOf(day);
        // String totaldate = m + "-" + da + "-" + y;

        if (m.contentEquals("1")) {
            m = "January";

        } else if (m.contentEquals("2")) {
            m = "February";

        } else if (m.contentEquals("3")) {
            m = "March";

        } else if (m.contentEquals("4")) {
            m = "April";

        } else if (m.contentEquals("5")) {
            m = "May";

        } else if (m.contentEquals("6")) {
            m = "June";

        } else if (m.contentEquals("7")) {
            m = "July";

        } else if (m.contentEquals("8")) {
            m = "August";

        } else if (m.contentEquals("9")) {
            m = "September";

        } else if (m.contentEquals("10")) {
            m = "October";

        } else if (m.contentEquals("11")) {
            m = "November";

        } else if (m.contentEquals("12")) {
            m = "December";

        }

        String totaldate = da + "-" + m + "-" + y;

        if (datecheck == 4) {
            edSearchSurrenderWD.setText(totaldate);
        }
    }

    private final DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;
            updateDisplay(mYear, mMonth, mDay);

        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            if (taskSurrender != null) {
                taskSurrender.cancel(true);
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
    private void dismissProgressDialog() {
        dismissDialog(DIALOG_DOWNLOAD_PROGRESS);
    }

    @SuppressWarnings("deprecation")
    private void showProgressDialog() {
        showDialog(DIALOG_DOWNLOAD_PROGRESS);
    }

    @SuppressWarnings("deprecation")
    private void showDateDialog() {
        showDialog(DATE_DIALOG_ID);
    }

    class DownloadFileAsyncSurrender extends AsyncTask<String, String, String> {

        private volatile boolean running = true;
        private String inputpolicylist = "", strSurrenderListErrorCOde = "";
        private int lstSurrenderListCount = 0;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
        }

        @Override
        protected String doInBackground(String... aurl) {
            try {

                running = true;
                SoapObject request = null;

                String UserType = mCommonMethods.GetUserType(context);

                if (UserType.contentEquals("MAN")
                        || UserType.contentEquals("BDM")
                        || UserType.contentEquals("TBDM")
                        || UserType.contentEquals("CIF")) {
                    String NAMESPACE = "http://tempuri.org/";
                    request = new SoapObject(NAMESPACE,
                            METHOD_NAME_SURRENDER_LIST);
                    request.addProperty("strCifNo", strCIFBDMUserId);

                    request.addProperty("surrenderSt", strsurrenderstring);
                    request.addProperty("strEmailId", strCIFBDMEmailId);
                    request.addProperty("strMobileNo", strCIFBDMMObileNo);
                    request.addProperty("strAuthKey", strCIFBDMPassword.trim());
                }

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
                        String SOAP_ACTION_SURRENDER_LIST = "http://tempuri.org/getCIFPoliciesSurrenderList";
                        androidHttpTranport.call(SOAP_ACTION_SURRENDER_LIST,
                                envelope);
                    }
                    Object response = envelope.getResponse();

                    if (!response.toString().contentEquals("anyType{}")) {
                        SoapPrimitive sa;
                        try {
                            sa = (SoapPrimitive) envelope.getResponse();
                            inputpolicylist = sa.toString();

                            ParseXML prsObj = new ParseXML();

                            inputpolicylist = prsObj.parseXmlTag(
                                    inputpolicylist, "CIFPolicyList");
                            inputpolicylist = new ParseXML().parseXmlTag(
                                    inputpolicylist, "ScreenData");
                            strSurrenderListErrorCOde = inputpolicylist;

                            if (strSurrenderListErrorCOde == null) {
                                inputpolicylist = sa.toString();
                                inputpolicylist = prsObj.parseXmlTag(
                                        inputpolicylist, "CIFPolicyList");

                                List<String> Node = prsObj.parseParentNode(
                                        inputpolicylist, "Table");

                                List<XMLHolderSurrender> nodeData = prsObj
                                        .parseNodeElementSurrender(Node);

                                // final List<XMLHolderSurrender> lst;
                                lstSurrender = new ArrayList<>();
                                lstSurrender.clear();

                                lstSurrender.addAll(nodeData);

                                lstSurrenderListCount = lstSurrender.size();
                                array_sort = new ArrayList<>(lstSurrender);

                                selectedAdapterSurrender = new SelectedAdapterSurrender(
                                        context, lstSurrender);
                                selectedAdapterSurrender
                                        .setNotifyOnChange(true);

                                registerForContextMenu(SurrenderlistView1);

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

        @Override
        protected void onPostExecute(String unused) {
            if (mProgressDialog.isShowing()) {
                dismissProgressDialog();
            }
            if (running) {
                txterrordescsurrender.setVisibility(View.VISIBLE);
                txtsurrenderlistcount.setVisibility(View.VISIBLE);
                SurrenderlistView1.setVisibility(View.VISIBLE);

                if (strSurrenderListErrorCOde == null) {
                    lnsearchsurrenderlist.setVisibility(View.VISIBLE);

                    txterrordescsurrender.setText("");
                    txtsurrenderlistcount.setText("Total Policy : "
                            + lstSurrenderListCount);
                    SurrenderlistView1.setAdapter(selectedAdapterSurrender);

                    Utility.setListViewHeightBasedOnChildren(SurrenderlistView1);
                } else {
                    lnsearchsurrenderlist.setVisibility(View.GONE);

                    txterrordescsurrender.setText("No Record Found");
                    txtsurrenderlistcount.setText("Total Policy : " + 0);
                    List<XMLHolderSurrender> lst;
                    lst = new ArrayList<>();
                    lst.clear();
                    lst.add(null);
                    selectedAdapterSurrender = new SelectedAdapterSurrender(
                            context, lst);
                    selectedAdapterSurrender.setNotifyOnChange(true);
                    SurrenderlistView1.setAdapter(selectedAdapterSurrender);

                    Utility.setListViewHeightBasedOnChildren(SurrenderlistView1);
                }
            } else {
                mCommonMethods.showMessageDialog(context, mCommonMethods.SERVER_ERROR);
            }
        }
    }

    class SelectedAdapterSurrender extends ArrayAdapter<XMLHolderSurrender> {


        final List<XMLHolderSurrender> lst;

        SelectedAdapterSurrender(Context context,
                                 List<XMLHolderSurrender> objects) {
            super(context, 0, objects);

            lst = objects;
        }


        @SuppressLint("InflateParams")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            // only inflate the view if it's null
            if (v == null) {
                LayoutInflater vi = (LayoutInflater) this.getContext()
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.list_item_surrenderlist, null);
            }

            // get text view
            TextView txtno = v.findViewById(R.id.txtsurrenderno);
            TextView txtcustomercode = v
                    .findViewById(R.id.txtcustomercode);
            TextView fname = v
                    .findViewById(R.id.txtsurrenderfirstname);
            /*
             * TextView lname =
             * (TextView)v.findViewById(R.id.txtsurrenderlastname);
             */
            TextView status = v
                    .findViewById(R.id.txtstatussurrenderwidthdrowdate);

            boolean i = lst.contains(null);

            if (!i) {
                if (lst.get(position).getStatus().contentEquals("Inforce")) {
                    status.setBackgroundColor(Color.parseColor("#008000"));
                } else if (lst.get(position).getStatus().contentEquals("Lapse")) {
                    status.setBackgroundColor(Color.parseColor("#FFA500"));
                } else if (lst.get(position).getStatus()
                        .contentEquals("Technical Lapse")) {
                    status.setBackgroundColor(Color.parseColor("#FFFF00"));
                } else if (lst.get(position).getStatus()
                        .contentEquals("Maturity")) {
                    status.setBackgroundColor(Color.parseColor("#0000FF"));
                } else if (lst.get(position).getStatus().contentEquals("Claim")) {
                    status.setBackgroundColor(Color.parseColor("#FFC0CB"));
                } else if (lst.get(position).getStatus()
                        .contentEquals("Surrender")) {
                    status.setBackgroundColor(Color.parseColor("#FF0000"));
                }

                txtno.setText(lst.get(position).getNo() == null ? "" : lst.get(
                        position).getNo());
                txtcustomercode
                        .setText(lst.get(position).getHolderId() == null ? ""
                                : lst.get(position).getHolderId());

                String fnm = lst.get(position).getFName() == null ? "" : lst
                        .get(position).getFName();
                String lnm = lst.get(position).getLName() == null ? "" : lst
                        .get(position).getLName();
                String name = fnm + " " + lnm;

                fname.setText(name);

                status.setText(lst.get(position).getStatus() == null ? "" : lst
                        .get(position).getStatus());
            }

            return (v);
        }
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();
        switch (id) {
            case R.id.btn_savesurrenderlist:
                getSurrenderList();
                break;
            case R.id.btn_reset_surrender_list:
                resetSurrenderList();
                break;
            case R.id.btn_click_surrender_policyno:
                searchBySurrenderPolicyNo();
                break;
            case R.id.btn_click_surrender_fn:
                searchBySurrenderFirstName();
                break;
            case R.id.btn_click_surrrender_withdrawaldate:
                searchBySurrenderWithdrawalDate();
                break;
            case R.id.imageButtonSurrenderWD:
                datecheck = 4;
                showDateDialog();
                break;
        }


    }

    private void getSurrenderList() {
        taskSurrender = new DownloadFileAsyncSurrender();

        lnsearchsurrenderlist.setVisibility(View.GONE);

        array_sort = new ArrayList<>();
        array_sort.clear();
        txtsurrenderlistcount.setText("");
        if (selectedAdapterSurrender != null) {
            selectedAdapterSurrender.clear();
            selectedAdapterSurrender.setNotifyOnChange(true);
        }


        txtsurrenderlistcount.setVisibility(View.GONE);
        txterrordescsurrender.setVisibility(View.GONE);
        SurrenderlistView1.setVisibility(View.GONE);

        if (strsurrenderstring.contentEquals("Select Status")) {
            mCommonMethods.showMessageDialog(context, "Please Select Status..");
        } else {
            if (mCommonMethods.isNetworkConnected(context)) {
                edSearchSurrenderPN.setText("");
                edSearchSurrenderFN.setText("");
                edSearchSurrenderWD.setText("");
                service_hits();
            } else {
                mCommonMethods.showMessageDialog(context, mCommonMethods.NO_INTERNET_MESSAGE);
            }
        }

    }

    private void service_hits() {

        ServiceHits service = new ServiceHits(context,
                METHOD_NAME_SURRENDER_LIST, strsurrenderstring, strCIFBDMUserId,
                strCIFBDMEmailId, strCIFBDMMObileNo,
                strCIFBDMPassword, this);
        service.execute();

    }


    private void startDownloadSurrenderList() {
        taskSurrender = new DownloadFileAsyncSurrender();
        taskSurrender.execute("demo");
    }

    @Override
    public void downLoadData() {
        startDownloadSurrenderList();
    }

    private void resetSurrenderList() {

        lnsearchsurrenderlist.setVisibility(View.GONE);
        spinisurrenderstring.setSelection(0);


        array_sort = new ArrayList<>();
        array_sort.clear();
        txtsurrenderlistcount.setText("");
        if (selectedAdapterSurrender != null) {
            selectedAdapterSurrender.clear();
            selectedAdapterSurrender.setNotifyOnChange(true);
        }


        txtsurrenderlistcount.setVisibility(View.GONE);
        txterrordescsurrender.setVisibility(View.GONE);
        SurrenderlistView1.setVisibility(View.GONE);
    }

    private void searchBySurrenderPolicyNo() {

        if (edSearchSurrenderPN.getText().toString()
                .equalsIgnoreCase("")) {
            //policynoalert();
            mCommonMethods.showMessageDialog(context, mCommonMethods.POLICY_NUMBER_ALERT);
        } else {
            mCommonMethods.hideKeyboard(edSearchSurrenderPN, context);

            array_sort = new ArrayList<>();
            array_sort.clear();
            String str = edSearchSurrenderPN.getText().toString().trim().toLowerCase();
            for (XMLHolderSurrender node : lstSurrender) {
                String policyNumber = node.getNo().trim().toLowerCase();
                if (policyNumber.contains(str) || policyNumber.startsWith(str)) {
                    array_sort.add(node);
                }
            }

            selectedAdapterSurrender = new SelectedAdapterSurrender(
                    context, array_sort);
            selectedAdapterSurrender.setNotifyOnChange(true);
            SurrenderlistView1.setAdapter(selectedAdapterSurrender);
            Utility.setListViewHeightBasedOnChildren(SurrenderlistView1);
        }
    }

    private void searchBySurrenderFirstName() {

        // TODO Auto-generated method stub

        if (edSearchSurrenderFN.getText().toString()
                .equalsIgnoreCase("")) {
            //fnalert();
            mCommonMethods.showMessageDialog(context, mCommonMethods.FIRST_NAME_ALERT);
        } else {
            mCommonMethods.hideKeyboard(edSearchSurrenderFN, context);

            array_sort = new ArrayList<>();
            array_sort.clear();
            String str = edSearchSurrenderFN.getText().toString().trim().toLowerCase();
            for (XMLHolderSurrender node : lstSurrender) {
               /* if (str.contains(node.getFName())) {
                    array_sort.add(node);
                }*/

                String firstname = node.getFName().trim().toLowerCase();
                if (firstname.contains(str) || firstname.startsWith(str)) {
                    array_sort.add(node);
                }
            }

            selectedAdapterSurrender = new SelectedAdapterSurrender(
                    context, array_sort);
            selectedAdapterSurrender.setNotifyOnChange(true);
            SurrenderlistView1.setAdapter(selectedAdapterSurrender);
            Utility.setListViewHeightBasedOnChildren(SurrenderlistView1);
        }
    }

    private void searchBySurrenderWithdrawalDate() {

        // TODO Auto-generated method stub

        if (edSearchSurrenderWD.getText().toString()
                .equalsIgnoreCase("")) {
            //duedatealert();
            mCommonMethods.showMessageDialog(context, mCommonMethods.DATE_ALERT);
        } else {
            mCommonMethods.hideKeyboard(edSearchSurrenderWD, context);

            array_sort = new ArrayList<>();
            array_sort.clear();
            String str = edSearchSurrenderWD.getText()
                    .toString().trim().toLowerCase();
            for (XMLHolderSurrender node : lstSurrender) {
                /*if (str.contains(node.getStatus())) {
                    array_sort.add(node);
                }*/
                String status = node.getStatus().trim().toLowerCase();
                if (status.contains(str) || status.startsWith(str)) {
                    array_sort.add(node);
                }

            }

            selectedAdapterSurrender = new SelectedAdapterSurrender(
                    context, array_sort);
            selectedAdapterSurrender.setNotifyOnChange(true);
            SurrenderlistView1
                    .setAdapter(selectedAdapterSurrender);
            Utility.setListViewHeightBasedOnChildren(SurrenderlistView1);
        }
    }


}
