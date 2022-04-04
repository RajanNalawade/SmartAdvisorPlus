package sbilife.com.pointofsale_bancaagency.reports.newbusiness;

import android.annotation.SuppressLint;
import androidx.appcompat.app.AppCompatActivity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import sbilife.com.pointofsale_bancaagency.ParseXML;
import sbilife.com.pointofsale_bancaagency.R;
import sbilife.com.pointofsale_bancaagency.ServiceURL;
import sbilife.com.pointofsale_bancaagency.common.SimpleCrypto;
import sbilife.com.pointofsale_bancaagency.new_bussiness.pivc.HttpConnector1;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;
import sbilife.com.pointofsale_bancaagency.reports.ServiceHits;

public class PIWCAudioCallingPendingListActivity extends AppCompatActivity implements View.OnClickListener, ServiceHits.DownLoadData {

    private final String METHOD_NAME = "getPIWCStatus_Calldesc";

    private CommonMethods commonMethods;
    private Context context;
    private String strCIFBDMUserId = "", strCIFBDMEmailId = "",
            strCIFBDMPassword = "", strCIFBDMMObileNo = "";
    private Spinner spinnerPIWCStatus;
    private TextView textViewFromDate, textViewToDate, textviewRecordCount;

    private EditText edittextSearch;
    private RecyclerView recyclerview;
    private int mYear, mMonth, mDay, datecheck = 0;
    private ServiceHits service;
    private String typeSelected = "", fromDate = "", toDate = "", strPIVCFlag = "";
    private ProgressDialog mProgressDialog;
    private SelectedAdapter selectedAdapter;
    private PIWCAudioCallingAsync piwcAudioCallingAsync;
    private ArrayList<ParseXML.PIWCAudioCallingValuesModel> globalList;

