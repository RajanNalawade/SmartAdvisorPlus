package sbilife.com.pointofsale_bancaagency.home.covid19;

import android.annotation.SuppressLint;
import androidx.appcompat.app.AppCompatActivity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import sbilife.com.pointofsale_bancaagency.ParseXML;
import sbilife.com.pointofsale_bancaagency.R;
import sbilife.com.pointofsale_bancaagency.ServiceURL;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;
import sbilife.com.pointofsale_bancaagency.common.SendSmsAsyncTask;
import sbilife.com.pointofsale_bancaagency.reports.ServiceHits;

public class Covid19QuestionnaireActivity extends AppCompatActivity implements View.OnClickListener, ServiceHits.DownLoadData {
    private ProgressDialog mProgressDialog;
    private final String METHOD_NAME = "getProposalDetail_smrt";
    private Button btnSubmitProposal;
    private EditText editextCovidProposalNumber;
    private Context context;
    private CommonMethods commonMethods;
    private String proposalNumber = "";
    private String strCIFBDMUserId = "";
    private String strCIFBDMEmailId = "";
    private String strCIFBDMMObileNo = "";
    private String EMAILID = "";
    private ServiceHits service;
    private DownloadCovid19AsyncTask downloadCovid19AsyncTask;
    private TextView textviewMobileNumber;
    private LinearLayout llProposalDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_covid_n_questionnaire);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);

        context = this;
        commonMethods = new CommonMethods();
        commonMethods.setApplicationToolbarMenu(this, "Covid-19 Questionnaire");
        btnSubmitProposal = findViewById(R.id.btnSubmitProposal);
        editextCovidProposalNumber = findViewById(R.id.editextCovidProposalNumber);
        textviewMobileNumber = findViewById(R.id.textviewMobileNumber);
        llProposalDetails = findViewById(R.id.llProposalDetails);
        llProposalDetails.setVisibility(View.GONE);
        btnSubmitProposal.setOnClickListener(this);
        mProgressDialog = new ProgressDialog(context, ProgressDialog.THEME_HOLO_LIGHT);
        String Message = "Loading Please wait...";
        mProgressDialog.setMessage(Html.fromHtml("<font color='#00a1e3'><b>" + Message + "<b></font>"));
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(true);

        mProgressDialog.setButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (downloadCovid19AsyncTask != null) {
                            downloadCovid19AsyncTask.cancel(true);
                        }
                        if (mProgressDialog != null) {
                            if (mProgressDialog.isShowing()) {
                                mProgressDialog.dismiss();
                            }
                        }
                    }
                });

        mProgressDialog.setMax(100);

        Button buttonCallMobile, buttonSendSMS, buttonShare;
        buttonCallMobile = findViewById(R.id.buttonCallMobile);
        buttonSendSMS = findViewById(R.id.buttonSendSMS);
        buttonShare = findViewById(R.id.buttonShare);

        buttonCallMobile.setOnClickListener(this);
        buttonSendSMS.setOnClickListener(this);
        buttonShare.setOnClickListener(this);
    }

    private void service_hits() {
        CommonMethods.UserDetailsValuesModel userDetailsValuesModel = commonMethods.setUserDetails(context);
        String input = strCIFBDMUserId + "," + proposalNumber;
        strCIFBDMUserId = userDetailsValuesModel.getStrCIFBDMUserId();
        strCIFBDMEmailId = userDetailsValuesModel.getStrCIFBDMEmailId();
        String strCIFBDMPassword = commonMethods.getStrAuth();
        strCIFBDMMObileNo = userDetailsValuesModel.getStrCIFBDMMObileNo();
        service = new ServiceHits(context, METHOD_NAME, input,
                strCIFBDMUserId, strCIFBDMEmailId,
                strCIFBDMMObileNo, strCIFBDMPassword, this);
        service.execute();
    }

    @Override
    public void downLoadData() {
        downloadCovid19AsyncTask = new DownloadCovid19AsyncTask();
        downloadCovid19AsyncTask.execute();
    }

    //you covid-19 qu is already submitted
    @Override
    public void onClick(View v) {
        if (commonMethods.isNetworkConnected(context)) {
            if (v.getId() == R.id.btnSubmitProposal) {
                llProposalDetails.setVisibility(View.GONE);
                proposalNumber = editextCovidProposalNumber.getText().toString();
                commonMethods.hideKeyboard(editextCovidProposalNumber, context);
                if (!TextUtils.isEmpty(proposalNumber)) {
                    service_hits();
                } else {
                    commonMethods.showMessageDialog(context, "Please enter Proposal Number");
                }

            } else {
                String mobileNumber = textviewMobileNumber.getText().toString();
                if (v.getId() == R.id.buttonCallMobile) {
                    if (!TextUtils.isEmpty(mobileNumber)) {
                        commonMethods.callMobileNumber(mobileNumber, context);
                    }
                } else if (v.getId() == R.id.buttonSendSMS) {
                    if (!TextUtils.isEmpty(mobileNumber)) {
                        confirmDialog("", "SMS", mobileNumber);
                    }
                } else if (v.getId() == R.id.buttonShare) {
                    confirmDialog(EMAILID, "Share", "");
                }
            }
        } else {
            commonMethods.showMessageDialog(context, commonMethods.NO_INTERNET_MESSAGE);
        }
    }

    class DownloadCovid19AsyncTask extends AsyncTask<String, String, String> {

        private volatile boolean running = true;
        private String error = "", NAME = "", MOBILE = "";
        private TextView textviewName;

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
                //string strCode, string strProposalNo, string strEmailId, string strMobileNo, string strAuthKey
                String NAMESPACE = "http://tempuri.org/";
                request = new SoapObject(NAMESPACE, METHOD_NAME);
                request.addProperty("strCode", strCIFBDMUserId);
                request.addProperty("strProposalNo", proposalNumber);
                commonMethods.appendSecurityParams(context, request, strCIFBDMEmailId, strCIFBDMMObileNo);
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

                    if (inputpolicylist != null && inputpolicylist.equalsIgnoreCase("2")) {
                        error = "2";
                    } else {
                        inputpolicylist = prsObj.parseXmlTag(inputpolicylist, "NewDataSet");
                        error = prsObj.parseXmlTag(
                                inputpolicylist, "ScreenData");

                        if (error == null) {
                            // for agent policy list
                            //<NewDataSet> <Table>
                            //String node = prsObj.parseParentNode(inputpolicylist, "Table");
                            NAME = prsObj.parseXmlTag(inputpolicylist, "NAME");
                            MOBILE = prsObj.parseXmlTag(inputpolicylist, "MOBILE");
                            EMAILID = prsObj.parseXmlTag(inputpolicylist, "EMAILID");

                            error = "success";
                        } else {
                            error = "1";
                        }
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
            llProposalDetails.setVisibility(View.GONE);
            if (running) {
                if (error.equalsIgnoreCase("success")) {
                    llProposalDetails.setVisibility(View.VISIBLE);
                    textviewName = findViewById(R.id.textviewName);
                    textviewMobileNumber.setText(MOBILE);
                    textviewName.setText(NAME);
                } else if (error.equalsIgnoreCase("2")) {
                    commonMethods.showMessageDialog(context, "Your Covid-19 Questionnaire already submitted");
                } else {
                    commonMethods.showMessageDialog(context, "No Record found");
                }
            } else {
                commonMethods.showMessageDialog(context, "No Record found");
            }
        }
    }

    private void confirmDialog(final String EMAILID, final String flag, final String mobileNumber) {

        //String link = "https://smartadvisor.sbilife.co.in";?data={"branchCode":"GOA"}


        /*final String link = "Dear  Customer, Greetings from SBI Life. We hope you and your dear ones are safe. As a part of the proposal, " +
                "we request you to complete and submit the health declaration by clicking on the below link.\n\n" +
                "https://smartadvisor.sbilife.co.in/covid/Questionnaire/Questionnaire.html?proposalNo=" + proposalNumber + "\n\n" +
                "Please note that insurance risk will commence only after the proposal is duly accepted by us. We implore you to stay protected.";*/

        final String link = "Dear  Customer, as a part of the proposal, " +
                "we request you to complete and submit the health declaration by clicking on the below link.\n" +
                "https://smartadvisor.sbilife.co.in/covid/Questionnaire/Questionnaire.html?proposalNo=" + proposalNumber + "\n" +
                "Please note that insurance risk will commence only after the proposal is duly accepted by us.";
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_yes_no);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Button bt_yes = dialog.findViewById(R.id.bt_yes);
        Button bt_no = dialog.findViewById(R.id.bt_no);
        TextView tv_title = dialog.findViewById(R.id.tv_title);
        tv_title.setTextIsSelectable(true);
        tv_title.setText(link);

        if (flag.equalsIgnoreCase("SMS")) {
            bt_yes.setText("Send");
        } else {
            bt_yes.setText("Share");
        }

        bt_no.setText("Cancel");
        bt_yes.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (flag.equalsIgnoreCase("SMS")) {
                    SendSmsAsyncTask sendSmsAsyncTask = new SendSmsAsyncTask(context, mobileNumber, link);
                    sendSmsAsyncTask.execute("");
                } else {
                    String[] addresses = {EMAILID};
                    try {
                        Intent shareIntent = new Intent(Intent.ACTION_SEND);
                        shareIntent.setType("text/plain");
                        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Covid 19 Questionnaire");
                        shareIntent.putExtra(Intent.EXTRA_TEXT, link);
                        shareIntent.putExtra(Intent.EXTRA_EMAIL, addresses);
                        startActivity(Intent.createChooser(shareIntent, "choose one"));
                    } catch (Exception e) {
                        //e.toString();
                    }
                }


            }
        });
        bt_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void composeEmail(String[] addresses, String subject, String body) {
        try {

            String title = "Open with";
            // File filelocation = new File(commonMethods.getDirectory().getAbsolutePath(), strFileName);
            // Uri path = Uri.fromFile(filelocation);
            Intent emailIntent = new Intent(Intent.ACTION_SEND);
            // set the type to 'email'
            emailIntent.setType("vnd.android.cursor.dir/email");
            emailIntent.putExtra(Intent.EXTRA_EMAIL, addresses);
            // the attachment
            //emailIntent .putExtra(Intent.EXTRA_STREAM, path);
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
            emailIntent.putExtra(Intent.EXTRA_TEXT, body);
            Intent chooser = Intent.createChooser(emailIntent, title);

            if (emailIntent.resolveActivity(getPackageManager()) != null) {
                startActivity(chooser);
            }


        } catch (SecurityException e) {
            commonMethods.showMessageDialog(context, "There might be issue in Permissions");
        } catch (Exception e) {
            commonMethods.showMessageDialog(context, "Something went wrong");
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

        if (downloadCovid19AsyncTask != null) {
            downloadCovid19AsyncTask.cancel(true);
        }

        if (service != null) {
            service.cancel(true);
        }
    }
}
