package sbilife.com.pointofsale_bancaagency.eftnotification;

import android.annotation.SuppressLint;
import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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
import android.widget.TableLayout;
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
import sbilife.com.pointofsale_bancaagency.home.CarouselHomeActivity;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;

/**
 * Created by O0110 on 14/09/2017.
 */

public class EFTPendingFormListActivity extends AppCompatActivity {


    private ProgressDialog mProgressDialog;
    private CommonMethods commonMethods;
    private Context context;
    private TableLayout tableLayoutEFTCashieredCases;
    private DownloadFileAsyncTaskEFTPendingForm taskEFTPendingForm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.eft_pending_form);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);

        new CommonMethods().setApplicationToolbarMenu(this,"EFT Pending Forms");

        tableLayoutEFTCashieredCases  = findViewById(R.id.tableLayoutEFTCashieredCases);
        tableLayoutEFTCashieredCases.setVisibility(View.GONE);

        commonMethods = new CommonMethods();
        context = this;

        taskEFTPendingForm = new DownloadFileAsyncTaskEFTPendingForm();
        taskEFTPendingForm.execute();
    }

    class DownloadFileAsyncTaskEFTPendingForm extends AsyncTask<String, String, String> {
        private volatile boolean running = true;
        private  final String NAMESPACE = "http://tempuri.org/";
        private  final String URl = ServiceURL.SERVICE_URL;

        private  final String SOAP_ACTION_EFT_NOT_RECEIVED_FORMS = "http://tempuri.org/getnonsubmissionform_smrt";
        private  final String METHOD_NAME_EFT_NOT_RECEIVED_FORMS = "getnonsubmissionform_smrt";
        private String inputpolicylist = "";
        int flag = 0;

        List<ParseXML.EFTPendingForm> nodeData;


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
                        nodeData = prsObj.parseNodeEFTNode(Node);
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

                    tableLayoutEFTCashieredCases.setVisibility(View.VISIBLE);
                    ListView lv = findViewById(R.id.listviewEFTForms);
                    SelectedAdapter adapter = new SelectedAdapter(0, nodeData);
                    lv.setAdapter(adapter);

                } else {
                    commonMethods.showMessageDialog(context, "No record found");
                }
            } else {
                commonMethods.showMessageDialog(context, "No record found");
            }
        }
    }


    class SelectedAdapter extends ArrayAdapter<ParseXML.EFTPendingForm> {

        private int selectedPos = -1; // init value for not-selected
        private List<ParseXML.EFTPendingForm> lst;

        SelectedAdapter(int textViewResourceId,
                        List<ParseXML.EFTPendingForm> objects) {
            super(context, textViewResourceId, objects);
            lst = objects;
        }

        public void setSelectedPosition(int pos) {
            selectedPos = pos;
            notifyDataSetChanged();
        }

        public int getSelectedPosition() {
            return selectedPos;
        }

        @SuppressLint("InflateParams")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;

            if (v == null) {
                LayoutInflater vi = (LayoutInflater) this.getContext()
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.eft_listview_items, null);
            }

            TextView textviewPolicyNumberEFT = v
                    .findViewById(R.id.textviewPolicyNumberEFT);
            TextView textviewFullNameEFT = v
                    .findViewById(R.id.textviewFullNameEFT);
            TextView textviewCashieringDateEFT = v
                    .findViewById(R.id.textviewCashieringDateEFT);

            Object obj = null;
            boolean i = lst.contains(obj);

            if (!i) {
                textviewPolicyNumberEFT.setText(lst.get(position).getPOLICY_NO());
                textviewFullNameEFT.setText(lst.get(position).getFULLNAME());
                textviewCashieringDateEFT.setText(lst.get(position).getCASHIERINGDATE());

            }

            return (v);
        }
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(context, CarouselHomeActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        finish();
    }

    @Override
    protected void onDestroy() {
        if(mProgressDialog!=null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }

        if(taskEFTPendingForm!=null){
            taskEFTPendingForm.cancel(true);
        }
        super.onDestroy();
    }
}

