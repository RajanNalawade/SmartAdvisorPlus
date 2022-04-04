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
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import java.util.List;

import sbilife.com.pointofsale_bancaagency.common.CommonMethods;
import sbilife.com.pointofsale_bancaagency.home.dashboard.NewDashboardAgent;
import sbilife.com.pointofsale_bancaagency.home.dashboard.NewDashboardCIF;
import sbilife.com.pointofsale_bancaagency.ParseXML;
import sbilife.com.pointofsale_bancaagency.ParseXML.XMLHolderBdm;
import sbilife.com.pointofsale_bancaagency.R;
import sbilife.com.pointofsale_bancaagency.ServiceURL;
import sbilife.com.pointofsale_bancaagency.Utility;
import sbilife.com.pointofsale_bancaagency.common.SimpleCrypto;
import sbilife.com.pointofsale_bancaagency.home.lmcorner.COTTOTTentativeReportActivity;
import sbilife.com.pointofsale_bancaagency.home.lmcorner.CovidDoghNextActivity;
import sbilife.com.pointofsale_bancaagency.home.lmcorner.CovidSelfDeclarationActivity;
import sbilife.com.pointofsale_bancaagency.home.lmcorner.JOTCActivity;
import sbilife.com.pointofsale_bancaagency.home.lmcorner.LMClubMembershipReportActivity;
import sbilife.com.pointofsale_bancaagency.home.lmcorner.MonthlyGraduationAllowanceReportActivity;
import sbilife.com.pointofsale_bancaagency.home.npsscore.NPSScoreActivity;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods.UserDetailsValuesModel;
import sbilife.com.pointofsale_bancaagency.reports.ServiceHits.DownLoadData;
import sbilife.com.pointofsale_bancaagency.reports.agency.AgencyReportsAdvisorProposalsStatusActivity;
import sbilife.com.pointofsale_bancaagency.reports.agency.AgencyReportsCommissionActivity;
import sbilife.com.pointofsale_bancaagency.reports.agency.AgencyReportsMandateRegistrationStatusActivity;
import sbilife.com.pointofsale_bancaagency.reports.agency.AgencyReportsMaturityActivity;
import sbilife.com.pointofsale_bancaagency.reports.agency.AgencyReportsPersistency;
import sbilife.com.pointofsale_bancaagency.reports.agency.AgencyReportsPolicyDetailsActivity;
import sbilife.com.pointofsale_bancaagency.reports.agency.AgencyReportsPolicyListActivity;
import sbilife.com.pointofsale_bancaagency.reports.agency.AgencyReportsProposalTracker;
import sbilife.com.pointofsale_bancaagency.reports.agency.AgencyReportsSBDueListActivity;
import sbilife.com.pointofsale_bancaagency.reports.agency.AgencyReportsSurrenderActivity;
import sbilife.com.pointofsale_bancaagency.reports.commonreports.AlternetModeCollectionStatusActivity;
import sbilife.com.pointofsale_bancaagency.reports.commonreports.AutoMandateRegistrationStatusListReportsActivity;
import sbilife.com.pointofsale_bancaagency.reports.commonreports.CommonReportsProposalListActivity;
import sbilife.com.pointofsale_bancaagency.reports.commonreports.DownloadPPCActivity;
import sbilife.com.pointofsale_bancaagency.reports.commonreports.MedicalPendingRequirementActivity;
import sbilife.com.pointofsale_bancaagency.reports.commonreports.PolicyDispatchStatusActivity;
import sbilife.com.pointofsale_bancaagency.reports.commonreports.PremiumPaymentReceiptActivity;
import sbilife.com.pointofsale_bancaagency.reports.commonreports.ProposalRinnStatusActivity;
import sbilife.com.pointofsale_bancaagency.reports.commonreports.RevivalCampaignReportList;
import sbilife.com.pointofsale_bancaagency.reports.commonreports.ViewMedicalStatusActivity;
import sbilife.com.pointofsale_bancaagency.reports.newbusiness.BancaReportsPIWCActivity;
import sbilife.com.pointofsale_bancaagency.reports.newbusiness.InstaImageFailureCasesActivity;
import sbilife.com.pointofsale_bancaagency.reports.newbusiness.NRIPaymentLinkActivity;
import sbilife.com.pointofsale_bancaagency.reports.newbusiness.PIWCAudioCallingPendingListActivity;
import sbilife.com.pointofsale_bancaagency.reports.newbusiness.SendMHRLinkAOLActivity;
import sbilife.com.pointofsale_bancaagency.reports.newbusiness.SendSMSAlternateProcessActivity;
import sbilife.com.pointofsale_bancaagency.reports.policyservicing.SendMandateLinkActivity;
import sbilife.com.pointofsale_bancaagency.reports.policyservicing.UnclaimedDataActivity;
import sbilife.com.pointofsale_bancaagency.reports.policyservicing.UnrealizedDataActivity;
import sbilife.com.pointofsale_bancaagency.reports.underwriting.SendLinkActivity;
import sbilife.com.pointofsale_bancaagency.reports.underwriting.ViewMedicalStatusTeleMERActivity;
import sbilife.com.pointofsale_bancaagency.utility.DocUploadNonMedicalPendingActivity;
import sbilife.com.pointofsale_bancaagency.utility.DocumentsUploadActivity;

