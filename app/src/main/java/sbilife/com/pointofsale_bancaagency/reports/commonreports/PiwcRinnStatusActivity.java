package sbilife.com.pointofsale_bancaagency.reports.commonreports;

import android.annotation.SuppressLint;
import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
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
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;
import sbilife.com.pointofsale_bancaagency.reports.ServiceHits;

public class PiwcRinnStatusActivity extends AppCompatActivity implements View.OnClickListener,
        ServiceHits.DownLoadData {

    // private ListView listviewAutoMandateStatus;
    private EditText edittextFormNumner,edittextSearch;

    private ProgressDialog mProgressDialog;
    private  final String SOAP_ACTION_PIWC_RINN_STATUS = "http://tempuri.org/getPIWCdetailRinn_smrt";
    private  final String METHOD_NAME_PIWC_RINN_STATUS = "getPIWCdetailRinn_smrt";

    private  final String NAMESPACE = "http://tempuri.org/";
    private  final String URl = ServiceURL.SERVICE_URL;

    private Context context;
    private CommonMethods commonMethods;
    private String formNumber = "";

    private DownloadPIWCRinnStatusAsync downloadPIWCRinnStatusAsync;
    private ServiceHits service;

    private ArrayList<ParseXML.PIWCRinnStatusValuesModel> piwcRinnStatusGlobalList;
    private SelectedAdapterPiwcRinnStatusList selectedAdapterPiwcRinnStatusList;
    private TextView textviewRecordCount;
    private RecyclerView recyclerviewPiwcRinnStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_piwc_rinn_status);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);
        context =this;
        commonMethods = new CommonMethods();
        commonMethods.setApplicationToolbarMenu(this, "Rinn Raksha PIWC Status");

        Button buttonPiwcRinnStatus = findViewById(R.id.buttonPiwcRinnStatus);
        edittextFormNumner = findViewById(R.id.edittextFormNumner);

        edittextSearch = findViewById(R.id.edittextSearch);
        textviewRecordCount = findViewById(R.id.textviewRecordCount);
        recyclerviewPiwcRinnStatus = findViewById(R.id.recyclerviewPiwcRinnStatus);
        //lstReq.setHasFixedSize(true);
        // set a LinearLayoutManager with default horizontal orientation and false value for reverseLayout to show the items from start to end
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false);
        // set LayoutManager to RecyclerView
        recyclerviewPiwcRinnStatus.setLayoutManager(linearLayoutManager);
        // call the constructor of CustomAdapter to send the reference and data to Adapter
        piwcRinnStatusGlobalList = new ArrayList<>();
        selectedAdapterPiwcRinnStatusList = new SelectedAdapterPiwcRinnStatusList(
                piwcRinnStatusGlobalList);
        recyclerviewPiwcRinnStatus.setAdapter(selectedAdapterPiwcRinnStatusList);
        recyclerviewPiwcRinnStatus.setItemAnimator(new DefaultItemAnimator());


        buttonPiwcRinnStatus.setOnClickListener(this);
        edittextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                selectedAdapterPiwcRinnStatusList.getFilter().filter(s);
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }


    @Override
    public void downLoadData() {
        downloadPIWCRinnStatusAsync = new DownloadPIWCRinnStatusAsync();
        downloadPIWCRinnStatusAsync.execute();
    }

    class DownloadPIWCRinnStatusAsync extends AsyncTask<String, String, String> {

        private volatile boolean running = true;
        private String strAutoMandateStatusListError = "";
        private int lstAutoMandateCount = 0;
        private TextView  txterrordesc;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            txterrordesc = findViewById(R.id.txterrordesc);
            txterrordesc.setVisibility(View.GONE);
            mProgressDialog = new ProgressDialog(context, ProgressDialog.THEME_HOLO_LIGHT);
            String Message = "Loading Please wait...";
            mProgressDialog.setMessage(Html.fromHtml("<font color='#00a1e3'><b>" + Message + "<b></font>"));
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setCancelable(true);

            mProgressDialog.setButton("Cancel",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            if (downloadPIWCRinnStatusAsync != null) {
                                downloadPIWCRinnStatusAsync.cancel(true);
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
                SoapObject request;
                //String UserType = commonMethods.GetUserType(context);
                request = new SoapObject(NAMESPACE,
                        METHOD_NAME_PIWC_RINN_STATUS);
                request.addProperty("strFormNo", formNumber);

                System.out.println("request:" + request.toString());
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);
                envelope.dotNet = true;

                envelope.setOutputSoapObject(request);

                commonMethods.TLSv12Enable();



                HttpTransportSE androidHttpTranport = new HttpTransportSE(URl);
                try {
                    androidHttpTranport.call(
                            SOAP_ACTION_PIWC_RINN_STATUS, envelope);
                    Object response = envelope.getResponse();

                    if (!response.toString().contentEquals("<NewDataSet />")) {
                        System.out.println("response:" + response.toString());
                        SoapPrimitive sa;
                        try {
                            sa = (SoapPrimitive) envelope.getResponse();

                            String inputpolicylist = sa.toString();

                            ParseXML prsObj = new ParseXML();

                            inputpolicylist = prsObj.parseXmlTag(
                                    inputpolicylist, "ProposalDetail");

                            strAutoMandateStatusListError = inputpolicylist;

                            if (inputpolicylist != null) {
                                // for agent policy list
                                List<String> Node = prsObj.parseParentNode(
                                        inputpolicylist, "Table");

                                List<ParseXML.PIWCRinnStatusValuesModel> nodeData = prsObj
                                        .parseNodePIWCRinnStatus(Node);
                                piwcRinnStatusGlobalList.clear();
                                piwcRinnStatusGlobalList.addAll(nodeData);
                                lstAutoMandateCount = piwcRinnStatusGlobalList.size();
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
                mProgressDialog.dismiss();
            }
            if (running) {
                edittextSearch.setVisibility(View.GONE);
                textviewRecordCount.setVisibility(View.VISIBLE);
                txterrordesc.setVisibility(View.VISIBLE);
                if (strAutoMandateStatusListError != null) {
                    txterrordesc.setText("");
                    textviewRecordCount.setText("Total Record : "
                            + lstAutoMandateCount);
                    selectedAdapterPiwcRinnStatusList = new SelectedAdapterPiwcRinnStatusList(piwcRinnStatusGlobalList);
                    recyclerviewPiwcRinnStatus.setAdapter(selectedAdapterPiwcRinnStatusList);
                    recyclerviewPiwcRinnStatus.invalidate();

                } else {

                    txterrordesc.setText("No Record Found");
                    textviewRecordCount.setText("Total Record : " + 0);

                    clearList();
                }
            } else {
                commonMethods.showMessageDialog(context,
                        "Server Not Responding,Try again..");
            }
        }
    }

    private void service_hits() {
        String strCIFBDMUserId, strCIFBDMEmailId,
                strCIFBDMPassword, strCIFBDMMObileNo;
        CommonMethods.UserDetailsValuesModel userDetailsValuesModel = commonMethods
                .setUserDetails(context);
        strCIFBDMUserId = userDetailsValuesModel.getStrCIFBDMUserId();
        strCIFBDMEmailId = userDetailsValuesModel.getStrCIFBDMEmailId();
        strCIFBDMPassword = userDetailsValuesModel.getStrCIFBDMPassword();
        strCIFBDMMObileNo = userDetailsValuesModel.getStrCIFBDMMObileNo();

        service = new ServiceHits(context,
                METHOD_NAME_PIWC_RINN_STATUS, formNumber,
                strCIFBDMUserId, strCIFBDMEmailId, strCIFBDMMObileNo,
                strCIFBDMPassword, this);
        service.execute();
    }
    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.buttonPiwcRinnStatus:
                formNumber = edittextFormNumner.getText().toString();
                commonMethods.hideKeyboard(edittextFormNumner,context);

                textviewRecordCount.setVisibility(View.GONE);

                clearList();
                if(commonMethods.isNetworkConnected(context)){

                    if(!TextUtils.isEmpty(formNumber)){
                        service_hits();
                    }
                    else{
                        commonMethods.showMessageDialog(context,"Please Enter Form Number");
                    }
                }
                else{
                    commonMethods.showMessageDialog(context,commonMethods.NO_INTERNET_MESSAGE);
                }
                break;
        }
    }

    private void clearList() {
        if(piwcRinnStatusGlobalList!=null && selectedAdapterPiwcRinnStatusList!=null){
            piwcRinnStatusGlobalList.clear();
            selectedAdapterPiwcRinnStatusList = new SelectedAdapterPiwcRinnStatusList(piwcRinnStatusGlobalList);
            recyclerviewPiwcRinnStatus.setAdapter(selectedAdapterPiwcRinnStatusList);
            recyclerviewPiwcRinnStatus.invalidate();
        }
    }

    public class SelectedAdapterPiwcRinnStatusList extends RecyclerView.Adapter<SelectedAdapterPiwcRinnStatusList.
            ViewHolderAdapter> implements Filterable {

        private ArrayList<ParseXML.PIWCRinnStatusValuesModel>  lstAdapterList, lstSearch;

        SelectedAdapterPiwcRinnStatusList(ArrayList<ParseXML.PIWCRinnStatusValuesModel> lstAdapterList) {
            this.lstAdapterList = lstAdapterList;
        }

        @Override
        public Filter getFilter() {

            return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence charSequence) {

                    final FilterResults oReturn = new FilterResults();
                    final ArrayList<ParseXML.AutoMandateStatusListValuesModel> results = new ArrayList<>();

                    if (lstSearch == null)
                        lstSearch = lstAdapterList;

                    String strSearch = charSequence == null ? "":charSequence.toString();

                    if (!strSearch.equals("")) {
                        if (lstSearch != null && lstSearch.size() > 0) {
                            for (final ParseXML.PIWCRinnStatusValuesModel model : lstSearch) {
                                /*if (model.getPOLICY_NO().toLowerCase().contains(charSequence.toString().toLowerCase())
                                        || model.getSTATUS_UPDATE_DATE().toLowerCase().contains(charSequence.toString().toLowerCase())
                                        || model.getPOLICY_STATUS().toLowerCase().contains(charSequence.toString().toLowerCase())
                                        || model.getPOL_HOLDER_NAME().toLowerCase().contains(charSequence.toString().toLowerCase())) {
                                    results.add(model);
                                }*/
                            }
                        }
                        oReturn.values = results;
                    }else {
                        oReturn.values = piwcRinnStatusGlobalList;
                    }
                    return oReturn;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {

                    lstAdapterList = (ArrayList<ParseXML.PIWCRinnStatusValuesModel>) results.values;
                    selectedAdapterPiwcRinnStatusList = new SelectedAdapterPiwcRinnStatusList(lstAdapterList);
                    recyclerviewPiwcRinnStatus.setAdapter(selectedAdapterPiwcRinnStatusList);

                    notifyDataSetChanged();

                    textviewRecordCount.setText("Total : " + lstAdapterList.size());
                }
            };}


        @Override
        public int getItemCount() {
            return lstAdapterList.size();
        }

        @Override
        public ViewHolderAdapter onCreateViewHolder(ViewGroup parent, int viewType) {
            // infalte the item Layout
            View mView = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.list_item_piwc_rinn_status, parent, false);

            // set the view's size, margins, paddings and layout parameters
            return new ViewHolderAdapter(mView);
        }

        @Override
        public void onBindViewHolder(ViewHolderAdapter holder, int position) {


            holder.textviewPiwcRinnStatusFormNo
                    .setText(lstAdapterList.get(position).getFORM_NO() == null ? ""
                            : lstAdapterList.get(position).getFORM_NO());

            holder.textviewPiwcRinnStatusName.setText(lstAdapterList.get(position).getNAME() == null ? "" : lstAdapterList.get(
                    position).getNAME());
            holder.textviewPiwcRinnStatusStatus.setText(lstAdapterList.get(position).getSTATUS() == null ? "" : lstAdapterList.get(
                    position).getSTATUS());

            holder.textviewPiwcRinnStatusSubStatus.setText(lstAdapterList.get(position).getSUB_STATUS() == null ? ""
                    : lstAdapterList.get(position).getSUB_STATUS());
            holder.textviewPiwcRinnStatusPiwcCallDate.setText(lstAdapterList.get(position).getPIWC_CALL_DATE() == null ? ""
                    : lstAdapterList.get(position).getPIWC_CALL_DATE());
            holder.textviewPiwcRinnStatusPremiumAmount.setText(lstAdapterList.get(position).getPREM_AMT() == null ? ""
                    : lstAdapterList.get(position).getPREM_AMT());
            holder.textviewPiwcRinnStatusRecordAddedDate.setText(lstAdapterList.get(position).getREC_ADD_DT() == null ? ""
                    : lstAdapterList.get(position).getREC_ADD_DT());
        }

        public class ViewHolderAdapter extends RecyclerView.ViewHolder{

            private final TextView textviewPiwcRinnStatusFormNo,textviewPiwcRinnStatusName ,
                    textviewPiwcRinnStatusStatus,textviewPiwcRinnStatusSubStatus,
                    textviewPiwcRinnStatusPiwcCallDate,textviewPiwcRinnStatusPremiumAmount,
                    textviewPiwcRinnStatusRecordAddedDate;

            ViewHolderAdapter(View v) {
                super(v);
                textviewPiwcRinnStatusFormNo = v.findViewById(R.id.textviewPiwcRinnStatusFormNo);
                textviewPiwcRinnStatusName = v.findViewById(R.id.textviewPiwcRinnStatusName);
                textviewPiwcRinnStatusStatus = v.findViewById(R.id.textviewPiwcRinnStatusStatus);
                textviewPiwcRinnStatusSubStatus = v.findViewById(R.id.textviewPiwcRinnStatusSubStatus);
                textviewPiwcRinnStatusPiwcCallDate = v.findViewById(R.id.textviewPiwcRinnStatusPiwcCallDate);

                textviewPiwcRinnStatusPremiumAmount = v.findViewById(R.id.textviewPiwcRinnStatusPremiumAmount);
                textviewPiwcRinnStatusRecordAddedDate = v.findViewById(R.id.textviewPiwcRinnStatusRecordAddedDate);

            }
        }
    }
    @Override
    protected void onDestroy() {
        killTasks();
        super.onDestroy();
    }


    private void killTasks(){
        if(mProgressDialog!=null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }

        if(downloadPIWCRinnStatusAsync!=null){
            downloadPIWCRinnStatusAsync.cancel(true);
        }
        if(service!=null){
            service.cancel(true);
        }
    }

}

