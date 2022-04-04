package sbilife.com.pointofsale_bancaagency.reports.commonreports;

import android.annotation.SuppressLint;
import androidx.appcompat.app.AppCompatActivity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

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

import sbilife.com.pointofsale_bancaagency.ParseXML;
import sbilife.com.pointofsale_bancaagency.R;
import sbilife.com.pointofsale_bancaagency.ServiceURL;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;
import sbilife.com.pointofsale_bancaagency.reports.ServiceHits;

public class AlternetModeCollectionStatusActivity extends AppCompatActivity implements View.OnClickListener,
        ServiceHits.DownLoadData, AdapterView.OnItemSelectedListener {
    private final String METHOD_NAME = "getDue_reconciliation_SMRT";
    private String NAMESPACE = "http://tempuri.org/";
    private String URl = ServiceURL.SERVICE_URL;

    private ArrayList<ParseXML.AlternateModeCollectionStatusValuesModel> globalDataList;
    private SelectedAdapter selectedAdapter;
    private ServiceHits service;
    private DownloadAlternateModeCollectionStatusAsync downloadAlternateModeCollectionStatusAsync;

    private EditText edittextAlternateModeCollectionStatus, edt_search_proposal;
    private LinearLayout llAlternateModeCollectionStatusPolicyNumber, llAlternateModeCollectionStatusMonth;
    private TextView textViewToDate, textViewFromDate, txterrordesc;
    private RecyclerView recyclerview;

    private ProgressDialog mProgressDialog;


    private Context context;
    private CommonMethods commonMethods;
    private int mYear, mMonth, mDay, datecheck = 0;
    private String typeSelected = "", policyNumber = "", fromDate = "", toDate = "", strCIFBDMUserId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_alternet_mode_collection_status);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);
        context = this;
        commonMethods = new CommonMethods();
        commonMethods.setApplicationToolbarMenu(this, "Alternate Mode Collection Status");

        edittextAlternateModeCollectionStatus = findViewById(R.id.edittextAlternateModeCollectionStatus);
        edt_search_proposal = findViewById(R.id.edt_search_proposal);
        Spinner spinnerAlternateModeCollectionStatusType = findViewById(R.id.spinnerAlternateModeCollectionStatusType);

        llAlternateModeCollectionStatusPolicyNumber = findViewById(R.id.llAlternateModeCollectionStatusPolicyNumber);
        llAlternateModeCollectionStatusMonth = findViewById(R.id.llAlternateModeCollectionStatusMonth);
        textViewToDate = findViewById(R.id.textViewToDate);
        textViewFromDate = findViewById(R.id.textViewFromDate);
        txterrordesc = findViewById(R.id.txterrordesc);
        Button buttonOk = findViewById(R.id.buttonOk);
        recyclerview = findViewById(R.id.recyclerview);

        Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        // set LayoutManager to RecyclerView
        recyclerview.setLayoutManager(linearLayoutManager);
        // call the constructor of CustomAdapter to send the reference and data to Adapter
        globalDataList = new ArrayList<>();
        selectedAdapter = new SelectedAdapter(globalDataList);
        recyclerview.setAdapter(selectedAdapter);
        recyclerview.setItemAnimator(new DefaultItemAnimator());


        buttonOk.setOnClickListener(this);

        llAlternateModeCollectionStatusPolicyNumber.setVisibility(View.GONE);
        llAlternateModeCollectionStatusMonth.setVisibility(View.GONE);

        spinnerAlternateModeCollectionStatusType.setOnItemSelectedListener(this);


        Intent intent = getIntent();
        String fromHome = intent.getStringExtra("fromHome");
        if (fromHome != null && fromHome.equalsIgnoreCase("N")) {

            strCIFBDMUserId = intent.getStringExtra("strBDMCifCOde");

        } else {
            CommonMethods.UserDetailsValuesModel userDetailsValuesModel = commonMethods
                    .setUserDetails(context);
            strCIFBDMUserId = userDetailsValuesModel.getStrCIFBDMUserId();
        }

        mProgressDialog = new ProgressDialog(context, ProgressDialog.THEME_HOLO_LIGHT);
        String Message = "Loading Please wait...";
        mProgressDialog.setMessage(Html.fromHtml("<font color='#00a1e3'><b>" + Message + "<b></font>"));
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(true);


        edt_search_proposal.addTextChangedListener(new TextWatcher() {
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

        mProgressDialog.setButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        if (downloadAlternateModeCollectionStatusAsync != null) {
                            downloadAlternateModeCollectionStatusAsync.cancel(true);
                        }
                        if (mProgressDialog != null) {
                            if (mProgressDialog.isShowing()) {
                                mProgressDialog.dismiss();
                            }
                        }
                    }
                });

        mProgressDialog.setMax(100);


        textViewFromDate.setOnClickListener(this);
        textViewToDate.setOnClickListener(this);
        textViewFromDate.setText(commonMethods.getPreviousMonthDate());
        textViewToDate.setText(commonMethods.getCurrentMonthDate());
    }

    private void service_hits(String input) {
        CommonMethods.UserDetailsValuesModel userDetails = commonMethods
                .setUserDetails(context);


        service = new ServiceHits(context, METHOD_NAME, input,
                strCIFBDMUserId, userDetails.getStrCIFBDMEmailId(),
                userDetails.getStrCIFBDMMObileNo(), userDetails.getStrCIFBDMPassword(), this);
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
                commonMethods.hideKeyboard(edittextAlternateModeCollectionStatus, context);

                txterrordesc.setText("");
                txterrordesc.setVisibility(View.GONE);
                edt_search_proposal.setVisibility(View.GONE);
                clearList();

                if (commonMethods.isNetworkConnected(context)) {
                    StringBuilder input = new StringBuilder();
                    input.append(typeSelected).append(",");

                    if (typeSelected.equalsIgnoreCase("Policy Number")) {
                        policyNumber = edittextAlternateModeCollectionStatus.getText().toString();

                        if (!TextUtils.isEmpty(policyNumber)) {
                            input.append(policyNumber);
                        } else {
                            commonMethods.showMessageDialog(context, "Please Enter Policy Number");
                            return;
                        }
                    } else if (typeSelected.equalsIgnoreCase("Due Period")) {

                        fromDate = textViewFromDate.getText().toString();
                        toDate = textViewToDate.getText().toString();

                        if (TextUtils.isEmpty(toDate) || TextUtils.isEmpty(fromDate)) {
                            commonMethods.showMessageDialog(context, "Please Select Dates");
                            return;
                        } else {
                            final SimpleDateFormat formatter = new SimpleDateFormat(
                                    "dd-MMMM-yyyy");
                            SimpleDateFormat finalDateFormat = new SimpleDateFormat("dd-MMM-yyyy");

                            Date d1 = null, d2 = null;
                            try {
                                d1 = formatter.parse(fromDate);
                                fromDate = finalDateFormat.format(d1);
                                fromDate = fromDate.toUpperCase();

                                d2 = formatter.parse(toDate);
                                toDate = finalDateFormat.format(d2);
                                toDate = toDate.toUpperCase();


                                if ((d2.after(d1)) || (d2.equals(d1))) {
                                    input.append(fromDate).append(",").append(toDate);
                                } else {
                                    commonMethods.showMessageDialog(context, "To date should be greater than From date");
                                    return;
                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }

                    } else {
                        commonMethods.showMessageDialog(context, "Please Select Type");
                        return;
                    }


                    service_hits(input.toString());

                } else {
                    commonMethods.showMessageDialog(context, commonMethods.NO_INTERNET_MESSAGE);
                }
                break;
        }
    }

    @Override
    public void downLoadData() {
        if (typeSelected.equalsIgnoreCase("Policy Number")) {
            fromDate = "";
            toDate = "";
        } else if (typeSelected.equalsIgnoreCase("Due Period")) {
            policyNumber = "";
        }
        downloadAlternateModeCollectionStatusAsync = new DownloadAlternateModeCollectionStatusAsync();
        downloadAlternateModeCollectionStatusAsync.execute();
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

        if (datecheck == 1) {
            textViewFromDate.setText(totaldate);
        } else if (datecheck == 2) {
            textViewToDate.setText(totaldate);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        Spinner spinnerSelected = (Spinner) adapterView;
        switch (spinnerSelected.getId()) {
            case R.id.spinnerAlternateModeCollectionStatusType:
                typeSelected = spinnerSelected.getSelectedItem().toString();
                llAlternateModeCollectionStatusMonth.setVisibility(View.GONE);
                llAlternateModeCollectionStatusPolicyNumber.setVisibility(View.GONE);
                txterrordesc.setVisibility(View.GONE);
                edittextAlternateModeCollectionStatus.setText("");

                edt_search_proposal.setVisibility(View.GONE);
                clearList();

                if (typeSelected.equalsIgnoreCase("Policy Number")) {
                    llAlternateModeCollectionStatusPolicyNumber.setVisibility(View.VISIBLE);
                    llAlternateModeCollectionStatusMonth.setVisibility(View.GONE);

                } else if (typeSelected.equalsIgnoreCase("Due Period")) {
                    llAlternateModeCollectionStatusPolicyNumber.setVisibility(View.GONE);
                    llAlternateModeCollectionStatusMonth.setVisibility(View.VISIBLE);
                } else {
                    llAlternateModeCollectionStatusPolicyNumber.setVisibility(View.GONE);
                    llAlternateModeCollectionStatusMonth.setVisibility(View.GONE);
                }
                break;


        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    class DownloadAlternateModeCollectionStatusAsync extends AsyncTask<String, String, String> {

        private volatile boolean running = true;
        private String status = "";

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

                request = new SoapObject(NAMESPACE, METHOD_NAME);
                //string strPolicyNo, string strFromdate, string strTodate, string strCode
                request.addProperty("strPolicyNo", policyNumber);
                request.addProperty("strFromdate", fromDate);
                request.addProperty("strTodate", toDate);
                request.addProperty("strCode", strCIFBDMUserId);

                System.out.println("request:" + request.toString());
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;

                envelope.setOutputSoapObject(request);

                commonMethods.TLSv12Enable();


                HttpTransportSE androidHttpTranport = new HttpTransportSE(URl);
                String SOAP_ACTION = "http://tempuri.org/getDue_reconciliation_SMRT";
                androidHttpTranport.call(SOAP_ACTION, envelope);
                Object response = envelope.getResponse();

                if (!response.toString().contentEquals("<NewDataSet />")) {
                    SoapPrimitive sa;
                    sa = (SoapPrimitive) envelope.getResponse();

                    String inputpolicylist = sa.toString();

                    ParseXML prsObj = new ParseXML();

                    inputpolicylist = prsObj.parseXmlTag(
                            inputpolicylist, "NewDataSet");
                    status = prsObj.parseXmlTag(
                            inputpolicylist, "ScreenData");

                    if (status == null) {
                        List<String> Node = prsObj.parseParentNode(
                                inputpolicylist, "Table");

                        List<ParseXML.AlternateModeCollectionStatusValuesModel> nodeData = prsObj
                                .parseNodeAlternateModeCollectionStatus(Node);
                        globalDataList.clear();
                        globalDataList.addAll(nodeData);
                        status = "Success";
                    }

                } else {
                    running = false;
                }
            } catch (Exception e) {
                running = false;
            }
            return null;

        }

        @Override
        protected void onProgressUpdate(String... progress) {
            mProgressDialog.setProgress(Integer.parseInt(progress[0]));
        }

        @Override
        protected void onPostExecute(String unused) {
            if (mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
            txterrordesc.setVisibility(View.VISIBLE);
            edt_search_proposal.setVisibility(View.GONE);
            if (running) {
                if (status.equalsIgnoreCase("Success")) {
                    edt_search_proposal.setVisibility(View.VISIBLE);
                    txterrordesc.setText("");
                    selectedAdapter = new SelectedAdapter(globalDataList);
                    recyclerview.setAdapter(selectedAdapter);
                    recyclerview.invalidate();
                } else {

                    txterrordesc.setText("No Record Found");
                    clearList();
                }
            } else {
                txterrordesc.setText("No Record Found");
                clearList();
            }
        }
    }

    class SendSmsAsync extends AsyncTask<String, String, String> {

        private volatile boolean running = true;
        private String response = "";

        private final String policyNumber;
        private final String dueDate;
        private final String amount;

        SendSmsAsync(String policyNumber, String amount, String dueDate) {
            this.policyNumber = policyNumber;
            this.dueDate = dueDate;
            this.amount = amount;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (mProgressDialog != null && !mProgressDialog.isShowing()) {
                mProgressDialog.show();
            }
        }

        @SuppressLint("SimpleDateFormat")
        @Override
        protected String doInBackground(String... aurl) {
            try {

                running = true;

                //sendreconciliationSMS_SMRT(string strPolicyNo, string strAmount, string strDueDate)
                String METHOD_NAME_SEND_SMS = "sendreconciliationSMS_SMRT";
                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME_SEND_SMS);
                request.addProperty("strPolicyNo", policyNumber);
                request.addProperty("strAmount", amount);
                request.addProperty("strDueDate", dueDate);


                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);
                envelope.dotNet = true;

                envelope.setOutputSoapObject(request);

                commonMethods.TLSv12Enable();

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);

                HttpTransportSE androidHttpTranport = new HttpTransportSE(URl);
                String SOAP_ACTION = "http://tempuri.org/" + METHOD_NAME_SEND_SMS;
                androidHttpTranport.call(SOAP_ACTION, envelope);

                SoapPrimitive sa = (SoapPrimitive) envelope.getResponse();
                response = sa.toString();
                if (response.equalsIgnoreCase("1")) {
                    response = "success";
                } else {
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
                    commonMethods.showMessageDialog(context, "Message sent successfully");
                } else {
                    commonMethods.showMessageDialog(context, "Message sending failed");
                }
            } else {
                commonMethods.showMessageDialog(context, "Message sending failed");
            }
        }
    }

    public class SelectedAdapter extends RecyclerView.Adapter<SelectedAdapter.
            ViewHolderAdapter> implements Filterable {

        private ArrayList<ParseXML.AlternateModeCollectionStatusValuesModel> lstAdapterList, lstSearch;

        SelectedAdapter(ArrayList<ParseXML.AlternateModeCollectionStatusValuesModel> lstAdapterList) {
            this.lstAdapterList = lstAdapterList;
        }

        @Override
        public Filter getFilter() {

            return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence charSequence) {

                    final FilterResults oReturn = new FilterResults();
                    final ArrayList<ParseXML.AlternateModeCollectionStatusValuesModel> results = new ArrayList<>();

                    if (lstSearch == null)
                        lstSearch = lstAdapterList;

                    String strSearch = charSequence == null ? "" : charSequence.toString();

                    if (!strSearch.equals("")) {
                        if (lstSearch != null && lstSearch.size() > 0) {
                            for (final ParseXML.AlternateModeCollectionStatusValuesModel model : lstSearch) {
                                if (model.getTDF_POLICY_NUMBER().toLowerCase().contains(charSequence.toString().toLowerCase())
                                        || model.getHOLDER_NAME().toLowerCase().contains(charSequence.toString().toLowerCase())
                                        || model.getTDF_FAILURE_REASON().toLowerCase().contains(charSequence.toString().toLowerCase())
                                        ) {
                                    results.add(model);
                                }
                            }
                        }
                        oReturn.values = results;
                    } else {
                        oReturn.values = globalDataList;
                    }
                    return oReturn;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {

                    lstAdapterList = (ArrayList<ParseXML.AlternateModeCollectionStatusValuesModel>) results.values;
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
                    R.layout.list_item_alternate_mode_collection_status, parent, false);

            return new ViewHolderAdapter(mView);
        }

        @Override
        public void onBindViewHolder(final ViewHolderAdapter holder, int position) {
            holder.textviewPolicyNumber.setText(lstAdapterList.get(position).getTDF_POLICY_NUMBER());
            holder.textviewPolicyHolderName.setText(lstAdapterList.get(position).getHOLDER_NAME());
            holder.textviewDueAmount.setText(lstAdapterList.get(position).getTDF_DUE_AMOUNT());
            holder.textviewDueDate.setText(lstAdapterList.get(position).getTDF_DUE_DATE());

            holder.textviewDateSent.setText(lstAdapterList.get(position).getTDF_DATE_SENT());

            String status = lstAdapterList.get(position).getTDF_STATUS();

            if (status.equalsIgnoreCase("FAILED")) {
                holder.imageviewSMS.setVisibility(View.VISIBLE);

                holder.imageviewSMS.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        commonMethods.hideKeyboard(edt_search_proposal, context);
                        int index = holder.getAdapterPosition();

                        String policyNumber = lstAdapterList.get(index).getTDF_POLICY_NUMBER();
                        String amount = lstAdapterList.get(index).getTDF_DUE_AMOUNT();
                        String dueDate = lstAdapterList.get(index).getTDF_DUE_DATE();
                        if (commonMethods.isNetworkConnected(context)) {
                            SendSmsAsync sendSmsAsync = new SendSmsAsync(policyNumber, amount, dueDate);
                            sendSmsAsync.execute();
                        } else {
                            commonMethods.showMessageDialog(context, commonMethods.NO_INTERNET_MESSAGE);
                        }
                    }
                });

            } else {
                holder.imageviewSMS.setVisibility(View.INVISIBLE);
            }
            holder.textviewStatus.setText(status);

            holder.textviewFailureReason.setText(lstAdapterList.get(position).getTDF_FAILURE_REASON());
            holder.textviewDebitDate.setText(lstAdapterList.get(position).getTDF_DEBIT_DATE());
            holder.textviewPaymentMechanism.setText(lstAdapterList.get(position).getPAYMENT_MECHANISM());
        }

        public class ViewHolderAdapter extends RecyclerView.ViewHolder {

            private final TextView textviewPolicyNumber, textviewPolicyHolderName, textviewDueAmount,
                    textviewDueDate, textviewDateSent, textviewStatus, textviewFailureReason, textviewDebitDate,
                    textviewPaymentMechanism;
            private final ImageView imageviewSMS;

            ViewHolderAdapter(View v) {
                super(v);
                textviewPolicyNumber = v.findViewById(R.id.textviewPolicyNumber);
                textviewPolicyHolderName = v.findViewById(R.id.textviewPolicyHolderName);
                textviewDueAmount = v.findViewById(R.id.textviewDueAmount);
                textviewDueDate = v.findViewById(R.id.textviewDueDate);

                textviewDateSent = v.findViewById(R.id.textviewDateSent);
                textviewStatus = v.findViewById(R.id.textviewStatus);
                textviewFailureReason = v.findViewById(R.id.textviewFailureReason);
                textviewDebitDate = v.findViewById(R.id.textviewDebitDate);
                textviewPaymentMechanism = v.findViewById(R.id.textviewPaymentMechanism);

                imageviewSMS = v.findViewById(R.id.imageviewSMS);

            }

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

        if (downloadAlternateModeCollectionStatusAsync != null) {
            downloadAlternateModeCollectionStatusAsync.cancel(true);
        }
        if (service != null) {
            service.cancel(true);
        }
    }

    private void clearList() {
        if (globalDataList != null && selectedAdapter != null) {
            globalDataList.clear();
            selectedAdapter = new SelectedAdapter(globalDataList);
            recyclerview.setAdapter(selectedAdapter);
            recyclerview.invalidate();
        }
    }
}
