package sbilife.com.pointofsale_bancaagency.reports.underwriting;

import android.annotation.SuppressLint;
import androidx.appcompat.app.AppCompatActivity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import sbilife.com.pointofsale_bancaagency.ParseXML;
import sbilife.com.pointofsale_bancaagency.R;
import sbilife.com.pointofsale_bancaagency.ServiceURL;
import sbilife.com.pointofsale_bancaagency.common.SendSmsAsyncTask;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;
import sbilife.com.pointofsale_bancaagency.reports.ServiceHits;

public class ViewMedicalStatusTeleMERActivity extends AppCompatActivity implements View.OnClickListener,
        ServiceHits.DownLoadData, AdapterView.OnItemSelectedListener {
    private final String METHOD_NAME = "getMidicalView_smrt_TELEMER";
    private ProgressDialog mProgressDialog;

    private Spinner spinnerViewMedicalStatusMode;
    private LinearLayout llViewMedicalMode, llViewMedicalStatusProposalNumber, llViewMedicalStatusDates;
    private EditText etViewMedicalStatusProposalNo, edittextSearch;
    private TextView textViewFromDate, textViewToDate, textviewRecordCount, txterrordesc;
    private RecyclerView recyclerview;

    private Context context;
    private CommonMethods commonMethods;
    private String typeSelected = "", modeSelected = "", proposalNumber = "", fromDate = "", toDate = "",
            strCIFBDMUserId;
    private int mYear, mMonth, mDay, datecheck = 0;
    private ServiceHits service;

    private ArrayList<ViewMedicalStatusTeleMERValuesModel> globalDataList;
    private DownloadViewMedicalStatusTeleMERAsync downloadViewMedicalStatusTeleMERAsync;
    private SelectedAdapter selectedAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_view_medical_status_tele_m_e_r);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);
        context = this;
        commonMethods = new CommonMethods();
        commonMethods.setApplicationToolbarMenu(this, "View Medical TeleMER Status");

        Spinner spinnerViewMedicalStatusType = findViewById(R.id.spinnerViewMedicalStatusType);
        spinnerViewMedicalStatusMode = findViewById(R.id.spinnerViewMedicalStatusMode);
        llViewMedicalMode = findViewById(R.id.llViewMedicalMode);
        llViewMedicalStatusProposalNumber = findViewById(R.id.llViewMedicalStatusProposalNumber);
        llViewMedicalStatusDates = findViewById(R.id.llViewMedicalStatusDates);
        etViewMedicalStatusProposalNo = findViewById(R.id.etViewMedicalStatusProposalNo);
        edittextSearch = findViewById(R.id.edittextSearch);
        textViewFromDate = findViewById(R.id.textViewFromDate);
        textViewToDate = findViewById(R.id.textViewToDate);
        textviewRecordCount = findViewById(R.id.textviewRecordCount);
        txterrordesc = findViewById(R.id.txterrordesc);
        Button buttonOk = findViewById(R.id.buttonOk);
        recyclerview = findViewById(R.id.recyclerview);

        textViewFromDate.setOnClickListener(this);
        textViewToDate.setOnClickListener(this);
        buttonOk.setOnClickListener(this);

        spinnerViewMedicalStatusType.setOnItemSelectedListener(this);
        spinnerViewMedicalStatusMode.setOnItemSelectedListener(this);

        Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        textViewFromDate.setText(commonMethods.getPreviousMonthDate());
        textViewToDate.setText(commonMethods.getCurrentMonthDate());

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

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        // set LayoutManager to RecyclerView
        recyclerview.setLayoutManager(linearLayoutManager);
        // call the constructor of CustomAdapter to send the reference and data to Adapter
        globalDataList = new ArrayList<>();
        selectedAdapter = new SelectedAdapter(globalDataList);
        recyclerview.setAdapter(selectedAdapter);
        recyclerview.setItemAnimator(new DefaultItemAnimator());

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

        mProgressDialog.setButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        if (downloadViewMedicalStatusTeleMERAsync != null) {
                            downloadViewMedicalStatusTeleMERAsync.cancel(true);
                        }
                        if (mProgressDialog != null) {
                            if (mProgressDialog.isShowing()) {
                                mProgressDialog.dismiss();
                            }
                        }
                    }
                });

        mProgressDialog.setMax(100);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonOk:
                commonMethods.hideKeyboard(etViewMedicalStatusProposalNo, context);
                txterrordesc.setText("");
                txterrordesc.setVisibility(View.GONE);
                textviewRecordCount.setVisibility(View.GONE);
                edittextSearch.setVisibility(View.GONE);
                clearList();

                if (commonMethods.isNetworkConnected(context)) {
                    StringBuffer input = new StringBuffer();
                    input.append(typeSelected);
                    if (typeSelected.equalsIgnoreCase("Individual")) {
                        input.append(",").append(modeSelected).append(",");
                        if (modeSelected.equalsIgnoreCase("Date")) {

                            fromDate = textViewFromDate.getText().toString();
                            toDate = textViewToDate.getText().toString();
                            if (TextUtils.isEmpty(toDate) || TextUtils.isEmpty(fromDate)) {
                                commonMethods.showMessageDialog(context, "Please Select Dates");
                            } else {
                                final SimpleDateFormat formatter = new SimpleDateFormat("dd-MMMM-yyyy");
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
                        } else if (modeSelected.equalsIgnoreCase("Proposal Number")) {

                            if (!validateProposalNumber()) {
                                return;
                            }

                        } else if (modeSelected.equalsIgnoreCase("Select Mode")) {
                            commonMethods.showMessageDialog(context, "Please Select Mode");
                            return;
                        }

                    } else if (typeSelected.equalsIgnoreCase("Rinn Raksha")) {
                        if (!validateProposalNumber()) {
                            return;
                        }

                    } else if (typeSelected.equalsIgnoreCase("Select Type")) {
                        commonMethods.showMessageDialog(context, "Please Select Type");
                        return;
                    }
                    service_hits(input.toString());

                } else {
                    commonMethods.showMessageDialog(context, commonMethods.NO_INTERNET_MESSAGE);
                }

                break;
            case R.id.textViewFromDate:
                datecheck = 1;
                showDateDialog();
                break;
            case R.id.textViewToDate:
                datecheck = 2;
                showDateDialog();
                break;
        }
    }

    private boolean validateProposalNumber() {
        proposalNumber = etViewMedicalStatusProposalNo.getText().toString();
        if (!TextUtils.isEmpty(proposalNumber)) {
            return true;
        } else {
            commonMethods.showMessageDialog(context, "Please Enter Proposal Number");
            return false;
        }
    }

    private void service_hits(String input) {
        CommonMethods.UserDetailsValuesModel userDetails = commonMethods
                .setUserDetails(context);

        service = new ServiceHits(context, METHOD_NAME, input,
                strCIFBDMUserId, userDetails.getStrCIFBDMEmailId(),
                userDetails.getStrCIFBDMMObileNo(), userDetails.getStrCIFBDMPassword(), this);
        service.execute();
    }

    @Override
    public void downLoadData() {

        if (typeSelected.equalsIgnoreCase("Individual")) {
            if (modeSelected.equalsIgnoreCase("Date")) {
                proposalNumber = "";
            } else if (modeSelected.equalsIgnoreCase("Proposal Number")) {
                fromDate = "";
                toDate = "";
            }
        } else if (typeSelected.equalsIgnoreCase("Rinn Raksha")) {
            fromDate = "";
            toDate = "";
        }
        downloadViewMedicalStatusTeleMERAsync = new DownloadViewMedicalStatusTeleMERAsync();
        downloadViewMedicalStatusTeleMERAsync.execute();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        Spinner spinnerSelected = (Spinner) adapterView;
        etViewMedicalStatusProposalNo.setText("");
        edittextSearch.setVisibility(View.GONE);
        txterrordesc.setText("");
        txterrordesc.setVisibility(View.GONE);
        textviewRecordCount.setText("");
        textviewRecordCount.setVisibility(View.GONE);
        clearList();

        switch (spinnerSelected.getId()) {
            case R.id.spinnerViewMedicalStatusType:
                typeSelected = spinnerSelected.getSelectedItem().toString();
                proposalNumber = "";
                fromDate = "";
                toDate = "";

                spinnerViewMedicalStatusMode.setSelection(0);
                modeSelected = "Select Mode";

                if (typeSelected.equalsIgnoreCase("Individual")) {
                    llViewMedicalMode.setVisibility(View.VISIBLE);
                    llViewMedicalStatusDates.setVisibility(View.GONE);
                    llViewMedicalStatusProposalNumber.setVisibility(View.GONE);
                } else if (typeSelected.equalsIgnoreCase("Rinn Raksha")) {
                    llViewMedicalMode.setVisibility(View.GONE);
                    llViewMedicalStatusDates.setVisibility(View.GONE);
                    llViewMedicalStatusProposalNumber.setVisibility(View.VISIBLE);
                } else if (typeSelected.equalsIgnoreCase("Select Type")) {
                    llViewMedicalMode.setVisibility(View.GONE);
                    llViewMedicalStatusDates.setVisibility(View.GONE);
                    llViewMedicalStatusProposalNumber.setVisibility(View.GONE);
                }
                break;

            case R.id.spinnerViewMedicalStatusMode:
                modeSelected = spinnerSelected.getSelectedItem().toString();
                if (modeSelected.equalsIgnoreCase("Date")) {
                    llViewMedicalStatusDates.setVisibility(View.VISIBLE);
                    llViewMedicalStatusProposalNumber.setVisibility(View.GONE);
                } else if (modeSelected.equalsIgnoreCase("Proposal Number")) {
                    llViewMedicalStatusDates.setVisibility(View.GONE);
                    llViewMedicalStatusProposalNumber.setVisibility(View.VISIBLE);
                } else if (modeSelected.equalsIgnoreCase("Select Mode")) {
                    llViewMedicalStatusDates.setVisibility(View.GONE);
                    llViewMedicalStatusProposalNumber.setVisibility(View.GONE);
                }
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

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

    private final DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {

            mYear = year;
            mMonth = month;
            mDay = day;
            updateDisplay(mYear, mMonth, mDay);
        }
    };

    private void showDateDialog() {
        DatePickerDialog dialog = new DatePickerDialog(
                context,
                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                mDateSetListener,
                mYear, mMonth, mDay);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    class DownloadViewMedicalStatusTeleMERAsync extends AsyncTask<String, String, String> {

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
                String NAMESPACE = "http://tempuri.org/";
                request = new SoapObject(NAMESPACE, METHOD_NAME);
                //string strCode, string strFromdate, string strTodate,
                // string strPropNo,string strBRcode, string strEmailId, string strMobileNo, string strAuthKey
                request.addProperty("strCode", strCIFBDMUserId);
                request.addProperty("strFromdate", fromDate);
                request.addProperty("strTodate", toDate);
                request.addProperty("strPropNo", proposalNumber);
                request.addProperty("strBRcode", "");
                commonMethods.appendSecurityParams(context, request, "", "");
                System.out.println("request:" + request.toString());
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;

                envelope.setOutputSoapObject(request);

                commonMethods.TLSv12Enable();

                String URl = ServiceURL.SERVICE_URL;
                HttpTransportSE androidHttpTranport = new HttpTransportSE(URl);
                String SOAP_ACTION = "http://tempuri.org/getMidicalView_smrt_TELEMER";
                androidHttpTranport.call(SOAP_ACTION, envelope);
                Object response = envelope.getResponse();

                if (!response.toString().contentEquals("<NewDataSet />")) {
                    SoapPrimitive sa;
                    sa = (SoapPrimitive) envelope.getResponse();

                    String inputpolicylist = sa.toString();

                    ParseXML prsObj = new ParseXML();

                    inputpolicylist = prsObj.parseXmlTag(
                            inputpolicylist, "Data");
                    status = prsObj.parseXmlTag(
                            inputpolicylist, "ScreenData");

                    if (status == null) {
                        List<String> Node = prsObj.parseParentNode(
                                inputpolicylist, "Table");

                        ArrayList<ViewMedicalStatusTeleMERValuesModel> nodeData =
                                parseNodeViewMedicalStatusTeleMER(Node);
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
            edittextSearch.setVisibility(View.GONE);
            textviewRecordCount.setVisibility(View.VISIBLE);
            if (running) {
                if (status.equalsIgnoreCase("Success")) {
                    edittextSearch.setVisibility(View.VISIBLE);
                    txterrordesc.setText("");
                    textviewRecordCount.setText("Total Record :" + globalDataList.size());
                    selectedAdapter = new SelectedAdapter(globalDataList);
                    recyclerview.setAdapter(selectedAdapter);
                    recyclerview.invalidate();
                } else {

                    txterrordesc.setText("No Record Found");
                    textviewRecordCount.setText("Total Record :0");
                    clearList();
                }
            } else {
                txterrordesc.setText("No Record Found");
                textviewRecordCount.setText("Total Record :0");
                clearList();
            }
        }
    }

    public class SelectedAdapter extends RecyclerView.Adapter<SelectedAdapter.
            ViewHolderAdapter> implements Filterable {

        private ArrayList<ViewMedicalStatusTeleMERValuesModel> lstAdapterList, lstSearch;

        SelectedAdapter(ArrayList<ViewMedicalStatusTeleMERValuesModel> lstAdapterList) {
            this.lstAdapterList = lstAdapterList;
        }

        @Override
        public Filter getFilter() {

            return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence charSequence) {

                    final FilterResults oReturn = new FilterResults();
                    final ArrayList<ViewMedicalStatusTeleMERValuesModel> results = new ArrayList<>();

                    if (lstSearch == null)
                        lstSearch = lstAdapterList;

                    String strSearch = charSequence == null ? "" : charSequence.toString();

                    if (!strSearch.equals("")) {
                        if (lstSearch != null && lstSearch.size() > 0) {
                            for (final ViewMedicalStatusTeleMERValuesModel model : lstSearch) {
                                if (model.getPROPOSAL_NO().toLowerCase().contains(charSequence.toString().toLowerCase())
                                        || model.getCUSTOMER_NAME().toLowerCase().contains(charSequence.toString().toLowerCase())
                                        || model.getMEDICAL_DONE_DATE().toLowerCase().contains(charSequence.toString().toLowerCase())
                                        || model.getMAIN_STATUS().toLowerCase().contains(charSequence.toString().toLowerCase())
                                        || model.getINTIMATION_DATE().toLowerCase().contains(charSequence.toString().toLowerCase())
                                        || model.getTEST_NAME().toLowerCase().contains(charSequence.toString().toLowerCase())
                                        || model.getSUB_STATUS().toLowerCase().contains(charSequence.toString().toLowerCase())
                                        || model.getSUB_STATUS1().toLowerCase().contains(charSequence.toString().toLowerCase())
                                        || model.getCALLING_HISTORY().toLowerCase().contains(charSequence.toString().toLowerCase())
                                        || model.getCALLING_REMARKS().toLowerCase().contains(charSequence.toString().toLowerCase())
                                        || model.getLINK_FOR_SCHEDULING().toLowerCase().contains(charSequence.toString().toLowerCase())
                                        || model.getTYPE().toLowerCase().contains(charSequence.toString().toLowerCase())
                                        || model.getTPA_NAME().toLowerCase().contains(charSequence.toString().toLowerCase())
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

                    lstAdapterList = (ArrayList<ViewMedicalStatusTeleMERValuesModel>) results.values;
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
                    R.layout.list_item_view_medical_status_telemer, parent, false);

            return new ViewHolderAdapter(mView);
        }

        @Override
        public void onBindViewHolder(final ViewHolderAdapter holder, final int position) {
            holder.textvieProposalNumber.setText(lstAdapterList.get(position).getPROPOSAL_NO());
            holder.textviewCustomerName.setText(lstAdapterList.get(position).getCUSTOMER_NAME());
            holder.textviewTPAName.setText(lstAdapterList.get(position).getTPA_NAME());
            holder.textviewIntimationDate.setText(lstAdapterList.get(position).getINTIMATION_DATE());

            holder.textviewMedicalDoneDate.setText(lstAdapterList.get(position).getMEDICAL_DONE_DATE());
            holder.textviewTestName.setText(lstAdapterList.get(position).getTEST_NAME());
            holder.textviewMainStatus.setText(lstAdapterList.get(position).getMAIN_STATUS());
            holder.textviewSubStatus.setText(lstAdapterList.get(position).getSUB_STATUS());

            holder.textviewSubStatusOne.setText(lstAdapterList.get(position).getSUB_STATUS1());
            holder.textviewCallingHistory.setText(lstAdapterList.get(position).getCALLING_HISTORY());
            holder.textviewCallingRemarks.setText(lstAdapterList.get(position).getCALLING_REMARKS());
            holder.textviewMobileNumber.setText(lstAdapterList.get(position).getMOBILE());
            holder.textviewType.setText(lstAdapterList.get(position).getTYPE());

            holder.tvlinkScheduling.setText(lstAdapterList.get(position).getLINK_FOR_SCHEDULING());

            holder.tvlinkScheduling.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String link = lstAdapterList.get(position).getLINK_FOR_SCHEDULING();
                    commonMethods.openWebLink(context, link);
                }
            });

            holder.imageviewLink.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String link = lstAdapterList.get(position).getLINK_FOR_SCHEDULING();
                    commonMethods.openWebLink(context, link);
                }
            });

            holder.imgcontact_cust_r.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String mobileNumber = holder.textviewMobileNumber.getText().toString();

                    if (!TextUtils.isEmpty(mobileNumber)) {
                        commonMethods.callMobileNumber(mobileNumber, context);
                    }
                }
            });
            holder.textviewMobileNumber.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String mobileNumber = holder.textviewMobileNumber.getText().toString();
                    if (!TextUtils.isEmpty(mobileNumber)) {
                        commonMethods.callMobileNumber(mobileNumber, context);
                    }
                }
            });

            holder.buttonSendLink.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final String mobileNumber = holder.textviewMobileNumber.getText().toString();
                    if (!TextUtils.isEmpty(mobileNumber)) {

                        final Dialog dialog = new Dialog(context);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.loading_window_twobutton);
                        TextView text = dialog.findViewById(R.id.txtalertheader);
                        text.setText("Do you want to send Link.");
                        Button dialogButton = dialog.findViewById(R.id.btnalert);
                        dialogButton.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                dialog.dismiss();
                                try {
                                    String link = lstAdapterList.get(position).getLINK_FOR_SCHEDULING();
                                    String message = "Dear Customer, \nPlease click on link to schedule your appointment\n" + link;
                                    SendSmsAsyncTask sendSmsAsyncTask = new SendSmsAsyncTask(context, mobileNumber, message);
                                    sendSmsAsyncTask.execute("");
                                } catch (Exception e) {
                                    commonMethods.showMessageDialog(context, "Problem in sending Link, please Try Again");
                                }
                            }
                        });
                        Button dialogButtoncancel = dialog.findViewById(R.id.btnalertcancel);
                        dialogButtoncancel.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });

                        dialog.show();

                    }
                }
            });
        }

        public class ViewHolderAdapter extends RecyclerView.ViewHolder {

            private final TextView textvieProposalNumber, textviewCustomerName, textviewTPAName,
                    textviewIntimationDate, textviewMedicalDoneDate, textviewTestName, textviewMainStatus,
                    textviewSubStatus, tvlinkScheduling, textviewSubStatusOne, textviewCallingHistory,
                    textviewCallingRemarks, textviewType, textviewMobileNumber;
            private ImageView imageviewLink, imgcontact_cust_r;
            private Button buttonSendLink;

            ViewHolderAdapter(View v) {
                super(v);
                textvieProposalNumber = v.findViewById(R.id.textvieProposalNumber);
                textviewCustomerName = v.findViewById(R.id.textviewCustomerName);
                textviewTPAName = v.findViewById(R.id.textviewTPAName);
                textviewIntimationDate = v.findViewById(R.id.textviewIntimationDate);

                textviewMedicalDoneDate = v.findViewById(R.id.textviewMedicalDoneDate);
                textviewTestName = v.findViewById(R.id.textviewTestName);
                textviewMainStatus = v.findViewById(R.id.textviewMainStatus);
                textviewSubStatus = v.findViewById(R.id.textviewSubStatus);
                tvlinkScheduling = v.findViewById(R.id.tvlinkScheduling);

                textviewSubStatusOne = v.findViewById(R.id.textviewSubStatusOne);
                textviewCallingHistory = v.findViewById(R.id.textviewCallingHistory);
                textviewCallingRemarks = v.findViewById(R.id.textviewCallingRemarks);
                textviewMobileNumber = v.findViewById(R.id.textviewMobileNumber);
                textviewType = v.findViewById(R.id.textviewType);

                imageviewLink = v.findViewById(R.id.imageviewLink);
                imgcontact_cust_r = v.findViewById(R.id.imgcontact_cust_r);

                buttonSendLink = v.findViewById(R.id.buttonSendLink);

            }

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

    @Override
    protected void onDestroy() {
        killTasks();
        super.onDestroy();
    }


    private void killTasks() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }

        if (downloadViewMedicalStatusTeleMERAsync != null) {
            downloadViewMedicalStatusTeleMERAsync.cancel(true);
        }
        if (service != null) {
            service.cancel(true);
        }
    }

    class ViewMedicalStatusTeleMERValuesModel {

      /*
            <Data> <Table> <PROPOSAL_NO>7011560605</PROPOSAL_NO>
            <CUSTOMER_NAME>Sanjay Kumar Jain</CUSTOMER_NAME>
            <TPA_NAME>Health Assure</TPA_NAME>
            <INTIMATION_DATE>04-04-2020</INTIMATION_DATE>
            <MEDICAL_DONE_DATE>08-04-2020</MEDICAL_DONE_DATE>
            <TEST_NAME>Telemer</TEST_NAME>
            <MAIN_STATUS>Insurer Actionable </MAIN_STATUS>
            <SUB_STATUS>Open</SUB_STATUS>
            <SUB_STATUS1>Not responding</SUB_STATUS1>
            <CALLING_HISTORY>04-Apr-2020 12:31 PM 04-Apr-2020 04:12 PM 04-Apr-2020 07:36 PM 07-Apr-2020 05:11 PM 07-Apr-2020 05:11 PM</CALLING_HISTORY>
            <CALLING_REMARKS>LA's husband not ready to give phone to LA, said that after locked down call till yet no further calling &amp; disconnected the call</CALLING_REMARKS>
            <LINK_FOR_SCHEDULING>https://www.healthassure.in/TestScheduler/TeleRecallLink?ProposerId=v%2fFuJcLI%2bWsVpVUc2OILew%3d%3d</LINK_FOR_SCHEDULING>
            <TYPE>TeleMER</TYPE> </Table> </Data>
        */

        private String PROPOSAL_NO, CUSTOMER_NAME, TPA_NAME, INTIMATION_DATE,
                MEDICAL_DONE_DATE, TEST_NAME, MAIN_STATUS, SUB_STATUS, SUB_STATUS1,
                CALLING_HISTORY, CALLING_REMARKS, LINK_FOR_SCHEDULING, TYPE,MOBILE;

        public ViewMedicalStatusTeleMERValuesModel(String PROPOSAL_NO, String CUSTOMER_NAME, String TPA_NAME, String INTIMATION_DATE, String MEDICAL_DONE_DATE, String TEST_NAME, String MAIN_STATUS, String SUB_STATUS, String SUB_STATUS1, String CALLING_HISTORY, String CALLING_REMARKS, String LINK_FOR_SCHEDULING, String TYPE,String MOBILE) {
            this.PROPOSAL_NO = PROPOSAL_NO;
            this.CUSTOMER_NAME = CUSTOMER_NAME;
            this.TPA_NAME = TPA_NAME;
            this.INTIMATION_DATE = INTIMATION_DATE;
            this.MEDICAL_DONE_DATE = MEDICAL_DONE_DATE;
            this.TEST_NAME = TEST_NAME;
            this.MAIN_STATUS = MAIN_STATUS;
            this.SUB_STATUS = SUB_STATUS;
            this.SUB_STATUS1 = SUB_STATUS1;
            this.CALLING_HISTORY = CALLING_HISTORY;
            this.CALLING_REMARKS = CALLING_REMARKS;
            this.LINK_FOR_SCHEDULING = LINK_FOR_SCHEDULING;
            this.TYPE = TYPE;
            this.MOBILE = MOBILE;
        }

        public String getPROPOSAL_NO() {
            return PROPOSAL_NO;
        }

        public String getCUSTOMER_NAME() {
            return CUSTOMER_NAME;
        }

        public String getTPA_NAME() {
            return TPA_NAME;
        }

        public String getINTIMATION_DATE() {
            return INTIMATION_DATE;
        }

        public String getMEDICAL_DONE_DATE() {
            return MEDICAL_DONE_DATE;
        }

        public String getTEST_NAME() {
            return TEST_NAME;
        }

        public String getMAIN_STATUS() {
            return MAIN_STATUS;
        }

        public String getSUB_STATUS() {
            return SUB_STATUS;
        }

        public String getSUB_STATUS1() {
            return SUB_STATUS1;
        }

        public String getCALLING_HISTORY() {
            return CALLING_HISTORY;
        }

        public String getCALLING_REMARKS() {
            return CALLING_REMARKS;
        }

        public String getLINK_FOR_SCHEDULING() {
            return LINK_FOR_SCHEDULING;
        }

        public String getTYPE() {
            return TYPE;
        }

        public String getMOBILE() {
            return MOBILE;
        }
    }

    public ArrayList<ViewMedicalStatusTeleMERValuesModel> parseNodeViewMedicalStatusTeleMER(List<String> lsNode) {
        ArrayList<ViewMedicalStatusTeleMERValuesModel> lsData = new ArrayList<>();

        String PROPOSAL_NO, CUSTOMER_NAME, TPA_NAME, INTIMATION_DATE,
                MEDICAL_DONE_DATE, TEST_NAME, MAIN_STATUS, SUB_STATUS, SUB_STATUS1,
                CALLING_HISTORY, CALLING_REMARKS, LINK_FOR_SCHEDULING, TYPE,MOBILE;
        ParseXML prsObj = new ParseXML();

        for (String Node : lsNode) {

            PROPOSAL_NO = prsObj.parseXmlTag(Node, "PROPOSAL_NO");
            PROPOSAL_NO = PROPOSAL_NO == null ? "" : PROPOSAL_NO;

            CUSTOMER_NAME = prsObj.parseXmlTag(Node, "CUSTOMER_NAME");
            CUSTOMER_NAME = CUSTOMER_NAME == null ? "" : CUSTOMER_NAME;

            TPA_NAME = prsObj.parseXmlTag(Node, "TPA_NAME");
            TPA_NAME = TPA_NAME == null ? "" : TPA_NAME;

            INTIMATION_DATE = prsObj.parseXmlTag(Node, "INTIMATION_DATE");
            INTIMATION_DATE = INTIMATION_DATE == null ? "" : INTIMATION_DATE;

            MEDICAL_DONE_DATE = prsObj.parseXmlTag(Node, "MEDICAL_DONE_DATE");
            MEDICAL_DONE_DATE = MEDICAL_DONE_DATE == null ? "" : MEDICAL_DONE_DATE;

            TEST_NAME = prsObj.parseXmlTag(Node, "TEST_NAME");
            TEST_NAME = TEST_NAME == null ? "" : TEST_NAME;

            MAIN_STATUS = prsObj.parseXmlTag(Node, "MAIN_STATUS");
            MAIN_STATUS = MAIN_STATUS == null ? "" : MAIN_STATUS;

            SUB_STATUS = prsObj.parseXmlTag(Node, "SUB_STATUS");
            SUB_STATUS = SUB_STATUS == null ? "" : SUB_STATUS;

            SUB_STATUS1 = prsObj.parseXmlTag(Node, "SUB_STATUS1");
            SUB_STATUS1 = SUB_STATUS1 == null ? "" : SUB_STATUS1;

            CALLING_HISTORY = prsObj.parseXmlTag(Node, "CALLING_HISTORY");
            CALLING_HISTORY = CALLING_HISTORY == null ? "" : CALLING_HISTORY;

            CALLING_REMARKS = prsObj.parseXmlTag(Node, "CALLING_REMARKS");
            CALLING_REMARKS = CALLING_REMARKS == null ? "" : CALLING_REMARKS;

            LINK_FOR_SCHEDULING = prsObj.parseXmlTag(Node, "LINK_FOR_SCHEDULING");
            LINK_FOR_SCHEDULING = LINK_FOR_SCHEDULING == null ? "" : LINK_FOR_SCHEDULING;

            TYPE = prsObj.parseXmlTag(Node, "TYPE");
            TYPE = TYPE == null ? "" : TYPE;

            MOBILE = prsObj.parseXmlTag(Node, "MOBILE");
            MOBILE = MOBILE == null ? "" : MOBILE;

            ViewMedicalStatusTeleMERValuesModel nodeVal = new ViewMedicalStatusTeleMERValuesModel(PROPOSAL_NO, CUSTOMER_NAME, TPA_NAME, INTIMATION_DATE, MEDICAL_DONE_DATE, TEST_NAME, MAIN_STATUS, SUB_STATUS, SUB_STATUS1, CALLING_HISTORY, CALLING_REMARKS, LINK_FOR_SCHEDULING, TYPE,MOBILE);
            lsData.add(nodeVal);
        }
        return lsData;
    }
}
