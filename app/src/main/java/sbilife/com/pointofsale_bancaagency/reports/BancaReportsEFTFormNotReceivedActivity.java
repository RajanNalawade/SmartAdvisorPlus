package sbilife.com.pointofsale_bancaagency.reports;

import android.annotation.SuppressLint;
import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.List;

import sbilife.com.pointofsale_bancaagency.ParseXML;
import sbilife.com.pointofsale_bancaagency.R;
import sbilife.com.pointofsale_bancaagency.ServiceURL;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;

/**
 * Created by O0110 on 18/10/2017.
 */

public class BancaReportsEFTFormNotReceivedActivity extends AppCompatActivity implements ServiceHits.DownLoadData {
    private ProgressDialog mProgressDialog;
    private CommonMethods commonMethods;
    private Context context;
    private DownloadFileAsyncTaskEFTFormNotReceived taskEFTFormNotReceived;
    private  ServiceHits service;

    private  final String METHOD_NAME_EFT_NOT_RECEIVED_FORMS = "geteftformnot_received_smrt";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.eft_form_not_received);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);

        new CommonMethods().setApplicationToolbarMenu(this,"EFT Form Not Received");

        commonMethods = new CommonMethods();
        context = this;

        service_hits();
    }

    private void service_hits() {
        CommonMethods.UserDetailsValuesModel userDetails = commonMethods
                .setUserDetails(context);
        service = new ServiceHits(context, METHOD_NAME_EFT_NOT_RECEIVED_FORMS, "",
                userDetails.getStrCIFBDMUserId(), userDetails.getStrCIFBDMEmailId(),
                userDetails.getStrCIFBDMMObileNo() , userDetails.getStrCIFBDMPassword(), this);
        service.execute();
    }
    @Override
    public void downLoadData() {
        taskEFTFormNotReceived = new DownloadFileAsyncTaskEFTFormNotReceived();
        taskEFTFormNotReceived.execute();
    }

    class DownloadFileAsyncTaskEFTFormNotReceived extends AsyncTask<String, String, String> {
        private volatile boolean running = true;
        private  final String NAMESPACE = "http://tempuri.org/";
        private  final String URl = ServiceURL.SERVICE_URL;
        private  final String SOAP_ACTION_EFT_NOT_RECEIVED_FORMS = "http://tempuri.org/geteftformnot_received_smrt";

        private String inputpolicylist = "";
        int flag = 0;

        private List<ParseXML.EFTFormNotReceived> nodeData;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            mProgressDialog = new ProgressDialog(context,
                    ProgressDialog.THEME_HOLO_LIGHT);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog
                    .setTitle(Html
                            .fromHtml("<font color='#00a1e3'><b>Please Wait...<b></font>"));

            mProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... aurl) {

            try {
                running = true;

                SoapObject request = new SoapObject(NAMESPACE,
                        METHOD_NAME_EFT_NOT_RECEIVED_FORMS);
                request.addProperty("strCode", commonMethods.setUserDetails(context).getStrCIFBDMUserId());

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);

                HttpTransportSE androidHttpTranport = new HttpTransportSE(URl);
                androidHttpTranport.call(SOAP_ACTION_EFT_NOT_RECEIVED_FORMS,
                        envelope);

                SoapPrimitive sa = null;

                sa = (SoapPrimitive) envelope.getResponse();
                inputpolicylist = sa.toString();

                if (inputpolicylist.equalsIgnoreCase("0")) {
                    flag = 0;
                } else {
                    flag = 1;
                    ParseXML prsObj = new ParseXML();
                    inputpolicylist = prsObj.parseXmlTag(inputpolicylist, "Data");

                    if (inputpolicylist != null) {
                        List<String> Node = prsObj.parseParentNode(
                                inputpolicylist, "Table");
                        nodeData = prsObj.parseNodeEFTFormNotReceivedNode(Node);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
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
                if (flag == 1) {
                    ListView lv = findViewById(R.id.listviewEFTFormsNotReceived);
                    SelectedAdapter adapter = new SelectedAdapter(nodeData);
                    lv.setAdapter(adapter);

                } else {
                    commonMethods.showMessageDialog(context, "No record found");
                }
            } else {
                commonMethods.showMessageDialog(context, "No record found");
            }
        }
    }


    class SelectedAdapter extends ArrayAdapter<ParseXML.EFTFormNotReceived> {

        private final List<ParseXML.EFTFormNotReceived> lst;

        SelectedAdapter(List<ParseXML.EFTFormNotReceived> objects) {
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
                v = vi.inflate(R.layout.list_item_eft_form_not_received, null);
            }

            TextView textviewEFTFormNotReceivedProposalNO = v
                    .findViewById(R.id.textviewEFTFormNotReceivedProposalNO);
            TextView textviewEFTFormNotReceivedTAT = v
                    .findViewById(R.id.textviewEFTFormNotReceivedTAT);
            TextView textviewEFTFormNotReceivedBankBranchName = v
                    .findViewById(R.id.textviewEFTFormNotReceivedBankBranchName);

            TextView textviewEFTFormNotReceivedCashieringDate = v
                    .findViewById(R.id.textviewEFTFormNotReceivedCashieringDate);
            TextView textviewEFTFormNotReceivedCIFName = v
                    .findViewById(R.id.textviewEFTFormNotReceivedCIFName);
            TextView textviewEFTFormNotReceivedCIFCode = v
                    .findViewById(R.id.textviewEFTFormNotReceivedCIFCode);

            textviewEFTFormNotReceivedProposalNO.setText(lst.get(position).getPROPOSAL_NO());
            textviewEFTFormNotReceivedTAT.setText(lst.get(position).getTAT());
            textviewEFTFormNotReceivedBankBranchName.setText(lst.get(position).getBANK_BRANCH_NAME());

            textviewEFTFormNotReceivedCashieringDate.setText(lst.get(position).getCASHIERINGDATE());
            textviewEFTFormNotReceivedCIFName.setText(lst.get(position).getCIF_NAME());
            textviewEFTFormNotReceivedCIFCode.setText(lst.get(position).getCIF_CODE());


            return (v);
        }
    }

    @Override
    protected void onDestroy() {
        killTask();
        super.onDestroy();
    }

    private void killTask() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }

        if (taskEFTFormNotReceived != null) {
            taskEFTFormNotReceived.cancel(true);
        }

        if (service != null) {
            service.cancel(true);
        }

    }

}
