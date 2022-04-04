package sbilife.com.pointofsale_bancaagency.reports.commonreports;

import android.annotation.SuppressLint;
import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import sbilife.com.pointofsale_bancaagency.ParseXML;
import sbilife.com.pointofsale_bancaagency.R;
import sbilife.com.pointofsale_bancaagency.ServiceURL;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;
import sbilife.com.pointofsale_bancaagency.reports.ServiceHits;

public class CommonReportsPersistencyDueDataActivity extends AppCompatActivity implements
        ServiceHits.DownLoadData, UpdateAltMobileNoCommonAsyncTask.UpdateAltMobNoInterface {
    private final String METHOD_NAME_PERSISTENCY_DUE_DATA = "getDetail_Notcollected_pers";
    private String NAMESPACE = "http://tempuri.org/";
    private String URl = ServiceURL.SERVICE_URL;


    //private SelectedAdapterPersistencyDueData selectedAdapterPersistencyDueData;
    private Context context;
    private CommonMethods commonMethods;

    private DownloadPersistencyDueDataAsync downloadPersistencyDueDataAsync;
    private ServiceHits service;
    private ProgressDialog mProgressDialog;

    //private ListView listviewPersistencyDueData;
    private String strCIFBDMUserId = "", strCIFBDMEmailId = "",
            strCIFBDMPassword = "", strCIFBDMMObileNo = "";
    private int finalPosition = 0;
    private ArrayList<PersistencyDueDataValuesModel> globleList;
    private RecyclerView recyclerView;
    private SelectedAdapter selectedAdapter;
    private FundValueAsyncTask fundValueAsyncTask;
    private SendRenewalSMSAsynTask sendRenewalSMSAsynTask;
    private GetPremiumAmountCommonAsync getPremiumAmountCommonAsync;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_common_reports_persistency_due_data);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);
        context = this;
        commonMethods = new CommonMethods();
        commonMethods.setApplicationToolbarMenu(this, "Persistency Due Data");

        //listviewPersistencyDueData = findViewById(R.id.listviewPersistencyDueData);
        recyclerView = findViewById(R.id.recyclerView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        // set LayoutManager to RecyclerView
        recyclerView.setLayoutManager(linearLayoutManager);
        // call the constructor of CustomAdapter to send the reference and data to Adapter
        globleList = new ArrayList<>();
        selectedAdapter = new SelectedAdapter(globleList);
        recyclerView.setAdapter(selectedAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        Intent intent = getIntent();

        getUserDetails();
        String bdmCifCOde = intent.getStringExtra("strBDMCifCOde");
        if (bdmCifCOde != null) {
            strCIFBDMUserId = bdmCifCOde;
        }
        if (commonMethods.isNetworkConnected(context)) {
            service_hits();
        } else {
            commonMethods.showMessageDialog(context, commonMethods.NO_INTERNET_MESSAGE);
        }
    }

    private void getUserDetails() {
        CommonMethods.UserDetailsValuesModel userDetailsValuesModel = commonMethods.setUserDetails(context);
        strCIFBDMUserId = userDetailsValuesModel.getStrCIFBDMUserId();
        strCIFBDMEmailId = userDetailsValuesModel.getStrCIFBDMEmailId();
        strCIFBDMPassword = userDetailsValuesModel.getStrCIFBDMPassword();
        strCIFBDMMObileNo = userDetailsValuesModel.getStrCIFBDMMObileNo();
    }

    @Override
    public void downLoadData() {
        downloadPersistencyDueDataAsync = new DownloadPersistencyDueDataAsync();
        downloadPersistencyDueDataAsync.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    class DownloadPersistencyDueDataAsync extends AsyncTask<String, String, String> {

        private volatile boolean running = true;
        private String strPersistencyDueDataError = "";
        private int lstPersistencyDueDataCount = 0;
        private TextView txtpolicylistcount, txterrordesc;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            txtpolicylistcount = findViewById(R.id.txtpolicylistcount);
            txterrordesc = findViewById(R.id.txterrordesc);
            mProgressDialog = new ProgressDialog(context, ProgressDialog.THEME_HOLO_LIGHT);
            String Message = "Loading Please wait...";
            mProgressDialog.setMessage(Html.fromHtml("<font color='#00a1e3'><b>" + Message + "<b></font>"));
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setCancelable(true);

            mProgressDialog.setButton("Cancel",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            if (downloadPersistencyDueDataAsync != null) {
                                downloadPersistencyDueDataAsync.cancel(true);
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
        }

        @SuppressLint("SimpleDateFormat")
        @Override
        protected String doInBackground(String... aurl) {
            try {

                running = true;
                SoapObject request = null;
                //String UserType = commonMethods.GetUserType(context);

                request = new SoapObject(NAMESPACE,
                        METHOD_NAME_PERSISTENCY_DUE_DATA);
                //getDetail_Notcollected_pers(string strAgenyCode, string strEmailId,
                // string strMobileNo, string strAuthKey)
                request.addProperty("strAgenyCode", strCIFBDMUserId);
                request.addProperty("strEmailId", strCIFBDMEmailId);
                request.addProperty("strMobileNo", strCIFBDMMObileNo);
                request.addProperty("strAuthKey", strCIFBDMPassword);


                System.out.println("request:" + request.toString());
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);
                envelope.dotNet = true;

                envelope.setOutputSoapObject(request);

                commonMethods.TLSv12Enable();

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);


                HttpTransportSE androidHttpTranport = new HttpTransportSE(URl,300000);
                try {
                    String SOAP_ACTION_PERSISTENCY_DUE_DATA = "http://tempuri.org/getDetail_Notcollected_pers";
                    androidHttpTranport.call(
                            SOAP_ACTION_PERSISTENCY_DUE_DATA, envelope);
                    Object response = envelope.getResponse();

                    if (!response.toString().contentEquals("<NewDataSet />")) {
                        System.out.println("response:" + response.toString());
                        SoapPrimitive sa = null;
                            sa = (SoapPrimitive) envelope.getResponse();

                            String inputpolicylist = sa.toString();

                            ParseXML prsObj = new ParseXML();

                            inputpolicylist = prsObj.parseXmlTag(
                                    inputpolicylist, "NewDataSet");
                            inputpolicylist = new ParseXML().parseXmlTag(
                                    inputpolicylist, "ScreenData");
                            strPersistencyDueDataError = inputpolicylist;

                            if (strPersistencyDueDataError == null) {
                                // for agent policy list

                                inputpolicylist = sa.toString();
                                inputpolicylist = prsObj.parseXmlTag(
                                        inputpolicylist, "NewDataSet");

                                List<String> Node = prsObj.parseParentNode(
                                        inputpolicylist, "Table");

                            List<PersistencyDueDataValuesModel> nodeData = parseNodePersistencyDueData(Node);

                                // final List<XMLHolder> lstPolicyList;
                                 globleList = new ArrayList<>();
                                globleList.clear();

                                globleList.addAll(nodeData);

                                lstPersistencyDueDataCount = globleList.size();
                            /*selectedAdapterPersistencyDueData = new SelectedAdapterPersistencyDueData(
                                        context, globleList);
                                selectedAdapterPersistencyDueData
                                        .setNotifyOnChange(true);

                            registerForContextMenu(listviewPersistencyDueData);*/

                            }
                    }

                } catch (Exception e) {
                    mProgressDialog.dismiss();
                    running = false;
                    e.printStackTrace();
                }
            } catch (Exception e) {
                mProgressDialog.dismiss();
                running = false;
            }
            return null;

        }


        @Override
        protected void onPostExecute(String unused) {
            if (mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
            if (running) {

                if (strPersistencyDueDataError == null) {
                    txterrordesc.setText("");
                    txtpolicylistcount.setText("Total Record : "
                            + lstPersistencyDueDataCount);
                    /*listviewPersistencyDueData.setAdapter(selectedAdapterPersistencyDueData);
                    Utility.setListViewHeightBasedOnChildren(listviewPersistencyDueData);*/

                    selectedAdapter = new SelectedAdapter(globleList);
                    recyclerView.setAdapter(selectedAdapter);
                    recyclerView.invalidate();

                } else {

                    txterrordesc.setText("No Record Found");
                    txtpolicylistcount.setText("Total Record : " + 0);
                    clearList();
                    commonMethods.showMessageDialog(context, "No record found.");
                   /* List<PersistencyDueDataValuesModel> lst;
                    PersistencyDueDataValuesModel node = null;
                    lst = new ArrayList<>();
                    lst.clear();
                    lst.add(null);
                    selectedAdapterPersistencyDueData = new SelectedAdapterPersistencyDueData(context,
                            lst);
                    selectedAdapterPersistencyDueData.setNotifyOnChange(true);
                    listviewPersistencyDueData.setAdapter(selectedAdapterPersistencyDueData);
                    Utility.setListViewHeightBasedOnChildren(listviewPersistencyDueData);*/
                }
            } else {
                commonMethods.showMessageDialog(context,
                        "Server Not Responding,Try again..");
                clearList();
                commonMethods.showMessageDialog(context, "No record found.");
            }
        }

        private void clearList() {
            if (globleList != null && selectedAdapter != null) {
                globleList.clear();
                selectedAdapter = new SelectedAdapter(globleList);
                recyclerView.setAdapter(selectedAdapter);
                recyclerView.invalidate();
            }
        }
    }

    public class SelectedAdapter extends RecyclerView.Adapter<SelectedAdapter.
            ViewHolderAdapter> {

        private ArrayList<PersistencyDueDataValuesModel> lst;

        SelectedAdapter(ArrayList<PersistencyDueDataValuesModel> lstAdapterList) {
            this.lst = lstAdapterList;
        }

        @Override
        public int getItemCount() {
            return lst.size();
        }

        @Override
        public SelectedAdapter.ViewHolderAdapter onCreateViewHolder(ViewGroup parent, int viewType) {
            View mView = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.list_item_persistency_due_data, parent, false);

            return new SelectedAdapter.ViewHolderAdapter(mView);
        }

        @Override
        public void onBindViewHolder(final SelectedAdapter.ViewHolderAdapter holder, final int position) {
            holder.textviewPersistencyDueDataCustomerName.setText(lst.get(position).getCUSTOMERNAME() == null ? "" : lst.get(position).getCUSTOMERNAME());
            holder.textviewPersistencyDueDataPolicyNumber
                    .setText(lst.get(position).getPOLICYNUMBER() == null ? "" : lst.get(position).getPOLICYNUMBER());
            holder.textviewPersistencyDueDataHolderId
                    .setText(lst.get(position).getHOLDERID() == null ? ""
                            : lst.get(position).getHOLDERID());

            String policyCurrentStatus = lst.get(position).getPOLICYCURRENTSTATUS() == null ? "" : lst.get(
                    position).getPOLICYCURRENTSTATUS();
            holder.textviewPersistencyDueDataPolicyCurrentStatus.setText(policyCurrentStatus);
            holder.textviewPersistencyDueDataPremiumGrossAmount.setText(lst.get(position).getPREMIUMGROSSAMOUNT() == null ? "" : lst.get(position).getPREMIUMGROSSAMOUNT());

            if (policyCurrentStatus.equalsIgnoreCase("Lapse")) {
                holder.imageviewSMS.setVisibility(View.INVISIBLE);
                //textviewPersistencyDueDataPremiumGrossAmount.setText("");
            } else {
                holder.imageviewSMS.setVisibility(View.VISIBLE);
            }


            holder.textviewPersistencyDueDataDueDate.setText(lst.get(position).getDUE_DATE() == null ? "" : lst.get(
                    position).getDUE_DATE());

            holder.textviewPersistencyDueDataCollectableAmount.setText(lst.get(position).getCOLLECTABLE_AMOUNT() == null ? "" : lst.get(
                    position).getCOLLECTABLE_AMOUNT());
            holder.textviewPersistencyDueDataCollectedAmount.setText(lst.get(position).getCOLLECTED_AMOUNT() == null ? "" : lst.get(
                    position).getCOLLECTED_AMOUNT());
            holder.textviewPersistencyDueDataMobileNumber.setText(lst.get(position).getCUSTOMERMOBILE() == null ? "" : lst.get(position).getCUSTOMERMOBILE());

            String premiumPayFreq = "Premium Payment Frequency: ";
            holder.textviewPersistencyDueDataPremiumPaymentFrequencyTitle.setText(premiumPayFreq);

            String FUPDateString = lst.get(position).getPREMIUMFUP() == null ? "" : lst.get(position).getPREMIUMFUP();
            String premiumPaymentFrequency = lst.get(position).getPREMIUMPAYMENTFREQUENCY() == null ? "" : lst.get(position).getPREMIUMPAYMENTFREQUENCY();
            holder.textviewPersistencyDueDataPremiumPaymentFrequency.setText(premiumPaymentFrequency);
            holder.textviewPersistencyDueDataPremiumUp.setText(FUPDateString);


            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
                Date FUPDate = dateFormat.parse(FUPDateString);
                Date today = new Date();

                double diff = Math.abs(today.getTime() - FUPDate.getTime());
                String unpaidDues;
                if (premiumPaymentFrequency.equalsIgnoreCase("Yearly")) {
                    double numOfYear = Math.ceil((diff / (1000 * 60 * 60 * 24)) / 365);
                    unpaidDues = (int) numOfYear + "";
                } else if (premiumPaymentFrequency.contains("Quarterly")) {
                    double quarterly = Math.ceil((diff / (1000 * 60 * 60 * 24)) / 90);
                    unpaidDues = (int) quarterly + "";
                } else if (premiumPaymentFrequency.contains("Monthly")) {
                    double monthly = Math.ceil((diff / (1000 * 60 * 60 * 24)) / 30);
                    unpaidDues = (int) monthly + "";
                } else {
                    double halfYear = Math.ceil((diff / (1000 * 60 * 60 * 24)) / 180);
                    unpaidDues = (int) halfYear + "";
                }

                holder.textviewPersistencyDueDataNumberOfUnpaidDues.setText(unpaidDues);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (TextUtils.isEmpty(lst.get(position).getCONTACTOFFICE())) {
                holder.llOfficeMaster.setVisibility(View.GONE);
            } else {
                holder.llOfficeMaster.setVisibility(View.VISIBLE);
                holder.textviewContactOffice.setText(lst.get(position).getCONTACTOFFICE());
                holder.textviewContactOffice.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!TextUtils.isEmpty(holder.textviewContactOffice.getText().toString())) {
                            commonMethods.callMobileNumber(holder.textviewContactOffice.getText().toString(), context);
                        }
                    }
                });
                holder.LLofficeContact.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!TextUtils.isEmpty(holder.textviewContactOffice.getText().toString())) {
                            commonMethods.callMobileNumber(holder.textviewContactOffice.getText().toString(), context);
                        }
                    }
                });
            }


            if (TextUtils.isEmpty(lst.get(position).getCONTACTRESIDENCE())) {
                holder.llResidenceMaster.setVisibility(View.GONE);
            } else {
                holder.llResidenceMaster.setVisibility(View.VISIBLE);
                holder.textviewResidenceOffice.setText(lst.get(position).getCONTACTRESIDENCE());
                holder.textviewResidenceOffice.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!TextUtils.isEmpty(holder.textviewResidenceOffice.getText().toString())) {
                            commonMethods.callMobileNumber(holder.textviewResidenceOffice.getText().toString(), context);
                        }
                    }
                });
                holder.LLResidenceContact.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!TextUtils.isEmpty(holder.textviewResidenceOffice.getText().toString())) {
                            commonMethods.callMobileNumber(holder.textviewResidenceOffice.getText().toString(), context);
                        }
                    }
                });

            }

            if (lst.get(position).getPOLICYTYPE().equalsIgnoreCase("ULIP")) {
            holder.llFundValue.setVisibility(View.VISIBLE);
            holder.buttonFundValue.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //commonMethods.hideKeyboard(edittextSearch, context);
                    int index = holder.getAdapterPosition();
                    String holderId = lst.get(index).getHOLDERID();
                    String policyNumber = lst.get(index).getPOLICYNUMBER();
                    fundValueAsyncTask = new FundValueAsyncTask(context, holderId, policyNumber,
                            CommonReportsPersistencyDueDataActivity.this::getFundValueInterfaceMethod);
                    fundValueAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                }
            });

            }else{
                holder.llFundValue.setVisibility(View.GONE);
            }
            holder.tvDOC.setText(lst.get(position).getPOLICYRISKCOMMENCEMENTDATE());
            holder.tvPaymentMechanism.setText(lst.get(position).getPOLICYPAYMENTMECHANISM());

            holder.buttonCRM.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {

                    int index = holder.getAdapterPosition();
                    String premiumFUP = lst.get(index).getPREMIUMFUP();
                    String selectedPolicyNumber = lst.get(index).getPOLICYNUMBER();
                    commonMethods.showDispositionAlert(context, premiumFUP, selectedPolicyNumber,
                            strCIFBDMEmailId, strCIFBDMMObileNo, strCIFBDMUserId);
                }
            });

            holder.buttonUpdatAltMobile.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    int index = holder.getAdapterPosition();
                    //showDispositionAlert(lstAdapterList.get(index));
                    final String premiumFUP = lst.get(index).getPREMIUMFUP();
                    final String policyNumber = lst.get(index).getPOLICYNUMBER();
                    commonMethods.updateAltMobileAlert(context, premiumFUP, policyNumber,
                            CommonReportsPersistencyDueDataActivity.this);

                }
            });
            holder.tvActualPremium.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int index = holder.getAdapterPosition();
                    String selectedPolicyNumber = lst.get(index).getPOLICYNUMBER();
                    if (lst.get(position).getPOLICYCURRENTSTATUS().equalsIgnoreCase("Lapse") ||
                            lst.get(position).getPOLICYCURRENTSTATUS().equalsIgnoreCase("Technical Lapse")) {
                        getPremiumAmountCommonAsync = new GetPremiumAmountCommonAsync(selectedPolicyNumber, context,
                                CommonReportsPersistencyDueDataActivity.this::getPremiumInterfaceMethod);
                        getPremiumAmountCommonAsync.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    } else {
                        String msg = "Gross Premium Amount is - " + lst.get(index).getPREMIUMGROSSAMOUNT();
                        commonMethods.showMessageDialog(context, msg);
                    }
                }
            });


            holder.imgcontact_cust_r.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    if (!TextUtils.isEmpty(holder.textviewPersistencyDueDataMobileNumber.getText().toString())) {
                        commonMethods.callMobileNumber(holder.textviewPersistencyDueDataMobileNumber.getText().toString(), context);
                    }
                }
            });

            holder.textviewPersistencyDueDataMobileNumber.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!TextUtils.isEmpty(holder.textviewPersistencyDueDataMobileNumber.getText().toString())) {
                        commonMethods.callMobileNumber(holder.textviewPersistencyDueDataMobileNumber.getText().toString(), context);
                    }
                }
            });

            holder.imageviewSMS.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //commonMethods.hideKeyboard(, context);
                    AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.MyAlertDialogStyle);
                    builder.setTitle("Choose Communication Medium");
                    finalPosition = 0;
                    //final String[] languagesArray = {"English", "Hindi", "Telugu"};