@SuppressLint("InflateParams")
public class BancaReportsCIFListActivity extends AppCompatActivity implements OnClickListener, DownLoadData {

    private final String METHOD_NAME_BDM_CIF_LIST = "getBDMCIFList";


    private final String METHOD_NAME_AGENT_UM_LIST = "getUMAgentList";

    private DownloadFileAsyncBdm taskBdm;
    private final int DIALOG_DOWNLOAD_PROGRESS = 0;
    private ProgressDialog mProgressDialog;
    //private  final int DATE_DIALOG_ID = 1;

    private CommonMethods mCommonMethods;
    private Context context;

    private ArrayList<XMLHolderBdm> lstBDMList;

    private String strCIFBDMUserId = "", strCIFBDMEmailId = "",
            strCIFBDMPassword = "", strCIFBDMMObileNo = "",
            strbdmstatusstring = "", strUserType = "";
    private int lstBdmListCount = 0;

    private SelectedAdapterBdm selectedAdapterBdm;

    private ListView BDMlistView1;

    private Spinner spinSearchBDMUMBN, spinBDMStringStatus;
    private LinearLayout lnsearchbdmlist;
    private TextView txterrordescbdm, txtbdmlistcount, txtbdmumcode;
    private EditText edSearchBDMUMCode, edSearchBDMUMFN;

    private ArrayList<XMLHolderBdm> array_sort;