    private Dialog dialog_selectAction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_piwcaudio_calling_pending_list);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);
        context = this;
        commonMethods = new CommonMethods();
        commonMethods.setApplicationToolbarMenu(this, "PIWC(Audio Calling) List");

        spinnerPIWCStatus = findViewById(R.id.spinnerPIWCStatus);
        textViewFromDate = findViewById(R.id.textViewFromDate);
        textViewToDate = findViewById(R.id.textViewToDate);
        textviewRecordCount = findViewById(R.id.textviewRecordCount);
        Button buttonOk = findViewById(R.id.buttonOk);
        edittextSearch = findViewById(R.id.edittextSearch);
        recyclerview = findViewById(R.id.recyclerview);

        Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        mProgressDialog = new ProgressDialog(context, ProgressDialog.THEME_HOLO_LIGHT);
        String Message = "Loading Please wait...";
        mProgressDialog.setMessage(Html.fromHtml("<font color='#00a1e3'><b>" + Message + "<b></font>"));
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(true);


        Intent intent = getIntent();
        String fromHome = intent.getStringExtra("fromHome");

        if (fromHome != null && fromHome.equalsIgnoreCase("Y")) {
            getUserDetails();
        } else {
            strCIFBDMUserId = intent.getStringExtra("strBDMCifCOde");
            strCIFBDMEmailId = intent.getStringExtra("strEmailId");
            strCIFBDMMObileNo = intent.getStringExtra("strMobileNo");
            try {
                strCIFBDMPassword = SimpleCrypto.encrypt("SBIL", "sbil");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
        recyclerview.setLayoutManager(linearLayoutManager);

        globalList = new ArrayList<>();
        selectedAdapter = new SelectedAdapter(
                globalList);
        recyclerview.setAdapter(selectedAdapter);
        recyclerview.setItemAnimator(new DefaultItemAnimator());

        textViewFromDate.setOnClickListener(this);
        textViewToDate.setOnClickListener(this);
        buttonOk.setOnClickListener(this);
        setDates();

        edittextSearch.addTextChangedListener(new TextWatcher() {
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
        });
    }

    private void getUserDetails() {
        CommonMethods.UserDetailsValuesModel userDetailsValuesModel = commonMethods.setUserDetails(context);
        strCIFBDMUserId = userDetailsValuesModel.getStrCIFBDMUserId();
        strCIFBDMEmailId = userDetailsValuesModel.getStrCIFBDMEmailId();
        strCIFBDMPassword = userDetailsValuesModel.getStrCIFBDMPassword();
        strCIFBDMMObileNo = userDetailsValuesModel.getStrCIFBDMMObileNo();
    }

    private void setDates() {
        textViewFromDate.setText(commonMethods.getPreviousMonthDate());
        textViewToDate.setText(commonMethods.getCurrentMonthDate());
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.textViewFromDate:
                datecheck = 1;
                showDateDialog();
                break;

            case R.id.textViewToDate:
                datecheck = 2;
                showDateDialog();
                break;
            case R.id.buttonOk:
                clearList();

                if (commonMethods.isNetworkConnected(context)) {
                    typeSelected = spinnerPIWCStatus.getSelectedItem().toString();
                    StringBuilder input = new StringBuilder();
                    input.append(typeSelected);


                    fromDate = textViewFromDate.getText().toString();
                    toDate = textViewToDate.getText().toString();
                    input.append(",").append(fromDate).append(",").append(toDate);

                    if (TextUtils.isEmpty(toDate) || TextUtils.isEmpty(fromDate)) {
                        commonMethods.showMessageDialog(context, "Please Select Dates");
                    } else {
                        final SimpleDateFormat formatter = new SimpleDateFormat(
                                "dd-MMMM-yyyy", Locale.ENGLISH);
                        SimpleDateFormat finalDateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);

                        Date d1 = null, d2 = null;
                        try {
                            d1 = formatter.parse(fromDate);
                            fromDate = finalDateFormat.format(d1);
                            fromDate = fromDate.toUpperCase();

                            d2 = formatter.parse(toDate);
                            toDate = finalDateFormat.format(d2);
                            toDate = toDate.toUpperCase();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        long difference = Math.abs(d1.getTime() - d2.getTime());
                        long differenceDates = difference / (24 * 60 * 60 * 1000);

                        if (differenceDates <= 181) {
                            if ((d2.after(d1)) || (d2.equals(d1))) {
                                service_hits(input.toString());
                            } else {
                                commonMethods.showMessageDialog(context, "To date should be greater than From date");
                            }
                        } else {
                            commonMethods.showMessageDialog(context, "Difference between To date and From date should not be more than 6 month");
                        }
                    }
                } else {
                    commonMethods.showMessageDialog(context, commonMethods.NO_INTERNET_MESSAGE);
                }
                break;
        }
    }

    private void service_hits(String input) {
        service = new ServiceHits(context,
                METHOD_NAME, input,
                strCIFBDMUserId, strCIFBDMEmailId, strCIFBDMMObileNo,
                strCIFBDMPassword, this);
        service.execute();
    }

    private final DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
            mYear = year;
            mMonth = month;
            mDay = day;
            updateDisplay(mYear, mMonth, mDay);
        }
    };

    @Override
    public void downLoadData() {

        piwcAudioCallingAsync = new PIWCAudioCallingAsync();
        piwcAudioCallingAsync.execute();
    }

    private void showDateDialog() {
        DatePickerDialog dialog = new DatePickerDialog(
                context,
                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                mDateSetListener,
                mYear, mMonth, mDay);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    private void updateDisplay(int year, int month, int day) {

        String y = String.valueOf(year);
        String m = String.valueOf(month + 1);
        String da = String.valueOf(day);


        String monthFormatted = commonMethods.getFullMonthName(m);
        String totaldate = da + "-" + monthFormatted + "-" + y;

        if (datecheck == 1) {
            textViewFromDate.setText(totaldate);
        } else if (datecheck == 2) {
            textViewToDate.setText(totaldate);
        }
    }

    class PIWCAudioCallingAsync extends AsyncTask<String, String, String> {

        private volatile boolean running = true;
        private String error = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog.show();
        }

        @SuppressLint("SimpleDateFormat")
        @Override
        protected String doInBackground(String... aurl) {
            try {

                running = true;
                SoapObject request;
                String NAMESPACE = "http://tempuri.org/";
                request = new SoapObject(NAMESPACE, METHOD_NAME);
                request.addProperty("strType", typeSelected);
                request.addProperty("strFromdate", fromDate);
                request.addProperty("strTodate", toDate);
                request.addProperty("strCode", strCIFBDMUserId);
                request.addProperty("strEmailId", strCIFBDMEmailId);
                request.addProperty("strMobileNo", strCIFBDMMObileNo);
                request.addProperty("strAuthKey", commonMethods.getStrAuth());

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

                if (!response.toString().contentEquals("<NewDataSet />")) {
                    System.out.println("response:" + response.toString());
                    SoapPrimitive sa;
                    sa = (SoapPrimitive) envelope.getResponse();

                    String inputpolicylist = sa.toString();

                    ParseXML prsObj = new ParseXML();

                    inputpolicylist = prsObj.parseXmlTag(
                            inputpolicylist, "NewDataSet");
                    inputpolicylist = prsObj.parseXmlTag(
                            inputpolicylist, "ScreenData");
                    error = inputpolicylist;

                    if (error == null) {
                        // for agent policy list

                        inputpolicylist = sa.toString();
                        inputpolicylist = prsObj.parseXmlTag(
                                inputpolicylist, "NewDataSet");

                        List<String> Node = prsObj.parseParentNode(
                                inputpolicylist, "Table");

                        List<ParseXML.PIWCAudioCallingValuesModel> nodeData = prsObj
                                .parseNodePIWCAudioCalling(Node);
                        globalList.clear();
                        globalList.addAll(nodeData);
                    }

                } else {
                    running = false;
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
            textviewRecordCount.setVisibility(View.VISIBLE);
            if (running) {
                edittextSearch.setVisibility(View.GONE);

                if (error == null) {
                    edittextSearch.setVisibility(View.VISIBLE);
                    selectedAdapter = new SelectedAdapter(globalList);
                    recyclerview.setAdapter(selectedAdapter);
                    recyclerview.invalidate();
                    String count = "Total Count : " + globalList.size();
                    textviewRecordCount.setText(count);
                } else {
                    commonMethods.showMessageDialog(context, "No Record Found");
                    clearList();
                }
            } else {
                commonMethods.showMessageDialog(context, "No Record Found");
                clearList();
            }
        }
    }


    public class SelectedAdapter extends RecyclerView.Adapter<SelectedAdapter.
            ViewHolderAdapter> implements Filterable {

        private ArrayList<ParseXML.PIWCAudioCallingValuesModel> lstAdapterList, lstSearch;

        SelectedAdapter(ArrayList<ParseXML.PIWCAudioCallingValuesModel> lstAdapterList) {
            this.lstAdapterList = lstAdapterList;
        }

        @Override
        public Filter getFilter() {

            return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence charSequence) {

                    final FilterResults oReturn = new FilterResults();
                    final ArrayList<ParseXML.PIWCAudioCallingValuesModel> results = new ArrayList<>();

                    if (lstSearch == null)
                        lstSearch = lstAdapterList;

                    String strSearch = charSequence == null ? "" : charSequence.toString();

                    if (!strSearch.equals("")) {
                        if (lstSearch != null && lstSearch.size() > 0) {
                            for (final ParseXML.PIWCAudioCallingValuesModel model : lstSearch) {
                                if (model.getPL_PROP_NUM().toLowerCase().contains(charSequence.toString().toLowerCase())) {
                                    results.add(model);
                                }
                            }
                        }
                        oReturn.values = results;
                    } else {
                        oReturn.values = globalList;
                    }
                    return oReturn;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {

                    lstAdapterList = (ArrayList<ParseXML.PIWCAudioCallingValuesModel>) results.values;
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
            // infalte the item Layout
            View mView = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.list_item_piwc_audio_calling, parent, false);
            return new ViewHolderAdapter(mView);
        }

        @Override
        public void onBindViewHolder(ViewHolderAdapter holder, int position) {
            holder.tvProposalNo.setText(lstAdapterList.get(position).getPL_PROP_NUM());
            holder.tvProposalName.setText(lstAdapterList.get(position).getPR_FULL_NM());
        }

        public class ViewHolderAdapter extends RecyclerView.ViewHolder{

            private final TextView tvProposalNo,tvProposalName;

            ViewHolderAdapter(View v) {
                super(v);
                tvProposalNo = v.findViewById(R.id.tvProposalNo);
                tvProposalName= v.findViewById(R.id.tvProposalName);

                v.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {

                        int selectedItem = getLayoutPosition();

                        List lstSubMenus = new ArrayList();

                        lstSubMenus.add("PIWC Status");
                        lstSubMenus.add("Send Link");

                        dialog_selectAction = new Dialog(context);
                        dialog_selectAction.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog_selectAction.setContentView(R.layout.dialog_submenu_all);
                        dialog_selectAction.getWindow().setBackgroundDrawable(
                                new ColorDrawable(android.graphics.Color.TRANSPARENT));
                        dialog_selectAction.setCancelable(false);

                        TextView dia_aob_req_upload_title = dialog_selectAction.findViewById(R.id.dia_aob_req_upload_title);
                        dia_aob_req_upload_title.setText("Select Action");
                        ImageView dia_aob_req_cancel = dialog_selectAction.findViewById(R.id.dia_aob_req_cancel);

                        RecyclerView dia_aob_req_upload_list = (RecyclerView) dialog_selectAction.findViewById(R.id.dia_aob_req_upload_list);

                        // call the constructor of CustomAdapter to send the reference and data to Adapter
                        DialogAdapterAOBRequirementDocs clad = new DialogAdapterAOBRequirementDocs(context, lstSubMenus, selectedItem);

                        // set a LinearLayoutManager with default horizontal orientation and false value for reverseLayout to show the items from start to end
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
                        // set LayoutManager to RecyclerView
                        dia_aob_req_upload_list.setLayoutManager(linearLayoutManager);
                        dia_aob_req_upload_list.setAdapter(clad);
                        dia_aob_req_upload_list.setItemAnimator(new DefaultItemAnimator());

                        dia_aob_req_cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog_selectAction.dismiss();
                            }
                        });

                        dialog_selectAction.show();

                        return false;
                    }
                });

            }
        }
    }

    private void clearList() {
        if (globalList != null && selectedAdapter != null) {
            textviewRecordCount.setText("");
            globalList.clear();
            selectedAdapter = new SelectedAdapter(globalList);
            recyclerview.setAdapter(selectedAdapter);
            recyclerview.invalidate();
        }
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

        if (piwcAudioCallingAsync != null) {
            piwcAudioCallingAsync.cancel(true);
        }

        if (service != null) {
            service.cancel(true);
        }
    }

    public class GetPIVCDetails extends AsyncTask<String, String, String> {

        private volatile boolean running = true;
        private String strResOutput = "", strPIVCProposerNo;


        public GetPIVCDetails(String strPIVCProposerNo) {
            this.strPIVCProposerNo = strPIVCProposerNo;
        }

        @Override
        protected void onPreExecute() {
            mProgressDialog = new ProgressDialog(context,
                    ProgressDialog.THEME_HOLO_LIGHT);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.setTitle(Html.fromHtml("<font color='#00a1e3'><b>Please Wait...<b></font>"));
            mProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            if (commonMethods.isNetworkConnected(context)) {
                try {
                    running = true;

                    String sbil_data = "";

                    HttpConnector1.setServerCert(getResources().openRawResource(R.raw.combohttpscertificate));

                    switch (strPIVCFlag) {

                        case "GETLINK":

                            String RequestLinkData = "------WebKitFormBoundary7MA4YWxkTrZu0gW\r\nContent-Disposition: form-data; name=\"proposal_no\"\r\n\r\n" + strPIVCProposerNo + "\r\n------WebKitFormBoundary7MA4YWxkTrZu0gW--";

                            //for production
                            strResOutput = HttpConnector1.getInstance().postData_PIVCGETLINK("https://pivc.sbilife.co.in/portal/static/page/resendSMSProposalLink_process", strPIVCProposerNo, RequestLinkData);

                            //for uat
                            //strResOutput = HttpConnector1.getInstance().postData_PIVCGETLINK("https://pivc-uat.sbilife.co.in/portal/static/page/resendSMSProposalLink_process", strPIVCProposerNo, RequestLinkData);

                            break;

                        default:
                            break;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    running = false;
                    return commonMethods.WEEK_INTERNET_MESSAGE;
                }
            } else {
                return commonMethods.NO_INTERNET_MESSAGE;
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            if (mProgressDialog.isShowing())
                mProgressDialog.dismiss();

            if (running) {

                try {
                    if (!strResOutput.equals("")) {
                        JSONObject json_main = new JSONObject(strResOutput);
                        String PIVCLink_status = json_main.get("status").toString();
                        String PIVC_msg = json_main.get("msg").toString();

                        if (PIVCLink_status.equalsIgnoreCase("true")) {
                            String PIVC_output = json_main.get("output").toString();

                            JSONObject json_output = new JSONObject(PIVC_output);

                            /*if (strPIVCFlag.equalsIgnoreCase("GETSTATUS")) {
                                String completed_status_text = json_output.get("completed_status_text").toString();
                                boolean completed_status = (Boolean) json_output.get("completed_status");

                                if (!completed_status || completed_status_text.equalsIgnoreCase("Not Completed")) {
                                    strPIVCFlag = "GETLINK";
                                    new GetPIVCDetails(strPIVCProposerNo).execute();
                                } else if (completed_status) {
                                    strPIVCProposerNo = "";
                                    commonMethods.showToast(context, "PIVC Done SuccessFully !!");
                                }

                            } else*/
                            if (strPIVCFlag.equalsIgnoreCase("GETLINK")) {
                                commonMethods.showMessageDialog(context, "PIVC Link has been sent to Customer.");
                            }

                            //commonMethods.showToast(context, PIVC_msg);

                        } else if (PIVC_msg.contains("Invalid proposal number")) {

                            strPIVCFlag = "GETLINK";
                            new GetPIVCDetails(strPIVCProposerNo).execute();

                        } else {
                            commonMethods.showToast(context, PIVC_msg);
                        }
                    } else {
                        commonMethods.showMessageDialog(context, "Link is not generated.");
                    }
                } catch (Exception e) {
                    commonMethods.showToast(context, e.getMessage());
                }
            } else {
                commonMethods.showToast(context, s);
            }
        }
    }

    public class DialogAdapterAOBRequirementDocs extends RecyclerView.Adapter<DialogAdapterAOBRequirementDocs.ViewHolderAdapter> {

        private final Context mAdapterContext;
        private List lstDialogAdapterList;
        private int mainPos;

        DialogAdapterAOBRequirementDocs(Context mAdapterContext, List lstDialogAdapterList, int position) {
            this.mAdapterContext = mAdapterContext;
            this.lstDialogAdapterList = lstDialogAdapterList;
            mainPos = position;
        }

        @Override
        public int getItemCount() {
            return lstDialogAdapterList.size();
        }

        @NonNull
        @Override
        public ViewHolderAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            // infalte the item Layout
            //layout also used ActivityAOBAgentReqDoc.java
            View mView = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.row_dialog_submenus, parent, false);

            // set the view's size, margins, paddings and layout parameters
            return new ViewHolderAdapter(mView);
        }

        @Override
        public void onBindViewHolder(ViewHolderAdapter holder, final int position) {

            holder.tvMenuItem.setText(lstDialogAdapterList.get(position).toString());

            holder.tvMenuItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (globalList.size() > 0){
                        String proposalNumber = globalList.get(mainPos).getPL_PROP_NUM();
                        proposalNumber = proposalNumber == null ? "" : proposalNumber;

                        if (dialog_selectAction.isShowing())
                            dialog_selectAction.dismiss();

                        switch (position){
                            case 0:

                                Intent intent = new Intent(context, BancaReportsPIWCActivity.class);
                                intent.putExtra("fromHome", "PIWC");
                                intent.putExtra("ProposalNumber", proposalNumber);
                                startActivity(intent);

                                break;

                            case 1:
                                if (proposalNumber.length() == 10) {

                                    strPIVCFlag = "GETLINK";
                                    new GetPIVCDetails(proposalNumber).execute();
                                }

                                break;

                            default:
                                break;
                        }

                    }
                }
            });
        }

        public class ViewHolderAdapter extends RecyclerView.ViewHolder {

            private final TextView tvMenuItem;

            ViewHolderAdapter(View v) {
                super(v);

                tvMenuItem = v.findViewById(R.id.tvMenuItem);
            }
        }
    }
}