//                    final String[] languagesArray = {"English"};
                    //final String[] commMediumArray = {"SMS", "Email"};
                    final String[] commMediumArray = {"SMS"};
                    // cow
                    builder.setSingleChoiceItems(commMediumArray, finalPosition, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finalPosition = which;
                        }
                    });

                    builder.setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            String commMedium = commMediumArray[finalPosition];
                            if (commonMethods.isNetworkConnected(context)) {
                                int index = holder.getAdapterPosition();
                                if (commMedium.equalsIgnoreCase("SMS")) {

                                    final String mobileNumber = holder.textviewPersistencyDueDataMobileNumber.getText().toString();
                                    final String policyNumber = holder.textviewPersistencyDueDataPolicyNumber.getText().toString();

                                    final String dueDate = holder.textviewPersistencyDueDataDueDate.getText().toString();
                                    final String status = holder.textviewPersistencyDueDataPolicyCurrentStatus.getText().toString();
                                    final String amount = holder.textviewPersistencyDueDataPremiumGrossAmount.getText().toString();


                                    sendRenewalSMSAsynTask = new SendRenewalSMSAsynTask(policyNumber, mobileNumber, dueDate,
                                            status, amount, "English",
                                            context, CommonReportsPersistencyDueDataActivity.this::getSMSDetailsInterfaceMethod);
                                    sendRenewalSMSAsynTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                                } else if (commMedium.equalsIgnoreCase("Email")) {
                                    final String dueDate = holder.textviewPersistencyDueDataDueDate.getText().toString();
                                    final String paymentMechanism = "";//lst.get(index).getPOLICYPAYMENTMECHANISM();
                                    final String policyNumber = holder.textviewPersistencyDueDataPolicyNumber.getText().toString();
                                    final String emailid = "";//lst.get(index).getEMAILID();
                                    final String amount = holder.textviewPersistencyDueDataPremiumGrossAmount.getText().toString();
                                    final String name = lst.get(index).getCUSTOMERNAME();

                                    String mode = "";

                                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MMMM-yyyy");
                                    Date renewalDate = null;
                                    try {
                                        renewalDate = sdf.parse(dueDate);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    if (new Date().before(renewalDate)
                                            && !(paymentMechanism.equalsIgnoreCase("ATM"))
                                            && !(paymentMechanism.equalsIgnoreCase("Direct Bill"))) {
                                        mode = "Pre Alter";
                                    } else if (new Date().before(renewalDate)
                                            && paymentMechanism.equalsIgnoreCase("ATM")
                                            && paymentMechanism.equalsIgnoreCase("Direct Bill")) {
                                        mode = "Pre Non Alter";
                                    } else if (new Date().after(renewalDate)) {
                                        mode = "Post Non Alter";
                                    }
                                    System.out.println("mode = " + mode);
                                    System.out.println("mode = " + paymentMechanism);
                                    SendRenewalDueEmailAsyncTask sendRenewalDueEmailAsyncTask =
                                            new SendRenewalDueEmailAsyncTask(policyNumber, emailid, dueDate, amount, mode,
                                                    name);
                                    sendRenewalDueEmailAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                                }


                            } else {
                                commonMethods.showMessageDialog(context, commonMethods.NO_INTERNET_MESSAGE);
                            }
                        }
                    });
                    builder.setNegativeButton("Cancel", null);

                    AlertDialog dialog = builder.create();
                    dialog.setCancelable(false);
                    dialog.show();
                }
            });

            /*holder.imageviewSMS.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final String mobileNumber = holder.textviewPersistencyDueDataMobileNumber.getText().toString();
                    final String policyNumber = holder.textviewPersistencyDueDataPolicyNumber.getText().toString();

                    final String dueDate = holder.textviewPersistencyDueDataDueDate.getText().toString();
                    final String status = holder.textviewPersistencyDueDataPolicyCurrentStatus.getText().toString();
                    final String amount = holder.textviewPersistencyDueDataPremiumGrossAmount.getText().toString();

                    AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.MyAlertDialogStyle);
                    builder.setTitle("Choose Language");
                    finalPosition = 0;
                    final String[] languagesArray = {"English", "Hindi", "Telugu"};
                    // cow
                    builder.setSingleChoiceItems(languagesArray, finalPosition, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finalPosition = which;
                        }
                    });

                    builder.setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            System.out.println("which = " + which);
                            System.out.println("checkedItem = " + finalPosition);

                            String language = languagesArray[finalPosition];

                            if (commonMethods.isNetworkConnected(context)) {
                                SendSmsAsync sendSmsAsync = new SendSmsAsync(policyNumber, mobileNumber, dueDate,
                                        status, amount, language);
                                sendSmsAsync.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                            } else {
                                commonMethods.showMessageDialog(context, commonMethods.NO_INTERNET_MESSAGE);
                            }
                        }
                    });
                    builder.setNegativeButton("Cancel", null);

                    AlertDialog dialog = builder.create();
                    dialog.setCancelable(false);
                    dialog.show();


                }
            });*/

        }

        public class ViewHolderAdapter extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener,
                MenuItem.OnMenuItemClickListener {

            private final TextView textviewPersistencyDueDataPolicyNumber, textviewPersistencyDueDataHolderId,
                    textviewPersistencyDueDataPolicyCurrentStatus, textviewPersistencyDueDataDueDate,
                    textviewPersistencyDueDataCollectableAmount,
                    textviewPersistencyDueDataCollectedAmount, textviewPersistencyDueDataMobileNumber,
                    textviewPersistencyDueDataCustomerName,
                    textviewPersistencyDueDataPremiumPaymentFrequency,
                    textviewPersistencyDueDataPremiumPaymentFrequencyTitle,
                    textviewPersistencyDueDataPremiumUp,
                    textviewPersistencyDueDataPremiumGrossAmount,
                    textviewPersistencyDueDataNumberOfUnpaidDues;
            private final ImageView imgcontact_cust_r, imageviewSMS;


            private final TextView tvDOC, textviewContactOffice, textviewResidenceOffice, buttonFundValue,
                    tvActualPremium, tvPaymentMechanism;
            private final LinearLayout LLofficeContact, LLResidenceContact, llResidenceMaster, llOfficeMaster,
                    llFundValue, llDOC, llCRMPaymentDetails;
            private final Button buttonUpdatAltMobile, buttonCRM;

            private ViewHolderAdapter(View v) {
                super(v);

                textviewPersistencyDueDataPolicyNumber = v.findViewById(R.id.textviewPersistencyDueDataPolicyNumber);
                textviewPersistencyDueDataHolderId = v.findViewById(R.id.textviewPersistencyDueDataHolderId);
                textviewPersistencyDueDataPolicyCurrentStatus = v.findViewById(R.id.textviewPersistencyDueDataPolicyCurrentStatus);
                textviewPersistencyDueDataDueDate = v.findViewById(R.id.textviewPersistencyDueDataDueDate);
                textviewPersistencyDueDataCollectableAmount = v.findViewById(R.id.textviewPersistencyDueDataCollectableAmount);
                textviewPersistencyDueDataCollectedAmount = v.findViewById(R.id.textviewPersistencyDueDataCollectedAmount);
                textviewPersistencyDueDataMobileNumber = v.findViewById(R.id.textviewPersistencyDueDataMobileNumber);
                textviewPersistencyDueDataCustomerName = v.findViewById(R.id.textviewPersistencyDueDataCustomerName);
                textviewPersistencyDueDataPremiumPaymentFrequency = v.findViewById(R.id.textviewPersistencyDueDataPremiumPaymentFrequency);
                textviewPersistencyDueDataPremiumPaymentFrequencyTitle = v.findViewById(R.id.textviewPersistencyDueDataPremiumPaymentFrequencyTitle);
                textviewPersistencyDueDataPremiumUp = v.findViewById(R.id.textviewPersistencyDueDataPremiumUp);
                textviewPersistencyDueDataPremiumGrossAmount = v.findViewById(R.id.textviewPersistencyDueDataPremiumGrossAmount);
                textviewPersistencyDueDataNumberOfUnpaidDues = v.findViewById(R.id.textviewPersistencyDueDataNumberOfUnpaidDues);
                imgcontact_cust_r = v.findViewById(R.id.imgcontact_cust_r);
                imageviewSMS = v.findViewById(R.id.imageviewSMS);


                tvDOC = v.findViewById(R.id.tvDOC);
                textviewContactOffice = v.findViewById(R.id.textviewContactOffice);
                LLofficeContact = v.findViewById(R.id.LLofficeContact);
                llOfficeMaster = v.findViewById(R.id.llOfficeMaster);

                textviewResidenceOffice = v.findViewById(R.id.textviewResidenceOffice);
                LLResidenceContact = v.findViewById(R.id.LLResidenceContact);
                llResidenceMaster = v.findViewById(R.id.llResidenceMaster);

                llFundValue = v.findViewById(R.id.llFundValue);
                llFundValue.setVisibility(View.GONE);
                buttonFundValue = v.findViewById(R.id.buttonFundValue);
                tvActualPremium = v.findViewById(R.id.tvActualPremium);
                tvPaymentMechanism = v.findViewById(R.id.tvPaymentMechanism);
                buttonUpdatAltMobile = v.findViewById(R.id.buttonUpdatAltMobile);
                buttonCRM = v.findViewById(R.id.buttonCRM);

                llDOC = v.findViewById(R.id.llDOC);
                llDOC.setVisibility(View.VISIBLE);
                llCRMPaymentDetails = v.findViewById(R.id.llCRMPaymentDetails);
                llCRMPaymentDetails.setVisibility(View.VISIBLE);
                v.setOnCreateContextMenuListener(this);
            }


            @Override
            public void onCreateContextMenu(ContextMenu menu, View v,
                                            ContextMenu.ContextMenuInfo menuInfo) {

                menu.setHeaderTitle("Select Action");
                String status = globleList.get(getAdapterPosition()).getPOLICYCURRENTSTATUS();

                if (status.equalsIgnoreCase("Lapse") || status.equalsIgnoreCase("Technical Lapse")) {
                    MenuItem revivalQuotation = menu.add(Menu.NONE, 1, 1, "Revival Quotation");
                    revivalQuotation.setOnMenuItemClickListener(this);
                }
            }

            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()) {

                    case 1:
                        String strPolicyNo = globleList.get(getAdapterPosition()).getPOLICYNUMBER();
                        String mobileNumber = globleList.get(getAdapterPosition()).getCUSTOMERMOBILE();
                        String emailId = "";

                        if (!TextUtils.isEmpty(strPolicyNo)) {
                            Intent intent = new Intent(context, RevivalQuotationActivity.class);
                            intent.putExtra("policyNumber", strPolicyNo);
                            intent.putExtra("mobileNumber", mobileNumber);
                            intent.putExtra("emailId", emailId);
                            startActivity(intent);
                        }
                        break;
                }
                return true;
            }


        }

    }

    /*class SelectedAdapterPersistencyDueData extends ArrayAdapter<PersistencyDueDataValuesModel> {

        // used to keep selected position in ListView

        final List<PersistencyDueDataValuesModel> lst;

        SelectedAdapterPersistencyDueData(Context context,
                                          List<PersistencyDueDataValuesModel> objects) {
            super(context, 0, objects);

            lst = objects;
        }


        @SuppressLint("InflateParams")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            if (v == null) {
                LayoutInflater vi = (LayoutInflater) this.getContext()
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.list_item_persistency_due_data, null);
            }

            // get text view
            final TextView textviewPersistencyDueDataPolicyNumber = v.findViewById(R.id.textviewPersistencyDueDataPolicyNumber);
            TextView textviewPersistencyDueDataHolderId = v.findViewById(R.id.textviewPersistencyDueDataHolderId);
            final TextView textviewPersistencyDueDataPolicyCurrentStatus = v.findViewById(R.id.textviewPersistencyDueDataPolicyCurrentStatus);
            final TextView textviewPersistencyDueDataDueDate = v.findViewById(R.id.textviewPersistencyDueDataDueDate);
            TextView textviewPersistencyDueDataCollectableAmount = v.findViewById(R.id.textviewPersistencyDueDataCollectableAmount);
            TextView textviewPersistencyDueDataCollectedAmount = v.findViewById(R.id.textviewPersistencyDueDataCollectedAmount);
            final TextView textviewPersistencyDueDataMobileNumber = v.findViewById(R.id.textviewPersistencyDueDataMobileNumber);
            TextView textviewPersistencyDueDataCustomerName = v.findViewById(R.id.textviewPersistencyDueDataCustomerName);
            TextView textviewPersistencyDueDataPremiumPaymentFrequency = v.findViewById(R.id.textviewPersistencyDueDataPremiumPaymentFrequency);
            TextView textviewPersistencyDueDataPremiumPaymentFrequencyTitle = v.findViewById(R.id.textviewPersistencyDueDataPremiumPaymentFrequencyTitle);
            TextView textviewPersistencyDueDataPremiumUp = v.findViewById(R.id.textviewPersistencyDueDataPremiumUp);
            final TextView textviewPersistencyDueDataPremiumGrossAmount = v.findViewById(R.id.textviewPersistencyDueDataPremiumGrossAmount);
            TextView textviewPersistencyDueDataNumberOfUnpaidDues = v.findViewById(R.id.textviewPersistencyDueDataNumberOfUnpaidDues);
            ImageView imgcontact_cust_r = v.findViewById(R.id.imgcontact_cust_r);
            ImageView imageviewSMS = v.findViewById(R.id.imageviewSMS);


            TextView tvDOC = v.findViewById(R.id.tvDOC);
            TextView textviewContactOffice = v.findViewById(R.id.textviewContactOffice);
            LinearLayout LLofficeContact = v.findViewById(R.id.LLofficeContact);
            TextView textviewResidenceOffice = v.findViewById(R.id.textviewResidenceOffice);
            LinearLayout LLResidenceContact = v.findViewById(R.id.LLResidenceContact);

            Object obj = null;
            boolean i = lst.contains(null);

            if (!i) {
                textviewPersistencyDueDataCustomerName.setText(lst.get(position).getCUSTOMERNAME() == null ? "" : lst.get(position).getCUSTOMERNAME());
                textviewPersistencyDueDataPolicyNumber
                        .setText(lst.get(position).getPOLICYNUMBER() == null ? "" : lst.get(position).getPOLICYNUMBER());
                textviewPersistencyDueDataHolderId
                        .setText(lst.get(position).getHOLDERID() == null ? ""
                                : lst.get(position).getHOLDERID());

                String policyCurrentStatus = lst.get(position).getPOLICYCURRENTSTATUS() == null ? "" : lst.get(
                        position).getPOLICYCURRENTSTATUS();
                textviewPersistencyDueDataPolicyCurrentStatus.setText(policyCurrentStatus);
                textviewPersistencyDueDataPremiumGrossAmount.setText(lst.get(position).getPREMIUMGROSSAMOUNT() == null ? "" : lst.get(position).getPREMIUMGROSSAMOUNT());

                if(policyCurrentStatus.equalsIgnoreCase("Lapse")){
                    imageviewSMS.setVisibility(View.INVISIBLE);
                    //textviewPersistencyDueDataPremiumGrossAmount.setText("");
                }else{
                    imageviewSMS.setVisibility(View.VISIBLE);

                }



                textviewPersistencyDueDataDueDate.setText(lst.get(position).getDUE_DATE() == null ? "" : lst.get(
                        position).getDUE_DATE());

                textviewPersistencyDueDataCollectableAmount.setText(lst.get(position).getCOLLECTABLE_AMOUNT() == null ? "" : lst.get(
                        position).getCOLLECTABLE_AMOUNT());
                textviewPersistencyDueDataCollectedAmount.setText(lst.get(position).getCOLLECTED_AMOUNT() == null ? "" : lst.get(
                        position).getCOLLECTED_AMOUNT());
                textviewPersistencyDueDataMobileNumber.setText(lst.get(position).getCUSTOMERMOBILE() == null ? "" : lst.get(position).getCUSTOMERMOBILE());

                WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
                Display display = wm.getDefaultDisplay();
                int screenWidth = display.getWidth(); // Get full screen width

                int eightyPercent = (screenWidth * 50) / 100;
                String premiumPayFreq = "Premium Payment Frequency: ";
                float textWidthPPF = textviewPersistencyDueDataPremiumPaymentFrequencyTitle.getPaint().measureText(premiumPayFreq);
                float numberOflinesForPPF = (textWidthPPF / eightyPercent) + 0.7f;

                textviewPersistencyDueDataPremiumPaymentFrequencyTitle.setLines(Math.round(numberOflinesForPPF));
                textviewPersistencyDueDataPremiumPaymentFrequencyTitle.setText(premiumPayFreq);

                String FUPDateString = lst.get(position).getPREMIUMFUP() == null ? "" : lst.get(position).getPREMIUMFUP();
                String premiumPaymentFrequency = lst.get(position).getPREMIUMPAYMENTFREQUENCY() == null ? "" : lst.get(position).getPREMIUMPAYMENTFREQUENCY();
                textviewPersistencyDueDataPremiumPaymentFrequency.setText(premiumPaymentFrequency);
                textviewPersistencyDueDataPremiumUp.setText(FUPDateString);


                textviewContactOffice.setText(lst.get(position).getCONTACTOFFICE());
                textviewResidenceOffice.setText(lst.get(position).getCONTACTRESIDENCE());
                tvDOC.setText(lst.get(position).getPOLICYRISKCOMMENCEMENTDATE());


                try {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
                    Date FUPDate = dateFormat.parse(FUPDateString);
                    Date today = new Date();

                    double diff = Math.abs(today.getTime() - FUPDate.getTime());
                    String unpaidDues;
                    if (premiumPaymentFrequency.equalsIgnoreCase("Yearly")) {
                        double numOfYear = Math.ceil((diff / (1000 * 60 * 60 * 24)) / 365);
                        unpaidDues = (int) numOfYear + "";
                    } else if (premiumPaymentFrequency.contains("Quarterly")) {
                        double quarterly = Math.ceil((diff / (1000 * 60 * 60 * 24)) / 90);
                        unpaidDues = (int) quarterly + "";
                    } else if (premiumPaymentFrequency.contains("Monthly")) {
                        double monthly = Math.ceil((diff / (1000 * 60 * 60 * 24)) / 30);
                        unpaidDues = (int) monthly + "";
                    } else {
                        double halfYear = Math.ceil((diff / (1000 * 60 * 60 * 24)) / 180);
                        unpaidDues = (int) halfYear + "";
                    }

                    textviewPersistencyDueDataNumberOfUnpaidDues.setText(unpaidDues);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                imgcontact_cust_r.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        if (!TextUtils.isEmpty(textviewPersistencyDueDataMobileNumber.getText().toString())) {
                            commonMethods.callMobileNumber(textviewPersistencyDueDataMobileNumber.getText().toString(), context);
                        }
                    }
                });

                textviewPersistencyDueDataMobileNumber.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!TextUtils.isEmpty(textviewPersistencyDueDataMobileNumber.getText().toString())) {
                            commonMethods.callMobileNumber(textviewPersistencyDueDataMobileNumber.getText().toString(), context);
                        }
                    }
                });

                textviewContactOffice.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!TextUtils.isEmpty(textviewContactOffice.getText().toString())) {
                            commonMethods.callMobileNumber(textviewContactOffice.getText().toString(), context);
                        }
                    }
                });
                LLofficeContact.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!TextUtils.isEmpty(textviewContactOffice.getText().toString())) {
                            commonMethods.callMobileNumber(textviewContactOffice.getText().toString(), context);
                        }
                    }
                });
                textviewResidenceOffice.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!TextUtils.isEmpty(textviewResidenceOffice.getText().toString())) {
                            commonMethods.callMobileNumber(textviewResidenceOffice.getText().toString(), context);
                        }
                    }
                });
                LLResidenceContact.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!TextUtils.isEmpty(textviewResidenceOffice.getText().toString())) {
                            commonMethods.callMobileNumber(textviewResidenceOffice.getText().toString(), context);
                        }
                    }
                });

                imageviewSMS.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        final String mobileNumber = textviewPersistencyDueDataMobileNumber.getText().toString();
                        final String policyNumber = textviewPersistencyDueDataPolicyNumber.getText().toString();

                        final String dueDate = textviewPersistencyDueDataDueDate.getText().toString();
                        final String status = textviewPersistencyDueDataPolicyCurrentStatus.getText().toString();
                        final String amount = textviewPersistencyDueDataPremiumGrossAmount.getText().toString();

                        AlertDialog.Builder builder = new AlertDialog.Builder(context,R.style.MyAlertDialogStyle);
                        builder.setTitle("Choose Language");
                        finalPosition = 0;
                        final String[] languagesArray = {"English", "Hindi", "Telugu"};
                        // cow
                        builder.setSingleChoiceItems(languagesArray, finalPosition, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finalPosition = which;
                            }
                        });

                        builder.setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                System.out.println("which = " + which);
                                System.out.println("checkedItem = " + finalPosition);

                                String language = languagesArray[finalPosition];

                                if(commonMethods.isNetworkConnected(context)) {
                                    SendSmsAsync sendSmsAsync = new SendSmsAsync(policyNumber, mobileNumber, dueDate,
                                            status, amount, language);
                                    sendSmsAsync.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                                }else{
                                    commonMethods.showMessageDialog(context,commonMethods.NO_INTERNET_MESSAGE);
                                }
                            }
                        });
                        builder.setNegativeButton("Cancel", null);

                        AlertDialog dialog = builder.create();
                        dialog.setCancelable(false);
                        dialog.show();


                    }
                });


            }

            return (v);
        }


    }*/

    /*@Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        // MenuInflater inflater = getMenuInflater();

        menu.setHeaderTitle("Services");

        int id = v.getId();
        if (id == R.id.listviewPersistencyDueData) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
            String policyCurrentStatus = globleList.get(info.position).getPOLICYCURRENTSTATUS();

            if(policyCurrentStatus.equalsIgnoreCase("Lapse")
                    ||policyCurrentStatus.equalsIgnoreCase("Technical Lapse")){
                menu.add(0, v.getId(), 0, "Revival Quotation");
            }

        }

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
                .getMenuInfo();

        if (item.getTitle() == "Revival Quotation") {
            String strPolicyNo = globleList.get(info.position).getPOLICYNUMBER();
            String mobileNumber = globleList.get(info.position).getCUSTOMERMOBILE();
            String emailId = "";

            if (!TextUtils.isEmpty(strPolicyNo)) {
                Intent intent = new Intent(context, RevivalQuotationActivity.class);
                intent.putExtra("policyNumber", strPolicyNo);
                intent.putExtra("mobileNumber", mobileNumber);
                intent.putExtra("emailId", emailId);
                startActivity(intent);
            }
        }
        return true;
    }*/


    private void service_hits() {
        String strCIFBDMUserId = "", strCIFBDMEmailId = "",
                strCIFBDMPassword = "", strCIFBDMMObileNo = "";
        CommonMethods.UserDetailsValuesModel userDetailsValuesModel = commonMethods
                .setUserDetails(context);
        strCIFBDMUserId = userDetailsValuesModel.getStrCIFBDMUserId();
        strCIFBDMEmailId = userDetailsValuesModel.getStrCIFBDMEmailId();
        strCIFBDMPassword = userDetailsValuesModel.getStrCIFBDMPassword();
        strCIFBDMMObileNo = userDetailsValuesModel.getStrCIFBDMMObileNo();

        service = new ServiceHits(context,
                METHOD_NAME_PERSISTENCY_DUE_DATA, "",
                strCIFBDMUserId, strCIFBDMEmailId, strCIFBDMMObileNo,
                strCIFBDMPassword, this);
        service.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    protected void onDestroy() {
        killTasks();
        super.onDestroy();
    }


    private void killTasks() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }

        if (downloadPersistencyDueDataAsync != null) {
            downloadPersistencyDueDataAsync.cancel(true);
        }
        if (service != null) {
            service.cancel(true);
        }

        if (fundValueAsyncTask != null) {
            fundValueAsyncTask.cancel(true);
        }

        if (sendRenewalSMSAsynTask != null) {
            sendRenewalSMSAsynTask.cancel(true);
    }
        if (getPremiumAmountCommonAsync != null) {
            getPremiumAmountCommonAsync.cancel(true);
        }
    }

    public class PersistencyDueDataValuesModel {
		/*<POLICYNUMBER>53006984306</POLICYNUMBER>
		<HOLDERID>44604434</HOLDERID>
		<POLICYCURRENTSTATUS>Lapse</POLICYCURRENTSTATUS>
		<DUE_DATE>2017-10-04T00:00:00-07:00</DUE_DATE>
		<COLLECTABLE_AMOUNT>150000</COLLECTABLE_AMOUNT>
		<COLLECTED_AMOUNT>0</COLLECTED_AMOUNT>
		 <CUSTOMERMOBILE>9771202960</CUSTOMERMOBILE>
		 <CUSTOMERNAME>Fulkumari Dasin</CUSTOMERNAME>
		 <PREMIUMPAYMENTFREQUENCY>Yearly</PREMIUMPAYMENTFREQUENCY>
		<PREMIUMFUP>04-AUG-2017</PREMIUMFUP>
		<PREMIUMGROSSAMOUNT>30000</PREMIUMGROSSAMOUNT> </Table> */

        private String POLICYNUMBER = "";
        private String HOLDERID = "";
        private String POLICYCURRENTSTATUS = "";
        private String DUE_DATE = "";
        private String COLLECTABLE_AMOUNT = "";
        private String COLLECTED_AMOUNT = "";
        private String CUSTOMERMOBILE = "";
        private String CUSTOMERNAME = "";
        private String PREMIUMPAYMENTFREQUENCY = "";
        private String PREMIUMFUP = "";
        private String PREMIUMGROSSAMOUNT = "";
        private String POLICYRISKCOMMENCEMENTDATE, CONTACTRESIDENCE,
                CONTACTOFFICE, POLICYTYPE, POLICYPAYMENTMECHANISM;

        PersistencyDueDataValuesModel(String POLICYNUMBER, String HOLDERID, String POLICYCURRENTSTATUS,
                                      String DUE_DATE, String COLLECTABLE_AMOUNT, String COLLECTED_AMOUNT,
                                      String CUSTOMERMOBILE, String CUSTOMERNAME, String PREMIUMPAYMENTFREQUENCY,
                                      String PREMIUMFUP, String PREMIUMGROSSAMOUNT, String POLICYRISKCOMMENCEMENTDATE,
                                      String CONTACTRESIDENCE, String CONTACTOFFICE, String POLICYTYPE,
                                      String POLICYPAYMENTMECHANISM) {
            this.POLICYNUMBER = POLICYNUMBER;
            this.HOLDERID = HOLDERID;
            this.POLICYCURRENTSTATUS = POLICYCURRENTSTATUS;
            this.DUE_DATE = DUE_DATE;
            this.COLLECTABLE_AMOUNT = COLLECTABLE_AMOUNT;
            this.COLLECTED_AMOUNT = COLLECTED_AMOUNT;
            this.CUSTOMERMOBILE = CUSTOMERMOBILE;
            this.CUSTOMERNAME = CUSTOMERNAME;
            this.PREMIUMPAYMENTFREQUENCY = PREMIUMPAYMENTFREQUENCY;
            this.PREMIUMFUP = PREMIUMFUP;
            this.PREMIUMGROSSAMOUNT = PREMIUMGROSSAMOUNT;

            this.POLICYRISKCOMMENCEMENTDATE = POLICYRISKCOMMENCEMENTDATE;
            this.CONTACTRESIDENCE = CONTACTRESIDENCE;
            this.CONTACTOFFICE = CONTACTOFFICE;

            this.POLICYRISKCOMMENCEMENTDATE = POLICYRISKCOMMENCEMENTDATE;
            this.CONTACTRESIDENCE = CONTACTRESIDENCE;
            this.CONTACTOFFICE = CONTACTOFFICE;
            this.POLICYTYPE = POLICYTYPE;
            this.POLICYPAYMENTMECHANISM = POLICYPAYMENTMECHANISM;
        }

        public String getPOLICYNUMBER() {
            return POLICYNUMBER;
        }

        public String getHOLDERID() {
            return HOLDERID;
        }

        public String getPOLICYCURRENTSTATUS() {
            return POLICYCURRENTSTATUS;
        }

        public String getDUE_DATE() {
            return DUE_DATE;
        }

        public String getCOLLECTABLE_AMOUNT() {
            return COLLECTABLE_AMOUNT;
        }

        public String getCOLLECTED_AMOUNT() {
            return COLLECTED_AMOUNT;
        }

        public String getCUSTOMERMOBILE() {
            return CUSTOMERMOBILE;
        }

        public String getCUSTOMERNAME() {
            return CUSTOMERNAME;
        }

        public String getPREMIUMPAYMENTFREQUENCY() {
            return PREMIUMPAYMENTFREQUENCY;
        }

        public String getPREMIUMFUP() {
            return PREMIUMFUP;
        }

        public String getPREMIUMGROSSAMOUNT() {
            return PREMIUMGROSSAMOUNT;
        }

        public String getPOLICYRISKCOMMENCEMENTDATE() {
            return POLICYRISKCOMMENCEMENTDATE;
        }

        public String getCONTACTRESIDENCE() {
            return CONTACTRESIDENCE;
        }

        public String getCONTACTOFFICE() {
            return CONTACTOFFICE;
        }

        public String getPOLICYTYPE() {
            return POLICYTYPE;
        }

        public String getPOLICYPAYMENTMECHANISM() {
            return POLICYPAYMENTMECHANISM;
        }
    }

    public List<PersistencyDueDataValuesModel> parseNodePersistencyDueData(List<String> lsNode) {
        List<PersistencyDueDataValuesModel> lsData = new ArrayList<>();
        //POLICYCURRENTSTATUS,PREMIUMFUP,POLICYNUMBER
        String POLICYNUMBER = "", HOLDERID = "", POLICYCURRENTSTATUS = "", DUE_DATE = "",
                COLLECTABLE_AMOUNT = "", COLLECTED_AMOUNT = "", CUSTOMERMOBILE = "", CUSTOMERNAME = "",
                PREMIUMPAYMENTFREQUENCY = "", PREMIUMFUP = "", PREMIUMGROSSAMOUNT = "";
        ParseXML parseXML = new ParseXML();
        for (String Node : lsNode) {

            POLICYNUMBER = parseXML.parseXmlTag(Node, "POLICYNUMBER");
            POLICYNUMBER = POLICYNUMBER == null ? "" : POLICYNUMBER;

            HOLDERID = parseXML.parseXmlTag(Node, "HOLDERID");
            HOLDERID = HOLDERID == null ? "" : HOLDERID;

            POLICYCURRENTSTATUS = parseXML.parseXmlTag(Node, "POLICYCURRENTSTATUS");
            POLICYCURRENTSTATUS = POLICYCURRENTSTATUS == null ? "" : POLICYCURRENTSTATUS;

            DUE_DATE = parseXML.parseXmlTag(Node, "DUE_DATE");
            DUE_DATE = DUE_DATE == null ? "" : DUE_DATE;

            COLLECTABLE_AMOUNT = parseXML.parseXmlTag(Node, "COLLECTABLE_AMOUNT");
            COLLECTABLE_AMOUNT = COLLECTABLE_AMOUNT == null ? "" : COLLECTABLE_AMOUNT;

            COLLECTED_AMOUNT = parseXML.parseXmlTag(Node, "COLLECTED_AMOUNT");
            COLLECTED_AMOUNT = COLLECTED_AMOUNT == null ? "" : COLLECTED_AMOUNT;

            CUSTOMERMOBILE = parseXML.parseXmlTag(Node, "CUSTOMERMOBILE");
            CUSTOMERMOBILE = CUSTOMERMOBILE == null ? "" : CUSTOMERMOBILE;

            CUSTOMERNAME = parseXML.parseXmlTag(Node, "CUSTOMERNAME");
            CUSTOMERNAME = CUSTOMERNAME == null ? "" : CUSTOMERNAME;

            PREMIUMPAYMENTFREQUENCY = parseXML.parseXmlTag(Node, "PREMIUMPAYMENTFREQUENCY");
            PREMIUMPAYMENTFREQUENCY = PREMIUMPAYMENTFREQUENCY == null ? "" : PREMIUMPAYMENTFREQUENCY;

            PREMIUMFUP = parseXML.parseXmlTag(Node, "PREMIUMFUP");
            PREMIUMFUP = PREMIUMFUP == null ? "" : PREMIUMFUP;

            PREMIUMGROSSAMOUNT = parseXML.parseXmlTag(Node, "PREMIUMGROSSAMOUNT");
            PREMIUMGROSSAMOUNT = PREMIUMGROSSAMOUNT == null ? "" : PREMIUMGROSSAMOUNT;


            String POLICYRISKCOMMENCEMENTDATE = parseXML.parseXmlTag(Node, "POLICYRISKCOMMENCEMENTDATE");
            POLICYRISKCOMMENCEMENTDATE = POLICYRISKCOMMENCEMENTDATE == null ? "" : POLICYRISKCOMMENCEMENTDATE;

            try {
                POLICYRISKCOMMENCEMENTDATE = POLICYRISKCOMMENCEMENTDATE.split("T")[0];

                Date dt1 = null;
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat df1 = new SimpleDateFormat("dd-MMMM-yyyy");

                dt1 = df.parse(POLICYRISKCOMMENCEMENTDATE);

                POLICYRISKCOMMENCEMENTDATE = df1.format(dt1);
            } catch (Exception e) {
                e.printStackTrace();
            }

            String CONTACTRESIDENCE = parseXML.parseXmlTag(Node, "CONTACTRESIDENCE");
            CONTACTRESIDENCE = CONTACTRESIDENCE == null ? "" : CONTACTRESIDENCE;

            String CONTACTOFFICE = parseXML.parseXmlTag(Node, "CONTACTOFFICE");
            CONTACTOFFICE = CONTACTOFFICE == null ? "" : CONTACTOFFICE;


            String POLICYTYPE = parseXML.parseXmlTag(Node, "POLICYTYPE");
            POLICYTYPE = POLICYTYPE == null ? "" : POLICYTYPE;


            String POLICYPAYMENTMECHANISM = parseXML.parseXmlTag(Node, "POLICYPAYMENTMECHANISM");
            POLICYPAYMENTMECHANISM = POLICYPAYMENTMECHANISM == null ? "" : POLICYPAYMENTMECHANISM;

            PersistencyDueDataValuesModel nodeVal = new PersistencyDueDataValuesModel(POLICYNUMBER, HOLDERID, POLICYCURRENTSTATUS, DUE_DATE,
                    COLLECTABLE_AMOUNT, COLLECTED_AMOUNT, CUSTOMERMOBILE, CUSTOMERNAME, PREMIUMPAYMENTFREQUENCY,
                    PREMIUMFUP, PREMIUMGROSSAMOUNT, POLICYRISKCOMMENCEMENTDATE, CONTACTRESIDENCE, CONTACTOFFICE,
                    POLICYTYPE, POLICYPAYMENTMECHANISM);
            lsData.add(nodeVal);
        }
        return lsData;
    }


    class SendRenewalDueEmailAsyncTask extends AsyncTask<String, String, String> {

        private final String policyNumber;
        private final String emailId;
        private final String dueDate;
        private final String amount;
        private final String mode;
        private final String name;
        private volatile boolean running = true;
        private String response = "";

        SendRenewalDueEmailAsyncTask(String policyNumber, String emailId, String dueDate, String amount, String mode,
                                     String name) {
            this.policyNumber = policyNumber;
            this.emailId = emailId;
            this.dueDate = dueDate;
            this.amount = amount;
            this.mode = mode;
            this.name = name;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if(mProgressDialog!=null && !mProgressDialog.isShowing()) {
                mProgressDialog.show();
            }
        }

        @SuppressLint("SimpleDateFormat")
        @Override
        protected String doInBackground(String... aurl) {
            try {

                running = true;
                //SendRenewalDueEmail_SMRT(string strPolicyNo, string strEmail, string strDueDate, string strAmt,
                // string strMode, string Name, string strEmailId, string strMobileNo, string strAuthKey)
                String METHOD_NAME_SEND_EMAIL = "SendRenewalDueEmail_SMRT";
                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME_SEND_EMAIL);
                request.addProperty("strPolicyNo", policyNumber);
                request.addProperty("strEmail", emailId);
                request.addProperty("strDueDate", dueDate);
                request.addProperty("strAmt", amount);
                request.addProperty("strMode", mode);
                request.addProperty("Name", name);
                request.addProperty("strEmailId", strCIFBDMEmailId);
                request.addProperty("strMobileNo", strCIFBDMMObileNo);
                request.addProperty("strAuthKey", commonMethods.getStrAuth());

                Log.d("doInBackground", "doInBackground: " + request.toString());
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);
                envelope.dotNet = true;

                envelope.setOutputSoapObject(request);

                commonMethods.TLSv12Enable();

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);

                HttpTransportSE androidHttpTranport = new HttpTransportSE(URl);
                String SOAP_ACTION = "http://tempuri.org/" + METHOD_NAME_SEND_EMAIL;
                androidHttpTranport.call(SOAP_ACTION,envelope);

                SoapPrimitive sa = (SoapPrimitive) envelope.getResponse();
                response = sa.toString();
                if(response.equalsIgnoreCase("1")){
                    response = "success";
                }else{
                    response = "";
                }
            } catch (Exception e) {
                mProgressDialog.dismiss();
                running = false;
            }
            return null;

        }


        @Override
        protected void onPostExecute(String unused) {

            if (mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }

            if (running) {
                if (response.equalsIgnoreCase("success")) {
                    commonMethods.showMessageDialog(context, "Email sent successfully");
                } else {
                    commonMethods.showMessageDialog(context, "Email sending failed");
                }
            } else {
                commonMethods.showMessageDialog(context, "Email sending failed");
            }
        }
    }

    public void getFundValueInterfaceMethod(List<String> Node, String policyNumber) {

        if (TextUtils.isEmpty(policyNumber)) {
            commonMethods.showMessageDialog(context, commonMethods.NO_RECORD_FOUND);
        } else {
            String result = "", fundValueStr = "";
            ParseXML parseXML = new ParseXML();
            List<ParseXML.XMLFundSwitchHolder> nodeData = parseXML.parseNodeElementFundSwitch(Node);
            for (ParseXML.XMLFundSwitchHolder node : nodeData) {
                System.out.println("sa.toString() = " + policyNumber);
                if (policyNumber.equalsIgnoreCase(node.getPOLICYNO())) {
                    fundValueStr = node.getFUNDVALUE();
                    System.out.println("sa.toString() = " + fundValueStr);
                    result = "Success";
                }
            }

            if (result.equalsIgnoreCase("Success")) {
                commonMethods.showMessageDialog(context, "Fund Value Is : " + fundValueStr);
            } else {
                commonMethods.showMessageDialog(context, commonMethods.NO_RECORD_FOUND);
            }
        }
    }

    void getSMSDetailsInterfaceMethod(String result) {
        if (result.equalsIgnoreCase("1")) {
                    commonMethods.showMessageDialog(context,"Message sent successfully");
                } else {
                    commonMethods.showMessageDialog(context,"Message sending failed");
                }
    }

    void getPremiumInterfaceMethod(String premiumAmount, String result) {
        if (premiumAmount.equals("")) {
            commonMethods.showMessageDialog(context, result);
            } else {
            commonMethods.showMessageDialog(context, "Gross Premium Amount is - " + premiumAmount);
        }
    }

    public void getUpdateAltMobResultMethod(String result) {
        if (result != null && result.equalsIgnoreCase("Success")) {
            commonMethods.showMessageDialog(context, "Mobile Number Updated Successfully");
        } else {
            commonMethods.showMessageDialog(context, "Mobile Number Not Updated.Please Try Again.");
        }
    }
}