    private TableRow tblum_bdm_bnk_br;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.banca_reports_bdm_cif_list);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);


        context = this;
        mCommonMethods = new CommonMethods();
        mProgressDialog = new ProgressDialog(context);
        Intent intent = getIntent();
        String fromHome = intent.getStringExtra("fromHome");

        if (fromHome.equalsIgnoreCase("Y")) {
            strUserType = mCommonMethods.GetUserType(context);
            getUserDetails();
        } else {
            strCIFBDMUserId = intent.getStringExtra("strBDMCifCOde");
            strCIFBDMEmailId = intent.getStringExtra("strEmailId");
            strCIFBDMMObileNo = intent.getStringExtra("strMobileNo");
            strUserType = intent.getStringExtra("strUserType");

            try {
                strCIFBDMPassword = SimpleCrypto.encrypt("SBIL", "sbil");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (strUserType.equalsIgnoreCase("UM")) {
            mCommonMethods.setApplicationToolbarMenu(this, "Agent List");
        } else {
            mCommonMethods.setApplicationToolbarMenu(this, "CIF List");
        }


        array_sort = new ArrayList<>();
        taskBdm = new DownloadFileAsyncBdm();
        tblum_bdm_bnk_br = findViewById(R.id.tblum_bdm_bnk_br);
        spinSearchBDMUMBN = findViewById(R.id.spinSearchBDMUMBN);
        spinBDMStringStatus = findViewById(R.id.spbdnstatus);
        BDMlistView1 = findViewById(R.id.BdnlistView11);

        lnsearchbdmlist = findViewById(R.id.lnsearchbdmlist);
        txterrordescbdm = findViewById(R.id.txterrordescbdn);
        txtbdmlistcount = findViewById(R.id.txtbdnlistcount);
        txtbdmumcode = findViewById(R.id.txtbdmumcode);

        Button btn_save_bdn = findViewById(R.id.btn_save_bdn);
        Button btn_reset_bdn = findViewById(R.id.btn_reset_bdn);
        Button btn_click_bdmum_bn = findViewById(R.id.btn_click_bdmum_bn);
        Button btn_click_bdmum_cod = findViewById(R.id.btn_click_bdmum_cod);
        Button btn_click_bdmum_fn = findViewById(R.id.btn_click_bdmum_fn);

        edSearchBDMUMCode = findViewById(R.id.edSearchBDMUMCode);
        edSearchBDMUMFN = findViewById(R.id.edSearchBDMUMFN);

        btn_save_bdn.setOnClickListener(this);
        btn_reset_bdn.setOnClickListener(this);
        btn_click_bdmum_bn.setOnClickListener(this);
        btn_click_bdmum_cod.setOnClickListener(this);
        btn_click_bdmum_fn.setOnClickListener(this);


        spinBDMStringStatus.setOnItemSelectedListener(new OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> arg0, View arg1, int pos, long id) {
                strbdmstatusstring = spinBDMStringStatus.getSelectedItem().toString();
            }

            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    private void getUserDetails() {
        UserDetailsValuesModel userDetailsValuesModel = mCommonMethods.setUserDetails(context);
        strCIFBDMUserId = userDetailsValuesModel.getStrCIFBDMUserId();
        strCIFBDMEmailId = userDetailsValuesModel.getStrCIFBDMEmailId();
        strCIFBDMPassword = userDetailsValuesModel.getStrCIFBDMPassword();
        strCIFBDMMObileNo = userDetailsValuesModel.getStrCIFBDMMObileNo();

    }

    private void service_hits(String strServiceName) {

        ServiceHits service = new ServiceHits(context,
                strServiceName, strbdmstatusstring, strCIFBDMUserId,
                strCIFBDMEmailId, strCIFBDMMObileNo,
                strCIFBDMPassword, this);
        service.execute();
    }

    private void startDownloadBdmList() {
        taskBdm.execute("demo");
    }

    @Override
    public void downLoadData() {
        taskBdm = new DownloadFileAsyncBdm();
        startDownloadBdmList();

    }

    class DownloadFileAsyncBdm extends AsyncTask<String, String, String> {

        private volatile boolean running = true;
        private String strBdmListErrorCOde1 = "";


        private ArrayList<String> lst_cif_branch_name;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
        }

        @Override
        protected String doInBackground(String... aurl) {
            try {

                String soap_action;

                running = true;
                SoapObject request;
                String NAMESPACE = "http://tempuri.org/";
                if (strUserType.equalsIgnoreCase("UM")) {

                    soap_action = "http://tempuri.org/getUMAgentList";
                    request = new SoapObject(NAMESPACE,
                            METHOD_NAME_AGENT_UM_LIST);
                    request.addProperty("strUMNo", strCIFBDMUserId);
                    request.addProperty("strUMStatus", strbdmstatusstring);
                    request.addProperty("strEmailId", strCIFBDMEmailId);
                    request.addProperty("strMobileNo", strCIFBDMMObileNo);
                    request.addProperty("strAuthKey", strCIFBDMPassword.trim());
                } else {

                    soap_action = "http://tempuri.org/getBDMCIFList";
                    request = new SoapObject(NAMESPACE,
                            METHOD_NAME_BDM_CIF_LIST);
                    request.addProperty("strBdmNo", strCIFBDMUserId);
                    request.addProperty("strBdmStatus", strbdmstatusstring);
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
                    androidHttpTranport
                            .call(soap_action, envelope);
                    Object response = envelope.getResponse();

                    if (!response.toString().contentEquals("anyType{}")) {

                        SoapPrimitive sa;
                        try {
                            sa = (SoapPrimitive) envelope.getResponse();

                            String inputpolicylist = sa.toString();

                            ParseXML prsObj = new ParseXML();

                            inputpolicylist = prsObj.parseXmlTag(
                                    inputpolicylist, "CIFPolicyList");
                            inputpolicylist = new ParseXML().parseXmlTag(
                                    inputpolicylist, "ScreenData");
                            strBdmListErrorCOde1 = inputpolicylist;

                            if (strBdmListErrorCOde1 == null) {
                                inputpolicylist = sa.toString();
                                inputpolicylist = prsObj.parseXmlTag(
                                        inputpolicylist, "CIFPolicyList");

                                List<String> Node = prsObj.parseParentNode(
                                        inputpolicylist, "Table");

                                List<XMLHolderBdm> nodeData = prsObj
                                        .parseNodeElementBdm(Node);

                                // final List<XMLHolderBdm> lst;
                                lstBDMList = new ArrayList<>();
                                lstBDMList.clear();

                                lst_cif_branch_name = new ArrayList<>();
                                lst_cif_branch_name.clear();
                                int i = 0;

                                for (XMLHolderBdm node : nodeData) {

                                    lstBDMList.add(node);

                                    if (node.getBranchName() != null
                                            && node.getBranchName() != "") {
                                        if (i == 0) {
                                            lst_cif_branch_name
                                                    .add("Select Branch");
                                            i = 1;
                                        }

                                        if (!lst_cif_branch_name.contains(node
                                                .getBranchName())) {
                                            lst_cif_branch_name.add(node
                                                    .getBranchName());
                                        }
                                    }
                                }

                                lstBdmListCount = lstBDMList.size();
                                array_sort = new ArrayList<>(lstBDMList);
                                selectedAdapterBdm = new SelectedAdapterBdm(
                                        context, lstBDMList);
                                selectedAdapterBdm.setNotifyOnChange(true);

                                registerForContextMenu(BDMlistView1);

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

                if (lst_cif_branch_name != null && lst_cif_branch_name.size() == 0)
                    tblum_bdm_bnk_br.setVisibility(View.GONE);
                else
                    tblum_bdm_bnk_br.setVisibility(View.VISIBLE);

                txterrordescbdm.setVisibility(View.VISIBLE);
                txtbdmlistcount.setVisibility(View.VISIBLE);
                BDMlistView1.setVisibility(View.VISIBLE);
                if (strBdmListErrorCOde1 == null) {
                    lnsearchbdmlist.setVisibility(View.VISIBLE);

                    txterrordescbdm.setText("");

                    if (strUserType.equalsIgnoreCase("UM")) {
                        txtbdmlistcount.setText("Total Agents : " + lstBdmListCount);
                        txtbdmumcode.setText("Agent Code");
                    } else {
                        txtbdmlistcount.setText("Total CIF : " + lstBdmListCount);
                        txtbdmumcode.setText("CIF Code");
                    }

                    BDMlistView1.setAdapter(selectedAdapterBdm);

                    Utility.setListViewHeightBasedOnChildren(BDMlistView1);

                    ArrayAdapter<String> productnameAdapter = new ArrayAdapter<>(
                            context, R.layout.spinner_item,
                            lst_cif_branch_name);
                    productnameAdapter
                            .setDropDownViewResource(R.layout.spinner_dropdown);
                    spinSearchBDMUMBN.setAdapter(productnameAdapter);
                    productnameAdapter.notifyDataSetChanged();


                } else {
                    lnsearchbdmlist.setVisibility(View.GONE);
                    BDMlistView1.setVisibility(View.GONE);

                    txterrordescbdm.setText("No Record Found");
                    txtbdmlistcount.setText("");
                    txtbdmumcode.setText("");
                    List<XMLHolderBdm> lst;
                    XMLHolderBdm node = null;
                    lst = new ArrayList<>();
                    lst.clear();
                    lst.add(null);
                    selectedAdapterBdm = new SelectedAdapterBdm(context, lst);
                    selectedAdapterBdm.setNotifyOnChange(true);
                    BDMlistView1.setAdapter(selectedAdapterBdm);

                    Utility.setListViewHeightBasedOnChildren(BDMlistView1);
                }
            } else {
                //servererror();
                mCommonMethods.showMessageDialog(context, mCommonMethods.SERVER_ERROR);
            }
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            if (taskBdm != null) {
                taskBdm.cancel(true);
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
                                if (taskBdm != null) {
                                    taskBdm.cancel(true);
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

		/*case DATE_DIALOG_ID:
            return new DatePickerDialog(this, R.style.datepickerstyle,
					mDateSetListener, mYear, mMonth, mDay);*/

            default:
                return null;
        }
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();
        switch (id) {
            case R.id.btn_save_bdn:
                getCIFList();
                break;
            case R.id.btn_reset_bdn:
                btn_reset_bdn();
                break;
            case R.id.btn_click_bdmum_bn:
                searchByBranchName();
                break;
            case R.id.btn_click_bdmum_cod:
                searchByCIFCode();
                break;
            case R.id.btn_click_bdmum_fn:
                searchByFirstName();
                break;
            case R.id.BdnlistView11:

                break;
        }

    }

    private void getCIFList() {

        taskBdm = new DownloadFileAsyncBdm();
        array_sort = new ArrayList<>();
        array_sort.clear();

        // strBdmcifno1 = edBdmCifNo.getText().toString();

        if (strbdmstatusstring.contentEquals("Select Status")) {
            //validationAlert();
            mCommonMethods.showMessageDialog(context, "All Fields Required..");
        } else {


            if (mCommonMethods.isNetworkConnected(context)) {
                if (strUserType.contentEquals("MAN")
                        || strUserType.contentEquals("BDM")
                        || strUserType.contentEquals("TBDM")
                        || strUserType.contentEquals("CIF")) {
                    service_hits(METHOD_NAME_BDM_CIF_LIST);
                } else if (strUserType.contentEquals("UM")) {
                    service_hits(METHOD_NAME_AGENT_UM_LIST);
                }
            } else {
                //intereneterror();
                mCommonMethods.showMessageDialog(context, mCommonMethods.NO_INTERNET_MESSAGE);
            }

        }
    }

    private void btn_reset_bdn() {
        array_sort = new ArrayList<>();
        array_sort.clear();
        lnsearchbdmlist.setVisibility(View.GONE);

        // edBdmCifNo.setText("");
        spinBDMStringStatus.setSelection(0);

        txtbdmlistcount.setVisibility(View.GONE);
        txterrordescbdm.setVisibility(View.GONE);
        BDMlistView1.setVisibility(View.GONE);
    }

    private void searchByBranchName() {

        if (spinSearchBDMUMBN.getSelectedItem().toString()
                .contentEquals("Select Branch")) {
            //branchnamealert();
            mCommonMethods.showMessageDialog(context, "Please Select Branch Name...");
        } else {
            mCommonMethods.hideKeyboard(spinSearchBDMUMBN, context);

            array_sort = new ArrayList<>();

            array_sort.clear();
            String str = spinSearchBDMUMBN.getSelectedItem()
                    .toString().trim().toLowerCase();
            for (XMLHolderBdm node : lstBDMList) {

                String branchName = node.getBranchName().trim().toLowerCase();
                if (branchName.contains(str) || branchName.startsWith(str)) {
                    array_sort.add(node);
                }
            }

            selectedAdapterBdm = new SelectedAdapterBdm(context,
                    array_sort);
            selectedAdapterBdm.setNotifyOnChange(true);
            BDMlistView1.setAdapter(selectedAdapterBdm);
            Utility.setListViewHeightBasedOnChildren(BDMlistView1);
        }
    }

    private void searchByCIFCode() {

        if (edSearchBDMUMCode.getText().toString()
                .equalsIgnoreCase("")) {
            //codealert();
            mCommonMethods.showMessageDialog(context, "Please Enter Code...");
        } else {
            mCommonMethods.hideKeyboard(edSearchBDMUMCode, context);

            array_sort = new ArrayList<>();

            array_sort.clear();
            String str = edSearchBDMUMCode.getText().toString().trim().toLowerCase();

            for (XMLHolderBdm node : lstBDMList) {
                    /*if (str.contains(node.getChannelCode())) {
                        array_sort.add(node);
					}*/

                String channelCode = node.getChannelCode().trim().toLowerCase();
                if (channelCode.contains(str) || channelCode.startsWith(str)) {
                    array_sort.add(node);
                }
            }

            selectedAdapterBdm = new SelectedAdapterBdm(context,
                    array_sort);
            selectedAdapterBdm.setNotifyOnChange(true);
            BDMlistView1.setAdapter(selectedAdapterBdm);
            Utility.setListViewHeightBasedOnChildren(BDMlistView1);
        }
    }

    private void searchByFirstName() {

        if (edSearchBDMUMFN.getText().toString()
                .equalsIgnoreCase("")) {
            //fnalert();
            mCommonMethods.showMessageDialog(context, mCommonMethods.FIRST_NAME_ALERT);
        } else {
            mCommonMethods.hideKeyboard(edSearchBDMUMFN, context);

            array_sort = new ArrayList<>();

            array_sort.clear();
            String str = edSearchBDMUMFN.getText().toString().trim().toLowerCase();

            for (XMLHolderBdm node : lstBDMList) {
                    /*if (str.contains(node.getFname())) {
                        array_sort.add(node);
					}*/

                String fName = node.getFname().trim().toLowerCase();
                if (fName.contains(str) || fName.startsWith(str)) {
                    array_sort.add(node);
                }
            }

            selectedAdapterBdm = new SelectedAdapterBdm(context,
                    array_sort);
            selectedAdapterBdm.setNotifyOnChange(true);
            BDMlistView1.setAdapter(selectedAdapterBdm);
            Utility.setListViewHeightBasedOnChildren(BDMlistView1);
        }
    }

    class SelectedAdapterBdm extends ArrayAdapter<XMLHolderBdm> {

        // used to keep selected position in ListView
        final List<XMLHolderBdm> lst;

        SelectedAdapterBdm(Context context,
                           List<XMLHolderBdm> objects) {
            super(context, 0, objects);
            lst = objects;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            if (v == null) {
                LayoutInflater vi = (LayoutInflater) this.getContext()
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.list_item_bdmlist, null);
            }


            TextView branchname = v.findViewById(R.id.txtbranchname);
            TextView channelcode = v.findViewById(R.id.txtchannelcode);
            TextView channelsigle = v.findViewById(R.id.txtchannelsigle);

            TextView textViewUserCodeTitle = v.findViewById(R.id.textViewUserCodeTitle);
            TextView textViewUserNameTitle = v.findViewById(R.id.textViewUserNameTitle);
            LinearLayout linearlayoutBrnachName = v.findViewById(R.id.linearlayoutBrnachName);
            final TextView textviewMobileNumber = v.findViewById(R.id.textviewMobileNumber);
            ImageView imgcontact_cust_r = v.findViewById(R.id.imgcontact_cust_r);
            LinearLayout llMobileLayout = v.findViewById(R.id.llMobileLayout);
            Object obj = null;
            boolean i = lst.contains(null);

            if (!i) {

                if (strUserType.equalsIgnoreCase("UM")) {
                    textViewUserCodeTitle.setText("Agent Code: ");
                    textViewUserNameTitle.setText("Agent Name: ");
                } else if (strUserType.equalsIgnoreCase("BDM")) {
                    textViewUserCodeTitle.setText("CIF Code: ");
                    textViewUserNameTitle.setText("CIF Name: ");
                }

                String branchName = lst.get(position).getBranchName() == null ? ""
                        : lst.get(position).getBranchName();
                if (!TextUtils.isEmpty(branchName)) {
                    linearlayoutBrnachName.setVisibility(View.VISIBLE);
                    branchname
                            .setText(branchName);
                } else {
                    linearlayoutBrnachName.setVisibility(View.GONE);
                }

                channelcode
                        .setText(lst.get(position).getChannelCode() == null ? ""
                                : lst.get(position).getChannelCode());

                String sal = lst.get(position).getChannelSigle() == null ? ""
                        : lst.get(position).getChannelSigle();
                String fnm = lst.get(position).getFname() == null ? "" : lst
                        .get(position).getFname();
                String lnm = lst.get(position).getLname() == null ? "" : lst
                        .get(position).getLname();
                String name = sal + " " + fnm + " " + lnm;

                WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
                Display display = wm.getDefaultDisplay();
                int screenWidth = display.getWidth(); // Get full screen width

                int eightyPercent = (screenWidth * 50) / 100;
                float textWidthPPF = channelsigle.getPaint().measureText(name);
                float numberOflinesForPPF = (textWidthPPF / eightyPercent) + 0.7f;

                channelsigle.setLines(Math.round(numberOflinesForPPF));
                channelsigle.setText(name);

                String mobileNumber = lst.get(position).getMOBILE();
                if (TextUtils.isEmpty(mobileNumber)) {
                    llMobileLayout.setVisibility(View.GONE);
                } else {
                    llMobileLayout.setVisibility(View.VISIBLE);
                }
                textviewMobileNumber.setText(lst.get(position).getMOBILE());
                imgcontact_cust_r.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String mobileNumber = textviewMobileNumber.getText().toString();

                        if (!TextUtils.isEmpty(mobileNumber)) {
                            mCommonMethods.callMobileNumber(mobileNumber, context);
                        }
                    }
                });
                textviewMobileNumber.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String mobileNumber = textviewMobileNumber.getText().toString();
                        if (!TextUtils.isEmpty(mobileNumber)) {
                            mCommonMethods.callMobileNumber(mobileNumber, context);
                        }
                    }
                });
            }

            return (v);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        //MenuInflater inflater = getMenuInflater();
        menu.setHeaderTitle("Services");
        int id = v.getId();
        if (id == R.id.BdnlistView11) {
            menu.add(0, v.getId(), 0, "Policy List");
            menu.add(0, v.getId(), 1, "Policy Maturity List");
            menu.add(0, v.getId(), 2, "Policy Surrender List");
            menu.add(0, v.getId(), 3, "Policy Revival List");
            menu.add(0, v.getId(), 4, "Policy Renewal List");
            menu.add(0, v.getId(), 5, "Help Desk");
            menu.add(0, v.getId(), 6, "Mandate Registration Status");
            menu.add(0, v.getId(), 7, "PIWC Status");
            menu.add(0, v.getId(), 8, "Proposal Status Tracker");
            menu.add(0, v.getId(), 9, "Advisor Proposals Status");
            menu.add(0, v.getId(), 10, "SB Due List");
            menu.add(0, v.getId(), 11, "Download/share PPC");
            menu.add(0, v.getId(), 12, "Pending Requirements - Non-Medical");
            menu.add(0, v.getId(), 13, "Proposal List");
            menu.add(0, v.getId(), 14, "Persistency");
            menu.add(0, v.getId(), 15, "DashBoard");
            menu.add(0, v.getId(), 16, "Pending Requirements - Medical");
            menu.add(0, v.getId(), 17, "Revival Campaign");
            menu.add(0, v.getId(), 18, "Premium Acknowledgement Receipt");
            menu.add(0, v.getId(), 19, "Rinn Raksha Proposal Tracker");
            menu.add(0, v.getId(), 20, "Auto Mandate Status List");

            menu.add(0, v.getId(), 21, "Alternate Mode Collection Status");
            menu.add(0, v.getId(), 22, "View Medical Status");
            menu.add(0, v.getId(), 23, "Policy Dispatch Status");
            menu.add(0, v.getId(), 24, "PIWC(Audio Calling) List");
            menu.add(0, v.getId(), 25, "Insta Image Verification Status");
            menu.add(0, v.getId(), 26, "NPS");
            menu.add(0, v.getId(), 27, "Send SMS Alternate Process");
            menu.add(0, v.getId(), 32, "Send eMandate Link");
            menu.add(0, v.getId(), 33, "Send MHR Link (Agent Own Life)");
            menu.add(0, v.getId(), 34, "Send Link");
            menu.add(0, v.getId(), 35, "View Medical TeleMER Status");

            menu.add(0, v.getId(), 36, "Unclaimed Data");
            menu.add(0, v.getId(), 37, "Unrealized Data");
            menu.add(0, v.getId(), 39, "NRI Payment Link");

            if (strUserType.equalsIgnoreCase("UM")) {
                menu.add(0, v.getId(), 28, "Commission");
                menu.add(0, v.getId(), 30, "COT and TOT Tentative Standings Report");
                menu.add(0, v.getId(), 31, "JOTC Report");
                menu.add(0, v.getId(), 40, "DOGH/Covid");
                menu.add(0, v.getId(), 41, "Covid Self Declaration");
                menu.add(0, v.getId(), 38, "Monthly Graduation Allowance Report");
                menu.add(0, v.getId(), 29, "LM Club Membership Report");

            }

        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
                .getMenuInfo();

        if (array_sort != null) {
            String strCifNo = array_sort.get(info.position).getChannelCode();

            if (!TextUtils.isEmpty(strCifNo)) {

                Intent intent = null;
                String title = item.getTitle().toString();
                if (title.equalsIgnoreCase("DashBoard")) {

                    if (strUserType.equalsIgnoreCase("UM")) {
                        intent = new Intent(getApplicationContext(), NewDashboardAgent.class);
                    } else {
                        intent = new Intent(getApplicationContext(), NewDashboardCIF.class);
                    }

                } else if (title.equalsIgnoreCase("Policy Renewal List")) {

                    intent = new Intent(context, BancaReportsRenewalActivity.class);

                } else if (title.equalsIgnoreCase("Policy Revival List")) {
                    intent = new Intent(context, BancaReportsRevivalActivity.class);
                } else if (title.equalsIgnoreCase("Policy Surrender List")) {
                    if (strUserType.equalsIgnoreCase("AGENT") || strUserType.contentEquals("UM")) {
                        intent = new Intent(context, AgencyReportsSurrenderActivity.class);
                    } else {
                        intent = new Intent(context, BancaReportsSurrenderActivity.class);
                    }
                } else if (title.equalsIgnoreCase("Policy List")) {
                    if (strUserType.equalsIgnoreCase("AGENT") || strUserType.contentEquals("UM")) {
                        intent = new Intent(context, AgencyReportsPolicyListActivity.class);
                    } else {
                        intent = new Intent(context, BancaReportsPolicyListActivity.class);
                    }
                } else if (title.equalsIgnoreCase("Policy Maturity List")) {
                    if (strUserType.equalsIgnoreCase("AGENT") || strUserType.contentEquals("UM")) {
                        intent = new Intent(context, AgencyReportsMaturityActivity.class);
                    } else {
                        intent = new Intent(context, BancaReportsMaturityActivity.class);
                    }
                } else if (item.getTitle().equals("Persistency")) {
                    intent = new Intent(context, AgencyReportsPersistency.class);
                    if (strUserType.equalsIgnoreCase("UM")) {
                        intent.putExtra("strUserType", "Agent");
                    } else {
                        intent.putExtra("strUserType", "CIF");
                    }
                } else if (item.getTitle().equals("Commission")) {
                    intent = new Intent(context, AgencyReportsCommissionActivity.class);
                } else if (title.equalsIgnoreCase("Help Desk")) {
                    if (strUserType.equalsIgnoreCase("UM")) {
                        intent = new Intent(context, AgencyReportsPolicyDetailsActivity.class);
                    } else {
                        intent = new Intent(context, BancaReportsPolicyDetailsActivity.class);
                    }
                } else if (title.equalsIgnoreCase("Mandate Registration Status")) {
                    if (strUserType.equalsIgnoreCase("UM")) {
                        intent = new Intent(context, AgencyReportsMandateRegistrationStatusActivity.class);
                    } else {
                        intent = new Intent(context, BancaReportsMandateRegistrationStatusActivity.class);
                    }
                } else if (title.equalsIgnoreCase("PIWC Status")) {
                    intent = new Intent(context, BancaReportsPIWCActivity.class);
                } else if (title.equalsIgnoreCase("Proposal Status Tracker")) {

                    if (strUserType.equalsIgnoreCase("UM")) {
                        intent = new Intent(context, AgencyReportsProposalTracker.class);
                    } else {
                        intent = new Intent(context, BancaReportsProposalTracker.class);
                    }

                } else if (title.equalsIgnoreCase("Advisor Proposals Status")) {

                    if (strUserType.equalsIgnoreCase("UM")) {
                        intent = new Intent(context, AgencyReportsAdvisorProposalsStatusActivity.class);
                    } else {
                        intent = new Intent(context, BancaReportsAdvisorProposalsStatusActivity.class);
                    }

                } else if (title.equalsIgnoreCase("SB Due List")) {

                    if (strUserType.equalsIgnoreCase("UM")) {
                        intent = new Intent(context, AgencyReportsSBDueListActivity.class);
                    } else {
                        intent = new Intent(context, BancaReportsSBDueListActivity.class);
                    }

                } else if (title.equalsIgnoreCase("Download/share PPC")) {
                    intent = new Intent(context, DownloadPPCActivity.class);
                } else if (title.equalsIgnoreCase("Requirement Upload")) {
                    intent = new Intent(context, DocumentsUploadActivity.class);
                    if (strUserType.equalsIgnoreCase("UM")) {
                        intent.putExtra("strUserType", "Agent");
                    } else {
                        intent.putExtra("strUserType", "CIF");

                    }

                } else if (title.equalsIgnoreCase("Proposal List")) {
                    intent = new Intent(context, CommonReportsProposalListActivity.class);
                    if (strUserType.equalsIgnoreCase("UM")) {
                        intent.putExtra("strUserType", "Agent");
                    } else {
                        intent.putExtra("strUserType", "CIF");
                    }
                } else if (title.equals("Pending Requirements - Non-Medical")) {
                    intent = new Intent(context, DocUploadNonMedicalPendingActivity.class);
                    if (strUserType.equalsIgnoreCase("UM")) {
                        intent.putExtra("strUserType", "Agent");
                    } else {
                        intent.putExtra("strUserType", "CIF");
                    }
                } else if (title.equalsIgnoreCase("Pending Requirements - Medical")) {
                    intent = new Intent(context, MedicalPendingRequirementActivity.class);
                    if (strUserType.equalsIgnoreCase("UM")) {
                        intent.putExtra("strUserType", "Agent");
                    } else {
                        intent.putExtra("strUserType", "CIF");
                    }
                } else if (title.equalsIgnoreCase("Revival Campaign")) {
                    intent = new Intent(context, RevivalCampaignReportList.class);
                } else if (title.equalsIgnoreCase("Rinn Raksha Proposal Tracker")) {
                    intent = new Intent(context, ProposalRinnStatusActivity.class);
                } else if (title.equalsIgnoreCase("Premium Acknowledgement Receipt")) {
                    intent = new Intent(context, PremiumPaymentReceiptActivity.class);
                } else if (title.equalsIgnoreCase("Auto Mandate Status List")) {
                    intent = new Intent(context, AutoMandateRegistrationStatusListReportsActivity.class);
                } else if (title.equalsIgnoreCase("Alternate Mode Collection Status")) {
                    intent = new Intent(context, AlternetModeCollectionStatusActivity.class);
                } else if (title.equalsIgnoreCase("View Medical Status")) {
                    intent = new Intent(context, ViewMedicalStatusActivity.class);
                } else if (title.equalsIgnoreCase("Policy Dispatch Status")) {
                    intent = new Intent(context, PolicyDispatchStatusActivity.class);
                } else if (title.equalsIgnoreCase("LM Club Membership Report")) {
                    intent = new Intent(context, LMClubMembershipReportActivity.class);
                } else if (title.equalsIgnoreCase("COT and TOT Tentative Standings Report")) {
                    intent = new Intent(context, COTTOTTentativeReportActivity.class);
                } else if (title.equalsIgnoreCase("JOTC Report")) {
                    intent = new Intent(context, JOTCActivity.class);
                } else if (title.equalsIgnoreCase("PIWC(Audio Calling) List")) {
                    intent = new Intent(context, PIWCAudioCallingPendingListActivity.class);
                } else if (title.equalsIgnoreCase("Insta Image Verification Status")) {
                    intent = new Intent(context, InstaImageFailureCasesActivity.class);
                } else if (title.equalsIgnoreCase("NPS")) {
                    intent = new Intent(context, NPSScoreActivity.class);
                } else if (title.equalsIgnoreCase("Send SMS Alternate Process")) {
                    intent = new Intent(context, SendSMSAlternateProcessActivity.class);
                } else if (title.equalsIgnoreCase("Send eMandate Link")) {
                    intent = new Intent(context, SendMandateLinkActivity.class);
                } else if (title.equalsIgnoreCase("Send MHR Link (Agent Own Life)")) {
                    intent = new Intent(context, SendMHRLinkAOLActivity.class);
                } else if (title.equalsIgnoreCase("Send Link")) {
                    intent = new Intent(context, SendLinkActivity.class);
                } else if (title.equalsIgnoreCase("View Medical TeleMER Status")) {
                    intent = new Intent(context, ViewMedicalStatusTeleMERActivity.class);
                } else if (title.equalsIgnoreCase("Unclaimed Data")) {
                    intent = new Intent(context, UnclaimedDataActivity.class);
                } else if (title.equalsIgnoreCase("Unrealized Data")) {
                    intent = new Intent(context, UnrealizedDataActivity.class);
                }else if (title.equalsIgnoreCase("Monthly Graduation Allowance Report")) {
                    intent = new Intent(context, MonthlyGraduationAllowanceReportActivity.class);
                }else if (title.equalsIgnoreCase("NRI Payment Link")) {
                    intent = new Intent(context, NRIPaymentLinkActivity.class);
                }else if (title.equalsIgnoreCase("DOGH/Covid")) {
                    intent = new Intent(context, CovidDoghNextActivity.class);
                }else if (title.equalsIgnoreCase("Covid Self Declaration")) {
                    intent = new Intent(context, CovidSelfDeclarationActivity.class);
                }

                if (intent != null) {
                    intent.putExtra("fromHome", "N");
                    intent.putExtra("strBDMCifCOde", strCifNo);
                    intent.putExtra("strMenuItem", item.getTitle());
                    intent.putExtra("strEmailId", strCIFBDMEmailId);
                    intent.putExtra("strPassword", strCIFBDMPassword.trim());
                    intent.putExtra("strMobileNo", strCIFBDMMObileNo);
                    startActivity(intent);
                }

            }
        }

        return true;

    }


}
