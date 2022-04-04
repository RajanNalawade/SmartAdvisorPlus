package sbilife.com.pointofsale_bancaagency.home.mhr;

import android.annotation.SuppressLint;
import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.List;

import sbilife.com.pointofsale_bancaagency.ParseXML;
import sbilife.com.pointofsale_bancaagency.R;
import sbilife.com.pointofsale_bancaagency.ServiceURL;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;
import sbilife.com.pointofsale_bancaagency.reports.ServiceHits;

public class DigitalMHRActivity extends AppCompatActivity implements ServiceHits.DownLoadData {
    private final String METHOD_NAME = "getdigitalMHR";
    private ProgressDialog mProgressDialog;
    private ArrayList<ParseXML.DigitalMHRValuesModel> globalDataList;
    private Context context;
    private CommonMethods commonMethods;
    private EditText etPolicyNumber, edittextSearch;
    private TextView textviewRecordCount;
    private RecyclerView recyclerview;
    private String policyNumber = "";

    private ServiceHits service;
    private SelectedAdapter selectedAdapter;
    private DigitalMHRAsyncTask digitalMHRAsyncTask;

    String strCIFBDMUserId, strCIFBDMEmailId, strCIFBDMPassword, strCIFBDMMObileNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_digital_mhr);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);
        context = this;
        commonMethods = new CommonMethods();
        commonMethods.setApplicationToolbarMenu(this, "Digital MHR");

        edittextSearch = findViewById(R.id.edittextSearch);
        textviewRecordCount = findViewById(R.id.textviewRecordCount);
        TextView txterrordesc = findViewById(R.id.txterrordesc);

        recyclerview = findViewById(R.id.recyclerview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        // set LayoutManager to RecyclerView
        recyclerview.setLayoutManager(linearLayoutManager);
        // call the constructor of CustomAdapter to send the reference and data to Adapter
        globalDataList = new ArrayList<>();
        selectedAdapter = new SelectedAdapter(globalDataList);
        recyclerview.setAdapter(selectedAdapter);
        recyclerview.setItemAnimator(new DefaultItemAnimator());

        /*edittextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                selectedAdapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });*/

        mProgressDialog = new ProgressDialog(context, ProgressDialog.THEME_HOLO_LIGHT);
        String Message = "Loading Please wait...";
        mProgressDialog.setMessage(Html.fromHtml("<font color='#00a1e3'><b>" + Message + "<b></font>"));
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(true);

        mProgressDialog.setButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {


                        if (digitalMHRAsyncTask != null) {
                            digitalMHRAsyncTask.cancel(true);
                        }

                        if (mProgressDialog != null) {
                            if (mProgressDialog.isShowing()) {
                                mProgressDialog.dismiss();
                            }
                        }
                    }
                });

        mProgressDialog.setMax(100);
        service_hits("");

    }

    private void service_hits(String input) {
        CommonMethods.UserDetailsValuesModel userDetailsValuesModel = commonMethods.setUserDetails(context);
        strCIFBDMUserId = userDetailsValuesModel.getStrCIFBDMUserId();
        strCIFBDMEmailId = userDetailsValuesModel.getStrCIFBDMEmailId();
        strCIFBDMPassword = userDetailsValuesModel.getStrCIFBDMPassword();

        strCIFBDMMObileNo = userDetailsValuesModel.getStrCIFBDMMObileNo();
        service = new ServiceHits(context, METHOD_NAME, input,
                strCIFBDMUserId, strCIFBDMEmailId,
                strCIFBDMMObileNo, strCIFBDMPassword, this);
        service.execute();
    }

    @Override
    public void downLoadData() {
        digitalMHRAsyncTask = new DigitalMHRAsyncTask();
        digitalMHRAsyncTask.execute();
    }

    class DigitalMHRAsyncTask extends AsyncTask<String, String, String> {

        private volatile boolean running = true;
        private String error = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog.show();

            if (mProgressDialog != null && !mProgressDialog.isShowing()) {
                mProgressDialog.show();
            }


        }

        @SuppressLint("SimpleDateFormat")
        @Override
        protected String doInBackground(String... aurl) {
            try {

                running = true;
                SoapObject request;

                /*getPolicyDispatchStatus(string strPolicy)  */


                String NAMESPACE = "http://tempuri.org/";
                request = new SoapObject(NAMESPACE, METHOD_NAME);
                request.addProperty("strCode", strCIFBDMUserId);
                commonMethods.appendSecurityParams(context, request, strCIFBDMEmailId, strCIFBDMMObileNo);
                commonMethods.TLSv12Enable();

                System.out.println("request:" + request.toString());
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);

                commonMethods.TLSv12Enable();


                String URl = ServiceURL.SERVICE_URL;
                HttpTransportSE androidHttpTranport = new HttpTransportSE(URl);
                String SOAP_ACTION = "http://tempuri.org/" + METHOD_NAME;
                androidHttpTranport.call(SOAP_ACTION, envelope);
                Object response = envelope.getResponse();

                if (!response.toString().contentEquals("<CIFPolicyList />")) {

                    ParseXML prsObj = new ParseXML();
                    String inputpolicylist = response.toString();
                    inputpolicylist = prsObj.parseXmlTag(inputpolicylist, "NewDataSet");
                    error = prsObj.parseXmlTag(
                            inputpolicylist, "ScreenData");

                    if (error == null) {
                        // for agent policy list
                        List<String> Node = prsObj.parseParentNode(inputpolicylist, "Table");
                        List<ParseXML.DigitalMHRValuesModel> nodeData = prsObj
                                .parseNodeDigitalMHR(Node);
                        globalDataList.clear();
                        globalDataList.addAll(nodeData);
                        error = "success";
                    } else {
                        error = "1";
                    }

                } else {
                    error = "1";
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
            edittextSearch.setVisibility(View.GONE);
            if (running) {
                if (error.equalsIgnoreCase("success")) {
                    textviewRecordCount.setText("Total Record :" + globalDataList.size());
                    //edittextSearch.setVisibility(View.VISIBLE);
                    selectedAdapter = new SelectedAdapter(globalDataList);
                    recyclerview.setAdapter(selectedAdapter);
                    recyclerview.invalidate();
                } else {
                    commonMethods.showMessageDialog(context, "No Record found");
                    clearList();
                }
            } else {
                commonMethods.showMessageDialog(context, "No Record found");
                clearList();
            }
        }
    }

    public class SelectedAdapter extends RecyclerView.Adapter<SelectedAdapter.
            ViewHolderAdapter> implements Filterable {

        private ArrayList<ParseXML.DigitalMHRValuesModel> lstAdapterList, lstSearch;

        SelectedAdapter(ArrayList<ParseXML.DigitalMHRValuesModel> lstAdapterList) {
            this.lstAdapterList = lstAdapterList;
        }

        @Override
        public Filter getFilter() {

            return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence charSequence) {

                    String charString = charSequence.toString();
                    if (charString.isEmpty()) {
                        lstSearch = globalDataList;
                    } else {
                        final ArrayList<ParseXML.DigitalMHRValuesModel> results = new ArrayList<>();
                        for (final ParseXML.DigitalMHRValuesModel model : globalDataList) {
                            if (model.getPROPOSALNO().toLowerCase().contains(charSequence.toString().toLowerCase())) {
                                results.add(model);
                            }
                        }

                        lstAdapterList = results;
                    }

                    FilterResults filterResults = new FilterResults();
                    filterResults.values = lstAdapterList;
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {

                    lstAdapterList = (ArrayList<ParseXML.DigitalMHRValuesModel>) results.values;
                    selectedAdapter = new SelectedAdapter(lstAdapterList);
                    recyclerview.setAdapter(selectedAdapter);

                    notifyDataSetChanged();
                }
            };
        }


        @Override
        public int getItemCount() {
            return lstAdapterList.size();
        }

        @Override
        public ViewHolderAdapter onCreateViewHolder(ViewGroup parent, int viewType) {
            View mView = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.list_item_digital_mhr, parent, false);

            return new ViewHolderAdapter(mView);
        }

        @Override
        public void onBindViewHolder(final ViewHolderAdapter holder, final int position) {


            holder.textProposalNumber.setText(lstAdapterList.get(position).getPROPOSALNO());
            holder.textProposalNumber.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String link = lstAdapterList.get(position).getBITLYLINK();
                    gotoLink(link);
                }
            });


            holder.imageviewLink.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String link = lstAdapterList.get(position).getBITLYLINK();
                    gotoLink(link);
                }
            });
        }

        private void gotoLink(String link) {


            if (TextUtils.isEmpty(link)) {
                commonMethods.showMessageDialog(context, "Link is not available");
            } else {

                commonMethods.openWebLink(context, link);
                /*Intent intent = new Intent(context,DigitalMHRWebViewActivity.class);
                intent.putExtra("bitlyURL",link);*/
                //startActivity(intent);
                //commonMethods.openDialogToRedirectActivity(context,intent,"Open Bitly Link");
            }
        }

        public class ViewHolderAdapter extends RecyclerView.ViewHolder {


            private final TextView textProposalNumber;

            //private LinearLayout linearlayoutAWBNumber;
            private ImageView imageviewLink;

            ViewHolderAdapter(View v) {
                super(v);
                textProposalNumber = v.findViewById(R.id.textProposalNumber);
                imageviewLink = v.findViewById(R.id.imageviewLink);

            }

        }

    }

    private void clearList() {
        edittextSearch.setVisibility(View.GONE);
        textviewRecordCount.setVisibility(View.GONE);
        textviewRecordCount.setText("");
        edittextSearch.setText("");

        if (globalDataList != null && selectedAdapter != null) {
            globalDataList.clear();
            selectedAdapter = new SelectedAdapter(globalDataList);
            recyclerview.setAdapter(selectedAdapter);
            recyclerview.invalidate();
        }
    }
}
